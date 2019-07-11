
-- procedure to create / update data in Party Ledger transaction using Payment Receipt record
-- This procedure is to be executed whenever the payment receipt is authorised.
-- Procedure is called through mm_tepmt.java

-- FEATURES INTRODUCED
--  When Payment Receipt is authorised, A single transaction is introduced in MM_PLTRN (Party Ledger) with Total Payment Receipt Value.
-- New record generated will have
--        Party Group Code from Payment Receipt transaction.
--        Next Sequence Number (Transaction Processing sequence number for the Party Group)
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Payment Receipt Amount from Closing balance of previous sequence number)




drop procedure SPLDATA.updPLTRN_PMR_MM;
commit;

create procedure splDATA.updPLTRN_PMR_MM(IN LP_CMPCD char(2),IN LP_BILTP char(2), IN LP_DOCNO char(8))  language sql  modifies sql data 
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
--  declare L_BYRCD char(5);
  declare L_GRPCD char(5);
--  declare L_CNSCD char(6);
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

  declare C_PMR_MM cursor for  select  PR_PRTTP,PR_PRTCD, PR_DOCTP, PR_DOCDT, PR_SBSCD, PR_GRPCD,sum(PR_RCTVL) PR_RCTVL from SPLDATA.MM_PRTRN   where   PR_CMPCD = LP_CMPCD  and PR_DOCNO=LP_DOCNO and  PR_DOCTP in ('11','12','13','19')  group by PR_PRTTP,PR_PRTCD, PR_DOCTP, PR_DOCDT, PR_SBSCD, PR_GRPCD;

  declare C_PAD_MM cursor for select PA_PRTTP,PA_PRTCD,PA_DBTTP,PA_DBTNO from SPLDATA.MM_PATRN where PA_CMPCD = LP_CMPCD  and PA_CRDNO = LP_DOCNO order by PA_CMPCD, PA_PRTTP, PA_PRTCD, PA_DBTTP, PA_DBTNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_PMR_MM ;
  fetch C_PMR_MM into  L_PRTTP,L_PRTCD, L_DOCTP,  L_DOCDT, L_SBSCD, L_GRPCD,L_DOCVL;

  while END_TABLE = 0 DO 


      set L_SEQNO = 0;
      set L_BALVL = 0.00;
      set L_BALFL = 'CR';
      set L_BALVL1 = 0.00;
      set L_BALFL1 = 'CR';
      set L_ADJVL = 0.00;
      set L_ACCRF = '    ';
      set L_ACCDT = null;
      set L_TRNFL = '0';
      set L_STSFL = '1';
      set L_LUSBY = 'SYS';
      set L_LUPDT = current_date;


      select isnull(PL_SEQNO,0) PL_SEQNO into L_SEQNO from SPLDATA.MM_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = LP_DOCNO;

       select  max(isnull(PL_SEQNO,0)) PL_SEQNO into L_SEQNO from SPLDATA.MM_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;

        select distinct PR_DOCDT into L_DOCDT from SPLDATA.MM_PRTRN where PR_CMPCD = LP_CMPCD and PR_PRTTP = L_PRTTP and PR_PRTCD = L_PRTCD and PR_DOCNO = LP_DOCNO; 
         set L_SEQNO = isnull(L_SEQNO,0)+1;


         if L_SEQNO > 1 then
             select  isnull(PL_BALVL,0), isnull(PL_BALFL,'DB')  into  L_BALVL, L_BALFL from SPLDATA.MM_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_SEQNO = L_SEQNO-1;
        else
           select  isnull(PT_YOPVL,0) PT_YOPVL, isnull(PT_YOPFL,'DB') PT_YOPFL  into   L_BALVL, L_BALFL from SPLDATA.co_ptmst where pt_prttp= L_PRTTP and PT_PRTCD = L_PRTCD;
         end if;

