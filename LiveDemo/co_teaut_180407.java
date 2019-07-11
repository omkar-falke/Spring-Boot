import java.sql.*;import java.util.Hashtable;
import javax.swing.JButton;import javax.swing.JTabbedPane;import javax.swing.JPanel;import javax.swing.JComponent;import javax.swing.JLabel;import javax.swing.JTextArea;
import javax.swing.JTextField;import javax.swing.JOptionPane;import javax.swing.JTable;import javax.swing.JComboBox;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;import javax.swing.JLabel;import javax.swing.JScrollPane;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.InputVerifier;
import java.io.FileOutputStream;import java.io.DataOutputStream;import java.io.File;
import java.util.Vector;
class co_teaut extends cl_pbase implements ChangeListener
{
	private JTextField txtTEMP;
	private JButton btnRPDC1,btnRPDC2,btnRPDC3;
	private JTabbedPane jtpTRNDT;
	private JPanel pnlGPAPR,pnlGPAUT,pnlINAUT,pnlCLSDT,pnlRCLDT,pnlPTFDT;
	private cl_JTable tblGPAPR,tblGPAUT,tblINAUT,tblITDT1,tblITDT2,tblITDT3;
    private final int TBL_CHKFL =0;
	private final int TBL_DSPFL =1;
	private final int TBL_STRTP =2;
	private final int TBL_DOCTP =3;
	private final int TBL_DOCNO =4;
	private final int TBL_DPTCD =5;
	private final int TBL_APRVL =6;
	private final int TBL_VENCD =8;
	private final int TBL_APRFL =7;
	private final int TBL_REMDS =9;
	private final int TBL_COMDS =10;
	private final int TB1_REMDS =8;
	private final int TB1_COMDS =9;
	private final int TB1_PREBY =10;
	private final int TBL_PREBY =11;
	private final int TBL_VEHNO =12;
	private final int TBL_VEHDS =13;
	
	private final String strGPAPR_fn = "GP Approval";
	private final String strFNLCL_fn ="Final Classification";
	private final String strPRVCL_fn ="Prov. Classification";
	private final String strLTRCL_fn ="ReClassification";
	private final String strLTPTF_fn ="Product Transfer Form";
	private final String strDFLSR_fn ="00000";
	private final String strINTRCL_fn ="00";
	/*private int intGPAPR =0;
	private int intGPAUT =1;
	private int intINAPR =2;
	private int intPRVCL =3;
	private int intFNLCL =4;*/
     
    // these are set to 99 , as if access to some tab is not given then these integers were 
    // set and were giving wrong results 
    private int intGPAPR = 99;
	private int intGPAUT = 99;
	private int intINAPR = 99;
	private int intPRVCL = 99;
	private int intFNLCL = 99;
    private int intLTRCL = 99;
    private int intLTPTF = 99;
	
	private Hashtable hstCODDS,hstCODCD,hstCMPVL,hstBAGVL,hstHSTST;
	private boolean flgDATA = false;
	private java.sql.Date datTEMP;
		
	// Used for classifcation screen
	private JComboBox cmbPRDTP;
	private JButton btnRLS;
	private ButtonGroup bgrOPT;
	private JTextField txtLOTNO,txtRCLNO,txtBAGQT,txtREMDS,txtTSTBY,txtTSTDT,txtPCLBY,txtCLSBY;
	private JTextField txtPCLTM,txtCLSTM,txtTPRCD,txtPPRCD,txtCPRCD,txtTPRDS,txtPPRDS,txtCPRDS,txtIPRDS;
	private JTextArea txtGRBTS,txtADDTS;
	private JLabel lblTSTBY,lblCLSBY,lblPCLBY,lblGRB,lblADDPAR,lblREFPAR,lblAUTCLS;
	private cl_JTable tblTSTDT;
	private char chrCLSFL;
	private boolean flgFNLOVR = false;
	private boolean flgOFFGRD= false;
	private boolean flgOUTRG= false;
	private boolean flgACLFL= false;
	private float fltNPFVL,fltNPTVL,fltQPRVL;
	private int intQPRCT =0,intGRBCT=0;
	private String strNPFDS,strNPTDS, strQPRDS,strMSG;
	
    private Vector vtrQPRCD = new Vector();
    private String strCLSFL ="",strCHKCD="",strPRSTS ="",strTSTNO;
    private boolean flgLOTFL = false;
    private boolean flgTSTFL = false;
    private String strRLSFL ="";
	private String strRLSPRD ="";
	private String strCMPTP_fn ="0103";
	private String strCLSTP_fn ="0199"; 
	private String strGRBTP_fn = "0101";
	private int intROWCT = 20;
	private final String strPRVST_fn = "4";
	private final String strFNLST_fn = "9";
	
	boolean  flgTSTOK = true;
	private String staCMPFL[];
	private String staGRBDT[][];
	private String staQPRDT[][];
	
	private final int TB_CHKFL =0;
	private final int TB_QPRCD =1;
	private final int TB_UOMDS =2;
	private final int TB_SPEC  =3;
	private final int TB_QPRDS =4;
	private final int TB_CMPVL =5;
	private final int TB_BAGVL =6;
	private final int TB_FLAG = 7;
   
    private inpVRFY objINPVR = new inpVRFY();	
	///
	//	private int L_ROWNUM =0,L_SELROW=0;
	int L_RECCNT =0;
    private String L_CMPVL,L_BAGVL,L_QPRCD,L_RNGVL,L_CHKCD,strPTAFL,strPTARM;
    private float L_QPRVL;
    // Used for classifcation screen

    // For Reclassification Screen
    private JComboBox cmbRCPRD;
    private JTextField txtNWPRD,txtNWGRD,txtNWDAT,txtRCLOT,txtRBGQT;
    private cl_JTable tblLOTRC,tblTSTRC,tblSTKDT;	
    private final String strSCPLT_fn = "('999','900','910')"; // Lot series of ps scrap, sps scrap and processed material
    private String strLSTRCL,strLSTPRD,strCURRC,LM_REPRCL;
    private double strCURSTK;
    private Vector LM_CMPFL = new Vector();
    boolean  flgTSROK = true;
    boolean  flgTSTCP = true;
    boolean  LM_BLKVL = true;
 	private final int TB1_CHKFL =0;
	private final int TB1_RCLNO =1;
	private final int TB1_PRDCD =2;
	private final int TB1_PRDDS =3;
	private final int TB1_CLSTM =4;
	private final int TB1_DSPQT =5;
	private final int TB1_STKQT =6;

    private final int TB2_LOCCD = 1;
    private final int TB2_STKQT = 2;

	private final int TB3_CHKFL =0;
	private final int TB3_QPRCD =1;
	private final int TB3_QPRDS =2;
	private final int TB3_UOMDS =3;
	private final int TB3_SPEC  =4;
	private final int TB3_CMPVL =5;
	private final int TB3_FLAG = 6;
	
	final String strRCRCT_fn ="51"; // Reclassification reciept.
	private String strRCLDOC;
	int strROWNO =0 ;
	int intRCQPCT =0;
	private boolean flgRESFL = false,LM_REPCLS=false,flgLTVLD = false,flgPRVLD = false;
	private Timestamp tmsTEMP;

    // used for PTF NETRY
    private JTextField txtPTFNO,txtPTFDT,txtPTFCT,txtBALQT,txtPTFQT; 
    private JButton btnPRINT;
    private cl_JTable tblPTFDT;
    private String strREFDT,strREFFL,strREFTM;
    private String strPTFLOT,strPTFRCL,strUCLQT,strTPRDS,strPTFCT,strPTFNO;
	private String strPKGTP,strTOTQT,strTBLQT,strPTFQT,strOPRCD,strPTFRF,strPTFCHP,strRCTQT;
	private String LM_CLSFL,strPRPTF,strPRVDT;
    private int LM_CNT = 200;
  	private String[] AR_PKGTP = new String[LM_CNT];
	private String[] AR_PRDCD = new String[LM_CNT];
	private String[] AR_CLSFL = new String[LM_CNT];
	
	int TB4_STATS = 0;
	int TB4_LOTNO = 1;
	int TB4_RCLNO = 2;
	int TB4_PKGTP = 3;
    int TB4_TPRCD = 4;
    int TB4_CPRCD = 5;
	int TB4_UCLQT = 6;
	int TB4_REMDS = 7;
	int TB4_WRHTP = 8;
	int TB4_PRDTP = 9;
	int TB4_PKGCD = 10;
	int TB4_PRDCD = 11;
	int TB4_OPRCD = 12;
	
	int LM_DSPLOT = 17;  // Lot Selec
	String strDGRTP ="61";
	String strPTFUSR,strCURTM;
	double dblDRTQT = 0,dblDUMQT = 0,dblTOUQT = 0;
	int LM_COUNT =0;
	co_teaut()
	{
	   super(1);	
	   try
	   {
		hstCODDS = new Hashtable();   
		hstCODCD = new Hashtable();   
		setMatrix(20,8);
		jtpTRNDT=new JTabbedPane();
		pnlGPAPR = new JPanel(null);
		pnlGPAUT = new JPanel(null);
		pnlINAUT = new JPanel(null);
		pnlCLSDT = new JPanel(null);
		pnlRCLDT = new JPanel(null);
	    pnlPTFDT = new JPanel(null);
		String[] L_strTB1HD = {"SEL","DSP","Str.Type ","GP Type","GP NO.","Dept.","Approx value","<HTML> <P COLOUR = blue> Approval Flag </P></HTML>","Vendor","Remark","Comment","Prep by","Veh No.","Veh Desc"};
		int[] L_intCOLSZ = {25,30,55,50,65,55,72,60,50,200,200,5,5,5};
		
		String[] L_strTB2HD = {" ","Item Code ","UOM","Description","GP Qty.","Due Date"};
		int[] L_intCL2SZ = {20,90,60,400,90,90};
		
		String[] L_strTB3HD = {"SEL","DSP","Str.Type ","Type","Indent NO.","Dept.","Approx value","<HTML> <P COLOUR = blue> Approval Flag </P></HTML>","Remark","Comment","Prep by"};
		int[] L_intCO3SZ = {25,30,55,40,60,40,72,90,200,200,10};
		
		String[] L_strTB4HD = {" ","Item Code ","UOM","Description","Ind Qty.","Req Date","Exp Date","T.C","Ins"};
		int[] L_intCL4SZ = {20,80,40,300,80,80,80,25,25};
		
		tblGPAPR = crtTBLPNL1(pnlGPAPR,L_strTB1HD,100,1,1,5.2,7.9,L_intCOLSZ,new int[]{0,1});
		add(btnRPDC1=new JButton("Show Document"),7,7,1,1.5,pnlGPAPR,'L');
		tblITDT1 = crtTBLPNL1(pnlGPAPR,L_strTB2HD,100,8,1,8.3,7.9,L_intCL2SZ,new int[]{0});
		
		tblGPAUT = crtTBLPNL1(pnlGPAUT,L_strTB1HD,100,1,1,5.2,7.9,L_intCOLSZ,new int[]{0,1});
		add(btnRPDC2=new JButton("Show Document"),7,7,1,1.5,pnlGPAUT,'L');
		tblITDT2 = crtTBLPNL1(pnlGPAUT,L_strTB2HD,100,8,1,8.3,7.9,L_intCL2SZ,new int[]{0});
		
		tblINAUT = crtTBLPNL1(pnlINAUT,L_strTB3HD,100,1,1,5.2,7.9,L_intCO3SZ,new int[]{0,1});
		add(btnRPDC3=new JButton("Show Document"),7,7,1,1.5,pnlINAUT,'L');
		tblITDT3 = crtTBLPNL1(pnlINAUT,L_strTB4HD,100,8,1,8.3,7.9,L_intCL4SZ,new int[]{0,7,8});
		tblINAUT.setColumnColor(TBL_APRFL,java.awt.Color.blue);
		tblGPAPR.setColumnColor(TBL_APRFL,java.awt.Color.blue);
		pnlCLSDT.setLayout(null);
		setMatrix(19,6);
		// Classification Screen 
		add(new JLabel("Product Type"),1,1,1,1,pnlCLSDT,'L');
		add(new JLabel("LOT No."),1,2,1,0.5,pnlCLSDT,'L');
		add(new JLabel("RCL No."),1,2,1,0.5,pnlCLSDT,'R');
		add(new JLabel("Bag.Qty / Grade"),1,3,1,1,pnlCLSDT,'L');
		//add(new JLabel("Bagging Grade"),1,4,1,1,pnlCLSDT,'L');
		add(new JLabel("Remarks"),1,4,1,3,pnlCLSDT,'L');
	    add(cmbPRDTP = new JComboBox(),2,1,1,1,pnlCLSDT,'L');
	    add(txtLOTNO = new TxtLimit(8),2,2,1,0.75,pnlCLSDT,'L');
	    add(txtRCLNO = new TxtLimit(2),2,2,1,0.25,pnlCLSDT,'R');
	    add(txtBAGQT = new JTextField(),2,3,1,0.4,pnlCLSDT,'L');
	    add(txtIPRDS = new JTextField(),2,3,1,0.6,pnlCLSDT,'R');
	    add(txtREMDS = new TxtLimit(200),2,4,1,3,pnlCLSDT,'L');
		
		add(new JLabel("Target Grade"),3,1,1,1,pnlCLSDT,'L');
		add(new JLabel("Date"),3,2,1,1,pnlCLSDT,'L');
		
		add(new JLabel("Prov.Grade "),3,3,1,1,pnlCLSDT,'L');
		add(new JLabel("Date"),3,4,1,1,pnlCLSDT,'L');
		
		add(new JLabel("Final Grade"),3,5,1,1,pnlCLSDT,'L');
		add(new JLabel("Date"),3,6,1,1,pnlCLSDT,'L');
	    
	    add(txtTPRCD = new JTextField(),4,1,1,1,pnlCLSDT,'L');
		add(txtTSTDT = new JTextField(),4,2,1,1,pnlCLSDT,'L');
		add(txtTPRDS = new JTextField(),5,1,1,1.5,pnlCLSDT,'L');
		add(txtTSTBY = new JTextField(),5,2,1,0.5,pnlCLSDT,'R');
		
		add(txtPPRCD = new TxtLimit(10),4,3,1,1,pnlCLSDT,'L');
		add(txtPCLTM = new JTextField(),4,4,1,1,pnlCLSDT,'L');
		add(txtPPRDS = new JTextField(),5,3,1,1.5,pnlCLSDT,'L');
		add(txtPCLBY = new JTextField(),5,4,1,0.5,pnlCLSDT,'R');
        
        add(txtCPRCD = new TxtLimit(10),4,5,1,1,pnlCLSDT,'L');
		add(txtCLSTM = new JTextField(),4,6,1,1,pnlCLSDT,'L');
		add(txtCPRDS = new JTextField(),5,5,1,1.5,pnlCLSDT,'L');
		add(txtCLSBY = new JTextField(),5,6,1,0.5,pnlCLSDT,'R');
        add(lblAUTCLS = new JLabel(""),6,1,1,6,pnlCLSDT,'L');
        lblAUTCLS.setForeground(java.awt.Color.blue);
        String[] LM_ARRGHD = {"","Para Code","UOM","Specification ", "Description","Comp Test","Bag Test","Flag"};
	    int[] LM_ARRGSZ = {5,40,70,100,170,70,65,30};
        tblTSTDT = crtTBLPNL1(pnlCLSDT,LM_ARRGHD,100,7,1,7,4.5,LM_ARRGSZ,new int[]{0});
        tblTSTDT.setColumnColor(TB_FLAG,java.awt.Color.red);
	    txtADDTS = new JTextArea();
		JScrollPane jspADD = new JScrollPane(txtADDTS,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//add(txtADDTS,7,6,3,1.5,pnlCLSDT,'R');
		add(jspADD,7,6,3,1.5,pnlCLSDT,'R');
        txtGRBTS = new JTextArea();
        JScrollPane jspGRB = new JScrollPane(txtGRBTS,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//add(txtGRBTS,11,6,3,1.5,pnlCLSDT,'R');
		add(jspGRB,11,6,3,1.5,pnlCLSDT,'R');
        staGRBDT = new String[50][3];
		staQPRDT = new String[50][3];
        staCMPFL = new String[20];
        add(btnRLS = new JButton("Release"),15,6,1,1,pnlCLSDT,'L');
        txtPPRCD.setInputVerifier(objINPVR);	
        txtCPRCD.setInputVerifier(objINPVR);	
       // Reclassification
        setMatrix(20,8);
        add(new JLabel("Product Type"),1,1,1,1,pnlRCLDT,'L');
	    add(cmbRCPRD = new JComboBox(),1,2,1,2,pnlRCLDT,'L');
        add(new JLabel("Lot Number"),1,4,1,1,pnlRCLDT,'L');
	    add(txtRCLOT = new TxtLimit(8),1,5,1,1,pnlRCLDT,'L');
	    add(new JLabel("Bagged Qty."),1,6,1,1,pnlRCLDT,'L');
	    add(txtRBGQT = new TxtNumLimit(6.3),1,7,1,1,pnlRCLDT,'L');
  	
	    tblLOTRC = crtTBLPNL1(pnlRCLDT,new String[]{" ","RCL. No. ","Product Code ","Grade","Classification Date","Dsp. Qty.","Stock(in MT)"},10,2,1,3.2,7,new int[]{20,50,100,160,120,100,100},new int[]{0}) ;	
	    add(new JLabel("New Grade"),6,1,1,1,pnlRCLDT,'L');
	    add(txtNWPRD = new TxtLimit(10),6,2,1,1,pnlRCLDT,'L');
		add(txtNWGRD = new TxtLimit(40),6,3,1,3,pnlRCLDT,'L');		
		add(txtNWDAT = new TxtLimit(40),6,6     ,1,1.5,pnlRCLDT,'L');		
	
	    txtRCLOT.setInputVerifier(objINPVR);	
	    JLabel lblSTK,lblTST; 
	    add(lblSTK = new JLabel("Stock Details"),7,1,1,1,pnlRCLDT,'L');
	    add(lblTST = new JLabel("Test Details"),7,3,1,1,pnlRCLDT,'L');
	    lblSTK.setForeground(java.awt.Color.blue);
	    lblTST.setForeground(java.awt.Color.blue);
	    tblSTKDT = crtTBLPNL1(pnlRCLDT,new String[]{" ","Location ","Quantity"},50,8,1,8,2,new int[]{20,60,80},new int[]{0}) ;
	    tblTSTRC = crtTBLPNL1(pnlRCLDT,new String[]{" ","Q.Para.","Description  ","   UOM    ","Specifications","Test Value","Flag "},20,8,3,8,5.5,new int[]{10,45,160,85,120,70,10},new int[]{0}) ;
	    tblTSTRC.setColumnColor(TB3_FLAG,java.awt.Color.red);

        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strPRDCD = "";
				while(M_rstRSSET.next())
				{
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strPRDCD.equals(""))
					{
						cmbPRDTP.addItem(L_strPRDCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						cmbRCPRD.addItem(L_strPRDCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					}
				}
				M_rstRSSET.close();
			}		
		M_strSQLQRY = "SELECT SUBSTRING(CMT_CODCD,5,10) L_CODCD FROM CO_CDTRN WHERE "
		            + " SUBSTRING(CMT_CODCD,1,3) ='"+cl_dat.M_strUSRCD_pbst+"'"
                    + " AND CMT_CGMTP ='AUT' and CMT_CGSTP ='COXXAUT' ORDER BY CMT_NMP01";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		int L_intTABCT =0;
		String L_strCODCD;
		if(M_rstRSSET !=null)
		while(M_rstRSSET.next())
		{
		    L_strCODCD = M_rstRSSET.getString("L_CODCD").trim();
		    if(L_strCODCD.equals("GPAPR"))
		    {
		        jtpTRNDT.add(pnlGPAPR,strGPAPR_fn);
		        intGPAPR = L_intTABCT;
		        tblGPAPR.addMouseListener(this);
		        L_intTABCT++;
		    }
		    else if(L_strCODCD.equals("GPAUT"))
		    {
		        jtpTRNDT.add(pnlGPAUT,"GP Authorisation");
		        intGPAUT = L_intTABCT;
		        tblGPAUT.addMouseListener(this);
		        L_intTABCT++;
		    }
		    else if(L_strCODCD.equals("INAPR"))
		    {
		        jtpTRNDT.add(pnlINAUT,"Indent Authorisation");
		        intINAPR = L_intTABCT;
		        tblINAUT.addMouseListener(this);
		        L_intTABCT++;
		    }
		    else if(L_strCODCD.equals("FNLCL"))
		    {
		        jtpTRNDT.add(pnlCLSDT,strFNLCL_fn);
		        intFNLCL = L_intTABCT;
		        tblTSTDT.addMouseListener(this);
		        ((JTextField)tblTSTDT.cmpEDITR[TB_CMPVL]).addKeyListener(this);
		         L_intTABCT++;
		    }
		    else if(L_strCODCD.equals("PRVCL"))
		    {
		        jtpTRNDT.add(pnlCLSDT,strPRVCL_fn);
		        intPRVCL = L_intTABCT;
		        tblTSTDT.addMouseListener(this);
		        ((JTextField)tblTSTDT.cmpEDITR[TB_CMPVL]).addKeyListener(this);
		        L_intTABCT++;
		    }
		    else if(L_strCODCD.equals("LTRCL"))
		    {
		        jtpTRNDT.add(pnlRCLDT,strLTRCL_fn);
		        intLTRCL = L_intTABCT;
		         L_intTABCT++;
		    }
		    else if(L_strCODCD.equals("LTPTF"))
		    {
		        jtpTRNDT.add(pnlPTFDT,strLTPTF_fn);
		        intLTPTF = L_intTABCT;
		         L_intTABCT++;
		    }
		}
	  	jtpTRNDT.addMouseListener(this);
		jtpTRNDT.addChangeListener(this);
		JComboBox cmbAUTST = new JComboBox();
		cmbAUTST.addItem("Select");
		cmbAUTST.addItem("Held For Discussion");
		cmbAUTST.addItem("Authorised");
	    JComboBox cmbGPAUT = new JComboBox();
		cmbGPAUT.addItem("Select");
		cmbGPAUT.addItem("Held For Discussion");
		cmbGPAUT.addItem("Approved");
		tblINAUT.setCellEditor(TBL_APRFL,cmbAUTST);
		tblGPAPR.setCellEditor(TBL_APRFL,cmbGPAUT);
	
		add(jtpTRNDT,1,1,18,8,this,'L');
		M_strSQLQRY = "SELECT CMT_CODCD,CMT_SHRDS from CO_CDTRN WHERE CMT_CGMTP||CMT_CGSTP IN('SYSCOXXDPT','SYSMMXXSST','SYSMMXXMGP') AND isnull(CMT_STSFL,'') <>'X'"; 
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				hstCODDS.put(M_rstRSSET.getString("CMT_CODCD"),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));
				hstCODCD.put(nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""),M_rstRSSET.getString("CMT_CODCD"));
			}
		}
		//// PTF ENTRY SCREEN
    		M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
    		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    		if(M_rstRSSET !=null)
    		{
        		while(M_rstRSSET.next())
        		{
        			strREFDT = M_rstRSSET.getString("CMT_CCSVL").trim();
        			strREFTM = M_rstRSSET.getString("CMT_CHP02").trim();
        			strREFFL = M_rstRSSET.getString("CMT_CHP01").trim();
        		}
        		M_rstRSSET.close();
    		}
    		add(new JLabel("PTF No."),2,1,1,1,pnlPTFDT,'L');
	        add(txtPTFNO = new TxtLimit(8),2,2,1,1,pnlPTFDT,'L');
	        add(new JLabel("Date"),2,3,1,1,pnlPTFDT,'L');
	        add(txtPTFDT = new TxtDate(),2,4,1,1,pnlPTFDT,'L');
            add(new JLabel("PTF Category"),2,5,1,1,pnlPTFDT,'L');
	        add(txtPTFCT = new TxtLimit(40),2,6,1,2,pnlPTFDT,'L');
    
