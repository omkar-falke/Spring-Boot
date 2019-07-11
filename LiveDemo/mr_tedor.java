import javax.swing.*;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Arrays;
import java.awt.Cursor;

/**<P><FONT color=maroon face="Comic Sans MS" size=4><STRONG>Program Description </STRONG></FONT>  :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>   Delivery Order Entry</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                                        Form for&nbsp;Delivery Order       Entry and Authorisation&nbsp; along with Delivery Scheduling </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Marketing System Enhancement Proposal by Mr. SRD       &amp; Existing D.O. Entry Program      in Foxpro      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\mr_tedor.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_tedor.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>03/02/2004 </TD></TR>  <TR>    <TD>Version </TD>    <TD>2.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD></TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD></TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD><STRONG>Information Details</STRONG></TD>    <TD>      <P align=center><STRONG>Table Name</STRONG> </P></TD>    <TD>      <P align=center><STRONG>Primary Key</STRONG> </P></TD>    <TD>      <P align=center><STRONG>Add</STRONG></P></TD>    <TD>      <P align=center><STRONG>Mod</STRONG></P></TD>    <TD>      <P align=center><STRONG>Del</STRONG></P></TD>    <TD>      <P align=center><STRONG>Enq</STRONG></P></TD></TR>  <TR>    <TD>DO details</TD>    <TD>MR_DOTRN</TD>    <TD>      <P><FONT size=1>DOT_MKTTP,DOT_INDNO,</FONT><FONT       size=1>DOT_PRDCD,</FONT></P>      <P><FONT size=1>DOT_PKGTP</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>DO Delivery schedule</TD>    <TD>MR_DODEL</TD>    <TD>      <P><FONT size=1>DOD_MKTTP,DOD_DORNO,</FONT><FONT       size=1>DOD_PRDCD,</FONT></P>      <P><FONT size=1>DOD_PKGTP,DOD_SRLNO</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>DO qty. in Indent details</TD>    <TD>MR_INTRN</TD>    <TD>      <P><FONT size=1>INT_MKTTP,INT_INDNO,INT_PRDCD</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD>DO details in DOTRN</TD>    <TD>      <P>MR_DOTAM </P>      <P>(only if DO is authorised)</P></TD>    <TD>      <P><FONT size=1>DOT_MKTTP,DOT_INDNO,</FONT></P>      <P><FONT size=1>DOT_PRDCD,DOT_PKGTP</FONT></P></TD>    <TD></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD></TD>    <TD></TD></TR>  <TR>    <TD>DO delivery details in MR_DODEL</TD>    <TD>      <P>MR_DODAM</P>      <P>(only if DO is authorised)</P></TD>    <TD>      <P><FONT size=1>DOD_MKTTP,DOD_DORNO,</FONT><FONT       size=1>DOD_PRDCD,</FONT></P>      <P><FONT size=1>DOD_PKGTP,DOD_SRLNO</FONT></P></TD>    <TD></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD>    <TD></TD>    <TD></TD></TR></TABLE></P>*/
class mr_tedor extends cl_pbase
{	/**Thread to prepare object of mr_rpdor for DO printing  */
	private Thread thrOBJRP;		
									
	private boolean flgREMDS;		
	
	private Object objRPDOR, objRPDOH;
									/**To store old selected item of combo. Used to disable selection change by nouse when combo is not having focus	 */
	private Object objITMRF;
	private Object[] arrHSTKEY;		// Object array for getting hash table key in sorted order
									/**String to store STSFL of D.O. in MOD/ADD/DEL */
	private String strSTSFL="";		/**String array to store list of printers */
	private String [] staPRLST;		/**String for detailed address of parties. To be displayed on Pressing 'F9' from Name of the party */
	private String strAMDNO = "00";	/**String for detailed address of parties. To be displayed on Pressing 'F9' from Name of the party */
	private String strDSRAD,strBYRAD,strCNSAD,strTRPAD;
	
									/**Details of grades from indent transaction Format : PRDDS - INDQT|BASRT|PKGWT */
	//private Hashtable hstPRDCD;	/**Hash table to Store Gradewise DO details entered. Format : PRDDS - Vector of delivery schedule for that grade */
	private Hashtable hstOLDGR;		/**Hash table to Store Gradewise DO details entered. Format : PRDDS - Vector of delivery schedule for that grade */
	private Hashtable hstLADQT;		/**Hash table to Store Gradewise DO details entered. Format : PRDDS - Vector of delivery schedule for that grade */
	private Hashtable hstOLDQT;		/**Hash table to Store Gradewise DO details entered. Format : PRDDS - Vector of delivery schedule for that grade */
	//private Hashtable hstDODTL;		
									/**Base rate of grade under consideration. Initialised on focus gain of required qty in tblDOTRN */
	private Vector<String> vtrOLDDT;		
									/**Button to swap program mode between 'D.O. Authorisation' and 'D.O. Modification'<br>Available in modification only */
	//private JButton btnAUTRN;		/**Button to print D.O. */
	private JButton btnPRINT;		
	private JButton btnRLSE;		// Button for releasing Doc.No. lock
										/**To ACCEPT/AUTHORISE D.O. */
	private JRadioButton rdbAUTH_Y;		/**To reject D.O.*/
	private JRadioButton rdbAUTH_N;	
	private JRadioButton rdbREPT_H;
	private JRadioButton rdbREPT_T;
	private JCheckBox chkAUTOCF;
	private String strZONCD_EXP = "12";		// Export Zone
	private String strSALTP_EXP = "12";		// Export Sale Type
	private String strSALTP_DEX = "03";		// Deemed Export Sale Type
	private String strSALTP_STF = "04";		// Stock Transfer Sale Type
	private double dblTOTQT_SCH;
	private double dblTOTQT_GRD;
	
	
	///	private JCheckBox chkINDDSP;
	
									/**Label to display grade with red color for which delivery schedule is being entered.  */
	private JLabel lblPRDCD;		/**Indent No */
	private JLabel lblFRTRT;		
	private JLabel lblSTSDS;		
	private JLabel lblAMDNO;
	private JLabel lblAMDDT;
	private JLabel lblAUTBY;
	private JLabel lblAUTDT;
	private JLabel lblDSTCD;	
	private JLabel lblDSTDS;	
									/**Indent No */
	private JTextField txtINDNO;
	private JTextField txtDORNO;
	private JTextField txtLRYNO;
	private JTextField txtREMDS;
	private JTextField txtDSRNM;
	private JTextField txtDLCCD;
	private JTextField txtFRTRT;
	private JLabel lblDLCDS;
	//private JTextField txtDLCDS;	/**  */
	private JLabel lblDSRTP;	/** Distributor Code */
	private JTextField txtDSRCD;	/** Buyer Code */
	private JTextField txtBYRCD;	/** Consignee Code */
	private JTextField txtCNSCD;	/** Transporter Code */
	private JTextField txtTRPCD;	/** Buyer Name */
	private JTextField txtBYRNM;	/** Comsignee Name */
	private JTextField txtCNSNM;	/** Transtporter Name */
	private JTextField txtTRPNM;
	private JTextField txtFAXNO;
	private JPanel pnlINDDSP;
	
	private boolean flgBASRT_ENTER = false;

	private TxtNumLimit txtBASRT;
	private TxtNumLimit txtDORQT;
	private TxtNumLimit txtQLHQT;
	private TxtLimit txtDELTP;


									/**Combobox for market types. Populated in constructor */
	private JComboBox cmbMKTTP;
	private JComboBox cmbSALTP;		/**Payment Type	 */
	//private JComboBox cmbSCHTP;
	private JComboBox cmbDTPCD;		/** Combo for Mode of transport Populated in Constructor */
	private JComboBox cmbMOTCD;		
									/** Table for delivery schedule of a grade */
	private cl_JTable tblDODEL;		/** Table for grade details in an order */
	private cl_JTable tblDOTRN;		
	private cl_JTable tblINDDSP;		
									/** Constant for column index in Table for Grade details (tblDOTRN)	 */
	private int intTB1_CHKFL = 0;
	private int intTB1_SRLNO = 1;
	private int intTB1_PRDCD = 2;
	private int intTB1_PRDDS = 3;
	private int intTB1_PKGDS = 4;
	private int intTB1_INDQT = 5;
	private int intTB1_BASRT = 6;
	private int intTB1_BALQT = 7;
	private int intTB1_DORQT = 8;
	private int intTB1_DORPK = 9;
	private int intTB1_DELTP = 10;
	private int intTB1_STKQT = 11;
	private int intTB1_QLHQT = 12;
	private int intTB1_PKGTP = 13;
										/** Constant for column index in Table for DO details (tblDODEL)	 */
    private int intTB2_CHKFL = 0;
    private int intTB2_PRDCD = 1;
    private int intTB2_PRDDS = 2;
	private int intTB2_DSPDT = 3;
	private int intTB2_DORQT = 4;
	private int intTB2_SRLNO = 5;
	private int intTB2_LADQT = 6;
	private int intTB2_PKGTP = 7;
	
	private int intTB3_INDNO = 1;
	private int intTB3_BKGDT = 2;
	private int intTB3_AMDNO = 3;
	private int intTB3_AMDDT = 4;
	private int intTB3_PRDDS = 5;
	private int intTB3_REQQT = 6;
	private int intTB3_INDQT = 7;
	private int intTB3_BKGBY = 8;
	private int intTB3_PRTNM = 9;
		

	private String strYREND = "31/03/2009";
    private String strYRDGT;
    private String strWHRSTR;
    private String strMKTTP;
	private String strDORDT;
	private String strMKTTP_CAP = "03";		// Captive Consumption Market Type
	private boolean flgAUTRN = false;
	private boolean flgCHK_EXIST = false;
	private boolean flgFIRSTREC = true;

									/** Variables for Code Transaction Table
									 */
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	private String strPRDCD;		


								/**Current selected row of grade detail table */
	private int intGRROW;		/**Base rate of grade under consideration. Initialised on focus gain of required qty in tblDOTRN */
	private float fltBASRT;		/**Rquired qty of grade under consideration Initialised on focus gain of cmbSCHTP */

											/** Array elements for hstCDTRN */
	private int intCDTRN_TOT = 9;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
											/** Array elements for hstINTRN  & hstINTRN_1*/
	private int intINTRN_TOT = 12;			
    private int intAE_INT_SRLNO = 0;		
    private int intAE_INT_PRDCD = 1;		
    private int intAE_INT_PKGDS = 2;		
    private int intAE_INT_PRDDS = 3;		
    private int intAE_INT_INDQT = 4;		
    private int intAE_INT_BASRT = 5;		
    private int intAE_INT_BALQT = 6;		
    private int intAE_INT_STKQT = 7;		
    private int intAE_INT_PKGTP = 8;		
    private int intAE_INT_DORQT = 9;		
    private int intAE_INT_INVQT = 10;	
	private int intAE_INT_FCMQT = 11;
											/** Array elements for hstGRTRN */
	private int intGRTRN_TOT = 11;			
    private int intAE_GR_PRDCD = 0;		
    private int intAE_GR_QLHQT = 1;		
										/** Array elements for hstDOTRN & hstDOTRN_1 */
	private int intDOTRN_TOT = 5;
    private int intAE_DOT_DORQT = 0;		
    private int intAE_DOT_DORPK = 1;		
    private int intAE_DOT_DELTP = 2;		
    private int intAE_DOT_LADQT = 3;		
    private int intAE_DOT_INVQT = 4;	
    
    /** Array elements for Grade Details */
	private int intGRDTL_TOT = 2;
    private int intAE_GRD_DORQT = 0;		
    private int intAE_GRD_INVQT = 1;		
											/** Array elements for hstDODEL */
	private int intDODEL_TOT = 8;
    private int intAE_DOD_CHKFL = 0;		
    private int intAE_DOD_PRDDS = 1;		
    private int intAE_DOD_DSPDT = 2;		
    private int intAE_DOD_DORQT = 3;		
    private int intAE_DOD_LADQT = 4;		
    private int intAE_DOD_SRLNO = 5;		
    private int intAE_DOD_PRDCD = 6;		
    private int intAE_DOD_PKGTP = 7;
    private int intINDEL_TOT = 6;
    private int intAE_IND_SRLNO = 0;		
    private int intAE_IND_DSPDT = 1;		
    private int intAE_IND_INDQT = 2;		
    private int intAE_IND_PRDCD = 3;		
    private int intAE_IND_PKGTP = 4;	
    private int intAE_IND_DELTP = 5;	
											/** Array elements for Product Master details */
	private int intPRMST_TOT = 3;
    private int intAE_PR_PRDCD = 0;		
    private int intAE_PR_PRDDS = 1;		
    private int intAE_PR_AVGRT = 2;		
	private Vector<String> vtrSALTP;
	private Vector<String> vtrMKTTP;
	private Vector<String> vtrMOTCD;
	private Vector<String> vtrDTPCD;
	
	private Hashtable<String,String[]> hstCDTRN;
	private Hashtable<String,String> hstCODDS;
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstINTRN;
	private Hashtable<String,String[]> hstINTRN_1;
	private Hashtable<String,String[]> hstGRTRN;
	private Hashtable<String,String[]> hstDOTRN;
	private Hashtable<String,String[]> hstDOTRN_1;
	///private Hashtable hstDODEL;
	///private Hashtable hstINDEL;
	private Hashtable<String,String> hstINSRL;

	private Hashtable<String,String> hstPRDDT;
	String strFRTCT_DTP, strINSCT_DTP;

	/**Constructor for the form<br>
	 * Retrieves market type/delivery type/payment type and transport type  options from CO_CDTRN and populates respective combos.<br>
	 * Starts thread for retrieving Distributor details along with curren year series for INDNO	 */
	mr_tedor()
	{
		super(2);
		try
		{
			vtrSALTP = new Vector<String>();
			vtrMKTTP = new Vector<String>();
			vtrMOTCD = new Vector<String>();
			vtrDTPCD = new Vector<String>();
	
			hstPRMST = new Hashtable<String,String[]>();
			hstCDTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstINTRN = new Hashtable<String,String[]>();
			hstINTRN_1 = new Hashtable<String,String[]>();
			hstGRTRN = new Hashtable<String,String[]>();
			hstDOTRN = new Hashtable<String,String[]>();
			hstDOTRN_1 = new Hashtable<String,String[]>();
			///hstDODEL = new Hashtable();
			///hstINDEL = new Hashtable();
			hstINSRL = new Hashtable<String,String>();
			chkAUTOCF = new JCheckBox("Auto C/F");

			hstCDTRN.clear();
            hstCODDS.clear();
			crtCDTRN("'SYSCOXXDST','STSMRXXDOR','MSTCOXXMKT','D"+cl_dat.M_strCMPCD_pbst+"MR01DOR','D"+cl_dat.M_strCMPCD_pbst+"MR02DOR','D"+cl_dat.M_strCMPCD_pbst+"MR03DOR','D"+cl_dat.M_strCMPCD_pbst+"MR04DOR','D"+cl_dat.M_strCMPCD_pbst+"MR05DOR','D"+cl_dat.M_strCMPCD_pbst+"MR07DOR','D"+cl_dat.M_strCMPCD_pbst+"MR12DOR','SYSMRXXDTP', 'SYSMR01MOT','SYSFGXXPKG','SYSMR00SAL'","",hstCDTRN);
			crtPRMST();
			
			// Default Values
			strFRTCT_DTP = "02";		// DTPCD for freight    (FOR)
			strINSCT_DTP = "04";		// DTPCD for Insurance  (CIF)
			
			setMatrix(20,10);
			//ADDING BASE DETAILS OF ORDER
			add(new JLabel("Sale Type"),1,1,1,1,this,'L');
			add(cmbSALTP=new JComboBox(),1,2,1,1,this,'L');
			add(new JLabel("Market Type"),1,3,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),1,4,1,1,this,'L');
			add(new JLabel("Order No."),1,5,1,1,this,'L');
			add(txtINDNO=new TxtLimit(9),1,6,1,1,this,'L');
			add(new JLabel("D.O. No."),1,7,1,1,this,'L');
			add(txtDORNO=new TxtLimit(8),1,8,1,1,this,'L');
			//add(txtDLCDS=new TxtLimit(45),2,8,1,3,this,'L');
			add(rdbAUTH_Y=new JRadioButton("Authorise"),1,9,1,1,this,'L');
			add(rdbAUTH_N=new JRadioButton("DoNot Authorise"),1,10,1,1,this,'R');
			ButtonGroup btgAUTH=new ButtonGroup();
			btgAUTH.add(rdbAUTH_Y);btgAUTH.add(rdbAUTH_N);


			add(rdbREPT_H = new JRadioButton("HTML"),17,9,1,1,this,'L');
			add(rdbREPT_T = new JRadioButton("TEXT"),17,10,1,1,this,'L');

			add(new JLabel("Buyer"),2,1,1,1,this,'L');
			add(txtBYRCD=new TxtLimit(5),2,2,1,1,this,'L');
			add(txtBYRNM=new TxtLimit(45),2,3,1,6,this,'L');
			add(new JLabel("Loading At"),2,9,1,1,this,'L');
			add(txtDLCCD=new TxtLimit(3),2,10,1,1,this,'L');
			
			add(new JLabel("Consignee"),3,1,1,1,this,'L');
			add(txtCNSCD=new TxtLimit(5),3,2,1,1,this,'L');
			add(txtCNSNM=new TxtLimit(45),3,3,1,6,this,'L');
			add(lblDLCDS=new JLabel(""),3,9,1,2,this,'L');
			
			add(new JLabel("Distr."),4,1,1,0.75,this,'L');
			add(lblDSRTP=new JLabel(" "),4,1,1,0.25,this,'R');
			add(txtDSRCD=new TxtLimit(5),4,2,1,1,this,'L');
			add(txtDSRNM=new TxtLimit(45),4,3,1,6,this,'L');
			add(lblFRTRT=new JLabel("Frt.Rate"),4,9,1,1,this,'L');
			add(txtFRTRT=new TxtNumLimit(10.3),4,10,1,1,this,'L');

			add(new JLabel("Transporter"),5,1,1,1,this,'L');
			add(txtTRPCD=new TxtLimit(5),5,2,1,1,this,'L');
			add(txtTRPNM=new TxtLimit(45),5,3,1,6,this,'L');
			add(new JLabel("Lorry No."),5,9,1,1,this,'L');
			add(txtLRYNO=new TxtLimit(11),5,10,1,1,this,'L');
			
			//add(new JLabel("Destination"),5,1,1,1,this,'L');
			
			

			add(new JLabel("Delivery"),6,1,1,1,this,'L');
			add(cmbDTPCD=new JComboBox(),6,2,1,1,this,'L');
			add(new JLabel("Trp.FAX No."),6,4,1,1,this,'L');
			add(txtFAXNO=new TxtLimit(45),6,5,1,2,this,'L');
			add(new JLabel("Transport"),6,8,1,1,this,'L');
			add(cmbMOTCD=new JComboBox(),6,9,1,2,this,'L');
			
			
			add(new JLabel("Remark"),7,1,1,1,this,'L');
			add(txtREMDS=new TxtLimit(200),7,2,1,9,this,'L');
			
		///	chkINDDSP = new JCheckBox("Unauthorised Cust.Orders");
		///	add(chkINDDSP,14,6,1,3,this,'L');

			
			add(lblSTSDS=new JLabel(),13,7,1,3,this,'L');
			add(new JLabel("Auth.By :"),15,7,1,1,this,'L');
			add(lblAUTBY=new JLabel(""),15,8,1,1,this,'L');
			add(new JLabel("Date    :"),15,9,1,1,this,'L');
			add(lblAUTDT=new JLabel(""),15,10,1,1,this,'L');
			add(new JLabel("Amd.No. :"),16,7,1,1,this,'L');
			add(lblAMDNO=new JLabel(""),16,8,1,1,this,'L');
			add(new JLabel("Date    :"),16,9,1,1,this,'L');
			add(lblAMDDT=new JLabel(""),16,10,1,1,this,'L');
			add(lblDSTDS=new JLabel(""),17,7,1,2,this,'L');
			add(lblDSTCD=new JLabel(""),17,10,1,1,this,'L');

			

			ButtonGroup btgREPT=new ButtonGroup();
			btgREPT.add(rdbREPT_H);btgREPT.add(rdbREPT_T);

			add(chkAUTOCF,13,4,1,2,this,'L');


			



			
			add(btnRLSE = new JButton("Release"),18,7,1,1.5,this,'L');
			add(btnPRINT=new JButton("Print"),18,9,1,1.5,this,'L');
			rdbREPT_T.setSelected(true);
			

			chkAUTOCF.setSelected(true);
	///		chkINDDSP.setSelected(false);
			
			//ADDING TABLE FOR GRADE DETAILS			
			tblDOTRN=crtTBLPNL1(this,new String[]{"FL","Sr.No.","Prd.Cd.","Grade","Pkg Tp.","Ind.Qty","B.Rate","Bal.Qty","DO Qty","Pkgs","Del.Type","Stock","Qlt.H","pkgtp"},20,8,1,5,10,new int[]{20,30,80,90,75,75,75,75,75,30,30,75,75,10},new int[]{0});
			tblDOTRN.setInputVerifier(new TBLINPVF());
			add(new JLabel("Del.Sch.For: "),13,1,1,1,this,'L');
			add(lblPRDCD=new JLabel(""),13,2,1,2,this,'L');
			lblPRDCD.setForeground(Color.red);lblPRDCD.setAlignmentX(1.0f);
		//	tblDOTRN.setCellEditor(intTB1_BASRT,new TxtNumLimit(8.2));
		//	tblDOTRN.setCellEditor(intTB1_DORQT,new TxtNumLimit(8.3));

			tblDOTRN.setCellEditor(intTB1_BASRT,txtBASRT=new TxtNumLimit(10.2));
			tblDOTRN.setCellEditor(intTB1_DORQT,txtDORQT=new TxtNumLimit(10.3));
			tblDOTRN.setCellEditor(intTB1_QLHQT,txtQLHQT=new TxtNumLimit(10.3));
			tblDOTRN.setCellEditor(intTB1_DELTP,txtDELTP=new TxtLimit(1));

			txtBASRT.addFocusListener(this);
			txtBASRT.addKeyListener(this);
			txtDORQT.addFocusListener(this);
			txtDORQT.addKeyListener(this);
			txtQLHQT.addFocusListener(this);
			txtQLHQT.addKeyListener(this);
			txtDELTP.addFocusListener(this);
			txtDELTP.addKeyListener(this);
			//	ADDING TABLE FOR DELIVERY SCHEDULE DETAILS
		//	tblDODEL=crtTBLPNL1(this,new String[]{"FL","Sr.No.","Date","Qty.","Prdcd","Pkgtp"},20,14,1,5,4,new int[]{20,40,120,100,10,10},new int[]{0});
			tblDODEL=crtTBLPNL1(this,new String[]{"FL","Code","Grade","Date","Sch.Qty.","Sr.No.","Dsp. Qty","Pkgtp"},20,14,1,5,5.5,new int[]{20,80,90,80,80,40,10,10,10},new int[]{0});
			tblDODEL.setInputVerifier(new TBLINPVF());
			//tblDODEL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TxtDate txtDODT=new TxtDate();txtDODT.addKeyListener(this);
			TxtNumLimit txtDOQT=new TxtNumLimit(10.3);txtDOQT.addKeyListener(this);txtDOQT.addFocusListener(this);
			tblDODEL.setCellEditor(intTB2_DSPDT,txtDODT);tblDODEL.setCellEditor(intTB2_DORQT,txtDOQT);
			INPVF objMKIPV=new INPVF();

			//REGISTERING INPUTVERIFIER for COMPONENTS OTHER THAN JLABEL
			//ADDING ITEM LISTENER TO COMBO's TO DISABLE USAGE WHEN NOT HAVING FOCUS ON IT
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objMKIPV);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
				}
			}
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "0" : "9";
			setCMBBOX();
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			thrOBJRP=new Thread(this);thrOBJRP.start();
		}
		catch (Exception e)
		{setMSG(e,"Child.Constructor");}
	}
	



	
	/** Generating string for Insertion Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 */
	private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
	try {
		//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
		if (LP_FLDTP.equals("C"))
			 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
	 	else if (LP_FLDTP.equals("N"))
	         return   nvlSTRVL(LP_FLDVL,"0") + ",";
	 	else if (LP_FLDTP.equals("D"))
			 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
		else if (LP_FLDTP.equals("T"))
			 return   (LP_FLDVL.length()>=5) ? ("'"+LP_FLDVL.trim()+"',") : "null,";
		else return " ";
	        }
	    catch (Exception L_EX) 
		{setMSG("Error in setINSSTR : "+L_EX,'E');}
	return " ";
	}
		



	/** Generating string for Updation Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 */
	private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 
	{
		try 
		{
			//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
			if (LP_FLDTP.equals("C"))
				 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
		 	else if (LP_FLDTP.equals("N"))
		         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
		 	else if (LP_FLDTP.equals("D"))
				 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
			else if (LP_FLDTP.equals("T"))
			 return   (LP_FLDNM + " = "+ (LP_FLDVL.length()>=5 ? ("'"+LP_FLDVL.trim()+"',") : "null,"));
			else return " ";
		}
		catch (Exception L_EX) 
			{setMSG("Error in setUPDSTR : "+L_EX,'E');}
		return " ";
	}
    /** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
		if (LP_FLDTP.equals("C"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
			//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
		else if (LP_FLDTP.equals("N"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
		else if (LP_FLDTP.equals("D"))
			return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
		else if (LP_FLDTP.equals("T"))
		    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
		else 
			return " ";
		}
		catch (Exception L_EX)
		{setMSG(L_EX,"getRSTVAL");}
	return " ";
	} 
	/**
	 */
	private void setCMBBOX()
	{
		try
		{
			//RETRIEVING MARKET TYPE/DELIVERY TYPE/PAYMENT TYPE AND TRANSPORT TYPE  OPTIONS FROM CDTRN AND PUTTING IT IN RESPECTIVE COMBOS.
			setCDLST(vtrMOTCD,"SYSMR01MOT","CMT_CODDS");
			setCDLST(vtrDTPCD,"SYSMRXXDTP","CMT_CODDS");

			setCMBVL(cmbMOTCD,vtrMOTCD);
			setCMBVL(cmbDTPCD,vtrDTPCD);
		}
		catch (Exception e)
		{setMSG(e,"setCMBBOX");}
	}


	/**  To set default status during intial display
	 */	
	private void setDFTSTS()
	{
		txtFRTRT.setText("0.00");
		txtFRTRT.setVisible(false);
		lblFRTRT.setVisible(false);
	///	setDFTSTS1("AUT","0");
		rdbAUTH_Y.setSelected(false);
		rdbAUTH_N.setSelected(false);
		rdbAUTH_Y.setVisible(false);
		rdbAUTH_N.setVisible(false);
		if(flgAUTRN)
		{
		    ///setDFTSTS1("AUT","1");
		    rdbAUTH_Y.setVisible(true);
		    rdbAUTH_N.setVisible(true);
		}
	}	
	

	/**To disable Combo Selection change if combo is not having focus 
	 * */	
	public void itemStateChanged(ItemEvent L_IE)
	{
		super.itemStateChanged(L_IE);
		try
		{
			M_objSOURC=L_IE.getSource();
			//KEEP cmbOPTN ACCESSIBLE ALWAYS			
			//KEEP cmbOPTN ACCESSIBLE ALWAYS			
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst || M_objSOURC!=cl_dat.M_cmbSBSL1_pbst || M_objSOURC!=cl_dat.M_cmbSBSL2_pbst || M_objSOURC!=cl_dat.M_cmbSBSL3_pbst)
				objITMRF=null;
			//OTHER WISE REMEMBER EARLIER SELECTED ITEM OF COMBO
			else if(M_objSOURC instanceof JComboBox)
			{
				if(L_IE.getStateChange()==2)
				{
					if(((JComponent)M_objSOURC).hasFocus())
						objITMRF=null;
					else
					{
						objITMRF=L_IE.getItem();
						((JComboBox)M_objSOURC).removeItemListener(this);
					}
				}
				else
					objITMRF=null;
			}
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				//cmbSALTP.setSelectedItem(getCDTRN("SYSMR00SAL"+M_strSBSCD.substring(2,4),"CMT_CODDS",hstCDTRN));
				//setCMBMKTTP();
				//cmbMKTTP.requestFocus();	
			}
			/*if(M_objSOURC==cmbSALTP)
			{
				inlCDTRN();
				crtCDTRN("'MSTCOXXMKT'"," and CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and CMT_CHP02 like '%"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"%'",hstCDTRN);
				setCDLST(vtrMKTTP,"MSTCOXXMKT","CMT_CODDS");
				setCMBVL(cmbMKTTP,vtrMKTTP);
				cmbMKTTP.requestFocus();
			}*/
			if(M_objSOURC==cmbMKTTP)
			{
				strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
				setDFTSTS();
			}

		}
		catch(Exception e){setMSG(e,"Child.itemStateChanged");}
	}

	/** Returning Delivery Type from grade table
	 *  for specified product code & package type combination
	 */
	private String getDOT_DELTP(String LP_PRDCD, String LP_PKGTP)
	{
		String L_strDOT_DELTP = "";
		if((LP_PRDCD+LP_PKGTP).length()!=12)
			return L_strDOT_DELTP;
		
		try
		{
			for(int intDOTCTR=0;intDOTCTR<tblDOTRN.getRowCount();intDOTCTR++)
			{
				if(tblDOTRN.getValueAt(intDOTCTR,intTB1_PRDCD).toString().length()==10)
				{
					if(LP_PRDCD.equals(tblDOTRN.getValueAt(intDOTCTR,intTB1_PRDCD).toString()) && LP_PKGTP.equals(tblDOTRN.getValueAt(intDOTCTR,intTB1_PKGTP).toString()))
					{L_strDOT_DELTP = tblDOTRN.getValueAt(intDOTCTR,intTB1_DELTP).toString(); break;}
				}
			}
		}
		catch(Exception e)
			{setMSG(e,"getDOT_DELTP");}
		return L_strDOT_DELTP;		
	}
	