--        if L_BALFL = 'CR'  then
--          set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
--        set L_BALFL1 = 'CR';
--  else 
--   set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
-- set L_BALFL1 = 'DB';
--         end if;

       if L_BALFL = 'DB'  then
          set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
          set L_BALFL1 = 'DB';
      else 
         set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
         set L_BALFL1 = 'DB';
        end if;

        -- After deducting credit amount, if the balance goes negative,  Then it is a Credit Balancde
--         if L_BALVL1 < 0 then
--            set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
--            set L_BALFL1 = 'CR';
--         end if;

         if L_BALVL1 < 0 then
            set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
            set L_BALFL1 = 'DB';
         end if;

      set L_RECCT = 0;
      select count(*) into L_RECCT from SPLDATA.MM_PLTRN where PL_CMPCD= LP_CMPCD and PL_PRTTP = L_PRTTP  and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = LP_DOCNO;
      if L_RECCT = 0 then
         insert into SPLDATA.MM_PLTRN (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_BILTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT) values  (LP_CMPCD,L_PRTTP,L_PRTCD,L_DOCTP,LP_DOCNO,L_SEQNO,L_DOCDT,round(L_DOCVL,0),round(L_BALVL1,0),L_BALFL1,round(L_ADJVL,0),L_ACCRF,L_ACCDT,LP_BILTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
      end if;
      update SPLDATA.co_ptmst set pt_ytdvl = round(L_BALVL1,0), pt_ytdfl = L_BALFL1  where pt_prttp = L_PRTTP and PT_PRTCD = L_PRTCD;
      set END_TABLE = 0;
  fetch C_PMR_MM into  L_PRTTP,L_PRTCD, L_DOCTP,  L_DOCDT, L_SBSCD, L_GRPCD, L_DOCVL;
  end while;
  close C_PMR_MM;
  commit;

  OPEN C_PAD_MM;
  fetch C_PAD_MM into L_PRTTP,L_PRTCD,L_DBTTP,L_DBTNO;
  set END_TABLE = 0;
  while END_TABLE = 0 do
      select max(PL_DOCDT) PL_DOCDT into L_PAYDT from SPLDATA.MM_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP=L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCNO in (select PA_CRDNO from SPLDATA.MM_PATRN where PA_CMPCD = LP_CMPCD and PA_PRTTP = L_PRTTP and PA_PRTCD=L_PRTCD and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO) ;
      update SPLDATA.MM_PLTRN set PL_PAYDT = L_PAYDT where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DBTTp and PL_DOCNO = L_DBTNO;
      set END_TABLE = 0;
      fetch C_PAD_MM into L_PRTTP,L_PRTCD,L_DBTTP,L_DBTNO;
  end while;
  close C_PAD_MM;
  commit;
  select sum(isnull(PA_ADJVL,0)) into L_ADJVL from SPLDATA.MM_PATRN where PA_CMPCD = LP_CMPCD and PA_CRDNO = LP_DOCNO;
  update SPLDATA.MM_PLTRN set PL_ADJVL = L_ADJVL where PL_CMPCD = LP_CMPCD and PL_DOCNO = LP_DOCNO;
  commit;
end P1;

commit;
drop procedure SPLDATA.updPLTRN_PMR_MM;
call SPLDATA.updPLTRN_PMR('11','01','91300010');
commit;
call SPLDATA.rwkPLTRN_PRT_MM('01','S','C0138','07/01/2006',0,'DB');
sELECT * FROM SPLDATA.MM_PLTRN WHERE PL_PRTCD='C0138' order by pl_seqno ;
COMMIT;

SELECT * FROM SPLDATA.MR_IVTR1 WHERE IVT_INVNO IN ('95070382','95070389','95070394','95070398','95070399','95070350');
UPDATE SPLDATA.MR_IVTRN SET IVT_SBSCD1='030300' WHERE IVT_INVNO IN ('95070382','95070389','95070394','95070398','95070399');