import java.sql.*;import java.util.Hashtable;
import javax.swing.JButton;import javax.swing.JTabbedPane;import javax.swing.JPanel;import javax.swing.JComponent;import javax.swing.JLabel;import javax.swing.JTextArea;
import javax.swing.JTextField;import javax.swing.JOptionPane;import javax.swing.JTable;import javax.swing.JComboBox;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.InputVerifier;
import java.util.Vector;
class co_teaut extends cl_pbase implements ChangeListener
{
	//private co_teautTBLINVFR objTBLVRF;
	private JTextField txtTEMP;
	private JButton btnRPDC1,btnRPDC2,btnRPDC3;
	private JTabbedPane jtpTRNDT;
	private JPanel pnlGPAPR,pnlGPAUT,pnlINAUT,pnlCLSDT;
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
	private final int TB1_PREBY =10;
	private final int TBL_PREBY =11;
	private final int TBL_VEHNO =12;
	private final int TBL_VEHDS =13;
	private final String strGPAPR_fn = "GP Approval";
	private final String strFNLCL_fn ="Final Classification";
	private final String strPRVCL_fn ="Prov. Classification";
	private final String strDFLSR_fn ="00000";
	private final String strINTRCL_fn ="00";
	/*private int intGPAPR =0;
	private int intGPAUT =1;
	private int intINAPR =2;
	private int intPRVCL =3;
	private int intFNLCL =4;*/
     
    // these are set to 99 , as if access to some tab is not given then these integers were 
    // set and were giving wrong results 
    private int intGPAPR =99;
	private int intGPAUT =99;
	private int intINAPR =99;
	private int intPRVCL =99;
	private int intFNLCL =99;
	
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
		
