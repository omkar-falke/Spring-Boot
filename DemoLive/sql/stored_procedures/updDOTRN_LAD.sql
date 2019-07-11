
-- procedure to reset cumulative quantities (of other transactions) maintained in DO Transaction.
-- This procedure is called externally whenever require. It is also called in LA preparation (if required), occasionally through MR_TELAD.java

-- FEATURES INTRODUCED

drop procedure spldata.updDOTRN_LAD;
commit;

create procedure spldata.updDOTRN_LAD(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_DORNO char(8))  language sql  modifies sql data 
P1:
begin
  declare L_PRDCD char(10);
  declare L_PKGTP char(2);
  declare L_REQQT decimal(10,3) default 0.000;
  declare L_LADQT decimal(10,3) default 0.000;
  declare L_LADQT1 decimal(10,3) default 0.000;

  declare END_TABLE int default 0;

  declare C_DOR  cursor for  select  DOT_PRDCD,DOT_PKGTP,sum(isnull(IVT_REQQT,0)) IVT_REQQT from spldata.MR_DOTRN left outer join spldata.MR_IVTRN on DOT_CMPCD = IVT_CMPCD and DOT_MKTTP = IVT_MKTTP and DOT_DORNO = IVT_DORNO and DOT_PRDCD = IVT_PRDCD and DOT_PKGTP = IVT_PKGTP  where DOT_CMPCD = LP_CMPCD and DOT_MKTTP = LP_MKTTP and DOT_DORNO = LP_DORNO and isnull(DOT_DORQT,0)>0 and DOT_STSFL <> 'X' group by DOT_PRDCD,DOT_PKGTP order by DOT_PRDCD, DOT_PKGTP;

  declare continue handler for not found 
       set END_TABLE = 1; 


  open C_DOR;
   set END_TABLE = 0;
  fetch C_DOR  into  L_PRDCD, L_PKGTP, L_REQQT;

  while END_TABLE = 0 DO 

      update spldata.mr_dotrn set dot_ladqt = L_REQQT where dot_cmpcd = LP_CMPCD and dot_mkttp = LP_MKTTP and dot_dorno = LP_DORNO and dot_prdcd = L_PRDCD and dot_pkgtp = L_PKGTP;
      set END_TABLE = 0;
  fetch C_DOR  into  L_PRDCD, L_PKGTP, L_REQQT;
end while;
close C_DOR;
       
end P1;

commit;

call spldata.updDOTRN_LAD('01','70107792');
commit;
