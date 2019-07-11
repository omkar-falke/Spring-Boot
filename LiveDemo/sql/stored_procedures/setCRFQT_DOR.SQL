

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--  Booking Carry forward
--  --------------------------------




--  Cutoff date 	:  cmt_ccsvl	SYS/MRXXCRF

--  Market type		: dot_mkttp
--  Indent No.		: dot_dorno
--  Prd.Code		: dot_prdcd
--  Pkg.Type		: dot_pkgtp
--  Booking Date	: dot_dordt
--  Invoce Date	: ivt_invdt
--  Status Flag		: dot_stsfl	0: Unauthorised	X: cancelled
				
--  a.	Indent Qty.	: sum(dot_dorqt)
				 (Only those indents which are booked before cut-off date)

--  b.	Invoice Qty	: sum(ivt_invqt)
            	                          (Only those despatches which have taken place before
				  Cut-off Date)



--  Records with following condition are considerd for Carry Forward
 		a-b > 0    and 		Status Flag <> 'X'      and  Status Flag <> '0'

--  Carry Forward Qty.	:   a-b
		    dot_cfwqt

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------



;
drop procedure spldata.setCRFQT_DOR;
commit;



create procedure spldata.setCRFQT_DOR(IN LP_CMPCD char(2),IN LP_CUTDT char(10))  language sql  modifies sql data 

 P1:





begin

  create table spldata.tt_dowrk (dot_mkttp varchar(2), dot_dorno varchar(9), dot_prdcd varchar(10), dot_pkgtp varchar(2),dot_dorqt decimal(10,3), dot_invqt decimal(10,3));

  insert into spldata.tt_dowrk  select dot_MKTTP, dot_DORNO, dot_PRDCD, dot_PKGTP, isnull(dot_DORQT,0.000) dot_DORQT, sum(isnull(IVT_INVQT,0)) IVT_INVQT from   spldata.MR_DOTRN   left outer join spldata.MR_IVTRN on dot_cmpcd=ivt_cmpcd and dot_MKTTP = IVT_MKTTP and dot_DORNO = IVT_DORNO and dot_PRDCD=IVT_PRDCD and dot_PKGTP = IVT_PKGTP   and IVT_STSFL <>'X'  and CONVERT(varchar,IVT_INVDT,101) < CONVERT(varchar,LP_CUTDT,101)  where dot_cmpcd = lp_cmpcd and dot_dordt < CONVERT(varchar,LP_CUTDT,101) and  dot_STSFL <> '0'  and dot_mkttp in ('01','04','05') group by dot_MKTTP, dot_DORNO, dot_PRDCD, dot_PKGTP,dot_DORQT  having (isnull(dot_DORQT,0) - sum(isnull(IVT_INVQT,0))) > 0;

  update spldata.co_cdtrn set cmt_ccsvl = LP_CUTDT where cmt_cgmtp='S' + LP_CMPCD and cmt_cgstp='MRDOCRF';

  commit;

  update spldata.mr_dotrn set dot_cfwqt=0 where dot_cmpcd = lp_cmpcd and isnull(dot_cfwqt,0)<>0;

  commit;

  update spldata.mr_dotrn set dot_stsfl = '1' where dot_cmpcd = lp_cmpcd and dot_stsfl='1' and dot_mkttp + dot_dorno + dot_prdcd + dot_pkgtp  in (select dot_mkttp + dot_dorno + dot_prdcd + dot_pkgtp from spldata.tt_dowrk);

  commit;

end P1;

commit;

drop table spldata.tt_dowrk;
commit;
call spldata.setCRFQT_DOR('05/01/2007');
commit;
