drop procedure spltest.crtTSTDATA;
commit;


create procedure spltest.crtTSTDATA(IN LP_SYSNM varchar(2), IN LP_STRDT varchar(10), IN LP_ENDDT varchar(10))  language sql  modifies sql data 
 P1:
 begin
   if LP_SYSNM = 'MR' then

     
    delete from spltest.TEMP_CHK;
    commit;
    drop table spltest.MR_OCMST;
     commit;
     create table spltest.MR_OCMST like spldata.MR_OCMST;
     insert into spltest.MR_OCMST select * from spldata.MR_OCMST where OC_BKGDT between LP_STRDT and LP_ENDDT;
     commit;
     insert into spltest.TEMP_CHK values('MR_OCMST');
     commit;

     drop table spltest.MR_OCTRN;
     commit;
     create table spltest.MR_OCTRN like spldata.MR_OCTRN;
     insert into spltest.MR_OCTRN select * from spldata.MR_OCTRN where OCT_MKTTP +  OCT_OCFNO in (select OC_MKTTP +  OC_OCFNO from spltest.MR_OCMST) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_OCTRN');
     commit;

     drop table spltest.MR_OCDEL;
     commit;
     create table spltest.MR_OCDEL like spldata.MR_OCDEL;
     insert into spltest.MR_OCDEL select * from spldata.MR_OCDEL where OCD_MKTTP +  OCD_OCFNO in (select OC_MKTTP +  OC_OCFNO from spltest.MR_OCMST) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_OCDEL');
     commit;


     drop table spltest.MR_INMST;
     commit;
     create table spltest.MR_INMST like spldata.MR_INMST;
     insert into spltest.MR_INMST select * from spldata.MR_INMST where IN_BKGDT between LP_STRDT and LP_ENDDT;
     commit;
     insert into spltest.TEMP_CHK values('MR_INMST');
     commit;


     drop table spltest.MR_INTRN;
     commit;
     create table spltest.MR_INTRN like spldata.MR_INTRN;
     insert into spltest.MR_INTRN select * from spldata.MR_INTRN where INT_MKTTP +  INT_INDNO in (select IN_MKTTP +  IN_INDNO from spltest.MR_INMST) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_INTRN');
     commit;


     drop table spltest.MR_INDEL;
     commit;
     create table spltest.MR_INDEL like spldata.MR_INDEL;
     insert into spltest.MR_INDEL select * from spldata.MR_INDEL where IND_MKTTP +  IND_INDNO in (select IN_MKTTP +  IN_INDNO from spltest.MR_INMST);
     commit;
     insert into spltest.TEMP_CHK values('MR_INDEL');
     commit;


     drop table spltest.MR_DOTRN;
     commit;
     create table spltest.MR_DOTRN like spldata.MR_DOTRN;
     insert into spltest.MR_DOTRN select * from spldata.MR_DOTRN where DOT_MKTTP +  DOT_INDNO in (select IN_MKTTP +  IN_INDNO from spltest.MR_INMST) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_DOTRN');
     commit;


     drop table spltest.MR_DODEL;
     commit;
     create table spltest.MR_DODEL like spldata.MR_DODEL;
     insert into spltest.MR_DODEL select * from spldata.MR_DODEL where DOD_MKTTP +  DOD_DORNO in (select DOT_MKTTP +  DOT_DORNO from spltest.MR_DOTRN) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_DODEL');
     commit;


     drop table spltest.MR_IVTRN;
     commit;
     create table spltest.MR_IVTRN like spldata.MR_IVTRN;
     insert into spltest.MR_IVTRN select * from spldata.MR_IVTRN where IVT_MKTTP +  IVT_INDNO in (select IN_MKTTP +  IN_INDNO from spltest.MR_INMST) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_IVTRN');
     commit;


     drop table spltest.MR_RMMST;
     commit;
     create table spltest.MR_RMMST like spldata.MR_RMMST;
     insert into spltest.MR_RMMST select * from spldata.MR_RMMST where RM_TRNTP = 'OR' and RM_MKTTP +  RM_DOCNO in (select OC_MKTTP +  OC_OCFNO from spltest.MR_OCMST);
     insert into spltest.MR_RMMST select * from spldata.MR_RMMST where RM_TRNTP = 'IR' and RM_MKTTP +  RM_DOCNO in (select IN_MKTTP +  IN_INDNO from spltest.MR_INMST);
     insert into spltest.MR_RMMST select * from spldata.MR_RMMST where RM_TRNTP = 'DO' and RM_MKTTP +  RM_DOCNO in (select DOT_MKTTP +  DOT_DORNO from spltest.MR_DOTRN);
     commit;
     insert into spltest.MR_RMMST select * from spldata.MR_RMMST where RM_TRNTP = 'LA' and RM_MKTTP +  RM_DOCNO in (select IVT_MKTTP +  IVT_LADNO from spltest.MR_IVTRN);
     commit;
     insert into spltest.TEMP_CHK values('MR_RMMST');
     commit;
