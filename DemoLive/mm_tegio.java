// Entry form for Gate-In/Out Operations

import java.sql.*;
import java.awt.event.*;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.util.StringTokenizer;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import java.awt.Font;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;import java.util.Hashtable;
import javax.swing.JCheckBox;
import javax.swing.*;import javax.swing.border.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
class mm_tegio extends cl_pbase{
	private ResultSet rstRSSET;
	
	private PreparedStatement pstmOTLRY1,pstmINLRY1,pstmOTLRY2,pstmINLRY2,pstmUPDREC,pstmINVQR;
	private JTextField txtGINNO,txtGINBY,txtORDRF,txtORDDT,txtORDQT,txtLRYNO;
	private JTextField txtMATCD,txtMATDS,txtTPRCD,txtTPRDS,txtREMGT,txtEDITR,txtEDITR1;
	private JTextField txtGOTBY,txtFRMDT,txtTORDT,txtREPDT,txtREPTM,txtFRMTM,txtTORTM;
	private JTextField txtGOTDT,txtGOTTM,txtGINDT,txtGINTM;
	private JTextField txtPORNO,txtVENCD,txtVENDS,txtGPSTS,txtDOCRF;
	private JTextField txtMGPNO,txtMGPDT,txtCRRNM,txtDPTCD,txtGPOBY,txtGPODT,txtGPOTM,txtVEHNO;
	private JTextField txtGINCT,txtGOTCT,txtLCCQT,txtBADGE;
	private String strVSPNO =""; 
//	private JRadioButton rdbREPT_H,rdbREPT_T ;
	private hr_rpvgp objRPVGP;	
	//**Added By ATC
	private JTextField txtGPVEN,txtVENNM;
	//**
	private JLabel lblCHLIN,lblCHLOT,lblGINQT,lblGOTQT,lblGINSR,lblGOTSR;
	private JLabel lblDOCNO,lblDOCDT;
	private JButton btnPRINT,btnRPVST,btnRPVST1;
    private JCheckBox chkEXPRT;
	private JComboBox cmbGINTP,cmbGINTP1,cmbMGPTP,cmbSTRTP;
	private JTabbedPane jtpGATEIO;
	private java.sql.Timestamp tmsTEMP;
	private java.sql.Date datTEMP;
	private String strACTDTM ="";
	private String strJOINC ="+";
	private int intTBLRW,intTBLCL,intROWCT,intSELPG,intINDEX,intOLRCT=0,intLRYCT =0;
	private String strGINTP="",strGINNO,strGINDT,strGINBY,strORDRF,strORDDT,strORDQT,strLRYNO,strMATCD,strVENCD,strVENDS,strPORNO,strEXAPR="";	
	private String strMATDS,strTPRCD,strTPRDS,strREMGT,strGOTBY,strGOTDT,strDORNO="",strDOCRF ="";
    private String strSTSFL = "",strLUSBY,strLUPDT,strCURDT,strGINTP1="";
	
	private String strPGINTP,strPGINNO;
	private String strTARWT;	
	private JPanel pnlGINTB = new JPanel();
	private JPanel pnlGOTTB = new JPanel();
	private JPanel pnlGPOTB = new JPanel();
	private JPanel pnlVSTTB = new JPanel();
	private JPanel pnlEXPTB = new JPanel();
	private boolean flgSCRVSB = false; 
    private boolean	flgRMADD = false;
	private boolean	flgPSADD = false;
	private boolean	flgRCADD = false;
	private boolean	flgOTADD = false;
	private boolean	flgLRYFND = true;
	private cl_JTBL tblGINLR,tblGOTLR;
	//**Added By ATC for Gate Pass Details
	private cl_JTBL tblGPDTL;
	final String strRAWMT_fn = "01";
	final String strRECPT_fn = "02";
	final String strDESPT_fn = "03";
//	final String strOTHER_fn = "04";
	final String strSLSRT_fn = "04";
	final String strOTHER_fn = "09";
	final String strSTYRN_fn = "6805010045";				// Material Code for Styrene
	final String strDUMCD_fn = "Z9999";						// Code for Dummy Transporter
	final String strMATCD_fn = "M9999";						// Code for Dummy Material
	final String strSLFCD_fn = "S0029";						// Code for Self Transporter
	final String strACPTG_fn = "W";						// Tag for Waiting Status
	final String strDEFTM_fn = "07:00";
	final String strTRNFL_fn = "0";						// Transfer flag
	final int TBL_OUTFL = 0;
    final int TBL_SRLNO = 1;
    final int TBL_strGINNO = 2;
    final int TBL_LRYNO = 3;
	final int TBL_MATDS = 4;
	final int TBL_CHLQT = 5; 
	final int TBL_NETWT = 6; 
	final int TBL_GINDT = 7; 
    final int TBL_EXAPR = 8;
	final int TBL_GOTDT = 9; 
  	final int TBL_GOTBY = 10;
    final int TBL_REMGT = 11;
    final int TBL_ORDRF = 12;
	String strFRMDT,strTORDT,strFRDTM,strTODTM,strFRMTM,strTORTM,strREPTM;
	//**Added By ATC for Gate Pass Details
	final int TBL_GPMATCD = 1;
	final int TBL_GPMATDS = 2;
	final int TBL_GPUOMCD = 3;
	final int TBL_GPISSQT = 4;
	private int intVSCNT =0,intVSICT=0,intVSOCT=0;
	private  JRadioButton	rdbMOBFLY;
	private  JRadioButton	rdbMOBFLN;
	private  JRadioButton	rdbCMRFLY;
	private  JRadioButton	rdbCMRFLN;
	private  ButtonGroup btgMOBFL;
	private  ButtonGroup btgCMRFL;
	cl_eml ocl_eml = new cl_eml();

	/** <P><PRE style = font-size : 10 pt >
	 * jtpGATEIO number 3 is Visitors Entry Program.	
	 *	
	 *  Author        : Mrs.Dipti.S.Shinde.
		Date          : 27th OCT 2005
		Version       : MMS v2.0.0

		List of tables used :
		Table Name         Primary key                               Operation done
                                                              Insert	Update	Query	Delete	
		----------------------------------------------------------------------------------------------------------
		HR_VSTRN           VS_VSTDT,VS_VSPNO                    #        #      #         #       
		HR_RMMST           RM_SBSCD,RM_DOCNO                    #        #      #         #  
        CO_CDTRN           CD_CODCD,CD_CCSVL                    #        #      #         #                        
		----------------------------------------------------------------------------------------------------------
	 * This Entry Program consist tow types of entries 1. tabMANTAB number 0 = Visitors  2.tabMANTAB number 1 = Contractors  
	 * In visitor / Contractors entry -: User can enter or ADD visitor's Daily Data - Date is Log Date and pass number is generated through CO_CDTRN table.
	 *                                 : and In time of the Visitor is when data is enter so it is log time.
	 *                                 : and Out Time of visitor is when User modified this data that log time will be Out time of Visitor.
	 * MODIFICATION                    : Records of Date wise Visitors which has not Out Time Specified so user has to mod and specify the Out time for visitor.  
	 * DELETE                          : Records of Date wise Visitors which has In time and Out time also.
	 * ENQUIERY                        : Records of date wise Visitors or Contractors which has Specified In and Out Time.
	 * Validatins vldVISITOR()         : Visitor Table -Visitor Name is Compalsary.
	 *                                 : Contractors Table - No of Persons are Compalsary.                  
	 * getDATA()                       : Method for Getting All Records from Table.
	 * 
	 *   */
	private JLabel lblVSTDT,lblVSICT,lblVSTDT1,lblVSICT1;  
	private JComboBox cmbVSCAT;                              /** Visitor Date */
	private JTextField txtVSTDT,txtVSITM,txtVSOTM,txtVSTDT1, /** TextField for Visitor Date */
		     txtVSTNM,txtVSTNM1,txtVSTNM2,txtVSICT,txtVSOCT2,/** textFields are attached to tables column */
		     txtVSORG,txtBDGNO,txtPURPS,txtVSREM,txtVSPNO,   /** textFields are attached to tables column */
		     txtPERVS,txtCLRBY,txtESCBY,txtVSICT1,txtPRTCD,  /** textFields are attached to tables column */
	         txtVSVHN,txtVSARA,txtVSOCT,txtSHFCD,txtFMDAT,txtTODAT,txtDOCDT,txtVSOCT1,/** for driver details*/
			 txtLICNO,txtLICBY,txtLVLDT,txtDRVNM,txtDRVCD;
	private JLabel lblEMPNO;
	private JTextArea txtREMDS;
	private JCheckBox chkREMDS;
	private JButton btnAUTEP;
	private String L_strVCATE="";
	private cl_JTable tblVSTDTL1,tblVSTDTL2,tblCNTDTL;       /** textFields are attached to tables column */
	private JTabbedPane jtpMANTAB;                           /** main tab for visitors tables */
	private JPanel pnlVSTDTL,pnlCNTDTL,pnlVSTIN;             /** panel for Visitor details and contractors details */
	private final int TB1_CHKFL =0;                          /** final value for Visitor tables Column */
	private final int TB1_VSTNM =1;
	private final int TB1_VSPNO =2;
	private final int TB1_VSICT =3;
	private final int TB1_VSOCT =4;
	private final int TB1_VSORG =5;
	private final int TB1_PERVS =6;
	private final int TB1_CLRBY =7;
	private final int TB1_REMDS =8;
	private final int TB1_VSITM =9;
	private final int TB1_EMPNO =10;

	private final int TB3_CHKFL =0;                          /** final column values for Contractors Tables */
	private final int TB3_VSTNM =1;
	private final int TB3_CRDNM =2;
	private final int TB3_VSICT =3;
	private final int TB3_VSOCT =4;
	private final int TB3_SHFCD =5;
	private final int TB3_REMDS =6;
	private final int TB3_GINBY =7;
	private final int TB3_VSITM =8;
	private final int TB3_VSOTM =9;
	private final int TB3_VSPNO =10;
	private final int TB3_PRTCD =11;
	
	private final int TB4_CHKFL=0;
	private final int TB4_VSTNM=1;
	private final int TB4_VSPNO=2;
	private final int TB4_VSTCT=3 ;
	private final int TB4_VSORG =4;
	private final int TB4_PERVS  =5;
	private final int TB4_CLRBY = 6; 
	private final int TB4_MOBFL = 7;
	private final int TB4_CMRFL = 8;
	private final int TB4_REMDS= 9;
	private final int TB4_VSITM =10 ;
	private final int TB4_PURPS =11;
	private final int TB4_BADGE =12;
	private final int TB4_VEHNO =13;
	private final int TB4_VSARA=14;
	private final int TB4_EMPNO=15;
	
	private final int TB5_CHKFL=0;
	private final int TB5_VSTNM=1;
	private final int TB5_VSPNO=2;
	private final int TB5_VSTCT=3 ;
	private final int TB5_VSORG =4;
	private final int TB5_PERVS  =5;
	private final int TB5_CLRBY = 6;
	private final int TB5_APRFL = 7;
	private final int TB5_MOBFL = 8;
	private final int TB5_CMRFL = 9;
	private final int TB5_REMDS= 10;
	private final int TB5_VSITM =11 ;
	private final int TB5_PURPS =12;
	private final int TB5_BADGE =13;
	private final int TB5_VSARA=14;
	private final int TB5_EMPNO=15;
	
	/**column for exit pass table**/
	private final int TB6_CHKFL =0;                         /** final value for Visitor tables Column */
	private final int TB6_DOCDT =1;
	private final int TB6_EMPNO =2;
	private final int TB6_EMPNM =3;
	private final int TB6_DPTNM =4;
	private final int TB6_OUTTM =5;
	private final int TB6_INCTM =6;
	private final int TB6_DOCNO =7;
	private final int TB6_WRKSH =8;
	private final int TB6_REMDS =9;
	private final int TB6_OFPFL =10;
	private final int TB6_AUTBY =11; JTextField txtAUTBY;
	private final int TB6_STSFL =12;
	
    private TxtLimit txtDPTNM;

    private String strPERVS;

    private String strDPTNM;

    

    private JPanel pnlVSTAUTH;
	private JPanel pnlDRVDT;

    private cl_JTable tblVSTDTL3;

    private cl_JTable tblVSTDTL4;
    
    private cl_JTable tblEXPDTL;

    private cl_JTable tblEXPDTL1;

    private JComboBox cmbVSARA;

    private TxtLimit txtVSARA1;

    private JLabel lblAPRE;	
	private boolean flgDRV_EXIST=false;
	private Hashtable<String,String> hstBLKCD;
	private Hashtable<String,String> hstEXPST;
	// visistors...........................
	
	String L_strTIME="";/** String Variable for count difference between Actual out time & Expected out time**/
	String L_strLINE="________________________________________________";
	
	private TBLINPVF objTBLVRF;
	
