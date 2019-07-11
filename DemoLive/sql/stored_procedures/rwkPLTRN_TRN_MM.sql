-- procedure to rework Party ledger closing balance (transactionwise) from specified transaction
-- This procedure is executed whenever correction takes place in transaction, which is already present in Party Ledger

-- FEATURES INTRODUCED
-- Sequence number of previous transaction is arrived (from current sequence number)
Year opening payment outstanding (credit/debit) are recorded in Party Master along with opening date
-- In this procedure, every transaction in the party ledger, starting from opening date are processed (Considering the Starting Balance Figure)
-- While processing every transaction, the current closing balance against the transaction is overwritten
-- when all thransactions are processed, the final closing balance along with credit / debit flag is recorded against party record (in party master).


;
;
select * from SPLDATA.co_ptmst where pt_prttp ='D' and pt_prtcd like 'S%' order by pt_prtnm;

select * from SPLDATA.co_cdtrn where cmt_codcd='WSC';
update SPLDATA.co_cdtrn set cmt_codcd='WSC7' where cmt_codcd='WSC';
commit;



drop procedure SPLDATA.rwkPLTRN_TRN_MM;
commit;

create procedure SPLDATA.rwkPLTRN_TRN_MM(IN LP_CMPCD char(2), IN LP_PRTTP char(2), IN LP_PRTCD char(5), IN LP_DOCTP char(2), IN LP_DOCNO char(8), IN LP_SEQNO  decimal(10))  language sql  modifies sql data
P1:
begin
  declare L_YOPVL decimal(16);
  declare L_YOPFL char(2);
  declare L_YTDVL decimal(16);
  declare L_YTDFL char(2);

  declare L_DOCTP char(2);
  declare L_DOCVL decimal(12,2);
  declare L_TRNVL_OLD decimal(12,2);
  declare L_TRNVL_NEW decimal(12,2);
  declare L_TRNVL_CUR decimal(12,2);

  declare L_STRFL char(2);
  declare L_TRNFL_OLD char(2);
  declare L_TRNFL_NEW char(2);
  declare L_TRNFL_CUR char(2);

  declare L_SEQNO decimal(10);
  declare L_CRSER1 char(1);
  declare L_CRSER2 char(1);
  declare L_DBSER1  char(1);
  declare L_DBSER2  char(1);
  declare L_DBSER3  char(1);
  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_PLD_MM cursor for  select  PL_SEQNO, PL_DOCTP, PL_DOCVL from  SPLDATA.MM_PLTRN  where   PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_SEQNO >= LP_SEQNO order by PL_SEQNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

set L_CRSER1 = '0';
set L_CRSER2 = '1';
set L_DBSER1 = '2';
set L_DBSER2 = '3';
set L_DBSER3 = '4';

set L_TRNVL_OLD = 0;
set L_TRNFL_OLD = 'DB';


if LP_SEQNO=1 then
  select  isnull(PT_YOPVL,0) PT_YOPVL, isnull(PT_YOPFL,'DB') PT_YOPFL into  L_YOPVL, L_YOPFL from SPLDATA.co_ptmst where pt_prttp= LP_PRTTP and PT_PRTCD = LP_PRTCD;
     set L_TRNVL_OLD = L_YOPVL;
     set L_TRNFL_OLD = L_YOPFL;
end if;


if LP_SEQNO>1 then
         select isnull(PL_BALVL,0) PL_BALVL, isnull(PL_BALFL,'CR') PL_BALFL  into L_TRNVL_OLD, L_TRNFL_OLD  from SPLDATA.MM_PLTRN  where PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_SEQNO = (LP_SEQNO-1);
    else
        select  isnull(PT_YOPVL,0) PT_YOPVL, isnull(PT_YOPFL,'DB') PT_YOPFL  into   L_TRNVL_OLD, L_TRNFL_OLD from SPLDATA.co_ptmst where pt_prttp= LP_PRTTP and PT_PRTCD = LP_PRTCD;
end if;


  open C_PLD_MM;
  fetch C_PLD_MM into  L_SEQNO, L_DOCTP, L_DOCVL;

   while END_TABLE = 0 do
          set L_TRNFL_NEW = 'CR';
          if SUBSTRING(L_DOCTP,1,1) in (L_DBSER1,L_DBSER2,L_DBSER3) then
              set L_TRNFL_NEW = 'DB';
          end if;
         set L_TRNVL_NEW = L_DOCVL;

        set L_TRNVL_CUR = 0;
        set L_TRNFL_CUR = 'XX ';

         if L_TRNFL_OLD = 'CR' and L_TRNFL_NEW = 'CR' then
            set L_TRNVL_CUR = L_TRNVL_OLD + L_TRNVL_NEW;
            set L_TRNFL_CUR = 'CR'; 
         end if;

         if L_TRNFL_OLD = 'DB' and L_TRNFL_NEW = 'DB' then
            set L_TRNVL_CUR = L_TRNVL_OLD + L_TRNVL_NEW;
            set L_TRNFL_CUR = 'DB'; 
         end if;

         if L_TRNFL_OLD = 'CR' and L_TRNFL_NEW = 'DB' then
            set L_TRNVL_CUR = L_TRNVL_OLD - L_TRNVL_NEW;
            set L_TRNFL_CUR = 'CR'; 
            if L_TRNVL_CUR < 0 then
               set L_TRNVL_CUR = L_TRNVL_CUR * (-1);
               set L_TRNFL_CUR = 'DB';
            end if;
         end if;

         if L_TRNFL_OLD = 'DB' and L_TRNFL_NEW = 'CR' then
            set L_TRNVL_CUR = L_TRNVL_OLD - L_TRNVL_NEW;
            set L_TRNFL_CUR = 'DB'; 
            if L_TRNVL_CUR < 0 then
               set L_TRNVL_CUR = L_TRNVL_CUR * (-1);
               set L_TRNFL_CUR = 'CR';
            end if;
         end if;
         update SPLDATA.mm_pltrn set PL_BALVL = L_TRNVL_CUR,  PL_BALFL = L_TRNFL_CUR where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_SEQNO = L_SEQNO;
         set L_TRNVL_OLD = L_TRNVL_CUR;
         set L_TRNFL_OLD = L_TRNFL_CUR;
         set END_TABLE =0;
         fetch C_PLD_MM into  L_SEQNO, L_DOCTP, L_DOCVL;
   end while;
   close C_PLD_MM;
   --commit;

      set L_YTDVL = L_TRNVL_CUR;
      set L_YTDFL = L_TRNFL_CUR;

   update SPLDATA.co_ptmst set PT_YTDVL = L_YTDVL,  PT_YTDFL = L_YTDFL where PT_PRTTP = LP_PRTTP and PT_PRTCD = LP_PRTCD;
   --commit;
end P1;
commit;

update SPLDATA.co_ptmst set pt_ystdt='07/01/2006', pt_yopcr = 0 where pt_prttp='C' and pt_prtcd='V0155';
commit;

call SPLDATA.rwkPLTRN_TRN_MM('01',  'C',  'K0131',  '21',  '70004187',  4);
commit;
select * from spltest.mm_pltrn where pl_prtcd='C0138';
SELECT * FROM SPLTEST.CO_CDTRN WHERE CMT_CGSTP='MMXXPTT';