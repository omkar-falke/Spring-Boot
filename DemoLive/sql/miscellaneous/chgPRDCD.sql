

drop procedure spldata.chgPRDCD;
commit;


create procedure spldata.chgPRDCD(IN LP_OLDCD char(10), IN LP_NEWCD char(10))  language sql  modifies sql data 
 P1:
begin
  update spldata.CO_PRMST  set  pr_prdcd = LP_NEWCD where  PR_PRDCD = LP_OLDCD;
  update spldata.CO_QPMST  set  QP_prdcd = LP_NEWCD where  QP_PRDCD = LP_OLDCD;
  update spldata.CO_QVMST set  QV_prdcd = LP_NEWCD where  QV_PRDCD = LP_OLDCD;
  update spldata.CO_TXDOC set  TX_prdcd = LP_NEWCD where  TX_PRDCD = LP_OLDCD;
  update spldata.CO_TXSPC set  TXT_prdcd = LP_NEWCD where  TXT_PRDCD = LP_OLDCD;
  update spldata.EX_DATFL set  DT_prdcd = LP_NEWCD where  DT_PRDCD = LP_OLDCD;
  update spldata.EX_DATFL set  DT_fprcd = LP_NEWCD where  DT_fprCD = LP_OLDCD;
  update spldata.FG_ISTRN set  IST_prdcd = LP_NEWCD where  IST_PRDCD = LP_OLDCD;
  update spldata.FG_OPSTK set  OP_prdcd = LP_NEWCD where  OP_PRDCD = LP_OLDCD;
  update spldata.FG_PTFRF set  PTF_prdcd = LP_NEWCD where  PTF_PRDCD = LP_OLDCD;
  update spldata.FG_PTFRF set  PTF_oprcd = LP_NEWCD where  PTF_OPRCD = LP_OLDCD;
  update spldata.FG_RCTRN set  RCT_prdcd = LP_NEWCD where  RCT_PRDCD = LP_OLDCD;
  update spldata.FG_RSTRN  set  RS_prdcd = LP_NEWCD where  RS_PRDCD = LP_OLDCD;
  update spldata.FG_STMST set  ST_prdcd = LP_NEWCD where  ST_PRDCD = LP_OLDCD;
  update spldata.FG_STMST set  ST_cprcd = LP_NEWCD where  ST_cprCD = LP_OLDCD;
  update spldata.FG_STMST set  ST_tprcd = LP_NEWCD where  ST_tprCD = LP_OLDCD;
  update spldata.MR_CRCLM set  CC_prdcd = LP_NEWCD where  CC_PRDCD = LP_OLDCD;
  update spldata.MR_DODAM set  DOD_prdcd = LP_NEWCD where  DOD_PRDCD = LP_OLDCD;
  update spldata.MR_DODEL  set  DOD_prdcd = LP_NEWCD where  DOD_PRDCD = LP_OLDCD;
  update spldata.MR_DOTAM  set  DOT_prdcd = LP_NEWCD where  DOT_PRDCD = LP_OLDCD;
  update spldata.MR_DOTRN  set  DOT_prdcd = LP_NEWCD where  DOT_PRDCD = LP_OLDCD;
  update spldata.MR_GRMST  set  GR_prdcd = LP_NEWCD where  GR_PRDCD = LP_OLDCD;
  update spldata.MR_INTAM  set  INT_prdcd = LP_NEWCD where  INT_PRDCD = LP_OLDCD;
  update spldata.MR_INTRN  set  INT_prdcd = LP_NEWCD where  INT_PRDCD = LP_OLDCD;
  update spldata.MR_IVTRN  set  IVT_prdcd = LP_NEWCD where  IVT_PRDCD = LP_OLDCD;
  update spldata.MR_PITRN  set  PIT_prdcd = LP_NEWCD where  PIT_PRDCD = LP_OLDCD;
  update spldata.MR_TMTRN  set  TM_prdcd = LP_NEWCD where  TM_PRDCD = LP_OLDCD;
  update spldata.PR_LTMST  set  LT_prdcd = LP_NEWCD where  LT_PRDCD = LP_OLDCD;
  update spldata.PR_LTMST  set  LT_cprcd = LP_NEWCD where  LT_cprCD = LP_OLDCD;
  update spldata.PR_LTMST  set  LT_tprcd = LP_NEWCD where  LT_tprCD = LP_OLDCD;
  update spldata.QC_QTTRN set  QTT_prdcd = LP_NEWCD where  QTT_PRDCD = LP_OLDCD;
  update spldata.QC_SMTRN set  SMT_prdcd = LP_NEWCD where  SMT_PRDCD = LP_OLDCD;
