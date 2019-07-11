select * from spldata.co_cdtrn where cmt_cgstp='FGXXRCT' and cmt_cgmtp='D01' and cmt_codcd='010';
update spldata.co_cdtrn set cmt_ccsvl = '01766' where cmt_cgstp='FGXXRCT' and cmt_cgmtp='D01' and cmt_codcd='010';
commit;

select * from spldata.fg_rctrn where rct_rctno = '01001752' and rct_cmpcd='01';
update spldata.fg_rctrn set rct_rctno = '01001763' where rct_rctno = '01001752' and rct_cmpcd='01';
commit;
select * from spldata.fg_lwmst where lw_rctno = '01001752' and lw_cmpcd='01';
update spldata.fg_lwmst set lw_rctno='01001763' where lw_rctno = '01001752' and lw_cmpcd='01';
commit;

select * from spldata.ex_datfl where dt_docdt='12/20/2009';
delete from spldata.ex_datfl where dt_docdt='12/20/2009';
commit;
