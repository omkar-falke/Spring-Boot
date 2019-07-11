-- Procedure for setting stock qty. in FG_OPSTK (during day end closing operation)
-- Records in fg_opstk are updated every day through "Day End Closing Operation (fg_hkdcc.java)"
-- In case the operation fails to update stock quantities in fg_opstk, This procedure should be executed

drop procedure spldata.setFGOPSTK;
commit;

 
;

create procedure spldata.setFGOPSTK(IN LP_CMPCD char(2))  language sql  modifies sql data 
 P1:
 begin

--drop table spldata.tt_fgopstk;
--commit;

insert into spldata.fg_opstk (op_cmpcd,op_wrhtp,op_prdtp,op_prdcd,op_pkgtp,op_lupdt) select distinct st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp,current_date from spldata.fg_stmst where st_cmpcd = LP_CMPCD and (st_stkqt+st_uclqt+st_dosqt+st_douqt)>0 and st_cmpcd + st_wrhtp + st_prdtp + st_prdcd + st_pkgtp not in (select b.op_cmpcd + b.op_wrhtp + b.op_prdtp + b.op_prdcd + b.op_pkgtp from spldata.fg_opstk b);
commit;

create table spldata.tt_fgopstk (op_cmpcd varchar(2),op_wrhtp varchar(2), op_prdtp varchar(2), op_prdcd varchar(10),op_pkgtp varchar(2), op_stkqt decimal(10,3), op_uclqt decimal(10,3), op_dosqt decimal(10,3), op_douqt decimal(10,3));
commit;

insert into spldata.tt_fgopstk select st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp,sum(st_stkqt), sum(st_uclqt),sum(st_dosqt),sum(st_douqt) from spldata.fg_stmst group by st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp;
commit;

update spldata.fg_opstk a set op_stkqt = (select b.op_stkqt from spldata.tt_fgopstk b where a.op_cmpcd=b.op_cmpcd and a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);

update spldata.fg_opstk a set op_uclqt = (select b.op_uclqt from spldata.tt_fgopstk b where a.op_cmpcd=b.op_cmpcd and a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);

update spldata.fg_opstk a set op_dosqt = (select b.op_dosqt from spldata.tt_fgopstk b where a.op_cmpcd=b.op_cmpcd and  a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);

update spldata.fg_opstk a set op_douqt = (select b.op_douqt from spldata.tt_fgopstk b where a.op_cmpcd=b.op_cmpcd and a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);
commit;

drop table spldata.tt_fgopstk;
commit;
create table spldata.tt_fgopstk (op_cmpcd varchar(2),op_wrhtp varchar(2), op_prdtp varchar(2), op_prdcd varchar(10),op_pkgtp varchar(2), op_stkqt decimal(10,3));
commit;

insert into spldata.tt_fgopstk select st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp,sum(st_stkqt)  from spldata.fg_stmst  where st_cmpcd = LP_CMPCD and length(trim(isnull(st_remds,'')))>1 group by st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp;
commit;

update spldata.fg_opstk a set op_tdsqt = (select b.op_stkqt from spldata.tt_fgopstk b where a.op_cmpcd = b.op_cmpcd and a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);
commit;

delete from spldata.tt_fgopstk;
commit;
insert into spldata.tt_fgopstk select st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp,sum(st_stkqt)  from spldata.fg_stmst  where st_cmpcd = LP_CMPCD and st_stsfl='2' group by st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp;
commit;

update spldata.fg_opstk a set op_slrqt = (select b.op_stkqt from spldata.tt_fgopstk b where a.op_cmpcd = b.op_cmpcd and a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);
commit;

drop table spldata.tt_fgopstk;
commit;


commit;
end P1;
drop table spldata.tt_fgopstk;
commit;
call spldata.setFGOPSTK('01');
commit;

select sum(op_stkqt),sum(op_dosqt) from spldata.fg_opstk where op_cmpcd='01';
