-- procedure to create a Work Table for Lotwise Bagging Despatch detail

drop procedure spldata.crtPR_WKLBD;
commit;


create procedure spldata.crtPR_WKLBD(IN LP_CMPCD char(2),IN LP_PRDCD char(10),IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 
 P1:

begin
  declare L_LOTNO char(8);
  declare L_DOCDT date;
  declare L_DOCQT decimal(12,3);
  declare END_TABLE int default 0;
  declare L_RECCT int default 0;

  declare C_RCT cursor for  select  RCT_LOTNO,RCT_RCTDT,sum(RCT_RCTQT) from spldata.FG_RCTRN where  RCT_CMPCD = LP_CMPCD and RCT_RCTTP in ('10','15') and rct_stsfl='2' and rct_rctdt between LP_STRDT and LP_ENDDT and rct_prdtp + rct_lotno + rct_rclno in (select lt_prdtp + lt_lotno + lt_rclno from spldata.pr_ltmst where lt_prdcd = LP_PRDCD) group by RCT_LOTNO,RCT_RCTDT;

  declare C_ISS cursor for  select  IST_LOTNO,IST_ISSDT,sum(IST_ISSQT) from spldata.FG_ISTRN where  IST_CMPCD = LP_CMPCD and IST_ISSTP in ('10') and ist_stsfl='2' and ist_issdt between LP_STRDT and LP_ENDDT and ist_prdcd = LP_PRDCD  group by IST_LOTNO,IST_ISSDT;


  declare continue handler for not found 
       set END_TABLE = 1; 
delete from spldata.PR_WKLBD1;
commit;

 set END_TABLE = 0;
 open C_RCT;
  fetch C_RCT into  L_LOTNO, L_DOCDT, L_DOCQT;
  while END_TABLE = 0 DO 
      select count(*) into L_RECCT from spldata.PR_WKLBD1 where  WK_PRDCD = LP_PRDCD and WK_LOTNO = L_LOTNO;
       if L_RECCT = 0 then
          insert into spldata.pr_wklbd1 (wk_cmpcd,wk_prdcd,wk_docdt,wk_lotno) values (lp_cmpcd,lp_prdcd, l_docdt, l_lotno);
       end if;
       update spldata.pr_wklbd1 set wk_rctqt =  l_docqt where  wk_cmpcd = lp_cmpcd and wk_prdcd = lp_prdcd and wk_docdt = l_docdt and wk_lotno = l_lotno;
       set END_TABLE = 0;
       fetch C_RCT into  L_LOTNO, L_DOCDT, L_DOCQT;
  end while;
  close C_RCT; 
  commit;

 set END_TABLE = 0;
 open C_ISS;
  fetch C_ISS into  L_LOTNO, L_DOCDT, L_DOCQT;
  while END_TABLE = 0 DO 
       select count(*) into L_RECCT from spldata.PR_WKLBD1 where  WK_PRDCD = LP_PRDCD and WK_LOTNO = L_LOTNO;
       if L_RECCT = 0 then
          insert into spldata.pr_wklbd1 (wk_cmpcd,wk_prdcd,wk_docdt,wk_lotno) values (lp_cmpcd,lp_prdcd, l_docdt, l_lotno);
       end if;
       update spldata.pr_wklbd1 set wk_issqt =  l_docqt where  wk_cmpcd = lp_cmpcd and wk_prdcd = lp_prdcd and wk_docdt = l_docdt and wk_lotno = l_lotno;
       set END_TABLE = 0;
       fetch C_ISS into  L_LOTNO, L_DOCDT, L_DOCQT;
  end while;
  close C_ISS; 
  commit;

end P1;
commit;


select * from spldata.co_prmst where pr_prdds = 'SC201E';

select * from spldata.fg_istrn where ist_cmpcd='11';
call spldata.crtPR_WKLBD('01','5112150150','12/01/2008','12/31/2008');
commit;
