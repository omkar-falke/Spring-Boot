drop trigger SPLDATA.delSALINV;
commit;

create trigger SPLDATA.delSALINV after delete on SPLDATA.MR_IVTRN referencing 
old as old_row for each row mode DB2ROW 
begin 

IF old_row.ivt_saltp = '14' then
  UPDATE SPLDATA.MR_IVTR1 SET IVT_SALQT = ifnull(IVT_SALQT,0) - old_row.IVT_INVQT 
  where IVT_SALTP ='04' and IVT_CMPCD = old_row.IVT_CMPCD and IVT_INVNO = old_row.IVT_CSIRF and IVT_PRDCD = old_row.IVT_PRDCD AND IVT_PKGTP = old_row.IVT_PKGTP;
end if;
END;
commit;



