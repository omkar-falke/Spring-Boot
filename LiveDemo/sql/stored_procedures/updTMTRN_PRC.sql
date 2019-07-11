

drop procedure spltest.updTRTMN_PRC;
commit;

create procedure spltest.updTRTMN_PRC(IN LP_RUNDT date, IN LP_)  language sql  modifies sql data
 P1:
  begin
     declare L_mmydt char(4); 
     declare L_prdcd char(10); 
     declare L_pkgtp char(2); 

     declare l_pkgtp char(2);
     declare l_prdcd char(10);
     declare l_prdds char(15);
     declare l_zoncd char(2);
     declare l_saltp cahr(2);
     declare l_docqt decimal(10,2);
     declare l_logdt date;


     declare C_TMTRN CURSOR FOR  select * from spltest.mr_tmtrn where tm_mmydt = L_MMYDT and tm_prdcd = L_PRDCD and tm_pkgtp = L_PKGTP;
     declare C_ORDQT CURSOR FOR  select int_pkgtp,int_prdcd,int_prdds,in_zoncd,in_saltp,sum(isnull(int_indqt,0)) l_indqt from mr_intrn,mr_inmst where int_mkttp=in_mkttp and int_indno=in_indno  and in_bkgdt <=  L_logdt  and in_stsfl != 'X' and in_saltp in ('01','12','03') and in_mkttp='01' group by int_prdcd,int_pkgtp,in_zoncd,in_saltp,int_prdds;

declare continue handler for not found 
          set END_TABLE = 1; 

open C_ORDQT;

select distinct current_date into l_logdt from spltest.mr_tmtrn;
fetch C_ORDQT into l_pkgtp, l_prdcd, l_prdds, l_zoncd, l_saltp, l_docqt;

while END_TABLE = 0 DO 
    select cmt_chp01 into l_chpzn from spltest.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codcd = l_zoncd;
    select cmt_chp01 into l_chpsl from spltest.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd = l_salcd;
    select pr_stsfl into l_stsfl from spltest.co_prmst where pr_prdcd=l_prdcd;
   select count(*) into l_recct from spltest.mr_tmtrn where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;

if l_recct>0 then
   if upper(l_chpzn) + upper(l_chpsl) = 'EZDO' then
          update spltest.mr_tmtrn set tm_ezdoo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'EZDE' then
          update spltest.mr_tmtrn set tm_ezdeo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'WZDO' then
          update spltest.mr_tmtrn set tm_wzdoo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'WZDE' then
          update spltest.mr_tmtrn set tm_wzdeo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'NZDO' then
          update spltest.mr_tmtrn set tm_nzdoo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'NZDE' then
          update spltest.mr_tmtrn set tm_nzdeo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'SZDO' then
          update spltest.mr_tmtrn set tm_szdoo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'SZDE' then
          update spltest.mr_tmtrn set tm_szdeo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'CZDO' then
          update spltest.mr_tmtrn set tm_czdoo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if if upper(l_chpzn) + upper(l_chpsl) = 'CZDE' then
          update spltest.mr_tmtrn set tm_czdeo = l_docqt where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   update spltest.mr_tmtrn set tm_lusby = 'SYS', tm_lupdt = current_date, tm_stsfl=l_stsfl, 
else
   if upper(l_chpzn) + upper(l_chpsl) = 'EZDO' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_ezdoo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'EZDE' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_ezdeo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'WZDO' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_wzdoo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'WZDE' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_wzdeo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'NZDO' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_nzdoo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'NZDE' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_nzdeo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'SZDO' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_szdoo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'SZDE' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_szdeo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'CZDO' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_czdoo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
   else if upper(l_chpzn) + upper(l_chpsl) = 'CZDE' then
          insert into spltest.mr_tmtrn (tm_mmydt, tm_prdcd, tm_pkgtp, tm_prdds, tm_czdeo,tm_lusby,tm_lupdt,tm_stsfl) values (lp_mmydt, l_prdcd, l_pkgtp, l_prdds,  l_docqt,'SYS' ,current_date,l_stsfl)  where tm_mmydt = lp_mmydt and tm_prdcd=l_prdcd and tm_pkgtp = l_pkgtp;
end if;
end if;

end while;
close C_ORDQT; 
close C_ORDQT; 
commit;
end P1;
commit;