	mm_tegio()
	{
		super(2);
		try
		{
			pnlGINTB.setLayout(null);
            pnlGOTTB.setLayout(null);
			pnlGPOTB.setLayout(null);
			pnlVSTTB.setLayout(null);
			pnlEXPTB.setLayout(null);
			jtpGATEIO=new JTabbedPane();
			jtpGATEIO.addMouseListener(this);
			setMatrix(15,8);
			setVGAP(12);
			jtpGATEIO.add(pnlGINTB,"Gate-In Entry");
			jtpGATEIO.add(pnlGOTTB,"Gate-Out Entry");
			jtpGATEIO.add(pnlGPOTB,"Gate-Pass Out Entry");
			jtpGATEIO.add(pnlVSTTB,"Visitor Entry");
			jtpGATEIO.add(pnlEXPTB,"Exit Pass Entry");
			 
			/* Added for third tab Gate Pass*/
			add(new JLabel("Stores Type"),1,2,1,1,pnlGPOTB,'L');
			add(cmbSTRTP=new JComboBox(),1,3,1,1.5,pnlGPOTB,'L');
			
			add(new JLabel("Gate Pass Type"),2,2,1,1,pnlGPOTB,'L');
			add(cmbMGPTP=new JComboBox(),2,3,1,1.5,pnlGPOTB,'L');
			
			add(new JLabel("Dept Code"),2,5,1,1,pnlGPOTB,'L');
			add(txtDPTCD=new TxtLimit(3),2,6,1,0.5,pnlGPOTB,'L');
			add(txtDPTNM=new TxtLimit(30),2,8,1,2.65,pnlGPOTB,'R');
	
			add(new JLabel("Gate Pass No."),3,2,1,1,pnlGPOTB,'L');
			add(txtMGPNO=new TxtLimit(8),3,3,1,1.5,pnlGPOTB,'L');
			
			add(new JLabel("Gate pass Date"),3,5,1,1,pnlGPOTB,'L');
			add(txtMGPDT=new TxtDate(),3,6,1,1,pnlGPOTB,'L');
		
			add(new JLabel("Vehicle No."),4,2,1,1,pnlGPOTB,'L');
			add(txtVEHNO=new TxtLimit(20),4,3,1,1.5,pnlGPOTB,'L');
			
			//**Added by ATC
			add(new JLabel("vendor Code"),4,5,1,1,pnlGPOTB,'L');
			add(txtGPVEN = new TxtLimit(5),4,6,1,0.5,pnlGPOTB,'L');
			add(txtVENNM = new TxtLimit(40),4,8,1,2.65,pnlGPOTB,'R');
			//**
			add(new JLabel("Carrier Name"),5,2,1,1,pnlGPOTB,'L');
			add(txtCRRNM=new TxtLimit(40),5,3,1,1.5,pnlGPOTB,'L');
			
			add(new JLabel("Gate Out By"),6,2,1,1,pnlGPOTB,'L');
			add(txtGPOBY=new TxtLimit(3),6,3,1,1.5,pnlGPOTB,'L');
		
			add(new JLabel("Status"),5,5,1,1,pnlGPOTB,'L');
			add(txtGPSTS=new TxtLimit(30),5,6,1,2,pnlGPOTB,'L');
		
			add(new JLabel("Gate Out Date"),6,5,1,1,pnlGPOTB,'L');
			add(txtGPODT=new TxtDate(),6,6,1,1,pnlGPOTB,'L');
			add(txtGPOTM=new TxtDate(),6,7,1,1,pnlGPOTB,'L');
			
			//**Added By ATC for Gate Pass Details
			tblGPDTL = crtTBLPNL(pnlGPOTB,new String[]{"","Material Code","Description","UOM","Quantity"},50,7,1,6,7.93,new int[]{20,100,420,100,100},new int[]{0});
			//**
			/* end Added for third tab Gate Pass*/
			
			add(new JLabel("Gate-In Type"),1,1,1,1,pnlGINTB,'L');
			add(cmbGINTP=new JComboBox(),2,1,1,1.8,pnlGINTB,'L');
			M_strSQLQRY = "Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP IN('SYSMMXXWBT','SYSMMXXMGP','SYSMMXXSST')";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			String L_strDATA ="";	
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)	
			while(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXWBT"))
				{
					L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
			  		cmbGINTP.addItem(L_strDATA);
				}
				else if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXSST"))
				{
					L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
			  		cmbSTRTP.addItem(L_strDATA);
				}
				else
				{
					L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
			  		cmbMGPTP.addItem(L_strDATA);
				}	
			}
			add(new JLabel("Gate-In No."),1,3,1,1,pnlGINTB,'L');
			add(txtGINNO=new TxtLimit(8),2,3,1,1,pnlGINTB,'L');
			
			add(new JLabel("Gate-In Date"),1,4,1,1,pnlGINTB,'L');
			add(txtGINDT=new TxtDate(),2,4,1,1,pnlGINTB,'L');
			add(new JLabel("Time"),1,5,1,1,pnlGINTB,'L');
			add(txtGINTM=new TxtTime(),2,5,1,1,pnlGINTB,'R');
			add(new JLabel("Gate-In By"),1,6,1,1,pnlGINTB,'L');
			add(txtGINBY=new TxtLimit(3),2,6,1,1,pnlGINTB,'L');
			add(btnPRINT = new JButton("Print"),1,7,1,0.9,pnlGINTB,'L');
			btnPRINT.setEnabled(false);
			
			add(lblDOCNO = new JLabel("Chalan No."),3,1,1,1,pnlGINTB,'L');
			add(txtORDRF=new TxtLimit(8),4,1,1,1,pnlGINTB,'L');
		
			add(lblDOCDT = new JLabel("Chalan Date"),3,2,1,1,pnlGINTB,'L');
			add(txtORDDT=new TxtDate(),4,2,1,1,pnlGINTB,'L');
			
			add(new JLabel("Chalan Qty."),3,3,1,1,pnlGINTB,'L');
			add(txtORDQT=new TxtNumLimit(12.3),4,3,1,1,pnlGINTB,'L');
            
			add(new JLabel("Gate-Out Date"),3,4,1,1,pnlGINTB,'L');
			add(txtGOTDT=new TxtDate(),4,4,1,1,pnlGINTB,'L');
			add(new JLabel("Time"),3,5,1,1,pnlGINTB,'L');
			add(txtGOTTM=new TxtTime(),4,5,1,1,pnlGINTB,'R');
			txtGOTDT.setEnabled(false);
			txtGOTTM.setEnabled(false);
			add(new JLabel("Gate-Out By"),3,6,1,1,pnlGINTB,'L');
			add(txtGOTBY=new TxtLimit(3),4,6,1,1,pnlGINTB,'L');
			txtGOTBY.setEnabled(false);
			
			add(new JLabel("Vendor Code "),5,1,1,3,pnlGINTB,'L');
			add(txtVENCD=new TxtLimit(5),6,1,1,1,pnlGINTB,'L');
			add(new JLabel("Vendor Name"),5,2,1,3,pnlGINTB,'L');
			add(txtVENDS=new TxtLimit(40),6,2,1,3,pnlGINTB,'L');
			txtVENDS.setEnabled(false);
			add(new JLabel("Doc. Ref."),5,5,1,1,pnlGINTB,'L');
			add(txtDOCRF=new TxtLimit(8),6,5,1,1,pnlGINTB,'L');
			txtVENDS.setEnabled(false);
			
			add(new JLabel("P.O.No."),5,6,1,1,pnlGINTB,'L');
			add(txtPORNO=new TxtLimit(8),6,6,1,1,pnlGINTB,'L');
			
			add(new JLabel("Material Code"),7,1,1,3,pnlGINTB,'L');
			add(txtMATCD=new TxtLimit(10),8,1,1,1,pnlGINTB,'L');
			add(new JLabel("Material Description"),7,2,1,3,pnlGINTB,'L');
			add(txtMATDS=new TxtLimit(45),8,2,1,3,pnlGINTB,'L');
			txtMATDS.setEnabled(false);
			
			add(new JLabel("Reporting Date"),7,5,1,1,pnlGINTB,'L');
			add(txtREPDT=new TxtDate(),8,5,1,1,pnlGINTB,'L');
			add(new JLabel("Time"),7,6,1,1,pnlGINTB,'L');
			add(txtREPTM=new TxtTime(),8,6,1,1,pnlGINTB,'L');
			
			add(new JLabel("Lorry No."),9,1,1,1,pnlGINTB,'L');
			add(txtLRYNO=new TxtLimit(15),10,1,1,1,pnlGINTB,'L');
			add(new JLabel("Transporter Code "),9,2,1,3,pnlGINTB,'L');
			add(txtTPRCD=new TxtLimit(5),10,2,1,1,pnlGINTB,'L');
			add(new JLabel("  Description"),9,3,1,4,pnlGINTB,'L');
			add(txtTPRDS=new TxtLimit(40),10,3,1,4,pnlGINTB,'L');
			txtTPRDS.setEnabled(false);
			
			add(new JLabel("Load carry. cap"),9,7,1,1.3,pnlGINTB,'R');
			add(txtLCCQT=new TxtNumLimit(5.3),10,7,1,0.8,pnlGINTB,'L');
			
			add(new JLabel("Remarks"),11,1,1,1,pnlGINTB,'L');
			add(chkEXPRT = new JCheckBox("Export"),11,2,1,1,pnlGINTB,'L');
            add(txtREMGT=new TxtLimit(100),12,1,1,5,pnlGINTB,'L');
            add(new JLabel("In Persons"),11,6,1,1,pnlGINTB,'L');
            add(new JLabel("Out Persons"),11,7,1,1,pnlGINTB,'L');
            add(txtGINCT=new TxtNumLimit(2.0),12,6,1,0.75,pnlGINTB,'L');
            add(txtGOTCT=new TxtNumLimit(2.0),12,7,1,0.75,pnlGINTB,'L');
            //add(txtREMGT=new TxtLimit(100),12,1,1,5,pnlGINTB,'L');
			//add(chkEXPRT = new JCheckBox("Export"),12,7,1,1,pnlGINTB,'L');
			add(new JLabel("Gate-In Type"),1,1,1,1,pnlGOTTB,'L');
			add(cmbGINTP1=new JComboBox(),1,2,1,2,pnlGOTTB,'L');
			
			//driver details
			pnlDRVDT = new JPanel(null);
			//pnlDRVDT.setBorder(new EtchedBorder(Color.black,Color.lightGray));
			pnlDRVDT.setBorder(BorderFactory.createTitledBorder("   Driver Details   "));
			add(pnlDRVDT,2,7,6.5,2.1,pnlGINTB,'L');
			
			add(new JLabel("  Licence No."),1,1,1,1,pnlDRVDT,'L');
			add(txtLICNO = new TxtLimit(20),1,2,1,1,pnlDRVDT,'L');
            
			add(new JLabel("  Issuing Authority"),2,1,1,1,pnlDRVDT,'L');
			add(txtLICBY = new TxtLimit(20),2,2,1,1,pnlDRVDT,'L');
			
			add(new JLabel("  Valid Up To"),3,1,1,1,pnlDRVDT,'L');
			add(txtLVLDT = new TxtDate(),3,2,1,1,pnlDRVDT,'L');

			add(new JLabel("  Driver Name"),4,1,1,1,pnlDRVDT,'L');
			add(txtDRVNM = new TxtLimit(30),4,2,1,1,pnlDRVDT,'L');
			
			add(new JLabel("  Driver Code"),5,1,1,1,pnlDRVDT,'L');
			add(txtDRVCD = new TxtLimit(5),5,2,1,1,pnlDRVDT,'L');
			
			add(new JLabel("  Remark"),6,1,1,0.5,pnlDRVDT,'L');
			add(chkREMDS = new JCheckBox(""),6,1,1,0.5,pnlDRVDT,'R');
			//add(txtREMDS = new TxtAreaLimit(200),6,2,1,1,pnlDRVDT,'L');
			txtREMDS = new TxtAreaLimit(200);	
			JScrollPane scrollPane=new JScrollPane(txtREMDS);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 			scrollPane.setBounds(10, 10, 300, 200);
			add(scrollPane,6,2,1,1,pnlDRVDT,'L');
			////end of driver details
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(rstRSSET !=null)
			while(rstRSSET.next()){
				L_strDATA = rstRSSET.getString("CMT_CODCD") + " " + rstRSSET.getString("CMT_CODDS");
			      cmbGINTP1.addItem(L_strDATA);
			}
			//setENBL(false);
			txtGOTDT.setEnabled(false);
			txtGOTTM.setEnabled(false);
			txtGOTBY.setEnabled(false);
			cmbGINTP.setEnabled(false);
			txtGINNO.setEnabled(false);
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			crtPRESTM();
			Color clrBRWN = new Color(176,28,54);	
			setMatrix(15,8);
			String[] L_LOCHD = {"Out","SR","Gate-In No","Lorry No.","Material","Quantity","Net Qty","In Date","APRVL","Gate-Out Date","By","Remark","Ref. No"};
			int[] L_COLSZ = {20,20,60,100,80,60,60,100,35,115,35,100,20};
			tblGINLR = crtTBLPNL(pnlGOTTB,L_LOCHD,500,2,1,4.1,7.8,L_COLSZ,new int[]{0});
			tblGINLR.addMouseListener(this);
			add(new JLabel("Total In Vehicles"),6,1,1,2,pnlGOTTB,'L');
			add(lblGINSR = new JLabel(" "),6,3,1,1,pnlGOTTB,'L');
			lblGINSR.setForeground(clrBRWN);	
			add(new JLabel("Net Qty."),6,4,1,1,pnlGOTTB,'L');
			add(lblCHLIN = new JLabel(" "),6,5,1,1,pnlGOTTB,'L');
			lblCHLIN.setHorizontalAlignment(RIGHT);
			lblCHLIN.setForeground(new Color(176,28,54));	
			add(lblGINQT = new JLabel(" "),6,6,1,1,pnlGOTTB,'L');
			lblGINQT.setHorizontalAlignment(RIGHT);
			lblGINQT.setForeground(clrBRWN);	
	
			add(new JLabel("Out Vehicles from  "),7,1,1,2,pnlGOTTB,'L');
			add(txtFRMDT = new TxtDate(),7,3,1,1,pnlGOTTB,'L');
			add(txtFRMTM = new TxtTime(),7,4,1,1,pnlGOTTB,'L');
			add(new JLabel(" to "),7,5,1,1,pnlGOTTB,'L');
			add(txtTORDT = new TxtDate(),7,6,1,1,pnlGOTTB,'L');
			add(txtTORTM = new TxtTime(),7,7,1,1,pnlGOTTB,'L');
			tblGOTLR = crtTBLPNL(pnlGOTTB,L_LOCHD,500,8,1,3.7,7.8,L_COLSZ,new int[]{0});
			tblGOTLR.setEnabled(false);
			txtFRMDT.setText(cl_dat.M_strLOGDT_pbst);
			txtFRMTM.setText(strDEFTM_fn);
	
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			M_calLOCAL.add(Calendar.DATE,+1);
			txtTORDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
			txtTORTM.setText(strDEFTM_fn);
			add(new JLabel("Total Out Vehicles"),12,1,1,2,pnlGOTTB,'L');
			add(lblGOTSR = new JLabel(" "),12,3,1,1,pnlGOTTB,'L');
			lblGOTSR.setHorizontalAlignment(RIGHT);
			lblGOTSR.setForeground(clrBRWN);	
			add(new JLabel("Net Qty."),12,4,1,1,pnlGOTTB,'L');	
			add(lblCHLOT = new JLabel(""),12,5,1,1,pnlGOTTB,'L');	
		
			lblCHLOT.setHorizontalAlignment(RIGHT);
			lblCHLOT.setForeground(new Color(176,28,54));	
		
			add(lblGOTQT = new JLabel(""),12,6,1,1,pnlGOTTB,'L');	
		
			lblGOTQT.setHorizontalAlignment(RIGHT);
			lblGOTQT.setForeground(clrBRWN);	
			getINLRY();
			getOTLRY();
			intSELPG = 0;								// Selected Tab Page
		
		//visitors...............................
		
			/*add(lblVSTDT = new JLabel("Visit Date"),1,1,1,1,pnlVSTTB,'L');
			add(txtVSTDT = new TxtDate(),1,2,1,1,pnlVSTTB,'L');
			txtVSTDT.setText(cl_dat.M_strLOGDT_pbst);*/
			
			pnlVSTIN = new JPanel();pnlVSTIN.setLayout(null);
			pnlVSTDTL = new JPanel();
			pnlCNTDTL = new JPanel();
			pnlVSTAUTH =new JPanel(null);
			objRPVGP = new hr_rpvgp(M_strSBSCD);
		    ///
			add(new JLabel("Visitor Category"),2,1,1,2,pnlVSTIN,'L');
			add(cmbVSCAT=new JComboBox(),2,2,1,2,pnlVSTIN,'L');
			add(new JLabel("Visit Date"),2,5,1,1,pnlVSTIN,'L');
			add(txtVSTDT=new TxtDate(),2,6,1,1,pnlVSTIN,'L');
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXVCT' and CMT_CHP01='01' ";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(rstRSSET !=null)
			while(rstRSSET.next())
			{
				L_strVCATE = rstRSSET.getString("CMT_CODCD") + " " + rstRSSET.getString("CMT_CODDS");
			     cmbVSCAT.addItem(L_strVCATE);
			}
			add(btnRPVST1 = new JButton("Print"),1,6,1,1,pnlVSTIN,'L');
			
		    add(new JLabel("Visitor Pass No"),3,1,1,1,pnlVSTIN,'L');
			add(txtVSPNO=new TxtLimit(8),3,2,1,1,pnlVSTIN,'L');
			add(new JLabel("Badge No."),3,3,1,1,pnlVSTIN,'L');
			add(txtBDGNO=new TxtLimit(10),3,4,1,1,pnlVSTIN,'L');
			add(new JLabel("Shift"),3,5,1,1,pnlVSTIN,'L');
			add(txtSHFCD=new TxtLimit(2),3,6,1,1,pnlVSTIN,'L');
		    add(new JLabel("Visitor Name"),4,1,1,1,pnlVSTIN,'L');
			add(txtVSTNM=new TxtLimit(35),4,2,1,1,pnlVSTIN,'L');
			add(new JLabel("Organisation"),4,3,1,1,pnlVSTIN,'L');
			add(txtVSORG=new TxtLimit(35),4,4,1,3,pnlVSTIN,'L');	
		    add(new JLabel("No.of In Persons"),5,1,1,1,pnlVSTIN,'L');
			add(txtVSICT=new TxtNumLimit(3),5,2,1,1,pnlVSTIN,'L');
			add(new JLabel("Purpose"),5,3,1,1,pnlVSTIN,'L');
			add(txtPURPS=new TxtLimit(50),5,4,1,3,pnlVSTIN,'L');	
			
			add(new JLabel("No.ofOutPersons"),6,1,1,1,pnlVSTIN,'L');
			add(txtVSOCT=new TxtNumLimit(3),6,2,1,1,pnlVSTIN,'L');
			add(new JLabel("Area"),6,3,1,1,pnlVSTIN,'L');
			add(txtVSARA=new TxtLimit(15),6,4,1,1,pnlVSTIN,'L');
			add(new JLabel("Vehicle"),6,5,1,1,pnlVSTIN,'L');
			add(txtVSVHN=new TxtLimit(15),6,6,1,1,pnlVSTIN,'L');
		
            add(new JLabel("Person Visited"),7,1,1,1,pnlVSTIN,'L');
			add(txtPERVS=new TxtLimit(15),7,2,1,1,pnlVSTIN,'L');
			add(lblEMPNO=new JLabel(""),7,5,1,1,pnlVSTIN,'L');
			//************************ 28-05-2007	
			add(new JLabel("Department"),7,3,1,1,pnlVSTIN,'L');
			add(txtDPTNM=new TxtLimit(15),7,4,1,1,pnlVSTIN,'L');
			//************************ 28-05-2007
			add(new JLabel("Clearance By"),8,1,1,1,pnlVSTIN,'L');
			add(txtCLRBY=new TxtLimit(5),8,2,1,1,pnlVSTIN,'L');	
			add(new JLabel("Escorted By"),8,3,1,1,pnlVSTIN,'L');
			add(txtESCBY=new TxtLimit(15),8,4,1,1,pnlVSTIN,'L');
			
			
		    add(new JLabel("Gate-In Date"),9,1,1,1,pnlVSTIN,'L');
		  	add(txtVSITM=new TxtLimit(20),9,2,1,2,pnlVSTIN,'L');		
			add(new JLabel("Gate-Out Date"),9,4,1,1,pnlVSTIN,'L');
			add(txtVSOTM=new TxtLimit(20),9,5,1,2,pnlVSTIN,'L');		
		    add(new JLabel("Material Brought In"),10,1,1,1,pnlVSTIN,'L');
			add(txtVSREM=new TxtLimit(200),10,2,1,5,pnlVSTIN,'L');	
			
			btgMOBFL=new ButtonGroup();
			btgCMRFL=new ButtonGroup();
			
			add(new JLabel("Mobile Allowed"),11,1,1,1,pnlVSTIN,'L');
			add(rdbMOBFLY=new JRadioButton("Yes"),11,2,1,1,pnlVSTIN,'L');
			add(rdbMOBFLN=new JRadioButton("No"),11,3,1,1,pnlVSTIN,'L');
				
			add(new JLabel("Camera Allowed"),11,4,1,2,pnlVSTIN,'L');
			add(rdbCMRFLY=new JRadioButton("Yes"),11,5,1,1,pnlVSTIN,'L');
			add(rdbCMRFLN=new JRadioButton("No"),11,6,1,1,pnlVSTIN,'L');
			
			btgMOBFL.add(rdbMOBFLY);btgMOBFL.add(rdbMOBFLN);btgCMRFL.add(rdbCMRFLY);btgCMRFL.add(rdbCMRFLN);
			
			rdbMOBFLN.setSelected(true);
			rdbCMRFLN.setSelected(true);
		    ///
			jtpMANTAB = new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			
			
			/**table for Exit Pass Entry**/
			
			//add(chkCANCEL_EX=new JCheckBox("Cancel Exit Pass"),1,5,1,5,pnlEXPTB,'L');
			String[] L_strCOLHD5 = {"","Doc Date","Emp No","Emp Name","Dept","Out Time","In Time","DOC No","WrkShf","Purpose","Off/Pers","Auth By","Status"};
			int[] L_intCOLSZ5 = {20,80,50,100,80,70,70,70,40,210,40,50,80};		    				
			tblEXPDTL = crtTBLPNL1(pnlEXPTB,L_strCOLHD5,500,2,1,5,6,L_intCOLSZ5,new int[]{0});
			
			add(btnAUTEP=new JButton("Employee Exit"),8,2,1,1,pnlEXPTB,'L');
			add(new JLabel("Date"),8,4,1,1,pnlEXPTB,'L');
			add(txtDOCDT=new TxtDate(),8,5,1,1,pnlEXPTB,'L');
			txtDOCDT.addKeyListener(this);
			tblEXPDTL1 = crtTBLPNL1(pnlEXPTB,L_strCOLHD5,500,9,1,5,6,L_intCOLSZ5,new int[]{0});
			//add(pnlEXPTB,1,1,15,10,jtpGATEIO,'L');
			tblEXPDTL.addKeyListener(this);
			tblEXPDTL.addFocusListener(this);
			tblEXPDTL.setCellEditor(TB6_AUTBY,txtAUTBY = new TxtLimit(3));
			txtAUTBY.addKeyListener(this);
			txtAUTBY.addFocusListener(this);
			objTBLVRF = new TBLINPVF();
			tblEXPDTL.setInputVerifier(objTBLVRF);
			
			/**first table of visitor Entry*/
			pnlVSTDTL.setLayout(null);
			String[] L_strCOLHD = {"Select","Visitor Name","Pass No","In Count","Out Count","Orgainsation","To Meet","APR By","Remarks","In Time","Emp.No"};
      		int[] L_intCOLSZ = {20,100,80,60,60,100,50,50,150,150,40};	    				
			tblVSTDTL1 = crtTBLPNL1(pnlVSTDTL,L_strCOLHD,100,1,1,4,6,L_intCOLSZ,new int[]{0});
			tblVSTDTL1.addMouseListener(this);
			add(lblVSTDT = new JLabel("In Visitors "),5,1,1,2,pnlVSTDTL,'L');
			add(lblVSICT = new JLabel("  "),5,2,1,1,pnlVSTDTL,'L');
			add(new JLabel("From Date"),6,4,1,1,pnlVSTDTL,'L');
			add(txtFMDAT=new TxtDate(),6,5,1,1,pnlVSTDTL,'L');
			add(new JLabel("To Date"),6,6,1,1,pnlVSTDTL,'L');
			add(txtTODAT=new TxtDate(),6,7,1,1,pnlVSTDTL,'L');
			tblVSTDTL1.setCellEditor(TB1_VSOCT, txtVSOCT1 = new TxtNumLimit(3));
			txtFMDAT.addKeyListener(this);
			txtTODAT.addKeyListener(this);
			String[] L_strCOLHD1 = {"Select","Visitor Name","Pass No","In Count","Out Count","Orgainsation","To Meet","APR By","Remarks","Out Time","Emp.No."};
			tblVSTDTL2 = crtTBLPNL1(pnlVSTDTL,L_strCOLHD1,50,7,1,4,6,L_intCOLSZ,new int[]{0});
			add(lblVSTDT = new JLabel("Out Visitors "),6,1,1,2,pnlVSTDTL,'L');
			add(pnlVSTDTL,1,1,10,9,jtpMANTAB,'L');
			tblVSTDTL1.addFocusListener(this);
			tblVSTDTL2.addFocusListener(this);
			
			add(lblAPRE=new JLabel("Approved Requests"),1,1,1,2.5,pnlVSTAUTH,'L');
		      lblAPRE.setForeground(Color.BLUE );
			String[] L_strCOLHD3 = {"Select","Visitor Name","Pass No","InCount","Orgainsation","To Meet","APR By","Mobile","Camera","Material Brought In","In Time","Purpose","Badge No","Vehicle No","Area","Emp.No."};
			int[] L_intCOLSZ3 = {20,100,80,60,100,50,50,30,30,100,100,75,75,75,75,40};	  
			add(btnRPVST = new JButton("Print"),1,6,1,1,pnlVSTAUTH,'L');
		//	add(rdbREPT_H = new JRadioButton("HTML"),1,4,1,1,pnlVSTAUTH,'L');
		//	add(rdbREPT_T = new JRadioButton("TEXT"),1,5,1,1,pnlVSTAUTH,'L');
		//	ButtonGroup btgREPT=new ButtonGroup();
		//	btgREPT.add(rdbREPT_H);btgREPT.add(rdbREPT_T);
			
			tblVSTDTL3 = crtTBLPNL1(pnlVSTAUTH,L_strCOLHD3,200,2,1,4,6,L_intCOLSZ3,new int[]{0,7,8});
			tblVSTDTL3.setCellEditor(TB4_VSARA, txtVSARA1=new TxtLimit(15));
			tblVSTDTL3.setCellEditor(TB4_BADGE, txtBADGE=new TxtLimit(15));
			tblVSTDTL3.setCellEditor(TB4_VEHNO, new TxtLimit(15));

			tblVSTDTL3.addFocusListener( this);
			//tblVSTDTL3.addKeyListener( this);
			//tblVSTDTL3.addMouseListener( this);
			
			add(lblAPRE=new JLabel("GP Requests Pending for Approval"),6,1,1,4,pnlVSTAUTH,'L');
		    lblAPRE.setForeground(Color.BLUE );
			String[] L_strCOLHD4 = {"Select","Visitor Name","Pass No","InCount","Orgainsation","To Meet","APR By","Apr Status","Mobile","Camera","Material Brought In","In Time","Purpose","Badge No","Area","Emp.No."};
			int[] L_intCOLSZ4 = {20,100,80,60,100,50,50,70,30,30,100,100,75,75,75,75,40};	    				
			tblVSTDTL4 = crtTBLPNL1(pnlVSTAUTH,L_strCOLHD4,200,7,1,4,6,L_intCOLSZ3,new int[]{0,8,9});
			add(pnlVSTAUTH,1,1,10,9,jtpMANTAB,'L');
			tblVSTDTL4.setColumnColor(TB5_APRFL,java.awt.Color.red);
			tblVSTDTL4.addFocusListener(this);
			
			//first table of Contractors Entry
			pnlCNTDTL.setLayout(null);
			String[] L_strCOLHD2 = {"Select","Contractor Name","Cordinator Name","In Count","Out Count","Shift","Remarks","Gate In Charge","In Time","Out Time","Pass no","party code"};
      		int[] L_intCOLSZ2 = {30,150,120,40,40,30,140,30,70,70,70,50};	    				
			add(lblVSTDT = new JLabel("Date"),1,1,1,1,pnlCNTDTL,'L');
			add(txtVSTDT1 = new TxtDate(),1,2,1,1,pnlCNTDTL,'L');
			tblCNTDTL = crtTBLPNL1(pnlCNTDTL,L_strCOLHD2,50,2,1,10,6,L_intCOLSZ2,new int[]{0});
			//tblCNTDTL.setCellEditor(TB3_VSTNM, txtVSTNM2 = new TxtLimit(35));
			tblCNTDTL.addMouseListener(this);
			add(pnlCNTDTL,1,1,10,6,jtpMANTAB,'L');
			add(lblVSTDT1 = new JLabel("In Contractors "),12,1,1,1,pnlCNTDTL,'L');
			add(lblVSICT1 = new JLabel("  "),12,2,1,1,pnlCNTDTL,'L');
			tblCNTDTL.addFocusListener(this);
			tblCNTDTL.addKeyListener(this);
			tblCNTDTL.setCellEditor(TB3_VSICT, txtVSICT1 = new TxtNumLimit(3));
			tblCNTDTL.setCellEditor(TB3_VSOCT, txtVSOCT2 = new TxtNumLimit(3));
			tblCNTDTL.setCellEditor(TB3_VSTNM, txtVSTNM1 = new TxtLimit(35));
			tblCNTDTL.setCellEditor(TB3_PRTCD, txtPRTCD = new TxtLimit(5));
			jtpMANTAB.addTab("Visitor-GE-Request ",pnlVSTIN);
			jtpMANTAB.addTab("Visitor-Gate-out",pnlVSTDTL);
			jtpMANTAB.addTab("Contractors",pnlCNTDTL);
			jtpMANTAB.addTab("Vistors-Gate-in",pnlVSTAUTH);
			add(jtpMANTAB,1,1,14,8,pnlVSTTB,'L');
			tblCNTDTL.addFocusListener(this);
			txtVSTNM1.addKeyListener(this);
			txtPRTCD.addKeyListener(this);
			txtVSTNM1.addFocusListener(this);
			txtPRTCD.addFocusListener(this);
			txtREMDS.setEnabled(false);
			//visitors.....................
			add(jtpGATEIO,1,1,18,8,this,'L');
			
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			flgSCRVSB = true;
			setENBL(false);
			txtDPTNM.setEnabled(false);
			txtDRVCD.setEnabled(false);
			
			
			
			////Driver black listing codes
			hstBLKCD=new Hashtable<String,String>(); 			
			try
			{
					M_strSQLQRY  = " Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXDBL'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						while(M_rstRSSET.next())
							hstBLKCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					}				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"fetching BLKCD()");
			}
			//////////////////////////////////////////////////////////////////
			
			hstEXPST=new Hashtable<String,String>(); 			
			try
			{
					M_strSQLQRY  = " Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'STS' and CMT_CGSTP = 'HRXXEXP'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!=null)
					{
						while(M_rstRSSET.next())
							hstEXPST.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					}				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"fetching BLKCD()");
			}
			txtDOCDT.setText(cl_dat.M_strLOGDT_pbst);
			
		}
		catch(Exception e)
		{
			setMSG(e,"Error in constructor ");
			//System.out.println(e);
		}
	}	
	// Method to clear the screen
	void clrCOMP()
	{
		try
		{
			txtGINNO.setText("");
			txtGINDT.setText("");
			txtGINTM.setText("");
			txtGINBY.setText("");
			txtGOTDT.setText("");
			txtGOTTM.setText("");
			txtGOTBY.setText("");
			txtORDRF.setText("");
			txtORDDT.setText("");
			txtORDQT.setText("");
			txtLRYNO.setText("");
			txtMATCD.setText("");
			txtMATDS.setText("");
			txtTPRCD.setText("");
			txtTPRDS.setText("");
			txtREMGT.setText("");
			txtREPDT.setText("");
			txtREPTM.setText("");
			txtPORNO.setText("");
			txtVENCD.setText("");
			txtVENDS.setText("");
			txtDOCRF.setText("");
			txtMGPNO.setText("");
			txtMGPDT.setText("");
			txtVEHNO.setText("");
			txtCRRNM.setText("");
			txtDPTCD.setText("");
			txtDPTNM.setText("");
			txtGPOBY.setText("");
			txtGPODT.setText("");
			txtGPOTM.setText("");
			//Added by ATC
			txtGPVEN.setText("");
			txtVENNM.setText("");
			txtLICNO.setText("");
			txtLICBY.setText("");
			txtLVLDT.setText("");
			txtDRVNM.setText("");
			txtDRVCD.setText("");
			txtREMDS.setText("");
			chkEXPRT.setSelected(false);
			btnPRINT.setEnabled(false);
            strSTSFL = "";
            txtVSPNO.setText("");
            txtVSTDT.setText("");
            txtBDGNO.setText("");
            txtVSTNM.setText("");
            txtVSORG.setText("");
            txtVSICT.setText("");
            txtPERVS.setText("");
            lblEMPNO.setText("");
            txtESCBY.setText("");
            txtCLRBY.setText("");
            txtPURPS.setText("");
            txtVSREM.setText("");
            txtVSITM.setText("");
            txtVSOTM.setText("");
			txtVSARA.setText("");
			txtSHFCD.setText("");
			txtVSOCT.setText("");
			txtVSVHN.setText("");
			txtGINCT.setText("");
			txtGOTCT.setText("");
			txtLCCQT.setText("");
			txtDPTNM.setText("");
		}
		catch(Exception e)
		{
			setMSG(e,"Error in clrCOMP ");
		}
	}
	void clrGPTAB()
	{
	    txtDPTCD.setText("");
	    txtDPTNM.setText("");
	    txtGPVEN.setText("");
	    txtVENNM.setText("");
	    txtVEHNO.setText("");
	    txtMGPDT.setText("");
	    txtMGPDT.setText("");
	    txtGPOBY.setText("");
	    txtGPODT.setText("");
	    txtGPOTM.setText("");
	    txtGPSTS.setText("");
	    txtCRRNM.setText("");
	    tblGPDTL.clrTABLE();         
	}
	// Method to Enable/Disable the components
	void setENBL(boolean L_ACTION)
	{
		try
		{
			cmbGINTP.setEnabled(!L_ACTION);
			txtGINNO.setEnabled(!L_ACTION);
			txtTPRCD.setEnabled(L_ACTION);
			txtTPRDS.setEnabled(L_ACTION);
			txtREMGT.setEnabled(L_ACTION);
		   	txtGINDT.setEnabled(L_ACTION);
			txtGINTM.setEnabled(L_ACTION);
			txtGINBY.setEnabled(L_ACTION);
			txtORDRF.setEnabled(L_ACTION);
			txtORDDT.setEnabled(L_ACTION);
			txtORDQT.setEnabled(L_ACTION);
			txtLRYNO.setEnabled(L_ACTION);
			txtMATCD.setEnabled(L_ACTION);
			txtMATDS.setEnabled(L_ACTION);
			txtREPDT.setEnabled(L_ACTION);
			txtREPTM.setEnabled(L_ACTION);
			txtPORNO.setEnabled(L_ACTION);
			txtVENCD.setEnabled(L_ACTION);
			
			txtLICNO.setEnabled(L_ACTION);
			txtLICBY.setEnabled(L_ACTION);
			txtLVLDT.setEnabled(L_ACTION);
			txtDRVNM.setEnabled(L_ACTION);
			txtREMDS.setEnabled(false);
			
			txtDPTNM.setEnabled( !L_ACTION);
			txtDOCRF.setEnabled(false);
            if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
            {
                txtGINCT.setEnabled(true);
                txtGOTCT.setEnabled(false);
            }
            else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
            {   
                txtGINCT.setEnabled(false);
                txtGOTCT.setEnabled(true);
            }
            else    
            {
                txtGINCT.setEnabled(false);
                txtGOTCT.setEnabled(false);
            }
		 	// For Gate Pass Tab
			txtMGPDT.setEnabled(false);
			txtDPTCD.setEnabled(false);
			txtDPTNM.setEnabled(false);
			txtGPOBY.setEnabled(false);
			txtGPODT.setEnabled(false);
			txtGPOTM.setEnabled(false);
			txtGPSTS.setEnabled(false);
		 	//**Added By ATC
			txtGPVEN.setEnabled(false);
			txtVENNM.setEnabled(false);
			//**
			tblGPDTL.setEnabled(false);
			tblVSTDTL1.setEnabled(false);
			tblVSTDTL2.setEnabled(false);
			tblVSTDTL3.setEnabled(false);
			tblVSTDTL4.setEnabled(false);
			tblEXPDTL.setEnabled(false);
			tblEXPDTL1.setEnabled(false);
			
			tblCNTDTL.setEnabled(false);
			tblVSTDTL1.cmpEDITR[TB1_CHKFL].setEnabled(true);
		
            txtVSITM.setEnabled(false);
            txtVSOTM.setEnabled(false);
           	//if(jtpGATEIO.getSelectedIndex() == 3)
			//{
			  	tblVSTDTL1.clrTABLE();
				tblVSTDTL2.clrTABLE();
				tblVSTDTL3.clrTABLE();
				tblVSTDTL4.clrTABLE();
				tblEXPDTL.clrTABLE();
				tblEXPDTL1.clrTABLE();
				tblCNTDTL.clrTABLE();
				
				tblVSTDTL3.cmpEDITR[TB4_MOBFL].setEnabled(false);
				tblVSTDTL3.cmpEDITR[TB4_CMRFL].setEnabled(false);
				
				tblVSTDTL4.cmpEDITR[TB5_APRFL].setEnabled(false);
				tblVSTDTL4.cmpEDITR[TB5_MOBFL].setEnabled(false);
				tblVSTDTL4.cmpEDITR[TB5_CMRFL].setEnabled(false);
				
				
				if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					txtVSPNO.setEnabled(true);
					txtVSTDT.setEnabled(false);
		            txtVSTDT1.setEnabled(true);
					tblCNTDTL.setEnabled(false);
					tblCNTDTL.cmpEDITR[TB3_CHKFL].setEnabled(true);
					tblVSTDTL1.cmpEDITR[TB1_CHKFL].setEnabled(true);
					tblVSTDTL3.setEnabled(true);
					tblVSTDTL4.setEnabled(true);
					
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					txtVSTDT.setText(cl_dat.M_strLOGDT_pbst);
		         	   txtVSTDT.setEnabled(false);
		         	   txtVSTDT1.setEnabled(false);
        	      		  txtVSTDT1.setText(cl_dat.M_strLOGDT_pbst);
					txtVSPNO.setEnabled(false);
					txtVSOCT.setEnabled(false);
		          		txtBDGNO.setEnabled(true);
					txtVSTNM.setEnabled(true);
					txtVSORG.setEnabled(true);
					txtPERVS.setEnabled(true);
					txtCLRBY.setEnabled(true);
					txtESCBY.setEnabled(true);
					txtPURPS.setEnabled(true);
					txtVSICT.setEnabled(true);
					txtVSREM.setEnabled(true);
					txtVSVHN.setEnabled(true);
					txtVSARA.setEnabled(true);
					txtSHFCD.setEnabled(true);
					txtDPTNM.setEnabled(false);
					tblCNTDTL.setEnabled(true);
					
					tblCNTDTL.cmpEDITR[TB3_VSITM].setEnabled(false);
					tblCNTDTL.cmpEDITR[TB3_VSOTM].setEnabled(false);
					tblCNTDTL.cmpEDITR[TB3_GINBY].setEnabled(false);
					tblCNTDTL.cmpEDITR[TB3_PRTCD].setEnabled(false);
					tblCNTDTL.cmpEDITR[TB3_VSPNO].setEnabled(false);
					tblVSTDTL1.cmpEDITR[TB1_VSOCT].setEnabled(false);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					txtVSPNO.setEnabled(true);
					txtVSTDT.setEnabled(false);
				    txtVSTDT1.setEnabled(false);
					tblCNTDTL.setEnabled(false);
				    txtVSTDT1.setText(cl_dat.M_strLOGDT_pbst);
					txtVSOCT.setEnabled(false);
					txtBDGNO.setEnabled(true);
					txtVSTNM.setEnabled(true);
					txtVSORG.setEnabled(true);
					txtPERVS.setEnabled(true);
					txtCLRBY.setEnabled(true);
					txtESCBY.setEnabled(true);
					txtPURPS.setEnabled(true);
					txtVSICT.setEnabled(true);
					txtVSREM.setEnabled(true);
					txtVSVHN.setEnabled(true);
					txtVSARA.setEnabled(true);
					txtSHFCD.setEnabled(true);
					txtDPTNM.setEnabled( false);
					tblCNTDTL.cmpEDITR[TB3_VSOCT].setEnabled(true);
					tblCNTDTL.cmpEDITR[TB3_REMDS].setEnabled(true);
					tblCNTDTL.cmpEDITR[TB3_CHKFL].setEnabled(true);
					tblVSTDTL1.cmpEDITR[TB1_CHKFL].setEnabled(true);
					tblVSTDTL1.cmpEDITR[TB1_VSOCT].setEnabled(true);
					tblVSTDTL3.cmpEDITR[TB4_CHKFL].setEnabled(true);
					tblVSTDTL3.cmpEDITR[TB4_VSTCT].setEnabled(true);
					tblVSTDTL3.cmpEDITR[TB4_REMDS].setEnabled(true);
					tblVSTDTL3.cmpEDITR[TB4_BADGE].setEnabled(true);
					tblVSTDTL3.cmpEDITR[TB4_VEHNO].setEnabled(true);

					tblVSTDTL4.cmpEDITR[TB5_CHKFL].setEnabled(true);
					tblVSTDTL4.cmpEDITR[TB5_VSTCT].setEnabled(true);
					tblVSTDTL4.cmpEDITR[TB5_REMDS].setEnabled(true);
					tblVSTDTL4.cmpEDITR[TB5_BADGE].setEnabled(true);
					//tblVSTDTL4.cmpEDITR[TB5_VEHNO].setEnabled(true);
					//tblVSTDTL3.cmpEDITR[TB4_VSARA].setEnabled(true);
					
					tblEXPDTL.cmpEDITR[TB6_AUTBY].setEnabled(true);

				}					
				else    
				{
					txtVSPNO.setEnabled(false);
					txtVSTDT1.setEnabled(true);
					txtVSPNO.setEnabled(true);
					txtVSTDT.setEnabled(false);
					txtBDGNO.setEnabled(false);
					txtVSTNM.setEnabled(false);
					txtVSORG.setEnabled(false);
					txtPERVS.setEnabled(false);
					txtCLRBY.setEnabled(false);
					txtESCBY.setEnabled(false);
					txtPURPS.setEnabled(false);
					txtVSICT.setEnabled(false);
					txtVSREM.setEnabled(false);
					txtVSVHN.setEnabled(false);
					txtVSARA.setEnabled(false);
					txtVSOCT.setEnabled(false);
					txtSHFCD.setEnabled(false);
				}
			//}
		}
		catch(Exception e)
		{
			setMSG(e,"setENBL ");
		}
	}
	// Method to Enable/Disable the components
	private void setENBL1(boolean L_ACTION)
	{
		try
		{
			cmbGINTP.setEnabled(!L_ACTION);
			txtGINNO.setEnabled(!L_ACTION);
			txtGOTDT.setEnabled(L_ACTION);
			txtGOTTM.setEnabled(L_ACTION);
			txtGOTBY.setEnabled(L_ACTION);
			txtORDRF.setEnabled(!L_ACTION);
			txtORDDT.setEnabled(!L_ACTION);
			txtORDQT.setEnabled(!L_ACTION);
			txtLRYNO.setEnabled(!L_ACTION);
			txtMATCD.setEnabled(!L_ACTION);
			txtMATDS.setEnabled(!L_ACTION);
			txtTPRCD.setEnabled(!L_ACTION);
			txtTPRDS.setEnabled(L_ACTION);
			txtREMGT.setEnabled(L_ACTION);
			txtREPDT.setEnabled(!L_ACTION);
			txtREPTM.setEnabled(!L_ACTION);
		}
		catch(Exception e)
		{
			setMSG(e,"Error in setENBL ");
		}
	}
	// Check valid Transporter Code
	private boolean vldPRTCD(String P_strPRTTP,String P_strPRTCD)
	{
		try
		{
			txtVENDS.setEnabled(false);
			M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
			M_strSQLQRY += " where PT_PRTTP = '"+P_strPRTTP+"' and PT_STSFL <> 'X'";
			M_strSQLQRY += " and PT_PRTCD = '" + P_strPRTCD + "'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			if(M_rstRSSET.next())
			{
				if(P_strPRTTP.equals("T"))
				{
					strTPRDS = M_rstRSSET.getString("PT_PRTNM");
					txtTPRCD.setText(P_strPRTCD);	
					txtTPRDS.setText(strTPRDS);	
				}
				else if(P_strPRTTP.equals("S"))
				{
					strVENDS = M_rstRSSET.getString("PT_PRTNM");
					txtVENCD.setText(P_strPRTCD);	
					txtVENDS.setText(strVENDS);	
					if(txtVENCD.getText().equals(strDUMCD_fn))
					{
						txtVENDS.setEnabled(true);
						txtVENDS.requestFocus();
					}
					else
						txtVENDS.setEnabled(false);
				}
				setMSG("",'N');
				M_rstRSSET.close();
				return true;			
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			txtTPRDS.setText("");
			return false;			
		}
		catch(Exception e)
		{
			setMSG(e,"Error in vldPRTCD ");
			return false;
		}
	}					   
	private boolean vldPORNO(String P_strPORNO)
	{
		try
		{
			M_strSQLQRY = "Select count(distinct PO_STRTP) L_CNT from MM_POMST,CO_PTMST ";
			M_strSQLQRY += " where PT_PRTTP = 'S' and po_vencd = pt_prtcd and isnull(PT_STSFL,'') <> 'X'";
			M_strSQLQRY += " and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_PORNO = '" + P_strPORNO + "'";
			if(txtVENCD.getText().trim().length() >0)
				M_strSQLQRY += " and PO_VENCD = '" + txtVENCD.getText().trim() + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			if(M_rstRSSET.next())
			if(M_rstRSSET.getInt("L_CNT") >1)
			{
				setMSG("More Than one P.O..,Enter Vendor code and then P.O. number..",'E');
				M_rstRSSET.close();
				txtPORNO.setText("");
				txtVENCD.requestFocus();
				return true;			
			}
			M_strSQLQRY = "Select PO_SHRDS,PO_VENCD,PT_PRTNM from MM_POMST,CO_PTMST ";
			M_strSQLQRY += " where PT_PRTTP = 'S' and po_vencd = pt_prtcd and isnull(PT_STSFL,'') <> 'X'";
			M_strSQLQRY += " and PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_PORNO = '" + P_strPORNO + "'";
			if(txtVENCD.getText().trim().length() >0)
				M_strSQLQRY += " and PO_VENCD = '" + txtVENCD.getText().trim() + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
				if(M_rstRSSET.next())
				{
					txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("PO_VENCD"),""));
					txtVENDS.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));	
					txtMATDS.setText(nvlSTRVL(M_rstRSSET.getString("PO_SHRDS"),""));
					setMSG("",'N');
					M_rstRSSET.close();
					txtMATDS.requestFocus();
					return true;			
				}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			txtVENDS.setText("");
			txtMATDS.setText("");
			return false;			
		}
		catch(Exception e)
		{
			setMSG(e,"Error in vldPRTCD ");
			return false;
		}
	}					   
	// Check valid Lorry No
	private boolean vldLRYNO(String P_strLRYNO)
	{
		try
		{
			flgLRYFND = false;
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			
			M_strSQLQRY = "Select LR_LRYNO,LR_TPRCD" + strJOINC + "'|'" + strJOINC;
			M_strSQLQRY += "PT_PRTNM strTPRDS from MM_LRMST,CO_PTMST ";
			M_strSQLQRY += " where LR_TPRCD = PT_PRTCD and PT_PRTTP = 'T' and LR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LR_STSFL <> 'X'";
			M_strSQLQRY += " and LR_LRYNO = '" + P_strLRYNO + "'";
			
			if(strGINTP.equals(strDESPT_fn))
			{					// Despatch
			   strTPRCD = txtTPRCD.getText().trim();
				if(strTPRCD.length() > 0)
					M_strSQLQRY += " and LR_TPRCD = '" + strTPRCD + "'";
			}
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int L_intRECCT =0;
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				strTPRDS = M_rstRSSET.getString("strTPRDS");
				
				flgLRYFND = true;
				L_intRECCT++;
			}
			if(L_intRECCT ==1)    
			{
				intINDEX = strTPRDS.indexOf('|');
				txtTPRCD.setText(strTPRDS.substring(0,intINDEX));
				txtTPRDS.setText(strTPRDS.substring(intINDEX+1));
			}
			if(L_intRECCT ==0)    // Not found
			{
				if(txtMATCD.getText().trim().equals(strSTYRN_fn))
				{
					txtTPRCD.setText(strSLFCD_fn);
					vldPRTCD("T",strSLFCD_fn);
				}
				else
				{
					txtTPRCD.setText(strDUMCD_fn);
					vldPRTCD("T",strDUMCD_fn);
				}
				txtTPRDS.setEnabled(true);
			}
			txtTPRCD.requestFocus();
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"vldLRYNO ");
			return false;
		}
		return true;
	}
	// Check valid Material Code
	private boolean vldMATCD(String P_strMATCD)
	{
		try
		{
			M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
			M_strSQLQRY += " where isnull(CT_STSFL,'') <> 'X'";
			M_strSQLQRY += " and CT_MATCD = '" + P_strMATCD + "'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next()){
				txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));
				M_rstRSSET.close();
				return true;			
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			txtMATDS.setText("");
			txtMATDS.setEnabled(true);
			return false;			
		}
		catch(Exception e)
		{
			setMSG(e,"vldMATCD ");
			return false;
		}
	}
	// Check valid Gate-In No.
	private boolean vldGINNO(String P_strGINNO,String P_strDOCTP)
	{
		try
		{
			M_strSQLQRY = "Select WB_DOCNO from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCNO = '" + P_strGINNO + "'";
			M_strSQLQRY += " and WB_DOCTP = '" + P_strDOCTP + "'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				if(strGINTP.equals(strRECPT_fn) || strGINTP.equals(strRAWMT_fn))					
					M_strSQLQRY += " and isnull(WB_DOCRF,'') = ''";
				else
					M_strSQLQRY += " and WB_STSFL = '0'";
				M_strSQLQRY += " and isnull(WB_STSFL,'') <> 'X'";
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				M_strSQLQRY += " and WB_STSFL not in('X','9')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			if(M_rstRSSET.next())
			{
				M_rstRSSET.close();
				return true;			
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			return false;			
		}
		catch(Exception e)
		{
			setMSG(e,"vldGINNO ");
			return false;
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);	
			if(M_objSOURC == tblGINLR)
			{ 
				intTBLCL = tblGINLR.getSelectedColumn();
				intTBLRW = tblGINLR.getSelectedRow();
				if(intROWCT > 0){
					if(intTBLCL == TBL_GOTDT || intTBLCL == TBL_GOTBY)
					{
						txtEDITR.setEnabled(true);
						setMSG("",'N');
					}
					else
						txtEDITR.setEnabled(false);
				}
				else
					txtEDITR.setEnabled(false);					
			}
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				
				if(L_KE.getSource().equals(txtLICNO))
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtLICNO";
					String L_ARRHDR[] = {"Licence No","Driver"};
					M_strSQLQRY = "select DV_LICNO,DV_DRVCD from MM_DVMST where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DV_STSFL <> 'X'";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
				}

				if(M_objSOURC== txtVSTNM1)
				{						
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					 {
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtVSTNM1";
						M_strSQLQRY = "Select PT_PRTNM,PT_PRTCD from CO_PTMST";
						M_strSQLQRY += " where isnull(PT_STSFL,'') <> 'X' AND PT_PRTTP ='N'";
						if(txtVSTNM1.getText().length() >0)
						    M_strSQLQRY += " AND PT_PRTNM like '"+txtVSTNM1.getText().trim() +"%'";
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Party Name","Party Code"},2,"CT");
					}
				setCursor(cl_dat.M_curDFSTS_pbst);
				}
				/*if(M_objSOURC == txtVSTNM2)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVSTNM2";
					String L_ARRHDR[] = {"Party Name","Party code"};
					M_strSQLQRY = "Select PT_PRTNM,PT_PRTCD from CO_PTMST";
					M_strSQLQRY += " where isnull(PT_STSFL,'') <> 'X' AND PT_PRTTP ='N'";
					if(txtVSTNM2.getText().trim().length() >0)
					    M_strSQLQRY += " AND PT_PRTNM like '"+txtVSTNM2.getText().trim() +"%'";
					M_strSQLQRY += " order by  PT_PRTNM ";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
				}*/
				if(M_objSOURC == txtVSPNO)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVSPNO";
					String L_ARRHDR[] = {"Visitor No","Visitor Name","Date"};
					M_strSQLQRY = "Select VS_VSPNO,VS_VSTNM,VS_VSTDT from HR_VSTRN ";
					M_strSQLQRY += " where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,'') <> 'X' AND VS_VSTTP ='01' and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
					if(txtVSPNO.getText().trim().length() >0)
					    M_strSQLQRY += " AND VS_VSPNO like '"+txtVSPNO.getText().trim() +"%'";
					M_strSQLQRY += " order by  VS_VSPNO DESC";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
				}
				//************************ Adding 28-05-2007
				if(M_objSOURC == txtPERVS)		
				{
				    cl_dat.M_flgHELPFL_pbst = true;
			        M_strHLPFLD="txtPERVS";
			        String L_ARRHDR[] ={"Emp Name","Department name","Department Code","Emp.No."};
			        M_strSQLQRY = "select SUBSTRING(EP_FSTNM,1,1) + ' ' + SUBSTRING(EP_MDLNM,1,1) + ' ' + EP_LSTNM , EP_DPTCD,EP_DPTNM,EP_EMPNO FROM HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null";
			        cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				    
				}
				
				 if(M_objSOURC==txtVSARA)
				    {
				        cl_dat.M_flgHELPFL_pbst = true;
				        M_strHLPFLD="txtVSARA";
				        String L_ARRHDR[] ={"Area","Description"};
				        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
						M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXVSA'  ";
						cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
				        
				    }
				//************************ Adding 28-05-2007
				if(M_objSOURC == txtGINNO)			// Gate-In No
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtGINNO";
					String L_ARRHDR[] = {"Gate-In No.","Lorry No","Gate-In Time"};
		
					M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_GINDT from MM_WBTRN ";
					M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						if(strGINTP.equals(strRECPT_fn) || strGINTP.equals(strRAWMT_fn))					
							M_strSQLQRY += " and isnull(WB_DOCRF,'') = ''";
						else
							M_strSQLQRY += " and WB_STSFL = '0'";
						M_strSQLQRY += " and isnull(WB_STSFL,'') <> 'X'";
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						M_strSQLQRY += " and WB_STSFL not in('X','9')";
					if(txtGINNO.getText().trim().length() >0)
						M_strSQLQRY += " and WB_DOCNO like '"+txtGINNO.getText().trim() +"%' ";
					M_strSQLQRY += " order by isnull(WB_GINDT,current_timestamp) desc";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,3,"CT");
				}
				else if(M_objSOURC == txtLRYNO)		// Lorry No
				{
					strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtLRYNO";
					String L_ARRHDR[] = {"Lorry No","Transporter"};
					M_strSQLQRY = "Select LR_LRYNO,LR_TPRCD " + strJOINC + "'|'" + strJOINC;
					M_strSQLQRY += "PT_PRTNM from MM_LRMST,CO_PTMST ";
					M_strSQLQRY += " where LR_TPRCD = PT_PRTCD and PT_PRTTP = 'T' and LR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LR_STSFL <> 'X'";
					if(strGINTP.equals(strDESPT_fn)){					// Despatch
						strTPRCD = txtTPRCD.getText().trim();
						if(strTPRCD.length() > 0)
							M_strSQLQRY += " and LR_TPRCD = '" + strTPRCD + "'";
					}
					M_strSQLQRY += " order by LR_LRYNO";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtMGPNO)		// GATE PASS NO
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMGPNO";
					String L_ARRHDR[] = {"Gate Pass No"};
					M_strSQLQRY = "Select distinct GP_MGPNO from MM_GPTRN WHERE ";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						M_strSQLQRY += " GP_STSFL ='4' AND";
					}
					M_strSQLQRY += "  GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_MGPTP ='"+cmbMGPTP.getSelectedItem().toString().substring(0,2)+"'";
					M_strSQLQRY += " AND GP_STRTP ='"+cmbSTRTP.getSelectedItem().toString().substring(0,2)+"'";
					if(txtMGPNO.getText().trim().length() >0)
						M_strSQLQRY += " AND GP_MGPNO like '"+txtMGPNO.getText().trim()+"%'";
					M_strSQLQRY += " order by GP_MGPNO DESC";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
				}
				else if(M_objSOURC == txtMATCD)
				{		
					M_strHLPFLD = "txtMATCD";
					cl_dat.M_flgHELPFL_pbst = true;
					String L_ARRHDR[] = {"Code","Material"};
					if(strGINTP.equals(strRAWMT_fn))
					{
						M_strSQLQRY = "Select Distinct TK_MATCD,TK_MATDS from MM_TKMST where TK_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' ";
						M_strSQLQRY += " order by TK_MATDS";
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
					}
					else if(strGINTP.equals(strRECPT_fn))
					{
						M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
						M_strSQLQRY += " where isnull(CT_STSFL,'') <> 'X'";
						if(txtMATCD.getText().length() >0)
							M_strSQLQRY +=" CT_MATCD like '"+txtMATCD.getText().trim() +"%'";
						M_strSQLQRY += " order by CT_MATDS";
						
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
					}
					else if(strGINTP.equals(strSLSRT_fn))
					{
						M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST ";
						M_strSQLQRY += " where isnull(PR_STSFL,'') <> 'X'";
						if(txtMATCD.getText().length() >0)
							M_strSQLQRY +=" PR_PRDCD like '"+txtMATCD.getText().trim() +"%'";
						M_strSQLQRY += " order by PR_PRDDS";
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
					}
					else
					{
						M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
						M_strSQLQRY += " where isnull(CT_STSFL,'') <> 'X' and CT_MATCD like '68%'";
						M_strSQLQRY += " order by CT_MATDS";
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
					}
				}
				else if(M_objSOURC == txtTPRCD)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTPRCD";
					String L_ARRHDR[] = {"Code","Transporter"};
					M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
					M_strSQLQRY += " where PT_PRTTP = 'T' and PT_STSFL <> 'X'";
					M_strSQLQRY += " order by PT_PRTCD";
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtVENCD)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVENCD";
					String L_ARRHDR[] = {"Code","Description"};
					if(strGINTP.equals(strSLSRT_fn))
					{
						M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
						M_strSQLQRY += " where PT_PRTTP = 'C' and PT_STSFL <> 'X'";
					}
					else
					{
						M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST ";
						M_strSQLQRY += " where PT_PRTTP = 'S' and PT_STSFL <> 'X'";
					}
					M_strSQLQRY += " order by PT_PRTCD";
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtPORNO)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPORNO";
					String L_ARRHDR[] = {"P.O. Number","Vendor","P.O Short Description"};
					M_strSQLQRY = "Select PO_PORNO,PO_VENCD,PO_SHRDS from MM_POMST where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,' ') <>'X' ";
					if(txtPORNO.getText().trim().length() >0)
					M_strSQLQRY += " and PO_PORNO like '"+txtPORNO.getText().trim() +"%'";
					if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " AND PO_VENCD ='"+txtVENCD.getText().trim() +"' ";
					M_strSQLQRY += " order by PO_PORNO desc";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT",new int[]{80,80,350});
				}
				
				/**Fetch Authority persons & Shift Incharge in txtAUTBY for Exit Pass entry ***/
				else if(M_objSOURC == txtAUTBY)
				{
					M_strHLPFLD = "txtAUTBY";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					//M_strSQLQRY = " select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN','HR"+cl_dat.M_strCMPCD_pbst+"EXA') and SUBSTRING(cmt_codcd,1,4)='"+ tblEXPDTL.getValueAt(tblEXPDTL.getSelectedRow(),TB6_EMPNO).toString()+"'";
					M_strSQLQRY = " select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN','HR"+cl_dat.M_strCMPCD_pbst+"EXA') and SUBSTRING(cmt_codcd,1,4)='"+ tblEXPDTL.getValueAt(tblEXPDTL.getSelectedRow(),TB6_EMPNO).toString()+"'";
					M_strSQLQRY += " union select distinct cmt_shrds cmt_ccsvl from co_cdtrn where cmt_cgmtp ='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXSIC' and cmt_codcd in (select ep_empno from hr_epmst where ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_inost,'') = 'I')";

					if(tblEXPDTL.getValueAt(tblEXPDTL.getSelectedRow(),TB6_AUTBY).toString().length()>0)				
						M_strSQLQRY += " AND cmt_ccsvl like '"+ tblEXPDTL.getValueAt(tblEXPDTL.getSelectedRow(),TB6_AUTBY).toString()+"%'";
					M_strSQLQRY += " order by cmt_ccsvl";
					//System.out.println("txtAUTBY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Authority"},1,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
					
				}	
				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if((M_objSOURC == cl_dat.M_btnHLPOK_pbst) || (M_objSOURC == cl_dat.M_txtHLPPOS_pbst))
					exeHLPOK();
				else if(M_objSOURC == txtGINNO)
				{	
					strGINNO = txtGINNO.getText().trim();
					strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
					if(strGINNO.length() > 0)
					{
						if(vldGINNO(strGINNO,strGINTP))
						{
							exeGETREC(strGINTP,strGINNO,"1");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							{		
								if(strSTSFL.equals("0"))
								{
									setENBL(true);
									txtGINDT.requestFocus();
								}
                                else if(strSTSFL.equals("9"))
								{
								    setMSG("Gate out is done,modification not allowed..",'E');
								}
								else
								{
									setENBL1(true);
									txtGOTDT.requestFocus();
								}
							}
						}
						else
						{
							txtGINNO.requestFocus();
							setMSG("Invalid Gate-In No. Press F1 for help",'E');
						}
					}
				}//Visitors Data
				else if(M_objSOURC == txtTODAT)
				{
					getOTVST();
				}
				else if(M_objSOURC == txtGINDT)
				{
					txtGINTM.requestFocus();
				}
				else if(M_objSOURC == txtGINTM)
				{		
					strGINDT = txtGINDT.getText().trim()+" "+txtGINTM.getText().trim();
					strCURDT = cl_dat.getCURDTM();
					if(strGINDT.length() > 0)
					{
						if(M_fmtLCDTM.parse(strGINDT).compareTo(M_fmtLCDTM.parse(strCURDT))> 0)
						{		
							txtGINDT.requestFocus();
								setMSG("Gate-In Date/Time can not be greater than current Date/Time ",'E');	
						}
						else
						{
							txtGINBY.requestFocus();
							setMSG("",'N');	
						}
					}
					else
					{
						txtGINDT.requestFocus();
						setMSG("Gate-In Date can not be empty",'E');
					}
				}
				else if(M_objSOURC == txtGINBY)		// Gate-In Officer
					txtORDRF.requestFocus();
				else if(M_objSOURC == txtVENDS)		
					txtMATCD.requestFocus();
				else if(M_objSOURC == txtORDRF)		// Doc.Ref
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtGINDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						txtGINTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
						//txtREPDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						//txtREPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					}
					txtORDDT.requestFocus();
				}
				else if(L_KE.getSource().equals(txtORDDT))
				{		
					strORDDT = txtORDDT.getText().trim();
					if(strORDDT.length() > 0)
					{
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(Calendar.DATE,-7);
						String L_strDTCHK = M_fmtLCDAT.format(M_calLOCAL.getTime());
	
						if(strGINTP.equals(strRAWMT_fn))
						{
							if(M_fmtLCDAT.parse(L_strDTCHK).compareTo(M_fmtLCDAT.parse(strORDDT))>0)		
							{
								setMSG("Please check the challan date..",'E');
							}
							else
							{
								if(strORDDT.equals("null"))
									txtORDDT.setText("");
								else
									txtORDDT.setText(strORDDT);
								setMSG("",'N');
								txtORDQT.requestFocus();
							}
						}
						else
						{
							if(strORDDT.equals("null"))
									txtORDDT.setText("");
								else
									txtORDDT.setText(strORDDT);
							setMSG("",'N');
							txtORDQT.requestFocus();
						}
					}
					else
					{
						txtMATCD.requestFocus();
						setMSG("",'N');
					}
				}
				else if(M_objSOURC == txtORDQT)		// Doc.Qty
				{
					if((strGINTP.equals(strRECPT_fn))||(strGINTP.equals(strSLSRT_fn)))
						txtVENCD.requestFocus();
					else
						txtMATCD.requestFocus();
				}
				else if(M_objSOURC == txtPORNO)		// Doc.Qty
				{
					txtMATDS.requestFocus();
				}
				else if(M_objSOURC == txtVENCD)		// Doc.Qty
				{
					txtVENCD.setText(txtVENCD.getText().trim().toUpperCase());
					if(strGINTP.equals(strRECPT_fn))
					{
						if(vldPRTCD("S",txtVENCD.getText().trim()))
						{
							if(txtVENCD.getText().equals(strDUMCD_fn))
								txtVENDS.requestFocus();
							else
							{
								if(txtPORNO.getText().trim().length() >0)
									txtMATCD.requestFocus();
								else
									txtPORNO.requestFocus();
							}
						}
					}
					else if(strGINTP.equals(strSLSRT_fn))
					{
						if(vldPRTCD("C",txtVENCD.getText().trim()))
							txtPORNO.requestFocus();
					}
				}
				else if(M_objSOURC == txtGOTTM)
				{		
					strGOTDT = txtGOTDT.getText().trim()+ " "+txtGOTTM.getText().trim();
					strCURDT = cl_dat.getCURDTM();
					if(strGOTDT.length() > 0)
					{
						if(M_fmtLCDTM.parse(strGOTDT).compareTo(M_fmtLCDTM.parse(strCURDT))> 0) 
						{				
							txtGOTTM.requestFocus();
								setMSG("Gate-Out Date/Time can not be greater than current Date/Time ",'E');	
						}
						else
						{
							txtGOTBY.requestFocus();
							setMSG("",'N');	
						}
					}
					else
					{
						txtGOTTM.requestFocus();
						setMSG("Gate-Out Date can not be empty",'E');
					}
				}
				else if(M_objSOURC == txtGOTBY)		// Gate-Out Officer
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else if(M_objSOURC == txtMATCD)		// Material Code
				{
					strMATCD = txtMATCD.getText().trim();
					strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
					if(strGINTP.equals(strRAWMT_fn))		//Tanker
					{
						if(vldMATTF(strMATCD))
							txtREPDT.requestFocus();
						else
						{
							txtMATDS.requestFocus();
							txtMATCD.setText("");
						}
					}
					else if(strGINTP.equals(strRECPT_fn))		
					{
						if(txtMATCD.getText().length() ==10)
						{
							if(vldMATCD(strMATCD))
							txtREPDT.requestFocus();
							else
							{
								txtMATDS.requestFocus();
								txtMATCD.setText("");
							}
						}
						else
						{
							txtMATCD.setText(strMATCD_fn);
							txtMATDS.setText("");
							txtMATDS.requestFocus();
						}
					}
					else
					{
						if(vldMATCD(strMATCD))
							txtREPDT.requestFocus();
						else
						{
							txtMATDS.requestFocus();
							txtMATCD.setText("");
						}
					}
				}
				else if(M_objSOURC == txtMATDS)		// Material Description
					txtREPDT.requestFocus();					
				else if(M_objSOURC == txtREPDT)		// Reporteing Date/Time
				{	
					strREPTM = txtREPDT.getText().trim()+" "+txtREPTM.getText().trim();
					strCURDT = cl_dat.getCURDTM();
					if(strREPTM.length() > 0)
					{
						if(M_fmtLCDTM.parse(strREPTM).compareTo(M_fmtLCDTM.parse(strCURDT))>0)
						{
							txtREPDT.requestFocus();
							setMSG("Reporting Time can not be greater than current Date/Time ",'E');
						}
						else
						{
							txtREPTM.requestFocus();
							//txtLRYNO.requestFocus();
							setMSG("",'N');
						}
					}
					else
					{
						txtREPDT.requestFocus();
						setMSG("Reporting Time can not be empty",'E');
					}
				}
				else if(M_objSOURC == txtREPTM)		// Reporteing Date/Time
				{
					txtLRYNO.requestFocus();
				}
				else if(M_objSOURC == txtLRYNO)		// Lorry No
				{	
					if(!strGINTP.equals(strRECPT_fn))
						vldLRYNO(txtLRYNO.getText().trim());
					else
						txtTPRCD.requestFocus();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtGINDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						txtGINTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
						//txtREPDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						//txtREPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					}
					txtLRYNO.setText(txtLRYNO.getText().trim().toUpperCase());
				}
				else if(M_objSOURC == txtTPRCD)
			    {		
					strTPRCD = txtTPRCD.getText().trim().toUpperCase();
					if(!strGINTP.equals(strRECPT_fn))
					if(!vldPRTCD("T",strTPRCD))
					{
						txtTPRDS.setText("");
						txtTPRCD.requestFocus();
						setMSG("Invalid Transporter. Press F1 for help",'E');
					}
                    else
                    {
                        if(strGINTP.equals(strDESPT_fn))
                            txtLCCQT.requestFocus();
                        else
                             txtREMGT.requestFocus(); 
                    }
                    
			    } 
			    else if(M_objSOURC == txtLCCQT)		
				{
				     txtREMGT.requestFocus(); 
				}
				else if(M_objSOURC == txtREMGT)		// Remark by Gate-Officer
				{
				    txtGINCT.requestFocus();
				    setMSG("Enter the number of persons along with the vehicle",'N');
				}
				else if(M_objSOURC == txtGINCT)		
				{
				    txtLICNO.requestFocus();
				    setMSG("Enter Licence No. Of Driver",'N');
				}
				else if(M_objSOURC == txtLICNO)		
				{
				    txtLICBY.requestFocus();
				    //setMSG("Enter Licence Issuing Authority ",'N');
				}
				else if(M_objSOURC == txtLICBY)		
				{
				    txtLVLDT.requestFocus();
				    //setMSG("Enter Licence Validity",'N');
				}
				else if(M_objSOURC == txtLVLDT)		
				{
				    txtDRVNM.requestFocus();
				    //setMSG("Enter Driver Name",'N');
				}
				else if(M_objSOURC == txtDRVNM)		
				{
				    txtREMDS.requestFocus();
					//cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else if(M_objSOURC == txtREMDS)		
				{
					txtREMDS.setText(txtREMDS.getText().replace("'","`"));
				    cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				
				else if(M_objSOURC == txtFRMDT)		// From date
					txtFRMTM.requestFocus();
				else if(M_objSOURC == txtFRMTM)		// From time
					txtTORDT.requestFocus();
				else if(M_objSOURC == txtTORDT)		
					txtTORTM.requestFocus();
				else if(M_objSOURC == txtTORTM)
			    {		
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					tblGOTLR.clrTABLE();
					getOTLRY();
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtVSTDT)
				{
				    txtBDGNO.requestFocus();
				    setMSG("Enter Badge No..",'N');
				}
				else if(M_objSOURC == txtBDGNO)
				{
					txtSHFCD.requestFocus();
				    setMSG("Enter Visitor Shift..",'N');
				}
				else if(M_objSOURC == txtSHFCD)
				{
					txtVSTNM.requestFocus();
					setMSG("Enter Visitor Name",'N');
				}
				else if(M_objSOURC == txtVSTNM)
				{
					txtVSORG.requestFocus();
				    setMSG("Enter Orgainisation..",'N');
				}
				else if(M_objSOURC == txtVSORG)
				{
					txtVSICT.requestFocus();
				    setMSG("Enter the Visitor Count..",'N');
				}
    			else if(M_objSOURC == txtVSICT)
				{
				    txtPURPS.requestFocus();
					setMSG("Enter the Purpose..",'N');
				}
			    else if(M_objSOURC == txtPURPS)
				{
					txtVSARA.requestFocus();
				    setMSG("Enter the Area of Visit..",'N');
				}
			    else if(M_objSOURC == txtVSOCT)
				{
					txtVSARA.requestFocus();
					setMSG("Enter the Visitor Area..",'N');
				}
			    else if(M_objSOURC == txtVSARA)
				{
					if(vldVSARA())
					{    
					    txtVSVHN.requestFocus();
					    setMSG("Visitor Vehicle No..",'N');
					}
					else
					{
					    setMSG("Please Press F1 ...",'E');
					    txtVSARA.requestFocus();
					    
					}
				}
				else if(M_objSOURC == txtVSVHN)
				{
					txtPERVS.requestFocus();
				    setMSG("Person To Visit..",'N');
				}
				 else if(M_objSOURC == txtPERVS)
				{
				     //************************ Adding 28-05-2007
				     if(txtPERVS.getText().length()  >0)
				      {    
					       if( strPERVS.equals(txtPERVS.getText().toString() .trim() ) )
					       {    
					           setMSG("Valid Person To Visit",'N');
					           txtCLRBY.requestFocus();
					       } 
					       else
					       {   
				           setMSG("Please Press F1 for Help Screen Display",'E');
				           txtPERVS.requestFocus() ;
					       }
				      }else{setMSG("Please Press F1 Key " ,'E'); txtPERVS.requestFocus() ;}
				     //************************ Adding 28-05-2007
				     
					//txtCLRBY.requestFocus();
				   // setMSG("Cleared By..",'N');
				}
				else if(M_objSOURC == txtCLRBY)
				{
					txtESCBY.requestFocus();
				    setMSG("Escorted By..",'N');
				}
				else if(M_objSOURC == txtESCBY)
				{
					txtVSREM.requestFocus();
				    setMSG("Enter Remark..",'N');
				}
				else if(M_objSOURC == txtVSREM)
				{
					txtVSREM.requestFocus();
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"keyPressed ");
		}
	}
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			super.mouseReleased(L_ME);
			if(M_objSOURC == tblGINLR)
			{
				intTBLCL = tblGINLR.getSelectedColumn();
				intTBLRW = tblGINLR.getSelectedRow();
				if(intTBLCL == TBL_OUTFL)
				{
					strGINTP1 = String.valueOf(cmbGINTP1.getSelectedItem()).substring(0,2);
					if(strGINTP1.equals(strRAWMT_fn))
						tblGINLR.setValueAt(getTARWT(intTBLRW),intTBLRW,TBL_GOTDT);
					else
						tblGINLR.setValueAt(cl_dat.M_txtCLKDT_pbst.getText().trim() +" "+cl_dat.M_txtCLKTM_pbst.getText().trim(),intTBLRW,TBL_GOTDT);
					tblGINLR.setValueAt(cl_dat.M_strUSRCD_pbst,intTBLRW,TBL_GOTBY);				
				}
			}
			if(M_objSOURC == jtpGATEIO)
			{
				intSELPG = jtpGATEIO.getSelectedIndex();
				if(intSELPG == 1)
				{
					if(intROWCT > 0)
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					else
						cl_dat.M_btnSAVE_pbst.setEnabled(false);
					//cl_dat.M_cmbOPTN_pbst.setEnabled(false);
				}
				else if(intSELPG == 0)
				{
					cl_dat.M_cmbOPTN_pbst.setEnabled(true);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))						
						cl_dat.M_btnSAVE_pbst.setEnabled(false);
					else 
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
				}
				/**call Exit Pass method to display record after selection of Exit pass entry tab**/
				if(intSELPG == 4)
	    		{
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
	    		    {
				    	tblEXPDTL.cmpEDITR[TB6_CHKFL].setEnabled(true);
				    	txtDOCDT.setText(cl_dat.M_strLOGDT_pbst);
				    	getEXPDL();
	    		    }	
			    }
				
			}
			
			//visitors.......
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}
	
	public void mouseClicked(MouseEvent L_ME)
	{
		if(L_ME.getSource().equals(jtpGATEIO))
		{
			if(jtpGATEIO.getSelectedIndex() == 1)
			{
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				strGINTP1 = String.valueOf(cmbGINTP1.getSelectedItem()).substring(0,2);
				
				if(strGINTP.equals(strRAWMT_fn))
				{
					// changed on 06/03/2004 as per the work request
					//if((flgRMADD)||(!strGINTP1.equals(strRAWMT_fn)))
					if(flgRMADD)
					{
						cmbGINTP1.setSelectedIndex(Integer.parseInt(strGINTP)-1);
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						getINLRY();
						getOTLRY();
						flgRMADD = false;
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				else if(strGINTP.equals(strDESPT_fn))
				{
					if(flgPSADD)
					{
						cmbGINTP1.setSelectedIndex(Integer.parseInt(strGINTP)-1);
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						getINLRY();
						getOTLRY();
						flgPSADD = false;
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				if(strGINTP.equals(strRECPT_fn))
				{
					if(flgRCADD)
					{
						cmbGINTP1.setSelectedIndex(Integer.parseInt(strGINTP)-1);
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						getINLRY();
						getOTLRY();
						flgRCADD = false;
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				if(strGINTP.equals(strOTHER_fn))
				{
					if(flgOTADD)
					{
						cmbGINTP1.setSelectedIndex(Integer.parseInt(strGINTP)-1);
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						getINLRY();
						getOTLRY();
						flgOTADD = false;
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				
			}
			
		}
		if(L_ME.getSource().equals(jtpMANTAB))
		{
		    if(jtpMANTAB.getSelectedIndex() == 0)
    		{
    		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))						
    		    {
    		        txtVSTDT.setText(cl_dat.M_strLOGDT_pbst);
    		        txtVSTDT.requestFocus();
    		    }
    		    else
    		        txtVSPNO.requestFocus();
    		}
    		if(jtpMANTAB.getSelectedIndex() == 1)
    		{
    			tblVSTDTL1.clrTABLE();
    			tblVSTDTL2.clrTABLE();
    			tblVSTDTL1.clrTABLE();
    			tblVSTDTL2.clrTABLE();
    			
    			
    			getINVST();
    			if(txtFMDAT.getText().length() ==0)
    			    txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
    			if(txtTODAT.getText().length() ==0)
    			    txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
    			getOTVST();
    		}
    		if(jtpMANTAB.getSelectedIndex() == 2)
    		{
    			tblCNTDTL.clrTABLE();
    			tblCNTDTL.clrTABLE();
    			getINCNT();
    		}
    		if(jtpMANTAB.getSelectedIndex() == 3)
    		{
    		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst )))						
    		    {
		   
	    		    tblVSTDTL3.clrTABLE();
	    			tblVSTDTL4.clrTABLE();
	    			getAUTHVST();
	    			getWTGVST();
    		    }
    		   	
    		}
    		
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if((M_objSOURC ==cl_dat.M_cmbOPTN_pbst)||(M_objSOURC == cl_dat.M_btnUNDO_pbst))
			{
				clrCOMP();
				//Added by ATC
				if(tblGPDTL.isEditing())
					tblGPDTL.getCellEditor().stopCellEditing();
				//**
				tblGPDTL.clrTABLE();
				strGINTP = cmbGINTP.getSelectedItem().toString().substring(0,2);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
				{
					setMSG("Select an Option.",'N');
					setENBL(false);
					cmbGINTP.setEnabled(false);
					txtGINNO.setEnabled(false);		
				}
				else if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					setENBL(false);
					txtGINNO.requestFocus();
				}
				else
				{
					strSTSFL = "";
					setENBL(true);
						
					cmbGINTP.setEnabled(true);
					txtGOTDT.setEnabled(false);
					txtGOTTM.setEnabled(false);
					txtGOTBY.setEnabled(false);
					btnPRINT.setEnabled(false);
					txtGINDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
					txtGINTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					txtREPDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
					txtREPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					txtGINBY.setText(cl_dat.M_strUSRCD_pbst.trim());
					if(strGINTP.equals(strRAWMT_fn))   // Raw Material
					{						
						btnPRINT.setEnabled(true);
						setCHLDT();
						txtORDRF.requestFocus();
					}
					else if(strGINTP.equals(strDESPT_fn))					// Despatch
					{
						txtMATDS.setText("POLYSTYRENE");
						txtLRYNO.requestFocus();
					}
					else if(strGINTP.equals(strSLSRT_fn))					// Sales Return
					{
						txtMATDS.setText("POLYSTYRENE");
						txtORDRF.requestFocus();
					}
					else if(strGINTP.equals(strOTHER_fn))					// Other
					{
						txtMATDS.setText("Scrap");
						txtORDRF.requestFocus();
					}
					else
					{
						txtMATCD.setText(strMATCD_fn);
						txtORDRF.requestFocus();
					}
				}
				if((M_objSOURC ==cl_dat.M_cmbOPTN_pbst))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)    				
					{
						if(jtpMANTAB.getSelectedIndex() == 1)
						{
							tblVSTDTL1.clrTABLE();
        					tblVSTDTL2.clrTABLE();
        					
        					
							txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
							txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
        					getINVST();
        					getOTVST();
						}
        				else if(jtpMANTAB.getSelectedIndex() == 2)
        				{
        					tblCNTDTL.clrTABLE();
        					getINCNT();
						}
        				if(jtpMANTAB.getSelectedIndex() == 3)
        	    		{
        				     if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst )))						
        		    		    {
            				     tblVSTDTL3.clrTABLE();
            	    			 tblVSTDTL4.clrTABLE();
            	    			 getAUTHVST();
            	    			 getWTGVST();
        		    		    }
        	    		}
    				}
    			}
			}
			else if(M_objSOURC ==btnAUTEP)
		    {
				exeAUTEP();
			}
			else if(M_objSOURC ==chkEXPRT)
		    {
				if((chkEXPRT.isSelected())&&(strGINTP.equals(strDESPT_fn)))
					txtREMGT.setText("EXPORT");
				else
					txtREMGT.setText("");
			}
			else if(M_objSOURC ==chkREMDS)
		    {
				if(txtREMDS.getText().length()>0)
					JOptionPane.showMessageDialog(null,"REMARK  :  \n"+txtREMDS.getText(), "Verify", JOptionPane.INFORMATION_MESSAGE);	
			}
			else if(M_objSOURC ==txtMATDS)
			{
				if((txtMATDS.getText().indexOf("'")>=0)||(txtMATDS.getText().indexOf("\"")>=0)||(txtMATDS.getText().indexOf("\\")>=0))
					setMSG("Special characters LIKE ' are not allowed in material desc ",'E');
			}
			else if(M_objSOURC ==btnPRINT)
		    {
				mm_rptrs omm_rptrs = new mm_rptrs();
				int L_SELOPT = JOptionPane.showConfirmDialog(this,"Insert Tanker Routing Slip in Printer","Tanker Routing Slip ",JOptionPane.OK_CANCEL_OPTION); 
				if(L_SELOPT ==0)
					txtORDRF.requestFocus();
				omm_rptrs.prnGINVL(strPGINTP,strPGINNO);
				btnPRINT.setEnabled(false);
				strPGINTP = "";
				strPGINNO = "";
				if(txtGINNO.isEnabled())
				{
					txtGINNO.requestFocus();
				}
				else
					txtORDRF.requestFocus();
			}
			else if(M_objSOURC == txtLICNO)
			{	
				txtLICNO.setText(txtLICNO.getText().trim().toUpperCase());
				exeGETDRV(txtLICNO.getText().trim());				
			}	
			else if(M_objSOURC == txtLICBY)
				txtLVLDT.requestFocus();
			else if(M_objSOURC == txtLVLDT)
				txtDRVNM.requestFocus();
			else if(M_objSOURC == txtDRVNM)
			{	
				txtDRVNM.setText(txtDRVNM.getText().trim().toUpperCase());
				txtDRVCD.setText(getDRVCD(txtDRVNM.getText().trim()));
				txtREMDS.requestFocus();
			}	
			
			if(M_objSOURC ==cmbGINTP1)
			{
				if(flgSCRVSB)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
				  tblGINLR.clrTABLE();
				  getINLRY();
				  tblGOTLR.clrTABLE();
				  getOTLRY();
				  this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			if(M_objSOURC == cmbGINTP)
			{
				if(flgSCRVSB)
				{
					strSTSFL = "";
					strGINTP = cmbGINTP.getSelectedItem().toString().substring(0,2);
					chkEXPRT.setSelected(false);
					txtREMGT.setText("");
					if(strGINTP.equals(strRAWMT_fn))						// Raw Material
					{
						txtMATDS.setText("");
						setCHLDT();
					}
					else if(strGINTP.equals(strRECPT_fn))					// Receipt 
					{
						txtMATCD.setText(strMATCD_fn);
						txtMATDS.setText("");
					}
					else if(strGINTP.equals(strDESPT_fn))					// Despatch
					{
						txtMATCD.setText("");
						txtMATDS.setText("POLYSTYRENE");
					}
					else if(strGINTP.equals(strSLSRT_fn))					// Sales Return
					{
						txtMATCD.setText("");
						txtMATDS.setText("POLYSTYRENE");
					}
					else if(strGINTP.equals(strOTHER_fn))					// Other
					{
						txtMATCD.setText("");
						txtMATDS.setText("Scrap");
					}
					txtORDRF.setText("");
					txtORDQT.setText("");
					txtTPRCD.setText("");
					txtTPRDS.setText("");
					txtLRYNO.setText("");			
					txtPORNO.setText("");			
					txtVENCD.setText("");			
					txtVENDS.setText("");			
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						if(strGINTP.equals(strDESPT_fn))
							txtLRYNO.requestFocus();
						else
							txtORDRF.requestFocus();
						txtREPDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						txtREPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					}
					else
						txtGINNO.requestFocus();
				}
			}
			if(M_objSOURC == txtPORNO)
			{
				if(txtPORNO.getText().trim().length() >0)
				{
					if(!strGINTP.equals(strSLSRT_fn))
					{
						if(!vldPORNO(txtPORNO.getText().trim()))
							setMSG("Invalid P.O. number..",'E');
						//else txtMATDS.requestFocus();
					}
					else
						txtMATCD.requestFocus();
				}
			}
			if(M_objSOURC == txtMGPNO)
			{
				try
				{
					boolean L_blnFIRST = true;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				   	if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						setMSG("Please select the Modification Option ..",'E');
						return;
					}
					clrGPTAB();
					M_strSQLQRY = "SELECT GP_MGPTP,GP_MGPDT,GP_DPTCD,GP_GOTBY,GP_GOTDT,GP_VEHNO,GP_VEHDS,"
								+"GP_VENCD,GP_VENNM,GP_MATCD,GP_ISSQT,GP_STSFL,CT_MATDS,CT_UOMCD,CMT_CODDS FROM MM_GPTRN,"
								+"CO_CDTRN,CO_CTMST WHERE GP_DPTCD = CMT_CODCD AND CMT_CGMTP ='SYS' and cmt_CGSTP ='COXXDPT' "
								+"AND CT_MATCD = GP_MATCD AND GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_MGPNO ='"+txtMGPNO.getText().trim()+"' ";
					M_strSQLQRY +="AND GP_MGPTP ='"+cmbMGPTP.getSelectedItem().toString().substring(0,2)+"'";			
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						M_strSQLQRY +="AND isnull(GP_STSFL,'') ='4' AND GP_STRTP ='"+cmbSTRTP.getSelectedItem().toString().substring(0,2)+"' ";
						txtVEHNO.setEnabled(true);
						txtCRRNM.setEnabled(true);
					}
					else			  
					{
						M_strSQLQRY +="AND isnull(GP_STSFL,'') <> 'X'";
						txtVEHNO.setEnabled(false);
						txtCRRNM.setEnabled(false);
					}
				//	System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					int i = 0;
					if(M_rstRSSET !=null)
					{
						while(M_rstRSSET.next())
						{
	
							if(L_blnFIRST)
							{
								L_blnFIRST = false;
								txtDPTCD.setText(nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),""));
								txtVEHNO.setText(nvlSTRVL(M_rstRSSET.getString("GP_VEHNO"),""));
								txtCRRNM.setText(nvlSTRVL(M_rstRSSET.getString("GP_VEHDS"),""));
								if(M_rstRSSET.getString("GP_MGPDT")!=null)
									txtMGPDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("GP_MGPDT")));
								//**Added by ATC
								txtGPVEN.setText(nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),""));
								txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),""));
								txtGPSTS.setText(cl_dat.getPRMCOD("CMT_CODDS","STS","MMXXGPS",nvlSTRVL(M_rstRSSET.getString("GP_STSFL"),"")));
							
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
								{
									
									txtGPOBY.setText(cl_dat.M_strUSRCD_pbst);
									txtGPODT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
									txtGPOTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
								}
								else
								{
									txtGPOBY.setText(nvlSTRVL(M_rstRSSET.getString("GP_GOTBY"),""));
									if(M_rstRSSET.getString("GP_GOTDT")!=null)
									{
										txtGPODT.setText(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("GP_GOTDT")).substring(0,10));
										txtGPOTM.setText(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("GP_GOTDT")).substring(11));
									}
								}
							}
							//** Added by ATC
							tblGPDTL.setValueAt(M_rstRSSET.getString("GP_MATCD"),i,TBL_GPMATCD);
							tblGPDTL.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,TBL_GPMATDS);
							tblGPDTL.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,TBL_GPUOMCD);
							tblGPDTL.setValueAt(M_rstRSSET.getString("GP_ISSQT"),i,TBL_GPISSQT);
							i++;
						}
					}
					else
						setMSG("Data not found for Gate Out..",'E');
					if(i==0)
						setMSG("Data not found for Gate Out..",'E');
				}
				catch(SQLException L_SE)
				{
					setMSG(L_SE,"action p");
				}
			}
			if(M_objSOURC == cmbMGPTP)
			{
			    clrGPTAB();
			}
			if(M_objSOURC == cmbSTRTP)
			{
			    clrGPTAB();
			}
			//if(L_AE.getSource().equals(jtpGATEIO))
			{
			//**visitors.................
				if(jtpGATEIO.getSelectedIndex() == 3)
				{
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
						setENBL(false);
				
					if(M_objSOURC == txtVSPNO)
					{
					    String L_strVSPNO = txtVSPNO.getText().trim();
					    clrCOMP();	
					    txtVSPNO.setText(L_strVSPNO);	       
					    getDATA();
					}
					if(M_objSOURC == txtVSTDT1)
					{
					    tblCNTDTL.clrTABLE();
					    getCNTDT();
					}
				}
				
			}
		
			 if((M_objSOURC == btnRPVST)||(M_objSOURC == btnRPVST1))
			 {
			    // hr_rpvgp objRPVGP;
				 String L_strVGPNO;
				 int L_intCNT =0;
				 if(strVSPNO.length() >0)
					objRPVGP.getDATA(strVSPNO,strVSPNO);
			//	if(rdbREPT_H.isSelected())
			  // {
					try
					{	
						Runtime r = Runtime.getRuntime();
						Process p = null;
						p  = r.exec("c:\\windows\\iexplore.exe "+cl_dat.M_strREPSTR_pbst+"hr_rpvgp.html"); 
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"Error.exescrn.. ");
 					}
					finally
					{
						//objRPVGP = null;
					}
			/*	}
				else
				{		
					// for printing
					JComboBox L_cmbLOCAL = objRPVGP.getPRNLS();
					objRPVGP.doPRINT(cl_dat.M_strREPSTR_pbst+"hr_rpvgp.doc",L_cmbLOCAL.getSelectedIndex());
				}*/
				
			}
			 
			 /**call exit pass method in txtbox of from date**/
			if(M_objSOURC == txtDOCDT)
    		{
				if(txtDOCDT.getText().length()>0)
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))						
						getEXPDL();
    				
		    }
			
		}
		catch(Exception e)
		{
			setMSG(e,"Child.ActionPerformed ");
		}
	}
	
	private void exeAUTEP()
	{
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
		{
			try{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;	
					
				if(!vldEXPAU())/****/
				{
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
					
				boolean flgSELRW=false;////flag to check whether atleast 1 row from tblEXPDTL is selected
				for(int P_intROWNO=0;P_intROWNO<tblEXPDTL.getRowCount();P_intROWNO++)
				{
					if(tblEXPDTL.getValueAt(P_intROWNO,TB6_CHKFL).toString().equals("true"))
					{
						flgSELRW=true;
							
					}
				}
				if(flgSELRW==false)
					setMSG("Please Select atleast 1 row from the Table",'E');
					
					
				else 
				{	
					/**update Exit Pass record to set Status flag '2' after Security Confirmation***/
					for(int i=0;i<tblEXPDTL.getRowCount();i++)
					{
					    if(tblEXPDTL.getValueAt(i,TB6_CHKFL).toString().trim().equals("true"))
					    {
							M_strSQLQRY ="UPDATE HR_EXTRN SET ";
							M_strSQLQRY += " EX_STSFL = '2',";
							M_strSQLQRY += " EX_SECTM ='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEXPDTL.getValueAt(i,TB6_DOCDT).toString()+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
							M_strSQLQRY += " EX_AUTBY ='"+tblEXPDTL.getValueAt(i,TB6_AUTBY).toString().trim().toUpperCase()+"'";
							M_strSQLQRY += " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  EX_DOCNO = '" + tblEXPDTL.getValueAt(i,TB6_DOCNO).toString().trim()+"'";
							//System.out.println(">>>UPDATE>>"+M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								
					    }
					}
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Record Updated Successfully...",'N');
						try
						{
							for(int i=0;i<tblEXPDTL.getRowCount();i++)
							{
							    if(tblEXPDTL.getValueAt(i,TB6_CHKFL).toString().trim().equals("true"))
							    {
									M_strSQLQRY = " select distinct SUBSTRING(cmt_codcd,6,4),ep_emlrf,trim(isnull(ep_lstnm,' ')) + ' '  + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM,getdate() DATE,current_time TIME";
									M_strSQLQRY +=" from co_cdtrn,hr_epmst";
									M_strSQLQRY +=" where ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and ep_empno=SUBSTRING(cmt_codcd,6,4) and ep_lftdt is null and cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN') and SUBSTRING(cmt_codcd,1,4)='"+tblEXPDTL.getValueAt(i,TB6_EMPNO).toString().trim()+"'";
									M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
									//System.out.println("M_strSQLQRY"+M_strSQLQRY);
									if(M_rstRSSET != null)
								    { 
										while(M_rstRSSET.next())
										{
											if(M_rstRSSET.getString("EP_EMLRF").length()>0)
											{
												String strBODY = tblEXPDTL.getValueAt(i,TB6_EMPNM).toString().trim()+" Has Been Approved For Security Exit at "+M_fmtLCDAT.format(M_rstRSSET.getDate("DATE"))+" "+M_rstRSSET.getString("TIME").substring(0,5)+" Hrs. for '"+tblEXPDTL.getValueAt(i,TB6_REMDS).toString().trim()+"' Reason";
												JOptionPane.showMessageDialog(null,strBODY, "Verify", JOptionPane.INFORMATION_MESSAGE); 
												//cl_eml ocl_eml = new cl_eml();
												//ocl_eml.sendfile(M_rstRSSET.getString("EP_EMLRF"),null,"Exit Pass Approval",strBODY);
											}
										}
									}
									else 
										setMSG("Email Reference For Employee recm and sanc authoritise does not Exist..",'E'); 
										
										
									/***While Actual Out time has more than half an hour of Expected Out time,then Security Send mail to Sactioning Authority **/
										
								    L_strTIME=subTIME(cl_dat.M_txtCLKTM_pbst.getText().substring(1,6),tblEXPDTL.getValueAt(i,TB6_OUTTM).toString());
									//System.out.println("Subtime "+L_strTIME);
									String L_strHOURS="01:00";
										
									/*if((tblEXPDTL.getValueAt(i,TB6_DOCDT).toString()+" "+L_strTIME).compareTo(tblEXPDTL.getValueAt(i,TB6_DOCDT).toString()+" "+L_strHOURS) > 0)
									{
										//if(!vldEXTPS())
										//	return;
										JOptionPane.showMessageDialog( null,"Exit Pass delayed gate Out Intimation  \n"+L_strLINE+" \nEmp Name : "+tblEXPDTL.getValueAt(i,TB6_EMPNM).toString().trim()+ "       Dept : "+tblEXPDTL.getValueAt(i,TB6_DPTNM).toString().trim()+"\nOut Time       Expected : "+tblEXPDTL.getValueAt(i,TB6_OUTTM).toString().trim()+"\n                        Actual :      "+cl_dat.M_txtCLKTM_pbst.getText().substring(1,6)+"\n                        Delay :        "+L_strTIME+ " \n Purpose : "+tblEXPDTL.getValueAt(i,TB6_REMDS).toString().trim()+"\n"+L_strLINE,"Expected Out Time Validation",JOptionPane.INFORMATION_MESSAGE);
										M_strSQLQRY = " SELECT EP_EMPNO,trim(isnull(ep_lstnm,' '))||' '  ||left(isnull(ep_fstnm,' '),1)||'.'||left(isnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ,EP_EMLRF ";
										M_strSQLQRY +=" from HR_EPMST where EP_EMPNO  in (select distinct SUBSTRING(cmt_codcd,6,4) from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' ";
										M_strSQLQRY +=" and SUBSTRING(cmt_codcd,1,4) = '"+ tblEXPDTL.getValueAt(i,TB6_EMPNO).toString().trim()+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LSN')) ";
										M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
										//System.out.println("M_strSQLQRY"+M_strSQLQRY);
										if(M_rstRSSET != null)
									    { 
											while(M_rstRSSET.next())
											{
												if(M_rstRSSET.getString("EP_EMLRF").length()>0)
												{
														
													String strBODY = "Exit Pass delayed gate Out Intimation  \n"+L_strLINE+" \nEmp Name : "+tblEXPDTL.getValueAt(i,TB6_EMPNM).toString().trim()+ "       Dept : "+tblEXPDTL.getValueAt(i,TB6_DPTNM).toString().trim()+"\nOut Time      Expected : "+tblEXPDTL.getValueAt(i,TB6_OUTTM).toString().trim()+"\n                       Actual : "+cl_dat.M_txtCLKTM_pbst.getText().substring(1,6)+"\n                       Delay : "+L_strTIME+ " \n Purpose : "+tblEXPDTL.getValueAt(i,TB6_REMDS).toString().trim();
													JOptionPane.showMessageDialog(null,strBODY, "Verify", JOptionPane.INFORMATION_MESSAGE); 
													cl_eml ocl_eml = new cl_eml();
													ocl_eml.sendfile(M_rstRSSET.getString("EP_EMLRF"),null,"Out Time Validation ",strBODY);
													//ocl_eml.sendfile("Salunkhe@spl.co.in",null,"Out Time Validation",strBODY);
														
												}
											}
									    }
										else 
											setMSG("Email Reference For Employee Sanc authoritise does not Exist..",'E'); 
									}*/
											    
							    }
							}
						}
						catch(Exception E)
						{
							setMSG(E," Error while Mailing Data....");
						}
										
						tblEXPDTL.clrTABLE();
						inlTBLEDIT(tblEXPDTL);
						getEXPDL();				
					}					
				}
			}
			catch(Exception E)
			{
				setMSG(E," Error In Updating Exit Pass....");
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}		
	}
	/** Method to display Exit Pass record with status flag '0' in first table & '1' in second table . */
	private void getEXPDL()
	{        
		try  
		{    
			if(txtDOCDT.getText().length()==0)
			{
				setMSG("Please Enter Date...",'E');
				txtDOCDT.requestFocus();
				return;
			}

			tblEXPDTL.clrTABLE();
			inlTBLEDIT(tblEXPDTL);
			tblEXPDTL1.clrTABLE();
			inlTBLEDIT(tblEXPDTL1);
			
			int L_ROWNO = 0;
			int L_ROWNO1 = 0;
			String L_strSTSFL="";
			java.sql.Timestamp L_tmsINCTM=null,L_tmsOUTTM=null;
			
			//// for the Employees which are not approved yet by security for exit pass
			M_strSQLQRY= " SELECT EX_DOCDT,EX_EMPNO,EX_DOCNO,EX_REMDS,EX_EOTTM,EX_EINTM,EX_AOTTM,EX_OFPFL,EX_AUTBY,EX_SHFCD,EX_STSFL, trim(isnull(ep_lstnm,' ')) + ' '  + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ,EP_DPTNM from HR_EXTRN,HR_EPMST ";
			M_strSQLQRY+= " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO=EX_EMPNO AND EP_CMPCD=EX_CMPCD AND isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
			M_strSQLQRY+= " and EX_STSFL in('0','1') ";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				 M_strSQLQRY+= " and CONVERT(varchar,EX_DOCDT,103)< getdate";	
			M_strSQLQRY += " order by EX_DOCDT,EX_EMPNO";
				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strSTSFL=M_rstRSSET.getString("EX_STSFL");
					//if(L_strSTSFL.equals("0") || L_strSTSFL.equals("1"))
					{
						L_tmsINCTM=M_rstRSSET.getTimestamp("EX_EINTM");
						L_tmsOUTTM=M_rstRSSET.getTimestamp("EX_EOTTM");
						tblEXPDTL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")),L_ROWNO,TB6_DOCDT);	
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_EMPNO"),""),L_ROWNO,TB6_EMPNO);
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_EMPNM"),""),L_ROWNO,TB6_EMPNM);
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""),L_ROWNO,TB6_DPTNM);
						tblEXPDTL.setValueAt(L_tmsOUTTM==null ? "" : M_fmtLCDTM.format(L_tmsOUTTM).substring(11),L_ROWNO,TB6_OUTTM);
						tblEXPDTL.setValueAt(L_tmsINCTM==null ? "" : M_fmtLCDTM.format(L_tmsINCTM).substring(11),L_ROWNO,TB6_INCTM);
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_DOCNO"),""),L_ROWNO,TB6_DOCNO);
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_SHFCD"),""),L_ROWNO,TB6_WRKSH);
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_REMDS"),""),L_ROWNO,TB6_REMDS);
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_OFPFL"),""),L_ROWNO,TB6_OFPFL);
						tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_AUTBY"),""),L_ROWNO,TB6_AUTBY);
							
						if(hstEXPST.containsKey(L_strSTSFL))
						{	
							tblEXPDTL.setValueAt(hstEXPST.get(L_strSTSFL),L_ROWNO,TB6_STSFL);
						}
						
						L_ROWNO ++;
					}
				}
			}
				
			M_rstRSSET.close();
				
			//// for the Employees approved by security for exit pass
			M_strSQLQRY= " SELECT EX_DOCDT,EX_EMPNO,EX_DOCNO,EX_REMDS,EX_EOTTM,EX_EINTM,EX_AOTTM,EX_OFPFL,EX_AUTBY,EX_SHFCD,EX_STSFL,EX_LUSBY, trim(isnull(ep_lstnm,' ')) + ' '   + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ,EP_DPTNM from HR_EXTRN,HR_EPMST ";
			M_strSQLQRY+= " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO=EX_EMPNO AND EP_CMPCD=EX_CMPCD AND isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
			M_strSQLQRY+= " and EX_STSFL ='2' ";
			M_strSQLQRY+= " and CONVERT(varchar,EX_DOCDT,103)= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOCDT.getText()))+"'";	
			M_strSQLQRY += " order by EX_DOCDT,EX_EMPNO";
				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strSTSFL=M_rstRSSET.getString("EX_STSFL");
					
					//if(L_strSTSFL.equals("2"))
					{
						L_tmsINCTM=M_rstRSSET.getTimestamp("EX_EINTM");
						L_tmsOUTTM=M_rstRSSET.getTimestamp("EX_EOTTM");
						tblEXPDTL1.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")),L_ROWNO1,TB6_DOCDT);	
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_EMPNO"),""),L_ROWNO1,TB6_EMPNO);
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_EMPNM"),""),L_ROWNO1,TB6_EMPNM);
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""),L_ROWNO1,TB6_DPTNM);
						tblEXPDTL1.setValueAt(L_tmsOUTTM==null ? "" : M_fmtLCDTM.format(L_tmsOUTTM).substring(11),L_ROWNO1,TB6_OUTTM);
						tblEXPDTL1.setValueAt(L_tmsINCTM==null ? "" : M_fmtLCDTM.format(L_tmsINCTM).substring(11),L_ROWNO1,TB6_INCTM);
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_DOCNO"),""),L_ROWNO1,TB6_DOCNO);
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_SHFCD"),""),L_ROWNO1,TB6_WRKSH);
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_REMDS"),""),L_ROWNO1,TB6_REMDS);
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_OFPFL"),""),L_ROWNO1,TB6_OFPFL);
						tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_AUTBY"),""),L_ROWNO1,TB6_AUTBY);
						if(hstEXPST.containsKey(L_strSTSFL))
						{	
							tblEXPDTL1.setValueAt(hstEXPST.get(L_strSTSFL),L_ROWNO1,TB6_STSFL);
						}
						L_ROWNO1 ++;
					}
					
				}
			}
			M_rstRSSET.close();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getEXPDL");
		}
		
	}
	
	
	private String getDRVCD(String P_strDRVNM)
	{
		String L_strDRVCD = "";
		String L_strINTLT = P_strDRVNM.substring(0,1).toUpperCase();
		try
		{
			M_strSQLQRY =  "Select max(DV_DRVCD) strDRVCD from MM_DVMST" ;
			M_strSQLQRY += " where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(DV_DRVCD,1,1) = '" + L_strINTLT + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_strDRVCD = M_rstRSSET.getString("strDRVCD");
				M_rstRSSET.close();
				if(L_strDRVCD == null)
				   L_strDRVCD = L_strINTLT + "0001";
				else
				{
					L_strDRVCD = String.valueOf(Integer.parseInt(L_strDRVCD.substring(1).trim()) + 1).trim();
					for(int i=L_strDRVCD.length(); i<4; i++)		
						L_strINTLT = L_strINTLT.concat("0");
			
					L_strDRVCD = L_strINTLT.concat(L_strDRVCD);
				}
			}
			else	
				L_strDRVCD = L_strINTLT + "0001";
		}
		catch(Exception e)
		{
			System.out.println("Error in getDRVCD : " + e.toString());
		}
		return L_strDRVCD;
	}
	
	private void exeGETDRV1(String P_strDRVCD)
	{
		try
		{
			M_strSQLQRY  = "Select DV_LICNO,DV_DRVNM,DV_LICBY,DV_LVLDT,DV_REMDS,DV_BLKCD" ;
			M_strSQLQRY += " from MM_DVMST where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DV_DRVCD = '" + P_strDRVCD + "'" ;
			M_strSQLQRY += " and DV_STSFL <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(">>>>>>exeGETDRV1>>>>>>"+M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				//System.out.println(">>M_rstRSSET.getString(DV_LICNO)>>"+M_rstRSSET.getString("DV_LICNO"));
				if(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("DV_LVLDT"))).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
				{	
				   JOptionPane.showMessageDialog(null,"LICENCE EXPIRED ON :  "+M_fmtLCDAT.format(M_rstRSSET.getDate("DV_LVLDT")), "Verify", JOptionPane.INFORMATION_MESSAGE);
				   txtLICNO.requestFocus();
				}  
				txtLICNO.setText(nvlSTRVL(M_rstRSSET.getString("DV_LICNO"),""));
				txtDRVNM.setText(nvlSTRVL(M_rstRSSET.getString("DV_DRVNM"),""));
				txtLICBY.setText(nvlSTRVL(M_rstRSSET.getString("DV_LICBY"),""));
				txtLVLDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("DV_LVLDT")));
				txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("DV_REMDS"),""));
				if(!nvlSTRVL(M_rstRSSET.getString("DV_BLKCD"),"00").equals("00"))
				{
					if(hstBLKCD.containsKey(M_rstRSSET.getString("DV_BLKCD")))
					{	
						String L_strSQLQRY = "select wb_gindt,wb_lryno,wb_docno from mm_wbtrn where CONVERT(varchar,wb_gindt,103) = (select max(CONVERT(varchar,wb_gindt,103)) from mm_wbtrn where wb_drvcd = '" + P_strDRVCD + "') and wb_drvcd = '" + P_strDRVCD + "'";
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
						if(L_rstRSSET!=null && L_rstRSSET.next())
							JOptionPane.showMessageDialog(null,"DRIVER BLACK LISTED\n"+hstBLKCD.get(M_rstRSSET.getString("DV_BLKCD"))+"\n"+"Previous Visited Date : "+M_fmtLCDAT.format(L_rstRSSET.getDate("wb_gindt"))+"\n"+"Previous Lorry No : "+L_rstRSSET.getString("wb_lryno")+"\n"+"Previous Gate In No : "+L_rstRSSET.getString("wb_docno"), "Verify", JOptionPane.INFORMATION_MESSAGE);
						else 
							JOptionPane.showMessageDialog(null,"DRIVER BLACK LISTED\n"+hstBLKCD.get(M_rstRSSET.getString("DV_BLKCD")), "Verify", JOptionPane.INFORMATION_MESSAGE);
					}	
				}	
			}
		}	
		catch(Exception e)
		{
			System.out.println("Error in exeGETREC1 : " + e.toString());
		}	
    }
	
	private boolean DRV_EXIST(String P_strDRVCD)
	{
		boolean flgEXIST=false;
		try
		{
			String M_strSQLQRY1="";
			M_strSQLQRY1  = "Select count(*) CNT" ;
			M_strSQLQRY1 += " from MM_DVMST where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DV_DRVCD = '" + P_strDRVCD + "'" ;
			M_strSQLQRY1 += " and DV_STSFL <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY1);
			//System.out.println(">>>>>>exeGETDRV1>>>>>>"+M_strSQLQRY1);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{
				if(M_rstRSSET.getInt("CNT")>0)
					flgEXIST=true;	
			}
			return flgEXIST;
		}	
		catch(Exception e)
		{
			System.out.println("Error in exeGETREC1 : " + e.toString());
		}	
		return flgEXIST;
    }
	
	private void exeGETDRV(String P_strLICNO)
	{
		try
		{
			txtDRVCD.setText("");
			txtDRVNM.setText("");
			txtLICBY.setText("");
			txtLVLDT.setText("");
			txtREMDS.setText("");
			
			M_strSQLQRY = " Select count(*) CNT";
			M_strSQLQRY += " from MM_DVMST where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DV_LICNO = '" + P_strLICNO + "' and DV_STSFL<>'X'" ;			 
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			if(M_rstRSSET!=null && M_rstRSSET.next())
			{			 
				if(M_rstRSSET.getInt("CNT") > 0)
				{	
					flgDRV_EXIST=true;
					M_strSQLQRY  = "Select DV_DRVCD,DV_DRVNM,DV_LICBY,DV_LVLDT,DV_REMDS,DV_BLKCD" ;
					M_strSQLQRY += " from MM_DVMST where DV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DV_LICNO = '" + P_strLICNO + "'" ;
					M_strSQLQRY += " and DV_STSFL <> 'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						txtDRVCD.setText(nvlSTRVL(M_rstRSSET.getString("DV_DRVCD"),""));
						txtDRVNM.setText(nvlSTRVL(M_rstRSSET.getString("DV_DRVNM"),""));
						txtLICBY.setText(nvlSTRVL(M_rstRSSET.getString("DV_LICBY"),""));
						txtLVLDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("DV_LVLDT")));
						txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("DV_REMDS"),""));
						if(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("DV_LVLDT"))).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
						{	
							JOptionPane.showMessageDialog(null,"LICENCE EXPIRED ON :  "+M_fmtLCDAT.format(M_rstRSSET.getDate("DV_LVLDT")), "Verify", JOptionPane.INFORMATION_MESSAGE);
							txtLICNO.requestFocus();
						}	
						if(!nvlSTRVL(M_rstRSSET.getString("DV_BLKCD"),"00").equals("00"))
						{
							if(hstBLKCD.containsKey(M_rstRSSET.getString("DV_BLKCD")))
							{	
								String L_strSQLQRY = "select wb_gindt,wb_lryno,wb_docno from mm_wbtrn where CONVERT(varchar,wb_gindt,103) = (select max(CONVERT(varchar,wb_gindt,103)) from mm_wbtrn where wb_drvcd = '" + nvlSTRVL(M_rstRSSET.getString("DV_DRVCD"),"") + "') and wb_drvcd = '" + nvlSTRVL(M_rstRSSET.getString("DV_DRVCD"),"") + "'";
								ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
								if(L_rstRSSET!=null && L_rstRSSET.next())
									JOptionPane.showMessageDialog(null,"DRIVER BLACK LISTED\n"+hstBLKCD.get(M_rstRSSET.getString("DV_BLKCD"))+"\n"+"Previous Visited Date : "+M_fmtLCDAT.format(L_rstRSSET.getDate("wb_gindt"))+"\n"+"Previous Lorry No : "+L_rstRSSET.getString("wb_lryno")+"\n"+"Previous Gate In No : "+L_rstRSSET.getString("wb_docno"), "Verify", JOptionPane.INFORMATION_MESSAGE);
								else 
									JOptionPane.showMessageDialog(null,"DRIVER BLACK LISTED\n"+hstBLKCD.get(M_rstRSSET.getString("DV_BLKCD")), "Verify", JOptionPane.INFORMATION_MESSAGE);
							}	
						}
						txtDRVNM.setEnabled(false);
						txtLICBY.setEnabled(false);
						txtLVLDT.setEnabled(false);
						txtREMDS.setEnabled(false);
					}
				}	
				else
				{
					flgDRV_EXIST=false;
					txtDRVNM.setEnabled(true);
					txtLICBY.setEnabled(true);
					txtLVLDT.setEnabled(true);
					txtREMDS.setEnabled(false);
					txtLICBY.requestFocus();
				}
			}
			else
			{
				flgDRV_EXIST=false;
				txtLICBY.requestFocus();
			}	
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
			
		}
		catch(Exception e)
		{
			System.out.println("Error in exeGETREC : " + e.toString());
		}
	}

	public void focusGained(FocusEvent L_FE)
	{
		if(M_objSOURC==txtVSTNM1)
		{
				setMSG("press F1 to select the value..",'N');
			
		}
		if(M_objSOURC==tblCNTDTL)
		{
			tblCNTDTL.setValueAt(new Boolean(true),tblCNTDTL.getSelectedRow(),TB3_CHKFL);
		}
		if(M_objSOURC==tblVSTDTL1)
		{
			tblVSTDTL1.setValueAt(new Boolean(true),tblVSTDTL1.getSelectedRow(),TB1_CHKFL);
		}
		if(M_objSOURC==tblVSTDTL2)
		{
			tblVSTDTL2.setValueAt(new Boolean(true),tblVSTDTL2.getSelectedRow(),TB1_CHKFL);
		}
		if(M_objSOURC==tblEXPDTL)
		{
			tblEXPDTL.setValueAt(new Boolean(true),tblEXPDTL.getSelectedRow(),TB6_CHKFL);
			
		}
		
		if(M_objSOURC == txtAUTBY)
		{
			if(jtpGATEIO.getSelectedIndex()==4)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblEXPDTL.getValueAt(tblEXPDTL.getSelectedRow(),TB6_STSFL).toString().trim().equals("Applied"))
					((JTextField)tblEXPDTL.cmpEDITR[TB6_AUTBY]).setEditable(true);
				else
					((JTextField)tblEXPDTL.cmpEDITR[TB6_AUTBY]).setEditable(false); 
				 
			}
		}
	}
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
	}	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtLICNO"))
			{
				txtDRVNM.setText(cl_dat.M_strHLPSTR_pbst);
				txtLICNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				exeGETDRV(txtLICNO.getText().trim());
			}

			if(M_strHLPFLD.equals("txtGINNO"))
			{						
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				strGINNO = cl_dat.M_strHLPSTR_pbst;
				exeGETREC(strGINTP,strGINNO,"1");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{	
					if(strSTSFL.equals("0"))
					{
						setENBL(true);
						txtGINDT.requestFocus();
					}
                    else if(strSTSFL.equals("9"))
					{
						setMSG("Gate out is done,Modification not allowed..",'E');
					}
					else
					{
						setENBL1(true);
						txtGOTDT.requestFocus();
						txtGOTTM.requestFocus();
					}
				}
			}			
			else if(M_strHLPFLD.equals("txtDORNO"))
			{	
				txtORDRF.setText(cl_dat.M_strHLPSTR_pbst);		
				txtORDDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
				strTPRCD = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim();
				txtTPRCD.setText(strTPRCD);
				txtTPRDS.setText(getPRTDS("T",strTPRCD));
				txtLRYNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)).trim());
				txtREMGT.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtLRYNO"))
			{
				txtLRYNO.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());		
				strTPRDS = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
				intINDEX = strTPRDS.indexOf('|');
				txtTPRCD.setText(strTPRDS.substring(0,intINDEX));
				txtTPRDS.setText(strTPRDS.substring(intINDEX+1));
				txtTPRCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtPORNO"))
			{
				txtPORNO.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());		
				//txtPORNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtVSPNO"))
			{
				txtVSPNO.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());		
			}
			else if(M_strHLPFLD.equals("txtMGPNO"))
			{
				txtMGPNO.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());		
			}
			else if(M_strHLPFLD.equals("txtMATCD"))
			{
				txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtMATDS.setText(cl_dat.M_strHLPSTR_pbst);	
				txtLRYNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtTPRCD"))
			{
				txtTPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				strTPRDS = cl_dat.M_strHLPSTR_pbst;
				if(strTPRDS.trim().substring(0,5).equals("DUMMY"))
				{
					txtTPRDS.setEnabled(true);
					txtTPRDS.requestFocus();
				}
				else
				{
					txtTPRDS.setText(strTPRDS);	
					txtREMGT.requestFocus();
				}
			}
			else if(M_strHLPFLD.equals("txtVENCD"))
			{
				txtVENDS.setEnabled(false);
				txtVENCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				strVENDS = cl_dat.M_strHLPSTR_pbst;
				txtVENDS.setText(strVENDS);
				if(txtVENCD.getText().equals(strDUMCD_fn))
				{	
					txtVENDS.setEnabled(true);
					txtVENDS.requestFocus();
				}
				else
					txtVENDS.setEnabled(false);
				if((strGINTP.equals(strRECPT_fn))||(strGINTP.equals(strSLSRT_fn)))					// Receipt 
					txtPORNO.requestFocus();
				else
					txtMATCD.requestFocus();
			}
			if(M_strHLPFLD.equals("txtVSTNM1"))
			{
			  	tblCNTDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),tblCNTDTL.getSelectedRow(),TB3_PRTCD);
				txtVSTNM1.setText(cl_dat.M_strHLPSTR_pbst);	
			}
			if(M_strHLPFLD.equals("txtPERVS"))
		    {
		        strPERVS =String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim() ;
		        
		        txtPERVS.setText(strPERVS);
		        
		        strDPTNM=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim();
		        txtDPTNM.setText(strDPTNM);
		        lblEMPNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());
		    }
			
			if(M_strHLPFLD.equals("txtVSARA"))
		    {
		        txtVSARA.setText(cl_dat.M_strHLPSTR_pbst );
		    }
			
			if(M_strHLPFLD.equals("txtAUTBY"))
			{
			      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			      txtAUTBY.setText(L_STRTKN.nextToken());
			      //tblEXPDTL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).toString(),tblEXPDTL.getSelectedRow(),TB6_AUTBY);
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK ");
		}
	}
	private boolean vldVSARA()
	{
	    try{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXVSA'  and  CMT_CODDS='"+txtVSARA.getText().toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldVSPNO ....."+L_EX);							   
		}
	    return false;
	}
	///visitors..............
