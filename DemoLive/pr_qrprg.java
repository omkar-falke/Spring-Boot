//Additive Filler Typewise Realisation Query 
/** Purpose  : 1. Product Register based on various parameters invoved in product code
 * Author    : SRD
 * Impl.Date : 
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

class pr_qrprg extends cl_rbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;
	private JPanel pnlCNSRPT,pnlINDRPT;
	private String strPRMCD,strPRTTP,strPRTCD,strWHRSTR, strWHRSTR1;	//strSTRDT,strENDDT,
	private String strPSCSTR, strPCCSTR, strSBSSTR;
	private String strRESSTR = cl_dat.M_strREPSTR_pbst+"pr_qrprg.doc"; 
	private String strCUTDT = ""; 

	
	/** Labels for Various Options
	 */
	private JLabel lblPMR;
	private JLabel lblPCT;
	private JLabel lblPSC;
	private JLabel lblPAF;
	private JLabel lblPPD;
	private JLabel lblPPT;
	private JLabel lblPCC;
	private JLabel lblPCS;
	private JLabel lblPRM;
	private JLabel lblPRS;
	private JLabel lblREMDS;
	
	/** Text fields for various options
	 */
	private JTextField txtPMR;
	private JTextField txtPCT;
	private JTextField txtPSC;
	private JTextField txtPAF;
	private JTextField txtPPD;
	private JTextField txtPPT;
	private JTextField txtPPC;
	private JTextField txtPCS;
	private JTextField txtPRM;
	private JTextField txtPRS;
	private JTextField txtREMDS;
	//private JTextField txtRECCT;
	private JCheckBox chkCFWQT;

	
	private JList lstOPTNM;			// JList for displaying details of options selected
	private Vector vtrOPTNM;		// Vector for poulating data into the JList
	private cl_JTable tblCNSRPT,tblINDRPT;   // JTable for displaying Selective, Additive Filler Type & Customer order detail
	private cl_JTable tblCNSTOT,tblINDTOT;   // JTable for displaying Selective, Additive Filler Type & Customer order detail
	private boolean flgCHK_EXIST;			// Flag to check whether record exists in table
	//private boolean flgIVT_RLSN = false;	// Flag for Invoice Based Realisation
	
	
	private JRadioButton rdbBASE_BAG;	
	private JRadioButton rdbBASE_STK;	
	private JRadioButton rdbBASE_DSP;	
	private JRadioButton rdbBASE_BKG;	
	private JRadioButton rdbBASE_OUT;	

	
	private JButton btnPRINT_CNS,btnPRINT_IND;

	/** String for creating Code Transaction detail hashtable
	 */
	private String strCGMTP;
	private String strCGSTP;
	private String strCODCD;

	private String strHDRCT;
	private String strHDRCD;

	
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
	

	private int intCDWRK_TOT = 1;
    private int intAE_CD_HDRDS = 1;		
	
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
	
	private int intTB2_CHKFL = 0;
	private int intTB2_PRDDS = 1;
	private int intTB2_INDQT = 2;
	private int intTB2_RSNVL = 3;
	private int intTB2_PRDCD = 4;

	private int intTB3_CHKFL = 0;
	private int intTB3_PRDDS = 1;
	private int intTB3_INDNO = 2;
	private int intTB3_INDDT = 3;
	private int intTB3_INDQT = 4;
	private int intTB3_RSNVL = 5;
	private int intTB3_INDVL = 6;
	private int intTB3_CCLVL = 7;
	private int intTB3_COMVL = 8;
	private int intTB3_FRTVL = 9;
	private int intTB3_CRDVL = 10;
	private int intTB3_CNSNM = 11;
	
	
	private int intINTRN_TOT = 7;
	private int intAE_IN_BYRCD = 0;
	private int intAE_IN_DSRCD = 1;
	private int intAE_IN_INDNO = 2;
	private int intAE_INT_PRDCD = 3;
	private int intAE_INT_PRDDS = 4;
	private int intAE_INT_INDQT = 5;
	private int intAE_INT_RSNVL = 6;

	private int intINTRN_TOT_C = 2;
	private int intAE_INT_INDQT_C = 0;
	private int intAE_INT_RSNVL_C = 1;
	
	private int intINTRN_TOT_I = 2;
	private int intAE_INT_CFWQT_I = 0;
	private int intAE_INT_CFWPR_I = 1;
	
	private double dblINDQT1 =0;
	private double dblINDQT = 0;
	private double dblRSNVL = 0;
	private double dblCFWQT = 0;
	private double dblCFWPR = 0;
	private double dblRSNPR = 0;
	
	private String strWHRINT_PMR = "";
	private String strWHRINT_PCT = "";
	private String strWHRINT_PSC = "";
	private String strWHRINT_PAF = "";
	private String strWHRINT_PPD = "";
	private String strWHRINT_PPT = "";
	private String strWHRINT_PCC = "";
	private String strWHRINT_PCS = "";
	private String strWHRINT_PRM = "";
	private String strWHRINT_PRS = "";

	private String strWHRIVT_PMR = "";
	private String strWHRIVT_PCT = "";
	private String strWHRIVT_PSC = "";
	private String strWHRIVT_PAF = "";
	private String strWHRIVT_PPD = "";
	private String strWHRIVT_PPT = "";
	private String strWHRIVT_PCC = "";
	private String strWHRIVT_PCS = "";
	private String strWHRIVT_PRM = "";
	private String strWHRIVT_PRS = "";
	
	
	private String strALLWS = "Overall";
	private String strPMRWS = "Base Polymer";
	private String strPCTWS = "Product category";
	private String strPSCWS = "Product Sub-category";
	private String strPAFWS = "Additive Filletr type";
	private String strPPDWS = "Desired component %";
	private String strPPTWS = "Total Additive %";
	private String strPCCWS = "Colour Code";
	private String strPCSWS = "Colour Srl. No.";	
	private String strPRMWS = "Ref.Main Code";	
	private String strPRSWS = "Ref.Sub code";	

	private String strDFTSQ = "Default";		
	private String strPAFSQ = "Additive Filler Type";		
	private String strPPDSQ = "Desired component %";		
	private String strPPTSQ = "Total Additive %";		
	private String strINOSQ = "Invoice No.";		

	
	private String strKEY01_INT = "";
	private String strKEY01_IVT = "";
	private String strKEY02_INT = "";
	private String strKEY02_IVT = "";
	
	
	private String[] arrCNSHDR = new String[]{"0","1","2","3","4"};
	private String[] arrCNSHDT = new String[]{"0","1","2","3","4"};
	private int[] arrCNSHDR_WD = new int[]{0,1,2,3,4};
	private char[] arrCNSHDR_PAD = new char[]{'0','1','2','3','4'};
	
	
	private String[] arrINDHDR = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11"};
	private String[] arrINDHDT = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11"};
	private int[] arrINDHDR_WD = new int[]{0,1,2,3,4,5,6,7,8,9,10,11};
	private char[] arrINDHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8','9','0','1'};

	
	
	private JComboBox cmbPRINTERS;
	
	
	private	int intDSRCTR = 0, intBYRCTR = 0, intBLKCTR = 0; 
	//private double[] arrCOLTOT;
	private double dblDSRQT = 0.000, dblBYRQT = 0.000, dblTOTQT = 0.000;
	private double dblDSRVL = 0.000, dblBYRVL = 0.000;

	//private String strQTFLD_INT, strVLFLD_INT;
	//private String  strPRSCD_OLD = "", strPPTCD_OLD="", strPRMCD_OLD ="";

	private double dblINDQT_TOT=0, dblRSNPR_TOT=0;
	
	private int intCNSWD = 130;      // report width
	private int intCNSCL = 4;		 // No.of columns 
	private int intINDWD = 145;      // report width
	private int intINDCL = 11;		 // No.of columns in the report line

	private int intLINECT=72, intPAGENO=0, intRUNCL=0;
	//boolean flgCNSCD_NEW = true, flgBYRCD_NEW = true, flgPRDCD_NEW = true, flgEOFCHK = false;
	boolean flgEOFCHK = false;

	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	
	private JLabel lblRPTWS;		
	private JLabel lblKEY01;		
	private JLabel lblKEY02;		
	private Vector vtrRPTWS;		/**Vector for adding elements to cmbRPTWS */
	private JComboBox cmbRPTWS;		/**Combo-box for defining scope of the report */
	private Vector vtrKEY01;		/**Vector for adding elements to cmbKEY01 */
	private Vector vtrKEY02;		/**Vector for adding elements to cmbKEY02 */

	private JLabel lblMKTTP;		
	private Vector vtrMKTTP;		/**Vector for adding elements to cmbMKTTP */
	private JComboBox cmbMKTTP;
	private JComboBox cmbKEY01;		/**Combo-box for defining First Key column */
	private JComboBox cmbKEY02;		/**Combo-box for defining First Key column */
	
	private Hashtable hstOPTNM;
	private Hashtable hstCDTRN;			// Code Transaction details
	private Hashtable hstCDWRK;			// Product code Header Details
	private Vector vtrINTRN01_C2;			// Vector deciding the distict key value for first tab pane
	private Vector vtrINTRN02_C2;			// Parallel vector for second key 
	private Hashtable hstINTRN_C2;			// Selective details hash table

	private Vector vtrINTRN_G2;			// Additive Filler Type list for 
	private Hashtable hstINTRN_G1;			// Additive Filler Type list with Tot.Qty for 
	private Hashtable hstINTRN_G2;			// Additive Filler Type details for 

	
	private Hashtable hstCODDS;			// Code Description
	private Hashtable hstPRMST;			// Product Master details
	private Hashtable hstPTMST;			// Party Details
	private Hashtable hstLOTNO;			// Party Details
	private Hashtable hstDSRNM;			// Distibutor Short Name (as key) & Code (as value)
	private Vector vtrDSRNM;			// Distributor Code & Short Name
	private Object[] arrHSTKEY;			// Object array for getting hash table key in sorted order
	
	pr_qrprg()
	{
		super(2);
		try
		{

			arrCNSHDR[intTB1_CHKFL] = "xx";
			arrCNSHDR[intTB1_KEY01] = "Key01";
			arrCNSHDR[intTB1_KEY02] = "Key02";
			arrCNSHDR[intTB1_INDQT] = "Qty";
			arrCNSHDR[intTB1_RSNVL] = "Realsn.";

			arrCNSHDR_WD[intTB1_CHKFL] = 2;
			arrCNSHDR_WD[intTB1_KEY01] = (80*2)+(8*1);
			arrCNSHDR_WD[intTB1_KEY02] = 80;
			arrCNSHDR_WD[intTB1_INDQT] = 90;
			arrCNSHDR_WD[intTB1_RSNVL] = 90;
			
			arrCNSHDR_PAD[intTB1_CHKFL] = 'R';
			arrCNSHDR_PAD[intTB1_KEY01] = 'R';
			arrCNSHDR_PAD[intTB1_KEY02] = 'R';
			arrCNSHDR_PAD[intTB1_INDQT] = 'L';
			arrCNSHDR_PAD[intTB1_RSNVL] = 'L';

			
			
			arrINDHDR[intTB3_CHKFL] = "xx";
			arrINDHDR[intTB3_PRDDS] = "Additive Filler Type";
			arrINDHDR[intTB3_INDNO] = "Doc.No.";
			arrINDHDR[intTB3_INDDT] = "Date";
			arrINDHDR[intTB3_INDQT] = "Qty";
			arrINDHDR[intTB3_RSNVL] = "Realsn.";
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
			arrINDHDR_PAD[intTB3_INDVL] = 'L';
			arrINDHDR_PAD[intTB3_CCLVL] = 'L';
			arrINDHDR_PAD[intTB3_COMVL] = 'L';
			arrINDHDR_PAD[intTB3_FRTVL] = 'L';
			arrINDHDR_PAD[intTB3_CRDVL] = 'L';
			arrINDHDR_PAD[intTB3_CNSNM] = 'L';

			
			vtrOPTNM = new Vector();
			lstOPTNM = new JList();
			
			
			tbpMAIN = new JTabbedPane();
			pnlCNSRPT = new JPanel(null);
			pnlINDRPT = new JPanel(null);
			//txtSPCCT = new JTextField();

			txtPMR = new JTextField();
			txtPCT = new JTextField();
			txtPSC = new JTextField();
			txtPAF = new JTextField();
			txtPPD = new JTextField();
			txtPPT = new JTextField();
			txtPPC = new JTextField();
			txtPCS = new JTextField();
			txtPRM = new JTextField();
			txtPRS = new JTextField();
			txtREMDS  = new JTextField();
			//txtRECCT  = new JTextField();

			lblPMR = new JLabel("Base Polymer Type");
			lblPCT = new JLabel("Product Category");
			lblPSC = new JLabel("Product Sub-Category");
			lblPAF = new JLabel("Additive Filler Type");
			lblPPD = new JLabel("Desired component %.");
			lblPPT = new JLabel("Total Additive %");
			lblPCC = new JLabel("Colour Code");
			lblPCS = new JLabel("Colour Srl.No.");
			lblPRM = new JLabel("Ref.Main code");
			lblPRS = new JLabel("Ref.Sub code");
			lblREMDS = new JLabel("Remark");
			
			
			vtrINTRN01_C2 = new Vector();
			vtrINTRN02_C2 = new Vector();
			vtrINTRN_G2 = new Vector();
			hstOPTNM = new Hashtable();
			hstCDTRN = new Hashtable();
			hstCDWRK = new Hashtable();
			hstINTRN_C2 = new Hashtable();
			hstCODDS = new Hashtable();
			hstPRMST = new Hashtable();
			hstPTMST = new Hashtable();
			hstLOTNO = new Hashtable();
			hstDSRNM = new Hashtable();
			
			vtrRPTWS = new Vector();
			vtrMKTTP = new Vector();
			vtrKEY01 = new Vector();
			vtrKEY02 = new Vector();
			
			lblRPTWS  = new JLabel("Scope");
			lblKEY01  = new JLabel("Key Col 1:");
			lblKEY02  = new JLabel("Key Col 2:");
			lblMKTTP  = new JLabel("Market Type");

			lblRPTWS.setForeground(Color.blue);
			lblKEY01.setForeground(Color.blue);
			lblKEY02.setForeground(Color.blue);
			lblMKTTP.setForeground(Color.blue);


			btnPRINT_CNS = new JButton("Selective.)");
			btnPRINT_IND = new JButton("Document");
			

			crtPRMST();
			hstCDTRN.clear();
			hstCDWRK.clear();
			crtCDTRN("'MSTPRXXPMR','MSTPRXXPCT','MSTPRXXCLR','MSTPRXXQFL', 'MSTPRXXEXC'","",hstCDTRN);
			crtCDWRK("'PC','CS'");
			
			setMatrix(20,8);
			
			add(lblRPTWS,1,2,1,2,this,'L');
			add(cmbRPTWS=new JComboBox(),2,2,1,2,this,'L');
			
			add(lblKEY01,4,1,1,1,this,'L');
			add(lblKEY02,4,2,1,1,this,'L');
			add(cmbKEY01=new JComboBox(),5,1,1,1,this,'L');
			add(cmbKEY02=new JComboBox(),5,2,1,1,this,'L');

			add(lblMKTTP,1,1,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),2,1,1,1,this,'L');



			add(lblPMR,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPMR,intSCT_ROW,intSCT_COL+1,1,1,this,'L');

			add(lblPCT,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPCT,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPSC,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPSC,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPAF,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPAF,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPPD,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPPD,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPPT,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPPT,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPCC,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPPC,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPCS,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPCS,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPRM,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPRM,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
			add(lblPRS,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtPRS,intSCT_ROW,intSCT_COL+1,1,1,this,'L');

			add(new JScrollPane(lstOPTNM),intSCT_ROW,intSCT_COL+2,3,4,this,'L');

			//add(new JLabel("Top"),intSCT_ROW+1,intSCT_COL+4,1,0.5,this,'L');
			//add(txtRECCT,intSCT_ROW+2,intSCT_COL+4,1,0.5,this,'L');
			

			add(rdbBASE_BAG=new JRadioButton("Bagging"),intSCT_ROW-1,intSCT_COL+8,1,2,this,'R');
			add(rdbBASE_STK=new JRadioButton("Stock"),intSCT_ROW,intSCT_COL+8,1,2,this,'R');
			add(rdbBASE_DSP=new JRadioButton("Despatch"),intSCT_ROW+1,intSCT_COL+8,1,2,this,'R');
			add(rdbBASE_BKG=new JRadioButton("Booking"),intSCT_ROW+2,intSCT_COL+8,1,2,this,'R');
			add(rdbBASE_OUT=new JRadioButton("Outstanding"),intSCT_ROW+3,intSCT_COL+8,1,2,this,'R');
			ButtonGroup btg_BASE=new ButtonGroup();
				btg_BASE.add(rdbBASE_BAG);
				btg_BASE.add(rdbBASE_STK);
				btg_BASE.add(rdbBASE_DSP);
				btg_BASE.add(rdbBASE_BKG);
				btg_BASE.add(rdbBASE_OUT);
			
			
			//add(chkSTOTFL,2,7,1,2,this,'L');
			add(new JLabel("Print"),6,6,1,0.5,this,'L');
			add(btnPRINT_CNS,7,6,1,1,this,'L');
			add(btnPRINT_IND,7,7,1,1,this,'L');
			
			chkCFWQT = new JCheckBox("C/F Effect");
			add(chkCFWQT,18,7,1,2,this,'L');
			
			
			tblCNSRPT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,500,0,1,7,7.9,arrCNSHDR_WD,new int[]{0});  
			tblCNSTOT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,1,7,1,2.1,7.9,arrCNSHDR_WD,new int[]{0});  


			tblINDRPT = crtTBLPNL1(pnlINDRPT,arrINDHDR,500,0,1,7,7.9,arrINDHDR_WD,new int[]{0});  
			tblINDTOT = crtTBLPNL1(pnlINDRPT,arrINDHDR,1,7,1,2.1,7.9,arrINDHDR_WD,new int[]{0});  
			
			tbpMAIN.addTab("Selective Detail",pnlCNSRPT);
			tbpMAIN.addTab("Document Detail",pnlINDRPT);
			
			add(tbpMAIN,7,1,13,8,this,'L');
			tblCNSRPT.addMouseListener(this);
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
			{setMSG(L_EX,"pr_qrprg");}
	}


	/** Disabling & Initializing the components
	 */	
	private void exeOPT_DSB()
	{
		try
		{
			exeOPT_DSB1();
			txtPMR.setText("");
			txtPCT.setText("");
			txtPSC.setText("");
			txtPAF.setText("");
			txtPPD.setText("");
			txtPPT.setText("");
			txtPPC.setText("");
			txtPCS.setText("");
			txtPRM.setText("");
			txtPRS.setText("");
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
			lblPMR.setVisible(false);
			lblPCT.setVisible(false);
			lblPSC.setVisible(false);
			lblPAF.setVisible(false);
			lblPPD.setVisible(false);
			lblPPT.setVisible(false);
			lblPCC.setVisible(false);
			lblPCS.setVisible(false);
			lblPRM.setVisible(false);
			lblPRS.setVisible(false);

			 txtPMR.setVisible(false);
			 txtPCT.setVisible(false);
			 txtPSC.setVisible(false);
			 txtPAF.setVisible(false);
			 txtPPD.setVisible(false);
			 txtPPT.setVisible(false);
			 txtPPC.setVisible(false);
			 txtPCS.setVisible(false);
			 txtPRM.setVisible(false);
			 txtPRS.setVisible(false);
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
			vtrRPTWS.addElement(strPMRWS);		
			vtrRPTWS.addElement(strPCTWS);		
			vtrRPTWS.addElement(strPSCWS);		
			vtrRPTWS.addElement(strPAFWS);		
			vtrRPTWS.addElement(strPPDWS);		
			vtrRPTWS.addElement(strPPTWS);		
			vtrRPTWS.addElement(strPCCWS);		
			vtrRPTWS.addElement(strPCSWS);		
			vtrRPTWS.addElement(strPRMWS);		
			vtrRPTWS.addElement(strPRSWS);		
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
			vtrKEY01.addElement(strPPDWS);		
			vtrKEY01.addElement(strPPTWS);		
			vtrKEY01.addElement(strPRMWS);		
			vtrKEY01.addElement(strPSCWS);		
			vtrKEY01.addElement(strPCCWS);		
			vtrKEY01.addElement(strPAFWS);		
			vtrKEY01.addElement(strPRMWS);		
			vtrKEY01.addElement(strPCSWS);		
			vtrKEY01.addElement(strPMRWS);		
			vtrKEY01.addElement(strPCTWS);		
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
			vtrKEY02.addElement(strPRSWS);		
			vtrKEY02.addElement(strPPTWS);		
			vtrKEY02.addElement(strPPDWS);		
			vtrKEY02.addElement(strPSCWS);		
			vtrKEY02.addElement(strPCCWS);		
			vtrKEY02.addElement(strPAFWS);		
			vtrKEY02.addElement(strPRMWS);		
			vtrKEY02.addElement(strPCSWS);		
			vtrKEY02.addElement(strPMRWS);		
			vtrKEY02.addElement(strPCTWS);		
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
			//txtRECCT.setText("0");
			rdbBASE_BAG.setSelected(false);
			rdbBASE_STK.setSelected(true);
			rdbBASE_DSP.setSelected(false);
			rdbBASE_BKG.setSelected(false);
			rdbBASE_OUT.setSelected(false);
			chkCFWQT.setSelected(true);

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
		
		if(cmbKEY01.getSelectedItem().toString().equals(strPRSWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Reference Sub code";
			strKEY01_INT = "in_cnscd";
			strKEY01_IVT = "ivt_cnscd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPPTWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Total Additive %";
			strKEY01_INT = "in_byrcd";
			strKEY01_IVT = "ivt_byrcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPPDWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Desired component %";
			strKEY01_INT = "in_dsrcd";
			strKEY01_IVT = "ivt_dsrcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPSCWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Product sub-category";
			strKEY01_INT = "in_zoncd";
			strKEY01_IVT = "ivt_zoncd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPCCWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Colour Code";
			strKEY01_INT = "in_saltp";
			strKEY01_IVT = "ivt_saltp";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPAFWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Additive Filler Type";
			strKEY01_INT = "int_prdcd";
			strKEY01_IVT = "ivt_prdcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPRMWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Reference Main Code";
			strKEY01_INT = "substr(int_prdcd,1,4)";
			strKEY01_IVT = "substr(ivt_prdcd,1,4)";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPCSWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Colour Srl. Number";
			strKEY01_INT = "in_pmtcd";
			strKEY01_IVT = "ivt_pmtcd";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPMRWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Base Polymer Type";
			strKEY01_INT = "cmt_chp02";
			strKEY01_IVT = "cmt_chp02";
		}
		else if(cmbKEY01.getSelectedItem().toString().equals(strPCTWS))
		{
			arrCNSHDR[intTB1_KEY01] = "Product Category";
			strKEY01_INT = "cmt_modls";
			strKEY01_IVT = "cmt_modls";
		}
	}
	
	
	/** Setting values after selecting Key Field column
	 */
	private void setKEY02()
	{
		
		if(cmbKEY02.getSelectedItem().toString().equals(strPRSWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Customer";
			strKEY02_INT = "in_cnscd";
			strKEY02_IVT = "ivt_cnscd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPPTWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Total Additive %";
			strKEY02_INT = "in_byrcd";
			strKEY02_IVT = "ivt_byrcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPPDWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Desired component %";
			strKEY02_INT = "in_dsrcd";
			strKEY02_IVT = "ivt_dsrcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPSCWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Product Sub-Category";
			strKEY02_INT = "in_zoncd";
			strKEY02_IVT = "ivt_zoncd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPCCWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Colour Code";
			strKEY02_INT = "in_saltp";
			strKEY02_IVT = "ivt_saltp";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPAFWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Additive Filler Type";
			strKEY02_INT = "int_prdcd";
			strKEY02_IVT = "ivt_prdcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPRMWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Ref.Main Code";
			strKEY02_INT = "substr(int_prdcd,1,4)";
			strKEY02_IVT = "substr(ivt_prdcd,1,4)";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPCSWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Paymet Type";
			strKEY02_INT = "in_pmtcd";
			strKEY02_IVT = "ivt_pmtcd";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPMRWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Base Polymer Type";
			strKEY02_INT = "cmt_chp02";
			strKEY02_IVT = "cmt_chp02";
		}
		else if(cmbKEY02.getSelectedItem().toString().equals(strPCTWS))
		{
			arrCNSHDR[intTB1_KEY02] = "Product Category";
			strKEY02_INT = "cmt_modls";
			strKEY02_IVT = "cmt_modls";
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
				setCMBVL(cmbKEY01,vtrKEY01);
				setCMBVL(cmbKEY02,vtrKEY02);
				setCMBVL(cmbRPTWS,vtrRPTWS);
				setCMBVL(cmbMKTTP,vtrMKTTP);
				crtPTMST();
			}

			if(M_txtFMDAT.getText().length()<10)
				M_txtFMDAT.setText("01/04/2004");
			if(M_txtTODAT.getText().length()<10)
				M_txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
			strPSCSTR = setSBSSTR("PSC");
			strPCCSTR = setSBSSTR("PCC");
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
			if(M_objSOURC == btnPRINT_IND)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_ingrr.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtINDRPT();
					exePRINT1();
				}
			//if(M_objSOURC == rdbBASE_BAG || M_objSOURC == rdbBASE_STK)
			//{
			//	flgIVT_RLSN = true;
			//	if(rdbBASE_BAG.isSelected() == true)
			//		flgIVT_RLSN = false;
			//	crtPTMST();
			//}
		}
		catch (Exception L_EX) {setMSG(L_EX,"acionPerformed");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
		
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
				if(L_FE.getSource() == txtPMR && txtPMR.getText().length()>0)
					{setOPTNM("PMR");getSUBHDR1();}
				else if(L_FE.getSource() == txtPCT && txtPCT.getText().length()>0)
					{setOPTNM("PCT");getSUBHDR1();}
				else if(L_FE.getSource() == txtPSC && txtPSC.getText().length()>0)
					{setOPTNM("PSC");getSUBHDR1();}
				else if(L_FE.getSource() == txtPAF && txtPAF.getText().length()>0)
					{setOPTNM("PAF");getSUBHDR1();}
				else if(L_FE.getSource() == txtPPD && txtPPD.getText().length()>0)
					{setOPTNM("PPD"); getSUBHDR1();}
				else if(L_FE.getSource() == txtPPT && txtPPT.getText().length()>0)
					{setOPTNM("PPT"); getSUBHDR1();}
				else if(L_FE.getSource() == txtPPC && txtPPC.getText().length()>0)
					{setOPTNM("PCC");  getSUBHDR1();}
				else if(L_FE.getSource() == txtPCS && txtPCS.getText().length()>0)
					{setOPTNM("PCS");  getSUBHDR1();}
				else if(L_FE.getSource() == txtPRM && txtPRM.getText().length()>0)
					{setOPTNM("PRM");  getSUBHDR1();}
				else if(L_FE.getSource() == txtPRS && txtPRS.getText().length()>0)
					{setOPTNM("PRS");  getSUBHDR1();}
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
	
	
	
	
	

	
	/** Accepts option category (LP_OPTCT) as a parameter
	 *  Recording selected options in vector & populating in JList
	 *  for displaying selected categories
	 */
	public void setOPTNM(String LP_OPTCT)
	{
		try
		{
			{
				if(LP_OPTCT.equals("PMR"))
					{vtrOPTNM.addElement(lblPMR.getText()+": "+getCDTRN("MSTPRXXPMR"+txtPMR.getText(),"CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("PCT"))
					{vtrOPTNM.addElement(lblPCT.getText()+": "+getCDTRN("MSTPRXXPCT"+txtPCT.getText(),"CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("PSC"))
					{vtrOPTNM.addElement(lblPSC.getText()+": "+getCDWRK("PC"+txtPCT.getText()+txtPSC.getText()+"0A","CD_HDRDS"));}
				else if(LP_OPTCT.equals("PAF"))
					{vtrOPTNM.addElement(lblPAF.getText()+": "+getCDWRK("PC"+txtPCT.getText()+txtPSC.getText()+txtPAF.getText(),"CD_HDRDS"));}
				else if(LP_OPTCT.equals("PPD"))
					{vtrOPTNM.addElement(lblPPD.getText()+": "+txtPPD.getText()+"%");}
				else if(LP_OPTCT.equals("PPT"))
					{vtrOPTNM.addElement(lblPPT.getText()+": "+txtPPT.getText()+"%");}
				else if(LP_OPTCT.equals("PCC"))
					{vtrOPTNM.addElement(lblPCC.getText()+": "+getCDTRN("MSTPRXXCLR"+txtPPC.getText(),"CMT_CODDS",hstCDTRN));}
				else if(LP_OPTCT.equals("PCS"))
					{vtrOPTNM.addElement(lblPCS.getText()+": "+getCDWRK("CS"+txtPCS.getText()+txtPCS.getText(),"CD_HDRDS"));}
				else if(LP_OPTCT.equals("PRM"))
					{vtrOPTNM.addElement(lblPRM.getText()+": "+getPRCAT("PRM",txtPRM.getText()));}
				else if(LP_OPTCT.equals("PRS"))
					{vtrOPTNM.addElement(lblPRS.getText()+": "+getPRCAT("PRS",txtPRM.getText()+txtPRS.getText()));}
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
	}
	*/
	
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
		if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				//M_strHLPFLD = "txtSPCCT";
				if(cmbRPTWS.getSelectedItem().toString().equals(strPMRWS))
					{M_strHLPFLD = "txtPMR"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='PRXXPMR'  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPCTWS))
					{M_strHLPFLD = "txtPCT"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='PRXXPCT'  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPSCWS))
					{M_strHLPFLD = "txtPSC"; cl_hlp("Select substr(CD_HDRCD,1,4) CD_HDRCD, CD_HDRDS from PR_CDWRK where CD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  CD_HDRCT='PC' and substr(CD_HDRCD,5,2) = '0A' order by CD_HDRCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPAFWS))
					{M_strHLPFLD = "txtPAF"; cl_hlp("Select CD_HDRCD, CD_HDRDS from PR_CDWRK where CD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CD_HDRCT = 'PC' and substr(CD_HDRCD,5,2) <> '0A' order by CD_HDRCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPPDWS))
					{M_strHLPFLD = "txtPPD"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp = 'MST' and cmt_cgstp = 'PRXXPPD' order by cmt_codcd order by CMT_CODCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPPTWS))
					{M_strHLPFLD = "txtPPT"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp = 'MST' and cmt_cgstp = 'PRXXPPT' order by cmt_codcd order by CMT_CODCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPCCWS))
					{M_strHLPFLD = "txtPPC"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='PRXXCLR'  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPCSWS))
					{M_strHLPFLD = "txtPCS"; cl_hlp("Select CD_HDRCD, CD_HDRDS from PR_CDWRK where CD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CD_HDRCT = 'CS'  order by CD_HDRCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRMWS))
					{M_strHLPFLD = "txtPRM"; cl_hlp("Select substr(PR_PRDCD,5,2) PR_PRDCD, min(PR_PRDDS) PR_PRDDS from CO_PRMST  group by substr(PR_PRDCD,5,2) order by PR_PRDCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRSWS))
					{M_strHLPFLD = "txtPRS"; cl_hlp("Select substr(PR_PRDCD,5,4) PR_PRDCD, min(PR_PRDDS) PR_PRDDS from CO_PRMST  group by substr(PR_PRDCD,5,4) order by PR_PRDCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
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
			if(M_strHLPFLD.equals("txtPMR"))
				{txtPMR.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPCT"))
				{txtPCT.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPSC"))
				{txtPSC.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPAF"))
				{txtPAF.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPPD"))
				{txtPPD.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPPT"))
				{txtPPT.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPPC"))
				{txtPPC.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPCS"))
				{txtPCS.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPRM"))
				{txtPRM.setText(L_STRTKN.nextToken());}
			else if(M_strHLPFLD.equals("txtPRS"))
				{txtPRS.setText(L_STRTKN.nextToken());}
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeHLPOK");}
	}

	
	

	/** Displaying Selective Detail 
	 */
	private void dspCNSDTL()
	{
		try
		{
			ResultSet rstRSSET = null;
			
			if(rdbBASE_BAG.isSelected())
				M_strSQLQRY = "select "+strKEY01_BAG+" prd_key01,"+strKEY02_BAG+" prd_key02,sum(ifnull(rct_rctqt,0)) prd_docqt from vw_rcprd where "+strWHRSTR+"   group by  "+strKEY01_BAG+","+strKEY02_BAG+" order by "+strKEY01_BAG+","+strKEY02_BAG;
			else if(rdbBASE_STK.isSelected())
				M_strSQLQRY = "select "+strKEY01_STK+" prd_key01,"+strKEY02_STK+" prd_key02,sum(ifnull(st_stkqt,0)) prd_docqt from vw_stprd where "+strWHRSTR+"   group by  "+strKEY01_STK+","+strKEY02_STK+" order by "+strKEY01_STK+","+strKEY02_STK;
			else if(rdbBASE_DSP.isSelected())
				M_strSQLQRY = "select "+strKEY01_DSP+" prd_key01,"+strKEY02_DSP+" prd_key02,sum(ifnull(ivt_invqt,0)) prd_docqt from vw_ivprd where "+strWHRSTR+"   group by  "+strKEY01_DSP+","+strKEY02_STK+" order by "+strKEY01_STK+","+strKEY02_STK;
			System.out.println(M_strSQLQRY);
			
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			crtINTRN_C2(rstRSSET);
			
			if(chkCFWQT.isSelected()==true)
				setCRFQT_C();
			int i;
			i=0;
			//int L_intRECCT = Integer.parseInt(txtRECCT.getText());

			tblCNSRPT.clrTABLE();

			dblINDQT_TOT=0; dblRSNPR_TOT=0;
			double L_dblINDQT=0, L_dblRSNVL=0;
			for(i=0;i<vtrINTRN01_C2.size();i++)
			{
				//if(L_intRECCT>0 && i>=L_intRECCT)
				//	break;
				String L_strKEY01 = vtrINTRN01_C2.elementAt(i).toString();
				String L_strKEY02 = vtrINTRN02_C2.elementAt(i).toString();
			    tblCNSRPT.setValueAt(setKEY01_DS(L_strKEY01),i,intTB1_KEY01);
			    tblCNSRPT.setValueAt(setKEY02_DS(L_strKEY02),i,intTB1_KEY02);
				//System.out.println("L_strKEY01 :"+L_strKEY01);
				//System.out.println("L_strKEY02 :"+L_strKEY02);
				L_dblINDQT=0; L_dblRSNVL=0;
				L_dblINDQT = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_INDQT"));
				L_dblRSNVL = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_RSNVL"));
				tblCNSRPT.setValueAt(setNumberFormat(L_dblINDQT,3),i,intTB1_INDQT);
				tblCNSRPT.setValueAt(setNumberFormat(L_dblRSNVL,0),i,intTB1_RSNVL);
				//System.out.println("dblINDQT_TOT :"+dblINDQT_TOT); 
				dblINDQT_TOT += L_dblINDQT;
				dblRSNPR_TOT += (L_dblINDQT*L_dblRSNVL);
			}
			tblCNSTOT.setValueAt("Total",0,intTB1_KEY01);
			tblCNSTOT.setValueAt(setNumberFormat(dblINDQT_TOT,3),0,intTB1_INDQT);
			tblCNSTOT.setValueAt(setNumberFormat(dblRSNPR_TOT/dblINDQT_TOT,0),0,intTB1_RSNVL);
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspCNSDTL");}
	}

	/** Adding carry forward booking qty. to Actual Booking Qty.
	 */
	private void setCRFQT_C()
	{
		try
		{
			if (flgIVT_RLSN)
				return;
			ResultSet rstRSSET = null;
			String L_strWHRSTR1 = " ifnull(int_cfwqt,0)>0 and in_bkgdt < '"+strCUTDT+"' and "+strWHRSTR1;
			String L_strCFWPR = "ifnull(int_cfwqt,0)*(ifnull(int_rsnvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)))";
			if(cmbKEY01.getSelectedItem().toString().equals(strPMRWS) || cmbKEY01.getSelectedItem().toString().equals(strPCTWS))
					M_strSQLQRY = "select "+strKEY01_INT+" in_key01,"+strKEY02_INT+" in_key02,sum(ifnull(int_cfwqt,0)) int_cfwqt,sum("+L_strCFWPR+") int_cfwpr from vw_intrn,co_cdtrn where  "+L_strWHRSTR1+"  and in_zoncd=cmt_codcd and cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' group by "+strKEY01_INT+","+strKEY02_INT+" order by "+strKEY01_INT+","+strKEY02_INT;
			else
					M_strSQLQRY = "select "+strKEY01_INT+" in_key01,"+strKEY02_INT+" in_key02,sum(ifnull(int_cfwqt,0)) int_cfwqt,sum("+L_strCFWPR+") int_cfwpr from vw_intrn where  "+L_strWHRSTR1+" group by "+strKEY01_INT+","+strKEY02_INT+" order by "+strKEY01_INT+","+strKEY02_INT;
			
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			crfINTRN_C2(rstRSSET);
		}
		catch (Exception L_EX) {setMSG(L_EX,"setCRFQT_C");}
	}
	

	
	
	
	/** Fetching Description for the first key field
	 */
	private String setKEY01_DS(String LP_KEYCD)
	{
		try
		{
			if(cmbKEY01.getSelectedItem().toString().equals(strPMRWS))
				return getCDTRN("MSTPRXXPMR"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strPCTWS))
				return getCDTRN("MSTPRXXPCT"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strPSCWS))
				return getCDWRK("PC"+LP_KEYCD,"CD_HDRDS");
			else if(cmbKEY01.getSelectedItem().toString().equals(strPAFWS))
				return getCDWRK("PC"+LP_KEYCD,"CD_HDRDS");
			else if(cmbKEY01.getSelectedItem().toString().equals(strPPDWS))
				return getCDTRN("MSTPRXXPPD"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strPPTWS))
				return getCDTRN("MSTPRXXPPC"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strPCCWS))
				return getCDTRN("MSTPRXXCLR"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY01.getSelectedItem().toString().equals(strPCSWS))
				return getCDWRK("CS"+LP_KEYCD,"CD_HDRDS");
			else if(cmbKEY01.getSelectedItem().toString().equals(strPRMWS))
				return getPRCAT("PM",LP_KEYCD);
			else if(cmbKEY01.getSelectedItem().toString().equals(strPRSWS))
				return getPRCAT("PRS",LP_KEYCD);
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
			if(cmbKEY01.getSelectedItem().toString().equals(strPMRWS))
				return getCDTRN("MSTPRXXPMR"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strPCTWS))
				return getCDTRN("MSTPRXXPCT"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strPSCWS))
				return getCDWRK("PC"+LP_KEYCD,"CD_HDRDS");
			else if(cmbKEY02.getSelectedItem().toString().equals(strPAFWS))
				return getCDWRK("PC"+LP_KEYCD,"CD_HDRDS");
			else if(cmbKEY02.getSelectedItem().toString().equals(strPPDWS))
				return getCDTRN("MSTPRXXPPD"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strPPTWS))
				return getCDTRN("MSTPRXXPPC"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strPCCWS))
				return getCDTRN("MSTPRXXCLR"+LP_KEYCD,"CMT_CODDS",hstCDTRN);
			else if(cmbKEY02.getSelectedItem().toString().equals(strPCSWS))
				return getCDWRK("CS"+LP_KEYCD,"CD_HDRDS");
			else if(cmbKEY02.getSelectedItem().toString().equals(strPRMWS))
				return getPRCAT("PM",LP_KEYCD);
			else if(cmbKEY02.getSelectedItem().toString().equals(strPRSWS))
				return getPRCAT("PRS",LP_KEYCD);
		}
		catch (Exception L_EX) {setMSG(L_EX,"setKEY02_DS");}
		return "";
	}

	

	/** Displaying Customer Orderwise detail
	 */
	private void dspINDDTL()
	{
		try
		{
			ResultSet rstRSSET = null;
			Hashtable hstINTRN_I1 = new Hashtable();
			hstINTRN_I1.clear();
			Hashtable hstINTRN_I2 = new Hashtable();
			hstINTRN_I2.clear();
			boolean L_flgTOPCHK = false;
			tblINDRPT.clrTABLE();
			
			//if(Integer.parseInt(txtRECCT.getText())>0)
		    //{
			//	//Records related to Additive Filler Typewise & Selective display are only considered
			//	for(int i = 0;i<tblCNSRPT.getRowCount();i++)
			//	{
			//		if(tblCNSRPT.getValueAt(i,intTB1_KEY01).toString().length() == 0)
			//			break;
			//		hstINTRN_I2.put(tblCNSRPT.getValueAt(i,intTB1_KEY01).toString(),"");
			//	}
			//}

			String L_strWHRSTR1 = " ifnull(int_cfwqt,0)>0 and in_bkgdt < '"+strCUTDT+"' and " + strWHRSTR1;
			if(flgIVT_RLSN)
				M_strSQLQRY = "select ivt_prdcd int_prdcd,date(ivt_invdt) in_inddt,ivt_prdds int_prdds,ivt_invno int_indno,ifnull(ivt_invqt,0) int_indqt,ifnull(ivt_rsnvl,0)/ifnull(ivt_invqt,1) int_rsnvl, ifnull(ivt_invrt,0) int_indvl, int(round(ifnull(ivt_cclvl,0)/ifnull(ivt_invqt,1),0)) int_cclvl,  int(round(ifnull(ivt_comvl,0)/ifnull(ivt_invqt,1),0)) int_comvl,int(round(ifnull(ivt_frtvl,0)/ifnull(ivt_invqt,1),0)) int_frtvl,  int(round(ifnull(ivt_crdvl,0)/ifnull(ivt_invqt,1),0)) int_crdvl, ivt_cnscd in_cnscd from mr_ivtrn where "+strWHRSTR+"  order by ivt_prdcd";
			else
				M_strSQLQRY = "select int_prdcd,int_prdds,int_indno,in_inddt,(ifnull(int_indqt,0)-ifnull(int_fcmqt,0)) int_indqt, ifnull(int_rsnvl,0)/ifnull(int_indqt,1) int_rsnvl, ifnull(int_basrt,0) int_indvl, int(round(ifnull(int_cclvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_cclvl,  int(round(ifnull(int_comvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_comvl, 0 int_frtvl,  int(round(ifnull(int_crdvl,0)/ifnull(int_indqt,1),0)) int_crdvl, in_cnscd from vw_intrn where "+strWHRSTR
							  + (chkCFWQT.isSelected()==true ? " union all select int_prdcd,int_prdds,int_indno,in_inddt,ifnull(int_cfwqt,0) int_indqt, ifnull(int_rsnvl,0)/(ifnull(int_indqt,0)-ifnull(int_fcmqt,0)) int_rsnvl, ifnull(int_basrt,0) int_indvl, int(round(ifnull(int_cclvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_cclvl,  int(round(ifnull(int_comvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_comvl, 0 int_frtvl,  int(round(ifnull(int_crdvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_crdvl, in_cnscd from vw_intrn where "+L_strWHRSTR1 : "")+"  order by int_indno,int_prdcd";

			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			dblINDQT_TOT=0; dblRSNPR_TOT=0;
			double L_dblINDQT=0, L_dblRSNVL=0;
			int i =0;
			while(true)
			{
				String L_strPRSNM = getPTMST("C",getRSTVAL(rstRSSET,"IN_CNSCD","C"),"PT_PRTNM");
				if(L_flgTOPCHK && !hstINTRN_I2.containsKey(L_strPRSNM) && !hstINTRN_I1.containsKey(getRSTVAL(rstRSSET,"INT_PRDCD","C")))
				{
					if(!rstRSSET.next())
						break;
					else
						continue;
				}
				L_dblINDQT=0; L_dblRSNVL=0;
				L_dblINDQT = Double.parseDouble(getRSTVAL(rstRSSET,"INT_INDQT","C"));
				L_dblRSNVL = Double.parseDouble(getRSTVAL(rstRSSET,"INT_RSNVL","C"));
			    tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_PRDDS","C"),i,intTB3_PRDDS);
			    tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_INDNO","C"),i,intTB3_INDNO);
			    tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"IN_INDDT","D"),i,intTB3_INDDT);
				tblINDRPT.setValueAt(setNumberFormat(L_dblINDQT,3),i,intTB3_INDQT);
				tblINDRPT.setValueAt(setNumberFormat(L_dblRSNVL,0),i,intTB3_RSNVL);
				tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_INDVL","C"),i,intTB3_INDVL);
				tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_CCLVL","C"),i,intTB3_CCLVL);
				tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_COMVL","C"),i,intTB3_COMVL);
				tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_FRTVL","C"),i,intTB3_FRTVL);
				tblINDRPT.setValueAt(getRSTVAL(rstRSSET,"INT_CRDVL","C"),i,intTB3_CRDVL);
				tblINDRPT.setValueAt(L_strPRSNM,i,intTB3_CNSNM);
				dblINDQT_TOT += L_dblINDQT;
				dblRSNPR_TOT += (L_dblINDQT*L_dblRSNVL);
				i++;
				if(!rstRSSET.next())
					break;
			}
			tblINDTOT.setValueAt("Total",0,intTB3_PRDDS);
			tblINDTOT.setValueAt(setNumberFormat(dblINDQT_TOT,3),0,intTB3_INDQT);
			tblINDTOT.setValueAt(setNumberFormat(dblRSNPR_TOT/dblINDQT_TOT,0),0,intTB3_RSNVL);
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
			strPSCSTR = setSBSSTR("PSC");
			strPCCSTR = setSBSSTR("PCC");
			strSBSSTR = setSBSSTR("SBS");
			setWHRSTR_ALL();

						
			if(!exeTBLREFSH())
				return;
			
				//strQTFLD_INT = "ifnull(int_indqt,0)+ifnull(int_cfwqt,0)";
				//strVLFLD_INT = "(ifnull(int_rsnvl,0)/ifnull(int_indqt,1))";
			ResultSet rstRSSET;
			M_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp = 'MRXXCRF'";
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(rstRSSET.next() && rstRSSET!=null)
				strCUTDT = getRSTVAL(rstRSSET,"CMT_CCSVL","C");
			dspCNSDTL();
			dspINDDTL();
		}
		catch (Exception L_EX) {setMSG(L_EX,"exePRINT");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
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
				if(cmbRPTWS.getSelectedItem().toString().equals(strPMRWS))
					{exeGETSCT(lblPMR, txtPMR);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPCTWS))
					{exeGETSCT(lblPCT, txtPCT);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPSCWS))
					{exeGETSCT(lblPSC, txtPSC);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPAFWS))
					{exeGETSCT(lblPAF, txtPAF);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPPDWS))
					{exeGETSCT(lblPPD, txtPPD);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPPTWS))
					{exeGETSCT(lblPPT, txtPPT);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPCCWS))
					{exeGETSCT(lblPCC, txtPPC);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPCSWS))
					{exeGETSCT(lblPCS, txtPCS);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRMWS))
					{exeGETSCT(lblPRM, txtPRM);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRSWS))
					{exeGETSCT(lblPRS, txtPRS);}
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
				if(txtPMR.getText().length()>0)
				{
					strWHRBAG_PMR = setWHRSUB(strWHRBAG_PMR, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,3,2) = '"+txtPMR.getText()+"')");
					strWHRSTK_PMR = setWHRSUB(strWHRSTK_PMR, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,3,2) = '"+txtPMR.getText()+"')");
					strWHRDSP_PMR = setWHRSUB(strWHRDSP_PMR, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,3,2) = '"+txtPMR.getText()+"')");
					strWHRBKG_PMR = setWHRSUB(strWHRBKG_PMR, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,3,2) = '"+txtPMR.getText()+"')");
				}
				if(txtPCT.getText().length()>0)
				{
					strWHRBAG_PCT = setWHRSUB(strWHRBAG_PCT, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,2) = '"+txtPCT.getText()+"')");
					strWHRSTK_PCT = setWHRSUB(strWHRSTK_PCT, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,2) = '"+txtPCT.getText()+"')");
					strWHRDSP_PCT = setWHRSUB(strWHRDSP_PCT, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,2) = '"+txtPCT.getText()+"')");
					strWHRBKG_PCT = setWHRSUB(strWHRBKG_PCT, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,2) = '"+txtPCT.getText()+"')");
				}
				if(txtPSC.getText().length()>0)
				{
					strWHRBAG_PSC = setWHRSUB(strWHRBAG_PSC, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,4) = '"+txtPSC.getText()+"')");
					strWHRSTK_PSC = setWHRSUB(strWHRSTK_PSC, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,4) = '"+txtPSC.getText()+"')");
					strWHRDSP_PSC = setWHRSUB(strWHRDSP_PSC, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,4) = '"+txtPSC.getText()+"')");
					strWHRBKG_PSC = setWHRSUB(strWHRBKG_PSC, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,4) = '"+txtPSC.getText()+"')");
				}
				if(txtPAF.getText().length()>0)
				{
					strWHRBAG_PAF = setWHRSUB(strWHRBAG_PAF, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,6) = '"+txtPAF.getText()+"')");
					strWHRSTK_PAF = setWHRSUB(strWHRSTK_PAF, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,6) = '"+txtPAF.getText()+"')");
					strWHRDSP_PAF = setWHRSUB(strWHRDSP_PAF, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,6) = '"+txtPAF.getText()+"')");
					strWHRBKG_PAF = setWHRSUB(strWHRBKG_PAF, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,5,6) = '"+txtPAF.getText()+"')");
				}
				if(txtPPD.getText().length()>0)
				{
					strWHRBAG_PPD = setWHRSUB(strWHRBAG_PPD, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,11,2) between '"+txtPPD.getText()+"' and '"+txtPPD.getText()+"')");
					strWHRSTK_PPD = setWHRSUB(strWHRSTK_PPD, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,11,2)  between '"+txtPPD.getText()+"' and '"+txtPPD.getText()+"')");
					strWHRDSP_PPD = setWHRSUB(strWHRDSP_PPD, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,11,2)  between '"+txtPPD.getText()+"' and '"+txtPPD.getText()+"')");
					strWHRBKG_PPD = setWHRSUB(strWHRBKG_PPD, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,11,2)  between '"+txtPPD.getText()+"' and '"+txtPPD.getText()+"')");
				}
				if(txtPPT.getText().length()>0)
				{
					strWHRBAG_PPT = setWHRSUB(strWHRBAG_PPT, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,13,2) between '"+txtPPT.getText()+"' and '"+txtPPT.getText()+"')");
					strWHRSTK_PPT = setWHRSUB(strWHRSTK_PPT, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,13,2)  between '"+txtPPT.getText()+"' and '"+txtPPT.getText()+"')");
					strWHRDSP_PPT = setWHRSUB(strWHRDSP_PPT, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,13,2)  between '"+txtPPT.getText()+"' and '"+txtPPT.getText()+"')");
					strWHRBKG_PPT = setWHRSUB(strWHRBKG_PPT, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,13,2)  between '"+txtPPT.getText()+"' and '"+txtPPT.getText()+"')");
				}
				if(txtPCC.getText().length()>0)
				{
					strWHRBAG_PCC = setWHRSUB(strWHRBAG_PCC, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,2) = '"+txtPCC.getText()+"')");
					strWHRSTK_PCC = setWHRSUB(strWHRSTK_PCC, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,2) = '"+txtPCC.getText()+"')");
					strWHRDSP_PCC = setWHRSUB(strWHRDSP_PCC, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,2) = '"+txtPCC.getText()+"')");
					strWHRBKG_PCC = setWHRSUB(strWHRBKG_PCC, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,2) = '"+txtPCC.getText()+"')");
				}
				if(txtPCS.getText().length()>0)
				{
					strWHRBAG_PCS = setWHRSUB(strWHRBAG_PCS, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,6) = '"+txtPCS.getText()+"')");
					strWHRSTK_PCS = setWHRSUB(strWHRSTK_PCS, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,6) = '"+txtPCS.getText()+"')");
					strWHRDSP_PCS = setWHRSUB(strWHRDSP_PCS, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,6) = '"+txtPCS.getText()+"')");
					strWHRBKG_PCS = setWHRSUB(strWHRBKG_PCS, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,15,6) = '"+txtPCS.getText()+"')");
				}
				if(txtPRM.getText().length()>0)
				{
					strWHRBAG_PRM = setWHRSUB(strWHRBAG_PRM, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,21,2) = '"+txtPRM.getText()+"')");
					strWHRSTK_PRM = setWHRSUB(strWHRSTK_PRM, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,21,2) = '"+txtPRM.getText()+"')");
					strWHRDSP_PRM = setWHRSUB(strWHRDSP_PRM, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,21,2) = '"+txtPRM.getText()+"')");
					strWHRBKG_PRM = setWHRSUB(strWHRBKG_PRM, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,21,2) = '"+txtPRM.getText()+"')");
				}
				if(txtPRS.getText().length()>0)
				{
					strWHRBAG_PRS = setWHRSUB(strWHRBAG_PRS, " RCT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,23,2) = '"+txtPRS.getText()+"')");
					strWHRSTK_PRS = setWHRSUB(strWHRSTK_PRS, " ST_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,23,2) = '"+txtPRS.getText()+"')");
					strWHRDSP_PRS = setWHRSUB(strWHRDSP_PRS, " IVT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,23,2) = '"+txtPRS.getText()+"')");
					strWHRBKG_PRS = setWHRSUB(strWHRBKG_PRS, " INT_PRDCD in (select PR_OPRCD from PR_PRMST where substr(PR_TPRCD,23,2) = '"+txtPRS.getText()+"')");
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
				strWHRBAG_PMR = "";
				strWHRBAG_PCT = "";
				strWHRBAG_PSC = "";
				strWHRBAG_PAF = "";
				strWHRBAG_PPD = "";
				strWHRBAG_PPT = "";
				strWHRBAG_PCC = "";
				strWHRBAG_PCS = "";
				strWHRBAG_PRM = "";
				strWHRBAG_PRS = "";

				strWHRSTK_PMR = "";
				strWHRSTK_PCT = "";
				strWHRSTK_PSC = "";
				strWHRSTK_PAF = "";
				strWHRSTK_PPD = "";
				strWHRSTK_PPT = "";
				strWHRSTK_PCC = "";
				strWHRSTK_PCS = "";
				strWHRSTK_PRM = "";
				strWHRSTK_PRS = "";

				strWHRDSP_PMR = "";
				strWHRDSP_PCT = "";
				strWHRDSP_PSC = "";
				strWHRDSP_PAF = "";
				strWHRDSP_PPD = "";
				strWHRDSP_PPT = "";
				strWHRDSP_PCC = "";
				strWHRDSP_PCS = "";
				strWHRDSP_PRM = "";
				strWHRDSP_PRS = "";

				strWHRBKG_PMR = "";
				strWHRBKG_PCT = "";
				strWHRBKG_PSC = "";
				strWHRBKG_PAF = "";
				strWHRBKG_PPD = "";
				strWHRBKG_PPT = "";
				strWHRBKG_PCC = "";
				strWHRBKG_PCS = "";
				strWHRBKG_PRM = "";
				strWHRBKG_PRS = "";
				
				
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
			strWHRSTR1 = "";
				if(strWHRBAG_PMR.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PMR+") ";
				else if(strWHRSTK_PMR.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PMR+") ";
				else if(strWHRDSP_PMR.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PMR+") ";
				else if(strWHRBKG_PMR.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PMR+") ";


				if(strWHRBAG_PCT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PCT+") ";
				else if(strWHRSTK_PCT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PCT+") ";
				else if(strWHRDSP_PCT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PCT+") ";
				else if(strWHRBKG_PCT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PCT+") ";
				
				
				if(strWHRBAG_PSC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PSC+") ";
				else if(strWHRSTK_PSC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PSC+") ";
				else if(strWHRDSP_PSC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PSC+") ";
				else if(strWHRBKG_PSC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PSC+") ";
				
				if(strWHRBAG_PAF.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PAF+") ";
				else if(strWHRSTK_PAF.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PAF+") ";
				else if(strWHRDSP_PAF.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PAF+") ";
				else if(strWHRBKG_PAF.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PAF+") ";
				
				if(strWHRBAG_PPD.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PPD+") ";
				else if(strWHRSTK_PPD.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PPD+") ";
				else if(strWHRDSP_PPD.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PPD+") ";
				else if(strWHRBKG_PPD.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PPD+") ";
				
				if(strWHRBAG_PPT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PPT+") ";
				else if(strWHRSTK_PPT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PPT+") ";
				else if(strWHRDSP_PPT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PPT+") ";
				else if(strWHRBKG_PPT.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PPT+") ";
				
				if(strWHRBAG_PCC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PCC+") ";
				else if(strWHRSTK_PCC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PCC+") ";
				else if(strWHRDSP_PCC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PCC+") ";
				else if(strWHRBKG_PCC.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PCC+") ";
				
				if(strWHRBAG_PCS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PCS+") ";
				else if(strWHRSTK_PCS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PCS+") ";
				else if(strWHRDSP_PCS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PCS+") ";
				else if(strWHRBKG_PCS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PCS+") ";
				
				if(strWHRBAG_PRM.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PRM+") ";
				else if(strWHRSTK_PRM.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PRM+") ";
				else if(strWHRDSP_PRM.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PRM+") ";
				else if(strWHRBKG_PRM.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PRM+") ";
				
				if(strWHRBAG_PRS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBAG_PRS+") ";
				else if(strWHRSTK_PRS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRSTK_PRS+") ";
				else if(strWHRDSP_PRS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRDSP_PRS+") ";
				else if(strWHRBKG_PRS.length() > 2)
					strWHRSTR1 += " and ("+ strWHRBKG_PRS+") ";
				
				//System.out.println(strWHRSTR);
				strWHRSTR = strWHRSTR + strWHRSTR1;
			    strWHRSTR1 = " and (int_stsfl <>'X' and ifnull(ivt_invqt,0)>0  and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'"
				+ " and ivt_sbscd in "+M_strSBSLS+ strWHRSTR1;
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
			txtPMR.setText("");
			txtPCT.setText("");
			txtPSC.setText("");
			txtPAF.setText("");
			txtPPD.setText("");
			txtPPT.setText("");
			txtPPC.setText("");
			txtPCS.setText("");
			txtPRM.setText("");
			txtPRS.setText("");
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
	
	/**  Report heaer for Selective detail
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
			O_DOUT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd",25)+padSTRING('C',"Selective Realisation Details  from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intCNSWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			setOPTHDR();						
			O_DOUT.writeBytes("\n"); ;
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
					if(i % 3 == 0 && i>0) {O_DOUT.writeBytes("\n"); intLINECT ++;}
				}
				O_DOUT.writeBytes("\n"); intLINECT ++;
			}
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setOPTHDR");}
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
			O_DOUT.writeBytes(padSTRING('R',"Supreme Petrochem Ltd",25)+padSTRING('C',"Customer Orderwise Realisation from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intINDWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
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
			tbpMAIN.remove(pnlINDRPT);
			pnlCNSRPT.removeAll();
			pnlINDRPT.removeAll();

			pnlCNSRPT = new JPanel(null);
			pnlINDRPT = new JPanel(null);
			
			int i;
			M_strSQLQRY = "select count(*) int_recct from "+(flgIVT_RLSN ? " mr_ivtrn " : " vw_intrn ")+" where "+strWHRSTR;
			ResultSet rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next()  || rstRSSET==null)
				{setMSG("No Detail Records Found",'E');return false;}
			int L_intRECCT = Integer.parseInt(getRSTVAL(rstRSSET,"INT_RECCT","N"));
			L_intRECCT = 3*L_intRECCT;
			rstRSSET.close();
			//System.out.println("004");
			setKEY01();
			setKEY02();
			tblCNSRPT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,L_intRECCT,0,1,7,7.9,arrCNSHDR_WD,new int[]{0});  
			tblCNSTOT = crtTBLPNL1(pnlCNSRPT,arrCNSHDR,1,7,1,2.1,7.9,arrCNSHDR_WD,new int[]{0});  

			tblINDRPT = crtTBLPNL1(pnlINDRPT,arrINDHDR,L_intRECCT,0,1,7,7.9,arrINDHDR_WD,new int[]{0});  
			tblINDTOT = crtTBLPNL1(pnlINDRPT,arrINDHDR,1,7,1,2.1,7.9,arrINDHDR_WD,new int[]{0});  

			tbpMAIN.addTab("Selective Detail",pnlCNSRPT);
			tbpMAIN.addTab("Document Detail",pnlINDRPT);
		
			//System.out.println("005");
			tblCNSRPT.addMouseListener(this);
			tblINDRPT.addMouseListener(this);
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeREFSH");}
		return true;
	}

	
	/** Setting string for Sub-system, Product Sub-Category & Sale Type Filter
	 */	
	private String setSBSSTR(String LP_SBSTP)
	{
		String L_strRETSTR = ""; 
		try
		{
			//System.out.println("M_staUSRRT.length : "+M_staUSRRT.length);
			for(int i=0;i<M_staUSRRT.length;i++)
			{
				if(LP_SBSTP.equals("PSC"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(2,4)+"',";
				else if(LP_SBSTP.equals("PCC"))
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
			String L_strDTFLD = (flgIVT_RLSN ? " date(ivt_invdt) " : " in_bkgdt ");
				
			L_strRETSTR = L_strDTFLD +" between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and "+(flgIVT_RLSN ? " ivt_stsfl " : " int_stsfl ")+" <>'X' and ifnull("+(flgIVT_RLSN ? " ivt_invqt " : " (ifnull(int_indqt,0)-ifnull(int_fcmqt,0)) ")+",0)>0  and "+(flgIVT_RLSN ? " ivt_mkttp " : " int_mkttp ")+" = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'"
				+ " and "+(flgIVT_RLSN ? " ivt_sbscd " : " int_sbscd ")+" in "+M_strSBSLS;
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
	        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl <> 'X' and substr(pr_prdcd,1,2) in ('51','52','53','54')";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
	        if(L_rstRSSET == null || !L_rstRSSET.next())
	        {
	             setMSG("Product Records not found in co_prmst",'E');
	              return;
	        }
	        while(true)
	        {
	                strPRMCD = getRSTVAL(L_rstRSSET,"PR_PRDCD","C");
	                String[] staPRMST = new String[intPRMST_TOT];
	                staPRMST[intAE_PR_PRDDS] = getRSTVAL(L_rstRSSET,"PR_PRDDS","C");
	                staPRMST[intAE_PR_AVGRT] = getRSTVAL(L_rstRSSET,"PR_AVGRT","N");
	                hstPRMST.put(strPRMCD,staPRMST);
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
	 * into the Hash Table
	 */
     private void crtINTRN_C2(ResultSet LP_RSSET)
    {
		hstINTRN_C2.clear();
        try
        {
			//cl_dat.exeSQLUPD("delete from MR_CPWRK","");
			//if(cl_dat.exeDBCMT("crtINTRN_C2"))
			//	setMSG("Records cleared from MR_CPWRK",'N');
			String L_strKEY01 = "";
			String L_strKEY02 = "";
			hstINTRN_C2.clear();
			vtrINTRN01_C2.clear();
			vtrINTRN02_C2.clear();
            while(true)
            {
				L_strKEY01 = getRSTVAL(LP_RSSET,"IN_KEY01","C");
				L_strKEY02 = getRSTVAL(LP_RSSET,"IN_KEY02","C");
				dblINDQT = Double.parseDouble(getRSTVAL(LP_RSSET,"INT_INDQT","N"));
				dblRSNVL = Double.parseDouble(getRSTVAL(LP_RSSET,"INT_RSNPR","N"))/dblINDQT;
				String[] staINTRN = new String[intINTRN_TOT_C];
				staINTRN[intAE_INT_INDQT_C] = setNumberFormat(dblINDQT,3);
				staINTRN[intAE_INT_RSNVL_C] = setNumberFormat(dblRSNVL,0);
				hstINTRN_C2.put(L_strKEY01+L_strKEY02,staINTRN);
				vtrINTRN01_C2.addElement(L_strKEY01);
				vtrINTRN02_C2.addElement(L_strKEY02);
				dblINDQT =0; dblRSNVL =0;
                if (!LP_RSSET.next())
                    break;
            }
            LP_RSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtINTRN_C2");
        }
	}
		

	 
	/** One time data capturing for  VW_INTRN
	 * into the Vector
	 */
     private void crfINTRN_C2(ResultSet LP_RSSET)
    {
        try
        {
            while(true)
            {
				String L_strKEY01 = getRSTVAL(LP_RSSET,"IN_KEY01","C");
				String L_strKEY02 = getRSTVAL(LP_RSSET,"IN_KEY02","C");
				dblCFWQT = Double.parseDouble(getRSTVAL(LP_RSSET,"INT_CFWQT","N"));
				dblCFWPR = Double.parseDouble(getRSTVAL(LP_RSSET,"INT_CFWPR","N"));
				dblINDQT = 0;  dblRSNPR=0;
				//System.out.println("Key: "+L_strKEY01+L_strKEY02);
				//System.out.println("                CFW Q: "+setNumberFormat(dblCFWQT,3)+"   CFW PR: "+setNumberFormat(dblCFWPR,0));
				if(hstINTRN_C2.containsKey(L_strKEY01+L_strKEY02))
				{
					dblINDQT = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_INDQT"));
					dblRSNPR = Double.parseDouble(getINTRN_C2(L_strKEY01+L_strKEY02,"INT_RSNVL"))*dblINDQT;
					//System.out.println("                IND Q: "+setNumberFormat(dblINDQT,0)+"   IND PR: "+setNumberFormat(dblRSNPR,0));
				}
				String L_strINDQT = setNumberFormat(dblINDQT+dblCFWQT,3);
				String L_strRSNVL = setNumberFormat((dblCFWPR+dblRSNPR)/(dblINDQT+dblCFWQT),3);
				//System.out.println("                                         New V: "+L_strRSNVL);
				if(!hstINTRN_C2.containsKey(L_strKEY01+L_strKEY02))
				{
					String[] staINTRN = new String[intINTRN_TOT_C];
					staINTRN[intAE_INT_INDQT_C] = L_strINDQT;
					staINTRN[intAE_INT_RSNVL_C] = L_strRSNVL;
					hstINTRN_C2.put(L_strKEY01+L_strKEY02,staINTRN);
				}
				else
				{
		            putCRFQT_C2(L_strKEY01+L_strKEY02,"INT_INDQT",L_strINDQT);
		            putCRFQT_C2(L_strKEY01+L_strKEY02,"INT_RSNVL",L_strRSNVL);
				}
                if (!LP_RSSET.next())
					break;
            }
            LP_RSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crfINTRN_C2");
        }
	}
	 
	/**
	 */
	 private void putCRFQT_C2(String LP_KEYVL, String LP_FLDNM, String LP_FLDVL)
    {
	//System.out.println("putSTMST : "+LP_STMST_KEY+"/"+LP_FLDNM+"/"+LP_FLDVL);
    try
    {

			if(!hstINTRN_C2.containsKey(LP_KEYVL))
				return;
			String[] L_staINTRN = (String[])hstINTRN_C2.get(LP_KEYVL);
            if (LP_FLDNM.equals("INT_INDQT"))
				L_staINTRN[intAE_INT_INDQT_C]= LP_FLDVL;
            else if (LP_FLDNM.equals("INT_RSNVL"))
				L_staINTRN[intAE_INT_RSNVL_C]= LP_FLDVL;
			hstINTRN_C2.put(LP_KEYVL,L_staINTRN);
    }
	catch (Exception L_EX)
	{
		setMSG(L_EX,"putCRFQT_C2");
	}
    }

	 
	 
	 
	 
		
	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	 */
     private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable LP_HSTNM)
    {
		String L_strSQLQRY = "";
        try
        {
            L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp||cmt_cgstp in ("+LP_CATLS+")"+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
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


	/** One time data capturing for specified category from Product Code Header file
	 * into the Hash Table
	 */
     private void crtCDWRK(String LP_CATLS)
    {
		String L_strSQLQRY = "";
        try
        {
            L_strSQLQRY = "select * from pr_cdwrk where CD_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND cd_hdrct in ("+LP_CATLS+")  order by cd_hdrct";
			//System.out.println(L_strSQLQRY);
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 //setMSG("Records not found in PR_CDWRK",'E');
                  return;
            }
            while(true)
            {
                    strHDRCT = getRSTVAL(L_rstRSSET,"CD_HDRCT","C");
                    strHDRCD = getRSTVAL(L_rstRSSET,"CD_HDRCD","C");
                    String[] staCDWRK = new String[intCDWRK_TOT];
                    staCDWRK[intAE_CD_HDRDS] = getRSTVAL(L_rstRSSET,"CD_HDRDS","C");
                    hstCDWRK.put(strHDRCT+strHDRCD,staCDWRK);
                    if (!L_rstRSSET.next())
                            break;
            }
            L_rstRSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtCDWRK");
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
private void setCMBVL(JComboBox LP_CMBNM, Vector LP_VTRNM)
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
	


		/** Picking up Specified Codes Transaction related details from Hash Table
		 * <B> for Specified Code Transaction key
		 * @param LP_CDWRK_KEY	Header Code key
		 */
        private String getCDWRK(String LP_CDWRK_KEY, String LP_FLDNM)
        {
		//System.out.println("getCDWRK: "+LP_CDWRK_KEY);
        try
        {
			    if(!hstCDWRK.containsKey(LP_CDWRK_KEY))
					{System.out.println("getCDWRK : "+LP_CDWRK_KEY+" Not found"); return "";}
                if (LP_FLDNM.equals("CD_HDRDS"))
                        return ((String[])hstCDWRK.get(LP_CDWRK_KEY))[intAE_CD_HDRDS];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDWRK");
			System.out.println("getCDWRK : "+LP_CDWRK_KEY+"/"+LP_FLDNM);
		}
        return "";
        }
		

		
		/** Picking up Specified Codes Transaction related details from Hash Table
		 * <B> for Specified Code Transaction key
		 * @param LP_CDWRK_KEY	Header Code key
		 */
        private String getPRCAT(String LP_PRCAT, String LP_KEYVL)
        {
		//System.out.println("getPRCAT: "+LP_PRCAT_KEY);
        try
        {
			    //if(!hstCDWRK.containsKey(LP_PRCAT))
				//	{System.out.println("getPRCAT : "+LP_PRCAT+" Not found"); return "";}
                //if (LP_FLDNM.equals("CD_HDRDS"))
                //        return ((String[])hstCDWRK.get(LP_CDWRK_KEY))[intAE_CD_HDRDS];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getPRCAT");
			System.out.println("getPRCAT : "+LP_PRCAT);
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
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getINTRN_C2");
			System.out.println("getINTRN_C2 : "+LP_INTRN_KEY+"/"+LP_FLDNM);
		}
        return "";
        }
		

		
		
		
	/**
	 */
	private   String padNUMBER(char P_chrPADTP,String P_strSTRVL,int P_intPADLN)	{		String P_strTRNVL = "";		try		{			
			if(P_strSTRVL.length()==0)				P_strSTRVL = "-";
			else if(Double.parseDouble(P_strSTRVL)==0)				P_strSTRVL = "-";
			P_strSTRVL = P_strSTRVL.trim();			int L_STRLN = P_strSTRVL.length();			if(P_intPADLN <= L_STRLN)			{				P_strSTRVL = P_strSTRVL.substring(0,P_intPADLN).trim();				L_STRLN = P_strSTRVL.length();				P_strTRNVL = P_strSTRVL;			}			int L_STRDF = P_intPADLN - L_STRLN;
						StringBuffer L_STRBUF;			switch(P_chrPADTP)			{				case 'C':					L_STRDF = L_STRDF / 2;					L_STRBUF = new StringBuffer(L_STRDF);					for(int j = 0;j < L_STRBUF.capacity();j++)						L_STRBUF.insert(j,' ');					P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;					break;				case 'R':					L_STRBUF = new StringBuffer(L_STRDF);					for(int j = 0;j < L_STRBUF.capacity();j++)					{
						if(M_rdbTEXT.isSelected())
						L_STRBUF.insert(j,' ');
						else
							L_STRBUF.insert(j,"\t");
					}					P_strTRNVL =  P_strSTRVL+L_STRBUF ;					break;				case 'L':					L_STRBUF = new StringBuffer(L_STRDF);					for(int j = 0;j < L_STRBUF.capacity();j++)						L_STRBUF.insert(j,' ');					P_strTRNVL =  L_STRBUF+P_strSTRVL ;					break;			}		}catch(Exception L_EX){			setMSG(L_EX,"padNUMBER");		}		return P_strTRNVL;	}

	
/** Generating string for Insertion Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {try 
{	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);	if (LP_FLDTP.equals("C"))		 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',"; 	else if (LP_FLDTP.equals("N"))         return   nvlSTRVL(LP_FLDVL,"0") + ","; 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";	else if (LP_FLDTP.equals("T"))		 return   (LP_FLDVL.length()>10) ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else return " ";        }    catch (Exception L_EX) 
	{setMSG("Error in setINSSTR : "+L_EX,'E');}
return " ";}		



	/** Generating string for Updation Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 */	private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {	try 
	{		//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);		if (LP_FLDTP.equals("C"))			 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");	 	else if (LP_FLDTP.equals("N"))	         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");	 	else if (LP_FLDTP.equals("D"))
			 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));		else if (LP_FLDTP.equals("T"))			 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
		else return " ";	        }	    catch (Exception L_EX) 
		{setMSG("Error in setUPDSTR : "+L_EX,'E');}
	return " ";	}
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