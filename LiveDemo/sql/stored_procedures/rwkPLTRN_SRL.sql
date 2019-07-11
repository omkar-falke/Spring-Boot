--  procedure to reset serial numbers in Party Ledger
-- This procedure is executed whenever a record is iserted in Party Ledger (MR_PLTRN)  updPLTRN_PMR
-- Partial reworking is carried out, only if necessary
-- After reworking of serial number, corresponding closing balances have also to be set right (corresponding procedures rwkPLTRN_TRN and rwkPLTRN_PRT have to be called)

-- FEATURES INTRODUCED
-- During insertion, party ledger is verified to see if there are any other transactions with Document Date greater than the date of current transaction (Being Inserted)
-- If yes, then the sequence numbers are reworked for these further transactions, by keeping a room (of one seq.number) for current transaction.
-- This current serial number is then returned back to calling program (For record insertion)



drop procedure spldata.rwkPLTRN_SRL;
commit;

create procedure spldata.rwkPLTRN_SRL(IN LP_CMPCD char(2), IN LP_PRTTP char(1), IN LP_PRTCD char(5),  IN LP_DOCDT date, IN LP_DOCTP char(2), IN LP_DOCNO char(8), OUT LP_SEQNO decimal(10))  language sql  modifies sql data
P1:
begin
  declare L_SEQNO_NXT decimal(10);
  declare L_SEQNO_RUN decimal(10);

  declare L_CMPCD char(2);
  declare L_PRTTP char(2);
  declare L_PRTCD char(5);
  declare L_DOCDT date;
  declare L_DOCTP char(2);
  declare L_DOCNO char(8);
  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_SRL cursor for  select  PL_CMPCD , PL_PRTTP , PL_PRTCD , PL_DOCDT,PL_DOCTP , PL_DOCNO  from  spldata.MR_PLTRN  where   PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_SEQNO >= L_SEQNO_NXT order by PL_CMPCD, PL_PRTTP, PL_PRTCD,PL_DOCDT,PL_DOCTP,PL_DOCNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

  select  min(isnull(PL_SEQNO,0)) PL_SEQNO into L_SEQNO_NXT from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCDT > LP_DOCDT;
  if L_SEQNO_NXT > 0 then
     set L_SEQNO_RUN = L_SEQNO_NXT + 1;
     open C_SRL;
     set END_TABLE = 0;
     fetch C_SRL into L_CMPCD , L_PRTTP , L_PRTCD , L_DOCDT , L_DOCTP , L_DOCNO;
     while END_TABLE = 0 do
             update spldata.MR_PLTRN set PL_SEQNO = L_SEQNO_RUN where PL_CMPCD = L_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCDT = L_DOCDT and PL_DOCTP = L_DOCTP and PL_DOCNO = L_DOCNO;
             set L_SEQNO_RUN = L_SEQNO_RUN+1;
            fetch C_SRL into L_CMPCD , L_PRTTP , L_PRTCD , L_DOCDT , L_DOCTP , L_DOCNO;
     end while;
     close C_SRL;
   end if;
   set LP_SEQNO = L_SEQNO_NXT;
   --insert into spldata.temp_chk values ('Returned Seq.No.: '||char(L_SEQNO_NXT));
end P1;

commit;
select * from spldata.mr_pltrn where pl_docno='71100001';

call spldata.rwkPLTRN_SRL('01','C','S0280','07/09/2006','11','71100001');
commit;

select * from spldata.mr_pltrn where pl_prtcd='S0280';
select * from spldata.temp_chk;


update spldata.mr_pltrn set pl_docdt = '07/11/2006',pl_seqno=1 where pl_docno='71100001';
update spldata.mr_pltrn set pl_seqno=2 where pl_docno='70003546';
commit;

select pt_prtcd,pt_stsfl from exdata.co_ptmst where pt_prttp='T' and pt_prtcd like 'S%';