/*
     drop table spltest.MR_IVTR1;
     commit;
     create table spltest.MR_IVTR1 like spldata.MR_IVTR1;
     insert into spltest.MR_IVTR1 select * from spldata.MR_IVTR1 where IVT_MKTTP +  IVT_INDNO in (select IN_MKTTP +  IN_INDNO from spltest.MR_INMST) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_IVTR1');
     commit;
*/


    drop table spltest.CO_TXDOC;
     commit;
     create table spltest.CO_TXDOC like spldata.CO_TXDOC;
     insert into spltest.CO_TXDOC select * from spldata.CO_TXDOC where TX_SYSCD = 'MR' and TX_DOCTP = 'IND' and TX_DOCNO in (select IN_INDNO from spltest.MR_INMST);
     insert into spltest.CO_TXDOC select * from spldata.CO_TXDOC where TX_SYSCD = 'MR'  and TX_DOCTP = 'LAD' and TX_DOCNO in (select IVT_LADNO from spltest.MR_IVTRN);
     commit;
     insert into spltest.TEMP_CHK values('CO_TXDOC');
     commit;



    drop table spltest.CO_TXSPC;
     commit;
     create table spltest.CO_TXSPC like spldata.CO_TXSPC;
     insert into spltest.CO_TXSPC select * from spldata.CO_TXSPC where TXT_SYSCD = 'MR' and TXT_DOCTP = 'IND' and TXT_DOCNO in (select IN_INDNO from spltest.MR_INMST);
     insert into spltest.CO_TXSPC select * from spldata.CO_TXSPC where TXT_SYSCD = 'MR'  and TXT_DOCTP = 'OCF' and TXT_DOCNO in (select OC_OCFNO from spltest.MR_OCMST);
     commit;
     insert into spltest.TEMP_CHK values('CO_TXSPC');
     commit;




     drop table spltest.FG_ISTRN;
     commit;
     create table spltest.FG_ISTRN like spldata.FG_ISTRN;
     insert into spltest.FG_ISTRN select * from spldata.FG_ISTRN where IST_ISSNO in  (select IVT_LADNO from spltest.MR_IVTRN);
     commit;
     insert into spltest.TEMP_CHK values('FG_ISTRN');
     commit;



     drop table spltest.FG_STMST;
     commit;
     create table spltest.FG_STMST like spldata.FG_STMST;
     insert into spltest.FG_STMST select * from spldata.FG_STMST where st_dosqt+st_stkqt+st_douqt+st_uclqt>0;
     insert into spltest.FG_STMST select * from spldata.FG_STMST where st_dosqt+st_stkqt+st_douqt+st_uclqt = 0 and st_lotno in (select ist_lotno from spltest.fg_istrn);
     commit;
     insert into spltest.TEMP_CHK values('FG_STMST');
     commit;

     drop table spltest.FG_RCTRN;
     commit;
     create table spltest.FG_RCTRN like spldata.FG_RCTRN;
     insert into spltest.FG_RCTRN select * from spldata.FG_RCTRN where RCT_LOTNO in  (select ST_LOTNO from spltest.FG_STMST);
     commit;
     insert into spltest.TEMP_CHK values('FG_RCTRN');
     commit;


     drop table spltest.PR_LTMST;
     commit;
     create table spltest.PR_LTMST like spldata.PR_LTMST;
     insert into spltest.PR_LTMST select * from spldata.PR_LTMST where LT_LOTNO in  (select ST_LOTNO from spltest.FG_STMST);
     commit;
     insert into spltest.TEMP_CHK values('PR_LTMST');
     commit;

     drop table spltest.MM_WBTRN;
     commit;
     create table spltest.MM_WBTRN like spldata.MM_WBTRN;
     insert into spltest.MM_WBTRN select * from spldata.MM_WBTRN where wb_doctp = '03' and wb_docno in (select ivt_ginno from spltest.mr_ivtrn where isnull(ivt_ginno,'')<>'');
     commit;
     insert into spltest.TEMP_CHK values('MM_WBTRN');
     commit;


     delete from spltest.CO_CDTRN where SUBSTRING(cmt_cgstp,1,2) in ('FG','MR');
     commit;
     insert into spltest.CO_CDTRN select * from spldata.CO_CDTRN where SUBSTRING(cmt_cgstp,1,2) in ('FG','MR');
     commit;
     insert into spltest.TEMP_CHK values('CO_CDTRN');
     commit;


     drop table spltest.CO_PTMST;
     commit;
     create table spltest.CO_PTMST like spldata.CO_PTMST;
     insert into spltest.CO_PTMST select * from spldata.CO_PTMST;
     commit;
     insert into spltest.TEMP_CHK values('CO_PTMST');
     commit;


     drop table spltest.CO_PRMST;
     commit;
     create table spltest.CO_PRMST like spldata.CO_PRMST;
     insert into spltest.CO_PRMST select * from spldata.CO_PRMST;
     commit;
     insert into spltest.TEMP_CHK values('CO_PRMST');
     commit;


     drop table spltest.MR_PITRN;
     commit;
     create table spltest.MR_PITRN like spldata.MR_PITRN;
     insert into spltest.MR_PITRN select * from spldata.MR_PITRN where PIT_LADNO in (select IVT_LADNO from spltest.MR_IVTRN);
     commit;
     insert into spltest.TEMP_CHK values('MR_PITRN');
     commit;

     drop table spltest.MR_PIMST;
     commit;
     create table spltest.MR_PIMST like spldata.MR_PIMST;
     insert into spltest.MR_PIMST select * from spldata.MR_PIMST where PI_PINNO in (select PIT_PINNO from spltest.MR_PITRN);
     commit;
     insert into spltest.TEMP_CHK values('MR_PIMST');
     commit;

 
     drop table spltest.MR_PTTRN;
     commit;
     create table spltest.MR_PTTRN like spldata.MR_PTTRN;
     insert into spltest.MR_PTTRN select * from spldata.MR_PTTRN where PT_INVNO in (select IVT_INVNO from spltest.MR_IVTRN);
     insert into spltest.MR_PTTRN select * from spldata.MR_PTTRN where PT_DOCDT between LP_STRDT and LP_ENDDT and PT_INVNO not in (select b.PT_INVNO from spltest.MR_PTTRN b);
     commit;
     insert into spltest.TEMP_CHK values('MR_PTTRN');
     commit;

    drop table spltest.MR_PATRN;
     commit;
     create table spltest.MR_PATRN like spldata.MR_PATRN;
     insert into spltest.MR_PATRN select * from spldata.MR_PATRN where pa_dbttp +  pa_dbtno in (select pt_crdtp +  pt_docrf from spltest.MR_PTTRN);
     commit;
     insert into spltest.TEMP_CHK values('MR_PATRN');
     commit;


     drop table spltest.MR_PRTRN;
     commit;
     create table spltest.MR_PRTRN like spldata.MR_PRTRN;
     insert into spltest.MR_PRTRN select * from spldata.MR_PRTRN where pr_docno in (select pa_crdno from spltest.mr_patrn where pa_crdtp = '13');
     insert into spltest.MR_PRTRN select * from spldata.MR_PRTRN where pr_chqdt between LP_STRDT and LP_ENDDT and pr_docno not in (select b.pr_docno from spltest.mr_prtrn b);
     commit;
     insert into spltest.TEMP_CHK values('MR_PRTRN');
     commit;

     drop table spltest.MR_PLTRN;
     commit;
     create table spltest.MR_PLTRN like spldata.MR_PLTRN;
     insert into spltest.MR_PLTRN select * from spldata.MR_PLTRN where pl_doctp +  pl_docno in (select pt_crdtp +  pt_docrf from spltest.MR_PTTRN);
    delete from spltest.mr_pltrn where pl_doctp='21';
