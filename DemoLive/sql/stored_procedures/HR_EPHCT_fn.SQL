drop procedure SPLDATA.hr_ephct_fn;
commit;

create procedure SPLDATA.hr_ephct_fn() language sql modifies sql data
P2:
begin
declare L_EMPNO char(5);
declare L_INOCD char(1);
declare L_INOST CHAR(1);
declare L_PNCTM  TIMESTAMP;
declare END_TABLE int default 0; 

declare C_ISS CURSOR FOR SELECT SLT_EMPNO,SLT_INOCD,SLT_PNCTM FROM SPLDATA.ho_sltrn,SPLDATA.HR_EPMST WHERE  SLT_EMPNO = EP_EMPNO AND isnull(SLT_STSFL,'1') ='1' and  SLT_EMPNO <> 'XXXX' ORDER BY SLT_EMPNO,SLT_PNCTM;


declare continue handler for not found 
set END_TABLE  =1;

   open C_ISS;
   fetch C_ISS INTO L_EMPNO,L_INOCD,L_PNCTM ; 

WHILE END_TABLE =0 DO
IF (L_INOCD = '0') THEN
    SET L_INOST ='I';
ELSE
    SET L_INOST ='O';
END IF;    
UPDATE SPLDATA.HR_EPMST SET EP_INOST =L_INOST,EP_PNCTM = L_PNCTM WHERE EP_CMPCD in ('01','02','03','04','05','11') and EP_EMPNO = L_EMPNO;
   UPDATE SPLDATA.ho_sltrn SET SLT_STSFL ='1' WHERE SLT_EMPNO = L_EMPNO AND SLT_INOCD = L_INOCD AND SLT_PNCTM = L_PNCTM;

   fetch C_ISS INTO L_EMPNO,L_INOCD,L_PNCTM ; 
END WHILE; 
close C_ISS;
COMMIT; 
END P2; 

select * from spldata.hr_epmst where  EP_CMPCD in ('01','02','03','04','05','11');
select * from spldata.hr_epmst where  EP_CMPCD in ('0',''0'');
commit;
call spldata.hr_ephct_fn();
commit;
