/** Program Name : MR_TEIND
 *  Purpose : Capturing & Authorisation of Customer Order Detail 
 *  Author : AAP
 *  Modified by : SRD
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.undo.*;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.ResultSet;
import javax.swing.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Cursor;
/**<P>Program Description :</P><P><FONT color=purple><STRONG>Program Details :</STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="HEIGHT: 89px; WIDTH: 767px" width="75%" borderColorDark=darkslateblue borderColorLight=white>    <TR>    <TD>System Name</TD>    <TD>Marketing Management System </TD></TR>  <TR>    <TD>Program Name</TD>    <TD>  Customer Order Booking Entry</TD></TR>  <TR>    <TD>Program Desc</TD>    <TD>                  Form for Customer Order                  Entry by Regional Offices.       (To be extended to Distributors in future)&nbsp; </TD></TR>  <TR>    <TD>Basis Document</TD>    <TD>      Marketing System Enhancement Proposal by      Mr. SRD      </TD></TR>  <TR>    <TD>Executable path</TD>    <TD>f:\dada\asoft\exec\splerp2\mr_teind.class</TD></TR>  <TR>    <TD>Source path</TD>    <TD>g:\splerp2\mr_teind.java</TD></TR>  <TR>    <TD>Author </TD>    <TD>AAP </TD></TR>  <TR>    <TD>Date</TD>    <TD>10/11/2003 </TD></TR>  <TR>    <TD>Version </TD>    <TD>1.0.0</TD></TR>  <TR>    <TD><STRONG>Revision : 1 </STRONG></TD>    <TD>      </TD></TR>  <TR>    <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       By</TD>    <TD></TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;       Date</P>       </TD>    <TD>      <P align=left>&nbsp;</P>  </TD></TR>  <TR>    <TD>      <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Version</P>         </TD>    <TD></TD></TR></TABLE><FONT color=purple><STRONG>Tables Used : </STRONG></FONT><TABLE border=1 cellPadding=1 cellSpacing=1 style="WIDTH: 100%" width  ="100%" background="" borderColorDark=white borderColorLight=darkslateblue>    <TR>    <TD>      <P align=center>Table Name</P></TD>    <TD>      <P align=center>Primary Key</P></TD>    <TD>      <P align=center>Add</P></TD>    <TD>      <P align=center>Mod</P></TD>    <TD>      <P align=center>Del</P></TD>    <TD>      <P align=center>Enq</P></TD></TR>  <TR>    <TD></TD>    <TD> </TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR>  <TR>    <TD></TD>    <TD></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center>&nbsp;</P></TD>    <TD>      <P align=center><FONT face="LotusWP Icon">/</FONT></P></TD></TR></TABLE></P>*/
class mr_teind extends cl_pbase implements ChangeListener//,PopupMenuListener
{
	private Object objITMRF;	/**Object to transefer data to FoxPro*/
	private mr_hkitr omr_hkitr;/**Scroll pane for distributor series detail list*/
	
								/**Flag to indicate that ammendment is to be raised during modification	 */
	private boolean flgAMDFL;	/**Flag to remember whether user has autothorisation rights */
	private boolean flgAUTRN;	/**Flag to indicate whether remark was added previously */
	private boolean flgREGRM;	/**String for order no. */
	//private boolean flgAUTRM;	/**String for order no. */
	//private boolean flgBKGRM;
	
								/**String for order no. */
	private String strORDNO;	/**Hash table for package Types */
	private String strDSTDT;	/**Hash table for package Types */
	private String strBYRDT;	/**Consignee details */
	private String strCNSDT;
    	private String strTRPDT;
	private String strINMKTTP, strININDNO, strININDDT, strINAMDNO, strINAMDDT, strINSALTP, strINDTPCD, strINPORNO, strINPORDT, strINZONCD, strINCNSCD, strINBYRCD, strINCURCD,
		strINECHRT, strINAPTVL, strINCPTVL, strINPMTCD, strINTRNFL, strINSTSFL, strINLUSBY, strINLUPDT, strINPSHFL, strINSBSCD, strINDSTCD, strINMOTCD, strINFILRF,
		strINPMTRF, strININSRF, strINFORRF, strINTSHFL, strINREGBY, strINREGDT, strINBKGBY, strINBKGDT, strINDTPDS, strOCFNO;

	private String strDFT_DTP = "01";   // Default value for DTPCD (Ex-factory)
	private String strDFT_PMT = "01";	// Default value for PMTCD (Against Credit)
	private String strDFT_MOT = "01";	// Default value for MOTCD (By Road)

	private String strZONCD_EXP = "12";		// Export Zone
	private String strSALTP_EXP = "12";		// Export Sale Type
	private String strSALTP_DEX = "03";		// Deemed Export Sale Type
	private String strSALTP_STF = "04";		// Stock Transfer Sale Type

	private ResultSet rstRSSET1;
	
										/**Hash tables  */
	private Hashtable<String,String[]> hstCDTRN;			// Details of all codes used in program
	private Hashtable<String,String[]> hstPTMST;			// Distributor details
	private Hashtable<String,String> hstCODDS;			// Code No. from Code Description
	private Hashtable<String,String[]> hstDFTTX;			// Default Taxes
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstOPSTK;			// Product Master details
	private Hashtable<String,String> hstGRTAX_TAX;		// List of Tax Codes from Gradewise Tax Entry
	private Hashtable<String,String> hstCOTAX_TAX;		// List of Tax Codes from Common Tax Entry
	private Hashtable<String,String[]> hstGRDTL_PRD;		// List of prod.codes from Grade Entry
	private Hashtable<String,String[]> hstGRDTL;			// Product Code, Pkg.type & corresponding DO / Inv.Qty.
	private Hashtable<String,String[]> hstINDNO;			// Last indent number for individual distributor series
	
								// Vectors for storing items list of Combo Boxes
	private Vector<String> vtrMKTTP;
	private Vector<String> vtrSALTP;
	private Vector<String> vtrMOTCD;
	private Vector<String> vtrDTPCD;
	private Vector<String> vtrPMTCD;
	private Vector<String> vtrINDNO;
	private Vector<String[]> vtrHLPARR;
	
	private Object[] arrHSTKEY;		// Object array for getting hash table key in sorted order
	
								/**Button for indent authorisation only in modification	 */
	//private JButton btnAUTRN;	/**Button to print indent, only in authorisation state */
	private JButton btnPRINT;	
									/**Master level tax, applicable for all items in the order	 */
	private JRadioButton rdbAUTH_Y;	
	private JRadioButton rdbAUTH_N;	
	private JCheckBox chbTSHFL;		
	private JCheckBox chbCFMFL;		
	private JCheckBox chbPSHFL;
									/**Distributor series part in indent no.	 */
	private JComboBox cmbINDNO;		/**Market type	 */	
	private JComboBox cmbSALTP;		/**Sale Type	 */
	private JComboBox cmbMKTTP;		/**Payment Type	 */
	private JComboBox cmbPMTCD;		/**Delivery Type	 */
	private JComboBox cmbDTPCD;		/**Tansport Type	 */
	private JComboBox cmbMOTCD;		
									/**Thread to collect Distributor details from back end	 */
	private Thread thrDSTDT;		/**Thread to collect TAX details from back end	 */
	private Thread thrTAXDT;		
									/**Label for "Distributor Details"*/
	private JLabel lblDSTDT;		/**Label for "Consignee Details"*/
	private JLabel lblCNSDT;		/**Label for F.O.R. Reference no.	 */
	private JLabel lblFORNO;		/**Label for LC number	 */
	private JLabel lblCURCD;		/**Label for LC number	 */
	private JLabel lblDEFNO;		/**Label for LC number	 */
	private JLabel lblLCNO;			/**Label For Advance Payment Document No.	 */
	private JLabel lblAPDNO;		/**Label For Payment term in days	 */
	private JLabel lblPAYTM;
	private JLabel lblSTSDS;
	private JLabel lblTRPCD;
	private JLabel lblDSRDUE;
	private JLabel lblBYRDUE;
	private JLabel lblOCFNO;
									/**Order No.	 */
	private TxtLimit txtREGRM;		/**Buyer Code	 */
	//private TxtLimit txtBKGRM;	/**Buyer Code	 */
	//private TxtLimit txtAUTRM;	/**Buyer Code	 */
	private TxtNumLimit txtINDNO;	/**Buyer Code	 */
	private TxtLimit txtBYRCD;		/**Consinee Code	 */
	private TxtLimit txtCNSCD;		/**Distributor Name	 */
	private TxtLimit txtDSTCD;		/**Distributor Name	 */
	private TxtLimit txtDSRNM;		/**sHORT  Name	 */
	private TxtLimit txtSHRNM;		/**Buyer Name	 */
	private TxtLimit txtDSTNM;		/**Buyer Name	 */
	private TxtLimit txtBYRNM;		/**Consinee Name	 */
	private TxtLimit txtCNSNM;		/**Order Confirmation Number */
	private TxtNumLimit txtOCFNO;		/**Order Date	*/

	private TxtDate txtORDDT;		/**Ammendment Date	 */
	private TxtDate txtREGDT;		/**Ammendment Date	 */
	private TxtDate txtAMDDT;		/**Booking Date	 */
	private TxtDate txtBKGDT;		/**Customer Reference Date	 */
	private TxtDate txtCRFDT;
	private JTextField txtPRDCD;		
	private JTextField txtPKGTP1;		
	private JTextField txtDELTP1;	
	private JTextField txtDELDT;	
	private JTextField txtDELAD;	
	private JTextField txtPRTRF;	
									/**Accounts payment Term	 */
	private JTextField txtGRDCD;	/**Customer Payment Term	 */
	private JTextField txtREGBY;	/**Customer Payment Term	 */
	private JTextField txtEUSCD;	/**Customer Payment Term	 */
	private JTextField txtPKGTP;	// Package Type
	private JTextField txtPAYAC;	/**Customer Payment Term	 */
	private JTextField txtPAYCT;	/**F.O.R. Reference Number */
	private JTextField txtFORNO;	/**Distributor Category	 */
	private JLabel     lblDSRTP;	/**Distributor code	 */
	private JTextField txtDSRCD;	/**LC Number	 */
	private JTextField txtLCNO;		/**Advance Payment Document Number	 */
	private JTextField txtAPDNO;	/**Insurence Policy Number	 */
	private JCheckBox chbINSFL;		/**Deemed Export File Number	 */
	private JTextField txtDEFNO;	/**Booking By	 */
	private JTextField txtBKGBY;	/**Ammenment Number	 */
	private JTextField txtECHRT;	/**Ammenment Number	 */
	private JTextField txtCURCD;	/**Ammenment Number	 */
	private JTextField txtAMDNO;	/**Customer Reference Number	 */
	private JTextField txtCRFNO;
    private JTextField txtTRPCD;
    private JTextField txtTRPNM;
    
	private JTextField txtCOTAX_TAXCD;
	private JTextField txtCOTAX_TAXDS;
	private JTextField txtGRTAX_TAXCD;
	private JTextField txtGRTAX_PRDCD;
	
	
									/**Other taxes details table	 */
	private cl_JTable tblCOTAX;		/**Grade Details table	 */
	private cl_JTable tblGRDTL;		/**Grade wise tax details table	 */
	private cl_JTable tblGRTAX;		/**Status details table	 */
	private cl_JTBL tblSTATS;		
	private cl_JTable tblINDDSP;		
	private cl_JTable tblDLSCH;		
									/**Tabbed pane for order details 	 */
	private JTabbedPane tbpMAIN;
	private JPanel pnlINDDSP;
	
									/**Panel for Common Details	 */
	private JPanel pnlCODTL;		/**Panel for common tax details	 */
	private JPanel pnlCOTAX;		/**Panel for grade wise details	 */
	private JPanel pnlGRDTL;		/**Panel for grade wise tax details	 */
	private JPanel pnlGRTAX;		/**Panel for status details	 */
	private JPanel pnlSTATS;		
	private JPanel pnlDLSCH;
									
	private String strMKTTP="";					// Product Type
    private String strYREND = "31/03/2009";		// Year Ending date
    private String strYRDGT ="";				// Year digit for Doc.Numbering


									/**Panel for new LC entry	 */	
	private JPanel pnlLCDTL;		/**Components for new LC entry	 */	
	private JTextField	txtLCNEW,
						txtLCOPNDT,
						txtLCEXPDT,
						txtLCOPNVL,
						txtLCUTLVL;

	
	/**Panel for new Deemed Export file no. entry	 */	
	private JPanel pnlDEFDTL;/**Components for new Deemed Export file no. entry	 */	
	private JTextField	txtDEFNEW,
						txtDEFOPNDT,
						txtDEFEXPDT,
						txtDEFOPNVL,
						txtDEFUTLVL,
						txtDEFPRDCD,
						txtDEFOPNQT,
						txtDEFUTLQT;
									/**Components for new Advance Payment Document Details entry	 */	
	private JTextField	txtAPDCHQNO,
						txtAPDCHQDT,
						txtAPDDEPDT,
						txtAPDCHQVL,
						txtAPDUTLVL;
	
									/**Panel for new F.O.R. entry*/
	private JPanel pnlFORDTL;	
									/**Panel for new Advance Payment Document Details entry	 */	
	private JPanel pnlAPDDTL;
									/**Components for new F.O.R. entry*/	
	private JTextField	txtFORTRPCD,
						txtFORSRCCD,
						txtFORDSTCD,
						txtFORFRTVL,
						txtFORADDVL,
						txtFORVEHQT,
						txtFORVEHTP;

	private JRadioButton rdbFORRATFL_A,
						 rdbFORRATFL_R;

	private mr_rpind objRPIND;

	private int intCOTAX_ROW = 0;
	private int intGRTAX_ROW = 0;
	
											/** Table column for Grade Table */
	private int intTB1_CHKFL = 0;			
	private int intTB1_PRDCD = 1;			
	private int intTB1_PRDDS = 2;			
	private int intTB1_PKGTP = 3;			
	private int intTB1_REQQT = 4;
	//private int intTB1_DELTP = 5;
	private int intTB1_UOMCD = 5;			
	private int intTB1_INDPK = 6;			
	private int intTB1_BASRT = 7;			
	private int intTB1_EUSCD = 8;			
	private int intTB1_CDCVL = 9;			
	private int intTB1_DDCVL = 10;			
	//private int intTB1_TDCVL = 11;			
	//private int intTB1_TDCRF = 12;			
	private int intTB1_PRDRF = 11;
	

											/** Table column for Common Taxes */
	private int intTB2_CHKFL = 0;			
	private int intTB2_TAXCD = 1;			
	private int intTB2_TAXDS = 2;			
	private int intTB2_TAXVL = 3;			
	private int intTB2_AMTFL = 4;			
	private int intTB2_PRCSQ = 5;			
	
	
											/** Table column for Gradewise Taxes */
	private int intTB3_CHKFL = 0;
	private int intTB3_PRDCD = 1;			
	private int intTB3_PRDDS = 2;			
	private int intTB3_TAXCD = 3;			
	private int intTB3_TAXDS = 4;			
	private int intTB3_TAXVL = 5;
	private int intTB3_AMTFL = 6;			
	private int intTB3_PRCSQ = 7;			

	
	private int intTB4_INDNO = 1;
	private int intTB4_BKGDT = 2;
	private int intTB4_AMDNO = 3;
	private int intTB4_AMDDT = 4;
	private int intTB4_PRDDS = 5;
	private int intTB4_REQQT = 6;
	private int intTB4_INDQT = 7;
	private int intTB4_BKGBY = 8;
	private int intTB4_PRTNM = 9;
	
    	private int intTB5_CHKFL = 0;
	private int intTB5_PRDCD = 1;			
	private int intTB5_PRDDS = 2;			
	private int intTB5_DELTP = 3;
	private int intTB5_PKGTP = 4;
	private int intTB5_DELQT = 5;
	private int intTB5_DELDT = 6;												
	private int intTB5_SRLNO = 7;
    	private int intTB5_ORGDT = 8;
											/** Array elements for records picked up from Code Transactoion */
	private int intCDTRN_TOT = 10;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
    private int intAE_CMT_MODLS = 9;

	
											/** Array elements for Distributor details */
	private int intPTMST_TOT = 6;
    private int intAE_PT_PRTNM = 0;		
    private int intAE_PT_ZONCD = 1;		
    private int intAE_PT_SHRNM = 2;		
    private int intAE_PT_ADR01 = 3;		
    private int intAE_PT_ADR02 = 4;		
    private int intAE_PT_ADR03 = 5;		
	

											/** Array elements for Product Master details */
	private int intOPSTK_TOT = 4;
    private int intAE_OP_PRDCD = 0;		
    private int intAE_OP_PRDDS = 1;		
    private int intAE_OP_PKGTP = 2;		
    private int intAE_OP_AVGRT = 3;		
	
											/** Array elements for Product Master details */
	private int intPRMST_TOT = 3;
    private int intAE_PR_PRDCD = 0;		
    private int intAE_PR_PRDDS = 1;		
    private int intAE_PR_AVGRT = 2;		
	

											/** Array elements for Grade Details */
	private int intGRDTL_TOT = 4;
    private int intAE_GRD_REQQT = 0;		
    private int intAE_GRD_INDQT = 1;		
    private int intAE_GRD_DORQT = 2;		
    private int intAE_GRD_INVQT = 3;		
	
	
	private String strIN_STSFL;
	private String strOC_STSFL;
	
									/** Variables for Code Transaction Table
									 */
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	private String strPRDCD;		
	private String strPKGTP;		

	private String strPRTCD;
	private String strWHRSTR;
	private boolean flgCHK_EXIST;
	private JCheckBox chkINDDSP;
	private JCheckBox chkREDTAX;
	private boolean flgREDTAX = false;

	private Hashtable<String,String> hstPRDDT;
	/**Constructor for the form<br>
	 * Retrieves market type/delivery type/payment type and transport type  options from CO_CDTRN and populates respective combos.<br>
	 * Starts thread for retrieving Distributor details along with curren year series for INDNO
	 */
	mr_teind()
	{
		super(2);
		try
		{
			objRPIND = new mr_rpind(1);
			hstCDTRN = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstDFTTX = new Hashtable<String,String[]>();
			//hstPRDCD = new Hashtable();
			hstPRMST = new Hashtable<String,String[]>();
			hstOPSTK = new Hashtable<String,String[]>();
			hstGRTAX_TAX = new Hashtable<String,String>();
			hstCOTAX_TAX = new Hashtable<String,String>();
			hstGRDTL_PRD = new Hashtable<String,String[]>();
			hstGRDTL	 = new Hashtable<String,String[]>();
			hstINDNO	 = new Hashtable<String,String[]>();

			vtrSALTP = new Vector<String>();
			vtrMKTTP = new Vector<String>();
			vtrMOTCD = new Vector<String>();
			vtrDTPCD = new Vector<String>();
			vtrPMTCD = new Vector<String>();
			vtrINDNO = new Vector<String>();
			vtrHLPARR = new Vector<String[]>();

			chkINDDSP = new JCheckBox("Unauthorised Cust.Orders");
			chkREDTAX = new JCheckBox("Redefine Tax");
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "0" : "9";
			//strYRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4);
			hstCDTRN.clear();
            hstCODDS.clear();
			crtCDTRN("'MSTCOXXCUR','STSMRXXIND','SYSMRXXDTP', 'SYSMRXXPMT','SYSMR01MOT','SYSMR00EUS','SYSCOXXTAX','SYSCOXXDST','SYSMRXXPOD','SYSFGXXPKG','SYSCOXXAMT','SYSMR00SAL'","",hstCDTRN);
			crtPTMST();
			crtPRMST();
			crtOPSTK();
			//crtHLPVTR();

			setMatrix(20,12);
			add(new JLabel("Sale Type"),1,1,1,2,this,'L');
			add(cmbSALTP=new JComboBox(),1,3,1,1,this,'L');
			add(new JLabel("Market Type"),1,4,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),1,5,1,1,this,'L');
			add(lblSTSDS=new JLabel(""),1,5,1,2,this,'L');
			add(rdbAUTH_Y=new JRadioButton("Authorise Order"),5,7,1,2,this,'L');
        	add(rdbAUTH_N=new JRadioButton("Donot Authorise"),5,9,1,2,this,'L');
			add(btnPRINT=new JButton("Print"),1,11,1,1.5,this,'L');
			ButtonGroup btg=new ButtonGroup();btg.add(rdbAUTH_Y);btg.add(rdbAUTH_N);
			rdbAUTH_Y.setVisible(false);
			rdbAUTH_N.setVisible(false);
			//btnAUTRN.setVisible(false);

			
			//ADDING BASE DETAILS OF ORDER		
			//add(new JLabel("Dst.Type."),1,5,1,1,this,'L');
			//add(lblDSRTP=new TxtLimit(1),1,6,1,0.5,this,'L');
			add(lblOCFNO=new JLabel("Ord.Conf.No."),1,5,1,1,this,'L');
			add(txtOCFNO=new TxtNumLimit(8.0),1,6,1,1,this,'L');
			lblOCFNO.setVisible(false);
			txtOCFNO.setVisible(false);	
			txtOCFNO.setEnabled(false);
			add(new JLabel("C/Ord.No."),1,7,1,1,this,'L');
			add(cmbINDNO=new JComboBox(),1,8,1,2,this,'L');
			cmbINDNO.setMaximumRowCount(4);
			add(txtINDNO=new TxtNumLimit(5.0),1,10,1,0.75,this,'L');
			add(lblDSTDT=new JLabel("Distributor"),2,1,1,2,this,'L');
			add(lblDSRTP=new JLabel(""),2,2,1,0.25,this,'R');
			add(txtDSRCD=new TxtLimit(5),2,3,1,0.75,this,'L');
			add(txtDSRNM=new TxtLimit(45),2,4,1,6,this,'L');
			add(lblDSRDUE=new JLabel(" ",JLabel.RIGHT),2,11,1,1.5,this,'L');
			add(txtSHRNM=new TxtLimit(20),2,10,1,1,this,'L');
			add(new JLabel("Buyer / Overdue "),3,1,1,2,this,'L');
			add(txtBYRCD=new TxtLimit(5),3,3,1,0.75,this,'L');
			add(txtBYRNM=new TxtLimit(45),3,4,1,7,this,'L');
			add(lblBYRDUE=new JLabel(" ",JLabel.RIGHT),3,11,1,1.5,this,'L');
			add(lblCNSDT=new JLabel("Consignee"),4,1,1,2,this,'L');
			add(chkINDDSP,6,10,1,2,this,'L');
          
			add(txtCNSCD=new TxtLimit(5),4,3,1,0.75,this,'L');
			add(txtCNSNM=new TxtLimit(45),4,4,1,8,this,'L');
			add(new JLabel("Destination"),5,1,1,2,this,'L');
			add(txtDSTCD=new TxtLimit(3),5,3,1,0.75,this,'L');
			add(txtDSTNM=new TxtLimit(45),5,4,1,3,this,'L');
			setMatrix(20,6);

			//CREATING COMMON DETAILS AS FIRST TABBED PANE		
			add(new JLabel("Order Date"),1,1,1,1,pnlCODTL=new JPanel(null),'L');
			add(txtORDDT=new TxtDate(),1,2,1,1,pnlCODTL,'L');
			add(new JLabel("Amd. No. / Date"),1,3,1,1,pnlCODTL,'L');
			add(txtAMDNO=new TxtLimit(2),1,4,1,0.3,pnlCODTL,'L');
			add(txtAMDDT=new TxtDate(),1,4,1,0.7,pnlCODTL,'R');
			add(new JLabel("Booking By / Date"),1,5,1,1,pnlCODTL,'L');
			add(txtBKGBY=new TxtLimit(3),1,6,1,0.3,pnlCODTL,'L');
			add(txtBKGDT=new TxtDate(),1,6,1,0.7,pnlCODTL,'R');
			add(new JLabel("Cust. Ref. No"),2,1,1,1,pnlCODTL,'L');
			add(txtCRFNO=new TxtLimit(30),2,2,1,1,pnlCODTL,'L');
			add(new JLabel("Cust. Ref. Date"),2,3,1,1,pnlCODTL,'L');
			add(txtCRFDT=new TxtDate(),2,4,1,1,pnlCODTL,'L');
			add(new JLabel("Registered By / Date"),2,5,1,1,pnlCODTL,'L');
			add(txtREGBY=new TxtLimit(3),2,6,1,0.3,pnlCODTL,'L');
			add(txtREGDT=new TxtDate(),2,6,1,0.7,pnlCODTL,'R');
			
			add(new JLabel("Delivery Type"),3,1,1,1,pnlCODTL,'L');
			add(cmbDTPCD=new JComboBox(),3,2,1,1,pnlCODTL,'L');
			add(lblFORNO=new JLabel("F.O.R. Ref."),3,3,1,1,pnlCODTL,'L');
			add(txtFORNO=new TxtLimit(15),3,4,1,1,pnlCODTL,'L');
			add(lblPAYTM=new JLabel("Cr. days Cust/Acct "),3,5,1,1,pnlCODTL,'L');
			add(txtPAYCT=new TxtNumLimit(3.0),3,6,1,0.5,pnlCODTL,'L');
			add(txtPAYAC=new TxtNumLimit(3.0),3,6,1,0.5,pnlCODTL,'R');
			add(new JLabel("Payment Type"),4,1,1,1,pnlCODTL,'L');
			add(cmbPMTCD=new JComboBox(),4,2,1,1,pnlCODTL,'L');
			add(lblAPDNO=new JLabel("Adv. Pmt. Doc. No"),4,3,1,1,pnlCODTL,'L');
			add(txtAPDNO=new TxtLimit(25),4,4,1,1,pnlCODTL,'L');
			add(lblLCNO=new JLabel("LC No"),4,3,1,1,pnlCODTL,'L');
			add(txtLCNO=new JTextField(),4,4,1,1,pnlCODTL,'L');
			lblLCNO.setVisible(false);txtLCNO.setVisible(false);
			add(new JLabel("Trans-shipment"),4,5,1,1,pnlCODTL,'L');
			add(chbTSHFL=new JCheckBox("  Allowed"),4,6,1,1,pnlCODTL,'L');

			add(new JLabel("C-FORM"),6,5,1,1,pnlCODTL,'L');
			add(chbCFMFL=new JCheckBox("  Required"), 6,6,1,1,pnlCODTL,'L');

			
			add(new JLabel("Mode Of Transport"),5,1,1,1,pnlCODTL,'L');
			add(cmbMOTCD=new JComboBox(),5,2,1,1,pnlCODTL,'L');
			add(new JLabel("Insu. Policy No"),5,3,1,1,pnlCODTL,'L');
			add(chbINSFL=new JCheckBox("Available"),5,4,1,1,pnlCODTL,'L');
			
			add(lblDEFNO=new JLabel("D/Exp. File No"),6,5,1,1,pnlCODTL,'L');
			add(txtDEFNO=new TxtLimit(30),6,6,1,1,pnlCODTL,'L');
			add(lblCURCD=new JLabel("Currency / Exchange Rate "),6,1,1,1,pnlCODTL,'L');
			add(txtCURCD=new TxtLimit(2),6,2,1,0.5,pnlCODTL,'L');
			add(txtECHRT=new TxtNumLimit(6.2),6,2,1,0.5,pnlCODTL,'R');
			add(new JLabel("Part-shipment"),5,5,1,1,pnlCODTL,'L');
			add(chbPSHFL=new JCheckBox("  Allowed"),5,6,1,1,pnlCODTL,'L');
			add(new JLabel("Reg. Remark"),7,1,1,1,pnlCODTL,'L');
			add(txtREGRM=new TxtLimit(200),7,2,1,5,pnlCODTL,'L');

			add(lblTRPCD = new JLabel("Transporter"),8,1,1,1,pnlCODTL,'L');
			add(txtTRPCD=new TxtLimit(5),8,2,1,1,pnlCODTL,'L');
			add(txtTRPNM=new TxtLimit(45),8,3,1,4,pnlCODTL,'L');
			add(new JLabel("Del. Address"),9,1,1,1,pnlCODTL,'L');
			add(txtDELAD=new TxtLimit(200),9,2,1,4,pnlCODTL,'L');
			add(new JLabel("SPL Ref."),10,1,1,1,pnlCODTL,'L');
			add(txtPRTRF=new TxtLimit(15),10,2,1,1,pnlCODTL,'L');

			//add(new JLabel("Bkg. Remark"),8,1,1,1,pnlCODTL,'L');
			//add(txtBKGRM=new TxtLimit(200),8,2,1,5,pnlCODTL,'L');
			//add(new JLabel("Aut. Remark"),9,1,1,1,pnlCODTL,'L');
			//add(txtAUTRM=new TxtLimit(200),9,2,1,5,pnlCODTL,'L');
			txtDEFNO.setVisible(false);txtECHRT.setVisible(false);txtCURCD.setVisible(false);
			lblDEFNO.setVisible(false);lblCURCD.setVisible(false);

			//lblTRPCD.setVisible(false);txtTRPCD.setVisible(false);txtTRPNM.setVisible(false);
			
			//CREATING COMMON TAX DETIALS PANEL AS SECOND TABBED PANE		
			tblCOTAX=crtTBLPNL1(pnlCOTAX=new JPanel(null),new String[]{"FL","Code","Description","Value","Amt./Percent","Proc.Seq."},20,1,1,6,6,new int[]{20,100,200,100,150,100},new int[]{0});

			add(chkREDTAX,9,6,1,3,pnlCOTAX,'L');
			tblCOTAX.setCellEditor(intTB2_TAXCD,txtCOTAX_TAXCD=new TxtLimit(3));
			tblCOTAX.setCellEditor(intTB2_TAXDS,txtCOTAX_TAXDS=new JTextField());
			tblCOTAX.setCellEditor(intTB2_TAXVL,new TxtNumLimit(7.2));
			tblCOTAX.setCellEditor(intTB2_PRCSQ,new TxtLimit(2));
			tblCOTAX.setCellEditor(intTB2_AMTFL,new TxtLimit(1));
			tblCOTAX.setInputVerifier(new TBLINPVF());
			txtCOTAX_TAXCD.addFocusListener(this);
			txtCOTAX_TAXCD.addKeyListener(this);
			txtCOTAX_TAXDS.addFocusListener(this);
			txtCOTAX_TAXDS.addKeyListener(this);

			//CREATING GRADE DETAILS PANEL AS THIRD TABBED PANE					
			//tblGRDTL=crtTBLPNL1(pnlGRDTL=new JPanel(null),new String[]{"FL","Prd.Code","Description","Pkg.Type","Req.Qty.","UOM","Pkgs","Bas.Rate","Sector","C.Disc.","D.Disc.","T.Disc.","T.Code"},20,1,1,8,5.9,new int[]{20,80,100,60,75,40,40,75,75,45,45,40,50},new int[]{0});
			tblGRDTL=crtTBLPNL1(pnlGRDTL=new JPanel(null),new String[]{"FL","Prd.Code","Description","Pkg.Type","Req.Qty.","UOM","Pkgs","Bas.Rate","Sector","C.Disc.","D.Disc.","Prd. Ref"},20,1,1,8,5.9,new int[]{20,80,100,60,75,40,40,75,75,45,45,45},new int[]{0});
			tblGRDTL.setCellEditor(intTB1_PRDCD,txtGRDCD=new TxtNumLimit(10.0));
			tblGRDTL.setCellEditor(intTB1_UOMCD,new TxtLimit(2));
			tblGRDTL.setCellEditor(intTB1_BASRT,new TxtNumLimit(9.2));
			tblGRDTL.setCellEditor(intTB1_EUSCD,txtEUSCD=new JTextField());
			tblGRDTL.setCellEditor(intTB1_PKGTP,txtPKGTP=new JTextField());
			tblGRDTL.setCellEditor(intTB1_PRDRF,new TxtLimit(25));
		//	tblDLSCH.setCellEditor(intTB5_PKGTP,txtPKGTP1=new JTextField());
			((JTextField) tblGRDTL.cmpEDITR[intTB1_BASRT]).addFocusListener(this);
			txtPKGTP.addFocusListener(this);
			txtPKGTP.addKeyListener(this);
			txtEUSCD.addFocusListener(this);
			txtEUSCD.addKeyListener(this);
			txtGRDCD.addKeyListener(this);

			tblGRDTL.setInputVerifier(new TBLINPVF());
			tblGRDTL.cmpEDITR[intTB1_PRDCD].addFocusListener(this);
			
			//CREATING GRADE TAX DETAILS PANEL AS FOURTH TABBED PANE		
			//                  LP_TBLPNL,                             LP_COLHD,                                                             LP_ROWCNT,P_ROW,P_COL,P_WETX,P_WETY,LP_ARRGSZ,LP_CHKCOL[])
			tblGRTAX=crtTBLPNL1(pnlGRTAX=new JPanel(null),new String[]{"FL","Prd.Cd.","Grade","Tax Cd.","Description","Value","Amt/Prct","Proc.Seq."},20,1,1,5,5,new int[]{20,100,100,60,200,100,100,90},new int[]{0});
			tblGRTAX.setCellEditor(intTB3_TAXCD,txtGRTAX_TAXCD=new TxtLimit(3));
			tblGRTAX.setCellEditor(intTB3_PRDCD,txtGRTAX_PRDCD=new TxtNumLimit(10.0));
			tblGRTAX.setCellEditor(intTB3_TAXVL,new TxtNumLimit(6.2));
			tblGRTAX.setCellEditor(intTB3_PRCSQ,new TxtLimit(2));
			tblGRTAX.setCellEditor(intTB3_AMTFL,new TxtLimit(1));
			tblGRTAX.setInputVerifier(new TBLINPVF());
			txtGRTAX_TAXCD.addFocusListener(this);
			txtGRTAX_TAXCD.addKeyListener(this);
			txtGRTAX_PRDCD.addFocusListener(this);
			txtGRTAX_PRDCD.addKeyListener(this);

			//CREATING STATUS  DETAILS PANEL AS FIFTH TABBED PANE		
			tblSTATS=crtTBLPNL(pnlSTATS=new JPanel(null),new String[]{"FL","Grade","Req. Qty.","Bkd. Qty.","Inv. Qty."},20,1,1,8,5.9,new int[]{20,200,100,100,100,100},new int[]{0});


			//CREATING Delivery schedule PANEL AS FIFTH TABBED PANE		
			tblDLSCH=crtTBLPNL1(pnlDLSCH=new JPanel(null),new String[]{"FL","Product","Grade","Del Type","Pkg Type","Del. Qty.","Del. Date","Srl No","Old DT"},20,1,1,8,5.9,new int[]{20,100,100,100,100,100,100,100,5},new int[]{0});
			tblDLSCH.setCellEditor(intTB5_PRDCD,txtPRDCD=new TxtNumLimit(10.0));
			tblDLSCH.setCellEditor(intTB5_PKGTP,txtPKGTP1=new JTextField());
			tblDLSCH.setCellEditor(intTB5_DELTP,txtDELTP1=new TxtLimit(1));
			tblDLSCH.setCellEditor(intTB5_DELDT,txtDELDT=new TxtDate());
			tblDLSCH.setInputVerifier(new TBLINPVF());
			txtPRDCD.addKeyListener(this);txtPRDCD.addFocusListener(this);
			txtPKGTP1.addKeyListener(this);txtPKGTP1.addFocusListener(this);
			txtDELTP1.addKeyListener(this);txtDELTP1.addFocusListener(this);
			txtDELDT.addKeyListener(this);txtDELDT.addFocusListener(this);
        //((JTextField) tblDLSCH.cmpEDITR[intTB5_PRDCD]).addKeyListener(this);
		//	((JTextField) tblDLSCH.cmpEDITR[intTB5_PKGTP]).addKeyListener(this);
		
			//ADDING ALL PANELS IN TABBED PANE			
			tbpMAIN=new JTabbedPane();
			tbpMAIN.add("Common Details",pnlCODTL);
			tbpMAIN.add("Grade Details",pnlGRDTL);
			tbpMAIN.add("Common Tax",pnlCOTAX);
			tbpMAIN.add("Gradewise Tax",pnlGRTAX);
			tbpMAIN.add("Status",pnlSTATS);
			tbpMAIN.add("Schedule",pnlDLSCH);
			tbpMAIN.addChangeListener(this);

			//ADDING TABBED PANE TO SCREEN			
			add(tbpMAIN,6,1,12,6.1,this,'L');

			

			//RETRIEVING MARKET TYPE/DELIVERY TYPE/PAYMENT TYPE AND TRANSPORT TYPE  OPTIONS FROM CDTRN AND PUTTING IT IN RESPECTIVE COMBOS.

			setCDLST(vtrMOTCD,"SYSMR01MOT","CMT_CODDS");
			setCDLST(vtrDTPCD,"SYSMRXXDTP","CMT_CODDS");
			setCDLST(vtrPMTCD,"SYSMRXXPMT","CMT_CODDS");

			setCMBVL(cmbMOTCD,vtrMOTCD);
			setCMBVL(cmbDTPCD,vtrDTPCD);
			setCMBVL(cmbPMTCD,vtrPMTCD);

			
			INPVF oINPVF =new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					if(M_vtrSCCOMP.elementAt(i) instanceof JTextField || M_vtrSCCOMP.elementAt(i) instanceof JComboBox || M_vtrSCCOMP.elementAt(i) instanceof JCheckBox)
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(oINPVF);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
					{
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
					}
				}
				else
					((JLabel)M_vtrSCCOMP.elementAt(i)).setForeground(new Color(95,95,95));
			}
			
//			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			chkINDDSP.setSelected(false);
			chkREDTAX.setSelected(false);
			setMatrix(20,6);
		}catch (Exception e)
		{setMSG(e,"Child.Constructor");}
	}


	
/** Generating string for Insertion Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try 
{
	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
	if (LP_FLDTP.equals("C"))
		 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
 	else if (LP_FLDTP.equals("N"))
         return   nvlSTRVL(LP_FLDVL,"0") + ",";
 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else if (LP_FLDTP.equals("T"))
		 return   (LP_FLDVL.trim().length()>=5) ? ("'"+LP_FLDVL.trim()+"',") : "null,";
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
private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
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
		 return   (LP_FLDNM + " = "+(LP_FLDVL.trim().length()>=5 ? ("'"+LP_FLDVL.trim()+"',") : "null,"));
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
	{setMSG(L_EX,"getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);}
return " ";
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
          return(L_RETSTR);
        }
		
	
	
	
	
/** Populating values in Combo Box from Vector
 */
