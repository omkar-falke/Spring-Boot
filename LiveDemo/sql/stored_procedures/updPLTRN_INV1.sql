
-- procedure to create/update data in Party Ledger transaction using Invoice details
-- This procedure is to be executed whenever the invoice is approved for Gate-out.
-- Procedure is called through mm_teexa.java

-- FEATURES INTRODUCED
--  When invoice is gemerated, A single transaction is introduced in MR_PLTRN (Party Ledger) with Total Invoice Value.
-- New record generated will have
--        Party Group Code (Picked up from Party Master (CO_PTMST) for the specfied Buyer.
--        Next Sequence Number (Transaction Processing sequence number for the Party Group)
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Invoice Amount from Closing balance of previous sequence number)


select * from spldata.mr_ivtrn;


drop procedure spldata.updPLTRN_INV1;
commit;

create procedure spldata.updPLTRN_INV1(IN LP_CMPCD char(2),IN LP_MKTTP char(2), IN LP_INVNO char(8), IN LP_LDGFL char(1))  language sql  modifies sql data 
P1:
begin
  declare L_PRTTP char(1);
  declare L_PRTCD char(5);
  declare L_DOCTP char(2);
  declare L_DOCNO char(8);
  declare L_DOCDT date;
  declare L_DUEDT date;
  declare L_SEQNO decimal(10);
  declare L_DOCVL decimal(12,2);
  declare L_BALVL decimal(12,2);
  declare L_BALFL char(2);
  declare L_BALVL1 decimal(12,2);
  declare L_BALFL1 char(2);
  declare L_ADJVL decimal(12,2);
  declare L_ACCRF char(8);
  declare L_ACCDT date;
  declare L_BYRCD char(5);
  declare L_GRPCD char(5);
  declare L_SBSCD char(6);
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;

  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_INV cursor for  select  distinct IVT_BYRCD, IVT_INVNO, date(IVT_INVDT) IVT_INVDT,IVT_PMDDT, IVT_SBSCD, ifnull(ivt_invvl,0)  ivt_invvl from spldata.mr_ivtrn   where   IVT_CMPCD=LP_CMPCD AND IVT_MKTTP = LP_MKTTP and IVT_INVNO=LP_INVNO and IVT_INVQT > 0 and IVT_STSFL <> 'X' ;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_INV ;
  fetch C_INV into  L_BYRCD, L_DOCNO, L_DOCDT,L_DUEDT, L_SBSCD, L_DOCVL;
  set L_PRTTP = 'C';

  set L_PRTCD = L_BYRCD;

  select distinct ifnull(PT_GRPCD,PT_PRTCD) into L_GRPCD from spldata.CO_PTMST where PT_PRTTP = L_PRTTP and PT_PRTCD = L_PRTCD;

  if length(L_GRPCD) < 5 then 
     set L_GRPCD = L_PRTCD;
  end if;
  set L_PRTCD = L_BYRCD;
  set L_DOCTP = '21';
  set L_SEQNO = 0;
  set L_BALVL = 0.00;
  set L_BALFL = 'DB';
  set L_BALVL1 = 0.00;
  set L_BALFL1 = 'DB';
  set L_ADJVL = 0.00;
  set L_ACCRF = '    ';
  set L_ACCDT = null;
  set L_TRNFL = '0';
  set L_STSFL = '1';
  set L_LUSBY = 'SYS';
  set L_LUPDT = current_date;


     select ifnull(PL_SEQNO,0) PL_SEQNO into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = '21' and PL_DOCNO = LP_INVNO;
  -- If the Invoice Record exists in payment ledger, only Invoice value is over written and closing balances for subsequent transactions (within the party) are reworked

     if L_SEQNO > 0 and LP_LDGFL = 'Y' then 
         update spldata.mr_pltrn set PL_DOCVL = L_DOCVL where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = LP_INVNO;
         call spldata.rwkPLTRN_TRN(LP_CMPCD, L_PRTTP, L_PRTCD,L_DOCTP, LP_INVNO, L_SEQNO);
     end if;


     if L_SEQNO =0 then 
        if LP_LDGFL = 'Y' then
           select max(ifnull(PL_SEQNO,0))  into L_SEQNO from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD;
           set L_SEQNO = ifnull(L_SEQNO,0);
           if L_SEQNO > 0 then
               select  ifnull(PL_BALVL,0), ifnull(PL_BALFL,'DB')  into  L_BALVL, L_BALFL from spldata.MR_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = L_PRTTP and PL_PRTCD = L_PRTCD and PL_SEQNO = L_SEQNO;
           end if;

           if L_BALFL = 'DB'  then
               set L_BALVL1 = ifnull(L_BALVL,0) + ifnull(L_DOCVL,0);
               set L_BALFL1 = 'DB';
           else 
               set L_BALVL1 = ifnull(L_BALVL,0) - ifnull(L_DOCVL,0);
               set L_BALFL1 = 'CR';
           end if;

    -- After deducting debit amount, if the balance goes negative,  Then it is a Credit Balancde
           if L_BALVL1 < 0 then
              set L_BALVL1 = ifnull(L_BALVL1,0) * (-1);
              set L_BALFL1 = 'DB';
           end if;
      end if;
       insert into spldata.mr_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DUEDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_MKTTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT) values  (LP_CMPCD,L_PRTTP,L_PRTCD,L_DOCTP,L_DOCNO,L_SEQNO+1,L_DOCDT,L_DUEDT,L_DOCVL,L_BALVL1,L_BALFL1,L_ADJVL,L_ACCRF,L_ACCDT,LP_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
      update spldata.co_ptmst set pt_ytdvl = L_BALVL1, pt_ytdfl = L_BALFL1 where pt_prttp = L_PRTTP and PT_PRTCD = L_PRTCD;

  end if;
  close C_INV;
  --commit;
end P1;


commit;


--select * from spldata.mr_ivtrn where date(ivt_invdt) = '06/05/2006';

call spldata.updPLTRN_INV1('01','01','70002372','N');
commit;


select * from spldata.mr_pltrn where pl_docno='70002372';