/*private void setCMBMKTTP()
{
	try
	{
		//if(cmbSALTP.getItemCount()>0 && cmbSALTP.getSelectedIndex()>0)
		if(cmbSALTP.getItemCount()>0)
		{
			inlCDTRN();
			crtCDTRN("'MSTCOXXMKT'"," and CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and CMT_CHP02 like '%"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"%'",hstCDTRN);
			setCDLST(vtrMKTTP,"MSTCOXXMKT","CMT_CODDS");
			setCMBVL(cmbMKTTP,vtrMKTTP);
		}
	}
	catch(Exception e)
		{setMSG(e,"setCMBMKTTP");}
	}
	*/

private void setCMBMKTTP()
{
	try
	{
		inlCDTRN("MKT");
		String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls + cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";				
		if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
			crtCDTRN("'MSTCOXXMKT'"," and  CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and  CMT_CHP02 like '%"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"%'",hstCDTRN);
		crtCDTRN("'D"+cl_dat.M_strCMPCD_pbst+"MR01IND','D"+cl_dat.M_strCMPCD_pbst+"MR02IND','D"+cl_dat.M_strCMPCD_pbst+"MR03IND','D"+cl_dat.M_strCMPCD_pbst+"MR04IND','D"+cl_dat.M_strCMPCD_pbst+"MR05IND','D"+cl_dat.M_strCMPCD_pbst+"MR07IND','D"+cl_dat.M_strCMPCD_pbst+"MR12IND'",L_strDSTCHK+" and  SUBSTRING(CMT_CODCD,4,1)='"+strYRDGT+"' and CMT_CHP01='"+M_strSBSCD.substring(0,2)+"'",hstCDTRN);
		setCDLST(vtrMKTTP,"MSTCOXXMKT","CMT_CODDS");
		setCMBVL(cmbMKTTP,vtrMKTTP);
	}
	catch(Exception e){setMSG(e,"setCMBMKTTP");}
}


private void setCMBSALTP()
{
	try
	{
		inlCDTRN("SAL");
		String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls + cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";				
		crtCDTRN("'SYSMR00SAL'"," and  CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and  CMT_CHP02 like '%"+M_strSBSCD.substring(2,4)+"%'",hstCDTRN);
		setCDLST(vtrSALTP,"SYSMR00SAL","CMT_CODDS");
		setCMBVL(cmbSALTP,vtrSALTP);
	}
	catch(Exception e){setMSG(e,"setCMBMKTTP");}
}
	
	/** Returning D.O.qty from grade table
	 *  for specified product code & package type combination
	 */
	private float getDOT_DORQT(String LP_PRDCD, String LP_PKGTP)
	{
		float L_fltDOT_DORQT1 = 0.00f;
		if((LP_PRDCD+LP_PKGTP).length()!=12)
			return L_fltDOT_DORQT1;
		
		try
		{
			//System.out.println("getDOT_DORQT : "+LP_PRDCD+" / "+LP_PKGTP);
			for(int intDOTCTR=0;intDOTCTR<tblDOTRN.getRowCount();intDOTCTR++)
			{
				if(tblDOTRN.getValueAt(intDOTCTR,intTB1_PRDCD).toString().length()==10)
				{
					if(LP_PRDCD.equals(tblDOTRN.getValueAt(intDOTCTR,intTB1_PRDCD).toString()) && LP_PKGTP.equals(tblDOTRN.getValueAt(intDOTCTR,intTB1_PKGTP).toString()))
						L_fltDOT_DORQT1 += Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(intDOTCTR,intTB1_DORQT).toString(),"0.00"));
				}
			}
		}
		catch(Exception e)
			{setMSG(e,"getDOT_DORQT");}
		return L_fltDOT_DORQT1;		
	}
	
	
	
	/**<b>TASKS :</b><br>
	 * &nbsp&nbsp&nbspSource=Combobox except cmbOPTN : Disable user selection if combo is not having focus<br>
	 * &nbsp&nbsp&nbspSource=cmbOPTN : Display respective Components<br>	 
	 * &nbsp&nbsp&nbspSource=btnAUTRN : Swap between Modification and Authorisation mode<br>
	 * &nbsp&nbsp&nbspSource=btnPRINT : Execute report, display printer selection and print the report<br>	 	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC!=cl_dat.M_cmbOPTN_pbst&&(!((JComponent)M_objSOURC).hasFocus())&&M_objSOURC instanceof JComboBox&&objITMRF!=null)
			{//DISABLE MOUSE USAGE IF NOT HAVING FOCUS ON IT
				((JComboBox)M_objSOURC).setSelectedItem(objITMRF);
				objITMRF=null;
				JOptionPane.showMessageDialog((Component)M_objSOURC,"Invalid Operation","ERROR !",JOptionPane.ERROR_MESSAGE);
				((JComboBox)M_objSOURC).addItemListener(this);
			}
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{//SETTING ENTRY FIELDS ENABLED
				setCMBSALTP();
				
			flgAUTRN = false;
			if(M_staUSRRT[0][M_intAE_AUTFL].equals("Y"))		
			    flgAUTRN=true;
			txtDORNO.setEnabled(true);
			//SETTING Authorisation button and resp. radiobuttons' status
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				if(flgAUTRN)
				{
					///setDFTSTS1("AUT","1");
					rdbAUTH_Y.setVisible(true);
        			rdbAUTH_N.setVisible(true);
        			rdbAUTH_Y.setSelected(false);
        			rdbAUTH_N.setSelected(false);
				}
			cmbSALTP.setEnabled(true);cmbSALTP.setVisible(true);
			cmbSALTP.requestFocus();
			}
			else if(M_objSOURC==cmbSALTP && (cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0))
			{
				setCMBMKTTP();
				//inlCDTRN();
				//String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls||cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";
				//if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
				//	crtCDTRN("'MSTCOXXMKT'"," and  CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and  CMT_CHP02 like '%"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"%'",hstCDTRN);
				//crtCDTRN("'D"+cl_dat.M_strCMPCD_pbst+"MR01IND','D"+cl_dat.M_strCMPCD_pbst+"MR02IND','D"+cl_dat.M_strCMPCD_pbst+"MR03IND','D"+cl_dat.M_strCMPCD_pbst+"MR04IND','D"+cl_dat.M_strCMPCD_pbst+"MR05IND','D"+cl_dat.M_strCMPCD_pbst+"MR12IND'",L_strDSTCHK+" and  SUBSTRING(CMT_CODCD,4,1)='"+strYRDGT+"' and CMT_CHP01='"+M_strSBSCD.substring(0,2)+"'",hstCDTRN);

				cmbMKTTP.setEnabled(true);cmbMKTTP.setVisible(true);
				cmbMKTTP.requestFocus();
			}
			/*else if(M_objSOURC==chkINDDSP)
			{
				exeINDDSP();
			}*/
			else if(M_objSOURC==txtINDNO)
			{
			}
			else if(M_objSOURC==btnPRINT)
			{
				if(thrOBJRP!=null)
					thrOBJRP.join();
				if (rdbREPT_T.isSelected())
					exeREPT_T();				
				else if (rdbREPT_H.isSelected())
					exeREPT_H();				
			}
			else if(M_objSOURC==btnRLSE)
			{
				exeRLSE_DO();
			}
		}catch(Exception e)
		{setMSG(e,"Child.actionPerformed");}
	}
	/** D.O. release
	 * If the document generation series is locked and nobody is entering D.O.transaction
	 * from some other PC, this option is available to release the document generation lock, externally
	 * 
	 */	
	private void exeRLSE_DO()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
			String L_strSQLQRY = "update co_cdtrn set cmt_chp01='Y' where cmt_cgmtp='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'MR"+strMKTTP+"DOR' and cmt_codcd = '"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+strMKTTP+"'";
			//System.out.println(L_strSQLQRY);
			cl_dat.exeSQLUPD(L_strSQLQRY,"");
			if(cl_dat.exeDBCMT("exeRLSE_DO"))
				setMSG("Released",'N');
			L_strSQLQRY = "select cmt_codcd,cmt_ccsvl from co_cdtrn  where cmt_cgmtp='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'MR"+strMKTTP+"DOR' and cmt_codcd = '"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+strMKTTP+"'";
			//System.out.println(L_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			String L_strDORNO = "xxxxxxxx";
			if(chkDORNO())
				L_strDORNO = txtDORNO.getText();
			L_strSQLQRY = "select count(*) DOT_RECCT from mr_dotrn where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND dot_mkttp = '"+strMKTTP+"' and dot_dorno = '"+L_strDORNO+"'";
			//System.out.println(L_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			int L_intRECCT = 0;
			if(L_rstRSSET != null && L_rstRSSET.next())
				{L_intRECCT = L_rstRSSET.getInt("DOT_RECCT"); L_rstRSSET.close();}
			if(L_intRECCT == 0)
			{
				L_strSQLQRY = "delete from MR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP = '"+strMKTTP+"' and RM_TRNTP = 'DO' and RM_DOCNO = '"+L_strDORNO+"'";
				//System.out.println(L_strSQLQRY);
				cl_dat.exeSQLUPD(L_strSQLQRY,"");
			}
			L_strSQLQRY = "update co_cdtrn set cmt_chp01='Y' where cmt_cgmtp='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'MR"+strMKTTP+"DOR' and cmt_codcd = '"+cl_dat.M_strFNNYR1_pbst.substring(3,4)+strMKTTP+"'";
			//System.out.println(L_strSQLQRY);
			cl_dat.exeSQLUPD(L_strSQLQRY,"");
			if(cl_dat.exeDBCMT("exeRLSE_DO"))
				setMSG("Released",'N');
		}
		catch(Exception e)
		{setMSG(e,"exeRLSE_DO");}
	}
	/** D.O. printing in Text Format (mr_rpdor)
	 */	
	private void exeREPT_T()
	{
				//objRPDOR.M_strSBSLS = M_strSBSLS;
				//objRPDOR.staPRLST = staPRLST;
				mr_rpdor objRPDOR = new mr_rpdor(1, M_strSBSLS);
				staPRLST=objRPDOR.getPRINTERS();

				objRPDOR.txtFRDNO.setText(txtDORNO.getText());
				objRPDOR.txtTODNO.setText(txtDORNO.getText());

				objRPDOR.txtPRDTP.setText(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()));
				JComboBox L_cmbTEMP=new JComboBox(staPRLST);
				
				//L_cmbTEMP.insertItemAt("Select",0);

				JOptionPane.showConfirmDialog( this,L_cmbTEMP,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
				objRPDOR.strLFTMRG = "     ";
				objRPDOR.M_cmbDESTN=L_cmbTEMP;
				objRPDOR.flgOUTPRT = true;
				objRPDOR.M_cmbDESTN.setSelectedIndex(L_cmbTEMP.getSelectedIndex());
				if(M_strSBSCD.equals("511600"))
					objRPDOR.rdbPLNPN.setSelected(true);
				else
					objRPDOR.rdbPREPN.setSelected(true);
				objRPDOR.exePRINT();
	}
	/** D.O. printing in HTML Format (mr_rpdoh)
	 */	
	private void exeREPT_H()
	{
		mr_rpdoh objRPDOH=new mr_rpdoh(M_strSBSLS);

		objRPDOH.txtFMDOR.setText(txtDORNO.getText());

		objRPDOH.txtPRDTP.setText(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()));
		cl_dat.M_cmbOPTN_pbst.setSelectedItem(cl_dat.M_OPFAX_pbst);
		objRPDOH.M_txtDESTN.setText(txtFAXNO.getText());
		objRPDOH.flgOUTPRT = true;
		
		objRPDOH.exePRINT();
	}
	/**User friendly messagees<br>
	 * Source = D.O. qty. in tblDODEL : Calculate remaining qty of do schedule and display it selected<br>
	 * Source = tblDOTRN : Remember current row selected number	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
		 	this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(!M_flgERROR)
			{//USER FRIENLY MESSAGES
				if(M_objSOURC==txtINDNO)
					setMSG("Enter Indent Number; 'F1'-Search by Date; 'F2'-Search by Distributor ..",'N');
				else if(M_objSOURC==txtCNSNM||M_objSOURC==txtBYRNM||M_objSOURC==txtDSRNM||M_objSOURC==txtTRPNM)
					setMSG("Press 'F9' for other details ..",'N');
				else if(M_objSOURC==txtDLCCD)
					setMSG("Enter Loading Station; Press 'F1' for help ..",'N');
				else if(M_objSOURC==txtTRPCD)
					setMSG("Enter Transporter; 'F1': Search by Name, 'F2': By Code ..",'N');
				else if(M_objSOURC==txtTRPCD&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
					setMSG("Enter D.O. Number; Press 'F1' for help ..",'N');
				else if(M_objSOURC==txtFRTRT&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
					txtFRTRT.select(0,txtFRTRT.getText().toString().length());
			}

			if(M_objSOURC==txtDORQT)
				if(txtDORQT.getText().length()!=0)
						txtDORQT.select(0,txtDORQT.getText().length());
			if(M_objSOURC==txtQLHQT)
				if(txtQLHQT.getText().length()!=0)
						txtQLHQT.select(0,txtQLHQT.getText().length());
			if(M_objSOURC==txtBASRT)
			{
			   	if(tblDOTRN.getValueAt(tblDOTRN.getSelectedRow(),intTB1_PRDCD).toString().length()==10)
				{
					setMSG("Basic Rate : "+getINTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(tblDOTRN.getSelectedRow(),intTB1_PRDCD).toString()+tblDOTRN.getValueAt(tblDOTRN.getSelectedRow(),intTB1_PKGTP).toString(),"INT_BASRT"),'N');
					txtBASRT.select(0,txtBASRT.getText().length());
					/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
					    // once delivery sch is entered in Do hashtable , it will take over
					    if(!setDODEL(tblDOTRN.getSelectedRow()))
					        setINDEL(tblDOTRN.getSelectedRow());
					}*/
				}
			}
			if(M_objSOURC==tblDODEL.cmpEDITR[intTB2_DSPDT])
				if(((JTextField)tblDODEL.cmpEDITR[intTB2_DSPDT]).getText().length()!=0)
						((JTextField)tblDODEL.cmpEDITR[intTB2_DSPDT]).select(0,((JTextField)tblDODEL.cmpEDITR[intTB2_DSPDT]).toString().length());
			if(M_objSOURC==tblDODEL.cmpEDITR[intTB2_DORQT])
				if(((JTextField)tblDODEL.cmpEDITR[intTB2_DORQT]).getText().length()!=0)
						((JTextField)tblDODEL.cmpEDITR[intTB2_DORQT]).select(0,((JTextField)tblDODEL.cmpEDITR[intTB2_DORQT]).toString().length());
			//if(M_objSOURC==tblDOTRN.cmpEDITR[intTB1_BASRT]||M_objSOURC==tblDOTRN.cmpEDITR[intTB1_DORQT])
			//{}
		}
		catch(Exception e){setMSG(e,"Child.FocusGained"+M_objSOURC);}
			 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
		
	}
	
	/** Returning running total of Qty. entered in Del.Schedule Entry Table
	 *  (upto current row)
	 */
	private float getDOD_RUNQT(String LP_PRDCD,String LP_PKGTP,int LP_ROWNO)
	{
		float L_fltDOD_DORQT = 0.00f;
		if((LP_PRDCD+LP_PKGTP).length()!=12)
			return L_fltDOD_DORQT;
		try
		{
			for(int intDODCTR=0;intDODCTR<tblDODEL.getRowCount();intDODCTR++)
			{
				if (intDODCTR>LP_ROWNO)
					break;
				if(tblDODEL.getValueAt(intDODCTR,intTB2_SRLNO).toString().length()==2)
					if((LP_PRDCD+LP_PKGTP).equals(tblDODEL.getValueAt(intDODCTR,intTB2_PRDCD).toString()+tblDODEL.getValueAt(intDODCTR,intTB2_PKGTP).toString())) 
						L_fltDOD_DORQT += Float.parseFloat(nvlSTRVL(tblDODEL.getValueAt(intDODCTR,intTB2_DORQT).toString(),"0.00"));
			}
		}
		catch(Exception e)
			{setMSG(e,"getDOD_TOTQT");}
		return L_fltDOD_DORQT;
	}
	