commit;
     insert into spltest.MR_PLTRN select * from spldata.MR_PLTRN where pl_doctp='21' and pl_docno  in (select ivt_invno from spltest.mr_ivtrn);
     insert into spltest.MR_PLTRN select * from spldata.MR_PLTRN where pl_doctp = '13' and pl_docno in (select pr_docno from spltest.mr_prtrn);
     commit;
     insert into spltest.TEMP_CHK values('MR_PLTRN');
     commit;
     
   end if;

commit;
end P1;

commit;

call spltest.crtTSTDATA('MR','10/01/2007','11/01/2007');
commit;
;



     drop table spltest.MR_IVTRN;
     commit;
     create table spltest.MR_IVTRN like spldata.MR_IVTRN;
     insert into spltest.MR_IVTRN select * from spldata.MR_IVTRN where IVT_MKTTP +  IVT_INDNO in (select IN_MKTTP +  IN_INDNO from spltest.MR_INMST) ;
     commit;
     insert into spltest.TEMP_CHK values('MR_IVTRN');
     commit;

select IN_MKTTP +  IN_INDNO from spltest.MR_INMST;
select IN_MKTTP +  IN_INDNO from spltest.MR_INMST;

;
;

   if LP_SYSNM = 'FG' then
     drop table spltest.FG_RCTRN;
     commit;
     create table spltest.FG_RCTRN like spldata.FG_RCTRN;
     insert into spltest.FG_RCTRN select * from spldata.FG_RCTRN;
     commit;


     drop table spltest.FG_ISTRN;
     commit;
     create table spltest.FG_ISTRN like spldata.FG_ISTRN;
     insert into spltest.FG_ISTRN select * from spldata.FG_ISTRN;
     commit;


     drop table spltest.FG_IVTR1;
     commit;
     create table spltest.FG_IVTR1 like spldata.FG_IVTR1;
     insert into spltest.FG_IVTR1 select * from spldata.FG_IVTR1;
     commit;


     drop table spltest.FG_IVTR2;
     commit;
     create table spltest.FG_IVTR2 like spldata.FG_IVTR2;
     insert into spltest.FG_IVTR2 select * from spldata.FG_IVTR2;
     commit;



     drop table spltest.FG_STMST;
     commit;
     create table spltest.FG_STMST like spldata.FG_STMST;
     insert into spltest.FG_STMST select * from spldata.FG_STMST;
     commit;


     drop table spltest.FG_OPSTK;
     commit;
     create table spltest.FG_OPSTK like spldata.FG_OPSTK;
     insert into spltest.FG_OPSTK select * from spldata.FG_OPSTK;
     commit;


     drop table spltest.PR_LTMST;
     commit;
     create table spltest.PR_LTMST like spldata.PR_LTMST;
     insert into spltest.PR_LTMST select * from spldata.PR_LTMST;
     commit;

     drop table spltest.FG_LCMST;
     commit;
     create table spltest.FG_LCMST like spldata.FG_LCMST;
     insert into spltest.FG_LCMST select * from spldata.FG_LCMST;
     commit;

     drop table spltest.CO_PRMST;
     commit;
     create table spltest.CO_PRMST like spldata.CO_PRMST;
     insert into spltest.CO_PRMST select * from spldata.CO_PRMST;
     commit;


     drop table spltest.MR_IVTRN;
     commit;
     create table spltest.MR_IVTRN like spldata.MR_IVTRN;
     insert into spltest.MR_IVTRN select * from spldata.MR_IVTRN;
     commit;

     drop table spltest.MR_IVBAK;
     commit;
     create table spltest.MR_IVBAK like spldata.MR_IVBAK;
     insert into spltest.MR_IVBAK select * from spldata.MR_IVBAK;
     commit;

     drop table spltest.EX_DATFL;
     commit;
     create table spltest.EX_DATFL like spldata.EX_DATFL;
     insert into spltest.EX_DATFL select * from spldata.EX_DATFL;
     commit;


     delete from spltest.co_cdtrn where SUBSTRING(cmt_cgstp,1,2)='FG';
     commit;
     insert into spltest.CO_CDTRN select * from spldata.CO_CDTRN where SUBSTRING(cmt_cgstp,1,2) = 'FG';
     commit;
   end if;

 if LP_SYSNM = 'MM' then
     drop table spltest.MM_WBTRN;
     commit;
     create table spltest.MM_WBTRN like spldata.MM_WBTRN;
     insert into spltest.MM_WBTRN select * from spldata.MM_WBTRN;
     commit;


     drop table spltest.MM_STMST;
     commit;
     create table spltest.MM_STMST like spldata.MM_STMST;
     insert into spltest.MM_STMST select * from spldata.MM_STMST;
     commit;

     drop table spltest.MM_STPRC;
     commit;
     create table spltest.MM_STPRC like spldata.MM_STPRC;
     insert into spltest.MM_STPRC select * from spldata.MM_STPRC;
     commit;

     drop table spltest.MM_YRPRC;
     commit;
     create table spltest.MM_YRPRC like spldata.MM_YRPRC;
     insert into spltest.MM_YRPRC select * from spldata.MM_YRPRC;
     commit;

     drop table spltest.MM_PSLYR;
     commit;
     create table spltest.MM_PSLYR like spldata.MM_PSLYR;
     insert into spltest.MM_PSLYR select * from spldata.MM_PSLYR;
     commit;

     drop table spltest.MM_ISMST;
     commit;
     create table spltest.MM_ISMST like spldata.MM_ISMST;
     insert into spltest.MM_ISMST select * from spldata.MM_ISMST;
     commit;

     drop table spltest.MM_MRMST;
     commit;
     create table spltest.MM_MRMST like spldata.MM_MRMST;
     insert into spltest.MM_MRMST select * from spldata.MM_MRMST;
     commit;

     drop table spltest.MM_GRMST;
     commit;
     create table spltest.MM_GRMST like spldata.MM_GRMST;
     insert into spltest.MM_GRMST select * from spldata.MM_GRMST;
     commit;

     drop table spltest.MM_INMST;
     commit;
     create table spltest.MM_INMST like spldata.MM_INMST;
     insert into spltest.MM_INMST select * from spldata.MM_INMST;
     commit;

     drop table spltest.MM_SAMST;
     commit;
     create table spltest.MM_SAMST like spldata.MM_SAMST;
     insert into spltest.MM_SAMST select * from spldata.MM_SAMST;
     commit;