	/** 
	 * For Using Input verifier, for textfield and JTable,register with the input verifier
	 */

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
		add(new JLabel("Product Type"),1,1,1,1,pnlCLSDT,'L');
		add(new JLabel("LOT No."),1,2,1,0.5,pnlCLSDT,'L');
		add(new JLabel("RCL No."),1,2,1,0.5,pnlCLSDT,'R');
		add(new JLabel("Bagged Qty"),1,3,1,1,pnlCLSDT,'L');
		add(new JLabel("Bagging Grade"),1,4,1,1,pnlCLSDT,'L');
		add(new JLabel("Remarks"),1,5,1,3,pnlCLSDT,'L');
	    add(cmbPRDTP = new JComboBox(),2,1,1,1,pnlCLSDT,'L');
	    add(txtLOTNO = new TxtLimit(8),2,2,1,0.75,pnlCLSDT,'L');
	    add(txtRCLNO = new TxtLimit(2),2,2,1,0.25,pnlCLSDT,'R');
	    add(txtBAGQT = new JTextField(),2,3,1,1,pnlCLSDT,'L');
	    add(txtIPRDS = new JTextField(),2,4,1,1,pnlCLSDT,'L');
	    add(txtREMDS = new TxtLimit(200),2,5,1,2,pnlCLSDT,'L');
		
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
		add(txtADDTS,7,6,3,1.5,pnlCLSDT,'R');
        txtGRBTS = new JTextArea();
		add(txtGRBTS,11,6,3,1.5,pnlCLSDT,'R');
        staGRBDT = new String[50][3];
		staQPRDT = new String[50][3];
        staCMPFL = new String[20];
        add(btnRLS = new JButton("Release"),15,6,1,1,pnlCLSDT,'L');
        txtPPRCD.setInputVerifier(objINPVR);	
        txtCPRCD.setInputVerifier(objINPVR);	
        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
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
		        //tblTSTDT.addKEyListener(this);
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
			if(M_objSOURC == btnRPDC3)
			{
				int i=0;
				mm_rpind objINDRP = new mm_rpind(M_strSBSCD.substring(0,2)+hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString()+"00",hstCODCD.get(tblINAUT.getValueAt(i,TBL_STRTP).toString()).toString());
				for(i=0;i<tblINAUT.getRowCount();i++)
				{
					if(tblINAUT.getValueAt(i,TBL_DSPFL).toString().equals("true"))
					{
						L_strINDNO = tblINAUT.getValueAt(i,TBL_DOCNO).toString();
						break;
					}
				}
				objINDRP.getALLREC(L_strINDNO,L_strINDNO,'I',"PI");
				try
				{
					Runtime r = Runtime.getRuntime();
					Process p = null;
					p  = r.exec("c:\\windows\\wordpad.exe "+"c:\\reports\\mm_rpind.doc"); 
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
					p  = r.exec("c:\\windows\\wordpad.exe "+"c:\\reports\\mm_rpgpp.doc"); 
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
			if((M_objSOURC == txtLOTNO)||(M_objSOURC == txtRCLNO))
			{
           		//exeINTSTA();
        		String L_strLOTNO = txtLOTNO.getText().trim();
        		String L_strRCLNO = txtRCLNO.getText().trim();
        		clrCOMP();
        		txtLOTNO.setText(L_strLOTNO);
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
        			  //txtLOTNO.requestFocus();
        		}
            	this.setCursor(cl_dat.M_curDFSTS_pbst);
           		if(tblTSTDT.isEditing())
        		    tblTSTDT.getCellEditor().stopCellEditing();
        		tblTSTDT.setRowSelectionInterval(0,0);
        		tblTSTDT.setColumnSelectionInterval(0,0);
       			if(chrCLSFL !='Q')
    			{
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
						tblTSTDT.requestFocus();
						tblTSTDT.setRowSelectionInterval(0,0);
						tblTSTDT.setColumnSelectionInterval(TB_CMPVL,TB_CMPVL);

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
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
          }
          /* if(L_AE.getSource().equals(btnEXT))
		    {
			if(cl_dat.ocl_dat.M_USUSRTP.equals("AU1"))
			{
				JOptionPane LM_OPTNPN = new JOptionPane();
				int L_SELOPT = LM_OPTNPN.showConfirmDialog(null,"Do you want to Generate PT Form ?", "Confirm",JOptionPane.YES_NO_OPTION);
				if(L_SELOPT == 0)
				{
					cl_dat.ocl_dat.M_PPPRGCD = "FG_TEPTF";
					if(cl_dat.ocl_dat.getUSRRIT())
					{
						fg_teptf ofg_teptf = new fg_teptf();
						ofg_teptf.show();
						ofg_teptf.setUSRAUT();
					}
				}
			}
			this.dispose();
		    }*/
		}
		catch(Exception L_E)
		{
			setMSG("Error in generating report ..",'E');
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
					P_tblITMDT.setValueAt(M_fmtLCDAT.format(datTEMP),i++,5);
				}
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
										tblINAUT.setValueAt(L_strREMDS,i,TBL_REMDS);
									else if(nvlSTRVL(L_rstRSSET.getString("RM_REMTP"),"").equals("OTH"))
										tblINAUT.setValueAt(L_strREMDS,i,TBL_COMDS);
								}
								L_rstRSSET.close();
							}
							i++;
						}
						M_rstRSSET.close();
					}
				}
			}
			/*else if(M_objSOURC == tblTSTDT)
			{
				if(tblTSTDT.getSelectedColumn()== TB_CMPVL)
        			getGRBARR(tblTSTDT.getSelectedRow());
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
	       if(M_objSOURC == txtREMDS)
	            txtREMDS.transferFocus();
	      // if(M_objSOURC == txtPPRCD)
	        //    txtREMDS.transferFocus();     
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
			    		        
        	    M_strSQLQRY = "select LT_LOTNO,LT_RCLNO,LT_PSTDT,PR_PRDDS,CMT_SHRDS,LT_CLSFL from PR_LTMST,CO_PRMST,co_cdtrn where LT_PRDTP = '";
                M_strSQLQRY += cmbPRDTP.getSelectedItem().toString().substring(0,2) + "' and LT_TPRCD = PR_PRDCD and  " ;
				if(chrCLSFL == 'P')
				M_strSQLQRY += "((LT_CLSFL in('1','3')) OR (LT_CLSFL ='9' and LT_PPRCD is null))";
				else
				M_strSQLQRY += " LT_CLSFL in('0','1','3','4')" ;
				M_strSQLQRY += " AND LT_RCLNO ='"+strINTRCL_fn +"'";
				M_strSQLQRY += " AND cmt_cgmtp ='SYS' and cmt_cgstp = 'QCXXCLS' and LT_CLSFL = cmt_codcd ";
	         	if(txtLOTNO.getText().trim().length() >0)
					M_strSQLQRY += " and LT_LOTNO like '" + txtLOTNO.getText().trim() + "%'  AND LT_STSFL <> 'X' order by LT_CLSFL desc,LT_LOTNO desc";
				else	
					M_strSQLQRY += " order by LT_CLSFL desc,LT_LOTNO desc";
	            M_strHLPFLD = "txtLOTNO";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot Number","Rcl. No.","Lot Start Date  ","  Grade ","Status"},5,"CT");
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
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Rcl. No.","Grade "},2,"CT");
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
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtLOTNO")
		{
		    txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);
		    txtRCLNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtRCLNO.requestFocus();
			/*if(LM_STRHLP !=null)
			{
				st = new StringTokenizer(LM_STRHLP,"|");
				if (st.hasMoreTokens()) {
					 st.nextToken();	
					 L_RCLNO = st.nextToken();
						txtRCLNO.setText(L_RCLNO);
					}	
				LM_RCLNO = L_RCLNO.trim();
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
    //tblTSTDT.cmpEDITR[TB_CMPVL].setEnabled(false);
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
        	strCLSFL =strPRVST_fn;
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
				return false;
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
			strMSG = "Final Classification is done as "+ L_strCPRDS;
			setMSG(strMSG,'E');
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			exeDSBSCR();
			// Functionality for release button 	
			M_strSQLQRY ="Select count(*) L_CNT from FG_PTFRF where "; 
			M_strSQLQRY += " PTF_LOTNO = '"+txtLOTNO.getText().trim()+"' AND PTF_PRDTP  ='"+cmbPRDTP.getSelectedItem().toString()+"'"; 
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
    			{
    			    if(M_rstRSSET.getInt("L_CNT") >0)
    			    {
    				    btnRLS.setVisible(false);
    				    setMSG("PTF has been created, Lot can not be released ..",'E');
    			    }
    			    else if(L_strCLSFL.equals("9"))
    				    btnRLS.setVisible(true);  
    				else
    				    btnRLS.setVisible(false);    
    			}
    			else if(L_strCLSFL.equals("9"))
    				btnRLS.setVisible(true);
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
		}
		L_strTPRDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
		L_strBAGQT = nvlSTRVL(L_rstRSSET.getString("LT_BAGQT"),"");
		L_strPRSTS = nvlSTRVL(L_rstRSSET.getString("PR_STSFL"),"");
		L_strPCLBY = nvlSTRVL(L_rstRSSET.getString("LT_PCLBY"),"");
		Timestamp tmsTEMP = L_rstRSSET.getTimestamp("LT_PCLTM");
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
			if(chrCLSFL !='Q')
			{
				txtREMDS.requestFocus();
			}
			else setMSG(strMSG,'E');	
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
	  			txtPPRCD.requestFocus();
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
		    chkQPRNG(fltNPFVL,fltNPTVL,fltQPRVL,P_intROWNO);
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
private void chkQPRNG(float P_fltNPFVL,float P_fltNPTVL,float P_fltQPRVL,int P_intROWNO)
{
	L_RNGVL ="";
	try
	{
		if ((P_fltNPFVL == 0) && (P_fltNPTVL == 0))
		    tblTSTDT.setValueAt("",P_intROWNO,TB_FLAG);
		else if ((P_fltNPFVL != 0) && (P_fltNPTVL == 0))
		{
		   if (P_fltQPRVL < P_fltNPFVL)
		   {
			   tblTSTDT.setValueAt("*",P_intROWNO,TB_FLAG);
		   }
		   else
		       tblTSTDT.setValueAt("",P_intROWNO,TB_FLAG);
		}
		else if ((P_fltNPFVL == 0) && (P_fltNPTVL != 0))
		{
		   if (P_fltQPRVL > P_fltNPTVL)
		   {
			   tblTSTDT.setValueAt("*",P_intROWNO,TB_FLAG);
			}
		   else
		   {
			   tblTSTDT.setValueAt("",P_intROWNO,TB_FLAG);
		   }
		}
		else
		{
		   if (( P_fltQPRVL < P_fltNPFVL) || (P_fltQPRVL > P_fltNPTVL))
		   {
			   tblTSTDT.setValueAt("*",P_intROWNO,TB_FLAG);
		   }
		   else
		       tblTSTDT.setValueAt("",P_intROWNO,TB_FLAG);
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
             L_strGRBDT = L_strGRBDT + staGRBDT[i][0].trim() + "\t" + padSTRING('R',staGRBDT[i][1].trim().substring(11),11) + staGRBDT[i][2].trim() + "\n";
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
				L_strADDDT = L_strADDDT + staQPRDT[k][0].trim() +"\t"+  padSTRING('R',L_strDATTM.substring(11),11) + staQPRDT[k][2].trim() + "\n";
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
			if(txtPPRCD.getText().length() == 0)
			    return true;
			if((input == txtPPRCD)&&(chrCLSFL =='P'))
			{
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
			if(jtpTRNDT.getSelectedIndex() ==intGPAPR)
			{
				// GP Approval
				for(int i=0;i<tblGPAPR.getRowCount();i++)
				{
					if(tblGPAPR.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intSELCT++;
						if(tblGPAPR.getValueAt(i,TBL_APRFL).toString().equals(""))
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
			return true;
		}
		catch(Exception L_E)
		{
			return false;
		}
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
							   continue; // Next Iteration

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
								L_strEML = M_rstRSSET.getString("US_EMLRF");
								if(L_strEML.length() >0)
									ocl_eml.sendfile(L_strEML,null,"Intimation of Gate Pass Approval","Gate Pass No."+L_strMGPNO + " is Approved by "+cl_dat.M_strUSRCD_pbst);
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
				        clrCOMP();
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
    			    }
		   			else
    				{
    					//setMSG("Error in Saving..",'E');
    				}
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
    	 setMSG("Releasing the lot from Final classification ..",'N');
    	 M_strSQLQRY ="Update PR_LTMST SET ";
    	 M_strSQLQRY += "LT_CLSBY='',LT_CLSTM =null,LT_CPRCD ='',";
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
    		txtLOTNO.requestFocus();
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
			if((txtCPRCD.getText().trim().equals("")) ||(!strCLSFL.equals("9")))
			   	setMSG("Misupdation of data, please contact Systems department ",'E');
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
			    txtREMDS.requestFocus();
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
}
