// This Class transfers the data to EXACT System. (Excise)
// Author				:	Santosh Sawant
// Date of Creation		:	12.10.2002
// Modification Date	:	28.08.2002 
//							by Santosh  (Provision for Rebagging - RB)
// Modification Date	:	29.10.2002 
//							by Santosh  (According to new classes)
//							Product type and From Package Type are added.
// Modification Date	:	17.02.2003 
//							by Santosh (Provision for 'JR','CJ','CS')
// Modification Date	:	22/03/2003
//							Provision to transfer Tankfarm related data to Excise
// Modification Date	:	26/11/2003 by Prashant 
//							Provision for Raw Material 'RM' 
//							Conversion to JDK1.4

import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
public  class cc_exact extends cl_pbase
{
	public static String strSQLQRY;
	public static cc_exact occ_exact;
	static PreparedStatement pstmCHKDATFL,pstmADDDATFL;
	static PreparedStatement pstmCHKDATRM,pstmADDDATRM;
    final static String strSTRTP = "04";
    final static String strEXBGR = "47";
    final static String LM_ADVGR = "48";

	// Method to insert the data into EX_DATFL (24.07.2002  by Santosh)
		
	/*public static void EX_DATFL(String LP_DOCTP,String LP_DOCDT)
	{s
		try
		{
			ResultSet L_rstRSLSET;
			crtPRESTM();		// Method to create the prepared Statements
			
			String L_DOCNO,L_PRDTP,L_LOTNO,L_PKGTP,L_FPKTP;
			String L_FPRCD,L_PRDCD,L_DOCQT,L_DOCDT;
			cl_dat.M_flgLCUPD_pbst = true;
			
			// Get the Lock Date	// Modified by Santosh on 04/02/2003
			//cl_dat.getREFDT();		// cl_dat.ocl_dat.M_REFDT
			String L_LOCDT =  M_fmtLCDAT.format(cl_dat.M_REFDT );
							
			if(LP_DOCTP.equals("RC"))			// Receipt(bagging) data(FG_RCTRN)
			{
				strSQLQRY = "Select RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,";
				strSQLQRY += "RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,";
				strSQLQRY += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT ";
				strSQLQRY += " from FG_RCTRN,PR_LTMST";
				strSQLQRY += " where RCT_RCTDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and RCT_RCTTP in ('10','15') and RCT_STSFL = '2'";
				strSQLQRY += " and RCT_PRDTP = LT_PRDTP ";
				strSQLQRY += " and RCT_LOTNO = LT_LOTNO ";
				strSQLQRY += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,LT_PRDCD,";
				strSQLQRY += " RCT_RCTDT";
				strSQLQRY += " order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_PRDCD,LM_DOCDT";
			}
			else if(LP_DOCTP.equals("JR"))		// Jobwork Receipt Data (FG_RCTRN)
			{
				strSQLQRY = "Select RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,";
				strSQLQRY += "RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,";
				strSQLQRY += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT ";
				strSQLQRY += " from FG_RCTRN,PR_LTMST";
				strSQLQRY += " where RCT_RCTDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and RCT_LOTNO like '999%'";
				strSQLQRY += " and RCT_RCTTP in ('21') and RCT_STSFL = '2'";
				strSQLQRY += " and RCT_PRDTP = LT_PRDTP ";
				strSQLQRY += " and RCT_LOTNO = LT_LOTNO ";
				strSQLQRY += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,LT_PRDCD,";
				strSQLQRY += " RCT_RCTDT";
				strSQLQRY += " order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_PRDCD,LM_DOCDT";
			}
			else if(LP_DOCTP.equals("C1") || LP_DOCTP.equals("C2"))	// From FG_PTFRF
			{
				strSQLQRY = "Select PTF_PTFNO LM_DOCNO,PTF_PRDTP LM_PRDTP,PTF_LOTNO LM_LOTNO,";
				strSQLQRY += "PTF_PKGTP LM_PKGTP,'XX' LM_FPKTP,PTF_OPRCD LM_FPRCD,PTF_PRDCD LM_PRDCD,";
				strSQLQRY += "PTF_PTFDT LM_DOCDT,PTF_PTFCT,sum(PTF_PTFQT) LM_DOCQT";
				strSQLQRY += " from FG_PTFRF";
				strSQLQRY += " where PTF_PTFDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and PTF_PTFCT in ('01','02')";
				strSQLQRY += " and substr(PTF_LOTNO,1,3) <> '999'";
				strSQLQRY += " group by PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_PKGTP,PTF_OPRCD,";
				strSQLQRY += " PTF_PRDCD,PTF_PTFDT,PTF_PTFCT";
				strSQLQRY += " order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPRCD,";
				strSQLQRY += " LM_PRDCD,LM_DOCDT,PTF_PTFCT";
			}
			else if(LP_DOCTP.equals("C3"))					// From FG_PTFRF & FG_ISTRN
			{
				strSQLQRY = "select PTF_PTFNO LM_DOCNO,IST_PRDTP LM_PRDTP,IST_LOTNO LM_LOTNO,";          
				strSQLQRY += "IST_PKGTP LM_PKGTP,'XX' LM_FPKTP,IST_PRDCD LM_FPRCD,'' LM_PRDCD,";
				strSQLQRY += "PTF_PTFDT LM_DOCDT,sum(IST_ISSQT) LM_DOCQT";
				strSQLQRY += " from FG_PTFRF,FG_ISTRN";         
				strSQLQRY += " where PTF_PTFRF = IST_ISSNO";                
				strSQLQRY += " and PTF_PTFDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and PTF_PTFCT = '03'";                               
				strSQLQRY += " group by PTF_PTFNO,IST_PRDTP,IST_LOTNO,IST_PKGTP,";
				strSQLQRY += " IST_PRDCD,PTF_PTFDT";
					
				strSQLQRY += " union select PTF_PTFNO LM_DOCNO,PTF_PRDTP LM_PRDTP,PTF_LOTNO LM_LOTNO,";          
				strSQLQRY += "PTF_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,PTF_PRDCD LM_PRDCD,";
				strSQLQRY += "PTF_PTFDT LM_DOCDT,PTF_PTFQT LM_DOCQT";
				strSQLQRY += " from FG_PTFRF ";
				strSQLQRY += " where PTF_PTFDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and PTF_PTFCT = '03'";                               
				strSQLQRY += " order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPRCD,LM_PRDCD,LM_DOCDT";
				System.out.println(strSQLQRY);
			}
			else if(LP_DOCTP.equals("RB"))			// Rebagging From FG_RCTRN & FG_ISTRN
			{
				strSQLQRY = "select IST_ISSNO LM_DOCNO,IST_PRDTP LM_PRDTP,IST_LOTNO LM_LOTNO,";          
				strSQLQRY += "'XX' LM_PKGTP,IST_PKGTP LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,";
				strSQLQRY += "IST_ISSDT LM_DOCDT,sum(IST_ISSQT) LM_DOCQT";
				strSQLQRY += " from FG_ISTRN,PR_LTMST";         
				strSQLQRY += " where IST_PRDTP = LT_PRDTP ";       
				strSQLQRY += " and IST_LOTNO = LT_LOTNO ";
				strSQLQRY += " and IST_ISSDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and IST_ISSTP = '16' ";
				strSQLQRY += " and IST_STSFL = '2' ";             
                                strSQLQRY += " and (IST_MKTTP <> 'SR' or IST_MKTTP is null) ";             
				strSQLQRY += " group by IST_ISSNO,IST_PRDTP,IST_LOTNO,IST_PKGTP,";
				strSQLQRY += " LT_PRDCD,IST_ISSDT";
				
				strSQLQRY += " union select RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,";          
				strSQLQRY += "RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,";
				strSQLQRY += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT";
				strSQLQRY += " from FG_RCTRN,PR_LTMST";         
				strSQLQRY += " where RCT_PRDTP = LT_PRDTP ";       
				strSQLQRY += " and RCT_LOTNO = LT_LOTNO ";
				strSQLQRY += " and RCT_RCTDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and RCT_RCTTP = '16' ";
				strSQLQRY += " and RCT_STSFL = '2' ";             
                                strSQLQRY += " and (RCT_ISSRF <> 'SR' or RCT_ISSRF is null) ";             
				strSQLQRY += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,";
				strSQLQRY += " LT_PRDCD,RCT_RCTDT";
				
				strSQLQRY += " order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPKTP,";
				strSQLQRY += " LM_FPRCD,LM_PRDCD,LM_DOCDT,LM_DOCQT";
			}
			else if(LP_DOCTP.equals("CJ"))		// Jobwork Classification
			{
				strSQLQRY = "Select PTF_PTFNO LM_DOCNO,PTF_PRDTP LM_PRDTP,PTF_LOTNO LM_LOTNO,";
				strSQLQRY += "PTF_PKGTP LM_PKGTP,'XX' LM_FPKTP,PTF_OPRCD LM_FPRCD,PTF_PRDCD LM_PRDCD,";
				strSQLQRY += "PTF_PTFDT LM_DOCDT,PTF_PTFCT,sum(PTF_PTFQT) LM_DOCQT";
				strSQLQRY += " from FG_PTFRF";
				strSQLQRY += " where PTF_PTFDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and PTF_PTFCT in ('01')";
				strSQLQRY += " and substr(PTF_LOTNO,1,3) = '999'";
				strSQLQRY += " group by PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,PTF_PKGTP,PTF_OPRCD,";
				strSQLQRY += " PTF_PRDCD,PTF_PTFDT,PTF_PTFCT";
				strSQLQRY += " order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPRCD,";
				strSQLQRY += " LM_PRDCD,LM_DOCDT,PTF_PTFCT";
			}
			else if(LP_DOCTP.equals("CS"))		// Sales return (Classified from Receipts)
			{
				strSQLQRY = "Select RCT_RCTNO LM_DOCNO,RCT_PRDTP LM_PRDTP,RCT_LOTNO LM_LOTNO,";          
				strSQLQRY += "RCT_PKGTP LM_PKGTP,'XX' LM_FPKTP,'' LM_FPRCD,LT_PRDCD LM_PRDCD,";
				strSQLQRY += "RCT_RCTDT LM_DOCDT,sum(RCT_RCTQT) LM_DOCQT";
				strSQLQRY += " from FG_RCTRN,PR_LTMST";         
				strSQLQRY += " where RCT_PRDTP = LT_PRDTP ";
				strSQLQRY += " and RCT_LOTNO = LT_LOTNO ";       
				strSQLQRY += " and RCT_RCTDT between " + L_LOCDT;
				strSQLQRY += " and " + cc_dattm.occ_dattm.setDBSDT(LP_DOCDT );
				strSQLQRY += " and RCT_RCTTP in ('30') ";
				strSQLQRY += " and RCT_STSFL = '2' ";             
				strSQLQRY += " group by RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,RCT_PKGTP,";
				strSQLQRY += " LT_PRDCD,RCT_RCTDT";
				strSQLQRY += " order by LM_DOCNO,LM_PRDTP,LM_LOTNO,LM_PKGTP,LM_FPKTP,";
				strSQLQRY += " LM_FPRCD,LM_PRDCD,LM_DOCDT,LM_DOCQT";
			}
//                        System.out.println(strSQLQRY);                 
			L_rstRSLSET = cl_dat.exeSQLQRY(strSQLQRY);			
			
			int L_ROW = 0;
			
			while(L_rstRSLSET.next())
			{
				if(LP_DOCTP.equals("C1") || LP_DOCTP.equals("C2"))
				{
					if(L_rstRSLSET.getString("PTF_PTFCT").equals("01"))	
						LP_DOCTP = "C1";
					else
						LP_DOCTP = "C2";
				}
					
				L_DOCNO = L_rstRSLSET.getString("LM_DOCNO").trim();
				L_PRDTP = L_rstRSLSET.getString("LM_PRDTP").trim(); 
				L_LOTNO = L_rstRSLSET.getString("LM_LOTNO").trim();
				L_PKGTP = L_rstRSLSET.getString("LM_PKGTP").trim();
				L_FPKTP = L_rstRSLSET.getString("LM_FPKTP").trim();
				
				if(isUNIQUE(LP_DOCTP,L_DOCNO,L_PRDTP,L_LOTNO,L_PKGTP,L_FPKTP))
				{
					L_FPRCD = L_rstRSLSET.getString("LM_FPRCD");
					L_PRDCD = L_rstRSLSET.getString("LM_PRDCD");
					L_DOCQT = L_rstRSLSET.getString("LM_DOCQT");
					L_DOCDT = cc_dattm.setDATE("DMY",L_rstRSLSET.getDate("LM_DOCDT"));
					L_DOCDT = L_DOCDT.substring(6) + "-" + L_DOCDT.substring(3,5) + "-" + L_DOCDT.substring(0,2);
					
					pstmADDDATFL.setString(1,LP_DOCTP);
					pstmADDDATFL.setString(2,L_DOCNO);
					pstmADDDATFL.setString(3,L_PRDTP);
					pstmADDDATFL.setString(4,L_LOTNO);
					pstmADDDATFL.setString(5,L_PKGTP);
					pstmADDDATFL.setString(6,L_FPKTP);
					pstmADDDATFL.setString(7,L_FPRCD);
					pstmADDDATFL.setString(8,L_PRDCD);
					pstmADDDATFL.setString(9,L_DOCQT);
					pstmADDDATFL.setDate(10,java.sql.Date.valueOf(L_DOCDT));
					pstmADDDATFL.setString(11,"");
					pstmADDDATFL.addBatch();
					L_ROW++;
				}
			}
			
			if(L_rstRSLSET != null)
				L_rstRSLSET.close();
			
			if(L_ROW > 0)
			{
				int rows[] = pstmADDDATFL.executeBatch();
				if(rows.length == L_ROW)
					cl_dat.ocl_dat.exeDBCMT("SP","ACT","");
				else
					cl_dat.ocl_dat.exeDBRBK("SP","ACT");
			}
		}catch(Exception L_EX){
			System.out.println(L_EX + "EX_DATFL");
		}
	}*/
	

