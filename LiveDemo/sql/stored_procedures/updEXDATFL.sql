
drop procedure spldata.updEXDATFL;
commit;


create procedure spldata.updEXDATFL(IN LP_CMPCD char(2),IN LP_DOCTP char(2), IN LP_DOCDT char(10))  language sql  modifies sql data 
P1:

  begin
 
  declare L_DOCNO varchar(8);
  declare L_DOCTP varchar(2);
  declare L_DOCTP1 varchar(2);
  declare L_PRDTP varchar(2);
  declare L_LOTNO varchar(8);
  declare L_PKGTP varchar(2);
  declare L_FPKTP varchar(2);
  declare L_FPRCD varchar(10);
  declare L_PRDCD varchar(10);
  declare L_LOCDT varchar(10);
  declare L_DOCDT date;
  declare L_DOCQT decimal(10,3);
  declare L_UPDFL varchar(1) default ' ';
  declare L_RECCT int default 0;
  declare END_TABLE int default 0;


/* Receipt(bagging) data(FG_RCTRN)  */
   declare C_EXD cursor for select RCT_RCTTP LM_DOCTP,RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT  from spldata.FG_RCTRN,spldata.PR_LTMST where RCT_CMPCD = LP_CMPCD and RCT_RCTDT between  CONVERT(varchar,L_LOCDT,101)  and CONVERT(varchar,LP_DOCDT,101)  and RCT_RCTTP in ('10','15','21','22','23') and RCT_STSFL = '2'  and RCT_PRDTP = LT_PRDTP  and RCT_LOTNO = LT_LOTNO  group by RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,LT_PRDCD, RCT_RCTDT order by LM_DOCTP,LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_PRDCD,LM_DOCDT;



  declare continue handler for not found 
       set END_TABLE = 1; 


 select cmt_ccsvl into L_LOCDT from spldata.co_cdtrn where cmt_cgmtp='S' + lp_cmpcd and cmt_cgstp = 'FGXXREF' and cmt_codcd = 'DOCDT';
 set L_LOCDT = SUBSTRING(L_LOCDT,4,2) + '/' + SUBSTRING(L_LOCDT,1,2) + '/' + SUBSTRING(L_LOCDT,7,4);

  set END_TABLE = 0;
  open C_EXD ;
  fetch C_EXD into L_DOCTP,L_DOCNO,L_PRDTP,L_LOTNO,L_PKGTP,L_FPKTP,L_FPRCD,L_PRDCD,L_DOCDT,L_DOCQT;
  while END_TABLE = 0 DO 
          if L_DOCTP in ('10','15') then
             set L_DOCTP1 = 'RC';
          elseif L_DOCTP in ('21','22','23')  then 
             set L_DOCTP1 = 'JR';
          end if;
          select count(*)  into L_RECCT from spldata.EX_DATFL where dt_CMPCD = LP_CMPCD and DT_DOCTP = LP_DOCTP and DT_DOCNO = L_DOCNO and DT_PRDTP = L_PRDTP and DT_LOTNO = L_LOTNO and DT_PKGTP = L_PKGTP and DT_FPKTP = L_FPKTP;
          if L_RECCT = 0 then
             Insert into spldata.EX_DATFL (DT_CMPCD,DT_DOCTP,DT_DOCNO,DT_PRDTP,DT_LOTNO,DT_PKGTP,DT_FPKTP,DT_FPRCD,DT_PRDCD,DT_DOCQT,DT_DOCDT,DT_UPDFL) values (lp_cmpcd,LP_DOCTP,L_DOCNO,L_PRDTP,L_LOTNO,L_PKGTP,L_FPKTP,L_FPRCD,L_PRDCD,L_DOCQT,L_DOCDT,L_UPDFL);	
          end  if;
         set END_TABLE = 0;
  fetch C_EXD into L_DOCTP,L_DOCNO,L_PRDTP,L_LOTNO,L_PKGTP,L_FPKTP,L_FPRCD,L_PRDCD,L_DOCDT,L_DOCQT;
  end while;
  close C_EXD;
end;
commit;

call spldata.updEXDATFL('RC','08/10/2006');
commit;


select * from spldata.ex_datfl;


/* Jobwork Receipt Data (FG_RCTRN)  */
/*if LP_DOCTP = 'JR' then  
   declare C_EXD cursor for select RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT  from FG_RCTRN,PR_LTMST where rct_CMPCD = LP_CMPCD and RCT_RCTDT between L_LOCDT and LP_DOCDT  and RCT_LOTNO like '999%' and RCT_RCTTP in ('21','22','23') and RCT_STSFL = '2' and RCT_PRDTP = LT_PRDTP  and RCT_LOTNO = LT_LOTNO  group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,LT_PRDCD, RCT_RCTDT order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_PRDCD,LM_DOCDT;
end if;*/



/*  From FG_PTFRF  */
/*declare C_EXD_C12 cursor for Select PTF_PTFNO LM_DOCNO,PTF_PRDTP LM_PRDTP,PTF_LOTNO LM_LOTNO,PTF_PKGTP LM_PKGTP,'XX' LM_FPKTP,PTF_OPRCD LM_FPRCD,PTF_PRDCD LM_PRDCD,";
PTF_PTFDT LM_DOCDT,PTF_PTFCT,sum(PTF_PTFQT) LM_DOCQT from FG_PTFRF where ptf_CMPCD = LP_CMPCD and PTF_PTFDT between " + L_LOCDT and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT ) and PTF_PTFCT in ('01','02') and substr(PTF_LOTNO,1,3) <> '999' group by PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_PKGTP,PTF_OPRCD, PTF_PRDCD,PTF_PTFDT,PTF_PTFCT order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPRCD, LM_PRDCD,LM_DOCDT,PTF_PTFCT;
*/