        	add(new JLabel("Balance Qty."),3,1,1,1,pnlPTFDT,'L');
	        add(txtBALQT = new TxtNumLimit(12.2),3,2,1,1,pnlPTFDT,'L');
	      	add(new JLabel("PTF Qty."),3,3,1,1,pnlPTFDT,'L');
	        add(txtPTFQT = new TxtNumLimit(12.2),3,4,1,1,pnlPTFDT,'L');
	        add(btnPRINT = new JButton("Print"),3,7,1,1,pnlPTFDT,'L');
	        int[] L_COLSZ = {10,80,30,60,80,80,80,200,10,10,10,10,10};
			String[] L_TBLHD = {"Status","Lot No.","Rcl. No.","Package Type","Old Grade","Clsfd. Grade","Balance Qty.","Remarks","W/H Type","PRDTP","PKGCD","PRDCD","OPRCD"};
			tblPTFDT = crtTBLPNL1(pnlPTFDT,L_TBLHD,100,5,1,11,7,L_COLSZ,new int[]{0});
    		tblPTFDT.addMouseListener(this);
    		cl_dat.M_flgHELPFL_pbst = false;
    		txtPTFDT.setText(cl_dat.M_strLOGDT_pbst);
    		txtPTFNO.setEnabled(false);
    		txtPTFDT.setEnabled(false);
    		txtPTFCT.setEnabled(false);
    		txtBALQT.setEnabled(false); 
    		txtPTFQT.setEnabled(false); 
    		btnPRINT.setEnabled(false);
            txtPTFNO.setInputVerifier(objINPVR);	
            txtPTFDT.setInputVerifier(objINPVR);	
            txtPTFCT.setInputVerifier(objINPVR);	
    		///chkPTFTXT("IN");
       }
	   catch(Exception L_E)
	   {
		   setMSG(L_E,"Constructor");
	   }
	}
	public void stateChanged(ChangeEvent L_CE)
	{
		int L_intSELCT =0;
		setMSG("",'N');
		if(jtpTRNDT.getSelectedIndex()==intGPAPR)
		{
			L_intSELCT =0;
			for(int i=0;i<tblGPAUT.getRowCount();i++)
			{
				if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Gate Pass Authorisation will be lost..",'E');
			}
			L_intSELCT =0;
			for(int i=0;i<tblINAUT.getRowCount();i++)
			{
				if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Indent Authorisation will be lost..",'E');
			}
		}
		else if(jtpTRNDT.getSelectedIndex()==intGPAUT)
		{
			for(int i=0;i<tblGPAPR.getRowCount();i++)
			{
				if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT >0)
			{
				setMSG("All selections for Gate Pass Approval will be lost..",'E');
			}
			L_intSELCT =0;
			for(int i=0;i<tblINAUT.getRowCount();i++)
			{
				if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Indent Authorisation will be lost..",'E');
			}
		}
		else if(jtpTRNDT.getSelectedIndex()==intINAPR)
		{
			for(int i=0;i<tblGPAPR.getRowCount();i++)
			{
				if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT >0)
			{
				setMSG("All selections in Gate Pass Approval will be lost..",'E');
			}
			L_intSELCT =0;
			for(int i=0;i<tblGPAUT.getRowCount();i++)
			{
				if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					L_intSELCT++;
				}
			}
			if(L_intSELCT > 0)
			{
				setMSG("All selections in Gate Pass Authorisation will be lost..",'E');
			}
		
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		String L_strINDNO ="";
		String L_strGPPNO ="";
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
				    if((jtpTRNDT.getSelectedIndex() ==intLTRCL)||(jtpTRNDT.getSelectedIndex() ==intPRVCL))
				    txtLOTNO.requestFocus();
				}
			}
	        if(M_objSOURC == btnRPDC3)
			{
			    mm_rpind objINDRP;
				int i=0;
			    for(i=0;i<tblINAUT.getRowCount();i++)
				{
					if(tblINAUT.getValueAt(i,TBL_DSPFL).toString().equals("true"))
					{
						L_strINDNO = tblINAUT.getValueAt(i,TBL_DOCNO).toString();
						//L_strSTRTP = tblINAUT.getValueAt(i,TBL_DOCNO).toString();
						objINDRP = new mm_rpind(M_strSBSCD.substring(0,2)+hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"00",hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString());
						objINDRP.getALLREC(L_strINDNO,L_strINDNO,'I',"PI");
						break;
					}
				}
				
				try
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					p  = r.exec("c:\\windows\\wordpad.exe "+cl_dat.M_strREPSTR_pbst+"mm_rpind.doc"); 
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"Error.exescrn.. ");
 				}
				finally
				{
					objINDRP = null;
				}
			}
			if((M_objSOURC == btnRPDC1)||(M_objSOURC == btnRPDC2))
			{
				int i=0;
				if(M_objSOURC == btnRPDC1)
				{
					for(i=0;i<tblGPAPR.getRowCount();i++)
					{
						if(tblGPAPR.getValueAt(i,TBL_DSPFL).toString().equals("true"))
						{
							L_strGPPNO = tblGPAPR.getValueAt(i,TBL_DOCNO).toString();
							break;
						}
					}
					mm_rpgpp objGPPRP = new mm_rpgpp(M_strSBSCD.substring(0,2)+hstCODCD.get(tblGPAPR.getValueAt(i,TBL_STRTP).toString()).toString()+"00");
					objGPPRP.genRPFIL(L_strGPPNO,L_strGPPNO,hstCODCD.get(tblGPAPR.getValueAt(i,TBL_DOCTP).toString()).toString(),tblGPAPR.getValueAt(i,TBL_DOCTP).toString(),tblGPAPR.getValueAt(i,TBL_STRTP).toString());
				}
				else if(M_objSOURC == btnRPDC2)
				{
					i =0;
					for(i=0;i<tblGPAUT.getRowCount();i++)
					{
						if(tblGPAUT.getValueAt(i,TBL_DSPFL).toString().equals("true"))
						{
							L_strGPPNO = tblGPAUT.getValueAt(i,TBL_DOCNO).toString();
							break;
						}
					}
				    mm_rpgpp objGPPRP = new mm_rpgpp(M_strSBSCD.substring(0,2)+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"00");
					objGPPRP.genRPFIL(L_strGPPNO,L_strGPPNO,hstCODCD.get(tblGPAUT.getValueAt(i,TBL_DOCTP).toString()).toString(),tblGPAUT.getValueAt(i,TBL_DOCTP).toString(),tblGPAUT.getValueAt(i,TBL_STRTP).toString());
				}
				try
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					p  = r.exec("c:\\windows\\wordpad.exe "+cl_dat.M_strREPSTR_pbst+"mm_rpgpp.doc"); 
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"Error.exescrn.. ");
 				}
				finally
				{
					//objGPPRP = null;
				}
			}
			if((M_objSOURC == txtLOTNO)||(M_objSOURC == txtRCLNO))// For classification screen
			{
           		//exeINTSTA();
        		String L_strLOTNO = txtLOTNO.getText().trim();
        		String L_strRCLNO = txtRCLNO.getText().trim();
        		clrCOMP();
        		txtLOTNO.setText(L_strLOTNO);
        		if(M_objSOURC == txtRCLNO)
        		    txtRCLNO.setText(L_strRCLNO);
        		if(L_strLOTNO.length() ==0)
        		{
        		    setMSG("Enter Lot number ..",'E');
        		    return;
        		}
        		if((M_objSOURC == txtRCLNO) &&(L_strRCLNO.length() ==0))
        		{
        		    setMSG("Enter Reclasification number ..",'E');
        		    return;   
        		}
        		M_strSQLQRY = "SELECT MAX(LT_RCLNO) L_RCLNO from PR_LTMST WHERE LT_PRDTP = '"+cmbPRDTP.getSelectedItem().toString().substring(0,2) +"'";
        		M_strSQLQRY += " AND LT_LOTNO ='"+txtLOTNO.getText().trim()+"'";
        		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
        		if(M_rstRSSET !=null)
        		if(M_rstRSSET.next())
        		   if(txtRCLNO.getText().length() ==0)
        		    txtRCLNO.setText(M_rstRSSET.getString("L_RCLNO"));
        		if(M_rstRSSET !=null)
        		    M_rstRSSET.close();
            	if(txtRCLNO.getText().length() ==0)
            	    txtRCLNO.setText("00");
        		setENBL(true);
        		txtGRBTS.setText("");
        		txtADDTS.setText("");
        		setMSG("",'N');
        		setCursor(cl_dat.M_curWTSTS_pbst);	
        		flgLOTFL = getLOTDET();
        		flgACLFL = false;
		        flgACLFL = getACLST(cmbPRDTP.getSelectedItem().toString().substring(0,2),txtLOTNO.getText(),txtRCLNO.getText());
        		if((strPTAFL.equals("N")) && flgACLFL)
                    lblAUTCLS.setText("Probable Lot for Auto Classification");   
                else if(strPTAFL.equals("Y"))
    	          lblAUTCLS.setText("Plant Abnormaility : "+strPTARM);  
        		if(!flgLOTFL)
        		{
        		      setCursor(cl_dat.M_curDFSTS_pbst);
        			  txtLOTNO.requestFocus();
        		}
            	this.setCursor(cl_dat.M_curDFSTS_pbst);
           		if(tblTSTDT.isEditing())
        		    tblTSTDT.getCellEditor().stopCellEditing();
        		tblTSTDT.setRowSelectionInterval(0,0);
        		tblTSTDT.setColumnSelectionInterval(0,0);
       			/*if(chrCLSFL !='Q')
    			{
    			    //System.out.println("Remark focus from 1");
    				/// commented today
    				txtREMDS.requestFocus();
    			}*/
    			if(flgLOTFL)
    			{
    			   // if(txtREMDS.isEnabled())
    			   txtREMDS.requestFocus();
    			}

    		}
		   if (M_objSOURC == btnRLS)
           {
    			exeRLSLOT(strRLSFL,strRLSPRD);
    	   }
           else if ((M_objSOURC == txtCPRCD) || (M_objSOURC == txtPPRCD))
           {
			   setCursor(cl_dat.M_curWTSTS_pbst);
               String L_PRDDS = " ",L_strFLAG ="";
	           if(M_objSOURC == txtCPRCD)
			   {
				   if(txtCPRCD.getText().trim().length() >0)
				   {
						txtCLSTM.setText(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText());
						txtCLSBY.setText(cl_dat.M_strUSRCD_pbst);
						L_PRDDS = exePRVLDN(txtCPRCD.getText().trim());
				
				   }
				   else
					   setMSG("Product code can not be blank ..",'E');
			   }
               else if (M_objSOURC == txtPPRCD)
			   {
				   if(txtPPRCD.getText().trim().length() >0)
				   {
						txtPCLTM.setText(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText());
						txtPCLBY.setText(cl_dat.M_strUSRCD_pbst);
						L_PRDDS = exePRVLDN(txtPPRCD.getText().trim());
					//	tblTSTDT.requestFocus();
					//	tblTSTDT.setRowSelectionInterval(0,0);
					//	tblTSTDT.setColumnSelectionInterval(TB_CMPVL,TB_CMPVL);
    				}
				   else
					   setMSG("Product code can not be blank ..",'E');
			   }
               if(L_PRDDS.trim().equals("-"))
               {
                    setMSG("Product Code does not exist ... ",'E');
                    txtCPRCD.requestFocus();
               }
               else 
               {
			   ////  Added on 18/04/ to sort out B grade classification issue
				String L_strTEMP ="";
				if (!txtCPRCD.getText().trim().equals(""))
		      		 L_strTEMP = txtCPRCD.getText().trim();
				else if (!txtPPRCD.getText().trim().equals(""))
			      	 L_strTEMP = txtPPRCD.getText().trim();
				else
	      	 		L_strTEMP = txtTPRCD.getText().trim();		  
		  	if (!txtCPRCD.getText().trim().equals(""))
			    chkQPRVTR(L_strTEMP);	
			   //// end Added on 18/04/ to sort out B grade classification issue
		
					if(M_objSOURC == txtCPRCD)
					{
						 txtCPRDS.setText(L_PRDDS);
					}
					else if(M_objSOURC == txtPPRCD)
					{
						 txtPPRDS.setText(L_PRDDS);
					}
				tblTSTDT.clrTABLE();
				txtGRBTS.setText("");
				txtADDTS.setText("");
				getTSTDET(cmbPRDTP.getSelectedItem().toString().substring(0,2));
				getALLREC();
				for(int j=0;j<intQPRCT;j++)
				{
					L_QPRCD = nvlSTRVL(tblTSTDT.getValueAt(j,TB_QPRCD).toString(),"");
					if(hstCMPVL !=null)
		  			{
		  				L_CMPVL = (String)hstCMPVL.get(L_QPRCD);
		  				if(L_CMPVL != null)
							L_QPRVL = Float.valueOf(L_CMPVL).floatValue();
						tblTSTDT.setValueAt(L_CMPVL,j,TB_CMPVL);
		  			}
		  			if(hstBAGVL !=null)
		  			{
		  				L_BAGVL = (String)hstBAGVL.get(L_QPRCD);
		  				tblTSTDT.setValueAt(L_BAGVL,j,TB_BAGVL);
		
		  			}
				}
				///////
				if(chrCLSFL !='Q')
				{
					for(int i=0;i<intROWCT;i++)
					{
						L_strFLAG = tblTSTDT.getValueAt(i,TB_FLAG).toString().trim();
						if(L_strFLAG.length() >0)
						{
							if(L_strFLAG.equals("*"))
							{
								flgOUTRG = true;			// Out of Range
							}
						}
					}
					if(!flgOUTRG)
					{
						if(!flgTSTOK)
							setMSG("Some of the test values are not available..",'E');
						else
							setMSG("",'N');
						if(chrCLSFL !='Q')
						    cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}
					else
					{
						cl_dat.M_btnSAVE_pbst.setEnabled(false); 
					}
					////// included on 14/03 for Off grade only
	        		//		if(LM_OFFGRD)
			        //			btnCLS.setEnabled(true); 
					///////
				}
			   }
			if(tblTSTDT.isEditing())
			{
					tblTSTDT.getCellEditor().stopCellEditing();
		   			if(chrCLSFL =='P')
		   			{
    		   			tblTSTDT.setRowSelectionInterval(0,0);
    					tblTSTDT.setColumnSelectionInterval(TB_CMPVL,TB_CMPVL);
    					tblTSTDT.editCellAt(0,TB_CMPVL);
    					tblTSTDT.cmpEDITR[TB_CMPVL].requestFocus();
		   			}
		   	}
			setCursor(cl_dat.M_curDFSTS_pbst);
          }
          else if(M_objSOURC == txtRCLOT)
	      {
	         if(flgLTVLD)
	            dspLOTDT ();
	      }
	      else if(M_objSOURC == txtNWPRD)
	      {
	        	// Check for repeated classification
				setCursor(cl_dat.M_curWTSTS_pbst);
				tblTSTRC.cmpEDITR[TB3_CMPVL].setEnabled(true);
				LM_REPCLS = false; // Repeated classification tag
				LM_REPRCL = "";
				String L_strPRDCD ="";
				if(txtNWPRD.getText().trim().equals(strLSTPRD))
				{
				    setMSG("Lot is already classified as "+strLSTPRD,'E');
				    setCursor(cl_dat.M_curDFSTS_pbst);
				    return ;
				}
				txtNWGRD.setText("");
			    M_strSQLQRY = "select PR_PRDCD,PR_PRDDS from CO_PRMST where SUBSTRING(PR_PRDCD,1,2) = '";
                M_strSQLQRY += tblLOTRC.getValueAt(0,TB1_PRDCD).toString().trim().substring(0,2) + "' and PR_STSFL <> 'X'";
        		M_strSQLQRY += " and PR_PRDCD ='"+txtNWPRD.getText().trim()+"'";
        		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
        		if(M_rstRSSET ==null)
        		{
        		    setMSG("Invalid Product Code..",'E');
        		    setCursor(cl_dat.M_curDFSTS_pbst);
        		    return ;
        		}
        		if(M_rstRSSET.next())
    			{
    				//setMSG("Valid Grade..",'N');
    				txtNWGRD.setText(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
    				setCursor(cl_dat.M_curDFSTS_pbst);
    				M_rstRSSET.close();
    			}
    			else
    			{ 
    			    setMSG("Invalid Product Code..",'E');
    			    return;
    			}
				for(int i=0;i<10;i++)// 10 rows in lot table
				{
					L_strPRDCD = tblLOTRC.getValueAt(i,TB1_PRDCD).toString().trim();
					if(L_strPRDCD.length() >0)
					{
						if(txtNWPRD.getText().trim().equals(L_strPRDCD))
						{
							LM_REPCLS = true;	
							LM_REPRCL = tblLOTRC.getValueAt(i,1).toString().trim();
						}
					}
				}
				if(LM_REPCLS)
				    tblTSTRC.cmpEDITR[TB3_CMPVL].setEnabled(false);
				tblTSTRC.clrTABLE();
				if(tblTSTRC.isEditing())
        		    tblTSTRC.getCellEditor().stopCellEditing();
        		tblTSTRC.setRowSelectionInterval(0,0);
        		tblTSTRC.setColumnSelectionInterval(0,0);
				getQPDTL(txtNWPRD.getText().trim());
				if(LM_REPCLS)
				{
					// Block the modification of parameters.
					dspTSTDT(LM_REPRCL,txtNWPRD.getText().trim());
				}
				else
					dspTSTDT(strLSTRCL,txtNWPRD.getText().trim());
				for(int i=0;i<intRCQPCT;i++)
				{
					vldRNG(i);
				}
				if(tblTSTRC.isEditing())
				{
					tblTSTRC.getCellEditor().stopCellEditing();
				}
				exeRCLCHK();
				setCursor(cl_dat.M_curDFSTS_pbst);
	      }
	      else if(M_objSOURC == txtPTFCT)
	      {
	        if(vldPTFCT())
	        {
	            tblPTFDT.clrTABLE();
	            if(tblPTFDT.isEditing())
	            {
	                tblPTFDT.getCellEditor().stopCellEditing();
	                tblPTFDT.setRowSelectionInterval(0,0);
	                tblPTFDT.setColumnSelectionInterval(0,0);
	            }
	            getPTFREC();
	        }
	      }
	      else if(M_objSOURC == btnPRINT)
		  {
              try
              {
                java.sql.Date L_datTEMP;
                M_strSQLQRY = " SELECT distinct PTF_PTFDT from FG_PTFRF WHERE PTF_PTFNO ='"+strPRPTF +"'";
                M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
                if(M_rstRSSET !=null)
                if(M_rstRSSET.next())
                {
                    L_datTEMP = M_rstRSSET.getDate("PTF_PTFDT");
                    if(L_datTEMP !=null)
                    {
                        strPRVDT =M_fmtLCDAT.format(L_datTEMP); 
                    }
                }
              	fg_rpptf ofg_rpptf = new fg_rpptf(M_strSBSCD,strPRPTF,strPRVDT); 
				ofg_rpptf.getDATA();
				Runtime r = Runtime.getRuntime();
				Process p = null;					
				p  = r.exec("c:\\windows\\iexplore.exe "+cl_dat.M_strREPSTR_pbst+"fg_rpptf.html"); 
				setMSG("For Printing Select File Menu, then Print  ..",'N');
              }
              catch(Exception L_E)
              {
                setMSG(L_E,"btnPRINT");
              }
          }
	   	}
		catch(Exception L_E)
		{
			setMSG("Error in Action Performed ..",'E');
		}
	}
	private void getDOCDT(ResultSet P_rstRSSET,cl_JTable P_tblGPDTL) throws Exception
	{
		int i=0;
		ResultSet L_rstRSSET;
		String L_strSTRTP ="",L_strMGPNO ="",L_strDPTCD ="",L_strMGPTP ="",L_strSTSFL ="";
		P_tblGPDTL.clrTABLE();
		if(P_rstRSSET !=null)
		{
			flgDATA = true;
			while(P_rstRSSET.next())
			{
		        L_strSTRTP = nvlSTRVL(P_rstRSSET.getString("GP_STRTP"),"");
				L_strMGPNO = nvlSTRVL(P_rstRSSET.getString("GP_MGPNO"),"");
				L_strMGPTP = nvlSTRVL(P_rstRSSET.getString("GP_MGPTP"),"");
				L_strDPTCD = nvlSTRVL(P_rstRSSET.getString("GP_DPTCD"),"");
				if(hstCODDS.containsKey((String)L_strSTRTP))
				    P_tblGPDTL.setValueAt(hstCODDS.get(L_strSTRTP).toString(),i,TBL_STRTP);
				else
				    P_tblGPDTL.setValueAt(L_strSTRTP,i,TBL_STRTP);
				if(hstCODDS.containsKey((String)L_strMGPTP))
				    P_tblGPDTL.setValueAt(hstCODDS.get(L_strMGPTP).toString(),i,TBL_DOCTP);
				else
				    P_tblGPDTL.setValueAt(L_strMGPTP,i,TBL_DOCTP);
				P_tblGPDTL.setValueAt(L_strMGPNO,i,TBL_DOCNO);
				if(hstCODDS.containsKey((String)L_strDPTCD))
				    P_tblGPDTL.setValueAt(hstCODDS.get(L_strDPTCD).toString(),i,TBL_DPTCD);
				else
				    P_tblGPDTL.setValueAt(L_strDPTCD,i,TBL_DPTCD);
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_APRVL"),"0.00"),i,TBL_APRVL);
				L_strSTSFL = nvlSTRVL(P_rstRSSET.getString("GP_STSFL"),"");
				if(L_strSTSFL.equals("2"))
				{
					L_strSTSFL = "Held For Discussion";
					tblGPAPR.setValueAt(L_strSTSFL,i,TBL_APRFL);
				}
                P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_VENCD"),""),i,TBL_VENCD);
				M_strSQLQRY ="SELECT RM_DOCTP,RM_REMDS FROM MM_RMMST WHERE RM_STRTP ='"+L_strSTRTP +"'"
							 +" AND RM_TRNTP ='GP' AND RM_DOCTP in('GP','RQ') AND RM_DOCNO ='"+L_strMGPNO +"' AND isnull(RM_STSFL,'') <>'X'";										
				L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				String L_strREMDS ="";
				if(L_rstRSSET !=null)
				{
				 	while(L_rstRSSET.next())
					{
						L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
						if(nvlSTRVL(L_rstRSSET.getString("RM_DOCTP"),"").equals("GP"))
							P_tblGPDTL.setValueAt(L_strREMDS,i,TBL_REMDS);
						else if(nvlSTRVL(L_rstRSSET.getString("RM_DOCTP"),"").equals("RQ"))
							P_tblGPDTL.setValueAt(L_strREMDS,i,TBL_COMDS);
					}
				}
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_PREBY"),""),i,TBL_PREBY);
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_VEHNO"),""),i,TBL_VEHNO);
				P_tblGPDTL.setValueAt(nvlSTRVL(P_rstRSSET.getString("GP_VEHDS"),""),i,TBL_VEHDS);
				i++;
			}
		}
	}
	private void getITDTL(cl_JTable P_tblGPDTL,cl_JTable P_tblITMDT) throws Exception 
	{
		int L_intSELROW = P_tblGPDTL.getSelectedRow();
		if(P_tblGPDTL.getValueAt(L_intSELROW,TBL_DSPFL).equals("false"))
		{
			P_tblITMDT.clrTABLE();
			return;
		}
		int i=0;
		for(i=0;i<P_tblGPDTL.getRowCount();i++)
		if(i != L_intSELROW)
			P_tblGPDTL.setValueAt(new Boolean(false),i,TBL_DSPFL);
		M_strSQLQRY = "SELECT GP_MGPNO,GP_MATCD,GP_MGPDT,GP_VENCD,GP_VENNM,GP_DUEDT,GP_STSFL,GP_FRWTO," 
		+"GP_ISSQT,GP_RECQT,GP_DPTCD,GP_VEHNO,GP_VEHDS,GP_GRNNO,GP_GINNO,GP_INSFL,GP_STSFL,GP_PREBY,GP_APRBY,GP_APRVL,GP_PKGCT,CT_MATDS,CT_UOMCD "
		+"FROM MM_GPTRN,CO_CTMST WHERE CT_MATCD = GP_MATCD AND GP_STRTP = '"+hstCODCD.get(P_tblGPDTL.getValueAt(L_intSELROW,TBL_STRTP).toString()).toString()+"' AND "
		+"GP_MGPTP = '"+hstCODCD.get(P_tblGPDTL.getValueAt(L_intSELROW,TBL_DOCTP).toString()).toString()+"' AND GP_MGPNO = '"+P_tblGPDTL.getValueAt(L_intSELROW,TBL_DOCNO).toString()+"' AND GP_STSFL <>'X'";
		P_tblITMDT.clrTABLE();
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		i=0;
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				P_tblITMDT.setValueAt(M_rstRSSET.getString("GP_MATCD"),i,1);
				P_tblITMDT.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,2);
				P_tblITMDT.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,3);
				P_tblITMDT.setValueAt(M_rstRSSET.getString("GP_ISSQT"),i,4);
				datTEMP = M_rstRSSET.getDate("GP_DUEDT");
				if(datTEMP !=null)
				{
					P_tblITMDT.setValueAt(M_fmtLCDAT.format(datTEMP),i,5);
				}
				i++;
			}
		}
	}
	private void getINDTL() throws Exception 
	{
		int L_intSELROW = tblINAUT.getSelectedRow();
		if(tblINAUT.getValueAt(L_intSELROW,TBL_DSPFL).equals("false"))
		{
			tblITDT3.clrTABLE();
			return;
		}
		int i=0;
		String L_strTEMP ="";
		for(i=0;i<tblINAUT.getRowCount();i++)
		if(i != L_intSELROW)
			tblINAUT.setValueAt(new Boolean(false),i,TBL_DSPFL);
		M_strSQLQRY = "SELECT IN_MATCD,IN_INDQT,IN_REQDT,IN_EXPDT,IN_TCFFL,IN_INSFL,CT_MATDS,CT_UOMCD "
		+"FROM MM_INMST,CO_CTMST WHERE CT_MATCD = IN_MATCD AND IN_STRTP = '"+hstCODCD.get(tblINAUT.getValueAt(L_intSELROW,TBL_STRTP).toString()).toString()+"'"
		+" AND IN_INDNO = '"+tblINAUT.getValueAt(L_intSELROW,TBL_DOCNO).toString()+"' AND IN_STSFL <>'X'";
		tblITDT3.clrTABLE();
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		i=0;
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				tblITDT3.setValueAt(M_rstRSSET.getString("IN_MATCD"),i,1);
				tblITDT3.setValueAt(M_rstRSSET.getString("CT_UOMCD"),i,2);
				tblITDT3.setValueAt(M_rstRSSET.getString("CT_MATDS"),i,3);
				tblITDT3.setValueAt(M_rstRSSET.getString("IN_INDQT"),i,4);
				datTEMP = M_rstRSSET.getDate("IN_REQDT");
				if(datTEMP !=null)
				{
					tblITDT3.setValueAt(M_fmtLCDAT.format(datTEMP),i,5);
				}
				datTEMP = M_rstRSSET.getDate("IN_EXPDT");
				if(datTEMP !=null)
				{
					tblITDT3.setValueAt(M_fmtLCDAT.format(datTEMP),i,6);
				}
				L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_TCFFL"),"");
				if(L_strTEMP.equals("Y"))
					tblITDT3.setValueAt(Boolean.TRUE,i,7);
				else
					tblITDT3.setValueAt(Boolean.FALSE,i,7);
				L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_INSFL"),"");
				if(L_strTEMP.equals("Y"))
					tblITDT3.setValueAt(Boolean.TRUE,i,8);
				else
					tblITDT3.setValueAt(Boolean.FALSE,i,8);
				i++;
			}
		}
	}
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			super.mouseClicked(L_ME);
			if(M_objSOURC == jtpTRNDT)
			{
				M_strSQLQRY ="";
                if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))               	
                {
                    setMSG("Please Select the option as Authorisation..",'E');
                   /* if((jtpTRNDT.getSelectedIndex() !=intLTRCL)&&(jtpTRNDT.getSelectedIndex() !=intFNLCL)&&(jtpTRNDT.getSelectedIndex() !=intPRVCL))
                    {
                        cl_dat.M_btnSAVE_pbst.setEnabled(true);
                    }*/
                    return;
                }
                if(jtpTRNDT.getSelectedIndex() ==intGPAPR)
				{
					tblGPAPR.setEnabled(false);
					tblITDT1.setEnabled(false);
					tblGPAPR.cmpEDITR[TBL_CHKFL].setEnabled(true);
					tblGPAPR.cmpEDITR[TBL_DSPFL].setEnabled(true);
					tblGPAPR.cmpEDITR[TBL_APRFL].setEnabled(true);
					M_strSQLQRY = "SELECT DISTINCT GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MGPDT,GP_DPTCD,GP_APRVL,GP_VENCD,GP_PREBY,GP_VEHNO,GP_VEHDS,GP_STSFL FROM MM_GPTRN "
						+"WHERE isnull(GP_STSFL,'') in('1','2') AND GP_FRWTO = '"+cl_dat.M_strUSRCD_pbst+"'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					getDOCDT(M_rstRSSET,tblGPAPR);
				}
				else if(jtpTRNDT.getSelectedIndex() ==intGPAUT)
				{
					tblGPAUT.setEnabled(false);
					tblITDT2.setEnabled(false);
					tblGPAUT.cmpEDITR[TBL_CHKFL].setEnabled(true);
					tblGPAUT.cmpEDITR[TBL_DSPFL].setEnabled(true);
					M_strSQLQRY = "SELECT DISTINCT GP_STRTP,GP_MGPTP,GP_MGPNO,GP_MGPDT,GP_DPTCD,GP_APRVL,GP_VENCD,GP_PREBY,GP_VEHNO,GP_VEHDS,GP_STSFL FROM MM_GPTRN "
						+"WHERE isnull(GP_STSFL,'') = '3'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					getDOCDT(M_rstRSSET,tblGPAUT);
				}
				else if(jtpTRNDT.getSelectedIndex() ==intINAPR)
				{
					ResultSet L_rstRSSET;
					String L_strSTRTP="",L_strINDNO ="",L_strINDTP="",L_strTEMP ="";
					tblINAUT.setEnabled(false);
					tblITDT3.setEnabled(false);
					tblINAUT.cmpEDITR[TBL_CHKFL].setEnabled(true);
					tblINAUT.cmpEDITR[TBL_DSPFL].setEnabled(true);
					tblINAUT.cmpEDITR[TBL_APRFL].setEnabled(true);
					M_strSQLQRY = "SELECT DISTINCT IN_INDTP,IN_STRTP,IN_INDNO,IN_DPTCD,IN_FRWBY,IN_STSFL,sum(IN_INDVL) L_APRVL FROM MM_INMST "
							+"WHERE isnull(IN_STSFL,'') IN ('2','3') and IN_FRWTO ='"+cl_dat.M_strUSRCD_pbst+"' and isnull(IN_STSFL,'') <>'X'"
						    +" group by IN_INDTP,IN_STRTP,IN_INDNO,IN_DPTCD,IN_FRWBY,IN_STSFL ORDER BY IN_INDTP,IN_STRTP,IN_INDNO ";
					tblINAUT.clrTABLE();
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					int i=0;
					if(M_rstRSSET !=null)
					{
						while(M_rstRSSET.next())
						{
							L_strSTRTP = M_rstRSSET.getString("IN_STRTP");
							L_strINDNO = M_rstRSSET.getString("IN_INDNO");
							L_strINDTP = M_rstRSSET.getString("IN_INDTP");
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IN_STSFL"),"");
							if(L_strINDTP.equals("01"))
								L_strINDTP = "P.I.";
							else
								L_strINDTP = "R.C.R";
							tblINAUT.setValueAt(hstCODDS.get(L_strSTRTP).toString(),i,TBL_STRTP);
							tblINAUT.setValueAt(L_strINDTP,i,TBL_DOCTP);
							tblINAUT.setValueAt(M_rstRSSET.getString("IN_INDNO"),i,TBL_DOCNO);
							tblINAUT.setValueAt(hstCODDS.get(M_rstRSSET.getString("IN_DPTCD")).toString(),i,TBL_DPTCD);
							tblINAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("L_APRVL"),"0.00"),i,TBL_APRVL);
							tblINAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_FRWBY"),""),i,TB1_PREBY);
							if(L_strTEMP.equals("3"))
							{
								L_strTEMP = "Held For Discussion";
								tblINAUT.setValueAt(L_strTEMP,i,TBL_APRFL);
							}
							M_strSQLQRY ="SELECT RM_REMTP,RM_REMDS FROM MM_RMMST WHERE RM_STRTP ='"+L_strSTRTP +"'"
							 +" AND RM_TRNTP ='IN' AND RM_REMTP in('IND','OTH') AND RM_DOCNO ='"+L_strINDNO +"' AND isnull(RM_STSFL,'') <>'X'";										
							L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
							String L_strREMDS ="";
							if(L_rstRSSET !=null)
							{
								while(L_rstRSSET.next())
								{
									L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
									if(nvlSTRVL(L_rstRSSET.getString("RM_REMTP"),"").equals("IND"))
										tblINAUT.setValueAt(L_strREMDS,i,TB1_REMDS);
									else if(nvlSTRVL(L_rstRSSET.getString("RM_REMTP"),"").equals("OTH"))
										tblINAUT.setValueAt(L_strREMDS,i,TB1_COMDS);
								}
								L_rstRSSET.close();
							}
							i++;
						}
						M_rstRSSET.close();
					}
				}
				else if(jtpTRNDT.getSelectedIndex() ==intLTPTF)
				{
				    genPTFNO();
				    txtPTFNO.requestFocus();
				}
			}
			/*if(M_objSOURC == tblPTFDT.cmpEDITR[TB4_STATS])
			{
				
			}*/
       	}
		catch(Exception L_E)
		{
			setMSG(L_E,"Child.ActionPerformed..");	
		}
	}
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			super.mouseReleased(L_ME);
			if((M_objSOURC == tblGPAPR)&&(tblGPAPR.getSelectedColumn() == TBL_DSPFL))
			{
				setMSG("",'N');
				getITDTL(tblGPAPR,tblITDT1);
			}
			else if((M_objSOURC == tblGPAUT)&&(tblGPAUT.getSelectedColumn() == TBL_DSPFL))
			{
				setMSG("",'N');
				getITDTL(tblGPAUT,tblITDT2);
			}
			else if((M_objSOURC == tblINAUT)&&(tblINAUT.getSelectedColumn() == TBL_DSPFL))
			{
				setMSG("",'N');
				getINDTL();
			}
			else if((M_objSOURC == tblTSTDT)&&(tblTSTDT.getSelectedColumn() == TB_CMPVL))
			{
				setMSG("",'N');
				getGRBARR(tblTSTDT.getSelectedRow());
			}
			else if((M_objSOURC == tblPTFDT)&&(tblTSTDT.getSelectedColumn() == TB4_STATS))
			{
				LM_CLSFL = AR_CLSFL[tblPTFDT.getSelectedRow()].toString().trim();
				if(!LM_CLSFL.equals("6"))
				{
					if(!(LM_CLSFL.equals("1")||LM_CLSFL.equals("2")||LM_CLSFL.equals("3")))
					{
						if(!tblPTFDT.getValueAt(tblPTFDT.getSelectedRow(),TB4_CPRCD).toString().trim().equals(""))
						{
							LM_COUNT = getCLKREC();	
    						if(LM_COUNT > LM_DSPLOT)
    							modLINCNT();  //module for counting No. of Lines
							if(LM_COUNT >= LM_DSPLOT)
							    JOptionPane.showMessageDialog(this,"Please save the current PTF & generate new PTF.","PTF Entry Status",JOptionPane.INFORMATION_MESSAGE);		
							calCLSQT();		
    						if(strPTFCT.equals("02"))
    							tblPTFDT.setValueAt("Rcl. from "+getPRDDS(tblPTFDT.getValueAt(tblPTFDT.getSelectedRow(),TB4_OPRCD).toString()),tblPTFDT.getSelectedRow(),TB4_REMDS);
    					}
					}
					else
						JOptionPane.showMessageDialog(this,"Please Contact QC Dept. to make Lot No. Provisionally Classified.","Grade Entry Status",JOptionPane.INFORMATION_MESSAGE);			
				}
				else
					JOptionPane.showMessageDialog(this,"Please make Provisional & Classified grade identical.","Grade Entry Status",JOptionPane.INFORMATION_MESSAGE);		
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"mouseReleased");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		int L_intSELRW =0;
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
	  	   if(M_objSOURC == tblTSTDT.cmpEDITR[TB_CMPVL])
	       {
			   if(!flgOFFGRD)
			     exeRNGCHK(tblTSTDT.getSelectedRow());
	       }
	       if(M_objSOURC == tblTSTRC.cmpEDITR[TB3_CMPVL])
	       {
				vldRNG(tblTSTRC.getSelectedRow());
			   	exeRCLCHK();
				//exeENBCLS();	
	        }	 
	       if(M_objSOURC == txtREMDS)
	            txtREMDS.transferFocus();
	       if(M_objSOURC == cmbRCPRD)
	           txtRCLOT.requestFocus();
	       if(M_objSOURC == txtRCLOT)
	       {
	          if(txtRCLOT.getText().length() !=8)
              {
                setMSG("Invalid Lot no..",'E');
                flgLTVLD = false;
              }
	          else 
	             txtNWPRD.requestFocus();
	       }
	       if(M_objSOURC == txtNWPRD)
	       {   
    	        if(txtNWPRD.getText().length() !=10)
                {
                    setMSG("Invalid Product Code..",'E');
                    txtNWGRD.setText("");
                }
	       }
	       if(M_objSOURC == txtPTFNO)
	       {
	          txtPTFDT.requestFocus();
	          setMSG("Please Enter PTF Date ..",'N');
	       }
	       if(M_objSOURC == txtPTFDT)
	       {
	          txtPTFCT.requestFocus();
	          setMSG("Please Select the PTF Category ..",'N');
	       }
	       
	    }
		else if (L_KE.getKeyCode()== L_KE.VK_UP)
		{
			if(M_objSOURC == tblTSTDT.cmpEDITR[TB_CMPVL])
			{
			    L_intSELRW = tblTSTDT.getSelectedRow();
				if( L_intSELRW >0)
				getGRBARR(L_intSELRW-1);
				if(!flgOFFGRD)
			        exeRNGCHK(tblTSTDT.getSelectedRow());
			}
			if(M_objSOURC == tblTSTRC.cmpEDITR[TB3_CMPVL])
			{
			    vldRNG(tblTSTRC.getSelectedRow());
		       	exeRCLCHK();
			}
		}
		else if (L_KE.getKeyCode()== L_KE.VK_DOWN)
		{
			if(M_objSOURC == tblTSTDT.cmpEDITR[TB_CMPVL])
			{
			    L_intSELRW = tblTSTDT.getSelectedRow();
				if(L_intSELRW < intROWCT-1)
					getGRBARR(L_intSELRW+1);
				if(!flgOFFGRD)
			         exeRNGCHK(tblTSTDT.getSelectedRow());
			}
			if(M_objSOURC == tblTSTRC.cmpEDITR[TB3_CMPVL])
			{
			    vldRNG(tblTSTRC.getSelectedRow());
		       	exeRCLCHK();
			}
		}
		else if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtLOTNO)
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
			    cl_dat.M_flgHELPFL_pbst = true;
			    if(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strFNLCL_fn))
			        chrCLSFL ='F';
	            else if(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strPRVCL_fn))
			        chrCLSFL ='P';
			    		        
        	    M_strSQLQRY = "select LT_LOTNO,LT_RCLNO,LT_PSTDT,PR_PRDDS,CMT_SHRDS,LT_CLSFL,isnull(LT_BAGQT,0) LT_BAGQT from PR_LTMST,CO_PRMST,co_cdtrn where LT_PRDTP = '";
                M_strSQLQRY += cmbPRDTP.getSelectedItem().toString().substring(0,2) + "' and LT_TPRCD = PR_PRDCD and  " ;
				if(chrCLSFL == 'P')
				M_strSQLQRY += "((LT_CLSFL in('1','3')) OR (LT_CLSFL ='9' and LT_PPRCD is null))";
				else
				M_strSQLQRY += " LT_CLSFL in('0','1','3','4') and SUBSTRING(lt_prdcd,1,3) <> '512'" ;
				M_strSQLQRY += " AND LT_RCLNO ='"+strINTRCL_fn +"'";
				M_strSQLQRY += " AND cmt_cgmtp ='SYS' and cmt_cgstp = 'QCXXCLS' and LT_CLSFL = cmt_codcd ";
	         	if(txtLOTNO.getText().trim().length() >0)
					M_strSQLQRY += " and LT_LOTNO like '" + txtLOTNO.getText().trim() + "%'  AND LT_STSFL <> 'X' order by LT_CLSFL desc,LT_LOTNO desc";
				else	
					M_strSQLQRY += " order by LT_CLSFL desc,LT_LOTNO desc";
	            M_strHLPFLD = "txtLOTNO";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot Number","Rcl. No.","Lot Start Date  ","  Grade ","Status","Bagged"},6,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
    		else if(L_KE.getSource().equals(txtRCLNO))
			{
				cl_dat.M_flgHELPFL_pbst = true;
			    M_strSQLQRY = " ";
			    M_strSQLQRY = "select LT_RCLNO,PR_PRDDS from PR_LTMST,CO_PRMST where LT_PRDCD = PR_PRDCD AND LT_LOTNO = ";
			    M_strSQLQRY +="'"+txtLOTNO.getText().trim()+"'";
				M_strSQLQRY +=" AND LT_PRDTP ='"+cmbPRDTP.getSelectedItem().toString().substring(0,2).trim()+"'";
			    M_strHLPFLD = "txtRCLNO";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Re-classification No.","Grade "},2,"CT");
			}
		    else if(L_KE.getSource().equals(txtCPRCD))
			{
				cl_dat.M_flgHELPFL_pbst = true;
               // M_strSQLQRY = "select distinct PR_PRDCD,PR_PRDDS from CO_PRMST where (SUBSTRING(PR_PRDCD,1,4) = '";
               // M_strSQLQRY = M_strSQLQRY + txtTPRCD.getText().trim().substring(0,4) + "' or SUBSTRING(PR_PRDCD,5,2) = '99') and PR_STSFL <> 'X'  order by PR_PRDCD";
				M_strSQLQRY = "select distinct PR_PRDCD,PR_PRDDS from CO_PRMST where (SUBSTRING(PR_PRDCD,1,4) = '";
                M_strSQLQRY = M_strSQLQRY + txtTPRCD.getText().trim().substring(0,4) + "' or SUBSTRING(PR_PRDCD,3,2) = '95'" +" or SUBSTRING(PR_PRDCD,5,2) = '95') and PR_STSFL <> 'X'  order by PR_PRDCD";
              
                M_strHLPFLD = "txtCPRCD";
           		cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"CT");
			}
            else if(L_KE.getSource().equals(txtPPRCD))
			{
				cl_dat.M_flgHELPFL_pbst = true;     
                M_strSQLQRY = "select distinct PR_PRDCD,PR_PRDDS from CO_PRMST where (SUBSTRING(PR_PRDCD,1,4) = '";
                //M_strSQLQRY = M_strSQLQRY + txtTPRCD.getText().trim().substring(0,4) + "' or SUBSTRING(PR_PRDCD,5,2) = '99') and PR_STSFL <> 'X'  order by PR_PRDCD";
				M_strSQLQRY = M_strSQLQRY + txtTPRCD.getText().trim().substring(0,4) + "' or SUBSTRING(PR_PRDCD,3,2) = '95'" +" or SUBSTRING(PR_PRDCD,5,2) = '95') and PR_STSFL <> 'X'  order by PR_PRDCD";
                M_strHLPFLD = "txtPPRCD";
          		cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"CT");
			}
		     // Help of Reclassification Screen
		    else if(M_objSOURC == txtRCLOT)
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT distinct ST_LOTNO,PR_PRDDS,sum(st_stkqt) from fg_STMST,CO_PRMST where SUBSTRING(ST_LOTNO,1,3) not in "+strSCPLT_fn;
				M_strSQLQRY += " and ST_PRDTP = '"+cmbRCPRD.getSelectedItem().toString().substring(0,2)+"'";
				M_strSQLQRY += " AND ST_CPRCD = PR_PRDCD ";
				if(txtRCLOT.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOTNO like '"+txtRCLOT.getText().trim() +"%'";
				M_strSQLQRY += " AND ST_STKQT > 0 ";
				M_strSQLQRY +="group by ST_LOTNO,PR_PRDDS order by ST_LOTNO";
				M_strHLPFLD = "txtRCLOT";
              	cl_hlp(M_strSQLQRY,1,1,new String[]{" Lot Number ","Grade ","Stock Quantity"},3,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtNWPRD)
			{
		        M_strSQLQRY = "select distinct PR_PRDCD,PR_PRDDS from CO_PRMST where SUBSTRING(PR_PRDCD,1,2) = '";
                M_strSQLQRY += tblLOTRC.getValueAt(0,TB1_PRDCD).toString().trim().substring(0,2)+"' and PR_STSFL <> 'X'";
				if(txtNWPRD.getText().length() >0)
				{
				    M_strSQLQRY += " AND PR_PRDCD like '"+txtNWPRD.getText().trim()+"%'";
				}
				 M_strSQLQRY += " order by PR_PRDCD";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtNWPRD";
			  	cl_hlp(M_strSQLQRY,2,1,new String[]{" Product Code ","Grade  "},2,"CT");
			}	
			else if(M_objSOURC == txtPTFCT)
			{
				M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,5,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS'";
        		M_strSQLQRY += " and CMT_CGSTP = 'FGXXCAT' and SUBSTRING(CMT_CODCD,1,4)='PTF_' and cmt_modls like '%"+cl_dat.M_strUSRTP_pbst+"%'";
				//System.out.println(M_strSQLQRY);
        		cl_dat.M_flgHELPFL_pbst = true;
        		M_strHLPFLD = "txtPTFCT";
        		cl_hlp(M_strSQLQRY,1,2,new String[]{"PTF Category","DESC"},2,"CT");
	   	    }
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtLOTNO")
		{
		    txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);
		    txtRCLNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		//	txtRCLNO.requestFocus();
			/*if(LM_STRHLP !=null)
			{
				st = new StringTokenizer(LM_STRHLP,"|");
				if (st.hasMoreTokens()) {
					 st.nextToken();	
					 L_RCLNO = st.nextToken();
						txtRCLNO.setText(L_RCLNO);
					}	
				strPTFRCL = L_RCLNO.trim();
			}*/
		}
		else if(M_strHLPFLD == "txtRCLNO")
         {
             txtRCLNO.setText(cl_dat.M_strHLPSTR_pbst);
             txtRCLNO.requestFocus();
         }
         else if(M_strHLPFLD == "txtPPRCD")
         {
             txtPPRCD.setText(cl_dat.M_strHLPSTR_pbst);
             txtPPRCD.requestFocus();
         }
         else if(M_strHLPFLD == "txtCPRCD")
         {
             txtCPRCD.setText(cl_dat.M_strHLPSTR_pbst);
             txtCPRCD.requestFocus();
         }
         else if(M_strHLPFLD == "txtRCLOT")
         {
             txtRCLOT.setText(cl_dat.M_strHLPSTR_pbst);
             txtRCLOT.requestFocus();
         }
         else if(M_strHLPFLD == "txtNWPRD")
         {
             txtNWPRD.setText(cl_dat.M_strHLPSTR_pbst);
             txtNWPRD.requestFocus();
         }
         else if(M_strHLPFLD.equals("txtPTFCT"))
		 {
			txtPTFCT.setText(cl_dat.M_strHLPSTR_pbst);
		}
   	}
