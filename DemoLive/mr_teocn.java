/** Program Name : MR_TEOCN
 *  Purpose : Capturing & Authorisation of Order Confirmation Detail 
 *  Author : SRT
 *  Modified by : 
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

class mr_teocn extends cl_pbase implements ChangeListener//,PopupMenuListener
{
	private Object objITMRF;	/**Object to transefer data to FoxPro*/
	//private mr_hkitr omr_hkitr;/**Scroll pane for distributor series detail list*/
	
								/**Flag to indicate that ammendment is to be raised during modification	 */
	private boolean flgAMDFL;	/**Flag to remember whether user has autothorisation rights */
	private boolean flgAUTRN;	/**Flag to indicate whether remark was added previously */
	private boolean flgREGRM;	/**String for order no. */
	//private boolean flgAUTRM;	/**String for order no. */
	//private boolean flgBKGRM;
	
								/**String for order no. */
	//private String strORDNO;	/**Hash table for package Types */
	private String strOCMKTTP, strOCOCFNO, strOCOCFDT, strOCAMDNO,strOCAMDDT, strOCSALTP,
			strOCDTPCD, strOCPORNO, strOCPORDT, strOCZONCD, strOCCNSCD, strOCDSRCD, strOCBYRCD, 
		    strOCCURCD,	strOCEXCRT, strOCAPTVL, strOCCPTVL, strOCPMTCD, strOCTRNFL, strOCSTSFL, 
		    strOCLUSBY,	strOCLUPDT, strOCPSHFL, strOCTSHFL, strOCSBSCD, strOCDSTCD, strOCMOTCD, 
		    strOCFILRF,	strOCPMTRF, strOCINSRF, strOCFORRF, strOCREGBY, strOCREGDT, strOCBKGBY, 
		    strOCDTPDS, strOCFNO5,  strOCFMM,   strOCFYY;
								/** Distributor Details */
	private String strDSTDT;	/** Buyer Details */
	private String strBYRDT;	/** Distributor details */
	private String strDSRDT;	/** Consignee details */
	private String strCNSDT;	/** Transporter Details */
    private String strTRPDT;	/** Agent Details  	 */
//	private String strAGTDT;
	
	//private String strINMKTTP, strININDNO, strININDDT, strINAMDNO, strINAMDDT, strINSALTP, strINDTPCD, strINPORNO, strINPORDT, strINZONCD, strINCNSCD, strINBYRCD, strINCURCD,
	//	strINECHRT, strINAPTVL, strINCPTVL, strINPMTCD, strINTRNFL, strINSTSFL, strINLUSBY, strINLUPDT, strINPSHFL, strINSBSCD, strINDSTCD, strINMOTCD, strINFILRF,
	//	strINPMTRF, strININSRF, strINFORRF, strINTSHFL, strINREGBY, strINREGDT, strINBKGBY, strINBKGDT, strINDTPDS;
	
										/** Default value for DTPCD (Ex-factory) */	
	private String strDFT_DTP = "01";   /** Default value for PMTCD (Against Credit) */
	private String strDFT_PMT = "01";	/** Default value for MOTCD (By Road) */
	private String strDFT_MOT = "01";	
											/** Export Sale Type */
	private String strSALTP_EXP = "12";		/** Deemed Export Sale Type */
	private String strSALTP_DEX = "03";		/** Stock Transfer Sale Type */
	private String strSALTP_STF = "04";		
	
	private ResultSet rstRSSET1;
	
										/**Hash tables  */
										/** Details of all codes used in program */
	private Hashtable<String,String[]> hstCDTRN;			/** Distributor details */
	private Hashtable<String,String[]> hstPTMST;			/**	Code No. from Code Description */ 
	private Hashtable<String,String> hstCODDS;			/** Default Taxes */
	private Hashtable<String,String[]> hstDFTTX;			/** Product Master details */
	private Hashtable<String,String[]> hstPRMST;			/** List of Tax Codes from Gradewise Tax Entry */ 
	private Hashtable<String,String> hstGRTAX_TAX;		/** List of Tax Codes from Common Tax Entry */ 
	private Hashtable<String,String> hstCOTAX_TAX;		/** List of prod.codes from Grade Entry */
	private Hashtable<String,String[]> hstGRDTL_PRD;		/** Product Code, Pkg.type & corresponding  Indent/DO/Inv.Qty. //DO / Inv.Qty. */
	private Hashtable<String,String[]> hstGRDTL;			/** Last indent number for individual distributor series */
	private Hashtable<String,String[]> hstOCFNO;			/** Agent's Details */
	private Hashtable<String,String[]> hstPTMST1;
	
								// Vectors for storing items list of Combo Boxes
								/** Vector for storing items list of Combo Box */
	private Vector<String> vtrSALTP;
	private Vector<String> vtrMKTTP;	/** Vector for storing mode of transport list of Combo Box */
	private Vector<String> vtrMOTCD;	/**	Vector for storing Despatch Type list of Combo Box */
	private Vector<String> vtrDTPCD;	/** Vector for storing payment types list of Combo Box */
	private Vector<String> vtrPMTCD;    
	private Vector<String[]> vtrHLPARR;
	
	private Vector<String> vtrOCFNO;
	//private Vector vtrHLPARR;
	
	private Object[] arrHSTKEY;		// Object array for getting hash table key in sorted order
	
								/**Button for indent authorisation only in modification	 */
	//private JButton btnAUTRN;	/**Button to print indent, only in authorisation state */
	private JButton btnPRINT;	
									/**Master level tax, applicable for all items in the order	 */
	private JRadioButton rdbAUTH_Y;	
	private JRadioButton rdbAUTH_N;	/** Checkbox for Trans-shipment flag */
	private JCheckBox chbTSHFL;		/** Checkbox for PartShipment flag	 */
	private JCheckBox chbPSHFL;		
									/**Distributor series part in indent no.	 */
	//private JComboBox cmbOCFNO;		/**Market type	 */	
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
	private JLabel lblFORNO;		/**Label for Currency Code	 */
	private JLabel lblCURCD;		/**Label for Deemed Export File No. */
	private JLabel lblDEFNO;		/**Label for LC number	 */
	private JLabel lblLCNO;			/**Label For Advance Payment Document No.	 */
	private JLabel lblAPDNO;		/**Label For Payment term in days	 */
	private JLabel lblPAYTM;
	private JLabel lblSTSDS;
	private JLabel lblTRPCD;
	private JLabel lblDSRDUE;
	private JLabel lblBYRDUE;		/**Label for Freight Rate */
	private JLabel lblFRTPC;		/**Label for FOB Rate */
	//private JLabel lblFOBRT;		/**Label for Usance Rate*/
	private JLabel lblUSNRT;		/**Label for Additional Cost*/
	private JLabel lblADLCO;		/**Label for USD Conversion Factor */
	private JLabel lblUSDCF;		/**Label for Delivery At Field */
	private JLabel lblDELAD;
									/**Order No.	 */
	private TxtLimit txtREGRM;		/**Buyer Code	 */
	//private TxtLimit txtBKGRM;	/**Buyer Code	 */
	//private TxtLimit txtAUTRM;	/**Buyer Code	 */
	//private TxtNumLimit txtINDNO;	/**Buyer Code	 */
	private TxtLimit txtOCFNO;
	//private TxtLimit txtOCFMM;
	//private TxtLimit txtOCFYY;
	//private TxtLimit txtOCFNO5;
	
									/**Distributor Code	 */
	private TxtLimit txtDSRCD;		/**Agent Code  		 */
	//private TxtLimit txtAGTCD;		/**Buyer Code  		 */
	private TxtLimit txtBYRCD;		/**Consinee Code	 */
	private TxtLimit txtCNSCD;		/**Distributor Name	 */
	private TxtLimit txtDSTCD;		/**Distributor Name	 */
	private TxtLimit txtDSRNM;		/**Agent  Name	 */
	//private TxtLimit txtAGTNM;		/**sHORT  Name	 */
	//private TxtLimit txtSHRNM;		/**Buyer Name	 */
	private TxtLimit txtDSTNM;		/**Buyer Name	 */
	private TxtLimit txtBYRNM;		/**Consinee Name	 */
	private TxtLimit txtCNSNM;		/**Delivery At */
	private TxtLimit txtDELAD;		/**Order Confirmation Date */

	//private TxtDate txtORDDT;		/**Registration Date	 */
	private TxtDate txtOCFDT;
	private TxtDate txtREGDT;		/**Ammendment Date	 */
	private TxtDate txtAMDDT;		/**Booking Date	 */
	private TxtDate txtBKGDT;		/**Customer Reference Date	 */
	private TxtDate txtCRFDT;		/**Agreed Shipmenet Date	 */
	private TxtDate txtASHDT;		/**Last Shipment Date  		 */
	private TxtDate txtLSHDT;		/**L/C Establishment Date	 */
	private TxtDate txtLCEDT;		
	private JTextField txtPRDCD;		
	private JTextField txtPKGTP1;		
	private JTextField txtDELTP1;	
	private JTextField txtDELDT;	
									/**Accounts payment Term	 */
	private JTextField txtGRDCD;	/**Customer Payment Term	 */
	private JTextField txtREGBY;	/**Customer Payment Term	 */
	private JTextField txtEUSCD;	/**Customer Payment Term	 */
	private JTextField txtPKGTP;	// Package Type
	private JTextField txtPAYAC;	/**Customer Payment Term	 */
	private JTextField txtPAYCT;	/**F.O.R. Reference Number */
	private JTextField txtFORNO;	/**Distributor Category	 */
	private JLabel     lblDSRTP;	/**Agent Category	 */
	//private JLabel	lblAGTTP;
	//private JTextField txtDSRCD;	/**LC Number	 */
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
	private JTextField txtFRTPC;
	private JTextField txtFOBRT;
	private JTextField txtUSNRT;
	private JTextField txtADLCO;
	private JTextField txtUSDCF;
    
	private JTextField txtCOTAX_TAXCD;
	private JTextField txtCOTAX_TAXDS;
	private JTextField txtGRTAX_TAXCD;
	private JTextField txtGRTAX_PRDCD;
	
	
									/**Other taxes details table	 */
	private cl_JTable tblCOTAX;		/**Grade Details table	 */
	private cl_JTable tblGRDTL;		/**Grade wise tax details table	 */
	private cl_JTable tblGRTAX;		/**Status details table	 */
	private cl_JTBL tblSTATS;		
	private cl_JTable tblOCFDSP;		
	private cl_JTable tblDLSCH;		
									/**Tabbed pane for order details 	 */
	private JTabbedPane tbpMAIN;
	private JPanel pnlOCFDSP;
	
									/**Panel for Common Details	 */
	private JPanel pnlCODTL;		/**Panel for common tax details	 */
	private JPanel pnlCOTAX;		/**Panel for grade wise details	 */
	private JPanel pnlGRDTL;		/**Panel for grade wise tax details	 */
	private JPanel pnlGRTAX;		/**Panel for status details	 */
	private JPanel pnlSTATS;		
	private JPanel pnlDLSCH;
									
	private String strMKTTP="";					// Product Type
    private String strYREND = "31/03/2008";		// Previous Year Ending date
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

	//	private mr_rpind objRPIND;
	//private mr_rpOCF objRPOCF;
	
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
	private int intTB1_OCFPK = 6;			
	private int intTB1_BASRT = 7;			
	private int intTB1_EUSCD = 8;			
	private int intTB1_CDCVL = 9;			
	private int intTB1_DDCVL = 10;	
	private int intTB1_FRTPM = 11;
	private int intTB1_FOBRT = 12;
	
	//private int intTB1_TDCVL = 11;			
	//private int intTB1_TDCRF = 12;			

	

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

	
	private int intTB4_OCFNO = 1;
	private int intTB4_OCFDT = 2;
	private int intTB4_AMDNO = 3;
	private int intTB4_AMDDT = 4;
	private int intTB4_PRDDS = 5;
	private int intTB4_REQQT = 6;
	private int intTB4_OCFQT = 7;
	//private int intTB4_OCFQT = 6;
	private int intTB4_REGBY = 8;
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
	private int intPTMST_TOT1 = 6;
    private int intAE_PT_PRTNM = 0;		
    private int intAE_PT_ZONCD = 1;		
    private int intAE_PT_SHRNM = 2;		
    private int intAE_PT_ADR01 = 3;		
    private int intAE_PT_ADR02 = 4;		
    private int intAE_PT_ADR03 = 5;		
	

											/** Array elements for Product Master details */
	private int intPRMST_TOT = 3;
    private int intAE_PR_PRDCD = 0;		
    private int intAE_PR_PRDDS = 1;		
    private int intAE_PR_AVGRT = 2;		
	

											/** Array elements for Grade Details */
	//private int intGRDTL_TOT = 4;
    //private int intAE_GRD_OCFQT = 0;		
    //private int intAE_GRD_INDQT = 1;		
    //private int intAE_GRD_DORQT = 2;		
    //private int intAE_GRD_INVQT = 3;		
	
	private int intGRDTL_TOT = 5;
    private int intAE_GRD_REQQT = 0;		
    private int intAE_GRD_OCFQT = 1;		
    private int intAE_GRD_INDQT = 2;		
    private int intAE_GRD_DORQT = 3;		
    private int intAE_GRD_INVQT = 4;		
	
	private String strOC_STSFL;
	
									/** Variables for Code Transaction Table
									 */
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	private String strPRDCD;		

	private String strPRTCD;
	private String strPRTCD1;
	private String strWHRSTR;
	private boolean flgCHK_EXIST;
	private JCheckBox chkOCFDSP;
	private JCheckBox chkREDTAX;
	private boolean flgREDTAX = false;

	private Hashtable<String,String> hstPRDDT;
	/**Constructor for the form<br>
	 * Retrieves market type/delivery type/payment type and transport type  options from CO_CDTRN and populates respective combos.<br>
	 * Starts thread for retrieving Distributor details along with curren year series for INDNO
	 */
	mr_teocn()
	{
		super(2);
		try
		{
			//objRPOCN = new mr_rpocn(1);
			hstCDTRN = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			hstPTMST1 = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstDFTTX = new Hashtable<String,String[]>();
			//hstPRDCD = new Hashtable();
			hstPRMST = new Hashtable<String,String[]>();
			hstGRTAX_TAX = new Hashtable<String,String>();
			hstCOTAX_TAX = new Hashtable<String,String>();
			hstGRDTL_PRD = new Hashtable<String,String[]>();
			hstGRDTL	 = new Hashtable<String,String[]>();
			hstOCFNO	 = new Hashtable<String,String[]>();

			vtrSALTP = new Vector<String>();
			vtrMKTTP = new Vector<String>();
			vtrMOTCD = new Vector<String>();
			vtrDTPCD = new Vector<String>();
			vtrPMTCD = new Vector<String>();
			vtrOCFNO = new Vector<String>();
			vtrHLPARR = new Vector<String[]>();

			chkOCFDSP = new JCheckBox("Unauth.Order Confirmations");
			chkREDTAX = new JCheckBox("Redefine Tax");
			//strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "7" : "6";
			//strYRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4);
			strYRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4);
			hstCDTRN.clear();
            hstCODDS.clear();
			//crtCDTRN("'MSTCOXXCUR','STSMRXXIND','SYSMRXXDTP', 'SYSMRXXPMT','SYSMR01MOT','SYSMR00EUS','SYSCOXXTAX','SYSCOXXDST','SYSFGXXPKG','SYSCOXXAMT'","",hstCDTRN);
			crtCDTRN("'MSTCOXXCUR','STSMRXXOCF','SYSMRXXDTP', 'SYSMRXXPMT','SYSMR01MOT','SYSMR00EUS','SYSCOXXTAX','SYSCOXXDST','SYSFGXXPKG','SYSMR00SAL','SYSCOXXAMT','SYSMRXXPOD'","",hstCDTRN);			
			crtPTMST();
			crtPTMST1();
			crtPRMST();
			//crtHLPVTR();

			setMatrix(20,12);
			add(new JLabel("Sale Type"),1,1,1,2,this,'L');
			add(cmbSALTP=new JComboBox(),1,3,1,1,this,'L');
			add(new JLabel("Market Type"),1,4,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),1,5,1,1,this,'L');
			add(lblSTSDS=new JLabel(""),1,5,1,2,this,'L');
			add(rdbAUTH_Y=new JRadioButton("Authorise Order Conf."),5,7,1,2,this,'L');
        	add(rdbAUTH_N=new JRadioButton("Donot Authorise Order Conf."),5,9,1,2,this,'L');
			add(btnPRINT=new JButton("Print"),1,11,1,1.5,this,'L');
			ButtonGroup btg=new ButtonGroup();btg.add(rdbAUTH_Y);btg.add(rdbAUTH_N);
			rdbAUTH_Y.setVisible(false);
			rdbAUTH_N.setVisible(false);
			//btnAUTRN.setVisible(false);
			//System.out.println("1");
			
			//ADDING BASE DETAILS OF ORDER		
			//add(new JLabel("Dst.Type."),1,5,1,1,this,'L');
			//add(lblDSRTP=new TxtLimit(1),1,6,1,0.5,this,'L');
			add(new JLabel("O/Con.No."),1,7,1,1,this,'L');
			add(txtOCFNO=new TxtLimit(10),1,8,1,2,this,'L');
			//add(cmbOCFNO=new JComboBox(),1,8,1,2,this,'L');
			//cmbOCFNO.setMaximumRowCount(4);
			//add(txtOCFNO=new TxtNumLimit(5.0),1,10,1,0.75,this,'L');
			add(lblDSTDT=new JLabel("Distr./Agent"),2,1,1,2,this,'L');
			add(lblDSRTP=new JLabel(""),2,2,1,0.25,this,'R');
			add(txtDSRCD=new TxtLimit(5),2,3,1,0.75,this,'L');
			add(txtDSRNM=new TxtLimit(45),2,4,1,3,this,'L');
			//add(new JLabel(" Agent"),2,7,1,1,this,'L');
			//add(txtAGTCD=new TxtLimit(5),2,8,1,0.75,this,'L');
			//add(txtAGTNM=new TxtLimit(45),2,9,1,4,this,'L');
			//add(lblDSRDUE=new JLabel(" ",JLabel.RIGHT),2,11,1,1.5,this,'L');
			//add(txtSHRNM=new TxtLimit(20),2,10,1,1,this,'L');
			add(new JLabel("Buyer / Overdue "),3,1,1,2,this,'L');
			add(txtBYRCD=new TxtLimit(5),3,3,1,0.75,this,'L');
			add(txtBYRNM=new TxtLimit(45),3,4,1,7,this,'L');
			add(lblBYRDUE=new JLabel(" ",JLabel.RIGHT),3,11,1,1.5,this,'L');
			add(lblCNSDT=new JLabel("Consignee"),4,1,1,2,this,'L');
			add(chkOCFDSP,6,10,1,2,this,'L');
			//System.out.println("2");
          
			add(txtCNSCD=new TxtLimit(5),4,3,1,0.75,this,'L');
			add(txtCNSNM=new TxtLimit(45),4,4,1,8,this,'L');
			add(new JLabel("Destination"),5,1,1,2,this,'L');
			add(txtDSTCD=new TxtLimit(3),5,3,1,0.75,this,'L');
			add(txtDSTNM=new TxtLimit(45),5,4,1,3,this,'L');
			setMatrix(20,6);

			//CREATING COMMON DETAILS AS FIRST TABBED PANE		
			add(new JLabel("Order Date"),1,1,1,1,pnlCODTL=new JPanel(null),'L');
			add(txtOCFDT=new TxtDate(),1,2,1,1,pnlCODTL,'L');
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
			
			add(new JLabel("Agreed D.O.S."),3,1,1,1,pnlCODTL,'L');
			add(txtASHDT=new TxtDate(),3,2,1,1,pnlCODTL,'L');
			add(new JLabel("Last D.O.S."),3,3,1,1,pnlCODTL,'L');
			add(txtLSHDT=new TxtDate(),3,4,1,1,pnlCODTL,'L');
			add(new JLabel("L/C to be Established"),3,5,1,1,pnlCODTL,'L');
			add(txtLCEDT=new TxtDate(),3,6,1,1,pnlCODTL,'L');
			
			add(new JLabel("Delivery Type"),4,1,1,1,pnlCODTL,'L');
			add(cmbDTPCD=new JComboBox(),4,2,1,1,pnlCODTL,'L');
			add(lblFORNO=new JLabel("F.O.R. Ref."),4,3,1,1,pnlCODTL,'L');
			add(txtFORNO=new TxtLimit(15),4,4,1,1,pnlCODTL,'L');
			add(lblPAYTM=new JLabel("Cr. days Cust/Acct "),4,5,1,1,pnlCODTL,'L');
			add(txtPAYCT=new TxtNumLimit(3.0),4,6,1,0.5,pnlCODTL,'L');
			add(txtPAYAC=new TxtNumLimit(3.0),4,6,1,0.5,pnlCODTL,'R');
			add(new JLabel("Payment Type"),5,1,1,1,pnlCODTL,'L');
			add(cmbPMTCD=new JComboBox(),5,2,1,1,pnlCODTL,'L');
			add(lblAPDNO=new JLabel("Adv. Pmt. Doc. No"),5,3,1,1,pnlCODTL,'L');
			add(txtAPDNO=new TxtLimit(25),5,4,1,1,pnlCODTL,'L');
			add(lblLCNO=new JLabel("LC No"),5,3,1,1,pnlCODTL,'L');
			add(txtLCNO=new JTextField(),5,4,1,1,pnlCODTL,'L');
			lblLCNO.setVisible(false);txtLCNO.setVisible(false);
			add(new JLabel("Trans-shipment"),5,5,1,1,pnlCODTL,'L');
			add(chbTSHFL=new JCheckBox("  Allowed"),5,6,1,1,pnlCODTL,'L');
			
			add(new JLabel("Mode Of Transport"),6,1,1,1,pnlCODTL,'L');
			add(cmbMOTCD=new JComboBox(),6,2,1,1,pnlCODTL,'L');
			add(new JLabel("Insu. Policy No"),6,3,1,1,pnlCODTL,'L');
			add(chbINSFL=new JCheckBox("Available"),6,4,1,1,pnlCODTL,'L');
			
			add(lblDEFNO=new JLabel("D/Exp. File No"),7,5,1,1,pnlCODTL,'L');
			add(txtDEFNO=new TxtLimit(30),7,6,1,1,pnlCODTL,'L');
			add(lblCURCD=new JLabel("Currency / Exchange Rate "),7,1,1,1,pnlCODTL,'L');
			add(txtCURCD=new TxtLimit(2),7,2,1,0.5,pnlCODTL,'L');
			add(txtECHRT=new TxtNumLimit(6.2),7,2,1,0.5,pnlCODTL,'R');
			add(lblFRTPC=new JLabel("Frt./Cont."),7,3,1,0.5,pnlCODTL,'L');
			add(txtFRTPC=new TxtNumLimit(7.3),7,4,1,1,pnlCODTL,'L');
			//add(lblFOBRT=new JLabel("FOB Chgs/PMT "),7,4,1,0.5,pnlCODTL,'L');
			//add(txtFOBRT=new TxtNumLimit(6.2),7,4,1,0.5,pnlCODTL,'R');
			add(lblUSNRT=new JLabel("Usance Chgs/PMT "),7,5,1,0.5,pnlCODTL,'L');
			add(txtUSNRT=new TxtNumLimit(6.2),7,5,1,0.5,pnlCODTL,'R');
			add(lblADLCO=new JLabel("Addl.Cost/PMT "),7,6,1,0.5,pnlCODTL,'L');
			add(txtADLCO=new TxtNumLimit(6.2),7,6,1,0.5,pnlCODTL,'R');
			add(new JLabel("Part-shipment"),6,5,1,1,pnlCODTL,'L');
			add(chbPSHFL=new JCheckBox("  Allowed"),6,6,1,1,pnlCODTL,'L');
			
			add(lblUSDCF=new JLabel("USD Conversion Factor "),8,1,1,1,pnlCODTL,'L');
			add(txtUSDCF=new TxtNumLimit(6.3),8,2,1,0.5,pnlCODTL,'L');
			//txtUSDCF.setText(String.valueOf(Integer.parseInt("1.00")));
			//String.valueOf(Integer.parseInt(L_strOCFTXT)
			add(lblTRPCD = new JLabel("Transporter"),8,3,1,1,pnlCODTL,'L');
			add(txtTRPCD=new TxtLimit(5),8,4,1,1,pnlCODTL,'L');
		    add(txtTRPNM=new TxtLimit(45),8,5,1,3,pnlCODTL,'L');
			
			add(new JLabel("Reg. Remark"),9,1,1,1,pnlCODTL,'L');
			add(txtREGRM=new TxtLimit(200),9,2,1,5,pnlCODTL,'L');
			add(new JLabel("Delivery At"),10,1,1,1,pnlCODTL,'L');
			add(txtDELAD=new TxtLimit(200),10,2,1,5,pnlCODTL,'L');
			
			//add(new JLabel("Bkg. Remark"),8,1,1,1,pnlCODTL,'L');
			//add(txtBKGRM=new TxtLimit(200),8,2,1,5,pnlCODTL,'L');
			//add(new JLabel("Aut. Remark"),9,1,1,1,pnlCODTL,'L');
			//add(txtAUTRM=new TxtLimit(200),9,2,1,5,pnlCODTL,'L');
			txtDEFNO.setVisible(false);txtECHRT.setVisible(false);txtCURCD.setVisible(false);
			lblDEFNO.setVisible(false);lblCURCD.setVisible(false);

			//lblTRPCD.setVisible(false);txtTRPCD.setVisible(false);txtTRPNM.setVisible(false);
			
			//CREATING COMMON TAX DETIALS PANEL AS SECOND TABBED PANE		
			tblCOTAX=crtTBLPNL1(pnlCOTAX=new JPanel(null),new String[]{"FL","Code","Description","Value","Amt./Percent","Proc.Seq."},20,1,1,6,6,new int[]{20,100,200,100,150,100},new int[]{0});

			add(chkREDTAX,10,6,1,3,pnlCOTAX,'L');
			tblCOTAX.setCellEditor(intTB2_TAXCD,txtCOTAX_TAXCD=new TxtLimit(3));
			tblCOTAX.setCellEditor(intTB2_TAXDS,txtCOTAX_TAXDS=new JTextField());
			tblCOTAX.setCellEditor(intTB2_TAXVL,new TxtNumLimit(6.2));
			tblCOTAX.setCellEditor(intTB2_PRCSQ,new TxtLimit(2));
			tblCOTAX.setCellEditor(intTB2_AMTFL,new TxtLimit(1));
			tblCOTAX.setInputVerifier(new TBLINPVF());
			txtCOTAX_TAXCD.addFocusListener(this);
			txtCOTAX_TAXCD.addKeyListener(this);
			txtCOTAX_TAXDS.addFocusListener(this);
			txtCOTAX_TAXDS.addKeyListener(this);

			//CREATING GRADE DETAILS PANEL AS THIRD TABBED PANE					
			//tblGRDTL=crtTBLPNL1(pnlGRDTL=new JPanel(null),new String[]{"FL","Prd.Code","Description","Pkg.Type","Req.Qty.","UOM","Pkgs","Bas.Rate","Sector","C.Disc.","D.Disc.","T.Disc.","T.Code"},20,1,1,8,5.9,new int[]{20,80,100,60,75,40,40,75,75,45,45,40,50},new int[]{0});
			tblGRDTL=crtTBLPNL1(pnlGRDTL=new JPanel(null),new String[]{"FL","Prd.Code","Description","Pkg.Type","Req.Qty.","UOM","Pkgs","Bas.Rate","Sector","C.Disc.","D.Disc.","Frt/PMT","FOB/PMT"},20,1,1,8,5.9,new int[]{20,80,100,60,75,40,40,75,75,45,45,45,45},new int[]{0});
			tblGRDTL.setCellEditor(intTB1_PRDCD,txtGRDCD=new TxtNumLimit(10.0));
			tblGRDTL.setCellEditor(intTB1_UOMCD,new TxtLimit(2));
			tblGRDTL.setCellEditor(intTB1_BASRT,new TxtNumLimit(9.2));
			tblGRDTL.setCellEditor(intTB1_EUSCD,txtEUSCD=new JTextField());
			tblGRDTL.setCellEditor(intTB1_PKGTP,txtPKGTP=new JTextField());
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
			tblSTATS=crtTBLPNL(pnlSTATS=new JPanel(null),new String[]{"FL","Grade","Req. Qty.","Ord. Qty.","Bkd. Qty.","D.O. Qty.","Inv. Qty."},20,1,1,8,5.9,new int[]{20,200,100,100,100,100,100,100},new int[]{0});


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
			chkOCFDSP.setSelected(false);
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
		 return   (LP_FLDVL.length()>10) ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
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
		 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
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
		//System.out.println(LP_FLDNM);
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
	{		System.out.println(LP_FLDNM);
setMSG(L_EX,"getRSTVAL");}
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
private Vector getHLPVTR(String LP_HLPTP, String LP_TXTVL)
{
	vtrHLPARR.clear();
	if (LP_HLPTP.equals("PRDCD"))
	{
		setHST_ARR(hstPRMST);
		String L_strPRDCT = getCDTRN("MSTCOXXMKT"+strMKTTP,"CMT_CCSVL",hstCDTRN);
		for(int i=0;i<arrHSTKEY.length;i++)
		{
			if(!M_strSBSCD.equals("511600"))
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
			//if(arrHSTKEY[i].toString().substring(0,10).equals("SYSCOXXDST"))
			if(arrHSTKEY[i].toString().substring(0,10).equals("SYSMRXXPOD"))
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN),getCDTRN(arrHSTKEY[i].toString(),"CMT_CHP02",hstCDTRN),getCDTRN(arrHSTKEY[i].toString(),"CMT_NCSVL",hstCDTRN)});
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
				vtrHLPARR.addElement(new String[] {arrHSTKEY[i].toString().substring(10),getCDTRN(arrHSTKEY[i].toString(),"CMT_CODDS",hstCDTRN),getCDTRN(arrHSTKEY[i].toString(),"CMT_NMP02",hstCDTRN)});
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
 * Order Confirmation series is set according to market type and distributor type combination
 */
