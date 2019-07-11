/*
System Name   : Material Management System
Program Name  : Customer Complaint Analysis Entry
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 2/11/2005
System        : 
Version       : MMS v2.0.0
Modificaitons :
Modified By   :
Modified Date :
Modified det. :
Version       :
*/

import javax.swing.JTextField;import javax.swing.JTextArea;import javax.swing.JTabbedPane;
import javax.swing.JPanel;import javax.swing.JTextArea;import javax.swing.JLabel;import javax.swing.JScrollPane;
import javax.swing.JCheckBox;import javax.swing.JComboBox;
import javax.swing.BorderFactory;import javax.swing.border.TitledBorder;import javax.swing.JButton;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.awt.Color;import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
import java.sql.ResultSet;

import javax.swing.InputVerifier;import javax.swing.JComponent;

class co_tecrg extends cl_pbase
{
    private JPanel pnlCPDTL,pnlIMDAC,pnlCOMCS,pnlCOMED,pnlCUST,pnlOTHR;
    private JTabbedPane jtbMAIN = new JTabbedPane();
    private JTabbedPane jtbREGDT = new JTabbedPane();    
    private JTextField txtREGNO;
	private JTextField txtBRNGR;
	private JTextField txtREGDT;
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;	
	private JTextField txtCONPR;
	private JTextField txtEMLID;
	private JTextField txtZONCD;
    private JTextField txtADDR1;
	private JTextField txtADDR2;	
	private JTextField txtPHNNO;
	private JTextField txtCELNO;
	private JTextField txtFAXNO;
    private JTextField txtINVNO;
	private JTextField txtINVDT;
	private JTextField txtDORNO;
	private JTextField txtDORDT;
	private JTextField txtTPRCD;
	private JTextField txtTPRNM;
    private JTextField txtLOTNO;
	private JTextField txtPRDCD;
	private JTextField txtPRDDS;
	private JTextField txtINVQT;
	private JTextField txtSMPQT;    
    private JTextField txtCMPQT;
	private JTextField txtADLOT;
	private JTextField txtINDNO;
	private JTextField txtATACH;
	private JTextField txtGRNNO;
	private JTextField txtOAREF;
	private JTextField txtBATNO;
	private JTextField txtSTRTP;
	private JTextField txtPORNO;
	private JTextField txtPORDT;	
	private JTextField txtMATDS;
	
    private JTextField txtUSR1;
	private JTextField txtUSR2;
	private JTextField txtUSR3;
	private JTextField txtUSR4;
	private JTextField txtDAT1;
	private JTextField txtDAT2;
	private JTextField txtDAT3;
	private JTextField txtDAT4;			
	
	private JTextArea txaCMDTL;
	private JTextArea txaIMDAC;
	private JTextArea txaCOMCS;
	private JTextArea txaCOMED;
	
    private JButton btnPRNT;
    private JComboBox cmbCMPTP;
	private JComboBox cmbYESNO;
	private JComboBox cmbCMPCT;
	private JCheckBox chkPARTB;
	private JCheckBox chkSLRTN;
	
	private JLabel lblDORNO;
	private JLabel lblDORDT;
	private JLabel lblPRDCD;
	private JLabel lblSTAT;
	private JLabel lblADDLT;
	private JLabel lblOAREF;
	private JLabel lblTPRCD;
	private JLabel lblBATNO;	
	private JLabel lbl1,lbl2,lbl3,lbl4;
	
	private String strSTSVL;
	private String strSTSFL;
	private Hashtable<String,String> hstSTRTP;
	private Hashtable<String,String> hstCMPTP;
	private Hashtable<String,String> hstZONDS;	
	private Hashtable<String,String> hstSTSDS;
	private Hashtable<String,String> hstCMPCT;
	private String strRMRK1="";
	private String strRMRK2="";
	private String strRMRK3="";
	private String strRMRK4="";
	private String strCMPTP="";
	private String strREGNO="";
	private String strBRNGR="";
	private INPVF objINPVR = new INPVF();
	
