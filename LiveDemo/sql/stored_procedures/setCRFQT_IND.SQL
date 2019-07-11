

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--  Booking Carry forward
--  --------------------------------




--  Cutoff date 	:  cmt_ccsvl	SYS/MRXXCRF

--  Market type		: int_mkttp
--  Indent No.		: int_indno
--  Prd.Code		: int_prdcd
--  Pkg.Type		: int_pkgtp
--  Booking Date	: in_bkgdt
--  Invoce Date	: ivt_invdt
--  Status Flag		: int_stsfl	0: Unauthorised	X: cancelled
				
--  a.	Indent Qty.	: sum(int_indqt)
				 (Only those indents which are booked before cut-off date)

--  b.	Invoice Qty	: sum(ivt_invqt)
            	                          (Only those despatches which have taken place before
				  Cut-off Date)



--  Records with following condition are considerd for Carry Forward
 		a-b > 0    and 		Status Flag <> 'X'      and  Status Flag <> '0'

--  Carry Forward Qty.	:   a-b
		    int_cfwqt

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


;

drop procedure spldata.setCRFQT_IND;
commit;



create procedure spldata.setCRFQT_IND(IN LP_CMPCD char(2),IN LP_CUTDT char(10))  language sql  modifies sql data 
 P1:


begin
  --create table spldata.mr_inwrk (in_cmpcd varchar(2),in_mkttp varchar(2), in_indno varchar(9), int_prdcd varchar(10), int_pkgtp varchar(2),int_indqt decimal(10,3), int_fcmqt decimal(10,3),int_invqt decimal(10,3));
  delete from spldata.mr_inwrk;
  insert into spldata.mr_inwrk  select INT_CMPCD,INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP, isnull(INT_INDQT,0.000) INT_INDQT,isnull(INT_FCMQT,0.000) INT_FCMQT, sum(isnull(IVT_INVQT,0)) IVT_INVQT from   spldata.VW_INTRN   left outer join spldata.MR_IVTRN on INT_CMPCD = IVT_CMPCD and INT_MKTTP = IVT_MKTTP and INT_INDNO = IVT_INDNO and INT_PRDCD=IVT_PRDCD and INT_PKGTP = IVT_PKGTP   and IVT_STSFL <>'X'  and CONVERT(varchar,IVT_INVDT,101) < CONVERT(varchar,LP_CUTDT,101)  where int_cmpcd = lp_cmpcd and IN_BKGDT < CONVERT(varchar,LP_CUTDT,101) and  INT_STSFL <> '0'  and int_mkttp in ('01','04','05') group by INT_CMPCD,INT_MKTTP, INT_INDNO, INT_PRDCD, INT_PKGTP,INT_INDQT,INT_FCMQT  having isnull(INT_INDQT,0.000) > sum(isnull(IVT_INVQT,0));
  update spldata.co_cdtrn set cmt_ccsvl = LP_CUTDT where cmt_cgmtp='S' + lp_cmpcd and cmt_cgstp='MRXXCRF';
  commit;
  update spldata.mr_intrn set int_cfwqt=0 where int_cmpcd = lp_cmpcd and int_cfwqt<>0;
  commit;
  update spldata.mr_intrn set int_stsfl = '1' where int_cmpcd = lp_cmpcd and int_stsfl='1' and int_cmpcd + int_mkttp + int_indno + int_prdcd + int_pkgtp  in (select in_cmpcd + in_mkttp + in_indno + int_prdcd + int_pkgtp from spldata.mr_inwrk);
  commit;
end P1;

commit;

drop table spldata.mr_inwrk;
create table spldata.mr_inwrk (in_cmpcd varchar(2),in_mkttp varchar(2), in_indno varchar(9), int_prdcd varchar(10), int_pkgtp varchar(2),int_indqt decimal(10,3),int_fcmqt decimal(10,3), int_invqt decimal(10,3));
commit;
call spldata.setCRFQT_IND('01','05/01/2009');
commit;

