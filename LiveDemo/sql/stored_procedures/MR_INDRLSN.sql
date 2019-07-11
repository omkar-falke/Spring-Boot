

---------------------------------------------------------------------------------------------------------------------------------------------------------
--  Indent Realisation :
--  -------------------------

--  Indent Qty 			: int_indqt
--  Basic Rate 			: int_basrt
--  Consignee Discount Rate 	                      : int_cdcvl
--  Distributor Discount Rate 	                      : int_ddcvl
--  Third Party Discount Rate 	                      : int_tdcvl
--  Credit period		: in_cptvl
--  Commission Rate 		: A stored procedure getDCMRT is used to pickup commission rate for the distributor
--  Scarp grade 		: SUBSTRING(int_prdcd,1,6) = '519895'    or SUBSTRING(int_prdcd,1,6) = '529895'

--  a.       	Indent value = Indent Qty * Basic Rate
--  b.	Booking Discount Consignee  =  Indent Qty   *  Consignee Discount Rate
--  c.	Booking Discount Distributor  =  Indent Qty  *  Distributor Discount Rate
--  d. 	Booking Discount Third Party  =  Indent Qty *  Third party Discount Rate

--  A.	Total Booking Discount = b+c+d

--  B.	Commission =  Indent Qty * Commission rate   
--           	(For grades other than scrap)
--  C.	Realisation Value = a - (A+B)
--  D.	Credit Interest  Rate /day @ 10% of Realisation Value =  (C * 0.10)/365
--  E. 	Credit Interest Amount	= D * Credit Period


--  Realisation Value (Before Credit Interest) 	: int_rsnvl             	C
--  Credit Interest Amount		: int_crdvl		E
--  Booking Discount Value		: int_cclvl		A
--  Commission Value		: int_comvl		B
--  Indent Value		: int_indvl		a

----------------------------------------------------------------------------------------------------------------------------------------------------------

;

drop procedure spldata.MR_INDRLSN;
commit;


