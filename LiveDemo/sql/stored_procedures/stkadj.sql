insert into spltest.sa_ppmst select * from spldata.sa_ppmst where pp_prgcd='HR_RPADT';
commit;
insert into spltest.sa_pprtr select * from spldata.sa_pprtr where ppr_prgcd='HR_RPADT';
commit;
select * from spldata.sa_ppmst where pp_prgcd='HR_RPADT';
update spldata.sa_ppmst  set pp_prgds = 'Attendance Details' where pp_prgcd='HR_RPADT';
update spltest.sa_ppmst  set pp_prgds = 'Attendance Details' where pp_prgcd='HR_RPADT';
commit;

