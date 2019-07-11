import javax.swing.*;import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.event.*;import java.awt.Image;	import java.awt.Color; import java.io.File;	
import java.util.*;
import java.sql.SQLException;
/**<FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>												  <TR><TD>System Name</TD><TD>Human Resource Management System </TD></TR><TR><TD>Program Desc</TD><TD>Entry form for&nbsp;Employee Master </TD></TR><TR><TD>Basis Document</TD><TD>Employee joining form given by Mr. V.V.G.</TD></TR><TR><TD>Executable path</TD><TD>f:\dada\asoft\exec\splerp2\hr_meepm.class</TD></TR><TR><TD>Source path</TD><TD>g:\splerp2\hr_meepm.java</TD></TR><TR><TD>Author </TD><TD>AAP </TD></TR><TR><TD>Date</TD><TD>20/03/2003 </TD></TR><TR><TD>Version </TD><TD>1.0.0</TD></TR><TR><TD><STRONG>Revision : 1 </STRONG></TD><TD>for Base classes revision and Subsystem implementation</TD></TR><TR><TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; By</TD><TD>AAP</TD></TR><TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Date</TD><TD><P align=left>20/09/2003  </TD></TR><TR><TD><P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Version  </TD><TD>2.0.0.</TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>												  <TR><TD><P align=center>Table Name</P></TD><TD><P align=center>Primary Key</P></TD><TD><P align=center>Add</P></TD><TD><P align=center>Mod</P></TD><TD><P align=center>Del</P></TD><TD><P align=center>Enq</P></TD></TR><TR><TD>HR_EPMST</TD><TD>EP_EMPNO</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR><TR><TD>HR_QLMST</TD><TD>QL_EMPNO, QL_QLDES</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR><TR><TD>HR_FLMST</TD><TD>FL_EMPNO, FL_MEMNM</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR><TR><TD>HR_LGMST</TD><TD>LG_EMPNO, FL_LNGDS</TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD><TD><P align=center>&nbsp;</P></TD><TD><P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
 class hr_meepm extends cl_pbase implements KeyListener,ItemListener,ChangeListener, FocusListener,Runnable
{
	/**Thread to lay the components	  */
	private  Thread thrLAYCP;/**Count for number of children eligible for Education Allowance	 */
	private  int intEDCNT;/**String Arrays for components of respective Combos	 */
	private  final  String[]	staSEXDS_fn=new String[] {"Select","Male","Female"},
					staEDCAT_fn=new String[] {"Select","Technical","Non-Technical","Management","Others"},
					staEDLVL_fn=new String[] {"Select","Under-Graduate","Graduate","Post Graduate","Diploma","PG Diploma","Doctorate"},
					staDPTDS_fn=new String[] {"Select"},
					staDESGN_fn=new String[] {"Select"},		
					staMRTLS_fn=new String[] {"Select","Unmarried","Married","Widow","Widower","Divorcee"};
	private  String strMDALW,strLCFLT;
	private  boolean[] flaODLQL=new boolean[10],flaODLFL=new boolean[10],flaODLLG=new boolean[10];
	private boolean flgIMGFL;
	private  JTextField txtEDTFL=new JTextField(),txtEDTQL=new JTextField();/**Textfield for Employee name in full - FIRST NAME*/
	private  TxtNonNum	txtENM1;/**Textfield for Employee name in full - MIDDLE NAME*/
	private  TxtNonNum txtENM2; /**Textfield for Employee name in full - LAST NAME*/
	private  TxtNonNum txtENM3 ;/** Employee name in Short */
	private  JTextField txtACCRF ;/** Employee name in Short */
	private  JTextField txtSAPNO ;/** Employee name in Short */
	private  TxtNonNumLimit txtENM ;/** Employee Number */
	private  TxtNumLimit		txtEMPNO ;/** Field for Date Of Birth */	
	private  TxtDate txtDOB ;/** Field for Date Of Birth */	
	private  TxtDate txtTBLDT ;/** Joining Date */	
	private  TxtDate	txtDTJOIN ;/** Confirmation Date */
	private  TxtDate	txtCRFDT ;/** Bank Code */
	private  TxtLimit	txtBNKCD;/** Acct Number */
	private  TxtLimit	txtACTNO;/** PAN */
	private  TxtLimit	txtPANNO;/** Reference Person - 2 */
	private  TxtLimit	txtREF1;/** Reference Person - 2 */
	private  TxtLimit txtREF2;/** Name in Permenent Address */
//Textfields for permenet address
	private  JTextField	txtNAMEP;/** Picture file URL */
	private  JTextField	txtPHOTO;/** Emloyee Category */
	private  JTextField	txtEMPCT;/** Line 1 of Permenent Address */
	private  JTextField	txtADDP1;/** Line 2 of Permenent Address */
	private  JTextField	txtADDP2 ;/** District of Permenent Address */
	private  JTextField	txtDISTP ;/** State of Permenent Address */
	private  JTextField	M_txtSTAT_pbstP ;/** Tel num. of Permenent Address */
	private  JTextField	txtTELNOP ;/** Email id */
	private  JTextField	txtEMAIL ;/** PIN of Permenent Address */
	private  TxtNumLimit	txtPINP ;/** Name in Temporary(present) Address */
	private  JTextField txtNAMET ;/** Line 1 of Temporary(present) Address */
	private  JTextField	txtADDT1 ;/** Line 2 of Temporary(present) Address */
	private  JTextField	txtADDT2 ;/** District of Temporary(present) Address */
	private  JTextField	txtDISTT ;/** State of Temporary(present) Address */
	private  JTextField	M_txtSTAT_pbstT;/** Tel num. of Temporary(present) Address  */
	private  JTextField	txtTELNOT ;/** PIN of Temporary(present) Address */
	private  TxtNumLimit	txtPINT ;/** Combo for designation  */
	private  JCheckBox chkGENFL;
	private  JComboBox	cmbDESG ;/** Combo from Dept code */
	private  JComboBox	cmbDEPT;/** Combo from Sex */
	private  JComboBox	cmbSEX;/** Combo for Education catagory as Tech/nontech etc */
	private  JComboBox	cmbEDCAT;/** Combo for Management Grade */
	private  JComboBox	cmbMGRD;/** Combo for Educationlvl as graduate/PG etc */
	private  JComboBox	cmbEDLVL;/** Combo for Employee location as Works, H.O. etc */
	private  JComboBox	cmbEPLOC;/** Combo for marital status */
	private  JComboBox	cmbMRTL;/** Combo for Payment Mode */
	private  JComboBox	cmbPAYMD;
	private  JTextField	txtBRTPL;/** Field for place of birth*/
	private  JTextField	txtLANG1;/** Language 1 */
	private  JTextField	txtLANG2;/** Language 2 */
	private  JTextField	txtLANG3;/** Language 3 */
	private  JTextField	txtIDENT;/** Identification Mark on Body */
	private  JTextField	txtDSBLY;/** Phisical Disability */
	private  JTextField	txtLANG4;/** Language 4 */
	private  JTextField	txtNATL;
	private  JTextField	txtCATA;
	private  JTextField	txtLANG5;/** Language 5 */
	private  JTextField	txtBLDGP;/** Blood Group */
// TEXTFIELDS FOR PNLPAY
	private  TxtNumLimit		txtBASIC;/**  */
	private  TxtNumLimit		txtHRALW;/**  */
	private  TxtNumLimit		txtDNALW;/**  */
	private  TxtNumLimit		txtSPALW;/**  */
	private  TxtNumLimit		txtCONVY;/**  */
	private  TxtNumLimit		txtLNSUB;/**  */
	private  TxtNumLimit		txtCHEDN;/**  */
	private  TxtNumLimit		txtMDALW;/**  */
	private  TxtNumLimit		txtLTALW;/**  */
	private  TxtNumLimit		txtPFALW;/**  */
	private  TxtNumLimit		txtSAALW;/**  */
	private  TxtNumLimit		txtWSALW;/**  */
	private  TxtNumLimit		txtGRALW;/**  */
	private  TxtNumLimit		txtCRALW;/**  */
	private  TxtNumLimit		txtWKALW;/**  */
	private  TxtNumLimit		txtGROS;/**  */
	private  TxtNumLimit		txtVCLMT;/**  */		
	private  TxtNumLimit		txtTELEN;/**  */		
	private  TxtNumLimit		txtPPAY;/**  */
	private  JTabbedPane tbpMAIN;/**  */
	private  JCheckBox	chbREAD1;/**  */
	private  JCheckBox	chbWRT1;/**  */
	private  JCheckBox	chbSPK1;/**  */
	private  JCheckBox	chbREAD2;/**  */
	private  JCheckBox	chbWRT2;/**  */
	private  JCheckBox	chbSPK2;/**  */
	private  JCheckBox	chbREAD3;/**  */
	private  JCheckBox	chbWRT3;/**  */
	private  JCheckBox	chbSPK3;/**  */
	private  JCheckBox	chbREAD5;/**  */
	private  JCheckBox	chbWRT5;/**  */
	private  JCheckBox	chbSPK5;/**  */
	private  JCheckBox	chbREAD4;/**  */
	private  JCheckBox	chbWRT4;/**  */
	private  JCheckBox	chbTELEN;/**  */		
	private  JCheckBox	chbVCLMT;/**  */		
	private  JCheckBox	chbSPK4;/**  */
	private  boolean		flgFMLY;
	private  boolean		flgEDUCN;
	private  boolean		flgHSTRY;
	private  JRadioButton	rdbMTNG1;/** Flag for Mother Toung for language 1. */
	private  JRadioButton	rdbMTNG2;/** Flag for Mother Toung language 2. */
	private  JRadioButton	rdbMTNG3;/** Flag for Mother Toung language 3. */
	private  JRadioButton	rdbMTNG4;/** Flag for Mother Toung language 4. */
	private  JRadioButton	rdbMTNG5;/** Flag for Mother Toung language 5. */
