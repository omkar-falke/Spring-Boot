


drop procedure spldata.updHREHTRN;
drop procedure spldata.updHR_EHTRN;
commit;


create procedure spldata.updHR_EHTRN(IN LP_CMPCD char(2),IN LP_STRDT char(10),IN LP_ENDDT char(10), IN LP_MMGRD char(5))  language sql  modifies sql data 

P1:



  begin

 

  declare L_CMPCD varchar(2);

  declare L_EMPNO varchar(5);

  declare L_DESGN varchar(10);

  declare L_EPLOC varchar(15);

  declare L_QUALN varchar(30);

  declare L_DPTCD varchar(3);

  declare L_BASAL decimal(5);

  declare L_DNALW decimal(5);

  declare L_PPSAL decimal(5);

  declare L_GRSAL decimal(7);

  declare L_MMGRD varchar(10);

  declare L_TELAL decimal(8,2);

  declare L_VHMAL decimal(8,2);

  declare L_TRFDT date;

  declare L_REPHD varchar(5);

  declare L_HRSBS varchar(6);

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;



  declare C_EPMST cursor for select EP_EMPNO, EP_DESGN, EP_EPLOC, EP_QUALN, EP_DPTCD, EP_BASAL, GR_DNALW,EP_PPSAL, EP_GRSAL,  EP_MMGRD, EP_TELEN,EP_VCLMT,EP_TRFDT, EP_REPHD, EP_HRSBS from spldata.HR_EPMST,spldata.HR_GRMST where EP_CMPCD = LP_CMPCD and EP_MMGRD = LP_MMGRD and isnull(EP_LFTDT,LP_STRDT) >= LP_STRDT and EP_CMPCD = GR_CMPCD and EP_MMGRD = GR_GRDCD;

  declare continue handler for not found 

       set END_TABLE = 1; 



  set END_TABLE = 0;

   open C_EPMST ;

  fetch C_EPMST into  L_EMPNO, L_DESGN, L_EPLOC, L_QUALN, L_DPTCD, L_BASAL, L_DNALW, L_PPSAL, L_GRSAL,  L_MMGRD, L_TELAL, L_VHMAL,L_TRFDT, L_REPHD, L_HRSBS;

  while END_TABLE = 0 DO 

    set END_TABLE = 0;

    select count(*) into L_RECCT  from spldata.hr_ehtrn where EH_CMPCD = LP_CMPCD and EH_EMPNO = L_EMPNO and EH_STRDT = LP_STRDT;

   if L_RECCT = 0 then 

      insert into spldata.HR_EHTRN (EH_CMPCD,EH_EMPNO, EH_STRDT,EH_ENDDT,EH_DESGN, EH_EPLOC, EH_QUALN, EH_DPTCD, EH_BASAL, EH_DNALW,EH_PPSAL, EH_GRSAL,  EH_MMGRD, EH_TELAL, EH_VHMAL,EH_TRFDT, EH_REPHD, EH_HRSBS,EH_CMPRF) values (LP_CMPCD,L_EMPNO, LP_STRDT,LP_ENDDT,L_DESGN, L_EPLOC, L_QUALN, L_DPTCD, L_BASAL, L_DNALW,L_PPSAL, L_GRSAL,  L_MMGRD, L_TELAL, L_VHMAL,L_TRFDT, L_REPHD, L_HRSBS,'SUPREME PETROCHEM LTD.');

   end if;



  fetch C_EPMST into  L_EMPNO, L_DESGN, L_EPLOC, L_QUALN, L_DPTCD, L_BASAL, L_DNALW, L_PPSAL, L_GRSAL,  L_MMGRD, L_TELAL, L_VHMAL,L_TRFDT, L_REPHD, L_HRSBS;

  end while;

  close C_EPMST;

  commit;

end;

commit;

select distinct ep_mmgrd  from spldata.hr_epmst where ep_cmpcd='01' and ep_mmgrd not like '%TM%';
select * from spltest.hr_ehtrn;

call spldata.updHR_EHTRN('01','04/01/2008','03/31/2009','ATM3');
call spldata.updHR_EHTRN('01','04/01/2008','03/31/2009','TM3');
call spldata.updHR_EHTRN('01','04/01/2008','03/31/2009','TM4');
call spldata.updHR_EHTRN('01','04/01/2008','03/31/2009','TM5');
call spldata.updHR_EHTRN('01','04/01/2008','03/31/2009','TM6');
call spldata.updHR_EHTRN('01','04/01/2008','03/31/2009','TTM');
commit;
update spltest.hr_ehtrn set eh_eploc='WORKS' where eh_eploc='01';
commit;

call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','GET');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MIA');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MIIA');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MIVA');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MIVB');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MIVC');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MVA');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MVB');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MVIA');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MVIB');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MVIIA');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MVIIB');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','MVIIC');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','RTO');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','RTT');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','WM1');	
call spltest.updHR_EHTRN('01','04/01/2008','03/31/2009','WM2');
commit;
	
