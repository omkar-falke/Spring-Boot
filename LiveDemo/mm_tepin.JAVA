/*
System Name   : Material Management System
Program Name  : Perpetual Inventry
Program Desc. : Generate Perpetual Inventry Number, Modifify and Print 
Author        : 
Date          : 28/07/2004
Version       : MMS 2.0
*/

import java.sql.*;import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JCheckBox;import javax.swing.JPanel;import javax.swing.JButton;import java.awt.event.*;
import javax.swing.BorderFactory;import javax.swing.JTabbedPane;import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;import javax.swing.JList;import javax.swing.DefaultCellEditor;
import javax.swing.JTable;import javax.swing.border.TitledBorder;import java.util.Vector;
import javax.swing.InputVerifier;import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.JComponent;import javax.swing.JOptionPane;import java.io.FileOutputStream;import java.io.DataOutputStream;

/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Material Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>  Perpetual Inventry </TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                  Generate Perpetual Inventry Number, Modifify and Print.&nbsp; </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>            </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\mm_tepin.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>f:\splerp2\mm_tepin.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>ATC </TD></TR>  <TR>    <TD>Date</TD>    <TD>09/08/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD></TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD></TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD></TD>    <TD> </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD></TD>    <TD></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/

public class mm_tepin extends cl_pbase
{									/**Text Field For Perpetual Inventry Number*/
	public  JTextField txtPINNO;	/**Text Field For 	Perpetual Inventry date	*/
	private JTextField txtPINDT;	/**Text Field For Check By*/
	private JTextField txtCHKBY;	/**Text Field For Check Date*/
	private JTextField txtCHKDT;	/**Text Field For Remark*/
	private JTextField txtREMDS;	
	private JTextField txtMATCD;	/**Text Field For From Material Code*/
	private JTextField txtFMMAT;	/**Text Field For To Material Code	*/
	private JTextField txtTOMAT;
	private JTextField txtTRNDT;
									/**ComboBox For Priter List	*/
	public JComboBox cmbDESTN;
	public JComboBox cmbPINTP;
									/**Button For Print Button	*/
	private JButton btnPRINT;		
									/**Final String for Report name	*/
	public final String strFILNM = "C:\\Reports\\mm_rppin.doc";	
	
	public FileOutputStream fosREPORT;
	public DataOutputStream dosREPORT;
									/**JTabel For Pirpetual inventry details*/	
	public cl_JTable tblPINDT;
									/**String for perpetual inventry number	 */	
	private String strPINNO;		/**String for remark description		 */	
	private String strREMDS;		/**String for perpetual inventry date	 */	
	private String strPINDT;		/**String for temparary variable for 	 */	
	private String strTEMP;			
		
	final String strTRNTP_fn = "PI";
	private Hashtable<String,String>  hstITMDT = new Hashtable<String,String>();
	private Hashtable<String,String>  hstSTKQT = new Hashtable<String,String>();
	private Vector<String> vtrMATCD,vtrSTKQT;
	private JPanel pnlPIDTL;
	private mm_tePINTBLINVFR objTBLVRF;
										//Final integer Variable to hold the column number 
	private final int TBL_CHKFL = 0;
	private final int TBL_MATCD = 1;
	private final int TBL_MATDS = 2;
	private final int TBL_UOMCD = 3;
	private final int TBL_LOCCD = 4;
	private final int TBL_ACTQT = 5;
	private int intRECCT =0;	
	private boolean flgRMKFL = false;		//Remark Flag
	
	private final String strDOCTP_fn ="03";	
										
	private mm_rpper objREPRT;	//declare reference variable for perpetual inventry report
	
	/**Constructor for the form
	 */
	