private void setCMBVL(JComboBox LP_CMBNM, Vector<String> LP_VTRNM)
{
		LP_CMBNM.removeAllItems();
		for(int i=LP_VTRNM.size()-1 ; i>=0; i--)
        {
                LP_CMBNM.addItem(LP_VTRNM.get(i).toString());
        }
}
	

/**
 */
private Vector<String[]> getHLPVTR(String LP_HLPTP, String LP_TXTVL)
{
	vtrHLPARR.clear();
	if (LP_HLPTP.equals("PRDCD"))
	{
		setHST_ARR(hstPRMST);
		String L_strPRDCT = getCDTRN("MSTCOXXMKT"+strMKTTP,"CMT_CCSVL",hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
		{
			//if(!M_strSBSCD.equals("511600"))
			if(!((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")))
				//if(!strMKTTP.equals("02"))
				if(L_strPRDCT.indexOf(arrHSTKEY[i].toString().substring(0,2))<0)
					continue;
			if(LP_TXTVL.length()==0)
				{vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString(),getPRMST(arrHSTKEY[i].toString(),"PR_PRDDS")}); continue;}
			if(arrHSTKEY[i].toString().substring(0,LP_TXTVL.length()).equals(LP_TXTVL))
				{vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString(),getPRMST(arrHSTKEY[i].toString(),"PR_PRDDS")}); continue;}
		}
	}
	else if (LP_HLPTP.equals("DSTCD"))
	{
		setHST_ARR(hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
			if(arrHSTKEY[i].toString().substring(0,10).equals("SYSCOXXDST"))
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
	}
	else if (LP_HLPTP.equals("DTPCD"))
	{
		setHST_ARR(hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
			if(arrHSTKEY[i].toString().substring(0,10).equals("SYSMRXXDTP"))
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
	}
	else if (LP_HLPTP.equals("TAXCD"))
	{
		setHST_ARR(hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
			if(arrHSTKEY[i].toString().substring(0,10).equals("SYSCOXXTAX"))
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
	}
	else if (LP_HLPTP.equals("PKGTP"))
	{
		setHST_ARR(hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
			if(arrHSTKEY[i].toString().substring(0,10).equals("SYSFGXXPKG"))
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
	}
	else if (LP_HLPTP.equals("CURCD"))
	{
		setHST_ARR(hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
			if(arrHSTKEY[i].toString().substring(0,10).equals("MSTCOXXCUR"))
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
	}
	else if (LP_HLPTP.equals("EUSCD"))
	{
		setHST_ARR(hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
			if(arrHSTKEY[i].toString().substring(0,10).equals("SYSMR00EUS"))
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN)});
	}
	return vtrHLPARR;
}

/**
 * Indent series is set according to market type and distributor type combination
 */
private void setCMBINDNO()
{
	try
	{
		strMKTTP ="";
		if(cmbMKTTP.getItemCount()>0 && cmbMKTTP.getSelectedIndex()>0)
			strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
		if(strMKTTP.equals(""))
			return;
		System.out.println("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND");
		setCDLST1(vtrINDNO,"D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND","CMT_CODCD");
		setCMBVL(cmbINDNO,vtrINDNO);
		String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls + cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";				
		if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
		{
			crtCDTRN("'SYSCOXXTAX'"," and upper(CMT_CHP02) <> 'X' and  CMT_CGMTP + CMT_CGSTP + CMT_CODCD in (select cd1_cgmtp + cd1_cgstp + cd1_codcd from co_cdtr1 where cd1_cgmtp + cd1_cgstp='SYSCOXXTAX' and cd1_subcd = 'MR"+strMKTTP+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' and cd1_taxtp='01')",hstDFTTX);
			crtCDTRN("'SYSCOXXTAX'"," and upper(CMT_CHP02) = 'X' and  CMT_CGMTP + CMT_CODCD in (select cd1_cgmtp + cd1_codcd from co_cdtr1 where cd1_cgmtp + cd1_cgstp='SYSCOXXDES' and cd1_subcd = 'MR"+strMKTTP+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' and cd1_taxtp='02')",hstDFTTX);
		}
		crtCDTRN("'D"+cl_dat.M_strCMPCD_pbst+"MR01IND','D"+cl_dat.M_strCMPCD_pbst+"MR02IND','D"+cl_dat.M_strCMPCD_pbst+"MR03IND','D"+cl_dat.M_strCMPCD_pbst+"MR04IND','D"+cl_dat.M_strCMPCD_pbst+"MR05IND','D"+cl_dat.M_strCMPCD_pbst+"MR07IND','D"+cl_dat.M_strCMPCD_pbst+"MR12IND'",L_strDSTCHK,hstINDNO);
	}
	catch(Exception e){setMSG(e,"setHST_ARR");}
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



/**To disable Combo Selection change if combo is not having focus if source is not cmbOPTN<br>
 * if source is cmbOPTN, set component states as per selection of cmbOPTN, nevigate focus to cmbMKTTP */	
	public void itemStateChanged(ItemEvent L_IE)
	{
		super.itemStateChanged(L_IE);
		try
		{
			M_objSOURC=L_IE.getSource();
			if(L_IE.getStateChange()!=1)
				return;
				
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
/*				clrCOMP();
				setCDLST(vtrSALTP,"SYSMR00SAL","CMT_CODDS");
				setCMBVL(cmbSALTP,vtrSALTP);
				cmbSALTP.setSelectedItem(getCDTRN("SYSMR00SAL"+M_strSBSCD.substring(2,4),"CMT_CODDS",hstCDTRN));
				if(cmbMKTTP.getItemCount()==0)
					setCMBMKTTP();
				inlCDTRN();
				String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls||cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";
				if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
					crtCDTRN("'MSTCOXXMKT'"," and  CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and  CMT_CHP02 like '%"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"%'",hstCDTRN);
				crtCDTRN("'D"+cl_dat.M_strCMPCD_pbst+"MR01IND','D"+cl_dat.M_strCMPCD_pbst+"MR02IND','D"+cl_dat.M_strCMPCD_pbst+"MR03IND','D"+cl_dat.M_strCMPCD_pbst+"MR04IND','D"+cl_dat.M_strCMPCD_pbst+"MR05IND','D"+cl_dat.M_strCMPCD_pbst+"MR12IND'",L_strDSTCHK+" and  SUBSTRING(CMT_CODCD,4,1)='"+strYRDGT+"' and CMT_CHP01='"+M_strSBSCD.substring(0,2)+"'",hstCDTRN);

				String L_strOPSEL=L_IE.getItem().toString();
				cmbMKTTP.requestFocus();	
				if(L_strOPSEL.equals(cl_dat.M_OPADD_pbst))
				{
					btnPRINT.setEnabled(false);
					txtORDDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREGBY.setText(cl_dat.M_strUSRCD_pbst);
					txtBKGDT.setText("");
					txtBKGBY.setText("");
					cmbMKTTP.requestFocus();
				}
				else if(L_strOPSEL.equals(cl_dat.M_OPDEL_pbst))
				{//Set default date and user data
					btnPRINT.setVisible(false);
					cmbMKTTP.setEnabled(true);
					txtINDNO.setEnabled(true);
					cmbINDNO.setEnabled(true);
				}
				else if(L_strOPSEL.equals(cl_dat.M_OPMOD_pbst) || L_strOPSEL.equals(cl_dat.M_OPAUT_pbst))
				{
					btnPRINT.setEnabled(false);
				}
				setDFTSTS();
				cmbMKTTP.setEnabled(true);cmbMKTTP.setVisible(true);
				cmbMKTTP.requestFocus();
				*/
				String L_strOPSEL=L_IE.getItem().toString();
				if(L_strOPSEL.equals(cl_dat.M_OPADD_pbst))
				{
					btnPRINT.setEnabled(false);
					txtORDDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREGBY.setText(cl_dat.M_strUSRCD_pbst);
					txtBKGDT.setText("");
					txtBKGBY.setText("");
					//cmbMKTTP.requestFocus();
				}
				else if(L_strOPSEL.equals(cl_dat.M_OPENQ_pbst))
					{btnPRINT.setVisible(true); btnPRINT.setEnabled(true);}
				else if(L_strOPSEL.equals(cl_dat.M_OPDEL_pbst))
				{//Set default date and user data
					btnPRINT.setVisible(false);
					cmbSALTP.setEnabled(true);
					cmbMKTTP.setEnabled(true);
					txtINDNO.setEnabled(true);
					cmbINDNO.setEnabled(true);
				}
				else if(L_strOPSEL.equals(cl_dat.M_OPMOD_pbst) || L_strOPSEL.equals(cl_dat.M_OPAUT_pbst))
				{
					btnPRINT.setEnabled(false);
				}
			}

			else if(M_objSOURC==cmbMKTTP)
			{
  					////setCMBINDNO();
					////setDFTSTS();
					////cmbINDNO.requestFocus();
			}
			
		else if(M_objSOURC==cmbINDNO) 
		{
				if(cmbINDNO.getSelectedIndex()>0)
				{
					String L_strINDTXT = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CCSVL", hstINDNO);
					txtINDNO.setText(String.valueOf(Integer.parseInt(L_strINDTXT) + (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)==true ? 1 :0)));
					if(txtINDNO.getText().length()<4)
					for(int i=0;i<=4;i++)
					{
						txtINDNO.setText("0"+txtINDNO.getText());
						if(txtINDNO.getText().length()==4)
							break;
					}
									
					txtAMDNO.setText("00");
					txtAMDDT.setText("");
					String L_strCHP02 = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN);
					String L_strMODLS = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_MODLS", hstCDTRN);
					//System.out.println(L_strMODLS+"/"+L_strCHP02);
					lblDSRTP.setText(L_strMODLS);
					txtDSRCD.setText(L_strCHP02);
					//System.out.println("*"+lblDSRTP.getText()+txtDSRCD.getText()+"*");
					txtDSRNM.setText(getPTMST(lblDSRTP.getText()+txtDSRCD.getText(),"PT_PRTNM"));
					txtSHRNM.setText(getPTMST(lblDSRTP.getText()+txtDSRCD.getText(),"PT_SHRNM"));
					txtINDNO.requestFocus();
				}
				
				if(!cmbINDNO.isPopupVisible())
				{
					txtINDNO.setVisible(true);
					lblCNSDT.setVisible(true);
					lblDSTDT.setVisible(true);
					txtINDNO.requestFocus();
				}
			}
			else if(M_objSOURC==cmbPMTCD)
			{
				setDFTSTS1("ADV","0");
				setDFTSTS1("L_C","0");
				if(getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()).equals("03"))//|Credit is selected
					setDFTSTS1("ADV","1");
				else if(getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()).equals("02")) //Against LC is selected
					setDFTSTS1("L_C","1");
			}
			else if(M_objSOURC==cmbDTPCD)
			{
				setDFTSTS1("FOR","0");
				chbINSFL.setSelected(false);
				if(getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()).equals("01")) // Ex-factory
				{   
				    lblTRPCD.setVisible(true);txtTRPCD.setVisible(true);txtTRPNM.setVisible(true);
				}
				else
				{
				    lblTRPCD.setVisible(false);txtTRPCD.setVisible(false);txtTRPNM.setVisible(false);
				}
				if(getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()).equals("02")) // FOR
					setDFTSTS1("FOR","1");
				if(getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()).equals("04")) // CIF
					chbINSFL.setSelected(true);
			}
			
		}catch(Exception e){setMSG(e,"Child.itemStateChanged");}
	}

	
/** Setting initial/default values in Combo Box (taking code as a parameter)
 */
	private void setCMBDFT(JComboBox LP_CMBNM, String LP_CODCT, String LP_CMBVL)
	{
		try
		{
			int L_intCMBITMS = LP_CMBNM.getItemCount();
			String L_strCMBDS = getCDTRN(LP_CODCT+LP_CMBVL,"CMT_CODDS", hstCDTRN);
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
	

/** Setting initial/default values in Combo Box (taking item description as a parameter)
 */
	private void setCMBDFT_1(JComboBox LP_CMBNM, String LP_CMBVL)
	{
		try
		{
			int L_intCMBITMS = LP_CMBNM.getItemCount();
			//String L_strCMBDS = getCDTRN(LP_CODCT+LP_CMBVL,"CMT_CODDS", hstCDTRN);
			for(int i=0;i<LP_CMBNM.getItemCount();i++)
			{
				if((LP_CMBNM.getItemAt(i).toString()).equalsIgnoreCase(LP_CMBVL))
				{
					LP_CMBNM.setSelectedItem(LP_CMBNM.getItemAt(i).toString());
					LP_CMBNM.setSelectedIndex(i);
					break;
				}
			}
		}
	catch(Exception e){setMSG(e,"setCMBDFT_1");}
	}
	
	
	
	
	/** Displaying default tax figures in Master Tax entry table
	 */
	private void setDFTTAX1()
	{
	try
		{//RETRIEVING TAX CATAGORY DETAILS
		int i = 0;	
		Enumeration enmCODKEYS=hstDFTTX.keys();
		while(enmCODKEYS.hasMoreElements())
		{
			String L_strCODCD = (String)enmCODKEYS.nextElement();
			tblCOTAX.setValueAt(new Boolean(true),i,intTB2_CHKFL);
			tblCOTAX.setValueAt(getCDTRN(L_strCODCD,"CMT_CODCD", hstDFTTX),i,intTB2_TAXCD);
			tblCOTAX.setValueAt(getCDTRN(L_strCODCD,"CMT_CODDS", hstDFTTX),i,intTB2_TAXDS);
			tblCOTAX.setValueAt(getCDTRN(L_strCODCD,"CMT_NCSVL", hstDFTTX),i,intTB2_TAXVL);
			tblCOTAX.setValueAt(getCDTRN(L_strCODCD,"CMT_CHP02", hstDFTTX),i,intTB2_AMTFL);
			tblCOTAX.setValueAt(getCDTRN(L_strCODCD,"CMT_CCSVL", hstDFTTX),i,intTB2_PRCSQ);
			i += 1;
		}

		}
		catch(Exception e)
		{
			setMSG(e,"setDFTTAX1 "+M_strSQLQRY );
		}
	}
	

	
	

//CHECKING WHERTHER USER HAS PERMISSION TO AUTHORISE ORDER
private void setAUTFL()
{
try
	{
		flgAUTRN = false;
		if(cl_dat.M_strUSRCD_pbst.equals("SYS"))
			flgAUTRN=true;
		if(M_staUSRRT[0][M_intAE_AUTFL].equals("Y"))		
			flgAUTRN=true;
		//ResultSet L_rstRSSET=cl_dat.exeSQLQRY1("Select UST_USRTP from  sa_ustrn where ust_usrcd='"+cl_dat.M_strUSRCD_pbst+"' and ust_USRTP in ('MR102','MR112','MR191','MR104','MR204','MR404')");
		//if(L_rstRSSET!=null && L_rstRSSET.next())
		//	{flgAUTRN=true;	L_rstRSSET.close();}
	}
	catch(Exception e)
	{
		setMSG(e,"In setAUTFL : "+M_strSQLQRY );
	}
}

	
	
	
	/**Prevents user from entering tax details before entering grade wise details	  <br>Displays list of applicable taxes in Grade - wise tax tab. i.e. taxes having zero value in common tax table*/
	public void stateChanged(ChangeEvent L_CE)
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
			return;
		if(tblCOTAX.isEditing())
			tblCOTAX.getCellEditor().stopCellEditing();
		if(tblGRDTL.isEditing())
			tblGRDTL.getCellEditor().stopCellEditing();
		if(tblGRTAX.isEditing())
			tblGRTAX.getCellEditor().stopCellEditing();
		if(tbpMAIN.getSelectedIndex()==2)
		{
			if(tblGRDTL.getValueAt(0,intTB1_PRDCD).toString().length()==0)
				{setMSG("Enter Grade Details ",'E');	tbpMAIN.setSelectedIndex(1);  return;}
			setTBP2DTL();			
		}
		else if(tbpMAIN.getSelectedIndex()==3)
		{
			if(tblGRDTL.getValueAt(0,intTB1_PRDCD).toString().length()==0)
				{setMSG("Enter Grade Details",'E'); 	tbpMAIN.setSelectedIndex(1); return;}
			setTBP3DTL();			
		}
	}

	
	
	/** Creting hash tables required during tabPANE-2 editing
	 *  (Common Tax Entry)
	 */
	private void setTBP2DTL()
	{
		hstGRTAX_TAX.clear();
		for(int i=0;i<tblGRTAX.getRowCount();i++)
		{
			if(tblGRTAX.getValueAt(i,intTB3_TAXCD).toString().length()==3)
				hstGRTAX_TAX.put(tblGRTAX.getValueAt(i,intTB3_TAXCD).toString(),"");
		}
	}

	/** Creting hash tables required during tabPANE-3 editing
	 *  (Grade Tax Entry)
	 */
	private void setTBP3DTL()
	{
		hstCOTAX_TAX.clear();
		for(int i=0;i<tblCOTAX.getRowCount();i++)
		{
			if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().length()==3)
				hstCOTAX_TAX.put(tblCOTAX.getValueAt(i,intTB3_TAXCD).toString(),"");
		}
		hstGRDTL_PRD.clear();
		for(int i=0;i<tblGRDTL.getRowCount();i++)
		{
			if(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length()==10)
				hstGRTAX_TAX.put(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString(),"");
		}
	}

	
	
	
/*  ********************************************************
	code from tbpMAIN.getSelectedIndex()==3
	********************************************************
			Hashtable L_hstGRDTX=new Hashtable (10,0.2f);
			for(int i=0;i<tblGRTAX.getRowCount()&&nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_PRDDS).toString(),"").length()>0;i++)
			{//Putting existing details of grade wise tax in a hash table
					L_hstGRDTX.put(nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_PRDDS).toString()," ")+"|"+nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_TAXCD).toString(),""),
								  //nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_TAXDS).toString()," ")+"|"+nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_PRTRF).toString()," ")+"|"+
								  //nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_PRTRF).toString()," ")+"|"+nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_AMTFL).toString()," ")+"|"+
								  nvlSTRVL(tblGRTAX.getValueAt(i,intTB3_AMTFL).toString()," "));
			}
			tblGRTAX.clrTABLE();//Data deleted from table
			Enumeration L_enmGRDKEYS=L_hstGRDTX.keys();
			int row=0;
			String L_strTEMP="",L_strTAXCD="",L_strGRDDS="";
			StringTokenizer L_stkTEMP=null;
			while (L_enmGRDKEYS.hasMoreElements())
			{
				L_strTEMP=(String)L_enmGRDKEYS.nextElement();
				L_stkTEMP=new StringTokenizer(L_strTEMP,"|");
				L_strGRDDS=L_stkTEMP.nextToken();
				L_strTAXCD=L_stkTEMP.nextToken();
				for(int i=0;i<tblCOTAX.getRowCount();i++)
				{//Finding out taxes having zero value in common tax tab
					if(nvlSTRVL(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"").equals(L_strTAXCD))
					{
						if(Double.parseDouble(nvlSTRVL(tblCOTAX.getValueAt(i,intTB2_TAXVL).toString(),"0.0"))==0.0)
						{
							tblGRTAX.setValueAt(L_strGRDDS,row,intTB3_PRDDS);
							tblGRTAX.setValueAt(L_strTAXCD,row,intTB3_TAXCD);
							if(L_hstGRDTX.containsKey(L_strTEMP))
							{//if details are available in history hashtable, display same data
								L_stkTEMP=new StringTokenizer(L_hstGRDTX.get(L_strTEMP).toString(),"|");
								tblGRTAX.setValueAt(L_stkTEMP.nextToken(),row,intTB3_TAXDS);
								//tblGRTAX.setValueAt(L_stkTEMP.nextToken(),row,intTB3_PRTRF);
								tblGRTAX.setValueAt(L_stkTEMP.nextToken(),row,intTB3_TAXVL);
								tblGRTAX.setValueAt(L_stkTEMP.nextToken(),row++,intTB3_AMTFL);
							}
						}
					}
				}
			}
			for(int i=0;i<tblCOTAX.getRowCount()&&nvlSTRVL(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"").length()>0;i++)
			{
				if(Double.parseDouble(nvlSTRVL(tblCOTAX.getValueAt(i,intTB2_TAXVL).toString(),"0.0"))==0.0)
				{
					if(!L_hstGRDTX.containsKey(tblGRDTL.getValueAt(0,intTB1_PRDDS).toString()+"|"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()))
						for(int j=0;j<tblGRDTL.getRowCount()&&nvlSTRVL(tblGRDTL.getValueAt(j,intTB1_PRDCD).toString(),"").length()>0;j++)
						{//If details are not available in history hash table
							tblGRTAX.setValueAt(tblGRDTL.getValueAt(j,intTB1_PRDDS),row,intTB3_PRDDS);
							tblGRTAX.setValueAt(tblCOTAX.getValueAt(i,intTB2_TAXCD),row,intTB3_TAXCD);
							tblGRTAX.setValueAt(tblCOTAX.getValueAt(i,intTB2_TAXDS),row++,intTB3_TAXDS);
							L_hstGRDTX.put(tblGRDTL.getValueAt(j,intTB1_PRDDS).toString()+"|"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"");
						}
				}
			}
			for(int i=0;i<tblGRDTL.getRowCount()&&nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString(),"").length()>0;i++)
			{//For entries for gades newly added into gade details tab
				for(int j=0;j<tblCOTAX.getRowCount()&&nvlSTRVL(tblCOTAX.getValueAt(j,intTB2_TAXCD).toString(),"").length()>0;j++)
				{
					if(!L_hstGRDTX.containsKey(tblGRDTL.getValueAt(i,intTB1_PRDDS).toString()+"|"+tblCOTAX.getValueAt(j,intTB2_TAXCD).toString()))
					{
						if(Double.parseDouble(nvlSTRVL(tblCOTAX.getValueAt(j,intTB2_TAXVL).toString(),"0.0"))==0.0)
						{
							tblGRTAX.setValueAt(tblGRDTL.getValueAt(i,intTB1_PRDDS),row,intTB3_PRDDS);
							tblGRTAX.setValueAt(tblCOTAX.getValueAt(j,intTB2_TAXCD),row,intTB3_TAXCD);
							tblGRTAX.setValueAt(tblCOTAX.getValueAt(j,intTB2_TAXDS),row,intTB3_TAXDS);
							tblGRTAX.setValueAt(tblCOTAX.getValueAt(j,intTB2_TAXVL),row,intTB3_TAXVL);
							tblGRTAX.setValueAt(tblCOTAX.getValueAt(j,intTB2_AMTFL),row++,intTB3_AMTFL);
						}
					}
				}
			}
******************************************************** */	
	
	/**<b>TASKS :</b><br>
	 * &nbsp&nbsp&nbspSource=cmbPMTCD/cmbDTPCD : Show/hide related details' fields depending on item selected<br>&nbsp&nbsp&nbsp&nbsp&nbspSelection Index used for this. There fore, change in display order should br taken care
	 * &nbsp&nbsp&nbspSource=cmbINDNO : Display respective Distributor details in address and ORDNO text Fields<br>	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				clrCOMP();
				setAUTFL();
				cmbINDNO.setEnabled(true);
				txtINDNO.setEnabled(true);
				cmbSALTP.setEnabled(true);
				cmbMKTTP.setEnabled(true);
				setCMBDFT(cmbDTPCD,"SYSMRXXDTP",strDFT_DTP);
				lblTRPCD.setVisible(true);txtTRPCD.setVisible(true);txtTRPNM.setVisible(true);
				setCMBDFT(cmbPMTCD,"SYSMRXXPMT",strDFT_PMT);
				setCMBDFT(cmbMOTCD,"SYSMR01MOT",strDFT_MOT);
				////setCDLST(vtrMKTTP,"MSTCOXXMKT","CMT_CODDS");
				////setCMBVL(cmbMKTTP,vtrMKTTP);
				
				if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					if(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))<=0)
					{
						setMSG("Addition in Previous Year Not allowed ..",'E');
						setENBL(false);
					}
				}
				setCMBSALTP();
				cmbSALTP.setEnabled(true);cmbSALTP.setVisible(true);
				cmbSALTP.requestFocus();
				//setCDLST(vtrSALTP,"SYSMR00SAL","CMT_CODDS");
				//setCMBVL(cmbSALTP,vtrSALTP);

			}

			else if(M_objSOURC==cmbSALTP && (cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0))
			{
				setCMBMKTTP();
				//inlCDTRN();
				//String L_strDSTCHK = cl_dat.M_strUSRCT_pbst.equals("02") ? " and cmt_modls||cmt_chp02 = '"+cl_dat.M_strUSRCD_pbst+"'" : "";
				//if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
				//	crtCDTRN("'MSTCOXXMKT'"," and  CMT_CHP01 like '%"+M_strSBSCD.substring(0,2)+"%'  and  CMT_CHP02 like '%"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"%'",hstCDTRN);
				//crtCDTRN("'D"+cl_dat.M_strCMPCD_pbst+"MR01IND','D"+cl_dat.M_strCMPCD_pbst+"MR02IND','D"+cl_dat.M_strCMPCD_pbst+"MR03IND','D"+cl_dat.M_strCMPCD_pbst+"MR04IND','D"+cl_dat.M_strCMPCD_pbst+"MR05IND','D"+cl_dat.M_strCMPCD_pbst+"MR12IND'",L_strDSTCHK+" and  SUBSTRING(CMT_CODCD,4,1)='"+strYRDGT+"' and CMT_CHP01='"+M_strSBSCD.substring(0,2)+"'",hstCDTRN);

				setDFTSTS();
				cmbMKTTP.setEnabled(true);cmbMKTTP.setVisible(true);
				cmbMKTTP.requestFocus();
			}
			else if(M_objSOURC==cmbMKTTP && (cmbMKTTP.getItemCount() > 0 && cmbMKTTP.getSelectedIndex()>0))
			{
					setCMBDFT(cmbDTPCD,"SYSMRXXDTP",strDFT_DTP);
					lblTRPCD.setVisible(true);txtTRPCD.setVisible(true);txtTRPNM.setVisible(true);
					setCMBDFT(cmbPMTCD,"SYSMRXXPMT",strDFT_PMT);
					setCMBDFT(cmbMOTCD,"SYSMR01MOT",strDFT_MOT);
  					setCMBINDNO();
					setDFTSTS();
					if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP) && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
						txtOCFNO.requestFocus();
					else
						cmbINDNO.requestFocus();
					//if(cmbMKTTP.getSelectedIndex()>0)
					//{
							//strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
					//}
			}
			else if(M_objSOURC==chkINDDSP)
				exeINDDSP();
			else if(M_objSOURC==chkREDTAX)
				exeREDTAX(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()), cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"CLEAR");
			else if(M_objSOURC==txtINDNO)
			{
				if(M_flgERROR)
					return;
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))//DISPLAY ORDER DETAILS IN MODIFICATION
					getDATA();
				if(((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")))
				{//SET DEFAULT VALUES FOR CAPTIVE CONSUMPTION
					//System.out.println("captive");
					//System.out.println(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN));
					txtBYRCD.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN));
					txtBYRNM.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CODDS", hstCDTRN));
					txtCNSCD.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN));
					txtCNSNM.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CODDS", hstCDTRN));
					new INPVF().verify(txtBYRCD);
					txtDSTCD.setText("NGT");
					txtTRPCD.setText("S0029");
					txtDSTNM.setText("Nagothane");
					txtPAYAC.setText("1");txtPAYCT.setText("1");
					chbTSHFL.setSelected(false);
					chbPSHFL.setSelected(false);
					chbINSFL.setSelected(false);
					txtREGRM.requestFocus();
				}
			}
		else if(M_objSOURC==txtPAYCT&&txtPAYAC.getText().length()==0)
			{
				txtPAYAC.requestFocus();
				txtPAYAC.setText(txtPAYCT.getText()); 
				txtPAYAC.select(0,txtPAYAC.getText().length());
			}
			if(M_objSOURC==txtPAYCT)
			{txtPAYAC.requestFocus();
				txtPAYAC.select(0,txtPAYAC.getText().length());
			}
	//CODE FOR INDENT AUTHORISATION
			//else if(M_objSOURC==btnAUTRN)
			//{
			//	if(btnAUTRN.getText().equalsIgnoreCase("Authorisation"))
			//	{
			//		setAUTH("AUTHORISE");
			//	}
			//	else if(btnAUTRN.getText().equalsIgnoreCase("Modification"))
			//	{
			//		setAUTH("MODIFY");
			//	}	
			//}
			else if(M_objSOURC==btnPRINT)
			{//PRINT ORDER
				//String [] L_staTEMP=objRPIND.getPRINTERS();
				//JComboBox L_cmbTEMP=new JComboBox(L_staTEMP);
				//L_cmbTEMP.insertItemAt("Select",0);
				//JOptionPane.showConfirmDialog( this,L_cmbTEMP,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
				//objRPIND.M_cmbDESTN=L_cmbTEMP;
				//objRPIND.M_cmbDESTN.setSelectedIndex(L_cmbTEMP.getSelectedIndex());

				objRPIND.txtMKTTP.setText(strMKTTP);
				objRPIND.txtINDNO.setText(cmbINDNO.getSelectedItem().toString()+txtINDNO.getText());
				objRPIND.flgOUTPRN = true;
				objRPIND.M_strSBSCD=M_strSBSCD;
				objRPIND.M_strSBSLS=M_strSBSLS;
				objRPIND.M_rdbHTML.setSelected(true);
				objRPIND.exePRINT();
			}
		}catch(Exception e)
		{setMSG(e,"Child.actionPerformed");}
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
	catch(Exception e){setMSG(e,"setCMBSALTP");}
}
	
	
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
	
	
	/**User friendly messagees	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
		 	this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(!M_flgERROR)
			{
				if(M_objSOURC==txtBYRCD)
					setMSG("Enter Byer Code; 'F1' : Search by Name; 'F2' : Search by Code ..",'N');
				else if(M_objSOURC==txtCNSCD)
					setMSG("Enter Consignee Code; 'F1' : Search by Name; 'F2' : Search by Code ..",'N');
				else if(M_objSOURC==txtPAYCT)
					setMSG("Enter Customer Payment term in days ..",'N');
				else if(M_objSOURC==txtDSTCD)
					setMSG("Enter Destination code, Press 'F1' for help ..",'N');
				else if(M_objSOURC==txtCRFDT)
					setMSG("Enter Customer Reference Date ..",'N');
				else if(M_objSOURC==txtCRFNO)
					setMSG("Enter Customer Reference Number ..",'N');
				else if(M_objSOURC==txtORDDT)
					setMSG("Enter Order Date ..",'N');
				else if(M_objSOURC==txtFORTRPCD)
					setMSG("Enter Transporter, Press 'F1' for Help ..",'N');
				else if(M_objSOURC==txtFORDSTCD)
					setMSG("Enter Destination, Press 'F1' for Help ..",'N');
				else if(M_objSOURC==txtFORSRCCD)
					setMSG("Enter Source, Press 'F1' for Help ..",'N');
				else if(M_objSOURC==tblCOTAX.cmpEDITR[intTB2_TAXCD])
					setMSG("Enter Tax Catagory, 'F1' for help ..",'N');
				else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_EUSCD])
					setMSG("Enter Usage Sector, 'F1' for search  ..",'N');
				else if(M_objSOURC==txtOCFNO)
					setMSG("Enter Order Confirmation No., Press 'F1' for Help ..",'N');
				
				else if(M_objSOURC==cmbMKTTP)
				{
					setMSG("Select Market Type ..",'N');
					cmbMKTTP.showPopup();
				}
				else if(M_objSOURC==cmbPMTCD)
				{
					setMSG("Select Payment Mode ..",'N');
					cmbPMTCD.showPopup();
				}
				else if(M_objSOURC==cmbDTPCD)
				{
					setMSG("Select Delivery Type ..",'N');
					cmbDTPCD.showPopup();
				}
				else if(M_objSOURC==cmbMOTCD)
				{
					setMSG("Select Mode of Transport ..",'N');
					cmbMOTCD.showPopup();
				}
				else if(M_objSOURC==cmbINDNO)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
						setMSG("Select Distributor, Press 'F1' for help ..",'N');
					else
						setMSG("Select Distributor ..",'N');
					cmbINDNO.showPopup();
				}
				else if(M_objSOURC==txtINDNO)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
						setMSG("Enter Order Number, Press 'F1' for help ..",'N');
					else
						setMSG("Enter Order Number ..",'N');
				}
			}
			if(M_objSOURC==txtPAYAC&&txtPAYAC.getText().length()==0)
			{
				txtPAYAC.setText(txtPAYCT.getText()); 
				txtPAYAC.select(0,txtPAYAC.getText().length());
			}
			else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_PKGTP])
			{
				//setMSG("Enter Pkg.Type , 'F1' for search  ..",'N');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
				{
					((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).setEditable(true);
					if(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PKGTP).toString().length()>2)
						if(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PRDCD).toString().length()==10 && tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PKGTP).toString().length()>0)
							if(!getGRDTL(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PKGTP).toString()),"GRD_REQQT").equals("0.00"))
								 ((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).setEditable(false);
				}
				else
				{
					((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).setEditable(true);
					if(((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().length()==0 && tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PRDCD).toString().length()==10)
					{
						((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).setText("01");
						tblGRDTL.setValueAt("01",tblGRDTL.getSelectedRow(),intTB1_PKGTP);
					}
					((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).select(0,2);
				}
			}

			else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_PRDCD])
			{
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setEditable(true);
				setMSG("Enter Grade, 'F1': Search by Code; 'F2': Search by description  ..",'N');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
				{	
					if(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PRDCD).toString().length()==10 && tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PKGTP).toString().length()>0)
						if(hstGRDTL.containsKey(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PKGTP).toString())))
							((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setEditable(false);
				}
				else
					((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setEditable(true);
			}
			else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_BASRT])
				exeDSP_ENBL(intTB1_BASRT);
			else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_EUSCD])
				exeDSP_ENBL(intTB1_EUSCD);
			else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_CDCVL])
				exeDSP_ENBL(intTB1_CDCVL);
			else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_DDCVL])
				exeDSP_ENBL(intTB1_DDCVL);
		//	else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_TDCVL])
		//		exeDSP_ENBL(intTB1_TDCVL);
		//	else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_TDCRF])
		//		exeDSP_ENBL(intTB1_TDCRF);
			/*if(M_objSOURC==txtDELTP1)
			{
				if(txtDELTP1.getText().toString().length()==0 && tblDLSCH.getValueAt(tblDLSCH.getSelectedRow(),intTB5_PRDCD).toString().length()==10)
				{
					txtDELTP1.setText("I");
					tblDLSCH.setValueAt("I",tblDLSCH.getSelectedRow(),intTB5_DELTP);
				}
					//txtDELTP.select(0,txtDELTP.getText().length());
			}*/
		}catch(Exception e)
		{
			setMSG(e,"TEIND.FocusGained"+M_objSOURC);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	
/** Enabling /Disabling table columns in Grade Entry table
 * According to despatch status
 */	
private void exeDSP_ENBL(int LP_TBLCOL)
{
		((JTextField)tblGRDTL.cmpEDITR[LP_TBLCOL]).setEditable(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
		{	
				if(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PRDCD).toString().length()==10 && tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PKGTP).toString().length()>0)
					if(exeDSPCHK1(tblGRDTL.getSelectedRow(),"INV_DOR_NOMSG"))
						if(hstGRDTL.containsKey(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),intTB1_PKGTP))))
							((JTextField)tblGRDTL.cmpEDITR[LP_TBLCOL]).setEditable(false);
		}
		else
			((JTextField)tblGRDTL.cmpEDITR[LP_TBLCOL]).setEditable(true);
}
	
	
	/**<b>TASKS : </B><br>
	 * Source = cmbMKTTP : start thread for collecting tax details	 */
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
			if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_PRDCD])
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setEditable(true);
			if(M_objSOURC==txtINDNO && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				tblCOTAX.clrTABLE();
				setDFTTAX1();
			}
			else if(M_objSOURC==cmbINDNO)
			{
				txtDSTNM.setVisible(true);
				txtBYRNM.setVisible(true);
				txtINDNO.setVisible(true);
				lblCNSDT.setVisible(true);
				lblDSTDT.setVisible(true);
				txtINDNO.requestFocus();
			}
			
		}catch(Exception e){setMSG(e,"Child.FocusLost");}
	}
	
/**<b>TASKS : </B><br>
 * &nbsp&nbsp&nbspSource = Column-3 : Smart search on pakage size */
