/*
System Name	  : Material Management System
Program Name  : Cylinder Transaction 
Program Desc. :
Author        : A.T.Chaudhari
Date          : 16/07/2004
Version       : 
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT> </P><P><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white><TR><TD>System Name</TD><TD>Material Management System </TD></TR><TR><TD>Program Name</TD><TD>Cylinder Transaction Report</TD></TR><TR><TD>Program Desc</TD><TD>                                                        Generate Cylinder transaction report deepending on option </TD></TR><TR><TD>Basis Document</TD><TD>   </TD></TR><TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_rpcyl.class</TD></TR> <TR><TD>Source path</TD><TD>f:\source\splerp2\mm_rpcyl.java</TD></TR> <TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR> <TR><TD>Date</TD><TD>30/08/2004 </TD></TR> <TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR> <TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR> <TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR> <TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR></TABLE></P><P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P> <P> <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue> <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR> <TR><TD>CO_PTMST</TD><TD>PT_PRTTP,PT_PRTCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>CO_CDTRN</TD><TD>CMT_CGMTP,CMT_CGSTP,CMT_CODCD </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>MM_CLTRN</TD><TD></TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> <TR><TD>CO_CTMST</TD><TD>CT_GRPCD,CT_CODTP,CT_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR> </TABLE> </P>*/
class mm_rpcyl extends cl_rbase
{									/**Text Field for from date	 */
	private JTextField txtFMDAT;	/**Text Field For to date	 */
	private JTextField txtTODAT;	/**ComboBox For Report Options  */
	private cl_Combo cmbRPTOP;		/**Combobox Report Order 	 */
	private cl_Combo cmbRPORD;		/**Tabel To Display Cylander Details  */
	private cl_JTable tblCLTRN;		/**Panel To Display Cylinder Details Other Than Table  */
	private  JPanel pnlCLTRN;		/**Integer Hold Maximum Number Of Items In Vector   */
	private int intCOUNT;			/**Hashtable To Strore Department Name W.R.T. Department Code  */
	private Hashtable<String,String> hstDPTCD;		/**Hashtable For Statu Code And Description */
	private Hashtable<String,String> hstSTSFL;		/**Hashtable For Vendor code	 */
	private Hashtable<String,String> hstVENCD;		/**Vector For Cylinder Details Other Than Table  */
	private Vector<String> vtrCLTRN;		/**Vector for extra description used to print report   */
	private Vector<String> vtrMATDS;		/**Constant String variabel for File name  */
	private final String strFILNM = "C:\\Reports\\mm_rpcyl.doc";
	private FileOutputStream fosREPORT;
	private DataOutputStream dosREPORT;	
	public mm_rpcyl()
	{
		super(2);
		try
		{
			intCOUNT = 0;
			//Remove the component added by super class
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			vtrCLTRN = new Vector<String>(10,5);
			vtrMATDS = new Vector<String>(10,5);
			cmbRPTOP = new cl_Combo();
			//Add report option to report option combo box
			cmbRPTOP.addItem("Select an Option","");
			cmbRPTOP.addItem("Pending For Acceptance","'0'");
			cmbRPTOP.addItem("Full Cylinder With Stores","'1'");
			cmbRPTOP.addItem("Full Cylinder With Plant","'2'");
			cmbRPTOP.addItem("Empty Cylinder With Plant","'3'");
			cmbRPTOP.addItem("Empty Cylinder With Stores","'5'");
			cmbRPTOP.addItem("Cylinder Left The Plant","'7'");
			cmbRPORD = new cl_Combo();
			//add option to report order combo box
			cmbRPORD.addItem("By Material Code","CLT_MATCD");
			cmbRPORD.addItem("By Issue Number","CLT_ISSNO");
			cmbRPORD.addItem("By Vendor Code","CLT_VENCD");
			cmbRPORD.addItem("By Status","CLT_STSFL");
			hstDPTCD = new Hashtable<String,String>(10,0.8f);			
			hstSTSFL = new Hashtable<String,String>(10,0.8f);
			hstVENCD = new Hashtable<String,String>(10,0.8f);
			
			/**Store Department Code And Department Name In hstDPTCD			 */
			M_strSQLQRY = "SELECT  DISTINCT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
				+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXDPT' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
				hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
			M_rstRSSET.close();
			
			/**save vendor code and vendor name in hstVENCD	 */
			M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP = 'S' ";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				hstVENCD.put(M_rstRSSET.getString("PT_PRTCD"),M_rstRSSET.getString("PT_PRTNM"));
			}
			/**Stored Status Code And Status Description In hstSTSFL	 */
			/*
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
				+"CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSTS' ORDER BY CMT_CODCD ";
			M_strRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
				hstSTSFL.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
			M_rstRSSET.close();
			*/
			hstSTSFL.put("0","Pending For Acceptance");
			hstSTSFL.put("1","Full Cylinder With Stores");
			hstSTSFL.put("2","Full Cylinder With Plant");
			hstSTSFL.put("3","Empty Cylinder With Plant");
			hstSTSFL.put("4","Return Full Req Raised");
			hstSTSFL.put("5","Empty Cylinder With Stores");
			hstSTSFL.put("6","Full Accepted");
			hstSTSFL.put("7","Cylinder Left The Palnt");
			hstSTSFL.put("8","RGP");
			hstSTSFL.put("X","Deleted");
			setMatrix(20,8);
			add(new JLabel("From Date"),2,1,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),2,2,1,1,this,'L');
			add(new JLabel("To Date"),2,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),2,4,1,1,this,'L');
			add(new JLabel("Report Option"),3,1,1,1,this,'L');
			add(cmbRPTOP,3,2,1,2,this,'L');
			add(new JLabel("Order Type"),4,1,1,1,this,'L');
			add(cmbRPORD,4,2,1,1.5,this,'L');
			tblCLTRN = crtTBLPNL1(this,new String[]{"","Material Code","Material Description","Srl No.","Status","GRIN Date","Vendor Code","Issue To.","Ret. Dt.","Out Date"},150,5,1,7.76,8,new int[]{20,80,200,65,65,65,65,65,65,65},new int[]{0});
			cmbRPTOP.addActionListener(this);
			cmbRPORD.addActionListener(this);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**Method for action performed	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() > 0)
					txtFMDAT.requestFocus();
			}
			/**Display the details of cylinder in the table on action report order combobox and report option combobox		 */
			if(M_objSOURC == cmbRPORD || M_objSOURC == cmbRPTOP)
			{
				if(vldDATA())
				{
					tblCLTRN.clrTABLE();
					if(tblCLTRN.isEditing())
						tblCLTRN.getCellEditor().stopCellEditing();
					if(!vtrCLTRN.isEmpty())
						vtrCLTRN.removeAllElements();
					/**Query For Cylinder Details  */
					M_strSQLQRY = "SELECT CLT_MATCD,CLT_SRLNO,CLT_STSFL,CLT_GRNDT,CLT_VENCD,"
						+"CLT_DPTCD,CLT_RTSDT,CLT_MGPDT,CLT_GRNNO,CLT_ISSNO,CLT_CRNNO,CLT_CRNDT,"
						+"CLT_RFDAY,CLT_RNTRT,CLT_MGPNO,CLT_MGPDT,CLT_RNTAM,CLT_BILRF,"
						+"((DAYS(CLT_GRNDT) + CLT_RFDAY) - DAYS(CURRENT_DATE)) AS AVDAY,CT_MATDS "
						+"FROM MM_CLTRN,CO_CTMST WHERE CT_MATCD = CLT_MATCD  AND "
						+"CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND CLT_GRNDT BETWEEN '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
						+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
						M_strSQLQRY += "AND isnull(CLT_STSFL,'') = "+cmbRPTOP.getITMCD()+" ";
						M_strSQLQRY += "ORDER BY  "+cmbRPORD.getITMCD();
					//System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					int i = 0;
					if(M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
						{
						   tblCLTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("CLT_MATCD"),""),i,1);
						   tblCLTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),i,2);
						   tblCLTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("CLT_SRLNO"),""),i,3);
						   tblCLTRN.setValueAt(hstSTSFL.get(nvlSTRVL(M_rstRSSET.getString("CLT_STSFL"),"")),i,4);
						   if(M_rstRSSET.getDate("CLT_GRNDT") != null)
							   tblCLTRN.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_GRNDT")),i,5);
						   else
							   tblCLTRN.setValueAt(" ",i,5);
						   tblCLTRN.setValueAt(nvlSTRVL(M_rstRSSET.getString("CLT_VENCD"),""),i,6);
						   tblCLTRN.setValueAt(hstDPTCD.get(nvlSTRVL(M_rstRSSET.getString("CLT_DPTCD"),"")),i,7);
						   if(M_rstRSSET.getDate("CLT_RTSDT") != null)
							   tblCLTRN.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getString("CLT_RTSDT")),i,8);
						   else
							   tblCLTRN.setValueAt(" ",i,8);
						   if(M_rstRSSET.getDate("CLT_MGPDT") != null)
							   tblCLTRN.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getString("CLT_MGPDT")),i,9);
						   else
							   tblCLTRN.setValueAt(" ",i,9);
						  
						   /**String For Cylinder Details(Sequence is Importent For Display Details)  
						    * This string is used to display information on pressing f9*/
						   String L_strCLTRN =  nvlSTRVL(M_rstRSSET.getString("CLT_GRNNO"),"")+"|";
						   if(M_rstRSSET.getDate("CLT_GRNDT") != null)
							   L_strCLTRN += M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_GRNDT"))+"|";
						   else
							   L_strCLTRN += " "+"|";
						   L_strCLTRN += nvlSTRVL(M_rstRSSET.getString("CLT_ISSNO")," ")+"|"
							   +nvlSTRVL(M_rstRSSET.getString("CLT_CRNNO")," ")+"|";
						   if(M_rstRSSET.getDate("CLT_CRNDT") != null)
							   L_strCLTRN += M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_CRNDT"))+"|";
						   else
							   L_strCLTRN += " "+"|";
						   L_strCLTRN +=nvlSTRVL(M_rstRSSET.getString("CLT_RFDAY")," ")+"|"
							   +nvlSTRVL(M_rstRSSET.getString("CLT_RNTRT")," ")+"|"
							   +nvlSTRVL(M_rstRSSET.getString("CLT_MGPNO")," ")+"|";
						   if(M_rstRSSET.getDate("CLT_MGPDT") != null)
							   L_strCLTRN += M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_MGPDT"))+"|";
						   else
							   L_strCLTRN += " "+"|";
						   L_strCLTRN += nvlSTRVL(M_rstRSSET.getString("CLT_RNTAM")," ")+"|"
							   +nvlSTRVL(M_rstRSSET.getString("CLT_BILRF")," ");
						   /**Store Cylindeer Detail String in Vector   */
						   vtrCLTRN.addElement(L_strCLTRN);
						   
						   //Store detail in vector by suing string which is used in report printing as sting tokenizer
						   L_strCLTRN = nvlSTRVL(M_rstRSSET.getString("CLT_SRLNO")," ")+"|";	//Serial No.
						  
						   if(M_rstRSSET.getDate("CLT_GRNDT") != null)	//Received date
							   L_strCLTRN += M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_GRNDT"))+"|";
						   else
							   L_strCLTRN += " "+"|";
						   
						   if(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("AVDAY"),"0")) > 0)	//Availabel Days
							   L_strCLTRN += M_rstRSSET.getString("AVDAY")+"|";
						   else
							   L_strCLTRN += " "+"|";
						  
						   L_strCLTRN += nvlSTRVL(M_rstRSSET.getString("CLT_BILRF")," ")+"|";	//Bill No.
						  
						   if(M_rstRSSET.getDate("CLT_MGPDT") != null)	//Leaving Date 
							   L_strCLTRN += M_fmtLCDAT.format(M_rstRSSET.getDate("CLT_MGPDT"))+"|";
						   else
							   L_strCLTRN += " "+"|";
						  
						   L_strCLTRN += nvlSTRVL(M_rstRSSET.getString("CLT_RNTAM"),"0.0")+"|";		//Rental Amount
						   
						   /**Store Material Details for report printing in vector vtrMATDS */
						   vtrMATDS.addElement(L_strCLTRN);
						   i++;
						}
					}
					setMSG("For More Details Click On Record And Press 'F9'..",'N');
					tblCLTRN.setEnabled(false);
					tblCLTRN.cmpEDITR[1].setEnabled(true);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			/**Display additional of material code in seperate window on pressing F9			 */
			if(L_KE.getKeyCode() == L_KE.VK_F9)
			{
				if(tblCLTRN.getSelectedColumn() == 1)
				{
					/**Integer For Selected Row  */
					int L_intSELROW = tblCLTRN.getSelectedRow();
					JPanel pnlCLTRN = new JPanel(null);
					pnlCLTRN.setBorder(new TitledBorder("Cylinder Details For Mat Cd. "+tblCLTRN.getValueAt(L_intSELROW,1).toString()));
					pnlCLTRN.setSize(400,180);
					pnlCLTRN.setPreferredSize(new Dimension(400,180));

					String L_strTEMP = vtrCLTRN.elementAt(L_intSELROW).toString();
					StringTokenizer stkTOKEN = new StringTokenizer(L_strTEMP,"|");

					add(new JLabel("GRIN number "),1,1,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),1,2,1,1,pnlCLTRN,'L');//CLT_GRNNO,
					add(new JLabel("Accepted Dt. "),1,3,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),1,4,1,1,pnlCLTRN,'L');//CLT_GRNDT
				
					add(new JLabel("Issue No. "),2,1,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),2,2,1,1,pnlCLTRN,'L');//CLT_ISSNO,
					add(new JLabel("Issue Date "),2,3,1,1,pnlCLTRN,'L');
			
					add(new JLabel("CRN Number "),3,1,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),3,2,1,1,pnlCLTRN,'L');//CLT_CRNNO
					add(new JLabel("CRN Date "),3,3,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),3,4,1,1,pnlCLTRN,'L');//CLT_CRNDT
				
					add(new JLabel("Free Days "),4,1,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),4,2,1,1,pnlCLTRN,'L');//CLT_RFDAY
					add(new JLabel("Rent Rate "),4,3,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),4,4,1,1,pnlCLTRN,'L');//CLT_RNTRT
				
					add(new JLabel("G. P. No. "),5,1,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),5,2,1,1,pnlCLTRN,'L');//CLT_MGPNO
				
					add(new JLabel("G. P. Date "),5,3,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),5,4,1,1,pnlCLTRN,'L');//CLT_MGPDT
				
					add(new JLabel("Rent Amt "),6,1,1,1,pnlCLTRN,'L'); 
					add(new JLabel(stkTOKEN.nextToken().toString()),6,2,1,1,pnlCLTRN,'L');//CLT_RNTAM
					add(new JLabel("Bill No. "),6,3,1,1,pnlCLTRN,'L');
					add(new JLabel(stkTOKEN.nextToken().toString()),6,4,1,1,pnlCLTRN,'L');//CLT_BILRF
				
					JOptionPane.showMessageDialog(this,pnlCLTRN);
				}
			}
			//Action on pressing enter button
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == cmbRPORD)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					((JComponent)M_objSOURC).transferFocus();
			}
		}
		catch(Exception L_EX){setMSG(L_EX,"keyPressed");}
	}
	/**Method to validate imput data before process	 */
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
	/**Method to for report it diaplay on screen or take print dependig on option 	 */
	public void exePRINT()
	{
		try
		{
			M_intLINNO = 0;
			M_intPAGNO = 1;
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			for(int i = 0; i < 150 ;i++)
			{
				if(!tblCLTRN.getValueAt(i,1).equals(""))
				{
					if(M_intLINNO > 60 || M_intPAGNO == 1)
					{
						if(M_intLINNO > 1)
							prnFMTCHR(dosREPORT,M_strEJT);
						dosREPORT.writeBytes("\n");
						prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,96));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',cmbRPTOP.getSelectedItem().toString()+" From "+txtFMDAT.getText()+" To "+txtTODAT.getText(),76));
						dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),76));
						dosREPORT.writeBytes(padSTRING('R',"Page No : "+M_intPAGNO,20));
						dosREPORT.writeBytes("\n");
						M_intLINNO += 4;
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("Material Code and Description                             Cyliner No.     Rcvd. Date  Available Dy.");
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("Supplier Name                                                Bill No.    Leaving Dt.    Rental Amt.");
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");
						dosREPORT.writeBytes("\n"); 
						M_intLINNO += 4;
						M_intPAGNO += 1;
					}
					String L_strTEMP = vtrMATDS.elementAt(i).toString();
					StringTokenizer stkMATDS = new StringTokenizer(L_strTEMP,"|");
					dosREPORT.writeBytes(padSTRING('R',tblCLTRN.getValueAt(i,1).toString(),11));
					dosREPORT.writeBytes(padSTRING('R',tblCLTRN.getValueAt(i,2).toString(),45));
					dosREPORT.writeBytes(padSTRING('L',stkMATDS.nextToken(),13));	//Cylinder Number
					dosREPORT.writeBytes(padSTRING('L',stkMATDS.nextToken(),15));	//Rcvd Date
					dosREPORT.writeBytes(padSTRING('L',stkMATDS.nextToken(),15));	//Available Days
					dosREPORT.writeBytes("\n");
					M_intLINNO += 1;
					dosREPORT.writeBytes(padSTRING('R',hstVENCD.get(tblCLTRN.getValueAt(i,6)).toString(),56));
					dosREPORT.writeBytes(padSTRING('L',stkMATDS.nextToken(),13));	//Bill Number
					dosREPORT.writeBytes(padSTRING('L',stkMATDS.nextToken(),15));	//Leaving Date
					dosREPORT.writeBytes(padSTRING('L',stkMATDS.nextToken(),15));	//Rental Amount
					dosREPORT.writeBytes("\n\n");
					M_intLINNO += 1;
				}
			}
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");
			dosREPORT.close();
			fosREPORT.close();
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
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
}