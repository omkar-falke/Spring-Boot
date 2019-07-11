//Purchase Order Query
/*
System Name   : Material Management System
Program Name  : Purchase Order Details
Program Desc. : User Enter the Purchase Order Number And Get All The Details 
Author        : A.T.Chaudhari
Date          : 20/05/2004
Version       : MMS 2.0

Modificaitons 
Modified By    : AAP
Modified Date  : 13/07/2004
Modified det.  : Status display of PO rectified, Vendor details on F9, Cancelled PO included in qry.
Version        :
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.SQLException;
import java.awt.Color;
import java.sql.ResultSet;
import java.util.Hashtable;
/**<P>Program Description :</P>
 * <P><FONT color=purple>
 * <STRONG>Program Details :</STRONG></FONT> </P>
 * <P>
 * 	<TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    
 * 		<TR><TD>System Name</TD><TD>Material Management System </TD></TR>  
 * 		<TR><TD>Program Name</TD><TD>P.O.&nbsp;Query</TD></TR>  
 *		<TR><TD>Program Desc</TD><TD>Display                                        All the information i.e. P.O. Details,GRIN Detail and Indent Detail       for given P.O. Number And Vendor Code. </TD></TR>  
 * 		<TR><TD>Basis Document</TD><TD>                       </TD></TR>  
 * 		<TR><TD>Executable path</TD><TD>f:\exec\splerp\mm_qrpor.class</TD></TR>  
 * 		<TR><TD>Source path</TD><TD>f:\source\splerp2\mm_qrpor.java</TD></TR>  
 * 		<TR><TD>Author </TD><TD>Abhijit T. Chaudhari </TD></TR>  
 * 		<TR><TD>Date</TD><TD>20/08/2004 </TD></TR>  
 * 		<TR><TD>Version </TD><TD>MMS 2.0.0</TD></TR>  
 * 		<TR><TD><STRONG>Revision : 1 </STRONG></TD><TD></TD></TR>  
 * 		<TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;By</TD><TD></TD></TR>  
 * 		<TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date</P></TD><TD><P align=left>&nbsp;</P></TD></TR>  
 * 		<TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P></TD><TD></TD></TR>
 * 	</TABLE></P>
 * <P><FONT color=purple><STRONG>Tables Used : </STRONG></FONT></P>
 * <P>
 *  <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    
 * 		<TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR>  
 * 		<TR><TD>MM_POMST</TD><TD>PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 		<TR><TD>MM_GRMST</TD><TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO&nbsp; </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  
 * 		<TR><TD>MM_INMST</TD><TD>IN_MMSBS,IN_STRTP,IN_INDNO,IN_MATCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 		<TR><TD>CO_PTMST</TD><TD>PT_PRTTP,PT_PRTCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 		<TR><TD>CO_CDTRN</TD><TD>CMT_CGMTP,CMT_CGSTP,CMT_CODCD</TD><TD><P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 	</TABLE></P>
 */
