-- procedure to create a Work Table for Despatch & Classification summary
-- Procedure is called through fg_rpdcl.java

drop procedure spldata.crtFG_WKDCL;
commit;


create procedure spldata.crtFG_WKDCL(IN LP_CMPCD char(2),IN LP_RPTDT char(10))  language sql  modifies sql data 
 P1:

begin
  declare L_CMPCD char(2);
  declare L_STRDT char(10);
  declare L_PRDCT char(4);
  declare L_QLTCT char(2);
  declare L_PRDCD char(10);
  declare L_SALTP char(2);
  declare L_SLRFL char(1);

  declare L_CLOQT decimal(10,3);
  declare L_CLTQT decimal(10,3);

  declare L_DSOQT decimal(10,3);
  declare L_DDOQT decimal(10,3);
  declare L_DEOQT decimal(10,3);
  declare L_DCOQT decimal(10,3);

  declare L_DSTQT decimal(10,3);
  declare L_DDTQT decimal(10,3);
  declare L_DETQT decimal(10,3);
  declare L_DCTQT decimal(10,3);

  declare L_DSRQT decimal(10,3);
  declare L_RECCT  int default 0;
  declare END_TABLE int default 0;

-- Classification for the day
  declare C_CLO cursor for  select  PTF_PRDCD, sum(isnull(PTF_PTFQT,0)) PTF_PTFQT from spldata.FG_PTFRF where  PTF_CMPCD = LP_CMPCD and PTF_PTFCT in ('01') and PTF_PTFDT = LP_RPTDT group by PTF_PRDCD order by  PTF_PRDCD;

-- Classification for the Month
  declare C_CLT cursor for  select  PTF_PRDCD, sum(isnull(PTF_PTFQT,0)) PTF_PTFQT from spldata.FG_PTFRF where  PTF_CMPCD = LP_CMPCD and PTF_PTFCT in ('01') and PTF_PTFDT between L_STRDT and  LP_RPTDT  group by PTF_PRDCD order by  PTF_PRDCD;

-- Despatches for the Day
  declare C_DSO cursor for  select  IVT_PRDCD, IVT_SALTP,isnull(IVT_SLRFL,' ') IVT_SLRFL,sum(isnull(IVT_INVQT,0)) IVT_INVQT from spldata.MR_IVTRN where  IVT_CMPCD = LP_CMPCD and IVT_MKTTP in ('01','03','04','05') and CONVERT(varchar,ivt_invdt,101) = CONVERT(varchar,LP_RPTDT,101)  group by IVT_PRDCD, IVT_SALTP,IVT_SLRFL  order by  IVT_PRDCD;

-- Despatches for the Month
  declare C_DST cursor for  select  IVT_PRDCD, IVT_SALTP,isnull(IVT_SLRFL,' ') IVT_SLRFL, sum(isnull(IVT_INVQT,0)) IVT_INVQT from spldata.MR_IVTRN where  IVT_CMPCD = LP_CMPCD and IVT_MKTTP in ('01','03','04','05') and CONVERT(varchar,ivt_invdt,101)  between CONVERT(varchar,L_STRDT,101) and  CONVERT(varchar,LP_RPTDT,101)  group by IVT_PRDCD, IVT_SALTP,IVT_SLRFL  order by  IVT_PRDCD;


  declare continue handler for not found 
       set END_TABLE = 1; 
