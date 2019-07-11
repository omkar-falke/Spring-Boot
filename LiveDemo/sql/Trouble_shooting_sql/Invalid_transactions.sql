
--                                 Op.Stock	Issue	Closing
--11191425	BK13         12.725	14.000	12.725
--11190412	AC20          15.300	30.600	-5.300
--BL22	9.500	
--BL23	9.500	
--BP01	17.100	
--BF17	9.500	

select * from spldata.pr_ltmst where lt_lotno='10290718' ;
select sum(lw_bagqt) from spldata.fg_lwmst where lw_lotno='10290718' and lw_rctdt='10/01/2009'  ;
select lw_mnlcd,lw_bagqt from spldata.fg_lwmst where lw_lotno='10290718' and lw_rctdt='10/01/2009'  ;
delete from spldata.fg_lwmst where lw_lotno='10290718' and lw_rctdt='10/01/2009'  ;
commit;
select sum(rct_rctqt) from spldata.fg_rctrn where rct_lotno='10290718' and rct_MNLCD in ('BL22','BL23','BP01','BF17') and rct_rctdt='10/01/2009';
delete from spldata.fg_rctrn where rct_lotno='10290718' and rct_MNLCD in ('BL22','BL23','BP01','BF17') and rct_rctdt='10/01/2009';
commit;
select sum(ist_issqt) from spldata.fg_istrn where ist_lotno='10290718' and ist_MNLCD in ('BL22','BL23','BP01','BF17') and ist_issdt='10/01/2009';
select sum(st_uclqt) from spldata.fg_stmst where st_lotno='10290718' and st_MNLCD in ('BL22','BL23','BP01','BF17');
delete from spldata.fg_stmst where st_lotno='10290718' and st_MNLCD in ('BL22','BL23','BP01','BF17');
commit;
select sum(ptf_ptfqt) from spldata.fg_ptfrf where ptf_lotno='10290718';
select * from spldata.pr_ltmst where lt_lotno = '10290718';
select * from spldata.ex_datfl where dt_lotno = '10290718' ;
select * from spldata.fg_opstk where op_prdcd='5112150150';

select * from spldata.fg_istrn where ist_lotno='11191425' and ist_mnlcd='BC11' and ist_issdt='09/02/2009';5.450;
select * from spldata.fg_istrn where ist_lotno='11191425' and ist_mnlcd='BC22' and ist_issdt='09/02/2009' and ist_issno='00105469';
update spldata.fg_istrn set ist_MNLCD='BC22' where ist_lotno='11191425' and ist_mnlcd='BC11' and ist_issdt='09/02/2009' and ist_issno='00105469';
select * from spldata.fg_istrn where ist_lotno='11191425' and ist_mnlcd='BF2';
create table spltest.fg_istrn like spldata.fg_istrn;
commit;
insert into spltest.fg_istrn select * from spldata.fg_istrn where ist_lotno='11191425' and ist_mnlcd='BC11' and ist_issdt='09/02/2009' and ist_issno='00105469';
commit;
select * from spltest.fg_istrn;
update spltest.fg_istrn set ist_mnlcd='BC22';
insert into spldata.fg_istrn select * from spltest.fg_istrn;
select * from spldata.fg_istrn where ist_lotno='11191425' and ist_mnlcd='BC11' and ist_issdt='09/02/2009' and ist_issno='00105469';
delete from spldata.fg_istrn where ist_lotno='11191425' and ist_mnlcd='BC11' and ist_issdt='09/02/2009' and ist_issno='00105469';
commit;
update spldata.fg_istrn set ist_cmpcd='01' where ist_lotno='11191425' and ist_mnlcd='BC22' and ist_issdt='09/02/2009' and ist_issno='00105469';
update spldata.fg_istrn set ist_mnlcd='BC11' where ist_lotno='11191425' and ist_mnlcd='BC22' and ist_issdt='09/02/2009' and ist_issno='00105469';
commit;
select * from spldata.ist_istrn where where ist_lotno='11191425' and ist_mnlcd='BC22' and ist_issdt='09/02/2009' and ist_issno='00105469';

