drop trigger spldata.insLTBDT;
commit;

create trigger spldata.insLTBDT after insert on spldata.PR_LTBDT referencing 
new as new_row for each row mode DB2ROW 
begin
update spldata.pr_btmst set bt_bagqt = bt_bagqt + new_row.ltb_bagqt where bt_cmpcd = new_row.ltb_cmpcd and bt_batno = new_row.ltb_batno and bt_grdcd = new_row.ltb_prdcd; 
END;
commit;