void setENBL(boolean L_flgSTAT)
{
    super.setENBL(L_flgSTAT);
    txtBAGQT.setEnabled(false);
    txtTSTBY.setEnabled(false);
    txtTSTDT.setEnabled(false);
    txtTPRCD.setEnabled(false);
    txtTPRDS.setEnabled(false);
    txtPCLBY.setEnabled(false);
    txtPCLTM.setEnabled(false);
    txtPPRDS.setEnabled(false);
    txtCLSBY.setEnabled(false);
    txtCLSTM.setEnabled(false);
    txtCPRDS.setEnabled(false);
    txtIPRDS.setEnabled(false);
    btnRLS.setVisible(false);
    txtGRBTS.setEnabled(false);
    txtADDTS.setEnabled(false);
    tblTSTDT.cmpEDITR[TB_QPRCD].setEnabled(false);
    tblTSTDT.cmpEDITR[TB_UOMDS].setEnabled(false);
    tblTSTDT.cmpEDITR[TB_SPEC].setEnabled(false);
    tblTSTDT.cmpEDITR[TB_QPRDS].setEnabled(false);
    tblTSTDT.cmpEDITR[TB_FLAG].setEnabled(false);
    tblTSTDT.cmpEDITR[TB_CHKFL].setEnabled(false);
    tblTSTDT.cmpEDITR[TB_BAGVL].setEnabled(false);
    txtNWGRD.setEnabled(false);
    txtNWDAT.setEnabled(false);
    txtRBGQT.setEnabled(false);
    tblLOTRC.setEnabled(false);
    tblSTKDT.setEnabled(false);
    tblTSTRC.cmpEDITR[TB3_CHKFL].setEnabled(false);
    tblTSTRC.cmpEDITR[TB3_QPRDS].setEnabled(false);
    tblTSTRC.cmpEDITR[TB3_QPRCD].setEnabled(false);
    tblTSTRC.cmpEDITR[TB3_UOMDS].setEnabled(false);
    tblTSTRC.cmpEDITR[TB3_SPEC].setEnabled(false);
    tblTSTRC.cmpEDITR[TB3_FLAG].setEnabled(false);
    // PTF ENTRY
    txtPTFQT.setEnabled(false);
    txtBALQT.setEnabled(false);
    tblPTFDT.setEnabled(false);
    tblPTFDT.cmpEDITR[TB4_STATS].setEnabled(true);
    tblPTFDT.cmpEDITR[TB4_REMDS].setEnabled(true);
    //txtPTFDT.setEnabled(false);
    
}
private void exeDSBSCR()
{
    txtBAGQT.setEnabled(false);
    txtTSTBY.setEnabled(false);
    txtTSTDT.setEnabled(false);
    txtTPRCD.setEnabled(false);
    txtTPRDS.setEnabled(false);
    txtPCLBY.setEnabled(false);
    txtPCLTM.setEnabled(false);
    txtPPRCD.setEnabled(false);
    txtPPRDS.setEnabled(false);
    txtCLSBY.setEnabled(false);
    txtCLSTM.setEnabled(false);
    txtCPRCD.setEnabled(false);
    txtCPRDS.setEnabled(false);
    txtREMDS.setEnabled(false);
    txtIPRDS.setEnabled(false);
    cl_dat.M_btnSAVE_pbst.setEnabled(false);
    tblTSTDT.setEnabled(false);
}
/**
    This method fetchs Lot and specification details from PR_LTMST,CO_PRMST and CO_QPMST
*/
private boolean getLOTDET()
{
	M_strSQLQRY= "";
	ResultSet L_rstRSSET;
	String L_strCLSFL,L_strPPRCD,L_strCPRCD,L_strTPRCD,L_strTPRDS,L_strBAGQT,L_strPRSTS;
	String L_strPCLBY,L_strPCLTM="",L_strIPRDS,L_strCPRDS ="",L_strCLSBY,L_strCLSTM="";
	flgOFFGRD = false;
	strPTAFL ="";
	try
	{
	    strMSG ="";
	  	M_strSQLQRY = "SELECT LT_LOTNO,LT_RCLNO,LT_TPRCD,LT_CLSFL,LT_BAGQT,LT_PCLBY,"
         +" LT_PPRCD,LT_PCLTM,LT_CPRCD,LT_CLSTM,LT_CLSBY,LT_IPRDS,LT_PTAFL,PR_PRDDS,"
         +" PR_STSFL,QP_QPRCD,QP_ORDBY FROM PR_LTMST,CO_QPMST,CO_PRMST WHERE"
         +" LT_PRDCD = PR_PRDCD and LT_PRDCD = QP_PRDCD and QP_QCATP = "+ "'"+cmbPRDTP.getSelectedItem().toString().substring(0,2) + "'"
         +" and QP_SRLNO = " + "'" + strDFLSR_fn + "'"
         +" and QP_ENDDT is null and QP_ORDBY is not null "
         +" and LT_PRDTP = " + "'" + cmbPRDTP.getSelectedItem().toString().substring(0,2) + "'"
         +" and LT_LOTNO = " + "'" + txtLOTNO.getText().trim() + "'"
         +" and LT_RCLNO = " + "'" + txtRCLNO.getText().trim() + "'"
		             		             +" order by QP_ORDBY";
		if(tblTSTDT.isEditing())
		    tblTSTDT.getCellEditor().stopCellEditing();
		tblTSTDT.setRowSelectionInterval(0,0);
		tblTSTDT.setColumnSelectionInterval(0,0);
		
		L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(L_rstRSSET == null)
		{
			setMSG("Lot No. Not Found..",'E');
			return false;
		}
	    if(!L_rstRSSET.next())
	    {
	   		setMSG("Data Not Found..",'E');
	        return false;
	    }
	  	L_strCLSFL = nvlSTRVL(L_rstRSSET.getString("LT_CLSFL"),"");
		L_strPPRCD = nvlSTRVL(L_rstRSSET.getString("LT_PPRCD"),"");
		L_strCPRCD = nvlSTRVL(L_rstRSSET.getString("LT_CPRCD"),"");	
		L_strTPRCD = nvlSTRVL(L_rstRSSET.getString("LT_TPRCD"),"");
		L_strIPRDS = nvlSTRVL(L_rstRSSET.getString("LT_IPRDS"),"");
		strPTAFL =   nvlSTRVL(L_rstRSSET.getString("LT_PTAFL"),"");
		L_strPRSTS = nvlSTRVL(L_rstRSSET.getString("PR_STSFL"),"");
		strPTARM ="";
		btnRLS.setVisible(false); 
		if(strPTAFL.equals("Y"))
		{
    		M_strSQLQRY = "SELECT RM_REMDS FROM QC_RMMST WHERE RM_QCATP ='01' AND RM_TSTTP ='LOT' AND RM_TSTNO ='"
    		            + txtLOTNO.getText().trim() +"'";
    		M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
    		if(M_rstRSSET !=null)
    		{
    		    if(M_rstRSSET.next())
    		        strPTARM = M_rstRSSET.getString("RM_REMDS");
    		    M_rstRSSET.close();    
    		}
    		lblAUTCLS.setText("Plant Abnormaility : "+strPTARM);  
  		}
		else
		{
		    lblAUTCLS.setText("");
		}
		if(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strFNLCL_fn))
		{
		    strCLSFL = strFNLST_fn;
		    chrCLSFL ='F';
		}
		else if(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strPRVCL_fn))
		{
		    strCLSFL = strPRVST_fn;
		    chrCLSFL ='P';
		}
		if((L_strCLSFL.equals(strPRVST_fn)) &&(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strPRVCL_fn)))
		{
		    // CLSFL is 4 and provisional classification tab is selected , query to be done 
		    strMSG ="Provisional Classification is done as "+ getPRDDS(L_strPPRCD);
		    setMSG(strMSG,'E');	
        	exeDSBSCR();
        	setCursor(cl_dat.M_curDFSTS_pbst);
        	cl_dat.M_btnSAVE_pbst.setEnabled(false);
        	chrCLSFL ='Q';
        	btnRLS.setVisible(true); 
         	strRLSFL = "3";
			strRLSPRD = L_strTPRCD;
        	strCLSFL =strPRVST_fn;
        	txtLOTNO.requestFocus();
        	
		}
		else if((L_strCLSFL.equals(strFNLST_fn)) &&(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strPRVCL_fn)))
		{
		    // CLSFL is 9 and provisional classification tab is selected 
		    strCLSFL =strFNLST_fn;
		    if(L_strCPRCD.trim().equals(L_strPPRCD.trim()))
			{
				L_strCPRDS = getPRDDS(L_strCPRCD);	
				strMSG = "Final and Provisional Classifications are done as "+ L_strCPRDS;
				setMSG(strMSG,'E');	
				flgFNLOVR = false;
				chrCLSFL ='Q';
				setCursor(cl_dat.M_curDFSTS_pbst);
				exeDSBSCR();
            	cl_dat.M_btnSAVE_pbst.setEnabled(false);
           	 	txtLOTNO.requestFocus();
        		//return false;
			}
			else
			{
				flgFNLOVR = true;			// Final over , Prov .not over
				chrCLSFL ='P';
			}
		}
		else if((L_strCLSFL.equals(strFNLST_fn)) &&(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strFNLCL_fn)))
		{
		    strCLSFL =strFNLST_fn;
		    chrCLSFL ='Q';
		    strRLSFL ="";
			strRLSPRD ="";
			L_strCPRDS = getPRDDS(L_strCPRCD);	
			if(txtRCLNO.getText().trim().equals("00"))
			    strMSG = "Final Classification is done as "+ L_strCPRDS;
			else
			    strMSG = "Re-Classification "+txtRCLNO.getText().trim()+" is done as "+ L_strCPRDS;
			setMSG(strMSG,'E');
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			exeDSBSCR();
			// Functionality for release button 	
			M_strSQLQRY ="Select count(*) L_CNT from FG_PTFRF where "; 
			M_strSQLQRY += " PTF_LOTNO = '"+txtLOTNO.getText().trim()+"' AND PTF_PRDTP  ='"+cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'"; 
		//	System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
    			{
    			    if(M_rstRSSET.getInt("L_CNT") >0)
    			    {
    				    btnRLS.setVisible(false);
    				   // setMSG("PTF has been created, Lot can not be released ..",'E');
    			    }
    			    else if(L_strCLSFL.equals("9"))
    				{    
                        btnRLS.setVisible(true);  
                    }
    				else
    				    btnRLS.setVisible(false);    
    			}
    			else if(L_strCLSFL.equals("9"))
                {
    				btnRLS.setVisible(true);
                }
    			else
    			    btnRLS.setVisible(false);  	
			}
			if(L_strPPRCD.trim().length() == 0)
			{
				strRLSFL = "3";
				strRLSPRD = L_strTPRCD;
			}
			else
			{
				strRLSFL = "4";
				strRLSPRD = L_strPPRCD;
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
	    	txtLOTNO.requestFocus();
      	}
		L_strTPRDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
		L_strBAGQT = nvlSTRVL(L_rstRSSET.getString("LT_BAGQT"),"");
		L_strPRSTS = nvlSTRVL(L_rstRSSET.getString("PR_STSFL"),"");
		L_strPCLBY = nvlSTRVL(L_rstRSSET.getString("LT_PCLBY"),"");
		tmsTEMP = L_rstRSSET.getTimestamp("LT_PCLTM");
		if(tmsTEMP !=null)
		    L_strPCLTM = M_fmtLCDTM.format(tmsTEMP);
		L_strCLSBY = nvlSTRVL(L_rstRSSET.getString("LT_CLSBY"),"");
		tmsTEMP = L_rstRSSET.getTimestamp("LT_CLSTM");
		if(tmsTEMP !=null)
		    L_strCLSTM = M_fmtLCDTM.format(tmsTEMP);
		if(strPRSTS == null)
			strPRSTS = L_strPRSTS;
		else if(strPRSTS.trim().equals(""));
			strPRSTS = L_strPRSTS;
		if(strPRSTS.equals("4"))
			flgOFFGRD = true;
		else
			flgOFFGRD = false;
	  	
	  	txtTPRCD.setText(L_strTPRCD);
	    txtTPRDS.setText(getPRDDS(txtTPRCD.getText().trim()));
		txtIPRDS.setText(L_strIPRDS);
        txtBAGQT.setText(L_strBAGQT);
		txtPPRCD.setText(L_strPPRCD);
		txtCPRCD.setText(L_strCPRCD);
		if(!txtPPRCD.getText().trim().equals(""))
		{
			txtPPRDS.setText(getPRDDS(txtPPRCD.getText().trim()));
		    txtPCLBY.setText(L_strPCLBY);
			txtPCLTM.setText(L_strPCLTM);
		}
       if(!txtCPRCD.getText().trim().equals(""))
	   {
		   txtCPRDS.setText(getPRDDS(txtCPRCD.getText().trim()));
	       txtCLSBY.setText(L_strCLSBY);
		   txtCLSTM.setText(L_strCLSTM);
       }
  	   if(chrCLSFL =='F')
	   {
		   if(txtPPRCD.getText().trim().length() >0)
		   {
		   		txtCPRCD.setText(txtPPRCD.getText().trim());
		   		txtCPRDS.setText(txtPPRDS.getText().trim());  
		   }
		   else
		   {
		   		txtCPRCD.setText(txtTPRCD.getText().trim());
		   		txtCPRDS.setText(txtTPRDS.getText().trim());  
		   }
		   txtCLSBY.setText(cl_dat.M_strUSRCD_pbst);  
		   txtCLSTM.setText(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText());
		   //btnCLS.setEnabled(true);
	   }
	  	strCHKCD ="";
		if (!txtCPRCD.getText().trim().equals(""))
		    strCHKCD = txtCPRCD.getText().trim();
		else if (!txtPPRCD.getText().trim().equals(""))
		    strCHKCD = txtPPRCD.getText().trim();
		else
		    strCHKCD = txtTPRCD.getText().trim();	
		vtrQPRCD.removeAllElements();	
		vtrQPRCD.add(nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),""));
		while(L_rstRSSET.next())
		{
			vtrQPRCD.add(nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),""));
		}
		flgTSTFL = getTSTDET(cmbPRDTP.getSelectedItem().toString().substring(0,2));
		if((flgTSTFL==false)&&(chrCLSFL != 'Q')&&(!flgOFFGRD))
		{
			setMSG("Test Details not found",'E');
		    cl_dat.M_btnSAVE_pbst.setEnabled(false);
	        setCursor(cl_dat.M_curDFSTS_pbst);
		}
		else
		{
	  		getALLREC();
	  		if(chrCLSFL =='Q')
	  		{    
	  		    setMSG(strMSG,'E');	
	  		    txtLOTNO.requestFocus();
	  		}
	  		setCursor(cl_dat.M_curDFSTS_pbst);
		}
	    if(chrCLSFL =='P')
	    {
	        txtCPRCD.setEnabled(false);
	        txtPPRCD.setEnabled(true);
	    }
	   else if(chrCLSFL =='F')
	    {
	        txtCPRCD.setEnabled(true);
	        txtPPRCD.setEnabled(false);
	    }
	    else
	    {
	        txtCPRCD.setEnabled(false);
	        txtPPRCD.setEnabled(false);
	        txtREMDS.setEnabled(false);
	    }
	}
    catch(Exception L_E)
	{
		setMSG(L_E,"getLOTDET");
		return false;
	}
	return true;
}
/**
    This method fetches the Test details of Lot from QC_PSMST
*/
private boolean getTSTDET(String P_strQCATP)
{
    boolean L_flgRETFL = false;
	try
	{
        Timestamp L_tmsTEMP ;
    	flgTSTOK = true;
    	String L_strCMPVL="",L_strBAGVL="",L_strQPRCD="";
    	strTSTNO ="";
    	M_strSQLQRY = "Select * from QC_PSMST where PS_QCATP = '" + P_strQCATP + "' and PS_LOTNO = '" ;
		M_strSQLQRY += txtLOTNO.getText().trim() + "'";
		M_strSQLQRY += " AND PS_RCLNO = '"+txtRCLNO.getText().trim() + "'";
		M_strSQLQRY += " and PS_TSTTP in ('0103','0104')";
		M_strSQLQRY += " AND isnull(PS_STSFL,'') <> 'X'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		hstCMPVL = new Hashtable();
		hstBAGVL = new Hashtable();
		hstCMPVL.clear();
		hstBAGVL.clear();
		if(M_rstRSSET ==null)
		{
		    setMSG("Test Details not found ..",'E');
		    return false;
		}
    	while (M_rstRSSET.next())
    	{
    	    if(M_rstRSSET.getString("PS_TSTTP").trim().equals(strCMPTP_fn))
    	    {
    		      L_flgRETFL = true;
    			  L_tmsTEMP = M_rstRSSET.getTimestamp("PS_TSTDT");
    			  txtTSTDT.setText(M_fmtLCDTM.format(L_tmsTEMP));
    	          strTSTNO = M_rstRSSET.getString("PS_TSTNO");
    	          cl_dat.M_txtUSER_pbst.setText(M_rstRSSET.getString("PS_LUSBY"));
    	          txtTSTBY.setText(M_rstRSSET.getString("PS_TSTBY"));
    			  for(int i =0;i<vtrQPRCD.size();i++)
    			  {
    				  L_strQPRCD = vtrQPRCD.elementAt(i).toString().trim();
    				  L_strCMPVL = M_rstRSSET.getString("PS_"+L_strQPRCD+"VL");
    				  if(L_strCMPVL !=null)
    					  hstCMPVL.put(L_strQPRCD,L_strCMPVL);
    				  else
    				  {
    					  flgTSTOK = false;
    					  hstCMPVL.put(L_strQPRCD,"0");
    				  }
    			  }
    	      }
    		  else
    		  {
    			  for(int i = 0;i<vtrQPRCD.size();i++)
    			  {
    				  L_strQPRCD = vtrQPRCD.elementAt(i).toString().trim();
    				  L_strBAGVL = M_rstRSSET.getString("PS_"+L_strQPRCD+"VL");
    				  if(L_strBAGVL !=null)
    					  hstBAGVL.put(L_strQPRCD,L_strBAGVL);
    			  }
    		  }
    	    }
    	    if(M_rstRSSET !=null)
			    M_rstRSSET.close();
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getTSTDET");
	}
	if(!L_flgRETFL)
	    setMSG("Test Details not found ..",'E');
	return L_flgRETFL;
}
private String getPRDDS(String P_strPRDCD)
{
	try
	{
	    ResultSet L_rstRSPRD;
		M_strSQLQRY = "SELECT * FROM CO_PRMST WHERE PR_PRDCD = "+ "'"+P_strPRDCD + "'";
		L_rstRSPRD = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(L_rstRSPRD !=null)
		{
			if(L_rstRSPRD.next())
			{
				return L_rstRSPRD.getString("PR_PRDDS");
			}
			L_rstRSPRD.close();
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getPRDDS");
	}
	return "";
}
private void exeINTGRB()
{
    int i,j;
    for (i = 0;i < 50; i ++)
    for (j = 0; j < 3; j ++)
	{
		staGRBDT[i][j] = "";
	}
}
private void getALLREC()
{
    try
    {
	   int L_intCNT =0;
	   String L_strCHKCD ="";
	   String L_strCMPFL;
	   hstHSTST = new Hashtable();
	   tblTSTDT.clrTABLE();
       exeINTGRB();
       M_strSQLQRY = " ";
	   if (!txtCPRCD.getText().trim().equals(""))
	       L_strCHKCD = txtCPRCD.getText().trim();
	   else if (!txtPPRCD.getText().trim().equals(""))
	       L_strCHKCD = txtPPRCD.getText().trim();
	   else
	       L_strCHKCD = txtTPRCD.getText().trim();	
	   if((flgFNLOVR)&& (chrCLSFL =='P'))   // ADDED
	   {
	  		if(!txtPPRCD.getText().trim().equals(txtCPRCD.getText().trim()))
			{
	  			setMSG("Lot Finally classified,Prov. grade should match the Final grade..",'E');
	  			if(txtPPRCD.getText().length()> 0)
	  			    txtPPRCD.requestFocus();
	  			//else    
	  			  //  txtREMDS.requestFocus();
	  		}
	   }
	   M_strSQLQRY = "select LT_LOTNO,LT_PRDCD,QP_QPRCD,QP_QPRDS,QP_UOMDS,QP_ORDBY,QP_NPFVL,QP_NPTVL,QP_CMPFL";
	   M_strSQLQRY +=" from PR_LTMST,CO_QPMST where LT_PRDTP = '";
       M_strSQLQRY += cmbPRDTP.getSelectedItem().toString().substring(0,2)+"'";
	   M_strSQLQRY += " and LT_LOTNO = '" + txtLOTNO.getText().trim() + "'";
	   M_strSQLQRY += " and LT_RCLNO = '" + txtRCLNO.getText().trim() + "'";
	   M_strSQLQRY += "and QP_QCATP = '" + cmbPRDTP.getSelectedItem().toString().substring(0,2) + "' and QP_TSTTP = '0103'";
	   M_strSQLQRY += " and QP_PRDCD ='"+ L_strCHKCD.trim()+"'";
	   M_strSQLQRY += " and QP_SRLNO ='"+ strDFLSR_fn.trim()+"'";
	   M_strSQLQRY += " and QP_ORDBY is not null and qp_enddt is null order by QP_ORDBY";			 
	   M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
       L_intCNT =0;
	   intQPRCT = 0;
	   flgOUTRG = false;
	   if(M_rstRSSET !=null)
	   {
			while(M_rstRSSET.next())
			{
	  			staCMPFL[L_intCNT] = "";
        		fltNPFVL=0;
        		fltNPTVL=0;
        		fltQPRVL=0;
                strNPFDS=" ";
        		strNPTDS = " ";
        		strQPRDS = " ";
        		L_QPRCD = nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),"");
        		if(L_intCNT < vtrQPRCD.size())
    				vtrQPRCD.setElementAt(L_QPRCD.trim(),L_intCNT);
    			else
    				vtrQPRCD.addElement(L_QPRCD.trim());
    			tblTSTDT.setValueAt(L_QPRCD,L_intCNT,TB_QPRCD);
    			tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRDS"),""),L_intCNT,TB_QPRDS);
    			tblTSTDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_UOMDS"),""),L_intCNT,TB_UOMDS);
              	try
    		  	{
		  			  fltNPFVL = M_rstRSSET.getFloat("QP_NPFVL");
		  	          strNPFDS = "" + fltNPFVL;
    		  	}
		  	    catch (Exception L_E)
		  	    {
		  	          fltNPFVL = 0;
		  	          strNPFDS = " ";
		  	    }
    		  	try
    		  	{
    		  	      fltNPTVL = M_rstRSSET.getFloat("QP_NPTVL");
    		  	      strNPTDS = "" + fltNPTVL;
    		  	}
    		  	catch (Exception L_E)
    		  	{
    		  	      fltNPTVL = 0;
    		  	      strNPTDS = " ";
    		  	}
    		  	String L_strRNGVL = " " + strNPFDS + " - " + strNPTDS;
    		  	tblTSTDT.setValueAt(nvlSTRVL(L_strRNGVL,""),L_intCNT,TB_SPEC);  
    		  	L_CMPVL ="";
    		  	L_BAGVL ="";
    		  	if(hstCMPVL !=null)
    		  	{
    		  		L_CMPVL = (String)hstCMPVL.get(L_QPRCD);
    		  		if(L_CMPVL != null)
    					fltQPRVL = Float.valueOf(L_CMPVL).floatValue();
    				//// on 01/09/2003
    				if(strPRSTS.equals("4"))
    				    flgOFFGRD = true;
    				else
    				    flgOFFGRD = false;
    				///
    				if(flgOFFGRD)
    					tblTSTDT.setValueAt(nvlSTRVL(L_CMPVL,"0"),L_intCNT,TB_CMPVL);
    				else
    					tblTSTDT.setValueAt(L_CMPVL,L_intCNT,TB_CMPVL);
    		  	}
    		  	if(hstBAGVL !=null)
    		  	{
    		  		L_BAGVL = (String)hstBAGVL.get(L_QPRCD);
    		  		tblTSTDT.setValueAt(L_BAGVL,L_intCNT,TB_BAGVL);
    		
    		  	}
    		  	L_strCMPFL = nvlSTRVL(M_rstRSSET.getString("QP_CMPFL"),"");
    		  	if (L_strCMPFL.equals("Y"))
    		  	{
    		  			staCMPFL[L_intCNT] = "Y";
    		  	}
    		  		intQPRCT += 1;
       			L_intCNT+=1;
			}
			M_rstRSSET.close();
	   }
	  ///////////// check it later
	   if(strPRSTS.equals("4"))
			flgOFFGRD = true;
	   else
			flgOFFGRD = false;
	   if(!flgOFFGRD)
	   {
			for(int i=0;i<intQPRCT;i++)
			{
				exeRNGCHK(i);
			    if (tblTSTDT.getValueAt(i,TB_FLAG).toString().trim().equals("*"))
			    {
			          setMSG("Quality Parameter Value is not in the range specified ",'E');
			    }
			}
	   }
	   getRMK(txtLOTNO.getText().trim(),strTSTNO);
	  /* if (chrCLSFL == 'Q')
       {
           exeDSBSCR();
        }*/
    }
    catch(SQLException L_E)
    {
		setMSG(L_E,"getALLREC");
    }
	catch(NullPointerException L_NE)
    {
		setMSG(L_NE,"getALLREC");
    }
	getGRADDT();
	getGRBARR(0);