class mm_qrpor extends cl_pbase
{									/**Text Field for Purchase Order Number  */
	private JTextField txtPORNO;	/**Text Field for Purchase Order Date  */
	private JTextField txtPORDT;	/**Text Field for Complitation Date  */
	private JTextField txtCMPDT;	/**Text Field for Vendor Code  */
	private JTextField txtVENCD;	/**Text Field for Vendor Name  */
	private JTextField txtVENNM;	/**Text Field for Amd Number  */
	private JTextField txtAMDNO;	/**Text Field for Amd Date  */
	private JTextField txtAMDDT;	/**Text Field for Indent Number  */
	private JTextField txtINDNO;	/**Text Field for Authorised Quantity  */
	private JTextField txtAUTQT;	/**Text Field for Required Date  */
	private JTextField txtREQDT;	/**Text Field for Indent Status  */
	private JTextField txtINDTG;	/**Label for P.O. Detail table 	 */
	private JLabel lblPORDT;		/**Label for GRIN Detail table 	 */
	private JLabel lblGINDT;		/**String to store address and contact details of vendor */
	private String strVENDT;		/**Hashtabel for store status of p.o. p.o. number as a key and status as value */
	private Hashtable<String,String> hstSTSFL = new Hashtable<String,String>();	/**Table For Purchase Order Details  */
	private cl_JTable tblPORDT;		/**Table For GRIN Details  */
	private cl_JTable tblGRNDT;		/**final int variable for column number for p.o. detail tabel */
	private final int TBL_CHKFL = 0;
	private final int TBL_PORNO = 1;
	private final int TBL_INDNO = 2;
	private final int TBL_MATCD = 3;
	private final int TBL_MATDS = 4;
	private final int TBL_UOMCD = 5;
	private final int TBL_PORQT = 6;
	private final int TBL_FRCQT = 7;
	private final int TBL_PORRT = 8;
	private final int TBL_PORTG = 9;

/**final int variable for column number for grin detail table 	 */
	private final int TBL_GINNO = 0;
	private final int TBL_GRNNO = 1;
	private final int TBL_GRNDT = 2;
	private final int TBL_RECQT = 3;
	private final int TBL_ACPQT = 4;
	private final int TBL_ACPDT = 5;
	/**Constructor for the query porgram */
	mm_qrpor()
	{
		super(2);
		lblPORDT = new JLabel("P.O. Details");
		lblGINDT = new JLabel("Receipt Details");
		lblPORDT.setForeground(Color.blue);
		lblGINDT.setForeground(Color.blue);
		setMatrix(20,8);
		add(new JLabel("Vendor"),1,1,1,1,this,'L');
		add(txtVENCD = new TxtLimit(5),1,2,1,1,this,'L');
		add(txtVENNM = new JTextField(),1,3,1,4,this,'L');
		add(new JLabel("P.O. No."),2,1,1,1,this,'L');
		add(txtPORNO = new TxtNumLimit(8),2,2,1,1,this,'L');
		add(new JLabel("P.O. Date"),2,3,1,1,this,'L');
		add(txtPORDT = new JTextField(),2,4,1,1,this,'L');
		add(new JLabel("Amd No/Date"),2,5,1,1,this,'L');
		add(txtAMDNO = new JTextField(),2,6,1,0.25,this,'L');
		add(txtAMDDT = new JTextField(),2,6,1,0.75,this,'R');
		add(new JLabel("Cmp Date"),2,7,1,1,this,'L');
		add(txtCMPDT = new JTextField(),2,8,1,1,this,'L');
		add(lblPORDT,3,1,1,1,this,'L');
		tblPORDT = crtTBLPNL1(this,new String[]{"","P.O. No","Indent No","Mat. Code","Description","UOM","P.O.Qty.","F.C.Qty","P.O. Rate","P.O.Status"},250,4,1,7,7.93,new int[]{20,62,62,79,310,40,60,60,60,80},new int[]{0});
		add(lblGINDT,11,1,1,1,this,'L');
		tblGRNDT = crtTBLPNL1(this,new String[]{"Gate In Number","GRIN Number","GRIN Date","Received Qty.","Acp. Quantity","Accepted Date"},40,12,1,5,7.93,new int[]{124,124,124,124,123,123},new int[]{});
		add(new JLabel("Indent Number"),17,1,1,1.54,this,'L');
		add(new JLabel("Authorised Qty."),17,3,1,1.54,this,'R');
		add(new JLabel("Required Date"),17,4,1,1.54,this,'L');
		add(new JLabel("Indent Status"),17,6,1,1.54,this,'R');
		add(txtINDNO = new JTextField(),18,1,1,1.54,this,'L');
		add(txtAUTQT = new JTextField(),18,3,1,1.54,this,'R');
		add(txtREQDT = new JTextField(),18,4,1,1.54,this,'L');
		add(txtINDTG = new JTextField(),18,6,1,1.54,this,'R');
		tblPORDT.addMouseListener(this);
		getSTSFL();	//Call method which store status deasription w.r.t p.o. number	
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 4)
			{
				setMSG("",'N');
				setENBL(false);
				txtVENCD.requestFocus();
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			{
				setMSG("Select An Option..",'N');
			}
		}
		if(M_objSOURC == txtVENCD)
		{
			txtVENCD.setText(txtVENCD.getText().toUpperCase());
		}
	}
	//Method to display message when focus on given component 
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtVENCD)
			setMSG("Enter Vendor Code Or Press 'F1' For Help Or Blank ..",'N');
		if(M_objSOURC == txtVENNM)
			if(txtVENNM.getText().trim().length() > 0)
				setMSG("Press 'F9' For Vendor Details ..",'N');
		if(M_objSOURC == txtPORNO)
			setMSG("Enter P.O. Number Or Press 'F1' For Help ..",'N');
	}
	/**Overwrite Method setENBL	 */
	public void setENBL(boolean L_ACTION)
	{
		super.setENBL(L_ACTION);
		txtPORNO.setEnabled(true);
		tblPORDT.cmpEDITR[0].setEnabled(true);
		txtVENCD.setEnabled(true);
		txtVENNM.setEnabled(true);
	}
	/**Method For Key Pressed
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPORNO)
			{
				M_strHLPFLD = "txtPORNO";
				M_strSQLQRY = "SELECT DISTINCT PO_PORNO,PO_PORDT,CMT_CODDS FROM MM_POMST,CO_CDTRN "
					+"WHERE CMT_CODCD = PO_STSFL AND CMT_CGMTP = 'STS' AND CMT_CGSTP = 'MMXXPOR' AND "
					+"PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"'  ";
				if(txtPORNO.getText().trim().length() > 0)
					M_strSQLQRY += "AND PO_PORNO LIKE '"+txtPORNO.getText().trim()+"%' ";
				if(txtVENCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND PO_VENCD = '"+txtVENCD.getText().trim()+"'";
				M_strSQLQRY += "ORDER BY PO_PORDT DESC,PO_PORNO DESC";
				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"P. O. Number","P. O. Date","Status"},3,"CT");
			}
			if(M_objSOURC == txtVENCD)
			{
				M_strHLPFLD = "txtVENCD";
				M_strSQLQRY = "SELECT DISTINCT PO_VENCD,PT_PRTNM FROM MM_POMST,CO_PTMST "
					+"WHERE PT_PRTTP = PO_VENTP AND PT_PRTCD = PO_VENCD AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_VENTP = 'S' ";
				if(txtVENCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND PO_VENCD LIKE '"+txtVENCD.getText().trim().toUpperCase()+"%' ";
				M_strSQLQRY += "ORDER BY PO_VENCD ";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Vendor Code","Vendor Name"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtPORNO)
			{
				if(vldDATA())
					getDATA();	//Call method to get data for specific input and display in screen
			}
			else
				((JComponent)M_objSOURC).transferFocus();
		}
		//display additional information of vendor when press F9 for vendor name
		if(M_objSOURC == txtVENNM && L_KE.getKeyCode()==L_KE.VK_F9 && strVENDT!=null)
		{
			JOptionPane.showMessageDialog(txtVENNM,strVENDT); 
		}
	}
	/** Method When Mouse Realesed which display GRIN detail and indent details
	 */
	public void mouseReleased(MouseEvent L_ME)
	{
		super.mouseReleased(L_ME);
		if((M_objSOURC == tblPORDT)&&(tblPORDT.getSelectedColumn() == TBL_CHKFL))
		{
			int L_intSELROW = tblPORDT.getSelectedRow();
			for(int i=0;i<tblPORDT.getRowCount();i++)
				if(i != L_intSELROW)
					tblPORDT.setValueAt(new Boolean(false),i,0);
		}
		java.sql.Date jdtTEMP;
		int i = 0;
		try
		{
			/**Query For GRIN Data	 */
			M_strSQLQRY = "SELECT GR_GINNO,GR_GRNNO,GR_GRNDT,GR_RECQT,GR_ACPDT,GR_ACPQT "
				+"FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"GR_PORNO = '"+tblPORDT.getValueAt(tblPORDT.getSelectedRow(),TBL_PORNO)
				+"' AND GR_MATCD = '"+tblPORDT.getValueAt(tblPORDT.getSelectedRow(),TBL_MATCD)+"' AND "
				+" isnull(GR_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			tblGRNDT.clrTABLE();
			txtINDNO.setText("");
			txtAUTQT.setText("");
			txtREQDT.setText("");
			txtINDTG.setText("");
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),""),i,TBL_GINNO);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),i,TBL_GRNNO);
					if(M_rstRSSET.getDate("GR_GRNDT") != null)
						tblGRNDT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_GRNDT")),i,TBL_GRNDT);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0"),i,TBL_RECQT);
					if(M_rstRSSET.getDate("GR_ACPDT") != null)
						tblGRNDT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_ACPDT")),i,TBL_ACPDT);
					tblGRNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),""),i,TBL_ACPQT);
					i++;
				}
			}
			/**Query For Indent Details
			 */
			M_strSQLQRY = "SELECT IN_INDNO,IN_AUTQT,IN_REQDT,IN_STSFL,CMT_CODDS,SUBSTRING(IN_MMSBS,1,2) WRHO "
				+"FROM MM_INMST,CO_CDTRN WHERE "
				+"CMT_CGMTP = 'STS' AND CMT_CGSTP = 'MMXXIND' AND CMT_CODCD = IN_STSFL AND "
				+"IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND IN_INDNO = '"
				+tblPORDT.getValueAt(tblPORDT.getSelectedRow(),TBL_INDNO)+"' AND "
				+"IN_MATCD = '"+tblPORDT.getValueAt(tblPORDT.getSelectedRow(),TBL_MATCD)+"' "
				+"AND isnull(IN_STSFL,'') <> 'X' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				txtINDNO.setText(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""));
				txtAUTQT.setText(nvlSTRVL(M_rstRSSET.getString("IN_AUTQT"),""));
				jdtTEMP = M_rstRSSET.getDate("IN_REQDT");
				if(jdtTEMP != null)
					txtREQDT.setText(M_fmtLCDAT.format(jdtTEMP));
				txtINDTG.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
			}
			M_rstRSSET.close();
		}
		catch(SQLException L_SQL)
		{
			setMSG(L_SQL,"mouseReleased");
		}
		setMSG("",'N');
	}
	/**Method on pressing enter or ok of help window
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtVENCD")
		{
			txtVENCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtVENNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtPORNO.requestFocus();
		}
		if(M_strHLPFLD == "txtPORNO")
		{
			txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtPORNO.requestFocus();
		}
	}
	/**Method to Check Data Is Valid 
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtPORNO.getText().trim().length() == 8)
			{
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY("SELECT PO_PORNO FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND PO_PORNO = '"+txtPORNO.getText().trim()+"' ");
				if(L_rstRSSET.next())
				{
					setMSG("",'N');
					return true;
				}
				else
				{
					txtPORNO.requestFocus();
					setMSG("Enter Valid P.O. Number Or Press F1 For P.O. Number..",'E');
					return false;
				}
			}
		}
		catch(SQLException L_SQL)
		{
				setMSG(L_SQL,"vldDATA");
		}
		txtPORNO.requestFocus();
		setMSG("Enter Eight Digit P.O. Number Or Press F1 For Help..",'E');
		return false;
	}
	/**Method To get data For Perticular P. O. Number
	 */
	public void getDATA()
	{
		java.sql.Date jdtTEMP;
		boolean L_blnFIRST = true;
		String L_strTEMP=txtPORNO.getText();
		clrCOMP();
		txtPORNO.setText(L_strTEMP);
		L_strTEMP=null;
		if(vldDATA())
		{
			try
			{
				M_strSQLQRY = "SELECT PO_PORNO,PO_PORDT,PO_CMPDT,PO_PORVL,PO_VENCD,PO_AMDNO,PO_AMDDT,"
					+"PO_INDNO,PO_MATCD,PO_MATDS,PO_UOMCD,PO_MATDS,PO_PORQT,isnull(PO_FRCQT,0) PO_FRCQT,PO_PORRT,PO_STSFL,CT_MATDS "
					+"FROM MM_POMST,CO_CTMST WHERE PO_MATCD = CT_MATCD AND "
					+"PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_PORNO = '"+txtPORNO.getText().trim()+"' AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtVENCD.getText().trim().length() > 0)
					M_strSQLQRY += "AND PO_VENCD = '"+txtVENCD.getText().trim()+"' ";
				M_strSQLQRY += " ORDER BY PO_INDNO,PO_MATCD";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					int i = 0;
					while(M_rstRSSET.next())
					{
						if(L_blnFIRST)
						{
							L_blnFIRST = false;
							//P.O. Date
							if(M_rstRSSET.getDate("PO_PORDT") != null)
								txtPORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("PO_PORDT")));
							else
								txtPORDT.setText("");
							//Cmp. Date
							if(M_rstRSSET.getDate("PO_CMPDT") != null)
								txtCMPDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("PO_CMPDT")));
							else
								txtCMPDT.setText("");
							txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),""));
							//Call method which return party name and store party detail in siring prtdt 
							//which is popup on pressing F9 on vendor name
							txtVENNM.setText(getPRTNM(txtVENCD.getText().trim()));
							txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("PO_AMDNO"),""));
							if(M_rstRSSET.getDate("PO_AMDDT") != null)
								txtAMDDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("PO_AMDDT")));
							else
								txtAMDDT.setText("");
						}
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),""),i,TBL_PORNO);
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),""),i,TBL_INDNO);
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),""),i,TBL_MATCD);
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),i,TBL_MATDS);
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_UOMCD"),""),i,TBL_UOMCD);
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),""),i,TBL_PORQT);
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_FRCQT"),""),i,TBL_FRCQT);
						tblPORDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),""),i,TBL_PORRT);
						tblPORDT.setValueAt(hstSTSFL.get(nvlSTRVL(M_rstRSSET.getString("PO_STSFL"),"")).toString(),i,TBL_PORTG);
						i++;
					}
				}
				else
				{
					setMSG("Record Not Found..",'E');
				}
			}
			catch(SQLException L_SQL)
			{
				setMSG(L_SQL,"getDATA");
			}
		}
	}
	/**Method to get Party details 
	 */
	private String getPRTNM(String P_strVENCD)
	{
		try
		{
			ResultSet L_rstRSSET ;
			M_strSQLQRY ="SELECT * FROM CO_PTMST WHERE PT_PRTTP ='S' AND PT_PRTCD ='"+txtVENCD.getText().trim() +"'";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
				if(L_rstRSSET.next())
				{
					strVENDT=nvlSTRVL(L_rstRSSET.getString("PT_PRTNM")," ")
						+" \n"+nvlSTRVL(L_rstRSSET.getString("PT_ADR01")," ")
						+" "+nvlSTRVL(L_rstRSSET.getString("PT_ADR02")," ")
						+" \n"+nvlSTRVL(L_rstRSSET.getString("PT_ADR03")," ")
						+" "+nvlSTRVL(L_rstRSSET.getString("PT_ADR04")," ")
						+" \n"+nvlSTRVL(L_rstRSSET.getString("PT_CTYNM")," ")
						+" "+nvlSTRVL(L_rstRSSET.getString("PT_PINCD")," ")
						+"\nContact Numbers : "
						+nvlSTRVL(L_rstRSSET.getString("PT_TEL01")," ")+" , " 
						+nvlSTRVL(L_rstRSSET.getString("PT_TEL02")," ");
					return(nvlSTRVL(L_rstRSSET.getString("PT_PRTNM")," "));
				}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"");
		}
		return "";
	}
	/**Method to get the status description and status code store in respect to p.o. number 
	 * and p.o. status description 
	*/
	private void getSTSFL()
	{
		try
		{
			ResultSet L_rstRSSET ;
			M_strSQLQRY ="SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP ='STS' AND CMT_CGSTP ='MMXXPOR' ";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
				while(L_rstRSSET.next())
				{
					hstSTSFL.put(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
				}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"");
		}

	}
}