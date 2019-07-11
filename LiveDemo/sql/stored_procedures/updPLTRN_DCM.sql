
-- procedure to create/update data in Party Ledger transaction using Credit Note details for Distributors
-- Invoicewise Distributor Commission and Distributor booking discount figures are already available (generated) in mr_pttrn
-- A single record is posted in party ledger (mr_pltrn)  against many selected records in Payment Transactions (mr_pttrn)
-- This procedure is to be executed when Distributor Commission processing is carried out by user (mr_tecrt.java)

-- FEATURES INTRODUCED
-- New record generated will have
--        Credit category,Distributor code, document number from parameters supplied. 
--        Tax details against the document are captured from tax record in common tax table (co_txdoc)
--        Tax is applied on Total Gross value (of the selected transactions in MR_PTTRN) net value is arrived at and is recorded as document value in party ledger.
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Credit Note Amount from Closing balance of previous sequence number)

;

drop procedure spldata.updPLTRN_DCM;
commit;

create procedure spldata.updPLTRN_DCM(IN LP_CMPCD char(2),IN LP_CRDTP char(2), IN LP_DSRCD char(5), LP_DOCRF char(8), IN LP_DOCDT char(10),  IN LP_PNTVL decimal (12,2))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1) default 'D';
  declare L_PRTCD char(5);
  declare L_GRPCD char(5);
  declare L_DOCTP char(2);
  declare L_DOCNO char(8);
  declare L_DOCDT date;
  declare L_SEQNO decimal(10);
  declare L_PNTVL decimal(12,2);
  declare L_DOCVL decimal(12,2);
  declare L_BALVL decimal(12,2);
  declare L_BALFL char(2);
  declare L_BALVL1 decimal(12,2);
  declare L_BALFL1 char(2);
  declare L_ADJVL decimal(12,2);
  declare L_ACCRF char(8);
  declare L_ACCDT date;
  declare L_MKTTP char(2) default '00';
  declare L_SBSCD char(6) default '000000';
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;
  declare L_PLTRN_PUSH char(1);

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;


  declare continue handler for not found 
       set END_TABLE = 1; 

  set L_PRTTP = 'D';
  set L_PRTCD = LP_DSRCD;
  set L_GRPCD = L_PRTCD;
  set L_DOCTP = LP_CRDTP;
  set L_DOCNO = LP_DOCRF;
  set L_DOCDT = LP_DOCDT;
  set L_DOCVL = LP_PNTVL;
      set L_SEQNO = 0;
      set L_BALVL = 0.00;
      set L_BALFL = 'CR';
      set L_BALVL1 = 0.00;
      set L_BALFL1 = 'CR';
      set L_ADJVL = 0.00;
      set L_ACCRF = '    ';
      set L_ACCDT = null;
      set L_TRNFL = '0';
      set L_STSFL = '1';
      set L_LUSBY = 'SYS';
      set L_LUPDT = current_date;

      select isnull(PL_SEQNO,0) PL_SEQNO into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = L_DOCNO;


      -- If the Credit Note Record exists in payment ledger, only Credit Note value is over written and closing balance is reworked for subsequent transactions
      if L_SEQNO > 0 then 
          update spldata.mr_pltrn set PL_DOCVL = L_DOCVL where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP  and PL_DOCNO = L_DOCNO;
          call spldata.rwkPLTRN_TRN(LP_CMPCD, L_PRTTP, L_PRTCD, L_DOCTP, L_DOCNO, L_SEQNO);
      end if;


     if L_SEQNO =0 then 
        call spldata.rwkPLTRN_SRL(LP_CMPCD, L_PRTTP, L_PRTCD,L_DOCDT, L_DOCTP, L_DOCNO,L_SEQNO);
        set L_PLTRN_PUSH = '0';
        if L_SEQNO > 0 then
            set L_PLTRN_PUSH = '1';
        end if;
        --insert into spldata.temp_chk values ('Received Seq.No. : '||char(L_SEQNO));
        if L_PLTRN_PUSH = '0'  then     
           select max(isnull(PL_SEQNO,0))  into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;
           set L_SEQNO = isnull(L_SEQNO,0)+1;
        end if;

         if L_SEQNO > 1 then
             select  isnull(PL_BALVL,0), isnull(PL_BALFL,'DB')  into  L_BALVL, L_BALFL from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_SEQNO = L_SEQNO-1;
         end if;

        if L_BALFL = 'CR'  then
            set L_BALVL1 = isnull(L_BALVL,0) + isnull(L_DOCVL,0);
            set L_BALFL1 = 'CR';
        else 
           set L_BALVL1 = isnull(L_BALVL,0) - isnull(L_DOCVL,0);
           set L_BALFL1 = 'DB';
         end if;

        -- After deducting credit amount, if the balance goes negative,  Then it is a Credit Balancde
         if L_BALVL1 < 0 then
            set L_BALVL1 = isnull(L_BALVL1,0) * (-1);
            set L_BALFL1 = 'CR';
         end if;

       insert into spldata.mr_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_MKTTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT) values  (LP_CMPCD,L_PRTTP,L_PRTCD,L_DOCTP,L_DOCNO,L_SEQNO,L_DOCDT,L_DOCVL,L_BALVL1,L_BALFL1,L_ADJVL,L_ACCRF,L_ACCDT,L_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
      update spldata.co_ptmst set pt_ytdvl = L_BALVL1, pt_ytdfl = L_BALFL1 where pt_prttp = L_PRTTP and PT_PRTCD = L_PRTCD;
       if L_PLTRN_PUSH = '1' then
          call spldata.rwkPLTRN_TRN(LP_CMPCD,L_PRTTP, L_PRTCD, L_DOCTP, L_DOCNO, L_SEQNO);
       end if;

      end if;
end P1;


commit;



call spldata.updPLTRN_DCM('01','02','O0001','70200001','08/03/2006',11919.0);
commit;

select * from spldata.co_cdtrn where cmt_cgmtp='DOC' and cmt_cgstp='MRXXPTT' and cmt_codcd='702';


