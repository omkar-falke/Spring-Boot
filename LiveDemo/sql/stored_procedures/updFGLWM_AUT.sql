
;
/*

CODCD          Receipt Type                       CHP01                                 CHP02                                CCSVL
                                                                (STKTP)
10	Fresh Production Receipt	1		1		0
21	Job Work Receipt	1		1		1
22	Job Work(Lot) Receipt	1		2		1
23	Outsourcing	mnlc	1		2		1
30	Sales Return Receipt	2		3		1
40	W.House Transfer Receipt	x		4		2
50	Stock Adjustmen Receipt	x		1		1
15	Repacking Receipt	1		0		0
16	Rebagging Receipt	x		4		1
51	ReClassification Receipt	x		4		2
61	Down Grading Receipt	x		4		2

Chp01 :   1  Fresh Production
              2  Sales Return material 
              x  As applicable (Taken from source  record)
              chp01 is stored as stock type in Stock Master, while converting the receipt record into stock record.

chp02  :   1  Entered through receipt entry program (FG_TERCT)
              0 Entered through receipt entry program with default entries
              3 Entered through Receipt entry program with Issue Reference
              4 Not entered through Receipt entry program (Accepted through other module) but stored in FG_RCTRN

ccsvl   :   0 Records accepted in upper table of entry screen
              1  Records accepted in lower table of entry screen
              2  Records of this receipt type are captured through other entry modules (Other than FG_TERCT)

*/



drop procedure spldata.updFGLWM_AUT;
commit;


