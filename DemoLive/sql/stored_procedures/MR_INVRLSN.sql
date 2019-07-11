
update spldata.mr_ivtrn set ivt_invqt=ivt_invqt where CONVERT(varchar,ivt_invdt,101) between '04/01/2005' and '08/30/2005';
commit;



-----------------------------------------------------------------------------------------------------------------------------------------------------
--  Invoice Realisation :
--  -------------------------

--  Invoice Qty 			: ivt_invqt
--  Invoice Rate 			: ivt_invrt
--  Consignee Discount Rate 	                                           : int_cc1vl      	( & int_cc1rf = Consignee Code)
--  Distributor Discount Rate 	                                           : int_cc2vl	( & int_cc2rf = Distributor Code)
--  Third Party Discount Rate 	                                           : int_cc3vl	( & int_cc3rf = Third Party)
--  Freight Rate			: dot_frtrt

--  Credit period			: in_cptvl
--  Commission Rate 		                     : pt_dmcrt for Domestic   pt_decrt for Deemed Export (From Distributor Record from Party Master)
--  Prime Grade 			: substr(ivt_prdcd,7,1) = '0'

--  a.       	Invoice value = Invoice Qty * Invoice Rate
--  b.	Booking Discount Consignee  =  Invoice Qty   *  Consignee Discount Rate
--  c.	Booking Discount Distributor  =  Invoice Qty  *  Distributor Discount Rate
--  d. 	Booking Discount Third Party  =  Invoice Qty *  Third party Discount Rate

--  A.	Total Booking Discount = b+c+d

--  B.	Commission =  Invoice Qty * Commission rate
           	(Only for prime grade)
--  C.	Freight Amount = Freight Rate * Invoice Qty.
--  D.	Realisation Value = a - (A+B+C)
--  E.	Credit Interest  Rate /day @ 10% of Realisation Value =  (D * 0.10)/365
--  F. 	Credit Interest Amount	= E * Credit Period


--  Realisation Value (Before Credit Interest) 	: ivt_rsnvl             	D
--  Credit Interest Amount			: ivt_crdvl		F
--  Booking Discount Value			: ivt_cclvl		A
--  Commission Value			: ivt_comvl		B
--  Freight Amount			: ivt_frtvl		C
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
;

drop procedure spldata.MR_INVRLSN;
commit;


