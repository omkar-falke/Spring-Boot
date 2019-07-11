drop trigger spldata.insPTMST;
commit;

create trigger spldata.insPTMST after insert on spldata.CO_PTMST referencing 
new as new_row for each row mode DB2ROW 
begin 
insert into shndata.co_ptmst select * from spldata.co_ptmst where pt_prttp = new_row.pt_prttp and pt_prtcd = new_row.pt_prtcd;
END;
commit;
