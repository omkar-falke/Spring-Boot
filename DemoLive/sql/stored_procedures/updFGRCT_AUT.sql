

/*

CODCD          Receipt Type                       CHP01                                 CHP02                                CCSVL
                                                                (STKTP)
10	Fresh Production Receipt	1		1		0
21	Job Work Receipt	1		1		1
22	Job Work(Lot) Receipt	1		2		1
23	Outsourcing		1		2		1
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





drop procedure spldata.updFGRCT_AUT;
commit;


create procedure spldata.updFGRCT_AUT(IN LP_CMPCD char(2),IN LP_WRHTP char(2),IN LP_RCTTP char(2), IN LP_RCTNO char(8), IN LP_AUTBY char(3))  language sql  modifies sql data 
P1:

  begin
 
  declare L_CMPCD varchar(2);
  declare L_PRDTP varchar(2);
  declare L_LOTNO varchar(8);
  declare L_RCLNO varchar(2);
  declare L_PKGTP varchar(2);
  declare L_MNLCD varchar(5);
  declare L_RCTDT date;
  declare L_SHFCD varchar(1);
  declare L_STSFL varchar(1);
  declare L_STKTP varchar(1);
  declare L_RCTQT decimal(10,3) default 0.000;
  declare L_STKQT decimal(10,3) default 0.000;
  declare L_UCLQT decimal(10,3) default 0.000;
  declare L_RCTPK decimal(8) default 0;
  declare L_PKGWT decimal(8,3) default 0.000;
  declare L_PKGCT varchar(2);
  declare L_CHP01 varchar(1);
  declare L_CHP02 varchar(1);
  declare L_CCSVL varchar(1);
  declare L_PRDCD varchar(10);
  declare L_TPRCD varchar(10);

  declare L_RECCT int default 0;
  declare END_TABLE int default 0;

  declare C_RCT cursor for select RCT_CMPCD,RCT_PRDTP, RCT_LOTNO, RCT_RCLNO, RCT_PKGTP, RCT_MNLCD, RCT_RCTDT,RCT_SHFCD,RCT_STSFL,RCT_STKTP, isnull(RCT_RCTQT,0) RCT_RCTQT,isnull(RCT_RCTPK,0) RCT_RCTPK from spldata.fg_rctrn where RCT_CMPCD = LP_CMPCD and RCT_WRHTP = LP_WRHTP and RCT_RCTTP = LP_RCTTP and RCT_RCTNO = LP_RCTNO and isnull(RCT_RCTQT,0)>0 and RCT_STSFL = '1'  order by RCT_PRDTP, RCT_LOTNO, RCT_RCLNO, RCT_PKGTP, RCT_MNLCD;
  declare continue handler for not found 
       set END_TABLE = 1; 

  set END_TABLE = 0;
   open C_RCT ;
  fetch C_RCT into  L_CMPCD,L_PRDTP, L_LOTNO, L_RCLNO, L_PKGTP, L_MNLCD, L_RCTDT, L_SHFCD, L_STSFL, L_STKTP, L_RCTQT, L_RCTPK;
  while END_TABLE = 0 DO 

           select cmt_ccsvl, cmt_ncsvl  into L_PKGCT,L_PKGWT from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp = 'FGXXPKG' and cmt_codcd = L_PKGTP;
           select cmt_chp01, cmt_chp02, cmt_ccsvl  into L_CHP01, L_CHP02, L_CCSVL from spldata.co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp = 'FGXXRTP' and cmt_codcd = LP_RCTTP;
           select LT_PRDCD,LT_TPRCD into L_PRDCD, L_TPRCD  from spldata.PR_LTMST where lt_cmpcd = L_CMPCD and lt_prdtp = L_PRDTP and lt_lotno = L_LOTNO and LT_RCLNO = L_RCLNO;

       set L_STKQT = 0.000;
       set L_UCLQT = L_RCTQT;
       if L_CHP01 = '2'  then               /* Sales Return -- No classification required.*/
           set L_STKQT = L_RCTQT;
           set L_UCLQT = 0.000;
       elseif (LP_RCTTP = '21' and L_PRDCD <>  '5211951680')  then  /* Unprocessed Jobwork material is equivalent to Sales Return -- No classification required.*/
           set L_STKQT = L_RCTQT;
           set L_UCLQT = 0.000;
       end if;


       if L_CHP01 <> 'X' then            /* Stock type specified in Codes Transaction (cmt_chp01) is replaced in Stock Master (FG_STMST) */
          set L_STKTP = L_CHP01;
       end if;
       select count(*) into L_RECCT from spldata.fg_stmst where st_cmpcd = LP_CMPCD and st_wrhtp = LP_WRHTP and st_prdtp = L_PRDTP  and  st_lotno = L_LOTNO  and  st_rclno = L_RCLNO  and  st_pkgtp = L_PKGTP  and  st_mnlcd = L_MNLCD;
    if L_RECCT = 0 then 
           Insert into  spldata.FG_STMST(ST_CMPCD,ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_MNLCD,ST_PKGTP,ST_STKQT,ST_UCLQT,ST_CPRCD,ST_STSFL,ST_ALOQT,ST_DOSQT,ST_DOUQT,ST_RCTDT,ST_PKGCT,ST_RESNO,ST_RESCD,ST_RESDT,ST_REXDT,ST_PKGWT,ST_TPRCD,ST_PRDCD,ST_REMDS,ST_RESFL,ST_LUSBY,ST_TRNFL,ST_LUPDT) values (LP_CMPCD,LP_WRHTP,L_PRDTP, L_LOTNO,L_RCLNO,L_MNLCD,L_PKGTP, L_STKQT, L_UCLQT, L_PRDCD, L_STKTP, 0, 0,0, L_RCTDT,L_PKGCT,' ', ' ', null,null, L_PKGWT, L_TPRCD, L_PRDCD, '', '0',LP_AUTBY, '0', current_date);

      else
           update spldata.FG_STMST set ST_STKQT = ST_STKQT +L_STKQT, ST_UCLQT = ST_UCLQT+L_UCLQT, ST_TRNFL = '0', ST_LUSBY = LP_AUTBY, ST_LUPDT = current_date  where st_cmpcd = LP_CMPCD and st_wrhtp = LP_WRHTP and st_prdtp = L_PRDTP  and  st_lotno = L_LOTNO  and  st_rclno = L_RCLNO  and  st_pkgtp = L_PKGTP  and  st_mnlcd = L_MNLCD;
      end if; 

     /* Only fresh production is recorded as bagged quantity against the lot*/
      if LP_RCTTP in ('10','15') then
           update spldata.PR_LTMST set LT_BAGQT = LT_BAGQT+L_RCTQT where LT_CMPCD = L_CMPCD and LT_PRDTP = L_PRDTP and LT_LOTNO = L_LOTNO and LT_RCLNO = L_RCLNO;
      end if;
      update spldata.FG_LCMST set LC_STKQT = LC_STKQT+L_RCTQT where LC_CMPCD = LP_CMPCD and LC_WRHTP = LP_WRHTP and LC_MNLCD = L_MNLCD;


    set END_TABLE = 0;
    fetch C_RCT into  L_CMPCD,L_PRDTP, L_LOTNO, L_RCLNO, L_PKGTP, L_MNLCD, L_RCTDT, L_SHFCD, L_STSFL,L_STKTP, L_RCTQT, L_RCTPK;
  end while;
  close C_RCT;
  update spldata.fg_rctrn set rct_stsfl='2' where RCT_CMPCD = LP_CMPCD and RCT_WRHTP = LP_WRHTP and RCT_RCTTP = LP_RCTTP and RCT_RCTNO = LP_RCTNO and isnull(RCT_RCTQT,0)>0 and RCT_STSFL = '1';
 
end;

commit;