select * from spldata.fg_stmst where  where st_lotno='11191425' and st_mnlcd='BC11';
select * from spldata.fg_istrn where ist_lotno='11191425' and ist_issdt='09/02/2009'; and ist_mnlcd='BC22';
select * from spldata.fg_stmst where st_lotno='11191425' and st_mnlcd='BC22';

select * from spldata.fg_stmst where st_lotno='11191425' and st_mnlcd='BF21';
select * from spldata.fg_istrn where ist_lotno='11191425' and ist_issdt='09/02/2009' and ist_mnlcd='BF21';

select * from spldata.fg_stmst where st_lotno='11191425' and st_mnlcd='BC22';

SELECT * FROM SPLDATA.FG_ISTRN WHERE IST_LOTNO='11191425' and ist_mnlcd='BC22' and ist_issdt='09/02/2009' AND IST_ISSNO='00105469';
update SPLDATA.FG_ISTRN set ist_issqt=ist_issqt+1.125,ist_isspk=261 WHERE IST_LOTNO='11191425' and ist_mnlcd='BC22' and ist_issdt='09/02/2009' AND IST_ISSNO='00105469';

delete FROM SPLDATA.FG_ISTRN WHERE IST_LOTNO='11191425' and ist_mnlcd='BF21' and ist_issdt='09/02/2009' AND IST_ISSNO='00105469';
select sum(st_stkqt) from spldata.fg_stmst where st_lotno='11191425'; and st_mnlcd='BC22';
update spldata.fg_stmst set st_stkqt=st_stkqt - 1.125 where st_lotno='11191425' and st_mnlcd='BC22';
commit;
delete from spltest.fg_istrn;
commit;
insert into spltest.fg_istrn SELECT * FROM SPLDATA.FG_ISTRN WHERE IST_LOTNO='11191425' AND IST_ISSNO='00105469' and ist_mnlcd='BF21';
commit;
select * from spltest.fg_istrn;
select
insert into spldata.fg_istrn select * from spltest.fg_istrn;
update spltest.fg_istrn set ist_mnlcd='BC22';

select * from spldata.fg_stmst where st_lotno='11191425' and st_mnlcd='BC22';
update spldata.fg_stmst set st_stkqt = 12.250 where st_lotno='11191425' and st_mnlcd='BC22';
commit;
select sum(ist_issqt)  from spldata.fg_istrn where ist_lotno='11191425' and ist_issdt='09/02/2009';;
commit;
select * from spldata.pr_ltmst where lt_lotno='11191425';

select * from spldata.pr_ltmst where lt_lotno='11191425';

select *  from spldata.fg_istrn where ist_lotno='11191425' and ist_issdt='09/02/2009' and ist_issno='00105469'; and ist_mnlcd='BC22' ;;
update spldata.fg_istrn set ist_issqt=6.575+2.425 where ist_lotno='11191425' and ist_issdt='09/02/2009' and ist_issno='00105469';
commit;
select * from spldata.fg_stmst where st_lotno='11191425' and st_mnlcd='BC22';
update spldata.fg_stmst set st_stkqt=9.825 where st_lotno='11191425' and st_mnlcd='BC22';
commit;

select *  from spldata.fg_istrn where ist_lotno='11191439' and ist_issdt='09/02/2009'; and ist_issno='00105469'; and ist_mnlcd='BC22' ;;
commit;
select * from spldata.fg_stmst where st_lotno='11191439' and st_mnlcd='BE06';
update spldata.fg_stmst set st_stkqt=0.075 where st_lotno='11191439' and st_mnlcd='BE06';
commit;
select * from spldata.pr_ltmst where lt_lotno='11191439';
update spldata.pr_ltmst set lt_dspqt=28.850 where lt_lotno='11191425';
select sum(ist_issqt) from spldata.fg_istrn where ist_lotno='11191439';
commit;