	private final int TBL_CHKFL = 0;
	private final int TBL_DPTCD = 1;
	private final int TBL_DPTNM = 2;
	private final int TBL_CAPA = 3;		
	private int intROWCT = 40;
	private String strINVBY = "";		// Departments	
	private String strYESNO = "No";
	private String strPRTB = "No";
	private String strEDCOM = "No";
	private String strCMPCT = "";		
	private String arrCMPTP[] = new String[50];
	String strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasA.html";
    co_tecrg()
    {
		super(2);
		try
		{		
			setMatrix(20,8);			
		    pnlCPDTL = new JPanel();pnlCPDTL.setLayout(null);
		    pnlIMDAC = new JPanel();pnlIMDAC.setLayout(null);
		    pnlCOMCS = new JPanel();pnlCOMCS.setLayout(null);
		    pnlCOMED = new JPanel();pnlCOMED.setLayout(null);
		    pnlCUST = new JPanel();pnlCUST.setLayout(null);
		    pnlOTHR = new JPanel();pnlOTHR.setLayout(null);
		    
		    txaCMDTL = new TxtAreaLimit(1000);
		    txaCOMCS = new TxtAreaLimit(1000);
		    txaCOMED = new TxtAreaLimit(1000);
		    txaIMDAC = new TxtAreaLimit(1000); 
		 
		    add(new JLabel("Complaint Type"),1,1,1,2,this,'L');
			add(cmbCMPTP = new JComboBox(),1,2,1,1.2,this,'L');			
		    add(new JLabel("Reg. No."),1,3,1,.7,this,'R');
		   	add(txtREGNO = new TxtNumLimit(8),1,4,1,1,this,'L');
		    add(new JLabel("Branch Reg."),1,5,1,1,this,'R');
			add(txtBRNGR = new TxtLimit(8),1,6,1,1.2,this,'L');
		  	add(btnPRNT = new JButton("PRINT"),1,8,1,1,this,'L');
		  	
		  	add(new JLabel("Reported On"),2,1,1,1,this,'L');
			add(txtREGDT = new TxtDate(),2,2,1,1.2,this,'L');  			
		  	add(new JLabel("Zone Code"),2,3,1,.7,this,'R');
			add(txtZONCD = new TxtLimit(2),2,4,1,1,this,'L');
			add(new JLabel("Comp Category"),2,5,1,1,this,'L');
			add(cmbCMPCT = new JComboBox(),2,6,1,2.5,this,'L');
		    
			add(new JLabel("Reg. Completed"),3,1,1,1,this,'R');
			add(cmbYESNO = new JComboBox(),3,2,1,1.2,this,'L');
			add(new JLabel("Status"),3,3,1,.7,this,'R');
			add(lblSTAT = new JLabel(""),3,4,1,3,this,'L');			
			cmbYESNO.addItem("No");
			cmbYESNO.addItem("Yes");
			
			// Party Details
			add(new JLabel("Party Code"),1,1,1,1,pnlCUST,'L');
			add(txtPRTCD = new TxtLimit(5),1,2,1,1,pnlCUST,'L');
			add(new JLabel("Party Name"),1,3,1,.9,pnlCUST,'R');  
		    add(txtPRTNM = new TxtLimit(40),1,4,1,4,pnlCUST,'L');  
				   
		    add(new JLabel("Address"),2,1,1,1,pnlCUST,'L');
		    add(txtADDR1 = new TxtLimit(200),2,2,1,6,pnlCUST,'L');  
		    add(txtADDR2 = new TxtLimit(200),3,2,1,6,pnlCUST,'L');			

 			add(new JLabel("Contact Person"),4,1,1,1,pnlCUST,'L');
			add(txtCONPR = new TxtLimit(30),4,2,1,2.3,pnlCUST,'L');
			add(new JLabel("Phone No"),4,4,1,.7,pnlCUST,'R');
			add(txtPHNNO = new TxtLimit(15),4,5,1,3,pnlCUST,'L');
			
			add(new JLabel("E-Mail"),5,1,1,1,pnlCUST,'L');
			add(txtEMLID = new TxtLimit(30),5,2,1,2.3,pnlCUST,'L');			
			add(new JLabel("Cell No"),5,4,1,.7,pnlCUST,'R');
			add(txtCELNO = new TxtLimit(10),5,5,1,1,pnlCUST,'L');
			add(new JLabel("Fax No."),5,6,1,.6,pnlCUST,'R');
			add(txtFAXNO = new TxtLimit(15),5,7,1,1,pnlCUST,'L');
			
		    add(pnlCUST,2,1,4,8,this,'L');
			add(new JLabel("Sub System"),1,1,1,1,pnlOTHR,'L');
		    add(txtSTRTP = new JTextField(),1,2,1,1,pnlOTHR,'L');  			
		    add(new JLabel("Invoice No."),1,3,1,1,pnlOTHR,'R');
		    add(txtINVNO = new TxtNumLimit(8),1,4,1,1,pnlOTHR,'L');  
		    add(new JLabel("Invoice Date"),1,5,1,1,pnlOTHR,'R');
		    add(txtINVDT = new TxtDate(),1,6,1,1,pnlOTHR,'L');  
		    add(new JLabel("Invoice Qty"),1,7,1,.9,pnlOTHR,'R');
		    add(txtINVQT = new TxtNumLimit(9.3),1,8,1,.9,pnlOTHR,'R');
		    
		    add(lblDORNO = new JLabel("D.O. No."),2,1,1,1,pnlOTHR,'L');
		    add(txtDORNO = new TxtNumLimit(8),2,2,1,1,pnlOTHR,'L');  
			add(txtPORNO = new TxtNumLimit(8),2,2,1,1,pnlOTHR,'L');  
		    add(lblDORDT = new JLabel("D.O Date"),2,3,1,1,pnlOTHR,'R');
		    add(txtDORDT = new TxtDate(),2,4,1,1,pnlOTHR,'L');  
			add(txtPORDT = new TxtDate(),2,4,1,1,pnlOTHR,'L');
		    add(new JLabel("Complaint Qty"),2,5,1,1,pnlOTHR,'L');
		    add(txtCMPQT = new TxtNumLimit(9.3),2,6,1,1,pnlOTHR,'L');  
		    add(new JLabel("Sample Qty"),2,7,1,.9,pnlOTHR,'R');
		    add(txtSMPQT = new TxtNumLimit(9.3),2,8,1,.9,pnlOTHR,'R');  
		    
		    add(lblPRDCD = new JLabel("Product"),3,1,1,1,pnlOTHR,'L');
		    add(txtPRDCD = new TxtLimit(10),3,2,1,1,pnlOTHR,'L');  
		    add(txtPRDDS = new JTextField(),3,3,1,2,pnlOTHR,'L');  
			add(txtMATDS = new JTextField(),3,3,1,4,pnlOTHR,'L');  
		    add(lblTPRCD = new JLabel("Transporter"),3,5,1,1,pnlOTHR,'L');
		    add(txtTPRCD = new TxtLimit(5),3,6,1,1,pnlOTHR,'L');
			add(lblBATNO = new JLabel("Batch No"),3,7,1,.9,pnlOTHR,'R');
			add(txtBATNO = new TxtLimit(5),3,8,1,.9,pnlOTHR,'R');
		    add(txtTPRNM = new JTextField(),3,7,1,1.9,pnlOTHR,'L');  
		          
		    add(new JLabel("Lot No."),4,1,1,1,pnlOTHR,'L');
		    add(txtLOTNO = new TxtNumLimit(8),4,2,1,1,pnlOTHR,'L');  
		    add(lblADDLT = new JLabel("Additional Lots."),4,3,1,1,pnlOTHR,'L');
		    add(txtADLOT = new JTextField(),4,4,1,3,pnlOTHR,'L');  						
			add(txtGRNNO = new TxtNumLimit(8),4,4,1,1,pnlOTHR,'L');
			add(lblOAREF = new JLabel("O.A. Ref."),4,5,1,1,pnlOTHR,'L');
			add(txtOAREF = new JTextField(),4,6,1,1,pnlOTHR,'L'); 
			add(new JLabel("Indent No."),4,7,1,.9,pnlOTHR,'R');
		    add(txtINDNO = new TxtNumLimit(8),4,8,1,.9,pnlOTHR,'R');
			
		    add(chkSLRTN = new JCheckBox("Sales Return"),5,7,1,1.1,pnlOTHR,'L');  
			add(chkPARTB = new JCheckBox("Part B Req."),5,8,1,1,pnlOTHR,'L');
			
		    add(new JLabel("Attachments"),5,1,1,1,pnlOTHR,'L');
		    add(txtATACH = new JTextField(),5,2,1,5,pnlOTHR,'L');  
		     
			add(new JScrollPane(txaCMDTL,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),2,2,4,6,pnlCPDTL,'L');
			add(lbl1 = new JLabel("Details Of the Complaint"),1,2,1,2,pnlCPDTL,'L');
			add(new JLabel("Added By"),1,4,1,.7,pnlCPDTL,'R');
		    add(txtUSR1 = new JTextField(),1,5,1,1,pnlCPDTL,'L');  
		    add(new JLabel("Date"),1,6,1,.5,pnlCPDTL,'R');
		    add(txtDAT1 = new TxtDate(),1,7,1,1,pnlCPDTL,'L');  

			add(new JScrollPane(txaIMDAC,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),2,2,4,6,pnlIMDAC,'L');
			add(lbl2 = new JLabel("Details Of the Immediate Action"),1,2,1,2,pnlIMDAC,'L');
		    add(new JLabel("Added By"),1,4,1,.7,pnlIMDAC,'R');
		    add(txtUSR2 = new JTextField(),1,5,1,1,pnlIMDAC,'L');
			add(new JLabel("Date"),1,6,1,.5,pnlIMDAC,'R');
		    add(txtDAT2 = new TxtDate(),1,7,1,1,pnlIMDAC,'L');
		  
			add(new JScrollPane(txaCOMCS,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),2,2,4,6,pnlCOMCS,'L');
			add(lbl3 = new JLabel("Comments of the CSS Dept. Head"),1,2,1,2.3,pnlCOMCS,'L');
		    add(new JLabel("Added By"),1,4,1,.7,pnlCOMCS,'R');
		    add(txtUSR3 = new TxtLimit(3),1,5,1,1,pnlCOMCS,'L');
			add(new JLabel("Date"),1,6,1,.5,pnlCOMCS,'R');
		    add(txtDAT3 = new TxtDate(),1,7,1,1,pnlCOMCS,'L');
		       
			add(new JScrollPane(txaCOMED,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),2,2,4,6,pnlCOMED,'L');
			add(lbl4 = new JLabel("Comments entered by ED"),1,2,1,2,pnlCOMED,'L');
		    add(new JLabel("Added By"),1,4,1,.7,pnlCOMED,'R');
		    add(txtUSR4 = new TxtLimit(3),1,5,1,1,pnlCOMED,'L');
			add(new JLabel("Date"),1,6,1,.5,pnlCOMED,'R');
		    add(txtDAT4 = new TxtDate(),1,7,1,1,pnlCOMED,'L');
		  	    
		    jtbREGDT.addTab("Contact Details",pnlCUST);
		   	jtbREGDT.addTab("Registration Details ",pnlOTHR);
		    add(jtbREGDT,4,1,7,8,this,'L');
		    jtbMAIN.addTab("Details of Complaint",pnlCPDTL);
		   	jtbMAIN.addTab("Immediate Action",pnlIMDAC);			
		    jtbMAIN.addTab("Comments HOD(CSS)",pnlCOMCS);
		    jtbMAIN.addTab("Comments (ED)",pnlCOMED);
			add(jtbMAIN,12,1,7,8,this,'L');
			
			lblSTAT.setForeground(Color.blue);
			setENBL(false);
			
			lbl1.setForeground(Color.blue);
			lbl2.setForeground(Color.blue);
			lbl3.setForeground(Color.blue);
			lbl4.setForeground(Color.blue);
			txtGRNNO.setVisible(false);
			txtOAREF.setVisible(false);
			txtBATNO.setVisible(false);
			lblBATNO.setVisible(false);
			txtMATDS.setVisible(false);
			hstSTRTP =  new Hashtable<String,String>();
			M_strSQLQRY =" SELECT CMT_CODCD,CMT_SHRDS from CO_CDTRN where"
			+" CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MMXXSST' AND isnull(CMT_STSFL,'')<>'X'";	
			if(txtSTRTP.getText().length() >0)
				M_strSQLQRY += " AND CMT_CODCD = '"+txtSTRTP.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstSTRTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));
				M_rstRSSET.close();
			}
			hstZONDS = new Hashtable<String,String>();
			M_strSQLQRY =" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
				+" CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MR00ZON' AND isnull(CMT_STSFL,'')<>'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstZONDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP = 'A"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_CGSTP = 'COXXEDC' AND CMT_CODCD ='"+ cl_dat.M_strUSRCD_pbst +"'";		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					txaCOMED.setEnabled(true);
					txtUSR4.setText(cl_dat.M_strUSRCD_pbst);
					txtDAT4.setText(cl_dat.M_strLOGDT_pbst);
					setMSG("Please enter ED Comments..",'N');
					strEDCOM = "Yes";
					return;
				}
				else
					strEDCOM = "No";
			}
			else
				strEDCOM = "No";
		
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
			+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXCCT' order by CMT_CODCD ";
			M_rstRSSET= cl_dat.exeSQLQRY1(M_strSQLQRY);
			{
				hstCMPCT =  new Hashtable<String,String>();
				if(M_rstRSSET != null)
				{		
					int i = 0;
					String L_strCODCD = "";
					String L_strCODDS = "";
					while(M_rstRSSET.next())
					{
						L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
						L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");						
						arrCMPTP[i] = L_strCODCD;
						hstCMPCT.put(L_strCODCD,L_strCODDS);
						i++;
					}
					M_rstRSSET.close();
				}
			}			
					
			txtREGNO.setInputVerifier(objINPVR);
			txtPRTCD.setInputVerifier(objINPVR);
			txtINVNO.setInputVerifier(objINPVR);
			txtLOTNO.setInputVerifier(objINPVR);
			txtBRNGR.setInputVerifier(objINPVR);
			txaCMDTL.addKeyListener(this);
			txaIMDAC.addKeyListener(this);
			txaCOMCS.addKeyListener(this);
			txaCOMED.addKeyListener(this);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
    }
	/**
	 * Super Class method overrided to enable & disable the Components.
	 * @param L_flgSTAT boolean argument to pass State of the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{    
		try
		{
			super.setENBL(L_flgSTAT);
			if(L_flgSTAT == false)	
				return;					
			txtADDR1.setEnabled(false);
			txtADDR2.setEnabled(false);
			txtPRTNM.setEnabled(false);
			txtBRNGR.setEnabled(false);
			txtUSR1.setEnabled(false);
			txtUSR2.setEnabled(false);
			txtUSR3.setEnabled(false);
			txtUSR4.setEnabled(false);
			txtDAT1.setEnabled(false);
			txtDAT2.setEnabled(false);
			txtDAT3.setEnabled(false);
			txtDAT4.setEnabled(false);
			txtPRDDS.setEnabled(false);
			txtTPRNM.setEnabled(false);
			txtDORDT.setEnabled(false);
			txtPORDT.setEnabled(false);
			txtMATDS.setEnabled(false);
			txtINVDT.setEnabled(false);
			txtINVQT.setEnabled(false);
			txtTPRCD.setEnabled(false);
			txtINDNO.setEnabled(false);
			txtDORNO.setEnabled(false);
			txaCOMED.setEnabled(false);	
		
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
			{	
				txaCMDTL.setEnabled(false);
				txaIMDAC.setEnabled(false);
				txaCOMCS.setEnabled(false);
			}
			else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) && (strEDCOM.equals("Yes")))
				{					
					txaCOMED.setEnabled(true);
					txtUSR4.setText(cl_dat.M_strUSRCD_pbst);
					txtDAT4.setText(cl_dat.M_strLOGDT_pbst);
					setMSG("Please enter ED Comments..",'N');
					txaCMDTL.setEnabled(false);
					txaIMDAC.setEnabled(false);
					txaCOMCS.setEnabled(false);
					return;
				}
				txtBRNGR.setEnabled(true);
				txaCMDTL.setEnabled(true);
				txaIMDAC.setEnabled(true);
				txaCOMCS.setEnabled(true);					
				txtUSR1.setText(cl_dat.M_strUSRCD_pbst);
				txtUSR2.setText(cl_dat.M_strUSRCD_pbst);
				txtUSR3.setText(cl_dat.M_strUSRCD_pbst);
				
				txtDAT1.setText(cl_dat.M_strLOGDT_pbst);
				txtDAT2.setText(cl_dat.M_strLOGDT_pbst);
				txtDAT3.setText(cl_dat.M_strLOGDT_pbst);
			// commented temporarily
			///	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			///		txtREGNO.setEnabled(false);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setENBL");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);    
		try 
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() > 0)			
				{
					setENBL(true);
					if(cmbCMPTP.getItemCount() == 0)
					{
						String L_strCODCD="", L_strCODDS="";
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXCMT'";
						M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);						
						if(M_rstRSSET != null)
						{
							hstCMPTP = new Hashtable<String,String>();
							cmbCMPTP.removeActionListener(this);
							cmbCMPTP.addItem("Select");
							while(M_rstRSSET.next())
							{
								L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
								L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
								hstCMPTP.put(L_strCODCD,L_strCODDS);
								cmbCMPTP.addItem(L_strCODCD+" "+L_strCODDS);
							}
							cmbCMPTP.addActionListener(this);
							cmbCMPTP.setSelectedItem("02 Technical");
							M_rstRSSET.close();
						}
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP = 'STS' AND CMT_CGSTP = 'COXXCST'";
						M_rstRSSET= cl_dat.exeSQLQRY1(M_strSQLQRY);
						{
							if(M_rstRSSET != null)
							{
								hstSTSDS = new Hashtable<String,String>();
								while(M_rstRSSET.next())
									hstSTSDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
								M_rstRSSET.close();
							}
						}
					}
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
					{
						setENBL(false);
						cmbCMPTP.setEnabled(true);
						txtREGNO.setEnabled(true);
						btnPRNT.setEnabled(true);
					}				
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
						lblSTAT.setText("Fresh Entry");
					else
						lblSTAT.setText("");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						//txtREGNO.setEnabled(false);
						txtBRNGR.setEnabled(true);
						txtREGNO.requestFocus();
						//txtBRNGR.requestFocus();
					}
					else 
					{
						txtREGNO.setEnabled(true);
						txtREGNO.requestFocus();
					}
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == cmbCMPTP)
			{
				//clrCOMP();
				setENBL(true);
			// commented on 23/03/2006	
			//	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))				
			//		txtBRNGR.requestFocus();
			//	else 				
					txtREGNO.requestFocus();

				strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);				
				cmbCMPTP.setSelectedItem(strCMPTP+" "+hstCMPTP.get(strCMPTP).toString());
				txtGRNNO.setVisible(false);
				lblOAREF.setVisible(false);
				txtOAREF.setVisible(false);
				lblADDLT.setText("Additional Lots");

				txtZONCD.setEnabled(true);
				txtADLOT.setVisible(true);
				txtPORNO.setVisible(false);
				txtPORDT.setVisible(false);
				txtDORNO.setVisible(true);
				txtDORDT.setVisible(true);
				lblTPRCD.setVisible(true);
				lblBATNO.setVisible(false);
				txtTPRCD.setVisible(true);
				txtTPRNM.setVisible(true);
				txtBATNO.setVisible(false);
				txtMATDS.setVisible(false);
				txtPRDDS.setVisible(true);

				if(strCMPTP.equals("01"))
				{
					lblDORNO.setText("D.O. No");
					lblDORDT.setText("D.O. Date");
					lblPRDCD.setText("Product");
				}
				else if(strCMPTP.equals("02"))
				{
					lblDORNO.setText("D.O. No");
					lblDORDT.setText("D.O. Date");
					lblPRDCD.setText("Product");
				}
				else if(strCMPTP.equals("03"))	///Supplier complaint
				{
					txtMATDS.setVisible(true);
					txtPRDDS.setVisible(false);
					lblTPRCD.setVisible(false);
					lblBATNO.setVisible(true);
					txtTPRCD.setVisible(false);
					txtTPRNM.setVisible(false);
					txtBATNO.setVisible(true);					
					lblDORNO.setText("P.O. No");
					txtPORNO.setVisible(true);
					txtPORDT.setVisible(true);
					txtDORNO.setVisible(false);
					txtDORDT.setVisible(false);
					lblDORDT.setText("P.O. Date");
					lblPRDCD.setText("Material");					
					txtBRNGR.setEnabled(false);
					txtZONCD.setEnabled(false);
					txtADLOT.setVisible(false);
					txtGRNNO.setVisible(true);
					lblOAREF.setVisible(true);
					txtOAREF.setVisible(true);
					lblADDLT.setText("GRIN No.");
				}								
				cmbCMPCT.removeAllItems();				
				String L_strTEMP = "";
				for (int i=0; i<arrCMPTP.length;i++)
				{				
					if(arrCMPTP[i] != null)
					{
						L_strTEMP = arrCMPTP[i];					
						if(L_strTEMP.charAt(0) == strCMPTP.charAt(1))
						{	
							cmbCMPCT.addItem(L_strTEMP+" "+hstCMPCT.get(L_strTEMP).toString());
						}	
					}
				}
			}
			else if(M_objSOURC == txtZONCD)
			{
				if(txtZONCD.getText().trim().length() == 2)	
				{
					M_strSQLQRY =" SELECT CMT_CODDS from CO_CDTRN where"
					+" CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MR00ZON' AND isnull(CMT_STSFL,'')<>'X'"
					+" AND CMT_CODCD = '"+txtZONCD.getText().trim()+"'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())						
							txtZONCD.setText(txtZONCD.getText().trim()+" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						M_rstRSSET.close();
					}
				}
			}
			else if(M_objSOURC == txtPRTCD)
			{
				txtPRTNM.setText("");
				txtADDR1.setText("");
				txtADDR2.setText("");				
				txtCONPR.setText("");
				txtCELNO.setText("");
				txtFAXNO.setText("");
				txtEMLID.setText("");
				txtPHNNO.setText("");
								
				if(txtPRTCD.getText().trim().length() == 5)
				{
					txtPRTCD.setText(txtPRTCD.getText().trim().toUpperCase());
					M_strSQLQRY =" SELECT distinct PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_PINCD,PT_CTYNM,PT_CONNM,PT_MOBNO,PT_FAXNO,PT_EMLRF,PT_TEL01 from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
					if(strCMPTP.equals("03"))
						M_strSQLQRY += " AND PT_PRTTP ='S'";
					else
						M_strSQLQRY += " AND PT_PRTTP ='C'";															
					M_strSQLQRY += " AND PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
							txtADDR1.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),"")+" "+nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),""));
							txtADDR2.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),"")+" "+nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),"")+" "+nvlSTRVL(M_rstRSSET.getString("PT_CTYNM"),"")+" "+nvlSTRVL(M_rstRSSET.getString("PT_PINCD"),""));
							txtCONPR.setText(nvlSTRVL(M_rstRSSET.getString("PT_CONNM"),""));
							txtCELNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_MOBNO"),""));
							txtFAXNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_FAXNO"),""));
							txtEMLID.setText(nvlSTRVL(M_rstRSSET.getString("PT_EMLRF"),""));
							txtPHNNO.setText(nvlSTRVL(M_rstRSSET.getString("PT_TEL01"),""));
						}
						M_rstRSSET.close();
					}
				}
			}			
			else if(M_objSOURC == txtSTRTP)
			{
				if(txtSTRTP.getText().trim().length() >=2 )
				{
					if(hstSTRTP.containsKey(txtSTRTP.getText().trim().substring(0,2)))
						txtSTRTP.setText(txtSTRTP.getText().trim()+" "+hstSTRTP.get(txtSTRTP.getText().trim().substring(0,2)));
					else
						setMSG("Invalid Subsystem Code Plz Press F1 to select from List..",'E');					
				}
			}
			else if(M_objSOURC == txtREGNO)
			{
				if(cmbCMPTP.getSelectedItem().toString().equals("Select"))
				{
					setMSG("Please Select Complaint Type..",'E');
					cmbCMPTP.requestFocus();
					return;
				}
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					getDATA();				
			}			
			else if(M_objSOURC == txtINVNO)
			{
				java.util.Date L_tmpDATE;
				M_strSQLQRY =" SELECT IVT_PRDCD,IVT_INVDT,IVT_INVQT,IVT_INDNO,IVT_DORDT,IVT_DORNO,IVT_TRPCD,PR_PRDDS from MR_IVTRN,CO_PRMST"//,CO_PTMST" //PT_PRTNM,
					+" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_STSFL,'')<>'X'";
				if(txtPRTCD.getText().trim().length() == 5)
					M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtPRTCD.getText().trim()+"'";
				if(txtTPRCD.getText().trim().length() == 5)
					M_strSQLQRY +=" AND IVT_TRPCD ='"+ txtTPRCD.getText().trim()+"'";
				if(txtPRDCD.getText().trim().length() == 10)
					M_strSQLQRY +=" AND IVT_PRDCD ='"+ txtPRDCD.getText().trim()+"'";
				if(txtINVNO.getText().length() >0)
					M_strSQLQRY += " AND IVT_INVNO = '"+txtINVNO.getText().trim()+"'";
				M_strSQLQRY += " AND  PR_PRDCD = IVT_PRDCD"
					+" AND  isnull(PR_STSFL,'')<>'X'";				
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						L_tmpDATE = M_rstRSSET.getDate("IVT_INVDT");
						if(L_tmpDATE != null)
							txtINVDT.setText(M_fmtLCDAT.format(L_tmpDATE));
						txtINVQT.setText(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""));
						txtINDNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""));
						L_tmpDATE = M_rstRSSET.getDate("IVT_DORDT");
						if(L_tmpDATE != null)
							txtDORDT.setText(M_fmtLCDAT.format(L_tmpDATE));
						txtDORNO.setText(nvlSTRVL(M_rstRSSET.getString("IVT_DORNO"),""));
						txtTPRCD.setText(nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),""));
					}
					M_rstRSSET.close();
				}
				M_strSQLQRY =" SELECT PT_PRTNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
				M_strSQLQRY += " AND PT_PRTTP ='T'";
				M_strSQLQRY += " AND PT_PRTCD = '"+txtTPRCD.getText().trim().toUpperCase()+"'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET != null)
				{
					if(L_rstRSSET.next())
						txtTPRNM.setText(nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""));
					L_rstRSSET.close();
				}
			}
			else if(M_objSOURC == btnPRNT)
			{
				if(txtREGNO.getText().trim().length() == 0)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to generate the Report..",'E');
					return;
				}
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"A");
				
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM);
			}
			else if(M_objSOURC == cmbYESNO)
			{				
				if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
					strYESNO = "Yes";
				else 
					strYESNO = "No";
			}
			else if(M_objSOURC == chkPARTB)
			{
				if(chkPARTB.isSelected())
					strPRTB = "Yes";
				else
					strPRTB = "No";
			}
			else if(M_objSOURC == cmbCMPCT)
			{
				strCMPCT = cmbCMPCT.getSelectedItem().toString().substring(0,3);				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action performed");
		}
	}	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == cmbCMPTP)
			setMSG("Please Select Complain Type..",'N');
		else if(M_objSOURC == txtREGNO)
			setMSG("Please Enter HO Registration number..",'N');
		else if(M_objSOURC == txtBRNGR)
			setMSG("Please Enter Branch Reistration number..",'N');
		else if(M_objSOURC == txtREGDT)
			setMSG("Please Enter Registration Date..",'N');
		else if(M_objSOURC == txtPRTCD)
			setMSG("Please Enter Party Code..",'N');
				else if(M_objSOURC == txtCONPR)
			setMSG("Please Enter the Contact Person name..",'N');
		else if(M_objSOURC == txtEMLID)
			setMSG("Please Enter the Contact Email Address..",'N');
		else if(M_objSOURC == txtZONCD)
			setMSG("Please Enter Zone Code..",'N');
		else if(M_objSOURC == txtADDR1)
			setMSG("Please Enter the first part of the Address..",'N');
		else if(M_objSOURC == txtADDR2)
			setMSG("Please Enter the second part of the Address..",'N');		
		else if(M_objSOURC == txtPHNNO)
			setMSG("Please Enter the Phone Number ..",'N');
		else if(M_objSOURC == txtCELNO)
			setMSG("Please Enter the contact Cell Number ..",'N');
		else if(M_objSOURC == txtFAXNO)
			setMSG("Please Enter Fax Number..",'N');
		else if(M_objSOURC == txtINVNO)
			setMSG("Please Enter Invoice Number Or press F1 to select From List..",'N');
		else if(M_objSOURC == txtINVDT)
			setMSG("Please Enter Invoice Date..",'N');
		else if(M_objSOURC == txtDORNO)
			setMSG("Please Enter D.O. Number..",'N');
		else if(M_objSOURC == txtDORDT)
			setMSG("Please Enter D.O. Date..",'N');
		else if(M_objSOURC == txtTPRCD)
			setMSG("Please Enter Transporter code or press F1 to select From List",'N');
		else if(M_objSOURC == txtTPRNM)
			setMSG("Please Enter Transporter Name..",'N');
		else if(M_objSOURC == txtLOTNO)
			setMSG("Please Enter Lot Number..",'N');
		else if(M_objSOURC == txtPRDCD)
		{
			if(strCMPTP.equals("03"))
				setMSG("Please Enter Material Code Or Press F1 to select from List..",'N');
			else 
				setMSG("Please Enter Product Code Or Press F1 to select from List..",'N');
		}
		else if(M_objSOURC == txtPRDDS)
		{
			if(strCMPTP.equals("03"))
				setMSG("Please Enter Material Description Or Press F1 to select from List..",'N');
			else 
				setMSG("Please Enter Product Description Or Press F1 to select from List..",'N');
		}
		else if(M_objSOURC == txtINVQT)
			setMSG("Please Enter Invoice Quantity..",'N');
		else if(M_objSOURC == txtSMPQT)    
			setMSG("Please Enter Sample Quantity ..",'N');
		else if(M_objSOURC == txtCMPQT)
			setMSG("Please Enter Complaint Quantity..",'N');
		else if(M_objSOURC == txtADLOT)
			setMSG("Please Enter Additonal Lot Number separated by Comma ..",'N');
		else if(M_objSOURC == txtINDNO)
			setMSG("Please Enter Indent Number ..",'N');
		else if(M_objSOURC == txtATACH)
			setMSG("Please Enter Remark Or any Attachament..",'N');		
		else if(M_objSOURC == txaCOMCS)
			setMSG("Please Enter the comments (HOD CSS)..",'N');
		else if(M_objSOURC == txaCOMED)
			setMSG("Please Enter the Comments (ED)..",'N');
		else if(M_objSOURC == txaIMDAC)
			setMSG("Please Enter the Comments fro immadate Acton..",'N');
		else if(M_objSOURC == txaCMDTL)  
			setMSG("Please Enter the datails of the Complaint..",'N');
		else if(M_objSOURC == btnPRNT)
			setMSG("Please Click the Button print to generate the Report ..",'N');		
		else if(M_objSOURC == chkPARTB)
			setMSG("Please Check (click) if Part B Requried ..",'N');
		else if(M_objSOURC == chkSLRTN)
			setMSG("Please Check (click) if Sales Returned ..",'N');
		else if(M_objSOURC == txtSTRTP)
			setMSG("Please enter Store Type Or Press F1 To select from List ..",'N');
	}
	
	public void setTAREA(JTextArea LP_TXA)
	{
		if(LP_TXA.getCaretPosition() % 60==0 )
		{
			int s = LP_TXA.getCaretPosition();
			LP_TXA.setText(LP_TXA.getText().substring(0,LP_TXA.getCaretPosition())+"\n"+LP_TXA.getText().substring(LP_TXA.getCaretPosition(),LP_TXA.getText().length()));
			LP_TXA.setCaretPosition(s+1);
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);	
		if((L_KE.getKeyCode() == L_KE.VK_LEFT) || (L_KE.getKeyCode() == L_KE.VK_RIGHT))
			return;
		
		if(M_objSOURC == txaCMDTL || M_objSOURC == txaIMDAC || M_objSOURC == txaCOMCS || M_objSOURC == txaCOMED)
		{
			setTAREA((JTextArea) M_objSOURC);
		}
		/*if(M_objSOURC == txaCMDTL)
		{
			//if(txaCMDTL.getCaretPosition()>=txaCMDTL.getText().length() && txaCMDTL.getText().length() > 0 && (txaCMDTL.getText().length()% 60==0))
			//	txaCMDTL.setText(txaCMDTL.getText()+"\n");
			
			if(txaCMDTL.getCaretPosition() % 60==0 )
			{
				int s = txaCMDTL.getCaretPosition();
				txaCMDTL.setText(txaCMDTL.getText().substring(0,txaCMDTL.getCaretPosition())+"\n"+txaCMDTL.getText().substring(txaCMDTL.getCaretPosition(),txaCMDTL.getText().length()));
				txaCMDTL.setCaretPosition(s+1);
			}
		}
		if(M_objSOURC == txaIMDAC)
		{
			if(txaIMDAC.getText().length() > 0 && (txaIMDAC.getText().length()% 60==0))
				txaIMDAC.setText(txaIMDAC.getText()+"\n");
		}
		if(M_objSOURC == txaCOMCS)
		{
			if(txaCOMCS.getText().length() > 0 && (txaCOMCS.getText().length()% 60==0))
				txaCOMCS.setText(txaCOMCS.getText()+"\n");
		}
		if(M_objSOURC == txaCOMED)
		{
			if(txaCOMED.getText().length() > 0 && (txaCOMED.getText().length()% 60==0))
				txaCOMED.setText(txaCOMED.getText()+"\n");
		}*/
		if((L_KE.getKeyCode() == L_KE.VK_F1))
		{
			try
			{
				if(M_objSOURC == txtREGNO)
				{
					if(cmbCMPTP.getSelectedItem().toString().equals("Select"))
					{
						setMSG("Please Select Complaint Type..",'E');
						cl_dat.M_flgHELPFL_pbst = false;
						cmbCMPTP.requestFocus();
						return;
					}
					M_strHLPFLD = "txtREGNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_ZONCD,CMT_CODDS,CM_PRTNM,CM_LOTNO from CO_CMMST,CO_CDTRN where"
					+" CMT_CGMTP = 'SYS' AND CMT_CGSTP ='MR00ZON' AND CMT_CODCD = CM_ZONCD AND CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CM_STSFL,'')<>'X'"
					+" AND CM_CMPTP = '"+ strCMPTP+"' AND isnull(CM_STSFL,'')<>'X'";
					if(txtREGNO.getText().length() >0)
						M_strSQLQRY += " AND CM_REGNO like '"+txtREGNO.getText().trim()+"%'";
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
					  if(strEDCOM.equals("No"))
							M_strSQLQRY += " AND CM_STSFL = '1'";
					  else if(strEDCOM.equals("Yes"))
							M_strSQLQRY += " AND CM_STSFL <> '1'";
						  
					}
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						M_strSQLQRY += " AND CM_STSFL = '1'";
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
						M_strSQLQRY += " AND CM_STSFL = '4' AND CM_FINFL = 'Y'";
					M_strSQLQRY +=" Order By CM_REGNO ";
					
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Reg. No.","Regional Reg.No.","Reg.Date","Zone Code","Desc.","Party Name","Lot No."},7,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtZONCD)
				{
					M_strHLPFLD = "txtZONCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
					+" CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MR00ZON' AND isnull(CMT_STSFL,'')<>'X'";	
					if(txtZONCD.getText().length() >= 2)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtZONCD.getText().trim().substring(0,2)+"%'";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Zone Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtTPRCD)
				{
					M_strHLPFLD = "txtTPRCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT PT_PRTCD,PT_PRTNM from CO_PTMST where"
					+" PT_PRTTP ='T' AND isnull(PT_STSFL,'')<>'X'";	
					if(txtTPRCD.getText().length() > 0)
						M_strSQLQRY += " AND PT_PRTCD like '"+txtTPRCD.getText().trim()+"%'";
					M_strSQLQRY += " order by PT_PRTNM";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtPRDCD)
				{
					M_strHLPFLD = "txtPRDCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					if(!strCMPTP.equals("03"))
					{
						if(txtINVNO.getText().trim().length()!= 8)
						{
							setMSG("Please Enter invoice Number.. ",'E');
							txtINVNO.requestFocus();
							return;
						}
						M_strSQLQRY =" SELECT PR_PRDCD,PR_PRDDS from CO_PRMST,MR_IVTRN where"
						+" isnull(PR_STSFL,'')<>'X'"
						+" AND PR_PRDCD = IVT_PRDCD"
						+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO = '"+ txtINVNO.getText().trim() +"'"
						+" AND IVT_BYRCD = '"+ txtPRTCD.getText().trim() +"'";
						if(txtPRDCD.getText().length() > 0)
							M_strSQLQRY += " AND PR_PRDCD like '"+txtPRDCD.getText().trim()+"%'";
						M_strSQLQRY += " order by PR_PRDCD";
					}
					else // Supplier Complaint
					{
						M_strSQLQRY =" SELECT distinct CT_MATCD,CT_MATDS from CO_CTMST,MM_GRMST where"
							+" isnull(CT_STSFL,'')<>'X' AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'')<>'X'"
							+" AND CT_MATCD = GR_MATCD ";
						if(txtPORNO.getText().trim().length() == 8)
							M_strSQLQRY += " AND GR_PORNO = '"+txtPORNO.getText().trim()+"'";
						if(txtPRDCD.getText().length() > 0)
							M_strSQLQRY += " AND CT_MATCD like '"+txtPRDCD.getText().trim()+"%'";
						M_strSQLQRY += " order by CT_MATCD";
					}
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtPRTCD)
				{
					M_strHLPFLD = "txtPRTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT distinct PT_PRTCD,PT_PRTNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
					if(strCMPTP.equals("03"))
						M_strSQLQRY += " AND PT_PRTTP ='S'";
					else
						M_strSQLQRY += " AND PT_PRTTP ='C'";
					if(txtPRTCD.getText().length() > 0)
						M_strSQLQRY += " AND PT_PRTCD like '"+txtPRTCD.getText().trim().toUpperCase()+"%'";
					M_strSQLQRY += " order by PT_PRTCD ";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSTRTP)
				{
					M_strHLPFLD = "txtSTRTP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT CMT_CODCD,CMT_SHRDS from CO_CDTRN where"
					+" CMT_CGMTP ='SYS' AND CMT_CGSTP = 'MMXXSST' AND isnull(CMT_STSFL,'')<>'X'";	
					if(txtSTRTP.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtSTRTP.getText().trim().substring(0,2)+"%'";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Store Type","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}								
				else if(M_objSOURC == txtINVNO)
				{
					txtLOTNO.setText("");
					txtADLOT.setText("");
					txtSMPQT.setText("0.000");
					txtCMPQT.setText("0.000");
					
					if(txtPRTCD.getText().trim().length() != 5)
					{
						setMSG("Please Enter Party Code before Invoice Number",'N');
						//jtbREGDT.setSelectedIndex(0);
						//txtPRTCD.requestFocus();
						return;
					}
					M_strHLPFLD = "txtINVNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					M_strSQLQRY =" SELECT IVT_INVNO,IVT_BYRCD,PT_PRTNM,IVT_INVDT,IVT_INVQT,IVT_PRDCD from MR_IVTRN,CO_PTMST"
					+" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_STSFL,'')<>'X'";
					if(txtPRTCD.getText().trim().length() == 5)
						M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtPRTCD.getText().trim()+"'";
					if(txtTPRCD.getText().trim().length() == 5)
						M_strSQLQRY +=" AND IVT_TRPCD ='"+ txtTPRCD.getText().trim()+"'";
					if(txtPRDCD.getText().trim().length() == 10)
						M_strSQLQRY +=" AND IVT_PRDCD ='"+ txtPRDCD.getText().trim()+"'";
					if(txtINVNO.getText().length() >0)
						M_strSQLQRY += " AND IVT_INVNO like '"+txtINVNO.getText().trim()+"%'";
					M_strSQLQRY += " AND PT_PRTTP = 'C' AND PT_PRTCD = IVT_BYRCD"
					+" AND isnull(PT_STSFL,'')<>'X' order by IVT_INVDT desc";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Invoice No","Buyer Code","Buyer Name","Inv. Date","Inv Qty.","Grade"},6,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else
					if(M_objSOURC == txtLOTNO)
				{
					if(txtPRTCD.getText().trim().length() != 5)
					{
						setMSG("Please Enter valid Party Code before Lot Number..",'E');
						//jtbREGDT.setSelectedIndex(0);
						//txtPRTCD.requestFocus();
						return;
					}
					if(txtINVNO.getText().trim().length() != 8)
					{
						setMSG("Please Enter valid Invoice Number before Lot Number..",'E');
						//jtbREGDT.setSelectedIndex(1);
						//txtINVNO.requestFocus();
						return;
					}	
					M_strHLPFLD = "txtLOTNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					M_strSQLQRY = "Select distinct IST_LOTNO,IVT_PRDCD,IVT_INVNO from MR_IVTRN,FG_ISTRN where"
					+" IVT_CMPCD=IST_CMPCD AND IVT_MKTTP = IST_MKTTP AND IVT_PRDCD = IST_PRDCD"
					+" AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO"
					+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_PRDCD = '"+ txtPRDCD.getText().trim() +"'"
					+" AND IVT_INVNO = '"+ txtINVNO.getText().trim() +"'"					
					+" AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IST_STSFL,'')<> 'X'"
					+" AND isnull(IVT_STSFL,'')<> 'X'";
					if(txtLOTNO.getText().trim().length()>0)
						M_strSQLQRY +=" AND IST_LOTNO like '"+ txtLOTNO.getText().trim() +"%'";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot Number","Product Code","Invoice No"},3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtPORNO)
				{
					M_strHLPFLD = "txtPORNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT PO_PORNO,PO_PORDT,GR_MATCD,CT_MATDS,GR_GRNNO,GR_BATNO from MM_POMST,MM_GRMST,CO_CTMST where"					
						+" PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'')<>'X' AND  GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'')<>'X' AND GR_CMPCD=PO_CMPCD and GR_STRTP = PO_STRTP"
						+" AND GR_PORNO = PO_PORNO AND GR_MATCD = PO_MATCD AND GR_VENCD ='"+ txtPRTCD.getText().trim() +"'"
						+" AND isnull(CT_STSFL,'')<>'X' AND GR_MATCD = CT_MATCD";
					if(txtPORNO.getText().trim().length()>0)
						M_strSQLQRY += " AND PO_PORNO = '"+ txtPORNO.getText().trim()+"'";	
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"PO Number","PO Date","Material Code","Description","GRIN No","Batch No"},6,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtGRNNO)
				{
					if(txtPORNO.getText().trim().length() != 8)
					{
						setMSG("Please Enter PO Number, before GRIN No..",'E');
						return;
					}
					if(txtPRDCD.getText().trim().length() != 8)
					{
						setMSG("Please Enter PO Number, before GRIN No..",'E');
						return;
					}
					M_strHLPFLD = "txtGRNNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					
					M_strSQLQRY =" SELECT GR_GRNNO,GR_MATCD,GR_BATNO from MM_POMST,MM_GRMST,CO_CTMST where"
						+" PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'')<>'X' AND  GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'')<>'X' AND GR_CMPCD=PO_CMPCD AND GR_STRTP = PO_STRTP"
						+" AND GR_PORNO = PO_PORNO AND GR_MATCD = PO_MATCD AND GR_VENCD ='"+ txtPRTCD.getText().trim() +"'"
						+" AND isnull(CT_STSFL,'')<>'X' AND GR_MATCD = CT_MATCD"
						+" AND PO_PORNO = '"+ txtPORNO.getText().trim()+"'";	
					if(txtPORNO.getText().trim().length()>0)
						M_strSQLQRY += " AND PO_PORNO = '"+ txtPORNO.getText().trim()+"'";	
					cl_hlp(M_strSQLQRY,1,1,new String[]{"PO Number","PO Date","Material Code","Description","GRIN No"},5,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
		}
		else if((L_KE.getKeyCode() == L_KE.VK_ENTER))
		{
			try
			{
				if(M_objSOURC == txtREGNO)
				{
					if(cmbCMPTP.getSelectedItem().toString().equals("Select"))
					{
						setMSG("Please Select Complaint Type..",'E');
						cmbCMPTP.requestFocus();
						return;
					}
					if(strCMPTP.equals("03"))
					{
						txtREGDT.requestFocus();
						txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						txtBRNGR.requestFocus();					
				}
				else if(M_objSOURC == txtBRNGR)
				{
					if(cmbCMPTP.getSelectedItem().toString().equals("Select"))
					{
						setMSG("Please Select Complaint Type..",'E');
						cmbCMPTP.requestFocus();
						return;
					}
					txtBRNGR.setText(txtBRNGR.getText().trim().toUpperCase());
					txtREGDT.requestFocus();
					txtREGDT.setText(cl_dat.M_strLOGDT_pbst);
				}	
				else if(M_objSOURC == txtREGDT)
				{					
					txtZONCD.requestFocus();
				}
				else if(M_objSOURC == txtZONCD)
				{					
					cmbCMPCT.requestFocus();					
				}
				else if(M_objSOURC == cmbCMPCT)
				{					
					cmbYESNO.requestFocus();					
				}
				else if(M_objSOURC == cmbYESNO)
				{					
					txtPRTCD.requestFocus();					
				}
				else if(M_objSOURC == txtSTRTP)
					txtINVNO.requestFocus();
				else if(M_objSOURC == txtPRTCD)
				{
					if(txtPRTCD.getText().trim().length() == 5)				
					txtCONPR.requestFocus();
				}
				else if(M_objSOURC == txtCONPR)
					txtPHNNO.requestFocus();
				else if(M_objSOURC == txtPHNNO)
					txtEMLID.requestFocus();
				else if(M_objSOURC == txtEMLID)
					txtCELNO.requestFocus();
				else if(M_objSOURC == txtCELNO)
					txtFAXNO.requestFocus();
				else if(M_objSOURC == txtFAXNO)
					jtbREGDT.requestFocus();				
				else if(M_objSOURC == txtSTRTP)
				{
					if(txtSTRTP.getText().trim().length() >= 2)
						txtINVQT.requestFocus();
				}
				else if(M_objSOURC == txtINVNO)
				{
					txtCMPQT.requestFocus();
					txtCMPQT.setText("0.000");
				}
				else if(M_objSOURC == txtCMPQT)
				{
					txtSMPQT.setText("0.000");
					txtSMPQT.requestFocus();
				}
				else if(M_objSOURC == txtSMPQT)
					txtPRDCD.requestFocus();				
				else if(M_objSOURC == txtPRDCD)
					txtLOTNO.requestFocus();				
				else if(M_objSOURC == txtLOTNO)
					txtADLOT.requestFocus();
				else if(M_objSOURC == txtGRNNO)
					txtOAREF.requestFocus();
				else if(M_objSOURC == txtOAREF)
					txtINDNO.requestFocus();				
				else if(M_objSOURC == txtADLOT)
					txtATACH.requestFocus();
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"L_KE.VK_ENTER");
			}
		}
	}
	/**
	 * Super class Method overrrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{
		try
		{
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtZONCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtZONCD.setText(cl_dat.M_strHLPSTR_pbst+" "+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}		
			else if(M_strHLPFLD.equals("txtPRTCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
			else if(M_strHLPFLD.equals("txtSTRTP"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtSTRTP.setText(cl_dat.M_strHLPSTR_pbst);//+" "+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
			else if(M_strHLPFLD.equals("txtTPRCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtTPRNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}		
			else if(M_strHLPFLD.equals("txtPRDCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			}
			else if(M_strHLPFLD.equals("txtREGNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;			
				txtREGNO.setText(cl_dat.M_strHLPSTR_pbst);
				txtBRNGR.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
				txtREGDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));
				txtZONCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3))+" "+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)));				
			}
			else if(M_strHLPFLD.equals("txtINVNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtINVNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtLOTNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtPORNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtPORNO.setText(cl_dat.M_strHLPSTR_pbst);			
				txtPORDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
				txtPRDCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));				
				txtMATDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)));
				txtGRNNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)));
				txtBATNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),5)));			
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Method to validate the data before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		try
		{
			if(vldREMDS(txaCMDTL.getText().trim()) == false)
				return false;
			else if(vldREMDS(txaIMDAC.getText().trim()) == false)
				return false;
			else if(vldREMDS(txaCOMCS.getText().trim()) == false)
				return false;
			else if(vldREMDS(txaCOMED.getText().trim()) == false)
				return false;
			
			if(cmbCMPTP.getSelectedItem().toString().equals("Select"))
			{
				setMSG("Please Select Complaint Type..",'E');
				cmbCMPTP.requestFocus();
				return false;
			}
			if(txtREGDT.getText().trim().length() == 0)
			{
				setMSG("Please enter Date of registration..",'E');
				txtREGDT.requestFocus();
				return false;
			}
			else if(txtBRNGR.getText().trim().length() == 0)
			{
				setMSG("Please enter Branch Reg number..",'E');
				txtBRNGR.requestFocus();
				return false;
			}
			else if(txtPRTCD.getText().trim().length() == 0)
			{
				setMSG("Please enter Party Code..",'E');
				txtPRTCD.requestFocus();
				return false;
			}
			else if(txtPRTNM.getText().trim().length() == 0)
			{
				setMSG("Please enter Party Name..",'E');
				txtPRTNM.requestFocus();
				return false;
			}
			else if((txtPHNNO.getText().trim().length() == 0) && (txtCELNO.getText().trim().length() == 0))
			{
				setMSG("Please enter Phone No. Or Cell No...",'E');
				txtPHNNO.requestFocus();
				return false;
			}
			
			if(!strCMPTP.equals("03")) // only if commercial or technical Complaint
			{
				if(strCMPTP.equals("02"))// technicals
				{		
					if(txtLOTNO.getText().trim().length() == 0)
					{
						setMSG("Please enter Lot Number..",'E');
						txtLOTNO.requestFocus();
						return false;
					}
					else if(txtPRDCD.getText().trim().length() == 0)
					{
						setMSG("Please enter Product Code..",'E');
						txtPRDCD.requestFocus();
						return false;
					}
				}
				if(txtINVNO.getText().trim().length() == 0)
				{
					setMSG("Please enter Invoice Number..",'E');
					txtINVNO.requestFocus();
					return false;
				}
			}
			if(txtCMPQT.getText().trim().length() == 0)
			{
				txtCMPQT.setText("0.000");
			}
			else if(txtSMPQT.getText().trim().length() == 0)
			{
				txtSMPQT.setText("0.000");
			}
			else if(txtINVDT.getText().trim().length() == 0)
			{
				setMSG("Please enter Invoice Date ..",'E');
				txtINVDT.requestFocus();
				return false;
			}
			else if(txtDORNO.getText().trim().length() == 0)
			{
				setMSG("Please enter D.O. Number ..",'E');
				txtDORNO.requestFocus();
				return false;
			}
			if(txaCMDTL.getText().trim().length() == 0)
			{
				setMSG("Please enter the Complaint Details ..",'E');
				txaCMDTL.requestFocus();
				return false;
			}
			if(txaIMDAC.getText().trim().length() == 0)
			{
				setMSG("Please enter the Details of Immadiate action ..",'E');
				txaIMDAC.requestFocus();
				return false;
			}
			if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
			{
				if(txaCOMCS.getText().equals(""))
				{
					strYESNO = "No";
					cmbYESNO.setSelectedItem("No");
					txaCOMCS.requestFocus();
					setMSG("You cannot Complete Registration before the Comments of HOD(CSS)..",'E');
					return false;
				}
				 /*M_strSQLQRY = "Select CMT_REMDS from CO_CMTRN where"
					 +" CMT_SBSCD ='"+ M_strSBSCD+"'"
					 +" AND CMT_CMPTP ='"+strCMPTP+"'"
					 +" AND CMT_REGNO ='"+ txtREGNO.getText().trim()+"'"
					 +" AND CMT_PRTNO = 'A'"
					 +" AND CMT_REMTP ='COMCS'";
				 M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				 if(M_rstRSSET != null)
				 {
					if(M_rstRSSET.next())						
						strYESNO = "Yes";
					else
					{
						strYESNO = "No";
						cmbYESNO.setSelectedItem("No");
						setMSG("You cannot Complete Registration before the Comments of HOD(CSS)..",'E');
					}
				}*/
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	/**
	 * Super class method overrided here to inhance its functionality, to perform 
	 * Database operations.
	 */
	void exeSAVE()
	{					
		try
		{
			if(!vldDATA())			
				return ;			
			else
				setMSG("",'E');			
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				if(cmbYESNO.getSelectedItem().toString().equals("Yes"))//List of dept has to perform investigation to update
				{	
					/*M_strSQLQRY =" SELECT CMT_CCSVL from CO_CDTRN where"
					+" CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXCMT' AND CMT_CODCD = '"+ strCMPTP +"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							strINVBY = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
						M_rstRSSET.close();			
					}*/
					
					M_strSQLQRY =" SELECT SUBSTRING(CMT_CODCD,4,3) DPTCD from CO_CDTRN where"
					+" CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXIDP' AND SUBSTRING(CMT_CODCD,1,2) = '"+ strCMPTP +"' order by CMT_CHP01";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						strINVBY="";
						while(M_rstRSSET.next())
						{	
							if(strINVBY.length()==0)
								strINVBY +=nvlSTRVL(M_rstRSSET.getString("DPTCD"),"");
							else
								strINVBY +="_"+nvlSTRVL(M_rstRSSET.getString("DPTCD"),"");
						}
						M_rstRSSET.close();			
					}
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					//System.out.println("strINVBY>>"+strINVBY);
				}
			}					
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				String L_strTEMP1 = "";			
				// Number generation blocked on 23/03/2006 
				/*String L_strCODCD = cl_dat.M_strFNNYR_pbst.substring(3,4)+"00";
				M_strSQLQRY = "Select CMT_CCSVL from CO_CDTRN where CMT_CGMTP ='DOC' AND CMT_CGSTP ='COXXCRN' AND isnull(CMT_STSFL,'')<>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET!= null)
				{				
					if(M_rstRSSET.next())				
						L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),""); 				
					M_rstRSSET.close();
				}						
				int L_intTSTNO = Integer.valueOf(L_strTEMP1).intValue()+1;
				strREGNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + strCMPTP.trim()
					+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
					+ String.valueOf(L_intTSTNO);				
				txtREGNO.setText(strREGNO); // Autogenerated Reg Number.
				*/				
				M_strSQLQRY = "Insert into CO_CMMST(CM_CMPCD,CM_SBSCD,CM_CMPTP,CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTTP,"
					+"CM_PRTCD,CM_PRTNM,CM_CONPR,CM_CELNO,CM_FAXNO,CM_PHNNO,CM_EMLID,CM_ZONCD,CM_TPRCD,"
					+"CM_INDNO,CM_MRSBS,CM_ORDNO,CM_ORDDT,CM_INVNO,CM_INVDT,CM_INVQT,CM_MMSBS,CM_GRNNO,"
					+"CM_PRDCD,CM_LOTNO,CM_ADLOT,CM_CMPQT,CM_SMPQT,CM_FINFL,CM_SLRFL,CM_CMPCT,CM_ATACH,";				
				if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
					M_strSQLQRY +="CM_INVBY,";				
				M_strSQLQRY +="CM_TRNFL,CM_STSFL,CM_LUSBY,CM_LUPDT)"
					+" values ('"+cl_dat.M_strCMPCD_pbst+"','"+ M_strSBSCD +"',"
					+"'"+ strCMPTP +"',"					
					//+"'"+ strREGNO +"',"
                    +"'"+ txtREGNO.getText().trim() +"',"
					+"'"+ txtBRNGR.getText().trim().toUpperCase()+"',"
					+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtREGDT.getText().trim()))+"',";
				if(strCMPTP.equals("03"))
					M_strSQLQRY += "'S',";
				else
					M_strSQLQRY += "'C',";
				M_strSQLQRY += "'"+ txtPRTCD.getText().trim() +"',"
					+ "'"+ txtPRTNM.getText().trim() +"',"
					+ "'"+ txtCONPR.getText().trim() +"',"
					+ "'"+ txtCELNO.getText().trim() +"',"
					+ "'"+ txtFAXNO.getText().trim() +"',"
					+ "'"+ txtPHNNO.getText().trim() +"',"
					+ "'"+ txtEMLID.getText().trim() +"',"
					+ "'"+ txtZONCD.getText().trim().substring(0,2) +"',"
					+ "'"+ txtTPRCD.getText().trim() +"',"
					+ "'"+ txtINDNO.getText().trim() +"',"
					//+"'"+ txtMRSBS.getText().trim() +"',"
					+ "'ABCDEF',"
					+ "'"+ txtDORNO.getText().trim() +"',"
					+ "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDORDT.getText().trim())) +"',"
					+ "'"+ txtINVNO.getText().trim() +"',"
					+ "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINVDT.getText().trim())) +"',"
					+ txtINVQT.getText().trim() +","
						// +"'"+ txtMMSBS.getText().trim() +"',"
					+ "'PQRSTU',"
					+ "'"+ txtGRNNO.getText().trim() +"',"
					+ "'"+ txtPRDCD.getText().trim() +"',"
					+ "'"+ txtLOTNO.getText().trim() +"',"
					+ "'"+ txtADLOT.getText().trim() +"',"
					+ txtCMPQT.getText().trim() +","
					+ txtSMPQT.getText().trim() +",";
				if(chkPARTB.isSelected())
					M_strSQLQRY +="'Y',";
				else
					M_strSQLQRY +="'N',";
				if(chkSLRTN.isSelected())
					M_strSQLQRY +="'Y',";
				else
					M_strSQLQRY +="'N',";
				M_strSQLQRY += "'"+ strCMPCT +"',"//cmbCMPCT.getSelectedItem().toString().substring(0,2) +"',"
					+"'"+ txtATACH.getText().trim() +"',";
				if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
				{
					M_strSQLQRY += "'"+ strINVBY +"',"
					+ getUSGDTL("CM",'I',"2") + ")";
				}
				else 
					M_strSQLQRY += getUSGDTL("CM",'I',"1") + ")";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				// Number generation blocked on 23/03/2006
				/*if(cl_dat.M_flgLCUPD_pbst == true)
				{
					M_strSQLQRY = "Update CO_CDTRN set CMT_CCSVL ='"+ strREGNO.substring(3) +"' where CMT_CGMTP ='DOC' AND CMT_CGSTP ='COXXCRN'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}*/
			}
			else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) && (strEDCOM.equals("No")))
			{
				M_strSQLQRY = "Update CO_CMMST set CM_SBSCD ='"+ M_strSBSCD +"',"					
					+ "CM_REPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtREGDT.getText().trim())) +"',"
					//+ "CM_PRTTP = '"+ +"',"
					+ "CM_PRTCD = '"+ txtPRTCD.getText().trim() +"',"
					+ "CM_PRTNM = '"+ txtPRTNM.getText().trim() +"',"
					+ "CM_CONPR = '"+ txtCONPR.getText().trim() +"',"
					+ "CM_CELNO = '"+ txtCELNO.getText().trim() +"',"
					+ "CM_FAXNO = '"+ txtFAXNO.getText().trim() +"',"
					+ "CM_PHNNO = '"+ txtPHNNO.getText().trim() +"',"
					+ "CM_EMLID = '"+ txtEMLID.getText().trim() +"',"
					+ "CM_ZONCD = '"+ txtZONCD.getText().trim().substring(0,2) +"',"
					+ "CM_TPRCD = '"+ txtTPRCD.getText().trim() +"',"
					+ "CM_INDNO = '"+ txtINDNO.getText().trim() +"',"
					//+ "CM_MRSBS = '"+ +"',"
					+ "CM_ORDNO = '"+ txtDORNO.getText().trim() +"',"
					+ "CM_ORDDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDORDT.getText().trim())) +"',"
					+ "CM_INVNO = '"+ txtINVNO.getText().trim() +"',"
					+ "CM_INVDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINVDT.getText().trim()))+"',"
					+ "CM_INVQT = "+ txtINVQT.getText().trim() +","
					//+ "CM_MMSBS = '"+ +"',"
					+ "CM_GRNNO = '"+ txtGRNNO.getText().trim() +"',"
					+ "CM_PRDCD = '"+ txtPRDCD.getText().trim() +"',"
					+ "CM_LOTNO = '"+ txtLOTNO.getText().trim() +"',"
					+ "CM_ADLOT = '"+ txtADLOT.getText().trim() +"',"
					+ "CM_CMPQT = "+ txtCMPQT.getText().trim() +","
					+ "CM_SMPQT = "+ txtSMPQT.getText().trim() +","
					+ "CM_CMPCT = '"+ strCMPCT +"',";//cmbCMPCT.getSelectedItem().toString().substring(0,2) +"',";				
					if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
					{
						M_strSQLQRY += " CM_STSFL = '2',"
						+"CM_INVBY = '"+ strINVBY +"',";
					}
					M_strSQLQRY += "CM_ATACH = '"+ txtATACH.getText().trim() +"',";
					if(chkPARTB.isSelected())
						M_strSQLQRY +=" CM_FINFL = 'Y',";
					else
						M_strSQLQRY +=" CM_FINFL = 'N',";
					if(chkSLRTN.isSelected())
						M_strSQLQRY +=" CM_SLRFL = 'Y',";
					else
						M_strSQLQRY +=" CM_SLRFL = 'N',";
					M_strSQLQRY += " CM_TRNFL = '',"					
					+ "CM_LUSBY = '"+ cl_dat.M_strUSRCD_pbst +"',"
					+ "CM_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'"
					+ " where CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CM_CMPTP = '"+ strCMPTP +"'"
					+ " AND CM_REGNO = '"+ txtREGNO.getText().trim() +"'";				
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					strREGNO = txtREGNO.getText().trim();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				M_strSQLQRY = "Update CO_CMMST set CM_TRNFL = '',"
				+ "CM_STSFL = 'X',"
				+ "CM_LUSBY = '"+ cl_dat.M_strUSRCD_pbst +"',"
				+ "CM_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'"
				+ " where CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CM_CMPTP = '"+ strCMPTP +"'"
				+ " AND CM_REGNO = '"+ txtREGNO.getText().trim() +"'"
				+ " AND CM_BRNRG = '"+ txtBRNGR.getText().trim().toUpperCase() +"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
			}
			if(strEDCOM.equals("No"))
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				{			
					if(cl_dat.M_flgLCUPD_pbst == true)
					{
						modREMDS("CMDTL",txaCMDTL.getText().trim(),strRMRK1,txtUSR1.getText().trim());									
						modREMDS("IMDAC",txaIMDAC.getText().trim(),strRMRK2,txtUSR2.getText().trim());					
						modREMDS("COMCS",txaCOMCS.getText().trim(),strRMRK3,txtUSR3.getText().trim());
//						modREMDS("COMED",txaCOMED.getText().trim(),strRMRK4,txtUSR4.getText().trim());
					}
				}
			}
			else if((strEDCOM.equals("Yes")) && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{//ED Comments through modification
				modREMDS("COMED",txaCOMED.getText().trim(),strRMRK4,txtUSR4.getText().trim());
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				cmbCMPTP.setEnabled(true);
				txtREGNO.setEnabled(true);
			}
			String L_strBRGNO = 	txtBRNGR.getText().trim();
			if(cl_dat.exeDBCMT("exeSAVE"))
			{	
				clrCOMP();
				lblSTAT.setText("");			
				setENBL(true);
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Saved Successfully..",'N');
			}
			else
			{
				strYESNO = "No";
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'E');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Error in saving data..",'E');
			}
			//send Mails to currosponding persons
			//no mail after mod to enter ED Comments
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) && (strEDCOM.equals("No"))))
			{				
				if(strYESNO.equals("Yes"))
				{
					cl_eml ocl_eml = new cl_eml();					
					co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
					obj.getDATA(M_strSBSCD,strCMPTP,strREGNO,"A");
				
					String L_strINVBY = "";
					StringTokenizer L_stkTEMP = new StringTokenizer(strINVBY,"_");
					while(L_stkTEMP.hasMoreTokens())
					{
						if(L_strINVBY.length() != 0)
							L_strINVBY +=",";
						L_strINVBY += "'"+ L_stkTEMP.nextToken()+"'";						
					}										
					// Default mails
					
					M_strSQLQRY =" SELECT CMT_CODDS from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"	
						+" AND CMT_CGMTP ='EML' AND CMT_CGSTP ='COXXCAS'"
						+" AND CMT_CODCD like '"+ strCMPTP+ "A%'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
						{	ocl_eml.sendfile(M_rstRSSET.getString("CMT_CODDS").trim()+"@spl.co.in",strFILNM,"Customer Complaint Registration"," ");
						}
						M_rstRSSET.close();
					}	
				    //COMMENTED on 26/03/2007,as per CSS
					///if(strPRTB.equals("Yes"))
					///{
						String L_strPREFIX = " ";
						if(L_strPREFIX.length() >0)
							L_strPREFIX = L_strBRGNO.substring(0,1);
						/*M_strSQLQRY =" SELECT CMT_CHP01 from CO_CDTRN where"
							+" isnull(CMT_STSFL,'')<>'X'"	
							+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXDPT'"
							+" AND CMT_CODCD in ("+ L_strINVBY +") UNION";	*/
						M_strSQLQRY ="SELECT CMT_CODDS CMT_CHP01 from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"	
						+" AND CMT_CGMTP ='EML' AND CMT_CGSTP ='COXXCAS'"
						+" AND CMT_CODCD like '"+ strCMPTP+ L_strPREFIX+"A%'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							while(M_rstRSSET.next())
								ocl_eml.sendfile(M_rstRSSET.getString("CMT_CHP01").trim()+"@spl.co.in",strFILNM,"Customer Complaint Registration"," ");
							M_rstRSSET.close();
						}
					///} END if(strPRTB.equals("Yes"))

				}
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
	}
	/**
	 * Method to fetch data releated to the give registration Number.
	 */
	void getDATA()
	{
		try
		{
			String L_strTEMP = "";
			String L_strCMPCT = "";			
			if(txtREGNO.getText().trim().length() == 0)
			{
				setMSG("Please Enter Regestration Number..",'E');
				return;
			}
			M_strSQLQRY = "Select CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM,"
			+"CM_CONPR,CM_CELNO,CM_FAXNO,CM_PHNNO,CM_EMLID,CM_ZONCD,CM_INDNO,CM_MRSBS,"
			+"CM_ORDNO,CM_ORDDT,CM_INVNO,CM_INVDT,CM_INVQT,CM_MMSBS,CM_GRNNO,CM_TPRCD,CM_PRDCD,"
			+"CM_LOTNO,CM_ADLOT,CM_CMPQT,CM_SMPQT,CM_FINFL,CM_SLRFL,CM_STSFL,CM_ATACH,CM_CMPCT from CO_CMMST" //CM_INVBY,CM_FRWTO
			+" Where CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CM_STSFL,'') <>'X' AND CM_REGNO ='"+txtREGNO.getText().trim()+"'"
			+" AND CM_CMPTP = '"+ strCMPTP +"'";
			M_rstRSSET= cl_dat.exeSQLQRY3(M_strSQLQRY);
			{
				if(M_rstRSSET!= null)
				{
					if(M_rstRSSET.next())
					{
						L_strCMPCT = nvlSTRVL(M_rstRSSET.getString("CM_CMPCT"),"");
						if(hstCMPCT.containsKey(L_strCMPCT))
							cmbCMPCT.setSelectedItem(L_strCMPCT+" "+hstCMPCT.get(L_strCMPCT).toString());
						strCMPTP = strCMPTP;
						strREGNO = nvlSTRVL(M_rstRSSET.getString("CM_REGNO"),"");
						txtREGNO.setText(strREGNO);
						strBRNGR = nvlSTRVL(M_rstRSSET.getString("CM_BRNRG"),"");
						txtBRNGR.setText(strBRNGR);
						java.sql.Date L_tmpDATE = M_rstRSSET.getDate("CM_REPDT");
						if(L_tmpDATE != null)								
							txtREGDT.setText(M_fmtLCDAT.format(L_tmpDATE));
						else
							txtREGDT.setText("");
						//txtREGNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTTP"),""));
						txtPRTCD.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTCD"),""));							
						M_strSQLQRY =" SELECT PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
						if(strCMPTP.equals("03"))
							M_strSQLQRY += " AND PT_PRTTP ='S'";
						else
							M_strSQLQRY += " AND PT_PRTTP ='C'";
						M_strSQLQRY += " AND PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'";
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{									
								txtADDR1.setText(nvlSTRVL(L_rstRSSET.getString("PT_ADR01"),"")+" "+nvlSTRVL(L_rstRSSET.getString("PT_ADR02"),""));
								txtADDR2.setText(nvlSTRVL(L_rstRSSET.getString("PT_ADR03"),"")+" "+nvlSTRVL(L_rstRSSET.getString("PT_ADR04"),"")+" "+nvlSTRVL(L_rstRSSET.getString("PT_CTYNM"),""));
							}
							L_rstRSSET.close();
						}
						txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),""));
						txtCONPR.setText(nvlSTRVL(M_rstRSSET.getString("CM_CONPR"),""));
						txtCELNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_CELNO"),""));
						txtFAXNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_FAXNO"),""));
						txtPHNNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_PHNNO"),""));
						txtEMLID.setText(nvlSTRVL(M_rstRSSET.getString("CM_EMLID"),""));
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CM_ZONCD"),"");
						if(hstZONDS.containsKey(L_strTEMP))
							L_strTEMP = L_strTEMP +" "+hstZONDS.get(L_strTEMP).toString();
						txtZONCD.setText(L_strTEMP);
						txtINDNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_INDNO"),""));
						//txtREGNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_MRSBS"),""));
						txtDORNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_ORDNO"),""));
						L_tmpDATE = M_rstRSSET.getDate("CM_ORDDT");
						if(L_tmpDATE != null)								
							txtDORDT.setText(M_fmtLCDAT.format(L_tmpDATE));
						else
							txtDORDT.setText("");
						txtINVNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_INVNO"),""));
						L_tmpDATE = M_rstRSSET.getDate("CM_INVDT");
						if(L_tmpDATE != null)								
							txtINVDT.setText(M_fmtLCDAT.format(L_tmpDATE));
						else
							txtINVDT.setText("");
						txtINVQT.setText(nvlSTRVL(M_rstRSSET.getString("CM_INVQT"),""));
						//txtREGNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_MMSBS"),""));
						txtGRNNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_GRNNO"),""));
						txtTPRCD.setText(nvlSTRVL(M_rstRSSET.getString("CM_TPRCD"),""));
						M_strSQLQRY =" SELECT PT_PRTNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
						M_strSQLQRY += " AND PT_PRTTP ='T'";
						M_strSQLQRY += " AND PT_PRTCD = '"+txtTPRCD.getText().trim().toUpperCase()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
								txtTPRNM.setText(nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""));
							L_rstRSSET.close();
						}
						txtPRDCD.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRDCD"),""));
						M_strSQLQRY =" SELECT PR_PRDDS from CO_PRMST where isnull(PR_STSFL,'')<>'X'";
						M_strSQLQRY += " AND PR_PRDCD = '"+txtPRDCD.getText().trim().toUpperCase()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
								txtPRDDS.setText(nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),""));
							L_rstRSSET.close();
						}
						txtLOTNO.setText(nvlSTRVL(M_rstRSSET.getString("CM_LOTNO"),""));
						txtADLOT.setText(nvlSTRVL(M_rstRSSET.getString("CM_ADLOT"),""));
						txtCMPQT.setText(nvlSTRVL(M_rstRSSET.getString("CM_CMPQT"),""));
						txtSMPQT.setText(nvlSTRVL(M_rstRSSET.getString("CM_SMPQT"),""));
						if(nvlSTRVL(M_rstRSSET.getString("CM_FINFL"),"").equals("Y"))
							chkPARTB.setSelected(true);
						else
							chkPARTB.setSelected(false);
						if(nvlSTRVL(M_rstRSSET.getString("CM_SLRFL"),"").equals("Y"))
							chkSLRTN.setSelected(true);
						else 
							chkSLRTN.setSelected(false);
						strSTSFL = nvlSTRVL(M_rstRSSET.getString("CM_STSFL"),"");						
						if(Integer.valueOf(strSTSFL).intValue() >= 2)
						{
							cmbYESNO.setSelectedItem("Yes");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) &&(strEDCOM.equals("No")))
								setMSG("Modification is not allowed..",'E');
							txtINVNO.setEnabled(false);
							txtPRDCD.setEnabled(false);
							txtLOTNO.setEnabled(false);							
						}
						else 
						{
							cmbYESNO.setSelectedItem("No");
							txtINVNO.setEnabled(true);
							txtPRDCD.setEnabled(true);
							txtLOTNO.setEnabled(true);
						}
						if(hstSTSDS.containsKey(strSTSFL))
							lblSTAT.setText(hstSTSDS.get(strSTSFL).toString());
					//	String L_strFRWTO = nvlSTRVL(M_rstRSSET.getString("CM_FRWTO"),"");						
						txtATACH.setText(nvlSTRVL(M_rstRSSET.getString("CM_ATACH"),""));
					}
					M_rstRSSET.close();
				}											
			}
			M_strSQLQRY = "select CMT_REMBY,CMT_REMDT,CMT_REMTP,CMT_REMDS from CO_CMTRN where CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'"
			+" AND CMT_CMPTP ='"+ strCMPTP +"'"
			+" AND CMT_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CMT_STSFL,'')<>'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strREMTP ="";
				strRMRK1 = "";
				strRMRK2 = "";
				strRMRK3 = "";
				strRMRK4 = "";
				java.util.Date L_tmpDATE;
				while(M_rstRSSET.next())
				{					
					L_strREMTP = nvlSTRVL(M_rstRSSET.getString("CMT_REMTP"),"");					
					if(L_strREMTP.equals("CMDTL"))
					{
						strRMRK1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
						txaCMDTL.setText(strRMRK1);
						txtUSR1.setText(nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),""));						
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)								
							txtDAT1.setText(M_fmtLCDAT.format(L_tmpDATE));
						else
							txtDAT1.setText("");
					}
					else if(L_strREMTP.equals("IMDAC"))
					{
						strRMRK2 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
						txaIMDAC.setText(strRMRK2);
						txtUSR2.setText(nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),""));
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)								
							txtDAT2.setText(M_fmtLCDAT.format(L_tmpDATE));
						else
							txtDAT2.setText("");
					}
					else if(L_strREMTP.equals("COMCS"))
					{
						strRMRK3 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
						txaCOMCS.setText(strRMRK3);
						txtUSR3.setText(nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),""));
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)								
							txtDAT3.setText(M_fmtLCDAT.format(L_tmpDATE));
						else
							txtDAT3.setText("");
					}
					else if(L_strREMTP.equals("COMED"))
					{
						strRMRK4 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
						txaCOMED.setText(strRMRK4);
						txtUSR4.setText(nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),""));
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)								
							txtDAT4.setText(M_fmtLCDAT.format(L_tmpDATE));
						else
							txtDAT4.setText("");
					}
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		
	}
	/**
	 * Method for addition & Modification of the Remarks Entered.
	 * @param P_strREMTP String argument to pass the Remark Type.
	 * @param P_strREMDS String argument to pass the new Remark Description.
	 * @param P_strOREMDS String argument to pass the Old Remark Decription
	 * @param P_strUSRCD String argument to pass User Code who has entered the corresponding Ramraks.
	 */
	private void modREMDS(String P_strREMTP,String P_strREMDS,String P_strOREMDS,String P_strUSRCD)
	{
		try
		{				
			if(!P_strOREMDS.equals(P_strREMDS))
			{
				if(P_strREMDS.length() == 0)
				{					
					M_strSQLQRY = "update CO_CMTRN set CMT_REMDS = '-',"
					+ "	CMT_STSFL ='0',"
					+ "	CMT_TRNFL = '',"
					+ "	CMT_LUSBY ='"+ P_strUSRCD+"',"
					+ "	CMT_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+ " where CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_REMTP = '"+ P_strREMTP +"' AND CMT_CMPTP = '"+strCMPTP +"'"
					+ " AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"				
					+ " AND CMT_DPTCD = 'CSS'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
				else if(P_strOREMDS.length() == 0) 
				{
					M_strSQLQRY = "Insert into CO_CMTRN(CMT_CMPCD,CMT_SBSCD,CMT_CMPTP,CMT_REGNO,CMT_PRTNO,CMT_REMTP,CMT_DPTCD,"
					+ "CMT_REMBY,CMT_REMDT,CMT_REMDS,CMT_CPAFL,CMT_TRNFL,CMT_STSFL,CMT_LUSBY,CMT_LUPDT)"
					+ " Values ( '"+cl_dat.M_strCMPCD_pbst+"','"+ M_strSBSCD +"',"
					+"'"+ strCMPTP +"',"
					+"'"+ txtREGNO.getText().trim() +"',"
					+"'A',"
					+"'"+ P_strREMTP +"',"
					+"'CSS',"
					+"'"+ P_strUSRCD +"',"
					+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()))+"',"
					+"'"+ P_strREMDS +"',"
					+"'',";
					M_strSQLQRY += getUSGDTL("CMT",'I',"0") + ")";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
				else
				{
					M_strSQLQRY = "update CO_CMTRN set CMT_REMDS ='"+ P_strREMDS +"',"
					+ "	CMT_REMBY ='"+ P_strUSRCD + "',"
					+ "	CMT_STSFL ='0',"
					+ "	CMT_TRNFL = '',"
					+ "	CMT_LUSBY ='"+ P_strUSRCD+"',"
					+ "	CMT_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+ " where CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_REMTP = '"+ P_strREMTP +"' AND CMT_CMPTP = '"+strCMPTP +"'"
					+ " AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"
					+ " AND CMT_DPTCD = 'CSS'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"modREMDS");
		}
	}
	/**
	 * Method to check the Special Charectores in the data entered
	 * @param P_strREMDS String argument to pass Remark description
	 */
	private boolean vldREMDS(String P_strREMDS)
	{		
		if(P_strREMDS.length() == 0)
			return true;
		if((P_strREMDS.indexOf("'") >=0)||(P_strREMDS.indexOf("\"") >=0))
		{
			setMSG("Special Characters like \', \", are not allowed in the Data entered",'E');
			return false;
		}
		return true;
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{		
				ResultSet L_rstRSSET;				
				/*if((input == txtREGNO) && (txtREGNO.getText().trim().length() == 8))
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{					
						M_strSQLQRY =" SELECT CM_REGNO from CO_CMMST,CO_CDTRN where"
						+" CMT_CGMTP = 'SYS' AND CMT_CGSTP ='MRXXZON' AND CMT_CODCD = CM_ZONCD AND isnull(CM_STSFL,'')<>'X'"
						+" AND CM_SBSCD ='"+ M_strSBSCD +"' AND CM_CMPTP = '"+ strCMPTP +"' AND isnull(CM_STSFL,'')<>'X'";
						if(txtREGNO.getText().length() >0)
							M_strSQLQRY += " AND CM_REGNO like '"+txtREGNO.getText().trim()+"%'";
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
							M_strSQLQRY += " AND CM_STSFL ='1'";
						M_strSQLQRY += " AND CM_REGNO = '"+ txtREGNO.getText().trim() +"'";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if (L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								strRNOVD = "Yes";
								L_rstRSSET.close();
								return true;
							}	
							else
							{
								strRNOVD = "No";
								L_rstRSSET.close();
								setMSG( "Invalid Registration Number, Press F1 to select from List ..",'E');
								return false;
							}
						}
					}
				}
				else*/ if((input == txtBRNGR) && (txtBRNGR.getText().trim().length() == 8))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strSQLQRY =" SELECT CM_BRNRG from CO_CMMST where CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CM_BRNRG ='"+ txtBRNGR.getText().trim().toUpperCase()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if (L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								L_rstRSSET.close();
								setMSG("Branch Reg. number already exist, duplicate is not allowed ..",'E');
								return false;							
							}	
							else
							{
								L_rstRSSET.close();
								return true;
							}
						}
					}
				}
				else if((input == txtPRTCD) && (txtPRTCD.getText().trim().length() == 5))
				{					
					M_strSQLQRY =" SELECT distinct PT_PRTCD,PT_PRTNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
					if(strCMPTP.equals("03"))
						M_strSQLQRY += " AND PT_PRTTP ='S'";
					else
						M_strSQLQRY += " AND PT_PRTTP ='C'";
					M_strSQLQRY += " AND PT_PRTCD = '"+txtPRTCD.getText().trim().toUpperCase()+"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if (L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}	
						else
						{
							L_rstRSSET.close();
							setMSG( "Invalid Party Code, Press F1 to select from List ..",'E');
							return false;
						}
					}											
				}
				else if((input == txtPRDCD) && (txtPRDCD.getText().trim().length() == 10))
				{	
					if(strCMPTP.equals("03"))
					{
						M_strSQLQRY =" SELECT distinct PR_PRDCD from CO_PRMST,MM_GRMST where isnull(PR_STSFL,'')<>'X'"
						+ " AND PR_PRDCD = '"+txtPRDCD.getText().trim().toUpperCase()+"' AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
						if(txtPORNO.getText().trim().length() == 8)
							M_strSQLQRY += " AND GR_PORNO = '"+ txtPORNO.getText().trim() +"'";
						M_strSQLQRY += " AND GR_MATCD = PR_PRDCD AND isnull(GR_STSFL,'')<>'X'";						
					}
					else
					{
						//M_strSQLQRY =" SELECT distinct PR_PRDCD from CO_PRMST where isnull(PR_STSFL,'')<>'X'";
						//M_strSQLQRY += " AND PR_PRDCD = '"+txtPRDCD.getText().trim().toUpperCase()+"'";
						M_strSQLQRY =" SELECT PR_PRDCD,PR_PRDDS from CO_PRMST,MR_IVTRN where"
						+" isnull(PR_STSFL,'')<>'X'"
						+" AND PR_PRDCD = IVT_PRDCD"
						+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_INVNO = '"+ txtINVNO.getText().trim() +"'"
						+" AND IVT_BYRCD = '"+ txtPRTCD.getText().trim() +"'"
						+" AND PR_PRDCD = '"+ txtPRDCD.getText().trim() +"'";
					}
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if (L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}	
						else
						{
							L_rstRSSET.close();
							setMSG( "Invalid Product Code, Press F1 to select from List ..",'E');
							return false;
						}
					}
				}
				if(!strCMPTP.equals("03"))
				{
					if((input == txtINVNO) && (txtINVNO.getText().trim().length() == 8))
					{
						if(txtPRTCD.getText().trim().length() != 5)
						{
							setMSG("Please Enter Party Code before Invoice Number",'E');
							return false;
						}
						M_strSQLQRY =" SELECT IVT_INVNO,IVT_BYRCD,PT_PRTNM,IVT_INVDT,IVT_INVQT from MR_IVTRN,CO_PTMST"
						+" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IVT_STSFL,'')<>'X'";
						if(txtPRTCD.getText().trim().length() == 5)
							M_strSQLQRY +=" AND IVT_BYRCD ='"+ txtPRTCD.getText().trim()+"'";
						if(txtTPRCD.getText().trim().length() == 5)
							M_strSQLQRY +=" AND IVT_TRPCD ='"+ txtTPRCD.getText().trim()+"'";
						if(txtPRDCD.getText().trim().length() == 10)
							M_strSQLQRY +=" AND IVT_PRDCD ='"+ txtPRDCD.getText().trim()+"'";
						if(txtINVNO.getText().length() >0)
							M_strSQLQRY += " AND IVT_INVNO like '"+txtINVNO.getText().trim()+"%'";
						M_strSQLQRY += " AND PT_PRTTP = 'C' AND PT_PRTCD = IVT_BYRCD"
						+" AND isnull(PT_STSFL,'')<>'X'";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if (L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								L_rstRSSET.close();
								return true;
							}	
							else
							{
								L_rstRSSET.close();
								setMSG( "Invalid Invoice Number, Press F1 to select from List ..",'E');
								return false;
							}
						}	
					}
					else if((input == txtLOTNO) && (txtLOTNO.getText().trim().length() == 8))
					{
						if(txtINVNO.getText().trim().length() != 8)
						{
							setMSG("Please Enter Invoice Number before Lot Number",'E');
							return false;
						}
						if(txtPRTCD.getText().trim().length() != 5)
						{
							setMSG("Please Enter Party Code before Lot Number",'E');
							return false;
						}
						M_strSQLQRY = "Select IST_LOTNO from MR_IVTRN,FG_ISTRN where"
						+" IVT_CMPCD=IST_CMPCD and IVT_MKTTP = IST_MKTTP AND IVT_PRDCD = IST_PRDCD"
						+" AND IVT_PKGTP = IST_PKGTP AND IVT_LADNO = IST_ISSNO"
						+" AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_PRDCD = '"+ txtPRDCD.getText().trim() +"'"
						+" AND IVT_INVNO = '"+ txtINVNO.getText().trim() +"'"
						+" AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_LOTNO = '"+ txtLOTNO.getText().trim() +"'"
						+" AND isnull(IST_STSFL,'')<> 'X'"
						+" AND isnull(IVT_STSFL,'')<> 'X'";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if (L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								L_rstRSSET.close();
								return true;
							}	
							else
							{
								L_rstRSSET.close();
								setMSG( "Invalid Lot Number, Press F1 to select from List ..",'E');
								return false;
							}
						}
					}
				}
				else if(strCMPTP.equals("03"))
				{
					if((input == txtPORNO) && (txtPORNO.getText().trim().length() == 8))
					{
						M_strSQLQRY =" SELECT distinct PO_PORCD from MM_POMST where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'')<>'X'"
						+ " AND PR_PRDCD = '"+txtPRDCD.getText().trim().toUpperCase()+"'";
						if(txtPRTCD.getText().trim().length() == 5)
							M_strSQLQRY += " AND PO_VENCD = '"+ txtPRTCD.getText().trim()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if (L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								L_rstRSSET.close();
								return true;
							}	
							else
							{
								L_rstRSSET.close();
								setMSG( "Invalid PO Number, Press F1 to select from List ..",'E');
								return false;
							}
						}
					}
					if((input == txtGRNNO) && (txtGRNNO.getText().trim().length() == 8))
					{
						if(txtPORNO.getText().trim().length() != 8)
						{
							setMSG("Please Enter PO Number before GRIN No Number",'E');
							return false;
						}
						if(txtPRDCD.getText().trim().length() != 10)
						{
							setMSG("Please Enter Product Code before Lot Number",'E');
							return false;
						}
						M_strSQLQRY =" SELECT distinct GR_GRNNO from MM_POMST,MM_GRMST where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'')<>'X' AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PO_STSFL,'')<>'X'"
						+ " AND GR_MATCD = '"+ txtPRDCD.getText().trim().toUpperCase()+"'"
						+ " AND PO_PORNO = '"+ txtPORNO.getText().trim()+"'"
						+ " AND PO_GRNNO = '"+ txtGRNNO.getText().trim()+"'"
						+ " AND PO_CMPCD=GR_CMPCD and PO_PORNO = GR_GRNNO AND PO_STRTP = GR_STRTP AND PO_MATCD = GR_MATCD";
						L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if (L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{
								L_rstRSSET.close();
								return true;
							}
							else
							{
								L_rstRSSET.close();
								setMSG( "Invalid GRIN Number, Press F1 to select from List ..",'E');
								return false;
							}
						}
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"InputVerify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}	
	}
}