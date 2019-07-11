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
--  Commission Rate 		                     : cmt_nmp01     DOC/MRxxINDssss
						  xx : ivt_mkttp                      Market Type
						  ssss : substr(int_indno,1,4)   Indent Series
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


********** Not in Use **********
drop trigger spldata.invRLSN;
commit;

create trigger spldata.invRLSN  after update of IVT_INVQT on spldata.MR_IVTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 

  begin
     declare L_CCL_VAL decimal(10,2) default 0.00; 
     declare L_COM_VAL decimal(10,2) default 0.00; 
     declare L_FRT_VAL decimal(10,2) default 0.00; 
     declare L_CRD_VAL decimal(10,2) default 0.00; 
     declare L_RSN_VAL decimal(10,2) default 0.00; 

     declare L_FRTRT decimal(10,2) default 0.00; 
     declare L_CC1RT decimal(10,2) default 0.00; 
     declare L_CC2RT decimal(10,2) default 0.00; 
     declare L_CC3RT decimal(10,2) default 0.00; 
     declare L_CPTVL decimal(10,2) default 0.00; 
     declare L_CC1RF varchar(6); 
     declare L_CC2RF varchar(6);
     declare L_CC3RF varchar(6);

     declare L_IVT_CC1VL decimal(10,2) default 0.00; 
     declare L_IVT_CC2VL decimal(10,2) default 0.00; 
     declare L_IVT_CC3VL decimal(10,2) default 0.00; 


     declare L_INVQT decimal(10,3) default 0.00; 
     declare L_INVRT decimal(10,2) default 0.00; 
     declare L_EXCVL decimal(10,2) default 0.00; 
     declare L_EDCVL decimal(10,2) default 0.00; 
     declare L_ECHVL decimal(10,2) default 0.00; 
     declare L_MKTTP char(2); 
     declare L_INDNO char(9); 
     declare L_BKGDT date; 
     declare L_PRDCD char(10); 
     declare L_PKGTP char(2); 
     declare L_BYRCD char(5); 
     declare L_CNSCD char(5); 
     declare L_DSRCD char(5); 

     declare L_DORNO char(8); 
     declare L_LADNO char(8); 


     declare L_COMRT decimal(10,2) default 0.00; 


    set L_INVQT  = ifnull(new_row.ivt_invqt,0);
    set L_INVRT  =  ifnull(new_row.ivt_invrt,0);
    set L_EXCVL  =  ifnull(new_row.ivt_excvl,0);
    set L_EDCVL  =  ifnull(new_row.ivt_edcvl,0);
    set L_ECHVL  =  ifnull(new_row.ivt_echvl,0);
    set L_MKTTP  = new_row.ivt_mkttp;
    set L_INDNO  = new_row.ivt_indno;
    set L_PRDCD  = new_row.ivt_prdcd;
    set L_PKGTP  = new_row.ivt_pkgtp;
    set L_DORNO  = new_row.ivt_dorno;
    set L_LADNO  = new_row.ivt_ladno;
    set L_BYRCD  = new_row.ivt_byrcd;
    set L_CNSCD  = new_row.ivt_cnscd;
    set L_DSRCD  = new_row.ivt_dsrcd;


    select ifnull(int_cc1vl,0) int_cc1vl, ifnull(int_cc2vl,0) int_cc2vl, ifnull(int_cc3vl,0) int_cc3vl, ifnull(int_cc1rf,'') int_cc1rf, ifnull(int_cc2rf,'') int_cc2rf, ifnull(int_cc3rf,'') int_cc3rf  into L_CC1RT, L_CC2RT, L_CC3RT, L_CC1RF, L_CC2RF, L_CC3RF from spldata.mr_intrn where int_mkttp=L_MKTTP and int_indno = L_indno and int_prdcd = L_prdcd and int_pkgtp = L_pkgtp;


    select ifnull(DOT_FRTRT,0) dot_frtrt  into L_FRTRT from spldata.mr_dotrn where dot_mkttp=L_mkttp and dot_dorno = L_dorno and dot_prdcd = L_prdcd and  dot_pkgtp = L_pkgtp;
    set L_FRT_VAL = L_INVQT*L_FRTRT;

   -- ******** Value is multiplied by 0.95 to consider 5% TDS ********

  set L_IVT_CC1VL = 0.00;
  set L_IVT_CC2VL = 0.00;
  set L_IVT_CC3VL = 0.00;

  -- 5% TDS is applicable for Distributor & Third party credit discount
   if (L_CC1RT>0 ) then 
      if (L_CC1RF = 'D'||L_DSRCD)  then set L_IVT_CC1VL = (L_INVQT*L_CC1RT)*0.95;   end if;
      if (L_CC1RF = 'C'||L_CNSCD)  then set L_IVT_CC1VL =  (L_INVQT*L_CC1RT);  else  set L_IVT_CC2VL =  (L_INVQT*L_CC1RT)*0.95;  end if;
   end if;

   if (L_CC2RT>0 ) then 
      if (L_CC2RF = 'D'||L_DSRCD)  then set L_IVT_CC2VL = (L_INVQT*L_CC2RT)*0.95;   end if;
      if (L_CC2RF = 'C'||L_CNSCD)  then set L_IVT_CC2VL =  (L_INVQT*L_CC2RT);  else  set L_IVT_CC2VL =  (L_INVQT*L_CC2RT)*0.95;  end if;
   end if;


   if (L_CC3RT>0 ) then 
      if (L_CC3RF = 'D'||L_DSRCD)  then set L_IVT_CC3VL = (L_INVQT*L_CC3RT)*0.95;   end if;
      if (L_CC3RF = 'C'||L_CNSCD)  then set L_IVT_CC3VL =  (L_INVQT*L_CC3RT);  else  set L_IVT_CC3VL =  (L_INVQT*L_CC3RT)*0.95;  end if;
   end if;


    set L_CCL_VAL  = L_IVT_CC1VL+L_IVT_CC2VL+L_IVT_CC3VL;

    select ifnull(CMT_NMP01,0) cmt_nmp01  into  L_COMRT from spldata.CO_CDTRN where CMT_CGMTP||CMT_CGSTP||CMT_CODCD =   'DOCMR'||L_MKTTP||'IND'||substr(L_INDNO,1,4);

   -- Distributor commission is applicable for prime grade ***substr(L_PRDCD,7,1) = '0' ***
    set L_COM_VAL = 0.00;
 
    if (substr(L_PRDCD,1,6) <> '519895' and substr(L_PRDCD,1,6) <> '529895')  then set L_COM_VAL = (L_COMRT*L_INVQT)*0.95; end if;


  set L_RSN_VAL = (L_INVQT * L_INVRT) - (L_CCL_VAL+L_COM_VAL+L_FRT_VAL);

   select ifnull(in_cptvl,0) in_cptvl into L_CPTVL from spldata.mr_inmst where in_mkttp=L_MKTTP and in_indno = L_INDNO;
   select in_bkgdt into L_BKGDT from spldata.mr_inmst where in_mkttp=L_MKTTP and in_indno = L_INDNO;
   set L_CRD_VAL = 0.00;
   if L_CPTVL > 0 then
       set L_CRD_VAL = ((L_RSN_VAL*0.10)/365)*L_CPTVL;
   end if;


