
create table spldata.co_cdtrn_newfg01 like spldata.co_cdtrn;
delete from spldata.co_cdtrn_newfg01;
insert into spldata.co_cdtrn_newfg01 select * from spldata.co_cdtrn where cmt_cgmtp = 'D01' and cmt_cgstp like 'FG%' and cmt_codcd like '9%';
commit;
update spldata.co_cdtrn_newfg01 set cmt_codcd = '0'||substr(cmt_codcd,2,2), cmt_ccsvl='00000' where cmt_codcd like '9__' and length(
cmt_ccsvl)=5;
update spldata.co_cdtrn_newfg01 set cmt_codcd = '0'||substr(cmt_codcd,2,2), cmt_ccsvl='0000' where cmt_codcd like '9__' and length(
cmt_ccsvl)=4;
commit;

-------------------------------------------------------------------------------------------------------------------------------------------------------
create table spldata.fg_opstk010409_01 like spldata.fg_opstk;
commit;
insert into spldata.fg_opstk010409_01 select * from spldata.fg_opstk where op_cmpcd='01';
commit;
create table spldata.fg_stmst010409_01 like spldata.fg_stmst;
commit;
insert into spldata.fg_stmst010409_01 select * from spldata.fg_stmst where st_cmpcd='01';
commit;

create table splhist.fg_opstk_010409_01 like spldata.fg_opstk;
commit;
insert into splhist.fg_opstk_010409_01 select * from spldata.fg_opstk where op_cmpcd='01';
commit;
create table splhist.fg_stmst_010409_01 like spldata.fg_stmst;
commit;
insert into splhist.fg_stmst_010409_01 select * from spldata.fg_stmst where st_cmpcd='01';
commit;

-------------------------------------------------------------------------------------------------------------------------------------------------------

create table spltest.co_cdtrn_oldfg01 like spldata.co_cdtrn;
commit;
insert into spltest.co_cdtrn_oldfg01 select * from spldata.co_cdtrn where cmt_cgmtp = 'D01' and cmt_cgstp like 'FG%' and cmt_codcd like '9%';
commit;
delete from spldata.co_cdtrn where cmt_cgmtp = 'D01' and cmt_cgstp like 'FG%' and cmt_codcd like '9%';
commit;
insert into spldata.co_cdtrn select * from spldata.co_cdtrn_newfg01;
commit;


call spldata.setFGOPSTK01_YR('01');
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------


