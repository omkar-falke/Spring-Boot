drop trigger SPLDATA.rctGRN;
commit;
create trigger SPLDATA.rctGRN after update of GR_RECQT on SPLDATA.MM_GRMST referencing 
new as new_row old as old_row for each row mode DB2ROW 
 begin 
 declare END_TABLE int default 0; 
  declare continue handler for not found 
 set END_TABLE = 1; 
IF(new_row.GR_STSFL = '0') THEN
 update SPLDATA.MM_STMST 
 set ST_TRNFL ='0', ST_STKIP =  ifnull(ST_STKIP,0) +  (ifnull(new_row.GR_RECQT,0)  -  ifnull(old_row.GR_RECQT,0)) 
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD;
END IF;
end;
drop trigger SPLDATA.insGRN;
commit;
create trigger SPLDATA.insGRN after insert on SPLDATA.MM_GRMST referencing 
new as new_row for each row mode DB2ROW 
 begin 
 declare L_CNT decimal(1,0) default 0; 
 declare L_MATTP char(1); 
 declare END_TABLE int default 0; 

declare C1 cursor for select COUNT(*) L_COUNT FROM SPLDATA.MM_STMST
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD;

 declare continue handler for not found 
 set END_TABLE = 1; 

 	open C1;
	while END_TABLE = 0 DO 

	 fetch C1 into L_CNT; 
	 if L_CNT = 0 then
	 	 if substr(new_row.GR_MATCD,1,2) = '69' THEN
			SET L_MATTP = '3';
		 end if;	
		 if substr(new_row.GR_MATCD,1,2) = '68' THEN
	 		SET L_MATTP = '2';
		 else SET L_MATTP = '1';	
		 end if;	
		 insert into SPLDATA.mm_stmst(st_cmpcd,st_mmsbs,st_strtp,st_matcd,st_matds,st_mattp,st_uomcd,st_loccd,st_yopst,st_yopvl,st_stsfl,st_trnfl,st_lusby,st_lupdt) values(new_row.GR_cmpcd,new_row.GR_cmpcd||new_row.GR_strtp||'00',new_row.GR_strtp,new_row.GR_matcd,' ',L_MATTP,' ','XX',0,0,' ','0',new_row.GR_lusby,new_row.GR_lupdt);	
		 update SPLDATA.MM_STMST A SET (ST_MATDS,ST_UOMCD,ST_PPONO,ST_PPOVL,ST_PGRNO,ST_PGRDT,ST_PISNO,ST_PISDT,ST_PMRNO,ST_PMRDT,ST_PSNNO,ST_PSNDT,ST_PSTNO,ST_PSTDT) = (SELECT B.CT_MATDS,B.CT_UOMCD,B.CT_PPONO,B.CT_PPORT,B.CT_PGRNO,CT_PGRDT,CT_PISNO,CT_PISDT,CT_PMRNO,CT_PMRDT,CT_PSNNO,CT_PSNDT,CT_PSTNO,CT_PSTDT FROM SPLDATA.CO_CTMST B WHERE A.ST_CMPCD = new_row.gr_cmpcd and A.ST_MATCD = B.CT_MATCD) WHERE ST_CMPCD = NEW_ROW.gr_CMPCD and ST_STRTP = NEW_ROW.gr_STRTP and a.st_matcd = new_row.GR_matcd;

	    insert into SPLDATA.MM_STPRC(stp_cmpcd,stp_mmsbs,stp_strtp,stp_matcd,stp_matds,stp_uomcd) values(new_row.GR_cmpcd,new_row.GR_cmpcd||new_row.GR_strtp||'00',new_row.gr_strtp,new_row.gr_matcd,' ',' ');	
	    update SPLDATA.MM_STPRC A SET (STP_MATDS,STP_UOMCD,STP_OWNBY) = (SELECT B.CT_MATDS,B.CT_UOMCD,B.CT_OWNBY FROM SPLDATA.CO_CTMST B WHERE A.STP_CMPCD = new_row.gr_cmpcd and A.STP_MATCD =  B.CT_MATCD) WHERE STP_CMPCD = NEW_ROW.gr_CMPCD and STP_STRTP = NEW_ROW.gr_STRTP and a.stp_matcd = new_row.gr_matcd;
	  end if;
	 fetch C1 into L_CNT; 
	 end while; 
	 close C1; 

