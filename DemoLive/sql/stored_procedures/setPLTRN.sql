
-- procedure to create / update data in Party Ledger transaction using Payment Receipt record
-- This procedure is to be executed whenever the payment receipt is authorised.
-- Procedure is called through mr_tepmr.java

-- FEATURES INTRODUCED
--  When Payment Receipt is authorised, A single transaction is introduced in MR_PLTRN (Party Ledger) with Total Payment Receipt Value.
-- New record generated will have
--        Party Group Code from Payment Receipt transaction.
--        Next Sequence Number (Transaction Processing sequence number for the Party Group)
--        Current closing balance, along with CR/DB flag  (Arrived at by adding/deducting Payment Receipt Amount from Closing balance of previous sequence number)




drop procedure spldata.setPLTRN;
commit;

create procedure spldata.setPLTRN(IN LP_CMPCD char(2), IN LP_STRDT char(10), IN LP_ENDDT char(10))  language sql  modifies sql data 
P1:
begin
  declare L_MKTTP char(2);
  declare L_INVNO char(8);
  declare L_RECCT int default 0;
  declare END_TABLE int default 0;

  declare C_INV1 cursor for  select  IVT_MKTTP, IVT_INVNO from spldata.MR_IVTRN where CONVERT(varchar,IVT_INVDT,101)  between LP_STRDT and LP_ENDDT and IVT_MKTTP in ('01','04','05')  and isnull(IVT_INVQT,0)>0 and IVT_STSFL<>'X'  order by IVT_MKTTP, IVT_INVNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

  open C_INV1 ;
  fetch C_INV1  into  L_MKTTP, L_INVNO;
  while END_TABLE = 0 DO 

     --call spldata.updPTTRN_INV(LP_CMPCD,L_MKTTP,L_INVNO);
     --call spldata.updPLTRN_INV(LP_CMPCD,L_MKTTP,L_INVNO);
     call spldata.updPLTRN_PTT(LP_CMPCD,L_MKTTP,L_INVNO);
     call spldata.updPATRN_PTT(LP_CMPCD,L_MKTTP,L_INVNO);
     fetch C_INV1 into  L_MKTTP, L_INVNO;
  end while;
  close C_INV1;
  commit;
end P1;


commit;


commit;
call spldata.setPLTRN('01','07/01/2006','08/01/2006');
commit;

select * from spldata.mr_pltrn order by pl_prtcd,pl_seqno;

