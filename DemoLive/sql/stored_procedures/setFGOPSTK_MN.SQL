-- Procedure for setting stock qty. in FG_OPSTK (during day end closing operation)
-- Records in fg_opstk are updated every day through "Day End Closing Operation (fg_hkdcc.java)"
-- In case the operation fails to update stock quantities in fg_opstk, This procedure should be executed

drop procedure spldata.setFGOPSTK_MN;
commit;

 
;

create procedure spldata.setFGOPSTK_MN(IN LP_CMPCD char(2))  language sql  modifies sql data 
 P1:
 begin

--drop table spldata.tt_fgopstk;
--commit;

update spldata.fg_opstk set op_mosqt=0, op_mouqt=0;
commit;

insert into spldata.fg_opstk (op_cmpcd,op_wrhtp,op_prdtp,op_prdcd,op_pkgtp,op_lupdt) select distinct st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp,current_date from spldata.fg_stmst where st_cmpcd = lp_cmpcd and (st_stkqt+st_uclqt+st_dosqt+st_douqt)>0 and st_wrhtp + st_prdtp + st_prdcd + st_pkgtp not in (select b.op_wrhtp + b.op_prdtp + b.op_prdcd + b.op_pkgtp from spldata.fg_opstk b);
commit;

create table spldata.tt_fgopstk (op_cmpcd varchar(2),op_wrhtp varchar(2), op_prdtp varchar(2), op_prdcd varchar(10),op_pkgtp varchar(2), op_stkqt decimal(10,3), op_uclqt decimal(10,3), op_dosqt decimal(10,3), op_douqt decimal(10,3));
commit;

insert into spldata.tt_fgopstk select st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp,sum(st_stkqt), sum(st_uclqt),sum(st_dosqt),sum(st_douqt) from spldata.fg_stmst group by st_cmpcd,st_wrhtp,st_prdtp,st_prdcd,st_pkgtp;
commit;

update spldata.fg_opstk a set op_mosqt = (select b.op_stkqt from spldata.tt_fgopstk b where a.op_cmpcd = lp_cmpcd and a.op_cmpcd = b.op_cmpcd and a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);

update spldata.fg_opstk a set op_mouqt = (select b.op_uclqt from spldata.tt_fgopstk b where a.op_cmpcd = lp_cmpcd and a.op_cmpcd = b.op_cmpcd and a.op_wrhtp = b.op_wrhtp and a.op_prdtp = b.op_prdtp and  a.op_prdcd = b.op_prdcd and  a.op_pkgtp = b.op_pkgtp) where a.op_cmpcd + a.op_wrhtp + a.op_prdtp + a.op_prdcd + a.op_pkgtp in (select c.op_cmpcd + c.op_wrhtp + c.op_prdtp + c.op_prdcd + c.op_pkgtp from spldata.tt_fgopstk c);

drop table spldata.tt_fgopstk;
commit;
end P1;
commit;


call spldata.setFGOPSTK_MN('01');
commit;
select * from spldata.fg_opstk;
update spldata.fg_opstk set op_mosqt=0, op_mouqt=0;
commit;