//	LM_CMP.setCellRenderer(new RowRenderer());
//	LM_FLG.setCellRenderer(new RowRenderer1());
 
 }
private void exeRNGCHK(int P_intROWNO) 
{
	boolean L_flgOUTRG = false;
	int L_intIDXVL = 0;
	String L_strFLAG;
    fltNPFVL = 0;
 	fltNPTVL = 0;
 	tblTSTDT.editCellAt(P_intROWNO,TB_CMPVL);
 	try
 	{
 	    // added on 30 april 2006
 	    setMSG(" ",'N');
	     if (tblTSTDT.getValueAt(P_intROWNO,TB_CMPVL)== null)
	    {
	       setMSG("Some of the test values are not available..",'E');
	       cl_dat.M_btnSAVE_pbst.setEnabled(false);
	       return; 
	    }// end added on 30 april 2006
 	   if (!tblTSTDT.getValueAt(P_intROWNO,TB_CMPVL).toString().trim().equals(""))
 	        fltQPRVL = Float.valueOf(tblTSTDT.getValueAt(P_intROWNO,TB_CMPVL).toString().trim()).floatValue();
 	   L_intIDXVL = tblTSTDT.getValueAt(P_intROWNO,TB_SPEC).toString().trim().indexOf("-");
 	   try
 	   {
 			fltNPFVL = Float.valueOf(tblTSTDT.getValueAt(P_intROWNO,TB_SPEC).toString().substring(0,L_intIDXVL - 1).trim()).floatValue();
 	   }
 	   catch(Exception L_E)
 	   {
 			fltNPFVL = 0;
 	   }
		try
		{
		 	fltNPTVL =Float.valueOf(tblTSTDT.getValueAt(P_intROWNO,TB_SPEC).toString().substring(L_intIDXVL + 2).trim()).floatValue(); 
		}
		catch(Exception L_E)
		{
		 	fltNPTVL = 0;
		}
		if (staCMPFL[P_intROWNO].trim().equals("Y"))
		{
		    if (tblTSTDT.getValueAt(P_intROWNO,TB_CMPVL).toString().trim().equals(""))
		    {
		       setMSG("Some of the test values are not available..",'E');
		       return; 
		    }
		    chkQPRNG(fltNPFVL,fltNPTVL,fltQPRVL,P_intROWNO,tblTSTDT,TB_FLAG);
		    L_flgOUTRG = false;
		 	for(int i=0;i<intROWCT;i++)
		 	{
		 		L_strFLAG = tblTSTDT.getValueAt(i,TB_FLAG).toString().trim();
		 		if(L_strFLAG.length() >0)
		 		{
		 			if(L_strFLAG.equals("*"))
		 			{
		 				L_flgOUTRG = true;			// Out of Range
		 			}
		 		}
		 	}
		 	if(!L_flgOUTRG)
		 	{
				if(!flgTSTOK)
					setMSG("Some of the test values are not available..",'E');
			    else
					setMSG("",'N');
				if(chrCLSFL !='Q')
		 		    cl_dat.M_btnSAVE_pbst.setEnabled(true);
		 	}
		 	else
		 		cl_dat.M_btnSAVE_pbst.setEnabled(false);
		}
		else // added on 30 april 2006
		{
		    if (tblTSTDT.getValueAt(P_intROWNO,TB_CMPVL)== null)
		    {
		       setMSG("Some of the test values are not available..",'E');
		       return; 
		    }
		    if (nvlSTRVL(tblTSTDT.getValueAt(P_intROWNO,TB_CMPVL).toString(),"").equals(""))
		    {
		       setMSG("Some of the test values are not available..",'E');
		       return; 
		    }
		}
		
	}
	catch(NumberFormatException L_NFE)
	{
	  setMSG("Invalid Value ...",'E');
	}
	catch(NullPointerException L_NE)
	{
	  setMSG(L_NE,"exeRNGCHK");
	}
}
/** This method is used to compare the test value with quality para range and
    display * , if it is out of range 
    */