private void setCMBOCFNO()
{
	try
	{
		//setCDLST1(vtrOCFNO,"D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_CODCD");
		//setCMBVL(cmbOCFNO,vtrOCFNO);
		//System.out.println("MR"+strMKTTP+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()));				
		if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
		{
			crtCDTRN("'SYSCOXXTAX'"," and upper(CMT_CHP02) <> 'X' and  CMT_CGMTP + CMT_CGSTP + CMT_CODCD in (select cd1_cgmtp + cd1_cgstp + cd1_codcd from co_cdtr1 where cd1_cgmtp + cd1_cgstp='SYSCOXXTAX' and cd1_subcd = 'MR"+strMKTTP+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' and cd1_taxtp='01')",hstDFTTX);
			crtCDTRN("'SYSCOXXTAX'"," and upper(CMT_CHP02) = 'X' and  CMT_CGMTP + CMT_CODCD in (select cd1_cgmtp + cd1_codcd from co_cdtr1 where cd1_cgmtp + cd1_cgstp='SYSCOXXDES' and cd1_subcd = 'MR"+strMKTTP+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' and cd1_taxtp='02')",hstDFTTX);
		}
		//System.out.println("setting cmbocfno");
		// REQUIRED FOR MANUAL OCF NO. GENERATION //crtCDTRN("'DOCMR01OCF','DOCMR02OCF','DOCMR03OCF','DOCMR04OCF','DOCMR05OCF','DOCMR11OCF'","",hstOCFNO);
		//crtCDTRN("'DOCMR01OCF','DOCMR02OCF','DOCMR03OCF','DOCMR04OCF','DOCMR05OCF','DOCMR11OCF'","",hstOCFNO);		
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
			//System.out.println("90");
			M_objSOURC=L_IE.getSource();
			//System.out.println("91");
			if(L_IE.getStateChange()!=1)
				return;
				
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
			//	System.out.println("92");
			//	clrCOMP();
			//	System.out.println("100");
			//	System.out.println("101");
			//	System.out.println("itemstatechanged");
			//	System.out.println(M_strSBSCD);
				//REQUIRED FOR MANUAL OCF NO GENERATION //crtCDTRN("'DOCMR01OCF','DOCMR02OCF','DOCMR03OCF','DOCMR04OCF','DOCMR05OCF','DOCMR11OCF'"," and  SUBSTRING(CMT_CODCD,8,1)='"+strYRDGT+"' and CMT_CHP01='"+M_strSBSCD.substring(0,2)+"'",hstCDTRN);
			//	System.out.println("102");
				String L_strOPSEL=L_IE.getItem().toString();
			//	System.out.println("103");
				if(L_strOPSEL.equals(cl_dat.M_OPADD_pbst))
				{//Set default date and user data
			//		System.out.println("104");
					btnPRINT.setEnabled(false);
					txtOCFDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
					txtREGBY.setText(cl_dat.M_strUSRCD_pbst);
					txtBKGDT.setText("");
					txtBKGBY.setText("");
					//cmbSALTP.requestFocus();
				}
				else if(L_strOPSEL.equals(cl_dat.M_OPDEL_pbst))
				{//Set default date and user data
					btnPRINT.setVisible(false);
					cmbSALTP.setEnabled(true);
					cmbMKTTP.setEnabled(true);
					txtOCFNO.setEnabled(true);
					//cmbOCFNO.setEnabled(true);
				}
				else if(L_strOPSEL.equals(cl_dat.M_OPMOD_pbst) || L_strOPSEL.equals(cl_dat.M_OPAUT_pbst))
				{
					btnPRINT.setEnabled(false);
				}
				setDFTSTS();
				cmbMKTTP.setEnabled(true);cmbMKTTP.setVisible(true);
				cmbSALTP.requestFocus();
			}

			//else if(M_objSOURC==cmbMKTTP)
			//{
			//	if(cmbMKTTP.getSelectedIndex()>0)
			//	{
			//		strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
  			//		setCMBOCFNO();
			//		setDFTSTS();
			//	}
			//	//String L_strOCFTXT = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF","CMT_CCSVL", hstOCFNO);
			//	String L_strOCFTXT = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF8","CMT_CCSVL", hstOCFNO);
			//	String CHP01 = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF8","CMT_CHP01", hstOCFNO);
			//	String CHP02 = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF8","CMT_CHP02", hstOCFNO);
			//	System.out.println("chp01 "+CHP01+" CHP02 "+CHP02);
			//	System.out.println("STRMKTTP "+"D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF8");
			//	System.out.println("100");
			//	strOCFNO5 = (String.valueOf(Integer.parseInt(L_strOCFTXT) + (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)==true ? 1 :0)));
				
			//	if(strOCFNO5.length()<4)
			//		for(int i=0;i<=4;i++)
			//		{
			//			strOCFNO5 = "0"+strOCFNO5;
			//			if(strOCFNO5.length()==4)
			//				break;
			//		}				
				
			//	System.out.println("101");
			//	strOCFMM = cl_dat.M_strLOGDT_pbst.trim().substring(4,5);
			//	if(strOCFMM.length()<2)
			//		for(int i=0;i<=2;i++)
			//		{
			//			strOCFMM = "0"+strOCFMM;
			//			if(strOCFMM.length()==2)
			//				break;
			//		}				
			//	System.out.println("102");
			//	System.out.println(strOCFMM);
			//	System.out.println("103");
			//	strOCFYY = cl_dat.M_strLOGDT_pbst.trim().substring(9,10);
			//	if(strOCFYY.length()<2)
			//		for(int i=0;i<=2;i++)
			//		{
			//			strOCFYY = "0"+strOCFYY;
			//			if(strOCFYY.length()==2)
			//				break;
			//		}				
			//	System.out.println("104");
			//	System.out.println(strOCFYY);
			//	System.out.println("105");
			//	strOCOCFNO = strOCFNO5+"/"+strOCFMM+"/"+strOCFYY ;	
			//	System.out.println(strOCOCFNO);
			//	txtOCFNO.setText(strOCOCFNO);
			//}

//*********************
//			else if(M_objSOURC==cmbMKTTP)
//			{
//				if(cmbMKTTP.getSelectedIndex()>0)
//				{
//					strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
//					System.out.println("90");
//					setCMBOCFNO();
//					System.out.println("91");
//					setDFTSTS();
//				}
//				//String L_strOCFTXT = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF","CMT_CCSVL", hstOCFNO);
//				String L_strOCFTXT = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_CCSVL", hstCDTRN);
//				System.out.println("L_strOCFTXT "+L_strOCFTXT);
//				System.out.println("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF");
//				System.out.println("92");
//				String L_strOCFTXT1 = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_CODCD",hstCDTRN);				
//				System.out.println("93");
//				//String CHP01 = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF8","CMT_CHP01", hstOCFNO);
//				//String CHP02 = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF8","CMT_CHP02", hstOCFNO);
//				//System.out.println("chp01 "+CHP01+" CHP02 "+CHP02);
//				//System.out.println("STRMKTTP "+"D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+"MR"+strMKTTP+"OCF8");
//				//System.out.println("100");
//				System.out.println("strOCFNO5 "+strOCFNO5);
//				strOCFNO5 = (String.valueOf(Integer.parseInt(L_strOCFTXT) + (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)==true ? 1 :0)));
//				System.out.println("95");
//				System.out.println(strOCFNO5);
//				if(strOCFNO5.length()<4)
//					for(int i=0;i<=4;i++)
//					{
//						strOCFNO5 = "0"+strOCFNO5;
//						if(strOCFNO5.length()==4)
//							break;
//					}				
//				System.out.println(L_strOCFTXT1);
//				strOCOCFNO = L_strOCFTXT1+strOCFNO5;
//				System.out.println(strOCFNO5);
//				//System.out.println(strOCOCFNO);
//				txtOCFNO.setText(strOCOCFNO);
//			}
//****************			
//****************			
			else if(M_objSOURC==cmbMKTTP)
			{
				//if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				//return true;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{	
				if(cmbMKTTP.getSelectedIndex()>0)
				{
					strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
					setCMBOCFNO();
					ResultSet M_rstRSSET=cl_dat.exeSQLQRY("Select * from co_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MR"+strMKTTP+"OCF' and cmt_CODCD='"+strYRDGT+strMKTTP+"'");
					//System.out.println("Select * from co_CDTRN where CMT_CGMTP='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='MR"+strMKTTP+"OCF' and cmt_CODCD='"+strYRDGT+strMKTTP+"'");
					//System.out.println("in 1");
					if(M_rstRSSET==null || (!M_rstRSSET.next()))
					{setMSG("Order Confirmation series not found ..",'E');} //cl_dat.M_flgLCUPD_pbst = false; return false;}
//****
					String L_strOCFTXT=Integer.toString(Integer.parseInt(getRSTVAL(M_rstRSSET,"CMT_CCSVL","N"))+1);
					//System.out.println(Integer.parseInt(getRSTVAL(M_rstRSSET,"CMT_CCSVL","N"))+1);
					//System.out.println("L_strOCFTXT "+L_strOCFTXT);
					txtOCFNO.setText(L_strOCFTXT);
					if(L_strOCFTXT.length()<5)
					{
					//System.out.println("in 3");
						for(int i=0;i<=5;i++)
						{
							txtOCFNO.setText("0"+txtOCFNO.getText());
							//System.out.println(txtOCFNO.getText());
							if(txtOCFNO.getText().length()==5)
							break;
						}
					}
					L_strOCFTXT = txtOCFNO.getText();
					//System.out.println("L_strOCFTXT"+L_strOCFTXT);
					txtOCFNO.setText(getRSTVAL(M_rstRSSET,"CMT_CODCD","C")+L_strOCFTXT);
					//System.out.println("in 6");
					if(M_rstRSSET!=null)
					{
						M_rstRSSET.close();
					//	System.out.println("in 7");
					}
					setDFTSTS();				
//****		
				}
				}
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
				}	
			}	
//*******************			
//*********************	
			
		/**else if(M_objSOURC==cmbOCFNO)
		{
				if(cmbOCFNO.getSelectedIndex()>0)
				{
					String L_strOCFTXT = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),"CMT_CCSVL", hstOCFNO);
					txtOCFNO.setText(String.valueOf(Integer.parseInt(L_strOCFTXT) + (cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst)==true ? 1 :0)));
					if(txtOCFNO.getText().length()<4)
					for(int i=0;i<=4;i++)
					{
						txtOCFNO.setText("0"+txtOCFNO.getText());
						if(txtOCFNO.getText().length()==4)
							break;
					}
					
					txtAMDNO.setText("00");
					txtAMDDT.setText("");
					String L_strCHP02 = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN);
					String L_strMODLS = getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),"CMT_MODLS", hstCDTRN);
					//System.out.println(L_strMODLS+"/"+L_strCHP02);
					lblDSRTP.setText(L_strMODLS);
					txtDSRCD.setText(L_strCHP02);
					//System.out.println("*"+lblDSRTP.getText()+txtDSRCD.getText()+"*");
					txtDSRNM.setText(getPTMST(lblDSRTP.getText()+txtDSRCD.getText(),"PT_PRTNM"));
					txtSHRNM.setText(getPTMST(lblDSRTP.getText()+txtDSRCD.getText(),"PT_SHRNM"));
					txtOCFNO.requestFocus();
				}
				
				if(!cmbOCFNO.isPopupVisible())
				{
					txtOCFNO.setVisible(true);
					lblCNSDT.setVisible(true);
					lblDSTDT.setVisible(true);
					txtOCFNO.requestFocus();
				}
			}*/
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
	} /** Setting initial/default values in Combo Box (taking code as a parameter)
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


//CHECKING WHETHER USER HAS PERMISSION TO AUTHORISE ORDER
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

	
	
	/** Creating hash tables required during tabPANE-2 editing
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

	/** Creating hash tables required during tabPANE-3 editing
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
	/**<b>TASKS :</b><br>
	 * &nbsp&nbsp&nbspSource=cmbPMTCD/cmbDTPCD : Show/hide related details' fields depending on item selected<br>&nbsp&nbsp&nbsp&nbsp&nbspSelection Index used for this. There fore, change in display order should br taken care
	 * &nbsp&nbsp&nbspSource=cmbOCFNO : Display respective Distributor details in address and ORDNO text Fields<br>	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				clrCOMP();
				setAUTFL();
				//cmbOCFNO.setEnabled(true);
				txtOCFNO.setEnabled(true);

				
				
				cmbSALTP.setEnabled(true);
				cmbMKTTP.setEnabled(true);
				setCMBDFT(cmbDTPCD,"SYSMRXXDTP",strDFT_DTP);
				lblTRPCD.setVisible(true);txtTRPCD.setVisible(true);txtTRPNM.setVisible(true);
				setCMBDFT(cmbPMTCD,"SYSMRXXPMT",strDFT_PMT);
				setCMBDFT(cmbMOTCD,"SYSMR01MOT",strDFT_MOT);
				//setCDLST(vtrMKTTP,"MSTCOXXMKT","CMT_CODDS");
				
				//setCMBVL(cmbMKTTP,vtrMKTTP);
				
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
			}
			else if(M_objSOURC==cmbSALTP && (cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0))
			{
				setCMBMKTTP();
				cmbMKTTP.setEnabled(true);cmbMKTTP.setVisible(true);
				cmbMKTTP.requestFocus();
			}

			else if(M_objSOURC==cmbMKTTP  && (cmbMKTTP.getItemCount() > 0 && cmbMKTTP.getSelectedIndex()>0))
			{
					setCMBDFT(cmbDTPCD,"SYSMRXXDTP",strDFT_DTP);
					lblTRPCD.setVisible(true);txtTRPCD.setVisible(true);txtTRPNM.setVisible(true);
					setCMBDFT(cmbPMTCD,"SYSMRXXPMT",strDFT_PMT);
					setCMBDFT(cmbMOTCD,"SYSMR01MOT",strDFT_MOT);
			}
			else if(M_objSOURC==chkOCFDSP)
				exeOCFDSP();
			else if(M_objSOURC==chkREDTAX)
//				exeREDTAX(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()), cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"CLEAR");
				exeREDTAX(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()), txtOCFNO.getText(),"CLEAR");
			else if(M_objSOURC==txtOCFNO)
			{
				if(M_flgERROR)
					return;
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))//DISPLAY ORDER DETAILS IN MODIFICATION
					getDATA();
				if(M_strSBSCD.equals("511600"))
				{//SET DEFAULT VALUES FOR CAPTIVE CONSUMPTION
					//System.out.println("captive");
					//System.out.println(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbINDNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN));
					//txtBYRCD.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN));
					txtBYRCD.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_CHP02", hstCDTRN));
					//txtBYRNM.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),"CMT_CODDS", hstCDTRN));
					txtBYRNM.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_CODDS", hstCDTRN));
					//txtCNSCD.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),"CMT_CHP02", hstCDTRN));
					txtCNSCD.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_CHP02", hstCDTRN));
					//txtCNSNM.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),"CMT_CODDS", hstCDTRN));
					txtCNSNM.setText(getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_CODDS", hstCDTRN));
					new INPVF().verify(txtBYRCD);
					new INPVF().verify(txtDSRCD);
					//new INPVF().verify(txtAGTCD);
					txtDSTCD.setText("NGT");
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

				//objRPOCF.txtMKTTP.setText(strMKTTP);
				//objRPOCF.txtOCFNO.setText(cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText());
				//objRPOCF.M_strSBSCD=M_strSBSCD;
				//objRPOCF.M_strSBSLS=M_strSBSLS;
				//objRPOCF.M_rdbHTML.setSelected(true);
				//objRPOCF.exePRINT();
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
		crtCDTRN("'D"+cl_dat.M_strCMPCD_pbst+"MR01OCF','D"+cl_dat.M_strCMPCD_pbst+"MR02OCF','D"+cl_dat.M_strCMPCD_pbst+"MR03OCF','D"+cl_dat.M_strCMPCD_pbst+"MR04OCF','D"+cl_dat.M_strCMPCD_pbst+"MR05OCF','D"+cl_dat.M_strCMPCD_pbst+"MR12OCF'",L_strDSTCHK+" and  SUBSTRING(CMT_CODCD,4,1)='"+strYRDGT+"' and CMT_CHP01='"+M_strSBSCD.substring(0,2)+"'",hstCDTRN);
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
				else if(M_objSOURC==txtDSRCD)
					setMSG("Enter Distributor Code; 'F1' : Search by Name; 'F2' : Search by Code ..",'N');
				//else if(M_objSOURC==txtAGTCD)
				//	setMSG("Enter Agent Code; 'F1' : Search by Name; 'F2' : Search by Code ..",'N');
				else if(M_objSOURC==txtPAYCT)
					setMSG("Enter Customer Payment term in days ..",'N');
				else if(M_objSOURC==txtDSTCD)
					setMSG("Enter Destination code, Press 'F1' for help ..",'N');
				else if(M_objSOURC==txtCRFDT)
					setMSG("Enter Customer Reference Date ..",'N');
				else if(M_objSOURC==txtCRFNO)
					setMSG("Enter Customer Reference Number ..",'N');
				else if(M_objSOURC==txtOCFDT)
					setMSG("Enter Order Date ..",'N');
				else if(M_objSOURC==txtASHDT)
					setMSG("Enter Agreed Shipment Date ..",'N');
				else if(M_objSOURC==txtLSHDT)
					setMSG("Enter Last Date of Shipment ..",'N');
				else if(M_objSOURC==txtLCEDT)
					setMSG("Enter L/C Establishment Date ..",'N');
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
				//else if(M_objSOURC==tblGRDTL.cmpEDITR[intTB1_FRTRT])
				//	setMSG("Enter Freight Rate/PMT in USD..",'N');
					
				
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
				/**else if(M_objSOURC==cmbOCFNO)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
						setMSG("Select Distributor, Press 'F1' for help ..",'N');
					else
						setMSG("Select Distributor ..",'N');
					cmbOCFNO.showPopup();
				}*/
				else if(M_objSOURC==txtOCFNO)
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
				setMSG("Enter Pkg.Type , 'F1' for search  ..",'N');
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
			setMSG(e,"TEOCF.FocusGained"+M_objSOURC);
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
					if(exeDSPCHK1(tblGRDTL.getSelectedRow(),"IND_INV_DOR_NOMSG"))
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
			if(M_objSOURC==txtOCFNO && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				tblCOTAX.clrTABLE();
				setDFTTAX1();
				System.out.println("Default Tax Set");
			}
			/**else if(M_objSOURC==cmbOCFNO)
			{
				txtDSTNM.setVisible(true);
				txtBYRNM.setVisible(true);
				txtOCFNO.setVisible(true);
				lblCNSDT.setVisible(true);
				lblDSTDT.setVisible(true);
				txtOCFNO.requestFocus();
			}*/
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
		if(M_objSOURC==txtDSRCD)
			txtDSRCD.setText(txtDSRCD.getText().toUpperCase());