IF (new_row.GR_STSFL = '0') THEN
 update SPLDATA.MM_STMST 
 set ST_TRNFL ='0', ST_STKIP =  ifnull(ST_STKIP,0) +  ifnull(new_row.GR_RECQT,0)
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD;
  
END IF;
end;
COMMIT;
drop trigger SPLDATA.delGRN;
commit;
create trigger SPLDATA.delGRN after update of GR_STSFL on SPLDATA.MM_GRMST referencing 
new as new_row for each row mode DB2ROW 
 begin 
 
 declare END_TABLE int default 0; 
 
  declare continue handler for not found 
 set END_TABLE = 1; 
IF(new_row.GR_STSFL = 'X') THEN
 update SPLDATA.MM_STMST 
 set ST_TRNFL ='0', ST_STKIP =  ifnull(ST_STKIP,0) -  ifnull(new_row.GR_RECQT,0)
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD;
  
END IF;
end;
COMMIT;
drop trigger SPLDATA.acpGRN;
commit;
create trigger SPLDATA.acpGRN after update of GR_ACPQT on SPLDATA.MM_GRMST referencing 
new as new_row old as old_row for each row mode DB2ROW 
begin 
 declare END_TABLE int default 0; 
 declare continue handler for not found 
 set END_TABLE = 1; 
IF (ifnull(new_row.GR_STSFL,'') = '2') THEN
 update SPLDATA.MM_STMST 
 set ST_TRNFL ='0',ST_STKIP =  ifnull(ST_STKIP,0) -((ifnull(new_row.GR_ACPQT,0)  -  ifnull(old_row.GR_ACPQT,0))+(ifnull(new_row.GR_REJQT,0)  -  ifnull(old_row.GR_REJQT,0))),
 ST_STKOR =  ifnull(ST_STKOR,0) -  (ifnull(new_row.GR_ACPQT,0)  -  ifnull(old_row.GR_ACPQT,0)), 
 ST_STKQT =  ifnull(ST_STKQT,0) +  (ifnull(new_row.GR_ACPQT,0)  -  ifnull(old_row.GR_ACPQT,0)), 
 ST_YTDGR =  ifnull(ST_YTDGR,0) +  (ifnull(new_row.GR_ACPQT,0)  -  ifnull(old_row.GR_ACPQT,0))
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD AND ifnull(new_row.GR_AMDNO,'') = '00';
 update SPLDATA.MM_STMST 
 set ST_PGRNO =  new_row.GR_GRNNO,ST_PGRDT = NEW_ROW.GR_ACPDT 
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD AND ifnull(new_row.GR_AMDNO,'') = '00' AND IFNULL(NEW_ROW.GR_ACPQT,0) >0;
 
 update SPLDATA.MM_STMST 
 set ST_PISDT = NEW_ROW.GR_ACPDT 
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD AND ifnull(new_row.GR_AMDNO,'') = '00' AND IFNULL(NEW_ROW.GR_ACPQT,0) >0 and ST_PISDT IS NULL;
 
 /* aDDED ON 17/08/2005 to update previous issue date with GRIN acceptance date , if pisdt is null */
 update SPLDATA.MM_STMST set ST_PISDT = NEW_ROW.GR_ACPDT where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP
  and ST_MATCD = new_row.GR_MATCD AND ifnull(new_row.GR_AMDNO,'') = '00'
 AND IFNULL(NEW_ROW.GR_ACPQT,0) >0 and ST_PISDT IS NULL;
  
 UPDATE SPLDATA.CO_CTMST SET CT_TRNFL ='0',CT_PGRNO = NEW_ROW.GR_GRNNO,CT_PGRDT = NEW_ROW.GR_ACPDT
 where  CT_MATCD = new_row.GR_MATCD AND IFNULL(NEW_ROW.GR_ACPQT,0) >0;
 update SPLDATA.MM_STMST 
 set ST_TRNFL ='0', ST_STKQT =  ifnull(ST_STKQT,0) +  (ifnull(new_row.GR_ACPQT,0)  -  ifnull(old_row.GR_ACPQT,0)), 
 ST_STKOR =  ifnull(ST_STKOR,0) -  (ifnull(new_row.GR_ACPQT,0)  -  ifnull(old_row.GR_ACPQT,0)), 
 ST_YTDGR =  ifnull(ST_YTDGR,0) +  (ifnull(new_row.GR_ACPQT,0)  -  ifnull(old_row.GR_ACPQT,0))  
 where ST_CMPCD = new_row.GR_CMPCD and ST_STRTP = new_row.GR_STRTP and ST_MATCD = new_row.GR_MATCD AND ifnull(new_row.GR_AMDNO,'') <> '00';
