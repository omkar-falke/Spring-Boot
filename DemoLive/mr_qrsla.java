//Sales Analysis Query 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.Date;
import java.sql.SQLException;
import java.io.*;
import java.awt.Color;
import java.sql.ResultSet;
import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;

class mr_qrsla extends cl_rbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;
	private JPanel pnlDTLRPT,pnlSUMRPT,pnlREGRPT,pnlDUCRPT;
	private String strSPCCT,strPRDCD,strPRTTP,strPRTCD,strWHRSTR,strWHRSTR_REST,strORDBY;	//strSTRDT,strENDDT,
	private String strZONSTR, strSALSTR, strSBSSTR;
	private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_qrsla.doc"; 
	private JTextField txtSPCCT,txtSPCCT1;

	private cl_JTable tblDTLRPT,tblSUMRPT,tblREGRPT,tblDUCRPT;

	//private JRadioButton rdbALLWS, rdbRGNWS, rdbZONWS, rdbGRDWS, rdbDSRWS, rdbBYRWS, rdbSALWS, rdbPRDWS;
	//private JRadioButton rdbDFTSQ, rdbGRDSQ, rdbDSRSQ, rdbBYRSQ, rdbINOSQ, rdbIDTSQ;
	private JCheckBox chkSTOTFL, chkCSTKFL;
	private JLabel lblSPCCT, lblSPCDS,lblSPCCT1, lblSPCDS1;
	private JLabel lblRPTSQ, lblRPTWS,lblRPTWS1, lblMKTTP;
	private ButtonGroup btgRPTWS, btgRPTSQ;
	private JButton btnDTLPRINT, btnSUMPRINT, btnREGPRINT, btnDUCPRINT;
	//private JCheckBox chkSELFML;

	private String strCGMTP;
	private String strCGSTP;
	private String strCODCD;

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
	private int intTB1_DSRNM = 1;
	private int intTB1_BYRNM = 2;
	private int intTB1_PRDDS = 3;
	private int intTB1_INVQT = 4;
	private int intTB1_INVNO = 5;
	private int intTB1_INVDT = 6;
	

	private int intIVTRN_TOT = 25;
	private int intAE_IVT_INVNO = 0;
	private int intAE_IVT_DORNO = 1;
	private int intAE_IVT_DORDT = 2;
	private int intAE_IVT_SALTP = 3;
	private int intAE_IVT_DTPCD = 4;
	private int intAE_IVT_PMTCD = 5;
	private int intAE_IVT_PORNO = 6;
	private int intAE_IVT_BYRCD = 7;
	private int intAE_IVT_INVDT = 8;
	private int intAE_IVT_INDNO = 9;
	private int intAE_IVT_INDDT = 10;
	private int intAE_IVT_LRYNO = 11;
	private int intAE_IVT_LR_NO = 12;
	private int intAE_IVT_CNTDS = 13;
	private int intAE_IVT_PORDT = 14;
	private int intAE_IVT_TRPCD = 15;
	private int intAE_IVT_DSRTP = 16;
	private int intAE_IVT_DSRCD = 17;
	private int intAE_IVT_INVQT = 18;
	private int intAE_IVT_ASSVL = 19;
	private int intAE_IVT_EXCVL = 20;
	private int intAE_IVT_EDHVL = 21;
	private int intAE_IVT_FRTVL = 22;
	private int intAE_IVT_INVVL = 23;
	private int intAE_IVT_CNSCD = 24;
	
	
	private int intTB31_CHKFL = 0;
	
	private int intTB31_INVNO = 1;
	private int intTB31_DORNO = 2;
	private int intTB31_DORDT = 3;
	private int intTB31_SALDS = 4;
	private int intTB31_DTPDS = 5;
	private int intTB31_PMTDS = 6;
	private int intTB31_PORNO = 7;
	private int intTB31_BYRNM1 = 8;
	private int intTB31_BYRNM = 9;

	private int intTB32_INVDT = 1;
	private int intTB32_INDNO = 2;
	private int intTB32_INDDT = 3;
	private int intTB32_LRYNO = 4;
	private int intTB32_LR_NO = 5;
	private int intTB32_CNTDS = 6;
	private int intTB32_PORDT = 7;
	private int intTB32_TRPNM1 = 8;
	private int intTB32_TRPNM = 9;

	private int intTB33_BLK01 = 1;
	private int intTB33_DSRNM = 2;
	private int intTB33_INVQT = 3;
	private int intTB33_ASSVL = 4;
	private int intTB33_EXCVL = 5;
	private int intTB33_EDHVL = 6;
	private int intTB33_FRTVL = 7;
	private int intTB33_INVVL = 8;
	private int intTB33_CNSNM = 9;

	
	private int intTB34_BLK01 = 1;
	private int intTB34_PRDDS = 2;
	private int intTB34_INVQT = 3;
	private int intTB34_ASSVL = 4;
	private int intTB34_EXCVL = 5;
	private int intTB34_EDHVL = 6;
	private int intTB34_FRTVL = 7;
	private int intTB34_INVRT = 8;
	private int intTB34_LOTNO = 9;
	//private int intTB3_SALDS = 8;
	//private int intTB3_CNTDS = 13;
	//private int intTB3_LADNO = 18;
	//private int intTB3_LADDT = 19;
	//private int intTB3_EDCVL = 22;
	
	private int intTB41_CHKFL = 0;
	
	private int intTB41_BYRNM = 1;
	private int intTB41_DSRNM = 2;
	private int intTB41_INVNO = 3;
	private int intTB41_INVVL = 4;
	private int intTB41_INVCP = 5;
	private int intTB41_ACCCP = 6;
	private int intTB41_DORNO = 7;
	private int intTB41_INDNO = 8;

	private int intTB42_BLK01 = 1;
	private int intTB42_BLK02 = 2;
	private int intTB42_INVDT = 3;
	private int intTB42_BLK04 = 4;
	private int intTB42_INVDD = 5;
	private int intTB42_ACCDD = 6;
	private int intTB42_DORDT = 7;
	private int intTB42_INDDT = 8;
	
	
	private String strALLWS = "Overall";		
	private String strRGNWS = "Region";		
	private String strZONWS = "Zone";
	private String strGRDWS = "Grade";	
	private String strDSRWS = "Distributor";	
	private String strBYRWS = "Buyer";	
	private String strSALWS = "Sale Type";	
	private String strPMTWS = "Payment Type";	
	private String strPRDWS = "Prod.Category";	
	private String strLOTWS = "Lot No.";	
	private String strTRPWS = "Transporter";	
	private String strCNSWS = "Consignee";	
	

	private String strDFTSQ = "Default";		
	private String strGRDSQ = "Grade";		
	private String strDSRSQ = "Distributor";		
	private String strBYRSQ = "Buyer";		
	private String strINOSQ = "Invoice No.";		
	
	
	
	
	private String[] arrDTLHDR = new String[]{"0","1","2","3","4","5","6"};
	private int[] arrDTLHDR_WD = new int[]{0,1,2,3,4,5,6};
	private char[] arrDTLHDR_PAD = new char[]{'0','1','2','3','4','5','6'};
	
	private String[] arrREGHDR = new String[]{"","","","","","","","","","","",""};
	private String[] arrREGHDR1 = new String[]{"","","","","","","","","","","",""};
	private String[] arrREGHDR2 = new String[]{"","","","","","","","","","","",""};
	private String[] arrREGHDR3 = new String[]{"","","","","","","","","","","",""};
	private String[] arrREGHDR4 = new String[]{"","","","","","","","","","","",""};
	private int[] arrREGHDR_WD = new int[]{0,1,2,3,4,5,6,7,8,9,10,11};
	private char[] arrREGHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8','9','0','1'};

	private String[] arrDUCHDR = new String[]{"","","","","","","","","","",""};
	private String[] arrDUCHDR1 = new String[]{"","","","","","","","","","",""};
	private String[] arrDUCHDR2 = new String[]{"","","","","","","","","","",""};
	private String[] arrDUCHDR3 = new String[]{"","","","","","","","","","",""};
	private String[] arrDUCHDR4 = new String[]{"","","","","","","","","","",""};
	private int[] arrDUCHDR_WD = new int[]{0,1,2,3,4,5,6,7,8,9,10};
	private char[] arrDUCHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8','9','0'};
	
	
	private String[] arrSUMHDR = new String[] {"0","1"};
	private int[] arrSUMHDR_WD = new int[] {0,1};
	private char[] arrSUMHDR_PAD = new char[] {'0','1'};

	private JComboBox cmbPRINTERS;
	
	
	private	int intDSRCTR = 0, intBYRCTR = 0, intBLKCTR = 0; 
	//private double[] arrCOLTOT;
	private int intSUMFIX = 2;         // Fixed / predefined number of columns in Summary Table
	private double dblDSRQT = 0.000, dblBYRQT = 0.000, dblTOTQT = 0.000;
	private double dblDSRVL = 0.000, dblBYRVL = 0.000;
	private String  strDSRTP_OLD = "", strDSRCD_OLD = "", strBYRCD_OLD="", strPRDCD_OLD ="";
	private String strSHDRNM = "", strSHDRDS = "",strSHDRNM1 = "", strSHDRDS1 = "";

	private double dblGPPS_QTY_0 = 0.000, dblHIPS_QTY_0 = 0.000, dblSPS_QTY_0 = 0.000, dblOTHER_QTY_0 = 0.000, dblTOTAL_QTY_0 = 0.000;
	private double dblGPPS_QTY_1 = 0.000, dblHIPS_QTY_1 = 0.000, dblSPS_QTY_1 = 0.000, dblOTHER_QTY_1 = 0.000, dblTOTAL_QTY_1 = 0.000;
	private double dblGPPS_VAL_0 = 0.000, dblHIPS_VAL_0 = 0.000, dblSPS_VAL_0 = 0.000, dblOTHER_VAL_0 = 0.000, dblTOTAL_VAL_0 = 0.000;
	private double dblGPPS_VAL_1 = 0.000, dblHIPS_VAL_1 = 0.000, dblSPS_VAL_1 = 0.000, dblOTHER_VAL_1 = 0.000, dblTOTAL_VAL_1 = 0.000;
	
	
	private int intDTLWD = 130;      // Detail report width
	private int intDTLCL = 8;		 // No.of columns in Detail Report
	private int intSUMWD = 130;      // Summary report width
	private int intSUMCL = 8;		 // No.of columns in Summary Report
	private int intREGWD = 160;      // Register report width
	private int intREGCL = 16;		 // No.of columns in Register
	private int intDUCWD = 140;      // Dual Credit report width
	private int intDUCCL = 15;		 // No.of columns in Dual Credit Report

	private int intLINECT=72, intPAGENO=0, intRUNCL=0;
	boolean flgDSRCD_NEW = true, flgBYRCD_NEW = true, flgPRDCD_NEW = true, flgEOFCHK = false;

	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	
	private Vector<String> vtrMKTTP;		
	private Vector<String> vtrRPTWS;		/**Vector for adding elements to cmbRPTWS */
	private Vector<String> vtrRPTSQ;		/**Vector for adding elements to cmbRPTSQ */
	private Vector<String> vtrINVNO;		// Vector for storing invoice list, to print missin invoice numbers
	private JComboBox cmbMKTTP;		
	private JComboBox cmbRPTWS;		/**Combo-box for defining scope of the report */
	private JComboBox cmbRPTSQ;		/**Combo-box for defining order/sequence of the report */
	private JComboBox cmbRPTWS1;
	
	private Hashtable<String,String[]> hstCDTRN;			// Code Transaction details
	private Hashtable<String,String[]> hstIVTRN;			// Inv. Transaction details
	private Hashtable<String,String> hstCODDS;			// Code Description
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstPTMST;			// Party Details
	private Hashtable<String,String> hstLOTNO;			// Party Details
	private Hashtable<String,String> hstDSRNM;			// Distibutor Short Name (as key) & Code (as value)
	private Hashtable<String,String> hstINVNO_REST;	// Hash table for invoice list market types other than selected one.
	private Vector<String> vtrDSRNM;			// Distributor Code & Short Name
	private Object[] arrHSTKEY;			// Object array for getting hash table key in sorted order
	
	mr_qrsla()
	{
		super(2);
		try
		{

			arrDTLHDR[intTB1_CHKFL] = "";
			arrDTLHDR[intTB1_DSRNM] = "Distr.";
			arrDTLHDR[intTB1_BYRNM] = "Buyer";
			arrDTLHDR[intTB1_PRDDS] = "Grade";
			arrDTLHDR[intTB1_INVNO] = "Inv.No.";
			arrDTLHDR[intTB1_INVQT] = "Qty.";
			arrDTLHDR[intTB1_INVDT] = "Inv.Dt.";

			arrDTLHDR_WD[intTB1_CHKFL] = 20;
			arrDTLHDR_WD[intTB1_DSRNM] = 80;
			arrDTLHDR_WD[intTB1_BYRNM] = (80*3)+(8*2);
			arrDTLHDR_WD[intTB1_PRDDS] = 100;
			arrDTLHDR_WD[intTB1_INVNO] = 80;
			arrDTLHDR_WD[intTB1_INVQT] = 80;
			arrDTLHDR_WD[intTB1_INVDT] = 80;
			
			arrDTLHDR_PAD[intTB1_CHKFL] = 'R';
			arrDTLHDR_PAD[intTB1_DSRNM] = 'R';
			arrDTLHDR_PAD[intTB1_BYRNM] = 'R';
			arrDTLHDR_PAD[intTB1_PRDDS] = 'R';
			arrDTLHDR_PAD[intTB1_INVNO] = 'R';
			arrDTLHDR_PAD[intTB1_INVQT] = 'R';	//'L'
			arrDTLHDR_PAD[intTB1_INVDT] = 'R';

			arrREGHDR[intTB31_CHKFL] = "";
			arrREGHDR[intTB31_INVNO] = "Inv.No.";
			arrREGHDR[intTB31_DORNO] = "DO No.";
			arrREGHDR[intTB31_DORDT] = "DO.Date";
			arrREGHDR[intTB31_SALDS] = "Sale Type";
			arrREGHDR[intTB31_DTPDS] = "Del.Type";
			arrREGHDR[intTB31_PMTDS] = "Pmt.Type";
			arrREGHDR[intTB31_PORNO] = "Ord Ref.";
			arrREGHDR[intTB31_BYRNM1] = "----";
			arrREGHDR[intTB31_BYRNM] = "Buyer";
	
			arrREGHDR[intTB32_INVDT] +=  " / Inv.Dt";
			arrREGHDR[intTB32_INDNO] +=  " / Ind.No.";
			arrREGHDR[intTB32_INDDT] +=  " / Ind.Dt.";
			arrREGHDR[intTB32_LRYNO] +=  " / Lorry No.";
			arrREGHDR[intTB32_LR_NO] +=  " / LR No.";
			arrREGHDR[intTB32_CNTDS] +=  " / Cntr.No.";
			arrREGHDR[intTB32_PORDT] +=  " / Ord.Dt.";
			arrREGHDR[intTB32_TRPNM1] +=  " / ----";
			arrREGHDR[intTB32_TRPNM] +=  " / Transporter";

			arrREGHDR[intTB33_BLK01] +=  "";
			arrREGHDR[intTB33_DSRNM] +=  " / Distr.";
			arrREGHDR[intTB33_INVQT] +=  " / Tot.Qty.";
			arrREGHDR[intTB33_ASSVL] +=  " / Tot.A.Val";
			arrREGHDR[intTB33_EXCVL] +=  " / Tot.Exc.";
			arrREGHDR[intTB33_EDHVL] +=  " / Tot.EDC+EHC";
			arrREGHDR[intTB33_FRTVL] +=  " / Tot.Frt";
			arrREGHDR[intTB33_INVVL] +=  " / Inv.Val.";
			arrREGHDR[intTB33_CNSNM] +=  " / Consignee";


			
			arrREGHDR[intTB34_BLK01] +=  "";
			arrREGHDR[intTB34_PRDDS] +=  " / Grade";
			arrREGHDR[intTB34_INVQT] +=  " / Qty.";
			arrREGHDR[intTB34_ASSVL] +=  " / Asbl.Val";
			arrREGHDR[intTB34_EXCVL] +=  " / Excise";
			arrREGHDR[intTB34_EDHVL] +=  " / Ed.Cess+H.Ed.Cess";
			arrREGHDR[intTB34_FRTVL] +=  " / Freight";
			arrREGHDR[intTB34_INVRT] +=  " / Rate";
			arrREGHDR[intTB34_LOTNO] +=  " / Lot Nos.";
			
	
			arrREGHDR1[intTB31_CHKFL] = "";
			arrREGHDR1[intTB31_INVNO] = "Inv.No.";
			arrREGHDR1[intTB31_DORNO] = "DO No.";
			arrREGHDR1[intTB31_DORDT] = "DO.Date";
			arrREGHDR1[intTB31_SALDS] = "Sale Type";
			arrREGHDR1[intTB31_DTPDS] = "Del.Type";
			arrREGHDR1[intTB31_PMTDS] = "Pmt.Type";
			arrREGHDR1[intTB31_PORNO] = "Ord Ref.";
			arrREGHDR1[intTB31_BYRNM1] = "----";
			arrREGHDR1[intTB31_BYRNM] = "Buyer";
	
			arrREGHDR2[intTB32_INVDT] =  "Inv.Dt";
			arrREGHDR2[intTB32_INDNO] =  "Ind.No.";
			arrREGHDR2[intTB32_INDDT] =  "Ind.Dt.";
			arrREGHDR2[intTB32_LRYNO] =  "Lorry No.";
			arrREGHDR2[intTB32_LR_NO] =  "LR No.";
			arrREGHDR2[intTB32_CNTDS] =  "Cntr.No.";
			arrREGHDR2[intTB32_PORDT] =  "Ord.Dt.";
			arrREGHDR2[intTB32_TRPNM1] =  "----";
			arrREGHDR2[intTB32_TRPNM] =  "Transporter";

			arrREGHDR3[intTB33_BLK01] =  "";
			arrREGHDR3[intTB33_DSRNM] =  "Distr.";
			arrREGHDR3[intTB33_INVQT] =  "Tot.Qty.";
			arrREGHDR3[intTB33_ASSVL] =  "Tot.A.Val";
			arrREGHDR3[intTB33_EXCVL] =  "Tot.Exc.";
			arrREGHDR3[intTB33_EDHVL] =  "Tot.EDC+EHC";
			arrREGHDR3[intTB33_FRTVL] =  "Tot.Frt.";
			arrREGHDR3[intTB33_INVVL] =  "Inv.Val.";
			arrREGHDR3[intTB33_CNSNM] =  "Consignee";

			arrREGHDR4[intTB34_BLK01] =  "";
			arrREGHDR4[intTB34_PRDDS] =  "Grade";
			arrREGHDR4[intTB34_INVQT] =  "Qty.";
			arrREGHDR4[intTB34_ASSVL] =  "Asbl.Val";
			arrREGHDR4[intTB34_EXCVL] =  "Excise";
			arrREGHDR4[intTB34_EDHVL] =  "Ed.Cess+H.Ed.Cess";
			arrREGHDR4[intTB34_FRTVL] =  "Freight";
			arrREGHDR4[intTB34_INVRT] =  "Rate";
			arrREGHDR4[intTB34_LOTNO] =  "Lot Nos.";
			
			arrREGHDR_WD[intTB31_CHKFL] = 5;
			arrREGHDR_WD[intTB31_INVNO] = 90;
			arrREGHDR_WD[intTB31_DORNO] = 90;
			arrREGHDR_WD[intTB31_DORDT] = 90;
			arrREGHDR_WD[intTB31_SALDS] = 90;
			arrREGHDR_WD[intTB31_DTPDS] = 90;
			arrREGHDR_WD[intTB31_PMTDS] = 90;
			arrREGHDR_WD[intTB31_PORNO] = 90;
			arrREGHDR_WD[intTB31_BYRNM1] = 90;
			arrREGHDR_WD[intTB31_BYRNM] = 90*3+9*2;
	

			arrREGHDR_PAD[intTB31_CHKFL] = 'R';
			arrREGHDR_PAD[intTB31_INVNO] = 'R';
			arrREGHDR_PAD[intTB31_DORNO] = 'R';
			arrREGHDR_PAD[intTB31_DORDT] = 'L';
			arrREGHDR_PAD[intTB31_SALDS] = 'L';
			arrREGHDR_PAD[intTB31_DTPDS] = 'L';
			arrREGHDR_PAD[intTB31_PMTDS] = 'L';
			arrREGHDR_PAD[intTB31_PORNO] = 'L';
			arrREGHDR_PAD[intTB31_BYRNM1] = 'L';
			arrREGHDR_PAD[intTB31_BYRNM] = 'R';
			
			
			arrDUCHDR[intTB41_CHKFL] = "";
			arrDUCHDR[intTB41_BYRNM] = "Buyer";
			arrDUCHDR[intTB41_DSRNM] = "Distr.";
			arrDUCHDR[intTB41_INVNO] = "Inv No";
			arrDUCHDR[intTB41_INVVL] = "Inv.Amt.";
			arrDUCHDR[intTB41_INVCP] = "Inv.CP";
			arrDUCHDR[intTB41_ACCCP] = "Acc.CP";
			arrDUCHDR[intTB41_DORNO] = "D.O.No.";
			arrDUCHDR[intTB41_INDNO] = "Ind.No.";
	
			arrDUCHDR[intTB42_BLK01] +=  "";
			arrDUCHDR[intTB42_BLK02] +=  "";
			arrDUCHDR[intTB42_INVDT] +=  "Inv.Dt.";
			arrDUCHDR[intTB42_BLK04] +=  "";
			arrDUCHDR[intTB42_INVDD] +=  "Inv.DDt.";
			arrDUCHDR[intTB42_ACCDD] +=  "Acc.DDt.";
			arrDUCHDR[intTB42_DORDT] +=  "D.O.Dt.";
			arrDUCHDR[intTB42_INDDT] +=  "Ind.Dt.";

	
			arrDUCHDR1[intTB41_CHKFL] = "";
			arrDUCHDR1[intTB41_BYRNM] = "Buyer";
			arrDUCHDR1[intTB41_DSRNM] = "Distr.";
			arrDUCHDR1[intTB41_INVNO] = "Inv No";
			arrDUCHDR1[intTB41_INVVL] = "Inv.Amt.";
			arrDUCHDR1[intTB41_INVCP] = "Inv.CP";
			arrDUCHDR1[intTB41_ACCCP] = "Acc.CP";
			arrDUCHDR1[intTB41_DORNO] = "D.O.No.";
			arrDUCHDR1[intTB41_INDNO] = "Ind.No.";
	
			arrDUCHDR2[intTB42_BLK01] =  "";
			arrDUCHDR2[intTB42_BLK02] =  "";
			arrDUCHDR2[intTB42_INVDT] =  "Inv.Dt";
			arrDUCHDR2[intTB42_BLK04] =  "";
			arrDUCHDR2[intTB42_INVDD] =  "Inv.DDt";
			arrDUCHDR2[intTB42_ACCDD] =  "Acc.DDt";
			arrDUCHDR2[intTB42_DORDT] =  "D.O.Dt.";
			arrDUCHDR2[intTB42_INDDT] =  "Ind.Dt.";


			
			arrDUCHDR_WD[intTB41_CHKFL] = 15;
			arrDUCHDR_WD[intTB41_BYRNM] = 90*2 + 9*1;
			arrDUCHDR_WD[intTB41_DSRNM] = 90;
			arrDUCHDR_WD[intTB41_INVNO] = 90;
			arrDUCHDR_WD[intTB41_INVVL] = 90;
			arrDUCHDR_WD[intTB41_INVCP] = 90;
			arrDUCHDR_WD[intTB41_ACCCP] = 90;
			arrDUCHDR_WD[intTB41_DORNO] = 90;
			arrDUCHDR_WD[intTB41_INDNO] = 90;
	

			arrDUCHDR_PAD[intTB41_CHKFL] = 'R';
			arrDUCHDR_PAD[intTB41_BYRNM] = 'R';
			arrDUCHDR_PAD[intTB41_DSRNM] = 'R';
			arrDUCHDR_PAD[intTB41_INVNO] = 'R';
			arrDUCHDR_PAD[intTB41_INVVL] = 'L';
			arrDUCHDR_PAD[intTB41_INVCP] = 'L';
			arrDUCHDR_PAD[intTB41_ACCCP] = 'L';
			arrDUCHDR_PAD[intTB41_DORNO] = 'L';
			arrDUCHDR_PAD[intTB41_INDNO] = 'L';
			
			
			tbpMAIN = new JTabbedPane();
			pnlDTLRPT = new JPanel(null);
			pnlSUMRPT = new JPanel(null);
			pnlREGRPT = new JPanel(null);
			pnlDUCRPT = new JPanel(null);
			txtSPCCT = new JTextField();
			txtSPCCT1 = new JTextField();
			
			hstCDTRN = new Hashtable<String,String[]>();
			hstIVTRN = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstPRMST = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			hstLOTNO = new Hashtable<String,String>();
			hstDSRNM = new Hashtable<String,String>();
			hstINVNO_REST = new Hashtable<String,String>();
			
			vtrMKTTP = new Vector<String>();
			vtrRPTWS = new Vector<String>();
			vtrRPTSQ = new Vector<String>();
			vtrINVNO = new Vector<String>();
			
			// Radio buttons for scope selection for the report
			//rdbALLWS = new JRadioButton("OverAll");
			//rdbRGNWS = new JRadioButton("Region");
			//rdbZONWS = new JRadioButton("Zone");
			//rdbGRDWS = new JRadioButton("Grade");
			//rdbDSRWS = new JRadioButton("Distributor");
			//rdbBYRWS = new JRadioButton("Buyer");
			//rdbSALWS = new JRadioButton("Sale Type");
			//rdbPRDWS = new JRadioButton("Prod.Cat.");
			//btgRPTWS = new ButtonGroup();

			
			// Radio buttons for Order By selection in detail report
			//rdbDFTSQ = new JRadioButton("Default");
			//rdbGRDSQ = new JRadioButton("Grade");
			//rdbDSRSQ = new JRadioButton("Distributor");
			//rdbBYRSQ = new JRadioButton("Buyer");
			//rdbINOSQ = new JRadioButton("Inv.No.");
			//rdbIDTSQ = new JRadioButton("Inv.Date");
			//btgRPTSQ = new ButtonGroup();

			lblSPCCT  = new JLabel("Specify");
			lblSPCDS  = new JLabel("");
			lblSPCCT1  = new JLabel("Specify");
			lblSPCDS1  = new JLabel("");
			lblMKTTP  = new JLabel("Market Type");
			lblRPTWS  = new JLabel("Scope1");
			lblRPTWS1  = new JLabel("Scope2");
			lblRPTSQ  = new JLabel("Order By");
			chkSTOTFL = new JCheckBox("With Sub-Total");
			chkCSTKFL = new JCheckBox("Consg.Stockist");

			btnDTLPRINT = new JButton("Detail");
			btnSUMPRINT = new JButton("Summary");
			btnREGPRINT = new JButton("Register");
			btnDUCPRINT = new JButton("Dual Credit");
			//rdbALLWS.setForeground(Color.blue);
			//rdbDFTSQ.setForeground(Color.blue);
			lblSPCCT.setForeground(Color.blue);
			lblSPCDS.setForeground(Color.blue);
			lblSPCCT1.setForeground(Color.blue);
			lblSPCDS1.setForeground(Color.blue);
			
			lblMKTTP.setForeground(Color.blue);
			lblRPTWS.setForeground(Color.blue);
			lblRPTWS1.setForeground(Color.blue);
			lblRPTSQ.setForeground(Color.blue);
			

			crtPRMST();
			
			setMatrix(20,8);
			//btgRPTWS.add(rdbALLWS);btgRPTWS.add(rdbRGNWS);btgRPTWS.add(rdbZONWS);btgRPTWS.add(rdbGRDWS);btgRPTWS.add(rdbDSRWS);btgRPTWS.add(rdbBYRWS);btgRPTWS.add(rdbSALWS);btgRPTWS.add(rdbPRDWS);
			//btgRPTSQ.add(rdbDFTSQ);btgRPTSQ.add(rdbGRDSQ);btgRPTSQ.add(rdbDSRSQ);btgRPTSQ.add(rdbBYRSQ);btgRPTSQ.add(rdbINOSQ);btgRPTSQ.add(rdbIDTSQ);

			add(lblMKTTP,2,1,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),3,1,1,1,this,'L');
			
			add(lblRPTWS,2,2,1,1,this,'L');
			add(cmbRPTWS=new JComboBox(),3,2,1,2,this,'L');
	        add(lblRPTWS1,2,4,1,1,this,'L');
			add(cmbRPTWS1=new JComboBox(),3,4,1,2,this,'L');
        
          	add(lblRPTSQ,2,6,1,1,this,'L');
			add(cmbRPTSQ=new JComboBox(),3,6,1,2,this,'L');
			
			
			add(lblSPCCT,4,1,1,1,this,'L');
			add(txtSPCCT,4,2,1,1,this,'L');
			add(lblSPCDS,5,1,1,3,this,'L');
            add(lblSPCCT1,4,3,1,1,this,'L');
			add(txtSPCCT1,4,4,1,1,this,'L');
			add(lblSPCDS1,5,4,1,3,this,'L');
			add(chkSTOTFL,2,7,1,2,this,'L');
			add(chkCSTKFL,4,7,1,2,this,'L');
		
			add(new JLabel("Print"),6,3,1,1,this,'L');
			add(btnDTLPRINT,6,4,1,1,this,'L');
			add(btnSUMPRINT,6,5,1,1,this,'L');
			add(btnREGPRINT,6,6,1,1,this,'L');
			add(btnDUCPRINT,6,7,1,1,this,'L');
			//chkSELFML = new JCheckBox("Self Mail");
			//add(chkSELFML,6,8,1,1,this,'L');
			txtSPCCT.setText("");
           	txtSPCCT.setVisible(false);
            txtSPCCT1.setText("");			
            txtSPCCT1.setVisible(false);
			lblSPCCT.setText("");
			lblSPCDS.setText("");
			lblSPCCT1.setText("");
			lblSPCDS1.setText("");
			tblSUMRPT = crtTBLPNL1(pnlSUMRPT,new String[]{"","Grade","Distr.1","Distr.2.","Distr.3","Total"},500,2,1,9.1,7.9,new int[]{20,65,60,60,60,100},new int[]{0});
			tblDTLRPT = crtTBLPNL1(pnlDTLRPT,arrDTLHDR,500,2,1,9.1,7.9,arrDTLHDR_WD,new int[]{0});  //,"Amount"
			tblREGRPT = crtTBLPNL1(pnlREGRPT,arrREGHDR,500,2,1,9.1,7.9,arrREGHDR_WD,new int[]{0});  //,"Amount"
			tblDUCRPT = crtTBLPNL1(pnlDUCRPT,arrDUCHDR,500,2,1,9.1,7.9,arrDUCHDR_WD,new int[]{0});  //,"Amount"
			
			tbpMAIN.addTab("Detail",pnlDTLRPT);
			tbpMAIN.addTab("Summary",pnlSUMRPT);
			tbpMAIN.addTab("Register",pnlREGRPT);
			tbpMAIN.addTab("Dual Credit",pnlDUCRPT);
			
			add(tbpMAIN,7,1,13,8,this,'L');
			tblDTLRPT.addMouseListener(this);
			tblSUMRPT.addMouseListener(this);
			tblREGRPT.addMouseListener(this);
			tblDUCRPT.addMouseListener(this);

			setVTRMKTTP();
			setVTRRPTWS();
			setVTRRPTSQ();
			
			remove(M_cmbDESTN);
			M_cmbDESTN.setVisible(true);
			M_vtrSCCOMP.remove(M_cmbDESTN);
			//chkSELFML.setSelected(false);
        	updateUI();
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"mr_qrsla");}
	}


	/** Adding elements to vtrMKTTP, 
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
			vtrMKTTP.addElement("07 XPS");		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRMKTTP");}
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
			vtrRPTWS.addElement(strLOTWS);		
			vtrRPTWS.addElement(strTRPWS);		
			vtrRPTWS.addElement(strCNSWS);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRRPTWS");}
	}


	/** Adding elements to vtrRPTSQ, for defining order/sequence of the report
	 */
	void setVTRRPTSQ()
	{
		try
		{
			vtrRPTSQ.clear();
			vtrRPTSQ.addElement(strDFTSQ);		
			vtrRPTSQ.addElement(strGRDSQ);		
			vtrRPTSQ.addElement(strDSRSQ);		
			vtrRPTSQ.addElement(strBYRSQ);		
			vtrRPTSQ.addElement(strINOSQ);		
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"setVTRRPTSQ");}
	}
	
	
	
	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		super.clrCOMP();
		chkSTOTFL.setSelected(true);
		chkSTOTFL.setSelected(false);
		M_txtFMDAT.setText("01"+cl_dat.M_txtCLKDT_pbst.getText().substring(2,10));
		M_txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText());
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
				chkSTOTFL.setSelected(true);
				chkSTOTFL.setSelected(false);
				setCMBVL(cmbMKTTP,vtrMKTTP);
				setCMBVL(cmbRPTWS,vtrRPTWS);
				setCMBVL(cmbRPTWS1,vtrRPTWS);
				setCMBVL(cmbRPTSQ,vtrRPTSQ);
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
			if(M_objSOURC == cmbRPTWS1)
				getSUBHDR();	
			if(M_objSOURC == M_txtFMDAT)
				M_txtTODAT.requestFocus();
			if(M_objSOURC == btnDTLPRINT)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_dtsla.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtDTLRPT();
					//exeSELFML(strRESSTR,"Sales Analysis Detail");
					exePRINT1();
				}
			if(M_objSOURC == btnSUMPRINT)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_smsla.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtSUMRPT();
					//exeSELFML(strRESSTR,"Sales Analysis Summary");
					exePRINT1();
				}
			if(M_objSOURC == btnREGPRINT)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_rgsla.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtREGRPT();
					//exeSELFML(strRESSTR,"Sales Register");
					exePRINT1();
				}
			if(M_objSOURC == btnDUCPRINT)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_dcmis.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtDUCRPT();
					exePRINT1();
				}
		}
		catch (Exception L_EX) {setMSG(L_EX,"acionPerformed");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
		
	}