private void chkQPRNG(float P_fltNPFVL,float P_fltNPTVL,float P_fltQPRVL,int P_intROWNO,JTable P_tblTBLNM,int P_intCOLID)
{
	L_RNGVL ="";
	try
	{
		if ((P_fltNPFVL == 0) && (P_fltNPTVL == 0))
		    P_tblTBLNM.setValueAt("",P_intROWNO,P_intCOLID);
		else if ((P_fltNPFVL != 0) && (P_fltNPTVL == 0))
		{
		   if (P_fltQPRVL < P_fltNPFVL)
		   {
			   P_tblTBLNM.setValueAt("*",P_intROWNO,P_intCOLID);
		   }
		   else
		       P_tblTBLNM.setValueAt("",P_intROWNO,P_intCOLID);
		}
		else if ((P_fltNPFVL == 0) && (P_fltNPTVL != 0))
		{
		   if (P_fltQPRVL > P_fltNPTVL)
		   {
			   P_tblTBLNM.setValueAt("*",P_intROWNO,P_intCOLID);
			}
		   else
		   {
			   P_tblTBLNM.setValueAt("",P_intROWNO,P_intCOLID);
		   }
		}
		else
		{
		   if (( P_fltQPRVL < P_fltNPFVL) || (P_fltQPRVL > P_fltNPTVL))
		   {
			   P_tblTBLNM.setValueAt("*",P_intROWNO,P_intCOLID);
		   }
		   else
		       P_tblTBLNM.setValueAt("",P_intROWNO,P_intCOLID);
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"chkQPRNG");
	}
}
private void getGRBARR(int P_intROWNO)
{
  try
  {
    String L_strGRBDT = "";
    txtGRBTS.setText("");
    for (int i = 0;i<intGRBCT;i++)
    {
         if (staGRBDT[i][0].trim().equals(tblTSTDT.getValueAt(P_intROWNO,TB_QPRCD).toString().trim()))
             L_strGRBDT = L_strGRBDT + padSTRING('R',staGRBDT[i][0].trim(),10) + padSTRING('R',staGRBDT[i][1].trim().substring(11),11) + staGRBDT[i][2].trim() + "\n";
    }
    txtGRBTS.insert(L_strGRBDT,0);
    txtGRBTS.select(0,0);
  }
  catch(Exception L_E)
  {
     setMSG("Grab test Details not available ... ",'E');
  }                 
}
/**
    This method is used to get GRAB and Additional Test details from QC_PSMST
*/
private void getGRADDT()// Grab and additional details
{
	boolean L_flgGRBFL = false;
    int l=0;
	int i=0,p=0;
	int L_intCOLCT =0;
	ResultSet L_rstRSGRB;	
	ResultSetMetaData L_rsmRSMDT;
    Timestamp L_tmsTEMP;		
    String L_strADDDT = "";
    String L_strDATTM="";
   	String L_strQPRCD ="";
	String L_strQPVAL ="";
	String L_strQPRLS ="";
	String L_strCOLNM ="";
    try
    {
		if(hstHSTST !=null)
			hstHSTST.clear();
		M_strSQLQRY ="Select * from QC_PSMST where isnull(PS_STSFL,'') <> 'X'" ;
		M_strSQLQRY += " and PS_QCATP = '" + cmbPRDTP.getSelectedItem().toString().substring(0,2) + "' and PS_TSTTP = '" + strGRBTP_fn.trim() + "'";
		M_strSQLQRY += " and PS_LOTNO = '"+txtLOTNO.getText().trim() + "'" + " order by PS_TSTDT";
	    L_rstRSGRB = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(L_rstRSGRB !=null)
		{
			L_rsmRSMDT = L_rstRSGRB.getMetaData();
			L_intCOLCT = L_rsmRSMDT.getColumnCount();
			while (L_rstRSGRB.next())
			{
				for(i=0;i<L_intCOLCT;i++)
				{
					L_strCOLNM = L_rsmRSMDT.getColumnName(i+1);
					if(L_strCOLNM.substring(6).equals("VL"))
				    {
						L_strQPRCD = L_strCOLNM.substring(3,6); 
						L_strQPVAL = L_rstRSGRB.getString(L_strCOLNM);	
						if(L_strQPVAL !=null)
						{
						   	staQPRDT[p][0] = L_strQPRCD;
							L_tmsTEMP = L_rstRSGRB.getTimestamp("PS_TSTDT");
							if(L_tmsTEMP !=null)
							    L_strDATTM = M_fmtLCDTM.format(L_tmsTEMP);
							staQPRDT[p][1] = L_strDATTM.trim();
							staQPRDT[p][2] = L_strQPVAL.trim();
							p++;
						}
					}
				}
			}
			L_rstRSGRB.close();
		}
		for(int j=0;j<intQPRCT;j++)
		{
		     tblTSTDT.setCellColor(j,TB_CMPVL,java.awt.Color.black);
		}
		for(int k=0;k<p;k++)
		{
			L_flgGRBFL = false;
			for(int j=0;j<intQPRCT;j++)
			{
			 	if(staQPRDT[k][0].trim().equals(tblTSTDT.getValueAt(j,TB_QPRCD)))
				{
					staGRBDT[l][0] = staQPRDT[k][0];
					staGRBDT[l][1] = staQPRDT[k][1];
					staGRBDT[l][2] = staQPRDT[k][2];
					l += 1;
					L_flgGRBFL = true;
					hstHSTST.put(String.valueOf(j),"x");
		            tblTSTDT.setCellColor(j,TB_CMPVL,java.awt.Color.blue);
				}
			}
			if(!L_flgGRBFL)
			{
				L_strDATTM = staQPRDT[k][1];
				L_strADDDT = L_strADDDT + padSTRING('R',staQPRDT[k][0].trim(),10)+padSTRING('R',L_strDATTM.substring(11),11) + staQPRDT[k][2].trim() + "\n";
			}
		}
		txtADDTS.insert(L_strADDDT,0);
		txtADDTS.select(0,0);
     	intGRBCT = l;
        //L_rstRSGRB.close();
    }
    catch(Exception L_E)
    {
       	setMSG("Grab test Details not available... ",'E');
    }
}
private void getRMK(String P_strLOTNO,String P_strTSTNO)
{
	String M_strSQLQRY ="";
	M_strSQLQRY ="Select count(*) from QC_RMMST where ";
	M_strSQLQRY += " RM_QCATP = '"+cmbPRDTP.getSelectedItem().toString().substring(0,2)+"' AND RM_TSTTP  ='"+strCLSTP_fn+"' and RM_TSTNO ='"+P_strLOTNO.trim()+"' ";
	L_RECCNT = cl_dat.getRECCNT(M_strSQLQRY);
	if(L_RECCNT == 1)
	{
		getTSRMK(P_strLOTNO,strCLSTP_fn);
	}
	else
	{
		if(strCLSFL.indexOf("'4','9'")<=0) 
		{
			if(P_strTSTNO !=null)
				if(P_strTSTNO.length() >0)
					getTSRMK(P_strTSTNO,"0103");	
		}
	}
}
private void getTSRMK(String P_strTSTNO,String P_strTSTTP)
{
	ResultSet L_RSLRMK;
	String L_TSTRMK = "" ;
    M_strSQLQRY = "Select RM_TSTTP, RM_REMDS from QC_RMMST where RM_QCATP = '" + cmbPRDTP.getSelectedItem().toString().substring(0,2).trim() + "' and RM_TSTTP = '" ;
    M_strSQLQRY += P_strTSTTP.trim() +"' and RM_TSTNO = '";
    M_strSQLQRY += P_strTSTNO.trim()+"'";
//	M_strSQLQRY += " AND RM_STSFL <> 'X'";
    try
    {
	    L_RSLRMK = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(L_RSLRMK !=null)
		{
			while (L_RSLRMK.next())
			{
				txtREMDS.setText(L_RSLRMK.getString("RM_REMDS"));
			}
			 L_RSLRMK.close();
		}
      }
      catch(Exception L_SE)
      {
		  setMSG(L_SE,"getTSRMK");
	  }  
}
	class inpVRFY extends InputVerifier 
	{
	    String L_strTEMP ="";
		public boolean verify(JComponent input) 
		{
		    try
		    {
    			if((input == txtPPRCD)&&(chrCLSFL =='P'))
    			{
    			   if(txtPPRCD.getText().length() == 0)
    			    return true;
    			   txtPCLTM.setText(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText());
    			   txtPCLBY.setText(cl_dat.M_strUSRCD_pbst);
    			   L_strTEMP = exePRVLDN(txtPPRCD.getText().trim());
    			   if(L_strTEMP.equals("-"))
    			   {
    			        setMSG("Invalid Product Code..",'E');
    			        return false;
    			   }
    			}
    			if((input == txtCPRCD)&&(chrCLSFL =='C'))
    			{
    			   if(txtCPRCD.getText().length() == 0)
    			     return true;
    			   txtCLSTM.setText(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText());
    			   txtCLSBY.setText(cl_dat.M_strUSRCD_pbst);
    			   L_strTEMP = exePRVLDN(txtCPRCD.getText().trim());
    			   if(L_strTEMP.equals("-"))
    			   {
    			        setMSG("Invalid Product Code..",'E');
    			        return false;
    			   }
    			}
    			if(input == txtRCLOT)
    			{
    			   if(txtRCLOT.getText().length() != 8)
    			    return true;
    			    flgLTVLD = false;
    			    M_strSQLQRY = "SELECT LT_LOTNO from PR_LTMST where SUBSTRING(LT_LOTNO,1,3) not in "+strSCPLT_fn;		
            		M_strSQLQRY +=" and LT_PRDTP = '"+cmbRCPRD.getSelectedItem().toString().substring(0,2)+"'";
            		M_strSQLQRY +=" and LT_LOTNO = '"+txtRCLOT.getText().trim()+"'";
            		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            		if(M_rstRSSET !=null)
            		{
            			if(M_rstRSSET.next())
            			{
            				//setMSG("Valid Lot Number..",'N');
            				flgLTVLD = true;
            				setCursor(cl_dat.M_curDFSTS_pbst);
            				return true;
            			}
            			else
            			{ 
            			    setMSG("Invalid Lot Number..",'E');
            			    return false;
            			}
            		}
    			}
    			if(input == txtPTFNO)
    			{
    			    if(txtPTFNO.getText().length() != 5)
    			        return true;
    			    if(!txtPTFNO.getText().trim().equals(strPTFNO))    
    			    {
    			        setMSG("PTF No. Can not be changed..",'E');
    			        txtPTFNO.setText(strPTFNO);
    			        return false;
    			    }
    			}
    			if(input == txtPTFDT)
    			{
    			    if(txtPTFNO.getText().length() == 0)
    			        return true;
    			    if(M_fmtLCDAT.parse(txtPTFDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
    				{							
    					setMSG("Invalid Date,Should not be greater than Login date(DD/MM/YYYY)",'E');
    					return false;
    				}
    				else
    				{
    				    M_calLOCAL.setTime(M_fmtLCDAT.parse(txtPTFDT.getText().trim()));
        				M_calLOCAL.add(java.util.Calendar.DATE,-1);
        				String L_strTEMP = M_fmtLCDAT.format(M_calLOCAL.getTime());
                        if(M_fmtLCDAT.parse(L_strTEMP).compareTo(M_fmtLCDAT.parse(strREFDT))== 0)
        				{
        					if(M_fmtLCDAT.parse(txtPTFDT.getText().trim()).compareTo(M_fmtLCDAT.parse(strREFDT))>0)
        					{	  
        						setMSG("Valid Date...",'N');
        					}
        					else
        					{
        					    setMSG("Start Transaction Entering Today's date.",'E');
        					   // JOptionPane.showMessageDialog(this,"Start Transaction Entering Today's date.","Date Enter Status",JOptionPane.INFORMATION_MESSAGE);
        					    return false;
        					}
        				}
        				else
        				{
        				     M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));
        				     M_calLOCAL.add(java.util.Calendar.DATE,+1);
        				     L_strTEMP = M_fmtLCDAT.format(M_calLOCAL.getTime());
            			      setMSG("Kindly Enter PTF Date as "+L_strTEMP,'E');
            			 //  JOptionPane.showMessageDialog(this,"Kindly Enter PTF Date as "+L_strTEMP,"Date Enter Status",JOptionPane.INFORMATION_MESSAGE);
            			     return false;
            			}
        			}
    			}
    			/*if(input == txtPTFCT)
    			{
    			    if(txtPTFCT.getText().length() == 0)
    			        return true;
    			    if(!vldPTFCT())
    			    {
    			        setMSG("Invalid PTF Category..",'E');
    			        return false;
    			    }
    			}*/
    			/*if(input == txtNWPRD)
    			{
    			   if(txtNWPRD.getText().length() == 0)
    			    return true;
    			    flgPRVLD = false;
    			    txtNWGRD.setText("");
    			    M_strSQLQRY = "select PR_PRDCD,PR_PRDDS from CO_PRMST where SUBSTRING(PR_PRDCD,1,2) = '";
                    M_strSQLQRY += tblLOTRC.getValueAt(0,TB1_PRDCD).toString().trim().substring(0,2) + "' and PR_STSFL <> 'X'";
            		M_strSQLQRY += " and PR_PRDCD ='"+txtNWPRD.getText().trim()+"'";
            		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
            		if(M_rstRSSET ==null)
            		{
            		    setMSG("Invalid Product Code..",'E');
            		    return false;
            		}
            		if(M_rstRSSET.next())
        			{
        				//setMSG("Valid Grade..",'N');
        				txtNWGRD.setText(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
        				setCursor(cl_dat.M_curDFSTS_pbst);
        				flgPRVLD = true;
        				M_rstRSSET.close();
        				return true;
        			}
        			else
        			{ 
        			    setMSG("Invalid Product Code..",'E');
        			    return false;
        			}
        		}*/
    		}
    		catch(Exception L_E)
    		{
    		    setMSG(L_E,"Verify");
    		}
   			return true;	
        
		}
	}
	private class co_teautTBLVR extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				if(P_intCOLID== 0) // Column ID is 0
				{
					
				}
			}
			catch(Exception L_E)
			{
			}
			return true;
		}
		
	}
	boolean vldDATA()
	{
		try
		{
			setMSG("",'N');
			int L_intSELCT =0;
			String L_strGPSTS ="";
			if(jtpTRNDT.getSelectedIndex() ==intGPAPR)
			{
				// GP Approval
				for(int i=0;i<tblGPAPR.getRowCount();i++)
				{
					if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
						L_strGPSTS ="";
						if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Held For Discussion"))
						{
							L_strGPSTS ="2";
						}
						else if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Approved"))
						{
							L_strGPSTS ="3";
						}
						else if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Select"))
						{
							L_strGPSTS ="";
						}
						if(L_strGPSTS.equals(""))
						//if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals(""))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}
						if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Select"))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}

					}
				}
				if(L_intSELCT ==0)
				{
					setMSG("Please select at least one row for Approval..",'E');
					return false;
				}
			}
			else if(jtpTRNDT.getSelectedIndex() ==intGPAUT)
			{
				// GP Authorisation
				L_intSELCT =0;
				for(int i=0;i<tblGPAUT.getRowCount();i++)
				{
					if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
						if(tblGPAUT.getValueAt(i,TBL_VEHNO).toString().equals(""))
						{
							setMSG("Vehicle no. for G.P "+tblGPAUT.getValueAt(i,TBL_DOCNO).toString()+" Should be updated by Stores before authorisation..",'E');
							return false;
						}
						if(tblGPAUT.getValueAt(i,TBL_VEHDS).toString().equals(""))
						{
							setMSG("Carrier Name for G.P "+tblGPAUT.getValueAt(i,TBL_DOCNO).toString()+" Should be updated by Stores before authorisation..",'E');
							return false;
						}
					}
				}
				if(L_intSELCT ==0)
				{
					setMSG("Please select at least one row for Authorisation..",'E');
					return false;
				}
			}
			else if(jtpTRNDT.getSelectedIndex() ==intINAPR)
			{
				// Indent Authorisation
				L_intSELCT =0;
				for(int i=0;i<tblINAUT.getRowCount();i++)
				{
					if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
						if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals(""))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}
						if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals("Select"))
						{
							setMSG("Please Select the Authorisation Tag at line "+(i+1) + "..",'E');
							return false;
						}
					}
				}
				if(L_intSELCT ==0)
				{
					setMSG("Please select at least one row for Authorisation..",'E');
					return false;
				}
			}
			else if(jtpTRNDT.getSelectedIndex() ==intLTPTF)
			{
			    if(txtPTFDT.getText().trim().length()==0)
			    {
			        setMSG("Enter PTF Date ..",'E');
			        return false; 
			    }
			    int L_intCNT =0;
    			for(int i=0;i<=(tblPTFDT.getRowCount()-1);i++)
    			{			
    		        if(tblPTFDT.getValueAt(i,TB4_STATS).toString().trim().equals("true"))
    		        {	
    		            L_intCNT++;
    			       // cl_dat.M_flgLCUPD_pbst = true;
    	    	    }
        		}
    			if(L_intCNT ==0)
    		    {
    		        setMSG("Select At least one row for PTF generation ..",'E');
    		        return false;
    		    }
			}
		}
		catch(Exception L_E)
		{
			return false;
		}
		return true;
	}
	void exeSAVE()
	{
		try
		{
			if(!vldDATA())
				return;
			//cl_dat.M_strLCUPD_pbst = true;
            String L_strGPSTS ="",L_strGPAPR ="";
			String L_strEML ="",L_strMGPNO ="",L_strINDNO="",L_strSTSFL ="",L_strAPRDS="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				cl_eml ocl_eml = new cl_eml();
				if(jtpTRNDT.getSelectedIndex() ==intGPAPR)
				{
					for(int i=0;i<tblGPAPR.getRowCount();i++)
					{
						if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{	
							L_strMGPNO =tblGPAPR.getValueAt(i,TBL_DOCNO).toString();
                            L_strGPSTS ="";
							if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Held For Discussion"))
							{
								L_strGPAPR = "Held For Discussion";
								L_strGPSTS ="2";
							}
							else if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals("Approved"))
							{
								L_strGPAPR = "Approved";
								L_strGPSTS ="3";
							}
							if(L_strGPSTS.equals(""))
							   //continue; // Next Iteration
                            {
                                setMSG("Please select the authorisation Flag..",'E');
                                return;
                            }

							M_strSQLQRY ="UPDATE MM_GPTRN SET GP_STSFL = '"+L_strGPSTS+"',GP_LUSBY = '"
							+cl_dat.M_strUSRCD_pbst+"',GP_LUPDT = '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
							+"GP_APRBY = '"+cl_dat.M_strUSRCD_pbst+"',GP_APRDT = current_timestamp "
							+"WHERE GP_STRTP = '"+hstCODCD.get(tblGPAPR.getValueAt(i,TBL_STRTP).toString()).toString()+"' AND GP_MGPTP = '"
							+hstCODCD.get(tblGPAPR.getValueAt(i,TBL_DOCTP).toString()).toString()+"' AND GP_MGPNO = '"
							+L_strMGPNO+"' and isnull(GP_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							// Generate email to user saying GP no. is approved.
							// keep two variables preby and aprby, frwto 
							M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+tblGPAPR.getValueAt(i,TBL_PREBY).toString()+"'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							if(M_rstRSSET.next())
							{
								String L_STSDS ="";
                                L_strEML = M_rstRSSET.getString("US_EMLRF");
                                if(L_strGPSTS.equals("2")) // Held For Discussion
                                    L_STSDS =" is Held For Discussion by ";
                                else
                                    L_STSDS =" is Approved by ";
								if(L_strEML.length() >0)
									ocl_eml.sendfile(L_strEML,null,"Intimation of Gate Pass Approval","Gate Pass No."+L_strMGPNO + L_STSDS+cl_dat.M_strUSRCD_pbst);
							}
							if(M_rstRSSET != null)
								M_rstRSSET.close();
						}
					}
				}
				else if(jtpTRNDT.getSelectedIndex() ==intGPAUT)
				{
					for(int i=0;i<tblGPAUT.getRowCount();i++)
					{
						if(tblGPAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							L_strMGPNO = tblGPAUT.getValueAt(i,TBL_DOCNO).toString();
							M_strSQLQRY ="UPDATE MM_GPTRN SET GP_STSFL = '4',GP_LUSBY = '"
							+cl_dat.M_strUSRCD_pbst+"',GP_LUPDT = '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
							+"GP_AUTBY = '"+cl_dat.M_strUSRCD_pbst+"',GP_AUTDT = current_timestamp "
							+"WHERE GP_STRTP = '"+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"' AND GP_MGPTP = '"
							+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_DOCTP).toString()).toString()+"' AND GP_MGPNO = '"
							+L_strMGPNO+"' and isnull(GP_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(hstCODCD.get(tblGPAUT.getValueAt(i,TBL_DOCTP).toString()).toString().equals("51")) //Returnable
							{
								M_strSQLQRY = "update mm_gptrn set gp_duedt = date(days(gp_duedt)+days(CURRENT_DATE)-days(GP_MGPDT)) "
										+"WHERE GP_STRTP = '"+hstCODCD.get(tblGPAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"' AND GP_MGPTP = '51'"
										+" AND GP_MGPNO = '"+L_strMGPNO+"' and isnull(GP_STSFL,'') <>'X'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
							// Generate email to user saying GP no. is authorised.
							M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+tblGPAUT.getValueAt(i,TBL_PREBY).toString()+"'";;
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							while(M_rstRSSET.next())
							{
								L_strEML = M_rstRSSET.getString("US_EMLRF");
								if(L_strEML.length() >0)
									ocl_eml.sendfile(L_strEML,null,"Intimation of Gate Pass Authorisation","Gate Pass No."+ L_strMGPNO + " is Authorised for Gate out. ");
							}
							if(M_rstRSSET != null)
							M_rstRSSET.close();
						}
					}
				}
				else if(jtpTRNDT.getSelectedIndex() ==intINAPR)
				{
					for(int i=0;i<tblINAUT.getRowCount();i++)
					{
						if(tblINAUT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							L_strINDNO = tblINAUT.getValueAt(i,TBL_DOCNO).toString();
							if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals("Held For Discussion"))
							{
								L_strAPRDS = "Held For Discussion";
								L_strSTSFL ="3";
							}
							else if(tblINAUT.getValueAt(i,TBL_APRFL).toString().equals("Authorised"))
							{
								L_strAPRDS = "Authorised";
								L_strSTSFL ="4";
							}
							if(L_strSTSFL.equals(""))
							   continue; // Next Iteration
							M_strSQLQRY ="UPDATE MM_INMST SET IN_STSFL = '"+L_strSTSFL +"',IN_LUSBY = '"
							+cl_dat.M_strUSRCD_pbst+"',IN_LUPDT = '"
							+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',"
							+"IN_AUTBY = '"+cl_dat.M_strUSRCD_pbst+"',IN_AUTDT = current_timestamp ";
							if(L_strSTSFL.equals("4"))
							{
								M_strSQLQRY +=",IN_AUTQT = IN_INDQT ";	
							}
							M_strSQLQRY +=" WHERE IN_STRTP = '"+hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"'"
							+" AND IN_INDNO = '"+L_strINDNO+"' and isnull(IN_STSFL,'') <>'X'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(L_strSTSFL.equals("4"))
							{
								M_strSQLQRY = "update mm_inmst set in_porby = date(days(in_porby)+days(CURRENT_DATE)-days(IN_INDDT)),in_reqdt = date(days(in_reqdt)+days(CURRENT_DATE)-days(IN_INDDT)) "
											 +",in_expdt = date(days(in_expdt)+days(CURRENT_DATE)-days(IN_INDDT))"
										+"WHERE IN_STRTP = '"+hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"'"
										+" AND IN_INDNO = '"+L_strINDNO+"' AND isnull(IN_INDTP,'') ='01'";
								cl_dat.exeSQLUPD(M_strSQLQRY,"");
							}
							// Generate email to user saying Indent is authorised.
							M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+tblINAUT.getValueAt(i,TB1_PREBY).toString()+"'";;
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET != null)
							while(M_rstRSSET.next())
							{
								L_strEML = M_rstRSSET.getString("US_EMLRF");
								if(L_strEML.length() >0)
									ocl_eml.sendfile(L_strEML,null,"Intimation of Indent Authorisation","Indent No."+ L_strINDNO + " is "+L_strAPRDS);
							}
							if(M_rstRSSET != null)
							M_rstRSSET.close();
						}
					}
				}
				if((jtpTRNDT.getSelectedIndex() ==intFNLCL)||(jtpTRNDT.getSelectedIndex() ==intPRVCL))
				{
	            	if(exeCLSREC())
				    {
				        String L_strLOTNO =txtLOTNO.getText().trim();
				        clrCOMP();
				        txtLOTNO.setText(L_strLOTNO);
				        txtGRBTS.setText("");
				        txtADDTS.setText("");
				        if(cl_dat.exeDBCMT("exeSAVE"))
    				    {
    					    setMSG("Data saved successfully..",'N');
    				    }
    				    else
    				    {   
    				        setMSG("Error in Saving..",'E');    
    				    }
    				    txtLOTNO.requestFocus();
    			    }
		   			else
    				{
    					//setMSG("Error in Saving..",'E');
    				}
				}
				else if((jtpTRNDT.getSelectedIndex() ==intLTRCL))
				{
				 	for(int i=0;i<intRCQPCT;i++)
    				{
    					vldRNG(i);
    				}
				 	exeRCLCHK();
        			if(!chkOPTSEL())
        			{
        				cl_dat.M_btnSAVE_pbst.setEnabled(false);
        				return;
        			}
				    exeLOTRCL();
				    if(cl_dat.exeDBCMT("exeSAVE"))
				    {
					    setMSG("Data saved successfully..",'N');
					    clrCOMP();
                        jtpTRNDT.setSelectedIndex(intLTRCL);
                        setMSG("Data saved successfully..",'N');
				    }
				    else
				    {   
				        setMSG("Error in Saving..",'E');    
				    }
				}
				else if((jtpTRNDT.getSelectedIndex() ==intLTPTF))
				{
				    genPTFREC();
				}
				else
				{
    				if(cl_dat.exeDBCMT("exeSAVE"))
    				{
    					setMSG("Data saved successfully..",'N');
    					if(jtpTRNDT.getSelectedIndex() ==intGPAPR)
    					{
    						tblGPAPR.clrTABLE();
    						tblITDT1.clrTABLE();
    					}
    					else if(jtpTRNDT.getSelectedIndex() ==intGPAUT)
    					{
    						tblGPAUT.clrTABLE();
    						tblITDT2.clrTABLE();
    					}
    					else if(jtpTRNDT.getSelectedIndex() ==intINAPR)
    					{
    						tblINAUT.clrTABLE();
    						tblITDT3.clrTABLE();
    					}
    					flgDATA =false;	
    				}
    				else
    				{
    					setMSG("Error in Saving..",'E');
    				}
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");			
		}
	}
