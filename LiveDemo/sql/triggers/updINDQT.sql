commit;

drop trigger spldata.updINDQT;
commit;

create trigger spldata.updINDQT  after update of INT_INDQT on spldata.MR_INTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 
 begin 
update spldata.mr_octrn set oct_indqt= ifnull(oct_indqt,0)+(ifnull(new_row.int_indqt,0)-ifnull(old_row.int_indqt,0)), oct_lupdt = current_date  where oct_cmpcd=new_row.int_cmpcd and  oct_mkttp=new_row.int_mkttp and  oct_ocfno=new_row.int_ocfno and oct_prdcd=new_row.int_prdcd and oct_pkgtp = new_row.int_pkgtp;
end;

commit;


;


