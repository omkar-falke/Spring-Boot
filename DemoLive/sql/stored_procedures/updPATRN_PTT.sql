
-- procedure to create/update data in Payment Adjustment  transaction using Credit Note & Invoice details from Party ledger 
-- This procedure is to be executed whenever the invoice is approved for Gate-out.
-- Procedure is called through mm_teexa.java

-- Sequence of procedure execution   1. updCNTRN_INV               Credit Note records creation (in MR_CNTRN) from Invoice Dtata
--                                                   2. updPLTRN_PTT              Credit Note records creation/updation in Party Ledger (MR_PLTRN)
--                   l                               3. updPATRN_PTT              Credit Note records adjustments (Records generation in MR_PATRN)

-- Auto adjustment takes place only for Booking Discount & Distributor Commission category
--                                                   01. Bkg.Disc. Buyer     02.  Bkg.Discount Distr.      03. Distributor Commission

-- FEATURES INTRODUCED
-- When Credit Note is gemerated, and Credit Note record is posted in Party Ledger
-- A multiple records are generated in Pmt.Adjustment table. Multiple credit note are adjusted against One Invoice
-- The credit note amount is adjusted against the Invoice, from which the credit note is generated.
-- If there is no outstanding amount available for adjustment (against corresponding invoice) then the credit note is left unadjusted.
-- such credit notes have to be processed through manual interaction. (Credit nNote Adjustment module)
-- New record generated will have
--       1. Key fields from Party Ledger - Credit Side (Credit Note record from Party Ledger)
--                                                     Debit Side (Invoice Credit Note record from Party Ledger)
--       2. Credit Amount Adjusted.
--       The adjustment amount of Invice Record in Party Ledger is also increased (by Credit Amount)
-- After adjustment is carried out, The credit note record is locked for further operations (Status flag is set to '9')




drop procedure spldata.updPATRN_PTT;
commit;

create procedure spldata.updPATRN_PTT(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_INVNO char(8))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_CRDTP char(2);
  declare L_CRDNO char(8);
  declare L_DBTTP char(2);
  declare L_DBTNO char(8);
  declare L_CRDVL decimal(12,2);
  declare L_DBTVL decimal(12,2);
  declare L_ADJVL decimal(12,2);
  declare L_ACCRF char(8);
  declare L_ACCDT date;
  declare L_GRPCD char(5);
  declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_PTT cursor for  select  PL_PRTTP,PL_PRTCD, PL_DOCTP, PL_DOCNO, PL_DOCVL, PL_GRPCD, PL_SBSCD from spldata.MR_PLTRN where PL_DOCTP in ('01') and PL_DOCNO in (select PT_DOCRF from spldata.MR_PTTRN where PT_MKTTP = LP_MKTTP and PT_INVNO = LP_INVNO) and PL_STSFL <> '9';


  declare C_PAD cursor for  select  PA_PRTTP,PA_PRTCD, PA_CRDTP, PA_CRDNO, sum(isnull(PA_ADJVL,0))  PA_ADJVL from spldata.MR_PATRN where PA_CRDTP in ('01','02','03') and PA_DBTTP='21' and PA_DBTNO = LP_INVNO group by PA_PRTTP,PA_PRTCD, PA_CRDTP, PA_CRDNO  order by PA_PRTTP,PA_PRTCD, PA_CRDTP, PA_CRDNO;


  declare continue handler for not found 
       set END_TABLE = 1; 

 set END_TABLE = 0;
  open C_PTT;
  fetch C_PTT into  L_PRTTP,L_PRTCD, L_CRDTP, L_CRDNO, L_CRDVL, L_GRPCD, L_SBSCD;

  while END_TABLE = 0 DO 
     set L_DBTTP = '21';
     set L_DBTNO = 'xxxxxxxx';
      select distinct PT_INVNO into L_DBTNO from spldata.MR_PTTRN where PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD and PT_CRDTP = L_CRDTP and PT_DOCRF = L_CRDNO;
       -- insert into spldata.temp_chk values ('L_DBTNO: '||L_DBTNO);
      select  isnull(PL_DOCVL,0)-isnull(PL_ADJVL,0) into L_DBTVL from spldata.MR_PLTRN where PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DBTTP and PL_DOCNO  = L_DBTNO;

            set  L_ADJVL   = L_CRDVL;
            set  L_ACCRF  = '    ';
            set  L_TRNFL = '0';
            set  L_STSFL = '1';
            set  L_LUSBY = 'SYS';
            set  L_LUPDT = current_date;
            -- insert into spldata.temp_chk values ('L_CRVL : '||char(L_CRDVL) || '    L_DBTVL : '||char(L_DBTVL));

    set L_RECCT = 0;
     if L_CRDVL <= L_DBTVL then 
        select count(*) into L_RECCT from spldata.mr_patrn where PA_CMPCD = LP_CMPCD and PA_PRTTP = L_PRTTP and PA_PRTCD = L_PRTCD and PA_CRDTP = L_CRDTP and PA_CRDNO = L_CRDNO and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO;
            -- insert into spldata.temp_chk values ('L_RECCT : '||char(L_RECCT));

       if L_RECCT = 1 then
          update spldata.mr_patrn set PA_ADJVL = L_CRDVL where PA_CMPCD = LP_CMPCD and PA_PRTTP = L_PRTTP and PA_PRTCD = L_PRTCD and PA_CRDTP = L_CRDTP and PA_CRDNO = L_CRDNO and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO;
       end if;
       if L_RECCT = 0 then
           insert into spldata.mr_patrn (PA_CMPCD,PA_PRTTP,PA_PRTCD,PA_CRDTP,PA_CRDNO,PA_DBTTP,PA_DBTNO,PA_ADJVL,PA_ACCRF,PA_MKTTP,PA_GRPCD,PA_SBSCD,PA_TRNFL,PA_STSFL,PA_LUSBY,PA_LUPDT)
 values (LP_CMPCD ,L_PRTTP,L_PRTCD,L_CRDTP,L_CRDNO,L_DBTTP,L_DBTNO,L_ADJVL,L_ACCRF,LP_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
        end if;

     end if;

  fetch C_PTT  into  L_PRTTP,L_PRTCD, L_CRDTP, L_CRDNO, L_CRDVL, L_GRPCD, L_SBSCD;
  end while;
  close C_PTT;
   select  sum(PA_ADJVL) into L_ADJVL from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_DBTTP = L_DBTTP and PA_DBTNO = LP_INVNO;
    update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL where PL_CMPCD = LP_CMPCD and  PL_DOCTP = L_DBTTP and PL_DOCNO  = LP_INVNO;

  set END_TABLE = 0;
  open C_PAD;
  fetch C_PAD into  L_PRTTP,L_PRTCD, L_CRDTP, L_CRDNO, L_ADJVL;

  while END_TABLE = 0 DO 
       update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_CRDTP and PL_DOCNO = L_CRDNO;
        fetch C_PAD into  L_PRTTP,L_PRTCD, L_CRDTP, L_CRDNO, L_ADJVL;
  end while;
  close C_PAD;
  --commit;
end P1;


commit;


delete from spldata.temp_chk;
commit;
call spldata.updPATRN_CRN('01','01','70000842');
call spldata.updPATRN_CRN('01','01','70000843');
commit;
select * from spldata.temp_chk;
select * from spldata.mr_pltrn;
select * from spldata.mr_patrn;