	mm_tepin()
	{
		super(2);
		try
		{	
			cmbDESTN = new JComboBox();
			//Add Component to JFrame
			setMatrix(20,8);
			add(new JLabel("Per. Inv. Type"),2,1,1,1,this,'L');
			add(cmbPINTP = new JComboBox(),2,2,1,1,this,'L');
			cmbPINTP.addItem("Regular");
			cmbPINTP.addItem("Automatic ");
			add(new JLabel("Transaction Dt"),2,3,1,1,this,'L');
			add(txtTRNDT = new TxtDate(),2,4,1,1,this,'L');
			add(new JLabel("Per. Inv. No."),3,1,1,1,this,'L');
			add(txtPINNO = new TxtLimit(8),3,2,1,1,this,'L');
			add(new JLabel("From Mat."),3,3,1,1,this,'L');
			add(txtFMMAT = new TxtLimit(10),3,4,1,1,this,'L');
			add(new JLabel("To Mat."),3,5,1,1,this,'L');
			add(txtTOMAT = new TxtLimit(10),3,6,1,1,this,'L');
			add(btnPRINT = new JButton("PRINT"),3,7,1,1,this,'L');
			
			add(new JLabel("Per.Inv.Date"),4,1,1,1,this,'L');
			add(txtPINDT = new TxtDate(),4,2,1,1,this,'L');
			add(new JLabel("Checked By"),4,3,1,1,this,'L');
			add(txtCHKBY = new TxtLimit(3),4,4,1,1,this,'L');
			add(new JLabel("Checked Date"),4,5,1,1,this,'L');
			add(txtCHKDT = new TxtDate(),4,6,1,1,this,'L');
			//Column Heading 
			String[] L_strTBLHD = {" ","Item Code","Description","UOM ","Location","Physical Qty"};
			//Columen Size
			int[] L_intCOLSZ = {25,80,375,80,90,90};
			tblPINDT = crtTBLPNL1(this,L_strTBLHD,500,6,1,10,7.9,L_intCOLSZ,new int[]{0});

			add(new JLabel("Remarks"),17,1,1,1,this,'L');
			add(txtREMDS=new TxtLimit(200),17,2,1,7,this,'L');
						
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_tePINTBLINVFR();
			tblPINDT.setInputVerifier(objTBLVRF);		
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
			
	}
	/**<b>TASKS :</b><br>
	* &nbsp&nbsp&nbspSource=M_cmbOPTN_pbst : Show/hide PRINT Button depending on item selected<br>&nbsp&nbsp&nbsp&nbsp&nbspSelection Item used for this. There fore, change in display should be taken care
	* &nbsp&nbsp&nbspSource=btnPRINT : Popup list of printer for user selection to select printer <br>	 
	* */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				cmbPINTP.setEnabled(true);
				txtPINNO.setEnabled(false);
				txtTRNDT.setEnabled(false);
				txtFMMAT.setEnabled(true);
				txtTOMAT.setEnabled(true);
				cmbPINTP.requestFocus();
			//	txtFMMAT.requestFocus();
				btnPRINT.setVisible(false);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				cmbPINTP.setEnabled(false);
				txtPINNO.setEnabled(true);
				txtFMMAT.setEnabled(false);
				txtTOMAT.setEnabled(false);
				tblPINDT.cmpEDITR[TBL_ACTQT].setEnabled(true);
				btnPRINT.setVisible(false);
				txtPINNO.requestFocus();
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				cmbPINTP.setEnabled(false);
				txtPINNO.setEnabled(true);
				txtFMMAT.setEnabled(false);
				txtTOMAT.setEnabled(false);
				btnPRINT.setVisible(false);
				txtPINNO.requestFocus();
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				cmbPINTP.setEnabled(false);
				txtPINNO.setEnabled(true);
				txtFMMAT.setEnabled(false);
				txtTOMAT.setEnabled(false);
				txtPINNO.requestFocus();
				btnPRINT.setVisible(true);
				cmbDESTN.setVisible(true);
				cmbDESTN.setEnabled(true);
			}
			else
			{
				cmbPINTP.setEnabled(false);
				btnPRINT.setVisible(false);
				cmbDESTN.setVisible(false);
			}
			
		}
		if(M_objSOURC == txtTRNDT)
		{
			try
			{
				vtrMATCD = new Vector<String>();
				vtrSTKQT = new Vector<String>();
				String L_strSTRTP =M_strSBSCD.substring(2,4);
				String strTRNDT ="",L_strMATCD ="";
				// Get yesterday's transaction data
				strTRNDT = txtTRNDT.getText().trim();
				intRECCT =0;
				M_strSQLQRY = "SELECT ST_MATCD,ST_STKQT FROM MM_STMST WHERE ST_STRTP ='"+L_strSTRTP +"' AND "+
							  "ST_MATCD IN(SELECT IS_MATCD FROM MM_ISMST WHERE CONVERT(varchar,IS_AUTDT,103) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRNDT))+"'"+
							  " AND IS_STRTP ='"+L_strSTRTP +"'AND IS_STSFL ='2')"+
							  " UNION "+
							  "SELECT ST_MATCD,ST_STKQT FROM MM_STMST WHERE ST_STRTP ='"+L_strSTRTP +"' AND "+
							  " ST_MATCD IN(SELECT GR_MATCD FROM MM_GRMST WHERE CONVERT(varchar,GR_ACPDT,103) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRNDT))+"' "+
							  " AND GR_STRTP ='"+L_strSTRTP +"'AND GR_STSFL ='2')"+
							  " UNION "+
							  "SELECT ST_MATCD,ST_STKQT FROM MM_STMST WHERE ST_STRTP ='"+L_strSTRTP +"' AND "+
							  "ST_MATCD IN(SELECT MR_MATCD FROM MM_MRMST WHERE CONVERT(varchar,MR_AUTDT,103) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRNDT))+"' "+
							  " AND MR_STRTP ='"+L_strSTRTP +"'AND MR_STSFL ='2')"+
							  " UNION "+
							  "SELECT ST_MATCD,ST_STKQT FROM MM_STMST WHERE ST_STRTP ='"+L_strSTRTP +"' AND "+
							  "ST_MATCD IN(SELECT SA_MATCD FROM MM_SAMST WHERE SA_SANDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRNDT))+"' and isnull(SA_STSFL,'') <> 'X')"+
							  " ORDER BY ST_MATCD ";
				
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				int L_intRECCT=0;
				if(M_rstRSSET.next())
				{
					while(M_rstRSSET.next())
					{
						L_strMATCD =M_rstRSSET.getString("ST_MATCD");
						if(!vtrMATCD.contains((String)L_strMATCD))
						{
							// Item is not available in the vector, then add in the vector
							vtrMATCD.addElement(L_strMATCD);
							vtrSTKQT.addElement(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0.00"));
						}
						L_intRECCT++;
					}
					M_rstRSSET.close();
					M_rstRSSET = null;
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
				JOptionPane L_OPTNPN = new JOptionPane();
				int L_intOPTN = L_OPTNPN.showConfirmDialog(this,"No. Of Items "+vtrMATCD.size()+" , Continue ","",JOptionPane.YES_NO_OPTION);
				intRECCT = vtrMATCD.size();
				if(L_intOPTN == 0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"txtTRNDT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		//Action On Button PRINT
		if(M_objSOURC == cmbPINTP)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				try
				{
					if(cmbPINTP.getSelectedIndex()==1)
					{
						txtTRNDT.setEnabled(true);
						txtPINNO.setEnabled(false);
						txtFMMAT.setEnabled(false);
						txtTOMAT.setEnabled(false);
						txtTOMAT.setText("");
						txtFMMAT.setText("");
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
						txtTRNDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						txtTRNDT.requestFocus();
						btnPRINT.setVisible(false);
					}
					if(cmbPINTP.getSelectedIndex()==0)
					{
						txtTRNDT.setEnabled(false);
						txtPINNO.setEnabled(false);
						txtFMMAT.setEnabled(true);
						txtTOMAT.setEnabled(true);
						txtTRNDT.setText("");
						txtFMMAT.requestFocus();
						btnPRINT.setVisible(false);
					}
				}
				catch(Exception L_E)
				{
					
				}
			}
		}
		if(M_objSOURC == btnPRINT)
		{
			if(txtPINNO.getText().trim().length() > 0)
			{					
				String L_strPINNO = txtPINNO.getText().trim();	//take string as pi number from perpetual inventry textfield
				objREPRT = new mm_rpper(M_strSBSCD,L_strPINNO);	//initialise object to ref varialbe call constructo for pi number
				objREPRT.getDATA(L_strPINNO);
				JComboBox L_cmbLOCAL = objREPRT.getPRNLS();
				objREPRT.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpper.doc",L_cmbLOCAL.getSelectedIndex());
			}
		}
		if(M_objSOURC == txtPINNO)
		{
			String L_strPINNO = txtPINNO.getText().trim();
			clrCOMP();
			txtPINNO.setText(L_strPINNO);
			txtPINDT.setText(cl_dat.M_strLOGDT_pbst);												
			txtCHKBY.setText(cl_dat.M_strUSRCD_pbst);
			txtCHKDT.setText(cl_dat.M_strLOGDT_pbst);
			//call method to display data in table for given perpetual inventry number
			getDATA(M_strSBSCD.substring(2,4),L_strPINNO);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				btnPRINT.requestFocus();
		}
	}
	/**Method for action on key pressed on the component <br>
	 * Key F1 For Help For the text field value.
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int[] L_inaCOLSZ = new int[]{100,100,380};
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtFMMAT)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtFMMAT";
				String L_ARRHDR[] = {"Item Code","UOM","Description"};
				M_strSQLQRY = "SELECT ST_MATCD,ST_UOMCD,ST_MATDS FROM MM_STMST WHERE "
					+"ST_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STSFL,'') <>'X'";
				if(txtFMMAT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_MATCD LIKE '"+txtFMMAT.getText().trim() +"%'";
				M_strSQLQRY += " ORDER BY ST_MATCD ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT",L_inaCOLSZ);
			}
			if(M_objSOURC == txtTOMAT)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtTOMAT";
				String L_ARRHDR[] = {"Item Code","UOM","Description"};
				M_strSQLQRY = "SELECT ST_MATCD,ST_UOMCD,ST_MATDS FROM MM_STMST WHERE "
					+"ST_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STSFL,'') <>'X'";
				if(txtTOMAT.getText().trim().length() > 0)
					M_strSQLQRY += " AND ST_MATCD LIKE '"+txtTOMAT.getText().trim() +"%'";
				if(txtFMMAT.getText().trim().length() > 0)
					M_strSQLQRY += " AND ST_MATCD > '"+txtFMMAT.getText().trim()+"' ";
				M_strSQLQRY += " ORDER BY ST_MATCD ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtPINNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPINNO";
				String L_ARRHDR[] = {"PIN Number","PIN Date"};
				M_strSQLQRY = "SELECT DISTINCT PI_PINNO,PI_PINDT FROM MM_PIMST ";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					M_strSQLQRY += " WHERE isnull(PI_STSFL,'') NOT IN ('X','1') ";
				else
					M_strSQLQRY += " WHERE isnull(PI_STSFL,'') <>'X'";
				if(txtPINNO.getText().trim().length() >0)
					M_strSQLQRY += " AND PI_PINNO LIKE '" + txtPINNO.getText().trim() + "%'";
				M_strSQLQRY += " AND PI_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				M_strSQLQRY +=" ORDER BY PI_PINNO DESC ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtTOMAT)
			{
				/**Method To confirm no of items in the inventry list selection
				 * After Selection of to material code display confimation dialog box if yes 
				 * the go to save other wise allow to reenter limit	 */
				try
				{
					intRECCT =0;
					if(txtTOMAT.getText().trim().length() > 0 && txtFMMAT.getText().trim().length() > 0)
					{
						M_strSQLQRY = "SELECT COUNT(*) FROM MM_STMST WHERE ST_MATCD BETWEEN '"
							+txtFMMAT.getText().trim()+"' AND '"+txtTOMAT.getText().trim()+"' "
							+"AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STSFL,'') <> 'X'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next())
						{
							JOptionPane L_OPTNPN = new JOptionPane();
							//int L_intOPTN = L_OPTNPN.showConfirmDialog(this,"Please, Note This Inventry Contain "+M_rstRSSET.getString(1)+" Items","",JOptionPane.INFORMATION_MESSAGE);
							intRECCT = M_rstRSSET.getInt(1);
							int L_intOPTN = L_OPTNPN.showConfirmDialog(this,"No. Of Items "+intRECCT+" , Continue ","",JOptionPane.YES_NO_OPTION);
							if(L_intOPTN == 0)
								cl_dat.M_btnSAVE_pbst.requestFocus();
							else
							{
								txtTOMAT.select(0,txtTOMAT.getText().trim().length());
								txtTOMAT.requestFocus();
							}
						}
					}
					else
						cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"Enter On TOMAT");
				}
			}
			else if(M_objSOURC == txtCHKBY)
			{
				txtCHKBY.transferFocus();
				txtCHKBY.setText(txtCHKBY.getText().toUpperCase());
			}
			else if(M_objSOURC == txtCHKDT)
			{
				tblPINDT.cmpEDITR[TBL_ACTQT].setEnabled(true);
				tblPINDT.setRowSelectionInterval(tblPINDT.getSelectedRow(),tblPINDT.getSelectedRow());		
				tblPINDT.setColumnSelectionInterval(TBL_ACTQT,TBL_ACTQT);		
				tblPINDT.editCellAt(tblPINDT.getSelectedRow(),TBL_ACTQT);
				tblPINDT.cmpEDITR[TBL_ACTQT].requestFocus();
			}
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	/**Method when user pressed 'Enter' Or 'OK' Button on Help Window
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtPINNO"))
			{
				clrCOMP();
				txtPINNO.setText(cl_dat.M_strHLPSTR_pbst);
				if(txtPINNO.getText().trim().length() != 0)
					//call method to display data in tabel for perpetual inventry number
					getDATA(M_strSBSCD.substring(2,4),txtPINNO.getText().trim());
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					btnPRINT.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtFMMAT"))
			{
				txtFMMAT.setText(cl_dat.M_strHLPSTR_pbst);
				txtTOMAT.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtTOMAT"))
			{
				txtTOMAT.setText(cl_dat.M_strHLPSTR_pbst);
				txtTOMAT.requestFocus();
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**Method to Display message when cursor move to component
	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtPINNO)
			setMSG("Enter Perpetual Inventry Number Or Press 'F1' For Help..",'N');
		if(M_objSOURC == txtFMMAT)
			setMSG("Enter From Material Code Or Press 'F1' For Help..",'N');
		if(M_objSOURC == txtTOMAT)
			setMSG("Enter To Material Code Or Press 'F1' For Help..",'N');
		if(M_objSOURC == txtPINDT)
			setMSG("Enter Perpetual Inventry Date..",'N');
		if(M_objSOURC == txtCHKBY)
			setMSG("Enter Checked By..",'N');
		if(M_objSOURC == txtCHKDT)
			setMSG("Enter Checked Date..",'N');
	}
	//method to get data form database for the given perpetual inventry number
	private boolean getDATA(String P_strSTRTP,String P_strPINNO)
	{
		boolean L_FIRST = true;
		java.sql.Date L_datTMPDT;
		String L_strDPTCD="";
		
		txtCHKBY.setText(cl_dat.M_strUSRCD_pbst);
		txtCHKDT.setText(cl_dat.M_strLOGDT_pbst);			
		
		try
		{
			String L_TPRCD,L_CHLQT,L_RECQT;
			String L_strMATCD ="",L_strINDNO ="",L_strSTSFL="",L_strORGCD="",L_strRCNVL ="";
			int i = 0;
			if(tblPINDT.isEditing())
				tblPINDT.getCellEditor().stopCellEditing();
			tblPINDT.setRowSelectionInterval(0,0);
			tblPINDT.setColumnSelectionInterval(0,0);
			setCursor(cl_dat.M_curWTSTS_pbst);
			//SQL String to get Data for perpetual inventry number 
			M_strSQLQRY = "SELECT PI_MMSBS,PI_STRTP,PI_PINNO,PI_PINDT,PI_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD,";
			M_strSQLQRY +=" PI_ACTQT,PI_CHKBY,PI_CHKDT,PI_LUSBY,PI_LUPDT FROM MM_PIMST,MM_STMST";
			M_strSQLQRY += " WHERE PI_STRTP = ST_STRTP AND PI_MATCD = ST_MATCD AND PI_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_strSQLQRY += " AND PI_PINNO = '" + P_strPINNO + "'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				M_strSQLQRY += " AND isnull(PI_STSFL,'') NOT IN ('X','1') ORDER BY PI_MATCD";
			else
				M_strSQLQRY += " AND isnull(PI_STSFL,'') <> 'X' ORDER BY PI_MATCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(hstITMDT !=null)
				hstITMDT.clear();
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				flgRMKFL = true;
				if(L_FIRST)
				{
					txtPINNO.setText(P_strPINNO);
					L_datTMPDT = M_rstRSSET.getDate("PI_PINDT");
					if(L_datTMPDT !=null)
					{
						txtPINDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						strPINDT = txtPINDT.getText().trim();
					}
					else
						txtPINDT.setText("");
					txtCHKBY.setText(nvlSTRVL(M_rstRSSET.getString("PI_CHKBY"),""));
					L_datTMPDT = M_rstRSSET.getDate("PI_CHKDT");
					if(L_datTMPDT !=null)
					{
						txtCHKDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					}
					else
						txtCHKDT.setText("");
				}
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("PI_MATCD"),"");
				tblPINDT.setValueAt(L_strMATCD,i,TBL_MATCD);
				hstITMDT.put(L_strMATCD,"");
				tblPINDT.setValueAt(M_rstRSSET.getString("ST_MATDS"),i,TBL_MATDS);
				tblPINDT.setValueAt(M_rstRSSET.getString("ST_UOMCD"),i,TBL_UOMCD);
				tblPINDT.setValueAt(M_rstRSSET.getString("ST_LOCCD"),i,TBL_LOCCD);
				tblPINDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("PI_ACTQT"),"0.0"),i,TBL_ACTQT);
				i++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();

			M_strSQLQRY = "SELECT RM_REMDS,RM_STSFL FROM MM_RMMST ";
			M_strSQLQRY += " WHERE RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
			M_strSQLQRY += " AND RM_TRNTP = '" + strTRNTP_fn + "'";
			M_strSQLQRY += " AND RM_DOCNO = '" + txtPINNO.getText().trim() + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(!nvlSTRVL(M_rstRSSET.getString("RM_STSFL"),"").equals("X"))
						txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
					else
						txtREMDS.setText("");
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception e)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(e,"getDATA");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
		return !L_FIRST;
	}
	//method to validate input 
	boolean vldDATA()
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			if(cmbPINTP.getSelectedIndex()==0)
			{
				if(txtFMMAT.getText().trim().length() == 0)
				{
					setMSG("Enter From Material Code..",'E');
					return false;
				}
				if(txtTOMAT.getText().trim().length() == 0)
				{
					setMSG("Enter To Material Code..",'E');
					return false;
				}
			}
			else if(cmbPINTP.getSelectedIndex()==1)
			{
				if(txtTRNDT.getText().trim().length() == 0)
				{
					setMSG("Enter Transaction Date..",'E');
					return false;
				}
			}
			if(intRECCT ==0)
			{
				setMSG("No of items are 0 , can not generate P.I...",'E');
				return false;
			}
		}
		else
		{
			if(txtPINDT.getText().trim().length() ==0)
			{
				setMSG("PIN Date can not be blank..",'E');
			}
			if(txtCHKBY.getText().trim().length() ==0)
			{
				setMSG("Checked By can not be blank..",'E');
			}
			if(txtCHKDT.getText().trim().length() ==0)
			{
				setMSG("Checking Date can not be blank..",'E');
			}
			for(int i=0;i<tblPINDT.getRowCount();i++)
			{
				if(tblPINDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					strTEMP = nvlSTRVL(tblPINDT.getValueAt(i,TBL_MATCD).toString(),"");
					if(strTEMP.length() == 0)
					{
						setMSG("item Code can not be blank..",'E');
						return false;
					}
					strTEMP = tblPINDT.getValueAt(i,TBL_ACTQT).toString();
					if(strTEMP.length() == 0)
					{
						setMSG("Enter Physical Qty...",'E');
						return false;
					}
					/*if(Double.parseDouble(tblPINDT.getValueAt(i,TBL_SANQT).toString())==0)
					{
						setMSG("Qty. can not be zero..",'E');
						return false;
					}*/
				}
			}
			if(tblPINDT.isEditing())
				tblPINDT.getCellEditor().stopCellEditing();
			tblPINDT.setRowSelectionInterval(0,0);
			tblPINDT.setColumnSelectionInterval(0,0);
		}
		return true;													 
	}
	//method to save the details for the given perpetual number 
	void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(!vldDATA())
			{
				return;
			}
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				strPINDT = cl_dat.M_strLOGDT_pbst;
				strPINNO = genPINNO();
				if(strPINNO !=null)
				{
					if(cmbPINTP.getSelectedIndex() ==0)
					{
						//Query For Add Recored For Perpetual Inventory Code
						M_rstRSSET = cl_dat.exeSQLQRY("SELECT ST_MATCD,ST_STKQT FROM MM_STMST "
								+"WHERE ST_MATCD BETWEEN '"+txtFMMAT.getText().trim()+"' AND '"
								+txtTOMAT.getText().trim()+"' AND "
								+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STSFL,'') <> 'X'");
						if(M_rstRSSET != null)
						{
							while(M_rstRSSET.next())
							{
								M_strSQLQRY = "INSERT INTO MM_PIMST(PI_MMSBS,PI_STRTP,PI_PINNO,"
									+"PI_PINDT,PI_MATCD,PI_STKQT,PI_TRNFL,PI_STSFL,PI_LUSBY,"
									+"PI_LUPDT) VALUES ('"+M_strSBSCD+"','"
									+M_strSBSCD.substring(2,4)+"','"+txtPINNO.getText().trim()+"'"
									+",'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strPINDT))+"','"
									+M_rstRSSET.getString("ST_MATCD")+"',"
									+M_rstRSSET.getDouble("ST_STKQT")+","+getUSGDTL("PI",'I',"0")+")";
								cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
							}
						}
					}
					else if(cmbPINTP.getSelectedIndex() ==1)
					{
						if(vtrMATCD !=null)
						for(int i=0;i<vtrMATCD.size();i++)
						{
							M_strSQLQRY = "INSERT INTO MM_PIMST(PI_MMSBS,PI_STRTP,PI_PINNO,"
								+"PI_PINDT,PI_MATCD,PI_STKQT,PI_TRNFL,PI_STSFL,PI_LUSBY,"
								+"PI_LUPDT) VALUES ('"+M_strSBSCD+"','"
								+M_strSBSCD.substring(2,4)+"','"+txtPINNO.getText().trim()+"'"
								+",'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strPINDT))+"','"
								+vtrMATCD.elementAt(i).toString()+"',"
								+vtrSTKQT.elementAt(i).toString()+","+getUSGDTL("PI",'I',"0")+")";
							cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
						}
					}
					if(cl_dat.M_flgLCUPD_pbst)
						exePINNO(strPINNO);
				}
			}
			//If Delete Option is selected
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
			{
				for(int i=0;i<tblPINDT.getRowCount();i++)
				{
					if(tblPINDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						M_strSQLQRY = "UPDATE MM_PIMST SET ";
						M_strSQLQRY += getUSGDTL("PI",'U',"X");
						M_strSQLQRY += " where PI_MMSBS = '" + M_strSBSCD + "'";
						M_strSQLQRY += " and PI_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
						M_strSQLQRY += " and PI_PINNO = '" + txtPINNO.getText().trim() + "'";
						M_strSQLQRY += " and PI_MATCD = '" + tblPINDT.getValueAt(i,TBL_MATCD).toString() + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					}
				}
				if(flgRMKFL)							// To update existing record
				{					
					M_strSQLQRY = "Update MM_RMMST set ";
					M_strSQLQRY += getUSGDTL("RM",'U',"X");
					M_strSQLQRY += " where RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
					M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
					M_strSQLQRY += " and RM_DOCNO = '" + txtPINNO.getText().trim() + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
			{
				for(int i=0;i<tblPINDT.getRowCount();i++)
				{
					
					if(tblPINDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						if(hstITMDT.containsKey(tblPINDT.getValueAt(i,TBL_MATCD).toString()))
						{
							M_strSQLQRY = "UPDATE MM_PIMST SET ";
							M_strSQLQRY +=" PI_ACTQT ="+tblPINDT.getValueAt(i,TBL_ACTQT)+",";
							M_strSQLQRY += getUSGDTL("PI",'U',null)+" ,PI_STSFL = '1'";
							M_strSQLQRY += " where PI_MMSBS = '" + M_strSBSCD + "'";
							M_strSQLQRY += " and PI_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
							M_strSQLQRY += " and PI_PINNO = '" + txtPINNO.getText().trim() + "'";
							M_strSQLQRY += " and PI_MATCD = '" + tblPINDT.getValueAt(i,TBL_MATCD).toString() + "'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
						else
						{
							exeINSREC(i);
							hstITMDT.put(tblPINDT.getValueAt(i,TBL_MATCD).toString(),"");
						}
					}
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				strREMDS = txtREMDS.getText().trim();
				if(!flgRMKFL)
				{
					if(strREMDS.length() > 0)
					{								// To add new record
						M_strSQLQRY = "Insert into MM_RMMST(RM_STRTP,RM_TRNTP,";
						M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,";
						M_strSQLQRY += "RM_LUPDT) values (";
						M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
						M_strSQLQRY += "'" + strTRNTP_fn + "',";
						M_strSQLQRY += "'" + strTRNTP_fn + "',";
						M_strSQLQRY += "'" + txtPINNO.getText().trim() + "',";
						M_strSQLQRY += "'" + strREMDS + "',";
						M_strSQLQRY += getUSGDTL("RM",'I',"0")+")";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				else
				{
					if(strREMDS.length() > 0)
					{
						M_strSQLQRY = "Update MM_RMMST set RM_REMDS ='"+txtREMDS.getText().trim()+"',";
						M_strSQLQRY += getUSGDTL("RM",'U',"");
						M_strSQLQRY += " where RM_STRTP = '" + M_strSBSCD.substring(0,2) + "'";
						M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
						M_strSQLQRY += " and RM_DOCNO = '" + txtPINNO.getText().trim() + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					flgRMKFL = false;
					setMSG("Data saved successfully",'N');
					intRECCT =0;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						JOptionPane.showMessageDialog(this,"Please, Note down the Per. Inv. No. " + strPINNO,"PIN No.",JOptionPane.INFORMATION_MESSAGE);
						clrCOMP();		
						txtPINDT.setText(cl_dat.M_strLOGDT_pbst);
						txtCHKBY.setText(cl_dat.M_strUSRCD_pbst);
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
					clrCOMP();	
					if(hstITMDT !=null)
						hstITMDT.clear();
					if(hstSTKQT !=null)
						hstSTKQT.clear();
				}
				else
				{
					setMSG("Error in saving data..",'E');
				}
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	//Overlod setENBL Method to enable and disable component on selection
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			txtFMMAT.setEnabled(true);
			txtTOMAT.setEnabled(true);
			txtPINNO.setEnabled(false);
			cmbDESTN.setEnabled(false);
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{
			txtPINNO.setEnabled(true);
			tblPINDT.cmpEDITR[TBL_ACTQT].setEnabled(true);
			cmbDESTN.setEnabled(false);
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			txtPINNO.setEnabled(true);
			btnPRINT.setEnabled(true);
			cmbDESTN.setEnabled(true);
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			txtPINNO.setEnabled(true);
			cmbDESTN.setEnabled(false);
			tblPINDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		}
		txtPINDT.setEnabled(false);
		tblPINDT.cmpEDITR[TBL_MATCD].setEnabled(false);
		tblPINDT.cmpEDITR[TBL_MATDS].setEnabled(false);
		tblPINDT.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblPINDT.cmpEDITR[TBL_LOCCD].setEnabled(false);
		
	}
	//method to generate pirpetual inventry number
	private String genPINNO()
	{
		strPINNO ="";
		String L_PINNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for SAN;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP = 'MMXXPIN' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn +"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					if(L_CHP01.trim().length() >0)
					{
						M_rstRSSET.close();
						return null;
					}
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
			M_strSQLQRY += " and CMT_CGSTP = 'MMXXPIN'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn + "'";			
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY," ");
			if(cl_dat.exeDBCMT("genPINNO"))
			{
				L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
				for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
					L_PINNO += "0";
			
				L_CCSVL = L_PINNO + L_CCSVL;
				L_PINNO = L_CODCD + L_CCSVL;
				txtPINNO.setText(L_PINNO);
				strPINNO = L_PINNO;
			}
			else return null;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genPINNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_PINNO;
	}
	// Method to update the last Issue No.in the CO_CDTRN
	private void exePINNO(String P_strPINNO)
	{
		try
		{
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + P_strPINNO.substring(3) + "',";
			M_strSQLQRY += getUSGDTL("CMT",'U',"");
			M_strSQLQRY += " where CMT_CGMTP = 'DOC' and CMT_CGSTP ='MMXXPIN'";
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strDOCTP_fn +"'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeISSNO ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	//method for tabel input verification
	private class mm_tePINTBLINVFR extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			String L_strTEMP ="";
			try
			{
				if(P_intCOLID==TBL_MATCD)
				{
					strTEMP = tblPINDT.getValueAt(P_intROWID,TBL_MATCD).toString();
		    		if(strTEMP.length()>0)
					{
						for(int i=0;i<=P_intROWID-1;i++)
						{
							if(tblPINDT.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
							if(tblPINDT.getValueAt(i,TBL_MATCD).toString().trim().equals(strTEMP.trim()))
							{
								setMSG("Duplicate entry ..",'E');
								return false;
							}
						}
						if(!vldMATCD(strTEMP,P_intROWID))
						{
							return false;
						}
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
			}
			return true;
		}
	}
	//Method to validate material code in the table
	private boolean vldMATCD(String P_strMATCD,int P_intROWID)
	{
		try
		{
			M_strSQLQRY = "select ST_MATDS,ST_UOMCD,ST_STKQT,ST_LOCCD from MM_STMST,MM_STPRC where";
			M_strSQLQRY += " ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND ST_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_strSQLQRY += " AND ST_MATCD = '" + P_strMATCD + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				tblPINDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),P_intROWID,TBL_UOMCD);
				tblPINDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),P_intROWID,TBL_MATDS);
				tblPINDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),"0"),P_intROWID,TBL_LOCCD);
				hstSTKQT.put(P_strMATCD,nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"));
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			setMSG("Invalid Material Code.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"vldMATCD");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	//method to insert record in database for record
	private boolean exeINSREC(int i)
	{
		try
		{
			String L_strMATCD = tblPINDT.getValueAt(i,TBL_MATCD).toString();
			M_strSQLQRY = "Insert into MM_PIMST(PI_MMSBS,PI_STRTP,PI_PINNO,PI_PINDT,PI_MATCD,PI_STKQT,";
			M_strSQLQRY += "PI_ACTQT,PI_CHKBY,PI_CHKDT,PI_TRNFL,PI_STSFL,PI_LUSBY,PI_LUPDT) values (";
			M_strSQLQRY += "'"+ M_strSBSCD + "',";;
			M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
			M_strSQLQRY += "'" + txtPINNO.getText().trim() + "',";
			M_strSQLQRY += "'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(strPINDT))+"',";
			M_strSQLQRY += "'"+ tblPINDT.getValueAt(i,TBL_MATCD).toString() +"',";
			M_strSQLQRY +=  hstSTKQT.get(tblPINDT.getValueAt(i,TBL_MATCD).toString()) +",";
			M_strSQLQRY +=  tblPINDT.getValueAt(i,TBL_ACTQT).toString() +",";
			M_strSQLQRY += "'" + txtCHKBY.getText().trim() + "',";
			M_strSQLQRY += "'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHKDT.getText().trim()))+ "',";
			// checked date 
			M_strSQLQRY += getUSGDTL("PI",'I',"0")+")";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeINSREC");
			setCursor(cl_dat.M_curDFSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			return false;
		}
		return true;
	}
}