/* ADDED ON 01/03/2005 to adjust the cum. receipt value in case of GRIN AMENDMENT*/
/* commented part changed on 03/07/07 for batch/lot no
UPDATE SPLDATA.MM_STPRC SET STP_CRCVL =  IFNULL(STP_CRCVL,0)-(SELECT GR_PORVL - IFNULL(GR_VATVL,0) FROM SPLDATA.MM_GRMST WHERE GR_CMPCD = STP_CMPCD AND GR_STRTP = STP_STRTP AND GR_GRNNO =NEW_ROW.GR_GRNNO AND GR_MATCD = STP_MATCD) 
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD and ifnull(new_row.gr_AMDNO,'00') <>'00';

UPDATE SPLDATA.MM_STPRC SET STP_YCSVL =  IFNULL(STP_YCSVL,0)-(SELECT GR_PORVL FROM SPLDATA.MM_GRMST WHERE GR_CMPCD = STP_CMPCD AND GR_STRTP = STP_STRTP AND GR_GRNNO =NEW_ROW.GR_GRNNO AND GR_MATCD = STP_MATCD) 
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD and ifnull(new_row.GR_AMDNO,'00') <>'00';*/

UPDATE SPLDATA.MM_STPRC SET STP_CRCVL =  IFNULL(STP_CRCVL,0) - (NEW_ROW.GR_PORVL - IFNULL(NEW_ROW.GR_VATVL,0)) 
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD and ifnull(new_row.gr_AMDNO,'00') <>'00';

UPDATE SPLDATA.MM_STPRC SET STP_YCSVL =  IFNULL(STP_YCSVL,0) - (NEW_ROW.GR_PORVL - IFNULL(NEW_ROW.GR_VATVL,0))
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD and ifnull(new_row.GR_AMDNO,'00') <>'00';


UPDATE SPLDATA.MM_GRMST SET (GR_PORVL,GR_EXCVL,GR_VATVL) = (SELECT  FLOAT(SUM(IFNULL(PO_ITVAL,0))) / FLOAT(SUM(IFNULL(PO_PORQT,0)/FLOAT(po_ucnvl))),
FLOAT(SUM(IFNULL(PO_EXVAL,0))) / FLOAT(SUM(IFNULL(PO_PORQT,0)/FLOAT(po_ucnvl))),FLOAT(SUM(IFNULL(PO_VATVL,0))) / FLOAT(SUM(IFNULL(PO_PORQT,0)/FLOAT(po_ucnvl)))
FROM SPLDATA.MM_POMST WHERE PO_CMPCD = new_row.GR_CMPCD and PO_STRTP = NEW_ROW.GR_STRTP AND PO_PORNO = NEW_ROW.GR_PORNO AND PO_MATCD = NEW_ROW.GR_matcd AND IFNULL(PO_PORQT,0) >0 AND IFNULL(PO_STSFL,'') <>'X' GROUP BY PO_STRTP,PO_PORNO, PO_MATCD) WHERE GR_CMPCD = new_row.GR_CMPCD and GR_STRTP =NEW_ROW.GR_STRTP AND GR_GRNNO =NEW_ROW.GR_GRNNO AND GR_MATCD =NEW_ROW.GR_MATCD;

