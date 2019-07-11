//Gradewise Realisation Query 
/** Purpose  : 1. Pickup Booking / Sales data for specified scope
 *               Display booking / sales performance against predefined (Cut-off) price.
 *			  2. Define (record) Gradewise Cut-off price for particular date-range
 * Author    : SRD
 * Impl.Date : 01/03/2005
 * Last Modfn: 10/03/2005
 * Modfn Detail : Introducing total for every column
 *                Storing cut-off price at Sales Level
 *                Displaying Sales (Invoice) level realisation detail
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.Date;
import java.sql.SQLException;
import java.io.*;
import java.sql.*;
import java.awt.Color;
import java.sql.ResultSet;
import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;

class mr_qrgrr extends cl_rbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;
	private JPanel pnlCNSRPT,pnlGRDRPT,pnlINDRPT;
	private String strPRDCD,strPRTTP,strPRTCD,strWHRSTR,strORDBY;	//strSTRDT,strENDDT,
	private String strZONSTR, strSALSTR, strSBSSTR;
	private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_qrgrr.doc"; 

	
	/** Labels for Various Options
	 */
	private JLabel lblRGN;
	private JLabel lblZON;
	private JLabel lblGRD;
	private JLabel lblDSR;
	private JLabel lblBYR;
	private JLabel lblSAL;
	private JLabel lblPMT;
	private JLabel lblPRD;
	private JLabel lblCNS;
	private JLabel lblREMDS;
	
	/** Text fields for various options
	 */
	private JTextField txtRGN;
	private JTextField txtZON;
	private JTextField txtGRD;
	private JTextField txtDSR;
	private JTextField txtBYR;
	private JTextField txtSAL;
	private JTextField txtPMT;
	private JTextField txtPRD;
	private JTextField txtCNS;
	private JTextField txtREMDS;
	private JTextField txtRECCT;
	private JCheckBox chkN_EFF;

	
	private JList lstOPTNM;			// JList for displaying details of options selected
	private Vector<String> vtrOPTNM;		// Vector for poulating data into the JList
	private Hashtable hstOPTNM;
	private cl_JTable tblCNSRPT,tblGRDRPT,tblINDRPT;   // JTable for displaying Consignee, Grade & Customer order detail
	private cl_JTable tblCNSTOT,tblGRDTOT,tblINDTOT;   // JTable for displaying Consignee, Grade & Customer order detail
	private boolean flgCHK_EXIST;	// Flag to check whether record exists in table
	private boolean flgIVT_RLSN = false;	// Flag for Invoice Based Realisation
	
	private JRadioButton rdbORDER_A;	
	private JRadioButton rdbORDER_D;	
	
	private JRadioButton rdbBASE_INT;	
	private JRadioButton rdbBASE_IVT;	

	
	private JButton btnPRINT_CNS, btnPRINT_GRD,btnPRINT_IND, btnSAVE_CPR;
	CallableStatement cstCPRVL;	   // statement to execute Stored Procedure "setCPRVL"

	/** String for creating Code Transaction detail hashtable
	 */
	private String strCGMTP;
	private String strCGSTP;
	private String strCODCD;


	private int intSCT_ROW = 3, intSCT_COL = 1;   // Row column position for 
	
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
	

	private int intPRMST_TOT = 3;
    private int intAE_PR_PRDCD = 0;
    private int intAE_PR_PRDDS = 1;
    private int intAE_PR_AVGRT = 2;

	private int intPTMST_TOT = 6;
    private int intAE_PT_PRTNM = 0;
    private int intAE_PT_ZONCD = 1;
    private int intAE_PT_SHRNM = 2;
    private int intAE_PT_ADR01 = 3;
    private int intAE_PT_ADR02 = 4;		
    private int intAE_PT_ADR03 = 5;		

	private int intTB1_CHKFL = 0;
	private int intTB1_KEY01 = 1;
	private int intTB1_KEY02 = 2;
	private int intTB1_INDQT = 3;
	private int intTB1_RSNVL = 4;
	private int intTB1_CPRVL = 5;
	private int intTB1_DFFVL = 6;
	private int intTB1_TOTVL = 7;
	private int intTB1_NEFVL = 8;
	
	private int intTB2_CHKFL = 0;
	private int intTB2_PRDDS = 1;
	private int intTB2_INDQT = 2;
	private int intTB2_RSNVL = 3;
	private int intTB2_CPRVL = 4;
	private int intTB2_DFFVL = 5;
	private int intTB2_TOTVL = 6;
	private int intTB2_PRDCD = 7;

	private int intTB3_CHKFL = 0;
	private int intTB3_PRDDS = 1;
	private int intTB3_INDNO = 2;
	private int intTB3_INDDT = 3;
	private int intTB3_INDQT = 4;
	private int intTB3_RSNVL = 5;
	private int intTB3_CPRVL = 6;
	private int intTB3_DFFVL = 7;
	private int intTB3_TOTVL = 8;
	private int intTB3_INDVL = 9;
	private int intTB3_CCLVL = 10;
	private int intTB3_COMVL = 11;
	private int intTB3_FRTVL = 12;
	private int intTB3_CRDVL = 13;
	private int intTB3_CNSNM = 14;
	
	
	private int intINTRN_TOT = 7;
	private int intAE_IN_BYRCD = 0;
	private int intAE_IN_DSRCD = 1;
	private int intAE_IN_INDNO = 2;
	private int intAE_INT_PRDCD = 3;
	private int intAE_INT_PRDDS = 4;
	private int intAE_INT_INDQT = 5;
	private int intAE_INT_RSNVL = 6;
	private int intAE_INT_CPRVL = 7;

	private int intINTRN_TOT_C = 6;
	private int intAE_INT_INDQT_C = 0;
	private int intAE_INT_RSNVL_C = 1;
	private int intAE_INT_CPRVL_C = 2;
	private int intAE_INT_DFFVL_C = 3;
	private int intAE_INT_TOTVL_C = 4;
	private int intAE_INT_NEFVL_C = 5;
	

	private int intINTRN_TOT_G = 6;
	private int intAE_INT_INDQT_G = 0;
	private int intAE_INT_RSNVL_G = 1;
	private int intAE_INT_CPRVL_G = 2;
	private int intAE_INT_DFFVL_G = 3;
	private int intAE_INT_TOTVL_G = 4;
	private int intAE_INT_PRDCD_G = 5;
	
	
	
	private double dblINDQT1 =0;
	private double dblINDQT = 0;
	private double dblRSNVL = 0;
	private double dblCPRVL = 0;


	private String strKEY01_INT = "";
	private String strKEY01_IVT = "";
	private String strKEY02_INT = "";
	private String strKEY02_IVT = "";
	
	
	private String strWHRINT_RGN = "";
	private String strWHRINT_ZON = "";
	private String strWHRINT_GRD = "";
	private String strWHRINT_DSR = "";
	private String strWHRINT_BYR = "";
	private String strWHRINT_SAL = "";
	private String strWHRINT_PMT = "";
	private String strWHRINT_PRD = "";
	private String strWHRINT_CNS = "";

	private String strWHRIVT_RGN = "";
	private String strWHRIVT_ZON = "";
	private String strWHRIVT_GRD = "";
	private String strWHRIVT_DSR = "";
	private String strWHRIVT_BYR = "";
	private String strWHRIVT_SAL = "";
	private String strWHRIVT_PMT = "";
	private String strWHRIVT_PRD = "";
	private String strWHRIVT_CNS = "";

	
	
	private String strALLWS = "Overall";
	private String strRGNWS = "Region";
	private String strZONWS = "Zone";
	private String strGRDWS = "Grade";
	private String strDSRWS = "Distributor";
	private String strBYRWS = "Buyer";
	private String strSALWS = "Sale Type";
	private String strPMTWS = "Payment Type";	
	private String strPRDWS = "Prod.Category";	
	private String strCNSWS = "Consignee";	
	

	private String strDFTSQ = "Default";		
	private String strGRDSQ = "Grade";		
	private String strDSRSQ = "Distributor";		
	private String strBYRSQ = "Buyer";		
	private String strINOSQ = "Invoice No.";		
	
	
	private String[] arrCNSHDR = new String[]{"0","1","2","3","4","5","6","7","8"};
	private String[] arrCNSHDT = new String[]{"0","1","2","3","4","5","6","7","8"};
	private int[] arrCNSHDR_WD = new int[]{0,1,2,3,4,5,6,7,8};
	private char[] arrCNSHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8'};
	
	private String[] arrGRDHDR = new String[]{"0","1","2","3","4","5","6","7"};
	private String[] arrGRDHDT = new String[]{"0","1","2","3","4","5","6","7"};
	private int[] arrGRDHDR_WD = new int[]{0,1,2,3,4,5,6,7};
	private char[] arrGRDHDR_PAD = new char[]{'0','1','2','3','4','5','6','7'};

	private String[] arrINDHDR = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
	private String[] arrINDHDT = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
	private int[] arrINDHDR_WD = new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
	private char[] arrINDHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8','9','0','1','2','3','4'};

	
	
	private JComboBox cmbPRINTERS;
	
	
	private	int intDSRCTR = 0, intBYRCTR = 0, intBLKCTR = 0; 
	//private double[] arrCOLTOT;
	private double dblDSRQT = 0.000, dblBYRQT = 0.000, dblTOTQT = 0.000;
	private double dblDSRVL = 0.000, dblBYRVL = 0.000;

	private String strQTFLD_INT, strVLFLD_INT;
	//private String  strCNSCD_OLD = "", strBYRCD_OLD="", strPRDCD_OLD ="";

	

	private double dblINDQT_TOT=0, dblRSNVL_TOT=0, dblCPRVL_TOT=0, dblDFFVL_TOT=0, dblNEFVL_TOT;
	private double dblINDVL_TOT=0, dblCCLVL_TOT=0, dblCOMVL_TOT=0, dblFRTVL_TOT=0, dblCRDVL_TOT=0;
	
	private int intCNSWD = 130;      // report width
	private int intCNSCL = 8;		 // No.of columns 
	private int intGRDWD = 130;      // report width
	private int intGRDCL = 8;		 // No.of columns
	private int intINDWD = 145;      // report width
	private int intINDCL = 11;		 // No.of columns in the report line

	private int intLINECT=72, intPAGENO=0, intRUNCL=0;
	boolean flgCNSCD_NEW = true, flgBYRCD_NEW = true, flgPRDCD_NEW = true, flgEOFCHK = false;

	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	
	private JLabel lblRPTWS;		
	private JLabel lblKEY01;		
	private JLabel lblKEY02;		
	private Vector<String> vtrRPTWS;		/**Vector for adding elements to cmbRPTWS */
	private Vector<String> vtrKEY01;		/**Vector for adding elements to cmbKEY01 */
	private Vector<String> vtrKEY02;		/**Vector for adding elements to cmbKEY02 */
	private JComboBox cmbRPTWS;		/**Combo-box for defining scope of the report */

	private JComboBox cmbKEY01;		/**Combo-box for defining First Key column */
	private JComboBox cmbKEY02;		/**Combo-box for defining First Key column */

	private JLabel lblMKTTP;		
	private Vector<String> vtrMKTTP;		/**Vector for adding elements to cmbMKTTP */
	private JComboBox cmbMKTTP;
	
	private Hashtable<String,String[]> hstCDTRN;			// Code Transaction details
	//private Vector vtrINTRN_C1;			// Distributor list for 
	private Hashtable<String,String> hstINTRN_C1;			// Distributor list with Tot.Qty for 
	private Hashtable<String,String[]> hstINTRN_C2;			// Distributorwise details for 

	//private Vector vtrINTRN_G1;			// Grade list for 
	private Hashtable<String,String> hstINTRN_G1;			// Grade list with Tot.Qty for 
	private Hashtable<String,String[]> hstINTRN_G2;			// Grade details for 

	
	private Hashtable<String,String> hstCODDS;			// Code Description
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstPTMST;			// Party Details
	private Hashtable hstLOTNO;			// Party Details
	private Hashtable<String,String> hstDSRNM;			// Distibutor Short Name (as key) & Code (as value)
	private Vector<String> vtrDSRNM;			// Distributor Code & Short Name
	private Object[] arrHSTKEY;			// Object array for getting hash table key in sorted order
	
	mr_qrgrr()
	{
		super(2);
		try
		{
			System.out.println("modified");
			cstCPRVL = cl_dat.M_conSPDBA_pbst.prepareCall("call setCPRVL(?,?,?,?)");
			arrCNSHDR[intTB1_CHKFL] = "xx";
			arrCNSHDR[intTB1_KEY01] = "Key01";
			arrCNSHDR[intTB1_KEY02] = "Key02";
			arrCNSHDR[intTB1_INDQT] = "Qty";
			arrCNSHDR[intTB1_RSNVL] = "Avg.Cash Prc.(Ex-Factory)";
			arrCNSHDR[intTB1_CPRVL] = "Ref.Prc.";
			arrCNSHDR[intTB1_DFFVL] = "Var.";
			arrCNSHDR[intTB1_TOTVL] = "Total (+/-)";
			arrCNSHDR[intTB1_NEFVL] = "-ve Effect";

			arrCNSHDR_WD[intTB1_CHKFL] = 2;
			arrCNSHDR_WD[intTB1_KEY01] = (80*2)+(8*1);
			arrCNSHDR_WD[intTB1_KEY02] = 80;
			arrCNSHDR_WD[intTB1_INDQT] = 80;
			arrCNSHDR_WD[intTB1_RSNVL] = 80;
			arrCNSHDR_WD[intTB1_CPRVL] = 80;
			arrCNSHDR_WD[intTB1_DFFVL] = 80;
			arrCNSHDR_WD[intTB1_TOTVL] = 80;
			arrCNSHDR_WD[intTB1_NEFVL] = 80;
			
			arrCNSHDR_PAD[intTB1_CHKFL] = 'R';
			arrCNSHDR_PAD[intTB1_KEY01] = 'R';
			arrCNSHDR_PAD[intTB1_KEY02] = 'R';
			arrCNSHDR_PAD[intTB1_INDQT] = 'L';
			arrCNSHDR_PAD[intTB1_RSNVL] = 'L';
			arrCNSHDR_PAD[intTB1_CPRVL] = 'L';
			arrCNSHDR_PAD[intTB1_DFFVL] = 'L';
			arrCNSHDR_PAD[intTB1_TOTVL] = 'L';
			arrCNSHDR_PAD[intTB1_NEFVL] = 'L';

			
			arrGRDHDR[intTB2_CHKFL] = "xx";
			arrGRDHDR[intTB2_PRDDS] = "Grade";
			arrGRDHDR[intTB2_INDQT] = "Qty";
			arrGRDHDR[intTB2_RSNVL] = "Avg.cash Prc.(Ex-factory)";
			arrGRDHDR[intTB2_CPRVL] = "Ref. Prc.";
			arrGRDHDR[intTB2_DFFVL] = "Var.";
			arrGRDHDR[intTB2_TOTVL] = "Total (+/-)";
			arrGRDHDR[intTB2_PRDCD] = "Prd.Code";

			arrGRDHDR_WD[intTB2_CHKFL] = 2;
			arrGRDHDR_WD[intTB2_PRDDS] = (100*2)+(10*1);
			arrGRDHDR_WD[intTB2_INDQT] = 100;
			arrGRDHDR_WD[intTB2_RSNVL] = 100;
			arrGRDHDR_WD[intTB2_CPRVL] = 100;
			arrGRDHDR_WD[intTB2_DFFVL] = 100;
			arrGRDHDR_WD[intTB2_TOTVL] = 100;
			arrGRDHDR_WD[intTB2_PRDCD] = 2;
			
			arrGRDHDR_PAD[intTB2_CHKFL] = 'R';
			arrGRDHDR_PAD[intTB2_PRDDS] = 'R';
			arrGRDHDR_PAD[intTB2_INDQT] = 'L';
			arrGRDHDR_PAD[intTB2_RSNVL] = 'L';
			arrGRDHDR_PAD[intTB2_CPRVL] = 'L';
			arrGRDHDR_PAD[intTB2_DFFVL] = 'L';
			arrGRDHDR_PAD[intTB2_TOTVL] = 'L';
			arrGRDHDR_PAD[intTB2_PRDCD] = 'R';
			
			arrINDHDR[intTB3_CHKFL] = "xx";
			arrINDHDR[intTB3_PRDDS] = "Grade";
			arrINDHDR[intTB3_INDNO] = "Doc.No.";
			arrINDHDR[intTB3_INDDT] = "Date";
			arrINDHDR[intTB3_INDQT] = "Qty";
			arrINDHDR[intTB3_RSNVL] = "Avg.Cash Prc.(Ex-factory)";
			arrINDHDR[intTB3_CPRVL] = "Ref. Prc.";
			arrINDHDR[intTB3_DFFVL] = "Var.";
			arrINDHDR[intTB3_TOTVL] = "Total (+/-)";
			arrINDHDR[intTB3_INDVL] = "Bas. Prc.";
			arrINDHDR[intTB3_CCLVL] = "Bkg.Dsc.";
			arrINDHDR[intTB3_COMVL] = "Comm.";
			arrINDHDR[intTB3_FRTVL] = "Frt.";
			arrINDHDR[intTB3_CRDVL] = "Crd.Prc.";
			arrINDHDR[intTB3_CNSNM] = "Customer";

			arrINDHDR_WD[intTB3_CHKFL] = 2;
			arrINDHDR_WD[intTB3_PRDDS] = (80*2)+(8*1);
			arrINDHDR_WD[intTB3_INDNO] = 90;
			arrINDHDR_WD[intTB3_INDDT] = 90;
			arrINDHDR_WD[intTB3_INDQT] = 90;
			arrINDHDR_WD[intTB3_RSNVL] = 90;
			arrINDHDR_WD[intTB3_CPRVL] = 90;
			arrINDHDR_WD[intTB3_DFFVL] = 90;
			arrINDHDR_WD[intTB3_TOTVL] = 90;
			arrINDHDR_WD[intTB3_INDVL] = 90;
			arrINDHDR_WD[intTB3_CCLVL] = 90;
			arrINDHDR_WD[intTB3_COMVL] = 90;
			arrINDHDR_WD[intTB3_FRTVL] = 90;
			arrINDHDR_WD[intTB3_CRDVL] = 90;
			arrINDHDR_WD[intTB3_CNSNM] = 90;
			
			arrINDHDR_PAD[intTB3_CHKFL] = 'R';
			arrINDHDR_PAD[intTB3_PRDDS] = 'R';
			arrINDHDR_PAD[intTB3_INDNO] = 'R';
			arrINDHDR_PAD[intTB3_INDDT] = 'R';
			arrINDHDR_PAD[intTB3_INDQT] = 'L';
			arrINDHDR_PAD[intTB3_RSNVL] = 'L';
			arrINDHDR_PAD[intTB3_CPRVL] = 'L';
			arrINDHDR_PAD[intTB3_DFFVL] = 'L';
			arrINDHDR_PAD[intTB3_TOTVL] = 'L';
			arrINDHDR_PAD[intTB3_INDVL] = 'L';
			arrINDHDR_PAD[intTB3_CCLVL] = 'L';
			arrINDHDR_PAD[intTB3_COMVL] = 'L';
			arrINDHDR_PAD[intTB3_FRTVL] = 'L';
			arrINDHDR_PAD[intTB3_CRDVL] = 'L';
			arrINDHDR_PAD[intTB3_CNSNM] = 'L';

			
			vtrOPTNM = new Vector<String>();
			hstOPTNM = new Hashtable();
			lstOPTNM = new JList();
			
			
			tbpMAIN = new JTabbedPane();
			pnlCNSRPT = new JPanel(null);
			pnlGRDRPT = new JPanel(null);
			pnlINDRPT = new JPanel(null);
			//txtSPCCT = new JTextField();

			txtRGN = new JTextField();
			txtZON = new JTextField();
			txtGRD = new JTextField();
			txtDSR = new JTextField();
			txtBYR = new JTextField();
			txtSAL = new JTextField();
			txtPMT = new JTextField();
			txtPRD = new JTextField();
			txtCNS = new JTextField();
			txtREMDS  = new JTextField();
			txtRECCT  = new JTextField();

			lblRGN = new JLabel("Region");
			lblZON = new JLabel("Zone");
			lblGRD = new JLabel("Grade");
			lblDSR = new JLabel("Distr.");
			lblBYR = new JLabel("Buyer");
			lblSAL = new JLabel("Sal.Type");
			lblPMT = new JLabel("Pmt.Type");
			lblPRD = new JLabel("Prd.Cat.");
			lblCNS = new JLabel("Cons.");
			lblREMDS = new JLabel("Remark");
			
			
			hstCDTRN = new Hashtable<String,String[]>();
			hstINTRN_C1 = new Hashtable<String,String>();
			hstINTRN_C2 = new Hashtable<String,String[]>();
			hstINTRN_G1 = new Hashtable<String,String>();
			hstINTRN_G2 = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstPRMST = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			hstLOTNO = new Hashtable();
			hstDSRNM = new Hashtable<String,String>();
			
			vtrRPTWS = new Vector<String>();
			vtrKEY01 = new Vector<String>();
			vtrKEY02 = new Vector<String>();
			vtrMKTTP = new Vector<String>();
			
			lblRPTWS  = new JLabel("Scope");
			lblKEY01  = new JLabel("Key Col 1:");
			lblKEY02  = new JLabel("Key Col 2:");
			lblMKTTP  = new JLabel("Market Type");

			lblRPTWS.setForeground(Color.blue);
			lblKEY01.setForeground(Color.blue);
			lblKEY02.setForeground(Color.blue);
			lblMKTTP.setForeground(Color.blue);


			btnPRINT_CNS = new JButton("Selective.)");
			btnPRINT_GRD = new JButton("Grade");
			btnPRINT_IND = new JButton("Document");
			btnSAVE_CPR = new JButton("Save");
			

			crtPRMST();
			hstCDTRN.clear();
			crtCDTRN("'SYSMRXXRGN','SYSMRXXPMT','MSTCOXXPGR', 'SYSMR00SAL', 'SYSMR00ZON'","",hstCDTRN);
			
			setMatrix(20,8);
			
			add(lblRPTWS,1,2,1,2,this,'L');
			add(cmbRPTWS=new JComboBox(),2,2,1,2,this,'L');
			
			
			add(lblKEY01,4,1,1,1,this,'L');
			add(lblKEY02,4,2,1,1,this,'L');
			add(cmbKEY01=new JComboBox(),5,1,1,1,this,'L');
			add(cmbKEY02=new JComboBox(),5,2,1,1,this,'L');
			
			add(lblMKTTP,1,1,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),2,1,1,1,this,'L');


			//add(rdbALLWS,2,1,1,1,this,'L');
			//add(lblSPCCT,3,1,1,1,this,'L');
			//add(txtSPCCT,3,2,1,1,this,'L');
			//add(lblSPCDS,5,2,1,3,this,'L');

			add(lblRGN,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtRGN,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblZON,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtZON,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblGRD,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtGRD,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblDSR,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtDSR,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblBYR,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtBYR,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblSAL,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtSAL,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPMT,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPMT,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPRD,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPRD,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblCNS,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtCNS,intSCT_ROW,intSCT_COL+1,1,1,this,'L');

			add(new JScrollPane(lstOPTNM),intSCT_ROW,intSCT_COL+2,3,2,this,'L');

			//add(lblREMDS,intSCT_ROW+1,intSCT_COL+4,1,1,this,'L');
			//add(txtREMDS,intSCT_ROW+2,intSCT_COL+4,1,4,this,'L');
			add(new JLabel("Top"),intSCT_ROW+1,intSCT_COL+4,1,0.5,this,'L');
			add(txtRECCT,intSCT_ROW+2,intSCT_COL+4,1,0.5,this,'L');
			
			add(rdbORDER_A=new JRadioButton("Ascending"),intSCT_ROW,intSCT_COL+5,1,1,this,'L');
			add(rdbORDER_D=new JRadioButton("Descending"),intSCT_ROW+1,intSCT_COL+5,1,1,this,'L');
			ButtonGroup btg_ORDER=new ButtonGroup();btg_ORDER.add(rdbORDER_A);btg_ORDER.add(rdbORDER_D);

			add(rdbBASE_INT=new JRadioButton("Order Base"),intSCT_ROW-1,intSCT_COL+7,1,2,this,'R');
			add(rdbBASE_IVT=new JRadioButton("Invoice Base"),intSCT_ROW,intSCT_COL+7,1,2,this,'R');
			ButtonGroup btg_BASE=new ButtonGroup();btg_BASE.add(rdbBASE_INT);btg_BASE.add(rdbBASE_IVT);
			
			add(btnSAVE_CPR,intSCT_ROW+4,intSCT_COL+4,1,0.75,this,'L');
			chkN_EFF = new JCheckBox("-ve Effect");
			add(chkN_EFF,18,7,1,2,this,'L');
			
			//add(chkSTOTFL,2,7,1,2,this,'L');
			add(new JLabel("Print"),6,6,1,0.5,this,'L');
			add(btnPRINT_CNS,7,6,1,1,this,'L');
			add(btnPRINT_GRD,7,7,1,1,this,'L');
			add(btnPRINT_IND,7,8,1,1,this,'L');
			
			//setKEY01();
			tblCNSRPT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,500,0,1,7,7.9,arrCNSHDR_WD,new int[]{0});  
			tblCNSTOT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,1,7,1,2.1,7.9,arrCNSHDR_WD,new int[]{0});  

			tblGRDRPT = crtTBLPNL1(pnlGRDRPT,arrGRDHDR,500,0,1,7,7.9,arrGRDHDR_WD,new int[]{0});  
			tblGRDTOT = crtTBLPNL1(pnlGRDRPT,arrGRDHDR,1,7,1,2.1,7.9,arrGRDHDR_WD,new int[]{0});  

			tblINDRPT = crtTBLPNL1(pnlINDRPT,arrINDHDR,500,0,1,7,7.9,arrINDHDR_WD,new int[]{0});  
			tblINDTOT = crtTBLPNL1(pnlINDRPT,arrINDHDR,1,7,1,2.1,7.9,arrINDHDR_WD,new int[]{0});  
			
			tbpMAIN.addTab("Selective Detail",pnlCNSRPT);
			tbpMAIN.addTab("Grade Detail",pnlGRDRPT);
			tbpMAIN.addTab("Document Detail",pnlINDRPT);
			
			add(tbpMAIN,7,1,11,8,this,'L');
			tblCNSRPT.addMouseListener(this);
			tblGRDRPT.addMouseListener(this);
			tblINDRPT.addMouseListener(this);

			setVTRRPTWS();
			setVTRKEY01();
			setVTRKEY02();
			setVTRMKTTP();
			
			lstOPTNM.setBackground(Color.CYAN);
			remove(M_cmbDESTN);
			M_cmbDESTN.setVisible(true);
			M_vtrSCCOMP.remove(M_cmbDESTN);
			exeOPT_DSB();
			updateUI();
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"mr_qrgrr");}
	}


	/** Disabling & Initializing the components
	 */	
	private void exeOPT_DSB()
	{
		try
		{
			exeOPT_DSB1();
			txtRGN.setText("");
			txtZON.setText("");
			txtGRD.setText("");
			txtDSR.setText("");
			txtBYR.setText("");
			txtSAL.setText("");
			txtPMT.setText("");
			txtPRD.setText("");
			txtCNS.setText("");
			 hstOPTNM.clear();
			 vtrOPTNM.clear();
			 lstOPTNM.setListData(vtrOPTNM);
		}
	
		catch (Exception L_EX)
			{setMSG(L_EX,"exeOPT_DSB");}
	}


	/**  Making option (categories) Labels & Text Fields invisible
	 */
	private void exeOPT_DSB1()
	{
		try
		{
			lblRGN.setVisible(false);
			lblZON.setVisible(false);
			lblGRD.setVisible(false);
			lblDSR.setVisible(false);
			lblBYR.setVisible(false);
			lblSAL.setVisible(false);
			lblPMT.setVisible(false);
			lblPRD.setVisible(false);
			lblCNS.setVisible(false);

			 txtRGN.setVisible(false);
			 txtZON.setVisible(false);
			 txtGRD.setVisible(false);
			 txtDSR.setVisible(false);
			 txtBYR.setVisible(false);
			 txtSAL.setVisible(false);
			 txtPMT.setVisible(false);
			 txtPRD.setVisible(false);
			 txtCNS.setVisible(false);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"exeOPT_DSB1");}
	}
	
	
	

	/** Adding elements to vtrRPTWS, for defining Scope of the report
	 */
	void setVTRRPTWS()
	{
		try
		{
			vtrRPTWS.clear();
			vtrRPTWS.addElement(strALLWS);		
			vtrRPTWS.addElement(strRGNWS);		
			vtrRPTWS.addElement(strZONWS);		
			vtrRPTWS.addElement(strGRDWS);		
			vtrRPTWS.addElement(strDSRWS);		
			vtrRPTWS.addElement(strBYRWS);		
			vtrRPTWS.addElement(strSALWS);		
			vtrRPTWS.addElement(strPMTWS);		
			vtrRPTWS.addElement(strPRDWS);		
			vtrRPTWS.addElement(strCNSWS);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRRPTWS");}
	}


	
	/** Adding elements to vtrKEY01, for defining First Key Column
	 */
	void setVTRKEY01()
	{
		try
		{
			vtrKEY01.clear();
			vtrKEY01.addElement(strCNSWS);		
			vtrKEY01.addElement(strBYRWS);		
			vtrKEY01.addElement(strDSRWS);		
			vtrKEY01.addElement(strZONWS);		
			vtrKEY01.addElement(strSALWS);		
			vtrKEY01.addElement(strGRDWS);		
			vtrKEY01.addElement(strPRDWS);		
			vtrKEY01.addElement(strPMTWS);		
			vtrKEY01.addElement(strRGNWS);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRKEY01");}
	}

	
	/** Adding elements to vtrKEY02, for defining First Key Column
	 */
	void setVTRKEY02()
	{
		try
		{
			vtrKEY02.clear();
			vtrKEY02.addElement(strCNSWS);		
			vtrKEY02.addElement(strBYRWS);		
			vtrKEY02.addElement(strDSRWS);		
			vtrKEY02.addElement(strZONWS);		
			vtrKEY02.addElement(strSALWS);		
			vtrKEY02.addElement(strGRDWS);		
			vtrKEY02.addElement(strPRDWS);		
			vtrKEY02.addElement(strPMTWS);		
			vtrKEY02.addElement(strRGNWS);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRKEY02");}
	}
	
	
	
	/** Adding elements to vtrMKTTP, for defining Scope of the report
	 */
	void setVTRMKTTP()
	{
		try
		{
			vtrMKTTP.clear();
			vtrMKTTP.addElement("01 Polystyrene");		
			vtrMKTTP.addElement("02 Styrene");		
			vtrMKTTP.addElement("03 Captive Consumption");		
			vtrMKTTP.addElement("04 Wood Profile");		
			vtrMKTTP.addElement("05 Master Batch");		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRMKTTP");}
	}
	
	
	
	
	
	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		try
		{
			super.clrCOMP();
			//chkSTOTFL.setSelected(true);
			M_txtFMDAT.setText("01"+cl_dat.M_txtCLKDT_pbst.getText().substring(2,10));
			M_txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText());
			txtRECCT.setText("0");
			rdbORDER_A.setSelected(true);
			rdbORDER_D.setSelected(false);
			rdbBASE_INT.setSelected(true);
			rdbBASE_IVT.setSelected(false);
			chkN_EFF.setSelected(false);

			hstOPTNM.clear();
			vtrOPTNM.clear();
			lstOPTNM.setListData(vtrOPTNM);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"clrCOMP");}
	}


	/** Setting values after selecting Key Field column
	 */
	private void setKEY01()
	{
		
		if(cmbKEY01.getSelectedItem().toString().equals(strCNSWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Customer";
			strKEY01_INT = "in_cnscd";
			strKEY01_IVT = "ivt_cnscd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strBYRWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Buyer";
			strKEY01_INT = "in_byrcd";
			strKEY01_IVT = "ivt_byrcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strDSRWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Distributor";
			strKEY01_INT = "in_dsrcd";
			strKEY01_IVT = "ivt_dsrcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strZONWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Zone";
			strKEY01_INT = "in_zoncd";
			strKEY01_IVT = "ivt_zoncd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strSALWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Sale Type";
			strKEY01_INT = "in_saltp";
			strKEY01_IVT = "ivt_saltp";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strGRDWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Grade";
			strKEY01_INT = "int_prdcd";
			strKEY01_IVT = "ivt_prdcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPRDWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Prod.Category";
			strKEY01_INT = "SUBSTRING(int_prdcd,1,4)";
			strKEY01_IVT = "SUBSTRING(ivt_prdcd,1,4)";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPMTWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Paymet Type";
			strKEY01_INT = "in_pmtcd";
			strKEY01_IVT = "ivt_pmtcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strRGNWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Region";
			strKEY01_INT = "cmt_chp02";
			strKEY01_IVT = "cmt_chp02";
		}
	}
	
	
	/** Setting values after selecting Key Field column
	 */
	private void setKEY02()
	{
		
		if(cmbKEY02.getSelectedItem().toString().equals(strCNSWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Customer";
			strKEY02_INT = "in_cnscd";
			strKEY02_IVT = "ivt_cnscd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strBYRWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Buyer";
			strKEY02_INT = "in_byrcd";
			strKEY02_IVT = "ivt_byrcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strDSRWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Distributor";
			strKEY02_INT = "in_dsrcd";
			strKEY02_IVT = "ivt_dsrcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strZONWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Zone";
			strKEY02_INT = "in_zoncd";
			strKEY02_IVT = "ivt_zoncd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strSALWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Sale Type";
			strKEY02_INT = "in_saltp";
			strKEY02_IVT = "ivt_saltp";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strGRDWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Grade";
			strKEY02_INT = "int_prdcd";
			strKEY02_IVT = "ivt_prdcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPRDWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Prod.Category";
			strKEY02_INT = "SUBSTRING(int_prdcd,1,4)";
			strKEY02_IVT = "SUBSTRING(ivt_prdcd,1,4)";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPMTWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Paymet Type";
			strKEY02_INT = "in_pmtcd";
			strKEY02_IVT = "ivt_pmtcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strRGNWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Region";
			strKEY02_INT = "cmt_chp02";
			strKEY02_IVT = "cmt_chp02";
		}
	}
	
	
	/**
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst && cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{	
				//chkSTOTFL.setSelected(true);
				setCMBVL(cmbKEY01,vtrKEY01);
				setCMBVL(cmbKEY02,vtrKEY02);
				setCMBVL(cmbRPTWS,vtrRPTWS);
				setCMBVL(cmbMKTTP,vtrMKTTP);
				//System.out.println("M_strSBSLS : "+M_strSBSLS);
				crtPTMST();
			}

			if(M_txtFMDAT.getText().length()<10)
				M_txtFMDAT.setText("01/04/2004");
			if(M_txtTODAT.getText().length()<10)
				M_txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
			strSBSSTR = setSBSSTR("SBS");
			if(M_objSOURC == cmbRPTWS)
				getSUBHDR();
			if(M_objSOURC == cmbKEY01)
				setKEY01();
			if(M_objSOURC == cmbKEY02)
				setKEY02();
			if(M_objSOURC == M_txtFMDAT)
				M_txtTODAT.requestFocus();
			if(M_objSOURC == btnPRINT_CNS)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_csgrr.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtCNSRPT();
					exePRINT1();
				}
			if(M_objSOURC == btnPRINT_GRD)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_grgrr.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtGRDRPT();
					exePRINT1();
				}
			if(M_objSOURC == btnPRINT_IND)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_ingrr.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtINDRPT();
					exePRINT1();
				}
			if(M_objSOURC == btnSAVE_CPR)
					exeSAVE_CPR();
			if(M_objSOURC == rdbBASE_INT || M_objSOURC == rdbBASE_IVT)
			{
				flgIVT_RLSN = true;
				if(rdbBASE_INT.isSelected() == true)
					flgIVT_RLSN = false;
				crtPTMST();
				//exeOPT_DSB();inlSTRWHR_XXX();
			}
			//if(M_objSOURC == chkN_EFF)
			//{
			//	setN_EFF(tblCNSRPT, tblCNSTOT, intTB1_TOTVL);
			//	setN_EFF(tblGRDRPT, tblGRDTOT, intTB2_TOTVL);
			//	setN_EFF(tblINDRPT, tblINDTOT, intTB3_TOTVL);
			//}
		}
		catch (Exception L_EX) {setMSG(L_EX,"acionPerformed");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
		
	}
	
	/** Setting negative effect on/off for Total Value column
	 */
   private void setN_EFF(cl_JTable LP_TBLNM,cl_JTable LP_TBLNM_T, int LP_COLNO)
   {
		try
		{
			int i =0;
			double L_dblTOTVL_TOT = 0.00,  L_dblTOTVL = 0.00;
			for(i=0;i<LP_TBLNM.getRowCount();i++)
			{
				if(LP_TBLNM.getValueAt(i,LP_COLNO).toString().length()==0)
					break;
				L_dblTOTVL = Double.parseDouble(LP_TBLNM.getValueAt(i,LP_COLNO).toString());
				if(chkN_EFF.isSelected()==true && L_dblTOTVL<0)
					L_dblTOTVL_TOT += L_dblTOTVL;
				else if(chkN_EFF.isSelected()==false)
					L_dblTOTVL_TOT += L_dblTOTVL;
			}
			LP_TBLNM_T.setValueAt(setNumberFormat(L_dblTOTVL_TOT,0),0,LP_COLNO);

		}
		catch (Exception L_EX) {setMSG(L_EX,"setN_EFF");}
   }
	
	

   
   
	/**  Saving Cut-off price entered in Grade Table to MR_CPMST
	 */
	private void exeSAVE_CPR()
	{
		try
		{
			for(int i=0; i<tblGRDRPT.getRowCount();i++)
			{
				if(tblGRDRPT.getValueAt(i,intTB2_PRDCD).toString().length() != 10)
					break;
				//if(Double.parseDouble(tblGRDRPT.getValueAt(i,intTB2_CPRVL).toString())== 0)
				//	continue;
				if(tblGRDRPT.getValueAt(i,intTB2_CHKFL).toString().equalsIgnoreCase("true"))
					exeSAVE_CPR1(tblGRDRPT.getValueAt(i,intTB2_PRDCD).toString(),tblGRDRPT.getValueAt(i,intTB2_CPRVL).toString());
			}
			JOptionPane.showMessageDialog( this,"Saved Successfully","Message",JOptionPane.INFORMATION_MESSAGE);
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeSAVE_CPR");}
	}

	
	/** Validating the From & To date for duplication in MR_CPMST
	 */
	private boolean chkCPMST_EXIST()
	{
		try
		{
			
			String L_strSTRDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()));
			String L_strENDDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()));
			String L_strWHRSTR =  "CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CP_MKTTP = '" +cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' and "
						 +" ((CP_STRDT between  '" +L_strSTRDT+"' and '"+L_strENDDT+"') or (CP_ENDDT between  '" +L_strSTRDT+"' and '"+L_strENDDT+"'))";
		
			M_strSQLQRY = "select distinct CP_STRDT, CP_ENDDT from MR_CPMST where "+L_strWHRSTR;
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
				return false;
			String L_strSTRDT1 = getRSTVAL(L_rstRSSET,"CP_STRDT","D");
			String L_strENDDT1 = getRSTVAL(L_rstRSSET,"CP_ENDDT","D");
			if(!L_strSTRDT1.equals(M_txtFMDAT.getText()) || !L_strENDDT1.equals(M_txtTODAT.getText()))
			{
				int L_intOPTN=JOptionPane.showConfirmDialog( this,"Date Range "+L_strSTRDT1+ " - "+L_strENDDT1+" already exists \n Do you want to redefine it to "+L_strSTRDT1+" - "+M_txtTODAT.getText(),"Warning",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN==0)
				{
					strWHRSTR =  "CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CP_MKTTP = '" +cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' and "
								 +"CP_STRDT = '" +M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strSTRDT1))+"' and "
								 +"CP_ENDDT = '" +M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strENDDT1))+"' ";
					M_strSQLQRY="update MR_CPMST set "
					+"CP_STRDT = '" +M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strSTRDT1))+"',"
					+"CP_ENDDT = '" +M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"'"
					+" where "+strWHRSTR;
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY ,"");
					if(cl_dat.exeDBCMT("chkCPMST_EXIST"))
						return false;
				}
				else
					return true;
			}
		}
		catch (Exception L_EX) {setMSG(L_EX,"chkCPMST_EXIST");}
		return false;
	}

	
	
	/** Sub procedure for saving data in MR_CPMST
	 */
	private void exeSAVE_CPR1(String LP_PRDCD, String LP_CPRVL)
	{
		try
		{
			//System.out.println("exeSAVE_CPR1 : "+LP_PRDCD+"/"+LP_CPRVL);
			cl_dat.M_flgLCUPD_pbst = true;
			strWHRSTR =  "CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CP_MKTTP = '" +cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' and "
			             +"CP_PRDCD = '" +LP_PRDCD+"' and "
						 +"CP_STRDT = '" +M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and "
						 +"CP_ENDDT = '" +M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"' ";
			flgCHK_EXIST =  chkEXIST("MR_CPMST", strWHRSTR);
			if(!flgCHK_EXIST)
			{
				if(Double.parseDouble(LP_CPRVL)==0)
					return;
				M_strSQLQRY="insert into MR_CPMST (CP_CMPCD,CP_PRDCD, CP_STRDT, CP_ENDDT, CP_CPRVL, CP_STSFL, CP_TRNFL, CP_LUPDT, CP_LUSBY) values ("
				+setINSSTR("CP_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
				+setINSSTR("CP_PRDCD",LP_PRDCD,"C")
				+setINSSTR("CP_STRDT",M_txtFMDAT.getText(),"D")
				+setINSSTR("CP_ENDDT",M_txtTODAT.getText(),"D")
				+setINSSTR("CP_CPRVL",LP_CPRVL,"N")
				+setINSSTR("CP_TRNFL","0","C")
				+setINSSTR("CP_STSFL","0","C")
				+setINSSTR("CP_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"'"+cl_dat.M_strUSRCD_pbst+"')";
			}
			else if(flgCHK_EXIST)
			{
				M_strSQLQRY="update MR_CPMST set "
				+setUPDSTR("CP_CPRVL",LP_CPRVL,"N")
				+setUPDSTR("CP_TRNFL","0","C")
				+setUPDSTR("CP_STSFL","0","C")
				+setUPDSTR("CP_LUPDT",cl_dat.M_strLOGDT_pbst,"D")
				+"CP_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' "
				+" where "+strWHRSTR;
			}
			System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			if(cl_dat.exeDBCMT("exeSAVE_CPR1"))
				setMSG("Saving ...",'N');
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeSAVE_CPR1");}
	}
	
	
	
	

	/**
	 */
	public void focusLost(FocusEvent L_FE)
	{
		try
		{
			super.focusLost(L_FE);
			{
				cmbRPTWS.removeActionListener(this);
				if(L_FE.getSource() == txtRGN && txtRGN.getText().length()>0)
					{setOPTNM("RGN");getSUBHDR1();}
				else if(L_FE.getSource() == txtZON && txtZON.getText().length()>0)
					{setOPTNM("ZON");getSUBHDR1();}
				else if(L_FE.getSource() == txtGRD && txtGRD.getText().length()>0)
					{setOPTNM("GRD");getSUBHDR1();}
				else if(L_FE.getSource() == txtDSR && txtDSR.getText().length()>0)
					{setOPTNM("DSR"); getSUBHDR1();}
				else if(L_FE.getSource() == txtBYR && txtBYR.getText().length()>0)
					{setOPTNM("BYR"); getSUBHDR1();}
				else if(L_FE.getSource() == txtSAL && txtSAL.getText().length()>0)
					{setOPTNM("SAL");  getSUBHDR1();}
				else if(L_FE.getSource() == txtPMT && txtPMT.getText().length()>0)
					{setOPTNM("PMT");  getSUBHDR1();}
				else if(L_FE.getSource() == txtPRD && txtPRD.getText().length()>0)
					{setOPTNM("PRD");  getSUBHDR1();}
				else if(L_FE.getSource() == txtCNS && txtCNS.getText().length()>0)
					{setOPTNM("CNS");  getSUBHDR1();}
				else if(L_FE.getSource() == M_txtFMDAT && M_txtFMDAT.getText().length()>0)
				{
					M_txtTODAT.requestFocus();
				}
				else if(L_FE.getSource() == M_txtTODAT && M_txtTODAT.getText().length()>0)
				{
					strWHRSTR = setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText());
					crtPTMST();
				}
				cmbRPTWS.addActionListener(this);
			}
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"focusLost");}
	}
	
	
	
	
