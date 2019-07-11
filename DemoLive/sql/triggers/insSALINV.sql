drop trigger SPLDATA.insSALINV;
commit;

create trigger SPLDATA.insSALINV after insert on SPLDATA.MR_IVTRN referencing 
new as new_row for each row mode DB2ROW 
begin 

IF new_row.ivt_saltp = '14' then
  UPDATE SPLDATA.MR_IVTR1 SET IVT_SALQT = ifnull(IVT_SALQT,0) + new_row.IVT_INVQT 
  where IVT_SALTP ='04' and IVT_CMPCD = new_row.IVT_CMPCD and IVT_INVNO = new_row.IVT_CSIRF and IVT_PRDCD = new_row.IVT_PRDCD AND IVT_PKGTP = new_row.IVT_PKGTP;
end if;
END;
commit;