UPDATE SPLDATA.MM_GRMST SET GR_PORVL = IFNULL(GR_PORVL,0)*IFNULL(NEW_ROW.GR_ACPQT,0),GR_EXCVL =IFNULL(GR_EXCVL,0)*IFNULL(NEW_ROW.GR_ACPQT,0),GR_VATVL =IFNULL(GR_VATVL,0)*IFNULL(NEW_ROW.GR_ACPQT,0) 
WHERE GR_CMPCD = new_row.GR_CMPCD and GR_STRTP = NEW_ROW.GR_STRTP AND GR_GRNNO = NEW_ROW.GR_GRNNO AND GR_MATCD = NEW_ROW.GR_MATCD AND IFNULL(GR_STSFL,'') <>'X'; 

UPDATE SPLDATA.MM_GRMST SET GR_GRNRT = IFNULL(GR_PORVL,0)/IFNULL(GR_ACPQT,0) WHERE GR_CMPCD = new_row.GR_CMPCD and GR_STRTP = NEW_ROW.GR_STRTP AND GR_GRNNO = NEW_ROW.GR_GRNNO AND GR_MATCD = NEW_ROW.GR_MATCD AND IFNULL(GR_STSFL,'') <>'X' AND IFNULL(GR_ACPQT,0) >0; 

UPDATE SPLDATA.MM_STPRC SET STP_CRCQT = ifnull(STP_CRCQT,0) +(ifnull(NEW_ROW.GR_ACPQT,0)- ifnull(OLD_ROW.GR_ACPQT,0))
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;

UPDATE SPLDATA.MM_STPRC SET STP_CRCVL =  IFNULL(STP_CRCVL,0)+ (NEW_ROW.GR_PORVL - IFNULL(NEW_ROW.GR_VATVL,0))
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;

UPDATE SPLDATA.MM_STPRC SET STP_YCSQT = ifnull(STP_YCSQT,0) +(ifnull(NEW_ROW.GR_ACPQT,0)- ifnull(OLD_ROW.GR_ACPQT,0))
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;

UPDATE SPLDATA.MM_STPRC SET STP_YCSVL =  IFNULL(STP_YCSVL,0)+ (NEW_ROW.GR_PORVL - IFNULL(NEW_ROW.GR_VATVL,0))
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;

UPDATE SPLDATA.MM_STPRC SET STP_YCLRT = IFNULL(STP_YCSVL,0)/IFNULL(STP_YCSQT,0)
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD AND IFNULL(STP_YCSQT,0) > 0;

UPDATE SPLDATA.MM_STPRC SET STP_WAVRT = IFNULL(STP_CRCVL,0)/IFNULL(STP_CRCQT,0)
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD AND IFNULL(STP_CRCQT,0) >0;
IF (IFNULL(NEW_ROW.GR_MODFL,'N') ='Y') THEN

UPDATE SPLDATA.MM_STPRC SET STP_CMDQT = ifnull(STP_CMDQT,0) +(ifnull(NEW_ROW.GR_ACPQT,0)- ifnull(OLD_ROW.GR_ACPQT,0)),
STP_YCMQT = ifnull(STP_YCMQT,0) +(ifnull(NEW_ROW.GR_ACPQT,0)- ifnull(OLD_ROW.GR_ACPQT,0)) 
where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;

END IF;
END IF;
end;
commit;
drop trigger SPLDATA.updMDV;
commit;
create trigger SPLDATA.updMDV after update of GR_MODFL on SPLDATA.MM_GRMST referencing 
new as new_row old as old_row for each row mode DB2ROW 
 begin 
 
 declare END_TABLE int default 0; 
 declare continue handler for not found 
 set END_TABLE = 1; 
