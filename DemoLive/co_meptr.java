/*
System Name   : 
Program Name  : co_meptr
Program Desc. : Vendor Registration
				
Author        : S.R.Deshpande
Date          : 24/06/2004
Version       : 1.0

Modificaitons 

Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : 			
*/
import javax.swing.tree.*;
import java.awt.*;
import java.sql.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.Number;
import java.lang.Float;
import java.util.Date;
import java.text.SimpleDateFormat;
class co_meptr extends cl_pbase
{
	//private TxtLimit txtDOCRF;
	private TxtLimit txtDSRCD;
	private TxtLimit txtDSRNM;
	private TxtLimit txtDOCNO;
	private TxtLimit txtDSTCD;
	private TxtLimit txtPRTNM;
	private TxtLimit txtSHRNM;
	private TxtLimit txtADR01;
	private TxtLimit txtADR02;
	private TxtLimit txtADR03;
	private TxtLimit txtADR04;
	private TxtLimit txtCTYNM;
	private TxtLimit txtSTACD;
	private TxtLimit txtCNTCD;
	private TxtLimit txtCONNM;
	private TxtLimit txtEMLRF;
	private TxtLimit txtSTXNO;
	private TxtLimit txtCSTNO;
	private TxtLimit txtECCNO;
	private TxtLimit txtITPNO;
	private TxtLimit txtCLSCD;
	private TxtLimit txtEXCNO;
	private TxtLimit txtRNGDS;
	private TxtLimit txtDIVDS;
	private TxtLimit txtCLLDS;
	private TxtLimit txtZONDS;
	private TxtLimit txtTRNCD;
	private TxtLimit txtGRPCD;
	private TxtLimit txtSTANM;
	private TxtLimit txtCNTNM;
	private TxtLimit txtCOWEB;
	private TxtLimit txtEMLPR;
	private TxtLimit txtEMLMR;
	private TxtLimit txtEMLAC;
	private TxtLimit txtTRNFL;
	private TxtLimit txtZONNM;
	private TxtLimit txtTRNNM;
	private TxtLimit txtEUSCD;
	private TxtLimit txtEUSDS;
	private TxtLimit txtPINCD;
	private TxtLimit txtMOBNO;
	private TxtLimit txtTEL01;
	private TxtLimit txtTEL02;
	private TxtLimit txtFAXNO;
	
	private TxtNumLimit txtZONCD;
	private TxtNumLimit	txtPURVL;
	private TxtNumLimit txtYOPCR;
	private TxtNumLimit txtYOPDB;
	private TxtNumLimit	txtYTDCR;
	private TxtNumLimit txtYTDDB;
	private TxtNumLimit txtSALVL;
	private TxtNumLimit txtCAPQT;
	
	private TxtDate	txtSTXDT;
	private TxtDate txtCSTDT;
	private TxtDate txtAONDT;
	
	private JComboBox cmbPRTTP;
	
	private JPanel pnlMAIN;			//JPanel for All Components.
	private JPanel pnlCONTACT;		//JPanel for Contact Information.
	private JPanel pnlDETAIL;		//JPanel for Detail Information
	private JPanel pnlOTHER;		//JPanel for Other Details
	private JPanel pnlSECTOR;		//JPanel for Sector Information JTable
			
	private JRadioButton rdbOBSYES;
	private JRadioButton rdbOBSNO;
	private JRadioButton rdbTSTYES;
	private JRadioButton rdbTSTNO;
	private JRadioButton rdbINDFL;
	private JRadioButton rdbFORFL;
	
	private ButtonGroup btgINFFL;
	private ButtonGroup btgTSTFL;
	private ButtonGroup btgOBSFL;
	
	private JTabbedPane tbpDETAIL;
	
    private cl_JTBL tblSCTDL;
	
	private Hashtable<String,String> hstEUSCD;
	private String[] L_staNAMES;
	private int[] L_inaCOLSZ;

    int TB1_CHKFL = 0;
    int TB1_EUSCD = 1;
    int TB1_EUSDS = 2;
    int TB1_CAPQT = 3;
    int TB1_AONDT = 4;

    private String strSPDNA = "splmast";
	private boolean flgCHK_EXIST = false;
    private String strWHRSTR;

	private String strYREND = "31/03/2004";
	private String strYRDGT;
    	
    Connection conSPDBA = null;               
    
	Statement stmSPDBA = null;               
    Statement stmSPDBQ = null; 
	
