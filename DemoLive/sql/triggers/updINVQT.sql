commit;

drop trigger spldata.updINVQT;
commit;


create trigger spldata.updINVQT  after update of IVT_INVQT on spldata.MR_IVTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 

 begin 
 declare L_TOT_INVQT decimal(10,3) default 0.000; 
 declare L_TOT_DORQT decimal(10,3) default 0.000; 
 declare L_RUN_INVQT decimal(10,3) default 0.000; 
 declare L_SET_INVQT decimal(10,3) default 0.000; 

 declare L_DOD_CMPCD char(2); 
 declare L_DOD_MKTTP char(2); 
 declare L_DOD_DORNO char(8); 
 declare L_DOD_PRDCD char(10); 
 declare L_DOD_PKGTP char(2); 
 declare L_DOD_DSPDT date; 
 declare L_DOD_DORQT decimal(10,3); 
 declare L_DOD_SRLNO char(2); 
 declare END_TABLE int default 0; 

     declare L_CCL_VAL decimal(10,2) default 0.00; 
     declare L_COM_VAL decimal(10,2) default 0.00; 
     declare L_TDM_VAL decimal(10,2) default 0.00; 

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

     declare L_IVT_TD1VL decimal(10,2) default 0.00; 
     declare L_IVT_TD2VL decimal(10,2) default 0.00; 
     declare L_IVT_TD3VL decimal(10,2) default 0.00; 


     declare L_INVQT decimal(10,3) default 0.00; 
     declare L_INVRT decimal(10,2) default 0.00; 
     declare L_EXCVL decimal(10,2) default 0.00; 
     declare L_EDCVL decimal(10,2) default 0.00; 
     declare L_EHCVL decimal(10,2) default 0.00; 
     declare L_CMPCD char(2); 
     declare L_MKTTP char(2); 
     declare L_INDNO char(9); 
     declare L_BKGDT date; 
     declare L_PRDCD char(10); 
     declare L_PKGTP char(2); 
     declare L_BYRCD char(5); 
     declare L_CNSCD char(5); 
     declare L_DSRTP char(1); 
     declare L_DSRCD char(5); 
     declare L_GRPCD char(5); 
     declare L_SALTP char(2); 

     declare L_DORNO char(8); 
     declare L_LADNO char(8); 


     declare L_GRPCD1 char(5) default 'V0012';
     declare L_COMCT char(2);
     declare L_COMRT decimal(10,2) default 0.00; 





declare C1 cursor for select DOD_CMPCD,DOD_MKTTP, DOD_DORNO, DOD_PRDCD, DOD_PKGTP, DOD_DSPDT, DOD_DORQT,DOD_SRLNO from spldata.MR_DODEL 
 where DOD_CMPCD = new_row.IVT_CMPCD and DOD_MKTTP = new_row.IVT_MKTTP and DOD_DORNO = new_row.IVT_DORNO and DOD_PRDCD = new_row.IVT_PRDCD and DOD_PKGTP = new_row.IVT_PKGTP and ifnull(dod_dorqt,0)>0 and dod_stsfl <> 'X'  order by dod_mkttp,dod_dorno,dod_prdcd,dod_pkgtp,dod_dspdt; 


declare continue handler for not found 
     set END_TABLE = 1; 

    set L_INVQT  = ifnull(new_row.ivt_invqt,0);
    set L_INVRT  =  ifnull(new_row.ivt_invrt,0);
    set L_EXCVL  =  ifnull(new_row.ivt_excvl,0);
    set L_EDCVL  =  ifnull(new_row.ivt_edcvl,0);
    set L_EHCVL  =  ifnull(new_row.ivt_ehcvl,0);
    set L_CMPCD  = new_row.ivt_cmpcd;
    set L_MKTTP  = new_row.ivt_mkttp;
    set L_INDNO  = new_row.ivt_indno;
    set L_PRDCD  = new_row.ivt_prdcd;
    set L_PKGTP  = new_row.ivt_pkgtp;
    set L_DORNO  = new_row.ivt_dorno;
    set L_LADNO  = new_row.ivt_ladno;
    set L_BYRCD  = new_row.ivt_byrcd;
    set L_CNSCD  = new_row.ivt_cnscd;
    set L_DSRTP  = new_row.ivt_dsrtp;
    set L_DSRCD  = new_row.ivt_dsrcd;
    set L_SALTP  = new_row.ivt_saltp;

update spldata.MR_DOTRN 
 set DOT_TRNFL ='0', DOT_INVQT =  ifnull(DOT_INVQT,0) +  (ifnull(new_row.IVT_INVQT,0)  -  ifnull(old_row.IVT_INVQT,0)) 
 where DOT_CMPCD = new_row.IVT_CMPCD and DOT_MKTTP = new_row.IVT_MKTTP  and DOT_DORNO = new_row.IVT_DORNO  and DOT_PRDCD = new_row.IVT_PRDCD and DOT_PKGTP = new_row.IVT_PKGTP;