//		if(M_objSOURC==txtAGTCD)
//			txtAGTCD.setText(txtAGTCD.getText().toUpperCase());
	}

	/**<b>TASKS : </b><br>
	 * &nbsp&nbsp&nbspSource = txtBYRCD : HELP by buyer code(F2) as well as buyer name(F1)<br>
	 * &nbsp&nbsp&nbspSource = txtCONCD : HELP by consignee code(F2) as well as consignee name(F1)<br> 
	 * &nbsp&nbsp&nbspSource = txtDSRCD : HELP by Distributor code(F2) as well as distributor name(F1)<br> 
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
						if(!(txtBYRCD.getText().trim().length()==1))
							return;
//						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" +(M_strSBSCD.equals("511600") ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
						//M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" +(M_strSBSCD.equals("511600") ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = 'X8888' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" +(M_strSBSCD.equals("511600") ? "and PT_PRTCD like 'S77%' " : "")+" and +upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
						M_strHLPFLD = "txtBYRCD1";
						cl_hlp(M_strSQLQRY ,1,2,new String[] {"Byer Name","Byer Code"},5 ,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtBYRCD.getText().trim().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtBYRCD.getText().trim().length()==1)
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" + (M_strSBSCD.equals("511600") ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().trim().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtBYRCD.getText()+"%' "+(M_strSBSCD.equals("511600")==true ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
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
					M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+txtBYRCD.getText().substring(0,1)+"'" + (M_strSBSCD.equals("511600") ? "and PT_PRTCD like 'S77%' " : "")+" and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
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
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and SUBSTRING(PT_PRTCD,1,1)= '"+txtCNSCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtCNSCD.getText().toUpperCase()+"%' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtCNSCD1";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Consignee Name","Consignee Code"},5,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtCNSCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtCNSCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and SUBSTRING(PT_PRTCD,1,1)= '"+txtCNSCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD like '"+txtCNSCD.getText().toUpperCase()+"%'"+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
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

			else if(M_objSOURC==txtDSRCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{//search by Name
					if(txtDSRCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtDSRCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('D','A')  and SUBSTRING(PT_PRTCD,1,1)= '"+txtDSRCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('D','A') and PT_PRTCD like '"+txtDSRCD.getText().toUpperCase()+"%' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtDSRCD1";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Distri./Agent Name","Distr./Agent Code"},5,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtDSRCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtDSRCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('D','A') and SUBSTRING(PT_PRTCD,1,1)= '"+txtDSRCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('D','A') and PT_PRTCD like '"+txtDSRCD.getText().toUpperCase()+"%'"+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtDSRCD2";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Distri./Agent Code","Distri.?Agent Name"},5,"CT");
					cl_dat.M_txtHLPPOS_pbst.setText(txtDSRCD.getText().toUpperCase());
				}
				else if(L_intKEYCD==L_KE.VK_F9)
				{//Display Distributor/Agent details
					if(strDSRDT!=null)
						JOptionPane.showMessageDialog(txtDSRCD,strDSRDT);
					else
						dspADDR(txtDSRCD,txtDSRCD.getText(),"C");
				}
			}
			/**else if(M_objSOURC==txtAGTCD)
			{
				if(L_intKEYCD==L_KE.VK_F1)
				{//search by Name
					if(txtAGTCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtAGTCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('A')  and SUBSTRING(PT_PRTCD,1,1)= '"+txtAGTCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('A') and PT_PRTCD like '"+txtAGTCD.getText().toUpperCase()+"%' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtAGTCD1";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Agent Name","Agent Code"},5,"CT");
				}
				else if(L_intKEYCD==L_KE.VK_F2)
				{//Search by Code
					if(txtAGTCD.getText().length()==0)
						{setMSG("Type First letter and then press F1",'E'); return;}
					if(txtAGTCD.getText().length()==1)
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('A') and SUBSTRING(PT_PRTCD,1,1)= '"+txtAGTCD.getText().substring(0,1)+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					else
						M_strSQLQRY="SELECT PT_PRTCD, PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP in ('A') and PT_PRTCD like '"+txtAGTCD.getText().toUpperCase()+"%'"+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
					M_strHLPFLD = "txtAGTCD2";
					cl_hlp(M_strSQLQRY ,1,1,new String[] {"Agent Code","Agent Name"},5,"CT");
					cl_dat.M_txtHLPPOS_pbst.setText(txtAGTCD.getText().toUpperCase());
				}
				else if(L_intKEYCD==L_KE.VK_F9)
				{//Display Agent details
					if(strAGTDT!=null)
						JOptionPane.showMessageDialog(txtAGTCD,strAGTDT);
					else
						dspADDR(txtAGTCD,txtAGTCD.getText(),"C");
				}
			}*/
	
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
                            hstPRDDT.put(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString(),nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_PKGTP).toString(),"")+"|"+nvlSTRVL(tblGRDTL.getValueAt(i,intTB1_REQQT).toString(),"0"));
                            if(L_intCNT == 0)
							{	
                                L_strPRDLS += "'"+tblGRDTL.getValueAt(i,intTB1_PRDCD).toString()+tblGRDTL.getValueAt(i,intTB1_PKGTP).toString()+"'";
							}	
                            else
							{	
								System.out.println("1");
                                L_strPRDLS += ",'"+tblGRDTL.getValueAt(i,intTB1_PRDCD).toString()+tblGRDTL.getValueAt(i,intTB1_PKGTP).toString()+"'";
							}	
                            L_intCNT++;    
                        }   
                    }
                    //M_strSQLQRY = "SELECT PR_PRDCD,PR_PRDDS from CO_PRMST WHERE PR_PRDCD in( ";
                    //M_strSQLQRY += L_strPRDLS +")";
                    M_strSQLQRY = "SELECT PR_PRDCD,PR_PRDDS,CMT_CODCD from CO_PRMST,CO_CDTRN WHERE CMT_CGMTP='SYS' AND CMT_CGSTP='FGXXPKG' AND PR_PRDCD + CMT_CODDS in( ";
                    M_strSQLQRY += L_strPRDLS+")";
					System.out.println(M_strSQLQRY);
                 	cl_hlp(M_strSQLQRY ,2,1,new String[] {"Grade Code","Description","Packing Type"},3,"CT");
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
				//if((M_objSOURC==txtOCFNO||M_objSOURC==cmbOCFNO)&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
				if((M_objSOURC==txtOCFNO)&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>1)
				{
					M_strHLPFLD = "txtOCFNO1";
					//if(cmbOCFNO.getSelectedIndex()==0)
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,PT_PRTNM from MR_INMST, CO_PTMST where pt_prtcd=in_byrcd and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
					//	cl_hlp("Select DISTINCT OC_OCFNO,OC_OCFDT,OC_BYRNM from MR_OCMST where oc_sbscd='"+M_strSBSCD +"' and oc_stsfl='0' and OC_SBSCD = '"+M_strSBSCD+"' order by OC_OCFNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
					//else
						//cl_hlp("Select DISTINCT IN_INDNO,IN_INDDT,PT_PRTNM from MR_INMST, CO_PTMST where pt_prtcd=in_byrcd and in_sbscd='"+M_strSBSCD +"' and in_stsfl='0' and IN_INDNO like '"+cmbOCFNO.getSelectedItem().toString()+"%' and IN_SBSCD = '"+M_strSBSCD+"' order by IN_INDNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
							cl_hlp("Select DISTINCT OC_OCFNO,OC_OCFDT,OC_BYRNM from VW_OCTRN where OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND oct_sbscd1 in ("+M_strSBSLS +") and oc_stsfl <> 'X'  order by OC_OCFNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							cl_hlp("Select DISTINCT OC_OCFNO,OC_OCFDT,OC_BYRNM from VW_OCTRN where OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND oct_sbscd1 in ("+M_strSBSCD +") and oc_stsfl='0'  order by OC_OCFNO" ,1,1,new String[] {"Grade Code","Description"},3,"CT");
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
						cl_hlp(getHLPVTR("DSTCD",txtDSTCD.getText()),2,1,new String[] {"Port  Code","Name        ","Line","Rate/Cont."},4,"CT");
					else
						//cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGSTP='COXXDST' and CMT_CODCD like '"+txtDSTCD.getText()+"%' order by CMT_CODDS" ,2,1,new String[] {"Grade Code","Description"},2,"CT");
						cl_hlp(getHLPVTR("DSTCD",txtDSTCD.getText()),2,1,new String[] {"Port  Code","Name        ","Line","Rate/Cont."},4,"CT");
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
			/**else if(M_objSOURC==txtAGTNM&&L_intKEYCD==L_KE.VK_F9)
			{
				dspADDR(txtAGTNM,txtAGTCD.getText(),lblAGTTP.getText());
			}*/
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
			else if(M_objSOURC==rdbFORRATFL_R&&L_intKEYCD==L_KE.VK_ENTER)
			{
				rdbFORRATFL_A.requestFocus();
			}
			if(M_objSOURC==((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD])&&L_intKEYCD!=L_KE.VK_F1)
				L_KE.consume();
			if(L_intKEYCD==L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtOCFNO)
					txtDSRCD.requestFocus();
			else if(M_objSOURC==txtDSRCD)	
					//txtAGTCD.requestFocus();
			//else if(M_objSOURC==txtAGTCD)	
					txtBYRCD.requestFocus();
			else if(M_objSOURC==txtECHRT)
					txtFRTPC.requestFocus();
			else if(M_objSOURC==txtFRTPC)
					txtUSNRT.requestFocus();
			//else if(M_objSOURC==txtFOBRT)
			//		txtUSNRT.requestFocus();
			else if(M_objSOURC==txtUSNRT)
					txtADLCO.requestFocus();
			else if(M_objSOURC==txtADLCO)// && !txtCURCD.getText().equals("02"))
			{	
				txtUSDCF.requestFocus();
				txtUSDCF.setText(String.valueOf(Integer.parseInt("1")));
			}		
			else if(M_objSOURC==txtUSDCF)
			{	
				if(txtTRPCD.isVisible())
				{
				    txtTRPCD.requestFocus();
				}
				else
					txtREGRM.requestFocus();
			}
			else if(M_objSOURC==txtTRPCD)
				txtREGRM.requestFocus();
			else if(M_objSOURC==txtREGRM)
				txtDELAD.requestFocus();
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
			
			//else if(M_objSOURC==txtREGRM)
			else if(M_objSOURC==txtDELAD)
			{
				//if(txtTRPCD.isVisible())
				//{
				 //   txtTRPCD.requestFocus();
			//	}
			//	else
			//	{
    				tbpMAIN.setSelectedIndex(1);
    				tblGRDTL.requestFocus();
    				tblGRDTL.editCellAt(0,intTB1_PRDCD);
    				tblGRDTL.cmpEDITR[intTB1_PRDCD].requestFocus();
    				updateUI();
				//}
			}
			//else if(M_objSOURC==txtTRPCD)
			//{
			//	tbpMAIN.setSelectedIndex(1);
			//	tblGRDTL.requestFocus();
			//	tblGRDTL.editCellAt(0,intTB1_PRDCD);
			//	tblGRDTL.cmpEDITR[intTB1_PRDCD].requestFocus();
			//	updateUI();
			//}
			else if(M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_PKGTP] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_REQQT] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_BASRT] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_EUSCD] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_FRTPM] && M_objSOURC!=tblGRDTL.cmpEDITR[intTB1_FOBRT] )
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
			//	setCMBOCFNO();
			//	cmbOCFNO.requestFocus();
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
				getOVRDUE();
				txtCNSCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtBYRCD2"))
			{
				txtBYRCD.setText(L_STRTKN.nextToken());
				txtBYRNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strBYRDT=txtBYRNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
				txtCNSCD.setText(txtBYRCD.getText());
				txtCNSNM.setText(txtBYRNM.getText());
				txtCNSCD.requestFocus();
			}
			if(M_strHLPFLD.equals("txtDSRCD1"))
			{
				txtDSRNM.setText(L_STRTKN.nextToken());
				txtDSRCD.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strDSRDT=txtDSRNM.getText();
				while (L_STRTKN.hasMoreTokens())
					strDSRDT += "\n"+L_STRTKN.nextToken();
				//txtCNSCD.setText(txtBYRCD.getText());
				//txtCNSNM.setText(txtBYRNM.getText());
				//getOVRDUE();
				//txtAGTCD.requestFocus();
				txtBYRCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtDSRCD2"))
			{
				txtDSRCD.setText(L_STRTKN.nextToken());
				txtDSRNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strDSRDT=txtDSRNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
				//txtCNSCD.setText(txtBYRCD.getText());
				//txtCNSNM.setText(txtBYRNM.getText());
				//txtAGTCD.requestFocus();
				txtBYRCD.requestFocus();
			}
			/**if(M_strHLPFLD.equals("txtAGTCD1"))
			{
				txtAGTNM.setText(L_STRTKN.nextToken());
				txtAGTCD.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strAGTDT=txtAGTNM.getText();
				while (L_STRTKN.hasMoreTokens())
					strAGTDT += "\n"+L_STRTKN.nextToken();
				//txtCNSCD.setText(txtBYRCD.getText());
				//txtCNSNM.setText(txtBYRNM.getText());
				//getOVRDUE();
				txtBYRCD.requestFocus();
			}*/
			/**else if(M_strHLPFLD.equals("txtAGTCD2"))
			{
				txtAGTCD.setText(L_STRTKN.nextToken());
				txtAGTNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strAGTDT=txtAGTNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
				//txtCNSCD.setText(txtBYRCD.getText());
				//txtCNSNM.setText(txtBYRNM.getText());
				txtBYRCD.requestFocus();
			}*/
			if(M_strHLPFLD.equals("txtOCFNO1"))
			{
				txtOCFNO.setText(L_STRTKN.nextToken());
				//L_STRTKN.nextToken();
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
				txtDSTCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtCNSCD2"))
			{
				txtCNSCD.setText(L_STRTKN.nextToken());
				txtCNSNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				strCNSDT=txtCNSNM.getText()+"\n"+L_STRTKN.nextToken()+"\n"+L_STRTKN.nextToken();
				txtDSTCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtDSTCD"))
			{
				txtDSTCD.setText(L_STRTKN.nextToken());
				txtDSTNM.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				//txtFRTPC.setText(String.valueOf(Integer.parseInt(L_STRTKN.nextToken())));
				txtFRTPC.setText(L_STRTKN.nextToken());
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
				tblGRDTL.setValueAt("MT",tblGRDTL.getSelectedRow(),intTB1_UOMCD);
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText(L_strTEMP);
				((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).requestFocus();
				//COMMENTED ON 24/08/07 to allow SAME GRADE WITH DIFFERENT PACKING TYPE IN MR_OCTRN
				/**for(int i=0;i<tblGRDTL.getSelectedRow();i++)
					if(tblGRDTL.getValueAt(i,intTB1_PRDCD).equals(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText()))
					{//AVOIDS DUPLICATION OF GRADE ENTRIES IN AN ORDER
						setMSG("Grade is Already Added ..",'E');
						((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText("");
						tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_PRDDS);
						tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_BASRT);
						return;
					}*/
				for(int i=0;i<tblGRDTL.getSelectedRow();i++)
					if(tblGRDTL.getValueAt(i,intTB1_PRDCD).equals(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText()) && tblGRDTL.getValueAt(i,intTB1_PKGTP).equals(((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText()))
					{//AVOIDS DUPLICATION OF GRADE ENTRIES IN AN ORDER
						setMSG("Grade+Packing Type is Already Added ..",'E');
						((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).setText("");
						tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_PRDDS);
						tblGRDTL.setValueAt("",tblGRDTL.getSelectedRow(),intTB1_BASRT);
						return;
					}
			}
			else if(M_strHLPFLD.equals("txtGRDCD3"))
			{
			    
				String L_strTEMP=L_STRTKN.nextToken();
				((JTextField)tblDLSCH.cmpEDITR[intTB5_PRDCD]).setText(L_strTEMP);
				StringTokenizer L_STRTK1=new StringTokenizer(hstPRDDT.get(L_strTEMP).toString(),"|");
				tblDLSCH.setValueAt(Boolean.TRUE,tblDLSCH.getSelectedRow(),intTB5_CHKFL);
				tblDLSCH.setValueAt(L_STRTKN.nextToken(),tblDLSCH.getSelectedRow(),intTB5_PRDDS);
				tblDLSCH.setValueAt(L_STRTK1.nextToken(),tblDLSCH.getSelectedRow(),intTB5_PKGTP);
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
				//txtFOBRT.setText(L_STRTKN.nextToken());
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
			else if(M_strHLPFLD.equals("OCFNO"))
			{
				String L_strTEMP=L_STRTKN.nextToken();
				//cmbOCFNO.setSelectedItem(L_strTEMP.substring(0,4));
				txtOCFNO.setText(L_strTEMP.substring(4));
				txtOCFDT.setText(L_STRTKN.nextToken());
				txtBYRNM.setText(L_STRTKN.nextToken());
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
		txtDSRCD.setEnabled(true);
		txtDSRNM.setEnabled(false);
		//txtAGTCD.setEnabled(true);
		//txtAGTNM.setEnabled(false);
		txtDSTNM.setEnabled(false);
		txtBKGDT.setEnabled(false);
		txtTRPNM.setEnabled(false);
		
		tblGRDTL.cmpEDITR[0].setEnabled(false);
		tblGRDTL.cmpEDITR[intTB1_PRDDS].setEnabled(false);
		tblGRDTL.cmpEDITR[intTB1_UOMCD].setEnabled(false);
		tblGRDTL.cmpEDITR[intTB1_OCFPK].setEnabled(false);
		//tblGRDTL.cmpEDITR[intTB1_FRTPM].setEnabled(false);
		//tblGRDTL.cmpEDITR[intTB1_FOBRT].setEnabled(false);
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
		chkOCFDSP.setEnabled(true);
		chkREDTAX.setEnabled(true);
		btnPRINT.setEnabled(false);
	}

	
	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		//System.out.println("80");
		cmbMKTTP.removeFocusListener(this);
		cmbSALTP.removeFocusListener(this);
		//System.out.println("81");
		super.clrCOMP();
		//System.out.println("82");
		//lblDSRDUE.setText("");
		//System.out.println("83");
		lblBYRDUE.setText("");
		//System.out.println("84");
		setDFTSTS();
		//System.out.println("85");
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
				txtOCFDT.setText(cl_dat.M_strLOGDT_pbst);
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
		}
		if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
			if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP))
				{txtCURCD.setText("01");txtECHRT.setText("1");}
		lblSTSDS.setText("");
		chbTSHFL.setSelected(true);
		chbPSHFL.setSelected(true);
		cmbSALTP.addFocusListener(this);
		cmbMKTTP.addFocusListener(this);
	}



	/** Restoring default Key Values after clearing components 
	 * on the entry screen
	 */
	private void clrCOMP_1()
	{
		try
		{
			String L_strSALTP = getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString());
			String L_strMKTTP = getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString());
			//String L_strOCFNO1 = cmbOCFNO.getSelectedItem().toString();
			String L_strOCFNO2 = txtOCFNO.getText();
			clrCOMP();
			setCMBDFT(cmbMKTTP,"MSTCOXXMKT",L_strMKTTP);
			setCMBDFT(cmbSALTP,"SYSMR00SAL",L_strSALTP);
			//setCMBDFT_1(cmbOCFNO,L_strOCFNO1);
			txtOCFNO.setText(L_strOCFNO2);
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
} /** Data validation before saving the record
 */
boolean vldDATA()
	{
		try
		{
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
			if(M_strSBSCD.equals("511600")) 
			{
                // added on 11/10/2006 API SRD
			    //for captive consumption set default delivery schedule
			    setDFDEL(); 
			}
			tblGRDTL.setRowSelectionInterval(0,0);
			tblGRDTL.setColumnSelectionInterval(0,0);
			tblCOTAX.setRowSelectionInterval(0,0);
			tblCOTAX.setColumnSelectionInterval(0,0);
			//if(!chkEMPTY_CMB(cmbOCFNO,"Enter Order Number ...")) return false;
			//if(!chkEMPTY_TXT(lblDSRTP,"Enter Distributor Type ..."))        return false;
			if(!chkEMPTY_TXT(txtBYRCD,"Enter Buyer ..."))        return false;
			if(!chkEMPTY_TXT(txtDSRCD,"Enter Distributor ..."))        return false;
			//if(!chkEMPTY_TXT(txtAGTCD,"Enter Agent ..."))        return false;
			if(!chkEMPTY_TXT(txtCNSCD,"Enter Consignee ..."))    return false;
			if(!chkEMPTY_TXT(txtDSTCD,"Enter Destination ..."))  return false;
        	if(!chkEMPTY_TXT(txtOCFDT,"Enter Order Date ..."))   return false;
			if(!chkEXCISE("Excise Duty Not specified properly ..."))   return false;
			if(!chkPARTY("C",txtBYRCD.getText(),"Invalid Buyer Code","Y"))		 return false;
			if(!chkPARTY("D",txtDSRCD.getText(),"Invalid Distributor Code","Y"))		 return false;
			//if(!chkPARTY("A",txtAGTCD.getText(),"Invalid Agent Code","Y"))		 return false;
			//i am here
			if(!chkPARTY("C",txtCNSCD.getText(),"Invalid Consignee code","Y"))	 return false;
			if(txtTRPCD.isVisible())
			{
			     if(!M_strSBSCD.equals("511600"))
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
			if(txtASHDT.getText().length()==0)			{
				setMSG("Agreed Shipment Date Cannot be Blank ..",'E');
				txtASHDT.requestFocus();
				return false;
			}
			if(txtLSHDT.getText().length()==0)			{
				setMSG("Last Shipment Date Cannot be Blank ..",'E');
				txtLSHDT.requestFocus();
				return false;
			}
			if(txtLCEDT.getText().length()==0)			{
				setMSG("L/C Establishment Date Cannot be Blank ..",'E');
				txtLCEDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtREGDT.getText()).compareTo(M_fmtLCDAT.parse(txtOCFDT.getText()))<0)
			{
				setMSG("Registration Date Cannot be Less that Order Date ..",'E');
				txtREGDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtASHDT.getText()).compareTo(M_fmtLCDAT.parse(txtOCFDT.getText()))<0)
			{
				setMSG("Agreed Shipment Date Cannot be Less that Order Date ..",'E');
				txtASHDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtLSHDT.getText()).compareTo(M_fmtLCDAT.parse(txtOCFDT.getText()))<0)
			{
				setMSG("Last Shipment Date Cannot be Less than Order Date ..",'E');
				txtLSHDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtLSHDT.getText()).compareTo(M_fmtLCDAT.parse(txtASHDT.getText()))<0)
			{
				setMSG("Last Shipment Date Cannot be Less than Agreed Shipment Date ..",'E');
				txtLSHDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtLCEDT.getText()).compareTo(M_fmtLCDAT.parse(txtOCFDT.getText()))<0)
			{
				setMSG("L/C Establishment Date Cannot be Less than Order Date ..",'E');
				txtLCEDT.requestFocus();
				return false;
			}
			
			
			if(!M_strSBSCD.equals("511600")) // check released for captive consumption
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
                if(!M_strSBSCD.equals("511600")) // released for captive consumption
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