// FOR pnlPAY
	private  JRadioButton	rdbBHLR;/**  */
	private  JRadioButton	rdbOWND;/**  */
	private  JRadioButton	rdbFMLY;/**  */
	private	 JButton btnCPADR;
	private  ButtonGroup btgMTNG;/**  */
	private  ButtonGroup btgACCO;/**  */
	private  cl_JTable tblDPNDT;/**  */
	private  cl_JTable tblQUAL;/**  */
	private  cl_JTable tblHSTRY;/**  */
	private  JPanel	pnlTOP;/** Panel for basic details of employee */
	private  JPanel	pnlFMLY;/** Panel for family details */
	private  JPanel	pnlEDUCN;/** Panel for Educational details */
	private  JPanel	pnlPAY;/** Panel for Salary details */
	private  JPanel	pnlHSTRY;/** Panel for Salary details */
	private  JPanel	pnlIMAGE;/** Panel for Salary details */
	private  JPanel	pnlTAB;/** Panel for adding components to tabbed pane */
	private JButton btnREMOV;
	private JLabel lblEMPCT;
	private  Hashtable<String,String> hstDEPT;
	private  Hashtable<String,String> hstEMPCT;
	private String strMDAFL="",strLTAFL="";
	private JLabel lblMDAFL,lblLTAFL;
	
	hr_meepm()
	{
		super(2);
		try
		{
			pnlTAB=new JPanel(null);pnlTOP=new JPanel(null);JPanel pnlTAB1=new JPanel(null);
			pnlPAY=new JPanel(null);pnlIMAGE=new JPanel(null);
			
			setMatrix(20,6);
			add(cmbDEPT=new JComboBox(),4,2,1,1,pnlTOP,'L');
			add(cmbDESG=new JComboBox(),3,4,1,1,pnlTOP,'L');
			add(cmbMGRD=new JComboBox(),3,5,1,0.95,pnlTOP,'L');
			cmbDEPT.addItem("Select");cmbDESG.addItem("Select");cmbMGRD.addItem("Select");
			hstDEPT=new Hashtable<String,String>();
			hstEMPCT=new Hashtable<String,String>();
			thrLAYCP=new Thread(this);
			thrLAYCP.start();
			tbpMAIN=new JTabbedPane();tbpMAIN.addChangeListener(this);
			
			String[] names=new String[]{"FL","Qualification","Year","Percentage (dd.d)","Subject","College","University"};
			int[] wid=new int[]{30,125,125,125,125,125,125};
			setMatrix(20,4);
			tblQUAL=crtTBLPNL1(pnlEDUCN=new JPanel(null),names,10,2,1,6,3.9,wid,new int[]{0}) ;
			tblQUAL.setCellEditor(1,new TxtLimit(30));
			tblQUAL.setCellEditor(2,new TxtLimit(15));
			tblQUAL.setCellEditor(3,new TxtNumLimit(3.1));
			tblQUAL.setCellEditor(4,new TxtLimit(25));
			tblQUAL.setCellEditor(5,new TxtLimit(40));
			tblQUAL.setCellEditor(6,new TxtLimit(40));
			setMatrix(20,7);
			pnlFMLY=new JPanel(null);
			add(btnREMOV=new JButton("Remove"),1,5,1,1,pnlFMLY,'L');
			tblDPNDT=crtTBLPNL1(pnlFMLY,new String[]{"FL","Name","Relation","D.O.B.","Date of Entry","Qualification","Occupation","PF Nominee","Acc Nominee","Mediclaim","Dpndt","Edn. Allownce"},12,2,1,6,6.9,new int[]{20,130,100,100,100,100,100,65,65,65,65,65},new int[]{0,11,10,9,8,7});
			setMatrix(20,4);
			tblDPNDT.clrTABLE();
			tblDPNDT.setCellEditor(1,new TxtLimit(20));
			tblDPNDT.setCellEditor(2,new TxtLimit(15));
			tblDPNDT.setCellEditor(3,new TxtDate());
			tblDPNDT.setCellEditor(5,new TxtLimit(15));
			tblDPNDT.setCellEditor(6,new TxtLimit(15));
			for(int i=0;i<tblDPNDT.cmpEDITR.length;i++)
				tblDPNDT.cmpEDITR[i].addKeyListener(this);
			for(int i=0;i<tblQUAL.cmpEDITR.length;i++)
				tblQUAL.cmpEDITR[i].addKeyListener(this);
			
		//CREATING TABLE FOR EMPLYEE HISTRY
			tblHSTRY=crtTBLPNL1(pnlHSTRY=new JPanel(null),new String[]{"FL","Year","Designation","Basic","Personal Pay","Telephone","Vehicle","Gross"},10,2,1,6,6.9,new int[]{20,100,130,100,100,100,100,100},new int[]{0});
	//POPULATING ALL COMBO BOXES FOR DEPT,GRD,LOCATION ETC.		
	//*******************///////////*********************
	//***** ADDING BASIC DETAILS OF EMPLOYEE AT TOP OF SCREEN
			setMatrix(20,6);
			setDefaultGap();
			add(new JLabel("Employee No./Name : "),1,1,1,1,pnlTOP,'L');
			add(new JLabel(" Emp No.     Initial  "),1,2,1,1,this,'R');
			add(new JLabel(" First Name  "),1,3,1,1,this,'L');
			add(new JLabel(" Middle Name  "),1,4,1,1,this,'L');
			add(new JLabel(" Last Name  "),1,5,1,1,this,'L');
			add(new JLabel(" Account Ref No.  "),1,6,1,1,this,'L');
			add(new JLabel(" SAP No.  "),3,6,1,1,this,'L');

			setMatrix(20,20);
			add(new JLabel("Category"),7,13,1,2,this,'L');
			add(txtEMPCT=new TxtLimit(20),7,15,1,1,this,'L');
			add(lblEMPCT=new JLabel(),7,16,1,2,this,'L');
			add(chkGENFL=new JCheckBox("General Shift"),7,18,1,3,this,'L');		
	
			setMatrix(20,6);
			add(txtEMPNO=new TxtNumLimit(5),1,2,1,0.5,pnlTOP,'L');
			add(txtENM=new TxtNonNumLimit(3),1,2,1,0.5,pnlTOP,'R');
			
			add(txtENM1=new TxtNonNum(),1,3,1,1,pnlTOP,'L');
			add(txtENM2=new TxtNonNum(),1,4,1,1,pnlTOP,'L');
			add(txtENM3=new TxtNonNum(),1,5,1,0.95,pnlTOP,'L');
			System.out.println("1");
			add(txtACCRF=new TxtLimit(10),2,6,1,1,this,'L');
			add(txtSAPNO=new TxtLimit(10),4,6,1,1,this,'L');
			
			System.out.println("2");
		//	add(new JLabel("Employee No. : "),2,1,1,1,pnlTOP,'L');
		//	add(txtEMPNO=new TxtNumLimit(5),2,2,1,1,pnlTOP,'L');
			add(new JLabel("Qualification	: "),2,3,1,1,pnlTOP,'L');
			add(cmbEDCAT=new JComboBox(staEDCAT_fn),2,4,1,1,pnlTOP,'L');
			add(cmbEDLVL=new JComboBox(staEDLVL_fn),2,5,1,0.95,pnlTOP,'L');
			add(new JLabel("Joining Date : "),2,1,1,1,pnlTOP,'L');
			add(txtDTJOIN=new TxtDate(),2,2,1,1,pnlTOP,'L');
			add(new JLabel("Confirmation Date	: "),3,1,1,1,pnlTOP,'L');
			add(txtCRFDT=new TxtDate(),3,2,1,1,pnlTOP,'L');
			add(new JLabel("Deptt. : "),4,1,1,1,pnlTOP,'L');
			add(cmbEPLOC=new JComboBox(new String[]{"Select"} ),3,5,1,0.95,pnlTOP,'L');
			add(new JLabel("Desg. / Grade	: "),3,3,1,1,pnlTOP,'L');
			add(new JLabel("Emp. Picture	: "),4,3,1,1,pnlTOP,'L');
			add(txtPHOTO=new JTextField(),4,4,1,1.95,pnlTOP,'L');
			add(new JLabel("PAN	: "),5,1,1,1,pnlTOP,'L');
			add(txtPANNO=new TxtLimit(15),5,2,1,1,pnlTOP,'L');
			add(new JLabel("Bank / Acct No	: "),5,3,1,1,pnlTOP,'L');
			add(txtBNKCD=new TxtLimit(5),5,4,1,0.5,pnlTOP,'L');
			add(txtACTNO=new TxtLimit(20),5,5,1,1.5,pnlTOP,'R');
			add(pnlTOP,1,1,6,5,this,'L');
			
	//***** ADDING FIRST TAB FOR CORRESPONDANCE
			add(new JLabel("Permanent Address  : "),1,2,1,1,pnlTAB,'L');
			add(new JLabel("Present Address  : "),1,5,1,1,pnlTAB,'L');
			add(new JLabel("Address 1		: "),2,1,1,1,pnlTAB,'L');
			add(txtNAMEP=new JTextField(),2,2,1,2,pnlTAB,'L');
			add(txtNAMET=new JTextField(),2,5,1,1.8,pnlTAB,'L');
			add(new JLabel("Address 2		: "),3,1,1,1,pnlTAB,'L');
			add(txtADDP1=new JTextField(),3,2,1,2,pnlTAB,'L');
			add(txtADDT1=new JTextField(),3,5,1,1.8,pnlTAB,'L');
			add(new JLabel("Address 3		: "),4,1,1,1,pnlTAB,'L');
			add(txtADDP2=new JTextField(),4,2,1,2,pnlTAB,'L');
			add(txtADDT2=new JTextField(),4,5,1,1.8,pnlTAB,'L');
			add(new JLabel("District		: "),5,1,1,1,pnlTAB,'L');
			add(txtDISTP=new JTextField(),5,2,1,2,pnlTAB,'L');
			add(txtDISTT=new JTextField(),5,5,1,1.8,pnlTAB,'L');
			add(new JLabel("State			: "),6,1,1,1,pnlTAB,'L');
			add(M_txtSTAT_pbstP=new JTextField(),6,2,1,2,pnlTAB,'L');
			add(M_txtSTAT_pbstT=new JTextField(),6,5,1,1.8,pnlTAB,'L');
			add(new JLabel("PIN				: "),7,1,1,1,pnlTAB,'L');
			add(txtPINP=new TxtNumLimit(6),7,2,1,2,pnlTAB,'L');
			add(txtPINT=new TxtNumLimit(6),7,5,1,1.8,pnlTAB,'L');
			add(new JLabel("Telephone no.	: "),8,1,1,1,pnlTAB,'L');
			add(txtTELNOP=new JTextField(),8,2,1,2,pnlTAB,'L');
			add(txtTELNOT=new JTextField(),8,5,1,1.8,pnlTAB,'L');
			add(new JLabel("E-mail			: "),9,1,1,1,pnlTAB,'L');
			add(txtEMAIL=new JTextField(),9,2,1,1.8,pnlTAB,'L');
	
			add(btnCPADR = new JButton("Copy Adress>>"),5,4,1,1,pnlTAB,'L');
			tbpMAIN.addTab("Correspondance",pnlTAB);
			
	//******ADDING TAB FOR PERSONAL DETAILS
			add(new JLabel("Date of Birth : "),1,1,1,1,pnlTAB1,'L');
			add(txtDOB=new TxtDate(),1,2,1,1,pnlTAB1,'L');
			add(new JLabel("Sex : "),1,3,1,1,pnlTAB1,'L');
			add(cmbSEX=new JComboBox(staSEXDS_fn),1,4,1,1,pnlTAB1,'L');
			add(new JLabel("Marital Status"),1,5,1,1,pnlTAB1,'L');
			add(cmbMRTL=new JComboBox(staMRTLS_fn),1,6,1,1,pnlTAB1,'L');
			add(new JLabel("Place of Birth : "),2,1,1,1,pnlTAB1,'L');
			add(txtBRTPL=new TxtLimit(15),2,2,1,1,pnlTAB1,'L');
			add(new JLabel("Religion : "),2,3,1,1,pnlTAB1,'L');
			add(txtCATA=new TxtLimit(15),2,4,1,1,pnlTAB1,'L');
			add(new JLabel("Nationality : "),2,5,1,1,pnlTAB1,'L');
			add(txtNATL=new TxtLimit(10),2,6,1,1,pnlTAB1,'L');
			add(new JLabel("LANGUAGES :"),3,1,1,1,pnlTAB1,'L');
			add(txtLANG1=new JTextField("Marathi"),3,2,1,1,pnlTAB1,'L');
			add(chbREAD1=new JCheckBox("Read"),3,3,1,1,pnlTAB1,'L');
			add(chbWRT1=new JCheckBox("Write"),3,4,1,1,pnlTAB1,'L');
			add(chbSPK1=new JCheckBox("Speak"),3,5,1,1,pnlTAB1,'L');
			btgMTNG=new ButtonGroup();btgACCO=new ButtonGroup();
			add(rdbMTNG1=new JRadioButton("Mother Tounge"),3,6,1,2,pnlTAB1,'L');
			add(txtLANG2=new JTextField("Hindi"),4,2,1,1,pnlTAB1,'L');
			add(chbREAD2=new JCheckBox("Read"),4,3,1,1,pnlTAB1,'L');
			add(chbWRT2=new JCheckBox("Write"),4,4,1,1,pnlTAB1,'L');
			add(chbSPK2=new JCheckBox("Speak"),4,5,1,1,pnlTAB1,'L');
			add(rdbMTNG2=new JRadioButton("Mother Tounge"),4,6,1,2,pnlTAB1,'L');
			add(txtLANG3=new JTextField("English"),5,2,1,1,pnlTAB1,'L');
			add(chbREAD3=new JCheckBox("Read"),5,3,1,1,pnlTAB1,'L');
			add(chbWRT3=new JCheckBox("Write"),5,4,1,1,pnlTAB1,'L');
			add(chbSPK3=new JCheckBox("Speak"),5,5,1,1,pnlTAB1,'L');
			add(rdbMTNG3=new JRadioButton("Mother Tounge"),5,6,1,2,pnlTAB1,'L');
			add(txtLANG4=new JTextField(),6,2,1,1,pnlTAB1,'L');
			add(chbREAD4=new JCheckBox("Read"),6,3,1,1,pnlTAB1,'L');
			add(chbWRT4=new JCheckBox("Write"),6,4,1,1,pnlTAB1,'L');
			add(chbSPK4=new JCheckBox("Speak"),6,5,1,1,pnlTAB1,'L');
			add(rdbMTNG4=new JRadioButton("Mother Tounge"),6,6,1,2,pnlTAB1,'L');
			add(txtLANG5=new JTextField(),7,2,1,1,pnlTAB1,'L');
			add(chbREAD5=new JCheckBox("Read"),7,3,1,1,pnlTAB1,'L');
			add(chbWRT5=new JCheckBox("Write"),7,4,1,1,pnlTAB1,'L');
			add(chbSPK5=new JCheckBox("Speak"),7,5,1,1,pnlTAB1,'L');
			add(rdbMTNG5=new JRadioButton("Mother Tounge"),7,6,1,2,pnlTAB1,'L');
			add(new JLabel("Identification Mark : "),8,1,1,1,pnlTAB1,'L');
			add(txtIDENT=new TxtLimit(30),8,2,1,3,pnlTAB1,'L');
			add(new JLabel("Physical Disability : "),9,1,1,1,pnlTAB1,'L');
			add(txtDSBLY=new TxtLimit(50),9,2,1,3,pnlTAB1,'L');
			add(new JLabel("Blood Group : "),9,5,1,1,pnlTAB1,'L');
			add(txtBLDGP=new TxtLimit(10),9,6,1,1,pnlTAB1,'L');

			add(new JLabel("References : "),10,1,1,1,pnlTAB1,'L');
			add(txtREF1=new TxtLimit(14),10,2,1,2,pnlTAB1,'L');
			add(txtREF2=new TxtLimit(14),10,5,1,2,pnlTAB1,'L');

			btgMTNG.add(rdbMTNG1);btgMTNG.add(rdbMTNG2);btgMTNG.add(rdbMTNG3);
			btgMTNG.add(rdbMTNG4);btgMTNG.add(rdbMTNG5);
			btgACCO.add(rdbFMLY);btgACCO.add(rdbOWND);btgACCO.add(rdbBHLR);
			
			tbpMAIN.addTab("Personal",pnlTAB1);
			
	//****************ADDING SALARY DETAILS
			setMatrix(22,6);
			JPanel ptemp=new JPanel(null);
			ptemp.setBorder(BorderFactory.createTitledBorder("   Grade-Wise Salary   "));
			
			add(new JLabel("D.A. : "),1,1,1,1,ptemp,'L');
			add(txtDNALW=new TxtNumLimit(8.2),1,2,1,0.5,ptemp,'L');
			add(new JLabel("Lunch Subsidy : "),1,3,1,1,ptemp,'L');
			add(txtLNSUB=new TxtNumLimit(8.2),1,4,1,0.5,ptemp,'L');
			add(new JLabel("H.R.A. % : "),1,5,1,1,ptemp,'L');
			add(txtHRALW=new TxtNumLimit(7.2),1,6,1,0.5,ptemp,'L');	
	
			add(new JLabel("Special Allowance : "),2,1,1,1,ptemp,'L');
			add(txtSPALW=new TxtNumLimit(8.2),2,2,1,0.5,ptemp,'L');
			add(new JLabel("Medical : "),2,3,1,0.5,ptemp,'L');
			add(lblMDAFL=new JLabel(""),2,3,1,0.5,ptemp,'R');
			add(txtMDALW=new TxtNumLimit(8.2),2,4,1,0.5,ptemp,'L');
						
			add(new JLabel("L.T.A. : "),2,5,1,0.5,ptemp,'L');
			add(lblLTAFL=new JLabel(),2,5,1,0.5,ptemp,'R');
			add(txtLTALW=new TxtNumLimit(8.3),2,6,1,0.5,ptemp,'L');
						
			add(new JLabel("Work Allowance : "),3,1,1,1,ptemp,'L');
			add(txtWKALW=new TxtNumLimit(8.2),3,2,1,0.5,ptemp,'L');
			add(new JLabel("Conveyance : "),3,3,1,1,ptemp,'L');
			add(txtCONVY=new TxtNumLimit(8.2),3,4,1,0.5,ptemp,'L');
			add(new JLabel("P.F. Contribuation % : "),3,5,1,1,ptemp,'L');
			add(txtPFALW=new TxtNumLimit(8.2),3,6,1,0.5,ptemp,'L');
			
			add(new JLabel("Child Edu. Allowance : "),4,1,1,1,ptemp,'L');
			add(txtCHEDN=new TxtNumLimit(7.2),4,2,1,0.5,ptemp,'L');
			add(new JLabel("Washing Allowance : "),4,3,1,1,ptemp,'L');
			add(txtWSALW=new TxtNumLimit(7.2),4,4,1,0.5,ptemp,'L');
			add(new JLabel("Super Annuation % : "),4,5,1,1,ptemp,'L');
			add(txtSAALW=new TxtNumLimit(7.2),4,6,1,0.5,ptemp,'L');
	
			add(new JLabel("Car Allowance     : "),5,1,1,1,ptemp,'L');
			add(txtCRALW=new TxtNumLimit(7.2),5,2,1,0.5,ptemp,'L');
			add(new JLabel("Grade Allowance : "),5,3,1,1,ptemp,'L');
			add(txtGRALW=new TxtNumLimit(7.2),5,4,1,0.5,ptemp,'L');
			
			add(new JLabel("Basic : "),1,1,1,1,pnlPAY,'L');
			add(txtBASIC=new TxtNumLimit(7.2),1,2,1,1,pnlPAY,'L');
			add(chbTELEN=new JCheckBox("Telephone Encash"),1,3,1,1,pnlPAY,'L');
			add(txtTELEN=new TxtNumLimit(7.2),1,4,1,1,pnlPAY,'L');
			add(new JLabel("Personal Pay : "),2,1,1,1,pnlPAY,'L');
			add(txtPPAY=new TxtNumLimit(7.2),2,2,1,1,pnlPAY,'L');
			add(chbVCLMT=new JCheckBox("Vehicle Maint."),2,3,1,1,pnlPAY,'L');
			add(txtVCLMT=new TxtNumLimit(7.2),2,4,1,1,pnlPAY,'L');
			add(new JLabel("Type of Accomodation"),3,1,1,1,pnlPAY,'L');
			add(rdbBHLR=new JRadioButton("Bachlor"),3,2,1,1,pnlPAY,'L');
			add(rdbFMLY=new JRadioButton("Family"),3,3,1,1,pnlPAY,'L');
			add(rdbOWND=new JRadioButton("Own"),3,4,1,1,pnlPAY,'L');
			add(ptemp,4,1,7,6,pnlPAY,'L');
			add(new JLabel("Gross Salary : "),11,1,1,1,pnlPAY,'L');
			add(txtGROS=new TxtNumLimit(7.2),11,2,1,1,pnlPAY,'L');
			add(new JLabel("Payment Mode : "),11,3,1,1,pnlPAY,'L');
			add(cmbPAYMD=new JComboBox(),11,4,1,1,pnlPAY,'L');
			
			tbpMAIN.addTab("Salary",pnlPAY);
			
	//*************ADDING FAMILY DETAILS
			add(new JLabel("DEPENDENTS : "),1,1,1,1,pnlFMLY,'L');
			tbpMAIN.addTab("Dependents",pnlFMLY);
			
	//**************** ADDING EDUCATION DETAILS
			add(new JLabel("EDUCATIONAL DETAILS : "),1,1,1,1,pnlEDUCN,'L');
			tbpMAIN.addTab("Qualification",pnlEDUCN);
	//**************** ADDING HISTRY DETAILS
			add(new JLabel("HISTORY : "),1,1,1,1,pnlHSTRY,'L');
			tbpMAIN.addTab("History",pnlHSTRY);
			add(tbpMAIN,8,1,13,6,this,'L');

			if(thrLAYCP!=null)
				thrLAYCP.join();
			btgMTNG=new ButtonGroup();btgACCO=new ButtonGroup();
			btgMTNG.add(rdbMTNG1);btgMTNG.add(rdbMTNG2);btgMTNG.add(rdbMTNG3);
			btgMTNG.add(rdbMTNG4);btgMTNG.add(rdbMTNG5);
			btgACCO.add(rdbFMLY);btgACCO.add(rdbOWND);btgACCO.add(rdbBHLR);
			setMatrix(20,6);
			cmbPAYMD.addItem("Select");
			cmbPAYMD.addItem("Cash");
			cmbPAYMD.addItem("Cheque");
			cmbPAYMD.addItem("Voucher");
//			cmbPAYMD.setSelectedIndex(2);
            updNAME();
			INPVF oINPVF=new INPVF();
			txtEMPCT.setInputVerifier(oINPVF);
			txtACCRF.setInputVerifier(oINPVF);
			txtSAPNO.setInputVerifier(oINPVF);
		}catch(Exception e)
		{setMSG(e,"child.constructor");}
	}
	public void run()
	{
		try
		{
			super.run();
			if(Thread.currentThread()==thrLAYCP)
			{
				String st1="COXXDPT",st2="COXXDIV";
				M_strSQLQRY="SELECT  CMT_SHRDS,CMT_CGSTP,CMT_CODCD from co_cdTRN  WHERE CMT_CGSTP='COXXDSG' or (CMT_CGSTP='COXXDPT' and CMT_CGMTP='SYS') order by CMT_CODCD";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString(2).equals(st1))
					{	
						cmbDEPT.addItem(M_rstRSSET.getString(1));
						hstDEPT.put(M_rstRSSET.getString(1),M_rstRSSET.getString(3));
					}
					else
						cmbDESG.addItem(M_rstRSSET.getString(1));
				}
				M_rstRSSET.close();
				M_strSQLQRY="select cmt_codcd from co_cdtrn where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXGRD'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				while(M_rstRSSET.next())
					cmbMGRD.addItem(M_rstRSSET.getString(1));

        		hstEMPCT.clear();
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
        		M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXEPC' order by cmt_codcd";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				while(M_rstRSSET.next())
					hstEMPCT.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				M_rstRSSET.close();
			}
		}catch(Exception e)
		{
			setMSG(e,"Child.Run");
			setMSG("Error in Fectching Grade/Desgn/Educn deatails ..",'E');
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			String L_ACTCMD = L_AE.getActionCommand();
			if(txtEMPCT.getText().length()==0)
				lblEMPCT.setText("");
			if(M_objSOURC == cmbMGRD)
			{
				String strSQLQRY = "select cmt_ccsvl from co_cdtrn where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXGRD' and cmt_codcd='"+cmbMGRD.getSelectedItem().toString().trim()+"'";
			    java.sql.ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println("strSQLQRY>>"+strSQLQRY);
				if(rstRSSET!=null && rstRSSET.next())
				{
				 	txtEMPCT.setText(rstRSSET.getString("cmt_ccsvl"));
				 	if(hstEMPCT.containsKey(rstRSSET.getString("cmt_ccsvl")))
				 		lblEMPCT.setText(hstEMPCT.get(rstRSSET.getString("cmt_ccsvl")));
				}
				rstRSSET.close();
				// Get the Gradewise salary
			    if(txtEMPNO.getText().length() >0)
			    {
				   getSAL();
			       if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
			       {
					   double	ltalw,mdalw;
			       	// CALCULATING GROSS SALARY PER ANNUM (ALSO REFERED AS COST TO COMPANY(C 2 C) BY HRD
			        double	basic=Double.parseDouble(nvlSTRVL(txtBASIC.getText(),"0")) *12;
			        double	convy=Double.parseDouble(nvlSTRVL(txtCONVY.getText(),"0")) *12;
			        double	hralw=Double.parseDouble(nvlSTRVL(txtHRALW.getText(),"0")) *(basic/100);
			        double	spalw=Double.parseDouble(nvlSTRVL(txtSPALW.getText(),"0")) *12;
			        double	dnalw=Double.parseDouble(nvlSTRVL(txtDNALW.getText(),"0")) *12;
			        double	pfalw=Double.parseDouble(nvlSTRVL(txtPFALW.getText(),"0")) *(basic/100);
			        double	saalw=Double.parseDouble(nvlSTRVL(txtSAALW.getText(),"0")) *(basic/100);
			        double	wsalw=Double.parseDouble(nvlSTRVL(txtWSALW.getText(),"0")) *12;
			        double	gralw=Double.parseDouble(nvlSTRVL(txtGRALW.getText(),"0")) *12;
			        double	cralw=Double.parseDouble(nvlSTRVL(txtCRALW.getText(),"0")) *12;
			        double	wkalw=Double.parseDouble(nvlSTRVL(txtWKALW.getText(),"0")) *12;
			        //double	ltalw=Double.parseDouble(nvlSTRVL(txtLTALW.getText(),"0")) *(basic/1200);
					if(strLTAFL.equals("A"))
					{	
						ltalw=Double.parseDouble(nvlSTRVL(txtLTALW.getText(),"0")) *12;
						lblLTAFL.setText("");
					}	
					else
					{	
						lblLTAFL.setText("%");
						ltalw=Double.parseDouble(nvlSTRVL(txtLTALW.getText(),"0")) *(basic/100);
					}	
					
					double	lnsub=Double.parseDouble(nvlSTRVL(txtLNSUB.getText(),"0")) *12;
			        double	ppay=Double.parseDouble(nvlSTRVL(txtPPAY.getText(),"0")) *12;
			        if(strMDAFL.equals("P"))
					{	
						lblMDAFL.setText("%");
						mdalw=Double.parseDouble(nvlSTRVL(txtMDALW.getText(),"0")) *(basic/100);
					}	
					else	
					{	
						mdalw=Double.parseDouble(nvlSTRVL(txtMDALW.getText(),"0")) *12;
						lblMDAFL.setText("");						
					}	
					double	chedn=Double.parseDouble(nvlSTRVL(txtCHEDN.getText(),"0")) *12;
					
					/*String L_strGROS = "";
					L_strGROS += " Basic        = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtBASIC.getText(),"0"))*12,2)+"\n";
					L_strGROS += " Conveyance   = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtCONVY.getText(),"0")) *12,2)+"\n";
					L_strGROS += " HRA          = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtHRALW.getText(),"0")) *(basic/100),2)+"\n";
					L_strGROS += " Special All. = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtSPALW.getText(),"0")) *12,2)+"\n";
					L_strGROS += " Dearness Al. = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtDNALW.getText(),"0")) *12,2)+"\n";
					L_strGROS += " PF           = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtPFALW.getText(),"0")) *(basic/100),2)+"\n";
					L_strGROS += " Sup.Annu.    = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtSAALW.getText(),"0")) *(basic/100),2)+"\n";
                    L_strGROS += " Washing      = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtWSALW.getText(),"0")) *12,2)+"\n";
                    L_strGROS += " Grade All.   = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtGRALW.getText(),"0")) *12,2)+"\n";
                    L_strGROS += " Car All.     = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtCRALW.getText(),"0")) *12,2)+"\n";
					L_strGROS += " Work Allow.  = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtWKALW.getText(),"0")) *12,2)+"\n";
					//L_strGROS += " L.T.A.       = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtLTALW.getText(),"0")) *(basic/1200),2)+"\n";
					L_strGROS += " L.T.A.       = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtLTALW.getText(),"0")) *(basic/100),2)+"\n";
					L_strGROS += " Lunch Subs.  = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtLNSUB.getText(),"0")) *12,2)+"\n";
					L_strGROS += " Pers.Pay     = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtPPAY.getText(),"0")) *12,2)+"\n";
					L_strGROS += " Medical      = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtMDALW.getText(),"0")) *12,2)+"\n";
					L_strGROS += " Child Allow  = "+setNumberFormat(Double.parseDouble(nvlSTRVL(txtCHEDN.getText(),"0")) *12,2)+"\n";
					
					JOptionPane.showMessageDialog(pnlPAY,L_strGROS,"Salary Calculation",JOptionPane.INFORMATION_MESSAGE);
					*/
					
			        txtGROS.setText(setNumberFormat(ppay+basic+convy+hralw+dnalw+spalw+pfalw+saalw+lnsub+wkalw+mdalw+ltalw+wsalw+chedn+gralw+gralw,0));
			       }
			    }
			}
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				cmbEPLOC.removeAllItems();
				lblLTAFL.setText("");
				lblMDAFL.setText("");
				if(cl_dat.M_cmbSBSL1_pbst.getSelectedIndex()==1)
				{
					strLCFLT=" EP_EPLOC='WORKS' ";
					cmbEPLOC.addItem("WORKS");
				}
				else if(cl_dat.M_cmbSBSL1_pbst.getSelectedIndex()==2)
				{
					strLCFLT=" EP_EPLOC='H.O.' ";
					cmbEPLOC.addItem("H.O.");
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					txtENM.requestFocus();
					txtENM.setText("");
					txtLANG1.setText("Marathi");
					txtLANG2.setText("Hindi");
					txtLANG3.setText("English");
					chbREAD1.setSelected(true);
					chbWRT1.setSelected(true);
					chbSPK1.setSelected(true);
					rdbMTNG1.setSelected(true);
					chbREAD2.setSelected(true);
					chbWRT2.setSelected(true);
					chbSPK2.setSelected(true);
					chbREAD3.setSelected(true);
					chbWRT3.setSelected(true);
					chbSPK3.setSelected(true);
					txtNATL.setText("Indian");
					
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
				{
					txtEMPNO.setEnabled(true);
					txtEMPNO.requestFocus();
					txtEMPNO.setText("");
				}
			    else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst))
				{
					txtEMPNO.setEnabled(true);
					btnREMOV.setEnabled(true);
					txtEMPNO.requestFocus();
					txtEMPNO.setText("");
				}
				else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPENQ_pbst))
				{
					txtEMPNO.setEnabled(true);
					txtEMPNO.setEnabled(true);
					txtENM.setEnabled(true);
					txtEMPNO.requestFocus();
					txtEMPNO.setText("");
				}
			}
			else if(L_AE.getSource()==txtEMPNO||L_AE.getSource()==txtENM)
			{
				if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
				{
					cl_dat.M_flgLCUPD_pbst = true;
					if(txtEMPNO.getText().length()!=0)
						M_strSQLQRY = "SELECT max(ep_empno) from HR_EPMST WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO LIKE '"+txtEMPNO.getText()+"%'";
					else
						M_strSQLQRY = "SELECT max(ep_empno) from HR_EPMST Where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET.next())
					{
						String L_STRTMP=M_rstRSSET.getString(1);
						if(L_STRTMP.substring(L_STRTMP.length()-2,L_STRTMP.length()).equals("99"))
							setMSG("Series is Exausted ..",'E');
						else
							txtEMPNO.setText(Integer.toString((Integer.parseInt(L_STRTMP)+1)));
					}
				}
				else 
				{
					if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst))
						txtEMPNO.setEnabled(false);
					flgEDUCN=flgFMLY=flgHSTRY=true;
					if(L_AE.getSource()==txtEMPNO)
						exeSELREC("EMPNO");
					else
						exeSELREC("ENM");
					if(tbpMAIN.getSelectedIndex()==3)
						selFL();
					else if(tbpMAIN.getSelectedIndex()==4)
						selQL();
				}
			}
			else if(L_AE.getSource()==btnREMOV)
			{
				delFLMST();
			}
			else if(L_AE.getSource()==btnCPADR)
			{
				txtNAMET.setText(txtNAMEP.getText());
				txtADDT1.setText(txtADDP1.getText());
				txtADDT2.setText(txtADDP2.getText());
				txtDISTT.setText(txtDISTP.getText());
				M_txtSTAT_pbstT.setText(M_txtSTAT_pbstP.getText());
				txtPINT.setText(txtPINP.getText());
				txtTELNOT.setText(txtTELNOP.getText());
			}	
		}catch(Exception e)
		{
			setMSG(e,"Child.actionPerformed");
			setMSG("Series not yet used ..",'E');
		}
	}

	void exeSAVE()
	{
		if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst))
		{
			exeMODREC();
			txtEMPNO.setEnabled(true);
		}
		else if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
			exeADDREC();
	}
	public void keyTyped(KeyEvent L_KE){}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try{
		int key=L_KE.getKeyCode();
		Object L_SRC=L_KE.getSource();
		if (key==L_KE.VK_F1)
		{
			if(M_objSOURC == txtPHOTO)
			{
				JFileChooser fchFILES=new JFileChooser("\\\\192.168.10.207\\user\\exec\\image\\hrms");
				fchFILES.addActionListener(this);
				int i=fchFILES.showOpenDialog(this);
				if(i == 0)
					txtPHOTO.setText(fchFILES.getSelectedFile().toString());
			}
	       	if(M_objSOURC==txtEMPCT)
        	{
        		cl_dat.M_flgHELPFL_pbst = true;
        		M_strHLPFLD = "txtEMPCT";
        		String L_ARRHDR[] = {"Code","Category"};
        		M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
        		M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXEPC' order by cmt_codcd";
        		cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
	       	}
			if(L_KE.getSource()==txtEMPNO)
			{
			    M_strSQLQRY="SELECT EP_EMPNO,EP_FULNM FROM HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_HRSBS='"+M_strSBSCD+"' ORDER BY EP_EMPNO";
			    M_strHLPFLD = "txtEMPNO";
			    cl_hlp(M_strSQLQRY ,2,1,new String[] {"Employee no.","Name"},2,"CT");
			}
			if(L_KE.getSource()==txtENM)
			{
			    M_strSQLQRY="SELECT EP_SHRNM,EP_EMPNO,EP_FULNM FROM HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_HRSBS='"+M_strSBSCD+"'";
			    if(txtENM.getText().length() >0)
			    M_strSQLQRY += " AND EP_SHRNM like '"+txtENM.getText().trim() +"%'";
			    M_strSQLQRY += "ORDER BY EP_SHRNM";
			    M_strHLPFLD = "txtENM";
			    cl_hlp(M_strSQLQRY ,1,2,new String[] {"Initial", "Employee no.","Name"},3,"CT");
			}
		}
	//TO CHANGE SELECTED TAB OF TABBED PANE.
		else if(L_KE.isControlDown()&&!L_SRC.equals(tbpMAIN))
		{
			if(key==L_KE.VK_PAGE_DOWN)
			{
				int tab=tbpMAIN.getSelectedIndex();
				if(tab!=tbpMAIN.getTabCount()-1)
					tbpMAIN.setSelectedIndex(tbpMAIN.getSelectedIndex()+1);
			}
			else if(key==L_KE.VK_PAGE_UP)
			{
				int tab=tbpMAIN.getSelectedIndex();
				if(tab!=0)
					tbpMAIN.setSelectedIndex(tbpMAIN.getSelectedIndex()-1);
			}
			else if(key==L_KE.VK_HOME)
				tbpMAIN.setSelectedIndex(0);
			else if(key==L_KE.VK_END)
				tbpMAIN.setSelectedIndex(tbpMAIN.getTabCount()-1);
		}
		else if (key==L_KE.VK_ENTER)
		{
			if(L_SRC==txtNAMEP)
				txtADDP1.requestFocus();
			else if(L_SRC==txtADDP1)
				txtADDP2.requestFocus();
			else if(L_SRC==txtADDP2)
				txtDISTP.requestFocus();
			else if(L_SRC==txtDISTP)
				M_txtSTAT_pbstP.requestFocus();
			else if(L_SRC==txtNAMET)
				txtADDT1.requestFocus();
			else if(L_SRC==M_txtSTAT_pbstP)
				txtPINP.requestFocus();
			else if(L_SRC==txtTELNOP)
				txtEMAIL.requestFocus();
			else if(L_SRC==txtEMAIL)
				txtNAMET.requestFocus();
			else if(L_SRC==txtPINP)
				txtTELNOP.requestFocus();
			else if(L_SRC==txtNAMET)
				txtADDT1.requestFocus();
			else if(L_SRC==txtADDT1)
				txtADDT2.requestFocus();
			else if(L_SRC==txtADDT2)
				txtDISTT.requestFocus();
			else if(L_SRC==txtTELNOT)
			{
				tbpMAIN.setSelectedIndex(1);
				txtDOB.requestFocus();
			}
			else if(L_SRC==txtDISTT)
				M_txtSTAT_pbstT.requestFocus();
			else if(L_SRC==M_txtSTAT_pbstT)
				txtPINT.requestFocus();
			else if(L_SRC==txtPINT)
				txtTELNOT.requestFocus();
			else if(L_SRC==txtREF2)
			{
				tbpMAIN.setSelectedIndex(2);
				txtBASIC.requestFocus();
			}
			else if(L_SRC==txtGROS)
			{
				tbpMAIN.setSelectedIndex(3);
				tblDPNDT.requestFocus();
			}
			else
				L_KE.getComponent().transferFocus();
		}
		else if(L_SRC==tblDPNDT.cmpEDITR[1])
		{
			int row=tblDPNDT.getEditingRow();
			int col=tblDPNDT.getEditingColumn();
			if(!(col==-1&&row==-1))
			{
				tblDPNDT.setValueAt(M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)),row,4);
				if(flaODLFL[row])
					tblDPNDT.cmpEDITR[col].setEnabled(false);
				else
					tblDPNDT.cmpEDITR[col].setEnabled(true);
			}
		}
		else if(L_KE.getSource().equals(tblQUAL.cmpEDITR[1]))
		{
			int row=tblQUAL.getEditingRow();
			 row=tblQUAL.getSelectedRow();
			int col=tblQUAL.getSelectedColumn();
			if(!(col==-1&&row==-1))
			{
				if(flaODLQL[row])
					tblQUAL.cmpEDITR[1].setEnabled(false);
				else
					tblQUAL.cmpEDITR[1].setEnabled(true);
			}
		}
		}catch(Exception e)
		{setMSG(e,"child.keyreleased");}
	}
	public void itemStateChanged(ItemEvent L_IE)
	{
		super.itemStateChanged(L_IE);
		
		if(L_IE.getSource()==chbTELEN)
			txtTELEN.setVisible(chbTELEN.isSelected());
		else if(L_IE.getSource()==chbVCLMT)
			txtVCLMT.setVisible(chbVCLMT.isSelected());
	}
	/** Selects data from HR_QLMST	 */
	private void selQL()
	{
		if(flgEDUCN)
			{
				try
				{
					M_strSQLQRY = "SELECT * from HR_QLMST where QL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QL_EMPNO ='"+txtEMPNO.getText().trim() +"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET !=null)
					{	
						int i=0;
						while(M_rstRSSET.next())
						{
							tblQUAL.setValueAt(M_rstRSSET.getString("QL_CATGR"),i,1);
							tblQUAL.setValueAt(M_rstRSSET.getString("QL_PSGYR"),i,2);
							tblQUAL.setValueAt(M_rstRSSET.getString("QL_CATDS"),i,4);
							tblQUAL.setValueAt(M_rstRSSET.getString("QL_COLLG"),i,5);
							tblQUAL.setValueAt(M_rstRSSET.getString("QL_UNIVT"),i,6);
							tblQUAL.setValueAt(M_rstRSSET.getString("QL_PERCT"),i,3);
							flaODLQL[i]=true;
							i++;
						}
					}
					flgEDUCN=false;
				}catch(Exception e)
				{setMSG(e,"Child.selQL");}
			}
			else 
				flgEDUCN=false;
	}
	/** Selects data from HR_FLMST (Family Master) and putsin resp table	 */
	private void selFL()
	{
		if(flgFMLY)
			{
				try
				{
					M_strSQLQRY = "SELECT * from HR_FLMST where FL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND FL_EMPNO ='"+txtEMPNO.getText().trim() +"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET !=null)
					{	
						int i=0;
						while(M_rstRSSET.next())
						{
							tblDPNDT.setValueAt(M_rstRSSET.getString("FL_MEMNM"),i,1);
							//System.out.println(M_rstRSSET.getString("FL_MEMNM"));
							//System.out.println(M_rstRSSET.getDate("FL_BTHDT"));
							if(M_rstRSSET.getDate("FL_BTHDT")!=null)
							{
								tblDPNDT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("FL_BTHDT")),i,3);
								//System.out.println(M_fmtLCDAT.format(M_rstRSSET.getDate("FL_BTHDT")));

							}
							tblDPNDT.setValueAt(M_rstRSSET.getString("FL_RELDS"),i,2);
							if(M_rstRSSET.getString("FL_MODDT")!=null)
								tblDPNDT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("FL_MODDT")),i,4);
							tblDPNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("FL_QUALN"),""),i,5);
							tblDPNDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("FL_OCCPN"),""),i,6);
							tblDPNDT.setValueAt((M_rstRSSET.getString("FL_PFNFL").equals("Y")?new Boolean(true):new Boolean(false)),i,7);
							tblDPNDT.setValueAt((M_rstRSSET.getString("FL_ACNFL").equals("Y")?new Boolean(true):new Boolean(false)),i,8);
							tblDPNDT.setValueAt((M_rstRSSET.getString("FL_MDCFL").equals("Y")?new Boolean(true):new Boolean(false)),i,9);
							tblDPNDT.setValueAt((M_rstRSSET.getString("FL_DPDFL").equals("Y")?new Boolean(true):new Boolean(false)),i,10);
							tblDPNDT.setValueAt((M_rstRSSET.getString("FL_CLDFL").equals("Y")?new Boolean(true):new Boolean(false)),i,11);
							flaODLFL[i]=true;
							i++;
						}
						flgFMLY=false;
					}
				}catch(Exception e)
				{setMSG(e,"Child.selFL");}
			}
			else 
				flgFMLY=false;
	}
	/** Selects data from HR_FLMST (Family Master) and putsin resp table	 */
	private void selHSTRY()
	{
		if(flgHSTRY)
			{
				try
				{
					M_strSQLQRY = "SELECT max(EP_LUPDT),EP_YRDGT,EP_DESGN,EP_BASAL,EP_PPSAL,EP_TELEN,EP_VCLMT,EP_GRSAL from HR_EPMAM where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO ='"+txtEMPNO.getText().trim() +"' group by EP_YRDGT,EP_DESGN,EP_BASAL,EP_PPSAL,EP_TELEN,EP_VCLMT,EP_GRSAL order by EP_YRDGT desc ";
					M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY );
					if(M_rstRSSET !=null)
					{	
						int i=0;
						while(M_rstRSSET.next())
						{
							tblHSTRY.setValueAt(M_rstRSSET.getString("EP_YRDGT"),i,1);
							tblHSTRY.setValueAt(M_rstRSSET.getString("EP_DESGN"),i,2);
							tblHSTRY.setValueAt(M_rstRSSET.getString("EP_BASAL"),i,3);
							tblHSTRY.setValueAt(M_rstRSSET.getString("EP_PPSAL"),i,4);
							tblHSTRY.setValueAt(M_rstRSSET.getString("EP_TELEN"),i,5);
							tblHSTRY.setValueAt(M_rstRSSET.getString("EP_VCLMT"),i,6);
							tblHSTRY.setValueAt(M_rstRSSET.getString("EP_GRSAL"),i,7);
							i++;
						}
						flgHSTRY=false;
					}
				}catch(Exception e)
				{setMSG(e,"Child.selHSTRY");}
			}
			else 
				flgHSTRY=false;
	}
	/** For tabbed pan selection	 */
	public void stateChanged(ChangeEvent L_CE)//FOR TABBED PAN SELECTION
	{
		if(tbpMAIN.getSelectedIndex()==3)
			selFL();
		else if(tbpMAIN.getSelectedIndex()==4)
			selQL();
		else if(tbpMAIN.getSelectedIndex()==5)
			selHSTRY();
		else if(tbpMAIN.getSelectedIndex()==2&&cmbMGRD.getSelectedIndex()==0)
		{
			setMSG("Please select Management Grade First ..",'E');
			tbpMAIN.setSelectedIndex(0);
		}
	}
	
