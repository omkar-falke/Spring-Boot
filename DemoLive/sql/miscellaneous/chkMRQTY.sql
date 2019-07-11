/* QUERY TO FIND MULTIPLE D.O. / D.O. QTY IN MR_DOTRN MORE THAN INDQT */

SELECT INT_MKTTP,INT_INDNO,INT_PRDDS,INT_INDQT,SUM(DOT_DORQT) FROM SPLDATA.MR_INTRN,SPLDATA.MR_DOTRN WHERE INT_MKTTP=DOT_MKTTP AND INT_INDNO=DOT_INDNO AND INT_STSFL<>'X' AND DOT_STSFL<>'X' AND INT_PRDCD=DOT_PRDCD AND INT_LUPDT>'03/17/2004'  GROUP BY INT_MKTTP,INT_INDNO,INT_PRDDS,INT_INDQT HAVING  INT_INDQT<SUM(DOT_DORQT);


/*TO FIND OUT WRONG UPDATION OF INT_DORQT */

SELECT INT_INDNO,INT_PRDDS,INT_INDQT,INT_DORQT,SUM(DOT_DORQT) FROM SPLDATA.MR_INTRN,SPLDATA.MR_DOTRN WHERE INT_INDNO=DOT_INDNO AND INT_STSFL<>'X' AND DOT_STSFL<>'X' AND INT_PRDCD=DOT_PRDCD AND INT_LUPDT>'03/20/2004'  GROUP BY INT_INDNO,INT_PRDDS,INT_INDQT,INT_DORQT HAVING  INT_DORQT<>SUM(DOT_DORQT);


/* TO FIND CANCELLED D.O. WITH L.A.*/
SELECT * FROM SPLDATA.MR_DOTRN WHERE DOT_LADQT>0 AND DOT_STSFL='X' AND DOT_LUPDT>'03/20/2004';

/* TO FIND CANCELLED GRADES WITH LIVE D.O. */

SELECT INT_INDNO,INT_PRDDS,INT_INDQT,SUM(DOT_DORQT) FROM SPLDATA.MR_INTRN,SPLDATA.MR_DOTRN WHERE INT_INDNO=DOT_INDNO AND INT_STSFL='X' AND DOT_STSFL<>'X' AND INT_PRDCD=DOT_PRDCD AND INT_LUPDT>'03/20/2004'  GROUP BY INT_INDNO,INT_PRDDS,INT_INDQT HAVING  SUM(DOT_DORQT)>0;




--  ********** Invoice records found but DO is missing **********
select * from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101)>'03/18/2004' and ivt_invqt>0 and ivt_stsfl<>'X' and ivt_dorno + ivt_prdcd not in (select dot_dorno + dot_prdcd from spldata.mr_dotrn where dot_stsfl<>'X' and dot_dorno in (select ivt_dorno from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101)>'03/18/2004' and ivt_invqt>0 and ivt_stsfl<>'X'));

--  ********** DO records found but INDENT is missing **********
select * from spldata.mr_dotrn where dot_dordt>'03/17/2004' and dot_dorqt>0 and dot_stsfl<>'X' and dot_indno + dot_prdcd not in (select int_indno + int_prdcd from spldata.mr_intrn where int_stsfl<>'X' and int_indno in (select dot_indno from spldata.mr_dotrn where dot_dordt>'03/17/2004' and dot_dorqt>0 and dot_stsfl<>'X'));


-- ********** DO qty in INDENT  is negative ***********
select in_mkttp,in_indno,in_bkgdt,int_prdds,int_indqt,int_dorqt from spldata.mr_inmst,spldata.mr_intrn where in_mkttp=int_mkttp and in_indno=int_indno and int_dorqt<0 and upper(int_stsfl)<>'X' and int_mkttp='01' order by in_bkgdt;

-- ********** INVOICE  qty in DO is negative  ***********
select dot_mkttp,dot_dorno,dot_prdds,dot_dorqt,dot_invqt  from spldata.mr_dotrn where dot_invqt<0 and upper(dot_stsfl)<>'X'  and dot_mkttp='01' order by dot_dordt;


-- ********** DO qty exceeds INDENT qty. ***********
select in_mkttp,in_indno,in_bkgdt,int_prdds,int_indqt,int_dorqt  from spldata.mr_inmst,spldata.mr_intrn where  in_mkttp=int_mkttp and in_indno=int_indno and int_dorqt>int_indqt and upper(int_stsfl)<>'X' and int_mkttp='01' order by in_bkgdt;


call spldata.setINTRN('01',date('05/01/2003'),date('05/31/2003'));


-- ********** INVOICE  qty exceeds DO qty. ***********
select dot_mkttp,dot_dorno,dot_dordt,dot_prdds,dot_dorqt,dot_invqt  from spldata.mr_dotrn where dot_invqt>dot_dorqt and upper(dot_stsfl)<>'X' and dot_mkttp='01' order by dot_dordt;




-- ********** INVOICE  qty exceeds DO qty. ***********
select dot_mkttp,dot_dorno,dot_dordt,dot_prdds,dot_dorqt,dot_invqt  from spldata.mr_dotrn where dot_invqt>dot_dorqt and upper(dot_stsfl)<>'X';


