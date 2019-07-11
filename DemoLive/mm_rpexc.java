/*
System Name   : Material Management
Program Name  : Excise Data Transfer Report

Program Desc. :
Author        : Mr. Prashant Sankle
Date          : 19th Nov 2003
Version       : MMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/
/**
<P>Program Description :</P>
<P><FONT color=purple><STRONG>Program Details :</STRONG></FONT> 
<TABLE border=1 borderColorDark=darkslateblue borderColorLight=white 
cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%">
  
  <TR>
    <TD>System Name</TD>
    <TD>Material Management</TD></TR>
  <TR>
    <TD>Program Name</TD>
    <TD> Eaxct Data Transfer</TD></TR>
  <TR>
    <TD>Program Desc</TD>
    <TD>    Generate Report and maintains a Data file for 
    excise.</TD></TR>
  <TR>
    <TD>Basis Document</TD>
    <TD></TD></TR>
  <TR>
    <TD>Executable path</TD>
    <TD>f:\data\asoft\exec\splerp2\</TD></TR>
  <TR>
    <TD>Source path</TD>
    <TD>g:\splerp2\</TD></TR>
  <TR>
    <TD>Author </TD>
    <TD>&nbsp;</TD></TR>
  <TR>
    <TD>Date</TD>
    <TD>&nbsp; </TD></TR>
  <TR>
    <TD>Version </TD>
    <TD>2.0.0</TD></TR>
  <TR>
    <TD><STRONG>Modification&nbsp;: 1 </STRONG></TD>
    <TD></TD></TR>
  <TR>
    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD>
    <TD>Prashant Sankle</TD></TR>
  <TR>
    <TD>
      <P 
      align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
      Date</P></TD>
    <TD>
      <P align=left>20/09/2003</P></TD></TR>
  <TR>
    <TD>
      <P 
      align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD>
    <TD>2.0.0.</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : 
</STRONG></FONT>
<TABLE background="" border=1 borderColorDark=white 
borderColorLight=darkslateblue cellPadding=1 cellSpacing=1 
style="HEIGHT: 98px; WIDTH: 588px" width="100%">
  
  <TR>
    <TD>
      <P align=center>Table Name</P></TD>
    <TD>
      <P align=center>Primary Key</P></TD>
    <TD>
      <P align=center>Add</P></TD>
    <TD>
      <P align=center>Mod</P></TD>
    <TD>
      <P align=center>Del</P></TD>
    <TD>
      <P align=center>Enq</P></TD></TR>
  <TR>
    <TD>MM_GRMST</TD>
    <TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO</TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
  <TR>
    <TD>MM_BETRN</TD>
    <TD>BE_STRTP,BE_PORNO,BE_MATCD,BE_CONNO,BE_BOENO</TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
  <TR>
    <TD>EX_DATRM</TD>
    <TD></TD>
    <TD>
      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD>
    <TD>
      <P align=center>&nbsp;</P></TD></TR></TABLE></P>
<P>
<P>
<DL>
  <DT><B>See Also:</B> 
  <DD><A href="serialized-form.html" target=sa_usrpf>Serialized 
Form</A></DD></DL>
<P>

<P>
<DL>
</DL>
*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.util.Properties;
import java.util.Date; 
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.Math;
import java.sql.ResultSet;
import java.util.Hashtable;

class mm_rpexc extends cl_rbase
{
	private JLabel lblDOCTP;
	private JLabel lblGRNDT;
	private JLabel lblSTRTP;
	/**TextField for Document Type i.e.:Reeipt or Issue.*/
	private TxtLimit txtDOCTP;
	/**TextField for Material Type i.e.:Raw Material or Utility Chemicals or Spares and Stores.*/
	private TxtLimit txtSTRTP;
	/**TextField for Party Name.*/
	private TxtLimit txtPRTNM;
	/**textField as CellEditor for Invoice/Bill of Entry No.*/
	private TxtLimit txtINVNO;
	/**TextField as cellEditor for Chalan No.*/
	private TxtLimit txtCHLNO;
	/**TextField for Document No i.e.: GRIN No for Receipts and Issue No for Issues.*/
	private TxtLimit txtDOCNO;
	/**TextField as CellEditor for Material description.*/
	private TxtLimit txtMATDS;
	/**TextField as CellEditor for Invoice Value.*/
	private TxtNumLimit txtINVVL;
	/**TextField as CellEditor for Excise Duty Percentage.*/
	private TxtNumLimit txtBEDPR;
	/**TextField as CellEditor for Excise duty Value.*/
	private TxtNumLimit txtBEDVL;
	/**TextField as CellEditor for Chalan Qty.*/
	private TxtNumLimit txtCHLQT;
	/**TextField as CellEditor for Material rate.*/
	private TxtNumLimit txtMATRT;
	/**TextField as CellEditor for Net Qty.*/
	private TxtNumLimit txtNETQT;
	/**TextField as CellEditor for chalan date.*/
	private TxtDate txtCHLDT;
	/**TextField as CellEditor for Invoice date.*/
	private TxtDate txtINVDT;
	/**JComboBox for Document Types.*/
	private JComboBox cmbDOCTP;
	/**JComboBox for Store Types/Material Type.*/
	private JComboBox cmbSTRTP;
	/**JButton to show Details and saving/updating new Values for Invoce Value,
	 * Excise Value, Excise percentage and Invoice No.*/
	private JButton btnSAVE;
	/**JPanel for Deatils table.	 */
	private JPanel pnlTBLDTL;
	/**String to contain path report file.*/
	private String strRESSTR ="c:\\reports\\mm_rpexc.doc"; 
	/**File Output Stream to write file at given path.*/
	FileOutputStream O_FOUT;
	/**Data Output Stream to write data into the file.*/
    DataOutputStream O_DOUT;
	/**String containing Document date.*/
	private String strDOCDT;
	private String strTOGRNDT;
	/**String to contain Queries.*/
	private String strSQLQRY;
	/**ResultSet.*/
	private ResultSet rstRSLSET;
	/**String for store type.*/
	final String strSTRTP = "04";	//RM/UC/SS
	int intRECCNT;
	/**Hash table for Store Types.*/
	private Hashtable<String,String> hstSTRTP;
	/**Hash table for Document Type.*/
	private Hashtable<String,String> hstDOCTP;
	/**JTable for Details.*/
	private cl_JTBL tblDETAIL;
	int TB1_CHKFL=0;
	int TB1_MATDS=1;
	int TB1_VENNM=2;
	int TB1_CHLNO=3;
	int TB1_CHLDT=4;
	int TB1_INVNO=5;
	int TB1_INVDT=6;
	int TB1_INVVL=7;
	int TB1_BEDVL=8;
	int TB1_BEDPR=9;
	int TB1_CHLQT=10;
	int TB1_MATRT=11;
	int TB1_NETQT=12;
	int TB1_MATCD=13;
	int TB1_VENCD=14;
	int TB1_CONNO=15;
	mm_rpexc()
	{
		super(2);
		try
		{	
			setMatrix(20,6);
			String[] L_staNAMES=new String[]{"FL","Mat.Desc","Party Name","Chln No","Chln Date","Inv No","Inv Date","Acc Val","BED Val","BED %ge","Chln Qty","Mat.Rate","Net Qty","MatCD","PrtCD","Conno"};
			int[] L_inaCOLSZ=new int[]{20,80,150,80,80,80,80,60,60,60,60,60,60,1,1,1};
            tblDETAIL=(cl_JTBL)crtTBLPNL(pnlTBLDTL=new JPanel(null),L_staNAMES,20,1,1,10,5.9,L_inaCOLSZ,new int[]{0}) ;
			tblDETAIL.setCellEditor(TB1_MATDS,txtMATDS=new TxtLimit(40));
			tblDETAIL.setCellEditor(TB1_VENNM,txtPRTNM=new TxtLimit(40));
			tblDETAIL.setCellEditor(TB1_CHLNO,txtCHLNO=new TxtLimit(15));
			tblDETAIL.setCellEditor(TB1_CHLDT,txtCHLDT=new TxtDate());
			tblDETAIL.setCellEditor(TB1_INVNO,txtINVNO=new TxtLimit(15));
			tblDETAIL.setCellEditor(TB1_INVDT,txtINVDT=new TxtDate());
			tblDETAIL.setCellEditor(TB1_CHLQT,txtCHLQT=new TxtNumLimit(12.3));
			tblDETAIL.setCellEditor(TB1_MATRT,txtMATRT=new TxtNumLimit(11.3));
			tblDETAIL.setCellEditor(TB1_INVVL,txtINVVL=new TxtNumLimit(12.3));
			tblDETAIL.setCellEditor(TB1_BEDVL,txtBEDVL=new TxtNumLimit(12.3));
			tblDETAIL.setCellEditor(TB1_BEDPR,txtBEDPR=new TxtNumLimit(5.2));
			tblDETAIL.setCellEditor(TB1_NETQT,txtNETQT=new TxtNumLimit(12.3));
			txtPRTNM.addActionListener(this);txtPRTNM.addFocusListener(this);
			txtPRTNM.addKeyListener(this);
			txtCHLNO.addActionListener(this);txtCHLNO.addFocusListener(this);
			txtCHLNO.addKeyListener(this);
			txtCHLDT.addActionListener(this);txtCHLDT.addFocusListener(this);
			txtCHLDT.addKeyListener(this);
			txtINVNO.addActionListener(this);txtINVNO.addFocusListener(this);
			txtINVNO.addKeyListener(this);
			txtINVDT.addActionListener(this);txtINVDT.addFocusListener(this);
			txtINVDT.addKeyListener(this);
			txtINVVL.addActionListener(this);txtINVVL.addFocusListener(this);
			txtINVVL.addKeyListener(this);
			txtBEDVL.addActionListener(this);txtBEDVL.addFocusListener(this);
			txtBEDVL.addKeyListener(this);
			txtBEDPR.addActionListener(this);txtBEDPR.addFocusListener(this);
			txtBEDPR.addKeyListener(this);
			txtMATRT.addActionListener(this);txtMATRT.addFocusListener(this);
			txtMATRT.addKeyListener(this);
			txtNETQT.addActionListener(this);txtNETQT.addFocusListener(this);
			txtNETQT.addKeyListener(this);
			txtCHLQT.addActionListener(this);txtCHLQT.addFocusListener(this);
			txtCHLQT.addKeyListener(this);
			txtMATDS.addActionListener(this);txtMATDS.addFocusListener(this);
			txtMATDS.addKeyListener(this);
			
			add(lblDOCTP = new JLabel("Document Type"),2,3,1,1,this,'L');
			add(cmbDOCTP = new JComboBox(),2,4,1,1,this,'L');
			add(lblSTRTP = new JLabel("Store Type"),3,3,1,1,this,'L');
			add(cmbSTRTP = new JComboBox(),3,4,1,1,this,'L');
			add(btnSAVE = new JButton("Details"),4,4,1,1,this,'L');
            add(pnlTBLDTL,6,1,11,6,this,'L');
			pnlTBLDTL.setVisible(false);
			updateUI();
			
			cmbDOCTP.addItem("Select");
			cmbSTRTP.addItem("Select");
			hstSTRTP = new Hashtable<String,String>(5,0.2f);
			hstDOCTP = new Hashtable<String,String>(5,0.2f);
			
			//Populating Store Type ComboBox
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS From CO_CDTRN Where";
			M_strSQLQRY += " CMT_CGMTP='SYS' And CMT_CGSTP = 'MMXXMTP'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				cmbSTRTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				hstSTRTP.put(M_rstRSSET.getString("CMT_CODDS"),M_rstRSSET.getString("CMT_CODCD"));
			}
			
			//Populating Document Type ComboBox
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS From CO_CDTRN Where";
			M_strSQLQRY += " CMT_CGMTP='SYS' And CMT_CGSTP = 'MMXXTTP'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				cmbDOCTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				hstDOCTP.put(M_rstRSSET.getString("CMT_CODDS"),M_rstRSSET.getString("CMT_CODCD"));
			}
			updateUI();
		}catch(Exception P_EX)
		{
			System.out.println("Error in mm_rpexc "+P_EX);
		}
	}
	/**exePRINT method, to Print Report on Printer, Show Report on Screen
	 * or Save Report at particular given location, calls getALLREC method.*/
	void exePRINT()
	{
		try
		{
			if(vldDATA())
			{
				getALLREC(); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					doPRINT("c:\\reports\\mm_rpexc.doc");
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
					if(intRECCNT > 0)
					{
						Runtime r = Runtime.getRuntime();
						Process p = null;
						try
						{
							p  = r.exec("c:\\windows\\wordpad.exe "+strRESSTR); 
						}catch(IOException L_EX){
							System.out.println("Error.exescrn.......... "+L_EX);
 						}
					}
					else
							setMSG("Record could not be found",'E');	
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				{
					if(intRECCNT > 0)
					{
						if(M_txtDESTN.getText().length()>0)
							setMSG("File Copied to file:"+M_txtDESTN.getText().trim()+"",'N');
						else
							setMSG("File Copied to file:c:\\reports\\mm_rpexc.doc",'N');
					}
					else
						setMSG("Record could not be found",'E');
				}
				/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
				{
					if(intRECCNT > 0)
					{
						try{
							cl_eml ocl_eml = new cl_eml();
							ocl_eml.sendfile(M_txtDESTN.getText().trim(),strRESSTR,cl_dat.M_STRMOD,"Test Mail");
						}catch(Exception L_EX){
							System.out.println("L_EX :"+L_EX);
 						}
						setMSG("Email Sent to " + M_txtDESTN.getText().trim() + " ",'N');
					}
				}*/
			}//vldDATA
		}catch(Exception L_EX)
		{
			System.out.println("Error in exePRINT "+L_EX);
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			M_objSOURC=L_AE.getSource();
			if(tblDETAIL.isEditing())
					tblDETAIL.getCellEditor().stopCellEditing();
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				M_lblTODAT.setVisible(false);
				M_txtTODAT.setVisible(false);
				pnlTBLDTL.setVisible(false);
				cl_dat.M_btnSAVE_pbst.setEnabled(false);
				btnSAVE.setText("Details");
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					M_txtFMDAT.setEditable(true);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
						M_txtFMDAT.requestFocus();
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
						M_txtDESTN.requestFocus();
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						M_cmbDESTN.requestFocus();
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
						M_cmbDESTN.requestFocus();
				}
			}
			else if(M_objSOURC==M_txtDESTN)
			{
				M_txtFMDAT.requestFocus();
			}
			else if(M_objSOURC == M_txtFMDAT)
			{
				if(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date can not be greater than today's date..",'E');
					pnlTBLDTL.setVisible(false);
				}
				else
				{
					cmbDOCTP.requestFocus();
				}
			}
			else if(M_objSOURC==btnSAVE)
			{
				tblDETAIL.cmpEDITR[TB1_CHKFL].setEnabled(false);
				Boolean TRUE=new Boolean(true);
				setMSG("",'N');
				if(btnSAVE.getText().equals("Details"))
				{
					if(M_txtFMDAT.getText().length()>0&&cmbDOCTP.getSelectedIndex()>0&&cmbSTRTP.getSelectedIndex()>0)
					{
						((cl_JTBL)tblDETAIL).clrTABLE();
						strDOCDT=M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()));
						strSQLQRY = "Select GR_CHLNO,GR_CHLDT,GR_VENNM,GR_MATCD,GR_VENCD,";
						strSQLQRY += "GR_BOENO,BE_BOEDT,BE_ACCVL,BE_DUTPR,BE_DUTVL,";
						strSQLQRY += "sum(GR_ACPQT) GR_ACPQT, sum(GR_CHLQT) GR_CHLQT,";
						strSQLQRY += "BE_ACCVL/sum(GR_CHLQT) MATRT";
						strSQLQRY += " From MM_GRMST Left Outer Join MM_BETRN";
						strSQLQRY += " On GR_STRTP = BE_STRTP And GR_BOENO = BE_BOENO";
						strSQLQRY += " And GR_PORNO = BE_PORNO And GR_MATCD = BE_MATCD and GR_CMPCD = BE_CMPCD";
						strSQLQRY += " Where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRNDT = '"+strDOCDT+"' And GR_STRTP = '"+strSTRTP+"'";
						strSQLQRY += " And GR_GRNTP = '47' And GR_MATCD <> '6805010045'";
						strSQLQRY += " And ifnull(GR_STSFL,'') <> 'X'";
						strSQLQRY += " And BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(BE_STSFL,'') <> 'X'";
						strSQLQRY += " Group by GR_CHLNO,GR_CHLDT,GR_VENNM,GR_MATCD,";
						strSQLQRY += " GR_BOENO,BE_BOEDT,GR_VENCD,BE_ACCVL,BE_DUTPR,BE_DUTVL";
						rstRSLSET = cl_dat.exeSQLQRY(strSQLQRY);
						int L_intROW=0;
						while(rstRSLSET.next())
						{
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_MATCD"),""),L_intROW,TB1_MATDS);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_VENNM"),""),L_intROW,TB1_VENNM);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_CHLNO"),""),L_intROW,TB1_CHLNO);
							tblDETAIL.setValueAt(rstRSLSET.getString("GR_CHLDT").equals("") ? "" : M_fmtLCDAT.format(rstRSLSET.getDate("GR_CHLDT")),L_intROW,TB1_CHLDT);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_BOENO"),""),L_intROW,TB1_INVNO);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("BE_BOEDT"),"").equals("") ? "" : M_fmtLCDAT.format(rstRSLSET.getDate("BE_BOEDT")),L_intROW,TB1_INVDT);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_CHLQT"),""),L_intROW,TB1_CHLQT);
							tblDETAIL.setValueAt(setNumberFormat(rstRSLSET.getDouble("MATRT"),3),L_intROW,TB1_MATRT);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("BE_ACCVL"),""),L_intROW,TB1_INVVL);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("BE_DUTVL"),"").trim(),L_intROW,TB1_BEDVL);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("BE_DUTPR"),"").trim(),L_intROW,TB1_BEDPR);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_ACPQT"),"").trim(),L_intROW,TB1_NETQT);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_MATCD"),"").trim(),L_intROW,TB1_MATCD);
							tblDETAIL.setValueAt(TRUE,L_intROW,TB1_CHKFL);
							tblDETAIL.setValueAt(nvlSTRVL(rstRSLSET.getString("GR_VENCD"),"").trim(),L_intROW++,TB1_VENCD);
						}
						M_strSQLQRY = "Select GR_BOENO GR_CHLNO,BE_BOEDT GR_CHLDT,GR_VENNM,";                  
						M_strSQLQRY += "GR_VENCD,GR_MATCD,BE_ACCVL,BE_DUTPR,BE_DUTVL,";
						M_strSQLQRY += "sum(GR_ACPQT) GR_ACPQT,sum(GR_CHLQT) GR_CHLQT,";
						M_strSQLQRY += "BE_ACCVL/sum(GR_CHLQT) MATRT";
						M_strSQLQRY += " From MM_GRMST Left Outer Join MM_BETRN";
						M_strSQLQRY += " On GR_STRTP = BE_STRTP And GR_BOENO = BE_BOENO";
						M_strSQLQRY += " And GR_PORNO = BE_PORNO And GR_MATCD = BE_MATCD and GR_CMPCD = BE_CMPCD";
						M_strSQLQRY += " Where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRNDT = '"+strDOCDT+"' And GR_STRTP = '"+strSTRTP+"'";
						M_strSQLQRY += " And GR_GRNTP = '47' And GR_MATCD = '6805010045'";
						M_strSQLQRY += " And ifnull(GR_STSFL,'') <> 'X'";
						M_strSQLQRY += " And BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(BE_STSFL,'') <> 'X'";
						M_strSQLQRY += " Group by GR_BOENO,BE_BOEDT,GR_VENNM,GR_MATCD,";                                   
						M_strSQLQRY += " GR_VENCD,BE_ACCVL,BE_DUTPR,BE_DUTVL";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						while(M_rstRSSET.next())
						{
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),L_intROW,TB1_MATDS);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),L_intROW,TB1_VENNM);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),""),L_intROW,TB1_CHLNO);
							tblDETAIL.setValueAt(M_rstRSSET.getString("GR_CHLDT").equals("") ? "" : M_fmtLCDAT.format(M_rstRSSET.getDate("GR_CHLDT")),L_intROW,TB1_CHLDT);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),""),L_intROW,TB1_INVNO);
							tblDETAIL.setValueAt(M_rstRSSET.getString("GR_CHLDT").equals("") ? "" : M_fmtLCDAT.format(M_rstRSSET.getDate("GR_CHLDT")),L_intROW,TB1_INVDT);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),""),L_intROW,TB1_CHLQT);
							tblDETAIL.setValueAt(setNumberFormat(M_rstRSSET.getDouble("MATRT"),3),L_intROW,TB1_MATRT);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_ACCVL"),""),L_intROW,TB1_INVVL);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_DUTVL"),"").trim(),L_intROW,TB1_BEDVL);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("BE_DUTPR"),"").trim(),L_intROW,TB1_BEDPR);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"").trim(),L_intROW,TB1_NETQT);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"").trim(),L_intROW,TB1_MATCD);
							tblDETAIL.setValueAt(TRUE,L_intROW,TB1_CHKFL);
							tblDETAIL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"").trim(),L_intROW++,TB1_VENCD);
						}
						pnlTBLDTL.setVisible(true);
						if(vldDATA())
						{
							cl_dat.M_btnSAVE_pbst.setEnabled(true);
							btnSAVE.setText("Save");
							btnSAVE.setEnabled(false);
						}
						else
						{
							cl_dat.M_btnSAVE_pbst.setEnabled(false);
							btnSAVE.setText("Save");
							btnSAVE.setEnabled(true);
						}
					}
					else
						setMSG("Please Input Proper Data",'E');
				}
				
				else if(btnSAVE.getText().equals("Save"))
				{
					for(int i=0;i<tblDETAIL.getRowCount();i++)
					{
						if(tblDETAIL.getValueAt(i,TB1_CHKFL).equals(new Boolean(true)))
						{
							M_strSQLQRY="Select count(*) from MM_BETRN where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_STRTP='"+strSTRTP+"'";
							M_strSQLQRY+=" And BE_BOENO='"+tblDETAIL.getValueAt(i,TB1_INVNO).toString()+"'";
							M_strSQLQRY+=" And BE_MATCD='"+tblDETAIL.getValueAt(i,TB1_MATCD).toString()+"'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next())
							{
								if(M_rstRSSET.getInt(1)>0)
								{		
									cl_dat.M_flgLCUPD_pbst=true;
									M_strSQLQRY = "Update MM_BETRN set";
									M_strSQLQRY += " BE_ACCVL="+setNumberFormat(Double.parseDouble(tblDETAIL.getValueAt(i,TB1_INVVL).toString()),3)+",";
									M_strSQLQRY += "BE_DUTVL="+setNumberFormat(Double.parseDouble(tblDETAIL.getValueAt(i,TB1_BEDVL).toString()),3)+",";
									M_strSQLQRY += "BE_DUTPR="+tblDETAIL.getValueAt(i,TB1_BEDPR).toString()+"";
									M_strSQLQRY += " Where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_STRTP = '"+strSTRTP+"'";
									M_strSQLQRY += " And BE_BOENO='"+tblDETAIL.getValueAt(i,TB1_INVNO).toString()+"'";
									M_strSQLQRY+=" and BE_MATCD='"+tblDETAIL.getValueAt(i,TB1_MATCD).toString()+"'";
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
									if(tblDETAIL.getValueAt(i,TB1_MATCD).toString().equals("6805010035"))
									{
										if(cl_dat.M_flgLCUPD_pbst)
										{
											M_strSQLQRY = "Update MM_GRMST Set GR_BOENO='"+tblDETAIL.getValueAt(i,TB1_INVNO).toString().trim()+"',";
											M_strSQLQRY += "GR_PORNO='"+tblDETAIL.getValueAt(i,TB1_INVNO).toString().trim()+"'";
											M_strSQLQRY += " Where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_CHLNO='"+tblDETAIL.getValueAt(i,TB1_CHLNO).toString()+"'";
											M_strSQLQRY += " And GR_MATCD='"+tblDETAIL.getValueAt(i,TB1_MATCD).toString()+"'";
											cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
										}
									}
								}
								else
								{
									cl_dat.M_flgLCUPD_pbst=true;
									M_strSQLQRY = "Insert into MM_BETRN (";
									M_strSQLQRY += "BE_STRTP,BE_PORNO,BE_MATCD,BE_UOMCD,";
									M_strSQLQRY += "BE_CONNO,BE_CONDT,BE_CONQT,BE_CONDS,";
									M_strSQLQRY += "BE_BOENO,BE_BOEDT,BE_LOCCD,BE_BOEQT,";
									M_strSQLQRY += "BE_ACCVL,BE_DUTVL,BE_DUTPR,BE_BOETP,";
									M_strSQLQRY += "BE_MATTP,BE_CHLQT,BE_STSFL,BE_LUSBY,";
									M_strSQLQRY += "BE_LUPDT,BE_NETQT,BE_TRNFL)";
									M_strSQLQRY += " Values('"+strSTRTP+"',";	//strtp
									M_strSQLQRY += "'"+tblDETAIL.getValueAt(i,TB1_INVNO).toString()+"',";//porno=invno
									M_strSQLQRY += "'"+tblDETAIL.getValueAt(i,TB1_MATCD).toString()+"',";//matcd
									M_strSQLQRY += "'MT',";//uomcd
									M_strSQLQRY += "'"+tblDETAIL.getValueAt(i,TB1_INVNO).toString().trim()+"',";//conno=invno
									M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINVDT.getText().trim()))+"',";//condt=invdt
									M_strSQLQRY += tblDETAIL.getValueAt(i,TB1_CHLQT).toString()+",";//conqt=chlqt
									M_strSQLQRY += "'"+tblDETAIL.getValueAt(i,TB1_INVNO).toString()+"',";//conds=invno
									M_strSQLQRY += "'"+tblDETAIL.getValueAt(i,TB1_INVNO).toString()+"',";//boeno=invno
									M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINVDT.getText().trim()))+"',";//boedt=invdt
									M_strSQLQRY += "'"+"AGS"+"',";//loccd
									M_strSQLQRY += tblDETAIL.getValueAt(i,TB1_CHLQT).toString()+",";//boeqt=chlqt
									M_strSQLQRY += tblDETAIL.getValueAt(i,TB1_INVVL).toString()+",";//accvl
									M_strSQLQRY += tblDETAIL.getValueAt(i,TB1_BEDVL).toString()+",";//dutvl
									M_strSQLQRY += tblDETAIL.getValueAt(i,TB1_BEDPR).toString()+",";//dutpr
									M_strSQLQRY += "'"+"01"+"',";//boetp
									M_strSQLQRY += "'"+"01"+"',";//mattp
									M_strSQLQRY += tblDETAIL.getValueAt(i,TB1_CHLQT).toString()+",";//chlqt
									M_strSQLQRY += "'"+"0"+"',";//stsfl
									M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";//lusby
									M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";//lupdt
									M_strSQLQRY += tblDETAIL.getValueAt(i,TB1_NETQT).toString()+",";//netqt
									M_strSQLQRY += "'"+"0"+"')";//trnfl
									cl_dat.exeSQLUPD(M_strSQLQRY,"seTLCLUPD");
									if(tblDETAIL.getValueAt(i,TB1_MATCD).toString().equals("6805010035"))
									{
										if(cl_dat.M_flgLCUPD_pbst)
										{
											M_strSQLQRY = "Update MM_GRMST Set GR_BOENO='"+tblDETAIL.getValueAt(i,TB1_INVNO).toString().trim()+"',";
											M_strSQLQRY += "GR_PORNO='"+tblDETAIL.getValueAt(i,TB1_INVNO).toString().trim()+"'";
											M_strSQLQRY += " Where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_CHLNO='"+tblDETAIL.getValueAt(i,TB1_CHLNO).toString()+"'";
											M_strSQLQRY += " And GR_MATCD='"+tblDETAIL.getValueAt(i,TB1_MATCD).toString()+"'";
											cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
										}
									}
								}
								
							}
						}
					}
					if(cl_dat.exeDBCMT("actionPerformed"))
					{
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
						btnSAVE.setText("Details");
					}
				}
			}
			else if(M_objSOURC==cmbDOCTP)
			{
				if(cmbDOCTP.getSelectedItem().toString().equals("ISSUE"))
				{
					btnSAVE.setVisible(false);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
				}
				else
				{
					btnSAVE.setVisible(true);
					cl_dat.M_btnSAVE_pbst.setEnabled(false);
				}
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Date Time Comparision");
		}
	}
	
	public void itemStateChanged(ItemEvent L_IE)
	{		
		super.itemStateChanged(L_IE);
		try
		{
			if(M_objSOURC==cmbDOCTP)
				cmbSTRTP.showPopup();			
		}catch(Exception L_EX)
		{
			System.out.println("Error in ItemStateChanged "+L_EX);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			int L_intKEYVL=L_KE.getKeyCode();
			if(L_intKEYVL==L_KE.VK_MULTIPLY)
			//if(M_objSOURC==L_KE.VK_MULTIPLY)
			{
				M_cmbDESTN.setVisible(false);
				M_txtDESTN.setVisible(false);
				M_lblDESTN.setVisible(false);
				M_lblFMDAT.setVisible(false);
				M_txtFMDAT.setVisible(false);
				M_lblTODAT.setVisible(false);
				M_txtTODAT.setVisible(false);
			}
		}catch(Exception L_EX)
		{
			System.out.println("Error in KeyPressed "+L_EX);
		}
	}
	
	private void prnHEADER()	//gets the Header of the Report
	{  
		try
		{
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n\n");
			O_DOUT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",66));
			O_DOUT.writeBytes(padSTRING('L',"REPORT DATE : "+cl_dat.M_strLOGDT_pbst,79));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"EXCISE DATA TRANSFER DETAILS" ,121));
			O_DOUT.writeBytes("PAGE No.    : " + padSTRING('L',String.valueOf(cl_dat.M_PAGENO),10) + "\n");
			O_DOUT.writeBytes(cmbDOCTP.getSelectedItem().toString() + " AS ON " + M_txtFMDAT.getText().trim() + " FOR " + cmbSTRTP.getSelectedItem().toString());
			O_DOUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 6;
		}catch(Exception L_EX){
			System.out .println ("L_EX..prnhead....... :"+L_EX);
		}
	}

	private void getTBLHDR()
	{
		try
		{
			crtLINE(150);
			if(hstDOCTP.get(cmbDOCTP.getSelectedItem().toString()).toString().equals("RC"))
			{	
				O_DOUT.writeBytes("\nSr. Invoice No.    Vendor Code                          Material Code               Invoice    Invoice   Invoice   B.E.D.   B.E.D.  Recipt     Receipt");
				O_DOUT.writeBytes("\nNo. Date.          Name                                 Desc.                       Qty.       Rate      Value     %age.    Value   No.        Qty. \n");
			}
			else
			{	
				O_DOUT.writeBytes("\nSr. Material Code  Description                               Issue      Issue");
				O_DOUT.writeBytes("\nNo.                                                          No.        Qty. \n");
			}
			crtLINE(150);
			O_DOUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 4;
		}catch(Exception L_EX){
			System.out.println(L_EX.toString());
		}
	}
	/***/
	private void prnFOOTR()
	{
		try
		{
			O_DOUT.writeBytes ("\n\n");
			crtLINE(150);
			O_DOUT.writeBytes ("\n\n");
			O_DOUT.writeBytes ("\n");
				
			cl_dat.M_intLINNO_pbst += 5;
			prnFMTCHR(O_DOUT,M_strEJT);				
			prnFMTCHR(O_DOUT,M_strCPI17);
		}catch(Exception L_EX)
		{
			//showEXMSG(L_EX,"prnFOOTR","");
			setMSG(L_EX,"prnFOOTR");
		}
	}
	/**To Print Line.*/
	private void crtLINE(int LM_CNT)
	{
		String strln = "";
		try
		{
			for(int i=1;i<=LM_CNT;i++)
				strln += "-";
			O_DOUT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}
	/**This Method fetch records from MM_GRMST,MM_BETRN and CO_CTMST for given
	 * Date, Doc Type & Material Type. It Stores Data into  String Buffer and Write
	 * String Buffer into Data Output Stream.*/
	private void getALLREC() // fetches all the records
	{
		try
		{
			String L_strSELQRY="",L_strINSQRY="";
			String L_strDOCTP="",L_strSTRTP="";
			ResultSet L_rstRSLSET=null;
			StringBuffer L_stbPRNSTR;
			String L_strINVNO="",L_strINVDT,L_strVENCD,L_strVENNM,L_strVENTP;
			String L_strMATCD,L_strMATDS,L_strINVQT,L_strINVVL,L_strINVRT;
			String L_strDUTPR,L_strDUTVL,L_strDOCNO,L_strRECQT,L_strCONNO;
			String L_strPORNO,L_strMATTP,L_strDOCDT,L_strISSQT,L_strTEMP="";
			float L_fltTRECQT,L_fltTBOEQT,L_fltTACCVL,L_fltTISSQT;
			float L_fltTDUTPR,L_fltTDUTVL,L_fltTPORRT;
			int L_intSRLNO;
			String L_strDESTN=M_txtDESTN.getText().trim();
			strDOCDT=M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()));
			intRECCNT = 0;
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			try
			{
				if(M_txtDESTN.isVisible()==true&&L_strDESTN.length()>0)
				{
					L_strDESTN+=".doc";
					O_FOUT = new FileOutputStream(L_strDESTN);
				}
				else
				{
					O_FOUT = new FileOutputStream("c:\\reports\\mm_rpexc.doc");
					
				}
				
				O_DOUT = new DataOutputStream(O_FOUT);
				cl_dat.M_PAGENO = 0;
				cl_dat.M_intLINNO_pbst = 85;
				prnFMTCHR(O_DOUT,M_strCPI17);
				
				if(hstDOCTP.get(cmbDOCTP.getSelectedItem().toString()).toString().equals("RC"))//********"RC--RM/UC/SS.."*********
				{
					L_strDOCTP = hstDOCTP.get(cmbDOCTP.getSelectedItem().toString()).toString();
					if(hstSTRTP.get(cmbSTRTP.getSelectedItem().toString()).toString().equals("RM"))
					{	
						L_strSTRTP = hstSTRTP.get(cmbSTRTP.getSelectedItem().toString()).toString();
						M_strSQLQRY = "Select GR_BOENO,GR_VENCD,GR_VENNM,GR_MATCD,GR_GRNDT,";
						M_strSQLQRY += " GR_GRNNO,GR_RECQT,GR_PORNO,BE_BOEQT,BE_ACCVL,";
						M_strSQLQRY += " BE_DUTPR,BE_DUTVL,BE_CONNO,BE_BOENO,BE_BOEDT,BE_MATTP,";
						M_strSQLQRY += " (BE_ACCVL/BE_BOEQT) L_RATE,CT_MATDS From CO_CTMST,";
						M_strSQLQRY += " MM_GRMST Left Outer Join MM_BETRN";
						M_strSQLQRY += " On GR_STRTP = BE_STRTP";
						M_strSQLQRY += " And GR_BOENO = BE_BOENO";
						M_strSQLQRY += " And GR_PORNO = BE_PORNO";
						M_strSQLQRY += " And GR_MATCD = BE_MATCD";
						M_strSQLQRY += " Where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRNDT = '"+strDOCDT+"'";
						M_strSQLQRY += " And GR_STRTP = '"+strSTRTP+"'";	//Raw Material && Furnace Min. Oil
						M_strSQLQRY += " And GR_GRNTP = '"+47+"'";		//Exbonded
						M_strSQLQRY += " And GR_MATCD = CT_MATCD";
						M_strSQLQRY += " And ifnull(GR_STSFL,'') <> 'X'";
						M_strSQLQRY += " And ifnull(BE_STSFL,'') <> 'X'";
						M_strSQLQRY += " And ifnull(CT_STSFL,'') <> 'X'";
						M_strSQLQRY += " Order by GR_BOENO";
						L_rstRSLSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						L_stbPRNSTR = new StringBuffer("");
						L_fltTRECQT = L_fltTBOEQT = L_fltTACCVL = 0;
						L_fltTDUTPR = L_fltTDUTVL = L_fltTPORRT = 0;
								
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO = 1;
						prnHEADER(); //gets the Header of the report
						getTBLHDR(); // gets the Header of the Table
				
						while(L_rstRSLSET.next())
						{
							L_intSRLNO = intRECCNT + 1;
							L_strINVNO = nvlSTRVL(L_rstRSLSET.getString("BE_BOENO"),"").trim();
							L_strVENCD = nvlSTRVL(L_rstRSLSET.getString("GR_VENCD"),"").trim();
							L_strVENNM = nvlSTRVL(L_rstRSLSET.getString("GR_VENNM"),"").trim();
							L_strMATCD = nvlSTRVL(L_rstRSLSET.getString("GR_MATCD"),"").trim();
							L_strDOCDT = M_fmtLCDAT.format(L_rstRSLSET.getDate("GR_GRNDT"));
							L_strDOCNO = nvlSTRVL(L_rstRSLSET.getString("GR_GRNNO"),"").trim();
							L_strRECQT = nvlSTRVL(L_rstRSLSET.getString("GR_RECQT"),"").trim();
							L_strPORNO = nvlSTRVL(L_rstRSLSET.getString("GR_PORNO"),"").trim();
							L_strINVQT = nvlSTRVL(L_rstRSLSET.getString("BE_BOEQT"),"").trim();
							L_strINVVL = nvlSTRVL(L_rstRSLSET.getString("BE_ACCVL"),"").trim();
							L_strDUTPR = nvlSTRVL(L_rstRSLSET.getString("BE_DUTPR"),"").trim();
							L_strDUTVL = nvlSTRVL(L_rstRSLSET.getString("BE_DUTVL"),"").trim();
							L_strCONNO = nvlSTRVL(L_rstRSLSET.getString("BE_CONNO"),"").trim();
							L_strMATTP = nvlSTRVL(L_rstRSLSET.getString("BE_MATTP"),"").trim();
							if(nvlSTRVL(L_rstRSLSET.getString("BE_BOEDT"),"").equals(""))
								L_strINVDT="";
							else
								L_strINVDT = M_fmtLCDAT.format(L_rstRSLSET.getDate("BE_BOEDT"));
							//L_strINVRT = Double.toString(L_rstRSLSET.getDouble("L_RATE"));
							L_strINVRT = setNumberFormat(L_rstRSLSET.getDouble("L_RATE"),3);
							L_strMATDS = nvlSTRVL(L_rstRSLSET.getString("CT_MATDS"),"").trim();
							
							if(L_stbPRNSTR.length() != 0)
								L_stbPRNSTR.delete(0,L_stbPRNSTR.length());
									
							if(cl_dat.M_intLINNO_pbst > 65)
							{
								crtLINE(150);
								prnFMTCHR(O_DOUT,M_strEJT);				
								cl_dat.M_intLINNO_pbst = 0;
								cl_dat.M_PAGENO += 1;
								prnHEADER(); //gets the Header of the report
								getTBLHDR(); // gets the Header of the Table
							}
							
							if(!L_strINVNO.equals(L_strTEMP)||L_strINVNO.length()<1)
							{
								if(intRECCNT > 0)	// To print the Total
								{
									prnFMTCHR(O_DOUT,M_strBOLD);
									O_DOUT.writeBytes("                                                                                                                                    Total :");
									O_DOUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTRECQT)),3),10));
									prnFMTCHR(O_DOUT,M_strNOBOLD);
									O_DOUT.writeBytes("\n");
								}
								L_stbPRNSTR.append("\n");
								L_stbPRNSTR.append(padSTRING('L',String.valueOf(L_intSRLNO),3));
								L_stbPRNSTR.append(" ");
								L_stbPRNSTR.append(padSTRING('R',L_strINVNO,15));
								L_stbPRNSTR.append(padSTRING('R',"S"+"/"+L_strVENCD,36));
								L_stbPRNSTR.append(" ");
								L_stbPRNSTR.append(padSTRING('R',L_strMATCD,25));
								L_stbPRNSTR.append(padSTRING('L',L_strINVQT,10));
								L_stbPRNSTR.append(padSTRING('L',L_strINVRT,10));
								L_stbPRNSTR.append(padSTRING('L',L_strINVVL,10));
								L_stbPRNSTR.append(padSTRING('L',L_strDUTPR,9));
								L_stbPRNSTR.append(padSTRING('L',L_strDUTVL,10));
								L_stbPRNSTR.append(padSTRING('L',L_strDOCNO,10));
								L_stbPRNSTR.append(padSTRING('L',L_strRECQT,9));
								L_stbPRNSTR.append("  ");
								L_stbPRNSTR.append("\n");
								L_stbPRNSTR.append(padSTRING('L',"",3));
								L_stbPRNSTR.append(" ");
								L_stbPRNSTR.append(padSTRING('R',L_strINVDT,15));
								L_stbPRNSTR.append(padSTRING('R',L_strVENNM,36));
								L_stbPRNSTR.append(" ");
								L_stbPRNSTR.append(padSTRING('R',L_strMATDS,30));
								L_stbPRNSTR.append("  ");
								L_stbPRNSTR.append("\n");
								intRECCNT++;
								O_DOUT.writeBytes(L_stbPRNSTR.toString());
								cl_dat.M_intLINNO_pbst++;
								L_strTEMP=L_strINVNO;
								L_fltTRECQT = 0;	
								L_fltTRECQT += Float.parseFloat(L_strRECQT);
							}
							else
							{
								L_stbPRNSTR.append(padSTRING('R',"",130));
								L_stbPRNSTR.append(padSTRING('L',L_strDOCNO,10));
								L_stbPRNSTR.append(padSTRING('L',L_strRECQT,9));
								L_strTEMP=L_strINVNO;
								L_stbPRNSTR.append("  ");
								L_stbPRNSTR.append("\n");
								O_DOUT.writeBytes(L_stbPRNSTR.toString());
								cl_dat.M_intLINNO_pbst++;
								L_fltTRECQT += Float.parseFloat(L_strRECQT);
							}
						}//end While Loop
						if(intRECCNT > 0)	// To print th Total of the last record
						{
							prnFMTCHR(O_DOUT,M_strBOLD);
							O_DOUT.writeBytes("                                                                                                                                    Total :");
							O_DOUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTRECQT)),3),10));
							prnFMTCHR(O_DOUT,M_strNOBOLD);
							O_DOUT.writeBytes("\n");
						}
					}
				}//*****"end RC--RM/UC/SS.."******
				else if(hstDOCTP.get(cmbDOCTP.getSelectedItem().toString()).toString().equals("IS"))//********"IS--RM/UC/SS.."*********
				{
					L_strDOCTP = hstDOCTP.get(cmbDOCTP.getSelectedItem().toString()).toString();
					if(hstSTRTP.get(cmbSTRTP.getSelectedItem().toString()).toString().equals("RM"))
					{	
						L_strSTRTP = hstSTRTP.get(cmbSTRTP.getSelectedItem().toString()).toString();
						M_strSQLQRY = "Select IS_STRTP,IS_ISSNO,IS_MATCD,IS_MATTP,";
						M_strSQLQRY += "IS_ISSDT,IS_ISSQT,CT_MATDS From MM_ISMST,";
						M_strSQLQRY += "CO_CTMST Where IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_ISSDT = '"+strDOCDT+"'";
						M_strSQLQRY += " And IS_ISSTP = '"+47+"'";
						M_strSQLQRY += " And IS_STRTP = '"+strSTRTP+"'";
						M_strSQLQRY += " And IS_MATCD = CT_MATCD";
						L_rstRSLSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						
						L_stbPRNSTR = new StringBuffer("");
						L_fltTISSQT = 0;
														
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO = 1;
						prnHEADER(); //gets the Header of the report
						getTBLHDR(); // gets the Header of the Table
						L_strINVNO="";
						L_strTEMP="";
						while(L_rstRSLSET.next())
						{
							L_intSRLNO = intRECCNT + 1;
							L_strMATCD = nvlSTRVL(L_rstRSLSET.getString("IS_MATCD"),"").trim();
							L_strMATDS = nvlSTRVL(L_rstRSLSET.getString("CT_MATDS"),"").trim();
							L_strDOCDT = M_fmtLCDAT.format(L_rstRSLSET.getDate("IS_ISSDT"));
							L_strDOCNO = nvlSTRVL(L_rstRSLSET.getString("IS_ISSNO"),"").trim();
							L_strISSQT = nvlSTRVL(L_rstRSLSET.getString("IS_ISSQT"),"").trim();
							
							if(L_stbPRNSTR.length() != 0)
								L_stbPRNSTR.delete(0,L_stbPRNSTR.length());
									
							if(cl_dat.M_intLINNO_pbst > 65)
							{
								crtLINE(150);
								prnFMTCHR(O_DOUT,M_strEJT);				
								cl_dat.M_intLINNO_pbst = 0;
								cl_dat.M_PAGENO += 1;
								prnHEADER(); //gets the Header of the report
								getTBLHDR(); // gets the Header of the Table
							}
							
							if(!L_strINVNO.equals(L_strTEMP)||L_strINVNO.length()<1)
							{	
								if(intRECCNT > 0)	// To print the Total
								{
									prnFMTCHR(O_DOUT,M_strBOLD);
									O_DOUT.writeBytes("                                                             Total :");
									O_DOUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTISSQT)),3),10));
									prnFMTCHR(O_DOUT,M_strNOBOLD);
									O_DOUT.writeBytes("\n");
								}
								L_stbPRNSTR.append("\n");
								L_stbPRNSTR.append(padSTRING('L',String.valueOf(L_intSRLNO),3));
								L_stbPRNSTR.append(" ");
								L_stbPRNSTR.append(padSTRING('R',L_strMATCD,15));
								L_stbPRNSTR.append(padSTRING('R',L_strMATDS,40));
								L_stbPRNSTR.append(padSTRING('L',L_strDOCNO,10));
								L_stbPRNSTR.append(padSTRING('L',L_strISSQT,9));
								L_stbPRNSTR.append("  ");
								L_stbPRNSTR.append("\n");
								intRECCNT++;
								O_DOUT.writeBytes(L_stbPRNSTR.toString());
								cl_dat.M_intLINNO_pbst++;
								L_strTEMP=L_strINVNO;
								L_fltTISSQT = 0;	
								L_fltTISSQT += Float.parseFloat(L_strISSQT);
							}
							else
							{
								L_stbPRNSTR.append(padSTRING('R',"",54));
								L_stbPRNSTR.append(padSTRING('L',L_strDOCNO,10));
								L_stbPRNSTR.append(padSTRING('L',L_strISSQT,9));
								L_strTEMP=L_strINVNO;
								L_stbPRNSTR.append("  ");
								L_stbPRNSTR.append("\n");
								O_DOUT.writeBytes(L_stbPRNSTR.toString());
								cl_dat.M_intLINNO_pbst++;
								L_fltTISSQT += Float.parseFloat(L_strISSQT);
							}
						}//end While Loop
						if(intRECCNT > 0)	// To print th Total of the last record
						{
							prnFMTCHR(O_DOUT,M_strBOLD);
							O_DOUT.writeBytes("                                                             Total :");
							O_DOUT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTISSQT)),3),10));
							prnFMTCHR(O_DOUT,M_strNOBOLD);
							O_DOUT.writeBytes("\n");
						}
					}
				}//end else if IS--RM/UC/SS..
				crtLINE(150);
				prnFMTCHR(O_DOUT,M_strEJT);				
				prnFMTCHR(O_DOUT,M_strNOCPI17);
				O_DOUT.close();
				O_FOUT.close();
					
				L_rstRSLSET.close();
				setMSG("",'N');
			
				cc_exact.occ_exact=new cc_exact();
				cc_exact.occ_exact.setTNKDATA(L_strDOCTP,L_strSTRTP,strDOCDT);
				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}catch(IOException e)
			{
				System.out.println("Error in writing the file : " + e.toString());
			}
			finally
			{
				System.out.println("File closed successfully");
				O_DOUT.close();
				O_FOUT.close();
			}
		}catch(Exception L_EX)
		{
			System.out.println("Error in getALLREC "+L_EX);
		}
	}
	/**Checks The Validity of Data for Input.*/
	boolean vldDATA()
	{
		if(M_txtFMDAT.getText().length()<1)
		{
			setMSG("Please Enter the date",'E');
			M_txtFMDAT.requestFocus();
			return false;
		}
		else if(cmbDOCTP.getSelectedIndex()<1)
		{
			setMSG("Please select Document Type",'E');
			cmbDOCTP.requestFocus();
			return false;
		}
		else if(cmbSTRTP.getSelectedIndex()<1)
		{
			setMSG("Please select Store Type",'E');
			cmbSTRTP.requestFocus();
			return false;
		}
		else
		{
			for(int L_intROW=0;L_intROW<tblDETAIL.getRowCount();L_intROW++)
			{
				if(tblDETAIL.getValueAt(L_intROW,TB1_CHKFL).equals(new Boolean(true)))
				{
					if(tblDETAIL.getValueAt(L_intROW,TB1_INVNO).toString().equals(""))
					{
						setMSG("Please enter Invoice Number",'E');
						return false;
					}
					else if(tblDETAIL.getValueAt(L_intROW,TB1_INVDT).toString().equals(""))
					{
						setMSG("Please enter Invoice Date",'E');
						return false;
					}
					else if(tblDETAIL.getValueAt(L_intROW,TB1_INVVL).toString().equals(""))
					{
						setMSG("Please enter Accessible Values",'E');
						return false;
					}
					else if(Double.parseDouble(tblDETAIL.getValueAt(L_intROW,TB1_INVVL).toString())==0.0)
					{
						setMSG("Accessible Value can not be less than 1",'E');
						return false;
					}
					else if(tblDETAIL.getValueAt(L_intROW,TB1_BEDVL).toString().equals(""))
					{
						setMSG("Please enter Excise Duty Values",'E');
						return false;
					}
					else if(Double.parseDouble(tblDETAIL.getValueAt(L_intROW,TB1_BEDVL).toString())==0.0)
					{
						setMSG("Excise Duty Value can not be less than 1",'E');
						return false;
					}
					else if(tblDETAIL.getValueAt(L_intROW,TB1_BEDPR).toString().equals(""))
					{
						setMSG("Please enter Capacity",'E');
						return false;
					}
					else if(Double.parseDouble(tblDETAIL.getValueAt(L_intROW,TB1_BEDPR).toString())==0.0)
					{
						setMSG("Excise Duty Percent can not be less than 1",'E');
						return false;
					}
				}
			}
		}
		return true;
	}
}