/**
 */
 	/** Self E-mailing the report file
	 ///
	private void exeSELFML(String LP_DOCFL,String LP_MSGDS)
	{
		try
		{
			String L_strDOCEXE = "c:\\windows\\wordpad.exe";
			if(chkSELFML.isSelected())
			{
				String L_strEML = "";
				cl_eml ocl_eml = new cl_eml();
				M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+cl_dat.M_strUSRCD_pbst+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null && M_rstRSSET.next())
				{
					L_strEML = M_rstRSSET.getString("US_EMLRF");
					if(L_strEML.length() >0)
						ocl_eml.sendfile(L_strEML,LP_DOCFL,LP_MSGDS,LP_MSGDS);
				}
			}
		}
		catch (Exception L_EX) {setMSG(L_EX,"exeSELFML");}
	}
*/	
	
	
	/** method from cl_rbase overidden, as Printing is taken
	 * after selecting Scrren Display option
	 */
/*	String[] getPRINTERS()
	{
		
					flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//					flavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_US_ASCII;
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

	/**
	 */
	private void dspINVDUC()
	{
		try
		{
			int i,j,k, L_intDSRCTR ,L_intBYRCTR;
			ResultSet rstRSSET = null;
			
			
			M_strSQLQRY = "select distinct ivt_byrcd,ivt_dsrtp, ivt_dsrcd, ivt_invno, isnull(ivt_invvl,0) ivt_invvl, isnull(in_cptvl,0) ivt_invcp, isnull(in_aptvl,0) ivt_acccp, ivt_dorno, ivt_indno, CONVERT(varchar,ivt_invdt,103) ivt_invdt, CONVERT(varchar,day(ivt_invdt)+isnull(in_cptvl,0),103) ivt_invdd,CONVERT(varchar,day(ivt_invdt)+isnull(in_aptvl,0),103) ivt_accdd, dot_dordt ivt_dordt, in_inddt ivt_inddt from mr_ivtrn, mr_dotrn, mr_inmst where ivt_cmpcd = dot_cmpcd and ivt_mkttp=dot_mkttp and ivt_dorno=dot_dorno and ivt_cmpcd = in_cmpcd and ivt_mkttp=in_mkttp and ivt_indno=in_indno and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_dsrtp = '"+(chkCSTKFL.isSelected() ? "G" : "D")+"' and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "";

			M_strSQLQRY += " AND isnull(in_cptvl,0)<>isnull(in_aptvl,0) and "+strWHRSTR;
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			i=0; j=0; L_intDSRCTR = 0; L_intBYRCTR = 0;
			dblTOTQT = 0.000;
			flgEOFCHK = false;
			tblDUCRPT.clrTABLE();
			String L_strINVNO = "", L_strINVNO_OLD = "";
			while(true)
			{
				L_strINVNO = getRSTVAL(rstRSSET,"IVT_INVNO","C");
				if(!L_strINVNO.equals(L_strINVNO_OLD))
				{
					L_strINVNO_OLD = L_strINVNO;
					i+=1; 
					tblDUCRPT.setValueAt(getPTMST("C",getRSTVAL(rstRSSET,"IVT_BYRCD","C"),"PT_PRTNM"),i,intTB41_BYRNM);
					tblDUCRPT.setValueAt(getPTMST(getRSTVAL(rstRSSET,"IVT_DSRTP","C"),getRSTVAL(rstRSSET,"IVT_DSRCD","C"),"PT_PRTNM"),i,intTB41_DSRNM);
					tblDUCRPT.setValueAt(L_strINVNO+"    ",i,intTB41_INVNO);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVVL","N")+"    ",i,intTB41_INVVL);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVCP","C")+"    ",i,intTB41_INVCP);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_ACCCP","C")+"    ",i,intTB41_ACCCP);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_DORNO","C")+"    ",i,intTB41_DORNO);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INDNO","C")+"    ",i,intTB41_INDNO);
					i+=1;

					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVDT","D"),i,intTB42_INVDT);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVDD","C"),i,intTB42_INVDD);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_ACCDD","C"),i,intTB42_ACCDD);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_DORDT","C"),i,intTB42_DORDT);
					tblDUCRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INDDT","C"),i,intTB42_INDDT);
					i+=1;
				
				}
				if(!rstRSSET.next())
					break;
			}
			flgEOFCHK = true;

			rstRSSET.close();
		
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspINVDUC");}
	}

	
	
