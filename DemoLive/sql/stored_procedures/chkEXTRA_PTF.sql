drop procedure spldata.chkEXTRA_PTF;
commit;


create procedure spldata.chkEXTRA_PTF()  language sql  modifies sql data 
 P1:
 begin

declare L_PRDTP varchar(2);
declare L_LOTNO varchar(8);
declare L_RCLNO varchar(2);
declare L_RCTQT decimal(10,3);
declare L_PTFQT decimal(10,3);

declare END_TABLE int default 0;

declare C_RCT CURSOR FOR  select rct_prdtp,rct_lotno,rct_rclno,sum(rct_rctqt) from spldata.fg_rctrn where rct_rcttp in ('10','15') and rct_stsfl='2' and rct_rctno like '9%'group by rct_prdtp,rct_lotno,rct_rclno;

declare continue handler for not found 
       set END_TABLE = 1;

drop table spldata.tt_ptfch;
commit;
create table spldata.tt_ptfch (tt_prdtp varchar(2), tt_lotno varchar(8), tt_rclno varchar(2), tt_rctqt decimal(10,3), tt_ptfqt decimal (10,3));
commit;
delete from spldata.temp_chk;
commit;

open C_RCT;
fetch C_RCT  into l_prdtp, l_lotno,l_rclno,l_rctqt;

while END_TABLE = 0 DO 
   --insert into spldata.temp_chk values(l_prdtp||' - '|| l_lotno||' - '||l_rclno||' - '||char(l_rctqt));
   select sum(ptf_ptfqt) into l_ptfqt from spldata.fg_ptfrf where ptf_prdtp = l_prdtp and ptf_lotno=l_lotno and ptf_rclno = l_rclno and ptf_ptfno like '9%';
   if l_rctqt <> l_ptfqt then
      insert into spldata.tt_ptfch values (l_prdtp,l_lotno,l_rclno,l_rctqt,l_ptfqt);
   end if;
   set end_table = 0;
   fetch C_RCT  into l_prdtp, l_lotno,l_rclno,l_rctqt;
end while;
close C_RCT; 

commit;
end P1;
commit;

create table spldata.tt_ptfch (tt_prdtp varchar(2), tt_lotno varchar(8), tt_rclno varchar(2), tt_rctqt decimal(10,3), tt_ptfqt decimal (10,3));
commit;
call spldata.chkEXTRA_PTF();


select * from spldata.tt_ptfch;

select rct_prdtp,rct_lotno,rct_rclno,sum(rct_rctqt) from spldata.fg_rctrn where rct_rcttp in ('10','15') and rct_stsfl='2' and rct_rctno like '8%'group by rct_prdtp,rct_lotno,rct_rclno;