commit;
end P1;

commit;

call spldata.chgPRDCD1('5111201360','5111200360');
commit;
call spldata.chgPRDCD2('5111201360','5111200360');
commit;

call spldata.chgPRDCD1('5112200780','5112201780');
commit;
call spldata.chgPRDCD2('5112200780','5112201780');
commit;


select * from spldata.co_prmst where pr_prdcd in ('5112200780','5112201780');
delete from spldata.co_prmst where pr_prdcd='5112201780';
commit;


delete from spldata.co_prmst where pr_prdcd='5112200780';
commit;

delete from spldata.co_prmst where pr_prdcd='5111201360';
commit;


select pr_prdcd,pr_stsfl from spl0304.co_prmst where pr_prdcd in ('5112200780','5111201360');

update spldata.co_prmst set pr_stsfl='2' where pr_prdcd='5111200360';
commit;

update spldata.co_prmst set pr_stsfl='3' where pr_prdcd='5112201780';
commit;



select * from spldata.co_prmst where pr_prdcd in ('5111201360','5111200360');
delete from spldata.co_prmst where pr_prdcd='5111200360';
commit;


call spldata.chgPRDCD('5112200780','5112201780');
commit;



drop procedure spltest.chgPRDCD;
commit;


create procedure spltest.chgPRDCD(IN LP_OLDCD char(10), IN LP_NEWCD char(10))  language sql  modifies sql data 
 P1:

