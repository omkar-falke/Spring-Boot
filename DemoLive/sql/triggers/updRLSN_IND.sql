drop trigger spldata.updRLSN_IND;
commit;


create trigger spldata.updRLSN_IND  after update of INT_STSFL on spldata.MR_INTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 

  begin
 declare L_IND_VAL decimal(14,2) default 0.00; 
 declare L_CCL_VAL decimal(14,2) default 0.00; 
 declare L_COM_VAL decimal(14,2) default 0.00; 
 declare L_CRD_VAL decimal(14,2) default 0.00; 
 declare L_RLSN_VAL decimal(14,2) default 0.00; 

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
 declare L_INDRT decimal(10,2) default 0.00; 
 declare L_CPTVL decimal(10,2) default 0.00; 

declare L_COMRT decimal(10,2) default 0.00; 


if new_row.INT_STSFL in ( '1','O','A')  then 
    set L_IND_VAL = (ifnull(new_row.INT_INDQT,0)-ifnull(new_row.INT_FCMQT,0))*ifnull(new_row.INT_BASRT,0);
    set L_CCL_VAL =  ((ifnull(new_row.INT_INDQT,0)-ifnull(new_row.INT_FCMQT,0))*ifnull(new_row.INT_CC1VL,0)) + ((ifnull(new_row.INT_INDQT,0)-ifnull(new_row.INT_FCMQT,0))*ifnull(new_row.INT_CC2VL,0)) + ((ifnull(new_row.INT_INDQT,0)-ifnull(new_row.INT_FCMQT,0))*ifnull(new_row.INT_CC3VL,0));

select IN_BKGDT,IN_DSRTP,IN_DSRCD,IN_SALTP,IN_BYRCD  into  L_BKGDT,L_DSRTP,L_DSRCD,L_SALTP,L_BYRCD  from spldata.MR_INMST where IN_CMPCD = new_row.int_cmpcd and IN_MKTTP = new_row.int_mkttp and in_indno = new_row.int_indno;
   select ifnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp='C'  and pt_prtcd = L_BYRCD;

   set L_COMRT = 0;
   set L_COM_VAL = 0;
   set L_PRDCD = new_row.INT_PRDCD;
   call spldata.getDCMRT(L_DSRTP,L_DSRCD,L_PRDCD,L_SALTP,L_GRPCD, L_COMRT);
   set L_COM_VAL =  ((ifnull(new_row.INT_INDQT,0)-ifnull(new_row.INT_FCMQT,0))*ifnull(L_COMRT,0));

  set L_RLSN_VAL = L_IND_VAL-(L_CCL_VAL+L_COM_VAL);
   select ifnull(in_cptvl,0) in_cptvl into L_CPTVL from spldata.mr_inmst where in_CMPCD = new_row.int_cmpcd and in_mkttp=new_row.INT_MKTTP and in_indno = new_row.INT_INDNO;
   set L_CRD_VAL = 0.00;
   if L_CPTVL > 0 then
       set L_CRD_VAL = ((L_RLSN_VAL*0.10)/365)*L_CPTVL;
   end if;

    update spldata.MR_INTRN set INT_INDVL = L_IND_VAL, INT_CCLVL = L_CCL_VAL, INT_COMVL = L_COM_VAL, INT_CRDVL = L_CRD_VAL, INT_RSNVL = L_RLSN_VAL  where int_CMPCD = new_row.int_cmpcd and INT_MKTTP = new_row.INT_MKTTP and INT_INDNO = new_row.INT_INDNO and INT_PRDCD =   new_row.INT_PRDCD and INT_PKGTP = new_row.INT_PKGTP; 

end if;
end;

commit;
