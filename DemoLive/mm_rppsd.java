/*
System Name   : Material Management System
Program Name  : List Of descripency  
Program Desc. : Display Descripency for 
				year closing quantity and calculated closing quantity 
				closing quantity and stock quantity
				year cum receit quantity and sum of accpted quantity in mm_grmst
				year cum issue quantity and sum of issue quantity form mm_ismst
				year cum material ret quantity and sum of return quantity form mm_mrmst
				year cum san quantity and sum of san quantity form mm_samst
				for From date to date
Author        : A. T. Chaudhari
Date          : 15/04/2004
Version       : MMS 2.0
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.SQLException;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
/**<P>Program Description :</P> <P><FONT color=purple> <STRONG>Program Details :</STRONG></FONT> </P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white> <TR><TD>System Name</TD><TD>Material Management System </TD></TR> <TR><TD>Program Name</TD><TD>List Of Descripency</TD></TR> <TR><TD>Program Desc</TD><TD>                                                           Display descripency in sotck </TD></TR> <TR><TD>Basis Document</TD><TD>                       </TD></TR> <TR><TD>Executable path</TD><TD>f:\exec\splerp\sa_pslds.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\sa_pslds.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>28/09/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR> </TABLE></P> <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD>							<TD><P align=center>Mod</P>								  </TD>	<TD><P align=center>Del</P></TD>			<TD><P align=center>Enq</P></TD></TR> <TR><TD>MM_STPRC</TD><TD>					</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_STMST</TD><TD>			ST_MMSBS,ST_STRTP,ST_MATCD			</TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_GRMST</TD><TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO	</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center><FONT face="LotusWP Icon"></FONT>&nbsp;</P></TD>	<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_ISMST</TD><TD>IS_STPRC,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_TAGNO,IS_BATNO								</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_MRMST</TD><TD>												MR_MMSBS,MR_STRTP,MR_MRNNO,MR_MATCD												</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_SAMST</TD><TD>						SA_MMSBS,SA_STRTP,SA_SANNO,SA_MATCD						</TD><TD><P align=center>&nbsp;</P></TD>							<TD><P align=center>&nbsp;</P></TD>								<TD><P align=center>&nbsp;</P></TD>			<TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE></P>*/
class mm_rppsd extends cl_rbase
{									/**JTextField for from date  */
	private JTextField txtFMDAT;	/**JTextField for to date	 */
	private JTextField txtTODAT;	/**Constant string variable for file name	 */
	private final String strFILNM = "C:\\Reports\\sa_pslds.doc";	/**file output stream object */
	private FileOutputStream fosREPORT;		/**data output stream object		 */
	private DataOutputStream dosREPORT;		/**boolean print flag to print  */
	private boolean blnPRNFL;
	private boolean blnRPTFL = true;
	//Constructor for report	
	public mm_rppsd()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		
			setMatrix(20,8);
			add(new JLabel("From Date"),2,4,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),2,5,1,1,this,'L');
			add(new JLabel("To Date"),3,4,1,1,this,'L');
			add(txtTODAT = new TxtDate(),3,5,1,1,this,'L');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor ..");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				M_cmbDESTN.requestFocus();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				txtFMDAT.requestFocus();
		}
		if(M_objSOURC == M_cmbDESTN)
			txtFMDAT.requestFocus();
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtTODAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == txtFMDAT)
				setMSG("Enter Form Date ..",'N');
			if(M_objSOURC == txtTODAT)
			{
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				txtTODAT.select(0,txtTODAT.getText().length());
				setMSG("Enter To Date ..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"focusGained");
		}
	}
	//method to check valid data before processing report
	public boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	//method to print or display report depand on selection
	public void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				fosREPORT = new FileOutputStream(strFILNM);
				dosREPORT = new DataOutputStream(fosREPORT);
				M_intPAGNO = 1;
				M_intLINNO = 1;
				setCursor(cl_dat.M_curWTSTS_pbst);
				blnRPTFL = false;
				prnYCSQT();			//call method to print descripency in year closing quantity and calculated closing quantity 
				prnYCSQTSTKQT();	//call method to print descripency in year closing quantity and stock quantity
				prnCRCQTGRNQT();	//call method to print descripency in year cum receit quantity and sum of accpted quantity in mm_grmst
				prnCISQTISSQT();	//Call method to print descripency in year cum issue quantity and sum of issue quantity form mm_ismst
				prnCMRQTRETQT();	//Call method to print descripency in year cum material ret quantity and sum of return quantity form mm_mrmst
				prnCSAQTSANQT();	//Call method to print descripency in year cum san quantity and sum of san quantity form mm_samst
				setCursor(cl_dat.M_curDFSTS_pbst);	
				M_rstRSSET.close();
				dosREPORT.close();
				fosREPORT.close();
				if(blnRPTFL)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						doPRINT(strFILNM);
					//if screen is selected then display it on screen
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
					{
						Process prcREPORT;
						Runtime r = Runtime.getRuntime();
						prcREPORT= r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
					}
				}
				else
					setMSG("No descripancies are found..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
	//method to print report of descripency in year closing stock quantity and actual closing quantity in mm_stprc
	public void prnYCSQT()
	{
		try
		{
			M_strSQLQRY = "SELECT STP_MATCD,STP_YOSQT,STP_CRCQT,STP_CMRQT,STP_CSAQT,STP_CISQT,"
				+"STP_YCSQT FROM MM_STPRC WHERE STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
				+"AND (isnull(STP_YCSQT,0) <> isnull(STP_YOSQT,0) + isnull(STP_CRCQT,0) + "
				+"isnull(STP_CMRQT,0) + isnull(STP_CSAQT,0) - isnull(STP_CISQT,0)) ORDER BY STP_MATCD";
		System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			blnPRNFL = false;
			if(M_rstRSSET != null)
			{
				M_intPAGNO = 1;
				M_intLINNO = 1;
				while(M_rstRSSET.next())
				{
					blnPRNFL = true;
					if(M_intPAGNO == 1 || M_intLINNO > 58)
					{
						if(M_intPAGNO > 1)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						dosREPORT.writeBytes("\n");	M_intLINNO ++;	
						dosREPORT.writeBytes("Report For The Material Code Which Stock Closing Quanity Is Not Match With Addition Of ");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"(yosqt+crcqt+cmrqt+csaqt-cisqt)",70));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"For Period "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),70));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("MAT. CD.              YOSQT           CRCQT           CMRQT           CSAQT           CISQT           YCSQT");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						M_intPAGNO++;
					}
					dosREPORT.writeBytes("\n");	M_intLINNO ++;
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YOSQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CRCQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CMRQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CSAQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CISQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCSQT"),"0.0"),16));
				}
				if(blnPRNFL)
				{
					blnRPTFL = true;
					blnPRNFL = false;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
				}
				M_intPAGNO = 1;
				M_intLINNO = 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnYCSQT");
		}
	}
	//method for descripency report in year closing stock quantity and actual stock quantity
	public void prnYCSQTSTKQT()
	{
		try
		{
			M_strSQLQRY = "SELECT STP_MATCD,STP_YCSQT,ST_STKQT FROM MM_STPRC,MM_STMST WHERE "
				+"ST_CMPCD=STP_CMPCD and ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"
				+M_strSBSCD.substring(2,4)+"' AND isnull(STP_YCSQT,0) <> isnull(ST_STKQT,0) "
				+"AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' ORDER BY STP_MATCD";
		    System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			blnPRNFL = false;
			if(M_rstRSSET != null)
			{
				M_intPAGNO = 1;
				M_intLINNO = 1;
				while(M_rstRSSET.next())
				{
					blnPRNFL = true;
					if(M_intPAGNO == 1 || M_intLINNO > 58)
					{
						if(M_intPAGNO > 1)
						{
							dosREPORT.writeBytes("\n");	
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("Report for the material code which stock closing quanity is not match with stock quantity ");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"in stock master ",70));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"For Period "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),70));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("MAT. CD.              YCSQT           STKQT");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						M_intPAGNO++;
					}
					dosREPORT.writeBytes("\n");	M_intLINNO ++;
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCSQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0.0"),16));
				}
				if(blnPRNFL)
				{
					blnRPTFL = true;
					blnPRNFL = false;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
				}
				M_intPAGNO = 1;
				M_intLINNO = 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnYCSQTSTKQT");
		}
	}
	//method to print descripency report for cum receit value and sum of actual grin quantity
	public void prnCRCQTGRNQT()
	{
		try
		{
			M_strSQLQRY = "SELECT STP_MATCD,STP_CRCQT,SUM(isnull(GR_ACPQT,0)) AS ACPQT FROM "
				+"MM_STPRC,MM_GRMST WHERE GR_CMPCD=STP_CMPCD and GR_STRTP = STP_STRTP AND GR_MATCD = STP_MATCD AND "
				+"STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') = '2' "
				+"AND GR_GRNDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' "
				+"AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
				+"GROUP BY STP_MATCD,STP_CRCQT HAVING SUM(isnull(GR_ACPQT,0)) <> STP_CRCQT "
				+"ORDER BY STP_MATCD ";
		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			blnPRNFL = false;
			if(M_rstRSSET != null)
			{
				M_intPAGNO = 1;
				M_intLINNO = 1;
				while(M_rstRSSET.next())
				{
					blnPRNFL = true;
					if(M_intPAGNO == 1 || M_intLINNO > 58)
					{
						if(M_intPAGNO > 1)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("Report for the material code which cum rec quantity is not match with sum of accepted quantity ");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"in MM_GRMST ",70));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"For Period "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),70));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("MAT. CD.              CRCQT           ACPQT");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						M_intPAGNO++;
					}
					dosREPORT.writeBytes("\n");	M_intLINNO ++;
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CRCQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ACPQT"),"0.0"),16));
				}
				if(blnPRNFL)
				{
					blnRPTFL = true;
					blnPRNFL = false;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
				}
				M_intPAGNO = 1;
				M_intLINNO = 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnYCSQTSTKQT");
		}
	}
	//method for descripency report of cum issue quantity and sum of actual issue quantity
	public void prnCISQTISSQT()
	{
		try
		{
			M_strSQLQRY = "SELECT STP_MATCD,STP_CISQT,SUM(isnull(IS_ISSQT,0)) AS ISSQT FROM "
				+"MM_STPRC,MM_ISMST WHERE IS_CMPCD=STP_CMPCD and IS_STRTP = STP_STRTP AND IS_MATCD = STP_MATCD AND "
				+"STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IS_STSFL,'') = '2' "
				+"AND CONVERT(varchar,IS_AUTDT,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' "
				+"AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
				+"GROUP BY STP_MATCD,STP_CISQT HAVING SUM(isnull(IS_ISSQT,0)) <> STP_CISQT "
				+"ORDER BY STP_MATCD ";
		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			blnPRNFL = false;
			if(M_rstRSSET != null)
			{
				M_intPAGNO = 1;
				M_intLINNO = 1;
				while(M_rstRSSET.next())
				{
					blnPRNFL = true;
					if(M_intPAGNO == 1 || M_intLINNO > 58)
					{
						if(M_intPAGNO > 1)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("Report for the material code which cum Issue quanity is not match with sum of Issue quantity ");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"in Issue Master ",70));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"For Period "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),70));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("MAT. CD.              CRCQT           ISSQT");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						M_intPAGNO++;
					}
					dosREPORT.writeBytes("\n");	M_intLINNO ++;
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CISQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ISSQT"),"0.0"),16));
				}
				if(blnPRNFL)
				{
					blnRPTFL = true;
					blnPRNFL = false;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
				}
				M_intPAGNO = 1;
				M_intLINNO = 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnYCSQTSTKQT");
		}
	}
	//methot for descripency report for cum material return quantity and sum of actual return quantity 
	public void prnCMRQTRETQT()
	{
		try
		{
			M_strSQLQRY = "SELECT STP_MATCD,STP_CMRQT,SUM(isnull(MR_RETQT,0)) AS RETQT FROM "
				+"MM_STPRC,MM_MRMST WHERE MR_CMPCD=STP_CMPCD and MR_STRTP = STP_STRTP AND MR_MATCD = STP_MATCD AND "
				+"STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND MR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MR_STSFL,'') = '2' "
				+"AND MR_AUTDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' "
				+"AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
				+"GROUP BY STP_MATCD,STP_CMRQT HAVING SUM(isnull(MR_RETQT,0)) <> STP_CMRQT "
				+"ORDER BY STP_MATCD ";
		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			blnPRNFL = false;
			if(M_rstRSSET != null)
			{
				M_intPAGNO = 1;
				M_intLINNO = 1;
				while(M_rstRSSET.next())
				{
					blnPRNFL = true;
					if(M_intPAGNO == 1 || M_intLINNO > 58)
					{
						if(M_intPAGNO > 1)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("Report for the material code which cum return quanity is not match with return quantity ");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"In MM_MRMST ",70));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"For Period "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),70));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("MAT. CD.              CMRQT           RETQT");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						M_intPAGNO++;
					}
					dosREPORT.writeBytes("\n");	M_intLINNO ++;
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CMRQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("RETQT"),"0.0"),16));
				}
				if(blnPRNFL)
				{
					blnRPTFL = true;
					blnPRNFL = false;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
				}
				M_intPAGNO = 1;
				M_intLINNO = 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnYCSQTSTKQT");
		}
	}
	//method for the descripency report for cum san quantity and sum of san quantity
	public void prnCSAQTSANQT()
	{
		try
		{
			M_strSQLQRY = "SELECT STP_MATCD,STP_CSAQT,SUM(isnull(SA_SANQT,0)) AS SANQT FROM "
				+"MM_STPRC,MM_SAMST WHERE SA_CMPCD=STP_CMPCD and SA_STRTP = STP_STRTP AND SA_MATCD = STP_MATCD AND "
				+"STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(SA_STSFL,'') <> 'X' "
				+"AND SA_SANDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' "
				+"AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
				+"GROUP BY STP_MATCD,STP_CSAQT HAVING SUM(isnull(SA_SANQT,0)) <> STP_CSAQT "
				+"ORDER BY STP_MATCD ";
		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			blnPRNFL = false;
			if(M_rstRSSET != null)
			{
				M_intPAGNO = 1;
				M_intLINNO = 1;
				while(M_rstRSSET.next())
				{
					blnPRNFL = true;
					if(M_intPAGNO == 1 || M_intLINNO > 58)
					{
						if(M_intPAGNO > 1)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
							M_intLINNO = 0;
							prnFMTCHR(dosREPORT,M_strEJT);
						}
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("Report for the material code which cum san quanity is not match with sum of san quantity ");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"In MM_SAMST ",70));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes(padSTRING('R',"For Period "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),70));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("MAT. CD.              CSAQT           SANQT");
						dosREPORT.writeBytes("\n");	M_intLINNO ++;
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
						M_intPAGNO++;
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_CSAQT"),"0.0"),16));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SANQT"),"0.0"),16));
				}
				if(blnPRNFL)
				{
					blnRPTFL = true;
					blnPRNFL = false;
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------");
				}
				M_intPAGNO = 1;
				M_intLINNO = 1;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnYCSQTSTKQT");
		}
	}
}