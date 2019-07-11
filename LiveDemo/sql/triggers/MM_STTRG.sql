drop trigger SPLDATA.MM_STTRG;
commit;

create trigger SPLDATA.MM_STTRG after update of STN_STSFL on SPLDATA.MM_STNTR referencing 
new as new_row old as old_row for each row mode DB2ROW 

begin 
 declare L_CNT decimal(1,0) default 0;  declare L_MATTP char(1) default ' '; 
 declare END_TABLE int default 0; 

declare C2 cursor for select COUNT(*) L_COUNT FROM SPLDATA.MM_STMST
 where ST_CMPCD = new_row.STN_CMPCD and ST_STRTP = new_row.STN_TOSTR and ST_MATCD = new_row.STN_TOMAT;

 declare continue handler for not found 
 set END_TABLE = 1; 

IF new_row.STN_STSFL = '2' THEN 

IF new_row.STN_STNTP != '03' THEN

SET L_CNT =0;

open C2;
  fetch C2 into L_CNT; 
close C2; 

 IF L_CNT =0 THEN
  	 if substr(new_row.STN_TOMAT,1,2) = '69' THEN
		SET L_MATTP = '3';
	 end if;	
	 if substr(new_row.STN_TOMAT,1,2) = '68' THEN
  		SET L_MATTP = '2';
	 else SET L_MATTP = '1';	
	 end if;	
	insert into SPLDATA.mm_stmst(st_cmpcd,st_mmsbs,st_strtp,st_matcd,st_matds,st_mattp,st_uomcd,st_loccd,st_yopst,st_yopvl,st_stsfl,st_trnfl,st_lusby,st_lupdt) 
	values(new_row.stn_cmpcd,new_row.stn_cmpcd||new_row.stn_tostr||'00',new_row.stn_tostr,new_row.stn_tomat,' ',L_MATTP,' ','XX',0,0,' ','0',new_row.stn_lusby,new_row.stn_lupdt);	
	update SPLDATA.MM_STMST A SET (ST_MATDS,ST_UOMCD,ST_PPONO,ST_PPOVL,ST_PGRNO,ST_PGRDT,ST_PISNO,ST_PISDT,ST_PMRNO,ST_PMRDT,ST_PSNNO,ST_PSNDT,ST_PSTNO,ST_PSTDT)
	 = (SELECT B.CT_MATDS,B.CT_UOMCD,B.CT_PPONO,B.CT_PPORT,B.CT_PGRNO,CT_PGRDT,CT_PISNO,CT_PISDT,CT_PMRNO,CT_PMRDT,CT_PSNNO,CT_PSNDT,NEW_ROW.STN_STNNO,NEW_ROW.STN_STNDT FROM SPLDATA.CO_CTMST B WHERE A.ST_CMPCD = new_row.STN_CMPCD and A.ST_MATCD = B.CT_MATCD)
    WHERE ST_CMPCD = new_row.STN_CMPCD and ST_STRTP = NEW_ROW.STN_TOSTR and a.st_matcd = new_row.STN_TOMAT;

     insert into SPLDATA.MM_STPRC(stp_cmpcd,stp_mmsbs,stp_strtp,stp_matcd,stp_matds,stp_uomcd) 
     values(new_row.stn_cmpcd,new_row.stn_cmpcd||new_row.stn_tostr||'00',new_row.stn_tostr,new_row.stn_tomat,' ',' ');	
     update SPLDATA.MM_STPRC A SET (STP_MATDS,STP_UOMCD) = (SELECT B.CT_MATDS,B.CT_UOMCD FROM SPLDATA.CO_CTMST B WHERE A.STP_CMPCD = new_row.STN_CMPCD and A.STP_MATCD = B.CT_MATCD) WHERE STP_CMPCD = new_row.STN_CMPCD and STP_STRTP = NEW_ROW.STN_TOSTR and a.stp_matcd = new_row.STN_TOMAT;
  end if;
  
 set END_TABLE = 0; 