/** Validate data entered by user, Format all text and calculate salary */
	private  boolean vldEPMST()
	{
		try
		{
			if(txtEMPNO.getText().length()<4)
				{
					setMSG("Employee No. should be min. 4 digits",'E');
					txtEMPNO.requestFocus();
					return false;
				}
			else if (txtENM.getText().trim().length()<1)
			{
				setMSG("Employee Name in short is cumpulsury ..",'E');
				txtENM.requestFocus();
				return false;
			}
			else if (cmbDEPT.getSelectedItem().equals(staDPTDS_fn[0]))
			{
				setMSG("Pl. select Department ..",'E');
				cmbDEPT.requestFocus();
				return false;
			}
			else if (cmbDESG.getSelectedItem().equals(staDESGN_fn[0]))
			{
				setMSG("Pl. select Designation ..",'E');
				cmbDESG.requestFocus();
				return false;
			}
			else if ((cmbEDCAT.getSelectedItem().equals(staEDCAT_fn[0]))||(cmbEDLVL.getSelectedItem().equals(staEDLVL_fn[0])))
			{
				setMSG("Pl. select Education ..",'E');
				cmbEDCAT.requestFocus();
				return false;
			}
			else if (txtEMPCT.getText().trim().length()<1)
			{
				setMSG("Pl. select Employee Category ..",'E');
				txtEMPCT.requestFocus();
				return false;
			}
			else if ((cmbMRTL.getSelectedItem().equals(staMRTLS_fn[0]))||(cmbSEX.getSelectedItem().equals(staSEXDS_fn[0])))
			{
				setMSG("Pl. select Marital status & Gender ..",'E');
				cmbMRTL.requestFocus();
				tbpMAIN.setSelectedIndex(1);
				return false;
			}
			else if(!(chbSPK1.isSelected()||chbSPK2.isSelected()||chbSPK3.isSelected()||chbSPK4.isSelected()||chbSPK5.isSelected()))
			{
				setMSG("Pl. enter Language details ..",'E');
				tbpMAIN.setSelectedIndex(1);
				return false;
			}
			else if (txtBASIC.getText().length()==0&&txtHRALW.getText().length()==0&&txtDNALW.getText().length()==0&&
					 txtPPAY.getText().length()==0&&txtSPALW.getText().length()==0&&txtCONVY.getText().length()==0&&
					 txtGROS.getText().length()==0)
				
			{
				setMSG("Pl. enter Salary details ..",'E');
				tbpMAIN.setSelectedIndex(2);
				txtBASIC.requestFocus();
				return false;
			}
			if (txtADDP1.getText().trim().length()<1&&txtADDT1.getText().trim().length()<1)
			{
				setMSG("Pl. enter Address for correspondance ..",'E');
				tbpMAIN.setSelectedIndex(0);
				txtNAMET.requestFocus();
				return false;
			}
			if (txtDTJOIN.vldDATE()!=null)
			{
				setMSG(txtDTJOIN.vldDATE(),'E');
				txtDTJOIN.requestFocus();
				return false;
			}
			if (chbTELEN.isSelected()&&txtTELEN.getText()=="")
			{
				setMSG("Please Enter Limit for Telephont Encashment ..",'E');
				tbpMAIN.setSelectedIndex(2);
				txtTELEN.requestFocus();
				return false;
			}
			if (chbVCLMT.isSelected()&&txtVCLMT.getText()=="")
			{
				setMSG("Please Enter Limit for Vehicle Maintainance ..",'E');
				tbpMAIN.setSelectedIndex(2);
				txtVCLMT.requestFocus();
				return false;
			}
			String adp=(txtNAMEP.getText()+" |"+nvlSTRVL(txtADDP1.getText(),"  ")+" |"+nvlSTRVL(txtADDP2.getText(),"  ")+" |"+nvlSTRVL(txtDISTP.getText(),"  ")+" |"+nvlSTRVL(M_txtSTAT_pbstP.getText(),"  ")+" |"+nvlSTRVL(txtPINP.getText(),"  ")+" |"+nvlSTRVL(txtTELNOP.getText(),"  ")+" |"+nvlSTRVL(txtEMAIL.getText(),"  ")),
				adt= (nvlSTRVL(txtNAMET.getText(),"  ")+" |"+nvlSTRVL(txtADDT1.getText(),"  ")+" |"+nvlSTRVL(txtADDT2.getText(),"  ")+" |"+nvlSTRVL(txtDISTT.getText(),"  ")+" |"+nvlSTRVL(M_txtSTAT_pbstT.getText(),"  ")+" |"+nvlSTRVL(txtPINT.getText(),"  ")+" |"+nvlSTRVL(txtTELNOT.getText(),"  "));
			if(adp.length()>200)
			{
				setMSG("Permanent address is too long (by "+(adp.length()-200)+" characters) .. ",'E');
				tbpMAIN.setSelectedIndex(0);
				txtNAMEP.requestFocus();
				return false;
			}
			if(adt.length()>200)
			{
				setMSG("Present address is too long (by "+(adp.length()-200)+" characters) .. ",'E');
				tbpMAIN.setSelectedIndex(0);
				txtNAMET.requestFocus();
				return false;
			}
			if(txtDTJOIN.getText().length()>0 && txtCRFDT.getText().length()>0)
				if(M_fmtLCDAT.parse(txtDTJOIN.getText()).compareTo(M_fmtLCDAT.parse(txtCRFDT.getText()))>0)
				{
					setMSG("Confirmation date cannot be smaller than Joining Date ..",'E');
					txtCRFDT.requestFocus();
					return false;
				}
				
	//FORMATTING TEXT/*
			String text=txtENM1.getText();
			if(text.length()>2)
				txtENM1.setText(text.substring(0,1).toUpperCase()+text.substring(1,text.length()).toLowerCase());
			else 
				txtENM1.setText(text.toUpperCase());
			text=txtENM2.getText();
			if(text.length()>2)
				txtENM2.setText(text.substring(0,1).toUpperCase()+text.substring(1,text.length()).toLowerCase());
			else 
				txtENM2.setText(text.toUpperCase());
			text=txtENM3.getText();
			if(text.length()>2)
				txtENM3.setText(text.substring(0,1).toUpperCase()+text.substring(1,text.length()).toLowerCase());
			else 
				txtENM3.setText(text.toUpperCase());
			txtENM.setText(txtENM.getText().toUpperCase());
	//Getting Kid count from tblDPNDT
			Boolean b=new Boolean(true);
			intEDCNT=0;
			for(int i=0;i<tblDPNDT.getRowCount();i++)
			{
				if(tblDPNDT.getValueAt(i,0).equals(b))
					if(tblDPNDT.getValueAt(i,11).equals(b))
					   intEDCNT++;
			}
	// CALCULATING GROSS SALARY PER ANNUM (ALSO REFERED AS COST TO COMPANY(C 2 C) BY HRD
			double
						basic=Double.parseDouble(nvlSTRVL(txtBASIC.getText(),"0")) *12,
						convy=Double.parseDouble(nvlSTRVL(txtCONVY.getText(),"0")) *12,
						hralw=Double.parseDouble(nvlSTRVL(txtHRALW.getText(),"0")) *(basic/100),
						spalw=Double.parseDouble(nvlSTRVL(txtSPALW.getText(),"0")) *12,
						dnalw=Double.parseDouble(nvlSTRVL(txtDNALW.getText(),"0")) *12,
						pfalw=Double.parseDouble(nvlSTRVL(txtPFALW.getText(),"0")) *(basic/100),
						saalw=Double.parseDouble(nvlSTRVL(txtSAALW.getText(),"0")) *(basic/100),
                        wsalw=Double.parseDouble(nvlSTRVL(txtWSALW.getText(),"0")) *12,
                        grsalw=Double.parseDouble(nvlSTRVL(txtGRALW.getText(),"0")) *12,
                        cralw=Double.parseDouble(nvlSTRVL(txtCRALW.getText(),"0")) *12,
						wkalw=Double.parseDouble(nvlSTRVL(txtWKALW.getText(),"0")) *12,
						ltalw=Double.parseDouble(nvlSTRVL(txtLTALW.getText(),"0")) *(basic/1200),
						lnsub=Double.parseDouble(nvlSTRVL(txtLNSUB.getText(),"0")) *12,
						ppay=Double.parseDouble(nvlSTRVL(txtPPAY.getText(),"0")) *12,
						mdalw=Double.parseDouble(nvlSTRVL(txtMDALW.getText(),"0")) *12;
			txtGROS.setText(Double.toString(ppay+basic+convy+dnalw+spalw+pfalw+saalw+lnsub+wkalw+mdalw+ltalw));
		}
		catch(Exception e)
		{
			setMSG("Invalid Data ..",'E');
			setMSG(e,"Child.vldEPMST");
			return false;
		}
		return true;
	}

