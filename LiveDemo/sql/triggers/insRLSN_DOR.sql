commit;
drop trigger spldata.insRLSN_DOR;
commit;


create trigger spldata.insRLSN_DOR  before insert on spldata.MR_DOTRN referencing new as new_row 
 for each row mode DB2ROW 

  begin
 declare L_RLSRT decimal(12,2) default 0.00; 
 declare L_FRTVL decimal(12,2) default 0.00; 
 declare L_RLSN_IND decimal(14,2) default 0.00; 
 declare L_RLSN_VAL decimal(14,2) default 0.00; 

 declare L_DORDT date; 
 declare L_MKTTP char(2); 
 declare L_DORNO char(9); 
 declare L_PRDCD char(10); 
 declare L_PKGTP char(2); 
 declare L_DORQT decimal(10,3) default 0.00; 


if upper(ifnull(new_row.DOT_STSFL,' ')) in ( '1','O','A')  then 

   select ifnull(INT_RSNVL,0)/ifnull(int_indqt,1) int_rsnrt into L_RLSRT from spldata.mr_intrn where int_cmpcd=new_row.dot_cmpcd and int_mkttp=new_row.dot_mkttp and int_indno = new_row.dot_indno and int_prdcd = new_row.dot_prdcd and int_pkgtp = new_row.dot_pkgtp;
   set L_FRTVL = ifnull(new_row.dot_dorqt,0)*ifnull(new_row.dot_frtrt,0);
   set L_RLSN_IND = (new_row.dot_dorqt * L_RLSRT) ;
   set L_RLSN_VAL = L_RLSN_IND - L_FRTVL;

   set new_row.DOT_INDRS = L_RLSN_IND;
   set new_row.DOT_FRTVL = L_FRTVL;
   set new_row.DOT_RSNVL = L_RLSN_VAL;

end if;
end;

commit;

