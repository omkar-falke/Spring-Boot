-- This procedure reworks Stock on Indentand Stock on Order Qty. for Specified Indent Number

drop procedure spldata.setSTKQT_IND;
commit;


create procedure spldata.setSTKQT_IND(IN LP_STRTP char(2), IN LP_INDNO char(8))  language sql  modifies sql data 
 P1:
 begin

 declare L_STRTP varchar(2);
 declare L_DOCNO varchar(8);
 declare L_MATCD varchar(10);
 declare L_DOCQT1 decimal(10,3);
 declare L_DOCQT2 decimal(10,3);
 declare END_TABLE  int  default 0; 


declare C_GRN  CURSOR FOR  select GR_STRTP,GR_PORNO,GR_MATCD, sum(isnull(GR_ACPQT,0)) GR_ACPQT from spldata.MM_GRMST where GR_STRTP = LP_STRTP and GR_MATCD in (select IN_MATCD from spldata.mm_inmst where in_strtp=LP_STRTP and in_indno=LP_INDNO) group by GR_STRTP, GR_PORNO, GR_MATCD;

declare C_POR  CURSOR FOR  select PO_STRTP,PO_INDNO,PO_MATCD, sum(isnull(PO_PORQT,0)) PO_PORQT,  sum(isnull(PO_ACPQT,0)) PO_ACPQT from spldata.MM_POMST where PO_STRTP = LP_STRTP and PO_MATCD in (select IN_MATCD from spldata.mm_inmst where in_strtp=LP_STRTP and in_indno=LP_INDNO) group by PO_STRTP, PO_INDNO, PO_MATCD;

declare C_IND  CURSOR FOR  select IN_STRTP,IN_MATCD, sum(isnull(IN_INDQT,0)-isnull(in_ordqt,0)-isnull(in_fccqt,0)) IN_INDQT from spldata.MM_INMST where IN_STRTP = LP_STRTP and IN_MATCD in (select IN_MATCD from spldata.mm_inmst where in_strtp=LP_STRTP and in_indno=LP_INDNO) group by IN_STRTP, IN_MATCD;


declare C_POR1  CURSOR FOR  select PO_STRTP,PO_MATCD, sum(isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0)) PO_PORQT from spldata.MM_POMST where PO_STRTP = LP_STRTP and PO_MATCD in (select IN_MATCD from spldata.mm_inmst where in_strtp=LP_STRTP and in_indno=LP_INDNO) group by PO_STRTP, PO_MATCD;

declare continue handler for not found 
       set END_TABLE = 1; 

set END_TABLE = 0; 
open C_GRN;
fetch C_GRN  into L_STRTP, L_DOCNO,L_MATCD, L_DOCQT1;
while END_TABLE = 0 DO 
   update spldata.mm_pomst set po_acpqt = L_DOCQT1  where po_strtp = L_STRTP and po_porno = L_DOCNO and po_matcd = L_MATCD;
   fetch C_GRN  into L_STRTP, L_DOCNO,L_MATCD, L_DOCQT1;
end while;
close C_GRN;
commit;


set END_TABLE = 0; 
open C_POR;
fetch C_POR  into L_STRTP, L_DOCNO,L_MATCD, L_DOCQT1, L_DOCQT2;
while END_TABLE = 0 DO 
   update spldata.mm_inmst set in_ordqt = L_DOCQT1, in_acpqt = L_DOCQT2  where in_strtp = L_STRTP and in_indno = L_DOCNO and in_matcd = L_MATCD;
   fetch C_POR  into L_STRTP, L_DOCNO,L_MATCD, L_DOCQT1, L_DOCQT2;
end while;
close C_POR;
commit;


set END_TABLE = 0; 
open C_IND;
fetch C_IND  into L_STRTP, L_MATCD, L_DOCQT1;
while END_TABLE = 0 DO 
   update spldata.mm_stmst set st_stkin =  L_DOCQT1  where st_strtp = L_STRTP and st_matcd = L_MATCD;
   fetch C_IND  into L_STRTP, L_MATCD, L_DOCQT1;
end while;
close C_IND;
commit;

set END_TABLE = 0; 
open C_POR1;
fetch C_POR1  into L_STRTP, L_MATCD, L_DOCQT1;
while END_TABLE = 0 DO 
   update spldata.mm_stmst set st_stkor =  L_DOCQT1  where st_strtp = L_STRTP and st_matcd = L_MATCD;
   fetch C_POR1  into L_STRTP, L_MATCD, L_DOCQT1;
end while;
close C_POR1;
commit;

end P1;
commit;

call spldata.setSTKQT_IND('01','41130516');
commit;