private void exeINLTBL()
{
	try
	{
		inlTBLEDIT(tblDOTRN);
		inlTBLEDIT(tblDODEL);
		///crtDODEL_INL();
	}
	catch(Exception e){setMSG(e,"exeINLTBL");}
}

	
	/**<b>TASKS : </B><br>
	 * &nbsp&nbsp&nbsp&nbspSource = Schedule Type Combo in grade detail tab : Add current contents of tblDODEL to hashtable.  If Current grade schedule is already added, display details OR Allow user to add details if delivery type is scheduled OR Make default entry to tblDODEL if delivery type is immidiate<br>
	 * &nbsp&nbsp&nbsp&nbspSource = Req.ed qty of grade in grade detail tab : DORQT should not exceed  Pending qty. and <br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspChecks whether no. of packs is integer with entered req.ed qty. Else shows error and reverts to original location.<br>
	 * &nbsp&nbsp&nbsp&nbspSource = Base rate of grade in grade detail tab : Compare with rate in Indent<br>
	 * &nbsp&nbsp&nbsp&nbspSource = DO date in tblDODEL : Delivery date should not be repeated.<br>
	 * &nbsp&nbsp&nbsp&nbspSource = Req.ed qty of grade in delivery schedule : Check shedule qty < REQQT<br>
	 */
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
			///if(M_objSOURC==tblDOTRN.cmpEDITR[intTB1_DELTP])
			///	exeDODEL_REFRESH(tblDOTRN.getSelectedRow(),"N");

			if(M_objSOURC==txtBASRT && ((JTextField)M_objSOURC).getText().length()>0)
			{
			    //System.out.println("Focus Lost BASRT");
				//inlTBLEDIT(tblDOTRN);
			}
			else if(M_objSOURC==txtDELTP && ((JTextField)M_objSOURC).getText().length()>0)
			{
				//inlTBLEDIT(tblDOTRN);
			}
			else if(M_objSOURC==txtDORQT&&((JTextField)M_objSOURC).getText().length()>0)
			{
			    //System.out.println("Focus Lost DORQT");
				//inlTBLEDIT(tblDOTRN);
			}
			else if(M_objSOURC==txtQLHQT&&((JTextField)M_objSOURC).getText().length()>0)
			{
			    //System.out.println("Focus Lost DORQT");
				//inlTBLEDIT(tblDOTRN);
			}
			else if(M_objSOURC==tblDODEL.cmpEDITR[intTB2_DORQT]&&((JTextField)M_objSOURC).getText().length()>0)
				{/*tblDODEL.editCellAt(tblDODEL.getSelectedRow()+1,intTB2_SRLNO);*/}
				
		}catch(Exception e){setMSG(e,"Child.FocusLost");}
	}

	
	/**<b>TASKS : </b><br>
	 * &nbsp&nbsp&nbspSource = txtINDNO : HELP Indent No. and date of quthorised indents<br>
	 * &nbsp&nbsp&nbspSource = txtDORNO : HELP DO Number and date of valid DO for said indent<br>
	 * &nbsp&nbsp&nbspSource = txtTRPCD : HELP Transporters from party master<br>  
	 * &nbsp&nbsp&nbspSource = txtDSRNM / txtTRPNM / txtBYRNM : Display Address details of the party in an internal window<br> 
	 * &nbsp&nbsp&nbspSource = txtDLCCD : HELP Destination code and description from CO_CDTRN<br> 
	 * &nbsp&nbsp&nbspSource = DODDT from tblDODEL : Generate serial no.<br> 
	 * &nbsp&nbsp&nbspFocus navigation
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
		int L_intKEYCD=L_KE.getKeyCode();
		try
		{
			if(cmbMKTTP.getItemCount()>0)
				strMKTTP=getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
			if(M_objSOURC==txtDORNO)
			{
				M_strHLPFLD = "txtDORNO";
				if(L_KE.getKeyCode()==L_KE.VK_F1)
				{
					String L_strSQLQRY = "Select distinct DOT_DORNO,DOT_DORDT,DOT_AMDNO,DOT_AMDDT,DOT_INDNO from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP='"+strMKTTP+"' and DOT_STSFL<>'X' and DOT_LADQT<DOT_DORQT and DOT_INDNO in (select IN_INDNO from MR_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(IN_SBSCD,3,2) = '"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"')";
					L_strSQLQRY += cl_dat.M_strUSRCT_pbst.equals("02") ? " and DOT_CMPCD + DOT_MKTTP + DOT_INDNO in (select IN_CMPCD + IN_MKTTP + IN_INDNO from MR_INMST where IN_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IN_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"')" : "";
					L_strSQLQRY += " order by DOT_DORDT desc,DOT_DORNO desc";
					System.out.println(L_strSQLQRY);
					cl_hlp(L_strSQLQRY ,1,1,new String[] {"DO.No","DO.Date","Amd.No.","Amd.Date","Ind.No."},5,"CT");//	
				}
			}
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0&&(L_intKEYCD==L_KE.VK_ENTER||L_intKEYCD==L_KE.VK_A||L_intKEYCD==L_KE.VK_M||L_intKEYCD==L_KE.VK_E||L_intKEYCD==L_KE.VK_D))
			{
				//cmbMKTTP.requestFocus();			
			}
			else if(M_objSOURC==txtINDNO)
			{
				M_strHLPFLD = "txtINDNO";
				if(L_KE.getKeyCode()==L_KE.VK_F1 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					//cl_hlp("Select IN_INDNO,IN_BKGDT,IN_AUTTM,sum(int_INDQT-INT_DORQT) from MR_INMST,MR_INTRN where IN_INDNO=INT_INDNO and IN_STSFL='1' and IN_MKTTP='"+strMKTTP+"' and IN_SALTP = '"+M_strSBSCD.substring(2,4)+"' group by IN_INDNO,IN_BKGDT having sum(int_INDQT-INT_DORQT)>0 order by IN_BKGDT desc, IN_INDNO desc " ,1,1,new String[] {"Indent No","Indent Date","Time"},3,"CT");
					//M_strSQLQRY = "Select IN_INDNO,IN_BKGDT,isnull(IN_AUTTM,time('00:00:00')) IN_AUTTM,sum(int_INDQT-INT_DORQT) from MR_INMST,MR_INTRN where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_INDNO=INT_INDNO and IN_CMPCD = INT_CMPCD and IN_STSFL='1' and IN_MKTTP='"+strMKTTP+"' and IN_SALTP = '"+M_strSBSCD.substring(2,4)+"' group by IN_INDNO,IN_BKGDT,IN_AUTTM having sum(int_INDQT-INT_DORQT)>0 order by IN_BKGDT desc, IN_INDNO desc";
					M_strSQLQRY = "Select IN_INDNO,IN_BKGDT,isnull(IN_AUTTM,time('00:00:00')) IN_AUTTM,sum((isnull(int_indqt,0)-isnull(int_fcmqt,0))-INT_DORQT) from VW_INTRN where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and IN_STSFL='1' and IN_MKTTP='"+strMKTTP+"' and IN_SALTP = '"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' group by IN_INDNO,IN_BKGDT,IN_AUTTM having sum((isnull(int_indqt,0)-isnull(int_fcmqt,0))-INT_DORQT)>0 order by IN_BKGDT desc, IN_INDNO desc";
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Indent No","Indent Date","Time"},3,"CT");
				}
			}
			else if(M_objSOURC==txtDORNO && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
			{
				//M_strHLPFLD = "txtDORNO";
				//if(L_KE.getKeyCode()==L_KE.VK_F1 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
				//	cl_hlp("Select DOT_DORNO,DOT_DORDT from MR_DOTRN where  DOT_STSFL not in ('2','D','X') and DOT_MKTTP='"+strMKTTP+"' and SUBSTRING(DOT_SBSCD,3,2) = '"+M_strSBSCD.substring(2,4)+"' order by DOT_DORNO " ,1,1,new String[] {"DO No","DO Date"},2,"CT");
				M_strHLPFLD = "txtDORNO";
				if(L_KE.getKeyCode()==L_KE.VK_F1)
				{
					String L_strSQLQRY = "Select distinct DOT_DORNO,DOT_DORDT,DOT_AMDNO,DOT_AMDDT,DOT_INDNO from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP='"+strMKTTP+"' and DOT_STSFL<>'X'  and SUBSTRING(DOT_SBSCD,3,2) = '"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' and DOT_LADQT<DOT_DORQT order by DOT_DORDT desc,DOT_DORNO desc";
					//System.out.println(L_strSQLQRY);
					cl_hlp(L_strSQLQRY ,1,1,new String[] {"DO.No","DO.Date","Amd.No.","Amd.Date","Ind.No."},5,"CT");//	
				}
			}
			else if(M_objSOURC==txtTRPCD)
			{
				if(L_KE.getKeyCode()==L_KE.VK_F1)
				{
					M_strHLPFLD = "txtTRPCD1";
					cl_hlp("Select PT_PRTNM,PT_PRTCD,PT_FAXNO,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='T' and PT_STSFL<>'X' order by PT_PRTNM" ,1,1,new String[] {"Transporter Name","Transporter Code"},6,"CT");
				}
				else if(L_KE.getKeyCode()==L_KE.VK_F2)
				{
					M_strHLPFLD = "txtTRPCD2";
					cl_hlp("Select PT_PRTCD,PT_PRTNM,PT_FAXNO,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='T' and PT_STSFL<>'X' order by PT_PRTCD" ,1,1,new String[] {"Transporter Code","Transporter Name"},6,"CT");
				}
			}
			else if(M_objSOURC==txtBYRNM&&L_KE.getKeyCode()==L_KE.VK_F9)
			{
				if(strBYRAD!=null)
					JOptionPane.showMessageDialog(txtBYRNM,strBYRAD);
				else
					dspADDR(txtBYRNM,txtBYRCD.getText().toString(),"C");
			}
			else if(M_objSOURC==txtDSRNM&&L_KE.getKeyCode()==L_KE.VK_F9)
			{
				if(strBYRAD!=null)
					JOptionPane.showMessageDialog(txtDSRNM,strDSRAD);
				else
					dspADDR(txtDSRNM,txtDSRCD.getText().toString(),lblDSRTP.getText());
			}
			else if(M_objSOURC==txtCNSNM&&L_KE.getKeyCode()==L_KE.VK_F9)
			{
				if(strCNSAD!=null)
					JOptionPane.showMessageDialog(txtCNSNM,strCNSAD);
				else
					dspADDR(txtCNSNM,txtCNSCD.getText().toString(),"C");
			}
			else if(M_objSOURC==txtTRPNM&&L_KE.getKeyCode()==L_KE.VK_F9)
			{
				//System.out.println("008");
				if(strTRPAD!=null)
					JOptionPane.showMessageDialog(txtTRPNM,strTRPAD);
				else
				dspADDR(txtTRPNM,txtTRPCD.getText().toString(),"C");
			}
			else if(M_objSOURC==txtDLCCD&&L_KE.getKeyCode()==L_KE.VK_F1)
			{
				//System.out.println("009");
				M_strHLPFLD = "txtDLCCD";
				if(txtDLCCD.getText().length()==0)
					cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' order by CMT_CODCD" ,1,1,new String[] {"Destination Code","Description"},2,"CT");
				else
				{
					txtDLCCD.setText(txtDLCCD.getText().toUpperCase());
					cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' and CMT_CODCD like '"+txtDLCCD.getText()+"%' order by CMT_CODCD" ,1,1,new String[] {"Destination Code","Description"},2,"CT");
				}
			}
			//else if(M_objSOURC==tblDOTRN.cmpEDITR[intTB1_DELTP] && L_KE.getKeyCode()==L_KE.VK_S)			//SET SERIAL NO FOR THE ENTRY
			//{
			//	tblDODEL.editCellAt(0,intTB2_DSPDT);
			//}
			else if(M_objSOURC==tblDODEL.cmpEDITR[intTB2_PRDCD])//txtGRDCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{
					M_strHLPFLD = "txtGRDCD3";
                    String L_strPRDLS  ="";
                    int L_intCNT =0;
                    hstPRDDT = new Hashtable<String,String>(); 
                    for (int i=0;i<tblDOTRN.getRowCount();i++)
                    {
                        //if((tblDOTRN.getValueAt(i,intTB1_PRDCD).toString().length() ==10)&&(tblDOTRN.getValueAt(i,0).toString().equals("true")))
                        if((tblDOTRN.getValueAt(i,intTB1_PRDCD).toString().length() ==10))
                        {
                            //hstPRDDT.put(tblDOTRN.getValueAt(i,intTB1_PRDCD).toString(),nvlSTRVL(tblDOTRN.getValueAt(i,intTB1_PKGTP).toString(),"")+"|"+nvlSTRVL(tblDOTRN.getValueAt(i,intTB1_REQQT).toString(),"0")+"|"+nvlSTRVL(tblDOTRN.getValueAt(i,intTB1_DELTP).toString(),"I"));
                            hstPRDDT.put(tblDOTRN.getValueAt(i,intTB1_PRDCD).toString(),nvlSTRVL(tblDOTRN.getValueAt(i,intTB1_PKGTP).toString(),"")+"|"+nvlSTRVL(tblDOTRN.getValueAt(i,intTB1_DORQT).toString(),"0"));
                            if(L_intCNT == 0)
                                L_strPRDLS += "'"+tblDOTRN.getValueAt(i,intTB1_PRDCD).toString()+"'";
                            else 
                                L_strPRDLS += ",'"+tblDOTRN.getValueAt(i,intTB1_PRDCD).toString()+"'";
                            L_intCNT++;    
                        }   
                    }
                    M_strSQLQRY = "SELECT PR_PRDCD,PR_PRDDS from CO_PRMST WHERE PR_PRDCD in( ";
                    M_strSQLQRY += L_strPRDLS +")";
                 	cl_hlp(M_strSQLQRY ,1,1,new String[] {"Grade Code","Description"},2,"CT");
				}
			}
			/*else if(M_objSOURC==tblDODEL.cmpEDITR[intTB2_DSPDT]&& tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_SRLNO).toString().length()==0)			//SET SERIAL NO FOR THE ENTRY
			{
				if(tblDODEL.getSelectedRow()<9)
					tblDODEL.setValueAt("0"+Integer.toString(tblDODEL.getSelectedRow()+1),tblDODEL.getSelectedRow(),intTB2_SRLNO);
				else
					tblDODEL.setValueAt(Integer.toString(tblDODEL.getSelectedRow()+1),tblDODEL.getSelectedRow(),intTB2_SRLNO);
			}*/
			if(L_intKEYCD==L_KE.VK_ENTER)
			{
				//System.out.println("011");
				if(M_objSOURC==txtREMDS)
				{
				    inlTBLEDIT(tblDOTRN);
				    inlTBLEDIT(tblDODEL);
				    flgBASRT_ENTER = false;
				   // tblDOTRN.editCellAt(0,getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()).equals(strMKTTP_CAP) ? intTB1_DORQT : intTB1_BASRT);
				    if(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()).equals(strMKTTP_CAP))
				        tblDOTRN.editCellAt(0,intTB1_DORQT);
				    else
				    {
				        if(tblDOTRN.isEditing())
				        {
				            tblDOTRN.getCellEditor().stopCellEditing();
				         }
				        tblDOTRN.setRowSelectionInterval(0,0);
		                tblDOTRN.setColumnSelectionInterval(0,0);    
				        tblDOTRN.editCellAt(0,intTB1_DORQT);
				        tblDOTRN.cmpEDITR[intTB1_DORQT].requestFocus();
				    }
				    
			    }
				else if(M_objSOURC==txtDORNO)
				{
				    exeINLTBL(); txtTRPCD.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && M_objSOURC==txtINDNO)
				{
				    exeINLTBL(); txtTRPCD.requestFocus();
				}
				else if(M_objSOURC==txtBASRT)
				{
				    flgBASRT_ENTER = true; 
				    if(!vldDOT_BASRT(tblDOTRN.getSelectedRow()))
				      tblDOTRN.editCellAt(tblDOTRN.getSelectedRow(),intTB1_BASRT);
				}
				else if(M_objSOURC==txtDORQT)
				{
				    if(!vldDOT_DORQT(tblDOTRN.getSelectedRow()))
				      tblDOTRN.editCellAt(tblDOTRN.getSelectedRow(),intTB1_DORQT);
				}
				else if(M_objSOURC==txtQLHQT)
				{
				      tblDOTRN.editCellAt(tblDOTRN.getSelectedRow(),intTB1_QLHQT);
				}
				else if(M_objSOURC==txtDELTP)
				{
					inlTBLEDIT(tblDODEL);
					///crtDODEL_INL();
					/*if(tblDOTRN.getValueAt(tblDOTRN.getSelectedRow(),intTB1_DORQT).toString().length()>0)
					{
						if(Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(tblDOTRN.getSelectedRow(),intTB1_DORQT).toString(),"0.00"))>0)
							exeDODEL_REFRESH(tblDOTRN.getSelectedRow(),"N");
					}*/
					//tblDODEL.clrTABLE();
				}
				else
					((Component)M_objSOURC).transferFocus();
			}
		}catch(Exception e)	{setMSG(e,"Child.KeyPressed");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtINDNO"))
				txtINDNO.setText(L_STRTKN.nextToken());
			else if(M_strHLPFLD.equals("txtDORNO"))
			{
				txtDORNO.setText(L_STRTKN.nextToken());
				txtDORNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtTRPCD1"))
			{
				txtTRPNM.setText(L_STRTKN.nextToken());
				txtTRPCD.setText(L_STRTKN.nextToken());
               // System.out.println("1 "+txtTRPCD.getText());
				txtFAXNO.setText(L_STRTKN.nextToken());
				strTRPAD=txtTRPNM.getText();
				while(L_STRTKN.hasMoreTokens())
					strTRPAD+="\n"+L_STRTKN.nextToken();
			}
			else if(M_strHLPFLD.equals("txtTRPCD2"))
			{
				txtTRPCD.setText(L_STRTKN.nextToken());
               // System.out.println("2 "+txtTRPCD.getText());
                txtTRPNM.setText(L_STRTKN.nextToken());
				txtFAXNO.setText(L_STRTKN.nextToken());
				strTRPAD=txtTRPNM.getText();
				while(L_STRTKN.hasMoreTokens())
					strTRPAD+="\n"+L_STRTKN.nextToken();
			}
			else if(M_strHLPFLD.equals("txtDLCCD"))
			{
				txtDLCCD.setText(L_STRTKN.nextToken());
				while(L_STRTKN.hasMoreTokens())
					lblDLCDS.setText(L_STRTKN.nextToken());
					//txtDLCDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtGRDCD3"))
			{
				String L_strTEMP=L_STRTKN.nextToken();
				((JTextField)tblDODEL.cmpEDITR[intTB2_PRDCD]).setText(L_strTEMP);
				StringTokenizer L_STRTK1=new StringTokenizer(hstPRDDT.get(L_strTEMP).toString(),"|");
				//tblDODEL.setValueAt(Boolean.TRUE,tblDODEL.getSelectedRow(),intTB2_CHKFL);
				//tblDODEL.setValueAt(L_strTEMP,tblDODEL.getSelectedRow(),intTB2_PRDCD);
				tblDODEL.setValueAt(L_STRTKN.nextToken(),tblDODEL.getSelectedRow(),intTB2_PRDDS);
				tblDODEL.setValueAt(L_STRTK1.nextToken(),tblDODEL.getSelectedRow(),intTB2_PKGTP);
				tblDODEL.setValueAt(L_STRTK1.nextToken(),tblDODEL.getSelectedRow(),intTB2_DORQT);
				//tblDODEL.setValueAt("I",tblDODEL.getSelectedRow(),intTB5_DELTP);
			}

		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Child.exeHLPOK");
		}
	}

	
	/**Fields <B>NOT</B> available for user entry : <br>
	 * Distributor Code, Buyer Code, Loading station Description, Consignee Code, Distributor Code, Mode of transport,
	 *  Delivey Type, Serial No in tblDODEL, 
	 *  tblDOTRN : Grade Description, Indent Qty, Pending Qty, Pack size, No of packs, Stock available	 */
	void setENBL(boolean L_STAT)
	{
		try
		{
		super.setENBL(L_STAT);
		//cmbSALTP.setEnabled(false);
		txtDSRCD.setEnabled(false);
		txtBYRCD.setEnabled(false);
		//txtDLCDS.setEnabled(false);
		txtCNSCD.setEnabled(false);
		lblDSTCD.setVisible(false);
		txtDLCCD.setEnabled(false);
		lblDSTDS.setVisible(true);
		lblAUTBY.setVisible(true);
		lblAUTDT.setVisible(true);
		lblAMDNO.setVisible(true);
		lblAMDDT.setVisible(true);
		//cmbMOTCD.setEnabled(false);
		cmbDTPCD.setEnabled(false);
		chkAUTOCF.setSelected(true);
		chkAUTOCF.setSelected(false);
		txtBASRT.setEnabled(false);
		if(exeDSPCHK("INV_NOMSG"))
		{
		    // If condition for Addition added on 10/06/2006 , previously it was for all cases
		    if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
			    txtTRPCD.setEnabled(false);


			    //System.out.println("false from 1");
			}
		}
		//tblDODEL.cmpEDITR[0].setEnabled(false);
		tblDOTRN.cmpEDITR[intTB2_SRLNO].setEnabled(false);
		tblDOTRN.cmpEDITR[intTB2_DORQT].setEnabled(false);
		tblDOTRN.cmpEDITR[intTB1_BASRT].setEnabled(false);
		tblDOTRN.cmpEDITR[intTB1_QLHQT].setEnabled(L_STAT);
		tblDOTRN.setEnabled(false);
		txtDORQT.setEnabled(L_STAT);
		txtQLHQT.setEnabled(L_STAT);
		txtDELTP.setEnabled(L_STAT);
		//txtBASRT.setEnabled(L_STAT);
		
		tblDODEL.cmpEDITR[intTB2_SRLNO].setEnabled(false);
		tblDODEL.cmpEDITR[intTB2_PRDDS].setEnabled(false);
		tblDODEL.cmpEDITR[intTB2_PKGTP].setEnabled(false);
        tblDODEL.cmpEDITR[intTB2_LADQT].setEnabled(false);

		//Disable D.O. Number during addition
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
		{	
			txtDORNO.setEnabled(false);
		    tblDODEL.cmpEDITR[intTB2_CHKFL].setEnabled(true);
        }
		else 
		{
			txtINDNO.setEnabled(false);
		    tblDODEL.cmpEDITR[intTB2_CHKFL].setEnabled(false);	
		}
		
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
			btnPRINT.setEnabled(true);
		}
		catch(Exception L_EX) {	setMSG(L_EX,"setENBL");}
}
	/** Restoring default Key Values after clearing components 
	 * on the entry screen
	 */
	private void clrCOMP_1()
	{
		cmbMKTTP.removeFocusListener(this);
		cmbSALTP.removeFocusListener(this);
		cmbMKTTP.removeActionListener(this);
		cmbSALTP.removeActionListener(this);
		try
		{
			String L_strINDNO = txtINDNO.getText();
			String L_strDORNO = txtDORNO.getText();
			String L_strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
			String L_strSALTP = getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString());
			//String L_strMKTTP = cmbMKTTP.getSelectedItem().toString().trim();
			//String L_strSALTP = cmbSALTP.getSelectedItem().toString().trim();
			System.out.println(L_strMKTTP+" "+L_strSALTP);
			clrCOMP();
			setCMBDFT(cmbSALTP,"SYSMR00SAL",L_strSALTP);
			setCMBDFT(cmbMKTTP,"MSTCOXXMKT",L_strMKTTP);
			//cmbSALTP.setSelectedItem(L_strSALTP);
			//cmbMKTTP.setSelectedItem(L_strMKTTP);
			System.out.println(L_strMKTTP+" "+L_strSALTP);
			txtINDNO.setText(L_strINDNO);
			txtDORNO.setText(L_strDORNO);
			chkAUTOCF.setSelected(true);
			chkAUTOCF.setSelected(false);
		
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP_1");}
		cmbMKTTP.addFocusListener(this);
		cmbSALTP.addFocusListener(this);
		cmbMKTTP.addActionListener(this);
		cmbSALTP.addActionListener(this);
	}
	
	
	/**set Default focused cells for tblDOTRN and tblDODEL	 */
	void  clrCOMP()
	{
		super.clrCOMP();
		try
		{
			//flgEXSAV=false;
			if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
			{
				inlTBLEDIT(tblDOTRN);
				inlTBLEDIT(tblDODEL);
			}
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP");}
	}

	
	/**
	 */
	void exeSAVE()
	{
		try
		{
			String strVLDMSG = "";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				if(exeDSPCHK("LAD_MSG"))
					{cl_dat.M_flgLCUPD_pbst=false; return;}
			inlTBLEDIT(tblDOTRN);
			inlTBLEDIT(tblDODEL);
			
			if (!crtINTRN_1(strMKTTP,txtINDNO.getText()," AND INT_STSFL <>'X' "))
				return;
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				if(!crtDOTRN_1(strMKTTP,txtDORNO.getText()))
					return;
			}
			
			boolean L_flgRETFL = true;
			for(int LP_ROWNO=0;LP_ROWNO<tblDOTRN.getRowCount();LP_ROWNO++)
			{
				float fltDOT_DORQT = 0;
				if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					fltDOT_DORQT = Float.parseFloat(nvlSTRVL(getDOTRN_1(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"DOT_DORQT"),"0.00"));
				String L_strDORQT=tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString();
				float fltTBL_DORQT = Float.parseFloat(nvlSTRVL(L_strDORQT,"0.00"));
                                float fltINT_INDQT = Float.parseFloat(nvlSTRVL(getINTRN_1(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_INDQT"),"0.00"));
				float fltINT_FCMQT = Float.parseFloat(nvlSTRVL(getINTRN_1(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_FCMQT"),"0.00"));
				float fltINT_DORQT = Float.parseFloat(nvlSTRVL(getINTRN_1(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_DORQT"),"0.00"));
				float fltINT_INVQT = Float.parseFloat(nvlSTRVL(getINTRN_1(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_INVQT"),"0.00"));

                                //if((fltTBL_DORQT+fltINT_DORQT-fltDOT_DORQT) > fltINT_INDQT-fltINT_FCMQT)
                                if(Double.parseDouble(setNumberFormat(fltTBL_DORQT,3)) > Double.parseDouble(setNumberFormat(fltINT_INDQT - fltINT_FCMQT - fltINT_DORQT+fltDOT_DORQT,3)))
					strVLDMSG = "Against Ind:"+txtINDNO.getText()+"  Grade:"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString()+"     Available  = "+setNumberFormat(fltINT_INDQT - fltINT_FCMQT - fltINT_DORQT+fltDOT_DORQT,3)+"  Entered = "+setNumberFormat(fltTBL_DORQT,3);
				else if(fltINT_INVQT>(fltTBL_DORQT+fltINT_DORQT))
					strVLDMSG = "Against DO :"+txtDORNO.getText()+"  Grade:"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString()+"     Despacthed = "+setNumberFormat(fltINT_INVQT,3)+"  D.O. prepared  = "+setNumberFormat(fltTBL_DORQT+fltINT_DORQT,3);
				if(!strVLDMSG.equals(""))
					{setMSG(strVLDMSG,'E'); L_flgRETFL = false;}
			}
			if(!L_flgRETFL)
				return;
			
			
			
			if(!vldDATA())
				return;
			
			
				setMSG("",'N');
				String strMKTTP=getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
				String L_strAMDNO="00",L_strAMDDT="",L_strAUTBY="xxx",L_strAUTDT="xxx";
				if(!chkDORNO())
					return;
				cl_dat.M_flgLCUPD_pbst = true;
				StringTokenizer L_stkTEMP=null;
				//PUTTING REMARK
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					cl_dat.M_flgLCUPD_pbst = true;
					//StringTokenizer L_stkTEMP=null;

					// saveAMND();
            		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))&&(!strSTSFL.equals("0")))
            		{
                			String L_strSQLQRY  ="Insert into MR_DOTAM Select * from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"+strMKTTP +"' AND DOT_DORNO = '"+txtDORNO.getText()+"'";
                			cl_dat.exeSQLUPD(L_strSQLQRY,"" );
                			L_strSQLQRY  ="Insert into MR_DODAM Select * from MR_DODEL where DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOD_MKTTP = '"+strMKTTP +"' AND DOD_DORNO = '"+txtDORNO.getText()+"'";
                			cl_dat.exeSQLUPD(L_strSQLQRY,"");
                   	 }
					saveRMMST("DO",txtREMDS.getText());
					flgFIRSTREC=true;
					for(int i=0;i<tblDOTRN.getRowCount() ;i++)
					{
						if (((tblDOTRN.getValueAt(i,intTB1_PRDDS)).toString()).length()==0)
							continue;
						if(tblDOTRN.getValueAt(i,intTB1_DORQT).toString().length()==0)
							continue;
						saveDOTRN(i);
						saveDODEL_NEW(i,tblDOTRN.getValueAt(i,intTB1_PRDCD).toString(),tblDOTRN.getValueAt(i,intTB1_PKGTP).toString());												
						//saveDODEL(i);
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeSQLUPD("Update MR_DOTRN set DOT_STSFL='X',DOT_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ,DOT_DORQT=0 where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP='"
						+strMKTTP+"' and DOT_SBSCD='"+M_strSBSCD+"' and DOT_INDNO='"
						+txtINDNO.getText()+"' and DOT_DORNO='"+txtDORNO.getText()+"'","setLCLUPD");
					
					if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeSQLUPD("Update MR_DODEL set DOD_STSFL='X',DOD_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ,DOD_DORQT=0 where DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOD_MKTTP='"
						+strMKTTP+"' and DOD_SBSCD='"+M_strSBSCD+"' and DOD_DORNO='"
						+txtDORNO.getText()+"'","setLCLUPD");
				}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Record Saved Successfully",'N');
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("D.O. cancelled ..",'N');
				setINT_DORQT();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					rlsINDNT();
				clrCOMP_1();
				//flgEXSAV=true;
				if(btnPRINT.isEnabled())
					btnPRINT.requestFocus();
			}
			else
			{
				setMSG("Error occured during saving ..",'E');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					txtDORNO.setText("");
				//flgEXSAV=false;
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
			setMSG("Error occured during saving ..",'E');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					txtDORNO.setText("");
			cl_dat.M_flgLCUPD_pbst=false;
			cl_dat.exeDBCMT("exeSAVE");
			//flgEXSAV=false;
		}
		finally
		{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}


private boolean chkDORNO()
{
	try
	{
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			return true;
		M_strSQLQRY = "Select * from co_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MR"+strMKTTP+"DOR' and cmt_CODCD='"+strYRDGT+strMKTTP+"'";
		//System.out.println(M_strSQLQRY);
		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET==null || (!M_rstRSSET.next()))
			{setMSG("D.O. series not found ..",'E'); cl_dat.M_flgLCUPD_pbst = false; return false;}
		String L_strDORNO=null;
		if(getRSTVAL(M_rstRSSET,"CMT_CHP01","C").equals("N"))
				{setMSG("D.O. Series is in Use. Please retry after some time ..",'E'); 	return false;}
		cl_dat.exeSQLUPD("Update co_cdtrn set cmt_CHP01='N' where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MR"+strMKTTP+"DOR' and cmt_CODCD='"+strYRDGT+strMKTTP+"'","setLCLUPD");
		L_strDORNO=Integer.toString(Integer.parseInt(getRSTVAL(M_rstRSSET,"CMT_CCSVL","N"))+1);
		txtDORNO.setText(L_strDORNO);
		if(L_strDORNO.length()<5)
		{
			for(int i=0;i<=5;i++)
			{
				txtDORNO.setText("0"+txtDORNO.getText());
				if(txtDORNO.getText().length()==5)
					break;
			}
		}
		L_strDORNO = txtDORNO.getText();
		txtDORNO.setText(getRSTVAL(M_rstRSSET,"CMT_CODCD","C")+L_strDORNO);
	}
    catch(Exception L_EX)
		{setMSG(L_EX,"chkDORNO"); return false;}
	return true;
}


/** Setting master level details from MR_DOTRN
 */
private boolean setDOMST(String LP_MKTTP, String LP_DORNO)
{
	ResultSet L_rstRSSET = null;
	try
	{
		String strDOMOT="";
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
			{setMSG("Invalid option",'E'); return false;}
		M_strSQLQRY = "Select * from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"+LP_MKTTP+"' and DOT_DORNO='"+LP_DORNO+"'";
		M_strSQLQRY += cl_dat.M_strUSRCT_pbst.equals("02") ? " and DOT_CMPCD + DOT_MKTTP + DOT_INDNO in (select IN_CMPCD + IN_MKTTP + IN_INDNO from MR_INMST where IN_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IN_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"')" : "";
		System.out.println(M_strSQLQRY);
		//System.out.println(M_strSQLQRY);
		L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		
		if(L_rstRSSET==null || !L_rstRSSET.next())
			{setMSG("Record not found in MR_DOTRN for : "+LP_DORNO,'E'); return false;}		

		strDORDT = getRSTVAL(L_rstRSSET,"DOT_DORDT","D");
		txtINDNO.setText(getRSTVAL(L_rstRSSET,"DOT_INDNO","C"));
		txtLRYNO.setText(getRSTVAL(L_rstRSSET,"DOT_LRYNO","C"));
		txtTRPCD.setText(getRSTVAL(L_rstRSSET,"DOT_TRPCD","C"));
        txtFRTRT.setText(getRSTVAL(L_rstRSSET,"DOT_FRTRT","N"));
		lblAUTBY.setText(getRSTVAL(L_rstRSSET,"DOT_AUTBY","C"));
		lblAUTDT.setText(getRSTVAL(L_rstRSSET,"DOT_AUTDT","D"));
		lblSTSDS.setText("Status   :  "+getCDTRN("STSMRXXDOR"+getRSTVAL(L_rstRSSET,"DOT_STSFL","C"),"CMT_CODDS",hstCDTRN));
		strSTSFL = getRSTVAL(L_rstRSSET,"DOT_STSFL","C");
		strDOMOT = getRSTVAL(L_rstRSSET,"DOT_TMOCD","C");

		// Verifying for amendment status
		String L_strDOAMDNO = getRSTVAL(L_rstRSSET,"DOT_AMDNO","C").trim();
		String L_strDOAMDDT = getRSTVAL(L_rstRSSET,"DOT_AMDDT","D");
		if (!getRSTVAL(L_rstRSSET,"DOT_STSFL","C").trim().equals("0"))
		{
			L_strDOAMDNO = (Integer.parseInt(L_strDOAMDNO)<9 ? "0" : "")+String.valueOf(Integer.parseInt(L_strDOAMDNO)+1);
			L_strDOAMDDT = cl_dat.M_strLOGDT_pbst;
		}
		lblAMDNO.setText(L_strDOAMDNO);
		lblAMDDT.setText(L_strDOAMDDT);

		// Picking up records for Grade Entry Table
		hstDOTRN.clear();
		while(true)
		{
			crtDOTRN(L_rstRSSET);
			if(!L_rstRSSET.next())
				break;
		}
		L_rstRSSET.close();

		// Call to crtdodel replaced with crtdodel_new
		///crtDODEL(LP_MKTTP, LP_DORNO," and DOD_STSFL<>'X'");
		setDODEL_NEW(LP_MKTTP, LP_DORNO,"  and DOD_STSFL<>'X'");
		setINMST(LP_MKTTP,txtINDNO.getText().toString(),"","NO_CLEAR");
		setCMBDFT(cmbMOTCD,"SYSMR01MOT",strDOMOT); // CHANGED ON 06/07/07 TO TAKE IT FROM DO 

	}
    catch(Exception L_EX)
		{setMSG(L_EX,"setDOMST"); return false;}
	return true;
}



/** Setting master level details from MR_DOTRN
 */
private boolean crtDOTRN_1(String LP_MKTTP, String LP_DORNO)
{
	ResultSet L_rstRSSET = null;
	try
	{
		String strDOMOT="";
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
			{setMSG("Invalid option",'E'); return false;}
		M_strSQLQRY = "Select * from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"+LP_MKTTP+"' and DOT_DORNO='"+LP_DORNO+"'";
		//System.out.println(M_strSQLQRY);
		L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		if(L_rstRSSET==null || !L_rstRSSET.next())
			{setMSG("Record not found in MR_DOTRN for : "+LP_DORNO,'E'); return false;}		


		// Picking up records for Grade Entry Table
		hstDOTRN_1.clear();
		while(true)
		{
			crtDOTRN_1(L_rstRSSET);
			if(!L_rstRSSET.next())
				break;
		}
		L_rstRSSET.close();

		// Call to crtdodel replaced with crtdodel_new
		///crtDODEL(LP_MKTTP, LP_DORNO," and DOD_STSFL<>'X'");

	}
    catch(Exception L_EX)
		{setMSG(L_EX,"setDOMST"); return false;}
	return true;
}





/**  
 */
private void saveDOTRN(int LP_ROWNO)
{
	try
	{
		/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			if(exeDSPCHK("LAD_MSG"))
				{cl_dat.M_flgLCUPD_pbst=false; return;} */
		///saveAMND();
		lblAUTDT.setText("");
		lblAUTBY.setText("");
		if (rdbAUTH_Y.isSelected())
			{lblAUTDT.setText(cl_dat.M_strLOGDT_pbst); lblAUTBY.setText(cl_dat.M_strUSRCD_pbst);}
		if(!cl_dat.M_flgLCUPD_pbst)
			return;

		strWHRSTR =  "DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '" +strMKTTP+"' and "
					 +"DOT_DORNO = '" +txtDORNO.getText()+"' and "
					 +"DOT_PRDCD = '" +tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+"' and "
					 +"DOT_PKGTP = '" +tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()+"'";
		flgCHK_EXIST =  chkEXIST("MR_DOTRN", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in MR_DOTRN");
				return;
		}
		String L_dblDORPK = tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORPK).toString();
		 
		if(Double.parseDouble(L_dblDORPK)==0)
			L_dblDORPK = getDOTPK1(Double.parseDouble(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString()),tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString());
			
		
		inlTBLEDIT(tblDOTRN);
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY= "insert into MR_DOTRN (DOT_CMPCD,DOT_MKTTP ,DOT_DORNO ,DOT_PRDCD ,DOT_PKGTP ,DOT_DORDT ,DOT_INDNO ,DOT_AMDNO,DOT_AMDDT,DOT_TMOCD ,DOT_DLCCD ,DOT_LRYNO ,DOT_FRTRT ,DOT_PRDDS ,DOT_DORPK ,DOT_PKGWT ,DOT_DORQT ,DOT_ORDUM ,DOT_LADQT ,DOT_INVQT ,DOT_FCMQT ,DOT_DELTP ,DOT_STSFL ,DOT_LUSBY ,DOT_LUPDT ,DOT_TRPCD ,DOT_AUTBY,DOT_AUTDT,DOT_AUTTM,DOT_SBSCD,DOT_SBSCD1,DOT_TRNFL) values (" 
			+setINSSTR("DOT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("DOT_MKTTP",strMKTTP,"C")
			+setINSSTR("DOT_DORNO",txtDORNO.getText().toString(),"C")
			+setINSSTR("DOT_PRDCD",tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"C")
			+setINSSTR("DOT_PKGTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"C")
			+setINSSTR("DOT_DORDT",cl_dat.M_strLOGDT_pbst,"D")
			+setINSSTR("DOT_INDNO",txtINDNO.getText().toString(),"C")
			+setINSSTR("DOT_AMDNO",lblAMDNO.getText().toString().length()==0 ? "00" : lblAMDNO.getText().toString(),"C")
			+setINSSTR("DOT_AMDDT",lblAMDDT.getText().toString(),"D")
			+setINSSTR("DOT_TMOCD",getCODCD("SYSMR01MOT"+cmbMOTCD.getSelectedItem().toString()),"C")
			+setINSSTR("DOT_DLCCD",txtDLCCD.getText().toString(),"C")
			+setINSSTR("DOT_LRYNO",txtLRYNO.getText().toUpperCase(),"C")
			+setINSSTR("DOT_FRTRT",txtFRTRT.getText().toString(),"N")
			+setINSSTR("DOT_PRDDS",tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString(),"C")
			+setINSSTR("DOT_DORPK",L_dblDORPK,"N")
			+setINSSTR("DOT_PKGWT",getCDTRN("SYSFGXXPKG"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"CMT_NCSVL",hstCDTRN),"N")
			+setINSSTR("DOT_DORQT",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"N")
			+setINSSTR("DOT_ORDUM","MT","C")
			+setINSSTR("DOT_LADQT","0","N")
			+setINSSTR("DOT_INVQT","0","N")
			+setINSSTR("DOT_FCMQT","0","N")
			+setINSSTR("DOT_DELTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DELTP).toString(),"C")
			+setINSSTR("DOT_STSFL",(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"))==0.00)  ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setINSSTR("DOT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("DOT_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
			+setINSSTR("DOT_TRPCD",txtTRPCD.getText().toString(),"C")
			+setINSSTR("DOT_AUTBY",lblAUTBY.getText().toString(),"C")
			+setINSSTR("DOT_AUTDT",lblAUTDT.getText().toString(),"D")
			+setINSSTR("DOT_AUTTM",rdbAUTH_Y.isSelected() ? cl_dat.M_txtCLKTM_pbst.getText() : "","T")
			+setINSSTR("DOT_SBSCD",M_strSBSCD,"C")
						 +setINSSTR("DOT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString())+"00","C")
			+"'0')";   // setINSSTR("DOT_TRNFL","0","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY = "update MR_DOTRN set "
			+setUPDSTR("DOT_INDNO",txtINDNO.getText().toString(),"C")
			+setUPDSTR("DOT_AMDNO",lblAMDNO.getText().toString().length()==0 ? "00" : lblAMDNO.getText().toString(),"C")
			+setUPDSTR("DOT_AMDDT",lblAMDDT.getText().toString(),"D")
			+setUPDSTR("DOT_TMOCD",getCODCD("SYSMR01MOT"+cmbMOTCD.getSelectedItem().toString()),"C")
			+setUPDSTR("DOT_DLCCD",txtDLCCD.getText().toString(),"C")
			+setUPDSTR("DOT_LRYNO",txtLRYNO.getText().toUpperCase(),"C")
			+setUPDSTR("DOT_FRTRT",txtFRTRT.getText().toString(),"N")
			+setUPDSTR("DOT_PRDDS",tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString(),"C")
			+setUPDSTR("DOT_DORPK",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORPK).toString(),"N")
			+setUPDSTR("DOT_PKGWT",getCDTRN("SYSFGXXPKG"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"CMT_NCSVL",hstCDTRN),"N")
			+setUPDSTR("DOT_DORQT",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"N")
			+setUPDSTR("DOT_ORDUM","MT","C")
			//+setUPDSTR("DOT_LADQT","0","N")
			//+setUPDSTR("DOT_INVQT","0","N")
			//+setUPDSTR("DOT_FCMQT","0","N")
			+setUPDSTR("DOT_DELTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DELTP).toString(),"C")
			+setUPDSTR("DOT_TRPCD",txtTRPCD.getText().toString(),"C")
			+setUPDSTR("DOT_AUTBY",lblAUTBY.getText().toString(),"C")
			+setUPDSTR("DOT_AUTDT",lblAUTDT.getText().toString(),"D")
			+setUPDSTR("DOT_AUTTM",rdbAUTH_Y.isSelected() ? cl_dat.M_txtCLKTM_pbst.getText() : "","T")
			+setUPDSTR("DOT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("DOT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString())+"00","C")
			+setUPDSTR("DOT_STSFL",(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"))==0.00)  ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setUPDSTR("DOT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("DOT_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
			+"DOT_TRNFL = '0'  where "+strWHRSTR;
		}
		 //System.out.println(M_strSQLQRY);

		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");


		if(Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"))==0.00)
		{
			strWHRSTR =   "DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOD_MKTTP = '" +strMKTTP+"' and "
						 +"DOD_DORNO = '" +txtDORNO.getText()+"' and "
						 +"DOD_PRDCD = '" +tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+"' and "
						 +"DOD_PKGTP = '" +tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()+"'";
			cl_dat.exeSQLUPD("delete from MR_DODEL where "+strWHRSTR,"");
			//System.out.println("delete from MR_DODEL where "+strWHRSTR);
		}

		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgFIRSTREC)
		{
			JOptionPane.showMessageDialog(this,"Please Note Down D.O. Number\n"+txtDORNO.getText().toString(),"",JOptionPane.ERROR_MESSAGE);
			M_strSQLQRY="update co_cdtrn set cmt_ccsvl='"+txtDORNO.getText().substring(3)+"',CMT_CHP01='Y' where cmt_cgmtp='D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='MR"+strMKTTP+"DOR'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		flgFIRSTREC=false;
	}
	catch (Exception L_EX)
	{setMSG("Error in saveDOTRN : "+L_EX,'E');}
}

private void saveDODEL_NEW(int LP_ROWNO,String LP_PRDCD,String LP_PKGTP)
{
	String L_strDOTKEY = "", L_strDODKEY = "";
	int L_intSRLNO =0;
	String L_strSRLNO ="";
	try
	{
	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
	    {
    		for(int i=0;i<tblDODEL.getRowCount();i++)
    		{
    		    //if((tblDODEL.getValueAt(i,intTB2_CHKFL).toString().equals("true"))&&(tblDODEL.getValueAt(i,intTB2_PRDCD).toString().equals(LP_PRDCD))&&(tblDODEL.getValueAt(i,intTB2_PKGTP).toString().equals(LP_PKGTP)))
    		    if((tblDODEL.getValueAt(i,intTB2_CHKFL).toString().equals("true"))&&(tblDODEL.getValueAt(i,intTB2_PRDCD).toString().equals(LP_PRDCD))&&(tblDODEL.getValueAt(i,intTB2_PKGTP).toString().equals(LP_PKGTP)))
    		    {
    		        L_intSRLNO++;
    		        if((""+L_intSRLNO).length() == 1)
    		        L_strSRLNO = "0"+L_intSRLNO;
    		        //System.out.println(LP_PRDCD +" "+ L_strSRLNO);
        		    M_strSQLQRY="Insert into MR_DODEL (DOD_CMPCD,DOD_MKTTP,DOD_DORNO,DOD_PRDCD,DOD_SRLNO,DOD_DSPDT,DOD_DORQT,DOD_DELTP,DOD_PKGTP,DOD_SBSCD,DOD_SBSCD1,DOD_STSFL,DOD_LUSBY,DOD_LUPDT,DOD_TRNFL) values ("
        			+setINSSTR("DOD_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
					+setINSSTR("DOD_MKTTP",strMKTTP,"C")
        			+setINSSTR("DOD_DORNO",txtDORNO.getText().toString(),"C")
        			+setINSSTR("DOD_PRDCD",tblDODEL.getValueAt(i,intTB2_PRDCD).toString(),"C")
        			//+setINSSTR("DOD_SRLNO",tblDODEL.getValueAt(i,intTB2_SRLNO).toString(),"C")
        			+setINSSTR("DOD_SRLNO",L_strSRLNO,"C")
        			+setINSSTR("DOD_DSPDT",tblDODEL.getValueAt(i,intTB2_DSPDT).toString(),"D")
        			+setINSSTR("DOD_DORQT",tblDODEL.getValueAt(i,intTB2_DORQT).toString(),"N")
        			+setINSSTR("DOD_DELTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DELTP).toString().toUpperCase(),"C")
        			+setINSSTR("DOD_PKGTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"C")
        			+setINSSTR("DOD_SBSCD",M_strSBSCD,"C")
					+setINSSTR("DOD_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblDODEL.getValueAt(i,intTB2_PRDCD).toString())+"00","C")
        			+setINSSTR("DOD_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
        			+setINSSTR("DOD_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
        			+setINSSTR("DOD_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
        			+"'0')";
        			//System.out.println(M_strSQLQRY);
        			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
    		    }
    		}
	    }
	    else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
	    {
	       for(int i=0;i<tblDODEL.getRowCount();i++)
    		{
    	       //if((tblDODEL.getValueAt(i,intTB2_CHKFL).toString().equals("true"))&&(tblDODEL.getValueAt(i,intTB2_PRDCD).toString().equals(LP_PRDCD))&&(tblDODEL.getValueAt(i,intTB2_PKGTP).toString().equals(LP_PKGTP)))
    		    if((tblDODEL.getValueAt(i,intTB2_CHKFL).toString().equals("true"))&&(tblDODEL.getValueAt(i,intTB2_PRDCD).toString().equals(LP_PRDCD)))
    		    {
                    strWHRSTR =  "DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOD_MKTTP = '" +strMKTTP+"' and "
   					 +"DOD_DORNO = '" +txtDORNO.getText()+"' and "
   					 +"DOD_PRDCD = '" +LP_PRDCD+"' and "
   					 +"DOD_SRLNO = '" +tblDODEL.getValueAt(i,intTB2_SRLNO).toString()+"'";
    
            		flgCHK_EXIST =  chkEXIST("MR_DODEL", strWHRSTR);
                    if(!flgCHK_EXIST)
    		        {
                        M_strSQLQRY="Insert into MR_DODEL (DOD_CMPCD,DOD_MKTTP,DOD_DORNO,DOD_PRDCD,DOD_SRLNO,DOD_DSPDT,DOD_DORQT,DOD_DELTP,DOD_PKGTP,DOD_SBSCD,DOD_SBSCD1,DOD_STSFL,DOD_LUSBY,DOD_LUPDT,DOD_TRNFL) values ("
            			+setINSSTR("DOD_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
						+setINSSTR("DOD_MKTTP",strMKTTP,"C")
            			+setINSSTR("DOD_DORNO",txtDORNO.getText().toString(),"C")
            			+setINSSTR("DOD_PRDCD",tblDODEL.getValueAt(i,intTB2_PRDCD).toString(),"C")
            			+setINSSTR("DOD_SRLNO",tblDODEL.getValueAt(i,intTB2_SRLNO).toString(),"C")
            			//+setINSSTR("DOD_SRLNO",tblDODEL.getValueAt(i,intTB2_SRLNO).toString(),"C")
            			+setINSSTR("DOD_DSPDT",tblDODEL.getValueAt(i,intTB2_DSPDT).toString(),"D")
            			+setINSSTR("DOD_DORQT",tblDODEL.getValueAt(i,intTB2_DORQT).toString(),"N")
            			+setINSSTR("DOD_DELTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DELTP).toString().toUpperCase(),"C")
            			+setINSSTR("DOD_PKGTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"C")
            			+setINSSTR("DOD_SBSCD",M_strSBSCD,"C")
						+setINSSTR("DOD_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblDODEL.getValueAt(i,intTB2_PRDCD).toString())+"00","C")
            			+setINSSTR("DOD_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
            			+setINSSTR("DOD_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
            			+setINSSTR("DOD_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
            			+"'0')";
            			//System.out.println(M_strSQLQRY);
            			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
                    }
    		        else if(flgCHK_EXIST)
            		{
                  		M_strSQLQRY = "update MR_DODEL set "
            			+setUPDSTR("DOD_DSPDT",tblDODEL.getValueAt(i,intTB2_DSPDT).toString(),"D")
            			+setUPDSTR("DOD_DORQT",tblDODEL.getValueAt(i,intTB2_DORQT).toString(),"N")
            			+setUPDSTR("DOD_DELTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_DELTP).toString().toUpperCase(),"C")
            			+setUPDSTR("DOD_PKGTP",tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"C")
            			+setUPDSTR("DOD_SBSCD",M_strSBSCD,"C")
						+setUPDSTR("DOD_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblDODEL.getValueAt(i,intTB2_PRDCD).toString())+"00","C")
            			+setUPDSTR("DOD_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
            			+setUPDSTR("DOD_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
            			+setUPDSTR("DOD_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
            			+"DOD_TRNFL = '0'  where "+strWHRSTR;
            			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
                  	}
    		    }
    		}
        }
	}
	catch (Exception L_EX)
	{setMSG("Error in saveDODEL_1 : "+L_EX,'E');}
}

	
/** Saving record in remark master
 */
private void saveRMMST(String LP_DOCTP, String LP_REMDS)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(txtREMDS.getText().toString().length()==0)
			return;
		
		strWHRSTR = " RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP = '"+strMKTTP+"' and "
					+ "RM_TRNTP = '"+LP_DOCTP+"' and "
					+ "RM_DOCNO = '"+txtDORNO.getText()+"'";
		flgCHK_EXIST =  chkEXIST("MR_RMMST", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in MR_RMMST");
				cl_dat.M_flgLCUPD_pbst = false;
				return;
		}
		
		if(!flgCHK_EXIST)
		{
		
			M_strSQLQRY="Insert into MR_RMMST (RM_CMPCD,RM_MKTTP,RM_TRNTP,RM_DOCNO,RM_REMDS,RM_SBSCD,RM_SBSCD1,RM_TRNFL,RM_LUSBY,RM_LUPDT,RM_STSFL) values ("
			+setINSSTR("RM_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("RM_MKTTP",strMKTTP,"C")
			+setINSSTR("RM_TRNTP",LP_DOCTP,"C")
			+setINSSTR("RM_DOCNO",txtDORNO.getText().toString(),"C")
			+setINSSTR("RM_REMDS",delQuote(txtREMDS.getText().toString()),"C")
			+setINSSTR("RM_SBSCD",M_strSBSCD,"C")
			+setINSSTR("RM_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("RM_TRNFL","0","C")
			+setINSSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("RM_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
			+"'0')";   //+setUPDSTR("RM_STSFL",strIN_STSFL,"C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update  MR_RMMST set "
			+setUPDSTR("RM_REMDS",delQuote(LP_REMDS),"C")
			+setUPDSTR("RM_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("RM_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setUPDSTR("RM_TRNFL","0","C")
			+setUPDSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("RM_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
			+"RM_STSFL = '0' where " + strWHRSTR;  //+setUPDSTR("RM_STSFL","0","C")
		}
		 //System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveRMMST : "+L_EX,'E');}
}
	

private void setINT_DORQT()
{
	try
	{
		cl_dat.exeSQLUPD("update MR_INTRN set INT_DORQT=0,INT_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP = '"+strMKTTP+"' and INT_INDNO = '"+txtINDNO.getText()+"'","");
		String L_strDORQT ="";
		strWHRSTR = " DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"		+strMKTTP			+"' and "
					+ " DOT_INDNO = '"		+txtINDNO.getText()	+"' and "
					+ " DOT_STSFL <> 'X'";
		String L_strSQLQRY = "select DOT_MKTTP, DOT_INDNO, DOT_PRDCD, DOT_PKGTP, sum(DOT_DORQT) DOT_DORQT from MR_DOTRN where "+strWHRSTR+" group by DOT_MKTTP, DOT_INDNO, DOT_PRDCD, DOT_PKGTP";
		//System.out.println(L_strSQLQRY);
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
		if(L_rstRSSET==null || !L_rstRSSET.next())
			return;
		while(true)
		{
				String L_strINT_DORQT = getRSTVAL(L_rstRSSET,"DOT_DORQT","N");
				//System.out.println("L_strINT_DORQT : "+L_strINT_DORQT);
				//setINT_DORQT_1(getRSTVAL(L_rstRSSET,"DOT_PRDCD","C"),getRSTVAL(L_rstRSSET,"DOT_PKGTP","C"),getRSTVAL(L_rstRSSET,"DOT_DORQT","N"));
				strWHRSTR = " INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP = '"		+strMKTTP			+"' and "
					+ " INT_INDNO = '"		+txtINDNO.getText()	+"' and "
					+ " INT_PRDCD = '"		+getRSTVAL(L_rstRSSET,"DOT_PRDCD","C")	+"' and "
					+ " INT_PKGTP = '"		+getRSTVAL(L_rstRSSET,"DOT_PKGTP","C")	+"'";
        		/*flgCHK_EXIST =  chkEXIST("MR_INTRN", strWHRSTR);
        		if(!flgCHK_EXIST)
        		{
        				JOptionPane.showMessageDialog(this,"Record not found in MR_INTRN for "+strWHRSTR);
        				return;
        		}*/
				L_strSQLQRY = "Update MR_INTRN set "
        		+setUPDSTR("INT_DORQT",L_strINT_DORQT,"N")
        		+setUPDSTR("INT_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
        		+"INT_TRNFL='0' where "+strWHRSTR;
				//System.out.println(L_strSQLQRY);
        		cl_dat.exeSQLUPD(L_strSQLQRY,"");
        		if(cl_dat.exeDBCMT("setINT_DORQT_1"))
        			setMSG("INT_DORQT updated Successfully",'N');
				if(!L_rstRSSET.next())
					break;
		}
		L_rstRSSET.close();
	}
	catch (Exception L_EX)
		{setMSG(L_EX,"setINT_DORQT");}

}



	/**To start editing of said cell in said JTable	 */
	private void editCellAt(cl_JTBL P_tblTEMP,int P_intROWID,int P_intCOLID)
	{
		P_tblTEMP.setRowSelectionInterval(P_intROWID,P_intROWID);
		P_tblTEMP.setColumnSelectionInterval(P_intCOLID,P_intCOLID);
		P_tblTEMP.editCellAt(P_intROWID,P_intCOLID);
		P_tblTEMP.cmpEDITR[P_intCOLID].requestFocus();
	}
	/**Class to handle field level validations, if field not blank.	 */

	class INPVF extends InputVerifier
	{
		/**<b>TASKS : </B><BR>
		 * Source = txtINDNO : Display details of the indent<br> Source = txtDORNO : Display details of the D.O.<br>
		 * Source = txtTRPCD,txtDLCCD : Validate Transporter code and Loading station resp.		 */
		public boolean verify(JComponent input) 
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtINDNO)
				{
					txtINDNO.setText(txtINDNO.getText().toUpperCase());
					hstGRTRN.clear();
					hstINTRN.clear();
					hstINTRN_1.clear();
					hstDOTRN.clear();
					hstDOTRN_1.clear();
					setDFTSTS();
					if (!setINMST(strMKTTP,txtINDNO.getText(),"","CLEAR"))
						return false;
				}
				else if(input==txtDORNO)
				{
					//clrCOMP_1();
					//System.out.println("Input txtDORNO");
					hstGRTRN.clear();
					hstINTRN.clear();
					hstINTRN_1.clear();
					hstDOTRN.clear();
					hstDOTRN_1.clear();
					setDFTSTS();
					if(!setDOMST(strMKTTP,txtDORNO.getText()))
						return false;
				}
				else if(input == txtTRPCD)
				{
					//System.out.println("INPUT VERIFIER FIRED "); 
					txtTRPCD.setText(txtTRPCD.getText().toUpperCase());
                    //System.out.println("4 " +txtTRPCD.getText().toUpperCase());
					M_strSQLQRY = "Select * from co_ptmst where pt_prttp='T' and pt_prtcd='"+txtTRPCD.getText()+"'";
					//System.out.println(M_strSQLQRY);
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(L_rstRSSET!=null)
					{
						if(L_rstRSSET.next())
						{
							txtTRPNM.setText(L_rstRSSET.getString("PT_PRTNM"));
							txtFAXNO.setText(L_rstRSSET.getString("PT_FAXNO"));
							strTRPAD=L_rstRSSET.getString("PT_PRTNM")+"\n"+L_rstRSSET.getString("PT_ADR01")+"\n"+L_rstRSSET.getString("PT_ADR02")+"\n"+L_rstRSSET.getString("PT_ADR03");
							return true;
						}
						else
						{
							setMSG("Invalid Transporter ..",'E');
							return false;
						}
					}
					else 
					{
						setMSG("Invalid Transporter ..",'E');
						return false;
					}
				}
				else if(input == txtDLCCD)
				{
                            return true;    }
			}catch(Exception e)
			{setMSG(e,"Verifier");return false;}
			return true;
		}		         
	}
	/**Stops cell editing in all tables & puts current delivery schedule in tblDODEL to hstDODTL<br><B>VALIDATIONS : </B><BR>
	 * txtINDNO : Compulsory field;
	 * txtDLCCD : Compulsory field;
	 * txtTRPCD : Compulsory field;
	 * tblDOTRN : Base rate for each grade is compulsory
	 * tblDOTRN : Required qty. for each grade is compulsory
	 * tblDODEL/hstDODTL : Delivery schedule for each grade is compulsory	 */


	
/** Adding No.of days & returning new date
 */
private String setNEWDT(String LP_DTSTR, int LP_DAYVL)
{
	String L_strRETVL = "";
	try
	{
		M_calLOCAL.setTime(M_fmtLCDAT.parse(LP_DTSTR));
		M_calLOCAL.add(Calendar.DATE,LP_DAYVL);
		L_strRETVL = M_fmtLCDAT.format(M_calLOCAL.getTime());
	}
	catch(Exception e)
		{setMSG(e,"setNEWDT");}
	return L_strRETVL;
}
	

boolean vldDATA()
	{
		try
		{
		    setMSG("",'N');
		    /// full method body is commented so call itself commented on 28/07/2006
			///if(!chkTBLEDIT(tblDOTRN)) return false;
			///if(!chkTBLEDIT(tblDODEL)) return false;

			inlTBLEDIT(tblDOTRN);
			inlTBLEDIT(tblDODEL);

			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
				return true;
			
			///exeDODEL_FLUSH();
			if(!chkTXT_RANGE(txtINDNO,8,9,"Indent No. Entry is not proper ..."))  return false;
			if(!chkTXT_RANGE(txtDLCCD,2,2,"Despatch Location code Entry is not proper ..."))  return false;
			if(!chkTXT_RANGE(txtTRPCD,5,5,"Transporter code Entry is not proper ..."))  return false;
			if(!chkPARTY("T",txtTRPCD.getText(),"Invalid Buyer Code","Y"))		 return false;
			
			/*if(!vldDOT_DODQT()) 	// Matching D.O.qty of individual grade with Del.Schedule Total
			{
			    System.out.println("Do Del qty mismatch from vldDATA");
			    return false;
			}*/
			flgBASRT_ENTER = true;
			for(int intDOTCT=0;intDOTCT<tblDOTRN.getRowCount();intDOTCT++)
				if(!vldDOT_BASRT(intDOTCT)) return false;  // Validating D.O. Basic Rate with Indent Basic Rate
			
			for(int intDOTCT=0;intDOTCT<tblDOTRN.getRowCount();intDOTCT++)
				if(!vldDOT_DORQT(intDOTCT)) return false;  // Validating D.O. Qty with Invoice / LA qty.

			if(!vldDOT_FRTRT())  return false;	// Validating Freight Rate entry, if applicable
            String L_strDSPDT="",L_strDODKEY="";
            // Added on 12/06/2006 for del date verification with current date			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
    			///Enumeration enmDODKEYS=hstDODEL.keys();
        		///while(enmDODKEYS.hasMoreElements())
        		for(int i=0;i<tblDODEL.getRowCount()&&tblDODEL.getValueAt(i,intTB2_CHKFL).toString().equals("true");i++)
        		{
        			///L_strDODKEY = (String)enmDODKEYS.nextElement();
        			///L_strDSPDT = getDODEL(L_strDODKEY,"DOD_DSPDT");
        			///if(M_fmtLCDAT.parse(L_strDSPDT).compareTo(M_fmtLCDAT.parse(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) ? cl_dat.M_strLOGDT_pbst : strDORDT))<0)
        			if(M_fmtLCDAT.parse(tblDODEL.getValueAt(i,intTB2_DSPDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
        			{
        			    setMSG("Dispatch Date can not be less than current Date for grade " +tblDODEL.getValueAt(i,intTB2_PRDDS).toString(),'E');   
        			    return false;
        			}
        		}
			}
			int L_intSELCT =0;
			if(!M_strSBSCD.equals("511600"))
			{
    			for(int i=0;i<tblDODEL.getRowCount();i++)
    			{
    			    if(tblDODEL.getValueAt(i,intTB2_CHKFL).toString().equals("true"))
    			        L_intSELCT++;
    			}
    			if(L_intSELCT ==0 )			    	
                {
                    setMSG("Please select the delivery schedule",'E'); 
                    return false;
                }
            }
			for(int i=0;i<tblDOTRN.getRowCount()&&tblDOTRN.getValueAt(i,intTB1_CHKFL).toString().equals("true");i++)
			{
				//if(!M_strSBSCD.equals("511600")) // released for captive consumption
               if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			   {
				   dblTOTQT_SCH = getTOT_DODQT(tblDOTRN.getValueAt(i,intTB1_PRDCD).toString(),tblDOTRN.getValueAt(i,intTB1_PKGTP).toString());
				   dblTOTQT_GRD = Double.parseDouble(tblDOTRN.getValueAt(i,intTB1_DORQT).toString());
				   dblTOTQT_SCH = Double.parseDouble(setNumberFormat(dblTOTQT_SCH,3));
				   dblTOTQT_GRD = Double.parseDouble(setNumberFormat(dblTOTQT_GRD,3));
				   if(dblTOTQT_SCH != dblTOTQT_GRD)			    	
				   {
					    //System.out.println("Total "+getTOT_DODQT(tblDOTRN.getValueAt(i,intTB1_PRDCD).toString(),tblDOTRN.getValueAt(i,intTB1_PKGTP).toString()));
					    //System.out.println("grade "+Double.parseDouble(tblDOTRN.getValueAt(i,intTB1_DORQT).toString()));
						setMSG("Quantity mismatch in Delivery Schedule.. Grade : "+setNumberFormat(dblTOTQT_GRD,3)+" Scheduled : "+setNumberFormat(dblTOTQT_SCH,3)+" ( "+tblDOTRN.getValueAt(i,intTB1_PRDDS).toString()+")",'E'); 
					    return false;
				   }
			   }
    		}
		String L_strTEMP ="";
		String L_strTEMP1 ="";
		//System.out.println("Do del date heck starrt");
		for(int j=0;j<tblDODEL.getRowCount();j++)
		{
			if(tblDODEL.getValueAt(j,intTB2_PRDCD).toString().length() == 0)
			continue;
			L_strTEMP = tblDODEL.getValueAt(j,intTB2_PRDDS).toString().trim()+tblDODEL.getValueAt(j,intTB2_PKGTP).toString().trim()+tblDODEL.getValueAt(j,intTB2_DSPDT).toString().trim();
			L_strTEMP1 = tblDODEL.getValueAt(j,intTB2_PRDDS).toString().trim()+tblDODEL.getValueAt(j,intTB2_PKGTP).toString().trim();
		//System.out.println("L_strTEMP "+L_strTEMP);
		//System.out.println("L_strTEMP1 "+L_strTEMP1);
		for(int i=0;i<tblDODEL.getRowCount();i++)
	       {
			if(tblDODEL.getValueAt(i,intTB2_PRDCD).toString().length() == 0)
				continue;
			if( i==j)
			{
	          		//System.out.println(" i == j "+ i);
			 	continue;
			}
	           	if(getDOT_DELTP(tblDODEL.getValueAt(i,intTB2_PRDCD).toString(),tblDODEL.getValueAt(i,intTB2_PKGTP).toString()).equals("S"))
	           	{
				//System.out.println(" i == "+ i + " j == "+j +" S ");
				if((tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim().length() >0)&& (tblDODEL.getValueAt(i,intTB2_DSPDT).toString().trim().length() >0))
				if((tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim()+tblDODEL.getValueAt(i,intTB2_PKGTP).toString().trim()+tblDODEL.getValueAt(i,intTB2_DSPDT).toString().trim()).equals(L_strTEMP.trim()))
				{
					setMSG("Duplicate Date entry for .."+L_strTEMP,'E');
		      		return false;
		      	}
				else
				{
					//System.out.println("notmatching");
				}
	            }
	            else
	            {
				//System.out.println("immediate del "+tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim()+" "+L_strTEMP1.trim());
	            	if(tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim().length() >0)
		      	if((tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim()+tblDODEL.getValueAt(i,intTB2_PKGTP).toString().trim()).equals(L_strTEMP1.trim()))
		      	{
		      		setMSG("Duplicate Date entry for Immediate delivery of .."+L_strTEMP1,'E');
		      		return false;
		      	}
				else
				{
					//System.out.println("immediate del not matching");
				}

	           	}
		}
		}
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			return true;
		}
		catch(Exception e){setMSG(e,"(Child.vldDATA) Error Occured During Saving");this.setCursor(cl_dat.M_curWTSTS_pbst); return false;}
	}



/**  Validating D.O. Basic Rate with Indent Basic Rate
 *   for specified row in Grade Entry table
 */
private boolean vldDOT_BASRT(int LP_ROWNO)
{
	boolean L_flgRETFL = true;
	if(flgBASRT_ENTER=false)
		return false;
	try
	{
		if(strMKTTP.equals(strMKTTP_CAP))
			return true;
		if(Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"))==0)
			return true;
		String L_strBASRT=tblDOTRN.getValueAt(LP_ROWNO,intTB1_BASRT).toString();
		if(tblDOTRN.isEditing())
			if(tblDOTRN.getEditingRow()==LP_ROWNO && tblDOTRN.getEditingColumn()==intTB1_BASRT)
				L_strBASRT=txtBASRT.getText();
		//if(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString().length()!=10 || Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"))<=0)
		if(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString().length()==0)
			return true;
		float fltDOT_BASRT = Float.parseFloat(nvlSTRVL(L_strBASRT,"0.00"));
		float fltINT_BASRT = Float.parseFloat(nvlSTRVL(getINTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_BASRT"),"0.00"));
		//System.out.println("fltDOT_BASRT / fltINT_BASRT : "+fltDOT_BASRT+" / "+fltINT_BASRT);
		if(fltDOT_BASRT != fltINT_BASRT)
			{setMSG("Grade "+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString()+" : Bas.Rate =  "+fltINT_BASRT+"  Entered = "+fltDOT_BASRT,'E'); L_flgRETFL = false;}
	}
	catch(Exception e){setMSG(e,"vldDOT_BASRT");}
	return L_flgRETFL;
}



/** Validating D.O.Qty against Qty. available for Despatch
 *  for specified row in Grade Entry Table
 */
private boolean vldDOT_DORQT(int LP_ROWNO)
{
	boolean L_flgRETFL = true;
	try
	{
		String L_strDORQT=tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString();
		String L_strSTKQT=tblDOTRN.getValueAt(LP_ROWNO,intTB1_STKQT).toString();
		String L_strQLHQT=tblDOTRN.getValueAt(LP_ROWNO,intTB1_QLHQT).toString();
		if(tblDOTRN.isEditing())
			if(tblDOTRN.getEditingRow()==LP_ROWNO && tblDOTRN.getEditingColumn()==intTB1_DORQT)
				L_strDORQT=txtDORQT.getText();
				
			if(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString().length()!=10 || L_strDORQT.length()==0)
				return true;
			
			if(!chkDOTPK(LP_ROWNO))
			   return false;
			float fltTBL_DORQT = Float.parseFloat(nvlSTRVL(L_strDORQT,"0.00"));
			float fltTBL_STKQT = Float.parseFloat(nvlSTRVL(L_strSTKQT,"0.00"));
			float fltTBL_QLHQT = Float.parseFloat(nvlSTRVL(L_strQLHQT,"0.00"));
			float fltDOT_DORQT = Float.parseFloat(nvlSTRVL(getDOTRN(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"DOT_DORQT"),"0.00"));
			float fltDOT_LADQT = Float.parseFloat(nvlSTRVL(getDOTRN(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"DOT_LADQT"),"0.00"));
			float fltDOT_INVQT = Float.parseFloat(nvlSTRVL(getDOTRN(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"DOT_INVQT"),"0.00"));
			float fltINT_INDQT = Float.parseFloat(nvlSTRVL(getINTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_INDQT"),"0.00"));
			float fltINT_FCMQT = Float.parseFloat(nvlSTRVL(getINTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_FCMQT"),"0.00"));
			float fltINT_DORQT = Float.parseFloat(nvlSTRVL(getINTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_DORQT"),"0.00"));
			float fltINT_INVQT = Float.parseFloat(nvlSTRVL(getINTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_INVQT"),"0.00"));
			float fltINT_STKQT = Float.parseFloat(nvlSTRVL(getINTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"INT_STKQT"),"0.00"));
			//float fltGR_QLHQT = Float.parseFloat(nvlSTRVL(getGRTRN(strMKTTP+txtINDNO.getText()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"GR_QLHQT"),"0.00"));
			///float fltDOD_DORQT = getDOD_HSTQT(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString());
			 //System.out.println("fltTBL_DORQT : "+fltTBL_DORQT);
			 //System.out.println("fltTBL_STKQT : "+fltTBL_STKQT);
			 //System.out.println("fltTBL_QLHQT : "+fltTBL_QLHQT);
			 //System.out.println("fltDOT_DORQT : "+fltDOT_DORQT);
			 //System.out.println("fltDOT_LADQT : "+fltDOT_LADQT);
			 //System.out.println("fltDOT_INVQT : "+fltDOT_INVQT);
			 //System.out.println("fltINT_INDQT : "+fltINT_INDQT);
			 //System.out.println("fltINT_DORQT : "+fltINT_DORQT);
			 //System.out.println("fltINT_INVQT : "+fltINT_INVQT);
			String strVLDMSG = "";
			String strVLDMSG1 = "";
			//if(((fltTBL_DORQT-fltDOT_DORQT)+fltINT_DORQT) > fltINT_INDQT-fltINT_FCMQT)
			if(  (((fltTBL_DORQT-fltDOT_DORQT)+fltINT_DORQT)   -   (fltINT_INDQT-fltINT_FCMQT))  > 0.01  )
				strVLDMSG = "Against Ind:"+txtINDNO.getText()+"  Grade:"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString()+"     Available  = "+setNumberFormat(fltINT_INDQT - fltINT_FCMQT -(fltINT_DORQT-fltDOT_DORQT),3)+"  Entered = "+setNumberFormat(fltTBL_DORQT,3);
			else if((fltDOT_LADQT > fltDOT_INVQT ? fltDOT_LADQT : fltDOT_INVQT)>(fltTBL_DORQT) )
				strVLDMSG = "Against DO :"+txtDORNO.getText()+"  Grade:"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString()+"     Despacthed = "+setNumberFormat((fltDOT_LADQT > fltDOT_INVQT ? fltDOT_LADQT : fltDOT_INVQT),3)+"  Entered  = "+setNumberFormat(fltTBL_DORQT,3);
			else if(fltTBL_DORQT > fltTBL_STKQT)
				strVLDMSG1 = "Stock available for  Grade:"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString()+" & Pkg.Type : "+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()+"  = "+setNumberFormat(fltTBL_STKQT,3)+"  Entered  = "+setNumberFormat(fltTBL_DORQT,3);
			else if(fltTBL_DORQT > (fltTBL_STKQT-fltTBL_QLHQT) && fltTBL_QLHQT>0 && fltTBL_DORQT > 0)
				strVLDMSG1 = "Quality Hold Stock for  Grade:"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString()+"   = "+setNumberFormat((fltTBL_STKQT-fltTBL_QLHQT),3)+"  Entered  = "+setNumberFormat(fltTBL_DORQT,3);
			if(!strVLDMSG.equals(""))
				{setMSG(strVLDMSG,'E'); L_flgRETFL = false;}
			else if(!strVLDMSG1.equals("") && strVLDMSG.equals(""))
				{setMSG(strVLDMSG1,'E'); L_flgRETFL = true;}
				//{JOptionPane.showMessageDialog((Component)M_objSOURC,strVLDMSG1,"Warning ",JOptionPane.ERROR_MESSAGE); L_flgRETFL = true;}
			///if(fltTBL_DORQT==0.00)
			///	clrHSTDODEL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString());
	}
	catch(Exception e){setMSG(e,"vldDOT_DORQT");}
	return L_flgRETFL;
}

/** Checking whether delivery type belongs to the category, where Freight Rate is compulsory (e.g. FOR category)
 *  and If so, ensuring that freight rate is entered
 */
private boolean vldDOT_FRTRT()
{
	boolean L_flgRETFL = true;
	try
	{
		if((strFRTCT_DTP.indexOf(getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString())))!= -1) // Del.Type belongs to Freight Category
		{
			if (!getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()).equals(strMKTTP_CAP))   // Not Captive Consumption category
			{
				if (txtFRTRT.getText().length()==0)
					L_flgRETFL = false;
				else if(Double.parseDouble(txtFRTRT.getText().toString())<=0.00)
					L_flgRETFL = false;
				if (!L_flgRETFL)
					setMSG("Please Enter Freight Rate ..",'E');
			}
		}
	}
	catch(Exception e){setMSG(e,"vldDOT_FRTRT");}
	return L_flgRETFL;
}



/** Validating D.O. qty for every grade in Grade Entry table
 *  with respective Del.Schedule Total Qty.
 */
/*private boolean vldDOT_DODQT()
{
	float fltDOT_DORQT = 0.00f;
	float fltDOD_DORQT = 0.00f;
	boolean L_flgRETFL = true;
	try
	{
		for(int intDOTCT=0;intDOTCT<tblDOTRN.getRowCount();intDOTCT++)
		{
			if(tblDOTRN.getValueAt(intDOTCT,intTB1_PRDCD).toString().length()==10)
			{
				fltDOT_DORQT = Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(intDOTCT,intTB1_DORQT).toString(),"0.00"));
				///fltDOD_DORQT = getDOD_HSTQT(tblDOTRN.getValueAt(intDOTCT,intTB1_PRDCD).toString(),tblDOTRN.getValueAt(intDOTCT,intTB1_PKGTP).toString());
				fltDOD_DORQT = getTOT_DODQT(tblDOTRN.getValueAt(intDOTCT,intTB1_PRDCD).toString(),tblDOTRN.getValueAt(intDOTCT,intTB1_PKGTP).toString());
				if(Float.parseFloat(setNumberFormat(fltDOT_DORQT,3)) != Float.parseFloat(setNumberFormat(fltDOD_DORQT,3))  && fltDOT_DORQT>0)
					{JOptionPane.showMessageDialog(this,"Del.Sch.Total Mismatch for : "+tblDOTRN.getValueAt(intDOTCT,intTB1_PRDDS).toString()+"    "+Float.parseFloat(setNumberFormat(fltDOT_DORQT,3))+" / "+Float.parseFloat(setNumberFormat(fltDOD_DORQT,3))); L_flgRETFL = false;}
			}
		}
	}
	catch(Exception e){setMSG(e,"vldDOT_DODQT");}
	return L_flgRETFL;
}

*/
	/**Method to display detailed address of a party<br>Fires query to DB and displays details in a separate window	 */
	private void dspADDR(Component P_cmpSOURC,String P_strPRTCD,String P_strPRTTP)
	{
		try
		{
			M_strSQLQRY = "Select PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='"+P_strPRTTP+"' and PT_PRTCD='"+P_strPRTCD+"'";
			//System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(L_rstRSSET!=null)
				if(L_rstRSSET.next())
				{
					JOptionPane.showMessageDialog(P_cmpSOURC,L_rstRSSET.getString("PT_PRTNM")+"\n"+L_rstRSSET.getString("PT_ADR01")+"\n"+L_rstRSSET.getString("PT_ADR02")+"\n"+L_rstRSSET.getString("PT_ADR03")+"\n");
					if(P_cmpSOURC instanceof JTextField)
						((JTextField)P_cmpSOURC).setText(L_rstRSSET.getString("PT_PRTNM"));
					L_rstRSSET.close();
				}
		}
		catch(Exception e){setMSG(e,"dspADDR");}
	}

	/**Method to change selected item of ComboBox. <br>Method provided to avoid error generation due to selection change of ComboBox when not having focus on it<br>Removes ItemListener,changes selected item and then registers ItemListener	 */
	private void setSelectedIndex(JComboBox P_cmbSOURC,int P_intINDEX)
	{
		try
		{
			P_cmbSOURC.removeItemListener(this);
			P_cmbSOURC.setSelectedIndex(P_intINDEX);
			P_cmbSOURC.addItemListener(this);
		}
		catch(Exception e){setMSG(e,"seSelectedIndex : ");}
	}

	/** Clearing rest part of the table 
	 *  from specified row value onwards
	 */
	private void clrTBLDODEL_REST(int LP_ROWNO)
	{
		try
		{
			for(int intDODCT=LP_ROWNO+1; intDODCT<tblDODEL.getRowCount(); intDODCT++)
			{
				if(tblDODEL.getValueAt(intDODCT,intTB2_SRLNO).toString().length()==0)
					break;
				//System.out.println("clear table");	
				tblDODEL.setValueAt("",intDODCT,intTB2_SRLNO);
				tblDODEL.setValueAt("",intDODCT,intTB2_DSPDT);
				tblDODEL.setValueAt("",intDODCT,intTB2_DORQT);
				tblDODEL.setValueAt("",intDODCT,intTB2_LADQT);
				tblDODEL.setValueAt("",intDODCT,intTB2_PRDCD);
				tblDODEL.setValueAt("",intDODCT,intTB2_PRDDS);
				tblDODEL.setValueAt("",intDODCT,intTB2_PKGTP);
			}
		}
		catch(Exception e){setMSG(e,"clrTBLDODEL_REST : ");}
	}
	

/** Validating the current cell in the table
 *  (If table cell is under editing)
 */
	private boolean chkTBLEDIT(JTable LP_TBLNM)
	{
		try
		{
		/*	if(LP_TBLNM.isEditing())
			{
				{
					TBLINPVF L_ipvTBLNM = new TBLINPVF();
					L_ipvTBLNM.setSource(LP_TBLNM);
					//System.out.println("chkTBLEDIT : "+LP_TBLNM.getSelectedRow()+" "+LP_TBLNM.getSelectedColumn());
						
					if(L_ipvTBLNM.verify(LP_TBLNM.getSelectedRow(),LP_TBLNM.getSelectedColumn()))
						LP_TBLNM.getCellEditor().stopCellEditing();
					else
						return false;
				}
			}*/
		}
		catch(Exception e){setMSG(e,"chkTBLEDIT : ");}
		return true;
	}
	
	
	/**  Table Input Verifier
	 */
	class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(getSource()==tblDOTRN)
				{
					 //System.out.println(P_intROWID+"/"+P_intCOLID);
					if(P_intCOLID==intTB1_BASRT)
						{if(!vldDOT_BASRT(P_intROWID)) return false;}
					else if(P_intCOLID==intTB1_DORQT)
						{
							if(!vldDOT_DORQT(P_intROWID)) 
								return false;
							tblDOTRN.setValueAt(getDOTPK(P_intROWID),P_intROWID,intTB1_DORPK);
						}
					else if(P_intCOLID==intTB1_DELTP)
					{
						//String L_strDELTP = ((JTextField)tblDOTRN.cmpEDITR[intTB1_DELTP]).toString();
						if(tblDOTRN.getValueAt(tblDOTRN.getSelectedRow(),intTB1_PRDCD).toString().length()==10 && tblDOTRN.getValueAt(tblDOTRN.getSelectedRow(),intTB1_DORQT).toString().length()>0)
						{
							String L_strDELTP = tblDOTRN.getValueAt(P_intROWID,intTB1_DELTP).toString();
							if((!L_strDELTP.equals("I")) && (!L_strDELTP.equals("S")))
								{setMSG("Invalid Del.Type "+L_strDELTP+"  ... Enter I for Immediate,  S for Scheduled",'E'); return false;}
						}
						return true;
					}
				}
				else if(getSource()==tblDODEL)
				{
					/*if(P_intCOLID==intTB2_DSPDT)
					{
						//if(M_fmtLCDAT.parse(tblDODEL.getValueAt(P_intROWID,intTB2_DSPDT).toString()).compareTo(M_fmtLCDAT.parse( cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) ? cl_dat.M_strLOGDT_pbst : strDORDT))<0)
						//	{setMSG("Schedule date cannot be less than Today ..",'E'); 	return false;}
					}*/
					if(P_intCOLID == intTB2_DSPDT)
					{
					    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
					    {
					    	String L_strTEMP = tblDODEL.getValueAt(P_intROWID,intTB2_PRDDS).toString()+tblDODEL.getValueAt(P_intROWID,intTB2_DSPDT).toString();
					    	String strTEMP = tblDODEL.getValueAt(P_intROWID,intTB2_PRDDS).toString();
             		   	    if(M_fmtLCDAT.parse(tblDODEL.getValueAt(P_intROWID,intTB2_DSPDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))< 0)
							    {setMSG("Schedule date cannot be less than Today ..",'E'); 	return false;}
             		   	    else
             		   	    {
	             		   	    for(int i=0;i<tblDODEL.getRowCount();i++)
	             				{
	             		   	    	if( i==P_intROWID)
	             		   	    		continue;
	             		   	    	if(getDOT_DELTP(tblDODEL.getValueAt(i,intTB2_PRDCD).toString(),tblDODEL.getValueAt(i,intTB2_PKGTP).toString()).equals("S"))
	             		   	    	{
		             					if((tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim().length() >0)&& (tblDODEL.getValueAt(i,intTB2_DSPDT).toString().trim().length() >0))
		             					if((tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim()+tblDODEL.getValueAt(i,intTB2_DSPDT).toString().trim()).equals(L_strTEMP.trim()))
		             					{
		             						setMSG("Duplicate Date entry for .."+L_strTEMP,'E');
		             						return false;
		             					}
	             		   	    	}
	             		   	    	else
	             		   	    	{
	             		   	    		if(tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim().length() >0)
		             					if((tblDODEL.getValueAt(i,intTB2_PRDDS).toString().trim()).equals(strTEMP.trim()))
		             					{
		             						setMSG("Duplicate Date entry for Immediate delivery of .."+strTEMP,'E');
		             						return false;
		             					}
	             		   	    	}
	             		   	    	
	             		   	    	
	             				}

             		   	    }
					    }
			 		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
             		    {
             		        if(tblDODEL.getValueAt(P_intROWID,intTB2_SRLNO).equals(""))
             		            tblDODEL.setValueAt(getSRLNO(P_intROWID),P_intROWID ,intTB2_SRLNO);
             		    }
                    }
					else if(P_intCOLID==intTB2_DORQT)
					{
						float L_fltDODQT = getDOD_TOTQT(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PRDCD).toString(),tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PKGTP).toString());
						float L_fltDOTQT = getDOT_DORQT(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PRDCD).toString(),tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PKGTP).toString());
						if(L_fltDODQT < L_fltDOTQT && getDOT_DELTP(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PRDCD).toString(),tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PKGTP).toString()).equals("S"))
						{
							if(chkAUTOCF.isSelected())
							{
							   // System.out.println("auto c/f selected");
								tblDODEL.setValueAt(setNEWDT(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_DSPDT).toString(),1),tblDODEL.getSelectedRow()+1,intTB2_DSPDT);
								tblDODEL.setValueAt("0"+String.valueOf(Integer.parseInt(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_SRLNO).toString())+1),tblDODEL.getSelectedRow()+1,intTB2_SRLNO);
								tblDODEL.setValueAt(setNumberFormat(L_fltDOTQT-L_fltDODQT,3),tblDODEL.getSelectedRow()+1,intTB2_DORQT);
							}
							/*System.out.println("after auto c/f ");
							tblDODEL.setValueAt(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PRDCD),tblDODEL.getSelectedRow()+1,intTB2_PRDCD);
							tblDODEL.setValueAt(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PRDDS),tblDODEL.getSelectedRow()+1,intTB2_PRDDS);
							tblDODEL.setValueAt(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PKGTP),tblDODEL.getSelectedRow()+1,intTB2_PKGTP);
							*/
						}
						else if(Float.parseFloat(setNumberFormat(L_fltDODQT,3)) > Float.parseFloat(setNumberFormat(L_fltDOTQT,3)))
							if(setNumberFormat(L_fltDOTQT,3)==setNumberFormat(getDOD_RUNQT(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PRDCD).toString(),tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PKGTP).toString(),tblDODEL.getSelectedRow()),3))
								clrTBLDODEL_REST(tblDODEL.getSelectedRow());
							else
								{setMSG("Delivery Schedule Qty. Exceeds Grade Qty."+Float.floatToRawIntBits(L_fltDODQT)+ " / "+Float.floatToRawIntBits(L_fltDOTQT),'E'); return false;}
					}
				}
			}
			catch(Exception e)
				{setMSG(e,"Child.Table Verifier"); return false;}
			return true;
		}
	}
	

/**  Sub-procedure for setting party details
 */
private boolean setPRTNM_1(String LP_PRTTP, JTextField LP_CDFLD, JTextField LP_NMFLD, String LP_MSGDS, String LP_PTADR)
{
	try
	{
		M_strSQLQRY = "Select PT_PRTCD, PT_PRTNM, PT_ADR01, PT_ADR02, PT_ADR03 from co_ptmst where pt_prttp = '"+LP_PRTTP+"' and pt_prtcd = '"+LP_CDFLD.getText()+"'";
		//System.out.println(M_strSQLQRY);
		ResultSet T_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(T_rstRSSET==null || (!T_rstRSSET.next()))
			{setMSG(LP_MSGDS+ " "+txtTRPCD.getText()+" not found in Party Master",'E'); return false;}
		LP_NMFLD.setText(getRSTVAL(T_rstRSSET,"PT_PRTNM","C"));
		LP_PTADR=T_rstRSSET.getString("PT_PRTNM")+"\n"+T_rstRSSET.getString("PT_ADR01")+"\n"+T_rstRSSET.getString("PT_ADR02")+"\n"+T_rstRSSET.getString("PT_ADR03");
		T_rstRSSET.close();
	}
    catch(Exception L_EX)
    {
           setMSG(L_EX,"setPRTNM_1");
    }
	return true;
}


/** Putting values into Grde Entry table,  from hstINTRN
 */
private void setINTRN(String LP_INTKEY, int LP_ROWNO)
{
	try
	{
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_SRLNO"),LP_ROWNO,intTB1_SRLNO);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_PRDCD"),LP_ROWNO,intTB1_PRDCD);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_PRDDS"),LP_ROWNO,intTB1_PRDDS);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_PKGDS"),LP_ROWNO,intTB1_PKGDS);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_INDQT"),LP_ROWNO,intTB1_INDQT);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_BALQT"),LP_ROWNO,intTB1_BALQT);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_BASRT"),LP_ROWNO,intTB1_BASRT);
		if(strMKTTP.equals(strMKTTP_CAP))
			tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_BASRT"),LP_ROWNO,intTB1_BASRT);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_STKQT"),LP_ROWNO,intTB1_STKQT);
		tblDOTRN.setValueAt(getGRTRN(LP_INTKEY.substring(0,20),"GR_QLHQT"),LP_ROWNO,intTB1_QLHQT);
		tblDOTRN.setValueAt(getINTRN(LP_INTKEY,"INT_PKGTP"),LP_ROWNO,intTB1_PKGTP);
	}
    catch(Exception L_EX)
    {
           setMSG(L_EX,"setINTRN");
    }
}


/**
 */
private void setDOTRN(String LP_DOTKEY)
{
	try
	{
		for(int i=0;i<tblDOTRN.getRowCount();i++)
		{
			if(tblDOTRN.getValueAt(i,intTB1_PRDCD).toString().length()==0)
				break;
			if(LP_DOTKEY.equals(tblDOTRN.getValueAt(i,intTB1_PRDCD).toString()+tblDOTRN.getValueAt(i,intTB1_PKGTP).toString()))
			 {
				tblDOTRN.setValueAt(getDOTRN(LP_DOTKEY,"DOT_DORQT"),i,intTB1_DORQT);
				tblDOTRN.setValueAt(getDOTRN(LP_DOTKEY,"DOT_DORPK"),i,intTB1_DORPK);
				tblDOTRN.setValueAt(getDOTRN(LP_DOTKEY,"DOT_DELTP"),i,intTB1_DELTP);
				tblDOTRN.setValueAt(getINTRN(strMKTTP+txtINDNO.getText()+LP_DOTKEY,"INT_BASRT"),i,intTB1_BASRT);
			 }
		}
	}
    catch(Exception L_EX)
    {
           setMSG(L_EX,"setDOTRN");
    }
}

	


/**  Setting value in Remark TextField from Indent / DO remark (As applicable)
 */	
private void setRMMST()
{
	ResultSet L_rstRSSET = null;
	try
	{
		txtREMDS.setText("");
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)) // During addition, Fetch Indent Remark
		{
			M_strSQLQRY = "Select * from MR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP = '"+strMKTTP+"' and RM_DOCNO='"+txtINDNO.getText()+"'";
			//System.out.println(M_strSQLQRY);
			L_rstRSSET=cl_dat.exeSQLQRY3(M_strSQLQRY);
		}
		else   // Modification / Deltion / Enquiry ...  Fetch from D.O. Remark
		{
			M_strSQLQRY = "Select * from mr_rmmst where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rm_mkttp = '"+strMKTTP+"' and rm_trntp='DO' and rm_docno='"+txtDORNO.getText()+"'";
			//System.out.println(M_strSQLQRY);
			L_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			
		}
		if(L_rstRSSET!=null && L_rstRSSET.next())
			{txtREMDS.setText(L_rstRSSET.getString("RM_REMDS")); L_rstRSSET.close();}
	}
    catch(Exception L_EX)
    {
           setMSG(L_EX,"setRMMST");
    }
}
	

/** Setting default values for Captive Consumption category
 */
private void setCAPTIVE_1()
{
	if(M_strSBSCD.equals("511600"))
	{
	    //SETTING DATA FOR CAPTIVE CONSUMPTION
		if(tblDOTRN.isEditing())
			tblDOTRN.getCellEditor().stopCellEditing();
		if(tblDODEL.isEditing())
			tblDODEL.getCellEditor().stopCellEditing();
		txtTRPCD.setText("S0029");
		txtTRPNM.setText("SELF");
	}
}
	/** Setting values of Text fields from Indent Master & Transaction table
	 */
	private boolean setINMST(String LP_MKTTP, String LP_INDNO, String LP_ADDSTR, String LP_CLRMSG)
	{
		ResultSet L_rstRSSET = null;
		int i = 0;
		try
		{
			if(LP_CLRMSG.equalsIgnoreCase("CLEAR"))
				clrCOMP_1();
			
			M_strSQLQRY = "Select * from MR_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP='"+LP_MKTTP+"' and IN_INDNO = '"+LP_INDNO+"'"+LP_ADDSTR;
			//System.out.println(M_strSQLQRY);
			L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET==null || (!L_rstRSSET.next()))
				{//setMSG("Record Not found in MR_INMST for : "+LP_INDNO,'E'); 
				    return false;}
			if(L_rstRSSET.getString("IN_STSFL").equals("0"))
				{setMSG("Indent is not Authorised..",'E');	return false;}
			if (!L_rstRSSET.getString("IN_SALTP").equals(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())))
			{setMSG("Invalid Sale Type   IN_SALTP : "+L_rstRSSET.getString("IN_SALTP")+" getCODCD(SYSMR00SAL+cmbSALTP.getSelectedItem().toString()) : "+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()),'E'); return false;}
			if(L_rstRSSET.getString("IN_STSFL").equals("X"))
			{
				setMSG("Indent is Cancelled ..",'E');	return false;
			}
			M_strSBSCD=L_rstRSSET.getString("IN_SBSCD");
			txtINDNO.setText(getRSTVAL(L_rstRSSET,"IN_INDNO","C"));
			txtBYRCD.setText(getRSTVAL(L_rstRSSET,"IN_BYRCD","C"));
			txtCNSCD.setText(getRSTVAL(L_rstRSSET,"IN_CNSCD","C"));
			lblDSRTP.setText(getRSTVAL(L_rstRSSET,"IN_DSRTP","C"));
			txtDSRCD.setText(getRSTVAL(L_rstRSSET,"IN_DSRCD","C"));
			if(strMKTTP.equals(strMKTTP_CAP))
    		{
    		    txtTRPCD.setText("S0029");
    		}
    		else
    		{
                if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
                {
                   String L_strTEMP =nvlSTRVL(getRSTVAL(L_rstRSSET,"IN_TRPCD","C"),"");
                   if(L_strTEMP.length() > 0)
                   {
                        txtTRPCD.setText(L_strTEMP);
                   }
                   txtTRPNM.setText(getRSTVAL(L_rstRSSET,"IN_TRPNM","C"));
                }
    		}
          	lblDSTCD.setText(getRSTVAL(L_rstRSSET,"IN_DSTCD","C"));
			lblDSTDS.setText("Destination : "+getCDTRN("SYSCOXXDST"+getRSTVAL(L_rstRSSET,"IN_DSTCD","C"),"CMT_CODDS",hstCDTRN));
			txtDLCCD.setText("01");
			lblDLCDS.setText("Nagothane");
			setCMBDFT(cmbMKTTP,"MSTCOXXMKT",getRSTVAL(L_rstRSSET,"IN_MKTTP","C"));
			setCMBDFT(cmbDTPCD,"SYSMRXXDTP",getRSTVAL(L_rstRSSET,"IN_DTPCD","C"));
			setCMBDFT(cmbMOTCD,"SYSMR01MOT",getRSTVAL(L_rstRSSET,"IN_MOTCD","C"));
			txtFRTRT.setVisible(false);
			lblFRTRT.setVisible(false);
			if((strFRTCT_DTP.indexOf(getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString())))!= -1)
				if (!(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())).equals("03"))   //Other than Captive
					{txtFRTRT.setVisible(true); lblFRTRT.setVisible(true);}
					
			cl_dat.M_txtUSER_pbst.setText(getRSTVAL(L_rstRSSET,"IN_LUSBY","C"));
			cl_dat.M_txtDATE_pbst.setText(getRSTVAL(L_rstRSSET,"IN_LUPDT","D"));
			txtBYRNM.setText(getRSTVAL(L_rstRSSET,"IN_BYRNM","C"));
			txtCNSNM.setText(getRSTVAL(L_rstRSSET,"IN_CNSNM","C"));
			txtDSRNM.setText(getRSTVAL(L_rstRSSET,"IN_DSRNM","C"));
			L_rstRSSET.close();
			if(txtTRPCD.getText().length()== 0)
			{
    	        L_rstRSSET = null;
        		String L_strDORNO="";
				M_strSQLQRY = "Select max(DOT_DORNO) DOT_DORNO from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"+strMKTTP+"' and DOT_INDNO in (select IN_INDNO from MR_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_CNSCD = '"+txtCNSCD.getText()+"' and IN_MKTTP = '"+strMKTTP+"')";
				//System.out.println(M_strSQLQRY);
    			L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
    			if(L_rstRSSET!=null)
    			{
    			    if(L_rstRSSET.next())
    			        L_strDORNO = getRSTVAL(L_rstRSSET,"DOT_DORNO","C"); 
    			    L_rstRSSET.close();
    			}
    			if(L_strDORNO.length()>0)
    			{	
					M_strSQLQRY = "Select DOT_TRPCD from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '"+strMKTTP+"' and DOT_DORNO = '"+L_strDORNO+"'  and DOT_STSFL <>'X'";
					//System.out.println(M_strSQLQRY);
    				L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
        			if(L_rstRSSET!=null)
        			{   
        			    if(L_rstRSSET.next())
        			        txtTRPCD.setText(getRSTVAL(L_rstRSSET,"DOT_TRPCD","C"));
        				L_rstRSSET.close();
        			}
    			}
    			if(txtTRPCD.getText().length() > 0)
    			    if(!setPRTNM_1("T",txtTRPCD,txtTRPNM,"Transporter",strTRPAD))
    				    return false;
    		}
        	///if (!setPRTNM())
			///	return false;
			///setRMMST();
    		// Fetching the remarks
    		L_rstRSSET = null;
    		txtREMDS.setText("");
    		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)) // During addition, Fetch Indent Remark
			{
				M_strSQLQRY = "Select * from MR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP = '"+strMKTTP+"' and RM_DOCNO='"+txtINDNO.getText()+"'";
				//System.out.println(M_strSQLQRY);
    			L_rstRSSET=cl_dat.exeSQLQRY3(M_strSQLQRY);
			}
    		else   // Modification / Deltion / Enquiry ...  Fetch from D.O. Remark
			{
				M_strSQLQRY = "Select * from mr_rmmst where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rm_mkttp = '"+strMKTTP+"' and rm_trntp='DO' and rm_docno='"+txtDORNO.getText()+"'";
				//System.out.println(M_strSQLQRY);
    			L_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			}
    		if(L_rstRSSET!=null)
    		{
    		    if(L_rstRSSET.next())
    		        txtREMDS.setText(L_rstRSSET.getString("RM_REMDS"));
    		     L_rstRSSET.close();
    		}
    		if (!crtINTRN(LP_MKTTP,LP_INDNO," AND INT_STSFL <>'X' "))
				return false;
    		if (!crtGRTRN(LP_MKTTP,LP_INDNO," AND INT_STSFL <>'X' "))
				{setMSG("No Quality Hold Lots for this Customer Order",'N');}
			inlTBLEDIT(tblDOTRN);
			tblDOTRN.clrTABLE();
			setHST_ARR(hstINSRL);
			int intDOTROW = 0;
			for(int intARRCT=0;intARRCT<arrHSTKEY.length;intARRCT++)
				{setINTRN(hstINSRL.get(arrHSTKEY[intARRCT]).toString(),i); i++;}

		
			Enumeration enmDOTKEYS=hstDOTRN.keys();
			while(enmDOTKEYS.hasMoreElements())
				setDOTRN((String)enmDOTKEYS.nextElement());

			// check
			///if(!setDODEL(0))
			///	setDODEL_DEFAULT(0);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				crtINDEL(LP_MKTTP, txtINDNO.getText().toString());
	    	}
			setENBL(true);
	    }
	    catch(Exception L_EX)
			{setMSG(L_EX,"setINMST"); return false;}
		return true;
	}
	

	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	 */
	 private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
	{
		String L_strSQLQRY = "";
	    try
	    {
	        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
			//System.out.println(L_strSQLQRY);
			
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	            setMSG("Records not found in CO_CDTRN",'E');
				 //System.out.println(L_strSQLQRY);
	            return;
	        }
	        while(true)
	        {
	                strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
	                strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
	                strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
	                String[] staCDTRN = new String[intCDTRN_TOT];
	                staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
	                staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
	                staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
	                staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
	                staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
	                staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
	                staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
	                staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
	                staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
	                LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
					hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
	                if (!L_rstRSSET.next())
	                        break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtCDTRN");
	    }
	}




	/** One time data capturing  MR_INTRN
	 * into the Hash Table
	 */
	 private boolean crtINTRN(String LP_MKTTP, String LP_INDNO, String LP_ADDSTR)
	{
	    try
	    {
			hstINTRN.clear();
			hstINSRL.clear();
			String L_strSQLQRY = "select INT_SRLNO,INT_PRDCD,INT_PKGTP,INT_PRDDS,isnull(int_indqt,0) INT_INDQT,isnull(INT_FCMQT,0) INT_FCMQT,isnull(INT_DORQT,0) INT_DORQT,INT_BASRT,isnull(INT_PKGWT,0) INT_PKGWT,isnull(INT_INVQT,0) INT_INVQT,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) INT_STKQT from MR_INTRN left outer join FG_STMST on INT_PRDCD=ST_PRDCD AND INT_PKGTP=ST_PKGTP "
	                                                 + " where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP ='"+LP_MKTTP+"' and INT_INDNO = '"+LP_INDNO+"'"+LP_ADDSTR+" group by INT_SRLNO,INT_PRDCD,INT_PKGTP,INT_PRDDS,INT_INDQT,INT_FCMQT,INT_DORQT,INT_BASRT,INT_PKGWT,INT_INVQT";
			//System.out.println(L_strSQLQRY);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	             setMSG("Records not found in MR_INTRN for "+LP_MKTTP+"/"+LP_INDNO,'E');
	              return false;
	        }
	        while(true)
	        {
	                String[] staINTRN = new String[intINTRN_TOT];
	                staINTRN[intAE_INT_SRLNO] = getRSTVAL(L_rstRSSET,"INT_SRLNO","C");
	                staINTRN[intAE_INT_PRDCD] = getRSTVAL(L_rstRSSET,"INT_PRDCD","C");
	                staINTRN[intAE_INT_PRDDS] = getRSTVAL(L_rstRSSET,"INT_PRDDS","C");
	                staINTRN[intAE_INT_PKGDS] = getCDTRN("SYSFGXXPKG"+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"),"CMT_CODDS",hstCDTRN);
	                staINTRN[intAE_INT_INDQT] = getRSTVAL(L_rstRSSET,"INT_INDQT","N");
	                staINTRN[intAE_INT_FCMQT] = getRSTVAL(L_rstRSSET,"INT_FCMQT","N");
	                staINTRN[intAE_INT_BALQT] = setNumberFormat(Double.parseDouble(getRSTVAL(L_rstRSSET,"INT_INDQT","C"))-Double.parseDouble(getRSTVAL(L_rstRSSET,"INT_DORQT","C")) - Double.parseDouble(getRSTVAL(L_rstRSSET,"INT_FCMQT","C")),3);
	                staINTRN[intAE_INT_BASRT] = getRSTVAL(L_rstRSSET,"INT_BASRT","N");
	                staINTRN[intAE_INT_STKQT] = getRSTVAL(L_rstRSSET,"INT_STKQT","N");
	                staINTRN[intAE_INT_PKGTP] = getRSTVAL(L_rstRSSET,"INT_PKGTP","C");
	                staINTRN[intAE_INT_DORQT] = getRSTVAL(L_rstRSSET,"INT_DORQT","C");
	                staINTRN[intAE_INT_INVQT] = getRSTVAL(L_rstRSSET,"INT_INVQT","C");
	                hstINTRN.put(LP_MKTTP+LP_INDNO+getRSTVAL(L_rstRSSET,"INT_PRDCD","C")+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"),staINTRN);
	                hstDOTRN.put(LP_MKTTP+getRSTVAL(L_rstRSSET,"INT_PRDCD","C")+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"),staINTRN);
	                hstINSRL.put(getRSTVAL(L_rstRSSET,"INT_SRLNO","C"),LP_MKTTP+LP_INDNO+getRSTVAL(L_rstRSSET,"INT_PRDCD","C")+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"));
	                if (!L_rstRSSET.next())
	                        break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtINTRN");
	    }
		 return true;
	}


	 
	/** One time data capturing  MR_INTRN
	 * into the Hash Table
	 */
	 private boolean crtINTRN_1(String LP_MKTTP, String LP_INDNO, String LP_ADDSTR)
	{
	    try
	    {
			hstINTRN_1.clear();
			hstINSRL.clear();
			String L_strSQLQRY = "select INT_SRLNO,INT_PRDCD,INT_PKGTP,INT_PRDDS,isnull(int_indqt,0) INT_INDQT,isnull(INT_FCMQT,0) INT_FCMQT,isnull(INT_DORQT,0) INT_DORQT,INT_BASRT,isnull(INT_PKGWT,0) INT_PKGWT,isnull(INT_INVQT,0) INT_INVQT,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) INT_STKQT from MR_INTRN left outer join FG_STMST on INT_PRDCD=ST_PRDCD AND INT_PKGTP=ST_PKGTP "
	                                                 + " where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP ='"+LP_MKTTP+"' and INT_INDNO = '"+LP_INDNO+"'"+LP_ADDSTR+" group by INT_SRLNO,INT_PRDCD,INT_PKGTP,INT_PRDDS,INT_INDQT,INT_FCMQT,INT_DORQT,INT_BASRT,INT_PKGWT,INT_INVQT";
			//System.out.println(L_strSQLQRY);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	             setMSG("Records not found in MR_INTRN for "+LP_MKTTP+"/"+LP_INDNO,'E');
	              return false;
	        }
	        while(true)
	        {
	                String[] staINTRN = new String[intINTRN_TOT];
	                staINTRN[intAE_INT_SRLNO] = getRSTVAL(L_rstRSSET,"INT_SRLNO","C");
	                staINTRN[intAE_INT_PRDCD] = getRSTVAL(L_rstRSSET,"INT_PRDCD","C");
	                staINTRN[intAE_INT_PRDDS] = getRSTVAL(L_rstRSSET,"INT_PRDDS","C");
	                staINTRN[intAE_INT_PKGDS] = getCDTRN("SYSFGXXPKG"+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"),"CMT_CODDS",hstCDTRN);
	                staINTRN[intAE_INT_INDQT] = getRSTVAL(L_rstRSSET,"INT_INDQT","N");
	                staINTRN[intAE_INT_FCMQT] = getRSTVAL(L_rstRSSET,"INT_FCMQT","N");
	                staINTRN[intAE_INT_BALQT] = setNumberFormat(Double.parseDouble(getRSTVAL(L_rstRSSET,"INT_INDQT","C"))-Double.parseDouble(getRSTVAL(L_rstRSSET,"INT_DORQT","C")) -Double.parseDouble(getRSTVAL(L_rstRSSET,"INT_FCMQT","C")),3);
	                staINTRN[intAE_INT_BASRT] = getRSTVAL(L_rstRSSET,"INT_BASRT","N");
	                staINTRN[intAE_INT_STKQT] = getRSTVAL(L_rstRSSET,"INT_STKQT","N");
	                staINTRN[intAE_INT_PKGTP] = getRSTVAL(L_rstRSSET,"INT_PKGTP","C");
	                staINTRN[intAE_INT_DORQT] = getRSTVAL(L_rstRSSET,"INT_DORQT","C");
	                staINTRN[intAE_INT_INVQT] = getRSTVAL(L_rstRSSET,"INT_INVQT","C");
	                hstINTRN_1.put(LP_MKTTP+LP_INDNO+getRSTVAL(L_rstRSSET,"INT_PRDCD","C")+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"),staINTRN);
	                hstDOTRN_1.put(LP_MKTTP+getRSTVAL(L_rstRSSET,"INT_PRDCD","C")+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"),staINTRN);
	                hstINSRL.put(getRSTVAL(L_rstRSSET,"INT_SRLNO","C"),LP_MKTTP+LP_INDNO+getRSTVAL(L_rstRSSET,"INT_PRDCD","C")+getRSTVAL(L_rstRSSET,"INT_PKGTP","C"));
	                if (!L_rstRSSET.next())
	                        break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtINTRN");
	    }
		 return true;
	}
	 
	 
	/** One time data capturing  FG_STMST (gradewise Qulity Hold stock)
	 * into the Hash Table
	 */
	 private boolean crtGRTRN(String LP_MKTTP, String LP_INDNO, String LP_ADDSTR)
	{
	    try
	    {
			hstGRTRN.clear();
			M_strSQLQRY = "select ST_PRDCD,sum(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) GR_QLHQT from FG_STMST,PR_LTMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO = LT_RCLNO and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_RESFL in ('Q','H') and LT_PRDCD in (select INT_PRDCD from MR_INTRN  where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP ='"+LP_MKTTP+"' and INT_INDNO = '"+LP_INDNO+"'"+LP_ADDSTR+") group by ST_PRDCD" ;
			//System.out.println(M_strSQLQRY);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	             setMSG("Records not found in MR_INTRN for "+LP_MKTTP+"/"+LP_INDNO,'E');
	              return false;
	        }
	        while(true)
	        {
	                String[] staGRTRN = new String[intGRTRN_TOT];
	                staGRTRN[intAE_GR_PRDCD] = getRSTVAL(L_rstRSSET,"ST_PRDCD","C");
	                staGRTRN[intAE_GR_QLHQT] = getRSTVAL(L_rstRSSET,"GR_QLHQT","N");
	                hstGRTRN.put(LP_MKTTP+LP_INDNO+getRSTVAL(L_rstRSSET,"ST_PRDCD","C"),staGRTRN);
					//System.out.println("putting : "+LP_MKTTP+LP_INDNO+getRSTVAL(L_rstRSSET,"ST_PRDCD","C")+"/"+getRSTVAL(L_rstRSSET,"GR_QLHQT","N"));
	                if (!L_rstRSSET.next())
	                        break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtGRTRN");
	    }
		 return true;
	}
	 
	 
	 /** Displaying delivery schedule for specified row of Grade Entry table
	 */
	private void setDODEL_DEFAULT(int LP_ROWNO)
	{
		float L_fltDORQT = 0.00f;
		try
		{
		    //System.out.println("from set dodel default");
			tblDODEL.clrTABLE();
			inlTBLEDIT(tblDODEL);
			lblPRDCD.setText(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString());
			String L_strDSPDT = cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) ? cl_dat.M_strLOGDT_pbst : strDORDT;
			for (int i=0;i<tblDODEL.getRowCount();i++)
			{
				if(tblDODEL.getValueAt(i,intTB2_SRLNO).toString().length()==0) 
					break;
				L_fltDORQT += Float.parseFloat(nvlSTRVL(tblDODEL.getValueAt(i,intTB2_DORQT).toString(),"0.00"));
			}
			if(L_fltDORQT >= Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00")))
				return;
			tblDODEL.setValueAt("01",0,intTB2_SRLNO);
			//System.out.println("setting from default");
			tblDODEL.setValueAt(setNEWDT(L_strDSPDT,1),0,intTB2_DSPDT);
			tblDODEL.setValueAt(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"),0,intTB2_DORQT);
			tblDODEL.setValueAt(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"0.00"),0,intTB2_PRDCD);
			tblDODEL.setValueAt(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PRDDS).toString(),"0.00"),0,intTB2_PRDDS);
			tblDODEL.setValueAt(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"0.00"),0,intTB2_PKGTP);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setDODEL_DEFAULT");
		}
	}



	/** One time data capturing from MR_DOTRN
	 * into the Hash Table
	 */
	 private void crtDOTRN(ResultSet LP_RSSET)
	{
	    try
	    {
	        String[] staDOTRN = new String[intDOTRN_TOT];
	        staDOTRN[intAE_DOT_DORQT] = getRSTVAL(LP_RSSET,"DOT_DORQT","C");
	        staDOTRN[intAE_DOT_DORPK] = getRSTVAL(LP_RSSET,"DOT_DORPK","C");
	        staDOTRN[intAE_DOT_DELTP] = getRSTVAL(LP_RSSET,"DOT_DELTP","C");
	        staDOTRN[intAE_DOT_LADQT] = getRSTVAL(LP_RSSET,"DOT_LADQT","C");
	        staDOTRN[intAE_DOT_INVQT] = getRSTVAL(LP_RSSET,"DOT_INVQT","C");
	        hstDOTRN.put(getRSTVAL(LP_RSSET,"DOT_PRDCD","C")+getRSTVAL(LP_RSSET,"DOT_PKGTP","C"),staDOTRN);
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtDOTRN");
	    }
	}

	 
	/** One time data capturing from MR_DOTRN
	 * into the Hash Table
	 */
	 private void crtDOTRN_1(ResultSet LP_RSSET)
	{
	    try
	    {
	        String[] staDOTRN = new String[intDOTRN_TOT];
	        staDOTRN[intAE_DOT_DORQT] = getRSTVAL(LP_RSSET,"DOT_DORQT","C");
	        staDOTRN[intAE_DOT_DORPK] = getRSTVAL(LP_RSSET,"DOT_DORPK","C");
	        staDOTRN[intAE_DOT_DELTP] = getRSTVAL(LP_RSSET,"DOT_DELTP","C");
	        staDOTRN[intAE_DOT_LADQT] = getRSTVAL(LP_RSSET,"DOT_LADQT","C");
	        staDOTRN[intAE_DOT_INVQT] = getRSTVAL(LP_RSSET,"DOT_INVQT","C");
	        hstDOTRN_1.put(getRSTVAL(LP_RSSET,"DOT_PRDCD","C")+getRSTVAL(LP_RSSET,"DOT_PKGTP","C"),staDOTRN);
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtDOTRN_1");
	    }
	}
	 
	 
	 
	/** One time data capturing  MR_DODEL
	 * into the JTable
	 */
	 private void setDODEL_NEW(String LP_MKTTP, String LP_DORNO, String LP_ADDSTR)
	{
	    try
	    {
	        int L_intROWNO =0;
            String L_strSQLQRY = "select DOD_SRLNO,DOD_DSPDT,DOD_DORQT,DOD_PRDCD,DOD_PKGTP,DOD_LADQT,DOT_PRDDS from MR_DOTRN,MR_DODEL "
            +" where DOT_MKTTP = DOD_MKTTP AND DOT_DORNO = DOD_DORNO AND DOT_PRDCD = DOD_PRDCD AND DOT_PKGTP = DOD_PKGTP AND DOT_CMPCD = DOD_CMPCD AND DOD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND dod_mkttp='"+LP_MKTTP+"' and DOD_DORNO = '"+LP_DORNO+"'"+LP_ADDSTR;
			//System.out.println(L_strSQLQRY);
           /// hstDODEL.clear();
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	             setMSG("Records not found in MR_DODEL for "+LP_MKTTP+"/"+LP_DORNO,'E');
	              return;
	        }
	        while(true)
	        {
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"DOD_SRLNO","C"),L_intROWNO,intTB2_SRLNO);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"DOD_DSPDT","D"),L_intROWNO,intTB2_DSPDT);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"DOD_DORQT","N"),L_intROWNO,intTB2_DORQT);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"DOD_PRDCD","C"),L_intROWNO,intTB2_PRDCD);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"DOT_PRDDS","C"),L_intROWNO,intTB2_PRDDS);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"DOD_PKGTP","C"),L_intROWNO,intTB2_PKGTP);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"DOD_LADQT","C"),L_intROWNO,intTB2_LADQT);
                if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
                {
                    tblDODEL.setValueAt(new Boolean("true"),L_intROWNO,intTB2_CHKFL);
                }
                L_intROWNO++;
                if (!L_rstRSSET.next())
                    break;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtDODEL");
	    }
	} 
	 private void crtINDEL(String LP_MKTTP, String LP_INDNO)
	{
	    try
	    {
	        int L_intROWNO =0;
			//System.out.println("crtINDEL");
			///hstINDEL.clear();
			M_strSQLQRY = "select INT_SRLNO,INT_PRDCD,INT_PRDDS,IND_PRDCD,IND_PKGTP,IND_SRLNO,IND_DELTP,IND_INDQT,IND_DSPDT from MR_INTRN,MR_INDEL where ind_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and  ind_mkttp='"+LP_MKTTP+"' and IND_INDNO = '"+LP_INDNO+"' and isnull(INT_STSFL,'')<>'X' and isnull(IND_STSFL,'')<>'X' and IND_CMPCD = INT_CMPCD and IND_MKTTP = INT_MKTTP AND IND_INDNO = INT_INDNO AND IND_PRDCD = INT_PRDCD AND IND_PKGTP = INT_PKGTP  ORDER BY INT_SRLNO,IND_PRDCD,IND_SRLNO ";
			//System.out.println(M_strSQLQRY);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
	       // System.out.println("crtINDEL start");
	        if(L_rstRSSET == null)
	        {
	             setMSG("Records not found in MR_INDEL for "+LP_MKTTP+"/"+LP_INDNO,'E');
	              return;
	        }
            while(L_rstRSSET.next())
	        {
	            if(M_strSBSCD.equals("511600"))
	                tblDODEL.setValueAt(new Boolean("true"),L_intROWNO,intTB2_CHKFL);
	            tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"INT_PRDDS","C"),L_intROWNO,intTB2_PRDDS);
	            tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"IND_DSPDT","D"),L_intROWNO,intTB2_DSPDT);
	            tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"IND_INDQT","N"),L_intROWNO,intTB2_DORQT);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"IND_SRLNO","C"),L_intROWNO,intTB2_SRLNO);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"IND_PRDCD","C"),L_intROWNO,intTB2_PRDCD);
                tblDODEL.setValueAt(getRSTVAL(L_rstRSSET,"IND_PKGTP","C"),L_intROWNO,intTB2_PKGTP);
               // System.out.println("del tp :"+getDOT_DELTP(getRSTVAL(L_rstRSSET,"IND_PRDCD","C"),getRSTVAL(L_rstRSSET,"IND_PKGTP","C")));
               // tblDOTRN.setValueAt(getDOT_DELTP(getRSTVAL(L_rstRSSET,"IND_PRDCD","C"),getRSTVAL(L_rstRSSET,"IND_PKGTP","C")),L_intROWNO,intTB1_DELTP);
               if(getRSTVAL(L_rstRSSET,"IND_SRLNO","C").equals("01"))
               {
                    ///getDOT_DELTP(getRSTVAL(L_rstRSSET,"IND_PRDCD","C"),getRSTVAL(L_rstRSSET,"IND_PKGTP","C"));
                   for(int intDOTCTR=0;intDOTCTR<tblDOTRN.getRowCount();intDOTCTR++)
        			{
        				if(tblDOTRN.getValueAt(intDOTCTR,intTB1_PRDCD).toString().length()==10)
        				{
        					if(getRSTVAL(L_rstRSSET,"IND_PRDCD","C").equals(tblDOTRN.getValueAt(intDOTCTR,intTB1_PRDCD).toString()) && getRSTVAL(L_rstRSSET,"IND_PKGTP","C").equals(tblDOTRN.getValueAt(intDOTCTR,intTB1_PKGTP).toString()))
        						tblDOTRN.setValueAt(getRSTVAL(L_rstRSSET,"IND_DELTP","C"),intDOTCTR,intTB1_DELTP);
        				}
        			}
    			}
               // hstINDEL.put(getRSTVAL(L_rstRSSET,"IND_PRDCD","C")+getRSTVAL(L_rstRSSET,"IND_PKGTP","C")+getRSTVAL(L_rstRSSET,"IND_SRLNO","C"),staINDEL);
                L_intROWNO++;
	        }
	        if(L_intROWNO  == 0 )
	        {
	             setMSG("Records not found in MR_INDEL for "+LP_MKTTP+"/"+LP_INDNO,'E');
	              return;
	        }
	        L_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
	           setMSG(L_EX,"crtINDEL");
	    }
	}
		
	/**  Setting combo position to default value specified in parameter
	*/
	private void setCMBDFT(JComboBox LP_CMBNM, String LP_CODCT, String LP_CMBVL)
	{
		try
		{
			//System.out.println("setCMBDFT : "+LP_CODCT+"/"+LP_CMBVL);
			int L_intCMBITMS = LP_CMBNM.getItemCount();
			String L_strCMBDS = getCDTRN(LP_CODCT+LP_CMBVL,"CMT_CODDS",hstCDTRN);
			for(int i=0;i<LP_CMBNM.getItemCount();i++)
			{
				if((LP_CMBNM.getItemAt(i).toString()).equalsIgnoreCase(L_strCMBDS))
				{
					LP_CMBNM.setSelectedItem(LP_CMBNM.getItemAt(i).toString());
					LP_CMBNM.setSelectedIndex(i);
					break;
				}
			}
		}
	catch(Exception e){setMSG(e,"setCMBDFT");}
	}
	 
	 
	 
	/** Populating values in Combo Box from Vector
	 */
	private void setCMBVL(JComboBox LP_CMBNM, Vector<String> LP_VTRNM)
	{
		try
		{
			for (int j=LP_CMBNM.getItemCount()-1;j>0;j--)
				LP_CMBNM.removeItemAt(j);
			//LP_CMBNM.removeAllItems();
			for(int i=LP_VTRNM.size()-1 ; i>=0; i--)
	        {
	                LP_CMBNM.addItem(LP_VTRNM.get(i).toString());
					//System.out.println("setCMBVL : "+LP_VTRNM.get(i).toString());				
	        }
		}
		catch(Exception e){setMSG(e,"setCMBVL");}
	}


	/** Initializing Market Type related code transaction details (in Hash Table)
	 *  for re-inserting data.
	 */