IF (IFNULL(OLD_row.GR_MODFL,'N') = 'N') and (IFNULL(new_row.GR_MODFL,'N') = 'Y') THEN
	UPDATE SPLDATA.MM_STPRC SET STP_CMDQT = ifnull(STP_CMDQT,0) +ifnull(NEW_ROW.GR_ACPQT,0),STP_CMDVL = STP_CMDVL+ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0),
	STP_YCMQT = ifnull(STP_YCMQT,0) +ifnull(NEW_ROW.GR_ACPQT,0),STP_YCMVL = STP_YCMVL+ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0)  
	,STP_CRCVL = ifnull(STP_CRCVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0))
	where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
	UPDATE SPLDATA.MM_STPRC SET STP_YCSVL = ifnull(STP_YCSVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0))
	where ifnull(STP_YCSVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0)) > 0 
	and STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
	UPDATE SPLDATA.MM_STPRC SET STP_CISVL = ifnull(STP_CISVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0))
	where ifnull(STP_YCSVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0)) < 0
	and STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
else 
IF (IFNULL(OLD_row.GR_MODFL,'N') = 'Y') and (IFNULL(new_row.GR_MODFL,'N') = 'N') THEN
	UPDATE SPLDATA.MM_STPRC SET STP_CMDQT = ifnull(STP_CMDQT,0) -ifnull(NEW_ROW.GR_ACPQT,0),STP_CMDVL = STP_CMDVL - ifnull(old_ROW.GR_MODVL,0),
	STP_YCMQT = ifnull(STP_YCMQT,0) -ifnull(NEW_ROW.GR_ACPQT,0),STP_YCMVL = STP_YCMVL - ifnull(old_ROW.GR_MODVL,0)  
	,STP_CRCVL = ifnull(STP_CRCVL,0) + ifnull(OLD_ROW.GR_MODVL,0)
	where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
	UPDATE SPLDATA.MM_STPRC SET STP_YCSVL = ifnull(STP_YCSVL,0) + ifnull(OLD_ROW.GR_MODVL,0)
	where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD and ifnull(STP_YCSVL,0)>0;
	
	UPDATE SPLDATA.MM_STPRC SET STP_CISVL = ifnull(STP_CISVL,0) + ifnull(OLD_ROW.GR_MODVL,0)
	where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD and ifnull(STP_YCSVL,0)=0;
	
else 
 IF (IFNULL(OLD_row.GR_MODFL,'N') = 'Y')and (IFNULL(new_row.GR_MODFL,'N') = 'Y') THEN
	UPDATE SPLDATA.MM_STPRC SET STP_CMDVL = IFNULL(STP_CMDVL,0)+ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0),
	STP_YCMVL = IFNULL(STP_YCMVL,0)+ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0)  
	,STP_CRCVL = ifnull(STP_CRCVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0))
	where STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;

	UPDATE SPLDATA.MM_STPRC SET STP_YCSVL = ifnull(STP_YCSVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0))
	where ifnull(STP_YCSVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0)) > 0
	and STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
	UPDATE SPLDATA.MM_STPRC SET STP_CISVL = ifnull(STP_CISVL,0) + (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0))
	where ifnull(STP_YCSVL,0) - (ifnull(NEW_ROW.GR_MODVL,0)-ifnull(OLD_ROW.GR_MODVL,0)) < 0
	and STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
 END IF;
END IF;
END IF; 
	update SPLDATA.MM_STPRC set STP_YCLRT = ifnull(STP_YCSVL,0) / ifnull(STP_YCSQT,0)
	where ifnull(STP_YCSQT,0) >0
	and STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
	update SPLDATA.MM_STPRC set STP_WAVRT = ifnull(STP_CRCVL,0) / ifnull(STP_CRCQT,0)
	where ifnull(STP_CRCQT,0) >0
	and STP_CMPCD = new_row.GR_CMPCD and STP_STRTP = new_row.GR_STRTP and STP_MATCD = new_row.GR_MATCD ;
	
end;
COMMIT;
/*DIVIDE BY ZERO : SQLCODE -802 SQLSTATE 22012
*/
/*RESULT OF SELECT MORE THAN ONE ROW  : SQLCODE -811 SQLSTATE 
*/
