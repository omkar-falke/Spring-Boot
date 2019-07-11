/*
System Name   : Material Management System
Program Name  : Material Stock Master
Program Desc. : 
Author        : A.T.Chaudhari
Date          : 
Version       : MMS 2.0
*/
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.sql.SQLException;
import java.io.*;
import java.sql.Date;
import java.sql.ResultSet;
/**<P>Program Description :</P>
 * <P><FONT color=purple>
 *    <STRONG>Program Details :</STRONG></FONT> </P>
 * <P>
 * 	<TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    
 * 		<TR><TD>System Name</TD><TD>Material Management System </TD></TR>  
 * 		<TR><TD>Program Name</TD><TD>Stock Master List</TD></TR>  
 * 		<TR><TD>Program Desc</TD><TD>                                         Display List of material code as per Obsolute,surplus,Insurence and       stock controlled items as per the selection. </TD></TR>  
 * 		<TR><TD>Basis Document</TD><TD>                       </TD></TR>  
 * 		<TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_rpstm.class</TD></TR>  
 * 		<TR><TD>Source path</TD><TD>f:\source\splerp2\mm_rpstm.java</TD></TR>  
 * 		<TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR>  
 * 		<TR><TD>Date</TD><TD>26/08/2004 </TD></TR>  
 * 		<TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR>  
 * 		<TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR>  
 * 		<TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR>  
 * 		<TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR>  
 * 		<TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR>
 * 	</TABLE></P>
 * <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P>
 * <P>
 *	<TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    
 * 		<TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR>  
 * 		<TR><TD>MM_STMST</TD><TD>ST_MMSBS,ST_STRTP,ST_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 		<TR><TD>CO_CTMST</TD><TD>CT_GRPCD,CT_CODTP,CT_MATCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  
 * 		<TR><TD>CO_CDTRN</TD><TD>CMT_CGMTP,CMT_CGSTP,CMT_CODCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 	 </TABLE>
 * </P>
 * */
