

-- procedure to rework Party ledger closing balance (transactionwise) from opening date and opening balance figure
-- This procedure is to be executed whenever externally for reworking of disturbed closing balance

-- FEATURES INTRODUCED
--  Year opening payment outstanding (credit/debit) are recorded in Party Master along with opening date
-- In this procedure, every transaction in the party ledger, starting from opening date are processed (Considering the Starting Balance Figure)
-- While processing every transaction, the current closing balance against the transaction is overwritten
-- when all thransactions are processed, the final closing balance along with credit / debit flag is recorded against party record (in party master).

;

drop procedure spldata.rwkPLTRN_PRT;
commit;

create procedure spldata.rwkPLTRN_PRT(IN LP_CMPCD char(2),IN LP_PRTTP char(1), IN LP_PRTCD char(5),IN LP_YSTDT char(10), IN LP_YOPVL decimal(12),IN LP_YOPFL char(2))  language sql  modifies sql data 
P1:
begin
  declare L_STRDT date;

  declare L_YOPVL decimal(16);
  declare L_YTDVL decimal(16);
  declare L_YOPFL char(2);
  declare L_YTDFL char(2);

  declare L_DOCTP char(2);
  declare L_DOCSQ char(2);
  declare L_DOCNO char(8);
  declare L_DOCDT date;
  declare L_DOCVL decimal(12);

  declare L_MKTTP char(2);
  declare L_DUEDT date;
  declare L_SEQNO decimal(10);
  declare L_BALVL decimal(12);
  declare L_BALFL char(2);
  declare L_ADJVL decimal(12);
  declare L_ACCRF char(10);
  declare L_GRPCD char(5);
  declare L_SBSCD char(6);
  declare L_ACCDT date;
  declare L_TRNFL char(1);
  declare L_STSFL char(1);
  declare L_LUSBY char(5);
  declare L_LUPDT date;

  declare L_TRNVL_OLD decimal(12);
  declare L_TRNVL_NEW decimal(12);
  declare L_TRNVL_CUR decimal(12);

  declare L_STRFL char(2);
  declare L_TRNFL_OLD char(2);
  declare L_TRNFL_NEW char(2);
  declare L_TRNFL_CUR char(2);
  declare L_INVNO_DUMMY char(8) default '77777777';

  declare L_SEQNO_RUN decimal(10);
  declare L_CRSER1 char(1);
  declare L_CRSER2 char(1);
  declare L_DBSER1  char(1);
  declare L_DBSER2  char(1);
  declare L_RECCT int default 0;

  declare END_TABLE int default 0;

  declare C_SRL cursor for  select  PL_DOCDT, CMT_CCSVL PL_DOCSQ, PL_DOCNO, PL_DOCTP  from  spldata.mr_pltrn, spldata.co_cdtrn  where   cmt_cgmtp='SYS' and  cmt_cgstp='MRXXPTT' and cmt_codcd = SUBSTRING(PL_DOCNO,2,2)  and pl_cmpcd = LP_CMPCD  and pl_prttp = LP_PRTTP and pl_prtcd = LP_PRTCD and pl_docdt >= L_STRDT  and pl_doctp <>  '21'  union all 
select  PL_DOCDT, CMT_CCSVL PL_DOCSQ, PL_DOCNO, PL_DOCTP  from  spldata.mr_pltrn, spldata.co_cdtrn   where   cmt_cgmtp='SYS' and  cmt_cgstp='MRXXPTT' and cmt_codcd = PL_DOCTP  and pl_cmpcd = LP_CMPCD and pl_prttp = LP_PRTTP and pl_prtcd = LP_PRTCD and pl_docdt >= L_STRDT and   pl_doctp  = '21'  order by PL_DOCDT, PL_DOCSQ,PL_DOCNO;

  declare C_PLD cursor for  select  PL_SEQNO, PL_DOCTP, round(PL_DOCVL,0) PL_DOCVL from  spldata.mr_pltrn  where   pl_cmpcd = LP_CMPCD and pl_prttp = LP_PRTTP and pl_prtcd = LP_PRTCD and pl_docdt >= L_STRDT order by pl_seqno;

  declare continue handler for not found 
       set END_TABLE = 1; 

set L_CRSER1 = '0';
set L_CRSER2 = '1';
set L_DBSER1 = '2';
set L_DBSER2 = '3';

--if LP_YOPVL>0 then
   update spldata.co_ptmst set PT_YSTDT = LP_YSTDT, PT_YOPVL = LP_YOPVL, PT_YOPFL = LP_YOPFL where PT_PRTTP = LP_PRTTP and PT_PRTCD = LP_PRTCD;
