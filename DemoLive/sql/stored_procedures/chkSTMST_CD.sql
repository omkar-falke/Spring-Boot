drop procedure spldata.chkSTMST_CD;
commit;

create procedure spldata.chkSTMST_CD(IN LP_CMPCD char(2),IN LP_STRTP char(2), IN LP_YSTDT char(10), IN LP_MATCD char(10))  language sql  modifies sql data 

 P1:

 begin

declare L_CMPCD varchar(2);

declare L_MATCD varchar(10);

declare L_MATDS varchar(60);

declare L_YOPST decimal(10,3);

declare L_GRNQT decimal(10,3);

declare L_ISSQT decimal(10,3);

declare L_MRNQT decimal(10,3);

declare L_SANQT decimal(10,3);

declare L_STKQT decimal(10,3);

declare L_STKQT1 decimal(10,3);

declare L_LGRNO varchar(8);

declare L_LGRDT date;

declare L_LISNO varchar(8);

declare L_LISDT date;

declare L_LMRNO varchar(8);

declare L_LMRDT date;

declare L_LSNNO varchar(8);

declare L_LSNDT date;

declare L_UPDFL varchar(1);



declare END_TABLE int default 0;



declare C_STK CURSOR FOR  select st_matcd, st_matds,st_yopst,st_stkqt from spldata.mm_stmst where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd=LP_MATCD order by st_matcd;



declare continue handler for not found 

       set END_TABLE = 1;



--drop table spldata.tt_chkst_cd;

--commit;

--create table spldata.tt_chkst_cd (tt_cmpcd varchar(2),tt_strtp varchar(2), tt_matcd varchar(10), tt_matds varchar(60), tt_yopst decimal(10,3),
-- tt_grnqt decimal(10,3), tt_mrnqt decimal(10,3), tt_issqt decimal(10,3), tt_sanqt decimal(10,3), tt_stkqt1 decimal(10,3), tt_stkqt decimal(10,3));

delete from spldata.tt_chkst_cd;

commit;

set L_UPDFL = 'N';



open C_STK;

set END_TABLE = 0;

fetch C_STK  into L_MATCD, L_MATDS, L_YOPST,  L_STKQT;

while END_TABLE = 0 DO 

   select max(isnull(gr_grnno,'')),max(gr_grndt),sum(isnull(gr_acpqt,0))  into L_LGRNO,L_LGRDT,L_GRNQT from spldata.mm_grmst where gr_cmpcd = LP_CMPCD and gr_strtp=LP_STRTP and gr_matcd = L_MATCD and  gr_stsfl <>'X' and isnull(gr_acpqt,0)>0 and  gr_grndt >= LP_YSTDT;

   update spldata.mm_stmst set  ST_YTDGR = L_GRNQT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   if (length(L_LGRNO)=8 and L_UPDFL = 'Y') then

      update spldata.mm_stmst set st_PGRNO = L_LGRNO,st_PGRDT =  L_LGRDT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   end if;



   select max(isnull(is_issno,'')),max(is_issdt),sum(isnull(is_issqt,0))  into L_LISNO,L_LISDT,L_ISSQT from spldata.mm_ismst where is_cmpcd = LP_CMPCD and is_strtp=LP_STRTP and is_matcd = L_MATCD  and  is_stsfl = '2'  and isnull(is_issqt,0)>0 and   is_issdt >= LP_YSTDT;

   update spldata.mm_stmst set  ST_YTDIS = L_ISSQT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   if (length(L_LISNO)=8  and L_UPDFL = 'Y') then

      update spldata.mm_stmst set st_PISNO = L_LISNO,st_PISDT =  L_LISDT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   end if;



   select max(isnull(mr_mrnno,'')),max(mr_mrndt),sum(isnull(mr_retqt,0))  into L_LMRNO,L_LMRDT,L_MRNQT from spldata.mm_mrmst where mr_cmpcd = LP_CMPCD and mr_strtp=LP_STRTP and mr_matcd = L_MATCD  and  mr_stsfl = '5'  and isnull(mr_retqt,0)>0 and  mr_mrndt >= LP_YSTDT;

   update spldata.mm_stmst set  ST_YTDMR = L_MRNQT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   if (length(L_LMRNO)=8  and L_UPDFL = 'Y') then 

      update spldata.mm_stmst set st_PMRNO = L_LMRNO,st_PMRDT =  L_LMRDT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   end if;





   select max(isnull(sa_sanno,'')),max(sa_sandt),sum(isnull(sa_sanqt,0))  into L_LSNNO,L_LSNDT,L_SANQT from spldata.mm_samst where sa_cmpcd = LP_CMPCD and sa_strtp=LP_STRTP and sa_matcd = L_MATCD  and  sa_stsfl <> 'X'   and isnull(sa_sanqt,0)>0 and sa_sandt >= LP_YSTDT;

   update spldata.mm_stmst set  ST_YTDSN = L_SANQT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   if (length(L_LSNNO)=8  and L_UPDFL = 'Y') then

      update spldata.mm_stmst set st_PSNNO = L_LSNNO,st_PSNDT =  L_LSNDT where st_cmpcd = LP_CMPCD and st_strtp=LP_STRTP and st_matcd = L_MATCD;

   end if;

   set L_STKQT1 = isnull(L_YOPST,0)+isnull(L_GRNQT,0)-isnull(L_ISSQT,0)+isnull(L_MRNQT,0)+isnull(L_SANQT,0);



   insert into spldata.tt_chkst_cd(tt_cmpcd , tt_strtp , tt_matcd , tt_matds , tt_yopst , tt_grnqt , tt_mrnqt , tt_issqt , tt_sanqt , tt_stkqt1 , tt_stkqt) values (lp_strtp , l_matcd , l_matds , l_yopst , l_grnqt , l_mrnqt , l_issqt , l_sanqt , l_stkqt1 , l_stkqt);

    set END_TABLE = 0;

fetch C_STK  into L_MATCD, L_MATDS, L_YOPST,  L_STKQT;

end while;

close C_STK; 



commit;

end P1;
commit;

create table spldata.tt_chkst_cd (tt_cmpcd varchar(2),tt_strtp varchar(2), tt_matcd varchar(10), tt_matds varchar(60), tt_yopst decimal(10,3), tt_grnqt decimal(10,3), tt_mrnqt decimal(10,3), tt_issqt decimal(10,3), tt_sanqt decimal(10,3), tt_stkqt1 decimal(10,3), tt_stkqt decimal(10,3));