/** Save qualification details */	
	private void savQLMST()
	{
		try
		{
			for(int i=0;i<tblQUAL.getRowCount();i++)
			{
				if (!(tblQUAL.getValueAt(i,1).toString().equals(""))&&cl_dat.M_flgLCUPD_pbst==true)
				{
					if(tblQUAL.getValueAt(i,0).toString().equals("true"))
					{
						if(flaODLQL[i])
						{
							M_strSQLQRY="UPDATE HR_QLMST SET "
								+"QL_PSGYR='"+nvlSTRVL(tblQUAL.getValueAt(i,2).toString().trim(),"")+"',"//QL_PSGDT	Date,
								+"QL_CATDS='"+nvlSTRVL(tblQUAL.getValueAt(i,4).toString().trim(),"")+"',"//QL_CATDS	Varchar (25),
								+"QL_COLLG='"+nvlSTRVL(tblQUAL.getValueAt(i,5).toString().trim(),"")+"',"//QL_COLLG	Varchar (15),
								+"QL_UNIVT='"+nvlSTRVL(tblQUAL.getValueAt(i,6).toString().trim(),"")+"',"//QL_UNIVT	Varchar (15),
								+"QL_CLASS='"+nvlSTRVL(tblQUAL.getValueAt(i,3).toString().trim(),"")+"',"//QL_CLASS	Varchar (5),
								+"QL_PERCT= "+nvlSTRVL(tblQUAL.getValueAt(i,3).toString().trim(),"0")+","//+tblQUAL.getValueAt(i,2).toString()+"',"//QL_PERCT    Decimal(3,1),
								+getUSGDTL("QL",'u',null)+" WHERE QL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QL_EMPNO='"+txtEMPNO.getText().trim()+"' AND QL_CATGR='"+tblQUAL.getValueAt(i,1).toString().trim()+"'";//QL_LUPDT    Date
							flaODLQL[i]=false;
						}
						else
						{
							M_strSQLQRY="insert into HR_QLMST (QL_CMPCD,QL_EMPNO,QL_CATGR,QL_PSGYR,QL_CATDS,QL_COLLG,QL_UNIVT,QL_CLASS,QL_PERCT,QL_TRNFL,QL_STSFL,QL_LUSBY,QL_LUPDT) values("
								+"'"+cl_dat.M_strCMPCD_pbst+"',"//QL_CMPCD	Varchar (2),
								+"'"+txtEMPNO.getText().trim()+"',"//QL_EMPNO	Varchar (5),
								+"'"+tblQUAL.getValueAt(i,1).toString()+"',"//QL_CATGR	Varchar (30),
								+"'"+tblQUAL.getValueAt(i,2).toString()+"',"//QL_PSGDT	Date,
								+"'"+tblQUAL.getValueAt(i,4).toString()+"',"//QL_CATDS	Varchar (25),
								+"'"+tblQUAL.getValueAt(i,5).toString()+"',"//QL_COLLG	Varchar (15),
								+"'"+tblQUAL.getValueAt(i,6).toString()+"',"//QL_UNIVT	Varchar (15),
								+"'"+tblQUAL.getValueAt(i,3).toString()+"',"//QL_CLASS	Varchar (5),
								+nvlSTRVL(tblQUAL.getValueAt(i,3).toString().trim(),"0")+","//+tblQUAL.getValueAt(i,2).toString()+"',"//QL_PERCT    Decimal(3,1),
								+getUSGDTL("QL",'i',"")+")";//QL_LUPDT    Date
						}
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
			}
		}catch(Exception e)
		{
			setMSG(e,"Child.savQLMST");
			cl_dat.M_flgLCUPD_pbst=false;
			cl_dat.exeDBCMT("savQLMST");
			setMSG("Error in saving Qualification details, Action cancelled.. ",'E');		
		}
	}
	
/** To add a record into database */
	private void exeADDREC()
	{
		if(vldEPMST())
		{
			try
			{
				if(tblQUAL.isEditing())
					tblQUAL.getCellEditor().stopCellEditing();
				if(tblDPNDT.isEditing())
					tblDPNDT.getCellEditor().stopCellEditing();
				String adp=(txtNAMEP.getText()+" |"+nvlSTRVL(txtADDP1.getText(),"  ")+" |"+nvlSTRVL(txtADDP2.getText(),"  ")+" |"+nvlSTRVL(txtDISTP.getText(),"  ")+" |"+nvlSTRVL(M_txtSTAT_pbstP.getText(),"  ")+" |"+nvlSTRVL(txtPINP.getText(),"  ")+" |"+nvlSTRVL(txtTELNOP.getText(),"  ")+" |"+nvlSTRVL(txtEMAIL.getText(),"  ")),
				adt= (nvlSTRVL(txtNAMET.getText(),"  ")+" |"+nvlSTRVL(txtADDT1.getText(),"  ")+" |"+nvlSTRVL(txtADDT2.getText(),"  ")+" |"+nvlSTRVL(txtDISTT.getText(),"  ")+" |"+nvlSTRVL(M_txtSTAT_pbstT.getText(),"  ")+" |"+nvlSTRVL(txtPINT.getText(),"  ")+" |"+nvlSTRVL(txtTELNOT.getText(),"  "));
				String L_strYRDGT=null;
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				L_strYRDGT=Integer.toString( M_calLOCAL.YEAR);
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY = "INSERT INTO HR_EPMST (EP_CMPCD,EP_HRSBS,EP_EMPNO,EP_SHRNM,EP_FULNM,EP_ACCRF,EP_SAPNO,EP_DESGN,EP_MMGRD,EP_DPTNM,EP_DPTCD,EP_EPLOC,EP_QUALN,EP_JONDT,EP_EMPCT,EP_GENFL,EP_LFTDT,EP_BTHDT,EP_ADRPR,EP_ADRTP,EP_BASAL,EP_PPSAL,EP_GRSAL,EP_REFRN,EP_RELGN,EP_NATNL,EP_MRTST,EP_SEXFL,EP_BTHPL,EP_CATAG,EP_IDNMK,EP_BLDGP,EP_PHDSB,EP_ACCTP,EP_TELEN,EP_VCLMT,EP_EDNCT,EP_YRDGT,EP_CRFDT,EP_BNKCD,EP_ACTNO,EP_PANNO,EP_PAYMD,EP_STSFL,EP_TRNFL,EP_LUSBY,EP_LUPDT,EP_FSTNM,EP_MDLNM,EP_LSTNM) values ("
					+"'"+cl_dat.M_strCMPCD_pbst+"',"//EP_CMPCD
					+"'"+M_strSBSCD+"',"//EP_HRSBS
					+"'"+txtEMPNO.getText()+"',"//EP_EMPNO
					+"'"+txtENM.getText()+"',"//EP_SHRNM
					+"'"+nvlSTRVL(txtENM1.getText().trim(),"")+"|"+nvlSTRVL(txtENM2.getText().trim(),"")+"|"+nvlSTRVL(txtENM3.getText().trim(),"")+"',"//EP_FULNM
					+"'"+txtACCRF.getText().trim()+"',"//EP_ACCRF		 
					+"'"+txtSAPNO.getText().trim()+"',"//EP_ACCRF		 
					+"'"+cmbDESG.getSelectedItem()+"',"//EP_DESGN
					+"'"+cmbMGRD.getSelectedItem()+"',"//ep_mmgrd
					+"'"+cmbDEPT.getSelectedItem()+"',"//EP_DPTNM,
					+"'"+hstDEPT.get(cmbDEPT.getSelectedItem().toString())+"',"//EP_DPTCD,
					+"'"+cmbEPLOC.getSelectedItem()+"',"//EP_EPLOC,
					+"'"+cmbEDCAT.getSelectedItem()+"|"+cmbEDLVL.getSelectedItem()+"',"//EP_QUALN,
					+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDTJOIN.getText().trim()))+"',"//EP_JONDT,
					+"'"+txtEMPCT.getText()+"',"//EP_EMPCT
					+(chkGENFL.isSelected() ? "'Y'," : "'',")//,EP_GENFL,
					+ null+","//EP_LFTDT,
					+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOB.getText()))+"',"//EP_BTHDT,
					+"'"+adp+"',"//EP_ADRPR,
					+"'"+adt+"',"//EP_ADRTP,nvlSTRVL(txtNAMET.getText().trim(),"")
					+(txtBASIC.getText().length()>0 ? txtBASIC.getText().trim() : "0")+","//EP_BASAL,
					+(txtPPAY.getText().length()>0 ? txtPPAY.getText().trim() : "0")+","//EP_PPSAL,
					+(txtGROS.getText().length()>0 ? txtGROS.getText().trim() : "0")+","//EP_GRSAL,
					+"'"+nvlSTRVL(txtREF1.getText().trim(),"")+"|"+nvlSTRVL(txtREF2.getText().trim(),"")+"',"//EP_RERN,
					+"'"+nvlSTRVL(txtCATA.getText().trim(),"")+"',"//EP_RELGN,
					+"'"+nvlSTRVL(txtNATL.getText().trim(),"")+"',"//EP_NATNL,
					+"'"+cmbMRTL.getSelectedIndex()+"',"//EP_MRTST,
					+"'"+cmbSEX.getSelectedIndex()+"',"//EP_SEXFL
					+"'"+nvlSTRVL(txtBRTPL.getText(),"")+"',"//EP_SEXFL
					+"'"+nvlSTRVL(txtCATA.getText(),"")+"',"//EP_SEXFL
					+"'"+nvlSTRVL(txtIDENT.getText().trim(),"")+"',"//EP_IDNMK,
					+"'"+nvlSTRVL(txtBLDGP.getText().trim(),"")+"',"//EP_BLDGP,
					+"'"+nvlSTRVL(txtDSBLY.getText().trim(),"")+"',"//EP_PHDSB,
					+"'"+(rdbBHLR.isSelected() ? "0":(rdbFMLY.isSelected() ? "1":"2"))+"',"//EP_ACCTP
					+""+nvlSTRVL(txtTELEN.getText().trim(),"0")+","//EP_TELEN
					+""+nvlSTRVL(txtVCLMT.getText().trim(),"0")+","//EP_VCLMT
					+""+(intEDCNT>2 ? 2 : intEDCNT)+","//EP_EDNCT
					+""+L_strYRDGT+","//EP_YRDGT
					+(txtCRFDT.getText().length()>0 ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCRFDT.getText()))+"'," : "null,")//EP_CRFDT,
					+(txtBNKCD.getText().length()>0 ? "'"+txtCRFDT.getText()+"'," : "null,")//EP_BNKCD,
					+(txtACTNO.getText().length()>0 ? "'"+txtACTNO.getText()+"'," : "null,")//,EP_ACTNO,
					+(txtPANNO.getText().length()>0 ? "'"+txtPANNO.getText()+"'," : "null,")//,EP_PANNO,
					+"'"+Integer.toString( cmbPAYMD.getSelectedIndex())+"'," //,EP_PAYMD,
					+"'"+""+"',"//EP_STSFL
					+"'"+"0"+"',"//EP_TRNFL,
					+"'"+cl_dat.M_strUSRCD_pbst+"',"//EP_LUSBY,
					+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
					+"'"+txtENM1.getText().trim()+"',"//EP_LUSBY,
					+"'"+txtENM2.getText().trim()+"',"//EP_LUSBY,
					+"'"+txtENM3.getText().trim()+"'"//EP_LUSBY,
					+")";//EP_LUPDT
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
					modLGMST();
				if(cl_dat.M_flgLCUPD_pbst)
					savQLMST();	
				if(cl_dat.M_flgLCUPD_pbst)
					modFLMST();
				if(cl_dat.exeDBCMT("exeADDREC"))
				{
                    clrCOMP();
                    setMSG("Employee Added ..",'N');
                }
				else
					setMSG("Addition failed..",'E');
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}catch(Exception L_E)
			{
				cl_dat.M_flgLCUPD_pbst=false;
				cl_dat.exeDBCMT("exeADDREC");
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"exeADDREC");
			}
		}
	}
	/** To select a record from database	 */
	private void exeSELREC(String L_ACTCMD)
	{
		flaODLQL=new boolean[10];
		flaODLFL=new boolean[10];
		flaODLLG=new boolean[5];
		try
		{
			if(L_ACTCMD.equals("ENM"))
			{
                M_strSQLQRY="SELECT COUNT(*) L_CNT from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_SHRNM ='"+txtENM.getText().trim().toUpperCase() +"' and "+"EP_HRSBS='"+M_strSBSCD+"'"; 
                if(txtEMPNO.getText().trim().length() >0)
                    M_strSQLQRY += " AND EP_EMPNO ='"+txtEMPNO.getText().trim()+"'";
                M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			    if(M_rstRSSET !=null)
                {
                    if(M_rstRSSET.next())
                    if(M_rstRSSET.getInt("L_CNT") > 1)
                    {
                        setMSG("More than one record,Please select the Initial using F1 help..",'E');
                        return;
                    }
                    M_rstRSSET.close();
                }
                M_strSQLQRY="SELECT * from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_SHRNM ='"+txtENM.getText().trim().toUpperCase() +"' and EP_HRSBS='"+M_strSBSCD+"'";
                if(txtEMPNO.getText().trim().length() >0)
                    M_strSQLQRY += " AND EP_EMPNO ='"+txtEMPNO.getText().trim()+"'";
	        }	
            else
				M_strSQLQRY = "SELECT * from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO ='"+txtEMPNO.getText().trim() +"' and EP_HRSBS='"+M_strSBSCD+"'";
			clrCOMP();
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET ==null)
				setMSG("Emplyee not found ..",'E');
			else
			{
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("EP_LFTDT")!=null && (!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
						setMSG("Employee has left the orgnisation ..",'E');
					else
					{
						setMSG("",'N');
						txtEMPNO.setText(nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),""));
						txtENM.setText(nvlSTRVL(M_rstRSSET.getString("EP_SHRNM"),""));
						txtACCRF.setText(nvlSTRVL(M_rstRSSET.getString("EP_ACCRF"),""));
						txtSAPNO.setText(nvlSTRVL(M_rstRSSET.getString("EP_SAPNO"),""));
						if(M_rstRSSET.getDate("EP_BTHDT")!=null)
							txtDOB.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_BTHDT")));
						String strADD=nvlSTRVL(M_rstRSSET.getString("EP_ADRPR"),"Address not available");
						StringTokenizer st=new StringTokenizer(strADD,"|");
						int t=st.countTokens();
						String [] str=new String[t];
						int i=0;
						/*while(st.hasMoreTokens())
						{
							str[i]=st.nextToken();
							i++;
						};

						if(t>0)	txtEMAIL.setText(str[t-1]); else txtEMAIL.setText("");
						if(t>1)	txtTELNOP.setText(str[t-2]); else txtTELNOP.setText("");
						if(t>2)	txtPINP.setText(str[t-3]); else txtPINP.setText("");
						if(t>3)	M_txtSTAT_pbstP.setText(str[t-4]); else M_txtSTAT_pbstP.setText("");
						if(t>4)	txtDISTP.setText(str[t-5]); else txtDISTP.setText("");
						*/
						
						if(st.hasMoreTokens())
							txtNAMEP.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtADDP1.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtADDP2.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtDISTP.setText(st.nextToken());
						if(st.hasMoreTokens())
							M_txtSTAT_pbstP.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtPINP.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtTELNOP.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtEMAIL.setText(st.nextToken());

						/*if(str.length>4)
						{
							txtNAMEP.setText(str[0]);
							if(str.length>5)
							{
								txtADDP1.setText(str[1]);
								if(str.length>6)
									txtADDP2.setText(str[2]);
							}
						}*/
						strADD=nvlSTRVL(M_rstRSSET.getString("EP_ADRTP"),"Address not available");
						st=new StringTokenizer(strADD,"|");
						t=st.countTokens();
						str=new String[t];
						i=0;
						/*while(st.hasMoreTokens())
						{
							str[i]=st.nextToken();
							i++;
						};
						 if(t>0)	txtTELNOT.setText(str[t-1]); else txtTELNOT.setText("");
						 if(t>1)    txtPINT.setText(str[t-2]); else txtPINT.setText("");
						 if(t>2)	M_txtSTAT_pbstT.setText(str[t-3]); else M_txtSTAT_pbstT.setText("");
						 if(t>3)	txtDISTT.setText(str[t-4]); else txtDISTT.setText("");
						if(str.length>3)
						{
							txtNAMET.setText(str[0]);
							if(str.length>4)
							{
								txtADDT1.setText(str[1]);
								if(str.length>5)
									txtADDT2.setText(str[2]);
							}
						}
						*/
						if(st.hasMoreTokens())
							txtNAMET.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtADDT1.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtADDT2.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtDISTT.setText(st.nextToken());
						if(st.hasMoreTokens())
							M_txtSTAT_pbstT.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtPINT.setText(st.nextToken());
						if(st.hasMoreTokens())
							txtTELNOT.setText(st.nextToken());

						if(M_rstRSSET.getDate("EP_JONDT")!=null)
							txtDTJOIN.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_JONDT"))); 
						if(M_rstRSSET.getString("EP_EMPCT")!=null)
						{	
							txtEMPCT.setText(M_rstRSSET.getString("EP_EMPCT")); 
							lblEMPCT.setText(hstEMPCT.get(M_rstRSSET.getString("EP_EMPCT"))); 
						}
						if(nvlSTRVL(M_rstRSSET.getString("EP_GENFL"),"").equals("Y"))
							chkGENFL.setSelected(true);
						txtNATL.setText(nvlSTRVL(M_rstRSSET.getString("EP_NATNL"),""));
						txtIDENT.setText(nvlSTRVL(M_rstRSSET.getString("EP_IDNMK"),""));
						txtBLDGP.setText(nvlSTRVL(M_rstRSSET.getString("EP_BLDGP"),""));
						txtBASIC.setText(nvlSTRVL(M_rstRSSET.getString("EP_BASAL"),""));
						txtGROS.setText(nvlSTRVL(M_rstRSSET.getString("EP_GRSAL"),""));
						txtPPAY.setText(nvlSTRVL(M_rstRSSET.getString("EP_PPSAL"),""));
						cmbSEX.setSelectedIndex(Integer.parseInt(M_rstRSSET.getString("EP_SEXFL")));
						cmbMRTL.setSelectedIndex(Integer.parseInt(M_rstRSSET.getString("EP_MRTST")));
						cmbDESG.setSelectedItem(M_rstRSSET.getString("EP_DESGN"));
						cmbMGRD.setSelectedItem(M_rstRSSET.getString("EP_MMGRD"));
						cmbDEPT.setSelectedItem(M_rstRSSET.getString("EP_DPTNM"));
						cmbEPLOC.setSelectedItem(M_rstRSSET.getString("EP_EPLOC"));
						txtBRTPL.setText(nvlSTRVL(M_rstRSSET.getString("EP_BTHPL"),""));
						txtCATA.setText(nvlSTRVL(M_rstRSSET.getString("EP_CATAG"),""));
						txtDSBLY.setText(nvlSTRVL(M_rstRSSET.getString("EP_PHDSB"),""));
						rdbBHLR.setSelected(M_rstRSSET.getString("EP_ACCTP").equals("0") ? true : false);
						rdbOWND.setSelected(M_rstRSSET.getString("EP_ACCTP").equals("2") ? true : false);
						rdbFMLY.setSelected(M_rstRSSET.getString("EP_ACCTP").equals("1") ? true : false);
						st=new StringTokenizer(nvlSTRVL(M_rstRSSET.getString("EP_REFRN"),""),"|");
						if(st.hasMoreElements())
							txtREF1.setText(st.nextToken());
						if(st.hasMoreElements())
							txtREF2.setText(st.nextToken());
						
						
						st=new StringTokenizer(nvlSTRVL(M_rstRSSET.getString("EP_FULNM"),""),"|");
						int cntSTTKN = st.countTokens();
						if(st.hasMoreElements())
							txtENM1.setText(st.nextToken());
						if(cntSTTKN > 2 && st.hasMoreElements())
							txtENM2.setText(st.nextToken());
						if(st.hasMoreElements())
							txtENM3.setText(st.nextToken());
						
						st=new StringTokenizer(nvlSTRVL(M_rstRSSET.getString("EP_QUALN"),""),"|");
						if(st.hasMoreElements())
							cmbEDCAT.setSelectedItem(st.nextToken());
						if(st.hasMoreElements())
							cmbEDLVL.setSelectedItem(st.nextToken());
						intEDCNT=Integer.parseInt( M_rstRSSET.getString("EP_EDNCT"));
						if(M_rstRSSET.getDate("EP_CRFDT")!=null)
							txtCRFDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_CRFDT")));
						txtPANNO.setText(nvlSTRVL(M_rstRSSET.getString("EP_PANNO"),""));
						txtBNKCD.setText(nvlSTRVL(M_rstRSSET.getString("EP_BNKCD"),""));
						txtACTNO.setText(nvlSTRVL(M_rstRSSET.getString("EP_ACTNO"),""));
						cmbPAYMD.setSelectedIndex(Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("EP_PAYMD"),"0")));
						cl_dat.M_txtUSER_pbst.setText(M_rstRSSET.getString("EP_LUSBY"));
						cl_dat.M_txtDATE_pbst.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EP_LUPDT")));
					}
 				}
				else
				{
					setMSG("Emplyee not found ..",'E');
				}
			}
			//getSAL();
			M_strSQLQRY = "SELECT * from HR_LGMST where LG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LG_EMPNO ='"+txtEMPNO.getText().trim() +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET !=null)
			{
				int L_RWCNT=0;
				while(M_rstRSSET.next()){
				if(L_RWCNT==0)
				{
					txtLANG1.setText(nvlSTRVL(M_rstRSSET.getString("LG_LNGDS"),""));
					chbWRT1.setSelected(M_rstRSSET.getString("LG_WRTFL").equals("Y") ? true : false);
					chbREAD1.setSelected(M_rstRSSET.getString("LG_REDFL").equals("Y") ? true : false);
					chbSPK1.setSelected(M_rstRSSET.getString("LG_SPKFL").equals("Y") ? true : false);
					rdbMTNG1.setSelected(M_rstRSSET.getString("LG_MTGFL").equals("Y") ? true : false);
					flaODLLG[0]=true;
				}
				else if(L_RWCNT==1)
				{
					txtLANG2.setText(nvlSTRVL(M_rstRSSET.getString("LG_LNGDS"),""));
					chbWRT2.setSelected(M_rstRSSET.getString("LG_WRTFL").equals("Y") ? true : false);
					chbREAD2.setSelected(M_rstRSSET.getString("LG_REDFL").equals("Y") ? true : false);
					chbSPK2.setSelected(M_rstRSSET.getString("LG_SPKFL").equals("Y") ? true : false);
					rdbMTNG2.setSelected(M_rstRSSET.getString("LG_MTGFL").equals("Y") ? true : false);
					flaODLLG[1]=true;
				}
				else if(L_RWCNT==2)
				{
					txtLANG3.setText(nvlSTRVL(M_rstRSSET.getString("LG_LNGDS"),""));
					chbWRT3.setSelected(M_rstRSSET.getString("LG_WRTFL").equals("Y") ? true : false);
					chbREAD3.setSelected(M_rstRSSET.getString("LG_REDFL").equals("Y") ? true : false);
					chbSPK3.setSelected(M_rstRSSET.getString("LG_SPKFL").equals("Y") ? true : false);
					rdbMTNG3.setSelected(M_rstRSSET.getString("LG_MTGFL").equals("Y") ? true : false);
					flaODLLG[2]=true;
				}
				else if(L_RWCNT==3)
				{
					txtLANG4.setText(nvlSTRVL(M_rstRSSET.getString("LG_LNGDS"),""));
					chbWRT4.setSelected(M_rstRSSET.getString("LG_WRTFL").equals("Y") ? true : false);
					chbREAD4.setSelected(M_rstRSSET.getString("LG_REDFL").equals("Y") ? true : false);
					chbSPK4.setSelected(M_rstRSSET.getString("LG_SPKFL").equals("Y") ? true : false);
					rdbMTNG4.setSelected(M_rstRSSET.getString("LG_MTGFL").equals("Y") ? true : false);
					flaODLLG[3]=true;
				}
				else if(L_RWCNT==4)
				{
					txtLANG5.setText(nvlSTRVL(M_rstRSSET.getString("LG_LNGDS"),""));
					chbWRT5.setSelected(M_rstRSSET.getString("LG_WRTFL").equals("Y") ? true : false);
					chbREAD5.setSelected(M_rstRSSET.getString("LG_REDFL").equals("Y") ? true : false);
					chbSPK5.setSelected(M_rstRSSET.getString("LG_SPKFL").equals("Y") ? true : false);
					rdbMTNG5.setSelected(M_rstRSSET.getString("LG_MTGFL").equals("Y") ? true : false);
					flaODLLG[4]=true;
				}
				L_RWCNT++;
			}
			}
			flgIMGFL = false;
			M_rstRSSET=cl_dat.exeSQLQRY0("Select COUNT(*) L_CNT from CO_IMMST where IM_SYSCD='HR' and IM_SBSCD='"+M_strSBSCD+"' and IM_DOCNO='"+txtEMPNO.getText()+"'");
			if(M_rstRSSET!=null)
			{
			   if(M_rstRSSET.next())
			   {
				if(M_rstRSSET.getInt("L_CNT") >0)
					flgIMGFL = true;
			    }
			M_rstRSSET.close();	
				
			}
			
			M_rstRSSET=cl_dat.exeSQLQRY0("Select DLURLPATH(IM_FILRF) IM_FILRF from CO_IMMST where IM_SYSCD='HR' and IM_SBSCD='"+M_strSBSCD+"' and IM_DOCNO='"+txtEMPNO.getText()+"'");
			//System.out.println("1");
			if(M_rstRSSET!=null)
			   if(M_rstRSSET.next())
			   {
				   boolean L_flgFOUND=false;
				   if(M_rstRSSET.getString("IM_FILRF")!=null)
				   {
						File L_filTEMP=new File(M_rstRSSET.getString("IM_FILRF").substring(1).replace('/','\\'));
						//System.out.println("file "+L_filTEMP);
						if(L_filTEMP.isFile())
						{
							L_flgFOUND=true;
							txtPHOTO.setText(M_rstRSSET.getString("IM_FILRF").substring(1).replace('/','\\'));
							///flgIMGFL=true;
							JLabel tmp=new JLabel(new ImageIcon(this.getToolkit().createImage(M_rstRSSET.getString("IM_FILRF").substring(1).replace('/','\\')).getScaledInstance(122,125,Image.SCALE_SMOOTH)));
							pnlIMAGE.removeAll();
							tmp.setBorder(new EtchedBorder(Color.black,Color.lightGray));
							add(tmp,1,1,5,0.95,pnlIMAGE,'L');
							add(pnlIMAGE,1,6,6,1,this,'L');
							updateUI();
						}
					//	else if(nvlSTRVL(M_rstRSSET.getString("IM_FILRF")," ").length()>0)
					//	    flgIMGFL=true;
					    //System.out.println("file "+flgIMGFL);
				   }
				   if(!L_flgFOUND)
				   {
						pnlIMAGE.removeAll();
						add(pnlIMAGE,1,6,6,1,this,'L');
						updateUI();
				   }
			   }
			   //System.out.println("2 "+flgIMGFL);
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst))
				txtEMPNO.setEnabled(false);
			tbpMAIN.requestFocus();
		}catch(Exception L_E){
			setMSG(L_E,"exeSELREC");
		}
	}
	/** To retreive grade wise salary of the employee	 */
	private void getSAL()
	{
		try
		{
//GETTING GRTADE WISE SALARY
			java.sql.ResultSet L_rstRSSET;
			M_strSQLQRY = "SELECT isnull(gr_dnalw,0) gr_dnalw,isnull(gr_hralw,0) gr_hralw,isnull(gr_spalw,0) gr_spalw,isnull(gr_convy,0) gr_convy,isnull(gr_lnsub,0) gr_lnsub,isnull(gr_pfalw,0) gr_pfalw,isnull(gr_wkalw,0) gr_wkalw,isnull(gr_ltalw,0) gr_ltalw,gr_ltafl,isnull(gr_saalw,0) gr_saalw,isnull(gr_wsalw,0) gr_wsalw,isnull(gr_gralw,0) gr_gralw,isnull(gr_cralw,0) gr_cralw,isnull(gr_mdalw,0) gr_mdalw,gr_mdafl,isnull(gr_chedn,0) gr_chedn from HR_GRMST where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRDCD ='"+cmbMGRD.getSelectedItem().toString().trim() +"'";
			//System.out.print("M_strSQLQRY>>	"+M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					strMDAFL=nvlSTRVL(L_rstRSSET.getString("GR_MDAFL"),"X");
					strLTAFL=nvlSTRVL(L_rstRSSET.getString("GR_LTAFL"),"X");					
					
					txtDNALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_DNALW"),2));
					txtHRALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_HRALW"),2));
					txtSPALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_SPALW"),2));
					txtCONVY.setText(setNumberFormat(L_rstRSSET.getDouble("GR_CONVY"),2));
					txtLNSUB.setText(setNumberFormat(L_rstRSSET.getDouble("GR_LNSUB"),2));
					double basic=Double.parseDouble(txtBASIC.getText()) ;
					System.out.println("strMDAFL1>>"+strMDAFL);
					if(basic>15000 && !strMDAFL.equals("P"))
					{
						StringTokenizer st=new StringTokenizer(Double.toString(basic/12),".");
						String s1=st.nextToken(),s2=st.nextToken();
						if(s2.length()>1)
							s2=s2.substring(0,2);
						else
							s2="00";
						st=new StringTokenizer(Double.toString(basic/12),".");
						txtMDALW.setText(setNumberFormat(Double.parseDouble(s1+"."+s2),2));
					}
					else
						txtMDALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_MDALW"),2));
					txtPFALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_PFALW"),2));
					txtWKALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_WKALW"),2));
					txtLTALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_LTALW"),3));
					//System.out.println("result>>"+L_rstRSSET.getDouble("GR_LTALW"));
					//System.out.println("009>>"+setNumberFormat(L_rstRSSET.getDouble("GR_LTALW"),3));
					txtSAALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_SAALW"),2));
                    txtWSALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_WSALW"),2));
                    txtGRALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_GRALW"),2));
                    txtCRALW.setText(setNumberFormat(L_rstRSSET.getDouble("GR_CRALW"),2));
                    txtCHEDN.setText(Double.toString(((L_rstRSSET.getDouble("GR_CHEDN")))));
					//txtCHEDN.setText(Double.toString(((M_rstRSSET.getDouble("GR_CHEDN"))*((double)intEDCNT))));
					strMDALW=txtMDALW.getText();
				}
				L_rstRSSET.close();
			}
		}catch(SQLException e)
		{
			setMSG(e,"Child.getSAL");
		}
	}
	/** To save modified record	 */
	private void exeMODREC()
	{
		if(vldEPMST())
		{
			try
			{
				if(tblQUAL.isEditing())
					tblQUAL.getCellEditor().stopCellEditing();
				if(tblDPNDT.isEditing())
					tblDPNDT.getCellEditor().stopCellEditing();
		
				String adp=(txtNAMEP.getText()+"|"+nvlSTRVL(txtADDP1.getText(),"  ")+"|"+nvlSTRVL(txtADDP2.getText(),"  ")+"|"+nvlSTRVL(txtDISTP.getText(),"  ")+"|"+nvlSTRVL(M_txtSTAT_pbstP.getText(),"  ")+"|"+nvlSTRVL(txtPINP.getText(),"  ")+"|"+nvlSTRVL(txtTELNOP.getText(),"  ")+"|"+nvlSTRVL(txtEMAIL.getText(),"  ")),
				adt= (nvlSTRVL(txtNAMET.getText(),"  ")+"|"+nvlSTRVL(txtADDT1.getText(),"  ")+"|"+nvlSTRVL(txtADDT2.getText(),"  ")+"|"+nvlSTRVL(txtDISTT.getText(),"  ")+"|"+nvlSTRVL(M_txtSTAT_pbstT.getText(),"  ")+"|"+nvlSTRVL(txtPINT.getText(),"  ")+"|"+nvlSTRVL(txtTELNOT.getText(),"  "));
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				String L_strYRDGT=Integer.toString( M_calLOCAL.get(M_calLOCAL.YEAR));
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				boolean L_flgOLDDT=false;
				
				// COMMENTED TEMPORARILY
		/*		if(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(cl_dat.M_strYSTDT_pbst))<0)
				{
					cl_dat.exeSQLUPD("Insert into HR_EPMAM (EP_HRSBS,EP_EMPNO,EP_SHRNM,EP_FULNM,EP_DESGN,EP_MMGRD,EP_DPTNM,EP_DPTCD,EP_EPLOC,EP_QUALN,EP_JONDT,EP_LFTDT,EP_BTHDT,EP_ADRPR,EP_ADRTP,EP_BASAL,EP_PPSAL,EP_GRSAL,EP_REFRN,EP_RELGN,EP_NATNL,EP_MRTST,EP_SEXFL,EP_BTHPL,EP_CATAG,EP_IDNMK,EP_BLDGP,EP_PHDSB,EP_ACCTP,EP_TELEN,EP_VCLMT,EP_EDNCT,EP_YRDGT,EP_STSFL,EP_TRNFL,EP_LUSBY,EP_LUPDT) select EP_HRSBS,EP_EMPNO,EP_SHRNM,EP_FULNM,EP_DESGN,EP_MMGRD,EP_DPTNM,EP_DPTCD,EP_EPLOC,EP_QUALN,EP_JONDT,EP_LFTDT,EP_BTHDT,EP_ADRPR,EP_ADRTP,EP_BASAL,EP_PPSAL,EP_GRSAL,EP_REFRN,EP_RELGN,EP_NATNL,EP_MRTST,EP_SEXFL,EP_BTHPL,EP_CATAG,EP_IDNMK,EP_BLDGP,EP_PHDSB,EP_ACCTP,EP_TELEN,EP_VCLMT,EP_EDNCT,"+L_strYRDGT+",EP_STSFL,'0',EP_LUSBY,EP_LUPDT from HR_EPMST where EP_HRSBS='"+M_strSBSCD+"' and EP_EMPNO='"+txtEMPNO.getText()+"'","setLCLPD");
					L_flgOLDDT=true;
				}
				else if(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(cl_dat.M_strYEDDT_pbst))>0)
				{
					JOptionPane.showMessageDialog(this, "Modification for next finantial year is not allowed ..", "Error", JOptionPane.ERROR_MESSAGE) ;
					return;
				}
				else
				{
					M_strSQLQRY="Insert into HR_EPMAM select * from HR_EPMST where EP_HRSBS='"+M_strSBSCD+"' and EP_EMPNO='"+txtEMPNO.getText()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLPD");
				}*/
					M_strSQLQRY = "UPDATE "+(L_flgOLDDT==true ? "HR_EPMAM" : "HR_EPMST")+" set EP_EMPNO="
						+"'"+txtEMPNO.getText()+"',"//EP_EMPNO
						+"EP_SHRNM='"+txtENM.getText()+"',"//EP_SHRNM
						+"EP_FULNM='"+nvlSTRVL(txtENM1.getText().trim(),"")+"|"+nvlSTRVL(txtENM2.getText().trim(),"")+"|"+nvlSTRVL(txtENM3.getText().trim(),"")+"',"//EP_FULNM
						+"EP_ACCRF='"+txtACCRF.getText().trim()+"',"//EP_DESGN
						+"EP_SAPNO='"+txtSAPNO.getText().trim()+"',"//EP_DESGN
						+"EP_DESGN='"+cmbDESG.getSelectedItem()+"',"//EP_DESGN
						+"EP_MMGRD='"+cmbMGRD.getSelectedItem()+"',"//EP_DESGN
						+"EP_DPTCD='"+hstDEPT.get(cmbDEPT.getSelectedItem().toString())+"',"//EP_DPTCD,
						+"EP_DPTNM='"+cmbDEPT.getSelectedItem()+"',"//EP_DPTCD,
						+"EP_EPLOC='"+cmbEPLOC.getSelectedItem()+"',"//EP_EPLOC,
						+"EP_QUALN='"+cmbEDCAT.getSelectedItem()+"|"+cmbEDLVL.getSelectedItem()+"',"//EP_QUALN,
						+"EP_JONDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDTJOIN.getText().trim()))+"',"//EP_JONDT,
						+"EP_EMPCT='"+txtEMPCT.getText().trim()+"',"//EP_EMPCT,								 
						+"EP_GENFL="+(chkGENFL.isSelected() ? "'Y'," : "'',")//,EP_GENFL,
						+"EP_LFTDT= "+ null+","//EP_LFTDT,
						+"EP_BTHDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOB.getText()))+"',"//EP_BTHDT,
						+"EP_ADRPR='"+adp+"',"//EP_ADRPR,
						+"EP_ADRTP='"+adt+"',"//EP_ADRTP,nvlSTRVL(txtNAMET.getText().trim(),"")
						+"EP_BASAL= "+(txtBASIC.getText().length()>0 ? txtBASIC.getText().trim() : "0")+","//EP_BASAL,
						+"EP_PPSAL= "+(txtPPAY.getText().length()>0 ? txtPPAY.getText().trim() : "0")+","//EP_PPSAL,
						+"EP_GRSAL= "+(txtGROS.getText().length()>0 ? txtGROS.getText().trim() : "0")+","//EP_GRSAL,
						+"EP_REFRN='"+nvlSTRVL(txtREF1.getText().trim(),"")+"|"+nvlSTRVL(txtREF2.getText().trim(),"")+"',"//EP_RERN,
						+"EP_RELGN='"+nvlSTRVL(txtCATA.getText().trim(),"")+"',"//EP_RELGN,
						+"EP_NATNL='"+nvlSTRVL(txtNATL.getText().trim(),"")+"',"//EP_NATNL,
						+"EP_MRTST='"+cmbMRTL.getSelectedIndex()+"',"//EP_MRTST,
						+"EP_SEXFL='"+cmbSEX.getSelectedIndex()+"',"//EP_SEXFL
						+"EP_BTHPL='"+nvlSTRVL(txtBRTPL.getText(),"")+"',"//EP_SEXFL
						+"EP_CATAG='"+nvlSTRVL(txtCATA.getText(),"")+"',"//EP_SEXFL
						+"EP_IDNMK='"+nvlSTRVL(txtIDENT.getText().trim(),"")+"',"//EP_IDNMK,
						+"EP_BLDGP='"+nvlSTRVL(txtBLDGP.getText().trim(),"")+"',"//EP_BLDGP,
						+"EP_PHDSB='"+nvlSTRVL(txtDSBLY.getText().trim(),"")+"',"//EP_PHDSB,
						+"EP_ACCTP='"+(rdbBHLR.isSelected() ? "0":(rdbFMLY.isSelected() ? "1":"2"))+"',"//EP_ACCTP
						+"EP_TELEN="+nvlSTRVL(txtTELEN.getText(),"0")+","//EP_TELEN
						+"EP_VCLMT="+nvlSTRVL(txtVCLMT.getText(),"0")+","//EP_VCLMT
						+"EP_EDNCT="+intEDCNT+","//EP_EDNCT
						+"EP_CRFDT="+(txtCRFDT.getText().length()>0 ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCRFDT.getText()))+"'," : "null,")//EP_CRFDT,
						+"EP_BNKCD="+(txtBNKCD.getText().length()>0 ? "'"+txtBNKCD.getText()+"'," : "null,")//EP_BNKCD,
						+"EP_ACTNO="+(txtACTNO.getText().length()>0 ? "'"+txtACTNO.getText()+"'," : "null,")//,EP_ACTNO,
						+"EP_PANNO="+(txtPANNO.getText().length()>0 ? "'"+txtPANNO.getText()+"'," : "null,")//,EP_PANNO,
						+"EP_PAYMD="+"'"+Integer.toString( cmbPAYMD.getSelectedIndex())+"'," //,EP_PAYMD,
						+"EP_STSFL='"+""+"',"//EP_STSFL
						+"EP_TRNFL='"+"0"+"',"//EP_TRNFL,
						+"EP_FSTNM='"+txtENM1.getText().trim()+"',"//EP_TRNFL,
						+"EP_MDLNM='"+txtENM2.getText().trim()+"',"//EP_TRNFL,
						+"EP_LSTNM='"+txtENM3.getText().trim()+"',"//EP_TRNFL,		 
						+"EP_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//EP_LUSBY,
						+"EP_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText().trim()+"'"
						+(L_flgOLDDT==true ? " and EP_YRDGT="+L_strYRDGT : "");//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
					modLGMST();
				if(cl_dat.M_flgLCUPD_pbst)
					savQLMST();
				if(cl_dat.M_flgLCUPD_pbst)
					modFLMST();
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if(flgIMGFL)
					{
						if(txtPHOTO.getText().length() == 0)
							M_strSQLQRY="Update CO_IMMST set IM_FILRF = null where IM_SYSCD='HR' and IM_SBSCD='"+M_strSBSCD+"' and IM_DOCTP = 'EP' and IM_DOCNO='"+txtEMPNO.getText()+"'";
						else
							M_strSQLQRY="Update CO_IMMST set IM_FILRF = DLVALUE('file://192.168.10.201/"+txtPHOTO.getText().replace('\\','/')+"','URL','') where IM_SYSCD='HR' and IM_SBSCD='"+M_strSBSCD+"' and IM_DOCTP = 'EP' and IM_DOCNO='"+txtEMPNO.getText()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else if(txtPHOTO.getText().length() > 0)
					{
						M_strSQLQRY="insert into CO_IMMST (IM_CMPCD,IM_SYSCD,IM_SBSCD,IM_DOCTP,IM_DOCNO,IM_FILRF,IM_TRNFL,IM_STSFL,IM_LUSBY,IM_LUPDT) values ("
									+"'"+cl_dat.M_strCMPCD_pbst+"','HR','"+M_strSBSCD+"','EP','"+txtEMPNO.getText()+"',DLVALUE('file://192.168.10.201/"+txtPHOTO.getText().replace('\\','/')+"','URL',''), "
									+getUSGDTL("IM",'I',"0")+")";
					    cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				
				if(cl_dat.exeDBCMT("exeMODREC"))
				{
					clrCOMP();
					setMSG("Employee Modified ..",'N');
				}
				else
					setMSG("Modification failed..",'E');
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}catch(Exception L_E)
			{
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				cl_dat.M_flgLCUPD_pbst=false;
				cl_dat.exeDBCMT("exeMODREC");
				setMSG(L_E,"exeADDREC");
			}
		}	
	}

