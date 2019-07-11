drop procedure spldata.setRLSN_IND;
commit;


create procedure spldata.setRLSN_IND(IN LP_MKTTP char(2), IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 
 P1:
 begin
 declare L_IND_VAL decimal(10,2) default 0.00; 
 declare L_CCL_VAL decimal(10,2) default 0.00; 
 declare L_COM_VAL decimal(10,2) default 0.00; 
 declare L_CRD_VAL decimal(10,2) default 0.00; 

 declare L_MKTTP char(2); 
 declare L_INDNO char(9); 
 declare L_PRDCD char(10); 
 declare L_PKGTP char(2); 
 declare L_INDQT decimal(10,3) default 0.00; 
 declare L_INDRT decimal(10,2) default 0.00; 
 declare L_CDCVL decimal(10,2) default 0.00; 
 declare L_DDCVL decimal(10,2) default 0.00; 
 declare L_TDCVL decimal(10,2) default 0.00; 
 declare L_CPTVL decimal(10,2) default 0.00; 
 declare L_EOFFL int default 0; 
declare END_TABLE int default 0;

declare L_COMRT decimal(10,2) default 0.00; 


declare C_INT CURSOR FOR  select INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP, INT_INDQT, INT_BASRT,INT_CDCVL,INT_DDCVL,INT_TDCVL  from  spldata.MR_INTRN  where INT_MKTTP = LP_MKTTP and INT_STSFL<>'X' and INT_INDNO in (select IN_INDNO from spldata.MR_INMST where  IN_MKTTP=LP_MKTTP and IN_BKGDT between LP_STRDT and LP_ENDDT  and IN_STSFL <> 'X') order by INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP;


declare continue handler for not found 
--if(L_EOFFL=0) then 
       set END_TABLE = 1; 
--end if;

update spldata.MR_INTRN set INT_RSNVL = 0 where INT_MKTTP=LP_MKTTP and INT_INDNO in (select IN_INDNO from spldata.MR_INMST where IN_MKTTP=LP_MKTTP and  IN_BKGDT between LP_STRDT and LP_ENDDT  and IN_STSFL <> 'X');
delete from spldata.temp_chk;


open C_INT;
fetch C_INT into L_MKTTP, L_INDNO, L_PRDCD, L_PKGTP, L_INDQT, L_INDRT, L_CDCVL, L_DDCVL, L_TDCVL;


while END_TABLE = 0 DO 
    insert into spldata.temp_chk values (L_MKTTP+'/'+L_INDNO+'/'+L_PRDCD+'/'+L_PKGTP+'/'+char(L_INDQT)+'/'+char(L_INDRT)+'/'+char(L_CDCVL)+'/'+char(L_DDCVL)+'/'+char(L_TDCVL));

    set L_IND_VAL = isnull(L_INDQT,0)*isnull(L_INDRT,0);
    set L_CCL_VAL =  (isnull(L_INDQT,0)*isnull(L_CDCVL,0)) + (isnull(L_INDQT,0)*isnull(L_DDCVL,0)) + (isnull(L_INDQT,0)*isnull(L_TDCVL,0));

    insert into spldata.temp_chk values ('DOC'+'MR'+L_MKTTP+'IND'+SUBSTRING(L_INDNO,1,4));

    select isnull(CMT_NMP01,0)  into  L_COMRT from spldata.CO_CDTRN where CMT_CGMTP+CMT_CGSTP+CMT_CODCD = 'DOC'+'MR'+L_MKTTP+'IND'+SUBSTRING(L_INDNO,1,4);
    set L_COM_VAL = 0.00;
    if (SUBSTRING(L_PRDCD,7,1) = '0'  and SUBSTRING(L_PRDCD,1,2)='51')  or (SUBSTRING(L_PRDCD,1,2)='52')  then
          set L_COM_VAL =  (isnull(L_INDQT,0)*isnull(L_COMRT,0));
    end if;    

   select isnull(in_cptvl,0) into L_CPTVL from spldata.mr_inmst where in_mkttp=L_MKTTP and in_indno = L_INDNO;
   set L_CRD_VAL = 0.00;
   if L_CPTVL > 0 then
       set L_CRD_VAL = (((L_IND_VAL-(L_CCL_VAL+L_COM_VAL))*0.10)/365)*L_CPTVL;
   end if;

    insert into spldata.temp_chk values ('Indent value for  '+L_INDNO+' = '+char(L_IND_VAL));
    insert into spldata.temp_chk values ('Comm.Rate for  '+SUBSTRING(L_INDNO,1,4)+' = '+char(L_COM_VAL));
    insert into spldata.temp_chk values ('Credit claim for  '+L_INDNO+' = '+char(L_CCL_VAL));
    insert into spldata.temp_chk values ('Rlsn. value for  '+L_INDNO+' = '+char(L_IND_VAL-(L_CCL_VAL+L_COM_VAL)));

    update spldata.MR_INTRN set INT_INDVL = L_IND_VAL, INT_CCLVL = L_CCL_VAL, INT_COMVL = L_COM_VAL,INT_CRDVL = L_CRD_VAL, INT_RSNVL = (L_IND_VAL-(L_CCL_VAL+L_COM_VAL))  where INT_MKTTP = L_MKTTP and INT_INDNO = L_INDNO and INT_PRDCD =   L_PRDCD and INT_PKGTP = L_PKGTP; 

    fetch C_INT into L_MKTTP, L_INDNO, L_PRDCD, L_PKGTP, L_INDQT, L_INDRT, L_CDCVL, L_DDCVL, L_TDCVL;
end while;
close C_INT; 
commit;
end P1;


commit;

call spldata.setRLSN_IND('01','04/01/2007', '06/26/2007');
commit;

call spldata.setRLSN_IND('01','04/01/2003', '03/31/2004');
commit;
select * from spldata.temp_chk;
select * from spldata.mr_intrn where int_indno = 'WMP40892';

select * from spldata.mr_intrn where int_indno='WSM50064';

select int_indno,int_crdvl,int_rsnvl,int_stsfl from spldata.mr_intrn where int_indno in (select in_indno from spldata.mr_inmst where in_mkttp='01' and in_bkgdt between CONVERT(varchar,'12/01/2004',101) and CONVERT(varchar,'12/24/2004',101));
;

