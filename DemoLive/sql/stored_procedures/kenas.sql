select int_stsfl from spldata.mr_intrn where int_ocfno='80100688';
select pt_prtnm from spldata.co_ptmst where pt_prttp='C' and pt_prtcd='S0887';
update spldata.mr_ocmst set oc_byrcd='S0887',oc_cnscd='S0887',oc_byrnm='SIRAP GEMA SPA',oc_cnsnm='SIRAP GEMA SPA'  where  oc_ocfno='80100688';
update spldata.mr_inmst set in_ocfno='' where in_ocfno='80100688';
update spldata.mr_intrn set int_ocfno='' where int_ocfno='80100688';
commit;
