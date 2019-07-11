select * from spldata.mr_ivtrn  where ivt_cmpcd='01' and date(ivt_laddt) between '04/01/2009' and date(days(current_date)-1) and ivt_invqt<ivt_reqqt and ivt_reqqt>0;

update spldata.mr_ivtrn set ivt_reqqt=ivt_invqt where ivt_cmpcd='01' and date(ivt_laddt) between '04/01/2009' and date(days(current_date)-1) and ivt_invqt<ivt_reqqt and ivt_reqqt>0;

update spldata.mr_ivtrn set ivt_stsfl='X' where ivt_cmpcd='01' and date(ivt_laddt) between '04/01/2009' and date(days(current_date)-1) and ivt_reqqt=0 and ivt_stsfl<>'X';
commit;
