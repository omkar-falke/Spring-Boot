
-- Realisation balance Qty. going negative due to Cust.Order ammendments / cancellations after despatch operation
select int_cmpcd,int_indno,in_bkgdt,int_prdds,int_pkgtp,int_reqqt,int_indqt,ivt_invqt,int_lusby,int_lupdt  from spldata.vw_intrn,spldata.mr_ivtrn  where int_stsfl in ('0','X') and int_cmpcd = ivt_cmpcd and int_mkttp=ivt_mkttp and int_indno=ivt_indno and int_prdcd=ivt_prdcd and int_pkgtp=ivt_pkgtp and int_cmpcd + int_mkttp + int_indno + int_prdcd + int_pkgtp in (select ivt_cmpcd + ivt_mkttp + ivt_indno + ivt_prdcd + ivt_pkgtp from spldata.mr_ivtrn where ivt_invqt>0) and in_bkgdt >= '04/01/2008';

-- List of records where despatch has exceeded the Ordered Qty.

select int_cmpcd,int_mkttp,int_indno,in_bkgdt,int_prdds,int_pkgtp,int_reqqt,int_indqt,int_lusby,int_lupdt,sum(ivt_invqt) ivt_invqt  from spldata.vw_intrn,spldata.mr_ivtrn  where int_stsfl not  in ('0','X') and int_cmpcd = ivt_cmpcd and int_mkttp=ivt_mkttp and int_indno=ivt_indno and int_prdcd=ivt_prdcd and int_pkgtp=ivt_pkgtp and int_cmpcd + int_mkttp + int_indno + int_prdcd + int_pkgtp in (select ivt_cmpcd + ivt_mkttp + ivt_indno + ivt_prdcd + ivt_pkgtp from spldata.mr_ivtrn where ivt_invqt>0) and in_bkgdt >= '04/01/2008' group by int_cmpcd,int_mkttp,int_indno,in_bkgdt,int_prdds,int_pkgtp,int_reqqt,int_indqt,int_lusby,int_lupdt having int_indqt < sum(ivt_invqt);

