-- Procedure for setting year opening qty. in FG_OPSTK
-- on 01/04/2007 Day end closing for 31/03/2007 is carried out.
-- fg_stmst is copied to splhist as fg_stmst_010407
-- records are deleted from spldata.fg_opstk
-- After this, the procedure setFGOPSTK_YR is executed to set year opening stock.

drop procedure spldata.setFGOPSTK_YR;
commit;
drop procedure spldata.setFGOPSTK11_YR;
commit;


create procedure spldata.setFGOPSTK11_YR(IN LP_CMPCD char(2))  language sql  modifies sql data 
 P1:
 begin

declare L_WRHTP varchar(2);
declare L_PRDTP varchar(2);
declare L_PRDCD varchar(10);
declare L_PKGTP varchar(2);
declare L_YOPQT decimal(10,3);
declare L_CTR int default 0;


declare END_TABLE int default 0;
declare C_OP_YOS CURSOR FOR  select st_wrhtp, st_prdtp, st_prdcd,st_pkgtp, sum(st_dosqt) from splhist.fg_stmst_010409_11 where st_cmpcd=lp_cmpcd and st_dosqt>0 and st_stsfl in ('1','X') group by st_wrhtp, st_prdtp, st_prdcd,st_pkgtp;

declare C_OP_YOX CURSOR FOR  select st_wrhtp, st_prdtp, st_prdcd,st_pkgtp, sum(st_dosqt) from splhist.fg_stmst_010409_11 where st_cmpcd=lp_cmpcd and st_dosqt>0 and st_stsfl='2' group by st_wrhtp, st_prdtp, st_prdcd,st_pkgtp;

declare C_OP_STK CURSOR FOR  select st_wrhtp, st_prdtp, st_prdcd,st_pkgtp, sum(st_dosqt) from spldata.fg_stmst where st_cmpcd=LP_CMPCD and  st_dosqt>0 and  st_stsfl in ('1','X') group by st_wrhtp, st_prdtp, st_prdcd,st_pkgtp;

declare C_OP_SLR CURSOR FOR  select st_wrhtp, st_prdtp, st_prdcd,st_pkgtp, sum(st_dosqt) from spldata.fg_stmst where st_cmpcd=LP_CMPCD and st_dosqt>0 and st_stsfl='2' group by st_wrhtp, st_prdtp, st_prdcd,st_pkgtp;

declare C_OP_UCL CURSOR FOR  select st_wrhtp, st_prdtp, st_prdcd,st_pkgtp, sum(st_douqt) from spldata.fg_stmst where st_cmpcd=LP_CMPCD and st_douqt>0  group by st_wrhtp, st_prdtp, st_prdcd,st_pkgtp;

declare C_OP_TDS CURSOR FOR  select st_wrhtp, st_prdtp, st_prdcd,st_pkgtp, sum(st_stkqt) from spldata.fg_stmst where st_cmpcd=LP_CMPCD and st_douqt>0  and length(trim(st_remds))>0 group by st_wrhtp, st_prdtp, st_prdcd,st_pkgtp;


declare continue handler for not found 
       set END_TABLE = 1; 

--update spldata.fg_opstk set op_yosqt=0, op_yoxqt=0 where op_cmpcd=LP_CMPCD;

open C_OP_YOS;
fetch C_OP_YOS  into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;

while END_TABLE = 0 DO 
   select count(*) into L_ctr from spldata.fg_opstk where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd  and op_pkgtp=l_pkgtp;
   if l_ctr>0 then
     update spldata.fg_opstk set op_yosqt=l_yopqt where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and op_pkgtp=l_pkgtp;
   else 
      insert into spldata.fg_opstk (op_cmpcd,op_wrhtp, op_prdtp, op_prdcd, op_pkgtp,op_yosqt) values (lp_cmpcd,l_wrhtp, l_prdtp, l_prdcd,  l_pkgtp,l_yopqt);
  end if;
  fetch C_OP_YOS into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;
end while;
close C_OP_YOS; 

set END_TABLE = 0; 

open C_OP_YOX;
fetch C_OP_YOX into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;

