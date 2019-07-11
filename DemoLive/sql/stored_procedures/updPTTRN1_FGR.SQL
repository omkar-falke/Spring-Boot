ptl_l-- procedure to create/update data in Credit Note transaction using Sales Return - Finished Goods Receipts with Invoice Reference.
-- This procedure is to be executed whenever the Sales Recturn receipt is authorised
-- Procedure is called through fr_telau.java

-- Sales return Credit Note is raised on Buyers account against the Invoice amount
-- Sales return debit note is raised on concerned party (Buyer/Distributor) against the corresponding credit note amount.
-- While considering the credit note amount (for debit note generation), the TDS effect is not taken into account.
;

select * from spldata.mr_prtrn order by pr_prtcd;


COMMIT;

drop procedure spldata.updPTTRN1_FGR;
commit;


create procedure spldata.updPTTRN1_FGR(IN LP_CMPCD char(2),IN LP_RCTNO char(8))  language sql  modifies sql data 
P1:

begin
  declare L_RCTTP_SLR char(2) default '30';
  declare L_DOCTP_INV char(2) default '21';
  declare L_CRDTP_CR char(2) default '04';
  declare L_CRDTP_DB char(2) default '32';
  declare L_CRDTP_REF char(2);
  declare L_PRDCD char(10);
  declare L_PKGTP char(2);
  declare L_CRDTP char(2);
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_SRLNO char(2);
  declare L_TRNTP char(2);
  declare L_DOCRF char(8);
  declare L_DOCDT date;
  declare L_MKTTP char(2);
  declare L_INVNO char(8);
  declare L_INVDT date;
  declare L_RCTDT date;
  declare L_DOCQT decimal(10,3);
  declare L_RCTQT decimal(10,3);
  declare L_INVQT decimal(10,3);
  declare L_INVVL decimal(12,2);
  declare L_TRNRT int;
  declare L_TRNRT_INV int;
  declare L_PGRVL decimal(12,2);
  declare L_ATXVL decimal(8,2);
  declare L_LTXVL decimal(8,2);
  declare L_PNTVL decimal(12,2);
  declare L_INTVL decimal(12,2);
  declare L_ADJVL decimal(12,2);
  declare L_ACCRF char(10);
  declare L_ACCDT date;
  declare L_RPTDT date;
  declare L_GRPCD char(5);
  declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;



  declare L_SYSCD char(2);
  declare L_SBSTP char(2);
  declare L_DOCTP char(3);

  declare L_BYRCD char(5);
  declare L_CNSCD char(5);
  declare L_DSRCD char(5);
  declare L_INDNO char(9);

  declare L_SVCVL decimal(10,2);
  declare L_SVCFL char(1);
  declare L_EDCVL decimal(10,2);
  declare L_EDCFL char(1);
  declare L_EHCVL decimal(10,2);
  declare L_EHCFL char(1);
  declare L_TDSVL decimal(10,2);
  declare L_TDSFL char(1);
  declare L_SCHVL decimal(10,2);
  declare L_SCHFL char(1);

  declare L_ACSVL decimal(10,2);
  declare L_LCSVL decimal(10,2);

  declare L_CC1VL decimal(8,2);
  declare L_CC1RF char(6);
  declare L_CC2VL decimal(8,2);
  declare L_CC2RF char(6);
  declare L_CC3VL decimal(8,2);
  declare L_CC3RF char(6);

  declare L_REMDS char(200);

  --declare L_PMTFL varchar(1);
  declare L_RECCT int default 0;
  declare END_TABLE int default 0;

  declare C_FGR cursor for  select  IVT_MKTTP,IVT_BYRCD,IVT_DSRCD, IVT_SBSCD,substr(RCT_ISSRF,1,8) RCT_ISSRF,LT_PRDCD, RCT_PKGTP, RCT_RCTDT, ifnull(IVT_INVQT,0) IVT_INVQT, ifnull(IVT_INVRT,0)  IVT_INVRT, ifnull(IVT_NETVL,0) IVT_NETVL,sum(ifnull(RCT_RCTQT,0)) RCT_RCTQT from spldata.FG_RCTRN, spldata.PR_LTMST, spldata.MR_IVTRN where RCT_RCTTP = L_RCTTP_SLR and RCT_RCTNO = LP_RCTNO and RCT_STSFL not in ('1','X') and ifnull(RCT_RCTQT,0)>0 and RCT_PRDTP = LT_PRDTP and RCT_LOTNO=LT_LOTNO and RCT_RCLNO = LT_RCLNO and substr(RCT_ISSRF,1,8) = IVT_INVNO and IVT_SALTP not in ('04','05','16','21')  and LT_PRDCD = IVT_PRDCD and RCT_PKGTP = IVT_PKGTP group by  IVT_MKTTP,IVT_BYRCD,IVT_DSRCD, IVT_SBSCD,substr(RCT_ISSRF,1,8) ,LT_PRDCD, RCT_PKGTP, RCT_RCTDT, ifnull(IVT_INVQT,0) , IVT_INVRT,  ifnull(IVT_NETVL,0) ,  ifnull(IVT_NETVL,0) order by  RCT_ISSRF,LT_PRDCD, RCT_PKGTP;

  declare C_PTT  cursor for select PT_PRTTP, PT_PRTCD, PT_CRDTP, PT_PGRVL, PT_PNTVL, PT_TRNRT, PT_ATXVL, PT_LTXVL, PT_GRPCD, PT_SBSCD   from spldata.MR_PTTRN where PT_CMPCD = LP_CMPCD and  PT_CRDTP  in ('01','02','03')  and  PT_INVNO = L_INVNO and PT_PRDCD = L_PRDCD  and PT_PKGTP = L_PKGTP;


  declare C_DOC cursor for  select  distinct PT_CRDTP,PT_PRTTP, PT_PRTCD,PT_SRLNO  from  spldata.MR_PTTRN where PT_CMPCD = LP_CMPCD and PT_INVNO=L_INVNO and PT_DOCRF = '00000000' and PT_CRDTP in  (L_CRDTP_CR,L_CRDTP_DB)  order by PT_CRDTP,PT_PRTTP,PT_PRTCD,PT_SRLNO;

  declare C_DOC1 cursor for  select  distinct PT_MKTTP,PT_SBSCD,PT_DOCRF,PT_SRLNO  from  spldata.MR_PTTRN where PT_CMPCD = LP_CMPCD and PT_INVNO=L_INVNO and PT_CRDTP = L_CRDTP_DB  order by PT_MKTTP, PT_SBSCD,PT_DOCRF,PT_SRLNO;


  declare continue handler for not found 
       set END_TABLE = 1; 


  open C_FGR ;
  fetch C_FGR into  L_MKTTP,L_BYRCD, L_DSRCD, L_SBSCD,  L_INVNO, L_PRDCD, L_PKGTP,  L_RCTDT, L_INVQT,   L_TRNRT_INV,   L_PNTVL,  L_RCTQT;

  select count(*) into L_RECCT from spldata.mr_pttrn where pt_invno = L_INVNO and PT_CRDTP in ('01','02','03');

  -- If booking discount / commision records of corresponding invoices are not available, They will be generated before reverting them for Sales Return
  if L_RECCT = 0 then
     call spldata.updPTTRN_INV(LP_CMPCD,L_MKTTP,L_INVNO);
  end if;


 

  while END_TABLE = 0 DO
      set L_PRTTP = 'C';
      set L_PRTCD =  L_BYRCD;
      set L_DOCRF = '00000000';
      set L_PGRVL  = round((L_PNTVL*L_RCTQT)/L_INVQT,0);
      set L_PNTVL  = round((L_PNTVL*L_RCTQT)/L_INVQT,0);
      set L_ATXVL = 0;
      set L_LTXVL = 0;
      if L_PGRVL > L_PNTVL then
         set L_LTXVL = L_PGRVL - L_PNTVL;
      end if;
      if L_PGRVL < L_PNTVL then
         set L_ATXVL = L_PNTVL - L_PGRVL;
      end if;
      set L_DOCDT = L_RCTDT;
      set L_CRDTP = L_CRDTP_CR;
      set L_TRNTP = 'CR';
      set L_SRLNO = '00';
      set L_TRNFL = '0';
      set L_STSFL = '1';
      set L_LUSBY = 'SYS';
      set L_LUPDT = current_date;

      select count(*)  into L_RECCT from spldata.mr_pttrn where PT_CMPCD = LP_CMPCD and PT_INVNO = L_INVNO and PT_PRDCD = L_PRDCD and PT_PKGTP = L_PKGTP and PT_CRDTP = L_CRDTP and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_SRLNO = L_SRLNO;
      if L_RECCT = 0 then
         insert into spldata.mr_pttrn (PT_CMPCD,  PT_INVNO   ,PT_PRDCD  ,PT_PKGTP   ,PT_CRDTP  ,PT_PRTTP   ,PT_PRTCD ,  PT_SRLNO  ,PT_TRNTP,PT_DOCRF ,PT_DOCDT ,PT_INVQT   , PT_TRNRT, PT_PGRVL, PT_ATXVL, PT_LTXVL,  PT_PNTVL, PT_INTVL, PT_GRPCD, PT_MKTTP,PT_SBSCD, PT_TRNFL  ,PT_STSFL   ,PT_LUSBY  ,PT_LUPDT)  values (LP_CMPCD,  L_INVNO   ,L_PRDCD  ,L_PKGTP   ,L_CRDTP  ,L_PRTTP   ,L_PRTCD ,  L_SRLNO  ,L_TRNTP,L_DOCRF ,L_DOCDT ,L_RCTQT , L_TRNRT_INV , L_PGRVL, L_ATXVL, L_LTXVL,  L_PNTVL, L_INTVL, L_GRPCD, L_MKTTP,L_SBSCD, L_TRNFL  ,L_STSFL   ,L_LUSBY  ,L_LUPDT);
     else
         update spldata.mr_pttrn set PT_TRNTP = L_TRNTP,PT_DOCDT  = L_DOCDT ,PT_INVQT   = L_RCTQT   , PT_TRNRT = L_TRNRT_INV,  PT_PGRVL  = L_PGRVL , PT_ATXVL  = L_ATXVL , PT_LTXVL  = L_LTXVL ,  PT_PNTVL  = L_PNTVL , PT_INTVL  = L_INTVL , PT_GRPCD  = L_GRPCD , PT_MKTTP  = L_MKTTP ,PT_SBSCD  = L_SBSCD , PT_TRNFL  = L_TRNFL  ,PT_STSFL  = L_STSFL   ,PT_LUSBY  = L_LUSBY  ,PT_LUPDT  = L_LUPDT  where PT_CMPCD = LP_CMPCD and  PT_INVNO  = L_INVNO   and PT_PRDCD = L_PRDCD  and PT_PKGTP  = L_PKGTP   and PT_CRDTP  = L_CRDTP  and PT_PRTTP  = L_PRTTP   and PT_PRTCD = L_PRTCD and  PT_SRLNO = L_SRLNO ; 
      end if;


      set END_TABLE = 0;
      open C_PTT;
      fetch C_PTT  into  L_PRTTP, L_PRTCD, L_CRDTP_REF, L_PGRVL, L_PNTVL, L_TRNRT, L_ATXVL, L_LTXVL, L_GRPCD, L_SBSCD;
      while END_TABLE = 0 DO 
          set L_DOCRF = '00000000';
          set L_DOCDT = L_RCTDT;


          set L_PGRVL = round(L_PGRVL*(L_RCTQT/L_INVQT),0);
          set L_PNTVL = round(L_PNTVL*(L_RCTQT/L_INVQT),0);
          set L_ATXVL = round(L_ATXVL*(L_RCTQT/L_INVQT),0);
          set L_LTXVL = round(L_LTXVL*(L_RCTQT/L_INVQT),0);


          set L_SRLNO = L_CRDTP_REF;
          set L_TRNFL = '0';
          set L_STSFL = '1';
          set L_LUSBY = 'SYS';
          set L_LUPDT = current_date;

          set L_CRDTP = L_CRDTP_DB;
          set L_TRNTP = 'DB';
          if L_SRLNO in ('02','03')  then
             set L_LTXVL = 0;
             set L_PNTVL = L_PGRVL + L_ATXVL;
          end if;

          select count(*)  into L_RECCT from spldata.mr_pttrn where PT_CMPCD = LP_CMPCD and PT_INVNO = L_INVNO and PT_PRDCD = L_PRDCD and PT_PKGTP = L_PKGTP and PT_CRDTP = L_CRDTP  and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_SRLNO = L_SRLNO;
           if L_RECCT = 0 then
               insert into spldata.mr_pttrn (PT_CMPCD,  PT_INVNO   ,PT_PRDCD  ,PT_PKGTP   ,PT_CRDTP  ,PT_PRTTP   ,PT_PRTCD ,  PT_SRLNO  ,PT_TRNTP,PT_DOCRF ,PT_DOCDT ,PT_INVQT   ,PT_TRNRT , PT_PGRVL, PT_ATXVL, PT_LTXVL,  PT_PNTVL, PT_INTVL, PT_GRPCD, PT_MKTTP,PT_SBSCD, PT_TRNFL  ,PT_STSFL   ,PT_LUSBY  ,PT_LUPDT)  values (LP_CMPCD,  L_INVNO   ,L_PRDCD  ,L_PKGTP   ,L_CRDTP  ,L_PRTTP   ,L_PRTCD ,  L_SRLNO  ,L_TRNTP,L_DOCRF ,L_DOCDT ,L_RCTQT   ,L_TRNRT , L_PGRVL, L_ATXVL, L_LTXVL,  L_PNTVL, L_INTVL, L_GRPCD, L_MKTTP,L_SBSCD, L_TRNFL  ,L_STSFL   ,L_LUSBY  ,L_LUPDT);
            else 
               update spldata.mr_pttrn set PT_TRNTP = L_TRNTP, PT_DOCDT  = L_DOCDT ,PT_INVQT   = L_RCTQT   ,PT_TRNRT  = L_TRNRT , PT_PGRVL  = L_PGRVL , PT_ATXVL  = L_ATXVL , PT_LTXVL  = L_LTXVL ,  PT_PNTVL  = L_PNTVL , PT_INTVL  = L_INTVL , PT_GRPCD  = L_GRPCD , PT_MKTTP  = L_MKTTP ,PT_SBSCD  = L_SBSCD , PT_TRNFL  = L_TRNFL  ,PT_STSFL  = L_STSFL   ,PT_LUSBY  = L_LUSBY  ,PT_LUPDT  = L_LUPDT  where PT_CMPCD = LP_CMPCD and  PT_INVNO  = L_INVNO   and PT_PRDCD = L_PRDCD  and PT_PKGTP  = L_PKGTP   and PT_CRDTP  = L_CRDTP  and PT_PRTTP  = L_PRTTP   and PT_PRTCD = L_PRTCD and  PT_SRLNO = L_SRLNO ; 
           end if;
 
            set END_TABLE = 0;
            fetch C_PTT  into  L_PRTTP, L_PRTCD, L_CRDTP_REF, L_PGRVL, L_PNTVL, L_TRNRT, L_ATXVL, L_LTXVL, L_GRPCD, L_SBSCD;
       end while;
       close C_PTT;

      set END_TABLE = 0;
      fetch C_FGR into  L_MKTTP,L_BYRCD, L_DSRCD, L_SBSCD,  L_INVNO, L_PRDCD, L_PKGTP,  L_RCTDT, L_INVQT,   L_TRNRT_INV,  L_PNTVL,  L_RCTQT;
  end while;


 set END_TABLE = 0;
 open C_DOC;
  fetch C_DOC into  L_CRDTP,L_PRTTP, L_PRTCD,L_SRLNO;
  while END_TABLE = 0 DO 
     select char(int(cmt_codcd||cmt_ccsvl)+1) into L_DOCRF from spldata.co_cdtrn where cmt_cgmtp='DOC' and cmt_cgstp='MRXXPTT' and substr(cmt_codcd,2,2) = L_CRDTP;
    update spldata.MR_PTTRN set PT_DOCRF = L_DOCRF where PT_CMPCD=LP_CMPCD and PT_INVNO = L_INVNO and PT_CRDTP = L_CRDTP and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_DOCRF = '00000000' and PT_SRLNO = L_SRLNO;
     update spldata.co_cdtrn set CMT_CCSVL = substr(L_DOCRF,4,5) where cmt_cgmtp='DOC' and cmt_cgstp='MRXXPTT' and substr(cmt_codcd,2,2) = L_CRDTP;
    fetch C_DOC into  L_CRDTP,L_PRTTP, L_PRTCD,L_SRLNO;
  end while;
  close C_DOC;



 set END_TABLE = 0;
 open C_DOC1;
  fetch C_DOC1 into  L_MKTTP, L_SBSCD,L_DOCRF, L_SRLNO;
  while END_TABLE = 0 DO 
       if L_SRLNO in ('02','03')  then
        set L_SYSCD = 'MR';
        set L_SBSTP = L_MKTTP;
        set L_PRDCD = 'XXXXXXXXXX';
        set L_TRNFL = '0';
        set L_STSFL = '1';
        set L_LUSBY = 'SYS';
        set L_LUPDT = current_date;
        set L_DOCTP = 'CRA';
        set L_SVCVL = 12;
        set L_SVCFL = 'P';
        set L_EDCVL = 2;
        set L_EDCFL = 'P';
        set L_EHCVL = 1;
        set L_EHCFL = 'P';
        set L_TDSVL = 0.00;
        set L_TDSFL = ' ';
        set L_SCHVL = 0.00;
        set L_SCHFL = ' ';
        select count(*)  into L_RECCT from spldata.co_txdoc where TX_SYSCD = L_SYSCD and TX_SBSTP = L_SBSTP and TX_DOCTP =  L_DOCTP  and TX_DOCNO = L_DOCRF and TX_PRDCD = L_PRDCD ;

        if L_RECCT = 0 then 
            insert into spldata.co_txdoc (TX_SYSCD, TX_SBSTP, TX_DOCTP, TX_DOCNO, TX_PRDCD, TX_EDCVL, TX_EDCFL, TX_EHCVL, TX_EHCFL, TX_SCHVL, TX_SCHFL, TX_SVCVL, TX_SVCFL,TX_TDSVL, TX_TDSFL, TX_SBSCD, TX_STSFL, TX_TRNFL, TX_LUSBY, TX_LUPDT)  values (L_SYSCD, L_SBSTP, L_DOCTP, L_DOCRF, L_PRDCD, L_EDCVL, L_EDCFL, L_EHCVL, L_EHCFL, L_SCHVL, L_SCHFL,L_SVCVL, L_SVCFL,L_TDSVL, L_TDSFL, L_SBSCD, L_STSFL, L_TRNFL, L_LUSBY, L_LUPDT);
        else 
           update spldata.co_txdoc set TX_SYSCD = L_SYSCD , TX_SBSTP  = L_SBSTP , TX_DOCTP  = L_DOCTP , TX_DOCNO  = L_DOCRF , TX_PRDCD  = L_PRDCD , TX_EDCVL  = L_EDCVL , TX_EDCFL  = L_EDCFL, TX_EHCVL  = L_EHCVL , TX_EHCFL  = L_EHCFL , TX_SCHVL  = L_SCHVL , TX_SCHFL  = L_SCHFL ,TX_SVCVL  = L_SVCVL , TX_SVCFL  = L_SVCFL ,TX_TDSVL  = L_TDSVL , TX_TDSFL  = L_TDSFL , TX_SBSCD  = L_SBSCD , TX_STSFL  = L_STSFL , TX_TRNFL  = L_TRNFL , TX_LUSBY  = L_LUSBY , TX_LUPDT  = L_LUPDT  where TX_SYSCD = L_SYSCD and TX_SBSTP = L_SBSTP and TX_DOCTP =  L_DOCTP  and TX_DOCNO = L_DOCRF and TX_PRDCD = L_PRDCD;
        end if;
     set END_TABLE = 0;
  end if;

  if L_SRLNO in ('01','02','03')  then
     set L_TRNTP = 'DB';
     if L_SRLNO = '01'  then
        set L_TRNTP = 'CR';
     end if;
     set L_REMDS = '';
     select distinct PT_TRNRT into L_TRNRT from spldata.MR_PTTRN where PT_CMPCD = LP_CMPCD and PT_DOCRF=L_DOCRF and PT_SRLNO = L_SRLNO;

     if L_SRLNO in  ('01','02')  then
         set L_REMDS = 'Being booking discount reversal @ Rs.'||trim(char(L_TRNRT))||'/- PMT for rejection of Material';
     end if;
     if L_SRLNO = '03' then
        set L_REMDS = 'Being distributor commission reversal @ Rs.'||trim(char(L_TRNRT))||'/- PMT for rejection of Material';
     end if;

     set L_TRNFL = '0';
     set L_STSFL = '1';
     set L_LUSBY = 'SYS';
     set L_LUPDT = current_date;

     select count(*)  into L_RECCT from spldata.mr_rmmst where RM_MKTTP = L_MKTTP and RM_TRNTP = 'DB' and RM_DOCNO = L_DOCRF;

        if L_RECCT = 0 then 
            insert into spldata.mr_rmmst (RM_MKTTP, RM_TRNTP,RM_DOCNO, RM_REMDS, RM_SBSCD, RM_STSFL, RM_TRNFL, RM_LUSBY, RM_LUPDT)  values (L_MKTTP, L_TRNTP,L_DOCRF, L_REMDS, L_SBSCD, L_STSFL, L_TRNFL, L_LUSBY, L_LUPDT);
        else 
           update spldata.mr_rmmst set RM_REMDS = L_REMDS, RM_SBSCD = L_SBSCD, RM_STSFL = L_STSFL, RM_TRNFL = L_TRNFL, RM_LUSBY = L_LUSBY, RM_LUPDT = L_LUPDT where  RM_MKTTP = L_MKTTP and RM_TRNTP = L_TRNTP and RM_DOCNO = L_DOCRF;
        end if;
     
  end if;
  fetch C_DOC1 into  L_MKTTP, L_SBSCD, L_DOCRF, L_SRLNO;
  end while;
  close C_DOC1;

end;


commit;
call spldata.updPTTRN_FGR('01','73000027');
call spldata.updPTTRN_FGR('01','73000028');
call spldata.updPTTRN_FGR('01','73000029');
call spldata.updPTTRN_FGR('01','73000031');
call spldata.updPTTRN_FGR('01','73000032');
call spldata.updPTTRN_FGR('01','73000033');
call spldata.updPTTRN_FGR('01','73000034');
call spldata.updPTTRN_FGR('01','73000035');
call spldata.updPTTRN_FGR('01','73000036');
call spldata.updPTTRN_FGR('01','73000037');
call spldata.updPTTRN_FGR('01','73000038');
call spldata.updPTTRN_FGR('01','73000039');
call spldata.updPTTRN_FGR('01','73000040');
call spldata.updPTTRN_FGR('01','73000041');
call spldata.updPTTRN_FGR('01','73000042');
commit;

select * from spldata.mr_pttrn where pt_lupdt = '03/10/2007';

