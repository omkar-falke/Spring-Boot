
-- procedure to update bill passing calculations in MM_ISMST
-- This procedure is called externally through Bill Passing Entry module


drop procedure spldata.updISMST_BIL;
commit;

						  --updISMST_BIL(LP_STRTP,LP_ISSNO,LP_MATCD,LP_TAGNO,LP_ISSVL,LP_ISSRT,LP_USRCD) 


create procedure spldata.updISMST_BIL(IN LP_CMPCD char(2),IN LP_STRTP varchar(2),IN LP_ISSNO varchar(8), IN LP_MATCD varchar(10), IN  LP_TAGNO varchar(10),IN LP_ISSVL decimal(12,2),IN LP_ISSRT decimal(12,2),IN LP_USRCD varchar(5))  language sql  modifies sql data 
P1:
begin
  declare L_TOTQT decimal(12,3);
  declare L_BATNO varchar(20);
  declare L_ISSQT decimal(12,3);
  declare L_ISSVL decimal(12,2);
  declare L_ISSRT decimal(12,2);

  declare END_TABLE int default 0;


  declare C_ISS  cursor for  select  IS_BATNO, IS_ISSQT  from spldata.MM_ISMST where IS_CMPCD = LP_CMPCD and IS_STRTP = LP_STRTP and IS_MATCD = LP_MATCD and IS_TAGNO = LP_TAGNO order by IS_BATNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

  select sum(isnull(is_issqt,0)) into L_TOTQT from spldata.MM_ISMST where IS_CMPCD = LP_CMPCD and IS_STRTP = LP_STRTP and IS_ISSNO = LP_ISSNO and IS_MATCD = LP_MATCD and IS_TAGNO = LP_TAGNO;

  open C_ISS;
   set END_TABLE = 0;
  fetch C_ISS  into  L_BATNO, L_ISSQT;

  while END_TABLE = 0 DO 
      update spldata.mm_ISMST set is_issvl = ((L_ISSQT/L_TOTQT)*LP_ISSVL),  is_issrt = LP_ISSRT,
                             is_trnfl = '0', is_lupdt = current_date, is_lusby = LP_USRCD
        where is_CMPCD = LP_CMPCD and is_strtp = LP_STRTP and is_issno = LP_ISSNO and is_matcd = LP_MATCD and is_tagno = LP_TAGNO and  is_batno = L_BATNO;
      set END_TABLE = 0;
  fetch C_ISS  into  L_BATNO, L_ISSQT;
end while;
close C_ISS;
       
end P1;

commit;


