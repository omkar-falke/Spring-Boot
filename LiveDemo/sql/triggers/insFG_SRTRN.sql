
drop trigger spldata.insFG_SRTRN;
commit;

create trigger spldata.insFG_SRTRN after insert on spldata.FG_SRTRN referencing 
new as new_row for each row mode DB2ROW 
begin 
update spldata.PR_LTMST set LT_RESQT = ifnull(LT_RESQT,0) + ifnull(new_row.SR_RESQT,0) where LT_CMPCD = new_row.SR_CMPCD and LT_PRDTP = new_row.SR_PRDTP and LT_LOTNO = new_row.SR_LOTNO and LT_RCLNO = new_row.SR_RCLNO;
END;
commit;
