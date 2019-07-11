-- procedure to update despatched quntity for the lot, against reservation
-- Procedure also updates
--  1. Road Permit Form transaction with Invoice details
--  2. sets right Receipt date in Stock Master wherever not updated
-- Procedure is called through mr_qrgrr.java
drop procedure spldata.setDSPQT_RES;
commit;


create procedure spldata.setDSPQT_RES(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_INVNO char(8))  language sql  modifies sql data 

 P1:



begin



declare L_PRDTP char(2);

declare L_RESNO char(8);

declare L_LOTNO char(8);

declare L_RCLNO char(2);

declare L_PRDCD char(10);

declare L_GRPCD char(5);

declare L_ISSQT decimal(10,3);

declare L_RESQT decimal(10,3);

declare L_ENDDT date;

declare L_INVDT date;

declare L_LRYNO char(15);

declare L_BYRCD char(5);

declare L_FRMTP char(2);

declare L_FRMNO char(10);





declare END_TABLE int default 0;



 declare C_LOT cursor for  select lt_prdtp,lt_lotno,lt_rclno,lt_resqt from spldata.pr_ltmst, spldata.fg_istrn,spldata.mr_ivtrn  where lt_cmpcd = lp_cmpcd and lt_cmpcd = ivt_cmpcd and lt_prdtp=ist_prdtp and lt_lotno = ist_lotno and lt_rclno = ist_rclno and ist_mkttp = ivt_mkttp and ist_issno= ivt_ladno and ivt_mkttp = LP_MKTTP and ivt_invno = LP_INVNO and ist_resfl='1';







  declare C_RES cursor for select sr_resno,sr_prdtp,sr_lotno,sr_rclno,sr_prdcd,sr_grpcd,sr_enddt,isnull(sr_resqt,0)   from spldata.fg_srtrn, spldata.fg_istrn,spldata.mr_ivtrn  where sr_cmpcd = lp_cmpcd and sr_cmpcd=ist_cmpcd and ist_cmpcd = ivt_cmpcd and sr_prdtp=ist_prdtp and sr_lotno = ist_lotno and sr_rclno = ist_rclno and ist_mkttp = ivt_mkttp and ist_issno= ivt_ladno and ivt_mkttp = LP_MKTTP and ivt_invno =  LP_INVNO and ist_resfl='1';



  declare continue handler for not found 

       set END_TABLE = 1; 



 open C_LOT ;

  set END_TABLE = 0; 

  fetch C_LOT into  L_PRDTP,L_LOTNO, L_RCLNO,L_RESQT;

  while END_TABLE = 0 DO 



  select sum(IST_ISSQT) into L_ISSQT from spldata.FG_ISTRN where ist_cmpcd = lp_cmpcd and IST_PRDTP = L_PRDTP and IST_LOTNO = L_LOTNO and IST_RCLNO = L_RCLNO and IST_RESFL='1' ;

   if L_ISSQT > L_RESQT then

     set L_ISSQT = L_RESQT;

   end if;

   update spldata.PR_LTMST set LT_RDSQT = L_ISSQT where lt_cmpcd = lp_cmpcd and LT_PRDTP = L_PRDTP and LT_LOTNO = L_LOTNO and LT_RCLNO = L_RCLNO;



  fetch C_LOT into  L_PRDTP,L_LOTNO, L_RCLNO,L_PRDCD;

  end while;

  close C_LOT; 



  set END_TABLE = 0; 

  open C_RES ;

  fetch C_RES into  L_RESNO,L_PRDTP,L_LOTNO,L_RCLNO,L_PRDCD,L_GRPCD,L_ENDDT,L_RESQT;

  while END_TABLE = 0 DO 



  select sum(IST_ISSQT) into L_ISSQT from spldata.FG_ISTRN where  ist_cmpcd = lp_cmpcd and ist_prdtp = L_PRDTP and ist_lotno = L_LOTNO and ist_rclno = L_RCLNO and ist_cmpcd + ist_mkttp  + ist_issno in (select ivt_cmpcd + ivt_mkttp + ivt_ladno from spldata.mr_ivtrn where ivt_cmpcd = lp_cmpcd and ivt_mkttp = LP_MKTTP and ivt_grpcd=L_GRPCD and ivt_prdcd = L_PRDCD and CONVERT(varchar,ivt_invdt,101) <= L_ENDDT) and IST_PRDTP = L_PRDTP and IST_LOTNO=L_LOTNO and IST_RCLNO = L_RCLNO and IST_RESFL='1' ;

   if L_ISSQT > L_RESQT then

     set L_ISSQT = L_RESQT;

   end if;

   update spldata.FG_SRTRN set  SR_RDSQT = L_ISSQT where sr_cmpcd = lp_cmpcd and SR_RESNO = L_RESNO and SR_PRDTP = L_PRDTP and SR_LOTNO = L_LOTNO and SR_RCLNO = L_RCLNO and SR_GRPCD = L_GRPCD;

  fetch C_RES into  L_RESNO,L_PRDTP,L_LOTNO,L_RCLNO,L_PRDCD,L_GRPCD,L_ENDDT,L_RESQT;

  end while;

  close C_RES;

  commit;



    -- This part of the procedure updates invoice references into Road Permit form transaction (mr_pftrn)

    select distinct CONVERT(varchar,IVT_INVDT,101),IVT_LRYNO,IVT_BYRCD,IVT_FRMTP , IVT_FRMNO into L_INVDT,L_LRYNO,L_BYRCD, L_FRMTP, L_FRMNO   from  spldata.MR_IVTRN  where ivt_cmpcd = lp_cmpcd and IVT_MKTTP = LP_MKTTP and IVT_INVNO = LP_INVNO and length(isnull(IVT_FRMNO,''))>0;

  if length(trim(isnull(L_FRMNO,''))) > 1 then

     update spldata.MR_PFTRN set PFT_INVNO = LP_INVNO, PFT_INVDT = L_INVDT, PFT_LRYNO = L_LRYNO where pft_cmpcd = lp_cmpcd and PFT_PRTTP = 'C' and PFT_PRTCD = L_BYRCD and PFT_FRMTP = L_FRMTP and PFT_FRMNO = L_FRMNO and isnull(PFT_INVNO,'') = '';

     commit;

 -- This part of procedure sets right bagging date (st_rctdt) in Stosck Master (fg_stmst), wherever it is not updated

update spldata.fg_stmst a set st_rctdt = (select max(b.rct_rctdt) from spldata.fg_rctrn b where a.st_cmpcd = b.rct_cmpcd and a.st_prdtp=b.rct_prdtp and a.st_lotno = b.rct_lotno and a.st_rclno=b.rct_rclno) where  a.st_cmpcd = lp_cmpcd and a.st_rctdt is null;

commit;



  end if; 

  commit;

end P1;

commit;

call spldata.setDSPQT_RES('01','80001052');
commit;

select * from spldata.mr_pftrn;
