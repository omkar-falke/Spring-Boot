
create table spldata.xx_stmst like spldata.fg_stmst;
commit;
insert into spldata.xx_stmst select * from spldata.fg_stmst;
commit;

select ptf_lotno,ptf_prdcd,ptf_pkgtp,ptf_ptfqt from spldata.fg_ptfrf where ptf_ptfdt='05/26/2008' and ptf_lotno in (select lk_lotno from spldata.fg_lktrn) order by ptf_lotno;

select rct_lotno,rct_mnlcd,rct_pkgtp,rct_rctqt from spldata.fg_rctrn where rct_rctdt='05/26/2008' and rct_rcttp in ('10','15') and rct_lotno + rct_mnlcd + rct_pkgtp in (select lk_lotno + lk_mnlcd + lk_pkgtp from spldata.fg_lktrn) order by rct_lotno;

select st_lotno,st_mnlcd,st_pkgtp,st_dosqt,st_douqt,st_stkqt,st_uclqt from spldata.fg_stmst where st_lotno + st_mnlcd + st_pkgtp in  (select lk_lotno + lk_mnlcd + lk_pkgtp from spldata.fg_lktrn) order by st_lotno;

select ist_lotno,ist_mnlcd,ist_pkgtp,ist_issqt from spldata.fg_istrn where ist_issdt = '05/26/2008' and ist_lotno + ist_mnlcd + ist_pkgtp  in  (select lk_lotno + lk_mnlcd + lk_pkgtp from spldata.fg_lktrn)  order by ist_lotno;

select lk_lotno,lk_mnlcd,lk_pkgtp,lk_dosqt,lk_douqt ,lk_rctqt,lk_issqt,lk_stkqt,lk_uclqt from spldata.fg_lktrn order by lk_lotno;

