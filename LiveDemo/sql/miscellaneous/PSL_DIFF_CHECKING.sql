/** 
      QUERY TO CLACULATE THE CLOSING VALUE FROM OPENING AND CHECKING IT WITH CLOSING VALUE TO FINE THE DECRIPANIES.
      CLOSING = OPENING +( CUM GRIN + CUM SAN) - (CUM ISS - CUM MRN ) 
       NOTE DOWN THE ITEM CODES WHERE DECRIPANCY IS FOUND.        
*/

/*
      USED FOR CHECKING ON 01ST APRIL 2005 FOR DIFFERENCE IN GROUP 21 AND 84. VALUES WERE POSTED FROM YEARLY PRCESSING TO MONTHLY.
      DIFFERENCE DUE TO 99 GENERTAED SAN
*/

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE ROUND(isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)),0)  <> ROUND(isnull(STP_YCSVL,0),0) AND STP_MATCD LIKE  '35%'; 

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '47%'; 

/*
   CHECKING THE GRIN, SAN, ISSUE AND MRN QUANTITIES SEPARATELY.
*/
SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL ,isnull(STP_CRCVL,0) GRN, isnull(STP_CSAVL,0) SAN , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, isnull(STP_CISVL,0) ISS,isnull(STP_CMRVL,0) MRN,(isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '84%'; 




SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL ,isnull(STP_CRCVL,0) GRN, isnull(STP_CSAVL,0) SAN , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, isnull(STP_CISVL,0) ISS,isnull(STP_CMRVL,0) MRN,(isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD ='2105320011'; 

SELECT YR_MATCD,isnull(YR_YOSVL,0) L_YOSVL ,isnull(YR_CRCVL,0) GRN, isnull(YR_CSAVL,0) SAN , (isnull(YR_CRCVL,0) +isnull(YR_CSAVL,0)) L_RCTVL, isnull(YR_CISVL,0) ISS,isnull(YR_CMRVL,0) MRN,(isnull(YR_CISVL,0) -isnull(YR_CMRVL,0)) L_ISSVL, isnull(YR_YOSVL,0) + (isnull(YR_CRCVL,0) +isnull(YR_CSAVL,0)) - (isnull(YR_CISVL,0) -isnull(YR_CMRVL,0)) L_CALVL ,isnull(YR_YCSVL,0) L_YCSVL FROM SPLDATA.MM_YRPRC WHERE isnull(YR_YOSVL,0) + (isnull(YR_CRCVL,0) +isnull(YR_CSAVL,0)) - (isnull(YR_CISVL,0) -isnull(YR_CMRVL,0))  <> isnull(YR_YCSVL,0) AND YR_MATCD ='2105320011'; 

SELECT * FROM SPLDATA.MM_PSLYR WHERE PS_MATCD ='2105320011'; 
SELECT * FROM SPLDATA.MM_PSLYR WHERE PS_MATCD LIKE '84%' ;AND PS_DOCRF ='9999999999'; 
SELECT SUM(PS_DOCVL) FROM SPLDATA.MM_PSLYR WHERE PS_MATCD LIKE '84%' AND PS_DOCRF ='9999999999'; 

SELECT STP_YCSVL,STP_MCSVL FROM SPLDATA.MM_STPRC WHERE STP_STRTP ='01' AND STP_MATCD ='2105320011'; 
UPDATE SPLDATA.MM_STPRC SET STP_YCSVL = 52992.85,STP_MCSVL = 52992.85 WHERE STP_STRTP ='01' AND STP_MATCD ='2105320011';
COMMIT; 

UPDATE SPLDATA.MM_STPRC SET STP_YCSVL = STP_CRCVL+STP_CSAVL,STP_MCSVL = STP_CRCVL+STP_CSAVL WHERE STP_STRTP ='01' AND STP_MATCD IN ('8470100011',	
'8470100021',	
'8470100031',	
'8470100051',	
'8470100061',	
'8470100091',	
'8470100101');
commit;

select * from spldata.mm_samst where Sa_MATCD ='2105320011'; 
select * from spldata.mm_samst where Sa_sanno = '57000034';

select * from spldata.mm_grmst where gr_MATCD ='2105320011'; 

select * from spldata.mm_samst where Sa_MATCD in('8470100011',	
'8470100021',	
'8470100031',	
'8470100051',	
'8470100061',	
'8470100091',	
'8470100101');

select * from spldata.mm_grmst where gr_MATCD ='8470100101'; 
select * from spldata.mm_grmst where gr_MATCD ='8470100011'; 
select * from spldata.mm_grmst where gr_MATCD ='8470100011';

SELECT * FROM SPLDATA.CO_CDTRN WHERE CMT_CODCD LIKE '%MONTH';

select * from spldata.mm_blmst where bl_docno ='50101268';

select * from spldata.co_ptmst where pt_prtcd = 'S0265';

select * from spldata.mm_pomst where po_vencd = 'S0265';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '14%'; 

SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '1405157931' AND GR_ACPDT >='07/01/2004';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '1405157931';
SELECT * FROM SPLDATA.MM_MRMST WHERE MR_MATCD = '1405157931';
SELECT * FROM SPLDATA.MM_SAMST WHERE SA_MATCD = '1405157931';

SELECT * FROM SPLDATA.MM_SAMST WHERE SA_MATCD = '1405157931';
SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '1405157931';

/* checking on 01 sept 2005 */
/* checking the difference shown in group wise transaction summary */

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '02%'; 

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD ='0205050532';

select * from spldata.mm_grmst where gr_matcd = '0205050532';
select * from spldata.mm_stprc where stp_matcd = '0205050532';
select * from spldata.mm_ismst where is_matcd = '0205050532';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '05%'; 

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD ='0505105661';

select * from spldata.mm_grmst where gr_matcd = '0505105661';
select * from spldata.mm_stprc where stp_matcd = '0505105661';
select * from spldata.mm_ismst where is_matcd = '0505105661';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '14%'; 

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD ='1405154751';

select * from spldata.mm_grmst where gr_matcd = '1405154751';
select * from spldata.mm_stprc where stp_matcd = '1405154751';
select * from spldata.mm_ismst where is_matcd = '1405154751';


SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '34%'; 

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD ='3455150515';

select * from spldata.mm_grmst where gr_matcd = '3455150515';
select * from spldata.mm_stprc where stp_matcd = '3455150515';
select * from spldata.mm_ismst where is_matcd = '3455150515';

select count(*) from spldata.co_ptmst where pt_prttp = 'C';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '47%'; 

select * from spldata.mm_grmst where gr_matcd = '4715400305';
select * from spldata.mm_stprc where stp_matcd = '4715400305';
select * from spldata.mm_ismst where is_matcd = '4715400305';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '74%'; 

select * from spldata.mm_grmst where gr_matcd = '7473135645';
select * from spldata.mm_stprc where stp_matcd = '7473135645';
select * from spldata.mm_ismst where is_matcd = '7473135645';

select * from spldata.mm_grmst where gr_matcd = '7473990035';
select * from spldata.mm_stprc where stp_matcd = '7473990035';
select * from spldata.mm_ismst where is_matcd = '7473990035';

select * from spldata.mm_grmst where gr_matcd = '7473247075';
select * from spldata.mm_stprc where stp_matcd = '7473247075';
select * from spldata.mm_ismst where is_matcd = '7473247075';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '99%'; 

select * from spldata.mm_grmst where gr_matcd = '9930250551';
select * from spldata.mm_stprc where stp_matcd = '9930250551';
select * from spldata.mm_ismst where is_matcd = '9930250551';

select * from spldata.mm_grmst where gr_matcd = '9920354461';
select * from spldata.mm_stprc where stp_matcd = '9920354461';
select * from spldata.mm_ismst where is_matcd = '9920354461';

select * from spldata.mm_grmst where gr_matcd = '9920354471';
select * from spldata.mm_stprc where stp_matcd = '9920354471';
select * from spldata.mm_ismst where is_matcd = '9920354471';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '77%'; 

select * from spldata.mm_grmst where gr_matcd = '7760454525';
select * from spldata.mm_stprc where stp_matcd = '7760454525';
select * from spldata.mm_ismst where is_matcd = '7760454525';

select * from spldata.mm_grmst where gr_matcd = '7760990305';
select * from spldata.mm_stprc where stp_matcd = '7760990305';
select * from spldata.mm_ismst where is_matcd = '7760990305';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '76%'; 

select * from spldata.mm_grmst where gr_matcd = '7690650105';
select * from spldata.mm_stprc where stp_matcd = '7690650105';
select * from spldata.mm_ismst where is_matcd = '7690650105';

select * from spldata.mm_grmst where gr_matcd = '7690540945';
select * from spldata.mm_stprc where stp_matcd = '7690540945';
select * from spldata.mm_ismst where is_matcd = '7690540945';

select * from spldata.mm_grmst where gr_matcd = '7690540945';
select * from spldata.mm_stprc where stp_matcd = '7690540945';
select * from spldata.mm_ismst where is_matcd = '7690540945';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '81%'; 

select * from spldata.mm_grmst where gr_matcd = '8184572501';
select * from spldata.mm_stprc where stp_matcd = '8184572501';
select * from spldata.mm_ismst where is_matcd = '8184572501';

select * from spldata.mm_grmst where gr_matcd = '8184572105';
select * from spldata.mm_stprc where stp_matcd = '8184572105';
select * from spldata.mm_ismst where is_matcd = '8184572105';

select * from spldata.mm_grmst where gr_matcd = '8198191511';
select * from spldata.mm_stprc where stp_matcd = '8198191511';
select * from spldata.mm_ismst where is_matcd = '8198191511';




SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '69%';

select * from spldata.mm_grmst where gr_matcd = '6925051165' and gr_grndt >='07/01/2005' and gr_porvl <>gr_bilvl;
select * from spldata.mm_stprc where stp_matcd =  '6925051165'  ;
select * from spldata.mm_ismst where is_matcd =  '6925051165' and CONVERT(varchar,is_autdt,101) >='07/01/2005';
select * from spldata.mm_mrmst where mr_matcd =  '6925051165';
select * from spldata.mm_samst where sa_matcd =  '6925051165';
 '6925051165' ;
SELECT * FROM SPLDATA.MM_BILTR WHERE BIL_DOCRF = '63300154';

SELECT * FROM SPLDATA.MM_BILTR WHERE BIL_MATCD = '6925051165' AND BIL_STSFL ='X';
SELECT * FROM SPLDATA.MM_BLMST WHERE BL_DOCNO ='60100564';

select sum(isnull(gr_porvl,0)),sum(isnull(gr_bilvl,0)) from spldata.mm_grmst where gr_matcd = '6925051165' and gr_grndt >='07/01/2005'  and isnull(gr_bilvl,0) >0;

select sum(gr_porvl) from spldata.mm_grmst where gr_matcd = '6925051165' and gr_acpdt >='07/01/2005' and isnull(gr_bilvl,0) =0;

delete from spltest.mm_stprc;
commit;

insert into spltest.mm_grmst select * from spldata.mm_grmst;

delete from spltest.mm_ismst;
commit;

insert into spltest.mm_ismst select * from spldata.mm_ismst;
insert into spltest.mm_stprc select * from spldata.mm_stprc;

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '99%'; 

SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '6616054805';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '6613054305';

SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '6613054305';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '6613054305';

SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '3535800301';

SELECT * FROM SPLDATA.HR_VSTRN;

DELETE FROM SPLTEST.MM_STPRC;
INSERT INTO SPLTEST.MM_STPRC SELECT * FROM SPLDATA.MM_STPRC;
COMMIT;

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0))  <> isnull(STP_YCSVL,0) AND STP_MATCD LIKE  '99%'; 

SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '9902040815';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '9902040815';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '9902040815';
SELECT * FROM SPLDATA.MM_SAMST WHERE SA_MATCD = '9902040815';
SELECT * FROM SPLDATA.MM_MRMST WHERE MR_MATCD = '9902040815';
SELECT * FROM SPLDATA.MM_BLMST WHERE BL_DOCNO = '60100772';

SELECT * FROM SPLDATA.MM_GRMST WHERE GR_GRNNO = '63100551';

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE ROUND(isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)),0)  <> ROUND(isnull(STP_YCSVL,0),0) AND STP_MATCD LIKE  '22%'; 

SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '7810150185';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '7810150185';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '7810150185';

SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '7810150185';
SELECT * FROM SPLDATA.MM_SAMST WHERE SA_MATCD = '7810150185';
SELECT * FROM SPLDATA.MM_MRMST WHERE MR_MATCD = '7810150185';
SELECT * FROM SPLDATA.MM_BLMST WHERE BL_DOCNO = '60100772';
SELECT * FROM SPLDATA.MM_BLMST WHERE BL_DOCNO ='60100806';
SELECT * FROM SPLDATA.MM_BILTR WHERE BIL_DOCNO ='60100806';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '9151080315';
SELECT * FROM SPLDATA.MM_BILTR WHERE BIL_MATCD ='9151080315';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '3530200151';


SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '2225122025';	
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '2225122095';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '2225171525';	

ROLLBACK;

DELETE FROM SPLTEST.MM_STPRC;
DELETE FROM SPLTEST.MM_STMST;
DELETE FROM SPLTEST.MM_GRMST;
DELETE FROM SPLTEST.MM_ISMST;
DELETE FROM SPLTEST.MM_MRMST;
DELETE FROM SPLTEST.MM_SAMST;

COMMIT;
INSERT INTO SPLTEST.MM_STPRC SELECT * FROM SPLDATA.MM_STPRC;
INSERT INTO SPLTEST.MM_STMST SELECT * FROM SPLDATA.MM_STMST;
INSERT INTO SPLTEST.MM_GRMST SELECT * FROM SPLDATA.MM_GRMST;
INSERT INTO SPLTEST.MM_ISMST SELECT * FROM SPLDATA.MM_ISMST;
INSERT INTO SPLTEST.MM_SAMST SELECT * FROM SPLDATA.MM_SAMST;
INSERT INTO SPLTEST.MM_MRMST SELECT * FROM SPLDATA.MM_MRMST;

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE ROUND(isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)),0)  <> ROUND(isnull(STP_YCSVL,0),0) AND STP_MATCD LIKE  '47%'; 

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE ROUND(isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)),0)  <> ROUND(isnull(STP_YCSVL,0),0) AND STP_MATCD LIKE  '99%'; 

SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '4750157055' ;
'4750152155';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '9920560135';
SELECT * FROM SPLDATA.MM_isMST WHERE is_MATCD = '9920560135';

DELETE FROM SPLTEST.MM_STPRC;
COMMIT;
INSERT INTO SPLTEST.MM_STPRC SELECT * FROM SPLDATA.MM_STPRC;

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE ROUND(isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)),0)  <> ROUND(isnull(STP_YCSVL,0),0) AND STP_MATCD LIKE  '84%';

SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '9920460265';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '9920460025';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '9920460035';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '9920460165';

SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '9920460265';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '9920460025';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '9920460035';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '9920460165';

select * from spldata.mm_grmst where gr_matcd = '3530950171';
select * from spldata.mm_stprc where stp_matcd = '3530950171';
select * from spldata.mm_ismst where is_matcd = '3530950171';

select * from spldata.mm_stprc where stp_matcd = '7690540945';
select * from spldata.mm_ismst where is_matcd = '7690540945';

select * from spldata.mm_BILTR where BIL_matcd = '3530950171';

DELETE FROM SPLTEST.MM_STPRC;
COMMIT;
INSERT INTO SPLTEST.MM_STPRC SELECT * FROM SPLDATA.MM_STPRC;

--  USED ON 01/02/2007 

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE ROUND(isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)),0)  <> ROUND(isnull(STP_YCSVL,0),0) AND STP_MATCD LIKE  '84%';

SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_MRMST WHERE MR_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_SAMST WHERE SA_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_STNTR WHERE STN_LUPDT >='02/01/2007';
SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '8470301911';6048.93;

UPDATE SPLDATA.MM_STPRC SET STP_CISVL = 3826.63,STP_MCISV = 3826.63,STP_YCLRT = 3826.63 WHERE STP_MATCD = '8470301911' ;
COMMIT;

SELECT STP_MATCD,isnull(STP_YOSVL,0) L_YOSVL , (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)+isnull(STP_CSTVL,0)) L_RCTVL, (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_ISSVL, isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)+isnull(STP_CSTVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)) L_CALVL ,isnull(STP_YCSVL,0) L_YCSVL FROM SPLDATA.MM_STPRC WHERE ROUND(isnull(STP_YOSVL,0) + (isnull(STP_CRCVL,0) +isnull(STP_CSAVL,0)+isnull(STP_CSTVL,0)) - (isnull(STP_CISVL,0) -isnull(STP_CMRVL,0)),0)  <> ROUND(isnull(STP_YCSVL,0),0) AND STP_MATCD LIKE  '84%';

SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_MRMST WHERE MR_MATCD = '8470301911';
SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD =  '8470301911';




SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD = '8470301921';
SELECT * FROM SPLDATA.MM_GRMST WHERE GR_MATCD = '8470301921';
SELECT * FROM SPLDATA.MM_ISMST WHERE IS_MATCD = '8470301921';
SELECT * FROM SPLDATA.MM_MRMST WHERE MR_MATCD = '8470301921';
SELECT * FROM SPLDATA.MM_STPRC WHERE STP_MATCD =  '8470301921';
COMMIT;

SELECT * FROM SPLDATA.MM_STNTR;
