
drop trigger spldata.insRLSN_IND;
commit;


create trigger spldata.insRLSN_IND  before insert on spldata.MR_INTRN referencing new as new_row 
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
 declare L_GRPCD1 char(5) default 'V0012'; 
 declare L_INDQT decimal(10,3) default 0.00; 
 declare L_INDRT decimal(10,2) default 0.00; 
 declare L_COMCT char(2);
 declare L_COMRT decimal(10,2) default 0.00; 
 declare L_CPTVL decimal(10,2) default 0.00; 


if upper(ifnull(new_row.INT_STSFL,' ')) in ( '1','O','A')  then 

    set L_IND_VAL = ifnull(new_row.INT_INDQT,0)*ifnull(new_row.INT_BASRT,0);

    set L_CCL_VAL =  (ifnull(new_row.INT_INDQT,0)*ifnull(new_row.INT_CC1VL,0)) + (ifnull(new_row.INT_INDQT,0)*ifnull(new_row.INT_CC2VL,0))+ (ifnull(new_row.INT_INDQT,0)*ifnull(new_row.INT_CC3VL,0));

select IN_BKGDT,IN_DSRTP,IN_DSRCD,IN_SALTP, IN_BYRCD  into  L_BKGDT,L_DSRTP,L_DSRCD,L_SALTP, L_BYRCD  from spldata.MR_INMST where IN_CMPCD = new_row.int_cmpcd and IN_MKTTP = new_row.int_mkttp and in_indno = new_row.int_indno;
   select ifnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp='C'  and pt_prtcd = L_BYRCD;

   set L_COMRT = 0;
   set L_COM_VAL = 0;
   set L_PRDCD = new_row.INT_PRDCD;
  -- call spldata.getDCMRT(L_DSRTP, L_DSRCD, L_PRDCD, L_SALTP, L_GRPCD, L_COMRT);
    set L_COMCT = 'XX';

      select ifnull(CMT_CCSVL,'XX') into L_COMCT from spldata.CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCL' and cmt_codcd =  L_SALTP||'_'||substr(L_PRDCD,1,4);
      if L_COMCT = 'XX' then
          select ifnull(CMT_CCSVL,'XX') into L_COMCT from spldata.CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCL' and cmt_codcd =  L_SALTP||'_'||substr(L_PRDCD,1,3)||'X';
          if L_COMCT = 'XX' then
             select ifnull(CMT_CCSVL,'XX') into L_COMCT from spldata.CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCL' and cmt_codcd =  L_SALTP||'_'||substr(L_PRDCD,1,2)||'XX';
          end if;
      end if;


      set L_COMRT = -1;
      if L_COMCT <> 'XX' then
         if  substr(L_PRDCD,1,6) not in ( '519795','529795','519895','529895')  then 
             if L_GRPCD = L_GRPCD1 then 
                select ifnull(CMT_NCSVL,0)  into L_COMRT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and cmt_codcd=L_DSRTP||L_DSRCD||'_'||L_GRPCD1||'_'||L_COMCT;
             end if;
             if L_COMRT = -1  then 
                select ifnull(CMT_NCSVL,0)  into L_COMRT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and cmt_codcd=L_DSRTP||L_DSRCD||'_XXXXX_'||L_COMCT;
             end if;
         end if;    
      end if;

      if L_COMRT = -1 then
         set L_COMRT = 0;
      end if;

   set L_COM_VAL =  (ifnull(new_row.INT_INDQT,0)*ifnull(L_COMRT,0));
   set L_RLSN_VAL = L_IND_VAL-(L_CCL_VAL+L_COM_VAL);
   select ifnull(in_cptvl,0) in_cptvl into L_CPTVL from spldata.mr_inmst where in_cmpcd=new_row.INT_cmpcd and in_mkttp=new_row.INT_MKTTP and in_indno = new_row.INT_INDNO;
   set L_CRD_VAL = 0.00;
   if L_CPTVL > 0 then
       set L_CRD_VAL = ((L_RLSN_VAL*0.10)/365)*L_CPTVL;
   end if;



   set new_row.INT_INDVL = L_IND_VAL ;
   set new_row.INT_CCLVL =L_CCL_VAL ;
   set new_row.INT_COMVL = L_COM_VAL;
   set new_row.INT_CRDVL = L_CRD_VAL;
   set new_row.INT_RSNVL = L_RLSN_VAL ;

end if;
end;

commit;


