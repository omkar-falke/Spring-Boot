drop procedure spldata.setINVQT_PRC;
commit;


create procedure spldata.setINVQT_PRC(IN LP_CMPCD char(2))  language sql  modifies sql data 
 P1:
 begin

 declare L_DOT_MKTTP char(2); 
 declare L_DOT_DORNO char(9); 
 declare L_DOT_PRDCD char(10); 
 declare L_DOT_PKGTP char(2); 
 declare L_DOT_DORQT decimal(10,3); 
 declare L_DOT_INVQT decimal(10,3); 
 declare L_IVT_INVQT decimal(10,3); 


declare END_TABLE int default 0;


declare C_DOTRN CURSOR FOR  select dot_mkttp,dot_dorno,dot_prdcd,dot_pkgtp,dot_dorqt,dot_invqt,sum(ivt_invqt)  ivt_invqt from spldata.mr_dotrn left outer join spldata.mr_ivtrn on dot_cmpcd=ivt_cmpcd and  dot_mkttp=ivt_mkttp and dot_dorno=ivt_dorno and dot_prdcd=ivt_prdcd and dot_pkgtp = ivt_pkgtp and ivt_stsfl<>'X' where dot_cmpcd = lp_cmpcd and dot_dorqt>0 and dot_stsfl<>'X' group by dot_mkttp,dot_dorno,dot_prdcd,dot_pkgtp,dot_dorqt,dot_invqt having dot_invqt<>sum(ivt_invqt);



declare continue handler for not found 
       set END_TABLE = 1; 

open C_DOTRN;
fetch C_DOTRN into l_dot_mkttp,l_dot_dorno,l_dot_prdcd,l_dot_pkgtp,l_dot_dorqt,l_dot_invqt,l_ivt_invqt;

while END_TABLE = 0 DO 
       update spldata.mr_dotrn set dot_invqt = l_ivt_invqt where dot_cmpcd = lp_cmpcd and dot_mkttp= l_dot_mkttp and dot_dorno = l_dot_dorno and dot_prdcd = l_dot_prdcd and dot_pkgtp = l_dot_pkgtp;
       fetch C_DOTRN into l_dot_mkttp,l_dot_dorno,l_dot_prdcd,l_dot_pkgtp,l_dot_dorqt,l_dot_invqt,l_ivt_invqt;
end while;
close C_DOTRN; 

commit;
end P1;

commit;

call spldata.setDOTRN_PRC();
commit;

