/*
System Name    : Finished Goods Inventory Management System
Program Name   : Maximum Stock Summary
Program Desc.  : Report related to maximum stock is displayed in gradewise fashion
Author         : Mr. Zaheer Alam Khan
Date           : 03-11-2006
Version        : FIMS 2.0
Modificaitons  :
Modified By    :
Modified Date  : 
Modified det.  :
Version        :

*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.lang.*;
import java.util.*;
import java.util.Properties;
import java.util.Date; 
import java.io.*; 

public class fg_rpmax extends cl_rbase 
{
	
	Hashtable<String,String> hstMTHNM;
	String strFILNM = cl_dat.M_strREPSTR_pbst;
	String strFILNM1 = ""; 
	FileOutputStream fosREPORT;
    DataOutputStream dosREPORT;
	
	String strREFDT,strISODCA,strISODCB,strISODCC,strMGRDS;
	String strSGRDS,strPRMGR,strRPHDR,strPRSGR,strCCSVL,strMONTH,strDAY;
	
	
	String strGRTOT = "";
	String strSGTOT = "";
	String strMGTOT = "";
	String strCGTOT = "";
	
	String strPRMGR1 = "";
	String strPRSGR1 = "";
	
	String strPRMGR2 = "";
	String strPRSGR2 = "";
	
	String staGRTOT[];
	String staPRSGR[];
	String staPRMGR[];
	String staCGTOT[];
	
	StringBuffer strPRINT;
	int intLMRGN=0;
	int intCOUNT,k,l;
	double dblDSTQT = 0;
	private JCheckBox chkXPS;	
												 /** Hashtable for storing different Main Product Types*/
	private Hashtable<String,String> hstMNGRP = new Hashtable<String,String>();/** Hashtable for storing different Sub Product Types*/
	private Hashtable<String,String> hstSBGRP = new Hashtable<String,String>();
	private Hashtable<String,String> hstGENCD = new Hashtable<String,String>();
	boolean flgHDRFL = true;
	fg_rpmax()
	{
		super(2);
		setMatrix(20,8);
		try
		{	
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			chkXPS = new JCheckBox("XPS Report");
			add(chkXPS,3,2,1,2,this,'L');
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				L_rstRSSET.close();
			}
			strDAY = cl_dat.M_strLOGDT_pbst.substring(0,2).toString().trim();
			strMONTH = cl_dat.M_strLOGDT_pbst.substring(3,5).toString().trim();
			strFILNM1 = strFILNM.concat("\\fgmx"+strDAY+strMONTH+".doc"); 

			hstMTHNM = new Hashtable<String,String>();
			hstMTHNM.put("01","January");
			hstMTHNM.put("02","February");
			hstMTHNM.put("03","March");
			hstMTHNM.put("04","April");
			hstMTHNM.put("05","May");
			hstMTHNM.put("06","June");
			hstMTHNM.put("07","July");
			hstMTHNM.put("08","August");
			hstMTHNM.put("09","September");
			hstMTHNM.put("10","October");
			hstMTHNM.put("11","November");
			hstMTHNM.put("12","December");
			M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ( 'ISOFGXXRPT','M"+cl_dat.M_strCMPCD_pbst+"COXXSHF','MSTCOXXPGR','SYSFGXXPKG','SYSPRXXCYL')";
			M_strSQLQRY += " and ISNULL(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)   
			{
				while(M_rstRSSET.next())
				{   
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("MG"))
						hstMNGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,2),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("SG"))
						hstSBGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,4),M_rstRSSET.getString("CMT_CODDS"));
					else
						hstGENCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
			}
			strISODCA = hstGENCD.get("FG_RPRCM01").toString();
			strISODCB = hstGENCD.get("FG_RPRCM02").toString();
			strISODCC = hstGENCD.get("FG_RPRCM03").toString();
			M_pnlRPFMT.setVisible(true);
			setENBL(true);
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX.toString());
		}	
	}
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
	}
	
	public void exePRINT()
	{
		try
		{
			if(M_rdbHTML.isSelected())
		        strFILNM1 = cl_dat.M_strREPSTR_pbst +"fgmx"+strDAY+strMONTH+".html"; 
		    else if(M_rdbTEXT.isSelected())
		        strFILNM1 = cl_dat.M_strREPSTR_pbst + "fgmx"+strDAY+strMONTH+".doc"; 
			getALLREC();
			dosREPORT.close();
			fosREPORT.close();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
			    doPRINT(strFILNM1);
				else 
		        {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM1); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM1); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM1); 
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{					
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM1,"Party Ledger"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		System.out.println("IN ACTION P");
	}
	/**
	 * @return void
	 * Gets the Header of the Report when the report is displayed or printed 
	 * for the first time.
	*/
	private void prnHEADER()
	{
		try
		{
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"",106));
			crtLINE(31);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISODCA,137));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISODCB,137));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISODCC,137));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",106));
			crtLINE(31);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",25));
			dosREPORT.writeBytes(padSTRING('L',"Report Date :"+cl_dat.M_strLOGDT_pbst,112));
			dosREPORT.writeBytes("\n");
			String L_strMAXDT="";
			M_strSQLQRY ="select  CMT_CCSVL from CO_CDTRN where CMT_CGMTP = 'S"+cl_dat.M_strCMPCD_pbst+"' " ;
			M_strSQLQRY += " AND CMT_CGSTP = 'FGXXREF' AND CMT_CODCD = 'MAXDT'";
			System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET.next())
				L_strMAXDT = L_rstRSSET.getString("CMT_CCSVL");
			L_rstRSSET.close();
					
			String L_strMONTH = L_strMAXDT.substring(3,5).trim();
			String L_strYEAR  = L_strMAXDT.substring(6,10).trim();
			L_strMONTH = hstMTHNM.get(L_strMONTH).toString().trim();
			dosREPORT.writeBytes(padSTRING('R',"Maximum Stock for the Month : "+L_strMONTH.substring(0,3)+ "."+L_strYEAR+"    Stock Date: "+L_strMAXDT ,75));
			dosREPORT.writeBytes(padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,53));
			dosREPORT.writeBytes("\n");
			crtLINE(138);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R'," ",6));
			dosREPORT.writeBytes(padSTRING('R',"Category",22));
			dosREPORT.writeBytes(padSTRING('L',"W/H I",18));
			dosREPORT.writeBytes(padSTRING('L',"W/H II",18));
			dosREPORT.writeBytes(padSTRING('L',"SPS",18));
			dosREPORT.writeBytes(padSTRING('L',"XPS",18));
			dosREPORT.writeBytes(padSTRING('L',"EPS",18));
			dosREPORT.writeBytes(padSTRING('L',"Scrap Yard",18));
			dosREPORT.writeBytes(padSTRING('L',"Total Qty.",20));
			dosREPORT.writeBytes("\n");
			crtLINE(138);
			dosREPORT.writeBytes("\n");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			cl_dat.M_intLINNO_pbst += 11;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	
	private void crtLINE(int LP_CNT)
	{
		String strln = "";
		try
		{
			for(int i=1;i<=LP_CNT;i++)
			{
				strln += "-";
			}
			dosREPORT.writeBytes(padSTRING('L'," ",intLMRGN));
			dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}
	
	public void getALLREC()
	{
		try
		{
			setMSG("Report Generation in Progress....",'N');
			fosREPORT = new FileOutputStream(strFILNM1);
			dosREPORT = new DataOutputStream(fosREPORT);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Maximum Stock </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 69;
			intCOUNT = 6;
			staGRTOT = new String[12];
			strGRTOT = "0";
			for(l = 1;l <= intCOUNT;l++)
				staGRTOT[l] = "0";
			
			strRPHDR = "Regular :";
			System.out.println("001");
			getALLREC(" and SUBSTRING(ST_LOTNO,1,3) <> '999' "); //fetches record for Regular Receipts
			strRPHDR = "Job Work :";
			getALLREC(" and SUBSTRING(ST_LOTNO,1,3) = '999' "); //fetches record for Job Work Receipts
			prnFOOTR();
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += "CMT_CHP01 = '"+strREFDT+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'S"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'FGXXREF'";
			M_strSQLQRY += " and CMT_CODCD = 'MAXDT'";
			
			System.out.println("UPDATE = "+M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Data Updated successfully",'N');
				}
			}
			else
			{
				setMSG("Error in updating",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}

	private void getALLREC(String LP_CNDSTR)    // Fetches all the records
	{
		try
		{
			M_rstRSSET = null;
			staPRSGR = new String[12];
			staPRMGR = new String[12];
			staCGTOT = new String[12];
			
			strSGTOT = "0";
			strMGTOT = "0";
			strCGTOT = "0";
			
			strPRMGR1 = "";  // Previous Product Type
			strPRSGR1 = "";  // Previous Product Sub Type
			
			strPRMGR2 = "";  // Previous Product Type
			strPRSGR2 = "";  // Previous Product Sub Type
			
			strPRMGR = "";
			strPRSGR = "";
			strCCSVL = "";
			for(l = 1;l <= intCOUNT;l++)
			{
				staPRSGR[l] = "0";
				staPRMGR[l] = "0";
				staCGTOT[l] = "0";
			}

			M_strSQLQRY = "Select  SUBSTRING(ST_PRDCD,1,2) LM_PRDTP, SUBSTRING(ST_PRDCD,1,4) LM_SUBPD,";
			M_strSQLQRY += "CMT_CCSVL,sum(ST_MAXQT) L_MAXQT from FG_STMST,CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='FGXXMNL'  ";
			M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and   SUBSTRING(ST_PRDCD,1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'")+" AND  SUBSTRING(ST_MNLCD,1,1)=CMT_CODCD" + LP_CNDSTR;
			M_strSQLQRY += " group by  SUBSTRING(ST_PRDCD,1,2), SUBSTRING(ST_PRDCD,1,4),CMT_CCSVL";
			M_strSQLQRY += " order by  LM_PRDTP,LM_SUBPD,CMT_CCSVL";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			boolean L_1STFL = true;
			int i = 0;
			boolean LM_EOF = false;
			flgHDRFL = true;
			if(M_rstRSSET != null)
			{
				M_rstRSSET.next();
				while(!LM_EOF)
				{
					strCCSVL = M_rstRSSET.getString("CMT_CCSVL").trim();
					strPRSGR = M_rstRSSET.getString("LM_SUBPD").trim();
					strPRMGR = M_rstRSSET.getString("LM_PRDTP").trim();
					dblDSTQT = M_rstRSSET.getDouble("L_MAXQT");
					if(cl_dat.M_intLINNO_pbst >= 68)
					{
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						prnHEADER(); //gets the Header of the report
					}
					if(L_1STFL)
					{
						strPRSGR1 = strPRSGR;
						strPRMGR1 = strPRMGR;
						strPRSGR2 = strPRSGR;
						strPRMGR2 = strPRMGR;
						L_1STFL = false;
					}
					prnGRPHDR("MG",4);
					strPRMGR1 = strPRMGR;
					while((strPRMGR).equals(strPRMGR1) && !LM_EOF)
					{
						strPRMGR = strPRMGR2;
						strPRMGR1 = strPRMGR;
						while((strPRMGR+strPRSGR).equals(strPRMGR1+strPRSGR1) && !LM_EOF)
						{
							int L_intCCSVL = Integer.parseInt(strCCSVL);
							staPRSGR[L_intCCSVL] = setNumberFormat(Double.parseDouble(staPRSGR[L_intCCSVL])+dblDSTQT,3);
							staPRMGR[L_intCCSVL] = setNumberFormat(Double.parseDouble(staPRMGR[L_intCCSVL])+dblDSTQT,3);
							staCGTOT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staCGTOT[L_intCCSVL])+dblDSTQT,3);
							staGRTOT[L_intCCSVL] = setNumberFormat(Double.parseDouble(staGRTOT[L_intCCSVL])+dblDSTQT,3);
								
							strSGTOT = setNumberFormat(Double.parseDouble(strSGTOT)+dblDSTQT,3);
							strMGTOT = setNumberFormat(Double.parseDouble(strMGTOT)+dblDSTQT,3);
							strCGTOT = setNumberFormat(Double.parseDouble(strCGTOT)+dblDSTQT,3);
							strGRTOT = setNumberFormat(Double.parseDouble(strGRTOT)+dblDSTQT,3);
							if (!M_rstRSSET.next())
							{
								LM_EOF = true;
								break;
							}
										
							strPRMGR2 = M_rstRSSET.getString("LM_PRDTP").trim();
							strPRSGR2 = M_rstRSSET.getString("LM_SUBPD").trim();
							dblDSTQT = M_rstRSSET.getDouble("L_MAXQT");
							strCCSVL = M_rstRSSET.getString("CMT_CCSVL").trim();
							strPRSGR = strPRSGR2;
							strPRMGR = strPRMGR2;
						}
						prnGRPTOT("SG",strSGTOT,"N");
						intGRPTOT("SG");
					}
					prnGRPTOT("MG",strMGTOT,"B");
					intGRPTOT("MG");
				}
				M_rstRSSET.close();
				if(cl_dat.M_intLINNO_pbst >= 61)
					prnFMTCHR(dosREPORT,M_strEJT);
				dosREPORT.writeBytes ("\n");
				prnGRPTOT("CG",strCGTOT,"B");
				intGRPTOT("CG");
				cl_dat.M_intLINNO_pbst += 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC(String Argu)");
		}
	}
	
	private void prnFOOTR()
	{
		try
		{
			if(cl_dat.M_intLINNO_pbst >= 61)
				prnFMTCHR(dosREPORT,M_strEJT);
			dosREPORT.writeBytes ("\n");
			prnGRPTOT("GT",strGRTOT,"B");
			dosREPORT.writeBytes ("\n");
			crtLINE(138);
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",20));//margin
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",40));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY  ",20));
			dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)  ",35));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			crtLINE(138);
			cl_dat.M_intLINNO_pbst += 9;
			prnFMTCHR(dosREPORT,M_strEJT);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
	
	private void prnGRPHDR(String P_strGRPCT, int P_intMRGVL)
	{
		try
		{
			String L_strGRPDS = "";
			if(flgHDRFL)
			{
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',"",2));
				dosREPORT.writeBytes(padSTRING('R',strRPHDR,30));
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				flgHDRFL = false;
			}
			if (P_strGRPCT.equals("MG"))
				if(hstMNGRP.containsKey(strPRMGR1))
					L_strGRPDS = hstMNGRP.get(strPRMGR1).toString();
				else
					L_strGRPDS = strPRMGR1;
				//L_strGRPDS = hstMNGRP.get(strPRMGR1).toString();
				//L_strGRPDS = cl_cust.ocl_cust.getMNPRD(strPRMGR1);
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",P_intMRGVL));
			dosREPORT.writeBytes(padSTRING('R',L_strGRPDS,(28-P_intMRGVL)));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 2;
			if(cl_dat.M_intLINNO_pbst >= 68)
			{
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				crtLINE(138);
				prnFMTCHR(dosREPORT,M_strEJT);
				prnHEADER(); //gets the Header of the report
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPHDR");
		}
	}
	private void prnGRPTOT(String P_strGRPCT,String LP_TOTXXQT,String P_strBLDFL)
	{
		try
		{
			strPRINT = new StringBuffer("");
			if (P_strGRPCT.equals("SG"))
			{
				if(hstSBGRP.containsKey(strPRSGR1))
					strSGRDS = hstSBGRP.get(strPRSGR1).toString();
				else
					strSGRDS=strPRSGR1;
				//strSGRDS = cl_cust.ocl_cust.getSBPRD(strPRSGR1);
				System.out.println("strSGRDS = "+strSGRDS);
				dosREPORT.writeBytes(padSTRING('R',"",6));
				strPRINT.append(padSTRING('R',strSGRDS,22));
				for(int l=1;l<=intCOUNT;l++)
					strPRINT.append(padSTRING('L',getDASH(staPRSGR[l]),18));
			}
			else if (P_strGRPCT.equals("MG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				if(hstMNGRP.containsKey(strPRMGR1))
					strMGRDS = hstMNGRP.get(strPRMGR1).toString();
				else
					strMGRDS = strPRMGR1;
				System.out.println("strMGRDS = "+strMGRDS);
				//strMGRDS = cl_cust.ocl_cust.getMNPRD(strPRMGR1);
				dosREPORT.writeBytes(padSTRING('R',"",4));
				strPRINT.append(padSTRING('R',"TOTAL " + strMGRDS,24));
				for(int l=1;l<=intCOUNT;l++)
					strPRINT.append(padSTRING('L',getDASH(staPRMGR[l]),18));
			}
			else if (P_strGRPCT.equals("CG"))
			{
				dosREPORT.writeBytes(padSTRING('R',"",4));
				strPRINT.append(padSTRING('R',"CATEGORY TOTAL",24));
				for(int l=1;l<=intCOUNT;l++)
					strPRINT.append(padSTRING('L',getDASH(staCGTOT[l]),18));
			}
			else if (P_strGRPCT.equals("GT"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				dosREPORT.writeBytes(padSTRING('R',"",4));
				strPRINT.append(padSTRING('R',"GRAND TOTAL",24));
				for(int l=1;l<=intCOUNT;l++)
					strPRINT.append(padSTRING('L',getDASH(staGRTOT[l]),18));
			}
			strPRINT.append(padSTRING('L',getDASH(LP_TOTXXQT),20));
			if (P_strBLDFL.equals("B"))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(strPRINT.toString());
			if (P_strBLDFL.equals("B"))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			if(cl_dat.M_intLINNO_pbst >= 68)
			{
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				crtLINE(138);
				prnFMTCHR(dosREPORT,M_strEJT);
				prnHEADER(); //gets the Header of the report
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPTOT");
		}
	}
	
	private String getDASH(String P_strDASH)
	{  
		try
		{
			if(P_strDASH == null || P_strDASH.equals("0"))
				P_strDASH = "-";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDASH");
		}
		return P_strDASH;
	}
		
	private void intGRPTOT(String P_strGRPCT)
	{
		try
		{
			if (P_strGRPCT.equals("SG"))
			{
				for(l = 1;l <= intCOUNT;l++)
					staPRSGR[l] = "0";
				strSGTOT = "0";
				strPRSGR1 = strPRSGR;
			}
			else if (P_strGRPCT.equals("MG"))
			{
				for(l = 1;l <= intCOUNT;l++)
					staPRMGR[l] = "0";
				strMGTOT = "0";
				strPRMGR1 = strPRMGR;
			}
			else if (P_strGRPCT.equals("CG"))
			{
				for(l = 1;l <= intCOUNT;l++)
					staCGTOT[l] = "0";
				strCGTOT = "0";
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"intGRPTOT");
		}
	}
}