private void getDATA()
{        
	try  
	{    
		int L_ROWNO = 0;
		java.sql.Timestamp L_tmpTIME;
		java.sql.Date L_tmpDATE;
		String L_strSQLQRY ="";
		ResultSet L_rstRSSET;
		String L_strTEMP="",L_strTVSPNO="";
		//if(jtpMANTAB.getSelectedIndex() == 0)
		//{
			M_strSQLQRY ="Select * from HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND VS_CMPCD=RM_CMPCD AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='HC' and RM_TRNCD ='01' ";
		   	M_strSQLQRY+= " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,' ') <> 'X' and VS_VSTTP ='01' and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
		   	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				M_strSQLQRY+= " and VS_VSOTM IS NULL "; // status flag condition added on 25/05/07
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				M_strSQLQRY+= " and VS_VSOTM IS NOT NULL ";
			}
			M_strSQLQRY+= " AND VS_VSPNO ='"+txtVSPNO.getText().trim() +"'";
			M_strSQLQRY+= " order by VS_VSPNO ";
			//System.out.println("txtVSTDT : "+M_strSQLQRY);
			strVSPNO = txtVSPNO.getText().trim();
			intVSCNT =0;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			while(M_rstRSSET.next())
			{
				L_tmpDATE = M_rstRSSET.getDate("VS_VSTDT");
	 			L_strTEMP="";
	 			if (L_tmpDATE != null)
					L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
	 			txtVSTDT.setText(L_strTEMP);
	 			txtVSTNM.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""));
	 			txtBDGNO.setText(nvlSTRVL(M_rstRSSET.getString("VS_BDGNO"),""));
				txtVSICT.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),""));
				txtVSORG.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),""));
				txtVSREM.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
				txtPERVS.setText(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""));
				lblEMPNO.setText(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""));
				txtSHFCD.setText(nvlSTRVL(M_rstRSSET.getString("VS_SHFCD"),""));
				txtVSARA.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSARA"),""));
				txtVSVHN.setText(nvlSTRVL(M_rstRSSET.getString("VS_VEHNO"),""));
				txtVSOCT.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),""));
				txtPURPS.setText(nvlSTRVL(M_rstRSSET.getString("VS_PURPS"),""));
				txtESCBY.setText(nvlSTRVL(M_rstRSSET.getString("VS_ESCBY"),""));
				txtCLRBY.setText(nvlSTRVL(M_rstRSSET.getString("VS_CLRBY"),""));
				txtDPTNM.setText(nvlSTRVL(M_rstRSSET.getString("VS_DPTNM"),""));
				L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSOTM");
				L_strTEMP="";
				if (L_tmpTIME != null)
				{
					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
					txtVSOTM.setText(L_strTEMP);
				}
				L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
				L_strTEMP="";
				if (L_tmpTIME != null)
				{
					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
					txtVSITM.setText(L_strTEMP);
				}
				
				if(M_rstRSSET.getString("VS_MOBFL").equals("Y"))
				{
					rdbMOBFLY.setSelected(true);
				}
				else rdbMOBFLN.setSelected(true);
				
				if(M_rstRSSET.getString("VS_CMRFL").equals("Y"))
					rdbCMRFLY.setSelected(true);
				else rdbCMRFLN.setSelected(true);
		
				L_ROWNO ++;
			}
			if(L_ROWNO ==0)
			{
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			        setMSG("Not Available for Modification ..",'E');
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Not Available for Deletion ..",'E');
			//else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			//	setMSG("Data Not Found ..",'E');
			    
			M_rstRSSET.close();
			}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getINVST");
	}
}//visitors........
/** Method for Getting All Records from Table1. */
private void getINVST()
{        
	try  
	{    
		
		int L_ROWNO = 0;
		java.sql.Timestamp L_tmpTIME;
		java.sql.Date L_tmpDATE;
		String L_strSQLQRY ="";
		ResultSet L_rstRSSET;
		String L_strTEMP="",L_strTVSPNO="";
		if(jtpMANTAB.getSelectedIndex() == 1)
		{
			M_strSQLQRY ="Select * from HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND VS_CMPCD=RM_CMPCD AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='HC' and RM_TRNCD ='01' where ";
		    M_strSQLQRY+= " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,' ') <> 'X' and VS_VSOTM IS NULL and VS_VSTTP ='01' and isnull(VS_STSFL,' ') = '6 ' ";
			M_strSQLQRY+= " order by VS_VSPNO ";
			//System.out.println("tblVSTDTL1 : "+M_strSQLQRY);
			intVSCNT =0;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			while(M_rstRSSET.next())
			{
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO,TB1_VSTNM);
	 			L_strTVSPNO = nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),"");
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),""),L_ROWNO,TB1_VSPNO);
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),""),L_ROWNO,TB1_VSICT);
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),""),L_ROWNO,TB1_VSOCT);
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),""),L_ROWNO,TB1_VSORG);
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO,TB1_REMDS);
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""),L_ROWNO,TB1_PERVS);
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""),L_ROWNO,TB1_EMPNO);
				tblVSTDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_CLRBY"),""),L_ROWNO,TB1_CLRBY);
				intVSOCT = M_rstRSSET.getInt("VS_VSOCT");
				intVSICT = M_rstRSSET.getInt("VS_VSICT");
				intVSICT = intVSICT-intVSOCT;
				L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
				L_strTEMP="";
				if (L_tmpTIME != null)
				{
					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
					tblVSTDTL1.setValueAt(L_strTEMP,L_ROWNO,TB1_VSITM);
				}
				L_ROWNO ++;
				//for counting the visitors which in in..
				intVSCNT += intVSICT;
			}
			M_rstRSSET.close();
			lblVSICT.setText(String.valueOf(intVSCNT));
			intVSCNT=0;intVSICT=0;intVSOCT=0;
			}
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getINVST");
	}
	getOTVST();
}//visitors.......