while END_TABLE = 0 DO 
   select count(*) into L_ctr from spldata.fg_opstk where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and op_pkgtp=l_pkgtp;
   if l_ctr>0 then
     update spldata.fg_opstk set op_yoxqt=l_yopqt where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd  and op_pkgtp=l_pkgtp;
   else 
      insert into spldata.fg_opstk (op_cmpcd,op_wrhtp, op_prdtp, op_prdcd, op_pkgtp,op_yoxqt) values (lp_cmpcd,l_wrhtp, l_prdtp, l_prdcd,  l_pkgtp,l_yopqt);
  end if;
  fetch C_OP_YOX into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;
end while;
close C_OP_YOX; 

set END_TABLE = 0; 

open C_OP_STK;
fetch C_OP_STK into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;

while END_TABLE = 0 DO 
   select count(*) into L_ctr from spldata.fg_opstk where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and  op_pkgtp=l_pkgtp;
   if l_ctr>0 then
     update spldata.fg_opstk set op_stkqt=l_yopqt, op_dosqt=l_yopqt where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and  op_pkgtp=l_pkgtp;
   else 
      insert into spldata.fg_opstk (op_cmpcd,op_wrhtp, op_prdtp, op_prdcd,  op_pkgtp,op_dosqt,op_stkqt) values (lp_cmpcd,l_wrhtp, l_prdtp, l_prdcd,  l_pkgtp,l_yopqt,l_yopqt);
  end if;
  fetch C_OP_STK into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;
end while;
close C_OP_STK; 

set END_TABLE = 0; 

open C_OP_SLR;
fetch C_OP_SLR into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;

while END_TABLE = 0 DO 
   select count(*) into L_ctr from spldata.fg_opstk where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd  and op_pkgtp=l_pkgtp;
   if l_ctr>0 then
     update spldata.fg_opstk set op_slrqt=l_yopqt where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd  and op_pkgtp=l_pkgtp;
   else 
      insert into spldata.fg_opstk (op_cmpcd,op_wrhtp, op_prdtp, op_prdcd,  op_pkgtp,op_slrqt) values (lp_cmpcd,l_wrhtp, l_prdtp, l_prdcd,  l_pkgtp,l_yopqt);
  end if;
  fetch C_OP_SLR into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;
end while;
close C_OP_SLR; 

set END_TABLE = 0; 

open C_OP_UCL;
fetch C_OP_UCL into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;

while END_TABLE = 0 DO 
   select count(*) into L_ctr from spldata.fg_opstk where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and  op_pkgtp=l_pkgtp;
   if l_ctr>0 then
     update spldata.fg_opstk set op_douqt=l_yopqt,op_uclqt=l_yopqt where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and  op_pkgtp=l_pkgtp;
   else 
      insert into spldata.fg_opstk (op_cmpcd,op_wrhtp, op_prdtp, op_prdcd, op_pkgtp,op_douqt,op_uclqt) values (lp_cmpcd,l_wrhtp, l_prdtp, l_prdcd, l_pkgtp,l_yopqt,l_yopqt);
  end if;
  fetch C_OP_UCL into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;
end while;
close C_OP_UCL; 

set END_TABLE = 0; 

open C_OP_TDS;
fetch C_OP_TDS into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;

while END_TABLE = 0 DO 
   select count(*) into L_ctr from spldata.fg_opstk where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and  op_pkgtp=l_pkgtp;
   if l_ctr>0 then
     update spldata.fg_opstk set op_TDSqt=l_yopqt where op_cmpcd=LP_CMPCD and op_wrhtp=l_wrhtp and op_prdtp=l_prdtp and op_prdcd=l_prdcd and  op_pkgtp=l_pkgtp;
   else 
      insert into spldata.fg_opstk (op_cmpcd,op_wrhtp, op_prdtp, op_prdcd,  op_pkgtp,op_TDSqt) values (lp_cmpcd,l_wrhtp, l_prdtp, l_prdcd,  l_pkgtp,l_yopqt);
  end if;
  fetch C_OP_TDS into l_wrhtp, l_prdtp, l_prdcd,l_pkgtp,l_yopqt;
end while;
close C_OP_TDS; 


commit;
end P1;

commit;



delete from spldata.fg_opstk where ifnull(op_cmpcd,'xx')<>'01';
commit;

call spldata.setFGOPSTK11_YR('11');

commit;

select sum(op_yosqt+op_yoxqt) from spldata.fg_opstk where op_cmpcd='11';
select sum(st_douqt) from spldata.fg_stmst where st_cmpcd='11';

