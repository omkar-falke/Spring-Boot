
/* This procedure sets right lt_bagqt and lt_dspqt in PR_LTMST,
 by using actual bagging / despatch figures from transaction table
parameters to be passed BAGQT or DSPQT (as desired) and Starting date in 'mm/dd/yyyy' format
*/

drop procedure spldata.setLTMST_QT;
commit;


create procedure spldata.setLTMST_QT(IN LP_CMPCD char(2),IN LP_QTYCD varchar(5), IN LP_STRDT varchar(10))  language sql  modifies sql data 
P1:

  begin
 
  declare L_CMPCD varchar(2);
  declare L_PRDTP varchar(2);
  declare L_LOTNO varchar(8);
  declare L_RCLNO varchar(2);
  declare L_BAGQT decimal(10,3);
  declare L_RCTQT decimal(10,3);
  declare L_DSPQT decimal(10,3);
  declare L_ISSQT decimal(10,3);
  declare L_RECCT int default 0;
  declare END_TABLE int default 0;

  declare C_RCT cursor for select lt_prdtp,lt_lotno,lt_rclno,isnull(lt_bagqt,0) lt_bagqt,sum(isnull(rct_rctqt,0)) from spldata.pr_ltmst left outer join spldata.fg_rctrn on  lt_cmpcd = rct_cmpcd and lt_prdtp=rct_prdtp and lt_lotno = rct_lotno and lt_rclno = rct_rclno and rct_rcttp in ('10','15')  where lt_cmpcd = LP_CMPCD and CONVERT(varchar,lt_pstdt,101)>LP_STRDT and  lt_lotno not like '900%' group by lt_prdtp,lt_lotno,lt_rclno,lt_bagqt having isnull(sum(isnull(rct_rctqt,0)),0)<>isnull(lt_bagqt,0);

  declare C_ISS cursor for select lt_prdtp,lt_lotno,lt_rclno,isnull(lt_dspqt,0) lt_dspqt,sum(ist_issqt) from spldata.pr_ltmst,spldata.fg_istrn where lt_cmpcd=LP_CMPCD and lt_cmpcd = ist_cmpcd and lt_prdtp=ist_prdtp and lt_lotno = ist_lotno and lt_rclno = ist_rclno and ist_isstp = '10' and CONVERT(varchar,ist_issdt,101)>LP_STRDT and ist_stsfl='2'   group by lt_prdtp,lt_lotno,lt_rclno,lt_dspqt having sum(ist_issqt)<>isnull(lt_dspqt,0);

  declare continue handler for not found 
       set END_TABLE = 1; 

  set END_TABLE = 0;


if LP_QTYCD = 'BAGQT' then  
   open C_RCT ;
  fetch C_RCT into  L_PRDTP, L_LOTNO, L_RCLNO, L_BAGQT, L_RCTQT;
  while END_TABLE = 0 DO 
    update spldata.pr_ltmst set lt_bagqt=L_RCTQT where lt_cmpcd = LP_CMPCD and lt_prdtp=L_PRDTP and lt_lotno = L_LOTNO and lt_rclno = L_RCLNO and isnull(lt_bagqt,0) = L_BAGQT;
    set END_TABLE = 0;
    fetch C_RCT into  L_PRDTP, L_LOTNO, L_RCLNO, L_BAGQT, L_RCTQT;
  end while;
  close C_RCT;
end if;

if LP_QTYCD = 'DSPQT' then  
   open C_ISS ;
  fetch C_ISS into  L_PRDTP, L_LOTNO, L_RCLNO, L_DSPQT, L_ISSQT;
  while END_TABLE = 0 DO 
    update spldata.pr_ltmst set lt_dspqt=L_ISSQT where lt_cmpcd = LP_CMPCD and lt_prdtp=L_PRDTP and lt_lotno = L_LOTNO and lt_rclno = L_RCLNO and lt_dspqt = L_DSPQT;
    set END_TABLE = 0;
  fetch C_ISS into  L_PRDTP, L_LOTNO, L_RCLNO, L_DSPQT, L_ISSQT;
  end while;
  close C_ISS;


end if;

end;

commit;




call spldata.setLTMST_QT('01','BAGQT','01/01/2009');
commit;

call spldata.setLTMST_QT('01','DSPQT','01/01/2006');
commit;


select lt_prdtp,lt_lotno,lt_rclno,lt_bagqt,sum(isnull(rct_rctqt,0)) from spldata.pr_ltmst left outer join spldata.fg_rctrn on  lt_cmpcd = rct_cmpcd and lt_prdtp=rct_prdtp and lt_lotno = rct_lotno and lt_rclno = rct_rclno and rct_rcttp in ('10','15')  where lt_cmpcd = '11' and CONVERT(varchar,lt_pstdt,101)>'01/01/2009' and  lt_lotno not like '900%' group by lt_prdtp,lt_lotno,lt_rclno,lt_bagqt having isnull(sum(isnull(rct_rctqt,0)),0)<>isnull(lt_bagqt,0);



select lt_prdtp,lt_lotno,lt_rclno,isnull(lt_dspqt,0) lt_dspqt,sum(ist_issqt) from spldata.pr_ltmst,spldata.fg_istrn where lt_prdtp=ist_prdtp and lt_lotno = ist_lotno and lt_rclno = ist_rclno and ist_isstp = '10' and CONVERT(varchar,ist_issdt,101)>'01/01/2006' and ist_stsfl='2'   group by lt_prdtp,lt_lotno,lt_rclno,lt_dspqt having sum(ist_issqt)<>isnull(lt_dspqt,0);

