import java.sql.*;
import java.text.*;
import java.util.StringTokenizer;
public  class cl_cust{
	
    static ResultSet LM_CTRLST = null;
	static ResultSet LM_RSLSET = null;
	static cl_cust ocl_cust;
	static String M_strSQLQRY;
	static String LM_QUERY;
	static String LM_TMPSTR;
	static String LM_STLSQL;
	static String LM_TRNTP;
	static String LM_SALTP;
	static String LM_WRHTP;
	static String LM_PRDTP;
	static String L_PRDCD;
	static String LM_PKGCT;
	static String L_PKGTP;
	static String LM_TRNQT,LM_STKQT,LM_UCLQT,LM_RCTQT,LM_ISSQT;
	static String LM_FRSRCT = "10"; //Fresh Receipt type
	static String LM_RPKRCT = "15"; //Rebagging Receipt type
	static String LM_SLRRCT = "30"; //Sales Return Receipt type
	
	static String LM_SLRDSP = "30"; //Sales Return Despatch type
	static String LM_DIRDSP = "10"; //Direct Despatch type
	static String LM_DOMDSP = "1"; //Domestic Despatch Sale Type
	static String LM_EXPDSP = "2"; //Export Despatch sale Type
	static String LM_DEXDSP = "3"; //Deemed Export Despatch Sale Type
	static String LM_STFDSP = "4"; //Stock Transfer Despatch sale Type
	static String LM_FTSDSP = "5"; //Free test Sample Despatch Sale Type
	static String LM_CPCDSP = "6"; //Captive Consumption Despatch sale Type
	static DateFormat M_fmtLCDAT=new SimpleDateFormat("dd/MM/yyyy");
	static DateFormat M_fmtDBDAT=new SimpleDateFormat("MM/dd/yyyy");
	static java.util.Date LM_TMPDT;
	
	public static void showEXMSG(Exception LP_EX, String LP_MTHNM, String LP_SETFL)
	{
		System.out.println(LP_MTHNM+" : "+LP_EX.toString());
		if (LP_SETFL.equals("setLCLUPD"))
			cl_dat.M_flgLCUPD_pbst = false;
	}
	
