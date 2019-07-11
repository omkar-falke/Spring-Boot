drop procedure spldata.setINTRN;
commit;


create procedure spldata.setINTRN(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_DOCDT1 char(10), IN LP_DOCDT2 char(10))  language sql  modifies sql data 
 P1:
 begin

 declare L_DOT_MKTTP char(2); 
 declare L_DOT_INDNO char(9); 
 declare L_DOT_PRDCD char(10); 
 declare L_DOT_PKGTP char(2); 
 declare L_DOT_DORQT decimal(10,3); 


 declare L_IVT_MKTTP char(2); 
 declare L_IVT_INDNO char(9); 
 declare L_IVT_DORNO char(8); 
 declare L_IVT_PRDCD char(10); 
 declare L_IVT_PKGTP char(2); 
 declare L_IVT_INVQT decimal(10,3); 
declare END_TABLE int default 0;




declare C_DOTRN CURSOR FOR  select DOT_MKTTP, DOT_INDNO, DOT_PRDCD, DOT_PKGTP, sum(DOT_DORQT) from spldata.MR_DOTRN  
 where DOT_CMPCD = LP_CMPCD and DOT_MKTTP = LP_MKTTP and dot_indno in (select IN_INDNO from spldata.mr_inmst where (IN_BKGDT between LP_DOCDT1 and LP_DOCDT2) or (in_lupdt between LP_DOCDT1 and LP_DOCDT2) and in_stsfl<>'X' and in_cmpcd = LP_CMPCD and in_mkttp=LP_MKTTP) and dot_stsfl<>'X'   group by DOT_MKTTP, DOT_INDNO, DOT_PRDCD, DOT_PKGTP; 

declare C_IVTRN CURSOR FOR  select IVT_MKTTP, IVT_INDNO, IVT_PRDCD, IVT_PKGTP, sum(IVT_INVQT) from spldata.MR_IVTRN 
 where IVT_CMPCD = LP_CMPCD and IVT_MKTTP = LP_MKTTP and IVT_INDNO in  (select in_indno from spldata.mr_inmst where ((in_bkgdt between LP_DOCDT1 and LP_DOCDT2) or (in_lupdt between LP_DOCDT1 and LP_DOCDT2)) and in_stsfl<>'X' ) and ivt_stsfl<>'X'  group by IVT_MKTTP, IVT_INDNO, IVT_PRDCD, IVT_PKGTP;

declare C_IVTRN1 CURSOR FOR  select IVT_MKTTP, IVT_DORNO, IVT_PRDCD, IVT_PKGTP, sum(IVT_INVQT) from spldata.MR_IVTRN 
 where IVT_CMPCD = LP_CMPCD and IVT_MKTTP = LP_MKTTP and ivt_dorno in (select dot_dorno from spldata.mr_dotrn where  ((dot_dordt  between LP_DOCDT1 and LP_DOCDT2) or (dot_lupdt between LP_DOCDT1 and LP_DOCDT2)) and dot_stsfl<>'X') and ivt_stsfl<>'X'  and isnull(ivt_invqt,0)>0  group by IVT_MKTTP, IVT_DORNO, IVT_PRDCD, IVT_PKGTP;


declare continue handler for not found 
       set END_TABLE = 1; 

open C_DOTRN;
fetch C_DOTRN into L_DOT_MKTTP, L_DOT_INDNO, L_DOT_PRDCD, L_DOT_PKGTP, L_DOT_DORQT;

while END_TABLE = 0 DO 
    update spldata.MR_INTRN SET INT_DORQT = L_DOT_DORQT where INT_CMPCD = LP_CMPCD and INT_MKTTP = L_DOT_MKTTP and INT_INDNO = L_DOT_INDNO and INT_PRDCD = L_DOT_PRDCD and INT_PKGTP = L_DOT_PKGTP;
  fetch C_DOTRN into L_DOT_MKTTP, L_DOT_INDNO, L_DOT_PRDCD, L_DOT_PKGTP, L_DOT_DORQT;
end while;
close C_DOTRN; 

set END_TABLE = 0; 

open C_IVTRN;
fetch C_IVTRN into L_IVT_MKTTP, L_IVT_INDNO, L_IVT_PRDCD, L_IVT_PKGTP, L_IVT_INVQT;
while END_TABLE = 0 DO 
    update spldata.MR_INTRN SET INT_INVQT = L_IVT_INVQT where INT_CMPCD = LP_CMPCD and INT_MKTTP = L_IVT_MKTTP and INT_INDNO = L_IVT_INDNO and INT_PRDCD = L_IVT_PRDCD and INT_PKGTP = L_IVT_PKGTP;
fetch C_IVTRN into L_IVT_MKTTP, L_IVT_INDNO, L_IVT_PRDCD, L_IVT_PKGTP, L_IVT_INVQT;
end while;
close C_IVTRN; 


set END_TABLE = 0; 

open C_IVTRN1;
fetch C_IVTRN1 into L_IVT_MKTTP, L_IVT_DORNO, L_IVT_PRDCD, L_IVT_PKGTP, L_IVT_INVQT;
while END_TABLE = 0 DO 
    update spldata.MR_DOTRN SET DOT_INVQT = L_IVT_INVQT, DOT_LADQT = L_IVT_INVQT where DOT_CMPCD = LP_CMPCD and DOT_MKTTP = L_IVT_MKTTP and DOT_DORNO = L_IVT_DORNO and DOT_PRDCD = L_IVT_PRDCD and DOT_PKGTP = L_IVT_PKGTP;
fetch C_IVTRN1 into L_IVT_MKTTP, L_IVT_DORNO, L_IVT_PRDCD, L_IVT_PKGTP, L_IVT_INVQT;
end while;
close C_IVTRN1; 
commit;
end P1;

commit;

call spldata.setINTRN('01','01','01/01/2009','01/22/2009');
call spldata.setINTRN('01','05','01/01/2009','01/22/2009');
commit;


commit;
SELECT IN_MKTTP,IN_BKGDT,INT_INDQT,INt_dorqt,int_invqt,int_prdds,int_indno from spldata.VW_intrn where int_indno in ('WDR70381');
SELECT dot_MKTTP,dot_dorno,dot_indno,dot_prdcd,dot_dorqt,dot_invqt from spldata.mr_dotrn where dot_indno in ('SDR70371','NDR70033','WDR70246');

