
-- procedure to set package group (LW_PKGGR) in FG_LWMST
-- This group is used for summing up No. of packages during Auto Issue Note Generation

-- PRCEDURE WORKING
--  Company Code, Receipt No. are accepted as parameter
--  If Packing Group is not arrived at, content of PKGGR corresponding record is left blank (null)
--  (which should be verified in calling program before summing up Package Nos.)

drop procedure spldata.setLWMST_PKGGR;
commit;

create procedure spldata.setLWMST_PKGGR(IN LP_CMPCD char(2), IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 
P1:
begin
  declare L_PKGGR char(14);
  declare L_PKGGR1 char(6);
  declare L_PRDTP char(2);
  declare L_LOTNO char(8);
  declare L_RCLNO char(2);
  declare L_PKGTP char(2);
  declare L_PRDCD char(10);
  declare L_MATCD char(10);
  declare L_MATCD1 char(10);
  declare L_PRDCT char(4);
  declare L_MCHNO char(2);
  declare L_TAGNO char(10);
  declare L_BAGPK int;
  declare L_STKQT decimal(10,3);
  declare L_PPORT decimal(10,2);
  declare END_TABLE int default 0;

  declare C_LWM  cursor for  select  distinct LW_PRDTP, LW_LOTNO, LW_RCLNO, LW_PKGTP,LW_MCHNO from spldata.FG_LWMST where LW_CMPCD = LP_CMPCD  and LW_RCTTP in ('10','15')   and isnull(LW_ISSRF,'') = ''  and lw_stsfl = '2' and lw_rctdt between  CONVERT(varchar,LP_STRDT,101) and CONVERT(varchar,LP_ENDDT,101);

  declare C_PKG  cursor for  select  SUBSTRING(LW_PKGGR,1,6) LW_PKGGR1, sum(LW_BAGPK) from spldata.FG_LWMST where LW_CMPCD = LP_CMPCD and LW_RCTTP in ('10','15')   and isnull(LW_ISSRF,'') = ''  and lw_stsfl = '2' and lw_rctdt between  CONVERT(varchar,LP_STRDT,101) and CONVERT(varchar,LP_ENDDT,101)  group by SUBSTRING(LW_PKGGR,1,6);

 declare C_MAT  cursor for select st_matcd,ct_pport,st_stkqt from spldata.mm_stmst,spldata.co_ctmst where st_matcd = ct_matcd and st_cmpcd = LP_CMPCD and ct_matcd in (select CMT_CHP01 from spldata.co_cdtrn where cmt_cgmtp = 'S' + LP_CMPCD and cmt_cgstp = 'FGXXPKD' and SUBSTRING(cmt_codcd,1,6) = L_PKGGR1) order by ct_pport desc;

  declare continue handler for not found 
       set END_TABLE = 1; 
open C_LWM;
set END_TABLE = 0;
fetch C_LWM  into  L_PRDTP, L_LOTNO, L_RCLNO, L_PKGTP,L_MCHNO;

while END_TABLE = 0 DO 
      set L_PKGGR = '';
       select isnull(LT_PRDCD,LT_TPRCD) LT_PRDCD into L_PRDCD from spldata.PR_LTMST where LT_PRDTP= L_PRDTP and LT_LOTNO = L_LOTNO and LT_RCLNO = L_RCLNO;
       select cmt_chp01 into L_TAGNO from spldata.co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp = 'FGXXMCH' and cmt_codcd = L_MCHNO;
       set L_PRDCT = SUBSTRING(L_PRDCD,1,4);
       if L_PKGTP = '01' and L_PRDCT in ('5111','5112','5195') then
          set L_PKGGR = SUBSTRING(L_PKGTP,1,1) + '_' + L_PRDCT + '_' + L_TAGNO;
       end if;
       if SUBSTRING(L_PRDCD,5,2) = '95' and SUBSTRING(L_PRDCD,1,2)='51' and SUBSTRING(L_PRDCD,7,2) in ('11','16','17') then
          set L_PRDCT = '5195';
       end if;
       if L_PKGTP = '01' and SUBSTRING(L_PRDCT,1,2) in ('52','54') then
          set L_PKGGR = SUBSTRING(L_PKGTP,1,1) + '_52XX_' + L_TAGNO;
       end if;
       if SUBSTRING(L_PKGTP,1,1) = '1'  then
          set L_PKGGR = SUBSTRING(L_PKGTP,1,1) + '_XXXX_' + L_TAGNO;
       end if;

       update spldata.fg_lwmst set LW_PKGGR = L_PKGGR where LW_PRDTP = L_PRDTP and LW_LOTNO = L_LOTNO and LW_RCLNO = L_RCLNO and  LW_PKGTP = L_PKGTP and LW_MCHNO = L_MCHNO and LW_RCTTP in ('10','15');

        set END_TABLE = 0;
       fetch C_LWM  into  L_PRDTP, L_LOTNO, L_RCLNO, L_PKGTP,L_MCHNO;
end while;
close C_LWM;
commit;

open C_PKG;
set END_TABLE = 0;
fetch C_PKG  into  L_PKGGR1, L_BAGPK;
set L_MATCD1 = '';
while END_TABLE = 0 DO 
       open C_MAT;
       set END_TABLE = 0;
       fetch C_MAT into L_MATCD, L_PPORT, L_STKQT;
       while END_TABLE = 0 DO 
               if L_MATCD1 = '' then
                  if L_STKQT >= L_BAGPK then
                     set L_MATCD1 = L_MATCD;
                  end if;
               end if;
               set END_TABLE = 0;
               fetch C_MAT into L_MATCD, L_PPORT, L_STKQT;
       end while;
       update spldata.fg_lwmst set lw_matcd = L_MATCD1 where LW_CMPCD = LP_CMPCD and LW_RCTTP in ('10','15')   and isnull(LW_ISSRF,'') = ''  and lw_stsfl = '2' and lw_rctdt between  CONVERT(varchar,LP_STRDT,101) and CONVERT(varchar,LP_ENDDT,101)  and SUBSTRING(LW_PKGGR,1,6) = L_PKGGR1;
       close C_MAT;
       set END_TABLE = 0;
       fetch C_PKG  into  L_PKGGR1, L_BAGPK;
       set L_MATCD1 = '';
end while;
close C_PKG;
commit;

end P1;

commit;

call spldata.setLWMST_PKGGR('01','12/08/2008','12/09/2008');
commit;

select * from spldata.fg_lwmst where lw_rctdt between '12/08/2008' and '12/09/2008' and lw_rcttp in ('10','15');

commit;

select  distinct LW_PRDTP, LW_LOTNO, LW_RCLNO, LW_PKGTP,LW_MCHNO from spldata.FG_LWMST where LW_CMPCD = '01'  and LW_RCTTP in ('10','15')   and isnull(LW_ISSRF,'') = ''  and lw_stsfl = '2' and lw_rctdt between  '12/08/2008' and '12/09/2008' ;

select  SUBSTRING(LW_PKGGR,1,6) LW_PKGGR1, sum(LW_BAGPK) from spldata.FG_LWMST where LW_CMPCD = '01' and LW_RCTTP in ('10','15')   and isnull(LW_ISSRF,'') = ''  and lw_stsfl = '2' and lw_rctdt between  '12/08/2008' and '12/09/2008'   group by SUBSTRING(LW_PKGGR,1,6);
