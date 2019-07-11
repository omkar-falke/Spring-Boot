commit;

drop trigger spldata.updCRFQT_IND;
commit;


create trigger spldata.updCRFQT_IND  after update of INT_STSFL on spldata.MR_INTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 
 begin 
  declare L_CMT_CUTDT char(10);
  declare L_IN_BKGDT date;
  declare L_INT_MKTTP char(2);
  declare L_INT_INDNO char(9);
  declare L_INT_PRDCD char(10);
  declare L_INT_PKGTP char(2);
  declare L_INT_INDQT decimal(10,3);
  declare L_INT_FCMQT decimal(10,3);
  declare L_IVT_INVQT decimal(10,3);
  declare END_TABLE int default 0;

  declare C_IND cursor for  select INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP, ifnull(INT_INDQT,0.000)  INT_INDQT,ifnull(INT_FCMQT,0.000)  INT_FCMQT, sum(ifnull(IVT_INVQT,0)) IVT_INVQT from  spldata.VW_INTRN   left outer join spldata.MR_IVTRN on INT_MKTTP = IVT_MKTTP and INT_INDNO = IVT_INDNO and INT_PRDCD=IVT_PRDCD and INT_PKGTP = IVT_PKGTP  and IVT_STSFL <>'X'  and date(IVT_INVDT) < date(L_CMT_CUTDT)  where IN_BKGDT < date(L_CMT_CUTDT) and  INT_STSFL <> '0'  and int_cmpcd = new_row.INT_CMPCD and int_mkttp = new_row.INT_MKTTP and int_indno = new_row.INT_INDNO and int_prdcd = new_row.int_prdcd and int_pkgtp = new_row.int_pkgtp group by INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP,INT_INDQT,INT_FCMQT  having (ifnull(INT_INDQT,0) - sum(ifnull(IVT_INVQT,0))) > 0;

  declare continue handler for not found 
           set END_TABLE = 1; 


  select CMT_CCSVL into L_CMT_CUTDT from spldata.CO_CDTRN where CMT_CGMTP='S'||new_row.INT_CMPCD and CMT_CGSTP='MRXXCRF';
  select IN_BKGDT into L_IN_BKGDT from spldata.MR_INMST where IN_cmpcd = new_row.INT_CMPCD and IN_MKTTP = new_row.INT_MKTTP and IN_INDNO = new_row.INT_INDNO;


  if new_row.INT_STSFL in ( '1','X')  then
    if L_IN_BKGDT < date(L_CMT_CUTDT) then
        update spldata.MR_INTRN set INT_CFWQT = 0  where INT_cmpcd = new_row.INT_CMPCD and INT_MKTTP = new_row.INT_MKTTP and INT_INDNO = new_row.INT_INDNO
        and INT_PRDCD = new_row.INT_PRDCD and INT_PKGTP = new_row.INT_PKGTP;
        open C_IND ;
        fetch C_IND into  L_INT_MKTTP, L_INT_INDNO, L_INT_PRDCD, L_INT_PKGTP, L_INT_INDQT, L_INT_FCMQT, L_IVT_INVQT;
        while END_TABLE = 0 DO 
         update spldata.MR_INTRN set INT_CFWQT = (L_INT_INDQT-L_INT_FCMQT-L_IVT_INVQT)  where INT_cmpcd = new_row.INT_CMPCD and INT_MKTTP = new_row.INT_MKTTP and INT_INDNO = new_row.INT_INDNO
         and INT_PRDCD = new_row.INT_PRDCD and INT_PKGTP = new_row.INT_PKGTP;

         fetch C_IND into  L_INT_MKTTP, L_INT_INDNO, L_INT_PRDCD, L_INT_PKGTP, L_INT_INDQT, L_INT_FCMQT,L_IVT_INVQT;
       end while;
    close C_IND; 
   end if;
  end if;
end;

commit;
