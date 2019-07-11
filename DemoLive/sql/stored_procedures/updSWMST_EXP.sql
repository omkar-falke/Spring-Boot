
-- procedure to reset cumulative exit time (official & personal) for the day against every employee
-- This procedure is called externally through Process option in Exist Pass confirmation module.(HR_TEEXA.java)

drop procedure spldata.updSWMST_EXP;
commit;

create procedure spldata.updSWMST_EXP(IN LP_CMPCD char(2),IN LP_STRDT char(10),IN LP_ENDDT char(10))  language sql  modifies sql data 
P1:
begin
  declare L_DOCDT date;
  declare L_EMPNO char(4);
  declare L_EMPCT char(1);
  declare L_ADDHR int;
  declare L_AOTHR int;
  declare L_AOTMN int;
  declare L_AOTHR1 char(2);
  declare L_AOTMN1 char(2);
  declare L_AOTTM time;
  declare L_OVTWK time;
  declare L_OVTWK1 time;
  declare L_AOTTM1 char(5);
  declare L_OFPFL char(1);

  declare END_TABLE int default 0;

  declare C_EXP  cursor for select  EX_DOCDT,EX_EMPNO,EX_OFPFL,sum(hour(EX_AOTTM)),sum(minute(EX_AOTTM)) from spldata.HR_EXTRN where EX_CMPCD = LP_CMPCD and EX_DOCDT between CONVERT(varchar,LP_STRDT,101) and CONVERT(varchar,LP_ENDDT,101) and isnull(EX_OTGFL,'') <> 'Y' and isnull(EX_AOTTM,'00:00')>'00:00' and EX_stsfl in ('2','3') group by EX_DOCDT,EX_EMPNO,EX_OFPFL;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_EXP;
   set END_TABLE = 0;
  fetch C_EXP  into  L_DOCDT,L_EMPNO,L_OFPFL,L_AOTHR, L_AOTMN;

  while END_TABLE = 0 DO 
      set L_ADDHR = 0;
      if L_AOTMN > 59 then
         set L_ADDHR = int(L_AOTMN/60);
         set L_AOTMN = L_AOTMN - (60*L_ADDHR);
      end if;
      set L_AOTHR1 = right('00' + trim(char(L_AOTHR+L_ADDHR)),2);
      set L_AOTMN1 = right('00' + trim(char(L_AOTMN)),2);
      set L_AOTTM1 = L_AOTHR1 + ':' + L_AOTMN1;
      
      if L_OFPFL = 'O' then
        update spldata.hr_swmst set sw_oottm = L_AOTTM1 where sw_cmpcd = LP_CMPCD and sw_empno = L_EMPNO and sw_wrkdt = L_DOCDT;
      end if;
      if L_OFPFL = 'P' then
         update spldata.hr_swmst set sw_pottm = L_AOTTM1 where sw_cmpcd = LP_CMPCD and sw_empno = L_EMPNO and sw_wrkdt = L_DOCDT;
         select sw_ovtwk into L_OVTWK from spldata.hr_swmst where sw_cmpcd = LP_CMPCD and sw_empno = L_EMPNO and sw_wrkdt = L_DOCDT;
         select ep_empct into L_EMPCT from spldata.hr_epmst where ep_cmpcd = LP_CMPCD and ep_empno = L_EMPNO;
         set L_OVTWK1 = time('00:00:00');
         if(L_OVTWK > time(L_AOTTM1)) then 
            set L_OVTWK1 =  time(SUBSTRING(right(trim('000000' + char(L_OVTWK-time(L_AOTTM1))),6),1,2) + ':' + SUBSTRING(right(trim('000000' + char(L_OVTWK-time(L_AOTTM1))),6),3,2) + ':' + SUBSTRING(right(trim('000000' + char(L_OVTWK-time(L_AOTTM1))),6),5,2));
         end if;
         if (L_EMPCT = 'T' or L_EMPCT = 'W') and L_OVTWK1 < time('00:30:00') then 
             set L_OVTWK1 = time('00:00:00');
         end if;
         if L_EMPCT = 'O' and L_OVTWK1 < time('06:00:00') then 
             set L_OVTWK1 = time('00:00:00');
         end if;
         update spldata.hr_swmst set sw_ovtwk = L_OVTWK1 where sw_cmpcd = LP_CMPCD and sw_empno = L_EMPNO and sw_wrkdt = L_DOCDT;
      end if;
      set END_TABLE = 0;
      fetch C_EXP  into  L_DOCDT,L_EMPNO,L_OFPFL,L_AOTHR, L_AOTMN;
end while;
close C_EXP;
       
end P1;

commit;

call spldata.updSWMST_EXP('01','03/01/2009','04/04/2009');
commit;
select * from spldata.hr_swmst where sw_ovtwk>'00:00:00' and sw_pottm is not null;
select * from spldata.hr_swmst where sw_ovtwk>'00:00:00';