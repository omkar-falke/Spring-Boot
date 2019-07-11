drop trigger spldata.updRLSN_DOR;
commit;


create trigger spldata.updRLSN_DOR  after update of DOT_STSFL on spldata.MR_DOTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 

  begin
 declare L_RSNRT decimal(12,2) default 0.00; 
 declare L_FRTVL decimal(12,2) default 0.00; 
 declare L_RLSN_IND decimal(14,2) default 0.00; 
 declare L_RLSN_VAL decimal(14,2) default 0.00; 

 declare L_MKTTP char(2); 
 declare L_INDNO char(9); 
 declare L_PRDCD char(10); 
 declare L_PKGTP char(2); 


if new_row.DOT_STSFL in ( '1','O','A')  then 
    select ifnull(int_rsnvl,0)/ifnull(int_indqt,1)  int_rsnrt into L_RSNRT from spldata.mr_intrn where int_cmpcd = new_row.dot_cmpcd and int_mkttp = new_row.dot_mkttp and int_indno = new_row.dot_indno and int_prdcd = new_row.dot_prdcd and int_pkgtp = new_row.dot_pkgtp;
    set L_FRTVL = ifnull(new_row.dot_dorqt,0)*ifnull(new_row.DOT_FRTRT,0);
    set L_RLSN_IND = ifnull(new_row.dot_dorqt,0) * L_RSNRT;
    set L_RLSN_VAL = L_RLSN_IND - L_FRTVL;
    update spldata.MR_DOTRN set DOT_INDRS = L_RLSN_IND, DOT_RSNVL = L_RLSN_VAL, DOT_FRTVL = L_FRTVL   where DOT_cmpcd = new_row.dot_cmpcd and DOT_MKTTP = new_row.DOT_MKTTP and DOT_DORNO = new_row.DOT_DORNO and DOT_PRDCD =   new_row.DOT_PRDCD and DOT_PKGTP = new_row.DOT_PKGTP; 

end if;
end;

commit;
