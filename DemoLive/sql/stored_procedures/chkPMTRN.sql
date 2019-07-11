/* Enter one or more SQL statements separated by semicolons */

commit;

select * from spldata.mr_pltrn where pl_cmpcd='01' and pl_doctp='13' and pl_cmpcd + pl_doctp + pl_docno not in (select pr_cmpcd + pr_doctp + pr_docno from spldata.mr_prtrn where pr_cmpcd='01');


-- verifying booking discount credit note quantity with invoice quantity.
select pt_cmpcd,pt_crdtp,pt_prtcd,pt_docrf,pt_docdt,pt_invno,pt_prdcd,pt_pkgtp,pt_invqt,ivt_invqt from spldata.mr_ivtrn,spldata.mr_pttrn where ivt_cmpcd=pt_cmpcd and ivt_mkttp=pt_mkttp and ivt_invno=pt_invno and ivt_prdcd = pt_prdcd and ivt_pkgtp = pt_pkgtp and pt_crdtp = '01' and isnull(pt_invqt,0)<>isnull(ivt_invqt,0) and ivt_saltp not in ('04','05','14','15','16','21');

select pt_cmpcd,pt_crdtp,pt_prtcd,pt_docrf,pt_docdt,pt_invno,pt_prdcd,pt_pkgtp,pt_invqt,ivt_invqt from spldata.mr_ivtrn,spldata.mr_pttrn where ivt_cmpcd = pt_cmpcd and ivt_mkttp=pt_mkttp and ivt_invno=pt_invno and ivt_prdcd = pt_prdcd and ivt_pkgtp = pt_pkgtp and pt_crdtp = '02' and isnull(pt_invqt,0)<>isnull(ivt_invqt,0)  and ivt_saltp not in ('04','05','14','15','16','21');

select pt_cmpcd,pt_crdtp,pt_prtcd,pt_docrf,pt_docdt,pt_invno,pt_prdcd,pt_pkgtp,pt_invqt,ivt_invqt from spldata.mr_ivtrn,spldata.mr_pttrn where ivt_cmpcd = pt_cmpcd and ivt_mkttp=pt_mkttp and ivt_invno=pt_invno and ivt_prdcd = pt_prdcd and ivt_pkgtp = pt_pkgtp and pt_crdtp = '03' and isnull(pt_invqt,0)<>isnull(ivt_invqt,0)  and ivt_saltp not in ('04','05','14','15','16','21');


-- Missing Invoices in Booking Discount (updPTTRN_INV)
select distinct ivt_cmpcd,ivt_mkttp,ivt_invno,ivt_invdt from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101) > '03/31/2009' and ivt_mkttp in ('01','04','05')  and ivt_stsfl<>'X' and ivt_saltp not in ('04','05','14','15','16','21') and SUBSTRING(ivt_invno,2,1) <> '5'  and isnull(ivt_cc1vl,0)>0 and ivt_cmpcd + ivt_invno not in (select pt_cmpcd + pt_invno from spldata.mr_pttrn where pt_crdtp='01' and pt_docdt > '03/31/2009') order by ivt_cmpcd,ivt_mkttp,ivt_invno;


-- Missing Invoices in Party Ledger (updPLTRN_INV)
select distinct ivt_cmpcd,ivt_mkttp,ivt_invno,ivt_invdt from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101) > '03/31/2009' and ivt_mkttp in ('01','04','05')  and ivt_stsfl<>'X' and ivt_saltp not in ('04','05','14','15','16','21') and SUBSTRING(ivt_invno,2,1) <> '5'  and ivt_cmpcd + ivt_invno not in (select pl_cmpcd + pl_docno from spldata.mr_pltrn where pl_doctp='21') order by ivt_cmpcd,ivt_mkttp,ivt_invno;


-- **********Query to be checked for validity
-- Ledger records missing in MR_PTTRN
select pl_prttp,pl_prtcd,pl_cmpcd,pl_docno,pl_doctp,pl_docdt,pl_docvl from spldata.mr_pltrn where pl_docdt > '03/31/2009' and pl_mkttp  in ('01','04','05')  and SUBSTRING(pl_doctp,1,1) in ('0','2')  and pl_cmpcd + pl_docno not in (select pt_cmpcd + pt_docrf from spldata.mr_pttrn where pt_mkttp in ('01','04','05'))  and pl_cmpcd + pl_doctp + pl_docno not in (select ivt_cmpcd + '21' + ivt_invno from spldata.mr_ivtrn where (isnull(ivt_cc1vl,0)+isnull(ivt_cc2vl,0)+isnull(ivt_cc3vl,0) = 0 or SUBSTRING(ivt_indno,2,2) = 'DR') and ivt_mkttp in ('01','04','05')  and ivt_saltp not in ('04','05','14','15','16','21') and CONVERT(varchar,ivt_invdt,101)> '03/31/2009') order by pl_cmpcd,pl_docno;


