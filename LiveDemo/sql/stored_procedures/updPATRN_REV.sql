
-- procedure to reverse receipt adjustment data in Payment Adjustment  transaction
-- This procedure is to be executed whenever the REverse Entry is passes against a receipt (in case of cheque bouncing)
-- Procedure is called through mr_tepmt.java


drop procedure spldata.updPATRN_REV;
commit;

create procedure spldata.updPATRN_REV(IN LP_CMPCD char(2), IN LP_PRTTP char(1), IN LP_PRTCD char(5), IN LP_DOCTP char(2), IN LP_DOCNO char(8), IN LP_BNKCD char(5), IN LP_CHQNO char(10))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_DBTTP char(2);
  declare L_DBTNO char(8);
  declare L_ADJVL decimal(12,2);
  declare L_ADJVL1 decimal(12,2);
  declare L_ADJVL2 decimal(12,2);
  declare L_DOCVL decimal(12,2);

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_ADJ cursor for  select  PA_DBTTP,PA_DBTNO, PA_ADJVL  from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_PRTTP = LP_PRTTP and PA_PRTCD = LP_PRTCD and PA_CRDTP  = LP_DOCTP and PA_CRDNO = LP_DOCNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

 set END_TABLE = 0;
  open C_ADJ;
  fetch C_ADJ into  L_DBTTP, L_DBTNO, L_ADJVL;
  select pr_rctvl into L_DOCVL from spldata.mr_prtrn where PR_CMPCD = LP_CMPCD and PR_PRTTP = LP_PRTTP and PR_PRTCD = LP_PRTCD and PR_DOCTP = LP_DOCTP and PR_DOCNO = LP_DOCNO and PR_BNKCD = LP_BNKCD and PR_CHQNO = LP_CHQNO;
  if L_DOCVL = 0 then
        update spldata.MR_PATRN set PA_ADJVL = 0 where PA_CMPCD = LP_CMPCD and PA_PRTTP= LP_PRTTP and PA_PRTCD = LP_PRTCD and PA_CRDTP = LP_DOCTP and PA_CRDNO = LP_DOCNO;
  end if;
  while END_TABLE = 0 DO 
     set L_ADJVL1 = L_ADJVL;
     if L_DOCVL < L_ADJVL then 
        set L_ADJVL1 = L_DOCVL;
     end if;
     if L_DOCVL > 0 then
        update spldata.MR_PATRN set PA_ADJVL = PA_ADJVL - L_ADJVL1 where PA_CMPCD = LP_CMPCD and PA_PRTTP= LP_PRTTP and PA_PRTCD = LP_PRTCD and PA_CRDTP = LP_DOCTP and PA_CRDNO = LP_DOCNO and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO;
        set L_DOCVL = L_DOCVL - L_ADJVL1;
        set L_ADJVL2 = 0;
     end if;
     select  sum(PA_ADJVL) into L_ADJVL2 from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO;
     update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL2 where PL_CMPCD = LP_CMPCD and  PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and  PL_DOCTP = L_DBTTP and PL_DOCNO  = L_DBTNO;
     delete from  spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_DBTTP = L_DBTTP  and PA_DBTNO = L_DBTNO and isnull(PA_ADJVL,0)=0;
     fetch C_ADJ into  L_DBTTP, L_DBTNO, L_ADJVL;
  end while;
  close C_ADJ;
  set L_ADJVL2 = 0;
  select  sum(PA_ADJVL) into L_ADJVL2 from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_CRDTP = LP_DOCTP and PA_CRDNO = LP_DOCNO;
  update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL2  where PL_CMPCD = LP_CMPCD and  PL_DOCTP = LP_DOCTP and PL_DOCNO = LP_DOCNO;
  update spldata.MR_PRTRN set PR_ADJVL = L_ADJVL2  where PR_CMPCD = LP_CMPCD and  PR_DOCTP = LP_DOCTP and PR_DOCNO = LP_DOCNO;
  delete from  spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_CRDTP = LP_DOCTP and PA_CRDNO = LP_DOCNO and isnull(PA_ADJVL,0)=0;

end P1;


commit;

select * from spldata.mr_prtrn where pr_docno = '71302627';


call spldata.updPATRN_REV('01','C','C0149','13','71302627','B0003','42616');
commit;
