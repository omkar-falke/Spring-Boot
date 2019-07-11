/*
System Name   : Material Management System
Program Name  : Material Issue Note
Program Desc. : Report for material issue for from issue number - to issue number
Author        : A.T.Chaudhari
Date          : 13/08/2004
Version       : MMS 2.0
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Date;
import java.sql.SQLException;import java.sql.ResultSet;
import java.io.*;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Material Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>   Material Issue Number</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                                 Report for&nbsp;Material&nbsp;Issue&nbsp;Note&nbsp;For From - To Issue Number&nbsp; </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>                       </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp\mm_rpmin.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mm_rpmin.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>Abhijit T. Chaudhari </TD></TR>  <TR>    <TD>Date</TD>    <TD>13/08/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>MMS 2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD></TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD></TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD>MM_STMST</TD>    <TD> ST_MMSBS,ST_STRTP,ST_MATCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>MM_STPRC</TD>    <TD>STP_MMSBS,STP_STRTP,STP_MATCD</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class mm_rpmin extends cl_rbase 
{									
	                                	/**Text field for from issue number	 */
	private JTextField txtFMISS;	/**Text field for to issue number	 */
	private JTextField txtTOISS;	/**String for issue number	 */
	private String strISSNO;		/**String for issue date	 */
	private String strISSDT;		/**String for material type	 */
	//private String strMATTP;		/**String for usage type	 */
	private String strUSGTP;		/**String for remark	 */
	private String strREMDS;		/**String for usage description	 */
	private String strUSGDS;		/**String for previous issue number	 */
	private String strPISNO;		/**String for department name	 */
	private String strDPTNM;		/**String for iso number	 */
	private String strISOS1;		/**String for iso number	 */
	private String strISOS2;		/**String for iso number	 */
	private String strISOS3;		/**Integer variable for serial number	 */
	private int intSRLNO;			/**Integer for record count 	 */
	private int intRECCT;			/**Hashtable for store remark w.r.t. issue number	 */
	private Hashtable<String,String> hstREMRK;		/**Hashtable for store usage description w.r.t. issue number	 */
	private Hashtable<String,String> hstUSGDS;		/**Constant String variable for report name	 */
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mm_rpmin.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;
	private FileOutputStream fosREPORT ;   
    private DataOutputStream dosREPORT ;
	private String strPRTTP,strPRTCD,strPRTNM,strWORNO;
	private String strISSTP ="",strISSDS="";
	private Hashtable<String,String> hstISSTP;	
	mm_rpmin()
	{
		super(2);
		try
		{
			hstREMRK = new Hashtable<String,String>();
			hstUSGDS = new Hashtable<String,String>();
			hstISSTP = new Hashtable<String,String>();
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			setMatrix(20,8);
			add(new JLabel("From Issue"),4,4,1,1,this,'L');
			add(txtFMISS = new TxtNumLimit(10.0),4,5,1,1.3,this,'L');
			add(new JLabel("To Issue"),5,4,1,1,this,'L');
			add(txtTOISS = new TxtNumLimit(10.0),5,5,1,1.3,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	mm_rpmin(String P_strSBSCD)
	{
		M_strSBSCD = P_strSBSCD;
		M_rdbTEXT.setSelected(true);
		hstREMRK = new Hashtable<String,String>();
		hstUSGDS = new Hashtable<String,String>();
		hstISSTP = new Hashtable<String,String>();
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
				setENBL(true);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);
				txtFMISS.requestFocus();
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMISS)
			{
				if(txtTOISS.getText().trim().length() == 0)
					txtTOISS.setText(txtFMISS.getText());
				txtTOISS.select(0,txtTOISS.getText().trim().length());
				txtTOISS.requestFocus();
			}
			if(M_objSOURC == txtTOISS)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		//method to display list of material code in help window when press F1
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtFMISS)
				{
					M_strHLPFLD = "txtFMISS";
					M_strSQLQRY = "SELECT DISTINCT IS_ISSNO,IS_ISSDT,IS_DPTCD,IS_STSFL FROM MM_ISMST WHERE "
						+"IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
					if(txtFMISS.getText().trim().length() > 0)
						M_strSQLQRY += " AND IS_ISSNO LIKE '"+txtFMISS.getText().trim()+"%'";
					M_strSQLQRY += " AND isnull(IS_STSFL,' ') <>'X' ORDER BY IS_ISSDT desc";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Issue No.","Issue Date.","Dept.Code"},3,"CT");
				}
				if(M_objSOURC == txtTOISS)
				{
					txtTOISS.setText("");
					M_strHLPFLD = "txtTOISS";
					M_strSQLQRY = "SELECT DISTINCT IS_ISSNO,IS_ISSDT,IS_DPTCD,IS_STSFL FROM MM_ISMST WHERE "
						+"IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4)+"'";
					if(txtTOISS.getText().trim().length() > 0)
						M_strSQLQRY += " AND IS_ISSNO LIKE '"+txtTOISS.getText().trim()+"%'";
					M_strSQLQRY += " AND IS_ISSNO > '"+txtFMISS.getText().trim()+"' "
						+"AND isnull(IS_STSFL,' ') <>'X' ORDER BY IS_ISSDT desc";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Issue No.","Issue Date.","Dept.Code"},3,"CT");
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_E)
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"Key Pressed F1");
			}
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMISS")
		{
			txtFMISS.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOISS.setText(txtFMISS.getText().trim());
			txtTOISS.select(0,txtTOISS.getText().trim().length());
			txtTOISS.requestFocus();
		}
		if(M_strHLPFLD == "txtTOISS")
		{
			txtTOISS.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	//Method to generate report file by 
	void genRPTFL(String P_strFMISS,String P_strTOISS)
	{
		try
		{
			String strMATCD ="",strPMATCD ="";
			ResultSet L_rstRSSET;
			fosREPORT = new FileOutputStream(strRPFNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			M_intLINNO = 0;	
			M_intPAGNO = 1;
			intSRLNO = 1;
			java.sql.Date datTEMP ;
			intRECCT = 0;
			strPISNO = "";
			//Get ISO specific number
			strISOS1 = "DOCUMENT REF : "+cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPISS01");
			strISOS2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPISS02");
			strISOS3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPISS03");
			
			//Store Remark No and Remark Description in hash table
			M_strSQLQRY = "SELECT RM_DOCNO, RM_REMDS FROM MM_RMMST "
						 +"WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"RM_TRNTP = 'IS' AND RM_DOCNO BETWEEN '"+P_strFMISS+"' AND '"+P_strTOISS+"' and isnull(RM_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(hstREMRK != null)
					hstREMRK.clear();
				while(M_rstRSSET.next())
				{
					String L_strDOCDS = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
					hstREMRK.put(nvlSTRVL(M_rstRSSET.getString("RM_DOCNO"),""),L_strDOCDS);
				}
				M_rstRSSET.close();
			}
			//Store usage number and Usage Description in hash table
			M_strSQLQRY = "SELECT CMT_CGSTP,CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP + CMT_CGSTP IN("
				+"'SYSMMXXUSG','SYSMMXXISS') AND isnull(CMT_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(hstUSGDS != null)
					hstUSGDS.clear();
				if(hstISSTP != null)
					hstISSTP.clear();
				while(M_rstRSSET.next())
				{
					String L_strUSGDS = nvlSTRVL(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),"");
					if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXUSG"))
					    hstUSGDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),L_strUSGDS);
					else
					    hstISSTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),L_strUSGDS);
					    
				}
				if(M_rstRSSET!=null)
					M_rstRSSET.close();
			}
			
			M_strSQLQRY = "SELECT IS_STRTP,IS_ISSTP,IS_ISSNO,IS_ISSDT,IS_MATTP,IS_DPTCD,IS_MATCD,IS_GRNNO,IS_BATNO,"
				+"IS_USGTP,IS_REQQT,IS_ISSQT,IS_CCTCD,IS_PRTTP,IS_PRTCD,IS_WORNO,ST_MATDS,ST_LOCCD,ST_UOMCD,ST_STKQT,"
				+"ST_STSFL,ST_STKFL,CMT_CODDS FROM MM_ISMST,MM_STMST,CO_CDTRN "
				+"WHERE IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' AND CMT_CODCD = IS_DPTCD AND "
				+"IS_STRTP = ST_STRTP and IS_CMPCD = ST_CMPCD AND IS_MATCD = ST_MATCD AND IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_ISSNO BETWEEN '"+P_strFMISS+"' "
				+"AND '"+P_strTOISS+"' AND isnull(IS_STSFL,' ') <>'X' ORDER BY IS_ISSNO,IS_MATCD,IS_ISSDT";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT++;
					strISSNO = nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),"");
					strMATCD = nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"");

					datTEMP = M_rstRSSET.getDate("IS_ISSDT");
					if(datTEMP !=null)
						strISSDT = M_fmtLCDAT.format(datTEMP);
					else
						strISSDT ="";
					strUSGTP = nvlSTRVL(M_rstRSSET.getString("IS_USGTP"),"");	
					strDPTNM = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");	
					strPRTTP = nvlSTRVL(M_rstRSSET.getString("IS_PRTTP"),"");	
					strISSTP = nvlSTRVL(M_rstRSSET.getString("IS_ISSTP"),"");	
					strPRTCD = nvlSTRVL(M_rstRSSET.getString("IS_PRTCD"),"");	
					strWORNO = nvlSTRVL(M_rstRSSET.getString("IS_WORNO"),"");	
					if(hstUSGDS != null)
					{
						if(hstUSGDS.size()>0)
						{
							if(hstUSGDS.get(strUSGTP) != null)
								strUSGDS = hstUSGDS.get(strUSGTP).toString();
							else
								strUSGDS = "";
						}
					}
					if(hstISSTP !=null)
					{
					    if(hstISSTP.containsKey((String)strISSTP))
					        strISSDS = hstISSTP.get(strISSTP).toString();
					    else
					        strISSDS = "";    
					}
					if(intRECCT == 1)
					{
						if(M_rdbHTML.isSelected())
						{
						    dosREPORT.writeBytes("<HTML><HEAD><Title>List of Issues </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
						}
						strPRTNM ="";
						M_strSQLQRY = " SELECT PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='"+strPRTTP +"'";
						M_strSQLQRY += " AND PT_PRTCD ='"+strPRTCD+"'";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(L_rstRSSET !=null)
						{
							if(L_rstRSSET.next())
								strPRTNM = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");
						}
						prnRPHDR();
					}
					if(!strISSNO.equals(strPISNO))
					{
						strPMATCD ="";
						if(!strPISNO.trim().equals(""))
						{
							M_intPAGNO =1;
							prnRPFTR();
							prnRPHDR();
							intSRLNO = 1;
						}
						strPISNO = strISSNO;
					}
					
					if(!strMATCD.equals(strPMATCD))
					{
						dosREPORT.writeBytes("\n");
						M_intLINNO++;
						if(M_intLINNO >58)
						{
							prnRPSFT();
							M_intPAGNO += 1;
							prnRPHDR();			
						}
						dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),54));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),13));
						if(nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"").equals("Y"))								
							dosREPORT.writeBytes(padSTRING('L',"Y ",9));
						else
						{
							if(nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"").equals("9"))
								dosREPORT.writeBytes(padSTRING('L',"- ",9));
							else
								dosREPORT.writeBytes(padSTRING('L',"  ",9));
						}
						// IF STSFL = '9' OR STKFL = 'Y' THEN PRINT '/' ELSE PRINT BLANK 
						if(nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"").equals("9") || nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"").equals("Y"))
							dosREPORT.writeBytes(padSTRING('L'," /",2));
						else
							dosREPORT.writeBytes(padSTRING('L',"  ",2));
						//IF STSFL = '9' THEN PRINT 'Y ELSE CHECK STKFL = 'Y' IF YES PRINT '-' ELSE PRINT BLANK '  '  
						if(nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"").equals("9"))
							dosREPORT.writeBytes(padSTRING('L'," Y",2));
						else
						{
							if(nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"").equals("Y"))
								dosREPORT.writeBytes(padSTRING('L'," -",2));
							else	
								dosREPORT.writeBytes(padSTRING('L',"  ",2));
						}
						dosREPORT.writeBytes("\n");
						M_intLINNO++;
						if(M_intLINNO >58)
						{
							prnRPSFT();
							M_intPAGNO += 1;
							prnRPHDR();			
						}
						intSRLNO += 1;
						strPMATCD = strMATCD ;
					}
					dosREPORT.writeBytes(padSTRING('R',"",4));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_CCTCD"),""),14));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_GRNNO"),"")+" "+nvlSTRVL(M_rstRSSET.getString("IS_BATNO"),""),20));
					dosREPORT.writeBytes(padSTRING('R',strUSGDS,16));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),5));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IS_REQQT"),""),13));
					if(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),"0")) <=0)
						dosREPORT.writeBytes(padSTRING('L'," ",13));
					else
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),""),13));
					dosREPORT.writeBytes("\n");
						M_intLINNO++;
					if(M_intLINNO >58)
					{
						prnRPSFT();
						M_intPAGNO += 1;
						prnRPHDR();			
					}
					
				}
			}
			prnRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			M_intLINNO = 0;	
			M_intPAGNO = 1;
			intSRLNO = 1;
			strPISNO = "";
			dosREPORT.close();
			fosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"strRPFIL");
		
		}
	}
	//Method to print report header 
	void prnRPHDR()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			dosREPORT.writeBytes(padSTRING('L',"\n-------------------------------",96));
			dosREPORT.writeBytes("\n");    
			dosREPORT.writeBytes(padSTRING('L',strISOS1,96));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOS2,96));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISOS3,96));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",96));
			dosREPORT.writeBytes("\n \n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,40));
			dosREPORT.writeBytes(padSTRING('R',"",31));
			dosREPORT.writeBytes(padSTRING('R',"Report Date : "+cl_dat.M_strLOGDT_pbst,25));
			M_intLINNO += 7;
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"MATERIAL ISSUE NOTE ",71));
			dosREPORT.writeBytes(padSTRING('R',"Page No     : "+M_intPAGNO,25));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Stores Type  : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),71));
			dosREPORT.writeBytes(padSTRING('R',"Issue No    : "+M_strSBSCD.substring(2,4)+"/"+strISSNO,25));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Department   : "+strDPTNM,71));
			dosREPORT.writeBytes(padSTRING('R',"Issue Date  : "+strISSDT,25));
			dosREPORT.writeBytes("\n");
			if(strPRTTP.equals("C"))
			      dosREPORT.writeBytes(padSTRING('R',"Customer     : "+strPRTNM,71));
			else
			    dosREPORT.writeBytes(padSTRING('R',"Contractor   : "+strPRTNM,71));
			dosREPORT.writeBytes(padSTRING('R',"Order Ref.  : "+strWORNO,25));
			dosREPORT.writeBytes("\nIssue Type   : "+strISSDS);
			M_intLINNO += 5;
			dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes("Sr. Material Code And Description                                          Stock Qty     Stk/Obs");
			dosREPORT.writeBytes("\n");
		//	dosREPORT.writeBytes("No. Cost-Center                       Usage           UOM  Location        Req. Qty.     Iss.Qty.");        
			dosREPORT.writeBytes("No. Cost-Center  GRIN/Batch           Usage           UOM  Location        Req. Qty.     Iss.Qty.");        

			dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("</b>");    
			}
			M_intLINNO += 5;
		}
		catch(IOException ioe)
		{
			setMSG(ioe,"Report Header");
		}
	}
	//Method to print short footer
	void prnRPSFT()
	{ 
		try
		{
			if(!strISSNO.equals(strPISNO))
			{
				intSRLNO =0;
				M_intPAGNO =0;
				prnRPFTR();
			}
			else
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------\n");	
			M_intLINNO = 0;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strEJT);
		}
		catch(IOException L_IOE)
		{
			setMSG(L_IOE,"Report Short Footer");
		}
	}
	//Method to print footer 
	void prnRPFTR()
	{
		try
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<b>");    
			}
			dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------\n");
			if(hstREMRK != null)
			{
				if(hstREMRK.get(strPISNO) != null)
					strREMDS = hstREMRK.get(strPISNO).toString();
				else
					strREMDS = "";
			}
			dosREPORT.writeBytes(padSTRING('R',"Remarks :"+strREMDS,96));
			dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes("Requested By                 Issued By                Received By                 HOD(MHD)       ");
			dosREPORT.writeBytes("\n\n\n\n\n");
			dosREPORT.writeBytes("Date :                       Date :                   Date :                      Date :         ");
			dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------\n");
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("</b>");    
			}
			M_intLINNO = 0;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strEJT);
		}
		catch(IOException L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}
	//method to validate input date, this method called before generate report 
	boolean vldDATA()
	{
		try
		{
			if(M_rdbHTML.isSelected())
				     strRPFNM = strRPLOC + "mm_rpmin.html";
				if(M_rdbTEXT.isSelected())
				     strRPFNM = strRPLOC + "mm_rpmin.doc";
			if(txtFMISS.getText().trim().toString().length() == 0)
			{
				setMSG("Please Enter Issue No. Or Press F1 For Help.. ",'E');
				txtFMISS.requestFocus();
				return false;
			}
			if(txtTOISS.getText().trim().toString().length() == 0)
			{
				setMSG("Please Enter Issue No. Or Press F1 For Help.. ",'E');
				txtTOISS.requestFocus();
				return false;
			}
			if(Float.parseFloat(txtTOISS.getText()) < Float.parseFloat(txtFMISS.getText()))
			{
				setMSG("To Issue No. Should Be Greater Than From Issue No.",'E');
				txtFMISS.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if(M_cmbDESTN.getItemCount() ==0)
				{
					setMSG("Please select E-mail Id by using the F1 list ..",'E');
					return false;
				}
			}
			return true;
		}
		catch(Exception L_VLD)
		{
			return false;
		}
	}
	//Method to print,dispalay on screen report  depand on selection
	void exePRINT()
	{
		if(vldDATA())
		{
			try
			{	
				genRPTFL(txtFMISS.getText().trim(),txtTOISS.getText());
				if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT >0)
					    if (M_rdbTEXT.isSelected())
					        doPRINT(strRPFNM);
					    else 
			            {    
							Runtime r = Runtime.getRuntime();
							Process p = null;					
							p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
							setMSG("For Printing Select File Menu, then Print  ..",'N');
					    }    
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				    Runtime r = Runtime.getRuntime();
					Process p = null;
					if(intRECCT >0)
					{	
					    if(M_rdbHTML.isSelected())
					        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					    else
					        p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				    }
					else
					    setMSG("No data found..",'E');
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
			    	cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Issue Note"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
			    }
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Error.exescrn.. ");
			}
		}
	}
}