private void exeRLSLOT(String P_strNEWFL,String P_strPRDCD)
{
	try
	{
    	/*M_strSQLQRY ="select count(*) from FG_PTFRF where ";
    	M_strSQLQRY += " PTF_LOTNO = '"+txtLOTNO.getText().trim()+"' AND PTF_PRDTP  ='"+LM_PRDTP.trim()+"'"; 
    	L_RECCNT = cl_dat.ocl_dat.getRECCNT("FG","ACT",L_STRSQL);
    	if(L_RECCNT >= 1)
    	{
    		setMSG("PTF has been created, Lot can not be released ..",'E');
    	}
    	else
    	*/
    	 M_strSQLQRY ="Update PR_LTMST SET ";
    	if(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strPRVCL_fn))
    	{
    	    setMSG("Releasing the lot from Provisional classification ..",'N');    
    	    M_strSQLQRY += "LT_PCLBY='',LT_PCLTM =null,LT_PPRCD ='',";
    	}
    	else if(jtpTRNDT.getTitleAt(jtpTRNDT.getSelectedIndex()).equals(strFNLCL_fn))
    	{
    	     setMSG("Releasing the lot from Final classification ..",'N');
         	 M_strSQLQRY += "LT_CLSBY='',LT_CLSTM =null,LT_CPRCD ='',";
    	}
         M_strSQLQRY += "LT_PRDCD='"+P_strPRDCD.trim()+"',"; 
     	 M_strSQLQRY += "LT_CLSFL='"+P_strNEWFL+"',"; 
    	 M_strSQLQRY += "LT_TRNFL='0',"; 
    	 M_strSQLQRY += "LT_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"; 
         M_strSQLQRY += "LT_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"; 
    	 M_strSQLQRY +=" where LT_PRDTP ='"+cmbPRDTP.getSelectedItem().toString().substring(0,2) +"'";
    	 M_strSQLQRY +=" and LT_LOTNO ='"+txtLOTNO.getText().trim() +"'";
    	 M_strSQLQRY +=" and LT_RCLNO ='"+strINTRCL_fn +"'";
    	 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	if(cl_dat.exeDBCMT("RLSLOT"))
        {
    		String L_strLOTNO = txtLOTNO.getText().trim();
    		clrCOMP();
    		txtLOTNO.requestFocus();
    		txtLOTNO.setText(L_strLOTNO);
    		setMSG("Lot has been released..",'N');
    		btnRLS.setVisible(false);
    	}
    	else
    		setMSG("Error in releasing the Lot..",'N');
	}
	catch(Exception L_E)
	{
	    setMSG(L_E,"exeRLSLOT");
	}
}
private boolean getACLST(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO)
{
    try
    {
        String L_QPRCD ="";
        ResultSet L_RSLSET;
        boolean flgFOUND = false;
        double L_NPFVL =0,L_NPTVL =0,L_RSLVL =0;
        M_strSQLQRY = "select QP_QPRCD,QP_NPFVL,QP_NPTVL,QP_CMPFL,QP_TXTVL,A.* from  "
                      +" PR_LTMST,CO_QPMST,QC_PSMST A where LT_TPRCD = QP_PRDCD AND LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO "
                      +" AND PS_QCATP ='"+LP_PRDTP +"' AND PS_TSTTP ='0103' AND QP_QCATP ='"+LP_PRDTP +"' AND QP_TSTTP ='0103' AND QP_SRLNO ='00000'"
                      +" and QP_ORDBY is not null and qp_enddt is null AND QP_CMPFL ='Y' "
                      +" AND isnull(LT_PTAFL,'') ='N' and LT_PRDTP = '"+LP_PRDTP +"' "
                      +" AND LT_LOTNO ='"+LP_LOTNO +"' AND LT_RCLNO ='"+LP_RCLNO +"'";   
        L_RSLSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
        if(L_RSLSET == null)
            return false;
        while(L_RSLSET.next())
        {
            L_QPRCD = L_RSLSET.getString("QP_QPRCD");
            L_RSLVL = L_RSLSET.getDouble("PS_"+L_QPRCD+"VL");
            L_NPFVL = L_RSLSET.getDouble("QP_NPFVL");
            L_NPTVL = L_RSLSET.getDouble("QP_NPTVL");
         	if ((L_NPFVL != 0) && (L_NPTVL == 0))
			{
			   if (L_RSLVL < L_NPFVL)
			   {
			        return false;
			   }
			}
			else if ((L_NPFVL == 0) && (L_NPTVL != 0))
			{
			   if (L_RSLVL > L_NPTVL)
			   {
			        return false;
				}
			}
			else
			{
			   if (( L_RSLVL < L_NPFVL) || (L_RSLVL > L_NPTVL))
			   {
			        return false;
			   }
			}
			flgFOUND = true;
        }
        if(flgFOUND)
            return true;
        else
            return false;
    }
    catch(Exception L_E)
    {
        return false;
    }
}
private String exePRVLDN(String P_strPRDCD)
{
      ResultSet L_RSLPRV;
      String L_RETVL = " ";
	  M_strSQLQRY = "select distinct PR_PRDCD,PR_PRDDS,PR_STSFL from CO_PRMST where (SUBSTRING(PR_PRDCD,1,4) = '";
      M_strSQLQRY += txtTPRCD.getText().trim().substring(0,4) + "' or SUBSTRING(PR_PRDCD,3,2) = '95' or SUBSTRING(PR_PRDCD,5,2) = '95') and PR_STSFL <> 'X' and PR_PRDCD = '";
	  M_strSQLQRY +=P_strPRDCD.trim() + "'";
	  M_strSQLQRY += " order by PR_PRDCD";
	  strPRSTS ="";
      try
      {
           L_RSLPRV = cl_dat.exeSQLQRY1(M_strSQLQRY);
           if(L_RSLPRV !=null)
		   {
				if(L_RSLPRV.next())
				{
				     L_RETVL = L_RSLPRV.getString("PR_PRDDS");
					 strPRSTS = nvlSTRVL(L_RSLPRV.getString("PR_STSFL"),"");
					 /*if(nvlSTRVL(L_RSLPRV.getString("PR_STSFL"),"").equals("4"))
					 {
						 LM_OFFGRD = true;
					 }
					 else LM_OFFGRD = false; */
						 
				}
				else
				     L_RETVL = "-";
				L_RSLPRV.close();
		   }
           else
			     L_RETVL = "-";

           return (L_RETVL);
      }
      catch(Exception L_SE)
      {
           return ("-");
      }                                              
}
private boolean exeCLSREC()
{
   	try
	{
	    cl_dat.M_flgLCUPD_pbst = true;
		if(strPRSTS.equals("4"))
			flgOFFGRD = true;
		else
			flgOFFGRD = false;
		if(!flgOFFGRD)
		{
			for(int i=0;i<intQPRCT;i++)
			{
			    if (tblTSTDT.getValueAt(i,TB_CMPVL).toString().trim().equals(""))
			    {
			       setMSG("Some of the values are not available ",'E'); 
			       return false;  
			    }
			   	exeRNGCHK(i);
			    if (tblTSTDT.getValueAt(i,TB_FLAG).toString().trim().equals("*"))
			    {
			          setMSG("Quality Parameter Value is not in the range specified ",'E');
			          return false;
			    }
			}
			tblTSTDT.editCellAt(intROWCT-1,TB_CMPVL);
		}
		if (txtPPRCD.getText().length() < 10 && (chrCLSFL == 'P'))
		{
	         setMSG("Enter Prov. Classification Grade ... ",'E');
	          txtPPRCD.requestFocus();
	          return false;
		}
		if (txtCPRCD.getText().length() < 10 && (chrCLSFL == 'F'))
		{
	          setMSG("Enter Classification Grade ... ",'E');
	          txtCPRCD.requestFocus();
	          return false;
		}
		if(strTSTNO !=null)
		  strTSTNO = strTSTNO.trim();
		else
			strTSTNO = "";
	if(flgTSTFL)
	{
	      String L_strCOLNM ="";
    	  M_strSQLQRY = "update QC_PSMST set ";
          M_strSQLQRY +="PS_TRNFL ='0',"; 
    	  M_strSQLQRY +="PS_STSFL ='"+strCLSFL+"',"; 
		  for(int i=0;i<intQPRCT;i++)
	      {
			   // If condition added on 10/12/2005 
			   if(tblTSTDT.getValueAt(i,TB_CMPVL)!=null)
			   if(!tblTSTDT.getValueAt(i,TB_CMPVL).toString().trim().equals(""))
			   {
			        L_strCOLNM  = "PS_" + tblTSTDT.getValueAt(i,TB_QPRCD).toString().trim() +"VL";
	      	        M_strSQLQRY += L_strCOLNM +"="+nvlSTRVL(tblTSTDT.getValueAt(i,TB_CMPVL).toString(),"0").trim()+",";
			   }
	      } 
		   M_strSQLQRY +="PS_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',"; 
		   M_strSQLQRY +="PS_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
	       M_strSQLQRY += " where PS_QCATP = '" + cmbPRDTP.getSelectedItem().toString().substring(0,2) + "' and PS_TSTTP = '" + strCMPTP_fn + "' and PS_LOTNO = '" + txtLOTNO.getText().trim() + "' and PS_RCLNO ='00'";
		   cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		  if(!cl_dat.M_flgLCUPD_pbst) 
		  {
	         setMSG("Error in Test Saving Data...       ",'E');
	         txtLOTNO.requestFocus();
	         return false;
	      }
		  else
		  {
			    setMSG("Updation completed ... in Test Data ",'N');
		  }
	}
	if(!updRMK())
		return false;	
	 M_strSQLQRY = "update PR_LTMST set ";
     if (chrCLSFL == 'P')
     {
		 M_strSQLQRY += "LT_PCLBY='"+txtPCLBY.getText().trim()+"',";
         M_strSQLQRY += "LT_PCLTM='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtPCLTM.getText().trim()))+"',";
         M_strSQLQRY += "LT_PPRCD='"+txtPPRCD.getText().trim()+"',"; 
         if(!strCLSFL.trim().equals("9"))
			M_strSQLQRY += "LT_PRDCD='"+txtPPRCD.getText().trim()+"',"; 
		M_strSQLQRY += "LT_CLSFL='"+strCLSFL+"',"; 
     }
     else if (chrCLSFL == 'F')
     {
		 M_strSQLQRY += "LT_CLSBY='"+txtCLSBY.getText().trim()+"',";
         M_strSQLQRY += "LT_CLSTM='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtCLSTM.getText().trim()))+"',";
         M_strSQLQRY += "LT_CPRCD='"+txtCPRCD.getText().trim()+"',"; 
         M_strSQLQRY += "LT_PRDCD='"+txtCPRCD.getText().trim()+"',"; 
	 	 M_strSQLQRY += "LT_CLSFL='"+strCLSFL+"',"; 
		// if((txtCPRCD.getText().trim().equals("")) ||(!strCLSFL.equals("9")))
		  // 	setMSG("Misupdation of data, please contact Systems department ",'E');
	 }       
	 M_strSQLQRY += "LT_TRNFL='0',"; 
	 M_strSQLQRY += "LT_LUSBY='"+cl_dat.M_strUSRCD_pbst+"',"; 
     M_strSQLQRY += "LT_LUPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
     M_strSQLQRY += " where LT_PRDTP = '" + cmbPRDTP.getSelectedItem().toString().substring(0,2) + "' and LT_LOTNO = '" + txtLOTNO.getText().trim() + "'";
	 M_strSQLQRY += " and LT_RCLNO = '" + strINTRCL_fn + "'";
     cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
     if(cl_dat.M_flgLCUPD_pbst)
	 {
		 setMSG("Updation completed ... in Lot Data ",'N');
	 }
     else
     {
        setMSG("Error in Saving Lot Data ... ",'E');
        txtLOTNO.requestFocus();
        return false;
     }
   }
   catch(Exception L_E)
   {
	    setMSG("Error in Saving the Data",'E');
        txtLOTNO.requestFocus();
        return false; 
   }
   return true;
}
private boolean updRMK()
{
	M_strSQLQRY ="";
	ResultSet L_rstRSSET;
	try
	{
		M_strSQLQRY ="Select count(*) L_CNT from QC_RMMST where ";
		M_strSQLQRY += " RM_QCATP = '"+cmbPRDTP.getSelectedItem().toString().substring(0,2)+"' AND RM_TSTTP  ='"+strCLSTP_fn+"' and RM_TSTNO ='"+txtLOTNO.getText().trim()+"' ";
		L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if(L_rstRSSET !=null)
		{
		    if(L_rstRSSET.next())
		        L_RECCNT = L_rstRSSET.getInt("L_CNT");
		    L_rstRSSET.close();    
		}
		//L_RECCNT = cl_dat.ocl_dat.getRECCNT("QC","ACT",L_STRSQL);
	   // if(( L_RECCNT > 0)&&(!LM_CLSFL.equals("9")))
		//changed on 10/04/03 as per VAM's requirement
		M_strSQLQRY ="";
		if( L_RECCNT > 0)
	    {
	       	if (!txtREMDS.getText().trim().equals(""))
		   	{
			   	M_strSQLQRY = "update QC_RMMST set ";
			   	M_strSQLQRY +="RM_REMDS ='"+txtREMDS.getText().trim()+"',";
				M_strSQLQRY +="RM_TRNFL ='0',";						  
				M_strSQLQRY +="RM_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
			 	M_strSQLQRY +="RM_LUPDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
		   	}
		   	else if (txtREMDS.getText().trim().equals(""))
		   	{
			   		M_strSQLQRY = "delete from QC_RMMST ";
		   	}
			M_strSQLQRY += " where RM_QCATP = '" + cmbPRDTP.getSelectedItem().toString().substring(0,2).trim() + "' and RM_TSTTP = '" + strCLSTP_fn + "' and RM_TSTNO = '" + txtLOTNO.getText().trim() + "'";
		}
		else if((L_RECCNT == 0)&&(!txtREMDS.getText().trim().equals("")))
		{
		     M_strSQLQRY = "INSERT INTO QC_RMMST(RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT) VALUES( ";
		     M_strSQLQRY +="'"+cmbPRDTP.getSelectedItem().toString().substring(0,2).trim()+"',";
			 M_strSQLQRY +="'"+strCLSTP_fn.trim()+"',";
			 M_strSQLQRY +="'"+txtLOTNO.getText().trim()+"',";
			 M_strSQLQRY +="'"+txtREMDS.getText().trim()+"',";
		     M_strSQLQRY +="'0',";
		     M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
		     M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
		}
		//cl_dat.ocl_dat.M_LCLUPD = true;
		if(M_strSQLQRY.trim().length()>0)
		{
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(!cl_dat.M_flgLCUPD_pbst)
			 {
			    setMSG("Error in Saving Remark data .. ",'E');
			   // txtREMDS.requestFocus();
			    return false;
			  }
			else
			{
				setMSG("Updation completed in Remarks ..",'N');
			}
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"updRMK");
	}
	return true;
}
// Reclassification methods
private void dspLOTDT()
{
	float L_fltDSPQT;
	String L_strRCLNO ="";
	String L_strPRDCD ="";
	String L_strPRDDS ="";
	String L_strSTKQT ="";
	String L_strISSTP ="10"; // For Despatch Type
	String L_strTB1RC;
	strLSTRCL ="";strLSTPRD ="";
	java.sql.Timestamp tmsTEMP;
	int L_CNT =0;
	try
	{
	    setCursor(cl_dat.M_curWTSTS_pbst);
	    txtNWPRD.setText("");txtNWGRD.setText("");txtNWDAT.setText("");
		tblLOTRC.clrTABLE();tblTSTRC.clrTABLE();tblSTKDT.clrTABLE();
		if(tblTSTRC.isEditing())
		    tblTSTRC.getCellEditor().stopCellEditing();
		if(tblLOTRC.isEditing())
		    tblLOTRC.getCellEditor().stopCellEditing();
		if(tblSTKDT.isEditing())
		    tblSTKDT.getCellEditor().stopCellEditing();
		tblTSTRC.setRowSelectionInterval(0,0);tblTSTRC.setColumnSelectionInterval(0,0);    
		tblLOTRC.setRowSelectionInterval(0,0);tblLOTRC.setColumnSelectionInterval(0,0); 
		tblSTKDT.setRowSelectionInterval(0,0);tblSTKDT.setColumnSelectionInterval(0,0);    
		cl_dat.M_btnSAVE_pbst.setEnabled(false);
	    if(LM_CMPFL !=null)
	        LM_CMPFL.removeAllElements();
	    setMSG("Fetching the Lot Details.. ",'N');
    	M_strSQLQRY = " select lt_rclno,lt_prdcd,lt_clstm,lt_bagqt,sum(st_stkqt)ST_STKQT from PR_LTMST ";
    	M_strSQLQRY += " left outer join fg_stmst on lt_rclno = st_rclno and lt_lotno = st_lotno ";
    	M_strSQLQRY += " where lt_lotno ='"+txtRCLOT.getText().trim()+"' and SUBSTRING(lt_lotno,1,3) not in "+strSCPLT_fn;
    	M_strSQLQRY += "  group by lt_rclno,lt_prdcd,lt_clstm,lt_bagqt order by lt_rclno";
    	M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
 		if(M_rstRSSET !=null)
 		{
			while(M_rstRSSET.next())
			{
				L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("LT_RCLNO"),"");
				if(L_strRCLNO.equals(strINTRCL_fn))
				{
					txtRBGQT.setText(setNumberFormat(M_rstRSSET.getFloat("LT_BAGQT"),3));
				}
				L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("LT_PRDCD"),"");
				
				L_strSTKQT = nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"");
				L_strPRDDS = nvlSTRVL(getPRDDS(L_strPRDCD),"");
				tblLOTRC.setValueAt(L_strRCLNO,L_CNT,TB1_RCLNO);
				tblLOTRC.setValueAt(L_strPRDCD,L_CNT,TB1_PRDCD);
				tblLOTRC.setValueAt(L_strPRDDS,L_CNT,TB1_PRDDS);
				tmsTEMP = M_rstRSSET.getTimestamp("LT_CLSTM");
				if(tmsTEMP !=null)
				    tblLOTRC.setValueAt(M_fmtLCDTM.format(tmsTEMP),L_CNT,TB1_CLSTM);
				tblLOTRC.setValueAt(setNumberFormat(M_rstRSSET.getFloat("ST_STKQT"),3),L_CNT,TB1_STKQT);
				L_CNT++;
			}
			M_rstRSSET.close();
 		}
 		strLSTRCL = L_strRCLNO;
		strLSTPRD = L_strPRDCD;
		strCURSTK  = Double.valueOf(L_strSTKQT).doubleValue(); 
		int L_intTEMP;
		String L_CURRCL="";
		L_intTEMP = Integer.parseInt(strLSTRCL);
		L_intTEMP++;
		strCURRC = String.valueOf(L_intTEMP);
		if(strCURRC.length()<2)
			strCURRC ="0" +strCURRC;
		txtNWDAT.setText("");
		txtNWDAT.setText(cl_dat.M_strLOGDT_pbst +" "+cl_dat.M_txtCLKTM_pbst.getText());
		txtNWPRD.requestFocus();		
		String L_RESNO ="";
		int L_intCNT =0;
    	flgRESFL = false; // reserved lot 
		M_strSQLQRY = "Select ST_MNLCD,ST_STKQT,ST_RESNO from fg_STMST where st_STKQT >0 and ST_RCLNO = '" +strLSTRCL +"'";
		M_strSQLQRY += " and ST_LOTNO = '"+txtRCLOT.getText() +"'"; 
		M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				if(nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"").length() >0)
					flgRESFL = true;
				tblSTKDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_MNLCD"),""),L_intCNT,TB2_LOCCD);
				tblSTKDT.setValueAt(setNumberFormat(M_rstRSSET.getDouble("ST_STKQT"),3),L_intCNT,TB2_STKQT);
				L_intCNT++;
			}
			M_rstRSSET.close();
		}
		getQPDTL(strLSTPRD);
		dspTSTDT(strLSTRCL.trim(),strLSTPRD.trim());
		M_strSQLQRY ="select ist_prdtp,ist_lotno,ist_rclno,sum(ist_issqt)L_DSPQT ";
		M_strSQLQRY +=" from FG_istrn where ist_prdtp ='"+cmbRCPRD.getSelectedItem().toString().substring(0,2)+"'";
		M_strSQLQRY +=" and ist_lotno ='"+txtRCLOT.getText()+"'and ist_isstp ='"+L_strISSTP +"'"; 
		M_strSQLQRY += " group by ist_prdtp,ist_lotno,ist_rclno order by ist_prdtp,ist_lotno,ist_rclno";        
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);	
		if(M_rstRSSET !=null)
		{
			while(M_rstRSSET.next())
			{
				L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("IST_RCLNO"),"");
				L_fltDSPQT = M_rstRSSET.getFloat("L_DSPQT");
				for(int i=0;i<10;i++)
				{
					L_strTB1RC =tblLOTRC.getValueAt(i,TB1_RCLNO).toString().trim();
					if(L_strTB1RC.length() >0)
					{
						if(L_strTB1RC.equals(L_strRCLNO))
						{
							tblLOTRC.setValueAt(setNumberFormat(L_fltDSPQT,3),i,TB1_DSPQT);
						}
					}
				}
			}
			M_rstRSSET.close();
		}
    	for(int i=0;i<intRCQPCT;i++)
    	{
    		vldRNG(i);
    	}
    	if(tblTSTRC.isEditing())
		    tblTSTRC.getCellEditor().stopCellEditing();
		tblTSTRC.setRowSelectionInterval(0,0);
		tblTSTRC.setColumnSelectionInterval(0,0);
		txtNWPRD.requestFocus();    
    	setMSG("",'N');
  	    setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	catch(Exception L_E)
	{
		setCursor(cl_dat.M_curDFSTS_pbst);
		setMSG(L_E,"dspLOTDT");
	}
}
public void getQPDTL(String P_strPRDCD)
{
	 try
     {
    	  intRCQPCT =0;
          //LM_CMPFL[L_ROWNUM] = "";
          float L_fltNPFVL=0;
    	  float L_fltNPTVL=0;
    	  float L_fltQPRVL=0;
          String L_strNPFDS=" ";
    	  String L_strNPTDS = " ";
    	  String L_strQPRDS = " ";
          if(LM_CMPFL !=null)
    		  LM_CMPFL.removeAllElements();
          M_strSQLQRY = "Select QP_QPRCD,QP_QPRDS,QP_UOMDS,QP_NPFVL,QP_NPTVL,QP_CMPFL,QP_ORDBY from CO_QPMST where QP_QCATP = '" + cmbRCPRD.getSelectedItem().toString().substring(0,2).trim() + "' and QP_TSTTP = '"+strCMPTP_fn+"'";
    	  M_strSQLQRY += " and QP_ORDBY is not null ";
          M_strSQLQRY += " and QP_PRDCD ='";
          M_strSQLQRY += P_strPRDCD.trim()+"'";
    	  M_strSQLQRY += " and QP_SRLNO ='"+strDFLSR_fn.trim()+"'";
    	  M_strSQLQRY += " and QP_ENDDT is null order by QP_ORDBY";
          M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
          if(M_rstRSSET !=null)
          while(M_rstRSSET.next())
          {
				tblTSTRC.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),""),intRCQPCT,TB3_QPRCD);
				tblTSTRC.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRDS"),""),intRCQPCT,TB3_QPRDS);
                tblTSTRC.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_UOMDS"),""),intRCQPCT,TB3_UOMDS);
		         try
                 {
              		 L_fltNPFVL = M_rstRSSET.getFloat("QP_NPFVL");
                     L_strNPFDS = "" + L_fltNPFVL;
                 }
                 catch (Exception L_E)
                 {
                    L_fltNPFVL = 0;
                    L_strNPFDS = " ";
                 }
                 try
                 {
                    L_fltNPTVL = M_rstRSSET.getFloat("QP_NPTVL");
                    L_strNPTDS = "" + L_fltNPTVL;
                 }
                 catch (Exception L_E)
                 {
                  	 L_fltNPTVL = 0;
                     L_strNPTDS = " ";
                 }
                 if (M_rstRSSET.getString("QP_CMPFL").trim().equals("Y"))
                 {
				    strROWNO = intRCQPCT;
                    LM_CMPFL.addElement("Y");
                 }
				 else
					 LM_CMPFL.addElement("N");
            	 String L_RNGVL = " " + L_strNPFDS + " - " + L_strNPTDS;
				 tblTSTRC.setValueAt(L_RNGVL.trim(),intRCQPCT,TB3_SPEC);
				 intRCQPCT += 1;
            }
            if(M_rstRSSET !=null)
				M_rstRSSET.close();
         }
         catch (Exception e)
         {
           	 setMSG("Invalid SQL Statement ... getQPRRNG",'E');
         }
    }
