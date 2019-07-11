drop trigger spldata.updFG_ISTRN;
commit;


create trigger spldata.updFG_ISTRN  after update of ist_stsfl on spldata.FG_ISTRN referencing new as new_row old as old_row 
 for each row mode DB2ROW 
 begin 

 declare L_RECCT int default 0;
 declare L_ISSNO char(8);
 declare L_ISSTP char(2);
 declare L_CMPCD char(2);
 declare L_GINNO char(8);
 declare L_ISSQT decimal(10,3);
 declare L_ALOQT decimal(10,3);

 -- This part of the trigger is for Authorising the Issue directly or after allocation
 if (new_row.ist_stsfl='2' ) and new_row.ist_isstp in ('10','30') then
    set L_ISSQT = 0.000;
    set L_ALOQT = 0.000;

    if (old_row.ist_stsfl='1') then
       set L_ISSQT = new_row.ist_issqt;
       set L_ALOQT = old_row.ist_issqt;
    end if;
    if (old_row.ist_stsfl='2') then
       set L_ISSQT = new_row.ist_issqt - old_row.ist_issqt;
    end if;

    update spldata.fg_stmst set st_stkqt = st_stkqt - L_ISSQT , st_aloqt = ifnull(st_aloqt,0) - L_ALOQT where st_cmpcd = old_row.ist_cmpcd and st_wrhtp = old_row.ist_wrhtp and st_prdtp=old_row.ist_prdtp and  st_lotno = old_row.ist_lotno and st_rclno = old_row.ist_rclno and st_pkgtp = old_row.ist_pkgtp and st_mnlcd = old_row.ist_mnlcd and ifnull(st_stkqt,0)>=L_ISSQT;
    update spldata.fg_lcmst set lc_stkqt = lc_stkqt - L_ISSQT where lc_cmpcd = old_row.ist_cmpcd and lc_mnlcd = old_row.ist_mnlcd;
    update spldata.pr_ltmst set lt_dspqt = lt_dspqt + L_ISSQT  where lt_cmpcd = old_row.ist_cmpcd and lt_prdtp = old_row.ist_prdtp and lt_lotno = old_row.ist_lotno and lt_rclno = old_row.ist_rclno;
   if  old_row.ist_resfl='1' then
       update spldata.pr_ltmst set lt_rdsqt = ifnull(lt_rdsqt,0) + L_ISSQT, lt_ralqt = ifnull(lt_ralqt,0) - L_ALOQT  where lt_cmpcd = old_row.ist_cmpcd  and lt_prdtp = old_row.ist_prdtp and lt_lotno = old_row.ist_lotno and lt_rclno = old_row.ist_rclno;
       update spldata.fg_srtrn set sr_rdsqt = ifnull(sr_rdsqt,0) + L_ISSQT, sr_ralqt = ifnull(sr_ralqt,0) - L_ALOQT  where sr_cmpcd = old_row.ist_cmpcd and sr_prdtp = old_row.ist_prdtp and sr_lotno = old_row.ist_lotno and sr_rclno = old_row.ist_rclno and sr_prttp = 'C' and sr_grpcd in  (select distinct  ivt_grpcd from spldata.mr_ivtrn where ivt_cmpcd = old_row.ist_cmpcd and ivt_ladno = old_row.ist_issno);
   end if;
    select distinct ifnull(IVT_CMPCD,''),ifnull(IVT_GINNO,'') into L_CMPCD,L_GINNO from spldata.MR_IVTRN where IVT_CMPCD = new_row.IST_CMPCD and IVT_LADNO = new_row.IST_ISSNO;
    update spldata.mr_ivtrn set ivt_stsfl='2' where ivt_cmpcd=new_row.ist_cmpcd and ivt_mkttp=new_row.ist_mkttp and ivt_ladno = new_row.ist_issno and ivt_prdcd = new_row.ist_prdcd and ivt_pkgtp = new_row.ist_pkgtp and ivt_stsfl not in ('X','2','L','D');
   if new_row.ist_mkttp <>   '03' then
            select count(*) into L_RECCT from spldata.MR_IVTRN where IVT_CMPCD = L_CMPCD and IVT_GINNO = L_GINNO and ifnull(IVT_STSFL,'1') not in ('2','X','L','D') and IVT_MKTTP||IVT_LADNO||IVT_PRDCD||IVT_PKGTP <> new_row.IST_MKTTP||new_row.IST_ISSNO||new_row.IST_PRDCD||new_row.IST_PKGTP;
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

