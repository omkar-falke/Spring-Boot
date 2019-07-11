update spldata.mm_stprc a set 
(STP_YOSQT,stp_yosvl,stp_yomqt,stp_yomvl,stp_mosqt,stp_mosvl,stp_momqt,stp_momvl,STP_YCSQT,STP_YCSVL ,STP_YCMQT,STP_YCMVL ,STP_MCSQT,STP_MCSVL ,STP_MCMQT,STP_MCMVL ,STP_WAVRT,STP_YCLRT,STP_ADJVL) = (SELECT c.yr_YCSQT,c.yr_YCSVL,c.yr_YCMQT,c.yr_YCMVL,c.yr_YCSQT,c.yr_YCSVL,c.yr_YCMQT,c.yr_YCMVL,c.yr_YCSQT,c.yr_YCSVL ,c.yr_YCMQT,c.yr_YCMVL ,c.yr_YCSQT,c.yr_YCSVL ,c.yr_YCMQT,c.yr_YCMVL ,c.yr_YCLRT,c.yr_YCLRT,c.yr_ADJVL from spldata.mm_yrprc c where c.yr_cmpcd=a.stp_cmpcd and c.yr_strtp=a.stp_strtp and c.yr_matcd=a.stp_matcd) where stp_cmpcd='01' and stp_cmpcd||stp_strtp||stp_matcd in (select c.yr_cmpcd||c.yr_strtp||c.yr_matcd from spldata.mm_yrprc c where c.yr_cmpcd='01');
;
commit;
;

update spldata.mm_stprc set 
STP_CRCQT =0 ,
STP_CRCVL =0 ,
STP_CISQT  =0 ,
STP_CISVL   =0 ,
STP_CMRQT =0 ,
STP_CMRVL =0 ,
STP_CSAQT =0 ,
STP_CSAVL  =0 ,
STP_CSTQT =0 ,
STP_CSTVL  =0 ,
STP_CMDQT=0 ,
STP_CMIQT  =0 ,
STP_CMDVL =0 ,
STP_CMIVL   =0 ,
STP_MORCQ =0 ,
STP_MORCV=0 ,
STP_MOISQ =0 ,
STP_MOISV =0 ,
STP_MOMRQ=0 ,
STP_MOMRV=0 ,
STP_MOSAQ=0 ,
STP_MOSAV=0 ,
STP_MCRCQ=0 ,
STP_MCRCV=0 ,
STP_MCISQ =0 ,
STP_MCISV    =0 ,
STP_MCMRQ  =0 ,
STP_MCMRV  =0 ,
STP_MCSAQ  =0 ,
STP_MCSAV  = 0,
STP_MOSTQ  = 0,
STP_MOSTV  = 0,
STP_MCSTQ  = 0,
STP_MCSTV  = 0 where stp_cmpcd='01';


commit;
update spldata.mm_stmst a set 
(ST_YOPST,ST_YOPVL) = (SELECT b.YR_YCSQT,b.YR_YCSVL from spldata.mm_yrprc  b where b.yr_cmpcd=a.st_cmpcd and b.yr_strtp=a.st_strtp and b.yr_matcd=a.st_matcd) where a.st_cmpcd='01' and a.st_cmpcd||a.st_strtp||a.st_matcd in (select b.yr_cmpcd||b.yr_strtp||b.yr_matcd from spldata.mm_yrprc b where b.yr_cmpcd='01');
commit;



update spldata.mm_stmst set 
ST_YTDGR = 0,
ST_YTDIS   = 0,
ST_YTDMR = 0,
ST_YTDSN  = 0,
ST_YTDST  = 0 where st_cmpcd='01';
commit;






