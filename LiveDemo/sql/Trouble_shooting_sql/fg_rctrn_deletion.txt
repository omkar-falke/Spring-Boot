-- Message from SPS-APT:
17.500 MT of SP400 1480, against Lot No.53990013, has been declared excess on 30/01/09. The receipt number for the transaction is 91002098.
 
Kindly arrange to delete the same from the system. 



select * from spldata.fg_rctrn where rct_lotno='53990013';
select * from spldata.fg_lwmst where lw_lotno='53990013' and lw_rctdt='01/30/2009' ;
select * from spldata.fg_rctrn where rct_lotno='53990013' and rct_rctdt='01/30/2009' ;
select * from spldata.fg_stmst where st_lotno='53990013';
select * from spldata.fg_ptfrf where ptf_lotno='53990013' and ptf_ptfdt='01/30/2009';
select * from spldata.pr_ltmst where lt_lotno = '53990013';
select * from spldata.ex_datfl where dt_lotno = '53990013' and dt_docdt = '01/30/2009';
select * from spldata.fg_opstk where op_prdcd='5212101480';

delete from spldata.fg_lwmst where lw_lotno='53990013' and lw_rctdt='01/30/2009' ;
delete from spldata.fg_rctrn where rct_lotno='53990013' and rct_rctdt='01/30/2009' ;
update spldata.fg_stmst set st_stkqt=st_stkqt-17.5,st_dosqt=st_dosqt-17.5 where st_lotno='53990013';
delete from spldata.fg_ptfrf where ptf_lotno='53990013' and ptf_ptfdt='01/30/2009';
update spldata.pr_ltmst set lt_bagqt=lt_bagqt-17.5 where lt_lotno = '53990013';
delete from spldata.ex_datfl where dt_lotno = '53990013' and dt_docdt = '01/30/2009';
commit;
update spldata.fg_opstk set op_yrcqt = op_yrcqt-17.5,op_mrcqt=op_mrcqt-17.5,op_stkqt=op_stkqt-17.5,op_dosqt=op_dosqt-17.5,op_drcqt=op_drcqt-17.5  where op_prdcd='5212101480';
commit;


Receipt details for 53990013 on 30/01/2009 have been deleted.
Please arrange to re-print corresponding reports (including PTF No. 90539 and Stock Statement)


Regards

S.R.Deshpande

