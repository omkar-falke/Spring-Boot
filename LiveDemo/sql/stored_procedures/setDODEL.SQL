drop procedure spldata.setDODEL;
commit;


create procedure spldata.setDODEL(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_INVDT1 char(10), IN LP_INVDT2 char(10))  language sql  modifies sql data 
 P1:
 begin
 declare L_TOT_INVQT decimal(10,3) default 0.000; 
 declare L_TOT_DORQT decimal(10,3) default 0.000; 
 declare L_RUN_INVQT decimal(10,3) default 0.000; 
 declare L_SET_INVQT decimal(10,3) default 0.000; 

 declare L_DOR_MKTTP char(2); 
 declare L_DOR_DORNO char(8); 
 declare L_DOR_PRDCD char(10); 
 declare L_DOR_PKGTP char(2); 
 declare L_DOR_DORQT decimal(10,3); 


 declare L_DOD_MKTTP char(2); 
 declare L_DOD_DORNO char(8); 
 declare L_DOD_PRDCD char(10); 
 declare L_DOD_PKGTP char(2); 
 declare L_DOD_SRLNO char(2); 
 declare L_DOD_DORQT decimal(10,3); 
 declare END_TABLE  int  default 0; 



declare C_DOR CURSOR FOR  select DOT_MKTTP, DOT_DORNO, DOT_PRDCD, DOT_PKGTP, DOT_DORQT from spldata.MR_DOTRN  
 where dot_cmpcd = lp_cmpcd and DOT_MKTTP = LP_MKTTP and DOT_INDNO in (select IVT_INDNO from spldata.mr_ivtrn where ivt_cmpcd = lp_cmpcd and ivt_mkttp=lp_mkttp and  isnull(ivt_invqt,0)>0 and upper(ivt_stsfl) <>'X' and (CONVERT(varchar,ivt_invdt,101)  between LP_INVDT1 and LP_INVDT2) or (ivt_lupdt between LP_INVDT1 and LP_INVDT2))  order by DOT_MKTTP, DOT_DORNO, DOT_PRDCD, DOT_PKGTP; 

declare C_DOD CURSOR FOR  select DOD_MKTTP, DOD_DORNO, DOD_PRDCD, DOD_PKGTP, DOD_SRLNO, DOD_DORQT from spldata.MR_DODEL 
 where dod_cmpcd = lp_cmpcd and DOD_MKTTP + DOD_DORNO in (select DOT_MKTTP + DOT_DORNO from spldata.MR_DOTRN where dot_cmpcd = lp_cmpcd and DOT_MKTTP = LP_MKTTP and DOT_INDNO  in (select IVT_INDNO from spldata.mr_ivtrn where ivt_cmpcd = lp_cmpcd and ivt_mkttp=lp_mkttp and isnull(ivt_invqt,0)>0 and (CONVERT(varchar,ivt_invdt,101)  between LP_INVDT1 and LP_INVDT2) or (ivt_lupdt  between LP_INVDT1 and LP_INVDT2))); 


declare continue handler for not found 
       set END_TABLE = 1; 


set L_RUN_INVQT = 0; 

open C_DOR;
fetch C_DOR into L_DOR_MKTTP, L_DOR_DORNO, L_DOR_PRDCD, L_DOR_PKGTP, L_DOR_DORQT;
open C_DOD;
fetch C_DOD into L_DOD_MKTTP, L_DOD_DORNO, L_DOD_PRDCD, L_DOD_PKGTP, L_DOD_SRLNO, L_DOD_DORQT; 

while END_TABLE = 0 DO 

if (L_DOR_MKTTP + L_DOR_DORNO + L_DOR_PRDCD + L_DOR_PKGTP = L_DOR_MKTTP + L_DOR_DORNO + L_DOR_PRDCD + L_DOR_PKGTP) and END_TABLE = 0 then 
     select sum(IVT_INVQT) into L_TOT_INVQT from spldata.MR_IVTRN where ivt_cmpcd = lp_cmpcd and IVT_MKTTP = L_DOR_MKTTP and IVT_DORNO = L_DOR_DORNO and IVT_PRDCD = L_DOR_PRDCD and  IVT_PKGTP = L_DOR_PKGTP; 
     set L_RUN_INVQT = 0;
    update spldata.MR_DODEL SET DOD_LADQT =  0, dod_stsfl= ' '  where dod_cmpcd = lp_cmpcd and DOD_MKTTP = L_DOR_MKTTP and DOD_DORNO = L_DOR_DORNO and DOD_PRDCD =   L_DOR_PRDCD and DOD_PKGTP = L_DOR_PKGTP; 
     while (L_DOR_MKTTP + L_DOR_DORNO + L_DOR_PRDCD + L_DOR_PKGTP = L_DOR_MKTTP + L_DOR_DORNO + L_DOR_PRDCD + L_DOR_PKGTP) and END_TABLE = 0 DO 
 
           set L_SET_INVQT = (L_TOT_INVQT - L_RUN_INVQT); 
           if L_SET_INVQT > L_DOD_DORQT then 
              set L_SET_INVQT = L_DOD_DORQT; 
           end if; 
           set  L_RUN_INVQT = L_RUN_INVQT + L_SET_INVQT;
           if L_SET_INVQT > 0 then
              update spldata.MR_DODEL SET DOD_LADQT = L_SET_INVQT, dod_stsfl='D'  where dod_cmpcd = lp_cmpcd and DOD_MKTTP = L_DOD_MKTTP and DOD_DORNO = L_DOD_DORNO and DOD_PRDCD =   L_DOD_PRDCD and DOD_PKGTP = L_DOD_PKGTP and DOD_SRLNO = L_DOD_SRLNO; 
           end if;
           fetch C_DOD into L_DOD_MKTTP, L_DOD_DORNO, L_DOD_PRDCD, L_DOD_PKGTP, L_DOD_SRLNO, L_DOD_DORQT; 
     end while;
  end if;
       fetch C_DOR into L_DOR_MKTTP, L_DOR_DORNO, L_DOR_PRDCD, L_DOR_PKGTP,  L_DOR_DORQT; 
end while;
close C_DOR; 
close C_DOD; 
commit;
end P1;

commit;

call spldata.setDODEL('01','05/01/2006','06/30/2006');
commit;

