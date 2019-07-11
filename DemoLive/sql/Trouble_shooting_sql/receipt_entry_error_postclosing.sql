
Lot No. '81380689'   Qty. 1.375
Lot No. '81380690'   Qty.1.250
Entry Mistake : All qty. entered against Lot No. 81380689   Mistake noticed after two days locking


select * from spldata.fg_lwmst where lw_lotno='81380689' ;
select * from spldata.fg_rctrn where rct_lotno='81380689';
select * from spldata.fg_stmst where st_lotno='81380689';
select * from spldata.fg_ptfrf where ptf_lotno='81380689';
select * from spldata.pr_ltmst where lt_lotno = '81380689';
select * from spldata.ex_datfl where dt_lotno = '81380689' ;
select * from spldata.fg_opstk where op_prdcd='5212101480';

update spldata.fg_lwmst set lw_lotno = '81380690' where lw_lotno='81380689'  and lw_bagqt = 1.250;
drop table spldata.tt_rctrn;
create table spldata.tt_rctrn like spldata.fg_rctrn;
commit;
insert into spldata.tt_rctrn select * from spldata.fg_rctrn where rct_lotno='81380689';
commit;
update spldata.tt_rctrn set rct_lotno='81380690',rct_rctqt=1.250;
commit;
insert into spldata.fg_rctrn select * from spldata.tt_rctrn;
commit;
update spldata.fg_rctrn set rct_rctqt=1.375 where rct_lotno='81380689';
commit;

create table spldata.tt_stmst like spldata.fg_stmst;
commit;
insert into spldata.tt_stmst select * from spldata.fg_stmst where st_lotno='81380689';
commit;
update spldata.tt_stmst set st_lotno='81380690',st_prdcd='5121010120',st_cprcd='5121010120',st_tprcd='5121010120',st_douqt=1.250,st_uclqt=1.250;
commit;
insert into spldata.fg_stmst select * from spldata.tt_stmst;
commit;
update spldata.fg_stmst set st_douqt=1.375,st_uclqt=1.375 where st_lotno='81380689';
commit;

update spldata.pr_ltmst set lt_bagqt=1.375 where lt_lotno = '81380689';
update spldata.pr_ltmst set lt_bagqt=1.250 where lt_lotno = '81380690';
commit;


select * from spldata.ex_datfl where dt_lotno = '81380689' ;
create table spldata.tt_datfl like spldata.ex_datfl;
commit;
insert into spldata.tt_datfl select * from spldata.ex_datfl where dt_lotno='81380689';
commit;
update spldata.tt_datfl set dt_lotno='81380690',dt_prdcd='5121010120',dt_docqt=1.250;
commit;
insert into spldata.ex_datfl select * from spldata.tt_datfl;
commit;
update spldata.ex_datfl set dt_docqt=1.375 where dt_lotno='81380689';
commit;

;
;

drop table spldata.tt_rctrn;
drop table spldata.tt_stmst;
drop table spldata.tt_datfl;
commit;