create procedure spldata.MR_INDRLSN(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 
 P1:
 begin
 declare L_IND_VAL decimal(12,2) default 0.00; 
 declare L_CCL_VAL decimal(12,2) default 0.00; 
 declare L_COM_VAL decimal(12,2) default 0.00; 
 declare L_CRD_VAL decimal(12,2) default 0.00; 
 declare L_RLSN_VAL decimal(12,2) default 0.00; 

 declare L_BKGDT date;
 declare L_MKTTP char(2);
 declare L_INDNO char(9);
 declare L_PRDCD char(10);
 declare L_PKGTP char(2);
 declare L_SALTP char(2);
 declare L_BYRCD char(5);
 declare L_DSRTP char(1);
 declare L_DSRCD char(5);
 declare L_GRPCD char(5);
 declare L_INDQT decimal(10,3) default 0.00;
 declare L_FCMQT decimal(10,3) default 0.00;
 declare L_INDRT decimal(12,2) default 0.00;
 declare L_CC1VL decimal(12,2) default 0.00; 
 declare L_CC2VL decimal(12,2) default 0.00; 
 declare L_CC3VL decimal(12,2) default 0.00; 
 declare L_CPTVL decimal(12,2) default 0.00; 
 declare L_RECCT int default 0; 
 declare L_EOFFL int default 0; 
 declare END_TABLE int default 0;

 declare L_COMRT decimal(10,2) default 0.00; 


 declare C_INT CURSOR FOR  select INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP, in_bkgdt, IN_SALTP,IN_BYRCD,IN_DSRTP,IN_DSRCD, isnull(INT_INDQT,0) INT_INDQT,isnull(INT_FCMQT,0) INT_FCMQT, isnull(INT_BASRT,0) INT_BASRT,isnull(INT_CC1VL,0) INT_CC1VL,isnull(INT_CC2VL,0) INT_CC2VL,isnull(INT_CC3VL,0) INT_CC3VL  from  spldata.VW_INTRN  where INT_CMPCD = LP_CMPCD and INT_MKTTP = LP_MKTTP and INT_STSFL<>'X' and  IN_BKGDT between CONVERT(varchar,LP_STRDT,101)  and CONVERT(varchar,LP_ENDDT,101)  order by INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP;


declare continue handler for not found 
       set END_TABLE = 1; 



set END_TABLE = 0; 
open C_INT;
fetch C_INT into L_MKTTP, L_INDNO, L_PRDCD, L_PKGTP, L_BKGDT, L_SALTP, L_BYRCD, L_DSRTP,L_DSRCD, L_INDQT,L_FCMQT, L_INDRT, L_CC1VL, L_CC2VL, L_CC3VL;

while END_TABLE = 0 DO 

    set L_IND_VAL = (isnull(L_INDQT,0)-isnull(L_FCMQT,0))*isnull(L_INDRT,0);
    set L_CCL_VAL =  ((isnull(L_INDQT,0)-isnull(L_FCMQT,0))*isnull(L_CC1VL,0)) + ((isnull(L_INDQT,0)-isnull(L_FCMQT,0))*isnull(L_CC2VL,0)) + ((isnull(L_INDQT,0)-isnull(L_FCMQT,0))*isnull(L_CC3VL,0));

    --insert into spldata.temp_chk values ('DOC' + 'MR' + L_MKTTP + 'IND' + SUBSTRING(L_INDNO,1,4));

   select isnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp='C'  and pt_prtcd = L_BYRCD;

   set L_COMRT = 0;
   set L_COM_VAL = 0.00;
   call spldata.getDCMRT(L_DSRTP,L_DSRCD , L_PRDCD , L_SALTP, L_GRPCD, L_COMRT);

    set L_COM_VAL =  ((isnull(L_INDQT,0)-isnull(L_FCMQT,0))*isnull(L_COMRT,0));
  
   set L_RLSN_VAL = L_IND_VAL-(L_CCL_VAL+L_COM_VAL);

   select isnull(in_cptvl,0) IN_CPTVL  into L_CPTVL from spldata.mr_inmst where in_CMPCD = LP_CMPCD and in_mkttp=L_MKTTP and in_indno = L_INDNO;
   set L_CRD_VAL = 0.00;
   if L_CPTVL > 0 then
       set L_CRD_VAL = ((L_RLSN_VAL*0.10)/365)*L_CPTVL;
   end if;


    update spldata.mr_intrn set INT_INDVL = L_IND_VAL, INT_CCLVL = L_CCL_VAL, INT_COMVL = L_COM_VAL,INT_CRDVL = L_CRD_VAL, INT_RSNVL = L_RLSN_VAL  where INT_CMPCD = LP_CMPCD and INT_MKTTP = L_MKTTP and INT_INDNO = L_INDNO and INT_PRDCD =   L_PRDCD and INT_PKGTP = L_PKGTP; 

    
    set END_TABLE = 0; 
    fetch C_INT into L_MKTTP, L_INDNO, L_PRDCD, L_PKGTP, L_BKGDT, L_SALTP, L_BYRCD, L_DSRTP,L_DSRCD, L_INDQT, L_FCMQT,L_INDRT, L_CC1VL, L_CC2VL, L_CC3VL;
end while;
close C_INT; 
commit;
end P1;

commit;


call spldata.MR_INDRLSN('01','01','04/01/2008', '11/15/2008');
call spldata.MR_INDRLSN('01','04','04/01/2008', '11/15/2008');
call spldata.MR_INDRLSN('01','05','04/01/2008', '11/15/2008');
commit;
;

---**************************************************************************************************************************************************
/* Distributor commission from co_ptmst (old logic) FOR REFERENCE
    -- if L_BKGDT<date('06/01/2005')   then
    --   select isnull(CMT_NMP01,0) CMT_NMP01  into  L_COMRT from spldata.CO_CDTRN where CMT_CGMTP + CMT_CGSTP + CMT_CODCD  ='DOC' + 'MR' + L_MKTTP + 'IND' + SUBSTRING(L_INDNO,1,4);
    - -end if;

    --  if L_SALTP = '01'  then 
    --     select isnull(PT_DMCRT,0) PT_DMCRT  into  L_COMRT from spldata.CO_PTMST where PT_PRTTP ='D' and PT_PRTCD=L_DSRCD;
    --  end if;
    --  if L_SALTP = '03'  then 
    --   select isnull(PT_DECRT,0) PT_DECRT  into  L_COMRT from spldata.CO_PTMST where PT_PRTTP ='D' and PT_PRTCD=L_DSRCD;
    -- end if;
*/
---**************************************************************************************************************************************************
