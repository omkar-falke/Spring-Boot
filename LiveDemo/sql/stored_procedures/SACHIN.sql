select * from spldata.sa_ppmst where pp_prgds like ('Gradewise Lot%');
commit;
SELECT * FROM SPLDATA.MM_POMST WHERE PO_VENCD='O0032';
select IVT_PRDDS,IVT_SALTP,cmt_codds,SUM(IVT_INVQT) ivt_invqt,sum(ivt_excvl) ivt_excvl,sum(ivt_edcvl) ivt_edcvl,sum(ivt_ehcvl) ivt_ehcvl,SUM(IVT_ASSVL) ivt_assvl from spldata.MR_IVTRN,SPLDATA.CO_CDTRN WHERE LEFT(IVT_PRDCD,2) IN ('52','54')  AND CONVERT(varchar,IVT_INVDT) BETWEEN '09/08/2008' AND '03/31/2009' AND IVT_STSFL !='X'  and cmt_cgstp='MR00SAL' AND CMT_CODCD=IVT_SALTP GROUP BY IVT_PRDDS,IVT_SALTP,cmt_codds ORDER BY IVT_PRDDS,CMT_CODDS;
SELECT * FROM SPLDATA.CO_CDTRN WHERE CMT_CGSTP='MR00SAL';

SELECT * FROM SPLDATA.MR_IVTRN WHERE IVT_INVNO='00000001';
select IVT_SALTP,sum(ivt_invqt) ivt_invqt,sum(ivt_excvl) ivt_excvl,sum(ivt_edcvl) ivt_edcvl,sum(ivt_ehcvl) ivt_ehcvl,SUM(IVT_ASSVL) ivt_assvl from spldata.MR_IVTRN WHERE IVT_PRDDS='SCOGY'  AND CONVERT(varchar,IVT_INVDT,101) BETWEEN  '04/01/2008' AND '03/31/2009' AND   IVT_STSFL !='X' and ivt_cmpcd='01' GROUP BY IVT_SALTP; ORDER BY IVT_INVDT;

SELECT IVT_INVNO,IVT_INVQT,IVT_EXCVL,IVT_EDCVL,IVT_EHCVL,IVT_ASSVL FROM SPLDATA.MR_IVTRN WHERE IVT_PRDDS ='PSOG' AND CONVERT(varchar,IVT_INVDT,101)='12/31/2008' AND IVT_CMPCD='01'  AND IVT_INVNO IN('90007352','90007353','90007359') AND IVT_STSFL!='X'  ORDER BY IVT_INVNO;

SELECT IVT_INVNO,IVT_INVDT,IVT_INVQT,IVT_EXCVL,IVT_EDCVL,IVT_EHCVL,IVT_ASSVL FROM SPLDATA.MR_IVTRN WHERE CONVERT(varchar,IVT_INVDT,101) > '09/07/2008' AND IVT_PRDDS='SH400M' AND IVT_SALTP='16' ORDER BY IVT_INVDT;

select * from spldata.mr_ivtrn where ivt_invno='00000030' and ivt_cmpcd='01'  ORDER ;

select * from spldata.mr_ivtrn where left(ivt_invno,1)='0' and ivt_cmpcd='01' ;