--end if;

  select PT_YSTDT, round(isnull(PT_YOPVL,0),0)  PT_YOPVL, isnull(PT_YOPFL,'DB') PT_YOPFL into  L_STRDT, L_YOPVL, L_YOPFL from spldata.co_ptmst where pt_prttp= LP_PRTTP and PT_PRTCD = LP_PRTCD;

  set END_TABLE = 0;
  set L_SEQNO_RUN = 0;
  open C_SRL ;
  fetch C_SRL into  L_DOCDT, L_DOCSQ, L_DOCNO, L_DOCTP;
   while END_TABLE = 0 do
      set L_SEQNO_RUN = L_SEQNO_RUN +1;
      update spldata.mr_pltrn set PL_SEQNO = L_SEQNO_RUN  where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCDT = L_DOCDT and PL_DOCTP = L_DOCTP and PL_DOCNO = L_DOCNO;
      insert into spldata.temp_chk values (L_DOCNO||'    '||char(L_SEQNO));
      fetch C_SRL into  L_DOCDT, L_DOCSQ, L_DOCNO, L_DOCTP;
   end while;
   close C_SRL;


  set L_TRNFL_OLD = 'CR';
  set L_TRNVL_OLD = 0;
  set L_TRNVL_OLD = L_YOPVL;
  set L_TRNFL_OLD = L_YOPFL;

  set END_TABLE = 0;
  open C_PLD ;
  fetch C_PLD into  L_SEQNO, L_DOCTP, L_DOCVL;

   while END_TABLE = 0 do
          set L_TRNFL_NEW = 'CR';
          if SUBSTRING(L_DOCTP,1,1)  in (L_DBSER1,L_DBSER2) then
              set L_TRNFL_NEW = 'DB';
          end if;
          set L_TRNVL_NEW = L_DOCVL;

          set L_TRNVL_CUR = 0;
          set L_TRNFL_CUR = 'XX ';

          if L_TRNFL_OLD = 'CR' and L_TRNFL_NEW = 'CR' then
            set L_TRNVL_CUR = round(L_TRNVL_OLD + L_TRNVL_NEW,0);
            set L_TRNFL_CUR = 'CR'; 
          end if;

          if L_TRNFL_OLD = 'DB' and L_TRNFL_NEW = 'DB' then
             set L_TRNVL_CUR = round(L_TRNVL_OLD + L_TRNVL_NEW,0);
             set L_TRNFL_CUR = 'DB'; 
          end if;

         if L_TRNFL_OLD = 'CR' and L_TRNFL_NEW = 'DB' then
            set L_TRNVL_CUR = round(L_TRNVL_OLD - L_TRNVL_NEW,0);
            set L_TRNFL_CUR = 'CR'; 
            if L_TRNVL_CUR < 0 then
               set L_TRNVL_CUR = round(L_TRNVL_CUR * (-1),0);
               set L_TRNFL_CUR = 'DB';
            end if;
         end if;

         if L_TRNFL_OLD = 'DB' and L_TRNFL_NEW = 'CR' then
            set L_TRNVL_CUR = round(L_TRNVL_OLD - L_TRNVL_NEW,0);
            set L_TRNFL_CUR = 'DB'; 
            if L_TRNVL_CUR < 0 then
               set L_TRNVL_CUR = round(L_TRNVL_CUR * (-1),0);
               set L_TRNFL_CUR = 'CR';
            end if;
         end if;
         update spldata.mr_pltrn set PL_BALVL = L_TRNVL_CUR,  PL_BALFL = L_TRNFL_CUR where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_SEQNO = L_SEQNO;
         set L_TRNVL_OLD = L_TRNVL_CUR;
         set L_TRNFL_OLD = L_TRNFL_CUR;
         set END_TABLE =0;
         fetch C_PLD into  L_SEQNO, L_DOCTP, L_DOCVL;
   end while;
   close C_PLD;

   set L_YTDVL = L_TRNVL_CUR;
   set L_YTDFL = L_TRNFL_CUR;

   update spldata.co_ptmst set PT_YTDVL = L_YTDVL,  PT_YTDFL = L_YTDFL where PT_PRTTP = LP_PRTTP and PT_PRTCD = LP_PRTCD;

  if LP_YOPVL >= 0  and LP_YOPFL = 'DB' then

        set L_MKTTP = '01';
        set L_DOCTP = '39';
        set L_DOCNO = '73977777';
        set L_DOCVL = LP_YOPVL;
        set L_DOCDT = date(days(date(LP_YSTDT))-1);
        set L_DUEDT = L_DOCDT;
        set L_SEQNO = 0;
        set L_BALVL = 0.00;
        set L_BALFL = 'DB';
        set L_ADJVL = 0.00;
        set L_ACCRF = '    ';
        set L_ACCDT = null;
        set L_SBSCD = '060100';
        set L_GRPCD = LP_PRTCD;
        set L_TRNFL = '0';
        set L_STSFL = '1';
        set L_LUSBY = 'SYS';
        set L_LUPDT = current_date;

       select count(*) into L_RECCT from spldata.Mr_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCTP = L_DOCTP  and PL_DOCNO = L_DOCNO;

      -- select sum(ifnull(PL_ADJVL,0)) into L_ADJVL from spldata.Mr_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCDT < LP_YSTDT and PL_DOCNO <> L_DOCNO;

       if L_RECCT > 0 then
           update spldata.Mr_PLTRN set PL_DOCVL = LP_YOPVL  where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = L_DOCNO;
       else
       insert into spldata.mr_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DUEDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_BILTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT) values  (LP_CMPCD,LP_PRTTP,LP_PRTCD,L_DOCTP,L_DOCNO,L_SEQNO,L_DOCDT,L_DUEDT,L_DOCVL,L_BALVL,L_BALFL,L_ADJVL,L_ACCRF,L_ACCDT,L_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
       end if;
   end if;
  if LP_YOPVL >= 0  and LP_YOPFL = 'CR' then

        set L_MKTTP = '01';
        set L_DOCTP = '09';
        set L_DOCNO = '70977777';
        set L_DOCVL = LP_YOPVL;
        set L_DOCDT = date(days(date(LP_YSTDT))-1);
        set L_DUEDT = L_DOCDT;
        set L_SEQNO = 0;
        set L_BALVL = 0.00;
        set L_BALFL = 'CR';
        set L_ADJVL = 0.00;
        set L_ACCRF = '    ';
        set L_ACCDT = null;
        set L_SBSCD = '060100';
        set L_GRPCD = LP_PRTCD;
        set L_TRNFL = '0';
        set L_STSFL = '1';
        set L_LUSBY = 'SYS';
        set L_LUPDT = current_date;

       select count(*) into L_RECCT from spldata.Mr_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCTP = L_DOCTP  and PL_DOCNO = L_DOCNO;

      -- select sum(ifnull(PL_ADJVL,0)) into L_ADJVL from spldata.Mr_PLTRN where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCDT < LP_YSTDT and PL_DOCNO <> L_DOCNO;

       if L_RECCT > 0 then
           update spldata.Mr_PLTRN set PL_DOCVL = LP_YOPVL  where PL_CMPCD = LP_CMPCD and PL_PRTTP = LP_PRTTP and PL_PRTCD = LP_PRTCD and PL_DOCTP = L_DOCTP and PL_DOCNO = L_DOCNO;
       else
       insert into spldata.mr_pltrn (PL_CMPCD,PL_PRTTP,PL_PRTCD,PL_DOCTP,PL_DOCNO,PL_SEQNO,PL_DOCDT,PL_DUEDT,PL_DOCVL,PL_BALVL,PL_BALFL,PL_ADJVL,PL_ACCRF,PL_ACCDT,PL_MKTTP,PL_GRPCD,PL_SBSCD,PL_TRNFL,PL_STSFL,PL_LUSBY,PL_LUPDT) values  (LP_CMPCD,LP_PRTTP,LP_PRTCD,L_DOCTP,L_DOCNO,L_SEQNO,L_DOCDT,L_DUEDT,L_DOCVL,L_BALVL,L_BALFL,L_ADJVL,L_ACCRF,L_ACCDT,L_MKTTP,L_GRPCD,L_SBSCD,L_TRNFL,L_STSFL,L_LUSBY,L_LUPDT);
       end if;
   end if;

end P1;

commit;


select * from spldata.co_cdtrn where cmt_cgstp='MRXXPTT' and cmt_cgmtp='SYS';
select  PL_SEQNO,PL_DOCDT, CMT_CCSVL PL_DOCSQ, PL_DOCNO, PL_DOCTP  from  spldata.mr_pltrn, spldata.co_cdtrn  where   cmt_cgmtp='SYS' and cmt_cgstp='MRXXPTT' and cmt_codcd = PL_DOCTP  and pl_cmpcd = '01' and pl_prttp = 'C' and pl_prtcd = 'I0079' and pl_docdt >= '07/01/2006' order by PL_DOCDT, CMT_CCSVL, PL_DOCNO;




call spldata.rwkPLTRN_PRT('01','C','A0016','07/01/2006',74445,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0180','07/01/2006',610	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','A0184','07/01/2006',151216	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','A0250','07/01/2006',31608	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','A0271','07/01/2006',1145752	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','A0294','07/01/2006',235874	,'DB');	

call spldata.rwkPLTRN_PRT('01','C','A0354','07/01/2006',10125	,'CR');	
commit;
call spldata.rwkPLTRN_PRT('01','C','A0356','07/01/2006',362046	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','A0397','07/01/2006',126045	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','A0407','07/01/2006',336	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','B0020','07/01/2006',4500	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','B0021','07/01/2006',4500	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','B0147','07/01/2006',14320	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','B0168','07/01/2006',1319510	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','C0035','07/01/2006',10665	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','C0065','07/01/2006',42477	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','C0093','07/01/2006',543718	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','C0113','07/01/2006',7950	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','C0125','07/01/2006',1947198	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','C0183','07/01/2006',262157	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','C0209','07/01/2006',26250	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','C8904','07/01/2006',708	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','D0079','07/01/2006',1621	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','D0122','07/01/2006',3231	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','D0132','07/01/2006',57557	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','E0148','07/01/2006',306503	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','E0151','07/01/2006',2000	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','F0032','07/01/2006',45633	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','F0041','07/01/2006',52867	,'CR');	
commit;
call spldata.rwkPLTRN_PRT('01','C','F0066','07/01/2006',16018	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','G0047','07/01/2006',3934	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','G0174','07/01/2006',209376	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','G0182','07/01/2006',993885	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','H0005','07/01/2006',51600	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','H0041','07/01/2006',1000	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','I0081','07/01/2006',1573	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','J0131','07/01/2006',2134	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','J0151','07/01/2006',2500	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','K0201','07/01/2006',82030	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','L0091','07/01/2006',5000	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','M0090','07/01/2006',322194	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','M0177','07/01/2006',12740	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','M0327','07/01/2006',2187	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','M0331','07/01/2006',290802	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','N0048','07/01/2006',11250	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','N0149','07/01/2006',5000	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','P0018','07/01/2006',649066	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','P0057','07/01/2006',659633	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','P0135','07/01/2006',48782	,'CR');	
commit;
call spldata.rwkPLTRN_PRT('01','C','P0189','07/01/2006',2500	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','P0289','07/01/2006',41645	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','P0414','07/01/2006',1833	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','R0112','07/01/2006',1750	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','R0142','07/01/2006',2500	,'CR');	
commit;
call spldata.rwkPLTRN_PRT('01','C','R0199','07/01/2006',2326	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','R0218','07/01/2006',214046	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','S0378','07/01/2006',55000	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','S0453','07/01/2006',297	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','S0503','07/01/2006',985	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','S0592','07/01/2006',1875	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','S0593','07/01/2006',1770	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','S0621','07/01/2006',186112	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','S0643','07/01/2006',540633	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','S0706','07/01/2006',84623	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','S0714','07/01/2006',101014	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','S0721','07/01/2006',5000	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','T0049','07/01/2006',2502	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','T0097','07/01/2006',256	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','U0010','07/01/2006',161	,'DB');	
commit;
call spldata.rwkPLTRN_PRT('01','C','V0098','07/01/2006',283754	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','W0040','07/01/2006',5000	,'CR');	
call spldata.rwkPLTRN_PRT('01','C','W0051','07/01/2006',208539	,'DB');	
call spldata.rwkPLTRN_PRT('01','C','Y0012','07/01/2006',649647	,'DB');	
commit;

-------------------------------------------------------------------------------
call spldata.rwkPLTRN_PRT('01','C','A0052','07/01/2006',	1875	,'CR');
call spldata.rwkPLTRN_PRT('01','C','A0023','07/01/2006',	3299017	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0337','07/01/2006',	3748	,'DB');
call spldata.rwkPLTRN_PRT('01','C','B0199','07/01/2006',	504829	,'DB');
call spldata.rwkPLTRN_PRT('01','C','B0216','07/01/2006',	738632	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','B0192','07/01/2006',	3089008	,'DB');
call spldata.rwkPLTRN_PRT('01','C','B0138','07/01/2006',	1404019	,'DB');
call spldata.rwkPLTRN_PRT('01','C','B0028','07/01/2006',	1850	,'DB');
call spldata.rwkPLTRN_PRT('01','C','D0098','07/01/2006',	160268	,'DB');
call spldata.rwkPLTRN_PRT('01','C','E0016','07/01/2006',	4139	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','E0162','07/01/2006',	1325	,'CR');
call spldata.rwkPLTRN_PRT('01','C','F0018','07/01/2006',	649066	,'DB');
call spldata.rwkPLTRN_PRT('01','C','G0077','07/01/2006',	2553453	,'DB');
call spldata.rwkPLTRN_PRT('01','C','G0025','07/01/2006',	432710	,'DB');
call spldata.rwkPLTRN_PRT('01','C','H0053','07/01/2006',	5077	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','H0052','07/01/2006',	1179	,'DB');
call spldata.rwkPLTRN_PRT('01','C','I0058','07/01/2006',	8724	,'DB');
call spldata.rwkPLTRN_PRT('01','C','J0128','07/01/2006',	375	,'CR');
call spldata.rwkPLTRN_PRT('01','C','J0007','07/01/2006',	658640	,'DB');
call spldata.rwkPLTRN_PRT('01','C','J0016','07/01/2006',	757897	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','K0196','07/01/2006',	73034	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0195','07/01/2006',	72118	,'DB');
call spldata.rwkPLTRN_PRT('01','C','L0094','07/01/2006',	3317	,'DB');
call spldata.rwkPLTRN_PRT('01','C','L0040','07/01/2006',	2468219	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0185','07/01/2006',	685706	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','M0255','07/01/2006',	994	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0119','07/01/2006',	4693	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0261','07/01/2006',	3631134	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0214','07/01/2006',	349833	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0211','07/01/2006',	4404	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','M0024','07/01/2006',	3937080	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0347','07/01/2006',	5592730	,'DB');
call spldata.rwkPLTRN_PRT('01','C','N0050','07/01/2006',	436	,'CR');
call spldata.rwkPLTRN_PRT('01','C','N0021','07/01/2006',	499297	,'DB');
call spldata.rwkPLTRN_PRT('01','C','N0013','07/01/2006',	299817	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','N0012','07/01/2006',	19286	,'DB');
call spldata.rwkPLTRN_PRT('01','C','O0014','07/01/2006',	613007	,'DB');
call spldata.rwkPLTRN_PRT('01','C','O0052','07/01/2006',	6597	,'DB');
call spldata.rwkPLTRN_PRT('01','C','O0032','07/01/2006',	689852	,'DB');
call spldata.rwkPLTRN_PRT('01','C','O0032','07/01/2006',	214271	,'DB');
commit;
--call spldata.rwkPLTRN_PRT('01','C','O0032','07/01/2006',	14200+200071	,'DB');
commit;

--call spldata.rwkPLTRN_PRT('01','C','P0473','07/01/2006',	200071	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','P0482','07/01/2006',	1011	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0323','07/01/2006',	389754	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0497','07/01/2006',	3691	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0356','07/01/2006',	60	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0448','07/01/2006',	3228	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','R0015','07/01/2006',	582764	,'DB');
call spldata.rwkPLTRN_PRT('01','C','R0200','07/01/2006',	701097	,'DB');
call spldata.rwkPLTRN_PRT('01','C','R0212','07/01/2006',	45579	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0343','07/01/2006',	9427	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0726','07/01/2006',	916429	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0423','07/01/2006',	1910557	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0614','07/01/2006',	830	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0362','07/01/2006',	191667	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0686','07/01/2006',	140	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0746','07/01/2006',	72118	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0568','07/01/2006',	649065	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0756','07/01/2006',	612426	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0639','07/01/2006',	3182711	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0683','07/01/2006',	4773	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0681','07/01/2006',	132	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0062','07/01/2006',	4184612	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0664','07/01/2006',	917183	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0218','07/01/2006',	259893	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0672','07/01/2006',	5254	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0768','07/01/2006',	50	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0512','07/01/2006',	36069	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0152','07/01/2006',	1105	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0764','07/01/2006',	651316	,'DB');
call spldata.rwkPLTRN_PRT('01','C','T0063','07/01/2006',	2064	,'DB');
call spldata.rwkPLTRN_PRT('01','C','T0100','07/01/2006',	1250	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','V0037','07/01/2006',	13874410	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0111','07/01/2006',	596	,'CR');
call spldata.rwkPLTRN_PRT('01','C','V0089','07/01/2006',	1252759	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0206','07/01/2006',	500	,'CR');
commit;
		
call spldata.rwkPLTRN_PRT('01','C','A0332','07/01/2006',	23048	,'CR');
call spldata.rwkPLTRN_PRT('01','C','G0182','07/01/2006',	1229625	,'DB');
call spldata.rwkPLTRN_PRT('01','C','I0079','07/01/2006',	63079	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0132','07/01/2006',	7339979	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0251','07/01/2006',	23264	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','M0326','07/01/2006',	32009	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0494','07/01/2006',	428	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0688','07/01/2006',	6831	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0590','07/01/2006',	40891	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0679','07/01/2006',	3358	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0285','07/01/2006',	1566355	,'DB');
call spldata.rwkPLTRN_PRT('01','C','T0025','07/01/2006',	1276	,'CR');
call spldata.rwkPLTRN_PRT('01','C','T0155','07/01/2006',	1666	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0182','07/01/2006',	2556609	,'DB');
call spldata.rwkPLTRN_PRT('01','C','W0017','07/01/2006',	1206	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','X0005','07/01/2006',	1353336	,'DB');
call spldata.rwkPLTRN_PRT('01','C','Z0010','07/01/2006',	4642	,'CR');
commit;
		
call spldata.rwkPLTRN_PRT('01','C','A0405','07/01/2006',	1750	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0348','07/01/2006',	23111	,'DB');
call spldata.rwkPLTRN_PRT('01','C','D0111','07/01/2006',	2809085	,'DB');
call spldata.rwkPLTRN_PRT('01','C','E0087','07/01/2006',	62000	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','J0015','07/01/2006',	214685	,'CR');
call spldata.rwkPLTRN_PRT('01','C','R0004','07/01/2006',	808906	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0272','07/01/2006',	11733200	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0042','07/01/2006',	620025	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0516','07/01/2006',	1796997	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0652','07/01/2006',	338582	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0554','07/01/2006',	1008108	,'DB');
commit;
		
call spldata.rwkPLTRN_PRT('01','C','A0213','07/01/2006',	3326512	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0349','07/01/2006',	2327	,'CR');
call spldata.rwkPLTRN_PRT('01','C','P0504','07/01/2006',	35186	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0375','07/01/2006',	394767	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','P0320','07/01/2006',	243237	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0361','07/01/2006',	10002	,'CR');
call spldata.rwkPLTRN_PRT('01','C','P0339','07/01/2006',	3992	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0619','07/01/2006',	20554	,'CR');
call spldata.rwkPLTRN_PRT('01','C','U0036','07/01/2006',	7500	,'CR');
commit;
		
call spldata.rwkPLTRN_PRT('01','C','A0021','07/01/2006',	14204	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0181','07/01/2006',	6402	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0174','07/01/2006',	380540	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0154','07/01/2006',	2050	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','B0014','07/01/2006',	3725	,'CR');
call spldata.rwkPLTRN_PRT('01','C','B0194','07/01/2006',	5335550	,'DB');
call spldata.rwkPLTRN_PRT('01','C','E0176','07/01/2006',	4295624	,'DB');
call spldata.rwkPLTRN_PRT('01','C','E0120','07/01/2006',	54681	,'CR');
call spldata.rwkPLTRN_PRT('01','C','G0165','07/01/2006',	4184170	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','G0133','07/01/2006',	15934132	,'DB');
call spldata.rwkPLTRN_PRT('01','C','J0158','07/01/2006',	5235	,'CR');
call spldata.rwkPLTRN_PRT('01','C','J0155','07/01/2006',	90462	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0205','07/01/2006',	234	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0053','07/01/2006',	187	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','L0005','07/01/2006',	249	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0078','07/01/2006',	345	,'DB');
call spldata.rwkPLTRN_PRT('01','C','O0009','07/01/2006',	14802	,'CR');
call spldata.rwkPLTRN_PRT('01','C','O0016','07/01/2006',	872	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','P0091','07/01/2006',	1454	,'CR');
call spldata.rwkPLTRN_PRT('01','C','P0034','07/01/2006',	1645	,'CR');

call spldata.rwkPLTRN_PRT('01','C','P0409','07/01/2006',	767712	,'DB');
commit;

call spldata.rwkPLTRN_PRT('01','C','P0446','07/01/2006',	5300	,'CR');
call spldata.rwkPLTRN_PRT('01','C','P0118','07/01/2006',	6361	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0442','07/01/2006',	16900	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0280','07/01/2006',	18394	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0681','07/01/2006',	23	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0641','07/01/2006',	702000	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0004','07/01/2006',	13444011	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0129','07/01/2006',	1445650	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0080','07/01/2006',	4053625	,'DB');
call spldata.rwkPLTRN_PRT('01','C','T0137','07/01/2006',	5089	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0162','07/01/2006',	5381	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0134','07/01/2006',	4612	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','W0014','07/01/2006',	14842952	,'DB');
commit;
------------------------------------------------------------------------------------

call spldata.rwkPLTRN_PRT('01','C','A0369','07/01/2006',	12489	,'CR');
call spldata.rwkPLTRN_PRT('01','C','A0273','07/01/2006',	7500	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0059','07/01/2006',	859686	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0413','07/01/2006',	1044	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0049','07/01/2006',	740	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','A0233','07/01/2006',	2500	,'CR');
call spldata.rwkPLTRN_PRT('01','C','B0082','07/01/2006',	14400	,'CR');
call spldata.rwkPLTRN_PRT('01','C','B0175','07/01/2006',	74307	,'CR');
call spldata.rwkPLTRN_PRT('01','C','B0181','07/01/2006',	37499875	,'DB');
call spldata.rwkPLTRN_PRT('01','C','B0027','07/01/2006',	80756	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','B0126','07/01/2006',	20730086	,'DB');
call spldata.rwkPLTRN_PRT('01','C','C0084','07/01/2006',	1014310	,'DB');
call spldata.rwkPLTRN_PRT('01','C','D0104','07/01/2006',	6718543	,'DB');
call spldata.rwkPLTRN_PRT('01','C','D0086','07/01/2006',	10531494	,'DB');
call spldata.rwkPLTRN_PRT('01','C','D0112','07/01/2006',	234648	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','E0172','07/01/2006',	13014029	,'DB');
call spldata.rwkPLTRN_PRT('01','C','E0090','07/01/2006',	68599	,'CR');
call spldata.rwkPLTRN_PRT('01','C','E0079','07/01/2006',	20942	,'CR');
call spldata.rwkPLTRN_PRT('01','C','E0009','07/01/2006',	1233	,'CR');
call spldata.rwkPLTRN_PRT('01','C','E0128','07/01/2006',	232491	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','G0083','07/01/2006',	3967003	,'DB');
call spldata.rwkPLTRN_PRT('01','C','G0134','07/01/2006',	17330435	,'DB');
call spldata.rwkPLTRN_PRT('01','C','H0078','07/01/2006',	5030	,'CR');
call spldata.rwkPLTRN_PRT('01','C','H0116','07/01/2006',	4359	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','I0062','07/01/2006',	12003579	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0062','07/01/2006',	10390	,'CR');
call spldata.rwkPLTRN_PRT('01','C','K0163','07/01/2006',	360488	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0091','07/01/2006',	82793	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','K0061','07/01/2006',	2021780	,'DB');
call spldata.rwkPLTRN_PRT('01','C','L0087','07/01/2006',	1250	,'CR');
call spldata.rwkPLTRN_PRT('01','C','M0345','07/01/2006',	1342	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0206','07/01/2006',	129473	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','M0308','07/01/2006',	4174	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0254','07/01/2006',	3075534	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0329','07/01/2006',	874996	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0362','07/01/2006',	23316	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','N0099','07/01/2006',	32920	,'DB');
call spldata.rwkPLTRN_PRT('01','C','N0125','07/01/2006',	2125	,'CR');
call spldata.rwkPLTRN_PRT('01','C','O0059','07/01/2006',	984480	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0022','07/01/2006',	3076940	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','P0410','07/01/2006',	12491868	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0450','07/01/2006',	93	,'CR');
call spldata.rwkPLTRN_PRT('01','C','R0031','07/01/2006',	3039624	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0690','07/01/2006',	5372807	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0669','07/01/2006',	10012598	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0653','07/01/2006',	2452142	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0025','07/01/2006',	18284530	,'DB');
call spldata.rwkPLTRN_PRT('01','C','U0039','07/01/2006',	353914	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','V0164','07/01/2006',	23970481	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0083','07/01/2006',	28940	,'CR');
call spldata.rwkPLTRN_PRT('01','C','V0130','07/01/2006',	1051776	,'DB');
call spldata.rwkPLTRN_PRT('01','C','W0010','07/01/2006',	7775041	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','A0253','07/01/2006',	119269	,'CR');
call spldata.rwkPLTRN_PRT('01','C','A0153','07/01/2006',	2718397	,'DB');
call spldata.rwkPLTRN_PRT('01','C','A0320','07/01/2006',	2050	,'CR');
call spldata.rwkPLTRN_PRT('01','C','A0211','07/01/2006',	4879860	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','A0381','07/01/2006',	2830	,'CR');
call spldata.rwkPLTRN_PRT('01','C','A0191','07/01/2006',	447846	,'DB');
call spldata.rwkPLTRN_PRT('01','C','B0205','07/01/2006',	291	,'CR');
call spldata.rwkPLTRN_PRT('01','C','B0083','07/01/2006',	699179	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','B0117','07/01/2006',	689411	,'DB');
call spldata.rwkPLTRN_PRT('01','C','B0107','07/01/2006',	121944	,'CR');
call spldata.rwkPLTRN_PRT('01','C','C0129','07/01/2006',	1286461	,'DB');
call spldata.rwkPLTRN_PRT('01','C','C0155','07/01/2006',	450158	,'DB');
call spldata.rwkPLTRN_PRT('01','C','C0126','07/01/2006',	747926	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','C0091','07/01/2006',	1054	,'CR');
call spldata.rwkPLTRN_PRT('01','C','D0068','07/01/2006',	4039	,'CR');
call spldata.rwkPLTRN_PRT('01','C','E0137','07/01/2006',	918	,'CR');
call spldata.rwkPLTRN_PRT('01','C','F0011','07/01/2006',	868	,'CR');
call spldata.rwkPLTRN_PRT('01','C','G0122','07/01/2006',	6278559	,'DB');
call spldata.rwkPLTRN_PRT('01','C','G0160','07/01/2006',	92	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','G0014','07/01/2006',	1222032	,'DB');
call spldata.rwkPLTRN_PRT('01','C','G0006','07/01/2006',	306038	,'DB');
call spldata.rwkPLTRN_PRT('01','C','H0102','07/01/2006',	1161	,'CR');
call spldata.rwkPLTRN_PRT('01','C','K0058','07/01/2006',	694552	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0006','07/01/2006',	357684	,'DB');
call spldata.rwkPLTRN_PRT('01','C','K0140','07/01/2006',	6890	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','L0075','07/01/2006',	13000	,'CR');
call spldata.rwkPLTRN_PRT('01','C','M0299','07/01/2006',	2287350	,'DB');
call spldata.rwkPLTRN_PRT('01','C','M0260','07/01/2006',	5000	,'CR');
call spldata.rwkPLTRN_PRT('01','C','M0303','07/01/2006',	730020	,'DB');
call spldata.rwkPLTRN_PRT('01','C','N0154','07/01/2006',	14189	,'CR');
call spldata.rwkPLTRN_PRT('01','C','N0063','07/01/2006',	3750	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','N0145','07/01/2006',	114	,'CR');
call spldata.rwkPLTRN_PRT('01','C','O0003','07/01/2006',	61	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0129','07/01/2006',	119240	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0508','07/01/2006',	2646	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0319','07/01/2006',	3309500	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','P0043','07/01/2006',	151216	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0292','07/01/2006',	305	,'DB');
call spldata.rwkPLTRN_PRT('01','C','P0421','07/01/2006',	1416195	,'DB');
call spldata.rwkPLTRN_PRT('01','C','R0198','07/01/2006',	1683	,'CR');
call spldata.rwkPLTRN_PRT('01','C','R0054','07/01/2006',	1680824	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0440','07/01/2006',	1088	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0689','07/01/2006',	1000	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0197','07/01/2006',	1118271	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0432','07/01/2006',	3247	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0346','07/01/2006',	130052	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0809','07/01/2006',	236038	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0425','07/01/2006',	223242	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0708','07/01/2006',	31907	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0178','07/01/2006',	204	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0307','07/01/2006',	22482	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0704','07/01/2006',	167	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0600','07/01/2006',	11000	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0245','07/01/2006',	17228165	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0269','07/01/2006',	148890	,'DB');
call spldata.rwkPLTRN_PRT('01','C','T0010','07/01/2006',	1764014	,'DB');
call spldata.rwkPLTRN_PRT('01','C','T0126','07/01/2006',	20400	,'CR');
call spldata.rwkPLTRN_PRT('01','C','T0065','07/01/2006',	613875	,'CR');
call spldata.rwkPLTRN_PRT('01','C','T0072','07/01/2006',	379345	,'CR');
call spldata.rwkPLTRN_PRT('01','C','T0092','07/01/2006',	228803	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','T0021','07/01/2006',	1171897	,'DB');
call spldata.rwkPLTRN_PRT('01','C','U0041','07/01/2006',	675238	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0161','07/01/2006',	1288825	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0142','07/01/2006',	680472	,'DB');
call spldata.rwkPLTRN_PRT('01','C','Y0009','07/01/2006',	32314	,'DB');
call spldata.rwkPLTRN_PRT('01','C','D0040','07/01/2006',	157	,'CR');
call spldata.rwkPLTRN_PRT('01','C','E0127','07/01/2006',	5462	,'DB');
commit;
call spldata.rwkPLTRN_PRT('01','C','E0118','07/01/2006',	4000	,'CR');
call spldata.rwkPLTRN_PRT('01','C','F0015','07/01/2006',	1211	,'CR');
call spldata.rwkPLTRN_PRT('01','C','J0127','07/01/2006',	18614	,'CR');
call spldata.rwkPLTRN_PRT('01','C','L0037','07/01/2006',	1413971	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0406','07/01/2006',	3153274	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0418','07/01/2006',	6011	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0121','07/01/2006',	140	,'CR');
commit;
call spldata.rwkPLTRN_PRT('01','C','S0508','07/01/2006',	16000	,'CR');
call spldata.rwkPLTRN_PRT('01','C','S0770','07/01/2006',	711000	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0222','07/01/2006',	1759	,'DB');
call spldata.rwkPLTRN_PRT('01','C','S0638','07/01/2006',	7161574	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0109','07/01/2006',	1254	,'DB');
call spldata.rwkPLTRN_PRT('01','C','V0211','07/01/2006',	538971	,'DB');
call spldata.rwkPLTRN_PRT('01','C','X0007','07/01/2006',	1934	,'DB');
commit;


CALL spldata.rwkPLTRN_PRT('01','C','C0210','07/01/2006',	22398	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','M0288','07/01/2006',	237	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','P0271','07/01/2006',	474	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','P0118','07/01/2006',	4600	,'CR');
commit;
CALL spldata.rwkPLTRN_PRT('01','C','A0076','07/01/2006',	59000	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','B0167','07/01/2006',	2250	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','B0178','07/01/2006',	1047	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','D0102','07/01/2006',	17374	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','J0076','07/01/2006',	64143	,'DB');
commit;
CALL spldata.rwkPLTRN_PRT('01','C','J0118','07/01/2006',	9098	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','N0032','07/01/2006',	86612	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','T0052','07/01/2006',	1785	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','V0155','07/01/2006',	2250	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','G0186','07/01/2006',	24000	,'CR');
commit;
CALL spldata.rwkPLTRN_PRT('01','C','M0026','07/01/2006',	2422365	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','S0109','07/01/2006',	1187496	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','V0012','07/01/2006',	64695	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','V0099','07/01/2006',	548800	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','V0197','07/01/2006',	588951	,'DB');
commit;
CALL spldata.rwkPLTRN_PRT('01','C','F0023','07/01/2006',	42	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','G0099','07/01/2006',	214244	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','S0649','07/01/2006',	51815	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','S0431','07/01/2006',	16000	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','S0615','07/01/2006',	3468	,'DB');
commit;
CALL spldata.rwkPLTRN_PRT('01','C','T0068','07/01/2006',	130	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','P0082','07/01/2006',	6640	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','B0219','07/01/2006',	651081	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','H0110','07/01/2006',	593	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','J0089','07/01/2006',	2326	,'DB');
commit;
CALL spldata.rwkPLTRN_PRT('01','C','O0009','07/01/2006',	410753	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','P0341','07/01/2006',	379	,'DB');
CALL spldata.rwkPLTRN_PRT('01','C','S0280','07/01/2006',	4361	,'CR');
CALL spldata.rwkPLTRN_PRT('01','C','S0818','07/01/2006',	186418	,'CR');
commit;

select * from spldata.co_pprtr where ppr_prgcd='FG_RPRCL';
update spldata.co_pprtr set ppr_usrtp='DV1'  where ppr_prgcd='FG_RPRCL';
commit;


call spldata.rwkPLTRN_PRT('01','S','C0138','01/01/2009',0,'DB');
commit;