	public static boolean isUniqueRM(String P_strMATTP,String P_strDOCTP,String P_strDOCNO,String P_strMATCD)
	{
		ResultSet L_rstRSLSET;
		String L_strSQLQRY;
		try
		{
			pstmCHKDATRM.setString(1,P_strMATTP);
			pstmCHKDATRM.setString(2,P_strDOCTP);
			pstmCHKDATRM.setString(3,P_strDOCNO);
			pstmCHKDATRM.setString(4,P_strMATCD);
			L_rstRSLSET = pstmCHKDATRM.executeQuery();
			if(L_rstRSLSET.next())
			{
				L_rstRSLSET.close();
				return false;
			}
		}catch(Exception L_EX)
		{
			System.out.println("Error in isUniqueRM "+L_EX);
		}
		return true;
	}
	// Check whether data is already there (24.07.2002  by Santosh)
	// Modified on 28.08.02 by Santosh 	
	public static boolean isUNIQUE(String LP_DOCTP,String LP_DOCNO,String LP_PRDTP,String LP_LOTNO,String LP_PKGTP,String LP_FPKTP) 
	{	
		ResultSet L_rstRSLSET1;
		String LM_STRQRY;
		try
		{
			pstmCHKDATFL.setString(1,LP_DOCTP);
			pstmCHKDATFL.setString(2,LP_DOCNO);
			pstmCHKDATFL.setString(3,LP_PRDTP);
			pstmCHKDATFL.setString(4,LP_LOTNO);
			pstmCHKDATFL.setString(5,LP_PKGTP);
			pstmCHKDATFL.setString(6,LP_FPKTP);
			
			L_rstRSLSET1 = pstmCHKDATFL.executeQuery();
			if(L_rstRSLSET1.next())
			{
				L_rstRSLSET1.close();
				return false;
			}
		}catch(Exception L_EX)
		{
			System.out.println("Error in isUNIQUE : " + L_EX.toString());
		}
		return true;
	}
	