;
;;
select 
ST_MMSBS     VARCHAR(6)   not null,               -- Subsystem code
ST_STRTP     VARCHAR(2)   not null,               -- Stores type
ST_MATCD     VARCHAR(10)   not null,              -- Material code
ST_MATDS     VARCHAR(60),                         -- Material Desc.
ST_MATTP     VARCHAR(1),                          -- Material Type
ST_LOCCD     VARCHAR(10),                         -- Location code
ST_UOMCD     VARCHAR(3),                          -- Unit Of Measurement code
ST_STKFL     VARCHAR(1),                          -- Stock controlled item
ST_MINLV     DECIMAL(12,3),                       -- Minimum level
ST_MAXLV     DECIMAL(12,3),                       -- Maximum level
ST_RORLV     DECIMAL(12,3),                       -- Reorder Level
ST_RORQT     DECIMAL(12,3),                       -- Reorder Qty.
ST_YOPST     DECIMAL(12,3),                       -- Year Op. Stock
ST_YOPVL     DECIMAL(12,2),                       -- Year Op. value
ST_STKQT     DECIMAL(12,3),                       -- Stock on hand
ST_STKIN     DECIMAL(12,3),                       -- Stock on indent
ST_STKOR     DECIMAL(12,3),                       -- Stock on order
ST_STKIP     DECIMAL(12,3),                       -- Stock on inspection
ST_STODT     DATE,                                -- Stock out date
ST_PPONO     VARCHAR(8),                          -- Prv. P.O. number
ST_PPOVL     DECIMAL(12,2),                       -- Prv. P.O value
ST_PGRNO     VARCHAR(8),                          -- Prv. GRIN no.
ST_PGRDT     DATE,                                -- Prv. GRIN date
ST_PISNO     VARCHAR(8),                          -- Prv. Issue no.
ST_PISDT     DATE,                                -- Prv. Issue date
ST_PMRNO     VARCHAR(8),                          -- Prv. MRN no.
ST_PMRDT     DATE,                                -- Prv. MRN Date
ST_PSNNO     VARCHAR(8),                          -- Prv. SAn no.
ST_PSNDT     DATE,                                -- Prv. SAN Date
ST_PSTNO     VARCHAR(8),                          -- Prv. STN no.
ST_PSTDT     DATE,                                -- prv. STN date
ST_YTDGR     DECIMAL(12,3),                       -- Year to date GRIN
ST_YTDIS     DECIMAL(12,3),                       -- Year to date Issue
ST_YTDMR     DECIMAL(12,3),                       -- Year to date MRN
ST_YTDSN     DECIMAL(12,3),                       -- Year to date SAN
ST_YTDST     DECIMAL(12,3),                       -- Year to date STN
ST_MDVQT     DECIMAL(12,3),                       -- Modvat qty
ST_MDVVL     DECIMAL(12,2),                       -- Mod vat value
ST_WAVRT     DECIMAL(10,2),                       -- WAV rate
ST_ABCFL     VARCHAR(1),                          -- ABC flag
ST_HMLFL     VARCHAR(1),                          -- HML Flag
ST_VEDFL     VARCHAR(1),                          -- VED Flag
ST_XYZFL     VARCHAR(1),                          -- XYZ flag
ST_STSFL     VARCHAR(1),                          -- Status flag
ST_TRNFL     VARCHAR(1),                          -- Transfer flag
ST_LUSBY     VARCHAR(3),                          -- Last used by
ST_LUPDT     DATE,                                -- Last updated
ST_SRPQT     DECIMAL(12,3),                       -- Surplus Qty
ST_CONQT     DECIMAL(12,3),                       -- Consumption Qty.
ST_PCOQT     DECIMAL(12,3),                       -- Prv. Consumption
 Constraint MM_STMST Primary Key (ST_MMSBS,ST_STRTP,ST_MATCD))

