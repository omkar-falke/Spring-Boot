select EP_YOPDT   from spldata.hr_epmst;


drop trigger spldata.updHR_LVTRN;
commit;


create trigger spldata.updHR_LVTRN  after update of LVT_STSFL on spldata.HR_LVTRN referencing new as new_row old as old_row  for each row mode DB2ROW


  begin

  declare L_EPYOPDT date;
  select EP_YOPDT into L_EPYOPDT  from spldata.hr_epmst where ep_cmpcd=new_row.LVT_CMPCD and EP_EMPNO = new_row.LVT_EMPNO;

if L_EPYOPDT is not null and year(L_EPYOPDT) = year(new_row.LVT_LVEDT) then

if (new_row.LVT_STSFL ='2'  and old_row.LVT_STSFL ='1')  or (new_row.LVT_STSFL ='3'  and old_row.LVT_STSFL ='1')  then 

  if new_row.LVT_LVECD = 'CL'  then 
      update spldata.hr_epmst set ep_ytdcl = ep_ytdcl-new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;
  if new_row.LVT_LVECD = 'PL'  then 
      update spldata.hr_epmst set ep_ytdpl = ep_ytdpl-new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;
  if new_row.LVT_LVECD = 'PE'  then 
      update spldata.hr_epmst set ep_ytdpl = ep_ytdpl-new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;
  if new_row.LVT_LVECD = 'SL'  then 
      update spldata.hr_epmst set ep_ytdsl = ep_ytdsl-new_row.lvt_lveqt where  ep_cmpcd = new_row.lvt_cmpcd  and ep_empno =new_row.lvt_empno;
  end if;
  if new_row.LVT_LVECD = 'RH'  then 
      update spldata.hr_epmst set ep_ytdrh = ep_ytdrh-new_row.lvt_lveqt where  ep_cmpcd = new_row.lvt_cmpcd  and ep_empno =new_row.lvt_empno;
  end if;


end if;

if  (new_row.LVT_STSFL = 'X'    and  old_row.LVT_STSFL in  ('2','3') )  then 

  if old_row.LVT_LVECD = 'CL' then 
      update spldata.hr_epmst set ep_ytdcl = ep_ytdcl+new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;
  if old_row.LVT_LVECD = 'PL' then 
      update spldata.hr_epmst set ep_ytdpl = ep_ytdpl+new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;
  if old_row.LVT_LVECD = 'PE'  then 
      update spldata.hr_epmst set ep_ytdpl = ep_ytdpl+new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;
  if old_row.LVT_LVECD = 'SL'  then 
      update spldata.hr_epmst set ep_ytdsl = ep_ytdsl+new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;
  if old_row.LVT_LVECD = 'RH' then 
      update spldata.hr_epmst set ep_ytdrh = ep_ytdrh+new_row.lvt_lveqt where ep_cmpcd = new_row.lvt_cmpcd and ep_empno =new_row.lvt_empno;
  end if;


end if;
if new_row.LVT_STSFL in ('2','3','X')  then 
   if new_row.LVT_LVECD <> 'PE'  then 
      update spldata.hr_sstrn set ss_lvecd = new_row.lvt_lvecd  where ss_cmpcd = new_row.lvt_cmpcd and ss_empno =new_row.lvt_empno and ss_wrkdt = new_row.lvt_lvedt;
      update spldata.hr_swmst set sw_lvecd = new_row.lvt_lvecd  where sw_cmpcd = new_row.lvt_cmpcd and sw_empno =new_row.lvt_empno and sw_wrkdt = new_row.lvt_lvedt;
   end if;
end if;
end if;
end;


commit;

select * from spldata.fg_srtrn where sr_lotno ='12290733';
select * from spldata.fg_stmst where st_lotno ='12290733';

;





