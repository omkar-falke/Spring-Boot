drop trigger spldata.updFG_SRTRN;
commit;


create trigger spldata.updFG_SRTRN  after update of SR_RESQT on spldata.FG_SRTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 

 begin 
   declare L_RESQT_CHG decimal(10,3);
   declare L_RESQT_LOT decimal(10,3);
   declare L_RESQT decimal(10,3);

   set L_RESQT_LOT = 0.000;
   set L_RESQT_CHG = ifnull(new_row.sr_resqt,0)-ifnull(old_row.sr_resqt,0);
   select ifnull(lt_resqt,0) into L_RESQT_LOT from spldata.PR_LTMST where lt_cmpcd = old_row.sr_cmpcd and  lt_prdtp = old_row.sr_prdtp and  lt_lotno = old_row.sr_lotno and lt_rclno = old_row.sr_rclno;

   set L_RESQT = L_RESQT_LOT+L_RESQT_CHG;
   if (L_RESQT_CHG +L_RESQT_LOT<0) then
      set L_RESQT = 0;
   end if;
update spldata.pr_ltmst set lt_resqt= L_RESQT, lt_trnfl = '0', lt_lupdt = current_date  where lt_cmpcd = old_row.sr_cmpcd and  lt_prdtp = old_row.sr_prdtp and  lt_lotno = old_row.sr_lotno and lt_rclno = old_row.sr_rclno;
end;

commit;
