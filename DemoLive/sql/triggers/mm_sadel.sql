  
drop trigger spldata.mm_sadel;
commit;


CREATE TRIGGER SPLDATA.MM_SADEL 
	AFTER DELETE ON SPLDATA.MM_SAMST 
	REFERENCING OLD AS OLD_ROW 
	FOR EACH ROW 
	MODE DB2ROW 
	BEGIN 
	DECLARE L_CNT DECIMAL ( 1 , 0 ) DEFAULT 0 ; DECLARE L_MATTP CHAR ( 1 ) DEFAULT ' ' ; 
	DECLARE END_TABLE INT DEFAULT 0 ; 
	  
	  
	DECLARE CONTINUE HANDLER FOR NOT FOUND 
	SET SQLP_L2 . END_TABLE = 1 ; 
	  
	  
	  
	  
	UPDATE SPLDATA . MM_STMST SET  ST_TRNFL = '0' , ST_STKQT = QSYS2 . IFNULL ( SPLDATA . MM_STMST . ST_STKQT , 0 ) - OLD_ROW . SA_SANQT , ST_YTDSN = QSYS2 . IFNULL ( SPLDATA . MM_STMST . ST_YTDSN , 0 ) - QSYS2 . IFNULL ( OLD_ROW . SA_SANQT , 0 ) WHERE ST_CMPCD = OLD_ROW . SA_CMPCD AND SPLDATA . MM_STMST . ST_STRTP = OLD_ROW . SA_STRTP AND SPLDATA . MM_STMST . ST_MATCD = OLD_ROW . SA_MATCD ; 
	UPDATE SPLDATA . MM_STPRC SET STP_CSAQT = QSYS2 . IFNULL ( SPLDATA . MM_STPRC . STP_CSAQT , 0 ) - OLD_ROW . SA_SANQT , STP_CSAVL = QSYS2 . IFNULL ( SPLDATA . MM_STPRC . STP_CSAVL , 0 ) - QSYS2 . IFNULL ( OLD_ROW . SA_SANVL , 0 ) , 
	STP_YCSQT = QSYS2 . IFNULL ( SPLDATA . MM_STPRC . STP_YCSQT , 0 ) - OLD_ROW . SA_SANQT , STP_YCSVL = QSYS2 . ABS ( QSYS2 . IFNULL ( SPLDATA . MM_STPRC . STP_YCSVL , 0 ) - QSYS2 . IFNULL ( OLD_ROW . SA_SANVL , 0 ) ) 
	WHERE STP_CMPCD = OLD_ROW . SA_CMPCD AND SPLDATA . MM_STPRC . STP_STRTP = OLD_ROW . SA_STRTP AND SPLDATA . MM_STPRC . STP_MATCD = OLD_ROW . SA_MATCD ; 
	  
	UPDATE SPLDATA . MM_STPRC SET STP_YCLRT = QSYS2 . IFNULL ( SPLDATA . MM_STPRC . STP_YCSVL , 0 ) / QSYS2 . IFNULL ( SPLDATA . MM_STPRC . STP_YCSQT , 0 ) 
	WHERE STP_CMPCD = OLD_ROW . SA_CMPCD AND SPLDATA . MM_STPRC . STP_STRTP = OLD_ROW . SA_STRTP AND SPLDATA . MM_STPRC . STP_MATCD = OLD_ROW . SA_MATCD AND QSYS2 . IFNULL ( SPLDATA . MM_STPRC . STP_YCSQT , 0 ) > 0 ; 
	  
	END  ;
commit;



