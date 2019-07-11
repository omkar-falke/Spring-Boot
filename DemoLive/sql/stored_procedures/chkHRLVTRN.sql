
select ep_cmpcd,ep_dptcd,ep_yopcl,ep_ytdcl,sum(lvt_lveqt),ep_empno,ep_fstnm,ep_lstnm from spldata.hr_epmst  left outer join spldata.hr_lvtrn on ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='CL'  and lvt_lvedt > '12/31/2008' and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009'  group by ep_cmpcd,ep_dptcd,ep_empno,ep_fstnm,ep_lstnm,ep_yopcl,ep_ytdcl  having ep_yopcl <> isnull(ep_ytdcl,0)+isnull(sum(lvt_lveqt),0)   order by ep_cmpcd,ep_dptcd,ep_empno;


select ep_cmpcd,ep_dptcd,ep_yoppl,ep_ytdpl,sum(lvt_lveqt),ep_empno,ep_fstnm,ep_lstnm from spldata.hr_epmst left outer join spldata.hr_lvtrn on  ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='PL'  and lvt_lvedt > '12/31/2008'  and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009' group by ep_cmpcd,ep_dptcd,ep_empno,ep_fstnm,ep_lstnm,ep_yoppl,ep_ytdpl  having ep_yoppl <>isnull(ep_ytdpl,0)+isnull(sum(lvt_lveqt),0) order by ep_cmpcd,ep_dptcd,ep_empno;

select ep_dptcd,ep_yoprh,ep_ytdrh,sum(lvt_lveqt),ep_empno,ep_fstnm,ep_lstnm from spldata.hr_epmst left outer join spldata.hr_lvtrn on ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='RH'    and lvt_lvedt > '12/31/2008'  and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009' group by ep_dptcd,ep_empno,ep_fstnm,ep_lstnm,ep_yoprh,ep_ytdrh  having ep_yoprh <>isnull(ep_ytdrh,0)+isnull(sum(lvt_lveqt),0) order by ep_dptcd,ep_empno;

select ep_cmpcd,ep_dptcd,ep_yopsl,ep_ytdsl,sum(lvt_lveqt),ep_empno,ep_fstnm,ep_lstnm from spldata.hr_epmst left outer join spldata.hr_lvtrn on ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='SL'    and lvt_lvedt > '12/31/2008'  and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009' group by ep_cmpcd,ep_dptcd,ep_empno,ep_fstnm,ep_lstnm,ep_yopsl,ep_ytdsl  having ep_yopsl <>isnull(ep_ytdsl,0)+isnull(sum(lvt_lveqt),0) order by ep_cmpcd,ep_dptcd,ep_empno;



create table spldata.tt_lvchk (ep_cmpcd varchar(2),ep_empno varchar(4),ep_yopxx decimal (5,1), ep_ytdxx decimal (5,1), ep_totxx decimal (5,1),ep_ytdxx1  decimal (5,1));
commit;

delete from spldata.tt_lvchk;
commit;
insert into spldata.tt_lvchk (ep_cmpcd,ep_empno,ep_yopxx,ep_ytdxx,ep_totxx) select ep_cmpcd,ep_empno,ep_yopcl,ep_ytdcl,sum(lvt_lveqt) from spldata.hr_epmst left outer join spldata.hr_lvtrn on ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='CL'    and lvt_lvedt > '12/31/2008'  and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009' group by ep_cmpcd,ep_empno,ep_yopcl,ep_ytdcl  having ep_yopcl <> isnull(ep_ytdcl,0)+isnull(sum(lvt_lveqt),0) order by ep_cmpcd,ep_empno;
commit;
update spldata.tt_lvchk set ep_ytdxx1 = isnull(ep_yopxx,0)-isnull(ep_totxx,0);
commit;
select * from spldata.tt_lvchk;
update spldata.hr_epmst a set ep_ytdcl = (select b.ep_ytdxx1 from spldata.tt_lvchk b where a.ep_cmpcd = b.ep_cmpcd and a.ep_empno = b.ep_empno) where a.ep_cmpcd + a.ep_empno in (select c.ep_cmpcd + c.ep_empno from spldata.tt_lvchk c);
commit;

