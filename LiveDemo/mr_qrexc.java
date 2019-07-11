//Exceptional Transaction List
/** Purpose  : Viewing / Printing of Ammendments / Cancellation of Customer Order Transaction
 *              
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

class mr_qrexc extends cl_rbase implements MouseListener 
{
	private JTabbedPane tbpMAIN;
	private JPanel pnlINDRPT;
	private String strPRDCD,strPRTTP,strPRTCD,strWHRSTR, strWHRSTR1;	//strSTRDT,strENDDT,
	private String strZONSTR, strSALSTR, strSBSSTR;
	private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_qrexc.doc"; 
	private String strCUTDT = ""; 

	
	/** Labels for Various Options
	 */
	private JLabel lblRGN;
	private JLabel lblSCT;
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
	private JTextField txtSCT;
	private JTextField txtZON;
	private JTextField txtGRD;
	private JTextField txtDSR;
	private JTextField txtBYR;
	private JTextField txtSAL;
	private JTextField txtPMT;
	private JTextField txtPRD;
	private JTextField txtCNS;
	private JTextField txtREMDS;
	//private JCheckBox chkQRYFL;

	
	private JList lstOPTNM;			// JList for displaying details of options selected
	private Vector<String> vtrOPTNM;		// Vector for poulating data into the JList
	private cl_JTable tblINDRPT;   // JTable for displaying Selective, Grade & Customer order detail
	private cl_JTable tblINDTOT;   // JTable for displaying Selective, Grade & Customer order detail
	private boolean flgCHK_EXIST;	// Flag to check whether record exists in table
	
	

	
	private JButton btnPRINT_IND;

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
	
	private String strWHRINT_RGN = "";
	private String strWHRINT_SCT = "";
	private String strWHRINT_ZON = "";
	private String strWHRINT_GRD = "";
	private String strWHRINT_DSR = "";
	private String strWHRINT_BYR = "";
	private String strWHRINT_SAL = "";
	private String strWHRINT_PMT = "";
	private String strWHRINT_PRD = "";
	private String strWHRINT_CNS = "";

	private String strWHRIVT_RGN = "";
	private String strWHRIVT_SCT = "";
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
	private String strSCTWS = "Sector";
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

	
	private String strKEY01_INT = "";
	private String strKEY01_IVT = "";
	private String strKEY02_INT = "";
	private String strKEY02_IVT = "";
	
	
	
	
	private String[] arrINDHDR = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11"};
	private String[] arrINDHDT = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11"};
	private int[] arrINDHDR_WD = new int[]{0,1,2,3,4,5,6,7,8,9,10,11};
	private char[] arrINDHDR_PAD = new char[]{'0','1','2','3','4','5','6','7','8','9','0','1'};

	
	
	private JComboBox cmbPRINTERS;
	
	
	private	int intDSRCTR = 0, intBYRCTR = 0, intBLKCTR = 0; 

	//private String strQTFLD_INT, strVLFLD_INT;
	//private String  strCNSCD_OLD = "", strBYRCD_OLD="", strPRDCD_OLD ="";

	private double dblINDQT_TOT=0, dblRSNPR_TOT=0;
	
	private int intINDWD = 145;      // report width
	private int intINDCL = 11;		 // No.of columns in the report line

	private int intLINECT=72, intPAGENO=0, intRUNCL=0;
	boolean flgEOFCHK = false;

	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	
	private JLabel lblRPTWS;		
	private JLabel lblKEY01;		
	private JLabel lblKEY02;		
	private Vector<String> vtrRPTWS;		/**Vector for adding elements to cmbRPTWS */
	private JComboBox cmbRPTWS;		/**Combo-box for defining scope of the report */

	private JLabel lblMKTTP;		
	private Vector<String> vtrMKTTP;		/**Vector for adding elements to cmbMKTTP */
	private JComboBox cmbMKTTP;
	
	private Hashtable hstOPTNM;
	private Hashtable<String,String[]> hstCDTRN;			// Code Transaction details
	private Vector<String> vtrINTRN01_C2;			// Vector deciding the distict key value for first tab pane
	private Vector<String> vtrINTRN02_C2;			// Parallel vector for second key 
	private Hashtable<String,String[]> hstINTRN_C2;			// Selective details hash table

	private Vector<String> vtrINTRN_G2;			// Grade list for 
	private Hashtable hstINTRN_G1;			// Grade list with Tot.Qty for 
	private Hashtable hstINTRN_G2;			// Grade details for 

	
	private Hashtable<String,String> hstCODDS;			// Code Description
	private Hashtable<String,String[]> hstPRMST;			// Product Master details
	private Hashtable<String,String[]> hstPTMST;			// Party Details
	private Hashtable hstLOTNO;			// Party Details
	private Hashtable<String,String> hstDSRNM;			// Distibutor Short Name (as key) & Code (as value)
	private Vector<String> vtrDSRNM;			// Distributor Code & Short Name
	private Object[] arrHSTKEY;			// Object array for getting hash table key in sorted order
	
	mr_qrexc()
	{
		super(2);
		try
		{

			
			
			arrINDHDR[intTB3_CHKFL] = "xx";
			arrINDHDR[intTB3_PRDDS] = "Grade";
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

			
			vtrOPTNM = new Vector<String>();
			lstOPTNM = new JList();
			
			
			tbpMAIN = new JTabbedPane();
			pnlINDRPT = new JPanel(null);
			//txtSPCCT = new JTextField();

			txtRGN = new JTextField();
			txtSCT = new JTextField();
			txtZON = new JTextField();
			txtGRD = new JTextField();
			txtDSR = new JTextField();
			txtBYR = new JTextField();
			txtSAL = new JTextField();
			txtPMT = new JTextField();
			txtPRD = new JTextField();
			txtCNS = new JTextField();
			txtREMDS  = new JTextField();

			lblRGN = new JLabel("Region");
			lblSCT = new JLabel("Sector");
			lblZON = new JLabel("Zone");
			lblGRD = new JLabel("Grade");
			lblDSR = new JLabel("Distr.");
			lblBYR = new JLabel("Buyer");
			lblSAL = new JLabel("Sal.Type");
			lblPMT = new JLabel("Pmt.Type");
			lblPRD = new JLabel("Prd.Cat.");
			lblCNS = new JLabel("Cons.");
			lblREMDS = new JLabel("Remark");
			
			
			vtrINTRN01_C2 = new Vector<String>();
			vtrINTRN02_C2 = new Vector<String>();
			vtrINTRN_G2 = new Vector<String>();
			hstOPTNM = new Hashtable();
			hstCDTRN = new Hashtable<String,String[]>();
			hstINTRN_C2 = new Hashtable<String,String[]>();
			hstCODDS = new Hashtable<String,String>();
			hstPRMST = new Hashtable<String,String[]>();
			hstPTMST = new Hashtable<String,String[]>();
			hstLOTNO = new Hashtable();
			hstDSRNM = new Hashtable<String,String>();
			
			vtrRPTWS = new Vector<String>();
			vtrMKTTP = new Vector<String>();
			
			lblRPTWS  = new JLabel("Scope");
			lblKEY01  = new JLabel("Key Col 1:");
			lblKEY02  = new JLabel("Key Col 2:");
			lblMKTTP  = new JLabel("Market Type");

			lblRPTWS.setForeground(Color.blue);
			lblKEY01.setForeground(Color.blue);
			lblKEY02.setForeground(Color.blue);
			lblMKTTP.setForeground(Color.blue);


			btnPRINT_IND = new JButton("Document");
			

			crtPRMST();
			hstCDTRN.clear();
			crtCDTRN("'SYSMRXXRGN','SYSMRXXSCT','SYSMRXXPMT','MSTCOXXPGR', 'SYSMR00SAL', 'SYSMR00ZON'","",hstCDTRN);
			
			setMatrix(20,8);
			
			add(lblRPTWS,1,2,1,2,this,'L');
			add(cmbRPTWS=new JComboBox(),2,2,1,2,this,'L');
			
			add(lblKEY01,4,1,1,1,this,'L');
			add(lblKEY02,4,2,1,1,this,'L');

			add(lblMKTTP,1,1,1,1,this,'L');
			add(cmbMKTTP=new JComboBox(),2,1,1,1,this,'L');



			add(lblRGN,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtRGN,intSCT_ROW,intSCT_COL+1,1,1,this,'L');

			add(lblSCT,intSCT_ROW,intSCT_COL,1,1,this,'L');
			add(txtSCT,intSCT_ROW,intSCT_COL+1,1,1,this,'L');
			
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

			

			
			
			//add(chkSTOTFL,2,7,1,2,this,'L');
			add(new JLabel("Print"),6,6,1,0.5,this,'L');
			add(btnPRINT_IND,7,7,1,1,this,'L');
			


			tblINDRPT = crtTBLPNL1(pnlINDRPT,arrINDHDR,500,0,1,7,7.9,arrINDHDR_WD,new int[]{0});  
			tblINDTOT = crtTBLPNL1(pnlINDRPT,arrINDHDR,1,7,1,2.1,7.9,arrINDHDR_WD,new int[]{0});  
			
			tbpMAIN.addTab("Document Detail",pnlINDRPT);
			
			add(tbpMAIN,7,1,13,8,this,'L');
			tblINDRPT.addMouseListener(this);

			setVTRRPTWS();
			setVTRMKTTP();
			
			lstOPTNM.setBackground(Color.CYAN);
			remove(M_cmbDESTN);
			M_cmbDESTN.setVisible(true);
			M_vtrSCCOMP.remove(M_cmbDESTN);
			exeOPT_DSB();
			updateUI();
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"mr_qrexc");}
	}


	/** Disabling & Initializing the components
	 */	
	private void exeOPT_DSB()
	{
		try
		{
			exeOPT_DSB1();
			txtRGN.setText("");
			txtSCT.setText("");
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
			lblSCT.setVisible(false);
			lblZON.setVisible(false);
			lblGRD.setVisible(false);
			lblDSR.setVisible(false);
			lblBYR.setVisible(false);
			lblSAL.setVisible(false);
			lblPMT.setVisible(false);
			lblPRD.setVisible(false);
			lblCNS.setVisible(false);

			 txtRGN.setVisible(false);
			 txtSCT.setVisible(false);
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
			vtrRPTWS.addElement(strSCTWS);		
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
			//chkQRYFL.setSelected(false);

			hstOPTNM.clear();
			vtrOPTNM.clear();
			lstOPTNM.setListData(vtrOPTNM);
		}
		catch (Exception L_EX)
			{setMSG(L_EX,"clrCOMP");}
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
				setCMBVL(cmbRPTWS,vtrRPTWS);
				setCMBVL(cmbMKTTP,vtrMKTTP);
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
			if(M_objSOURC == M_txtFMDAT)
				M_txtTODAT.requestFocus();
			if(M_objSOURC == btnPRINT_IND)
				{
					strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_ingrr.doc"; 
					this.getParent().getParent().setCursor(cl_dat.M_curWTSTS_pbst);	this.setCursor(cl_dat.M_curDFSTS_pbst);
					crtINDRPT();
					exePRINT1();
				}
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
				if(L_FE.getSource() == txtRGN && txtRGN.getText().length()>0)
					{setOPTNM("RGN");getSUBHDR1();}
				else if(L_FE.getSource() == txtSCT && txtSCT.getText().length()>0)
					{setOPTNM("SCT");getSUBHDR1();}
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
				else if(LP_OPTCT.equals("SCT"))
					{vtrOPTNM.addElement(lblSCT.getText()+": "+getCDTRN("SYSMRXXSCT"+txtSCT.getText(),"CMT_CODDS",hstCDTRN));}
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
	 //
	String[] getPRINTERS()
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
				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
					{M_strHLPFLD = "txtRGN"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXRGN' and cmt_codcd in (select cmt_chp02 from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+"))  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strSCTWS))
					{M_strHLPFLD = "txtSCT"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXSCT' and cmt_codcd in (select cmt_modls from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+"))  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strZONWS))
					{M_strHLPFLD = "txtZON"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_codcd in ("+strZONSTR+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strGRDWS))
					{M_strHLPFLD = "txtGRD"; cl_hlp("Select PR_PRDCD, PR_PRDDS from co_prmst where substr(pr_prdcd,1,2) in ('51','52','53','54') and pr_prdcd in (select distinct  int_prdcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PR_PRDCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strDSRWS))
					{M_strHLPFLD = "txtDSR"; cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='D' and pt_prtcd in (select distinct  in_dsrcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strBYRWS))
					{M_strHLPFLD = "txtBYR"; cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' and pt_prtcd in (select distinct  in_byrcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strSALWS))
					{M_strHLPFLD = "txtSAL"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd in (select in_saltp from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPMTWS))
					{M_strHLPFLD = "txtPMT"; cl_hlp("Select CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='SYS' and cmt_cgstp='MRXXPMT' and cmt_codcd in (select  in_pmtcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strPRDWS))
					{M_strHLPFLD = "txtPRD"; cl_hlp("Select substr(CMT_CODCD,1,4) CMT_CODCD, CMT_CODDS from CO_CDTRN where cmt_cgmtp='MST' and cmt_cgstp='COXXPGR' and substr(cmt_codcd,1,4) in (select distinct  substr(int_prdcd,1,4) int_prdcd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+")  order by CMT_CODCD" ,2,1,new String[] {"Code","Description"},2,"CT");}
				if(cmbRPTWS.getSelectedItem().toString().equals(strCNSWS))
					{M_strHLPFLD = "txtCNS"; cl_hlp("Select PT_PRTCD, PT_PRTNM from CO_PTMST where pt_prttp='C' and pt_prtcd in (select distinct  in_cnscd from vw_intrn where "+setWHRSTR(M_txtFMDAT.getText(),M_txtTODAT.getText())+") order by PT_PRTCD" ,2,1,new String[] {"Code","Name"},2,"CT");}
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
			else if(M_strHLPFLD.equals("txtSCT"))
				{txtSCT.setText(L_STRTKN.nextToken());}
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
			

			String L_strWHRSTR1 = " ifnull(int_cfwqt,0)>0 and in_bkgdt < '"+strCUTDT+"' and "+strWHRSTR1;
				M_strSQLQRY = "select int_prdcd,int_prdds,int_indno,in_inddt,(ifnull(int_indqt,0)-ifnull(int_fcmqt,0)) int_indqt, ifnull(int_rsnvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)) int_rsnvl, ifnull(int_basrt,0) int_indvl, int(round(ifnull(int_cclvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_cclvl,  int(round(ifnull(int_comvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_comvl, 0 int_frtvl,  int(round(ifnull(int_crdvl,0)/(ifnull(int_indqt,1)-ifnull(int_fcmqt,0)),0)) int_crdvl, in_cnscd from vw_intrn where "+strWHRSTR;
				//+ " union all select int_prdcd,int_prdds,int_indno,in_inddt,ifnull(int_cfwqt,0) int_indqt, ifnull(int_rsnvl,0)/ifnull(int_indqt,0) int_rsnvl, ifnull(int_basrt,0) int_indvl, int(round(ifnull(int_cclvl,0)/ifnull(int_indqt,1),0)) int_cclvl,  int(round(ifnull(int_comvl,0)/ifnull(int_indqt,1),0)) int_comvl, 0 int_frtvl,  int(round(ifnull(int_crdvl,0)/ifnull(int_indqt,1),0)) int_crdvl, in_cnscd from vw_intrn where "+L_strWHRSTR1+"  order by int_indno,int_prdcd";

			//System.out.println(M_strSQLQRY);
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next() || rstRSSET==null)
				return;
			dblINDQT_TOT=0; dblRSNPR_TOT=0;
			double L_dblINDQT=0, L_dblRSNVL=0;
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
				tblINDRPT.setValueAt(L_strCNSNM,i,intTB3_CNSNM);
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
			strZONSTR = setSBSSTR("ZON");
			strSALSTR = setSBSSTR("SAL");
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
				if(cmbRPTWS.getSelectedItem().toString().equals(strRGNWS))
					{exeGETSCT(lblRGN, txtRGN);}
				if(cmbRPTWS.getSelectedItem().toString().equals(strSCTWS))
					{exeGETSCT(lblSCT, txtSCT);}
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
				if(txtSCT.getText().length()>0)
				{
					strWHRINT_SCT = setWHRSUB(strWHRINT_SCT, " in_zoncd in (select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_modls = '"+txtSCT.getText()+"')");
					strWHRIVT_SCT = setWHRSUB(strWHRIVT_SCT, " ivt_zoncd in (select cmt_codcd from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00ZON' and cmt_modls = '"+txtSCT.getText()+"')");
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
					strWHRINT_PRD = setWHRSUB(strWHRINT_PRD," substr(int_prdcd,1,4) = '"+txtPRD.getText()+"'"); 
					strWHRIVT_PRD = setWHRSUB(strWHRIVT_PRD," substr(ivt_prdcd,1,4) = '"+txtPRD.getText()+"'"); 
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
				strWHRINT_SCT = "";
				strWHRINT_ZON = "";
				strWHRINT_GRD = "";
				strWHRINT_DSR = "";
				strWHRINT_BYR = "";
				strWHRINT_SAL = "";
				strWHRINT_PMT = "";
				strWHRINT_PRD = "";
				strWHRINT_CNS = "";

				strWHRIVT_RGN = "";
				strWHRIVT_SCT = "";
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
			strWHRSTR1 = "";
				if(strWHRINT_RGN.length() > 2)
				{strWHRSTR1 += " and ("+strWHRINT_RGN+") ";}
				if(strWHRINT_SCT.length() > 2)
				{strWHRSTR1 += " and ("+strWHRINT_SCT+") ";}
				if(strWHRINT_ZON.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_ZON+") ";}
				if(strWHRINT_GRD.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_GRD+") ";}
				if(strWHRINT_DSR.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_DSR+") ";}
				if(strWHRINT_BYR.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_BYR+") ";}
				if(strWHRINT_SAL.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_SAL+") ";}
				if(strWHRINT_PMT.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_PMT+") ";}
				if(strWHRINT_PRD.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_PRD+") ";}
				if(strWHRINT_CNS.length() > 2)
					{strWHRSTR1 += " and ("+strWHRINT_CNS+") ";}
				//System.out.println(strWHRSTR);
				strWHRSTR = strWHRSTR + strWHRSTR1;
			    strWHRSTR1 = " INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND int_stsfl  <>'X' and (ifnull(int_indqt,0)-ifnull(int_fcmqt,0))>0  and  int_mkttp  = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'"
				+ " and int_sbscd  in "+M_strSBSLS+ strWHRSTR1;
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
			txtSCT.setText("");
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
			tbpMAIN.remove(pnlINDRPT);
			pnlINDRPT.removeAll();

			pnlINDRPT = new JPanel(null);
			
			int i;
			M_strSQLQRY = "select count(*) int_recct from  vw_intrn  where "+strWHRSTR;
			ResultSet rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!rstRSSET.next()  || rstRSSET==null)
				{setMSG("No Detail Records Found",'E');return false;}
			int L_intRECCT = Integer.parseInt(getRSTVAL(rstRSSET,"InT_RECCT","N"));
			L_intRECCT = 3*L_intRECCT;
			rstRSSET.close();
			//System.out.println("004");

			tblINDRPT = crtTBLPNL1(pnlINDRPT,arrINDHDR,L_intRECCT,0,1,7,7.9,arrINDHDR_WD,new int[]{0});  
			tblINDTOT = crtTBLPNL1(pnlINDRPT,arrINDHDR,1,7,1,2.1,7.9,arrINDHDR_WD,new int[]{0});  

			tbpMAIN.addTab("Document Detail",pnlINDRPT);
		
			//System.out.println("005");
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
			String L_strDTFLD = " in_bkgdt ";
				
			L_strRETSTR = L_strDTFLD +" between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and  int_stsfl  <>'X' and (ifnull(int_indqt,0)-ifnull(int_fcmqt,0))>0  and  int_mkttp  = '"+cmbMKTTP.getSelectedItem().toString().substring(0,2)+"'"
				+ " and  INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND int_sbscd  in "+M_strSBSLS;
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
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct  in_byrcd from vw_intrn  where "+strWHRSTR+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'C' and pt_prtcd in (select distinct  in_cnscd from vw_intrn where "+strWHRSTR+")"
				+ " union all select PT_PRTTP,PT_PRTCD,PT_PRTNM,PT_ZONCD,PT_SHRNM,PT_ADR01,PT_ADR02,PT_ADR03 "
				+" from co_ptmst where pt_prttp = 'D' and pt_prtcd in (select distinct  in_dsrcd from vw_intrn  where "+strWHRSTR+")";
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
	private void setHST_ARR(Hashtable<String,String[]> LP_HSTNM)
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
     private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
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