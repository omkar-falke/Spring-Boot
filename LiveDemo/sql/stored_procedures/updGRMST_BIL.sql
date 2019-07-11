
-- procedure to update bill passing calculations in MM_GRMST
-- This procedure is called externally through Bill Passing Entry module


drop procedure spldata.updGRMST_BIL;
commit;

create procedure spldata.updGRMST_BIL(IN LP_CMPCD char(2),IN LP_STRTP varchar(2),IN LP_GRNNO varchar(8), IN LP_BILRF varchar(15),IN LP_MATCD varchar(10), IN  LP_BLPQT decimal(12,3),IN LP_FRTAM decimal(12,2),IN LP_VATBL decimal(12,2),IN LP_ITVAL decimal (12,2), IN LP_USRCD varchar(5), IN LP_DELFL varchar(1))  language sql  modifies sql data 
P1:
begin
  declare L_BATNO varchar(20);
  declare L_ACPQT decimal(12,3);
  declare L_TOTQT decimal(12,3);
  declare L_BLPQT decimal(10,3);
  declare L_FRTAM decimal(10,3);
  declare L_ITVAL decimal(10,3);

  declare END_TABLE int default 0;


  declare C_GRN  cursor for  select  GR_BATNO, GR_ACPQT  from spldata.MM_GRMST where GR_CMPCD = LP_CMPCD and GR_STRTP = LP_STRTP and GR_MATCD = LP_MATCD and GR_GRNNO=LP_GRNNO order by GR_BATNO;

  declare continue handler for not found 
       set END_TABLE = 1; 

  select sum(isnull(gr_acpqt,0)) into L_TOTQT from spldata.MM_GRMST where GR_CMPCD = LP_CMPCD and GR_STRTP = LP_STRTP and GR_GRNNO = LP_GRNNO and GR_MATCD = LP_MATCD;

  open C_GRN;
   set END_TABLE = 0;
  fetch C_GRN  into  L_BATNO, L_ACPQT;

  while END_TABLE = 0 DO 
if LP_DELFL = 'A' then 
      update spldata.mm_grmst set gr_bilrf = LP_BILRF, 
                            gr_bilqt = isnull(gr_bilqt,0)+((L_ACPQT/L_TOTQT)*LP_BLPQT),
                            gr_frtam = isnull(gr_frtam,0)+((L_ACPQT/L_TOTQT)*LP_FRTAM),
                            gr_vatbl = isnull(gr_vatbl,0)+((L_ACPQT/L_TOTQT)*LP_VATBL),
                            gr_bilvl = isnull(gr_bilvl,0)+((L_ACPQT/L_TOTQT)*LP_ITVAL),
                             gr_trnfl = '0', gr_lupdt = current_date, gr_lusby = LP_USRCD
        where gr_CMPCD = LP_CMPCD and gr_strtp = LP_STRTP and gr_grnno = LP_GRNNO and gr_matcd = LP_MATCD and gr_batno = L_BATNO;
end if;


if LP_DELFL = 'D' then 
      update spldata.mm_grmst set gr_bilrf = LP_BILRF, 
                            gr_bilqt = isnull(gr_bilqt,0)-((L_ACPQT/L_TOTQT)*LP_BLPQT),
                            gr_frtam = isnull(gr_frtam,0)-((L_ACPQT/L_TOTQT)*LP_FRTAM),
                            gr_vatbl = isnull(gr_vatbl,0)-((L_ACPQT/L_TOTQT)*LP_VATBL),
                            gr_bilvl = isnull(gr_bilvl,0)-((L_ACPQT/L_TOTQT)*LP_ITVAL),
                             gr_trnfl = '0', gr_lupdt = current_date, gr_lusby = LP_USRCD
        where gr_CMPCD = LP_CMPCD and gr_strtp = LP_STRTP and gr_grnno = LP_GRNNO and gr_matcd = LP_MATCD and gr_batno = L_BATNO;
end if;

      set END_TABLE = 0;
  fetch C_GRN  into  L_BATNO, L_ACPQT;
end while;
close C_GRN;
       
end P1;

commit;