/** Verifying Excise Duty Entries at Order Confirmation Level
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
				else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("EX7") && tblCOTAX.getValueAt(i,intTB2_AMTFL).toString().equalsIgnoreCase("X"))
				{
					if(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0)
						if(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("03") && !getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals("01")) {L_strERRMSG = "EX7 Excise Code is applicable only for Domestic & Deemed Export Sale"; L_RETFL = false;}
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
			else if(strMKTTP.equals("02")) // R.M.Sale
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
		if(L_OCC01 == false)   // Excise Duty not defined
			{JOptionPane.showMessageDialog(this,"Excise Duty Not Defined"); L_RETFL = false;}
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
					//+ "RM_DOCNO = '"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'";
					+ "RM_DOCNO = '"+txtOCFNO.getText()+"'";
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
			//+setINSSTR("RM_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("RM_DOCNO",txtOCFNO.getText(),"C")
			+setINSSTR("RM_REMDS",LP_REMDS,"C")
			+setINSSTR("RM_SBSCD",M_strSBSCD,"C")
			+setINSSTR("RM_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("RM_TRNFL","0","C")
			+setINSSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("RM_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'A')";   //+setUPDSTR("IN_STSFL",strOC_STSFL,"C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update  MR_RMMST set "
			+setUPDSTR("RM_MKTTP",strMKTTP,"C")
			+setUPDSTR("RM_TRNTP",LP_DOCTP,"C")
			//+setUPDSTR("RM_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setUPDSTR("RM_DOCNO",txtOCFNO.getText(),"C")
			+setUPDSTR("RM_REMDS",LP_REMDS,"C")
			+setUPDSTR("RM_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("RM_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setUPDSTR("RM_TRNFL","0","C")
			+setUPDSTR("RM_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("RM_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"RM_STSFL = '0' where " + strWHRSTR;  //+setUPDSTR("IN_STSFL",strOC_STSFL,"C")
		}
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
		System.out.println(M_strSQLQRY);
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

			strWHRSTR =  "OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP = '" +strMKTTP+"' and "
						 //+"OC_OCFNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'";
						+"OC_OCFNO = '" +txtOCFNO.getText()+"'";
			cl_dat.exeSQLUPD("Insert into MR_OCMAM Select * from MR_OCMST where "+strWHRSTR ,"");

			strWHRSTR =  "OCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCT_MKTTP = '" +strMKTTP+"' and "
						 //+"OCT_OCFNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'";
						 +"OCT_OCFNO = '" +txtOCFNO.getText()+"'";
			cl_dat.exeSQLUPD("Insert into MR_OCTAM Select * from MR_OCTRN where "+strWHRSTR ,"");
			strWHRSTR =  "OCD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCD_MKTTP = '" +strMKTTP+"' and "
						 //+"OCD_OCFNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'";
						 +"OCD_OCFNO = '"+txtOCFNO.getText()+"'";
			cl_dat.exeSQLUPD("Insert into MR_OCDAM Select * from MR_OCDEL where "+strWHRSTR ,"");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveAMND : "+L_EX,'E');}
}

private void saveOCDEL(int LP_ROWNO)
{
   strWHRSTR =  "OCD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCD_MKTTP = '" +strMKTTP+"' and "
					 //+"OCD_OCFNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"' and "
					 +"OCD_OCFNO = '" +txtOCFNO.getText()+"' and "
					 +"OCD_PRDCD = '" +tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString()+"' and "
					 +"OCD_PKGTP = '" +getCODCD("SYSFGXXPKG"+tblDLSCH.getValueAt(LP_ROWNO,intTB5_PKGTP).toString())+"' AND "
					 +"OCD_SRLNO = '" +tblDLSCH.getValueAt(LP_ROWNO,intTB5_SRLNO).toString()+"'";
	flgCHK_EXIST =  chkEXIST("MR_OCDEL", strWHRSTR);
	if(!flgCHK_EXIST)
	{	
        M_strSQLQRY="insert into MR_OCDEL(OCD_CMPCD,OCD_MKTTP,OCD_OCFNO,OCD_PRDCD,OCD_PKGTP,OCD_SRLNO,OCD_AMDNO,OCD_DSPDT,OCD_OCFQT,OCD_DELTP,OCD_TRNFL,OCD_STSFL,OCD_LUSBY,OCD_LUPDT,OCD_SBSCD,OCD_SBSCD1) values ("
    	+setINSSTR("OCD_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
		+setINSSTR("OCD_MKTTP",strMKTTP,"C")
    	//+setINSSTR("OCD_OCFNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
    	+setINSSTR("OCD_OCFNO",txtOCFNO.getText(),"C")
    	+setINSSTR("OCD_PRDCD",tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString(),"C")
    	+setINSSTR("OCD_PKGTP",getCODCD("SYSFGXXPKG"+tblDLSCH.getValueAt(LP_ROWNO,intTB5_PKGTP).toString()),"C")
       	+setINSSTR("OCD_SRLNO",tblDLSCH.getValueAt(LP_ROWNO,intTB5_SRLNO).toString(),"C")
       	+setINSSTR("OCD_AMDNO","00","C")
    	+setINSSTR("OCD_DSPDT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELDT).toString(),"D")
    	+setINSSTR("OCD_OCFQT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"N")
    	+setINSSTR("OCD_DELTP",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELTP).toString(),"C")
    	+setINSSTR("OCD_TRNFL","0","C")
    	+setINSSTR("OCD_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (Float.parseFloat(nvlSTRVL(tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"0.00"))>0.00 ? (rdbAUTH_Y.isSelected() ? "1" : "0") : "X"),"C")
    	+setINSSTR("OCD_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
    	+setINSSTR("OCD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
        +"'"+M_strSBSCD+"',"
		+"'"+M_strSBSCD.substring(0,2)+getPRDTP(tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString())+"00')";
	}
	else
	{
	    M_strSQLQRY="UPDATE MR_OCDEL SET "
    //	+setUPDSTR("IND_MKTTP",strMKTTP,"C")
    //	+setINSSTR("IND_INDNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
    //	+setINSSTR("IND_PRDCD",tblDLSCH.getValueAt(LP_ROWNO,intTB5_PRDCD).toString(),"C")
    //	+setINSSTR("IND_PKGTP",getCODCD("SYSFGXXPKG"+tblDLSCH.getValueAt(LP_ROWNO,intTB5_PKGTP).toString()),"C")
      // 	+setINSSTR("IND_SRLNO",tblDLSCH.getValueAt(LP_ROWNO,intTB5_SRLNO).toString(),"C")
    	+setUPDSTR("OCD_AMDNO",txtAMDNO.getText(),"C")
    	+setUPDSTR("OCD_DSPDT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELDT).toString(),"D")
    	+setUPDSTR("OCD_OCFQT",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"N")
    	+setUPDSTR("OCD_DELTP",tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELTP).toString(),"C")
    	+setUPDSTR("OCD_TRNFL","0","C")
    	+setUPDSTR("OCD_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (Float.parseFloat(nvlSTRVL(tblDLSCH.getValueAt(LP_ROWNO,intTB5_DELQT).toString(),"0.00"))>0.00 ? (rdbAUTH_Y.isSelected() ? "1" : "0") : "X"),"C")
    	+setUPDSTR("OCD_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
    	+"OCD_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"'"
        + " WHERE "+strWHRSTR;
	}
	
	cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
}


/** Saving Order Confirmation master Table
 */
