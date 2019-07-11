
-- procedure to create/update data in Payment Adjustment  transaction using Advance Payment Receipt & Invoice details from Party ledger 
-- This procedure is to be executed whenever the invoice is approved for Gate-out.
-- Procedure is called through mm_teexa.java

-- Sequence of procedure execution   1. updCNTRN_INV               Credit Note records creation (in MR_CNTRN) from Invoice Dtata
--                                                   2. updPLTRN_PTT              Credit Note records creation/updation in Party Ledger (MR_PLTRN)
--                                                   3. updPATRN_PTT              Credit Note records adjustments (Records generation in MR_PATRN)
--                                                   4. updPATRN_ADV              Adance Payment Receipts adjustments (Records generation in MR_PATRN)

-- Auto adjustment takes place only for Booking Discount & Distributor Commission category
--                                                   01. Bkg.Disc. Buyer     02.  Bkg.Discount Distr.      03. Distributor Commission

-- FEATURES INTRODUCED
-- When Advance Payment is entered Payment Receipt record is posted in Party Ledger
-- A single records is generated in Pmt.Adjustment table. for every Invoice.
-- The Receipt adjustment amount is recorded against the Invoice.
-- New record generated will have
--       1. Key fields from Party Ledger - Credit Side   (Advance Payment Receipt details)
--                                                       Debit Side    (Invoice record from Party Ledger)
--       2. Advance Receipt Amount Adjusted.
--       The adjustment amount of Invice Record in Party Ledger is also increased (by Receipt Adjustment Amount)



drop procedure spldata.updPATRN_ADV;
commit;