------------------------------------------------------------------------------------
Create table MM_STPRC (                           -- Material Stock Processing (Online)
STP_MMSBS     VARCHAR(6)   not null,              -- Subsystem Code
STP_STRTP     VARCHAR(2)   not null,              -- Stores Type
STP_MATCD     VARCHAR(10)   not null,             -- Material Code
STP_MATDS     VARCHAR(60),                        -- Material Description
STP_UOMCD     VARCHAR(3),                         -- Unit Of Measurement Code
STP_YOSQT     DECIMAL(12,3),                      -- Year Opening Stock Quantity
STP_YOSVL     DECIMAL(12,2),                      -- Year Opening Stock Value
STP_YOMQT     DECIMAL(12,3),                      -- Year Opening Modvat Quantity
STP_YOMVL     DECIMAL(12,2),                      -- Year Opening Modvat Value
STP_MOSQT     DECIMAL(12,3),                      -- Month Opening Stock Quantity
STP_MOSVL     DECIMAL(12,2),                      -- Month Opening Stock Value
STP_MOMQT     DECIMAL(12,3),                      -- Month Opening Modvat Quantity
STP_MOMVL     DECIMAL(12,2),                      -- Month Opening Modvat Value
STP_YCSQT     DECIMAL(12,3),                      -- Year Closing Stock Quantity
STP_YCSVL     DECIMAL(12,2),                      -- Year Closing Stock Value
STP_YCMQT     DECIMAL(12,3),                      -- Year Closing Modvat Quantity
STP_YCMVL     DECIMAL(12,2),                      -- Year Closing Modvat Value
STP_MCSQT     DECIMAL(12,3),                      -- Monthly Closing Stock Quantity
STP_MCSVL     DECIMAL(12,2),                      -- Monthly Closing Stock Value
STP_MCMQT     DECIMAL(12,3),                      -- Monthly Closing Modvat Quantity
STP_MCMVL     DECIMAL(12,2),                      -- Monthly Closing Modvat Value
STP_CRCQT     DECIMAL(12,3),                      -- Cum. Rec. Quantity
STP_CRCVL     DECIMAL(12,2),                      -- Cum. Rec. Value
STP_CISQT     DECIMAL(12,3),                      -- Cum. Issue Quantity
STP_CISVL     DECIMAL(12,2),                      -- Cum. Issue Value
STP_CMRQT     DECIMAL(12,3),                      -- Cum. MRN Quantity
STP_CMRVL     DECIMAL(12,2),                      -- Cum. MRN Value
STP_CSAQT     DECIMAL(12,3),                      -- Cum. SAN Quantity
STP_CSAVL     DECIMAL(12,2),                      -- Cum. SAN Value
STP_CSTQT     DECIMAL(12,3),                      -- Cum. Stock Quantity
STP_CSTVL     DECIMAL(12,2),                      -- Cum. Stock Value
STP_WAVRT     DECIMAL(10,2),                      -- Wt. Avg. Rate
STP_CMDQT     DECIMAL(12,3),                      -- Cum. Modvat Rct qty.
STP_CMIQT     DECIMAL(12,3),                      -- Cum. Modvat Issue qty
STP_PRCDT     DATE,                               -- Processing Date
STP_YCLRT     DECIMAL(12,2),                      -- Year Closing Rate
STP_ADJVL     DECIMAL(12,2),                      -- Adjustment Value
STP_CMDVL     DECIMAL(12,2),                      -- Cum. Nod. Rct value
STP_CMIVL     DECIMAL(12,2),                      -- Cum. Nod. Issue value
STP_MORCQ     DECIMAL(12,3),                      -- Month Opening Receipt Qty
STP_MORCV     DECIMAL(12,2),                      -- Month Opening Receipt Value
STP_MOISQ     DECIMAL(12,3),                      -- Month Opening Issue Qty
STP_MOISV     DECIMAL(12,2),                      -- Month Opening Issue Value
STP_MOMRQ     DECIMAL(12,3),                      -- Month Opening MRN Qty
STP_MOMRV     DECIMAL(12,2),                      -- Month Opening MRN Value
STP_MOSAQ     DECIMAL(12,3),                      -- Month Opening SAN Qty
STP_MOSAV     DECIMAL(12,2),                      -- Month Opening SAN Value
STP_MCRCQ     DECIMAL(12,3),                      -- Month Closing Receipt Qty
STP_MCRCV     DECIMAL(12,2),                      -- Month Closing Receipt Value
STP_MCISQ     DECIMAL(12,3),                      -- Month Closing Issue Qty
STP_MCISV     DECIMAL(12,2),                      -- Month Closing Issue Value
STP_MCMRQ     DECIMAL(12,3),                      -- Month Closing MRN Qty.
STP_MCMRV     DECIMAL(12,2),                      -- Month Closing MRN Value
STP_MCSAQ     DECIMAL(12,3),                      -- Month Closing SAN Qty
STP_MCSAV     DECIMAL(12,2),                      -- Month Closing SAN Value
STP_CATFL     VARCHAR(1),                         -- Category Flag
STP_OWNBY     VARCHAR(3),                         -- Owned By
STP_MOSTQ     DECIMAL(12,3),                      -- -
STP_MOSTV     DECIMAL(12,2),                      -- -
STP_MCSTQ     DECIMAL(12,3),                      -- -
STP_MCSTV     DECIMAL(12,2),                      -- -
 Constraint MM_STPRC Primary Key (STP_MMSBS,STP_STRTP,STP_MATCD))

