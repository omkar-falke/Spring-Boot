commit;

drop trigger spldata.updCRFQT_DOR;
commit;


create trigger spldata.updCRFQT_DOR  after update of DOT_STSFL on spldata.MR_DOTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 
 begin 
  declare L_CMT_CUTDT char(10);
  declare L_DOT_DORDT date;
  declare L_DOT_MKTTP char(2);
  declare L_DOT_DORNO char(9);
  declare L_DOT_PRDCD char(10);
  declare L_DOT_PKGTP char(2);
  declare L_DOT_DORQT decimal(10,3);
  declare L_IVT_INVQT decimal(10,3);
  declare END_TABLE int default 0;

  declare C_DOR cursor for  select DOT_MKTTP, DOT_DORNO, DOT_PRDCD, DOT_PKGTP, ifnull(DOT_DORQT,0.000) DOT_DORQT, sum(ifnull(IVT_INVQT,0)) IVT_INVQT from  spldata.MR_DOTRN   left outer join spldata.MR_IVTRN on DOT_MKTTP = IVT_MKTTP and DOT_DORNO = IVT_DORNO and DOT_PRDCD=IVT_PRDCD and DOT_PKGTP = IVT_PKGTP  and DOT_STSFL <>'X'  and date(IVT_INVDT) < date(L_CMT_CUTDT)  where DOT_DORDT < date(L_CMT_CUTDT) and DOT_STSFL <> '0'  and dot_cmpcd = new_row.DOT_CMPCD and dot_mkttp = new_row.DOT_MKTTP and dot_dorno = new_row.DOT_DORNO and dot_prdcd = new_row.dot_prdcd and dot_pkgtp = new_row.dot_pkgtp group by DOT_MKTTP, DOT_DORNO, DOT_PRDCD, DOT_PKGTP,DOT_DORQT  having (ifnull(DOT_DORQT,0) - sum(ifnull(IVT_INVQT,0))) > 0;

  declare continue handler for not found 
           set END_TABLE = 1; 


  select CMT_CCSVL into L_CMT_CUTDT from spldata.CO_CDTRN where CMT_CGMTP='S'||new_row.DOT_CMPCD and CMT_CGSTP='MRDOCRF';
  --select DOT_DORDT into L_DOT_DORDT from spldata.MR_DOTRN where DOT_MKTTP = new_row.DOT_MKTTP and DOT_DORNO = new_row.DOT_DORNO;


  if new_row.DOT_STSFL in ( '1','X')  then
    if new_row.dot_dordt < date(L_CMT_CUTDT) then
        update spldata.MR_DOTRN set DOT_CFWQT = 0  where DOT_cmpcd = new_row.DOT_CMPCD and DOT_MKTTP = new_row.DOT_MKTTP and DOT_DORNO = new_row.DOT_DORNO
        and DOT_PRDCD = new_row.DOT_PRDCD and DOT_PKGTP = new_row.DOT_PKGTP;
        open C_DOR ;
        fetch C_DOR into  L_DOT_MKTTP, L_DOT_DORNO, L_DOT_PRDCD, L_DOT_PKGTP, L_DOT_DORQT, L_IVT_INVQT;
        while END_TABLE = 0 DO 
         update spldata.MR_DOTRN set DOT_CFWQT = (L_DOT_DORQT-L_IVT_INVQT)  where DOT_cmpcd = new_row.DOT_CMPCD and DOT_MKTTP = new_row.DOT_MKTTP and DOT_DORNO = new_row.DOT_DORNO
         and DOT_PRDCD = new_row.DOT_PRDCD and DOT_PKGTP = new_row.DOT_PKGTP;

         fetch C_DOR into  L_DOT_MKTTP, L_DOT_DORNO, L_DOT_PRDCD, L_DOT_PKGTP, L_DOT_DORQT, L_IVT_INVQT;
       end while;
    close C_DOR; 
   end if;
  end if;
end;

commit;
