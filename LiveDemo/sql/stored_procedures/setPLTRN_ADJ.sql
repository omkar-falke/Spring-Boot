
-- procedure to set right adjustment values in party ledger
-- This procedure is to be executed whenever the mismatches are found between transaction total of Part Ledger and Adjustment table

-- PRCEDURE WORKING
--  Document number and Credit / Debit category is accepted as parameter
--   The sum of ADJVL value from Adjustment table is arrived at and is updated against corresponding document number in MR_PLTRN

drop procedure spldata.setPLTRN_ADJ;
commit;

create procedure spldata.setPLTRN_ADJ(IN LP_CMPCD char(2),IN LP_DOCNO char(8), IN LP_CRDFL char(2))  language sql  modifies sql data 
P1:
begin
  declare L_ADJVL decimal(12,2);

  declare END_TABLE int default 0;

  declare continue handler for not found 
       set END_TABLE = 1; 



   if LP_CRDFL = 'CR' then
      select  sum(isnull(PA_ADJVL,0)) into L_ADJVL from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_CRDNO = LP_DOCNO;
      update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL where PL_CMPCD = LP_CMPCD and PL_DOCNO = LP_DOCNO;
   end if;

   if LP_CRDFL = 'DB' then
      select  sum(isnull(PA_ADJVL,0)) into L_ADJVL from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_DBTNO = LP_DOCNO;
      update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL where PL_CMPCD = LP_CMPCD and PL_DOCNO = LP_DOCNO;
   end if;

end P1;


commit;


select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl from spldata.mr_pltrn, spldata.mr_patrn where pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_docno = pa_dbtno and isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_docno, pl_adjvl  having pl_adjvl <> sum(isnull(pa_adjvl,0));



drop procedure spldata.setPLTRN_ADJ1;
commit;

create procedure spldata.setPLTRN_ADJ1(IN LP_CMPCD char(2))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_DOCNO char(8);
  declare L_ADJVL decimal(12,2);
  declare L_ADJVL1 decimal(12,2);
  declare END_TABLE int default  0;

  declare C_ADJ cursor for  select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl1 from spldata.mr_pltrn, spldata.mr_patrn where pl_cmpcd = lp_cmpcd and pl_cmpcd=pa_cmpcd and pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_docno = pa_dbtno and isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_docno, pl_adjvl  having pl_adjvl <> sum(isnull(pa_adjvl,0));


  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_ADJ ;
  fetch C_ADJ  into  L_PRTTP,L_PRTCD,  L_DOCNO, L_ADJVL, L_ADJVL1;

  while END_TABLE = 0 DO 
          update spldata.mr_pltrn set pl_adjvl = L_ADJVL1 where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCNO = L_DOCNO and PL_ADJVL = L_ADJVL;
          fetch C_ADJ  into  L_PRTTP,L_PRTCD,  L_DOCNO, L_ADJVL, L_ADJVL1;
  
  end while;
  close C_ADJ;

end P1;

call spldata.setPLTRN_ADJ1('01');
commit;

call spldata.setPLTRN_ADJ('01','70009796','DB');


select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl1 from spldata.mr_pltrn, spldata.mr_patrn where pl_cmpcd = '01' and pl_cmpcd=pa_cmpcd and pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_docno = pa_dbtno and isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_docno, pl_adjvl  having pl_adjvl <> sum(isnull(pa_adjvl,0));

select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl1 from spldata.mr_pltrn, spldata.mr_patrn where pl_cmpcd = '01' and pl_cmpcd=pa_cmpcd and pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_docno = pa_dbtno and isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_docno, pl_adjvl  having pl_adjvl <> sum(isnull(pa_adjvl,0));

select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl1 from spldata.mr_pltrn, spldata.mr_patrn where pl_cmpcd = '01' and pl_cmpcd=pa_cmpcd and pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_docno = pa_crdno and isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_docno, pl_adjvl  having pl_adjvl <> sum(isnull(pa_adjvl,0));
