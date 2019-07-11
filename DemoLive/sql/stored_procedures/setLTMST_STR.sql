-- For setting Reservation Qty and Despatch qty. against reservation in Lot Master

drop procedure spldata.setLTMST_STR;
commit;

create procedure spldata.setLTMST_STR(IN LP_CMPCD char(2),IN LP_PRDCD char(10), IN LP_PRDTP char(2))  language sql  modifies sql data 
 P1:
 begin
declare L_LOTNO varchar(8);
declare L_RCLNO varchar(2);
declare L_RESQT decimal(10,3);
declare L_RDSQT decimal(10,3);

declare END_TABLE int default 0;

declare C_STR CURSOR FOR  select sr_lotno,sr_rclno,sum(isnull(sr_resqt,0)),sum(isnull(sr_rdsqt,0)) from spldata.fg_srtrn  where  sr_cmpcd=LP_CMPCD and sr_prdcd = LP_PRDCD and sr_prdtp=LP_PRDTP and sr_lotno + sr_rclno in (select st_lotno + st_rclno  from spldata.fg_stmst where st_cmpcd=LP_CMPCD and st_prdcd = LP_PRDCD and st_prdtp=LP_PRDTP  and st_stkqt>0) group by sr_lotno,sr_rclno having sum(sr_resqt)>sum(sr_rdsqt);


declare continue handler for not found 
       set END_TABLE = 1;

   set end_table = 0;
   update spldata.fg_srtrn set sr_resqt=isnull(sr_rdsqt,0) where sr_stsfl='X' and isnull(sr_resqt,0)>isnull(sr_rdsqt,0) and sr_cmpcd=lp_cmpcd and sr_prdcd = LP_PRDCD and sr_prdtp = lp_prdtp;
   commit;
   open C_STR;
   fetch C_STR  into L_LOTNO, L_RCLNO, L_RESQT, L_RDSQT;
   while END_TABLE = 0 DO 
      update spldata.pr_ltmst set lt_resqt = L_RESQT, lt_rdsqt = L_RDSQT  where LT_CMPCD = LP_CMPCD and LT_PRDTP = LP_PRDTP and LT_LOTNO = L_LOTNO and LT_RCLNO = L_RCLNO;
      set end_table = 0;
      fetch C_STR  into L_LOTNO, L_RCLNO, L_RESQT, L_RDSQT;
   end while;
   close C_STR;
   commit;

end P1;
commit;



call spldata.setLTMST_STR('01','5111951640','01');

commit;

