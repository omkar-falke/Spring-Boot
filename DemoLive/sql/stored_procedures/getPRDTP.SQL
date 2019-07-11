drop procedure spldata.getPRDTP;
commit;


create procedure spldata.getPRDTP(IN LP_PRDCD char(10),  OUT L_PRDTP char(2))  language sql  modifies sql data 
P1:

begin
  declare L_PRDCT char(3);
  declare END_TABLE int default 0;
    set L_PRDCT = SUBSTRING(LP_PRDCD,1,3);
    set L_PRDTP = 'XX';
    if L_PRDCT = '511'  or L_PRDCT = '519' then
       set L_PRDTP = '01';
    end if;
    if L_PRDCT = '512' then
       set L_PRDTP = '03';
    end if;
    if L_PRDCT like '52_' then
       set L_PRDTP = '02';
    end if;
    if L_PRDCT like '53_' then
       set L_PRDTP = '04';
    end if;
    if L_PRDCT like '54_' then
       set L_PRDTP = '05';
    end if;
    if L_PRDCT like '68_' then
       set L_PRDTP = '11';
    end if;
    if L_PRDCT like '51_'  and L_PRDTP = 'XX' then
       set L_PRDTP = '01';
    end if;
end;
commit;


drop procedure spldata.getZONCD;
commit;

create procedure spldata.getZONCD(IN LP_SBSCD char(6),  OUT L_ZONCD char(2))  language sql  modifies sql data 
P1:

begin
  declare L_SALTP char(2);
  declare END_TABLE int default 0;
   set L_ZONCD = substring(LP_SBSCD,1,2);
   set L_SALTP  = substring(LP_SBSCD,3,2);
    if L_ZONCD = '11' and L_SALTP = '12' then
       set L_ZONCD = '12';
    end if;
    if L_ZONCD = '11' and L_SALTP = '16' then
       set L_ZONCD = '51';
    end if;
    if L_ZONCD = '11' and L_SALTP = '21' then
       set L_ZONCD = '21';
    end if;
end;
commit;
