drop procedure SPLDATA.hr_ephct;
commit;

create procedure SPLDATA.hr_ephct(IN LP_CMPCD char(2)) language sql modifies sql data
P2:
begin
declare L_EMPNO char(5);
declare L_INOCD char(1);
declare L_INOST CHAR(1);
declare L_PNCTM  TIMESTAMP;
declare END_TABLE int default 0; 

declare C_ISS CURSOR FOR SELECT SLT_EMPNO,SLT_INOCD,max(SLT_PNCTM) SLT_PNCTM FROM SPLDATA.HR_SLTRN,SPLDATA.HR_EPMST WHERE EP_LFTDT is null and SLT_EMPNO = EP_EMPNO AND isnull(SLT_STSFL,'1') ='1' and  SLT_EMPNO <> 'XXXX' group by SLT_EMPNO,SLT_INOCD ORDER BY SLT_EMPNO,SLT_PNCTM;


declare continue handler for not found 
set END_TABLE  =1;

   open C_ISS;
   fetch C_ISS INTO L_EMPNO,L_INOCD,L_PNCTM ; 

set END_TABLE  =0;
WHILE END_TABLE =0 DO
IF (L_INOCD = '0') THEN
    SET L_INOST ='I';
ELSE
    SET L_INOST ='O';
END IF;    
UPDATE SPLDATA.HR_EPMST SET EP_INOST =L_INOST,EP_PNCTM = L_PNCTM WHERE  EP_EMPNO = L_EMPNO;
   UPDATE SPLDATA.HR_SLTRN SET SLT_STSFL ='1'  WHERE  SLT_EMPNO = L_EMPNO AND SLT_INOCD = L_INOCD AND SLT_PNCTM = L_PNCTM;

   fetch C_ISS INTO L_EMPNO,L_INOCD,L_PNCTM ; 
END WHILE; 
close C_ISS;
COMMIT; 
END P2; 

commit;
call spldata.hr_ephct('01');
commit;




 SELECT * FROM SPLDATA.HR_SLTRN WHERE  isnull(SLT_STSFL,'1') ='2' and  SLT_CMPCD='01' and slt_empno like '6%';
update spldATA.HR_SLTRN set slt_stsfl='1' WHERE  isnull(SLT_STSFL,'1') ='2' and  SLT_CMPCD='01' and slt_empno like '6%';
commit;