end if;


   if LP_SYSNM = 'HW' then
     drop table spltest.HW_MCMST;
     commit;
     create table spltest.HW_MCMST like spldata.HW_MCMST;
     insert into spltest.HW_MCMST select * from spldata.HW_MCMST;
     commit;

     drop table spltest.HW_PRMST;
     commit;
     create table spltest.HW_PRMST like spldata.HW_PRMST;
     insert into spltest.HW_PRMST select * from spldata.HW_PRMST;
     commit;

     drop table spltest.HW_LCMST;
     commit;
     create table spltest.HW_LCMST like spldata.HW_LCMST;
     insert into spltest.HW_LCMST select * from spldata.HW_LCMST;
     commit;

     drop table spltest.CO_SWMST;
     commit;
     create table spltest.CO_SWMST like spldata.CO_SWMST;
     insert into spltest.CO_SWMST select * from spldata.CO_SWMST;
     commit;

  end if;

   if LP_SYSNM = 'PR' then
     drop table spltest.PR_PRMST;
     commit;
     create table spltest.PR_PRMST like spldata.PR_PRMST;
     insert into spltest.PR_PRMST select * from spldata.PR_PRMST;
     commit;

     drop table spltest.PR_CDWRK;
     commit;
     create table spltest.PR_CDWRK like spldata.PR_CDWRK;
     insert into spltest.PR_CDWRK select * from spldata.PR_CDWRK;
     commit;

     delete from spltest.co_cdtrn where SUBSTRING(cmt_cgstp,1,2)='PR';
     commit;
     insert into spltest.CO_CDTRN select * from spldata.CO_CDTRN where SUBSTRING(cmt_cgstp,1,2) = 'MR';
     commit;

  end if;

   if LP_SYSNM = 'QC' then



     drop table spltest.QC_PSMST;
     commit;
     create table spltest.QC_PSMST like spldata.QC_PSMST;
     insert into spltest.QC_PSMST select * from spldata.QC_PSMST;
     commit;


     drop table spltest.QC_WTTRN;
     commit;
     create table spltest.QC_WTTRN like spldata.QC_WTTRN;
     insert into spltest.QC_WTTRN select * from spldata.QC_WTTRN;
     commit;


     drop table spltest.QC_SPMST;
     commit;
     create table spltest.QC_SPMST like spldata.QC_SPMST;
     insert into spltest.QC_SPMST select * from spldata.QC_SPMST;
     commit;


     drop table spltest.QC_SMTRN;
     commit;
     create table spltest.QC_SMTRN like spldata.QC_SMTRN;
     insert into spltest.QC_SMTRN select * from spldata.QC_SMTRN;
     commit;


     drop table spltest.QC_RSMST;
     commit;
     create table spltest.QC_RSMST like spldata.QC_RSMST;
     insert into spltest.QC_RSMST select * from spldata.QC_RSMST;
     commit;


     drop table spltest.QC_RMMST;
     commit;
     create table spltest.QC_RMMST like spldata.QC_RMMST;
     insert into spltest.QC_RMMST select * from spldata.QC_RMMST;
     commit;


     drop table spltest.QC_QTTRN;
     commit;
     create table spltest.QC_QTTRN like spldata.QC_QTTRN;
     insert into spltest.QC_QTTRN select * from spldata.QC_QTTRN;
     commit;

  end if;




