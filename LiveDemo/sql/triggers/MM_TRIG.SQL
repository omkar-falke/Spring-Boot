drop trigger SPLDATA.MM_ISTRG;
commit;

create trigger SPLDATA.MM_ISTRG after update of IS_ISSQT on SPLDATA.MM_ISMST referencing 
new as new_row old as  old_row for each row mode DB2ROW 

begin 
 
 declare L_YOSQT DECIMAL(12,3)DEFAULT 0;
 declare L_CRCQT DECIMAL(12,3)DEFAULT 0;
 declare L_YOSVL DECIMAL(12,2)DEFAULT 0;
 declare L_CRCVL DECIMAL(12,2)DEFAULT 0;
 declare L_CISVL DECIMAL(12,2)DEFAULT 0;
 declare L_WAVRT DECIMAL(10,2)DEFAULT 0;
 declare L_STKQT DECIMAL(12,3)DEFAULT 0;
 declare L_CSAVL DECIMAL(12,2)DEFAULT 0;
 declare L_CMRVL DECIMAL(12,2)DEFAULT 0;
 declare L_CSAQT DECIMAL(12,3)DEFAULT 0;
 declare L_CMRQT DECIMAL(12,3)DEFAULT 0;
 declare L_YCSQT DECIMAL(12,3)DEFAULT 0;
 declare L_YCSVL DECIMAL(12,2)DEFAULT 0;
 declare L_YCMQT DECIMAL(12,3)DEFAULT 0;
 declare L_YCMVL DECIMAL(12,2)DEFAULT 0;
 declare L_ISSVL DECIMAL(12,2) DEFAULT 0;
 declare L_ISSRT DECIMAL(11,2) DEFAULT 0;
 declare L_YCLRT DECIMAL(12,2)DEFAULT 0;
 declare L_ISMQT DECIMAL(12,3)DEFAULT 0;
 declare L_ISMVL DECIMAL(12,2)DEFAULT 0;
 
 declare END_TABLE int default 0; 

 declare C1 cursor for select IFNULL(STP_YOSQT,0),IFNULL(STP_CRCQT,0),IFNULL(STP_YOSVL,0),
 IFNULL(STP_CRCVL,0),IFNULL(STP_CISVL,0),IFNULL(STP_WAVRT,0),IFNULL(ST_STKQT,0),
 IFNULL(STP_CSAVL,0),IFNULL(STP_CMRVL,0),IFNULL(STP_CSAQT,0),IFNULL(STP_CMRQT,0),
 IFNULL(STP_YCSQT,0),IFNULL(STP_YCSVL,0),IFNULL(STP_YCLRT,0),IFNULL(STP_YCMQT,0),
 IFNULL(STP_YCMVL,0) FROM  SPLDATA.MM_STPRC,SPLDATA.MM_STMST WHERE STP_CMPCD = ST_CMPCD and STP_STRTP = ST_STRTP 
 AND STP_MATCD = ST_MATCD AND STP_CMPCD = NEW_ROW.IS_CMPCD  AND STP_STRTP = NEW_ROW.IS_STRTP  AND STP_MATCD = NEW_ROW.IS_MATCD 
 AND IFNULL(NEW_ROW.IS_ISSQT,0) >0;

 declare continue handler for not found 
 set END_TABLE = 1; 

 IF (new_row.IS_STSFL = '2')  THEN  

	update SPLDATA.MM_STMST 
	set ST_TRNFL ='0', ST_STKQT =  ifnull(ST_STKQT,0) -((ifnull(new_row.IS_ISSQT,0) - ifnull(old_row.IS_ISSQT,0))),
	ST_YTDIS =  ifnull(ST_YTDIS,0) +  (ifnull(new_row.IS_ISSQT,0)  -  ifnull(old_row.IS_ISSQT,0))
	where ST_CMPCD = new_row.IS_CMPCD and ST_STRTP = new_row.IS_STRTP and ST_MATCD = new_row.IS_MATCD;

	update SPLDATA.MM_STMST 
	set ST_PISNO =  new_row.IS_ISSNO,ST_PISDT = NEW_ROW.IS_ISSDT 
	where ST_CMPCD = new_row.IS_CMPCD and ST_STRTP = new_row.IS_STRTP and ST_MATCD = new_row.IS_MATCD and ifnull(new_row.IS_ISSQT,0) >0;

	update SPLDATA.MM_STMST 
	set ST_STODT =  current_date where ST_CMPCD = new_row.IS_CMPCD and ST_STRTP = new_row.IS_STRTP and ST_MATCD = new_row.IS_MATCD 
	and ifnull(new_row.IS_ISSQT,0) >0 and ifnull(st_stkqt,0) =0 ;
	
	update SPLDATA.CO_CTMST SET CT_TRNFL ='0',CT_PISNO = NEW_ROW.IS_ISSNO,CT_PISDT = NEW_ROW.IS_ISSDT
	where  CT_MATCD = new_row.IS_MATCD AND IFNULL(NEW_ROW.IS_ISSQT,0) >0;
 
	SET L_YOSQT = 0;
	SET L_CRCQT = 0;
	SET L_YOSVL = 0;
	SET L_CRCVL = 0; 
	SET L_CISVL = 0;
	SET L_WAVRT = 0;
	SET L_STKQT = 0;
	SET L_CSAVL = 0;
	SET L_CMRVL = 0;
	SET L_CSAQT = 0;
	SET L_CMRQT = 0;
	SET L_YCSQT = 0;
	SET L_YCSVL = 0;
	SET L_YCLRT = 0;
	SET L_YCMQT = 0;
	SET L_YCMVL = 0;
 
 open C1;

 fetch C1 into L_YOSQT,L_CRCQT,L_YOSVL,L_CRCVL,L_CISVL,L_WAVRT,L_STKQT,L_CSAVL,L_CMRVL,L_CSAQT,L_CMRQT,L_YCSQT,L_YCSVL,L_YCLRT,L_YCMQT,L_YCMVL;
 
 SET L_YCSQT = ifnull(L_YCSQT,0)  - (IFNULL(NEW_ROW.IS_ISSQT,0)-IFNULL(OLD_ROW.IS_ISSQT,0));
 SET L_ISSVL = (ifnull(L_YOSVL,0)+ifnull(L_CRCVL,0)+ifnull(L_CSAVL,0)) -(ifnull(L_YCLRT,0) * ifnull(L_YCSQT,0))-(ifnull(L_CISVL,0)-ifnull(L_CMRVL,0)) ;
