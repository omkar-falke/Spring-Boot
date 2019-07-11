select sum(st_dosqt),sum(st_stkqt),sum(st_douqt),sum(st_uclqt),sum(st_posqt),sum(st_pouqt) from spldata.fg_stmst where st_cmpcd='11';
update spldata.fg_stmst set st_dosqt=st_posqt,st_douqt=st_pouqt where st_cmpcd='11';
update spldata.co_cdtrn set cmt_ccsvl = '27/12/2009' where cmt_cgmtp='S11' and cmt_cgstp='FGXXREF' and cmt_codcd='DOCDT';
commit;
commit;