update spldata.MR_INTRN  set INT_TRNFL='0', INT_INVQT =   ifnull(INT_INVQT,0) + (ifnull(new_row.IVT_INVQT,0) - ifnull(old_row.IVT_INVQT,0)) 
 where INT_CMPCD = new_row.IVT_CMPCD and INT_MKTTP = new_row.IVT_MKTTP  and INT_INDNO = new_row.IVT_INDNO  and INT_PRDCD = new_row.IVT_PRDCD  and INT_PKGTP = new_row.IVT_PKGTP;

update spldata.MM_WBTRN set WB_TRNFL='0', WB_CHLQT = ifnull(WB_CHLQT,0) + (ifnull(new_row.IVT_INVQT,0)-ifnull(old_row.IVT_INVQT,0)) where WB_CMPCD = new_row.IVT_CMPCD and WB_DOCTP='03' and WB_DOCNO = new_row.IVT_GINNO;

    select ifnull(int_cc1vl,0) int_cc1vl, ifnull(int_cc2vl,0) int_cc2vl, ifnull(int_cc3vl,0) int_cc3vl, ifnull(int_cc1rf,'') int_cc1rf, ifnull(int_cc2rf,'') int_cc2rf, ifnull(int_cc3rf,'') int_cc3rf  into L_CC1RT, L_CC2RT, L_CC3RT, L_CC1RF, L_CC2RF, L_CC3RF from spldata.mr_intrn where int_CMPCD = L_CMPCD and int_mkttp=L_MKTTP and int_indno = L_indno and int_prdcd = L_prdcd and int_pkgtp = L_pkgtp;


    select ifnull(DOT_FRTRT,0) dot_frtrt  into L_FRTRT from spldata.mr_dotrn where dot_CMPCD = L_CMPCD and dot_mkttp=L_mkttp and dot_dorno = L_dorno and dot_prdcd = L_prdcd and  dot_pkgtp = L_pkgtp;
    set L_FRT_VAL = L_INVQT*L_FRTRT;

   -- ******** Value is multiplied by 0.95 to consider 5% TDS ********

  set L_IVT_CC1VL = 0.00;
  set L_IVT_CC2VL = 0.00;
  set L_IVT_CC3VL = 0.00;
  set L_IVT_TD1VL = 0.00;
  set L_IVT_TD2VL = 0.00;
  set L_IVT_TD3VL = 0.00;

  -- 5% TDS is applicable for Distributor & Third party credit discount
   if (L_CC1RT>0 ) and substr(ifnull(L_CC1RF,' '),1,1)='C'  then 
      set L_IVT_CC1VL =  (L_INVQT*L_CC1RT);
   end if;

   if (L_CC2RT>0 ) and substr(ifnull(L_CC2RF,' '),1,1)='D'  then 
      set L_IVT_CC2VL =  (L_INVQT*L_CC2RT);
      set L_IVT_TD2VL =  (L_INVQT*L_CC2RT)*0.05;
   end if;


   if (L_CC3RT>0 ) and substr(ifnull(L_CC3RF,' '),1,1) in ('D','C')  then 
      set L_IVT_CC3VL =  (L_INVQT*L_CC3RT);
      set L_IVT_TD3VL =  (L_INVQT*L_CC3RT)*0.05;
   end if;

    set L_CCL_VAL  = L_IVT_CC1VL+L_IVT_CC2VL+L_IVT_CC3VL;

    select ifnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp='C'  and pt_prtcd = L_BYRCD;

    set L_COMRT = 0;
    set L_COM_VAL = 0.00;
    set L_TDM_VAL = 0.00;

    call spldata.getDCMRT(L_DSRTP,L_DSRCD,L_PRDCD,L_SALTP,L_GRPCD,L_COMRT);

    set L_COM_VAL = (L_COMRT*L_INVQT);
    set L_TDM_VAL = (L_COMRT*L_INVQT)*0.05;

  set L_RSN_VAL = (L_INVQT * L_INVRT) - (L_CCL_VAL+L_COM_VAL+L_FRT_VAL);
   select ifnull(in_cptvl,0) in_cptvl into L_CPTVL from spldata.mr_inmst where in_CMPCD = L_CMPCD and in_mkttp=L_MKTTP and in_indno = L_INDNO;
   select in_bkgdt into L_BKGDT from spldata.mr_inmst where in_CMPCD = L_CMPCD and in_mkttp=L_MKTTP and in_indno = L_INDNO;
   set L_CRD_VAL = 0.00;
   if L_CPTVL > 0 then
       set L_CRD_VAL = ((L_INVQT * L_INVRT*0.10)/365)*L_CPTVL;
   end if;

    --insert into spldata.temp_chk values ('L_CCL_VAL '||char(L_CCL_VAL)||'  L_COM_VAL : '||char(L_COM_VAL)||'  L_RSN_VAL : '||char(L_RSN_VAL));

