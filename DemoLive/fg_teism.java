/*
System Name    : Finished Goods Inventory Management System
Program Name   : Issue Entry Form
Program Desc.  : Issues are prepared for PS/SPS Processed Material,PS/SPS ReBagging,Ware House
				 Transfer,Sales Return,Stock Adjustment & Job Work. Stocks are Allocated as per the
				 requested Quantity as well as Issues are Authorized by the Authority person.
Author         : Mr. Deepal Mehta  (Modified by Mr. Zaheer A. Khan)
Date           : 
Version        : FIMS 2.0
Modificaitons  :
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;import javax.swing.JTable;import javax.swing.InputVerifier;
import javax.swing.JComponent;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import java.util.StringTokenizer;import java.util.Hashtable;import java.awt.Color;
import java.sql.ResultSet;import javax.swing.JPanel;import javax.swing.JTabbedPane;
import java.sql.CallableStatement;import javax.swing.JComboBox;
import javax.swing.JOptionPane;import javax.swing.JButton;import java.awt.Dimension;
import java.util.Date;import java.util.Calendar;
import java.math.BigDecimal;import javax.swing.JWindow;import javax.swing.JFrame;

/**
<P><PRE style = font-size : 10 pt >

<b>Purpose :</b> Issues are prepared for PS/SPS Processed Material,PS/SPS ReBagging,Ware House
				 Transfer,Sales Return,Stock Adjustment & Job Work. Stocks are Allocated as per the
				 requested Quantity as well as Issues are Authorized by the Authority person.

Points to be Noted :
	LT_RESFL = 'Q' in PR_LTMST indicate that the Lot has been marked for Quality Hold for despatch,
    such lots are not displayed / allowed for despatch operation
	LT_RESFL = 'H' in PR_LTMST indicate that the Lot has been marked for Ageing Quality Hold for despatch,
    such lots are not displayed / allowed for despatch operation

*/

class fg_teism extends cl_pbase
{
	JComboBox cmbWRHTP;
	JTextField txtISSTP,txtMKTTP,txtISSNO,txtISSDT,txtISRMK,txtDORNO,txtDORDT,txtSALTP;
	JTextField txtLRYNO,txtCNTDS,txtDESTN;
	JLabel lblTRPNM,lblBYRNM,lblBAD01,lblBAD02,lblCNSNM,lblCAD01,lblCAD02,lblISSTS,lblISQTY,lblISSPK;
    JTextField txtTSLNO,txtRSLNO,txtEDITR,txtEDITR_MBP,txtEDITR_VHD;

	private JTextField txtRESQT;
	private JTextField txtISSQT;
	private JTextField txtLOTNO;
	private JTextField txtPRDCD;
	private JTextField txtTDSFL;
	private JCheckBox chkCHKFL1;

	//private JTextField txtEDITR_VHD;
	
	private JCheckBox chkCHKFL5;
	private JTextField txtLOTNO5;
	private JTextField txtSRLNO5;
	private JTextField txtTRMDT5,txtSEQNO;;
	private JRadioButton rdbISALO,rdbISAUT,rdbFRSIDE,rdbBKSIDE,rdbREPRINT;
	
	private JPanel pnlRESEV;
	private JPanel pnlDSPST;
	private JPanel pnlVHREJ_E;
	private JPanel pnlVHREJ_V;
	private JPanel pnlMBPLT;
	private JPanel pnlVHDTN;
	private JLabel lblSEQ;
	
	ButtonGroup btgENTRY;
	ButtonGroup chkPRNCT;    // Print category button group
	cl_JTable tblGRDTL,tblLTDTL,tblVEHRJ,tblDSPST,tblMBPLT,tblVHDTN;
	
	private JCheckBox chkPPRN;     // Checkbox for setting preprinted status ON/Off
	private JCheckBox chkDSPST;
	private JCheckBox chkREAUT;
	private JCheckBox chkMBPLT;
	private JCheckBox chkVHDTN;
	private JCheckBox chkVHREJ_E;
	private JCheckBox chkVHREJ_V;
	private JButton btnPRINT_LA;
	private String strISSTP;
	private String strSTKTP ;
	//private String strMKTTP;
	private String strISSNO;
	private String strSALTP, strSEQ, strLDGCT;
	
	TxtLimit txtGINNO_VR;
	TxtLimit txtREJCD_VR;
	TxtLimit txtDETCD_VHD;
	JLabel lblREJDS_VR,lblVHDMSG,lblMBPMSG;
	TxtDate txtREJDT_VR;
	TxtLimit txtCORFL_VR;
	TxtLimit txtDSPFL_VR;
	TxtLimit txtREMDS_VR;
	TxtLimit txtREJBY_VR;
	private JCheckBox chkRESFL;
	private JCheckBox chkAUTFL;

	//Column numbers for Grade Table
	final int TB1_CHKFL = 0;
    final int TB1_PRDCD = 1;
    final int TB1_PRDDS = 2;
	final int TB1_PKGTP = 3;
    final int TB1_REQQT = 4;
    final int TB1_REQPK = 5;
    //final int TB1_RESNO = 6;
    //final int TB1_RESQT = 7;

	//Column numbers for despatch entry table
    final int TB2_CHKFL = 0;
	final int TB2_TDSFL = 1;
    final int TB2_LOTNO = 2;
	final int TB2_RCLNO = 3;
    final int TB2_REMRK = 4;
    final int TB2_MNLCD = 5;
	final int TB2_PKGCT = 6;
    final int TB2_PKGTP = 7;
	final int TB2_ISSQT = 8;
    final int TB2_ISSPK = 9;
    //final int TB2_RESQT = 10;
							 
	final int TB3_CHKFL = 0;
    final int TB3_DOCNO = 1;
	final int TB3_REJCD = 2;
    final int TB3_REJDS = 3;
    final int TB3_REJDT = 4;
	final int TB3_CORFL = 5;
    final int TB3_DSPFL = 6;
    final int TB3_REMDS = 7;
    final int TB3_LRYNO = 8;
    final int TB3_CNTDS = 9;

	final int TB4_CHKFL = 0;
	final int TB4_LRYNO = 1;
    final int TB4_LADTM = 2;
    final int TB4_ALOTM = 3;
    final int TB4_LODTM = 4;
    final int TB4_STSDS = 5;
    final int TB4_GINNO = 6;
    final int TB4_LADNO = 7;
    final int TB4_PRDDS = 8;
    final int TB4_PKGTP = 9;
    final int TB4_REQQT = 10;
    final int TB4_TRPNM = 11;
	
	final int TB5_CHKFL = 0;
	final int TB5_SRLNO = 1;
    final int TB5_LOTNO = 2;
    final int TB5_TRMDT = 3;
    final int TB5_PLTQT = 4;
	
	final int TB6_CHKFL = 0;
	final int TB6_SRLNO = 1;
    final int TB6_DETCD = 2;
    final int TB6_DETDS = 3;
    final int TB6_STRTM = 4;
    final int TB6_ENDTM = 5;
    final int TB6_EFFTM = 6;

  
    // Column number variables for Reserved LOts Display Table
	final int TB7_GRPNM=1;
    final int TB7_LOTNO=2;
    final int TB7_AVLQT=3;
    final int TB7_RESQT=4;
    final int TB7_RDSQT=5; 
    final int TB7_BALQT=6;
    final int TB7_STRDT=7;
    final int TB7_ENDDT=8;
    final int TB7_RESNO=9;


    // Column number variables for Quality Hold Lots Display Table
	final int TB8_PRDDS=1;
    final int TB8_LOTNO=2;
    final int TB8_RCLNO=3;
    final int TB8_REMDS=4;
    final int TB8_STKQT=5;
	
	
	//final int TB7_LOTNO=1;
    //final int TB7_PRTNM=2;
    //final int TB7_RESQT=3;
    //final int TB7_RDSQT=4;
    //final int TB7_STKQT=5;
    //final int TB7_STRDT=6;
    //final int TB7_ENDDT=7;
    //final int TB7_RESNO=8;
    

	
	int intTRGHR_L = 2;    // Target time for Domestic Despatches
	int intTRGMN_L = 20;
	
	int intTRGHR_E = 1;		// Target time for Export Despatches
	int intTRGMN_E = 45;

	
/** Array elements for Issue Transaction Detail*/

	private int intISTRN_TOT = 1;
	private int intAE_IST_ISSQT = 0;		
	
	private String strWRHTP = "01";
	private String strMKTTP,strTDSFL,strISSDT,strBAD01,strBAD02,strLOTNO,strRCLNO;
	private String strMNLCD,strSTKQT,strISSPK,strGRPCD;
	private String  strTRPCD,strTRPNM, strGINNO,strLADNO, strLRYNO,strCNTDS,strBYRCD,strCNSCD;
	private String strPRDCD,strPRDDS,strSTSFL,strPKGTP,strISSQT,strPKGCT,strCCSVL;/*,strRESNO;*/
	private String strALOQT;
	private String LM_TBLLOTNO,LM_TBLRCLNO,LM_TBLMNLCD,LM_TBLPKGTP,LM_TBLSTKQT;				
	private String LM_DISTR,strADLSTR;
	boolean LM_INDISS=true;
	boolean LM_DATFL=true;
	private Hashtable<String,String> hstISTRN;
	private Hashtable<String,String> hstSTCHK;
	boolean flgCHK_EXIST;
	String strWHERE;
	
	private boolean flgRESFL=false;
	private boolean flgAUTFL=false;
	boolean flgENBL=true;
	String strDOCTP = "03";
	String strPRDTP,strGRDQT;
	private String strLOTQT;
	private String strRETQT;
	private String strHLDQT;
	private String strUPDFL;
	private String strRSLNO;
	private String strTSLNO;
	private String strDESTN;
	private String strREFDT;
	private String strISSEX;
	private double dblRTHQT;
	private double dblLOTQT;	
	private double dblGRDQT1;		// Grade Qty.Total from Entry Table
	private Hashtable<String,String> hstLTDTL;
	double LM_LTGRQT;
	int LM_LTGRPK;		//determines Lot JTable's Total Packages.
	private double dblTOTQT,dblTOTQT1,dblOLDISS;
	
	String LM_UPDQT; //determines Updated Qty.
	
	double dblDSTQT; //used as an dummy variable for adding the Stock qty.
	double dblDUPQT; //used as an variable for determining the difference between LM_STKQT & LM_ALOQT
	double dblDALQT; //used as an variable for the allocated Qty.

	private static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	

	
	String strCHECK1="STRIN";
	String strCHECK2="NEWST";
	String strCHECK3="SpecifiedLotNum";
	private CallableStatement cstPLTRN_PMR ;
	private String strRESFL; 
    private JCheckBox chkRESEV;
    private cl_JTable tblRESEV;
    private cl_JTable tblQLTHLD;
    private JLabel lblGRAD;

