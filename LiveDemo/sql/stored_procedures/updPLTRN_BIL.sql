-- procedure to create/update data in Party Ledger (Creditors) transaction using Bill Passing details in MM_PLTRN
-- This procedure is to be executed whenever the Bill is passed and saved.
-- Procedure is called through mm_teblp.java (Purchase Bill Passing) and mm_tetbp.java (Transporter's Bill Passing)

-- FEATURES INTRODUCED
--  When Bill is Passed, A single transaction is introduced in MM_PLTRN (Party Ledger) with Total Bill Passed Value.
-- New record generated will have
--        Party Group Code (Picked up from Party Master (CO_PTMST) for the specfied Supplier.
--        Next Sequence Number (Transaction Processing sequence number for the Party Group)
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Bill Pass Amount from Closing balance of previous sequence number)


select * from spldata.mr_biltr;
SELECT * FROM SPLDATA.MM_BLMST WHERE BL_DOCNO='90100001';
CALL SPLDATA.updPLTRN_BIL('01','03','90300092');
drop procedure splDATA.updPLTRN_BIL;
commit;

create procedure splDATA.updPLTRN_BIL(IN LP_CMPCD char(2),IN LP_BILTP char(2), IN LP_DOCNO char(8),IN LP_PRTNM char(40))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_PRTNM char(40);
  declare L_DOCTP char(2);
  declare L_BILTP char(2);
  declare L_DOCNO char(8);
  declare L_DOCDT date;
  declare L_DUEDT date;
  declare L_SEQNO decimal(10);
  declare L_DOCVL decimal(12,2);
  declare L_BALVL decimal(12,2);
  declare L_BALFL char(2);
  declare L_BALVL1 decimal(12,2);
  declare L_BALFL1 char(2);
  declare L_ADJVL decimal(12,2);
  declare L_ACCRF char(8);
  declare L_ACCDT date;
  declare L_GRPCD char(5);
  declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

 declare C_BIL cursor for  select  distinct BL_PRTTP,BL_PRTCD, BL_BILTP,BL_DOCNO,BL_BLPDT,BL_PMDDT,isnull(BL_CALAM,0)  BL_CALAM from splDATA.mm_blmst   where   BL_BILTP = LP_BILTP and BL_DOCNO=LP_DOCNO and isnull(BL_STSFL,'') <> 'X' and  BL_CMPCD=LP_CMPCD;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_BIL ;
fetch C_BIL into L_PRTTP,L_PRTCD,L_BILTP,L_DOCNO,L_DOCDT,L_DUEDT,L_DOCVL;

 select distinct isnull(PT_GRPCD,PT_PRTCD) into L_GRPCD from splDATA.CO_PTMST where PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD;

  if length(L_GRPCD) < 5 then 
     set L_GRPCD = L_PRTCD;
  end if;

  set L_DOCTP = '21';
  set L_SEQNO = 0;
  set L_BALVL = 0.00;
set L_BALFL='CR';  
set L_BALVL1 = 0.00;
set L_BALFL1='CR';
  set L_ADJVL = 0.00;
  set L_ACCRF = '    ';
  set L_ACCDT = null;
  set L_TRNFL = '0';
  set L_STSFL = '1';
  set L_LUSBY = 'SYS';
  set L_LUPDT = current_date;

  select isnull(PL_SEQNO,0) PL_SEQNO into L_SEQNO from splDATA.MM_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = '21' and PL_DOCNO = LP_DOCNO;
  -- If the Bill Passing Record exists in payment ledger, only Bill Passed value is over written and closing balances for subsequent transactions (within the party) are reworked

  if L_SEQNO > 0 then 
      update splDATA.mm_pltrn set PL_DOCVL = L_DOCVL where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = LP_DOCNO;
      --insert into spldata.temp_chk values (LP_CMPCD||'  '||L_PRTTP||'  '||L_PRTCD||'  '||L_DOCTP||'  '|| LP_DOCNO||'  '||char(L_SEQNO));
      --call spldata.rwkPLTRN_TRN(LP_CMPCD, L_PRTTP, L_PRTCD,L_DOCTP, LP_DOCNO, L_SEQNO);
  end if;


  if L_SEQNO =0 then 
    select max(isnull(PL_SEQNO,0))  into L_SEQNO from splDATA.MM_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;
     set L_SEQNO = isnull(L_SEQNO,0);
     if L_SEQNO > 0 then
         select  isnull(PL_BALVL,0), isnull(PL_BALFL,'DB')  into  L_BALVL, L_BALFL from splDATA.MM_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_SEQNO = L_SEQNO;
     else
         select  isnull(PT_YOPVL,0), isnull(PT_YOPFL,'CR')  into  L_BALVL, L_BALFL from splDATA.CO_PTMST where PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD;
     end if;

   if L_BALFL='CR' then
        set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
        set L_BALFL1 = 'CR';
    else 
        set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
       set L_BALFL1 = 'DB';
     end if;

    -- After adding credit amount, if the balance goes negative,  Then it is a Debit Balance
     if L_BALVL1 < 0 then
        set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
        set L_BALFL1 = 'CR';
     end if;

   insert into splDATA.mm_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_PRTNM,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DUEDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_BILTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT) values  (LP_CMPCD,L_PRTTP,L_PRTCD,LP_PRTNM,L_DOCTP,L_DOCNO,L_SEQNO+1,L_DOCDT,L_DUEDT,L_DOCVL,L_BALVL1,L_BALFL1,L_ADJVL,L_ACCRF,L_ACCDT,LP_BILTP,L_GRPCD,'',L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
      update splDATA.co_ptmst set pt_ytdvl = L_BALVL1, pt_ytdfl = L_BALFL1 where pt_prttp = L_PRTTP and PT_PRTCD = L_PRTCD;

  end if;
  close C_BIL;
  --commit;
end P1;

COMMIT;


SELECT * FROM SPLTEST.CO_CDTRN WHERE CMT_CGSTP='MMXXBLP';
SELECT * FROM SPLTEST.MM_BLMST WHERE BL_LUPDT='03/23/2009';
SELECT * FROM SPLTEST.MM_PLTRN WHERE PL_DOCNO='90300092';
CALL SPLDATA.updPLTRN_BIL('01','03','90300092');
CALL SPLTEST.updPLTRN_BIL('01','07','90700001');
SELECT * FROM SPLDATA.MM_PLTRN WHERE PL_DOCNO='90300093';;
SELECT * FROM SPLDATA.MM_BLMST WHERE BL_DOCNO IN('90300092','90100092');
SELECT * FROM SPLTEST.CO_PTMST WHERE PT_PRTCD='R0077';
SELECT * FROM SPLDATA.MM_BLMST WHERE BL_DOCNO='90100001' AND BL_BILTP='03';
SELECT * FROM SPLDATA.MM_PLTRN;
DELETE FROM SPLDATA.MM_PLTRN;
SELECT * FROM SPLDATA.MR_OCTRN WHERE OCT_OCFNO='90100634';
UPDATE SPLDATA.MR_OCTRN SET OCT_SBSCD1='120100'  WHERE OCT_OCFNO='90100634';
DROP PROCEDURE SPLTEST.UPDPLTRN_BIL;
COMMIT;