update spldata.mr_ivtrn set ivt_bkgdt = L_BKGDT, ivt_frtvl=L_FRT_VAL, ivt_cclvl =  L_CCL_VAL , ivt_comvl = L_COM_VAL,ivt_tdmvl = L_TDM_VAL, IVT_CRDVL = L_CRD_VAL, IVT_RSNVL = L_RSN_VAL, ivt_cc1rf = l_cc1rf, ivt_cc2rf = l_cc2rf, ivt_cc3rf = l_cc3rf,ivt_cc1vl=l_ivt_cc1vl,ivt_cc2vl=l_ivt_cc2vl,ivt_cc3vl=l_ivt_cc3vl,ivt_td1vl=l_ivt_td1vl,ivt_td2vl=l_ivt_td2vl,ivt_td3vl=l_ivt_td3vl  where IVT_CMPCD = L_CMPCD and IVT_MKTTP = L_MKTTP and IVT_LADNO = L_LADNO and  IVT_PRDCD =   L_PRDCD and IVT_PKGTP = L_PKGTP; 


select sum(IVT_INVQT) into L_TOT_INVQT from spldata.MR_IVTRN where IVT_CMPCD = new_row.IVT_CMPCD and IVT_MKTTP = new_row.IVT_MKTTP and IVT_DORNO = new_row.IVT_DORNO and IVT_PRDCD = new_row.IVT_PRDCD and  IVT_PKGTP = new_row.IVT_PKGTP; 

select sum(DOT_DORQT)  into L_TOT_DORQT from spldata.MR_DOTRN  where DOT_CMPCD = new_row.IVT_CMPCD and DOT_MKTTP = new_row.IVT_MKTTP and DOT_DORNO = new_row.IVT_DORNO and DOT_PRDCD = new_row.IVT_PRDCD and DOT_PKGTP = new_row.IVT_PKGTP; 

set L_RUN_INVQT = 0; 
update spldata.MR_DODEL SET DOD_TRNFL='0', DOD_LADQT = 0, dod_stsfl= ' '  where DOD_CMPCD = new_row.IVT_CMPCD and DOD_MKTTP = new_row.IVT_MKTTP and DOD_DORNO = new_row.IVT_DORNO and DOD_PRDCD =   new_row.IVT_PRDCD and DOD_PKGTP = new_row.IVT_PKGTP;

if new_row.ivt_reqqt>new_row.ivt_ladqt and new_row.ivt_ladqt>0  then
   update spldata.mr_ivtrn set ivt_reqqt = ivt_ladqt where ivt_CMPCD = new_row.IVT_CMPCD and ivt_mkttp=new_row.ivt_mkttp and ivt_ladno=new_row.ivt_ladno and ivt_prdcd=new_row.ivt_prdcd and ivt_pkgtp=new_row.ivt_pkgtp;
   update spldata.mr_dotrn set dot_ladqt = L_TOT_INVQT where dot_CMPCD = new_row.IVT_CMPCD and dot_mkttp=new_row.ivt_mkttp and dot_dorno=new_row.ivt_dorno and dot_prdcd=new_row.ivt_prdcd and  dot_pkgtp=new_row.ivt_pkgtp;
end if;

open C1;
fetch C1 into L_DOD_CMPCD,L_DOD_MKTTP, L_DOD_DORNO, L_DOD_PRDCD, L_DOD_PKGTP, L_DOD_DSPDT, L_DOD_DORQT,L_DOD_SRLNO; 
while END_TABLE = 0 DO 

   set L_SET_INVQT = (L_TOT_INVQT - L_RUN_INVQT); 
   if L_SET_INVQT > L_DOD_DORQT then 
       set L_SET_INVQT = L_DOD_DORQT; 
   end if; 
   set L_RUN_INVQT = L_RUN_INVQT + L_SET_INVQT;
 
    if L_SET_INVQT > 0 then
        update spldata.MR_DODEL set DOD_TRNFL='0', DOD_LADQT = L_SET_INVQT, DOD_STSFL='D'  where DOD_CMPCD = L_DOD_CMPCD and DOD_MKTTP = L_DOD_MKTTP and DOD_DORNO = L_DOD_DORNO and DOD_PRDCD =   L_DOD_PRDCD and DOD_PKGTP = L_DOD_PKGTP and DOD_DSPDT= L_DOD_DSPDT and ifnull(dod_dorqt,0)>0 and DOD_STSFL <> 'X'; 
    end if;
    set END_TABLE=0;
    fetch C1 into L_DOD_CMPCD,L_DOD_MKTTP, L_DOD_DORNO, L_DOD_PRDCD, L_DOD_PKGTP, L_DOD_DSPDT, L_DOD_DORQT,L_DOD_SRLNO; 
   end while; 
   close C1; 
   
    if ifnull(new_row.ivt_saltp,'  ') = '04' then
       update spldata.mr_ivtr1 set ivt_invqt = ifnull(ivt_invqt,0)+(ifnull(new_row.ivt_invqt,0) - ifnull(old_row.ivt_invqt,0)) where ivt_CMPCD = new_row.IVT_CMPCD and ivt_mkttp=new_row.ivt_mkttp and ivt_ladno = new_row.ivt_ladno and ivt_prdcd = new_row.ivt_prdcd and ivt_pkgtp = new_row.ivt_pkgtp;
    end if;

end;

commit;

	
