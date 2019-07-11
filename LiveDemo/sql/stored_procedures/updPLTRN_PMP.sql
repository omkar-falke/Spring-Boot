
-- procedure to create / update data in Party Ledger transaction using Payment Receipt record
-- This procedure is to be executed whenever the payment receipt is authorised.
-- Procedure is called through mr_tepmr.java

-- FEATURES INTRODUCED
--  When Payment Receipt is authorised, A single transaction is introduced in MR_PLTRN (Party Ledger) with Total Payment Receipt Value.
-- New record generated will have
--        Party Group Code from Payment Receipt transaction.
--        Next Sequence Number (Transaction Processing sequence number for the Party Group)
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Payment Receipt Amount from Closing balance of previous sequence number)




drop procedure spldata.updPLTRN_PMP;
commit;

create procedure spldata.updPLTRN_PMP(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_DOCNO char(8))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_DOCTP char(2);
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
  declare L_CNSCD char(6);
  declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;
  declare L_PLTRN_PUSH char(1);

  declare L_DBTTP char(2);
  declare L_DBTNO char(8);
  declare L_PAYDT date;


  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_PMR cursor for  select  PR_PRTTP,PR_PRTCD, PR_DOCTP, PR_DOCDT, PR_SBSCD, PR_GRPCD,PR_CNSCD,sum(PR_RCTVL) PR_RCTVL from spldata.mr_prtrn   where   PR_MKTTP = LP_MKTTP and PR_DOCNO=LP_DOCNO and  PR_DOCTP in ('41')  group by PR_PRTTP,PR_PRTCD, PR_DOCTP,PR_DOCNO, PR_DOCDT, PR_SBSCD, PR_GRPCD,PR_CNSCD;

  declare C_PAD cursor for select PA_PRTTP,PA_PRTCD,PA_DBTTP,PA_DBTNO from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_MKTTP = LP_MKTTP and PA_CRDNO = LP_DOCNO order by PA_CMPCD, PA_PRTTP, PA_PRTCD, PA_DBTTP, PA_DBTNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_PMR ;
  fetch C_PMR into  L_PRTTP,L_PRTCD, L_DOCTP,  L_DOCDT, L_SBSCD, L_GRPCD,L_CNSCD,L_DOCVL;

  while END_TABLE = 0 DO 


      set L_SEQNO = 0;
      set L_BALVL = 0.00;
      set L_BALFL = 'DB';
      set L_BALVL1 = 0.00;
      set L_BALFL1 = 'DB';
      set L_ADJVL = 0.00;
      set L_ACCRF = '    ';
      set L_ACCDT = null;
      set L_TRNFL = '0';
      set L_STSFL = '1';
      set L_LUSBY = 'SYS';
      set L_LUPDT = current_date;


      select isnull(PL_SEQNO,0) PL_SEQNO into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = LP_DOCNO;
      -- If the Payment Receipt Record exists in Party ledger, only Payment Receipt value is over written and closing balance for subsequent transactions is reworked
      if L_SEQNO > 0 then 
          update spldata.mr_pltrn set PL_DOCVL = round(L_DOCVL,0) where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = LP_DOCNO;
          -- call spldata.rwkPLTRN_TRN(LP_CMPCD, L_PRTTP, L_PRTCD, L_DOCTP, LP_DOCNO, L_SEQNO);
      end if;
      --insert into spldata.temp_chk values('L_SEQNO : ' + char(L_SEQNO));
     if L_SEQNO = 0 then 

        select distinct PR_DOCDT into L_DOCDT from spldata.MR_PRTRN where PR_CMPCD = LP_CMPCD and PR_PRTTP = L_PRTTP and PR_PRTCD = L_PRTCD and PR_DOCNO = LP_DOCNO; 
        call spldata.rwkPLTRN_SRL(LP_CMPCD, L_PRTTP, L_PRTCD,L_DOCDT, L_DOCTP, LP_DOCNO,L_SEQNO);
        set L_PLTRN_PUSH = '0';
        if L_SEQNO > 0 then
            set L_PLTRN_PUSH = '1';
        end if;
        --insert into spldata.temp_chk values ('Received Seq.No. : ' + char(L_SEQNO));
        if L_PLTRN_PUSH = '0'  then     
           select max(isnull(PL_SEQNO,0))  into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;
           set L_SEQNO = isnull(L_SEQNO,0)+1;
        end if;


         if L_SEQNO > 1 then
             select  isnull(PL_BALVL,0), isnull(PL_BALFL,'DB')  into  L_BALVL, L_BALFL from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_SEQNO = L_SEQNO-1;
        else
           select  isnull(PT_YOPVL,0) PT_YOPVL, isnull(PT_YOPFL,'DB') PT_YOPFL  into   L_BALVL, L_BALFL from spldata.co_ptmst where pt_prttp= L_PRTTP and PT_PRTCD = L_PRTCD;
         end if;

        insert into spldata.temp_chk values (char(L_SEQNO) + '   ' + char(L_BALVL) + '    ' + char(L_BALFL) + '     ' + L_PRTCD);

        if L_BALFL = 'DB'  then
            set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
            set L_BALFL1 = 'DB';
        else 
           set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
           set L_BALFL1 = 'CR';
         end if;

        -- After deducting credit amount, if the balance goes negative,  Then it is a Credit Balancde
         if L_BALVL1 < 0 then
            set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
            set L_BALFL1 = 'DB';
         end if;

       insert into spldata.mr_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_MKTTP,PL_GRPCD,PL_CNSCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT) values  (LP_CMPCD,L_PRTTP,L_PRTCD,L_DOCTP,LP_DOCNO,L_SEQNO,L_DOCDT,round(L_DOCVL,0),round(L_BALVL1,0),L_BALFL1,round(L_ADJVL,0),L_ACCRF,L_ACCDT,LP_MKTTP,L_GRPCD,L_CNSCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
      update spldata.co_ptmst set pt_ytdvl = round(L_BALVL1,0), pt_ytdfl = L_BALFL1  where pt_prttp = L_PRTTP and PT_PRTCD = L_PRTCD;

       if L_PLTRN_PUSH = '1' then
          call spldata.rwkPLTRN_TRN(LP_CMPCD,L_PRTTP, L_PRTCD, L_DOCTP, LP_DOCNO, L_SEQNO);
       end if;
      end if;
      set END_TABLE = 0;
  fetch C_PMR into  L_PRTTP,L_PRTCD, L_DOCTP,  L_DOCDT, L_SBSCD, L_GRPCD, L_DOCVL;
  end while;
  close C_PMR;

  OPEN c_pad;
  fetch C_PAD into L_PRTTP,L_PRTCD,L_DBTTP,L_DBTNO;
  set END_TABLE = 0;
  while END_TABLE = 0 do
      select max(PL_DOCDT) PL_DOCDT into L_PAYDT from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP=L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCNO in (select PA_CRDNO from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_PRTTP = L_PRTTP and PA_PRTCD=L_PRTCD and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO) ;
      update spldata.MR_PLTRN set PL_PAYDT = L_PAYDT where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DBTTp and PL_DOCNO = L_DBTNO;
      set END_TABLE = 0;
      fetch C_PAD into L_PRTTP,L_PRTCD,L_DBTTP,L_DBTNO;
  end while;
  close C_PAD;
  select sum(isnull(PA_ADJVL,0)) into L_ADJVL from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_CRDNO = LP_DOCNO;
  update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL where PL_CMPCD = LP_CMPCD and PL_DOCNO = LP_DOCNO;
  --commit;
end P1;

commit;




call spldata.updPLTRN_PMP('01','01','71301232');
commit;