/*  From FG_PTFRF & FG_ISTRN  */
/*declare C_EXD_C3 cursor for    select PTF_PTFNO LM_DOCNO,IST_PRDTP LM_PRDTP,IST_LOTNO LM_LOTNO,IST_PKGTP LM_PKGTP,'XX' LM_FPKTP,IST_PRDCD LM_FPRCD,'' LM_PRDCD,PTF_PTFDT LM_DOCDT,sum(IST_ISSQT) LM_DOCQT from FG_PTFRF,FG_ISTRN where ptf_CMPCD = LP_CMPCD and ptf_cmpcd=ist_cmpcd and PTF_PTFRF = IST_ISSNO and PTF_PTFDT between " + L_LOCDT and cc_dattm.occ_dattm.setDBSDT(LP_DOCDT ) and PTF_PTFCT = '03' group by PTF_PTFNO,IST_PRDTP,IST_LOTNO,IST_PKGTP, IST_PRDCD,PTF_PTFDT	 union select PTF_PTFNO LM_DOCNO,PTF_PRDTP LM_PRDTP,PTF_LOTNO LM_LOTNO,PTF_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,PTF_PRDCD LM_PRDCD,PTF_PTFDT LM_DOCDT,PTF_PTFQT LM_DOCQT from FG_PTFRF  where ptf_CMPCD = LP_CMPCD and PTF_PTFDT between  L_LOCDT and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT ) and PTF_PTFCT = '03' order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPRCD,LM_PRDCD,LM_DOCDT;
*/

/*  Rebagging From FG_RCTRN & FG_ISTRN  */
/*declare C_EXD_RB cursor for     select IST_ISSNO LM_DOCNO,IST_PRDTP LM_PRDTP,IST_LOTNO LM_LOTNO,'XX' LM_PKGTP,IST_PKGTP LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,IST_AUTDT LM_DOCDT,sum(IST_ISSQT) LM_DOCQT from FG_ISTRN,PR_LTMST where ist_CMPCD = LP_CMPCD and ist_cmpcd = lt_cmpcd and IST_PRDTP = LT_PRDTP  and IST_LOTNO = LT_LOTNO  and IST_AUTDT between " + L_LOCDT and  cc_dattm.occ_dattm.setDBSDT(LP_DOCDT ) and IST_ISSTP = '16'  and IST_STSFL = '2'  and (IST_MKTTP <> 'SR' or IST_MKTTP is null)  group by IST_ISSNO,IST_PRDTP,IST_LOTNO,IST_PKGTP, LT_PRDCD,IST_AUTDT  union select RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT from FG_RCTRN,PR_LTMST where rct_CMPCD = LP_CMPCD and rct_cmpcd = lt_cmpcd and RCT_PRDTP = LT_PRDTP  and RCT_LOTNO = LT_LOTNO  and RCT_RCTDT between " + L_LOCDT and  cc_dattm.occ_dattm.setDBSDT(LP_DOCDT ) and RCT_RCTTP = '16'  and RCT_STSFL = '2'  and (RCT_ISSRF <> 'SR' or RCT_ISSRF is null)  group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP, LT_PRDCD,RCT_RCTDT order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPKTP, LM_FPRCD,LM_PRDCD,LM_DOCDT,LM_DOCQT;
*/

/*  Jobwork Classification  */
/*declare C_EXD_CJ cursor for  select PTF_PTFNO LM_DOCNO,PTF_PRDTP LM_PRDTP,PTF_LOTNO LM_LOTNO,PTF_PKGTP LM_PKGTP,'XX' LM_FPKTP,PTF_OPRCD LM_FPRCD,PTF_PRDCD LM_PRDCD,
PTF_PTFDT LM_DOCDT,PTF_PTFCT,sum(PTF_PTFQT) LM_DOCQT from FG_PTFRF where ptf_CMPCD = LP_CMPCD and PTF_PTFDT between L_LOCDT and  cc_dattm.occ_dattm.setDBSDT(LP_DOCDT ) and PTF_PTFCT in ('01') and substr(PTF_LOTNO,1,3) = '999' group by PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_PKGTP,PTF_OPRCD, PTF_PRDCD,PTF_PTFDT,PTF_PTFCT
 order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPRCD, LM_PRDCD,LM_DOCDT,PTF_PTFCT;
*/

/*  Sales return (Classified from Receipts)  */
/*declare C_EXD_CS cursor for select RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,
RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT from FG_RCTRN,PR_LTMST where rct_CMPCD = LP_CMPCD and rct_cmpcd = lt_cmpcd and RCT_PRDTP = LT_PRDTP  and RCT_LOTNO = LT_LOTNO  and RCT_RCTDT between  L_LOCDT and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT )  and RCT_RCTTP in ('30')  and RCT_STSFL = '2'   group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,
 LT_PRDCD,RCT_RCTDT order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPKTP, LM_FPRCD,LM_PRDCD,LM_DOCDT,LM_DOCQT;
*/



