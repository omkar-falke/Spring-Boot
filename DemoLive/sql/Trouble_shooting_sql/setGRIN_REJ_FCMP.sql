--GRIN NO. 43/03400025
update spldata.mm_grmst set gr_gpqty=gr_rejqt,gr_gptag='C' where gr_cmpcd='01' and gr_strtp='43' and gr_grnno='03400025' and ifnull(gr_rejqt,0)>0;
commit;

