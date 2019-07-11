drop trigger spldata.indRLSN1;
commit;


create trigger spldata.indRLSN1  after update of INT_STSFL on spldata.MR_INTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 

  begin
 declare L_IND_VAL decimal(10,2) default 0.00; 
 declare L_CCL_VAL decimal(10,2) default 0.00; 
 declare L_COM_VAL decimal(10,2) default 0.00; 

 declare L_MKTTP char(2); 
 declare L_INDNO char(9); 
 declare L_PRDCD char(10); 
 declare L_PKGTP char(2); 
 declare L_INDQT decimal(10,3) default 0.00; 
 declare L_INDRT decimal(10,2) default 0.00; 
 declare L_CDCVL decimal(10,2) default 0.00; 
 declare L_DDCVL decimal(10,2) default 0.00; 
 declare L_TDCVL decimal(10,2) default 0.00; 
 declare L_EOFFL int default 0; 
declare END_TABLE int default 0;

declare L_COMRT decimal(10,2) default 0.00; 


declare C_INT CURSOR FOR  select INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP, INT_INDQT, INT_BASRT,INT_CDCVL,INT_DDCVL,INT_TDCVL  from  spldata.MR_INTRN  where INT_MKTTP = new_row.INT_MKTTP and INT_INDNO = new_row.INT_INDNO order by INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP;


declare continue handler for not found 
if(L_EOFFL=0) then
       set END_TABLE = 1; 
end if;

if new_row.INT_STSFL in ( '1','O','A')  then 
   update spldata.MR_INTRN set INT_RSNVL = 0 where INT_MKTTP=new_row.INT_MKTTP and INT_INDNO = new_row.INT_INDNO;

open C_INT;
fetch C_INT into L_MKTTP, L_INDNO, L_PRDCD, L_PKGTP, L_INDQT, L_INDRT, L_CDCVL, L_DDCVL, L_TDCVL;


while END_TABLE = 0 DO 

    set L_IND_VAL = isnull(L_INDQT,0)*isnull(L_INDRT,0);
    set L_CCL_VAL =  (isnull(L_INDQT,0)*isnull(L_CDCVL,0)) + (isnull(L_INDQT,0)*isnull(L_DDCVL,0)) + (isnull(L_INDQT,0)*isnull(L_TDCVL,0));


   set L_EOFFL=1;
    select isnull(CMT_NMP01,0)  into  L_COMRT from spldata.CO_CDTRN where CMT_CGMTP + CMT_CGSTP + CMT_CODCD = 'DOC' + 'MR' + L_MKTTP + 'IND' + SUBSTRING(L_INDNO,1,4);
   set L_EOFFL=0;
    set L_COM_VAL = 0.00;
    if (SUBSTRING(L_PRDCD,7,1) = '0'  and SUBSTRING(L_PRDCD,1,2)='51')  or (SUBSTRING(L_PRDCD,1,2)='52')  then
          set L_COM_VAL =  (isnull(L_INDQT,0)*isnull(L_COMRT,0));
    end if;    


    update spldata.MR_INTRN set INT_RSNVL = (L_IND_VAL-(L_CCL_VAL+L_COM_VAL))  where INT_MKTTP = L_MKTTP and INT_INDNO = L_INDNO and INT_PRDCD =   L_PRDCD and INT_PKGTP = L_PKGTP; 

    fetch C_INT into L_MKTTP, L_INDNO, L_PRDCD, L_PKGTP, L_INDQT, L_INDRT, L_CDCVL, L_DDCVL, L_TDCVL;
end while;
close C_INT; 
end if;
end;