if(ifnull(L_ISSVL,0) <0) then
  set L_ISSVL = 0;
end if;
 SET L_ISSRT = ifnull(L_ISSVL,0)/IFNULL(NEW_ROW.IS_ISSQT,0.01);
  IF (ifnull(L_ISSVL,0)  <= 0.009)  THEN  
	SET L_ISSVL =0.01;
  
  END IF;
  
  IF (L_ISSRT <= 0.009)  THEN  
	SET L_ISSRT = 0.01;
  END IF;
    
  IF (ifnull(L_YCMQT,0) >0) THEN
    IF (ifnull(L_YCMQT,0) >IFNULL(NEW_ROW.IS_ISSQT,0)) THEN
		SET L_ISMQT = IFNULL(NEW_ROW.IS_ISSQT,0);
	ELSE
		SET L_ISMQT = ifnull(L_YCMQT,0);
	END IF	;
	SET L_ISMVL = ifnull(L_ISMQT,0) *(ifnull(L_YCMVL,0)/ifnull(L_YCMQT,1));
	SET L_YCMQT = ifnull(L_YCMQT,0) - ifnull(L_ISMQT,0);
	SET L_YCMVL = ifnull(L_YCMVL,0) - ifnull(L_ISMVL,0);
	
   END IF;	
	
  close C1; 
  UPDATE SPLDATA.MM_STPRC SET STP_CISVL = IFNULL(STP_CISVL,0) + ifnull(L_ISSVL,0),STP_CISQT = IFNULL(STP_CISQT,0) + (IFNULL(NEW_ROW.IS_ISSQT,0) - IFNULL(OLD_ROW.IS_ISSQT,0)), 
  STP_YCSQT =  ifnull(STP_YCSQT,0) -((ifnull(new_row.IS_ISSQT,0) - ifnull(old_row.IS_ISSQT,0))),
  STP_YCSVL = IFNULL(STP_YCSVL,0) - ifnull(L_ISSVL,0), 
  STP_YCMQT = IFNULL(STP_YCMQT,0) - ifnull(L_ISMQT,0) ,
  STP_YCMVL = IFNULL(STP_YCMVL,0) - ifnull(L_ISMVL,0), 
  STP_CMIQT = IFNULL(STP_YCMQT,0) + ifnull(L_ISMQT,0) ,
  STP_CMIVL = IFNULL(STP_YCMVL,0) + ifnull(L_ISMVL,0) 
  WHERE STP_CMPCD =   NEW_ROW.IS_CMPCD and  STP_STRTP =   NEW_ROW.IS_STRTP AND  
  STP_MATCD = NEW_ROW.IS_MATCD ;
  
  UPDATE SPLDATA.MM_ISMST SET IS_ISSRT = ifnull(L_ISSRT,0),IS_ISSVL = ifnull(L_ISSVL,0),IS_MDVQT = ifnull(L_ISMQT,0),IS_MDVVL = ifnull(L_ISMVL,0) WHERE IS_CMPCD = NEW_ROW.IS_CMPCD AND IS_STRTP = NEW_ROW.IS_STRTP AND IS_ISSNO = NEW_ROW.IS_ISSNO AND 
  IS_MATCD = NEW_ROW.IS_MATCD AND IS_TAGNO = NEW_ROW.IS_TAGNO AND IS_BATNO = NEW_ROW.IS_BATNO and IS_GRNNO = NEW_ROW.IS_GRNNO;
  
 END IF;
 