private void saveOCMST()
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			if(exeDSPCHK("OCF_IND_INV_DOR_MSG"))
				{cl_dat.M_flgLCUPD_pbst=false; return;}
		saveAMND();		
		if(!cl_dat.M_flgLCUPD_pbst)
			return;

		strWHRSTR =  "OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP = '" +strMKTTP+"' and "
					 //+"OC_OCFNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'";
					 +"OC_OCFNO = '" +txtOCFNO.getText()+"'";
		flgCHK_EXIST =  chkEXIST("MR_OCMST", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in MR_OCMST");
				cl_dat.M_flgLCUPD_pbst=false;
				return;
		}
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY=  "insert into MR_OCMST (OC_CMPCD,OC_MKTTP,OC_OCFNO,OC_OCFDT,OC_AMDNO,OC_AMDDT,";
			M_strSQLQRY+= "	OC_SALTP,OC_DTPCD,OC_PORNO,OC_PORDT,OC_ZONCD,OC_CNSCD,OC_CNSNM,";
			M_strSQLQRY+= "	OC_BYRCD,OC_BYRNM,OC_CURCD,OC_ECHRT,OC_FRTPC,OC_USNRT,OC_ADLCO,OC_USDCF,OC_DSRTP,OC_DSRCD,OC_DSRNM,";
			M_strSQLQRY+= " OC_APTVL,OC_CPTVL,OC_PMTCD,OC_STSFL,OC_LUSBY,OC_LUPDT,";
			M_strSQLQRY+= " OC_PSHFL,OC_SBSCD,OC_SBSCD1,OC_DSTCD,OC_MOTCD,OC_FILRF,OC_PMTRF,OC_INSRF,";
			M_strSQLQRY+= " OC_FORRF,OC_TSHFL,OC_REGBY,OC_REGDT,OC_ASHDT,OC_LSHDT,OC_LCEDT,OC_DTPDS,OC_TRPCD,OC_TRPNM,OC_DELAD,OC_TRNFL) values ("
			+setINSSTR("OC_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("OC_MKTTP",strMKTTP,"C")
			//+setINSSTR("OC_OCFNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("OC_OCFNO",txtOCFNO.getText(),"C")
			+setINSSTR("OC_OCFDT",txtOCFDT.getText(),"D")
			+setINSSTR("OC_AMDNO","00","C")
			+setINSSTR("OC_AMDDT"," ","D")
			+setINSSTR("OC_SALTP",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()) : "","C")
			+setINSSTR("OC_DTPCD",getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()),"C")
			+setINSSTR("OC_PORNO",txtCRFNO.getText(),"C")
			+setINSSTR("OC_PORDT",txtCRFDT.getText(),"D")
			+setINSSTR("OC_ZONCD",M_strSBSCD.substring(0,2),"C")
			+setINSSTR("OC_CNSCD",txtCNSCD.getText(),"C")
			+setINSSTR("OC_CNSNM",txtCNSNM.getText(),"C")
			+setINSSTR("OC_BYRCD",txtBYRCD.getText(),"C")
			+setINSSTR("OC_BYRNM",txtBYRNM.getText(),"C")
            +setINSSTR("OC_CURCD",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? (!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP)&&txtCURCD.getText().length()==0) ? "01" : txtCURCD.getText() : "","C")
            +setINSSTR("OC_ECHRT",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ?(!getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(strSALTP_EXP)&&txtECHRT.getText().length()==0) ? "1.00" : txtECHRT.getText():"","N")
			+setINSSTR("OC_FRTPC",txtFRTPC.getText(),"N")
			+setINSSTR("OC_USNRT",txtUSNRT.getText(),"N")
			+setINSSTR("OC_ADLCO",txtADLCO.getText(),"N")
			+setINSSTR("OC_USDCF",txtUSDCF.getText(),"N")
			+setINSSTR("OC_DSRTP",lblDSRTP.getText(),"C")
			+setINSSTR("OC_DSRCD",txtDSRCD.getText(),"C")
		//	+setINSSTR("IN_DSRNM",txtDSRNM.getText(),"C")
			+setINSSTR("OC_DSRNM",txtDSRNM.getText(),"C")
			//+setINSSTR("OC_AGTCD",txtAGTCD.getText(),"C")
			//+setINSSTR("OC_AGTNM",txtAGTNM.getText(),"C")
			+setINSSTR("OC_APTVL",txtPAYAC.getText(),"N")
			+setINSSTR("OC_CPTVL",txtPAYCT.getText(),"N")
			+setINSSTR("OC_PMTCD",getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()),"C")
		//	+setINSSTR("OC_TRNFL","0","C")
			+setINSSTR("OC_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setINSSTR("OC_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("OC_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setINSSTR("OC_PSHFL",(chbPSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setINSSTR("OC_SBSCD",M_strSBSCD,"C")
			+setINSSTR("OC_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setINSSTR("OC_DSTCD",txtDSTCD.getText(),"C")
			+setINSSTR("OC_MOTCD",getCODCD("SYSMR01MOT"+cmbMOTCD.getSelectedItem().toString()),"C")
			+setINSSTR("OC_FILRF",txtDEFNO.getText(),"C")
			+setINSSTR("OC_PMTRF",txtAPDNO.isVisible() ? txtAPDNO.getText() : (txtLCNO.isVisible() ? txtLCNO.getText() : ""),"C")
			+setINSSTR("OC_INSRF",chbINSFL.isSelected()==true ? "Y" : "N","C")
			+setINSSTR("OC_FORRF",txtFORNO.getText(),"C")
			+setINSSTR("OC_TSHFL",(chbTSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setINSSTR("OC_REGBY",txtREGBY.getText(),"C")
			+setINSSTR("OC_REGDT",txtREGDT.getText(),"D")
			+setINSSTR("OC_ASHDT",txtASHDT.getText(),"D")
			+setINSSTR("OC_LSHDT",txtLSHDT.getText(),"D")
			+setINSSTR("OC_LCEDT",txtLCEDT.getText(),"D")
			+setINSSTR("OC_DTPDS",cmbDTPCD.getSelectedItem().toString(),"C")
			+setINSSTR("OC_TRPCD",txtTRPCD.getText().trim(),"C")
			+setINSSTR("OC_TRPNM",txtTRPNM.getText().trim(),"C")
			+setINSSTR("OC_DELAD",txtDELAD.getText().trim(),"C")
			+"'0')";						 
			////+getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "SVC")+")";    //setINSSTR("IN_SVCRT","0.00","N")
			//M_strSQLQRY="Update CO_CDTRN set CMT_CCSVL='"+txtOCFNO.getText().toString().substring(3,8)+"' where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MR"+strMKTTP+"OCF'";
			////hstOCFNO.put("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),new String[]{"","","","","","","",txtOCFNO.getText(),""});
			//hstOCFNO.put("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF",new String[]{"","","","","","","",txtOCFNO.getText(),""});
			//cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			M_strSQLQRY="Update CO_CDTRN set CMT_CCSVL='"+txtOCFNO.getText().toString().substring(3,8)+"' where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MR"+strMKTTP+"OCF' and LEFT(CMT_CODCD,1)='"+strYRDGT+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update MR_OCMST set "
			+setUPDSTR("OC_OCFDT",txtOCFDT.getText(),"D")
			+setUPDSTR("OC_AMDNO",txtAMDNO.getText(),"C")
			+setUPDSTR("OC_AMDDT",txtAMDDT.getText(),"D")
			+setUPDSTR("OC_SALTP",(cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()) : "","C")
			+setUPDSTR("OC_DTPCD",getCODCD("SYSMRXXDTP"+cmbDTPCD.getSelectedItem().toString()),"C")
			+setUPDSTR("OC_PORNO",txtCRFNO.getText(),"C")
			+setUPDSTR("OC_PORDT",txtCRFDT.getText(),"D")
			+setUPDSTR("OC_ZONCD",M_strSBSCD.substring(0,2),"C")
			+setUPDSTR("OC_CNSCD",txtCNSCD.getText(),"C")
			+setUPDSTR("OC_CNSNM",txtCNSNM.getText(),"C")
			+setUPDSTR("OC_BYRCD",txtBYRCD.getText(),"C")
			+setUPDSTR("OC_BYRNM",txtBYRNM.getText(),"C")
			+setUPDSTR("OC_CURCD",txtCURCD.getText(),"C")
			+setUPDSTR("OC_ECHRT",txtECHRT.getText(),"N")
			+setUPDSTR("OC_FRTPC",txtFRTPC.getText(),"N")
			//+setUPDSTR("OC_FOBRT",txtFOBRT.getText(),"N")
			+setUPDSTR("OC_USNRT",txtUSNRT.getText(),"N")
			+setUPDSTR("OC_ADLCO",txtADLCO.getText(),"N")
			+setUPDSTR("OC_USDCF",txtUSDCF.getText(),"N")
			+setUPDSTR("OC_DSRTP",lblDSRTP.getText(),"C")
			+setUPDSTR("OC_DSRCD",txtDSRCD.getText(),"C")
			//+setUPDSTR("IN_DSRNM",txtDSRNM.getText(),"C")
			+setUPDSTR("OC_DSRNM",txtDSRNM.getText(),"C")
			//+setUPDSTR("OC_AGTCD",txtAGTCD.getText(),"C")
			//+setUPDSTR("OC_AGTNM",txtAGTNM.getText(),"C")
			+setUPDSTR("OC_APTVL",txtPAYAC.getText(),"N")
			+setUPDSTR("OC_CPTVL",txtPAYCT.getText(),"N")
			+setUPDSTR("OC_PMTCD",getCODCD("SYSMRXXPMT"+cmbPMTCD.getSelectedItem().toString()),"C")
			//+setUPDSTR("OC_TRNFL","0","C")
			+setUPDSTR("OC_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setUPDSTR("OC_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("OC_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setUPDSTR("OC_PSHFL",(chbPSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setUPDSTR("OC_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("OC_SBSCD1",M_strSBSCD.substring(0,2)+"XX00","C")
			+setUPDSTR("OC_DSTCD",txtDSTCD.getText(),"C")
			+setUPDSTR("OC_MOTCD",getCODCD("SYSMR01MOT"+cmbMOTCD.getSelectedItem().toString()),"C")
			+setUPDSTR("OC_FILRF",txtDEFNO.getText(),"C")
			+setUPDSTR("OC_PMTRF",txtAPDNO.isVisible() ? txtAPDNO.getText() : (txtLCNO.isVisible() ? txtLCNO.getText() : ""),"C")
			+setUPDSTR("OC_INSRF",chbINSFL.isSelected()==true ? "Y" : "N","C")
			+setUPDSTR("OC_FORRF",txtFORNO.getText(),"C")
			+setUPDSTR("OC_TSHFL",(chbTSHFL.isSelected()==true ? "Y" : "N"),"C")
			+setUPDSTR("OC_REGBY",txtREGBY.getText(),"C")
			+setUPDSTR("OC_REGDT",txtREGDT.getText(),"D")
			+setUPDSTR("OC_ASHDT",txtASHDT.getText(),"D")
			+setUPDSTR("OC_LSHDT",txtLSHDT.getText(),"D")
			+setUPDSTR("OC_LCEDT",txtLCEDT.getText(),"D")
			+setUPDSTR("OC_DTPDS",cmbDTPCD.getSelectedItem().toString(),"C")
			+setUPDSTR("OC_TRPCD",txtTRPCD.getText().trim(),"C")
			+setUPDSTR("OC_TRPNM",txtTRPNM.getText().trim(),"C")
			+setUPDSTR("OC_DELAD",txtDELAD.getText().trim(),"C")
			+"OC_TRNFL ="+ '0'						
			//+"IN_SVCRT = "+getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "SVC")    //setUPDSTR("IN_SVCRT","0.00","N")
			+" where "+strWHRSTR;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		/** Commented on 27/08/2007 to stop incrementing the document no. in CO_CDTRN.
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		//M_strSQLQRY="Update CO_CDTRN set CMT_CCSVL='"+txtOCFNO.getText()+"' where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MR"+strMKTTP+"OCF' and CMT_CODCD = '"+cmbOCFNO.getSelectedItem().toString()+"'";
		M_strSQLQRY="Update CO_CDTRN set CMT_CCSVL='"+txtOCFNO.getText().toString().substring(3,8)+"' where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MR"+strMKTTP+"OCF'";
		//hstOCFNO.put("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF"+cmbOCFNO.getSelectedItem().toString(),new String[]{"","","","","","","",txtOCFNO.getText(),""});
		hstOCFNO.put("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF",new String[]{"","","","","","","",txtOCFNO.getText(),""});
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");*/
		hstOCFNO.put("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF",new String[]{"","","","","","","",txtOCFNO.getText(),""});
		if(rdbAUTH_Y.isSelected())
		{
			cl_eml ocl_eml = new cl_eml();
			if(strMKTTP.equals("01") || strMKTTP.equals("04") || strMKTTP.equals("05"))
			{
				//objRPOCF.txtMKTTP.setText(strMKTTP);
				//objRPOCF.txtOCFNO.setText(cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText());
				//objRPOCF.flgOUTPRN = true;
				//objRPOCF.M_strSBSLS=M_strSBSLS;
				//objRPOCF.M_rdbHTML.setSelected(true);
				//objRPOCF.exePRINT();
				//ocl_eml.sendfile("cms@spl.co.in",cl_dat.M_strREPSTR_pbst+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+".html","Customer Order Authorisation "+strMKTTP+"/"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"The Customer Order "+strMKTTP+"/"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"  Amd.No.:"+txtAMDNO.getText()+" (for "+txtBYRNM.getText().trim()+") has been authorised by "+cl_dat.M_strUSRCD_pbst);
			}
			if(strMKTTP.equals("03"))
				//ocl_eml.sendfile("ptd@spl.co.in",null,"Order Confirmation Authorisation "+strMKTTP+"/"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"The Customer Order "+strMKTTP+"/"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"  Amd.No.:"+txtAMDNO.getText()+" (for "+txtBYRNM.getText().trim()+") has been authorised by "+cl_dat.M_strUSRCD_pbst);
				ocl_eml.sendfile("ptd@spl.co.in",null,"Order Confirmation Authorisation "+strMKTTP+"/"+txtOCFNO.getText(),"The Customer Order "+strMKTTP+"/"+txtOCFNO.getText()+"  Amd.No.:"+txtAMDNO.getText()+" (for "+txtBYRNM.getText().trim()+") has been authorised by "+cl_dat.M_strUSRCD_pbst);
		}
	}
	catch (Exception L_EX)
	{setMSG("Error in saveOCMST : "+L_EX,'E');}
}


private boolean exeDSPCHK(String LP_DSPCT)
{
	Enumeration enmGRDKEYS=hstGRDTL.keys();
	boolean L_flgRETFL = false;
	while(enmGRDKEYS.hasMoreElements())
	{
		String L_strGRDCD = (String)enmGRDKEYS.nextElement();
		if(LP_DSPCT.equalsIgnoreCase("OCF_IND_INV_DOR_MSG"))
		{
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_OCFQT"))>0)
				{JOptionPane.showMessageDialog(this,"Order Confirmation of "+getGRDTL(L_strGRDCD,"GRD_OCFQT")+" MT is prepared for "+getPRMST(L_strGRDCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_INDQT"))>0)
				{JOptionPane.showMessageDialog(this,"Indent of "+getGRDTL(L_strGRDCD,"GRD_INDQT")+" MT is prepared for "+getPRMST(L_strGRDCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_DORQT"))>0)
				{JOptionPane.showMessageDialog(this,"D.O. of "+getGRDTL(L_strGRDCD,"GRD_DORQT")+" MT is prepared for "+getPRMST(L_strGRDCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_INVQT"))>0)
				{JOptionPane.showMessageDialog(this,"Inv. of  "+getGRDTL(L_strGRDCD,"GRD_INVQT")+" MT is prepared for "+getPRMST(L_strGRDCD.substring(0,10),"PR_PRDDS")); L_flgRETFL=true;}
		}
		else if(LP_DSPCT.equalsIgnoreCase("OCF_IND_INV_DOR_NOMSG"))
		{
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_OCFQT"))>0)
				L_flgRETFL=true;
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_INDQT"))>0)
				L_flgRETFL=true;
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


/** Checking for Indent / D.O.preparation / Despatch status against particular grade
 */
private boolean exeDSPCHK1(int LP_ROWNO,String LP_DSPCT)
{
	//System.out.println(LP_DSPCT);
	boolean L_flgRETFL = false;
	//if(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_OCFQT").length()==0)
	//{System.out.println("1");
	 //return false;}
	if(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INDQT").length()==0)
	{System.out.println("2");
	 return false;}
	if(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT").length()==0)
	{System.out.println("3");
	 return false;}
	if(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT").length()==0)
	{		System.out.println("4");
			return false;}
	if(LP_DSPCT.equalsIgnoreCase("IND_INV_DOR_MSG"))
	{
		//if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_OCFQT"))>0)
		//{System.out.println("5");
		 //{JOptionPane.showMessageDialog(this,"Order Cofirmation of  "+getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INDQT")+" MT is prepared for "+getPRMST(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"PR_PRDDS")); L_flgRETFL=true;}}
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INDQT"))>0)
				{JOptionPane.showMessageDialog(this,"Indent of  "+getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT")+" MT is prepared for "+getPRMST(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"PR_PRDDS")); L_flgRETFL=true;}
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT"))>0)
				{JOptionPane.showMessageDialog(this,"D.O. of  "+getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT")+" MT is prepared for "+getPRMST(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"PR_PRDDS")); L_flgRETFL=true;}
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT"))>0)
				{JOptionPane.showMessageDialog(this,"Inv. of  "+getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT")+" MT is prepared for "+getPRMST(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"PR_PRDDS")); L_flgRETFL=true;}
	}
	else if(LP_DSPCT.equalsIgnoreCase("IND_INV_DOR_NOMSG"))
	{
		//if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_OCFQT"))>0)
		//{		System.out.println("6");
		//		L_flgRETFL=true;}
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INDQT"))>0)
				L_flgRETFL=true;
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_DORQT"))>0)
				L_flgRETFL=true;
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT"))>0)
				L_flgRETFL=true;
	}
	else if(LP_DSPCT.equalsIgnoreCase("INV_NOMSG"))
	{
		if(Float.parseFloat(getGRDTL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"GRD_INVQT"))>0)
		{System.out.println("7");
		 L_flgRETFL=true;}
	}
	else
		JOptionPane.showMessageDialog(this,"Invalid Category in exeDSPMSG1");
	return L_flgRETFL;		
}
 /** Saving Order Confirmation transaction
 */
private void saveOCTRN(int LP_ROWNO)
{
	try
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			if(exeDSPCHK1(LP_ROWNO,"OCF_IND_INV_DOR_MSG"))
				{cl_dat.M_flgLCUPD_pbst=false; return;}

		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		strWHRSTR =  "OCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCT_MKTTP = '" +strMKTTP+"' and "
					 //+"OCT_OCFNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"' and "
					 +"OCT_OCFNO = '" +txtOCFNO.getText()+"' and "
					 +"OCT_PRDCD = '" +tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString()+"' and "
					 +"OCT_PKGTP = '" +getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString())+"'";

		flgCHK_EXIST =  chkEXIST("MR_OCTRN",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in MR_OCTRN");
				cl_dat.M_flgLCUPD_pbst=false;
				return;
		}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY=  "insert into MR_OCTRN (OCT_CMPCD,OCT_MKTTP,OCT_OCFNO,OCT_PRDCD,OCT_PKGTP,";
			M_strSQLQRY+= "OCT_OCFDT,OCT_PRDDS,OCT_PRDGR,OCT_AMDNO,OCT_OCFPK,OCT_PKGWT,";
			M_strSQLQRY+= "OCT_UOMCD,OCT_EUSCD,OCT_REQQT,OCT_OCFQT,OCT_BASRT,OCT_TRNFL,OCT_STSFL,";
			M_strSQLQRY+= "OCT_LUSBY,OCT_LUPDT,OCT_INDQT,OCT_DORQT,OCT_LADQT,OCT_INVQT,";
			M_strSQLQRY+= "OCT_FCMQT,OCT_CFWQT,OCT_SBSCD,OCT_SBSCD1,OCT_SRLNO,OCT_CC1VL,OCT_CC1RF,";
			M_strSQLQRY+= "OCT_CC2VL,OCT_CC2RF,OCT_CC3VL,OCT_EXCRT,OCT_DCMVL,OCT_CDCVL,OCT_FRTPM,OCT_FOBRT,";
			M_strSQLQRY+= "OCT_TDCVL,OCT_DDCVL) values ("
			+setINSSTR("OCT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("OCT_MKTTP",strMKTTP,"C")
			//+setINSSTR("OCT_OCFNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("OCT_OCFNO",txtOCFNO.getText(),"C")
			+setINSSTR("OCT_PRDCD",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString(),"C")
			+setINSSTR("OCT_PKGTP",getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"C")
			+setINSSTR("OCT_OCFDT",txtOCFDT.getText(),"D")
			+setINSSTR("OCT_PRDDS",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString(),"C")
			+setINSSTR("OCT_PRDGR",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString().substring(0,2),"C")
			+setINSSTR("OCT_AMDNO",txtAMDNO.getText(),"C")
			+setINSSTR("OCT_OCFPK",String.valueOf(Float.parseFloat(tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString())/Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN))),"N")
			+setINSSTR("OCT_PKGWT",getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN),"N")
			+setINSSTR("OCT_UOMCD",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setINSSTR("OCT_EUSCD",getCODCD("SYSMR00EUS"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_EUSCD).toString()),"C")
			+setINSSTR("OCT_REQQT",tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString(),"N")
			+setINSSTR("OCT_OCFQT",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "0.00" : (rdbAUTH_Y.isSelected() ? tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString() : "0"),"N")		
			+setINSSTR("OCT_BASRT",tblGRDTL.getValueAt(LP_ROWNO,intTB1_BASRT).toString(),"N")
			+setINSSTR("OCT_TRNFL","0","C")
			+setINSSTR("OCT_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (rdbAUTH_Y.isSelected() ? "1" : "0"),"C")
			+setINSSTR("OCT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("OCT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setINSSTR("OCT_INDQT","0","N")		
			+setINSSTR("OCT_DORQT","0","N")		
			+setINSSTR("OCT_LADQT","0","N")
			+setINSSTR("OCT_INVQT","0","N")
			+setINSSTR("OCT_FCMQT","0","N")
			+setINSSTR("OCT_CFWQT","0","N")
			+setINSSTR("OCT_SBSCD",M_strSBSCD,"C")
			+setINSSTR("OCT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString())+"00","C")
			+setINSSTR("OCT_SRLNO",LP_ROWNO <10 ? "0"+Integer.toString(LP_ROWNO) : Integer.toString(LP_ROWNO),"C")
			+setINSSTR("OCT_CC1VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
			+setINSSTR("OCT_CC1RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString().length()>0 ? "C"+txtBYRCD.getText() : " ","C")
			+setINSSTR("OCT_CC2VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"N")
			+setINSSTR("OCT_CC2RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString().length()>0 ? lblDSRTP.getText()+txtDSRCD.getText():  " ","C")
			+setINSSTR("OCT_CC3VL","0","N")
		//	+setINSSTR("INT_CC3RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString().length()>0 ? "C"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCRF).toString() : "  ","C")
			+setINSSTR("OCT_EXCRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "EXC"),"N")
			//+setINSSTR("OCT_DCMVL",getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbOCFNO.getSelectedItem().toString(),"CMT_NCSVL", hstCDTRN),"N")
			+setINSSTR("OCT_DCMVL",getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_NCSVL", hstCDTRN),"N")
			+setINSSTR("OCT_CDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
			+setINSSTR("OCT_FRTPM",tblGRDTL.getValueAt(LP_ROWNO,intTB1_FRTPM).toString(),"N")
			+setINSSTR("OCT_FOBRT",tblGRDTL.getValueAt(LP_ROWNO,intTB1_FOBRT).toString(),"N")
            +setINSSTR("OCT_TDCVL","0","N")
		//	+setINSSTR("INT_DDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"N")
		    +nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"0")+")"; 
		//	+nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"0")+")"; //+setINSSTR("INT_TDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"N")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update MR_OCTRN set "
			+setUPDSTR("OCT_OCFDT",txtOCFDT.getText(),"D")
			+setUPDSTR("OCT_PRDDS",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString(),"C")
			+setUPDSTR("OCT_PRDGR",tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDDS).toString().substring(0,2),"C")
			+setUPDSTR("OCT_AMDNO",txtAMDNO.getText(),"C")
			+setUPDSTR("OCT_OCFPK",String.valueOf(Float.parseFloat(tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString())/Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN))),"N")
			+setUPDSTR("OCT_PKGWT",getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN),"N")
			+setUPDSTR("OCT_UOMCD",tblGRDTL.getValueAt(LP_ROWNO,intTB1_UOMCD).toString(),"C")
			+setUPDSTR("OCT_EUSCD",getCODCD("SYSMR00EUS"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_EUSCD).toString()),"C")
			+setUPDSTR("OCT_REQQT",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "0.00" : tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString(),"N")
			+setUPDSTR("OCT_OCFQT",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "0.00" : (rdbAUTH_Y.isSelected() ? tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString() : "0"),"N")		
			+setUPDSTR("OCT_BASRT",tblGRDTL.getValueAt(LP_ROWNO,intTB1_BASRT).toString(),"N")
			+setUPDSTR("OCT_TRNFL","0","C")
			+setUPDSTR("OCT_STSFL",cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ? "X" : (Float.parseFloat(nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_REQQT).toString(),"0.00"))>0.00 ? (rdbAUTH_Y.isSelected() ? "1" : "0") : "X"),"C")
			+setUPDSTR("OCT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("OCT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+setUPDSTR("OCT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("OCT_SBSCD1",M_strSBSCD.substring(0,2)+getPRDTP(tblGRDTL.getValueAt(LP_ROWNO,intTB1_PRDCD).toString())+"00","C")
			+setUPDSTR("OCT_SRLNO",LP_ROWNO <10 ? "0"+Integer.toString(LP_ROWNO) : Integer.toString(LP_ROWNO),"C")
			+setUPDSTR("OCT_CC1VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
			+setUPDSTR("OCT_CC1RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString().length()>0 ? "C"+txtBYRCD.getText() : " ","C")
			+setUPDSTR("OCT_CC2VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"N")
			+setUPDSTR("OCT_CC2RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString().length()>0 ? lblDSRTP.getText()+txtDSRCD.getText():  " ","C")
		//	+setUPDSTR("INT_CC3VL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"N")
		//	+setUPDSTR("INT_CC3RF",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString().length()>0 ? "C"+tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCRF).toString() : "  ","C")
			+setUPDSTR("OCT_EXCRT",getTXVAL(tblCOTAX, intTB2_TAXCD, intTB2_TAXVL, "EXC"),"N")
			//+setUPDSTR("OCT_DCMVL",getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"IND"+cmbOCFNO.getSelectedItem().toString(),"CMT_NCSVL", hstCDTRN),"N")
			+setUPDSTR("OCT_DCMVL",getCDTRN("D"+cl_dat.M_strCMPCD_pbst+"MR"+strMKTTP+"OCF","CMT_NCSVL", hstCDTRN),"N")
			+setUPDSTR("OCT_CDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_CDCVL).toString(),"N")
			+setUPDSTR("OCT_FRTPM",tblGRDTL.getValueAt(LP_ROWNO,intTB1_FRTPM).toString(),"N")
			+setUPDSTR("OCT_FOBRT",tblGRDTL.getValueAt(LP_ROWNO,intTB1_FOBRT).toString(),"N")
			+"OCT_DDCVL = "+nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_DDCVL).toString(),"0")
		//	+"INT_TDCVL = "+nvlSTRVL(tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"0") //+setUPDSTR("INT_TDCVL",tblGRDTL.getValueAt(LP_ROWNO,intTB1_TDCVL).toString(),"N")
			+" where "+ strWHRSTR;
		}
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveOCTRN : "+L_EX,'E');}
} 
/** Saving record in CO_TXDOC,  Indent level
 */
private void saveTXDOC_OCF()
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		strWHRSTR =   " TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD =     'MR'  and "
					 +"TX_SBSTP = '" +strMKTTP+"' and "
					 +"TX_DOCTP =     'OCF'  and "
					 //+"TX_DOCNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+ "' and "
					 +"TX_DOCNO = '" +txtOCFNO.getText()+ "' and "
					 +"TX_PRDCD = 'XXXXXXXXXX'";
		flgCHK_EXIST =  chkEXIST("CO_TXDOC",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXDOC");
				return;
		}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_SBSCD,TX_TRNTP,"
			+getTXDOC_OCF0()
			+"TX_TRNFL,TX_LUSBY,TX_LUPDT,TX_STSFL) values("
			+setINSSTR("TX_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TX_SYSCD","MR","C")
			+setINSSTR("TX_SBSTP",strMKTTP,"C")
			+setINSSTR("TX_DOCTP","OCF","C")
			//+setINSSTR("TX_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("TX_DOCNO",txtOCFNO.getText(),"C")
			+setINSSTR("TX_PRDCD","XXXXXXXXXX","C")
			+setINSSTR("TX_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TX_TRNTP","M","C")
			+getTXDOC_OCF1("INS")
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
			+getTXDOC_OCF1("UPD")
			+setUPDSTR("TX_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TX_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"TX_STSFL = '0' where "+strWHRSTR;  // setUPDSTR("TX_STSFL","0","C")
		}
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXDOC_OCF : "+L_EX,'E');}
}



/**
 * Building up field specification part for Insert statement through iteration
 * Example  Insert into .... ( ......  TX_DSBVL,TX_DSBFL,EXCVL,EXCFL, .....) .....
 */
private String getTXDOC_OCF0()
{
	String L_strTXDOC_OCF0 = "";
	for(int i=0;i<tblCOTAX.getRowCount();i++)
	{
		if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().length()!=3)  // tax code not available
			continue;
		if(!getCDTRN("SYSCOXXTAX"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  //does not belong to Common Tax Category
			continue;
		L_strTXDOC_OCF0 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL","TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL","N");
		L_strTXDOC_OCF0 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL","TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL","N");
		if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("STX"))
			L_strTXDOC_OCF0 += setINSSTR("TX_STXDS","TX_STXDS","N");
		else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("OTH"))
			L_strTXDOC_OCF0 += setINSSTR("TX_OTHDS","TX_OTHDS","N");
	}
    return L_strTXDOC_OCF0;
}


/** Builds up value part of insert/update query  for Tax Code related fields
 * Example : insert into CO_TXDOC (............) values (.....120,'A',16,'P',.....)
 *           update CO_TXDOC set ...... TX_DSBVL=120,TX_DSBFL='A',TX_EXCVL=16,TX_EXCFL='A',.....
 */
private String getTXDOC_OCF1(String LP_SAVETP)
{
	String L_strTXDOC_OCF1 = "";
	for(int i=0;i<tblCOTAX.getRowCount();i++)
	{
		if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().length()!=3)  // tax code not available
			continue;
		if(!getCDTRN("SYSCOXXTAX"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("01"))  //does not belong to Common Tax Category
			continue;
		if(LP_SAVETP.equalsIgnoreCase("INS"))
		{
			L_strTXDOC_OCF1 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL",tblCOTAX.getValueAt(i,intTB2_TAXVL).toString(),"N");
			L_strTXDOC_OCF1 += setINSSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL",tblCOTAX.getValueAt(i,intTB2_AMTFL).toString(),"C");
			if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("STX"))
				L_strTXDOC_OCF1 += setINSSTR("TX_STXDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
			else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("OTH"))
				L_strTXDOC_OCF1 += setINSSTR("TX_OTHDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
		}
		else if(LP_SAVETP.equalsIgnoreCase("UPD"))
		{
			L_strTXDOC_OCF1 += setUPDSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"VL",tblCOTAX.getValueAt(i,intTB2_TAXVL).toString(),"N");
			L_strTXDOC_OCF1 += setUPDSTR("TX_"+tblCOTAX.getValueAt(i,intTB2_TAXCD).toString()+"FL",tblCOTAX.getValueAt(i,intTB2_AMTFL).toString(),"C");
			if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("STX"))
				L_strTXDOC_OCF1 += setUPDSTR("TX_STXDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
			else if(tblCOTAX.getValueAt(i,intTB2_TAXCD).toString().equals("OTH"))
				L_strTXDOC_OCF1 += setUPDSTR("TX_OTHDS",tblCOTAX.getValueAt(i,intTB2_TAXDS).toString(),"C");
		}
	}
    return L_strTXDOC_OCF1;
}




/**  Saving record in CO_TXSPC,   Indent level
 */
private void saveTXSPC_OCF0(int LP_ROWNO)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString().length()!=3)
			return;
		if(!getCDTRN("SYSCOXXTAX"+tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("02"))
		   return;
		strWHRSTR =   " TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD =     'MR'  and "
					 +"TXT_SBSTP = '" +strMKTTP+"' and "
					 +"TXT_DOCTP =     'OCF'  and "
					 //+"TXT_DOCNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+ "' and "
					 +"TXT_DOCNO = '" +txtOCFNO.getText()+ "' and "
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
			M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,TXT_TRNFL,TXT_LUSBY,TXT_LUPDT,TXT_STSFL) values ("
			+setINSSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TXT_SYSCD","MR","C")
			+setINSSTR("TXT_SBSTP",strMKTTP,"C")
			+setINSSTR("TXT_DOCTP","OCF","C")
			//+setINSSTR("TXT_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("TXT_DOCNO",txtOCFNO.getText(),"C")
			+setINSSTR("TXT_PRDCD","XXXXXXXXXX","C")
			+setINSSTR("TXT_CODCD",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXCD).toString(),"C")
			//+setINSSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString().substring(0,30),"C")
			+setINSSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString(),"C")
			+setINSSTR("TXT_CODVL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXVL).toString(),"N")
			+setINSSTR("TXT_CODFL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_AMTFL).toString(),"C")
			+setINSSTR("TXT_PRCSQ",tblCOTAX.getValueAt(LP_ROWNO,intTB2_PRCSQ).toString(),"C")
			+setINSSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TXT_TRNFL","0","C")
			+setINSSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')"; //setINSSTR("TXT_STSFL","XYZ","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXSPC set "
			//+setUPDSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString().substring(0,30),"C")
			+setUPDSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setUPDSTR("TXT_CODDS",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXDS).toString(),"C")
			+setUPDSTR("TXT_CODVL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_TAXVL).toString(),"N")
			+setUPDSTR("TXT_CODFL",tblCOTAX.getValueAt(LP_ROWNO,intTB2_AMTFL).toString(),"C")
			+setUPDSTR("TXT_PRCSQ",tblCOTAX.getValueAt(LP_ROWNO,intTB2_PRCSQ).toString(),"C")
			+setUPDSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("TXT_TRNFL","0","C")
			+setUPDSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"TXT_STSFL = '0' where "+strWHRSTR; //setUPDSTR("TXT_STSFL","XYZ","C")
		}
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXSPC_OCF0 : "+L_EX,'E');}
}



/**  Saving record in CO_TXSPC,   Indent level
 */
private void saveTXSPC_OCF1(int LP_ROWNO)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		if(tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString().length()!=3)
			return;
		if(!getCDTRN("SYSCOXXTAX"+tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString(),"CMT_CHP01",hstCDTRN).equals("02"))
		   return;
		strWHRSTR =   " TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD =     'MR'  and "
					 +"TXT_SBSTP = '" +strMKTTP+"' and "
					 +"TXT_DOCTP =     'OCF'  and "
					 //+"TXT_DOCNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+ "' and "
					 +"TXT_DOCNO = '" +txtOCFNO.getText()+ "' and "
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
			M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,TXT_TRNFL,TXT_LUSBY,TXT_LUPDT,TXT_STSFL) values ("
			+setINSSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TXT_SYSCD","MR","C")
			+setINSSTR("TXT_SBSTP",strMKTTP,"C")
			+setINSSTR("TXT_DOCTP","OCF","C")
			//+setINSSTR("TXT_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("TXT_DOCNO",txtOCFNO.getText(),"C")
			+setINSSTR("TXT_PRDCD","XXXXXXXXXX","C")
			+setINSSTR("TXT_CODCD",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString(),"C")
			+setINSSTR("TXT_CODDS",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXDS).toString().substring(0,30),"C")
			+setINSSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setINSSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setINSSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setINSSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TXT_TRNFL","0","C")
			+setINSSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"'0')"; //setINSSTR("TXT_STSFL","XYZ","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXSPC set "
			+setUPDSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setUPDSTR("TXT_CODDS",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXDS).toString().substring(0,30),"C")
			+setUPDSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setUPDSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setUPDSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setUPDSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("TXT_TRNFL","0","C")
			+setUPDSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
			+"TXT_STSFL = '0' where "+strWHRSTR; //setUPDSTR("TXT_STSFL","XYZ","C")
		}
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	}
	catch (Exception L_EX)
	{setMSG("Error in saveTXSPC_OCF1 : "+L_EX,'E');}
}




/** Saving record in CO_TXDOC, grade level
 */
private void saveTXDOC_PRD(String LP_PRDCD)
{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;
		strWHRSTR =   " TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD =     'MR'  and "
					 +"TX_SBSTP = '" +strMKTTP+"' and "
					 +"TX_DOCTP = 'OCF'  and "
					 //+"TX_DOCNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+ "' and "
					 +"TX_DOCNO = '" +txtOCFNO.getText()+ "' and "
					 +"TX_PRDCD = '"+LP_PRDCD+"'";

		flgCHK_EXIST =  chkEXIST("CO_TXDOC",strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_TXDOC");
				return;
		}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY="Insert into CO_TXDOC(TX_CMPCD,TX_SYSCD,TX_SBSTP,TX_DOCTP,TX_DOCNO,TX_PRDCD,TX_SBSCD,TX_TRNTP,"
			+getTXDOC_PRD0(LP_PRDCD)
			+"TX_TRNFL,TX_LUSBY,TX_LUPDT,TX_STSFL) values("
			+setINSSTR("TX_SYSCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TX_SYSCD","MR","C")
			+setINSSTR("TX_SBSTP",strMKTTP,"C")
			+setINSSTR("TX_DOCTP","OFC","C")
			//+setINSSTR("TX_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("TX_DOCNO",txtOCFNO.getText(),"C")
			+setINSSTR("TX_PRDCD",LP_PRDCD,"C")
			+setINSSTR("TX_SBSCD",M_strSBSCD,"C")
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
		strWHRSTR =   " TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD =     'MR'  and "
					 +"TXT_SBSTP = '" +strMKTTP+"' and "
					 +"TXT_DOCTP =     'OCF'  and "
					 //+"TXT_DOCNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+ "' and "
					 +"TXT_DOCNO = '" +txtOCFNO.getText()+ "' and "
					 +"TXT_PRDCD = '" +tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRDCD).toString()+      "' and "
					 +"TXT_CODCD = '" +tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD)+ "'";

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
			M_strSQLQRY="Insert into CO_TXSPC(TXT_CMPCD,TXT_SYSCD,TXT_SBSTP,TXT_DOCTP,TXT_DOCNO,TXT_PRDCD,TXT_CODCD,TXT_CODDS,TXT_CODVL,TXT_CODFL,TXT_PRCSQ,TXT_SBSCD,TXT_TRNFL,TXT_LUSBY,TXT_LUPDT,TXT_STSFL) values ("
			+setINSSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setINSSTR("TXT_SYSCD","MR","C")
			+setINSSTR("TXT_SBSTP",strMKTTP,"C")
			+setINSSTR("TXT_DOCTP","OCF","C")
			//+setINSSTR("TXT_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
			+setINSSTR("TXT_DOCNO",txtOCFNO.getText(),"C")
			+setINSSTR("TXT_PRDCD",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRDCD).toString(),"C")
			+setINSSTR("TXT_CODCD",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXCD).toString(),"C")
			+setINSSTR("TXT_CODDS",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXDS).toString().substring(0,20),"C")
			+setINSSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setINSSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setINSSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setINSSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setINSSTR("TXT_TRNFL","0","C")
			+setINSSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setINSSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"C")
			+"'0')"; //setINSSTR("TXT_STSFL","XYZ","C")
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY="update CO_TXSPC set "
			+setUPDSTR("TXT_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
			+setUPDSTR("TXT_CODVL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_TAXVL).toString(),"N")
			+setUPDSTR("TXT_CODFL",tblGRTAX.getValueAt(LP_ROWNO,intTB3_AMTFL).toString(),"C")
			+setUPDSTR("TXT_PRCSQ",tblGRTAX.getValueAt(LP_ROWNO,intTB3_PRCSQ).toString(),"C")
			+setUPDSTR("TXT_SBSCD",M_strSBSCD,"C")
			+setUPDSTR("TXT_TRNFL","0","C")
			+setUPDSTR("TXT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+setUPDSTR("TXT_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"C")
			+"TXT_STSFL = '0' where "+strWHRSTR; //setUPDSTR("TXT_STSFL","XYZ","C")
		}
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
		+setINSSTR("TRT_DOCTP","OCF","C")
		//+setINSSTR("TRT_DOCNO",cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"C")
		+setINSSTR("TRT_DOCNO",txtOCFNO.getText(),"C")
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
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
} 
/** Verifying whether tax code related to Master(OCF) / Transaction (PRD) category
 * is entered by the user
 */
private boolean chkTXDOC(String LP_TRNTP)
{
	boolean L_flgRETFL = false;
	if (LP_TRNTP.equalsIgnoreCase("OCF"))
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
				//exeREDTAX(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()), cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText(),"SAVE");
				exeREDTAX(getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString()), txtOCFNO.getText(),"SAVE");

			saveOCMST();
			for(int i=0;i<tblGRDTL.getRowCount()&&tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().length()==10;i++)
				saveOCTRN(i);
			for(int i=0;i<tblDLSCH.getRowCount()&&tblDLSCH.getValueAt(i,intTB5_PRDCD).toString().length()==10;i++)
				saveOCDEL(i);
			saveRMMST("OR",txtREGRM.getText());

			for(int i=0;i<tblCOTAX.getRowCount();i++)
				saveTXSPC_OCF0(i);   //Saving specific tax at Indent level from tblCOTAX
			

			for(int i=0;i<tblGRTAX.getRowCount();i++)
			{
				//Saving specific tax at Indent & at product level
				saveTXSPC_OCF1(i);
				saveTXSPC_PRD1(i);
			}

			
			// Saving Common Tax at Indent Level
			if(chkTXDOC("OCF"))
				saveTXDOC_OCF();
			
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
	/** Marking master (Order level) record as cancelled
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
				//ResultSet L_rstRSLSET = cl_dat.exeSQLQRY2("select OCT_STSFL from MR_OCTRN where OCT_MKTTP='"+strMKTTP+"' and OCT_OCFNO = '"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"' and OCT_STSFL<>'X'");
				ResultSet L_rstRSLSET = cl_dat.exeSQLQRY2("select OCT_STSFL from MR_OCTRN where OCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCT_MKTTP='"+strMKTTP+"' and OCT_OCFNO = '"+txtOCFNO.getText()+"' and OCT_STSFL<>'X'");
				if(L_rstRSLSET!=null)
					if(L_rstRSLSET.next())
						{L_rstRSLSET.close(); return;}
				//cl_dat.exeSQLUPD("Update MR_OCMST set OC_STSFL='X' where OC_MKTTP='"+strMKTTP+"' and OC_OCFNO = '"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'","");
				cl_dat.exeSQLUPD("Update MR_OCMST set OC_STSFL='X' where OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP='"+strMKTTP+"' and OC_OCFNO = '"+txtOCFNO.getText()+"'","");
				if(cl_dat.exeDBCMT("chkDELMAST"))
					//JOptionPane.showMessageDialog(this,"Order Confirmation No."+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+" is cancelled");
					JOptionPane.showMessageDialog(this,"Order Confirmation No."+txtOCFNO.getText()+" is cancelled");
			}
		}
		catch(Exception e)
		{
			setMSG(e,"chkDELMAST");
		}
	}
	
	/**  Displaying details from MR_OCMST
	 */
	private void setOCMST(ResultSet LP_RSLSET)
	{
		try
		{
			lblSTSDS.setText("("+getCDTRN("STSMRXXOCF"+getRSTVAL(LP_RSLSET,"OC_STSFL","C"),"CMT_CODDS",hstCDTRN)+")");
			if(!getRSTVAL(LP_RSLSET,"OC_STSFL","C").equals("0"))
				btnPRINT.setEnabled(true);
			txtOCFDT.setText(getRSTVAL(LP_RSLSET,"OC_OCFDT","D"));
			lblDSRTP.setText(getRSTVAL(LP_RSLSET,"OC_DSRTP","C"));
			txtDSRCD.setText(getRSTVAL(LP_RSLSET,"OC_DSRCD","C"));
			txtDSRNM.setText(getRSTVAL(LP_RSLSET,"OC_DSRNM","C"));
			//txtAGTCD.setText(getRSTVAL(LP_RSLSET,"OC_AGTCD","C"));
			//txtAGTNM.setText(getRSTVAL(LP_RSLSET,"OC_AGTNM","C"));
			txtCNSCD.setText(getRSTVAL(LP_RSLSET,"OC_CNSCD","C"));
			txtCNSNM.setText(getRSTVAL(LP_RSLSET,"OC_CNSNM","C"));
			txtBYRCD.setText(getRSTVAL(LP_RSLSET,"OC_BYRCD","C"));
			txtBYRNM.setText(getRSTVAL(LP_RSLSET,"OC_BYRNM","C"));
			txtBKGDT.setText(getRSTVAL(LP_RSLSET,"OC_BKGDT","D"));
			txtBKGBY.setText(getRSTVAL(LP_RSLSET,"OC_BKGBY","C"));
			flgAMDFL = false;
			if (!getRSTVAL(LP_RSLSET,"OC_STSFL","C").trim().equals("0"))
				flgAMDFL = true;
			String L_strOCAMDNO = getRSTVAL(LP_RSLSET,"OC_AMDNO","C").trim();
			String L_strOCAMDDT = getRSTVAL(LP_RSLSET,"OC_AMDDT","C");
			if (flgAMDFL)
			{
				L_strOCAMDNO = (Integer.parseInt(L_strOCAMDNO)<9 ? "0" : "")+String.valueOf(Integer.parseInt(L_strOCAMDNO)+1);
				L_strOCAMDDT = cl_dat.M_strLOGDT_pbst;
			}
					
			txtAMDNO.setText(L_strOCAMDNO);
			txtAMDDT.setText(L_strOCAMDDT);
			txtPAYAC.setText(getRSTVAL(LP_RSLSET,"OC_APTVL","N"));
			txtPAYCT.setText(getRSTVAL(LP_RSLSET,"OC_CPTVL","N"));
			System.out.println("001");
			cl_dat.M_txtUSER_pbst.setText(getRSTVAL(LP_RSLSET,"OC_LUSBY","C"));
			cl_dat.M_txtDATE_pbst.setText(getRSTVAL(LP_RSLSET,"OC_LUPDT","D"));
			System.out.println("002");
			txtDSTCD.setText(getRSTVAL(LP_RSLSET,"OC_DSTCD","C"));
			txtCRFNO.setText(getRSTVAL(LP_RSLSET,"OC_PORNO","C"));
			System.out.println("003");
			txtCRFDT.setText(getRSTVAL(LP_RSLSET,"OC_PORDT","D"));
			txtCURCD.setText(txtCURCD.isVisible() ? getRSTVAL(LP_RSLSET,"OC_CURCD","C"): "");
			System.out.println("004");
			txtTRPCD.setText(txtTRPCD.isVisible() ? getRSTVAL(LP_RSLSET,"OC_TRPCD","C"): "");
			txtTRPNM.setText(txtTRPCD.isVisible() ? getRSTVAL(LP_RSLSET,"OC_TRPNM","C"): "");
			System.out.println("005");
			txtECHRT.setText(txtCURCD.isVisible() ? getRSTVAL(LP_RSLSET,"OC_ECHRT","C") : "");
			txtDEFNO.setText(getRSTVAL(LP_RSLSET,"OC_FILRF","C"));
			System.out.println("006");
			txtAPDNO.setText(txtAPDNO.isVisible() ? getRSTVAL(LP_RSLSET,"OC_PMTRF","C") : "");
			txtLCNO.setText(txtLCNO.isVisible() ? getRSTVAL(LP_RSLSET,"OC_PMTRF","C") : "");
			System.out.println("007");
			chbINSFL.setSelected(getRSTVAL(LP_RSLSET,"OC_INSRF","C").equals("Y"));
			txtFORNO.setText(getRSTVAL(LP_RSLSET,"OC_FORRF","C"));
			System.out.println("008");
			chbTSHFL.setSelected(getRSTVAL(LP_RSLSET,"OC_TSHFL","C").equals("Y"));
			txtREGBY.setText(getRSTVAL(LP_RSLSET,"OC_REGBY","C"));
			System.out.println("009");
			txtREGDT.setText(getRSTVAL(LP_RSLSET,"OC_REGDT","D"));
			txtASHDT.setText(getRSTVAL(LP_RSLSET,"OC_ASHDT","D"));
			System.out.println("010");
			txtLSHDT.setText(getRSTVAL(LP_RSLSET,"OC_LSHDT","D"));
			System.out.println("011");
			txtLCEDT.setText(getRSTVAL(LP_RSLSET,"OC_LCEDT","D"));
			txtFRTPC.setText(getRSTVAL(LP_RSLSET,"OC_FRTPC","N"));
			System.out.println("012");
			//txtFOBRT.setText(getRSTVAL(LP_RSLSET,"OC_FOBRT","N"));
			txtUSNRT.setText(getRSTVAL(LP_RSLSET,"OC_USNRT","N"));
			System.out.println("013");
			txtADLCO.setText(getRSTVAL(LP_RSLSET,"OC_ADLCO","N"));
			txtUSDCF.setText(getRSTVAL(LP_RSLSET,"OC_USDCF","N"));
			System.out.println("014");
			txtDELAD.setText(getRSTVAL(LP_RSLSET,"OC_DELAD","C"));
			setCMBDFT(cmbDTPCD,"SYSMRXXDTP",getRSTVAL(LP_RSLSET,"OC_DTPCD","C"));
			System.out.println("015");
			setCMBDFT(cmbPMTCD,"SYSMRXXPMT",getRSTVAL(LP_RSLSET,"OC_PMTCD","C"));
			setCMBDFT(cmbMOTCD,"SYSMR01MOT",getRSTVAL(LP_RSLSET,"OC_MOTCD","C"));
			System.out.println("016");

			txtDSTNM.setText(getCDTRN("SYSMRXXPOD"+txtDSTCD.getText(),"CMT_CODDS",hstCDTRN));
			System.out.println("017");
		    if(txtTRPCD.isVisible())
		    txtTRPNM.setText(getRSTVAL(LP_RSLSET,"OC_TRPNM","C"));
			System.out.println("018");
			chbPSHFL.setSelected(getRSTVAL(LP_RSLSET,"OC_PSHFL","C").equals("Y"));
			
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
			System.out.println("Error in setOCMST : "+L_SE.toString()); 
		}
	}
	
	/**  Displaying details from MR_INTRN into Grade detail Entry Table
	 */
	private void setOCTRN(ResultSet LP_RSLSET, int LP_ROWVL)
	{
		try
		{
			crtGRDTL(LP_RSLSET);
			tblGRDTL.setValueAt(new Boolean(true),LP_ROWVL,intTB1_CHKFL);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_PRDCD","C"),LP_ROWVL,intTB1_PRDCD);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_PRDDS","C"),LP_ROWVL,intTB1_PRDDS);
			tblGRDTL.setValueAt(getCDTRN("SYSFGXXPKG"+getRSTVAL(LP_RSLSET,"OCT_PKGTP","C"),"CMT_CODDS",hstCDTRN),LP_ROWVL,intTB1_PKGTP);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_REQQT","N"),LP_ROWVL,intTB1_REQQT);
			tblGRDTL.setValueAt(String.valueOf(new Float(Float.parseFloat(getRSTVAL(LP_RSLSET,"OCT_REQQT","N"))/Float.parseFloat(getCDTRN("SYSFGXXPKG"+getRSTVAL(LP_RSLSET,"OCT_PKGTP","C"),"CMT_NCSVL",hstCDTRN).length()==0 ? "1" : getCDTRN("SYSFGXXPKG"+getRSTVAL(LP_RSLSET,"OCT_PKGTP","C"),"CMT_NCSVL",hstCDTRN))).intValue()),LP_ROWVL,intTB1_OCFPK);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_BASRT","N"),LP_ROWVL,intTB1_BASRT);
			tblGRDTL.setValueAt(getCDTRN("SYSMR00EUS"+getRSTVAL(LP_RSLSET,"OCT_EUSCD","C"),"CMT_CODDS",hstCDTRN),LP_ROWVL,intTB1_EUSCD);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_CC1VL","N"),LP_ROWVL,intTB1_CDCVL);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_FRTPM","N"),LP_ROWVL,intTB1_FRTPM);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_FOBRT","N"),LP_ROWVL,intTB1_FOBRT);
			tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"OCT_CC2VL","N"),LP_ROWVL,intTB1_DDCVL);
		//	tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"INT_CC3VL","N"),LP_ROWVL,intTB1_TDCVL);
		//	tblGRDTL.setValueAt(getRSTVAL(LP_RSLSET,"INT_CC3RF","C").length()>5 ? getRSTVAL(LP_RSLSET,"INT_CC3RF","C").substring(1) : "",LP_ROWVL,intTB1_TDCRF);
			tblGRDTL.setValueAt("MT",LP_ROWVL,intTB1_UOMCD);
		}
		catch(Exception L_SE)
		{
			System.out.println("Error in setOCTRN : "+L_SE.toString()); 
		}
	}
	
/**
 * Capturing ing grade details from MR_OCTRN, in modification mode
 */
	private void crtGRDTL(ResultSet LP_RSSET)
	{
        String[] staGRDTL = new String[intGRDTL_TOT];
        staGRDTL[intAE_GRD_REQQT] = getRSTVAL(LP_RSSET,"OCT_REQQT","N");
        staGRDTL[intAE_GRD_OCFQT] = getRSTVAL(LP_RSSET,"OCT_OCFQT","N");
        staGRDTL[intAE_GRD_INDQT] = getRSTVAL(LP_RSSET,"OCT_INDQT","N");
        staGRDTL[intAE_GRD_DORQT] = getRSTVAL(LP_RSSET,"OCT_DORQT","C");
        staGRDTL[intAE_GRD_INVQT] = getRSTVAL(LP_RSSET,"OCT_INVQT","C");
		hstGRDTL.put(getRSTVAL(LP_RSSET,"OCT_PRDCD","C")+getRSTVAL(LP_RSSET,"OCT_PKGTP","C"),staGRDTL);		
	}
	
	
/** Fetching Tax details into CO_TXDOC
 */	
	private void  setCOTAX(ResultSet LP_RSLSET,String LP_TRNTP)
	{
		try
		{
			if(LP_TRNTP.equalsIgnoreCase("OCF"))
			{
				if(!getRSTVAL(LP_RSLSET,"TX_PRDCD","C").equalsIgnoreCase("XXXXXXXXXX"))
					return ;
				  setCOTAX1(LP_RSLSET,"DSB");
				  setCOTAX1(LP_RSLSET,"EXC");
				  setCOTAX1(LP_RSLSET,"EDC");
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
			if(LP_TRNTP.equalsIgnoreCase("OCF"))
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

private void setOCREM()
{
	try
	{
	flgREGRM=false;
	//flgBKGRM=false;
	//flgAUTRM=false;
	ResultSet L_rstRSSET;
	//L_rstRSSET = cl_dat.exeSQLQRY2("Select * from MR_RMMST where RM_MKTTP='"+strMKTTP+"' and RM_DOCNO='"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'");
	L_rstRSSET = cl_dat.exeSQLQRY2("Select * from MR_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MKTTP='"+strMKTTP+"' and RM_DOCNO='"+txtOCFNO.getText()+"' and RM_TRNTP='OR'");

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
				M_strSQLQRY="Select * from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD='MR' "
					+"and TXT_DOCTP='OCF' and TXT_SBSCD='"+M_strSBSCD+"' and "
					//+"txT_DOCNO='"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'"// and "
					+"txT_DOCNO='"+txtOCFNO.getText()+"'"// and "
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

/** Additional check for existance of Order Confirmation record
 * Depending on selection mode Addition / Modification / Deletion
 */
private boolean exeNEWFIND()
{
	boolean L_flgRETFL = false;
	try
	{
		//ResultSet L_rstRSLSET = cl_dat.exeSQLQRY2("select OC_STSFL from MR_OCMST where OC_MKTTP = '"+strMKTTP+"' and OC_OCFNO = '"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'");
		ResultSet L_rstRSLSET = cl_dat.exeSQLQRY2("select OC_STSFL from MR_OCMST where OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP = '"+strMKTTP+"' and OC_OCFNO = '"+txtOCFNO.getText()+"'");
		if(L_rstRSLSET!=null && L_rstRSLSET.next())
		{
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst)) && L_rstRSLSET.getString("OC_STSFL").toString().equalsIgnoreCase("X"))
				//{JOptionPane.showMessageDialog(this,"Order No. "+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+" is already cancelled"); L_flgRETFL = true;}
				{JOptionPane.showMessageDialog(this,"Order No. "+txtOCFNO.getText()+" is already cancelled"); L_flgRETFL = true;}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
				if (L_rstRSLSET.getString("OC_STSFL").toString().equalsIgnoreCase("X"))
					//{JOptionPane.showMessageDialog(this,"Order No. "+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+" is already cancelled"); L_flgRETFL = true;}
					{JOptionPane.showMessageDialog(this,"Order No. "+txtOCFNO.getText()+" is already cancelled"); L_flgRETFL = true;}
			    else
					//{JOptionPane.showMessageDialog(this,"Order No. "+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+" already exists, Go for Modification"); L_flgRETFL = true;}
					{JOptionPane.showMessageDialog(this,"Order No. "+txtOCFNO.getText()+" already exists, Go for Modification"); L_flgRETFL = true;}
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
			String L_strADDSTR = cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst) ? "" : " and OC_STSFL <> 'X' and OCT_STSFL <> 'X'" ;
			M_strSQLQRY="Select * from MR_OCMST,MR_OCTRN where OC_MKTTP=OCT_MKTTP and OC_OCFNO=OCT_OCFNO and OC_CMPCD=OCT_CMPCD "
				//+"and OC_MKTTP='"+strMKTTP+"' and OC_OCFNO='"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'"
				+"and OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP='"+strMKTTP+"' and OC_OCFNO='"+txtOCFNO.getText()+"'"
				+L_strADDSTR+" order by oct_srlno";

			System.out.println(M_strSQLQRY);
			rstRSSET1=cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(rstRSSET1==null || (!rstRSSET1.next()))
			{
				if(!exeNEWFIND())
					setMSG("Record Not found",'E');
				return;
			}
				

			int row=0;

			inlTBLEDIT(tblGRDTL);
			inlTBLEDIT(tblCOTAX);
			inlTBLEDIT(tblGRTAX);
			
			inlOCMST(rstRSSET1);
			strOC_STSFL=getRSTVAL(rstRSSET1,"OC_STSFL","C");
			if(!chkSBSCD(rstRSSET1))
				return;

			setOCMST(rstRSSET1);
			while(true)
			{
				setOCTRN(rstRSSET1, row);
				row++;
				if (!rstRSSET1.next())
					break;
			}
			rstRSSET1.close();

			//SET ENABLE STATUS WRT D.O. PREPARED OR NOT
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
				setMODFLT(strOC_STSFL);
			setOCREM();  

			ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXDOC where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD='MR' and TX_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TX_DOCTP='OCF' and "
					//+"tx_DOCNO='"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'");
					+"tx_DOCNO='"+txtOCFNO.getText()+"'");
			tblCOTAX.clrTABLE();
			tblGRTAX.clrTABLE();
			intCOTAX_ROW=0;
			intGRTAX_ROW=0;
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					setCOTAX(L_rstRSSET,"OCF");
					setCOTAX(L_rstRSSET,"PRD");
				}
				L_rstRSSET.close();
			}
			L_rstRSSET=cl_dat.exeSQLQRY2("Select * from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD='MR' and TXT_SBSTP = '"+getCODCD("MSTCOXXMKT"+cmbMKTTP.getSelectedItem().toString())+"' and TXT_DOCTP='OCF' and "
					//+"txt_DOCNO='"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'");
					+"txt_DOCNO='"+txtOCFNO.getText()+"'");
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
					setSPTAX(L_rstRSSET,"OCF");
					setSPTAX(L_rstRSSET,"PRD");
				}
				L_rstRSSET.close();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
				{setDSBL("MST"); setDSBL("TBP1"); setDSBL("TBP2"); setDSBL("TBP3"); setDSBL("TBP4");}
			M_strSQLQRY = " SELECT * FROM MR_OCDEL,CO_PRMST WHERE OCD_PRDCD = PR_PRDCD AND "
			//+" OCD_MKTTP='"+strMKTTP+"' and OCD_OCFNO='"+cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'"+
			+" OCD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCD_MKTTP='"+strMKTTP+"' and OCD_OCFNO='"+txtOCFNO.getText()+"'"+
				" AND OCD_STSFL <>'X' order by ocd_prdcd,ocd_srlno";	
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
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
			if(M_strSBSCD.substring(0,2).equals(getRSTVAL(LP_RSLSET,"OC_ZONCD","C")) && getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()).equals(getRSTVAL(LP_RSLSET,"OC_SALTP","C")))
				return true;
			setMSG("Zone & Product Type Mismatch: Actual = "+getRSTVAL(LP_RSLSET,"OC_ZONCD","C")+"/"+getRSTVAL(LP_RSLSET,"OC_SALTP","C").substring(2,4)+"  Selected = "+M_strSBSCD.substring(0,2)+"/"+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()),'E');
			//if(M_strSBSCD.equals(getRSTVAL(LP_RSLSET,"OC_ZONCD","C")+getRSTVAL(LP_RSLSET,"OC_SALTP","C")+"00"))
			//	return true;
			//setMSG("Zone & Sale Type Mismatch: Selected = "+getRSTVAL(LP_RSLSET,"OC_ZONCD","C")+"/"+getRSTVAL(LP_RSLSET,"OC_SALTP","C")+"  Original = "+M_strSBSCD.substring(0,2)+"/"+((cmbSALTP.getItemCount() > 0 && cmbSALTP.getSelectedIndex()>0) ? getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString()):""),'E');
		}
		catch (Exception e){setMSG(e,"chkSBSCD");}
		return false;
	}
	
	
	
	/** Extra details taken out from getDATA
	 */
	private void inlOCMST(ResultSet LP_RSLSET)
	{						  
		int L_intTEMP1=cmbMKTTP.getSelectedIndex();
		//int L_intTEMP2=cmbOCFNO.getSelectedIndex();
		String L_strTEMP1=txtOCFNO.getText();
		String L_strTEMP2=txtBYRNM.getText();
		clrCOMP_1();
		cmbMKTTP.setSelectedIndex(L_intTEMP1);
		//cmbOCFNO.setSelectedIndex(L_intTEMP2);

		txtOCFNO.setText(L_strTEMP1);
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
			txtDSRCD.setEnabled(false);
			//txtAGTCD.setEnabled(false);
			txtBYRCD.setEnabled(false);
			txtCNSCD.setEnabled(false);
			txtDSTCD.setEnabled(false);
			txtOCFDT.setEnabled(false);
			txtREGBY.setEnabled(false);
			txtREGDT.setEnabled(false);
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
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst)) && !exeDSPCHK("OCF_IND_INV_DOR_NOMSG"))
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
		//cmbOCFNO.setEnabled(false);
		txtOCFNO.setEnabled(false);
		if(P_strSTSFL.equals("1"))
		{
			setDSBL("MST");
		Enumeration enmGRDKEYS=hstGRDTL.keys();
		boolean L_flgRETFL = false;
		while(enmGRDKEYS.hasMoreElements())
		{
			String L_strGRDCD = (String)enmGRDKEYS.nextElement();
			if(Float.parseFloat(getGRDTL(L_strGRDCD,"GRD_INDQT"))>0)
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
				//		setCMBOCFNO();
				//}
				if(input==txtPAYAC && txtPAYAC.getText().length()==0)
				{
					setMSG("Please enter Account Cr. days ..",'E');
					return false;
				}
				if(input==txtPAYCT && txtPAYCT.getText().length()==0)
				{
					setMSG("Please enter Customer Cr. days ..",'E');
					return false;
				}
				if (input instanceof JTextField&&((JTextField)input).getText().length()==0)	
					return true;
				if(input==txtBYRCD)
				{
					txtBYRCD.setText(txtBYRCD.getText().toUpperCase());
					String L_strSQLQRY = "SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD= '"+txtBYRCD.getText().trim()+"'" +(M_strSBSCD.equals("511600") ? "and PT_PRTCD like 'S77%' " : "")+" and +upper(isnull(PT_STSFL,' ')) <> 'X' ORDER BY PT_PRTNM";
					System.out.println(L_strSQLQRY);
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
					//ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtBYRCD.getText()+"' and pt_PRTTP='C' and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ");
					if(L_rstRSSET!=null)
					{
						if (L_rstRSSET.next())
						{
							txtBYRNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
							strBYRDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
							if(txtDSTCD.getText().length()==0)
								txtDSTCD.setText(getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
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
				if(input==txtDSRCD)
				{
					txtDSRCD.setText(txtDSRCD.getText().trim().toUpperCase());
					//System.out.println(lblDSRTP.getText()+' '+txtDSRCD.getText());
					String L_strSQLQRY = "SELECT isnull(PT_PRTNM,' ') PT_PRTNM,isnull(PT_PRTCD,' ') PT_PRTCD,isnull(PT_ADR01,' ') PT_ADR01,isnull(PT_ADR02,' ') PT_ADR02,isnull(PT_ADR03,' ') PT_ADR03,isnull(PT_SHRNM,' ') PT_SHRNM from CO_PTMST where PT_PRTTP in ('D','A')  and PT_PRTCD= '"+txtDSRCD.getText().trim()+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
										//"Select * from co_ptmst where pt_prtcd='"+txtDSRCD.getText().trim()+"' and pt_PRTTP = '"+lblDSRTP.getText().trim()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText().trim()+"'";
					System.out.println(L_strSQLQRY);
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
					//System.out.println(Select * from co_ptmst where pt_prtcd='"+txtDSRCD.getText()+"' and pt_PRTTP = 'D' and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X');
					//String S = "Select * from co_ptmst where pt_prtcd='"+txtDSRCD.getText()+"' and pt_PRTTP = 'D' and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ";
					//System.out.println(S);
					if(L_rstRSSET!=null)
					{
						if (L_rstRSSET.next())
						{
							txtDSRNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
							strDSRDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
							//System.out.println(lblDSRTP.getText());
							System.out.println(getRSTVAL(L_rstRSSET,"PT_SHRNM","C"));
							System.out.println("100");
							//txtSHRNM.setText(getRSTVAL(L_rstRSSET,"PT_SHRNM","C").toString());
							System.out.println("101");
							//System.out.println(txtSHRNM.getText());
							//if(txtDSTCD.getText().length()==0)
							//	txtDSTCD.setText(getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
							L_rstRSSET.close();
							System.out.println("102");
							//if(txtCNSCD.getText().length()==0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
							//{
							//	txtCNSCD.setText(txtBYRCD.getText());
							//	txtCNSNM.setText(txtBYRNM.getText());
							//}
							setMSG("",'N');
							//getOVRDUE();
						}
						else 
							setMSG("Invalid Distri./Agent Code ..",'E');
					}
					else
						setMSG("Invalid Distri./Agent Code ..",'E');
				}
				/**if(input==txtAGTCD)
				{
					txtAGTCD.setText(txtAGTCD.getText().toUpperCase());
					//System.out.println("1");
					//System.out.println(txtAGTCD.getText());
					//ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtAGTCD.getText()+"' and pt_PRTTP = 'A' and "+(lblAGTTP.getText().equals("G") ? "PT_CNSRF" : "PT_AGTCD")+" = '"+txtAGTCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ");
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtAGTCD.getText()+"' and pt_PRTTP = 'A'  and upper(isnull(PT_STSFL,' ')) <> 'X' ");
					//System.out.println("2");
					//String S = "Select * from co_ptmst where pt_prtcd='"+txtDSRCD.getText()+"' and pt_PRTTP = 'D' and "+(lblDSRTP.getText().equals("G") ? "PT_CNSRF" : "PT_DSRCD")+" = '"+txtDSRCD.getText()+"' and upper(isnull(PT_STSFL,' ')) <> 'X' ";
					//System.out.println(S);
					if(L_rstRSSET!=null)
					{
						if (L_rstRSSET.next())
						{
							txtAGTNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
							//System.out.println("3");
							strAGTDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
									 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
									 +getRSTVAL(L_rstRSSET,	"PT_ADR03","C")+"\n";
							//System.out.println("4");
							//System.out.println(lblAGTTP.getText());
							txtAGTNM.setText(getPTMST1("A"+txtAGTCD.getText(),"PT_SHRNM"));
							//System.out.println("5");
							//if(txtDSTCD.getText().length()==0)
							//	txtDSTCD.setText(getRSTVAL(L_rstRSSET,"PT_DSTCD","C"));
							L_rstRSSET.close();
							//System.out.println("6");
							//if(txtCNSCD.getText().length()==0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
							//{
							//	txtCNSCD.setText(txtBYRCD.getText());
							//	txtCNSNM.setText(txtBYRNM.getText());
							//}
							setMSG("",'N');
							//getOVRDUE();
						}
						else 
							setMSG("Invalid Agent Code ..",'E');
					}
					else
						setMSG("Invalid Agent Code ..",'E');
				}*/
				
				else if(input==txtCNSCD)
				{
					if(!txtBYRCD.getText().equals(txtCNSCD.getText()))
					{
						String L_strSQLQRY = "SELECT PT_PRTNM,PT_PRTCD,PT_ADR01,PT_ADR02,PT_ADR03 from CO_PTMST where PT_PRTTP='C'  and PT_PRTCD= '"+txtCNSCD.getText().trim()+"' and upper(isnull(PT_STSFL,' ')) <> 'X'  ORDER BY PT_PRTNM";
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
						//ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_ptmst where pt_prtcd='"+txtCNSCD.getText()+"' and pt_PRTTP='C' and upper(isnull(PT_STSFL,' ')) <> 'X'");
						if(L_rstRSSET!=null)
						{
							if (L_rstRSSET.next())
							{
								txtCNSNM.setText(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"));
								strCNSDT=getRSTVAL(L_rstRSSET,"PT_PRTNM","C")+"\n"
										 +getRSTVAL(L_rstRSSET,"PT_ADR01","C")+"\n"
										 +getRSTVAL(L_rstRSSET,"PT_ADR02","C")+"\n"
										 +getRSTVAL(L_rstRSSET,"PT_ADR03","C")+"\n";
							if(txtDSTCD.getText().length()==0)
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
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select * from co_cdtrn where cmt_codcd='"+txtDSTCD.getText()+"' and CMT_CGSTP='MRXXPOD'");
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
				/**else if(input==cmbOCFNO)
				{
					if(cmbOCFNO.getSelectedIndex()==0)
					{
						setMSG("Select Disrtibutor Series ..",'E');
						cmbOCFNO.showPopup();
					}
					else
						setMSG("",'N');
				}*/
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
				else if(input==txtOCFDT||input==txtREGBY||input==txtREGDT||input==txtAPDNO
						||input==txtLCNO||input==txtFORNO||input==txtDEFNO||input==txtECHRT||input==txtCURCD)
				{
					if(((JTextField)input).getText().length()==0)
						setMSG("Neccessary information..Please enter data ..",'E');
					else
						setMSG("",'N');
				}
				
				else if(input==txtOCFNO)
				{

					strWHRSTR =  "OC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OC_MKTTP = '" +strMKTTP+"' and "
								 //+"OC_OCFNO = '" +cmbOCFNO.getSelectedItem().toString()+txtOCFNO.getText()+"'";
								 +"OC_OCFNO = '" +txtOCFNO.getText()+"'";
					flgCHK_EXIST =  chkEXIST("MR_OCMST", strWHRSTR);
					//System.out.println(strWHRSTR);
					//I AM HERE
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
					{
							setMSG("Order Confirmation Record alreay exists",'E'); return false;
					}
					else if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && !flgCHK_EXIST)
					{
							setMSG("Order Confirmation Record does not  exist",'E'); return false;
					}
					setMSG("",'N');
				}
				if(M_flgERROR)
					return false;
				return true;
			}catch (Exception e){setMSG(e,"INPVF");return false;}
		}
	} 
private boolean chkOCFPK(int P_intROWID,int P_intCOLID)
{
		if(!getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()).equalsIgnoreCase("99"))
		{
			float L_fltPKGWT = Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+tblGRDTL.getValueAt(P_intROWID,intTB1_PKGTP).toString()),"CMT_NCSVL",hstCDTRN));
			float L_fltPKGNO = (Float.parseFloat(((JTextField)tblGRDTL.cmpEDITR[intTB1_REQQT]).getText().toString())/L_fltPKGWT);
			L_fltPKGNO=Float.parseFloat(setNumberFormat(L_fltPKGNO,3));
			int L_intPKGNO = new Float(L_fltPKGNO).intValue();
			if(L_intPKGNO != L_fltPKGNO)
				{setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_fltPKGWT,3),'E'); return false;}
			tblGRDTL.setValueAt(setNumberFormat(L_intPKGNO,0),P_intROWID,intTB1_OCFPK);
		}
		else
			tblGRDTL.setValueAt(setNumberFormat(Double.parseDouble(tblGRDTL.getValueAt(P_intROWID,intTB1_REQQT).toString())*1000,0),P_intROWID,intTB1_OCFPK);
	return true;
}



/**
 */
private boolean chkOCFPK1()
{
		if(!getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText()).equalsIgnoreCase("99"))
		{
			float L_fltPKGWT = Float.parseFloat(getCDTRN("SYSFGXXPKG"+getCODCD("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText()),"CMT_NCSVL",hstCDTRN));
			
			float L_fltPKGNO = (Float.parseFloat(((JTextField)tblGRDTL.cmpEDITR[intTB1_REQQT]).getText().toString())/L_fltPKGWT);
			L_fltPKGNO=Float.parseFloat(setNumberFormat(L_fltPKGNO,3));
			int L_intPKGNO = new Float(L_fltPKGNO).intValue();
			if(L_intPKGNO != L_fltPKGNO)
				{setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_fltPKGWT,3),'E'); return false;}
			tblGRDTL.setValueAt(setNumberFormat(L_intPKGNO,0),tblGRDTL.getSelectedRow(),intTB1_OCFPK);
		}
		else
			tblGRDTL.setValueAt(((JTextField)tblGRDTL.cmpEDITR[intTB1_REQQT]).getText(),tblGRDTL.getSelectedRow(),intTB1_OCFPK);
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
							if(!M_strSBSCD.equals("511600"))
								//if(!strMKTTP.equals("02"))
									return true;
						if(!hstPRMST.containsKey(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText()))
							{setMSG("Invalid Product Code",'E'); return false;}
						if(M_strSBSCD.equals("511600"))
							{
								if(Double.parseDouble(getPRMST(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString(),"PR_AVGRT"))<=0)
									{setMSG("Rate not available",'E'); return false;}
								tblGRDTL.setValueAt(getPRMST(((JTextField)tblGRDTL.cmpEDITR[intTB1_PRDCD]).getText().toString(),"PR_AVGRT"),P_intROWID,intTB1_BASRT);
								tblGRDTL.setValueAt(getCDTRN("SYSMR00EUS"+"99","CMT_CODDS",hstCDTRN),P_intROWID,intTB1_EUSCD);
								tblGRDTL.cmpEDITR[intTB1_BASRT].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_EUSCD].setEnabled(false);
								tblGRDTL.cmpEDITR[intTB1_CDCVL].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_DDCVL].setEnabled(false);
							//	tblGRDTL.cmpEDITR[intTB1_TDCVL].setEnabled(false);tblGRDTL.cmpEDITR[intTB1_TDCRF].setEnabled(false);
							}
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
								//if(!chkOCFPK1())
								//	return false;
								if(!chkOCFPK(P_intROWID,P_intCOLID))
									return false;
						float L_fltREQQT = 	Float.parseFloat(nvlSTRVL(tblGRDTL.getValueAt(P_intROWID,intTB1_REQQT).toString(),"0"));
						float L_fltFRTPC = 	Float.parseFloat(nvlSTRVL(txtFRTPC.getText().toString(),"0"));
						//System.out.println(L_fltREQQT);
						//System.out.println(L_fltFRTPC);
						//System.out.println(L_fltFRTPC/L_fltREQQT);
						tblGRDTL.setValueAt(setNumberFormat(L_fltFRTPC/20.5,3),P_intROWID,intTB1_FRTPM);
						return true;
					}
					//if(P_intCOLID==intTB1_FOBRT)
					//{
					//	System.out.println(getCDTRN("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString(),"CMT_NMP02",hstCDTRN));
					//	tblGRDTL.setValueAt(getCDTRN("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString(),"CMT_NMP02",hstCDTRN),P_intROWID,intTB1_FOBRT);				
					//}
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
							if(tblGRDTL.getValueAt(P_intROWID,intTB1_PRDCD).toString().length()==0)
							{	
								return true;
							}
							String L_strPKGTP = "";
							boolean L_flgPKGTP = false;
							//System.out.println("PKGTP : "+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString());
							if(hstCODDS.containsKey("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()))
								{L_strPKGTP = hstCODDS.get("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()).toString(); L_flgPKGTP = true;}
							else if(hstCDTRN.containsKey("SYSFGXXPKG"+((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString()))
								{L_strPKGTP = ((JTextField)tblGRDTL.cmpEDITR[intTB1_PKGTP]).getText().toString(); L_flgPKGTP = true;}
								
							if((L_flgPKGTP && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (L_flgPKGTP && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
							{
								tblGRDTL.setValueAt(getCDTRN("SYSFGXXPKG"+L_strPKGTP,"CMT_CODDS",hstCDTRN),P_intROWID,intTB1_PKGTP); 
								//System.out.println("INPVF1");
								//System.out.println(getCDTRN("SYSFGXXPKG"+L_strPKGTP,"CMT_NMP02",hstCDTRN));
								tblGRDTL.setValueAt(getCDTRN("SYSFGXXPKG"+L_strPKGTP,"CMT_NMP02",hstCDTRN),P_intROWID,intTB1_FOBRT);				
								return true;
							}
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
						        
						        if(!tblDLSCH.getValueAt(P_intROWID - 1,intTB5_PRDCD).toString().equals(tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString()))
						         {  
						            // check the max no. entered for this grade
						           // tblDLSCH.setValueAt("01",P_intROWID ,intTB5_SRLNO);
						            tblDLSCH.setValueAt(getSRLNO(P_intROWID),P_intROWID ,intTB5_SRLNO);
						         }
						        else
						        {
						            if(tblDLSCH.getValueAt(P_intROWID - 1,intTB5_PRDCD).toString().equals(tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString())  && tblDLSCH.getValueAt(P_intROWID - 1,intTB5_PKGTP).toString().equals(tblDLSCH.getValueAt(P_intROWID,intTB5_PKGTP).toString())  && tblDLSCH.getValueAt(P_intROWID -1 ,intTB5_DELTP).toString().equals("I"))
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
                                if(tblGRDTL.getValueAt(i,intTB1_PRDCD).toString().equals(tblDLSCH.getValueAt(P_intROWID,intTB5_PRDCD).toString())  && tblGRDTL.getValueAt(i,intTB1_PKGTP).toString().equals(tblDLSCH.getValueAt(P_intROWID,intTB5_PKGTP).toString()))        
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
    						  else if(tblDLSCH.getValueAt(P_intROWID,intTB5_DELTP).toString().equals("S"))
    						  {
                                  L_dblSCHQT = getTOTQT(P_intROWID);
    						      if(L_dblSCHQT > L_dblGRDQT)
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
        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl not in ('X') and SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54','68','SX') order by pr_prdds";
        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
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
	
	
	
		/** One time data capturing for Distributors
		 * into the Hash Table
		 */
		 private void crtPTMST()
		        {
					String L_strSQLQRY = "";
		            try
		            {
		                hstPTMST.clear();
						//System.out.println("creating ptmst");
		                L_strSQLQRY = "select PT_PRTTP + PT_PRTCD PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 from co_ptmst where pt_stsfl<> 'X' " ;
							//and pt_prttp||pt_prtcd in (select cmt_modls||cmt_chp02 from co_cdtrn where cmt_cgmtp = 'D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('MR01OCF','MR02OCF','MR03OCF','MR04OCF','MR11OCF'))";
		                ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
		                if(L_rstRSSET == null || !L_rstRSSET.next())
		                {
		                     setMSG("Distributors not found in CO_PTMST",'E');
		                      return;
		                }
		                while(true)
		                {
		                        strPRTCD = getRSTVAL(L_rstRSSET,"PT_PRTCD","C");
								//System.out.println("cp1");
		                        String[] staPTMST = new String[intPTMST_TOT];
								//System.out.println("cp2");
		                        staPTMST[intAE_PT_PRTNM] = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
								//System.out.println("cp3");
		                        staPTMST[intAE_PT_ZONCD] = getRSTVAL(L_rstRSSET,"PT_ZONCD","C");
								//System.out.println("cp4");
		                        staPTMST[intAE_PT_SHRNM] = getRSTVAL(L_rstRSSET,"PT_SHRNM","C");
								//System.out.println("cp5");
		                        staPTMST[intAE_PT_ADR01] = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
								//System.out.println("cp6");
		                        staPTMST[intAE_PT_ADR02] = getRSTVAL(L_rstRSSET,"PT_ADR02","C");
								//System.out.println("cp7");
		                        staPTMST[intAE_PT_ADR03] = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
								//System.out.println("cp8");
		                        hstPTMST.put(strPRTCD,staPTMST);
								//System.out.println("cp9");
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
	
		/** One time data capturing for Agents
		 * into the Hash Table
		 */
		 private void crtPTMST1()
		        {
					String L_strSQLQRY = "";
		            try
		            {
		                hstPTMST1.clear();
		                //L_strSQLQRY = "select PT_PRTTP||PT_PRTCD PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 from co_ptmst where pt_stsfl<> 'X' and pt_prttp||pt_prtcd in (select cmt_modls||cmt_chp02 from co_cdtrn where cmt_cgmtp = 'D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('MR01OCF','MR02OCF','MR03OCF','MR04OCF','MR11OCF'))";
		                L_strSQLQRY = "select PT_PRTTP + PT_PRTCD PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 from co_ptmst where pt_stsfl<> 'X' and pt_prttp='A'";//||pt_prtcd in (select cmt_modls||cmt_chp02 from co_cdtrn where cmt_cgmtp = 'D"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('MR01OCF','MR02OCF','MR03OCF','MR04OCF','MR11OCF'))";
		                ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
		                if(L_rstRSSET == null || !L_rstRSSET.next())
		                {
		                     setMSG("Agents not found in CO_PTMST",'E');
		                      return;
		                }
		                while(true)
		                {
		                        strPRTCD1 = getRSTVAL(L_rstRSSET,"PT_PRTCD","C");
		                        String[] staPTMST1 = new String[intPTMST_TOT1];
		                        staPTMST1[intAE_PT_PRTNM] = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
		                        staPTMST1[intAE_PT_ZONCD] = getRSTVAL(L_rstRSSET,"PT_ZONCD","C");
		                        staPTMST1[intAE_PT_SHRNM] = getRSTVAL(L_rstRSSET,"PT_SHRNM","C");
		                        staPTMST1[intAE_PT_ADR01] = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
		                        staPTMST1[intAE_PT_ADR02] = getRSTVAL(L_rstRSSET,"PT_ADR02","C");
		                        staPTMST1[intAE_PT_ADR03] = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
		                        hstPTMST1.put(strPRTCD,staPTMST1);
		                        if (!L_rstRSSET.next())
		                                break;
		                }
		                L_rstRSSET.close();
		            }
		            catch(Exception L_EX)
		            {
		                   setMSG(L_EX,"crtPTMST1");
		            }
		return;
		}
	
	
		/** One time data capturing for specified codes from CO_CDTRN
		 * into the Hash Table
		 */
// 		crtCDTRN("'SYSCOXXTAX'"," and upper(CMT_CHP02) <> 'X' and  CMT_CGMTP||CMT_CGSTP||CMT_CODCD in (select cd1_cgmtp||cd1_cgstp||cd1_codcd from co_cdtr1 where cd1_cgmtp||cd1_cgstp='SYSCOXXTAX' and cd1_subcd = 'MR"+strMKTTP+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' and cd1_taxtp='01')",hstDFTTX);
//		crtCDTRN("'SYSCOXXTAX'"," and upper(CMT_CHP02) = 'X' and  CMT_CGMTP||CMT_CODCD in (select cd1_cgmtp||cd1_codcd from co_cdtr1 where cd1_cgmtp||cd1_cgstp='SYSCOXXDES' and cd1_subcd = 'MR"+strMKTTP+getCODCD("SYSMR00SAL"+cmbSALTP.getSelectedItem().toString())+"' and cd1_taxtp='02')",hstDFTTX);

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
						hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
//						System.out.println("One added");
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
		//System.out.println(LP_PRTCD);
        try
        {		
				String[] staPTMST = (String[])hstPTMST.get(LP_PRTCD);
                if (LP_FLDNM.equals("PT_PRTNM"))
            		{//System.out.println("CP1");
                        L_RETSTR = staPTMST[intAE_PT_PRTNM];}
                else if (LP_FLDNM.equals("PT_ZONCD"))
        		{//System.out.println("CP2");
                        L_RETSTR = staPTMST[intAE_PT_ZONCD];}
                else if (LP_FLDNM.equals("PT_SHRNM"))
        		{//System.out.println("CP3");
                        L_RETSTR = staPTMST[intAE_PT_SHRNM];}
                else if (LP_FLDNM.equals("PT_ADR01"))
        		{//System.out.println("CP4");
                        L_RETSTR = staPTMST[intAE_PT_ADR01];}
                else if (LP_FLDNM.equals("PT_ADR02"))
        		{//System.out.println("CP5");
                        L_RETSTR = staPTMST[intAE_PT_ADR02];}
                else if (LP_FLDNM.equals("PT_ADR03"))
        		{//System.out.println("CP6");
                        L_RETSTR = staPTMST[intAE_PT_ADR03];}
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPTMSTxxx");
		}
        return L_RETSTR;
        } 		/** Picking up Agent Details
		 * @param LP_PRTCD		Party Code 
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getPTMST1(String LP_PRTCD, String LP_FLDNM)
        {
        String L_RETSTR = "";
        try
        {		
				//System.out.println("11");
				String[] staPTMST1 = (String[])hstPTMST1.get(LP_PRTCD);
				//System.out.println("12");
                if (LP_FLDNM.equals("PT_PRTNM"))
                        L_RETSTR = staPTMST1[intAE_PT_PRTNM];
                else if (LP_FLDNM.equals("PT_ZONCD"))
                        L_RETSTR = staPTMST1[intAE_PT_ZONCD];
                else if (LP_FLDNM.equals("PT_SHRNM"))
				{	
                        L_RETSTR = staPTMST1[intAE_PT_SHRNM];
						//System.out.println("13");
				}		
                else if (LP_FLDNM.equals("PT_ADR01"))
                        L_RETSTR = staPTMST1[intAE_PT_ADR01];
                else if (LP_FLDNM.equals("PT_ADR02"))
                        L_RETSTR = staPTMST1[intAE_PT_ADR02];
                else if (LP_FLDNM.equals("PT_ADR03"))
                        L_RETSTR = staPTMST1[intAE_PT_ADR03];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPTMST1");
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
                if (LP_FLDNM.equals("GRD_OCFQT"))
                        return ((String[])hstGRDTL.get(LP_GRDTL_KEY))[intAE_GRD_OCFQT];
				else if(LP_FLDNM.equals("GRD_REQQT"))
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
        } 	/** Displaying unauthorised Order Confirmations in a separate window
	 */	
	private void exeOCFDSP()
	{
		try
			{
				if(!strMKTTP.equals("01") && !strMKTTP.equals("04") && !strMKTTP.equals("05"))
					return;
				ResultSet L_rstRSSET = null;
				if(pnlOCFDSP==null)
				{
					//pnlINDDSP=new JPanel(null);
					//tblINDDSP = crtTBLPNL1(pnlINDDSP,new String[]{"FL","Ind.No.","Date","Amd.No.","Amd.Date","Grade","Ind.Qty.","Aut.Qty.","Bkd.By","Customer"},100,0,1,6,8,new int[]{20,100,70,30,70,80,60,60,30,100},new int[]{0});
					tblOCFDSP = crtTBLPNL1(pnlOCFDSP=new JPanel(null),new String[]{"FL","Ind.No.","Date","Amd.No.","Amd.Date","Grade","Ind.Qty.","Aut.Qty.","Bkd.By","Customer"},100,0,1,6,5,new int[]{20,100,70,30,70,80,60,60,30,120},new int[]{0});
				}
				pnlOCFDSP.setSize(600,150);
				pnlOCFDSP.setPreferredSize(new Dimension(650,200));
				//L_rstRSSET=cl_dat.exeSQLQRY2("Select IN_MKTTP,IN_INDNO,IN_BKGDT, IN_AMDNO, IN_AMDDT, INT_PRDDS, INT_REQQT,INT_INDQT, IN_BKGBY,PT_PRTNM from VW_INTRN,CO_PTMST where IN_BYRCD = PT_PRTCD and PT_PRTTP='C' and INT_STSFL='0' and INT_REQQT>0 and IN_MKTTP in ('01','04','05') and in_sbscd in "+M_strSBSLS+" order by IN_BKGDT desc,IN_INDNO asc");
				L_rstRSSET=cl_dat.exeSQLQRY2("Select OC_MKTTP,OC_INDNO,OC_BKGDT, OC_AMDNO, OC_AMDDT, OCT_PRDDS, OCT_OCFQT, OC_BKGBY,OC_BYRNM from VW_OCTRN where OCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OCT_STSFL='0' and OCT_MKTTP in ('01','04','05') and OCT_SBSCD1 in "+M_strSBSLS+" order by OC_OCFDT desc,OC_OCFNO asc");
				if(L_rstRSSET==null || !L_rstRSSET.next())
					{setMSG("No unauthorised Order Confirmations",'E');}	
				int i=0;
				while(true)
				{
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OC_MKTTP","C")+" / "+getRSTVAL(L_rstRSSET,"OC_OCFNO","C"),i,intTB4_OCFNO);
					//tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OC_BKGDT","D"),i,intTB4_BKGDT);
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OC_OCFDT","D"),i,intTB4_OCFDT);
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OC_AMDNO","C"),i,intTB4_AMDNO);
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OC_AMDDT","D"),i,intTB4_AMDDT);
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OCT_PRDDS","C"),i,intTB4_PRDDS);
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OCT_REQQT","N"),i,intTB4_REQQT);
					//tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OCT_OCFQT","N"),i,intTB4_OCFQT);
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OC_REGBY","C"),i,intTB4_REGBY);
					//tblINDDSP.setValueAt(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"),i,intTB4_PRTNM);
					tblOCFDSP.setValueAt(getRSTVAL(L_rstRSSET,"OC_BYRNM","C"),i,intTB4_PRTNM);
					i++;
					if(!L_rstRSSET.next())
						break;
				}
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlOCFDSP,"Unauthorised Customer Orders",JOptionPane.OK_CANCEL_OPTION);
				chkOCFDSP.requestFocus();
			}
		catch(Exception e)
		{setMSG(e,"exeOCFDSP");}
	}
		
		

	
	/** Clearing table contents for re-defining common taxes
	 */	
	private void exeREDTAX(String LP_SBSTP,String LP_OCFNO,String LP_CLRMSG)
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
					M_strSQLQRY = "delete from CO_TXDOC where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_SYSCD='MR' and TX_SBSTP = '"+LP_SBSTP+"' and TX_DOCTP='OCF' and tx_DOCNO = '"+LP_OCFNO+"' and TX_PRDCD = 'XXXXXXXXXX'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"");
					M_strSQLQRY = "delete from CO_TXSPC where  TXT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TXT_SYSCD='MR' and TXT_SBSTP = '"+LP_SBSTP+"' and TXT_DOCTP='OCF' and TXT_DOCNO = '"+LP_OCFNO+"' and TXT_PRDCD = 'XXXXXXXXXX'";
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
	        //SELECT SUM(PL_DOCVL - isnull(PL_ADJVL,0)) FROM SPLDATA.MR_PLTRN WHERE  PL_DOCVL - isnull(PL_ADJVL,0) >0 AND PL_DUEDT > CURRENT_DATE AND SUBSTRING(PL_DOCTP,1,1) IN('2','3') and pl_prttp||pl_prtcd in(select b.pt_prttp||b.pt_prtcd from spldata.co_ptmst b where b.pt_dsrcd ='S0006')
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
		
		

	public static void main(String[] a)
	{
		mr_teocn mr=new mr_teocn();
	}

}