-- Customer Booking discount getting recorded against party other than Buyer
select * from spldata.mr_pttrn a where a.pt_prtcd  not in (select b.ivt_byrcd from spldata.mr_ivtrn b where a.pt_cmpcd = b.ivt_cmpcd and a.pt_invno = b.ivt_invno  and b.ivt_saltp not in ('04','05','14','15','16','21')) and a.pt_crdtp='01';

-- Distributor Booking discount getting recorded against party other than Distributor
select * from spldata.mr_pttrn a where a.pt_prtcd  not in (select b.ivt_dsrcd from spldata.mr_ivtrn b where a.pt_cmpcd = b.ivt_cmpcd and a.pt_invno = b.ivt_invno and b.ivt_saltp not in ('04','05','14','15','16','21')) and a.pt_crdtp='02';
-- Distributor Booking discount going for Stock Transfer, FTS , Captive Sale etc.
select * from spldata.mr_pttrn a where a.pt_crdtp='02' and pt_cmpcd + pt_invno in (select  ivt_cmpcd + ivt_invno from spldata.mr_ivtrn where  ivt_saltp  in ('04','05','14','15','16','21'));
-- Distributor Booking discount going for Direct Zone .
select * from spldata.mr_pttrn a where a.pt_crdtp='02' and pt_cmpcd + pt_invno in (select  ivt_cmpcd + ivt_invno from spldata.mr_ivtrn where  SUBSTRING(ivt_zoncd,2,4)='8888');



-- Missing Other Credit / Debit Notes

select PT_CMPCD, PT_MKTTP,PT_CRDTP,PT_PRTTP, PT_PRTCD, PT_DOCRF,PT_DOCDT,sum(PT_PNTVL) PT_PNTVL from spldata.mr_pttrn where SUBSTRING(pt_docrf,2,2)  in ('09','31')  and pt_docdt > '03/31/2009' and pt_docrf <> '00000000' and pt_cmpcd + pt_docrf not in (select pl_cmpcd + pl_docno from spldata.mr_pltrn where SUBSTRING(pl_docno,2,2) in ('09','31') ) and pt_docrf like '9%'  group by PT_CMPCD, PT_MKTTP,PT_CRDTP,PT_PRTTP, PT_PRTCD, PT_DOCRF,PT_DOCDT order by pt_cmpcd asc.pt_docdt desc;


-- Missing Sales return Credit Note
select pt_cmpcd, pt_invno,pt_prttp,pt_prtcd from spldata.mr_pttrn where SUBSTRING(pt_docrf,2,2) = '04'  and pt_docdt > '03/31/2009' and pt_docrf <> '00000000' and pt_cmpcd + pt_docrf not in (select pl_cmpcd + pl_docno from spldata.mr_pltrn where SUBSTRING(pl_docno,2,2)  ='04' ) ;

-- Records with Receipt Qty. and Sales Return Credit quantity mismatches
select  mr_pttrn.pt_cmpcd,mr_pttrn.pt_docrf,mr_pttrn.pt_docdt,mr_pttrn.pt_prtcd,pt_prtnm,rct_issrf,lt_prdcd,pr_prdds,pt_invqt,sum(rct_rctqt) rct_rctqt from spldata.fg_rctrn, spldata.pr_ltmst, spldata.mr_pttrn mr_pttrn, spldata.co_ptmst co_ptmst,spldata.co_prmst where rct_rctdt > '03/31/2009' and rct_rcttp='30' and rct_cmpcd + rct_prdtp + rct_lotno + rct_rclno = lt_cmpcd + lt_prdtp + lt_lotno + lt_rclno and rct_cmpcd + rct_issrf + lt_prdcd=pt_cmpcd + pt_invno + pt_prdcd and rct_rctdt=pt_docdt and pt_crdtp='04'  and co_ptmst.pt_prtcd=mr_pttrn.pt_prtcd and co_ptmst.pt_prttp='C' and pt_prdcd=pr_prdcd group by mr_pttrn.pt_cmpcd,mr_pttrn.pt_docrf,mr_pttrn.pt_docdt,mr_pttrn.pt_prtcd,pt_prtnm,rct_issrf,lt_prdcd,pr_prdds,pt_invqt  having pt_invqt<>sum(rct_rctqt);


-- Missing Other Debit Notes
select pt_cmpcd,  pt_docrf, pt_prttp, pt_prtcd from spldata.mr_pttrn where SUBSTRING(pt_docrf,2,2) = '39' and pt_docdt > '06/30/2006' and pt_docrf <> '00000000' and pt_cmpcd + pt_docrf not in (select pl_cmpcd + pl_docno from spldata.mr_pltrn where SUBSTRING(pl_docno,2,2) = '39' ) ;