/** Method for Getting All Records from Table3 When Authorised. */
private void getAUTHVST()
{        
	try  
	{    
		int L_ROWNO = 0;
		java.sql.Timestamp L_tmpTIME;
		java.sql.Date L_tmpDATE;
		String L_strSQLQRY ="";
		ResultSet L_rstRSSET;
		String L_strTEMP="",L_strTVSPNO="";
		if(jtpMANTAB.getSelectedIndex() == 3)
		{
			M_strSQLQRY ="Select * from  HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND VS_CMPCD=RM_CMPCD AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='HC' and RM_TRNCD ='01' where ";
		    M_strSQLQRY += " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP ='01' and ((VS_VSCAT = '03' and VS_STSFL = '4')OR(VS_VSCAT != '03' and (VS_STSFL = '4'))) ";
		    M_strSQLQRY += " and VS_VSOTM IS NULL ";

			M_strSQLQRY += " order by VS_VSPNO ";
			//System.out.println("tblVSTDTL3 : "+M_strSQLQRY);
			intVSCNT =0;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			while(M_rstRSSET.next())
			{
			    tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO,TB4_VSTNM);
				L_strTVSPNO = nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),"");
	 			tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),""),L_ROWNO,TB4_VSPNO);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTCT"),""),L_ROWNO,TB4_VSTCT);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),""),L_ROWNO,TB4_VSORG);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO,TB4_REMDS);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""),L_ROWNO,TB4_PERVS);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""),L_ROWNO,TB4_EMPNO);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_CLRBY"),""),L_ROWNO,TB4_CLRBY);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_BDGNO"),""),L_ROWNO,TB4_BADGE);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PURPS"),""),L_ROWNO,TB4_PURPS);
				
				if(nvlSTRVL(M_rstRSSET.getString("VS_CMRFL"),"").equals("Y"))
						tblVSTDTL3.setValueAt(new Boolean(true),L_ROWNO,TB4_CMRFL);
					else
						tblVSTDTL3.setValueAt(new Boolean(false),L_ROWNO,TB4_CMRFL);
					
					if(nvlSTRVL(M_rstRSSET.getString("VS_MOBFL"),"").equals("Y"))
						tblVSTDTL3.setValueAt(new Boolean(true),L_ROWNO,TB4_MOBFL);
					else
						tblVSTDTL3.setValueAt(new Boolean(false),L_ROWNO,TB4_MOBFL);
				
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSARA"),""),L_ROWNO,TB4_VSARA);
				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VEHNO"),""),L_ROWNO,TB4_VEHNO);