/*	private void inlCDTRN()
	{
		try
		{
			Enumeration enmCODKEYS=hstCDTRN.keys();
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals("MSTCOXXMKT"))
				{
					hstCDTRN.remove(L_strCODCD);
				}
			}
		}
		catch(Exception e){setMSG(e,"inlCDTRN");}
	}
*/

			private void inlCDTRN(String LP_CODTP)
		{
			try
			{
				Enumeration enmCODKEYS=hstCDTRN.keys();
				while(enmCODKEYS.hasMoreElements())
				{
					String L_strCODCD = (String)enmCODKEYS.nextElement();
					if(LP_CODTP.equals("MKT") && (L_strCODCD.substring(0,3).equals("D"+cl_dat.M_strCMPCD_pbst) || L_strCODCD.substring(0,10).equals("MSTCOXXMKT")))
					{
						hstCDTRN.remove(L_strCODCD);
					}
					if(LP_CODTP.equals("SAL") && (L_strCODCD.substring(0,10).equals("SYSMR00SAL")))
					{
						hstCDTRN.remove(L_strCODCD);
					}
				}
			}
			catch (Exception L_Ex)
			{}
		}


	/** Creting a vector for populating items into a Combo Box
	 */
	private void setCDLST(Vector<String> LP_VTRNM, String LP_CODCT, String LP_FLDNM)
	{
		try
		{
			Enumeration enmCODKEYS=hstCDTRN.keys();
			LP_VTRNM.clear();				
			while(enmCODKEYS.hasMoreElements())
			{
				String L_strCODCD = (String)enmCODKEYS.nextElement();
				if(L_strCODCD.substring(0,10).equals(LP_CODCT))
				{
					LP_VTRNM.addElement(getCDTRN(L_strCODCD,LP_FLDNM,hstCDTRN));
				}
			}
			if(!LP_VTRNM.contains("Select"))
				LP_VTRNM.addElement("Select");
		}
	catch(Exception e){setMSG(e,"setCDLST");}
	}
	
	
