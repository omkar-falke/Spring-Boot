/*
System Name : Preventive Maintenance.
Program Name : Work Request.
Source Directory : d:\source\splerp3\pm_tewrq        Executable Directory : d:\exec\splerp3\pm_tewrq

List of tables used:
Table Name		Primary key					            Operation done
								             Insert   Update	   Query    Delete	
---------------------------------------------------------------------------------------------
PM_WRMST	    WR_WORNO
                
CO_CDTRN        CMT_CGMTP,CMT_CGSTP,CMT_CODCD  

CO_CTMST 		CT_GRPCD,CT_CODTP,CT_MATCD        	                    	 
                 
-------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on screen:
Field Name		Column Name			    Table name	          Type/Size	         Description
-------------------------------------------------------------------------------------------------------
txtWORNO		WR_WORNO	            PM_WRMST	          Varchar(10)	     Work Order no.
txtWORDT		WR_WORDT			    PM_WRMST              TimeStamp	         Work Order date	
txtWORTM        WR_WORTM				PM_WRMST              TimeStamp          Work Order time
txtTAGNO        WR_TAGNO       		    PM_WRMST              Varchar(15)        Tag No.	
txtPLNCD		WR_PLNCD/CMT_CODCD	    PM_WRMST/CO_CDTRN     Varchar(3)         Plant Code
txtPLNDS		CMT_CODDS		        CO_CDTRN                       		     Plant Description
txtARACD		WR_ARACD/CMT_CODCD		PM_WRMST/CO_CDTRN     Varchar(3)         Area Code
txtARADS		CMT_CODDS				PM_WRMST                                 Area Description
txtFMDCD							    PM_WRMST                                 From Dept.Code
txtFMDNM								PM_WRMST                                 From Dept Name
txtTODCD								PM_WRMST                                 To Dept.Code
txtTODNM								PM_WRMST                                 To Dept.Name
txtWRKTP								PM_WRMST                                 Work Type Code
txtWRKDS								PM_WRMST                                 Work Type Description
txtPRICD								PM_WRMST              Varchar(1)         Priority Code
txtPRIDS								PM_WRMST                                 Priority Description
txtEQPID								PM_WRMST              Varchar(15)        Equipment ID
txtREQBY								PM_WRMST              Varchar(25)        Requested By
txtHAZPR								PM_WRMST              Varchar(50)        Hazard Present
txtREQDT								PM_WRMST              TimeStamp          Requested Date
txtREQTM								PM_WRMST              TimeStamp          Requested Time
txtRECDT								PM_WRMST              TimeStamp          Received Date
txtRECBY								PM_WRMST              Varchar(25)        Received By
txtWORST								PM_WRMST                                 Work Status
txtPRDLS								PM_WRMST                                 Production Loss 
txtCNTOR								PM_WRMST					             Contractor
txtMSSNO								PM_WRMST					             Maintenance Specification Sheet
txtHOMDT								PM_WRMST              TimeStamp          Handed over to Maintenance Date
txtHOMTM								PM_WRMST			  TimeStamp		     Handed over to Maintenance Time
txtWRPNO								PM_WRMST              Varchar(8)         Work Permit No.
txtBDDAT								PM_WRMST			  TimeStamp		     Break Down Date
txtBDTIM								PM_WRMST              TimeStamp          Break Down Time
txtSTRDT								PM_WRMST              TimeStamp          Start Date
txtSTRTM								PM_WRMST			  TimeStamp		     Start Time
txtENDDT								PM_WRMST			  TimeStamp		     End Date
txtENDTM								PM_WRMST			  TimeStamp		     End Time
txtWKREQ								PM_WRMST                                 Work Request
txtWRKDN								PM_WRMST              Varchar(500)       Work Down
txtREMDS								PM_WRMST					             Remark Description
chkHWPFL								PM_WRMST              Varchar(1)         Hot Work Permit Flag
chkCSPFL								PM_WRMST	          Varchar(1)		 Confined Space Flag
chkWHPFL								PM_WRMST              Varchar(1)         Height Work Permit Flag
chkEXPFL								PM_WRMST			  Varchar(1)		 Excavation Flag
chkELPFL								PM_WRMST              Varchar(1)         Electrical Flag


Table tblMATCD:
txtMATCD	    CT_MATCD			    CO_CTMST	         Varchar(3)          Material Code
txtMATDS        CT_MATDS      		    CO_CTMST	         Varchar(50)	     Material Description
txtUOMCD        CT_UOMCD                CO_CTMST             VARCHAR(1)          Unit of measure
txtSTKQT 															             Quantity

Table tblCODES:
txtFLRCD		CMT_CODCD				CO_CDTRN                                 Failure Code
txtRSNCD	    CMT_CODCD				CO_CDTRN                                 Reason Code


----------------------------------------------------------------------------------------------------------
Validations :
While saving the data:-
1]Equipment Main Type is compulsary.
2]In Table Attribute Serial No. is compulsary.
3]Attribute description is compulsary.
4]Indicator is compulsary. 

Help screen(F1):
1]Work Request No. 
2]Tag No.
3]Plant Code.
4]Equipment ID.
5]Area Code.
6]From Dept.
7]To Dept.
8]Work Type No.
9]Priority.

Other Requirement:

*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import javax.swing.table.*;import java.awt.event.FocusEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;import javax.swing.JComponent;
import javax.swing.JTable;import java.sql.ResultSet;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.DefaultCellEditor;import javax.swing.JComboBox;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTabbedPane;import javax.swing.JPanel;
import javax.swing.*;

public class pm_tewrq extends cl_pbase 
{	
  JTextField txtWORNO,txtWORDT,txtWORTM,txtTAGNO,txtTAGDS,txtPLNCD,txtPLNDS,txtARACD,txtARADS,txtEQPID,txtEQPDS,txtFMDCD,txtFMDNM,txtTODCD,txtTODNM,txtWRKTP,txtWRKDS,txtPRICD,txtPRIDS,txtREQBY;
  JTextField txtHAZPR,txtREQDT,txtREQTM,txtRECDT,txtRECBY,txtWORST,txtPRDLS,txtCNTOR,txtMSSNO,txtMSSDS;
  JTextField txtHOMDT,txtHOMTM,txtWRPNO,txtBDDAT,txtBDTIM,txtSTRDT,txtSTRTM,txtENDDT,txtENDTM;
  
  JTextArea txtWKREQ,txtWRKDN,txtREMDS;
  private JScrollPane scrollWKREQ;
  private JScrollPane scrollWRKDN;
  private JScrollPane scrollREMDS;
  private JCheckBox chkHWPFL,chkCSPFL,chkWHPFL,chkEXPFL,chkELPFL;
  
  private  cl_JTable tblMATCD,tblCODES;
  
  int TB1_CHKFL = 0;                   JCheckBox chkCHKFL;
  int TB1_MATCD = 1;                   JTextField txtMATCD;
  int TB1_MATDS = 2;                   JTextField txtMATDS;
  int TB1_UOMCD = 3;                   JTextField txtUOMCD;
  int TB1_STKQT = 4;                   JTextField txtSTKQT;
  
  
  int TB2_CHKFL = 0;                   JCheckBox chkCHKFL1;
  int TB2_FLRCD = 1;                   JTextField txtFLRCD;
  int TB2_RSNCD = 2;                   JTextField txtRSNCD;
  
  
  private JTabbedPane jtpMANTAB;          
  private JPanel pnlWRKRQ,pnlWRKDN,pnlFEDBK; 
  
  private Hashtable<String,String[]> hstCDTRN;
  private Hashtable<String,String> hstDPTCD;
  
  /** Array elements for records picked up from Code Transactoion */
  private int intCDTRN_TOT = 2;			
  private int intAE_CMT_CODCD = 0;		
  private int intAE_CMT_CODDS = 1;	
  
  /** Variables for Code Transaction Table
  */
  private String strCGMTP;		
  private String strCGSTP;		
  private String strCODCD;
 
  private INPVF oINPVF;
  String strDEPCD = "";
     
  pm_tewrq()
  {
	super(2);
	
	try
	{
		setMatrix(20,20);
		
		pnlWRKRQ = new JPanel();
		pnlWRKDN = new JPanel();
		pnlFEDBK = new JPanel();
		pnlWRKRQ.setLayout(null);
		pnlWRKDN.setLayout(null);
		pnlFEDBK.setLayout(null);
		jtpMANTAB=new JTabbedPane();
		jtpMANTAB.add(pnlWRKRQ,"Work Request");
		jtpMANTAB.add(pnlWRKDN,"Work Status");
		jtpMANTAB.add(pnlFEDBK,"Feedback");
		
		
		add(new JLabel("W.R.No."),2,2,1,2,this,'L');  
		add(txtWORNO = new TxtLimit(10),2,4,1,4,this,'L'); 
		
		add(new JLabel("W.R.Date/Time"),2,10,1,2,this,'L');  
		add(txtWORDT = new TxtDate(),2,12,1,2,this,'L');
		add(txtWORTM = new TxtTime(),2,14,1,2,this,'L');
		
		
		add(new JLabel("Tag No."),3,2,1,3,this,'L');  
		add(txtTAGNO = new TxtLimit(8),3,4,1,2,this,'L'); 
		add(txtTAGDS = new TxtLimit(50),3,6,1,5,this,'L'); 
		
		add(new JLabel("Plant"),3,12,1,2,this,'L'); 
		add(txtPLNCD = new TxtLimit(3),3,13,1,1,this,'L'); 
		add(txtPLNDS = new TxtLimit(50),3,14,1,4,this,'L');
		
		add(new JLabel("Eqpt.ID"),4,2,1,2,this,'L');  
		add(txtEQPID= new TxtLimit(8),4,4,1,2,this,'L'); 
		add(txtEQPDS= new TxtLimit(50),4,6,1,5,this,'L'); 
		
		add(new JLabel("Area"),4,12,1,2,this,'L'); 
		add(txtARACD = new TxtLimit(3),4,13,1,1,this,'L'); 
		add(txtARADS = new TxtLimit(50),4,14,1,4,this,'L');
		
		add(new JLabel("From Dept"),5,2,1,2,this,'L');  
		add(txtFMDCD = new TxtLimit(3),5,4,1,1,this,'L'); 
		add(txtFMDNM = new TxtLimit(50),5,5,1,3,this,'L'); 
		
		add(new JLabel("To Dept"),5,12,1,2,this,'L');  
		add(txtTODCD = new TxtLimit(3),5,13,1,1,this,'L'); 
		add(txtTODNM = new TxtLimit(50),5,14,1,4,this,'L'); 
		
		add(new JLabel("Work Type"),6,2,1,3,this,'L'); 
		add(txtWRKTP = new TxtLimit(2),6,4,1,1,this,'L');      
		add(txtWRKDS = new TxtLimit(50),6,5,1,6,this,'L'); 
		
		add(new JLabel("Priority"),6,12,1,2,this,'L');  
		add(txtPRICD = new TxtNumLimit(2),6,13,1,1,this,'L'); 
		add(txtPRIDS = new TxtLimit(50),6,14,1,4,this,'L'); 
		
		add(new JLabel("Description of Failure/Job"),1,2,1,6,pnlWRKRQ,'L');  
		add(txtWKREQ = new JTextArea(),2,2,3,13,pnlWRKRQ,'L');
		add(scrollWKREQ = new JScrollPane(txtWKREQ,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),2,2,3,13,pnlWRKRQ,'L');
	
		add(new JLabel("Requested by:"),6,2,1,3,pnlWRKRQ,'L'); 
		add(txtREQBY = new TxtLimit(25),6,5,1,3,pnlWRKRQ,'L'); 
		
		add(new JLabel("Hazard Present:"),6,9,1,3,pnlWRKRQ,'L'); 
		add(txtHAZPR = new TxtLimit(50),6,11,1,4,pnlWRKRQ,'L'); 
		
		add(new JLabel("Reqd. By Date/Time"),7,2,1,2,pnlWRKRQ,'L'); 
		add(txtREQDT = new TxtDate(),7,5,1,2,pnlWRKRQ,'L');
		add(txtREQTM = new TxtTime(),7,7,1,2,pnlWRKRQ,'L'); 
		
		add(new JLabel("Received Dt."),2,1,1,2,pnlWRKDN,'L'); 
		add(txtRECDT = new TxtDate(),2,3,1,2,pnlWRKDN,'L'); 
		
		add(new JLabel("Received By"),2,5,1,2,pnlWRKDN,'L'); 
		add(txtRECBY = new TxtLimit(25),2,7,1,2,pnlWRKDN,'L'); 
		
		add(new JLabel("MSS No."),2,9,1,2,pnlWRKDN,'L'); 
		add(txtMSSNO = new TxtLimit(8),2,11,1,2,pnlWRKDN,'L');
		add(txtMSSDS = new TxtLimit(30),2,13,1,3,pnlWRKDN,'L');
		
		add(new JLabel("Contractor"),2,16,1,2,pnlWRKDN,'L'); 
		add(txtCNTOR = new TxtLimit(50),2,18,1,2,pnlWRKDN,'L');
		
		add(new JLabel("Work Status"),4,1,1,2,pnlWRKDN,'L'); 
		add(txtWORST = new TxtLimit(50),4,3,1,2,pnlWRKDN,'L');
		
		add(new JLabel("B/D Occured"),4,5,1,2,pnlWRKDN,'L'); 
		add(txtBDDAT = new TxtDate(),4,7,1,2,pnlWRKDN,'L'); 
		add(txtBDTIM = new TxtTime(),4,9,1,1,pnlWRKDN,'L'); 
		
		add(new JLabel("Work Started"),4,10,1,2,pnlWRKDN,'L'); 
		add(txtSTRDT = new TxtDate(),4,12,1,2,pnlWRKDN,'L');
		add(txtSTRTM = new TxtTime(),4,14,1,1,pnlWRKDN,'L');
		
		
		add(new JLabel("Production Loss"),6,1,1,3,pnlWRKDN,'L'); 
		add(txtPRDLS = new TxtNumLimit(12),6,3,1,2,pnlWRKDN,'L');
		add(new JLabel("MT"),6,5,1,1,pnlWRKDN,'L'); 
		
		add(new JLabel("H/O to Maint."),6,5,1,2,pnlWRKDN,'L'); 
		add(txtHOMDT  = new TxtDate(),6,7,1,2,pnlWRKDN,'L'); 
		add(txtHOMTM  = new TxtTime(),6,9,1,1,pnlWRKDN,'L');
		
		add(new JLabel("Work Completed"),6,10,1,3,pnlWRKDN,'L'); 
		add(txtENDDT = new TxtDate(),6,12,1,2,pnlWRKDN,'L');
		add(txtENDTM = new TxtTime(),6,14,1,1,pnlWRKDN,'L');
		
		add(new JLabel("Work Permit No."),8,1,1,3,pnlWRKDN,'L'); 
		add(txtWRPNO = new TxtLimit(8),8,3,1,2,pnlWRKDN,'L');
		
		add(chkCSPFL=new JCheckBox("Confined Space", false),8,5,1,3,pnlWRKDN,'L');
		add(chkHWPFL=new JCheckBox("Height", false),8,8,1,2,pnlWRKDN,'L');
		add(chkWHPFL=new JCheckBox("Hot", false),8,10,1,2,pnlWRKDN,'L');
		add(chkEXPFL=new JCheckBox("Excavation", false),8,12,1,2,pnlWRKDN,'L');
		add(chkELPFL=new JCheckBox("Electrical", false),8,14,1,2,pnlWRKDN,'L');
		
		add(new JLabel("Work Done"),1,1,1,2,pnlFEDBK,'L');  
		add(txtWRKDN = new JTextArea(),1,3,4,8,pnlFEDBK,'L');
		add(scrollWRKDN = new JScrollPane(txtWRKDN,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),1,3,4,8,pnlFEDBK,'L');
	
		add(new JLabel("Remedy"),1,11,1,2,pnlFEDBK,'L'); 
		add(txtREMDS = new JTextArea(),1,12,4,8,pnlFEDBK,'L'); 
		add(scrollREMDS = new JScrollPane(txtREMDS,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),1,12,4,8,pnlFEDBK,'L');
	
		String[] L_strTBLHD = {"","Material Code","Unit","Quantity","Description"};
		int[] L_intCOLSZ = {10,100,50,100,100};
		tblMATCD= crtTBLPNL1(pnlFEDBK,L_strTBLHD,200,6,3,4,8,L_intCOLSZ,new int[]{0});
		tblMATCD.setEnabled(false);
		
		tblMATCD.setCellEditor(TB1_CHKFL,chkCHKFL =new JCheckBox());
		tblMATCD.setCellEditor(TB1_MATCD,txtMATCD =new TxtLimit(10));
		tblMATCD.setCellEditor(TB1_MATDS,txtMATDS =new TxtLimit(60));
		tblMATCD.setCellEditor(TB1_UOMCD,txtUOMCD =new TxtLimit(3));
		tblMATCD.setCellEditor(TB1_STKQT,txtSTKQT =new TxtNumLimit(12));
		
		String[] L_strTBLHD1 = {"","Fail Code","Rsn.Code"};
		int[] L_intCOLSZ1 = {10,120,130};
		tblCODES= crtTBLPNL1(pnlFEDBK,L_strTBLHD1,200,6,12,4,6,L_intCOLSZ1,new int[]{0});
		tblCODES.setEnabled(false);
		
		tblCODES.setCellEditor(TB2_CHKFL,chkCHKFL1 =new JCheckBox());
		tblCODES.setCellEditor(TB2_FLRCD,txtFLRCD =new TxtLimit(2));
		tblCODES.setCellEditor(TB2_RSNCD,txtRSNCD =new TxtLimit(2));
		
		add(jtpMANTAB,7,2,11,19,this,'L');
		
		oINPVF=new INPVF();
		txtTAGNO.setInputVerifier(oINPVF);
		txtWORNO.setInputVerifier(oINPVF);
		txtPLNCD.setInputVerifier(oINPVF);
		txtARACD.setInputVerifier(oINPVF);
		txtWORDT.setInputVerifier(oINPVF);
		txtEQPID.setInputVerifier(oINPVF);
		txtFMDCD.setInputVerifier(oINPVF);
		txtTODCD.setInputVerifier(oINPVF);
		txtWRKTP.setInputVerifier(oINPVF);
		txtPRICD.setInputVerifier(oINPVF);
		txtREQBY.setInputVerifier(oINPVF);
		txtHAZPR.setInputVerifier(oINPVF);
		txtREQDT.setInputVerifier(oINPVF);
		txtREQTM.setInputVerifier(oINPVF);
		txtMSSNO.setInputVerifier(oINPVF);
		
		tblMATCD.addKeyListener(this);
		txtMATCD.addKeyListener(this);
		txtFLRCD.addKeyListener(this);
		txtRSNCD.addKeyListener(this);
		
		
		hstCDTRN = new Hashtable<String,String[]>();
		hstCDTRN.clear();
		crtCDTRN("'MSTPMXXPLN','MSTPMXXARA','MSTPMXXWRT','MSTPMXXPRT'","",hstCDTRN);
		
		setENBL(false);
		
		//+cl_dat.M_strEMPNO_pbst+
		hstDPTCD = new Hashtable<String,String>();
		hstDPTCD.clear();
		M_strSQLQRY = "select CMT_CODCD,CMT_SHRDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		//System.out.println("hash"+M_strSQLQRY);
		if(M_rstRSSET !=null)                           
		{ 
			while(M_rstRSSET.next())
			{	
				hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_SHRDS"));
			}	 
		}
		if(M_rstRSSET != null)
		{	
			M_rstRSSET.close();
		}
		
		String L_strSQLQRY = " select EP_DPTCD from HR_EPMST where EP_EMPNO = '"+cl_dat.M_strEMPNO_pbst+"' and EP_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
		System.out.println("dept.code"+L_strSQLQRY);
		if(L_rstRSSET !=null)                           
		{ 
			while(L_rstRSSET.next())
			{	
				strDEPCD = L_rstRSSET.getString("EP_DPTCD");
				//System.out.println("USRCD"+strDEPCD);
			}
		}
		if(L_rstRSSET != null)
		{	
			L_rstRSSET.close();
		}
	} 
	catch(Exception L_EX)
	{
	    setMSG(L_EX,"Constructor");
	}
  }
  
  public void actionPerformed(ActionEvent L_AE)
  {
	super.actionPerformed(L_AE);
	    try
	    {
	    	if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) 
		    {
		  		clrCOMP();
		  		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					txtWORNO.requestFocus();	 
			  		txtTAGDS.setEnabled(false);
					txtEQPDS.setEnabled(false);
				    txtPLNCD.setEnabled(false);
					txtARACD.setEnabled(false);
					txtPLNDS.setEnabled(false);
					txtARADS.setEnabled(false);
					txtFMDNM.setEnabled(false);
					txtTODNM.setEnabled(false);
					txtWRKDS.setEnabled(false);
					txtPRIDS.setEnabled(false);
					txtMSSDS.setEnabled(false);
			  		if(strDEPCD.toString().equals(txtFMDCD.getText().toString()))
					{
						jtpMANTAB.setEnabledAt(0,true);
						jtpMANTAB.setEnabledAt(1,false);
						jtpMANTAB.setEnabledAt(2,false);
					}
					if(strDEPCD.toString().equals(txtTODCD.getText().toString()))
					{
						jtpMANTAB.setEnabledAt(0,false);
						jtpMANTAB.setEnabledAt(1,true);
						jtpMANTAB.setEnabledAt(2,true);
					}
			    	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)
			        || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			    	{
			    		txtWORNO.setEnabled(true);
			    		txtWORDT.setEnabled(false);
			    		txtEQPID.setEnabled(false);	
			    		txtTAGNO.setEnabled(false);
			    		txtFMDCD.setEnabled(false);
			    		txtTODCD.setEnabled(false);
			    		txtWRKTP.setEnabled(false);
			    		txtPRICD.setEnabled(false);
			    		txtWKREQ.setEnabled(false);
			    		txtHAZPR.setEnabled(false);
			    		txtREQBY.setEnabled(false);
			    		txtREQDT.setEnabled(false);
			    		txtREQTM.setEnabled(false);
			    		
			    		txtRECDT.setEnabled(false);
						txtRECBY.setEnabled(false);
						txtWORST.setEnabled(false);
						txtPRDLS.setEnabled(false);
						txtCNTOR.setEnabled(false);
						txtMSSNO.setEnabled(false);
						txtHOMDT.setEnabled(false);
						txtHOMTM.setEnabled(false);
						txtWRPNO.setEnabled(false);
						txtBDDAT.setEnabled(false);
						txtBDTIM.setEnabled(false);
						txtSTRDT.setEnabled(false);
						txtSTRTM.setEnabled(false);
						txtENDDT.setEnabled(false);
						txtENDTM.setEnabled(false);
						txtWRKDN.setEnabled(false);
						txtREMDS.setEnabled(false);
						tblCODES.setEnabled(false);
						tblMATCD.setEnabled(false);
						chkHWPFL.setEnabled(false);
						chkCSPFL.setEnabled(false);
						chkWHPFL.setEnabled(false);
						chkEXPFL.setEnabled(false);
						chkELPFL.setEnabled(false);
				    		
			    	}
			        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)
			        ||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))			
			    	{
			        	txtWORNO.setEnabled(true);
			    		txtWORDT.setEnabled(true);
			    		txtEQPID.setEnabled(true);	
			    		txtTAGNO.setEnabled(true);
			    		txtFMDCD.setEnabled(true);
			    		txtTODCD.setEnabled(true);
			    		txtWRKTP.setEnabled(true);
			    		txtPRICD.setEnabled(true);
			        }
			    } 
		  		else
		  			setENBL(false);
		    }
	    	if(M_objSOURC == txtFMDCD) 
		    { 
		  		if(strDEPCD.toString().equals(txtFMDCD.getText().toString()))
				{
					jtpMANTAB.setEnabledAt(0,true);
					jtpMANTAB.setEnabledAt(1,false);
					jtpMANTAB.setEnabledAt(2,false);
				}
		    }
	    	if(M_objSOURC == txtTODCD) 
		    { 
				if(strDEPCD.toString().equals(txtTODCD.getText().toString()))
				{
					jtpMANTAB.setEnabledAt(0,false);
					jtpMANTAB.setEnabledAt(1,true);
					jtpMANTAB.setEnabledAt(2,true);
				}
		    }
	    }
	    catch(Exception L_EX)
	    {
	      setMSG(L_EX,"exeMODREC()");
	    }
  }
  
  public void keyPressed(KeyEvent L_KE)
  {
    super.keyPressed(L_KE);
	try
	{
		if(L_KE.getKeyCode() == L_KE.VK_F1 )
    	{
			
			if(M_objSOURC==txtWORNO)	
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtWORNO";
    		  String L_ARRHDR[] = {"Work No","Work Date"}; 
    		  M_strSQLQRY = "select WR_WORNO,WR_WORDT from PM_WRMST"; 
    		  M_strSQLQRY+= " where WR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'AND isnull(WR_STSFL,'')<>'X'";
    		  if(txtWORNO.getText().length()>0)				
    			  M_strSQLQRY+= " AND WR_WORNO like '"+txtWORNO.getText().trim()+"%'";
    		  M_strSQLQRY += " order by WR_WORNO";  
    		  //System.out.println("equipment"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    		}
			else if(M_objSOURC==txtTAGNO)	
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtTAGNO";
    		  String L_ARRHDR[] = {"Tag No.","Tag Description","Plan Code","Area Code"};  
			  M_strSQLQRY=" SELECT distinct TG_TAGNO,TG_TAGDS,TG_PLNCD,TG_ARACD from PM_TGMST";
			  M_strSQLQRY+= " where TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
			  if(txtTAGNO.getText().length()>0)				
				  M_strSQLQRY+= " AND TG_TAGNO like '"+txtTAGNO.getText().trim()+"%'";
			  M_strSQLQRY+= " order by TG_TAGNO ";
			  System.out.println("TAG Addition F1"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,4,"CT");
    		}
			else if(M_objSOURC == txtEQPID)
			{
			   M_strHLPFLD = "txtEQPID";
			   cl_dat.M_flgHELPFL_pbst = true;
			   setCursor(cl_dat.M_curWTSTS_pbst);	   
			   M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS from PM_EQMST";
			   M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EQ_STSFL,'')<>'X'";
			   if(txtTAGNO.getText().length()>0)
					M_strSQLQRY+=" and EQ_TAGNO='"+txtTAGNO.getText()+"' ";
			   if(txtEQPID.getText().length()>0)				
				   M_strSQLQRY+= " AND EQ_EQPID like '"+txtEQPID.getText().trim()+"%'";
			   M_strSQLQRY+= " order by EQ_EQPID";
			   System.out.println("EQPT_ID>>"+M_strSQLQRY);
			   cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Id","Equipment Description"},2,"CT");
			   setCursor(cl_dat.M_curDFSTS_pbst);
			}	
			else if(M_objSOURC==txtPLNCD)	
    		{
    		  cl_dat.M_flgHELPFL_pbst = true;
    		  M_strHLPFLD = "txtPLNCD";
    		  String L_ARRHDR[] = {"Plant Code","Description"};
    		  M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn";
			  M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXPLN' AND isnull(CMT_STSFL,'')<>'X'";
			  if(txtPLNCD.getText().length()>0)				
			  M_strSQLQRY+=  " AND cmt_codcd like '"+txtPLNCD.getText().trim()+"%'";
			  M_strSQLQRY+= " order by cmt_codcd";
    		  //System.out.println("plantf1"+M_strSQLQRY);
    		  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
    		}
			
			else if(M_objSOURC==txtARACD)	
    		{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtARACD";
				String L_ARRHDR[] = {"Area Code","Description"};
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
				M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXARA' AND isnull(CMT_STSFL,'')<>'X' ";
				if(txtARACD.getText().length()>0)				
				M_strSQLQRY+= " AND cmt_codcd like '"+txtARACD.getText().trim()+"%'";
				M_strSQLQRY+= " order by cmt_codcd";
				//System.out.println("txtARACD f1>>"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
    		}
			
			else if(M_objSOURC==txtTODCD)
			{
		    	  M_strHLPFLD = "txtTODCD";
				  M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
				  M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and  cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"')";
				  //System.out.println(">>>>DPTCD>>>>"+M_strSQLQRY);
				  cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code", "Department Description"},2,"CT");
				  setCursor(cl_dat.M_curWTSTS_pbst);
			}
		         
			else if(M_objSOURC==txtFMDCD)
			{
				M_strHLPFLD = "txtFMDCD";
			  	M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
			  	M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and  cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"')";
			  	//System.out.println(">>>>DPTCD>>>>"+M_strSQLQRY);
			  	cl_hlp(M_strSQLQRY,2,1,new String[]{"Department Code", "Department Description"},2,"CT");
			  	setCursor(cl_dat.M_curWTSTS_pbst);
			}
		         
			else if(M_objSOURC==txtWRKTP)	
    		{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtWRKTP";
				String L_ARRHDR[] = {"Work Type","Work Description"};
				M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
				M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXWRT' AND isnull(CMT_STSFL,'')<>'X'";
				if(txtWRKTP.getText().length()>0)				
					M_strSQLQRY+= " AND cmt_codcd like '"+txtWRKTP.getText().trim()+"%'";
				M_strSQLQRY += " order by cmt_codcd";
				System.out.println("worktype"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");     
			}
			
			else if(M_objSOURC==txtPRICD)	
    		{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPRICD";
				String L_ARRHDR[] = {"Priority Code","Priority Description"};
				M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
				M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXPRT' AND isnull(CMT_STSFL,'')<>'X'";
				if(txtPRICD.getText().length()>0)				
					M_strSQLQRY+= " AND cmt_codcd like '"+txtPRICD.getText().trim()+"%'";
				M_strSQLQRY += " order by cmt_codcd";
				System.out.println("subtype"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");     
			}
		    else if(M_objSOURC==txtREQBY)		
			{
				  cl_dat.M_flgHELPFL_pbst = true;
				  M_strHLPFLD = "txtREQBY";
				  String L_ARRHDR[] = {"Employee Name"};
				  M_strSQLQRY = "select EP_LSTNM + ' '+ SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null";
				  if(txtFMDCD.getText().length()>0)
						M_strSQLQRY += " and EP_DPTCD='"+txtFMDCD.getText()+"'";
				  M_strSQLQRY += " order by EP_LSTNM";
				  System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
				  cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
			}
		    else if(M_objSOURC==txtRECBY)		
			{
				  cl_dat.M_flgHELPFL_pbst = true;
				  M_strHLPFLD = "txtRECBY";
				  String L_ARRHDR[] = {"Employee Name"};
				  M_strSQLQRY = "select EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null";
				  if(txtFMDCD.getText().length()>0)
						M_strSQLQRY += " and EP_DPTCD='"+txtFMDCD.getText()+"'";
				  M_strSQLQRY += " order by EP_LSTNM";
				  System.out.println(">>>>RECBY>>>>"+M_strSQLQRY);
				  cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
			}
			
			else if(M_objSOURC==txtWRPNO)		
			{
				  cl_dat.M_flgHELPFL_pbst = true;
				  M_strHLPFLD = "txtWRPNO";
				  String L_ARRHDR[] = {"Work Permit Number"};
				  M_strSQLQRY = "select WR_WRPNO from PM_WRMST"; 
				  M_strSQLQRY+= " where WR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'AND isnull(WR_STSFL,'')<>'X'";
				  if(txtWRPNO.getText().length()>0)				
					  M_strSQLQRY+= " AND WR_WRPNO like '"+txtWRPNO.getText().trim()+"%'";
				  M_strSQLQRY += " order by WR_WORNO";  
				  //System.out.println(">>>>WRPNO>>>>"+M_strSQLQRY);
				  cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
			}
			
			else if(M_objSOURC==txtMATCD)		
			{
				  cl_dat.M_flgHELPFL_pbst = true;
				  M_strHLPFLD = "txtMATCD";
				  String L_ARRHDR[] = {"Material Code","Description","Unit of measure"};
				  M_strSQLQRY = "select CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST"; 
				  M_strSQLQRY+= " where isnull(CT_STSFL,'')<>'X'";
				  if(txtMATCD.getText().length()>0)				
					  M_strSQLQRY+= " AND CT_MATCD like '"+txtMATCD.getText().trim()+"%'";
				  M_strSQLQRY += " order by CT_MATCD";  
				  System.out.println(">>>>MATCD>>>"+M_strSQLQRY);
				  cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
			}
			
			else if(M_objSOURC==txtFLRCD)		
			{
				  cl_dat.M_flgHELPFL_pbst = true;
				  M_strHLPFLD = "txtFLRCD";
				  String L_ARRHDR[] = {"Failure code","Description"};
				  M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
				  M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXFLR' AND isnull(CMT_STSFL,'')<>'X'";
				  if(txtFLRCD.getText().length()>0)				
					 M_strSQLQRY+= " AND cmt_codcd like '"+txtFLRCD.getText().trim()+"%'";
				  M_strSQLQRY += " order by cmt_codcd"; 
				  System.out.println(">>>>FLRCD>>>>"+M_strSQLQRY);
				  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
			}
			
			else if(M_objSOURC==txtRSNCD)		
			{
				  cl_dat.M_flgHELPFL_pbst = true;
				  M_strHLPFLD = "txtRSNCD";
				  String L_ARRHDR[] = {"Reason code","Description"};
				  M_strSQLQRY = "select cmt_codcd,cmt_codds from co_cdtrn"; 
				  M_strSQLQRY+= " where cmt_cgmtp='MST' and cmt_cgstp='PMXXRSN' AND isnull(CMT_STSFL,'')<>'X'";
				  if(txtRSNCD.getText().length()>0)				
					 M_strSQLQRY+= " AND cmt_codcd like '"+txtRSNCD.getText().trim()+"%'";
				  M_strSQLQRY += " order by cmt_codcd";
				  System.out.println(">>>>RSNCD>>>>"+M_strSQLQRY);
				  cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
			}	
			else if(M_objSOURC==txtMSSNO)		
			{
				M_strHLPFLD = "txtMSSNO";
				cl_dat.M_flgHELPFL_pbst = true;
				setCursor(cl_dat.M_curWTSTS_pbst);
				M_strSQLQRY=" SELECT distinct MS_MSSNO,MS_MSSDS from PM_MSMST where MS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MS_STSFL,'')<>'X'";
				if(txtMSSNO.getText().length() >0)
					M_strSQLQRY += " AND MS_MSSNO like '"+txtMSSNO.getText().trim()+"%'";
				M_strSQLQRY += " order by MS_MSSNO";
				//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,new String[]{"MSS No","Description"},2,"CT");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
    	}
    	
		if(L_KE.getKeyCode() == L_KE.VK_ENTER )
	    {
	
			if(M_objSOURC==txtWORNO)
			{	
			  txtWORDT.requestFocus();
			  setMSG("Enter work date..",'N');
		    }
			else if(M_objSOURC==txtWORDT)
			{	
			  txtWORTM.requestFocus();
			  setMSG("Enter work Time..",'N');
		    }	
			else if(M_objSOURC == txtWORTM)
			{
				txtTAGNO.requestFocus();
				setMSG("Enter Tag No. or Press F1 to Select from List..",'N');
			}
			else if(M_objSOURC == txtTAGNO)
			{
				txtEQPID.requestFocus();
				setMSG("Enter Equipment ID or Press F1 to Select from List..",'N');
				
				//txtPLNCD.requestFocus();
				//setMSG("Enter Plant code or Press F1 to Select from List..",'N');	
			}
			/*else if(M_objSOURC ==txtPLNCD )
			{
				if(txtPLNCD.getText().length()==0)
					txtPLNDS.setText("");
				txtEQPID.requestFocus();
				setMSG("Enter Equipment ID or Press F1 to Select from List..",'N');
			}*/
			else if(M_objSOURC == txtEQPID)
			{	
				//txtARACD.requestFocus();
				//setMSG("Enter Area code or Press F1 to Select from List..",'N');	
				
				txtFMDCD.requestFocus();
				setMSG("Enter From Dept. or Press F1 to Select from List..",'N');
			}
			/*else if(M_objSOURC == txtARACD)
			{
				if(txtARACD.getText().length()==0)
					txtARADS.setText("");
				txtFMDCD.requestFocus();
				setMSG("Enter From Dept.",'N');
			}*/
			else if(M_objSOURC == txtFMDCD)
			{
				if(txtFMDCD.getText().length()==0)
					txtFMDNM.setText("");
				txtTODCD.requestFocus();
				setMSG("Enter To Dept. or Press F1 to Select from List..",'N');	
			}
			else if(M_objSOURC == txtTODCD)
			{
				if(txtTODCD.getText().length()==0)
					txtTODNM.setText("");
				txtWRKTP.requestFocus();
				setMSG("Enter Work Type or Press F1 to Select from List..",'N');	
			}
			else if(M_objSOURC == txtWRKTP)
			{
				if(txtWRKTP.getText().length()==0)
					txtWRKDS.setText("");
				txtPRICD.requestFocus();
				setMSG("Enter Priority Code or Press F1 to Select from List..",'N');
			}
			
			else if(strDEPCD.toString().equals(txtFMDCD.getText().toString()))
			{
				if(M_objSOURC == txtPRICD)
				{
					if(txtPRICD.getText().length()==0)
						txtPRICD.setText("");
					txtWKREQ.requestFocus();
					setMSG("Enter Work required..",'N');
				}
				else if(M_objSOURC == txtWKREQ)
				{
					if(txtWKREQ.getText().length()>0)
						txtWKREQ.setText(txtWKREQ.getText().toUpperCase());
						
					txtREQBY.requestFocus();
					setMSG("Enter Requested By",'N');	
				}
				else if(M_objSOURC == txtREQBY)
				{
					txtHAZPR.requestFocus();
					setMSG("Enter Hazard Present",'N');
				}
				else if(M_objSOURC == txtHAZPR)
				{
					txtREQDT.requestFocus();
					setMSG("Enter Required By Date..",'N');
				}
				else if(M_objSOURC == txtREQDT)
				{
					txtREQTM.requestFocus();
					setMSG("Enter Required By Time",'N');
				}
				else if(M_objSOURC == txtREQTM)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
			
			else if(strDEPCD.toString().equals(txtTODCD.getText().toString()))
			{
				if(M_objSOURC == txtPRICD)
				{
					if(txtPRICD.getText().length()==0)
						txtPRICD.setText("");
					txtRECDT.requestFocus();
					setMSG("Enter Received Date..",'N');
				}
				else if(M_objSOURC == txtRECDT)
				{
					txtRECBY.requestFocus();
					setMSG("Enter Received By",'N');	
				}
				else if(M_objSOURC == txtRECBY)
				{
					txtMSSNO.requestFocus();
					setMSG("Enter Maintenance specification sheet",'N');
				}
				else if(M_objSOURC == txtMSSNO)
				{
					txtCNTOR.requestFocus();
					setMSG("Enter Contractor",'N');
				}
				else if(M_objSOURC == txtCNTOR)
				{
					txtWRKDN.requestFocus();
					setMSG("Enter Work Done",'N');
				}
				else if(M_objSOURC == txtWRKDN)
				{
					txtREMDS.requestFocus();
					setMSG("Enter Remedy Description",'N');	
				}
				else if(M_objSOURC == txtREMDS)
				{
					txtWORST.requestFocus();
					setMSG("Enter Work Status",'N');
				}
				else if(M_objSOURC == txtWORST)
				{
					txtBDDAT.requestFocus();
					setMSG("Enter B/D occured date",'N');
				}
				else if(M_objSOURC == txtBDDAT)
				{
					txtBDTIM.requestFocus();
					setMSG("Enter B/D occured time",'N');
				}
				else if(M_objSOURC == txtBDTIM)
				{
					txtSTRDT.requestFocus();
					setMSG("Enter work started date",'N');
				}
				else if(M_objSOURC == txtSTRDT)
				{
					txtSTRTM.requestFocus();
					setMSG("Enter work started time",'N');
				}
				else if(M_objSOURC == txtSTRTM)
				{
					txtPRDLS.requestFocus();
					setMSG("Enter Production loss in MT",'N');
				}
				else if(M_objSOURC == txtPRDLS)
				{
					txtHOMDT.requestFocus();
					setMSG("Enter Handed over maintenance date",'N');
				}
				else if(M_objSOURC == txtHOMDT)
				{
					txtHOMTM.requestFocus();
					setMSG("Enter Handed over maintenance time",'N');
				}
				else if(M_objSOURC == txtHOMTM)
				{
					txtENDDT.requestFocus();
					setMSG("Enter work end date",'N');
				}
				else if(M_objSOURC == txtENDDT)
				{
					txtENDTM.requestFocus();
					setMSG("Enter work end time",'N');
				}
				else if(M_objSOURC == txtENDTM)
				{
					txtWRPNO.requestFocus();
					setMSG("Enter work permit number",'N');
				}
				else if(M_objSOURC == txtWRPNO)
				{
					chkCSPFL.requestFocus();
				}
				else if(M_objSOURC == chkCSPFL)
				{
					chkWHPFL.requestFocus();
				}
				else if(M_objSOURC == chkWHPFL)
				{
					chkHWPFL.requestFocus();
				}
				else if(M_objSOURC == chkHWPFL)
				{
					chkEXPFL.requestFocus();
				}
				else if(M_objSOURC == chkEXPFL)
				{
					chkELPFL.requestFocus();
				}
				else if(M_objSOURC == chkELPFL)
				{
					tblMATCD.requestFocus();
				}
				else if(M_objSOURC == tblMATCD)
				{
					txtMATCD.requestFocus();
				}
				else if(M_objSOURC == txtMATCD)
				{
					txtUOMCD.requestFocus();
				}
				else if(M_objSOURC == txtUOMCD)
				{
					txtSTKQT.requestFocus();
				}
				else if(M_objSOURC == txtSTKQT)
				{
					tblCODES.requestFocus();
				}
				else if(M_objSOURC == tblCODES)
				{
					txtFLRCD.requestFocus();
				}
				else if(M_objSOURC == txtFLRCD)
				{
					txtRSNCD.requestFocus();
				}
				else if(M_objSOURC == txtRSNCD)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
			
		 }	
	}	 
   	catch(NullPointerException L_NPE)
	{
	  setMSG("keyPressed",'E');  
	  setCursor(cl_dat.M_curDFSTS_pbst);
	}
  }
  
  void exeHLPOK()
  {
 	try
 	{
 	    super.exeHLPOK();
 	    
 	    if(M_strHLPFLD.equals("txtWORNO"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtWORNO.setText(L_STRTKN.nextToken());
			 txtWORDT.setText(L_STRTKN.nextToken());
		}
 	   else if(M_strHLPFLD.equals("txtTAGNO"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtTAGNO.setText(L_STRTKN.nextToken());
			 txtTAGDS.setText(L_STRTKN.nextToken());
			 txtPLNCD.setText(L_STRTKN.nextToken());
			 txtARACD.setText(L_STRTKN.nextToken());
			 txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
			 txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
			 
		}
 	   /* else if(M_strHLPFLD.equals("txtPLNCD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtPLNCD.setText(L_STRTKN.nextToken());
			 txtPLNDS.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtARACD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtARACD.setText(L_STRTKN.nextToken());
			 txtARADS.setText(L_STRTKN.nextToken());
		}*/
		else if(M_strHLPFLD.equals("txtEQPID"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtEQPID.setText(L_STRTKN.nextToken());
			 txtEQPDS.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtFMDCD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtFMDCD.setText(L_STRTKN.nextToken());
			 txtFMDNM.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtTODCD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtTODCD.setText(L_STRTKN.nextToken());
			 txtTODNM.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtWRKTP"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtWRKTP.setText(L_STRTKN.nextToken());
			 txtWRKDS.setText(L_STRTKN.nextToken());
		}
 	    else if(M_strHLPFLD.equals("txtPRICD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtPRICD.setText(L_STRTKN.nextToken());
			 txtPRIDS.setText(L_STRTKN.nextToken());
		} 

 	    else if(M_strHLPFLD.equals("txtREQBY"))
		{
		    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		    txtREQBY.setText(L_STRTKN.nextToken());
		}
		
 	    else if(M_strHLPFLD.equals("txtRECBY"))
		{
		    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		    txtRECBY.setText(L_STRTKN.nextToken());
		}
 	    else if(M_strHLPFLD.equals("txtWRPNO"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtWRPNO.setText(L_STRTKN.nextToken());
		}
 	    else if(M_strHLPFLD.equals("txtMATCD"))
		{
			 String[] strMATDT = null;
			 strMATDT = cl_dat.M_strHELP_pbst.replace('|','~').split("~");
			 System.out.println("code"+strMATDT[0]);
			 tblMATCD.setValueAt(strMATDT[0],tblMATCD.getSelectedRow(),TB1_MATCD);
			 tblMATCD.setValueAt(strMATDT[1],tblMATCD.getSelectedRow(),TB1_MATDS);
			 if(strMATDT.length>2 && strMATDT[2].length()>1)
			 	tblMATCD.setValueAt(strMATDT[2],tblMATCD.getSelectedRow(),TB1_UOMCD);		 
		}
	    else if(M_strHLPFLD.equals("txtFLRCD"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtFLRCD.setText(L_STRTKN.nextToken());
		}  
	    else if(M_strHLPFLD.equals("txtRSNCD"))
		{
		    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		    txtRSNCD.setText(L_STRTKN.nextToken());
		}
	    else if(M_strHLPFLD.equals("txtMSSNO"))
		{
			 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			 txtMSSNO.setText(L_STRTKN.nextToken());
			 txtMSSDS.setText(L_STRTKN.nextToken());
		}   
 	}
 	catch(Exception L_EX)
 	{
 		setMSG(L_EX,"exeHLPOK"); 
 	}
  }	
  

  void clrEDITR(cl_JTable tblTABLE)
  {
	  if(tblTABLE.isEditing())
	  tblTABLE.getCellEditor().stopCellEditing();
	  tblTABLE.setRowSelectionInterval(0,0);
	  tblTABLE.setColumnSelectionInterval(0,0);	
  }
  
  
	/** Method to get Data from PM_ATMST in tblCODES,tblMATCD*/
  void getDATA()
  {
	  try
	  {
		  setCursor(cl_dat.M_curWTSTS_pbst);
		  tblCODES.clrTABLE();
  		  tblMATCD.clrTABLE();
  		  clrCOMP();
		  int L_CNT=0;
		  M_strSQLQRY = "select WR_WORNO,WR_WORDT,WR_PLNCD,WR_ARACD,WR_EQPID,WR_TAGNO,WR_WRKTP,WR_PRICD,WR_WKREQ,WR_HAZPR,WR_BDDAT,WR_BDTIM,WR_STRDT,WR_STRTM,WR_ENDDT,WR_ENDTM,WR_HOMDT,WR_HOMTM,WR_WRKDN,WR_REMDS,WR_RECDT,WR_REQBY,WR_REQDT,WR_REQTM,WR_WRPNO,WR_HWPFL,WR_CSPFL,WR_WHPFL,WR_EXPFL,WR_ELPFL";
		  M_strSQLQRY+=	" FROM PM_WRMST";
		  M_strSQLQRY+= " where WR_WORNO ='"+txtWORNO.getText()+"'";
		  M_strSQLQRY+= " and isnull(WR_STSFL,'')<>'X'";
			M_strSQLQRY+= " and WR_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'"; 
			 System.out.println("getData"+M_strSQLQRY);
		  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		  //System.out.println("getData"+M_strSQLQRY);
		  if(M_rstRSSET !=null)                           
		  { 
			 while(M_rstRSSET.next())
			 {
				 
				 if(M_rstRSSET.getDate("WR_WORDT")== null)
					 txtWORDT.setText("");
				 else
				 txtWORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_WORDT")));
				 txtPLNCD.setText(nvlSTRVL(M_rstRSSET.getString("WR_PLNCD"),""));
				 txtARACD.setText(nvlSTRVL(M_rstRSSET.getString("WR_ARACD"),""));		
				
				 txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				 txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));	
				 
				 txtEQPID.setText(nvlSTRVL(M_rstRSSET.getString("WR_EQPID"),""));
				 txtTAGNO.setText(nvlSTRVL(M_rstRSSET.getString("WR_TAGNO"),""));
				
				 txtWRKTP.setText(nvlSTRVL(M_rstRSSET.getString("WR_WRKTP"),""));
				 txtWRKDS.setText(getCDTRN("MSTPMXXWRT"+txtWRKTP.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				 
				 txtPRICD.setText(nvlSTRVL(M_rstRSSET.getString("WR_PRICD"),""));
				 txtPRIDS.setText(getCDTRN("MSTPMXXPRT"+txtPRICD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				 
				 txtWKREQ.setText(nvlSTRVL(M_rstRSSET.getString("WR_WKREQ"),""));
				 txtHAZPR.setText(nvlSTRVL(M_rstRSSET.getString("WR_HAZPR"),""));
				 txtREQBY.setText(nvlSTRVL(M_rstRSSET.getString("WR_REQBY"),""));
				 
				 if(M_rstRSSET.getDate("WR_BDDAT")== null)
					 txtBDDAT.setText("");
				 else
					 txtBDDAT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_BDDAT")));
				 
				 txtBDTIM.setText(nvlSTRVL(M_rstRSSET.getString("WR_BDTIM"),""));
				 
				 if(M_rstRSSET.getDate("WR_STRDT")== null)
					 txtSTRDT.setText("");
				 else
					 txtSTRDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_STRDT")));
				 
				 txtSTRTM.setText(nvlSTRVL(M_rstRSSET.getString("WR_STRTM"),""));
				 
				 if(M_rstRSSET.getDate("WR_ENDDT")== null)
					 txtENDDT.setText("");
				 else
					 txtENDDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_ENDDT")));
				 
				 txtENDTM.setText(nvlSTRVL(M_rstRSSET.getString("WR_ENDTM"),""));
				 
				 if(M_rstRSSET.getDate("WR_HOMDT")== null)
					 txtHOMDT.setText("");
				 else
					 txtHOMDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_HOMDT")));
				 
				 txtHOMTM.setText(nvlSTRVL(M_rstRSSET.getString("WR_HOMTM"),""));
				 txtWRKDN.setText(nvlSTRVL(M_rstRSSET.getString("WR_WRKDN"),""));
				 txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("WR_REMDS"),""));
				 
				 if(M_rstRSSET.getDate("WR_RECDT")== null)
					 txtRECDT.setText("");
				 else
					 txtRECDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_RECDT")));
				 
				 if(M_rstRSSET.getDate("WR_REQDT")== null)
					 txtREQDT.setText("");
				 else
					 txtREQDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_REQDT")));
				 
				 if(M_rstRSSET.getDate("WR_REQTM")== null)
					 txtREQTM.setText("");
				 else
					 txtREQTM.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("WR_REQTM")));
				 
				 txtWRPNO.setText(nvlSTRVL(M_rstRSSET.getString("WR_WRPNO"),""));
				 
				 if(nvlSTRVL(M_rstRSSET.getString("WR_HWPFL"),"").equals("Y"))
						chkHWPFL.setSelected(true);
				 if(nvlSTRVL(M_rstRSSET.getString("WR_CSPFL"),"").equals("Y"))
						chkCSPFL.setSelected(true);
				 if(nvlSTRVL(M_rstRSSET.getString("WR_WHPFL"),"").equals("Y"))
						chkWHPFL.setSelected(true);
				 if(nvlSTRVL(M_rstRSSET.getString("WR_EXPFL"),"").equals("Y"))
						chkEXPFL.setSelected(true);
				 if(nvlSTRVL(M_rstRSSET.getString("WR_ELPFL"),"").equals("Y"))
						chkELPFL.setSelected(true);
				 
				 L_CNT++;
		     }	
			 if(M_rstRSSET != null)
			 {	
				 M_rstRSSET.close();			
			 }
		  }
	  }	
	  catch(Exception L_E)
	  {
		setMSG(L_E,"getDATA");
		setCursor(cl_dat.M_curDFSTS_pbst);
	  }
  }
   
  
  /** Method to validate data  */
  boolean vldDATA()
  {
	try
	{
		if(txtWORNO.getText().trim().length()== 0)
		{
		  setMSG("Enter Work Request Number",'E');
		  txtWORNO.requestFocus();
		  return false;
		}
		else if(txtWORDT.getText().trim().length()==0)
		{
	  		setMSG("Enter Work Request Date",'E');
	  		return false;
	  	}
		else if(txtTAGNO.getText().trim().length()==0)
		{
	  		setMSG("Enter Tag Number",'E');
	  		return false;
	  	}
		else if(txtPLNCD.getText().trim().length()==0)
		{
	  		setMSG("Enter Plant Code",'E');
	  		return false;
		}
		else if(txtEQPID.getText().toString().length()== 0)
		{
			setMSG("Enter Equipment ID..",'E');
			txtEQPID.requestFocus();
			return false;  
		}
		else if(txtARACD.getText().trim().length()==0)
		{
	  		setMSG("Enter Area Code",'E');
	  		return false;
	  	}
		else if(txtFMDCD.getText().trim().length()==0)
		{
	  		setMSG("Enter From Dept. Code",'E');
	  		return false;
		}
		else if(txtTODCD.getText().trim().length()==0)
		{
	  		setMSG("Enter To Dept. Code",'E');
	  		return false;
		}
		else if(txtWRKTP.getText().toString().length()== 0)
		{
			setMSG("Enter Work Type..",'E');
			return false;  
		}
		else if(txtPRICD.getText().trim().length()==0)
		{
	  		setMSG("Enter Priority Code",'E');
	  		return false;
		}
		
		else if(strDEPCD.toString().equals(txtFMDCD.getText().toString()))
		{	
			if(txtWKREQ.getText().trim().length()==0)
			{
				setMSG("Enter Problem observed/Work Required...",'E');
				return false;  		
			}	 
			else if(txtREQBY.getText().toString().length()== 0)
			{
				setMSG("Enter Requested by..",'E');
				return false;  
			}
			else if(txtHAZPR.getText().toString().length()== 0)
			{
				setMSG("Enter Hazard Present..",'E');
				return false;  
			}
			else if(txtREQDT.getText().toString().length()== 0)
			{
				setMSG("Enter Required By Date",'E');
				return false;  
			}
			else if(txtREQTM.getText().trim().length()==0)
			{
		  		setMSG("Enter Required By Time",'E');
		  		return false;
			}
		}
		else if(strDEPCD.toString().equals(txtTODCD.getText().toString()))
		{
			if(txtRECDT.getText().trim().length()==0)
			{
				setMSG("Enter Received Date...",'E');
				return false;  		
			}	 
			else if(txtRECBY.getText().toString().length()== 0)
			{
				setMSG("Enter Received by..",'E');
				return false;  
			}
			else if(txtMSSNO.getText().toString().length()== 0)
			{
				setMSG("Enter Maintenance Specification Sheet..",'E');
				return false;  
			}
			else if(txtCNTOR.getText().toString().length()== 0)
			{
				setMSG("Enter Contractor",'E');
				return false;  
			}
			else if(txtWRKDN.getText().trim().length()==0)
			{
		  		setMSG("Enter Work Done",'E');
		  		return false;
			}
			else if(txtREMDS.getText().trim().length()==0)
			{
		  		setMSG("Enter Remedy Description",'E');
		  		return false;
			}
			else if(txtWORST.getText().trim().length()==0)
			{
		  		setMSG("Enter Work Status",'E');
		  		return false;
			}
			else if(txtBDDAT.getText().trim().length()==0)
			{
		  		setMSG("Enter Break Down Date",'E');
		  		return false;
			}
			else if(txtBDTIM.getText().trim().length()==0)
			{
		  		setMSG("Enter Break Down Time",'E');
		  		return false;
			}
			else if(txtSTRDT.getText().trim().length()==0)
			{
		  		setMSG("Enter work start date",'E');
		  		return false;
			}
			else if(txtSTRTM.getText().trim().length()==0)
			{
		  		setMSG("Enter work start time",'E');
		  		return false;
			}
			else if(txtPRDLS.getText().trim().length()==0)
			{
		  		setMSG("Enter production loss",'E');
		  		return false;
			}
			else if(txtHOMDT.getText().trim().length()==0)
			{
		  		setMSG("Enter handed over date",'E');
		  		return false;
			}
			else if(txtHOMTM.getText().trim().length()==0)
			{
		  		setMSG("Enter Handed over time",'E');
		  		return false;
			}
			else if(txtENDDT.getText().trim().length()==0)
			{
		  		setMSG("Enter Work completed date",'E');
		  		return false;
			}
			else if(txtENDTM.getText().trim().length()==0)
			{
		  		setMSG("Enter Work completed time",'E');
		  		return false;
			}
			else if(txtWRPNO.getText().trim().length()==0)
			{
		  		setMSG("Enter work permit number",'E');
		  		return false;
			}
			else if(txtMATCD.getText().trim().length()==0)
			{
		  		setMSG("Enter Material code",'E');
		  		return false;
			}
			else if(txtUOMCD.getText().trim().length()==0)
			{
		  		setMSG("Enter unit of measure",'E');
		  		return false;
			}
			else if(txtSTKQT.getText().trim().length()==0)
			{
		  		setMSG("Enter stock",'E');
		  		return false;
			}
			else if(txtFLRCD.getText().trim().length()==0)
			{
		  		setMSG("Enter failure code",'E');
		  		return false;
			}
			else if(txtRSNCD.getText().trim().length()==0)
			{
		  		setMSG("Enter reason code",'E');
		  		return false;
			} 
		}
			 
	}	  
  	catch(Exception L_EX)
  	{
  	  setMSG(L_EX,"This is vldDATA");
  	}	
  	return true;
  }
 
  /**save record after click on save button**/
  void exeSAVE()
  {
    int P_intROWNO=0;
    try
    {
    	if(!vldDATA())
 	  	{
 		  cl_dat.M_btnSAVE_pbst.setEnabled(true);
 		  return;
 	  	} 
    	if(strDEPCD.toString().equals(txtFMDCD.getText().toString()))
    	{
	        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
	   	    {
	           exeMODREC();	
	   	    }
	     	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
	   	    {
	     	  exeDELREC();	
	   	    }    	   	        			
	      	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
	        {
	      	  exeADDREC(); 
	        }	
	      	
    	}
    	if(strDEPCD.toString().equals(txtTODCD.getText().toString()))
    	{ 
	        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
	   	    {
	           exeMODREC1();	
	   	    }
	     	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
	   	    {
	     	  exeDELREC1();	
	   	    }    	   	        			
	      	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
	        {
	      	  exeADDREC1(); 
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
   	catch(Exception L_E)
	{
   		setMSG(L_E,"exeSAVE");
	}
  }
  
  /** Method to insert data  */
  private void exeADDREC()
  { 
	  try
	  {
		    M_strSQLQRY =" insert into PM_WRMST(WR_WORNO,WR_WORDT,WR_PLNCD,WR_ARACD,WR_EQPID,WR_TAGNO,WR_WRKTP,WR_PRICD,WR_WKREQ,WR_HAZPR,WR_REQBY,WR_REQDT,WR_REQTM,WR_LUPDT,WR_LUSBY,WR_STSFL,WR_TRNFL,WR_CMPCD,WR_SBSCD)";
			M_strSQLQRY += " values (";
			M_strSQLQRY += "'"+txtWORNO.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtWORDT.getText()))+"',";
			M_strSQLQRY += "'"+txtPLNCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtARACD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtEQPID.getText().toString()+"',";
			M_strSQLQRY += "'"+txtTAGNO.getText().toString()+"',";
			//M_strSQLQRY += "'"+txtFMDCD.getText().toString()+"',";
			//M_strSQLQRY += "'"+txtTODCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtWRKTP.getText().toString()+"',";
			M_strSQLQRY += "'"+txtPRICD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtWKREQ.getText().toString()+"',";
			M_strSQLQRY += "'"+txtHAZPR.getText().toString()+"',";
			M_strSQLQRY += "'"+txtREQBY.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtREQDT.getText()))+"',";
			M_strSQLQRY += "'"+txtREQTM.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'1',";
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'01',";
			M_strSQLQRY += "'"+M_strSBSCD+"')";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			System.out.println("insert"+M_strSQLQRY);
		
	  }
	  catch(Exception L_EX)
	  {
		  cl_dat.M_flgLCUPD_pbst=false;
		  setMSG(L_EX,"exeADDREC()"); 
	  }
  }
  
  /** Method to delete data  */
  private void exeDELREC()
  {
	  try
	  {
		    M_strSQLQRY="update PM_WRMST set";
			M_strSQLQRY+=" WR_STSFL = 'X'";
			M_strSQLQRY+=" where WR_WORNO ='"+txtWORNO.getText()+"'";
			M_strSQLQRY+=" and WR_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
			System.out.println(">>>delete>>"+M_strSQLQRY);
		
	  }  
	  catch(Exception L_EX)
	  {
		  setMSG(L_EX,"exeDELREC()");		
	  }
  }
  
  /** Method to modify data  */
  private void exeMODREC()
  {
	  try
	  {
		    M_strSQLQRY = " Update PM_WRMST set";
			M_strSQLQRY +=" WR_WORNO ='"+txtWORNO.getText().toString()+"',";
			M_strSQLQRY +=" WR_WORDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtWORDT.getText().toString()))+"',";
			M_strSQLQRY +=" WR_PLNCD ='"+txtPLNCD.getText().toString()+"',";
			M_strSQLQRY +=" WR_ARACD ='"+txtARACD.getText().toString()+"',";
			M_strSQLQRY +=" WR_EQPID ='"+txtEQPID.getText().toString()+"',";
			//M_strSQLQRY +="   		 ='"+txtFMDCD.getText().toString()+"',";
			//M_strSQLQRY +="   		 ='"+txtTODCD.getText().toString()+"',";
			M_strSQLQRY +=" WR_TAGNO ='"+txtTAGNO.getText().toString()+"',";
			M_strSQLQRY +=" WR_WRKTP ='"+txtWRKTP.getText().toString()+"',";
			M_strSQLQRY +=" WR_PRICD ='"+txtPRICD.getText().toString()+"',";
			M_strSQLQRY +=" WR_WKREQ ='"+txtWKREQ.getText().toString()+"',";
			M_strSQLQRY +=" WR_HAZPR ='"+txtHAZPR.getText().toString()+"',";
			M_strSQLQRY +=" WR_REQBY ='"+txtREQBY.getText().toString()+"',";
			M_strSQLQRY +=" WR_REQDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtREQDT.getText()))+"',";
			M_strSQLQRY +=" WR_REQTM ='"+txtREQTM.getText().toString()+"',";
			M_strSQLQRY +=" WR_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY +=" WR_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY +=" where WR_WORNO ='"+txtWORNO.getText()+"'";
			M_strSQLQRY +=" and WR_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
			System.out.println(">>>update>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		
	  }
	  catch(Exception L_EX)
	  {
		  	setMSG(L_EX,"exeMODREC()");
	  }
  }
  

  /** Method to insert data  */
  private void exeADDREC1()
  { 
	  try
	  {
		    M_strSQLQRY =" insert into PM_WRMST(WR_WORNO,WR_WORDT,WR_PLNCD,WR_ARACD,WR_EQPID,WR_TAGNO,WR_WRKTP,WR_PRICD,WR_RECDT,WR_WRKDN,WR_REMDS,WR_BDDAT,WR_BDTIM,WR_STRDT,WR_STRTM,WR_HOMDT,WR_HOMTM,WR_ENDDT,WR_ENDTM,WR_WRPNO,WR_CSPFL,WR_WHPFL,WR_HWPFL,WR_EXPFL,WR_ELPFL,WR_LUPDT,WR_LUSBY,WR_STSFL,WR_TRNFL,WR_CMPCD,WR_SBSCD)";
			M_strSQLQRY += " values (";
			M_strSQLQRY += "'"+txtWORNO.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtWORDT.getText()))+"',";
			M_strSQLQRY += "'"+txtPLNCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtARACD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtEQPID.getText().toString()+"',";
			M_strSQLQRY += "'"+txtTAGNO.getText().toString()+"',";
			//M_strSQLQRY += "'"+txtFMDCD.getText().toString()+"',";
			//M_strSQLQRY += "'"+txtTODCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtWRKTP.getText().toString()+"',";
			M_strSQLQRY += "'"+txtPRICD.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRECDT.getText()))+"',";  
			/*M_strSQLQRY += "'"+txtRECBY.getText().toString()+"',";
			M_strSQLQRY += "'"+txtMSSNO.getText().toString()+"',";
			M_strSQLQRY += "'"+txtCNTOR.getText().toString()+"',";**/
			M_strSQLQRY += "'"+txtWRKDN.getText().toString()+"',";
			M_strSQLQRY += "'"+txtREMDS.getText().toString()+"',";
			//M_strSQLQRY += "'"+txtWORST.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBDDAT.getText()))+"',"; 
			M_strSQLQRY += "'"+txtBDTIM.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"',"; 
			M_strSQLQRY += "'"+txtSTRTM.getText().toString()+"',";
			M_strSQLQRY += "'"+txtPRDLS.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtHOMDT.getText()))+"',"; 
			M_strSQLQRY += "'"+txtHOMTM.getText().toString()+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"',";
			M_strSQLQRY += "'"+txtENDTM.getText().toString()+"',";
			M_strSQLQRY += "'"+txtWRPNO.getText().toString()+"',";
			M_strSQLQRY += chkCSPFL.isSelected() ? "'Y'," : "'N',";
			M_strSQLQRY += chkWHPFL.isSelected() ? "'Y'," : "'N',";
			M_strSQLQRY += chkHWPFL.isSelected() ? "'Y'," : "'N',";
			M_strSQLQRY += chkEXPFL.isSelected() ? "'Y'," : "'N',";
			M_strSQLQRY += chkELPFL.isSelected() ? "'Y'," : "'N',";
			/*M_strSQLQRY += "'"+txtMATCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtUOMCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtSTKQT.getText().toString()+"',";
			M_strSQLQRY += "'"+txtFLRCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtRSNCD.getText().toString()+"',";**/
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'1',";
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'01',";
			M_strSQLQRY += "'"+M_strSBSCD+"')";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			//System.out.println("insert"+M_strSQLQRY);
		
	  }
	  catch(Exception L_EX)
	  {
		  cl_dat.M_flgLCUPD_pbst=false;
		  setMSG(L_EX,"exeADDREC()"); 
	  }
  }
  
  /** Method to delete data  */
  private void exeDELREC1()
  {
	  try
	  {
		    M_strSQLQRY="update PM_WRMST set";
			M_strSQLQRY+=" WR_STSFL = 'X'";
			M_strSQLQRY+=" where WR_WORNO ='"+txtWORNO.getText()+"'";
			M_strSQLQRY+=" and WR_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
			//System.out.println(">>>delete>>"+M_strSQLQRY);
		
	  }  
	  catch(Exception L_EX)
	  {
		  setMSG(L_EX,"exeDELREC()");		
	  }
  }
  
  /** Method to modify data  */
  private void exeMODREC1()
  {
	  try
	  {
		    M_strSQLQRY = " Update PM_WRMST set";
			M_strSQLQRY +=" WR_WORNO ='"+txtWORNO.getText().toString()+"',";
			M_strSQLQRY +=" WR_WORDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtWORDT.getText().toString()))+"',";
			M_strSQLQRY +=" WR_PLNCD ='"+txtPLNCD.getText().toString()+"',";
			M_strSQLQRY +=" WR_ARACD ='"+txtARACD.getText().toString()+"',";
			M_strSQLQRY +=" WR_EQPID ='"+txtEQPID.getText().toString()+"',";
			//M_strSQLQRY +="   		 ='"+txtFMDCD.getText().toString()+"',";
			//M_strSQLQRY +="   		 ='"+txtTODCD.getText().toString()+"',";
			M_strSQLQRY +=" WR_TAGNO ='"+txtTAGNO.getText().toString()+"',";
			M_strSQLQRY +=" WR_WRKTP ='"+txtWRKTP.getText().toString()+"',";
			M_strSQLQRY +=" WR_PRICD ='"+txtPRICD.getText().toString()+"',";
			M_strSQLQRY +=" WR_RECDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtRECDT.getText()))+"',";  
			//M_strSQLQRY +="'"+txtRECBY.getText().toString()+"',";
			//M_strSQLQRY +="'"+txtMSSNO.getText().toString()+"',";
			//M_strSQLQRY +="'"+txtCNTOR.getText().toString()+"',";
			M_strSQLQRY +=" WR_WRKDN ='"+txtWRKDN.getText().toString()+"',";
			M_strSQLQRY +=" WR_REMDS ='"+txtREMDS.getText().toString()+"',";
			//M_strSQLQRY += "'"+txtWORST.getText().toString()+"',";
			M_strSQLQRY +=" WR_BDDAT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtBDDAT.getText()))+"',"; 
			M_strSQLQRY +=" WR_BDTIM ='"+txtBDTIM.getText().toString()+"',";
			M_strSQLQRY +=" WR_STRDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"',"; 
			M_strSQLQRY +=" WR_STRTM ='"+txtSTRTM.getText().toString()+"',";
			//M_strSQLQRY += "'"+txtPRDLS.getText().toString()+"',";
			M_strSQLQRY +=" WR_HOMDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtHOMDT.getText()))+"',"; 
			M_strSQLQRY +=" WR_HOMTM ='"+txtHOMTM.getText().toString()+"',";
			M_strSQLQRY +=" WR_ENDDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"',";
			M_strSQLQRY +=" WR_ENDTM ='"+txtENDTM.getText().toString()+"',";
			M_strSQLQRY +=" WR_WRPNO ='"+txtWRPNO.getText().toString()+"',";
			M_strSQLQRY += chkCSPFL.isSelected() ? "WR_CSPFL = 'Y'," : "WR_CSPFL = 'N',";
			M_strSQLQRY += chkWHPFL.isSelected() ? "WR_WHPFL = 'Y'," : "WR_WHPFL = 'N',";
			M_strSQLQRY += chkHWPFL.isSelected() ? "WR_HWPFL = 'Y'," : "WR_HWPFL = 'N',";
			M_strSQLQRY += chkEXPFL.isSelected() ? "WR_EXPFL = 'Y'," : "WR_EXPFL = 'N',";
			M_strSQLQRY += chkELPFL.isSelected() ? "WR_ELPFL = 'Y'," : "WR_ELPFL = 'N',";
			/*M_strSQLQRY += "'"+txtMATCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtUOMCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtSTKQT.getText().toString()+"',";
			M_strSQLQRY += "'"+txtFLRCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtRSNCD.getText().toString()+"',";**/
			M_strSQLQRY +=" WR_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY +=" WR_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY +=" where WR_WORNO ='"+txtWORNO.getText()+"'";
			M_strSQLQRY +=" and WR_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'";
			//System.out.println(">>>update>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		
	  }
	  catch(Exception L_EX)
	  {
		  	setMSG(L_EX,"exeMODREC()");
	  }
  }
  
  
   
    /**Verify table for valid data entry***/  
   private class TBLINPVF extends TableInputVerifier
   {
      public boolean verify(int P_intROWID,int P_intCOLID)
      { 
	      	int i;
	    	String L_strLVDT =""; 
	  		try
	  		{
	  		
	  			if(strDEPCD.toString().equals(txtTODCD.getText().toString()))
	  	    	{	
		  			if(getSource()==tblMATCD)
		  			{
	  			       if(P_intCOLID == TB1_MATCD)
	  			       {   
  					    L_strLVDT = tblMATCD.getValueAt(P_intROWID,TB1_MATCD).toString();
  					    	
  					     if(tblMATCD.getValueAt(P_intROWID,TB1_CHKFL).toString().equals("true"))
  					     {	 
  					    	for(i=0;i<tblMATCD.getRowCount();i++)
  					    	{	
							    if(tblMATCD.getValueAt(i,TB1_MATCD).toString().length()>0 && i!=P_intROWID)
							    {	  
							      if(L_strLVDT.equals(tblMATCD.getValueAt(i,TB1_MATCD).toString()))
							      {
							    	setMSG("Enter material code Already Exist",'E');
							    	return false;
							      }
							      setMSG("",'N');
							    } 
  					    	}
	  					    if(tblMATCD.getValueAt(P_intROWID,TB1_MATCD).toString().length()<10)
	  						{	
	  							setMSG("Material code should be in ten digits",'E');	
	  							return false;
	  						}	
  					    	setMSG("",'N');
  					     }	
	  			       }
	  			       
	  			       if(P_intCOLID == TB1_UOMCD)
	  			       { 
	  			    	 tblMATCD.setValueAt(tblMATCD.getValueAt(P_intROWID,TB1_UOMCD).toString().replace("'","`"),P_intROWID,TB1_UOMCD);
	  			    	 tblMATCD.setValueAt(tblMATCD.getValueAt(P_intROWID,TB1_UOMCD).toString().toUpperCase(),P_intROWID,TB1_UOMCD);  
	  			       }  
		  			}
		  				
		  			/*if(getSource()==tblCODES)
		  			{
		  				if(P_intCOLID == TB2_FLRCD)
					    {
		  					 if(((JCheckBox) tblCODES.cmpEDITR[TB1_CHKFL]).isSelected())
						     {
		  						if(!hstCDTRN.containsKey("MSTPMXXFLR"+txtFLRCD.getText().toString().trim()))
		  						{
		  							setMSG("Enter valid Failure Code ",'E');
		  							return false;
		  						}
		  						else
		  							setMSG("",'N');
		  					}
					    }
		  				
		  				if(P_intCOLID == TB2_RSNCD)
					    {
		  					if(tblCODES.getValueAt(P_intROWID,TB2_CHKFL).toString().equals("true"))
						    {	 	
		  						if(!hstCDTRN.containsKey("MSTPMXXRSN"+txtRSNCD.getText().toString().trim()))
		  						{	
		  							setMSG("Enter valid Reason Code ",'E');
		  							return false;
		  						}
						    }	
		  					else
		  						setMSG("",'N');
					    }
		  			}**/
	  	    	}
	  		}	
	  	  	catch(Exception L_E)
	  		{
	  	  		setMSG(L_E,"Table Verifier");
	  		}
	  	  	return true;
      }
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
		{setMSG(L_EX,"getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);}
		return " ";
	} 
	
	/** One time data capturing for specified codes from CO_CDTRN
	 * into the Hash Table
	 */
   private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
   {
		String L_strSQLQRY = "";
      try
      {
	        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")   "+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
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
               
                  LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
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
					{setMSG(LP_CDTRN_KEY+" not found in CO_CDTRN hash table",'E'); return " ";}
	           if (LP_FLDNM.equals("CMT_CODCD"))
	                   return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
	           else if (LP_FLDNM.equals("CMT_CODDS"))
	                   return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS]; 
	   }
	   catch (Exception L_EX)
	   {
			setMSG(L_EX,"getCDTRN");
	   }
	   return "";
   }
  
  
  
  /**
	 * Method to clear data*/
	void clrCOMP()
	{
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) 
				txtWORNO.setText("");	
			
    		txtWORDT.setText("");
    		txtPLNCD.setText("");
    		txtARACD.setText("");	
    		txtPLNDS.setText("");
    		txtARADS.setText("");
    		txtEQPID.setText("");	
    		txtTAGNO.setText("");
    		txtFMDCD.setText("");
    		txtTODCD.setText("");
    		txtFMDNM.setText("");
    		txtTODNM.setText("");
    		txtWRKTP.setText("");
    		txtWRKDS.setText("");
    		txtPRICD.setText("");
    		txtPRIDS.setText("");
    		txtWKREQ.setText("");
    		txtHAZPR.setText("");
    		txtREQBY.setText("");
    		txtREQDT.setText("");
    		txtREQTM.setText("");
    		txtHOMDT.setText("");
    		txtHOMTM.setText("");
    		txtWRPNO.setText("");
    		txtBDDAT.setText("");
    		txtBDTIM.setText("");
    		txtSTRDT.setText("");
    		txtSTRTM.setText("");
    		txtENDDT.setText("");
    		txtENDTM.setText(""); 
    		txtRECDT.setText("");
    		txtRECBY.setText("");
    		txtWORST.setText("");
    		txtPRDLS.setText("");
    		txtWRKDN.setText("");
    		txtREMDS.setText("");
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
  
  /**Verify fields for valid data entry***/  
    class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;
				  if(input == txtWORNO)
				  {	
					 try
					 {
						if(txtWORNO.getText().length()==0)
						{
							setMSG("Enter Work Order No..",'E');
							txtWORNO.requestFocus();
							return false;
						}
						else if(txtWORNO.getText().length()>0)
							txtWORNO.setText(txtWORNO.getText().toUpperCase());
				
						String L_strTAGNO =txtWORNO.getText().toString();
						M_strSQLQRY=" SELECT WR_WORNO,WR_PLNCD,WR_ARACD,WR_EQPID,WR_TAGNO,WR_WRKTP,WR_PRICD,WR_WKREQ,WR_HAZPR from PM_WRMST where WR_WORNO='"+txtWORNO.getText().toUpperCase()+"'";
						M_strSQLQRY+= " AND WR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(WR_STSFL,'')<>'X'";
						//System.out.println("INPVF TAGNO"+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
						  if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						  {	
							getDATA();
						  }
						   
						  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						  {	  
							if(L_strTAGNO.equals(M_rstRSSET.getString("WR_WORNO")))
							{
								setMSG("This Work No. Already exist",'E');
								txtWORNO.requestFocus();
								return false;
							}
						  }		
						}
						else
						{
						  if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						  {	  
							setMSG("Enter valid Work Order No ",'E');
							txtWORNO.requestFocus();
							return false;
						  }	
						}
						M_rstRSSET.close();
					}	
					catch(Exception e)
					{
						setMSG(e,"error in Tag No InputVerifier  ");
					}
				}
				else if(input == txtTAGNO)
				{	
					 try
					 {
						if(txtTAGNO.getText().length()==0)
						{
							setMSG("Enter Tag No..",'E');
							txtTAGNO.requestFocus();
							return false;
						}
						if(txtTAGNO.getText().length()>0)
						{
							M_strSQLQRY=" SELECT distinct TG_TAGNO,TG_TAGDS,TG_ARACD,TG_PLNCD from PM_TGMST where TG_TAGNO='"+txtTAGNO.getText().toUpperCase()+"'";
							M_strSQLQRY+= " AND TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
							System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtTAGNO.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGNO"),""));
								txtTAGDS.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGDS"),""));
								txtPLNCD.setText(nvlSTRVL(M_rstRSSET.getString("TG_PLNCD"),""));
								txtARACD.setText(nvlSTRVL(M_rstRSSET.getString("TG_ARACD"),""));
								txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
								txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
							}
							else
							{
								setMSG("Enter valid Tag No ",'E');
								txtTAGNO.requestFocus();
								return false;
							}
							M_rstRSSET.close();
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Tag No InputVerifier  ");
					}
					if(txtTAGNO.getText().length()>0)
						txtTAGNO.setText(txtTAGNO.getText().toUpperCase());
				}  
				else if(input == txtEQPID)
				{	
					try
					{
						if(txtEQPID.getText().length()>0)
						{
							txtEQPID.setText(txtEQPID.getText().toUpperCase());
						
							/**Verification to Eqpt Id of Eqpt Detail**/
							M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS from PM_EQMST  ";
							M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EQ_STSFL,'')<>'X' AND EQ_EQPID ='"+txtEQPID.getText().toUpperCase()+"'";
							//if(txtTAGNO.getText().length()>0)
							//M_strSQLQRY+=" and EQ_TAGNO='"+txtTAGNO.getText()+"' ";
							System.out.println("M_strSQLQRY>>"+M_strSQLQRY);	
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);	
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtEQPID.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQPID"),""));
								txtEQPDS.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQPDS"),""));
							}
							else
							{
								setMSG("Enter valid Equipment Id ",'E');
								txtEQPID.requestFocus();
								return false;
							}
							M_rstRSSET.close();
						}	
					}
					catch(Exception e)
					{
						 setMSG(e,"error in Equipment id  InputVerifier ");
					}
				}	
				else if(input == txtFMDCD)
				{
					
					if(!hstDPTCD.containsKey(txtFMDCD.getText().toString().trim()))
					{
						txtFMDNM.setText("");
						setMSG("Enter Valid Department Code",'E');
						return false;	   
					}
					else
					{
						txtFMDNM.setText(hstDPTCD.get(txtFMDCD.getText().toString().trim()));
						setMSG("",'N');	
					}
				}	
				else if(input == txtTODCD)
				{
					
					if(!hstDPTCD.containsKey(txtTODCD.getText().toString().trim()))
					{
						txtTODNM.setText("");
						setMSG("Enter Valid Department Code",'E');
						return false;   
					}
					else
					{
						txtTODNM.setText(hstDPTCD.get(txtTODCD.getText().toString().trim()));
						setMSG("",'N');	
					}
				}  
				else if(input == txtPLNCD)
				{	
					try
					{
						if(!hstCDTRN.containsKey("MSTPMXXPLN"+txtPLNCD.getText().toString().trim()))
						{
							setMSG("Enter valid Plant Code ",'E');
							return false;	
						}
						else
							txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
					}
					catch(Exception e)
					{
						setMSG(e,"error in Plant Cod InputVerifier ");
					}

				}
				else if(input == txtARACD)
				{	
					try
					{
						if(!hstCDTRN.containsKey("MSTPMXXARA"+txtARACD.getText().toString().trim()))
						{	
							setMSG("Enter valid Area Code ",'E');
						    return false;
						}   
						else
							txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));	
					}
					catch(Exception e)
					{
						setMSG(e,"error in Area Code InputVerifier");
					}

				}
				else if(input == txtWRKTP)
				{	
					try
					{
						if(!hstCDTRN.containsKey("MSTPMXXWRT"+txtWRKTP.getText().toString().trim()))
						{
							setMSG("Enter valid Work Type Code ",'E');
							return false;	
						}
						else
							txtWRKDS.setText(getCDTRN("MSTPMXXWRT"+txtWRKTP.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
					}
					catch(Exception e)
					{
						setMSG(e,"error in Work Type Code InputVerifier ");
					}

				}
				else if(input ==  txtPRICD)
				{	
					try
					{
						if(!hstCDTRN.containsKey("MSTPMXXPRT"+txtPRICD.getText().toString().trim()))
						{	
							setMSG("Enter valid Priority Code ",'E');
						    return false;
						}   
						else
							txtPRIDS.setText(getCDTRN("MSTPMXXPRT"+txtPRICD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));	
					}
					catch(Exception e)
					{
						setMSG(e,"error in Priority Code InputVerifier");
					}

				}  
				else if(input == txtWORDT)
				{	
					if(txtWORDT.getText().toString().length()== 0)
					{
						setMSG("Enter  Workorder Request Date..",'E');
						txtWORDT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtWORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("Workorder Request date can't be greater than current date",'E');
						txtWORDT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtWORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtREQDT.getText().toString()))>0)
					{
						setMSG("Workorder Request date can't be greater than Required By date",'E');
						txtWORDT.requestFocus();
						return false;
					}
				} 
				    
			    else if(input == txtREQBY)
				{
					M_strSQLQRY = "select EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U'  and EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' = '"+txtREQBY.getText()+"'";
					if(txtFMDCD.getText().length()>0)
						M_strSQLQRY+=" and EP_DPTCD='"+txtFMDCD.getText()+"'";	
					System.out.println("INPVF EMPNM : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
					if(M_rstRSSET!=null)
					{
						while(M_rstRSSET.next())
						{	
							txtREQBY.setText(M_rstRSSET.getString("EP_EMPNM"));	
							setMSG("",'N');
						}	
						if(M_rstRSSET != null)
							 M_rstRSSET.close();
					}	
					else
					{
						txtREQBY.setText("");	
						setMSG("Enter Valid Employee Name",'E');
						return false;
					}
					
				}    
				else if(input == txtREQDT)
				{	
					if(txtREQDT.getText().toString().length()== 0)
					{
						setMSG("Enter Required By Date..",'E');
						txtREQDT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtWORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtREQDT.getText().toString()))>0)
					{
						setMSG("Requied By Date can't be smaller than Workorder Request date..",'E');
						txtREQDT.requestFocus();
						return false;
					}
				}     
				else if(input == txtHAZPR)
				{
					if(txtHAZPR.getText().length()== 0)
					{	
						setMSG("Enter Hazard Present",'E');
						return false;
					}	
					else if(txtHAZPR.getText().length()>0)
						txtHAZPR.setText(txtHAZPR.getText().toUpperCase());
				} 
				   
			    else if(input == txtRECBY)
				{
					M_strSQLQRY = "select EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U'  and EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' = '"+txtRECBY.getText()+"'";
					if(txtFMDCD.getText().length()>0)
						M_strSQLQRY+=" and EP_DPTCD='"+txtFMDCD.getText()+"'";	
					//System.out.println("INPVF EMPNM : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
					if(M_rstRSSET!=null)
					{
						while(M_rstRSSET.next())
						{	
							txtRECBY.setText(M_rstRSSET.getString("EP_EMPNM"));	
							setMSG("",'N');
						}	
						if(M_rstRSSET != null)
							 M_rstRSSET.close();
					}	
					else
					{
						txtRECBY.setText("");	
						setMSG("Enter Valid Employee Name",'E');
						return false;
					}
				}   
				else if(input == txtRECDT)
				{	
					if(txtRECDT.getText().toString().length()== 0)
					{
						setMSG("Enter Required By Date..",'E');
						txtRECDT.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse(txtWORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtRECDT.getText().toString()))>0)
					{
						setMSG("Requied By Date can't be smaller than Workorder Request date..",'E');
						txtREQDT.requestFocus();
						return false;
					}
				}
				else if(input == txtSTRDT)
				{	
					if(txtSTRDT.getText().toString().length()== 0)
					{
						setMSG("Enter Start Date..",'E');
						txtSTRDT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtSTRDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("Start Date Should Not Be Grater Than Todays Date..",'E');
						txtSTRDT.requestFocus();
						return false;
					}
				}	   
				else if(input == txtENDDT)
				{
					 if(txtENDDT.getText().toString().length()== 0)
					 {
						 setMSG("Enter end Date..",'E');
						 txtENDDT.requestFocus();
						 return false;
					 }
					 if(M_fmtLCDAT.parse(txtENDDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					 {
						 setMSG("End Date can not be greater Than Todays Date..",'E');
						 txtENDDT.requestFocus();
						 return false;
					 }
					 if(M_fmtLCDAT.parse(txtENDDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtSTRDT.getText().toString()))<0)
					 {
						 setMSG("End Date can not be greater than Or equal to start Date..",'E');
						 txtENDDT.requestFocus();
						 return false;
					 }
				} 
				else if(input == txtWRPNO)
				{	
					 try
					 {
						if(txtWRPNO.getText().length()==0)
						{
							setMSG("Enter Work permit No...",'E');
							txtWRPNO.requestFocus();
							return false;
						}
						String L_strTAGNO =txtWRPNO.getText().toString();
						M_strSQLQRY=" SELECT distinct WR_WRPNO from PM_WRMST where WR_WORNO='"+txtWRPNO.getText().toUpperCase()+"'";
						M_strSQLQRY+= " AND WR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(WR_STSFL,'')<>'X'";
						//System.out.println("INPVF WRPNO"+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{ 
							  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							  {	  
								 if(L_strTAGNO.equals(M_rstRSSET.getString("WR_WRPNO")))
								 {
									 setMSG("This Work permit No. Already exist",'E');
									 txtWRPNO.requestFocus();
									 return false;
								 }
							  }		
						}
						else
						{
							  if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							  {	  
								  setMSG("Enter valid Work permit No ",'E');
								  txtWRPNO.requestFocus();
								  return false;
							  }	
						}
						M_rstRSSET.close();
					}
					catch(Exception e)
					{
						setMSG(e,"error in work permit No InputVerifier  ");
					}
					if(txtWRPNO.getText().length()>0)
						txtWRPNO.setText(txtWRPNO.getText().toUpperCase());
				}      
				else if(input == txtMSSNO)
				{	
					try
					{	
						if(txtMSSNO.getText().length()>0)
						{
							txtMSSNO.setText(txtMSSNO.getText().toUpperCase());
							M_strSQLQRY=" SELECT distinct MS_MSSNO,MS_MSSDS from PM_MSMST where MS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MS_STSFL,'')<>'X'";
							M_strSQLQRY+= " AND MS_MSSNO ='"+txtMSSNO.getText().toUpperCase()+"' ";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtMSSNO.setText(txtMSSNO.getText());
								txtMSSDS.setText(nvlSTRVL(M_rstRSSET.getString("MS_MSSDS"),""));
							}	
							else
							{
								setMSG("Enter valid Mss No ",'E');
								txtMSSNO.requestFocus();
								return false;
							}
							M_rstRSSET.close();
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Mss No InputVerifier  ");
					}
				}
				else if(input ==  txtRSNCD)
				{	
					try
					{
						if(!hstCDTRN.containsKey("MSTPMXXRSN"+txtRSNCD.getText().toString().trim()))
						{	
							setMSG("Enter valid Reason Code ",'E');
						    return false;
						}   	
					}
					catch(Exception e)
					{
						setMSG(e,"error in Reason Code InputVerifier");
					}

				}       	  
				  
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"class INPVF");		
		}
		return true;
	   }
   }
}


 