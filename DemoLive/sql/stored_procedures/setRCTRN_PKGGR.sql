
-- procedure to set package group (RCT_PKGGR) in FG_RCTRN
-- This group is used for summing up No. of packages during Auto Issue Note Generation

-- PRCEDURE WORKING
--  Company Code, Receipt No. are accepted as parameter
--  If Packing Group is not arrived at, content of PKGGR corresponding record is left blank (null)
--  (which should be verified in calling program before summing up Package Nos.)

drop procedure spldata.setLWMST_PKGGR;
commit;

create procedure spldata.setLWMST_PKGGR(IN LP_CMPCD char(2),IN LP_RCTNO char(8))  language sql  modifies sql data 
P1:
begin
  declare L_PKGGR char(20);
  declare L_PRDTP char(2);
  declare L_LOTNO char(8);
  declare L_RCLNO char(2);
  declare L_RCTTP char(2);
  declare L_PKGTP char(2);
  declare L_PRDCD char(10);
  declare L_PRDCT char(4);
  declare L_MCHNO char(2);
  declare L_TAGNO char(10);
  declare END_TABLE int default 0;

  declare C_LWM  cursor for  select  distinct LW_PRDTP, LW_LOTNO, LW_RCLNO, LW_RCTTP, LW_PKGTP,LW_MCHNO from spldata.FG_LWMST where LW_CMPCD = LP_CMPCD and LW_RCTNO = LP_RCTNO;

  declare continue handler for not found 
       set END_TABLE = 1; 
open C_LWM;
set END_TABLE = 0;
fetch C_LWM  into  L_PRDTP, L_LOTNO, L_RCLNO, L_RCTTP, L_PKGTP,L_MCHNO;

while END_TABLE = 0 DO 
      set L_PKGGR = '';
       select LT_PRDCD into L_PRDCD from spldata.PR_LTMST where LT_PRDTP= L_PRDTP and LT_LOTNO = L_LOTNO and LT_RCLNO = L_RCLNO;
       select cmt_chp01 into L_TAGNO from spldata.co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp = 'FGXXMCH' and cmt_codcd = L_MCHNO;
       set L_PRDCT = SUBSTRING(L_PRDCD,1,4);
       -- Fresh bagging and Re-packing listed under type = '10';
       if L_RCTTP = '15' then
          set L_RCTTP = '10';
       end if;
       -- Job work receipts '21' and '22 listed under type = '21';
       if L_RCTTP = '22' then
          set L_RCTTP = '21';
       end if;
       if L_PKGTP = '01' and L_PRDCT in ('5111','5112','5195') then
          set L_PKGGR = SUBSTRING(L_PKGTP,1,1) + '_' + L_PRDCT + '_' + L_RCTTP + '_' + L_TAGNO;
       end if;
       if L_PKGTP = '01' and SUBSTRING(L_PRDCT,1,2) in ('52','54') then
          set L_PKGGR = SUBSTRING(L_PKGTP,1,1) + '_52XX_' + L_RCTTP + '_' + L_TAGNO;
       end if;
       if SUBSTRING(L_PKGTP,1,1) = '1'  then
          set L_PKGGR = SUBSTRING(L_PKGTP,1,1) + '_XXXX_XX_' + L_TAGNO;
       end if;

       update spldata.fg_lwmst set LW_PKGGR = L_PKGGR where LW_PRDTP = L_PRDTP and LW_LOTNO = L_LOTNO and LW_RCLNO = L_RCLNO and LW_RCTTP = L_RCTTP and LW_PKGTP = L_PKGTP and LW_MCHNO = L_MCHNO;

        set END_TABLE = 0;
       fetch C_LWM  into  L_PRDTP, L_LOTNO, L_RCLNO, L_RCTTP, L_PKGTP,L_MCHNO;
end while;
close C_LWM;
commit;
end P1;

commit;

call spldata.setLWMST_PKGGR('01','91001223');
commit;
