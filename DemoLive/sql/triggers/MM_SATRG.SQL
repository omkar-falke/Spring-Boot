drop trigger SPLDATA.MM_SATRG;
commit;

create trigger SPLDATA.MM_SATRG after insert on SPLDATA.MM_SAMST referencing 
new as new_row for each row mode DB2ROW 
begin 
 declare L_CNT decimal(1,0) default 0;  declare L_MATTP char(1) default ' '; 
 declare END_TABLE int default 0; 
 
declare C2 cursor for select COUNT(*) L_COUNT FROM SPLDATA.MM_STMST
 where ST_CMPCD = new_row.SA_CMPCD and ST_STRTP = new_row.SA_STRTP and ST_MATCD = new_row.SA_MATCD;

 declare continue handler for not found 
 set END_TABLE = 1; 

SET L_CNT =0;

open C2;
  fetch C2 into L_CNT; 
close C2; 

 IF L_CNT =0 THEN
  	 if substr(new_row.SA_MATCD,1,2) = '69' THEN
		SET L_MATTP = '3';
	 end if;	
	 if substr(new_row.SA_MATCD,1,2) = '68' THEN
  		SET L_MATTP = '2';
	 else SET L_MATTP = '1';	
	 end if;	
	insert into SPLDATA.mm_stmst(st_cmpcd,st_mmsbs,st_strtp,st_matcd,st_matds,st_mattp,st_uomcd,st_loccd,st_yopst,st_yopvl,st_stsfl,st_trnfl,st_lusby,st_lupdt) 
	values(new_row.sa_cmpcd,new_row.sa_cmpcd||new_row.sa_strtp||'00',new_row.sa_strtp,new_row.sa_matcd,' ',L_MATTP,' ','XX',0,0,' ','0',new_row.sa_lusby,new_row.sa_lupdt);	
	update SPLDATA.MM_STMST A SET (ST_MATDS,ST_UOMCD,ST_PPONO,ST_PPOVL,ST_PGRNO,ST_PGRDT,ST_PISNO,ST_PISDT,ST_PMRNO,ST_PMRDT,ST_PSNNO,ST_PSNDT,ST_PSTNO,ST_PSTDT) = (SELECT B.CT_MATDS,B.CT_UOMCD,B.CT_PPONO,B.CT_PPORT,B.CT_PGRNO,CT_PGRDT,CT_PISNO,CT_PISDT,CT_PMRNO,CT_PMRDT,NEW_ROW.SA_SANNO,NEW_ROW.SA_SANDT,CT_PSTNO,CT_PSTDT FROM SPLDATA.CO_CTMST B WHERE A.ST_CMPCD = new_row.SA_CMPCD and A.ST_MATCD = B.CT_MATCD) WHERE ST_CMPCD = new_row.SA_CMPCD and ST_STRTP = NEW_ROW.SA_STRTP and a.st_matcd = new_row.SA_MATCD;

     insert into SPLDATA.MM_STPRC(stp_cmpcd,stp_mmsbs,stp_strtp,stp_matcd,stp_matds,stp_uomcd) 
     values(new_row.sa_cmpcd,new_row.sa_cmpcd||new_row.sa_strtp||'00',new_row.sa_strtp,new_row.sa_matcd,' ',' ');	
     update SPLDATA.MM_STPRC A SET (STP_MATDS,STP_UOMCD) = (SELECT B.CT_MATDS,B.CT_UOMCD FROM SPLDATA.CO_CTMST B WHERE A.STP_CMPCD = new_row.SA_CMPCD and A.STP_MATCD = B.CT_MATCD) WHERE STP_CMPCD = new_row.SA_CMPCD and STP_STRTP = NEW_ROW.SA_STRTP and a.stp_matcd = new_row.SA_MATCD;
  end if;
  
 set END_TABLE = 0; 

 UPDATE SPLDATA.CO_CTMST SET CT_TRNFL ='0',CT_PSNNO = NEW_ROW.SA_SANNO,CT_PSNDT = NEW_ROW.SA_SANDT where  CT_MATCD = new_row.SA_MATCD;
 UPDATE SPLDATA.MM_STMST SET ST_PSNNO = NEW_ROW.SA_SANNO,ST_PSNDT = NEW_ROW.SA_SANDT,ST_TRNFL ='0',ST_STKQT = IFNULL(ST_STKQT,0)+NEW_ROW.SA_SANQT,ST_YTDSN = IFNULL(ST_YTDSN,0) + IFNULL(NEW_ROW.SA_SANQT,0) where  ST_CMPCD = new_row.SA_CMPCD and ST_STRTP = new_row.SA_STRTP AND ST_MATCD = new_row.SA_MATCD;
 UPDATE SPLDATA.MM_STPRC SET STP_CSAQT = IFNULL(STP_CSAQT,0)+NEW_ROW.SA_SANQT,STP_CSAVL = IFNULL(STP_CSAVL,0) + IFNULL(NEW_ROW.SA_SANVL,0),
 STP_YCSQT = IFNULL(STP_YCSQT,0)+NEW_ROW.SA_SANQT,STP_YCSVL = abs(IFNULL(STP_YCSVL,0) + IFNULL(NEW_ROW.SA_SANVL,0))  
 where  STP_CMPCD = new_row.SA_CMPCD and STP_STRTP = new_row.SA_STRTP AND STP_MATCD = new_row.SA_MATCD;

 UPDATE SPLDATA.MM_STPRC SET STP_YCLRT = IFNULL(STP_YCSVL,0)/IFNULL(STP_YCSQT,0)
  where  STP_CMPCD = new_row.SA_CMPCD and STP_STRTP = new_row.SA_STRTP AND STP_MATCD = new_row.SA_MATCD AND IFNULL(STP_YCSQT,0) >0;

END;
commit;