end if;
 
 IF new_row.STN_STNTP != '03' THEN
 UPDATE SPLDATA.CO_CTMST SET CT_TRNFL ='0',CT_PSTNO = NEW_ROW.STN_STNNO,CT_PSTDT = NEW_ROW.STN_STNDT where  CT_MATCD = new_row.STN_TOMAT;
    UPDATE SPLDATA.MM_STMST SET ST_PSTNO = NEW_ROW.STN_STNNO,ST_PSTDT = NEW_ROW.STN_STNDT,ST_TRNFL ='0',ST_STKQT = IFNULL(ST_STKQT,0)+NEW_ROW.STN_TRNQT,ST_YTDST = IFNULL(ST_YTDST,0) + IFNULL(NEW_ROW.STN_TRNQT,0) where  ST_CMPCD = new_row.STN_CMPCD and ST_STRTP = new_row.STN_TOSTR AND ST_MATCD = new_row.STN_TOMAT;
    UPDATE SPLDATA.MM_STPRC SET STP_CSTQT = IFNULL(STP_CSTQT,0)+NEW_ROW.STN_TRNQT,STP_CSTVL = IFNULL(STP_CSTVL,0) + IFNULL(NEW_ROW.STN_TRNVL,0),
    STP_YCSQT = IFNULL(STP_YCSQT,0)+NEW_ROW.STN_TRNQT,STP_YCSVL = abs(IFNULL(STP_YCSVL,0) + IFNULL(NEW_ROW.STN_TRNVL,0))  
    where  STP_CMPCD = new_row.STN_CMPCD and STP_STRTP = new_row.STN_TOSTR AND STP_MATCD = new_row.STN_TOMAT;
 END IF;
 IF new_row.STN_STNTP != '02' THEN   
    UPDATE SPLDATA.CO_CTMST SET CT_TRNFL ='0',CT_PSTNO = NEW_ROW.STN_STNNO,CT_PSTDT = NEW_ROW.STN_STNDT where  CT_MATCD = new_row.STN_FRMAT;
    UPDATE SPLDATA.MM_STMST SET ST_PSTNO = NEW_ROW.STN_STNNO,ST_PSTDT = NEW_ROW.STN_STNDT,ST_TRNFL ='0',ST_STKQT = IFNULL(ST_STKQT,0)- NEW_ROW.STN_TRNQT,ST_YTDST = IFNULL(ST_YTDST,0) - IFNULL(NEW_ROW.STN_TRNQT,0) where  ST_CMPCD = new_row.STN_CMPCD and ST_STRTP = new_row.STN_FRSTR AND ST_MATCD = new_row.STN_FRMAT;
    UPDATE SPLDATA.MM_STPRC SET STP_CSTQT = IFNULL(STP_CSTQT,0)- NEW_ROW.STN_TRNQT,STP_CSTVL = IFNULL(STP_CSTVL,0) - IFNULL(NEW_ROW.STN_TRNVL,0),
    STP_YCSQT = IFNULL(STP_YCSQT,0) - NEW_ROW.STN_TRNQT,STP_YCSVL = abs(IFNULL(STP_YCSVL,0) - IFNULL(NEW_ROW.STN_TRNVL,0))  
    where  STP_CMPCD = new_row.STN_CMPCD and STP_STRTP = new_row.STN_FRSTR AND STP_MATCD = new_row.STN_FRMAT;

 END IF;
 
 UPDATE SPLDATA.MM_STPRC SET STP_YCLRT = IFNULL(STP_YCSVL,0)/IFNULL(STP_YCSQT,0)
  where  STP_CMPCD = new_row.STN_CMPCD and STP_STRTP = new_row.STN_TOSTR AND STP_MATCD = new_row.STN_TOMAT AND IFNULL(STP_YCSQT,0) >0;

UPDATE SPLDATA.MM_STPRC SET STP_YCLRT = IFNULL(STP_YCSVL,0)/IFNULL(STP_YCSQT,0)
  where  STP_CMPCD = new_row.STN_CMPCD and STP_STRTP = new_row.STN_FRSTR AND STP_MATCD = new_row.STN_FRMAT AND IFNULL(STP_YCSQT,0) >0;
   
END IF;    
END;
commit;
