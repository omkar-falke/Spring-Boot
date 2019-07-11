
-- procedure to  update Avg.Rate in product master. These rates are used while raising customer order in Captive Consumption
-- Rates are updated for folowing categories
--              Speciality Polystyrene
--              Master Batch
--  Avg. Rates are picked up as (Last saling price per kg against the grade minus Rs.2)
-- Creation Date : 29th Sept 2007
--             By    : SRD
--             Job No. : 8SP00773

-- This procedure will be called from Avg.Rate Entry program mr_meavg


drop procedure spldata.setPRMST_AVGRT;
commit;

create procedure spldata.setPRMST_AVGRT(IN LP_CMPCD char(2),IN LP_PRDCT char(2))  language sql  modifies sql data 
P1:
begin
  declare L_PRDCD char(10);
  declare L_MAXRT decimal(10,2);
  declare L_MAXDT date;
  declare END_TABLE int default 0;

  declare C_INV1 cursor for  select  IVT_PRDCD, max(CONVERT(varchar,IVT_INVDT,101)) IVT_INVDT from spldata.MR_IVTRN where  ivt_cmpcd = lp_cmpcd and SUBSTRING(IVT_PRDCD,1,2) = LP_PRDCT and IVT_MKTTP in ('01','05') and IVT_STSFL<>'X'  and ivt_saltp = '01'  group by IVT_PRDCD order by IVT_PRDCD;


  declare continue handler for not found 
       set END_TABLE = 1; 
  --update spldata.co_prmst set pr_avgrt=0 where SUBSTRING(pr_prdcd,1,2) = LP_PRDCT;
  --commit;
  open C_INV1 ;
  fetch C_INV1  into  L_PRDCD, L_MAXDT;
  while END_TABLE = 0 DO 
    select max(ivt_invrt) into L_MAXRT from spldata.mr_ivtrn where ivt_cmpcd = lp_cmpcd and ivt_prdcd = L_PRDCD and CONVERT(varchar,ivt_invdt,101) = L_MAXDT and ivt_mkttp in ('01','05')    and  ivt_saltp = '01' and IVT_STSFL <> 'X';
     update spldata.CO_PRMST set PR_AVGRT = L_MAXRT-2000, PR_AVGDT = current_date, PR_AVGBY = 'SYS' where PR_PRDCD = L_PRDCD;
    fetch C_INV1  into  L_PRDCD, L_MAXDT;
  end while;
  close C_INV1;
  commit;
end P1;


commit;


call spldata.setPRMST_AVGRT('01','54');
commit;
select pr_prdcd,pr_avgrt,pr_avgdt from spldata.co_prmst where pr_prdcd like '52%';
select * from spldata.mm_gptrn where gp_dptcd='404';
