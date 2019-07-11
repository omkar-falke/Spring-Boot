update spldata.sa_usmst set us_stsfl='' where us_stsfl='D';
commit;

select dot_frtrt from spldata.mr_dotrn;
select ivt_frtrt from spldata.mr_ivtrn;

select sum(int_indqt) from spldata.mr_intrn where int_mkttp='01' and int_indno in (select int_indno from spldata.mr_inmst where in_bkgdt='05/22/2004') and int_stsfl<>'X' and SUBSTRING(int_prdcd,1,4)='5111';


select sum(int_indqt*int_basrt) from spldata.mr_intrn where int_mkttp='01' and int_indno in (select int_indno from spldata.mr_inmst where in_bkgdt='05/21/2004' and in_mkttp='01' and in_zoncd in ('06','07')) and int_stsfl<>'X' and SUBSTRING(int_prdcd,1,4)='5111';


select sum(int_indqt*int_basrt) from spldata.mr_intrn where int_mkttp='01' and int_indno in (select int_indno from spldata.mr_inmst where in_bkgdt='05/21/2004' and in_mkttp='01' and in_zoncd in ('06','07')) and int_stsfl<>'X' and SUBSTRING(int_prdcd,1,4)='5111';


select int_indno,sum(int_indqt*int_basrt) from spldata.mr_intrn where int_mkttp='01' and int_indno in (select in_indno from spldata.mr_inmst where in_bkgdt='05/22/2004' and in_mkttp='01' and in_zoncd in ('06','07')) and int_stsfl<>'X' and SUBSTRING(int_prdcd,1,4)='5111' group by int_indno;



select sum(int_indqt*int_basrt) from spldata.mr_intrn where int_mkttp='01' and int_indno in (select in_indno from spldata.mr_inmst where in_bkgd between '05/01/2004' and '05/22/2004' and in_mkttp='01' and in_zoncd in ('06','07')) and int_stsfl<>'X' and SUBSTRING(int_prdcd,1,4)='5111';


select sum(int_indqt*int_basrt) from spldata.mr_intrn where int_mkttp='01' and int_indno in (select in_indno from spldata.mr_inmst where in_bkgd between '05/01/2004' and '05/22/2004' and in_mkttp='01' and in_zoncd in ('06','07')) and int_stsfl<>'X' and SUBSTRING(int_prdcd,1,4)='5111';



select distinct ivt_invno, ivt_saltp, SUBSTRING(ivt_prdcd,1,4), ivt_prdds, ivt_invqt,(ivt_invqt*ivt_invrt) gross_total, dot_frtrt total_freight, int_cdcvl, int_ddcvl, int_tdcvl, ((ivt_invqt)*(int_cdcvl+int_ddcvl+int_tdcvl)) gross_discount from spldata.mr_ivtrn,spldata.mr_dotrn,spldata.mr_intrn,spldata.mr_inmst where in_mkttp=int_mkttp and in_indno=int_indno and int_mkttp=dot_mkttp and dot_mkttp=ivt_mkttp and int_mkttp=ivt_mkttp and int_mkttp='01'  and int_indno=dot_indno and int_indno=ivt_indno and in_bkgdt between '05/01/2004' and '05/24/2004' and int_prdcd=dot_prdcd and int_prdcd=ivt_prdcd and in_saltp not in ('12','03','04','05','51');

select SUBSTRING(ivt_prdcd,1,4) int_prodcd,sum( ivt_invqt)  total_qty, sum(ivt_invqt*ivt_invrt) gross_tot, sum((ivt_invqt)*(int_cdcvl+int_ddcvl+int_tdcvl)) gross_dis, sum(ivt_invqt*dot_frtrt) total_frt from spldata.mr_ivtrn,spldata.mr_dotrn,spldata.mr_intrn,spldata.mr_inmst where in_mkttp=int_mkttp and in_indno=int_indno and int_mkttp=dot_mkttp and dot_mkttp=ivt_mkttp and int_mkttp=ivt_mkttp and int_mkttp='01'  and int_indno=dot_indno and int_indno=ivt_indno and in_bkgdt between '05/01/2004' and '05/24/2004' and int_prdcd=dot_prdcd and int_prdcd=ivt_prdcd and in_saltp not in ('12','03','04','05','51') group by SUBSTRING(ivt_prdcd,1,4);


select int_indno,sum( ivt_invqt)  total_qty, sum(ivt_invqt*ivt_invrt) gross_tot, sum((ivt_invqt)*(int_cdcvl+int_ddcvl+int_tdcvl)) gross_dis, sum(ivt_invqt*dot_frtrt) total_frt from spldata.mr_ivtrn,spldata.mr_dotrn,spldata.mr_intrn,spldata.mr_inmst where in_mkttp=int_mkttp and in_indno=int_indno and int_mkttp=dot_mkttp and dot_mkttp=ivt_mkttp and int_mkttp=ivt_mkttp and int_mkttp='01'  and int_indno=dot_indno and int_indno=ivt_indno and in_bkgdt between '05/21/2004' and '05/21/2004' and int_prdcd=dot_prdcd and int_prdcd=ivt_prdcd and in_saltp not in ('12','03','04','05','51')  and ivt_invqt>0 and CONVERT(varchar,ivt_invdt,101) < '05/21/2005' group by int_indno order by int_indno;




