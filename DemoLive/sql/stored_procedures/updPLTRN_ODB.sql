
-- procedure to create/update data in Party Ledger transaction using Credit Note & Debit Note details 
-- This procedure is to be executed during Saving / Authorisation of OTHER DEBIT NOTES
-- Procedure is called through mr_tecrn.java

-- FEATURES INTRODUCED
--  When Credit Note / Debit Note is gemerated, A single transaction is introduced in MR_PLTRN (Party Ledger) with Total Credit / Debit Note Value.
-- New record generated will have
--        Party Code from Credit Note transaction.
--        Next Sequence Number (Transaction Processing sequence number for the Party Group)
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Credit Note Amount from Closing balance of previous sequence number)

;

drop procedure spldata.updPLTRN_ODB;
commit;

create procedure spldata.updPLTRN_ODB(IN LP_CMPCD char(2), IN LP_DOCRF char(8))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_DOCTP char(2);
  declare L_MKTTP char(2);
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

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_PTT cursor for  select  PT_PRTTP,PT_PRTCD, PT_CRDTP,PT_DOCRF, PT_DOCDT, PT_SBSCD, PT_GRPCD,PT_MKTTP,round(sum(isnull(PT_PNTVL,0)),0) PT_PNTVL from spldata.mr_pttrn   where    PT_DOCRF=LP_DOCRF and  PT_STSFL <> 'X' and PT_CRDTP in ('31','38','39')  group by PT_PRTTP,PT_PRTCD, PT_CRDTP, PT_DOCRF, PT_DOCDT, PT_SBSCD, PT_GRPCD,PT_MKTTP;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_PTT ;
  fetch C_PTT into  L_PRTTP,L_PRTCD, L_DOCTP, L_DOCNO, L_DOCDT, L_SBSCD, L_GRPCD,L_MKTTP, L_DOCVL;

  while END_TABLE = 0 DO 


      set L_SEQNO = 0;
      set L_BALVL = 0.00;
      set L_BALFL = 'DB';
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
          call spldata.rwkPLTRN_TRN(LP_CMPCD, L_PRTTP, L_PRTCD, L_DOCTP, L_DOCNO, L_SEQNO);
      end if;


     if L_SEQNO =0 then 
        select max(isnull(PL_SEQNO,0))  into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;
         set L_SEQNO = isnull(L_SEQNO,0);
         if L_SEQNO > 0 then
             select  isnull(PL_BALVL,0), isnull(PL_BALFL,'DB')  into  L_BALVL, L_BALFL from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_SEQNO = L_SEQNO;
         end if;

        if L_BALFL = 'CR'  then
            set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
            set L_BALFL1 = 'CR';
        else 
           set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
           set L_BALFL1 = 'DB';
         end if;

        -- After deducting debit amount from credit balance, if the balance goes negative,  Then it is a Debit Balancde
         if L_BALVL1 < 0 then
            set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
            set L_BALFL1 = 'DB';
         end if;

       insert into spldata.mr_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_MKTTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT,PL_DUEDT)  values  (LP_CMPCD,L_PRTTP,L_PRTCD,L_DOCTP,L_DOCNO,L_SEQNO+1,L_DOCDT,L_DOCVL,L_BALVL1,L_BALFL1,L_ADJVL,L_ACCRF,L_ACCDT,L_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT,L_DOCDT);
      update spldata.co_ptmst set pt_ytdvl = L_BALVL1, pt_ytdfl = L_BALFL1 where pt_prttp = L_PRTTP and PT_PRTCD = L_PRTCD;

      end if;
      set END_TABLE = 0;
  fetch C_PTT into  L_PRTTP,L_PRTCD, L_DOCTP, L_DOCNO, L_DOCDT, L_SBSCD, L_GRPCD, L_MKTTP,L_DOCVL;
  end while;
  close C_PTT;
  --commit;
end P1;


commit;

commit;
call spldata.updPLTRN_ODB('01','73900150');	
commit;
