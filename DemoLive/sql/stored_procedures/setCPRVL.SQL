-- procedure to set Cut-off price in indent transaction according to the date range specified through mr_qrgrr.java
-- Procedure is called through mr_qrgrr.java

drop procedure spldata.setCPRVL;
commit;


create procedure spldata.setCPRVL(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 
 P1:

begin
  declare L_CP_PRDCD char(10);
  declare L_CP_CPRVL decimal(8);
  declare END_TABLE int default 0;

  declare C_CPR cursor for  select  CP_PRDCD, CP_CPRVL from spldata.MR_CPMST where CP_CMPCD = LP_CMPCD and CP_STRDT = CONVERT(varchar,LP_STRDT,101) and CP_ENDDT = CONVERT(varchar,LP_ENDDT,101)  and CP_MKTTP = LP_MKTTP order by CP_PRDCD;


  declare continue handler for not found 
       set END_TABLE = 1; 

 open C_CPR ;
  fetch C_CPR into  L_CP_PRDCD, L_CP_CPRVL;
  while END_TABLE = 0 DO 
    --insert into spldata.temp_chk values(L_CP_PRDCD||'/'||char(L_CP_CPRVL));
    update spldata.MR_IVTRN set IVT_CPRVL = L_CP_CPRVL where IVT_CMPCD = LP_CMPCD and IVT_MKTTP=LP_MKTTP and IVT_PRDCD = L_CP_PRDCD and CONVERT(varchar,IVT_INVDT,101)  between CONVERT(varchar,LP_STRDT,101) and  CONVERT(varchar,LP_ENDDT,101);
    update spldata.MR_INTRN set INT_CPRVL = L_CP_CPRVL where INT_CMPCD = LP_CMPCD and INT_MKTTP=LP_MKTTP and INT_PRDCD = L_CP_PRDCD and INT_INDNO in (select IN_INDNO from spldata.MR_INMST where IN_CMPCD = LP_CMPCD and IN_MKTTP=LP_MKTTP and IN_BKGDT between CONVERT(varchar,LP_STRDT,101) and CONVERT(varchar,LP_ENDDT,101));       set END_TABLE = 0; 
  fetch C_CPR into  L_CP_PRDCD, L_CP_CPRVL;
  end while;
  close C_CPR; 
  commit;
end P1;

commit;
delete from spldata.temp_chk;
commit;
select * from spldata.temp_chk;



call spldata.setCPRVL('01','07/01/2005','07/31/2005');
commit;

select  CP_PRDCD, CP_CPRVL from spldata.MR_CPMST where CP_STRDT = CONVERT(varchar,'07/01/2005',101) and CP_ENDDT = CONVERT(varchar,'07/31/2005',101)  and CP_MKTTP = '01' and cp_prdcd='5111150160' order by CP_PRDCD;



select ivt_cprvl from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101) between '07/01/2005' and '07/31/2005' and ivt_prdcd='5111150160';
update spldata.mr_ivtrn set ivt_cprvl=11111 where CONVERT(varchar,ivt_invdt,101) between '07/01/2005' and '07/31/2005' and ivt_prdcd='5111150160';
commit;


select * from spldata.mr_cpmst where cp_strdt= '07/01/2005' and cp_enddt='07/31/2005' and cp_prdcd='5111150160';
select * from spldata.mr_cpmst where cp_strdt= '07/01/2005' and cp_enddt='07/31/2005' order by cp_prdcd;



select ivt_cprvl from spldata.mr_intrn where int_cprvl>0;
update spldata.mr_intrn set int_cprvl=0 where int_cprvl>0;
commit;


    --update spldata.MR_IVTRN set IVT_CPRVL = L_CP_CPRVL where IVT_MKTTP=LP_MKTTP and IVT_PRDCD = L_CP_PRDCD and IVT_INDNO in (select IN_INDNO from spldata.MR_INMST where IN_MKTTP=LP_MKTTP and IN_BKGDT between date(LP_STRDT) and date(LP_ENDDT));


alter table spldata.mr_ivtrn drop column ivt_copvl cascade;
commit;

alter table spldata.mr_ivtrn add column ivt_cprvl decimal(8);
commit;