/*	public void keyReleased(KeyEvent L_KE)
	{
		super.keyReleased(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		try
		{
			if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_PKGTP] && !L_KE.isActionKey()&&L_KE.getKeyCode()!=L_KE.VK_BACK_SPACE&&L_KE.getKeyCode()!=L_KE.VK_ENTER)
			{
				String L_strTEMP=((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText();
				if(L_strTEMP.length()>0)
				for(int i=0;i<vtrPKGSZ.size();i++)
				{
					if(vtrPKGSZ.elementAt(i).toString().substring(0,L_strTEMP.length()).equalsIgnoreCase(L_strTEMP))
					{
						((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).setText(vtrPKGSZ.elementAt(i).toString());
						((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).select(L_strTEMP.length(),((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().length());
						break;
					}
				}
			}
		}catch(Exception e)
		{
			setMSG(e,"Child.KeyReleased");
		}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
*/	
	

	
	/**
	 */
	public void keyReleased(KeyEvent L_KE)
	{
		super.keyReleased(L_KE);
		
		if(M_objSOURC==txtBYRCD)
			txtBYRCD.setText(txtBYRCD.getText().toUpperCase());
		if(M_objSOURC==txtCNSCD)
			txtCNSCD.setText(txtCNSCD.getText().toUpperCase());

	}

	/**<b>TASKS : </b><br>
	 * &nbsp&nbsp&nbspSource = txtBYRCD : HELP by buyer code(F2) as well as buyer name(F1)<br>
	 * &nbsp&nbsp&nbspSource = txtCONCD : HELP by consignee code(F2) as well as consignee name(F1)<br> 
	 * &nbsp&nbsp&nbspFocus navigation
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
		int L_intKEYCD=L_KE.getKeyCode();
		try
		{
			if(L_intKEYCD==L_KE.VK_LEFT&&L_KE.isControlDown())
				tbpMAIN.setSelectedIndex(tbpMAIN.getSelectedIndex()%tbpMAIN.getTabCount()-1);
			else if(L_intKEYCD==L_KE.VK_RIGHT&&L_KE.isControlDown())
				tbpMAIN.setSelectedIndex(tbpMAIN.getSelectedIndex()%tbpMAIN.getTabCount()+1);
			//else if(M_objSOURC==txtDSRTP)
			//{
			//	if(L_intKEYCD==L_KE.VK_F1)
			//	{//Search by Name
			//			M_strSQLQRY="SELECT CMT_CODCD, CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRT' and CMT_CODCD in ('D','G') ORDER BY CMT_CODCD";
			//			M_strHLPFLD = "txtDSRTP";
			//			cl_hlp(M_strSQLQRY ,2,1,new String[] {"Code","Description"},2 ,"CT");
			//	}
			//}
			else if(M_objSOURC==txtBYRCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{//Search by Name
						if(txtBYRCD.getText().length()==0)
							{setMSG("Type First letter and then press F1",'E'); return;}
						if(!(txtBYRCD.getText().length()==1))
							return;
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03,PT_DSTCD from CO_PTMST where PT_PRTTP='C' and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" +(((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")) ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
						M_strHLPFLD = "txtBYRCD1";
						cl_hlp(M_strSQLQRY ,1,2,new String[] {"Byer Name","Byer Code"},5 ,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtBYRCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtBYRCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_DSTCD from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" + (((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")) ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_DSTCD from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtBYRCD.getText()+"%' "+(((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")) ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtBYRCD2";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Byer Code","Byer Name"},5,"CT");
					cl_dat.M_txtHLPPOS_pbst.setText(txtBYRCD.getText().toUpperCase());
				}
				else if(L_intKEYCD==L_KE.VK_F9)
				{//Display Buyer details
					if(strBYRDT!=null)
						JOptionPane.showMessageDialog(txtBYRCD,strBYRDT);
					else
						dspADDR(txtBYRCD,txtBYRCD.getText(),"C");
				}
			}
			else if(M_objSOURC==txtTRPCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{//Search by Name
						if(txtTRPCD.getText().length()==0)
							{setMSG("Type First letter and then press F1",'E'); return;}
						if(!(txtTRPCD.getText().length()==1))
							return;
                        txtTRPCD.setText(txtTRPCD.getText().toUpperCase());
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='T' and PT_PRTCD like '"+txtTRPCD.getText().toUpperCase()+"%' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
						M_strHLPFLD = "txtTRPCD1";
						cl_hlp(M_strSQLQRY ,1,2,new String[] {"Transporter Name","Transporter Code"},5 ,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtTRPCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtTRPCD.getText().length()==1)
					M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" + (((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")) ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtTRPCD2";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Transporter Name","Transporter Code"},5 ,"CT");
					cl_dat.M_txtHLPPOS_pbst.setText(txtTRPCD.getText().toUpperCase());
				}
				else if(L_intKEYCD==L_KE.VK_F9)
				{//Display Buyer details
					if(strTRPDT!=null)
						JOptionPane.showMessageDialog(txtTRPCD,strTRPDT);
					else
						dspADDR(txtTRPCD,txtTRPCD.getText(),"C");
				}
			}
			/*else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_TDCRF])
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{//Search by Name
					if(((JTextField)tblGRDTL.cmpEDITR[intTB1_TDCRF]).getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(((JTextField)tblGRDTL.cmpEDITR[intTB1_TDCRF]).getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+((JTextField)tblGRDTL.cmpEDITR[intTB1_TDCRF]).getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+((JTextField)tblGRDTL.cmpEDITR[intTB1_TDCRF]).getText()+"%' ORDER BY PT_PRTNM";
					M_strHLPFLD = "tblGRDTL.cmpEDITR[intTB1_TDCRF]1";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Third Party Name","Third Party Code"},5 ,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtBYRCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtBYRCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"' ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtBYRCD.getText()+"%' ORDER BY PT_PRTNM";
					M_strHLPFLD = "tblGRDTL.cmpEDITR[intTB1_TDCRF]2";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Byer Code","Byer Name"},5,"CT");
					cl_dat.M_txtHLPPOS_pbst.setText(txtBYRCD.getText().toUpperCase());
				} 
			}*/
			else if(M_objSOURC==txtCNSCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{//search by Name
					if(txtCNSCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtCNSCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03,PT_DSTCD from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+txtCNSCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03,PT_DSTCD from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtCNSCD.getText().toUpperCase()+"%' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtCNSCD1";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Consignee Name","Consignee Code"},5,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtCNSCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtCNSCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_DSTCD from CO_PTMST where PT_PRTTP='C' and SUBSTRING(PT_PRTCD,1,1)= '"+txtCNSCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_DSTCD from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtCNSCD.getText().toUpperCase()+"%'"+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtCNSCD2";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Consignee Code","Consignee Name"},5,"CT");
					cl_dat.M_txtHLPPOS_pbst.setText(txtCNSCD.getText().toUpperCase());
				}
				else if(L_intKEYCD==L_KE.VK_F9)
				{//Display Consignee details
					if(strCNSDT!=null)
						JOptionPane.showMessageDialog(txtCNSCD,strCNSDT);
					else
						dspADDR(txtCNSCD,txtCNSCD.getText(),"C");
				}
			}
			else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_PRDCD])//txtGRDCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{
					M_strHLPFLD = "txtGRDCD2";
					//cl_hlp("Select PR_PRDCD,PR_PRDDS from CO_PRMST   where SUBSTRING(pr_prdcd,1,2) in ('51','52','53') and SUBSTRING(pr_prdcd,1,2)= '"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().substring(0,2)  +"' order by PR_PRDDS" ,2,1,new String[] {"Grade Code","Description"},2,"CT");
					cl_hlp(getHLPVTR("PRDCD",((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText()) ,2,1,new String[] {"Grade Code","Description"},2,"CT");
				}
			}
			else if(M_objSOURC==tblDLSCH.cmpEDITR[intTB5_PRDCD])//txtGRDCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{
					M_strHLPFLD = "txtGRDCD3";
                    String L_strPRDLS  ="";
                    int L_intCNT =0;
                    hstPRDDT = new Hashtable<String,String>(); 
                    for (int i=0;i<tblGRDTL.getRowCount();i++)
                    {
                        if(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length() ==10)
                        {
                            //hstPRDDT.put(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString(),nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_PKGTP).toString(),"")+"|"+nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_REQQT).toString(),"0")+"|"+nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_DELTP).toString(),"I"));
                            hstPRDDT.put(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString()+tblGRDTL.getValueAt(i,intTB1_PKGTP).toString(),nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_PKGTP).toString(),"")+"|"+nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_REQQT).toString(),"0"));
	                        System.out.println("hstPRDDT.put"+tblGRDTL.getValueAt(i,intTB1_PRDCD).toString()+tblGRDTL.getValueAt(i,intTB1_PKGTP).toString());
                            if(L_intCNT == 0)
                                L_strPRDLS += "'"+tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().trim()+tblGRDTL.getValueAt(i,intTB1_PKGTP).toString().trim()+"'";
                            else 
                                L_strPRDLS += ",'"+tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().trim()+tblGRDTL.getValueAt(i,intTB1_PKGTP).toString().trim()+"'";
                            L_intCNT++;    
                        } 
                        //System.out.println(L_strPRDLS);  
                    }
                    M_strSQLQRY = "SELECT PR_PRDCD,PR_PRDDS,cmt_codds from CO_PRMST,co_cdtrn  WHERE PR_PRDCD + cmt_codds  in(";
                    M_strSQLQRY += L_strPRDLS.trim() +")";
                    //System.out.println(M_strSQLQRY);
                 	cl_hlp(M_strSQLQRY ,2,1,new String[] {"Grade Code","Description","Package Type"},3,"CT");
				}
			}
			else if(M_objSOURC==tblGRTAX.cmpEDITR[intTB3_PRDCD]) 
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{
					M_strHLPFLD = "txtGRTAX_PRDCD";
					//cl_hlp("Select PR_PRDCD,PR_PRDDS from CO_PRMST where  SUBSTRING(pr_prdcd,1,2) in ('51','52','53') order by PR_PRDCD" ,1,1,new String[] {"Code","Description"},2,"CT");
					cl_hlp(getHLPVTR("PRDCD",txtGRTAX_PRDCD.getText()),2,1,new String[] {"Code","Description"},2,"CT");
				}
			}
			else if(L_intKEYCD==L_KE.VK_F1)
			{
				if((M_objSOURC==txtINDNO||M_objSOURC==cmbINDNO)&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
				{
					M_strHLPFLD = "ORDNO";
					if(cmbINDNO.getSelectedIndex()==0)
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,PT_PRTNM from MR_INMST, CO_PTMST where pt_prtcd=in_byrcd and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,IN_BYRNM from MR_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,IN_BYRNM from VW_INTRN where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(int_dorqt,0)=0 and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
					else
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,PT_PRTNM from MR_INMST, CO_PTMST where pt_prtcd=in_byrcd and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_INDNO like '"+cmbINDNO.getSelectedItem().toString()+"%' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,IN_BYRNM from MR_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_INDNO like '"+cmbINDNO.getSelectedItem().toString()+"%' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,IN_BYRNM from VW_INTRN where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(int_dorqt,0)=0 and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_INDNO like '"+cmbINDNO.getSelectedItem().toString()+"%' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
				}
				if((M_objSOURC==txtOCFNO)&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==1)
				{
					M_strHLPFLD = "OCFNO";
					//if(cmbINDNO.getSelectedIndex()==0)
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,PT_PRTNM from MR_INMST, CO_PTMST where pt_prtcd=in_byrcd and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						cl_hlp("Select DISTINCT OC_OCFNO,OC_OCFDT,OC_BYRNM from MR_OCMST,MR_OCTRN where OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_SBSCD='"+M_strSBSCD +"' and OC_STSFL not in('X','0') and OCT_SBSCD = '"+M_strSBSCD+"' and OC_CMPCD=OCT_CMPCD and OC_OCFNO=OCT_OCFNO and OCT_OCFQT-OCT_INDQT > 0 order by OC_OCFNO" ,1,1,new String[] {"Ord.Conf.No.","Description"},3,"CT");
//						System.out.println('"Select DISTINCT OC_OCFNO,OC_OCFDT,OC_BYRNM from MR_OCMST,MR_OCTRN where OC_SBSCD='"+M_strSBSCD +"' and OC_STSFL not in('X') and OCT_SBSCD = '"+M_strSBSCD+"' and OC_OCFNO=OCT_OCFNO and OCT_OCFQT-OCT_INDQT > 0 order by OC_OCFNO"');
					//else
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,PT_PRTNM from MR_INMST, CO_PTMST where pt_prtcd=in_byrcd and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_INDNO like '"+cmbINDNO.getSelectedItem().toString()+"%' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,IN_BYRNM from MR_INMST where in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_INDNO like '"+cmbINDNO.getSelectedItem().toString()+"%' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
				}
				else if(M_objSOURC==txtPKGTP)
				{
					M_strHLPFLD = "txtPKGTP";
					//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='FGXXPKG' order by CMT_CODCD" ,1,1,new String[] {"Code","Description"},2,"CT");
					cl_hlp(getHLPVTR("PKGTP",txtPKGTP.getText()),2,1,new String[] {"Code","Description"},2,"CT");
				}
				else if(M_objSOURC==tblDLSCH.cmpEDITR[intTB5_PKGTP])
				{
					M_strHLPFLD = "txtPKGTP1";
					//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='FGXXPKG' order by CMT_CODCD" ,1,1,new String[] {"Code","Description"},2,"CT");
					cl_hlp(getHLPVTR("PKGTP",txtPKGTP.getText()),2,1,new String[] {"Code","Description"},2,"CT");
				}
				else if(M_objSOURC==txtEUSCD)
				{
					M_strHLPFLD = "txtEUSCD";
					//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='MR00EUS' order by CMT_CODCD" ,1,1,new String[] {"Code","Description"},2,"CT");
					cl_hlp(getHLPVTR("EUSCD",txtEUSCD.getText()),2,1,new String[] {"Code","Description"},2,"CT");
				}
				else if(M_objSOURC==txtCOTAX_TAXCD)
				{
					M_strHLPFLD = "txtCOTAX_TAXCD";
					cl_hlp("Select CMT_CODCD,CMT_CODDS,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXTAX' order by CMT_CODCD" ,1,1,new String[] {"Code","Description","Value","Amt/%","Prc.Sq"},5,"CT");
				}
				else if(M_objSOURC==txtGRTAX_TAXCD)
				{
					M_strHLPFLD = "txtGRTAX_TAXCD";
					cl_hlp("Select CMT_CODCD,CMT_CODDS,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXTAX' order by CMT_CODCD" ,1,1,new String[] {"Code","Description","Value","Amt/%","Prc.Sq"},5,"CT");
				}
				else if(M_objSOURC==txtCURCD)
				{
					M_strHLPFLD = "txtCURCD";
					cl_hlp(getHLPVTR("CURCD",txtCURCD.getText()),2,1,new String[] {"Code","Description"},2,"CT");
				}
				else if(M_objSOURC==txtDSTCD)
				{
					M_strHLPFLD = "txtDSTCD";
					if(txtDSTCD.getText().length()==0)
						//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' order by CMT_CODDS" ,2,1,new String[] {"Grade Code","Description"},2,"CT");
						cl_hlp(getHLPVTR("DSTCD",txtDSTCD.getText()),2,1,new String[] {"Grade Code","Description"},2,"CT");
					else
						//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' and CMT_CODCD like '"+txtDSTCD.getText()+"%' order by CMT_CODDS" ,2,1,new String[] {"Grade Code","Description"},2,"CT");
						cl_hlp(getHLPVTR("DSTCD",txtDSTCD.getText()),2,1,new String[] {"Grade Code","Description"},2,"CT");
				}
				else if(M_objSOURC==txtFORTRPCD)
				{
					M_strHLPFLD = "txtFORTRPCD";
					if(txtFORTRPCD.getText().length()==0)
						cl_hlp("Select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP='T' and upper(isnull(PT_STSFL,' ')) <> 'X' order by PT_PRTNM" ,2,1,new String[] {"Transporter Code","Name"},2,"CT");
					else
						cl_hlp("Select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP='T' and PT_PRTCD like '"+txtFORTRPCD.getText()+"%' and upper(isnull(PT_STSFL,' ')) <> 'X' order by PT_PRTNM" ,2,1,new String[] {"Transporter Code","Name"},2,"CT");
				}
				else if(M_objSOURC==txtFORSRCCD)
				{
					M_strHLPFLD = "txtFORSRCCD";
					if(txtFORSRCCD.getText().length()==0)
						//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' order by CMT_CODDS" ,2,1,new String[] {"Code","Name"},2,"CT");
						cl_hlp(getHLPVTR("DSTCD",txtFORSRCCD.getText()),2,1,new String[] {"Grade Code","Description"},2,"CT");
					else
						//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' and CMT_CODCD like '"+txtFORSRCCD.getText()+"%' order by CMT_CODDS" ,1,1,new String[] {"Code","Name"},2,"CT");
						cl_hlp(getHLPVTR("DSTCD",txtFORSRCCD.getText()),2,1,new String[] {"Grade Code","Description"},2,"CT");
				}
				else if(M_objSOURC==txtFORDSTCD)
				{
					M_strHLPFLD = "txtFORDSTCD";
					if(txtFORDSTCD.getText().length()==0)
						//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' order by CMT_CODDS" ,2,1,new String[] {"Code","Name"},2,"CT");
						cl_hlp(getHLPVTR("DSTCD",txtFORDSTCD.getText()),2,1,new String[] {"Grade Code","Description"},2,"CT");
					else
						//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST and CMT_CODCD like '"+txtFORDSTCD.getText()+"%' order by CO_CODDS" ,2,1,new String[] {"Code","Name"},2,"CT");
						cl_hlp(getHLPVTR("DSTCD",txtFORDSTCD.getText()),2,1,new String[] {"Grade Code","Description"},2,"CT");
				}
				//else if(M_objSOURC==tblCOTAX.cmpEDITR[intTB2_TAXCD])
				//{
				//	M_strHLPFLD = "tblCOTAX";
				//	cl_hlp("Select CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' order by CMT_CODCD" ,1,1,new String[] {"Code","Name"},6,"CT");
				//}
				else if(M_objSOURC==tblGRTAX.cmpEDITR[intTB3_TAXCD])
				{
					M_strHLPFLD = "tblGRTAX";
					cl_hlp("Select CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_NCSVL,CMT_CHP02,CMT_CCSVL from CO_CDTRN where CMT_CGSTP='COXXTAX' order by CMT_CODCD" ,1,1,new String[] {"Code","Name"},6,"CT");
				}
			}
			else if(M_objSOURC==txtDSRNM&&L_intKEYCD==L_KE.VK_F9)
			{
				dspADDR(txtDSRNM,txtDSRCD.getText(),lblDSRTP.getText());
			}
			else if(M_objSOURC==txtLCNO&&L_intKEYCD==L_KE.VK_F9)
			{//DISPLAY WINDOW FOR ENTERING NEW LC DETAILS
				if(pnlLCDTL==null)
				{
					pnlLCDTL=new JPanel(null);
					add(new JLabel("LC Number :"),1,1,1,1,pnlLCDTL,'L');
					add(txtLCNEW=new JTextField(),1,2,1,1,pnlLCDTL,'L');
					add(new JLabel("Opening Date :"),2,1,1,1,pnlLCDTL,'L');
					add(txtLCOPNDT=new TxtDate(),2,2,1,1,pnlLCDTL,'L');
					add(new JLabel("Expiry Date :"),3,1,1,1,pnlLCDTL,'L');
					add(txtLCEXPDT=new TxtDate(),3,2,1,1,pnlLCDTL,'L');
					add(new JLabel("Opening Value :"),4,1,1,1,pnlLCDTL,'L');
					add(txtLCOPNVL=new JTextField(),4,2,1,1,pnlLCDTL,'L');
					add(new JLabel("Utilised Value :"),5,1,1,1,pnlLCDTL,'L');
					add(txtLCUTLVL=new JTextField(),5,2,1,1,pnlLCDTL,'L');
				}
				pnlLCDTL.setSize(100,100);
				pnlLCDTL.setPreferredSize(new Dimension(300,300));
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlLCDTL,"Enter LC Details",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN==0)
					txtLCNO.setText(txtLCNEW.getText());
				else
					txtLCNEW.setText("");
				txtLCNEW.requestFocus();
			}
			else if(M_objSOURC==txtDEFNO&&L_intKEYCD==L_KE.VK_F9)
			{//DISPLAY WINDOW FOR ENTERING NEW D.E. FILE DETAILS
				if(pnlDEFDTL==null)
				{
					pnlDEFDTL=new JPanel(null);
					add(new JLabel("D./Exp File Number :"),1,1,1,1,pnlDEFDTL,'L');
					add(txtDEFNEW=new JTextField(),1,2,1,1,pnlDEFDTL,'L');
					add(new JLabel("Opening Date :"),2,1,1,1,pnlDEFDTL,'L');
					add(txtDEFOPNDT=new TxtDate(),2,2,1,1,pnlDEFDTL,'L');
					add(new JLabel("Expiry Date :"),3,1,1,1,pnlDEFDTL,'L');
					add(txtDEFEXPDT=new TxtDate(),3,2,1,1,pnlDEFDTL,'L');
					add(new JLabel("Opening Value :"),4,1,1,1,pnlDEFDTL,'L');
					add(txtDEFOPNVL=new JTextField(),4,2,1,1,pnlDEFDTL,'L');
					add(new JLabel("Utilised Value :"),5,1,1,1,pnlDEFDTL,'L');
					add(txtDEFUTLVL=new JTextField(),5,2,1,1,pnlDEFDTL,'L');
					add(new JLabel("Opening Quantity :"),6,1,1,1,pnlDEFDTL,'L');
					add(txtDEFOPNQT=new JTextField(),6,2,1,1,pnlDEFDTL,'L');
					add(new JLabel("Utilised Quantity :"),7,1,1,1,pnlDEFDTL,'L');
					add(txtDEFUTLQT=new JTextField(),7,2,1,1,pnlDEFDTL,'L');
				}
				pnlDEFDTL.setSize(100,100);
				pnlDEFDTL.setPreferredSize(new Dimension(300,300));
				txtDEFNEW.requestFocus();
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDEFDTL,"Enter D./Exp. File Details",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN==0)
					txtDEFNO.setText(txtDEFNEW.getText());
				else
					txtDEFNEW.setText("");
			}
			else if(M_objSOURC==txtAPDNO&&L_intKEYCD==L_KE.VK_F9)
			{//DISPLAY WINDOW FOR ENTERING NEW A.P. DOCUMENT DETAILS
				if(pnlAPDDTL==null)
				{
					pnlAPDDTL=new JPanel(null);
					add(new JLabel("Cheque Number :"),1,1,1,1,pnlAPDDTL,'L');
					add(txtAPDCHQNO=new JTextField(),1,2,1,1,pnlAPDDTL,'L');
					add(new JLabel("Cheque Date :"),2,1,1,1,pnlAPDDTL,'L');
					add(txtAPDCHQDT=new TxtDate(),2,2,1,1,pnlAPDDTL,'L');
					add(new JLabel("Deposition Date :"),3,1,1,1,pnlAPDDTL,'L');
					add(txtAPDDEPDT=new TxtDate(),3,2,1,1,pnlAPDDTL,'L');
					add(new JLabel("Cheque Value :"),4,1,1,1,pnlAPDDTL,'L');
					add(txtAPDCHQVL=new JTextField(),4,2,1,1,pnlAPDDTL,'L');
					add(new JLabel("Utilised Value :"),5,1,1,1,pnlAPDDTL,'L');
					add(txtAPDUTLVL=new JTextField(),5,2,1,1,pnlAPDDTL,'L');
				}
				pnlAPDDTL.setSize(100,100);
				pnlAPDDTL.setPreferredSize(new Dimension(300,300));
				txtAPDCHQNO.requestFocus();
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlAPDDTL,"Enter Advance Payment Details",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN==0)
					txtAPDNO.setText(txtAPDCHQNO.getText());
				else
					txtAPDCHQNO.setText("");
			}
			else if(M_objSOURC==txtFORNO&&L_intKEYCD==L_KE.VK_F9)
			{//DISPLAY WINDOW FOR ENTERING NEW F.O.R. DETAILS
				if(pnlFORDTL==null)
				{
					pnlFORDTL=new JPanel(null);
					add(new JLabel("Transporter :"),1,1,1,1,pnlFORDTL,'L');
					add(txtFORTRPCD=new JTextField(),1,2,1,1,pnlFORDTL,'L');
					add(new JLabel("Source :"),2,1,1,1,pnlFORDTL,'L');
					add(txtFORSRCCD=new JTextField(),2,2,1,1,pnlFORDTL,'L');
					add(new JLabel("Destination :"),3,1,1,1,pnlFORDTL,'L');
					add(txtFORDSTCD=new JTextField(),3,2,1,1,pnlFORDTL,'L');
					add(new JLabel("Freight Value :"),4,1,1,1,pnlFORDTL,'L');
					add(txtFORFRTVL=new TxtNumLimit(10.2),4,2,1,1,pnlFORDTL,'L');
					add(new JLabel("Freight Type :"),5,1,1,1,pnlFORDTL,'L');
					add(rdbFORRATFL_A=new JRadioButton("Amount"),5,2,1,0.5,pnlFORDTL,'R');
					add(rdbFORRATFL_R=new JRadioButton("Rate"),5,2,1,0.5,pnlFORDTL,'L');
					ButtonGroup btg=new ButtonGroup();
					btg.add(rdbFORRATFL_A);btg.add(rdbFORRATFL_R);
					add(new JLabel("Additional Value :"),6,1,1,1,pnlFORDTL,'L');
					add(txtFORADDVL=new TxtNumLimit(10.2),6,2,1,1,pnlFORDTL,'L');
					add(new JLabel("Vehicle Type :"),7,1,1,1,pnlFORDTL,'L');
					add(txtFORVEHTP=new JTextField(),7,2,1,1,pnlFORDTL,'L');
					add(new JLabel("Vehicle Capacity :"),8,1,1,1,pnlFORDTL,'L');
					add(txtFORVEHQT=new TxtNumLimit(10.3),8,2,1,1,pnlFORDTL,'L');
				}
				pnlFORDTL.setSize(100,100);
				pnlFORDTL.setPreferredSize(new Dimension(300,300));
				txtFORTRPCD.requestFocus();
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlFORDTL,"Enter Freight Charges Details",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN==0)
					txtFORNO.setText(txtFORTRPCD.getText());
				else
					txtFORTRPCD.setText("");
			}
			else if(M_objSOURC==rdbFORRATFL_A&&L_intKEYCD==L_KE.VK_ENTER)
			{
				txtFORADDVL.requestFocus();
			}
			else if(M_objSOURC==txtOCFNO &&L_intKEYCD==L_KE.VK_ENTER)
			{
				//txtFORADDVL.requestFocus();
				setOCDTL();
			}
			else if(M_objSOURC==rdbFORRATFL_R&&L_intKEYCD==L_KE.VK_ENTER)
			{
				rdbFORRATFL_A.requestFocus();
			}
			if(M_objSOURC==((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD])&&L_intKEYCD!=L_KE.VK_F1)
				L_KE.consume();
			if(L_intKEYCD==L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtINDNO)
					txtBYRCD.requestFocus();
			else if(M_objSOURC==txtPAYCT&&txtPAYAC.getText().length()==0)
			{
				txtPAYAC.requestFocus();
				txtPAYAC.setText(txtPAYCT.getText()); 
				txtPAYAC.select(0,txtPAYAC.getText().length());
			}
			else if(M_objSOURC==txtCOTAX_TAXCD)
			{
				String L_strTAXCD = ((JTextField) tblCOTAX.cmpEDITR[intTB2_TAXCD]).getText();
				tblCOTAX.cmpEDITR[intTB2_TAXCD].setEnabled(true);
				//if(!L_strTAXCD.equals("STX") && !L_strTAXCD.equals("OTH"))
				//	tblCOTAX.cmpEDITR[intTB2_TAXCD].setEnabled(false);
				updateUI();
			}
			
			else if(M_objSOURC==txtREGRM)
			{
				if(txtTRPCD.isVisible())
				{
				    txtTRPCD.requestFocus();
				}
				else
				{
    				tbpMAIN.setSelectedIndex(1);
    				tblGRDTL.requestFocus();
    				tblGRDTL.editCellAt(0,intTB1_PRDCD);
    				tblGRDTL.cmpEDITR[intTB1_PRDCD].requestFocus();
    				updateUI();
				}
			}
			else if(M_objSOURC==txtTRPCD)
			{
				txtDELAD.requestFocus();
			}
			else if(M_objSOURC==txtDELAD)
			{
				txtPRTRF.requestFocus();
			}
			else if(M_objSOURC==txtPRTRF)
			{
				tbpMAIN.setSelectedIndex(1);
				tblGRDTL.requestFocus();
				tblGRDTL.editCellAt(0,intTB1_PRDCD);
				tblGRDTL.cmpEDITR[intTB1_PRDCD].requestFocus();
				updateUI();
			}

			else if(M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_PKGTP] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_REQQT] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_BASRT] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_EUSCD])
				((Component)M_objSOURC).transferFocus();
			}
		}catch(Exception e)	{setMSG(e,"Child.KeyPressed");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}


	/** Action to be executed after selecting a code using F1
	 */
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			cl_dat.M_wndHLP_pbst=null;
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			//if(M_strHLPFLD.equals("lblDSRTP"))
			//{
			//	lblDSRTP.setText(L_STRTKN.nextToken());
			//	setCMBINDNO();
			//	cmbINDNO.requestFocus();
			//}
			if(M_strHLPFLD.equals("txtBYRCD1"))
			{
				txtBYRNM.setText(L_STRTKN.nextToken());
				txtBYRCD.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strBYRDT=txtBYRNM.getText();
				while (L_STRTKN.hasMoreTokens())
					strBYRDT += "\n"+L_STRTKN.nextToken();
				txtCNSCD.setText(txtBYRCD.getText());
				txtCNSNM.setText(txtBYRNM.getText());
				txtDSTCD.setText(L_STRTKN.nextToken());
				getOVRDUE();
				txtCNSCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtBYRCD2"))
			{
				txtBYRCD.setText(L_STRTKN.nextToken());
				txtBYRNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strBYRDT=txtBYRNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
				//txtCNSCD.setText(txtBYRCD.getText(L_STRTKN.nextToken()));
				txtCNSCD.setText(txtBYRCD.getText());

				txtDSTCD.setText(L_STRTKN.nextToken());
				txtCNSNM.setText(txtBYRNM.getText());
				txtCNSCD.requestFocus();
			}
			if(M_strHLPFLD.equals("txtTRPCD1"))
			{
				txtTRPNM.setText(L_STRTKN.nextToken());
				txtTRPCD.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strTRPDT=txtTRPNM.getText();
				while (L_STRTKN.hasMoreTokens())
					strTRPDT += "\n"+L_STRTKN.nextToken();
			//	txtTRPCD.setText(txtTRPCD.getText());
			//	txtTRPNM.setText(txtTRPNM.getText());
			}
			else if(M_strHLPFLD.equals("txtTRPCD2"))
			{
				txtTRPCD.setText(L_STRTKN.nextToken());
				txtTRPNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strTRPDT=txtTRPNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
			//	txtTRPCD.setText(txtTRPCD.getText());
			//	txtCNSNM.setText(txtBYRNM.getText());
			//	txtCNSCD.requestFocus();
			}
			/*if(M_strHLPFLD.equals("tblGRDTL.cmpEDITR[intTB1_TDCRF]1"))
			{
				L_STRTKN.nextToken();
				((JTextField)tblGRDTL.cmpEDITR[intTB1_TDCRF]).setText(L_STRTKN.nextToken());
				tblGRDTL.setRowSelectionInterval(tblGRDTL.getSelectedRow()+1,tblGRDTL.getSelectedRow()+1);
				tblGRDTL.setColumnSelectionInterval(1,1);
				tblGRDTL.editCellAt(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedColumn());
				tblGRDTL.cmpEDITR[intTB1_PRDCD].requestFocus();
			}
			else if(M_strHLPFLD.equals("tblGRDTL.cmpEDITR[intTB1_TDCRF]2"))
			{
				((JTextField)tblGRDTL.cmpEDITR[intTB1_TDCRF]).setText(L_STRTKN.nextToken());
				tblGRDTL.cmpEDITR[intTB1_TDCRF].requestFocus();
			}*/
			if(M_strHLPFLD.equals("txtCNSCD1"))
			{
				txtCNSNM.setText(L_STRTKN.nextToken());
				txtCNSCD.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strCNSDT=txtCNSNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
				txtDSTCD.setText(L_STRTKN.nextToken());
				txtDSTCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtCNSCD2"))
			{
				txtCNSCD.setText(L_STRTKN.nextToken());
				txtCNSNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strCNSDT=txtCNSNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
				txtDSTCD.setText(L_STRTKN.nextToken());
				txtDSTCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtDSTCD"))
			{
				txtDSTCD.setText(L_STRTKN.nextToken());
				txtDSTNM.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtCURCD"))
			{
				txtCURCD.setText(L_STRTKN.nextToken());
				txtECHRT.setText(getCDTRN("MSTCOXXCUR"+txtCURCD.getText(),"CMT_NCSVL",hstCDTRN));
			}
			else if(M_strHLPFLD.equals("txtGRDCD1")||M_strHLPFLD.equals("txtGRDCD2"))
			{
				String L_strTEMP=L_STRTKN.nextToken();
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText(L_strTEMP);
				tblGRDTL.setValueAt(L_STRTKN.nextToken(),tblGRDTL.getSelectedRow(),intTB1_PRDDS);
				//tblGRDTL.setValueAt(L_strTEMP.substring(0,2).equals("SX") ? "SQM" : "MT",tblGRDTL.getSelectedRow(),intTB1_UOMCD);
				tblGRDTL.setValueAt("MT",tblGRDTL.getSelectedRow(),intTB1_UOMCD);
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText(L_strTEMP);
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).requestFocus();
				if(((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52") || M_strSBSCD.substring(0,2).equals("SX")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")))
				{
					String L_strPRDCD = ((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString();
					//String L_strPKGTP = getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString());
					
					//tblGRDTL.setValueAt(getOPSTK(L_strPRDCD,L_strPKGTP,"OP_AVGRT"),tblGRDTL.getSelectedRow(),intTB1_BASRT);
				}
				for(int i=0;i<tblGRDTL.getSelectedRow();i++)
					if(tblGRDTL.getValueAt(i,intTB1_PRDCD).equals(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText()))
					{//AVOIDS DUPLICATION OF GRADE ENTRIES IN AN ORDER
						setMSG("Grade is Already Added ..",'E');
						//((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText("");
						//tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_PRDDS);
						//tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_BASRT);
						//return;
					}
			}
			else if(M_strHLPFLD.equals("txtGRDCD3"))
			{
			    
				String L_strTEMP=L_STRTKN.nextToken();
				//System.out.println(L_strTEMP);
				String L_strTEMP1=L_STRTKN.nextToken();
				//System.out.println(L_strTEMP1);
				String L_strTEMP2=L_STRTKN.nextToken();
				//System.out.println(L_strTEMP2);
				((JTextField)tblDLSCH.cmpEDITR[intTB5_PRDCD]).setText(L_strTEMP);
				StringTokenizer L_STRTK1=new StringTokenizer(hstPRDDT.get(L_strTEMP+L_strTEMP2).toString(),"|");
				L_STRTK1.nextToken();
				tblDLSCH.setValueAt(Boolean.TRUE,tblDLSCH.getSelectedRow(),intTB5_CHKFL);
				tblDLSCH.setValueAt(L_strTEMP1,tblDLSCH.getSelectedRow(),intTB5_PRDDS);
				tblDLSCH.setValueAt(L_strTEMP2,tblDLSCH.getSelectedRow(),intTB5_PKGTP);
				tblDLSCH.setValueAt(L_STRTK1.nextToken(),tblDLSCH.getSelectedRow(),intTB5_DELQT);
				tblDLSCH.setValueAt("I",tblDLSCH.getSelectedRow(),intTB5_DELTP);
				//tblDLSCH.setValueAt(L_STRTK1.nextToken(),tblDLSCH.getSelectedRow(),intTB5_DELTP);
			/*	tblGRDTL.setValueAt("MT",tblGRDTL.getSelectedRow(),intTB1_UOMCD);
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText(L_strTEMP);
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).requestFocus();
				for(int i=0;i<tblGRDTL.getSelectedRow();i++)
					if(tblGRDTL.getValueAt(i,intTB1_PRDCD).equals(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText()))
					{//AVOIDS DUPLICATION OF GRADE ENTRIES IN AN ORDER
						setMSG("Grade is Already Added ..",'E');
						((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText("");
						tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_PRDDS);
						tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_BASRT);
						return;
					} */
			}
			else if(M_strHLPFLD.equals("txtPKGTP"))
			{
				L_STRTKN.nextToken();
				txtPKGTP.setText(L_STRTKN.nextToken());
				tblGRDTL.setValueAt(new Boolean(true),tblGRDTL.getSelectedRow(),intTB1_CHKFL);				
				//txtPKGTP.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtPKGTP1"))
			{
				L_STRTKN.nextToken();
				((JTextField)tblDLSCH.cmpEDITR[intTB5_PKGTP]).setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtEUSCD"))
			{
				L_STRTKN.nextToken();
				txtEUSCD.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtCOTAX_TAXCD"))
			{
				txtCOTAX_TAXCD.setText(L_STRTKN.nextToken());
				tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),intTB2_TAXDS);
				tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),intTB2_TAXVL);
				tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),intTB2_AMTFL);
				tblCOTAX.setValueAt(L_STRTKN.nextToken(),tblCOTAX.getSelectedRow(),intTB2_PRCSQ);
				tblCOTAX.cmpEDITR[intTB2_TAXDS].setEnabled(true);
				if(!txtCOTAX_TAXCD.getText().equals("STX")&& !txtCOTAX_TAXCD.getText().equals("OTH"))
					{tblCOTAX.cmpEDITR[intTB2_TAXDS].setEnabled(false);}
			}
			else if(M_strHLPFLD.equals("txtGRTAX_TAXCD"))
			{
				txtGRTAX_TAXCD.setText(L_STRTKN.nextToken());
				tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_TAXDS);
				tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_TAXVL);
				tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_AMTFL);
				tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_PRCSQ);
			}
			else if(M_strHLPFLD.equals("txtGRTAX_PRDCD"))
			{
				txtGRTAX_PRDCD.setText(L_STRTKN.nextToken());
				tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_PRDDS);
			}
			else if(M_strHLPFLD.equals("txtFORDSTCD"))
				txtFORDSTCD.setText(L_STRTKN.nextToken());
			else if(M_strHLPFLD.equals("txtFORSRCCD"))
				txtFORSRCCD.setText(L_STRTKN.nextToken());
			else if(M_strHLPFLD.equals("txtFORTRPCD"))
				txtFORTRPCD.setText(L_STRTKN.nextToken());
			else if(M_strHLPFLD.equals("tblGRTAX"))
			{
				//String L_strTEMP=L_STRTKN.nextToken();
				//if(hstTXCAT==null) hstTXCAT=new Hashtable(10,0.2f);
				//((JTextField)tblGRTAX.cmpEDITR[intTB3_TAXCD]).setText(L_strTEMP);
				//tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_TAXDS);
				//tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_TAXVL);
				//tblGRTAX.setValueAt(L_STRTKN.nextToken(),tblGRTAX.getSelectedRow(),intTB3_AMTFL);
			}
			else if(M_strHLPFLD.equals("ORDNO"))
			{
				String L_strTEMP=L_STRTKN.nextToken();
				cmbINDNO.setSelectedItem(L_strTEMP.substring(0,4));
				txtINDNO.setText(L_strTEMP.substring(4));
				txtORDDT.setText(L_STRTKN.nextToken());
				txtBYRNM.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("OCFNO"))
			{
				String L_strTEMP=L_STRTKN.nextToken();
				//cmbINDNO.setSelectedItem(L_strTEMP.substring(0,4));
				txtOCFNO.setText(L_strTEMP);
				//txtORDDT.setText(L_STRTKN.nextToken());
				//txtBYRNM.setText(L_STRTKN.nextToken());
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"in Child.exeHLPOK");
		}
	}

	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtBYRNM.setEnabled(false);
		txtCNSNM.setEnabled(false);
		txtAMDNO.setEnabled(false);
		txtAMDDT.setEnabled(false);
		txtBKGBY.setEnabled(false);
		txtBKGBY.setEnabled(false);
		lblDSRTP.setEnabled(false);
		txtDSRCD.setEnabled(false);
		txtDSTNM.setEnabled(false);
		txtBKGDT.setEnabled(false);
		txtTRPNM.setEnabled(false);
			
		tblGRDTL.cmpEDITR[0].setEnabled(false);
		tblGRDTL.cmpEDITR[intTB1_PRDDS].setEnabled(false);
		tblGRDTL.cmpEDITR[intTB1_UOMCD].setEnabled(false);
		tblGRDTL.cmpEDITR[intTB1_INDPK].setEnabled(false);
	//	tblGRDTL.cmpEDITR[intTB1_TDCVL].setEnabled(false); 
	//	tblGRDTL.cmpEDITR[intTB1_TDCRF].setEnabled(false); 
		tblCOTAX.cmpEDITR[0].setEnabled(false);

		//tblCOTAX.cmpEDITR[intTB2_TAXCD].setEnabled(false);
		tblCOTAX.cmpEDITR[intTB2_TAXDS].setEnabled(true);
		tblCOTAX.cmpEDITR[intTB2_PRCSQ].setEnabled(false); 
		tblGRTAX.cmpEDITR[0].setEnabled(false);
		//tblGRTAX.cmpEDITR[intTB3_TAXDS].setEnabled(false);
	    tblDLSCH.cmpEDITR[intTB5_PRDDS].setEnabled(false);
	    tblDLSCH.cmpEDITR[intTB5_PKGTP].setEnabled(false);
	    tblDLSCH.cmpEDITR[intTB5_SRLNO].setEnabled(false);
	    tblDLSCH.cmpEDITR[intTB5_ORGDT].setEnabled(false);
	    
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==1)
		{
			//txtBKGRM.setEnabled(false);
			//txtAUTRM.setEnabled(false);
		}
		chkINDDSP.setEnabled(true);
		chkREDTAX.setEnabled(true);
		btnPRINT.setEnabled(true);
	}

	
	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		cmbMKTTP.removeFocusListener(this);
		cmbSALTP.removeFocusListener(this);
		super.clrCOMP();
		lblDSRDUE.setText("");
		lblBYRDUE.setText("");
		setDFTSTS();
		if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
		{
			inlTBLEDIT(tblGRDTL);
			inlTBLEDIT(tblCOTAX);
			inlTBLEDIT(tblGRTAX);
			flgREDTAX = false;
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				setDFTSTS1("AUT","0");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtORDDT.setText(cl_dat.M_strLOGDT_pbst);
				txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
				txtREGBY.setText(cl_dat.M_strUSRCD_pbst);
				//System.out.println("getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()) : "+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()));
				//System.out.println("M_strSBSCD : "+M_strSBSCD);
				txtBKGDT.setText("");
				txtBKGBY.setText("");
			} 
		}
		//setCMBDFT(cmbDTPCD,"SYSMRXXDTP",strDFT_DTP);
		//setCMBDFT(cmbPMTCD,"SYSMRXXPMT",strDFT_PMT);
		//setCMBDFT(cmbMOTCD,"SYSMR01MOT",strDFT_MOT);
		if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
		{
			lblDSRTP.setText((getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_STF) ? "G" : "D"));
			lblDSTDT.setText((getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_STF) ? "Cons.Stockist" : "Distributor"));
			if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP))
				{txtCURCD.setText("01");txtECHRT.setText("1");}
		}
		lblSTSDS.setText("");
		chbTSHFL.setSelected(true);
		chbPSHFL.setSelected(true);
		cmbMKTTP.addFocusListener(this);
		cmbSALTP.addFocusListener(this);
	}



	/** Restoring default Key Values after clearing components 
	 * on the entry screen
	 */
	private void clrCOMP_1()
	{
		try
		{
			String L_strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
			String L_strSALTP = getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString());
			String L_strINDNO1 = cmbINDNO.getSelectedItem().toString();
			String L_strINDNO2 = txtINDNO.getText();
			clrCOMP();
			setCMBDFT(cmbMKTTP,"MSTCOXXMKT",L_strMKTTP);
			setCMBDFT(cmbSALTP,"SYSMR00SAL",L_strSALTP);
			setCMBDFT_1(cmbINDNO,L_strINDNO1);
			txtINDNO.setText(L_strINDNO2);
		}
		catch(Exception L_EX) {	setMSG(L_EX,"clrCOMP_1");}
	}
	
