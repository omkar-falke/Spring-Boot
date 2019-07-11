
-- procedure to create/update data in Party Ledger transaction using Credit Note & Debit Note details
-- This procedure is to be executed whenever the invoice is approved for Gate-out.
-- Procedure is called through mm_teexa.java

-- FEATURES INTRODUCED
--  When Credit Note / Debit Note is gemerated, A single transaction is introduced in MR_PLTRN (Party Ledger) with Total Credit / Debit Note Value.
-- New record generated will have
--        Party Code from Credit Note transaction.
--        Next Sequence Number (Transaction Processing sequence number for the Party Group)
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Credit Note Amount from Closing balance of previous sequence number)

;
commit;
drop procedure spldata.updPLTRN_FGR;
commit;

create procedure spldata.updPLTRN_FGR(IN LP_CMPCD char(2), IN LP_INVNO char(8))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_MKTTP char(2);
  declare L_DOCTP char(2);
  declare L_DOCTP_CR char(2) default '04';            -- Sales Return credit note category
  declare L_DOCTP_DB char(2) default '32';            -- Sales Return debit note category
  declare L_DOCNO char(8);
  declare L_DOCDT date;
  declare L_SEQNO decimal(10);
  declare L_DOCVL decimal(12,2);
  declare L_BALVL decimal(12,2);
  declare L_BALFL char(2);
  declare L_BALVL1 decimal(12,2);
  declare L_BALFL1 char(2);
  declare L_ADJVL decimal(12,2);
  declare L_ACCRF char(8);
  declare L_ACCDT date;
  declare L_BYRCD char(5);
  declare L_GRPCD char(5);
  declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;
  declare L_PLTRN_PUSH char(1);

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_PTT cursor for  select  PT_MKTTP,PT_PRTTP,PT_PRTCD, PT_CRDTP,PT_DOCRF, PT_DOCDT, PT_SBSCD, PT_GRPCD,sum(PT_PNTVL) PT_PNTVL from spldata.mr_pttrn   where   PT_CMPCD = LP_CMPCD and PT_INVNO=LP_INVNO and  PT_STSFL <> 'X' and PT_CRDTP in (L_DOCTP_CR,L_DOCTP_DB)  group by PT_MKTTP,PT_PRTTP,PT_PRTCD, PT_CRDTP, PT_DOCRF, PT_DOCDT, PT_SBSCD, PT_GRPCD;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_PTT ;
  fetch C_PTT into  L_MKTTP,L_PRTTP,L_PRTCD, L_DOCTP, L_DOCNO, L_DOCDT, L_SBSCD, L_GRPCD, L_DOCVL;

  while END_TABLE = 0 DO 
      if L_DOCTP = L_DOCTP_CR then
         set L_BALFL = 'CR';
      end if;
      if L_DOCTP = L_DOCTP_DB then
         set L_BALFL = 'DB';
      end if;

      set L_SEQNO = 0;
      set L_BALVL = 0.00;
      set L_BALVL1 = 0.00;
      set L_BALFL1 = 'CR';
      set L_ADJVL = 0.00;
      set L_ACCRF = '    ';
      set L_ACCDT = null;
      set L_TRNFL = '0';
      set L_STSFL = '1';
      set L_LUSBY = 'SYS';
      set L_LUPDT = current_date;

      select isnull(PL_SEQNO,0) PL_SEQNO into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = L_DOCNO;


      -- If the Credit Note Record exists in payment ledger, only Credit Note value is over written and closing balance is reworked for subsequent transactions
      if L_SEQNO > 0 then 
          update spldata.mr_pltrn set PL_DOCVL = L_DOCVL where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = L_DOCNO;
          --call spldata.rwkPLTRN_TRN(LP_CMPCD, L_PRTTP, L_PRTCD, L_DOCTP, L_DOCNO, L_SEQNO);
      end if;


     if L_SEQNO = 0 then 
        --select max(isnull(PL_SEQNO,0))  into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;
        --set L_SEQNO = isnull(L_SEQNO,0)+1;

        select distinct PT_DOCDT into L_DOCDT from spldata.MR_PTTRN where PT_CMPCD = LP_CMPCD and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_DOCRF = L_DOCNO; 
        call spldata.rwkPLTRN_SRL(LP_CMPCD, L_PRTTP, L_PRTCD,L_DOCDT, L_DOCTP, L_DOCNO,L_SEQNO);
        set L_PLTRN_PUSH = '0';
        if L_SEQNO > 0 then
            set L_PLTRN_PUSH = '1';
        end if;
        --insert into spldata.temp_chk values ('Received Seq.No. : '||char(L_SEQNO));
        if L_PLTRN_PUSH = '0'  then     
           select max(isnull(PL_SEQNO,0))  into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;
           set L_SEQNO = isnull(L_SEQNO,0)+1;
        end if;


         if L_SEQNO > 1 then
             select  isnull(PL_BALVL,0), isnull(PL_BALFL,'DB')  into  L_BALVL, L_BALFL from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_SEQNO = L_SEQNO-1;
        else
           select  isnull(PT_YOPVL,0) PT_YOPVL, isnull(PT_YOPFL,'DB') PT_YOPFL  into   L_BALVL, L_BALFL from spldata.co_ptmst where pt_prttp= L_PRTTP and PT_PRTCD = L_PRTCD;
         end if;

       if L_DOCTP = L_DOCTP_CR then 
          if L_BALFL = 'CR'  then
             set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
             set L_BALFL1 = 'CR';
          else 
             set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
             set L_BALFL1 = 'DB';
          end if;
        -- After deducting credit amount, if the balance goes negative,  Then it is a Credit Balancde
         if L_BALVL1 < 0 then
            set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
            set L_BALFL1 = 'CR';
         end if;


       end if;

       if L_DOCTP = L_DOCTP_DB then 
          if L_BALFL = 'CR'  then
             set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
             set L_BALFL1 = 'CR';
          else 
             set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
             set L_BALFL1 = 'DB';
          end if;
        -- After deducting credit amount, if the balance goes negative,  Then it is a Credit Balancde
         if L_BALVL1 < 0 then
            set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
            set L_BALFL1 = 'DB';
         end if;

       end if;




       insert into spldata.mr_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_MKTTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT,PL_DUEDT) values  (LP_CMPCD,L_PRTTP,L_PRTCD,L_DOCTP,L_DOCNO,L_SEQNO,L_DOCDT,L_DOCVL,L_BALVL1,L_BALFL1,L_ADJVL,L_ACCRF,L_ACCDT,L_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT,L_DOCDT);
      update spldata.co_ptmst set pt_ytdvl = L_BALVL1, pt_ytdfl = L_BALFL1 where pt_prttp = L_PRTTP and PT_PRTCD = L_PRTCD;
       --if L_PLTRN_PUSH = '1' then
       --   call spldata.rwkPLTRN_TRN(LP_CMPCD,L_PRTTP, L_PRTCD, L_DOCTP, L_DOCNO, L_SEQNO);
       --end if;


      end if;
    set END_TABLE = 0;
    fetch C_PTT into  L_MKTTP,L_PRTTP,L_PRTCD, L_DOCTP, L_DOCNO, L_DOCDT, L_SBSCD, L_GRPCD, L_DOCVL;
  end while;
  close C_PTT;
  --commit;
end P1;
commit;




commit;
call spldata.updPLTRN_FGR('01','70010913');
commit;

