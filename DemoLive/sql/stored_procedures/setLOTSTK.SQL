


drop procedure spldata.setLOTSTK;
commit;



create procedure spldata.setLOTSTK(IN LP_LOTNO char(8), IN LP_MNLCD char(5))  language sql  modifies sql data 
 P1:
begin

 declare L_WRHTP char(2); 
 declare L_LOTNO char(8); 
 declare L_RCLNO char(2); 
 declare L_MNLCD char(5); 
 declare L_PKGTP char(2); 
 declare L_DOSQT decimal(10,3) default 0.00; 
 declare L_RCTQT decimal(10,3) default 0.00; 
 declare L_ISSQT decimal(10,3) default 0.00; 
 declare L_STKQT decimal(10,3) default 0.00; 
 declare L_STKQT1 decimal(10,3) default 0.00; 
 declare L_UCLQT1 decimal(10,3) default 0.00; 

 declare L_PTFQT decimal(10,3) default 0.00; 
 declare L_PTFQT1 decimal(10,3) default 0.00; 

 declare L_DOUQT decimal(10,3) default 0.00; 
 declare L_UCLQT decimal(10,3) default 0.00; 

 declare L_TRNDT date; 
 declare L_PRCDT char(10); 
 declare L_CHKMSG char(10); 
 declare END_TABLE int default 0;
 declare trace_chk int default 1;

declare C_STK  CURSOR FOR  select ST_WRHTP, ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_MNLCD,isnull(ST_DOSQT,0),isnull(ST_STKQT,0),isnull(ST_DOUQT,0),isnull(ST_UCLQT,0)  from  spldata.FG_STMST  where ST_LOTNO=LP_LOTNO and ST_MNLCD = LP_MNLCD order by ST_WRHTP, ST_LOTNO, ST_MNLCD, ST_RCLNO, ST_PKGTP,ST_MNLCD;


declare continue handler for not found 
       set END_TABLE = 1; 



 select cmt_ccsvl into L_PRCDT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='FGXXREF' and cmt_codcd='DOCDT';
set L_TRNDT = CONVERT(varchar,day(SUBSTRING(L_PRCDT,4,2) + '/' + SUBSTRING(L_PRCDT,1,2) + '/' + SUBSTRING(L_PRCDT,7,4))+1,101);

set L_TRNDT = CONVERT(varchar,day(SUBSTRING(L_PRCDT,4,2) + '/' + SUBSTRING(L_PRCDT,1,2) + '/' + SUBSTRING(L_PRCDT,7,4))+1,101);

select sum(isnull(ptf_ptfqt,0)) into L_PTFQT1 from spldata.fg_ptfrf where ptf_ptfdt = L_TRNDT and ptf_lotno=LP_LOTNO;


open C_STK;
fetch C_STK into L_WRHTP, L_LOTNO,L_RCLNO,L_PKGTP, L_MNLCD,L_DOSQT, L_STKQT, L_DOUQT, L_UCLQT;


while END_TABLE = 0 DO 

   select sum(isnull(rct_rctqt,0)) into L_RCTQT from spldata.fg_rctrn where rct_wrhtp = L_WRHTP and rct_lotno=L_LOTNO and rct_rclno=L_RCLNO and rct_pkgtp = L_PKGTP and rct_mnlcd=L_MNLCD and rct_stsfl='2' and rct_rctdt = L_TRNDT;
    
   select sum(isnull(ist_issqt,0)) into L_ISSQT from spldata.fg_istrn where ist_wrhtp = L_WRHTP and ist_lotno=L_LOTNO and ist_rclno=L_RCLNO and ist_pkgtp = L_PKGTP and ist_mnlcd=L_MNLCD and ist_stsfl='2' and ist_autdt = L_TRNDT;

set L_RCTQT = isnull(L_RCTQT,0);
set L_ISSQT = isnull(L_ISSQT,0);


set L_STKQT1 = isnull(L_DOSQT,0);
set L_UCLQT1 = isnull(L_DOUQT,0);

if L_PTFQT1>0 and (L_RCTQT+L_DOUQT)>0 and L_UCLQT = 0  then
   set L_STKQT1 = L_STKQT1 +  isnull(L_DOUQT,0)  +  isnull(L_RCTQT,0);
   set L_PTFQT  =  isnull(L_DOUQT,0)  +  isnull(L_RCTQT,0);
else
   set  L_UCLQT1 = L_UCLQT1 +  isnull(L_DOUQT,0) + isnull(L_RCTQT,0);
end if;

set L_STKQT1 = L_STKQT1 - isnull(L_ISSQT,0);

set L_CHKMSG = 'Match';
if(L_STKQT<>L_STKQT1) then
  set L_CHKMSG = 'No Match C ';
  --update spldata.fg_stmst set st_stkqt = L_STKQT1 where st_wrhtp=L_WRHTP and st_lotno=L_LOTNO and st_rclno = L_RCLNO and st_mnlcd = L_MNLCD and st_pkgtp = L_PKGTP;
end if;
if(L_UCLQT<>L_UCLQT1) then
  set L_CHKMSG = 'No Match UC';
  --update spldata.fg_stmst set st_uclqt = L_UCLQT1 where st_wrhtp=L_WRHTP and st_lotno=L_LOTNO and st_rclno = L_RCLNO and st_mnlcd = L_MNLCD and st_pkgtp = L_PKGTP;
end if;

if trace_chk = 1 then
  delete from spldata.tempchk;
  insert into spldata.tempchk values ('date : ' + char(L_TRNDT));
  insert into spldata.tempchk values ('WRHTP / LOTNO / RCLNO / PKGTP / MNLCD : ' + L_WRHTP + ' / ' + L_LOTNO + ' / ' + L_RCLNO + ' / ' + L_PKGTP + ' /  ' + L_MNLCD) ;
  insert into spldata.tempchk values ('DOSQT: ' + char(L_DOSQT)) ;
  insert into spldata.tempchk values ('RCTQT : ' + char(L_RCTQT)) ;
  insert into spldata.tempchk values ('  ') ;
  insert into spldata.tempchk values ('DOUQT: ' + char(L_DOUQT)) ;
  insert into spldata.tempchk values ('PTFQT: ' + char(L_PTFQT)) ;
  insert into spldata.tempchk values ('  ') ;
  insert into spldata.tempchk values ('ISSQT : ' + char(L_ISSQT)) ;
  insert into spldata.tempchk values ('  ') ;
  insert into spldata.tempchk values ('STKQT1 : ' + char(L_STKQT1)) ;
  insert into spldata.tempchk values ('STKQT : ' + char(L_STKQT)) ;
  insert into spldata.tempchk values ('  ') ;
  insert into spldata.tempchk values ('UCLQT1 : ' + char(L_UCLQT1)) ;
  insert into spldata.tempchk values ('UCLQT: ' + char(L_UCLQT)) ;
  insert into spldata.tempchk values (L_CHKMSG) ;
end if;

set END_TABLE = 0;
fetch C_STK into L_WRHTP, L_LOTNO,L_RCLNO,L_PKGTP, L_MNLCD,L_DOSQT, L_STKQT, L_DOUQT, L_UCLQT;

end while;
close C_STK;
commit;

end;

commit;

delete from spldata.tempchk;
commit;
call spldata.setLOTSTK('10260070','AA48');
commit;


select * from spldata.tempchk;


select st_stkqt,st_uclqt from spldata.fg_stmst where st_lotno='10260070' and st_mnlcd='AA48';
update spldata.fg_stmst set st_stkqt=11, st_uclqt=22 where  st_lotno='10260070' and st_mnlcd='AA48';
commit;