end if;

 -- This part of the trigger is for Modification in allocated issue
 if (new_row.ist_stsfl='1' and old_row.ist_stsfl='1') then
         update spldata.fg_stmst set  st_aloqt = ifnull(st_aloqt,0) + (new_row.ist_issqt-old_row.ist_issqt)  where st_cmpcd = old_row.ist_cmpcd and st_wrhtp = old_row.ist_wrhtp and st_prdtp=old_row.ist_prdtp and st_lotno = old_row.ist_lotno and st_rclno = old_row.ist_rclno and st_pkgtp = old_row.ist_pkgtp and st_mnlcd = old_row.ist_mnlcd;
      if  old_row.ist_resfl='1' then
         update spldata.pr_ltmst set  lt_ralqt = ifnull(lt_ralqt,0) + (new_row.ist_issqt-old_row.ist_issqt)  where lt_cmpcd = old_row.ist_cmpcd and lt_prdtp = old_row.ist_prdtp and lt_lotno = old_row.ist_lotno and lt_rclno = old_row.ist_rclno;
         update spldata.fg_srtrn set sr_ralqt = ifnull(sr_ralqt,0) + (new_row.ist_issqt-old_row.ist_issqt) where sr_cmpcd = old_row.ist_cmpcd and sr_prdtp = old_row.ist_prdtp and sr_lotno = old_row.ist_lotno and sr_rclno = old_row.ist_rclno and sr_prttp = 'C' and sr_grpcd in (select distinct  ivt_grpcd from spldata.mr_ivtrn where ivt_cmpcd = old_row.ist_cmpcd and ivt_ladno = old_row.ist_issno);
      end if;
 end if;


 -- This part of the trigger is for Deletion of allocated issue
 if (new_row.ist_stsfl='X' and old_row.ist_stsfl='1') then
         update spldata.fg_stmst set  st_aloqt = ifnull(st_aloqt,0) - old_row.ist_issqt  where st_cmpcd = old_row.ist_cmpcd and st_wrhtp = old_row.ist_wrhtp and st_prdtp=old_row.ist_prdtp and st_lotno = old_row.ist_lotno and st_rclno = old_row.ist_rclno and st_pkgtp = old_row.ist_pkgtp and st_mnlcd = old_row.ist_mnlcd;
      if  old_row.ist_resfl='1' then
         update spldata.pr_ltmst set  lt_ralqt = ifnull(lt_ralqt,0) - old_row.ist_issqt  where lt_cmpcd = old_row.ist_cmpcd and lt_prdtp = old_row.ist_prdtp and lt_lotno = old_row.ist_lotno and lt_rclno = old_row.ist_rclno;
         update spldata.fg_srtrn set sr_ralqt = ifnull(sr_ralqt,0) - old_row.ist_issqt  where sr_cmpcd = old_row.ist_cmpcd and sr_prdtp = old_row.ist_prdtp and sr_lotno = old_row.ist_lotno and sr_rclno = old_row.ist_rclno and sr_prttp = 'C' and sr_grpcd  in  (select distinct  ivt_grpcd from spldata.mr_ivtrn where ivt_cmpcd = old_row.ist_cmpcd and ivt_ladno = old_row.ist_issno);
      end if;
 end if;


 -- This part of the trigger is for Reversal of Authorised Issue
 if (new_row.ist_stsfl='1' and old_row.ist_stsfl='2') then
        set L_ISSQT = old_row.ist_issqt;
         update spldata.fg_stmst set  st_aloqt = ifnull(st_aloqt,0) + L_ISSQT, st_stkqt = ifnull(st_stkqt,0) + L_ISSQT  where st_cmpcd = old_row.ist_cmpcd and st_wrhtp = old_row.ist_wrhtp and st_prdtp=old_row.ist_prdtp and st_lotno = old_row.ist_lotno and st_rclno = old_row.ist_rclno and st_pkgtp = old_row.ist_pkgtp and st_mnlcd = old_row.ist_mnlcd;
      if  old_row.ist_resfl='1' then
         update spldata.pr_ltmst set  lt_ralqt = ifnull(lt_ralqt,0) + L_ISSQT, lt_rdsqt = ifnull(lt_rdsqt,0) -  L_ISSQT  where lt_cmpcd = old_row.ist_cmpcd and lt_prdtp = old_row.ist_prdtp and lt_lotno = old_row.ist_lotno and lt_rclno = old_row.ist_rclno;
         update spldata.fg_srtrn set sr_ralqt = ifnull(sr_ralqt,0) + L_ISSQT, sr_rdsqt = ifnull(sr_rdsqt,0) - L_ISSQT where sr_cmpcd = old_row.ist_cmpcd and sr_prdtp = old_row.ist_prdtp and sr_lotno = old_row.ist_lotno and sr_rclno = old_row.ist_rclno and sr_prttp = 'C' and sr_grpcd in (select  distinct  ivt_grpcd from spldata.mr_ivtrn where ivt_cmpcd = old_row.ist_cmpcd and ivt_ladno = old_row.ist_issno);
      end if;

 end if;
end;

commit;

select * from spldata.fg_srtrn;

select * from spldata.fg_istrn where ist_resfl='1';