/** To modify language details in HR_LGMST */
	private void modLGMST()
	{
		try{
			if(txtLANG1.getText().trim().length()>1)
			{
              	if(flaODLLG[0])
				{
					M_strSQLQRY = "UPDATE HR_LGMST SET "
						+"LG_LNGDS='"+txtLANG1.getText().trim()+"',"//LG_LNGDS
						+"LG_REDFL='"+(chbREAD1.isSelected() ? "Y" : "N")+"',"
						+"LG_WRTFL='"+(chbWRT1.isSelected() ? "Y" : "N")+"',"
						+"LG_SPKFL='"+(chbSPK1.isSelected() ? "Y" : "N")+"',"
						+"LG_MTGFL='"+(rdbMTNG1.isSelected() ? "Y" : "N")+"',"
						+"LG_STSFL='"+""+"',"//EP_STSFL
						+"LG_TRNFL='"+"0"+"',"//EP_TRNFL,
						+"LG_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"//EP_LUSBY,
						+"LG_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' WHERE LG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LG_EMPNO= '"+txtEMPNO.getText().trim()+"' AND LG_LNGDS='"+txtLANG1.getText().trim()+"'";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
				  	M_strSQLQRY = "INSERT INTO HR_LGMST (LG_CMPCD,LG_EMPNO,LG_LNGDS,LG_REDFL,LG_WRTFL,LG_SPKFL,LG_MTGFL,LG_STSFL,LG_TRNFL,LG_LUSBY,LG_LUPDT) values ("
						+"'"+cl_dat.M_strCMPCD_EXT_pbst+"',"
						+"'"+txtEMPNO.getText().trim()+"',"
						+"'"+txtLANG1.getText().trim()+"',"//LG_LNGDS
						+"'"+(chbREAD1.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbWRT1.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbSPK1.isSelected() ? "Y" : "N")+"',"
						+"'"+(rdbMTNG1.isSelected() ? "Y" : "N")+"',"
						+getUSGDTL("LG",'i',"")+")";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(txtLANG2.getText().trim().length()>1)
			{
			  	if(flaODLLG[1])
				{
				  	M_strSQLQRY = "UPDATE HR_LGMST SET "
						+"LG_LNGDS='"+txtLANG2.getText().trim()+"',"//LG_LNGDS
						+"LG_REDFL='"+(chbREAD2.isSelected() ? "Y" : "N")+"',"
						+"LG_WRTFL='"+(chbWRT2.isSelected() ? "Y" : "N")+"',"
						+"LG_SPKFL='"+(chbSPK2.isSelected() ? "Y" : "N")+"',"
						+"LG_MTGFL='"+(rdbMTNG2.isSelected() ? "Y" : "N")+"',"
						+getUSGDTL("LG",'u',null)+" WHERE LG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LG_EMPNO= '"+txtEMPNO.getText().trim()+"' AND LG_LNGDS='"+txtLANG2.getText().trim()+"'";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
				  	M_strSQLQRY = "INSERT INTO HR_LGMST (LG_CMPCD,LG_EMPNO,LG_LNGDS,LG_REDFL,LG_WRTFL,LG_SPKFL,LG_MTGFL,LG_STSFL,LG_TRNFL,LG_LUSBY,LG_LUPDT) values ("
						+"'"+cl_dat.M_strCMPCD_pbst+"',"
						+"'"+txtEMPNO.getText().trim()+"',"
						+"'"+txtLANG2.getText().trim()+"',"//LG_LNGDS
						+"'"+(chbREAD2.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbWRT2.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbSPK2.isSelected() ? "Y" : "N")+"',"
						+"'"+(rdbMTNG2.isSelected() ? "Y" : "N")+"',"
						+getUSGDTL("LG",'i',"")+")";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					
				}
			}
			if(txtLANG3.getText().trim().length()>1)
			{
				if(flaODLLG[2])
				{
				  	M_strSQLQRY = "UPDATE HR_LGMST SET "
						+"LG_LNGDS='"+txtLANG3.getText().trim()+"',"//LG_LNGDS
						+"LG_REDFL='"+(chbREAD3.isSelected() ? "Y" : "N")+"',"
						+"LG_WRTFL='"+(chbWRT3.isSelected() ? "Y" : "N")+"',"
						+"LG_SPKFL='"+(chbSPK3.isSelected() ? "Y" : "N")+"',"
						+"LG_MTGFL='"+(rdbMTNG3.isSelected() ? "Y" : "N")+"',"
						+getUSGDTL("LG",'u',null)+" WHERE LG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LG_EMPNO= '"+txtEMPNO.getText().trim()+"' AND LG_LNGDS='"+txtLANG3.getText().trim()+"'";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
					M_strSQLQRY = "INSERT INTO HR_LGMST (LG_CMPCD,LG_EMPNO,LG_LNGDS,LG_REDFL,LG_WRTFL,LG_SPKFL,LG_MTGFL,LG_STSFL,LG_TRNFL,LG_LUSBY,LG_LUPDT) values ("
						+"'"+cl_dat.M_strCMPCD_pbst+"',"
						+"'"+txtEMPNO.getText().trim()+"',"
						+"'"+txtLANG3.getText().trim()+"',"//LG_LNGDS
						+"'"+(chbREAD3.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbWRT3.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbSPK3.isSelected() ? "Y" : "N")+"',"
						+"'"+(rdbMTNG3.isSelected() ? "Y" : "N")+"',"
						+getUSGDTL("LG",'i',"")+")";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(txtLANG4.getText().trim().length()>1)
			{
				if(flaODLLG[3])
				{
				 	M_strSQLQRY = "UPDATE HR_LGMST SET "
					+"LG_LNGDS='"+txtLANG4.getText().trim()+"',"//LG_LNGDS
					+"LG_REDFL='"+(chbREAD4.isSelected() ? "Y" : "N")+"',"
					+"LG_WRTFL='"+(chbWRT4.isSelected() ? "Y" : "N")+"',"
					+"LG_SPKFL='"+(chbSPK4.isSelected() ? "Y" : "N")+"',"
					+"LG_MTGFL='"+(rdbMTNG4.isSelected() ? "Y" : "N")+"',"
					+getUSGDTL("LG",'u',null)+" WHERE LG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LG_EMPNO= '"+txtEMPNO.getText().trim()+"' AND LG_LNGDS='"+txtLANG4.getText().trim()+"'";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
				 	M_strSQLQRY = "INSERT INTO HR_LGMST (LG_CMPCD,LG_EMPNO,LG_LNGDS,LG_REDFL,LG_WRTFL,LG_SPKFL,LG_MTGFL,LG_STSFL,LG_TRNFL,LG_LUSBY,LG_LUPDT) values ("
						+"'"+cl_dat.M_strCMPCD_pbst+"',"
						+"'"+txtEMPNO.getText().trim()+"',"
						+"'"+txtLANG4.getText().trim()+"',"//LG_LNGDS
						+"'"+(chbREAD4.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbWRT4.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbSPK4.isSelected() ? "Y" : "N")+"',"
						+"'"+(rdbMTNG4.isSelected() ? "Y" : "N")+"',"
						+getUSGDTL("LG",'i',"")+")";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(txtLANG5.getText().trim().length()>1)
			{
				if(flaODLLG[4])
				{
					M_strSQLQRY = "UPDATE HR_LGMST SET "
					+"LG_LNGDS='"+txtLANG5.getText().trim()+"',"//LG_LNGDS
					+"LG_REDFL='"+(chbREAD5.isSelected() ? "Y" : "N")+"',"
					+"LG_WRTFL='"+(chbWRT5.isSelected() ? "Y" : "N")+"',"
					+"LG_SPKFL='"+(chbSPK5.isSelected() ? "Y" : "N")+"',"
					+"LG_MTGFL='"+(rdbMTNG5.isSelected() ? "Y" : "N")+"',"
					+getUSGDTL("LG",'u',null)+" WHERE LG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LG_EMPNO= '"+txtEMPNO.getText().trim()+"' AND LG_LNGDS='"+txtLANG5.getText().trim()+"'";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else
				{
				 	M_strSQLQRY = "INSERT INTO HR_LGMST (LG_CMPCD,LG_EMPNO,LG_LNGDS,LG_REDFL,LG_WRTFL,LG_SPKFL,LG_MTGFL,LG_STSFL,LG_TRNFL,LG_LUSBY,LG_LUPDT) values ("
						+"'"+cl_dat.M_strCMPCD_pbst+"',"
						+"'"+txtEMPNO.getText().trim()+"',"
						+"'"+txtLANG5.getText().trim()+"',"//LG_LNGDS
						+"'"+(chbREAD5.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbWRT5.isSelected() ? "Y" : "N")+"',"
						+"'"+(chbSPK5.isSelected() ? "Y" : "N")+"',"
						+"'"+(rdbMTNG5.isSelected() ? "Y" : "N")+"',"
						+getUSGDTL("LG",'i',"")+")";//EP_LUPDT
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
		cl_dat.M_flgLCUPD_pbst = true;
		}catch(Exception e)
		{
			setMSG("Error in savLGMST ..",'E');
			setMSG(e,"Child.savLGMST");
		}
	}
	private void delFLMST()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			for(int i=0;i<tblDPNDT.getRowCount();i++)
			{
				if (tblDPNDT.getValueAt(i,0).toString().equals("true"))
				{
					M_strSQLQRY = "DELETE FROM HR_FLMST WHERE FL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND FL_EMPNO='"+txtEMPNO.getText().trim()+"' AND FL_MEMNM='"+tblDPNDT.getValueAt(i,1).toString().trim()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(cl_dat.exeDBCMT("delFLMST"))
			{
				setMSG("Data Deleted ..",'N');
				tblDPNDT.clrTABLE();
				flgFMLY=true;
				selFL();
			}
			else
				setMSG("Error in deleting Dependents detials..",'E');

		}
		catch(Exception L_E)
		{	
			setMSG("Error in deleting Dependents detials..",'E');
		}	

	}
/** To modify Family details in HR_FLMST */
	private void modFLMST()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(tblDPNDT.getSelectedRow()!=-1)
				tblDPNDT.editCellAt(tblDPNDT.getSelectedRow(),tblDPNDT.getSelectedColumn());
			for(int i=0;i<tblDPNDT.getRowCount();i++)
			{
				if (!(tblDPNDT.getValueAt(i,1).toString().equals(""))&&cl_dat.M_flgLCUPD_pbst==true)
				{
					// Commented on 05/03/07
					/*if(tblDPNDT.getValueAt(i,3).toString().length()<10)
					{
						setMSG("Enter Date of Birth in dd/mm/yyyy format",'E');
						tbpMAIN.setSelectedIndex(3);
					}*/
					if(flaODLFL[i]&&tblDPNDT.getValueAt(i,0).toString().equals("true"))
					{
						M_strSQLQRY="UPDATE HR_FLMST SET "
							+"FL_MEMNM='"+tblDPNDT.getValueAt(i,1).toString()+"',"  ;//FL_MEMNM	Varchar (10),
						if(tblDPNDT.getValueAt(i,3).toString().length() ==10)
							M_strSQLQRY += "FL_BTHDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDPNDT.getValueAt(i,3).toString().trim()))+"'," ;//FL_BTHDT	Date,
						else
							M_strSQLQRY += "FL_BTHDT= null,";
							M_strSQLQRY += "FL_RELDS='"+tblDPNDT.getValueAt(i,2).toString()+"',"//FL_RELDS	Varchar (15),
							+"FL_QUALN='"+tblDPNDT.getValueAt(i,5).toString()+"',"//FL_QUALN	Varchar (15),
							+"FL_OCCPN='"+tblDPNDT.getValueAt(i,6).toString()+"',"//FL_OCCPN	Varchar (15),
							+"FL_MODDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//FL_MODDT	Date,
							+"FL_PFNFL='"+(tblDPNDT.getValueAt(i,7).toString().equals("true") ? "Y" : "N")+"'," //FL_PFNFL	Varchar(1),
							+"FL_ACNFL='"+(tblDPNDT.getValueAt(i,8).toString().equals("true") ? "Y" : "N")+"',"//FL_ACNFL	Varchar(1),
							+"FL_MDCFL='"+(tblDPNDT.getValueAt(i,9).toString().equals("true") ? "Y" : "N")+"',"//FL_MDCFL	Varchar(1),
							+"FL_DPDFL='"+(tblDPNDT.getValueAt(i,10).toString().equals("true") ? "Y" : "N")+"',"//FL_DPDFL    Varchar(1),
							+"FL_CLDFL='"+(tblDPNDT.getValueAt(i,11).toString().equals("true") ? "Y" : "N")+"',"//FL_CLDFL    Varchar(1),
							+getUSGDTL("FL",'u',null)+" WHERE FL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND FL_EMPNO='"+txtEMPNO.getText().trim()+"' AND FL_MEMNM='"+tblDPNDT.getValueAt(i,1).toString().trim()+"'";//FL_LUPDT    Date
						//System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						flaODLFL[i]=false;
					}
					else if(tblDPNDT.getValueAt(i,0).toString().equals("true"))
					{
						M_strSQLQRY="INSERT INTO HR_FLMST (FL_CMPCD,FL_EMPNO,FL_MEMNM,FL_BTHDT,FL_RELDS,FL_QUALN,FL_OCCPN,FL_MODDT,FL_PFNFL,FL_ACNFL,FL_MDCFL,FL_DPDFL,FL_CLDFL,FL_TRNFL,FL_STSFL,FL_LUSBY,FL_LUPDT) values ("
							+"'"+cl_dat.M_strCMPCD_pbst+"',"
							+"'"+txtEMPNO.getText().trim()+"',"//FL_EMPNO	Varchar (5),
							+"'"+tblDPNDT.getValueAt(i,1).toString()+"'," ;//FL_MEMNM	Varchar (10),
						if(tblDPNDT.getValueAt(i,3).toString().length() ==10)
							M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDPNDT.getValueAt(i,3).toString()))+"'," ;//FL_BTHDT	Date,
						else 
							M_strSQLQRY += " null,";
							M_strSQLQRY += "'"+tblDPNDT.getValueAt(i,2).toString()+"',"//FL_RELDS	Varchar (15),
							+"'"+tblDPNDT.getValueAt(i,5).toString()+"',"//FL_QUALN	Varchar (15),
							+"'"+tblDPNDT.getValueAt(i,6).toString()+"',"//FL_OCCPN	Varchar (15),
							+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"//FL_MODDT	Date,
							+"'"+(tblDPNDT.getValueAt(i,7).toString().equals("true") ? "Y" : "N")+"'," //FL_PFNFL	Varchar(1),
							+"'"+(tblDPNDT.getValueAt(i,8).toString().equals("true") ? "Y" : "N")+"',"//FL_ACNFL	Varchar(1),
							+"'"+(tblDPNDT.getValueAt(i,9).toString().equals("true") ? "Y" : "N")+"',"//FL_MDCFL	Varchar(1),
							+"'"+(tblDPNDT.getValueAt(i,10).toString().equals("true") ? "Y" : "N")+"',"//FL_DPDFL    Varchar(1),
							+"'"+(tblDPNDT.getValueAt(i,11).toString().equals("true") ? "Y" : "N")+"',"//FL_CLDFL    Varchar(1),								 
							+getUSGDTL("FL",'i',"")+")";//FL_LUPDT    Date
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
			}
		}catch(Exception e)
		{
			setMSG(e,"Child.modFLMST");
			cl_dat.M_flgLCUPD_pbst = false;
		}
	}
	
	class MyAdapter extends MouseAdapter
	{
		public void mouseReleased(MouseEvent L_ME)
		{
			try{
			if(L_ME.getSource()==tblDPNDT)
			{
				int row=tblDPNDT.getSelectedRow();
				int col=tblDPNDT.getSelectedColumn();
				if(!(col==-1&&row==-1))
				{
					if(tblDPNDT.getValueAt(0,col).getClass().toString().equals("class java.lang.Boolean"))
					tblDPNDT.setValueAt(new Boolean (true),row,0);
					tblDPNDT.setValueAt(M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)),row,4);
					if(flaODLFL[row])
						tblDPNDT.cmpEDITR[col].setEnabled(false);
					else
						tblDPNDT.cmpEDITR[col].setEnabled(true);
				}
			}
			if(L_ME.getSource()==tblQUAL)
			{
				int row=tblQUAL.getSelectedRow();
				int col=tblQUAL.getSelectedColumn();
				if(!(col==-1&&row==-1))
				{
					if(tblQUAL.getValueAt(0,col).getClass().toString().equals("class java.lang.Boolean"))
					tblQUAL.setValueAt(new Boolean (true),row,0);
					if(flaODLQL[row])
						tblQUAL.cmpEDITR[1].setEnabled(false);
					else
						tblQUAL.cmpEDITR[1].setEnabled(true);
				}
			}
		}catch(Exception e)
			{setMSG(e,"Child.Mousereleased");}
		}
		
	}	
	
	public void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				txtEMPNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtEMPCT"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPCT.setText(L_STRTKN.nextToken());
				lblEMPCT.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtENM"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtENM.setText(L_STRTKN.nextToken());
				txtEMPNO.setText(L_STRTKN.nextToken());
				//System.out.println(cl_dat.M_strHLPSTR_pbst);
				
			}
		}catch(Exception e)
		{
			setMSG(e,"Child.exeHLPOK");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(!M_flgERROR)
			if(M_objSOURC == txtPHOTO)
				setMSG("Press 'F1' to select the file ..",'N');
	}

	public void focusLost(FocusEvent L_FE)
	{
		Object L_SRC=L_FE.getSource();
		if(L_SRC==txtBASIC&&txtBASIC.getText().length()>0)
		{
			
			double basic=Double.parseDouble(txtBASIC.getText()) ;
			if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPADD_pbst))
				getSAL();
			else if(basic>15000 && !strMDAFL.equals("P"))
			{
				strMDALW=txtMDALW.getText();
				String L_STRTMP=Double.toString(basic/12);
				StringTokenizer st=new StringTokenizer(L_STRTMP,".");
				String s1=st.nextToken(),s2=st.nextToken();
				if(s2.length()>1)
					s2=s2.substring(0,2);
				else s2="00";
				L_STRTMP=s1+"."+s2;
				txtMDALW.setText(L_STRTMP);
			}
			else
				txtMDALW.setText(strMDALW);
			//System.out.println("strMDAFL2>>"+strMDAFL);
		}
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtEMPCT.setEnabled(false);
		txtCHEDN.setEnabled(false);txtPFALW.setEnabled(false);txtLTALW.setEnabled(false);txtSAALW.setEnabled(false);txtWSALW.setEnabled(false);txtGRALW.setEnabled(false);txtCRALW.setEnabled(false);
		txtLNSUB.setEnabled(false);txtWKALW.setEnabled(false);txtMDALW.setEnabled(false);
		txtDNALW.setEnabled(false);txtCONVY.setEnabled(false);txtHRALW.setEnabled(false);txtSPALW.setEnabled(false);
		tblDPNDT.cmpEDITR[4].setEnabled(false);
		flgIMGFL=false;
		tblHSTRY.setEnabled(false);
	      if(((String)cl_dat.M_cmbOPTN_pbst.getSelectedItem()).equals(cl_dat.M_OPMOD_pbst))
			btnREMOV.setEnabled(true);
		else
			btnREMOV.setEnabled(false);
		
	}
	
	void clrCOMP()
	{
		super.clrCOMP();
		lblEMPCT.setText("");
		pnlIMAGE.removeAll();
		add(pnlIMAGE,1,6,6,1,this,'L');
		updateUI();
	}
	void updNAME()
	{
	  try
	  {
	    M_rstRSSET = cl_dat.exeSQLQRY("SELECT EP_EMPNO,EP_FULNM FROM HR_EPMST WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EP_FSTNM,'') =''");
	    String L_FSTNM ="";
	    String L_MDLNM ="";
	    String L_LSTNM ="";
	    String L_EMPNO ="";
	    StringTokenizer st;
	    if(M_rstRSSET !=null)
	    {
	        cl_dat.M_flgLCUPD_pbst = true;
    	    while(M_rstRSSET.next())
    	    {
    	        L_EMPNO = nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),"");
        	    st=new StringTokenizer(nvlSTRVL(M_rstRSSET.getString("EP_FULNM"),""),"|");
        		if(st.hasMoreElements())
        			L_FSTNM = st.nextToken();
        		if(st.hasMoreElements())
        			L_MDLNM = st.nextToken();
        		if(st.hasMoreElements())
        			L_LSTNM = st.nextToken();
        		M_strSQLQRY = "UPDATE HR_EPMST SET EP_FSTNM ='"+L_FSTNM +"',"
        		        +"EP_MDLNM ='"+L_MDLNM +"',"
        		        +"EP_LSTNM ='"+L_LSTNM +"'"
        		        +" WHERE EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO ='"+L_EMPNO +"'";
        		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
    	    }
    	    if(cl_dat.exeDBCMT("updNAME"))
    	    {
    	        System.out.println("Data updated");
    	    }
   	    }
	  }
	  catch(Exception L_E)
	  {
	    
	  }

	}
	
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
						return true;
				if(input == txtEMPCT)
				{
					if(hstEMPCT.containsKey(txtEMPCT.getText().toUpperCase()))
					{
						txtEMPCT.setText(txtEMPCT.getText().toUpperCase());
						lblEMPCT.setText(hstEMPCT.get(txtEMPCT.getText().toUpperCase()));
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Employee Category",'E');
						return false;
					}
				}	
				if(input == txtACCRF)
				{
					M_strSQLQRY = " select count(*) CNT from hr_epmst where ep_accrf='"+txtACCRF.getText().trim().toUpperCase()+"' and ep_empno<>'"+txtEMPNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt("CNT")>0)
						{	
							setMSG("Account Ref. no Already Exist",'E');
							return false;
						}	
						else
						{
							txtACCRF.setText(txtACCRF.getText().trim().toUpperCase());
							setMSG("",'N');
							return true;
						}
					}
				}	
				if(input == txtSAPNO)
				{
					M_strSQLQRY = " select count(*) CNT from hr_epmst where ep_sapno='"+txtSAPNO.getText().trim().toUpperCase()+"' and ep_empno<>'"+txtEMPNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt("CNT")>0)
						{	
							setMSG("SAP no Already Exist",'E');
							return false;
						}	
						else
						{
							txtSAPNO.setText(txtSAPNO.getText().trim().toUpperCase());
							setMSG("",'N');
							return true;
						}
					}
				}	
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"Input Verifier");		
			}
			return true;
		}
	}		
 }