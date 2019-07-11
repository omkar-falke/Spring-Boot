drop procedure spldata.setTTSWTRN;
commit;

create procedure spldata.setTTSWTRN()  language sql  modifies sql data 
 P1:
 begin
declare L_SRLNO int;
declare L_CMPCD varchar(2);
declare L_SBSCD varchar(6);
declare L_EMPNO varchar(4);
declare L_WRKDT date;
declare L_inctm timestamp;
declare L_outtm timestamp;

declare L_CMPCD1 varchar(2);
declare L_SBSCD1 varchar(6);
declare L_EMPNO1 varchar(4);
declare L_WRKDT1 date;
declare L_inctm1 timestamp;
declare L_outtm1 timestamp;

declare END_TABLE int default 0;

declare C_SWT CURSOR FOR  select swt_cmpcd,swt_sbscd,swt_empno,swt_wrkdt,swt_inctm,swt_outtm from spldata.tt_swtrn order by swt_cmpcd,swt_sbscd,swt_empno,swt_wrkdt,swt_inctm,swt_outtm;


declare continue handler for not found 
       set END_TABLE = 1;

   set end_table = 0;
   open C_SWT;
   fetch C_SWT  into l_cmpcd,l_sbscd,l_empno,l_wrkdt,l_inctm,l_outtm;

   while END_TABLE = 0 DO 
      set l_cmpcd1 = l_cmpcd;
      set l_sbscd1 = l_sbscd;
      set l_empno1 = l_empno;
      set l_wrkdt1 = l_wrkdt;
      set l_inctm1 = l_inctm;
      set l_outtm1 = l_outtm;
      set l_srlno = 0;
     while l_cmpcd1 = l_cmpcd and l_sbscd1 = l_sbscd and l_empno1 = l_empno and l_wrkdt1 = l_wrkdt and END_TABLE = 0 DO
            set l_srlno = l_srlno +1;
            if l_inctm is not null and l_outtm is not null then
               update spldata.tt_swtrn set swt_srlno = char(l_srlno) where swt_cmpcd = l_cmpcd and swt_sbscd=l_sbscd and swt_empno = l_empno and swt_wrkdt = l_wrkdt and swt_inctm = l_inctm and swt_outtm = l_outtm;
            end if;
            if l_inctm is not null and l_outtm is null then
               update spldata.tt_swtrn set swt_srlno = char(l_srlno) where swt_cmpcd = l_cmpcd and swt_sbscd=l_sbscd and swt_empno = l_empno and swt_wrkdt = l_wrkdt and swt_inctm = l_inctm;
            end if;
            if l_inctm is null and l_outtm is not null then
               update spldata.tt_swtrn set swt_srlno = char(l_srlno) where swt_cmpcd = l_cmpcd and swt_sbscd=l_sbscd and swt_empno = l_empno and swt_wrkdt = l_wrkdt and swt_outtm = l_outtm;
            end if;
           set end_table = 0;
          fetch C_SWT  into l_cmpcd,l_sbscd,l_empno,l_wrkdt,l_inctm,l_outtm;
     end while;
   end while;
   close C_SWT;
   commit;
end;

commit;

call spldata.setTTSWTRN();




