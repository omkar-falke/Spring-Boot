
--                                 Op.Stock	Issue	Closing
--10290429	BK13         21.250	23.250	00.000


select * from spldata.fg_lwmst where lw_lotno='10290429' ;
select * from spldata.fg_rctrn where rct_lotno='10290429';
select * from spldata.fg_ptfrf where ptf_lotno='10290429';
select * from spldata.pr_ltmst where lt_lotno = '10290429';
select * from spldata.ex_datfl where dt_lotno = '10290429' ;
select * from spldata.fg_stmst where st_lotno='10290430';
select * from spldata.fg_istrn where ist_lotno='10290429' and ist_mnlcd='AA03';
select * from spldata.fg_istrn where ist_issno='00104521';
select * from spldata.fg_stmst where st_prdcd='5112050010' and st_stkqt>0;
commit;
select * from spldata.fg_istrn where ist_issno='00300262';





create table spldata.tt_istrn like spldata.fg_istrn;
commit;
delete from spldata.tt_istrn;
commit;
insert into spldata.tt_istrn select * from spldata.fg_istrn where ist_issno='00300262' and ist_lotno='10290429' and ist_mnlcd='AA03';
commit;
update spldata.fg_istrn set ist_issqt = 18.300, ist_isspk=(18.300/0.025) where ist_issno='00300262' and ist_lotno='10290429' and ist_mnlcd='AA03';
update spldata.tt_istrn set ist_mnlcd='XX01',ist_lotno='XXXXXXXX', ist_issqt=2.000, ist_isspk=(2.000/0.025);
commit;
insert into spldata.fg_istrn select * from spldata.tt_istrn;
commit;
update spldata.fg_stmst set st_stkqt = st_stkqt-2.000 where st_lotno='XXXXXXXX' and st_mnlcd='XX01';
commit;
commit;

