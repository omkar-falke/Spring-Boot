-- This trigger is written for passing the loaded vehicle for Invoice Preparation.
-- Whenever a loading authorisation is executed on issue record, It is confirmed that no more issue records from same vehicle (same security number) are pending for Authorisation
-- If all the issues (Loading Advices) from the Lorry are authorised, all the L.A. belonging to that Lorry are marked as authorised for Invoice Preparation.
-- The trigger also updates allocated / despatched quantity in Stock Master, Reservation table and in Lot Master.


commit;

drop trigger spldata.insFG_ISTRN;
commit;


create trigger spldata.insFG_ISTRN after insert on spldata.FG_ISTRN referencing 
new as new_row for each row mode DB2ROW 
begin 
   declare L_ist_issqt decimal(10,3);
   declare L_ist_aloqt decimal(10,3);
   declare L_ist_rdsqt decimal(10,3);
   declare L_ist_ralqt decimal(10,3);
   declare L_RECCT int default 0;
   declare L_ISSNO char(8);
   declare L_ISSTP char(2);
   declare L_GINNO char(8);
   declare L_CMPCD char(2);

 if new_row.ist_stsfl='2' and new_row.ist_isstp in ('10','30')  then 
    select distinct ifnull(IVT_CMPCD,''),ifnull(IVT_GINNO,'') into L_CMPCD,L_GINNO from spldata.MR_IVTRN where IVT_CMPCD = new_row.IST_CMPCD and IVT_LADNO = new_row.IST_ISSNO;
    update spldata.mr_ivtrn set ivt_stsfl='2' where ivt_cmpcd=new_row.ist_cmpcd and ivt_mkttp=new_row.ist_mkttp and ivt_ladno = new_row.ist_issno and ivt_prdcd = new_row.ist_prdcd and ivt_pkgtp = new_row.ist_pkgtp and ivt_stsfl not in ('X','2','L','D');
   if new_row.ist_mkttp <>   '03' then
            select count(*) into L_RECCT from spldata.MR_IVTRN where IVT_CMPCD = L_CMPCD and IVT_GINNO = L_GINNO and ifnull(IVT_STSFL,'X') not in ('2','X','L','D') and IVT_MKTTP||IVT_LADNO||IVT_PRDCD||IVT_PKGTP <> new_row.IST_MKTTP||new_row.IST_ISSNO||new_row.IST_PRDCD||new_row.IST_PKGTP;
            if L_RECCT = 0 then
                update spldata.mr_ivtrn set ivt_stsfl='L' where ivt_cmpcd=L_CMPCD and ivt_ginno=L_GINNO and ivt_stsfl not in ('X','D');
            end if;
    end if;
   if new_row.ist_mkttp =   '03' then
            select count(*)  into L_RECCT from spldata.MR_IVTRN where IVT_CMPCD = new_row.IST_CMPCD and IVT_LADNO = new_row.IST_ISSNO and IVT_STSFL not in ('2','X','L','D') and IVT_MKTTP||IVT_LADNO||IVT_PRDCD||IVT_PKGTP <> new_row.IST_MKTTP||new_row.IST_ISSNO||new_row.IST_PRDCD||new_row.IST_PKGTP;
            if L_RECCT = 0 then
                update spldata.mr_ivtrn set ivt_stsfl='L' where ivt_cmpcd=new_row.IST_CMPCD and ivt_ladno=new_row.IST_ISSNO and ivt_stsfl not in ('X','D');
            end if;
    end if;


   set L_ist_issqt = new_row.ist_issqt;
   if (new_row.ist_resfl = '1') then 
       set L_ist_rdsqt = new_row.ist_issqt;
   end if;

    update spldata.fg_stmst set st_stkqt = ifnull(st_stkqt,0) - new_row.ist_issqt where st_cmpcd = new_row.ist_cmpcd and st_wrhtp = new_row.ist_wrhtp and st_prdtp=new_row.ist_prdtp and st_lotno = new_row.ist_lotno and st_rclno = new_row.ist_rclno and st_pkgtp = new_row.ist_pkgtp and st_mnlcd = new_row.ist_mnlcd;

    update spldata.fg_lcmst set lc_stkqt = lc_stkqt - L_ist_issqt where lc_cmpcd = new_row.ist_cmpcd and lc_mnlcd = new_row.ist_mnlcd;
    update spldata.pr_ltmst set lt_dspqt = lt_dspqt + L_ist_issqt where lt_cmpcd = new_row.ist_cmpcd and lt_prdtp = new_row.ist_prdtp and lt_lotno = new_row.ist_lotno and lt_rclno = new_row.ist_rclno;
   if L_ist_rdsqt > 0 then 
    update spldata.pr_ltmst set  lt_rdsqt = ifnull(lt_rdsqt,0) + L_ist_rdsqt  where lt_cmpcd = new_row.ist_cmpcd and lt_prdtp = new_row.ist_prdtp and lt_lotno = new_row.ist_lotno and lt_rclno = new_row.ist_rclno;
      update spldata.fg_srtrn set sr_rdsqt = ifnull(sr_rdsqt,0) + L_ist_rdsqt  where sr_cmpcd = new_row.ist_cmpcd and sr_prdtp = new_row.ist_prdtp and sr_lotno = new_row.ist_lotno and sr_rclno = new_row.ist_rclno and sr_prttp = 'C' and sr_grpcd in (select distinct  ivt_grpcd from spldata.mr_ivtrn where ivt_cmpcd = new_row.ist_cmpcd and ivt_ladno = new_row.ist_issno);
   end if;
 end if;


 if (new_row.ist_stsfl='1'  and new_row.ist_isstp in ('10','30')) then
     set L_ist_aloqt = new_row.ist_issqt;
      if  new_row.ist_resfl='1' then
          set L_ist_ralqt = new_row.ist_issqt;
      end if;
    update spldata.fg_stmst set st_aloqt = ifnull(st_aloqt,0) + new_row.ist_issqt where st_cmpcd = new_row.ist_cmpcd and st_wrhtp = new_row.ist_wrhtp and st_prdtp=new_row.ist_prdtp and st_lotno = new_row.ist_lotno and st_rclno = new_row.ist_rclno and st_pkgtp = new_row.ist_pkgtp and st_mnlcd = new_row.ist_mnlcd;

    if L_ist_ralqt > 0 then
      update spldata.pr_ltmst set  lt_ralqt = ifnull(lt_ralqt,0) + L_ist_ralqt  where lt_cmpcd = new_row.ist_cmpcd and lt_prdtp = new_row.ist_prdtp and lt_lotno = new_row.ist_lotno and lt_rclno = new_row.ist_rclno;
      update spldata.fg_srtrn set sr_ralqt = ifnull(sr_ralqt,0) + L_ist_ralqt where sr_cmpcd = new_row.ist_cmpcd and sr_prdtp = new_row.ist_prdtp and sr_lotno = new_row.ist_lotno and sr_rclno = new_row.ist_rclno and sr_prttp = 'C' and sr_grpcd in  (select distinct ivt_grpcd from spldata.mr_ivtrn where ivt_cmpcd = new_row.ist_cmpcd and ivt_ladno = new_row.ist_issno);
    end if;

end if;

end;
commit;

commit;

    select distinct ifnull(IVT_GINNO,'') from spldata.MR_IVTRN where IVT_LADNO = '80102212';
    update spldata.mr_ivtrn set ivt_stsfl='2' where ivt_mkttp=new_row.ist_mkttp and ivt_ladno = new_row.ist_issno and ivt_prdcd = new_row.ist_prdcd and ivt_pkgtp = new_row.ist_pkgtp and ivt_stsfl not in ('X','2','L','D');

select ivt_grpcd from spldata.mr_ivtrn where ivt_ladno ='80102212';