-- Overall (all categories) verification
select pt_cmpcd, pt_crdtp,  pt_prttp, pt_prtcd, pt_docrf,pt_invno,pt_docdt,sum(pt_pntvl) pt_pntvl from spldata.mr_pttrn where SUBSTRING(pt_docrf,2,2) in ('01','02','03','04','09','31''32','39','41','42') and pt_docdt > '03/31/2009' and pt_docrf <> '00000000' and pt_cmpcd + pt_docrf not in (select pl_cmpcd + pl_docno from spldata.mr_pltrn where SUBSTRING(pl_docno,2,2)  in ('01','02','03','04','09','31''32','39','41','42') ) group by pt_cmpcd, pt_crdtp, pt_crdtp, pt_prttp, pt_prtcd, pt_docrf,pt_invno,pt_docdt;

-- Missing records in MR_PTTRN for Customer booking discount
select ivt_cmpcd,ivt_mkttp,ivt_invno,ivt_byrcd,ivt_invdt,ivt_cc1vl,ivt_cc1rf from spldata.mr_ivtrn where ivt_mkttp in ('01','04','05')  and ivt_saltp not in ('04','05','14','15','16','21') and CONVERT(varchar,ivt_invdt,101) > '03/31/2009'  and isnull(ivt_cc1vl,0) > 0 and length(ivt_cc1rf)=6 and ivt_cmpcd + ivt_invno not in (select pt_cmpcd + pt_invno from spldata.mr_pttrn where pt_crdtp='01');


-- Missing records in MR_PTTRN for Distributor  booking discount
select ivt_cmpcd,ivt_mkttp,ivt_invno,ivt_byrcd,ivt_invdt,ivt_cc2vl,ivt_cc2rf  from spldata.mr_ivtrn where ivt_mkttp in ('01','04','05')  and ivt_saltp not in ('04','05','14','15','16','21') and CONVERT(varchar,ivt_invdt,101) > '03/31/2009'  and isnull(ivt_cc2vl,0) > 0 and length(ivt_cc2rf)=6  and SUBSTRING(ivt_cc2rf,3,4) <> '8888' and ivt_cmpcd + ivt_invno not in (select pt_cmpcd + pt_invno from spldata.mr_pttrn where pt_crdtp='02');



-- Missing records in MR_PTTRN for Distributor  commission record
select ivt_cmpcd,ivt_mkttp,ivt_invno,ivt_byrcd,ivt_invdt,ivt_comvl,ivt_dsrcd  from spldata.mr_ivtrn where ivt_mkttp in ('01','04','05') and CONVERT(varchar,ivt_invdt,101) > '03/31/2009'  and isnull(ivt_comvl,0) > 0  and SUBSTRING(ivt_dsrcd,2,4)<>'8888'  and ivt_saltp not in ('04','05','14','15','16','21') and SUBSTRING(ivt_prdcd,1,2) not in ('52','54') and ivt_cmpcd + ivt_invno not in (select pt_cmpcd + pt_invno from spldata.mr_pttrn where pt_crdtp='03');



-- Missing records in MR_PTTRN for Sales Return transactions
select rct_cmpcd,rct_rcttp, rct_rctno,rct_issrf,rct_rctdt,rct_lotno from spldata.fg_rctrn where rct_rctdt > '03/31/2009' and rct_rcttp = '30' and length(rct_issrf) = 8 and rct_cmpcd + rct_issrf not in (select pt_cmpcd + pt_invno from spldata.mr_pttrn where pt_crdtp='04') and rct_cmpcd + rct_issrf not in (select ivt_cmpcd + ivt_invno from spldata.mr_ivtrn where ivt_saltp='05');





-- Missing records in MR_PLTRN for Credit / Debit Transactions
select pt_prttp,pt_prtcd,pt_cmpcd,pt_crdtp,pt_docrf,pt_docdt,pt_mkttp,pt_invno from spldata.mr_pttrn  where pt_docdt > '03/31/2009' and pt_docrf <> '00000000' and pt_cmpcd + pt_docrf not in (select pl_cmpcd + pl_docno from spldata.mr_pltrn where SUBSTRING(pl_doctp,1,1) in ('0','3'));




-- Invoice / Debit note records in Party Ledger with adjustment amount mismatch
select pl_prttp,pl_prtcd,pl_cmpcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl from spldata.mr_pltrn, spldata.mr_patrn where pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_cmpcd = pa_cmpcd and pl_docno = pa_dbtno and isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_cmpcd,pl_docno, pl_adjvl  having abs(pl_adjvl -sum(isnull(pa_adjvl,0)))>1;