	ResultSet rstRSLSET;
	private ResultSet rstRSLTMP;
    public co_meptr()
	{
		super(1);
		try
		{
			pnlMAIN=new JPanel(null);
			setMatrix(20,8);
			pnlMAIN.removeAll();
			tbpDETAIL=new JTabbedPane();
			add(new JLabel("Party Type"),1,1,1,1,pnlMAIN,'L');
			add(cmbPRTTP=new JComboBox(),1,2,1,1,pnlMAIN,'L');
			//add(new JLabel("Document No."),1,3,1,1,pnlMAIN,'L');
			//add(txtDOCRF=new TxtLimit(8),1,4,1,1,pnlMAIN,'L');
			add(new JLabel("Doc. No."),2,1,1,1,pnlMAIN,'L');
			add(txtDOCNO=new TxtLimit(8),2,2,1,1,pnlMAIN,'L');
			add(new JLabel("Party Name"),2,3,1,1,pnlMAIN,'L');
			add(txtPRTNM=new TxtLimit(40),2,4,1,2.93,pnlMAIN,'L');
			add(new JLabel("Short Name"),2,7,1,1,pnlMAIN,'L');
			add(txtSHRNM=new TxtLimit(10),2,8,1,1,pnlMAIN,'L');
			add(new JLabel("Address"),3,1,1,1,pnlMAIN,'L');
			add(txtADR01=new TxtLimit(40),3,2,1,3.5,pnlMAIN,'L');
			add(txtADR02=new TxtLimit(40),3,6,1,3.43,pnlMAIN,'L');
			add(txtADR03=new TxtLimit(40),4,2,1,3.5,pnlMAIN,'L');
			add(txtADR04=new TxtLimit(40),4,6,1,3.43,pnlMAIN,'L');
			add(new JLabel("City Name"),5,1,1,1,pnlMAIN,'L');
			add(txtCTYNM=new TxtLimit(15),5,2,1,1.95,pnlMAIN,'L');
			add(new JLabel("Pin Code"),5,4,1,1,pnlMAIN,'L');
			add(txtPINCD=new TxtLimit(7),5,5,1,1,pnlMAIN,'L');
			add(new JLabel("State Code"),5,6,1,1,pnlMAIN,'L');
			add(txtSTACD=new TxtLimit(3),5,7,1,0.5,pnlMAIN,'L');
			add(txtSTANM=new TxtLimit(20),5,8,1,1.45,pnlMAIN,'R');
			add(new JLabel("Country Code"),6,1,1,1,pnlMAIN,'L');
			add(txtCNTCD=new TxtLimit(3),6,2,1,0.5,pnlMAIN,'L');
			add(txtCNTNM=new TxtLimit(15),6,3,1,1.5,pnlMAIN,'R');
			add(new JLabel("Group Code"),6,4,1,1,pnlMAIN,'L');
			add(txtGRPCD=new TxtLimit(5),6,5,1,1,pnlMAIN,'L');
			add(new JLabel("Transporter"),6,6,1,1,pnlMAIN,'R');
			add(txtTRNCD=new TxtLimit(5),6,7,1,0.5,pnlMAIN,'L');
			add(txtTRNNM=new TxtLimit(40),6,8,1,1.45,pnlMAIN,'R');
			add(new JLabel("Distributor"),7,1,1,1,pnlMAIN,'L');
			add(txtDSRCD=new TxtLimit(5),7,2,1,1,pnlMAIN,'L');
			add(new JLabel("Dist. Name"),7,3,1,1,pnlMAIN,'L');
			add(txtDSRNM=new TxtLimit(40),7,4,1,2.93,pnlMAIN,'L');
			add(new JLabel("Destination"),7,7,1,1,pnlMAIN,'L');
			add(txtDSTCD=new TxtLimit(3),7,8,1,1,pnlMAIN,'L');
			updateUI();

			add(new JLabel("Contact"),1,1,1,1,pnlCONTACT=new JPanel(null),'L');
			add(txtCONNM=new TxtLimit(40),1,2,1,2.9,pnlCONTACT,'L');
			add(new JLabel("Mobile"),1,5,1,1,pnlCONTACT,'L');
			add(txtMOBNO=new TxtLimit(10),1,6,1,2.85,pnlCONTACT,'L');
			add(new JLabel("Email ID"),2,1,1,1,pnlCONTACT,'L');
			add(txtEMLRF=new TxtLimit(30),2,2,1,2.9,pnlCONTACT,'L');
			add(new JLabel("Company WebSite"),2,5,1,1,pnlCONTACT,'L');
			add(txtCOWEB=new TxtLimit(40),2,6,1,2.85,pnlCONTACT,'L');
			add(new JLabel("Email ID of Purchase Email"),3,1,1,1,pnlCONTACT,'L');
			add(txtEMLPR=new TxtLimit(30),3,2,1,2.9,pnlCONTACT,'L');
			add(new JLabel("Mkt EmailID"),3,5,1,1,pnlCONTACT,'L');
			add(txtEMLMR=new TxtLimit(30),3,6,1,2.85,pnlCONTACT,'L');
			add(new JLabel("Acct EmailID"),4,1,1,1,pnlCONTACT,'L');
			add(txtEMLAC=new TxtLimit(30),4,2,1,2.9,pnlCONTACT,'L');
			add(new JLabel("Fax No."),4,5,1,1,pnlCONTACT,'L');
			add(txtFAXNO=new TxtLimit(15),4,6,1,2.85,pnlCONTACT,'L');
			add(new JLabel("Tel.No.1"),5,1,1,1,pnlCONTACT,'L');
			add(txtTEL01=new TxtLimit(15),5,2,1,2.9,pnlCONTACT,'L');
			add(new JLabel("Tel.No.2"),5,5,1,1,pnlCONTACT,'L');
			add(txtTEL02=new TxtLimit(15),5,6,1,2.85,pnlCONTACT,'L');
			tbpDETAIL.add(pnlCONTACT,"Contact Information");
			updateUI();

			add(new JLabel("S.T. No"),1,1,1,1,pnlDETAIL=new JPanel(null),'L');
			add(txtSTXNO=new TxtLimit(20),1,2,1,2.9,pnlDETAIL,'L');
			add(new JLabel("S.T. WEF"),1,5,1,1,pnlDETAIL,'L');
			add(txtSTXDT=new TxtDate(),1,6,1,2,pnlDETAIL,'L');
			add(new JLabel("C.S.T. No"),2,1,1,1,pnlDETAIL,'L');
			add(txtCSTNO=new TxtLimit(20),2,2,1,2.9,pnlDETAIL,'L');
			add(new JLabel("C.S.T. WEF"),2,5,1,1,pnlDETAIL,'L');
			add(txtCSTDT=new TxtDate(),2,6,1,2,pnlDETAIL,'L');
			add(new JLabel("Division"),3,1,1,1,pnlDETAIL,'L');
			add(txtDIVDS=new TxtLimit(15),3,2,1,2,pnlDETAIL,'L');
			add(new JLabel("ECC NO."),3,5,1,1,pnlDETAIL,'L');
			add(txtECCNO=new TxtLimit(20),3,6,1,2,pnlDETAIL,'L');
			add(new JLabel("I.T.P.No."),4,1,1,1,pnlDETAIL,'L');
			add(txtITPNO=new TxtLimit(40),4,2,1,2.9,pnlDETAIL,'L');
			add(new JLabel("Collectorate"),4,5,1,1,pnlDETAIL,'L');
			add(txtCLLDS=new TxtLimit(15),4,6,1,2,pnlDETAIL,'L');
			add(new JLabel("Class"),4,8,1,0.5,pnlDETAIL,'L');
			add(txtCLSCD=new TxtLimit(2),4,8,1,0.45,pnlDETAIL,'R');
			add(new JLabel("Excise No."),5,1,1,1,pnlDETAIL,'L');
			add(txtEXCNO=new TxtLimit(40),5,2,1,2.9,pnlDETAIL,'L');
			add(new JLabel("Range"),5,5,1,1,pnlDETAIL,'L');
			add(txtRNGDS=new TxtLimit(40),5,6,1,2.85,pnlDETAIL,'L');
			add(new JLabel("Zone"),6,1,1,1,pnlDETAIL,'L');
			add(txtZONCD=new TxtNumLimit(2),6,2,1,0.5,pnlDETAIL,'L');
			add(txtZONDS=new TxtLimit(15),6,3,1,1.5,pnlDETAIL,'R');

			rdbINDFL=new JRadioButton("Indian");
			rdbFORFL=new JRadioButton("Foreigner");
			rdbTSTYES=new JRadioButton("Yes");
			rdbTSTNO=new JRadioButton("No");
			rdbOBSYES=new JRadioButton("Yes");
			rdbOBSNO=new JRadioButton("No");

			btgINFFL=new ButtonGroup();
			btgTSTFL=new ButtonGroup();
			btgOBSFL=new ButtonGroup();

			btgINFFL.add(rdbINDFL);btgINFFL.add(rdbFORFL);
			btgTSTFL.add(rdbTSTYES);btgTSTFL.add(rdbTSTNO);
			btgOBSFL.add(rdbOBSYES);btgOBSFL.add(rdbOBSNO);

			add(new JLabel("IND/FGN"),6,5,1,1,pnlDETAIL,'L');
			add(rdbINDFL,6,6,1,1,pnlDETAIL,'L');
			add(rdbFORFL,6,7,1,1,pnlDETAIL,'L');
			add(new JLabel("Test Cert."),7,1,1,1,pnlDETAIL,'L');
			add(rdbTSTYES,7,2,1,1,pnlDETAIL,'L');
			add(rdbTSTNO,7,3,1,1,pnlDETAIL,'L');
			add(new JLabel("Obs.Flg."),7,5,1,1,pnlDETAIL,'L');
			add(rdbOBSYES,7,6,1,1,pnlDETAIL,'L');
			add(rdbOBSNO,7,7,1,1,pnlDETAIL,'L');
			tbpDETAIL.add(pnlDETAIL,"Detail Information");
			updateUI();

			add(new JLabel("Sales Value"),1,1,1,1,pnlOTHER=new JPanel(null),'L');
			add(txtSALVL=new TxtNumLimit(12.2),1,2,1,2.9,pnlOTHER,'L');
			add(new JLabel("TRN. Flag"),1,5,1,1,pnlOTHER,'L');
			add(txtTRNFL=new TxtLimit(1),1,6,1,0.5,pnlOTHER,'L');
			add(new JLabel("Y.To Date Cr."),2,1,1,1,pnlOTHER,'L');
			add(txtYTDCR=new TxtNumLimit(12.2),2,2,1,2.9,pnlOTHER,'L');
			add(new JLabel("Y.Opening Cr."),2,5,1,1,pnlOTHER,'L');
			add(txtYOPCR=new TxtNumLimit(12.2),2,6,1,2.85,pnlOTHER,'L');
			add(new JLabel("Y.To Date Db."),3,1,1,1,pnlOTHER,'L');
			add(txtYTDDB=new TxtNumLimit(12.2),3,2,1,2.9,pnlOTHER,'L');
			add(new JLabel("Y.Opening Db."),3,5,1,1,pnlOTHER,'L');
			add(txtYOPDB=new TxtNumLimit(12.2),3,6,1,2.85,pnlOTHER,'L');
			add(new JLabel("Doc Ref."),4,1,1,1,pnlOTHER,'L');
			//add(txtDOCRF=new TxtLimit(8),4,2,1,2,pnlOTHER,'L');
			add(new JLabel("Pur. Value"),4,5,1,1,pnlOTHER,'L');
			add(txtPURVL=new TxtNumLimit(12.2),4,6,1,2,pnlOTHER,'L');
			tbpDETAIL.add(pnlOTHER,"Other Information");
			updateUI();

			L_staNAMES=new String[]{"FL","Sec.Code","Sector","Capacity","As On Date"};
			L_inaCOLSZ=new int[]{20,50,250,100,100};
            tblSCTDL=(cl_JTBL)crtTBLPNL(pnlSECTOR=new JPanel(null),L_staNAMES,10,1,1,5,7,L_inaCOLSZ,new int[]{0}) ;
            tblSCTDL.setCellEditor(TB1_EUSCD,txtEUSCD=new TxtLimit(2));
            tblSCTDL.setCellEditor(TB1_EUSDS,txtEUSDS=new TxtLimit(30));
            tblSCTDL.setCellEditor(TB1_CAPQT,txtCAPQT=new TxtNumLimit(10.3));
            tblSCTDL.setCellEditor(TB1_AONDT,txtAONDT=new TxtDate());
			tblSCTDL.setAutoResizeMode(cl_JTBL.AUTO_RESIZE_OFF);

			txtEUSCD.addActionListener(this);txtEUSCD.addFocusListener(this);
			txtEUSCD.addKeyListener(this);
			txtEUSDS.addActionListener(this);txtEUSDS.addFocusListener(this);
			txtEUSDS.addKeyListener(this);
			txtCAPQT.addActionListener(this);txtCAPQT.addFocusListener(this);
			txtCAPQT.addKeyListener(this);
			txtAONDT.addActionListener(this);txtAONDT.addFocusListener(this);
			txtAONDT.addKeyListener(this);
			updateUI();

			tbpDETAIL.add(pnlSECTOR,"Sector Information");
			add(tbpDETAIL,9,1,9,7.95,pnlMAIN,'L');
			add(pnlMAIN,1,1,20,8,this,'L');
			updateUI();
			
			//To disable Document no.Enabled only for Customer type under Addition Mode 
			//txtDOCRF.setVisible(false);
			// To disable the fields on the third tab page
			txtSALVL.setEnabled(false);
			txtTRNFL.setEnabled(false);
			txtYTDCR.setEnabled(false);
			txtYOPCR.setEnabled(false);
			txtYTDDB.setEnabled(false);
			txtYOPDB.setEnabled(false);
			txtCLSCD.setEnabled(false);
			//txtDOCRF.setEnabled(false);
			txtPURVL.setEnabled(false);
			txtEUSDS.setEnabled(false);
			txtTRNCD.setEnabled(false);
			txtTRNNM.setEnabled(false);
			txtDSRCD.setEnabled(false);
			txtDSRNM.setEnabled(false);
			txtSTANM.setEnabled(false);
			txtCNTNM.setEnabled(false);
			txtZONDS.setEnabled(false);
			txtEXCNO.setEnabled(false);
			/* Excise No. (txtEXCNO) modification freezed on 14/03/2007
			   txtECCNO to be used for recording and fetching Excise No. details*/
			setMSG("Select an Option..",'N');
			txtDOCNO.setEnabled(false);
			cmbPRTTP.setEnabled(true);
			cmbPRTTP.requestFocus();
			
			String L_strTEMP1="Select Patry Type";
			cmbPRTTP.addItem(L_strTEMP1.trim());
			M_strSQLQRY="Select CMT_CODCD,CMT_CODDS from CO_CDTRN"
			+" where CMT_CGMTP='MST' and CMT_CGSTP='COXXPRT'";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					L_strTEMP1=nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					String L_strTEMP2=nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					cmbPRTTP.addItem(L_strTEMP1+"-"+L_strTEMP2);
				}
			}
			hstEUSCD=new Hashtable<String,String>(25,0.2f);
			M_strSQLQRY="select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS'" 
				+" and CMT_CGSTP='MRXXEUS'" ;
            M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
					hstEUSCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
			}
			revalidate();
			updateUI();
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "5" : "4";
		}catch(Exception L_EX)
		{
                        System.out.println("Error in CO_MEPTR()"+L_EX);
		}
	}	

	
	/**This Method creates Main Screen.*/
	private void setSCRIN()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				rdbOBSNO.setSelected(true);
				rdbTSTNO.setSelected(true);
				txtDOCNO.setEnabled(false);
				txtTRNCD.setEnabled(false);
				txtTRNNM.setEnabled(false);
				txtDSRCD.setEnabled(false);
				txtDSRNM.setEnabled(false);
				txtSTANM.setEnabled(false);
				txtCNTNM.setEnabled(false);
				txtZONDS.setEnabled(false);
				txtSALVL.setEnabled(false);
				txtTRNFL.setEnabled(false);
				txtYTDCR.setEnabled(false);
				txtYOPCR.setEnabled(false);
				txtYTDDB.setEnabled(false);
				txtYOPDB.setEnabled(false);
				txtCLSCD.setEnabled(false);
				//txtDOCRF.setEnabled(false);
				txtPURVL.setEnabled(false);
				txtEUSDS.setEnabled(false);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				setENBL(false);
				cmbPRTTP.setEnabled(true);
				txtDOCNO.setEnabled(true);cl_dat.M_btnEXIT_pbst.setEnabled(true);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				//setENBL(false);
				cmbPRTTP.setEnabled(true);
				txtDOCNO.setEnabled(true);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				setENBL(false);
				cmbPRTTP.setEnabled(true);
				txtDOCNO.setEnabled(true);
			}
			cmbPRTTP.requestFocus();
		}catch(Exception E){
			System.out.println("Error in setSCRIN : " + E);
		}					
	}