//				tblVSTDTL3.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO,TB5_REMDS);
				L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
				L_strTEMP="";
				if (L_tmpTIME != null)
				{
					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
					tblVSTDTL3.setValueAt(L_strTEMP,L_ROWNO,TB4_VSITM);
				}
				L_ROWNO ++;
				//for counting the visitors which in in..
				
			}
			M_rstRSSET.close();
			
			}
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getAUTHVST");
	}
	getWTGVST();
}//visitors........
private void  getWTGVST()
{
    try  
	{  
        
		int L_ROWNO = 0;
		java.sql.Timestamp L_tmpTIME;
		java.sql.Date L_tmpDATE;
		String L_strSQLQRY ="";
		ResultSet L_rstRSSET;
		String L_strSTSFL ="";
		String L_strTEMP="",L_strTVSPNO="";
		if(jtpMANTAB.getSelectedIndex() == 3)
		{
		    //
			M_strSQLQRY ="Select * from HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND VS_CMPCD = RM_CMPCD AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='HC' and RM_TRNCD ='01' where ";
		    M_strSQLQRY += " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (VS_STSFL = '1' or VS_STSFL='2' or VS_STSFL='3') and VS_VSOTM IS NULL and VS_VSTTP='01'";
			M_strSQLQRY += " order by VS_VSPNO ";
			//System.out.println("tblVSTDTL4 : "+M_strSQLQRY);

			intVSCNT =0;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
			while(M_rstRSSET.next())
			{
				
			    tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO,TB5_VSTNM);
	 			L_strTVSPNO = nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),"");
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),""),L_ROWNO,TB5_VSPNO);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTCT"),""),L_ROWNO,TB5_VSTCT);
				
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),""),L_ROWNO,TB5_VSORG);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO,TB5_REMDS);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""),L_ROWNO,TB5_PERVS);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""),L_ROWNO,TB5_EMPNO);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_CLRBY"),""),L_ROWNO,TB5_CLRBY);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_BDGNO"),""),L_ROWNO,TB5_BADGE);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PURPS"),""),L_ROWNO,TB5_PURPS);
					
				//	if(L_strSTSFL.equals("1"))
				//		tblVSTDTL4.setValueAt("Test color red",L_ROWNO,TB5_APRFL);
					L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("VS_STSFL"),"");
					if(L_strSTSFL.equals("2"))
					{
								L_strSTSFL = "Not Approved";
								tblVSTDTL4.setValueAt(L_strSTSFL,L_ROWNO,TB5_APRFL);
					}
					else if(L_strSTSFL.equals("3"))
					{
								L_strSTSFL = "Held For Discussion";
								tblVSTDTL4.setValueAt(L_strSTSFL,L_ROWNO,TB5_APRFL);
					}
					
				if(nvlSTRVL(M_rstRSSET.getString("VS_CMRFL"),"").equals("Y"))
						tblVSTDTL4.setValueAt(new Boolean(true),L_ROWNO,TB5_CMRFL);
					else
						tblVSTDTL4.setValueAt(new Boolean(false),L_ROWNO,TB5_CMRFL);
					
					if(nvlSTRVL(M_rstRSSET.getString("VS_MOBFL"),"").equals("Y"))
						tblVSTDTL4.setValueAt(new Boolean(true),L_ROWNO,TB5_MOBFL);
					else
						tblVSTDTL4.setValueAt(new Boolean(false),L_ROWNO,TB5_MOBFL);
				
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSARA"),""),L_ROWNO,TB5_VSARA);
				tblVSTDTL4.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO,TB5_REMDS);
			
				L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
				L_strTEMP="";
				if (L_tmpTIME != null)
				{
					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
					tblVSTDTL4.setValueAt(L_strTEMP,L_ROWNO,TB5_VSITM);
				}
				L_ROWNO ++;
				//for counting the visitors which in in..
			
			}
			M_rstRSSET.close();
			
			}
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getWTGVST");
	}
    
}
private void getINCNT()
{
 	try
 	{
     	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
     	{
         	int L_ROWNO1 = 0;
        	java.sql.Timestamp L_tmpTIME;
        	java.sql.Date L_tmpDATE;
        	String L_strSQLQRY ="";
        	ResultSet L_rstRSSET;
			intVSCNT =0;
        	String L_strTEMP="",L_strTVSPNO="";
        	M_strSQLQRY ="Select * from HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND VS_CMPCD=RM_CMPCD AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='HC' and RM_TRNCD ='02' where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,' ') <> 'X' ";
        	M_strSQLQRY+= " and VS_VSOTM IS NULL and VS_VSTTP ='02'";
        	M_strSQLQRY+= " order by VS_VSPNO ";
			//System.out.println("txtCNTDTL : "+M_strSQLQRY);
        	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
        	if(M_rstRSSET !=null)
        	{
        		while(M_rstRSSET.next())
        		{
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO1,TB3_VSTNM);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),""),L_ROWNO1,TB3_VSICT);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),""),L_ROWNO1,TB3_VSPNO);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_CRDNM"),""),L_ROWNO1,TB3_CRDNM);
					tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),""),L_ROWNO1,TB3_VSOCT);
					tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_SHFCD"),""),L_ROWNO1,TB3_SHFCD);
					tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PRTCD"),""),L_ROWNO1,TB3_PRTCD);
        			L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
        			L_strTEMP="";
        			if (L_tmpTIME != null)
        			{
        				L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
        				tblCNTDTL.setValueAt(L_strTEMP,L_ROWNO1,TB3_VSITM);
        			}
        			L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSOTM");
        			L_strTEMP="";
        			if (L_tmpTIME != null)
        			{
        				L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
        				tblCNTDTL.setValueAt(L_strTEMP,L_ROWNO1,TB3_VSOTM);
        			}
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_GINBY"),""),L_ROWNO1,TB3_GINBY);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO1,TB3_REMDS);
					intVSOCT = M_rstRSSET.getInt("VS_VSOCT");
					intVSICT = M_rstRSSET.getInt("VS_VSICT");
					intVSICT = intVSICT-intVSOCT;
        			L_ROWNO1 ++;
					intVSCNT += intVSICT;
        		}
        	    M_rstRSSET.close();
				lblVSICT1.setText(String.valueOf(intVSCNT));
				intVSCNT=0;intVSICT=0;intVSOCT=0;
        	}
     	}
    }
 	catch(Exception L_E)
 	{
        setMSG(L_E,"getINCNT");
 	}
}
private void getCNTDT()
{
 	try
 	{
     	if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
     	{
         	int L_ROWNO1 = 0;
        	java.sql.Timestamp L_tmpTIME;
        	java.sql.Date L_tmpDATE;
        	String L_strSQLQRY ="";
        	ResultSet L_rstRSSET;
			intVSCNT=0;
        	String L_strTEMP="",L_strTVSPNO="";
        	M_strSQLQRY ="Select * from HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND VS_CMPCD = RM_CMPCD AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='HC' and RM_TRNCD ='02' where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,' ') <> 'X' ";
        	M_strSQLQRY+= " and VS_VSTTP ='02' AND VS_VSTDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVSTDT1.getText().trim()))+"'";
        	M_strSQLQRY+= " order by VS_VSPNO ";
			//System.out.println("tblCNTDTL(getCNTDT) : "+M_strSQLQRY);
        	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
        	if(M_rstRSSET !=null)
        	{
        		while(M_rstRSSET.next())
        		{
        			L_tmpDATE = M_rstRSSET.getDate("VS_VSTDT");
        			L_strTEMP="";
        			if(L_tmpDATE != null)
        				L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
        			txtVSTDT1.setText(L_strTEMP);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO1,TB3_VSTNM);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),""),L_ROWNO1,TB3_VSICT);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),""),L_ROWNO1,TB3_VSPNO);
					tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),""),L_ROWNO1,TB3_VSOCT);
					tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_SHFCD"),""),L_ROWNO1,TB3_SHFCD);
					tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PRTCD"),""),L_ROWNO1,TB3_PRTCD);
     
        			L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
        			L_strTEMP="";
        			if (L_tmpTIME != null)
        			{
        				L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
        				tblCNTDTL.setValueAt(L_strTEMP,L_ROWNO1,TB3_VSITM);
        			}
        			L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSOTM");
        			L_strTEMP="";
        			if (L_tmpTIME != null)
        			{
        				L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
        				tblCNTDTL.setValueAt(L_strTEMP,L_ROWNO1,TB3_VSOTM);
        			}
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_GINBY"),""),L_ROWNO1,TB3_GINBY);
        			tblCNTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO1,TB3_REMDS);
					intVSOCT = M_rstRSSET.getInt("VS_VSOCT");
					intVSICT = M_rstRSSET.getInt("VS_VSICT");
					intVSICT = intVSICT-intVSOCT;
        			L_ROWNO1 ++;
					intVSCNT += intVSICT;
        			}
	       	        M_rstRSSET.close();
					lblVSICT1.setText(String.valueOf(intVSCNT));
        	}
     	}
    }
 	catch(Exception L_E)
 	{
        setMSG(L_E,"getINCNT");
 	}
}
private void getOTVST()
{        
	try  
	{    
		int L_ROWNO = 0;
		java.sql.Timestamp L_tmpTIME;
		java.sql.Date L_tmpDATE;
		String L_strSQLQRY ="";
		ResultSet L_rstRSSET;
		String L_strTEMP="",L_strTVSPNO="",L_strTTODAT="",L_strTFMDAT="";
		if((txtTODAT.getText().trim().length() ==0)||(txtFMDAT.getText().trim().length() ==0))
		    return;
		L_strTTODAT=M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
		L_strTFMDAT=M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
		if(jtpMANTAB.getSelectedIndex() == 1)
		{
			M_strSQLQRY ="Select * from HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND VS_CMPCD=RM_CMPCD AND RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP ='HC' and RM_TRNCD ='01' where ";
		    M_strSQLQRY+= " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(VS_STSFL,' ') <> 'X' and VS_VSTTP ='01'";
			M_strSQLQRY+= " and VS_VSOCT >0 ";
			M_strSQLQRY+= " and CONVERT(varchar,VS_VSITM,103) BETWEEN '"+L_strTFMDAT+"' and '"+L_strTTODAT+"'";
			M_strSQLQRY+= " order by VS_VSPNO ";
			//System.out.println("tblVSTDTL2 : "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
    			while(M_rstRSSET.next())
    			{
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO,TB1_VSTNM);
    	 			L_strTVSPNO = nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),"");
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),""),L_ROWNO,TB1_VSPNO);
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSICT"),""),L_ROWNO,TB1_VSICT);
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSOCT"),""),L_ROWNO,TB1_VSOCT);
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),""),L_ROWNO,TB1_VSORG);
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO,TB1_REMDS);
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""),L_ROWNO,TB1_PERVS);
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""),L_ROWNO,TB1_EMPNO);
    				tblVSTDTL2.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_CLRBY"),""),L_ROWNO,TB1_CLRBY);
					
					L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSOTM");
    				L_strTEMP="";
    				if (L_tmpTIME != null)
    				{
    					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
    					tblVSTDTL2.setValueAt(L_strTEMP,L_ROWNO,TB1_VSITM);
    				}
    				L_ROWNO ++;
    			}
			M_rstRSSET.close();
			}
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getOTVST");
	}
}//visitors........

