getdcl-- procedure to create/update data in Credit Note transaction using Invoice details
-- This procedure is to be executed whenever the invoice is approved for Gate-out.
-- Procedure is called through mm_teexa.java


;

COMMIT;

drop procedure spldata.updPTTRN_INV;
commit;


create procedure spldata.updPTTRN_INV(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_INVNO char(8))  language sql  modifies sql data 
P1:

begin
  declare L_PRDCD char(10);
  declare L_PKGTP char(2);
  declare L_CRDTP char(2);
  declare L_PRTTP char(1);
  declare L_BYRCD char(5);
  declare L_PRTCD char(5);
  declare L_SRLNO char(2);
  declare L_TRNTP char(2);
  declare L_DOCRF char(8);
  declare L_DOCDT date;
  declare L_INVDT date;
  declare L_SALTP char(2);
  declare L_INVQT decimal(10,3);
  declare L_TRNRT decimal(8,2);
  declare L_COMCT char(2);
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
  declare L_GRPCD_DSR char(5);
  declare L_GRPCD_BYR char(5);
  declare L_PRDTP char(2);
  declare L_ZONCD char(2);
  declare L_SBSCD1 char(6);
  ---declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;

  declare L_SYSCD char(2);
  declare L_SBSTP char(2);
  declare L_DOCTP char(3);

  declare L_CNSCD char(5);
  declare L_DSRTP char(1);
  declare L_DSRCD char(5);
  declare L_INDNO char(9);

  declare L_SVCVL decimal(10,2);
  declare L_SVCFL char(1);
  declare L_EDCVL decimal(10,2);
  declare L_EDCFL char(1);
  declare L_ECHVL decimal(10,2);
  declare L_ECHFL char(1);
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
  declare LM_CMMCD char(2);

  --declare L_PMTFL varchar(1);
  declare L_RECCT int default 0;
  declare END_TABLE int default 0;

  declare C_INV cursor for  select  IVT_PRDCD, IVT_PKGTP, IVT_BYRCD,IVT_CNSCD, IVT_DSRTP, IVT_DSRCD,IVT_INVQT,IVT_INDNO,IVT_SALTP,IVT_ZONCD,IVT_SBSCD1,CONVERT(varchar,IVT_INVDT,101) IVT_INVDT  from  spldata.MR_IVTRN where  IVT_CMPCD = LP_CMPCD and IVT_MKTTP = LP_MKTTP and IVT_SALTP  not in ('04','05','16','15') and  IVT_INVNO=LP_INVNO and IVT_INVQT > 0 and IVT_STSFL <> 'X';

  declare C_DOC cursor for  select  distinct PT_CRDTP,PT_PRTTP, PT_PRTCD  from  spldata.MR_PTTRN where  PT_CMPCD = LP_CMPCD and PT_INVNO=LP_INVNO and PT_DOCRF = '00000000' and PT_CRDTP not in ('02','03')  order by PT_CRDTP,PT_PRTTP,PT_PRTCD;

  --declare C_PTT cursor for  select distinct PT_DOCRF, PT_SBSCD  from  spldata.MR_PTTRN  where  PT_CMPCD = LP_CMPCD and PT_INVNO = LP_INVNO and PT_CRDTP = LM_CMMCD order by PT_DOCRF, PT_SBSCD;

  declare continue handler for not found 
       set END_TABLE = 1; 

 set LM_CMMCD = '03';
  open C_INV ;
  fetch C_INV into  L_PRDCD, L_PKGTP, L_BYRCD,L_CNSCD, L_DSRTP, L_DSRCD, L_INVQT, L_INDNO,L_SALTP, L_ZONCD, L_SBSCD1,L_INVDT;
  while END_TABLE = 0 DO 

  --select distinct isnull(pt_pmtfl,'N') pt_pmtfl into L_PMTFL from spldata.co_ptmst where pt_prttp=L_PRTTP  and pt_prtcd = L_PRTCD;
  --if L_PMTFL = 'Y'  then

      select isnull(pt_grpcd,pt_prtcd)  into L_GRPCD_BYR from spldata.co_ptmst where pt_prttp='C'  and pt_prtcd = L_BYRCD;
      select isnull(pt_grpcd,pt_prtcd)  into L_GRPCD_DSR from spldata.co_ptmst where pt_prttp=L_DSRTP and pt_prtcd = L_DSRCD;

  select isnull(int_cc1vl,0), isnull(int_cc1rf,'     '), isnull(int_cc2vl,0), isnull(int_cc2rf,'     '), isnull(int_cc3vl,0), isnull(int_cc3rf,'     ')  into L_CC1VL, L_CC1RF, L_CC2VL, L_CC2RF, L_CC3VL, L_CC3RF from spldata.mr_intrn where int_indno = L_INDNO and int_prdcd = L_PRDCD and int_pkgtp = L_PKGTP;


   if L_CC1VL > 0 then
      set L_PRTTP = SUBSTRING(L_CC1RF,1,1);
      set L_PRTCD = SUBSTRING(L_CC1RF,2,5);
      -- ***** L_PRDCD *****
      -- ***** L_PKGTP *****
      set L_CRDTP = '01';
      -- ***** L_PRTTP *****
      set L_SRLNO = '00';
      set L_TRNTP  = 'CR';
      set L_DOCRF = '00000000';
      set L_DOCDT = L_INVDT;
      -- **** L_INVQT *****
      set L_TRNRT = L_CC1VL;
      set L_PGRVL = L_TRNRT * L_INVQT;

      set L_SVCVL = 0;       -- (SErvice Tax)
      set L_ACSVL = 0;       -- (Cess Addition)
      set L_TDSVL = 0;       -- (Tax deduction at source)
      set L_SCHVL = 0;      -- (Surcharge on TDS)
      set L_LCSVL = 0;       -- (Cess Deduction)

      set L_ATXVL  = L_SVCVL + L_ACSVL;                          -- (Tax addition)
      set L_LTXVL  = L_TDSVL+L_SCHVL+L_ACSVL;             -- (Tax deduction)

      --set L_PNTVL = L_PGRVL + (L_SVCVL+L_ACSVL) - (L_TDSVL+L_SCHVL+L_LCSVL);   

      set L_PNTVL = L_PGRVL + L_ATXVL - L_LTXVL;

      set L_TRNFL = '0';
      set L_STSFL = '1';
      set L_LUSBY = 'SYS';
      set L_LUPDT = current_date;
      set L_INTVL = 0;

      -- ***** L_ADJVL *****             -- Not to be changed
      -- ***** L_ACCRF *****            -- Not to be changed
      -- ***** L_ACCDT *****             -- Maintained through other operation
      -- ***** L_RPTDT *****               -- Maintained through other operation
      -- set L_MKTTP = LP_MKTTP;
      -- ***** L_SBSCD *****

      select isnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp=L_PRTTP  and pt_prtcd = L_PRTCD;

      --  ***** L_GRPCD *****

        select count(*)  into L_RECCT from spldata.mr_pttrn where PT_CMPCD = LP_CMPCD and PT_INVNO = LP_INVNO and PT_PRDCD = L_PRDCD and PT_PKGTP = L_PKGTP and PT_CRDTP = L_CRDTP and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_SRLNO = L_SRLNO;
        if L_RECCT = 0 then
           insert into spldata.mr_pttrn (PT_CMPCD,  PT_INVNO   ,PT_PRDCD  ,PT_PKGTP   ,PT_CRDTP  ,PT_PRTTP   ,PT_PRTCD ,  PT_SRLNO  ,PT_TRNTP,PT_DOCRF ,PT_DOCDT ,PT_INVQT   ,PT_TRNRT , PT_PGRVL, PT_ATXVL, PT_LTXVL,  PT_PNTVL, PT_INTVL, PT_GRPCD, PT_MKTTP,PT_SBSCD1, PT_TRNFL  ,PT_STSFL   ,PT_LUSBY  ,PT_LUPDT)  values (LP_CMPCD,  LP_INVNO   ,L_PRDCD  ,L_PKGTP   ,L_CRDTP  ,L_PRTTP   ,L_PRTCD ,  L_SRLNO  ,L_TRNTP,L_DOCRF ,L_DOCDT ,L_INVQT   ,L_TRNRT , L_PGRVL, L_ATXVL, L_LTXVL,  L_PNTVL, L_INTVL, L_GRPCD_BYR, LP_MKTTP,L_SBSCD1, L_TRNFL  ,L_STSFL   ,L_LUSBY  ,L_LUPDT);
       else 
         update spldata.mr_pttrn set PT_TRNTP = L_TRNTP,PT_DOCDT  = L_DOCDT ,PT_INVQT   = L_INVQT   ,PT_TRNRT  = L_TRNRT , PT_PGRVL  = L_PGRVL , PT_ATXVL  = L_ATXVL , PT_LTXVL  = L_LTXVL ,  PT_PNTVL  = L_PNTVL , PT_INTVL  = L_INTVL , PT_GRPCD  = L_GRPCD_BYR , PT_MKTTP  = LP_MKTTP ,PT_SBSCD1  = L_SBSCD1 , PT_TRNFL  = L_TRNFL  ,PT_STSFL  = L_STSFL   ,PT_LUSBY  = L_LUSBY  ,PT_LUPDT  = L_LUPDT  where PT_CMPCD = LP_CMPCD and  PT_INVNO  = LP_INVNO   and PT_PRDCD = L_PRDCD  and PT_PKGTP  = L_PKGTP   and PT_CRDTP  = L_CRDTP  and PT_PRTTP  = L_PRTTP   and PT_PRTCD = L_PRTCD and  PT_SRLNO = L_SRLNO ; 
      end if;
   end if;



   if (L_CC2VL > 0 and SUBSTRING(L_CC2RF,3,4) <> '8888')  then
      set L_PRTTP = SUBSTRING(L_CC2RF,1,1);
      set L_PRTCD = SUBSTRING(L_CC2RF,2,5);
      -- ***** L_PRDCD *****
      -- ***** L_PKGTP  *****
      set L_CRDTP = '02';
      -- ***** L_PRTTP *****
      set L_SRLNO = '00';
      set L_TRNTP  = 'CR';
      set L_DOCRF = '00000000';
      set L_DOCDT = L_INVDT;
      -- ***** L_INVQT *****
      set L_TRNRT = L_CC2VL;
      set L_PGRVL = L_TRNRT * L_INVQT;

      set L_SVCVL = 0;        -- (SErvice Tax)
      set L_ACSVL = 0;       -- (Cess Addition)
      set L_TDSVL = 0;       -- (Tax deduction at source)
      set L_SCHVL = 0;      -- (Surcharge on TDS)
      set L_LCSVL = 0;       -- (Cess Deduction)

      set L_PGRVL = round(L_TRNRT * L_INVQT,2);
      set L_SVCVL = round(L_PGRVL * 0.12,2);                   -- Service Charge on DocAmount @ 12%
      set L_ACSVL = round(L_SVCVL * 0.02,2);                   -- Education Cess on Service Charge @ 2%
      set L_ACSVL = L_ACSVL + round(L_SVCVL * 0.01,2);  -- Higher Education Cess on Service Charge @ 1%
      set L_ATXVL = L_SVCVL + L_ACSVL;

      set L_TDSVL = round((L_PGRVL+L_ATXVL)*0.05,2);    -- Tax Deduction at Source @ 5%
      set L_SCHVL = round(L_TDSVL * 0.10,2);                    --  Surcharge on TDS @ 10%
      set L_LCSVL = round((L_TDSVL+L_SCHVL)*0.02,2);    --  Education Cess on TDS+Surchrge  @ 2%
      set L_LCSVL = L_LCSVL + round((L_TDSVL+L_SCHVL) * 0.01,2);   --  Higher Education Cess on TDS+Surchrge  @ 1%
      set L_LTXVL  = L_TDSVL+L_SCHVL+L_LCSVL;


      set L_ATXVL  = L_SVCVL + L_ACSVL;                          -- (Tax addition)
      set L_LTXVL  = L_TDSVL+L_SCHVL+L_ACSVL;             -- (Tax deduction)

      set L_PNTVL = L_PGRVL + (L_SVCVL+L_ACSVL) - (L_TDSVL+L_SCHVL+L_LCSVL);   

      set L_PNTVL = L_PGRVL + L_ATXVL - L_LTXVL;

      set L_TRNFL = '0';
      set L_STSFL = '1';
      set L_LUSBY = 'SYS';
      set L_LUPDT = current_date;
      set L_INTVL = 0;

      -- ***** L_ADJVL   *****        -- Not to be changed
      -- ***** L_ACCRF  *****       -- Not to be changed
      -- ***** L_ACCDT   *****         -- Maintained through other operation
      -- ***** L_RPTDT   *****        -- Maintained through other operation
      -- ***** L_SBSCD *******


      select isnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp=L_PRTTP  and pt_prtcd = L_PRTCD;
      -- ****** L_GRPCD *******

        select count(*)  into L_RECCT from spldata.mr_pttrn where PT_CMPCD = LP_CMPCD and PT_INVNO = LP_INVNO and PT_PRDCD = L_PRDCD and PT_PKGTP = L_PKGTP and PT_CRDTP = L_CRDTP and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_SRLNO = L_SRLNO;
        if L_RECCT = 0 then
            insert into spldata.mr_pttrn (PT_CMPCD,  PT_INVNO   ,PT_PRDCD  ,PT_PKGTP   ,PT_CRDTP  ,PT_PRTTP   ,PT_PRTCD ,  PT_SRLNO  ,PT_TRNTP,PT_DOCRF ,PT_DOCDT ,PT_INVQT   ,PT_TRNRT , PT_PGRVL, PT_ATXVL, PT_LTXVL,  PT_PNTVL, PT_INTVL, PT_GRPCD, PT_MKTTP,PT_SBSCD1, PT_TRNFL  ,PT_STSFL   ,PT_LUSBY  ,PT_LUPDT)  values (LP_CMPCD,  LP_INVNO   ,L_PRDCD  ,L_PKGTP   ,L_CRDTP  ,L_PRTTP   ,L_PRTCD ,  L_SRLNO  ,L_TRNTP,L_DOCRF ,L_DOCDT ,L_INVQT   ,L_TRNRT , L_PGRVL, L_ATXVL, L_LTXVL,  L_PNTVL, L_INTVL, L_GRPCD, LP_MKTTP,L_SBSCD1, L_TRNFL  ,L_STSFL   ,L_LUSBY  ,L_LUPDT);
       else 
         update spldata.mr_pttrn set PT_TRNTP = L_TRNTP,PT_DOCDT  = L_DOCDT ,PT_INVQT   = L_INVQT   ,PT_TRNRT  = L_TRNRT , PT_PGRVL  = L_PGRVL , PT_ATXVL  = L_ATXVL , PT_LTXVL  = L_LTXVL ,  PT_PNTVL  = L_PNTVL , PT_INTVL  = L_INTVL , PT_GRPCD  = L_GRPCD , PT_MKTTP  = LP_MKTTP ,PT_SBSCD1  = L_SBSCD1 , PT_TRNFL  = L_TRNFL  ,PT_STSFL  = L_STSFL   ,PT_LUSBY  = L_LUSBY  ,PT_LUPDT  = L_LUPDT  where PT_CMPCD = LP_CMPCD and  PT_INVNO  = LP_INVNO   and PT_PRDCD = L_PRDCD  and PT_PKGTP  = L_PKGTP   and PT_CRDTP  = L_CRDTP  and PT_PRTTP  = L_PRTTP   and PT_PRTCD = L_PRTCD and  PT_SRLNO = L_SRLNO ;
      end if;
     --end if;
   end if;

     -- Applying Distributor Commission
      set L_CRDTP = LM_CMMCD;
      set L_TRNTP = 'CR';
      set L_PRTTP = L_DSRTP;
      set L_PRTCD = L_DSRCD;
      set L_SRLNO = '00';
      set L_DOCRF = '00000000';
      set L_DOCDT = L_INVDT;

      -- Arriving at distributor commission rate
      call spldata.getDCMRT(L_DSRTP, L_DSRCD , L_PRDCD , L_SALTP, L_GRPCD_BYR, L_TRNRT);

      -- Distributor commission is applied, only if the commission rate is greater than zero
      if L_TRNRT>0 then
        set L_PGRVL = round(L_TRNRT * L_INVQT,2);
        set L_SVCVL = round(L_PGRVL * 0.12,2);
        set L_ACSVL = round(L_SVCVL * 0.02,2);
        set L_ATXVL = L_SVCVL + L_ACSVL;

        set L_TDSVL = round((L_PGRVL+L_ATXVL)*0.05,2);
        set L_SCHVL = round(L_TDSVL * 0.10,2);
        set L_LCSVL = round((L_TDSVL+L_SCHVL)*0.02,2);
        set L_LTXVL  = L_TDSVL+L_SCHVL+L_LCSVL;

        set L_PNTVL = L_PGRVL + L_ATXVL - L_LTXVL;
        set L_TRNFL = '0';
        set L_STSFL = '1';
        set L_LUSBY = 'SYS';
        set L_LUPDT = current_date;
        set L_GRPCD = L_PRTCD;
        select isnull(pt_grpcd,pt_prtcd)  into L_GRPCD from spldata.co_ptmst where pt_prttp= L_PRTTP and pt_prtcd = L_PRTCD;

           select count(*)  into L_RECCT from spldata.mr_pttrn where PT_CMPCD = LP_CMPCD and PT_INVNO = LP_INVNO and PT_PRDCD = L_PRDCD and PT_PKGTP = L_PKGTP and PT_CRDTP = L_CRDTP and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_SRLNO = L_SRLNO;
          if L_RECCT = 0 then
           insert into spldata.mr_pttrn (PT_CMPCD,  PT_INVNO   ,PT_PRDCD  ,PT_PKGTP   ,PT_CRDTP  ,PT_PRTTP   ,PT_PRTCD ,  PT_SRLNO  ,PT_TRNTP,PT_DOCRF ,PT_DOCDT ,PT_INVQT   ,PT_TRNRT , PT_PGRVL, PT_ATXVL, PT_LTXVL,  PT_PNTVL, PT_INTVL, PT_GRPCD, PT_MKTTP,PT_SBSCD1, PT_TRNFL  ,PT_STSFL   ,PT_LUSBY  ,PT_LUPDT)  values (LP_CMPCD,  LP_INVNO   ,L_PRDCD  ,L_PKGTP   ,L_CRDTP  ,L_PRTTP   ,L_PRTCD ,  L_SRLNO  ,L_TRNTP,L_DOCRF ,L_DOCDT ,L_INVQT   ,L_TRNRT , L_PGRVL, L_ATXVL, L_LTXVL,  L_PNTVL, L_INTVL, L_GRPCD_DSR, LP_MKTTP,L_SBSCD1, L_TRNFL  ,L_STSFL   ,L_LUSBY  ,L_LUPDT);
       else 
         update spldata.mr_pttrn set PT_TRNTP = L_TRNTP,PT_DOCDT  = L_DOCDT ,PT_INVQT   = L_INVQT   ,PT_TRNRT  = L_TRNRT , PT_PGRVL  = L_PGRVL , PT_ATXVL  = L_ATXVL , PT_LTXVL  = L_LTXVL ,  PT_PNTVL  = L_PNTVL , PT_INTVL  = L_INTVL , PT_GRPCD  = L_GRPCD_DSR , PT_MKTTP  = LP_MKTTP ,PT_SBSCD1  = L_SBSCD1 , PT_TRNFL  = L_TRNFL  ,PT_STSFL  = L_STSFL   ,PT_LUSBY  = L_LUSBY  ,PT_LUPDT  = L_LUPDT  where PT_CMPCD = LP_CMPCD and  PT_INVNO  = LP_INVNO   and PT_PRDCD = L_PRDCD  and PT_PKGTP  = L_PKGTP   and PT_CRDTP  = L_CRDTP  and PT_PRTTP  = L_PRTTP   and PT_PRTCD = L_PRTCD and  PT_SRLNO = L_SRLNO ;
       end if;
   end if;
   -- end if;    -- L_PMTFL = 'Y'
   set END_TABLE = 0;
   fetch C_INV into  L_PRDCD, L_PKGTP, L_BYRCD,L_CNSCD, L_DSRTP, L_DSRCD, L_INVQT, L_INDNO, L_SALTP,L_ZONCD,L_SBSCD1, L_INVDT;
  end while;
  close C_INV; 
  --commit;

 set END_TABLE = 0;
 open C_DOC;
  fetch C_DOC into  L_CRDTP,L_PRTTP, L_PRTCD;
  while END_TABLE = 0 DO 
     select char(cmt_codcd + right('00000' + trim(char(int(cmt_ccsvl)+1)),5)) into L_DOCRF from spldata.co_cdtrn where cmt_cgmtp='D' + LP_CMPCD and cmt_cgstp='MRXXPTT' and SUBSTRING(cmt_codcd,2,2) = L_CRDTP;
    update spldata.MR_PTTRN set PT_DOCRF = L_DOCRF where PT_CMPCD=LP_CMPCD and PT_INVNO = LP_INVNO and PT_CRDTP = L_CRDTP and PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_DOCRF = '00000000';
     update spldata.co_cdtrn set CMT_CCSVL = SUBSTRING(L_DOCRF,4,5) where cmt_cgmtp='D' + LP_CMPCD and cmt_cgstp='MRXXPTT' and SUBSTRING(cmt_codcd,2,2) = L_CRDTP;
    set END_TABLE = 0;
    fetch C_DOC into  L_CRDTP,L_PRTTP, L_PRTCD;
  end while;
  close C_DOC;