	// Method to create the prepared Statements
	
	private void crtPRESTM()
	{
		try
		{
			// Prepared Statement to check whether record already exists in EX_DATFL or not 
			pstmCHKDATFL = cl_dat.M_conSPDBA_pbst.prepareStatement(
							"Select * from EX_DATFL where DT_DOCTP = ? " +
							" and DT_DOCNO = ? and DT_PRDTP = ? and DT_LOTNO = ?" +
							" and DT_PKGTP = ? and DT_FPKTP = ?"
							);
			
			// Prepared Statement to check whether record already exists in EX_DATRM or not 
			pstmCHKDATRM = cl_dat.M_conSPDBA_pbst.prepareStatement(
							"Select * from EX_DATRM where DT_MATTP = ?"+
							" and DT_DOCTP = ?"+
							" and DT_DOCNO = ?"+
							" and DT_MATCD = ?"
							);
			
			// Prepared statement to insert record into EX_DATFL
			pstmADDDATFL = cl_dat.M_conSPDBA_pbst.prepareStatement(
							"Insert into EX_DATFL (DT_DOCTP,DT_DOCNO,DT_PRDTP,DT_LOTNO," +
							"DT_PKGTP,DT_FPKTP,DT_FPRCD,DT_PRDCD,DT_DOCQT,DT_DOCDT," +					
							"DT_UPDFL) values (?,?,?,?,?,?,?,?,?,?,?)"		 
							);
			
			// Prepared statement to insert record into EX_DATRM
			pstmADDDATRM = cl_dat.M_conSPDBA_pbst.prepareStatement(
							"Insert into EX_DATRM (DT_MATTP,DT_DOCTP,DT_DOCNO,DT_DOCDT," +
							"DT_MATCD,DT_VENCD,DT_IMPFL,DT_INVNO,DT_INVDT,DT_INVQT,DT_INVRT," +	
							"DT_INVVL,DT_BEDRT,DT_BEDIV,DT_DOCQT," +
							"DT_UPDFL) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"		 
							);
		}catch(Exception e)
		{
			System.out.println("Error in crtPRESTM : " + e.toString());
		}
	}