create procedure spldata.MR_INVRLSN(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data
 P1:
  begin
     declare L_CCL_VAL decimal(10,2) default 0.00; 
     declare L_COM_VAL decimal(10,2) default 0.00; 
     declare L_TDM_VAL decimal(10,2) default 0.00; 
     declare L_FRT_VAL decimal(10,2) default 0.00; 
     declare L_CRD_VAL decimal(10,2) default 0.00; 
     declare L_RLSN_VAL decimal(10,2) default 0.00; 

     declare L_FRTRT decimal(10,2) default 0.00; 
     declare L_CC1RT decimal(10,2) default 0.00; 
     declare L_CC2RT decimal(10,2) default 0.00; 
     declare L_CC3RT decimal(10,2) default 0.00; 
     declare L_CC1RF varchar(6); 
     declare L_CC2RF varchar(6);
     declare L_CC3RF varchar(6);

     declare L_IVT_CC1VL decimal(10,2) default 0.00; 
     declare L_IVT_CC2VL decimal(10,2) default 0.00; 
     declare L_IVT_CC3VL decimal(10,2) default 0.00; 

     declare L_IVT_TD1VL decimal(10,2) default 0.00; 
     declare L_IVT_TD2VL decimal(10,2) default 0.00; 
     declare L_IVT_TD3VL decimal(10,2) default 0.00; 


     declare L_INVQT decimal(10,3) default 0.00; 
     declare L_INVRT decimal(10,2) default 0.00; 
     declare L_BKGDT date; 
     declare L_MKTTP char(2); 
     declare L_INDNO char(9); 
     declare L_PRDCD char(10); 
     declare L_PKGTP char(2); 
     declare L_SALTP char(2); 
     declare L_BYRCD char(5); 
     declare L_CNSCD char(5); 
     declare L_DSRTP char(1); 
     declare L_DSRCD char(5); 
     declare L_GRPCD char(5); 
     declare L_CPTVL decimal(10,2) default 0.00;

     declare L_DORNO char(8); 
     declare L_LADNO char(8); 


     declare L_COMRT decimal(10,2) default 0.00; 
     declare L_RECCT int default 0;
     declare END_TABLE int default 0;

     declare C_INV CURSOR FOR  select  IVT_MKTTP,  IVT_INDNO,  IVT_PRDCD,  IVT_PKGTP,  IVT_SALTP, IVT_DORNO, IVT_LADNO,IVT_BYRCD, IVT_CNSCD, IVT_DSRTP,IVT_DSRCD,  ifnull(IVT_INVQT,0) IVT_INVQT,  ifnull(IVT_INVRT,0) IVT_INVRT  from  spldata.MR_IVTRN  where IVT_CMPCD = LP_CMPCD and IVT_MKTTP = LP_MKTTP and CONVERT(varchar,IVT_INVDT,101)  between CONVERT(varchar,LP_STRDT,101) and  CONVERT(varchar,LP_ENDDT,101) and ifnull(IVT_INVQT,0)>0  order by  IVT_MKTTP, IVT_INDNO,IVT_PRDCD, IVT_PKGTP;


declare continue handler for not found 
       set END_TABLE = 1; 



set END_TABLE = 0; 
open C_INV;
fetch C_INV  into  L_MKTTP,  L_INDNO,  L_PRDCD,  L_PKGTP, L_SALTP, L_DORNO,  L_LADNO,  L_BYRCD,  L_CNSCD,  L_DSRTP, L_DSRCD, L_INVQT,  L_INVRT;

while END_TABLE = 0 DO 

    select ifnull(int_cc1vl,0) int_cclvl, ifnull(int_cc2vl,0) int_cc2vl, ifnull(int_cc3vl,0) int_cc3vl, ifnull(int_cc1rf,'') int_cc1rf, ifnull(int_cc2rf,'') int_cc2rf, ifnull(int_cc3rf,'') int_cc3rf  into L_CC1RT, L_CC2RT, L_CC3RT, L_CC1RF, L_CC2RF, L_CC3RF from spldata.mr_intrn where int_CMPCD = LP_CMPCD and int_mkttp=L_MKTTP and int_indno = L_indno and int_prdcd = L_prdcd and int_pkgtp = L_pkgtp;


    select ifnull(DOT_FRTRT,0) dot_frtrt into L_FRTRT from spldata.mr_dotrn where dot_CMPCD = LP_CMPCD and dot_mkttp=L_mkttp and dot_dorno = L_dorno and dot_prdcd = L_prdcd and  dot_pkgtp = L_pkgtp;
    set L_FRT_VAL = L_INVQT*L_FRTRT;


   -- ******** 5% TDS to be considered for Distributor commission, Distributor booking discount and for Third Party booking discount ********

    set L_IVT_CC1VL = 0.00;
    set L_IVT_CC2VL = 0.00;
    set L_IVT_CC3VL = 0.00;

    set L_IVT_TD1VL = 0.00;
    set L_IVT_TD2VL = 0.00;
    set L_IVT_TD3VL = 0.00;

  -- 5% TDS is applicable for Distributor & Third party credit discount
   if  L_CC1RT>0  and substr(ifnull(L_CC1RF,' '),1,1)  = 'C'  then 
     set L_IVT_CC1VL =  (L_INVQT*L_CC1RT);
   end if;

   if  L_CC1RT>0  and substr(ifnull(L_CC2RF,' ') ,1,1) = 'D'  then 
     set L_IVT_CC2VL =  (L_INVQT*L_CC2RT)*0.95;
     set L_IVT_TD2VL =  (L_INVQT*L_CC2RT)*0.05;
   end if;


   if L_CC1RT>0  and substr(ifnull(L_CC2RF,' '),1,1)  in ( 'C','D')  then 
     set L_IVT_CC3VL =  (L_INVQT*L_CC3RT)*0.95;
     set L_IVT_TD3VL =  (L_INVQT*L_CC3RT)*0.05;
   end if;



    set L_CCL_VAL  = L_IVT_CC1VL+L_IVT_CC2VL+L_IVT_CC3VL+L_IVT_TD1VL+L_IVT_TD2VL+L_IVT_TD3VL;

   select in_bkgdt into  L_BKGDT from spldata.MR_INMST where IN_CMPCD = LP_CMPCD and IN_MKTTP = L_MKTTP and IN_INDNO = L_INDNO;
   select ifnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp='C'  and pt_prtcd = L_BYRCD;

   set L_COMRT = 0;
   set L_COM_VAL = 0;
   call spldata.getDCMRT(L_DSRTP, L_DSRCD , L_PRDCD , L_SALTP, L_GRPCD,  L_COMRT);

    set L_COM_VAL = (L_COMRT*L_INVQT)*0.95; set L_TDM_VAL = (L_COMRT*L_INVQT)*0.05;

   set L_RLSN_VAL = (L_INVQT * L_INVRT) - (L_CCL_VAL+L_COM_VAL+L_TDM_VAL+L_FRT_VAL);
   select ifnull(in_cptvl,0) in_cptvl into L_CPTVL from spldata.mr_inmst where in_CMPCD = LP_CMPCD and in_mkttp=L_MKTTP and in_indno = L_INDNO;
   set L_CRD_VAL = 0.00;
   if L_CPTVL > 0 then
       set L_CRD_VAL = ((L_RLSN_VAL*0.10)/365)*L_CPTVL;
   end if;


update spldata.mr_ivtrn set ivt_frtvl=L_FRT_VAL, ivt_cclvl =  L_CCL_VAL , ivt_comvl = L_COM_VAL, ivt_crdvl=L_CRD_VAL,   ivt_cc1rf = l_cc1rf, ivt_cc2rf = l_cc2rf, ivt_cc3rf  = l_cc3rf,ivt_cc1vl=l_ivt_cc1vl,ivt_cc2vl=l_ivt_cc2vl,ivt_cc3vl=l_ivt_cc3vl  where IVT_CMPCD = LP_CMPCD and IVT_MKTTP = L_MKTTP and IVT_LADNO = L_LADNO and  IVT_PRDCD =   L_PRDCD and IVT_PKGTP = L_PKGTP; 

update spldata.mr_ivtrn set ivt_rsnvl = ifnull(ivt_invqt,0)*ifnull(ivt_invrt,0) - ifnull(ivt_cclvl,0) - ifnull(ivt_comvl,0) - ifnull(ivt_frtvl,0)  where IVT_CMPCD = LP_CMPCD and IVT_MKTTP = L_MKTTP and IVT_LADNO = L_LADNO and  IVT_PRDCD =   L_PRDCD and IVT_PKGTP = L_PKGTP; 


set END_TABLE = 0; 
fetch C_INV  into  L_MKTTP,  L_INDNO,  L_PRDCD,  L_PKGTP, L_SALTP, L_DORNO,  L_LADNO,  L_BYRCD,  L_CNSCD,  L_DSRTP, L_DSRCD, L_INVQT,  L_INVRT;

end while;

close C_INV; 
commit;
end P1;

commit;



call spLdata.MR_INVRLSN('01','04/01/2008', '04/29/2008');
call spLdata.MR_INVRLSN('04','04/01/2008', '04/29/2008');
call spLdata.MR_INVRLSN('05','04/01/2008', '04/29/2008');
commit;
;

select count(*) from spldata.tt_ivrsn;


select a.ivt_stsfl,a.ivt_invno,a.ivt_invdt,a.ivt_prdds,a.ivt_invqt*a.ivt_invrt ivt_invvl,a.ivt_cclvl,a.ivt_comvl,a.ivt_frtvl,a.ivt_crdvl,a.ivt_rsnvl,b.ivt_cclvl,b.ivt_comvl,b.ivt_frtvl,b.ivt_crdvl,b.ivt_rsnvl from spldata.tt_ivrsn a, spldata.mr_ivtrn b where a.ivt_MKTTP = b.ivt_MKTTP and a.ivt_INVNO = b.ivt_INVNO and a.ivt_PRDCD = b.ivt_PRDCD and a.ivt_PKGTP = b.ivt_PKGTP and  substr(a.ivt_prdcd,1,4)='5111' and  ifnull(a.ivt_rsnvl,0) <> ifnull(b.ivt_rsnvl,0);

update spldata.mr_ivtrn set ivt_invqt=ivt_invqt where CONVERT(varchar,ivt_invdt,101) between '04/01/2005' and '08/30/2005';
commit;

update spldata.mr_ivtrn set ivt_invqt=ivt_invqt where ivt_invno='60003039';
commit;

--drop table spldata.tt_ivrsn;
--commit;



create table spldata.tt_ivrsn like spldata.mr_ivtrn;
commit;

call spldata.MR_INVRLSN('01','04/01/2007','09/30/2007');
call spldata.MR_INVRLSN('04','04/01/2007','09/30/2007');
call spldata.MR_INVRLSN('05','04/01/2007','09/30/2007');
commit;

select * from spldata.chkmsg;


select ivt_rsnvl, ivt_cclvl, ivt_comvl, ivt_frtvl,  int( round(ivt_invrt*ivt_invqt,2))  ivt_invvl from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101) = '05/31/2005';
commit;

update spldata.mr_ivtrn set ivt_rsnvl = ifnull(ivt_invqt,0)*ifnull(ivt_invrt,0) - ifnull(ivt_cclvl,0) - ifnull(ivt_comvl,0) - ifnull(ivt_frtvl,0)  where  ivt_invno='60001925';
commit;


select ivt_rsnvl/ivt_invqt ivt_rsnrt, ivt_cclvl, ivt_comvl, ivt_frtvl,  ivt_invrt,ivt_invdt from spldata.mr_ivtrn where ivt_invno='60001925';
commit;


select ivt_mkttp,ivt_invno,CONVERT(varchar,ivt_invdt,101),ivt_prdds,ivt_invqt,ivt_invrt,ivt_invvl,ivt_rsnvl,ivt_crdvl from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101)  between  '12/25/2004' and '12/25/2004';
select * from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101)>'04/01/2004';


alter table spldata.mr_ivtrn drop column ivr_crdvl;
commit;

alter table spldata.mr_ivtrn add  column ivt_crdvl decimal(10,2);
commit;






