/* Enter one or more SQL statements separated by semicolons */

/* updating the ownership from Catlog master */

UPDATE SPLDATA.MM_STPRC SET STP_OWNBY =(SELECT CT_OWNBY FROM SPLDATA.CO_CTMST WHERE CT_CODTP ='CD' AND CT_MATCD = STP_MATCD) where stp_cmpcd='01';
COMMIT;
SELECT CT_MATCD,CT_OWNBY FROM SPLDATA.CO_CTMST WHERE CT_MATCD IN( SELECT STP_MATCD FROM SPLDATA.MM_STPRC WHERE isnull(STP_OWNBY,'') ='');
UPDATE SPLDATA.MM_STPRC SET STP_OWNBY = 'X' where isnull(stp_ownby,'') ='' and stp_cmpcd='01';
UPDATE SPLDATA.MM_STPRC SET STP_OWNBY = 'X' where stp_ownby is null and stp_cmpcd='01';
/* Updating the category STP_CATFL */

/* Initializing the category Flag */
update spldata.mm_stprc set stp_catfl = null  where stp_cmpcd='01';
commit;

/* Updating category flag for stock controlled item as 1 */
update spldata.mm_stprc set stp_catfl ='1' where stp_cmpcd + stp_strtp + stp_matcd in (select st_cmpcd + st_strtp + st_matcd from spldata.mm_stmst where isnull(st_stkfl,'') ='Y')  and stp_cmpcd='01';
commit;

/* Updating category flag for Insurance item as 2, overwriting the common item codes if any  */

update spldata.mm_stprc set stp_catfl ='2' where stp_cmpcd + stp_strtp + stp_matcd in ( select st_cmpcd + st_strtp + st_matcd from spldata.mm_stmst where isnull(st_vedfl,'') ='E') and stp_cmpcd='01';
commit;


/* Updating category flag for Obsolete item as 3, overwriting the common item codes if any  */

update spldata.mm_stprc set stp_catfl ='3' where stp_cmpcd + stp_strtp + stp_matcd in ( select st_cmpcd + st_strtp + st_matcd from spldata.mm_stmst where isnull(st_stsfl,'') ='9') and stp_cmpcd='01';
commit;

/* Updating category flag for Other item as 4, overwriting the common item codes if any  */

update spldata.mm_stprc set stp_catfl ='4' where stp_catfl is null  and stp_cmpcd='01';
commit;