	// Method to send the Tankform data to Excise Department
	// Modified on 26.11.2003 by Prashant  
	/*public static boolean setTNKDATA(Hashtable P_hstDATRM,String P_strKEYVL,String[] P_staCOLNM,String[] P_staCOLVL) throws Exception
	{
		if(P_staCOLNM.length!=P_staCOLVL.length)
			throw new Exception("Mismatch in col number and values");
		if(!P_hstDATRM.containsKey(P_strKEYVL))
		{
			String L_strQURY= "insert into EX_DATRM (";
			L_strQURY=P_staCOLNM[0];
			for (int i=1;i<P_staCOLNM.length;i++)
				L_strQURY+=","+L_staCOLNM[i];
			L_strQURY+=") values ("+P_staCOLVL[0];
			for (int i=1;i<P_staCOLVL.length;i++)
				L_strQURY+=","+P_staCOLVL[i];
			L_strQURY+=")";
			cl_dat.exeSQLUPD(L_strQURY,"setLCLUPD");
			if(cl_dat.exeDBCMT("setTNKDATA"))
				P_hstDATRM.put(P_strKEYVL,P_staCOLVL[3]);
		}
	}*/
	public void setTNKDATA(String P_strDOCTP,String P_strSTRTP,String P_strDOCDT)
	{
		try
		{
			ResultSet L_rstRSLSET=null;
			crtPRESTM();
			String L_strINVNO="",L_strINVDT="",L_strVENCD="",L_strVENNM="",L_strVENTP="";
			String L_strMATCD="",L_strMATNM="",L_strINVQT="",L_strINVVL="",L_strINVRT="";
			String L_strBEDPR="",L_strBEDVL="",L_strDOCNO="",L_strDOCQT="",L_strCONNO="";
			String L_strPORNO="",L_strMATTP="",L_strDOCDT="",L_strTEMP="";
			float L_fltTRECQT,L_fltTBOEQT,L_fltTACCVL;
			float L_fltTDUTPR,L_fltTDUTVL,L_fltTPORRT;
			int L_intSRLNO=0;
			if(P_strDOCTP.equals("RC"))
			{
				strSQLQRY = "Select GR_BOENO,GR_VENCD,GR_VENNM,GR_MATCD,GR_GRNDT,";
				strSQLQRY += " GR_GRNNO,GR_RECQT,GR_PORNO,BE_BOEQT,BE_ACCVL,";
				strSQLQRY += " BE_DUTPR,BE_DUTVL,BE_CONNO,BE_BOENO,BE_BOEDT,BE_MATTP,";
				strSQLQRY += " (BE_ACCVL/BE_BOEQT) L_RATE,CT_MATDS From CO_CTMST,";
				strSQLQRY += " MM_GRMST Left Outer Join MM_BETRN";
				strSQLQRY += " On GR_STRTP = BE_STRTP";
				strSQLQRY += " And GR_BOENO = BE_BOENO";
				strSQLQRY += " And GR_PORNO = BE_PORNO";
				strSQLQRY += " And GR_MATCD = BE_MATCD";
				strSQLQRY += " Where GR_GRNDT = '"+P_strDOCDT+"'";
				strSQLQRY += " And GR_STRTP = '"+strSTRTP+"'";	//Raw Material && Furnace Min. Oil
				strSQLQRY += " And GR_GRNTP = '"+strEXBGR+"'";		//Exbonded
				strSQLQRY += " And GR_MATCD = CT_MATCD";
				strSQLQRY += " And ifnull(GR_STSFL,'') <> 'X'";
				strSQLQRY += " And ifnull(BE_STSFL,'') <> 'X'";
				strSQLQRY += " And ifnull(CT_STSFL,'') <> 'X'";
				strSQLQRY += " Order by GR_BOENO";
								
				L_rstRSLSET = cl_dat.exeSQLQRY(strSQLQRY);
					
				//L_intSRLNO = 0;
				while(L_rstRSLSET.next())
				{
					L_strINVNO = nvlSTRVL(L_rstRSLSET.getString("BE_BOENO"),"").trim();
					L_strVENCD = nvlSTRVL(L_rstRSLSET.getString("GR_VENCD"),"").trim();
					L_strVENNM = nvlSTRVL(L_rstRSLSET.getString("GR_VENNM"),"").trim();
					L_strMATCD = nvlSTRVL(L_rstRSLSET.getString("GR_MATCD"),"").trim();
					L_strDOCDT = M_fmtLCDAT.format(L_rstRSLSET.getDate("GR_GRNDT"));//*/*/dd/mm/yyyy
					L_strDOCNO = nvlSTRVL(L_rstRSLSET.getString("GR_GRNNO"),"").trim();
					L_strDOCQT = nvlSTRVL(L_rstRSLSET.getString("GR_RECQT"),"").trim();
					L_strPORNO = nvlSTRVL(L_rstRSLSET.getString("GR_PORNO"),"").trim();
					L_strINVQT = nvlSTRVL(L_rstRSLSET.getString("BE_BOEQT"),"").trim();
					L_strINVVL = nvlSTRVL(L_rstRSLSET.getString("BE_ACCVL"),"").trim();
					L_strBEDPR = nvlSTRVL(L_rstRSLSET.getString("BE_DUTPR"),"").trim();
					L_strBEDVL = nvlSTRVL(L_rstRSLSET.getString("BE_DUTVL"),"").trim();
					L_strCONNO = nvlSTRVL(L_rstRSLSET.getString("BE_CONNO"),"").trim();
					L_strMATTP = nvlSTRVL(L_rstRSLSET.getString("BE_MATTP"),"").trim();
					
					if(nvlSTRVL(L_rstRSLSET.getString("BE_BOEDT"),"").equals(""))
						L_strINVDT="";
					else
						L_strINVDT = M_fmtLCDAT.format(L_rstRSLSET.getDate("BE_BOEDT"));
					
					L_strINVRT = nvlSTRVL(L_rstRSLSET.getString("L_RATE"),"").trim();
					L_strMATNM = nvlSTRVL(L_rstRSLSET.getString("CT_MATDS"),"").trim();
					
					SimpleDateFormat L_fmtDBDAT=new SimpleDateFormat("yyyy-MM-dd");
					L_strDOCDT = L_fmtDBDAT.format(M_fmtLCDAT.parse(L_strDOCDT));//yyyy/mm/dd
					if(L_strINVDT.equals(""))
						L_strINVDT="";
					else
						L_strINVDT = L_fmtDBDAT.format(M_fmtLCDAT.parse(L_strINVDT));//yyyy/mm/dd
					if(L_strMATTP.length()>0 && P_strDOCTP.length()>0 && L_strDOCNO.length()>0 && L_strMATCD.length()>0)
					{
						if(isUniqueRM(L_strMATTP,P_strDOCTP,L_strDOCNO,L_strMATCD))
						{	
							pstmADDDATRM.setString(1,L_strMATTP.equals("") ? null : P_strSTRTP);
							pstmADDDATRM.setString(2,P_strDOCTP.equals("") ? null : P_strDOCTP);
							pstmADDDATRM.setString(3,L_strDOCNO.equals("") ? null : L_strDOCNO);
							pstmADDDATRM.setDate(4,L_strDOCDT.equals("") ? null : java.sql.Date.valueOf(L_strDOCDT));
							pstmADDDATRM.setString(5,L_strMATCD.equals("") ? null : L_strMATCD);
							pstmADDDATRM.setString(6,L_strVENCD.equals("") ? null : L_strVENCD);
							pstmADDDATRM.setString(7,"N");
							pstmADDDATRM.setString(8,L_strINVNO.equals("") ? null : L_strINVNO);
							pstmADDDATRM.setDate(9,L_strINVDT.equals("") ? null : java.sql.Date.valueOf(L_strINVDT));
							pstmADDDATRM.setString(10,L_strINVQT.equals("") ? null : L_strINVQT);
							pstmADDDATRM.setString(11,L_strINVRT.equals("") ? null : L_strINVRT);
							pstmADDDATRM.setString(12,L_strINVVL.equals("") ? null : L_strINVVL);
							pstmADDDATRM.setString(13,L_strBEDPR.equals("") ? null : L_strBEDPR);
							pstmADDDATRM.setString(14,L_strBEDVL.equals("") ? null : L_strBEDVL);
							pstmADDDATRM.setString(15,L_strDOCQT.equals("") ? null : L_strDOCQT);
							pstmADDDATRM.setString(16,"1");
							pstmADDDATRM.addBatch();
							L_intSRLNO++;
						}
					}
				}//end while
			}//end "RC"
			else if(P_strDOCTP.equals("IS"))
			{
				M_strSQLQRY = "Select IS_STRTP,IS_ISSNO,IS_MATCD,IS_MATTP,";
				M_strSQLQRY += "IS_ISSDT,IS_ISSQT,CT_MATDS From MM_ISMST,";
				M_strSQLQRY += "CO_CTMST Where IS_ISSDT = '"+P_strDOCDT+"'";
				M_strSQLQRY += "And IS_ISSTP = '"+strEXBGR+"'";
				M_strSQLQRY += "And IS_STRTP = '"+strSTRTP+"'";
				M_strSQLQRY += "And IS_MATCD = CT_MATCD";
				L_rstRSLSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				//L_intSRLNO = 0;
				while(L_rstRSLSET.next())
				{
					L_strMATCD = nvlSTRVL(L_rstRSLSET.getString("IS_MATCD"),"").trim();
					L_strDOCNO = nvlSTRVL(L_rstRSLSET.getString("IS_ISSNO"),"").trim();
					L_strDOCDT = M_fmtLCDAT.format(L_rstRSLSET.getDate("IS_ISSDT"));
					L_strDOCQT = nvlSTRVL(L_rstRSLSET.getString("IS_ISSQT"),"").trim();
					L_strMATTP = nvlSTRVL(L_rstRSLSET.getString("IS_MATTP"),"").trim();
					L_strMATNM = nvlSTRVL(L_rstRSLSET.getString("CT_MATDS"),"").trim();
					SimpleDateFormat L_fmtDBDAT=new SimpleDateFormat("yyyy-MM-dd");
					L_strDOCDT = L_fmtDBDAT.format(M_fmtLCDAT.parse(L_strDOCDT));//mm/dd/yyyy
					if(L_strMATTP.length()>0 && P_strDOCTP.length()>0 && L_strDOCNO.length()>0 && L_strMATCD.length()>0)
					{
						if(isUniqueRM(L_strMATTP,P_strDOCTP,L_strDOCNO,L_strMATCD))
						{	
							pstmADDDATRM.setString(1,L_strMATTP.equals("") ? null : P_strSTRTP);
							pstmADDDATRM.setString(2,P_strDOCTP.equals("") ? null : P_strDOCTP);
							pstmADDDATRM.setString(3,L_strDOCNO.equals("") ? null : L_strDOCNO);
							pstmADDDATRM.setDate(4,L_strDOCDT.equals("") ? null : java.sql.Date.valueOf(L_strDOCDT));
							pstmADDDATRM.setString(5,L_strMATCD.equals("") ? null : L_strMATCD);
							pstmADDDATRM.setString(6,L_strVENCD.equals("") ? null : L_strVENCD);
							pstmADDDATRM.setString(7,"N");
							pstmADDDATRM.setString(8,L_strINVNO.equals("") ? null : L_strINVNO);
							pstmADDDATRM.setDate(9,L_strINVDT.equals("") ? null : java.sql.Date.valueOf(L_strINVDT));
							pstmADDDATRM.setString(10,L_strINVQT.equals("") ? null : L_strINVQT);
							pstmADDDATRM.setString(11,L_strINVRT.equals("") ? null : L_strINVRT);
							pstmADDDATRM.setString(12,L_strINVVL.equals("") ? null : L_strINVVL);
							pstmADDDATRM.setString(13,L_strBEDPR.equals("") ? null : L_strBEDPR);
							pstmADDDATRM.setString(14,L_strBEDVL.equals("") ? null : L_strBEDVL);
							pstmADDDATRM.setString(15,L_strDOCQT.equals("") ? null : L_strDOCQT);
							pstmADDDATRM.setString(16,"1");
							pstmADDDATRM.addBatch();
							L_intSRLNO++;
						}
					}
				}//end while
			}//end "IS"
			if(L_rstRSLSET != null)
				L_rstRSLSET.close();
			if(L_intSRLNO > 0)
			{
				int rows[] = pstmADDDATRM.executeBatch();
				if(rows.length == L_intSRLNO)
					cl_dat.exeDBCMT("setTNKDATA");
			}
		}catch(Exception e)
		{
			System.out.println("Error in setTNKDATA : " + e.toString());
		}
	}
}
