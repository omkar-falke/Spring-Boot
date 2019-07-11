drop trigger spldata.delLTBDT;
commit;

create trigger spldata.delLTBDT after delete on spldata.PR_LTBDT referencing 
old as old_row for each row mode DB2ROW 
begin
update spldata.pr_btmst set bt_bagqt = bt_bagqt - old_row.ltb_bagqt where bt_cmpcd = old_row.ltb_cmpcd and bt_batno = old_row.ltb_batno and bt_grdcd = old_row.ltb_prdcd; 
END;
commit;