delete from spldata.tt_lvchk;
commit;
insert into spldata.tt_lvchk (ep_cmpcd,ep_empno,ep_yopxx,ep_ytdxx,ep_totxx) select ep_cmpcd,ep_empno,ep_yoppl,ep_ytdpl,sum(lvt_lveqt) from spldata.hr_epmst left outer join spldata.hr_lvtrn on ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='PL'    and lvt_lvedt > '12/31/2008'  and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009' group by ep_cmpcd,ep_empno,ep_yoppl,ep_ytdpl  having ep_yopPL <>isnull(ep_ytdPL,0)+isnull(sum(lvt_lveqt),0) order by ep_cmpcd,ep_empno;
commit;
update spldata.tt_lvchk set ep_ytdxx1 = isnull(ep_yopxx,0)-isnull(ep_totxx,0);
commit;
select * from spldata.tt_lvchk;
update spldata.hr_epmst a set ep_ytdPL = (select b.ep_ytdxx1 from spldata.tt_lvchk b where a.ep_cmpcd = b.ep_cmpcd and a.ep_empno = b.ep_empno) where a.ep_cmpcd + a.ep_empno in (select c.ep_cmpcd + c.ep_empno from spldata.tt_lvchk c);
commit;


delete from spldata.tt_lvchk;
commit;
insert into spldata.tt_lvchk (ep_cmpcd,ep_empno,ep_yopxx,ep_ytdxx,ep_totxx) select ep_cmpcd,ep_empno,ep_yopRH,ep_ytdRH,sum(lvt_lveqt) from spldata.hr_epmst left outer join spldata.hr_lvtrn on ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='RH'    and lvt_lvedt > '12/31/2008'  and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009' group by ep_cmpcd,ep_empno,ep_yopRH,ep_ytdRH  having ep_yopRH <>isnull(ep_ytdRH,0)+isnull(sum(lvt_lveqt),0) order by ep_cmpcd,ep_empno;
commit;
update spldata.tt_lvchk set ep_ytdxx1 = isnull(ep_yopxx,0)-isnull(ep_totxx,0);
commit;
select * from spldata.tt_lvchk;
update spldata.hr_epmst a set ep_ytdRH = (select b.ep_ytdxx1 from spldata.tt_lvchk b where a.ep_cmpcd = b.ep_cmpcd and a.ep_empno = b.ep_empno) where a.ep_cmpcd + a.ep_empno in (select c.ep_cmpcd + c.ep_empno from spldata.tt_lvchk c);
commit;


delete from spldata.tt_lvchk;
commit;
insert into spldata.tt_lvchk (ep_cmpcd,ep_empno,ep_yopxx,ep_ytdxx,ep_totxx) select ep_cmpcd,ep_empno,ep_yopSL,ep_ytdSL,sum(lvt_lveqt) from spldata.hr_epmst left outer join spldata.hr_lvtrn on ep_cmpcd=lvt_cmpcd and ep_empno=lvt_empno and lvt_lvecd='SL'    and lvt_lvedt > '12/31/2008'  and lvt_stsfl in ('2','3') and ep_yopdt = '01/01/2009' group by ep_cmpcd,ep_empno,ep_yopSL,ep_ytdSL  having ep_yopSL <>isnull(ep_ytdSL,0)+isnull(sum(lvt_lveqt),0) order by ep_cmpcd,ep_empno;
commit;
update spldata.tt_lvchk set ep_ytdxx1 = isnull(ep_yopxx,0)-isnull(ep_totxx,0);
commit;
select * from spldata.tt_lvchk;
update spldata.hr_epmst a set ep_ytdSL = (select b.ep_ytdxx1 from spldata.tt_lvchk b where a.ep_cmpcd = b.ep_cmpcd and a.ep_empno = b.ep_empno) where a.ep_cmpcd + a.ep_empno in (select c.ep_cmpcd + c.ep_empno from spldata.tt_lvchk c);
commit;