/**
 */	
	public void actionPerformed(ActionEvent L_AE)
	{
			super.actionPerformed(L_AE);
			M_objSOURC=L_AE.getSource();
		try
		{
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();
				setSCRIN();
			}
			else if(M_objSOURC==cmbPRTTP)
			{
				if(cmbPRTTP.getSelectedIndex()>0)
				{
					setACTIVATE();
					if(cmbPRTTP.getSelectedItem().toString().substring(0,1).equals("C"))
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							//txtDOCRF.setVisible(true);
							//txtDOCRF.setEnabled(true);
							//txtDOCRF.requestFocus();
							//txtDOCRF.setText("");
							txtPRTNM.setEnabled(true);
							txtTRNCD.setEnabled(true);
							txtDSRCD.setEnabled(true);
						}
						else
						{
							//txtDOCRF.setVisible(false);
							txtDOCNO.requestFocus();
						}
					}
					else
					{
						txtTRNCD.setEnabled(false);
						//txtTRNNM.setEnabled(false);
						txtDSRCD.setEnabled(false);
						//txtDSRNM.setEnabled(false);
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							txtPRTNM.requestFocus();
						else
							txtDOCNO.requestFocus();
					}
				}
			}
		}catch(Exception L_EX)
		{
			System.out.println("Error in Action Performed "+L_EX);
		}
	}
				
	public void focusLost(FocusEvent L_FE)
	{
			super.focusLost(L_FE);
		try
		{
			if(M_objSOURC==txtDSRCD)
				txtDSRCD.setText(txtDSRCD.getText().trim().toUpperCase());
			//else if (M_objSOURC==txtDOCRF)
			//{
			//	if(txtDOCRF.getText().length()<8)
			//		txtDOCRF.setVisible(false);
			//}
			else if(M_objSOURC==txtDOCNO)
				txtDOCNO.setText(txtDOCNO.getText().trim().toUpperCase());
			else if(M_objSOURC==txtPRTNM)
			{
				if(txtPRTNM.getText().length()>0)
					txtPRTNM.setText(txtPRTNM.getText().trim().toUpperCase());
			}
			else if(M_objSOURC==txtSHRNM)
			{
				if(txtSHRNM.getText().length()>0)
					txtSHRNM.setText(txtSHRNM.getText().trim().toUpperCase());
			}
			else if(M_objSOURC==txtADR01)
			{
				if(txtADR01.getText().length()>0)
					txtADR01.setText(txtADR01.getText().trim().toUpperCase());
			}
			else if(M_objSOURC==txtADR02)
				txtADR02.setText(txtADR02.getText().trim().toUpperCase());
			else if(M_objSOURC==txtADR03)
				txtADR03.setText(txtADR03.getText().trim().toUpperCase());
			else if(M_objSOURC==txtADR04)
				txtADR04.setText(txtADR04.getText().trim().toUpperCase());
			else if(M_objSOURC==txtCTYNM)
			{
				if(txtCTYNM.getText().length()>0)
					txtCTYNM.setText(txtCTYNM.getText().trim().toUpperCase());
			}
			else if(M_objSOURC==txtTRNCD)
				txtTRNCD.setText(txtTRNCD.getText().trim().toUpperCase());
			else if(M_objSOURC==txtDSTCD)
			{
				if(txtDSTCD.getText().length()>0)
					txtDSTCD.setText(txtDSTCD.getText().trim().toUpperCase());
			}
			else if(M_objSOURC==txtCONNM)
				txtCONNM.setText(txtCONNM.getText().trim().toUpperCase());
			else if(M_objSOURC==txtEMLRF)
			{
				if(txtEMLRF.getText().length()>0)
					txtEMLRF.setText(txtEMLRF.getText().trim().toLowerCase());
			}
			else if(M_objSOURC==txtCOWEB)
				txtCOWEB.setText(txtCOWEB.getText().trim().toLowerCase());
			else if(M_objSOURC==txtEMLPR)
				txtEMLPR.setText(txtEMLPR.getText().trim().toLowerCase());
			else if(M_objSOURC==txtEMLMR)
				txtEMLMR.setText(txtEMLMR.getText().trim().toLowerCase());
			else if(M_objSOURC==txtEMLAC)
				txtEMLAC.setText(txtEMLAC.getText().trim().toLowerCase());
			else if(M_objSOURC==txtSTXNO)
				txtSTXNO.setText(txtSTXNO.getText().trim().toUpperCase());
			else if(M_objSOURC==txtCSTNO)
				txtCSTNO.setText(txtCSTNO.getText().trim().toUpperCase());
			else if(M_objSOURC==txtECCNO)
				txtECCNO.setText(txtECCNO.getText().trim().toUpperCase());
			//else if(M_objSOURC==txtEXCNO)
			//	txtEXCNO.setText(txtEXCNO.getText().trim().toUpperCase());
			else if(M_objSOURC==txtITPNO)
				txtITPNO.setText(txtITPNO.getText().trim().toUpperCase());
			else if(M_objSOURC==txtDIVDS)
				txtDIVDS.setText(txtDIVDS.getText().trim().toUpperCase());
			else if(M_objSOURC==txtCLLDS)
				txtCLLDS.setText(txtCLLDS.getText().trim().toUpperCase());
			else if(M_objSOURC==txtRNGDS)
				txtRNGDS.setText(txtRNGDS.getText().trim().toUpperCase());
		}catch(Exception LP_EX)
		{
			System.out.println("Error in focusLost "+LP_EX);
		}
	}
			
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC==cmbPRTTP)		// Party Type
				setMSG("Select Party Type",'N');
			else if(M_objSOURC==txtDSRCD)		//Document No
				setMSG("Press F1 for Valid Distributor Code",'N');
			//else if(M_objSOURC==txtDOCRF)		//Document No
			//	setMSG("Press F1 for Valid Doc.No. for Registration Or Leave Doc.No. Blank to Proceed without Registration",'N');
			else if(M_objSOURC==txtDOCNO)		// Party code
				setMSG("Press F1 for valid Party Codes",'N');
			else if(M_objSOURC==txtPRTNM)		// Party Name
				setMSG("Enter Party Name",'N');
			else if(M_objSOURC==txtSHRNM)		// Short Name
				setMSG("Enter Short Name for Party",'N');
			else if(M_objSOURC==txtADR01)		// Address 1
				setMSG("Enter Address",'N');
			else if(M_objSOURC==txtADR02)		// Address 2
				setMSG("Enter Address",'N');
			else if(M_objSOURC==txtADR03)		// Address 3
				setMSG("Enter Address",'N');
			else if(M_objSOURC==txtADR04)		// Address 4
				setMSG("Enter Address",'N');
			else if(M_objSOURC==txtCTYNM)		// City Name
				setMSG("Enter valid City Code",'N');
			else if(M_objSOURC==txtSTACD)		// State code
				setMSG("Enter State Code. Press F1 for valid State Codes",'N');
			else if(M_objSOURC==txtCNTCD)		// Country code
				setMSG("Enter Country Code. Press F1 for valid Country Codes",'N');
			else if(M_objSOURC==txtPINCD)		// Pin Code
				setMSG("Enter valid Pin Code",'N');
			else if(M_objSOURC==txtTRNCD)		// Transporter code
				setMSG("Enter valid Transporter Codes",'N');
			else if(M_objSOURC==txtGRPCD)		// Group code
				setMSG("Enter valid Group codes",'N');
				
			// First tab page
			else if(M_objSOURC==txtCONNM)		// Contact Person Name
				setMSG("Enter Name of Contact Person",'N');
			else if(M_objSOURC==txtMOBNO)		// Contact Person Mobile
				setMSG("Enter Mobile of Contact Person",'N');
			else if(M_objSOURC==txtEMLRF)		// Contact Person Email
				setMSG("Enter Email of Contact Person",'N');
			else if(M_objSOURC==txtCOWEB)		// Company Website
				setMSG("Enter Company Website",'N');
			else if(M_objSOURC==txtEMLPR)		// Email of Purchase Dept.
				setMSG("Enter Email of Purchase Dept.",'N');
			else if(M_objSOURC==txtEMLMR)		// Email of Marketing Dept.
				setMSG("Enter Email of Marketing Dept.",'N');
			else if(M_objSOURC==txtEMLAC)		// Email of Account Dept.
				setMSG("Enter Email of Account Dept.",'N');
			else if(M_objSOURC==txtTEL01)		// Comp. tel. 1
				setMSG("Enter Company Telephone No.",'N');
			else if(M_objSOURC==txtTEL02)		// Comp. tel. 2
				setMSG("Enter Company Telephone No.",'N');
			else if(M_objSOURC==txtFAXNO)		// Comp. Fax no.
				setMSG("Enter Company Fax no.",'N');
			
			// Second tab page
			else if(M_objSOURC==txtSTXNO)		// State S.Tax no.
				setMSG("Enter State S.Tax no.",'N');
			else if(M_objSOURC==txtSTXDT)		// WEF Date
				setMSG("Enter WEF Date in DD/MM/YYYY format",'N');
			else if(M_objSOURC==txtCSTNO)		// Central S.Tax no.
				setMSG("Enter Central S.Tax no.",'N');
			else if(M_objSOURC==txtCSTDT)		// WEF Date
				setMSG("Enter WEF Date in DD/MM/YYYY format",'N');
			else if(M_objSOURC==txtDIVDS)		// Division
				setMSG("Enter Division",'N');
			else if(M_objSOURC==txtECCNO)		// ECC Number
				setMSG("Enter ECC Number",'N');
			else if(M_objSOURC==txtITPNO)		// I. Tax PA No.
				setMSG("Enter I. Tax PA No.",'N');
			else if(M_objSOURC==txtCLLDS)		// Collectorate
				setMSG("Enter Collectorate",'N');
			else if(M_objSOURC==txtCLSCD)		// Class
				setMSG("Enter Class",'N');
			//else if(M_objSOURC==txtEXCNO)		// Excise Number
			//	setMSG("Enter Excise Number",'N');
			else if(M_objSOURC==txtRNGDS)		// Range
				setMSG("Enter Range",'N');
			else if(M_objSOURC==txtZONCD)		// Zone code
				setMSG("Press F1 for valid Zone Codes",'N');
			else if(M_objSOURC==rdbINDFL)		// Indian
				setMSG("",'N');
			else if(M_objSOURC==rdbFORFL)		// Foreigner
				setMSG("",'N');
			else if(M_objSOURC==rdbTSTYES)		// Test Flag YES
				setMSG("",'N');
			else if(M_objSOURC==rdbTSTNO)		// NO
				setMSG("",'N');
			else if(M_objSOURC==rdbOBSYES)		// Obsolete Flag YES
				setMSG("",'N');
			else if(M_objSOURC==rdbOBSNO)		// NO
				setMSG("",'N');
			else if(M_objSOURC==txtEUSCD)
				txtEUSCD.requestFocus();
		}catch(Exception L_EX){
			System.out.println("Error in focusGained "+L_EX);
		}
	}
	
	/**Creates Help Screen on F1 and Navigation on Enter.*/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		M_objSOURC=L_KE.getSource();
		int intKEYVL=L_KE.getKeyCode();
		try
		{
            if(intKEYVL==L_KE.VK_F1)
			{
				if(M_objSOURC==txtDSRCD)
				{
					M_strSQLQRY="Select DISTINCT PT_PRTCD,PT_PRTNM From CO_PTMST"
						+" Where PT_PRTTP='D'";
					M_strHLPFLD="txtDSRCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Distributor Code","Distributor Name"},2,"CT");
				}
				else if(M_objSOURC==txtDOCNO)
				{
					M_strSQLQRY="Select PT_DOCRF,PT_PRTNM from CO_RGMST"
						+" Where PT_PRTTP='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"'"
						+" And PT_STSFL='0'";
					M_strHLPFLD="txtDOCNO";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT");
				}
				/*else if(M_objSOURC==txtDOCRF)
				{System.out.println("F1 doc");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strSQLQRY="Select PTP_DOCNO,PTP_PRTNM From CO_PTPTR"
							+" Where PTP_PRTTP='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"'"
							+" And PTP_STSFL = '0'";
						System.out.println(M_strSQLQRY);
						M_strHLPFLD="txtDOCRF";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Document No","Party Name"},2,"CT");
					}
				}*/
				else if(M_objSOURC==txtGRPCD)
				{
					M_strSQLQRY="Select PT_GRPCD,PT_PRTNM From CO_PTMST"
						+" Where PT_PRTTP='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"'"
						+" And PT_PRTNM Like '"+txtPRTNM.getText().toString().substring(0,1).trim().toUpperCase()+"%'";
					M_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
					M_strHLPFLD="txtGRPCD";
								cl_hlp(M_strSQLQRY,2,1,new String[]{"Group Code","Party Name"},2,"CT");
				}
				else if(M_objSOURC==txtSTACD)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()<cl_dat.M_cmbOPTN_pbst.getItemCount()-1)
					{
						M_strSQLQRY="select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXSTA'";
						M_strHLPFLD="txtSTACD";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"State Code","State"},2,"CT");
					}
				}
				else if(M_objSOURC==txtCNTCD)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()<cl_dat.M_cmbOPTN_pbst.getItemCount()-1)
					{
						M_strSQLQRY="select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXCNT'";
						M_strHLPFLD="txtCNTCD";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Country Code","Country"},2,"CT");
					}
				}
				else if(M_objSOURC==txtDSTCD)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0&&cl_dat.M_cmbOPTN_pbst.getSelectedIndex()<cl_dat.M_cmbOPTN_pbst.getItemCount()-1)
					{
						M_strSQLQRY="select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDST'";
						M_strHLPFLD="txtDSTCD";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Destination Code","Destination"},2,"CT");
					}
				}
				else if(M_objSOURC==txtTRNCD)
				{
					if(!cmbPRTTP.getSelectedItem().toString().substring(0,1).equals("T"))
					{
						M_strSQLQRY="Select DISTINCT PT_PRTCD,PT_PRTNM From CO_PTMST"
							+" Where PT_PRTTP='T'";
						M_strHLPFLD="txtTRNCD";
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Transportor Code","Transportor Name"},2,"CT");
					}
				}
				else if(M_objSOURC==txtZONCD)
				{
					M_strSQLQRY="select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MR00ZON'";
					M_strHLPFLD="txtZONCD";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Zone Code","Description"},2,"CT");
				}
				else if(M_objSOURC==txtEUSCD)
				{
					M_strSQLQRY="SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MR00EUS'";
					M_strHLPFLD="txtEUSCD";
					cl_hlp(M_strSQLQRY,2,1,new String[] {"Sector Code","Sector"},2,"CT");
				}
			}//F1
            else if(intKEYVL==9 || intKEYVL==L_KE.VK_ENTER)
			{
				if(M_objSOURC==cmbPRTTP)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && cmbPRTTP.getSelectedIndex()>0)
							txtPRTNM.requestFocus();
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) && cmbPRTTP.getSelectedIndex()>0)
							txtDOCNO.requestFocus();
				}
				else if(M_objSOURC==txtDOCNO)
				{
					if(!vldDOCNO())
						return;
					dspDATA();
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						txtDOCNO.requestFocus();
					else
					{
						setENBL(true);
						txtPRTNM.setEnabled(false);
						txtSALVL.setEnabled(false);
						txtTRNFL.setEnabled(false);
						txtYTDCR.setEnabled(false);
						txtYOPCR.setEnabled(false);
						txtYTDDB.setEnabled(false);
						txtYOPDB.setEnabled(false);
						txtSTANM.setEnabled(false);
						txtCNTNM.setEnabled(false);
						txtZONDS.setEnabled(false);
						txtTRNNM.setEnabled(false);
						txtDSRNM.setEnabled(false);
						txtEUSDS.setEnabled(false);
						if(cmbPRTTP.getSelectedItem().toString().substring(0,1).equals("C"))
						{
							txtTRNCD.setEnabled(true);
							txtDSRCD.setEnabled(true);
						}
						else
						{
							txtTRNCD.setEnabled(false);
							txtDSRCD.setEnabled(false);
						}
						txtSHRNM.requestFocus();
					}
				}
				else if(M_objSOURC==txtDSRCD)
				{
					if(txtDSRCD.getText().trim().length()==5)
					{
						M_strSQLQRY="Select PT_PRTNM From CO_PTMST Where PT_PRTCD='"+txtDSRCD.getText().trim().toUpperCase()+"'"
							+" And PT_PRTTP='D'";
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next())
						{
							txtDSRNM.setText(M_rstRSSET.getString("PT_PRTNM"));
							txtDSTCD.requestFocus();
						}
						else
						{
							setMSG("Illegal Distributor Code",'E');
							txtDSRCD.setText("");
							txtDSRCD.requestFocus();
						}
					}
					else
					{
						setMSG("Enter Proper Distributor Code",'E');
						txtDSRCD.requestFocus();
					}
				} // DSRCD
				/*else if(M_objSOURC==txtDOCRF)
				{
					if(txtDOCRF.getText().length()==0)
					{
						txtDOCRF.setVisible(false);
						setMSG("Party Entry Without Registration",'E');
						txtPRTNM.requestFocus();
					}
					else if(txtDOCRF.getText().length()>0&&txtDOCRF.getText().length()<8)
					{
						txtDOCRF.setVisible(true);
						setMSG("Invalid Document No.",'E');
						txtDOCRF.requestFocus();
					}
					else
					{
						txtDOCRF.setVisible(true);
						txtPRTNM.requestFocus();
					}
				}*/ 
				else if(M_objSOURC==txtDSTCD)
				{
					M_strSQLQRY="select CMT_CODCD from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDST' and CMT_CODCD='"+txtDSTCD.getText().trim().toUpperCase()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
						txtCONNM.requestFocus();
					else
					{
						setMSG("Enter Valid Destination Code",'E');
						txtDSTCD.requestFocus();
					}
				}
				else if(M_objSOURC==txtPRTNM)
				{
					//if(txtDOCRF.isVisible()==false)
					//{
						if(txtPRTNM.getText().length()>0)
						{
							txtPRTNM.setText(txtPRTNM.getText().toUpperCase());
							//getPRTCD();
							//getDOCNO();
							txtSHRNM.requestFocus();
						}
						else
						{
							setMSG("Enter Party Name",'E');
							txtPRTNM.requestFocus();
						}
					//}
				}
				else if(M_objSOURC==txtSHRNM)
					txtADR01.requestFocus();
				else if(M_objSOURC==txtADR01)
				{
					if(txtADR01.getText().length()>0)
						txtADR02.requestFocus();
					else
					{
						setMSG("Enter the Address",'E');
						txtADR01.requestFocus();
					}
				}
				else if(M_objSOURC==txtADR02)
					txtADR03.requestFocus();
				else if(M_objSOURC==txtADR03)
					txtADR04.requestFocus();
				else if(M_objSOURC==txtADR04)
					txtCTYNM.requestFocus();
				else if(M_objSOURC==txtCTYNM)
					txtPINCD.requestFocus();
				else if(M_objSOURC==txtPINCD)
					txtSTACD.requestFocus();
				else if(M_objSOURC==txtSTACD)
				{
					M_strSQLQRY="select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXSTA' and CMT_CODCD='"+txtSTACD.getText().trim()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						txtSTANM.setText(M_rstRSSET.getString("CMT_CODDS"));
						txtCNTCD.requestFocus();
					}
					else
					{
						setMSG("Illegal State Code",'E');
						txtSTANM.setText("");
						txtSTACD.requestFocus();
					}
				}
				else if(M_objSOURC==txtCNTCD)
				{
					M_strSQLQRY="select CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXCNT' and CMT_CODCD='"+txtCNTCD.getText().trim()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						txtCNTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
						if(txtCNTNM.getText().toString().equals("INDIA")||txtCNTNM.getText().toString().equals("India"))
						{
							rdbINDFL.setEnabled(true);
							rdbFORFL.setEnabled(false);
							rdbINDFL.setSelected(true);
						}
						else
						{
							rdbFORFL.setEnabled(true);
							rdbINDFL.setEnabled(false);
							rdbFORFL.setSelected(true);
						}
						if(cmbPRTTP.getSelectedItem().toString().substring(0,1).equals("C"))
							txtTRNCD.requestFocus();
						else
							txtDSTCD.requestFocus();
					}
					else
					{
						setMSG("Illegal Country Code",'E');
						txtCNTCD.requestFocus();
					}
				}
				else if(M_objSOURC==txtGRPCD)
				{
					if(cmbPRTTP.getSelectedItem().toString().substring(0,1).equals("C"))
						txtTRNCD.requestFocus();
					else
						txtDSTCD.requestFocus();
				}
				else if(M_objSOURC==txtTRNCD)
				{
					M_strSQLQRY="Select PT_PRTNM From CO_PTMST"
							+" Where PT_PRTTP='T' and PT_PRTCD='"+txtTRNCD.getText().trim().toUpperCase()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						txtTRNNM.setText(M_rstRSSET.getString("PT_PRTNM"));
						if(txtDSRCD.getText().length()>0)
							txtCONNM.requestFocus();
						else
							txtDSRCD.requestFocus();
					}
					else
					{
						setMSG("Invalid Transporter code",'E');
						txtTRNNM.setText("");
						txtTRNCD.requestFocus();
					}
				}
				else if(M_objSOURC==txtCONNM)
					txtMOBNO.requestFocus();
				else if(M_objSOURC==txtMOBNO)
					txtEMLRF.requestFocus();
				else if(M_objSOURC==txtEMLRF)
					txtCOWEB.requestFocus();
				else if(M_objSOURC==txtCOWEB)
					txtEMLPR.requestFocus();
				else if(M_objSOURC==txtEMLPR)
					txtEMLMR.requestFocus();
				else if(M_objSOURC==txtEMLMR)
					txtEMLAC.requestFocus();
				else if(M_objSOURC==txtEMLAC)
					txtFAXNO.requestFocus();
				else if(M_objSOURC==txtFAXNO)
					txtTEL01.requestFocus();
				else if(M_objSOURC==txtTEL01)
					txtTEL02.requestFocus();
				else if(M_objSOURC==txtTEL02)
				{
					tbpDETAIL.setSelectedIndex(1);
					if(txtSTXNO.getText().length()>0)
						txtDIVDS.requestFocus();
					else
						txtSTXNO.requestFocus();
				}
				else if(M_objSOURC==txtSTXNO)
					txtSTXDT.requestFocus();
				else if(M_objSOURC==txtSTXDT)
					txtCSTNO.requestFocus();
				else if(M_objSOURC==txtCSTNO)
					txtCSTDT.requestFocus();
				else if(M_objSOURC==txtCSTDT)
					txtDIVDS.requestFocus();						
				else if(M_objSOURC==txtDIVDS)
					txtECCNO.requestFocus();
				else if(M_objSOURC==txtECCNO)
					txtITPNO.requestFocus();
				else if(M_objSOURC==txtITPNO)
					txtCLLDS.requestFocus();
				else if(M_objSOURC==txtCLLDS)
					txtRNGDS.requestFocus();
				//else if(M_objSOURC==txtEXCNO)
				//	txtRNGDS.requestFocus();
				else if(M_objSOURC==txtRNGDS)
					txtZONCD.requestFocus();
				else if(M_objSOURC==rdbINDFL)
				{
					rdbINDFL.setSelected(true);
					rdbTSTYES.requestFocus();
				}
				else if(M_objSOURC==txtZONCD)
				{
					M_strSQLQRY="select CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS'" 
						+" and CMT_CGSTP='MR00ZON'" 
						+" and CMT_CODCD='"+txtZONCD.getText().trim()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
						txtZONDS.setText(M_rstRSSET.getString("CMT_CODDS"));
					else
					{
						setMSG("Invalid Zone Code",'E');
						txtZONCD.requestFocus();
					}
				}
				else if(M_objSOURC==txtEUSCD)
				{
					boolean flgSELECT=true;
					String L_strEUSCD=txtEUSCD.getText();//tblSCTDL.getValueAt(tblSCTDL.getSelectedRow(),TB1_EUSCD).toString().trim();
					String L_strEUSDS=tblSCTDL.getValueAt(tblSCTDL.getSelectedRow(),TB1_EUSDS).toString().trim();
					for(int i=0;i<tblSCTDL.getSelectedRow();i++)
					{
						if(tblSCTDL.getValueAt(i,TB1_EUSCD).toString().trim().equals(L_strEUSCD))
						{
							setMSG("Sector Code Already Selected",'E');
							flgSELECT=false;
							//tblSCTDL.editCellAt(tblSCTDL.getSelectedRow(),TB1_EUSCD);
							txtEUSCD.requestFocus();
							break;
						}
					}
					if(flgSELECT)
					{
						tblSCTDL.editCellAt(tblSCTDL.getSelectedRow(),TB1_EUSCD);
						vldEUSCD();
					}
				}
				else if(M_objSOURC==txtAONDT)
					vldAONDT();
			} //VK_ENTER
		}catch(Exception L_EX)
		{
			System.out.println("Exception in keyPressed "+L_EX);
		}
	}

        void vldCAPQT()
        {
			tblSCTDL.setColumnSelectionInterval(TB1_AONDT,TB1_AONDT);
			tblSCTDL.editCellAt(tblSCTDL.getSelectedRow(),TB1_AONDT);
        }

        void vldAONDT()
        {
            tblSCTDL.setRowSelectionInterval(tblSCTDL.getSelectedRow(),tblSCTDL.getSelectedRow()+1);
            tblSCTDL.setColumnSelectionInterval(TB1_AONDT,TB1_EUSCD);
			txtEUSCD.setText("");
			tblSCTDL.editCellAt(tblSCTDL.getSelectedRow()+1,TB1_EUSCD);
        }

		private boolean vldDOCNO()
		{
			String L_strADDSTR = " and PT_STSFL = '0' ";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				L_strADDSTR="";
			strWHRSTR =  "PT_PRTTP = '" +cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase()+"' and "
					 +"PT_DOCRF = '" +txtDOCNO.getText()+"'"+L_strADDSTR;
			if(chkEXIST("CO_RGMST", strWHRSTR))
				return true;
			setMSG("Record Not found in Party Registration",'E');
			return false;
		}
		
		void vldEUSCD()
        {
			try
            {
				if(tblSCTDL.getValueAt(tblSCTDL.getSelectedRow(),TB1_EUSCD).toString().length()>0)
				{
					if(hstEUSCD.containsKey(tblSCTDL.getValueAt(tblSCTDL.getSelectedRow(),TB1_EUSCD)))
					{
						tblSCTDL.setValueAt(hstEUSCD.get(tblSCTDL.getValueAt(tblSCTDL.getSelectedRow(),TB1_EUSCD)),tblSCTDL.getSelectedRow(),TB1_EUSDS);
						//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						//	tblSCTDL.cmpEDITR[TB1_CHKFL].setEnabled(false);
						//tblSCTDL.cmpEDITR[TB1_EUSDS].setEnabled(false);
						tblSCTDL.setColumnSelectionInterval(TB1_CAPQT,TB1_CAPQT);
						tblSCTDL.setRowSelectionInterval(tblSCTDL.getSelectedRow(),tblSCTDL.getSelectedRow());
						tblSCTDL.editCellAt(tblSCTDL.getSelectedRow(),TB1_CAPQT);
					}
					else
					{
						setMSG("Invalid Sector Code",'E');
						tblSCTDL.setValueAt("",tblSCTDL.getSelectedRow(),TB1_EUSDS);
						tblSCTDL.editCellAt(tblSCTDL.getSelectedRow(),TB1_EUSCD);
						txtEUSCD.requestFocus();
					}
				}
            }catch(Exception L_SE)
			{
				System.out.println("Error in vldEUSCD "+L_SE);
			}
        }

	void exeHLPOK()
	{
		try
		{
			if(M_strHLPFLD.equals("txtEUSCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				String L_strEUSCD=L_STRTKN.nextToken();
				String L_strEUSDS=L_STRTKN.nextToken();
				boolean flgSELECT=true;
				for(int i=0;i<tblSCTDL.getSelectedRow();i++)
				{
					if(tblSCTDL.getValueAt(i,TB1_EUSCD).toString().equals(L_strEUSCD))
					{
						setMSG("Sector Code Already Selected",'E');
						flgSELECT=false;
						tblSCTDL.editCellAt(tblSCTDL.getSelectedRow(),TB1_EUSCD);
						txtEUSCD.requestFocus();
						break;
					}
				}
				if(flgSELECT)
				{
					txtEUSCD.setText(L_strEUSCD);
					//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					//	tblSCTDL.cmpEDITR[TB1_CHKFL].setEnabled(false);
					tblSCTDL.setValueAt(L_strEUSDS,tblSCTDL.getSelectedRow(),TB1_EUSDS);
					//tblSCTDL.cmpEDITR[TB1_EUSDS].setEnabled(false);
					tblSCTDL.setColumnSelectionInterval(TB1_CAPQT,TB1_CAPQT);
					tblSCTDL.setRowSelectionInterval(tblSCTDL.getSelectedRow(),tblSCTDL.getSelectedRow());
					tblSCTDL.editCellAt(tblSCTDL.getSelectedRow(),TB1_CAPQT);
					txtCAPQT.requestFocus();
				}
			}
				
			else if(M_strHLPFLD.equals("txtDSRCD"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDSRCD.setText(L_STRTKN.nextToken());
				txtDSRNM.setText(L_STRTKN.nextToken());
				txtDSTCD.requestFocus();
			}
			/*else if(M_strHLPFLD.equals("txtDOCRF"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDOCRF.setText(L_STRTKN.nextToken());
				txtDOCRF.setVisible(true);txtDOCRF.setEnabled(false);
				dspDATA();
				txtSHRNM.requestFocus();
			}*/
			else if(M_strHLPFLD.equals("txtDOCNO"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHLPSTR_pbst,"|");
				txtDOCNO.setText(L_STRTKN.nextToken());
				//dspDATA();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>2)
				{
					setENBL(false);
					txtDSRCD.setEnabled(true);
					txtDOCNO.setEnabled(true);
					cmbPRTTP.setEnabled(true);
					txtDOCNO.requestFocus();
				}
				else
				{
					setENBL(true);
					txtPRTNM.setEnabled(false);
					txtSALVL.setEnabled(false);
					txtTRNFL.setEnabled(false);
					txtYTDCR.setEnabled(false);
					txtYOPCR.setEnabled(false);
					txtYTDDB.setEnabled(false);
					txtYOPDB.setEnabled(false);
					txtSTANM.setEnabled(false);
					txtCNTNM.setEnabled(false);
					txtZONDS.setEnabled(false);
					txtTRNNM.setEnabled(false);
					txtDSRNM.setEnabled(false);
					txtEUSDS.setEnabled(false);
					//txtDOCRF.setEnabled(false);
					txtPURVL.setEnabled(false);
					if(cmbPRTTP.getSelectedItem().toString().substring(0,1).equals("C"))
					{
						txtTRNCD.setEnabled(true);
						txtDSRCD.setEnabled(true);
					}
					else
					{
						txtTRNCD.setEnabled(false);
						txtDSRCD.setEnabled(false);
					}
					txtDOCNO.requestFocus();
				}
			}
			else if(M_strHLPFLD.equals("txtGRPCD"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHLPSTR_pbst,"|");
				//String L_strGRPCD=L_STRTKN.nextToken();
				txtGRPCD.setText("");
				if(L_STRTKN.hasMoreTokens())
					txtGRPCD.setText(L_STRTKN.nextToken());
				else
					txtGRPCD.setText(txtDOCNO.getText());
				if(txtTRNCD.isEnabled()==true)
					txtTRNCD.requestFocus();
				else
					txtCONNM.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtSTACD"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtSTACD.setText(L_STRTKN.nextToken());
				txtSTANM.setText(L_STRTKN.nextToken());
				txtCNTCD.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtCNTCD"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtCNTCD.setText(L_STRTKN.nextToken());
				txtCNTNM.setText(L_STRTKN.nextToken());
				txtTRNCD.requestFocus();
				if(txtCNTCD.getText().toString().equals("001"))
				{
					rdbINDFL.setEnabled(true);
					rdbFORFL.setEnabled(false);
					rdbINDFL.setSelected(true);
				}
				else
				{
					rdbFORFL.setEnabled(true);
					rdbINDFL.setEnabled(false);
					rdbFORFL.setSelected(true);
				}
			}
			else if(M_strHLPFLD.equals("txtZONCD"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtZONCD.setText(L_STRTKN.nextToken());
				txtZONDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtDSTCD"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHLPSTR_pbst,"|");
				txtDSTCD.setText(L_STRTKN.nextToken());
				txtCONNM.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtTRNCD"))
			{
				cl_dat.M_flgHELPFL_pbst=false;
				super.exeHLPOK();
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtTRNCD.setText(L_STRTKN.nextToken());
				txtTRNNM.setText(L_STRTKN.nextToken());
				if(txtDSRCD.getText().length()>0)
					txtCONNM.requestFocus();
				else
					txtDSRCD.requestFocus();
			}
		}catch(Exception L_EX)
		{
			System.out.println("Exception in exeHLPOK : "+L_EX);
		}
	}
	
	/**This Function fetches the record of a particular document no and /n
	 * display it on the screen.
	 */
	private void dspDATA() throws Exception
	{
		strWHRSTR =  "PT_PRTTP = '" +cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase()+"' and "
					 +"PT_DOCRF = '" +txtDOCNO.getText()+"'";
		flgCHK_EXIST =  chkEXIST("CO_RGMST", strWHRSTR);
		
		inlTBLEDIT(tblSCTDL);
		if(!flgCHK_EXIST)
			return;
		M_strSQLQRY="SELECT * FROM CO_RGMST WHERE "+strWHRSTR;
		System.out.println(M_strSQLQRY);
		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET.next())
		{
			txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
			txtDSRCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_DSRCD"),""));
			txtDSTCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),""));
			txtADR01.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""));
			txtADR02.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),""));
			txtADR03.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),""));
			txtADR04.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),""));
			txtPINCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_PINCD"),""));
			txtCTYNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_CTYNM"),""));
			txtSTACD.setText(nvlSTRVL(M_rstRSSET.getString("PT_STACD"),""));
			txtCNTCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_CNTCD"),""));
			txtZONCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_ZONCD"),""));
			txtSTXNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_STXNO"),""));
				
			if(nvlSTRVL(M_rstRSSET.getString("PT_STXDT"),"").equals(""))
				txtSTXDT.setText("");
			else
				txtSTXDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("PT_STXDT")));
				
			txtCSTNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_CSTNO"),""));
				
			if(nvlSTRVL(M_rstRSSET.getString("PT_CSTDT"),"").equals(""))
				txtCSTDT.setText("");
			else
				txtCSTDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("PT_CSTDT")));
				
			txtITPNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_ITPNO"),""));
				
			if(nvlSTRVL(M_rstRSSET.getString("PT_TSTFL"),"").trim().length()>0)
			{
				if(M_rstRSSET.getString("PT_TSTFL").equals("Y"))
					rdbTSTYES.setSelected(true);
				else //if(M_rstRSSET.getString("PT_TSTFL").equals("N"))
					rdbTSTNO.setSelected(true);
			}
			else 
				rdbTSTNO.setSelected(true);

			if(nvlSTRVL(M_rstRSSET.getString("PT_INFFL"),"").trim().length()>0)
			{
				if(M_rstRSSET.getString("PT_INFFL").equals("I"))
					rdbINDFL.setSelected(true);
				else 
					rdbFORFL.setSelected(true);
			}
			else 
				rdbFORFL.setSelected(true);
				txtSHRNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_SHRNM"),""));
				txtGRPCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_GRPCD"),""));
				txtTRNCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_TRNCD"),""));
				txtCONNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_CONNM"),""));
				txtEMLRF.setText(nvlSTRVL(M_rstRSSET.getString("PT_EMLRF"),""));
				txtMOBNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_MOBNO"),""));
				txtEMLPR.setText(nvlSTRVL(M_rstRSSET.getString("PT_EMLPR"),""));
				txtEMLMR.setText(nvlSTRVL(M_rstRSSET.getString("PT_EMLMR"),""));
				txtEMLAC.setText(nvlSTRVL(M_rstRSSET.getString("PT_EMLAC"),""));
				txtCOWEB.setText(nvlSTRVL(M_rstRSSET.getString("PT_COWEB"),""));
				txtTEL01.setText(nvlSTRVL(M_rstRSSET.getString("PT_TEL01"),""));
				txtTEL02.setText(nvlSTRVL(M_rstRSSET.getString("PT_TEL02"),""));
				txtFAXNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_FAXNO"),""));
				txtECCNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_ECCNO"),""));
				txtEXCNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_EXCNO"),""));
				txtRNGDS.setText(nvlSTRVL(M_rstRSSET.getString("PT_RNGDS"),""));
				txtDIVDS.setText(nvlSTRVL(M_rstRSSET.getString("PT_DIVDS"),""));
				txtCLLDS.setText(nvlSTRVL(M_rstRSSET.getString("PT_CLLDS"),""));
				txtSALVL.setText(nvlSTRVL(M_rstRSSET.getString("PT_SALVL"),""));
				txtYTDCR.setText(nvlSTRVL(M_rstRSSET.getString("PT_YTDCR"),""));
				txtYOPCR.setText(nvlSTRVL(M_rstRSSET.getString("PT_YOPCR"),""));
				txtYTDDB.setText(nvlSTRVL(M_rstRSSET.getString("PT_YTDDB"),""));
				txtYOPDB.setText(nvlSTRVL(M_rstRSSET.getString("PT_YOPDB"),""));
				//txtDOCRF.setText(nvlSTRVL(M_rstRSSET.getString("PT_DOCRF"),""));
				txtPURVL.setText(nvlSTRVL(M_rstRSSET.getString("PT_PURVL"),""));
				txtTRNFL.setText(nvlSTRVL(M_rstRSSET.getString("PT_TRNFL"),""));
		}
			
		M_strSQLQRY="SELECT PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP='D'"
			+" AND PT_PRTCD = '"+txtDSRCD.getText().trim()+"'";
		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET.next())
			txtDSRNM.setText(M_rstRSSET.getString("PT_PRTNM"));
		else
			txtDSRNM.setText("");
			
		M_strSQLQRY="Select PT_PRTNM From CO_PTMST Where PT_PRTTP='T'"
						+" and PT_PRTCD = '"+txtTRNCD.getText()+"'";
		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET.next())
			txtTRNNM.setText(M_rstRSSET.getString("PT_PRTNM"));
		else
			txtTRNNM.setText("");
			
		M_strSQLQRY="SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP='MST'"
			+" AND CMT_CGSTP='COXXSTA' AND CMT_CODCD='"+txtSTACD.getText().toString().trim()+"'";
		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET.next())
			txtSTANM.setText(M_rstRSSET.getString("CMT_CODDS"));
		else
			txtSTANM.setText("");
		
		M_strSQLQRY="SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP='MST'"
			+" AND CMT_CGSTP='COXXCNT' AND CMT_CODCD='"+txtCNTCD.getText().toString().trim()+"'";
		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET.next())
			txtCNTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
		else
			txtCNTNM.setText("");
		

		int L_intCOUNT=0;
		((cl_JTBL)tblSCTDL).clrTABLE();
		M_strSQLQRY="SELECT * FROM CO_RGSTR WHERE PTS_DOCRF='"+txtDOCNO.getText()+"'"
			+" AND PTS_PRTTP='"+cmbPRTTP.getSelectedItem().toString().substring(0,1).trim()+"'";
		ResultSet rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(rstRSSET!=null)
		{
			while(rstRSSET.next())
			{
				//tblRTDTL.setValueAt((rstRSSET.getString("PPR_ADDFL").equals("Y")? TRUE : FALSE) ,0,0);
                tblSCTDL.setValueAt(rstRSSET.getString("PTS_EUSCD"),L_intCOUNT,TB1_EUSCD);
                tblSCTDL.setValueAt(rstRSSET.getString("PTS_EUSDS"),L_intCOUNT,TB1_EUSDS);
				tblSCTDL.setValueAt(nvlSTRVL(rstRSSET.getString("PTS_CAPQT"),""),L_intCOUNT,TB1_CAPQT);
				tblSCTDL.setValueAt(new Boolean(true),L_intCOUNT,TB1_CHKFL);
				if(nvlSTRVL(rstRSSET.getString("PTS_AONDT"),"").equals(""))
					tblSCTDL.setValueAt("",L_intCOUNT++,TB1_AONDT);
				else
                    tblSCTDL.setValueAt(M_fmtLCDAT.format(rstRSSET.getDate("PTS_AONDT")),L_intCOUNT++,TB1_AONDT);
			}
		}
		revalidate();
		updateUI();
		
	}//END dspDATA

	/**
	 */
	void exeSAVE()
	{
		try
		{
			if (!getDOCNO())
				return;
			inlTBLEDIT(tblSCTDL);
			cl_dat.M_flgLCUPD_pbst=true;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(!vldDATA())
					return;
				saveRGMST();
				for(int L_intROW=0;L_intROW<tblSCTDL.getRowCount();L_intROW++)
				{
					if(tblSCTDL.getValueAt(L_intROW,TB1_CHKFL).equals(new Boolean(true)))
						saveRGSTR(L_intROW);
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				int L_intOPTN=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Party : "+txtDOCNO.getText().toString(), 
						"Confirm Deletion", JOptionPane.YES_NO_OPTION);
				if(L_intOPTN!=0)
					return;
				strWHRSTR =  "PT_PRTTP = '" +cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase()+"' and "
							+"PT_DOCRF = '" +txtDOCNO.getText()+"'";
					
				cl_dat.exeSQLUPD("Update CO_RGMST Set PT_STSFL = 'X' where " + strWHRSTR,"setLCLUPD");

				strWHRSTR =  "PTS_PRTTP = '" +cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase()+"' and "
							+"PTS_DOCRF = '" +txtDOCNO.getText()+"'";
				cl_dat.exeSQLUPD("Update CO_RGSTR Set PTS_STSFL = 'X' where " + strWHRSTR,"");
				
			}
			if(!cl_dat.M_flgLCUPD_pbst)
				{cl_dat.M_conSPDBA_pbst.rollback();	return;}
			cl_dat.M_conSPDBA_pbst.commit();
			clrCOMP();
		}catch(Exception LP_EX)
		{
			System.out.println("Error in exeSAVE "+LP_EX);
		}
	}

	/**
	 */
	private boolean getDOCNO()  throws Exception
	{
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			return true;
		
		M_strSQLQRY="SELECT SUBSTRING(CMT_CODCD,3,3) PT_DOCCT,  CMT_CCSVL PT_SRLNO  FROM CO_CDTRN WHERE CMT_CGMTP='DOC' AND CMT_CGSTP='COXXPTR' AND SUBSTRING(CMT_CODCD,1,1)='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"'";
		M_rstRSSET=cl_dat.exeSQLQRY("Select * from co_CDTRN where CMT_CGMTP='DOC' and CMT_CGSTP='COXXPTR' and cmt_CODCD='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"_"+strYRDGT+M_strSBSCD.substring(0,2)+"'");
		System.out.println("Select * from co_CDTRN where CMT_CGMTP='DOC' and CMT_CGSTP='COXXPTR' and cmt_CODCD='"+strYRDGT+M_strSBSCD.substring(0,2)+"'");
		if(M_rstRSSET==null || (!M_rstRSSET.next()))
			{setMSG("DOC. series not found ..",'E'); cl_dat.M_flgLCUPD_pbst = false; return false;}
		String L_strDOCNO=null;
		if(getRSTVAL(M_rstRSSET,"CMT_CHP01","C").equals("N"))
				{setMSG("DOC Series is in Use. Please retry after some time ..",'E'); 	return false;}
		cl_dat.exeSQLUPD("Update co_cdtrn set cmt_CHP01='N'  WHERE CMT_CGMTP='DOC' AND CMT_CGSTP='COXXPTR' AND SUBSTRING(CMT_CODCD,1,1)='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"'","setLCLUPD");
		System.out.println("1 : "+getRSTVAL(M_rstRSSET,"CMT_CCSVL","N"));
		L_strDOCNO=Integer.toString(Integer.parseInt(getRSTVAL(M_rstRSSET,"CMT_CCSVL","N"))+1);
		System.out.println("2 : "+L_strDOCNO);
		txtDOCNO.setText(L_strDOCNO);
		if(L_strDOCNO.length()<5)
		{
			for(int i=0;i<=5;i++)
			{
				txtDOCNO.setText("0"+txtDOCNO.getText());
				if(txtDOCNO.getText().length()==5)
					break;
			}
		}
		L_strDOCNO = txtDOCNO.getText();
		txtDOCNO.setText(getRSTVAL(M_rstRSSET,"CMT_CODCD","C").substring(2,5)+L_strDOCNO);
		return true;
	}
	
	
	

	/**
	 */
	public  Statement chkCONSTM1(Connection LP_CONVAL)
    {
		Statement L_stmSPXXA = null;
		try
        {
			if (LP_CONVAL != null)
			{
			   LP_CONVAL.setAutoCommit(true);
			   L_stmSPXXA = LP_CONVAL.createStatement();
			}
		}catch(Exception LP_EX)
		{
			System.out.println("Error in chkCONSTM1 :"+LP_EX);
		}	
        return L_stmSPXXA;             
	}
	
	
	/**
	 */
	private int getRECCNT(String LP_SQLSTR)
	{
		int L_RETVAL = -1;
		try{
			ResultSet M_rstRSSET_pbst = cl_dat.exeSQLQRY1(LP_SQLSTR);
			if(M_rstRSSET_pbst.next())
			  L_RETVAL = M_rstRSSET_pbst.getInt(1);
			M_rstRSSET_pbst.close();
	    }catch (Exception L_EX){
			System.out.println("getRECCNT: "+L_EX);				
	        L_RETVAL = -1;
	       }
		return L_RETVAL;
	}
	
	boolean vldDATA()
	{
		if(txtDOCNO.getText().toString().length()==0)
		{
			setMSG("Doc.No. Generation Failure",'E');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				txtPRTNM.requestFocus();
			else
				txtDOCNO.requestFocus();
			return false;
		}
		else if(cmbPRTTP.getSelectedIndex()==0)
		{
			setMSG("Please Select Party Type",'E');
			cmbPRTTP.requestFocus();
			return false;
		}
		
		else if(txtPRTNM.getText().toString().length()==0)
		{
			setMSG("Please enter Party Name",'E');
			txtPRTNM.requestFocus();
			return false;
		}
		else if(txtADR01.getText().toString().length()==0)
		{
			setMSG("Please enter Address 1",'E');
			txtADR01.requestFocus();
			return false;
		}
		else if(txtCTYNM.getText().toString().length()==0)
		{
			setMSG("Please enter City Name",'E');
			txtCTYNM.requestFocus();
			return false;
		}
		else if(txtSTACD.getText().toString().length()==0)
		{
			setMSG("Please enter State Code",'E');
			txtSTACD.requestFocus();
			return false;
		}
		else if(txtCNTCD.getText().toString().length()==0)
		{
			setMSG("Please enter Country Code",'E');
			txtCNTCD.requestFocus();
			return false;
		}
		else if(txtDSTCD.getText().toString().length()==0)
		{
			setMSG("Please enter Destination Code",'E');
			txtDSTCD.requestFocus();
			return false;
		}
		else if(txtGRPCD.getText().toString().length()==0)
		{
			setMSG("Please enter Group Code",'E');
			txtGRPCD.requestFocus();
			return false;
		}
		else if(txtSHRNM.getText().toString().length()==0)
		{
			setMSG("Please enter Short Name",'E');
			txtSHRNM.requestFocus();
			return false;
		}
		else if(txtZONCD.getText().toString().length()==0)
		{
			setMSG("Please enter Zone Code",'E');
			txtZONCD.requestFocus();
			tbpDETAIL.setSelectedIndex(1);
			return false;
		}
		else
		{
			for(int L_intROW=0;L_intROW<tblSCTDL.getRowCount();L_intROW++)
			{
				if(tblSCTDL.getValueAt(L_intROW,TB1_CHKFL).equals(new Boolean(true)))
				{
					if(tblSCTDL.getValueAt(L_intROW,TB1_EUSCD).toString().equals(""))
					{
						setMSG("Please enter Sector Code",'E');
						return false;
					}
					else if(tblSCTDL.getValueAt(L_intROW,TB1_EUSDS).toString().equals(""))
					{
						setMSG("Please select Sector Details",'E');
						return false;
					}
					else if(tblSCTDL.getValueAt(L_intROW,TB1_CAPQT).toString().equals(""))
					{
						setMSG("Please enter Capacity",'E');
						return false;
					}
					else if(tblSCTDL.getValueAt(L_intROW,TB1_AONDT).toString().equals(""))
					{
						setMSG("Please enter AONDT Date in table",'E');
						return false;
					}
				}
			}
		}
		return true;
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
		{setMSG(L_EX,"getRSTVAL");}
	return " ";
	} 

	void setENBL(boolean LP_flgSTAT)
	{
		try
		{
			super.setENBL(LP_flgSTAT);
			txtEXCNO.setEnabled(false);
		}catch(Exception e)
		{
			System.out.println("Err in child.setENBL :"+e);
		}
	}
	void setACTIVATE()
	{
		txtDOCNO.setText("");txtPRTNM.setText("");txtSHRNM.setText("");txtDSTCD.setText("");
		txtADR01.setText("");txtADR02.setText("");txtADR03.setText("");
		txtADR04.setText("");txtCTYNM.setText("");txtPINCD.setText("");
		txtSTACD.setText("");txtSTANM.setText("");txtCNTCD.setText("");
		txtCNTNM.setText("");txtTRNCD.setText("");txtTRNNM.setText("");
		txtDSRCD.setText("");txtDSRNM.setText("");txtGRPCD.setText("");
		txtCONNM.setText("");txtMOBNO.setText("");txtEMLRF.setText("");
		txtCOWEB.setText("");txtEMLPR.setText("");txtEMLAC.setText("");
		txtTEL01.setText("");txtTEL02.setText("");txtFAXNO.setText("");
		txtSTXNO.setText("");txtSTXDT.setText("");txtCSTNO.setText("");
		txtCSTDT.setText("");txtDIVDS.setText("");txtRNGDS.setText("");
		txtCLLDS.setText("");txtECCNO.setText("");txtITPNO.setText("");
		txtEXCNO.setText("");txtZONCD.setText("");txtZONDS.setText("");
		txtSALVL.setText("");txtTRNFL.setText("");txtYTDCR.setText("");
		txtYTDDB.setText("");txtYOPCR.setText("");txtYOPDB.setText("");
		//txtDOCRF.setText("");txtPURVL.setText("");txtCLSCD.setText("");
		tblSCTDL.clrTABLE();
	}

	/**
	 */
	void saveRGMST()
	{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;

		strWHRSTR =  "PT_PRTTP = '" +cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase()+"' and "
					 +"PT_DOCRF = '" +txtDOCNO.getText()+"'";
		flgCHK_EXIST =  chkEXIST("CO_RGMST", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_PTMST");
				return;
		}
		
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY= "INSERT INTO CO_RGMST ("
			+"PT_PRTTP,PT_DOCRF,PT_PRTNM,PT_SHRNM,PT_GRPCD,"
			+"PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_PINCD,"
			+"PT_CTYNM,PT_STACD,PT_CNTCD,PT_CONNM,PT_TEL01,PT_TEL02,"
			+"PT_EMLRF,PT_FAXNO,PT_INFFL,PT_STXNO,PT_CSTNO,"
			+"PT_STXDT,PT_CSTDT,PT_CLSCD,PT_SCRCD,PT_ECCNO,"
			+"PT_ITPNO,PT_EXCNO,PT_RNGDS,PT_DIVDS,PT_CLLDS,"
			+"PT_ZONCD,PT_TRNCD,PT_TSTFL,"//PT_SALVL,PT_YOPCR,
			+"PT_DSTCD,PT_MOBNO,"//PT_YOPDB,PT_YTDCR,PT_YTDDB,
			+"PT_COWEB,PT_EMLPR,PT_EMLMR,PT_EMLAC,PT_DSRCD,"
			+"PT_TRNFL,PT_STSFL,PT_LUSBY,"//PT_PURVL,
			+"PT_LUPDT) values (" 
			+setINSSTR("PT_PRTTP",cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase(),"C")
			+setINSSTR("PT_PRTCD",txtDOCNO.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_PRTNM",txtPRTNM.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_SHRNM",txtSHRNM.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_GRPCD",txtGRPCD.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_ADR01",txtADR01.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_ADR02",txtADR02.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_ADR03",txtADR03.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_ADR04",txtADR04.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_PINCD",txtPINCD.getText().toString().trim(),"C")
			+setINSSTR("PT_CTYNM",txtCTYNM.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_STACD",txtSTACD.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_CNTCD",txtCNTCD.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_CONNM",txtCONNM.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_TEL01",txtTEL01.getText().toString().trim(),"C")
			+setINSSTR("PT_TEL02",txtTEL02.getText().toString().trim(),"C")
			+setINSSTR("PT_EMLRF",txtEMLRF.getText().toString().trim(),"C")
			+setINSSTR("PT_FAXNO",txtFAXNO.getText().toString().trim(),"C")
			+setINSSTR("PT_INFFL",(rdbINDFL.isSelected() ? "I" : "F"),"C")
			+setINSSTR("PT_STXNO",txtSTXNO.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_CSTNO",txtCSTNO.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_STXDT",txtSTXDT.getText().toString(),"D")
			+setINSSTR("PT_CSTDT",txtCSTDT.getText().toString(),"D")
			+setINSSTR("PT_CLSCD","01","C") //txtCLSCD.getText().toString().trim().toUpperCase()
			+setINSSTR("PT_SCRCD","01","C") //txtSCRCD.getText().toString().trim().toUpperCase()
			+setINSSTR("PT_ECCNO",txtECCNO.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_ITPNO",txtITPNO.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_EXCNO",txtEXCNO.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_RNGDS",txtRNGDS.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_DIVDS",txtDIVDS.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_CLLDS",txtCLLDS.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_ZONCD",txtZONCD.getText().toString().trim(),"C")
			+setINSSTR("PT_TRNCD",txtTRNCD.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_TSTFL",(rdbTSTYES.isSelected() ? "Y" : "N"),"C")
			+setINSSTR("PT_DSTCD",txtDSTCD.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_MOBNO",txtMOBNO.getText().toString().trim(),"C")
			+setINSSTR("PT_COWEB",txtCOWEB.getText().toString().trim(),"C")
			+setINSSTR("PT_EMLPR",txtEMLPR.getText().toString().trim(),"C")
			+setINSSTR("PT_EMLMR",txtEMLMR.getText().toString().trim(),"C")
			+setINSSTR("PT_EMLAC",txtEMLAC.getText().toString().trim(),"C")
			+setINSSTR("PT_DSRCD",txtDSRCD.getText().toString().trim().toUpperCase(),"C")
			+setINSSTR("PT_TRNFL","1","C") // PT_TRNFL
			+setINSSTR("PT_STSFL","0","C") // PT_STSFL
			+setINSSTR("PT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')"; // PT_LUPDT
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY = "update CO_RGMST set "
			+setUPDSTR("PT_PRTNM",txtPRTNM.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_SHRNM",txtSHRNM.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_GRPCD",txtGRPCD.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_ADR01",txtADR01.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_ADR02",txtADR02.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_ADR03",txtADR03.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_ADR04",txtADR04.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_PINCD",txtPINCD.getText().toString().trim(),"C")
			+setUPDSTR("PT_CTYNM",txtCTYNM.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_STACD",txtSTACD.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_CNTCD",txtCNTCD.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_CONNM",txtCONNM.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_TEL01",txtTEL01.getText().toString().trim(),"C")
			+setUPDSTR("PT_TEL02",txtTEL02.getText().toString().trim(),"C")
			+setUPDSTR("PT_EMLRF",txtEMLRF.getText().toString().trim(),"C")
			+setUPDSTR("PT_FAXNO",txtFAXNO.getText().toString().trim(),"C")
			+setUPDSTR("PT_INFFL",(rdbINDFL.isSelected() ? "I" : "F"),"C")
			+setUPDSTR("PT_STXNO",txtSTXNO.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_CSTNO",txtCSTNO.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_STXDT",txtSTXDT.getText().toString(),"D")
			+setUPDSTR("PT_CSTDT",txtCSTDT.getText().toString(),"D")
			+setUPDSTR("PT_CLSCD","01","C") //txtCLSCD.getText().toString().trim().toUpperCase()
			+setUPDSTR("PT_SCRCD","01","C") //txtSCRCD.getText().toString().trim().toUpperCase()
			+setUPDSTR("PT_ECCNO",txtECCNO.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_ITPNO",txtITPNO.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_EXCNO",txtEXCNO.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_RNGDS",txtRNGDS.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_DIVDS",txtDIVDS.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_CLLDS",txtCLLDS.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_ZONCD",txtZONCD.getText().toString().trim(),"C")
			+setUPDSTR("PT_TRNCD",txtTRNCD.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_TSTFL",(rdbTSTYES.isSelected() ? "Y" : "N"),"C")
			+setUPDSTR("PT_DSTCD",txtDSTCD.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_MOBNO",txtMOBNO.getText().toString().trim(),"C")
			+setUPDSTR("PT_COWEB",txtCOWEB.getText().toString().trim(),"C")
			+setUPDSTR("PT_EMLPR",txtEMLPR.getText().toString().trim(),"C")
			+setUPDSTR("PT_EMLMR",txtEMLMR.getText().toString().trim(),"C")
			+setUPDSTR("PT_EMLAC",txtEMLAC.getText().toString().trim(),"C")
			+setUPDSTR("PT_DSRCD",txtDSRCD.getText().toString().trim().toUpperCase(),"C")
			+setUPDSTR("PT_TRNFL","1","C") // PT_TRNFL
			+setUPDSTR("PT_STSFL","0","C") // PT_STSFL
			+setUPDSTR("PT_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+"PT_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where "+strWHRSTR;
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
		catch (Exception L_EX)
			{setMSG("Error in exeSAVE : "+L_EX,'E');}
	}

	/**
	 */	
	void saveRGSTR(int LP_ROWNO)
	{
	try
	{
		if(!cl_dat.M_flgLCUPD_pbst)
			return;

		strWHRSTR =  "PTS_PRTTP = '" +cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase()+"' and "
					 +"PTS_DOCRF = '" +txtDOCNO.getText()+"'";
		flgCHK_EXIST =  chkEXIST("CO_PTSTR", strWHRSTR);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)
		{
				JOptionPane.showMessageDialog(this,"Record alreay exists in CO_PTMST");
				return;
		}
		
		inlTBLEDIT(tblSCTDL);
		if(!flgCHK_EXIST)
		{
			M_strSQLQRY= "INSERT INTO CO_RGSTR ("
			+"PTS_PRTTP,PTS_DOCRF,PTS_EUSCD,PTS_EUSDS,"
			+"PTS_CAPQT,PTS_AONDT,PTS_TRNFL,PTS_STSFL,PTS_LUSBY,PTS_LUPDT) Values("
			+setINSSTR("PTS_PRTTP",cmbPRTTP.getSelectedItem().toString().substring(0,1).toUpperCase(),"C")
			+setINSSTR("PTS_DOCRF",txtDOCNO.getText().toString().trim(),"C")
			+setINSSTR("PTS_EUSCD",tblSCTDL.getValueAt(LP_ROWNO,TB1_EUSCD).toString(),"C")
			+setINSSTR("PTS_EUSDS",tblSCTDL.getValueAt(LP_ROWNO,TB1_EUSDS).toString(),"C")
			+setINSSTR("PTS_CAPQT",tblSCTDL.getValueAt(LP_ROWNO,TB1_CAPQT).toString(),"N")
			+setINSSTR("PTS_AONDT",tblSCTDL.getValueAt(LP_ROWNO,TB1_AONDT).toString(),"D")
			+setINSSTR("PTS_TRNFL","1","C")
			+setINSSTR("PTS_STSFL","0","C")
			+setINSSTR("PTS_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";   //PTS_LUPDT
		}
		else if(flgCHK_EXIST)
		{
			M_strSQLQRY= "UPDATE  CO_RGSTR set "
			+setUPDSTR("PTS_EUSCD",tblSCTDL.getValueAt(LP_ROWNO,TB1_EUSCD).toString(),"C")
			+setUPDSTR("PTS_EUSDS",tblSCTDL.getValueAt(LP_ROWNO,TB1_EUSDS).toString(),"C")
			+setUPDSTR("PTS_CAPQT",tblSCTDL.getValueAt(LP_ROWNO,TB1_CAPQT).toString(),"N")
			+setUPDSTR("PTS_AONDT",tblSCTDL.getValueAt(LP_ROWNO,TB1_AONDT).toString(),"D")
			+setUPDSTR("PTS_TRNFL","1","C")
			+setUPDSTR("PTS_STSFL","0","C")
			+setUPDSTR("PTS_LUSBY",cl_dat.M_strUSRCD_pbst,"C")
			+" PTS_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where "+strWHRSTR;   //PTS_LUPDT
		}
		System.out.println(M_strSQLQRY);
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	}
		catch (Exception L_EX)
		{setMSG("Error in exeSAVE : "+L_EX,'E');}
	
	}

//****************************************************************************************
	// Common methods (in co_meptm.java and co_meptr.java) to be shifted to Custom Class
	/** Generating string for Insertion Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 */
	private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
	try {
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
	private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 
	{
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
	
	/** Checking key in table for record existance
	 */
	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
	{
		boolean L_flgCHKFL = false;
		try
		{
			M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
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
	
}//end CO_meptm
		