/**
 */
private void exePRINT1()
{
	try
	{
	//	M_cmbDESTN.removeAllItems();
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
		if(M_objSOURC==txtSPCCT && L_KE.getKeyCode()==L_KE.VK_ENTER)
			getSUBHDR();
		if(M_objSOURC==txtSPCCT1 && L_KE.getKeyCode()==L_KE.VK_ENTER)
			getSUBHDR();
		if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
			    if(M_objSOURC == txtSPCCT)
			    {
				    M_strHLPFLD = "txtSPCCT";
    				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN' and cmt_codcd in (select cmt_chp02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+"))  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strZONWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strGRDWS))
    					cl_hlp("Select PR_PRDCD, PR_PRDDS from CO_PRMST where SUBSTRING(pr_prdcd,1,2) in ('51','52','53') and pr_prdcd in (select distinct ivt_prdcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by PR_PRDDS" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strDSRWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp = '"+(chkCSTKFL.isSelected() ? "G" : "D")+"'"+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and pt_prtcd='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_dsrcd from mr_ivtrn where  "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strBYRWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' "+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and "+(cl_dat.M_strUSRCD_pbst.substring(0,1).equals("D") ? "PT_DSRCD" : "PT_CNSRF")+"='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_byrcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd in (select ivt_saltp from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strPMTWS))
    				//{System.out.println("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXPMT' and cmt_codcd in (select ivt_pmtcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD");}
	    				 cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXPMT' and cmt_codcd in (select ivt_pmtcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
    					cl_hlp("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and SUBSTRING(cmt_codcd,1,4) in (select distinct SUBSTRING(ivt_prdcd,1,4) ivt_prdcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+")  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strLOTWS))
    					{cl_hlp("Select distinct IST_LOTNO,IST_ISSDT from FG_ISTRN where IST_ISSTP='10' and ist_issno in (select ivt_ladno from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by ist_lotno",2,1,new String[] {"Lot No.","Desp.Date"},2,"CT");}
    				if(cmbRPTWS.getSelectedItem().toString().equals(strTRPWS))
    					cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='T' and pt_prtcd in (select distinct ivt_trpcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS.getSelectedItem().toString().equals(strCNSWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' "+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and "+(cl_dat.M_strUSRCD_pbst.substring(0,1).equals("D") ? "PT_DSRCD" : "PT_CNSRF")+"='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_cnscd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    					//cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' and pt_prtcd in (select distinct ivt_cnscd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
			    }
			    else if(M_objSOURC == txtSPCCT1)
			    {
				    M_strHLPFLD = "txtSPCCT1";
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strRGNWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN' and cmt_codcd in (select cmt_chp02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+"))  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strZONWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strGRDWS))
    					cl_hlp("Select PR_PRDCD, PR_PRDDS from CO_PRMST where SUBSTRING(pr_prdcd,1,2) in ('51','52','53') and pr_prdcd in (select distinct ivt_prdcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by PR_PRDDS" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strDSRWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp = '"+(chkCSTKFL.isSelected() ? "G" : "D")+"'"+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and pt_prtcd='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_dsrcd from mr_ivtrn where  "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strBYRWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' "+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and "+(cl_dat.M_strUSRCD_pbst.substring(0,1).equals("D") ? "PT_DSRCD" : "PT_CNSRF")+"='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_byrcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strSALWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd in (select ivt_saltp from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strPMTWS))
	    				 cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXPMT' and cmt_codcd in (select ivt_pmtcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strPRDWS))
    					cl_hlp("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and SUBSTRING(cmt_codcd,1,4) in (select distinct SUBSTRING(ivt_prdcd,1,4) ivt_prdcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+")  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strLOTWS))
    					{cl_hlp("Select distinct IST_LOTNO,IST_ISSDT from FG_ISTRN where IST_ISSTP='10' and ist_issno in (select ivt_ladno from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by ist_lotno",2,1,new String[] {"Lot No.","Desp.Date"},2,"CT");}
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strTRPWS))
    					cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='T' and pt_prtcd in (select distinct ivt_trpcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strCNSWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' "+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and "+(cl_dat.M_strUSRCD_pbst.substring(0,1).equals("D") ? "PT_DSRCD" : "PT_CNSRF")+"='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_cnscd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
/*    				if(cmbRPTWS1.getSelectedItem().toString().equals(strRGNWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN' and cmt_codcd in (select cmt_chp02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+"))  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strZONWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strGRDWS))
    					cl_hlp("Select PR_PRDCD, PR_PRDDS from CO_PRMST where SUBSTRING(pr_prdcd,1,2) in ('51','52','53') and pr_prdcd in (select distinct ivt_prdcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PR_PRDDS" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strDSRWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp = '"+(chkCSTKFL.isSelected() ? "G" : "D")+"'"+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and pt_prtcd='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_dsrcd from mr_ivtrn where  "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strBYRWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' "+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and "+(cl_dat.M_strUSRCD_pbst.substring(0,1).equals("D") ? "PT_DSRCD" : "PT_CNSRF")+"='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_byrcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strSALWS))
    					cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd in (select ivt_saltp from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strPMTWS))
	    				 cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXPMT' and cmt_codcd in (select ivt_pmtcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strPRDWS))
    					cl_hlp("Select SUBSTRING(CMT_CODCD,1,4) CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and SUBSTRING(cmt_codcd,1,4) in (select distinct SUBSTRING(ivt_prdcd,1,4) ivt_prdcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strLOTWS))
    					{cl_hlp("Select distinct IST_LOTNO,IST_ISSDT from FG_ISTRN where IST_ISSTP='10' and ist_issno in (select ivt_ladno from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by ist_lotno",2,1,new String[] {"Lot No.","Desp.Date"},2,"CT");}
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strTRPWS))
    					cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='T' and pt_prtcd in (select distinct ivt_trpcd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
    				if(cmbRPTWS1.getSelectedItem().toString().equals(strCNSWS))
						cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' "+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and "+(cl_dat.M_strUSRCD_pbst.substring(0,1).equals("D") ? "PT_DSRCD" : "PT_CNSRF")+"='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and pt_prtcd in (select distinct ivt_cnscd from mr_ivtrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTNM" ,2,1,new String[] {"Code","Name"},2,"CT");
					*/
    		    }

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
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtSPCCT"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtSPCCT"))
			{
				txtSPCCT.setText(L_STRTKN.nextToken());
				lblSPCDS.setText(L_STRTKN.nextToken());
			}
		}
		if(M_strHLPFLD.equals("txtSPCCT1"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtSPCCT1"))
			{
				txtSPCCT1.setText(L_STRTKN.nextToken());
				lblSPCDS1.setText(L_STRTKN.nextToken());
			}
		}
	}
	

	/**
	 */
	private void dspINVDTL()
	{
		try
		{
			ResultSet rstRSSET = null;
			//System.out.println("-003");
			strORDBY = "";
			if(cmbRPTWS.getSelectedItem().toString().equals(strDFTSQ))
				strORDBY = " order by ivt_dsrcd,ivt_byrcd,ivt_prdcd,ivt_invno";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strGRDSQ))
				strORDBY = " order by  ivt_prdcd ";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strDSRSQ))
				strORDBY = " order by  ivt_dsrcd ";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strBYRSQ))
				strORDBY = " order by  ivt_byrcd ";
			else if(cmbRPTSQ.getSelectedItem().toString().equals(strINOSQ))
				strORDBY = " order by  ivt_invno ";
			M_strSQLQRY = "select ivt_dsrtp,ivt_dsrcd,ivt_byrcd,ivt_invno,CONVERT(varchar,ivt_invdt,103) ivt_invdt,ivt_prdcd,ivt_pkgtp,ivt_dtpcd, ivt_saltp, isnull(ivt_invrt,0) ivt_invrt, ivt_cnscd, ivt_trpcd, ivt_lryno, ivt_cntds, ivt_lr_no,ivt_dorno,ivt_dordt, ivt_indno, ivt_ladno, ivt_laddt, isnull(ivt_assvl,0) ivt_assvl, isnull(ivt_excvl,0) ivt_excvl, isnull(ivt_edcvl,0)+isnull(ivt_ehcvl,0) ivt_edhvl, isnull(ivt_frtvl,0) ivt_frtvl, isnull(ivt_invvl,0) ivt_invvl, isnull(ivt_invqt,0) ivt_invqt from mr_ivtrn where "+strWHRSTR+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+ strORDBY;      //,sum(isnull(ivt_invvl,0)) ivt_invvl
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			int i,j,k, L_intDSRCTR ,L_intBYRCTR;
			i=0; j=0; L_intDSRCTR = 0; L_intBYRCTR = 0;
			dblDSRQT = 0.000; dblBYRQT = 0.000;
			dblDSRVL = 0.000; dblBYRVL = 0.000;
			dblTOTQT = 0.000;
			strDSRTP_OLD = ""; strDSRCD_OLD = ""; strBYRCD_OLD=""; strPRDCD_OLD="";
			flgDSRCD_NEW = true; flgBYRCD_NEW = true;flgPRDCD_NEW = true; flgEOFCHK = false;
			tblDTLRPT.clrTABLE();

			//System.out.println("-004");
			while(true)
			{
				//System.out.println("i = "+i);
				i = chkDTLHDR(rstRSSET,i);    // Printing Category Total
					
				//System.out.println("01");
				tblDTLRPT.setValueAt((flgDSRCD_NEW == true ? getPTMST(getRSTVAL(rstRSSET,"IVT_DSRTP","C"),getRSTVAL(rstRSSET,"IVT_DSRCD","C"),"PT_SHRNM") : ""),i,intTB1_DSRNM);
				tblDTLRPT.setValueAt((flgBYRCD_NEW == true ? getPTMST("C",getRSTVAL(rstRSSET,"IVT_BYRCD","C"),"PT_PRTNM") : "---\"---"),i,intTB1_BYRNM);
				tblDTLRPT.setValueAt((flgPRDCD_NEW == true ? getPRMST(getRSTVAL(rstRSSET,"IVT_PRDCD","C"),"PR_PRDDS") : "--\"--"),i,intTB1_PRDDS);
				//System.out.println("02");
				tblDTLRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVQT","N"),i,intTB1_INVQT);
				tblDTLRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVNO","C")+"  ",i,intTB1_INVNO);
				tblDTLRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVDT","D"),i,intTB1_INVDT);
				//System.out.println("03");

				dblTOTQT += Double.parseDouble(getRSTVAL(rstRSSET,"IVT_INVQT","N"));
				dblDSRQT += Double.parseDouble(getRSTVAL(rstRSSET,"IVT_INVQT","N"));
				dblBYRQT += Double.parseDouble(getRSTVAL(rstRSSET,"IVT_INVQT","N"));
				//System.out.println("invqt :"+getRSTVAL(rstRSSET,"IVT_INVQT","N"));
				i+=1;intDSRCTR+=1;intBYRCTR+=1;
				if(!rstRSSET.next())
					break;
			}
			flgEOFCHK = true;
			i = chkDTLHDR(rstRSSET,i);		// Printing Category Total at the end
			//System.out.println("-004a");
			
			tblDTLRPT.setRowColor(i,Color.blue);

			//System.out.println("-004b");
			tblDTLRPT.setValueAt("Grand Total : ",i,intTB1_PRDDS);
			//System.out.println("-004c");
			tblDTLRPT.setValueAt(setNumberFormat(dblTOTQT,3),i,intTB1_INVQT);
			//System.out.println("-005");

			rstRSSET.close();
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspINVDTL");}
	}
	

	
	/**
	 */
	private void dspINVSUM()
	{
		try
		{
			ResultSet rstRSSET = null;
			int i,j,k, L_intDSRCTR ,L_intBYRCTR;
			M_strSQLQRY = "select ivt_prdcd,ivt_dsrtp,ivt_dsrcd,sum(isnull(ivt_invqt,0)) ivt_invqt,sum(isnull(ivt_rsnvl,0)) ivt_rsnvl from mr_ivtrn where "+strWHRSTR+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" group by ivt_prdcd,ivt_dsrtp,ivt_dsrcd order by ivt_prdcd,ivt_dsrtp,ivt_dsrcd";      //,sum(isnull(ivt_invvl,0)) ivt_invvl
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			

			//System.out.println("011");
			setHST_ARR(hstDSRNM);
			double[] arrCOLTOT = new double[arrHSTKEY.length+intSUMFIX+1];
			for(k=0;k<arrCOLTOT.length;k++)
				arrCOLTOT[k]=0.000;
			boolean L_flgEOF = false;
			tblSUMRPT.clrTABLE(); i=0; j=0; double L_dblGRDQT = 0.000; String L_strPRDCD_OLD = ""; // setting intial values
			//System.out.println("012");
			while(true)
			{
				L_strPRDCD_OLD = getRSTVAL(rstRSSET,"IVT_PRDCD","C");L_dblGRDQT = 0.000;
				tblSUMRPT.setValueAt(getPRMST(getRSTVAL(rstRSSET,"IVT_PRDCD","C"),"PR_PRDDS"),i,1);
				while(L_strPRDCD_OLD.equals(getRSTVAL(rstRSSET,"IVT_PRDCD","C")))
					{
						j = getDSRCOL(getPTMST(getRSTVAL(rstRSSET,"IVT_DSRTP","C"),getRSTVAL(rstRSSET,"IVT_DSRCD","C"),"PT_SHRNM"))+2;
						tblSUMRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVQT","N"),i,j);
						L_dblGRDQT += Double.parseDouble(getRSTVAL(rstRSSET,"IVT_INVQT","N"));
						arrCOLTOT[j] += Double.parseDouble(getRSTVAL(rstRSSET,"IVT_INVQT","N"));
						if(!rstRSSET.next())
							{L_flgEOF = true; break;}
					}
					tblSUMRPT.setValueAt(setNumberFormat(L_dblGRDQT,3),i,arrHSTKEY.length+intSUMFIX);
					arrCOLTOT[arrCOLTOT.length-1] += L_dblGRDQT;
					if(L_flgEOF)
						break;
					i+=1;
			}
			//System.out.println("013");
			i+=1;
			tblSUMRPT.setRowColor(i,Color.blue);

			tblSUMRPT.setValueAt("Total:",i,1);
			for(k = intSUMFIX; k<arrCOLTOT.length; k++)
					tblSUMRPT.setValueAt(setNumberFormat(arrCOLTOT[k],3),i,k);
			//System.out.println("014");
				
			rstRSSET.close();
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspINVSUM");}
	}
	
	/**
	 */
	private void crtHSTINV_REST()
	{
		hstINVNO_REST.clear();
		ResultSet L_rstRSSET = null;
		try
		{
			M_strSQLQRY = "select ivt_invno,ivt_mkttp from mr_ivtrn where ivt_invno is not null "+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and "+strWHRSTR_REST;      //,sum(isnull(ivt_invvl,0)) ivt_invvl
			//System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			while(true)
			{
				hstINVNO_REST.put(L_rstRSSET.getString("ivt_invno"),L_rstRSSET.getString("ivt_mkttp"));
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
		}
		catch (Exception L_EX) {setMSG(L_EX,"crtHSTINV_REST");}
	}
	
	
	
	/**
	 */
	private void dspINVREG()
	{
		try
		{
			int i,j,k, L_intDSRCTR ,L_intBYRCTR;
			crtHSTINV_REST();
			vtrINVNO.clear();
			ResultSet rstRSSET = null;
			M_strSQLQRY = "select ivt_invno,ivt_dorno, dot_dordt ivt_dordt, ivt_saltp, ivt_dtpcd, ivt_pmtcd, ivt_porno,ivt_byrcd, CONVERT(varchar,ivt_invdt,103) ivt_invdt,ivt_indno, in_inddt ivt_inddt, ivt_lryno, ivt_lr_no, ivt_cntds, in_pordt ivt_pordt,ivt_trpcd,ivt_dsrtp,ivt_dsrcd, ivt_cnscd, isnull(ivt_invvl,0) ivt_invvl, sum(isnull(ivt_invqt,0)) ivt_invqt,sum(isnull(ivt_assvl,0)) ivt_assvl, sum(isnull(ivt_excvl,0)) ivt_excvl, sum(isnull(ivt_edcvl,0)+isnull(ivt_ehcvl,0)) ivt_edhvl, sum(isnull(ivt_frtvl,0)) ivt_frtvl from mr_ivtrn,mr_dotrn,mr_inmst where "+strWHRSTR+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+" and ivt_cmpcd=dot_cmpcd and ivt_mkttp=dot_mkttp and ivt_dorno=dot_dorno and ivt_prdcd=dot_prdcd and ivt_cmpcd=in_cmpcd and ivt_mkttp=in_mkttp and ivt_indno=in_indno group by ivt_invno,ivt_dorno, dot_dordt, ivt_saltp, ivt_dtpcd, ivt_pmtcd, ivt_porno,ivt_byrcd, CONVERT(varchar,ivt_invdt,103),ivt_indno, in_inddt, ivt_lryno, ivt_lr_no, ivt_cntds, in_pordt,ivt_trpcd,ivt_dsrtp,ivt_dsrcd, ivt_cnscd, ivt_invvl order by ivt_invno";      //,sum(isnull(ivt_invvl,0)) ivt_invvl
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			crtIVTRN(rstRSSET);
			
			
			dblGPPS_QTY_0 = 0.000; dblHIPS_QTY_0 = 0.000; dblSPS_QTY_0 = 0.000; dblOTHER_QTY_0 = 0.000; dblTOTAL_QTY_0 = 0.000;
			dblGPPS_QTY_1 = 0.000; dblHIPS_QTY_1 = 0.000; dblSPS_QTY_1 = 0.000; dblOTHER_QTY_1 = 0.000; dblTOTAL_QTY_1 = 0.000;
			dblGPPS_VAL_0 = 0.000; dblHIPS_VAL_0 = 0.000; dblSPS_VAL_0 = 0.000; dblOTHER_VAL_0 = 0.000; dblTOTAL_VAL_0 = 0.000;
			dblGPPS_VAL_1 = 0.000; dblHIPS_VAL_1 = 0.000; dblSPS_VAL_1 = 0.000; dblOTHER_VAL_1 = 0.000; dblTOTAL_VAL_1 = 0.000;
			M_strSQLQRY = "select ivt_invno,ivt_ladno,ivt_saltp,ivt_prdcd,ivt_pkgtp, isnull(ivt_invrt,0) ivt_invrt, isnull(ivt_assvl,0) ivt_assvl, isnull(ivt_excvl,0) ivt_excvl, isnull(ivt_edcvl,0)+isnull(ivt_ehcvl,0) ivt_edhvl, isnull(ivt_frtvl,0) ivt_frtvl, isnull(ivt_invqt,0) ivt_invqt from mr_ivtrn where "+strWHRSTR+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+ " order by ivt_invno";      //,sum(isnull(ivt_invvl,0)) ivt_invvl
			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			i=0; j=0; L_intDSRCTR = 0; L_intBYRCTR = 0;
			dblTOTQT = 0.000;
			flgEOFCHK = false;
			tblREGRPT.clrTABLE();
			String L_strINVNO = "", L_strINVNO_OLD = "";
			//System.out.println("-004");
			while(true)
			{
				//i = chkREGHDR(rstRSSET,i);    // Printing Category Total
				L_strINVNO = getRSTVAL(rstRSSET,"IVT_INVNO","C");
				if(!L_strINVNO.equals(L_strINVNO_OLD))
				{
					L_strINVNO_OLD = L_strINVNO;
					i+=1;
					vtrINVNO.addElement(L_strINVNO);
					tblREGRPT.setValueAt(L_strINVNO+"    ",i,intTB31_INVNO);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_DORNO")+"    ",i,intTB31_DORNO);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_DORDT")+"    ",i,intTB31_DORDT);
					tblREGRPT.setValueAt(getCDTRN("SYSMR00SAL"+getIVTRN(L_strINVNO,"IVT_SALTP"),"CMT_CODDS",hstCDTRN),i,intTB31_SALDS);
					tblREGRPT.setValueAt(getCDTRN("SYSMRXXDTP"+getIVTRN(L_strINVNO,"IVT_DTPCD"),"CMT_CODDS",hstCDTRN),i,intTB31_DTPDS);
					tblREGRPT.setValueAt(getCDTRN("SYSMRXXPMT"+getIVTRN(L_strINVNO,"IVT_PMTCD"),"CMT_CODDS",hstCDTRN),i,intTB31_PMTDS);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_PORNO")+"    ",i,intTB31_PORNO);
					tblREGRPT.setValueAt("----",i,intTB31_BYRNM1);
					tblREGRPT.setValueAt(getPTMST("C",getIVTRN(L_strINVNO,"IVT_BYRCD"),"PT_PRTNM"),i,intTB31_BYRNM);
					i+=1;

					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_INVDT"),i,intTB32_INVDT);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_INDNO"),i,intTB32_INDNO);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_INDDT"),i,intTB32_INDDT);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_LRYNO"),i,intTB32_LRYNO);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_LR_NO"),i,intTB32_LR_NO);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_CNTDS"),i,intTB32_CNTDS);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_PORDT"),i,intTB32_PORDT);
					tblREGRPT.setValueAt("----",i,intTB32_TRPNM1);
					tblREGRPT.setValueAt(getPTMST("T",getIVTRN(L_strINVNO,"IVT_TRPCD"),"PT_PRTNM"),i,intTB32_TRPNM);
					i+=1;

					tblREGRPT.setValueAt(getPTMST(getIVTRN(L_strINVNO,"IVT_DSRTP"),getIVTRN(L_strINVNO,"IVT_DSRCD"),"PT_PRTNM"),i,intTB33_DSRNM);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_INVQT"),i,intTB33_INVQT);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_ASSVL"),i,intTB33_ASSVL);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_EXCVL"),i,intTB33_EXCVL);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_EDHVL"),i,intTB33_EDHVL);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_FRTVL"),i,intTB33_FRTVL);
					tblREGRPT.setValueAt(getIVTRN(L_strINVNO,"IVT_INVVL"),i,intTB33_INVVL);
					if(!getIVTRN(L_strINVNO,"IVT_CNSCD").equals(getIVTRN(L_strINVNO,"IVT_BYRCD")))
						tblREGRPT.setValueAt(getPTMST("C",getIVTRN(L_strINVNO,"IVT_CNSCD"),"PT_PRTNM"),i,intTB33_CNSNM);
					i+=1;
				
				}
				double L_dblINVQT = Double.parseDouble(getRSTVAL(rstRSSET,"IVT_INVQT","N"));
				double L_dblINVVL = Double.parseDouble(getRSTVAL(rstRSSET,"IVT_ASSVL","N"))+Double.parseDouble(getRSTVAL(rstRSSET,"IVT_EXCVL","N"))+Double.parseDouble(getRSTVAL(rstRSSET,"IVT_EDHVL","N"))+Double.parseDouble(getRSTVAL(rstRSSET,"IVT_FRTVL","N"));
				String L_strSALTP = getRSTVAL(rstRSSET,"IVT_SALTP","C");
				String L_strPRDCD = getRSTVAL(rstRSSET,"IVT_PRDCD","C");
				if(!L_strSALTP.equals("12"))
				{
					if(L_strPRDCD.substring(0,4).equals("5111"))
						{dblGPPS_QTY_0 += L_dblINVQT; dblGPPS_VAL_0 += L_dblINVVL;}
					else if(L_strPRDCD.substring(0,4).equals("5112"))
						{dblHIPS_QTY_0 += L_dblINVQT; dblHIPS_VAL_0 += L_dblINVVL;}
					else if(L_strPRDCD.substring(0,2).equals("52"))
						{dblSPS_QTY_0 += L_dblINVQT; dblSPS_VAL_0 += L_dblINVVL;}
					else
						{dblOTHER_QTY_0 += L_dblINVQT; dblOTHER_VAL_0 += L_dblINVVL;}
				   dblTOTAL_QTY_0 += L_dblINVQT;
				   dblTOTAL_VAL_0 += L_dblINVVL;
				}
				if(L_strSALTP.equals("12"))
				{
					if(L_strPRDCD.substring(0,4).equals("5111"))
						{dblGPPS_QTY_1 += L_dblINVQT; dblGPPS_VAL_1 += L_dblINVVL;}
					else if(L_strPRDCD.substring(0,4).equals("5112"))
						{dblHIPS_QTY_1 += L_dblINVQT; dblHIPS_VAL_1 += L_dblINVVL;}
					else if(L_strPRDCD.substring(0,2).equals("52"))
						{dblSPS_QTY_1 += L_dblINVQT; dblSPS_VAL_1 += L_dblINVVL;}
					else
						{dblOTHER_QTY_1 += L_dblINVQT; dblOTHER_VAL_1 += L_dblINVVL;}
				   dblTOTAL_QTY_1 += L_dblINVQT;
				   dblTOTAL_VAL_1 += L_dblINVVL;
				}
				
				tblREGRPT.setValueAt((flgPRDCD_NEW == true ? getPRMST(getRSTVAL(rstRSSET,"IVT_PRDCD","C"),"PR_PRDDS") : "--\"--"),i,intTB34_PRDDS);
				tblREGRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVQT","N"),i,intTB34_INVQT);
				tblREGRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_ASSVL","N"),i,intTB34_ASSVL);
				tblREGRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_EXCVL","N"),i,intTB34_EXCVL);
				tblREGRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_EDHVL","N"),i,intTB34_EDHVL);
				tblREGRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_FRTVL","N"),i,intTB34_FRTVL);
				tblREGRPT.setValueAt(getRSTVAL(rstRSSET,"IVT_INVRT","N"),i,intTB34_INVRT);
				String strISTKEY = getRSTVAL(rstRSSET,"IVT_LADNO","C")+getRSTVAL(rstRSSET,"IVT_PRDCD","C")+getRSTVAL(rstRSSET,"IVT_PKGTP","C");
				tblREGRPT.setValueAt((hstLOTNO.containsKey(strISTKEY)? hstLOTNO.get(strISTKEY).toString() : ""),i,intTB34_LOTNO);
				i+=1;
				

				dblTOTQT += Double.parseDouble(getRSTVAL(rstRSSET,"IVT_INVQT","N"));
				if(!rstRSSET.next())
					break;
			}
			flgEOFCHK = true;

			if(dblTOTAL_QTY_0>0)
			{
				i+=2;
				tblREGRPT.setValueAt("DOMESTIC ",i,1);
				tblREGRPT.setValueAt("GPPS ",i,2);
				tblREGRPT.setValueAt("HIPS ",i,3);
				tblREGRPT.setValueAt("SPS ",i,4);
				tblREGRPT.setValueAt("OTHERS ",i,5);
				tblREGRPT.setValueAt("TOTAL ",i,6);
			
				i+=1;
				tblREGRPT.setValueAt("Qty(MT): ",i,1);
				tblREGRPT.setValueAt(setNumberFormat(dblGPPS_QTY_0,3),i,2);
				tblREGRPT.setValueAt(setNumberFormat(dblHIPS_QTY_0,3),i,3);
				tblREGRPT.setValueAt(setNumberFormat(dblSPS_QTY_0,3),i,4);
				tblREGRPT.setValueAt(setNumberFormat(dblOTHER_QTY_0,3),i,5);
				tblREGRPT.setValueAt(setNumberFormat(dblTOTAL_QTY_0,3),i,6);

				i+=1;
				tblREGRPT.setValueAt("Value: ",i,1);
				tblREGRPT.setValueAt(setNumberFormat(dblGPPS_VAL_0,0),i,2);
				tblREGRPT.setValueAt(setNumberFormat(dblHIPS_VAL_0,0),i,3);
				tblREGRPT.setValueAt(setNumberFormat(dblSPS_VAL_0,0),i,4);
				tblREGRPT.setValueAt(setNumberFormat(dblOTHER_VAL_0,0),i,5);
				tblREGRPT.setValueAt(setNumberFormat(dblTOTAL_VAL_0,0),i,6);
			}
			if(dblTOTAL_QTY_1>0)
			{
				i+=2;
				tblREGRPT.setValueAt("EXPORT ",i,1);
				tblREGRPT.setValueAt("GPPS ",i,2);
				tblREGRPT.setValueAt("HIPS ",i,3);
				tblREGRPT.setValueAt("SPS ",i,4);
				tblREGRPT.setValueAt("OTHERS ",i,5);
				tblREGRPT.setValueAt("TOTAL ",i,6);
			
				i+=1;
				tblREGRPT.setValueAt("Qty: ",i,1);
				tblREGRPT.setValueAt(setNumberFormat(dblGPPS_QTY_1,3),i,2);
				tblREGRPT.setValueAt(setNumberFormat(dblHIPS_QTY_1,3),i,3);
				tblREGRPT.setValueAt(setNumberFormat(dblSPS_QTY_1,3),i,4);
				tblREGRPT.setValueAt(setNumberFormat(dblOTHER_QTY_1,3),i,5);
				tblREGRPT.setValueAt(setNumberFormat(dblTOTAL_QTY_1,3),i,6);

				i+=1;
				tblREGRPT.setValueAt("Value: ",i,1);
				tblREGRPT.setValueAt(setNumberFormat(dblGPPS_VAL_1,0),i,2);
				tblREGRPT.setValueAt(setNumberFormat(dblHIPS_VAL_1,0),i,3);
				tblREGRPT.setValueAt(setNumberFormat(dblSPS_VAL_1,0),i,4);
				tblREGRPT.setValueAt(setNumberFormat(dblOTHER_VAL_1,0),i,5);
				tblREGRPT.setValueAt(setNumberFormat(dblTOTAL_VAL_1,0),i,6);
			}
			
			if(dblTOTAL_QTY_1+dblTOTAL_QTY_0>0)
			{
				i+=2;
				tblREGRPT.setValueAt("OVERALL ",i,1);
				tblREGRPT.setValueAt("GPPS ",i,2);
				tblREGRPT.setValueAt("HIPS ",i,3);
				tblREGRPT.setValueAt("SPS ",i,4);
				tblREGRPT.setValueAt("OTHERS ",i,5);
				tblREGRPT.setValueAt("TOTAL ",i,6);
			
				i+=1;
				tblREGRPT.setValueAt("Qty: ",i,1);
				tblREGRPT.setValueAt(setNumberFormat(dblGPPS_QTY_1+dblGPPS_QTY_0,3),i,2);
				tblREGRPT.setValueAt(setNumberFormat(dblHIPS_QTY_1+dblHIPS_QTY_0,3),i,3);
				tblREGRPT.setValueAt(setNumberFormat(dblSPS_QTY_1+dblSPS_QTY_0,3),i,4);
				tblREGRPT.setValueAt(setNumberFormat(dblOTHER_QTY_1+dblOTHER_QTY_0,3),i,5);
				tblREGRPT.setValueAt(setNumberFormat(dblTOTAL_QTY_1+dblTOTAL_QTY_0,3),i,6);

				i+=1;
				tblREGRPT.setValueAt("Value: ",i,1);
				tblREGRPT.setValueAt(setNumberFormat(dblGPPS_VAL_1+dblGPPS_VAL_0,0),i,2);
				tblREGRPT.setValueAt(setNumberFormat(dblHIPS_VAL_1+dblHIPS_VAL_0,0),i,3);
				tblREGRPT.setValueAt(setNumberFormat(dblSPS_VAL_1+dblSPS_VAL_0,0),i,4);
				tblREGRPT.setValueAt(setNumberFormat(dblOTHER_VAL_1+dblOTHER_VAL_0,0),i,5);
				tblREGRPT.setValueAt(setNumberFormat(dblTOTAL_VAL_1+dblTOTAL_VAL_0,0),i,6);

			}
			if(cmbRPTWS.getSelectedItem().toString().equals(strALLWS))
				crtREGFTR(i);

			rstRSSET.close();
		
		}
		catch (Exception L_EX) {setMSG(L_EX,"dspINVREG");}
	}
	
	
	
	/**
	 */
	public void exePRINT()
	{
		try
		{
			//super.exePRINT();
			//strSTRDT = M_txtFMDAT.getText();
			//strENDDT = M_txtTODAT.getText();
			this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
			hstCDTRN.clear();
			crtCDTRN("'MSTCOXXCUR','STSMRXXIND','SYSMRXXDTP', 'SYSMR00SAL', 'SYSMRXXPMT','SYSMR01MOT','SYSMR00EUS','SYSCOXXTAX','SYSCOXXDST','SYSFGXXPKG','SYSCOXXAMT'","",hstCDTRN);
			if(M_txtFMDAT.getText().length()!=10 || M_txtTODAT.getText().length()!=10)
				return;
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
			strSBSSTR = setSBSSTR("SBS");
			getSUBHDR();

			crtPTMST();
			crtLOTNO();
			//System.out.println("-002");
						
			if(!exeTBLREFSH())
				return;
			
			dspINVDTL();
			dspINVREG();
			dspINVSUM();
			dspINVDUC();
		
		}
		catch (Exception L_EX) {setMSG(L_EX,"exePRINT");}
		finally
		{this.getParent().getParent().setCursor(cl_dat.M_curDFSTS_pbst);this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}


	/**
	 */
	private void getSUBHDR()
	{
		try
		{
			strWHRSTR = setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText());
			strSHDRNM = "";
			strSHDRDS = "";
			if (cmbRPTWS.getSelectedIndex()>0)
			{
				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
					{strWHRSTR += " and ivt_zoncd in (select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_chp02 = '"+txtSPCCT.getText()+"')"; strSHDRDS = getCDTRN("SYSMR00RGN"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Region:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strZONWS))
					{strWHRSTR += " and ivt_zoncd = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00ZON"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Zone:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strGRDWS))
					{strWHRSTR += " and ivt_prdcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00ZON"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Grade:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strDSRWS))
					{strWHRSTR += " and ivt_dsrcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPRTNM("D",txtSPCCT.getText()); strSHDRNM = "Distributor:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strBYRWS))
					{strWHRSTR += " and ivt_byrcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPRTNM("C",txtSPCCT.getText()); strSHDRNM = "Buyer:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
					{strWHRSTR += " and ivt_saltp = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00SAL"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Sale Type:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strPMTWS))
					{strWHRSTR += " and ivt_pmtcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMRXXPMT"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Pmt.Type:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
					{strWHRSTR += " and SUBSTRING(ivt_prdcd,1,4) = '"+txtSPCCT.getText()+"'";strSHDRDS = getCDTRN("SYSMR00PGR"+txtSPCCT.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM = "Prod Category:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strLOTWS))
					{strWHRSTR += " and ivt_CMPCD + ivt_ladno + ivt_prdcd in (select ist_CMPCD + ist_issno + ist_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_lotno = '"+txtSPCCT.getText()+"')";strSHDRDS = txtSPCCT.getText(); strSHDRNM = "Lot No.:";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strTRPWS))
				{strWHRSTR += " and ivt_trpcd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPRTNM("T",txtSPCCT.getText()); strSHDRNM = "Transporter :";}
				else if(cmbRPTWS.getSelectedItem().toString().equals(strCNSWS))
				{strWHRSTR += " and ivt_cnscd = '"+txtSPCCT.getText()+"'";strSHDRDS = getPRTNM("C",txtSPCCT.getText()); strSHDRNM = "Consignee :";}
			}
			txtSPCCT.setVisible(true);
			if(strSHDRNM.equals(""))
				txtSPCCT.setVisible(false);
			lblSPCDS.setText(strSHDRDS);
			lblSPCCT.setText(strSHDRNM);
			if (cmbRPTWS1.getSelectedIndex()>0)
			{
			    if(cmbRPTWS1.getSelectedItem().toString().equals(cmbRPTWS.getSelectedItem().toString()))
			    {
			        setMSG("Scope 1 an scope 2 can not be same ..",'E');
			        return;
			    }
			    
				if(cmbRPTWS1.getSelectedItem().toString().equals(strRGNWS))
					{strWHRSTR += " and ivt_zoncd in (select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_chp02 = '"+txtSPCCT1.getText()+"')"; strSHDRDS1 = getCDTRN("SYSMR00RGN"+txtSPCCT1.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM1 = "Region:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strZONWS))
					{strWHRSTR += " and ivt_zoncd = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getCDTRN("SYSMR00ZON"+txtSPCCT1.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM1 = "Zone:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strGRDWS))
					{strWHRSTR += " and ivt_prdcd = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getCDTRN("SYSMR00ZON"+txtSPCCT1.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM1 = "Grade:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strDSRWS))
					{strWHRSTR += " and ivt_dsrcd = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getPRTNM("D",txtSPCCT1.getText()); strSHDRNM1 = "Distributor:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strBYRWS))
					{strWHRSTR += " and ivt_byrcd = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getPRTNM("C",txtSPCCT1.getText()); strSHDRNM1 = "Buyer:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strSALWS))
					{strWHRSTR += " and ivt_saltp = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getCDTRN("SYSMR00SAL"+txtSPCCT1.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM1 = "Sale Type:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strPMTWS))
					{strWHRSTR += " and ivt_pmtcd = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getCDTRN("SYSMRXXPMT"+txtSPCCT1.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM1 = "Pmt.Type:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strPRDWS))
					{strWHRSTR += " and SUBSTRING(ivt_prdcd,1,4) = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getCDTRN("SYSMR00PGR"+txtSPCCT1.getText(),"CMT_CODDS",hstCDTRN); strSHDRNM1 = "Prod Category:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strLOTWS))
					{strWHRSTR += " and ivt_CMPCD + ivt_ladno + ivt_prdcd in (select ist_CMPCD + ist_issno + ist_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_lotno = '"+txtSPCCT1.getText()+"')";strSHDRDS1 = txtSPCCT1.getText(); strSHDRNM1 = "Lot No.:";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strTRPWS))
				{strWHRSTR += " and ivt_trpcd = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getPRTNM("T",txtSPCCT1.getText()); strSHDRNM1 = "Transporter :";}
				else if(cmbRPTWS1.getSelectedItem().toString().equals(strCNSWS))
				{strWHRSTR += " and ivt_cnscd = '"+txtSPCCT1.getText()+"'";strSHDRDS1 = getPRTNM("C",txtSPCCT1.getText()); strSHDRNM1 = "Consignee :";}
			}
			
			if(strSHDRNM1.equals(""))
				txtSPCCT1.setVisible(false);
			else
			    txtSPCCT1.setVisible(true);	
			lblSPCDS1.setText(strSHDRDS1);
			lblSPCCT1.setText(strSHDRNM1);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"getSUBHDR");}
	}
		
	
	
	/**
 */	
	private void crtDTLHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intDTLWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Invoicewise Details "+strSHDRNM+" "+strSHDRDS+" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intDTLWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intDTLWD,"-")+"\n"); intLINECT +=1;
				  for(i=1;i<arrDTLHDR.length;i++)
			{
				O_DOUT.writeBytes(padSTRING(arrDTLHDR_PAD[i],arrDTLHDR[i],arrDTLHDR_WD[i]/8)+" "); intRUNCL +=1;
				if(intRUNCL>intDTLCL)
					{O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrDTLHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;}
			}
			O_DOUT.writeBytes("\n"+crtLINE(intDTLWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtDTLHDR");}
	}
	

	
	private void crtREGHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intREGWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Invoice Register "+strSHDRNM+" "+strSHDRDS+" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intREGWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intREGWD,"-")+"\n"); intLINECT +=1;

			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_INVNO],arrREGHDR1[intTB31_INVNO],arrREGHDR_WD[intTB31_INVNO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_DORNO],arrREGHDR1[intTB31_DORNO],arrREGHDR_WD[intTB31_DORNO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_DORDT],arrREGHDR1[intTB31_DORDT],arrREGHDR_WD[intTB31_DORDT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_SALDS],arrREGHDR1[intTB31_SALDS],arrREGHDR_WD[intTB31_SALDS]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_DTPDS],arrREGHDR1[intTB31_DTPDS],arrREGHDR_WD[intTB31_DTPDS]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_PMTDS],arrREGHDR1[intTB31_PMTDS],arrREGHDR_WD[intTB31_PMTDS]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_PORNO],arrREGHDR1[intTB31_PORNO],arrREGHDR_WD[intTB31_PORNO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_BYRNM1],arrREGHDR1[intTB31_BYRNM1],arrREGHDR_WD[intTB31_BYRNM1]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB31_BYRNM],arrREGHDR1[intTB31_BYRNM],arrREGHDR_WD[intTB31_BYRNM]/6)+" ");
			O_DOUT.writeBytes("\n"); intLINECT +=1;

			
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_INVDT],arrREGHDR2[intTB32_INVDT],arrREGHDR_WD[intTB32_INVDT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_INDNO],arrREGHDR2[intTB32_INDNO],arrREGHDR_WD[intTB32_INDNO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_INDDT],arrREGHDR2[intTB32_INDDT],arrREGHDR_WD[intTB32_INDDT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_LRYNO],arrREGHDR2[intTB32_LRYNO],arrREGHDR_WD[intTB32_LRYNO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_LR_NO],arrREGHDR2[intTB32_LR_NO],arrREGHDR_WD[intTB32_LR_NO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_CNTDS],arrREGHDR2[intTB32_CNTDS],arrREGHDR_WD[intTB32_CNTDS]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_PORDT],arrREGHDR2[intTB32_PORDT],arrREGHDR_WD[intTB32_PORDT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_TRPNM1],arrREGHDR2[intTB32_TRPNM1],arrREGHDR_WD[intTB32_TRPNM1]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB32_TRPNM],arrREGHDR2[intTB32_TRPNM],arrREGHDR_WD[intTB32_TRPNM]/6)+" ");
			O_DOUT.writeBytes("\n"); intLINECT +=1;

			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_BLK01],arrREGHDR3[intTB33_BLK01],arrREGHDR_WD[intTB33_BLK01]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_DSRNM],arrREGHDR3[intTB33_DSRNM],arrREGHDR_WD[intTB33_DSRNM]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_INVQT],arrREGHDR3[intTB33_INVQT],arrREGHDR_WD[intTB33_INVQT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_ASSVL],arrREGHDR3[intTB33_ASSVL],arrREGHDR_WD[intTB33_ASSVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_EXCVL],arrREGHDR3[intTB33_EXCVL],arrREGHDR_WD[intTB33_EXCVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_EDHVL],arrREGHDR3[intTB33_EDHVL],arrREGHDR_WD[intTB33_EDHVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_FRTVL],arrREGHDR3[intTB33_FRTVL],arrREGHDR_WD[intTB33_FRTVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_INVVL],arrREGHDR3[intTB33_INVVL],arrREGHDR_WD[intTB33_INVVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB33_CNSNM],arrREGHDR3[intTB33_CNSNM],arrREGHDR_WD[intTB33_CNSNM]/6)+" ");
			O_DOUT.writeBytes("\n"); intLINECT +=1;

			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_BLK01],arrREGHDR4[intTB34_BLK01],arrREGHDR_WD[intTB34_BLK01]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_PRDDS],arrREGHDR4[intTB34_PRDDS],arrREGHDR_WD[intTB34_PRDDS]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_INVQT],arrREGHDR4[intTB34_INVQT],arrREGHDR_WD[intTB34_INVQT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_ASSVL],arrREGHDR4[intTB34_ASSVL],arrREGHDR_WD[intTB34_ASSVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_EXCVL],arrREGHDR4[intTB34_EXCVL],arrREGHDR_WD[intTB34_EXCVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_EDHVL],arrREGHDR4[intTB34_EDHVL],arrREGHDR_WD[intTB34_EDHVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_FRTVL],arrREGHDR4[intTB34_FRTVL],arrREGHDR_WD[intTB34_FRTVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_INVRT],arrREGHDR4[intTB34_INVRT],arrREGHDR_WD[intTB34_INVRT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[intTB34_LOTNO],arrREGHDR4[intTB34_LOTNO],arrREGHDR_WD[intTB34_LOTNO]/6)+" ");
			O_DOUT.writeBytes("\n"); intLINECT +=1;

			O_DOUT.writeBytes("\n"+crtLINE(intREGWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtREGHDR");}
	}


	/**
	 * Invoice register footer, this includes missing invoice numbers within the specified dates.
	 */
	private void crtREGFTR(int LP_intROWNO)
	{
		try
		{
			boolean L_flgMISINV = false;
			int L_intINVNO = Integer.parseInt(vtrINVNO.get(0).toString());
			int L_intINVNO_PRV = L_intINVNO;
			
			LP_intROWNO +=2; tblREGRPT.setValueAt("Missing Invoices : ",LP_intROWNO,2); tblREGRPT.setValueAt("Market Type",LP_intROWNO,3); LP_intROWNO +=1;
			for (int i=1;i<vtrINVNO.size();i++)
			{
				L_intINVNO = Integer.parseInt(vtrINVNO.get(i).toString());
				if(!String.valueOf(L_intINVNO_PRV).substring(3,4).equals("2") && String.valueOf(L_intINVNO).substring(3,4).equals("2"))
					{L_intINVNO_PRV = L_intINVNO; continue;}
				if(L_intINVNO - L_intINVNO_PRV == 1)
					{L_intINVNO_PRV = L_intINVNO; continue;}
				L_flgMISINV = true;
				//System.out.println(L_intINVNO_PRV+" / "+L_intINVNO);
				//System.out.println(String.valueOf(L_intINVNO_PRV).substring(3,4)+" / "+String.valueOf(L_intINVNO).substring(3,4));
				for(int j = L_intINVNO_PRV+1; j<L_intINVNO;j++)
					{
						tblREGRPT.setValueAt(String.valueOf(j),LP_intROWNO,2);
						if(hstINVNO_REST.containsKey(String.valueOf(j)))
							tblREGRPT.setValueAt(hstINVNO_REST.get(String.valueOf(j)).toString(),LP_intROWNO,3);
						LP_intROWNO++;
					}
				L_intINVNO_PRV = L_intINVNO;
				
			}
			if(!L_flgMISINV)
				tblREGRPT.setValueAt("NIL ",LP_intROWNO,2);
			tblREGRPT.setValueAt("--------",LP_intROWNO,2);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtREGFTR");}
	}
	
	
	/**
	 */
	private void crtSUMHDR()
	{
		try
		{
			//System.out.println("crtSUMHDR 001");
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			//System.out.println("crtSUMHDR 002");
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intSUMWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			//System.out.println("crtSUMHDR 003");
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Sales Analysis "+strSHDRNM+" "+strSHDRDS+" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intSUMWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intSUMWD,"-")+"\n"); intLINECT +=1;
			//System.out.println("crtSUMHDR 004");
			for(i=1;i<arrSUMHDR.length;i++)
			{
				//System.out.println("crtSUMHDR 004a");
				O_DOUT.writeBytes(padSTRING(arrSUMHDR_PAD[i],arrSUMHDR[i],arrSUMHDR_WD[i]/8)+" "); intRUNCL +=1;
				//System.out.println("crtSUMHDR 004b");
				if(intRUNCL>intSUMCL)
					{O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrSUMHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;}
			}
			O_DOUT.writeBytes("\n"+crtLINE(intSUMWD,"-")+"\n"); intLINECT +=2;
			//System.out.println("crtSUMHDR 005");
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtSUMHDR");}
	}
	
	private void crtDUCHDR()
	{
		try
		{
			if(intLINECT<60)
				return;
			int i=0;
			intPAGENO +=1; 
			if(intPAGENO>1)
				{O_DOUT.writeBytes("\n"+crtLINE(intDUCWD,"-")+"\n"); intLINECT +=2; for(i=intLINECT;i<72;i++) O_DOUT.writeBytes("\n");}

			intLINECT = 0;
			O_DOUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('C',"Dual Credit "+strSHDRNM+" "+strSHDRDS+" from "+M_txtFMDAT.getText()+ " to "+ M_txtTODAT.getText(),intDUCWD-50)+padSTRING('L',"Page "+intPAGENO,25)+"\n\n"); intLINECT +=2;
			intRUNCL = 0;
			O_DOUT.writeBytes(crtLINE(intDUCWD,"-")+"\n"); intLINECT +=1;

			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_BYRNM],arrDUCHDR1[intTB41_BYRNM],arrDUCHDR_WD[intTB41_BYRNM]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_DSRNM],arrDUCHDR1[intTB41_DSRNM],arrDUCHDR_WD[intTB41_DSRNM]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_INVNO],arrDUCHDR1[intTB41_INVNO],arrDUCHDR_WD[intTB41_INVNO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_INVVL],arrDUCHDR1[intTB41_INVVL],arrDUCHDR_WD[intTB41_INVVL]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_INVCP],arrDUCHDR1[intTB41_INVCP],arrDUCHDR_WD[intTB41_INVCP]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_ACCCP],arrDUCHDR1[intTB41_ACCCP],arrDUCHDR_WD[intTB41_ACCCP]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_DORNO],arrDUCHDR1[intTB41_DORNO],arrDUCHDR_WD[intTB41_DORNO]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB41_INDNO],arrDUCHDR1[intTB41_INDNO],arrDUCHDR_WD[intTB41_INDNO]/6)+" ");
			O_DOUT.writeBytes("\n"); intLINECT +=1;

			
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_BLK01],arrDUCHDR2[intTB42_BLK01],arrDUCHDR_WD[intTB42_BLK01]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_BLK02],arrDUCHDR2[intTB42_BLK02],arrDUCHDR_WD[intTB42_BLK02]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_INVDT],arrDUCHDR2[intTB42_INVDT],arrDUCHDR_WD[intTB42_INVDT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_BLK04],arrDUCHDR2[intTB42_BLK04],arrDUCHDR_WD[intTB42_BLK04]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_INVDD],arrDUCHDR2[intTB42_INVDD],arrDUCHDR_WD[intTB42_INVDD]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_ACCDD],arrDUCHDR2[intTB42_ACCDD],arrDUCHDR_WD[intTB42_ACCDD]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_DORDT],arrDUCHDR2[intTB42_DORDT],arrDUCHDR_WD[intTB42_DORDT]/6)+" ");
			O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[intTB42_INDDT],arrDUCHDR2[intTB42_INDDT],arrDUCHDR_WD[intTB42_INDDT]/6)+" ");
			O_DOUT.writeBytes("\n"); intLINECT +=1;


			O_DOUT.writeBytes("\n"+crtLINE(intDUCWD,"-")+"\n"); intLINECT +=2;
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtDUCHDR");}
	}
	
	
	/**
	 */
	private void crtDTLRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72;

			int i=0, j = tblDTLRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtDTLHDR();
			for(i=0;i<tblDTLRPT.getRowCount();i++)
			{
				if(tblDTLRPT.getValueAt(i,intTB1_INVQT).toString().equals(""))
					break;
				else if(Double.parseDouble(tblDTLRPT.getValueAt(i,intTB1_INVQT).toString())==0)
					break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrDTLHDR_PAD[k],tblDTLRPT.getValueAt(i,k).toString().trim(),arrDTLHDR_WD[k]/8)+" "); intRUNCL += 1;
					if(intRUNCL>intDTLCL)
					{
						O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrDTLHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						
					}
				}
				O_DOUT.writeBytes("\n\n");intLINECT +=2;
				if(intLINECT>60) crtDTLHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intSUMWD,"-")+"\n"); intLINECT +=2;
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtDTLRPT");}
	}
	

	
	/**
	 */
	private void crtREGRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72; intBLKCTR = 0;

			int i=0, j = tblREGRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtREGHDR();
			for(i=0;i<tblREGRPT.getRowCount();i++)
			{
				//if(tblREGRPT.getValueAt(i,intTB3_INVQT).toString().equals(""))
				//	break;
				//else if(Double.parseDouble(tblREGRPT.getValueAt(i,intTB3_INVQT).toString())==0)
				//	break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrREGHDR_PAD[k],tblREGRPT.getValueAt(i,k).toString().trim(),arrREGHDR_WD[k]/6)+" "); intRUNCL += 1;
				}
				O_DOUT.writeBytes("\n");intLINECT +=1;
				intBLKCTR +=1 ;
				if(tblREGRPT.getValueAt(i,2).toString().trim().length()>0)
					intBLKCTR = 0;
				if(intBLKCTR>5)
					break;
				if(intLINECT>60) crtREGHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intSUMWD,"-")+"\n"); intLINECT +=2;
			
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtREGRPT");}
	}
	
	
	
	/**
	*/
	private void crtSUMRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72;
			int i=0, j = tblSUMRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtSUMHDR();
			for(i=0;i<tblSUMRPT.getRowCount();i++)
			{
				if(tblSUMRPT.getValueAt(i,arrHSTKEY.length+intSUMFIX).toString().equals(""))
					break;
				else if(Double.parseDouble(tblSUMRPT.getValueAt(i,arrHSTKEY.length+intSUMFIX).toString())==0)
					break;
				intRUNCL = 0;
				O_DOUT.writeBytes(padSTRING(arrSUMHDR_PAD[1],tblSUMRPT.getValueAt(i,1).toString(),arrSUMHDR_WD[1]/8)+" "); intRUNCL += 1;
				for(k=2;k<j;k++)
				{
					O_DOUT.writeBytes(padNUMBER(arrSUMHDR_PAD[k],tblSUMRPT.getValueAt(i,k).toString(),arrSUMHDR_WD[k]/8)+" "); intRUNCL += 1;
					if(intRUNCL>intSUMCL)
					{
						O_DOUT.writeBytes("\n"+padSTRING('L'," ",arrSUMHDR_WD[1]/8)+" "); intRUNCL=0; intLINECT +=1;
						if(intLINECT>60) crtSUMHDR();
					}
				}
				O_DOUT.writeBytes("\n\n");intLINECT +=2;
			}
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtSUMRPT");}
	}
	
	/**
	 */
	private void crtDUCRPT()
	{
		try
		{
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGENO = 0; intLINECT = 72; intBLKCTR = 0;

			int i=0, j = tblDUCRPT.getColumnCount(), k=0, intRUNCL = 0;

			crtDUCHDR();
			for(i=0;i<tblDUCRPT.getRowCount();i++)
			{
				//if(tblDUCRPT.getValueAt(i,intTB3_INVQT).toString().equals(""))
				//	break;
				//else if(Double.parseDouble(tblDUCRPT.getValueAt(i,intTB3_INVQT).toString())==0)
				//	break;
				intRUNCL = 0;
				for(k=1;k<j;k++)
				{
					O_DOUT.writeBytes(padSTRING(arrDUCHDR_PAD[k],tblDUCRPT.getValueAt(i,k).toString().trim(),arrDUCHDR_WD[k]/6)+" "); intRUNCL += 1;
				}
				O_DOUT.writeBytes("\n");intLINECT +=1;
				intBLKCTR +=1 ;
				if(tblDUCRPT.getValueAt(i,2).toString().trim().length()>0)
					intBLKCTR = 0;
				if(intBLKCTR>5)
					break;
				if(intLINECT>60) crtDUCHDR();
			}
			O_DOUT.writeBytes("\n"+crtLINE(intDUCWD,"-")+"\n"); intLINECT +=2;
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strEJT);
			
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"crtDUCRPT");}
	}
	
	
	/**
	 */
	private int getDSRCOL(String LP_SHRNM)
	{
		int i = 0;
		for (i=0; i<arrHSTKEY.length;i++)
			{if(arrHSTKEY[i].equals(LP_SHRNM)) break;}
		return i;
	}
	

	/**Dynamic Recreation of display table
	 * According to content/values applicable at the time of execution
	 */
	private boolean exeTBLREFSH()
	{
		try
		{
			int i;
			M_strSQLQRY = "select count(*) ivt_recct from mr_ivtrn where "+strWHRSTR+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "");
			ResultSet rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next()  || rstRSSET==null)
				{setMSG("No Detail Records Found",'E');return false;}
			int L_intRECCT = Integer.parseInt(getRSTVAL(rstRSSET,"IVT_RECCT","N"));
			L_intRECCT = 3*L_intRECCT;
			rstRSSET.close();
			//System.out.println("001");

			setHST_ARR(hstDSRNM);
			arrSUMHDR = new String[arrHSTKEY.length+intSUMFIX+1];
			arrSUMHDR[0] = ""; arrSUMHDR[1] = "Grade";
			for(i=2; i<arrHSTKEY.length+intSUMFIX; i++)
				arrSUMHDR[i] = arrHSTKEY[i-2].toString();
			arrSUMHDR[arrHSTKEY.length+intSUMFIX] = "Total";
			//System.out.println("002");

			//System.out.println("Array Elements : "+arrHSTKEY.length+intSUMFIX);
			arrSUMHDR_WD = new int[arrHSTKEY.length+intSUMFIX+1];
			arrSUMHDR_WD[0] = 20; arrSUMHDR_WD[1] = 100;
			for(i=2; i<arrHSTKEY.length+intSUMFIX; i++)
				arrSUMHDR_WD[i] = 80;
			arrSUMHDR_WD[arrHSTKEY.length+intSUMFIX] = 80;
			
			//System.out.println("003");
			arrSUMHDR_PAD = new char[arrHSTKEY.length+intSUMFIX+1];
			arrSUMHDR_PAD[0] = 'R'; arrSUMHDR_PAD[1] = 'R';
			for(i=2; i<arrHSTKEY.length+intSUMFIX; i++)
				arrSUMHDR_PAD[i] = 'L';
			arrSUMHDR_PAD[arrHSTKEY.length+intSUMFIX] = 'L';

			tbpMAIN.remove(pnlDTLRPT);
			tbpMAIN.remove(pnlSUMRPT);
			tbpMAIN.remove(pnlREGRPT);
			tbpMAIN.remove(pnlDUCRPT);
			pnlDTLRPT.removeAll();
			pnlSUMRPT.removeAll();
			pnlREGRPT.removeAll();
			pnlDUCRPT.removeAll();

			pnlDTLRPT = new JPanel(null);
			pnlSUMRPT = new JPanel(null);
			pnlREGRPT = new JPanel(null);
			pnlDUCRPT = new JPanel(null);
			
			//System.out.println("004");
			tblDTLRPT = crtTBLPNL1(pnlDTLRPT,arrDTLHDR,L_intRECCT,2,1,9.1,7.9,arrDTLHDR_WD,new int[]{0});    //,"Amount"
			tblSUMRPT = crtTBLPNL1(pnlSUMRPT,arrSUMHDR,500,2,1,9.1,7.9,arrSUMHDR_WD,new int[]{0});
			tblREGRPT = crtTBLPNL1(pnlREGRPT,arrREGHDR,L_intRECCT*4,2,1,9.1,7.9,arrREGHDR_WD,new int[]{0});    //,"Amount"
			tblDUCRPT = crtTBLPNL1(pnlDUCRPT,arrDUCHDR,L_intRECCT*4,2,1,9.1,7.9,arrDUCHDR_WD,new int[]{0});    //,"Amount"
			tbpMAIN.addTab("Detail",pnlDTLRPT);
			tbpMAIN.addTab("Summary",pnlSUMRPT);
			tbpMAIN.addTab("Register",pnlREGRPT);
			tbpMAIN.addTab("Dual Credit",pnlDUCRPT);
		
			//System.out.println("005");
			tblDTLRPT.addMouseListener(this);
			tblSUMRPT.addMouseListener(this);
			tblSUMRPT.setColumnColor(arrHSTKEY.length+intSUMFIX,Color.blue);
			tblDUCRPT.addMouseListener(this);
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
			for(int i=0;i<M_staUSRRT.length;i++)
			{
				if(LP_SBSTP.equals("ZON"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(2,4)+"',";
				else if(LP_SBSTP.equals("SAL"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(4,6)+"',";
				else if(LP_SBSTP.equals("SBS"))
					L_strRETSTR += "'"+M_staUSRRT[i][0].substring(2)+"',";
			}
			L_strRETSTR=L_strRETSTR.substring(0,L_strRETSTR.length()-1);
		}
		catch(Exception L_EX) {setMSG(L_EX,"setSBSSTR");}
		return L_strRETSTR;
	}
	
	
	/**
	 */
	private String setWHRSTR(String LP_STRDT, String LP_ENDDT)
	{
		String L_strRETSTR = "";
		//System.out.println("M_strSBSLS : "+M_strSBSLS);
		//System.out.println("strSBSSTR : "+strSBSSTR);
		try
		{
			L_strRETSTR = " IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_dsrtp = '"+(chkCSTKFL.isSelected() ? "G" : "D")+"' and CONVERT(varchar,ivt_invdt,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and ivt_stsfl<>'X' and isnull(ivt_invqt,0)>0 and ivt_mkttp = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' "
				+ " and ivt_SBSCD1 in "+M_strSBSLS;
			strWHRSTR_REST = " IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,ivt_invdt,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and ivt_stsfl<>'X' and isnull(ivt_invqt,0)>0 and ivt_mkttp <> '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"' ";
			//System.out.println(L_strRETSTR);
		}
		catch (Exception L_EX){setMSG(L_EX,"setWHRSTR");}
		return L_strRETSTR;
	}
	
	/**
	 */
	private int chkDTLHDR(ResultSet LP_RSSET, int LP_ROWNO)
	{
		try
		{
			flgDSRCD_NEW = false;
			flgBYRCD_NEW = false;
			flgPRDCD_NEW = false;
			if(flgEOFCHK || !getRSTVAL(LP_RSSET,"IVT_PRDCD","C").equals(strPRDCD_OLD))
			{
				flgPRDCD_NEW = true;
				if(!flgEOFCHK)
					strPRDCD_OLD = getRSTVAL(LP_RSSET,"IVT_PRDCD","C");
			}
			//System.out.println("0001");
			if(flgEOFCHK || !getRSTVAL(LP_RSSET,"IVT_BYRCD","C").equals(strBYRCD_OLD))
			{
				flgBYRCD_NEW = true;
				if(intBYRCTR>1  && chkSTOTFL.isSelected())
				{
					//System.out.println("0002");
					tblDTLRPT.setRowColor(LP_ROWNO,Color.darkGray);
					tblDTLRPT.setValueAt(setNumberFormat(dblBYRQT,3),LP_ROWNO,intTB1_INVQT);
					//tblDTLRPT.setValueAt(setNumberFormat(dblBYRVL,0),LP_ROWNO,intTB1_INVVL);
					LP_ROWNO+=1;
				}
				dblBYRQT=0.000;dblBYRVL=0.00;intBYRCTR=0;
				if(!flgEOFCHK)
					strBYRCD_OLD = getRSTVAL(LP_RSSET,"IVT_BYRCD","C");
			}
			if(flgEOFCHK || (!getRSTVAL(LP_RSSET,"IVT_DSRTP","C").equals(strDSRTP_OLD) && !getRSTVAL(LP_RSSET,"IVT_DSRCD","C").equals(strDSRCD_OLD)))
			{
				flgDSRCD_NEW = true;
				if(intDSRCTR>1 && chkSTOTFL.isSelected())
				{
					//System.out.println("0003");
					tblDTLRPT.setRowColor(LP_ROWNO,Color.blue);
					tblDTLRPT.setValueAt(getPTMST(strDSRTP_OLD,strDSRCD_OLD,"PT_SHRNM")+" Total:",LP_ROWNO,intTB1_PRDDS);
					tblDTLRPT.setValueAt(setNumberFormat(dblDSRQT,3),LP_ROWNO,intTB1_INVQT);
					//tblDTLRPT.setValueAt(setNumberFormat(dblDSRVL,0),LP_ROWNO,intTB1_INVVL);
					LP_ROWNO+=1;
				}
				dblDSRQT=0.000;dblDSRVL=0.00;intDSRCTR=0;
				if(!flgEOFCHK)
					{strDSRTP_OLD = getRSTVAL(LP_RSSET,"IVT_DSRTP","C"); strDSRCD_OLD = getRSTVAL(LP_RSSET,"IVT_DSRCD","C");}
			//System.out.println("0004");
			}
			tblDTLRPT.setRowColor(LP_ROWNO,Color.black);
		}
		catch (Exception L_EX1) {setMSG(L_EX1,"chkDTLHDR");}
		return LP_ROWNO;
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
	        L_strSQLQRY = "select PR_PRDCD,PR_PRDDS,PR_AVGRT from co_prmst where pr_stsfl <> 'X' and SUBSTRING(pr_prdcd,1,2) in ('51','52','53','54','67')";
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
	 private void crtLOTNO()
    {
        try
        {
			hstLOTNO.clear();
            String L_strSQLQRY = "select distinct ist_issno,ist_prdcd,ist_pkgtp,ist_lotno "
				+" from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_wrhtp = '01' and ist_isstp='10' and ist_issno in (select distinct ivt_ladno from mr_ivtrn  where "+strWHRSTR+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+")";
			
			//System.out.println(L_strSQLQRY);
            ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
            if(L_rstRSSET == null || !L_rstRSSET.next())
            {
                 setMSG("Issue records not found in FG_ISTRN",'E');
                  return;
            }
			String L_strISTKEY_OLD = getRSTVAL(L_rstRSSET,"IST_ISSNO","C")+getRSTVAL(L_rstRSSET,"IST_PRDCD","C")+getRSTVAL(L_rstRSSET,"IST_PKGTP","C");
			String L_strLOTNO = "";
            while(true)
            {
					String L_strISTKEY_NEW = getRSTVAL(L_rstRSSET,"IST_ISSNO","C")+getRSTVAL(L_rstRSSET,"IST_PRDCD","C")+getRSTVAL(L_rstRSSET,"IST_PKGTP","C");
					if(!L_strISTKEY_OLD.equals(L_strISTKEY_NEW))
					{
						L_strLOTNO = L_strLOTNO.substring(0,L_strLOTNO.length()-3);
						hstLOTNO.put(L_strISTKEY_OLD,L_strLOTNO);
						L_strISTKEY_OLD = getRSTVAL(L_rstRSSET,"IST_ISSNO","C")+getRSTVAL(L_rstRSSET,"IST_PRDCD","C")+getRSTVAL(L_rstRSSET,"IST_PKGTP","C");
						L_strLOTNO = "";
					}
					L_strLOTNO += getRSTVAL(L_rstRSSET,"IST_LOTNO","C")+ " / ";
					//System.out.println(L_strISTKEY_OLD+"/"+L_strLOTNO);
					if(!L_rstRSSET.next())
						{hstLOTNO.put(L_strISTKEY_OLD,L_strLOTNO.substring(0,L_strLOTNO.length()-3)+"");break;}
            }
            L_rstRSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtLOTNO");
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
			hstDSRNM.clear();
            L_strSQLQRY = "select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct ivt_byrcd from mr_ivtrn  where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct ivt_cnscd from mr_ivtrn  where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = '"+(chkCSTKFL.isSelected() ? "G" : "D")+"' and pt_prtcd in (select distinct ivt_dsrcd from mr_ivtrn  where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'T' and pt_prtcd in (select distinct ivt_trpcd from mr_ivtrn  where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+(cl_dat.M_strUSRCT_pbst.equals("02") ? " and  IVT_DSRTP='"+cl_dat.M_strUSRCD_pbst.substring(0,1)+"' and IVT_DSRCD='"+cl_dat.M_strUSRCD_pbst.substring(1,6)+"'" : "")+")";
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
					if(strPRTTP.equals((chkCSTKFL.isSelected() ? "G" : "D")))
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
	private void setHST_ARR(Hashtable<String,String> LP_HSTNM)
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

		
	
		
		

	/** One time data capturing for  MR_IVTRN
	 * into the Hash Table
	 */
     private void crtIVTRN(ResultSet LP_RSSET)
    {
		hstIVTRN.clear();
        try
        {
            while(true)
            {
                    String[] staIVTRN = new String[intIVTRN_TOT];
                    staIVTRN[intAE_IVT_INVNO] = getRSTVAL(LP_RSSET,"IVT_INVNO","C");
                    staIVTRN[intAE_IVT_DORNO] = getRSTVAL(LP_RSSET,"IVT_DORNO","C");
                    staIVTRN[intAE_IVT_DORDT] = getRSTVAL(LP_RSSET,"IVT_DORDT","D");
                    staIVTRN[intAE_IVT_SALTP] = getRSTVAL(LP_RSSET,"IVT_SALTP","C");
                    staIVTRN[intAE_IVT_DTPCD] = getRSTVAL(LP_RSSET,"IVT_DTPCD","C");
                    staIVTRN[intAE_IVT_PMTCD] = getRSTVAL(LP_RSSET,"IVT_PMTCD","C");
                    staIVTRN[intAE_IVT_PORNO] = getRSTVAL(LP_RSSET,"IVT_PORNO","C");
                    staIVTRN[intAE_IVT_BYRCD] = getRSTVAL(LP_RSSET,"IVT_BYRCD","C");

					staIVTRN[intAE_IVT_INVDT] = getRSTVAL(LP_RSSET,"IVT_INVDT","D");
					staIVTRN[intAE_IVT_INDNO] = getRSTVAL(LP_RSSET,"IVT_INDNO","C");
					staIVTRN[intAE_IVT_INDDT] = getRSTVAL(LP_RSSET,"IVT_INDDT","D");
					staIVTRN[intAE_IVT_LRYNO] = getRSTVAL(LP_RSSET,"IVT_LRYNO","C");
					staIVTRN[intAE_IVT_LR_NO] = getRSTVAL(LP_RSSET,"IVT_LR_NO","C");
					staIVTRN[intAE_IVT_CNTDS] = getRSTVAL(LP_RSSET,"IVT_CNTDS","C");
					staIVTRN[intAE_IVT_PORDT] = getRSTVAL(LP_RSSET,"IVT_PORDT","D");
					staIVTRN[intAE_IVT_TRPCD] = getRSTVAL(LP_RSSET,"IVT_TRPCD","C");
					
					staIVTRN[intAE_IVT_DSRTP] = getRSTVAL(LP_RSSET,"IVT_DSRTP","C");
					staIVTRN[intAE_IVT_DSRCD] = getRSTVAL(LP_RSSET,"IVT_DSRCD","C");
					staIVTRN[intAE_IVT_INVQT] = getRSTVAL(LP_RSSET,"IVT_INVQT","N");
					staIVTRN[intAE_IVT_ASSVL] = getRSTVAL(LP_RSSET,"IVT_ASSVL","N");
					staIVTRN[intAE_IVT_EXCVL] = getRSTVAL(LP_RSSET,"IVT_EXCVL","N");
					staIVTRN[intAE_IVT_EDHVL] = getRSTVAL(LP_RSSET,"IVT_EDHVL","N");
					staIVTRN[intAE_IVT_FRTVL] = getRSTVAL(LP_RSSET,"IVT_FRTVL","N");
					staIVTRN[intAE_IVT_INVVL] = getRSTVAL(LP_RSSET,"IVT_INVVL","N");
					staIVTRN[intAE_IVT_CNSCD] = getRSTVAL(LP_RSSET,"IVT_CNSCD","C");
					
					
                    hstIVTRN.put(getRSTVAL(LP_RSSET,"IVT_INVNO","C"),staIVTRN);
                    if (!LP_RSSET.next())
                            break;
            }
            LP_RSSET.close();
        }
        catch(Exception L_EX)
        {
               setMSG(L_EX,"crtIVTRN");
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
		LP_CMBNM.removeAllItems();
		for(int i=0;i<LP_VTRNM.size(); i++)
        {
			
				//System.out.println(i+"  : "+LP_VTRNM.get(i).toString());
                LP_CMBNM.addItem(LP_VTRNM.get(i).toString());
        }
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
		 * @param LP_IVTRN_KEY	Inv.No.
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getIVTRN(String LP_IVTRN_KEY, String LP_FLDNM)
        {
		//System.out.println("getIVTRN : "+LP_IVTRN_KEY+"/"+LP_FLDNM);
        try
        {
                if (LP_FLDNM.equals("IVT_INVNO"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_INVNO];
                else if (LP_FLDNM.equals("IVT_DORNO"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_DORNO];
                else if (LP_FLDNM.equals("IVT_DORDT"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_DORDT];
                else if (LP_FLDNM.equals("IVT_SALTP"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_SALTP];
                else if (LP_FLDNM.equals("IVT_DTPCD"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_DTPCD];
                else if (LP_FLDNM.equals("IVT_PMTCD"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_PMTCD];
                else if (LP_FLDNM.equals("IVT_PORNO"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_PORNO];
                else if (LP_FLDNM.equals("IVT_BYRCD"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_BYRCD];
                else if (LP_FLDNM.equals("IVT_INVDT"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_INVDT];
                else if (LP_FLDNM.equals("IVT_INDNO"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_INDNO];
                else if (LP_FLDNM.equals("IVT_INDDT"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_INDDT];
                else if (LP_FLDNM.equals("IVT_LRYNO"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_LRYNO];
                else if (LP_FLDNM.equals("IVT_LR_NO"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_LR_NO];
                else if (LP_FLDNM.equals("IVT_CNTDS"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_CNTDS];
                else if (LP_FLDNM.equals("IVT_PORDT"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_PORDT];
                else if (LP_FLDNM.equals("IVT_TRPCD"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_TRPCD];
                else if (LP_FLDNM.equals("IVT_DSRTP"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_DSRTP];
                else if (LP_FLDNM.equals("IVT_DSRCD"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_DSRCD];
                else if (LP_FLDNM.equals("IVT_INVQT"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_INVQT];
                else if (LP_FLDNM.equals("IVT_ASSVL"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_ASSVL];
                else if (LP_FLDNM.equals("IVT_EXCVL"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_EXCVL];
                else if (LP_FLDNM.equals("IVT_EDHVL"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_EDHVL];
                else if (LP_FLDNM.equals("IVT_FRTVL"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_FRTVL];
                else if (LP_FLDNM.equals("IVT_INVVL"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_INVVL];
                else if (LP_FLDNM.equals("IVT_CNSCD"))
                        return ((String[])hstIVTRN.get(LP_IVTRN_KEY))[intAE_IVT_CNSCD];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getIVTRN");
			System.out.println("getIVTRN : "+LP_IVTRN_KEY+"/"+LP_FLDNM);
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

}	