private void dspTSTDT(String P_strRCLNO,String P_strPRDCD)
{
	String L_strTEMP ="";
	String L_strQPRCD ="";
	String L_strQPRDS ="";
	String L_strSPCDS ="";
	String L_strTSTVL ="";
	String L_strQPRVL ="";
	flgTSROK = true;
	flgTSTCP = false;
	int L_CNT =0;
	try
	{
		M_strSQLQRY = "Select * from QC_PSMST where PS_QCATP = '" + cmbRCPRD.getSelectedItem().toString().substring(0,2) + "' and PS_RCLNO = '" ;
		M_strSQLQRY += P_strRCLNO + "' and PS_LOTNO = '";
		M_strSQLQRY += txtRCLOT.getText() + "' and PS_TSTTP ='"+strCMPTP_fn +"'";
		M_strSQLQRY += " AND PS_STSFL <> 'X'";
		M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		String L_STRDTM;
		if(M_rstRSSET !=null)
		{
    		while (M_rstRSSET.next())
            {
    		   for(int i=0;i<intRCQPCT;i++)
               {
            		L_strTEMP = "PS_"+tblTSTRC.getValueAt(i,TB_QPRCD)+"VL";
    				L_strQPRVL = M_rstRSSET.getString(L_strTEMP);
    				if(L_strQPRVL == null)
    				{
    					tblTSTRC.setValueAt("0",i,TB3_CMPVL);
    					setMSG("Test Result not available for the parameter " + tblTSTRC.getValueAt(i,TB3_QPRCD),'N');
    					flgTSROK = false;
    				}
    				else
    				{	
    					tblTSTRC.setValueAt(L_strQPRVL.trim(),i,TB3_CMPVL);	
    				}
    		   }
    		   L_CNT++;
            }
            M_rstRSSET.close();
        }
		if(L_CNT > 0)
			flgTSTCP = true;
		else
			flgTSTCP = false;
		if(!flgTSROK)
			setMSG("Some of the test values are not available.. please enter the Test Values. ",'E');
	}
	catch(Exception L_E)
	{
		setCursor(cl_dat.M_curDFSTS_pbst);
		setMSG(L_E,"dspTSTDT");
	}
}
private void vldRNG(int P_intROWNO)
{
	int L_intIDXVL =0;
	float L_fltNPFVL,L_fltNPTVL,L_fltQPRVL;
	tblTSTRC.editCellAt(P_intROWNO,TB3_CMPVL);
	try
	{
			
	   L_intIDXVL = tblTSTRC.getValueAt(P_intROWNO,TB3_SPEC).toString().trim().indexOf("-");
	   try
	   {
			L_fltNPFVL = Float.valueOf(tblTSTRC.getValueAt(P_intROWNO,TB3_SPEC).toString().substring(0,L_intIDXVL - 1).trim()).floatValue();
	   }
	   catch(Exception L_E)
	   {
			L_fltNPFVL = 0;
	   }
	   try
	   {
			L_fltNPTVL =Float.valueOf(tblTSTRC.getValueAt(P_intROWNO,TB3_SPEC).toString().substring(L_intIDXVL + 2).trim()).floatValue(); 
	   }
	   catch(Exception L_E)
	    {
			L_fltNPTVL = 0;
	   }
		if (tblTSTRC.getValueAt(P_intROWNO,TB3_CMPVL).toString().trim().length() >0)
		{
			L_fltQPRVL = Float.valueOf(tblTSTRC.getValueAt(P_intROWNO,TB3_CMPVL).toString().trim()).floatValue();
			if(LM_CMPFL.elementAt(P_intROWNO).toString().trim().equals("Y"))
				chkQPRNG(L_fltNPFVL,L_fltNPTVL,L_fltQPRVL,P_intROWNO,tblTSTRC,TB3_FLAG);
		}
	}
	catch(NumberFormatException L_NFE)
	{
	  setMSG("Invalid Value ...",'E');
	}
}
private void exeLOTRCL()
{
	try
	{
    	cl_dat.M_flgLCUPD_pbst = true;
    	setMSG("Updating of data is in progress..",'N');
    	String strPRDCD = tblLOTRC.getValueAt(0,TB1_PRDCD).toString().trim();
    	int L_intNXTNO = 0 ;
    	String L_strTEMP ="";
    	String L_strCODCD = cl_dat.M_strFNNYR1_pbst.substring(3,4) +"51";   // Code for Reclassification Grade change.
    	strRCLDOC = cl_dat.getPRMCOD("CMT_CCSVL","DOC","FGXXGCH",L_strCODCD);
    	L_intNXTNO = Integer.parseInt(strRCLDOC.trim())+1;
    	L_strTEMP = String.valueOf(L_intNXTNO);	
    	int L_intLEN = 5- L_strTEMP.trim().length();
    	for(int i=0;i< L_intLEN;i++)
    	{
    		 L_strTEMP = "0"+L_strTEMP;
    	}
    	strRCLDOC = L_strCODCD +L_strTEMP;
    	M_strSQLQRY = "UPDATE CO_CDTRN SET CMT_CCSVL = '"+L_strTEMP+"', CMT_TRNFL ='0'";
    	M_strSQLQRY +=" WHERE CMT_CGMTP ='DOC' and CMT_CGSTP ='FGXXGCH' and CMT_CODCD ='"+L_strCODCD.trim()+"'";
    	cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	setMSG("Generating the Reclassification Document number ..",'N');
    	M_strSQLQRY  = "INSERT INTO PR_LTMST(LT_PRDTP,LT_LOTNO,LT_PRDCD,LT_CPRCD,LT_TPRCD,LT_RUNNO,LT_LINNO,LT_CYLNO,";
    	M_strSQLQRY += "LT_PSTDT,LT_PENDT,LT_PRDQT,LT_BAGQT,LT_DSPQT,LT_RETQT,LT_CLSFL,LT_CLSBY,LT_CLSTM,";
    	M_strSQLQRY += "LT_PPRCD,LT_STSFL,LT_TRNFL,LT_LUSBY,LT_LUPDT,LT_RCLNO)";
    	M_strSQLQRY += "select LT_PRDTP,LT_LOTNO,";
    	M_strSQLQRY += "'"+ txtNWPRD.getText().trim()+"',";
    	M_strSQLQRY += "'"+ txtNWPRD.getText().trim()+"',";	
    	M_strSQLQRY += "LT_TPRCD,LT_RUNNO,LT_LINNO,LT_CYLNO,LT_PSTDT,LT_PENDT,LT_PRDQT,0,0,LT_RETQT,LT_CLSFL,";
    	M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";	
        M_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtNWDAT.getText().trim()))+"',";
    	M_strSQLQRY += "'"+ strLSTPRD+"',";	
    	M_strSQLQRY += "LT_STSFL,";
    	M_strSQLQRY += "'0',";	
    	M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";	
    	M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
    	M_strSQLQRY += "'"+strCURRC+"'";  // class method can't be used as ) or , not reqd.
    	M_strSQLQRY += " From PR_LTMST where LT_RCLNO = '"+strLSTRCL +"'";
    	M_strSQLQRY += " AND LT_LOTNO = '"+txtRCLOT.getText() +"'  and SUBSTRING(lt_lotno,1,3) not in "+strSCPLT_fn;
    	setMSG("Inserting new record in Lot master..",'N');
		//System.out.println(M_strSQLQRY);
    	cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    	String L_strISSTS ="2"; // authorised
    	M_strSQLQRY = "INSERT INTO FG_ISTRN(IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDCD,IST_PRDTP,IST_LOTNO,";
    	M_strSQLQRY +="IST_PKGTP,IST_MNLCD,IST_ISSDT,IST_AUTDT,IST_ISSQT,IST_ISSPK,IST_STKTP,";
    	M_strSQLQRY +="IST_STSFL,IST_TRNFL,IST_LUSBY,IST_LUPDT,IST_TDSFL,IST_PKGCT,";
    	M_strSQLQRY +="IST_RCLNO,IST_SALTP,IST_MKTTP)";
    	M_strSQLQRY +=" SELECT ST_WRHTP,"; // ST_WRHTP
    	M_strSQLQRY +="'"+strRCRCT_fn+"',";  // Issue Type '51'
    	M_strSQLQRY +="'"+strRCLDOC+"',"; // Issue No. - reclassification doc no.
    	M_strSQLQRY +="ST_PRDCD,ST_PRDTP,ST_LOTNO,ST_PKGTP,ST_MNLCD,";
        M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
        M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
    	M_strSQLQRY +="ST_STKQT,(ST_STKQT/ST_PKGWT)ST_ISSPK,";// Issue qty,Issue pkg
    	M_strSQLQRY +="' ',"; // Stock type
    	M_strSQLQRY +="'"+L_strISSTS+"',"; // Issue No. - reclassification doc no.
    	M_strSQLQRY +="'0',"; // Transaction flag
    	M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',"; // Last used by
        M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
    	M_strSQLQRY +="' ',' ',"; // TDS Flag , package category
    	M_strSQLQRY +="'"+strLSTRCL+"',"; // Last used by
    	M_strSQLQRY +="' ',"; // Sale type
    	M_strSQLQRY +="'"+" "+"'";    // market type
    	M_strSQLQRY +=" FROM FG_STMST WHERE ST_LOTNO ='"+txtRCLOT.getText().trim()+"'";
    	M_strSQLQRY +=" AND ST_RCLNO = '"+strLSTRCL +"'"; 
    	M_strSQLQRY +=" AND ST_STKQT > 0";
    	setMSG("Inserting new record in Issue Transaction..",'N');
		//System.out.println(M_strSQLQRY);
    		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    
    	String L_strRCTST ="2" ; // Authorised
    	M_strSQLQRY  =  "INSERT INTO FG_RCTRN(RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,RCT_LOTNO,";
    	M_strSQLQRY += "RCT_MNLCD,RCT_RCTDT,RCT_ISSRF,RCT_STKTP,RCT_RCTQT,RCT_RCTPK,RCT_AUTBY,RCT_AUTDT,";
    	M_strSQLQRY += "RCT_STSFL,RCT_TRNFL,RCT_LUSBY,RCT_LUPDT,RCT_PKGTP,RCT_SHFCD,RCT_PKGCT,RCT_PTFRF,";
    	M_strSQLQRY += "RCT_PRDCD,RCT_RCLNO)";
    	M_strSQLQRY +=" SELECT ST_WRHTP,";        // ST_WRHTP
    	M_strSQLQRY +="'"+strRCRCT_fn+"',";		// LM_TRNTP ='51' reciept type
    	M_strSQLQRY +="'"+strRCLDOC+"',";       // Reclassification Doc No. 
    	M_strSQLQRY +="ST_PRDTP,ST_LOTNO,ST_MNLCD,";
        M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
    	M_strSQLQRY +="' ',";	// RCT_ISSRF
    	M_strSQLQRY +="' ',";   // RCT_STKTP
    	M_strSQLQRY +="ST_STKQT,(ST_STKQT/ST_PKGWT) RCT_RCTPK,";		// RCT_RCTPK
    	M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";				// RCT_AUTBY - Current User
        M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
    	M_strSQLQRY +="'"+L_strRCTST+"',";										// STSFL '2' Authorised
    	M_strSQLQRY +="'0',";						// TRNFL
    	M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";				// Last User
        M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
    	M_strSQLQRY +="ST_PKGTP,";					// Package type
    	M_strSQLQRY +="' ',' ',' ',";				// Shift code,package cat.,PTFRF
    	M_strSQLQRY +="'"+strPRDCD+"',";				// product code
    	M_strSQLQRY += "'"+strCURRC.trim()+"'";
    	M_strSQLQRY +=" FROM FG_STMST WHERE ST_LOTNO ='"+txtRCLOT.getText().trim()+"' AND ST_RCLNO ='"+strLSTRCL.trim() +"'";                                                          
    	M_strSQLQRY += " AND ST_STKQT >0";
		//System.out.println(M_strSQLQRY);
    	
    	setMSG("Inserting new record in Reciept..",'N');
    		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    int L_CNT =0;
    if(flgTSTCP)
    {
        M_strSQLQRY = "INSERT INTO QC_PSMST(PS_QCATP,PS_TSTTP,PS_TSTNO,PS_TSTDT,PS_MORTP,PS_PRDTP,PS_LOTNO,PS_RCLNO,";
		M_strSQLQRY +="PS_LINNO,";
		for(int i=0;i<20;i++)// 20 is the no. of rows in test table
		{
			if(tblTSTRC.getValueAt(i,TB3_QPRCD).toString().trim().length()>0)
			{
				L_CNT++;
				if(tblTSTRC.getValueAt(i,TB3_CMPVL).toString().trim().length()>0)
				{
					M_strSQLQRY +="PS_"+tblTSTRC.getValueAt(i,TB3_QPRCD).toString()+"VL,";
				}
			}
		}
		M_strSQLQRY += "PS_TSTBY,PS_STSFL,PS_TRNFL,PS_LUPDT,PS_LUSBY)";
		M_strSQLQRY +=" SELECT ";
		M_strSQLQRY +="'"+cmbRCPRD.getSelectedItem().toString().substring(0,2)+"',";
		M_strSQLQRY +="'"+strCMPTP_fn+"',";
		M_strSQLQRY +="PS_TSTNO,";
	    M_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDAT.parse(txtNWDAT.getText().trim()))+"',";
		M_strSQLQRY +="PS_MORTP,PS_PRDTP,PS_LOTNO,";
		M_strSQLQRY +="'"+strCURRC+"',";
		M_strSQLQRY +="PS_LINNO,";
		for(int i=0;i<20;i++)
		{
			if(tblTSTRC.getValueAt(i,TB3_QPRCD).toString().trim().length()>0)
			{
				if(tblTSTRC.getValueAt(i,TB3_CMPVL).toString().trim().length()>0)
					M_strSQLQRY +=tblTSTRC.getValueAt(i,TB3_CMPVL).toString()+",";
			}
		}
		M_strSQLQRY +="PS_TSTBY,'3','0',"; // status 3, composite entry,trn fl
		M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
		M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"'";
		M_strSQLQRY +=" FROM QC_PSMST WHERE PS_LOTNO ='"+txtRCLOT.getText().trim()+"'";
		M_strSQLQRY +=" AND PS_TSTTP = '"+strCMPTP_fn +"'"; 
		M_strSQLQRY +=" AND PS_RCLNO = '"+strLSTRCL +"'"; 
		setMSG("Inserting new record in Test table..",'N');
		//System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    }
		M_strSQLQRY = "INSERT INTO FG_STMST(ST_WRHTP,ST_PRDTP,ST_LOTNO,";
		M_strSQLQRY +="ST_MNLCD,ST_RCTDT,ST_TPRCD,ST_CPRCD,";
		M_strSQLQRY +="ST_TRNFL,ST_STSFL,ST_LUSBY,ST_LUPDT,ST_ALOQT,ST_PRDCD,";
		M_strSQLQRY +="ST_PKGCT,ST_DOSQT,ST_UCLQT,ST_RCLNO,ST_DOUQT,ST_PKGTP,ST_PKGWT,ST_REMDS,ST_STKQT,ST_RESFL)";
		M_strSQLQRY +=" SELECT ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_MNLCD,ST_RCTDT,";
		M_strSQLQRY +="'"+strLSTPRD+"',";
		M_strSQLQRY +="'"+strPRDCD+"',";
		M_strSQLQRY +="ST_TRNFL,ST_STSFL,";
		M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
		M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
		M_strSQLQRY +="ST_ALOQT,";
		M_strSQLQRY +="'"+strPRDCD+"',";
		M_strSQLQRY +="ST_PKGCT,"; 
		M_strSQLQRY +="0,isnull(ST_STKQT,0),";    // ST_DOSQT,ST_UCLQT 
		M_strSQLQRY +="'"+strCURRC+"',";
		M_strSQLQRY +="0,";						// ST_DOUQT changed on 23/08/2002 as per the specs ( 0 instead of ST_DOUQT)
		M_strSQLQRY +="ST_PKGTP,ST_PKGWT,ST_REMDS,";
		M_strSQLQRY +="0,";						// ST_STKQT
		M_strSQLQRY +="ST_RESFL";						// ST_STKQT
		M_strSQLQRY +=" FROM FG_STMST WHERE ST_LOTNO ='"+txtRCLOT.getText().trim()+"'";
		M_strSQLQRY +=" AND ST_RCLNO = '"+strLSTRCL +"'"; 
		setMSG("Inserting new record in Stock master..",'N');
		//System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");

    	if(cl_dat.M_flgLCUPD_pbst)
    	{
    	    ResultSet L_RSLSET;
        	String L_STLSQL  ="";
    		M_strSQLQRY = "SELECT * FROM PR_LTMST WHERE LT_LOTNO ='"+txtRCLOT.getText().trim()+"'";
    		M_strSQLQRY +=" and SUBSTRING(lt_lotno,1,3) not in "+strSCPLT_fn+" AND LT_RCLNO = '"+strLSTRCL +"'"; 
    	    L_RSLSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
    		if(L_RSLSET !=null)
    		{
        		if(L_RSLSET.next())	
        		{
        			M_strSQLQRY = "UPDATE PR_LTMST SET ";
        			M_strSQLQRY +="LT_CLSRF = '"+nvlSTRVL(L_RSLSET.getString("LT_CLSRF"),"")+"',";
        			M_strSQLQRY +="LT_PCLBY = '"+nvlSTRVL(L_RSLSET.getString("LT_PCLBY"),"")+"',";												 
        			M_strSQLQRY +="LT_ADDBY = '"+nvlSTRVL(L_RSLSET.getString("LT_ADDBY"),"")+"',";
        		    M_strSQLQRY +="LT_ENDBY = '"+nvlSTRVL(L_RSLSET.getString("LT_ENDBY"),"")+"',";
        			M_strSQLQRY +="LT_REMDS = '"+nvlSTRVL(L_RSLSET.getString("LT_REMDS"),"")+"',";
                    tmsTEMP = L_RSLSET.getTimestamp("LT_PCLTM");
        			if(tmsTEMP !=null)
        			    L_strTEMP = M_fmtLCDTM.format(tmsTEMP);
                    M_strSQLQRY +="LT_PCLTM = '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strTEMP))+"',";			
        		    
        		    tmsTEMP = L_RSLSET.getTimestamp("LT_ADDTM");
        			if(tmsTEMP !=null)
        			    L_strTEMP = M_fmtLCDTM.format(tmsTEMP);
                    M_strSQLQRY +="LT_ADDTM = '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strTEMP))+"',";			
                    
                    tmsTEMP = L_RSLSET.getTimestamp("LT_ENDTM");
        			if(tmsTEMP !=null)
        			    L_strTEMP = M_fmtLCDTM.format(tmsTEMP);
                    M_strSQLQRY +="LT_ENDTM = '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strTEMP))+"',";			
        		    
        		    tmsTEMP = L_RSLSET.getTimestamp("LT_BSTDT");
        			if(tmsTEMP !=null)
        			    L_strTEMP = M_fmtLCDTM.format(tmsTEMP);
                    M_strSQLQRY +="LT_BSTDT = '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strTEMP))+"',";			
        		    
        		    tmsTEMP = L_RSLSET.getTimestamp("LT_BENDT");
        			if(tmsTEMP !=null)
        			    L_strTEMP = M_fmtLCDTM.format(tmsTEMP);
                    M_strSQLQRY +="LT_BENDT = '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strTEMP))+"',";			
        		    
        			M_strSQLQRY +="LT_IPRDS = '"+nvlSTRVL(L_RSLSET.getString("LT_IPRDS"),"")+"',";
        			M_strSQLQRY +="LT_TRNFL = '0'";
        			M_strSQLQRY += " Where LT_LOTNO = '"+txtRCLOT.getText().trim() +"'";
        			M_strSQLQRY += " AND LT_RCLNO = '"+strCURRC +"'";
        		}
        		L_RSLSET.close();
    		}
    		setMSG("Updating the Lot data..",'N');
			//System.out.println(M_strSQLQRY);
    		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        	
    		//updPRVLT(strPTFLOT,LM_LSTRCL);
    		M_strSQLQRY = " UPDATE PR_ltmst set LT_RETQT =0,LT_TRNFL ='0' where ";
            M_strSQLQRY += "LT_RCLNO = '"+strLSTRCL.trim() +"'";
            M_strSQLQRY += " AND LT_LOTNO = '"+txtRCLOT.getText().trim() +"'";
        	setMSG("Updating previous Lot data..",'N');
        	cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    		
    		//updSTMST(strPTFLOT,LM_LSTRCL);                    
    		M_strSQLQRY = " UPDATE FG_stmst set ST_STKQT =0,ST_TRNFL ='0',";
    	  	M_strSQLQRY +="ST_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
    	    M_strSQLQRY += "ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
    		M_strSQLQRY +=" where ST_LOTNO ='"+txtRCLOT.getText().trim()+"'";
    		M_strSQLQRY +=" AND ST_RCLNO ='"+strLSTRCL.trim()+"'";
			//System.out.println(M_strSQLQRY);
    		setMSG("Updating Stock data..",'N');
    		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    		//
    		if(cl_dat.M_flgLCUPD_pbst)
    		{
    			cl_dat.exeDBCMT("");
    			setMSG("Lot has been Reclassified",'N');
    			genRCEML();
    			setMSG("Lot Reclassified and E-mail has been sent to QCA.",'N');
    		}
    		else
    		{
    			setMSG("Error in updating Reclassification data..",'E');
    		}
    	}
    	else
    	{
    		setMSG("Error in Reclassification..",'E');
    	}
    	}
    	catch(Exception L_E)
    	{
    	    setMSG(L_E,"exeRCLLOT");
    	}
    	
}
private void genRCEML()
{
	String L_strEMLID ="";
	try
	{
	FileOutputStream fosREPORT = new FileOutputStream(cl_dat.M_strREPSTR_pbst+"QC_TSTDT.doc");
	DataOutputStream dosREPORT = new DataOutputStream(fosREPORT);
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("Following Lot has been Reclassified.The details has been given below.");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("LOT NUMBER	:  "+txtRCLOT.getText().trim());
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("RCL NUMBER   :  "+strCURRC.trim());
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("New  Grade   :  "+txtNWGRD.getText().trim());
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("Parameter values");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("----------------");
	dosREPORT.writeBytes("\n");
	for(int i=0;i<20;i++)
	{
			if(tblTSTRC.getValueAt(i,TB3_QPRCD).toString().trim().length()>0)
			{   
				dosREPORT.writeBytes(tblTSTRC.getValueAt(i,TB3_QPRCD).toString().trim());
				dosREPORT.writeBytes("\t"+tblTSTRC.getValueAt(i,TB3_CMPVL).toString());
				dosREPORT.writeBytes("\n");
			}
		}	
	dosREPORT.writeBytes("----------------");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("You can Update Your Register.");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("K.V.Mujumdar");
	dosREPORT.writeBytes("\n");
	dosREPORT.writeBytes("DCE(Operations)");
	dosREPORT.writeBytes("\n");
	dosREPORT.close();
	fosREPORT.close();
	cl_eml ocl_eml = new cl_eml();
	M_strSQLQRY="Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='EML' and CMT_CGSTP='FGXXGCH'";
	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
	if(M_rstRSSET !=null)
	while(M_rstRSSET.next())	
	{
		L_strEMLID = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
		if(L_strEMLID.length() >0)
			ocl_eml.sendfile(L_strEMLID.trim(),cl_dat.M_strREPSTR_pbst+"QC_TSTDT.doc","Lot Reclassification","Information of Reclassified Lot. ");
					
	}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"genRCLEML");
	}
}
public void exeRCLCHK()
{
	flgOUTRG = false;
    LM_BLKVL = false;
    String L_strFLAG ="";
	for(int i=0;i<intRCQPCT;i++)// intRCQPCT - Reclassification QPRCNT
	{
		L_strFLAG = tblTSTRC.getValueAt(i,TB3_FLAG).toString().trim();
		if(L_strFLAG.trim().length() >0)
		{
			if(L_strFLAG.equals("*"))
			{
				flgOUTRG = true;			// Out of Range
			}
            if(tblTSTRC.getValueAt(i,TB3_CMPVL).toString().equals(""))
			{
                LM_BLKVL = true;                        // Blank test value
			}
		}
	}
	if(!flgOUTRG)
	{
		if(strCURSTK > 0)
		{
			if(flgRESFL)
			{
				setMSG("Lot is Reserved, cannot Reclassify..",'E');
				cl_dat.M_btnSAVE_pbst.setEnabled(false);
			}
			else		
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
		}
		else
		{
			setMSG("Classified stock is zero,Reclassification option not available ..",'E');
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
		}
	}
	else
	{
		cl_dat.M_btnSAVE_pbst.setEnabled(false);
		if(!flgTSROK)// Test reclassification OK
			setMSG("Specifications are out of range and some of the test values are not available..",'E');
		else
			setMSG("Specifications are out of range..",'E');
	}
}
public boolean chkOPTSEL()
{
	boolean L_RTNVL = true;
	String L_MSG ="";
	try
	{
	   /// LM_SPNAPP = chkSPCN(txtNWPRD.getText().trim());
		JOptionPane JOptionPane = new JOptionPane();
		if(!strLSTPRD.trim().substring(0,4).equals(txtNWPRD.getText().trim().substring(0,4)))
        {
		int L_SELOPT = JOptionPane.showConfirmDialog(null,"Grade belongs to different category,Do you want to proceed ?", "Confirm",JOptionPane.YES_NO_OPTION);
		if(L_SELOPT == 0)
			L_RTNVL = true;	
		else if(L_SELOPT == 0)
			L_RTNVL = false;
		}
		if(!flgTSTCP)
		{
			int L_SELOPT = JOptionPane.showConfirmDialog(null,"Test results not available,Do you want to proceed ?", "Confirm",JOptionPane.YES_NO_OPTION);
			if(L_SELOPT == 0)
			{
				L_RTNVL = true;	
				return true;
			}
			else if(L_SELOPT == 0)
			{
				L_RTNVL = false;
				return false;
			}
		}
       // if(LM_SPNAPP)
      //  {
           L_MSG="";
           if (!flgTSROK)
              L_MSG = "Specs out of range";
           if (!flgTSTCP)
              L_MSG = "No Test results in source";
           if(!L_MSG.equals(""))
              {
                  JOptionPane.showMessageDialog(this,L_MSG,"Error Message",JOptionPane.INFORMATION_MESSAGE);
                  L_RTNVL = false;
				  return false;
              }
           if (LM_BLKVL)
               JOptionPane.showMessageDialog(this,"Blank test Results found, Please note","Error Message",JOptionPane.INFORMATION_MESSAGE);
      //  }
	}
	catch(Exception L_E)
	{
		
	}
	return L_RTNVL;
}
public void chkPTFTXT(String LP_PTFMSG)
	{
		try
		{
			File F = new File("F:\\toho\\fims\\chkptf.txt");
			if(LP_PTFMSG.equals("IN")){
				if(!F.exists()){
					FileOutputStream O_FOUT = new FileOutputStream("F:\\toho\\fims\\chkptf.txt");
					DataOutputStream O_DOUT = new DataOutputStream(O_FOUT);
					O_DOUT.writeBytes("PTF in Progress");
					O_DOUT.close();
					O_FOUT.close();
				}
			}
			else if(LP_PTFMSG.equals("OUT")){
				if(F.exists()){
					F.delete();
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"chkPTFTXT");
		}
	}
private void getPTFREC()
{
	try
	{
		this.setCursor(cl_dat.M_curWTSTS_pbst);
		LM_CNT = 0;
		String L_strCPRDS,L_strPKGDS,L_strPRDCD;
		setMSG("Fetching Records in Progress... ",'N');
		if(strPTFCT.equals("01"))
		{
			M_strSQLQRY = "Select ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_TPRCD,LT_PRDCD,ST_PKGTP,LT_CLSFL,LT_REMDS,";
			M_strSQLQRY += "sum(ST_UCLQT) L_UCLQT from FG_STMST,PR_LTMST where ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO";
			M_strSQLQRY += " and ST_RCLNO=LT_RCLNO and ST_RCLNO='00' and ST_UCLQT > 0 and LT_STSFL <> '7'";
                            M_strSQLQRY += " and ((ST_PRDTP='01' and LT_CLSFL='9') or (ST_PRDTP='01' and SUBSTRING(ST_PRDCD,1,3)='512' and LT_CLSFL in ('0','1','9')) or (ST_PRDTP in ('02','04','05') and LT_CLSFL in ('0','1','9')))";
			M_strSQLQRY += " group by ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_TPRCD,LT_PRDCD,LT_CLSFL,LT_REMDS";
			M_strSQLQRY += " order by ST_WRHTP,ST_PRDTP,LT_PRDCD,ST_LOTNO,ST_RCLNO,ST_PKGTP";
			
		}
		else if(strPTFCT.equals("02"))
		{
			M_strSQLQRY = "Select ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_TPRCD,LT_PRDCD,ST_PKGTP,LT_CLSFL,LT_REMDS,";
			M_strSQLQRY += "sum(ST_UCLQT) L_UCLQT from FG_STMST,PR_LTMST where ST_PRDTP=LT_PRDTP";
			M_strSQLQRY += " and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and ST_RCLNO > '00' and ST_UCLQT > 0";
            M_strSQLQRY += " and ((ST_PRDTP='01' and LT_CLSFL='9') or (ST_PRDTP in ('02','04','05') and LT_CLSFL in ('0','1','9')))";
			M_strSQLQRY += " group by ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_TPRCD,LT_PRDCD,LT_CLSFL,LT_REMDS";
			M_strSQLQRY += " order by ST_WRHTP,ST_PRDTP,LT_PRDCD,ST_LOTNO,ST_RCLNO,ST_PKGTP";
		}
		else if(strPTFCT.equals("03"))
		{
			M_strSQLQRY = "Select ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_TPRCD,LT_PRDCD,ST_PKGTP,LT_CLSFL,LT_REMDS,";
			M_strSQLQRY += "sum(ST_UCLQT) L_UCLQT from FG_STMST,PR_LTMST where ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO";
			M_strSQLQRY += " and ST_RCLNO=LT_RCLNO and ST_RCLNO='00' and ST_UCLQT > 0 and LT_STSFL='7'";
            M_strSQLQRY += " and ((ST_PRDTP='01' and LT_CLSFL='9') or (ST_PRDTP in ('02','04','05') and LT_CLSFL in ('0','1','9')))";
			M_strSQLQRY += " group by ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_TPRCD,LT_PRDCD,LT_CLSFL,LT_REMDS";
			M_strSQLQRY += " order by ST_WRHTP,ST_PRDTP,LT_PRDCD,ST_LOTNO,ST_RCLNO,ST_PKGTP";
		}
		cl_dat.M_btnSAVE_pbst.setEnabled(true);
		int i=0;
		//System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
	    if(M_rstRSSET !=null)
	    {		
			while(M_rstRSSET.next())
			{
				strPKGTP = M_rstRSSET.getString("ST_PKGTP");
                AR_PKGTP[i] = strPKGTP;
				strTPRDS = getPRDDS(M_rstRSSET.getString("ST_TPRCD"));
				L_strPRDCD = M_rstRSSET.getString("LT_PRDCD");
				L_strCPRDS = getPRDDS(M_rstRSSET.getString("LT_PRDCD"));
			    LM_CLSFL = M_rstRSSET.getString("LT_CLSFL");
			    AR_CLSFL[i] = LM_CLSFL;
				strPTFLOT =M_rstRSSET.getString("ST_LOTNO");
				strPTFRCL =M_rstRSSET.getString("ST_RCLNO");
				tblPTFDT.setValueAt(strPTFLOT,i,TB4_LOTNO);
				tblPTFDT.setValueAt(strPTFRCL,i,TB4_RCLNO);
				tblPTFDT.setValueAt(M_rstRSSET.getString("ST_WRHTP"),i,TB4_WRHTP);
				tblPTFDT.setValueAt(M_rstRSSET.getString("ST_PRDTP"),i,TB4_PRDTP);
				tblPTFDT.setValueAt(M_rstRSSET.getString("ST_TPRCD"),i,TB4_OPRCD);
				tblPTFDT.setValueAt(M_rstRSSET.getString("LT_PRDCD"),i,TB4_PRDCD);
				tblPTFDT.setValueAt(strTPRDS,i,TB4_TPRCD);
				tblPTFDT.setValueAt(L_strCPRDS,i,TB4_CPRCD);
				AR_PRDCD[i] = L_strPRDCD;
				tblPTFDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("LT_REMDS"),""),i,TB4_REMDS);
				L_strPKGDS = cl_dat.getPRMCOD("CMT_CODDS","SYS","FGXXPKG",strPKGTP);
				tblPTFDT.setValueAt(L_strPKGDS,i,TB4_PKGTP);
				tblPTFDT.setValueAt(strPKGTP,i,TB4_PKGCD);
				strUCLQT = M_rstRSSET.getString("L_UCLQT");
				tblPTFDT.setValueAt(strUCLQT,i,TB4_UCLQT);
				dblTOUQT = dblTOUQT + Double.parseDouble(strUCLQT);
				i++;
			}
	    }
	 	//strTOTQT = setFMT("RND",String.valueOf(dblTOUQT),3);
		strTOTQT = setNumberFormat(dblTOUQT,3);
		//txtPTFQT.setText(strTOTQT);
		txtBALQT.setText(strTOTQT);
		//strUCLQT = txtPTFQT.getText().toString().trim();
		strUCLQT = txtBALQT.getText().toString().trim();
		if(i > 0)
		    setMSG("Click Lot No. of which PTF is to be generated.",'N');
		else
		    setMSG("No Lot is pending for PTF in this category..",'N');
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}catch(Exception L_EX){
		setMSG(L_EX,"getPTFREC");
	}
}
private void genPTFNO()
{
	try
	{
		int L_intPTFNO = 0;
		String L_strTEMP = "";
		strPTFNO = "";
	//	System.out.println(" fn year "+cl_dat.M_strFNNYR1_pbst);
		String L_strPREFX = cl_dat.M_strFNNYR1_pbst.substring(3,4)+"00";
	//	System.out.println("prefix"+L_strPREFX);
		M_strSQLQRY = "Select CMT_CCSVL from CO_CDTRN where CMT_CGMTP='DOC'";
                    M_strSQLQRY += " and CMT_CGSTP='FGXXPTF' and CMT_CODCD = '"+L_strPREFX+"'";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		{
    		if(M_rstRSSET.next())
    		{
    			L_intPTFNO = M_rstRSSET.getInt("CMT_CCSVL");	
    		}
			M_rstRSSET.close();
		}
		strPRPTF = String.valueOf(L_intPTFNO);
		L_intPTFNO += 1;
        for(int i = 0;i < (5 - String.valueOf(L_intPTFNO).length());i++)
			L_strTEMP += "0";
		strPTFNO = L_strTEMP + String.valueOf(L_intPTFNO).trim();
		txtPTFNO.setText(strPTFNO);
		txtPTFDT.setText(cl_dat.M_strLOGDT_pbst);
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"genPTFNO");
	}
}
private boolean vldPTFCT()
{
	try
	{
		M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,5,2) L_CODCD,CMT_CHP01 from CO_CDTRN where";
		M_strSQLQRY += " CMT_CGMTP='SYS' and CMT_CGSTP='FGXXCAT' and CMT_CODDS='"+txtPTFCT.getText().trim()+"'";
		M_strSQLQRY += " and cmt_modls like '%"+cl_dat.M_strUSRTP_pbst+"%'";
		//System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET != null)
		if(M_rstRSSET.next())
		{
			strPTFCT = M_rstRSSET.getString("L_CODCD").toString().trim();
			strPTFCHP = M_rstRSSET.getString("CMT_CHP01").toString().trim();
			M_rstRSSET.close();
			return true;
		}
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"vldPTFCT");
		return false;
	}
	setMSG("Invalid PTF Category ..",'E');
	return false;
}
private void genPTFREC()
{
	try
	{
	    if(tblPTFDT.isEditing())
		    tblPTFDT.getCellEditor().stopCellEditing();
		tblPTFDT.setRowSelectionInterval(0,0);
		tblPTFDT.setColumnSelectionInterval(0,0);

	    chkPTFTXT("IN");
	    cl_dat.M_flgLCUPD_pbst = true;
	    String L_strWRHTP,L_strPRDTP,L_strOPRCD,L_strPKGTP,L_strPRDCD,L_strUCLQT,L_strREMDS;		
		String L_YRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4).trim();
		String L_CODCD = L_YRDGT + strDGRTP;
		if(strPTFCHP.trim().equals(strDGRTP))
		{
			strCURTM = "HOLD";
			strPTFUSR = cl_dat.M_strUSRCD_pbst;
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += "CMT_CHP01 = '"+strPTFUSR+"',";
			M_strSQLQRY += "CMT_CHP02 = '"+strCURTM+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
			M_strSQLQRY += " and CMT_CGSTP = 'FGXXDGR'";
			M_strSQLQRY += " and CMT_CODCD = '"+L_CODCD+"'";
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");   
			strPTFRF = L_CODCD + nvlSTRVL(cl_dat.getPRMCOD("CMT_CCSVL","DOC","FGXXDGR",L_CODCD),"");
			//System.out.println("strPTFRF : "+strPTFRF);
		}
		else
			strPTFRF = " ";
		
		cl_dat.M_flgLCUPD_pbst  = true;
		for(int i=0;i<=(tblPTFDT.getRowCount()-1);i++)
		{			
			if(tblPTFDT.getValueAt(i,TB4_STATS).toString().trim().equals("true"))
			{	
			   	if(cl_dat.M_flgLCUPD_pbst)
				{	
					L_strWRHTP = tblPTFDT.getValueAt(i,TB4_WRHTP).toString();
					L_strPRDTP = tblPTFDT.getValueAt(i,TB4_PRDTP).toString();
					L_strOPRCD = tblPTFDT.getValueAt(i,TB4_OPRCD).toString();
					L_strPKGTP = tblPTFDT.getValueAt(i,TB4_PKGCD).toString();
					L_strPRDCD = tblPTFDT.getValueAt(i,TB4_PRDCD).toString();
					strPTFLOT = tblPTFDT.getValueAt(i,TB4_LOTNO).toString();
					strPTFRCL = tblPTFDT.getValueAt(i,TB4_RCLNO).toString();
					L_strUCLQT = tblPTFDT.getValueAt(i,TB4_UCLQT).toString();
					L_strREMDS = tblPTFDT.getValueAt(i,TB4_REMDS).toString();
					setMSG("Updating Stock Master",'N');
    				M_strSQLQRY = "Update FG_STMST set ";
    				M_strSQLQRY += "ST_STKQT = ST_STKQT + ST_UCLQT,";
    				M_strSQLQRY += "ST_UCLQT = 0.000,";
    				M_strSQLQRY += "ST_CPRCD = '"+L_strPRDCD+"',";
    				M_strSQLQRY += "ST_PRDCD = '"+L_strPRDCD+"',";
    				M_strSQLQRY += "ST_TRNFL = '0',";
    				M_strSQLQRY += "ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
    				M_strSQLQRY += "ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
    				M_strSQLQRY += " where ST_WRHTP = '"+L_strWRHTP+"'";
    				M_strSQLQRY += " and ST_PRDTP = '"+L_strPRDTP+"'";
    				M_strSQLQRY += " and ST_LOTNO = '"+strPTFLOT+"'";
    				M_strSQLQRY += " and ST_RCLNO = '"+strPTFRCL+"'";
    				M_strSQLQRY += " and ST_PKGTP = '"+L_strPKGTP+"'";
					//System.out.println(M_strSQLQRY);
    				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					setMSG("Updating Product Master",'N');
					M_strSQLQRY = "Update CO_PRMST set ";
    				M_strSQLQRY += "PR_USTQT = PR_USTQT - "+L_strUCLQT+",";
    				M_strSQLQRY += "PR_CSTQT = PR_CSTQT + "+L_strUCLQT+",";
    				M_strSQLQRY += "PR_TRNFL = '0',";
    				M_strSQLQRY += "PR_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
    				M_strSQLQRY += "PR_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
    				M_strSQLQRY += " where PR_PRDCD = '"+L_strPRDCD+"'";
					//System.out.println(M_strSQLQRY);
    				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					M_strSQLQRY = "Update PR_LTMST set ";
    				M_strSQLQRY += "LT_REMDS = '"+L_strREMDS+"',";
    				M_strSQLQRY += "LT_TRNFL = '0',";
    				M_strSQLQRY += "LT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
    				M_strSQLQRY += "LT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
    				M_strSQLQRY += " where lt_prdtp = '"+L_strPRDTP+"'";
    				M_strSQLQRY += " and lt_lotno = '"+strPTFLOT+"'";
    				M_strSQLQRY += " and lt_rclno = '"+strPTFRCL+"'";
					//System.out.println(M_strSQLQRY);
    				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				    // Check in case of problem, this code is used in 1.2 , seems irrevant
					//strPTFQT = getPTFQT(strPTFLOT,strPTFRCL,strPKGTP);
    				strPTFQT = tblPTFDT.getValueAt(i,TB4_UCLQT).toString();
    				setMSG("Updating PTF Reference Table",'N');
    				M_strSQLQRY = "Insert into FG_PTFRF(PTF_PTFNO,PTF_PRDTP,PTF_LOTNO,";
    				M_strSQLQRY += "PTF_RCLNO,PTF_PKGTP,PTF_OPRCD,PTF_PRDCD,PTF_PTFCT,";
    				M_strSQLQRY += "PTF_PTFDT,PTF_PTFQT,PTF_PTFRF,PTF_PTFBY) values (";
    				M_strSQLQRY += "'"+strPTFNO+"',";
    				M_strSQLQRY += "'"+L_strPRDTP+"',";
    				M_strSQLQRY += "'"+strPTFLOT+"',";
    				M_strSQLQRY += "'"+strPTFRCL+"',";
    				M_strSQLQRY += "'"+L_strPKGTP+"',";
    				M_strSQLQRY += "'"+L_strOPRCD+"',";
    				M_strSQLQRY += "'"+L_strPRDCD+"',";
    				M_strSQLQRY += "'"+strPTFCT+"','";
    				M_strSQLQRY += M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPTFDT.getText().trim()))+"',";
    				M_strSQLQRY += strPTFQT+",";
    				M_strSQLQRY += "'"+strPTFRF+"',";
    				M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"')";
					//System.out.println(M_strSQLQRY);
    				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    			}
			}
		}
		M_strSQLQRY = "Update CO_CDTRN SET ";
		M_strSQLQRY += "CMT_CCSVL = '"+txtPTFNO.getText()+"'";
		M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
		M_strSQLQRY += " and CMT_CGSTP = 'FGXXPTF'";
		M_strSQLQRY += " and CMT_CODCD = '"+ cl_dat.M_strFNNYR1_pbst.substring(3,4)+"00"+"'";
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		if(cl_dat.M_flgLCUPD_pbst)
		{
			if(strPTFCHP.trim().equals(strDGRTP))
			{
				strCURTM = "";
				strPTFUSR = "YES";
				//cl_cust.ocl_cust.updCDTRN("DOC","FGXXDGR",L_CODCD,strCURTM,strPTFUSR);
				M_strSQLQRY = "Update CO_CDTRN set ";
    			M_strSQLQRY += "CMT_CHP01 = '"+strPTFUSR+"',";
    			M_strSQLQRY += "CMT_CHP02 = '"+strCURTM+"'";
    			M_strSQLQRY += " where CMT_CGMTP = 'DOC'";
    			M_strSQLQRY += " and CMT_CGSTP = 'FGXXDGR'";
    			M_strSQLQRY += " and CMT_CODCD = '"+L_CODCD+"'";
    			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");   
			}
		}
		if(cl_dat.M_flgLCUPD_pbst)
		{
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
    			JOptionPane.showMessageDialog(this,"PTF has been Saved Successfully","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
    		    setMSG("PTF Reference has been saved Successfully.",'N');
        		txtPTFNO.setText("");
        	    txtPTFDT.setText(cl_dat.M_strLOGDT_pbst);
        		txtPTFQT.setText("");
        		txtBALQT.setText("");
        		txtPTFCT.setText("");
        		tblPTFDT.clrTABLE();
        		if(tblPTFDT.isEditing())
	            {
	                tblPTFDT.getCellEditor().stopCellEditing();
	                tblPTFDT.setRowSelectionInterval(0,0);
	                tblPTFDT.setColumnSelectionInterval(0,0);
	            }
	            strPRPTF = strPTFNO;
	            strPRVDT = txtPTFDT.getText().trim();
        		strPTFNO = String.valueOf(Integer.parseInt(strPTFNO) +1);
        		txtPTFNO.setText(strPTFNO);
        		chkPTFTXT("OUT");
			}
			else
			{
			    setMSG("Error in generating PTF ..",'E');
			}
		}
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"genPTFREC");					   
		cl_dat.M_flgLCUPD_pbst = false;
	}
}
private void modLINCNT()
{
	try
	{
		String L_MNPRD = "";
		String L_SBPRD = "";
		String L_PRVMNP = "";
		String L_PRVSBP = "";
		String L_PRVPRD = "";
		String L_PRDCD ="";
		int L_CNT = 0;
		for(int i = 0;i <= (tblPTFDT.getRowCount()-1);i++)
		{
			if(tblPTFDT.getValueAt(i,TB4_STATS).toString().trim().equals("true"))
			{	
				L_PRDCD = AR_PRDCD[i].toString().trim();
				L_MNPRD = L_PRDCD.substring(0,2).trim();
				L_SBPRD = L_PRDCD.substring(0,4).trim();
				if(!L_MNPRD.equals(L_PRVMNP))
				{
					L_CNT += 3;
					L_PRVMNP = L_MNPRD;
				}
				if(!L_SBPRD.equals(L_PRVSBP))
				{
					L_CNT += 3;
					L_PRVSBP = L_SBPRD;
				}
				if(!L_PRDCD.equals(L_PRVPRD))
				{
					L_CNT += 2;
					L_PRVPRD = L_PRDCD;
				}
				else
					L_CNT += 1;
			}
		}
		if(66-(22+L_CNT) > 0)
			LM_DSPLOT++;
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX,"modLINCNT");
	}
}
	private void calCLSQT()
    {  
		try
        {
			dblDUMQT = 0;
			dblDRTQT = 0;
			strRCTQT = "";
			for(int i = 0;i<tblPTFDT.getRowCount();i++)
            {  
				if(tblPTFDT.getValueAt(i,TB4_STATS).toString().trim().equals("true"))
                {	
					strRCTQT = tblPTFDT.getValueAt(i,TB4_UCLQT).toString().trim();
					dblDRTQT = Double.parseDouble(strRCTQT);
					dblDRTQT = dblDRTQT + dblDUMQT;
					dblDUMQT = dblDRTQT;
				}
			}
			double L_UCLQT = Double.parseDouble(strUCLQT);
			txtBALQT.setText(setNumberFormat(L_UCLQT-dblDUMQT,3));
			txtPTFQT.setText(setNumberFormat(dblDUMQT,3));
		}
    catch(Exception L_EX)
    {
		setMSG(L_EX,"calCLSQT");
    }
    }
  	private int getCLKREC()
	{
		LM_COUNT = 0;
		try
		{
			for(int i=0;i<=(tblPTFDT.getRowCount()-1);i++)
			{			
				if(tblPTFDT.getValueAt(i,TB4_STATS).toString().trim().equals("true"))
				{	
					LM_COUNT++;
			    }
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCLKREC");
		}
		return LM_COUNT;
	}
	// Method added on 18/04/07 to resolve B Grade classification issue
	void 	chkQPRVTR(String P_strPRDCD)
	{
		try
		{
			int L_intCNT =0;
		 	M_strSQLQRY = "SELECT QP_QPRCD,QP_ORDBY FROM CO_QPMST WHERE " 
	 		+ " QP_QCATP = "+ "'"+cmbPRDTP.getSelectedItem().toString().substring(0,2) + "'"
			+ " AND QP_PRDCD ='"+P_strPRDCD +"'"
	            +" and QP_SRLNO = " + "'" + strDFLSR_fn + "'"
         		+" and QP_ENDDT is null and QP_ORDBY is not null "
      	      +" order by QP_ORDBY";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_QPRCD = nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),"");
        			if(L_intCNT < vtrQPRCD.size())
				{
    					//System.out.println("Element "+L_intCNT +" "+vtrQPRCD.elementAt(L_intCNT)+" is  replacd by "+ L_QPRCD);
					vtrQPRCD.setElementAt(L_QPRCD.trim(),L_intCNT);
				}
    				else
				{
    					vtrQPRCD.addElement(L_QPRCD.trim());
					//System.out.println("Element added "+L_QPRCD);
				}
				L_intCNT ++;
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"chkQPRVTR");
		}
	}
}
