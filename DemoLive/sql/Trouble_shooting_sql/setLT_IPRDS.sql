update spldata.pr_ltmst a set lt_iprds = (select b.pr_prdds from spldata.co_prmst b where b.pr_prdcd = a.lt_prdcd) where a.lt_cmpcd='11' and a.lt_lotno='81390198' and ifnull(a.lt_iprds,'')='';
commit;