-- Payment receipt / Credit Note records in Party Ledger with adjustment amount mismatch
select pl_prttp,pl_prtcd,pl_cmpcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl from spldata.mr_pltrn, spldata.mr_patrn where pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and and pl_cmpcd = pa_cmpcd and pl_docno = pa_crdno and isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_cmpcd,pl_docno, pl_adjvl  having abs(pl_adjvl -sum(isnull(pa_adjvl,0)))>2;

-- Extra Debit Adjustment values (records) in MR_PLTRN
select pl_prttp,pl_prtcd,pl_cmpcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl from spldata.mr_pltrn where pl_adjvl>0 and pl_prttp + pl_prtcd + pl_cmpcd + pl_docno not in (select pa_prttp + pa_prtcd + pa_cmpcd + pa_dbtno from spldata.mr_patrn) and SUBSTRING(pl_doctp,1,1) in ('2','3');


-- Extra Credit Adjustment values (records) in MR_PLTRN
select pl_prttp,pl_prtcd,pl_cmpcd,pl_doctp,pl_docno, pl_docvl, isnull(pl_adjvl,0) pl_adjvl from spldata.mr_pltrn where pl_docdt > '03/31/2009' and isnull(pl_adjvl,0)>0 and pl_prttp + pl_prtcd + pl_cmpcd + pl_docno not in (select pa_prttp + pa_prtcd + pa_cmpcd + pa_crdno from spldata.mr_patrn) and SUBSTRING(pl_doctp,1,1) in ('0','1');



-- Records in MR_PTTRN where Tax Calculation is not as per predefined rates (TDS, Cess, Surcharge etc.)
select pt_cmpcd,pt_crdtp,pt_invno,pt_docrf,pt_ltxvl, round( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)  +( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)) *0.02 ,2)  from spldata.mr_pttrn where abs(pt_ltxvl - round( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)  +( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)) *0.02 ,2) )>0.01 and pt_ltxvl>0;
select pt_crdtp,pt_docrf,pt_atxvl, round((pt_pgrvl*0.12)+((pt_pgrvl*0.12)*0.02),2)   from spldata.mr_pttrn where pt_atxvl <> round((pt_pgrvl*0.12)+((pt_pgrvl*0.12)*0.02),2)  and pt_docrf in (select tx_docno from spldata.co_txdoc where tx_doctp='CRA') order by pt_docrf;



-- Records in MR_PTTRN where Tax Record is found in CO_TXDOC but tax calculation has not taken place
select pt_cmpcd,pt_crdtp,pt_docrf,pt_ltxvl, round( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)  +( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)) *0.02 ,2)  from spldata.mr_pttrn where pt_ltxvl=0 and pt_docrf in (select tx_docno from spldata.co_txdoc where tx_doctp='CRD') order by pt_cmpcd,pt_docrf;


-- Records in MR_PTTRN where Tax Record is found in CO_TXDOC but tax calculation has not taken place
select pt_cmpcd,pt_crdtp,pt_docrf,pt_atxvl, round((pt_pgrvl*0.12)+((pt_pgrvl*0.12)*0.02),2)   from spldata.mr_pttrn where pt_atxvl=0 and pt_cmpcd + pt_docrf in (select tx_cmpcd + tx_docno from spldata.co_txdoc where tx_doctp='CRA') order by pt_cmpcd,pt_docrf;



-- Records with repeating sequence number in MR_PLTRN;

select pl_cmpcd,pl_prttp,pl_prtcd,pl_seqno,count(*) from spldata.mr_pltrn  where pl_docdt>'03/31/2009'  and pl_stsfl <> 'X' group by pl_cmpcd,pl_prttp,pl_prtcd,pl_seqno having count(*)>1;


-- *****CHECKING INVOICE VALUE IN LEDGER WITH ACTUAL INVOICE  ***
SELECT DISTINCT PL_PRTCD,ivt_cmpcd,IVT_BYRCD,IVT_CNSCD,PL_DOCNO,ivt_mkttp,IVT_INVNO,ivt_invdt,ivt_ladno,IVT_STSFL,PL_DOCVL,IVT_INVVL FROM SPLDATA.MR_PLTRN,SPLDATA.MR_IVTRN WHERE  ivt_saltp not in ('04','05','14','15','16','21') and ivt_cmpcd=pl_cmpcd and PL_DOCTP='21' and PL_DOCNO =IVT_INVNO  AND PL_DOCVL <> ROUND(IVT_INVVL,0) and CONVERT(varchar,ivt_invdt,101)>'03/31/2009';
select * from spldata.mr_ivtrn where ivt_netvl>0 and ivt_invvl=0 and CONVERT(varchar,ivt_invdt,101)>'06/30/2006';


select * from spldata.co_cdtrn where cmt_cgstp='MR00SAL';