delete from spldata.FG_WKDCL;
delete from spldata.TEMP_CHK;
commit;

 set L_STRDT = SUBSTRING (LP_RPTDT,1,2) + '/01/' + SUBSTRING (lp_rptdt,7,4);
 set END_TABLE = 0;
 open C_CLO;
  fetch C_CLO into  L_PRDCD, L_CLOQT;
  while END_TABLE = 0 DO 
      set L_PRDCT = SUBSTRING (L_PRDCD,1,4);
      set L_QLTCT = SUBSTRING (L_PRDCD,7,1);
      select count(*) into L_RECCT from spldata.FG_WKDCL where  WK_CMPCD = LP_CMPCD and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
       if L_RECCT = 0 then
          insert into spldata.fg_wkdcl (wk_cmpcd,wk_prdct,wk_qltct,wk_prdcd) values (lp_cmpcd,L_PRDCT, L_QLTCT, L_PRDCD);
       end if;
       update spldata.fg_wkdcl set wk_cloqt =  isnull(WK_CLOQT,0) +isnull( L_CLOQT,0) where  wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
       set END_TABLE = 0;
       fetch C_CLO into  L_PRDCD, L_CLOQT;
  end while;
  close C_CLO; 
  commit;

 set END_TABLE = 0;
 open C_CLT;
  fetch C_CLT into  L_PRDCD, L_CLTQT;
  while END_TABLE = 0 DO 
      set L_PRDCT = SUBSTRING (L_PRDCD,1,4);
      set L_QLTCT = SUBSTRING (L_PRDCD,7,1);
      select count(*) into L_RECCT from spldata.FG_WKDCL where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
       if L_RECCT = 0 then
          insert into spldata.fg_wkdcl (wk_cmpcd,wk_prdct,wk_qltct,wk_prdcd) values (lp_cmpcd,L_PRDCT, L_QLTCT, L_PRDCD);
       end if;
       update spldata.fg_wkdcl set wk_cltqt =  isnull(WK_CLTQT,0) + L_CLTQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
       set END_TABLE = 0;
       fetch C_CLT into  L_PRDCD, L_CLTQT;
  end while;
  close C_CLT; 
  commit;

 set END_TABLE = 0;
 open C_DSO;
  fetch C_DSO into  L_PRDCD,L_SALTP,L_SLRFL, L_DSOQT;
  while END_TABLE = 0 DO 
      set L_PRDCT = SUBSTRING (L_PRDCD,1,4);
      set L_QLTCT = SUBSTRING (L_PRDCD,7,1);
      select count(*) into L_RECCT from spldata.FG_WKDCL where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
       if L_RECCT = 0 then
          insert into spldata.fg_wkdcl (wk_cmpcd,wk_prdct,wk_qltct,wk_prdcd) values (lp_cmpcd,L_PRDCT, L_QLTCT, L_PRDCD);
       end if;
       if L_SALTP = '12' and L_SLRFL<>'2'  then 
          update spldata.fg_wkdcl set wk_deoqt =  isnull(WK_DEOQT,0) + L_DSOQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;
       if L_SALTP = '16' and L_SLRFL<>'2'  then 
          update spldata.fg_wkdcl set wk_dcoqt =  isnull(WK_DCOQT,0) + L_DSOQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;
       if L_SALTP in ( '01','03','05') and L_SLRFL<>'2'  then 
          update spldata.fg_wkdcl set wk_ddoqt =  isnull(WK_DDOQT,0) + L_DSOQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;

       if  L_SLRFL='2'  then 
          update spldata.fg_wkdcl set wk_dSRQT =  isnull(WK_DSRQT,0) + L_DSOQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;
       set END_TABLE = 0;
       fetch C_DSO into  L_PRDCD,L_SALTP,L_SLRFL, L_DSOQT;
  end while;
  close C_DSO; 
  commit;


 set END_TABLE = 0;
 open C_DST;
  fetch C_DST into  L_PRDCD,L_SALTP,L_SLRFL, L_DSTQT;
  while END_TABLE = 0 DO 
      set L_PRDCT = SUBSTRING (L_PRDCD,1,4);
      set L_QLTCT = SUBSTRING (L_PRDCD,7,1);
      if SUBSTRING (L_PRDCD,1,2) in ('52','54') then
         set L_QLTCT = '0';
      end if;
      select count(*) into L_RECCT from spldata.FG_WKDCL where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
       if L_RECCT = 0 then
          insert into spldata.fg_wkdcl (wk_cmpcd,wk_prdct,wk_qltct,wk_prdcd) values (lp_cmpcd,L_PRDCT, L_QLTCT, L_PRDCD);
       end if;
       if L_SALTP = '12' and L_SLRFL<>'2'  then 
          update spldata.fg_wkdcl set wk_deTQT =  isnull(WK_DETQT,0) + L_DSTQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;
       if L_SALTP = '16' and L_SLRFL<>'2'  then 
          update spldata.fg_wkdcl set wk_dcTQT =  isnull(WK_DCTQT,0) + L_DSTQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;
       if L_SALTP in ( '01','03','05') and L_SLRFL<>'2'  then 
          update spldata.fg_wkdcl set wk_ddTQT =  isnull(WK_DDTQT,0) + L_DSTQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;

       if  L_SLRFL='2'  then 
          update spldata.fg_wkdcl set wk_dSRQT =  isnull(WK_DSRQT,0) + L_DSTQT where wk_cmpcd = lp_cmpcd and WK_PRDCT = L_PRDCT and WK_QLTCT = L_QLTCT and WK_PRDCD = L_PRDCD;
      end if;

       set END_TABLE = 0;
       fetch C_DST into  L_PRDCD,L_SALTP,L_SLRFL, L_DSTQT;
  end while;
  close C_DST; 
  commit;

  update spldata.FG_WKDCL a set WK_PRDDS = (select b.PR_PRDDS from spldata.CO_PRMST b where a.WK_PRDCD = b.PR_PRDCD);
  commit;

end P1;


;

commit;
delete from spldata.fg_wkdcl;
commit;
call spldata.crtFG_WKDCL('03/31/2008');
commit;

select * from spldata.fg_wkdcl order by wk_prdct,wk_qltct;



select * from spldata.co_cdtrn where cmt_cgstp='COXXPGR';

;
drop table spldata.fg_wkdcl;
commit;

create table spldata.fg_wkdcl (
wk_cmpcd varchar(2),
wk_prdct varchar(4),
wk_qltct varchar(1),
wk_prdcd varchar(10),
wk_prdds varchar(40),
wk_cloqt decimal(10,3),
wk_cltqt decimal(10,3),
wk_ddoqt decimal(10,3),
wk_deoqt decimal(10,3),
wk_dcoqt decimal(10,3),
wk_ddtqt decimal(10,3),
wk_detqt decimal(10,3),
wk_dctqt decimal(10,3),
wk_dsrqt decimal(10,3));
commit;


select  IVT_PRDCD, IVT_SALTP,isnull(IVT_SLRFL,' ') IVT_SLRFL, sum(isnull(IVT_INVQT,0)) IVT_INVQT from spldata.MR_IVTRN where  IVT_MKTTP in ('01','03','04','05') and CONVERT(varchar,ivt_invdt,101)  between CONVERT(varchar,'03/01/2008',101) and  CONVERT(varchar,'03/31/2008',101)  group by IVT_PRDCD, IVT_SALTP,IVT_SLRFL  order by  IVT_PRDCD;