------------------------------------------------------------------------------------

------------------------------------------------------------------------------------
YR_MMSBS     VARCHAR(6)   not null,               -- Subsystem Code
YR_STRTP     VARCHAR(2)   not null,               -- Stores Type
YR_MATCD     VARCHAR(10)   not null,              -- Material Code
YR_MATDS     VARCHAR(60),                         -- Material Description
YR_UOMCD     VARCHAR(3),                          -- Unit Of Measurement
YR_YOSQT     DECIMAL(12,3),                       -- Year Opening Stock Quantity
YR_YOSVL     DECIMAL(12,2),                       -- Year Opening Stock Value
YR_YOMQT     DECIMAL(12,3),                       -- Year Opening Modvat Quantity
YR_YOMVL     DECIMAL(12,2),                       -- Year Opening Modvat Value
YR_YCSQT     DECIMAL(12,3),                       -- Year Closing Stock Quantity
YR_YCSVL     DECIMAL(12,2),                       -- Year Closing Stock Value
YR_YCMQT     DECIMAL(12,3),                       -- Year Closing Modvat Quantity
YR_YCMVL     DECIMAL(12,2),                       -- Year Closing Modvat Value
YR_CRCQT     DECIMAL(12,3),                       -- Cum Rec. Quantity
YR_CRCVL     DECIMAL(12,2),                       -- Cum Rec. Value
YR_CISQT     DECIMAL(12,3),                       -- Cum Issue Quantity
YR_CISVL     DECIMAL(12,2),                       -- Cum Issue Value
YR_CMRQT     DECIMAL(12,3),                       -- Cum MRN Quantity
YR_CMRVL     DECIMAL(12,2),                       -- Cum MRN Value
YR_CSAQT     DECIMAL(12,3),                       -- Cum SAN Quantity
YR_CSAVL     DECIMAL(12,2),                       -- Cum SAN Value
YR_CSTQT     DECIMAL(12,3),                       -- Cum Stock Quantity
YR_CSTVL     DECIMAL(12,2),                       -- Cum Stock Value
YR_WAVRT     DECIMAL(10,2),                       -- Wt. Avg. Rate
YR_CMDQT     DECIMAL(12,3),                       -- Cum Modvat Quantity
YR_CMDVL     DECIMAL(12,3),                       -- Cum Modvat Value
YR_CMIQT     DECIMAL(12,3),                       -- Cum Modvat Issue Quantity
YR_CMIVL     DECIMAL(12,3),                       -- Cum Modvat Issue Value
YR_PRCDT     DATE,                                -- Processing Date
YR_YCLRT     DECIMAL(10,2),                       -- Year Closing Rate
YR_ADJVL     DECIMAL(12,3),                       -- Adjustment value during processing
