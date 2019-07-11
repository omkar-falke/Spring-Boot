drop trigger spldata.updFG_RCTRN;
commit;


create trigger spldata.updFG_RCTRN  after update of RCT_STSFL on spldata.FG_RCTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 

  begin
 

if new_row.RCT_RCTTP='30' and new_row.RCT_STSFL='2' and old_row.RCT_STSFL='1'  then 

   update spldata.mr_ivtrn set ivt_slrqt = ifnull(ivt_slrqt,0) + ifnull(new_row.rct_rctqt,0), ivt_slrrf =  new_row.rct_rctno where ivt_cmpcd=new_row.rct_cmpcd and ivt_invno=new_row.rct_issrf and ivt_prdcd = (select lt_prdcd from spldata.pr_ltmst where lt_cmpcd=new_row.rct_cmpcd and lt_lotno=new_row.rct_lotno and lt_rclno=new_row.rct_rclno) ;

end if;
end;

commit;

