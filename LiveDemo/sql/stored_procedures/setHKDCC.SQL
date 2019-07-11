-- Procedure to revert issue status to previous day and releasing the Stock (From Day Opening Record)
-- where material is loaded in truck but is yet to be despatched (pending for invoice preparation)
-- Releasing of expired reservation is also taken care in this procedure
-- The procedure is called during Day-end closing operation (fg_hkdcc.java)

drop procedure spldata.setHKDCC;
commit;



create procedure spldata.setHKDCC(IN LP_CMPCD char(2),IN LP_LCKDT char(10), IN LP_NEWTM char(19))  language sql  modifies sql data 
 P1:
begin
  declare L_IST_CMPCD char(2);
  declare L_IST_WRHTP char(2);
  declare L_IST_LOTNO char(8);
  declare L_IST_RCLNO char(2);
  declare L_IST_PKGTP char(2);
  declare L_IST_MNLCD char(5);
  declare L_IST_ISSQT decimal(10,3);
  declare END_TABLE int default 0;

  declare C_ISTRN cursor for  Select IST_WRHTP, IST_LOTNO, IST_RCLNO, IST_PKGTP, IST_MNLCD, isnull(IST_ISSQT,0) IST_ISSQT  from spldata.FG_ISTRN where IST_CMPCD + IST_ISSNO + IST_PRDCD + IST_PKGTP in ( select IVT_CMPCD + IVT_LADNO + IVT_PRDCD + IVT_PKGTP from spldata.mr_ivtrn where ivt_cmpcd = LP_CMPCD and CONVERT(varchar,ivt_loddt,101) <= LP_LCKDT and isnull(ivt_ladqt,0) > isnull(ivt_invqt,0) ) and ist_stsfl='2' and ist_isstp in ('10','30');



  declare continue handler for not found 
       set END_TABLE = 1; 

insert into spldata.mr_ivbak select * from spldata.mr_ivtrn where ivt_cmpcd = LP_CMPCD and CONVERT(varchar,ivt_loddt,101) <= lp_lckdt and isnull(ivt_ladqt,0) > isnull(ivt_invqt,0);
commit;
 
  --update spldata.fg_stmst set st_dosqt = st_stkqt, st_douqt = st_uclqt;
  --commit;


  open C_ISTRN ;
  fetch C_ISTRN into  L_IST_WRHTP, L_IST_LOTNO, L_IST_RCLNO, L_IST_PKGTP, L_IST_MNLCD, L_IST_ISSQT;
  while END_TABLE = 0 DO 
      update spldata.FG_STMST set ST_DOSQT = isnull(ST_DOSQT,0)+L_IST_ISSQT where ST_CMPCD = LP_CMPCD and ST_WRHTP = L_IST_WRHTP and ST_LOTNO = L_IST_LOTNO and ST_RCLNO = L_IST_RCLNO and ST_PKGTP = L_IST_PKGTP and ST_MNLCD = L_IST_MNLCD;
  fetch C_ISTRN into  L_IST_WRHTP, L_IST_LOTNO, L_IST_RCLNO, L_IST_PKGTP, L_IST_MNLCD, L_IST_ISSQT;
  end while;

  close C_ISTRN; 
  commit;

  update spldata.fg_istrn set ist_autdt = date(days(date(LP_LCKDT))+1)  where IST_CMPCD + IST_ISSNO + IST_PRDCD + IST_PKGTP in ( select IVT_CMPCD + IVT_LADNO + IVT_PRDCD + IVT_PKGTP from  spldata.mr_ivtrn where ivt_cmpcd = LP_CMPCD and date(ivt_loddt) <= LP_LCKDT and isnull(ivt_ladqt,0)>isnull(ivt_invqt,0) ) and ist_stsfl='2' and ist_isstp in ('10','30');
  update spldata.mr_ivtrn set ivt_loddt = lp_newtm where ivt_cmpcd = LP_CMPCD and date(ivt_loddt) <= LP_LCKDT and isnull(ivt_ladqt,0)>isnull(ivt_invqt,0);
  commit;
  
  -- Releasing Expired Reservations from Reservation Transactions and Lot Master
  update spldata.fg_srtrn set sr_stsfl='X' where sr_cmpcd = LP_CMPCD and sr_enddt<current_date;
  commit;

  update spldata.pr_ltmst set lt_resqt = isnull(lt_rdsqt,0)  where lt_cmpcd = LP_CMPCD and (isnull(lt_resqt,0)-isnull(lt_rdsqt,0))>0 and lt_lotno not in (select sr_lotno from spldata.fg_srtrn where sr_cmpcd = LP_cmpcd and sr_stsfl<>'X'  and isnull(sr_resqt,0)-isnull(sr_rdsqt,0)>0);
  commit;

end P1;

commit;



call spldata.setHKDCC('02/27/2007','2007-02-28-07.01.01');
commit;