private void inlTBLEDIT(JTable P_tblTBLNM)
{
	if(!P_tblTBLNM.isEditing())
		return;
	P_tblTBLNM.getCellEditor().stopCellEditing();
	P_tblTBLNM.setRowSelectionInterval(0,0);
	P_tblTBLNM.setColumnSelectionInterval(0,0);
	
}


	private boolean exeADDREC()
	{
		try
		{
			String L_strGOTDT="";
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			if(!genGINNO(strGINTP))
			{
				setMSG("Error in Number Generation..",'E');
				return false;
			}
			strGINNO = txtGINNO.getText().trim();
			strGINDT = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtGINDT.getText().trim()+" "+txtGINTM.getText().trim()))+"'"; 
			strGINBY = txtGINBY.getText().trim();
			strGOTBY = txtGOTBY.getText().trim();
			L_strGOTDT = txtGOTDT.getText().trim()+" "+txtGOTTM.getText().trim();
			if(L_strGOTDT.trim().length() >0)
				strGOTDT = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strGOTDT))+"'"; 
			else
				strGOTDT = "null";
			strORDRF = txtORDRF.getText().trim();
			if(txtORDDT.getText().trim().length() >0)
				strORDDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtORDDT.getText().trim()))+"'"; 			
			else
				strORDDT = "null";
			strORDQT = nvlSTRVL(txtORDQT.getText().trim(),"0");
			strLRYNO = txtLRYNO.getText().trim().toUpperCase();
			strMATCD = txtMATCD.getText().trim();
			strMATDS = txtMATDS.getText().trim().toUpperCase();
			strTPRCD = txtTPRCD.getText().trim();
			strTPRDS = txtTPRDS.getText().trim();
			strVENCD = txtVENCD.getText().trim();
			strVENDS = txtVENDS.getText().trim();
			strREMGT = txtREMGT.getText().trim();
			if((txtREPDT.getText().trim()+txtREPTM.getText().trim()).length() >0)
				strREPTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtREPDT.getText().trim()+" "+txtREPTM.getText().trim()))+"'"; 			
			else
				strREPTM = "null";
			strLUSBY = cl_dat.M_strUSRCD_pbst.trim();
			strLUPDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()))+"'"; 			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				// Status flag "0" denotes that Gate-In entry has been done
				strSTSFL = "0";
				M_strSQLQRY = "Insert into MM_WBTRN(WB_CMPCD,WB_DOCTP,WB_DOCNO,WB_SRLNO,";
				M_strSQLQRY += "WB_GINDT,WB_GINBY,";
				if(strGINTP.equals(strRAWMT_fn) || strGINTP.equals(strOTHER_fn))// Raw Material/ Other 
					M_strSQLQRY += "WB_CHLNO,WB_CHLDT,WB_CHLQT,";
			  	else if(strGINTP.equals(strDESPT_fn))
					M_strSQLQRY += "WB_ORDRF,WB_ORDDT,WB_LCCQT,";
				else if(strGINTP.equals(strSLSRT_fn))
					M_strSQLQRY += "WB_CHLNO,WB_CHLDT,WB_CHLQT,WB_ORDRF,WB_PRTCD,WB_PRTDS,";
				else
					M_strSQLQRY += "WB_CHLNO,WB_CHLDT,WB_CHLQT,WB_ORDRF,WB_PRTCD,WB_PRTDS,";
				M_strSQLQRY += "WB_LRYNO,WB_MATCD,WB_MATDS,WB_TPRCD,";
				M_strSQLQRY += "WB_TPRDS,WB_REMGT,WB_REPTM,WB_ACPTG,WB_VSICT,";
				M_strSQLQRY += "WB_DRVCD,WB_DRVNM,";
				M_strSQLQRY += "WB_STSFL,WB_TRNFL,WB_LUSBY,WB_LUPDT) values (";
				M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
				M_strSQLQRY += "'" + strGINTP + "',";
				M_strSQLQRY += "'" + strGINNO + "',";
				M_strSQLQRY += "'1',current_timestamp,";					// Serial No.
			//	M_strSQLQRY += strGINDT + ",";
				M_strSQLQRY += "'" + strGINBY + "',";
				if(strGINTP.equals(strRAWMT_fn) || strGINTP.equals(strOTHER_fn))	
				{
					M_strSQLQRY += "'" + strORDRF + "',";
					M_strSQLQRY += strORDDT + ",";
					M_strSQLQRY += strORDQT + ",";
				}
				else if(strGINTP.equals(strDESPT_fn))					
				{
					M_strSQLQRY += "'" + strORDRF + "',";
					M_strSQLQRY += strORDDT + ",";
					M_strSQLQRY += txtLCCQT.getText().trim() + ",";
				}
				else if((strGINTP.equals(strRECPT_fn))||(strGINTP.equals(strSLSRT_fn)))					
				{
					M_strSQLQRY += "'" + strORDRF + "',";
					M_strSQLQRY += strORDDT + ",";
					M_strSQLQRY += strORDQT + ",";
					M_strSQLQRY += "'"+txtPORNO.getText().trim() + "',";
					M_strSQLQRY += "'"+txtVENCD.getText().trim() + "',";
					M_strSQLQRY += "'"+txtVENDS.getText().trim() + "',";
				}
				M_strSQLQRY += "'" + strLRYNO + "',";
				M_strSQLQRY += "'" + strMATCD + "',";
				M_strSQLQRY += "'" + strMATDS + "',";
				M_strSQLQRY += "'" + strTPRCD + "',";
				M_strSQLQRY += "'" + strTPRDS + "',";
				M_strSQLQRY += "'" + txtREMGT.getText().trim() + "',";
				M_strSQLQRY += strREPTM + ",";
				M_strSQLQRY += "'" + strACPTG_fn + "',";
				M_strSQLQRY += txtGINCT.getText().trim() + ",";
				M_strSQLQRY += "'"+txtDRVCD.getText()+"',";
				M_strSQLQRY += "'"+txtDRVNM.getText()+"',";
				M_strSQLQRY += "'" + strSTSFL + "',";
				M_strSQLQRY += "'" +"0" + "',";
				M_strSQLQRY += "'" + strLUSBY + "',";
				M_strSQLQRY += strLUPDT + ")";
				
				if(!flgDRV_EXIST)
					insDVMST();
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "Update MM_WBTRN set ";
			// Commented on 11/06/2005 to block out time modification
			/*	M_strSQLQRY += "WB_GINDT = current_timestamp,";
				M_strSQLQRY += "WB_GINBY = '" + strGINBY + "',";
				M_strSQLQRY += "WB_GOTDT = " + strGOTDT + ",";
				M_strSQLQRY += "WB_GOTBY = '" + strGOTBY + "',";*/
				if(strGINTP.equals(strRAWMT_fn) || strGINTP.equals(strOTHER_fn)){// Raw Material/ Other 
					M_strSQLQRY += "WB_CHLNO = '" + strORDRF + "',";
				M_strSQLQRY += "WB_CHLDT = " + strORDDT + ",";
				M_strSQLQRY += "WB_CHLQT = " + strORDQT + ",";
				M_strSQLQRY += "WB_DRVCD = '"+txtDRVCD.getText().trim()+"',";
				M_strSQLQRY += "WB_DRVNM = '"+txtDRVNM.getText().trim()+"',";
			}
			else if (strGINTP.equals(strDESPT_fn))
			{	
				M_strSQLQRY += "WB_ORDRF = '" + strORDRF + "',";
				M_strSQLQRY += "WB_ORDDT = " + strORDDT + ",";
				M_strSQLQRY += "WB_LCCQT = " + txtLCCQT.getText().trim() + ",";
			}
			else
			{
				M_strSQLQRY += "WB_CHLNO = '" + strORDRF + "',";
				M_strSQLQRY += "WB_CHLDT = " + strORDDT + ",";
				M_strSQLQRY += "WB_CHLQT = " + strORDQT + ",";
				M_strSQLQRY += "WB_ORDRF = '" + txtPORNO.getText().trim() + "',";
				M_strSQLQRY += "WB_PRTCD = '" + txtVENCD.getText().trim() + "',";
				M_strSQLQRY += "WB_PRTDS = '" + txtVENDS.getText().trim() + "',";
			}
			M_strSQLQRY += "WB_LRYNO = '" + strLRYNO + "',";
			M_strSQLQRY += "WB_MATCD = '" + strMATCD + "',";
			M_strSQLQRY += "WB_MATDS = '" + strMATDS + "',";
			M_strSQLQRY += "WB_TPRCD = '" + strTPRCD + "',";
			M_strSQLQRY += "WB_TPRDS = '" + strTPRDS + "',";
			M_strSQLQRY += "WB_REMGT = '" + strREMGT + "',";
			M_strSQLQRY += "WB_REPTM = " + strREPTM + ",";
			M_strSQLQRY += "WB_VSOCT = " + txtGOTCT.getText().trim() + ",";
			M_strSQLQRY += "WB_STSFL = '" + strSTSFL + "',";
			M_strSQLQRY += "WB_TRNFL = '" + strTRNFL_fn + "',";
			M_strSQLQRY += "WB_LUSBY = '" + strLUSBY + "',";
			M_strSQLQRY += "WB_LUPDT = " + strLUPDT;
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + strGINNO + "'";				
			M_strSQLQRY += " and WB_SRLNO = '1'";
			
			if(!DRV_EXIST(txtDRVCD.getText()))
				insDVMST();
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{
			//if(strSTSFL.equals("0"))
			if(strGINTP.equals(strDESPT_fn) || strGINTP.equals(strOTHER_fn)||strGINTP.equals(strSLSRT_fn))					
			{
				if(!strSTSFL.equals("0"))
				{
					txtGINNO.requestFocus();
					setMSG("Can not delete this record.Record is referenced somewhere.",'E');
					return false;
				}
			}	
			else if(strGINTP.equals(strRECPT_fn) || strGINTP.equals(strRAWMT_fn))					
			{
				if(strDOCRF.length() >0)
				{
					txtGINNO.requestFocus();
					setMSG("Can not delete this record,as GRIN is prepared..",'E');
					return false;
				}
			}
			M_strSQLQRY = "Update MM_WBTRN set  ";
			M_strSQLQRY += "WB_STSFL = 'X',";
			M_strSQLQRY += "WB_TRNFL = '" + strTRNFL_fn + "',";
			M_strSQLQRY += "WB_LUSBY = '" + strLUSBY + "',";
			M_strSQLQRY += "WB_LUPDT = " + strLUPDT;
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + strGINNO + "'";				
		}
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			if(cl_dat.M_flgLCUPD_pbst)
				// To save the Last Gate-In No.in CO_CDTRN table
				exeMEMNO(strGINTP,strGINNO,strLUSBY,strLUPDT);
			if(cl_dat.M_flgLCUPD_pbst)
			if(strGINTP.equals(strDESPT_fn))
			{
				M_strSQLQRY = "Select count(*) L_CNT from MM_LRMST "; 
			    M_strSQLQRY += " where LR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LR_TPRCD = '"+txtTPRCD.getText().trim() +"' AND ";
				M_strSQLQRY += " LR_LRYNO ='"+txtLRYNO.getText().trim() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt("L_CNT") == 0)
						{
							M_strSQLQRY	 ="INSERT INTO MM_LRMST(LR_CMPCD,LR_TPRCD,LR_LRYNO,LR_LRYDS,LR_TRPCT,LR_TRNFL,LR_STSFL,LR_LUSBY,LR_LUPDT) values("
										 + "'"+ cl_dat.M_strCMPCD_pbst+"',"
										 + "'"+ txtTPRCD.getText().trim()+"',"
										 + "'"+ txtLRYNO.getText().trim()+"',"
										 + "'"+ txtLRYNO.getText().trim()+"',1,"
										 + getUSGDTL("LR",'I',"")+")";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
						else
						{
							M_strSQLQRY	 ="UPDATE MM_LRMST SET LR_TRPCT = LR_TRPCT +1 where LR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LR_LRYNO = "
										 + "'"+ txtLRYNO.getText().trim()+"' AND LR_TPRCD ="
										 + "'"+ txtTPRCD.getText().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
					}
			}
			
		}
		if(cl_dat.M_flgLCUPD_pbst)
		{
			if(cl_dat.exeDBCMT("exeADDREC"))
			{
				setMSG("Record saved successfully",'N');
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{	
					//////////sending mail for black listed drivers
					M_strSQLQRY  = " Select DV_DRVCD,DV_DRVNM,DV_TPRCD,DV_LICNO,DV_LICBY,DV_LVLDT,isnull(DV_BLKCD,'00') DV_BLKCD,DV_REMDS" ;
					M_strSQLQRY += " from MM_DVMST where DV_DRVCD = '" + txtDRVCD.getText().trim() + "'" ;
					M_strSQLQRY += " and DV_STSFL <> 'X'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null && M_rstRSSET.next())
					{
						if(!nvlSTRVL(M_rstRSSET.getString("DV_BLKCD"),"00").equals("00"))
						{
							if(hstBLKCD.containsKey(M_rstRSSET.getString("DV_BLKCD")))
							{	
								String L_strSQLQRY = "select wb_gindt,wb_lryno,wb_docno from mm_wbtrn where CONVERT(varchar,wb_gindt,103) = (select max(CONVERT(varchar,wb_gindt,103)) from mm_wbtrn where wb_drvcd = '" + txtDRVCD.getText().trim() + "') and wb_drvcd = '" + txtDRVCD.getText().trim() + "'";
								ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
								if(L_rstRSSET!=null && L_rstRSSET.next())
								{	
									cl_eml ocl_eml = new cl_eml();
									String strMAIL = "";
									strMAIL += "BLACK LISTED DRIVER ENTRY DETAILS : \n";
									strMAIL += "Driver Name :"+M_rstRSSET.getString("DV_DRVNM")+"\n";
									strMAIL += "Licence No. :"+M_rstRSSET.getString("DV_LICNO")+"\n";
									strMAIL += "Reason      :"+hstBLKCD.get(M_rstRSSET.getString("DV_BLKCD"))+"\n";
									strMAIL += "Remark      :"+M_rstRSSET.getString("DV_REMDS")+"\n";
									strMAIL += "Gate In No. :"+txtGINNO.getText()+"\n";
									strMAIL += "Entry Time  :"+txtGINTM.getText()+"\n";
									strMAIL += "Previous Gate In Date :"+M_fmtLCDAT.format(L_rstRSSET.getDate("wb_gindt"))+"\n";
									strMAIL += "Previous Lorry No.    :"+L_rstRSSET.getString("wb_lryno")+"\n";
									strMAIL += "Previous Gate In No.  :"+L_rstRSSET.getString("wb_docno")+"\n";
									ocl_eml.sendfile("ap_deshmukh@spl.co.in",null,"Black Listed Driver",strMAIL);
									ocl_eml.sendfile("rs_pareek@spl.co.in",null,"Black Listed Driver",strMAIL);
									ocl_eml.sendfile("kv_mujumdar@spl.co.in",null,"Black Listed Driver",strMAIL);
									//ocl_eml.sendfile("systems_works@spl.co.in",null,"Black Listed Driver",strMAIL);
								}	
							}	
						}	
					}
				}	
				return true;
			}
		}
		else
		{
			setMSG("Error in saving Data.. ",'E');
			return false;
		}
	}
	catch(Exception e)
	{
		setMSG("Error in Saving..",'E');
		return false;
	}
	return true;
	}
	
	private void insDVMST()
	{
		try
		{
			String M_strSQLQRY1="";
			M_strSQLQRY1 = "Insert into MM_DVMST(DV_CMPCD,DV_DRVCD,DV_DRVNM,DV_TPRCD,DV_LICNO,DV_LICBY,";
			M_strSQLQRY1 += "DV_LVLDT,DV_STSFL,DV_TRNFL,DV_LUSBY,DV_LUPDT) values (" ;
			M_strSQLQRY1 += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY1 += "'"+txtDRVCD.getText().trim()+"',";
			M_strSQLQRY1 += "'"+txtDRVNM.getText().trim()+"',";
			M_strSQLQRY1 += "'"+txtTPRCD.getText().trim()+"',";
			M_strSQLQRY1 += "'"+txtLICNO.getText().trim()+"',";
			M_strSQLQRY1 += "'"+txtLICBY.getText().trim()+"',";
			M_strSQLQRY1 += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtLVLDT.getText().trim()))	+"',";
			M_strSQLQRY1 += "'0',";
			M_strSQLQRY1 += "'0',";
			M_strSQLQRY1 += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY1 += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY1,"setLCLUPD");		
			//System.out.println(">>>>>insDVMST>>>>>>>"+M_strSQLQRY);
		}	
		catch(Exception L_EX)
		{
		   setMSG(L_EX,"insDVMST()");		
		}
	}
	
	// Method to get the record from MMSDATA/MM_WBTRN
	private void exeGETREC(String P_strGINTP,String P_strGINNO,String P_strSRLNO)
	{
		try
		{
	
			M_strSQLQRY = "Select WB_DOCTP,WB_DOCNO,WB_GINBY,WB_GOTBY,WB_DOCRF,";
			M_strSQLQRY += "WB_CHLNO,WB_CHLDT,WB_CHLQT,";				
			M_strSQLQRY += "WB_ORDRF,WB_ORDDT,WB_PRTCD,WB_PRTDS,";                    
			M_strSQLQRY += "WB_GINDT,WB_GOTDT,WB_MATTP,WB_MATCD,WB_MATDS,WB_LRYNO,WB_TPRCD,";
			M_strSQLQRY += "WB_TPRDS,WB_DRVCD,WB_DRVNM,WB_REMGT,WB_REPTM,WB_STSFL,WB_EXAPR,WB_VSICT,WB_VSOCT,WB_LCCQT";
			M_strSQLQRY += " from MM_WBTRN where ";
			M_strSQLQRY += " WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			M_strSQLQRY += " and WB_SRLNO = '" + P_strSRLNO + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				strGINTP = M_rstRSSET.getString("WB_DOCTP");
				strGINNO = M_rstRSSET.getString("WB_DOCNO");
				strGINDT = M_rstRSSET.getString("WB_GINDT");
				strGINBY = M_rstRSSET.getString("WB_GINBY");
				strGOTBY = M_rstRSSET.getString("WB_GOTBY");
				strGOTDT = M_rstRSSET.getString("WB_GOTDT");
				if(P_strGINTP.equals(strRAWMT_fn))
				{
					strORDRF = M_rstRSSET.getString("WB_CHLNO");
					datTEMP = M_rstRSSET.getDate("WB_CHLDT");
					if(datTEMP !=null)
					strORDDT =M_fmtLCDAT.format(datTEMP);	
					strORDQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"");
				}
				else if(P_strGINTP.equals(strRECPT_fn))
				{
					strORDRF = M_rstRSSET.getString("WB_CHLNO");
					datTEMP = M_rstRSSET.getDate("WB_CHLDT");
					if(datTEMP !=null)
					strORDDT =M_fmtLCDAT.format(datTEMP);	
					strORDQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"");
					strVENCD = nvlSTRVL(M_rstRSSET.getString("WB_PRTCD"),"");
					strVENDS = nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),"");
					strPORNO = nvlSTRVL(M_rstRSSET.getString("WB_ORDRF"),"");
					txtPORNO.setText(strPORNO);
					txtVENCD.setText(strVENCD);
					txtVENDS.setText(strVENDS);
				}
				else if(P_strGINTP.equals(strSLSRT_fn))
				{
					strORDRF = M_rstRSSET.getString("WB_CHLNO");
					datTEMP = M_rstRSSET.getDate("WB_CHLDT");
					if(datTEMP !=null)
					strORDDT =M_fmtLCDAT.format(datTEMP);	
					strORDQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"");
					strVENCD = nvlSTRVL(M_rstRSSET.getString("WB_PRTCD"),"");
					strVENDS = nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),"");
					strPORNO = nvlSTRVL(M_rstRSSET.getString("WB_ORDRF"),"");
					txtPORNO.setText(strPORNO);
					txtVENCD.setText(strVENCD);
					txtVENDS.setText(strVENDS);
				}
				else 
				{
					strORDRF = M_rstRSSET.getString("WB_ORDRF");
					datTEMP = M_rstRSSET.getDate("WB_ORDDT");
					if(datTEMP !=null)
					strORDDT =M_fmtLCDAT.format(datTEMP);	
					strORDQT ="";
				}
				strEXAPR = nvlSTRVL(M_rstRSSET.getString("WB_EXAPR"),"");
				strLRYNO = M_rstRSSET.getString("WB_LRYNO");
				strMATCD = M_rstRSSET.getString("WB_MATCD");
				strMATDS = M_rstRSSET.getString("WB_MATDS");
				strTPRCD = M_rstRSSET.getString("WB_TPRCD");
				strTPRDS = M_rstRSSET.getString("WB_TPRDS");
				strREMGT = M_rstRSSET.getString("WB_REMGT");
				strREPTM = M_rstRSSET.getString("WB_REPTM");
				strSTSFL = nvlSTRVL(M_rstRSSET.getString("WB_STSFL"),"");
				strDOCRF = nvlSTRVL(M_rstRSSET.getString("WB_DOCRF"),"");
				txtGINNO.setText(strGINNO);
				tmsTEMP = M_rstRSSET.getTimestamp("WB_GINDT");
				String strTEMP ="";
				if(tmsTEMP !=null)
				{
					strTEMP = M_fmtLCDTM.format(tmsTEMP);
					txtGINDT.setText(strTEMP.substring(0,10));	
					txtGINTM.setText(strTEMP.substring(11));	
				}
				txtGINBY.setText(strGINBY);
				tmsTEMP = M_rstRSSET.getTimestamp("WB_GOTDT");
				if(tmsTEMP !=null)
				{
					strTEMP = M_fmtLCDTM.format(tmsTEMP);
					txtGOTDT.setText(strTEMP.substring(0,10));	
					txtGOTTM.setText(strTEMP.substring(11));	
				}
				txtGOTBY.setText(strGOTBY);
				txtORDRF.setText(strORDRF);
			/*	if(strORDDT.equals("null"))
					txtORDDT.setText("");
				else
					txtORDDT.setText(strORDDT);*/
				if(strORDDT != null)
				{
					if(strORDDT.equals("null"))
						txtORDDT.setText("");
					else
						txtORDDT.setText(strORDDT);
				}
				else
				{
					txtORDDT.setText("");
				
				}
				txtORDQT.setText(strORDQT);
				txtLRYNO.setText(strLRYNO);
				txtMATCD.setText(strMATCD);
				txtMATDS.setText(strMATDS);
				txtTPRCD.setText(strTPRCD);
				txtTPRDS.setText(strTPRDS);
				txtDRVCD.setText(nvlSTRVL(M_rstRSSET.getString("WB_DRVCD"),""));
				txtDRVNM.setText(nvlSTRVL(M_rstRSSET.getString("WB_DRVNM"),""));
				txtREMGT.setText(strREMGT);
				txtDOCRF.setText(nvlSTRVL(M_rstRSSET.getString("WB_DOCRF"),""));
				txtGINCT.setText(nvlSTRVL(M_rstRSSET.getString("WB_VSICT"),"0"));
				txtGOTCT.setText(nvlSTRVL(M_rstRSSET.getString("WB_VSOCT"),"0"));
				txtLCCQT.setText(M_rstRSSET.getString("WB_LCCQT"));
				tmsTEMP = M_rstRSSET.getTimestamp("WB_REPTM");
				if(M_rstRSSET.getString("WB_DRVCD") != null)
					exeGETDRV1(M_rstRSSET.getString("WB_DRVCD"));
				if(tmsTEMP !=null)
				{
					strTEMP = M_fmtLCDTM.format(tmsTEMP);
					txtREPDT.setText(strTEMP.substring(0,10));
					txtREPTM.setText(strTEMP.substring(11));
				}
			}
			else
				setMSG("Record not found!",'E');
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
		}catch(Exception e){
			setMSG(e,"exeGETREC ");
		}
	}
	
	// Method to get the Material Description from Catalogue Master
	private String getMATDS(String P_strMATCD)
	{
		String L_strMATDS = "";
		try
		{
			M_strSQLQRY = "Select CT_MATDS from CO_CTMST ";
			M_strSQLQRY += " where CT_MATCD = '" + P_strMATCD.trim() + "'";
			
			rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(rstRSSET.next())
			{
				L_strMATDS = rstRSSET.getString("CT_MATDS");
				rstRSSET.close();
			}
		}
		catch(Exception e)
		{
			setMSG(e,"getMATDS ");
		}
		return L_strMATDS;
	}
	
	// Method to get the Party Description from Party Master
	private String getPRTDS(String P_strPRTTP,String P_strPRTCD)
	{
		String L_strPRTDS = "";
		try{
			M_strSQLQRY = "Select PT_PRTNM from CO_PTMST ";
			M_strSQLQRY += " where PT_PRTTP = '" + P_strPRTTP + "'";
			M_strSQLQRY += " and PT_PRTCD = '" + P_strPRTCD + "'";
			
			rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(rstRSSET !=null)
			if(rstRSSET.next())
			{
				L_strPRTDS = rstRSSET.getString("PT_PRTNM");
				rstRSSET.close();
			}
		}
		catch(Exception e){
			setMSG(e,"getPRTDS ");
		}
		return L_strPRTDS;
	}
	
	// Method to generate the new Gate-In No. depending upon the Gate-In type
	private boolean genGINNO(String P_strGINTP){
		try{
			if(P_strGINTP.equals(strRECPT_fn))
				P_strGINTP ="00";
			String L_strGINNO  = "",  L_CODCD = "", L_CCSVL = "";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXWBT' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strGINTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = M_rstRSSET.getString("CMT_CODCD");
					L_CCSVL = M_rstRSSET.getString("CMT_CCSVL");
					M_rstRSSET.close();
				}
			}
			
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
			
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_strGINNO += "0";
			
			L_CCSVL = L_strGINNO + L_CCSVL;
			L_strGINNO = L_CODCD + L_CCSVL;
			txtGINNO.setText(L_strGINNO);
			return true;
		}catch(Exception L_EX){
			setMSG(L_EX,"genGINNO");
			return false;
		}
	}
	
	// Method to update the last Gate-In No.in the CO_CDTRN
	private void exeMEMNO(String P_strGINTP,String P_strGINNO,String P_strLUSBY,String P_strLUPDT)
	{
		try
		{
			if(P_strGINTP.equals(strRECPT_fn))
				P_strGINTP ="00";
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CCSVL = '" + P_strGINNO.substring(3,8) + "',";
			M_strSQLQRY += " CMT_TRNFL = '" + strTRNFL_fn + "',";			
			M_strSQLQRY += " CMT_LUSBY = '" + P_strLUSBY + "',";			
			M_strSQLQRY += " CMT_LUPDT = " + P_strLUPDT;			
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MMXXWBT'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) +P_strGINTP +"'";			
				
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch(Exception e)
		{
			setMSG(e,"exeMEMNO ");
		}
	}

	// Method to fetch the records of the Lorry's waiting for Gate-Out
	private void getINLRY()
	{
		try
		{
			intROWCT = 0;
			intLRYCT =0;
			double L_dblNETQT = 0,L_dblCHLQT = 0;
            String L_strGINNO,L_strLRYNO,L_strGINTP,L_strGINDT,L_strACTQT,L_strNETWT,L_strGOTDT,L_strGOTBY,L_strREMGT,L_strMATDS;
            L_strGINTP = String.valueOf(cmbGINTP1.getSelectedItem()).substring(0,2);
			L_strGINDT =""; ///
			L_strGOTDT = cl_dat.getCURDTM();
			L_strGOTBY = cl_dat.M_strUSRCD_pbst;
			strEXAPR ="";
			if(L_strGINTP.equals(strDESPT_fn))				// Despatch
				M_rstRSSET = pstmINLRY1.executeQuery();
			else
			{
				pstmINLRY2.setString(1,L_strGINTP);
				M_rstRSSET = pstmINLRY2.executeQuery();
			}
			intLRYCT =0;
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_strGINNO = M_rstRSSET.getString("WB_DOCNO");
                L_strLRYNO = M_rstRSSET.getString("WB_LRYNO");
                L_strMATDS = M_rstRSSET.getString("WB_MATDS");
				strEXAPR =nvlSTRVL(M_rstRSSET.getString("WB_EXAPR"),"");
				if(L_strGINTP.equals(strDESPT_fn))				// Despatch
				{
					//L_strACTQT = setFMT(nvlSTRVL(M_rstRSSET.getString("L_strINVQT"),"0"),3);
					//** Replace By ATC
					L_strACTQT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_strINVQT"),"0")),3);
					flgPSADD = false;		
				}
				else
				{
					//L_strACTQT = setFMT(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0"),3);
					//**Replace by ATC
					L_strACTQT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0")),3);
					if(L_strGINTP.equals(strRAWMT_fn))				
						flgRMADD = false;		
					else if(L_strGINTP.equals(strRECPT_fn))				
						flgRCADD = false;		
					if(L_strGINTP.equals(strOTHER_fn))				
						flgOTADD = false;		
				}
				//L_strNETWT = setFMT(nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0"),3);
				//**Replace By ATC
				L_strNETWT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0")),3);
				
				L_dblCHLQT += Double.parseDouble(L_strACTQT);
				L_dblNETQT += Double.parseDouble(L_strNETWT);
				
				tmsTEMP = M_rstRSSET.getTimestamp("WB_GINDT");
				if(tmsTEMP !=null)
				   L_strGINDT =M_fmtLCDTM.format(tmsTEMP);	
		        L_strREMGT = M_rstRSSET.getString("WB_REMGT");
				if(exeGETLRY(L_strLRYNO.trim(),tblGINLR,intROWCT)==0)
				{	intLRYCT = intLRYCT + 1;
				}
				tblGINLR.setValueAt(String.valueOf(intROWCT+1),intROWCT,TBL_SRLNO);
				tblGINLR.setValueAt(L_strGINNO,intROWCT,TBL_strGINNO);
                tblGINLR.setValueAt(L_strLRYNO,intROWCT,TBL_LRYNO);
				tblGINLR.setValueAt(L_strMATDS,intROWCT,TBL_MATDS);
				tblGINLR.setValueAt(L_strACTQT,intROWCT,TBL_CHLQT);
				tblGINLR.setValueAt(L_strNETWT,intROWCT,TBL_NETWT);
				tblGINLR.setValueAt(L_strGINDT,intROWCT,TBL_GINDT);
				tblGINLR.setValueAt(strEXAPR,intROWCT,TBL_EXAPR);
                tblGINLR.setValueAt(L_strREMGT,intROWCT,TBL_REMGT);
                intROWCT++;             
			}
			
			lblGINSR.setText(String.valueOf(intLRYCT));
			//lblCHLIN.setText(setFMT(String.valueOf(L_dblCHLQT),3));
			//** Replace By ATC
			lblCHLIN.setText(setNumberFormat(L_dblCHLQT,3));
			//lblGINQT.setText(setFMT(String.valueOf(L_dblNETQT),3));
			//**Replacd By ATC
			lblGINQT.setText(setNumberFormat(L_dblNETQT,3));
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			if(L_strGINTP.equals(strDESPT_fn))				// Despatch
				pstmINLRY1.clearParameters();
			else
				pstmINLRY2.clearParameters();
		}catch(Exception e){
			setMSG(e,"getINLRY : ");
		}
		intSELPG = jtpGATEIO.getSelectedIndex();
		if(intSELPG == 1)
		{
				if(intROWCT > 0)
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
				else
					cl_dat.M_btnSAVE_pbst.setEnabled(false);
		}
	}
	
	// Method to fetch the records of the Lorry's gone outside 
	private void getOTLRY()
	{
		try
		{
			strFRMDT = txtFRMDT.getText().trim();
			strFRMTM = txtFRMTM.getText().trim();
			strTORDT = txtTORDT.getText().trim();
			strTORTM = txtTORTM.getText().trim();
			strFRDTM = txtFRMDT.getText().trim() +" "+txtFRMTM.getText().trim();
			strTODTM = txtTORDT.getText().trim() +" "+txtTORTM.getText().trim();
			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			M_calLOCAL.add(Calendar.DATE,+1);
			strCURDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
			
			if(strFRDTM.length() == 0){
				txtFRMDT.requestFocus();
				setMSG("Please enter the From Date/Time.",'E');
				return;
			}
			else
			{
				if(M_fmtLCDTM.parse(strFRDTM).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
				{		
					txtFRMDT.requestFocus();
						setMSG("From Date/Time can not be greater than current Date/Time ",'E');
						return;
				}
			}
			if(strTODTM.length() == 0)
			{
				txtTORDT.requestFocus();
				setMSG("Please enter the To Date/Time.",'E');
				return;
			}
			else
			{
				if(M_fmtLCDTM.parse(strFRDTM).compareTo(M_fmtLCDTM.parse(strTODTM))>0)
				{		
						txtFRMDT.requestFocus();
						setMSG("From Date/Time can not be greater than To Date/Time ",'E');
						return;
				}
			}
			setMSG("",'N');
					
			double L_dblNETQT = 0,L_dblCHLQT = 0;
            String L_strGINNO,L_strLRYNO,L_strGINTP,L_strGINDT,L_strACTQT,L_strNETWT,L_strGOTDT,L_strGOTBY,L_strREMGT,L_strMATDS;
            L_strGINTP = String.valueOf(cmbGINTP1.getSelectedItem()).substring(0,2);
			L_strGOTDT = cl_dat.getCURDTM();
			L_strGINDT =""; ///
			L_strGOTBY = cl_dat.M_strUSRCD_pbst;
			
			strFRDTM = strFRMDT.substring(6,10) + "-" + strFRMDT.substring(3,5) + "-" + strFRMDT.substring(0,2) + " "+strFRMTM.substring(0,2) + ":" + strFRMTM.substring(3) + ":00.000000000";
			strTODTM = strTORDT.substring(6,10) + "-" + strTORDT.substring(3,5) + "-" + strTORDT.substring(0,2) + " "+strTORTM.substring(0,2) + ":" + strTORTM.substring(3) + ":00.000000000";
			if(L_strGINTP.equals(strDESPT_fn)){				// Despatch
				pstmOTLRY1.setTimestamp(1,Timestamp.valueOf(strFRDTM));
				pstmOTLRY1.setTimestamp(2,Timestamp.valueOf(strTODTM));
				M_rstRSSET = pstmOTLRY1.executeQuery();
			}
			else
			{
				pstmOTLRY2.setString(1,L_strGINTP);
				pstmOTLRY2.setTimestamp(2,Timestamp.valueOf(strFRDTM));
				pstmOTLRY2.setTimestamp(3,Timestamp.valueOf(strTODTM));
				M_rstRSSET = pstmOTLRY2.executeQuery();
			}
			
			int i = 0;
			L_dblNETQT = 0;
			intOLRCT =0;
			while(M_rstRSSET.next())
			{
				L_strGINNO = M_rstRSSET.getString("WB_DOCNO");
                L_strLRYNO = M_rstRSSET.getString("WB_LRYNO");
                L_strMATDS = M_rstRSSET.getString("WB_MATDS");
				strEXAPR = nvlSTRVL(M_rstRSSET.getString("WB_EXAPR"),"");
				if(L_strGINTP.equals(strDESPT_fn))				// Despatch
					//L_strACTQT = setFMT(nvlSTRVL(M_rstRSSET.getString("L_strINVQT"),"0"),3);
					//** Replace By ATC
					L_strACTQT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_strINVQT"),"0")),3);
				else
					//L_strACTQT = setFMT(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0"),3);
					//** Replace By ATC
					L_strACTQT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0")),3);
				
				//L_strNETWT = setFMT(nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0"),3);
				//**Replace By ATC
				L_strNETWT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0")),3);
				
				L_dblCHLQT += Double.parseDouble(L_strACTQT);
				L_dblNETQT += Double.parseDouble(L_strNETWT);
				tmsTEMP = M_rstRSSET.getTimestamp("WB_GINDT");
				if(tmsTEMP !=null)
					L_strGINDT =M_fmtLCDTM.format(tmsTEMP);	
				tmsTEMP = M_rstRSSET.getTimestamp("WB_GOTDT");
				if(tmsTEMP !=null)
					L_strGOTDT =M_fmtLCDTM.format(tmsTEMP);	                
				L_strGOTBY = M_rstRSSET.getString("WB_GOTBY");
				L_strREMGT = M_rstRSSET.getString("WB_REMGT");
				if(exeGETLRY(L_strLRYNO,tblGOTLR,i)==0)
				{	intOLRCT = intOLRCT + 1;
				}
			    tblGOTLR.setValueAt(String.valueOf(i+1),i,TBL_SRLNO);
				tblGOTLR.setValueAt(L_strGINNO,i,TBL_strGINNO);
                tblGOTLR.setValueAt(L_strLRYNO,i,TBL_LRYNO);
				tblGOTLR.setValueAt(L_strMATDS,i,TBL_MATDS);
				tblGOTLR.setValueAt(L_strACTQT,i,TBL_CHLQT);
				tblGOTLR.setValueAt(L_strNETWT,i,TBL_NETWT);
				tblGOTLR.setValueAt(L_strGINDT,i,TBL_GINDT);
				tblGOTLR.setValueAt(strEXAPR,i,TBL_EXAPR);
				tblGOTLR.setValueAt(L_strGOTDT,i,TBL_GOTDT);
				tblGOTLR.setValueAt(L_strGOTBY,i,TBL_GOTBY);
                tblGOTLR.setValueAt(L_strREMGT,i,TBL_REMGT);
                i++;             
			}
			lblGOTSR.setText(String.valueOf(intOLRCT));
			//lblCHLOT.setText(setFMT(String.valueOf(L_dblCHLQT),3));
			//** Replace By ATC
			lblCHLOT.setText(setNumberFormat(L_dblCHLQT,3));
			//lblGOTQT.setText(setFMT(String.valueOf(L_dblNETQT),3));
			//** Replace By ATC
			lblGOTQT.setText(setNumberFormat(L_dblNETQT,3));
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			if(L_strGINTP.equals(strDESPT_fn))				// Despatch
				pstmOTLRY1.clearParameters();
			else
				pstmOTLRY2.clearParameters();
		}catch(Exception e){
			setMSG(e,"getOTLRY ");
		}
	}
	// Method to update the Gate-Out Entry
	private boolean exeUPDREC()
	{
		boolean L_flgUPDFL = true;
		try
		{
		    String L_strEMLAD="";
		    String L_strFILNM=cl_dat.M_strREPSTR_pbst+"temp.txt";
			//File L_strFILNM = new File("c:\\reports\\temp.txt");
			FileOutputStream fosREPORT = null;
			DataOutputStream dosREPORT = null;
			int L_intCNT,L_intDTCNT = 0;
			String L_strGINNOS = "";
			String L_strGINTP,L_strGINNO,L_strGOTDT,L_strGOTBY,L_strCHLQT,L_strREMGT,L_strTRPCD ="";
			String L_strINVQT,L_strBYRCD,L_strBYRNM,L_strCNSCD,L_strDSTCD,L_strDSTNM="",L_strTRPNM="",L_strPRDDS,L_strLRYNO="",L_strINVNO,L_strPINVNO="",L_strLADNO="";
			String L_strEMLID ="";
			int L_intSRLNO =0;
			strSTSFL = "9";			// flag indicates that Gate-Out entry has been completed.
			L_strGINTP = String.valueOf(cmbGINTP1.getSelectedItem()).substring(0,2);
			strLUSBY = cl_dat.M_strUSRCD_pbst.trim();
			strLUPDT = cl_dat.M_strLOGDT_pbst.trim();
			strLUPDT =  strLUPDT.substring(6,10) + "-" + strLUPDT.substring(3,5) + "-" + strLUPDT.substring(0,2);
			for(L_intCNT=0;L_intCNT < intROWCT;L_intCNT++)
			{
				L_flgUPDFL = true;
				if(tblGINLR.getValueAt(L_intCNT,TBL_OUTFL).toString().trim().equals("true"))
				{
					L_strGINNO = tblGINLR.getValueAt(L_intCNT,TBL_strGINNO).toString().trim();		
					L_strCHLQT = tblGINLR.getValueAt(L_intCNT,TBL_CHLQT).toString().trim();		
					L_strLRYNO = tblGINLR.getValueAt(L_intCNT,TBL_LRYNO).toString().trim();		
					if(L_strGINTP.equals(strDESPT_fn) && Double.parseDouble(L_strCHLQT) == 0.000)
					{
						int L_ACTION = JOptionPane.showConfirmDialog(this,"Invoice is not yet prepared for " + L_strGINNO + ". Are you sure to send this vehicle out?","Invoice not prepared",JOptionPane.YES_NO_OPTION); 
						if(L_ACTION != 0){
							cl_dat.M_conSPDBA_pbst.rollback();
							pstmUPDREC.clearParameters();
							return false;
						}
					}
					if(L_strGINTP.equals(strDESPT_fn) && tblGINLR.getValueAt(L_intCNT,TBL_EXAPR).toString().trim().length() == 0)
					{
						int L_ACTION = JOptionPane.showConfirmDialog(this,"Excise Approval is not given for " + L_strGINNO + ". Are you sure to send this vehicle out?","No Excise Approval",JOptionPane.YES_NO_OPTION); 
						if(L_ACTION != 0){
							cl_dat.M_conSPDBA_pbst.rollback();
							pstmUPDREC.clearParameters();
							return false;
						}
					}
					if(L_intDTCNT == 0)
						L_strGINNOS += L_strGINNO;
					else
						L_strGINNOS += "','" + L_strGINNO;
					
					L_strGOTDT = tblGINLR.getValueAt(L_intCNT,TBL_GOTDT).toString().trim();
					L_strGOTDT = L_strGOTDT.substring(6,10) + "-" + L_strGOTDT.substring(3,5) + "-" + L_strGOTDT.substring(0,2) + L_strGOTDT.substring(10,13) + ":" + L_strGOTDT.substring(14) + ":00.000000000";
					L_strGOTBY = tblGINLR.getValueAt(L_intCNT,TBL_GOTBY).toString().trim();
                    L_strREMGT = tblGINLR.getValueAt(L_intCNT,TBL_REMGT).toString().trim();       
				
					pstmUPDREC.setTimestamp(1,Timestamp.valueOf(L_strGOTDT));
					pstmUPDREC.setString(2,L_strGOTBY);
					pstmUPDREC.setString(3,L_strREMGT);
					pstmUPDREC.setString(4,strSTSFL);
					pstmUPDREC.setString(5,strTRNFL_fn);
					pstmUPDREC.setString(6,strLUSBY);
					pstmUPDREC.setDate(7,java.sql.Date.valueOf(strLUPDT));
					pstmUPDREC.setString(8,L_strGINTP);
					pstmUPDREC.setString(9,L_strGINNO);
					pstmUPDREC.executeUpdate();
					if(L_flgUPDFL)
					{
                    	cl_dat.M_conSPDBA_pbst.commit();
                        // e-mail functionality included on 24/05/2006
                        if(L_strGINTP.equals(strDESPT_fn))
					    {
					       // e-mail functionality start
							/*M_strSQLQRY = "SELECT DISTINCT IVT_INVNO,IVT_BYRCD,IVT_CNSCD,IVT_PRDDS,IN_DSTCD,IVT_TRPCD,IVT_LR_NO,PT_PRTNM,PT_EMLPR,cmt_CODDS,SUM(isnull(IVT_INVQT,0)) L_INVQT FROM MR_IVTRN,MR_INMST,co_ptmst,co_cdtrn "
								+ " WHERE IVT_MKTTP = IN_MKTTP AND IVT_INDNO = IN_INDNO AND IVT_BYRCD = PT_PRTCD AND PT_PRTTP ='C' and CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDST' AND CMT_CODCD = IN_DSTCD AND isnull(IVT_INVNO,'') <>'' AND isnull(IVT_STSFL,'') <>'X'"
								+ " AND isnull(IN_STSFL,'') <>'X' AND IVT_GINNO ='"+L_strGINNO +"' group by IVT_INVNO,IVT_BYRCD,IVT_CNSCD,IVT_PRDDS,IN_DSTCD,IVT_TRPCD,IVT_LR_NO,PT_PRTNM,PT_EMLPR,cmt_CODDS order by IVT_INVNO";*/
							pstmINVQR.setString(1,L_strGINNO);	
							M_rstRSSET = pstmINVQR.executeQuery();
							//M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							Hashtable<String,String> hstEMLAD = new Hashtable<String,String>(10);
							if(M_rstRSSET !=null)
								while(M_rstRSSET.next())
								{
									L_strINVNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
									L_strBYRNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
									L_strBYRCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
									L_strTRPCD = nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),"");
									L_strCNSCD = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
									L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"");
									L_strINVQT = nvlSTRVL(M_rstRSSET.getString("L_INVQT"),"0");
									L_strDSTCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS")," ");
									L_strEMLAD = nvlSTRVL(M_rstRSSET.getString("PT_EMLPR")," ");
									hstEMLAD.put(L_strINVNO,L_strEMLAD);
									if(L_strEMLAD.trim().length()>0)
									{   
    									if(!L_strINVNO.equals(L_strPINVNO))
    								    {
    								        // Commented on 30/08/2006 and included hashtable ,as one gate in
    								        // two invoices case, both e-mail were going to same address
    								        ///L_strEMLAD = nvlSTRVL(M_rstRSSET.getString("PT_EMLPR")," ");
    								        L_intSRLNO =1;
    										if(dosREPORT !=null)
    										{
    										    dosREPORT.writeBytes("<hr>");
    										   dosREPORT.writeBytes("For Supreme Petrochem Ltd.\n\n\n\n");
    										    dosREPORT.writeBytes("Central Marketing Services\n");
    										    dosREPORT.writeBytes("cms@spl.co.in \n");
    										    dosREPORT.close();
    										    fosREPORT.close();
    										    dosREPORT = null;
    										    fosREPORT = null;
    										 
                                                 ocl_eml.setFRADR("cms@spl.co.in"); 
                                              	 ocl_eml.sendfile("EXT",L_strEMLID,L_strFILNM,"Delivery Initimation"," ");
                                                 //ocl_eml.sendfile("cms@spl.co.in",L_strFILNM,"Delivery Initimation"," ");
                                                  ocl_eml.setORADR();                              
                                            
                                               // For  Testing of e-mail functionality uncomment this and comment upper block
                    						   /*		  System.out.println("email add from 1 "+L_strEMLID);
                    								  System.out.println("File NAme from 1 "+L_strFILNM);
                                                       ocl_eml.setFRADR("systems@spl.co.in");  
                                                       ocl_eml.sendfile("systems@spl.co.in",L_strFILNM,"Delivery Initimation"," ");
                                                */
                                               
        									}
    										L_strFILNM = cl_dat.M_strREPSTR_pbst +L_strINVNO+".html";
    										L_strEMLID = hstEMLAD.get(L_strINVNO).toString();
    									//	System.out.println(L_strFILNM);
    										L_strPINVNO = L_strINVNO;
    										fosREPORT = new FileOutputStream(L_strFILNM);
    		                            	dosREPORT = new DataOutputStream(fosREPORT);
    										cl_dat.M_PAGENO=0;	
    										    dosREPORT.writeBytes("<HTML><HEAD><Title> Dispatch Intimation </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
    			                            	dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");
    			                            	dosREPORT.writeBytes("<H1><CENTRE>");
    			                            	dosREPORT.writeBytes("Dispatch Intimation \n\n\n");
    			                            	dosREPORT.writeBytes("</H1></CENTRE>");
    			                            	dosREPORT.writeBytes("Vehicle No. " +L_strLRYNO +" of " + getPRTDS("T",L_strTRPCD) +" with following material \n");
    			                            	dosREPORT.writeBytes("has been dispatched from SPL plant on " +cl_dat.M_strLOGDT_pbst +" at "+tblGINLR.getValueAt(L_intCNT,TBL_GOTDT).toString().trim().substring(11)+ " Hrs. \n\n");
    			                            	dosREPORT.writeBytes(padSTRING('R',"Invoice No. : "+ L_strINVNO,55));
    			                            	dosREPORT.writeBytes("Destination : "+ L_strDSTCD+"\n\n");
    			                            	dosREPORT.writeBytes("Buyer       : "+ L_strBYRNM+"\n\n");
    			                            	if(!L_strBYRCD.equals(L_strCNSCD))
    			                            	dosREPORT.writeBytes("Consignee   : "+ getPRTDS("C",L_strCNSCD)+"\n\n");
    			                            	dosREPORT.writeBytes("L.R. No.    : "+ nvlSTRVL(M_rstRSSET.getString("IVT_LR_NO"),"")+"\n\n");
    			                            	dosREPORT.writeBytes("<HR>");
    			                            	dosREPORT.writeBytes(padSTRING('R',"Grade",45)+ "Quantity(MT) \n\n");
    			                            	dosREPORT.writeBytes(padSTRING('R',L_strPRDDS,45)+L_strINVQT+"\n\n");
    								    }
    								    else
    								    {
    								           	dosREPORT.writeBytes(padSTRING('R',L_strPRDDS,45)+L_strINVQT+"\n\n");
    								    }
								   }
								}
								if(dosREPORT !=null)
								{
								    dosREPORT.writeBytes("<hr>");
								    dosREPORT.writeBytes("For Supreme Petrochem Ltd.\n\n\n\n");
								    dosREPORT.writeBytes("Central Marketing Services\n");
								    dosREPORT.writeBytes("cms@spl.co.in \n");
								    dosREPORT.close();
								    fosREPORT.close();
								    dosREPORT = null;
								    fosREPORT = null;
								
								    ocl_eml.setFRADR("cms@spl.co.in");  
							    	ocl_eml.sendfile("EXT",L_strEMLID,L_strFILNM,"Delivery Initimation"," ");
                                  //  ocl_eml.sendfile("cms@spl.co.in",L_strFILNM,"Delivery Initimation"," ");
                                    ocl_eml.setORADR();
                                    
							// For  Testing of e-mail functionality uncomment this
							/*	  System.out.println("email add from 2 "+L_strEMLID);
								  System.out.println("File NAme from 2 "+L_strFILNM);
                                   ocl_eml.setFRADR("systems@spl.co.in");  
                                   ocl_eml.sendfile("systems@spl.co.in",L_strFILNM,"Delivery Initimation"," ");
                              */                
									
								}
								
								}
        
                    }
					else
						cl_dat.M_conSPDBA_pbst.rollback();
					pstmUPDREC.clearParameters();
					L_flgUPDFL = true;
					L_intDTCNT++;
				}
			}
		}catch(Exception e){
			setMSG(e,"Error in exeUPDREC ");
			return false;
		}		
		return L_flgUPDFL;
	}
	// Validation of Raw Material (Tankfarm) 
	private boolean vldMATTF(String P_strMATCD)
	{
		try
		{
			M_strSQLQRY = "select TK_MATCD,TK_MATDS from MM_TKMST";
			M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_MATCD = '" + P_strMATCD + "'";
            	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)		
			if(M_rstRSSET.next())
			{
				txtMATDS.setText(M_rstRSSET.getString("TK_MATDS"));
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			txtMATDS.setText("");
			txtMATCD.requestFocus();
			setMSG("Invalid Material. Press F1 for help",'E');
				
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldMATTF");
			return false;
		}	
		return false;
	}
	
	// Method to display the Chalan Date
	private void setCHLDT()
	{
		try
		{
			
			int L_intCURTM = Integer.parseInt(cl_dat.M_txtCLKTM_pbst.getText().trim().substring(0,2));
			if(L_intCURTM <= 9)
			{
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				M_calLOCAL.add(Calendar.DATE,-1);
				txtORDDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
			}
			else
				txtORDDT.setText(cl_dat.M_strLOGDT_pbst);
			txtMATCD.setText(strSTYRN_fn);
			vldMATTF(strSTYRN_fn);			
			txtPORNO.setText("");
			txtVENCD.setText("");
			txtVENDS.setText("");
		}
		catch(Exception e)
		{
			setMSG(e,"Error in setCHLDT ");
		}
	}
	// Method to create the Prepared Statements
	private void crtPRESTM()
	{
		try
		{
			// For Despatch
			pstmOTLRY1 = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Select WB_DOCTP,WB_DOCNO,WB_LRYNO,WB_MATDS,WB_NETWT,WB_GINDT," +
				"WB_GOTDT,WB_GOTBY,WB_REMGT,WB_CHLQT,WB_EXAPR,sum(IVT_INVQT) L_strINVQT " +
				"from MM_WBTRN LEFT OUTER JOIN MR_IVTRN ON " + 
				"IVT_GINNO = WB_DOCNO AND IVT_CMPCD=WB_CMPCD " +
				" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL = '9' and WB_DOCTP = '" + strDESPT_fn + "'" +
				" and WB_GOTDT between ? and ? " + 
				" group by WB_DOCTP,WB_DOCNO,WB_LRYNO,WB_MATDS,WB_NETWT,WB_GINDT," +
				"WB_GOTDT,WB_GOTBY,WB_REMGT,WB_CHLQT,WB_EXAPR " + 
				"order by WB_DOCTP,WB_DOCNO"
				);
			//	System.out.println("1");
			pstmINLRY1 = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Select WB_DOCTP,WB_DOCNO,WB_LRYNO,WB_MATDS,WB_NETWT,WB_GINDT," +
				"WB_REMGT,WB_CHLQT,WB_EXAPR,sum(IVT_INVQT) L_strINVQT " + 
				"from MM_WBTRN LEFT OUTER JOIN MR_IVTRN ON " + 
				"IVT_GINNO = WB_DOCNO AND IVT_CMPCD=WB_CMPCD" +
				" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strDESPT_fn + "'" + " and WB_STSFL not in ('9','X','N')"+
				" group by WB_DOCTP,WB_DOCNO,WB_LRYNO,WB_MATDS,WB_NETWT," +
				"WB_GINDT,WB_REMGT,WB_CHLQT,WB_EXAPR " + 
				" order by WB_DOCTP,WB_DOCNO"
				);
		//		System.out.println("2");
			// For other Gate-In Types
			pstmOTLRY2 = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Select WB_DOCTP,WB_DOCNO,WB_LRYNO,WB_MATDS,WB_NETWT,WB_GINDT,WB_EXAPR," +
				"WB_GOTDT,WB_GOTBY,WB_REMGT,WB_CHLQT from MM_WBTRN " + 
				" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = ?" + " and WB_STSFL = '9' "+ 
				" and WB_GOTDT between ? and ? " + 
				"order by WB_DOCTP,WB_DOCNO"
				);
			pstmINLRY2 = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Select WB_DOCTP,WB_DOCNO,WB_LRYNO,WB_MATDS,WB_NETWT,WB_GINDT,WB_EXAPR," +
				"WB_REMGT,WB_CHLQT from MM_WBTRN " +
				" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL not in ('9','X','N') and WB_DOCTP = ?" +
				" order by WB_DOCTP,WB_DOCNO"
				);
			// Statement to update Out Entries
			pstmUPDREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"Update MM_WBTRN set WB_GOTDT = ?,WB_GOTBY = ?,WB_REMGT = ?," +
				"WB_STSFL = ?,WB_TRNFL = ?,WB_LUSBY = ?,WB_LUPDT = ?,WB_VSOCT = WB_VSICT " +
				" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = ? and WB_DOCNO = ?" 
				);
			pstmINVQR = cl_dat.M_conSPDBA_pbst.prepareStatement("SELECT DISTINCT IVT_INVNO,IVT_BYRCD,IVT_CNSCD,IVT_PRDDS,IN_DSTCD,IVT_TRPCD,IVT_LR_NO,PT_PRTNM,PT_EMLPR,cmt_CODDS,SUM(isnull(IVT_INVQT,0)) L_INVQT FROM MR_IVTRN,MR_INMST,co_ptmst,co_cdtrn "
								+ " WHERE IVT_MKTTP = IN_MKTTP AND IVT_INDNO = IN_INDNO AND IVT_CMPCD = IN_CMPCD AND IVT_BYRCD = PT_PRTCD AND PT_PRTTP ='C' and CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDST' AND CMT_CODCD = IN_DSTCD AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_INVNO,'') <>'' AND isnull(IVT_STSFL,'') <>'X'"
								+ " AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') <>'X' AND IVT_GINNO = ? group by IVT_INVNO,IVT_BYRCD,IVT_CNSCD,IVT_PRDDS,IN_DSTCD,IVT_TRPCD,IVT_LR_NO,PT_PRTNM,PT_EMLPR,cmt_CODDS order by IVT_INVNO");

		}
		catch(Exception e)
		{
			setMSG(e,"Error in crtPRESTM ");
		}
	}
	private int exeGETLRY(String P_strLRYNO,JTable P_strTBLNM,int P_intROWCNT)
	{
		try
		{
			for(int i=0;i<P_intROWCNT;i++)
			{
				if(P_strTBLNM.getValueAt(i,TBL_LRYNO).toString().trim().equals(P_strLRYNO.trim()))
					return 1;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeGETLRY");		
			return 0;
		}
		return 0;
	}
	public String getTARWT(int P_intTBLROW)
	{
		String L_strSQLQRY;
		ResultSet L_rstRSSET;
		java.util.Date odt = new java.util.Date();
		SimpleDateFormat dtFORM;
		dtFORM = new SimpleDateFormat("dd/MM/yyyy");
		String L_CURDT = "";
		String L_CURTM = "";
		String strCURDT ="";
		String L_strGINNO ="";
		try 
		{
			strTARWT ="0";
			L_strGINNO = tblGINLR.getValueAt(P_intTBLROW,TBL_strGINNO).toString();
			L_strSQLQRY = "select current_date L_CURDT,current_time L_CURTM,WB_NETWT,WB_TARWT from MM_WBTRN where ";
			L_strSQLQRY += "WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP ='"+strRAWMT_fn+"' AND WB_DOCNO ='"+L_strGINNO +"' AND WB_SRLNO ='1'";
			L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET!=null)
			if(L_rstRSSET.next())
			{
				odt =L_rstRSSET.getDate("L_CURDT");
				L_CURTM = L_rstRSSET.getString("L_CURTM").trim().substring(0,5);
				strTARWT = nvlSTRVL(L_rstRSSET.getString("WB_TARWT"),"0");
				//tblGINLR.setValueAt(setFMT(nvlSTRVL(L_rstRSSET.getString("WB_NETWT"),"0"),3),intTBLRW,TBL_NETWT);
				//** Replace By ATC
				tblGINLR.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(L_rstRSSET.getString("WB_NETWT"),"0")),3),intTBLRW,TBL_NETWT);
				if(Double.parseDouble(strTARWT) <= 0)
				{
					int L_SELOPT = JOptionPane.showConfirmDialog(this,"Tare weight has not been taken for " + L_strGINNO +" \n Press Yes to select for save, No to send back to weighbridge.. ?","confirm",JOptionPane.YES_NO_OPTION);
					if(L_SELOPT == 0)
					{
						tblGINLR.setValueAt(new Boolean(true),P_intTBLROW,TBL_OUTFL);						
					}
					else
					{
						tblGINLR.setValueAt(new Boolean(false),P_intTBLROW,TBL_OUTFL);						
						return "";
					}
				}
			}
			if(L_rstRSSET != null)
				L_rstRSSET.close();			
			if(odt != null)
			{
				L_CURDT = dtFORM.format(odt);
			}
			strCURDT = L_CURDT + " " + L_CURTM;
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"Exception ");
		}
        return strCURDT;
	}

	void exeSAVE()
	{
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		cl_dat.M_btnSAVE_pbst.setEnabled(false);
		cl_dat.M_flgLCUPD_pbst = true;				
		if(intSELPG == 0)
		{		
			if(vldDATA())	
			{
				if(exeADDREC())
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						JOptionPane.showMessageDialog(this,"Please note down Gate-In No. " + strGINNO,"Gate-In No.",JOptionPane.INFORMATION_MESSAGE); 
						strGINTP = cmbGINTP.getSelectedItem().toString().substring(0,2);
					    if(strGINTP.equals(strRAWMT_fn))
							flgRMADD = true;
						else if(strGINTP.equals(strDESPT_fn))
							flgPSADD = true;
						else if(strGINTP.equals(strRECPT_fn))
							flgRCADD = true;
						else if(strGINTP.equals(strOTHER_fn))
							flgOTADD = true;
					}
					clrCOMP();
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)&& strGINTP.equals(strRAWMT_fn))
					{
						strPGINTP = strGINTP;
						strPGINNO = strGINNO;
						btnPRINT.setEnabled(true);
					}
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						setENBL(false);
						txtGOTDT.setEnabled(false);
						txtGOTBY.setEnabled(false);
						txtGINNO.requestFocus();
					}
					else
					{
						strGINTP = cmbGINTP.getSelectedItem().toString().substring(0,2);
					    if(strGINTP.equals(strRAWMT_fn))
						{                                           
							setCHLDT();
							btnPRINT.requestFocus();
					    }
					    else if(strGINTP.equals(strDESPT_fn))
						{                                      
							txtMATDS.setText("POLYSTYRENE");
					        txtLRYNO.requestFocus();
					    }
					    else if(strGINTP.equals(strRECPT_fn)){                                      
							txtMATDS.setText("");
					        txtMATCD.setText(strMATCD_fn);
							txtORDRF.requestFocus();
					    }
					    else if(strGINTP.equals(strOTHER_fn)){                                      
							txtMATDS.setText("Scrap");
					        txtLRYNO.requestFocus();
					    }
						cmbGINTP.setEnabled(true);
						txtGINDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						txtGINTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
						txtREPDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
						txtREPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
						txtGINBY.setText(strLUSBY);
					}
				}
			}
		}
		else if(intSELPG == 2)
		{
			if(txtMGPNO.getText().trim().equals(""))
		    {
				setMSG("Gate pass no. can not be blank..",'E');
				return;
			}
			else if(txtCRRNM.getText().trim().equals(""))
		    {
				setMSG("Carrier name can not be blank..",'E');
				return;
			}
			else
			{
				String L_strSTSFL ="";
				if(cmbMGPTP.getSelectedItem().toString().substring(0,2).equals("51"))
					L_strSTSFL ="5";   // For returnable
				else	
					L_strSTSFL ="9";   // For Non returnable	
				M_strSQLQRY = "Update MM_GPTRN SET GP_GOTBY ='"+cl_dat.M_strUSRCD_pbst+"',"
							+" GP_GOTDT = current_timestamp,GP_VEHNO ='"+txtVEHNO.getText().trim()+"'," 
							+" GP_VEHDS ='"+txtCRRNM.getText().trim()+"',"
							+ getUSGDTL("GP",'U',L_strSTSFL)
							+" WHERE GP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GP_STRTP ='"+cmbSTRTP.getSelectedItem().toString().substring(0,2)+"'"
							+" AND GP_MGPTP ='"+cmbMGPTP.getSelectedItem().toString().substring(0,2)+"'"
							+" AND GP_MGPNO ='"+txtMGPNO.getText().trim()+"' and gp_stsfl ='4'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(cl_dat.exeDBCMT("Gate Pass Update"))
				{
					setMSG("Gate Pass updated successfully..",'N');
					clrCOMP();
					tblGPDTL.clrTABLE();
				}
				else
				{
					setMSG("Error in Gate Pass out ..",'E');
				}
			}
		}
		else
		{
		//	if(vldTBLDT())
		//	{
				if(exeUPDREC())
				{
					setMSG("Record saved successfully",'N');
					tblGINLR.clrTABLE();
					getINLRY();
					tblGOTLR.clrTABLE();
					getOTLRY();
					//	tblINLRY.clrJTBL();
				}
		//	}
		}
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		
		//visitors.............
		if(jtpGATEIO.getSelectedIndex() ==3)
		{ 
			//changed 13/01/2006 as per the work request--- modified by dipti
		try
		{
			String L_strPVSPNO="";
			if(!vldVISTOR())	
			return;
			if(jtpMANTAB.getSelectedIndex() == 0)
			{
			    String L_strVSPNO  = "",  L_strCODCD = "",L_strCCSVL="";
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
				{
				    cl_dat.M_flgLCUPD_pbst = true;
				    this.setCursor(cl_dat.M_curWTSTS_pbst);
				    M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
    				M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'HRXXVST' and ";
    				M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3)+"01"+ "'";
    				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    				if(M_rstRSSET != null)
    				{
    					if(M_rstRSSET.next())
    					{
							String strVSPNO_TMP = Integer.toString(Integer.parseInt(M_rstRSSET.getString("CMT_CCSVL"))+1);
							if(strVSPNO_TMP.length()<5)
							{
								for(int i=0;i<=5;i++)
								{
									strVSPNO_TMP = "0"+strVSPNO_TMP;
									if(strVSPNO_TMP.length()==5)
										break;
								}
							}
							L_strVSPNO = M_rstRSSET.getString("CMT_CODCD")+strVSPNO_TMP;
    						M_rstRSSET.close();
    					}
    				}
    				/*L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
    				for(int i=L_strCCSVL.length(); i<5; i++)				
    					L_strVSPNO = "0"+L_strVSPNO;
    			
    				L_strCCSVL = L_strVSPNO + L_strCCSVL;
    				L_strVSPNO = L_strCODCD + L_strCCSVL;//code generations
					*/
					
                    strVSPNO = L_strVSPNO;
                    M_strSQLQRY ="INSERT INTO HR_VSTRN(VS_CMPCD,VS_VSCAT,VS_VSTDT,VS_VSPNO,VS_VSTTP,VS_VSTCT,VS_VSICT,VS_VSTNM,VS_VSORG,VS_PURPS,VS_BDGNO,VS_PERVS,VS_EMPNO,VS_CLRBY,VS_ESCBY,";
					M_strSQLQRY +="VS_VSITM,VS_VSOTM,VS_GINBY,VS_LUSBY,VS_LUPDT,VS_VSARA,VS_SHFCD,VS_VEHNO,VS_STSFL,VS_DPTNM,VS_VSOCT,VS_SBSCD,VS_MOBFL,VS_CMRFL)Values(";
					M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'" +cmbVSCAT.getSelectedItem().toString().substring(0,2)+"',";
					M_strSQLQRY += "'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVSTDT.getText().toString().trim()))+"',";//text date
					M_strSQLQRY += "'"+L_strVSPNO+"',";
					M_strSQLQRY += "'01',";
					if(txtVSICT.getText().trim().length()>0)
						M_strSQLQRY += txtVSICT.getText().trim()+",";
					else
						M_strSQLQRY += 1+",";
					if(txtVSICT.getText().trim().length()>0)
						M_strSQLQRY += txtVSICT.getText().trim();
					else
						M_strSQLQRY += 1;
					M_strSQLQRY += ",'" + txtVSTNM.getText().trim()+"',";
					M_strSQLQRY += "'" + txtVSORG.getText().trim()+"',";	
					M_strSQLQRY += "'" + txtPURPS.getText().trim()+"',";	
					M_strSQLQRY += "'" + txtBDGNO.getText().trim()+"',";
					M_strSQLQRY += "'" + txtPERVS.getText().trim() +"',";
					M_strSQLQRY += "'" + lblEMPNO.getText().trim() +"',";
					M_strSQLQRY += "'" + txtCLRBY.getText().trim() +"',";
					M_strSQLQRY += "'" + txtESCBY.getText().trim()+"',";
					M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst +" "+ cl_dat.M_txtCLKTM_pbst.getText().trim()))+ "',";
					M_strSQLQRY += null;
					M_strSQLQRY +=",'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
					M_strSQLQRY += "'" + txtVSARA.getText().trim().toUpperCase()+"',";
					M_strSQLQRY += "'" + txtSHFCD.getText().trim().toUpperCase()+"',";
					M_strSQLQRY += "'" + txtVSVHN.getText().trim()+"',";
				// Direct Gate In done temporarily on 25/09/07 API KVM/HBP. 
				/*	if(cmbVSCAT.getSelectedItem().toString().substring(0,2).equals("03")) // External Visitors
						M_strSQLQRY += "'1',"; // Status flag 1 for external visitors
					else */
						M_strSQLQRY += "'6',"; // Status flag 6 for external visitors, direct gate in
					M_strSQLQRY += "'" +txtDPTNM.getText() .trim()+"'," ;
					if(txtVSOCT.getText().trim().length()>0)
						M_strSQLQRY += txtVSOCT.getText().trim();
					else
						M_strSQLQRY += 0;
					
					M_strSQLQRY += ",'"+M_strSBSCD+"',";
					M_strSQLQRY += "'"+(rdbMOBFLY.isSelected() ? "Y" : "N")+"',";
					M_strSQLQRY += "'"+(rdbCMRFLY.isSelected() ? "Y" : "N")+"')";
					strVSPNO = L_strVSPNO;
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
                   if((txtVSREM.getText().trim().length() > 0) && cl_dat.M_flgLCUPD_pbst)
                   {
                        M_strSQLQRY="INSERT INTO HR_RMMST(RM_CMPCD,RM_LINNO,RM_SBSCD,RM_REMDS,RM_TRNTP,RM_TRNCD,RM_DOCNO)Values(";
    					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY +="'1',";
    					M_strSQLQRY += "'"+M_strSBSCD+"',";
    					M_strSQLQRY += "'" + txtVSREM.getText().trim() +"',";
    					M_strSQLQRY += "'HC',";
    					M_strSQLQRY += "'01',";
    					M_strSQLQRY += "'"+L_strVSPNO+"')";
    					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
                   }
                   if(cl_dat.M_flgLCUPD_pbst)
                   {
                        M_strSQLQRY ="UPDATE CO_CDTRN SET ";
        				M_strSQLQRY +="CMT_CCSVL ='"+L_strVSPNO.substring(3,8)+"'";
        				M_strSQLQRY +=" where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'HRXXVST' and CMT_CODCD= '" + cl_dat.M_strFNNYR_pbst.substring(3)+"01"+ "'";
        				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
            			tblVSTDTL1.clrTABLE();
            			tblVSTDTL2.clrTABLE();
                   }
                   this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{
				    M_strSQLQRY ="UPDATE HR_VSTRN SET ";
					//M_strSQLQRY += "VS_VSCAT = " + "'" + cmbVSCAT.getSelectedItem().toString().substring(0,2)+"',";
    				M_strSQLQRY += "VS_VSTNM = " + "'" + txtVSTNM.getText().trim() +"',";
					M_strSQLQRY += "VS_BDGNO = " + "'" + txtBDGNO.getText().trim() +"',";
					if(txtVSICT.getText().trim().length()>0)
						M_strSQLQRY += "VS_VSICT = " + txtVSICT.getText().trim()+ ",";	
					else
						M_strSQLQRY += "VS_VSICT = 1,";
					M_strSQLQRY += "VS_VSORG = " + "'" + txtVSORG.getText().trim() +"',";
					M_strSQLQRY += "VS_PERVS = " + "'" + txtPERVS.getText().trim() +"',";
					M_strSQLQRY += "VS_EMPNO = " + "'" + lblEMPNO.getText().trim() +"',";
					M_strSQLQRY += "VS_PURPS = " + "'" + txtPURPS.getText().trim() +"',";
					M_strSQLQRY += "VS_GINBY = " + "'" + txtGINBY.getText().trim() +"',";
					M_strSQLQRY += "VS_ESCBY = " + "'" + txtESCBY.getText().trim() +"',";
					M_strSQLQRY += "VS_CLRBY = " + "'" + txtCLRBY.getText().trim() +"',";
					M_strSQLQRY += "VS_VSARA = " + "'" + txtVSARA.getText().trim() +"',";
					M_strSQLQRY += "VS_SHFCD = " + "'" + txtSHFCD.getText().trim() +"',";
					M_strSQLQRY += "VS_VEHNO = " + "'" + txtVSVHN.getText().trim() +"',";
					if(txtVSOCT.getText().trim().length()>0)
						M_strSQLQRY += "VS_VSOCT = " + txtVSOCT.getText().trim()+",";	
					else
						M_strSQLQRY += "VS_VSOCT = 0,";
					M_strSQLQRY += "VS_DPTNM =  " + "'" +txtDPTNM.getText() .trim() +"',";
					M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "VS_LUPDT= '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";

					M_strSQLQRY += "VS_MOBFL = " + "'"+(rdbMOBFLY.isSelected() ? "Y" : "N")+"',";
					M_strSQLQRY += "VS_CMRFL = " + "'"+(rdbCMRFLY.isSelected() ? "Y" : "N")+"',";
					M_strSQLQRY += "VS_REQBY = " + "'"+cl_dat.M_strUSRCD_pbst+"'";

					//M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '01' and VS_SBSCD = '"+M_strSBSCD+"'";
					M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '01'";
					M_strSQLQRY += " and VS_VSPNO = " + "'" + txtVSPNO.getText().trim()+"'";
					M_strSQLQRY += " and VS_VSCAT = " + "'" + cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	                if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) && txtVSREM.getText().trim().length() >0)
                    {
						M_strSQLQRY ="UPDATE HR_RMMST SET ";
						M_strSQLQRY += "RM_REMDS = " + "'" + txtVSREM.getText().trim() +"'";
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='01' and RM_DOCNO = '" + txtVSPNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			        }
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
				{
				    M_strSQLQRY ="UPDATE HR_VSTRN SET ";
				    M_strSQLQRY += getUSGDTL("VS",'U',"X");
				    //M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '01' and VS_SBSCD = '"+M_strSBSCD+"'";
					M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '01'";
    				M_strSQLQRY += " and VS_VSPNO = " + "'" + txtVSPNO.getText().trim()+"'";
					M_strSQLQRY += " and VS_VSCAT = " + "'" + cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
    				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    			}
			}
			if(jtpMANTAB.getSelectedIndex() == 1)
			{
				inlTBLEDIT(tblVSTDTL1);
				inlTBLEDIT(tblVSTDTL2);
				String M_strSQLQRY1="",M_strSQLQRY2="";
				int a=0,b=0,c=0;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgLCUPD_pbst = true;
    				for(int i=0;i<tblVSTDTL1.getRowCount();i++)
    				{
    					if(tblVSTDTL1.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
    					{
							a=Integer.parseInt(tblVSTDTL1.getValueAt(i,TB1_VSICT).toString().trim());
							b=Integer.parseInt(tblVSTDTL1.getValueAt(i,TB1_VSOCT).toString().trim());
							//System.out.println("M_strSBSCD  : "+M_strSBSCD);
															
								M_strSQLQRY ="UPDATE HR_VSTRN SET ";
    							/*M_strSQLQRY += "VS_VSTNM = " + "'" + tblVSTDTL1.getValueAt(i,TB1_VSTNM).toString().trim() +"',";
    							if(tblVSTDTL1.getValueAt(i,TB1_VSICT).toString().trim().length()>0)
    								M_strSQLQRY += "VS_VSICT = " + tblVSTDTL1.getValueAt(i,TB1_VSICT).toString().trim()+ ",";	
    							else
    								M_strSQLQRY += "VS_VSICT = 1";*/
								if(tblVSTDTL1.getValueAt(i,TB1_VSOCT).toString().trim().length()>0)
									M_strSQLQRY += "VS_VSOCT = " + tblVSTDTL1.getValueAt(i,TB1_VSOCT).toString().trim()+ ",";	
								else
									M_strSQLQRY += "VS_VSOCT = 0";
							//	M_strSQLQRY += "VS_VSORG = " + "'" + tblVSTDTL1.getValueAt(i,TB1_VSORG).toString().trim() +"',";
								// checking the value of in count and OutCount.... 
								if(a==b)
									M_strSQLQRY += "VS_STSFL ='7',VS_VSOTM = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
    							M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
    							M_strSQLQRY += "VS_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
    							//M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '01' and VS_SBSCD = '"+M_strSBSCD+"'";
								M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '01'";
    							M_strSQLQRY += " and VS_VSPNO = " + "'" + tblVSTDTL1.getValueAt(i,TB1_VSPNO).toString().trim()+"'";
								//System.out.println(M_strSQLQRY);
    							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
    				}
    				this.setCursor(cl_dat.M_curDFSTS_pbst);
    				tblVSTDTL1.clrTABLE();
    				tblVSTDTL2.clrTABLE();
    				inlTBLEDIT(tblVSTDTL1);
    				inlTBLEDIT(tblVSTDTL2);
    				getINVST();
					getOTVST();

    		    }
		}
		if(jtpMANTAB.getSelectedIndex() == 2)
		{
			inlTBLEDIT(tblCNTDTL);
			String L_strVSPNO  = "",  L_strCODCD = "", L_strCCSVL = "";
			int a=0,b=0,c=0;
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'HRXXVST' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3)+"02"+ "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    		if(M_rstRSSET != null)
    		{
    			if(M_rstRSSET.next())
    			{
					String strVSPNO_TMP = Integer.toString(Integer.parseInt(M_rstRSSET.getString("CMT_CCSVL"))+1);
					if(strVSPNO_TMP.length()<5)
					{
						for(int i=0;i<=5;i++)
						{
							strVSPNO_TMP = "0"+strVSPNO_TMP;
							if(strVSPNO_TMP.length()==5)
								break;
						}
					}
					L_strVSPNO = M_rstRSSET.getString("CMT_CODCD")+strVSPNO_TMP;
    				M_rstRSSET.close();
				}
    		}
			// for Generating Visitor Pass Number
			//L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
			//for(int i=L_strCCSVL.length(); i<5; i++)				
			//	L_strVSPNO += "0";
		
			//L_strCCSVL = L_strVSPNO + L_strCCSVL;
			//L_strVSPNO = L_strCODCD + L_strCCSVL;//code generations 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgLCUPD_pbst = true;	
					for(int i=0;i<tblCNTDTL.getRowCount();i++)
					{
					if(tblCNTDTL.getValueAt(i,TB3_CHKFL).toString().trim().equals("true"))
					{
						a=Integer.parseInt(tblCNTDTL.getValueAt(i,TB3_VSICT).toString().trim());
						b=Integer.parseInt(tblCNTDTL.getValueAt(i,TB3_VSOCT).toString().trim());
								
						M_strSQLQRY ="UPDATE HR_VSTRN SET ";
						M_strSQLQRY += "VS_VSTNM = " + "'" + tblCNTDTL.getValueAt(i,TB3_VSTNM).toString().trim() +"',";
						//if(tblCNTDTL.getValueAt(i,TB3_VSICT).toString().trim().length()>0)
						//	M_strSQLQRY += "VS_VSICT ="+ tblCNTDTL.getValueAt(i,TB3_VSICT).toString().trim()+",";	
						//else
						//	M_strSQLQRY += 1;
						if(tblCNTDTL.getValueAt(i,TB3_VSOCT).toString().trim().length()>0)
							M_strSQLQRY += "VS_VSOCT ="+ tblCNTDTL.getValueAt(i,TB3_VSOCT).toString().trim()+",";	
						else
							M_strSQLQRY += 0;
						if(a==b)
						{
					    	M_strSQLQRY += "VS_VSOTM = '" + M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
						}
						M_strSQLQRY += "VS_GINBY = " + "'" + tblCNTDTL.getValueAt(i,TB3_GINBY).toString().trim() +"',";
						M_strSQLQRY += "VS_CRDNM = " + "'" + tblCNTDTL.getValueAt(i,TB3_CRDNM).toString().trim() +"',";
						M_strSQLQRY += "VS_SHFCD = " + "'" + tblCNTDTL.getValueAt(i,TB3_SHFCD).toString().trim() +"',";
						M_strSQLQRY += "VS_PRTCD = " + "'" + tblCNTDTL.getValueAt(i,TB3_PRTCD).toString().trim() +"',";
						M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "VS_LUPDT= '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						//M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '02' and VS_SBSCD = '"+M_strSBSCD+"'";
						M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '02'";
						M_strSQLQRY += " and VS_VSPNO = " + "'" + tblCNTDTL.getValueAt(i,TB3_VSPNO).toString().trim()+"'";
						//System.out.println("MODIFICATION :  "+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) && tblCNTDTL.getValueAt(i,TB3_REMDS).toString().trim().length() >0)
		                {
							M_strSQLQRY ="UPDATE HR_RMMST SET ";
							M_strSQLQRY += "RM_REMDS = " + "'" + tblCNTDTL.getValueAt(i,TB3_REMDS).toString().trim() +"'";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='02' and RM_DOCNO = '" + tblCNTDTL.getValueAt(i,TB3_VSPNO).toString().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		                }
			 }
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			tblCNTDTL.clrTABLE();
			inlTBLEDIT(tblCNTDTL);
			getINCNT();
			}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			for(int i=0;i<tblCNTDTL.getRowCount();i++)
			{
				if(tblCNTDTL.getValueAt(i,TB3_CHKFL).toString().trim().equals("true"))
				{
					M_strSQLQRY ="INSERT INTO HR_VSTRN(VS_CMPCD,VS_VSPNO,VS_VSTDT,VS_VSTTP,VS_SBSCD,VS_VSICT,VS_VSTNM,VS_VSITM,VS_VSOTM,";
					M_strSQLQRY +="VS_GINBY,VS_CRDNM,VS_SHFCD,VS_PRTCD,VS_LUSBY,VS_LUPDT)Values(";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+L_strVSPNO+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVSTDT1.getText().toString().trim()))+"',";//text date
					M_strSQLQRY += "'02',";
					M_strSQLQRY += "'"+M_strSBSCD+"',";
					if(tblCNTDTL.getValueAt(i,TB3_VSICT).toString().trim().length()>0)
						M_strSQLQRY += tblCNTDTL.getValueAt(i,TB3_VSICT).toString().trim();
					else
						M_strSQLQRY += 1;
					M_strSQLQRY += ",'" + tblCNTDTL.getValueAt(i,TB3_VSTNM).toString().trim()+"',";
					M_strSQLQRY += "'" +M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
					M_strSQLQRY += null;
					M_strSQLQRY +=",'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +="'"+tblCNTDTL.getValueAt(i,TB3_CRDNM).toString().trim()+"',";
					M_strSQLQRY +="'"+tblCNTDTL.getValueAt(i,TB3_SHFCD).toString().trim().toUpperCase()+"',";
					M_strSQLQRY +="'"+tblCNTDTL.getValueAt(i,TB3_PRTCD).toString().trim()+"',";
					M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
					L_strPVSPNO = L_strVSPNO;
					String strVSPNO_TMP = Integer.toString(Integer.parseInt(L_strVSPNO.substring(3,8))+1);
					if(strVSPNO_TMP.length()<5)
					{
						for(int j=0;j<=5;j++)
						{
							strVSPNO_TMP = "0"+strVSPNO_TMP;
							if(strVSPNO_TMP.length()==5)
								break;
						}
					}
					L_strVSPNO = L_strVSPNO.substring(0,3)+strVSPNO_TMP;
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    				if(tblCNTDTL.getValueAt(i,TB3_REMDS).toString().trim().length() >0)
				    {
						M_strSQLQRY="INSERT INTO HR_RMMST(RM_CMPCD,RM_LINNO,RM_SBSCD,RM_REMDS,RM_TRNTP,RM_TRNCD,RM_DOCNO,RM_LUSBY,RM_LUPDT)Values(";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY +="'1',";
						M_strSQLQRY += "'"+M_strSBSCD+"',";
						M_strSQLQRY += "'" + tblCNTDTL.getValueAt(i,TB3_REMDS).toString().trim() +"',";
						M_strSQLQRY += "'HC',";
						M_strSQLQRY += "'02',";
						M_strSQLQRY += "'"+L_strPVSPNO+"',";
						M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						//System.out.println(" ADD "+M_strSQLQRY);
				    }
				}
			}
			M_strSQLQRY ="UPDATE CO_CDTRN SET ";
			M_strSQLQRY +="CMT_CCSVL ='"+L_strPVSPNO.substring(3,8)+"'";
			M_strSQLQRY +=" where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'HRXXVST' and CMT_CODCD= '" + cl_dat.M_strFNNYR_pbst.substring(3)+"02"+ "'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			tblCNTDTL.clrTABLE();
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
					
			for(int i=0;i<tblCNTDTL.getRowCount();i++)
			{
			if(tblCNTDTL.getValueAt(i,TB3_CHKFL).toString().trim().equals("true"))
			{
				M_strSQLQRY =" UPDATE HR_VSTRN SET ";
				M_strSQLQRY +="VS_LUSBY = "+"'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY +="VS_LUPDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
				M_strSQLQRY += "VS_STSFL = 'X'";
				//M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_SBSCD = '"+M_strSBSCD+"' and VS_VSTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVSTDT.getText().toString().trim()))+"' and VS_VSTTP = '02' and VS_VSTNM = '" + tblCNTDTL.getValueAt(i,TB3_VSTNM).toString().trim()+"'";
				M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and VS_VSTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVSTDT.getText().toString().trim()))+"' and VS_VSTTP = '02' and VS_VSTNM = '" + tblCNTDTL.getValueAt(i,TB3_VSTNM).toString().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				//System.out.println("DELETION :  "+M_strSQLQRY);		
			 }
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
		tblCNTDTL.clrTABLE();
		inlTBLEDIT(tblCNTDTL);
		getINCNT();
	 }
	}	
		
		if(jtpMANTAB.getSelectedIndex() == 3)
		{
		    ResultSet L_rstRSSET;
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
			{
		        for(int i=0;i<tblVSTDTL3.getRowCount();i++)
				{
		            if(tblVSTDTL3.getValueAt(i,TB4_CHKFL).toString().trim().equals("true"))
		            {
						
		                M_strSQLQRY ="UPDATE HR_VSTRN SET ";
					//	M_strSQLQRY += "VS_VSTNM = " + "'" + tblVSTDTL3.getValueAt(i,TB4_VSTNM).toString().trim() +"',";
					//	M_strSQLQRY += "VS_VSORG = " + "'" + tblVSTDTL3.getValueAt(i,TB4_VSORG).toString().trim() +"',";
						
						
					//	M_strSQLQRY += "VS_PERVS = "+ "'"+tblVSTDTL3.getValueAt(i,TB4_PERVS).toString().trim() +"',";
					//    M_strSQLQRY += "VS_PURPS ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_PURPS).toString().trim() +"',";
					    M_strSQLQRY += "VS_CLRBY ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_CLRBY).toString().trim() +"',";
					    if(tblVSTDTL3.getValueAt(i,TB4_VSITM).toString() .trim().length() >0)
						    M_strSQLQRY += "VS_VSITM ="+ "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
							//M_strSQLQRY += "VS_VSITM ="+ "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblVSTDTL3.getValueAt(i,TB4_VSITM).toString().trim() ))+"',";
					    else
					    M_strSQLQRY += "VS_VSITM =" + null +",";    
					    M_strSQLQRY += "VS_VSARA ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_VSARA).toString().trim() +"',";
					    M_strSQLQRY += "VS_BDGNO ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_BADGE).toString().trim() +"',";
					    M_strSQLQRY += "VS_VEHNO ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_VEHNO).toString().trim() +"',";
					    M_strSQLQRY += "VS_VSICT ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_VSTCT).toString().trim() +"',";
						M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "VS_STSFL = '6',";
						
						M_strSQLQRY += "VS_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY += "VS_VSTTP  = '01' ";
						M_strSQLQRY += " WHERE  ";
						M_strSQLQRY += " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSPNO = " + "'" +tblVSTDTL3 .getValueAt(i,TB4_VSPNO).toString().trim()+"'";
						strVSPNO = tblVSTDTL3 .getValueAt(i,TB4_VSPNO).toString().trim();
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				  if(tblVSTDTL3.getValueAt(i,TB4_REMDS).toString().trim().length() >0)
		              { 
					M_strSQLQRY = " select * from hr_rmmst where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='01' and RM_DOCNO = '" + tblVSTDTL3.getValueAt(i,TB4_VSPNO).toString().trim()+"'";
					//System.out.println("001 : "+M_strSQLQRY);
					L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET !=null)
					{
						if(L_rstRSSET.next())
						{
  							M_strSQLQRY ="UPDATE HR_RMMST SET ";
							M_strSQLQRY += "RM_REMDS = " + "'" + tblVSTDTL3.getValueAt(i,TB4_REMDS).toString().trim() +"'";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='01' and RM_DOCNO = '" + tblVSTDTL3.getValueAt(i,TB4_VSPNO).toString().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		                		}
						else
						{	
							M_strSQLQRY="INSERT INTO HR_RMMST(RM_CMPCD,RM_LINNO,RM_SBSCD,RM_REMDS,RM_TRNTP,RM_TRNCD,RM_DOCNO)Values(";
    							M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
								M_strSQLQRY +="'1',";
    							M_strSQLQRY += "'"+M_strSBSCD+"',";
		    					M_strSQLQRY += "'" + tblVSTDTL3.getValueAt(i,TB4_REMDS).toString().trim() +"',";
    							M_strSQLQRY += "'HC',";
    							M_strSQLQRY += "'01',";
		    					M_strSQLQRY += "'"+tblVSTDTL3.getValueAt(i,TB4_VSPNO).toString().trim()+"')";
    							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
				  	}
			        }
				} // check flag true
				} // end for
		        tblVSTDTL3.clrTABLE() ;
///////////// Added on 25/09/07 for gate in from unauthorised requests.

			for(int i=0;i<tblVSTDTL4.getRowCount();i++)
			{
		            if(tblVSTDTL4.getValueAt(i,TB4_CHKFL).toString().trim().equals("true"))
		            {
						
		                M_strSQLQRY ="UPDATE HR_VSTRN SET ";
				  if(tblVSTDTL4.getValueAt(i,TB5_CLRBY).toString() .trim().length() >0)
					  M_strSQLQRY +=	"  VS_CLRBY = " + "'" +tblVSTDTL4 .getValueAt(i,TB5_CLRBY).toString().trim()+"',";
				  else
					    M_strSQLQRY += "VS_CLRBY ="+ "'"+ cl_dat.M_strUSRCD_pbst +"',";
					    M_strSQLQRY += "VS_VSITM ="+ "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
					 
						M_strSQLQRY += "VS_VSARA ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_VSARA).toString().trim() +"',";
					    M_strSQLQRY += "VS_BDGNO ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_BADGE).toString().trim() +"',";
					    //M_strSQLQRY += "VS_VEHNO ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_VEHNO).toString().trim() +"',";
					    M_strSQLQRY += "VS_VSICT ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_VSTCT).toString().trim() +"',";
						
						M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "VS_STSFL = '6',";
						
						M_strSQLQRY += "VS_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY += "VS_VSTTP  = '01' ";
						M_strSQLQRY += " WHERE  ";
						M_strSQLQRY += " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSPNO = " + "'" +tblVSTDTL4 .getValueAt(i,TB5_VSPNO).toString().trim()+"'";
						strVSPNO = tblVSTDTL4 .getValueAt(i,TB5_VSPNO).toString().trim();
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(tblVSTDTL4.getValueAt(i,TB5_REMDS).toString().trim().length() >0)
		              { 
					M_strSQLQRY = " select * from hr_rmmst where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='01' and RM_DOCNO = '" + tblVSTDTL4.getValueAt(i,TB5_VSPNO).toString().trim()+"'";
					//System.out.println("002 : "+M_strSQLQRY);
					L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET !=null)
					{
						if(L_rstRSSET.next())
						{
  							M_strSQLQRY ="UPDATE HR_RMMST SET ";
							M_strSQLQRY += "RM_REMDS = " + "'" + tblVSTDTL4.getValueAt(i,TB5_REMDS).toString().trim() +"'";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='01' and RM_DOCNO = '" + tblVSTDTL4.getValueAt(i,TB5_VSPNO).toString().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		                		}
						else
						{	
							M_strSQLQRY="INSERT INTO HR_RMMST(RM_CMPCD,RM_LINNO,RM_SBSCD,RM_REMDS,RM_TRNTP,RM_TRNCD,RM_DOCNO)Values(";
    							M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
								M_strSQLQRY +="'1',";
    							M_strSQLQRY += "'"+M_strSBSCD+"',";
		    					M_strSQLQRY += "'" + tblVSTDTL4.getValueAt(i,TB5_REMDS).toString().trim() +"',";
    							M_strSQLQRY += "'HC',";
    							M_strSQLQRY += "'01',";
		    					M_strSQLQRY += "'"+tblVSTDTL4.getValueAt(i,TB5_VSPNO).toString().trim()+"')";
    							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
				  	}
			        }
				}
			}
///////////////
			tblVSTDTL4.clrTABLE() ;

		        getAUTHVST();
			  getWTGVST();
		        
			}
		    
		    
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
			{
		        
		        for(int i=0;i<tblVSTDTL3.getRowCount();i++)
				{
		            if(tblVSTDTL3.getValueAt(i,TB4_CHKFL).toString().trim().equals("true"))
		            {
						
		                M_strSQLQRY ="UPDATE HR_VSTRN SET ";
						/*M_strSQLQRY += "VS_VSTNM = " + "'" + tblVSTDTL3.getValueAt(i,TB4_VSTNM).toString().trim() +"',";
						M_strSQLQRY += "VS_VSORG = " + "'" + tblVSTDTL3.getValueAt(i,TB4_VSORG).toString().trim() +"',";
						
						
						M_strSQLQRY += "VS_PERVS = "+ "'"+tblVSTDTL3.getValueAt(i,TB4_PERVS).toString().trim() +"',";
					    M_strSQLQRY += "VS_PURPS ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_PURPS).toString().trim() +"',";
					    M_strSQLQRY += "VS_CLRBY ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_CLRBY).toString().trim() +"',";
					    if(tblVSTDTL3.getValueAt(i,TB4_VSITM).toString() .trim().length() >0)
					    M_strSQLQRY += "VS_VSITM ="+ "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblVSTDTL3.getValueAt(i,TB4_VSITM).toString().trim() ))+"',";
					    else
					    M_strSQLQRY += "VS_VSITM =" + null +",";    
					    M_strSQLQRY += "VS_VSARA ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_VSARA).toString().trim() +"',";
					    M_strSQLQRY += "VS_BDGNO ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_BADGE).toString().trim() +"',";
					    M_strSQLQRY += "VS_VSTCT ="+ "'"+tblVSTDTL3.getValueAt(i,TB4_VSTCT).toString().trim() +"',";*/

						M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "VS_STSFL = 'X',";
						M_strSQLQRY += "VS_VSTTP  = '01' ,";
						M_strSQLQRY += "VS_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						
						M_strSQLQRY += " WHERE  ";
						M_strSQLQRY += " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSPNO = " + "'" +tblVSTDTL3 .getValueAt(i,TB4_VSPNO).toString().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(tblVSTDTL3.getValueAt(i,TB4_REMDS).toString().trim().length() >0)
		                {
							M_strSQLQRY ="UPDATE HR_RMMST SET RM_STSFL ='X' ";
				//			M_strSQLQRY += "RM_REMDS = " + "'" + tblVSTDTL3.getValueAt(i,TB4_REMDS).toString().trim() +"'";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='02' and RM_DOCNO = '" + tblVSTDTL4.getValueAt(i,TB4_VSPNO).toString().trim()+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		                }
					
		            }
		           
				}
		        tblVSTDTL3.clrTABLE() ;
	              getAUTHVST();
		        for(int i=0;i<tblVSTDTL4.getRowCount();i++)
				{
		            if(tblVSTDTL4.getValueAt(i,TB5_CHKFL).toString().trim().equals("true"))
		            {
						
		                M_strSQLQRY ="UPDATE HR_VSTRN SET ";
						M_strSQLQRY += "VS_VSTNM = " + "'" + tblVSTDTL4.getValueAt(i,TB5_VSTNM).toString().trim() +"',";
						M_strSQLQRY += "VS_VSORG = " + "'" + tblVSTDTL4.getValueAt(i,TB5_VSORG).toString().trim() +"',";
						
						
						M_strSQLQRY += "VS_PERVS = "+ "'"+tblVSTDTL4.getValueAt(i,TB5_PERVS).toString().trim() +"',";
						M_strSQLQRY += "VS_EMPNO = "+ "'"+tblVSTDTL4.getValueAt(i,TB5_EMPNO).toString().trim() +"',";
					    M_strSQLQRY += "VS_PURPS ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_PURPS).toString().trim() +"',";
					    M_strSQLQRY += "VS_CLRBY ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_CLRBY).toString().trim() +"',";
					    if(tblVSTDTL4.getValueAt(i,TB5_VSITM).toString() .trim().length() >0)
					    M_strSQLQRY += "VS_VSITM ="+ "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblVSTDTL4.getValueAt(i,TB5_VSITM).toString().trim() ))+"',";
					    else
					    M_strSQLQRY += "VS_VSITM= " +null +",";    
					    M_strSQLQRY += "VS_VSARA ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_VSARA).toString().trim() +"',";
					    M_strSQLQRY += "VS_BDGNO ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_BADGE).toString().trim() +"',";
					    M_strSQLQRY += "VS_VSTCT ="+ "'"+tblVSTDTL4.getValueAt(i,TB5_VSTCT).toString().trim() +"',";
						M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "VS_STSFL = 'X',";
						M_strSQLQRY += "VS_VSTTP  = '01', ";
						M_strSQLQRY += "VS_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						
						M_strSQLQRY += " WHERE  ";
						M_strSQLQRY += " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  VS_VSPNO = " + "'" +tblVSTDTL4 .getValueAt(i,TB5_VSPNO).toString().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					//	if(tblVSTDTL4.getValueAt(i,TB5_REMDS).toString().trim().length() >0)
		                //{
					//		M_strSQLQRY ="UPDATE HR_RMMST SET ";
					//		M_strSQLQRY += "RM_REMDS = " + "'" + tblVSTDTL4.getValueAt(i,TB5_REMDS).toString().trim() +"'";
					//		M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='02' and RM_DOCNO = '" + tblVSTDTL4.getValueAt(i,TB4_VSPNO).toString().trim()+"'";
					//		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		                //}
						
		            }
		             
		           
				}
		        tblVSTDTL4.clrTABLE() ;
	            getWTGVST();  
			}
		    
		}
		
		if(cl_dat.exeDBCMT("exeSAVE"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG(" Data Saved Successfully..",'N'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				setMSG(" Data Modified Successfully..",'N'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Data Deleted Successsfully ..",'N');
			if(jtpMANTAB.getSelectedIndex() == 0)
			    clrCOMP();	
		}
		else
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG("Error in saving details..",'E'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			   setMSG("Error in Modified Data details..",'E'); 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				setMSG("Error in Deleting data..",'E');
		}
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"exeSAVE");
	}	
}//visitors..........
		
		/**update & Delete Exit pass record**/
		
		if(jtpGATEIO.getSelectedIndex() == 4)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) 
			{
		    	try
		    	{
					boolean flgSELRW=false;////flag to check whether atleast 1 row from tblEXPDTL is selected
					for(int P_intROWNO=0;P_intROWNO<tblEXPDTL.getRowCount();P_intROWNO++)
					{
						if(tblEXPDTL.getValueAt(P_intROWNO,TB6_CHKFL).toString().equals("true"))
						{
							flgSELRW=true;
						}
					}
					if(flgSELRW==false)
						setMSG("Please Select atleast 1 row from the Table",'E');
					else
					{	
						/**Delete Previous Exit Pass record***/
			    		this.setCursor(cl_dat.M_curWTSTS_pbst);
						cl_dat.M_flgLCUPD_pbst = true;	
						for(int i=0;i<tblEXPDTL.getRowCount();i++)
						{
						    if(tblEXPDTL.getValueAt(i,TB6_CHKFL).toString().trim().equals("true"))
						    {
								M_strSQLQRY ="UPDATE HR_EXTRN SET ";
								M_strSQLQRY += " EX_STSFL = 'X' ";
								M_strSQLQRY += " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  EX_DOCNO = '" + tblEXPDTL.getValueAt(i,TB6_DOCNO).toString().trim()+"'";
								//System.out.println(">>>UPDATE>>"+M_strSQLQRY);
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
								
						    }
						}
						this.setCursor(cl_dat.M_curDFSTS_pbst);
						if(cl_dat.exeDBCMT("exeSAVE"))
						{
							setMSG("Record Updated Successfully...",'N');
							tblEXPDTL.clrTABLE();
							inlTBLEDIT(tblEXPDTL);
							getEXPDL();	
						}
					}
				}
				catch(Exception E)
				{
					setMSG(E," Error In Deleting Exit Pass....");
				}
			}
		}
	}					
		
	
	/**While Actual Out time will be more than Expected out time (half an hour) Display Message to send mail **/
	/*private boolean vldEXTPS()
	{
		int L_intOPTN;
		for(int i=0;i<tblEXPDTL.getRowCount();i++)
		{
			if(tblEXPDTL.getValueAt(i,TB6_CHKFL).toString().equals("true"))
			{
				L_intOPTN=JOptionPane.showConfirmDialog( this,"Exit Pass delayed gate Out Intimation  \n"+L_strLINE+" \nEmp Name : "+tblEXPDTL.getValueAt(i,TB6_EMPNM).toString().trim()+ "       Dept : "+tblEXPDTL.getValueAt(i,TB6_DPTNM).toString().trim()+"\nOut Time       Expected : "+tblEXPDTL.getValueAt(i,TB6_OUTTM).toString().trim()+"\n                        Actual :      "+cl_dat.M_txtCLKTM_pbst.getText().substring(1,6)+"\n                        Delay :        "+L_strTIME+ " \n Purpose : "+tblEXPDTL.getValueAt(i,TB6_REMDS).toString().trim()+"\n"+L_strLINE+"\nDo You Want To Authorize?","Expected Out Time Validation",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN==2 || L_intOPTN==-1)
				{
					return false;
				}
				tblEXPDTL.clrTABLE();
				inlTBLEDIT(tblEXPDTL);
				getEXPDL();
					return true;
				
			}
		}	
		return true;
	}*/
	
	/**Method to validate Authorize Exit Pass **/
	private boolean vldEXPAU()
	{
		try
		{
			for(int P_intROWNO=0;P_intROWNO<tblEXPDTL.getRowCount();P_intROWNO++)
			{
				if(tblEXPDTL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					if(tblEXPDTL.getValueAt(P_intROWNO,TB6_DOCDT).toString().length()>0)
					if(M_fmtLCDAT.parse(tblEXPDTL.getValueAt(P_intROWNO,TB6_DOCDT).toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))<0)
					{
						setMSG("You Can not Authorize Previous day Exit Pass ",'E');
						return false;
					}
					if(tblEXPDTL.getValueAt(P_intROWNO,TB6_AUTBY).toString().length()==0)
					{
						setMSG("Please Enter Authorized By",'E');
						return false;
					}
					
				}
			}
			
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldEXPAU()");		
		}
		return true;
	}
	
	// Method to check whether all the necessary data has been entered
		
	boolean vldDATA()
	{
		try
		{
			String L_strGINDT,L_strGINBY,L_strDOCDT,L_strDOCQT,L_strLRYNO,L_strMATCD,L_strMATDS,L_strTPRCD,L_strPRTCD;
			String L_strGOTDT,L_strGOTBY,L_strREPTM;
			
			strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			L_strGINDT = txtGINDT.getText().trim()+" "+txtGINTM.getText().trim();
			L_strGINBY = txtGINBY.getText().trim();
			L_strGOTDT = txtGOTDT.getText().trim()+" "+txtGOTTM.getText().trim();
			L_strGOTBY = txtGOTBY.getText().trim();
			L_strDOCDT = txtORDDT.getText().trim();
			L_strDOCQT = txtORDQT.getText().trim();
			L_strLRYNO = txtLRYNO.getText().trim();
			L_strTPRCD = txtTPRCD.getText().trim().toUpperCase();
			L_strMATCD = txtMATCD.getText().trim();
			L_strREPTM = txtREPDT.getText().trim()+" "+txtREPTM.getText().trim();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
				return true;
			if(txtGINTM.isEnabled())
			{
				if(L_strGINBY.length() == 0)
				{
					txtGINBY.requestFocus();
					setMSG("Please enter Gate-In Officer",'E');
					return false;
				}
				if(L_strGINDT.length() == 0)
				{
					txtGINTM.requestFocus();
					setMSG("Please enter Gate-In date/Time",'E');
					return false;
				}
				else
				{
					if(M_fmtLCDTM.parse(L_strGINDT).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
					{
							txtGINTM.requestFocus();
							setMSG("Gate-In Date/Time can not be greater than current Date/Time ",'E');
							return false;
					}
				}
			}
			else
			{
				if(L_strGOTDT.length() == 0)
				{
					txtGOTDT.requestFocus();
					setMSG("Please enter Gate-Out date/Time",'E');
					return false;
				}
				else
				{
					strCURDT = cl_dat.getCURDTM();
				if(M_fmtLCDTM.parse(L_strGOTDT).compareTo(M_fmtLCDTM.parse(L_strGINDT))<0)
				{				
					JOptionPane.showMessageDialog(this,"Gate-Out time can not be less than Gate-In time ","Login",JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
				if(M_fmtLCDTM.parse(L_strGOTDT).compareTo(M_fmtLCDTM.parse(strCURDT))>0)
				{				
					txtGOTTM.requestFocus();
					setMSG("Gate-Out Date/Time can not be greater than current Date/Time ",'E');
					return false;
				}
				}
				if(L_strGOTBY.length() == 0)
				{
					txtGOTBY.requestFocus();
					setMSG("Please enter Gate-Out Officer",'E');
					return false;
				}
			}
			if((txtMATDS.getText().indexOf("'")>=0)||(txtMATDS.getText().indexOf("\"")>=0)||(txtMATDS.getText().indexOf("\\")>=0))
			{
				setMSG("Special characters LIKE ' are not allowed in material desc ",'E');
				return false;
			}
	    	if(L_strREPTM.length() == 0)
			{						
				txtREPDT.requestFocus();
				setMSG("Please enter Reporting Time",'E');
				return false;
			}
			else
			{
				strCURDT = cl_dat.getCURDTM();
				if(M_fmtLCDTM.parse(L_strREPTM).compareTo(M_fmtLCDTM.parse(strCURDT))> 0)
				{									
						txtREPDT.requestFocus();
						setMSG("Reporting Time can not be greater than current Date/Time ",'E');
						return false;
				}
			}
			if(txtGINCT.getText().length() == 0)
			{
				txtGINCT.requestFocus();
				setMSG("Please enter the No. of persons with vehicle",'E');
				return false;
			}
			if(!strGINTP.equals(strRECPT_fn))
			if(L_strLRYNO.length() == 0)
			{
				txtLRYNO.requestFocus();
				setMSG("Please enter Lorry No.",'E');
				return false;
			}
			if(L_strMATCD.length() == 0)
			{
				L_strMATDS = txtMATDS.getText().trim();
				if(L_strMATDS.length() == 0)
				{
					txtMATCD.requestFocus();
					setMSG("Material can not be empty. Press F1 for help.",'E');
					return false;
				}
			}
			else
			{
				if(strGINTP.equals(strRAWMT_fn))
				{
					if(!vldMATTF(L_strMATCD))
					{	
						txtMATDS.setText("");
						txtMATCD.requestFocus();
						setMSG("Invalid Material. Press F1 for help.",'E');
						return false;
					}
				}
				else if(!strGINTP.equals(strRECPT_fn))
				{
					if(!vldMATCD(L_strMATCD))
					{	
						txtMATDS.setText("");
						txtMATCD.requestFocus();
						setMSG("Invalid Material. Press F1 for help.",'E');
						return false;
					}
				}
			}
			if(L_strTPRCD.length() == 0)
			{
				if(!strGINTP.equals(strRECPT_fn))
				{
					txtTPRCD.requestFocus();
					setMSG("Please enter Transporter",'E');
					return false;
				}
			}
			else
			{
				if(!strGINTP.equals(strRECPT_fn))
				if(!vldPRTCD("T",L_strTPRCD))
				{
					txtTPRCD.requestFocus();
					setMSG("Invalid Transporter Code. Press F1 for help.",'E');
					return false;
				}
				else if(txtTPRDS.getText().trim().length() == 0){
					txtTPRDS.requestFocus();
					setMSG("Please Enter Transporter Description.",'E');
					return false;
				}
			}
			if(strGINTP.equals(strRECPT_fn))
			{
				if(!vldPRTCD("S",txtVENCD.getText().trim()))
				{
					txtVENCD.requestFocus();
					setMSG("Invalid Party Code. Press F1 for help.",'E');
					return false;
				}
				if(txtORDQT.getText().trim().length() ==0)
				{
					setMSG("Please enter some qty. in challan qty",'E');
					return false;
				}
			}
			if(txtORDDT.getText().length() ==10)
			{
				if(M_fmtLCDAT.parse(txtORDDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))> 0)
				{
					setMSG("Challan Date can not be greater than todays date..",'E');
					return false;
				}
			}
			if(strGINTP.equals(strDESPT_fn))
			    if(txtLCCQT.getText().length() == 0)
			    {
			        setMSG("Enter Load Carrying Capacity in MT..",'E');
			        return false;
			    }
			if(txtLICNO.getText().length()==0)
			{
				setMSG("Please Enter Licence No..",'E');
				txtLICNO.requestFocus();
			    return false;
			}	
			if(txtLICBY.getText().length()==0)
			{
				setMSG("Please Enter Issuing Authority..",'E');
				txtLICBY.requestFocus();
			    return false;
			}	
			if(txtLVLDT.getText().length()==0)
			{
				setMSG("Please Enter Licence No..",'E');
				txtLVLDT.requestFocus();
			    return false;
			}	
			if(txtDRVNM.getText().length()==0)
			{
				setMSG("Please Enter Driver Name..",'E');
				txtDRVNM.requestFocus();
			    return false;
			}	
			if(txtDRVCD.getText().length()==0)
			{
				setMSG("Please Enter Driver Code..",'E');
				txtDRVCD.requestFocus();
			    return false;
			}	
			if(txtREMDS.getText().length()>0)
			{
				txtREMDS.setText(txtREMDS.getText().replace("'","`"));
			}	

			setMSG("",'N');
			return true;				
		}
		catch(Exception e)
		{
			setMSG(e,"vldDATA");
			return false;
		}
	}
	boolean vldVISTOR()
	{
		try
		{
			if(jtpGATEIO.getSelectedIndex() ==3)
			{
				if(jtpMANTAB.getSelectedIndex() == 1)
				{
				 if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
				{
				
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				    {
				        setMSG("Select modification for Visitor gate out entry",'E');
				        return false; 
				    }
				  	boolean L_flgCHKFL= false;
			         if(txtFMDAT.getText().length() ==0)
			            txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);   
			        if(txtTODAT.getText().length() ==0)
			            txtTODAT.setText(cl_dat.M_strLOGDT_pbst);       
					if(txtFMDAT.getText().trim().length() == 0)
					{
						setMSG("Enter From Date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("From Date should not be greater than current Date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
					if(txtTODAT.getText().trim().length() == 0)
					{
						setMSG("Enter To Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("To Date should not be grater than current Date Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
					{
						setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
				}
				if(jtpMANTAB.getSelectedIndex() == 0)
				{
				    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
					{
					    if(txtVSTNM.getText().length() ==0)
					    {
					        setMSG("Visitor Name can not be blank..",'E');
					        return false;         
					    }
					    if(txtVSTDT.getText().length() ==0)
					    {
					        setMSG("Visit Date can not be blank..",'E');
					        return false;         
					    }
					}
				}
				if(jtpMANTAB.getSelectedIndex() == 1)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						boolean L_flgCHKFL= false;
						for(int i=0; i<tblVSTDTL1.getRowCount(); i++)
						{				
							if(tblVSTDTL1.getValueAt(i,TB1_CHKFL).toString().equals("true"))
							{				
								L_flgCHKFL= true;
								break;
							}	
						}	
						if(L_flgCHKFL== false)
						{
							setMSG("No row Selected..",'E');				
							return false;
						}	
						for(int i=0; i<tblVSTDTL1.getRowCount(); i++)
						{				
							if(tblVSTDTL1.getValueAt(i,TB1_CHKFL).toString().equals("true"))
							{				
								if(tblVSTDTL1.getValueAt(i,TB1_VSOCT).toString().equals(""))
								{	
									setMSG("Select the out count for visitor..",'E');		
									return false;
								}
							      else
									setMSG("",'N');		

							}	
						}	
		
					}
					else
					{
					      setMSG("Select The Option as Modification for Visitor-Out entry",'E'); 
					      return false;
					}
				}	
				if(jtpMANTAB.getSelectedIndex() == 2)
				{
				    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
					{
						boolean L_flgCHKFL= false;
						if(txtVSTDT1.getText().length() ==0)
					    {
					        setMSG("Visit Date can not be blank..",'E');
					        txtVSTDT1.requestFocus();
					        return false;         
					    }
						for(int i=0; i<tblCNTDTL.getRowCount(); i++)
						{				
							if(tblCNTDTL.getValueAt(i,TB3_CHKFL).toString().equals("true"))
							{	
								L_flgCHKFL= true;
								break;
							}		
						}	
						if(L_flgCHKFL== false)
						{
							setMSG("No row Selected..",'E');				
							return false;
						}	
						for(int i=0; i<tblCNTDTL.getRowCount(); i++)
    					{
    						if(tblCNTDTL.getValueAt(i,TB3_CHKFL).toString().equals("true"))
    						{
    							if(tblCNTDTL.getValueAt(i,TB3_VSICT).toString().length()==0)
    							{setMSG("Enter the No. of persons coming inside..",'E');	return false;}
    						}
    					}

					}
				}
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"vldDATA");
			return false;
		}
		return true;
	}
	// Method to check whether JTable data is complete or not
	private boolean vldTBLDT()
	{
		try
		{
			int L_intCNT;
			boolean L_flgSELREC = false;
			intTBLRW = tblGINLR.getSelectedRow();
			intTBLCL = tblGINLR.getSelectedColumn();
			if(intTBLRW < 0)
				intTBLRW = 0;
			if(intTBLCL < 0)
				intTBLCL = 0;
			tblGINLR.editCellAt(intTBLRW,intTBLCL);
			for(L_intCNT=0;L_intCNT < intROWCT;L_intCNT++)
			{
				if(tblGINLR.getValueAt(L_intCNT,TBL_OUTFL).toString().trim().equals("true"))
				{
					if(!L_flgSELREC)	L_flgSELREC = true;
					strGINDT = tblGINLR.getValueAt(L_intCNT,TBL_GINDT).toString().trim();
					strGOTDT = tblGINLR.getValueAt(L_intCNT,TBL_GOTDT).toString().trim();
					strGOTBY = tblGINLR.getValueAt(L_intCNT,TBL_GOTBY).toString().trim();		
					if(strGOTDT.length() == 0)
					{
						tblGINLR.requestFocus();
						tblGINLR.editCellAt(L_intCNT,TBL_GOTDT);
						tblGINLR.setColumnSelectionInterval(L_intCNT,L_intCNT);
						tblGINLR.setRowSelectionInterval(TBL_GOTDT,TBL_GOTDT);
						setMSG("Gate-Out Date can not be null",'E');
						return false;
					}
					else if(strGOTBY.length() == 0)
					{
						tblGINLR.requestFocus();
						tblGINLR.editCellAt(L_intCNT,TBL_GOTBY);
						tblGINLR.setColumnSelectionInterval(L_intCNT,L_intCNT);
						tblGINLR.setRowSelectionInterval(TBL_GOTBY,TBL_GOTBY);
						setMSG("Gate-Out By can not be null",'E');
						return false;
					}
					strCURDT = cl_dat.getCURDTM();
					if(M_fmtLCDTM.parse(strGOTDT).compareTo(M_fmtLCDTM.parse(strGINDT))<0)
					{		
							JOptionPane.showMessageDialog(this,"Gate Out Date-Time can not be less than Gate-In time","Login",JOptionPane.INFORMATION_MESSAGE);
							return false;
					}
					else if(M_fmtLCDTM.parse(strGOTDT).compareTo(M_fmtLCDTM.parse(strCURDT))>0)
					{		
							tblGINLR.requestFocus();
							tblGINLR.editCellAt(L_intCNT,TBL_GOTDT);
							tblGINLR.setColumnSelectionInterval(L_intCNT,L_intCNT);
							tblGINLR.setRowSelectionInterval(TBL_GOTDT,TBL_GOTDT);
							setMSG("Gate-Out Date/Time can not be greater than current Date/Time ",'E');
							return false;
					}
				}
			}
			if(!L_flgSELREC)
			{
				setMSG("Select atleast one entry",'E');
				return false;
			}
			return L_flgSELREC;
		}
		catch(Exception e)
		{
			setMSG(e,"vldTBLDT");
			return false;
		}
	}
	 /**
    Method to substract time given in second parameter to the time in first parameter 
    and returns the result in HH:MM format
	@param P_strSTRTM String argument to pass Starting Time.
	@param P_strNEWTM String argument to pass New Time.
    */
    private String subTIME(String P_strSTRTM,String P_strNEWTM)
    {
	    String L_strRETSTR = "";
		try
        {
            if (P_strSTRTM.equals(""))  P_strSTRTM = "00:00";
            if (P_strNEWTM.equals(""))  P_strNEWTM = "00:00";            
            int  L_intSTRLN = P_strSTRTM.trim().length();
            int  L_intNEWLN = P_strNEWTM.trim().length();
            int  L_intSTRCL = P_strSTRTM.indexOf(":");
            int  L_intNEWCL = P_strNEWTM.indexOf(":");                
            int  L_intSTRMN = Integer.parseInt(P_strSTRTM.substring(0,L_intSTRCL))*60+Integer.parseInt(P_strSTRTM.substring(L_intSTRCL+1,L_intSTRLN));
            int  L_intNEWMN = Integer.parseInt(P_strNEWTM.substring(0,L_intNEWCL))*60+Integer.parseInt(P_strNEWTM.substring(L_intNEWCL+1,L_intNEWLN));
            int  L_intTOTHR = (L_intSTRMN-L_intNEWMN) / 60;
            int  L_intTOTMN = (L_intSTRMN-L_intNEWMN)- ((L_intSTRMN-L_intNEWMN)/60)*60;
            String L_strTOTHR1 = "0000"+String.valueOf(L_intTOTHR).trim();
            String L_strTOTMN1 = "0000"+String.valueOf(L_intTOTMN).trim();                
            int L_intLENHR = L_strTOTHR1.length();
            int L_intLENMN = L_strTOTMN1.length();
            int L_intTOTCL;
            if (L_intTOTHR < 100)  
                L_intTOTCL = 2;
             else if (L_intTOTHR < 1000)
                    L_intTOTCL = 3;
                else 
                    L_intTOTCL = 4;
           L_strRETSTR = L_strTOTHR1.substring(L_intLENHR-L_intTOTCL,L_intLENHR)+":"+L_strTOTMN1.substring(L_intLENMN-2,L_intLENMN);                
           return  L_strRETSTR;
		}
        catch(Exception L_EX)
        {
		    setMSG(L_EX, "subTIME");
    		return "";
		}            
    }
   
    /**Method to verify Authorized by in Exit Pass Entry.***/
	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{	
			try
			{
				if(getSource()==tblEXPDTL)
			    {
					if(P_intCOLID == TB6_AUTBY)
    			    {
						if(jtpGATEIO.getSelectedIndex()==4)
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						if(tblEXPDTL.getValueAt(P_intROWID,TB6_AUTBY).toString().length()>0)
						{
							M_strSQLQRY = " select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN','HR"+cl_dat.M_strCMPCD_pbst+"EXA') and SUBSTRING(cmt_codcd,1,4)='"+ tblEXPDTL.getValueAt(P_intROWID,TB6_EMPNO).toString()+"'";
							M_strSQLQRY += " union select distinct cmt_shrds cmt_ccsvl from co_cdtrn where cmt_cgmtp ='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXSIC' and cmt_codcd in (select ep_empno from hr_epmst where ep_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_inost,'') = 'I')";
							M_strSQLQRY += " and cmt_ccsvl='"+tblEXPDTL.getValueAt(P_intROWID,TB6_AUTBY).toString().trim().toUpperCase()+"'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								tblEXPDTL.setValueAt(M_rstRSSET.getString("cmt_ccsvl"),P_intROWID,TB6_AUTBY);
								setMSG("",'N');
						
							}
							else
							{
								setMSG("Enter valid Authorized",'E');
								return false;
							}
							
							M_rstRSSET.close();
	    			    }
					}
			    }
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"class TBLINPVF");
			}
			return true;
		}
    }
}