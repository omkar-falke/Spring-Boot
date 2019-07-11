
select a.swt_empno,a.swt_wrkdt,a.swt_srlno,b.swt_srlno,a.swt_inctm,a.swt_outtm,b.swt_inctm,b.swt_outtm from spldata.hr_swtrn_tmp a, spldata.hr_swtrn b where a.swt_cmpcd='01' and a.swt_wrkdt > '07/01/2009' and a.swt_cmpcd=b.swt_cmpcd and a.swt_empno = b.swt_empno and a.swt_wrkdt = b.swt_wrkdt and a.swt_srlno=b.swt_srlno and (ifnull(a.swt_inctm,current_timestamp)<>ifnull(b.swt_inctm,current_timestamp) or ifnull(a.swt_outtm,current_timestamp)<>ifnull(b.swt_outtm,current_timestamp) )  order by swt_wrkdt;

select a.swt_empno,a.swt_wrkdt,a.swt_srlno,b.swt_srlno,a.swt_inctm,a.swt_outtm,b.swt_inctm,b.swt_outtm from spldata.hr_swtrn_tmp a, spldata.hr_swtrn b where a.swt_cmpcd='01' and a.swt_empno = '5040' and  a.swt_wrkdt = '07/22/2009' and a.swt_cmpcd=b.swt_cmpcd and a.swt_empno = b.swt_empno and a.swt_wrkdt = b.swt_wrkdt and a.swt_srlno=b.swt_srlno;
select * from spldata.hr_swtrn a where a.swt_cmpcd='01' and a.swt_empno = '5099' and  a.swt_wrkdt = '07/14/2009' ;
select * from spldata.hr_swtrn_tmp a where a.swt_cmpcd='01' and a.swt_empno = '5099' and  a.swt_wrkdt = '07/14/2009' ;

 and (ifnull(a.swt_inctm,current_timestamp)<>ifnull(b.swt_inctm,current_timestamp) or ifnull(a.swt_outtm,current_timestamp)<>ifnull(b.swt_outtm,current_timestamp) )  order by swt_wrkdt;


delete from spldata.hr_swtrn where swt_cmpcd='01' and swt_wrkdt = '07/14/2009'    and  swt_empno in ('5099');
commit;
insert into spldata.hr_swtrn select *  from spldata.hr_swtrn_tmp where swt_cmpcd='01' and swt_wrkdt = '07/14/2009'    and  swt_empno in ('5099');
commit;