/** Set default display status of TextFields & Labels
 */
private void setDFTSTS()
{
	setDFTSTS1("DEX","0");
	setDFTSTS1("EXP","0");
	setDFTSTS1("AUT","0");
	setDFTSTS1("FOR","0");
	setDFTSTS1("L_C","0");
	chbINSFL.setSelected(false);
	if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
	{
		lblDSTDT.setText((getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_STF) ? "Cons.Stockist" : "Distributor"));
		lblDSRTP.setText((getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_STF) ? "G" : "D"));
		if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_DEX))
			setDFTSTS1("DEX","1");
		else if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP))
			setDFTSTS1("EXP","1");
	}
	if(flgAUTRN)
		setDFTSTS1("AUT","1");
	
	if(getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()).equals("03"))//|Credit is selected
		setDFTSTS1("ADV","1");
	else if(getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()).equals("02")) //Against LC is selected
		setDFTSTS1("L_C","1");
	
	if(getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()).equals("02")) //FOR
		setDFTSTS1("FOR","1");
	else if(getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()).equals("04")) //CIF
		chbINSFL.setSelected(true);
	
}


/** Sub-method for setting default status
 */
private void setDFTSTS1(String LP_DFTCT, String LP_DFTST)
{
	if(LP_DFTCT.equals("DEX") && LP_DFTST.equals("0"))
	{
			txtDEFNO.setVisible(false);
			lblDEFNO.setVisible(false);
	}
	else if(LP_DFTCT.equals("DEX") && LP_DFTST.equals("1"))
	{
			txtDEFNO.setVisible(true);
			lblDEFNO.setVisible(true);
	}
	else if(LP_DFTCT.equals("EXP") && LP_DFTST.equals("0"))
	{
			txtCURCD.setVisible(false);
			txtECHRT.setVisible(false);
			lblCURCD.setVisible(false);
	}
	else if(LP_DFTCT.equals("EXP") && LP_DFTST.equals("1"))
	{
			txtCURCD.setVisible(true);
			txtECHRT.setVisible(true);
			lblCURCD.setVisible(true);
			lblOCFNO.setVisible(true);
			txtOCFNO.setVisible(true);
			txtOCFNO.requestFocus();
			txtOCFNO.setEnabled(true);
	}
	else if(LP_DFTCT.equals("AUT") && LP_DFTST.equals("0"))
	{
			rdbAUTH_Y.setVisible(false);
			rdbAUTH_N.setVisible(false);
			rdbAUTH_Y.setSelected(false);
			rdbAUTH_N.setSelected(false);
	}
	else if(LP_DFTCT.equals("AUT") && LP_DFTST.equals("1"))
	{
			rdbAUTH_Y.setVisible(true);
			rdbAUTH_N.setVisible(true);
			rdbAUTH_Y.setSelected(false);
			rdbAUTH_N.setSelected(false);
	}
	else if(LP_DFTCT.equals("FOR") && LP_DFTST.equals("0"))
	{
			txtFORNO.setVisible(false);
			lblFORNO.setVisible(false);
	}
	else if(LP_DFTCT.equals("FOR") && LP_DFTST.equals("1"))
	{
			txtFORNO.setVisible(true);
			lblFORNO.setVisible(true);
	}
	else if(LP_DFTCT.equals("EX-FACTORY") && LP_DFTST.equals("0"))
	{
	        lblTRPCD.setVisible(false);
			txtTRPCD.setVisible(false);
			txtTRPNM.setVisible(false);
	}
	else if(LP_DFTCT.equals("EX-FACTORY") && LP_DFTST.equals("1"))
	{
	        lblTRPCD.setVisible(true);
			txtTRPCD.setVisible(true);
			txtTRPNM.setVisible(true);
	}
	else if(LP_DFTCT.equals("L_C") && LP_DFTST.equals("0"))
	{
			lblLCNO.setVisible(false);
			txtLCNO.setVisible(false);
	}
	else if(LP_DFTCT.equals("L_C") && LP_DFTST.equals("1"))
	{
			lblLCNO.setVisible(true);
			txtLCNO.setVisible(true);
	}
	else if(LP_DFTCT.equals("ADV") && LP_DFTST.equals("0"))
	{
			lblAPDNO.setVisible(false);
			txtAPDNO.setVisible(false);
	}
	else if(LP_DFTCT.equals("ADV") && LP_DFTST.equals("1"))
	{
			lblAPDNO.setVisible(true);
			txtAPDNO.setVisible(true);
	}
}


/** Data validation before saving the record
 */
boolean vldDATA()
	{
		try
		{
			if(cmbSALTP.getItemCount() == 0 || cmbSALTP.getSelectedIndex()==0)			
				return false;
		 	if(tblGRDTL.isEditing())
			{
				if(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedColumn()).toString().length()>0)
				{
				TBLINPVF obj=new TBLINPVF();
				obj.setSource(tblGRDTL);
				if(obj.verify(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedColumn()))
					tblGRDTL.getCellEditor().stopCellEditing();
				else
					return false;
				}
			}
			
			if(tblDLSCH.isEditing())
			{
				if(tblDLSCH.getValueAt(tblDLSCH.getSelectedRow(),tblDLSCH.getSelectedColumn()).toString().length()>0)
				{
				TBLINPVF obj=new TBLINPVF();
				obj.setSource(tblDLSCH);
				if(obj.verify(tblDLSCH.getSelectedRow(),tblDLSCH.getSelectedColumn()))
					tblDLSCH.getCellEditor().stopCellEditing();
				else
					return false;
				}
			}
			if(tblCOTAX.isEditing())
			{
				TBLINPVF obj=new TBLINPVF();
				obj.setSource(tblGRDTL);
				if(obj.verify(tblCOTAX.getSelectedRow(),tblGRDTL.getSelectedColumn()))
					tblCOTAX.getCellEditor().stopCellEditing();
				else
					return false;
			}
			if(((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16"))) 
			{
                // added on 11/10/2006 API SRD
			    //for captive consumption set default delivery schedule
			    setDFDEL(); 
			}
			tblGRDTL.setRowSelectionInterval(0,0);
			tblGRDTL.setColumnSelectionInterval(0,0);
			tblCOTAX.setRowSelectionInterval(0,0);
			tblCOTAX.setColumnSelectionInterval(0,0);
			if(!chkEMPTY_CMB(cmbINDNO,"Enter Order Number ...")) return false;
			//if(!chkEMPTY_TXT(lblDSRTP,"Enter Distributor Type ..."))        return false;
			if(!chkEMPTY_TXT(txtBYRCD,"Enter Buyer ..."))        return false;
			if(!chkEMPTY_TXT(txtCNSCD,"Enter Consignee ..."))    return false;
			if(!chkEMPTY_TXT(txtDSTCD,"Enter Destination ..."))  return false;
        	if(!chkEMPTY_TXT(txtORDDT,"Enter Order Date ..."))   return false;
			if(!chkEXCISE("Excise Duty Not specified properly ..."))   return false;
			if(!chkPARTY("C",txtBYRCD.getText(),"Invalid Buyer Code","Y"))		 return false;
			if(!chkPARTY("C",txtCNSCD.getText(),"Invalid Consignee code","Y"))	 return false;
			if(txtTRPCD.isVisible())
			{
			     if(!((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")))
			     {
				    //validation skipped for captive consumption
                    if(!chkEMPTY_TXT(txtTRPCD,"Enter Transporter ..."))  return false;
			        if(!chkPARTY("T",txtTRPCD.getText(),"Invalid Transporter code","Y"))	 return false;
			     }
			}
		                //if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP) && txtCURCD.equals("01"))     {setMSG("Enter Currnecy",'E'); return false;}
                        //if(!hstCDTRN.containsKey("MSTCOXXCUR"+txtCURCD.getText()))      {setMSG("Enter Currnecy",'E'); return false;}
			if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
				if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_STF))
				{
					if(!chkEMPTY_TXT(txtPAYCT,"Enter Customer Credit Period")) return false;
					if(!chkEMPTY_TXT(txtPAYAC,"Enter Accounts Credit Period")) return false;
					if(!chkZERO_TXT(txtPAYCT,"Enter Customer Credit Period")) return false;
					if(!chkZERO_TXT(txtPAYAC,"Enter Accounts Credit Period")) return false;
				}
			
			if(tblGRDTL.getValueAt(0,intTB1_PRDCD).toString().length()==0)
			{
				setMSG("Enter Grade Details ..",'E');
				tbpMAIN.setSelectedIndex(1);
				tblGRDTL.editCellAt(0,intTB1_PRDCD);
				tblGRDTL.cmpEDITR[intTB1_PRDCD].requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtREGDT.getText()).compareTo(M_fmtLCDAT.parse(txtORDDT.getText()))<0)
			{
				setMSG("Registration Date Cannot be Less than Order Date ..",'E');
				txtREGDT.requestFocus();
				return false;
			}
			
			if(!((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16"))) // check released for captive consumption
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			for(int j=0;j<tblDLSCH.getRowCount()&&tblDLSCH.getValueAt(j,intTB5_PRDCD).toString().length()>0;j++)
			{
			    if(tblDLSCH.getValueAt(j,intTB5_PKGTP).toString().length()==0)
				{
					setMSG("Enter Package Type ..",'E');
					//tbpMAIN.setSelectedIndex(1);
					tblDLSCH.editCellAt(j,intTB5_PKGTP);
					return false;
				}
				if(tblDLSCH.getValueAt(j,intTB5_DELTP).toString().length()==0)
				{
					setMSG("Enter Delivery Type ..",'E');
					//tbpMAIN.setSelectedIndex(1);
					tblDLSCH.editCellAt(j,intTB5_DELTP);
					return false;
				}
				if(tblDLSCH.getValueAt(j,intTB5_DELQT).toString().length()==0)
				{
					setMSG("Enter Delivery Qty ..",'E');
					//tbpMAIN.setSelectedIndex(1);
					tblDLSCH.editCellAt(j,intTB5_DELQT);
					return false;
				}
				if(tblDLSCH.getValueAt(j,intTB5_DELDT).toString().length()==0)
				{
					setMSG("Enter Delivery Date ..",'E');
                  	tblDLSCH.editCellAt(j,intTB5_DELDT);
					return false;
				}
				/*if(M_fmtLCDAT.parse(tblDLSCH.getValueAt(j,intTB5_DELDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
				{
					setMSG("Delivery Date can not be less than current Date..",'E');
                	return false;
				}*/
				if(tblDLSCH.getValueAt(j,intTB5_SRLNO).toString().length()==0)
				{
					setMSG("Serial No. can not be blank, press Enter on Delivery Date ..",'E');
					//tbpMAIN.setSelectedIndex(1);
					tblDLSCH.editCellAt(j,intTB5_DELDT);
					return false;
				}
			}
			for(int i=0;i<tblGRDTL.getRowCount()&&tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length()>0;i++)
			{
				if(tblGRDTL.getValueAt(i,intTB1_PKGTP).toString().length()==0)
				{
					setMSG("Enter Package Type ..",'E');
					tbpMAIN.setSelectedIndex(1);
					tblGRDTL.editCellAt(i,intTB1_PKGTP);
					return false;
				}
				if(tblGRDTL.getValueAt(i,intTB1_REQQT).toString().length()==0 && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
				{
					setMSG("Enter Quantity for "+tblGRDTL.getValueAt(i,intTB1_PRDDS).toString()+" ..",'E');
					tbpMAIN.setSelectedIndex(1);
					tblGRDTL.editCellAt(i,intTB1_REQQT);
					return false;
				}
				if(tblGRDTL.getValueAt(i,intTB1_BASRT).toString().length()==0)
				{
					setMSG("Enter Base Rate for "+tblGRDTL.getValueAt(i,intTB1_PRDDS).toString()+" ..",'E');
					tbpMAIN.setSelectedIndex(1);
					tblGRDTL.editCellAt(i,intTB1_BASRT);
					return false;
				}
				if(tblGRDTL.getValueAt(i,intTB1_PKGTP).toString().length()==0)
				{
					setMSG("Enter Pkg.Type for "+tblGRDTL.getValueAt(i,intTB1_PRDDS).toString()+" ..",'E');
					tbpMAIN.setSelectedIndex(1);
					tblGRDTL.editCellAt(i,intTB1_PKGTP);
					return false;
				}
				/*if(tblGRDTL.getValueAt(i,intTB1_DELTP).toString().length()==0)
				{
					setMSG("Enter Delivery Type as I or S (I: Immediate S: Schedule) ..",'E');
					tbpMAIN.setSelectedIndex(1);
					tblGRDTL.editCellAt(i,intTB1_DELTP);
					return false;
				}*/
				if(tblGRDTL.getValueAt(i,intTB1_EUSCD).toString().length()==0)
				{
					setMSG("Enter Sector for "+tblGRDTL.getValueAt(i,intTB1_PRDDS).toString()+" ..",'E');
					tbpMAIN.setSelectedIndex(1);
					tblGRDTL.editCellAt(i,intTB1_EUSCD);
					return false;
				}
			//	if(!chkPARTY("C",tblGRDTL.getValueAt(i,intTB1_TDCRF).toString(),"Invalid Third Party code ...","N"))							 return false;
				// Check delivery Schedule here
                if(!((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16"))) // released for captive consumption
               if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
                if(getTOTQT(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString(),tblGRDTL.getValueAt(i,intTB1_PKGTP).toString()) != Double.parseDouble(tblGRDTL.getValueAt(i,intTB1_REQQT).toString()))			    	
                {
                    //System.out.println("1 "+getTOTQT(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString(),tblGRDTL.getValueAt(i,intTB1_PKGTP).toString()));
                    setMSG("Quantity mismatch in Delivery Schedule.., Check for "+tblGRDTL.getValueAt(i,intTB1_PRDDS).toString(),'E'); 
                    return false;
                }
              }
		}catch (Exception e)
		{
			setMSG(e,"vldDATA");
			return false;
		}
		return true;
	}

	

	

	void setDFDEL()
	{
	    int j=0;
        for (int i=0;i<tblGRDTL.getRowCount();i++)
        {
            if(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length() ==10)
            {
                tblDLSCH.setValueAt(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString(),j,intTB5_PRDCD);
                tblDLSCH.setValueAt(new Boolean(true),j,intTB5_CHKFL);
                tblDLSCH.setValueAt("I",j,intTB5_DELTP);
                tblDLSCH.setValueAt(tblGRDTL.getValueAt(i,intTB1_PKGTP).toString(),j,intTB5_PKGTP);
                tblDLSCH.setValueAt(tblGRDTL.getValueAt(i,intTB1_REQQT).toString(),j,intTB5_DELQT);
                tblDLSCH.setValueAt(cl_dat.M_strLOGDT_pbst,j,intTB5_DELDT);
                if((""+(j+1)).length() == 1)
                    tblDLSCH.setValueAt("0"+(j+1),j,intTB5_SRLNO);
                else
                    tblDLSCH.setValueAt(""+(j+1),j,intTB5_SRLNO);
                j++;
            }   
        }
	}
/** Validating party code
 */
private boolean chkPARTY(String LP_PRTTP, String LP_PRTCD, String LP_MSGDS, String LP_CMPFL)
{
	boolean L_RETFL = false;
	try
	{
	    if(LP_PRTCD.length()!=5)
	        return false;
	if(LP_PRTCD.length()>0)
	{
		//String L_strDSRCHK = (LP_PRTTP.equalsIgnoreCase("C") ? " and PT_DSRCD = '"+txtDSRCD.getText()+"'" : "");
		ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prttp = '"+LP_PRTTP+"' and pt_prtcd = '"+LP_PRTCD+"' and upper(PT_STSFL)<>'X' ");
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


/** Verifying Excise Duty Entries at Customer Order Level
 */

private boolean chkEXCISE(String LP_MSGDS)
{
	boolean L_RETFL = true;
	boolean L_OCC01 = false;
	boolean L_OCC02 = false;
	String L_strERRMSG = "";
	try
	{
		for(int i=0;i<tblCOTAX.getRowCount();i++)
		{
			if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().length() != 3)
				break;
			if(strMKTTP.equals("01") || strMKTTP.equals("04") || strMKTTP.equals("05") || strMKTTP.equals("07")) // Finished Goods Sale (Polystyrene)
			{
				if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EXC") && Double.parseDouble(tblCOTAX.getValueAt(i,intTB2_TAXVL).toString())>0)
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("12") || getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")) {L_strERRMSG = "Excise not applicable for Export & Captive Consumption category";  L_RETFL = false;}
						else {if (L_OCC01 == true)  L_OCC02 = true;  else  L_OCC01 = true;}
				}
				else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX1") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("03") && !getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("01")) {L_strERRMSG = "EX1 Excise Code is applicable only for Domestic & Deemed Export"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true; else L_OCC01 = true;}
				}
				else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX2") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("12") && !getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("03")) {L_strERRMSG = "EX2 Excise Code is applicable only for Export"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true;  else  L_OCC01 = true;}
				}
				else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX3") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("12")) {L_strERRMSG = "EX3 Excise Code is applicable only for Export"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true;  else  L_OCC01 = true;}
				}
				else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX4") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("12")) {L_strERRMSG = "EX4 Excise Code is applicable only for Export"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true;  else  L_OCC01 = true;}
				}
				else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX7") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("03") && !getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("01")) {L_strERRMSG = "EX7 Excise Code is applicable only for Domestic & Deemed Export Sale"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true; else L_OCC01 = true;}
				}
				else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX8") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("03") && !getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("01")) {L_strERRMSG = "EX8 Excise Code is applicable only for Domestic & Deemed Export Sale"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true; else L_OCC01 = true;}
				}
			}
			else if(strMKTTP.equals("03")) // Captive Consumption
			{
				if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX5") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")) {L_strERRMSG = "EX5 Excise Code is applicable only for Captive Consumption"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true;  else  L_OCC01 = true;}
				}
			}
			else if(strMKTTP.equals("02") || strMKTTP.equals("12")) // R.M.Sale
			{
				if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("CVD") && Double.parseDouble(tblCOTAX.getValueAt(i,intTB2_TAXVL).toString())>0)
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("21")) {L_strERRMSG = "CVD  category is applicable only for R.M. sale"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true;  else  L_OCC01 = true;}
				}
				if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("HIC") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("21")) {L_strERRMSG = "Hi-Seas category is applicable only for R.M. sale"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true;  else  L_OCC01 = true;}
				}
				if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX6") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("21")) {L_strERRMSG = "At Actuals category is applicable only for R.M. sale"; L_RETFL = false;}
						else {if (L_OCC01 == true) L_OCC02 = true;  else  L_OCC01 = true;}
				}
			}
		}
		if(L_RETFL == false)   // mistake in defining excise duty
			{JOptionPane.showMessageDialog(this,L_strERRMSG);}
		//if(L_OCC01 == false)   // Excise Duty not defined
		//	{JOptionPane.showMessageDialog(this,"Excise Duty Not Defined"); L_RETFL = false;}
		if(L_OCC02==true)      // Excise duty defined twice
			{JOptionPane.showMessageDialog(this,"Excise Duty Defined more than once"); L_RETFL = false;}
	}
	catch (Exception L_EX)
	{setMSG("Error in chkEXCISE : "+L_EX,'E');}
	return L_RETFL;
}




/**
 * Skipping validation for Tax Description related Excise Code Entry
 * When Amount flag is not defined as 'X'
 * (Amount / Percent flag is predefined as 'X'  for descriptive Tax Codes)
 */
private boolean vldEXCISE(int LP_ROWNO)
{
	String L_strTAXCD = tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString();
	try
	{
		if(L_strTAXCD.equals("EX1") || L_strTAXCD.equals("EX2") || L_strTAXCD.equals("EX3") || L_strTAXCD.equals("EX4") || L_strTAXCD.equals("EX5"))
			if(tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString().equals("X"))
				return false;
		else 
			return true;
	}
	catch (Exception L_EX)
	{setMSG("Error in vldEXCISE : "+L_EX,'E');}
	return true;
}




/** Verifying text field entry status
 */

private boolean chkEMPTY_TXT(JTextField LP_TXTNM, String LP_MSGDS)
{
	if(LP_TXTNM.getText().length()==0)
	{
		setMSG(LP_MSGDS,'E');
		LP_TXTNM.requestFocus();
		return false;
	}
	return true;
}
/** Verifying text field entry status for zero value
 */

private boolean chkZERO_TXT(JTextField LP_TXTNM, String LP_MSGDS)
{
	try
	{
		if(LP_TXTNM.getText().length()>0)
		{
			if(Double.parseDouble(LP_TXTNM.getText())<=0)
			{
				setMSG(LP_MSGDS,'E');
				LP_TXTNM.requestFocus();
				return false;
			}
		}
	}
	catch (Exception L_EX)
	{setMSG("Error in chkZERO_TXT : "+L_EX,'E'); return false;}
	return true;
}

/** Saving record in remark master
 */