class mm_rpstm extends cl_rbase 
{									/**Text Field For From Material Code	*/
	private JTextField txtFMMAT;	/**Text Field For To Material Code		*/
	private JTextField txtTOMAT;	/**Check Box for obsolute material code */
	private JCheckBox chbOBSFL;		/**Check Box for surplus material code	*/
	private JCheckBox chbSURFL;		/**Check Box for insurence material code	 */
	private JCheckBox chbINSFL;		/**Check Box for stock controlled material items	 */
	private JCheckBox chbSTKFL;		/**Constant string variabel for file name	 */
	private final String strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpstm.doc";	/**String for material type	 */
	private String strMATTP;		/**Integer variable for intem count	 */
	private int intINTCT;			/**File output stream report	 */
	private FileOutputStream fosREPORT ;   /**data output stream report	 */
    private DataOutputStream dosREPORT ;
	/**Constructor for the class mm_rpstm	 */
	mm_rpstm()
	{
		super(2);
		try
		{
			//Remove the component added by the super class
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			intINTCT = 0;	//initialise the int variable intINTCT as zero
			setMatrix(20,8);
			add(new JLabel("From Material Code"),2,4,1,1.3,this,'R');
			add(txtFMMAT = new TxtNumLimit(10.0),2,5,1,1,this,'L');
			add(new JLabel("To Material Code"),3,4,1,1.3,this,'R');
			add(txtTOMAT = new TxtNumLimit(10.0),3,5,1,1,this,'L');			
			add(chbOBSFL = new JCheckBox("Obsolete"),4,4,1,1.3,this,'R');
			add(chbINSFL = new JCheckBox("Insurence"),4,5,1,1,this,'L');
			add(chbSURFL = new JCheckBox("Surplus"),5,4,1,1.3,this,'R');
			add(chbSTKFL = new JCheckBox("Stock Controlled "),5,5,1,2,this,'L');
			txtFMMAT.setInputVerifier(new INPVF());
			txtTOMAT.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			
			setENBL(false);
		}
		catch(Exception L_CN)
		{
			setMSG(L_CN,"mm_rpstm");
		}
	}
	//method on action performed event
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			chbOBSFL.setSelected(false);
			chbSURFL.setSelected(false);
			chbINSFL.setSelected(false);
			chbSTKFL.setSelected(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				M_cmbDESTN.requestFocus();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				txtFMMAT.requestFocus();
		}
		if(M_objSOURC == M_cmbDESTN)
		{
			setMSG("",'N');
			if(M_cmbDESTN.getSelectedIndex() > 0)
				txtFMMAT.requestFocus();
		}
	}
	//method to display message when focus on a component
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == txtFMMAT)
				setMSG("Enter From Material Code Or Press 'F1' For Help..",'N');
			if(M_objSOURC == txtTOMAT)	
				setMSG("Enter To Material Code Or Press 'F1' For Help..",'N');
			if(M_objSOURC == chbOBSFL)
				setMSG("Select For Obsolute Items..",'N');
			if(M_objSOURC == chbINSFL)
				setMSG("Select For Insurance Items..",'N');
			if(M_objSOURC == chbSURFL)
				setMSG("Select For Surplus Items..",'N');
			if(M_objSOURC == chbSTKFL)
				setMSG("Select For StockControlled Items..",'N');
		}
		catch(Exception e)
		{
			setMSG(e,"Child.FocusGained");
		}
	}
	//event method on key pressed after pressing f1 display help windiow for the component
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			cl_dat.M_flgHELPFL_pbst = true; 
			setCursor(cl_dat. M_curWTSTS_pbst);
			if(M_objSOURC == txtFMMAT)
			{
				M_strHLPFLD = "txtFMMAT";
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' AND ";
				M_strSQLQRY +=" ST_MMSBS ='"+M_strSBSCD+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				if(txtFMMAT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_MATCD like '"+txtFMMAT.getText().trim() +"%'";
				M_strSQLQRY += " ORDER BY ST_MATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");
			}
			if(M_objSOURC == txtTOMAT)
			{
				M_strHLPFLD = "txtTOMAT";
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS FROM MM_STMST WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' AND ";
				M_strSQLQRY +=" ST_MMSBS ='"+M_strSBSCD+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				if(txtTOMAT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_MATCD like '"+txtTOMAT.getText().trim() +"%'";
				M_strSQLQRY += " ORDER BY ST_MATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");				
			}
			setCursor(cl_dat. M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMMAT)
			{
				if(txtFMMAT.getText().trim().length() != 0)
					txtTOMAT.requestFocus();
				else
					txtFMMAT.requestFocus();
			}
			if(M_objSOURC == txtTOMAT)
			{
				if(txtTOMAT.getText().trim().length() != 0)
					chbOBSFL.requestFocus();					
				else
					txtTOMAT.requestFocus();
			}
			if(M_objSOURC == chbSTKFL)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	//method on pressing enter or ok button of help window
	public void exeHLPOK()
	{
		super.exeHLPOK();
		cl_dat.M_flgHELPFL_pbst = false;
		if(M_strHLPFLD == "txtFMMAT")
		{
			txtFMMAT.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOMAT.requestFocus();
		}
		if(M_strHLPFLD == "txtTOMAT")
		{
			txtTOMAT.setText(cl_dat.M_strHLPSTR_pbst);
			chbOBSFL.requestFocus();
		}
	}
	/**Method to print report header	 */
	private void prnRPHDR()
	{
		try
		{
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("SUPREME PERTOCHEM LIMITED");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Stock Master List Of ");
			if(chbOBSFL.isSelected())
				dosREPORT.writeBytes("Obsolete");
			if(chbINSFL.isSelected())
			{
				if(chbOBSFL.isSelected())
					dosREPORT.writeBytes(",Insurence");
				else
					dosREPORT.writeBytes("Insurence");
			}
			if(chbSURFL.isSelected())
			{
				if(chbOBSFL.isSelected() || chbINSFL.isSelected())
					dosREPORT.writeBytes(",Surplus");  
				else
					dosREPORT.writeBytes("Surplus");  
			}
			if(chbSTKFL.isSelected())
			{
				if(chbOBSFL.isSelected() || chbINSFL.isSelected())
					dosREPORT.writeBytes(",Stock Controlled");
				else
					dosREPORT.writeBytes("Stock Controlled");
			}
			if(!chbOBSFL.isSelected() && !chbSURFL.isSelected() && !chbINSFL.isSelected() && !chbSTKFL.isSelected())
				dosREPORT.writeBytes("All");
			dosREPORT.writeBytes(" Items");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",76));
			dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),76));
			dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
			dosREPORT.writeBytes("\n");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Material Code And Description                                 YTD Issue   Min.Level  Qty.On Hand");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("UOM   Location  ABC   VED   HML   XYZ           Mat. Type     YTD Recpt   Max.Level  Qty.On Insp");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("                                            Lead Time I/E       YTD MRN         ROL  Qty.On Ord.");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("                                                 Std.Pkg.       YTD SAN         ROQ  Qty.On Ind.");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			M_intLINNO += 9;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"genRP5HD");
		}
	}
	/**Method to validate input data by the user before the processing	 */
	boolean vldDATA()
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		{
			if(M_cmbDESTN.getSelectedIndex() == 0)
			{
				setMSG("Select Printer..",'E');
				M_cmbDESTN.requestFocus();
				return false;
			}
		}
		if(txtFMMAT.getText().trim().length() == 0)
		{
			txtFMMAT.requestFocus();
			setMSG("Enter Material Code Or Press F1 For Help..",'E');
			return false;
		}
		if(txtTOMAT.getText().trim().length() == 0)
		{
			txtTOMAT.requestFocus();
			setMSG("Enter Material Code Or Press F1 For Help..",'E');
			return false;
		}
		if(Float.parseFloat(txtTOMAT.getText().trim()) < Float.parseFloat(txtFMMAT.getText().trim())){
			txtTOMAT.requestFocus();
			setMSG("To Materia Code Should Be Greater Than From Material Code..",'E');
			return false;
		}
		if(chbSURFL.isSelected() && chbSTKFL.isSelected())
		{
			chbSURFL.requestFocus();
			setMSG("Select Any One Option 'Surplus' Or 'Stock Controlled'..",'E');
			return false;
		}
		else
			return true;
	}
	/**Method to create horizontal line of specific character */
	private String genCRLIN(int k)
	{
		String s = "-";
		for(int i =0;i<=k;i++)
			s = s+"-";
		return s;
	}
	/**Method to print the report or display on the screen	 */
	void exePRINT()
	{
		setCursor(cl_dat. M_curWTSTS_pbst);
		if(vldDATA())
		{
			try
			{
				M_intPAGNO = 1;
				M_intLINNO = 0;
				intINTCT = 0;
				fosREPORT = new FileOutputStream(strFILNM);
				dosREPORT = new DataOutputStream(fosREPORT);
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,ST_ABCFL,ST_VEDFL,ST_HMLFL,"
					+"ST_XYZFL,ST_MATTP,ST_MINLV,ST_MAXLV,ST_RORLV,ST_RORQT,ST_STKQT,ST_STKIP,"
					+"ST_STKOR,ST_STKIN,ST_YTDGR,ST_YTDIS,ST_YTDMR,ST_YTDSN,CT_ILDTM,CT_ELDTM,CT_PKGQT "
					+"FROM MM_STMST,CO_CTMST WHERE ST_MATCD = CT_MATCD AND CT_CODTP ='CD' AND "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STSFL,'') <> 'X' AND "
					+"ST_MATCD BETWEEN '"+txtFMMAT.getText().trim()+"' AND '"+txtTOMAT.getText().trim()+"'";
				if(chbOBSFL.isSelected())	//if Obsolute is selected
					M_strSQLQRY += " AND isnull(ST_STSFL,'') = '9' ";
				if(chbSURFL.isSelected())	//If surplus is selected
					M_strSQLQRY += " AND isnull(ST_SRPQT,0) > 0 ";
				if(chbINSFL.isSelected())	//If Insurence is selected
					M_strSQLQRY += " AND isnull(ST_VEDFL,'') = 'E' ";
				if(chbSTKFL.isSelected())	//If stock controlled is slected
					M_strSQLQRY += " AND isnull(ST_STKFL,'') = 'Y' ";
				M_strSQLQRY += "ORDER BY ST_MATCD ";
				prnRPHDR();		//Call method to print header 
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						if(M_intLINNO >65)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							prnFMTCHR(dosREPORT,M_strEJT);
							M_intLINNO = 0;
							M_intPAGNO += 1;
							prnRPHDR();
						}
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),11));	//Material Code
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),50));	//Material Description
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDIS"),""),10));	//Year To Date ISS
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),""),12));	//Minimum Level
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),13));	//Quantity On Hand
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));	//Unit Of Measurment Code
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),10));	//Location Code
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_ABCFL"),""),5));	//ABC Flag
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_VEDFL"),""),6));	//VED Flag
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),""),6));	//HML Flag
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_XYZFL"),""),6));	//XYZ Flag
						dosREPORT.writeBytes(padSTRING('L',"",1));
						strMATTP = nvlSTRVL(M_rstRSSET.getString("ST_MATTP"),"");
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY3("SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXMTP' AND CMT_CODCD = '"+strMATTP+"' AND CMT_STSFL <> 'X'");
						if(L_rstRSSET.next())
							dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""),19));  //Material Type
						L_rstRSSET.close();
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDGR"),""),14));	//Year To Date For Receipt
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MAXLV"),""),12));	//Maximum Level
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIP"),""),13));	//Stock On Inspection
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('L',"",45));	//Blank
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0")+" / "+nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0"),12));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDMR"),""),14));	//Year To Date MR
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),""),12));	//Reorder Level
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKOR"),""),13));	//Quantity On Order
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",45));	//Blank
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),""),12));  // standare packing
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_YTDSN"),""),14));	//Year To Date SN
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_RORQT"),""),12));	//Reorder Quantity
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKIN"),""),13));	//Quantity On Indent
						dosREPORT.writeBytes("\n");
						M_intLINNO += 6;
						intINTCT++;
					}
					M_rstRSSET.close();
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(genCRLIN(96));
				dosREPORT.writeBytes("\nTotal Items : "+intINTCT);
				prnFMTCHR(dosREPORT,M_strEJT);
				M_intLINNO = 0;
				M_intPAGNO = 1;
				intINTCT = 0;
				dosREPORT.close();
				fosREPORT.close();
				//if print option is selected send to printer
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					doPRINT(strFILNM);
				//if screen option is selected then display on the screen
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					p  = r.exec("C:\\windows\\wordpad.exe "+strFILNM);
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"exePRINT");
			}
		}
		setCursor(cl_dat. M_curDFSTS_pbst);
	}
	//method to check valid input date in the given component
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			if(cl_dat.M_flgHELPFL_pbst)
				return true;
			try
			{
				if(input == txtFMMAT || input == txtTOMAT)
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select count(*) from MM_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STSFL,'') <> 'X' AND ST_MATCD = '"+((JTextField)input).getText()+"'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								setMSG("Invalid Material Code ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Material Code ",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Material Code ",'E');
						return false;
					}
				}
				return true;
			}
			catch(SQLException L_SQL)
			{
				setMSG(L_SQL,"verify");
				return false;
			}
			
		}
	}
}