	fg_teism()
	{
		super(2);
		try
		{
			setMatrix(21,9);
			btgENTRY=new ButtonGroup();
			chkPRNCT=new ButtonGroup();
			cmbWRHTP=new JComboBox();
			hstISTRN=new Hashtable<String,String>();
			hstSTCHK=new Hashtable<String,String>();
			hstLTDTL = new Hashtable<String,String>();
			cmbWRHTP.addItem("01");
			add(new JLabel("W.House"),1,1,1,1,this,'L');
			add(cmbWRHTP,2,1,1,1,this,'L');
					
			add(new JLabel("Dsp/Mkt.Type"),1,2,1,1,this,'L');
			add(txtISSTP = new TxtLimit(2),2,2,1,.4,this,'L');
			add(txtMKTTP = new TxtLimit(2),2,2,1,.6,this,'R');
		
			add(new JLabel("L.A.NO"),1,3,1,1,this,'L');
			add(txtISSNO = new TxtLimit(8),2,3,1,1,this,'L');
				
			add(new JLabel("L.A. Date"),1,4,1,1,this,'L');
			add(txtISSDT = new TxtDate(),2,4,1,1,this,'L');
		
			add(new JLabel("Trpt. SealNo."),1,5,1,1,this,'L');
			add(txtTSLNO = new TxtLimit(10),2,5,1,1,this,'L');
		
			add(new JLabel("Rail.Seal No"),1,6,1,1,this,'L');
			add(txtRSLNO = new TxtLimit(10),2,6,1,1,this,'L');

			
			add(new JLabel("D.O.NO"),3,1,1,1,this,'L');
			add(txtDORNO = new TxtLimit(8),4,1,1,1,this,'L');

			add(lblSEQ=new JLabel("Seq.No"),3,4,1,1,this,'L' );
			add(txtSEQNO=new JTextField(),4,4,1,0.5,this,'L');

			add(new JLabel("D.O.Date"),3,2,1,1,this,'L');
			add(txtDORDT = new TxtDate(),4,2,1,1,this,'L');
		
			add(new JLabel("Type of Sale"),3,3,1,1,this,'L');
			add(txtSALTP = new TxtLimit(10),4,3,1,1,this,'L');
						
			add(new JLabel("Transporter : "),4,5,1,1,this,'L');
			add(lblTRPNM = new JLabel(""),4,6,1,3,this,'L');
					
			add(new JLabel("Buyer       : "),6,5,1,1,this,'L');
			add(lblBYRNM = new JLabel(""),6,6,1,3,this,'L');
			add(lblBAD01 = new JLabel(""),7,6,1,3,this,'L');
			add(lblBAD02 = new JLabel(""),8,6,1,3,this,'L');
																								
			add(new JLabel("Consignee   : "),9,5,1,1,this,'L');
			add(lblCNSNM = new JLabel(""),9,6,1,3,this,'L');
			add(lblCAD01 = new JLabel(""),10,6,1,3,this,'L');
			add(lblCAD02 = new JLabel(""),11,6,1,3,this,'L');
			add(new JLabel("Lorry No."),5,1,1,1,this,'L');
			add(txtLRYNO = new TxtLimit(15),6,1,1,1,this,'L');												
		
			add(new JLabel("Container"),5,2,1,1,this,'L');
			add(txtCNTDS = new TxtLimit(15),6,2,1,1,this,'L');
		
			add(new JLabel("Destination"),5,3,1,1,this,'L');
			add(txtDESTN = new TxtLimit(15),6,3,1,1,this,'L');
			add(chkAUTFL = new JCheckBox("Suitable Lots"),5,4,1,2,this,'L');
			add(chkRESFL = new JCheckBox("Reserved Lots"),6,4,1,2,this,'L');
		
		
			btgENTRY.add(rdbISALO);
			btgENTRY.add(rdbISAUT);		
		
			add(chkDSPST = new JCheckBox("Display Status"),2,7,1,2,this,'L');
		
			add(rdbISALO=new JRadioButton("Allocate",true),12,6,1,2,this,'L');
			add(rdbISAUT=new JRadioButton("Authorize"),12,8,1,1.5,this,'L');
			btgENTRY.add(rdbISALO);
			btgENTRY.add(rdbISAUT);
			
			add(lblISSTS = new JLabel(""),13,6,1,3,this,'L');
			add(chkVHREJ_E = new JCheckBox("Enter Veh.Rejn"),14,6,1,2,this,'L');
			add(chkVHDTN = new JCheckBox("Veh.Detention"),14,8,1,2,this,'L');
			add(chkVHREJ_V = new JCheckBox("View Veh.Rejn."),15,6,1,2,this,'L');
			add(chkMBPLT = new JCheckBox("M.B.Pallet"),15,8,1,2,this,'L');
			add(chkRESEV=new JCheckBox("Res.Status"),16,6,1,1.5,this,'L');//adding the 23-05-2007
			add(chkREAUT = new JCheckBox("Re-Authorise"),16,8,1,1.5,this,'L');
					
					
			add(new JLabel("Total "),19,4,1,1,this,'L');
			add(lblISQTY = new JLabel(""),19,5,1,0.5,this,'L');
			 
			//add(new JLabel("Total Pkg."),11,7,1,1,this,'L');
			add(lblISSPK = new JLabel(""),19,6,1,0.5,this,'L');
			
			add(new JLabel("Remark"),12,1,1,1,this,'L');
			
			add(txtISRMK = new TxtLimit(200),12,2,1,4,this,'L');
			
			String[] L_strTBLHD = {"Tag","Status","Lot No.","Rcl. No.","Remark","Main Loc.","package Cat.","Package Type","Quantity","Packages"};
			int[] L_intCOLSZ = {20,50,80,30,80,50,30,30,80,80};
			tblLTDTL = crtTBLPNL1(this,L_strTBLHD,60,13,1,6,5,L_intCOLSZ,new int[]{0});
			
			String[] L_strTBLHD1 = {"S.No","Product Code","Description.","Pkg Type","Qty","Pkg."};
			int[] L_intCOLSZ1 = {15,80,80,30,50,50};
			tblGRDTL = crtTBLPNL1(this,L_strTBLHD1,50,7,1,5,4,L_intCOLSZ1,new int[]{0});
					
			tblGRDTL.setInputVerifier(new TBLINPVF());
			tblGRDTL.addKeyListener(this);
			tblGRDTL.setCellEditor(TB1_CHKFL,chkCHKFL1 = new JCheckBox());
			tblGRDTL.setCellEditor(TB1_PRDCD,txtPRDCD = new TxtLimit(10));
			
			chkCHKFL1.addKeyListener(this);
			txtPRDCD.addKeyListener(this);
			txtPRDCD.addFocusListener(this);
			
			tblLTDTL.setInputVerifier(new TBLINPVF());
			tblLTDTL.addKeyListener(this);
			tblLTDTL.setCellEditor(TB2_CHKFL,chkCHKFL1 = new JCheckBox());
			tblLTDTL.setCellEditor(TB2_LOTNO,txtLOTNO = new TxtLimit(10));
			tblLTDTL.setCellEditor(TB2_ISSQT,txtISSQT = new TxtLimit(10));
			tblLTDTL.setCellEditor(TB2_TDSFL,txtTDSFL = new TxtLimit(10));
			
			txtTDSFL.addKeyListener(this);
			txtTDSFL.addFocusListener(this);
			txtLOTNO.addKeyListener(this);
			txtLOTNO.addFocusListener(this);
			txtISSQT.addKeyListener(this);
			txtISSQT.addFocusListener(this);
					
						
			add(rdbFRSIDE=new JRadioButton("Front Page",true),19,1,1,1,this,'L');
			add(rdbBKSIDE=new JRadioButton("Back Page"),19,2,1,1,this,'L');
			add(rdbREPRINT=new JRadioButton("Reprint"),19,3,1,1,this,'L');
			chkPRNCT.add(rdbFRSIDE);
			chkPRNCT.add(rdbBKSIDE);	
			chkPRNCT.add(rdbREPRINT);
			
			add(chkPPRN = new JCheckBox("PrePrinted"),19,8,1,1,this,'L');
			add(btnPRINT_LA = new JButton("PRINT"),19,9,1,1,this,'L');
			setENBL(false);
			getREFDT(cl_dat.M_strLOGDT_pbst);
			

			INPVF objFGIPV=new INPVF();
			for(int i=0;i<M_vtrSCCOMP.size();i++)
			{
				if(!(M_vtrSCCOMP.elementAt(i) instanceof JLabel))
				{
					((JComponent)M_vtrSCCOMP.elementAt(i)).setInputVerifier(objFGIPV);
					if(M_vtrSCCOMP.elementAt(i) instanceof JComboBox)
						((JComboBox)M_vtrSCCOMP.elementAt(i)).addItemListener(this);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
			txtISSTP.setText("10");
			txtMKTTP.setText("01");
			
			txtISSDT.setEnabled(false);
			txtDORNO.setEnabled(false);
			txtDORDT.setEnabled(false);
			txtSALTP.setEnabled(false);
			txtLRYNO.setEnabled(false);
					
			lblTRPNM.setEnabled(false);
					
			lblBYRNM.setEnabled(false);
			lblBAD01.setEnabled(false);
			lblBAD02.setEnabled(false);
			lblCNSNM.setEnabled(false);
			lblCAD01.setEnabled(false);
			lblCAD02.setEnabled(false);
			txtSEQNO.setEnabled(false);		
			
			lblISQTY.setEnabled(false);
			lblISSPK.setEnabled(false);
			lblISSTS.setEnabled(false);
					
			tblGRDTL.cmpEDITR[TB1_PRDDS].setEnabled(false);
			tblGRDTL.cmpEDITR[TB1_PKGTP].setEnabled(false);
			tblGRDTL.cmpEDITR[TB1_REQQT].setEnabled(false);
			tblGRDTL.cmpEDITR[TB1_REQPK].setEnabled(false);
			
			tblLTDTL.cmpEDITR[TB2_RCLNO].setEnabled(false);
			tblLTDTL.cmpEDITR[TB2_REMRK].setEnabled(false);
			tblLTDTL.cmpEDITR[TB2_MNLCD].setEnabled(false);
			tblLTDTL.cmpEDITR[TB2_PKGTP].setEnabled(false);
			tblLTDTL.cmpEDITR[TB2_PKGCT].setEnabled(false);
			
	}
	
/**
 * Function for getting Reference Date
*/
	
	public void getREFDT(String P_strLOGDT)//get reference date
	{
		try
		{
			Date L_strTEMP=null;
			String L_strREFDT="";
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				L_rstRSSET.close();
				L_strREFDT=strREFDT;			
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
				strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				//System.out.println("REFDT = "+strREFDT);
				//if(!P_strLOGDT.equals(strREFDT))
				//{
				//	setMSG("Invalid Date ",'E'); 
				//	cl_dat.M_cmbOPTN_pbst.setEnabled(false);
				//	JOptionPane.showMessageDialog(this,"Transactions upto "+L_strREFDT+" have been already locked.","Entry Status",JOptionPane.INFORMATION_MESSAGE);					
				//	flgENBL=false;
				//	cl_dat.M_cmbOPTN_pbst.setEnabled(false);
				//	setENBL(false);
				//  this.dispose();
				//}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			strMKTTP=txtMKTTP.getText().trim();
			strMKTTP=txtMKTTP.getText().trim();
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{		
				txtISSTP.requestFocus();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					chkREAUT.setEnabled(true);
					txtISSTP.setEnabled(true);
					txtISSNO.setEnabled(true);
					tblGRDTL.cmpEDITR[TB1_PRDCD].setEnabled(true);
					rdbFRSIDE.setEnabled(true);
					rdbBKSIDE.setEnabled(true);
					rdbREPRINT.setEnabled(true);
					chkPPRN.setEnabled(true);
				}
			}
			
			if(M_objSOURC==btnPRINT_LA)
			{
				setMSG("Loading Advice Printing",'N');
			    mr_tplad objTPLAD = new mr_tplad();
				objTPLAD.chkPPRN.setSelected((chkPPRN.isSelected()==true ? true : false));
				objTPLAD.LM_PPRNFL = (chkPPRN.isSelected()==true ? true : false);
				objTPLAD.txtPRDTP.setText(txtMKTTP.getText().trim());
				if(rdbFRSIDE.isSelected())
				{
					objTPLAD.prnALLREC_F(strMKTTP,strLADNO,strLADNO);
					JComboBox L_cmbLOCAL = objTPLAD.getPRNLS();
					objTPLAD.doPRINT(cl_dat.M_strREPSTR_pbst+"mr_tpla1.doc",L_cmbLOCAL.getSelectedIndex());
					//exeRPTPRN("MR_TPLA1.DOC");
				}
				else if(rdbBKSIDE.isSelected())
				{
					objTPLAD.prnALLREC_B(strMKTTP,strLADNO,strLADNO);
					JComboBox L_cmbLOCAL = objTPLAD.getPRNLS();
					objTPLAD.doPRINT(cl_dat.M_strREPSTR_pbst+"mr_tpla2.doc",L_cmbLOCAL.getSelectedIndex());
						
					//exeRPTPRN("MR_TPLA2.DOC");
				}
				else if(rdbREPRINT.isSelected())
				{
					if(chkSTSFL())
					{
						
						objTPLAD.prnALLREC_R(strMKTTP,strLADNO,strLADNO);
						JComboBox L_cmbLOCAL = objTPLAD.getPRNLS();
					    objTPLAD.doPRINT(cl_dat.M_strREPSTR_pbst+"MR_TPLAR.doc",L_cmbLOCAL.getSelectedIndex());
						
						//exeRPTPRN("MR_TPLAR.DOC");
					}
					else
						JOptionPane.showMessageDialog(this,"All the Issues of Loading Advice Number "+strISSNO+" are not authorized.","Issue Printing Status",JOptionPane.INFORMATION_MESSAGE);				 
				}
				//}
				txtISSNO.requestFocus();
			}
			
			if(M_objSOURC==chkRESEV)
			{
				if(chkRESEV.isSelected())
				{
					chkRESEV(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TB1_PRDCD).toString());
				}
				else
				{
				    setMSG("No Data Found",'E');
				}
			}
			if(M_objSOURC==chkDSPST)
			{
				if(chkDSPST.isSelected())
				{
					dspDSPST();
				}
			}
			if(M_objSOURC==chkVHREJ_E)
			{
				if(chkVHREJ_E.isSelected())
				{
					getVEHREJ();
					
				}
			}
			if(M_objSOURC==chkVHREJ_V)
			{
				if(chkVHREJ_V.isSelected())
				{
					dspVEHREJ();
				}
			}
			if(M_objSOURC==chkVHDTN)
			{
				if(chkVHDTN.isSelected())
				{
					getVHDTN(txtISSNO.getText());
				}
			}
			if(M_objSOURC==chkMBPLT)
			{
				if(chkMBPLT.isSelected())
				{
					getMBPLT(txtISSNO.getText()); 	
					//this.setCursor(curDFSTS);
				}
			}
			if(M_objSOURC==chkREAUT)
			{
				if(chkREAUT.isSelected())
				{
					exeREAUT(txtISSNO.getText()); 	
					//this.setCursor(curDFSTS);
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"ActionPerformed");
		}
	
	}
	
	
	private boolean chkSTSFL()
	{
		try
		{
			String L_strPRVST = "";
			String L_strNXTST = "";
			int i=0;
			M_strSQLQRY = "Select distinct IST_STSFL from FG_ISTRN";
			M_strSQLQRY += " where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strISSTP+"'";
			M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"' and IST_STSFL not in 'X'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(L_rstRSSET.next())
			{
				L_strNXTST = L_rstRSSET.getString("IST_STSFL").toString().trim();
				if(!L_strNXTST.equals(L_strPRVST))
				{
					L_strPRVST = L_strNXTST;
					i++;
				}
			}
			if(L_rstRSSET !=null)
				L_rstRSSET.close();
			if(i > 1)
				return false;
		else
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkSTSFL");
		}
		return false;
	}
	
	/*
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
		if(M_objSOURC==txtPRDCD)
		{
			txtISRMK.requestFocus();
		}
		
	}
	*/
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC==txtISSTP)
				{
					strISSTP=txtISSTP.getText().trim();
					vldISSTP();
				}
						
				if(M_objSOURC==txtISSNO)
				{
					
					tblGRDTL.clrTABLE();
					tblLTDTL.clrTABLE();
					if(txtISSNO.getText().length()==8)
						{txtMKTTP.setText(txtISSNO.getText().substring(1,3)); strMKTTP = txtMKTTP.getText().trim();}
					if(tblLTDTL.isEditing())
						tblLTDTL.getCellEditor().stopCellEditing();
					tblLTDTL.setRowSelectionInterval(0,0);
					tblLTDTL.setColumnSelectionInterval(0,0);
				
				
					vldISSNO();
					//if(tblGRDTL.isEditing())
					//	tblGRDTL.getCellEditor().stopCellEditing();
					
					//tblGRDTL.setRowSelectionInterval(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedRow());		
					//tblGRDTL.setColumnSelectionInterval(TB1_PRDCD,TB1_PRDCD);		
					//tblGRDTL.editCellAt(tblGRDTL.getSelectedRow(),TB1_PRDCD);
					//txtMOPCD.setText("01");
					//tblGRDTL.cmpEDITR[TB1_PRDCD].requestFocus();
					txtSEQNO.requestFocus();
				}
				if(M_objSOURC==txtPRDCD)
				{
					//txtISRMK.requestFocus();
				}
				if(M_objSOURC==txtISRMK)
				{
					tblLTDTL.requestFocus();
					tblLTDTL.setRowSelectionInterval(tblLTDTL.getSelectedRow(),tblLTDTL.getSelectedRow());		
					tblLTDTL.setColumnSelectionInterval(TB2_TDSFL,TB2_TDSFL);		
					tblLTDTL.editCellAt(tblLTDTL.getSelectedRow(),TB2_TDSFL);
					tblLTDTL.cmpEDITR[TB2_TDSFL].requestFocus();
				}
				if(M_objSOURC==txtSEQNO)
				{
				   
				    
				    strSEQ=vldSEQNO();
				    //System.out.println("This is txtsEQno" +strSEQ);
				  if(strSEQ.equals(txtSEQNO.getText().toString().trim()))  
				  {
				   
				        setMSG("Valid Sequence No",'N');
				        
				        if(tblGRDTL.isEditing())
							tblGRDTL.getCellEditor().stopCellEditing();
						
						tblGRDTL.setRowSelectionInterval(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedRow());		
						tblGRDTL.setColumnSelectionInterval(TB1_PRDCD,TB1_PRDCD);		
						tblGRDTL.editCellAt(tblGRDTL.getSelectedRow(),TB1_PRDCD);
				        tblGRDTL.cmpEDITR[TB1_PRDCD].requestFocus();
				    }
				    
					else
					{
					    
				    setMSG("Please Enter Value Sequence No",'E');
				    txtSEQNO.requestFocus();
					}
				  }	
				  
			}
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				//System.out.println("F1 pressed");
				if(M_objSOURC==txtISSTP)
				{
					M_strHLPFLD="txtISSTP";
					String L_staHEADR[] ={"Issue Type.","DESC"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and";
					M_strSQLQRY += " CMT_CGSTP = 'FGXXITP' and CMT_CHP02 <> '2'";
					if(txtISSTP.getText().trim().length()>0)
					{
						M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtISSTP.getText().trim()+"%'"; 
					}
					cl_hlp(M_strSQLQRY,2,1,L_staHEADR,2,"CT");
				}
				else if(M_objSOURC==txtMKTTP)
				{
					M_strHLPFLD="txtMKTTP";
					String L_staHEADR[] ={"Market Type.","DESC"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and ";
					M_strSQLQRY += " CMT_CGSTP = 'COXXMKT' ";
					if(txtMKTTP.getText().trim().length()>0)
					{
						M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtMKTTP.getText().trim()+"%'"; 
					}
					cl_hlp(M_strSQLQRY,2,1,L_staHEADR,2,"CT");
				}
				else if(M_objSOURC==txtREJCD_VR)
				{
					M_strHLPFLD="txtREJCD_VR";
					String L_staHEADR[] ={"Rej.Code.","Desc"};
					M_strSQLQRY = "select CMT_CODCD, CMT_CODDS  from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXREJ' order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,2,1,L_staHEADR,2,"CT");

					cl_dat.M_wndHLP_pbst.setVisible(false);
					cl_dat.M_btnHLPOK_pbst.setVisible(false);
					//if(JOptionPane.showConfirmDialog(this,cl_dat.M_pnlHELP_pbst,"Help",JOptionPane.YES_NO_OPTION)==0)
					//	exeHLPOK();
					JOptionPane.showMessageDialog(this,cl_dat.M_pnlHELP_pbst,"Help",JOptionPane.PLAIN_MESSAGE);
					exeHLPOK();
						

				}
				else if(M_objSOURC==txtISSNO)
				{
					M_strHLPFLD="txtISSNO";
					String L_staHEADR[] ={"Issue No.","Lorry No.","Issue Date","Elsp.Time"};
					strISSTP=txtISSTP.getText().trim();
					String L_addSTRING = "";
					if(LM_INDISS)
					{
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
							L_addSTRING = "  and ist_issdt <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strREFDT))+"' and IST_STSFL not in ('2','X','3')";
					    M_strSQLQRY = "Select distinct IST_ISSNO,IVT_LRYNO,IST_ISSDT,(round(datediff(DAY,GETDATE(),ivt_laddt)/100,0)) ivt_elstm from FG_ISTRN   left outer join MR_IVTRN on IST_MKTTP=IVT_MKTTP and IST_ISSNO = IVT_LADNO where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSTP='"+strISSTP+"'"; 
						M_strSQLQRY += L_addSTRING +" order by IVT_ELSTM desc, IVT_LRYNO asc, IST_ISSNO desc";
					}
					else
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							L_addSTRING = " and IVT_STSFL='A' and IVT_REQQT > IVT_LADQT and isnull(ivt_ladqt,0)=0 and ivt_loddt is null and CONVERT(varchar,ivt_laddt,103) <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strREFDT))+"' and ivt_CMPCD + ivt_ladno + ivt_prdcd not in (select ist_CMPCD + ist_issno + ist_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_isstp='10' and isnull(ist_issqt,0)>0) ";
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							L_addSTRING = " and IVT_STSFL not in ('X','D','L') and IVT_REQQT >= IVT_LADQT and CONVERT(varchar,ivt_laddt,103) <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strREFDT))+"'   and ivt_CMPCD + ivt_ladno + ivt_prdcd in (select ist_CMPCD + ist_issno + ist_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_isstp='10' and isnull(ist_issqt,0)>0) ";
						M_strSQLQRY = "Select distinct IVT_LADNO,IVT_LRYNO,CONVERT(varchar,IVT_LADDT,103) IVT_LADDT,(round(datediff(DAY,GETDATE(),ivt_laddt)/100,0)) ivt_elstm from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SLRFL='"+strSTKTP+"' ";
						M_strSQLQRY += L_addSTRING + " order by ivt_elstm desc, ivt_lryno asc, ivt_ladno desc";
					}
					
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_staHEADR,4,"CT");
				}
				else if(M_objSOURC==txtLOTNO)
				{
					M_strHLPFLD="txtLOTNO";
					String L_staHEADR[]=new String[]{"Lot No.","Rcl. No","Lot Remark","Stk. Remark","Stk.Qty","Res.Qty","Main Loc.","Pkg. Type","Prod.Date"};
					String L_strTDSFL  = tblLTDTL.getValueAt(tblLTDTL.getSelectedRow(),TB2_TDSFL).toString().trim();
					
					if(LM_INDISS)
					{
						L_staHEADR=new String[] {"Lot No.","Rcl. No","Lot Remark","Stk. Remark","Stk.Qty","Res.Qty.","Main Loc.","Pkg. Type","Prod.Date"};		
						if(L_strTDSFL.equals("C"))
						{  //Checking for Classified
							M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO,LT_REMDS,ST_REMDS,(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)),0 SR_RESQT,ST_MNLCD,ST_PKGTP,CONVERT(varchar,LT_PSTDT,103) LT_PSTDT from FG_STMST,PR_LTMST";
							 M_strSQLQRY += " where ST_CMPCD = LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO  and ST_STKQT > ST_ALOQT and FG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(LT_RESFL,'X')) not in ('Q','H') order by LT_PSTDT asc,ST_LOTNO asc,ST_RCLNO desc";
						}
						else if(L_strTDSFL.equals("U"))
						{  //Checking for UnClassified
							M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO,LT_REMDS,ST_REMDS,ST_UCLQT,0 SR_RESQT,ST_MNLCD,ST_PKGTP,CONVERT(varchar,LT_PSTDT,103) LT_PSTDT from FG_STMST,PR_LTMST";
						   M_strSQLQRY += " where  ST_CMPCD= LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and ST_UCLQT > 0 and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(LT_RESFL,'X')) not in ('Q','H')  order by LT_PSTDT asc,ST_LOTNO asc,ST_RCLNO desc";
						}
					}
					else
					{
						strPRDTP = getPRDTP();
							if(L_strTDSFL.equals("T"))
							{
								L_staHEADR=new String[] {"Lot No.","Rcl. No","Lot Remark","Stk. Remark","Stk.Qty","Res.Qty","Main Loc.","Pkg. Type","Prod.Date"};		
								M_strSQLQRY = " Select distinct ST_LOTNO,ST_RCLNO,LT_REMDS + char(LT_RETQT),ST_REMDS,";
								M_strSQLQRY +=" (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0))LM_AVLQT, sum(isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)) SR_RESQT, ST_MNLCD,ST_PKGTP,CONVERT(varchar,LT_PSTDT,103) LT_PSTDT ";
								M_strSQLQRY += " from PR_LTMST,CO_CDTRN,FG_STMST left outer join FG_SRTRN on ST_CMPCD=SR_CMPCD and ST_PRDTP=SR_PRDTP and ST_LOTNO = SR_LOTNO and ST_RCLNO = SR_RCLNO  where ";
								M_strSQLQRY += " ST_PRDTP = LT_PRDTP and ST_LOTNO = LT_LOTNO and ST_RCLNO = LT_RCLNO and ST_CMPCD=LT_CMPCD and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(LT_RESFL,'X')) not in ('Q','H')";
								if(flgAUTFL && chkAUTFL.isSelected())
									M_strSQLQRY += " and LT_AUTLS like '%"+strGRPCD+"%'";
								if(flgRESFL && chkRESFL.isSelected())
									M_strSQLQRY += " and ST_CMPCD + ST_PRDTP + ST_LOTNO + ST_RCLNO in (select SR_CMPCD + SR_PRDTP + SR_LOTNO + SR_RCLNO from FG_SRTRN where (SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)-isnull(SR_RALQT,0))>0 and SR_GRPCD = '"+strGRPCD+"' and SR_ENDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
								M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_CPRCD='"+strPRDCD+"' and ST_PRDTP='"+strPRDTP+"' and ST_STSFL='"+strSTKTP+"' and ";
								M_strSQLQRY += " ST_PKGTP='"+strPKGTP+"'  and len(rtrim(ltrim(st_remds)))>2  and ST_STKQT > ST_ALOQT ";
								M_strSQLQRY += " group by ST_LOTNO,ST_RCLNO,LT_REMDS + char(LT_RETQT),ST_REMDS,(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)), ST_MNLCD,ST_PKGTP,LT_PSTDT order by LT_PSTDT asc,ST_LOTNO asc,ST_RCLNO desc,LM_AVLQT desc";
							}
							if(L_strTDSFL.equals("N"))
							{
								L_staHEADR=new String[] {"Lot No.","Rcl. No","Lot Remark","Stk. Remark","Stk.Qty","Res.Qty","Main Loc.","Pkg. Type","Prod.Date"};
								M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO,LT_REMDS + char(LT_RETQT),ST_REMDS,(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0))LM_AVLQT, sum(isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)) SR_RESQT,ST_MNLCD,ST_PKGTP,CONVERT(varchar,LT_PSTDT,103) LT_PSTDT from PR_LTMST,FG_STMST";
								M_strSQLQRY += " left outer join FG_SRTRN on ST_CMPCD=SR_CMPCD and ST_PRDTP=SR_PRDTP and ST_LOTNO = SR_LOTNO and ST_RCLNO = SR_RCLNO where ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(isnull(LT_RESFL,'X')) not in ('Q','H') and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STSFL='"+strSTKTP+"' and ST_PKGTP='"+strPKGTP+"'";
								if(flgAUTFL && chkAUTFL.isSelected())
									M_strSQLQRY += " and LT_AUTLS like '%"+strGRPCD+"%'";
								if(flgRESFL && chkRESFL.isSelected())
									M_strSQLQRY += " and ST_CMPCD + ST_PRDTP + ST_LOTNO + ST_RCLNO in (select SR_CMPCD + SR_PRDTP + SR_LOTNO + SR_RCLNO from FG_SRTRN where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)-isnull(SR_RALQT,0))>0 and SR_GRPCD = '"+strGRPCD+"' and SR_ENDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
								M_strSQLQRY += " and ST_CPRCD='"+strPRDCD+"' and ST_STKQT > ST_ALOQT group by ST_LOTNO,ST_RCLNO,LT_REMDS + char(LT_RETQT),ST_REMDS,(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)), ST_MNLCD,ST_PKGTP,LT_PSTDT order by LT_PSTDT asc,ST_LOTNO asc,ST_RCLNO desc,LM_AVLQT desc";
							}
						//System.out.println("hlpLOTDTL : "+L_strTDSFL+" : "+M_strSQLQRY);
					}
					System.out.println(M_strSQLQRY);
					M_flgBIGHLP = true;
					cl_hlp(M_strSQLQRY,1,1,L_staHEADR,9,"CT");
				}
				else if(pnlVHDTN!=null)
				{
					//System.out.println("F1 in tblVHDTN");
					if(tblVHDTN.getSelectedColumn() == TB6_DETCD)
					{
						//System.out.println("F1 in Detention Code");
						M_strHLPFLD="txtEDITR_VHD";
						String L_staHEADR[] ={"Det Code.","Desc"};
						M_strSQLQRY = "select CMT_CODCD, CMT_CODDS  from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXVHD' and cmt_codcd < '70' order by CMT_CODCD";
						cl_hlp(M_strSQLQRY,2,1,L_staHEADR,2,"CT");
						
						cl_dat.M_wndHLP_pbst.setVisible(false);
						cl_dat.M_btnHLPOK_pbst.setVisible(false);
						JOptionPane.showMessageDialog(this,cl_dat.M_pnlHELP_pbst,"Help",JOptionPane.PLAIN_MESSAGE);
							exeHLPOK();
					}
				}
			}
		}
		catch(Exception L_EK)
		{
			setMSG(L_EK,"KeyPressed Evelnt");
		}
	}
		
	
	/**
	 * Method to execute F1 help for the selected field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD == "txtISSTP")
			{
				txtISSTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			
			if(M_strHLPFLD == "txtMKTTP")
			{
				txtMKTTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtISSNO")
			{
				txtISSNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtREJCD_VR")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtREJCD_VR.setText(L_STRTKN.nextToken());
				lblREJDS_VR.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD == "txtEDITR_VHD")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEDITR_VHD.setText(L_STRTKN.nextToken());
				tblVHDTN.setValueAt(L_STRTKN.nextToken(),tblVHDTN.getSelectedRow(),TB6_DETDS);
			}
			if(M_strHLPFLD.equals("txtLOTNO"))
			{
				String  L_strLOTQT="0";
				String L_staHEADR[] ={"Lot No.","Rcl. No","Lot Remark","Stk. Remark","Stk.Qty","Res.Qty","Main Loc.","Pkg. Type","Prod.Date"};		
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtLOTNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());	
				tblLTDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblLTDTL.getSelectedRow(),TB2_RCLNO);
				if(!flgRESFL)
					tblLTDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),tblLTDTL.getSelectedRow(),TB2_REMRK);
				L_strLOTQT=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim();
				if(!LM_INDISS)
				{
					L_strLOTQT = calLDQTY(L_strLOTQT);
					L_strLOTQT=setNumberFormat(Double.parseDouble(L_strLOTQT),3);
				}
				
				tblLTDTL.setValueAt(String.valueOf(L_strLOTQT).trim(),tblLTDTL.getSelectedRow(),TB2_ISSQT);
				tblLTDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),6)).trim(),tblLTDTL.getSelectedRow(),TB2_MNLCD);
				tblLTDTL.setValueAt((String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),7)).trim().equals("01") ? "01" : "02"),tblLTDTL.getSelectedRow(),TB2_PKGCT);
				tblLTDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),7)).trim(),tblLTDTL.getSelectedRow(),TB2_PKGTP);
			}
		}
		catch(Exception L_EK)
		{
			setMSG(L_EK,"exeHLPOK");
		}
	}
	
	
	private void chkSEQNO()
	{
	    int i ;
	   try
	   {
			M_strSQLQRY = "select  count (*)  ivt_ldgct from mr_ivtrn where ivt_ginno in ( select ivt_ginno from mr_ivtrn b where";
			M_strSQLQRY += " b.IVT_MKTTP='"+txtMKTTP.getText().trim()+"' and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ladno= '"+txtISSNO.getText() .trim() +"')"; 
			M_strSQLQRY += " and  ivt_unlsq>1";
	    
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
	    
			while(M_rstRSSET.next())
			{
			      strLDGCT=M_rstRSSET.getString("ivt_ldgct") ;
			      //System.out.println(strLDGCT);
			}
	    
	    
			if( !(strLDGCT.equals("1" )) && !((strLDGCT.equals("0" ))))
			{
			    //System.out.println(strLDGCT);
			    txtSEQNO.setVisible(true);
			    lblSEQ.setVisible(true);
			    txtSEQNO.setEnabled(true);
			    
			    
			}
			else
			{
			    txtSEQNO.setVisible(false);
			    lblSEQ.setVisible(false);
			    if(tblGRDTL.isEditing())
					tblGRDTL.getCellEditor().stopCellEditing();
				
				tblGRDTL.setRowSelectionInterval(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedRow());		
				tblGRDTL.setColumnSelectionInterval(TB1_PRDCD,TB1_PRDCD);		
				tblGRDTL.editCellAt(tblGRDTL.getSelectedRow(),TB1_PRDCD);
			    tblGRDTL.cmpEDITR[TB1_PRDCD].requestFocus();
			    //txtSEQNO.setText(String.valueOf(i));
			}   
	    
	    
	   }catch(Exception L_EX)
	   {
	       setMSG(L_EX,"This is chkseqno");
	   }
	}
	
	
	private String vldSEQNO()
	{
	    
	    try
	    {
	    M_strSQLQRY = "select ivt_unlsq from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp= '"+txtMKTTP.getText() .toString() .trim()+"' and  ivt_ladno = '"+txtISSNO.getText().toString() .trim()+"'";
	    
	    
	    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
	    //System.out.println(M_strSQLQRY);
	    if(M_rstRSSET.next())
	    {
			strSEQ =M_rstRSSET.getString("ivt_unlsq");
			//System.out.println(strSEQ);
	        
			
		}
	    
		if(M_rstRSSET != null)
			M_rstRSSET.close();
		
	    }catch(Exception L_EX)
	    {
	        setMSG( L_EX,"This is the vldSEQNO");
	        
	    }
	    return strSEQ;
	    
	}
	
	
	
	/**
	 * @return void
	 * Calculates Loaded Qty within the JTable taking into consideration that the total qty.
	 * of the Lot JTable must not exceed Requseted Qty. of Grade JTable.
	*/
	private String calLDQTY(String LP_LOTQT)
	{ 
		try
		{
			//System.out.println("IIII");
			double  L_dblDGDQT  = strGRDQT.length()==0 ? 0.000 : Double.parseDouble(strGRDQT);
			double L_dblDLTQT   = LP_LOTQT.length()==0 ? 0.000 : Double.parseDouble(LP_LOTQT);
			if(tblLTDTL.getSelectedRow() == 0)
			{
			 	if(L_dblDGDQT >= L_dblDLTQT)
				{
					 return LP_LOTQT;
			    }
				else if(L_dblDLTQT >= L_dblDGDQT)
				{
				   	//return setFMT(strGRDQT,3);
					return strGRDQT;
				}
			}
			else
			{
				double L_dblDDSQT  = 0;
				double L_dblDLQTY  = 0;
				String strALOQT="";
				double LM_DSALQ=0.0;
				for(int i = 0;i < tblLTDTL.getRowCount();i++)
				{
		   			strALOQT = nvlSTRVL(tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim(),"0.000");
		   			LM_DSALQ = strALOQT.length()==0 ? 0.000 : Double.parseDouble(strALOQT);
		   			LM_DSALQ = LM_DSALQ + L_dblDDSQT;
		   			L_dblDDSQT = LM_DSALQ;
		   		}
		   		L_dblDLQTY = L_dblDGDQT - L_dblDDSQT;
		   		strALOQT = String.valueOf(L_dblDLQTY);
		   		if(L_dblDLQTY < 0)
				{
		   			setMSG("Selected Quantity Exceeds Requested Qty.",'E'); 
		   			return "0.000";
		   		}
		   		else if(L_dblDLQTY <= L_dblDLTQT)
		   			return strALOQT.trim();
		   		else if(L_dblDLQTY >= L_dblDLTQT)
		   			return LP_LOTQT.trim();
		   	}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calLDQTY");
		}
		return "";
	}

	
	
	/**
	 * @return void
	 * 1)Validates the Issue Type with the Database table i.e CO_CDTRN
	 * 2)enables and disables the component as per the requirement.
	 * 3)gets the user authority i.e only certain authorized persons are allowed
	 * to authorize the Issues.
	 */
	private void vldISSTP()
	{
		if(exeISSTP())
		{
			setMSG("Valid Issue Type",'N');
			//getUSRAUT(); //gets the User Authority.
			//if(cl_dat.ocl_dat.M_FLGOPT == 'A')
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(LM_INDISS)
				{
					txtISSNO.setEnabled(false);	
					txtMKTTP.setEnabled(false);
					txtCNTDS.setEnabled(false);
					txtDESTN.setEnabled(false);
					//txtRESNO.setEnabled(false);
					tblGRDTL.setEnabled(false);
					txtISRMK.setEnabled(true);
					tblLTDTL.setEnabled(true);
					rdbISALO.setSelected(false);
					rdbISAUT.setSelected(true);
					rdbISALO.setEnabled(false);
					txtISRMK.requestFocus();
				}
				else
				{
					txtISSNO.setEnabled(true);	
					txtMKTTP.setEnabled(false);
					txtCNTDS.setEnabled(true);
					txtDESTN.setEnabled(true);
					txtTSLNO.setEnabled(true);
					txtRSLNO.setEnabled(true);
					//txtRESNO.setEnabled(true);
					rdbISALO.setSelected(true);
					rdbISALO.setEnabled(true);
					//txtMKTTP.requestFocus();
					txtISSNO.requestFocus();
				}
			}
			//if(cl_dat.ocl_dat.M_FLGOPT == 'M')
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(LM_INDISS)
				{
					txtISSNO.setEnabled(true);	
					txtMKTTP.setEnabled(false);
					txtCNTDS.setEnabled(false);
					txtDESTN.setEnabled(false);
					tblGRDTL.setEnabled(false);
					txtISRMK.setEnabled(true);
					//txtRESNO.setEnabled(true);
					tblLTDTL.setEnabled(true);
					rdbISALO.setSelected(false);
					rdbISALO.setEnabled(false);
					txtISSNO.requestFocus();
				}
				else
				{
					txtISSNO.setEnabled(true);	
					//txtMKTTP.setEnabled(true);
					txtMKTTP.setEnabled(false);
					txtCNTDS.setEnabled(true);
					txtDESTN.setEnabled(true);
					rdbISALO.setSelected(true);
					rdbISALO.setEnabled(true);
					//txtMKTTP.requestFocus();
					txtISSNO.requestFocus();
				}
			}
			//if(cl_dat.ocl_dat.M_FLGOPT == 'E')
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				if(LM_INDISS)
				{
					txtISSNO.setEnabled(true);	
					txtMKTTP.setEnabled(false);	
					txtISSNO.requestFocus();
				}
				else
				{
					//txtMKTTP.setEnabled(true);
					txtMKTTP.setEnabled(false);
					txtISSNO.setEnabled(true);	
					//txtMKTTP.requestFocus();
					txtISSNO.requestFocus();
				}
			}
		}
		else
		{
			setMSG("InValid Issue Type",'E');
			txtISSTP.requestFocus();
		}
	}

	
	
	
	/**
	 * @return boolean
	 *  Validates the Issue Type with the database Table CO_CDTRN &
	 * fetches the Stock Type into strSTKTP. 
	 */
	private boolean exeISSTP()     //validates Issue Type with the Database
	{
		try
		{
			strCCSVL = getCODVL("SYSFGXXITP"+strISSTP,cl_dat.M_intCCSVL_pbst);
			strSTKTP = getCODVL("SYSFGXXITP"+strISSTP,cl_dat.M_intCHP01_pbst);
			LM_INDISS = strCCSVL.equals("1") ? true : false;
			if(cl_dat.M_hstMKTCD_pbst.containsKey("SYSFGXXITP"+strISSTP))
				return true;
			//M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01 from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'FGXXITP' and CMT_CODCD='"+strISSTP+"'";
			//M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//while(M_rstRSSET.next())
			//{
			//	strCCSVL = M_rstRSSET.getString("CMT_CCSVL").toString().trim();
			//	if((strCCSVL.equals("1")))
			//		LM_INDISS = true;
			//	strSTKTP = M_rstRSSET.getString("CMT_CHP01").toString().trim();
			//	return true;
			//}
			//if(M_rstRSSET !=null)
			//	M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeISSTP");							   
		}
		return false;
	}
	
	/**
	 * @return void
	 * Validates the Issue Number & makes the JTable enable to fetch the data.
	 * getALLREC() fetches the data into the Grade JTable for Direct Issues
	 */
	private void vldISSNO()    //validates Issue number
	{
		try
		{
			if(exeISSNO())
			{
				setMSG("Valid Issue No.",'N');
				String strRESFL = "";
				if(LM_INDISS)
				{
					//txtISRMK.setText("");
					txtDORNO.setText("");
					txtDORDT.setText("");
					txtSALTP.setText("");
					lblTRPNM.setText("");
					txtLRYNO.setText("");
					txtCNTDS.setText("");
					txtDESTN.setText("");
					txtTSLNO.setText("");
					txtRSLNO.setText("");
					lblBYRNM.setText("");
					lblBAD01.setText("");
					lblBAD02.setText("");
					lblCNSNM.setText("");
					lblCAD01.setText("");
					lblCAD02.setText("");
					lblISSTS.setText("");
					lblISQTY.setText("");
					lblISSPK.setText("");
					strSALTP = " ";
					//getTBLENB();
					//clrLOTTBL();
					//clrGRDTBL();
					//getLOTREC();
					//getRMMST();
				}
				else
				{
					//txtISRMK.setText("");
					txtDORNO.setText("");
					txtDORDT.setText("");
					txtSALTP.setText("");
					lblTRPNM.setText("");
					txtLRYNO.setText("");
					txtCNTDS.setText("");
					txtDESTN.setText("");
					txtTSLNO.setText("");
					txtRSLNO.setText("");
					lblBYRNM.setText("");
					lblBAD01.setText("");
					lblBAD02.setText("");
					lblCNSNM.setText("");
					lblCAD01.setText("");
					lblCAD02.setText("");
					lblISSTS.setText("");
					lblISQTY.setText("");
					lblISSPK.setText("");
					//txtRESNO.setText("");
					//System.out.println("0004");
					//getTBLENB();
					//clrLOTTBL();
					//clrGRDTBL();
					//System.out.println("0005");
					getALLREC();
					//tblGRDTL.requestFocus();
					//tblGRDTL.setRowSelectionInterval(TB1_CHKFL,TB1_CHKFL);
					//tblGRDTL.setColumnSelectionInterval(TB1_PRDCD,TB1_CHKFL);
					setMSG("Press Enter or Click the Product Code....",'N');
					//btnPRINT.setEnabled(false);
					btnPRINT_LA.setEnabled(true);
					chkVHREJ_E.setEnabled(true);
					chkVHREJ_V.setEnabled(true);
				}
				//if(cl_dat.ocl_dat.M_FLGOPT == 'E')
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					tblLTDTL.setEnabled(false);
					//txtISRMK.setEnabled(false);
					txtCNTDS.setEnabled(false);
					txtDESTN.setEnabled(false);
					//tblGRDTL.requestFocus();
					//tblGRDTL.setRowSelectionInterval(TB1_CHKFL,TB1_CHKFL);
					//tblGRDTL.setColumnSelectionInterval(TB1_PRDCD,TB1_CHKFL);
					setMSG("Press Enter or Click the Product Code....",'N');
					//btnPRINT.setEnabled(true);
					btnPRINT_LA.setEnabled(true);
				}
				//if(cl_dat.ocl_dat.M_FLGOPT == 'M' || cl_dat.ocl_dat.M_FLGOPT == 'E')
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
				{
					String L_strISSDT ="";
					strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
					M_strSQLQRY = "SELECT distinct IST_ISSDT FROM FG_ISTRN";
					M_strSQLQRY += " WHERE IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strISSTP+"'";
					M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"' and IST_MKTTP='"+strMKTTP+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						L_strISSDT = getRSTVAL(M_rstRSSET,"IST_ISSDT","D");
					}
					if(M_rstRSSET !=null)
						M_rstRSSET.close();
					txtISSDT.setText(L_strISSDT);

	
				}
				else
					txtISSDT.setText(cl_dat.M_strLOGDT_pbst);
				rdbISALO.setSelected(true);
				rdbISAUT.setSelected(false);
			}
			else
			{
				setMSG("InValid Issue No.",'E');
				//clrLOTTBL();
				//clrGRDTBL();
				txtISRMK.setText("");
				txtDORNO.setText("");
				txtDORDT.setText("");
				txtSALTP.setText("");
				lblTRPNM.setText("");
				txtLRYNO.setText("");
				txtCNTDS.setText("");
				txtDESTN.setText("");
				txtTSLNO.setText("");
				txtRSLNO.setText("");
				lblBYRNM.setText("");
				lblBAD01.setText("");
				lblBAD02.setText("");
				lblCNSNM.setText("");
				lblCAD01.setText("");
				lblCAD02.setText("");
				lblISSTS.setText("");
				lblISQTY.setText("");
				lblISSPK.setText("");
				txtISSNO.requestFocus();
			}
		}
		catch (Exception L_EX)
		{
			setMSG("vldISSNO "+L_EX,'E');
		}
	}
	
	
	/**
	 */
	private boolean exeISSNO()     //validates Issue Number with the database
	{
		try
		{
			String L_ISSNO = "";
			//System.out.println("0003");
			strISSNO=txtISSNO.getText().trim();
			strMKTTP=txtMKTTP.getText().trim();
			strISSTP=txtISSTP.getText().trim();
			if(LM_INDISS)
			{
				//if(cl_dat.ocl_dat.M_FLGOPT == 'E')
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					M_strSQLQRY = "Select IST_ISSNO from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSTP='"+strISSTP+"'";
					M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"'";
				}
				else
				{
					M_strSQLQRY = "Select IST_ISSNO from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSTP='"+strISSTP+"'";
					M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"' and IST_STSFL not in '2'";
				}
			}
			else
			{
				//if(cl_dat.ocl_dat.M_FLGOPT == 'A')
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
				    chkSEQNO();
					M_strSQLQRY = "Select IVT_LADNO from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP='"+strMKTTP+"'";
					M_strSQLQRY += " and IVT_STSFL='A' and IVT_REQQT > IVT_LADQT";
					M_strSQLQRY += " and IVT_SLRFL='"+strSTKTP+"' and IVT_LADNO='"+strISSNO+"'";
				}
				//else if(cl_dat.ocl_dat.M_FLGOPT == 'M')
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
				    chkSEQNO();
					M_strSQLQRY = "Select IST_ISSNO from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSTP='"+strISSTP+"' and";
					M_strSQLQRY += " IST_MKTTP='"+strMKTTP+"' and IST_ISSNO='"+strISSNO+"' and";
					M_strSQLQRY += " IST_STSFL not in ('X')";
				}
				//else if(cl_dat.ocl_dat.M_FLGOPT == 'E')
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					//M_strSQLQRY = "Select IST_ISSNO from FG_ISTRN where IST_ISSTP='"+strISSTP+"'";
					//M_strSQLQRY += " and IST_MKTTP='"+strMKTTP+"' and IST_ISSNO='"+strISSNO+"'";
					M_strSQLQRY = "Select IVT_LADNO IST_ISSNO from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP='"+strMKTTP+"'";
					M_strSQLQRY += " and IVT_LADNO='"+strISSNO+"'";
				}
			}
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_ISSNO = M_rstRSSET.getString(1);
			}
			//System.out.println("NNNNNNNNNN"+strISSNO);
			//System.out.println("NNNNNNNNNN22222 L_ISSNO = "+L_ISSNO);
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			if(strISSNO.equals(L_ISSNO))
				return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeISSNO");							   
		}
		return false;
	}
	
	private void getALLREC()
	{ 
		try
		{
			//this.setCursor(curWTSTS);
			setCursor(cl_dat.M_curWTSTS_pbst);
			getIVTRN();  
			getGRDREC(); 
			setCursor(cl_dat.M_curDFSTS_pbst);
			//this.setCursor(curDFSTS);
		}
		catch (Exception L_EX)
		{
			setMSG("getALLREC "+L_EX,'E');
		}
	}
	
	private void getIVTRN()
	{ 
		try
		{
			//dspTIME("getIVTRN Start");
			M_strSQLQRY = "Select IVT_DORNO,IVT_DORDT,IVT_LRYNO,IVT_CNTDS,IVT_DSTDS,IVT_SALTP,IVT_BYRCD,IVT_CNSCD,isnull(IVT_GRPCD,IVT_BYRCD) IVT_GRPCD, ";
			M_strSQLQRY += "IVT_TRPCD,IVT_GINNO, IVT_LADNO,IVT_DSRCD,IVT_TSLNO,IVT_RSLNO,PT_PRTNM,isnull(PT_ADR01,'') + ',  ' + isnull(PT_ADR02,'') ADR01,isnull(PT_ADR03,'') + ',  ' + isnull(PT_ADR04,'') ADR02,PT_ADR03,";
			M_strSQLQRY += "cmt_codds,ivt_laddt,getdate() ivt_curtm from MR_IVTRN,CO_PTMST,CO_CDTRN";
			M_strSQLQRY += " where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP='"+strMKTTP+"' and IVT_LADNO='"+strISSNO+"'";
			M_strSQLQRY += " and PT_PRTCD = IVT_BYRCD and PT_PRTTP = 'C' and cmt_cgmtp='SYS'";
            M_strSQLQRY += " and cmt_cgstp='MR00SAL' and cmt_codcd = IVT_SALTP";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				strLRYNO = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"").trim();
				strCNTDS = nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),"").trim();
				strGINNO = nvlSTRVL(M_rstRSSET.getString("IVT_GINNO"),"").trim();
				strLADNO = nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),"").trim();
				txtDORNO.setText(M_rstRSSET.getString("IVT_DORNO"));
				txtDORDT.setText(getRSTVAL(M_rstRSSET,"IVT_DORDT","D"));
				txtLRYNO.setText(strLRYNO);
				txtCNTDS.setText(M_rstRSSET.getString("IVT_CNTDS"));
				txtDESTN.setText(M_rstRSSET.getString("IVT_DSTDS"));
				txtTSLNO.setText(M_rstRSSET.getString("IVT_TSLNO"));
				txtRSLNO.setText(M_rstRSSET.getString("IVT_RSLNO"));
				strSALTP = nvlSTRVL(M_rstRSSET.getString("IVT_SALTP"),"").trim();
				txtSALTP.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"").trim());	
				strTRPCD = nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),"").trim();
				strTRPNM = nvlSTRVL(cl_dat.getPRMPRT("PT_PRTNM","T",strTRPCD),"").trim();
				lblTRPNM.setText(strTRPNM);
				strBYRCD=nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"").trim();
				strBYRCD=nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"").trim();
				strGRPCD=nvlSTRVL(M_rstRSSET.getString("IVT_GRPCD"),"").trim();
				lblBYRNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"").trim());
				lblBAD01.setText(nvlSTRVL(M_rstRSSET.getString("ADR01"),"").trim());	
				lblBAD02.setText(nvlSTRVL(M_rstRSSET.getString("ADR02"),"").trim());
				if(txtDESTN.getText().length() == 0)
					txtDESTN.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),"").trim());
				LM_DISTR = nvlSTRVL(M_rstRSSET.getString("IVT_DSRCD"),"").trim();
				setCNSDTL(M_rstRSSET.getString("IVT_BYRCD"),M_rstRSSET.getString("IVT_CNSCD"));


				String L_TIMEGAP = calTIME(getRSTVAL(M_rstRSSET,"IVT_CURTM","T"),getRSTVAL(M_rstRSSET,"IVT_LADDT","T"));
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					if(chkTRGTM(L_TIMEGAP))
						{chkVHDTN.setSelected(true);getVHDTN(strLADNO);}

				//String L_TIMEGAP = calTIME(getRSTVAL(M_rstRSSET,"IVT_CURTM","T"),getRSTVAL(M_rstRSSET,"IVT_LADDT","T"));
				//lblELSTM.setText("Elapsed Time : "+L_TIMEGAP+" Hrs.");
				//if(cl_dat.ocl_dat.M_FLGOPT == 'A' || cl_dat.ocl_dat.M_FLGOPT == 'M')
				//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					//if(chkTRGTM(L_TIMEGAP))
					//	{chkVHDTN.setSelected(true);getVHDTN(strLADNO);}
				//dspTIME("getIVTRN Complete");
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			getREMDS();
				strWHERE =  "VR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VR_DOCTP = '"+strDOCTP+"' and "
					 +" ((VR_LRYNO = '" +strLRYNO+"' and len(isnull(VR_LRYNO,''))>1)  or  (vr_cntds = '"+strCNTDS+"' and len(isnull(VR_CNTDS,''))>1))";
				flgCHK_EXIST = false; 
			flgCHK_EXIST =  chkEXIST("FG_VRTRN",strWHERE);
			//System.out.println("dspVEHREJ flgCHK_EXIST = "+flgCHK_EXIST);
			if (flgCHK_EXIST)
			{
				dspVEHREJ();
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLAMST");
		}
	}
	
	/** Displaying Consignee Detail
	 */
	private void setCNSDTL(String LP_BYRCD,String LP_CNSCD)
	{
		try
		{
			if(LP_BYRCD.equals(LP_CNSCD))
				{lblCNSNM.setText("(Same as Buyer)"); return;}
			M_strSQLQRY = "select PT_PRTNM,isnull(PT_ADR01,'') + ',  ' + isnull(PT_ADR02,'') ADR01,isnull(PT_ADR03,'') + ',  ' + isnull(PT_ADR04,'') ADR02 from CO_PTMST where PT_PRTTP='C' and PT_PRTCD = '"+LP_CNSCD+"'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET == null || !L_rstRSSET.next())
				return;
			lblCNSNM.setText(nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"").trim());
			lblCAD01.setText(nvlSTRVL(L_rstRSSET.getString("ADR01"),"").trim());	
			lblCAD02.setText(nvlSTRVL(L_rstRSSET.getString("ADR02"),"").trim());
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setCNSDTL");
		}
	}

	
	/**
    Method to Calculate the differance two Date & Time.
	@param P_strFINTM String argument to Final Time.
	@param P_strINITM String argument to Initial Time.
    */
	public String calTIME(String P_strFINTM,String P_strINITM)
	{
		String L_strDIFTM = "";
		try
		{
			int L_intYRS,L_intMTH,L_intDAY,L_intHRS,L_intMIN;
			int L_intYRS1,L_intMTH1,L_intDAY1,L_intHRS1,L_intMIN1;
			int L_intYRS2,L_intMTH2,L_intDAY2,L_intHRS2,L_intMIN2;
			String L_strHOUR,L_strMINT;			
			if(P_strFINTM.equals("") || P_strINITM.equals(""))
				return L_strDIFTM;			
			// Seperating year,month,day,hour & minute from Final time
			L_intYRS1 = Integer.parseInt(P_strFINTM.substring(6,10));
			L_intMTH1 = Integer.parseInt(P_strFINTM.substring(3,5));
			L_intDAY1 = Integer.parseInt(P_strFINTM.substring(0,2));
			L_intHRS1 = Integer.parseInt(P_strFINTM.substring(11,13));
			L_intMIN1 = Integer.parseInt(P_strFINTM.substring(14));			
			// Seperating year,month,day,hour & minute from Initial time
			L_intYRS2 = Integer.parseInt(P_strINITM.substring(6,10));
			L_intMTH2 = Integer.parseInt(P_strINITM.substring(3,5));
			L_intDAY2 = Integer.parseInt(P_strINITM.substring(0,2));
			L_intHRS2 = Integer.parseInt(P_strINITM.substring(11,13));
			L_intMIN2 = Integer.parseInt(P_strINITM.substring(14));			
			L_intMIN = L_intMIN1 - L_intMIN2;
			L_intHRS = L_intHRS1 - L_intHRS2;			
			// Checking for leap year
			if(L_intYRS1%4 == 0)
				arrDAYS[1] = "29";
			else
				arrDAYS[1] = "28";			
			// Final date is of next month
			if(L_intMTH1 > L_intMTH2)
			{
				for(int i = L_intMTH2;i < L_intMTH1;i++)
					L_intDAY1 += Integer.parseInt(arrDAYS[i-1]);
			}			
			L_intDAY = L_intDAY1 - L_intDAY2;			
			if(L_intMIN < 0)
			{
				L_intMIN += 60;
				L_intHRS--;
			}
			if(L_intHRS < 0)
			{
				L_intHRS += 24;
				L_intDAY--;
			}
			if(L_intDAY > 0)
				L_intHRS += L_intDAY*24;			
			L_strHOUR = String.valueOf(L_intHRS);
			L_strMINT = String.valueOf(L_intMIN);
			if(L_strHOUR.length() < 2)
				L_strHOUR = "0" + L_strHOUR;
			if(L_strMINT.length() < 2)
				L_strMINT = "0" + L_strMINT;
			L_strDIFTM = L_strHOUR + ":" + L_strMINT;
		}
		catch(Exception L_EX)
		{
			setMSG("Error in calTIME : "+L_EX,'E');
		}
		return L_strDIFTM;
	}	
	
	
	
	
    /**
     * Retrieving grade detail
     */	
	private void getGRDREC()
	{ 
		try
		{
			if(tblGRDTL.isEditing())
				tblGRDTL.getCellEditor().stopCellEditing();
			tblGRDTL.setRowSelectionInterval(tblGRDTL.getSelectedRow(),tblGRDTL.getSelectedRow());		
			tblGRDTL.setColumnSelectionInterval(0,0);		
			
			tblGRDTL.editCellAt(tblGRDTL.getSelectedRow(),0);
			String L_strREQQT = "";
			String L_strPKGWT = "";
			int L_intREQPK = 0;
			ResultSet L_rstRSSET;
			tblGRDTL.clrTABLE();
			tblLTDTL.clrTABLE();
			
			//if(cl_dat.ocl_dat.M_FLGOPT == 'E')
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				M_strSQLQRY  = "SELECT IVT_PRDCD,IVT_PRDDS,IVT_PKGTP,IVT_REQQT,IVT_GINNO,IVT_LADNO,IVT_TRPCD,IVT_LRYNO,IVT_CNTDS FROM MR_IVTRN";
				M_strSQLQRY += " WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+strMKTTP+"' and IVT_LADNO='"+strISSNO+"'";
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "SELECT IVT_PRDCD,IVT_PRDDS,IVT_PKGTP,IVT_REQQT,IVT_GINNO,IVT_LADNO,IVT_TRPCD,IVT_LRYNO,IVT_CNTDS FROM MR_IVTRN";
				M_strSQLQRY += " WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+strMKTTP+"' and IVT_LADNO='"+strISSNO+"'";	
				M_strSQLQRY += " and IVT_REQQT>IVT_LADQT";
				//M_strSQLQRY += " and IVT_LADNO in (select IST_ISSNO from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSNO = '"+strISSNO+"' and IST_STSFL = '1')";
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY = "SELECT IVT_PRDCD,IVT_PRDDS,IVT_PKGTP,IVT_REQQT,IVT_GINNO,IVT_LADNO,IVT_TRPCD,IVT_LRYNO,IVT_CNTDS FROM MR_IVTRN";
				M_strSQLQRY += " WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+strMKTTP+"' and IVT_LADNO='"+strISSNO+"'";	
				M_strSQLQRY += " and IVT_REQQT > IVT_LADQT and IVT_STSFL='A'";
			}
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i=0;
			while(M_rstRSSET.next())
			{
				 strPRDCD = nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),"").trim();
				 strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"").trim();
				 strPKGTP = nvlSTRVL(M_rstRSSET.getString("IVT_PKGTP"),"").trim();
				 strGINNO = nvlSTRVL(M_rstRSSET.getString("IVT_GINNO"),"").trim();
				 strLADNO = nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),"").trim();
				 strLRYNO = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"").trim();
				 strCNTDS = nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),"").trim();
				 tblGRDTL.setValueAt(strPRDCD,i,TB1_PRDCD);
				 tblGRDTL.setValueAt(strPRDDS,i,TB1_PRDDS);
				 tblGRDTL.setValueAt(strPKGTP,i,TB1_PKGTP);
				 L_strREQQT = M_rstRSSET.getString("IVT_REQQT");
				 M_strSQLQRY = "Select CMT_NCSVL from CO_CDTRN where CMT_CGMTP='SYS' and";
				 M_strSQLQRY += " CMT_CGSTP = 'FGXXPKG' and CMT_CODCD = '"+strPKGTP+"'";
				 L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				 if(L_rstRSSET.next())
				 {
					 L_strPKGWT = L_rstRSSET.getString("CMT_NCSVL").toString().trim();
					 if (strPKGTP.equals("99")) 
					 {
						 L_strPKGWT="0.001";
					 } // if (strPKGTP.equals("99")) {
				 }
				 if(L_rstRSSET != null)
				 	 L_rstRSSET.close();
				 L_intREQPK = Integer.parseInt(setNumberFormat(Double.parseDouble(L_strREQQT)/Double.parseDouble(L_strPKGWT),0));
				 tblGRDTL.setValueAt(L_strREQQT,i,TB1_REQQT);
				 tblGRDTL.setValueAt(String.valueOf(L_intREQPK),i,TB1_REQPK);
				 i++;
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getGRDREC");
		}
	}
	
	/**
	 * 
	*/
	private void getREMDS()
	{
		try
		{
			M_strSQLQRY  = "Select rm_remds from mr_rmmst where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rm_mkttp = '"+strMKTTP+"' and";
			M_strSQLQRY += " rm_trntp='LA' and rm_docno='"+strISSNO+"'";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//System.out.println(" Get REMARK =  "+M_strSQLQRY );
			if(M_rstRSSET.next())
				txtISRMK.setText(nvlSTRVL(M_rstRSSET.getString("rm_remds"),"").trim());
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREMDS");
		}
	}
		
	public String getPRMPRT(String LP_FLDRTN, String LP_STRPTP, String LP_STRPCD)
	{
		String L_strPRTNM = "";
		String L_strSQLQRY = "Select "  + LP_FLDRTN ;
		L_strSQLQRY += " from CO_PTMST where PT_PRTTP = "+ "'" + LP_STRPTP +"'" ;
		L_strSQLQRY += " and PT_PRTCD = " + "'" + LP_STRPCD + "'";
		try
		{
			ResultSet L_rstRSSET =cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET.next()|| L_rstRSSET!=null)
			{
				L_strPRTNM = L_rstRSSET.getString(1);
				L_rstRSSET.close();
			}
		}
		catch(Exception L_SE)
		{
	       //System.out.println("Error in getPRMPRT : "+L_SE.toString()); 
		   setMSG(L_SE,"GetPRMPRT");
		}
		return L_strPRTNM;
	}

	
	
	
	/**
	 * @return void
	 * Gets the record for the Lot JTable from FG_ISTRN Table when the mouse is 
	 * clicked on the Product Code of the Grade JTable for Direct Issues & for 
	 * Indirect Issues when Issue No. is entered.
	*/
	private void getLOTREC()
	{ 
		try
		{
			strADLSTR = "";
			M_rstRSSET = null;
			hstISTRN.clear();
			tblLTDTL.clrTABLE();
			double L_dblTOTQT = 0;
			double L_dblISSQT = 0;
			int L_intTOTPK = 0;
			int L_intISSPK = 0;
			strWRHTP="01";
			//if(cl_dat.ocl_dat.M_FLGOPT == 'E')
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				strSTSFL = getSTSFL();
				strADLSTR =  " and IST_STSFL='"+strSTSFL+"'";
			}
			//else if(cl_dat.ocl_dat.M_FLGOPT == 'A' || cl_dat.ocl_dat.M_FLGOPT == 'M')
			else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				strADLSTR = " and IST_STSFL = '1' ";
			if(LM_INDISS)
			{ //indirect issues.
				M_strSQLQRY = "SELECT IST_TDSFL,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGCT,IST_PKGTP,IST_ISSQT,IST_ISSPK,IST_ISSDT FROM FG_ISTRN";
				M_strSQLQRY += " WHERE IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strISSTP+"'";
				M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"' and IST_MKTTP='"+strMKTTP+"'" + strADLSTR;
			}
			else
			{ //direct issues.
				strPRDCD = tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TB1_PRDCD).toString().trim();
				strPKGTP = tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TB1_PKGTP).toString().trim();
				M_strSQLQRY = "SELECT IST_TDSFL,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGCT,IST_PKGTP,IST_ISSQT,IST_ISSPK FROM FG_ISTRN";
				M_strSQLQRY += " WHERE IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strISSTP+"'";
				M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"' and IST_MKTTP='"+strMKTTP+"' and IST_PRDCD='"+strPRDCD+"' and IST_PKGTP='"+strPKGTP+"'"+strADLSTR;
			}
			//System.out.println("getLOTREC "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i=0;
			LM_DATFL = true; //record found in issue transaction table
			if(M_rstRSSET == null || !M_rstRSSET.next())
			{
				LM_DATFL = false;    //record not found in issue transaction table
				return;
			} 
			while(true)
			{
				tblLTDTL.setValueAt(new Boolean(true),i,TB2_CHKFL);
				if(LM_INDISS)
				{
                     
					strISSDT = getRSTVAL(M_rstRSSET,"IST_ISSDT","D");
					txtISSDT.setText(strISSDT);
				}
				strTDSFL = nvlSTRVL(M_rstRSSET.getString("IST_TDSFL"),"");
				tblLTDTL.setValueAt(strTDSFL.trim(),i,TB2_TDSFL);
				strLOTNO = nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),"");
				tblLTDTL.setValueAt(strLOTNO.trim(),i,TB2_LOTNO);
				strRCLNO = nvlSTRVL(M_rstRSSET.getString("IST_RCLNO"),"");
				tblLTDTL.setValueAt(strRCLNO.trim(),i,TB2_RCLNO);
				strMNLCD = nvlSTRVL(M_rstRSSET.getString("IST_MNLCD"),"");
				tblLTDTL.setValueAt(strMNLCD.trim(),i,TB2_MNLCD);
				strPKGCT = nvlSTRVL(M_rstRSSET.getString("IST_PKGCT"),"");
				tblLTDTL.setValueAt(strPKGCT.trim(),i,TB2_PKGCT);
				strPKGTP = nvlSTRVL(M_rstRSSET.getString("IST_PKGTP"),"");
				tblLTDTL.setValueAt(strPKGTP.trim(),i,TB2_PKGTP);
				strISSQT = nvlSTRVL(M_rstRSSET.getString("IST_ISSQT"),"");
				tblLTDTL.setValueAt(strISSQT.trim(),i,TB2_ISSQT);
				strISSPK = nvlSTRVL(M_rstRSSET.getString("IST_ISSPK"),"");
				tblLTDTL.setValueAt(strISSPK.trim(),i,TB2_ISSPK);
				tblLTDTL.setValueAt("",i,TB2_REMRK);
				//setRSTDTL_LOT(strLOTNO,strRCLNO,i);				
				hstISTRN.put(strLOTNO+strRCLNO+strMNLCD+strPKGTP,strISSQT);
				L_dblISSQT = Double.parseDouble(strISSQT);
				L_intISSPK = Integer.parseInt(strISSPK);
				L_dblTOTQT += L_dblISSQT;
				L_intTOTPK += L_intISSPK;
				i++;
				if(!M_rstRSSET.next())
					break;
			}
			M_rstRSSET.close();
			int LM_CNT = i; //For Lot No. validation
			lblISQTY.setText(setNumberFormat(L_dblTOTQT,3));
			lblISSPK.setText(setNumberFormat(L_intTOTPK,0));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLOTREC");
		}
	}
	
	
