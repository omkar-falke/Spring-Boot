drop trigger spldata.updDORQT;
commit;


create trigger spldata.updDORQT  after update of DOT_DORQT on spldata.MR_DOTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 
 begin 
update spldata.mr_intrn set int_dorqt= int_dorqt+(new_row.dot_dorqt-old_row.dot_dorqt), int_lupdt = current_date  where int_cmpcd=new_row.dot_cmpcd and int_mkttp=new_row.dot_mkttp and  int_indno=new_row.dot_indno and int_prdcd=new_row.dot_prdcd and int_pkgtp = new_row.dot_pkgtp;
end;

commit;




