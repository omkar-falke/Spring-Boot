/*
System Name   : Material Management System
Program Name  : Customer Complaint Analysis Query
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
import javax.swing.JEditorPane;import javax.swing.JCheckBox;import javax.swing.JComboBox;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.awt.Color;import java.sql.ResultSet; import java.util.Hashtable;

class co_qrcas extends cl_pbase
{
    private JTabbedPane jtbMAIN = new JTabbedPane();
	private JTabbedPane jtbREGDT = new JTabbedPane();
	private JTabbedPane jtbREMDS = new JTabbedPane();
	private JPanel pnlPARTA;
	private JPanel pnlPARTB;
	private JPanel pnlPARTC;
	private JPanel pnlPARTD;
	private JComboBox cmbCMPTP;
    private JTextField txtREGNO;
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;    	    
	
	//Part A    
	private JPanel pnlCUST;
	private JPanel pnlOTHR;
	private JPanel pnlCPDTL;
	private JPanel pnlIMDAC;
	private JPanel pnlCOMCS;
	private JPanel pnlCOMED;	
	private JTextField txtBRNGR;
	private JTextField txtREGDT;	
	private JTextField txtPRTNM1;	
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
	    				
	private JTextArea txaCMDTL;
	private JTextArea txaIMDAC;
	private JTextArea txaCOMCS;
	private JTextArea txaCOMED;
	private JTextArea txaPARTB;
	//private JTextArea txaINVAU;
	private JTextArea txaPARTC;
	private JTextArea txaPARTD;
	private JTextArea txaCPAAU;
    	 
	private JCheckBox chkPARTB;
	private JCheckBox chkSLRTN;
	private JLabel lblSTAT;
	private JLabel lblDORNO;
	private JLabel lblDORDT;
	private JLabel lblPRDCD;	
	private JLabel lblADDLT;
	private JLabel lblOAREF;	
	
	private String strCMPTP;
	private String strRMRK1 = "";
	private String strRMRK2 = "";
	private String strRMRK3 = "";
	private String strRMRK4 = "";
	private String strBRNGR;
	private String strREGNO;
	private Hashtable<String,String> hstZONDS;
	private Hashtable<String,String> hstSTSDS;
	private Hashtable<String,String> hstDPTDS;	
	
    co_qrcas()
    {
        super(1);
		try
		{
	        setMatrix(20,8);
	        pnlPARTA = new JPanel();pnlPARTA.setLayout(null);
	        pnlPARTB = new JPanel();pnlPARTB.setLayout(null);
	        pnlPARTC = new JPanel();pnlPARTC.setLayout(null);
	        pnlPARTD = new JPanel();pnlPARTD.setLayout(null);
	        
			add(new JLabel("Complaint Type"),1,1,1,2,this,'L');
			add(cmbCMPTP = new JComboBox(),1,2,1,1.4,this,'L');
	        add(new JLabel("Reg. No"),1,3,1,.6,this,'R');
			add(txtREGNO = new JTextField(),1,4,1,1,this,'L');		
			add(new JLabel("Party Name"),1,5,1,.8,this,'R');
			add(txtPRTNM1 = new JTextField(),1,6,1,3,this,'L');  
			
			///Part A Tabbed Pane.
			txaCMDTL = new JTextArea();
			txaCOMCS = new JTextArea();
			txaCOMED = new JTextArea();
			txaIMDAC = new JTextArea();			
			pnlCUST = new JPanel();pnlCUST.setLayout(null);
			pnlOTHR = new JPanel();pnlOTHR.setLayout(null);		
			pnlCPDTL = new JPanel();pnlCPDTL.setLayout(null);
			pnlIMDAC = new JPanel();pnlIMDAC.setLayout(null);
			pnlCOMCS = new JPanel();pnlCOMCS.setLayout(null);
			pnlCOMED = new JPanel();pnlCOMED.setLayout(null);
						
			add(new JLabel("Branch Reg."),1,1,1,.9,pnlPARTA,'R');
			add(txtBRNGR = new TxtLimit(8),1,2,1,1,pnlPARTA,'L');			  	
			add(new JLabel("Reported On"),1,3,1,.9,pnlPARTA,'R');
			add(txtREGDT = new JTextField(),1,4,1,1,pnlPARTA,'L');
			add(new JLabel("Zone Code"),1,5,1,.8,pnlPARTA,'R');
			add(txtZONCD = new JTextField(),1,6,1,1,pnlPARTA,'L');
		
			add(new JLabel("Status :  "),2,5,1,.8,pnlPARTA,'R');
			add(lblSTAT = new JLabel("Status"),2,6,1,2.7,pnlPARTA,'L');
			lblSTAT.setForeground(Color.blue);
			
			add(new JLabel("Party Code"),1,1,1,1,pnlCUST,'L');
			add(txtPRTCD = new JTextField(),1,2,1,1,pnlCUST,'L');
			add(new JLabel("Party Name"),1,3,1,.9,pnlCUST,'R');  
			add(txtPRTNM = new JTextField(),1,4,1,4,pnlCUST,'L');  
					   
			add(new JLabel("Address"),2,1,1,1,pnlCUST,'L');
			add(txtADDR1 = new JTextField(),2,2,1,6,pnlCUST,'L');  
			add(txtADDR2 = new JTextField(),3,2,1,6,pnlCUST,'L');			

	 		add(new JLabel("Contact Person"),4,1,1,1,pnlCUST,'L');
			add(txtCONPR = new JTextField(),4,2,1,2.3,pnlCUST,'L');
			add(new JLabel("Phone No"),4,4,1,.7,pnlCUST,'R');
			add(txtPHNNO = new JTextField(),4,5,1,3,pnlCUST,'L');
				
			add(new JLabel("E-Mail"),5,1,1,1,pnlCUST,'L');
			add(txtEMLID = new JTextField(),5,2,1,2.3,pnlCUST,'L');			
			add(new JLabel("Cell No"),5,4,1,.7,pnlCUST,'R');
			add(txtCELNO = new JTextField(),5,5,1,1,pnlCUST,'L');
			add(new JLabel("Fax No."),5,6,1,.6,pnlCUST,'R');
			add(txtFAXNO = new JTextField(),5,7,1,1,pnlCUST,'L');
				
			add(pnlCUST,3,1,4,8,this,'L');
			
			add(new JLabel("Sub System"),1,1,1,1,pnlOTHR,'L');
			add(txtSTRTP = new JTextField(),1,2,1,1,pnlOTHR,'L');  			
			add(new JLabel("Invoice No."),1,3,1,1,pnlOTHR,'R');
			add(txtINVNO = new TxtNumLimit(8),1,4,1,1,pnlOTHR,'L');  
			add(new JLabel("Invoice Date"),1,5,1,1,pnlOTHR,'R');
			add(txtINVDT = new TxtDate(),1,6,1,1,pnlOTHR,'L');  
			add(new JLabel("Invoice Qty"),1,7,1,.9,pnlOTHR,'R');
			add(txtINVQT = new TxtNumLimit(9.3),1,8,1,.8,pnlOTHR,'L');
			    
			add(lblDORNO = new JLabel("D.O. No."),2,1,1,1,pnlOTHR,'L');
			add(txtDORNO = new TxtNumLimit(8),2,2,1,1,pnlOTHR,'L');  
			add(lblDORDT = new JLabel("D.O Date"),2,3,1,1,pnlOTHR,'R');
			add(txtDORDT = new TxtDate(),2,4,1,1,pnlOTHR,'L');  
			add(new JLabel("Complaint Qty"),2,5,1,1,pnlOTHR,'L');
			add(txtCMPQT = new TxtNumLimit(9.3),2,6,1,1,pnlOTHR,'L');  
			add(new JLabel("Sample Qty"),2,7,1,.9,pnlOTHR,'R');
			add(txtSMPQT = new TxtNumLimit(9.3),2,8,1,.8,pnlOTHR,'L');  
			    
			add(lblPRDCD = new JLabel("Product"),3,1,1,1,pnlOTHR,'L');
			add(txtPRDCD = new TxtLimit(10),3,2,1,1,pnlOTHR,'L');  
			add(txtPRDDS = new JTextField(),3,3,1,2,pnlOTHR,'L');  
			add(new JLabel("Transporter"),3,5,1,1,pnlOTHR,'L');
			add(txtTPRCD = new TxtLimit(5),3,6,1,1,pnlOTHR,'L');  
			add(txtTPRNM = new JTextField(),3,7,1,1.8,pnlOTHR,'L');  
			          
			add(new JLabel("Lot No."),4,1,1,1,pnlOTHR,'L');
			add(txtLOTNO = new TxtNumLimit(8),4,2,1,1,pnlOTHR,'L');  
			add(lblADDLT = new JLabel("Additional Lots."),4,3,1,1,pnlOTHR,'L');
			add(txtADLOT = new JTextField(),4,4,1,3,pnlOTHR,'L');  						
			add(txtGRNNO = new TxtNumLimit(8),4,4,1,1,pnlOTHR,'L');
			add(lblOAREF = new JLabel("O.A. Ref."),4,5,1,1,pnlOTHR,'L');
			add(txtOAREF = new JTextField(),4,6,1,1,pnlOTHR,'L'); 
			add(new JLabel("Indent No."),4,7,1,.9,pnlOTHR,'R');
			add(txtINDNO = new TxtNumLimit(8),4,8,1,.8,pnlOTHR,'L');
				
			add(chkSLRTN = new JCheckBox("Sales Return"),5,7,1,1.1,pnlOTHR,'L');  
			add(chkPARTB = new JCheckBox("Part B Req."),5,8,1,1,pnlOTHR,'L');
				
			add(new JLabel("Attachments"),5,1,1,1,pnlOTHR,'L');
			add(txtATACH = new JTextField(),5,2,1,5,pnlOTHR,'L');  
			
			add(pnlOTHR,3,1,4,8,this,'L');
				
			add(new JScrollPane(txaCMDTL,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),1,1,4,7,pnlCPDTL,'L');
			add(new JScrollPane(txaIMDAC,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),1,1,4,7,pnlIMDAC,'L');
			add(new JScrollPane(txaCOMCS,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),1,1,4,7,pnlCOMCS,'L');
			add(new JScrollPane(txaCOMED,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),1,1,4,7,pnlCOMED,'L');
			
			jtbREGDT.addTab("Contact Details",pnlCUST);
			jtbREGDT.addTab("Registration Details ",pnlOTHR);			
			add(jtbREGDT,2,1,7,8,pnlPARTA,'L');
			jtbREMDS.addTab("Details of Complaint",pnlCPDTL);
			jtbREMDS.addTab("Immediate Action",pnlIMDAC);			
			jtbREMDS.addTab("Comments HOD(CSS)",pnlCOMCS);
			jtbREMDS.addTab("Comments (ED)",pnlCOMED);			
			add(jtbREMDS,10,1,6,7.5,pnlPARTA,'L');

			// Part B Tabbed Pane 
			txaPARTB = new JTextArea();		
//			txaINVAU = new JTextArea();		
	        add(new JLabel("Investigation by (Dept. 1/2/3)"),1,1,1,6,pnlPARTB,'L');        
			add(new JScrollPane(txaPARTB,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),2,1,12,7.8,pnlPARTB,'L');

			// Part C Tabbed pane
	        add(new JLabel("Recommendations / Resolution of Complaint"),1,1,1,6,pnlPARTC,'L');
			txaPARTC = new JTextArea();		
			add(new JScrollPane(txaPARTC,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),2,1,12,7.8,pnlPARTC,'L');//HORIZONTAL_SCROLLBAR_ALWAYS
		    
			//Part D Tabbed pane
			txaPARTD = new JTextArea();		
			txaCPAAU = new JTextArea();	
		    add(new JLabel("Corrective / Preventive (Dept. 1/2/3)"),1,1,1,6,pnlPARTD,'L');
			add(new JScrollPane(txaPARTD,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),2,1,10,7.8,pnlPARTD,'L');	    
		    add(new JLabel("Comments of Approving Authority for CAPA"),12,1,1,6,pnlPARTD,'L');
		    add(new JScrollPane(txaCPAAU,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),13,1,3,7.8,pnlPARTD,'L');
		    
		    jtbMAIN.addTab("Complaint Registration(PART A)",pnlPARTA);
	       	jtbMAIN.addTab("Investigations (PART B)",pnlPARTB);			
		    jtbMAIN.addTab("Closure(PART C)",pnlPARTC);
		    jtbMAIN.addTab("CAPA (PART D)",pnlPARTD);
			add(jtbMAIN,2,1,17.1,8,this,'L');
			txtGRNNO.setEnabled(false);
			txtOAREF.setEnabled(false);
		
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
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'STS'"
			+" AND CMT_CGSTP = 'COXXCST' AND isnull(CMT_STSFL,'')<>'X'";
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
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS'"
			+" AND CMT_CGSTP ='COXXDPT' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET= cl_dat.exeSQLQRY1(M_strSQLQRY);
			{
				if(M_rstRSSET != null)
				{
					hstDPTDS = new Hashtable<String,String>();
					while(M_rstRSSET.next())
						hstDPTDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					M_rstRSSET.close();
				}
			}
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
		super.setENBL(L_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() > 0)
		{
			cmbCMPTP.setEnabled(true);
			txtREGNO.setEnabled(true);
		}
		txaCMDTL.setEnabled(false);
		txaIMDAC.setEnabled(false);
		txaCOMCS.setEnabled(false);
		txaCOMED.setEnabled(false);
		
		txaPARTB.setEnabled(false);
//		txaINVAU.setEnabled(false);
		txaPARTC.setEnabled(false);
		txaPARTD.setEnabled(false);
		txaCPAAU.setEnabled(false);
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
					if(cmbCMPTP.getItemCount() == 0)
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXCMT'";
						M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
						{
							if(M_rstRSSET != null)
							{
								while(M_rstRSSET.next())
									cmbCMPTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
								M_rstRSSET.close();
							}
						}
					}
					txtREGNO.requestFocus();
				}
				//else
				setENBL(false);
			}
			else if(M_objSOURC == cmbCMPTP)
			{	
				strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);				
				lblADDLT.setText("Additional Lots");				
				txtADLOT.setVisible(true);
				txtGRNNO.setVisible(false);
				lblOAREF.setVisible(false);
				txtOAREF.setVisible(false);
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
				else if(strCMPTP.equals("03"))
				{
					lblDORNO.setText("P.O. No");
					lblDORDT.setText("P.O. Date");
					lblPRDCD.setText("Material");
					lblADDLT.setText("GRIN No.");
					txtADLOT.setVisible(false);
					txtGRNNO.setVisible(true);
					lblOAREF.setVisible(true);
					txtOAREF.setVisible(true);
				}
			}
			else if(M_objSOURC == txtREGNO)
			{
				if(txtREGNO.getText().trim().length() > 0)
				{
					getDATAA();
					//getDATAB();
					//getDATAC();
					//getREMDS(String P_strPRTNO,String P_strREMTP,JTextArea P_txaREMDS)
					getREMDS("B","PARTB",txaPARTB);
					getREMDS("C","PARTC",txaPARTC);
					getREMDS("D","PARTD",txaPARTD);
					getREMDS("B","AUTRM",txaCPAAU);// CAPA Authorization Remarks
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtREGNO)
			setMSG("Please Enter registration number to generate the Report..",'N');
		else if(M_objSOURC == txtPRTCD)
			setMSG("Please Enter Part Code Or press F1 to select from List..",'N');
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		try
		{
			if((L_KE.getKeyCode() == L_KE.VK_F1))
			{			
				if(M_objSOURC == txtREGNO)
				{
					M_strHLPFLD = "txtREGNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTNM,CM_CMPTP from CO_CMMST where"
					+" isnull(CM_STSFL,'')<>'X'"
					+" AND CM_CMPTP = '"+ cmbCMPTP.getSelectedItem().toString().substring(0,2)+"'";
//					+" AND CM_SBSCD ='"+ M_strSBSCD 
					if(txtREGNO.getText().length() >0)
						M_strSQLQRY += " AND CM_REGNO like '"+txtREGNO.getText().trim()+"%'";					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Reg. No.","Regional Reg.No.","Reg.Date","Party Name","Complaint Type"},5,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if((L_KE.getKeyCode() == L_KE.VK_ENTER))
			{			
				if(M_objSOURC == txtREGNO)
				{
					
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"VK_F1");
		}
	}
	/**
	 * Super class Method overrrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{		
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtREGNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtREGNO.setText(cl_dat.M_strHLPSTR_pbst);			
			txtPRTNM1.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)));
		}
	}
	/**
	 * Method to fetch data releated to the give registration Number.
	 */
	void getDATAA()
	{
		try
		{
			String L_strTEMP = "";			
			M_strSQLQRY = "Select CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM,"
			+"CM_CONPR,CM_CELNO,CM_FAXNO,CM_PHNNO,CM_EMLID,CM_ZONCD,CM_INDNO,CM_MRSBS,"
			+"CM_ORDNO,CM_ORDDT,CM_INVNO,CM_INVDT,CM_INVQT,CM_MMSBS,CM_GRNNO,CM_TPRCD,CM_PRDCD,"
			+"CM_LOTNO,CM_ADLOT,CM_CMPQT,CM_SMPQT,CM_FINFL,CM_SLRFL,CM_STSFL from CO_CMMST"
			+" Where isnull(CM_STSFL,'') <>'X' AND CM_REGNO ='"+txtREGNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			{
				if( M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);
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
						M_strSQLQRY =" SELECT PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04 from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
						if(cmbCMPTP.getSelectedItem().toString().substring(0,2).equals("03"))
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
								txtADDR2.setText(nvlSTRVL(L_rstRSSET.getString("PT_ADR02"),"")+" "+nvlSTRVL(L_rstRSSET.getString("PT_ADR04"),""));
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
						//txtINVDT.setText(nvlSTRVL(M_rstRSSET.getString("CM_INVDT"),""));
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
						L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
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
						if(nvlSTRVL(M_rstRSSET.getString("CM_FINFL"),"").equals("Y"));
							chkPARTB.setSelected(true);
						if(nvlSTRVL(M_rstRSSET.getString("CM_SLRFL"),"").equals("Y"));
							chkSLRTN.setSelected(true);
						String L_strSTATS = nvlSTRVL(M_rstRSSET.getString("CM_STSFL"),"");
						if(hstSTSDS.containsKey(L_strSTATS))
							lblSTAT.setText(hstSTSDS.get(L_strSTATS).toString());
						else
							lblSTAT.setText("Status");
					}
					M_rstRSSET.close();
				}											
			}
			M_strSQLQRY = "select CMT_REMBY,CMT_REMDT,CMT_REMTP,CMT_REMDS from CO_CMTRN where "//CMT_SBSCD = '"+ M_strSBSCD+"' AND"
			+" CMT_CMPTP ='"+ cmbCMPTP.getSelectedItem().toString().substring(0,2) +"'"
			+" AND CMT_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CMT_STSFL,'')<>'X'"
			+" AND CMT_PRTNO = 'A'";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strREMTP ="";
				strRMRK1 = "";
				strRMRK2 = "";
				strRMRK3 = "";
				strRMRK4 = "";
				String L_strDATE = "";
				String L_strUSRCD = "";
				java.util.Date L_tmpDATE;
				while(M_rstRSSET.next())
				{
					L_strDATE = "";
					L_strREMTP = nvlSTRVL(M_rstRSSET.getString("CMT_REMTP"),"");					
					if(L_strREMTP.equals("CMDTL"))
					{
						strRMRK1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");						
						L_strUSRCD = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)
							L_strDATE = M_fmtLCDAT.format(L_tmpDATE);
						txaCMDTL.setText(strRMRK1 +"\nEntered By  :  "+L_strUSRCD +"  On  " +L_strDATE);
					}
					else if(L_strREMTP.equals("IMDAC"))
					{
						strRMRK2 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
						L_strUSRCD =  nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)
							L_strDATE = M_fmtLCDAT.format(L_tmpDATE);
						txaIMDAC.setText(strRMRK2 +"\nEntered By  :  "+L_strUSRCD +"  On  " +L_strDATE);
					}
					else if(L_strREMTP.equals("COMCS"))
					{
						strRMRK3 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");						
						L_strUSRCD = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)
							L_strDATE = M_fmtLCDAT.format(L_tmpDATE);
						txaCOMCS.setText(strRMRK3 +"\nEntered By  :  "+L_strUSRCD +"  On  " +L_strDATE);
					}
					else if(L_strREMTP.equals("COMED"))
					{
						strRMRK4 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");						
						L_strUSRCD = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)								
							L_strDATE = M_fmtLCDAT.format(L_tmpDATE);
						txaCOMED.setText(strRMRK4 +"\nEntered By  :  "+L_strUSRCD +"  On  " +L_strDATE);
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
	void getREMDS(String P_strPRTNO,String P_strREMTP,JTextArea P_txaREMDS)
	{
		try
		{
			P_txaREMDS.setText("");
			System.out.println("USRTP "+cl_dat.M_strUSRTP_pbst+"USRTP");
			if((cl_dat.M_strUSRTP_pbst.equals("CO131")) && (P_strPRTNO.equals("B")))//CO131
			{
				M_strSQLQRY = "select CM_REGNO from CO_CMMST where CM_SBSCD = '"+ M_strSBSCD+"' AND"
				+" CM_CMPTP ='"+ cmbCMPTP.getSelectedItem().toString().substring(0,2) +"'"
				+" AND CM_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CM_STSFL,'')<>'X'"
				+" AND CM_STSFL < '5'";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						setMSG("Investigation Details are not availible as Investigation in Progress..",'E');
						M_rstRSSET.close();
						return ;
					}
					M_rstRSSET.close();
				}
			}			
			M_strSQLQRY = "select CMT_REMBY,CMT_REMDT,CMT_DPTCD,CMT_REMDS from CO_CMTRN where CMT_SBSCD = '"+ M_strSBSCD+"' AND"
			+" CMT_CMPTP ='"+ cmbCMPTP.getSelectedItem().toString().substring(0,2) +"'"
			+" AND CMT_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CMT_STSFL,'')<>'X'"
			+" AND CMT_PRTNO = '"+ P_strPRTNO +"' AND CMT_REMTP = '"+ P_strREMTP +"'";			
			
			
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET != null)
			{				
				String L_strREMDS = "";				
				String L_strDATE =  "";
				String L_strUSRCD = "";
				String L_strDPTCD = "";
				java.util.Date L_tmpDATE;
				while(M_rstRSSET.next())
				{										
					L_strREMDS = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
					L_strUSRCD = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
					L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
					L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
					if(L_tmpDATE != null)
						L_strDATE = M_fmtLCDAT.format(L_tmpDATE);
					if(hstDPTDS.containsKey(L_strDPTCD))
						L_strDPTCD = hstDPTDS.get(L_strDPTCD).toString();
					if(P_strREMTP.equals("AUTAM"))
						P_txaREMDS.append(L_strREMDS +"\nEntered By  :  "+L_strUSRCD+"  during Authorization On  " +L_strDATE +"\n--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
					else
						P_txaREMDS.append(L_strREMDS +"\nEntered By  :  "+L_strUSRCD+"  for Department  "+L_strDPTCD +"  On  " +L_strDATE +"\n--------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATAB");
		}
	}
}