/**
	 * @return String
	 * gets the Status Flag from FG_ISTRN Table to determine whether this issue have 
	 * been authorized or allocated & displays the description into Issue Status textfield.
	 */
	private String getSTSFL()
	{ 
		String L_strSTSFL = "";
		try
		{
			if(LM_INDISS)
			{
				M_strSQLQRY = "SELECT distinct IST_STSFL FROM FG_ISTRN";
				M_strSQLQRY += " WHERE IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strISSTP+"'";
				M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"'";
			}
			else
			{
				strPRDCD = tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TB1_PRDCD).toString().trim();
				strPKGTP = tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TB1_PKGTP).toString().trim();
				M_strSQLQRY = "SELECT distinct IST_STSFL FROM FG_ISTRN";
				M_strSQLQRY += " WHERE IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strISSTP+"'";
				M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"' and IST_PRDCD='"+strPRDCD+"' and IST_PKGTP='"+strPKGTP+"'";
			}
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
				L_strSTSFL = M_rstRSSET.getString("IST_STSFL");
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			if(L_strSTSFL.equals("1"))
				lblISSTS.setText("Allocated Issues.");
			else if(L_strSTSFL.equals("2"))
				lblISSTS.setText("Authorized Issues.");
			else if(L_strSTSFL.equals("3"))
				lblISSTS.setText("Receipt adjusted against this Issue.");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTSFL");
			setMSG("All the records are not authorized or allocated",'E');
		}
		return L_strSTSFL;
	}
	
	
	
	
	/**
 * @return void
 * picks up the Product Type for Direct Issue within LM_PRDTP
 */
	private String getPRDTP()
	{
		try
		{
			strPRDCD = "";
			strGRDQT = "";
			for(int i = 0;i<tblGRDTL.getRowCount();i++)
			{
				if(tblGRDTL.getValueAt(i,TB1_CHKFL).equals(new Boolean(true)))
				{
					strPRDCD = tblGRDTL.getValueAt(i,TB1_PRDCD).toString().trim();	
					strGRDQT = tblGRDTL.getValueAt(i,TB1_REQQT).toString().trim();	
					strPKGTP = tblGRDTL.getValueAt(i,TB1_PKGTP).toString().trim();	
					strPKGTP = tblGRDTL.getValueAt(i,TB1_PKGTP).toString().trim();	
				}
			}
			strPRDTP = "";
			strPRDDS = "";
			M_strSQLQRY = "Select PR_PRDTP,PR_PRDDS from CO_PRMST where PR_PRDCD='"+strPRDCD+"'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(L_rstRSSET.next())
			{
				strPRDTP = L_rstRSSET.getString("PR_PRDTP");
				strPRDDS = L_rstRSSET.getString("PR_PRDDS");
			}
			if(L_rstRSSET !=null)
				L_rstRSSET.close();
			return strPRDTP;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDTP");
		}
		return "";
	}

	public boolean vldDATA()
	{
		String strTEMP="";
		if(txtISSTP.getText().trim().length()==0)
		{
			setMSG("Issue Type Can not be Blank..",'E');
			txtISSTP.requestFocus();
			return false;
		}
		if(txtISSNO.getText().trim().length()==0)
		{
			setMSG("Issue Number Can not be Blank..",'E');
			txtISSNO.requestFocus();
			return false;
		}
		for(int i=0;i<tblLTDTL.getRowCount();i++)
    	{
			if(tblLTDTL.getValueAt(i,TB2_CHKFL).toString().equals("true"))
    		{
				strTEMP = nvlSTRVL(tblLTDTL.getValueAt(i,TB2_LOTNO).toString(),"");
    			if(strTEMP.length() == 0)
    			{
    				setMSG("Lot Number Can not be Blank..",'E');
					return false;
    			}
			
				strTEMP = nvlSTRVL(tblLTDTL.getValueAt(i,TB2_RCLNO).toString(),"");				
    			if(strTEMP.length() == 0)
    			{
    				setMSG("Re Classification Number Can not be Blank..",'E');
					return false;
    			}
			
				strTEMP = nvlSTRVL(tblLTDTL.getValueAt(i,TB2_MNLCD).toString(),"");
    			if(strTEMP.length() == 0)
    			{
    				setMSG("Main Location Can not be Blank..",'E');
					return false;
    			}
				strTEMP = nvlSTRVL(tblLTDTL.getValueAt(i,TB2_PKGTP).toString(),"");
    			if(strTEMP.length() == 0)
    			{
    				setMSG("Packege Type Can not be Blank..",'E');
					return false;
    			}
			}
		}
		return true;
	}
	
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *and to perform Data Input Output operation with the DataBase.
	*/
	void exeSAVE()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				if(!vldDATA())
					return;
				strPRDTP = getPRDTP();
				if(rdbISAUT.isSelected())
				{
					strUPDFL = "2";
					exeAUTADD();
				}
				else if(rdbISALO.isSelected())
				{
					strUPDFL = "1";	
					exeALOADD();
				}
			}
			chkISTRN(cmbWRHTP.getSelectedItem().toString().trim(),txtISSTP.getText().trim(),txtISSNO.getText().trim());
			getGRDREC();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
			
		}
	 	setCursor(cl_dat.M_curDFSTS_pbst);
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
	}
		
	
	
	private void exeALOADD()
	{
		try
		{
			//if(!chkPKGWT())
			//{
				//JOptionPane.showMessageDialog(this,"Record Not Saved","Error Message",JOptionPane.ERROR_MESSAGE);
				//return;
			//}
			strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
			strISSTP = getSUBSTR(txtISSTP.getText(),0,2);
			strISSNO = getSUBSTR(txtISSNO.getText(),0,8);		
			strMKTTP = getSUBSTR(txtMKTTP.getText(),0,2);		
			strISSDT = getSUBSTR(txtISSDT.getText(),0,10);
			strCNTDS = getSUBSTR(txtCNTDS.getText(),0,15);
			
			strDESTN = getSUBSTR(txtDESTN.getText(),0,14);
			strTSLNO = getSUBSTR(txtTSLNO.getText(),0,10);
			strRSLNO = getSUBSTR(txtRSLNO.getText(),0,10);
			
			setMSG("Updating Allocated Qty. in Stock Master",'N');
			
			
			updSTMST1();			
			
			
			setMSG("Updating FG_ISTRN",'N');
			addISTRN();
			//System.out.println(" FG_ISTRN: "+cl_dat.M_flgLCUPD_pbst);
			//setMSG("Commit Completed: ",'N');
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Stock has been Allocated Successfully",'N');
					JOptionPane.showMessageDialog(this,"Stock has been Allocated Successfully","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
					lblISQTY.setText("");
					lblISSPK.setText("");
					tblLTDTL.clrTABLE();
					rdbISALO.setSelected(true);
				}
			}
					
			else
			{
				setMSG("Data Not Saved",'N');
				//cl_dat.M_flgLCUPD_pbst = false;	
				//LM_OPTNPN.showMessageDialog(this,"Empty fields are not valid","Data Transfer Status",JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeALOADD");	
			//LM_CURTM = "";
			//LM_USRCD = "";
			//cl_cust.ocl_cust.updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",LM_CODCD,LM_CURTM,LM_USRCD);
		}
	}

	
	private void exeAUTADD()
	{
		try
		{
			//if(!chkPKGWT())
			//{
			//	JOptionPane.showMessageDialog(this,"Record Not Saved","Error Message",JOptionPane.ERROR_MESSAGE);
			//	return;
			//}
			
			strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
			strISSTP = getSUBSTR(txtISSTP.getText(),0,2);
			strISSNO = getSUBSTR(txtISSNO.getText(),0,8);		
			strMKTTP = getSUBSTR(txtMKTTP.getText(),0,2);		
			strISSDT = getSUBSTR(txtISSDT.getText(),0,10);
			strCNTDS = getSUBSTR(txtCNTDS.getText(),0,15);
			
			strDESTN = getSUBSTR(txtDESTN.getText(),0,14);
			strTSLNO = getSUBSTR(txtTSLNO.getText(),0,10);
			strRSLNO = getSUBSTR(txtRSLNO.getText(),0,10);
			
			updSTMST1();
			
			setMSG("Updating FG_ISTRN",'N');
			addISTRN();
			//System.out.println(" FG_ISTRN: "+cl_dat.M_flgLCUPD_pbst);
			
			setMSG("Updating MR_IVTRN",'N');
			addIVTRN();
			
			//setMSG("Commit Completed: ",'N');
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Stock has been Authorised Successfully",'N');
					JOptionPane.showMessageDialog(this,"Stock has been Authorised Successfully","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
					exeREAUT(txtISSNO.getText());
					tblLTDTL.clrTABLE();
					lblISQTY.setText("");
					lblISSPK.setText("");
					rdbISALO.setSelected(true);
				}
			}
			else
			{
				setMSG("Data Not Saved",'N');
				//cl_dat.ocl_dat.M_LCLUPD = false;	
				//LM_OPTNPN.showMessageDialog(this,"Empty fields are not valid","Data Transfer Status",JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeALOADD");	
			//LM_CURTM = "";
			//LM_USRCD = "";
			//cl_cust.ocl_cust.updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",LM_CODCD,LM_CURTM,LM_USRCD);
		}
	}

	
 /**
  * 
  * 
  * 
 */
	private void addISTRN()      //Inserting or Modifying Issue Transaction
	{
		try
		{
			if(!cl_dat.M_flgLCUPD_pbst)
				return;
			//lotROWCT = tblLTDTL.getRowCount();
			for(int i=0;i<tblLTDTL.getRowCount();i++)
			{
				if(tblLTDTL.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
				{
					if(getRECVAL(i))
					{	
						if(LM_INDISS)
						{
							//LM_SALTP = "";
							//strPRDTP = getIDPDT();
							//if(LM_ISNOFL)
							//	LM_ISSNO = LM_GNISNO;
							//else
							//	LM_ISSNO = getSUBSTR(txtISSNO.getText(),0,8);		
						}
						//M_strSQLQRY = "Select * from fg_istrn where"; 
						M_strSQLQRY = " IST_WRHTP = '"+strWRHTP+"'";
						M_strSQLQRY += " and IST_ISSTP = '"+strISSTP+"'";
						M_strSQLQRY += " and IST_ISSNO = '"+strISSNO+"'";
						M_strSQLQRY += " and IST_PRDCD = '"+strPRDCD+"'";
						M_strSQLQRY += " and IST_PRDTP = '"+strPRDTP+"'";
						M_strSQLQRY += " and IST_LOTNO = '"+strLOTNO+"'";
						M_strSQLQRY += " and IST_RCLNO = '"+strRCLNO+"'";
						M_strSQLQRY += " and IST_PKGTP = '"+strPKGTP+"'";
						M_strSQLQRY += " and IST_MNLCD = '"+strMNLCD+"'";
						//System.out.println("Select count(*) = "+M_strSQLQRY);
						
						flgCHK_EXIST =chkEXIST("FG_ISTRN",M_strSQLQRY);   
						//if(cl_dat.ocl_dat.getRECCNT(M_strSQLQRY) > 0)
						if(flgCHK_EXIST)
							updISTRN();
						else
							insISTRN();
						//System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"addISTRN");
		}
	}

	/**
     * 
    */
	
	private boolean getRECVAL(int i)
	{
		try
		{
			if(tblLTDTL.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))				
			{	
				strTDSFL = tblLTDTL.getValueAt(i,TB2_TDSFL).toString().trim();
				strLOTNO = tblLTDTL.getValueAt(i,TB2_LOTNO).toString().trim();
				strRCLNO = tblLTDTL.getValueAt(i,TB2_RCLNO).toString().trim();
				strMNLCD = tblLTDTL.getValueAt(i,TB2_MNLCD).toString().trim();
				strPKGCT = tblLTDTL.getValueAt(i,TB2_PKGCT).toString().trim();
				strPKGTP = tblLTDTL.getValueAt(i,TB2_PKGTP).toString().trim();
				strSTKQT = setNumberFormat(Double.parseDouble(nvlSTRVL(tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim(),"0")),3);
				strISSPK = tblLTDTL.getValueAt(i,TB2_ISSPK).toString().trim();
				//System.out.println("getRECVAL : strALOQT "+strALOQT);
				//System.out.println("getRECVAL : LM_UPDQT : "+LM_UPDQT);
				return true;
			}
			else
				return false;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRECVAL");	
		}
		return false;
	}
	
	/**
	 *   Function to Modify Issue Transaction 
	*/
	private void updISTRN()      //Modifies Issue Transaction i.e FG_ISTRN
	{
		try
		{
			String L_strUPDFL = strUPDFL;
			
			if(strSTKQT.equals("0.000"))
				L_strUPDFL = "X";
						
			//if(!chkSTKUPD(strSTKQT))
			//	{cl_dat.M_flgLCUPD_pbst = false; return;}
			M_strSQLQRY = "Update fg_istrn set";
			M_strSQLQRY += " IST_ISSQT = "+strSTKQT+",";
			M_strSQLQRY += " IST_ISSPK = "+strISSPK+",";
			M_strSQLQRY += " IST_PKGCT = '"+strPKGCT+"',";
			M_strSQLQRY += " IST_STSFL = '"+L_strUPDFL+"',";
			if(L_strUPDFL.equals("2"))
				M_strSQLQRY += " IST_AUTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY += " IST_TRNFL = '0',";
			M_strSQLQRY += " IST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += " IST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and IST_ISSTP = '"+strISSTP+"'";
			M_strSQLQRY += " and IST_ISSNO = '"+strISSNO+"'";
			M_strSQLQRY += " and IST_PRDCD = '"+strPRDCD+"'";
			M_strSQLQRY += " and IST_PRDTP = '"+strPRDTP+"'";
			M_strSQLQRY += " and IST_LOTNO = '"+strLOTNO+"'";
			M_strSQLQRY += " and IST_RCLNO = '"+strRCLNO+"'";
			M_strSQLQRY += " and IST_PKGTP = '"+strPKGTP+"'";
			M_strSQLQRY += " and IST_MNLCD = '"+strMNLCD+"'";
			//cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
			//System.out.println(" updISTRN = "+M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updISTRN");
		}
	}


	/** Method to verify that stock qty.does not go negative after issue updation
	 */
	private boolean chkSTKUPD(String LP_ISSQT)
	{
		double L_dblISSQT_CHK = Double.parseDouble(LP_ISSQT);
		double L_dblSTKQT_CHK = 0.000;
		boolean L_flgRETFL = true;
		try
		{
			if(hstSTCHK.containsKey(strPRDTP+strLOTNO+strRCLNO+strPKGTP+strMNLCD))
			{
			   L_dblSTKQT_CHK = Double.parseDouble(hstSTCHK.get(strPRDTP+strLOTNO+strRCLNO+strPKGTP+strMNLCD).toString());
			   System.out.println("Issue Qty :"+setNumberFormat(L_dblISSQT_CHK,3)+" Stock Qty : "+setNumberFormat(L_dblSTKQT_CHK,3)+"   at "+strPRDTP+" / "+strLOTNO+" / "+strRCLNO+" / "+strPKGTP+" / "+strMNLCD);
			   if(L_dblSTKQT_CHK < L_dblISSQT_CHK)
			   {
				   JOptionPane.showMessageDialog(this,"Issue Qty :"+setNumberFormat(L_dblISSQT_CHK,3)+" more than Stock Qty : "+setNumberFormat(L_dblSTKQT_CHK,3)+"\n at "+strPRDTP+" / "+strLOTNO+" / "+strRCLNO+" / "+strPKGTP+" / "+strMNLCD ,"Warning",JOptionPane.INFORMATION_MESSAGE);					
				   L_flgRETFL = false;
			   }
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkSTKUPD");
		}
		return L_flgRETFL;
	}
	
	/**
	 *   Function to Delete unwanted Issue Transactions 
	*/
	private void chkISTRN(String LP_WRHTP, String LP_ISSTP, String LP_ISSNO)      //Removes records from FG_ISTRN which are marked for deletion
	{
		try
		{
			
			M_strSQLQRY = "delete from fg_istrn ";
			M_strSQLQRY += " where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+LP_WRHTP+"'";
			M_strSQLQRY += " and IST_ISSTP = '"+LP_ISSTP+"'";
			M_strSQLQRY += " and IST_ISSNO = '"+LP_ISSNO+"'";
			M_strSQLQRY += " and IST_ISSQT = 0 ";
			M_strSQLQRY += " and IST_STSFL = 'X'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			cl_dat.exeDBCMT("delete");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkISTRN");
		}
	}
	
	private void insISTRN()      //Inserts into Issue Transaction i.e FG_ISTRN
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				String L_strUPDFL = strUPDFL;
				//if(setFMT(LM_STKQT,3).equals("0.000"))
				//	L_UPDFL = "X";
				
				M_strSQLQRY = "Insert into fg_istrn (IST_CMPCD,IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDTP,";
				M_strSQLQRY += "IST_TDSFL,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGCT,";
				M_strSQLQRY += "IST_PKGTP,IST_ISSDT,IST_ISSQT,IST_ISSPK,IST_STKTP,";
				M_strSQLQRY += "IST_STSFL,"+(L_strUPDFL.equals("2") ? "IST_AUTDT," : "")+"IST_TRNFL,IST_LUSBY,IST_LUPDT,";
				M_strSQLQRY += " IST_PRDCD,IST_SALTP,IST_MKTTP,IST_RESFL) values (";
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
				M_strSQLQRY += "'"+strWRHTP+"',";
				M_strSQLQRY += "'"+strISSTP+"',";
				M_strSQLQRY += "'"+strISSNO+"',";
				M_strSQLQRY += "'"+strPRDTP+"',";
				M_strSQLQRY += "'"+strTDSFL+"',";
				
				M_strSQLQRY += "'"+strLOTNO+"',";
				M_strSQLQRY += "'"+strRCLNO+"',";
				M_strSQLQRY += "'"+strMNLCD+"',";
				M_strSQLQRY += "'"+strPKGCT+"',";
				M_strSQLQRY += "'"+strPKGTP+"',";
				
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strISSDT))+"',";
				M_strSQLQRY += strSTKQT+",";
				M_strSQLQRY += strISSPK+",";
				M_strSQLQRY += "'"+strSTKTP+"',";
				M_strSQLQRY += "'"+L_strUPDFL+"',"+(L_strUPDFL.equals("2") ? "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'," : "");
				
				M_strSQLQRY += "'0',";
				M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
				M_strSQLQRY += "'"+strPRDCD+"',";
				M_strSQLQRY += "'"+strSALTP+"',";
				M_strSQLQRY += "'"+strMKTTP+"',";
				M_strSQLQRY += "'"+strRESFL+"')";
				//cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
				//System.out.println("insISTRN = "+M_strSQLQRY);
				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"insISTRN");
			}
		}
	}

	/** 
	 * Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	*/
	
	private String getRSTVAL(ResultSet P_rstRSSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
			if (LP_FLDTP.equals("C"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? P_rstRSSET.getString(LP_FLDNM).toString() : "";
			//return P_rstRSSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString()," ")) : "";
			else if (LP_FLDTP.equals("N"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString(),"0") : "0";
			else if (LP_FLDTP.equals("D"))
				return P_rstRSSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(P_rstRSSET.getDate(LP_FLDNM)) : "";
			else if (LP_FLDTP.equals("T"))
				return P_rstRSSET.getTimestamp(LP_FLDNM) != null ? M_fmtLCDTM.format(P_rstRSSET.getTimestamp(LP_FLDNM)) : "";
			 //   return M_fmtLCDTM.parse(P_rstRSSET.getString(LP_FLDNM)));
			else 
				return " ";
		}
		catch (Exception L_EX)
		{
		    System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 
	
	/**
	 * Displays loading status for current day 
	 *
	*/
	private void dspDSPST()
	{
		try
		{//DISPLAY WINDOW FOR SHOWING CURRENT LOADING STATUS
			String L_strSTSFL="";
			String L_strSTSDS = "";
			String L_LADTM="";
			String L_ALOTM="";
			String L_LODTM ="";
			if(chkDSPST.isSelected()==false)
				return;
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(pnlDSPST==null)
			{
				pnlDSPST=new JPanel(null);
				String[] L_staCOLHD = {"FL","Lorry No.","Prep.At","Aloc.At","Load At","Status","Sec.No.","LA No.","Grade","P.Type","Qty","Transporter"};
				int[] L_inaCOLSZ = {20,100,40,40,40,100,80,80,90,20,60,90};
				tblDSPST = crtTBLPNL1(pnlDSPST,L_staCOLHD,500,1,1,6,8,L_inaCOLSZ,new int[]{0});
			}
			tblDSPST.clrTABLE();
				
			M_strSQLQRY = "select ivt_lryno, ivt_ginno,ivt_ladno,ivt_prdds,";
			M_strSQLQRY+="ivt_pkgtp,ivt_reqqt,ivt_ladqt,ivt_laddt,ivt_alodt,";
			M_strSQLQRY+="ivt_loddt,ivt_stsfl,ivt_trpcd,pt_prtnm  from mr_ivtrn,co_ptmst";
			M_strSQLQRY+=" where pt_prttp='T' and pt_prtcd=ivt_trpcd and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp in ('01','03','04','05')";
			M_strSQLQRY+=" and (CONVERT(varchar,ivt_laddt,101) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' ";
			M_strSQLQRY+=" or CONVERT(varchar,ivt_alodt,101) = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
			M_strSQLQRY+=" order by ivt_lryno,ivt_ladno,ivt_prdds";
			//System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
			{
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			int i =0;
			while (true)
			{
				L_LADTM = getRSTVAL(L_rstRSSET,"IVT_LADDT","T");
				L_ALOTM = getRSTVAL(L_rstRSSET,"IVT_ALODT","T");
				L_LODTM = getRSTVAL(L_rstRSSET,"IVT_LODDT","T");
				if(L_LADTM.length()>10)
					L_LADTM = L_LADTM.substring(11,16);
				if(L_ALOTM.length()>10)
					L_ALOTM = L_ALOTM.substring(11,16);
				if(L_LODTM.length()>10)
					L_LODTM = L_LODTM.substring(11,16);
				
				L_strSTSFL = getRSTVAL(L_rstRSSET,"IVT_STSFL","C");
				
				if(L_strSTSFL.equalsIgnoreCase("A") && L_ALOTM.length()<2)
				   L_strSTSDS = "LA prepared";
				else if(L_strSTSFL.equalsIgnoreCase("A") && L_ALOTM.length()>2)
				   L_strSTSDS = "Allocated";
				else if(L_strSTSFL.equalsIgnoreCase("2"))
				   L_strSTSDS = "Loaded";
				if(L_strSTSFL.equalsIgnoreCase("L"))
				   L_strSTSDS = "Authorised for Invoice";
				if(L_strSTSFL.equalsIgnoreCase("D"))
				   L_strSTSDS = "Despatched";
				if(L_strSTSFL.equalsIgnoreCase("X"))
				   L_strSTSDS = "Cancelled";
				tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"IVT_LRYNO","C"),i,TB4_LRYNO);
				tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"IVT_GINNO","C"),i,TB4_GINNO);
				tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"IVT_LADNO","C"),i,TB4_LADNO);
				tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"IVT_PRDDS","C"),i,TB4_PRDDS);
				tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"IVT_PKGTP","C"),i,TB4_PKGTP);
				tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"IVT_REQQT","N"),i,TB4_REQQT);
				//tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"IVT_LADQT","N"),i,TB4_LADQT);
				tblDSPST.setValueAt(L_LADTM,i,TB4_LADTM);
				tblDSPST.setValueAt(L_ALOTM,i,TB4_ALOTM);
				tblDSPST.setValueAt(L_LODTM,i,TB4_LODTM);
				tblDSPST.setValueAt(L_strSTSDS,i,TB4_STSDS);
				tblDSPST.setValueAt(getRSTVAL(L_rstRSSET,"PT_PRTNM","C"),i,TB4_TRPNM);
				i++;
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
				
			pnlDSPST.setSize(100,100);
			pnlDSPST.setPreferredSize(new Dimension(700,250));
			//pnlRETCP.setPreferredSize(new Dimension(700,250));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDSPST,"Today's loading status",JOptionPane.OK_CANCEL_OPTION);
			//int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDSPST,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch (Exception L_EX)
		{
			setMSG("Error in dspDSPST : "+L_EX,'E');
		}
	}


	
	/**
	 * 
	 *  
	*/
	
	private void getVEHREJ()
	{
		try
		{
			//DISPLAY WINDOW FOR ENTERING VEHICLE REJECTION
			if(chkVHREJ_E.isSelected()==false)
				return;
			if(pnlVHREJ_E==null)
			{
				pnlVHREJ_E=new JPanel(null);
				add(new JLabel("Gate In"),3,1,1,2,pnlVHREJ_E,'L');
				add(txtGINNO_VR = new TxtLimit(15),3,3,1,1,pnlVHREJ_E,'L');
				//txtGINNO_VR = crtTXT(pnlVHREJ_E,LEFT,120,40,100,20);	
				add(new JLabel("Rej.Code"),4,1,1,2,pnlVHREJ_E,'L');
				add(txtREJCD_VR = new TxtLimit(2),4,3,1,1,pnlVHREJ_E,'L');
							
				add(new JLabel("Rej.Date"),5,1,1,2,pnlVHREJ_E,'L');
				add(txtREJDT_VR = new TxtDate(),5,3,1,1,pnlVHREJ_E,'L');
							
				add(new JLabel("Corrected  (Y/N):"),6,1,1,2,pnlVHREJ_E,'L');
				add(txtCORFL_VR = new TxtLimit(1),6,3,1,1,pnlVHREJ_E,'L');
               
				add(new JLabel("Despatched (Y/N)"),7,1,1,2,pnlVHREJ_E,'L');
				add(txtDSPFL_VR = new TxtLimit(1),7,3,1,1,pnlVHREJ_E,'L');
				
				add(new JLabel("Remark"),8,1,1,2,pnlVHREJ_E,'L');
				add(txtREMDS_VR = new TxtLimit(40),8,3,1,3,pnlVHREJ_E,'L');
				
				add(new JLabel("Rej.By "),9,1,1,1,pnlVHREJ_E,'L');
				add(txtREJBY_VR = new TxtLimit(5),9,3,1,2,pnlVHREJ_E,'L');

				txtGINNO_VR.setText(strGINNO);
				setVEHREJ_DEF();
				setMSG("Blank Entry in Rej.By column to Delete the record",'N');
			}

				
			pnlVHREJ_E.setSize(100,100);
			pnlVHREJ_E.setPreferredSize(new Dimension(700,600));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVHREJ_E,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
			if(L_intOPTN==0)
			{
				if(txtGINNO_VR.getText().length()<8)
				{
					setVEHREJ_DEF();
					return;
				}
				if(txtREJCD_VR.getText().length()<2)
				{
					setVEHREJ_DEF();	
					return;
				}
				strWHERE =  "VR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VR_DOCTP = '"+strDOCTP+"' and "
							+"VR_DOCNO = '" +strGINNO+"' and "
							+"VR_REJCD = '" +txtREJCD_VR.getText()+"'";

				flgCHK_EXIST =  chkEXIST("FG_VRTRN",strWHERE);
				String L_strSQLQRY="";
				if(!flgCHK_EXIST)
				{
					L_strSQLQRY="insert into FG_VRTRN (VR_CMPCD,VR_DOCTP, VR_DOCNO, VR_REJCD, VR_REJDT, ";
					L_strSQLQRY += "VR_TRPCD, VR_LRYNO, VR_CNTDS, VR_CORFL,";
					L_strSQLQRY += " VR_DSPFL, VR_REMDS, VR_REJBY, VR_TRNFL, VR_STSFL,";
					L_strSQLQRY += " VR_LUPDT, VR_LUSBY) values (";
					L_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					L_strSQLQRY += "'"+strDOCTP+"',";
					L_strSQLQRY += "'"+strGINNO+"',";
					L_strSQLQRY += "'"+txtREJCD_VR.getText()+"',";
					L_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtREJDT_VR.getText()))+"',";
					L_strSQLQRY += "'"+strTRPCD+"',";
					L_strSQLQRY += "'"+strLRYNO+"',";
					L_strSQLQRY += "'"+strCNTDS+"',";
					L_strSQLQRY += "'"+txtCORFL_VR.getText()+"',";
					L_strSQLQRY += "'"+txtDSPFL_VR.getText()+"',";
					L_strSQLQRY += "'"+txtREMDS_VR.getText()+"',";
					L_strSQLQRY += "'"+txtREJBY_VR.getText()+"',";
					L_strSQLQRY += "'"+0+"',";
					L_strSQLQRY += "'"+0+"',";
					L_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
					L_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"')";
										
					
				}
				else if(flgCHK_EXIST)
				{
					L_strSQLQRY="update FG_VRTRN set ";
					L_strSQLQRY +=" VR_REJDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtREJDT_VR.getText()))+"',";
					L_strSQLQRY +=" VR_TRPCD='"+strTRPCD+"',";
					L_strSQLQRY +=" VR_LRYNO='"+strLRYNO+"',";
					L_strSQLQRY +=" VR_CNTDS='"+strCNTDS+"',";
					L_strSQLQRY +=" VR_CORFL='"+txtCORFL_VR.getText()+"',";
					L_strSQLQRY +=" VR_DSPFL='"+txtDSPFL_VR.getText()+"',";
					L_strSQLQRY +=" VR_REMDS='"+txtREMDS_VR.getText()+"',";
					L_strSQLQRY +=" VR_REJBY='"+txtREJBY_VR.getText()+"',";
					
					L_strSQLQRY += " VR_STSFL='0',";
					L_strSQLQRY += " VR_TRNFL='0',";
					L_strSQLQRY += " VR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
					L_strSQLQRY += " VR_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
					
					L_strSQLQRY += "  where "+ strWHERE;
					if(txtREJBY_VR.getText().equalsIgnoreCase("XXX") || txtREJBY_VR.getText().equalsIgnoreCase(""))
						L_strSQLQRY="delete from FG_VRTRN where "+ strWHERE;
				}
				//System.out.println(L_strSQLQRY);
				cl_dat.exeSQLUPD(L_strSQLQRY,"");
				cl_dat.exeDBCMT("save");
				
				setMSG("Saved Successfully",'N');
			
			}
			//saveVRTRN();
		}
		catch (Exception L_EX)
		{
			setMSG("Error in getVEHREJ : "+L_EX,'E');
		}
	}

	
	
	/**
	 * 
	*/
	private void setVEHREJ_DEF()   //DISPLAY WINDOW FOR ENTERING VEHICLE REJECTION
	{
		try
		{
			txtCORFL_VR.setText("N");
			txtDSPFL_VR.setText("N");
			txtREJDT_VR.setText(cl_dat.M_strLOGDT_pbst);
			txtREMDS_VR.setText("");
			txtREJBY_VR.setText(cl_dat.M_strUSRCD_pbst);
			setMSG("Blank Entry in Rej.By column to Delete the record",'N');
		}
		catch (Exception L_EX)
		{
			setMSG("Error in setVEHREJ_DEF : "+L_EX,'E');
		}
	}

	
	/**
	 */
	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
	{
		boolean L_flgCHKFL = false;
		try
		{
			String L_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
			//System.out.println(" chkEXIST = "+L_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				L_flgCHKFL = true;
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)	
		{
			setMSG("Error in chkEXIST : "+L_EX,'E');
		}
		return L_flgCHKFL;
	}
	
	
	/**
	 * Displays Vehicle Rejection records if Lorry No. or Container Number is found in FG_VRTRN
	 * (Condition confirms that Lorry number/Container number being matched is not blank)
	*/
	private void dspVEHREJ()
	{
		try    //DISPLAY WINDOW FOR ENTERING VEHICLE REJECTION
		{
			//if(chkVHREJ_V.isSelected()==false)
			//	return;
			if(pnlVHREJ_V==null)
			{
				pnlVHREJ_V=new JPanel(null);
				
				String[] L_staCOLHD = {"FL.","Gate In","R.Code","Description","Date","Corrected","Despatched","Remark","Lorry No.","Contr.No."};
				int[] L_inaCOLSZ = {20,80,50,120,100,30,30,200,100,100};
				tblVEHRJ = crtTBLPNL1(pnlVHREJ_V,L_staCOLHD,500,1,1,6,8,L_inaCOLSZ,new int[]{0});
				tblVEHRJ.addKeyListener(this);
				tblVEHRJ.setCellEditor(TB3_REJCD,txtREJCD_VR = new TxtLimit(2));
			}
			//clrVHRTBL();
				
			tblVEHRJ.clrTABLE();
			//System.out.println("lllllllllllll");
			M_strSQLQRY = "select * from fg_vrtrn where VR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND vr_doctp='"+strDOCTP+"' and ((vr_lryno = '"+strLRYNO+"'  and len(isnull(VR_LRYNO,''))>1) or (vr_cntds = '"+strCNTDS+"' and len(isnull(VR_CNTDS,''))>1))  order by vr_rejdt desc";
			//System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			int i =0;
			while (true)
			{
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_DOCNO","C"),i,TB3_DOCNO);
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_LRYNO","C"),i,TB3_LRYNO);
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_CNTDS","C"),i,TB3_CNTDS);
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_REJDT","D"),i,TB3_REJDT);
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_REJCD","C"),i,TB3_REJCD);
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_CORFL","C"),i,TB3_CORFL);
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_DSPFL","C"),i,TB3_DSPFL);
				tblVEHRJ.setValueAt(getRSTVAL(L_rstRSSET,"VR_REMDS","C"),i,TB3_REMDS);
				i++;
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
				
			pnlVHREJ_V.setSize(100,100);
			pnlVHREJ_V.setPreferredSize(new Dimension(700,250));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVHREJ_V,"Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
			//int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVHREJ_V,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
		}

		catch (Exception L_EX)
		{
			setMSG(L_EX,"Error in dspVEHREJ ");
		}
	}
	
	/**
	 *
	 *  Displaying Vehicle detntion detail
	 *
	*/
	private void getVHDTN(String LP_LADNO)
	{
		try
		{//DISPLAY WINDOW FOR SHOWING CURRENT LOADING STATUS
			//System.out.println("LP_LADNO : "+LP_LADNO);
			if(chkVHDTN.isSelected()==false)
				return;
			if(LP_LADNO.length()!=8)
				return;
			
			//this.setCursor(curWTSTS);
			setCursor(cl_dat.M_curWTSTS_pbst);
		
			//LM_ROWCNT = 20;
			if(pnlVHDTN==null)
			{
				pnlVHDTN=new JPanel(null);
				String[] L_staCOLHD = {"FL","Srl.No.","Det.Code","Description","Time From","Time To","Eff.Time"};
				int[] L_inaCOLSZ = {20,80,80,200,100,100,80};
				tblVHDTN = crtTBLPNL1(pnlVHDTN,L_staCOLHD,500,1,1,6,8,L_inaCOLSZ,new int[]{0});
				txtEDITR_VHD = (JTextField)tblVHDTN.getCellEditor(1,1).getTableCellEditorComponent(tblVHDTN,"",false,1,1);
				tblVHDTN.setCellEditor(TB6_DETCD,txtEDITR_VHD);
				tblVHDTN.addKeyListener(this);
				tblVHDTN.addMouseListener(this);
				tblVHDTN.addFocusListener(this);
			
				add(lblVHDMSG = new JLabel(""),9,1,1,6,pnlVHDTN,'L');
			}
			ResultSet L_rstRSSET=null;
			//clrVHDTBL();
			tblVHDTN.clrTABLE();
			M_strSQLQRY = "select ivt_ginno,ivt_lryno  from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ladno =  '"+LP_LADNO+"'";
			//System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
			{
				return;
			}
			String L_GINNO = getRSTVAL(L_rstRSSET,"IVT_GINNO","C");
			String L_LRYNO = getRSTVAL(L_rstRSSET,"IVT_LRYNO","C");
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
			lblVHDMSG.setText("Lorry No.:"+L_LRYNO+"               Enter Det.Code as XX to delete the record ...");				
			
			M_strSQLQRY = "select iv1_srlno,iv1_detcd,cmt_codds,iv1_strtm,iv1_endtm,iv1_efftm from fg_ivtr1,co_cdtrn  where cmt_cgmtp='SYS' and cmt_cgstp='FGXXVHD' and cmt_codcd=iv1_detcd and IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv1_mkttp = '"+strMKTTP+"' and iv1_ginno = '"+L_GINNO+"' order by iv1_srlno";
			//System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i =0;
			if(L_rstRSSET.next() && L_rstRSSET!=null)
			{
				while (true)
				{
					tblVHDTN.setValueAt(getRSTVAL(L_rstRSSET,"IV1_SRLNO","C"),i,TB6_SRLNO);
					tblVHDTN.setValueAt(getRSTVAL(L_rstRSSET,"IV1_DETCD","C"),i,TB6_DETCD);
					tblVHDTN.setValueAt(getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),i,TB6_DETDS);
					tblVHDTN.setValueAt(getRSTVAL(L_rstRSSET,"IV1_STRTM","T"),i,TB6_STRTM);
					tblVHDTN.setValueAt(getRSTVAL(L_rstRSSET,"IV1_ENDTM","T"),i,TB6_ENDTM);
					tblVHDTN.setValueAt(getRSTVAL(L_rstRSSET,"IV1_EFFTM","N"),i,TB6_EFFTM);
					i++;
					if(!L_rstRSSET.next())
						break;
				}
			L_rstRSSET.close();
			}
				
			//this.setCursor(curDFSTS);
			setCursor(cl_dat.M_curDFSTS_pbst);
			pnlVHDTN.setSize(100,100);
			pnlVHDTN.setPreferredSize(new Dimension(700,250));
			boolean L_flgEXIST = false;
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVHDTN,"Vehicle Detention",JOptionPane.OK_CANCEL_OPTION);
			if(L_intOPTN == 0)
			{
					
				for(int j=0;j<tblVHDTN.getRowCount();j++)
				{
					if(tblVHDTN.getValueAt(j,TB6_DETCD).toString().length()<2)
					{
						break;
					}
					if(tblVHDTN.getValueAt(i,TB6_CHKFL).toString().equals("true"))
					{
						strWHERE  = "IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv1_mkttp='"+strMKTTP+"' and iv1_ginno = '"+L_GINNO+"' and iv1_srlno='"+tblVHDTN.getValueAt(j,TB6_SRLNO).toString()+"'";
						flgCHK_EXIST =  chkEXIST("FG_IVTR1",strWHERE);
		
						if(!flgCHK_EXIST)
						{
							M_strSQLQRY = "insert into fg_ivtr1 (iv1_cmpcd,iv1_mkttp,iv1_ginno,iv1_srlno,iv1_detcd,iv1_strtm,iv1_endtm,iv1_efftm,iv1_trnfl,iv1_stsfl,iv1_lupdt,iv1_lusby) values (" 
							+setINSSTR("IV1_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
							+setINSSTR("IV1_MKTTP",strMKTTP,"C")
							+setINSSTR("IV1_LADNO",L_GINNO,"C")
							+setINSSTR("IV1_SRLNO",tblVHDTN.getValueAt(j,TB6_SRLNO).toString(),"C")
							+setINSSTR("IV1_DETCD",tblVHDTN.getValueAt(j,TB6_DETCD).toString(),"C")
							+setINSSTR("IV1_STRTM",tblVHDTN.getValueAt(j,TB6_STRTM).toString(),"T")
							+setINSSTR("IV1_ENDTM",tblVHDTN.getValueAt(j,TB6_ENDTM).toString(),"T")
							+setINSSTR("IV1_EFFTM",tblVHDTN.getValueAt(j,TB6_EFFTM).toString(),"N")
							+setINSSTR("IV1_TRNFL","0","C")
							+setINSSTR("IV1_STSFL","1","C")
							+setINSSTR("IV1_LUPDT","'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'","N")
							+"'"+cl_dat.M_strUSRCD_pbst+"')";
						}
						else
						{
							M_strSQLQRY = "update fg_ivtr1 set "
							+setUPDSTR("IV1_DETCD",tblVHDTN.getValueAt(j,TB6_DETCD).toString(),"C")
							+setUPDSTR("IV1_STRTM",tblVHDTN.getValueAt(j,TB6_STRTM).toString(),"T")
							+setUPDSTR("IV1_ENDTM",tblVHDTN.getValueAt(j,TB6_ENDTM).toString(),"T")
							+setUPDSTR("IV1_EFFTM",tblVHDTN.getValueAt(j,TB6_EFFTM).toString(),"N")
							+setUPDSTR("IV1_TRNFL","0","C")
							+setUPDSTR("IV1_STSFL","1","C")
							+setUPDSTR("IV1_LUPDT","'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'","N")
							+"IV1_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"' where "+strWHERE;
						}
						//System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"");
						
						M_strSQLQRY = "delete from fg_ivtr1 where IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(iv1_detcd)='XX'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"");
						if(cl_dat.exeDBCMT("Saved"))
						{
							setMSG("Data saved successfully",'N');
						}
						setMSG("Saved Successfully",'N');
					}
				}
			}
			//this.setCursor(curDFSTS);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}

		catch (Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG("Error in getVHDTN : "+L_EX,'E');
		}
	}

	
	
	/**
	 * Displaying M.B.pallet detail
	 *
	 */
	private void getMBPLT(String LP_LADNO)
	{
		try
		{//DISPLAY WINDOW FOR SHOWING CURRENT LOADING STATUS
		//	System.out.println("LP_LADNO : "+LP_LADNO);
			if(chkMBPLT.isSelected()==false)
				return;
			if(strLADNO.length()!=8)
				return;
			
			//this.setCursor(curWTSTS);
			setCursor(cl_dat.M_curWTSTS_pbst);

			//LM_ROWCNT = 20;
			if(pnlMBPLT==null)
			{
				pnlMBPLT=new JPanel(null);
				//cl_dat.ocl_dat.M_CHKTBL = false;
				String[] L_staCOLHD = {"FL","Srl.No.","Lot No.","Treatment Date","Quantity"};
				int[] L_inaCOLSZ = {20,100,120,150,250};
				//tblMBPLT = crtTBLPNL(pnlMBPLT,L_MBPHD,LM_ROWCNT,0,0,780,150,L_COLSZ,0);
				tblMBPLT = crtTBLPNL1(pnlMBPLT,L_staCOLHD,50,1,1,6,8,L_inaCOLSZ,new int[]{0});
				
				tblMBPLT.setInputVerifier(new TBLINPVF());
				tblMBPLT.addKeyListener(this);
				tblMBPLT.setCellEditor(TB5_CHKFL,chkCHKFL5 = new JCheckBox());
				tblMBPLT.setCellEditor(TB5_LOTNO,txtLOTNO5 = new TxtLimit(8));
				tblMBPLT.setCellEditor(TB5_SRLNO,txtSRLNO5 = new TxtLimit(2));
				tblMBPLT.setCellEditor(TB5_TRMDT,txtTRMDT5 = new TxtDate());			
			
				txtLOTNO5.addKeyListener(this);
				txtLOTNO5.addFocusListener(this);
				txtSRLNO5.addKeyListener(this);
				txtSRLNO5.addFocusListener(this);
				txtTRMDT5.addKeyListener(this);
				txtTRMDT5.addFocusListener(this);
				add(lblMBPMSG = new JLabel("Enter zero Qty. to delete the record ..."),9,1,1,6,pnlMBPLT,'L');
				
				//txtEDITR_MBP = (JTextField)tblMBPLT.getCellEditor(1,1).getTableCellEditorComponent(tblMBPLT,"",false,1,1);
				//setCOLWDT(LM_LSQTBL,L_MBPHD,L_COLSZ);
				//tblMBPLT.addMouseListener(this);
				//tblMBPLT.addFocusListener(this);
				//tblMBPLT.addKeyListener(this);

				//lblMBPMSG = new JLabel("Enter zero Qty. to delete the record ...");
				//lblMBPMSG.setLocation(35,200);
				//lblMBPMSG.setSize(500,20);
				//pnlMBPLT.add(lblMBPMSG);
				
				
			}
			//clrMBPTBL();
			tblMBPLT.clrTABLE();
	
			ResultSet L_rstRSSET=null;
			M_strSQLQRY = "select ivt_ginno,ivt_cntds from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp = '"+strMKTTP+"' and ivt_ladno = '"+LP_LADNO+"'";
			//System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			String L_GINNO = getRSTVAL(L_rstRSSET,"IVT_GINNO","C");
			String L_CNTDS = getRSTVAL(L_rstRSSET,"IVT_CNTDS","C");
				
			lblMBPMSG.setText("Container No.:"+L_CNTDS+"               Enter zero Qty. to delete the record ...");				
			M_strSQLQRY = "select iv2_srlno,iv2_lotno,iv2_trmdt,iv2_pltqt from fg_ivtr2 where IV2_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv2_mkttp = '"+strMKTTP+"' and iv2_ginno = '"+L_GINNO+"' order by iv2_srlno";
			//System.out.println(M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i =0;
			if(L_rstRSSET.next() && L_rstRSSET!=null)
			{
				while (true)
				{
					tblMBPLT.setValueAt(getRSTVAL(L_rstRSSET,"IV2_SRLNO","C"),i,TB5_SRLNO);
					tblMBPLT.setValueAt(getRSTVAL(L_rstRSSET,"IV2_LOTNO","C"),i,TB5_LOTNO);
					tblMBPLT.setValueAt(getRSTVAL(L_rstRSSET,"IV2_PLTQT","C"),i,TB5_PLTQT);
					tblMBPLT.setValueAt(getRSTVAL(L_rstRSSET,"IV2_TRMDT","D"),i,TB5_TRMDT);
					i++;
					if(!L_rstRSSET.next())
						break;
				}
				L_rstRSSET.close();
			}
			//this.setCursor(curDFSTS);
			setCursor(cl_dat.M_curDFSTS_pbst);
			pnlMBPLT.setSize(100,100);
			pnlMBPLT.setPreferredSize(new Dimension(700,250));
			boolean L_flgEXIST = false;
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlMBPLT,"M.B.Treated Pallet",JOptionPane.OK_CANCEL_OPTION);
			//System.out.println(" L_intOPTN = "+L_intOPTN);
			if(L_intOPTN == 0)
			{
				for(int j=0;j<tblMBPLT.getRowCount();j++)
				{
					if(tblMBPLT.getValueAt(j,TB5_LOTNO).toString().length()<2)
					{
						break;
					}
					if(tblMBPLT.getValueAt(j,TB5_CHKFL).toString().equals("true"))
					{
						//System.out.println(" flgCHK_EXIST = "+flgCHK_EXIST);
						strWHERE = "IV2_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv2_mkttp='"+strMKTTP+"' and iv2_ginno = '"+L_GINNO+"' and iv2_srlno='"+tblMBPLT.getValueAt(j,TB5_SRLNO).toString()+"'";
						flgCHK_EXIST =  chkEXIST("FG_IVTR2",strWHERE);
						//System.out.println(" flgCHK_EXIST = "+flgCHK_EXIST);
						if(!flgCHK_EXIST)
						{
							M_strSQLQRY = "insert into fg_ivtr2 (iv2_cmpcd,iv2_mkttp,iv2_ginno,iv2_srlno,iv2_lotno,iv2_cntds,iv2_pltqt,iv2_trmdt,iv2_trnfl,iv2_stsfl,iv2_lupdt,iv2_lusby) values (";
							M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'"+strMKTTP+"',";
							M_strSQLQRY += "'"+L_GINNO+"',";
							M_strSQLQRY += "'"+tblMBPLT.getValueAt(j,TB5_SRLNO).toString()+"',";
							M_strSQLQRY += "'"+tblMBPLT.getValueAt(j,TB5_LOTNO).toString()+"',";
							M_strSQLQRY += "'"+L_CNTDS+"',";
							M_strSQLQRY += ""+tblMBPLT.getValueAt(j,TB5_PLTQT).toString()+",";
							M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblMBPLT.getValueAt(j,TB5_TRMDT).toString()))+"',";
							M_strSQLQRY += "'"+0+"',";
							M_strSQLQRY += "'"+1+"',";
					
							M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
							M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"')";
					
									 
						//	+setINSSTR("IV2_MKTTP",strMKTTP,"C")
						//	+setINSSTR("IV2_GINNO",L_GINNO,"C")
						//	+setINSSTR("IV2_SRLNO",tblMBPLT.getValueAt(j,TB5_SRLNO).toString(),"C")
						//	+setINSSTR("IV2_LOTNO",tblMBPLT.getValueAt(j,TB5_LOTNO).toString(),"C")
						//	+setINSSTR("IV2_CNTDS",L_CNTDS,"C")
						//	+setINSSTR("IV2_PLTQT",tblMBPLT.getValueAt(j,TB5_PLTQT).toString(),"N")
						//	+setINSSTR("IV2_TRMDT",cc_dattm.occ_dattm.setDBSDT(tblMBPLT.getValueAt(j,TB5_TRMDT).toString()),"N")
						//	+setINSSTR("IV2_TRNFL","0","C")
						//	+setINSSTR("IV2_STSFL","1","C")
						//	+setINSSTR("IV2_LUPDT",cc_dattm.occ_dattm.setDBSDT(cl_dat.ocl_dat.M_LOGDAT),"N")
						//	+"'"+cl_dat.ocl_dat.M_USUSRCD+"')";
						}
						else
						{
							M_strSQLQRY = "update fg_ivtr2 set ";
							M_strSQLQRY +=" IV2_LOTNO='"+tblMBPLT.getValueAt(j,TB5_LOTNO).toString()+"',";
							
							M_strSQLQRY +=" IV2_CNTDS='"+L_CNTDS+"',";
							M_strSQLQRY +=" IV2_PLTQT="+tblMBPLT.getValueAt(j,TB5_PLTQT).toString()+",";
							M_strSQLQRY +=" IV2_TRMDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblMBPLT.getValueAt(j,TB5_TRMDT).toString()))+"',";
						
							M_strSQLQRY += " IV2_STSFL='1',";
							M_strSQLQRY += " IV2_TRNFL='0',";
							M_strSQLQRY += " IV2_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY += " IV2_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
					
							M_strSQLQRY += "  where "+ strWHERE;

						//	+setUPDSTR("IV2_LOTNO",tblMBPLT.getValueAt(j,TB5_LOTNO).toString(),"C")
						//	+setUPDSTR("IV2_CNTDS",L_CNTDS,"C")
						//	+setUPDSTR("IV2_PLTQT",tblMBPLT.getValueAt(j,TB5_PLTQT).toString(),"N")
						//	+setUPDSTR("IV2_TRMDT",cc_dattm.occ_dattm.setDBSDT(tblMBPLT.getValueAt(j,TB5_TRMDT).toString()),"N")
						//	+setUPDSTR("IV2_TRNFL","0","C")
						//	+setUPDSTR("IV2_STSFL","1","C")
						//	+setUPDSTR("IV2_LUPDT",cc_dattm.occ_dattm.setDBSDT(cl_dat.ocl_dat.M_LOGDAT),"N")
						//	+"IV2_LUSBY ='"+cl_dat.ocl_dat.M_USUSRCD+"' where "+strWHERE;
						}
						//System.out.println("getMBPLT = "+ M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"");
						//cl_dat.ocl_dat.exeDBCMT("SP","ACT","");
						M_strSQLQRY = "delete from fg_ivtr2 where IV2_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv2_pltqt=0";
						cl_dat.exeSQLUPD(M_strSQLQRY,"");
						//cl_dat.ocl_dat.exeDBCMT("SP","ACT","");
						if(cl_dat.exeDBCMT("Saved"))
						{
							setMSG("Data saved successfully",'N');
						}
						setMSG("Saved Successfully",'N');
					}
				}
			}
			//this.setCursor(curDFSTS);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		
		catch (Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG("Error in getMBPLT : "+L_EX,'E');
		}
	}

	
	/**
	 */
	private void updSTMST1()      //Updating Allocated Qty. within Stock Master
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				//lotROWCT = tblLTDTL.getRowCount();
				//System.out.println("in UPDSTMST1");
				for(int i=0;i<tblLTDTL.getRowCount();i++)
				{			
					if(getRECVAL(i))
					{	
						//System.out.println(" Value of i = "+i);
						//System.out.println("updSTMST1 : "+ i +" = "+strSTKQT);
						//if(LM_INDISS)
							//strPRDTP = getIDPDT();
						strALOQT = getALOQT();
						//System.out.println(" strALOQT = "+strALOQT);
						dblDSTQT = 0;
						dblDALQT = 0;
						dblDUPQT = 0;
						//System.out.println("updSTMST1 : LM_STKQT "+LM_STKQT);
						//System.out.println("updSTMST1 : strALOQT "+strALOQT);
						if(strUPDFL.equals("1"))              //condition defined only for Allocation
						{
							//System.out.println("strSTKQT : "+ i +" = "+strSTKQT);
							dblDSTQT = Double.parseDouble(new BigDecimal(strSTKQT).toString());
							//System.out.println("strSTKQT After : "+ i +" = "+strSTKQT);
							
							//System.out.println("updSTMST1 : "+ i +" = "+dblDSTQT);
							
							
							dblDALQT = Double.parseDouble(new BigDecimal(strALOQT).toString());
							
							//System.out.println("updSTMST1 : "+ i +" = "+dblDALQT);
							
							dblDUPQT = dblDSTQT - dblDALQT;
							//System.out.println(" dblDUPQT = "+dblDUPQT);
							LM_UPDQT = setNumberFormat(dblDUPQT,3);
							strSTKQT = "0.000";
							if(!LM_INDISS)
							{
								setMSG("Updating Invoice Transaction allocation date.",'N');
								//System.out.println("Updating Invoice Transaction allocation date.");
								addIVTADT();
							}
						}
						else if(strUPDFL.equals("2") && strISSEX.equals("1"))    //condition defined for Authorization
						{
							dblDSTQT = Double.parseDouble("-1");		//with record present in FG_ISTRN
							dblDALQT = Double.parseDouble(new BigDecimal(strALOQT).toString());
							dblDUPQT = dblDSTQT * dblDALQT;
							LM_UPDQT = setNumberFormat(dblDUPQT,3);
							strSTKQT = setNumberFormat(Double.parseDouble(strSTKQT),3);
						}
						else if(strUPDFL.equals("2") && strISSEX.equals("0"))		 //condition defined only for Authorization
						{
							LM_UPDQT = "0.000";							//with record not present in FG_ISTRN
							strSTKQT = setNumberFormat(Double.parseDouble(strSTKQT),3);
							if(!LM_INDISS)
							{
								setMSG("Updating Invoice Transaction allocation date.",'N');
                                //addIVTADT();
							}
						}
						//System.out.println("updSTMST1 : strSTKQT "+strSTKQT);
						//System.out.println("updSTMST1 : strALOQT "+strALOQT);
						//System.out.println("updSTMST1 : LM_UPDQT : "+LM_UPDQT);
						//System.out.println("updSTMST1 : strSTKQT : "+strSTKQT);
						//System.out.println("updSTMST1 : LM_UPDFL : "+LM_UPDFL);
						
						//updSTMQT();
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"updSTMST1");					   
			}
		}
	}

	/**
	 * @return void
	 * Modifies Allocated Quantity into Stock Master i.e FG_STMST i.e adds the JTable Qty.
	 * to the allocated Qty. of the Stock Master Table.
	*/
	/*private void updSTMQT()
	{ 
		try
		{
			M_strSQLQRY = "Update fg_stmst set ";
			if(strTDSFL.equals("U"))
			{
				M_strSQLQRY += " ST_UCLQT = ST_UCLQT - "+strSTKQT+",";
			}
			else
			{
				M_strSQLQRY += " ST_ALOQT = ST_ALOQT + "+LM_UPDQT+",";
				M_strSQLQRY += " ST_STKQT = ST_STKQT - "+strSTKQT+",";
			}
			M_strSQLQRY += " ST_TRNFL = '0',";
			M_strSQLQRY += " ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";	
			M_strSQLQRY += " ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
			M_strSQLQRY += " where ST_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and ST_PRDTP = '"+strPRDTP+"'";
			M_strSQLQRY += " and ST_LOTNO = '"+strLOTNO+"'";
			M_strSQLQRY += " and ST_RCLNO = '"+strRCLNO+"'";
			M_strSQLQRY += " and ST_PKGTP = '"+strPKGTP+"'";
			M_strSQLQRY += " and ST_MNLCD = '"+strMNLCD+"'";
			
			
			
		//	cl_dat.ocl_dat.M_STRSQL = cl_dat.ocl_dat.M_STLSQL;
		//	System.out.println("updSTMQT : strALOQT "+strALOQT);
		//	System.out.println("updSTMQT : LM_UPDQT : "+LM_UPDQT);
			//System.out.println("updSTMQT : "+M_strSQLQRY);
			
			
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
						
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updSTMQT");
		}
	}
	*/
	
	
	private void addIVTADT()                   //Inserting or Modifying into Invoice Transaction i.e MR_IVTRN
	{ 
		if(cl_dat.M_flgLCUPD_pbst)
		{	
			try
			{
				//System.out.println("in Updating Invoice Transaction allocation date.");
				//LM_STRSQL = "Select count(*) from mr_ivtrn where"; 
				M_strSQLQRY = " IVT_MKTTP = '"+strMKTTP+"'";
				M_strSQLQRY += " and IVT_LADNO = '"+strISSNO+"'";
				M_strSQLQRY += " and IVT_PRDCD = '"+strPRDCD+"'";
				M_strSQLQRY += " and IVT_PKGTP = '"+strPKGTP+"'";
                //System.out.println("Locate in IVTRN : "+LM_STRSQL);
				//if(cl_dat.ocl_dat.getRECCNT("SP","ACT",LM_STRSQL) > 0)
				flgCHK_EXIST =chkEXIST("mr_ivtrn",M_strSQLQRY);  
				if(flgCHK_EXIST)
				{
                    //updIVTADT();
					M_strSQLQRY = "Update mr_ivtrn set";
					M_strSQLQRY += " IVT_ALODT = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
					M_strSQLQRY += " IVT_TRNFL = '0'";
					M_strSQLQRY += " where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+strMKTTP+"'";
					M_strSQLQRY += " and IVT_LADNO = '"+strISSNO+"'";
					M_strSQLQRY += " and IVT_PRDCD = '"+strPRDCD+"'";
					M_strSQLQRY += " and IVT_PKGTP = '"+strPKGTP+"'";
					//System.out.println(" addIVTADT =  "+M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
				else
				{
					setMSG("Record does not exist in MR_IVTRN /"+strMKTTP + strISSNO + strPRDCD + strPKGTP,'E');
					cl_dat.M_flgLCUPD_pbst = false;
				}
			}
			catch(Exception L_EX)
			{
		         setMSG(L_EX,"addIVTADT");                                     
			}
		}
	}

	private void addIVTRN()      //Inserting or Modifying into Invoice Transaction i.e MR_IVTRN
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{	
			try
			{
				//M_strSQLQRY = "Select count(*) from mr_ivtrn where"; 
				M_strSQLQRY = " IVT_MKTTP = '"+strMKTTP+"'";
				M_strSQLQRY += " and IVT_LADNO = '"+strISSNO+"'";
				M_strSQLQRY += " and IVT_PRDCD = '"+strPRDCD+"'";
				M_strSQLQRY += " and IVT_PKGTP = '"+strPKGTP+"'";
				flgCHK_EXIST =chkEXIST("MR_IVTRN",M_strSQLQRY);   
				//if(cl_dat.ocl_dat.getRECCNT("SP","ACT",M_strSQLQRY) > 0)
				if(flgCHK_EXIST)
				{
					//updIVTRN();
					
					getLTQTPK();
					M_strSQLQRY = "Update mr_ivtrn set";
					M_strSQLQRY += " IVT_LADQT = "+setNumberFormat(LM_LTGRQT,3)+",";
					M_strSQLQRY+= " IVT_LODDT = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
					//M_strSQLQRY += " IVT_INVQT = "+setFMT(String.valueOf(LM_LTGRQT),3)+",";
					//M_strSQLQRY += " IVT_INVPK = "+String.valueOf(LM_LTGRPK)+",";
					M_strSQLQRY += " IVT_CNTDS = '"+strCNTDS+"',";
					M_strSQLQRY += " IVT_DSTDS = '"+strDESTN+"',";
					M_strSQLQRY += " IVT_TSLNO = '"+strTSLNO+"',";
					M_strSQLQRY += " IVT_RSLNO = '"+strRSLNO+"',";
					// Stsus (IVT_STSFL) "2" is for LAs loaded and waiting for Invoice Clearance
					// In case of Captive Consumption  Invoice Clearance stage is skipped
					// (There is no gate-in (Security No./Lorry No.) for this category)
					//M_strSQLQRY += " IVT_STSFL = '"+(LM_MKTTP.equals("03") ? "L" : "2")+"',";
					M_strSQLQRY += " IVT_TRNFL = '0',";
					M_strSQLQRY += " IVT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += " IVT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
					M_strSQLQRY += " where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+strMKTTP+"'";
					M_strSQLQRY += " and IVT_LADNO = '"+strISSNO+"'";
					M_strSQLQRY += " and IVT_PRDCD = '"+strPRDCD+"'";
					M_strSQLQRY += " and IVT_PKGTP = '"+strPKGTP+"'";
					//cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
					//System.out.println(" updIVTRN = "+ M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
				else
				{
					setMSG("Record does not exist in MR_IVTRN /"+strMKTTP + strISSNO + strPRDCD + strPKGTP,'E');
					cl_dat.M_flgLCUPD_pbst = false;
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"addIVTRN");					   
			}
		}
	}
	
	
	
	
	/**
	 * @return void
	 * calculates & fetches Lot Jtable Qty. & Pkg.
	*/
	private void getLTQTPK()
	{ 
		try
		{
			LM_LTGRQT = 0;
			LM_LTGRPK = 0;
			String L_strLOTQT="";
			String L_strLOTPK="";
			double L_dblLOTQT=0;
			int L_dblLOTPK=0;
			for(int i = 0;i<tblLTDTL.getRowCount();i++)
			{
				if(tblLTDTL.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
				{	
					L_strLOTQT  = tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim();
					L_dblLOTQT  = Double.parseDouble(L_strLOTQT);
					L_dblLOTQT  = L_dblLOTQT + LM_LTGRQT;
					LM_LTGRQT   = L_dblLOTQT;
					L_strLOTPK  = tblLTDTL.getValueAt(i,TB2_ISSPK).toString().trim();
					L_dblLOTPK  = Integer.parseInt(L_strLOTPK);
					L_dblLOTPK  = L_dblLOTPK + LM_LTGRPK;
					LM_LTGRPK   = L_dblLOTPK;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLTQTPK");
		}
	}
	
	
	
	/*private void updIVTADT()    //Modifying Invoice Transaction i.e MR_IVTRN
	{
		try
		{
			M_strSQLQRY = "Update mr_ivtrn set";
            M_strSQLQRY += " IVT_ALODT = "+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
			M_strSQLQRY += " IVT_TRNFL = '0'";
			M_strSQLQRY += " where IVT_MKTTP = '"+strMKTTP+"'";
			M_strSQLQRY += " and IVT_LADNO = '"+strISSNO+"'";
			M_strSQLQRY += " and IVT_PRDCD = '"+strPRDCD+"'";
			M_strSQLQRY += " and IVT_PKGTP = '"+strPKGTP+"'";
            
            //System.out.println(cl_dat.ocl_dat.M_STLSQL);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updIVTADT");
		}
	}
*/
	
	
	
	/**
	 * @return void
	 *Fetching Allocated Qty. from Issue Transaction i.e FG_ISTRN
	 */
	private String getALOQT()
	{ 
		//System.out.println(" getALOQT +getALOQT");
		String L_strALOQT = "0.000";
		strISSEX = "";
		try
		{
			strISSNO = getSUBSTR(txtISSNO.getText(),0,8);
			M_strSQLQRY = "Select IST_ISSQT from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strISSTP+"'";
			M_strSQLQRY += " and IST_ISSNO='"+strISSNO+"'  and IST_PRDCD='"+strPRDCD+"' and IST_PRDTP='"+strPRDTP+"'";
			M_strSQLQRY += " and IST_LOTNO='"+strLOTNO+"' and IST_RCLNO='"+strRCLNO+"' and IST_PKGTP='"+strPKGTP+"'";
			M_strSQLQRY += " and IST_MNLCD='"+strMNLCD+"' and IST_STSFL = '1'";
			
			//System.out.println(" getALOQT = "+M_strSQLQRY);
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
				L_strALOQT = M_rstRSSET.getString("IST_ISSQT");
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			if(L_strALOQT.equals("0.000") || L_strALOQT == null)
			{
				strISSEX = "0";
				return "0.000";
			}
			else
			{
				strISSEX = "1";
				return setNumberFormat(Double.parseDouble(L_strALOQT),3);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALOQT");
		}
		return L_strALOQT;
	}

	
	
	
	
	
	
	
	
	/** Input Verifier
 */	
	private class INPVF extends InputVerifier
	{
		public boolean verify (JComponent input)
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input==txtISSTP)
				{
					strISSTP=txtISSTP.getText().trim();
					if(exeISSTP())
					{
						//System.out.println("IN input==txtISSTP ");
						return true;
					}
					else
						return false;
				}
				if(input==txtISSDT)
				{
					if(!txtISSDT.getText().equals(strREFDT))
					{
					
						setMSG("Please enter Despatch Date as "+strREFDT,'E');
						return false;
					}
					
					if(M_fmtLCDAT.parse(txtISSDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Invalid Date ...",'E');
						return false;
					}
				}
			}
			catch (Exception e)
			{
				setMSG(e,"INPVF");
				return false;
			}
			return true;
		}
	}
	
	/**
	 *  Table Input Verifier Class for Validation
	*/
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				if(getSource()==tblGRDTL)
				{
					if(P_intCOLID==TB1_PRDCD)
					{ 
						if((((JTextField)tblGRDTL.cmpEDITR[TB1_PRDCD]).getText().length())!=0)
						{
							//for(int i = 0;i<=P_intROWID+5;i++)
							for(int i = 0;i<=tblGRDTL.getRowCount();i++)
							{
							
								if(tblGRDTL.getValueAt(i,TB1_PRDCD).toString().length()!=10)
									break;
								if(i==P_intROWID)
									tblGRDTL.setValueAt( new Boolean(true),i,TB1_CHKFL);
								else
									tblGRDTL.setValueAt( new Boolean(false),i,TB1_CHKFL);
								//tblGRDTL.setValueAt(new Boolean(true),P_intROWID,TB1_CHKFL);
							}
												
							strPRDCD=((JTextField)tblGRDTL.cmpEDITR[TB1_PRDCD]).getText();
							hstSTCHK.clear();
							getLOTREC();	
							chkSRTRN();
							chkAUTLS();
							//System.out.println(" flgRESFL = "+flgRESFL);
							//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							//txtTDSFL.setText("N");
							//tblLTDTL.setRowSelectionInterval(tblLTDTL.getSelectedRow(),tblLTDTL.getSelectedRow());		
							//tblLTDTL.setColumnSelectionInterval(TB2_TDSFL,TB2_TDSFL);		
							//tblLTDTL.editCellAt(tblLTDTL.getSelectedRow(),TB2_TDSFL);
							//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							//	txtTDSFL.setText("N");
							//tblLTDTL.cmpEDITR[TB2_TDSFL].requestFocus();
						}
					}
				}
				
				if(getSource()==tblLTDTL)
				{
					strPRDTP = getPRDTP();
					if(P_intCOLID==TB2_ISSQT)
					{
						//System.out.println("IN TB2_ISSQT");
						double L_dblTLTQT=0;
						
						hstLTDTL.clear();
						String L_strLOTNO="",L_strRCLNO="",L_strMNLOC="",L_strCHQDT="";
						for(int i=0;i<tblLTDTL.getRowCount();i++)
						{
							if(tblLTDTL.getValueAt(i,TB2_ISSQT).toString().length()==0)
								if(tblLTDTL.getValueAt(i+1,TB2_ISSQT).toString().length()==0)
									if(tblLTDTL.getValueAt(i+2,TB2_ISSQT).toString().length()==0)
										break;
							
							
							L_strRCLNO=tblLTDTL.getValueAt(i,TB2_RCLNO).toString();
							L_strMNLOC=tblLTDTL.getValueAt(i,TB2_MNLCD).toString();
							L_strLOTNO=tblLTDTL.getValueAt(i,TB2_LOTNO).toString()+L_strRCLNO+L_strMNLOC;
							if(i==P_intROWID)
								continue;
							else
								hstLTDTL.put(L_strLOTNO,"");
						}
						L_strLOTNO="";
						L_strRCLNO="";
						L_strMNLOC="";
						if(((JTextField)tblLTDTL.cmpEDITR[TB2_ISSQT]).getText().length()>0)
						{
							//System.out.println("ddddddd     "+ ((JTextField)tblLTDTL.cmpEDITR[TB2_ISSQT]).getText().length());
							L_strRCLNO = tblLTDTL.getValueAt(P_intROWID,TB2_RCLNO).toString();
							L_strMNLOC = tblLTDTL.getValueAt(P_intROWID,TB2_MNLCD).toString();
							L_strLOTNO = tblLTDTL.getValueAt(P_intROWID,TB2_LOTNO).toString();
							L_strLOTNO=L_strLOTNO+L_strRCLNO+L_strMNLOC;
						}
						if(hstLTDTL.containsKey(L_strLOTNO))
						{
							setMSG("'Duplicate Entry' Please Check The  Lot Number, Rcl Number And Location  ",'E');
							return false;
						}
						
						strLOTNO = tblLTDTL.getValueAt(P_intROWID,TB2_LOTNO).toString().trim();
						strRCLNO = tblLTDTL.getValueAt(P_intROWID,TB2_RCLNO).toString().trim();
						//System.out.println("IN TB2_ISSQT2");
						strMNLCD = tblLTDTL.getValueAt(P_intROWID,TB2_MNLCD).toString().trim();
						strPKGCT = tblLTDTL.getValueAt(P_intROWID,TB2_PKGCT).toString().trim();
						//System.out.println("IN TB2_ISSQT4");
						strPKGTP = tblLTDTL.getValueAt(P_intROWID,TB2_PKGTP).toString().trim();
						//System.out.println("IN TB2_ISSQT5");
						strLOTQT = ((JTextField)tblLTDTL.cmpEDITR[TB2_ISSQT]).getText();
						if(strLOTQT.length()>0)
							dblLOTQT=Double.parseDouble(strLOTQT);
						//System.out.println("IN TB2_ISSQT6");
						//System.out.println(" strGRDQT = "+strGRDQT);
						double L_dblGRDQT=Double.parseDouble(strGRDQT);
						//System.out.println(" L_dblGRDQT "+L_dblGRDQT);
						
						for(int i = 0;i<=P_intROWID+10;i++)
						{
							L_dblTLTQT +=Double.parseDouble(nvlSTRVL(tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim(),"0"));	
						}
						//System.out.println(" L_dblTLTQT = "+L_dblTLTQT);
						if(Double.parseDouble(setNumberFormat(L_dblTLTQT,3))>L_dblGRDQT)
						{
							setMSG("Enter Total Lot Quantity is more by "+(L_dblTLTQT-L_dblGRDQT)+" MT than Grade Qty.",'E');
							return false;
						}
						if(dblLOTQT>L_dblGRDQT)
						{
							setMSG("Enter Lot Quantity is more then Required Grade Quantity",'E');
							return false;
						}
						
						else if(!chkLOTQT())
						{
							return false;
						}
						else
						{
							double 	L_dblNCSVL=0.0;
							M_strSQLQRY =  "Select CMT_NCSVL from CO_CDTRN where CMT_CGMTP = 'SYS'" ;
							M_strSQLQRY +=  " AND CMT_CGSTP =  'FGXXPKG' ";
							M_strSQLQRY +=  " AND CMT_CODCD = " + "'" + strPKGTP + "'";
							//System.out.println("M_strSQLQRY  "+M_strSQLQRY);
							ResultSet L_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(L_rstRSSET.next() && L_rstRSSET!=null)
							{
								//LM_NCSVL = cl_dat.ocl_dat.getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",LM_PKGTP);
								L_dblNCSVL = Double.parseDouble(nvlSTRVL(L_rstRSSET.getString("CMT_NCSVL"),"0.001"));
							}
							
							double L_dblPKGNO = dblLOTQT/L_dblNCSVL;
							L_dblPKGNO = Float.parseFloat(setNumberFormat(L_dblPKGNO,3));
							int L_intPKGNO = new Float(L_dblPKGNO).intValue();
							if(L_intPKGNO != L_dblPKGNO)
							{
								setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblNCSVL,3),'E'); 
								return false;
							}
							else
							{
								String L_strISSPK =setNumberFormat((dblLOTQT/L_dblNCSVL),0);
								//LM_LISPK = Math.round(LM_DISPK);
								tblLTDTL.setValueAt(L_strISSPK,P_intROWID,TB2_ISSPK);
							
								getDSPPK();
								getDSPQT();
							}
							//LM_ISSPK = String.valueOf(LM_LISPK);
						}
					}
					if(P_intCOLID==TB2_LOTNO)
					{
						tblLTDTL.setValueAt(new Boolean(true),P_intROWID,TB1_CHKFL);	
					}
					if(P_intCOLID==TB2_ISSPK)
					{
						String L_strTDSFL = tblLTDTL.getValueAt(P_intROWID,TB2_TDSFL).toString().trim();
						//String L_strPRDCD = tblGRDTL.getValueAt(P_intROWID,TB2_PRDCD).toString().trim();
						if(tblLTDTL.getValueAt(P_intROWID+1,TB2_TDSFL).toString().trim().length()==0)
							tblLTDTL.setValueAt(L_strTDSFL, P_intROWID+1,TB2_TDSFL);

					}
					//if(P_intCOLID==TB2_RESQT)
					//{
					//	String L_strTDSFL = tblLTDTL.getValueAt(P_intROWID,TB2_TDSFL).toString().trim();
					//	//String L_strPRDCD = tblGRDTL.getValueAt(P_intROWID,TB2_PRDCD).toString().trim();
					//	if(tblLTDTL.getValueAt(P_intROWID+1,TB2_TDSFL).toString().trim().length()==0)
					//		tblLTDTL.setValueAt(L_strTDSFL, P_intROWID+1,TB2_TDSFL);

					//}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"TBLINPVF");
			}
			return true;
		}
	}
	
	/** Checking whether party has Authorised (suitable lots)
	 *  marked in LT_AUTLS of Lot Master
	 */
	public void chkAUTLS()
	{
		try
		{
			flgAUTFL = (cl_dat.M_hstMKTCD_pbst.containsKey("SYSMRXXLAU"+strGRPCD)) ? true : false;
			chkAUTFL.setVisible((flgAUTFL ? true:false));
			chkAUTFL.setSelected((flgAUTFL ? true:false));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkSRTRN");
		}
	}
	
	
	
	/** Checking whether party has Reserved Lots defined
	 *  Which are existing in Stock Record
	 */
	public void chkSRTRN()
	{
		try
		{
			//M_strSQLQRY="Select SR_PRDCD,SR_GRPCD,(sum(isnull(SR_RESQT,0))-sum(isnull(SR_RDSQT,0)))L_RESQT from MR_SRTRN where SR_PRDCD='"+strPRDCD+"' and  SR_GRPCD='"+strBYRCD+"' AND (sum(isnull(SR_RESQT,0)-sum(isnull(SR_RDSQT,0)))>0 and isnull(RS_STSFL,'')<>'X'";
			//M_strSQLQRY="Select * from MR_SRTRN where SR_PRDCD='"+strPRDCD+"' and  SR_GRPCD='"+strBYRCD+"' AND sum(isnull(SR_RESQT,0)-isnull(SR_RDSQT,0))>0 and isnull(RS_STSFL,'')<>'X'";
			//M_strSQLQRY +="  Group by SR_PRDCD,SR_GRPCD";
			
			M_strSQLQRY =" Select sum(isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)-isnull(SR_RALQT,0)) from FG_SRTRN where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_PRDCD='"+strPRDCD+"' and  SR_GRPCD='"+strBYRCD+"' and isnull(SR_STSFL,'')<>'X' and SR_ENDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' having  sum(isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)-isnull(SR_RALQT,0))>0 "; 
			
			//System.out.println("chkSRTRN = "+M_strSQLQRY);                                                   //sum(isnull(LT_RESQT,0)-isnull(LT_RDSQT,0))   
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			strRESFL="0";
			flgRESFL=false;
			if(M_rstRSSET.next()&& M_rstRSSET!=null)
				flgRESFL=true;
			strRESFL = (flgRESFL ? "1" : "0");
			chkRESFL.setEnabled(false);
			chkRESEV.setVisible(true);
			chkRESEV.setEnabled(true);
			chkRESFL.setVisible((flgRESFL ? true:false));
			chkRESFL.setSelected((flgRESFL ? true:false));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkSRTRN");
		}
	}
	
	private boolean chkLOTQT()    //validates Lot Table Qty.
	{  
		try
		{
			if(LM_INDISS)
			{
				//LM_PRDTP = getIDPDT();
			}
			else
				strPRDTP = getPRDTP();
			if(LM_INDISS)
			{
				
			}
			else
			{
				if(exeVDDBQT())
				{
					vldLOTQT();
						//return true;
				}
				else
				{
					setMSG("Exceeds Stock Qty.",'E'); 
					tblLTDTL.setColumnSelectionInterval(TB2_ISSPK,TB2_ISSQT);
					return false;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkLOTQT");
		}
		return true;
	}
	
	
	
	private void vldLOTQT()      //validates Lot Table Qty. for Direct Issues i.e Issue Type 10,20 
	{
		if(exeLOTQT())
		{
		//	LM_LOTTBL.setValueAt(LM_ISSPK,LM_LOTTBL.getSelectedRow(),TB2_ISSPK);
			//getDSPPK();
		//	LM_TDSFL = LM_LOTTBL.getValueAt(LM_LOTTBL.getSelectedRow(),TB2_TDSFL).toString().trim();
		//	LM_LOTTBL.setValueAt(LM_TDSFL,LM_LOTTBL.getSelectedRow()+1,TB2_TDSFL);
		//	LM_DSPPK = LM_LOTTBL.getValueAt(LM_LOTTBL.getSelectedRow(),TB2_PKGTP).toString().trim();
		//	LM_LOTTBL.setValueAt(LM_DSPPK,LM_LOTTBL.getSelectedRow()+1,TB2_PKGTP);
		//	LM_LOTTBL.setColumnSelectionInterval(TB2_TDSFL,TB2_LOTNO);
		//	LM_LOTTBL.setRowSelectionInterval(LM_LOTTBL.getSelectedRow(),LM_LOTTBL.getRowCount());
			setMSG("Enter Status either Targeted(T) or Normal(N) or Party(P)",'N');
		}
		else
		{
			setMSG("Entered Quantity Exceeds Requested Qty.",'E'); 
			
			tblLTDTL.setColumnSelectionInterval(TB2_ISSPK,TB2_ISSQT);
		}
	}
	
	/**
	 * @return boolean
	 *  Validates Total Lot JTable Qty. with the Grade JTable Requested Qty.
	 * Grade JTable Qty >= Total Lot JTable Qty.
	 */
	private boolean exeLOTQT()
	{ 
		try
		{
			double LM_DSALQ=0;
			strPKGTP = tblLTDTL.getValueAt(tblLTDTL.getSelectedRow(),TB2_PKGTP).toString().trim();
			//System.out.println(" strPKGTP = "+strPKGTP);
			//grdROWCT = LM_GRDTBL.getRowCount();
			for(int i = 0;i<tblGRDTL.getRowCount();i++)
			{
				if(tblGRDTL.getValueAt(i,TB1_CHKFL).equals(new Boolean(true)))
					strGRDQT = tblGRDTL.getValueAt(i,TB1_REQQT).toString().trim();	
			}
			double LM_DGDQT = Double.parseDouble(setNumberFormat(Double.parseDouble(strGRDQT),3));
			double LM_DDSQT = 0;
			for(int i = 0;i < tblLTDTL.getRowCount();i++)
			{
				 String L_ALOQT = nvlSTRVL(tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim(),"0.000");
				 LM_DSALQ = Double.parseDouble(L_ALOQT);
				 LM_DSALQ = LM_DSALQ + LM_DDSQT;
				 LM_DDSQT = LM_DSALQ;
			}
			LM_DDSQT = Double.parseDouble(setNumberFormat(LM_DDSQT,3));
			if(LM_DGDQT >= LM_DDSQT)
			{
			//	getDSPQT();
				if(strPKGTP.equals("99"))
					strISSPK = "1";
               // else
                 //   strISSPK= getISSPK();
				return true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeLOTQT");
		}
		return false;
	}
	
	
	
	private void getDSPPK()       //calculates Packages & displays it.
	{
		//lotROWCT = tblLTDTL.getRowCount();
		int L_intPKGNO = 0;
		int L_intLOTPK = 0;
		String L_strLTPKG = "";
		String L_strDSPPK = "0";
		for(int i = 0;i<tblLTDTL.getRowCount();i++)
		{  
			if(tblLTDTL.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
			{	
				L_strLTPKG = nvlSTRVL(tblLTDTL.getValueAt(i,TB2_ISSPK).toString(),"0").trim();
				L_intLOTPK = Integer.parseInt(L_strLTPKG);
				L_intLOTPK = L_intLOTPK + L_intPKGNO;
				L_intPKGNO = L_intLOTPK;
				L_strDSPPK = String.valueOf(L_intPKGNO);
				//System.out.println(" i  "+ i +" = "+ L_strDSPPK);
			}
		}
		lblISSPK.setText(L_strDSPPK);
	}
	
	
	/**
	 * @return void
	 * Calculates the Total Qty. of Lot JTable & displays it.
	 */
	private void getDSPQT()
	{  
		//lotROWCT = LM_LOTTBL.getRowCount();
		double LM_DDPVR = 0;
		String  LM_DSPQT = "0.000";
		String L_strLTQTY="";
		double LM_DLTQT=0;
		for(int i = 0;i<tblLTDTL.getRowCount();i++)
		{  
			if(tblLTDTL.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
			{	
				L_strLTQTY = tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim();
				LM_DLTQT = Double.parseDouble(L_strLTQTY);
				LM_DLTQT = LM_DLTQT + LM_DDPVR;
				LM_DDPVR = LM_DLTQT;
				//LM_DSPQT = String.valueOf(LM_DDPVR);
			}
		}
		lblISQTY.setText(setNumberFormat(LM_DDPVR,3));
	}
	
	
/**
 * @return boolean
 * Validates Lot Table Qty. i.e LM_LOTQT with the
 * Retention Qty i.e strRETQT
 * Hold On Qty i.e strHLDQT
 * Stock Qty i.e L_strSTKQT
 * LM_LOTQT <= (L_strSTKQT - (strRETQT + strHLDQT))
 * if(L_strSTKQT - (strRETQT + strHLDQT)) < 0, then Stock Qty. as well as Packages
 * for that particular entry is made zero.
*/
	private boolean exeVDDBQT()
	{  
		try
		{
			dblRTHQT = getRTHQT(strPRDTP,strLOTNO,strRCLNO); //getting Retention & Hold Qty for that particular Lot    //a+b
			
			double dblOLDISS = 0.000;
			if(hstISTRN.containsKey(strLOTNO+strRCLNO+strMNLCD+strPKGTP))
			   dblOLDISS = Double.parseDouble(hstISTRN.get(strLOTNO+strRCLNO+strMNLCD+strPKGTP).toString());
			//System.out.println("---- dblOLDISS : "+dblOLDISS+" ----");
			dblTOTQT = getTOTQT(strPRDTP,strLOTNO,strRCLNO);
			String L_strTOTQT = setNumberFormat(dblTOTQT,3);   //d
			double L_dblTOTQT=Double.parseDouble(L_strTOTQT);
			dblTOTQT += dblOLDISS;			
			dblTOTQT1 += dblOLDISS;			
			
			
			String L_strSTKQT = setNumberFormat(Double.parseDouble(getSTKQT())+dblOLDISS,3); //c
			
			double L_dblSTKQT = Double.parseDouble(L_strSTKQT);
			
			//System.out.println("L_dblSTKQT = "+L_dblSTKQT);
			
			double L_dblRESQT=getRESQT(strPRDTP, strLOTNO, strRCLNO);      //g
			
			double L_dblPRRES=getPTRES();                            //h
			
			//System.out.println("L_dblRESQT = "+L_dblRESQT);
			//System.out.println("L_dblPRRES = "+L_dblPRRES);
			System.out.println("(Includes previous issue   (dblOLDISS)  = "+dblOLDISS+")");
			System.out.println("Retention And Hold Quantity (a+b) =  "+dblRTHQT);
			System.out.println("Available Quantity Against Specific lot (d , L_dblTOTQT) = "+L_dblTOTQT);
			System.out.println("Available Quantity Against Specific lot,Location  (C , L_dblSTKQT) = "+L_dblSTKQT);
			System.out.println("Reserved Quantity Against Specific lot  (G , L_dblRESQT) = "+L_dblRESQT);
			System.out.println("Reserved Quantity Against Specific lot & not same party (h, L_dblPRRES) ="+L_dblPRRES);
						
			
			
			double L_dblRUNQT = getRUNQT(strLOTNO,strRCLNO,strMNLCD);
			System.out.println("Total Run Quantity  L_dblRUNQT ("+strMNLCD+") = "+L_dblRUNQT);
			
			//System.out.println("L_dblTOTQT = "+L_dblTOTQT);
			System.out.println("Entered Lot Quantity (dblLOTQT)  = "+dblLOTQT);
			
			double L_dblREMST=L_dblTOTQT-dblLOTQT;
			
			System.out.println("Entered Lot Quantity (dblLOTQT)  = "+dblLOTQT);
			System.out.println("Qty.in Stock (dblTOTQT1)  = "+dblTOTQT1);
			System.out.println("Qty.Hold/Retn (dblRTHQT)  = "+dblRTHQT);
			// Qty.over reserved will be removed from verification blocked for other parties
			if(L_dblRESQT > L_dblTOTQT)
				L_dblPRRES = L_dblPRRES - (L_dblRESQT-L_dblTOTQT);
			double L_dblAVLQT=dblTOTQT1-dblRTHQT-L_dblPRRES;
			   
			System.out.println("Available Quantity  (L_dblAVLQT)  = "+L_dblAVLQT);
			
			System.out.println(" Available Quantity = "+L_dblAVLQT);
			System.out.println(" L_dblREMST = "+L_dblREMST);
			
			if(Double.parseDouble(setNumberFormat(dblLOTQT,3))>Double.parseDouble(setNumberFormat(L_dblAVLQT,3)))
			{
				JOptionPane.showMessageDialog(this,"Entered Qty.("+setNumberFormat(dblLOTQT,3)+") is Greater than Available Qty.("+setNumberFormat(L_dblAVLQT,3)+") \n Qty. Reserved by Other Parties : "+setNumberFormat(L_dblPRRES,3),"Error Entry Status",JOptionPane.ERROR_MESSAGE);
					return false;
			}
			if(Double.parseDouble(setNumberFormat(dblRTHQT,3))>0)
			{
				if(Double.parseDouble(setNumberFormat(L_dblREMST,3))<Double.parseDouble(setNumberFormat(dblRTHQT,3)))
				{
					JOptionPane.showMessageDialog(this,"Entered Lot No. has a retention of "+setNumberFormat(dblRTHQT,3)+" mts.","Error Entry Status",JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
			if(Double.parseDouble(setNumberFormat(L_dblTOTQT,3))<Double.parseDouble(setNumberFormat(dblLOTQT,3)))
			{
				JOptionPane.showMessageDialog(this,"Qt. available against the Lot is : "+setNumberFormat(L_dblTOTQT,3),"Error Entry Status",JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(Double.parseDouble(setNumberFormat(L_dblSTKQT,3))<Double.parseDouble(setNumberFormat(dblLOTQT,3)))
			{
				JOptionPane.showMessageDialog(this,"Stock available at this location is "+setNumberFormat(L_dblSTKQT,3),"Error Entry Status",JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(Double.parseDouble(setNumberFormat(dblGRDQT1,3))>Double.parseDouble(strGRDQT))
			{
				JOptionPane.showMessageDialog(this,"Total Entered Qty. ("+setNumberFormat(dblGRDQT1,3)+") Exceeds the Required Qty. ("+strGRDQT+")","Error Entry Status",JOptionPane.ERROR_MESSAGE);
				return false;
			}
								
				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeVDDBQT");
		}
		return true;
	}
	
	/**
	 */
	private double getRTHQT(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO)       //gets the Retention & Hold Qty.
	{
		try
		{
			double L_RTHQT = 0; 
			String L_strSQLQRY = "Select isnull(LT_RETQT,0) LT_RETQT,isnull(LT_HLDQT,0) LT_HLDQT from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP='"+LP_PRDTP+"'";
			L_strSQLQRY += " and LT_LOTNO='"+LP_LOTNO+"' and LT_RCLNO='"+LP_RCLNO+"' and upper(isnull(LT_RESFL,'X')) not in ('Q','H')";
			//System.out.println(" getRTHQT = "+ L_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(M_rstRSSET.next())
			{
				strRETQT = M_rstRSSET.getString("LT_RETQT");
				strHLDQT = M_rstRSSET.getString("LT_HLDQT");
				System.out.println("strRETQT = "+strRETQT);
				System.out.println("strHLDQT = "+strHLDQT);
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			if(strRETQT == null)
				strRETQT="0.000";
			if(strHLDQT == null)
				strHLDQT="0.000";
			double L_RETQT = Double.parseDouble(strRETQT);
			System.out.println("L_RETQT: "+L_RETQT);
			double L_HLDQT = Double.parseDouble(strHLDQT);
			System.out.println("L_HLDQT: "+L_HLDQT);	
			L_RTHQT = L_RETQT + L_HLDQT;
			return L_RTHQT;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRTHQT");
		}
		return 0;
	}
	
	/**
	 */
	private double getTOTQT(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO)           //gets the Retention & Hold Qty.
	{
		try
		{
			String L_SUMQT = ""; 
            //M_strSQLQRY = "Select sum(ST_STKQT-ST_ALOQT) L_SUMQT from FG_STMST where ST_PRDTP='"+LP_PRDTP+"'";
            String L_strSQLQRY = "Select sum(ST_STKQT) L_SUMQT, sum(ST_STKQT-ST_ALOQT) L_SUMQT1 from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP='"+LP_PRDTP+"'";
			L_strSQLQRY += " and ST_LOTNO='"+LP_LOTNO+"' and ST_RCLNO='"+LP_RCLNO+"'";
            M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			while(M_rstRSSET.next())
			{
				dblTOTQT = M_rstRSSET.getDouble("L_SUMQT");
                dblTOTQT1 = M_rstRSSET.getDouble("L_SUMQT1");
				//dblTOTQT += dblOLDISS;
				//dblTOTQT1 += dblOLDISS;
				//System.out.println("**** M_rstRSSET.getDouble(L_SUMQT) : "+M_rstRSSET.getDouble("L_SUMQT")+" ****");
				//System.out.println("**** M_rstRSSET.getDouble(L_SUMQT1) : "+M_rstRSSET.getDouble("L_SUMQT1")+" ****");
				//System.out.println("**** dblTOTQT : "+dblTOTQT+" ****");
				//System.out.println("**** dblTOTQT1 : "+dblTOTQT1+" ****");
				//System.out.println("**** dblOLDISS : "+dblOLDISS+" ****");
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			//System.out.println("fltTOTQT: "+fltTOTQT);
			return dblTOTQT;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTOTQT");
		}
		return 0;
	}
	
	
	/**
	 */
	private String getSTKQT()          //gets the Stock Qty.
	{
		String L_RTNSTR = "0";
		try
		{
			//LM_TDSFL = tblLTDTL.getValueAt(tblLTDTL.getSelectedRow(),TB2_TDSFL).toString().trim();
			if(LM_INDISS)
			{
				/*if(LM_TDSFL.equals("C"))
				{		
                    M_strSQLQRY = "Select sum(ST_STKQT-ST_ALOQT) from FG_STMST where ST_WRHTP = '"+LM_WRHTP+"' and ST_PRDTP = '"+LM_PRDTP+"'";
                    //M_strSQLQRY = "Select sum(ST_STKQT) from FG_STMST where ST_WRHTP = '"+LM_WRHTP+"' and ST_PRDTP = '"+LM_PRDTP+"'";
					M_strSQLQRY += " and ST_LOTNO = '"+LM_LOTNO+"' and ST_RCLNO = '"+LM_RCLNO+"' and ST_PKGTP = '"+LM_PKGTP+"' and ST_MNLCD = '"+LM_MNLCD+"'"; 
				}
				else if(LM_TDSFL.equals("U"))
				{		
					M_strSQLQRY = "Select sum(ST_UCLQT) from FG_STMST where ST_UCLQT > 0 and ST_WRHTP = '"+LM_WRHTP+"' and ST_PRDTP = '"+LM_PRDTP+"'";
					M_strSQLQRY += " and ST_LOTNO = '"+LM_LOTNO+"' and ST_RCLNO = '"+LM_RCLNO+"' and ST_PKGTP = '"+LM_PKGTP+"' and ST_MNLCD = '"+LM_MNLCD+"'"; 
				}
				*/
			}
			else
            {
				M_strSQLQRY = "Select sum(ST_STKQT-ST_ALOQT) from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"' and ST_PRDTP = '"+strPRDTP+"'";
				M_strSQLQRY += " and ST_LOTNO = '"+strLOTNO+"' and ST_RCLNO = '"+strRCLNO+"' and ST_PKGTP = '"+strPKGTP+"' and ST_MNLCD = '"+strMNLCD+"'"; 
            }
			System.out.println("M_strSQLQRY = "+M_strSQLQRY);
            M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				hstSTCHK.put(strPRDTP+strLOTNO+strRCLNO+strPKGTP+strMNLCD,nvlSTRVL(M_rstRSSET.getString(1),"0"));
				L_RTNSTR = nvlSTRVL(M_rstRSSET.getString(1),"0");
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTKQT");
		}
		//System.out.println("L_RTNSTR = "+L_RTNSTR);
		return L_RTNSTR;
	}
	
	public double  getRESQT(String LP_PRDTP, String LP_LOTNO, String LP_RCLNO)
	{
		double L_dblRESQT=0;
		try
		{
			String L_strRESQT="0";
			M_strSQLQRY = "Select sum(isnull(SR_RESQT,0)-isnull(SR_RALQT,0)-isnull(SR_RDSQT,0)) SR_RESQT from FG_SRTRN where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_PRDTP='"+LP_PRDTP+"'";
			M_strSQLQRY += "and SR_LOTNO='"+LP_LOTNO+"' and SR_RCLNO='"+LP_RCLNO+"' and upper(SR_STSFL) <> 'X' and SR_ENDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			//M_strSQLQRY = "Select sum(isnull(LT_RESQT,0)-isnull(LT_RALQT,0)-isnull(LT_RDSQT,0)) from PR_LTMST where LT_PRDTP='"+strPRDTP+"'";
			//M_strSQLQRY += "and LT_LOTNO='"+strLOTNO+"' and LT_RCLNO='"+strRCLNO+"' and upper(isnull(LT_RESFL,'X')) not in ('Q','H')";
			   
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_strRESQT = nvlSTRVL(M_rstRSSET.getString("SR_RESQT"),"0");
			}
			L_dblRESQT=Double.parseDouble(L_strRESQT);
		
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRESQT");
		
		}
		return L_dblRESQT;
	}
	
	public double getPTRES()
	{
		String L_strRESQT="0";
		double L_dblRESQT=0;
		try
		{
			M_strSQLQRY = "Select sum(isnull(SR_RESQT,0)-isnull(SR_RDSQT,0)-isnull(SR_RALQT,0)) from FG_SRTRN where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SR_PRDTP='"+strPRDTP+"'";
			M_strSQLQRY += "and SR_LOTNO='"+strLOTNO+"' and SR_RCLNO='"+strRCLNO+"' AND SR_GRPCD<>'"+strGRPCD+"' and SR_ENDDT >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			   
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_strRESQT = nvlSTRVL(M_rstRSSET.getString(1),"0");
			}
			L_dblRESQT=Double.parseDouble(L_strRESQT);
		
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPTRES");
		}
		return L_dblRESQT;
	}
	
	private double getRUNQT(String LP_LOTNO,String LP_RCLNO,String LP_MNLCD)
	{
		String L_strLOTNO = "";
		String L_strRCLNO = "";
		String L_strMNLCD = "";
		String L_strLOTQT = "";
		dblGRDQT1 = 0.000;
		double L_dblRUNQT = 0;
		try
		{
			for(int i = 0;i < tblLTDTL.getRowCount();i++)
			{
				L_strLOTNO = tblLTDTL.getValueAt(i,TB2_LOTNO).toString().trim();
				if(L_strLOTNO.trim().length() != 8)
					continue;
				dblGRDQT1 += Double.parseDouble(tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim());
				L_strRCLNO = tblLTDTL.getValueAt(i,TB2_RCLNO).toString().trim();
				L_strMNLCD = tblLTDTL.getValueAt(i,TB2_MNLCD).toString().trim();
				if(L_strLOTNO.equals(LP_LOTNO) && L_strRCLNO.equals(LP_RCLNO))
				{
					if(L_strMNLCD.equals(LP_MNLCD))
						continue;
					L_strLOTQT = tblLTDTL.getValueAt(i,TB2_ISSQT).toString().trim(); 
					L_dblRUNQT +=  Double.parseDouble(L_strLOTQT);
				}
			}
			/*if(L_dblRUNQT != 0)
				L_dblRUNQT = L_dblRUNQT - LM_DLTQT;*/
			return Double.parseDouble(setNumberFormat(L_dblRUNQT,3));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRUNQT");
		}
		return 0;
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
	


	/**
	 */
	private boolean chkTRGTM(String LP_TIMEGAP)
	{
		boolean  L_RETFL = false;
		try{
			int L_TRGHR = intTRGHR_L;
			int L_TRGMN = intTRGMN_L;
			if(strSALTP.equals("12"))
				{L_TRGHR = intTRGHR_E; L_TRGMN = intTRGMN_E;}
			int L_ACTHR = Integer.parseInt(LP_TIMEGAP.substring(0,2));
			int L_ACTMN = Integer.parseInt(LP_TIMEGAP.substring(3,5));
			//System.out.println("L_TRGHR : "+L_TRGHR);
			//System.out.println("L_TRGMN : "+L_TRGMN);
			//System.out.println("L_ACTHR : "+L_ACTHR);
			//System.out.println("L_ACTMN : "+L_ACTMN);
				
			if(L_ACTHR>L_TRGHR)
				L_RETFL = true;
			if(L_ACTHR == L_TRGHR &&	L_ACTMN+2 > L_TRGMN)
				L_RETFL = true;
		}catch(Exception L_EX)
		{
			{setMSG("Error in chkTRGTM : "+L_EX,'E');}
		}
		return L_RETFL;
	}


	/**
	 * Re-updating the authorized status in FG_ISTRN
	 * This is to re-execution of the Invoice clearance trigger, in case the trigger fails
	 */
	private void exeREAUT(String LP_ISSNO)
	{
		try
		{
			String L_strSQLQRY = "";
			L_strSQLQRY="select ist_mkttp,ist_prdcd,ist_pkgtp, sum(isnull(ist_issqt,0)) ist_issqt,ivt_reqqt from fg_istrn,mr_ivtrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and  IST_WRHTP = '"+strWRHTP+"' and IST_ISSTP = '"+txtISSTP.getText()+"' and ist_issno = '"+txtISSNO.getText()+"' and ist_stsfl='2' and ist_cmpcd=ivt_cmpcd and ist_issno=ivt_ladno and ist_prdcd = ivt_prdcd and ist_pkgtp = ivt_pkgtp and ivt_reqqt>0 and ivt_stsfl<>'X' group by ist_mkttp,ist_prdcd,ist_pkgtp,ivt_reqqt";
			System.out.println(L_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if (!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			System.out.println(L_rstRSSET.getString("IST_ISSQT")+ " / "+L_rstRSSET.getString("IVT_REQQT"));
			if(L_rstRSSET.getDouble("IST_ISSQT") < L_rstRSSET.getDouble("IVT_REQQT"))
				{L_rstRSSET.close();return;}
            L_rstRSSET.close();		
			L_strSQLQRY="update FG_ISTRN set IST_STSFL = '2' where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+strWRHTP+"' and IST_ISSTP = '"+txtISSTP.getText()+"' and ist_issno = '"+txtISSNO.getText()+"' and ist_stsfl='2'";
			cl_dat.exeSQLUPD(L_strSQLQRY,"");
			L_strSQLQRY="select ist_mkttp,ist_prdcd,ist_pkgtp, sum(isnull(ist_issqt,0)) ist_issqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+strWRHTP+"' and IST_ISSTP = '"+txtISSTP.getText()+"' and ist_issno = '"+txtISSNO.getText()+"' and ist_stsfl='2' group by ist_mkttp,ist_prdcd,ist_pkgtp";
			System.out.println(L_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if (!L_rstRSSET.next() || L_rstRSSET==null)
				return;
			String L_strMKTTP = "",L_strPRDCD = "",L_strPKGTP = "",L_strISSQT = "";
			while(true)
			{
				L_strMKTTP = L_rstRSSET.getString("IST_MKTTP");
				L_strPRDCD = L_rstRSSET.getString("IST_PRDCD");
				L_strPKGTP = L_rstRSSET.getString("IST_PKGTP");
				L_strISSQT = setNumberFormat(Double.parseDouble(L_rstRSSET.getString("IST_ISSQT")),3);
				L_strSQLQRY = "update MR_IVTRN set IVT_LADQT = "+L_strISSQT+" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+L_strMKTTP+"' and IVT_LADNO = '"+LP_ISSNO+"' and IVT_PRDCD = '"+L_strPRDCD+"' and IVT_PKGTP = '"+L_strPKGTP+"'";
				System.out.println(L_strSQLQRY);
				cl_dat.exeSQLUPD(L_strSQLQRY,"");
				L_strSQLQRY = "update MR_IVTRN set IVT_LODDT = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"'  where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_MKTTP = '"+L_strMKTTP+"' and IVT_LADNO = '"+LP_ISSNO+"' and IVT_PRDCD = '"+L_strPRDCD+"' and IVT_PKGTP = '"+L_strPKGTP+"' and IVT_LODDT is null";
				cl_dat.exeSQLUPD(L_strSQLQRY,"");
				if(!L_rstRSSET.next())
					break;
			}
			if(cl_dat.exeDBCMT("exeREAUT"))
				setMSG("Re-Authorised",'N');
			
			
		}
		
		catch (Exception L_EX)
		{setMSG("Error in exeREAUT : "+L_EX,'E');}
	}

	
	
	/**
	 * Displays Reservation status for current day 
	 *
	*/
	private void chkRESEV(String LP_PRDCD)
	{
	   try
	   {
	       String L_strGRAD="";
	   
	    if(chkRESEV.isSelected()==false)
			return;
	    
		Hashtable<String,String> hstGRPNM = new Hashtable<String,String>();
	    this.setCursor(cl_dat.M_curWTSTS_pbst);
	    if(pnlRESEV==null)
		{
	        
	        pnlRESEV=new JPanel(null);
	        add(new JLabel("Grade"),1,1,1,1,pnlRESEV,'L');
	        add(lblGRAD=new JLabel(),1,2,1,2,pnlRESEV,'L');
	        lblGRAD.setText(tblGRDTL.getValueAt(tblGRDTL.getSelectedRow(),TB1_PRDDS).toString().trim());
	        String[] L_staCOLHD_RES = {"FL","Customer","Lot.No","Avl.Stk","Qty.Res","Qty.Dsp","Bal.Res","From","To","Res.No"};
			int[] L_inaCOLSZ_RES = {20,250,65,60,60,60,60,60,60,60};
			tblRESEV = crtTBLPNL1(pnlRESEV,L_staCOLHD_RES,500,2,1,4,8,L_inaCOLSZ_RES,new int[]{0});

			add(new JLabel("Quality Hold for Despatch"),7,1,1,3,pnlRESEV,'L');
			String[] L_staCOLHD_QLH = {"FL","Grade","Lot.No","Rcl.No.","Remark","Stock"};
			int[] L_inaCOLSZ_QLH = {20,150,80,20,250,80};
			tblQLTHLD = crtTBLPNL1(pnlRESEV,L_staCOLHD_QLH,100,8,1,12,8,L_inaCOLSZ_QLH,new int[]{0});
	        
		}
			hstGRPNM.clear();
			boolean L_flgEOF = false;
			M_strSQLQRY ="select distinct sr_grpcd,min(pt_prtnm) pt_prtnm from fg_srtrn  left outer join co_ptmst on pt_prttp='C' and pt_grpcd=sr_grpcd where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sr_prdcd='"+LP_PRDCD+"'  and (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0 and sr_enddt >= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'  group by  sr_grpcd order by sr_grpcd";
																																																																					  
			ResultSet M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		    //System.out.println(M_strSQLQRY);
			L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
			while (!L_flgEOF)
			{	 
			    hstGRPNM.put(getRSTVAL(M_rstRSSET,"sr_grpcd","C"),getRSTVAL(M_rstRSSET,"pt_prtnm","C"));
				    
			    L_flgEOF = (!M_rstRSSET.next()) ? true : false;
			}
			M_rstRSSET.close() ;

		
	        tblRESEV.clrTABLE();
			
			
	        M_strSQLQRY = "select sr_grpcd,st_lotno,sum(isnull(st_stkqt,0)) st_stkqt,(isnull(sr_resqt,0)-isnull(sr_rdsqt,0)) sr_balqt,isnull(sr_resqt,0) sr_resqt,isnull(sr_rdsqt,0) sr_rdsqt,";
	        M_strSQLQRY += "sr_strdt,sr_enddt,sr_resno from fg_stmst ,fg_srtrn  where  st_lotno=sr_lotno  and st_prdcd=sr_prdcd  and st_cmpcd=sr_cmpcd and SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0  ";
	        M_strSQLQRY += " and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"' and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stkqt,0)>0  and st_prdcd='"+LP_PRDCD+"'  group by  sr_resno,st_lotno,sr_grpcd,isnull(sr_resqt,0) ,isnull(sr_rdsqt,0), ";
	        M_strSQLQRY += " (isnull(sr_resqt,0)-isnull(sr_rdsqt,0)) ,sr_strdt,sr_enddt order by sr_grpcd,st_lotno";
			
	           M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    //System.out.println(M_strSQLQRY);
				int i =0;
				L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
				while (!L_flgEOF)
				{	
					if(hstGRPNM.containsKey(getRSTVAL(M_rstRSSET,"sr_grpcd","C")))
					    tblRESEV.setValueAt(hstGRPNM.get(getRSTVAL(M_rstRSSET,"sr_grpcd","C")).toString(),i,TB7_GRPNM);
					double L_dblAVLQT = Double.parseDouble(getRSTVAL(M_rstRSSET,"st_stkqt","C"))-Double.parseDouble(getRSTVAL(M_rstRSSET,"sr_balqt","C"));
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_lotno","C"),i,TB7_LOTNO);
				    tblRESEV.setValueAt(setNumberFormat(L_dblAVLQT,3),i,TB7_AVLQT);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_resqt","C"),i,TB7_RESQT);
				    
				    
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_rdsqt","C"),i,TB7_RDSQT);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_balqt","C"),i,TB7_BALQT);
				    
				    tblRESEV.setValueAt(" " +getRSTVAL(M_rstRSSET,"sr_strdt","C"),i,TB7_STRDT);
				    tblRESEV.setValueAt(" " +getRSTVAL(M_rstRSSET,"sr_enddt","C"),i,TB7_ENDDT);
				   
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"sr_resno","C"),i,TB7_RESNO);
				    i++;
				    
					L_flgEOF = (!M_rstRSSET.next()) ? true : false;
				}
				M_rstRSSET.close() ;
		        M_strSQLQRY = "select st_lotno,sum(isnull(st_stkqt,0)) st_stkqt from fg_stmst  where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(st_stkqt,0)>0  and st_prdcd='"+LP_PRDCD+"'  and st_lotno not in (select sr_lotno from fg_srtrn  where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND   (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0 and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"' and sr_prdcd='"+LP_PRDCD+"' )    group by   st_lotno order by st_lotno";
	            M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    
				L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
				while (!L_flgEOF)
				{	
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_lotno","C"),i,TB7_LOTNO);
				    tblRESEV.setValueAt(getRSTVAL(M_rstRSSET,"st_stkqt","C"),i,TB7_AVLQT);
				    i++;
				    
					L_flgEOF = (!M_rstRSSET.next()) ? true : false;
				}
				M_rstRSSET.close() ;

		
	        tblQLTHLD.clrTABLE();
			
			
	        M_strSQLQRY = "select pr_prdds,lt_lotno,lt_rclno,lt_remds,sum(isnull(st_stkqt,0)) st_stkqt from pr_ltmst,co_prmst,fg_stmst where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lt_prdtp=st_prdtp and lt_lotno=st_lotno and lt_prdcd = pr_prdcd  and isnull(st_stkqt,0)>0 and upper(isnull(lt_resfl,'X')) in ('Q','H') group by pr_prdds,lt_lotno,lt_rclno,lt_remds order by pr_prdds,lt_lotno,lt_rclno";

			
	           M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    //System.out.println(M_strSQLQRY);
			    if(!M_rstRSSET.next() || M_rstRSSET==null)
				i =0;
				L_flgEOF = (!M_rstRSSET.next() || M_rstRSSET==null) ? true : false;
				while (!L_flgEOF)
				{	
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"PR_PRDDS","C"),i,TB8_PRDDS);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"LT_LOTNO","C"),i,TB8_LOTNO);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"LT_RCLNO","C"),i,TB8_RCLNO);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"LT_REMDS","C"),i,TB8_REMDS);
				    tblQLTHLD.setValueAt(getRSTVAL(M_rstRSSET,"ST_STKQT","C"),i,TB8_STKQT);
				    i++;
				    
					L_flgEOF = (!M_rstRSSET.next()) ? true : false;
				}
				M_rstRSSET.close() ;
	    
	    pnlRESEV.setSize(300,700);
	    pnlRESEV.setPreferredSize(new Dimension(800,450));
		//pnlRETCP.setPreferredSize(new Dimension(700,250));
		//JOptionPane.showMessageDialog(pnlRESEV,"Reservation Status","Reservation Status",JOptionPane.INFORMATION_MESSAGE);					
		int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlRESEV," Reseveration status",JOptionPane.OK_CANCEL_OPTION);
		//int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlDSPST,"Enter Vehicle Rejection Details",JOptionPane.OK_CANCEL_OPTION);
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	   }catch(Exception L_EX)
	   {
	       setMSG(L_EX,"Resveration status");
	   }
	   
	    
	}
	
	

}