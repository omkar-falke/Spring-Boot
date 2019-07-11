-- procedure to set Month Opening & Closing Stock Quantity and transaction cumulatives for  specified Date Range w.r.t. Year Opening Stock -- This creates a table MM_STTMP similart to MM_STPRC, which is used for generating Monthly Stock Statement Report 
-- Procedure is called manually after monthly PSL processing

drop procedure spldata.setMMSTPRC;
commit;

-- 
create procedure spldata.setMMSTPRC(IN LP_CMPCD char(2), IN LP_YSTDT char(10), IN LP_PMEDT char(10), IN LP_RMEDT char(10))  language sql  modifies sql data 
 P1:

begin
  declare L_STRTP char(2);
  declare L_MATCD char(10);
  declare L_RECQT decimal(12,3);
  declare L_ISSQT decimal(12,3);
  declare L_RETQT decimal(12,3);
  declare L_SANQT decimal(12,3);
  declare END_TABLE int default 0;
  declare cnt int default 0;

  declare C_GRN_MOP cursor for  select  GR_STRTP,rtrim(ltrim(GR_MATCD)), sum(GR_ACPQT) from spldata.MM_GRMST where GR_CMPCD = LP_CMPCD and  GR_ACPDT between LP_YSTDT and LP_PMEDT and GR_STSFL='2'  and isnull(GR_ACPQT,0) >0  group by GR_STRTP,GR_MATCD ORDER BY GR_STRTP,GR_MATCD;

  declare C_ISS_MOP cursor for  select  IS_STRTP,rtrim(ltrim(IS_MATCD)), sum(IS_ISSQT) from spldata.MM_ISMST where IS_CMPCD = LP_CMPCD  and CONVERT(varchar,IS_AUTDT,101) between LP_YSTDT and LP_PMEDT  and IS_STSFL='2' and isnull(IS_ISSQT,0) >0  group by IS_STRTP,IS_MATCD ORDER BY IS_STRTP,IS_MATCD;


  declare C_MRN_MOP cursor for  select  MR_STRTP,rtrim(ltrim(MR_MATCD)), sum(MR_RETQT) from spldata.MM_MRMST where MR_CMPCD = LP_CMPCD and  MR_AUTDT between LP_YSTDT and LP_PMEDT  and MR_STSFL='5' and isnull(MR_RETQT,0) >0  group by MR_STRTP,MR_MATCD ORDER BY MR_STRTP,MR_MATCD;


  declare C_SAN_MOP cursor for  select  SA_STRTP,rtrim(ltrim(SA_MATCD)), sum(SA_SANQT) from spldata.MM_SAMST where SA_CMPCD = LP_CMPCD and SA_SANDT between LP_YSTDT and LP_PMEDT and SA_STSFL<>'X' and isnull(SA_SANQT,0) >0 group by SA_STRTP,SA_MATCD ORDER BY SA_STRTP,SA_MATCD;


  declare C_GRN_MCL cursor for  select  GR_STRTP,rtrim(ltrim(GR_MATCD)), sum(GR_ACPQT) from spldata.MM_GRMST where GR_CMPCD = LP_CMPCD and  GR_ACPDT between LP_YSTDT and LP_RMEDT and GR_STSFL='2' and isnull(GR_ACPQT,0) >0  group by GR_STRTP,GR_MATCD ORDER BY GR_STRTP,GR_MATCD;

  declare C_ISS_MCL cursor for  select  IS_STRTP,rtrim(ltrim(IS_MATCD)), sum(IS_ISSQT) from spldata.MM_ISMST where IS_CMPCD = LP_CMPCD  and CONVERT(varchar,IS_AUTDT,101) between LP_YSTDT and LP_RMEDT  and IS_STSFL='2' and isnull(IS_ISSQT,0) >0  group by IS_STRTP,IS_MATCD ORDER BY IS_STRTP,IS_MATCD;


  declare C_MRN_MCL cursor for  select  MR_STRTP,rtrim(ltrim(MR_MATCD)), sum(MR_RETQT) from spldata.MM_MRMST where MR_CMPCD = LP_CMPCD and  MR_AUTDT between LP_YSTDT and LP_RMEDT  and MR_STSFL='5' and isnull(MR_RETQT,0) >0  group by MR_STRTP,MR_MATCD ORDER BY MR_STRTP,MR_MATCD;


  declare C_SAN_MCL cursor for  select  SA_STRTP,rtrim(ltrim(SA_MATCD)), sum(SA_SANQT) from spldata.MM_SAMST where SA_CMPCD = LP_CMPCD and SA_SANDT between LP_YSTDT and LP_RMEDT and SA_STSFL<>'X' and isnull(SA_SANQT,0) >0 group by SA_STRTP,SA_MATCD ORDER BY SA_STRTP,SA_MATCD;

  
  declare continue handler for not found 
       set END_TABLE = 1; 

