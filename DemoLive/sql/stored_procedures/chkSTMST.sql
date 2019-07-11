drop procedure spldata.chkSTMST;
commit;

create procedure spldata.chkSTMST(IN LP_CMPCD char(2),IN LP_STRTP char(2), IN LP_YSTDT char(10))  language sql  modifies sql data 
 P1:
 begin
declare L_CMPCD varchar(2);
declare L_MATCD varchar(10);
declare L_MATDS varchar(60);
declare L_YOPST decimal(10,3);
declare L_TRNQT decimal(10,3);
declare L_STKQT decimal(10,3);
declare L_STKQT1 decimal(10,3);

declare END_TABLE int default 0;

declare C_GRN CURSOR FOR  select gr_matcd, sum(isnull(gr_acpqt,0)) gr_acpqt from spldata.mm_grmst where gr_cmpcd = LP_CMPCD and gr_strtp=LP_STRTP and  gr_stsfl<>'X' and isnull(gr_acpqt,0)>0 and gr_grndt >= LP_YSTDT group by gr_matcd;

 
declare C_ISS CURSOR FOR  select is_matcd, sum(isnull(is_issqt,0))  is_issqt from spldata.mm_ismst  where is_cmpcd = LP_CMPCD and is_strtp=LP_STRTP  and is_stsfl<>'X' and isnull(is_issqt,0) > 0 and CONVERT(varchar,is_autdt,101) >= LP_YSTDT  group by is_matcd;

declare C_MRN CURSOR FOR  select mr_matcd, sum(isnull(mr_retqt,0))  mr_retqt from spldata.mm_mrmst where mr_cmpcd = LP_CMPCD and mr_strtp=LP_STRTP  and mr_stsfl = '5' and mr_mrndt >= LP_YSTDT  group by mr_matcd;

declare C_SAN CURSOR FOR  select sa_matcd, sum(isnull(sa_sanqt,0))  sa_sanqt from spldata.mm_samst where sa_cmpcd = LP_CMPCD and sa_strtp=LP_STRTP and  sa_sandt >= LP_YSTDT  group by sa_matcd;


declare continue handler for not found 
       set END_TABLE = 1;

drop table spldata.tt_chkst;
commit;
   create table spldata.tt_chkst (tt_cmpcd varchar(2),tt_strtp varchar(2), tt_matcd varchar(10), tt_matds varchar(60), tt_yopst decimal(10,3), tt_grnqt decimal(10,3), tt_mrnqt decimal(10,3), tt_issqt decimal(10,3), tt_sanqt decimal(10,3), tt_stkqt1 decimal(10,3), tt_stkqt decimal(10,3));
commit;

   insert into spldata.tt_chkst  select st_cmpcd,st_strtp, st_matcd, st_matds, st_yopst, 0,0,0,0,0,st_stkqt from spldata.mm_stmst where st_cmpcd = LP_CMPCD and st_strtp= LP_STRTP;
commit;

   set end_table = 0;
   open C_GRN;
   fetch C_GRN  into L_MATCD, L_TRNQT;
   while END_TABLE = 0 DO 
      update spldata.tt_chkst set tt_grnqt = L_TRNQT where TT_cmpcd = LP_CMPCD and TT_STRTP=LP_STRTP and TT_MATCD = L_MATCD;
      set end_table = 0;
      fetch C_GRN  into L_MATCD, L_TRNQT;
   end while;
   close C_GRN;
   commit;

   set end_table = 0;
   open C_ISS;
   fetch C_ISS  into L_MATCD, L_TRNQT;

   while END_TABLE = 0 DO 
      update spldata.tt_chkst set tt_issqt = L_TRNQT where TT_cmpcd = LP_CMPCD and TT_STRTP=LP_STRTP and TT_MATCD = L_MATCD;
      set end_table = 0;
      fetch C_ISS  into L_MATCD, L_TRNQT;
   end while;
   close C_ISS;
   commit;

   set end_table = 0;
   open C_MRN;
   fetch C_MRN  into L_MATCD, L_TRNQT;

   while END_TABLE = 0 DO 
      update spldata.tt_chkst set tt_mrnqt = L_TRNQT where tt_cmpcd = LP_CMPCD and TT_STRTP=LP_STRTP and TT_MATCD = L_MATCD;
      set end_table = 0;
      fetch C_MRN  into L_MATCD, L_TRNQT;
   end while;
   close C_MRN;
   commit;


   set end_table = 0;
   open C_SAN;
   fetch C_SAN  into L_MATCD, L_TRNQT;

   while END_TABLE = 0 DO 
      update spldata.tt_chkst set tt_sanqt = L_TRNQT where tt_cmpcd = LP_CMPCD and TT_STRTP=LP_STRTP and TT_MATCD = L_MATCD;
      set end_table = 0;
      fetch C_SAN  into L_MATCD, L_TRNQT;
   end while;
   close C_SAN;
   commit;

   update spldata.tt_chkst set tt_stkqt1 = isnull(tt_YOPST,0)+isnull(tt_GRNQT,0)-isnull(tt_ISSQT,0)+isnull(tt_MRNQT,0)+isnull(tt_SANQT,0);
   commit;
   delete from spldata.tt_chkst where tt_cmpcd = LP_CMPCD and isnull(tt_stkqt,0) = isnull(tt_stkqt1,0);
   commit;

end P1;
commit;


create table spldata.tt_chkst (tt_cmpcd varchar(2),tt_strtp varchar(2), tt_matcd varchar(10), tt_matds varchar(60), tt_yopst decimal(10,3), tt_grnqt decimal(10,3), tt_mrnqt decimal(10,3), tt_issqt decimal(10,3), tt_sanqt decimal(10,3), tt_stkqt1 decimal(10,3), tt_stkqt decimal(10,3));
commit;


drop table spldata.tt_chkst;
commit;

call spldata.chkSTMST('01','01','07/01/2009');
commit;