update spldata.mr_ivtrn set ivt_bkgdt = L_BKGDT, ivt_frtvl=L_FRT_VAL, ivt_cclvl =  L_CCL_VAL , ivt_comvl = L_COM_VAL, IVT_CRDVL = L_CRD_VAL, IVT_RSNVL = L_RSN_VAL, ivt_cc1rf = l_cc1rf, ivt_cc2rf = l_cc2rf, ivt_cc3rf = l_cc3rf,ivt_cc1vl=l_ivt_cc1vl,ivt_cc2vl=l_ivt_cc2vl,ivt_cc3vl=l_ivt_cc3vl  where IVT_MKTTP = L_MKTTP and IVT_LADNO = L_LADNO and  IVT_PRDCD =   L_PRDCD and IVT_PKGTP = L_PKGTP; 


end;

commit;

update spldata.mr_ivtrn set ivt_rsnvl = (ifnull(ivt_invqt,0)*ifnull(ivt_invrt,0)) - (ifnull(ivt_cclvl,0)+ifnull(ivt_comvl,0)+ifnull(ivt_frtvl,0)) where ifnull(ivt_invqt,0)>0 and ifnull(ivt_crdvl,0)>0;
commit;

select ivt_invno, ivt_invqt,ivt_invvl,ivt_rsnvl,ivt_crdvl from spldata.mr_ivtrn where date(ivt_invdt)='12/25/2004' and ivt_invno='50007459';

update spldata.mr_ivtrn set ivt_rsnvl=0,ivt_crdvl=0 where date(ivt_invdt)='12/25/2004' and ivt_invno='50007459';
commit;

