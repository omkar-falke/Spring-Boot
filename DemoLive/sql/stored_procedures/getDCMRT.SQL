ll-- procedure to arrive at Distributor commission rate for specific Distributor
-- This procedure is to be executed from other procedures where Distributor Commission is to be calculated.
-- Procedure is called through stored procedure updPTTRN_INV  (Credit Note record auto creation)
--                                                                MR_INDRLSN    (Indent based realisation reworking)
--                                                                MR_INVRLSN     (Invoice based realisation reworking)
-- Similar logic is made applicable for Triggers at Customer Order Authorisation      insRLSN_IND,       updRLSN_IND
--                                                                  and Invoice Authorisation            updINVQT
-- Commission category is picked up through Sale Type and product category combination
-- Commission rate is picked up through Distributor code and Commission category combination
--  Commission is not applicable for Scrap Grade Sale
--  Scarp grade 		: SUBSTRING(int_prdcd,1,6) = '519895'    or SUBSTRING(int_prdcd,1,6) = '529895'


;

COMMIT;

drop procedure spldata.getDCMRT;
commit;


create procedure spldata.getDCMRT(IN LP_DSRTP char(1), IN LP_DSRCD char(5), IN LP_PRDCD char(10),IN LP_SALTP char(2), IN LP_GRPCD char(5) , OUT L_COMRT decimal(8))  language sql  modifies sql data 
P1:

begin
  declare L_COMCT char(2);
  declare L_PRTCT int default 0;
       -- For videocon sale, the distributor commission rate is different (in case of SJP)
  declare END_TABLE int default 0;
    set L_COMCT = 'XX';
      select isnull(CMT_CCSVL,'XX') into L_COMCT from spldata.CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCL' and cmt_codcd =  LP_SALTP + '_' + SUBSTRING(LP_PRDCD,1,4);
      if L_COMCT = 'XX' then
          select isnull(CMT_CCSVL,'XX') into L_COMCT from spldata.CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCL' and cmt_codcd =  LP_SALTP + '_' + SUBSTRING(LP_PRDCD,1,3) + 'X';
          if L_COMCT = 'XX' then
             select isnull(CMT_CCSVL,'XX') into L_COMCT from spldata.CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCL' and cmt_codcd =  LP_SALTP + '_' + SUBSTRING(LP_PRDCD,1,2) + 'XX';
          end if;
      end if;

      set L_COMRT = -1;
      if L_COMCT <> 'XX' then
         if  SUBSTRING(LP_PRDCD,1,6)  not in ( '519795','529795','519895','529895' )  then 
             select count(*) into L_PRTCT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and SUBSTRING(CMT_CODCD,8,5) = LP_GRPCD;
             if L_PRTCT > 0  then 
                select isnull(CMT_NCSVL,0)  into L_COMRT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and cmt_codcd=LP_DSRTP + LP_DSRCD + '_' + LP_GRPCD + '_' + L_COMCT;
             end if;
             if L_COMRT = -1  then 
                select isnull(CMT_NCSVL,0)  into L_COMRT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and cmt_codcd=LP_DSRTP + LP_DSRCD + '_XXXXX_' + L_COMCT;
             end if;
             --insert into spldata.temp_chk values(LP_DSRTP + LP_DSRCD + '_XXXXX_' + L_COMCT + '    ' + char(L_COMRT));
         end if;    
      end if;
      if L_COMRT = -1 then
         set L_COMRT = 0;
      end if;
end;
commit;



select * from spldata.co_cdtrn where cmt_cgstp='MRXXDCM' and cmt_cgmtp='SYS';
update spldata.co_cdtrn set cmt_codcd='D' + cmt_codcd where cmt_cgstp='MRXXDCM' and cmt_cgmtp='SYS';
commit;


 Select LT_RETQT,LT_HLDQT from spldata.PR_LTMST where LT_PRDTP='01' and LT_LOTNO='10160748' and LT_RCLNO='00';

             select count(*)  from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and SUBSTRING(CMT_CODCD,8,5) = 'V0012';
             select distinct SUBSTRING(CMT_CODCD,8,5)  from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' ;


select * from spldata.mr_pttrn where pt_crdtp='03' and pt_invno in (select ivt_invno from spldata.mr_ivtrn where ivt_byrcd='V0219');

select * from spldata.sa_ustrn where ust_usrcd='DRP';
select * from spldata.sa_ustrn where ust_usrcd='CTD';

 select * from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and cmt_codcd like 'DS0006_______06%';
update spldata.co_cdtrn set cmt_ncsvl=1000 where cmt_cgmtp='SYS' and cmt_cgstp='MRXXDCM' and cmt_codcd like 'DS0006_______06%';
commit;

