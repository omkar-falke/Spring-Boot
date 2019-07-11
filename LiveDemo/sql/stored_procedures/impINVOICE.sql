
-- procedure to import invoice from Invoice Transaction
-- This procedure is to be executed whenever the invoice record is not found during Payment Entry
-- Procedure is called through mr_tepmt.java

-- FEATURES INTRODUCED




drop procedure spldata.impINVOICE;
commit;

create procedure spldata.impINVOICE(IN LP_CMPCD char(2), IN LP_INVNO char(8))  language sql  modifies sql data 
P1:
begin
  declare L_MKTTP char(2);
  declare L_INVNO char(8);
  declare L_PRTCD char(5);
  declare L_RECCT int default 0;
  declare END_TABLE int default 0;

  declare C_IMP cursor for  select distinct  IVT_MKTTP, IVT_INVNO,IVT_BYRCD from spldata.MR_IVTRN where IVT_CMPCD = LP_CMPCD and IVT_INVNO = LP_INVNO;

  declare continue handler for not found 
       set END_TABLE = 1; 
  select count(*) into L_RECCT from spldata.mr_pltrn where pl_cmpcd=LP_CMPCD and pl_doctp='21' and pl_docno=LP_INVNO;
  if L_RECCT = 0 then
      open C_IMP ;
      set END_TABLE = 0;
      fetch C_IMP  into  L_MKTTP, L_INVNO, L_PRTCD;
      while END_TABLE = 0 DO 
         call spldata.updPTTRN_INV(LP_CMPCD,L_MKTTP,L_INVNO);
         call spldata.updPLTRN_INV(LP_CMPCD,L_MKTTP,L_INVNO);
         call spldata.updPLTRN_PTT(LP_CMPCD,L_MKTTP,L_INVNO);
         call spldata.updPATRN_PTT(LP_CMPCD,L_MKTTP,L_INVNO);
         update spldata.co_ptmst set pt_ystdt='07/01/2006', pt_yopvl=0,pt_yopfl='DB' where pt_prttp='C' and pt_prtcd = L_PRTCD and isnull(PT_YOPVL,0)=0;
         call spldata.rwkPLTRN_PRT('01','C',L_PRTCD,'07/01/2006',0,'DB');
         set END_TABLE = 0;
         fetch C_IMP into  L_MKTTP, L_INVNO, L_PRTCD;
      end while;
      close C_IMP;
  end if;
end P1;


commit;


call spldata.impINVOICE('01','70003277');
commit;
