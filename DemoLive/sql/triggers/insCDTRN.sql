drop trigger spldata.insCDTRN;
commit;

create trigger spldata.insCDTRN after insert on spldata.CO_CDTRN referencing 
new as new_row for each row mode DB2ROW 
begin 
insert into shndata.co_cdtrn select * from spldata.co_cdtrn where 
cmt_cgmtp = new_row.cmt_cgmtp and cmt_cgstp = new_row.cmt_cgstp and cmt_codcd = new_row.cmt_codcd
and new_row.cmt_cgmtp ='SYS' and new_row.cmt_cgstp ='COXXDST';
END;
commit;