end;
commit;
;
;

/*   Distributor Commission rate fetching old logic 
      -- select CMT_NMP01 into L_TRNRT from spldata.co_cdtrn where cmt_cgmtp='D' + LP_CMPCD and cmt_cgstp = 'MR' + LP_MKTTP + 'IND' and cmt_codcd=SUBSTRING(L_INDNO,1,4);
      -- if L_SALTP = '01'  then 
      --   select isnull(PT_DMCRT,0) PT_DMCRT  into  L_TRNRT from spldata.CO_PTMST where PT_PRTTP = L_DSRTP and PT_PRTCD=L_DSRCD;
      -- end if;
      -- if L_SALTP = '03'  then 
      --    select isnull(PT_DECRT,0) PT_DECRT  into  L_TRNRT from spldata.CO_PTMST where PT_PRTTP = L_DSRTP and PT_PRTCD=L_DSRCD;
      -- end if;
      Distributor Commission rate fetching old logic  */


/*  Distributor commission tax detail saving part eliminated
     --set END_TABLE = 0;
     --open C_PTT;
     --fetch C_PTT into L_DOCRF, L_SBSCD;
     --while END_TABLE = 0 DO 

      --  set L_SYSCD = 'MR';
      --  set L_SBSTP = LP_MKTTP;
      --  set L_PRDCD = 'XXXXXXXXXX';
      --  set L_TRNFL = '0';
      --  set L_STSFL = '1';
      --  set L_LUSBY = 'SYS';
      --  set L_LUPDT = current_date;

      --  set L_DOCTP = 'CRA';
      --  set L_SVCVL = 0.10;
      --  set L_SVCFL = 'P';
      --  set L_EDCVL = 0.02;
      --  set L_EDCFL = 'P';
      --  set L_TDSVL = 0.00;
      --  set L_TDSFL = ' ';
      --  set L_SCHVL = 0.00;
      --  set L_SCHFL = ' ';

      --     select count(*)  into L_RECCT from spldata.co_txdoc where TX_SYSCD = L_SYSCD and TX_SBSTP = L_SBSTP and TX_DOCTP =  L_DOCTP  and TX_DOCNO = L_DOCRF and TX_PRDCD = L_PRDCD;

      --    if L_RECCT = 0 then 
      --     insert into spldata.co_txdoc (TX_SYSCD, TX_SBSTP, TX_DOCTP, TX_DOCNO, TX_PRDCD, TX_EDCVL, TX_EDCFL, TX_SCHVL, TX_SCHFL, TX_SVCVL, TX_SVCFL,TX_TDSVL, TX_TDSFL, TX_SBSCD, TX_STSFL, TX_TRNFL, TX_LUSBY, TX_LUPDT)  values (L_SYSCD, L_SBSTP, L_DOCTP, L_DOCRF, L_PRDCD, L_EDCVL, L_EDCFL, L_SCHVL, L_SCHFL,L_SVCVL, L_SVCFL,L_TDSVL, L_TDSFL, L_SBSCD, L_STSFL, L_TRNFL, L_LUSBY, L_LUPDT);
     --  else 
     --    update spldata.co_txdoc set TX_SYSCD = L_SYSCD , TX_SBSTP  = L_SBSTP , TX_DOCTP  = L_DOCTP , TX_DOCNO  = L_DOCRF , TX_PRDCD  = L_PRDCD , TX_EDCVL  = L_EDCVL , TX_EDCFL  = L_EDCFL , TX_SCHVL  = L_SCHVL , TX_SCHFL  = L_SCHFL ,TX_SVCVL  = L_SVCVL , TX_SVCFL  = L_SVCFL ,TX_TDSVL  = L_TDSVL , TX_TDSFL  = L_TDSFL , TX_SBSCD  = L_SBSCD , TX_STSFL  = L_STSFL , TX_TRNFL  = L_TRNFL , TX_LUSBY  = L_LUSBY , TX_LUPDT  = L_LUPDT  where TX_SYSCD = L_SYSCD and TX_SBSTP = L_SBSTP and TX_DOCTP =  L_DOCTP  and TX_DOCNO = L_DOCRF and TX_PRDCD = L_PRDCD;
     --   end if;

     --   set L_DOCTP = 'CRD';
     --   set L_SVCVL = 0.00;
     --   set L_SVCFL = ' ';
     --   set L_EDCVL = 0.02;
     --   set L_EDCFL = 'P';
     --   set L_TDSVL = 0.05;
     --   set L_TDSFL = 'P';
     --   set L_SCHVL = 10.00;
     --   set L_SCHFL = 'P';

     --   set L_TRNFL = '0';
     --   set L_STSFL = '1';
     --   set L_LUSBY = 'SYS';
     --   set L_LUPDT = current_date;

     --      select count(*)  into L_RECCT from spldata.co_txdoc where TX_SYSCD = L_SYSCD and TX_SBSTP = L_SBSTP and TX_DOCTP =  L_DOCTP  and TX_DOCNO = L_DOCRF and TX_PRDCD = L_PRDCD;

     --     if L_RECCT = 0 then 
     --      insert into spldata.co_txdoc (TX_SYSCD, TX_SBSTP, TX_DOCTP, TX_DOCNO, TX_PRDCD, TX_EDCVL, TX_EDCFL, TX_SCHVL, TX_SCHFL, TX_SVCVL, TX_SVCFL,TX_TDSVL, TX_TDSFL, TX_SBSCD, TX_STSFL, TX_TRNFL, TX_LUSBY, TX_LUPDT)  values (L_SYSCD, L_SBSTP, L_DOCTP, L_DOCRF, L_PRDCD, L_EDCVL, L_EDCFL, L_SCHVL, L_SCHFL,L_SVCVL, L_SVCFL,L_TDSVL, L_TDSFL, L_SBSCD, L_STSFL, L_TRNFL, L_LUSBY, L_LUPDT);
     --  else 
     --    update spldata.co_txdoc set TX_SYSCD = L_SYSCD , TX_SBSTP  = L_SBSTP , TX_DOCTP  = L_DOCTP , TX_DOCNO  = L_DOCRF , TX_PRDCD  = L_PRDCD , TX_EDCVL  = L_EDCVL , TX_EDCFL  = L_EDCFL , TX_SCHVL  = L_SCHVL , TX_SCHFL  = L_SCHFL ,TX_SVCVL  = L_SVCVL , TX_SVCFL  = L_SVCFL ,TX_TDSVL  = L_TDSVL , TX_TDSFL  = L_TDSFL , TX_SBSCD  = L_SBSCD , TX_STSFL  = L_STSFL , TX_TRNFL  = L_TRNFL , TX_LUSBY  = L_LUSBY , TX_LUPDT  = L_LUPDT  where TX_SYSCD = L_SYSCD and TX_SBSTP = L_SBSTP and TX_DOCTP =  L_DOCTP  and TX_DOCNO = L_DOCRF and TX_PRDCD = L_PRDCD;
     --   end if;

     --   fetch C_PTT into L_DOCRF, L_SBSCD;
     --end while;
  --  close C_PTT;
  Distributor commission tax detail saving part eliminated  */
;


select distinct ivt_cmpcd,ivt_mkttp,ivt_invno,ivt_invdt from spldata.mr_ivtrn where CONVERT(varchar,ivt_invdt,101) > '03/31/2009' and ivt_mkttp in ('01','04','05')  and ivt_stsfl<>'X' and ivt_saltp not in ('04','05','14','15','16','21') and SUBSTRING(ivt_invno,2,1) <> '5'  and isnull(ivt_cc1vl,0)>0 and ivt_cmpcd + ivt_invno not in (select pt_cmpcd + pt_invno from spldata.mr_pttrn where pt_crdtp='01' and pt_docdt > '03/31/2009') order by ivt_cmpcd,ivt_mkttp,ivt_invno;

call spldata.updPTTRN_INV('01','01','00008862');

commit;

select * from spldata.mr_pttrn where pt_invno='00008862';
