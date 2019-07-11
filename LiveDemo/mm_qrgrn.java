/*
System Name   : Material Management System
Program Name  : GRIN Query
Program Desc. : 
Author        : A. T. Chaudhari
Date          : 06/07/2004
Version       : MMS 2.0
*/

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.border.*;
/**<P>Program Description :</P><P><FONT color=purple>
 * <STRONG>Program Details :</STRONG></FONT>
 * <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 767px; HEIGHT: 89px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    
 *		<TR>	<TD>System Name</TD>    <TD>Material Management System </TD></TR>  
 * 		<TR>    <TD>Program Name</TD>    <TD>    GRIN Query</TD></TR>  
 * 		<TR>    <TD>Program Desc</TD>    <TD>                                        GRIN Query display result on any combination of&nbsp;Material type, Material       code,P.O. number,Chalan number,GRIN number and Vendor code&nbsp; </TD></TR>  
 * 		<TR>    <TD>Basis Document</TD>    <TD>                       </TD></TR>  
 * 		<TR>    <TD>Executable path</TD>    <TD>f:\exec\splerp\mm_qrgrn.class</TD></TR>  
 * 		<TR>    <TD>Source path</TD>    <TD>f:\source\splerp2\mm_qrgrn.java</TD></TR>  
 * 		<TR>    <TD>Author </TD>    <TD>Abhijit T. Chaudhari </TD></TR>  
 *		<TR>    <TD>Date</TD>    <TD>13/08/2004 </TD></TR>  
 * 		<TR>    <TD>Version </TD>    <TD>MMS 2.0.0</TD></TR>  
 * 		<TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>      </TD></TR>  
 * 		<TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD></TD></TR>  
 * 		<TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  
 * 		<TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD></TD></TR>
 * 	</TABLE>
 *  <FONT color=purple><STRONG>Tables Used : </STRONG></FONT>
 *  <TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    
 * 		<TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  
 * 		<TR>    <TD>MM_GRMST</TD>    <TD>GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO&nbsp; </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  
 * 		<TR>    <TD>MM_STMST</TD>    <TD>ST_MMSBS,ST_STRTP,ST_MATCD</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 		<TR>	<TD>MM_POMST</TD>	 <TD>PO_STRTP,PO_PORTP,PO_PORNO,PO_INDNO,PO_MATCD</TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>
 * 	</TABLE></P>
 * */