create procedure spldata.updFGLWM_AUT(IN LP_CMPCD char(2),IN LP_WRHTP char(2),IN LP_RCTTP char(2), IN LP_RCTNO char(8), IN LP_AUTBY char(3))  language sql  modifies sql data 
P1:

  begin
 
  declare L_CMPCD varchar(2);
  declare L_PRDTP varchar(2);
  declare L_LOTNO varchar(8);
  declare L_ISSRF varchar(8) default '        ';
  declare L_RCLNO varchar(2);
  declare L_PKGTP varchar(2);
  declare L_MNLCD varchar(5);
  declare L_RCTDT date;
  declare L_SHFCD varchar(1);
  declare L_STSFL varchar(1);
  declare L_STKTP varchar(1);
  declare L_RCTQT decimal(10,3) default 0.000;
  declare L_RCTPK decimal(8) default 0;
  declare L_PKGWT decimal(8,3) default 0.000;
  declare L_PKGCT varchar(2);
  declare L_CHP01 varchar(1);
  declare L_CHP02 varchar(1);
  declare L_CCSVL varchar(1);

  declare L_RECCT int default 0;
  declare END_TABLE int default 0;

  declare C_RCT cursor for select LW_CMPCD,LW_PRDTP, LW_LOTNO, LW_RCLNO, LW_PKGTP, LW_MNLCD, LW_RCTDT,LW_SHFCD,LW_STSFL, sum(isnull(LW_BAGQT,0)) LW_BAGQT,sum(isnull(LW_BAGPK,0)) LW_BAGPK from spldata.fg_lwmst where LW_CMPCD = LP_CMPCD and LW_WRHTP = LP_WRHTP and LW_RCTTP = LP_RCTTP and LW_RCTNO = LP_RCTNO and isnull(LW_BAGQT,0)>0 and LW_STSFL = '1' group by LW_CMPCD,LW_PRDTP, LW_LOTNO, LW_RCLNO, LW_PKGTP, LW_MNLCD, LW_RCTDT,LW_SHFCD,LW_STSFL order by LW_CMPCD,LW_PRDTP, LW_LOTNO, LW_RCLNO, LW_PKGTP, LW_MNLCD;
  declare continue handler for not found 
       set END_TABLE = 1; 

  set END_TABLE = 0;
   open C_RCT ;
  fetch C_RCT into  L_CMPCD,L_PRDTP, L_LOTNO, L_RCLNO, L_PKGTP, L_MNLCD, L_RCTDT, L_SHFCD, L_STSFL, L_RCTQT, L_RCTPK;
  while END_TABLE = 0 DO 

           select cmt_ccsvl, cmt_ncsvl  into L_PKGCT,L_PKGWT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp = 'FGXXPKG' and cmt_codcd = L_PKGTP;
           select cmt_chp01, cmt_chp02, cmt_ccsvl  into L_CHP01, L_CHP02, L_CCSVL from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp = 'FGXXRTP' and cmt_codcd = LP_RCTTP;
       set L_STKTP = '';
       if L_CHP01 <> 'X' then            /* Stock type specified in Codes Transaction (cmt_chp01) is replaced in Stock Master (FG_STMST) */
          set L_STKTP = L_CHP01;
       end if;

       select count(*) into L_RECCT from spldata.fg_rctrn where rct_cmpcd = LP_CMPCD and  rct_wrhtp = LP_WRHTP and  rct_rcttp = LP_RCTTP and  rct_rctno = LP_RCTNO and  rct_prdtp = L_PRDTP and  rct_lotno = L_LOTNO and  rct_rclno = L_RCLNO and  rct_pkgtp = L_PKGTP and  rct_mnlcd = L_MNLCD;
       if L_RECCT =0 then 
          Insert into   spldata.FG_RCTRN (RCT_CMPCD,RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,RCT_RCTDT,RCT_LOTNO,RCT_RCLNO,RCT_MNLCD,RCT_ISSRF,RCT_STKTP,RCT_PKGTP,RCT_PKGCT,RCT_RCTQT,RCT_RCTPK,RCT_SHFCD,RCT_AUTBY,RCT_AUTDT,RCT_STSFL,RCT_TRNFL,RCT_LUSBY,RCT_LUPDT) values (LP_CMPCD,LP_WRHTP,LP_RCTTP,LP_RCTNO,L_PRDTP,L_RCTDT,L_LOTNO,L_RCLNO,L_MNLCD,L_ISSRF,L_STKTP,L_PKGTP,L_PKGCT, L_RCTQT,L_RCTPK,L_SHFCD,LP_AUTBY, current_date,L_STSFL,'0',LP_AUTBY,current_date);
       else
       update spldata.fg_rctrn set RCT_RCTQT = L_RCTQT, RCT_RCTPK = L_RCTPK, RCT_AUTBY = LP_AUTBY, RCT_AUTDT = current_date, RCT_LUSBY = LP_AUTBY, RCT_LUPDT = current_date where rct_cmpcd = LP_CMPCD and  rct_wrhtp = LP_WRHTP and  rct_rcttp = LP_RCTTP and  rct_rctno = LP_RCTNO and  rct_prdtp = L_PRDTP and  rct_lotno = L_LOTNO and  rct_rclno = L_RCLNO and  rct_pkgtp = L_PKGTP and  rct_mnlcd = L_MNLCD;
       end if;
    set END_TABLE = 0;
    fetch C_RCT into  L_CMPCD,L_PRDTP, L_LOTNO, L_RCLNO, L_PKGTP, L_MNLCD, L_RCTDT, L_SHFCD, L_STSFL, L_RCTQT, L_RCTPK;
  end while;
  close C_RCT;
  update spldata.fg_lwmst set lw_stsfl='2' where LW_CMPCD = LP_CMPCD and LW_WRHTP = LP_WRHTP and LW_RCTTP = LP_RCTTP and LW_RCTNO = LP_RCTNO and isnull(LW_BAGQT,0)>0 and LW_STSFL = '1';
  call spldata.updFGRCT_AUT(LP_CMPCD,LP_WRHTP, LP_RCTTP, LP_RCTNO, LP_AUTBY);

end;

commit;


call spldata.updFGLWM_AUT('01','01', '10', '91001795', 'SYS');
COMMIT;


