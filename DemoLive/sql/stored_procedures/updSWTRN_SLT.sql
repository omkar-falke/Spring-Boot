
-- 
-- 

-- FEATURES INTRODUCED

drop procedure spldata.updSWTRN_SLT;
commit;

create procedure spldata.updSWTRN_SLT(IN LP_CMPCD char(2))  language sql  modifies sql data 
P1:
begin
  declare L_EMPNO char(4);
  declare L_SBSCD char(6);
  declare L_WRKDT date;
  declare L_SRLNO char(2);
  declare L_PNCTM timestamp;
  declare L_XXXTM timestamp;
  declare L_INOCD char(1);
  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_SLT  cursor for  select  SLT_EMPNO,SLT_PNCTM,SLT_INOCD from spldata.HR_SLTRN  where SLT_CMPCD = LP_CMPCD and SLT_EMPNO <> 'XXXX' order by SLT_EMPNO, SLT_PNCTM;

  declare continue handler for not found 
       set END_TABLE = 1; 


  set END_TABLE = 0;
  set L_SBSCD = LP_CMPCD + LP_CMPCD + '00';

  open C_SLT;
  fetch C_SLT  into  L_EMPNO, L_PNCTM, L_INOCD;

  while END_TABLE = 0 DO 
     if L_INOCD = '0' then
        set L_WRKDT = CONVERT(varchar,L_PNCTM,101);
        if time(L_PNCTM) < '05:00' then
           set L_WRKDT = CONVERT(varchar,day(L_PNCTM)-1,101);
        end if; 
        select count(*) into L_RECCT from spldata.HR_SWTRN where SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_INCTM = L_PNCTM;
        select isnull(max(isnull(SWT_SRLNO,'0')),'0')+1 into L_SRLNO from spldata.HR_SWTRN where SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_WRKDT = L_WRKDT;
        
        if L_RECCT = 0 then
           select min(SWT_OUTTM) into L_XXXTM from spldata.HR_SWTRN where SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_WRKDT = L_WRKDT and SWT_OUTTM is not null and SWT_OUTTM > L_PNCTM;
           if L_XXXTM is null then
              insert into spldata.HR_SWTRN (SWT_CMPCD, SWT_EMPNO,SWT_WRKDT,SWT_SRLNO,SWT_INCTM,SWT_TRNFL,SWT_STSFL, SWT_LUSBY,SWT_LUPDT,SWT_SBSCD) values (LP_CMPCD,L_EMPNO,L_WRKDT,L_SRLNO,L_PNCTM,'0','1','SYS',current_date,L_SBSCD);
           end if;
           if L_XXXTM is not null then
              update spldata.HR_SWTRN set SWT_INCTM = L_PNCTM where  SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_WRKDT = L_WRKDT and SWT_OUTTM = L_XXXTM;
           end if;
        end if;
     end if;
     if L_INOCD = '1' then
        set L_WRKDT = CONVERT(varchar,L_PNCTM,101);
        if time(L_PNCTM) < '09:00' then
           set L_WRKDT = CONVERT(varchar,day(L_PNCTM)-1,101);
        end if; 
        select count(*) into L_RECCT from spldata.HR_SWTRN where  SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_OUTTM = L_PNCTM;
        select isnull(max(isnull(SWT_SRLNO,'0')),'0')+1 into L_SRLNO from spldata.HR_SWTRN where SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_WRKDT = L_WRKDT;
        if L_RECCT = 0 then
           select max(SWT_INCTM) into L_XXXTM from spldata.HR_SWTRN where  SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_WRKDT = L_WRKDT and SWT_INCTM is not null and SWT_INCTM < L_PNCTM;
           if L_XXXTM is null then
              insert into spldata.HR_SWTRN (SWT_CMPCD, SWT_EMPNO,SWT_WRKDT,SWT_SRLNO,SWT_OUTTM,SWT_TRNFL,SWT_STSFL, SWT_LUSBY,SWT_LUPDT,SWT_SBSCD) values (LP_CMPCD,L_EMPNO,L_WRKDT,L_SRLNO,L_PNCTM,'0','1','SYS',current_date,L_SBSCD);
           end if;
           if L_XXXTM is not null then
              update spldata.HR_SWTRN set SWT_OUTTM = L_PNCTM where  SWT_CMPCD = LP_CMPCD and SWT_EMPNO = L_EMPNO and SWT_WRKDT = L_WRKDT and SWT_INCTM = L_XXXTM;
           end if;
        end if;
     end if;
   
     set END_TABLE = 0;
        fetch C_SLT  into  L_EMPNO, L_PNCTM, L_INOCD;
end while;
close C_SLT;
commit;     
end P1;

commit;
call spldata.updSWTRN_SLT('01');
commit;

select * from spldata.ho_sltrn where slt_cmpcd='02' and CONVERT(varchar,slt_pnctm,101)='01/24/2009';
select * from spldata.hr_sltrn where slt_cmpcd='02' and CONVERT(varchar,slt_pnctm,101)='01/24/2009';
select * from spldata.hr_swtrn where swt_cmpcd='02';
select * from spldata.hr_epmst where ep_cmpcd='02';
commit;
;


   delete from spldata.ho_sltrn where slt_pnctm is null;
commit;
   delete from spldata.ho_sltrn where slt_cmpcd + slt_empno + char(slt_pnctm) + slt_inocd  in (select slt_cmpcd + slt_empno + char(slt_pnctm) + slt_inocd from spldata.hr_sltrn);
   commit;
   delete from spldata.hr_sltrn where slt_cmpcd='02';
commit;

   insert into spldata.hr_sltrn (SLT_EMPNO,SLT_PNCTM,SLT_INOCD,SLT_STSFL,SLT_CMPCD) select distinct SLT_EMPNO,SLT_PNCTM,SLT_INOCD,SLT_STSFL,SLT_CMPCD from spldata.ho_sltrn where isnull(slt_empno,'') <> 'XXXX' and slt_cmpcd='02';
   commit;

select SLT_EMPNO,SLT_PNCTM,SLT_INOCD,SLT_STSFL,SLT_CMPCD from spldata.ho_sltrn where isnull(slt_empno,'') <> 'XXXX' and slt_cmpcd='02';

select * from spldata.ho_sltrn where slt_cmpcd='02';
delete from spldata.ho_sltrn where slt_cmpcd='02' and slt_empno='XXXX';
commit;

select * from spldata.hr_swtrn where swt_wrkdt = '01/24/2009';