class mm_qrgrn extends cl_pbase
{										/**Panel to disply component of search field		 */
	private JPanel pnlSERCH;			/**search JComboBox for material type		 */
	private JComboBox cmbMATTP;			/**search TextField for GRIN Number		 */
	private TxtNumLimit txtGRNNO1;		/**search Text Field For Vendor Code		 */
	private TxtLimit txtVENCD1;			/**search Text Field for vendor name		 */
	private TxtLimit txtVENNM1;			/**search text field for p.o. number		 */
	private TxtNumLimit txtPORNO1;		/**search text field for material code		 */
	private TxtNumLimit txtMATCD1;		/**search text field for chalan number		 */
	private TxtNumLimit txtCHLNO1;		
										/**Textfield for grin number		*/ 	
	private JTextField txtGRNNO;		/**textfield for grin date 	 */
	private JTextField txtGRNDT;		/**textfield for grin type 	 */
	private JTextField txtGRNTP;		/**textfield for gatein number  	 */
	private JTextField txtGINNO;		/**textfield for p.o. number 	 */
	private JTextField txtPORNO;		/**textfield for p.o. date 	 */
	private JTextField txtPORDT;		/**textfield for chalan number 	 */
	private JTextField txtCHLNO;		/**textfield for chalan date 	 */
	private JTextField txtCHLDT;		/**textfield for vendor code 	 */
	private JTextField txtVENCD;		/**textfield for vendor name 	 */
	private JTextField txtVENNM;		/**textfield for material description 	 */
	private JTextField txtMATDS;		
	//private JTextField txtLUSBY;		
	//private JTextField txtLUPDT;		/**Table for GRIN details 	 */
	private cl_JTable tblGRNDT;			/**button for next record 	 */
	private JButton btnNEXT;			/**button for previous recored 	 */
	private JButton btnPREV;			/**final int variable for ckeck column 	 */
	final int TBL_CHKFL = 0;			/**final int variable for material code column*/
	final int TBL_MATCD = 1;			/**final int variable for material description column*/
	final int TBL_MATDS = 2;			/**final int variable for unit of measurement column*/
	final int TBL_UOMCD = 3;			/**final int variable for chalan quantity column*/
	final int TBL_CHLQT = 4;			/**final int variable for received quantity column*/
    final int TBL_RECQT = 5;			/**final int variable for accepted quantity column*/
	final int TBL_ACPQT = 6;			/**final int variable for unit rate column*/
	final int TBL_UNTRT = 7;			/**String array for grin details					 */
	private String [][] staGRNDT;		/**hash table for grin number					 */
	private Hashtable<String,Object> hstGRNDT;			/**initialise intROWID -1 for row identification					 */
	private int intROWID = -1;			/**initialise intRECCT 1 for record count		 */
	private int intRECCT = 1;			/**String Array For GRIN Type			 */	
	//Globle Variable to Display GRIN Tyepe Stored in String Array
	private String staGRNTP[] = {"","P.O.","Cash","Replacement","Row Material","Exbonded","Adv. Lic"};
	/*
	All the data of search result is stores in two field 1. string arra and 2. hsahtabel,
	1. stirng array staGRNDT stores value which is singly displayed
	like GR_GRNNO,GR_GRNDT,GR_GRNTP,GR_GINNO,GR_PORNO,PO_PORDT,GR_CHLNO,GR_CHLDT,GR_VENCD,
	GR_VENNM,GR_LUSBY,GR_LUPDT and another detail first store in one vector and this then this 
	vector is added to hash tabel as value with key as grin number.
	*/
	public mm_qrgrn()
	{
		super(2);
		try
		{
			pnlSERCH = new JPanel(null);
			pnlSERCH.setBorder(new TitledBorder("Search Fields ( Search Criteria )"));
			hstGRNDT = new Hashtable<String,Object>(20,0.8f);
			cmbMATTP = new JComboBox();
			//add material type to the combo box
			cmbMATTP.addItem("Select");
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'MMXXMTP' AND isnull(CMT_STSFL,' ') <>'X' AND "
				+"CMT_CODCD IN ('1','2','3','4')ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					cmbMATTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}
			setMatrix(20,8);
			add(new JLabel("Material Type"),1,1,1,.95,pnlSERCH,'R');
			add(cmbMATTP,1,2,1,1.2,pnlSERCH,'L');
			add(new JLabel("Mat. Code"),1,3,1,.75,pnlSERCH,'R');
			add(txtMATCD1 = new TxtNumLimit(10.0),1,4,1,1,pnlSERCH,'L');
			add(new JLabel("P.O. Number"),2,1,1,.95,pnlSERCH,'R');
			add(txtPORNO1 = new TxtNumLimit(10.0),2,2,1,1.2,pnlSERCH,'L');
			add(new JLabel("Chl. No."),2,3,1,.75,pnlSERCH,'R');
			add(txtCHLNO1 = new TxtNumLimit(10.0),2,4,1,1,pnlSERCH,'L');
			add(new JLabel("GRIN No"),2,5,1,1,pnlSERCH,'L');
			add(txtGRNNO1 = new TxtNumLimit(10.0),2,6,1,1,pnlSERCH,'L');
			add(new JLabel("Vendor Code"),3,1,1,.95,pnlSERCH,'R');
			add(txtVENCD1 = new TxtLimit(5),3,2,1,1.078,pnlSERCH,'L');
			add(txtVENNM1 = new TxtLimit(20),3,3,1,4,pnlSERCH,'L');
			add(pnlSERCH,1,1,5,8,this,'L');
			add(new JLabel("GRIN No"),7,1,1,1,this,'L');
			add(txtGRNNO = new JTextField(),7,2,1,1,this,'L');
			add(new JLabel("GRIN Date"),7,3,1,1,this,'L');
			add(txtGRNDT = new JTextField(),7,4,1,1,this,'L');
			add(new JLabel("GRIN Type"),7,5,1,1,this,'L');
			add(txtGRNTP = new TxtLimit(10),7,6,1,1,this,'L');
			add(new JLabel("Gate In No"),7,7,1,1,this,'L');
			add(txtGINNO = new JTextField(),7,8,1,1,this,'L');
			add(new JLabel("P.O. Number"),8,1,1,1,this,'L');
			add(txtPORNO = new JTextField(),8,2,1,1,this,'L');
			add(new JLabel("P.O. Date"),8,3,1,1,this,'L');
			add(txtPORDT = new JTextField(),8,4,1,1,this,'L');
			add(new JLabel("Challan No"),8,5,1,1,this,'L');
			add(txtCHLNO = new JTextField(),8,6,1,1,this,'L');
			add(new JLabel("Challan Dt."),8,7,1,1,this,'L');
			add(txtCHLDT = new JTextField(),8,8,1,1,this,'L');
			add(new JLabel("Vendor Code"),9,1,1,1,this,'L');
			add(txtVENCD = new JTextField(),9,2,1,1,this,'L');
			add(txtVENNM = new JTextField(),9,3,1,4,this,'L');
			//add(new JLabel("Last Used By"),10,1,1,1,this,'L');
			//add(txtLUSBY = new JTextField(),10,2,1,1,this,'L');
			//add(new JLabel("Last Updated Date"),10,3,1,1,this,'L');
			//add(txtLUPDT = new JTextField(),10,4,1,1,this,'L');
			//tblGRNDT = crtTBLPNL1(this,new String[]{"","Material Code","Description","UOM","Challan Qty.","Qty.Received","Acp. Qty.","Unit Rate","G.P. No."},200,11,1,6,8.1,new int[]{20,80,300,38,80,80,80,80,80},new int[]{0});
			tblGRNDT = crtTBLPNL1(this,new String[]{"","Material Code","Description","UOM","Challan Qty.","Qty.Received","Acp. Qty.","Unit Rate"},200,11,1,6,8.1,new int[]{20,80,300,38,80,80,80,80},new int[]{0});
			add(btnPREV = new JButton("<"),18,4,1,.65,this,'R');
			add(btnNEXT = new JButton(">"),18,5,1,.65,this,'L');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		//action on option selection combobox
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 4)
			{
				setMSG("",'N');
				setENBL(false);
				cmbMATTP.requestFocus();
				setMSG("Select Matetial Type..",'N');
			}
			else 
				setMSG("Select An Option..",'N');
		}
		//Action on material type combobox
		if(M_objSOURC == cmbMATTP)
		{
			int L_intINDEX = cmbMATTP.getSelectedIndex();
			clrCOMP();
			cmbMATTP.setSelectedIndex(L_intINDEX);
		}
		//Action on button PREVIOUS
		if(M_objSOURC == btnPREV)
		{
			String L_strTEMP=staGRNDT[intROWID][0];
			for(int i=intROWID;i>=0;i--)
			{
				if(!L_strTEMP.equals(staGRNDT[i][0]))
				{
					intRECCT--;
					dspDATA(i);	//call method to display data
					break;
				}
			}
		}
		//Action On Button NEXT
		if(M_objSOURC == btnNEXT)
		{
			String L_strTEMP=staGRNDT[intROWID][0];
			for(int i=intROWID;i<staGRNDT.length;i++)
			{
				if(!L_strTEMP.equals(staGRNDT[i][0]))
				{
					intRECCT++;
					dspDATA(i);
					break;
				}
			}
		}
	}
	//Method on pressing button display
	public void exePRINT()
	{
		exeSQUERY();		//call method to display data
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		//display orignal user code and loging date when pressing * button
		if(L_KE.getKeyChar() == '*')
		{
			cl_dat.M_txtUSER_pbst.setText(cl_dat.M_strUSRCD_pbst);
			cl_dat.M_txtDATE_pbst.setText(cl_dat.M_strLOGDT_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtVENCD1)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC == txtGRNNO1)
			{
				M_strHLPFLD = "txtGRNNO1";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST "
					+"WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				if(txtGRNNO1.getText().trim().length() > 0)
					M_strSQLQRY += "GR_GRNNO LIKE '"+txtGRNNO1.getText()+"%' AND ";
				M_strSQLQRY +="isnull(GR_STSFL,' ') <> 'X'ORDER BY GR_GRNNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRIN No.","GRIN Date"},2,"CT");
			}
			if(M_objSOURC == txtVENCD1)
			{
				M_strHLPFLD = "txtVENCD1";
				M_strSQLQRY = "SELECT DISTINCT GR_VENCD,GR_VENNM FROM MM_GRMST WHERE "
					+" GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				if(txtVENCD1.getText().trim().length() > 0)
					M_strSQLQRY += "GR_VENCD LIKE '"+txtVENCD1.getText().trim().toUpperCase()+"%' AND ";
				M_strSQLQRY += "isnull(GR_STSFL,'') <> 'X' ORDER BY GR_VENCD";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Vendor Code","Vendor Name"},2,"CT");
			}
			if(M_objSOURC == txtPORNO1)
			{
				M_strHLPFLD = "txtPORNO1";
				M_strSQLQRY = "SELECT DISTINCT GR_PORNO FROM MM_GRMST WHERE "
					+" GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND ";
				if(txtPORNO1.getText().trim().length() > 0)
					M_strSQLQRY += "GR_PORNO LIKE '"+txtPORNO1.getText().trim()+"%' AND ";
				M_strSQLQRY += "isnull(GR_STSFL,'') <> 'X'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"P.O. No."},1,"CT");
			}
			if(M_objSOURC == txtMATCD1)
			{
				M_strHLPFLD = "txtMATCD1";
				M_strSQLQRY = "SELECT DISTINCT GR_MATCD,CT_MATDS FROM MM_GRMST,CO_CTMST WHERE "
					+"CT_MATCD = GR_MATCD AND isnull(CT_STSFL,' ') <> 'X' AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,' ') <> 'X' ";
				if(txtMATCD1.getText().trim().length() > 0)
					M_strSQLQRY += "AND GR_MATCD LIKE '"+txtMATCD1.getText().trim()+"%' ";
				M_strSQLQRY += "ORDER BY CT_MATDS ";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Material Code","Description"},2,"CT");
			}
			if(M_objSOURC == txtCHLNO1)
			{
				M_strHLPFLD = "txtCHLNO1";
				M_strSQLQRY = "SELECT GR_CHLNO,GR_CHLDT FROM MM_GRMST WHERE ";
				if(txtCHLNO.getText().trim().length() > 0)
					M_strSQLQRY += "GR_CHLNO LIKE '"+txtCHLNO.getText().trim()+"%' AND ";
				M_strSQLQRY += "GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <> 'X'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Challan No.","Challan Date"},2,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtMATCD1")
		{
			txtMATCD1.setText(cl_dat.M_strHLPSTR_pbst);
			txtPORNO1.requestFocus();
		}
		if(M_strHLPFLD == "txtPORNO1")
		{
			txtPORNO1.setText(cl_dat.M_strHLPSTR_pbst);
			txtCHLNO1.requestFocus();
		}
		if(M_strHLPFLD == "txtCHLNO1")
		{
			txtCHLNO1.setText(cl_dat.M_strHLPSTR_pbst);
			txtGRNNO1.requestFocus();
		}
		if(M_strHLPFLD == "txtGRNNO1")
		{
			txtGRNNO1.setText(cl_dat.M_strHLPSTR_pbst);
			//txtGRNDT1.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtVENCD1.requestFocus();
		}
		if(M_strHLPFLD == "txtVENCD1")
		{
			txtVENCD1.setText(cl_dat.M_strHLPSTR_pbst);
			txtVENNM1.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			cl_dat.M_btnSAVE_pbst.requestFocus();			
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == cmbMATTP)
				setMSG("Select Material Type..",'N');
			if(M_objSOURC == txtGRNNO1)
				setMSG("Enter GRIN No. Or Press 'F1' For Help..",'N');
			if(M_objSOURC == txtVENCD1)
				setMSG("Enter Vendor Code Or Press 'F1' For Help..",'N');
			if(M_objSOURC == txtPORNO1)
				setMSG("Enter P. O. No. Or Press 'F1' For Help..",'N');
			if(M_objSOURC == txtMATCD1)
				setMSG("Enter Material Code Or Press 'F1' For Help..",'N');
			if(M_objSOURC == txtCHLNO1)
				setMSG("Enter Chalan No. Or Press 'F1' For Help..",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"focusGained");
		}
	}
	public void setENBL(boolean blnACTION)
	{
		super.setENBL(blnACTION);
		cmbMATTP.setEnabled(true);
		txtGRNNO1.setEnabled(true);
		txtVENCD1.setEnabled(true);
		txtPORNO1.setEnabled(true);
		txtMATCD1.setEnabled(true);
		txtCHLNO1.setEnabled(true);
		btnNEXT.setEnabled(true);
		btnPREV.setEnabled(true);
		tblGRNDT.cmpEDITR[0].setEnabled(true);
	}
	void clrCOMP()
	{
		super.clrCOMP();
		staGRNDT = null;
		hstGRNDT = null;
		intRECCT = 1;
	}
	public void exeSQUERY()
	{
		try
		{
			tblGRNDT.clrTABLE();
			txtGRNNO.setText("");txtGRNDT.setText("");txtGRNTP.setText("");txtGINNO.setText("");
			txtPORNO.setText("");txtPORDT.setText("");txtCHLNO.setText("");txtCHLDT.setText("");
			txtVENCD.setText("");txtVENNM.setText("");
			
			setCursor(cl_dat. M_curWTSTS_pbst);
			//sequence of column name is important it used to display data on the screen component
			M_strSQLQRY = "SELECT GR_GRNNO,GR_GRNDT,GR_GRNTP,GR_GINNO,GR_PORNO,PO_PORDT,"
				+"GR_CHLNO,GR_CHLDT,GR_VENCD,GR_VENNM,GR_LUSBY,GR_LUPDT,GR_MATCD,GR_CHLQT,"
				+"GR_RECQT,GR_ACPQT,GR_GRNRT,ST_UOMCD,ST_MATDS FROM MM_GRMST,MM_STMST,"
				+"MM_POMST WHERE GR_CMPCD = PO_CMPCD and GR_STRTP = PO_STRTP AND GR_PORNO = PO_PORNO AND "
				+"GR_MATCD = PO_MATCD AND GR_VENCD = PO_VENCD AND PO_CMPCD = ST_CMPCD and PO_STRTP = ST_STRTP AND "
				+"PO_MATCD = ST_MATCD AND GR_STRTP = ST_STRTP AND GR_MATCD = ST_MATCD AND  "
				+"PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'') <> 'X' AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <> 'X' AND "
				+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') <> 'X' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
				+"AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
			if(cmbMATTP.getSelectedIndex() > 0)
				M_strSQLQRY += "AND ST_MATTP = '"+cmbMATTP.getSelectedItem().toString().substring(0,1)+"' ";
			if(txtGRNNO1.getText().trim().length() > 0)
				M_strSQLQRY += "AND GR_GRNNO = '"+txtGRNNO1.getText().trim()+"' ";
			if(txtVENCD1.getText().trim().length() > 0)
				M_strSQLQRY += "AND GR_VENCD = '"+txtVENCD1.getText().trim()+"' ";
			if(txtPORNO1.getText().trim().length() > 0)
				M_strSQLQRY += "AND GR_PORNO = '"+txtPORNO1.getText().trim()+"' ";
			if(txtMATCD1.getText().trim().length() > 0)
				M_strSQLQRY += "AND GR_MATCD = '"+txtMATCD1.getText().trim()+"' ";
			if(txtCHLNO1.getText().trim().length() > 0)
				M_strSQLQRY += "AND GR_CHLNO = '"+txtCHLNO1.getText().trim()+"' ";
			M_strSQLQRY += "ORDER BY GR_GRNNO,GR_MATCD";

			System.out.println(M_strSQLQRY);									
			//java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			System.out.println("right");
			// java.lang.Thread.sleep(15);
			System.out.println(M_strSQLQRY);
			M_rstRSSET = L_stat.executeQuery(M_strSQLQRY);
			System.out.println("right");
			if(M_rstRSSET != null)
			{
				ResultSetMetaData L_rmtTEMP= M_rstRSSET.getMetaData();
				int L_intROWCT=0;
				int L_intROW=0;
				while(M_rstRSSET.next())
					L_intROWCT++;	//Get to total number of column in the result set
				if(L_intROWCT<0){	//If result set is empty, return
					setMSG("No data Found ..",'E');
					setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				//System.out.println(L_intROWCT);
				//initialise string array to store grin detail 
				staGRNDT = new String[L_intROWCT][12];	//Array For result set data
				//initialise hashtabel used to sotres grin no and grin subdetail which displayed in table 
				//in the form of string differenciate by "|" using string tokenizer
				hstGRNDT = new Hashtable<String,Object>(10,0.8f);		
				Vector<String> L_vtrMATDT = new Vector<String>(10,2);	//Vector For Material Data
				M_rstRSSET.beforeFirst();
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				//GR_GRNNO,GR_GRNDT,GR_GRNTP,GR_GINNO,GR_PORNO,PO_PORDT,GR_CHLNO,GR_CHLDT,GR_VENCD,GR_VENNM,GR_LUSBY,GR_LUPDT
				while(M_rstRSSET.next())
				{
					//store the value in two dimensional string array L_intROW in 0 initally
					for(int i = 0;i < 12;i++)
					{
						//If data is date type
						if(L_rmtTEMP.getColumnType(i+1) == 93 || L_rmtTEMP.getColumnType(i+1) == 91)
						{
							if(M_rstRSSET.getDate(i+1)!=null)
								staGRNDT[L_intROW][i] = M_fmtLCDAT.format(M_rstRSSET.getDate(i+1));
						}
						else
							staGRNDT[L_intROW][i] = M_rstRSSET.getString(i+1);
					}
					if(L_intROW > 0)
					{
						//if GRIN number is not equal to previous one
						if(!staGRNDT[L_intROW-1][0].equals(M_rstRSSET.getString("GR_GRNNO")))
						{
							//store grin number as key and vector containg table detail in hashtabel
							//hstGRNDT
							hstGRNDT.put(staGRNDT[L_intROW-1][0],L_vtrMATDT);
							//initialise vector as new
							L_vtrMATDT = new Vector<String>(10,2);
						}
					}
					//GR_MATCD,GR_CHLQT,GR_RECQT,GR_ACPQT,GR_GRNRT,ST_UOMCD,ST_MATDS "
					//Store in the vector L_vtrMATDT
					L_vtrMATDT .addElement(M_rstRSSET.getString("GR_MATCD")+"|"+M_rstRSSET.getString("ST_MATDS")+"|"+M_rstRSSET.getString("ST_UOMCD")+"|"+M_rstRSSET.getString("GR_CHLQT")+"|"+M_rstRSSET.getString("GR_RECQT")+"|"+M_rstRSSET.getString("GR_ACPQT")+"|"+M_rstRSSET.getString("GR_GRNRT"));
					L_intROW ++;
				}
				hstGRNDT.put(staGRNDT[L_intROW-1][0],L_vtrMATDT);//For last material code
				dspDATA(0);
			}
			setCursor(cl_dat. M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"exeSQUERY");
		}
	}
	private void dspDATA(int P_intROWID)
	{
		try
		{
			if(staGRNDT==null)
			{	
				setMSG("No data found ..",'E');
				return;
			}
			if(!hstGRNDT.containsKey(staGRNDT[P_intROWID][0]))
			{	
				setMSG("No data found ..",'E');
				return;
			}
			intROWID = P_intROWID; //copy parameter in global veriable
			//set button states and focus component
			if(!(intRECCT < hstGRNDT.size()))
				btnPREV.requestFocus();
			else if(!(intRECCT >1))
				btnNEXT.grabFocus();
			updateUI();
			//Display data from string arrya staQRDAT
			txtGRNNO.setText(nvlSTRVL(staGRNDT[P_intROWID][0],""));
			txtGRNDT.setText(nvlSTRVL(staGRNDT[P_intROWID][1],"")); 
			txtGRNTP.setText(nvlSTRVL(staGRNDT[P_intROWID][2],""));
			txtGRNTP.setText(staGRNTP[Integer.parseInt(staGRNDT[P_intROWID][2])]);
			txtGINNO.setText(nvlSTRVL(staGRNDT[P_intROWID][3],""));
			txtPORNO.setText(nvlSTRVL(staGRNDT[P_intROWID][4],""));
			txtPORDT.setText(nvlSTRVL(staGRNDT[P_intROWID][5],""));
			txtCHLNO.setText(nvlSTRVL(staGRNDT[P_intROWID][6],""));
			txtCHLDT.setText(nvlSTRVL(staGRNDT[P_intROWID][7],""));
			txtVENCD.setText(nvlSTRVL(staGRNDT[P_intROWID][8],""));
			txtVENNM.setText(nvlSTRVL(staGRNDT[P_intROWID][9],""));
			//txtLUSBY.setText(nvlSTRVL(staGRNDT[P_intROWID][10],""));
			//txtLUPDT.setText(nvlSTRVL(staGRNDT[P_intROWID][11],""));
			cl_dat.M_txtUSER_pbst.setText(nvlSTRVL(staGRNDT[P_intROWID][10],""));
			cl_dat.M_txtDATE_pbst.setText(nvlSTRVL(staGRNDT[P_intROWID][11],""));
			//Display data from vector in hashtable in the JTable
			tblGRNDT.clrTABLE();
			Vector L_vtrTEMP = (Vector)hstGRNDT.get(staGRNDT[P_intROWID][0]);
			StringTokenizer L_stkTEMP=null;
			for(int i=0;i<L_vtrTEMP.size();i++)
			{	//Retrieve data from vector & display in table using string tokenizer
				L_stkTEMP=new StringTokenizer((String)L_vtrTEMP.elementAt(i),"|");
				tblGRNDT.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,TBL_MATCD);	
				tblGRNDT.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,TBL_MATDS);
				tblGRNDT.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,TBL_UOMCD);
				tblGRNDT.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,TBL_CHLQT);
				tblGRNDT.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,TBL_RECQT);
				tblGRNDT.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,TBL_ACPQT);
				tblGRNDT.setValueAt(nvlSTRVL(L_stkTEMP.nextToken(),""),i,TBL_UNTRT);
			}
			setMSG("Record "+Integer.toString(intRECCT)+" of "+Integer.toString(hstGRNDT.size()),'N');
		}
		catch(Exception e)
		{
			setMSG(e,"Child.dspDATA");
		}
	}
}