end;
commit;

drop trigger SPLDATA.MM_INTRG;
commit;

create trigger SPLDATA.MM_INTRG after update of IN_AUTQT on SPLDATA.MM_INMST referencing 
new as new_row old as old_row for each row mode DB2ROW 

 begin 
 declare L_CNT decimal(1,0) default 0; 
 declare L_MATTP char(1); 
 
 declare END_TABLE int default 0; 
 
 declare C1 cursor for select COUNT(*) L_COUNT FROM SPLDATA.MM_STMST
 where ST_CMPCD = new_row.IN_CMPCD and ST_STRTP = new_row.IN_STRTP and ST_MATCD = new_row.IN_MATCD;

 declare continue handler for not found 
 set END_TABLE = 1; 

 
	open C1;
	while END_TABLE = 0 DO 

	 fetch C1 into L_CNT; 
	 if L_CNT = 0 and new_row.IN_STSFL ='4' then
	 	 if substr(new_row.IN_MATCD,1,2) = '69' THEN
			SET L_MATTP = '3';
		 end if;	
		 if substr(new_row.IN_MATCD,1,2) = '68' THEN
	 		SET L_MATTP = '2';
		 else SET L_MATTP = '1';	
		 end if;	
		 insert into SPLDATA.mm_stmst(st_cmpcd,st_mmsbs,st_strtp,st_matcd,st_matds,st_mattp,st_uomcd,st_loccd,st_yopst,st_yopvl,st_stsfl,st_trnfl,st_lusby,st_lupdt) values(new_row.in_cmpcd,new_row.in_cmpcd||new_row.in_strtp||'00',new_row.in_strtp,new_row.in_matcd,' ',L_MATTP,' ','XX',0,0,' ','0',new_row.in_lusby,new_row.in_lupdt);	
		 update SPLDATA.MM_STMST A SET (ST_MATDS,ST_UOMCD,ST_PPONO,ST_PPOVL,ST_PGRNO,ST_PGRDT,ST_PISNO,ST_PISDT,ST_PMRNO,ST_PMRDT,ST_PSNNO,ST_PSNDT,ST_PSTNO,ST_PSTDT,ST_STKIN) = (SELECT B.CT_MATDS,B.CT_UOMCD,B.CT_PPONO,B.CT_PPORT,B.CT_PGRNO,CT_PGRDT,CT_PISNO,CT_PISDT,CT_PMRNO,CT_PMRDT,CT_PSNNO,CT_PSNDT,CT_PSTNO,CT_PSTDT ,IFNULL(OLD_ROW.IN_AUTQT,0) FROM SPLDATA.CO_CTMST B WHERE A.ST_CMPCD = new_row.IN_CMPCD and A.ST_MATCD = B.CT_MATCD) WHERE ST_CMPCD = NEW_ROW.IN_CMPCD and ST_STRTP = NEW_ROW.IN_STRTP and a.st_matcd = new_row.in_matcd;

	    insert into SPLDATA.MM_STPRC(stp_cmpcd,stp_mmsbs,stp_strtp,stp_matcd,stp_matds,stp_uomcd) values(new_row.in_cmpcd,new_row.in_cmpcd||new_row.in_strtp||'00',new_row.in_strtp,new_row.in_matcd,' ',' ');	
	    update SPLDATA.MM_STPRC A SET (STP_MATDS,STP_UOMCD,STP_OWNBY) = (SELECT B.CT_MATDS,B.CT_UOMCD,B.CT_OWNBY FROM SPLDATA.CO_CTMST B WHERE A.STP_CMPCD = new_row.IN_CMPCD and A.STP_MATCD = B.CT_MATCD) WHERE STP_CMPCD = new_row.IN_CMPCD and STP_STRTP = NEW_ROW.IN_STRTP and a.stp_matcd = new_row.in_matcd;
	  end if;
	 fetch C1 into L_CNT; 
	 end while; 
	 close C1; 
if new_row.IN_INDTP ='01' then
	update SPLDATA.MM_STMST 
	set ST_TRNFL ='0', ST_STKIN =  ifnull(ST_STKIN,0) +  (ifnull(new_row.IN_AUTQT,0)  -  ifnull(old_row.IN_AUTQT,0)) 
	where ST_CMPCD = new_row.IN_CMPCD and ST_STRTP = new_row.IN_STRTP and ST_MATCD = new_row.IN_MATCD;
end if; 
end;

COMMIT;