begin
  --drop table spltest.tt_prmst;
  create table spltest.tt_prmst like spltest.co_prmst;
  insert into spltest.tt_prmst select * from spltest.co_prmst where pr_prdcd=LP_OLDCD;
  commit;
  update spltest.tt_prmst set pr_prdcd=LP_NEWCD where pr_prdcd=LP_OLDCD;
  commit;
  insert into spltest.co_prmst select * from spltest.tt_prmst;
  commit;
  update spltest.co_prmst set pr_stsfl='9' where pr_prdcd=LP_OLDCD;
  commit;
  update spltest.CO_QPMST  set  QP_prdcd = LP_NEWCD where  QP_PRDCD = LP_OLDCD;
  update spltest.CO_QVMST set  QV_prdcd = LP_NEWCD where  QV_PRDCD = LP_OLDCD;
  update spltest.CO_TXDOC set  TX_prdcd = LP_NEWCD where  TX_PRDCD = LP_OLDCD;
  update spltest.CO_TXSPC set  TXT_prdcd = LP_NEWCD where  TXT_PRDCD = LP_OLDCD;
  update spltest.EX_DATFL set  DT_prdcd = LP_NEWCD where  DT_PRDCD = LP_OLDCD;
  update spltest.EX_DATFL set  DT_fprcd = LP_NEWCD where  DT_FPRCD = LP_OLDCD;
  update spltest.FG_ISTRN set  IST_prdcd = LP_NEWCD where  IST_PRDCD = LP_OLDCD;
  update spltest.FG_OPSTK set  OP_prdcd = LP_NEWCD where  OP_PRDCD = LP_OLDCD;
  update spltest.FG_PTFRF set  PTF_prdcd = LP_NEWCD where  PTF_PRDCD = LP_OLDCD;
  update spltest.FG_PTFRF set  PTF_oprcd = LP_NEWCD where  PTF_OPRCD = LP_OLDCD;
  update spltest.FG_RCTRN set  RCT_prdcd = LP_NEWCD where  RCT_PRDCD = LP_OLDCD;
  update spltest.FG_RSTRN  set  RS_prdcd = LP_NEWCD where  RS_PRDCD = LP_OLDCD;
  update spltest.FG_STMST set  ST_prdcd = LP_NEWCD where  ST_PRDCD = LP_OLDCD;
  update spltest.FG_STMST set  ST_cprcd = LP_NEWCD where  ST_cprCD = LP_OLDCD;
  update spltest.FG_STMST set  ST_tprcd = LP_NEWCD where  ST_tprCD = LP_OLDCD;
  update spltest.MR_CRCLM set  CC_prdcd = LP_NEWCD where  CC_PRDCD = LP_OLDCD;
  update spltest.MR_DODAM set  DOD_prdcd = LP_NEWCD where  DOD_PRDCD = LP_OLDCD;
  update spltest.MR_DODEL  set  DOD_prdcd = LP_NEWCD where  DOD_PRDCD = LP_OLDCD;
  update spltest.MR_DOTAM  set  DOT_prdcd = LP_NEWCD where  DOT_PRDCD = LP_OLDCD;
  update spltest.MR_DOTRN  set  DOT_prdcd = LP_NEWCD where  DOT_PRDCD = LP_OLDCD;
  update spltest.MR_GRMST  set  GR_prdcd = LP_NEWCD where  GR_PRDCD = LP_OLDCD;
  update spltest.MR_INTAM  set  INT_prdcd = LP_NEWCD where  INT_PRDCD = LP_OLDCD;
  update spltest.MR_INTRN  set  INT_prdcd = LP_NEWCD where  INT_PRDCD = LP_OLDCD;
  update spltest.MR_IVTRN  set  IVT_prdcd = LP_NEWCD where  IVT_PRDCD = LP_OLDCD;
  update spltest.MR_PITRN  set  PIT_prdcd = LP_NEWCD where  PIT_PRDCD = LP_OLDCD;
  update spltest.MR_TMTRN  set  TM_prdcd = LP_NEWCD where  TM_PRDCD = LP_OLDCD;
  update spltest.PR_LTMST  set  LT_prdcd = LP_NEWCD where  LT_PRDCD = LP_OLDCD;
  update spltest.PR_LTMST  set  LT_cprcd = LP_NEWCD where  LT_cprCD = LP_OLDCD;
  update spltest.PR_LTMST  set  LT_tprcd = LP_NEWCD where  LT_tprCD = LP_OLDCD;
  update spltest.QC_QTTRN set  QTT_prdcd = LP_NEWCD where  QTT_PRDCD = LP_OLDCD;
  update spltest.QC_SMTRN set  SMT_prdcd = LP_NEWCD where  SMT_PRDCD = LP_OLDCD;
commit;
end P1;

commit;

drop table spltest.tt_prmst;
commit;
select * from spltest.co_prmst where pr_prdcd in ('5111201360','5111200360','5112200780','5112201780');


call spltest.chgPRDCD('5112200780','5112201780');
call spltest.chgPRDCD('5111201360','5111200360');

call spltest.chgPRDCD('5112201780','5112200780');
call spltest.chgPRDCD('5111200360','5111201360');


select * from spldata.qc_smtrn;

CREATE table spltest.qc_qttrn like spldata.qc_qttrn;

select * from spldata.co_prmst where pr_prdds like 'SC206%' order by pr_prdcd;
select * from spldata.co_prmst where pr_prdds like 'SH810%' order by pr_prdcd;

select * from spltest.fg_ptfrf;
select * from spltest.ex_datfl;



select * from spltest.fg_istrn where ist_prdcd='5111201360';
select * from spltest.fg_istrn where ist_prdcd='5111200360';

select * from spltest.fg_istrn where ist_prdcd='5112200780';
select * from spltest.fg_istrn where ist_prdcd='5112201780';



select distinct pr_stsfl from spldata.co_prmst;

select * from spldata.co_prmst where pr_stsfl='9';