private void saveRMMST(String LP_DOCTP, String LP_REMDS)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(!(txtREGRM.getText().length()>0))
			return;
		
		strWHRSTR = " RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP = '"+strMKTTP+"' and "
					+ "RM_TRNTP = '"+LP_DOCTP+"' and "
					+ "RM_DOCNO = '"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
		flgCHK_EXIST =  chkEXIST("MR_RMMST", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in MR_RMMST");
				return;
		}
		
		if(!flgCHK_EXIST)
		{
		
			M_strSQLQRY="Insert into MR_RMMST (RM_CMPCD,RM_MKTTP,RM_TRNTP,RM_DOCNO,RM_REMDS,RM_SBSCD,RM_SBSCD1,RM_TRNFL,RM_LUSBY,RM_LUPDT,RM_STSFL) values ("
			+setINSSTR("RM_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("RM_MKTTP",strMKTTP,"C")
			+setINSSTR("RM_TRNTP",LP_DOCTP,"C")
			+setINSSTR("RM_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("RM_REMDS",delQuote(LP_REMDS),"C")
			+setINSSTR("RM_SBSCD",M_strSBSCD,"C")
			//+setINSSTR("RM_SBSCD1",(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_DEX) ? strZONCD_EXP : M_strSBSCD.substring(0,2))+"XX00","C")
			+setINSSTR("RM_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("RM_TRNFL","0","C")
			+setINSSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("RM_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')";   //+setUPDSTR("IN_STSFL",strIN_STSFL,"C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update  MR_RMMST set "
			+setUPDSTR("RM_MKTTP",strMKTTP,"C")
			+setUPDSTR("RM_TRNTP",LP_DOCTP,"C")
			+setUPDSTR("RM_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setUPDSTR("RM_REMDS",delQuote(LP_REMDS),"C")
			+setUPDSTR("RM_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("RM_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setUPDSTR("RM_TRNFL","0","C")
			+setUPDSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("RM_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"RM_STSFL = '0' where " + strWHRSTR;  //+setUPDSTR("IN_STSFL",strIN_STSFL,"C")
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveRMMST : "+L_EX,'E');}
}



/** Checking key in table for record existance
 */
private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
{
	boolean L_flgCHKFL = false;
	try
	{
		M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
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

/** Copying current record into amendment tables
 */
private void saveAMND()
{
	try
	{
		if(!flgAMDFL)
			return;
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		// CODE TO TRANSFER DATA TO AMMENDMENT TABLE.

			strWHRSTR =  "IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP = '" +strMKTTP+"' and "
						 +"IN_INDNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
			cl_dat.exeSQLUPD("Insert into MR_INMAM Select * from MR_INMST where "+strWHRSTR ,"");

			strWHRSTR =  "INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP = '" +strMKTTP+"' and "
						 +"INT_INDNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
			cl_dat.exeSQLUPD("Insert into MR_INTAM Select * from MR_INTRN where "+strWHRSTR ,"");
			strWHRSTR =  "IND_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IND_MKTTP = '" +strMKTTP+"' and "
						 +"IND_INDNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
			cl_dat.exeSQLUPD("Insert into MR_INDAM Select * from MR_INDEL where "+strWHRSTR ,"");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveAMND : "+L_EX,'E');}
}

private void saveINDEL(int LP_ROWNO)
{
   strWHRSTR =  "IND_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IND_MKTTP = '" +strMKTTP+"' and "
					 +"IND_INDNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"' and "
					 +"IND_PRDCD = '" +tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString()+"' and "
					 +"IND_PKGTP = '" +getCODCD("SYSFGXXPKG"+tblDLSCH.getValueAt(LP_ROWNO,intTB5_PKGTP).toString())+"' AND "
					 +"IND_SRLNO = '" +tblDLSCH.getValueAt(LP_ROWNO,intTB5_SRLNO).toString()+"'";
	flgCHK_EXIST =  chkEXIST("MR_INDEL", strWHRSTR);
	if(!flgCHK_EXIST)
	{	
        M_strSQLQRY="insert into MR_INDEL(IND_CMPCD,IND_MKTTP,IND_INDNO,IND_PRDCD,IND_PKGTP,IND_SRLNO,IND_AMDNO,IND_DSPDT,IND_INDQT,IND_DELTP,IND_TRNFL,IND_STSFL,IND_LUSBY,IND_LUPDT,IND_SBSCD,IND_SBSCD1) values ("
    	+setINSSTR("IND_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
		+setINSSTR("IND_MKTTP",strMKTTP,"C")
    	+setINSSTR("IND_INDNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
    	+setINSSTR("IND_PRDCD",tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString(),"C")
    	+setINSSTR("IND_PKGTP",getCODCD("SYSFGXXPKG"+tblDLSCH.getValueAt(LP_ROWNO,intTB5_PKGTP).toString()),"C")
       	+setINSSTR("IND_SRLNO",tblDLSCH.getValueAt(LP_ROWNO,intTB5_SRLNO).toString(),"C")
       	+setINSSTR("IND_AMDNO","00","C")
    	+setINSSTR("IND_DSPDT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELDT).toString(),"D")
    	+setINSSTR("IND_INDQT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"N")
    	+setINSSTR("IND_DELTP",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELTP).toString(),"C")
    	+setINSSTR("IND_TRNFL","0","C")
    	+setINSSTR("IND_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (Float.parseFloat(nvlSTRVL(tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"0.00"))>0.00 ? (rdbAUTH_Y.isSelected() ? "1" : "0") : "X"),"C")
    	+setINSSTR("IND_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
    	+setINSSTR("IND_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
        +"'"+M_strSBSCD+"'"+","
        +"'"+M_strSBSCD.substring(0,2)+getPRDTP(tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString())+"00')";
	}
	else
	{
	    M_strSQLQRY="UPDATE MR_INDEL SET "
    //	+setUPDSTR("IND_MKTTP",strMKTTP,"C")
    //	+setINSSTR("IND_INDNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
    //	+setINSSTR("IND_PRDCD",tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString(),"C")
    //	+setINSSTR("IND_PKGTP",getCODCD("SYSFGXXPKG"+tblDLSCH.getValueAt(LP_ROWNO,intTB5_PKGTP).toString()),"C")
      // 	+setINSSTR("IND_SRLNO",tblDLSCH.getValueAt(LP_ROWNO,intTB5_SRLNO).toString(),"C")
    	+setUPDSTR("IND_AMDNO",txtAMDNO.getText(),"C")
    	+setUPDSTR("IND_DSPDT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELDT).toString(),"D")
    	+setUPDSTR("IND_INDQT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"N")
    	+setUPDSTR("IND_DELTP",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELTP).toString(),"C")
    	+setUPDSTR("IND_TRNFL","0","C")
    	+setUPDSTR("IND_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (Float.parseFloat(nvlSTRVL(tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"0.00"))>0.00 ? (rdbAUTH_Y.isSelected() ? "1" : "0") : "X"),"C")
    	+setUPDSTR("IND_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
    	+"IND_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"'"
        + " WHERE "+strWHRSTR;
	}
	
	System.out.println(M_strSQLQRY);
	cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
}

/** Saving indent master
 */
private void saveINMST()
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			if(exeDSPCHK("INV_DOR_MSG"))
				{cl_dat.M_flgLCUPD_pbst=false; return;}
		saveAMND();		
		if(!cl_dat.M_flgLCUPD_pbst)
			return;

		strWHRSTR =  "IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP = '" +strMKTTP+"' and "
					 +"IN_INDNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
		flgCHK_EXIST =  chkEXIST("MR_INMST", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in MR_INMST");
				cl_dat.M_flgLCUPD_pbst=false;
				return;
		}
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="insert into MR_INMST (IN_CMPCD,IN_MKTTP,IN_INDNO,IN_INDDT,IN_AMDNO,IN_AMDDT,IN_SALTP,IN_DTPCD,IN_PORNO,IN_PORDT,IN_ZONCD,IN_CNSCD,IN_CNSNM,IN_BYRCD,IN_BYRNM,IN_CURCD,IN_ECHRT,IN_DSRTP,IN_DSRCD,IN_DSRNM,IN_APTVL,IN_CPTVL,IN_PMTCD,IN_TRNFL,IN_STSFL,IN_LUSBY,IN_LUPDT,IN_PSHFL,IN_SBSCD,IN_SBSCD1,IN_DSTCD,IN_MOTCD,IN_FILRF,IN_PMTRF,IN_INSRF,IN_FORRF,IN_TSHFL,IN_REGBY,IN_REGDT,IN_BKGBY,IN_BKGDT,IN_AUTDT,IN_AUTTM,IN_DTPDS,IN_STXCD,IN_STXRT,IN_OCTCD,IN_OCTRT,IN_SVCCD,IN_TRPCD,IN_TRPNM,IN_CFTAG,IN_DELAD,IN_OCFNO,IN_SVCRT ) values ("
			+setINSSTR("IN_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("IN_MKTTP",strMKTTP,"C")
			+setINSSTR("IN_INDNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("IN_INDDT",txtORDDT.getText(),"D")
			+setINSSTR("IN_AMDNO","00","C")
			+setINSSTR("IN_AMDDT"," ","D")
						+setINSSTR("IN_SALTP",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()) : "","C")
			+setINSSTR("IN_DTPCD",getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()),"C")
			+setINSSTR("IN_PORNO",txtCRFNO.getText(),"C")
			+setINSSTR("IN_PORDT",txtCRFDT.getText(),"D")
			+setINSSTR("IN_ZONCD",M_strSBSCD.substring(0,2),"C")
			+setINSSTR("IN_CNSCD",txtCNSCD.getText(),"C")
			+setINSSTR("IN_CNSNM",txtCNSNM.getText(),"C")
			+setINSSTR("IN_BYRCD",txtBYRCD.getText(),"C")
			+setINSSTR("IN_BYRNM",txtBYRNM.getText(),"C")
						+setINSSTR("IN_CURCD",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? (!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP)&&txtCURCD.getText().length()==0) ? "01" : txtCURCD.getText() : "","C")
						+setINSSTR("IN_ECHRT",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ?(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP)&&txtECHRT.getText().length()==0) ? "1.00" : txtECHRT.getText():"","N")
			+setINSSTR("IN_DSRTP",lblDSRTP.getText(),"C")
			+setINSSTR("IN_DSRCD",txtDSRCD.getText(),"C")
		//	+setINSSTR("IN_DSRNM",txtDSRNM.getText(),"C")
			+setINSSTR("IN_DSRNM",txtSHRNM.getText(),"C")
			+setINSSTR("IN_APTVL",txtPAYAC.getText(),"N")
			+setINSSTR("IN_CPTVL",txtPAYCT.getText(),"N")
			+setINSSTR("IN_PMTCD",getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()),"C")
			+setINSSTR("IN_TRNFL","0","C")
			+setINSSTR("IN_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setINSSTR("IN_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("IN_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setINSSTR("IN_PSHFL",(chbPSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setINSSTR("IN_SBSCD",M_strSBSCD,"C")
			+setINSSTR("IN_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("IN_DSTCD",txtDSTCD.getText(),"C")
			+setINSSTR("IN_MOTCD",getCODCD("SYSMR01MOT"+cmbMOTCD.getSelectedItem().toString()),"C")
			+setINSSTR("IN_FILRF",txtDEFNO.getText(),"C")
			+setINSSTR("IN_PMTRF",txtAPDNO.isVisible() ? txtAPDNO.getText() : (txtLCNO.isVisible() ? txtLCNO.getText() : ""),"C")
			+setINSSTR("IN_INSRF",chbINSFL.isSelected()==true ? "Y" : "N","C")
			+setINSSTR("IN_FORRF",txtFORNO.getText(),"C")
			+setINSSTR("IN_TSHFL",(chbTSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setINSSTR("IN_REGBY",txtREGBY.getText(),"C")
			+setINSSTR("IN_REGDT",txtREGDT.getText(),"D")
			+setINSSTR("IN_BKGBY",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_strUSRCD_pbst : txtBKGBY.getText(),"C")
			+setINSSTR("IN_BKGDT",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_strLOGDT_pbst : txtBKGDT.getText(),"D")
			+setINSSTR("IN_AUTDT",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_strLOGDT_pbst : txtBKGDT.getText(),"D")
			+setINSSTR("IN_AUTTM",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_txtCLKTM_pbst.getText() : "","T")
			+setINSSTR("IN_DTPDS",cmbDTPCD.getSelectedItem().toString(),"C")
			+setINSSTR("IN_STXCD","XX","C")
			+setINSSTR("IN_STXRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "STX"),"N")
			+setINSSTR("IN_OCTCD","XX","C")
			+setINSSTR("IN_OCTRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "OCT"),"N")
			+setINSSTR("IN_SVCCD","XX","C")
			+setINSSTR("IN_TRPCD",txtTRPCD.getText().trim(),"C")
			+setINSSTR("IN_TRPNM",txtTRPNM.getText().trim(),"C")
			+setINSSTR("IN_CFTAG",chbCFMFL.isSelected()?"R":"","C")
			+setINSSTR("IN_DELAD",txtDELAD.getText().trim(),"C")
			+setINSSTR("IN_OCFNO",txtOCFNO.getText().trim(),"C")			
			+getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "SVC")+")";    //setINSSTR("IN_SVCRT","0.00","N")

			//M_strSQLQRY="Update CO_CDTRN set CMT_CCSVL='"+txtINDNO.getText()+"' where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MR"+strMKTTP+"IND' and CMT_CODCD = '"+cmbINDNO.getSelectedItem().toString()+"'";
			//hstINDNO.put("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),new String[]{"","","","","","","",txtINDNO.getText(),""});
			//cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update MR_INMST set "
			+setUPDSTR("IN_INDDT",txtORDDT.getText(),"D")
			+setUPDSTR("IN_AMDNO",txtAMDNO.getText(),"C")
			+setUPDSTR("IN_AMDDT",txtAMDDT.getText(),"D")
						+setUPDSTR("IN_SALTP",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()) : "","C")
			+setUPDSTR("IN_DTPCD",getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()),"C")
			+setUPDSTR("IN_PORNO",txtCRFNO.getText(),"C")
			+setUPDSTR("IN_PORDT",txtCRFDT.getText(),"D")
			+setUPDSTR("IN_ZONCD",M_strSBSCD.substring(0,2),"C")
			+setUPDSTR("IN_CNSCD",txtCNSCD.getText(),"C")
			+setUPDSTR("IN_CNSNM",txtCNSNM.getText(),"C")
			+setUPDSTR("IN_BYRCD",txtBYRCD.getText(),"C")
			+setUPDSTR("IN_BYRNM",txtBYRNM.getText(),"C")
			+setUPDSTR("IN_CURCD",txtCURCD.getText(),"C")
			+setUPDSTR("IN_ECHRT",txtECHRT.getText(),"N")
			+setUPDSTR("IN_DSRTP",lblDSRTP.getText(),"C")
			+setUPDSTR("IN_DSRCD",txtDSRCD.getText(),"C")
			//+setUPDSTR("IN_DSRNM",txtDSRNM.getText(),"C")
			+setUPDSTR("IN_DSRNM",txtSHRNM.getText(),"C")
			+setUPDSTR("IN_APTVL",txtPAYAC.getText(),"N")
			+setUPDSTR("IN_CPTVL",txtPAYCT.getText(),"N")
			+setUPDSTR("IN_PMTCD",getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()),"C")
			+setUPDSTR("IN_TRNFL","0","C")
			+setUPDSTR("IN_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setUPDSTR("IN_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("IN_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setUPDSTR("IN_PSHFL",(chbPSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setUPDSTR("IN_CFTAG",(chbCFMFL.isSelected()==true ? "R" : ""),"C")
			+setUPDSTR("IN_DELAD",txtDELAD.getText(),"C")
			+setUPDSTR("IN_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("IN_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setUPDSTR("IN_DSTCD",txtDSTCD.getText(),"C")
			+setUPDSTR("IN_MOTCD",getCODCD("SYSMR01MOT"+cmbMOTCD.getSelectedItem().toString()),"C")
			+setUPDSTR("IN_FILRF",txtDEFNO.getText(),"C")
			+setUPDSTR("IN_PMTRF",txtAPDNO.isVisible() ? txtAPDNO.getText() : (txtLCNO.isVisible() ? txtLCNO.getText() : ""),"C")
			+setUPDSTR("IN_INSRF",chbINSFL.isSelected()==true ? "Y" : "N","C")
			+setUPDSTR("IN_FORRF",txtFORNO.getText(),"C")
			+setUPDSTR("IN_TSHFL",(chbTSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setUPDSTR("IN_REGBY",txtREGBY.getText(),"C")
			+setUPDSTR("IN_REGDT",txtREGDT.getText(),"D")
			+setUPDSTR("IN_BKGBY",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_strUSRCD_pbst : txtBKGBY.getText(),"C")
			+setUPDSTR("IN_BKGDT",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_strLOGDT_pbst : txtBKGDT.getText(),"D")
			+setUPDSTR("IN_AUTDT",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_strLOGDT_pbst : txtBKGDT.getText(),"D")
			+setUPDSTR("IN_AUTTM",(Integer.parseInt(nvlSTRVL(txtAMDNO.getText(),"0"))==0 && rdbAUTH_Y.isSelected()) ? cl_dat.M_txtCLKTM_pbst.getText() : "","T")
			+setUPDSTR("IN_DTPDS",cmbDTPCD.getSelectedItem().toString(),"C")
			+setUPDSTR("IN_STXCD","XX","C")
			+setUPDSTR("IN_STXRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "STX"),"N")
			+setUPDSTR("IN_OCTCD","XX","C")
			+setUPDSTR("IN_OCTRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "OCT"),"N")
			+setUPDSTR("IN_SVCCD","XX","C")
			+setUPDSTR("IN_TRPCD",txtTRPCD.getText().trim(),"C")
			+setUPDSTR("IN_TRPNM",txtTRPNM.getText().trim(),"C")
			+"IN_SVCRT = "+getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "SVC")    //setUPDSTR("IN_SVCRT","0.00","N")
			+" where "+strWHRSTR;
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		M_strSQLQRY = "Update CO_PTMST SET PT_SPLRF ='"+txtPRTRF.getText().trim() +"'" + " WHERE PT_PRTTP ='C' AND PT_PRTCD ='"+txtBYRCD.getText().trim() +"'";
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		
		M_strSQLQRY="Update CO_CDTRN set CMT_CCSVL='"+txtINDNO.getText()+"' where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MR"+strMKTTP+"IND' and CMT_CODCD = '"+cmbINDNO.getSelectedItem().toString()+"'";
		hstINDNO.put("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),new String[]{"","","","","","","",txtINDNO.getText(),""});
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		if(rdbAUTH_Y.isSelected())
		{
		
			cl_eml ocl_eml = new cl_eml();
			if(strMKTTP.equals("01") || strMKTTP.equals("04") || strMKTTP.equals("05"))
			{
				objRPIND.txtMKTTP.setText(strMKTTP);
				objRPIND.txtINDNO.setText(cmbINDNO.getSelectedItem().toString()+txtINDNO.getText());
				objRPIND.flgOUTPRN = true;
				objRPIND.M_strSBSCD=M_strSBSCD;
				objRPIND.M_strSBSLS=M_strSBSLS;
				objRPIND.M_rdbHTML.setSelected(true);
				objRPIND.exePRINT();
				//ocl_eml.sendfile("cms@spl.co.in",cl_dat.M_strREPSTR_pbst+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+".html","Customer Order Authorisation "+strMKTTP+"/"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"The Customer Order "+strMKTTP+"/"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"  Amd.No.:"+txtAMDNO.getText()+" (for "+txtBYRNM.getText().trim()+") has been authorised by "+cl_dat.M_strUSRCD_pbst);
			}
			if(strMKTTP.equals("03"))
				ocl_eml.sendfile("ptd@spl.co.in",null,"Customer Order Authorisation "+strMKTTP+"/"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"The Customer Order "+strMKTTP+"/"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"  Amd.No.:"+txtAMDNO.getText()+" (for "+txtBYRNM.getText().trim()+") has been authorised by "+cl_dat.M_strUSRCD_pbst);
		}
	}
	catch (Exception L_EX)
	{setMSG("Error in saveINMST : "+L_EX,'E');}
}


/** Checking for D.O.preparation / Despatch status against etire Indent
 */
private boolean exeDSPCHK(String LP_DSPCT)
{
	Enumeration enmGRDKEYS=hstGRDTL.keys();
	boolean L_flgRETFL = false;
	while(enmGRDKEYS.hasMoreElements())
	{
		String L_strGRDCD = (String)enmGRDKEYS.nextElement();
		if(LP_DSPCT.equalsIgnoreCase("INV_DOR_MSG"))
		{
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_DORQT"))>0)
				{JOptionPane.showMessageDialog(this,"D.O. of "+getGRDTL(L_strGRDCD,"GRD_DORQT")+" MT is prepared for "+getPRMST(L_strGRDCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_INVQT"))>0)
				{JOptionPane.showMessageDialog(this,"Inv. of  "+getGRDTL(L_strGRDCD,"GRD_INVQT")+" MT is prepared for "+getPRMST(L_strGRDCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
		}
		else if(LP_DSPCT.equalsIgnoreCase("INV_DOR_NOMSG"))
		{
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_DORQT"))>0)
				L_flgRETFL=true;
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_INVQT"))>0)
				L_flgRETFL=true;
		}
		else if(LP_DSPCT.equalsIgnoreCase("INV_NOMSG"))
		{
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_INVQT"))>0)
				L_flgRETFL=true;
		}
		else
			JOptionPane.showMessageDialog(this,"Invalid Category in exeDSPMSG");
		if (L_flgRETFL)
			break;
	}
	return L_flgRETFL;		
}


/** Checking for D.O.preparation / Despatch status against particular grade
 */
private boolean exeDSPCHK1(int LP_ROWNO,String LP_DSPCT)
{
	boolean L_flgRETFL = false;
	if(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT").length()==0)
		return false;
	if(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT").length()==0)
		return false;
	if(LP_DSPCT.equalsIgnoreCase("INV_DOR_MSG"))
	{
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT"))>0)
				{JOptionPane.showMessageDialog(this,"D.O. of  "+getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT")+" MT is prepared for "+getPRMST(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"PR_PRDDS")); L_flgRETFL=true;}
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT"))>0)
				{JOptionPane.showMessageDialog(this,"Inv. of  "+getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT")+" MT is prepared for "+getPRMST(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"PR_PRDDS")); L_flgRETFL=true;}
	}
	else if(LP_DSPCT.equalsIgnoreCase("INV_DOR_NOMSG"))
	{
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT"))>0)
				L_flgRETFL=true;
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT"))>0)
				L_flgRETFL=true;
	}
	else if(LP_DSPCT.equalsIgnoreCase("INV_NOMSG"))
	{
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT"))>0)
				L_flgRETFL=true;
	}
	else
		JOptionPane.showMessageDialog(this,"Invalid Category in exeDSPMSG");
	return L_flgRETFL;		
}


/** Saving indent transaction
 */
private void saveINTRN(int LP_ROWNO)
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			if(exeDSPCHK1(LP_ROWNO,"INV_DOR_MSG"))
				{cl_dat.M_flgLCUPD_pbst=false; return;}

		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		strWHRSTR =  "INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP = '" +strMKTTP+"' and "
					 +"INT_INDNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"' and "
					 +"INT_PRDCD = '" +tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+"' and "
					 +"INT_PKGTP = '" +getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString())+"'";

		flgCHK_EXIST =  chkEXIST("MR_INTRN",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in MR_INTRN");
				cl_dat.M_flgLCUPD_pbst=false;
				return;
		}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="insert into MR_INTRN (INT_CMPCD,INT_MKTTP,INT_INDNO,INT_OCFNO, INT_PRDCD, INT_PKGTP, INT_INDDT,INT_PRDDS,INT_PRDGR,INT_AMDNO,INT_INDPK,INT_PKGWT,INT_REQQT,INT_CNVFT,INT_ORDUM,INT_STDUM,INT_UOMCD,INT_EUSCD,INT_INDQT,INT_BASRT,INT_RTPVL,INT_TRNFL,INT_STSFL,INT_LUSBY,INT_LUPDT,INT_DORQT,INT_LADQT,INT_INVQT,INT_FCMQT,INT_CFWQT,INT_SBSCD,INT_SBSCD1,INT_SRLNO,INT_CC1VL,INT_CC1RF,INT_CC2VL,INT_CC2RF,INT_CC3VL,INT_EXCRT,INT_DCMVL,INT_CDCVL,INT_TDCVL,INT_DDCVL,INT_PRDRF) values ("
			+setINSSTR("INT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("INT_MKTTP",strMKTTP,"C")
			+setINSSTR("INT_INDNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("INT_OCFNO",txtOCFNO.getText().trim(),"C")			
			+setINSSTR("INT_PRDCD",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"C")
			+setINSSTR("INT_PKGTP",getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"C")
			+setINSSTR("INT_INDDT",txtORDDT.getText(),"D")
			+setINSSTR("INT_PRDDS",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString(),"C")
			+setINSSTR("INT_PRDGR",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString().substring(0,2),"C")
			+setINSSTR("INT_AMDNO",txtAMDNO.getText(),"C")
			+setINSSTR("INT_INDPK",String.valueOf(Float.parseFloat(tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString())/Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN))),"N")
			+setINSSTR("INT_PKGWT",getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN),"N")
			+setINSSTR("INT_REQQT",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "0.00" : tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString(),"N")
			+setINSSTR("INT_CNVFT","1","N")
			+setINSSTR("INT_ORDUM",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setINSSTR("INT_STDUM",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setINSSTR("INT_UOMCD",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setINSSTR("INT_EUSCD",getCODCD("SYSMR00EUS"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_EUSCD).toString()),"C")
			+setINSSTR("INT_INDQT",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "0.00" : (rdbAUTH_Y.isSelected() ? tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString() : "0"),"N")		
			+setINSSTR("INT_BASRT",tblGRDTL.getValueAt(LP_ROWNO,intTB1_BASRT).toString(),"N")
			+setINSSTR("INT_RTPVL","1","N")
			+setINSSTR("INT_TRNFL","0","C")
			+setINSSTR("INT_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setINSSTR("INT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("INT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setINSSTR("INT_DORQT","0","N")		
			+setINSSTR("INT_LADQT","0","N")
			+setINSSTR("INT_INVQT","0","N")
			+setINSSTR("INT_FCMQT","0","N")
			+setINSSTR("INT_CFWQT","0","N")
			+setINSSTR("INT_SBSCD",M_strSBSCD,"C")
						+setINSSTR("INT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString())+"00","C")
			+setINSSTR("INT_SRLNO",LP_ROWNO <10 ? "0"+Integer.toString(LP_ROWNO) : Integer.toString(LP_ROWNO),"C")
			+setINSSTR("INT_CC1VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
			+setINSSTR("INT_CC1RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString().length()>0 ? "C"+txtBYRCD.getText() : " ","C")
			+setINSSTR("INT_CC2VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"N")
			+setINSSTR("INT_CC2RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString().length()>0 ? lblDSRTP.getText()+txtDSRCD.getText():  " ","C")
			+setINSSTR("INT_CC3VL","0","N")
		//	+setINSSTR("INT_CC3RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString().length()>0 ? "C"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCRF).toString() : "  ","C")
			+setINSSTR("INT_EXCRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "EXC"),"N")
			+setINSSTR("INT_DCMVL",getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_NCSVL", hstCDTRN),"N")
			+setINSSTR("INT_CDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
            +setINSSTR("INT_TDCVL","0","N")
			+setINSSTR("INT_CDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"N")
			+"'"+nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDRF).toString(),"")+"')";
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update MR_INTRN set "
			+setUPDSTR("INT_INDDT",txtORDDT.getText(),"D")
			+setUPDSTR("INT_PRDDS",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString(),"C")
			+setUPDSTR("INT_PRDGR",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString().substring(0,2),"C")
			+setUPDSTR("INT_PRDRF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDRF).toString(),"C")
			+setUPDSTR("INT_AMDNO",txtAMDNO.getText(),"C")
			+setUPDSTR("INT_INDPK",String.valueOf(Float.parseFloat(tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString())/Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN))),"N")
			+setUPDSTR("INT_PKGWT",getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN),"N")
			+setUPDSTR("INT_REQQT",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "0.00" : tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString(),"N")
			+setUPDSTR("INT_CNVFT","1","N")
			+setUPDSTR("INT_ORDUM",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setUPDSTR("INT_STDUM",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setUPDSTR("INT_UOMCD",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setUPDSTR("INT_EUSCD",getCODCD("SYSMR00EUS"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_EUSCD).toString()),"C")
			+setUPDSTR("INT_INDQT",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "0.00" : (rdbAUTH_Y.isSelected() ? tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString() : "0"),"N")		
			+setUPDSTR("INT_BASRT",tblGRDTL.getValueAt(LP_ROWNO,intTB1_BASRT).toString(),"N")
			+setUPDSTR("INT_RTPVL","1","N")
			+setUPDSTR("INT_TRNFL","0","C")
			+setUPDSTR("INT_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (Float.parseFloat(nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString(),"0.00"))>0.00 ? (rdbAUTH_Y.isSelected() ? "1" : "0") : "X"),"C")
			+setUPDSTR("INT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("INT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setUPDSTR("INT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("INT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString())+"00","C")
			+setUPDSTR("INT_SRLNO",LP_ROWNO <10 ? "0"+Integer.toString(LP_ROWNO) : Integer.toString(LP_ROWNO),"C")
			+setUPDSTR("INT_CC1VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
			+setUPDSTR("INT_CC1RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString().length()>0 ? "C"+txtBYRCD.getText() : " ","C")
			+setUPDSTR("INT_CC2VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"N")
			+setUPDSTR("INT_CC2RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString().length()>0 ? lblDSRTP.getText()+txtDSRCD.getText():  " ","C")
		//	+setUPDSTR("INT_CC3VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"N")
		//	+setUPDSTR("INT_CC3RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString().length()>0 ? "C"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCRF).toString() : "  ","C")
			+setUPDSTR("INT_EXCRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "EXC"),"N")
			+setUPDSTR("INT_DCMVL",getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_NCSVL", hstCDTRN),"N")
			+setUPDSTR("INT_CDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
			+"INT_DDCVL = "+nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"0")
		//	+"INT_TDCVL = "+nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"0") //+setUPDSTR("INT_TDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"N")
			+" where "+ strWHRSTR;
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveINTRN : "+L_EX,'E');}
}


/** Saving record in CO_TXDOC,  Indent level
 */
private void saveTXDOC_IND()
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		strWHRSTR =   "TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_SYSCD =     'MR'  and "
					 +"TX_SBSTP = '" +strMKTTP+"' and "
					 +"TX_DOCTP =     'IND'  and "
					 +"TX_DOCNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+ "' and "
					 +"TX_PRDCD = 'XXXXXXXXXX'";
		flgCHK_EXIST =  chkEXIST("CO_TXDOC",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXDOC");
				return;
		}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_SBSCD,TX_SBSCD1,TX_TRNTP,"
			+getTXDOC_IND0()
			+"TX_TRNFL,TX_LUSBY,TX_LUPDT,TX_STSFL) values("
			+setINSSTR("TX_CMPCD",cl_dat.M_strCMPCD_pbst,"C")			
			+setINSSTR("TX_SYSCD","MR","C")
			+setINSSTR("TX_SBSTP",strMKTTP,"C")
			+setINSSTR("TX_DOCTP","IND","C")
			+setINSSTR("TX_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("TX_PRDCD","XXXXXXXXXX","C")
			+setINSSTR("TX_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TX_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("TX_TRNTP","M","C")
			+getTXDOC_IND1("INS")
			+setINSSTR("TX_TRNFL","0","C")
			+setINSSTR("TX_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TX_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')";  // setINSSTR("TX_STSFL","0","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXDOC set "
			+setUPDSTR("TX_TRNTP","M","C")
			+setUPDSTR("TX_TRNFL","0","C")
			+getTXDOC_IND1("UPD")
			+setUPDSTR("TX_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TX_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"TX_STSFL = '0' where "+strWHRSTR;  // setUPDSTR("TX_STSFL","0","C")
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXDOC_IND : "+L_EX,'E');}
}



/**
 * Building up field specification part for Insert statement through iteration
 * Example  Insert into .... ( ......  TX_DSBVL,TX_DSBFL,EXCVL,EXCFL, .....) .....
 */
private String getTXDOC_IND0()
{
	String L_strTXDOC_IND0 = "";
	for(int i=0;i<tblCOTAX.getRowCount();i++)
	{
		if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().length()!=3)  // tax code not available
			continue;
		if(!getCDTRN("SYSCOXXTAX"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  //does not belong to Common Tax Category
			continue;
		L_strTXDOC_IND0 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL","TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL","N");
		L_strTXDOC_IND0 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL","TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL","N");
		if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("STX"))
			L_strTXDOC_IND0 += setINSSTR("TX_STXDS","TX_STXDS","N");
		else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("OTH"))
			L_strTXDOC_IND0 += setINSSTR("TX_OTHDS","TX_OTHDS","N");
	}
    return L_strTXDOC_IND0;
}


/** Builds up value part of insert/update query  for Tax Code related fields
 * Example : insert into CO_TXDOC (............) values (.....120,'A',16,'P',.....)
 *           update CO_TXDOC set ...... TX_DSBVL=120,TX_DSBFL='A',TX_EXCVL=16,TX_EXCFL='A',.....
 */
private String getTXDOC_IND1(String LP_SAVETP)
{
	String L_strTXDOC_IND1 = "";
	for(int i=0;i<tblCOTAX.getRowCount();i++)
	{
		if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().length()!=3)  // tax code not available
			continue;
		if(!getCDTRN("SYSCOXXTAX"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  //does not belong to Common Tax Category
			continue;
		if(LP_SAVETP.equalsIgnoreCase("INS"))
		{
			L_strTXDOC_IND1 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL",tblCOTAX.getValueAt(i,intTB2_TAXVL).toString(),"N");
			L_strTXDOC_IND1 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL",tblCOTAX.getValueAt(i,intTB2_AMTFL).toString(),"C");
			if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("STX"))
				L_strTXDOC_IND1 += setINSSTR("TX_STXDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
			else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("OTH"))
				L_strTXDOC_IND1 += setINSSTR("TX_OTHDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
		}
		else if(LP_SAVETP.equalsIgnoreCase("UPD"))
		{
			L_strTXDOC_IND1 += setUPDSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL",tblCOTAX.getValueAt(i,intTB2_TAXVL).toString(),"N");
			L_strTXDOC_IND1 += setUPDSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL",tblCOTAX.getValueAt(i,intTB2_AMTFL).toString(),"C");
			if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("STX"))
				L_strTXDOC_IND1 += setUPDSTR("TX_STXDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
			else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("OTH"))
				L_strTXDOC_IND1 += setUPDSTR("TX_OTHDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
		}
	}
    return L_strTXDOC_IND1;
}




/**  Saving record in CO_TXSPC,   Indent level
 */
private void saveTXSPC_IND0(int LP_ROWNO)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString().length()!=3)
			return;
		if(!getCDTRN("SYSCOXXTAX"+tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("02"))
		   return;
		strWHRSTR =   "TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TXT_SYSCD =     'MR'  and "
					 +"TXT_SBSTP = '" +strMKTTP+"' and "
					 +"TXT_DOCTP =     'IND'  and "
					 +"TXT_DOCNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+ "' and "
					 +"TXT_PRDCD = '" +"XXXXXXXXXX"+      "' and "
					 +"TXT_CODCD = '" +tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString()+ "'";

		
		flgCHK_EXIST =  chkEXIST("CO_TXSPC",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXSPC");
				return;
		}
		
		if(!vldEXCISE(LP_ROWNO))
			return;
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,TXT_SBSCD1,TXT_TRNFL,TXT_LUSBY,TXT_LUPDT,TXT_STSFL) values ("
			+setINSSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TXT_SYSCD","MR","C")
			+setINSSTR("TXT_SBSTP",strMKTTP,"C")
			+setINSSTR("TXT_DOCTP","IND","C")
			+setINSSTR("TXT_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("TXT_PRDCD","XXXXXXXXXX","C")
			+setINSSTR("TXT_CODCD",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString(),"C")
			//+setINSSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString().substring(0,30),"C")
			+setINSSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString(),"C")
			+setINSSTR("TXT_CODVL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXVL).toString(),"N")
			+setINSSTR("TXT_CODFL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_AMTFL).toString(),"C")
			+setINSSTR("TXT_PRCSQ",tblCOTAX.getValueAt(LP_ROWNO,intTB2_PRCSQ).toString(),"C")
			+setINSSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TXT_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("TXT_TRNFL","0","C")
			+setINSSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')"; //setINSSTR("TXT_STSFL","XYZ","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXSPC set "
			//+setUPDSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString().substring(0,30),"C")
			+setUPDSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString(),"C")
			+setUPDSTR("TXT_CODVL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXVL).toString(),"N")
			+setUPDSTR("TXT_CODFL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_AMTFL).toString(),"C")
			+setUPDSTR("TXT_PRCSQ",tblCOTAX.getValueAt(LP_ROWNO,intTB2_PRCSQ).toString(),"C")
			+setUPDSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("TXT_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setUPDSTR("TXT_TRNFL","0","C")
			+setUPDSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"TXT_STSFL = '0' where "+strWHRSTR; //setUPDSTR("TXT_STSFL","XYZ","C")
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXSPC_IND0 : "+L_EX,'E');}
}





/**  Saving record in CO_TXSPC,   Indent level
 */
private void saveTXSPC_IND1(int LP_ROWNO)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString().length()!=3)
			return;
		if(!getCDTRN("SYSCOXXTAX"+tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("02"))
		   return;
		strWHRSTR =   "TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TXT_SYSCD =     'MR'  and "
					 +"TXT_SBSTP = '" +strMKTTP+"' and "
					 +"TXT_DOCTP =     'IND'  and "
					 +"TXT_DOCNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+ "' and "
					 +"TXT_PRDCD = '" +"XXXXXXXXXX"+      "' and "
					 +"TXT_CODCD = '" +tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString()+ "'";

		flgCHK_EXIST =  chkEXIST("CO_TXSPC",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXSPC");
				return;
		}
		
		if(!vldEXCISE(LP_ROWNO))
			return;
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,TXT_SBSCD1,TXT_TRNFL,TXT_LUSBY,TXT_LUPDT,TXT_STSFL) values ("
			+setINSSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TXT_SYSCD","MR","C")
			+setINSSTR("TXT_SBSTP",strMKTTP,"C")
			+setINSSTR("TXT_DOCTP","IND","C")
			+setINSSTR("TXT_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("TXT_PRDCD","XXXXXXXXXX","C")
			+setINSSTR("TXT_CODCD",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString(),"C")
			+setINSSTR("TXT_CODDS",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXDS).toString().substring(0,30),"C")
			+setINSSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setINSSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setINSSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setINSSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TXT_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("TXT_TRNFL","0","C")
			+setINSSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')"; //setINSSTR("TXT_STSFL","XYZ","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXSPC set "
			+setUPDSTR("TXT_CODDS",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXDS).toString().substring(0,30),"C")
			+setUPDSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setUPDSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setUPDSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setUPDSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("TXT_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setUPDSTR("TXT_TRNFL","0","C")
			+setUPDSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"TXT_STSFL = '0' where "+strWHRSTR; //setUPDSTR("TXT_STSFL","XYZ","C")
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXSPC_IND1 : "+L_EX,'E');}
}




/** Saving record in CO_TXDOC, grade level
 */
private void saveTXDOC_PRD(String LP_PRDCD)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		strWHRSTR =   "TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_SYSCD =     'MR'  and "
					 +"TX_SBSTP = '" +strMKTTP+"' and "
					 +"TX_DOCTP = 'IND'  and "
					 +"TX_DOCNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+ "' and "
					 +"TX_PRDCD = '"+LP_PRDCD+"'";

		flgCHK_EXIST =  chkEXIST("CO_TXDOC",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXDOC");
				return;
		}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_SBSCD,TX_SBSCD1,TX_TRNTP,"
			+getTXDOC_PRD0(LP_PRDCD)
			+"TX_TRNFL,TX_LUSBY,TX_LUPDT,TX_STSFL) values("
			+setINSSTR("TX_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TX_SYSCD","MR","C")
			+setINSSTR("TX_SBSTP",strMKTTP,"C")
			+setINSSTR("TX_DOCTP","IND","C")
			+setINSSTR("TX_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("TX_PRDCD",LP_PRDCD,"C")
			+setINSSTR("TX_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TX_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(LP_PRDCD)+"00","C")
			+setINSSTR("TX_TRNTP","T","C")
			+getTXDOC_PRD1("INS",LP_PRDCD)
			+setINSSTR("TX_TRNFL","0","C")
			+setINSSTR("TX_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TX_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')";  // setINSSTR("TX_STSFL","0","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXDOC set "
			+setUPDSTR("TX_TRNTP","T","C")
			+setUPDSTR("TX_TRNFL","0","C")
			+getTXDOC_PRD1("UPD",LP_PRDCD)
			+setUPDSTR("TX_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TX_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"TX_STSFL = '0' where "+strWHRSTR;  // setUPDSTR("TX_STSFL","0","C")
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXDOC_PRD : "+L_EX,'E');}
}


/**
 * Building up field specification part for Insert statement through iteration
 * Example  Insert into .... ( ......  TX_DSBVL,TX_DSBFL,EXCVL,EXCFL, .....) .....
 */
private String getTXDOC_PRD0(String LP_PRDCD)
{
	String L_strTXDOC_PRD0 = "";
	for(int i=0;i<tblGRTAX.getRowCount();i++)
	{
		if(tblGRTAX.getValueAt(i,intTB3_TAXCD).toString().length()!=3)  // tax code not available
			continue;
		if(!tblGRTAX.getValueAt(i,intTB3_PRDCD).toString().equals(LP_PRDCD)) // different product code
			continue;
		if(!getCDTRN("SYSCOXXTAX"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  //does not belong to Common Tax Category
			continue;
		{
			L_strTXDOC_PRD0 += setINSSTR("TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"VL","TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"VL","N");
			L_strTXDOC_PRD0 += setINSSTR("TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"FL","TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"FL","N");
		}
	}
    return L_strTXDOC_PRD0;
}



/** Builds up value part of insert/update query  for Tax Code related fields
 * Example : insert into CO_TXDOC (............) values (.....120,'A',16,'P',.....)
 *           update CO_TXDOC set ...... TX_DSBVL=120,TX_DSBFL='A',TX_EXCVL=16,TX_EXCFL='A',.....
 */
private String getTXDOC_PRD1(String LP_SAVETP, String LP_PRDCD)
{
	String L_strTXDOC_PRD1 = "";
	for(int i=0;i<tblGRTAX.getRowCount();i++)
	{
		if(tblGRTAX.getValueAt(i,intTB3_TAXCD).toString().length()!=3)  // tax code not available
			continue;
		if(!tblGRTAX.getValueAt(i,intTB3_PRDCD).toString().equals(LP_PRDCD)) // different product code
			continue;
		if(!getCDTRN("SYSCOXXTAX"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  //does not belong to Common Tax Category
			continue;
		if(LP_SAVETP.equalsIgnoreCase("INS"))
		{
			L_strTXDOC_PRD1 += setINSSTR("TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"VL",tblGRTAX.getValueAt(i,intTB3_TAXVL).toString(),"N");
			L_strTXDOC_PRD1 += setINSSTR("TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"FL",tblGRTAX.getValueAt(i,intTB3_AMTFL).toString(),"C");
		}
		else if(LP_SAVETP.equalsIgnoreCase("UPD"))
		{
			L_strTXDOC_PRD1 += setUPDSTR("TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"VL",tblGRTAX.getValueAt(i,intTB3_TAXVL).toString(),"N");
			L_strTXDOC_PRD1 += setUPDSTR("TX_"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString()+"FL",tblGRTAX.getValueAt(i,intTB3_AMTFL).toString(),"C");
		}
	}
    return L_strTXDOC_PRD1;
}



/** Saving record in CO_TXSPC,  Product level
 */
private void saveTXSPC_PRD1(int LP_ROWNO)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString().length()!=3)
			return;
		if(!getCDTRN("SYSCOXXTAX"+tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("02"))
		   return;
		strWHRSTR =   "TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TXT_SYSCD =     'MR'  and "
					 +"TXT_SBSTP = '" +strMKTTP+"' and "
					 +"TXT_DOCTP =     'IND'  and "
					 +"TXT_DOCNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+ "' and "
					 +"TXT_PRDCD = '" +tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRDCD).toString()+      "' and "
					 +"TXT_CODCD = '" +tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD)+ "'";

		flgCHK_EXIST =  chkEXIST("CO_TXDOC",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXSPC");
				return;
		}
		
		if(!vldEXCISE(LP_ROWNO))
			return;
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,TXT_SBSCD1,TXT_TRNFL,TXT_LUSBY,TXT_LUPDT,TXT_STSFL) values ("
			+setINSSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TXT_SYSCD","MR","C")
			+setINSSTR("TXT_SBSTP",strMKTTP,"C")
			+setINSSTR("TXT_DOCTP","IND","C")
			+setINSSTR("TXT_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
			+setINSSTR("TXT_PRDCD",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRDCD).toString(),"C")
			+setINSSTR("TXT_CODCD",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString(),"C")
			+setINSSTR("TXT_CODDS",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXDS).toString().substring(0,20),"C")
			+setINSSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setINSSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setINSSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setINSSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TXT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRDCD).toString())+"00","C")
			+setINSSTR("TXT_TRNFL","0","C")
			+setINSSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"C")
			+"'0')"; //setINSSTR("TXT_STSFL","XYZ","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXSPC set "
			+setUPDSTR("TXT_CODDS",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXDS).toString().substring(0,20),"C")
			+setUPDSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setUPDSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setUPDSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setUPDSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("TXT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRDCD).toString())+"00","C")
			+setUPDSTR("TXT_TRNFL","0","C")
			+setUPDSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"C")
			+"TXT_STSFL = '0' where "+strWHRSTR; //setUPDSTR("TXT_STSFL","XYZ","C")
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXSPC_PRD1 : "+L_EX,'E');}
}



/** Returning Tax value of specified tax-code from CO_TXDOC
 */
private String getTXVAL(JTable LP_TBLNM,int LP_CDCOL, int LP_TXCOL,String LP_TAXCD)
{
	for(int z=0;z<LP_TBLNM.getRowCount();z++)
	{
		if(LP_TBLNM.getValueAt(z,LP_CDCOL).equals(LP_TAXCD))
			return LP_TBLNM.getValueAt(z,LP_TXCOL).toString();
	}
	return "0.00";
}



/** Saving LC details
 */
private void saveLCDTL()
{
	if(!txtLCNO.isVisible())
		return;
	if(txtLCNEW==null)
		return;
		if(txtLCNEW.getText().length()>0)
		{
			M_strSQLQRY="Insert into CO_QVMST (QV_CMPCD,QV_SYSCD,QV_MKTTP,QV_PRTTP,QV_DOCTP,QV_PRTCD,QV_DOCNO,QV_PRDCD,QV_OPNDT,QV_EXPDT,QV_OPNQT,QV_OPNVL,QV_UTLQT,QV_UTLVL,QV_STSFL,QV_LUSBY,QV_LUPDT,QV_TRNFL) values("
				+setINSSTR("QV_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
				+setINSSTR("QV_SYSCD","MR","C")
				+setINSSTR("QV_MKTTP",strMKTTP,"C")
				+setINSSTR("QV_PRTTP","C","C")
				+setINSSTR("QV_DOCTP","L_C","C")
				+setINSSTR("QV_PRTCD",txtBYRCD.getText(),"C")
				+setINSSTR("QV_DOCNO",txtLCNO.getText(),"C")
				+setINSSTR("QV_PRDCD","","C")
				+setINSSTR("QV_OPNDT",txtLCOPNDT.getText(),"D")
				+setINSSTR("QV_EXPDT",txtLCEXPDT.getText(),"D")
				+setINSSTR("QV_OPNQT","0.00","N")
				+setINSSTR("QV_OPNVL",txtLCOPNVL.getText(),"N")
				+setINSSTR("QV_UTLQT","0.00","N")
				+setINSSTR("QV_UTLVL",txtLCUTLVL.getText(),"N")
				+setINSSTR("QV_STSFL","0","C")
				+setINSSTR("QV_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
				+setINSSTR("QV_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
				+"'0')"; //setINSSTR("QV_TRNFL","0","C")
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"");
		}
}

/** Saving payment reference details
 */
private void savePMTDTL()
{
	if(!txtAPDNO.isVisible())
		return;
	if(txtAPDCHQNO==null)
		return;
	if(txtAPDCHQNO.getText().length()==0)
		return;
		M_strSQLQRY="Insert into CO_QVMST (QV_CMPCD,QV_DOCTP,QV_PRTCD,QV_DOCNO,QV_PRDCD,QV_OPNDT,QV_EXPDT,QV_OPNQT,QV_OPNVL,QV_UTLQT,QV_UTLVL,QV_STSFL,QV_LUSBY,QV_LUPDT,QV_TRNFL) values("
			+setINSSTR("QV_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("QV_DOCTP","ADV","C")
			+setINSSTR("QV_PRTCD",txtBYRCD.getText(),"C")
			+setINSSTR("QV_DOCNO",txtAPDCHQNO.getText(),"C")
			+setINSSTR("QV_PRDCD","","C")
			+setINSSTR("QV_OPNDT",txtAPDCHQDT.getText(),"D")
			+setINSSTR("QV_EXPDT",txtAPDDEPDT.getText(),"D")
			+setINSSTR("QV_OPNQT","0.00","N")
			+setINSSTR("QV_OPNVL",txtAPDCHQVL.getText(),"N")
			+setINSSTR("QV_UTLQT","0.00","N")
			+setINSSTR("QV_UTLVL",txtAPDUTLVL.getText(),"N")
			+setINSSTR("QV_STSFL","0","C")
			+setINSSTR("QV_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("QV_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')"; //setINSSTR("QV_TRNFL","0","C")
			System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY ,"");
}


/** Saving FOR details
 */
private void saveFORDTL()
{
	if(!txtFORNO.isVisible())
		return;
	if(txtFORTRPCD==null)
		return;
	if(txtFORTRPCD.getText().length()==0)
		return;
	M_strSQLQRY="Insert into CO_TRTRN (TRT_CMPCD,TRT_SYSCD,TRT_MKTTP,TRT_TRPCD,TRT_DOCTP,TRT_DOCNO,TRT_SBSCD,TRT_SRCCD,TRT_DSTCD,TRT_RATFL,TRT_FRTVL,TRT_ADDVL,TRT_VEHTP,TRT_VEHQT,TRT_STSFL,TRT_LUSBY,TRT_LUPDT,TRT_TRNFL) values("
		+setINSSTR("TRT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
		+setINSSTR("TRT_SYSCD","MR","C")
		+setINSSTR("TRT_MKTTP",strMKTTP,"C")
		+setINSSTR("TRT_TRPCD",txtFORTRPCD.getText(),"C")
		+setINSSTR("TRT_DOCTP","IND","C")
		+setINSSTR("TRT_DOCNO",cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"C")
		+setINSSTR("TRT_SBSCD",M_strSBSCD,"C")
		+setINSSTR("TRT_SRCCD",txtFORSRCCD.getText(),"C")
		+setINSSTR("TRT_DSTCD",txtFORDSTCD.getText(),"C")
		+setINSSTR("TRT_RATFL",(rdbFORRATFL_A.isSelected()==true ? "A" : "P"),"C")
		+setINSSTR("TRT_FRTVL",txtFORFRTVL.getText(),"N")
		+setINSSTR("TRT_ADDVL",txtFORADDVL.getText(),"N")
		+setINSSTR("TRT_VEHTP",txtFORVEHTP.getText(),"C")
		+setINSSTR("TRT_VEHQT",txtFORVEHQT.getText(),"N")
		+setINSSTR("TRT_STSFL","0","C")
		+setINSSTR("TRT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
		+setINSSTR("TRT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
		+"'0')"; //setINSSTR("TRT_TRNFL","0","C")
		System.out.println(M_strSQLQRY);
	cl_dat.exeSQLUPD(M_strSQLQRY ,"");
}


/**  Saving Deemed Export relted details
 */
private void saveDEXDTL()
{
	if(!txtDEFNO.isVisible())
		return;
	if(txtDEFNEW==null)
		return;
	if(txtDEFNEW.getText().length()==0)
		return;
			M_strSQLQRY="Insert into CO_QVMST (QV_CMPCD,QV_DOCTP,QV_PRTCD,QV_DOCNO,QV_PRDCD,QV_OPNDT,QV_EXPDT,QV_OPNQT,QV_OPNVL,QV_UTLQT,QV_UTLVL,QV_TRNFL,QV_STSFL,QV_LUSBY,QV_LUPDT) values("
				+setINSSTR("QV_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
				+setINSSTR("QV_DOCTP","FIL","C")
				+setINSSTR("QV_PRTCD",txtBYRCD.getText(),"C")
				+setINSSTR("QV_DOCNO",txtDEFNEW.getText(),"C")
				+setINSSTR("QV_PRDCD","'',","C")
				+setINSSTR("QV_OPNDT",txtDEFOPNDT.getText(),"D")
				+setINSSTR("QV_EXPDT",txtDEFEXPDT.getText(),"D")
				+setINSSTR("QV_OPNQT",txtDEFOPNQT.getText(),"N")
				+setINSSTR("QV_OPNVL",txtDEFOPNVL.getText(),"N")
				+setINSSTR("QV_UTLQT",txtDEFUTLQT.getText(),"N")
				+setINSSTR("QV_UTLVL",txtDEFUTLVL.getText(),"N")
				+setINSSTR("QV_STSFL","0","C")
				+setINSSTR("QV_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
				+setINSSTR("QV_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
				+"'0')"; //setINSSTR("QV_TRNFL","0","C")
			System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
}





/*


			String L_strCRFQT=null;
			Boolean L_TRUE=new Boolean(true);
			for(int i=0;i<tblGRDTL.getRowCount()&&tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length()>0&&cl_dat.M_flgLCUPD_pbst;i++)
			{
				if(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length()>0)
				{
					if(hstPRDCD.containsKey(tblGRDTL.getValueAt(i,intTB1_PRDCD)))
					{
						if(Double.parseDouble(nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_REQQT).toString(),"0.0"))!=0.0)
						{
					//CODE FOR CARRY FORWARD QTY UPDATION			
							L_strCRFQT=null;
							if(strIN_STSFL.equals("1"))
								if(Integer.parseInt(txtBKGDT.getText().substring(3,5))<Integer.parseInt(cl_dat.M_txtCLKDT_pbst.getText().substring(3,5))||
									Integer.parseInt(txtBKGDT.getText().substring(6))<Integer.parseInt(cl_dat.M_txtCLKDT_pbst.getText().substring(6)))
								{
									L_strCRFQT=setNumberFormat(Float.parseFloat(tblGRDTL.getValueAt(i,intTB1_REQQT).toString())-Float.parseFloat(vtrINVQT.elementAt(i).toString()),3);
								}
					//END OF CRFWD
							String L_strINDQT="0";
							if(rdbAUTH_Y.isVisible())
								if(rdbAUTH_Y.isSelected())
									L_strINDQT=tblGRDTL.getValueAt(i,intTB1_REQQT).toString();
							saveINTRN(i);
						}
						else
						{
							M_strSQLQRY="update MR_INTRN set "
								+"INT_AMDNO='"+nvlSTRVL(txtAMDNO.getText(),"00")+"',";//INT_AMDNO
							M_strSQLQRY+=" INT_INDQT=0, "
								+getUSGDTL("INT",'U',"X")+" where "//INT_SRLNO
								+"INT_MKTTP='"+strMKTTP+"' and INT_INDNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"' and "
								+"INT_PRDCD='"+tblGRDTL.getValueAt(i,intTB1_PRDCD).toString()+"'";
						}
					}
					else
					{
						String L_strINDQT="0";
						if(rdbAUTH_Y.isVisible())
							if(rdbAUTH_Y.isSelected())
								L_strINDQT=tblGRDTL.getValueAt(i,intTB1_REQQT).toString();
						saveINTRN(i);
					}
					if(cl_dat.M_flgLCUPD_pbst)
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
			if(btnAUTRN.getText().equalsIgnoreCase("Modification"))
			{
				if(flgBKGRM)
				{//UPDATING BOOKING REMARK
					M_strSQLQRY="Update MR_RMMST set RM_REMDS='"+txtBKGRM.getText() +"'"
								+" where RM_MKTTP='"+strMKTTP+"' and  RM_TRNTP ='IN' "
								+"and RM_DOCNO='"+cmbINDNO.getSelectedItem().toString()
								+txtINDNO.getText()+"' and RM_SBSCD='"+M_strSBSCD+"'";
					if(cl_dat.M_flgLCUPD_pbst)
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
				else if(txtBKGRM.getText().length()>0)
				{//INSERTING BOOKING REMARK
					M_strSQLQRY="Insert into MR_RMMST (RM_MKTTP,RM_TRNTP,RM_DOCNO,RM_REMDS,RM_SBSCD,RM_TRNFL,RM_STSFL,RM_LUSBY,RM_LUPDT) values ("
						+"'"+strMKTTP+"',"//RM_MKTTP,
						+"'IN',"//RM_TRNTP,
						+"'"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"',"//RM_DOCNO,
						+"'"+txtBKGRM.getText()+"',"//RM_REMDS,
						+"'"+M_strSBSCD+"',"//RM_SBSCD
						+getUSGDTL("RM",'I',"")+")";//RM_TRNFL,RM_STSFL,RM_LUSBY,RM_LUPDT
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			for(int P_intROWID=0;P_intROWID<tblGRDTL.getRowCount()&&tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString().length()>0;P_intROWID++)
			for(int i=0;i<vtrINVQT.size();i++)
			{//Required qty. cannot be less than invoice qty during modification
				if(Double.parseDouble(vtrINVQT.elementAt(i).toString())>0 && Double.parseDouble(vtrINVQT.elementAt(i).toString())>
				   Double.parseDouble(tblGRDTL.getValueAt(P_intROWID,intTB1_REQQT).toString()))
				{
					setMSG("D.O. of "+vtrINVQT.elementAt(i).toString()+"MT  has been already created for "+
					tblGRDTL.getValueAt(P_intROWID,intTB1_PRDDS).toString()+" ..",'E');
					return ;
					}
				}

			M_strSQLQRY="update MR_INMST set IN_STSFL='X',IN_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where IN_MKTTP='"+strMKTTP+"' and IN_INDNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
			cl_dat.M_flgLCUPD_pbst=true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			M_strSQLQRY="update MR_INTRN set INT_INDQT=0, INT_STSFL='X' ,INT_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where INT_MKTTP='"+strMKTTP+"' and INT_INDNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
			if(cl_dat.M_flgLCUPD_pbst)
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		if(cl_dat.exeDBCMT("exeSAVE"))
		{
			btnPRINT.setEnabled(true);
//					omr_hkitr.updDATAFL("IN",strMKTTP,cmbINDNO.getSelectedItem().toString()+txtINDNO.getText());
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG("Order booked ..",'N');
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				setMSG("Order Modified ..",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Order Cancelled ..",'N');
		}
		else
			setMSG("Error Occured During Saving Data ..",'E');
	}
					
}
*/





/** Verifying whether tax code related to Master(IND) / Transaction (PRD) category
 * is entered by the user
 */
private boolean chkTXDOC(String LP_TRNTP)
{
	boolean L_flgRETFL = false;
	if (LP_TRNTP.equalsIgnoreCase("IND"))
	{
		for(int i=0;i<tblCOTAX.getRowCount();i++)
		{
			if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().length()!=3) // tax code is not entered
				continue;
			//if(Float.parseFloat(tblCOTAX.getValueAt(i,intTB2_TAXVL).toString())<=0.0)  // tax value is not entered
			//	continue;
			if(!getCDTRN("SYSCOXXTAX"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  // tax does not belongs to CO_TXDOC table
				continue;
			L_flgRETFL = true;
			break;
		}
	}
	else if (LP_TRNTP.equalsIgnoreCase("PRD"))
	{
		for(int i=0;i<tblGRTAX.getRowCount();i++)
		{
			if(tblGRTAX.getValueAt(i,intTB3_TAXCD).toString().length()!=3) // tax code is not entered
				continue;
			//if(Float.parseFloat(tblGRTAX.getValueAt(i,intTB3_TAXVL).toString())<=0.0)  // tax value is not entered
			//	continue;
			if(!getCDTRN("SYSCOXXTAX"+tblGRTAX.getValueAt(i,intTB3_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  // tax does not belongs to CO_TXDOC table
				continue;
			L_flgRETFL = true;
			break;
		}
	}
	return L_flgRETFL;
}



	/**  Validating & Saving Data 
	 */
	void exeSAVE()
	{
		try
		{
			this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);			
			if(!vldDATA())
				return;
        	if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)&& !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				return;
			updateUI();
			cl_dat.M_flgLCUPD_pbst = true;
			inlTBLEDIT(tblGRDTL);
			inlTBLEDIT(tblCOTAX);
			inlTBLEDIT(tblGRTAX);
			if(flgREDTAX == true)
				exeREDTAX(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()), cmbINDNO.getSelectedItem().toString()+txtINDNO.getText(),"SAVE");

			saveINMST();
			for(int i=0;i<tblGRDTL.getRowCount()&&tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length()==10;i++)
				saveINTRN(i);
			for(int i=0;i<tblDLSCH.getRowCount()&&tblDLSCH.getValueAt(i,intTB5_PRDCD).toString().length()==10;i++)
				saveINDEL(i);
			saveRMMST("IR",txtREGRM.getText());

			for(int i=0;i<tblCOTAX.getRowCount();i++)
				saveTXSPC_IND0(i);   //Saving specific tax at Indent level from tblCOTAX
			

			for(int i=0;i<tblGRTAX.getRowCount();i++)
			{
				//Saving specific tax at Indent & at product level
				saveTXSPC_IND1(i);
				saveTXSPC_PRD1(i);
			}

			
			// Saving Common Tax at Indent Level
			if(chkTXDOC("IND"))
				saveTXDOC_IND();
			
			// Saving Common Tax at Product Level
			if(chkTXDOC("PRD"))
			{
				for(int i=0;i<tblGRTAX.getRowCount();i++)
					if(tblGRTAX.getValueAt(i,intTB3_PRDCD).toString().length()==10)
						saveTXDOC_PRD(tblGRTAX.getValueAt(i,intTB3_PRDCD).toString());
			}
			saveLCDTL();
			savePMTDTL();
			saveFORDTL();					
			saveDEXDTL();
			chkDELMAST();
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				btnPRINT.setEnabled(true);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Record saved successfully",'N');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					JOptionPane.showMessageDialog(this,"Deleted/Cancelled Successfully");
			}
			else
				setMSG((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) ? "Addition " : cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) ? "Modification/Authorisation" : cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "Deletion" : "")+" Operation not Successful",'E');
			chkDELMAST();
			clrCOMP_1();
		}

		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
			cl_dat.M_flgLCUPD_pbst=false;
			cl_dat.exeDBCMT("exeSAVE");
		}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}


	/** Marking master (Indent level) record as cancelled
	 * If qty.for all grades in transaction table is changed to Zero 
	 */
	private void chkDELMAST()
	{
		try
		{
			boolean flgDELALL = true;
			for(int i=0;i<tblGRDTL.getRowCount()&&tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length()==10;i++)
				{
				if(Float.parseFloat(nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_REQQT).toString(),"0.00"))>0)
					{flgDELALL = false; break;}
				}
			if (flgDELALL)
			{
				ResultSet L_rstRSLSET = cl_dat.exeSQLQRY2("select INT_STSFL from MR_INTRN where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_MKTTP='"+strMKTTP+"' and INT_INDNO = '"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"' and INT_STSFL<>'X'");
				if(L_rstRSLSET!=null)
					if(L_rstRSLSET.next())
						{L_rstRSLSET.close(); return;}
				System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD("Update MR_INMST set IN_STSFL='X' where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP='"+strMKTTP+"' and IN_INDNO = '"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'","");
				if(cl_dat.exeDBCMT("chkDELMAST"))
					JOptionPane.showMessageDialog(this,"Indent No."+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+" is cancelled");
			}
		}
		catch(Exception e)
		{
			setMSG(e,"chkDELMAST");
		}
	}
	
	/**  Displaying details from MR_INMST
	 */
	private void setINMST(String LP_PREFIX,ResultSet LP_RSLSET)
	{
		try
		{
			lblDSRTP.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_DSRTP","C"));
			//setCMBDFT(cmbSALTP,"SYSMR00SAL",getRSTVAL(LP_RSLSET,LP_PREFIX+"_SALTP","C"));
			//setCMBDFT(cmbMKTTP,"MSTCOXXMKT",getRSTVAL(LP_RSLSET,LP_PREFIX+"_MKTTP","C"));
			txtDSRCD.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_DSRCD","C"));
			txtCNSCD.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CNSCD","C"));
			txtCNSNM.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CNSNM","C"));
			txtBYRCD.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_BYRCD","C"));
			txtBYRNM.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_BYRNM","C"));
			txtOCFNO.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_OCFNO","C"));
			txtBKGDT.setText((LP_PREFIX.equals("OC")? cl_dat.M_strLOGDT_pbst : getRSTVAL(LP_RSLSET,LP_PREFIX+"_BKGDT","D")));
			txtBKGBY.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_BKGBY","C"));
			flgAMDFL = false;
			if (!getRSTVAL(LP_RSLSET,LP_PREFIX+"_STSFL","C").trim().equals("0"))
				flgAMDFL = true;
			String L_strINAMDNO = getRSTVAL(LP_RSLSET,LP_PREFIX+"_AMDNO","C").trim();
			String L_strINAMDDT = getRSTVAL(LP_RSLSET,LP_PREFIX+"_AMDDT","C");
			if (flgAMDFL)
			{
				L_strINAMDNO = (Integer.parseInt(L_strINAMDNO)<9 ? "0" : "")+String.valueOf(Integer.parseInt(L_strINAMDNO)+1);
				L_strINAMDDT = cl_dat.M_strLOGDT_pbst;
			}
					
			txtAMDNO.setText(L_strINAMDNO);
			txtAMDDT.setText(L_strINAMDDT);
			
			txtPAYAC.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_APTVL","N"));
			txtPAYCT.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CPTVL","N"));
			cl_dat.M_txtUSER_pbst.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_LUSBY","C"));
			cl_dat.M_txtDATE_pbst.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_LUPDT","D"));
			chbPSHFL.setSelected(getRSTVAL(LP_RSLSET,LP_PREFIX+"_PSHFL","C").equals("Y"));
			txtDSTCD.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_DSTCD","C"));
			txtCRFNO.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_PORNO","C"));
			txtCRFDT.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_PORDT","D"));
			txtDELAD.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_DELAD","C"));
			txtCURCD.setText(txtCURCD.isVisible() ? getRSTVAL(LP_RSLSET,LP_PREFIX+"_CURCD","C"): "");
			txtTRPCD.setText(txtTRPCD.isVisible() ? getRSTVAL(LP_RSLSET,LP_PREFIX+"_TRPCD","C"): "");
			txtTRPNM.setText(txtTRPCD.isVisible() ? getRSTVAL(LP_RSLSET,LP_PREFIX+"_TRPNM","C"): "");
			txtECHRT.setText(txtCURCD.isVisible() ? getRSTVAL(LP_RSLSET,LP_PREFIX+"_ECHRT","C") : "");
			txtDEFNO.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_FILRF","C"));
			txtAPDNO.setText(txtAPDNO.isVisible() ? getRSTVAL(LP_RSLSET,LP_PREFIX+"_PMTRF","C") : "");
			txtLCNO.setText(txtLCNO.isVisible() ? getRSTVAL(LP_RSLSET,LP_PREFIX+"_PMTRF","C") : "");
			chbINSFL.setSelected(getRSTVAL(LP_RSLSET,LP_PREFIX+"_INSRF","C").equals("Y"));
			txtFORNO.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_FORRF","C"));
			chbTSHFL.setSelected(getRSTVAL(LP_RSLSET,LP_PREFIX+"_TSHFL","C").equals("Y"));
			txtREGBY.setText(cl_dat.M_strUSRCD_pbst);
			txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
			//txtREGBY.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_REGBY","C"));
			//txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
			if(LP_PREFIX.equals("IN"))
			{
				lblSTSDS.setText("("+getCDTRN("STSMRXXIND"+getRSTVAL(LP_RSLSET,LP_PREFIX+"_STSFL","C"),"CMT_CODDS",hstCDTRN)+")");
				chbCFMFL.setSelected(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CFTAG","C").equals("R"));
				txtORDDT.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_INDDT","D"));
				if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("12"))
					txtDSTNM.setText(getCDTRN("SYSMRXXPOD"+txtDSTCD.getText(),"CMT_CODDS",hstCDTRN));
				else
					txtDSTNM.setText(getCDTRN("SYSCOXXDST"+txtDSTCD.getText(),"CMT_CODDS",hstCDTRN));
				if(!getRSTVAL(LP_RSLSET,LP_PREFIX+"_STSFL","C").equals("0"))
					btnPRINT.setEnabled(true);
			}
			setCMBDFT(cmbDTPCD,"SYSMRXXDTP",getRSTVAL(LP_RSLSET,LP_PREFIX+"_DTPCD","C"));
			setCMBDFT(cmbPMTCD,"SYSMRXXPMT",getRSTVAL(LP_RSLSET,LP_PREFIX+"_PMTCD","C"));
			setCMBDFT(cmbMOTCD,"SYSMR01MOT",getRSTVAL(LP_RSLSET,LP_PREFIX+"_MOTCD","C"));
			if(LP_PREFIX.equals("OC"))
			{
				//txtDSTNM.setText(getCDTRN("SYSCOXXDST"+txtDSTCD.getText(),"CMT_CODDS",hstCDTRN));
				txtDSTNM.setText(getCDTRN("SYSMRXXPOD"+txtDSTCD.getText(),"CMT_CODDS",hstCDTRN));
				txtDSRNM.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_DSRNM","C"));
			}	

		    if(txtTRPCD.isVisible())
		    txtTRPNM.setText(getRSTVAL(LP_RSLSET,LP_PREFIX+"_TRPNM","C"));
			
			ResultSet T_rstRSSET=cl_dat.exeSQLQRY2("Select PT_SPLRF from co_ptmst where pt_prttp = 'C' and pt_prtcd = '"+txtBYRCD.getText()+"'");
			while (T_rstRSSET!=null && T_rstRSSET.next())
			{
					txtPRTRF.setText(getRSTVAL(T_rstRSSET,"PT_SPLRF","C"));
			}
			T_rstRSSET.close();

		    // Commented on 26/05/2006 after maintaing party name in Indent master table
			/*ResultSet T_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prttp = 'C' and pt_prtcd in ('"+txtBYRCD.getText()+"','"+txtCNSCD.getText()+"')");
			while (T_rstRSSET!=null && T_rstRSSET.next())
			{
				if(getRSTVAL(T_rstRSSET,"PT_PRTCD","C").equals(txtBYRCD.getText()))
					txtBYRNM.setText(getRSTVAL(T_rstRSSET,"PT_PRTNM","C"));
				if(getRSTVAL(T_rstRSSET,"PT_PRTCD","C").equals(txtCNSCD.getText()))
					txtCNSNM.setText(getRSTVAL(T_rstRSSET,"PT_PRTNM","C"));
			}
			T_rstRSSET.close();
			ResultSet T_rstRSSET
			if(txtTRPCD.isVisible())
			{
    			T_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prttp = 'T' and pt_prtcd = '"+txtTRPCD.getText()+"'");
    			while (T_rstRSSET!=null && T_rstRSSET.next())
    			{
    				txtTRPNM.setText(getRSTVAL(T_rstRSSET,"PT_PRTNM","C"));
    			}
    			T_rstRSSET.close();
    		}*/
   		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setINMST : "+L_SE.toString()); 
		}
	}
	
	/**  Displaying details from MR_INTRN into Grade detail Entry Table
	 */
	private void setINTRN(String LP_PREFIX, ResultSet LP_RSLSET, int LP_ROWVL)
	{
		try
		{
			crtGRDTL(LP_RSLSET,LP_PREFIX);
			tblGRDTL.setValueAt(new Boolean(true),LP_ROWVL,intTB1_CHKFL);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_PRDCD","C"),LP_ROWVL,intTB1_PRDCD);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_PRDDS","C"),LP_ROWVL,intTB1_PRDDS);
			tblGRDTL.setValueAt(getCDTRN("SYSFGXXPKG"+getRSTVAL(LP_RSLSET,LP_PREFIX+"_PKGTP","C"),"CMT_CODDS",hstCDTRN),LP_ROWVL,intTB1_PKGTP);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_REQQT","N"),LP_ROWVL,intTB1_REQQT);
			tblGRDTL.setValueAt(String.valueOf(new Float(Float.parseFloat(getRSTVAL(LP_RSLSET,LP_PREFIX+"_REQQT","N"))/Float.parseFloat(getCDTRN("SYSFGXXPKG"+getRSTVAL(LP_RSLSET,LP_PREFIX+"_PKGTP","C"),"CMT_NCSVL",hstCDTRN).length()==0 ? "1" : getCDTRN("SYSFGXXPKG"+getRSTVAL(LP_RSLSET,LP_PREFIX+"_PKGTP","C"),"CMT_NCSVL",hstCDTRN))).intValue()),LP_ROWVL,intTB1_INDPK);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_BASRT","N"),LP_ROWVL,intTB1_BASRT);
			tblGRDTL.setValueAt(getCDTRN("SYSMR00EUS"+getRSTVAL(LP_RSLSET,LP_PREFIX+"_EUSCD","C"),"CMT_CODDS",hstCDTRN),LP_ROWVL,intTB1_EUSCD);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CC1VL","N"),LP_ROWVL,intTB1_CDCVL);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CC2VL","N"),LP_ROWVL,intTB1_DDCVL);
		//	tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CC3VL","N"),LP_ROWVL,intTB1_TDCVL);
		//	tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_CC3RF","C").length()>5 ? getRSTVAL(LP_RSLSET,LP_PREFIX+"_CC3RF","C").substring(1) : "",LP_ROWVL,intTB1_TDCRF);
			tblGRDTL.setValueAt("MT",LP_ROWVL,intTB1_UOMCD);
			if(LP_PREFIX.equals("INT"))
			{
				tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,LP_PREFIX+"_PRDRF","C"),LP_ROWVL,intTB1_PRDRF);
			}
		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setINTRN : "+L_SE.toString()); 
		}
	}
	
/**
 * Capturing Existing grade details from MR_INTRN, in modification mode
 */
	private void crtGRDTL(ResultSet LP_RSSET,String LP_PREFIX)
	{
        String[] staGRDTL = new String[intGRDTL_TOT];
        staGRDTL[intAE_GRD_REQQT] = getRSTVAL(LP_RSSET,LP_PREFIX+"_REQQT","N");
        staGRDTL[intAE_GRD_INDQT] = getRSTVAL(LP_RSSET,LP_PREFIX+"_INDQT","N");
        staGRDTL[intAE_GRD_DORQT] = getRSTVAL(LP_RSSET,LP_PREFIX+"_DORQT","C");
        staGRDTL[intAE_GRD_INVQT] = getRSTVAL(LP_RSSET,LP_PREFIX+"_INVQT","C");
		hstGRDTL.put(getRSTVAL(LP_RSSET,LP_PREFIX+"_PRDCD","C")+getRSTVAL(LP_RSSET,LP_PREFIX+"_PKGTP","C"),staGRDTL);		
	}
	
	
/** Fetching Tax details into CO_TXDOC
 */	
	private void  setCOTAX(ResultSet LP_RSLSET,String LP_TRNTP)
	{
		try
		{
			if(LP_TRNTP.equalsIgnoreCase("IND"))
			{
				if(!getRSTVAL(LP_RSLSET,"TX_PRDCD","C").equalsIgnoreCase("XXXXXXXXXX"))
					return ;
				  setCOTAX1(LP_RSLSET,"DSB");
				  setCOTAX1(LP_RSLSET,"EXC");
				  setCOTAX1(LP_RSLSET,"EDC");
				  setCOTAX1(LP_RSLSET,"EHC");
				  setCOTAX1(LP_RSLSET,"VAT");
				  setCOTAX1(LP_RSLSET,"CVD");
				  setCOTAX1(LP_RSLSET,"HIC");
				  setCOTAX1(LP_RSLSET,"PNF");
				  setCOTAX1(LP_RSLSET,"CST");
				  setCOTAX1(LP_RSLSET,"CLR");
				  setCOTAX1(LP_RSLSET,"STX");
				  setCOTAX1(LP_RSLSET,"OCT");
				  setCOTAX1(LP_RSLSET,"FRT");
				  setCOTAX1(LP_RSLSET,"INS");
				  setCOTAX1(LP_RSLSET,"CDS");
				  setCOTAX1(LP_RSLSET,"INC");
				  setCOTAX1(LP_RSLSET,"ENC");
				  setCOTAX1(LP_RSLSET,"FNI");
				  setCOTAX1(LP_RSLSET,"CDU");
				  setCOTAX1(LP_RSLSET,"OTH");
				//  setCOTAX1(LP_RSLSET,"VAT");
				//  setCOTAX1(LP_RSLSET,"SCH");
				//  setCOTAX1(LP_RSLSET,"WCT");
			}
			else if(LP_TRNTP.equalsIgnoreCase("PRD"))
			{
				if(getRSTVAL(LP_RSLSET,"TX_PRDCD","C").equalsIgnoreCase("XXXXXXXXXX"))
					return ;
				  setCOTAX2(LP_RSLSET,"DSB");
				  setCOTAX2(LP_RSLSET,"EXC");
				  setCOTAX2(LP_RSLSET,"EDC");
				  setCOTAX2(LP_RSLSET,"EHC");
				  setCOTAX2(LP_RSLSET,"VAT");
				  setCOTAX2(LP_RSLSET,"CVD");
				  setCOTAX2(LP_RSLSET,"HIC");
				  setCOTAX2(LP_RSLSET,"PNF");
				  setCOTAX2(LP_RSLSET,"CST");
				  setCOTAX2(LP_RSLSET,"CLR");
				  setCOTAX2(LP_RSLSET,"STX");
				  setCOTAX2(LP_RSLSET,"OCT");
				  setCOTAX2(LP_RSLSET,"FRT");
				  setCOTAX2(LP_RSLSET,"INS");
				  setCOTAX2(LP_RSLSET,"CDS");
				  setCOTAX2(LP_RSLSET,"INC");
				  setCOTAX2(LP_RSLSET,"ENC");
				  setCOTAX2(LP_RSLSET,"FNI");
				  setCOTAX2(LP_RSLSET,"CDU");
				  setCOTAX2(LP_RSLSET,"OTH");
				//  setCOTAX2(LP_RSLSET,"VAT");
				//  setCOTAX2(LP_RSLSET,"SCH");
				//  setCOTAX2(LP_RSLSET,"WCT");
			}
				
		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setCOTAX : "+L_SE.toString()); 
		}
		return ;
	}


	
	
/** Fetching Tax Details from CO_SPTAX
 */	
	private void  setSPTAX(ResultSet LP_RSLSET,String LP_TRNTP)
	{
		try
		{
			String	L_strTAXCD = getRSTVAL(LP_RSLSET,"TXT_CODCD","C");
			if(LP_TRNTP.equalsIgnoreCase("IND"))
			{
				if(!getRSTVAL(LP_RSLSET,"TXT_PRDCD","C").equalsIgnoreCase("XXXXXXXXXX"))
					return ;
				//setCOTAX1(LP_RSLSET,"DSB");
				if(!getRSTVAL(LP_RSLSET,"TXT_CODFL","C").equalsIgnoreCase("X"))
				{	
					if(Float.parseFloat(getRSTVAL(LP_RSLSET,"TXT_CODVL","N"))<=0)  // tax value is not available
						return;
				}
				tblCOTAX.setValueAt(new Boolean(true),intCOTAX_ROW,intTB2_CHKFL);
				tblCOTAX.setValueAt(L_strTAXCD,intCOTAX_ROW,intTB2_TAXCD);
				tblCOTAX.setValueAt(getCDTRN("SYSCOXXTAX"+L_strTAXCD,"CMT_CODDS", hstCDTRN),intCOTAX_ROW,intTB2_TAXDS);
				tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TXT_CODVL","N"),intCOTAX_ROW,intTB2_TAXVL);
				tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TXT_CODFL","C"),intCOTAX_ROW,intTB2_AMTFL);
				tblCOTAX.setValueAt(getCDTRN("SYSCOXXTAX"+L_strTAXCD,"CMT_CCSVL", hstCDTRN),intCOTAX_ROW,intTB2_PRCSQ);
				intCOTAX_ROW++;
			}
			else if(LP_TRNTP.equalsIgnoreCase("OCF"))
			{
				if(!getRSTVAL(LP_RSLSET,"TXT_PRDCD","C").equalsIgnoreCase("XXXXXXXXXX"))
					return ;
				//setCOTAX1(LP_RSLSET,"DSB");
				if(!getRSTVAL(LP_RSLSET,"TXT_CODFL","C").equalsIgnoreCase("X"))
				{	
					if(Float.parseFloat(getRSTVAL(LP_RSLSET,"TXT_CODVL","N"))<=0)  // tax value is not available
						return;
				}
				//System.out.println("tax detail ocf");
				tblCOTAX.setValueAt(new Boolean(true),intCOTAX_ROW,intTB2_CHKFL);
				tblCOTAX.setValueAt(L_strTAXCD,intCOTAX_ROW,intTB2_TAXCD);
				tblCOTAX.setValueAt(getCDTRN("SYSCOXXTAX"+L_strTAXCD,"CMT_CODDS", hstCDTRN),intCOTAX_ROW,intTB2_TAXDS);
				tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TXT_CODVL","N"),intCOTAX_ROW,intTB2_TAXVL);
				tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TXT_CODFL","C"),intCOTAX_ROW,intTB2_AMTFL);
				tblCOTAX.setValueAt(getCDTRN("SYSCOXXTAX"+L_strTAXCD,"CMT_CCSVL", hstCDTRN),intCOTAX_ROW,intTB2_PRCSQ);
				intCOTAX_ROW++;
			}
			else if(LP_TRNTP.equalsIgnoreCase("PRD"))
			{
				if(getRSTVAL(LP_RSLSET,"TXT_PRDCD","C").equalsIgnoreCase("XXXXXXXXXX"))
					return ;
				  //setCOTAX2(LP_RSLSET,"DSB");
				if(!getRSTVAL(LP_RSLSET,"TXT_CODFL","C").equalsIgnoreCase("X"))
				{	
					if(Float.parseFloat(getRSTVAL(LP_RSLSET,"TXT_CODVL","N"))<=0)  // tax value is not available
						return ;
				}
				tblGRTAX.setValueAt(new Boolean(true),intGRTAX_ROW,intTB3_CHKFL);
				tblGRTAX.setValueAt(getRSTVAL(LP_RSLSET,"TXT_PRDCD","C"),intGRTAX_ROW,intTB3_PRDCD);
				tblGRTAX.setValueAt(getPRMST(getRSTVAL(LP_RSLSET,"TXT_PRDCD","C"),"PR_PRDDS"),intGRTAX_ROW,intTB3_PRDDS);
				tblGRTAX.setValueAt(L_strTAXCD,intGRTAX_ROW,intTB3_TAXCD);
				tblGRTAX.setValueAt(getCDTRN("SYSCOXXTAX"+L_strTAXCD,"CMT_CODDS", hstCDTRN),intGRTAX_ROW,intTB3_TAXDS);
				tblGRTAX.setValueAt(getRSTVAL(LP_RSLSET,"TXT_CODVL","N"),intGRTAX_ROW,intTB3_TAXVL);
				tblGRTAX.setValueAt(getRSTVAL(LP_RSLSET,"TXT_CODFL","C"),intGRTAX_ROW,intTB3_AMTFL);
				tblGRTAX.setValueAt(getCDTRN("SYSCOXXTAX"+L_strTAXCD,"CMT_CCSVL", hstCDTRN),intGRTAX_ROW,intTB3_PRCSQ);
				intGRTAX_ROW++;
			}
				
		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setCOTAX : "+L_SE.toString()); 
		}
		return ;
	}
	

	/** Saving Indent level TAX details into CO_TXDOC 
	 */
	private void setCOTAX1(ResultSet LP_RSLSET,String LP_TAXCD)
	{
		if(!getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"FL","C").equalsIgnoreCase("X"))
		{	
			if(Float.parseFloat(getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"VL","N"))<=0)  // tax value is not available
				return;
		}
		tblCOTAX.setValueAt(new Boolean(true),intCOTAX_ROW,intTB2_CHKFL);
		tblCOTAX.setValueAt(LP_TAXCD,intCOTAX_ROW,intTB2_TAXCD);
		tblCOTAX.setValueAt(getCDTRN("SYSCOXXTAX"+LP_TAXCD,"CMT_CODDS", hstCDTRN),intCOTAX_ROW,intTB2_TAXDS);
		if(LP_TAXCD.equals("STX"))
			tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TX_STXDS","C"),intCOTAX_ROW,intTB2_TAXDS);
		else if(LP_TAXCD.equals("OTH"))
			tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TX_OTHDS","C"),intCOTAX_ROW,intTB2_TAXDS);
		tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"VL","N"),intCOTAX_ROW,intTB2_TAXVL);
		tblCOTAX.setValueAt(getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"FL","C"),intCOTAX_ROW,intTB2_AMTFL);
		tblCOTAX.setValueAt(getCDTRN("SYSCOXXTAX"+LP_TAXCD,"CMT_CCSVL", hstCDTRN),intCOTAX_ROW,intTB2_PRCSQ);
		intCOTAX_ROW++;
		return ;
	}

	/** Saving grade level TAX details into CO_TXDOC 
	 */
	private void setCOTAX2(ResultSet LP_RSLSET,String LP_TAXCD)
	{
		//System.out.print("setCO_TAX2 : Row No."+intGRTAX_ROW+"  "+LP_TAXCD+" / "+"TX_"+LP_TAXCD+"VL");
		if(!getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"FL","C").equalsIgnoreCase("X"))
		{	
			if(Float.parseFloat(getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"VL","N"))<=0)  // tax value is not available
				return ;
		}
		tblGRTAX.setValueAt(new Boolean(true),intGRTAX_ROW,intTB3_CHKFL);
		tblGRTAX.setValueAt(getRSTVAL(LP_RSLSET,"TX_PRDCD","C"),intGRTAX_ROW,intTB3_PRDCD);
		tblGRTAX.setValueAt(getPRMST(getRSTVAL(LP_RSLSET,"TX_PRDCD","C"),"PR_PRDDS"),intGRTAX_ROW,intTB3_PRDDS);
		tblGRTAX.setValueAt(LP_TAXCD,intGRTAX_ROW,intTB3_TAXCD);
		tblGRTAX.setValueAt(getCDTRN("SYSCOXXTAX"+LP_TAXCD,"CMT_CODDS", hstCDTRN),intGRTAX_ROW,intTB3_TAXDS);
		tblGRTAX.setValueAt(getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"VL","N"),intGRTAX_ROW,intTB3_TAXVL);
		tblGRTAX.setValueAt(getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"FL","C"),intGRTAX_ROW,intTB3_AMTFL);
		tblGRTAX.setValueAt(getCDTRN("SYSCOXXTAX"+LP_TAXCD,"CMT_CCSVL", hstCDTRN),intGRTAX_ROW,intTB3_PRCSQ);
		intGRTAX_ROW++;
		//System.out.println("    "+getRSTVAL(LP_RSLSET,"TX_"+LP_TAXCD+"VL","C"));
		return;
	}

private void setINREM()
{
	try
	{
	flgREGRM=false;
	//flgBKGRM=false;
	//flgAUTRM=false;
	ResultSet L_rstRSSET;
	L_rstRSSET = cl_dat.exeSQLQRY2("Select * from MR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP='"+strMKTTP+"' and RM_DOCNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'");

	if(L_rstRSSET!=null)
		{
			while(L_rstRSSET.next())
			{

				if(getRSTVAL(L_rstRSSET,"RM_TRNTP","C").equals("IR"))
				{
					txtREGRM.setText(getRSTVAL(L_rstRSSET,"RM_REMDS","C"));
					flgREGRM=true;
				}
				//else if(getRSTVAL(L_rstRSSET,"RM_TRNTP","C").equals("IN"))
				//{
				//	txtBKGRM.setText(getRSTVAL(L_rstRSSET,"RM_REMDS","C"));
				//	flgBKGRM=true;
				//}
				//else if(getRSTVAL(L_rstRSSET,"RM_TRNTP","C").equals("IA"))
				//{
				//	txtAUTRM.setText(getRSTVAL(L_rstRSSET,"RM_REMDS","C"));
				//	flgAUTRM=true;
				//}
						
			}
		}
				
		L_rstRSSET.close();
		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setINREM : "+L_SE.toString()); 
		}
}	

private void setOCREM()
{
	try
	{
	flgREGRM=false;
	//flgBKGRM=false;
	//flgAUTRM=false;
	ResultSet L_rstRSSET;
	L_rstRSSET = cl_dat.exeSQLQRY2("Select * from MR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP='"+strMKTTP+"' and RM_DOCNO='"+txtOCFNO.getText()+"' and RM_TRNTP='OR'" );

	if(L_rstRSSET!=null)
		{
			while(L_rstRSSET.next())
			{

				if(getRSTVAL(L_rstRSSET,"RM_TRNTP","C").equals("OR"))
				{
					txtREGRM.setText(getRSTVAL(L_rstRSSET,"RM_REMDS","C"));
					flgREGRM=true;
				}
				//else if(getRSTVAL(L_rstRSSET,"RM_TRNTP","C").equals("IN"))
				//{
				//	txtBKGRM.setText(getRSTVAL(L_rstRSSET,"RM_REMDS","C"));
				//	flgBKGRM=true;
				//}
				//else if(getRSTVAL(L_rstRSSET,"RM_TRNTP","C").equals("IA"))
				//{
				//	txtAUTRM.setText(getRSTVAL(L_rstRSSET,"RM_REMDS","C"));
				//	flgAUTRM=true;
				//}
						
			}
		}
				
		L_rstRSSET.close();
		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setOCREM : "+L_SE.toString()); 
		}
}	

private void setSPCTX()
{
	try
	{
				M_strSQLQRY="Select * from CO_TXSPC where TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TXT_SYSCD='MR' "
					+"and TXT_DOCTP='IND' and TXT_SBSCD='"+M_strSBSCD+"' and "
					+"txT_DOCNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'"// and "
//					+"TXT_PRTTP='C'"
					;
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET!=null)
				{
					int i=0;
					while(L_rstRSSET.next())
					{
						if(getRSTVAL(L_rstRSSET,"TXT_PRDCD","C").equals("XXXXXXXXXX"))
						{
							tblCOTAX.setValueAt(new Boolean(true),i,intTB2_CHKFL);
							tblCOTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_CODCD","C"),i,intTB2_TAXCD);
							tblCOTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_CODDS","C"),i,intTB2_TAXDS);
							tblCOTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_CODVL","N"),i,intTB2_TAXVL);
							tblCOTAX.setValueAt((getRSTVAL(L_rstRSSET,"TXT_CODCD","C").equals("A")? "Amt." : "%"),i,intTB2_AMTFL);
							tblCOTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_PRCSQ","C"),i++,intTB2_PRCSQ);
						}
						else
						{
							tblGRTAX.setValueAt(new Boolean(true),i,intTB3_CHKFL);
							tblGRTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_PRDCD","C"),i,intTB3_PRDDS);
							tblGRTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_CODCD","C"),i,intTB3_TAXCD);
							tblGRTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_CODDS","C"),i,intTB3_TAXDS);
							//tblGRTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_CODVL","N"),i,intTB3_PRTRF);
							tblGRTAX.setValueAt((getRSTVAL(L_rstRSSET,"TXT_CODCD","C").equals("A")? "Amt." : "%"),i,intTB3_TAXVL);
							tblGRTAX.setValueAt(getRSTVAL(L_rstRSSET,"TXT_PRCSQ","C"),i++,intTB3_AMTFL);
						}
					}
					
				}
		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setSPCTX : "+L_SE.toString()); 
		}
}

/** Additional check for existance of Indent record
 * Depending on selection mode Addition / Modification / Deletion
 */
private boolean exeNEWFIND()
{
	boolean L_flgRETFL = false;
	try
	{
		ResultSet L_rstRSLSET = cl_dat.exeSQLQRY2("select IN_STSFL from MR_INMST where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP = '"+strMKTTP+"' and IN_INDNO = '"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'");
		if(L_rstRSLSET!=null && L_rstRSLSET.next())
		{
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)) && L_rstRSLSET.getString("IN_STSFL").toString().equalsIgnoreCase("X"))
				{JOptionPane.showMessageDialog(this,"Indent No. "+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+" is already cancelled"); L_flgRETFL = true;}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				if (L_rstRSLSET.getString("IN_STSFL").toString().equalsIgnoreCase("X"))
					{JOptionPane.showMessageDialog(this,"Indent No. "+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+" is already cancelled"); L_flgRETFL = true;}
			    else
					{JOptionPane.showMessageDialog(this,"Indent No. "+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+" already exists, Go for Modification"); L_flgRETFL = true;}
			}
			L_rstRSLSET.close();				
		}
	}
	catch(Exception L_SE)
	{
		System.out.println("Error in exeNEWFIND : "+L_SE.toString()); 
	}
	return L_flgRETFL;
}

/** Initializing table editing before poulating/capturing data
 */
private void inlTBLEDIT(JTable P_tblTBLNM)
{
	if(!P_tblTBLNM.isEditing())
		return;
	P_tblTBLNM.getCellEditor().stopCellEditing();
	P_tblTBLNM.setRowSelectionInterval(0,0);
	P_tblTBLNM.setColumnSelectionInterval(0,0);
			
}
	
	void getDATA()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst)) 
			{
				M_strSQLQRY="Select * from MR_DOTRN where "
				+" DOT_MKTTP='"+strMKTTP+"' and DOT_INDNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"' and dot_stsfl <>'X'" ;
				rstRSSET1=cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(rstRSSET1!=null)
				{
					if(rstRSSET1.next())
					{
						JOptionPane.showMessageDialog(this,"D.O. is generated, can not amend Indent ");
						rstRSSET1.close();
						return;
					}

				}
			}*/
			String L_strADDSTR = cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst) ? "" : " and IN_STSFL <> 'X' and INT_STSFL <> 'X'" ;
			M_strSQLQRY="Select * from VW_INTRN where  IN_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IN_MKTTP='"+strMKTTP+"' and IN_INDNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"' "
				+L_strADDSTR+" order by int_srlno";

			
			rstRSSET1=cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(rstRSSET1==null || (!rstRSSET1.next()))
			{
				if(!exeNEWFIND())
					setMSG("Record Not found",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst))
			{
			   if(rstRSSET1.getDouble("INT_DORQT")>0)
					{JOptionPane.showMessageDialog(this,"DO has been prepared against Indent No. "+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"  ....  Modification Not Possible"); return;}
			}

			int row=0;

			inlTBLEDIT(tblGRDTL);
			inlTBLEDIT(tblCOTAX);
			inlTBLEDIT(tblGRTAX);
			inlINMST(rstRSSET1);
			strIN_STSFL=getRSTVAL(rstRSSET1,"IN_STSFL","C");
			if(!chkSBSCD(rstRSSET1))
				return;
			

			setINMST("IN",rstRSSET1);
			while(true)
			{
				setINTRN("INT",rstRSSET1, row);
				row++;
				if (!rstRSSET1.next())
					break;
			}
			rstRSSET1.close();

			//SET ENABLE STATUS WRT D.O. PREPARED OR NOT
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
				setMODFLT(strIN_STSFL);
			setINREM();  

			ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXDOC where TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_SYSCD='MR' and TX_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TX_DOCTP='IND' and "
					+"tx_DOCNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'");
			tblCOTAX.clrTABLE();
			tblGRTAX.clrTABLE();
			intCOTAX_ROW=0;
			intGRTAX_ROW=0;
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					setCOTAX(L_rstRSSET,"IND");
					setCOTAX(L_rstRSSET,"PRD");
				}
				L_rstRSSET.close();
			}
			L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXSPC where TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TXT_SYSCD='MR' and TXT_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TXT_DOCTP='IND' and "
					+"txt_DOCNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'");
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					setSPTAX(L_rstRSSET,"IND");
					setSPTAX(L_rstRSSET,"PRD");
				}
				L_rstRSSET.close();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
				{setDSBL("MST"); setDSBL("TBP1"); setDSBL("TBP2"); setDSBL("TBP3"); setDSBL("TBP4");}
			M_strSQLQRY = " SELECT * FROM MR_INDEL,CO_PRMST WHERE IND_PRDCD = PR_PRDCD AND "
			+" IND_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IND_MKTTP='"+strMKTTP+"' and IND_INDNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'"+
				" AND IND_STSFL <>'X' order by ind_prdcd,ind_srlno";	
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int i=0;
		    inlTBLEDIT(tblDLSCH);
			if(L_rstRSSET !=null)
			{
			    while(L_rstRSSET.next())
			    {
			        tblDLSCH.setValueAt(L_rstRSSET.getString("IND_PRDCD"),i,intTB5_PRDCD);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("PR_PRDDS"),i,intTB5_PRDDS);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("IND_DELTP"),i,intTB5_DELTP);		        
	                tblDLSCH.setValueAt(getCDTRN("SYSFGXXPKG"+getRSTVAL(L_rstRSSET,"IND_PKGTP","C"),"CMT_CODDS",hstCDTRN),i,intTB5_PKGTP);		        
	                tblDLSCH.setValueAt(getRSTVAL(L_rstRSSET,"IND_DSPDT","D"),i,intTB5_DELDT);
	                tblDLSCH.setValueAt(L_rstRSSET.getString("IND_INDQT"),i,intTB5_DELQT);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("IND_SRLNO"),i,intTB5_SRLNO);		        
	                tblDLSCH.setValueAt(getRSTVAL(L_rstRSSET,"IND_DSPDT","D"),i,intTB5_ORGDT);
	                i++;	
	 		    }
			    L_rstRSSET.close();
			}
			
			
		}
		catch (Exception e){setMSG(e,"in child.getData");}
		finally	{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}

	void setOCDTL()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			//String L_strADDSTR = cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst) ? "" : " and IN_STSFL <> 'X' and INT_STSFL <> 'X'" ;
			M_strSQLQRY="Select * from MR_OCMST,MR_OCTRN where OC_MKTTP=OCT_MKTTP and OCT_OCFNO=OC_OCFNO and OCT_CMPCD = OC_CMPCD "
				+" and OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP='"+strMKTTP+"' and OC_OCFNO = '"+txtOCFNO.getText()+"'"
				+" order by OCT_SRLNO";

			
			rstRSSET1=cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(rstRSSET1==null || (!rstRSSET1.next()))
			{
				//if(!exeNEWFIND())
				setMSG("Record Not found",'E');
				return;
			}
				

			int row=0;

			inlTBLEDIT(tblGRDTL);
			inlTBLEDIT(tblCOTAX);
			inlTBLEDIT(tblGRTAX);
			
			//inlINMST(rstRSSET1);
			strOC_STSFL=getRSTVAL(rstRSSET1,"OC_STSFL","C");
			if(!chkSBSCD(rstRSSET1))
				return;

			setINMST("OC",rstRSSET1);
			while(true)
			{
				setINTRN("OCT",rstRSSET1, row);
				row++;
				if (!rstRSSET1.next())
					break;
			}
			rstRSSET1.close();
//*****************

			//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
			//	{setDSBL("MST"); setDSBL("TBP1"); setDSBL("TBP2"); setDSBL("TBP3"); setDSBL("TBP4");}
			M_strSQLQRY = " SELECT * FROM MR_OCDEL,CO_PRMST WHERE OCD_PRDCD = PR_PRDCD AND "
			+" OCD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCD_MKTTP='"+strMKTTP+"' and OCD_OCFNO = '"+txtOCFNO.getText()+"' AND OCD_STSFL <>'X' order by OCD_PRDCD,OCD_SRLNO";	
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int i=0;
		    inlTBLEDIT(tblDLSCH);
			if(L_rstRSSET !=null)
			{
			    while(L_rstRSSET.next())
			    {
			        tblDLSCH.setValueAt(L_rstRSSET.getString("OCD_PRDCD"),i,intTB5_PRDCD);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("PR_PRDDS"),i,intTB5_PRDDS);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("OCD_DELTP"),i,intTB5_DELTP);		        
	                tblDLSCH.setValueAt(getCDTRN("SYSFGXXPKG"+getRSTVAL(L_rstRSSET,"OCD_PKGTP","C"),"CMT_CODDS",hstCDTRN),i,intTB5_PKGTP);		        
	                tblDLSCH.setValueAt(getRSTVAL(L_rstRSSET,"OCD_DSPDT","D"),i,intTB5_DELDT);
	                tblDLSCH.setValueAt(L_rstRSSET.getString("OCD_OCFQT"),i,intTB5_DELQT);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("OCD_SRLNO"),i,intTB5_SRLNO);		        
	                tblDLSCH.setValueAt(getRSTVAL(L_rstRSSET,"OCD_DSPDT","D"),i,intTB5_ORGDT);
	                i++;	
	 		    }
			    L_rstRSSET.close();
			}
			

			//ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXSPC where TXT_SYSCD='MR' and TXT_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TXT_DOCTP='OCF' and "
			//		+"'txt_DOCNO='"+txtOCFNO.getText()+"'");
			L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXSPC where TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TXT_SYSCD='MR' and TXT_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TXT_DOCTP='OCF' and "
									+"TXT_DOCNO='"+txtOCFNO.getText()+"'");
//System.out.println("Select * from CO_TXSPC where TXT_SYSCD='MR' and TXT_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TXT_DOCTP='OCF' and "
//									+"TXT_DOCNO='"+txtOCFNO.getText()+"'");			
//String s = "Select * from CO_TXSPC where TXT_SYSCD='MR' and TXT_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TXT_DOCTP='OCF' and "+"txt_DOCNO='"+txtINDNO.getText()+"'";
			
			if(L_rstRSSET!=null)
			{
//System.out.println("txdtl not null");	
				while(L_rstRSSET.next())
				{
					setSPTAX(L_rstRSSET,"OCF");
					setSPTAX(L_rstRSSET,"PRD");
				}
				L_rstRSSET.close();
			}
			setOCREM();
			String L = "SELECT CMT_CODCD FROM CO_CDTRN,MR_OCMST WHERE OC_DSRCD=CMT_CHP02 AND CMT_CGSTP='MR"+strMKTTP+"IND' AND OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_OCFNO='"+txtOCFNO.getText().trim()+"' AND SUBSTRING(CMT_CODCD,4,1)='"+strYRDGT+"'";
			//L_rstRSSET=cl_dat.exeSQLQRY2("SELECT CMT_CODCD FROM CO_CDTRN,MR_OCMST WHERE OC_DSRCD=CMT_CHP02 AND CMT_CGSTP='MR"+strMKTTP+"IND' AND OC_OCFNO='"+txtOCFNO.getText()+"' AND SUBSTRING(CMT_CODCD,4,1)='"+strYRDGT+"'");
			System.out.println(L);
			L_rstRSSET=cl_dat.exeSQLQRY2(L);
			if(L_rstRSSET!=null)
			{
					//System.out.println("I got it 0");
				while(L_rstRSSET.next())
				{
					//System.out.println("I got it");
					cmbINDNO.setSelectedItem(getRSTVAL(L_rstRSSET,"CMT_CODCD","C"));
				}
				L_rstRSSET.close();
			}
			else
			{
				setMSG("Indent Series Not Found in Codes Transaction...",'E');				
			}
			//setCMBDFT(cmbDTPCD,"SYSMRXXDTP",getRSTVAL(LP_RSLSET,"OC_DTPCD","C"));
			
//*****************
			
			//SET ENABLE STATUS WRT Indent PREPARED OR NOT
			//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
			//	setMODFLT(strOC_STSFL);
			/**setINREM();  

			ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXDOC where TX_SYSCD='MR' and TX_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TX_DOCTP='IND' and "
					+"tx_DOCNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'");
			tblCOTAX.clrTABLE();
			tblGRTAX.clrTABLE();
			intCOTAX_ROW=0;
			intGRTAX_ROW=0;
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					setCOTAX(L_rstRSSET,"IND");
					setCOTAX(L_rstRSSET,"PRD");
				}
				L_rstRSSET.close();
			}
			L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXSPC where TXT_SYSCD='MR' and TXT_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TXT_DOCTP='IND' and "
					+"txt_DOCNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'");
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					setSPTAX(L_rstRSSET,"IND");
					setSPTAX(L_rstRSSET,"PRD");
				}
				L_rstRSSET.close();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
				{setDSBL("MST"); setDSBL("TBP1"); setDSBL("TBP2"); setDSBL("TBP3"); setDSBL("TBP4");}
			M_strSQLQRY = " SELECT * FROM MR_INDEL,CO_PRMST WHERE IND_PRDCD = PR_PRDCD AND "
			+" IND_MKTTP='"+strMKTTP+"' and IND_INDNO='"+cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'"+
				" AND IND_STSFL <>'X' order by ind_prdcd,ind_srlno";	
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int i=0;
		    inlTBLEDIT(tblDLSCH);
			if(L_rstRSSET !=null)
			{
			    while(L_rstRSSET.next())
			    {
			        tblDLSCH.setValueAt(L_rstRSSET.getString("IND_PRDCD"),i,intTB5_PRDCD);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("PR_PRDDS"),i,intTB5_PRDDS);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("IND_DELTP"),i,intTB5_DELTP);		        
	                tblDLSCH.setValueAt(getCDTRN("SYSFGXXPKG"+getRSTVAL(L_rstRSSET,"IND_PKGTP","C"),"CMT_CODDS",hstCDTRN),i,intTB5_PKGTP);		        
	                tblDLSCH.setValueAt(getRSTVAL(L_rstRSSET,"IND_DSPDT","D"),i,intTB5_DELDT);
	                tblDLSCH.setValueAt(L_rstRSSET.getString("IND_INDQT"),i,intTB5_DELQT);		        
	                tblDLSCH.setValueAt(L_rstRSSET.getString("IND_SRLNO"),i,intTB5_SRLNO);		        
	                tblDLSCH.setValueAt(getRSTVAL(L_rstRSSET,"IND_DSPDT","D"),i,intTB5_ORGDT);
	                i++;	
	 		    }
			    L_rstRSSET.close();
			}*/
			
			
		}
		catch (Exception e){setMSG(e,"in child.getData");}
		finally	{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
	
	private boolean chkSBSCD(ResultSet LP_RSLSET)
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				return true;
			if(M_strSBSCD.substring(0,2).equals(getRSTVAL(LP_RSLSET,"IN_ZONCD","C")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(getRSTVAL(LP_RSLSET,"IN_SALTP","C")))
				return true;
			setMSG("Zone & Product Type Mismatch: Actual = "+getRSTVAL(LP_RSLSET,"IN_ZONCD","C")+"/"+getRSTVAL(LP_RSLSET,"IN_SALTP","C").substring(2,4)+"  Selected = "+M_strSBSCD.substring(0,2)+"/"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()),'E');
			//if(M_strSBSCD.equals(getRSTVAL(LP_RSLSET,"IN_ZONCD","C")+getRSTVAL(LP_RSLSET,"INT_SBSCD1","C").substring(2,4)+"00"))
			//	return true;
			//setMSG("Zone & Product Type Mismatch: Selected = "+getRSTVAL(LP_RSLSET,"IN_ZONCD","C")+"/"+getRSTVAL(LP_RSLSET,"INT_SBSCD1","C").substring(2,4)+"  Original = "+M_strSBSCD.substring(0,2)+"/"+((cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()):""),'E');
		}
		catch (Exception e){setMSG(e,"chkSBSCD");}
		return false;
	}
	
	
	
	/** Extra details taken out from getDATA
	 */
	private void inlINMST(ResultSet LP_RSLSET)
	{						  
		//int L_intTEMP1=cmbMKTTP.getSelectedIndex();
		int L_intTEMP2=cmbINDNO.getSelectedIndex();
		String L_strTEMP3=cmbMKTTP.getSelectedItem().toString().trim();
		String L_strTEMP1=txtINDNO.getText();
		String L_strTEMP2=txtBYRNM.getText();
		clrCOMP_1();
		//cmbMKTTP.setSelectedIndex(L_intTEMP1);
		cmbMKTTP.setSelectedItem(L_strTEMP3);
		cmbINDNO.setSelectedIndex(L_intTEMP2);

		txtINDNO.setText(L_strTEMP1);
		if (L_strTEMP2.length()>0)
		{
			txtBYRNM.setText(L_strTEMP2);
			txtCNSNM.setText(txtBYRCD.getText().equals(txtCNSCD.getText()) ? txtBYRNM.getText(): txtCNSNM.getText());
		}

		//new INPVF().verify(txtCNSCD);
		//new INPVF().verify(txtBYRCD);
		//new INPVF().verify(txtDSTCD);
		
	}
	

	/**
	 */
	private void setDSBL(String LP_TRNTP)
	{
		if(LP_TRNTP.equalsIgnoreCase("MST"))
		{
			txtBYRCD.setEnabled(false);
			txtCNSCD.setEnabled(false);
			txtDSTCD.setEnabled(false);
			txtORDDT.setEnabled(false);
			txtREGBY.setEnabled(false);
			txtREGDT.setEnabled(false);
			txtOCFNO.setEnabled(false);
		}
		if(LP_TRNTP.equalsIgnoreCase("TBP1"))
		{
			tbpMAIN.setSelectedIndex(1);
			txtCRFNO.setEnabled(false);
			txtCRFDT.setEnabled(false);
			cmbDTPCD.setEnabled(false);
			cmbMOTCD.setEnabled(false);
			cmbPMTCD.setEnabled(false);
			txtPAYCT.setEnabled(false);
			txtPAYAC.setEnabled(false);
			chbINSFL.setEnabled(false);
			chbPSHFL.setEnabled(false);
			chbTSHFL.setEnabled(false);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst)) && !exeDSPCHK("INV_DOR_NOMSG"))
				cmbDTPCD.setEnabled(true);
		}
		if(LP_TRNTP.equalsIgnoreCase("TBP2"))
			tblGRDTL.setEnabled(false);
		if(LP_TRNTP.equalsIgnoreCase("TBP3"))
			tblGRDTL.setEnabled(false);
		if(LP_TRNTP.equalsIgnoreCase("TBP4"))
			tblGRDTL.setEnabled(false);
	}
	
	
	
	/**To Disable fields where modification is not to be allowed depending on indent status	 */
	private void setMODFLT(String P_strSTSFL)
	{
		cmbSALTP.setEnabled(false);
		cmbMKTTP.setEnabled(false);
		//lblDSRTP.setEnabled(false);
		cmbINDNO.setEnabled(false);
		txtINDNO.setEnabled(false);
		if(P_strSTSFL.equals("1"))
		{
			setDSBL("MST");
		Enumeration enmGRDKEYS=hstGRDTL.keys();
		boolean L_flgRETFL = false;
		while(enmGRDKEYS.hasMoreElements())
		{
			String L_strGRDCD = (String)enmGRDKEYS.nextElement();
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_DORQT"))>0)
				{setDSBL("TBP1"); break;}
		}
	}
}

	
	/**
	 */
	private boolean vldITMDT(String P_strSQLQRY)
	{
		try
		{
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY1(P_strSQLQRY);
			if(L_rstRSSET==null)
				return false;
			else if (!L_rstRSSET.next())
				return false;
			L_rstRSSET.close();
			return true;
		}
		catch(Exception e)
		{}
	return false;
	}

	

/**
 */	
private class INPVF extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
		 if(input==cmbMKTTP)
			 return true;
			try
			{
				//if(input==txtDSRTP)
				//{
				//	if(!txtDSRTP.getText().equals("G") && !txtDSRTP.getText().equals("D"))
 				//		{setMSG("Please enter Distributor Type D or G ..",'E');	return false;}
				//	else
				//		setCMBINDNO();
				//}
				if(input==txtPAYAC && txtPAYAC.getText().length()==0)
				{
					setMSG("Please enter Account Cr. days ..",'E');
					return false;
				}
				else if(input==txtPAYCT && txtPAYCT.getText().length()==0)
				{
					setMSG("Please enter Customer Cr. days ..",'E');
					return false;
				}
				else if (input instanceof JTextField&&((JTextField)input).getText().length()==0)	
					return true;
				else if(input==txtBYRCD)
				{
					//System.out.println("Input verifier");
					if(txtBYRCD.getText().length() !=5)
						return true;
					txtBYRCD.setText(txtBYRCD.getText().toUpperCase());
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtBYRCD.getText()+"' and pt_PRTTP='C' and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ");
					if(L_rstRSSET!=null)
					{
						if (L_rstRSSET.next())
						{
							txtBYRNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
							strBYRDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
								txtPRTRF.setText(getRSTVAL(L_rstRSSET,"PT_SPLRF","C"));
							//System.out.println("destination "+ getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
							if(txtDSTCD.getText().length()==0)
							{
								txtDSTCD.setText(getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
							}
							L_rstRSSET.close();
							if(txtCNSCD.getText().length()==0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
							{
								txtCNSCD.setText(txtBYRCD.getText());
								txtCNSNM.setText(txtBYRNM.getText());
							}
							setMSG("",'N');
							getOVRDUE();
						}
						else 
							setMSG("Invalid Buyer Code ..",'E');
					}
					else
						setMSG("Invalid Buyer Code ..",'E');
				}

				else if(input==txtCNSCD)
				{
					if(!txtBYRCD.getText().equals(txtCNSCD.getText()))
					{
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtCNSCD.getText()+"' and pt_PRTTP='C' and upper(isnull(PT_STSFL,' ')) <> 'X'");
						if(L_rstRSSET!=null)
						{
							if (L_rstRSSET.next())
							{
								txtCNSNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
								strCNSDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
										 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
										 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
										 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
							 //if(txtDSTCD.getText().length()==0)
								txtDSTCD.setText(getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
								setMSG("",'N');
								L_rstRSSET.close();
							}
							else 
								setMSG("Invalid Consignee Code ..",'E');
						}
						else
							setMSG("Invalid Consignee Code ..",'E');
					}
					else
					{
						setMSG("",'N');
						strCNSDT=strBYRDT;
					}
				}
				else if(input==txtTRPCD)
				{
					if(txtTRPCD.getText().length() !=5)
					    return true;
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtTRPCD.getText()+"' and pt_PRTTP='T' and upper(isnull(PT_STSFL,' ')) <> 'X'");
					if(L_rstRSSET!=null)
					{
						if (L_rstRSSET.next())
						{
							txtTRPNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
							strTRPDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
							setMSG("",'N');
							L_rstRSSET.close();
						}
						else 
							setMSG("Invalid Transporter Code ..",'E');
					}
					else
						setMSG("Invalid Transporter Code ..",'E');
				
				}
				else if(input==txtDSTCD)
				{
					txtDSTCD.setText(txtDSTCD.getText().toUpperCase());
					ResultSet L_rstRSSET=null;
					//System.out.println(txtINDNO.getText().toString().subString(0,1));
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
					{
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("12"))
							L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_cdtrn where cmt_codcd='"+txtDSTCD.getText()+"' and CMT_CGSTP='COXXDST'");
						else
							L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_cdtrn where cmt_codcd='"+txtDSTCD.getText()+"' and CMT_CGSTP='MRXXPOD'");
					}
					if(L_rstRSSET!=null)
					{
						if (L_rstRSSET.next())
						{
							txtDSTNM.setText(getRSTVAL(L_rstRSSET,"CMT_CODDS","C"));
							L_rstRSSET.close();
							setMSG("Valid Destination ..",'N');
						}
						else 
							setMSG("Invalid Destination Code ..",'E');
					}
					else
						setMSG("Invalid Destination Code ..",'E');
				}
				else if(input==cmbINDNO)
				{
					if(cmbINDNO.getSelectedIndex()==0)
					{
						setMSG("Select Disrtibutor Series ..",'E');
						cmbINDNO.showPopup();
					}
					else
						setMSG("",'N');
				}
				else if(input==txtCURCD)
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP) && txtCURCD.equals("01"))
							{setMSG("Enter Currnecy",'E'); return false;}
					if(txtCURCD.getText().toString().length()==0)
						{setMSG("Enter Currnecy",'E'); return false;}
					if(!hstCDTRN.containsKey("MSTCOXXCUR"+txtCURCD.getText()))
						{setMSG("Enter Currnecy",'E'); return false;}
						
				}
				else if(input==txtORDDT||input==txtREGBY||input==txtREGDT||input==txtAPDNO
						||input==txtLCNO||input==txtFORNO||input==txtDEFNO||input==txtECHRT||input==txtCURCD)
				{
					if(((JTextField)input).getText().length()==0)
						setMSG("Neccessary information..Please enter data ..",'E');
					else
						setMSG("",'N');
				}
				
				else if(input==txtINDNO)
				{

					strWHRSTR =  "IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MKTTP = '" +strMKTTP+"' and "
								 +"IN_INDNO = '" +cmbINDNO.getSelectedItem().toString()+txtINDNO.getText()+"'";
					flgCHK_EXIST =  chkEXIST("MR_INMST", strWHRSTR);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
					{
							setMSG("Indent Record alreay exists",'E'); return false;
					}
					else if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && !flgCHK_EXIST)
					{
							setMSG("Indent Record does not  exist",'E'); return false;
					}
					setMSG("",'N');
				}
				if(M_flgERROR)
					return false;
				return true;
			}catch (Exception e){setMSG(e,"INPVF");return false;}
		}
	}


/**
 */
private boolean chkINDPK(int P_intROWID,int P_intCOLID)
{
		if(!getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()).equalsIgnoreCase("99"))
		{
			float L_fltPKGWT = Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN));
			float L_fltPKGNO = (Float.parseFloat(((JTextField)tblGRDTL.cmpEDITR[intTB1_REQQT]).getText().toString())/L_fltPKGWT);
			L_fltPKGNO=Float.parseFloat(setNumberFormat(L_fltPKGNO,3));
			int L_intPKGNO = new Float(L_fltPKGNO).intValue();
			if(L_intPKGNO != L_fltPKGNO)
				{setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_fltPKGWT,3),'E'); return false;}
			tblGRDTL.setValueAt(setNumberFormat(L_intPKGNO,0),P_intROWID,intTB1_INDPK);
		}
		else
			tblGRDTL.setValueAt(setNumberFormat(Double.parseDouble(tblGRDTL.getValueAt(P_intROWID,intTB1_REQQT).toString())*1000,0),P_intROWID,intTB1_INDPK);
	return true;
}



/**
 */
private boolean chkINDPK1()
{
		if(!getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText()).equalsIgnoreCase("99"))
		{
			float L_fltPKGWT = Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText()),"CMT_NCSVL",hstCDTRN));
			
			float L_fltPKGNO = (Float.parseFloat(((JTextField)tblGRDTL.cmpEDITR[intTB1_REQQT]).getText().toString())/L_fltPKGWT);
			L_fltPKGNO=Float.parseFloat(setNumberFormat(L_fltPKGNO,3));
			int L_intPKGNO = new Float(L_fltPKGNO).intValue();
			if(L_intPKGNO != L_fltPKGNO)
				{setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_fltPKGWT,3),'E'); return false;}
			tblGRDTL.setValueAt(setNumberFormat(L_intPKGNO,0),tblGRDTL.getSelectedRow(),intTB1_INDPK);
		}
		else
			tblGRDTL.setValueAt(((JTextField)tblGRDTL.cmpEDITR[intTB1_REQQT]).getText(),tblGRDTL.getSelectedRow(),intTB1_INDPK);
	return true;
}


	/**Method to display detailed address of a party<br>Fires query to DB and displays details in a separate window	 */
	private void dspADDR(Component P_cmpSOURC,String P_strPRTCD,String P_strPRTTP)
	{
		try
		{
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY3("Select PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='"+P_strPRTTP+"' and PT_PRTCD='"+P_strPRTCD+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ");
			if(L_rstRSSET!=null)
				if(L_rstRSSET.next())
				{
					JOptionPane.showMessageDialog(P_cmpSOURC,getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
																+getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
																+getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
																+getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n");
					if(P_cmpSOURC instanceof JTextField)
						((JTextField)P_cmpSOURC).setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
					L_rstRSSET.close();
				}
		}
		catch(Exception e){setMSG(e,"dspADDR");}
	}
	
	

	/**
	 */
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(getSource()==tblGRDTL)
				{	
					if(P_intCOLID==intTB1_PRDCD)
					{
						if(getSource().getValueAt(P_intROWID,P_intCOLID).toString().length()==0)
							if(!((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")))
								//if(!strMKTTP.equals("02"))
									return true;
						if(!hstPRMST.containsKey(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText()))
							{setMSG("Invalid Product Code",'E'); return false;}
						//if(((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")))
						//	{
								//if(Double.parseDouble(getOPSTK(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString(),getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()),"PR_AVGRT"))<=0)
								//	{
								//		setMSG("Rate not available",'E'); return false;
								//	}
								//tblGRDTL.setValueAt(getOPSTK(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString(),getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()),"PR_AVGRT"),P_intROWID,intTB1_BASRT);
						//		tblGRDTL.setValueAt(getCDTRN("SYSMR00EUS"+"99","CMT_CODDS",hstCDTRN),P_intROWID,intTB1_EUSCD);
						//		tblGRDTL.cmpEDITR[intTB1_BASRT].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_EUSCD].setEnabled(false);
						//		tblGRDTL.cmpEDITR[intTB1_CDCVL].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_DDCVL].setEnabled(false);
							//	tblGRDTL.cmpEDITR[intTB1_TDCVL].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_TDCRF].setEnabled(false);
						//	}
						return true;
					}
					// Package Type is displayed in Description form
					// getCODCD("SYSFGXXPKG"+.....) is used to arrive at PKGTP code 
					if(P_intCOLID==intTB1_REQQT)
					{
						//blank row
						if(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString().length()==0)
							return true;
						//Mode other than Addition/Modification
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							return true;
						// reqqt < invqt  and invqt > 0
						if(Double.parseDouble(tblGRDTL.getValueAt(P_intROWID,intTB1_REQQT).toString()) < Double.parseDouble(getGRDTL(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()).toString(),"GRD_INVQT")) && Double.parseDouble(getGRDTL(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()),"GRD_INVQT"))>0)
							{setMSG("Qty. "+getGRDTL(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()),"GRD_INVQT")+"MT  has been despatched",'E'); return false;}
						// reqqt < dorqt and  dorqt > 0
						if(Double.parseDouble(tblGRDTL.getValueAt(P_intROWID,intTB1_REQQT).toString()) < Double.parseDouble(getGRDTL(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()).toString(),"GRD_DORQT")) && Double.parseDouble(getGRDTL(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()),"GRD_DORQT"))>0)
							{setMSG("D.O. is prepared for "+getGRDTL(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()),"GRD_DORQT")+"MT ",'E'); return false;}
						// If pkg.type not available																																																			 
						if(!hstCODDS.containsKey("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()))
							{setMSG("Package Type Not Entered",'E'); return false;}
						//Addition / Modification mode    Req.Qty >0 But qty. not in line with Pkg.Wt.
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							if(Float.parseFloat(nvlSTRVL(tblGRDTL.getValueAt(P_intROWID,intTB1_REQQT).toString(),"0"))>0.00)
								//if(!chkINDPK1())
								//	return false;
								if(!chkINDPK(P_intROWID,P_intCOLID))
									return false;
						return true;
					}
					else if(P_intCOLID==intTB1_EUSCD)
						{
							if(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString().length()==0)
								return true;
							if(hstCODDS.containsKey("SYSMR00EUS"+((JTextField)tblGRDTL.cmpEDITR[intTB1_EUSCD]).getText().toString()))
							   return true;
							else if(hstCDTRN.containsKey("SYSMR00EUS"+((JTextField)tblGRDTL.cmpEDITR[intTB1_EUSCD]).getText().toString()))
								{tblGRDTL.setValueAt(getCDTRN("SYSMR00EUS"+((JTextField)tblGRDTL.cmpEDITR[intTB1_EUSCD]).getText().toString(),"CMT_CODDS",hstCDTRN),P_intROWID,intTB1_EUSCD); return true;}
							else
							{setMSG("Invalid Usage Type",'E'); return false;}
						}
					else if(P_intCOLID==intTB1_PKGTP)
						{
							String L_strPRDCD = "", L_strPKGTP = "";
							if(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString().length()==0)
								return true;
							if(hstCODDS.containsKey("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()))
							{
								System.out.println("M_strSBSCD : "+M_strSBSCD);
								System.out.println("SALTP : "+cmbSALTP.getSelectedItem().toString());
								if(((M_strSBSCD.substring(0,2).equals("51") || M_strSBSCD.substring(0,2).equals("52")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("16")))
									{
										//System.out.println("PRDCD : "+((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString());
										//System.out.println("PKGDS : "+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString());
										
										L_strPRDCD = ((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString();
										L_strPKGTP = ((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString();
										if(L_strPKGTP.length()>2)
											L_strPKGTP = getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString());
										//System.out.println("001"+L_strPRDCD+ "/"+L_strPKGTP);
										if(Double.parseDouble(getOPSTK(L_strPRDCD,L_strPKGTP,"OP_AVGRT"))<=0)
											{
												setMSG("Rate not available",'E'); return false;
											}
										tblGRDTL.setValueAt(getOPSTK(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString(),getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()),"OP_AVGRT"),P_intROWID,intTB1_BASRT);
										tblGRDTL.setValueAt(getCDTRN("SYSMR00EUS"+"99","CMT_CODDS",hstCDTRN),P_intROWID,intTB1_EUSCD);
										tblGRDTL.cmpEDITR[intTB1_BASRT].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_EUSCD].setEnabled(false);
										tblGRDTL.cmpEDITR[intTB1_CDCVL].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_DDCVL].setEnabled(false);
									//	tblGRDTL.cmpEDITR[intTB1_TDCVL].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_TDCRF].setEnabled(false);
									}

							   return true;
							}
							else if(hstCDTRN.containsKey("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()))
								{tblGRDTL.setValueAt(getCDTRN("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString(),"CMT_CODDS",hstCDTRN),P_intROWID,intTB1_PKGTP); return true;}
							else
							{setMSG("Invalid Package Type",'E'); return false;}
						}
						
						/* else if(P_intCOLID == intTB1_DELTP)
						{
						  String  L_strTEMP = tblGRDTL.getValueAt(P_intROWID,P_intCOLID).toString();
                            if(L_strTEMP.length() >0)						    
                              if(!L_strTEMP.equals("I"))
						        if(!L_strTEMP.equals("S"))
						        {
						            setMSG("Enter I : For Immediate or S : for Schedule..",'E');
						            return false;
						        }
						}*/
		
					}
					else if(getSource() == tblCOTAX)
					{
						if(P_intCOLID == intTB2_TAXCD)
						{
							if(!hstCDTRN.containsKey("SYSCOXXTAX"+((JTextField)tblCOTAX.cmpEDITR[intTB2_TAXCD]).getText()))
								{setMSG("Invalid Tax Code",'E'); return false;}
							else if(hstGRTAX_TAX.containsKey(((JTextField)tblCOTAX.cmpEDITR[intTB2_TAXCD]).getText()))
								{setMSG("Tax code already exist in Gradewise Tax Detail",'E'); return false;}
						}
						else if(P_intCOLID == intTB2_AMTFL)
						{
							if(!((JTextField)tblCOTAX.cmpEDITR[intTB2_AMTFL]).getText().equals("A") && !((JTextField)tblCOTAX.cmpEDITR[intTB2_AMTFL]).getText().equals("P") && !((JTextField)tblCOTAX.cmpEDITR[intTB2_AMTFL]).getText().equals("X"))
								{setMSG("Enter A: for Amount   P: for %age",'E'); return false;}  else return true;
						}
					}
					else if(getSource() == tblGRTAX)
					{
						if(P_intCOLID == intTB3_TAXCD)
						{
							if(!hstCDTRN.containsKey("SYSCOXXTAX"+((JTextField)tblGRTAX.cmpEDITR[intTB3_TAXCD]).getText()))
								{setMSG("Invalid Tax Code",'E'); return false;}
							else if(hstCOTAX_TAX.containsKey(((JTextField)tblGRTAX.cmpEDITR[intTB3_TAXCD]).getText()))
								{setMSG("Tax code already exist in Common Tax Detail",'E'); return false;}
						}
						else if(P_intCOLID == intTB3_PRDCD)
						{
							int i = 0;
							boolean L_flgRETFL = false;
							for(i=0;i<tblGRDTL.getRowCount();i++)
								{
									String L_GRTAX_PRDCD = tblGRDTL.getValueAt(i,intTB3_PRDCD).toString();
									if(L_GRTAX_PRDCD.length()<10)
										break;
									if(L_GRTAX_PRDCD.equals(((JTextField)tblGRTAX.cmpEDITR[intTB3_PRDCD]).getText()))
										{L_flgRETFL = true; tblGRTAX.setValueAt(getPRMST(((JTextField)tblGRTAX.cmpEDITR[intTB3_PRDCD]).getText(),"PR_PRDDS"),tblGRTAX.getSelectedRow(),intTB3_PRDDS); break;}
								}
							if(!L_flgRETFL)
								{setMSG("This Grade is not available in Grade Wise Qty. Entry",'E'); return false;}
						}
						else if(P_intCOLID == intTB3_AMTFL)
						{
							if(!((JTextField)tblGRTAX.cmpEDITR[intTB3_AMTFL]).getText().equals("A") && !((JTextField)tblGRTAX.cmpEDITR[intTB3_AMTFL]).getText().equals("P"))
								{setMSG("Enter A: for Amount   P: for %age",'E'); return false;}  else return true;
						}
					}
					else if(getSource() == tblDLSCH)
					{
					    if(P_intCOLID == intTB5_DELTP)
						{
						  String  L_strTEMP = tblDLSCH.getValueAt(P_intROWID,P_intCOLID).toString();
                            if(L_strTEMP.length() >0)						    
                              if(!L_strTEMP.equals("I"))
						        if(!L_strTEMP.equals("S"))
						        {
						            setMSG("Enter I : For Immediate or S : for Schedule..",'E');
						            return false;
						        }
						     if(L_strTEMP.equals("I"))
						     {
						        // changed on 05/07/2006, default del. date to be shown only in Addition
						        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
						        {
    						        M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
    	                            M_calLOCAL.add(Calendar.DATE,1);
    		                        // Changed on 08/11/2006 , current date for immediate delivery api HBP
                                    //tblDLSCH.setValueAt(M_fmtLCDAT.format(M_calLOCAL.getTime()),P_intROWID,intTB5_DELDT);
                                    tblDLSCH.setValueAt(cl_dat.M_strLOGDT_pbst,P_intROWID,intTB5_DELDT);
		                        }
						     }
						}
						if(P_intCOLID == intTB5_DELDT)
						{
                 		    String  L_strTEMP = tblDLSCH.getValueAt(P_intROWID,P_intCOLID).toString();
                 		    boolean vldAPPL = false;
                 		    if(L_strTEMP.length() >0)
                 		        vldAPPL = true;
                 		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
                 		    {
                 		        // if date has been changed in modification mode then only validation is applicable
                                if((L_strTEMP.length() >0) && nvlSTRVL(tblDLSCH.getValueAt(P_intROWID,intTB5_ORGDT).toString(),"").length() >0)
						        if(M_fmtLCDAT.parse(tblDLSCH.getValueAt(P_intROWID,intTB5_ORGDT).toString()).compareTo(M_fmtLCDAT.parse(L_strTEMP)) != 0)       		    
						        {
						            vldAPPL = true;          
						        }
						        else
						            vldAPPL = false;          
						    }
                 		    if(vldAPPL) // If date validation is applicable
						    if(M_fmtLCDAT.parse(tblDLSCH.getValueAt(P_intROWID,intTB5_DELDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
            				{
            					setMSG("Delivery Date can not be less than current Date..",'E');
                            	return false;
            				}
						    if(P_intROWID == 0)
						      tblDLSCH.setValueAt("01",P_intROWID ,intTB5_SRLNO);
						    else
						    {
						        
						        if(!(tblDLSCH.getValueAt(P_intROWID - 1,intTB5_PRDCD).toString()+tblDLSCH.getValueAt(P_intROWID - 1,intTB5_PKGTP).toString()).equals((tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString())+tblDLSCH.getValueAt(P_intROWID,intTB5_PKGTP).toString()))
						         {  
						            // check the max no. entered for this grade
						           // tblDLSCH.setValueAt("01",P_intROWID ,intTB5_SRLNO);
						            tblDLSCH.setValueAt(getSRLNO(P_intROWID),P_intROWID ,intTB5_SRLNO);
						         }
						        else
						        {
						            if(tblDLSCH.getValueAt(P_intROWID -1 ,intTB5_DELTP).toString().equals("I"))
    						        {
    						            setMSG("Duplicate entry for Immediate delivery..",'E');
    						            return false;
    						        } 
                                    if(tblDLSCH.getValueAt(P_intROWID -1 ,intTB5_DELTP).toString().equals("S"))
    						        {
    						            if(L_strTEMP.equals(tblDLSCH.getValueAt(P_intROWID -1 ,intTB5_DELDT).toString()))
    						            {
    						                setMSG("Delivery already scheduled for "+L_strTEMP +"..",'E');
    						                return false;
    						            }
    						        }    
                                if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
                                {
						           String L_strSRLNO = tblDLSCH.getValueAt(P_intROWID -1 ,intTB5_SRLNO).toString(); 
						           L_strSRLNO = Integer.toString((Integer.parseInt(L_strSRLNO)+1));
						           if(L_strSRLNO.length() == 1)
						            L_strSRLNO ="0"+L_strSRLNO;
						                tblDLSCH.setValueAt(L_strSRLNO,P_intROWID,intTB5_SRLNO);
                                }
                                else
						        {
						            if(tblDLSCH.getValueAt(P_intROWID,intTB5_SRLNO).toString().equals(""))
						              tblDLSCH.setValueAt(getSRLNO(P_intROWID),P_intROWID ,intTB5_SRLNO);
						        }
                             }
						    }
                     	}
						if(P_intCOLID == intTB5_DELQT)
						{
    						  double L_dblGRDQT=0.0,L_dblTDLQT=0.0,L_dblSCHQT=0.0;
    		                  if(tblDLSCH.getValueAt(P_intROWID,intTB5_DELQT).toString().equals(""))
    		                    return true;	
    		                  String L_strDELTP =tblDLSCH.getValueAt(P_intROWID,intTB5_DELTP).toString();
    		                  String L_strPRDCD =tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString();   			  
    		                  String L_strPKGTP =tblDLSCH.getValueAt(P_intROWID,intTB5_PKGTP).toString();   			  
    		                  String L_strPRDDS =tblDLSCH.getValueAt(P_intROWID,intTB5_PRDDS).toString();   			  
    						  for(int i=0;i<tblGRDTL.getRowCount();i++)
                             {
                                if((tblGRDTL.getValueAt(i,intTB1_PRDCD).toString()+tblGRDTL.getValueAt(i,intTB1_PKGTP).toString()).equals(tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString()+tblDLSCH.getValueAt(P_intROWID,intTB5_PKGTP).toString()))        
                                {
                                    L_dblGRDQT = Double.parseDouble(tblGRDTL.getValueAt(i,intTB1_REQQT).toString());         
                                }
                             }
    						  if(L_strDELTP.equals("I"))
    						  {
    						     if(Double.parseDouble(tblDLSCH.getValueAt(P_intROWID,intTB5_DELQT).toString()) != L_dblGRDQT)
                                { 
                                    setMSG("Schedule Quantity does not match with order quantity..",'E');
                                       
                                    return false;
                                }
    						  }
						//else if(Float.parseFloat(setNumberFormat(L_fltDODQT,3)) > Float.parseFloat(setNumberFormat(L_fltDOTQT,3)))
						//	if(setNumberFormat(L_fltDOTQT,3)==setNumberFormat(getDOD_RUNQT(tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PRDCD).toString(),tblDODEL.getValueAt(tblDODEL.getSelectedRow(),intTB2_PKGTP).toString(),tblDODEL.getSelectedRow()),3))
							  
    						  else if(tblDLSCH.getValueAt(P_intROWID,intTB5_DELTP).toString().equals("S"))
    						  {
                                  L_dblSCHQT = getTOTQT(P_intROWID);
    						      if(Double.parseDouble(setNumberFormat(L_dblSCHQT,3)) > Double.parseDouble(setNumberFormat(L_dblGRDQT,3)))
    						      {
    						        setMSG("Schdule Quantity can not exceed Order quantity..",'E');
    						        return false;
    						      }
                                  else if(L_dblSCHQT < L_dblGRDQT)
    						      {
    						          // changed on 05/07/2006, default del. date to be shown only in Addition
        						        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
        						        {
            						        tblDLSCH.setValueAt(setNumberFormat((L_dblGRDQT - L_dblSCHQT),3),P_intROWID + 1,intTB5_DELQT);
                                            tblDLSCH.setValueAt(tblDLSCH.getValueAt(P_intROWID,intTB5_DELTP),P_intROWID + 1,intTB5_DELTP);
                                            tblDLSCH.setValueAt(tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD),P_intROWID + 1,intTB5_PRDCD);
                                            tblDLSCH.setValueAt(tblDLSCH.getValueAt(P_intROWID,intTB5_PRDDS),P_intROWID + 1,intTB5_PRDDS);
                                            tblDLSCH.setValueAt(tblDLSCH.getValueAt(P_intROWID,intTB5_PKGTP),P_intROWID + 1,intTB5_PKGTP);
                                            tblDLSCH.setValueAt("",P_intROWID + 1,intTB5_SRLNO);
                                            //return false;
        						        }
						          }
    						  }
						}
					}
					
				return true;
				
			}catch(Exception e)
			{
				setMSG(e,"TableInputVerifier");
				setMSG("Invalid Data ..",'E');				
				return false;
			}
		}
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
        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl not in ('X') and SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54','SX','68','66') order by pr_prdds";
        System.out.println(L_strSQLQRY);
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
        if(L_rstRSSET == null || !L_rstRSSET.next())
        {
		     //setMSG("Product Records not found in CO_PRMST",'E');
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
	


/** One time data capturing for Product Master
*	into the Hash Table
*/
 private void crtOPSTK()
{
	String L_strSQLQRY = "";
    try
    {
        hstOPSTK.clear();
        L_strSQLQRY = "select OP_PRDCD,PR_PRDDS,OP_PKGTP,isnull(OP_AVGRT,0) OP_AVGRT from FG_OPSTK,CO_PRMST where op_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and OP_PRDCD = PR_PRDCD  and SUBSTRING(op_prdcd,1,2) in ('51','52','53','54','SX','68','66') order by pr_prdds";
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
        if(L_rstRSSET == null || !L_rstRSSET.next())
        {
		     //setMSG("Product Records not found in CO_PRMST",'E');
              return;
        }
        while(true)
        {
                strPRDCD = getRSTVAL(L_rstRSSET,"OP_PRDCD","C");
                strPKGTP = getRSTVAL(L_rstRSSET,"OP_PKGTP","C");
                String[] staOPSTK = new String[intOPSTK_TOT];
                staOPSTK[intAE_OP_PRDDS] = getRSTVAL(L_rstRSSET,"PR_PRDDS","C");
                staOPSTK[intAE_OP_AVGRT] = getRSTVAL(L_rstRSSET,"OP_AVGRT","N");
                hstOPSTK.put(strPRDCD+strPKGTP,staOPSTK);
                System.out.println(" hstOPSTK.put "+strPRDCD+strPKGTP+" / "+staOPSTK[intAE_OP_PRDDS].toString()+staOPSTK[intAE_OP_AVGRT].toString());
                if (!L_rstRSSET.next())
                        break;
        }
        L_rstRSSET.close();
    }
    catch(Exception L_EX)
    {
           setMSG(L_EX,"crtOPSTK");
    }
return;
}
	
	
		/** One time data capturing for Distributors
		 * into the Hash Table
		 */
		 private void crtPTMST()
		        {
					String L_strSQLQRY = "";
		            try
		            {
		                hstPTMST.clear();
		                L_strSQLQRY = "select PT_PRTTP + PT_PRTCD PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 from co_ptmst where pt_stsfl<> 'X' and pt_prttp + pt_prtcd in (select cmt_modls + cmt_chp02 from co_cdtrn where cmt_cgmtp = 'D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('MR01IND','MR02IND','MR03IND','MR04IND','MR05IND','MR07IND','MR12IND'))";
		                ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
		                if(L_rstRSSET == null || !L_rstRSSET.next())
		                {
		                     setMSG("Distributors not found in CO_PTMST",'E');
		                      return;
		                }
		                while(true)
		                {
		                        strPRTCD = getRSTVAL(L_rstRSSET,"PT_PRTCD","C");
		                        String[] staPTMST = new String[intPTMST_TOT];
		                        staPTMST[intAE_PT_PRTNM] = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
		                        staPTMST[intAE_PT_ZONCD] = getRSTVAL(L_rstRSSET,"PT_ZONCD","C");
		                        staPTMST[intAE_PT_SHRNM] = getRSTVAL(L_rstRSSET,"PT_SHRNM","C");
		                        staPTMST[intAE_PT_ADR01] = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
		                        staPTMST[intAE_PT_ADR02] = getRSTVAL(L_rstRSSET,"PT_ADR02","C");
		                        staPTMST[intAE_PT_ADR03] = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
		                        hstPTMST.put(strPRTCD,staPTMST);
		                        if (!L_rstRSSET.next())
		                                break;
		                }
		                L_rstRSSET.close();
		            }
		            catch(Exception L_EX)
		            {
		                   setMSG(L_EX,"crtPTMST");
		            }
		return;
		}
	
	
	
	
		/** One time data capturing for specified codes from CO_CDTRN
		 * into the Hash Table
		 */
         private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
        {
			String L_strSQLQRY = "";
            try
            {
		        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")   "+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
			  //System.out.println(L_strSQLQRY);
		        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
                if(L_rstRSSET == null || !L_rstRSSET.next())
                {
		            //setMSG("Records not found in CO_CDTRN",'E');
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
                        staCDTRN[intAE_CMT_MODLS] = getRSTVAL(L_rstRSSET,"CMT_MODLS","C");
                        LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
				//if(strCGSTP.equals("MRXXPOD"))
				//	System.out.println("Adding : "+strCGMTP+strCGSTP+strCODCD);
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
						LP_VTRNM.addElement(getCDTRN(L_strCODCD,LP_FLDNM, hstCDTRN));
						//System.out.println("adding to Vector : "+getCDTRN(L_strCODCD,LP_FLDNM, hstCDTRN));
					}
				}
				LP_VTRNM.addElement("Select");
			}
			catch (Exception L_Ex)
			{}
		}
		 
		/**  Specially used for populating combo elements in Customer Order Series
		 *   Depending on Consignment Stockist / Distributor Category
		 */
		private void setCDLST1(Vector<String> LP_VTRNM, String LP_CODCT, String LP_FLDNM)
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
						String L_strCHP02 = getCDTRN(L_strCODCD,"CMT_CHP02", hstCDTRN);
						String L_strMODLS = getCDTRN(L_strCODCD,"CMT_MODLS", hstCDTRN);
						//System.out.println(L_strMODLS+" / "+lblDSRTP.getText());
						if(L_strMODLS.equals(lblDSRTP.getText()))
							LP_VTRNM.addElement(getCDTRN(L_strCODCD,LP_FLDNM, hstCDTRN));
						//System.out.println("adding to Vector : "+getCDTRN(L_strCODCD,LP_FLDNM, hstCDTRN));
					}
				}
				LP_VTRNM.addElement("Select");
			}
			catch (Exception L_Ex)
			{}
		}
		

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





		/** Picking up Distributor Details
		 * @param LP_PRTCD		Party Code 
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getPTMST(String LP_PRTCD, String LP_FLDNM)
        {
        String L_RETSTR = "";
        try
        {
                String[] staPTMST = (String[])hstPTMST.get(LP_PRTCD);
                if (LP_FLDNM.equals("PT_PRTNM"))
                        L_RETSTR = staPTMST[intAE_PT_PRTNM];
                else if (LP_FLDNM.equals("PT_ZONCD"))
                        L_RETSTR = staPTMST[intAE_PT_ZONCD];
                else if (LP_FLDNM.equals("PT_SHRNM"))
                        L_RETSTR = staPTMST[intAE_PT_SHRNM];
                else if (LP_FLDNM.equals("PT_ADR01"))
                        L_RETSTR = staPTMST[intAE_PT_ADR01];
                else if (LP_FLDNM.equals("PT_ADR02"))
                        L_RETSTR = staPTMST[intAE_PT_ADR02];
                else if (LP_FLDNM.equals("PT_ADR03"))
                        L_RETSTR = staPTMST[intAE_PT_ADR03];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPTMST");
		}
        return L_RETSTR;
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
		



		/** Picking up Product Master Details
		 * @param LP_PRDCD		Product Code 
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
		private String getOPSTK(String LP_PRDCD,String LP_PKGTP, String LP_FLDNM)
		{
			String L_RETSTR = "";
			try
			{
			        String[] staOPSTK = (String[])hstOPSTK.get(LP_PRDCD+LP_PKGTP);
			        //System.out.println(LP_PRDCD+LP_PKGTP);
			        if (LP_FLDNM.equals("OP_PRDDS"))
			                L_RETSTR = staOPSTK[intAE_OP_PRDDS];
			        else if (LP_FLDNM.equals("OP_AVGRT"))
			                L_RETSTR = staOPSTK[intAE_OP_AVGRT];
			}
			catch (Exception L_EX)
			{
				setMSG(L_EX,"getOPSTK");
			}
			if(L_RETSTR.trim().length()==0)
			   L_RETSTR = "0.000";
			//System.out.println("getOPSTK : "+L_RETSTR);
			return L_RETSTR;
		}


		/** Picking up Specified details from Indent Transaction Entry
		 * @param LP_CDTRN_KEY	Code Transaction key
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getGRDTL(String LP_GRDTL_KEY, String LP_FLDNM)
        {
		//System.out.println("getGRDTL : "+LP_GRDTL_KEY+"/"+LP_FLDNM);
        try
        {
				if(!hstGRDTL.containsKey(LP_GRDTL_KEY))
					return "0.00";
                if (LP_FLDNM.equals("GRD_REQQT"))
                        return ((String[])hstGRDTL.get(LP_GRDTL_KEY))[intAE_GRD_REQQT];
                else if (LP_FLDNM.equals("GRD_INDQT"))
                        return ((String[])hstGRDTL.get(LP_GRDTL_KEY))[intAE_GRD_INDQT];
                else if (LP_FLDNM.equals("GRD_DORQT"))
                        return ((String[])hstGRDTL.get(LP_GRDTL_KEY))[intAE_GRD_DORQT];
                else if (LP_FLDNM.equals("GRD_INVQT"))
                        return ((String[])hstGRDTL.get(LP_GRDTL_KEY))[intAE_GRD_INVQT];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getGRDTL");
		}
        return "";
        }


		/**  Returning code value for specified code description
		 */
		private String getCODCD(String LP_CODDS_KEY)		
		{
			if(!hstCODDS.containsKey(LP_CODDS_KEY))
				return "";
			return hstCODDS.get(LP_CODDS_KEY).toString();
		}

		
		/** Picking up Specified Codes Transaction related details from Hash Table
		 * <B> for Specified Code Transaction key
		 * @param LP_CDTRN_KEY	Code Transaction key
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
        {
		//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
        try
        {
				if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
					{setMSG(LP_CDTRN_KEY+" not found in CO_CDTRN hash table",'E'); return " ";}
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
                else if (LP_FLDNM.equals("CMT_MODLS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_MODLS];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return "";
        }
		 

	/** Displaying unauthorised indents in a separate window
	 */	
	private void exeINDDSP()
	{
		try
			{
				if(!strMKTTP.equals("01") && !strMKTTP.equals("04") && !strMKTTP.equals("05"))
					return;
				ResultSet L_rstRSSET = null;
				if(pnlINDDSP==null)
				{
					//pnlINDDSP=new JPanel(null);
					//tblINDDSP = crtTBLPNL1(pnlINDDSP,new String[]{"FL","Ind.No.","Date","Amd.No.","Amd.Date","Grade","Ind.Qty.","Aut.Qty.","Bkd.By","Customer"},100,0,1,6,8,new int[]{20,100,70,30,70,80,60,60,30,100},new int[]{0});
					tblINDDSP = crtTBLPNL1(pnlINDDSP=new JPanel(null),new String[]{"FL","Ind.No.","Date","Amd.No.","Amd.Date","Grade","Ind.Qty.","Aut.Qty.","Bkd.By","Customer"},100,0,1,6,5,new int[]{20,100,70,30,70,80,60,60,30,120},new int[]{0});
				}
				pnlINDDSP.setSize(600,150);
				pnlINDDSP.setPreferredSize(new Dimension(650,200));
				//L_rstRSSET=cl_dat.exeSQLQRY2("Select IN_MKTTP,IN_INDNO,IN_BKGDT, IN_AMDNO, IN_AMDDT, INT_PRDDS, INT_REQQT,INT_INDQT, IN_BKGBY,PT_PRTNM from VW_INTRN,CO_PTMST where IN_BYRCD = PT_PRTCD and PT_PRTTP='C' and INT_STSFL='0' and INT_REQQT>0 and IN_MKTTP in ('01','04','05') and in_sbscd in "+M_strSBSLS+" order by IN_BKGDT desc,IN_INDNO asc");
				L_rstRSSET=cl_dat.exeSQLQRY2("Select IN_MKTTP,IN_INDNO,IN_BKGDT, IN_AMDNO, IN_AMDDT, INT_PRDDS, INT_REQQT,(isnull(int_indqt,0)-isnull(int_fcmqt,0)) INT_INDQT, IN_BKGBY,IN_BYRNM from VW_INTRN where INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND INT_STSFL='0' and INT_REQQT>0 and IN_MKTTP in ('01','04','05') and int_sbscd in "+M_strSBSLS+" order by IN_BKGDT desc,IN_INDNO asc");
				if(L_rstRSSET==null || !L_rstRSSET.next())
					{setMSG("No unauthorised Customer Orders",'E');}	
				int i=0;
				while(true)
				{
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"IN_MKTTP","C")+" / "+getRSTVAL(L_rstRSSET,"IN_INDNO","C"),i,intTB4_INDNO);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"IN_BKGDT","D"),i,intTB4_BKGDT);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"IN_AMDNO","C"),i,intTB4_AMDNO);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"IN_AMDDT","D"),i,intTB4_AMDDT);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"INT_PRDDS","C"),i,intTB4_PRDDS);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"INT_REQQT","N"),i,intTB4_REQQT);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"INT_INDQT","N"),i,intTB4_INDQT);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"IN_BKGBY","C"),i,intTB4_BKGBY);
					//tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"),i,intTB4_PRTNM);
					tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"IN_BYRNM","C"),i,intTB4_PRTNM);
					i++;
					if(!L_rstRSSET.next())
						break;
				}
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlINDDSP,"Unauthorised Customer Orders",JOptionPane.OK_CANCEL_OPTION);
				chkINDDSP.requestFocus();
			}
		catch(Exception e)
		{setMSG(e,"exeINDDSP");}
	}
		
		

	
	/** Clearing table contents for re-defining common taxes
	 */	
	private void exeREDTAX(String LP_SBSTP,String LP_INDNO,String LP_CLRMSG)
	{
		try
		{
			if(LP_CLRMSG.equalsIgnoreCase("CLEAR"))
			{
				flgREDTAX = false;
				int L_intOPTN=JOptionPane.showConfirmDialog( this,"Clear Existing Common Tax Entries and Redefine ? ","Message",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN!=0)
					return;
				flgREDTAX = true;
				tblCOTAX.clrTABLE();
			}
			else if(LP_CLRMSG.equalsIgnoreCase("SAVE"))
			{
					M_strSQLQRY = "delete from CO_TXDOC where TX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TX_SYSCD='MR' and TX_SBSTP = '"+LP_SBSTP+"' and TX_DOCTP='IND' and tx_DOCNO = '"+LP_INDNO+"' and TX_PRDCD = 'XXXXXXXXXX'";
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					M_strSQLQRY = "delete from CO_TXSPC where TXT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TXT_SYSCD='MR' and TXT_SBSTP = '"+LP_SBSTP+"' and TXT_DOCTP='IND' and TXT_DOCNO = '"+LP_INDNO+"' and TXT_PRDCD = 'XXXXXXXXXX'";
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					if(!cl_dat.exeDBCMT("exeREDTAX"))
						setMSG("Initialization of old Tax Detail not successful ...",'E');
			}
		}
		catch(Exception e)
		{setMSG(e,"exeREDTAX");}
	}
	
	private double getTOTQT(int P_intROWID)
	{
	    double L_dblTDLQT = 0.0;
	    String L_strPRDCD = tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString();
	    for(int i=0;i< tblDLSCH.getRowCount();i++)
	    {
           if(tblDLSCH.getValueAt(i,intTB5_PRDCD).toString().equals(L_strPRDCD))
                L_dblTDLQT += Double.parseDouble(nvlSTRVL(tblDLSCH.getValueAt(i,intTB5_DELQT).toString(),"0"));        
	    }
	    return L_dblTDLQT;
	}
	
	private String getSRLNO(int P_intROWID)
	{
	    String L_strMAXSR ="00";
	    String L_strSRLNO ="00";
	    String L_strPRDCD = tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString();
	    for(int i=0;i< P_intROWID;i++)
	    {
	       if(tblDLSCH.getValueAt(i,intTB5_PRDCD).toString().equals(L_strPRDCD))
           {
                L_strSRLNO =nvlSTRVL(tblDLSCH.getValueAt(i,intTB5_SRLNO).toString(),"00");
                if(Integer.parseInt(L_strSRLNO) >Integer.parseInt(L_strMAXSR))
                    L_strMAXSR = L_strSRLNO;
           }
	    }
	    L_strSRLNO = Integer.toString(Integer.parseInt(L_strMAXSR) +1);
	    if(L_strSRLNO.length() == 1)
	        L_strSRLNO ="0"+L_strSRLNO;
	    return L_strSRLNO;
	}
	private double getTOTQT(String P_strPRDCD,String P_strPKGTP)
	{
	    double L_dblTDLQT = 0.0;
	    for(int i=0;i< tblDLSCH.getRowCount();i++)
	    {
           if((tblDLSCH.getValueAt(i,intTB5_PRDCD).toString().equals(P_strPRDCD))&&(tblDLSCH.getValueAt(i,intTB5_PKGTP).toString().equals(P_strPKGTP)))
                L_dblTDLQT += Double.parseDouble(tblDLSCH.getValueAt(i,intTB5_DELQT).toString());        
	    }
	    return L_dblTDLQT;
	}
	private void getOVRDUE()
	{
	    try
	    {
	        // Distributor overdue
	        //SELECT SUM(PL_DOCVL - isnull(PL_ADJVL,0)) FROM MR_PLTRN WHERE  PL_DOCVL - isnull(PL_ADJVL,0) >0 AND PL_DUEDT > CURRENT_DATE AND SUBSTRING(PL_DOCTP,1,1) IN('2','3') and pl_prttp||pl_prtcd in(select b.pt_prttp||b.pt_prtcd from co_ptmst b where b.pt_dsrcd ='S0006')
	       /* M_strSQLQRY = "SELECT SUM(PL_DOCVL - isnull(PL_ADJVL,0)) OVERDUE FROM MR_PLTRN "
	                  +"WHERE PL_DOCVL - isnull(PL_ADJVL,0) >0 AND PL_DUEDT < CURRENT_DATE "
	                  +" AND SUBSTRING(PL_DOCTP,1,1) IN('2','3') "
                      + " and pl_prttp||pl_prtcd in(select b.pt_prttp||b.pt_prtcd from co_ptmst b where b.pt_dsrcd ='"+txtDSRCD.getText().trim()+"')";
	        
	        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	        if(M_rstRSSET !=null)
	        {
    	        if(M_rstRSSET.next())
    	        {
    	            lblDSRDUE.setText(nvlSTRVL(M_rstRSSET.getString("OVERDUE"),"0.00"));    
    	        }
    	        M_rstRSSET.close();
	        }	*/
	        double dblOVRDUE =0,dblUNADJ =0;
	         M_strSQLQRY = "SELECT SUM(PL_DOCVL - isnull(PL_ADJVL,0)) OVERDUE FROM MR_PLTRN "
	                  +"WHERE PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_DOCVL - isnull(PL_ADJVL,0) >0 AND PL_DUEDT < CURRENT_DATE "
	                  +" AND SUBSTRING(PL_DOCTP,1,1) IN('2','3') AND PL_PRTTP ='C' and PL_PRTCD ='"+txtBYRCD.getText().trim()+"'"; 
	        //System.out.println(M_strSQLQRY);
	        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	        if(M_rstRSSET !=null)
	        {
    	        if(M_rstRSSET.next())
    	        {
    	            dblOVRDUE = M_rstRSSET.getDouble("OVERDUE");
    	                    //System.out.println("OVRDUE "+dblOVRDUE);
    	            //lblBYRDUE.setText(nvlSTRVL(M_rstRSSET.getString("OVERDUE"),"0.00"));    
    	        }
    	        M_rstRSSET.close();
	        }
	        // receipt and credit notes
	        M_strSQLQRY = "SELECT SUM(PL_DOCVL - isnull(PL_ADJVL,0)) UNADJ FROM MR_PLTRN "
	                  +"WHERE PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PL_DOCVL - isnull(PL_ADJVL,0) >0 "
	                  +" AND SUBSTRING(PL_DOCTP,1,1) IN('0','1') AND PL_PRTTP ='C' and PL_PRTCD ='"+txtBYRCD.getText().trim()+"'"; 
	        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            //System.out.println(M_strSQLQRY);	        
            if(M_rstRSSET !=null)
	        {
    	        if(M_rstRSSET.next())
    	        {
    	            dblUNADJ = M_rstRSSET.getDouble("UNADJ");
    	            //System.out.println("UNADJ "+dblUNADJ);
    	        }
    	        M_rstRSSET.close();
	        }          
	        lblBYRDUE.setText(setNumberFormat(dblOVRDUE - dblUNADJ,0));    
	        //System.out.println("overall "+(dblOVRDUE - dblUNADJ));
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getOVRDUE");
	    }
	}
	/*private boolean vldDELQT(int P_intROWID)
	{
	    String L_strPRDCD,L_strPPRDCD; 
	    String L_strPKGTP="",L_strPPKGTP; 
	    double L_dblDELQT =0.0;
	     double L_dblPDELQT = 0.0 ;
	     double L_dblTDLQT = 0.0;
	     double L_dblGRDQT = 0.0;
	     L_strPRDCD = tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString();
	    for(int i=0;i< tblDLSCH.getRowCount();i++)
	    {
	        L_strPRDCD = tblDLSCH.getValueAt(i,intTB5_PRDCD).toString();
	        if(L_strPRDCD.length() == 0)
	            return true;
	        L_dblDELQT = Double.parseDouble(tblDLSCH.getValueAt(i,intTB5_DELQT).toString());
            if(i > 0)
            {
                L_strPPRDCD = tblDLSCH.getValueAt(i-1,intTB5_PRDCD).toString();
                L_strPPKGTP = tblDLSCH.getValueAt(i-1,intTB5_PKGTP).toString();
                L_dblPDELQT = Double.parseDouble(tblDLSCH.getValueAt(i-1,intTB5_DELQT).toString());
            }
            else
            {
                L_strPPRDCD = L_strPRDCD;
                L_strPPKGTP = L_strPKGTP;
                L_dblPDELQT =0;
            }
            if((L_strPRDCD.equals(L_strPPRDCD))&&(L_strPKGTP.equals(L_strPPKGTP)))
            {   
                L_dblTDLQT += L_dblDELQT ;
                //System.out.println(" Total i = "+i+" :"+L_dblTDLQT);
            }
            else
            {
                 // In case of change of Grade, verify total delivery quantity with hashtable 
                 for(int j=0;j<tblGRDTL.getRowCount();j++)
                 {
                    if(tblGRDTL.getValueAt(j,intTB1_PRDCD).toString().equals(L_strPPRDCD))        
                    {
                        L_dblGRDQT = Double.parseDouble(tblGRDTL.getValueAt(j,intTB1_PRDCD).toString());         
                    }
                 }
                if(L_dblTDLQT != L_dblGRDQT)
                { 
                    setMSG("Quantity can not be greater than the order quantity..",'E');
                    return false;
                }
                L_dblTDLQT = L_dblDELQT ;
            }
	    }
	    return true;
	}*/
		 
	public static void main(String[] a)
	{
		mr_teind mr=new mr_teind();
	}
}
/*update MR_INMST set IN_INDDT = ctod('02/28/2004') , IN_DONO = '' , IN_AMNDNO = '
00' , IN_AMNDDT = ctod('  /  /    ')  , IN_BOOKDT = ctod('02/28/2004') , IN_SALT
YPE = '01' , IN_DTPCD = '01' , IN_DTPDS = 'EX-FACTORY' , IN_PONO = 'CF01' , IN_P
ODATE = ctod('01/01/2001') , IN_ZONE = '02' , IN_PARTSH = 'Y' , IN_CONSIGNEE = '
A0323' , IN_BUYER = 'A0322' , IN_CURRENCY = '01' , IN_EXCHRATE = 0 , IN_DISTRIBT
R = 'V0003'  where IN_PRTYPE = '01' and IN_INDNO = 'NVE40002'
update MR_INMST set IN_FAXNO = '' , IN_FAXPLACE = '' , IN_PMTACCT = 0 , IN_PMTCU
ST = 0 , IN_PMTTYPE = '01' , IN_STXCD = 'STX' , IN_STXRT = 0 , IN_OCTCD = 'OCT'
, IN_OCTRT = 0 , IN_SVCCD = '' , IN_SVCRT = 0 , IN_STATUS = 'U' , IN_LUID = 'SYS
' , IN_LUPD = date() where IN_PRTYPE = '01' and IN_INDNO = 'NVE40002'
insert into MR_INTRN (INT_PRTYPE,INT_INDNO,INT_GRADECD,INT_PRODCD,INT_PKGTYPE,IN
T_AMNDNO,INT_PKGS,INT_PKGWT,INT_INDQTY,INT_SUOM,INT_OUOM,INT_EUSCD,INT_CNVFACT,I
NT_AUTHQTY,INT_BASRATE,INT_RATEPER,INT_STATUS) values ('01','NVE40002','SC201E',
'SC','16','',0,0,0,'MT','MT','01',0,0,0,0,'')
update MR_INTRN set INT_AUTHBY = '' , INT_DOQTY = 0 , INT_FCQTY = 0 , INT_EXCDUT
Y = 0 , INT_CDCNT = 0 , INT_DDCNT = 0 , INT_TDCNT = 0 , INT_TDCONS = '' , INT_LU
ID = 'SYS' , INT_LUPD = date() where INT_PRTYPE = '01' and INT_INDNO = 'NVE40002
' and INT_GRADECD = 'SC201E'
*/