/**  Conversion of Hash table keys to sorted array
 *   For retrieving elements in sorted order
 */
private void setHST_ARR(Hashtable LP_HSTNM)
{
	try
	{
		arrHSTKEY = LP_HSTNM.keySet().toArray();
		Arrays.sort(arrHSTKEY);
	}
	catch(Exception e){setMSG(e,"setHST_ARR");}
}
	
	
	/** Picking up Specified Codes Transaction related details from Hash Table
	 * <B> for Specified Code Transaction key
	 * @param LP_CDTRN_KEY	Code Transaction key
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
	private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
	{
		try
		{
			//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
			if(!hstCDTRN.containsKey(LP_CDTRN_KEY))
			{
				setMSG(LP_CDTRN_KEY+" not found in hstCDTRN",'E');
				//ResultSet L_rstRSSET = cl_dat.exeSQLQRY3("select * from co_cdtrn where cmt_cgmtp||cmt_cgstp||cmt_codcd = '"+LP_CDTRN_KEY+"'");
			    //if(L_rstRSSET!=null && L_rstRSSET.next())
				//	{String L_strRETSTR = getRSTVAL(L_rstRSSET,LP_FLDNM,"C"); L_rstRSSET.close(); return L_strRETSTR;}
			}
		    if (LP_FLDNM.equals("CMT_CODCD"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
		    else if (LP_FLDNM.equals("CMT_CODDS"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
		    else if (LP_FLDNM.equals("CMT_SHRDS"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
		    else if (LP_FLDNM.equals("CMT_CHP01"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
		    else if (LP_FLDNM.equals("CMT_CHP02"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
		    else if (LP_FLDNM.equals("CMT_NMP01"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
		    else if (LP_FLDNM.equals("CMT_NMP02"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
		    else if (LP_FLDNM.equals("CMT_NCSVL"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
		    else if (LP_FLDNM.equals("CMT_CCSVL"))
		            return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
		return "";
	}

	/** Picking up Specified details from hstINTRN
	 */
	private String getINTRN(String LP_INTRN_KEY, String LP_FLDNM)
	{
		//System.out.println("getINTRN : "+LP_INTRN_KEY+"/"+LP_FLDNM);
		if(!hstINTRN.containsKey(LP_INTRN_KEY))
			{setMSG(LP_INTRN_KEY+" not found in hstINTRN",'E'); return "";}
		try
		{
		        if (LP_FLDNM.equals("INT_SRLNO"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_SRLNO];
		        else if (LP_FLDNM.equals("INT_PRDCD"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_PRDCD];
		        else if (LP_FLDNM.equals("INT_PRDDS"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_PRDDS];
		        else if (LP_FLDNM.equals("INT_PKGDS"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_PKGDS];
		        else if (LP_FLDNM.equals("INT_INDQT"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_INDQT];
		        else if (LP_FLDNM.equals("INT_FCMQT"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_FCMQT];
		        else if (LP_FLDNM.equals("INT_BALQT"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_BALQT];
		        else if (LP_FLDNM.equals("INT_BASRT"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_BASRT];
		        else if (LP_FLDNM.equals("INT_STKQT"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_STKQT];
		        else if (LP_FLDNM.equals("INT_PKGTP"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_PKGTP];
		        else if (LP_FLDNM.equals("INT_DORQT"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_DORQT];
		        else if (LP_FLDNM.equals("INT_INVQT"))
		                return ((String[])hstINTRN.get(LP_INTRN_KEY))[intAE_INT_INVQT];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getINTRN");
		}
		return "";
	}


	
	/** Picking up Specified details from hstINTRN_1
	 */
	private String getINTRN_1(String LP_INTRN_KEY, String LP_FLDNM)
	{
		//System.out.println("getINTRN_1 : "+LP_INTRN_KEY+"/"+LP_FLDNM);
		if(!hstINTRN_1.containsKey(LP_INTRN_KEY))
			{setMSG(LP_INTRN_KEY+" not found in hstINTRN_1",'E'); return "";}
		try
		{
		        if (LP_FLDNM.equals("INT_SRLNO"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_SRLNO];
		        else if (LP_FLDNM.equals("INT_PRDCD"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_PRDCD];
		        else if (LP_FLDNM.equals("INT_PRDDS"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_PRDDS];
		        else if (LP_FLDNM.equals("INT_PKGDS"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_PKGDS];
		        else if (LP_FLDNM.equals("INT_INDQT"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_INDQT];
		        else if (LP_FLDNM.equals("INT_FCMQT"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_FCMQT];
		        else if (LP_FLDNM.equals("INT_BALQT"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_BALQT];
		        else if (LP_FLDNM.equals("INT_BASRT"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_BASRT];
		        else if (LP_FLDNM.equals("INT_STKQT"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_STKQT];
		        else if (LP_FLDNM.equals("INT_PKGTP"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_PKGTP];
		        else if (LP_FLDNM.equals("INT_DORQT"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_DORQT];
		        else if (LP_FLDNM.equals("INT_INVQT"))
		                return ((String[])hstINTRN_1.get(LP_INTRN_KEY))[intAE_INT_INVQT];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getINTRN_1");
		}
		return "";
	}
	
	
	
	/** Picking up Specified details from hstGRTRN
	 */
	private String getGRTRN(String LP_GRTRN_KEY, String LP_FLDNM)
	{
		//System.out.println("getGRTRN : "+LP_GRTRN_KEY+"/"+LP_FLDNM);
		if(!hstGRTRN.containsKey(LP_GRTRN_KEY))
			{setMSG(LP_GRTRN_KEY+" not found in hstGRTRN",'N'); return "0.000";}
		try
		{
		        if (LP_FLDNM.equals("GR_PRDCD"))
		                return ((String[])hstGRTRN.get(LP_GRTRN_KEY))[intAE_GR_PRDCD];
		        else if (LP_FLDNM.equals("GR_QLHQT"))
		                return ((String[])hstGRTRN.get(LP_GRTRN_KEY))[intAE_GR_QLHQT];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getGRTRN");
		}
		return "";
	}
	
	
		
	/** Picking up Specified details from hstDOTRN
	 */
	private String getDOTRN(String LP_DOTRN_KEY, String LP_FLDNM)
	{
		//System.out.println("getDOTRN : "+LP_DOTRN_KEY+"/"+LP_FLDNM);
		try
		{
				if(!hstDOTRN.containsKey(LP_DOTRN_KEY))
					{System.out.println(LP_DOTRN_KEY+ " not found in hstDOTRN"); return "";}
					//{setMSG(LP_DOTRN_KEY+ " not found in hstDOTRN",'E'); return "";}
		        if (LP_FLDNM.equals("DOT_DORQT"))
		                return ((String[])hstDOTRN.get(LP_DOTRN_KEY))[intAE_DOT_DORQT];
		        else if (LP_FLDNM.equals("DOT_DORPK"))
		                return ((String[])hstDOTRN.get(LP_DOTRN_KEY))[intAE_DOT_DORPK];
		        else if (LP_FLDNM.equals("DOT_DELTP"))
		                return ((String[])hstDOTRN.get(LP_DOTRN_KEY))[intAE_DOT_DELTP];
		        else if (LP_FLDNM.equals("DOT_LADQT"))
		                return ((String[])hstDOTRN.get(LP_DOTRN_KEY))[intAE_DOT_LADQT];
		        else if (LP_FLDNM.equals("DOT_INVQT"))
		                return ((String[])hstDOTRN.get(LP_DOTRN_KEY))[intAE_DOT_INVQT];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getDOTRN");
		}
		return "";
	}


	
	/** Picking up Specified details from hstDOTRN
	 */
	private String getDOTRN_1(String LP_DOTRN_KEY, String LP_FLDNM)
	{
		//System.out.println("getDOTRN : "+LP_DOTRN_KEY+"/"+LP_FLDNM);
		try
		{
				if(!hstDOTRN_1.containsKey(LP_DOTRN_KEY))
					{System.out.println(LP_DOTRN_KEY+ " not found in hstDOTRN"); return "";}
					//{setMSG(LP_DOTRN_KEY+ " not found in hstDOTRN",'E'); return "";}
		        if (LP_FLDNM.equals("DOT_DORQT"))
		                return ((String[])hstDOTRN_1.get(LP_DOTRN_KEY))[intAE_DOT_DORQT];
		        else if (LP_FLDNM.equals("DOT_DORPK"))
		                return ((String[])hstDOTRN_1.get(LP_DOTRN_KEY))[intAE_DOT_DORPK];
		        else if (LP_FLDNM.equals("DOT_DELTP"))
		                return ((String[])hstDOTRN_1.get(LP_DOTRN_KEY))[intAE_DOT_DELTP];
		        else if (LP_FLDNM.equals("DOT_LADQT"))
		                return ((String[])hstDOTRN_1.get(LP_DOTRN_KEY))[intAE_DOT_LADQT];
		        else if (LP_FLDNM.equals("DOT_INVQT"))
		                return ((String[])hstDOTRN_1.get(LP_DOTRN_KEY))[intAE_DOT_INVQT];
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getDOTRN_1");
		}
		return "";
	}
	
	

	/** Checking key in table for record existance
	 */
	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
	{
		boolean L_flgCHKFL = false;
		try
		{
			M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
			//System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				L_flgCHKFL = true;
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)	
		{setMSG("Error in chkEXIST : "+L_EX,'E');}
		return L_flgCHKFL;
	}


	/** Deleting Single Quotes(') from a specified string  (for Saving to Database)
	*/
    private String delQuote(String LP_STRVL)
      {
      String L_STRVL = LP_STRVL;
      String L_RETSTR="";
      StringTokenizer L_STRTKN;
      try
        {
          if(LP_STRVL==null)
             return L_STRVL;
          else if (LP_STRVL.length()==0)
             return L_STRVL;
          int L_STRLEN = LP_STRVL.length();
          int L_QOTLCN = 0;
          L_RETSTR = "";
          L_STRTKN = new StringTokenizer(L_STRVL,"'");
          while(L_STRTKN.hasMoreTokens())
          {
               L_RETSTR +=  L_STRTKN.nextToken();
          }         
        }
        catch(Exception ex)
        {
	  	  setMSG(ex,"in delQuote");
        }
        //System.out.println("Original : "+L_STRVL);
        //System.out.println("Modified : "+L_RETSTR);
        return(L_RETSTR);
      }


	/** Initializing table editing before poulating/capturing data
	 */
	private void inlTBLEDIT(JTable P_tblTBLNM)
	{
		if(!P_tblTBLNM.isEditing())
			return;
		//System.out.println("Editing "+P_tblTBLNM.isEditing());
		//System.out.println("Selected "+P_tblTBLNM.hasFocus());
        if(P_tblTBLNM.isEditing())
		    P_tblTBLNM.getCellEditor().stopCellEditing();
		P_tblTBLNM.setRowSelectionInterval(0,0);
		P_tblTBLNM.setColumnSelectionInterval(0,0);
	}
	  
	/**  Returning code value for specified code description
	 */
	private String getCODCD(String LP_CODDS_KEY)		
	{
		if(!hstCODDS.containsKey(LP_CODDS_KEY))
			return "";
		return hstCODDS.get(LP_CODDS_KEY).toString();
	}
	
	/** Verifying Combo selection status
	 */
	private boolean chkEMPTY_CMB(JComboBox LP_CMBNM, String LP_MSGDS)
	{
		if(LP_CMBNM.getSelectedIndex()==0)
		{
			setMSG(LP_MSGDS,'E');
			LP_CMBNM.setFocusable(true);
			LP_CMBNM.requestFocus();
			return false;
		}
		return true;
	}


	
/** Verifying text field entry status
 */

private boolean chkTXT_RANGE(JTextField LP_TXTNM,int LP_MINVL, int LP_MAXVL, String LP_MSGDS)
{
	try
	{
		if(LP_TXTNM.getText().length()< LP_MINVL || LP_TXTNM.getText().length()>LP_MAXVL)
		{
			setMSG(LP_MSGDS,'E');
			LP_TXTNM.requestFocus();
			return false;
		}
	}
	catch(Exception e) {setMSG(e,"chkTXT_RANGE");}
	return true;
}
	

/** Verifying that Qty. entered is in multiple of Pkg.Wt.
 */
private boolean chkDOTPK(int LP_ROWNO)
{
	try
	{
		if(!getCODCD("SYSFGXXPKG"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()).equalsIgnoreCase("99"))
		{
			//float L_fltPKGWT = Float.parseFloat(nvlSTRVL(getCDTRN("SYSFGXXPKG"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"CMT_NCSVL",hstCDTRN),"0.00"));
			//float L_fltPKGNO = Float.parseFloat(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"))/L_fltPKGWT;
			//L_fltPKGNO = Float.parseFloat(setNumberFormat(L_fltPKGNO,3));

			double L_dblPKGWT = Double.parseDouble(nvlSTRVL(getCDTRN("SYSFGXXPKG"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"CMT_NCSVL",hstCDTRN),"0.00"));
			double L_dblPKGNO = Double.parseDouble(nvlSTRVL(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),"0.00"))/L_dblPKGWT;
			L_dblPKGNO = Float.parseFloat(setNumberFormat(L_dblPKGNO,3));
			 //System.out.println("L_dblPKGNO : "+L_dblPKGNO);
			int L_intPKGNO = new Float(L_dblPKGNO).intValue();
			 //System.out.println("L_intPKGNO : "+L_intPKGNO);
			if(L_intPKGNO != L_dblPKGNO)
				{setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblPKGWT,3),'E'); return false;}
			//tblDOTRN.setValueAt(setNumberFormat(L_intPKGNO,0),LP_ROWID,intTB1_DORPK);
		}
		else
			tblDOTRN.setValueAt(tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString(),LP_ROWNO,intTB1_DORPK);
	}
	catch(Exception e) {setMSG(e,"chkDOTPK");}
	return true;
}

/** Returning No.of pkgs for specified row
 *  of Grade Entry Table
 */	
private String getDOTPK(int LP_ROWNO)
{
	try
	{
		if(!getCODCD("SYSFGXXPKG"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()).equalsIgnoreCase("99"))
		{
			double L_dblPKGWT = Double.parseDouble(nvlSTRVL(getCDTRN("SYSFGXXPKG"+tblDOTRN.getValueAt(LP_ROWNO,intTB1_PKGTP).toString(),"CMT_NCSVL",hstCDTRN),"0.00"));
			double L_dblPKGNO = Double.parseDouble(nvlSTRVL(txtDORQT.getText().toString(),"0.00"))/L_dblPKGWT;
			return setNumberFormat(L_dblPKGNO,0);
		}
		else
			return tblDOTRN.getValueAt(LP_ROWNO,intTB1_DORQT).toString();
	}
	catch(Exception e) {setMSG(e,"getDOTPK");}
	return "";
}
	


/** Returning No.of pkgs for specified row
 *  of Grade Entry Table
 */	
private String getDOTPK1(double LP_PKGQT,String LP_PKGTP)
{
	try
	{
		if(!LP_PKGTP.equalsIgnoreCase("99"))
		{
			double L_dblPKGWT = Double.parseDouble(nvlSTRVL(getCDTRN("SYSFGXXPKG"+LP_PKGTP,"CMT_NCSVL",hstCDTRN),"0.00"));
			double L_dblPKGNO = LP_PKGQT/L_dblPKGWT;
			return setNumberFormat(L_dblPKGNO,0);
		}
		else
			return setNumberFormat(LP_PKGQT,0);
	}
	catch(Exception e) {setMSG(e,"getDOTPK");}
	return "";
}


	
	
/** Checking for D.O.preparation / Despatch status against etire Indent
 */
private boolean exeDSPCHK(String LP_DSPCT)
{
	boolean L_flgRETFL = false;
	Enumeration enmDOTKEYS=hstDOTRN.keys();
	while(enmDOTKEYS.hasMoreElements())
	{
		String L_strDOTCD = (String)enmDOTKEYS.nextElement();
		if(LP_DSPCT.equalsIgnoreCase("LAD_MSG"))
		{
			if(Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_LADQT"))>0 || Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_LADQT"))>0)
				{JOptionPane.showMessageDialog(this,"LA of "+getDOTRN(L_strDOTCD,"DOT_LADQT")+" MT is prepared for "+getPRMST(L_strDOTCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
		}
		else if(LP_DSPCT.equalsIgnoreCase("LAD_NOMSG"))
		{
			if(Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_LADQT"))>0 || Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_LADQT"))>0)
				L_flgRETFL=true;
		}
		else if(LP_DSPCT.equalsIgnoreCase("INV_MSG"))
		{
			if(Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_INVQT"))>0 || Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_LADQT"))>0)
				{JOptionPane.showMessageDialog(this,"LA of "+getDOTRN(L_strDOTCD,"DOT_INVQT")+" MT is prepared for "+getPRMST(L_strDOTCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
		}
		else if(LP_DSPCT.equalsIgnoreCase("INV_NOMSG"))
		{
		  //  System.out.println("Key " + L_strDOTCD);
		  //System.out.println("Inv QTy "+getDOTRN(L_strDOTCD,"DOT_INVQT"));
		  //System.out.println("LA QTy "+getDOTRN(L_strDOTCD,"DOT_LADQT"));
			if(L_strDOTCD.length() == 12)
			{
			    if(Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_INVQT"))>0 || Float.parseFloat(getDOTRN(L_strDOTCD,"DOT_LADQT"))>0)
				    L_flgRETFL=true;
			}
		}
		else
			JOptionPane.showMessageDialog(this,"Invalid Category in exeDSPMSG");
		if (L_flgRETFL)
			break;
	
	}
	return L_flgRETFL;		
}
	

		/** Picking up Product Master Details
		 * @param LP_PRDCD		Product Code 
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
		private String getPRMST(String LP_PRDCD, String LP_FLDNM)
		{
			String L_RETSTR = "";
			try
			{
			        String[] staPRMST = (String[])hstPRMST.get(LP_PRDCD);
			        if (LP_FLDNM.equals("PR_PRDDS"))
			                L_RETSTR = staPRMST[intAE_PR_PRDDS];
			        else if (LP_FLDNM.equals("PR_AVGRT"))
			                L_RETSTR = staPRMST[intAE_PR_AVGRT];
			}
			catch (Exception L_EX)
			{
				setMSG(L_EX,"getPRMST");
			}
			return L_RETSTR;
		}

/** One time data capturing for Product Master
*	into the Hash Table
*/
 private void crtPRMST()
{
	String L_strSQLQRY = "";
    try
    {
        hstPRMST.clear();
        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl <> 'X' and SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54','SX')";
		//System.out.println(L_strSQLQRY);
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
        if(L_rstRSSET == null || !L_rstRSSET.next())
        {
             setMSG("Product Records not found in CO_PRMST",'E');
              return;
        }
        while(true)
        {
                strPRDCD = getRSTVAL(L_rstRSSET,"PR_PRDCD","C");
                String[] staPRMST = new String[intPRMST_TOT];
                staPRMST[intAE_PR_PRDDS] = getRSTVAL(L_rstRSSET,"PR_PRDDS","C");
                staPRMST[intAE_PR_AVGRT] = getRSTVAL(L_rstRSSET,"PR_AVGRT","N");
                hstPRMST.put(strPRDCD,staPRMST);
                if (!L_rstRSSET.next())
                        break;
        }
        L_rstRSSET.close();
    }
    catch(Exception L_EX)
    {
           setMSG(L_EX,"crtPRMST");
    }
return;
}
/** Validating party code
 */
private boolean chkPARTY(String LP_PRTTP, String LP_PRTCD, String LP_MSGDS, String LP_CMPFL)
{
	boolean L_RETFL = false;
	try
	{
	if(LP_PRTCD.length()>0)
	{
		//String L_strDSRCHK = (LP_PRTTP.equalsIgnoreCase("C") ? " and PT_DSRCD = '"+txtDSRCD.getText()+"'" : "");
		M_strSQLQRY = "Select * from co_ptmst where pt_prttp = '"+LP_PRTTP+"' and pt_prtcd = '"+LP_PRTCD+"' and upper(PT_STSFL)<>'X' ";
		//System.out.println(M_strSQLQRY);
		ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
		if(L_rstRSSET!=null)
		{
			L_rstRSSET.close();
			L_RETFL = true;
		}
	}
	else if (LP_CMPFL.equalsIgnoreCase("N"))
		L_RETFL = true;
	}
	catch (Exception L_EX)
	{}
	return L_RETFL;
}
	/** Returning Overall total of Qty. entered in Del.Schedule Entry Table
	 *  For specified PRDCD & PKGTP combination
	 */
	private float getDOD_TOTQT(String LP_PRDCD,String LP_PKGTP)
	{
		float L_fltDOD_DORQT = 0.00f;
		if((LP_PRDCD+LP_PKGTP).length()!=12)
			return L_fltDOD_DORQT;
		try
		{
			for(int intDODCTR=0;intDODCTR<tblDODEL.getRowCount();intDODCTR++)
			{
			    if(tblDODEL.getValueAt(intDODCTR,intTB1_CHKFL).toString().equals("true"))
			    {
    				//if(Float.parseFloat(nvlSTRVL(tblDODEL.getValueAt(intDODCTR,intTB2_DORQT).toString(),"0.00"))>0 && tblDODEL.getValueAt(intDODCTR,intTB2_DSPDT).toString().length()==10)
    				if(Float.parseFloat(nvlSTRVL(tblDODEL.getValueAt(intDODCTR,intTB2_DORQT).toString(),"0.00"))>0)
    					if((LP_PRDCD+LP_PKGTP).equals(tblDODEL.getValueAt(intDODCTR,intTB2_PRDCD).toString()+tblDODEL.getValueAt(intDODCTR,intTB2_PKGTP).toString())) 
    						L_fltDOD_DORQT += Float.parseFloat(nvlSTRVL(tblDODEL.getValueAt(intDODCTR,intTB2_DORQT).toString(),"0.00"));
			    }
			}
		}
		catch(Exception e)
			{setMSG(e,"getDOD_TOTQT");}
		return L_fltDOD_DORQT;
	}
	private double getTOT_DODQT(String P_strPRDCD,String P_strPKGTP)
	{
	    double L_dblTDLQT = 0.0;
	    for(int i=0;i< tblDODEL.getRowCount();i++)
	    {
	        if(tblDODEL.getValueAt(i,intTB2_CHKFL).toString().equals("true"))
	        {
               if((tblDODEL.getValueAt(i,intTB2_PRDCD).toString().equals(P_strPRDCD))&&(tblDODEL.getValueAt(i,intTB2_PKGTP).toString().equals(P_strPKGTP)))
                    L_dblTDLQT += Double.parseDouble(tblDODEL.getValueAt(i,intTB2_DORQT).toString());        
             }
	    }
	    return L_dblTDLQT;
	}
	private String getSRLNO(int P_intROWID)
	{
	    String L_strMAXSR ="00";
	    String L_strSRLNO ="00";
	    String L_strPRDCD = tblDODEL.getValueAt(P_intROWID,intTB2_PRDCD).toString();
	    for(int i=0;i< P_intROWID;i++)
	    {
	       if(tblDODEL.getValueAt(i,intTB2_PRDCD).toString().equals(L_strPRDCD))
           {
                L_strSRLNO =nvlSTRVL(tblDODEL.getValueAt(i,intTB2_SRLNO).toString(),"00");
                if(Integer.parseInt(L_strSRLNO) >Integer.parseInt(L_strMAXSR))
                    L_strMAXSR = L_strSRLNO;
           }
	    }
	    L_strSRLNO = Integer.toString(Integer.parseInt(L_strMAXSR) +1);
	    if(L_strSRLNO.length() == 1)
	        L_strSRLNO ="0"+L_strSRLNO;
	    return L_strSRLNO;
	}
    private void rlsINDNT()
    {
		try
		{
		strWHRSTR =  "DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_MKTTP = '" +strMKTTP+"' and "
					 +"DOT_INDNO = '" +txtINDNO.getText()+"' and isnull(DOT_DORQT,0) > 0";
		flgCHK_EXIST =  chkEXIST("MR_DOTRN", strWHRSTR);
		if(flgCHK_EXIST)
		{
			// can not unauthorise Indent
		}
		else
		{
			int L_intOPTN=JOptionPane.showConfirmDialog( this,"Do you want to Unauthorize Indent..?","Unauthorize Indent",JOptionPane.YES_NO_OPTION);
			if(L_intOPTN==0)
			{
				M_strSQLQRY="update MR_INMST set IN_STSFL='0',IN_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',IN_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP='"+strMKTTP+"' and IN_INDNO='"+txtINDNO.getText()+"'";
				cl_dat.M_flgLCUPD_pbst=true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				M_strSQLQRY="update MR_INTRN set INT_INDQT=0, INT_STSFL='0',int_lusby ='"+cl_dat.M_strUSRCD_pbst+"',INT_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP='"+strMKTTP+"' and INT_INDNO='"+txtINDNO.getText()+"'";
				if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				if(cl_dat.exeDBCMT("exeSAVE"))
					setMSG("D.O. modified and Indent is Unauthorised",'N');
			}
		}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"rlsINDNT");
		}

    }
}
