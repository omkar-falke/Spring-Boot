drop procedure spldata.chgPRDCD;
commit;


create procedure spldata.chgPRDCD(IN LP_OLDCD char(10), IN LP_NEWCD char(10))  language sql  modifies sql data 
 P1:
 begin

  update        spldata.PR_LTMST         set          LT_TPRCD       = LP_NEWCD where             LT_TPRCD          = LP_OLDCD;
  update        spldata.PR_LTMST         set          LT_CPRCD       = LP_NEWCD where             LT_CPRCD          = LP_OLDCD;
  update        spldata.PR_LTMST         set          LT_PRDCD       = LP_NEWCD where             LT_PRDCD          = LP_OLDCD;
  update        spldata.PR_LTMST         set          LT_PPRCD       = LP_NEWCD where             LT_PPRCD          = LP_OLDCD;
  update        spldata.CO_QPMST         set          QP_PRDCD       = LP_NEWCD where             QP_PRDCD          = LP_OLDCD;
  update        spldata.CO_QVMST         set          QV_PRDCD       = LP_NEWCD where             QV_PRDCD          = LP_OLDCD;
  update        spldata.CO_TXDAM         set          TX_PRDCD       = LP_NEWCD where             TX_PRDCD          = LP_OLDCD;
  update        spldata.CO_TXDOC         set          TX_PRDCD       = LP_NEWCD where             TX_PRDCD          = LP_OLDCD;
  update        spldata.CO_TXSAM         set          TXT_PRDCD       = LP_NEWCD where             TXT_PRDCD          = LP_OLDCD;
  update        spldata.CO_TXSPC         set          TXT_PRDCD       = LP_NEWCD where             TXT_PRDCD          = LP_OLDCD;
  update        spldata.EX_DATFL         set          DT_FPRCD       = LP_NEWCD where             DT_FPRCD          = LP_OLDCD;
  update        spldata.EX_DATFL         set          DT_PRDCD       = LP_NEWCD where             DT_PRDCD          = LP_OLDCD;
  update        spldata.FG_ISTRN         set          IST_PRDCD       = LP_NEWCD where             IST_PRDCD          = LP_OLDCD;
  update        spldata.FG_LKTRN        set          LK_PRDCD       = LP_NEWCD where             LK_PRDCD          = LP_OLDCD;
  update        spldata.FG_LKTRN        set          LK_TPRCD       = LP_NEWCD where             LK_TPRCD          = LP_OLDCD;
  update        spldata.FG_LKTRN        set          LK_CPRCD       = LP_NEWCD where             LK_CPRCD         = LP_OLDCD;
  update        spldata.FG_OPSTK         set          OP_PRDCD       = LP_NEWCD where             OP_PRDCD          = LP_OLDCD;
  update        spldata.FG_PTFRF         set          PTF_PRDCD       = LP_NEWCD where             PTF_PRDCD          = LP_OLDCD;
  update        spldata.FG_PTFRF        set          PTF_OPRCD       = LP_NEWCD where             PTF_OPRCD          = LP_OLDCD;
  update        spldata.FG_RCTRN        set          RCT_PRDCD       = LP_NEWCD where             RCT_PRDCD          = LP_OLDCD;
  update        spldata.FG_RSTRN        set          RS_PRDCD       = LP_NEWCD where             RS_PRDCD          = LP_OLDCD;
  update        spldata.FG_SMWRK         set          SM_PRDCD       = LP_NEWCD where             SM_PRDCD          = LP_OLDCD;
  update        spldata.FG_STMST         set          ST_PRDCD       = LP_NEWCD where             ST_PRDCD          = LP_OLDCD;
  update        spldata.FG_STMST         set          ST_TPRCD       = LP_NEWCD where             ST_TPRCD          = LP_OLDCD;
  update        spldata.FG_STMST         set          ST_CPRCD       = LP_NEWCD where             ST_CPRCD         = LP_OLDCD;
  update        spldata.MM_EXPRD         set          EX_PRDCD       = LP_NEWCD where             EX_PRDCD          = LP_OLDCD;
  update        spldata.MR_PTTRN         set          PT_PRDCD       = LP_NEWCD where             PT_PRDCD          = LP_OLDCD;
  update        spldata.MR_DODAM         set          DOD_PRDCD       = LP_NEWCD where             DOD_PRDCD          = LP_OLDCD;
  update        spldata.MR_DODEL         set          DOD_PRDCD       = LP_NEWCD where             DOD_PRDCD          = LP_OLDCD;
  update        spldata.MR_DOTAM         set          DOT_PRDCD       = LP_NEWCD where             DOT_PRDCD          = LP_OLDCD;
  update        spldata.MR_DOTRN         set          DOT_PRDCD       = LP_NEWCD where             DOT_PRDCD          = LP_OLDCD;
  update        spldata.MR_INTAM         set          INT_PRDCD       = LP_NEWCD where             INT_PRDCD          = LP_OLDCD;
  update        spldata.MR_INTRN         set          INT_PRDCD       = LP_NEWCD where             INT_PRDCD          = LP_OLDCD;
  update        spldata.MR_IVTRN         set          IVT_PRDCD       = LP_NEWCD where             IVT_PRDCD          = LP_OLDCD;
  update        spldata.MR_PITRN         set          PIT_PRDCD       = LP_NEWCD where             PIT_PRDCD          = LP_OLDCD;
  update        spldata.MR_TMTRN         set          TM_PRDCD       = LP_NEWCD where             TM_PRDCD          = LP_OLDCD;
  update        spldata.QC_QTTRN         set          QTT_PRDCD       = LP_NEWCD where             QTT_PRDCD          = LP_OLDCD;
  update        spldata.QC_SMTRN         set          SMT_PRDCD       = LP_NEWCD where             SMT_PRDCD          = LP_OLDCD;
  commit;
end P1;


commit;
call spldata.chgPRDCD('5290900490','5261900490');
commit;
