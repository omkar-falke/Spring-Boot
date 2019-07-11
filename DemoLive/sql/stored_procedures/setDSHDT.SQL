drop procedure spldata.setDSHDT;
commit;


create procedure spldata.setDSHDT(IN LP_CMPCD char(2),IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 
 P1:
 begin

 declare L_IVT_INVNO varchar(8);
 declare END_TABLE  int  default 0; 

declare C_IVT1  CURSOR FOR  select distinct IVT_INVNO from spldata.MR_IVTRN  
 where ivt_cmpcd = lp_cmpcd and IVT_MKTTP in ('01','04','05') and isnull(IVT_INVQT,0)>0 and IVT_STSFL = 'D' and CONVERT(varchar,IVT_INVDT,101) between  CONVERT(varchar,LP_STRDT,101)  and CONVERT(varchar,LP_ENDDT,101);

declare continue handler for not found 
       set END_TABLE = 1; 

open C_IVT1;
fetch C_IVT1 into L_IVT_INVNO;
while END_TABLE = 0 DO 
   call spldata.setDSHINV(L_IVT_INVNO);
   fetch C_IVT1 into L_IVT_INVNO;
end while;
close C_IVT1; 
--commit;
end P1;



commit;
call spldata.setDSHDT('03/01/2007','03/16/2007');
commit;