--create table spldata.mm_sttmp like spldata.mm_stprc;
delete from spldata.mm_sttmp;
commit;
insert into spldata.mm_sttmp (stp_cmpcd,stp_strtp,stp_matcd,stp_matds,stp_yosqt,stp_uomcd,stp_mmsbs)  (select stp_cmpcd,stp_strtp,stp_matcd,stp_matds,stp_yosqt,stp_uomcd,stp_mmsbs from spldata.mm_stprc where stp_cmpcd = LP_CMPCD);


 set END_TABLE =0;
set cnt=1;
 open C_GRN_MOP;
  fetch C_GRN_MOP into  L_STRTP,L_MATCD, L_RECQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MORCQ = L_RECQT where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_GRN_MOP into  L_STRTP,L_MATCD, L_RECQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_GRN_MOP; 
  commit;


 set END_TABLE =0;
set cnt=0;
 open C_ISS_MOP;
  fetch C_ISS_MOP into  L_STRTP,L_MATCD, L_ISSQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MOISQ = L_ISSQT where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_ISS_MOP into  L_STRTP,L_MATCD, L_ISSQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_ISS_MOP; 
  commit;


 set END_TABLE =0;
set cnt=0;
 open C_MRN_MOP;
  fetch C_MRN_MOP into  L_STRTP,L_MATCD, L_RETQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MOMRQ = L_RETQT where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_MRN_MOP into  L_STRTP,L_MATCD, L_RETQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_MRN_MOP; 
  commit;

 set END_TABLE =0;
set cnt=0;
 open C_SAN_MOP;
  fetch C_SAN_MOP into  L_STRTP,L_MATCD, L_SANQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MOSAQ = L_SANQT where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_SAN_MOP into  L_STRTP,L_MATCD, L_SANQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_SAN_MOP; 
  commit;


 set END_TABLE =0;
set cnt=0;
 open C_GRN_MCL;
  fetch C_GRN_MCL into  L_STRTP,L_MATCD, L_RECQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MCRCQ = L_RECQT where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_GRN_MCL into  L_STRTP,L_MATCD, L_RECQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_GRN_MCL; 
  commit;

 set END_TABLE =0;
set cnt=0;
 open C_ISS_MCL;
  fetch C_ISS_MCL into  L_STRTP,L_MATCD, L_ISSQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MCISQ = L_ISSQT where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_ISS_MCL into  L_STRTP,L_MATCD, L_ISSQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_ISS_MCL; 
  commit;

 set END_TABLE =0;
set cnt=0;
 open C_MRN_MCL;
  fetch C_MRN_MCL into  L_STRTP,L_MATCD, L_RETQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MCMRQ = L_RETQT where rtrim(ltrim(stp_cmpcd)) =rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_MRN_MCL into  L_STRTP,L_MATCD, L_RETQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_MRN_MCL; 
  commit;

 set END_TABLE =0;
set cnt=0;
 open C_SAN_MCL;
  fetch C_SAN_MCL into  L_STRTP,L_MATCD, L_SANQT;
  while END_TABLE = 0 DO 
    update spldata.mm_sttmp set STP_MCSAQ = L_SANQT where rtrim(ltrim(stp_cmpcd)) = rtrim(ltrim(LP_CMPCD)) and rtrim(ltrim(stp_strtp)) = rtrim(ltrim(L_STRTP)) and rtrim(ltrim(stp_matcd)) = rtrim(ltrim(L_MATCD));
    fetch C_SAN_MCL into  L_STRTP,L_MATCD, L_SANQT;
set cnt = cnt+1;
  end while;
insert into spldata.mm_cnt (mm_cnt) values(cnt);
  close C_SAN_MCL; 
  commit;

    update spldata.mm_sttmp set STP_MOSQT = isnull(STP_YOSQT,0)+isnull(STP_MORCQ,0)+isnull(STP_MOMRQ,0)-isnull(STP_MOISQ,0)+isnull(STP_MOSAQ,0) where stp_cmpcd=LP_CMPCD;
    update spldata.mm_sttmp set STP_MCSQT = isnull(STP_YOSQT,0)+isnull(STP_MCRCQ,0)+isnull(STP_MCMRQ,0)-isnull(STP_MCISQ,0)+isnull(STP_MCSAQ,0) where stp_cmpcd=LP_CMPCD;
commit;


end P1;
commit;


commit;

call spldata.setMMSTPRC('01','07/01/2008','08/31/2008','09/30/2008');
commit;

;


--set END_TABLE =0;
-- open C_ISS_MCL;
--  fetch C_ISS_MCL into  L_STRTP,L_MATCD, L_RECQT;
--  while END_TABLE = 0 DO 
-- INSERT INTO SPLDATA.MM_ISSCL (MM_CMPCD,MM_STRTP,MM_MATCD,MM_MCISQ) VALUES (LP_CMPCD,L_STRTP,L_MATCD,L_RECQT);
--    fetch C_ISS_MCL into  L_STRTP,L_MATCD, L_RECQT;
--  end while;
--  close C_ISS_MCL; 
--  commit;


