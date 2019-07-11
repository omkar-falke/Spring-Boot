
 
--Procedure for refreshing allocation


drop procedure spldata.setREFSH_ALO;
commit;


create procedure spldata.setREFSH_ALO()  language sql  modifies sql data 
 P1:
 begin
 declare L_LOTNO char(8); 
 declare L_RCLNO char(2); 
 declare L_MNLCD char(5); 
 declare L_PKGTP char(2); 
 declare L_ISSQT decimal(10,3); 
 declare L_ALOQT decimal(10,3); 
 declare END_TABLE int default 0; 


declare C_ALO CURSOR FOR  select ist_lotno,ist_rclno,ist_mnlcd,ist_pkgtp,isnull(ist_issqt,0) ist_issqt,isnull(st_aloqt,0) st_aloqt from spldata.fg_istrn left outer join spldata.fg_stmst on ist_lotno=st_lotno and ist_rclno=st_rclno and ist_mnlcd = st_mnlcd and ist_pkgtp = st_pkgtp where isnull(ist_issqt,0)<>isnull(st_aloqt,0) and ist_stsfl='1';


declare continue handler for not found 
       set END_TABLE = 1; 

open C_ALO;
fetch C_ALO into L_LOTNO, L_RCLNO, L_MNLCD, L_PKGTP,L_ISSQT,L_ALOQT;


while END_TABLE = 0 DO 

    update spldata.FG_STMST set st_aloqt = L_ISSQT where ST_LOTNO = L_LOTNO and ST_RCLNO = L_RCLNO and ST_MNLCD = L_MNLCD and ST_PKGTP = L_PKGTP;

     fetch C_ALO into L_LOTNO, L_RCLNO, L_MNLCD, L_PKGTP,L_ISSQT,L_ALOQT;
end while;
close C_ALO; 
commit;
end P1;


commit;

call spldata.setREFSH_ALO();
commit;

-- SQL for listing of issue records where allcation quantity does not match with Stock Master record.
 select ist_lotno,ist_issdt,ist_rclno,ist_mnlcd,ist_pkgtp,isnull(st_aloqt,0) st_aloqt,sum(isnull(ist_issqt,0)) ist_issqt from spldata.fg_istrn left outer join spldata.fg_stmst on ist_lotno=st_lotno and ist_rclno=st_rclno and ist_mnlcd = st_mnlcd and ist_pkgtp = st_pkgtp where ist_stsfl='1' group by ist_lotno,ist_issdt,ist_rclno,ist_mnlcd,ist_pkgtp,isnull(st_aloqt,0) having sum(isnull(ist_issqt,0))<>isnull(st_aloqt,0);

-- SQL to set right allocation quantity in Stock Master
update spldata.fg_stmst a set st_aloqt = (select sum(isnull(b.ist_issqt,0)) from spldata.fg_istrn b where a.st_prdtp = b.ist_prdtp and a.st_lotno = b.ist_lotno and a.st_rclno = b.ist_rclno and a.st_mnlcd = b.ist_mnlcd and a.st_pkgtp = b.ist_pkgtp and b.ist_stsfl='1') where a.st_prdtp + a.st_lotno + a.st_rclno + a.st_mnlcd + a.st_pkgtp in (select c.ist_prdtp + c.ist_lotno + c.ist_rclno + c.ist_mnlcd + c.ist_pkgtp from spldata.fg_istrn c where c.ist_stsfl='1') ;
commit;