//	/** Accepts option category (LP_OPTCT) as a parameter
//	 *  Recording selected options in Hash Table
//	 *  and calling crtOPTNM method to rework the JList from Hash Table
//	 */
//	public void setOPTNM(String LP_OPTCT)
//	{
//		try
//		{
//			{
//				if(LP_OPTCT.equals("RGN"))
//					{hstOPTNM.put(lblRGN.getText(),getCDTRN("SYSMRXXRGN"+txtRGN.getText(),"CMT_CODDS",hstCDTRN)); crtOPTNM();}
//				else if(LP_OPTCT.equals("ZON"))
//					{hstOPTNM.put(lblZON.getText(),getCDTRN("SYSMR00ZON"+txtZON.getText(),"CMT_CODDS",hstCDTRN));  crtOPTNM();}
//				else if(LP_OPTCT.equals("GRD"))
//					{hstOPTNM.put(lblGRD.getText(),getPRMST(txtGRD.getText(),"PR_PRDDS"));  crtOPTNM();}
//				else if(LP_OPTCT.equals("DSR"))
//					{hstOPTNM.put(lblDSR.getText(),getPTMST("D",txtDSR.getText(),"PT_PRTNM"));  crtOPTNM();}
//				else if(LP_OPTCT.equals("BYR"))
//					{hstOPTNM.put(lblBYR.getText(),getPTMST("C",txtBYR.getText(),"PT_PRTNM"));  crtOPTNM();}
//				else if(LP_OPTCT.equals("SAL"))
//					{hstOPTNM.put(lblSAL.getText(),getCDTRN("SYSMR00SAL"+txtSAL.getText(),"CMT_CODDS",hstCDTRN)); crtOPTNM();}
//				else if(LP_OPTCT.equals("PMT"))
//					{hstOPTNM.put(lblPMT.getText(),getCDTRN("SYSMRXXPMT"+txtPMT.getText(),"CMT_CODDS",hstCDTRN)); crtOPTNM();}
//				else if(LP_OPTCT.equals("PRD"))
//					{hstOPTNM.put(lblPRD.getText(),getCDTRN("MSTCOXXPGR"+txtPRD.getText()+"00000A","CMT_CODDS",hstCDTRN)); crtOPTNM(); }
//				else if(LP_OPTCT.equals("CNS"))
//					{hstOPTNM.put(lblCNS.getText(),getPTMST("C",txtCNS.getText(),"PT_PRTNM")); crtOPTNM(); }
//			}
//		}
//		catch (Exception L_EX)
//			{setMSG(L_EX,"focusLost");}
//	}
	

	
	/** Accepts option category (LP_OPTCT) as a parameter
	 *  Recording selected options in vector & populating in JList
	 *  for displaying selected categories
	 */
	public void setOPTNM(String LP_OPTCT)
	{
		try
		{
			{
				if(LP_OPTCT.equals("RGN"))
				{vtrOPTNM.addElement(lblRGN.getText()+": "+getCDTRN("SYSMRXXRGN"+txtRGN.getText(),"CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("ZON"))
					{vtrOPTNM.addElement(lblZON.getText()+": "+getCDTRN("SYSMR00ZON"+txtZON.getText(),"CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("GRD"))
					{vtrOPTNM.addElement(lblGRD.getText()+": "+getPRMST(txtGRD.getText(),"PR_PRDDS"));}
				else if(LP_OPTCT.equals("DSR"))
					{vtrOPTNM.addElement(lblDSR.getText()+": "+getPTMST("D",txtDSR.getText(),"PT_PRTNM"));}
				else if(LP_OPTCT.equals("BYR"))
					{vtrOPTNM.addElement(lblBYR.getText()+": "+getPTMST("C",txtBYR.getText(),"PT_PRTNM"));}
				else if(LP_OPTCT.equals("SAL"))
					{vtrOPTNM.addElement(lblSAL.getText()+": "+getCDTRN("SYSMR00SAL"+txtSAL.getText(),"CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("PMT"))
					{vtrOPTNM.addElement(lblPMT.getText()+": "+getCDTRN("SYSMRXXPMT"+txtPMT.getText(),"CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("PRD"))
					{vtrOPTNM.addElement(lblPRD.getText()+": "+getCDTRN("MSTCOXXPGR"+txtPRD.getText()+"00000A","CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("CNS"))
					{vtrOPTNM.addElement(lblCNS.getText()+": "+getPTMST("C",txtCNS.getText(),"PT_PRTNM"));}
			}
			lstOPTNM.setListData(vtrOPTNM);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"focusLost");}
	}
	
	
	

	
	/** method from cl_rbase overidden, as Printing is taken
	 * after selecting Scrren Display option
	 */
	/*String[] getPRINTERS()
	{
		
		flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		aset = new HashPrintRequestAttributeSet();
		pservices =PrintServiceLookup.lookupPrintServices(flavor, aset);
		M_cmbDESTN.insertItemAt("Select",0);
		for(int i=0;i<pservices.length;i++)
			M_cmbDESTN.addItem(pservices[i].getName());
		M_cmbDESTN.setSelectedIndex(1);
		if(M_cmbDESTN.getItemCount()==2)
			M_cmbDESTN.setEnabled(false);
		String [] L_staTEMP=new String[M_cmbDESTN.getItemCount()];
		for(int i=0;i<L_staTEMP.length;i++)
			L_staTEMP[i]=M_cmbDESTN.getItemAt(i).toString();
		return L_staTEMP;
	}*/
	
	
/** Method for printing report by getting list of printers
 */
private void exePRINT1()
{
	try
	{
		//M_cmbDESTN.removeAllItems();
		getPRINTERS();
		int intRESP = JOptionPane.showConfirmDialog( this,M_cmbDESTN,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
		if(intRESP==0)
			doPRINT(strRESSTR);
	}
	catch(Exception L_EX) {setMSG(L_EX,"exePRINT1");}
}
	
	



	/**
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
		M_objSOURC=L_KE.getSource();
		//if(M_objSOURC==txtSPCCT && L_KE.getKeyCode()==L_KE.VK_ENTER)
		//	getSUBHDR();
		if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				//M_strHLPFLD = "txtSPCCT";
				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
					{M_strHLPFLD = "txtRGN"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN' and cmt_codcd in (select cmt_chp02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+"))  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strZONWS))
					{M_strHLPFLD = "txtZON"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strGRDWS))
					{M_strHLPFLD = "txtGRD"; cl_hlp("Select PR_PRDCD, PR_PRDDS from co_prmst where SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54') and pr_prdcd in (select distinct "+(flgIVT_RLSN ? " ivt_prdcd from MR_IVTRN " : " int_prdcd from vw_intrn")+" where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PR_PRDCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strDSRWS))
					{M_strHLPFLD = "txtDSR"; cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='D' and pt_prtcd in (select distinct "+(flgIVT_RLSN ? " ivt_dsrcd from MR_IVTRN " : " in_dsrcd from vw_intrn")+" where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strBYRWS))
					{M_strHLPFLD = "txtBYR"; cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' and pt_prtcd in (select distinct "+(flgIVT_RLSN ? " ivt_byrcd from MR_IVTRN " : " in_byrcd from vw_intrn")+" where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
					{M_strHLPFLD = "txtSAL"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd in (select "+(flgIVT_RLSN ? " ivt_saltp from MR_IVTRN " : " in_saltp from vw_intrn")+" where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPMTWS))
					{M_strHLPFLD = "txtPMT"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXPMT' and cmt_codcd in (select "+(flgIVT_RLSN ? " ivt_pmtcd from MR_IVTRN " : " in_pmtcd from vw_intrn")+" where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
					{M_strHLPFLD = "txtPRD"; cl_hlp("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and SUBSTRING(cmt_codcd,1,4) in (select distinct "+(flgIVT_RLSN ? " SUBSTRING(ivt_prdcd,1,4) int_prdcd from MR_IVTRN " : " SUBSTRING(int_prdcd,1,4) int_prdcd from vw_intrn")+" where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strCNSWS))
					{M_strHLPFLD = "txtCNS"; cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' and pt_prtcd in (select distinct "+(flgIVT_RLSN ? " ivt_cnscd from MR_IVTRN " : " in_cnscd from vw_intrn")+" where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
			}
		}
		catch (Exception L_EX) {setMSG(L_EX,"keyPressed");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}



	/**
	 */
	public void exeHLPOK()
	{
		try
		{
			super.exeHLPOK();
			cl_dat.M_flgHELPFL_pbst = false;
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtRGN"))
				{txtRGN.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtZON"))
				{txtZON.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtGRD"))
				{txtGRD.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtDSR"))
				{txtDSR.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtBYR"))
				{txtBYR.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtSAL"))
				{txtSAL.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPMT"))
				{txtPMT.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPRD"))
				{txtPRD.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtCNS"))
				{txtCNS.setText(L_STRTKN.nextToken());}
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeHLPOK");}
	}

	
	

	/** Displaying Selective Detail 
	 */
	private void dspCNSDTL()
	{
		try
		{
			strORDBY = " asc";
			if(rdbORDER_D.isSelected() == true)
				strORDBY = " desc";
			ResultSet rstRSSET = null;
			if(cmbKEY01.getSelectedItem().toString().equals(strRGNWS) || cmbKEY02.getSelectedItem().toString().equals(strRGNWS))
			{
				if (flgIVT_RLSN)
					M_strSQLQRY = "select "+strKEY01_IVT+" in_key01,"+strKEY02_IVT+" in_key02, sum((isnull(int_indqt,0)-isnull(int_fcmqt,0))) int_indqt from mr_ivtrn,co_cdtrn where "+strWHRSTR+" and ivt_zoncd=cmt_codcd and cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' group by "+strKEY01_IVT+","+strKEY02_IVT;
				else
					M_strSQLQRY = "select "+strKEY01_INT+" in_key01,"+strKEY02_INT+" in_key02,sum("+strQTFLD_INT+") int_indqt from vw_intrn,co_cdtrn where "+strWHRSTR+" and in_zoncd=cmt_codcd and cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' group by "+strKEY01_INT+","+strKEY02_INT;
			}
			else
			{
				if (flgIVT_RLSN)
					M_strSQLQRY = "select "+strKEY01_IVT+" in_key01,"+strKEY02_IVT+" in_key02, sum(isnull(ivt_invqt,0)) int_indqt from mr_ivtrn where "+strWHRSTR+" group by "+strKEY01_IVT+","+strKEY02_IVT;
				else
					M_strSQLQRY = "select "+strKEY01_INT+" in_key01,"+strKEY02_INT+" in_key02,sum("+strQTFLD_INT+") int_indqt from vw_intrn where "+strWHRSTR+" group by "+strKEY01_INT+","+strKEY02_INT;
			}
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			crtINTRN_C1(rstRSSET);

			//String M_strSQLQRY_DET ="";			
			if(cmbKEY01.getSelectedItem().toString().equals(strRGNWS) || cmbKEY02.getSelectedItem().toString().equals(strRGNWS))
			{
				if (flgIVT_RLSN)
				{
					M_strSQLQRY = "select "+strKEY01_IVT+" in_key01,"+strKEY02_IVT+" in_key02,isnull(ivt_invqt,0) int_indqt, isnull(ivt_cprvl,0) int_cprvl,int(round((isnull(ivt_rsnvl,0)-isnull(ivt_crdvl,0))/isnull(ivt_invqt,1),0)) int_rsnvl from mr_ivtrn,co_cdtrn where "+strWHRSTR+" and ivt_zoncd=cmt_codcd and cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' order by "+strKEY01_IVT+","+strKEY02_IVT;
				}
				else
				{
					M_strSQLQRY = "select "+strKEY01_INT+" in_key01,"+strKEY02_INT+" in_key02,"+strQTFLD_INT+" int_indqt,isnull(int_cprvl,0) int_cprvl, "+strVLFLD_INT+" int_rsnvl from vw_intrn,co_cdtrn where "+strWHRSTR+"  and in_zoncd=cmt_codcd and cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' order by "+strKEY01_INT+","+strKEY02_INT;
				}
			}
			else
			{
				if (flgIVT_RLSN)
				{
					M_strSQLQRY = "select "+strKEY01_IVT+" in_key01,"+strKEY02_IVT+" in_key02,isnull(ivt_invqt,0) int_indqt, isnull(ivt_cprvl,0) int_cprvl,int(round((isnull(ivt_rsnvl,0)-isnull(ivt_crdvl,0))/isnull(ivt_invqt,1),0)) int_rsnvl from mr_ivtrn where "+strWHRSTR+" order by "+strKEY01_IVT+","+strKEY02_IVT;
				}
				else
				{
					M_strSQLQRY = "select "+strKEY01_INT+" in_key01,"+strKEY02_INT+" in_key02,"+strQTFLD_INT+" int_indqt,isnull(int_cprvl,0) int_cprvl, "+strVLFLD_INT+" int_rsnvl from vw_intrn where "+strWHRSTR+" order by "+strKEY01_INT+","+strKEY02_INT;
				}
			}

			
			//M_strSQLQRY = "select in_cnscd,"+L_strQTFLD+" int_indqt, isnull(int_cprvl,0) int_cprvl,int(round((isnull(int_rsnvl,0)-isnull(int_crdvl,0))/isnull(int_indqt,1),0)) int_rsnvl from vw_intrn where "+strWHRSTR+" order by in_cnscd";
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			crtINTRN_C2(rstRSSET);
			
			int i;
			i=0;
			int L_intRECCT = Integer.parseInt(txtRECCT.getText());

			tblCNSRPT.clrTABLE();
			M_strSQLQRY = "select wr_key01, wr_key02,wr_keyvl from mr_cpwork order by wr_keyvl "+strORDBY;
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;

			dblINDQT_TOT=0; dblRSNVL_TOT=0; dblCPRVL_TOT=0; dblDFFVL_TOT=0; dblNEFVL_TOT=0;
			double L_dblINDQT=0, L_dblRSNVL=0, L_dblCPRVL=0, L_dblDFFVL=0, L_dblTOTVL=0, L_dblNEFVL=0;
			while(true)
			{
				if(L_intRECCT>0 && i>=L_intRECCT)
					break;
				//System.out.println("001");
				String L_strKEY01 = getRSTVAL(rstRSSET,"WR_KEY01","C");
				String L_strKEY02 = getRSTVAL(rstRSSET,"WR_KEY02","C");
				//System.out.println("002");
			    tblCNSRPT.setValueAt(setKEY01_DS(L_strKEY01),i,intTB1_KEY01);
			    tblCNSRPT.setValueAt(setKEY02_DS(L_strKEY02),i,intTB1_KEY02);
				//System.out.println("003");
				L_dblINDQT=0; L_dblRSNVL=0; L_dblCPRVL=0; L_dblDFFVL=0; L_dblTOTVL=0; L_dblNEFVL=0;
				L_dblINDQT = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_INDQT"));
				if(L_dblINDQT==0)
					if(!rstRSSET.next())
						break;  else  continue;
				//System.out.println("L_dblINDQT : "+L_dblINDQT);
				L_dblRSNVL = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_RSNVL"));
				//System.out.println("L_dblRSNVL : "+L_dblRSNVL);
				L_dblCPRVL = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_CPRVL"));
				//System.out.println("L_dblCPRVL : "+L_dblCPRVL);
				L_dblDFFVL = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_DFFVL"));
				//System.out.println("L_dblDFFVL : "+L_dblDFFVL);
				L_dblNEFVL = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_NEFVL"));
				//System.out.println("L_dblNEFVL : "+L_dblNEFVL);
				L_dblTOTVL = L_dblDFFVL*L_dblINDQT;
				//System.out.println("004");
				tblCNSRPT.setValueAt(setNumberFormat(L_dblINDQT,3),i,intTB1_INDQT);
				tblCNSRPT.setValueAt(setNumberFormat(L_dblRSNVL,0),i,intTB1_RSNVL);
				tblCNSRPT.setValueAt(setNumberFormat(L_dblCPRVL,0),i,intTB1_CPRVL);
				tblCNSRPT.setValueAt(setNumberFormat(L_dblDFFVL,0),i,intTB1_DFFVL);
				tblCNSRPT.setValueAt(setNumberFormat(L_dblTOTVL,0),i,intTB1_TOTVL);
				tblCNSRPT.setValueAt(setNumberFormat(L_dblNEFVL,0),i,intTB1_NEFVL);
				i++;
				//System.out.println("005");
				dblINDQT_TOT += L_dblINDQT;
				dblRSNVL_TOT += (L_dblINDQT*L_dblRSNVL);
				dblCPRVL_TOT += (L_dblINDQT*L_dblCPRVL);
				dblNEFVL_TOT += L_dblNEFVL;
				if(!rstRSSET.next())
					break;
			}
			dblDFFVL_TOT = Double.parseDouble(setNumberFormat((dblRSNVL_TOT-dblCPRVL_TOT)/dblINDQT_TOT,0));
			tblCNSTOT.setValueAt("Total",0,intTB1_KEY01);
			tblCNSTOT.setValueAt(setNumberFormat(dblINDQT_TOT,3),0,intTB1_INDQT);
			tblCNSTOT.setValueAt(setNumberFormat(dblRSNVL_TOT/dblINDQT_TOT,0),0,intTB1_RSNVL);
			tblCNSTOT.setValueAt(setNumberFormat(dblCPRVL_TOT/dblINDQT_TOT,0),0,intTB1_CPRVL);
			tblCNSTOT.setValueAt(setNumberFormat(dblDFFVL_TOT,0),0,intTB1_DFFVL);
			tblCNSTOT.setValueAt(setNumberFormat(dblDFFVL_TOT*dblINDQT_TOT,0),0,intTB1_TOTVL);
			tblCNSTOT.setValueAt(setNumberFormat(dblNEFVL_TOT,0),0,intTB1_NEFVL);
			setN_EFF(tblCNSRPT, tblCNSTOT, intTB1_TOTVL);
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspCNSDTL");}
	}
	

	/** Fetching Description for the first key field
	 */
	private String setKEY01_DS(String LP_KEYCD)
	{
		try
		{
			if(cmbKEY01.getSelectedItem().toString().equals(strCNSWS))
				return getPTMST("C",LP_KEYCD,"PT_PRTNM");
			else if(cmbKEY01.getSelectedItem().toString().equals(strBYRWS))
				return getPTMST("C",LP_KEYCD,"PT_PRTNM");
			else if(cmbKEY01.getSelectedItem().toString().equals(strDSRWS))
				return getPTMST("D",LP_KEYCD,"PT_PRTNM");
			else if(cmbKEY01.getSelectedItem().toString().equals(strZONWS))
				return getCDTRN("SYSMR00ZON"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strSALWS))
				return getCDTRN("SYSMR00SAL"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strGRDWS))
				return getPRMST(LP_KEYCD,"PR_PRDDS");
			else if(cmbKEY01.getSelectedItem().toString().equals(strPRDWS))
				return getCDTRN("MSTCOXXPGR"+LP_KEYCD+"00000A","CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strPMTWS))
				return getCDTRN("SYSMRXXPMT"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strRGNWS))
				return getCDTRN("SYSMRXXRGN"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
		}
		catch (Exception L_EX) {setMSG(L_EX,"setKEY01_DS");}
		return "";
	}
	

	
	/** Fetching Description for the second key field
	 */
	private String setKEY02_DS(String LP_KEYCD)
	{
		try
		{
			if(cmbKEY02.getSelectedItem().toString().equals(strCNSWS))
				return getPTMST("C",LP_KEYCD,"PT_PRTNM");
			else if(cmbKEY02.getSelectedItem().toString().equals(strBYRWS))
				return getPTMST("C",LP_KEYCD,"PT_PRTNM");
			else if(cmbKEY02.getSelectedItem().toString().equals(strDSRWS))
				return getPTMST("D",LP_KEYCD,"PT_PRTNM");
			else if(cmbKEY02.getSelectedItem().toString().equals(strZONWS))
				return getCDTRN("SYSMR00ZON"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strSALWS))
				return getCDTRN("SYSMR00SAL"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strGRDWS))
				return getPRMST(LP_KEYCD,"PR_PRDDS");
			else if(cmbKEY02.getSelectedItem().toString().equals(strPRDWS))
				return getCDTRN("MSTCOXXPGR"+LP_KEYCD+"00000A","CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strPMTWS))
				return getCDTRN("SYSMRXXPMT"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strRGNWS))
				return getCDTRN("SYSMRXXRGN"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
		}
		catch (Exception L_EX) {setMSG(L_EX,"setKEY02_DS");}
		return "";
	}
	
	/** Displaying gradewise detail
	 */
	private void dspGRDDTL()
	{
		try
		{
			strORDBY = " asc";
			if(rdbORDER_D.isSelected() == true)
				strORDBY = " desc";
			ResultSet rstRSSET = null;
			if (flgIVT_RLSN)
				M_strSQLQRY = "select ivt_prdcd int_prdcd,sum(isnull(ivt_invqt,0)) int_indqt from mr_ivtrn where "+strWHRSTR+" group by ivt_prdcd ";
			else
				M_strSQLQRY = "select int_prdcd,sum("+strQTFLD_INT+") int_indqt from vw_intrn where "+strWHRSTR+" group by int_prdcd ";
			//System.out.println(M_strSQLQRY);

			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			crtINTRN_G1(rstRSSET);			
			
			if (flgIVT_RLSN)
				M_strSQLQRY = "select ivt_prdcd int_prdcd,isnull(ivt_invqt,0) int_indqt,int(round((isnull(ivt_rsnvl,0)-isnull(ivt_crdvl,0))/isnull(ivt_invqt,1),0)) int_rsnvl, isnull(ivt_cprvl,0) int_cprvl from mr_ivtrn where "+strWHRSTR+" order by ivt_prdcd";
			else
				M_strSQLQRY = "select int_prdcd,"+strQTFLD_INT+" int_indqt,"+strVLFLD_INT+" int_rsnvl, isnull(int_cprvl,0) int_cprvl from vw_intrn where "+strWHRSTR+" order by int_prdcd";
			//System.out.println(M_strSQLQRY);

			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			crtINTRN_G2(rstRSSET);			
			
			int i;
			i=0;
			tblGRDRPT.clrTABLE();

			int L_intRECCT = Integer.parseInt(txtRECCT.getText());
			M_strSQLQRY = "select wr_key01,wr_keyvl from mr_cpwork order by wr_keyvl "+strORDBY;
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			dblINDQT_TOT=0; dblRSNVL_TOT=0; dblCPRVL_TOT=0; dblDFFVL_TOT=0;
			double L_dblINDQT=0, L_dblRSNVL=0, L_dblCPRVL=0, L_dblDFFVL=0, L_dblTOTVL=0;
			while(true)
			{
				if(L_intRECCT>0 && i>=L_intRECCT)  // Check for Top xx number of records
					break;
				String L_strPRDCD = getRSTVAL(rstRSSET,"WR_KEY01","C");
				L_dblINDQT=0; L_dblRSNVL=0; L_dblCPRVL=0; L_dblDFFVL=0; L_dblTOTVL=0;
				L_dblINDQT = Double.parseDouble(getINTRN_G2(L_strPRDCD,"INT_INDQT"));
				L_dblRSNVL = Double.parseDouble(getINTRN_G2(L_strPRDCD,"INT_RSNVL"));
				L_dblCPRVL = Double.parseDouble(getINTRN_G2(L_strPRDCD,"INT_CPRVL"));
				L_dblDFFVL = Double.parseDouble(getINTRN_G2(L_strPRDCD,"INT_DFFVL"));
				L_dblTOTVL = L_dblDFFVL*L_dblINDQT;
			    tblGRDRPT.setValueAt(getPRMST(L_strPRDCD,"PR_PRDDS"),i,intTB2_PRDDS);
				tblGRDRPT.setValueAt(setNumberFormat(L_dblINDQT,3),i,intTB2_INDQT);
				tblGRDRPT.setValueAt(setNumberFormat(L_dblRSNVL,0),i,intTB2_RSNVL);
				tblGRDRPT.setValueAt(setNumberFormat(L_dblCPRVL,0),i,intTB2_CPRVL);
				tblGRDRPT.setValueAt(setNumberFormat(L_dblDFFVL,0),i,intTB2_DFFVL);
				tblGRDRPT.setValueAt(setNumberFormat(L_dblTOTVL,0),i,intTB2_TOTVL);
				tblGRDRPT.setValueAt(L_strPRDCD,i,intTB2_PRDCD);
				dblINDQT_TOT += L_dblINDQT;
				dblRSNVL_TOT += (L_dblINDQT*L_dblRSNVL);
				dblCPRVL_TOT += (L_dblINDQT*L_dblCPRVL);
				i++;
			if(!rstRSSET.next())
				break;
			}
			dblDFFVL_TOT = Double.parseDouble(setNumberFormat((dblRSNVL_TOT-dblCPRVL_TOT)/dblINDQT_TOT,0));
			tblGRDTOT.setValueAt("Total",0,intTB2_PRDDS);
			tblGRDTOT.setValueAt(setNumberFormat(dblINDQT_TOT,3),0,intTB2_INDQT);
			tblGRDTOT.setValueAt(setNumberFormat(dblRSNVL_TOT/dblINDQT_TOT,0),0,intTB2_RSNVL);
			tblGRDTOT.setValueAt(setNumberFormat(dblCPRVL_TOT/dblINDQT_TOT,0),0,intTB2_CPRVL);
			tblGRDTOT.setValueAt(setNumberFormat(dblDFFVL_TOT,0),0,intTB2_DFFVL);
			tblGRDTOT.setValueAt(setNumberFormat(dblDFFVL_TOT*dblINDQT_TOT,0),0,intTB2_TOTVL);
			setN_EFF(tblGRDRPT, tblGRDTOT, intTB2_TOTVL);
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspGRDDTL");}
	}
	

	/** Displaying Customer Orderwise detail
	 */
	private void dspINDDTL()
	{
		try
		{
			ResultSet rstRSSET = null;
			Hashtable<String,String> hstINTRN_I1 = new Hashtable<String,String>();
			hstINTRN_I1.clear();
			Hashtable<String,String> hstINTRN_I2 = new Hashtable<String,String>();
			hstINTRN_I2.clear();
			boolean L_flgTOPCHK = false;
			if(Integer.parseInt(txtRECCT.getText())>0)
		    {
				L_flgTOPCHK = true;
				for(int i = 0;i<tblGRDRPT.getRowCount();i++)
				{
					if(tblGRDRPT.getValueAt(i,intTB2_PRDCD).toString().length() != 10)
						break;
					hstINTRN_I1.put(tblGRDRPT.getValueAt(i,intTB2_PRDCD).toString(),"");
				}
				for(int i = 0;i<tblCNSRPT.getRowCount();i++)
				{
					if(tblCNSRPT.getValueAt(i,intTB1_KEY01).toString().length() == 0)
						break;
					hstINTRN_I2.put(tblCNSRPT.getValueAt(i,intTB1_KEY01).toString(),"");
				}
			}
			if(flgIVT_RLSN)
				M_strSQLQRY = "select ivt_prdcd int_prdcd,CONVERT(varchar,ivt_invdt,101) in_inddt,ivt_prdds int_prdds,ivt_invno int_indno,isnull(ivt_invqt,0) int_indqt,int(round((isnull(ivt_rsnvl,0)-isnull(ivt_crdvl,0))/isnull(ivt_invqt,1),0)) int_rsnvl, isnull(ivt_cprvl,0) int_cprvl, isnull(ivt_invrt,0) int_indvl, int(round(isnull(ivt_cclvl,0)/isnull(ivt_invqt,1),0)) int_cclvl,   int(round(isnull(ivt_comvl,0)/isnull(ivt_invqt,1),0)) int_comvl,int(round(isnull(ivt_frtvl,0)/isnull(ivt_invqt,1),0)) int_frtvl,  int(round(isnull(ivt_crdvl,0)/isnull(ivt_invqt,1),0)) int_crdvl, ivt_cnscd in_cnscd from mr_ivtrn where "+strWHRSTR+"  order by ivt_prdcd";
			else
				M_strSQLQRY = "select int_prdcd,int_prdds,int_indno,in_inddt,"+strQTFLD_INT+" int_indqt,"+strVLFLD_INT+" int_rsnvl, isnull(int_cprvl,0) int_cprvl, isnull(int_basrt,0) int_indvl, int(round(isnull(int_cclvl,0)/(isnull(int_indqt,1)-isnull(int_fcmqt,0)),0)) int_cclvl,  int(round(isnull(int_comvl,0)/(isnull(int_indqt,1)-isnull(int_fcmqt,0)),0)) int_comvl, 0 int_frtvl,  int(round(isnull(int_crdvl,0)/(isnull(int_indqt,1)-isnull(int_fcmqt,0)),0)) int_crdvl, in_cnscd from vw_intrn where "+strWHRSTR+"  order by int_prdcd";

			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			dblINDQT_TOT=0; dblRSNVL_TOT=0; dblCPRVL_TOT=0; dblDFFVL_TOT=0;
			dblINDVL_TOT=0; dblCCLVL_TOT=0; dblCOMVL_TOT=0; dblFRTVL_TOT=0; dblCRDVL_TOT=0;
			double L_dblINDQT=0, L_dblRSNVL=0, L_dblCPRVL=0, L_dblDFFVL=0, L_dblTOTVL=0;
			double L_dblINDVL=0, L_dblCCLVL=0, L_dblCOMVL=0, L_dblFRTVL=0, L_dblCRDVL=0;
			int i =0;
			while(true)
			{
				String L_strCNSNM = getPTMST("C",getRSTVAL(rstRSSET,"IN_CNSCD","C"),"PT_PRTNM");
				if(L_flgTOPCHK && !hstINTRN_I2.containsKey(L_strCNSNM) && !hstINTRN_I1.containsKey(getRSTVAL(rstRSSET,"INT_PRDCD","C")))
				{
					if(!rstRSSET.next())
						break;
					else
						continue;
				}
				L_dblINDQT=0; L_dblRSNVL=0; L_dblCPRVL=0; L_dblDFFVL=0; L_dblTOTVL=0;
				L_dblINDQT = Double.parseDouble(getRSTVAL(rstRSSET,"INT_INDQT","C"));
				L_dblRSNVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_RSNVL","C"));
				L_dblCPRVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_CPRVL","C"));
				L_dblINDVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_INDVL","C"));
				L_dblCCLVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_CCLVL","C"));
				L_dblCOMVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_COMVL","C"));
				L_dblFRTVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_FRTVL","C"));
				L_dblCRDVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_CRDVL","C"));
			    tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_PRDDS","C"),i,intTB3_PRDDS);
			    tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_INDNO","C"),i,intTB3_INDNO);
			    tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"IN_INDDT","D"),i,intTB3_INDDT);
				tblINDRPT.setValueAt(setNumberFormat(L_dblINDQT,3),i,intTB3_INDQT);
				tblINDRPT.setValueAt(setNumberFormat(L_dblRSNVL,0),i,intTB3_RSNVL);
				tblINDRPT.setValueAt(setNumberFormat(L_dblCPRVL,0),i,intTB3_CPRVL);
				String L_strDFFVL = setNumberFormat(L_dblRSNVL-dblCPRVL,0);
				String L_strTOTVL = setNumberFormat((L_dblRSNVL-dblCPRVL)*L_dblINDQT,0);
				tblINDRPT.setValueAt(L_strDFFVL,i,intTB3_DFFVL);
				tblINDRPT.setValueAt(L_strTOTVL,i,intTB3_TOTVL);
				tblINDRPT.setValueAt(setNumberFormat(L_dblINDVL,0),i,intTB3_INDVL);
				tblINDRPT.setValueAt(setNumberFormat(L_dblCCLVL,0),i,intTB3_CCLVL);
				tblINDRPT.setValueAt(setNumberFormat(L_dblCOMVL,0),i,intTB3_COMVL);
				tblINDRPT.setValueAt(setNumberFormat(L_dblFRTVL,0),i,intTB3_FRTVL);
				tblINDRPT.setValueAt(setNumberFormat(L_dblCRDVL,0),i,intTB3_CRDVL);
				tblINDRPT.setValueAt(L_strCNSNM,i,intTB3_CNSNM);
				dblINDQT_TOT += L_dblINDQT;
				dblRSNVL_TOT += (L_dblINDQT*L_dblRSNVL);
				dblCPRVL_TOT += (L_dblINDQT*L_dblCPRVL);
				dblINDVL_TOT += (L_dblINDQT*L_dblINDVL);
				dblCCLVL_TOT += (L_dblINDQT*L_dblCCLVL);
				dblCOMVL_TOT += (L_dblINDQT*L_dblCOMVL);
				dblFRTVL_TOT += (L_dblINDQT*L_dblFRTVL);
				dblCRDVL_TOT += (L_dblINDQT*L_dblCRDVL);
				i++;
				if(!rstRSSET.next())
					break;
			}
			dblDFFVL_TOT = Double.parseDouble(setNumberFormat((dblRSNVL_TOT-dblCPRVL_TOT)/dblINDQT_TOT,0));
			tblINDTOT.setValueAt("Total",0,intTB3_PRDDS);
			tblINDTOT.setValueAt(setNumberFormat(dblINDQT_TOT,3),0,intTB3_INDQT);
			tblINDTOT.setValueAt(setNumberFormat(dblRSNVL_TOT/dblINDQT_TOT,0),0,intTB3_RSNVL);
			tblINDTOT.setValueAt(setNumberFormat(dblCPRVL_TOT/dblINDQT_TOT,0),0,intTB3_CPRVL);
			tblINDTOT.setValueAt(setNumberFormat(dblDFFVL_TOT,0),0,intTB3_DFFVL);
			tblINDTOT.setValueAt(setNumberFormat(dblDFFVL_TOT*dblINDQT_TOT,0),0,intTB3_TOTVL);
			tblINDTOT.setValueAt(setNumberFormat(dblINDVL_TOT/dblINDQT_TOT,0),0,intTB3_INDVL);
			tblINDTOT.setValueAt(setNumberFormat(dblCCLVL_TOT/dblINDQT_TOT,0),0,intTB3_CCLVL);
			tblINDTOT.setValueAt(setNumberFormat(dblCOMVL_TOT/dblINDQT_TOT,0),0,intTB3_COMVL);
			tblINDTOT.setValueAt(setNumberFormat(dblFRTVL_TOT/dblINDQT_TOT,0),0,intTB3_FRTVL);
			tblINDTOT.setValueAt(setNumberFormat(dblCRDVL_TOT/dblINDQT_TOT,0),0,intTB3_CRDVL);
			setN_EFF(tblINDRPT, tblINDTOT, intTB3_TOTVL);
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspINDDTL");}
	}
	
	

		
	
	/**  Main procedure getting executed after pressing Display button
	 */
	public void exePRINT()
	{
		try
		{
			this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_txtFMDAT.getText().length()!=10 || M_txtTODAT.getText().length()!=10)
				return;
			if(chkCPMST_EXIST())
				return;
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
			strSBSSTR = setSBSSTR("SBS");
			setWHRSTR_ALL();

						
			if(!exeTBLREFSH())
				return;
			
			setDETAIL_CPR();

			strQTFLD_INT = "(isnull(int_indqt,0)-isnull(int_fcmqt,0))";
			strVLFLD_INT = "int(round((isnull(int_rsnvl,0)-isnull(int_crdvl,0)) / (isnull(int_indqt,1)-isnull(int_fcmqt,0)),0))";
			
			dspCNSDTL();
			dspGRDDTL();
			dspINDDTL();
		}
		catch (Exception L_EX) {setMSG(L_EX,"exePRINT");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}

	
	/** Executing Storedprocedure "setCPRVL" for updating cutoff price from MR_CPMST to MR_INTRN
	 */
	private void setDETAIL_CPR()
	{
		try
		{
			cstCPRVL.setString(1,cl_dat.M_strCMPCD_pbst);
			cstCPRVL.setString(2,cmbMKTTP.getSelectedItem().toString().substring(0,2));
			cstCPRVL.setString(3,M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText())));
			cstCPRVL.setString(4,M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText())));
			//System.out.println("calling : "+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"  "+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"  "+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText())));
			cstCPRVL.executeUpdate();							
		}
		catch (Exception L_EX) {setMSG(L_EX,"setDETAIL_CPR");}
	}
	
	

	
	
	/** Procedure to be activated after selecting category from Scope (cmbRPTWS)
	 *  Calls a procedure for displaying & accepting the category
	 */
	private void getSUBHDR()
	{
		try
		{
				if(cmbRPTWS.getSelectedItem().toString().equals(strALLWS))
					{/*addRPTWS()*/;exeOPT_DSB();inlSTRWHR_XXX();}
				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
					{exeGETSCT(lblRGN, txtRGN);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strZONWS))
					{exeGETSCT(lblZON, txtZON);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strGRDWS))
					{exeGETSCT(lblGRD, txtGRD);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strDSRWS))
					{exeGETSCT(lblDSR, txtDSR);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strBYRWS))
					{exeGETSCT(lblBYR, txtBYR);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
					{exeGETSCT(lblSAL, txtSAL);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPMTWS))
					{exeGETSCT(lblPMT, txtPMT);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
					{exeGETSCT(lblPRD, txtPRD);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strCNSWS))
					{exeGETSCT(lblCNS, txtCNS);}
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"getSUBHDR");}
	}
		

	/** Setting the selection condition (where condition) in a query depending on the 
	 * content entered in the category
	 */
	private void getSUBHDR1()
	{
		try
		{
			if (cmbRPTWS.getSelectedIndex()>0)
			{
				if(txtRGN.getText().length()>0)
				{
					strWHRINT_RGN = setWHRSUB(strWHRINT_RGN, " in_zoncd in (select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_chp02 = '"+txtRGN.getText()+"')");
					strWHRIVT_RGN = setWHRSUB(strWHRIVT_RGN, " ivt_zoncd in (select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_chp02 = '"+txtRGN.getText()+"')");
				}
				if(txtZON.getText().length()>0)
				{
					strWHRINT_ZON = setWHRSUB(strWHRINT_ZON, " in_zoncd = '"+txtZON.getText()+"'");
					strWHRIVT_ZON = setWHRSUB(strWHRIVT_ZON, " ivt_zoncd = '"+txtZON.getText()+"'"); 
				}
				if(txtGRD.getText().length()>0)
				{
					strWHRINT_GRD = setWHRSUB(strWHRINT_GRD," int_prdcd  = '"+txtGRD.getText()+"'"); 
					strWHRIVT_GRD = setWHRSUB(strWHRIVT_GRD," ivt_prdcd  = '"+txtGRD.getText()+"'"); 
				}
				if(txtDSR.getText().length()>0)
				{
					strWHRINT_DSR = setWHRSUB(strWHRINT_DSR," in_dsrcd  = '"+txtDSR.getText()+"'"); 
					strWHRIVT_DSR = setWHRSUB(strWHRIVT_DSR," ivt_dsrcd  = '"+txtDSR.getText()+"'"); 
				}
				if(txtBYR.getText().length()>0)
				{
					strWHRINT_BYR = setWHRSUB(strWHRINT_BYR," in_byrcd  = '"+txtBYR.getText()+"'");
					strWHRIVT_BYR = setWHRSUB(strWHRIVT_BYR," ivt_byrcd  = '"+txtBYR.getText()+"'");
				}
				if(txtSAL.getText().length()>0)
				{
					strWHRINT_SAL = setWHRSUB(strWHRINT_SAL," in_saltp  = '"+txtSAL.getText()+"'"); 
					strWHRIVT_SAL = setWHRSUB(strWHRIVT_SAL," ivt_saltp = '"+txtSAL.getText()+"'"); 
				}
				if(txtPMT.getText().length()>0)
				{
					strWHRINT_PMT = setWHRSUB(strWHRINT_PMT," in_pmtcd  = '"+txtPMT.getText()+"'"); 
					strWHRIVT_PMT = setWHRSUB(strWHRIVT_PMT," ivt_pmtcd = '"+txtPMT.getText()+"'");
				}
				if(txtPRD.getText().length()>0)
				{
					strWHRINT_PRD = setWHRSUB(strWHRINT_PRD," SUBSTRING(int_prdcd,1,4) = '"+txtPRD.getText()+"'"); 
					strWHRIVT_PRD = setWHRSUB(strWHRIVT_PRD," SUBSTRING(ivt_prdcd,1,4) = '"+txtPRD.getText()+"'"); 
				}
				if(txtCNS.getText().length()>0)
				{
					strWHRINT_CNS = setWHRSUB(strWHRINT_CNS," in_cnscd  = '"+txtCNS.getText()+"'");
					strWHRIVT_CNS = setWHRSUB(strWHRIVT_CNS," ivt_cnscd = '"+txtCNS.getText()+"'");
				}
			}
			inlTXTXXX();
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"getSUBHDR");}
	}
	

	
	/** Initializing the categorywise condition strings
	 */
	private void inlSTRWHR_XXX()
	{
		try
		{
				strWHRINT_RGN = "";
				strWHRINT_ZON = "";
				strWHRINT_GRD = "";
				strWHRINT_DSR = "";
				strWHRINT_BYR = "";
				strWHRINT_SAL = "";
				strWHRINT_PMT = "";
				strWHRINT_PRD = "";
				strWHRINT_CNS = "";

				strWHRIVT_RGN = "";
				strWHRIVT_ZON = "";
				strWHRIVT_GRD = "";
				strWHRIVT_DSR = "";
				strWHRIVT_BYR = "";
				strWHRIVT_SAL = "";
				strWHRIVT_PMT = "";
				strWHRIVT_PRD = "";
				strWHRIVT_CNS = "";
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"inlSTRWHR_XXX");}
	}
	
	
	/** Creating a string for where condition of the query
	 * Considering various selections made
	 */	
	private void setWHRSTR_ALL()
	{
		try
		{
			strWHRSTR = setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText());
				if(strWHRINT_RGN.length() > 2)
				{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_RGN : strWHRINT_RGN)+") ";}
				if(strWHRINT_ZON.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_ZON : strWHRINT_ZON)+") ";}
				if(strWHRINT_GRD.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_GRD : strWHRINT_GRD)+") ";}
				if(strWHRINT_DSR.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_DSR : strWHRINT_DSR)+") ";}
				if(strWHRINT_BYR.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_BYR : strWHRINT_BYR)+") ";}
				if(strWHRINT_SAL.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_SAL : strWHRINT_SAL)+") ";}
				if(strWHRINT_PMT.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_PMT : strWHRINT_PMT)+") ";}
				if(strWHRINT_PRD.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_PRD : strWHRINT_PRD)+") ";}
				if(strWHRINT_CNS.length() > 2)
					{strWHRSTR += " and ("+(flgIVT_RLSN ? strWHRIVT_CNS : strWHRINT_CNS)+") ";}
				//System.out.println(strWHRSTR);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setWHRSTR_ALL");}
	}

	
	
	/** Initializing category text fields 
	 */
	private void inlTXTXXX()
	{
		try
		{
			txtRGN.setText("");
			txtZON.setText("");
			txtGRD.setText("");
			txtDSR.setText("");
			txtBYR.setText("");
			txtSAL.setText("");
			txtPMT.setText("");
			txtPRD.setText("");
			txtCNS.setText("");
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"inlTXTXXX");}
	}
	
	
	
	/** Generating string for where condition for Individual catgory selected
	 */
	private String setWHRSUB(String LP_ORGVL, String LP_ADDVL)
	{
		return	LP_ORGVL + (LP_ORGVL.length()>0 ? " OR " : "") + LP_ADDVL;
	}

	
	
	
	
	/** Displaying label & text field for 
	 */
	private void exeGETSCT(JLabel LP_LBLCMP,JTextField LP_TXTCMP)
	{
		exeOPT_DSB1();
		LP_LBLCMP.setVisible(true);
		LP_TXTCMP.setVisible(true);
		LP_TXTCMP.requestFocus();
	}
	
	/**  Report heaer for Selective Detail
	 */	
	private void crtCNSHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intCNSWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Selective Realisation Analysis from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intCNSWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			setOPTHDR();						
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intCNSWD,"-")+"\n"); intLINECT +=1;
				  for(i=1;i<arrCNSHDR.length;i++)
			{
				O_DOUT.writeBytes(padSTRING(arrCNSHDR_PAD[i],arrCNSHDR[i],arrCNSHDR_WD[i]/8)+" "); intRUNCL +=1;
				if(intRUNCL>intCNSCL)
					{O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrCNSHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;}
			}
			O_DOUT.writeBytes("\n"+crtLINE(intCNSWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtCNSHDR");}
	}


	/** Writing detail of option selection in report header
	 */
	private void setOPTHDR()
	{
		try
		{
			if(vtrOPTNM.size()>0)
			{
				for(int i=0;i<vtrOPTNM.size();i++)
				{
					O_DOUT.writeBytes(vtrOPTNM.get(i).toString()+"  ");
					//System.out.println("i :"+i +" i%3 :"+i%3);
					if(i % 3 == 0 && i>0) {O_DOUT.writeBytes("\n"); intLINECT ++;}
				}
				O_DOUT.writeBytes("\n"); intLINECT ++;
			}
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setOPTHDR");}
	}

	/** Gradewise report header
	 */	
	private void crtGRDHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intGRDWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Gradewise Realisation Analysis from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intGRDWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			setOPTHDR();						
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intGRDWD,"-")+"\n"); intLINECT +=1;
				  for(i=1;i<arrGRDHDR.length;i++)
			{
				O_DOUT.writeBytes(padSTRING(arrGRDHDR_PAD[i],arrGRDHDR[i],arrGRDHDR_WD[i]/8)+" "); intRUNCL +=1;
				if(intRUNCL>intGRDCL)
					{O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrGRDHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;}
			}
			O_DOUT.writeBytes("\n"+crtLINE(intGRDWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtGRDHDR");}
	}
	

	
	
	/** Customer Orderwise report header
	 */	
	private void crtINDHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intINDWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Doucumentwise Realisation Detail from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intINDWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			setOPTHDR();						
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intINDWD,"-")+"\n"); intLINECT +=1;
				  for(i=1;i<arrINDHDR.length;i++)
			{
				O_DOUT.writeBytes(padSTRING(arrINDHDR_PAD[i],arrINDHDR[i],arrINDHDR_WD[i]/8)+" "); intRUNCL +=1;
				if(intRUNCL>intINDCL)
					{O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrINDHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;}
			}
			O_DOUT.writeBytes("\n"+crtLINE(intINDWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtINDHDR");}
	}
	

	
	
	/**  Selective Report Generation
	 */
	private void crtCNSRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72;

			int i=0, j = tblCNSRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtCNSHDR();
			for(i=0;i<tblCNSRPT.getRowCount();i++)
			{
				if(tblCNSRPT.getValueAt(i,intTB1_RSNVL).toString().equals(""))
					break;
				else if(Double.parseDouble(tblCNSRPT.getValueAt(i,intTB1_RSNVL).toString())==0)
					break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrCNSHDR_PAD[k],tblCNSRPT.getValueAt(i,k).toString().trim(),arrCNSHDR_WD[k]/8)+" "); intRUNCL += 1;
					if(intRUNCL>intCNSCL)
					{
						O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrCNSHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						
					}
				}
				O_DOUT.writeBytes("\n\n");intLINECT +=2;
				if(intLINECT>60) crtCNSHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intCNSWD,"-")+"\n"); intLINECT +=2;
			// Printing Total
			i=0;	intRUNCL = 0;
			for(k=1;k<j;k++)
			{
					
				O_DOUT.writeBytes(padSTRING(arrCNSHDR_PAD[k],tblCNSTOT.getValueAt(i,k).toString().trim(),arrCNSHDR_WD[k]/8)+" "); intRUNCL += 1;
				if(intRUNCL>intCNSCL)
				{
					O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrCNSHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						
				}
			}
			if(chkN_EFF.isSelected()==true)
				O_DOUT.writeBytes("\n (-ve Effect considered in Total +/-)");
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtCNSRPT");}
	}
	

	
	/** Gradewise Report Generation
	 */
	private void crtGRDRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72;

			int i=0, j = tblGRDRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtGRDHDR();
			for(i=0;i<tblGRDRPT.getRowCount();i++)
			{
				if(tblGRDRPT.getValueAt(i,intTB2_RSNVL).toString().equals(""))
					break;
				else if(Double.parseDouble(tblGRDRPT.getValueAt(i,intTB2_RSNVL).toString())==0)
					break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrGRDHDR_PAD[k],tblGRDRPT.getValueAt(i,k).toString().trim(),arrGRDHDR_WD[k]/8)+" "); intRUNCL += 1;
					if(intRUNCL>intGRDCL)
					{
						O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrGRDHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						
					}
				}
				O_DOUT.writeBytes("\n\n");intLINECT +=2;
				if(intLINECT>60) crtGRDHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intGRDWD,"-")+"\n"); intLINECT +=2;
			i=0;	intRUNCL = 0;
			for(k=1;k<j;k++)
			{
				O_DOUT.writeBytes(padSTRING(arrGRDHDR_PAD[k],tblGRDTOT.getValueAt(i,k).toString().trim(),arrGRDHDR_WD[k]/8)+" "); intRUNCL += 1;
				if(intRUNCL>intGRDCL)
				{
					O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrGRDHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						
				}
			}
			if(chkN_EFF.isSelected()==true)
				O_DOUT.writeBytes("\n (-ve Effect considered in Total +/-)");
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtGRDRPT");}
	}


	
	/** CustomerOrderwise Report Generation
	 */
	private void crtINDRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72;

			int i=0, j = tblINDRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtINDHDR();
			for(i=0;i<tblINDRPT.getRowCount();i++)
			{
				if(tblINDRPT.getValueAt(i,intTB3_RSNVL).toString().equals(""))
					break;
				else if(Double.parseDouble(tblINDRPT.getValueAt(i,intTB3_RSNVL).toString())==0)
					break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrINDHDR_PAD[k],tblINDRPT.getValueAt(i,k).toString().trim(),arrINDHDR_WD[k]/8)+" "); intRUNCL += 1;
					if(intRUNCL>intINDCL)
					{
						O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrINDHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
					}
				}
				O_DOUT.writeBytes("\n");intLINECT +=1;
				if(intLINECT>60) crtINDHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intINDWD,"-")+"\n"); intLINECT +=2;
			i=0;	intRUNCL = 0;
			for(k=1;k<j;k++)
			{
				O_DOUT.writeBytes(padSTRING(arrINDHDR_PAD[k],tblINDTOT.getValueAt(i,k).toString().trim(),arrINDHDR_WD[k]/8)+" "); intRUNCL += 1;
				if(intRUNCL>intINDCL)
				{
					O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrINDHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
				}
			}
			if(chkN_EFF.isSelected()==true)
				O_DOUT.writeBytes("\n (-ve Effect considered in Total +/-)");
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtINDRPT");}
	}
	
	
	
	
	

	/**Dynamic Recreation of display table
	 * According to content/values applicable at the time of execution
	 */
	private boolean exeTBLREFSH()
	{
		try
		{
			tbpMAIN.remove(pnlCNSRPT);
			tbpMAIN.remove(pnlGRDRPT);
			tbpMAIN.remove(pnlINDRPT);
			pnlCNSRPT.removeAll();
			pnlGRDRPT.removeAll();
			pnlINDRPT.removeAll();

			pnlCNSRPT = new JPanel(null);
			pnlGRDRPT = new JPanel(null);
			pnlINDRPT = new JPanel(null);
			
			int i;
			M_strSQLQRY = "select count(*) int_recct from "+(flgIVT_RLSN ? " mr_ivtrn " : " vw_intrn ")+" where "+strWHRSTR;
			ResultSet rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next()  || rstRSSET==null)
				{setMSG("No Detail Records Found",'E');return false;}
			int L_intRECCT = Integer.parseInt(getRSTVAL(rstRSSET,"InT_RECCT","N"));
			L_intRECCT = 3*L_intRECCT;
			rstRSSET.close();
			//System.out.println("004");
			setKEY01();
			setKEY02();
			tblCNSRPT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,L_intRECCT,0,1,7,7.9,arrCNSHDR_WD,new int[]{0});  
			tblCNSTOT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,1,7,1,2.1,7.9,arrCNSHDR_WD,new int[]{0});  

			tblGRDRPT = crtTBLPNL1(pnlGRDRPT,arrGRDHDR,L_intRECCT,0,1,7,7.9,arrGRDHDR_WD,new int[]{0});  
			tblGRDTOT = crtTBLPNL1(pnlGRDRPT,arrGRDHDR,1,7,1,2.1,7.9,arrGRDHDR_WD,new int[]{0});  

			tblINDRPT = crtTBLPNL1(pnlINDRPT,arrINDHDR,L_intRECCT,0,1,7,7.9,arrINDHDR_WD,new int[]{0});  
			tblINDTOT = crtTBLPNL1(pnlINDRPT,arrINDHDR,1,7,1,2.1,7.9,arrINDHDR_WD,new int[]{0});  

			tbpMAIN.addTab("Selective Detail",pnlCNSRPT);
			tbpMAIN.addTab("Grade Detail",pnlGRDRPT);
			tbpMAIN.addTab("Document Detail",pnlINDRPT);
		
			//System.out.println("005");
			tblCNSRPT.addMouseListener(this);
			tblGRDRPT.addMouseListener(this);
			tblINDRPT.addMouseListener(this);
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeREFSH");}
		return true;
	}

	
	/** Setting string for Sub-system, Zone & Sale Type Filter
	 */	
	private String setSBSSTR(String LP_SBSTP)
	{
		String L_strRETSTR = ""; 
		try
		{
			//System.out.println("M_staUSRRT.length : "+M_staUSRRT.length);
			for(int i=0;i<M_staUSRRT.length;i++)
			{
				if(LP_SBSTP.equals("ZON"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(2,4)+"',";
				else if(LP_SBSTP.equals("SAL"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(4,6)+"',";
				else if(LP_SBSTP.equals("SBS"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(2)+"',";
			}
			//System.out.println("L_strRETSTR : "+L_strRETSTR);
			L_strRETSTR=L_strRETSTR.substring(0,L_strRETSTR.length()-1);
		}
		catch(Exception L_EX) {setMSG(L_EX,"setSBSSTR");}
		return L_strRETSTR;
	}
	
	
	/**  setting where condition from the content of date range and sub-system detail
	 */
	private String setWHRSTR(String LP_STRDT, String LP_ENDDT)
	{
		//System.out.println("setWHRSTR : "+LP_STRDT+"/"+LP_ENDDT);
		String L_strRETSTR = "";
		try
		{
			//String L_strDTFLD = (flgIVT_RLSN ? " ivt_bkgdt " : " in_bkgdt ");
			String L_strDTFLD = (flgIVT_RLSN ? " CONVERT(varchar,ivt_invdt,101) " : " in_bkgdt ");
			String L_strNGEFF = "";
			if(chkN_EFF.isSelected()==true)
				L_strNGEFF = flgIVT_RLSN ? " and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(ivt_rsnvl,0)-isnull(ivt_crdvl,0)) < (isnull(ivt_cprvl,0)*isnull(ivt_invqt,0)) " : " and INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(int_rsnvl,0)-isnull(int_crdvl,0)) < isnull(int_cprvl,0)*(isnull(int_indqt,1)-isnull(int_fcmqt,0)) ";
				
			L_strRETSTR = L_strDTFLD +" between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and "+(flgIVT_RLSN ? " IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_stsfl " : " INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND int_stsfl ")+" <>'X' and isnull("+(flgIVT_RLSN ? " ivt_invqt " : " (isnull(int_indqt,0)-isnull(int_fcmqt,0)) ")+",0)>0  and "+(flgIVT_RLSN ? " ivt_mkttp " : " int_mkttp ")+" = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'"
				+ L_strNGEFF+" and "+(flgIVT_RLSN ? " ivt_sbscd " : " int_sbscd ")+" in "+M_strSBSLS;
		}
		catch (Exception L_EX){setMSG(L_EX,"setWHRSTR");}
		return L_strRETSTR;
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
	        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl <> 'X' and SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54')";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	             setMSG("Product Records not found in co_prmst",'E');
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

	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
	    try
	    {
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP+"/"+LP_RSLSET.getString(LP_FLDNM).toString())			;
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
		{System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);setMSG(L_EX,"getRSTVAL");}
	return " ";
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
			hstDSRNM.clear();
			strWHRSTR = setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText());
            L_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct "+(flgIVT_RLSN ? " ivt_byrcd from mr_ivtrn " : " in_byrcd from vw_intrn ")+"  where "+strWHRSTR+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct "+(flgIVT_RLSN ? " ivt_cnscd from mr_ivtrn " : " in_cnscd from vw_intrn ")+"  where "+strWHRSTR+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'D' and pt_prtcd in (select distinct "+(flgIVT_RLSN ? " ivt_dsrcd from mr_ivtrn " : " in_dsrcd from vw_intrn ")+"  where "+strWHRSTR+")";
			//System.out.println(L_strSQLQRY);
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 setMSG("Party records not found in CO_PTMST",'E');
                  return;
            }
            while(true)
            {
                    strPRTTP = getRSTVAL(L_rstRSSET,"PT_PRTTP","C");
                    strPRTCD = getRSTVAL(L_rstRSSET,"PT_PRTCD","C");
                    String[] staPTMST = new String[intPTMST_TOT];
                    staPTMST[intAE_PT_PRTNM] = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
                    staPTMST[intAE_PT_ZONCD] = getRSTVAL(L_rstRSSET,"PT_ZONCD","C");
                    staPTMST[intAE_PT_SHRNM] = getRSTVAL(L_rstRSSET,"PT_SHRNM","C");
                    staPTMST[intAE_PT_ADR01] = getRSTVAL(L_rstRSSET,"PT_ADR01","C");
                    staPTMST[intAE_PT_ADR02] = getRSTVAL(L_rstRSSET,"PT_ADR02","C");
                    staPTMST[intAE_PT_ADR03] = getRSTVAL(L_rstRSSET,"PT_ADR03","C");
                    hstPTMST.put(strPRTTP+strPRTCD,staPTMST);
					if(strPRTTP.equals("D"))
						{hstDSRNM.put(getRSTVAL(L_rstRSSET,"PT_SHRNM","C"),strPRTCD);}
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


	/** Picking up Distributor Details
	 * @param LP_PRTCD		Party Code 
	 * @param LP_FLDNM		Field name for which, details have to be picked up
	 */
    private String getPTMST(String LP_PRTTP,String LP_PRTCD, String LP_FLDNM)
    {
		String L_RETSTR = "";
		try
		{
			if(!hstPTMST.containsKey(LP_PRTTP+LP_PRTCD))
				return "";
		        String[] staPTMST = (String[])hstPTMST.get(LP_PRTTP+LP_PRTCD);
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
			{System.out.println("getPTMST : "+LP_PRTTP+"/"+LP_PRTCD+"/"+LP_FLDNM);setMSG(L_EX,"getPTMST");}
		return L_RETSTR;
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

	
	

	/**
	 */
	public static DataOutputStream crtDTOUTSTR(FileOutputStream outfile){
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}

	/**
	 */
	public static FileOutputStream crtFILE(String strFILE){
		FileOutputStream outFILE = null;
		try{
			File file = new File(strFILE);
			outFILE = new FileOutputStream(file);
        	return outFILE;
		}
		catch(IOException L_IO){
			System.out.println("L_IO FOS...........:"+L_IO);
			return outFILE;		
		}
	}


	/**
	 *
	 *Method to create lines that are used in the Reports
	 */
        private String crtLINE(int P_strCNT,String P_strLINCHR)
        {
		String strln = "";
		try{
              for(int i=1;i<=P_strCNT;i++)    strln += P_strLINCHR;
		}catch(Exception L_EX){
			System.out.println("L_EX Error in Line:"+L_EX);
		}
                return strln;
	}
	
		
		
	/** One time data capturing for  VW_INTRN
	 * into the Vector
	 */
     private void crtINTRN_C1(ResultSet LP_RSSET)
    {
		//vtrINTRN_C1.clear();
		hstINTRN_C1.clear();
        try
        {
            while(true)
            {
                    //vtrINTRN_C1.addElement(getRSTVAL(LP_RSSET,"IN_CNSCD","C"));
                    hstINTRN_C1.put(getRSTVAL(LP_RSSET,"IN_KEY01","C")+getRSTVAL(LP_RSSET,"IN_KEY02","C"),getRSTVAL(LP_RSSET,"INT_INDQT","N"));
                    if (!LP_RSSET.next())
                            break;
            }
            LP_RSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtINTRN_C1");
        }
	}
		
		
	/** One time data capturing for  VW_INTRN
	 * into the Hash Table
	 */
     private void crtINTRN_C2(ResultSet LP_RSSET)
    {
		hstINTRN_C2.clear();
        try
        {
			cl_dat.exeSQLUPD("delete from MR_CPWORK","");
			if(cl_dat.exeDBCMT("crtINTRN_C2"))
				setMSG("Records cleared from MR_CPWORK",'N');
			dblINDQT1 =0; dblINDQT = 0; dblRSNVL=0; dblCPRVL=0;
			String L_strKEY01 = getRSTVAL(LP_RSSET,"IN_KEY01","C");
			String L_strKEY02 = getRSTVAL(LP_RSSET,"IN_KEY02","C");
			double L_dblINDQT_DET = 0;
			double L_dblRSNPR_DET = 0;
			double L_dblCPRPR_DET = 0;
			double L_dblNEFPR_DET = 0;
			double L_dblINDQT_GRP = 0;
			double L_dblRSNPR_GRP = 0;
			double L_dblCPRPR_GRP = 0;
			double L_dblNEFPR_GRP = 0;
            while(true)
            {
                    if(!getRSTVAL(LP_RSSET,"IN_KEY01","C").equals(L_strKEY01) || !getRSTVAL(LP_RSSET,"IN_KEY02","C").equals(L_strKEY02))
					{
						L_dblINDQT_GRP = Double.parseDouble(hstINTRN_C1.get(L_strKEY01+L_strKEY02).toString());
						putINTRN_C2(L_strKEY01,L_strKEY02,L_dblINDQT_GRP,L_dblRSNPR_GRP,L_dblCPRPR_GRP,L_dblNEFPR_GRP);
						L_strKEY01 = getRSTVAL(LP_RSSET,"IN_KEY01","C");
						L_strKEY02 = getRSTVAL(LP_RSSET,"IN_KEY02","C");
						L_dblINDQT_GRP =0; L_dblRSNPR_GRP =0; L_dblCPRPR_GRP = 0; L_dblNEFPR_GRP = 0;
					}
					L_dblRSNPR_DET  = (Double.parseDouble(getRSTVAL(LP_RSSET,"INT_RSNVL","N"))*Double.parseDouble(getRSTVAL(LP_RSSET,"INT_INDQT","N")));
					L_dblCPRPR_DET  = (Double.parseDouble(getRSTVAL(LP_RSSET,"INT_CPRVL","N"))*Double.parseDouble(getRSTVAL(LP_RSSET,"INT_INDQT","N")));
					L_dblNEFPR_DET =0;
					if (L_dblRSNPR_DET  < L_dblCPRPR_DET)
						L_dblNEFPR_DET = L_dblRSNPR_DET - L_dblCPRPR_DET;
					
					L_dblRSNPR_GRP += L_dblRSNPR_DET;
					L_dblCPRPR_GRP += L_dblCPRPR_DET;
					L_dblNEFPR_GRP += L_dblNEFPR_DET;
					
                    if (!LP_RSSET.next())
                            break;
            }
			//dblINDQT1 = Double.parseDouble(hstINTRN_C1.get(L_strKEY01+L_strKEY02).toString());
			L_dblINDQT_GRP = Double.parseDouble(hstINTRN_C1.get(L_strKEY01+L_strKEY02).toString());
			putINTRN_C2(L_strKEY01,L_strKEY02,L_dblINDQT_GRP,L_dblRSNPR_GRP,L_dblCPRPR_GRP,L_dblNEFPR_GRP);
            LP_RSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtINTRN_C2");
        }
	}
		

	 /** Adding distinct Selective Key Level Detail
	  * for Customer TabPane
	  */
	 private void putINTRN_C2(String LP_KEY01, String LP_KEY02,double LP_INDQT,double LP_RSNPR,double LP_CPRPR,double LP_NEFPR)
	 {
		 try
		 {
			String[] staINTRN = new String[intINTRN_TOT_C];
			staINTRN[intAE_INT_INDQT_C] = setNumberFormat(LP_INDQT,3);
			staINTRN[intAE_INT_RSNVL_C] = setNumberFormat(LP_RSNPR/LP_INDQT,0);
			staINTRN[intAE_INT_CPRVL_C] = setNumberFormat(LP_CPRPR/LP_INDQT,0);
			staINTRN[intAE_INT_DFFVL_C] = setNumberFormat((LP_RSNPR-LP_CPRPR)/LP_INDQT,0);
			staINTRN[intAE_INT_TOTVL_C] = setNumberFormat((LP_RSNPR-LP_CPRPR),0);
			staINTRN[intAE_INT_NEFVL_C] = setNumberFormat(LP_NEFPR,0);
			//System.out.println("Putting in hstINTRN_C2 : "+LP_KEY01+","+setNumberFormat(LP_CPRPR/LP_INDQT,0)+","+setNumberFormat(LP_RSNPR/LP_INDQT,0));
			hstINTRN_C2.put(LP_KEY01+LP_KEY02,staINTRN);
			cl_dat.exeSQLUPD("insert into MR_CPWORK (wr_cmpcd,wr_key01, wr_key02, wr_keyvl) values ('"+cl_dat.M_strCMPCD_pbst+"','"+LP_KEY01+"','"+LP_KEY02+"',"+setNumberFormat((LP_RSNPR-LP_CPRPR),0)+")","");
			if(cl_dat.exeDBCMT("putINTRN_C2"))
				setMSG(LP_KEY01+"/"+LP_KEY02+" Inserted",'N');
		 }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"putINTRN_C2");
        }
	 }
	 

	 
	 
	/** One time data capturing for  VW_INTRN
	 * into the Vector
	 */
     private void crtINTRN_G1(ResultSet LP_RSSET)
    {
		//vtrINTRN_G1.clear();
		hstINTRN_G1.clear();
        try
        {
            while(true)
            {
                    //vtrINTRN_G1.addElement(getRSTVAL(LP_RSSET,"INT_PRDCD","C"));
                    hstINTRN_G1.put(getRSTVAL(LP_RSSET,"INT_PRDCD","C"),getRSTVAL(LP_RSSET,"INT_INDQT","N"));
                    if (!LP_RSSET.next())
                            break;
            }
            LP_RSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtINTRN_G1");
        }
	}
		
		
	/** One time data capturing for  VW_INTRN
	 * into the Hash Table
	 */
     private void crtINTRN_G2(ResultSet LP_RSSET)
    {
		hstINTRN_G2.clear();
        try
        {
			cl_dat.exeSQLUPD("delete from MR_CPWORK","");
			if(cl_dat.exeDBCMT("crtINTRN_G2"))
				setMSG("Records cleared from MR_CPWORK",'N');
			dblINDQT1 =0; dblINDQT = 0; dblRSNVL =0; dblCPRVL = 0;
			String L_strPRDCD = getRSTVAL(LP_RSSET,"INT_PRDCD","C");
            while(true)
            {
                    if(!getRSTVAL(LP_RSSET,"INT_PRDCD","C").equals(L_strPRDCD))
					{
						dblINDQT1 = Double.parseDouble(hstINTRN_G1.get(L_strPRDCD).toString());
						putINTRN_G2(L_strPRDCD);
						L_strPRDCD = getRSTVAL(LP_RSSET,"INT_PRDCD","C");
						dblINDQT =0; dblRSNVL =0; dblCPRVL = 0;
					}
					dblRSNVL += (Double.parseDouble(getRSTVAL(LP_RSSET,"INT_RSNVL","N"))*Double.parseDouble(getRSTVAL(LP_RSSET,"INT_INDQT","N")));
					dblCPRVL += (Double.parseDouble(getRSTVAL(LP_RSSET,"INT_CPRVL","N"))*Double.parseDouble(getRSTVAL(LP_RSSET,"INT_INDQT","N")));
                    if (!LP_RSSET.next())
                            break;
            }
			dblINDQT1 = Double.parseDouble(hstINTRN_G1.get(L_strPRDCD).toString());
			putINTRN_G2(L_strPRDCD);
            LP_RSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtINTRN_G2");
        }
	}
	 

	 
	 /** Adding distinct Selective Key Level Detail
	  * for Grade Detail TabPane
	  */
	 private void putINTRN_G2(String LP_PRDCD)
	 {
		 try
		 {
				String[] staINTRN = new String[intINTRN_TOT_G];
				staINTRN[intAE_INT_INDQT_G] = setNumberFormat(dblINDQT1,3);
				staINTRN[intAE_INT_RSNVL_G] = setNumberFormat(dblRSNVL/dblINDQT1,0);
				staINTRN[intAE_INT_CPRVL_G] = setNumberFormat(dblCPRVL/dblINDQT1,0);
				staINTRN[intAE_INT_DFFVL_G] = setNumberFormat((dblRSNVL-dblCPRVL)/dblINDQT1,0);
				staINTRN[intAE_INT_TOTVL_G] = setNumberFormat(((dblRSNVL-dblCPRVL)/dblINDQT1)*dblINDQT1,0);
				hstINTRN_G2.put(LP_PRDCD,staINTRN);
				cl_dat.exeSQLUPD("insert into MR_CPWORK (wr_cmpcd,wr_key01,wr_keyvl) values ('"+cl_dat.M_strCMPCD_pbst+"','"+LP_PRDCD+"',"+setNumberFormat((dblRSNVL-dblCPRVL),0)+")","");
				if(cl_dat.exeDBCMT("crtINTRN_G2"))
					setMSG(LP_PRDCD+" Inserted",'N');
		 }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"putINTRN_G2");
        }
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

	 
	/** Validating party code
	 */
	private String getPRTNM(String LP_PRTTP, String LP_PRTCD)
	{
		String L_RETSTR = "";
		try
		{
			if(LP_PRTCD.length()>0)
			{
				ResultSet L_rstRSSET=cl_dat.exeSQLQRY2("Select PT_PRTNM from co_ptmst where pt_prttp = '"+LP_PRTTP+"' and pt_prtcd = '"+LP_PRTCD+"'");
					if(L_rstRSSET!=null && L_rstRSSET.next())
					{
						L_RETSTR = getRSTVAL(L_rstRSSET,"PT_PRTNM","C");
						L_rstRSSET.close();
					}
			}
		}
		catch (Exception L_EX)
		{setMSG(L_EX,"getPRTNM");}
		return L_RETSTR;
	}


	
/** Populating values in Combo Box from Vector
 */
private void setCMBVL(JComboBox LP_CMBNM, Vector<String> LP_VTRNM)
{
		LP_CMBNM.removeActionListener(this);
		LP_CMBNM.removeAllItems();
		for(int i=0;i<LP_VTRNM.size(); i++)
        {
			
				//System.out.println(i+"  : "+LP_VTRNM.get(i).toString());
                LP_CMBNM.addItem(LP_VTRNM.get(i).toString());
        }
		LP_CMBNM.addActionListener(this);
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
					{System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM); return "";}
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
			System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
		}
        return "";
        }
	

	
		/** Picking up Specified Invoice Transaction related details from Hash Table
		 * <B> for Specified Inv.No.
		 * @param LP_INTRN_KEY	Inv.No.
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getINTRN_C2(String LP_INTRN_KEY, String LP_FLDNM)
        {
		//System.out.println("getINTRN_C2 : "+LP_INTRN_KEY+"/"+LP_FLDNM);
        try
        {
				if(!hstINTRN_C2.containsKey(LP_INTRN_KEY))
					return "0";
                if (LP_FLDNM.equals("INT_INDQT"))
                        return ((String[])hstINTRN_C2.get(LP_INTRN_KEY))[intAE_INT_INDQT_C];
                else if (LP_FLDNM.equals("INT_RSNVL"))
                        return ((String[])hstINTRN_C2.get(LP_INTRN_KEY))[intAE_INT_RSNVL_C];
                else if (LP_FLDNM.equals("INT_CPRVL"))
                        return ((String[])hstINTRN_C2.get(LP_INTRN_KEY))[intAE_INT_CPRVL_C];
                else if (LP_FLDNM.equals("INT_DFFVL"))
                        return ((String[])hstINTRN_C2.get(LP_INTRN_KEY))[intAE_INT_DFFVL_C];
                else if (LP_FLDNM.equals("INT_TOTVL"))
                        return ((String[])hstINTRN_C2.get(LP_INTRN_KEY))[intAE_INT_TOTVL_C];
                else if (LP_FLDNM.equals("INT_NEFVL"))
                        return ((String[])hstINTRN_C2.get(LP_INTRN_KEY))[intAE_INT_NEFVL_C];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getINTRN_C2");
			System.out.println("getINTRN_C2 : "+LP_INTRN_KEY+"/"+LP_FLDNM);
		}
        return "";
        }
		

		
		/** Picking up Specified Invoice Transaction related details from Hash Table
		 * <B> for Specified Inv.No.
		 * @param LP_INTRN_KEY	Inv.No.
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getINTRN_G2(String LP_INTRN_KEY, String LP_FLDNM)
        {
		//System.out.println("getINTRN_G2 : "+LP_INTRN_KEY+"/"+LP_FLDNM);
        try
        {
                if (LP_FLDNM.equals("INT_INDQT"))
                        return ((String[])hstINTRN_G2.get(LP_INTRN_KEY))[intAE_INT_INDQT_G];
                else if (LP_FLDNM.equals("INT_RSNVL"))
                        return ((String[])hstINTRN_G2.get(LP_INTRN_KEY))[intAE_INT_RSNVL_G];
                else if (LP_FLDNM.equals("INT_CPRVL"))
                        return ((String[])hstINTRN_G2.get(LP_INTRN_KEY))[intAE_INT_CPRVL_G];
                else if (LP_FLDNM.equals("INT_DFFVL"))
                        return ((String[])hstINTRN_G2.get(LP_INTRN_KEY))[intAE_INT_DFFVL_G];
                else if (LP_FLDNM.equals("INT_TOTVL"))
                        return ((String[])hstINTRN_G2.get(LP_INTRN_KEY))[intAE_INT_TOTVL_G];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getINTRN_G2");
			System.out.println("getINTRN_G2 : "+LP_INTRN_KEY+"/"+LP_FLDNM);
		}
        return "";
        }
		
		
		
	/**
	 */
	private   String padNUMBER(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)
	{
		String P_strTRNVL = "";
		try
		{
			
			if(P_strSTRVL.length()==0)
				P_strSTRVL = "-";
			else if(Double.parseDouble(P_strSTRVL)==0)
				P_strSTRVL = "-";
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			if(P_intPADLN <= L_STRLN)
			{
				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			int L_STRDF = P_intPADLN - L_STRLN;
			
			StringBuffer L_STRBUF;
			switch(P_chrPADTP)
			{
				case 'C':
					L_STRDF = L_STRDF / 2;
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
					break;
				case 'R':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
					{
						if(M_rdbTEXT.isSelected())
						L_STRBUF.insert(j,' ');
						else
							L_STRBUF.insert(j,"\t");
					}
					P_strTRNVL =  P_strSTRVL+L_STRBUF ;
					break;
				case 'L':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL ;
					break;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"padNUMBER");
		}
		return P_strTRNVL;
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

	/** Checking key in table for record existance
	 */
	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
	{
		boolean L_flgCHKFL = false;
		try
		{
			M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
			//System.out.println(M_strSQLQRY);
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

	
}	