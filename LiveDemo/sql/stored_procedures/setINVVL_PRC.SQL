drop procedure spltest.setINVVL_PRC;
commit;


create procedure spltest.setINVVL_PRC(IN LP_MKTTP char(2), IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 

 P1:

 begin

 declare l_ivt_mkttp char(2);

 declare l_ivt_invno char(8);

 declare l_ivt_prdcd char(10);

 declare l_ivt_pkgtp char(2);

 declare l_tx_excvl decimal(10,2);

 declare l_tx_edcvl decimal(10,2); 

 declare l_tx_ehcvl decimal(10,2); 

 declare l_ivt_invvl decimal(12,2); 

 declare l_ivt_mkttp_old char(2);

 declare l_ivt_invno_old char(8);

 declare l_ivt_itxrt decimal(6,3);

 declare END_TABLE  int  default 0; 



declare C_TXDOC CURSOR FOR  select ivt_mkttp,ivt_invno,ivt_prdcd,ivt_pkgtp, isnull(tx_excvl,0) tx_excvl,isnull(tx_edcvl,0),isnull(tx_ehcvl,0) tx_ehcvl  from spltest.mr_ivtrn left outer join spltest.co_txdoc on tx_syscd='MR' and tx_sbstp='01' and tx_doctp='IND' and tx_prdcd='XXXXXXXXXX' and tx_docno=ivt_indno  where CONVERT(varchar,ivt_invdt,101) between LP_STRDT and LP_ENDDT  and ivt_mkttp=LP_MKTTP  and isnull(ivt_invqt,0)>0 order by ivt_mkttp,ivt_invno,ivt_prdcd,ivt_pkgtp;





declare C_INVVL CURSOR FOR  select ivt_mkttp,ivt_invno,sum(ivt_netvl) ivt_netvl  from spltest.mr_ivtrn  where CONVERT(varchar,ivt_invdt,101) between LP_STRDT and LP_ENDDT  and ivt_mkttp=LP_MKTTP  and isnull(ivt_invqt,0)>0 group by  ivt_mkttp,ivt_invno order by ivt_mkttp,ivt_invno;





declare continue handler for not found 

       set END_TABLE = 1; 





open C_TXDOC;

fetch C_TXDOC into l_ivt_mkttp,l_ivt_invno,l_ivt_prdcd,l_ivt_pkgtp, l_tx_excvl,l_tx_edcvl,l_tx_ehcvl;



set l_ivt_mkttp_old = l_ivt_mkttp,l_ivt_invno_old = l_ivt_invno;



while END_TABLE = 0 DO 

   if l_ivt_mkttp + l_ivt_invno <> l_ivt_mkttp_old + l_ivt_invno_old then

        select sum(round(isnull(ivt_assvl,0)+isnull(ivt_excvl,0)+isnull(ivt_edcvl,0)+isnull(ivt_ehcvl,0),0)) into l_ivt_invvl from spltest.mr_ivtrn where ivt_mkttp=l_ivt_mkttp_old and ivt_invno = l_ivt_invno_old and ivt_stsfl<>'X' and isnull(ivt_invqt,0)>0;

        update spltest.mr_ivtrn set ivt_invvl = l_ivt_invvl where ivt_mkttp=l_ivt_mkttp_old and ivt_invno = l_ivt_invno_old;

        set l_ivt_mkttp_old = l_ivt_mkttp,l_ivt_invno_old = l_ivt_invno;

   end if;

   set  l_ivt_itxrt = 1.000;

   if SUBSTRING(l_ivt_prdcd,1,6) in ('519895','529895','529795','519795') then

      set l_ivt_itxrt=1.01025;

   end if;

   update spltest.mr_ivtrn set ivt_excrt=l_tx_excvl, ivt_edcrt = l_tx_edcvl, ivt_ehcrt = l_tx_ehcvl, ivt_excvl = round(isnull(ivt_assvl,0)*l_tx_excvl/100,0),  ivt_edcvl  =  round(isnull(ivt_assvl,0)*l_tx_edcvl/100,0),  ivt_ehcvl  =  round(isnull(ivt_assvl,0)*l_tx_ehcvl/100,0),  ivt_netvl = round((isnull(ivt_assvl,0) + round(ivt_assvl*l_tx_excvl/100,0)+round(ivt_assvl*l_tx_edcvl/100,0)+round(ivt_assvl*l_tx_ehcvl/100,0))*l_ivt_itxrt,0)  where ivt_mkttp=l_ivt_mkttp and ivt_invno=l_ivt_invno and ivt_prdcd=l_ivt_prdcd and ivt_pkgtp = l_ivt_pkgtp;

    fetch C_TXDOC into l_ivt_mkttp,l_ivt_invno,l_ivt_prdcd,l_ivt_pkgtp, l_tx_excvl,l_tx_edcvl,l_tx_ehcvl;

end while;

close C_TXDOC; 

commit;



open C_INVVL;

fetch C_INVVL into l_ivt_mkttp,l_ivt_invno,l_ivt_invvl;

set END_TABLE = 0;

while END_TABLE = 0 DO 

        update spltest.mr_ivtrn set ivt_invvl1 = round(l_ivt_invvl,0) where ivt_mkttp=l_ivt_mkttp and ivt_invno = l_ivt_invno;

        fetch C_INVVL into l_ivt_mkttp,l_ivt_invno,l_ivt_invvl;

end while;

close C_INVVL; 

end P1;

commit;
call spltest.setINVVL_PRC('01','07/09/2004','08/05/2004');
commit;

select ivt_invno,CONVERT(varchar,ivt_invdt,101) ivt_invdt,ivt_prdcd,ivt_invvl,ivt_invvl1,ivt_invqt,ivt_invrt,ivt_assvl,ivt_excvl,ivt_edcvl,ivt_ehcvl,ivt_netvl,ivt_excrt,ivt_edcrt,ivt_ehcrt,ivt_saltp from spltest.mr_ivtrn where isnull(ivt_invvl1,0)>0 and CONVERT(varchar,ivt_invdt,101)>'07/01/2004' and ivt_invvl<>ivt_invvl1;




update spltest.mr_ivtrn set ivt_netvl = (isnull(ivt_assvl,0) + round(ivt_assvl*16/100,0)+round(ivt_assvl*0.32/100,0))*1.01025 where ivt_invno='50004148';
