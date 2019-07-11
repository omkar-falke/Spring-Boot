/* Enter one or more SQL statements separated by semicolons */

/* used on 01/01/2005 for checking and back updating to monthly data */
/* used on 01/11/2004 for checking and back updating to monthly data */
/* used on 01/01/2008 for checking and back updating to monthly data */

/* used for updating values CISVL,CRCVL from yearly processing as on date to acual */

/* checking for difference of cum . quantity */

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_cisqt,0) <>ifnull(yr_cisqt,0) and stp_cmpcd = '01' and stp_strtp in('01','06','07','08') and yr_cmpcd = '01' and yr_strtp in('01','06','07','08') and stp_cmpcd=yr_cmpcd and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_crcqt,0) <>ifnull(yr_crcqt,0) and stp_cmpcd = '01' and stp_strtp in('01','06','07','08') and yr_cmpcd = '01' and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_csaqt,0) <>ifnull(yr_csaqt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_cstqt,0) <>ifnull(yr_cstqt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_cmrqt,0) <>ifnull(yr_cmrqt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_ycsqt,0) <>ifnull(yr_ycsqt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_yclrt,0) <>ifnull(yr_yclrt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

/* checking for difference of cum . value */

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_crcvl,0) <>ifnull(yr_crcvl,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_cisvl,0) <>ifnull(yr_cisvl,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_cmrvl,0) <>ifnull(yr_cmrvl,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_csavl,0) <>ifnull(yr_csavl,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select * from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_csavl,0) <>ifnull(yr_csavl,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_ycsvl,0) <>ifnull(yr_ycsvl,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select count(*) from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_yclrt,0) <>ifnull(yr_yclrt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

/* checking for difference of cum . quantity */

select YR_STRTP,yr_matcd,yr_crcqt,stp_crcqt from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_crcqt,0) <>ifnull(yr_crcqt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select YR_STRTP,yr_matcd,yr_cisqt,stp_cisqt from spldata.mm_yrprc,spldata.mm_stprc where ifnull(stp_cisqt,0) <>ifnull(yr_cisqt,0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select stp_matcd,stp_crcvl,yr_crcvl from spldata.mm_yrprc,spldata.mm_stprc where round(ifnull(stp_crcvl,0),0) <>round(ifnull(yr_crcvl,0),0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

select stp_matcd,stp_csavl,yr_csavl from spldata.mm_yrprc,spldata.mm_stprc where round(ifnull(stp_csavl,0),0) <>round(ifnull(yr_csavl,0),0) and stp_strtp in('01','06','07','08') and yr_strtp in('01','06','07','08') and stp_strtp = yr_strtp and stp_matcd = yr_matcd ;

update spldata.mm_stprc a set stp_cisqt = (select yr_cisqt from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 

/* updating of cum. values */
update spldata.mm_stprc a set stp_cisvl = (select yr_cisvl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 
commit;

update spldata.mm_stprc a set stp_csavl = (select yr_csavl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 
commit;

update spldata.mm_stprc a set stp_cstvl = (select yr_cstvl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 
commit;

update spldata.mm_stprc a set stp_crcvl = (select yr_crcvl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 
commit;
update spldata.mm_stprc a set stp_cmrvl = (select yr_cmrvl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 
commit;
update spldata.mm_stprc a set stp_mcsvl = (select yr_ycsvl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 
commit;
update spldata.mm_stprc a set stp_yclrt = (select yr_yclrt from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 

commit;

/*UPDATE SPLDATA.MM_ISMST SET (IS_ISSVL,IS_ISSRT,IS_MDVQT,IS_MDVVL)= (SELECT ROUND(PS_DOCVL,2),ROUND(PS_DOCRT,2),ROUND(PS_MODQT,3),ROUND(PS_MODVL,2) FROM SPLDATA.MM_PSLYR WHERE PS_DOCTP='6' AND PS_STRTP=IS_STRTP AND PS_DOCNO=IS_ISSNO AND PS_MATCD=IS_MATCD AND PS_DOCRF=IS_TAGNO) WHERE  IS_STRTP||IS_MATCD||IS_ISSNO||(IS_TAGNO||'|'||IS_BATNO||'|'||IS_GRNNO) IN (SELECT B.PS_STRTP||B.PS_MATCD||B.PS_DOCNO||B.PS_DOCRF FROM SPLDATA.MM_PSLYR B WHERE B.PS_DOCTP='6') AND DATE(IS_AUTDT) >= '07/01/2008'  AND IS_STSFL='2' ; */

UPDATE SPLDATA.MM_ISMST a SET (IS_ISSVL,IS_ISSRT,IS_MDVQT,IS_MDVVL)= (SELECT ROUND(PS_DOCVL,2),ROUND(PS_DOCRT,2),ROUND(PS_MODQT,3),ROUND(PS_MODVL,2) FROM SPLDATA.MM_PSLYR WHERE PS_CMPCD = '01' and PS_DOCTP='6' AND PS_CMPCD = a.IS_CMPCD and PS_STRTP=a.IS_STRTP AND PS_DOCNO=a.IS_ISSNO AND PS_MATCD=a.IS_MATCD AND PS_DOCRF=a.IS_TAGNO||'|'||a.IS_BATNO||'|'||a.IS_GRNNO) WHERE  IS_CMPCD = '01' and a.IS_CMPCD||a.IS_STRTP||a.IS_MATCD||a.IS_ISSNO||(a.IS_TAGNO||'|'||a.IS_BATNO||'|'||a.IS_GRNNO) IN (SELECT B.PS_CMPCD||B.PS_STRTP||B.PS_MATCD||B.PS_DOCNO||B.PS_DOCRF FROM SPLDATA.MM_PSLYR B WHERE B.PS_DOCTP='6') AND DATE(a.IS_AUTDT) >= '07/01/2009'  AND a.IS_STSFL='2' ;


UPDATE SPLDATA.MM_MRMST a SET (MR_MRNVL,MR_MRNRT)= (SELECT ROUND(PS_DOCVL,2),ROUND(PS_DOCRT,2) FROM SPLDATA.MM_PSLYR WHERE PS_CMPCD = '01' and PS_DOCTP='2' AND PS_CMPCD=a.MR_CMPCD and PS_STRTP=a.MR_STRTP AND PS_DOCNO=a.MR_MRNNO AND PS_MATCD=a.MR_MATCD AND PS_DOCRF=a.MR_TAGNO) WHERE  a.MR_CMPCD = '01' and a.MR_CMPCD||a.MR_STRTP||a.MR_MATCD||a.MR_MRNNO||a.MR_TAGNO IN (SELECT B.PS_CMPCD||B.PS_STRTP||B.PS_MATCD||B.PS_DOCNO||B.PS_DOCRF FROM SPLDATA.MM_PSLYR B WHERE B.PS_DOCTP='2') AND a.MR_AUTDT >= '07/01/2009'  AND a.MR_STSFL='2' ;

commit;

SELECT DISTINCT MR_TAGNO FROM SPLDATA.MM_MRMST;



UPDATE SPLDATA.MM_STPRC a SET STP_YCMQT=(SELECT IFNULL(YR_YCMQT ,0) FROM SPLDATA.MM_YRPRC WHERE YR_CMPCD = '01' and YR_CMPCD=a.STP_CMPCD  AND YR_STRTP=a.STP_STRTP AND YR_MATCD=a.STP_MATCD) WHERE a.stp_cmpcd = '01' and a.STP_STRTP NOT IN ('04','09','41','42','43') AND a.STP_CMPCD||a.STP_STRTP||a.STP_MATCD IN (SELECT B.YR_CMPCD||B.YR_STRTP||B.YR_MATCD FROM SPLDATA.MM_YRPRC B);

UPDATE SPLDATA.MM_STPRC a SET STP_YCMVL=(SELECT IFNULL(YR_YCMVL ,0) FROM SPLDATA.MM_YRPRC WHERE YR_CMPCD = '01' and YR_CMPCD = a.STP_CMPCD and YR_STRTP=a.STP_STRTP AND YR_MATCD=a.STP_MATCD) WHERE a.stp_cmpcd = '01' and a.STP_STRTP NOT IN ('04','09','41','42','43') AND a.STP_CMPCD||a.STP_STRTP||a.STP_MATCD IN (SELECT B.YR_CMPCD||B.YR_STRTP||B.YR_MATCD FROM SPLDATA.MM_YRPRC B);

update spldata.mm_stprc a set (stp_cmiqt,stp_cmivl) = (select yr_cmiqt,yr_cmivl from spldata.mm_yrprc where yr_cmpcd='01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;

update spldata.mm_stprc a set (stp_cmdqt,stp_cmdvl) = (select yr_cmdqt,yr_cmdvl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;

commit;

update spldata.mm_stprc a set stp_ycsvl = (select yr_ycsvl from spldata.mm_yrprc where yr_cmpcd = '01' and yr_cmpcd = a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd and yr_cmpcd = '01' and yr_strtp in ('01','06','07','08')) where a.stp_cmpcd = '01' and a.stp_strtp in ('01','06','07','08') ; 


/* for monthly back update from yearly */
update spldata.mm_stprc a set (stp_crcqt,stp_crcvl) = (select yr_crcqt,yr_crcvl from spldata.mm_yrprc where yr_cmpcd='01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;
commit;

update spldata.mm_stprc a set (stp_cisqt,stp_cisvl) = (select yr_cisqt,yr_cisvl from spldata.mm_yrprc where yr_cmpcd='01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;
commit;

update spldata.mm_stprc a set (stp_cmrqt,stp_cmrvl) = (select yr_cmrqt,yr_cmrvl from spldata.mm_yrprc where yr_cmpcd='01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;
commit;

update spldata.mm_stprc a set (stp_csaqt,stp_csavl) = (select yr_csaqt,yr_csavl from spldata.mm_yrprc where yr_cmpcd='01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;
commit;

update spldata.mm_stprc a set (stp_cstqt,stp_cstvl) = (select yr_cstqt,yr_cstvl from spldata.mm_yrprc where yr_cmpcd='01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;
commit;

update spldata.mm_stprc a set (stp_ycsqt,stp_ycsvl) = (select yr_ycsqt,yr_ycsvl from spldata.mm_yrprc where yr_cmpcd='01' and yr_cmpcd=a.stp_cmpcd and yr_strtp = a.stp_strtp and yr_matcd = a.stp_matcd) where a.stp_cmpcd = '01' and a.stp_strtp in('01','08','06','07') ;
commit;

update spldata.mm_stprc set stp_mcrcq = stp_crcqt,stp_mcrcv = stp_crcvl,stp_mcisq = stp_cisqt,stp_mcisv = stp_cisvl,stp_mcmrq = stp_cmrqt,stp_mcmrv = stp_cmrvl,stp_mcsaq = stp_csaqt,stp_mcsav = stp_csavl,stp_mcstq = stp_cstqt,stp_mcstv = stp_cstvl,stp_mcsqt = stp_ycsqt,stp_mcsvl = stp_ycsvl,stp_mcmqt = stp_ycmqt,stp_mcmvl = stp_ycmvl where stp_cmpcd='01';
commit; 



/* Opening to closing stock integrity verification
*/

SELECT STP_MATCD,STP_MATDS,STP_UOMCD,STP_MOSQT,STP_MCSQT, (IFNULL(STP_MCRCQ,0) - IFNULL(STP_MORCQ,0)) RECQT, (IFNULL(STP_MCMRQ,0) - IFNULL(STP_MOMRQ,0)) MRNQT,(IFNULL(STP_MCISQ,0) - IFNULL(STP_MOISQ,0)) ISSQT, (IFNULL(STP_MCSAQ,0) - IFNULL(STP_MOSAQ,0)) SANQT, (ifnull(stp_mosqt,0)+(IFNULL(STP_MCRCQ,0) - IFNULL(STP_MORCQ,0))+(IFNULL(STP_MCMRQ,0) - IFNULL(STP_MOMRQ,0))-(IFNULL(STP_MCISQ,0) - IFNULL(STP_MOISQ,0))+(IFNULL(STP_MCSAQ,0) - IFNULL(STP_MOSAQ,0))) STP_MCSQT1  FROM spldata.MM_STPRC WHERE STP_STRTP = '08'  AND  (ifnull(stp_mosqt,0)+(IFNULL(STP_MCRCQ,0) - IFNULL(STP_MORCQ,0))+(IFNULL(STP_MCMRQ,0) - IFNULL(STP_MOMRQ,0))-(IFNULL(STP_MCISQ,0) - IFNULL(STP_MOISQ,0))+(IFNULL(STP_MCSAQ,0) - IFNULL(STP_MOSAQ,0))) <> ifnull(STP_MCSQT,0)  ORDER BY STP_MATCD;
select sum(yr_ycsvl) from spldata.mm_yrprc where yr_strtp='01';
92595406.44;
;
select sum(stp_yosvl),sum(stp_ycsvl) from spldata.mm_stprc where stp_strtp='01';
select sum(stp_yosvl),sum(stp_ycsvl),sum(stp_mcsvl) from spldata.mm_stprc where stp_strtp='01' and substr(stp_matcd,1,2) not in ('68','69');
drop table spldata.tt_rplgr;
commit;



select  GR_STRTP, sum(GR_ACPQT) from spldata.MM_GRMST where GR_CMPCD = '01' and  GR_ACPDT between '12/01/2009' and '12/31/2009' and GR_STSFL='2'  and ifnull(GR_ACPQT,0) >0  and gr_strtp in ('01','06','07','08','04') and substr(gr_matcd,1,2) not in ('85','86') group by GR_STRTP;
-- 01 : select sum(qty_acpt) from psl_gr01 where substr(mat_code,1,2) not in ('85','86')
-- 06,07,08 : select st,sum(qty_acpt) from psl_gr07 group by st

;
select  IS_STRTP, sum(IS_ISSQT) from spldata.MM_ISMST where IS_CMPCD = '01'  and DATE(IS_AUTDT) between '12/01/2009' and '12/31/2009'  and IS_STSFL='2' and ifnull(IS_ISSQT,0) >0 and is_strtp in ('01','06','07','08','04') group by IS_STRTP;
-- 04 : select sum(is_issqt) from mm_issdt
-- 01 : select sum(is_qty) from psl_is01
-- 06,07,08 : select st,sum(is_qty) from psl_is07 group by st

;
-- Releasing (allowing)  auto-issue generation and Bagging Entry for Next Month
select * from spldata.co_cdtrn where cmt_codcd='AUTIS' and cmt_cgmtp= 'S01' and cmt_cgstp='FGXXREF';
update spldata.co_cdtrn set cmt_chp01='01/02/2010', cmt_chp02 = '28/02/2010'  where cmt_codcd='AUTIS' and cmt_cgmtp= 'S01' and cmt_cgstp='FGXXREF';
commit;
;