	public static String getMAXDT(String LP_WRHTP, String LP_PRDTP, String LP_LOTNO, String LP_RCLNO){
		String L_RTNSTR = "";
		try{
			LM_QUERY = " Select max(RCT_RCTDT) L_RCTDT from FG_RCTRN where rct_wrhtp='"+LP_WRHTP+"'";
			LM_QUERY += " and rct_prdtp='"+LP_PRDTP+"' and rct_lotno='"+LP_LOTNO+"' and rct_rclno='"+LP_RCLNO+"'";
			LM_QUERY += " and rct_rcttp in ('10','15') and rct_stsfl not in 'X'";
			//System.out.println(LM_QUERY);
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_CTRLST.next()){
				L_RTNSTR = M_fmtLCDAT.format(LM_CTRLST.getDate("L_RCTDT"));
				}
			LM_CTRLST.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getMAXDT","");
		}
		return L_RTNSTR;
	}
	
	public static void exeRCTQRY(String LP_FRMDT,String LP_TODAT,String LP_CONDN){// get System Sub Type.
		try{
//			cl_dat.M_RCTQT = "";
//			cl_dat.M_RCTPK = "";
      		LM_QUERY = " Select sum(RCT_RCTQT),sum(RCT_RCTPK) from FG_RCTRN,PR_LTMST,CO_PRMST where";
			LM_QUERY += " RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and RCT_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD and";
			LM_QUERY += LP_CONDN + " and RCT_RCTDT between "+LP_FRMDT+" and "+LP_TODAT+" and RCT_STSFL='2'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_CTRLST.next()){
//				cl_dat.M_RCTQT = LM_CTRLST.getString(1);
//				cl_dat.M_RCTPK = LM_CTRLST.getString(2);
				}
			LM_CTRLST.close();
/*			if(cl_dat.M_RCTQT == null || cl_dat.M_RCTQT.length() == 0)
				cl_dat.M_RCTQT = "0.000";
			if(cl_dat.M_RCTPK == null || cl_dat.M_RCTPK.length() == 0)
				cl_dat.M_RCTPK = "0";
*/			//System.out.println(L_RCTQT);
	  }catch(SQLException L_EX){
			showEXMSG(L_EX,"exeRCTQRY","");
      }
    }
	
	public static String getPRDCD(String LP_PRDDS){
		String L_PRDCD = "";
		try{
			LM_QUERY = "Select PR_PRDCD from CO_PRMST where PR_PRDDS='"+LP_PRDDS+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			while(LM_CTRLST.next()){
				L_PRDCD = LM_CTRLST.getString("PR_PRDCD");
			}
			LM_CTRLST.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getPRDCD","setLCLUPD");
		}
		if(L_PRDCD == null)
			L_PRDCD = "";
		return L_PRDCD;
	}
	
	public static String getPRDCD(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO){
		String L_PRDCD = "";
		try{
			LM_QUERY = "Select LT_PRDCD from PR_LTMST where LT_PRDTP='"+LP_PRDTP+"'";
			LM_QUERY += " and LT_LOTNO='"+LP_LOTNO+"' and LT_RCLNO='"+LP_RCLNO+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			while(LM_CTRLST.next()){
				L_PRDCD = LM_CTRLST.getString("LT_PRDCD");
			}
			LM_CTRLST.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getPRDCD","setLCLUPD");
		}
		if(L_PRDCD == null)
			L_PRDCD = "";
		return L_PRDCD;
	}
	
	public static String getPRDDS(String L_PRDCD){ //To get Product Grade i.e SH3001
		String L_PRDDS = "";
		try{
			LM_QUERY = "Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+L_PRDCD+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			while(LM_CTRLST.next()){
				L_PRDDS = LM_CTRLST.getString("PR_PRDDS");
			}
			LM_CTRLST.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getPRDDS","");
			}
		if(L_PRDDS == null)
			L_PRDDS = "";
		return L_PRDDS;
	}
	
	public static String getPRDDS(String L_PRDTP,String L_LOTNO,String L_RCLNO){ //To get Product Grade i.e SH3001
		String L_PRDDS = "";
		try{
			LM_QUERY = "Select PR_PRDDS from CO_PRMST,PR_LTMST where PR_PRDCD=LT_PRDCD";
			LM_QUERY += " and LT_PRDTP='"+L_PRDTP+"' and LT_LOTNO='"+L_LOTNO+"' and LT_RCLNO='"+L_RCLNO+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			while(LM_CTRLST.next()){
				L_PRDDS = LM_CTRLST.getString("PR_PRDDS");
			}
			LM_CTRLST.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getPRDDS","");
			}
		if(L_PRDDS == null)
			L_PRDDS = "";
		return L_PRDDS;
	}
	
	public static String getMNPRD(String LP_MNPRD){  //To get Main Product Code i.e Polystyrene
		String L_MNPRD = "";
		try{
			LM_QUERY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and substr(CMT_CODCD,1,3)='"+LP_MNPRD+"' and CMT_CCSVL='MG'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			while(LM_CTRLST.next()){
				L_MNPRD = LM_CTRLST.getString("CMT_CODDS");
			}
			LM_CTRLST.close();
		}catch(Exception L_EX){
			System.out.println(L_EX);
			}
		if(L_MNPRD == null)
			L_MNPRD = "";
		return L_MNPRD;
	}
	
	public static String getSBPRD(String LP_SBPRD){ //To get Sub Product Code i.e HIPS,GPPS
		String L_SBPRD = "";
		try{
			LM_QUERY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXPGR' and substr(CMT_CODCD,1,4)='"+LP_SBPRD+"' and CMT_CCSVL='SG'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			while(LM_CTRLST.next()){
				L_SBPRD = LM_CTRLST.getString("CMT_CODDS");
			}
			LM_CTRLST.close();
		}catch(Exception L_EX){
			System.out.println(L_EX);
			}
		if(L_SBPRD == null)
			L_SBPRD = "";
		return L_SBPRD;
	}
	
	public static String getPRODCD(String LP_PRDCD){
			String L_PSPRD = "";
			String L_SPSPD = "";
			String L_PRODCD = "";
			if(!LP_PRDCD.equals("") || LP_PRDCD != null){
				L_PSPRD = LP_PRDCD.substring(0,4).toString().trim();
				L_SPSPD = LP_PRDCD.substring(0,3).toString().trim();
				if(L_PSPRD.equals("6701"))
					L_PRODCD = "SC";
				else if(L_PSPRD.equals("6702"))
					L_PRODCD = "SH";
				else if(L_SPSPD.equals("671"))
					L_PRODCD = "SP";
				else
					L_PRODCD = "PS";
		}else
			L_PRODCD = "";
		return L_PRODCD;
	}
	
	public static String getRESFL(String L_LOTNO,String L_RCLNO){
		String L_RESFL = "";
		try{
			LM_QUERY = "Select LT_RESFL from PR_LTMST where LT_LOTNO='"+L_LOTNO+"' and LT_RCLNO='"+L_RCLNO+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_CTRLST.next()){
				L_RESFL = LM_CTRLST.getString("LT_RESFL");
				}
			LM_CTRLST.close();
			if(L_RESFL != null)
				L_RESFL = L_RESFL.trim();
			else
				L_RESFL = "0";
		}catch(Exception L_EX){
			System.out.println("Exception L_RESFL: "+L_RESFL);
			showEXMSG(L_EX,"getRESFL","setLCLUPD");
		}
		System.out.println("L_RESFL: "+L_RESFL);
		return L_RESFL;
	}
	
	/**
 * method for getting Customer Name
 * 
*/
	public static String getPRTNM(String LP_PRTCD){
		String L_PRTNM = "";
		try{
			LM_QUERY = "Select PT_PRTNM from CO_PTMST where";
			LM_QUERY += " PT_PRTTP='C' and PT_PRTCD='"+LP_PRTCD+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_CTRLST.next())
				L_PRTNM = LM_CTRLST.getString("PT_PRTNM"); 
			if(L_PRTNM != null)
				L_PRTNM = L_PRTNM.trim();
			else
				L_PRTNM = "";
			LM_CTRLST.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getPRTNM","");
		}
		return L_PRTNM;
	}
	
	/**
 * method for getting Transporter Name
 * 
*/
	public static String getTRNNM(String LP_PRTCD){
		String L_TRNNM = "";
		try{
			LM_QUERY = "Select PT_PRTNM from CO_PTMST where";
			LM_QUERY += " PT_PRTTP='T' and PT_PRTCD='"+LP_PRTCD+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_CTRLST.next())
				L_TRNNM = LM_CTRLST.getString("PT_PRTNM"); 
			if(L_TRNNM != null)
				L_TRNNM = L_TRNNM.trim();
			else
				L_TRNNM = "";
			LM_CTRLST.close();
	}catch(Exception L_EX){
			showEXMSG(L_EX,"getTRNNM","");
		}
		return L_TRNNM;
	}
	
	/**
 * method for getting Customer Name
 * 
*/
	public static String getPTMST(String LP_PRTCD,char LP_PRTTP){
		String L_PRTNM = "";
		try{
			LM_QUERY = "Select PT_PRTNM from CO_PTMST where";
			LM_QUERY += " PT_PRTTP='"+LP_PRTTP+"' and PT_PRTCD='"+LP_PRTCD+"'";
			LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_CTRLST.next())
				L_PRTNM = LM_CTRLST.getString("PT_PRTNM"); 
			if(L_PRTNM != null)
				L_PRTNM = L_PRTNM.trim();
			else
				L_PRTNM = "";
			LM_CTRLST.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getPTMST","");
		}
		return L_PRTNM;
	}
	
	public static boolean chkTBLQT(String LP_WRHTP,String LP_RCTTP,String LP_RCTNO,boolean LP_DIRFL,boolean LP_DATFL,String LP_FRMDT,String LP_TODAT){
		try{
			String L_BAGQT = "";
			String L_BAGPK = "";
			String L_RCTQT = "";
			String L_RCTPK = "";
			String L_ADDQRY = "";
			String LP_RCTRF = LP_WRHTP + LP_RCTTP + LP_RCTNO;
			if(LP_DIRFL){
				if(LP_DATFL){
					L_ADDQRY = " LW_RCTDT between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+"";
				}else{
					L_ADDQRY = " LW_RCTRF ='"+LP_RCTRF+"'";
				}
				LM_QUERY = "Select sum(LW_BAGQT),sum(LW_BAGPK) from FG_LWMST"; 
				LM_QUERY += " where " + L_ADDQRY + " and LW_STSFL not in ('X')";
				LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
				while(LM_CTRLST.next()){
					L_BAGQT = LM_CTRLST.getString(1);
					L_BAGPK = LM_CTRLST.getString(2);
					}
				if(LM_CTRLST != null)
					LM_CTRLST.close();
				if(LP_DATFL){
					L_ADDQRY = " RCT_RCTDT between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+"";
					L_ADDQRY += " and RCT_RCTTP in ('10','15') ";
				}else{
					L_ADDQRY = " RCT_WRHTP='"+LP_WRHTP+"' and RCT_RCTTP='"+LP_RCTTP+"'";
					L_ADDQRY += " and RCT_RCTNO='"+LP_RCTNO+"'";
				}
				LM_QUERY = "Select sum(RCT_RCTQT),sum(RCT_RCTPK) from FG_RCTRN"; 
				LM_QUERY += " where " + L_ADDQRY + " and RCT_STSFL not in ('X')";
				LM_CTRLST = cl_dat.exeSQLQRY(LM_QUERY );
				while(LM_CTRLST.next()){
					L_RCTQT = LM_CTRLST.getString(1);
					L_RCTPK = LM_CTRLST.getString(2);
					}
				if(LM_CTRLST != null)
					LM_CTRLST.close();
				if(L_BAGQT.equals(L_RCTQT) && L_BAGPK.equals(L_RCTPK))
					return true;
			}else
				return true;
		}catch(Exception L_EX){
			showEXMSG(L_EX,"chkTBLQT","");
		}
		return false;
	}
	
	public static void cpyDBSTR(String LP_SYSCD){
		boolean L_EXTVL = false;
		Runtime r = Runtime.getRuntime();
		try{
			Process p = null;
//			p  = r.exec("F:\\data\\asoft\\exec\\splerp\\co_exsup.exe DTRZAP "+cl_dat.M_SPSYSCD+" "+cl_dat.M_strSYSLC_pb_st); 
			while(!L_EXTVL){
				try{
					L_EXTVL = true;
					p.exitValue();
			}catch(Exception L_E){
					L_EXTVL = false;
				}
			}
			p.destroy();
		}catch(Exception L_EX){
				showEXMSG(L_EX,"cpyDBSTR","");
			}
	}

	public static void crtDBVIEW(String LP_SYSCD){
		boolean L_EXTVL = false;
		Runtime r = Runtime.getRuntime();
		try{
			Process p = null;
//			p  = r.exec(cl_dat.M_SPELB+"\\co_exsup.exe VIEW "+LP_SYSCD+" "+cl_dat.M_strSYSLC_pb_st); 
			while(!L_EXTVL){
				try{
					L_EXTVL = true;
					p.exitValue();
			}catch(Exception L_EX){
					L_EXTVL = false;
				}
			}
			p.destroy();
		}catch(Exception L_EX){
				showEXMSG(L_EX,"crtDBVIEW","");
			}
	}

	public static void zipDTRFL(String LP_FILNM){
		boolean L_EXTVL = false;
		Runtime r = Runtime.getRuntime();
		Process p = null;
		try{
			System.out.println("jar cf F:\\DATA\\TOHO\\FIMS\\" + LP_FILNM + ".jar -C F:\\data\\FOXDAT\\0102\\dtrwork .");
			p  = r.exec("jar cf F:\\DATA\\TOHO\\FIMS\\" + LP_FILNM + ".jar -C F:\\data\\FOXDAT\\0102\\dtrwork ."); 
			while(!L_EXTVL){
				try{
					L_EXTVL = true;
					p.exitValue();
			}catch(Exception L_E){
					L_EXTVL = false;
				}
			}
			p.destroy();
			System.out.println("Jar file creation completed.");
	}catch(Exception L_EX){
		showEXMSG(L_EX,"zipDTRFL","");
 		}
	}

	public static void updDTRFL(String LP_SESNO,String LP_CHP01){
		try{
			LM_STLSQL = "Update CO_CDTRN set ";
			LM_STLSQL += "CMT_CCSVL = '"+LP_SESNO+"',";
			LM_STLSQL += "CMT_CHP01 = '"+LP_CHP01+"'";
			LM_STLSQL += " where CMT_CGMTP = 'DTR'";
			LM_STLSQL += " and CMT_CGSTP = 'FGXXTOH'";
			LM_STLSQL += " and CMT_CODCD = '0000'";
			cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");
//			cl_dat.exeSQLUPD(M_strSQLQRY,"SP","REM","");
			if(cl_dat.M_flgLCUPD_pbst){
				cl_dat.exeDBCMT("cl_cust");
//				cl_dat.exeDBCMT("SP","REM","");
		}else{
//			cl_dat.exeDBRBK("SP","ACT");
//			cl_dat.exeDBRBK("SP","REM");
			System.out.println("Session number Updation Failed ... in CO_CDTRN");
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"updDTRFL","");
		}			
	}
 
	public static void delLCKTBL(){
		try{
			LM_STLSQL = "Delete from FG_LKTRN";
			cl_dat.exeSQLUPD(LM_STLSQL ," ");
				cl_dat.exeDBCMT("cl_cust.dellcktbl");
		}catch(Exception L_EX){
			showEXMSG(L_EX,"delLCKTBL","");
		}
	}
	
	public static void cpyRCTRN(String LP_CONDN){
		try{
			LM_STLSQL = "Insert into fg_lktrn select distinct rct_wrhtp,rct_prdtp,";   
			LM_STLSQL += "rct_lotno,rct_mnlcd,'03/19/2002',0,'','','','','','03/19/2002',0,'',";       
			LM_STLSQL += "'',0,0,rct_pkgtp,rct_rclno,0,0,'Y','N',0,0.025,'','','' from fg_rctrn" + LP_CONDN;          
			cl_dat.exeSQLUPD(LM_STLSQL ,"");
/*			if(cl_dat.M_flgLCUPD_pbst){
				cl_dat.exeDBCMT( "CMT");
			}else{
				cl_dat.exeDBRBK("SP","ACT");
			}
*/
			cl_dat.exeDBCMT("cl_cust.cpyRCTRN");
		}catch(Exception L_EX){
			showEXMSG(L_EX,"cpyRCTRN","");
		}
	}
	
	public static void cpyISTRN(String LP_CONDN){
		try{
			LM_STLSQL = "Insert into fg_lktrn select distinct ist_wrhtp,ist_prdtp,ist_lotno,";
			LM_STLSQL += "ist_mnlcd,'03/19/2002',0,'','','','','','03/19/2002',0,'','',0,0,";    
			LM_STLSQL += "ist_pkgtp,ist_rclno,0,0,'N','Y',0,0.025,'','','' from fg_istrn" + LP_CONDN;       
			cl_dat.exeSQLUPD(LM_STLSQL ,"");
/*			if(cl_dat.M_flgLCUPD_pbst){
				cl_dat.exeDBCMT( "CMT");
			}else{
				cl_dat.exeDBRBK("SP","ACT");
			}
*/
		cl_dat.exeDBCMT("cl_cust.cpyISTRN");
		}catch(Exception L_EX){
			showEXMSG(L_EX,"cpyISTRN","");
		}
	}
	
	public static void cpyIVTRN(String LP_CONDN){
		try{
			LM_STLSQL = "Insert into fg_lktrn select distinct '','','','','03/19/2002',";
			LM_STLSQL += "0,'','','','','','03/19/2002',0,'','',0,0,'','',0,0,'',";    
			LM_STLSQL += "ivt_ladno,0,0.025,'','',ivt_mkttp from mr_ivtrn" + LP_CONDN;       
			cl_dat.exeSQLUPD(LM_STLSQL ,"");
/*			if(cl_dat.M_flgLCUPD_pbst){
				cl_dat.exeDBCMT( "CMT");
			}else{
				cl_dat.exeDBRBK("SP","ACT");
			}
*/
		cl_dat.exeDBCMT("cl_cust.cpyIVTRN");
		}catch(Exception L_EX){
			showEXMSG(L_EX,"cpyIVTRN","");
		}
	}
	
	public static void exeALLREC(String LP_FRMDT,String LP_TODAT,boolean LP_DATFL){
		try{
			if(LP_DATFL){
				LM_STKQT = ctlSTKQT(); //updates Classified stock qty in DOC/FGXXREF/TOTCL. 
				updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTCL",LM_STKQT);
				LM_UCLQT = ctlUCLQT(); //updates UnClassified stock qty in DOC/FGXXREF/TOTUC. 
				updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTUC",LM_UCLQT);
				LM_RCTQT = ctlRCTQT(LP_TODAT); //updates Receipt qty in DOC/FGXXREF/TOTRC
				updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTRC",LM_RCTQT);
				LM_ISSQT = ctlISSQT(LP_TODAT); //updates Issue qty in DOC/FGXXREF/TOTIS
				updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTIS",LM_ISSQT);
				getTRFREC("FG_RCTRN"," where (rct_rctdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+") or (rct_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+")","  ");
				getTRFREC("FG_LWMST"," where (lw_rctdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+") or (lw_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+")","  ");
				getTRFREC("FG_ISTRN"," where ist_issdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+" and ist_isstp <> '10'","  ");
				updTRNFL(LP_FRMDT,LP_TODAT,LP_DATFL);  //ST_TRNFL is made '3' 
				getTRFREC("FG_STMST"," where st_prdtp||st_lotno||st_rclno||st_pkgtp||st_mnlcd in "," (select lk_prdtp||lk_lotno||lk_rclno||lk_pkgtp||lk_mnlcd from fg_lktrn) or st_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)));
				getTRFREC("PR_LTMST"," where lt_prdtp||lt_lotno||lt_rclno in "," (select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn) or lt_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)));
				getTRFREC("FG_RMMST"," where rm_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)),"  ");
				getTRFREC("FG_PTFRF"," where ptf_ptfdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)),"  ");
				getTRFREC("MR_IVTRN"," where ivt_mkttp||ivt_ladno in "," (select lk_mkttp||lk_issrf from fg_lktrn) or ivt_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)));
				getTRFREC("FG_ISTRN"," where ist_mkttp||ist_issno in "," (select lk_mkttp||lk_issrf from fg_lktrn) ");
				getTRFREC("FG_LCMST"," where lc_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)),"  ");
				getTRFREC("CO_PRMST"," where pr_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)),"  ");
		}else{
			LP_TODAT = M_fmtLCDAT.format(new java.util.Date());
			LM_STKQT = ctlSTKQT(); //updates Classified stock qty in DOC/FGXXREF/TOTCL. 
			updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTCL",LM_STKQT);
			LM_UCLQT = ctlUCLQT(); //updates UnClassified stock qty in DOC/FGXXREF/TOTUC. 
			updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTUC",LM_UCLQT);
			LM_RCTQT = ctlRCTQT(LP_TODAT); //updates Receipt qty in DOC/FGXXREF/TOTRC
			updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTRC",LM_RCTQT);
			LM_ISSQT = ctlISSQT(LP_TODAT); //updates Issue qty in DOC/FGXXREF/TOTIS
			updNCSVL("S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","TOTIS",LM_ISSQT);	
			getTRFREC("PR_LTMST"," where lt_prdtp||lt_lotno||lt_rclno in "," (select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn) ");
			getTRFREC("FG_RCTRN"," where rct_prdtp||rct_lotno||rct_rclno in "," (select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn) ");
			getTRFREC("FG_LWMST"," where lw_prdtp||lw_lotno||lw_rclno in "," (select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn) ");
			getTRFREC("FG_ISTRN"," where ist_prdtp||ist_lotno||ist_rclno in "," (select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn) ");
			updTRNFL(LP_FRMDT,LP_TODAT,LP_DATFL);  //ST_TRNFL is made '3' 
			getTRFREC("FG_STMST"," where st_prdtp||st_lotno||st_rclno in "," (select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn) ");
			//getTRFREC("FG_RMMST"," where rm_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT)),"  ");
			getTRFREC("FG_PTFRF"," where ptf_prdtp||ptf_lotno||ptf_rclno in "," (select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn) ");
			getTRFTBL(" distinct IST_ISSNO ","MR_IVTRN"," where ivt_ladno in ","");
			getTRFREC("FG_LCMST"," "," ");
			getTRFTBL(" distinct IST_PRDCD ","CO_PRMST"," where pr_prdcd in ","");
			}
			//getTRFREC("FG_RSTRN","  ","  ");
			getTRFREC("FG_OPSTK","  ","  ");
			getTRFREC("CO_CDTRN"," where cmt_cgmtp||cmt_cgstp||cmt_codcd not in "," ('DTRFGXXTOH0001') ");
			if(cl_dat.M_flgLCUPD_pbst)
				System.out.println("Data Transfer Completed");
			else
				System.out.println("Data Transfer not Completed Successfully.");
		}catch(Exception L_EX){
			showEXMSG(L_EX,"exeALLREC","");
		}
	}
	
	public static void updTRNFL(String LP_FRMDT,String LP_TODAT,boolean LP_DATFL){
		try{
			System.out.println("Updation of Transaction Flag in progress..");
			String L_ADDSTR = "";
			L_ADDSTR = " where st_prdtp||st_lotno||st_rclno||st_pkgtp||st_mnlcd in (Select LK_PRDTP||LK_LOTNO||LK_RCLNO||LK_PKGTP||LK_MNLCD from FG_LKTRN)";
			if(LP_DATFL)
				L_ADDSTR += " or st_lupdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT));				
			LM_STLSQL = "Update FG_STMST set ST_TRNFL='3'" + L_ADDSTR;
			cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");
			cl_dat.exeDBCMT("updTRNFL");
		}catch(Exception L_EX){
			showEXMSG(L_EX,"updTRNFL","");
		}
	}
	
	public static void getTRFREC(String LP_TBLNM,String LP_CONDN,String LP_CONDN1){
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				System.out.println("Transfer of "+LP_TBLNM+" in progress..");
				LM_QUERY = "select * from "+ LP_TBLNM + LP_CONDN + LP_CONDN1; 
				LM_RSLSET = cl_dat.exeSQLQRY(LM_QUERY );
				if(LP_TBLNM.equals("PR_LTMST"))		
					crtLTMST(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("FG_RCTRN"))
					crtRCTRN(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("FG_LWMST"))
					crtLWMST(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("FG_ISTRN"))
					crtISTRN(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("FG_STMST"))
					crtSTMST(LM_RSLSET,"SP");
				else if(LP_TBLNM.equals("FG_OPSTK"))
					crtOPSTK(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("FG_LCMST"))
					crtLCMST(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("FG_RMMST"))
					crtRMMST(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("FG_PTFRF"))
					crtPTFRF(LM_RSLSET,"SP");					
				//else if(LP_TBLNM.equals("FG_RSTRN"))
				//	crtRSTRN(LM_RSLSET,"SP");					
				else if(LP_TBLNM.equals("MR_IVTRN"))
					crtIVTRN(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("CO_PRMST"))
					crtPRMST(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("CO_CDTRN"))
					crtCDTRN(LM_RSLSET,"SP");	
			}catch(Exception L_EX){
				showEXMSG(L_EX,"getTRFREC","");
			}
		}
	}
	
	public static void crtLTMST(ResultSet LP_RSLSET,String LP_SYSCD){
		try	{
			LM_TMPSTR = "Insert Into DT_LTMST(LT_PRDTP,LT_LOTNO,LT_PRDCD,LT_RUNNO,LT_TPRCD,LT_LINNO,LT_CYLNO,LT_PSTDT,LT_PENDT,LT_BSTDT,";
			LM_TMPSTR += "LT_BENDT,LT_PRDQT,LT_BAGQT,LT_DSPQT,LT_CLSFL,LT_PPRCD,LT_PCLBY,LT_PCLTM,LT_CPRCD,LT_CLSBY,LT_CLSTM,LT_CLSRF,";
			LM_TMPSTR += "LT_STSFL,LT_TRNFL,LT_LUSBY,LT_LUPDT,LT_ADDTM,LT_ADDBY,LT_ENDTM,LT_ENDBY,LT_RCTRF,LT_RETQT,LT_REMDS,LT_RESFL,";
			LM_TMPSTR += "LT_HLDQT,LT_IPRDS,LT_RCLNO) values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"PR_LTMST");		
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_LOTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_PRDCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_RUNNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_TPRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_LINNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_CYLNO")+"',";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_PSTDT"))+"'),";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_PENDT"))+"'),";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_BSTDT"))+"'),";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_BENDT"))+"'),";
				LM_STLSQL += LP_RSLSET.getString("LT_PRDQT")+",";
				LM_STLSQL += LP_RSLSET.getString("LT_BAGQT")+",";
				LM_STLSQL += LP_RSLSET.getString("LT_DSPQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_CLSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_PPRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_PCLBY")+"',";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_PCLTM"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_CPRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_CLSBY")+"',";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_CLSTM"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_CLSRF")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("LT_LUPDT"))+"'),";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_ADDTM"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_ADDBY")+"',";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LT_ENDTM"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_ENDBY")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_RCTRF")+"',";
				LM_STLSQL += LP_RSLSET.getString("LT_RETQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_REMDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_RESFL")+"',";
				LM_STLSQL += LP_RSLSET.getString("LT_HLDQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_IPRDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LT_RCLNO")+"')";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
			if(LP_RSLSET != null)
				LP_RSLSET.close();
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtLTMST","");
		}
	}
	
	public static void crtRCTRN(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_RCTRN(RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_MNLCD,RCT_ISSRF,";
			LM_TMPSTR += "RCT_STKTP,RCT_RCTQT,RCT_RCTPK,RCT_PKGCT,RCT_PTFRF,RCT_AUTBY,RCT_AUTDT,RCT_PTFDT,";
			LM_TMPSTR += "RCT_STSFL,RCT_TRNFL,RCT_PKGTP,RCT_RCTDT,RCT_SHFCD,RCT_PRDCD,RCT_RCLNO,RCT_LUSBY,RCT_LUPDT)";
			LM_TMPSTR += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_RCTRN");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_RCTTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_RCTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_LOTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_MNLCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_ISSRF")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_STKTP")+"',";
				LM_STLSQL += LP_RSLSET.getString("RCT_RCTQT")+",";
				LM_STLSQL += LP_RSLSET.getString("RCT_RCTPK")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_PKGCT")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_PTFRF")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_AUTBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RCT_AUTDT"))+"'),";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RCT_PTFDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_PKGTP")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RCT_RCTDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_SHFCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_PRDCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_RCLNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RCT_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RCT_LUPDT"))+"'))";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtRCTRN","");
		}
	}
	
	public static void crtLWMST(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_LWMST(LW_WRHTP,LW_RCTTP,LW_RCTNO,LW_PRDTP,LW_LOTNO,";
			LM_TMPSTR += "LW_RCLNO,LW_STRTM,LW_ENDTM,LW_STRCT,LW_ENDCT,LW_MISCT,LW_BAGQT,";
			LM_TMPSTR += "LW_BAGPK,LW_MCHNO,LW_STSFL,LW_TRNFL,LW_SHFCD,LW_LUSBY,LW_LUPDT,";
			LM_TMPSTR += "LW_MNLCD,LW_RCTDT,LW_PKGTP,LW_REMDS,LW_UCLTG) values (";
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_LWMST");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_RCTTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_RCTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_LOTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_RCLNO")+"',";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LW_STRTM"))+"'),";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("LW_ENDTM"))+"'),";
				LM_STLSQL += LP_RSLSET.getString("LW_STRCT")+",";
				LM_STLSQL += LP_RSLSET.getString("LW_ENDCT")+",";
				LM_STLSQL += LP_RSLSET.getString("LW_MISCT")+",";
				LM_STLSQL += LP_RSLSET.getString("LW_BAGQT")+",";
				LM_STLSQL += LP_RSLSET.getString("LW_BAGPK")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_MCHNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_SHFCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("LW_LUPDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_MNLCD")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("LW_RCTDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_PKGTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_REMDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LW_UCLTG")+"')";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"crtLWMST","");
		}
	}
	
	public static void crtISTRN(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_ISTRN(IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDCD,IST_PRDTP,";
			LM_TMPSTR += "IST_LOTNO,IST_PKGTP,IST_MNLCD,IST_ISSDT,IST_ISSQT,IST_ISSPK,IST_STKTP,";
			LM_TMPSTR += "IST_STSFL,IST_TRNFL,IST_TDSFL,IST_PKGCT,IST_SALTP,IST_MKTTP,IST_RCLNO,IST_LUSBY,IST_LUPDT)";
			LM_TMPSTR += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_ISTRN");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_ISSTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_ISSNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_PRDCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_LOTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_PKGTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_MNLCD")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IST_ISSDT"))+"'),";
				LM_STLSQL += LP_RSLSET.getString("IST_ISSQT")+",";
				LM_STLSQL += LP_RSLSET.getString("IST_ISSPK")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_STKTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_TDSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_PKGCT")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_SALTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_MKTTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_RCLNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IST_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IST_LUPDT"))+"'))";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtISTRN","");
		}
	}
	
	public static void crtSTMST(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_STMST(ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_MNLCD,ST_RCTDT,ST_STKQT,ST_TPRCD,ST_CPRCD,";
			LM_TMPSTR += "ST_TRNFL,ST_STSFL,ST_ALOQT,ST_UCLQT,ST_DOUQT,ST_DOSQT,ST_PRDCD,ST_MAXQT,ST_RESNO,ST_RESCD,";
			LM_TMPSTR += "ST_RESDT,ST_REXDT,ST_PKGCT,ST_PKGTP,ST_PKGWT,ST_RCLNO,ST_REMDS,ST_RESFL,ST_LUSBY,ST_LUPDT)";
			LM_TMPSTR += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_STMST");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_LOTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_MNLCD")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("ST_RCTDT"))+"'),";
				LM_STLSQL += LP_RSLSET.getString("ST_STKQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_TPRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_CPRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_STSFL")+"',";
				LM_STLSQL += LP_RSLSET.getString("ST_ALOQT")+",";
				LM_STLSQL += LP_RSLSET.getString("ST_UCLQT")+",";
				LM_STLSQL += LP_RSLSET.getString("ST_DOUQT")+",";
				LM_STLSQL += LP_RSLSET.getString("ST_DOSQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_PRDCD")+"',";
				LM_STLSQL += LP_RSLSET.getString("ST_MAXQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_RESNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_RESCD")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("ST_RESDT"))+"'),";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("ST_REXDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_PKGCT")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_PKGTP")+"',";
				LM_STLSQL += LP_RSLSET.getString("ST_PKGWT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_RCLNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_REMDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_RESFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("ST_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("ST_LUPDT"))+"'))";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_EX){
			showEXMSG(L_EX,"crtSTMST","");
		}
	}
	
	public static void crtOPSTK(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_OPSTK(OP_WRHTP,OP_PRDTP,OP_PKGCT,OP_YOSQT,OP_PRDCD,OP_SLRQT,OP_PKGTP,";
			LM_TMPSTR += "OP_YXRQT,OP_MXRQT,OP_DXRQT,OP_YRCQT,OP_MRCQT,OP_DRCQT,OP_YXDQT,OP_MXDQT,OP_DXDQT,";
			LM_TMPSTR += "OP_Y1DQT,OP_M1DQT,OP_D1DQT,OP_Y2DQT,OP_M2DQT,OP_D2DQT,OP_Y3DQT,OP_M3DQT,OP_D3DQT,";
			LM_TMPSTR += "OP_Y6DQT,OP_M6DQT,OP_D6DQT,OP_Y4DQT,OP_M4DQT,OP_D4DQT,OP_Y5DQT,OP_M5DQT,OP_D5DQT,";
			LM_TMPSTR += "OP_UCLQT,OP_TDSQT,OP_STKQT,OP_DOSQT,OP_DOUQT,OP_YAPQT,OP_YAMQT,OP_YOXQT,OP_LUPDT) values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_OPSTK");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("OP_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("OP_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("OP_PKGCT")+"',";
				LM_STLSQL += LP_RSLSET.getString("OP_YOSQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("OP_PRDCD")+"',";
				LM_STLSQL += LP_RSLSET.getString("OP_SLRQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("OP_PKGTP")+"',";
				LM_STLSQL += LP_RSLSET.getString("OP_YXRQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_MXRQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_DXRQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_YRCQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_MRCQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_DRCQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_YXDQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_MXDQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_DXDQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_Y1DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_M1DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_D1DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_Y2DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_M2DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_D2DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_Y3DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_M3DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_D3DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_Y6DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_M6DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_D6DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_Y4DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_M4DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_D4DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_Y5DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_M5DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_D5DQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_UCLQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_TDSQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_STKQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_DOSQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_DOUQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_YAPQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_YAMQT")+",";
				LM_STLSQL += LP_RSLSET.getString("OP_YOXQT")+",";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("OP_LUPDT"))+"'))";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_EX){
			showEXMSG(L_EX,"crtOPSTK","");
		}
	}
	
	public static void getTRFTBL(String LP_FLDNM,String LP_TBLNM,String LP_CONDN,String LP_CONDN1){
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				System.out.println("Transfer of "+LP_TBLNM+" in progress..");
				LM_QUERY = "select * from " + LP_TBLNM + LP_CONDN;
				LM_QUERY += " (select "+LP_FLDNM+" from fg_istrn where ist_prdtp||ist_lotno||ist_rclno in";
				LM_QUERY += " (Select lk_prdtp||lk_lotno||lk_rclno from fg_lktrn "+ LP_CONDN1 + "))";              
				LM_RSLSET = cl_dat.exeSQLQRY(LM_QUERY );
				if(LP_TBLNM.equals("MR_IVTRN"))
					crtIVTRN(LM_RSLSET,"SP");	
				else if(LP_TBLNM.equals("CO_PRMST"))
					crtPRMST(LM_RSLSET,"SP");	
			}catch(Exception L_EX){
				System.out.println(L_EX);
			}
		}
	}
	
	public static void crtIVTRN(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			/*LM_TMPSTR = "Insert Into DT_IVTRN(IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP,IVT_LADDT,IVT_LODDT,";
			LM_TMPSTR += "IVT_DORNO,IVT_DORDT,IVT_INDNO,IVT_INVNO,IVT_INVDT,IVT_GINNO,IVT_SALTP,IVT_CNSCD,";
			LM_TMPSTR += "IVT_BYRCD,IVT_DSRCD,IVT_TRPCD,IVT_TMOCD,IVT_CURCD,IVT_LRYNO,IVT_CNTDS,IVT_LR_NO,";
			LM_TMPSTR += "IVT_LR_DT,IVT_PMDDT,IVT_CPTVL,IVT_ASSVL,IVT_DSCVL,IVT_PKFVL,IVT_INSVL,IVT_STXVL,";
			LM_TMPSTR += "IVT_EXCVL,IVT_INVVL,IVT_NETVL,IVT_ECHRT,IVT_LADRT,IVT_EXCRT,IVT_INVRT,IVT_PKGWT,";
			LM_TMPSTR += "IVT_LADQT,IVT_REQQT,IVT_INVQT,IVT_INVPK,IVT_AUTBY,IVT_DSTDS,IVT_STSFL,IVT_LUSBY,";
			LM_TMPSTR += "IVT_LUPDT,IVT_DTPCD,IVT_TSTFL,IVT_SLRFL,IVT_TSLNO,IVT_RSLNO,IVT_ADLNO,IVT_ADLDT,";
			LM_TMPSTR += "IVT_PRDDS,IVT_PRDTP,IVT_LOTRF,IVT_UOMCD,IVT_PORNO,IVT_PORDT,IVT_ZONCD,IVT_PMTTP,";
			LM_TMPSTR += "IVT_EXCCO,IVT_STXCD,IVT_STXRT,IVT_OCTCD,IVT_OCTRT,IVT_SVCCD,IVT_SVCRT,IVT_ALODT,IVT_TRNFL)";*/
			LM_TMPSTR = "Insert Into DT_IVTRN(IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP,IVT_LADDT,IVT_LODDT,";
			LM_TMPSTR += "IVT_DORNO,IVT_DORDT,IVT_INDNO,IVT_INVNO,IVT_INVDT,IVT_GINNO,IVT_SALTP,IVT_CNSCD,";
			LM_TMPSTR += "IVT_BYRCD,IVT_DSRCD,IVT_TRPCD,IVT_TMOCD,IVT_CURCD,IVT_LRYNO,IVT_CNTDS,IVT_LR_NO,";
			LM_TMPSTR += "IVT_LR_DT,IVT_PMDDT,IVT_CPTVL,IVT_ASSVL,IVT_DSCVL,IVT_PKFVL,IVT_INSVL,IVT_STXVL,";
			LM_TMPSTR += "IVT_EXCVL,IVT_INVVL,IVT_NETVL,IVT_ECHRT,IVT_LADRT,IVT_EXCRT,IVT_INVRT,IVT_PKGWT,";
			LM_TMPSTR += "IVT_LADQT,IVT_REQQT,IVT_INVQT,IVT_INVPK,IVT_AUTBY,IVT_DSTDS,IVT_STSFL,IVT_LUSBY,";
			LM_TMPSTR += "IVT_LUPDT,IVT_DTPCD,IVT_TSTFL,IVT_SLRFL,IVT_TSLNO,IVT_RSLNO,IVT_ADLNO,IVT_ADLDT,";
			LM_TMPSTR += "IVT_PRDDS,IVT_PRDTP,IVT_LOTRF,IVT_UOMCD,IVT_PORNO,IVT_PORDT,IVT_ZONCD,IVT_PMTTP,";
			LM_TMPSTR += "IVT_EXCCO,IVT_STXCD,IVT_STXRT,IVT_OCTCD,IVT_OCTRT,IVT_ALODT,IVT_TRNFL)";
			LM_TMPSTR += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"MR_LATRN");
			int i = 0;
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_MKTTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_LADNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_PRDCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_PKGTP")+"',";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("IVT_LADDT"))+"'),";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("IVT_LODDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_DORNO")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IVT_DORDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_INDNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_INVNO")+"',";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("IVT_INVDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_GINNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_SALTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_CNSCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_BYRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_DSRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_TRPCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_TMOCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_CURCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_LRYNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_CNTDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_LR_NO")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IVT_LR_DT"))+"'),";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IVT_PMDDT"))+"'),";
				LM_STLSQL += LP_RSLSET.getString("IVT_CPTVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_ASSVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_DSCVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_PKFVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_INSVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_STXVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_EXCVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_INVVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_NETVL")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_ECHRT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_LADRT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_EXCRT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_INVRT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_PKGWT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_LADQT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_REQQT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_INVQT")+",";
				LM_STLSQL += LP_RSLSET.getString("IVT_INVPK")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_AUTBY")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_DSTDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IVT_LUPDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_DTPCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_TSTFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_SLRFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_TSLNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_RSLNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_ADLNO")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IVT_ADLDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_PRDDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_LOTRF")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_UOMCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_PORNO")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("IVT_PORDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_ZONCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_PMTTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_EXCCO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_STXCD")+"',";
				LM_STLSQL += LP_RSLSET.getString("IVT_STXRT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_OCTCD")+"',";
				LM_STLSQL += LP_RSLSET.getString("IVT_OCTRT")+",";
				//LM_STLSQL += "'"+LP_RSLSET.getString("IVT_SVCCD")+"',";
				//LM_STLSQL += LP_RSLSET.getString("IVT_SVCRT")+",";
				LM_STLSQL += "ctot('"+setDTMVFP(LP_RSLSET.getString("IVT_ALODT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("IVT_TRNFL")+"')";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				i++;
				}
			System.out.println("Records Transferred in DT_IVTRN: "+i);
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtIVTRN","");
		}
	}
	
	public static void crtLCMST(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_LCMST(LC_WRHTP,LC_MNLCD,LC_LOCDS,LC_MAXQT,";
			LM_TMPSTR += "LC_STKQT,LC_STSFL,LC_TRNFL,LC_LUSBY,LC_LUPDT)";
			LM_TMPSTR += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_LCMST");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("LC_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LC_MNLCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LC_LOCDS")+"',";
				LM_STLSQL += LP_RSLSET.getString("LC_MAXQT")+",";
				LM_STLSQL += LP_RSLSET.getString("LC_STKQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("LC_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LC_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("LC_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("LC_LUPDT"))+"'))";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtLCMST","");
		}
	}
	
	public static void crtRMMST(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_RMMST(RM_WRHTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,RM_REMDS,";
			LM_TMPSTR += "RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)";
			LM_TMPSTR += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_RMMST");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_TRNTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_DOCTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_DOCNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_REMDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RM_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RM_LUPDT"))+"'))";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtRMMST","");
		}
	}
	
	public static void crtPTFRF(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_PTFRF(PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_RCLNO,PTF_PKGTP,";
			LM_TMPSTR += "PTF_OPRCD,PTF_PRDCD,PTF_PTFCT,PTF_PTFDT,PTF_PTFQT,PTF_PTFRF,PTF_PTFBY)";
			LM_TMPSTR += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_PTFRF");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_PTFNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_LOTNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_RCLNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_PKGTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_OPRCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_PRDCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_PTFCT")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("PTF_PTFDT"))+"'),";
				LM_STLSQL += LP_RSLSET.getString("PTF_PTFQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_PTFRF")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PTF_PTFBY")+"')";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtPTFRF","");
		}
	}
	
	private void crtRSTRN(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_RSTRN(RS_WRHTP,RS_RESTP,RS_RESNO,RS_PRDCD,RS_RESDT,";
			LM_TMPSTR += "RS_REXDT,RS_REQBY,RS_AUTBY,RS_RESCD,RS_RESDS,RS_REMDS,RS_RESQT,";
			LM_TMPSTR += "RS_LUSBY,RS_LUPDT) values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"FG_PTFRF");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_WRHTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_RESTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_RESNO")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_PRDCD")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RS_RESDT"))+"'),";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RS_REXDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_REQBY")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_AUTBY")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_RESCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_RESDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_REMDS")+"',";
				LM_STLSQL += LP_RSLSET.getString("RS_RESQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("RS_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("RS_LUPDT"))+"'))";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_EX){
			showEXMSG(L_EX,"crtRSTRN","");
		}
	}
	
	public static void crtPRMST(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR  = "Insert Into DT_PRMST(PR_PRDCD,PR_PRDDS,PR_UOMCD,PR_CSTQT,PR_USTQT,PR_RSTQT,PR_MOSQT,PR_YOSQT,PR_TBGQT,PR_MBGQT,PR_YBGQT,PR_PINNO,";
			LM_TMPSTR  += "PR_PINDT,PR_PINRT,PR_TRNFL,PR_STSFL,PR_LUSBY,PR_LUPDT,PR_PRDTP,PR_GRDDS)";
			LM_TMPSTR  += " values ("; 
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"CO_PRMST");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_PRDCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_PRDDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_UOMCD")+"',";
				LM_STLSQL += LP_RSLSET.getString("PR_CSTQT")+",";
				LM_STLSQL += LP_RSLSET.getString("PR_USTQT")+",";
				LM_STLSQL += LP_RSLSET.getString("PR_RSTQT")+",";
				LM_STLSQL += LP_RSLSET.getString("PR_MOSQT")+",";
				LM_STLSQL += LP_RSLSET.getString("PR_YOSQT")+",";
				LM_STLSQL += LP_RSLSET.getString("PR_TBGQT")+",";
				LM_STLSQL += LP_RSLSET.getString("PR_MBGQT")+",";
				LM_STLSQL += LP_RSLSET.getString("PR_YBGQT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_PINNO")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("PR_PINDT"))+"'),";
				LM_STLSQL += LP_RSLSET.getString("PR_PINRT")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_STSFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("PR_LUPDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_PRDTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("PR_GRDDS")+"')";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
				}
		}catch(SQLException L_SE){
			showEXMSG(L_SE,"crtPRMST","");
		}
	}
	
	public static void crtCDTRN(ResultSet LP_RSLSET,String LP_SYSCD){
		try{
			LM_TMPSTR = "Insert Into DT_CDTRN(CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_SHRDS,";
			LM_TMPSTR += "CMT_CHP01,CMT_CHP02,CMT_NMP01,CMT_NMP02,CMT_NCSVL,CMT_CCSVL,CMT_MODLS,";
			LM_TMPSTR += "CMT_TRNFL,CMT_LUSBY,CMT_LUPDT,CMT_STSFL) values (";
			chkTBLCOL(LM_TMPSTR,LP_RSLSET,"CO_CDTRN");
			while(LP_RSLSET.next()){
				LM_STLSQL = LM_TMPSTR;
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_CGMTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_CGSTP")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_CODCD")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_CODDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_SHRDS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_CHP01")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_CHP02")+"',";
				LM_STLSQL += LP_RSLSET.getString("CMT_NMP01")+",";
				LM_STLSQL += LP_RSLSET.getString("CMT_NMP02")+",";
				LM_STLSQL += LP_RSLSET.getString("CMT_NCSVL")+",";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_CCSVL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_MODLS")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_TRNFL")+"',";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_LUSBY")+"',";
				LM_STLSQL += "ctod('"+M_fmtDBDAT.format(LP_RSLSET.getDate("CMT_LUPDT"))+"'),";
				LM_STLSQL += "'"+LP_RSLSET.getString("CMT_STSFL")+"')";
				M_strSQLQRY = LM_STLSQL;
				chkUPDCT(LP_SYSCD);
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"crtCDTRN","");
		}
	}
	
	public static void chkTBLCOL(String LP_STRQRY,ResultSet LP_RSLSET,String LP_TBLNM){
		int LM_FLDCNT =0;
		int LM_SPLCNT =0;
		try{
			java.util.StringTokenizer L_STFLD = new java.util.StringTokenizer(LP_STRQRY,",");
			LM_FLDCNT = L_STFLD.countTokens();	
			ResultSetMetaData LM_RSLMDT = LP_RSLSET.getMetaData();		
			LM_SPLCNT = LM_RSLMDT.getColumnCount();
			if((LM_SPLCNT - LM_FLDCNT)>0){
				System.out.println("Column count mismatch in "+LP_TBLNM +" SRCTBL - DESTTBL ="+(LM_SPLCNT - LM_FLDCNT));			
			}
			else if((LM_SPLCNT - LM_FLDCNT)<0){
				System.out.println("Column count mismatch in "+LP_TBLNM +" DESTTBL - SRCTBL ="+(LM_FLDCNT - LM_SPLCNT));			
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"chkTBLCOL","");
		}
	}
	
	public static  String setDTMVFP(String LP_DTMSTR){
		String L_RTNSTR = "";
		try{
			if(LP_DTMSTR != null){
				if(LP_DTMSTR.length() > 0){
					int L_LEN = LP_DTMSTR.trim().length();
					if(L_LEN >=16){
						L_RTNSTR = LP_DTMSTR.substring(5,7)+"/"+ LP_DTMSTR.substring(8,10)+"/"+LP_DTMSTR.substring(0,4);
					    L_RTNSTR += " "+ LP_DTMSTR.substring(11,13)+ ":" + LP_DTMSTR.substring(14,16);
					}
					if(L_RTNSTR.trim().equals("30/12/1899 00:00"))
					   L_RTNSTR = "";
				}
			}
		}catch(Exception L_EX){
			System.out.println("setDTMFMT: "+L_EX);
		}
		return L_RTNSTR;
	}
	
	public static void chkUPDCT(String LP_SYSCD){
		if(cl_dat.M_flgLCUPD_pbst){
			try{
//				cl_dat.exeDTRUPD(M_strSQLQRY,LP_SYSCD,"setLCLUPD");
//				if(cl_dat.M_flgLCUPD_pbst)
//					cl_dat.exeDTCMT(LP_SYSCD);
//				else
//					cl_dat.exeDTRBK(LP_SYSCD);
		}catch(Exception L_EX){
			showEXMSG(L_EX,"chkUPDCT","");
			System.out.println(M_strSQLQRY);
			}
		}
	}
	
	public static void updCDTRN(String LP_CGMTP,String LP_CGSTP,String LP_CODCD,String LP_CURTM,String LP_USRCD){
		cl_dat.M_flgLCUPD_pbst = true;
		try{
			LM_STLSQL = "Update CO_CDTRN set ";
			LM_STLSQL += "CMT_CHP01 = '"+LP_USRCD+"',";
			LM_STLSQL += "CMT_CHP02 = '"+LP_CURTM+"'";
			LM_STLSQL += " where CMT_CGMTP = '"+LP_CGMTP+"'";
			LM_STLSQL += " and CMT_CGSTP = '"+LP_CGSTP+"'";
			LM_STLSQL += " and CMT_CODCD = '"+LP_CODCD+"'";
			M_strSQLQRY = LM_STLSQL;
			cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");                   
//			cl_dat.exeSQLUPD(M_strSQLQRY,"SP","REM","");
/*			if(cl_dat.M_flgLCUPD_pbst){
				cl_dat.exeDBCMT( "");
				cl_dat.exeDBCMT("SP","REM","");			
			}else{
				cl_dat.exeDBRBK("SP","ACT");
				cl_dat.exeDBRBK("SP","REM");			
			}
*/
		cl_dat.exeDBCMT("cl_cust.updCDTRN");
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
	}
	
	/**
	 * @return void
	 */
	public static String ctlSTKQT(){
		String L_STKQT = "";	
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				LM_QUERY = "Select sum(st_stkqt) l_stkqt from fg_stmst";
				LM_QUERY += " where st_stkqt > 0";
				LM_RSLSET = cl_dat.exeSQLQRY(LM_QUERY );
				if(LM_RSLSET.next())
					L_STKQT = LM_RSLSET.getString("l_stkqt");
				if(LM_RSLSET != null)
					LM_RSLSET.close();
		}catch(Exception L_EX){
				System.out.println("ctlSTKQT: "+L_EX);
			}
		}
		if(L_STKQT == null)
			L_STKQT = "0.000";
		return L_STKQT;
	}
	
	/**
	 * @return void
	 */
	public static String ctlUCLQT(){
		String L_UCLQT = "";	
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				LM_QUERY = "Select sum(st_uclqt) l_uclqt from fg_stmst";
				LM_QUERY += " where st_uclqt > 0";
				LM_RSLSET = cl_dat.exeSQLQRY(LM_QUERY );
				if(LM_RSLSET.next())
					L_UCLQT = LM_RSLSET.getString("l_uclqt");
				if(LM_RSLSET != null)
					LM_RSLSET.close();
		}catch(Exception L_EX){
			System.out.println("ctlUCLQT: "+L_EX);
			}
		}
		if(L_UCLQT == null)
			L_UCLQT = "0.000";
		return L_UCLQT;
	}
	
	/**
	 * @return void
	 */
	public static String ctlRCTQT(String LP_DATE){
		String L_RCTQT = "";
		if(cl_dat.M_flgLCUPD_pbst){
		try{
			LM_QUERY = "Select sum(rct_rctqt) l_rctqt from fg_rctrn where";
			LM_QUERY += " rct_rctdt = "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DATE))+" and rct_stsfl='2'";
			LM_RSLSET = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_RSLSET.next())
				L_RCTQT = LM_RSLSET.getString("l_rctqt");
			if(LM_RSLSET != null)
				LM_RSLSET.close();
		}catch(Exception L_EX){
			System.out.println("ctlRCTQT: "+L_EX);
			}
		}
		if(L_RCTQT == null)
			L_RCTQT = "0.000";
		return L_RCTQT;
	}
	
	/**
	 * @return void
	 */
	public static String ctlISSQT(String LP_DATE){
		String L_ISSQT = "";
		if(cl_dat.M_flgLCUPD_pbst){
		try{
			LM_QUERY = "Select sum(ist_issqt) l_issqt from fg_istrn where";
			LM_QUERY += " ist_issdt = "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DATE))+" and ist_stsfl='2'";
			LM_RSLSET = cl_dat.exeSQLQRY(LM_QUERY );
			if(LM_RSLSET.next())
				L_ISSQT = LM_RSLSET.getString("l_issqt");
			if(LM_RSLSET != null)
				LM_RSLSET.close();
		}catch(Exception L_EX){
			System.out.println("ctlISSQT: "+L_EX);
			}
		}
		if(L_ISSQT == null)
			L_ISSQT = "0.000";
		return L_ISSQT;
	}
	
	public static void updNCSVL(String LP_CGMTP,String LP_CGSTP,String LP_CODCD,String LP_NCSVL){
		cl_dat.M_flgLCUPD_pbst = true;
		try{
			LM_STLSQL = "Update CO_CDTRN set ";
			LM_STLSQL += "CMT_NCSVL = "+LP_NCSVL;
			LM_STLSQL += " where CMT_CGMTP = '"+LP_CGMTP+"'";
			LM_STLSQL += " and CMT_CGSTP = '"+LP_CGSTP+"'";
			LM_STLSQL += " and CMT_CODCD = '"+LP_CODCD+"'";
			M_strSQLQRY = LM_STLSQL;
			cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");                   
//			cl_dat.exeSQLUPD(M_strSQLQRY,"SP","REM","");
/*			if(cl_dat.M_flgLCUPD_pbst){
				cl_dat.exeDBCMT( "");
				cl_dat.exeDBCMT("SP","REM","");			
			}else{
				cl_dat.exeDBRBK("SP","ACT");
				cl_dat.exeDBRBK("SP","REM");			
			}
*/
		cl_dat.exeDBCMT("cl_cust.updNCSVL");
		}catch(Exception L_EX){
			System.out.println("updNCSVL: "+L_EX);
		}
	}
	
	public static boolean chkCDTRN(){
		try{
			String L_CHP01 = "";
			String L_CHP02 = "";
			
			LM_STLSQL = "Select CMT_CHP01,CMT_CHP02 FROM CO_CDTRN WHERE CMT_CGMTP = 'DTR'";
			LM_STLSQL += " AND CMT_CGSTP = 'FGXXTOH'";
			LM_STLSQL += " AND CMT_CODCD = '0002'";
			
			LM_RSLSET = cl_dat.exeSQLQRY(LM_STLSQL );
			
			if(LM_RSLSET.next()){
				L_CHP01 = LM_RSLSET.getString("CMT_CHP01").trim();
				L_CHP02 = LM_RSLSET.getString("CMT_CHP02").trim();
				}
				if(LM_RSLSET != null)
					LM_RSLSET.close();
				if(L_CHP01 == null || L_CHP01.equals(""))
					return true;
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
		return false;
	}
	
	/**
	 * @return void
	 * This method updates monthly qty. to 0
	 */
	public static void rstYRMTST(){
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				LM_STLSQL = "Update FG_OPSTK set ";
				LM_STLSQL += "OP_MXRQT = 0,";
				LM_STLSQL += "OP_MRCQT = 0,";
				LM_STLSQL += "OP_MXDQT = 0,";
				LM_STLSQL += "OP_M1DQT = 0,";
				LM_STLSQL += "OP_M2DQT = 0,";
				LM_STLSQL += "OP_M3DQT = 0,";
				LM_STLSQL += "OP_M4DQT = 0,";
				LM_STLSQL += "OP_M5DQT = 0,";
				LM_STLSQL += "OP_M6DQT = 0,";
				LM_STLSQL += "OP_YXRQT = 0,";
				LM_STLSQL += "OP_YRCQT = 0,";
				LM_STLSQL += "OP_YXDQT = 0,";
				LM_STLSQL += "OP_Y1DQT = 0,";
				LM_STLSQL += "OP_Y2DQT = 0,";
				LM_STLSQL += "OP_Y3DQT = 0,";
				LM_STLSQL += "OP_Y4DQT = 0,";
				LM_STLSQL += "OP_Y5DQT = 0,";
				LM_STLSQL += "OP_Y6DQT = 0,";
				LM_STLSQL += "OP_DXRQT = 0,";
				LM_STLSQL += "OP_DRCQT = 0,";
				LM_STLSQL += "OP_DXDQT = 0,";
				LM_STLSQL += "OP_D1DQT = 0,";
				LM_STLSQL += "OP_D2DQT = 0,";
				LM_STLSQL += "OP_D3DQT = 0,";
				LM_STLSQL += "OP_D4DQT = 0,";
				LM_STLSQL += "OP_D5DQT = 0,";
				LM_STLSQL += "OP_D6DQT = 0";
				M_strSQLQRY = LM_STLSQL;
				cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");
				cl_dat.exeDBCMT("cl_cust.rstYRMTST");
			}catch(Exception L_EX){
			showEXMSG(L_EX,"rstYRMTST","");
			}
		}
	}
	
	public static void modRCTQT(String LP_FRMDT,String LP_TODAT,boolean LP_YRFLG,boolean LP_MNFLG,boolean LP_DYFLG,boolean LP_HKDCF){
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				String L_XXRCT = "";
				String L_MNXXX = "";
				String L_YRXXX = "";
				String L_DYXXX = "";
				
				LM_STLSQL = "Select RCT_RCTTP,RCT_WRHTP,RCT_PRDTP,LT_PRDCD,RCT_PKGCT,RCT_PKGTP,";
				LM_STLSQL += "sum(RCT_RCTQT) L_RCTQT from FG_RCTRN,PR_LTMST where RCT_PRDTP = LT_PRDTP";
				LM_STLSQL += " and RCT_LOTNO = LT_LOTNO and RCT_RCLNO = LT_RCLNO and ";
				LM_STLSQL += " RCT_RCTDT between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT));
				LM_STLSQL += " and RCT_STSFL='2' and RCT_RCTTP in ('10','15','30')";
				LM_STLSQL += " group by RCT_RCTTP,RCT_WRHTP,RCT_PRDTP,LT_PRDCD,RCT_PKGCT,RCT_PKGTP";
				LM_STLSQL += " order by RCT_RCTTP,RCT_WRHTP,RCT_PRDTP,LT_PRDCD,RCT_PKGCT,RCT_PKGTP";
				
				LM_RSLSET = cl_dat.exeSQLQRY1(LM_STLSQL );
				
				while(LM_RSLSET.next()){
					LM_TRNTP = LM_RSLSET.getString("RCT_RCTTP");
					LM_WRHTP = LM_RSLSET.getString("RCT_WRHTP");
					LM_PRDTP = LM_RSLSET.getString("RCT_PRDTP");
					L_PRDCD = LM_RSLSET.getString("LT_PRDCD");
					LM_PKGCT = LM_RSLSET.getString("RCT_PKGCT");
					L_PKGTP = LM_RSLSET.getString("RCT_PKGTP");
					LM_TRNQT = LM_RSLSET.getString("L_RCTQT");
					if(LM_TRNTP.equals(LM_FRSRCT) || LM_TRNTP.equals(LM_RPKRCT)){
						if(LP_HKDCF){
							L_YRXXX = "op_yrcqt";
							L_MNXXX = "op_mrcqt";
							L_DYXXX = "op_drcqt";
						}else{
							if(LP_YRFLG)
								L_XXRCT = "op_yrcqt";
							else if(LP_MNFLG)
								L_XXRCT = "op_mrcqt";
							else if(LP_DYFLG)
								L_XXRCT = "op_drcqt";
						}
					}
					else if(LM_TRNTP.equals(LM_SLRRCT)){
						if(LP_HKDCF){
							L_YRXXX = "op_yxrqt";
							L_MNXXX = "op_mxrqt";
							L_DYXXX = "op_dxrqt";
						}else{
							if(LP_YRFLG)
								L_XXRCT = "op_yxrqt";
							else if(LP_MNFLG)
								L_XXRCT = "op_mxrqt";
							else if(LP_DYFLG)
								L_XXRCT = "op_dxrqt";
						}
					}
					if(LM_TRNQT != null)
						addOPSTK(L_XXRCT,L_YRXXX,L_MNXXX,L_DYXXX,LP_HKDCF);							
				}
				if(LM_RSLSET != null)
					LM_RSLSET.close();
			}catch(Exception L_EX){
				showEXMSG(L_EX,"modRCTQT","");
			}
		}
	}
	
	public static void modISSQT(String LP_FRMDT,String LP_TODAT,boolean LP_YRFLG,boolean LP_MNFLG,boolean LP_DYFLG,boolean LP_HKDCF){
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				String L_XXISS = "";
				String L_MNXXX = "";
				String L_YRXXX = "";
				String L_DYXXX = "";
				
				LM_STLSQL = "Select IST_ISSTP,IST_SALTP,IST_WRHTP,IST_PRDTP,IST_PRDCD,IST_PKGCT,";
				LM_STLSQL += "IST_PKGTP,sum(IST_ISSQT) L_ISSQT from FG_ISTRN where";
				LM_STLSQL += " IST_ISSDT between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FRMDT))+" and "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TODAT))+"";	
				LM_STLSQL += " and IST_STSFL='2' and IST_ISSTP in ('10','30')";
				LM_STLSQL += " group by IST_ISSTP,IST_SALTP,IST_WRHTP,IST_PRDTP,IST_PRDCD,IST_PKGCT,IST_PKGTP";
				LM_STLSQL += " order by IST_ISSTP,IST_SALTP,IST_WRHTP,IST_PRDTP,IST_PRDCD,IST_PKGCT,IST_PKGTP";
				
				LM_RSLSET = cl_dat.exeSQLQRY1(LM_STLSQL );
				
				while(LM_RSLSET.next()){
					LM_TRNTP = LM_RSLSET.getString("IST_ISSTP");
					LM_SALTP = LM_RSLSET.getString("IST_SALTP");
					LM_WRHTP = LM_RSLSET.getString("IST_WRHTP");
					LM_PRDTP = LM_RSLSET.getString("IST_PRDTP");
					L_PRDCD = LM_RSLSET.getString("IST_PRDCD");
					LM_PKGCT = LM_RSLSET.getString("IST_PKGCT");
					L_PKGTP = LM_RSLSET.getString("IST_PKGTP");
					LM_TRNQT = LM_RSLSET.getString("L_ISSQT");
					if(LM_TRNTP.equals(LM_DIRDSP)){
						if(LM_SALTP.equals(LM_DOMDSP)){
							if(LP_HKDCF){
								L_YRXXX = "op_y1dqt";
								L_MNXXX = "op_m1dqt";
								L_DYXXX = "op_d1dqt";
						}else{
							if(LP_YRFLG)
								L_XXISS = "op_y1dqt";
							else if(LP_MNFLG)
								L_XXISS = "op_m1dqt";
							else if(LP_DYFLG)
								L_XXISS = "op_d1dqt";
							}
						}
						else if(LM_SALTP.equals(LM_EXPDSP)){
							if(LP_HKDCF){
								L_YRXXX = "op_y2dqt";
								L_MNXXX = "op_m2dqt";
								L_DYXXX = "op_d2dqt";
						}else{
							if(LP_YRFLG)
								L_XXISS = "op_y2dqt";
							else if(LP_MNFLG)
								L_XXISS = "op_m2dqt";
							else if(LP_DYFLG)
								L_XXISS = "op_d2dqt";
							}
						}
						else if(LM_SALTP.equals(LM_DEXDSP)){
							if(LP_HKDCF){
								L_YRXXX = "op_y3dqt";
								L_MNXXX = "op_m3dqt";
								L_DYXXX = "op_d3dqt";
						}else{
							if(LP_YRFLG)
								L_XXISS = "op_y3dqt";
							else if(LP_MNFLG)
								L_XXISS = "op_m3dqt";
							else if(LP_DYFLG)
								L_XXISS = "op_d3dqt";
							}
						}
						else if(LM_SALTP.equals(LM_STFDSP)){
							if(LP_HKDCF){
								L_YRXXX = "op_y5dqt";
								L_MNXXX = "op_m5dqt";
								L_DYXXX = "op_d5dqt";
						}else{
							if(LP_YRFLG)
								L_XXISS = "op_y5dqt";
							else if(LP_MNFLG)
								L_XXISS = "op_m5dqt";
							else if(LP_DYFLG)
								L_XXISS = "op_d5dqt";
							}
						}
						else if(LM_SALTP.equals(LM_FTSDSP)){
							if(LP_HKDCF){
								L_YRXXX = "op_y4dqt";
								L_MNXXX = "op_m4dqt";
								L_DYXXX = "op_d4dqt";
						}else{
							if(LP_YRFLG)
								L_XXISS = "op_y4dqt";
							else if(LP_MNFLG)
								L_XXISS = "op_m4dqt";
							else if(LP_DYFLG)
								L_XXISS = "op_d4dqt";
							}
						}
						else if(LM_SALTP.equals(LM_CPCDSP)){
							if(LP_HKDCF){
								L_YRXXX = "op_y6dqt";
								L_MNXXX = "op_m6dqt";
								L_DYXXX = "op_d6dqt";
						}else{
							if(LP_YRFLG)
								L_XXISS = "op_y6dqt";
							else if(LP_MNFLG)
								L_XXISS = "op_m6dqt";
							else if(LP_DYFLG)
								L_XXISS = "op_d6dqt";
							}
						}
					}
					else if(LM_TRNTP.equals(LM_SLRDSP)){
						if(LP_HKDCF){
							L_YRXXX = "op_yxdqt";
							L_MNXXX = "op_mxdqt";
							L_DYXXX = "op_dxdqt";
					}else{
						if(LP_YRFLG)
							L_XXISS = "op_yxdqt";
						else if(LP_MNFLG)
							L_XXISS = "op_mxdqt";
						else if(LP_DYFLG)
							L_XXISS = "op_dxdqt";
						}
					}
					if(LM_TRNQT != null)
						addOPSTK(L_XXISS,L_YRXXX,L_MNXXX,L_DYXXX,LP_HKDCF);							
				}
				if(LM_RSLSET != null)
					LM_RSLSET.close();
			}catch(Exception L_EX){
				showEXMSG(L_EX,"modISSQT","");
			}
		}
	}
	
	public static void addOPSTK(String LP_XXYYY,String LP_YRXXX,String LP_MNXXX,String LP_DYXXX,boolean LP_HKDCF){
		if(cl_dat.M_flgLCUPD_pbst){
			try{
				LM_STLSQL = "Select count(*) from fg_opstk";
				LM_STLSQL += " where op_wrhtp = '"+LM_WRHTP+"'";
				LM_STLSQL += " and op_prdtp = '"+LM_PRDTP+"'";
				LM_STLSQL += " and op_prdcd = '"+L_PRDCD+"'";
				LM_STLSQL += " and op_pkgct = '"+LM_PKGCT+"'";
				LM_STLSQL += " and op_pkgtp = '"+L_PKGTP+"'";
				if(cl_dat.getRECCNT( LM_STLSQL) > 0)
					updOPSTK(LP_XXYYY,LP_YRXXX,LP_MNXXX,LP_DYXXX,LP_HKDCF);
				else
					insOPSTK(LP_XXYYY,LP_YRXXX,LP_MNXXX,LP_DYXXX,LP_HKDCF);
				M_strSQLQRY = LM_STLSQL;
				cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");
				cl_dat.exeDBCMT("cl_cust.addOPSTK");
			}catch(Exception L_EX){
				showEXMSG(L_EX,"addOPSTK","");
			}
		}
	}
	
	public static void updOPSTK(String LP_XXYYY,String LP_YRXXX,String LP_MNXXX,String LP_DYXXX,boolean LP_HKDCF){
		try{
			LM_STLSQL = "Update FG_OPSTK set ";
			if(LP_HKDCF){
				LM_STLSQL += LP_YRXXX +" = "+ LP_YRXXX + "+" + LM_TRNQT+",";
				LM_STLSQL += LP_MNXXX +" = "+ LP_MNXXX + "+" + LM_TRNQT+",";
				LM_STLSQL += LP_DYXXX +" = "+ LP_DYXXX + "+" + LM_TRNQT+",";
			}else{
				LM_STLSQL += LP_XXYYY +" = "+ LP_XXYYY + "+" + LM_TRNQT+",";
			}
			LM_STLSQL += "OP_LUPDT = "+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			LM_STLSQL += " where op_wrhtp = '"+LM_WRHTP+"'";
			LM_STLSQL += " and op_prdtp = '"+LM_PRDTP+"'";
			LM_STLSQL += " and op_prdcd = '"+L_PRDCD+"'";
			LM_STLSQL += " and op_pkgct = '"+LM_PKGCT+"'";
			LM_STLSQL += " and op_pkgtp = '"+L_PKGTP+"'";
		}catch(Exception L_EX){
			showEXMSG(L_EX,"updOPSTK","");
		}
	}
	
	public static void insOPSTK(String LP_XXYYY,String LP_YRXXX,String LP_MNXXX,String LP_DYXXX,boolean LP_HKDCF){
		try{
			LM_STLSQL = "Insert into FG_OPSTK(OP_WRHTP,OP_PRDTP,OP_PRDCD,OP_PKGCT,";
			if(LP_HKDCF)
				LM_STLSQL += "OP_PKGTP,"+LP_YRXXX+","+LP_MNXXX+","+LP_DYXXX+",OP_LUPDT) values (";	
			else
				LM_STLSQL += "OP_PKGTP,"+LP_XXYYY+",OP_LUPDT) values (";
			LM_STLSQL += "'"+LM_WRHTP+"',";
			LM_STLSQL += "'"+LM_PRDTP+"',";
			LM_STLSQL += "'"+L_PRDCD+"',";
			LM_STLSQL += "'"+LM_PKGCT+"',";
			LM_STLSQL += "'"+L_PKGTP+"',";
			LM_STLSQL += LM_TRNQT+",";
			if(LP_HKDCF){
				LM_STLSQL += LM_TRNQT+",";	
				LM_STLSQL += LM_TRNQT+",";	
			}
			LM_STLSQL += M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+")";
		}catch(Exception L_EX){
			showEXMSG(L_EX,"insOPSTK","");
		}
	}
	
	public static void updDODEL(String LP_DORNO,String LP_DSPDT){
		try{
			int L_CNT = 0;
			double L_UPDQT = 0;
			boolean L_UPDFL = false;
			String L_MKTTP1 = "";
			String L_DORNO1 = "";
			String L_PRDCD1 = "";
			String L_PKGTP1 = "";
			String L_MKTTP = "";
			String L_DORNO = "";
			String L_PRDCD = "";
			String L_PKGTP = "";
			String L_SRLNO = "";
			String L_DSPDT = "";
			double L_DORQT = 0;
			double L_LADQT = 0;
			double L_DTRQT = 0;
			double L_INVQT = 0;
			String L_ADDSTR = "";
			if(LP_DORNO.trim().length() > 0){
				L_ADDSTR += " and dod_dorno in ('"+LP_DORNO+"')";
			}
			if(LP_DSPDT.trim().length() > 0){
				L_ADDSTR += " and dod_dspdt <= "+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DSPDT));
			}
					
			LM_STLSQL = "Select dod_mkttp,dod_dorno,dod_prdcd,dod_pkgtp,dod_srlno,dod_dspdt,";
			LM_STLSQL += "sum(dod_dorqt) dod_dorqt,sum(dod_ladqt) dod_ladqt from mr_dodel where dod_dorqt > dod_ladqt";
			LM_STLSQL += " and dod_stsfl != 'X'"+L_ADDSTR;
			LM_STLSQL += " group by dod_mkttp,dod_dorno,dod_prdcd,dod_pkgtp,dod_srlno,dod_dspdt";
			LM_STLSQL += " order by dod_mkttp,dod_dorno,dod_prdcd,dod_pkgtp,dod_srlno,dod_dspdt";
			LM_RSLSET = cl_dat.exeSQLQRY1(LM_STLSQL );
			while(LM_RSLSET.next()){
				L_MKTTP = LM_RSLSET.getString("dod_mkttp");
				L_DORNO = LM_RSLSET.getString("dod_dorno");
				L_PRDCD = LM_RSLSET.getString("dod_prdcd");
				L_PKGTP = LM_RSLSET.getString("dod_pkgtp");
				L_SRLNO = LM_RSLSET.getString("dod_srlno");
				L_DSPDT = M_fmtLCDAT.format(LM_RSLSET.getDate("dod_dspdt"));
				L_DORQT = LM_RSLSET.getDouble("dod_dorqt");
				L_LADQT = LM_RSLSET.getDouble("dod_ladqt");
				if(!(L_MKTTP+L_DORNO+L_PRDCD+L_PKGTP).equals(L_MKTTP1+L_DORNO1+L_PRDCD1+L_PKGTP1)){
					LM_STLSQL = "Select dot_dorqt,dot_invqt from mr_dotrn where";
					LM_STLSQL += " dot_mkttp='"+L_MKTTP+"' and dot_dorno='"+L_DORNO+"'";
					LM_STLSQL += " and dot_prdcd='"+L_PRDCD+"' and dot_pkgtp='"+L_PKGTP+"'";
					ResultSet L_RSLSET1 = cl_dat.exeSQLQRY2(LM_STLSQL );
					if(L_RSLSET1.next()){
						L_DTRQT = L_RSLSET1.getDouble("dot_dorqt");
						L_INVQT = L_RSLSET1.getDouble("dot_invqt");
					}else{
						System.out.println(L_MKTTP+"     "+L_DORNO+"     "+L_PRDCD+"     "+L_PKGTP);
						L_CNT++;
					}
					if(L_RSLSET1 != null)
						L_RSLSET1.close();
					L_MKTTP1= L_MKTTP;
					L_DORNO1= L_DORNO;
					L_PRDCD1= L_PRDCD;
					L_PKGTP1= L_PKGTP;
					L_UPDFL = true;
				}
				/**
				 * if this condition is true than dod_ladqt in mr_dodel is replaced
				 * with dod_dorqt for retrieved D.O. Ref. No. with status flag not
				 * equal to 'X'
				 */
				if(L_UPDFL){
					if(L_DTRQT == L_INVQT){ 
						LM_STLSQL = "Update MR_DODEL set dod_ladqt=dod_dorqt where";
						LM_STLSQL += " dod_mkttp='"+L_MKTTP+"' and dod_dorno='"+L_DORNO+"'";
						LM_STLSQL += " and dod_prdcd='"+L_PRDCD+"' and dod_pkgtp='"+L_PKGTP+"'";
						cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");
						cl_dat.exeDBCMT("cl_cust.updDODEL");
						L_UPDFL = false;
					}
				}
				/**
				 * if this condition is true than dod_ladqt in mr_dodel is replaced
				 * with dod_dorqt if dot_invqt >= dod_dorqt or replaced by 
				 * dod_dorqt - dot_invqt if dot_invqt < dod_dorqt
				 * for retrieved D.O. Ref. No. with status flag not
				 * equal to 'X'
				 */
				if(L_DTRQT > L_INVQT){
					L_UPDQT =  0;
					if(L_INVQT > 0){
						if(L_LADQT > 0){
							if((L_INVQT-L_DORQT) == L_LADQT){
								L_UPDQT = L_LADQT;
							}else
								L_UPDQT = L_DORQT;
						}else{
							if(L_INVQT > L_DORQT){
								L_UPDQT = L_DORQT;
							}
							else if(L_INVQT <= L_DORQT){
								L_UPDQT = L_INVQT;
							}
						}
						L_INVQT -= L_UPDQT;
						LM_STLSQL = "Update MR_DODEL set dod_ladqt="+String.valueOf(L_UPDQT);
						LM_STLSQL += " where dod_mkttp='"+L_MKTTP+"' and dod_dorno='"+L_DORNO+"'";
						LM_STLSQL += " and dod_prdcd='"+L_PRDCD+"' and dod_pkgtp='"+L_PKGTP+"'";
						LM_STLSQL += " and dod_srlno='"+L_SRLNO+"' and";
						LM_STLSQL += " dod_dspdt = "+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_DSPDT));
						cl_dat.exeSQLUPD(LM_STLSQL ,"setLCLUPD");
						cl_dat.exeDBCMT("cl_cust");
					}
				}
			}
			if(LM_RSLSET != null)
				LM_RSLSET.close();
			System.out.println("No. of records not found in MR_DOTRN are: "+L_CNT);
		}catch(Exception L_EX){
			showEXMSG(L_EX,"updDODEL","");
		}
	}
}