create procedure spldata.updPATRN_ADV(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_INVNO char(8))  language sql  modifies sql data 
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
  declare L_RUNDBT decimal(12,2);
  declare L_ACCRF char(8);
  declare L_ACCDT date;
  declare L_GRPCD char(5);
  declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);	   -- status flag for record generated in MR_PATRN
  declare L_LUSBY char(5);
  declare L_LUPDT date;

  declare L_STSFL1 char(1);       -- Status flag for  credit record in MR_PLTRN

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_ADV  cursor for  select  PL_PRTTP,PL_PRTCD, PL_DOCTP, PL_DOCNO, isnull(PL_DOCVL,0)-isnull(PL_ADJVL,0) PL_DOCVL, PL_GRPCD, PL_SBSCD from spldata.MR_PLTRN where PL_DOCTP in ('11','12') and PL_PRTTP = 'C' and PL_PRTCD in (select PL_PRTCD  from spldata.MR_PLTRN where PL_PRTTP = 'C' and  isnull(PL_DOCVL,0) > isnull(PL_ADJVL,0)) and  PL_PRTTP + PL_PRTCD in (select PL_PRTTP+PL_PRTCD from spldata.mr_pltrn where PL_DOCTP = L_DBTTP and PL_DOCNO = L_DBTNO and isnull(PL_DOCVL,0) > isnull(PL_ADJVL,0)) order by PL_PRTTP,PL_PRTCD, PL_DOCTP, PL_DOCNO;


  declare continue handler for not found 
       set END_TABLE = 1; 


     set L_DBTTP = '21';
     set L_DBTNO = LP_INVNO;
     select  isnull(PL_DOCVL,0)-isnull(PL_ADJVL,0) into L_DBTVL from spldata.MR_PLTRN where  PL_DOCTP = L_DBTTP and PL_DOCNO  = L_DBTNO;
  

  set END_TABLE = 0;
  open C_ADV;
  fetch C_ADV  into  L_PRTTP,L_PRTCD, L_CRDTP, L_CRDNO, L_CRDVL, L_GRPCD, L_SBSCD;

  set L_STSFL1 = '9';
  set L_RUNDBT = L_DBTVL;
  while END_TABLE = 0 DO 
      -- insert into spldata.temp_chk values ('L_DBTNO: '+L_DBTNO);
      if L_RUNDBT > 0  and L_CRDVL > 0 then

               set  L_ADJVL   = L_CRDVL;
               set L_STSFL = '9';
               if L_CRDVL > L_RUNDBT  then
                  set L_ADJVL = L_RUNDBT;
                  set L_STSFL1 = '1';
               end if;
               set L_RUNDBT = L_RUNDBT - L_ADJVL;
               set  L_ACCRF  = '    ';
               set  L_TRNFL = '0';
               set  L_LUSBY = 'SYS';
               set  L_LUPDT = current_date;
               -- insert into spldata.temp_chk values ('L_CRVL : '+char(L_CRDVL) + '    L_DBTVL : '+char(L_DBTVL));

               set L_RECCT = 0;
              select count(*) into L_RECCT from spldata.mr_patrn where PA_CMPCD = LP_CMPCD and PA_PRTTP = L_PRTTP and PA_PRTCD = L_PRTCD and PA_CRDTP = L_CRDTP and PA_CRDNO = L_CRDNO and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO;
              --insert into spldata.temp_chk values ('L_RECCT : '+char(L_RECCT));

               if L_RECCT = 1 then
                   update spldata.mr_patrn set PA_ADJVL = L_ADJVL where PA_CMPCD = LP_CMPCD and PA_PRTTP = L_PRTTP and PA_PRTCD = L_PRTCD and PA_CRDTP = L_CRDTP and PA_CRDNO = L_CRDNO and PA_DBTTP = L_DBTTP and PA_DBTNO = L_DBTNO;
               end if;
               if L_RECCT = 0 then
                   insert into spldata.mr_patrn  (PA_CMPCD,PA_PRTTP,PA_PRTCD,PA_CRDTP,PA_CRDNO,PA_DBTTP,PA_DBTNO,PA_ADJVL,PA_ACCRF,PA_MKTTP,PA_GRPCD,PA_SBSCD,PA_TRNFL,PA_STSFL,PA_LUSBY,PA_LUPDT)  values (LP_CMPCD,L_PRTTP,L_PRTCD,L_CRDTP,L_CRDNO,L_DBTTP,L_DBTNO,L_ADJVL,L_ACCRF,LP_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
               end if;

               select  sum(PA_ADJVL) into L_ADJVL from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_PRTTP = L_PRTTP and PA_PRTCD = L_PRTCD and PA_CRDTP = L_CRDTP and PA_CRDNO = L_CRDNO;
               update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL, PL_STSFL = L_STSFL1 where PL_CMPCD = LP_CMPCD and  PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_CRDTP and PL_DOCNO  = L_CRDNO;
        end if;
        set END_TABLE = 0;
        fetch C_ADV  into  L_PRTTP,L_PRTCD, L_CRDTP, L_CRDNO, L_CRDVL, L_GRPCD, L_SBSCD;
      end while;
      close C_ADV;
      select  sum(PA_ADJVL) into L_ADJVL from spldata.MR_PATRN where PA_CMPCD = LP_CMPCD and PA_DBTTP = L_DBTTP and PA_DBTNO = LP_INVNO;
      update spldata.MR_PLTRN set PL_ADJVL = L_ADJVL where PL_CMPCD = LP_CMPCD and  PL_DOCTP = L_DBTTP and PL_DOCNO  = LP_INVNO;
  --commit;
end P1;


commit;

select  PL_PRTTP,PL_PRTCD, PL_DOCTP, PL_DOCNO, PL_DOCVL, PL_GRPCD, PL_SBSCD from spldata.MR_PLTRN where PL_DOCTP in ('11','12') and PL_PRTTP = 'C' and PL_PRTCD in (select PL_PRTCD  from spldata.MR_PLTRN where PL_PRTTP = 'C' and  isnull(PL_DOCVL,0) > isnull(PL_ADJVL,0)) and  PL_PRTTP+PL_PRTCD in (select PL_PRTTP+PL_PRTCD from spldata.mr_pltrn where PL_DOCTP = '21' and PL_DOCNO = '70003546' and isnull(PL_DOCVL,0) > isnull(PL_ADJVL,0));

call spldata.updPATRN_ADV('01','01','70003546');
commit;


select * from spldata.mr_patrn where pa_dbtno='70003546';
select * from spldata.mr_pltrn where pl_docno='70003546';
select * from spldata.mr_pltrn where pl_docno='71100001';
select * from spldata.mr_patrn where pa_crdno='71100001';

select * from spldata.mr_patrn where pa_dbtno='70003726';
select * from spldata.mr_pltrn where pl_docno='70003726';
select * from spldata.mr_pltrn where pl_docno='71100001';
select * from spldata.mr_patrn where pa_crdno='71100001';




call spldata.updPATRN_ADV('01','01','70003546');
commit;
call spldata.updPATRN_ADV('01','01','70003726');
commit;
call spldata.updPATRN_ADV('01','01','70004598');
commit;



call spldata.updPATRN_ADV('01','01','70003546');
commit;
call spldata.updPATRN_ADV('01','01','70004051');
call spldata.updPATRN_ADV('01','01','70003726');
call spldata.updPATRN_ADV('01','01','70004396');
call spldata.updPATRN_ADV('01','01','70004129');
commit;
call spldata.updPATRN_ADV('01','01','70004468');
call spldata.updPATRN_ADV('01','01','70004490');
call spldata.updPATRN_ADV('01','01','70004274');
call spldata.updPATRN_ADV('01','01','70004294');
commit;
call spldata.updPATRN_ADV('01','01','70004325');
call spldata.updPATRN_ADV('01','01','70004598');
call spldata.updPATRN_ADV('01','01','70004758');
call spldata.updPATRN_ADV('01','01','70004728');
commit;
call spldata.updPATRN_ADV('01','01','70004625');
call spldata.updPATRN_ADV('01','01','70004640');
call spldata.updPATRN_ADV('01','01','70004589');
call spldata.updPATRN_ADV('01','01','70003475');
commit;
call spldata.updPATRN_ADV('01','01','70003476');
call spldata.updPATRN_ADV('01','01','70003477');
call spldata.updPATRN_ADV('01','01','70003477');
call spldata.updPATRN_ADV('01','01','70005123');
commit;
call spldata.updPATRN_ADV('01','01','70005207');
call spldata.updPATRN_ADV('01','01','70004755');
call spldata.updPATRN_ADV('01','01','70005277');
call spldata.updPATRN_ADV('01','01','70005283');
call spldata.updPATRN_ADV('01','01','70005471');
commit;
call spldata.updPATRN_ADV('01','01','70004970');
call spldata.updPATRN_ADV('01','01','70005501');
call spldata.updPATRN_ADV('01','01','70005479');
call spldata.updPATRN_ADV('01','01','70005669');
commit;
;;;



select * from spldata.temp_chk;

select * from spldata.mr_patrn where pa_dbtno = '70003546';

select * from spldata.mr_pltrn where pl_docno='70003546';
select * from spldata.mr_pltrn where pl_docno='71100001';
select * from spldata.mr_ivtrn where ivt_invno='70003546';

update spldata.mr_pltrn set pl_stsfl='1',pl_adjvl=0 where pl_docno='70003546';
update spldata.mr_pltrn set pl_stsfl='1',pl_adjvl=0 where pl_docno='71100001';
commit;