select a.stp_mosqt,b.stp_mosqt from spldata.mm_sttmp a, spldata.mm_sttmp b where a.stp_strtp=b.stp_strtp and a.stp_matcd = b.stp_matcd and isnull(a.stp_mosqt,0)<> isnull(b.stp_mosqt,0);
select a.stp_strtp,a.stp_matcd,a.stp_mcsqt,b.stp_mcsqt from spldata.mm_sttmp a, spldata.mm_sttmp b where a.stp_strtp=b.stp_strtp and a.stp_matcd = b.stp_matcd and isnull(a.stp_mcsqt,0)<> isnull(b.stp_mcsqt,0);



select a.stp_mosqt,b.stp_mosqt from spldata.mm_stprc a, spldata.mm_sttmp b where a.stp_strtp=b.stp_strtp and a.stp_matcd = b.stp_matcd and isnull(a.stp_mosqt,0)<> isnull(b.stp_mosqt,0);
select a.stp_mosqt,b.stp_mosqt from spldata.mm_stprc a, spldata.mm_sttmp b where a.stp_strtp=b.stp_strtp and a.stp_matcd = b.stp_matcd and isnull(a.stp_mosqt,0)<> isnull(b.stp_mosqt,0);

;
;
;



Create table MM_STPRC (                           -- -
STP_MMSBS     VARCHAR(6)   not null,              -- -
STP_STRTP     VARCHAR(2)   not null,              -- -
STP_MATCD     VARCHAR(10)   not null,             -- -
STP_MATDS     VARCHAR(60),                        -- -
STP_UOMCD     VARCHAR(3),                         -- -
STP_YOSQT     DECIMAL(12,3),                      -- -
STP_YOSVL     DECIMAL(12,2),                      -- -
STP_YOMQT     DECIMAL(12,3),                      -- -
STP_YOMVL     DECIMAL(12,2),                      -- -
STP_MOSQT     DECIMAL(12,3),                      -- -
STP_MOSVL     DECIMAL(12,2),                      -- -
STP_MOMQT     DECIMAL(12,3),                      -- -
STP_MOMVL     DECIMAL(12,2),                      -- -
STP_YCSQT     DECIMAL(12,3),                      -- -
STP_YCSVL     DECIMAL(12,2),                      -- -
STP_YCMQT     DECIMAL(12,3),                      -- -
STP_YCMVL     DECIMAL(12,2),                      -- -
STP_MCSQT     DECIMAL(12,3),                      -- -
STP_MCSVL     DECIMAL(12,2),                      -- -
STP_MCMQT     DECIMAL(12,3),                      -- -
STP_MCMVL     DECIMAL(12,2),                      -- -
STP_CRCQT     DECIMAL(12,3),                      -- -
STP_CRCVL     DECIMAL(12,2),                      -- -
STP_CISQT     DECIMAL(12,3),                      -- -
STP_CISVL     DECIMAL(12,2),                      -- -
STP_CMRQT     DECIMAL(12,3),                      -- -
STP_CMRVL     DECIMAL(12,2),                      -- -
STP_CSAQT     DECIMAL(12,3),                      -- -
STP_CSAVL     DECIMAL(12,2),                      -- -
STP_CSTQT     DECIMAL(12,3),                      -- -
STP_CSTVL     DECIMAL(12,2),                      -- -
STP_WAVRT     DECIMAL(10,2),                      -- -
STP_CMDQT     DECIMAL(12,3),                      -- -
STP_CMIQT     DECIMAL(12,3),                      -- -
STP_PRCDT     DATE,                               -- -
STP_YCLRT     DECIMAL(12,2),                      -- -
STP_ADJVL     DECIMAL(12,2),                      -- -
STP_CMDVL     DECIMAL(12,2),                      -- -
STP_CMIVL     DECIMAL(12,2),                      -- -
STP_MORCQ     DECIMAL(12,3),                      -- -
STP_MORCV     DECIMAL(12,2),                      -- -
STP_MOISQ     DECIMAL(12,3),                      -- -
STP_MOISV     DECIMAL(12,2),                      -- -
STP_MOMRQ     DECIMAL(12,3),                      -- -
STP_MOMRV     DECIMAL(12,2),                      -- -
STP_MOSAQ     DECIMAL(12,3),                      -- -
STP_MOSAV     DECIMAL(12,2),                      -- -
STP_MCRCQ     DECIMAL(12,3),                      -- -
STP_MCRCV     DECIMAL(12,2),                      -- -
STP_MCISQ     DECIMAL(12,3),                      -- -
STP_MCISV     DECIMAL(12,2),                      -- -
STP_MCMRQ     DECIMAL(12,3),                      -- -
STP_MCMRV     DECIMAL(12,2),                      -- -
STP_MCSAQ     DECIMAL(12,3),                      -- -
STP_MCSAV     DECIMAL(12,2),                      -- -
STP_CATFL     VARCHAR(1),                         -- -
STP_OWNBY     VARCHAR(3),                         -- -
STP_MOSTQ     DECIMAL(12,3),                      -- -
STP_MOSTV     DECIMAL(12,2),                      -- -
STP_MCSTQ     DECIMAL(12,3),                      -- -
STP_MCSTV     DECIMAL(12,2),                      -- -
STP_CMPCD     VARCHAR(2),                         -- -
 Constraint MM_STPRC Primary Key (STP_CMPCD,STP_STRTP,STP_MATCD))

;
;
