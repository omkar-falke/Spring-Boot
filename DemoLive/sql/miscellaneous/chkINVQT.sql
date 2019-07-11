/* Enter one or more SQL statements separated by semicolons */

update spldata.sa_usmst set us_stsfl='' where us_stsfl='D';
commit;

SELECT DOT_DORNO,dot_dordt,DOT_INDNO, DOT_PRDDS,DOT_PKGTP, DOT_INVQT,SUM(IVT_INVQT),						DOT_LADQT FROM SPLDATA.MR_DOTRN,SPLDATA.MR_IVTRN WHERE DOT_STSFL<>'X' AND 						
IVT_STSFL<>'X' AND  IVT_DORNO=DOT_DORNO AND IVT_PRDCD=DOT_PRDCD AND 						
IVT_PKGTP=DOT_PKGTP GROUP BY DOT_DORNO,dot_dordt,DOT_PRDDS,DOT_INDNO,DOT_PKGTP,						
DOT_INVQT,DOT_LADQT HAVING DOT_INVQT<>SUM(IVT_INVQT);						

call spldata.setINTRN('01',date('01/07/2004'),date('01/07/2004'));
commit;
