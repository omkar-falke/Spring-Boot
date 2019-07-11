
--                                 Op.Stock	Issue	Closing --10290135	BK13         12.725	14.000	12.725 --11190412	AC20          15.300	30.600	-5.300   select * from spldata.fg_lwmst where lw_lotno='10290135' ;
select * from spldata.fg_rctrn where rct_lotno='10290135';
select * from spldata.fg_istrn where iss_lotno='10290135';
select * from spldata.fg_ptfrf where ptf_lotno='10290135';
select * from spldata.pr_ltmst where lt_lotno = '10290135';
select * from spldata.ex_datfl where dt_lotno = '10290135' ;
select * from spldata.fg_opstk where op_prdcd='5212101480';


--                                 Op.Stock	Issue	Closing
--10290135	BK13         12.725	14.000	12.725
--11190412	AC20          15.300	30.600	-5.300

select * from spldata.fg_stmst where st_lotno='11190412';
select * from spldata.fg_istrn where ist_lotno='11190412';
select * from spldata.fg_istrn where ist_lotno='11190412' and ist_issno in ('90114080') and ist_mnlcd='AC20';
update spldata.fg_istrn set ist_mnlcd='AC22' where ist_lotno='11190412' and ist_issno in ('90114080') and ist_mnlcd='AC20';
commit;
delete from  spldata.fg_istrn  where ist_lotno='11190412' and ist_issno in ('90114082') and ist_mnlcd='AC20';
update spldata.fg_istrn set ist_issqt=ist_issqt+5.300 where ist_lotno='11190412' and ist_issno in ('90114082') and ist_mnlcd='AC22';
commit;

update  spldata.fg_stmst set st_stkqt=st_stkqt-5.300 where st_lotno='11190412' and st_mnlcd='AC22';
commit;
select * from spldata.fg_stmst where st_stkqt<0;
update spldata.fg_stmst set st_stkqt=0 where st_stkqt<0;
commit;




create table spldata.tt_istrn like spldata.fg_istrn;
commit;
delete from spldata.tt_istrn;
commit;
insert into spldata.tt_istrn select * from spldata.fg_istrn where ist_issno='90114051' and ist_lotno='11190412' and ist_mnlcd='BK13';
commit;
update spldata.fg_istrn set ist_issqt = 12.725, ist_isspk=(12.725/0.025) where ist_issno='90114051' and ist_lotno='11190412' and ist_mnlcd='BK13';
update spldata.tt_istrn set ist_mnlcd='BH02', ist_issqt=14.000-12.725, ist_isspk=((14.000-12.725)/0.025);
commit;
insert into spldata.fg_istrn select * from spldata.tt_istrn;
commit;
update spldata.fg_stmst set st_stkqt = st_dosqt-(1.275+1.000) where st_lotno='11190412' and st_mnlcd='BH02';
commit;
update spldata.fg_stmst set st_stkqt = 0 where st_lotno='11190412' and st_mnlcd='BK13';
update spldata.fg_stmst set st_stkqt = st_stkqt -(14.000-12.725) where st_lotno='11190412' and st_mnlcd='BH02';
commit;

