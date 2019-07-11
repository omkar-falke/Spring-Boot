/**System Name:Plant Maintenance.
 
Program Name:Equipment

Purpose : This module used for accepting the Equipment Detail ,Equipment's Technical Specifications & Trouble Shooting Guide.
 
Source Directory: f:\source\splerp3\pm_tetsp.java                         
Executable Directory: F:\exec\splerp3\pm_tetsp.class

 
List of tables used:
Table Name			Primary key											Operation done
															Insert	Update	   Query    Delete	
------------------------------------------------------------------------------------------------------------------------
PM_EQMST			EQ_EQPID,EQ_CMPCD                         /	      /          /        /
PM_ESTRN			ES_SRLNO,ES_EQPID,ES_CMPCD                /	      /          /        /
PM_TSMST			TS_TSCNO,TS_TSFNO,TS_TSGNO,TS_CMPCD       /       /          /        /
PM_TGMST			TG_TAGNO,TG_CMPCD                                            /
PM_ATMST			AT_ATIND,AT_SRLNO,AT_EQSTP,AT_EQMTP,AT_CMPCD                 /                          
-----------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on Equipment-Base Detail  screen:
Field Name		Column Name		Table name		Type/Size		Description
-----------------------------------------------------------------------------------------------------------------------
txtTAGNO		EQ_TAGNO     	PM_EQMST		VARCHAR(15),    Tag No 
txtTAGDS		TG_TAGDS     	PM_TGMST		VARCHAR(50),    Tag Description                     
txtEQPID		EQ_EQPID    	PM_EQMST		VARCHAR(15),    Equipment ID                    
txtEQPDS		EQ_EQPDS    	PM_EQMST		VARCHAR(50),    Equipment Description                    
txtEQMTP		EQ_EQMTP    	PM_EQMST		VARCHAR(3),     Equipment main type	 
txtEQMTP_DS		CMT_CODDS    	CO_CDTRN		VARCHAR(50),    Equipment main type	Description                    
txtEQSTP		EQ_EQSTP    	PM_EQMST		VARCHAR(3),     Equipment sub type 
txtEQSTP_DS		CMT_CODDS    	CO_CDTRN		VARCHAR(50),    Equipment sub type Description                  
txtDPTCD		EQ_DPTCD     	PM_EQMST		VARCHAR(3),     Department code  
txtDPTDS		CMT_CODDS     	CO_CDTRN		VARCHAR(20),    Department Name                
txtPLNCD		EQ_PLNCD     	PM_EQMST		VARCHAR(3),     Plant code  
txtPLNDS		CMT_CODDS     	CO_CDTRN		VARCHAR(15),    Plant Description                  
txtARACD		EQ_ARACD     	PM_EQMST		VARCHAR(3),     Area code  
txtARADS		CMT_CODDS     	CO_CDTRN		VARCHAR(15),    Area Description  
txtCRICD		TG_CRICD     	PM_TGMST		VARCHAR(3),     Criticality code 
txtCRIDS		CMT_CODDS     	CO_CDTRN		VARCHAR(15),    Criticality Description                     
txtTSGNO_EQ		EQ_TSGNO    	PM_EQMST		VARCHAR(8),     T.S.G NO   
txtTSGDS_EQ		TG_TSGDS    	PM_TGMST		VARCHAR(8),     T.S.G Description                 
txtDRGNO		EQ_DRGNO     	PM_EQMST		VARCHAR(20),    Drawing No                    
txtRNIND		EQ_RNIND     	PM_EQMST		VARCHAR(1),     Runlog Ind                     
txtMATGR		EQ_MATGR    	PM_EQMST		VARCHAR(6),     Material Group                    
txtMFRCD		EQ_MFRCD     	PM_EQMST		VARCHAR(5),     Manufacture Code 
txtMFRNM		PT_PRTNM     	CO_PTMST		VARCHAR(40),    Manufacture Name
txtADR01		PT_ADR01M     	CO_PTMST		VARCHAR(40),    Manufacture Address 
txtCTYNM		PT_CTYNM     	CO_PTMST		VARCHAR(15),    Manufacture City                    
txtICHCD		EQ_ICHCD     	PM_EQMST		VARCHAR(1),     Interchange Id.                    
txtPORNO		EQ_PORNO     	PM_EQMST		VARCHAR(15),    Purchase Order No                   
txtPORDT		EQ_PORDT     	PM_EQMST		DATE,           Purchase Order Date                     
txtPORVL		EQ_PORVL     	PM_EQMST		DECIMAL(12),    Purchase Order Value 
txtHRSRN		EQ_HRSRN     	PM_EQMST		DECIMAL(12),    Hours Run                 
txtSCHDT		EQ_SCHDT     	PM_EQMST		DATE,                               
-----------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on Technical Specification screen:
Field Name		Column Name		Table name		Type/Size		Description
-----------------------------------------------------------------------------------------------------------------------			
txtEQPID		ES_EQPID     	PM_ESTRN		VARCHAR(15),    Equipment No                  
txtEQPDS		ES_EQPDS    	PM_ESTRN		VARCHAR(50),    Equipment Description 
txtEQMTP		ES_EQMTP    	PM_ESTRN		VARCHAR(3),     Equipment main type	 
txtEQMTP_DS		CMT_CODDS    	CO_CDTRN		VARCHAR(50),    Equipment main type	Description                    
txtEQSTP		ES_EQSTP    	PM_ESTRN		VARCHAR(3),     Equipment sub type 
txtEQSTP_DS		CMT_CODDS    	CO_CDTRN		VARCHAR(50),    Equipment sub type Description         
txtSPDSC		ES_SPDSC     	PM_ESTRN		VARCHAR(50),    Specification 

For Addition:
txtSRLNO		AT_SRLNO     	PM_ATMST		VARCHAR(3),     Serial No                   
txtATDSC		AT_ATDSC     	PM_ATMST		VARCHAR(25),    Attribute Description                 

For Modification:
txtSRLNO		ES_SRLNO     	PM_ESTRN		VARCHAR(3),     Serial No                   
txtATDSC		ES_ATDSC     	PM_ESTRN		VARCHAR(25),    Attribute Description                 
-----------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on Trouble Shooting Guide screen:
Field Name		Column Name		Table name		Type/Size		Description
-----------------------------------------------------------------------------------------------------------------------			
txtTSGNO_TS		TS_TSGNO    	PM_TSMST		VARCHAR(8),     Trouble Shooting Guide No.                    
txtTSGDS_TS		TS_TSGDS     	PM_TSMST		VARCHAR(50),    Trouble Shooting Guide Description                    
txtTSFNO		TS_TSFNO   		PM_TSMST		VARCHAR(3),     T.S.G Failure No.                     
txtTSFDS		TS_TSFDS     	PM_TSMST		VARCHAR(60),    T.S.G Failure Description.                   
txtTSCNO		TS_TSCNO     	PM_TSMST		VARCHAR(3),     T.S.G Cause No.                     
txtTSCDS		TS_TSCDS     	PM_TSMST		VARCHAR(300),   T.S.G Cause Description.                     
txtTSCRM		TS_TSCRM     	PM_TSMST		VARCHAR(300),   T.S.G Remedy                     
-----------------------------------------------------------------------------------------------------------------------

List of fields with help facility: 
Field Name	 Display Description		     		Display Columns			  	Table Name
-----------------------------------------------------------------------------------------------------------------------
txtTAGNO	 Tag No, Description             		TG_TAGNO,TG_TAGDS 		   	PM_TGMST
txtEQPID	 Equipment ID, Description       		EQ_EQPID,EQ_EQPDS 		   	PM_EQMST
txtEQMTP	 Equipment main type, Description       CMT_CODCD,CMT_CODDS 		CO_CDTRN/MST/PMXXEQT
txtEQSTP	 Equipment Sub type, Description        CMT_CODCD,CMT_CODDS 		CO_CDTRN/MST/PMXXSTP
txtPLNCD	 Plant Code, Description                CMT_CODCD,CMT_CODDS		    CO_CDTRN/MST/PMXXPLN
txtARACD	 Area Code, Description                 CMT_CODCD,CMT_CODDS		    CO_CDTRN/MST/PMXXARA
txtCRICD	 Criticality Code, Description          CMT_CODCD,CMT_CODDS      	CO_CDTRN/MST/PMXXCRT
txtTSGNO	 T.S.G. No, Description             	TS_TSGNO,TS_TSGDS 		   	PM_TSMST
txtDPTCD	 Department Code, Department Name       CMT_CODCD,CMT_CODDS		    CO_CDTRN/SYS/COXXDPT
txtMFRCD	 Mfr Code, Name, Address,City           PT_PRTCD,PT_PRTNM, 			CO_PTMST/PT_PRTTP=S
													PT_ADR01,PT_CTYNM		    
------------------------------------------------------------------------------------------------------------------------

Validations & Requirements:

In Equipment -Base Detail:
	-> For Equipment addition should be enter Tag no which belongs to given Equipment Id.
	-> While user enter Tag No which will be display Plant, Area & Criticality for the given Equipment Id from Tag Master.
	-> User can be enter Equipment type & sub type from co_cdtrn table & also enter TSG No & Dept code from co_cdtrn.
	-> Fetches Description of all data that's fetch from co_cdtrn in Hash table.
	-> Fetch Manufacture Name, Address & City from co_ptmst table.
	-> Validate Equipment Id to checcking duplicate Equipment Id entry for addition .
	-> Fetches Equipment Id from Equipment master based on the Tag No.
	-> For modification validate the enetered Equipment Id is avaliable in the Equipment master & fetches the corresponding Equipment Desp.
	-> Make changes in existing  Equipment-Base Detail for the specified Equipment Id .
	-> For Modification & Enquiry ,Data will be pick up from pm_eqmst table of the specified Equipment Id.
	-> User can be Deleted records of corresponding Equipment Id.
	
In Equipment -Technical Specification:
    -> Fetches data from the attrib(pm_atmst), Equipment Detail(pm_eqmst) tables for the addition of the Specification, belongs to the given the Equipment type & sub-type.
    -> Fetches data from the Equipment Details(pm_eqmst) ,Equipment_Spcs(pm_estrn) tables for modification of the existing Specification for the Equipment type & sub-type.
	-> Generated the Plan,area & Criticality of the Given Tag No, it will not be added/updated in pm_estrn.
	-> Validating whether the Serial No & Attribute are not avaliable .
	
In Equipment- Trouble Shooting Guide:
	-> Facility is provided to accept trouble, Cause & Remedy details of given T.S.G. No.
	-> Any observation of above can be added/updated & deleted in future.
	-> Validating whether the failure no,Descp & Cause No,Descp are not avaliable. 
**/

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.table.*;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.util.StringTokenizer;
import java.sql.ResultSet;
import java.util.Hashtable;

class pm_tetsp extends cl_pbase 
{
	
	private JTextField txtEQPID,txtEQPDS;
	private JTextField txtTAGNO,txtTAGDS;
	private JTextField txtPLNCD,txtPLNDS;
	private JTextField txtARACD,txtARADS;
	private JTextField txtCRICD,txtCRIDS;
	private JTextField txtLUSBY,txtLUSDT;
	private JTextField txtEQMTP,txtEQSTP,txtEQMTP_DS,txtEQSTP_DS;
	private JTextField txtDPTCD,txtDPTNM;
    private JTextField txtTSGNO_EQ,txtTSGDS_EQ,txtDRGNO,txtRNIND,txtMATGR,txtMFRCD,txtMFRNM,txtADR01,txtCTYNM,txtMFRDS,txtICHCD,txtPORNO,txtPORDT,txtPORVL,txtHRSRN,txtINSDT,txtMNTVL;
    private JTextField txtTSGNO_TS,txtTSGDS_TS;
	private JTabbedPane jtpMANTAB,jtpEQPTAB;          
	private JPanel pnlEQPDL,pnlSPECF,pnlTSGDL,pnlEQPMT; 
	
	private JCheckBox chkDELEQ;
	
	private  cl_JTable tblSPECF,tblTSFAL,tblTSCAS; 
	private INPVF oINPVF;
	private TBLINPVF objTBLVRF;
	
    int TB1_CHKFL = 0; 				JCheckBox chkCHKFL;
    int TB1_SRLNO = 1;              JTextField txtSRLNO;
	int TB1_ATDSC = 2;              JTextField txtATDSC;// text box for ATDSCbute.
	int TB1_SPDSC = 3;              JTextField txtSPDSC;//for Attribute Specification.
	
	int TB2_CHKFL = 0; 			    JCheckBox chkCHKFL1;
	int TB2_TSFNO = 1;              JTextField txtTSFNO;
	int TB2_TSFDS = 2;              JTextField txtTSFDS;
	 
	int TB3_CHKFL = 0; 			    JCheckBox chkCHKFL2;
	int TB3_TSCNO = 1;              JTextField txtTSCNO;
	int TB3_TSCDS = 2;              JTextField txtTSCDS;
	int TB3_TSCRM = 3;              JTextField txtTSCRM;

	Hashtable<String,String> hstTSGDL;
	Hashtable<String,String> hstSRLNO;
	Hashtable<String,String[]> hstCDTRN;
	
	private int intCDTRN_TOT = 2;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;	
    
    private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;	
	
	String strTSFNO ="",strTSFDS ="";
	int L_ROWNO=0,intTSFAL_ROW=0,intTSCAS_ROW=0,L_CNT=0;
    pm_tetsp()		/*  Constructor   */
	{
		super(1);
		try
		{
			setMatrix(20,20);	
			
			pnlEQPDL = new JPanel();
			pnlSPECF = new JPanel();
			pnlTSGDL = new JPanel();
			pnlEQPMT = new JPanel();
			pnlEQPMT.setLayout(null);
			pnlTSGDL.setLayout(null);
			pnlEQPDL.setLayout(null);
			pnlSPECF.setLayout(null);
		
			jtpMANTAB=new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			
			jtpEQPTAB=new JTabbedPane();
			jtpEQPTAB.addMouseListener(this);
			
			//jtpMANTAB.add(pnlEQPDL,"Equipments Detail");
			//jtpMANTAB.add(pnlSPECF,"Technical Specification");
			jtpMANTAB.add(pnlEQPMT,"Equipment Base Details");
			
			jtpEQPTAB.add(pnlEQPDL,"Equipment Detail");
			jtpEQPTAB.add(pnlSPECF,"Technical Specification");
			jtpEQPTAB.add(pnlTSGDL,"Trouble Shooting Guide");
			
			add(new JLabel("Tag No :"),1,1,1,2,pnlEQPMT,'L');
			add(txtTAGNO = new TxtLimit(12),1,3,1,2,pnlEQPMT,'L');
			add(txtTAGDS = new TxtLimit(50),1,5,1,6,pnlEQPMT,'L');
			
			add(new JLabel("Plant Code :"),1,11,1,2,pnlEQPMT,'L');
			add(txtPLNCD = new TxtLimit(3),1,13,1,1,pnlEQPMT,'L');
			add(txtPLNDS = new TxtLimit(10),1,14,1,4,pnlEQPMT,'L');
			
			add(new JLabel("Equipment Id :"),2,1,1,2,pnlEQPMT,'L');
			add(txtEQPID = new TxtLimit(12),2,3,1,2,pnlEQPMT,'L');
			add(txtEQPDS = new TxtLimit(50),2,5,1,6,pnlEQPMT,'L');
		
			add(new JLabel("Area Code :"),2,11,1,2,pnlEQPMT,'L');
			add(txtARACD = new TxtLimit(3),2,13,1,1,pnlEQPMT,'L');
			add(txtARADS = new TxtLimit(10),2,14,1,4,pnlEQPMT,'L');
			
			add(new JLabel("Criticality :"),3,11,1,2,pnlEQPMT,'L');
			add(txtCRICD = new TxtLimit(1),3,13,1,1,pnlEQPMT,'L');
			add(txtCRIDS = new TxtLimit(5),3,14,1,4,pnlEQPMT,'L');
			
			add(chkDELEQ=new JCheckBox("Delete Eqpt Detail"),4,15,1,3,pnlEQPMT,'L');
			
			add(new JLabel("Equipment Type:"),3,1,1,2.5,pnlEQPMT,'L');  
			add(txtEQMTP = new TxtLimit(3),3,3,1,2,pnlEQPMT,'L'); 
			add(txtEQMTP_DS = new TxtLimit(50),3,5,1,6,pnlEQPMT,'L'); 
			add(new JLabel("Sub Type:"),4,1,1,2,pnlEQPMT,'L');  
			add(txtEQSTP = new TxtLimit(3),4,3,1,2,pnlEQPMT,'L');
			add(txtEQSTP_DS = new TxtLimit(50),4,5,1,6,pnlEQPMT,'L');
			
		//	add(new JLabel("T.S.G.Number"),1,1,1,2,pnlEQPDL,'L');  
			//add(txtTSGNO_EQ = new TxtLimit(8),1,3,1,2,pnlEQPDL,'L');
			//add(txtTSGDS_EQ = new TxtLimit(50),1,5,1,6,pnlEQPDL,'L');
			add(new JLabel("Drawing No."),1,1,1,2,pnlEQPDL,'L');  
			add(txtDRGNO = new TxtLimit(20),1,3,1,4,pnlEQPDL,'L');
			add(new JLabel("Runlog Ind"),1,7,1,2,pnlEQPDL,'L');  
			add(txtRNIND = new TxtLimit(1),1,9,1,2,pnlEQPDL,'L'); 
			add(new JLabel("Department"),1,11,1,2,pnlEQPDL,'L');  
			add(txtDPTCD = new TxtLimit(3),1,13,1,1,pnlEQPDL,'L');
			add(txtDPTNM = new TxtLimit(15),1,14,1,4.5,pnlEQPDL,'L');
			
			//add(new JLabel("Drawing No."),2,1,1,2,pnlEQPDL,'L');  
			//add(txtDRGNO = new TxtLimit(20),2,3,1,4,pnlEQPDL,'L');
			//add(new JLabel("Runlog Ind"),2,8,1,2,pnlEQPDL,'L');  
			//add(txtRNIND = new TxtLimit(1),2,10,1,2,pnlEQPDL,'L'); 
			
			add(new JLabel("Material Group"),2,1,1,2,pnlEQPDL,'L');  
			add(txtMATGR = new TxtLimit(6),2,3,1,2,pnlEQPDL,'L');
			add(new JLabel("Mfr. Code"),2,5,1,2,pnlEQPDL,'L');  
			add(txtMFRCD = new TxtLimit(5),2,7,1,2,pnlEQPDL,'L');
			add(txtMFRNM = new TxtLimit(20),2,9,1,7,pnlEQPDL,'L');
			add(txtADR01 = new TxtLimit(20),3,9,1,7,pnlEQPDL,'L');
			add(txtCTYNM = new TxtLimit(20),4,9,1,4,pnlEQPDL,'L');
			//add(txtMFRDS = new TxtLimit(20),7,9,1,4,pnlEQPDL,'L');
		
			add(new JLabel("Interchange Id."),5,1,1,2,pnlEQPDL,'L');  
			add(txtICHCD = new TxtLimit(1),5,3,1,2.5,pnlEQPDL,'L');
			
			add(new JLabel("P.O Number"),6,1,1,2,pnlEQPDL,'L');  
			add(txtPORNO = new TxtLimit(15),6,3,1,2.5,pnlEQPDL,'L');
			add(new JLabel("P.O Date"),6,6,1,2,pnlEQPDL,'L');  
			add(txtPORDT = new TxtDate(),6,8,1,2.5,pnlEQPDL,'L');
			add(new JLabel("P.O Value"),6,11,1,2,pnlEQPDL,'L');  
			add(txtPORVL = new TxtNumLimit(12),6,13,1,3,pnlEQPDL,'L');
		
			add(new JLabel("Hours Run"),7,1,1,2,pnlEQPDL,'L');  
			add(txtHRSRN = new TxtNumLimit(12),7,3,1,2.5,pnlEQPDL,'L');
			add(new JLabel("Installed Date"),7,6,1,2,pnlEQPDL,'L');  
			add(txtINSDT = new TxtDate(),7,8,1,2.5,pnlEQPDL,'L');
			add(new JLabel("Mnt Cost"),7,11,1,2,pnlEQPDL,'L');  
			add(txtMNTVL = new TxtNumLimit(12.2),7,13,1,3,pnlEQPDL,'L');
		
    		String[] L_strTBLHD = {"","Serial No","Attribute","Specification"};
    		int[] L_intCOLSZ = {10,70,300,350};
    		tblSPECF= crtTBLPNL1(pnlSPECF,L_strTBLHD,100,1,2,9,15,L_intCOLSZ,new int[]{0});
    		
    		tblSPECF.addKeyListener(this);
    		tblSPECF.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
    		tblSPECF.setCellEditor(TB1_SRLNO,txtSRLNO = new TxtLimit(3));
    		tblSPECF.setCellEditor(TB1_ATDSC,txtATDSC = new TxtLimit(20));
    		tblSPECF.setCellEditor(TB1_SPDSC,txtSPDSC = new TxtLimit(50));
    		txtSRLNO.addFocusListener(this);
    		
    		add(jtpMANTAB,1,2,18,19,this,'L');
    		add(jtpEQPTAB,5,1,12,18,pnlEQPMT,'L');
    		
			oINPVF=new INPVF();
			txtEQPID.setInputVerifier(oINPVF);
			txtTAGNO.setInputVerifier(oINPVF);
			txtPLNCD.setInputVerifier(oINPVF);
			txtARACD.setInputVerifier(oINPVF);
			txtCRICD.setInputVerifier(oINPVF);
			txtEQMTP.setInputVerifier(oINPVF);
			txtEQSTP.setInputVerifier(oINPVF);
			txtDPTCD.setInputVerifier(oINPVF);
			//txtTSGNO_EQ.setInputVerifier(oINPVF);
			txtMFRCD.setInputVerifier(oINPVF);
			txtDRGNO.setInputVerifier(oINPVF);
			txtPORNO.setInputVerifier(oINPVF);
			txtRNIND.setInputVerifier(oINPVF);
			txtMATGR.setInputVerifier(oINPVF);
			txtICHCD.setInputVerifier(oINPVF);
    		
			add(new JLabel("T.S.G. No"),1,2,1,2,pnlTSGDL,'L');  
			add(txtTSGNO_TS = new TxtLimit(8),1,4,1,2,pnlTSGDL,'L');
			add(new JLabel("Description"),1,6,1,2,pnlTSGDL,'L');  
			add(txtTSGDS_TS = new TxtLimit(50),1,8,1,7,pnlTSGDL,'L');
			
    		String[] L_strTBLHD1 = {"","Failure No","Failure Description."};
    		int[] L_intCOLSZ1 = {10,60,500};
    		tblTSFAL= crtTBLPNL1(pnlTSGDL,L_strTBLHD1,50,2,2,3,12,L_intCOLSZ1,new int[]{0});
    		
    		tblTSFAL.addKeyListener(this);
    		tblTSFAL.setCellEditor(TB2_CHKFL,chkCHKFL1=new JCheckBox());
    		tblTSFAL.setCellEditor(TB2_TSFNO,txtTSFNO = new TxtLimit(3));
    		tblTSFAL.setCellEditor(TB2_TSFDS,txtTSFDS = new TxtLimit(60));
    		txtTSFNO.addFocusListener(this);
    		chkCHKFL1.addMouseListener(this);
    		
    		String[] L_strTBLHD2 = {"","Cause No","Cause Description.","Remedy"};
    		int[] L_intCOLSZ2 = {10,60,350,350};
    		tblTSCAS= crtTBLPNL1(pnlTSGDL,L_strTBLHD2,50,6,2,5,16,L_intCOLSZ2,new int[]{0});
    		
    		tblTSCAS.addKeyListener(this);
    		tblTSCAS.setCellEditor(TB3_CHKFL,chkCHKFL2=new JCheckBox());
    		tblTSCAS.setCellEditor(TB3_TSCNO,txtTSCNO = new TxtLimit(3));
    		tblTSCAS.setCellEditor(TB3_TSCDS,txtTSCDS = new TxtLimit(300));
    		tblTSCAS.setCellEditor(TB3_TSCRM,txtTSCRM = new TxtLimit(300));
    		txtTSCNO.addFocusListener(this);
    		chkCHKFL2.addFocusListener(this);
    		
    		txtTSGNO_TS.setInputVerifier(oINPVF);
    		objTBLVRF = new TBLINPVF();
    		tblTSFAL.setInputVerifier(objTBLVRF);
    		tblSPECF.setInputVerifier(objTBLVRF);
    		tblTSCAS.setInputVerifier(objTBLVRF);
    		
			setENBL(false);
			
			hstCDTRN = new Hashtable<String,String[]>();
			hstCDTRN.clear();
			
			crtCDTRN("'MSTPMXXPLN','MSTPMXXARA','MSTPMXXCRT', 'MSTPMXXEQT','MSTPMXXEST','SYSCOXXDPT'","",hstCDTRN);
			
			/**Hashtable for Trouble Shooting Guide Description*/
			/*try
			{	
				hstTSGDL = new Hashtable<String,String>();
				String L_strSQLQRY1= "Select TS_TSGNO,TS_TSGDS from PM_TSMST where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TS_STSFL,'')<>'X'";
				
				ResultSet L_rstRSSET1= cl_dat.exeSQLQRY(L_strSQLQRY1);
				//System.out.println("hstTSGDL"+L_strSQLQRY1);
				if(L_rstRSSET1 != null)
				{
					while(L_rstRSSET1.next())
					{
						hstTSGDL.put(nvlSTRVL(L_rstRSSET1.getString("TS_TSGNO"),""),nvlSTRVL(L_rstRSSET1.getString("TS_TSGDS"),""));
					}
						L_rstRSSET1.close();	
				}	
			}catch(Exception Ex){System.out.println(Ex+":TS_TSGNO");}
		*/
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
   
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{ 
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP1();
				clrCOMP();
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					if(jtpEQPTAB.getSelectedIndex()==2)
						txtTSGNO_TS.requestFocus();
					else
						txtTAGNO.requestFocus();
					
					setENBL(true);
					
				    txtTAGDS.setEnabled(false);
					txtEQSTP_DS.setEnabled(false);
				    txtEQMTP_DS.setEnabled(false);
					txtPLNDS.setEnabled(false);
					txtARADS.setEnabled(false);
					txtCRIDS.setEnabled(false);
					txtDPTNM.setEnabled(false);
					txtMFRNM.setEnabled(false);
					txtADR01.setEnabled(false);
					txtCTYNM.setEnabled(false);
					txtPLNCD.setEnabled(false);
					txtARACD.setEnabled(false);
					txtCRICD.setEnabled(false);
				}
				else
					setENBL(false);
			
				 setENBL_TXT();
				
			}
			
			/** while enter on Tag No ,set plant,area,criticality code (from help screen) & Description (from hashtable) in text box**/
			else if(M_objSOURC == txtTAGNO)
			{
				if(txtTAGNO.getText().length()>0)
				{
					txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
					txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
					txtCRIDS.setText(getCDTRN("MSTPMXXCRT"+txtCRICD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				}
			}
			/** while enter on Equipment Id ,set Equipment type ,sub type code (from help screen) & description (from hashtable) in text box**/
			
			else if((M_objSOURC == txtEQPID) && jtpEQPTAB.getSelectedIndex()!=0 || (M_objSOURC == txtEQPID) && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && jtpEQPTAB.getSelectedIndex()==0 )
			{
				if(txtEQMTP.getText().length()>0)
					txtEQMTP_DS.setText(getCDTRN("MSTPMXXEQT"+txtEQMTP.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				if(txtEQSTP.getText().length()>0)
					txtEQSTP_DS.setText(getCDTRN("MSTPMXXEST"+txtEQSTP.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
			}
		
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"actionPerformed");		
		}
	}
	void setENBL_TXT()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				 if(jtpEQPTAB.getSelectedIndex()==0)
				 {
					 txtEQPDS.setEnabled(false);
					 txtEQMTP.setEnabled(false);
					 txtTAGDS.setEnabled(false);
					// txtPLNCD.setEnabled(false);
					// txtARACD.setEnabled(false);
					// txtCRICD.setEnabled(false);
					 txtDRGNO.setEnabled(false);
					 txtRNIND.setEnabled(false);
					 txtDPTCD.setEnabled(false);
					 txtMATGR.setEnabled(false);
					 txtMFRCD.setEnabled(false);
					 txtICHCD.setEnabled(false);
					 txtPORNO.setEnabled(false);
					 txtPORDT.setEnabled(false);
					 txtPORVL.setEnabled(false);
					 txtHRSRN.setEnabled(false);
					 txtINSDT.setEnabled(false);
					 txtMNTVL.setEnabled(false);
				}
				 if(jtpEQPTAB.getSelectedIndex()==2)
				 {
				 	 txtEQPDS.setEnabled(false);
					 txtEQMTP.setEnabled(false);
					 txtTAGDS.setEnabled(false);
					// txtPLNCD.setEnabled(false);
					 //txtARACD.setEnabled(false);
					// txtCRICD.setEnabled(false);
					// txtTSGDS_TS.setEnabled(false);
					
				 }
				 if(jtpEQPTAB.getSelectedIndex()==2)
				 {
					 txtEQPDS.setEnabled(false);
					 txtEQMTP.setEnabled(false);
					 txtTAGDS.setEnabled(false);
					// txtPLNCD.setEnabled(false);
					 //txtARACD.setEnabled(false);
					// txtCRICD.setEnabled(false);
					 txtTSGDS_TS.setEnabled(false);
				 }
			}
			else
			{
				if(jtpEQPTAB.getSelectedIndex()==0)
				{
					 txtEQPDS.setEnabled(true);
					 //txtPLNCD.setEnabled(true);
					// txtARACD.setEnabled(true);
					// txtCRICD.setEnabled(true);
					 txtTAGNO.setEnabled(true);
					 txtEQPDS.setEnabled(true);
					 txtEQMTP.setEnabled(true);
					 txtEQSTP.setEnabled(true);
					 txtDRGNO.setEnabled(true);
					 txtRNIND.setEnabled(true);
					 txtDPTCD.setEnabled(true);
					 txtMATGR.setEnabled(true);
					 txtMFRCD.setEnabled(true);
					 txtICHCD.setEnabled(true);
					 txtPORNO.setEnabled(true);
					 txtPORDT.setEnabled(true);
					 txtPORVL.setEnabled(true);
					 txtHRSRN.setEnabled(true);
					 txtINSDT.setEnabled(true);
					 txtMNTVL.setEnabled(true);
				 } 
				 if(jtpEQPTAB.getSelectedIndex()==1)
				 {
					 
					/* try
						{	
							hstSRLNO = new Hashtable<String,String>();
							String L_strSQLQRY1= "Select ES_EQPID,ES_SRLNO from PM_ESTRN where ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(ES_STSFL,'')<>'X'";
							
							ResultSet L_rstRSSET1= cl_dat.exeSQLQRY(L_strSQLQRY1);
							System.out.println("hstTSGDL"+L_strSQLQRY1);
							if(L_rstRSSET1 != null)
							{
								while(L_rstRSSET1.next())
								{
									hstSRLNO.put(nvlSTRVL(L_rstRSSET1.getString("ES_EQPID"),""),nvlSTRVL(L_rstRSSET1.getString("ES_SRLNO"),""));
								}
									L_rstRSSET1.close();	
							}	
						}catch(Exception Ex){System.out.println(Ex+":ES_SRLNO");}*/
					
					 txtEQPDS.setEnabled(false);
					 txtEQMTP.setEnabled(false);
					 txtTAGDS.setEnabled(false);
					// txtPLNCD.setEnabled(false);
					// txtARACD.setEnabled(false);
					// txtCRICD.setEnabled(false);
					 tblSPECF.setEnabled(true);
				 }
				 if(jtpEQPTAB.getSelectedIndex()==2)
				 {
					 txtEQPDS.setEnabled(false);
					 txtEQMTP.setEnabled(false);
					 txtTAGDS.setEnabled(false);
					// txtPLNCD.setEnabled(false);
					// txtARACD.setEnabled(false);
					// txtCRICD.setEnabled(false);
					
				 }
			 }
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"setENBL_TXT");		
		}
		 
	}

			
	/** Method to request Focus in all TextField of component,when press ENTER & Display help screen when press F1 **/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtTAGNO)
				{
					if(txtTAGNO.getText().length()==0)
					{
						txtTAGDS.setText("");
						txtPLNCD.setText("");
						txtARACD.setText("");
						txtCRICD.setText("");
						txtPLNDS.setText("");
						txtARADS.setText("");
						txtCRIDS.setText("");
						
					}
					txtEQPID.requestFocus();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						setMSG("Enter Equipment ID ..",'N');
					else
						setMSG("Enter Equipment ID or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtEQPID)
				{
					if(txtEQPID.getText().length()==0)
					{
						txtEQPDS.setText("");
						txtEQMTP.setText("");
						txtEQSTP.setText("");
						txtEQMTP_DS.setText("");
						txtEQSTP_DS.setText("");
						clrCOMP();
					}
					
					txtEQPDS.requestFocus();
					setMSG("Enter Equipment Description..",'N');
					if(jtpEQPTAB.getSelectedIndex()!=0 || jtpEQPTAB.getSelectedIndex()==0 && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						txtEQSTP.requestFocus();
				
				}
				else if(M_objSOURC == txtEQPDS)
				{
					txtEQPDS.setText(txtEQPDS.getText().replace("'","`"));
				    txtEQMTP.requestFocus();
					setMSG("Enter Equipment Type or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtEQMTP)
				{
					if(txtEQMTP.getText().length()==0)
						txtEQMTP_DS.setText("");
					txtEQSTP.requestFocus();
					setMSG("Enter Equipment Sub Type or Press F1 to Select from List..",'N');
				}
				else if(M_objSOURC == txtEQSTP)
				{
					if(txtEQSTP.getText().length()==0)
						txtEQSTP_DS.setText("");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{	
						//txtPLNCD.requestFocus();
						txtDRGNO.requestFocus();
						setMSG("Enter Drawing No ..",'N');
					}
					
					/**Fetch Equipment detail records*/
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						clrCOMP();
						getEQPDL();
						getEQPSP(); /**Fetch Technical Specification records*/
						if(txtTSGNO_TS.getText().length()>0)
							getTSFDL();
					}
					
				    /**For Technical Specification ,display Attribute of specify Equipment main type & sub type in table  . */
					
					if(jtpEQPTAB.getSelectedIndex()==1 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						try
						{
							String L_strSRLNO="",L_strSRLNO_AT="";
							
							this.setCursor(cl_dat.M_curWTSTS_pbst);
							
							tblSPECF.clrTABLE();
							inlTBLEDIT(tblSPECF);
							setMSG("Fetching Records ...",'N');
							
							M_strSQLQRY= " SELECT AT_SRLNO,AT_ATDSC FROM PM_ATMST ";
							M_strSQLQRY+= " where  AT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  AND ifnull(AT_STSFL,'')<>'X' ";
							if(txtEQMTP.getText().length()>0)
								M_strSQLQRY+= " AND AT_EQMTP='"+txtEQMTP.getText()+"'";
							if(txtEQSTP.getText().length()>0)
								M_strSQLQRY+= " AND AT_EQSTP='"+txtEQSTP.getText()+"' ";
							//System.out.println(">>>select>>"+ M_strSQLQRY );
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
							L_CNT=0;
							if(M_rstRSSET != null)
							{
								while(M_rstRSSET.next())
								{
									 L_strSRLNO_AT=nvlSTRVL(M_rstRSSET.getString("AT_SRLNO"),"");
									tblSPECF.setValueAt(L_strSRLNO_AT,L_CNT,TB1_SRLNO);
									tblSPECF.setValueAt(nvlSTRVL(M_rstRSSET.getString("AT_ATDSC"),""),L_CNT,TB1_ATDSC);
									L_CNT++;
									
								}
								 if(L_CNT == 0)
										setMSG("No Attribute Found for Specification ..",'E');
						           
							}
				            M_rstRSSET.close();
				            
				          /*  if(hstSRLNO.containsKey(txtEQPID.getText()))
								L_strSRLNO = hstSRLNO.get(txtEQPID.getText()).toString();
							
				            if(L_strSRLNO_AT.equals(L_strSRLNO))
								((JCheckBox)tblSPECF.cmpEDITR[TB1_CHKFL]).setEnabled(false);
							else
							{
								tblSPECF.setValueAt(new Boolean(true),L_CNT,TB1_CHKFL);	
								((JCheckBox)tblSPECF.cmpEDITR[TB1_CHKFL]).setEnabled(true);
							}*/
				          
						}
						catch(Exception E_VR)
						{
							setMSG(E_VR,"Fetch Attribute");		
							this.setCursor(cl_dat.M_curDFSTS_pbst);
						}		
					}       
				}
				if(jtpEQPTAB.getSelectedIndex()==0)
				{
					/*if(M_objSOURC == txtPLNCD)
					{
						if(txtPLNCD.getText().length()==0)
							txtPLNDS.setText("");
						txtARACD.requestFocus();
						setMSG("Enter Area Code or Press F1 to Select from List..",'N');
					}
					else if(M_objSOURC == txtARACD)
					{
						if(txtARACD.getText().length()==0)
							txtARADS.setText("");
						txtCRICD.requestFocus();
						setMSG("Enter Criticality Code or Press F1 to Select from List..",'N');
					}
					else if(M_objSOURC == txtCRICD)
					{
						if(txtCRICD.getText().length()==0)
							txtCRIDS.setText("");
						
						txtDRGNO.requestFocus();
						setMSG("Enter Drawing. NO..",'N');
						
					}*/
					if(M_objSOURC == txtDRGNO)
					{
						txtDRGNO.setText(txtDRGNO.getText().replace("'","`"));
						txtRNIND.requestFocus();
						setMSG("Enter Runlog Ind..",'N');
					}
					else if(M_objSOURC == txtRNIND)
					{
						txtDPTCD.requestFocus();
						setMSG("Enter Department Code or Press F1 to Select from List..",'N');
						
					}
					else if(M_objSOURC == txtDPTCD)
					{
						if(txtDPTCD.getText().length()==0)
							txtDPTNM.setText("");
						txtMATGR.requestFocus();
						setMSG("Enter Material Group..",'N');
					}
					
					else if(M_objSOURC == txtMATGR)
					{
						txtMATGR.setText(txtMATGR.getText().replace("'","`"));
						txtMFRCD.requestFocus();
						setMSG("Enter Mfr.Code or Press F1 to Select from List..",'N');
					}
					else if(M_objSOURC == txtMFRCD)
					{
						if(txtMFRCD.getText().length()==0)
						{
							txtMFRNM.setText("");
							txtADR01.setText("");
							txtCTYNM.setText("");
							//txtMFRDS.setText("");
						}
						txtICHCD.requestFocus();
						setMSG("Enter Interchange Id...",'N');
					}
					else if(M_objSOURC == txtICHCD)
					{
						txtPORNO.requestFocus();
						setMSG("Enter P.O. NO...",'N');
					}
					else if(M_objSOURC == txtPORNO)
					{
						txtPORNO.setText(txtPORNO.getText().replace("'","`"));
						txtPORDT.requestFocus();
						setMSG("Enter P.O. Date..",'N');
					}
					else if(M_objSOURC == txtPORDT)
					{
						txtPORVL.requestFocus();
						setMSG("Enter P.O. Value..",'N');
					}
					else if(M_objSOURC == txtPORVL)
					{
						txtHRSRN.requestFocus();
						setMSG("Enter Hours Run..",'N');
					}
					else if(M_objSOURC == txtHRSRN)
					{
						txtINSDT.requestFocus();
						setMSG("Enter Installed Date..",'N');
					}
					else if(M_objSOURC == txtINSDT)
					{
						txtMNTVL.requestFocus();
						setMSG("Enter Maintenance Value..",'N');
					}
					else if(M_objSOURC == txtMNTVL)
						cl_dat.M_btnSAVE_pbst.requestFocus();
			  }
			  if(jtpEQPTAB.getSelectedIndex()==2)
			  {
					if(M_objSOURC == txtTSGNO_TS)
					{
						if(txtTSGNO_TS.getText().length()==0)
						{
							txtTSGDS_TS.setText("");
							tblTSFAL.clrTABLE();
							inlTBLEDIT(tblTSFAL);
							tblTSCAS.clrTABLE();
							inlTBLEDIT(tblTSCAS);
						}
						txtTSGDS_TS.requestFocus();
						setMSG("Enter T.S.G. No Description..",'N');
					}	
					else if(M_objSOURC == txtTSGDS_TS)
					{
						txtTSGDS_TS.setText(txtTSGDS_TS.getText().replace("'","`"));
						tblTSFAL.requestFocus();
						setMSG("Enter Failure No..",'N');
					}
			  }
			}
			else if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
				if(M_objSOURC == txtTAGNO )
				{
					M_strHLPFLD = "txtTAGNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT distinct TG_TAGNO,TG_TAGDS,TG_PLNCD,TG_ARACD,TG_CRICD from PM_TGMST";
					M_strSQLQRY+= " where TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TG_STSFL,'')<>'X'";
					if(txtTAGNO.getText().length()>0)				
						M_strSQLQRY+= " AND TG_TAGNO like '"+txtTAGNO.getText().trim()+"%'";
					M_strSQLQRY+= " order by TG_TAGNO ";
					//System.out.println("txtTAGNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Tag No","Tag Description","Plant Code","Area Code","Criticality"},5,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			
				else if((M_objSOURC == txtEQPID ) && jtpEQPTAB.getSelectedIndex()==2  || (M_objSOURC == txtEQPID ) &&  !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && jtpEQPTAB.getSelectedIndex()==0
					 ||	(M_objSOURC == txtEQPID ) &&  cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)  && jtpEQPTAB.getSelectedIndex()==1 )
				{
						M_strHLPFLD = "txtEQPID";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
					
						M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS,EQ_EQMTP,EQ_EQSTP from PM_EQMST";
						M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(EQ_STSFL,'')<>'X'";
						if(txtTAGNO.getText().length()>0)		
							M_strSQLQRY+= " AND EQ_TAGNO='"+txtTAGNO.getText()+"'";
						if(txtEQPID.getText().length()>0)				
							M_strSQLQRY+= " AND EQ_EQPID like '"+txtEQPID.getText().trim()+"%'";
						M_strSQLQRY+= " order by EQ_EQPID";
						//System.out.println("EQPT_ID>>"+M_strSQLQRY);
					
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Id","Equipment Description","Equipment Type","Sub Type"},4,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if((M_objSOURC == txtEQPID ) && jtpEQPTAB.getSelectedIndex()==1 &&  !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					M_strHLPFLD = "txtEQPID";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
				
					M_strSQLQRY=" SELECT distinct EQ_EQPID,EQ_EQPDS,EQ_EQMTP,EQ_EQSTP from PM_ESTRN,PM_EQMST "; 
					M_strSQLQRY+= " where ES_CMPCD=EQ_CMPCD AND ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(ES_STSFL,'')<>'X' ";
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						M_strSQLQRY+= " AND ES_EQPID=EQ_EQPID";
					//else
					//	M_strSQLQRY+= " AND ES_EQPID <>EQ_EQPID";
					if(txtTAGNO.getText().length()>0)					
						M_strSQLQRY+= " AND EQ_TAGNO='"+txtTAGNO.getText().trim()+"'";
					if(txtEQPID.getText().length()>0)				
						M_strSQLQRY+= " AND EQ_EQPID like '"+txtEQPID.getText().trim()+"%'";
					M_strSQLQRY+= " order by EQ_EQPID";
					//System.out.println("EQPT_ID1>>>"+M_strSQLQRY);
					
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Id","Equipment Description","Equipment Type","Sub Type"},4,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
					
				}	
				else if(M_objSOURC == txtARACD)
				{
					M_strHLPFLD = "txtARACD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXARA' AND ifnull(CMT_STSFL,'')<>'X' ";
					if(txtARACD.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtARACD.getText().trim()+"%'";
					M_strSQLQRY+= " order by cmt_codcd";
					//System.out.println("txtARACD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Area Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				else if(M_objSOURC == txtPLNCD)
				{
					M_strHLPFLD = "txtPLNCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXPLN' AND ifnull(CMT_STSFL,'')<>'X'";
					if(txtPLNCD.getText().length()>0)				
						M_strSQLQRY+=  " AND cmt_codcd like '"+txtPLNCD.getText().trim()+"%'";
					M_strSQLQRY+= " order by cmt_codcd";
					//System.out.println("txtPLNCD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Plant Code","Description"},2,"CT");
				//	cl_hlp(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN),2,1,new String[]{"Plant Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
					 
				}	
				else if(M_objSOURC == txtCRICD)
				{
					M_strHLPFLD = "txtCRICD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXCRT' AND ifnull(CMT_STSFL,'')<>'X'";
					if(txtCRICD.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtCRICD.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Criticality Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				else if(M_objSOURC == txtEQMTP)
				{
					M_strHLPFLD = "txtEQMTP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXEQT' AND ifnull(CMT_STSFL,'')<>'X' ";
					if(txtEQMTP.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtEQMTP.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Type","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				else if(M_objSOURC == txtEQSTP)
				{
					if(jtpEQPTAB.getSelectedIndex()!=1 )
					{
						M_strHLPFLD = "txtEQSTP";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
	
						M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
						M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXEST' AND ifnull(CMT_STSFL,'')<>'X'";
						if(txtEQSTP.getText().length()>0)				
							M_strSQLQRY+= " AND cmt_codcd like '"+txtEQSTP.getText().trim()+"%'";
						M_strSQLQRY += " order by cmt_codcd";
						//System.out.println("txtCRICD>>"+M_strSQLQRY);
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Sub Type","Description"},2,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				else if(M_objSOURC==txtDPTCD)		
				{
					M_strHLPFLD = "txtDPTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP ='SYS'"
						+" AND CMT_CGSTP = 'COXXDPT'";
					if(txtDPTCD.getText().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtDPTCD.getText().trim()+"%'";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Department code","Department Name"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
				else if(jtpEQPTAB.getSelectedIndex()==2)	
				//else if(M_objSOURC==txtTSGNO_EQ  || M_objSOURC==txtTSGNO_TS && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && jtpEQPTAB.getSelectedIndex()==2)	
				{
					//if(M_objSOURC==txtTSGNO_EQ)
					//	M_strHLPFLD = "txtTSGNO_EQ";
					//if(M_objSOURC==txtTSGNO_TS)
					M_strHLPFLD = "txtTSGNO_TS";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct TS_TSGNO,TS_TSGDS from PM_TSMST ";
					M_strSQLQRY+= " where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TS_STSFL,'')<>'X'";
					//if(jtpEQPTAB.getSelectedIndex()==0 && txtTSGNO_EQ.getText().length() >0)
					//	M_strSQLQRY += " AND TS_TSGNO like '"+txtTSGNO_EQ.getText().trim()+"%'";
					if(txtTSGNO_TS.getText().length() >0)
						M_strSQLQRY += " AND TS_TSGNO like '"+txtTSGNO_TS.getText().trim()+"%'";
					M_strSQLQRY += " order by TS_TSGNO";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"T.S.G. NO","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
				else if(M_objSOURC==txtMFRCD)		
				{
					M_strHLPFLD = "txtMFRCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST ";
					M_strSQLQRY += "WHERE PT_PRTTP='S' ";
					if(txtMFRCD.getText().length() >0)
						M_strSQLQRY += " AND PT_PRTCD like '"+txtMFRCD.getText().trim()+"%'";
					M_strSQLQRY += " order by PT_PRTCD";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Mfr code","Mfr.Name","Address","City"},4,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"keypressed");		
		}		
		
	}
	
	/**enabled & disabled table field **/
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtTAGNO)
		{
			txtTAGNO.requestFocus();
			setMSG("Enter Tag No or Press F1 to Select from List..",'N');
		}
		if(M_objSOURC == txtEQSTP)
		{
			txtEQSTP.requestFocus();
			setMSG("Enter Equipment Sub type or Press F1 to Select from List..",'N');
		}
		else if(M_objSOURC == txtSRLNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(tblSPECF.getSelectedRow()>=L_CNT)
				{
				 // ((JCheckBox)tblSPECF.cmpEDITR[TB1_CHKFL]).setEnabled(false);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SRLNO]).setEditable(false);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_ATDSC]).setEditable(false);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SPDSC]).setEditable(false);
				}
				else
				{
				 // ((JCheckBox)tblSPECF.cmpEDITR[TB1_CHKFL]).setEnabled(true);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SRLNO]).setEditable(true);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_ATDSC]).setEditable(true);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SPDSC]).setEditable(true); 
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblSPECF.getSelectedRow()>=L_ROWNO)
				{
				  //((JCheckBox)tblSPECF.cmpEDITR[TB1_CHKFL]).setEnabled(false);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SRLNO]).setEditable(false);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_ATDSC]).setEditable(false);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SPDSC]).setEditable(false);
				}
				else
				{
				  //((JCheckBox)tblSPECF.cmpEDITR[TB1_CHKFL]).setEnabled(true);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SRLNO]).setEditable(true);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_ATDSC]).setEditable(true);
				  ((JTextField)tblSPECF.cmpEDITR[TB1_SPDSC]).setEditable(true); 
				}
			}
		}
		else if(M_objSOURC == txtTSFNO)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblTSFAL.getSelectedRow()>=intTSFAL_ROW)
				{
				  ((JTextField)tblTSFAL.cmpEDITR[TB2_TSFNO]).setEditable(false);
				  ((JTextField)tblTSFAL.cmpEDITR[TB2_TSFDS]).setEditable(false);
				}
				else
				{
				  ((JTextField)tblTSFAL.cmpEDITR[TB2_TSFNO]).setEditable(true);
				  ((JTextField)tblTSFAL.cmpEDITR[TB2_TSFDS]).setEditable(true);
				}
				
			}
		}
		else if(M_objSOURC == txtTSCNO)
		{
			if(tblTSCAS.getSelectedRow()>=intTSCAS_ROW)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
				   //((JCheckBox)tblTSCAS.cmpEDITR[TB3_CHKFL]).setEnabled(false);
				   ((JTextField)tblTSCAS.cmpEDITR[TB3_TSCNO]).setEditable(false);
				   ((JTextField)tblTSCAS.cmpEDITR[TB3_TSCDS]).setEditable(false);
				   ((JTextField)tblTSCAS.cmpEDITR[TB3_TSCRM]).setEditable(false);
				}
				else
				{
					//((JCheckBox)tblTSCAS.cmpEDITR[TB3_CHKFL]).setEnabled(true);
					((JTextField)tblTSCAS.cmpEDITR[TB3_TSCNO]).setEditable(true);
					((JTextField)tblTSCAS.cmpEDITR[TB3_TSCDS]).setEditable(true);
					((JTextField)tblTSCAS.cmpEDITR[TB3_TSCRM]).setEditable(true);
				}
				
			/*	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					System.out.println("intTSCAS_ROW"+intTSCAS_ROW);
					if(tblTSCAS.getSelectedRow()>=intTSCAS_ROW)
						((JCheckBox)tblTSCAS.cmpEDITR[TB3_CHKFL]).setEnabled(true);
					else
						((JCheckBox)tblTSCAS.cmpEDITR[TB3_CHKFL]).setEnabled(false);
				
				}*/
			}
		}
	}
	
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		 {
			if(L_ME.getSource().equals(jtpEQPTAB))
			{
				if(jtpEQPTAB.getSelectedIndex()==2)
					txtTSGNO_TS.requestFocus();
				else
					txtTAGNO.requestFocus();
				 setENBL_TXT();
			}
		}
		catch(Exception e)
		{
			setMSG(e,"mouseClicked");
		}
	}
	public void mouseReleased(MouseEvent L_KE)
	 {
   	super.mouseReleased(L_KE);
		try
		{ 	 
			if(L_KE.getSource().equals(chkCHKFL1))
		     {
			      if(((JCheckBox)tblTSFAL.cmpEDITR[TB2_CHKFL]).isSelected())
			      {
			    	if(tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_CHKFL).toString().equals("true"))
				   	{  
			    		/**get Failure no & Description for TSG addition,modification & deletion**/
			           strTSFNO = tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_TSFNO).toString();
			           strTSFDS = tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_TSFDS).toString();
			    	   if(strTSFNO.length()>0)
			    	   {	
			    		   /**after click on checkbox,Fetch Causes of corresponding failure no **/
			    		  getTSCDL(strTSFNO);
			    	   }
				   	}
			      }
			      else
				  {  
			    	  tblTSCAS.clrTABLE(); 
				  }
		     
			      /**Select one ro of Failure table at a time**/
			      for(int i=0;i<tblTSFAL.getRowCount();i++)
			   	  {	
				      if(tblTSFAL.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					  {
				   	     for(int j=0;j<tblTSFAL.getRowCount();j++)
				   	     {	
					   		if(tblTSFAL.getValueAt(j,TB2_CHKFL).toString().equals("true"))
					   		{
					   			if(j !=i)	
					   			{
					   				tblTSFAL.setValueAt(new Boolean(false),i,TB2_CHKFL);   
					   			}
					   	    }
				   	     }
					   }
			   	  }
		     }
		}
		catch(Exception e1)
		{
			setMSG(e1,"mouseReleased");
		}
	 }
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtEQPID"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtEQPID.setText(L_STRTKN.nextToken());
				 txtEQPDS.setText(L_STRTKN.nextToken());
				 txtEQMTP.setText(L_STRTKN.nextToken());
				 if(L_STRTKN.hasMoreTokens())
					 L_STRTKN.nextToken();
				 if(L_STRTKN.hasMoreTokens())
					 txtEQSTP.setText(L_STRTKN.nextToken());
				
			}
			else if(M_strHLPFLD.equals("txtTAGNO"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtTAGNO.setText(L_STRTKN.nextToken());
				 txtTAGDS.setText(L_STRTKN.nextToken());
				 
				 /**Display Tag record */
				// if(jtpMANTAB.getSelectedIndex()==0)
				// if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				 {
					 txtPLNCD.setText(L_STRTKN.nextToken());
					 txtARACD.setText(L_STRTKN.nextToken());
					 txtCRICD.setText(L_STRTKN.nextToken());
				 }
				
			}
			if(M_strHLPFLD.equals("txtPLNCD"))
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
			}
			else if(M_strHLPFLD.equals("txtCRICD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtCRICD.setText(L_STRTKN.nextToken());
				 txtCRIDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtEQMTP"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtEQMTP.setText(L_STRTKN.nextToken());
				 txtEQMTP_DS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtEQSTP"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtEQSTP.setText(L_STRTKN.nextToken());
				 txtEQSTP_DS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtDPTCD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtDPTCD.setText(L_STRTKN.nextToken());
				 txtDPTNM.setText(L_STRTKN.nextToken());
			}
			
			else if(M_strHLPFLD.equals("txtTSGNO_TS"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtTSGNO_TS.setText(L_STRTKN.nextToken());
				 txtTSGDS_TS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtMFRCD"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtMFRCD.setText(L_STRTKN.nextToken());
				 txtMFRNM.setText(L_STRTKN.nextToken());
				 txtADR01.setText(L_STRTKN.nextToken());
				 L_STRTKN.nextToken();
				 if(L_STRTKN.hasMoreTokens())
				 {
					 L_STRTKN.nextToken();
					 txtCTYNM.setText(L_STRTKN.nextToken());
				 }
			}
		}
		catch(Exception E_VR)
		{
			E_VR.printStackTrace();
			setMSG(E_VR,"exeHLPOK()");		
		}
	}	
	
	
	/**
	 * Method to get Data from pm_eqmst to display Equipment detail records */
	private void getEQPDL() 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Records ...",'N');
			String L_strDPTCD="",L_strMFRCD="";
		
			M_strSQLQRY= " SELECT EQ_DPTCD,EQ_TSGNO,EQ_DRGNO,EQ_MATGR,EQ_MFRCD,EQ_RNIND,EQ_ICHCD,EQ_PORNO,EQ_PORDT,EQ_PORVL,EQ_HRSRN,EQ_INSDT,EQ_MNTVL";
			M_strSQLQRY+= " FROM PM_EQMST";
			M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EQ_EQPID = '"+txtEQPID.getText()+"' AND ifnull(EQ_STSFL,'')<>'X'";
			
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 

			if(M_rstRSSET != null)
			{
				while( M_rstRSSET.next())
				{
					/*display Department Name from hstCDTRN hashtable*/
					L_strDPTCD=nvlSTRVL(M_rstRSSET.getString("EQ_DPTCD"),"");
					txtDPTCD.setText(L_strDPTCD);
					if(txtDPTCD.getText().length()>0)
						txtDPTNM.setText(getCDTRN("SYSCOXXDPT"+L_strDPTCD,"CMT_CODDS",hstCDTRN));
					
					txtTSGNO_TS.setText(nvlSTRVL(M_rstRSSET.getString("EQ_TSGNO"),""));
					/*if(hstTSGDL.containsKey(L_strTSGNO))
						L_strTSGDS = hstTSGDL.get(L_strTSGNO).toString();
					txtTSGDS_EQ.setText(L_strTSGDS);*/
					
					L_strMFRCD=nvlSTRVL(M_rstRSSET.getString("EQ_MFRCD"),"");
					txtMFRCD.setText(L_strMFRCD);
					
					txtDRGNO.setText(nvlSTRVL(M_rstRSSET.getString("EQ_DRGNO"),""));
					txtMATGR.setText(nvlSTRVL(M_rstRSSET.getString("EQ_MATGR"),""));
					txtRNIND.setText(nvlSTRVL(M_rstRSSET.getString("EQ_RNIND"),""));
					txtICHCD.setText(nvlSTRVL(M_rstRSSET.getString("EQ_ICHCD"),""));
					txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("EQ_PORNO"),""));
					if(!(M_rstRSSET.getDate("EQ_PORDT")==null))
						txtPORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EQ_PORDT")));
					txtPORVL.setText(nvlSTRVL(M_rstRSSET.getString("EQ_PORVL"),""));
					txtHRSRN.setText(nvlSTRVL(M_rstRSSET.getString("EQ_HRSRN"),""));
					if(!(M_rstRSSET.getDate("EQ_INSDT")==null))
						txtINSDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EQ_INSDT")));
					txtMNTVL.setText(nvlSTRVL(M_rstRSSET.getString("EQ_MNTVL"),""));
				}
			}
			
            M_rstRSSET.close();
            if(L_strMFRCD.length()>0)
            {
	            String L_strSQLQRY= "Select PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST where PT_PRTTP='S' ";
	            	L_strSQLQRY+= " AND PT_PRTCD='"+L_strMFRCD+"' ";
	            ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY); 
				if(L_rstRSSET != null)
				{
					while( L_rstRSSET.next())
					{
						txtMFRNM.setText(nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),""));
						txtADR01.setText(nvlSTRVL(L_rstRSSET.getString("PT_ADR01"),""));
						txtCTYNM.setText(nvlSTRVL(L_rstRSSET.getString("PT_CTYNM"),""));
					}
				}
				 L_rstRSSET.close();
            }
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getEQPDL()"); 
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	
	
	/**
	 * This Method to get Data from pm_estrn for technical specification */
	private void getEQPSP() 
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Records ...",'N');
			tblSPECF.clrTABLE();
			inlTBLEDIT(tblSPECF);
		
			M_strSQLQRY= " SELECT ES_SRLNO,ES_ATDSC,ES_SPDSC,ES_EQMTP,ES_EQSTP";
			M_strSQLQRY+= " FROM PM_ESTRN";
			M_strSQLQRY+= " where ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ES_EQPID='"+txtEQPID.getText()+"' AND ifnull(ES_STSFL,'')<>'X' ";
		    System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			L_ROWNO=0;
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					txtEQMTP.setText(nvlSTRVL(M_rstRSSET.getString("ES_EQMTP"),""));
					txtEQSTP.setText(nvlSTRVL(M_rstRSSET.getString("ES_EQSTP"),""));
					tblSPECF.setValueAt(nvlSTRVL(M_rstRSSET.getString("ES_SRLNO"),""),L_ROWNO,TB1_SRLNO);
					tblSPECF.setValueAt(nvlSTRVL(M_rstRSSET.getString("ES_ATDSC"),""),L_ROWNO,TB1_ATDSC);
					tblSPECF.setValueAt(nvlSTRVL(M_rstRSSET.getString("ES_SPDSC"),""),L_ROWNO,TB1_SPDSC);
					L_ROWNO++;
				}
			}
			
            M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getEQPSP()"); 
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * This Method to get the number of failue for the given TSG No. */
	private void getTSFDL() 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Records ...",'N');
			intTSFAL_ROW=0;
			
			//M_strSQLQRY= " SELECT distinct TS_TSGDS,TS_TSFNO,TS_TSFDS,TS_TSCNO,TS_TSCDS,TS_TSCRM";
			//M_strSQLQRY+= " FROM PM_TSMST";
			//M_strSQLQRY+= " where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TS_STSFL,'')<>'X' ";
			//M_strSQLQRY+= " AND TS_TSGNO='"+txtTSGNO_TS.getText()+"' and TS_TSFNO='"+LP_strTSFNO+"' ";
			M_strSQLQRY= " SELECT distinct TS_TSGDS,TS_TSFNO,TS_TSFDS";
			M_strSQLQRY+= " FROM PM_TSMST";
			M_strSQLQRY+= " where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TS_STSFL,'')<>'X' ";
			M_strSQLQRY+= " AND TS_TSGNO='"+txtTSGNO_TS.getText()+"'";
		
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 

			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					txtTSGDS_TS.setText(nvlSTRVL(M_rstRSSET.getString("TS_TSGDS"),""));
					tblTSFAL.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSFNO"),""),intTSFAL_ROW,TB2_TSFNO);
					tblTSFAL.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSFDS"),""),intTSFAL_ROW,TB2_TSFDS);
					intTSFAL_ROW++;
				}
			}
            M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSFDL()"); 
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**
	 * This Method to get the number of failue for the given TSG No. */
	private void getTSCDL(String LP_strTSFNO) 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			
			intTSCAS_ROW=0;
			
			M_strSQLQRY= " SELECT TS_TSCNO,TS_TSCDS,TS_TSCRM";
			M_strSQLQRY+= " FROM PM_TSMST";
			M_strSQLQRY+= " where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TS_STSFL,'')<>'X' ";
			M_strSQLQRY+= " AND TS_TSGNO='"+txtTSGNO_TS.getText()+"' and TS_TSFNO='"+LP_strTSFNO+"' ";
		
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					tblTSCAS.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSCNO"),""),intTSCAS_ROW,TB3_TSCNO);
					tblTSCAS.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSCDS"),""),intTSCAS_ROW,TB3_TSCDS);
					tblTSCAS.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSCRM"),""),intTSCAS_ROW,TB3_TSCRM);
					
					intTSCAS_ROW++;
					
				}
			}
            M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSCDL()"); 
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	void clrCOMP1()
	{
		try
		{
			txtEQPID.setText("");
			txtTAGNO.setText("");
			txtEQPDS.setText("");
			txtTAGDS.setText("");
			txtPLNCD.setText(""); 
			txtARACD.setText(""); 
			txtCRICD.setText("");
			txtPLNDS.setText(""); 
			txtARADS.setText(""); 
			txtCRIDS.setText("");
			txtEQMTP.setText("");
			txtEQMTP_DS.setText(""); 
			txtEQSTP.setText(""); 
			txtEQSTP_DS.setText("");
			
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP1()");			
		}	
	}
	/**method to clear component after click on save button  **/
	void clrCOMP()
	{
		try
		{	
			txtDRGNO.setText("");
			txtRNIND.setText("");
			txtDPTCD.setText("");
			txtDPTNM.setText("");
			txtMATGR.setText("");
			txtMFRCD.setText(""); 
			txtMFRNM.setText(""); 
			txtADR01.setText(""); 
			txtCTYNM.setText(""); 
			txtICHCD.setText(""); 
			txtPORNO.setText("");
			txtPORDT.setText(""); 
			txtPORVL.setText(""); 
			txtHRSRN.setText("");
			txtINSDT.setText(""); 
			txtMNTVL.setText("");
			
			tblSPECF.clrTABLE();
			inlTBLEDIT(tblSPECF);
		
			txtTSGNO_TS.setText("");
			txtTSGDS_TS.setText("");
			tblTSFAL.clrTABLE();
			inlTBLEDIT(tblTSFAL);
			tblTSCAS.clrTABLE();
			inlTBLEDIT(tblTSCAS);
			
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
	
	/**Validation for enter  data**/
	boolean vldDELEQ()
	{
		try
		{
			boolean L_flgCHKFL_EQ= false;
			if(chkDELEQ.isSelected())
			{
				L_flgCHKFL_EQ= true;
			
			}	
			
			if(L_flgCHKFL_EQ== false)
			{
				if(!chkDELEQ.isSelected())
				{
					setMSG("For Eqpt Detail Deletion Select 'Delete Eqpt Detail' checkbox'..",'E');
					return false;
				}
			}	

		 }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDELEQ()"); 
		}
		return true;
	}
	
	/**Validation for enter  data**/
	boolean vldDATA()
	{
		try
		{
			if(jtpEQPTAB.getSelectedIndex()!=2)
			{
				if(txtTAGNO.getText().trim().length() ==0)
		    	{
					txtTAGNO.requestFocus();
		    		setMSG("Enter Tag No..",'E');
		    		return false;
		    	}
				else if(txtEQPID.getText().trim().length() ==0)
		    	{
					txtEQPID.requestFocus();
		    		setMSG("Enter Equipment Id..",'E');
		    		return false;
		    	} 
				else if(txtEQPDS.getText().trim().length() ==0)
		    	{
					txtEQPDS.requestFocus();
		    		setMSG("Enter Equipment Id Description..",'E');
		    		return false;
		    	} 
				else if(txtEQMTP.getText().trim().length() ==0)
		    	{
					txtEQMTP.requestFocus();
		    		setMSG("Enter Equipment Main Type..",'E');
		    		return false;
		    	}
				else if(txtPLNCD.getText().trim().length() ==0)
		    	{
					txtPLNCD.requestFocus();
		    		setMSG("Enter Plant Code..",'E');
		    		return false;
		    	}
				else if(txtARACD.getText().trim().length() ==0)
		    	{
					txtARACD.requestFocus();
		    		setMSG("Enter Area Code..",'E');
		    		return false;
		    	}
				/*else if(txtCRICD.getText().trim().length() ==0)
		    	{
					txtCRICD.requestFocus();
		    		setMSG("Enter the Criticality Code..",'E');
		    		return false;
		    	}*/
			}
			
			if(jtpEQPTAB.getSelectedIndex() ==1)
			{
				boolean L_flgCHKFL= false;
				
				for(int i=0; i<tblSPECF.getRowCount(); i++)
				{	
					if(tblSPECF.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						L_flgCHKFL= true;
						break;
					}	
				}
				
				if(L_flgCHKFL== false)
				{
					if(tblSPECF.getValueAt(tblSPECF.getSelectedRow(),TB1_SRLNO).toString().length()>0)
					{
						setMSG("No row Selected in Technical Specification..",'E');
						return false;
					}
				}	
			}
			if(jtpEQPTAB.getSelectedIndex() ==2)
			{
			 	boolean L_flgCHKFL1= false;
				boolean L_flgCHKFL2= false;
				/**for Failure table row selection**/
				for(int j=0;j<tblTSFAL.getRowCount();j++)
				{
					for(int k=0;k<tblTSCAS.getRowCount();k++)
					{
						if(tblTSFAL.getValueAt(j,TB2_CHKFL).toString().equals("true"))  
						{
							L_flgCHKFL1= true;
						}
					}
				}
				if(L_flgCHKFL1== false)
				{
					if(tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_TSFNO).toString().length()>0)
					{
						setMSG("No row Selected in Failure table of Trouble Shooting Guide..",'E');				
						 return false;
					}
				}	
				/**for Cause table row selection**/
				for(int j=0;j<tblTSFAL.getRowCount();j++)
				{
					for(int k=0;k<tblTSCAS.getRowCount();k++)
					{
						if( tblTSCAS.getValueAt(k,TB3_CHKFL).toString().equals("true"))
						{
							L_flgCHKFL2= true;
						}
					}
				}
				if(L_flgCHKFL2== false)
				{
					if(tblTSCAS.getValueAt(tblTSCAS.getSelectedRow(),TB3_TSCNO).toString().length()>0)
					{
						setMSG("No row Selected in Cause Table of Trouble Shooting Guide..",'E');				
						 return false;
					}
				}		
			}
			
	    	for(int P_intROWNO=0;P_intROWNO<tblSPECF.getRowCount();P_intROWNO++)
			{
				if(tblSPECF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					if(tblSPECF.getValueAt(P_intROWNO,TB1_ATDSC).toString().length()>0 && tblSPECF.getValueAt(P_intROWNO,TB1_SRLNO).toString().length()==0)
					{
			    		setMSG("Enter Serial No..",'E');
			    		return false;
					}
					if(tblSPECF.getValueAt(P_intROWNO,TB1_ATDSC).toString().length()==0)
					{
			    		setMSG("Enter Attribute Description..",'E');
			    		return false;
					}
					
					if(tblSPECF.getValueAt(P_intROWNO,TB1_ATDSC).toString().length()>0 && tblSPECF.getValueAt(P_intROWNO,TB1_SPDSC).toString().length()==0)
					{
			    		setMSG("Enter Specification",'E');
			    		return false;
					}
					
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && tblSPECF.getValueAt(P_intROWNO,TB1_SRLNO).toString().length()>0 )
				{
					M_strSQLQRY = "select ES_SRLNO FROM PM_ESTRN";
					M_strSQLQRY+=" where ifnull(ES_STSFL,'') <> 'X' and ES_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' ";
					M_strSQLQRY += " AND ES_EQPID = '"+txtEQPID.getText().toString()+"'";
					//System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);						
		            if(M_rstRSSET!=null)
		            {
		            	while(M_rstRSSET.next())
		            	{
		            		if(nvlSTRVL(M_rstRSSET.getString("ES_SRLNO"),"").equals(tblSPECF.getValueAt(P_intROWNO,TB1_SRLNO).toString()))
							{
								setMSG("The Serial No of Attribute "+tblSPECF.getValueAt(P_intROWNO,TB1_ATDSC)+" Already exist",'E');
								return false;
							}
		            	}
		            }
		            M_rstRSSET.close();
				}
	    	}
	    	for(int P_intROWNO=0;P_intROWNO<tblTSFAL.getRowCount();P_intROWNO++)
			{
				if(tblTSFAL.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
				{
					if(tblTSFAL.getValueAt(P_intROWNO,TB2_TSFDS).toString().length()>0 && tblTSFAL.getValueAt(P_intROWNO,TB2_TSFNO).toString().length()==0)
					{
			    		setMSG("Enter Failure Code",'E');
			    		return false;
					}
					if(tblTSFAL.getValueAt(P_intROWNO,TB2_TSFDS).toString().length()==0)
					{
			    		setMSG("Enter Failure Description",'E');
			    		return false;
					}
				/*	for(int i=0;i<tblTSFAL.getRowCount();i++)
					{
						if(tblTSFAL.getValueAt(i,TB2_CHKFL).toString().equals("true"))
						{
							if(i!=P_intROWNO)
							{
								setMSG("Please Select One Row at a Time",'E');
								return false;
							}
							
						}
					}	*/	
				}
				
	    	}
			for(int P_intROWNO=0;P_intROWNO<tblTSCAS.getRowCount();P_intROWNO++)
			{
				if(tblTSCAS.getValueAt(P_intROWNO,TB3_CHKFL).toString().equals("true"))
				{
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCDS).toString().length()>0 && tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString().length()==0)
					{
			    		setMSG("Enter Cause Code",'E');
			    		return false;
					}
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCDS).toString().length()==0)
					{
			    		setMSG("Enter Cause Description",'E');
			    	   return false;
					}
					
				}
	    	}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATA()");		
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
				//cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			
			 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
			{
				if(txtEQPID.getText().length()>0)
					exeADDEDL();
				
				for(P_intROWNO=0;P_intROWNO<tblSPECF.getRowCount();P_intROWNO++)
				{
					if(tblSPECF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeADDESP(P_intROWNO);
					}
				}
				
				 for(P_intROWNO=0;P_intROWNO<tblTSCAS.getRowCount();P_intROWNO++)
				 {
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_CHKFL).toString().equals("true"))
					{
						exeADDTSG(P_intROWNO);
					}
				 }
	    			
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
				if(txtEQPID.getText().length()>0)
					exeMODEDL();
				
				for(P_intROWNO=0;P_intROWNO<tblSPECF.getRowCount();P_intROWNO++)
				{
					if(tblSPECF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeMODESP(P_intROWNO);
					}
				}
				
				 for(P_intROWNO=0;P_intROWNO<tblTSCAS.getRowCount();P_intROWNO++)
				 {
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_CHKFL).toString().equals("true"))
					{
				    	exeMODTSG(P_intROWNO);
					}
				 }
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
				if(jtpEQPTAB.getSelectedIndex() ==0)
				{
					if(!vldDELEQ())
						return;
				}
				if(txtEQPID.getText().length()>0)
				{
					exeDELEDL();
				}
				for(P_intROWNO=0;P_intROWNO<tblSPECF.getRowCount();P_intROWNO++)
				{
					if(tblSPECF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeDELESP(P_intROWNO);
					}	
				}
			
				for(P_intROWNO=0;P_intROWNO<tblTSCAS.getRowCount();P_intROWNO++)
				 {
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_CHKFL).toString().equals("true"))
					{
				    	exeDELTSG(P_intROWNO);
					}
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
				clrCOMP1();
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
	} 
	
	 /** Method to insert records in pm_eqmst(Equipment Detail record) table.  */
	  private void exeADDEDL()
	  { 
		  try
		  {
			String strSQLQRY=" select count(*) from PM_EQMST where";
			strSQLQRY+=" EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ifnull(EQ_STSFL,'')<>'X' AND EQ_EQPID='"+txtEQPID.getText().toUpperCase()+"'";
			ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			//System.out.println(">>>Count>>"+M_strSQLQRY);
			if(rstRSSET.next() && rstRSSET != null)
			{
				if(rstRSSET.getInt(1)>0)
				{
				   exeMODEDL();
				}
				else
				{
					
					this.setCursor(cl_dat.M_curWTSTS_pbst);
				    cl_dat.M_flgLCUPD_pbst = true;
				    M_strSQLQRY =" insert into PM_EQMST (EQ_TAGNO,EQ_EQPID,EQ_EQPDS,EQ_EQMTP,EQ_EQSTP,EQ_PLNCD,EQ_ARACD,EQ_DPTCD,EQ_TSGNO,EQ_DRGNO,EQ_RNIND,EQ_MATGR,EQ_MFRCD,EQ_ICHCD,EQ_PORNO,EQ_PORDT,EQ_PORVL,EQ_HRSRN,EQ_INSDT,EQ_MNTVL,"
					+"EQ_TRNFL,EQ_STSFL,EQ_LUSBY,EQ_LUPDT,EQ_CMPCD,EQ_SBSCD)"
					+"  VALUES('" + txtTAGNO.getText().toString()+"',";
			  	
			  		M_strSQLQRY += "'"+txtEQPID.getText().toString()+"',";
					M_strSQLQRY += "'"+txtEQPDS.getText().toString()+"',";
					M_strSQLQRY += "'"+txtEQMTP.getText().toString()+"',";
					M_strSQLQRY += "'"+txtEQSTP.getText().toString()+"',";
					M_strSQLQRY += "'"+txtPLNCD.getText().toString()+"',";
					M_strSQLQRY += "'"+txtARACD.getText().toString()+"',";
					M_strSQLQRY += "'"+txtDPTCD.getText().toString()+"',";
					M_strSQLQRY += "'"+txtTSGNO_TS.getText().toString()+"',";
					M_strSQLQRY += "'"+txtDRGNO.getText().toString()+"',";
					M_strSQLQRY += "'"+txtRNIND.getText().toString()+"',";
					M_strSQLQRY += "'"+txtMATGR.getText().toString()+"',";
					M_strSQLQRY += "'"+txtMFRCD.getText().toString()+"',";
					M_strSQLQRY += "'"+txtICHCD.getText().toString()+"',";
					M_strSQLQRY += "'"+txtPORNO.getText().toString()+"',";
					
					if(txtPORDT.getText().length()>0)
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPORDT.getText().toString()))+"',";
					else 
						  M_strSQLQRY += "null,"; 
					if(txtPORVL.getText().length()>0)
						M_strSQLQRY += "'"+txtPORVL.getText().toString()+"',";
					else 
						  M_strSQLQRY += "0,"; 
					if(txtHRSRN.getText().length()>0)
						M_strSQLQRY += "'"+txtHRSRN.getText().toString()+"',";
					else 
						  M_strSQLQRY += "0,"; 
					if(txtINSDT.getText().length()>0)
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINSDT.getText().toString()))+"',";
					else 
						  M_strSQLQRY += "null,"; 
					if(txtPORVL.getText().length()>0)
					if(txtMNTVL.getText().length()>0)
						M_strSQLQRY += "'"+txtMNTVL.getText().toString()+"',";
					else 
						  M_strSQLQRY += "0,"; 
					
					M_strSQLQRY += "'0',";
					M_strSQLQRY += "'0',";
					M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
					M_strSQLQRY += "'"+M_strSBSCD+"')";
					
					System.out.println(">>>Insert>>"+ M_strSQLQRY );
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false; 
			  cl_dat.exeDBCMT("exeADDEDL");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDEDL()"); 
		  }
	  }

	  
	 /** Method to insert records in pm_estrn(Technical Specification) table.  */
	  private void exeADDESP(int P_intROWNO)
	  { 
		  try
		  {
			   // String strSQLQRY=" select count(*) from PM_ESTRN where";
				//strSQLQRY+=" ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ifnull(ES_STSFL,'')<>'X' AND ES_EQPID='"+txtEQPID.getText().toString()+"'";
				//ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count>>"+M_strSQLQRY);
				/*if(rstRSSET.next() && rstRSSET != null)
				{
					if(rstRSSET.getInt(1)>0)
					{
						if(txtEQPID.getText().toString().length()>0)
							exeMODESP(P_intROWNO);
					}
					else
					{*/
						this.setCursor(cl_dat.M_curWTSTS_pbst);
					    cl_dat.M_flgLCUPD_pbst = true;
						M_strSQLQRY =" insert into PM_ESTRN (ES_EQPID,ES_EQMTP,ES_EQSTP,ES_SRLNO,ES_ATDSC,ES_SPDSC,ES_TRNFL,ES_STSFL,ES_LUSBY,ES_LUPDT,ES_CMPCD,ES_SBSCD)";
						M_strSQLQRY += " values (";
						M_strSQLQRY += "'"+txtEQPID.getText().toString()+"',";
						M_strSQLQRY += "'"+txtEQMTP.getText().toString()+"',";
						M_strSQLQRY += "'"+txtEQSTP.getText().toString()+"',";
						M_strSQLQRY += "'"+tblSPECF.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"',";
						M_strSQLQRY += "'"+tblSPECF.getValueAt(P_intROWNO,TB1_ATDSC).toString()+"',";
						M_strSQLQRY += "'"+tblSPECF.getValueAt(P_intROWNO,TB1_SPDSC).toString()+"',";
					
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
						M_strSQLQRY += "'"+M_strSBSCD+"')";
						
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						System.out.println("insert"+M_strSQLQRY);
					//}
				//}
			
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false; 
			  cl_dat.exeDBCMT("exeADDESP");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDESP()");
		  }
	  }
	  
	  /** Method to insert records in pm_tsmst(Trouble Shooting Guide) table.  */
	  private void exeADDTSG(int P_intROWNO)
	  { 
		  try
		  {
			   // String strSQLQRY=" select count(*) from PM_TSMST where";
				//strSQLQRY+=" TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ifnull(TS_STSFL,'')<>'X' AND TS_TSGNO='"+txtTSGNO_TS.getText().toString()+"'";
				//ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count>>"+M_strSQLQRY);
				//if(rstRSSET.next() && rstRSSET!= null)
				//{
					//if(rstRSSET.getInt(1)>0)
					//{
					//	if(txtTSGNO_TS.getText().toString().length()>0)
					//		exeMODTSG(P_intROWNO);
					//}
					//else
					//{
			
				  		this.setCursor(cl_dat.M_curWTSTS_pbst);
				  		cl_dat.M_flgLCUPD_pbst = true;
				  		M_strSQLQRY =" insert into PM_TSMST (TS_TSGNO,TS_TSGDS,TS_TSFNO,TS_TSFDS,TS_TSCNO,TS_TSCDS,TS_TSCRM,TS_TRNFL,TS_STSFL,TS_LUSBY,TS_LUPDT,TS_CMPCD,TS_SBSCD)";
						M_strSQLQRY += " values (";
						M_strSQLQRY += "'"+txtTSGNO_TS.getText().toString()+"',";
						M_strSQLQRY += "'"+txtTSGDS_TS.getText().toString()+"',";
						
				  	    M_strSQLQRY += "'"+strTSFNO+"',";
						M_strSQLQRY += "'"+strTSFDS+"',";
						
				  		M_strSQLQRY += "'"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString()+"',";
						M_strSQLQRY += "'"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCDS).toString()+"',";
						M_strSQLQRY += "'"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCRM).toString()+"',";
				    	
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
						M_strSQLQRY += "'"+M_strSBSCD+"')";
						
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						System.out.println("insert"+M_strSQLQRY);
					//}
				//}
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false;
			  cl_dat.exeDBCMT("exeADDTSG");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDTSG()");
		  }
	  }
	  
	  
	  /**Method to Modify Records of pm_eqmst(Equipment Detail records)**/
	  private void exeMODEDL() 
		{
		    try
		    {
	    	    M_strSQLQRY = " Update PM_EQMST set";
		    	M_strSQLQRY += " EQ_PLNCD='"+txtPLNCD.getText().toString()+"',";
		    	M_strSQLQRY += " EQ_ARACD='"+txtARACD.getText().toString()+"',";
		    	if(txtEQPDS.getText().length()>0)
		    		M_strSQLQRY += " EQ_EQPDS='"+txtEQPDS.getText().toString()+"',";
		    	if(txtEQMTP.getText().length()>0)
		    		M_strSQLQRY += " EQ_EQMTP='"+txtEQMTP.getText().toString()+"',";
		    	if(txtEQSTP.getText().length()>0)
		    		M_strSQLQRY += " EQ_EQSTP='"+txtEQSTP.getText().toString()+"',";
		    	if(txtDPTCD.getText().length()>0)
		    		M_strSQLQRY += " EQ_DPTCD='"+txtDPTCD.getText().toString()+"',";
		    	if(txtTSGNO_TS.getText().length()>0)
		    		M_strSQLQRY += " EQ_TSGNO='"+txtTSGNO_TS.getText().toString()+"',";
		    	if(txtDRGNO.getText().length()>0)
		    		M_strSQLQRY += " EQ_DRGNO='"+txtDRGNO.getText().toString()+"',";
		    	if(txtRNIND.getText().length()>0)
		    		M_strSQLQRY += " EQ_RNIND='"+txtRNIND.getText().toString()+"',";
		    	if(txtMATGR.getText().length()>0)
		    		M_strSQLQRY += " EQ_MATGR='"+txtMATGR.getText().toString()+"',";
		    	if(txtMFRCD.getText().length()>0)
		    		M_strSQLQRY += " EQ_MFRCD='"+txtMFRCD.getText().toString()+"',";
		    	if(txtICHCD.getText().length()>0)
		    		M_strSQLQRY += " EQ_ICHCD='"+txtICHCD.getText().toString()+"',";
		    	if(txtPORNO.getText().length()>0)
		    		M_strSQLQRY += " EQ_PORNO='"+txtPORNO.getText().toString()+"',";
		    	if(txtPORDT.getText().length()>0)
		    		M_strSQLQRY += " EQ_PORDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtPORDT.getText().toString()))+"',";
		    	if(txtPORVL.getText().length()>0)
		    		M_strSQLQRY += " EQ_PORVL='"+txtPORVL.getText().toString()+"',";
		    	if(txtHRSRN.getText().length()>0)
		    		M_strSQLQRY += " EQ_HRSRN='"+txtHRSRN.getText().toString()+"',";
		    	if(txtINSDT.getText().length()>0)
		    		M_strSQLQRY += " EQ_INSDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtINSDT.getText().toString()))+"',";
		    	if(txtMNTVL.getText().length()>0)
		    		M_strSQLQRY += " EQ_MNTVL='"+txtMNTVL.getText().toString()+"',";
				M_strSQLQRY += " EQ_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += " EQ_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY += " where EQ_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
				M_strSQLQRY += " AND EQ_EQPID= '"+txtEQPID.getText().toString()+"'";
		    	
				System.out.println(">>>update>>"+M_strSQLQRY);  
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			 }
		    catch(Exception L_EX)
		    {
		        setMSG(L_EX,"exeMODREC()");
		    }
		}
		
	/** Method to modify Records of PM_EQTRN (Technical Specification) table.
	*/
	private void exeMODESP(int P_intROWNO) 
	{
	    try
	    {
	    	M_strSQLQRY = " Update PM_ESTRN set";
	    	if(tblSPECF.getValueAt(P_intROWNO,TB1_SPDSC).toString().length()>0)
	    		M_strSQLQRY += " ES_ATDSC='"+tblSPECF.getValueAt(P_intROWNO,TB1_ATDSC).toString()+"',";
			if(tblSPECF.getValueAt(P_intROWNO,TB1_SPDSC).toString().length()>0)
				M_strSQLQRY += " ES_SPDSC='"+tblSPECF.getValueAt(P_intROWNO,TB1_SPDSC).toString()+"',";
			if(txtEQMTP.getText().length()>0)
	    		M_strSQLQRY += " ES_EQMTP='"+txtEQMTP.getText().toString()+"',";
	    	if(txtEQSTP.getText().length()>0)
	    		M_strSQLQRY += " ES_EQSTP='"+txtEQSTP.getText().toString()+"',";
	    	if(txtDPTCD.getText().length()>0)
			M_strSQLQRY += " ES_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += " ES_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where ES_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " AND ES_EQPID= '"+txtEQPID.getText().toString()+"'";
			M_strSQLQRY += " AND  ES_SRLNO='"+tblSPECF.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"'";
				
			System.out.println(">>>update>>"+M_strSQLQRY);  
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		 }
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODESP()");
	    }
	}
	
	/** Method to modify Records of PM_TSMST(Trouble Shooting Guide) table.
	*/
	private void exeMODTSG(int P_intROWNO) 
	{
	    try
	    {
	    	M_strSQLQRY = " Update PM_TSMST set";
			M_strSQLQRY += " TS_TSFDS='"+tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_TSFDS).toString()+"',";
	    
			if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCDS).toString().length()>0)
				M_strSQLQRY += " TS_TSCDS='"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCDS).toString()+"',";
	    	if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCRM).toString().length()>0)
	    		M_strSQLQRY += " TS_TSCRM='"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCRM).toString()+"',";
	    		
			M_strSQLQRY += " TS_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += " TS_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where TS_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " AND TS_TSGNO= '"+txtTSGNO_TS.getText().toString()+"'";
			M_strSQLQRY += " AND TS_TSFNO='"+strTSFNO+"'";
			if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString().length()>0)
				M_strSQLQRY += " AND TS_TSCNO='"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString()+"'";
			
			System.out.println(">>>update>>"+M_strSQLQRY);  
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		 }
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODTSG()");
	    }
	}
	
	
	/**
	 * Delete Records From PM_EQMST Table*/
	private void exeDELEDL() 
	{ 
	  try
	  {
	  		M_strSQLQRY = "UPDATE PM_EQMST SET";	
			M_strSQLQRY +="	EQ_STSFL='X'";	
			M_strSQLQRY +=" where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY +=" AND EQ_EQPID='"+txtEQPID.getText().toString()+"'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			System.out.println(">>>Delete>>"+M_strSQLQRY);
	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELEQP()");		
	  }
	}
	
	/**
	 * Delete Records From PM_EQTRN Table*/
	private void exeDELESP(int P_intROWNO) 
	{ 
	  try
	  {		
	  		M_strSQLQRY = "UPDATE PM_ESTRN SET";	
			M_strSQLQRY +=" ES_STSFL='X'";	
			M_strSQLQRY +=" where ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " AND ES_EQPID= '"+txtEQPID.getText().toString()+"' ";
			M_strSQLQRY += " AND  ES_SRLNO='"+tblSPECF.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"'";
			
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			System.out.println(">>>Delete>>"+M_strSQLQRY);
	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELESP()");		
	  }
	}
	
	
		
	/**
	 * Delete Records From PM_TSMST Table*/
	private void exeDELTSG(int P_intROWNO) 
	{ 
	  try
	  {
	  		M_strSQLQRY = "UPDATE PM_TSMST SET";	
			M_strSQLQRY +=" TS_STSFL='X'";	
			M_strSQLQRY += " where TS_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
			
			M_strSQLQRY += " AND TS_TSGNO= '"+txtTSGNO_TS.getText().toString()+"'";
			M_strSQLQRY += " AND TS_TSFNO='"+strTSFNO+"'";
	    	M_strSQLQRY += " AND TS_TSCNO='"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString()+"'";
	    
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			System.out.println(">>>Delete>>"+M_strSQLQRY);
	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELTSG()");		
	  }
	}
	
	
	void inlTBLEDIT(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
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
	        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp||cmt_cgstp in ("+LP_CATLS+")   "+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
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
                   // System.out.println("LP_HSTNM"+LP_HSTNM);
		
			//hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
			
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
	 

	/**Verification for enter valid record***/
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				String L_strEQPID_DL="";
				
				if(input == txtTAGNO)
				{	
					try{
						if(jtpEQPTAB.getSelectedIndex()!=2 && txtTAGNO.getText().length()==0)
						{
							setMSG("Enter Tag No..",'E');
							txtTAGNO.requestFocus();
							return false;
						}
						if(txtTAGNO.getText().length()>0)
						{
							M_strSQLQRY=" SELECT distinct TG_TAGNO,TG_TAGDS,TG_PLNCD,TG_ARACD,TG_CRICD from PM_TGMST where TG_TAGNO='"+txtTAGNO.getText().toUpperCase()+"'";
							M_strSQLQRY+= " AND TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TG_STSFL,'')<>'X'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtTAGNO.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGNO"),""));
								txtTAGDS.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGDS"),""));
								txtPLNCD.setText(nvlSTRVL(M_rstRSSET.getString("TG_PLNCD"),""));
								txtARACD.setText(nvlSTRVL(M_rstRSSET.getString("TG_ARACD"),""));
								txtCRICD.setText(nvlSTRVL(M_rstRSSET.getString("TG_CRICD"),""));
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
				}
				
				
				else if(input == txtEQPID)
				{	
					try
					{
						if(jtpEQPTAB.getSelectedIndex()!=2 && txtEQPID.getText().length()==0)
						{
							setMSG("Enter Equipment Id..",'E');
							txtEQPID.requestFocus();
							return false;
						}
						
						if(txtEQPID.getText().length()>0)
						{
							txtEQPID.setText(txtEQPID.getText().toUpperCase());
							String strEQPID=txtEQPID.getText().toString();
				
						/**Verification to Eqpt Id of Eqpt Detail**/
							
							M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS,EQ_EQMTP,EQ_EQSTP from PM_EQMST  ";
							M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(EQ_STSFL,'')<>'X' AND EQ_EQPID ='"+txtEQPID.getText().toUpperCase()+"' ";
							if(txtTAGNO.getText().length()>0)
								M_strSQLQRY+=" and EQ_TAGNO='"+txtTAGNO.getText()+"' ";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								L_strEQPID_DL=nvlSTRVL(M_rstRSSET.getString("EQ_EQPID"),"");
								txtEQPID.setText(L_strEQPID_DL);
								txtEQPDS.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQPDS"),""));
								txtEQMTP.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQMTP"),""));
								txtEQSTP.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQSTP"),""));
								
							}
							else
							{
								if(jtpEQPTAB.getSelectedIndex()==2 ||  !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && jtpEQPTAB.getSelectedIndex()==0
								|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)  && jtpEQPTAB.getSelectedIndex()==1 )
								{
									setMSG("Enter valid Equipment Id ",'E');
									txtEQPID.requestFocus();
									return false;
								}
							}
							M_rstRSSET.close();
						
							if(jtpEQPTAB.getSelectedIndex()==0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
							if(strEQPID.equals(L_strEQPID_DL))
							{
								setMSG("This Equipment Id Already exist",'E');
								txtEQPID.requestFocus();
								return false;
							}
							
							/**Verification for Eqpt Id of Technical Specification**/
							if(jtpEQPTAB.getSelectedIndex()==1 && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
							{
								String M_strSQLQRY1=" SELECT EQ_EQPID,EQ_EQPDS,EQ_EQMTP,EQ_EQSTP from PM_ESTRN,PM_EQMST "; 
								M_strSQLQRY1+= " where ES_CMPCD=EQ_CMPCD AND ES_EQPID=EQ_EQPID AND ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(ES_STSFL,'')<>'X' AND EQ_EQPID='"+txtEQPID.getText().toUpperCase()+"'";
							
								if(txtTAGNO.getText().length()>0)
									M_strSQLQRY1+=" and EQ_TAGNO='"+txtTAGNO.getText()+"' ";
								//System.out.println("M_strSQLQRY>>"+M_strSQLQRY1);
								
								ResultSet M_rstRSSET1=cl_dat.exeSQLQRY(M_strSQLQRY1);
								
								if(M_rstRSSET1.next() && M_rstRSSET1!=null)
								{
									txtEQPID.setText(nvlSTRVL(M_rstRSSET1.getString("EQ_EQPID"),""));
									txtEQPDS.setText(nvlSTRVL(M_rstRSSET1.getString("EQ_EQPDS"),""));
									txtEQMTP.setText(nvlSTRVL(M_rstRSSET1.getString("EQ_EQMTP"),""));
									txtEQSTP.setText(nvlSTRVL(M_rstRSSET1.getString("EQ_EQSTP"),""));
								}
								else
								{
									setMSG("Enter valid Equipment Id for Technical Specification",'E');
									txtEQPID.requestFocus();
									return false;
								}
								M_rstRSSET1.close();
							}
							
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Equipment id  InputVerifier ");
					}
				}
				
				if(((JTextField)input).getText().length() == 0)
					return true;
				else if(input == txtEQMTP)
				{	
					try{
						txtEQMTP.setText(txtEQMTP.getText().toUpperCase());
						if(txtEQMTP.getText().length()>0)
						{
							if(!hstCDTRN.containsKey("MSTPMXXEQT"+txtEQMTP.getText().toString().toUpperCase()))
							{
								setMSG("Enter Valid Equipment Main Type ",'E');
								return false;
							}
							else
								txtEQMTP_DS.setText(getCDTRN("MSTPMXXEQT"+txtEQMTP.getText().toString().toUpperCase(),"CMT_CODDS",hstCDTRN));
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error txtEQMTP InputVerifier ");
					}
				}
				
				else if(input == txtEQSTP)
				{	
					try
					{
						txtEQSTP.setText(txtEQSTP.getText().toUpperCase());
						if(txtEQSTP.getText().length()>0)
						{
							if(!hstCDTRN.containsKey("MSTPMXXEST"+txtEQSTP.getText().toString().toUpperCase()))
							{
								setMSG("Enter Valid Equipment Sub Type ",'E');
								return false;
							}
							else
								txtEQSTP_DS.setText(getCDTRN("MSTPMXXEST"+txtEQSTP.getText().toString().toUpperCase(),"CMT_CODDS",hstCDTRN));
							
						}	
					}
					catch(Exception e)
					{
						setMSG(e,"error in  Equipment Sub Type InputVerifier");
					}
				}
				
				else if(input == txtPLNCD)
				{	
					try{
						txtPLNCD.setText(txtPLNCD.getText().toUpperCase());
						if(!hstCDTRN.containsKey("MSTPMXXPLN"+txtPLNCD.getText().toString().trim()))
						{
							setMSG("Enter Valid  Plant Code ",'E');
							return false;
						}
						else
							txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
					}
					catch(Exception e)
					{
						setMSG(e,"error in Plant Code InputVerifier ");
					}

				}
				else if(input == txtARACD)
				{	
					try{
						
						if(!hstCDTRN.containsKey("MSTPMXXARA"+txtARACD.getText().toString().trim()))
						{
							setMSG("Enter Valid Area Code ",'E');
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
				else if(input == txtCRICD)
				{	
					try{
						txtCRICD.setText(txtCRICD.getText().toUpperCase());
						if(txtCRICD.getText().length()>0)
						{
							if(!hstCDTRN.containsKey("MSTPMXXCRT"+txtCRICD.getText().toString().trim()))
							{
								setMSG("Enter Valid Criticality Code ",'E');
								return false;
							}
							else
								txtCRIDS.setText(getCDTRN("MSTPMXXCRT"+txtCRICD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Criticality Code InputVerifier");
					}

				}
				else if(input == txtDPTCD)
				{ 
					if(txtDPTCD.getText().length()>0)
					{
						if(!hstCDTRN.containsKey("SYSCOXXDPT"+txtDPTCD.getText().toString().trim()))
						{
							setMSG("Enter Valid Department Code ",'E');
							return false;
						}
						else
							txtDPTNM.setText(getCDTRN("SYSCOXXDPT"+txtDPTCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
					}	
				}
				
				/*if(input == txtTSGNO_EQ)
				{	
					try
					{
						if(txtTSGNO_EQ.getText().length()>0 && jtpEQPTAB.getSelectedIndex()==0 )
						{
							 if(hstTSGDL.containsKey(txtTSGNO_EQ.getText().toString().trim()))
							 {
							   txtTSGNO_EQ.setText(txtTSGNO_EQ.getText());
							   txtTSGDS_EQ.setText(hstTSGDL.get(txtTSGNO_EQ.getText().toString()));
							 }
							 else
							 {
								setMSG("Enter Valid T.S.G No ",'E');
								return false;
							 }
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in T.S.G_EQ NO InputVerifier  ");
					}
				}*/
				else if(input == txtTSGNO_TS)
				{	
					try
					{	
						txtTSGNO_TS.setText(txtTSGNO_TS.getText().toUpperCase());
						if(txtTSGNO_TS.getText().length()>0 && jtpEQPTAB.getSelectedIndex()==2)
			    		{
							M_strSQLQRY=" Select TS_TSGNO,TS_TSGDS from PM_TSMST where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ifnull(TS_STSFL,'')<>'X'";
							M_strSQLQRY+= " AND TS_TSGNO ='"+txtTSGNO_TS.getText().toUpperCase()+"' ";
						
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtTSGNO_TS.setText(txtTSGNO_TS.getText());
								txtTSGNO_TS.setText(nvlSTRVL(M_rstRSSET.getString("TS_TSGNO"),""));
								tblTSFAL.clrTABLE();
								inlTBLEDIT(tblTSFAL);
								tblTSCAS.clrTABLE();
								inlTBLEDIT(tblTSCAS);
								getTSFDL();
							}	
							else
							{
								if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
								{
									setMSG("Enter valid T.S.G No ",'E');
									txtTSGNO_TS.requestFocus();
									return false;
								}
							}
							M_rstRSSET.close();
						}
						
					}
					catch(Exception e)
					{
						setMSG(e,"error in T.S.G_TS NO InputVerifier  ");
					}
				}
				
				else if(input == txtMFRCD)
				{	
					try{
						
						M_strSQLQRY=" SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_CTYNM from CO_PTMST ";
						M_strSQLQRY += "WHERE PT_PRTTP='S' AND PT_PRTCD ='"+txtMFRCD.getText().toUpperCase()+"'";
					
						//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							txtMFRCD.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),""));
							txtMFRNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
							txtADR01.setText(nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""));
							txtCTYNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_CTYNM"),""));
						}
						else
						{
							setMSG("Enter valid Mfr.Code ",'E');
							return false;
						}
						M_rstRSSET.close();
					}
					catch(Exception e)
					{
						setMSG(e,"error in Mfr.Code InputVerifier");
					}

				}
				else if(input == txtDRGNO)
				{	
					if(txtDRGNO.getText().length()>0)
						txtDRGNO.setText(txtDRGNO.getText().toUpperCase());
				}
				else if(input == txtPORNO)
				{	
					if(txtPORNO.getText().length()>0)
						txtPORNO.setText(txtPORNO.getText().toUpperCase());
				}
				else if(input == txtMATGR)
				{	
					if(txtMATGR.getText().length()>0)
						txtMATGR.setText(txtMATGR.getText().toUpperCase());
				}
				else if(input == txtRNIND)
				{	
					if(txtRNIND.getText().length()>0)
						txtRNIND.setText(txtRNIND.getText().toUpperCase());
				}
				else if(input == txtICHCD)
				{	
					if(txtICHCD.getText().length()>0)
						txtICHCD.setText(txtICHCD.getText().toUpperCase());
				}
				
		}
		catch(Exception e)
		{
			setMSG(e, "in class INPVF");
		}
		return true;
	}
}
	private class TBLINPVF extends TableInputVerifier
	  {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
		   	try
		   	{
		   		if(getSource()==tblSPECF)
			    {
		   			try
		   			{
						if(P_intCOLID == TB1_ATDSC)
	    			    {
							if(tblSPECF.getValueAt(P_intROWID,TB1_ATDSC).toString().length()>0)
								tblSPECF.setValueAt(tblSPECF.getValueAt(P_intROWID,TB1_ATDSC).toString().replace("'","`"),P_intROWID,TB1_ATDSC);
							
	    			    }
						else if(P_intCOLID == TB1_SPDSC)
	    			    {
							if(tblSPECF.getValueAt(P_intROWID,TB1_SPDSC).toString().length()>0)
								tblSPECF.setValueAt(tblSPECF.getValueAt(P_intROWID,TB1_SPDSC).toString().replace("'","`"),P_intROWID,TB1_SPDSC);
	    			    }
					
				    }
					catch(Exception e)
					{
						setMSG(e,"error in tblSPECF InputVerifier  ");
					}
			    }
			
				/*	if(P_intCOLID == TB2_CHKFL)
    			    {
				   	   if(((JCheckBox)tblTSFAL.cmpEDITR[TB2_CHKFL]).isSelected())
					   {
				   	     for(int i=0;i<tblTSFAL.getRowCount();i++)
				   	     {	
					   		if(tblTSFAL.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					   		{
					   			if(i !=P_intROWID)	
					   			{
					   				tblTSFAL.setValueAt(new Boolean(false),P_intROWID,TB2_CHKFL);  
					   				
					   			}
					   	    }
				   	     }
					   } 
    			    }*/
		   		else if(getSource()==tblTSFAL)
			    {
					try
					{
						if(P_intCOLID == TB2_TSFNO)
	    			    {
					   	     for(int j=0;j<tblTSFAL.getRowCount();j++)
					   	     {	
						   		//if(tblTSFAL.getValueAt(j,TB2_CHKFL).toString().equals("true"))
						   		{
						   			if(tblTSFAL.getValueAt(P_intROWID,TB2_TSFNO).toString().length()>0 && tblTSFAL.getValueAt(j,TB2_TSFNO).toString().length()>0 && j !=P_intROWID)
						   			if(tblTSFAL.getValueAt(P_intROWID,TB2_TSFNO).toString().compareTo(tblTSFAL.getValueAt(j,TB2_TSFNO).toString().trim())==0)
									{
										setMSG("This Failure No Already Exist in the table for "+txtTSGNO_TS.getText()+" T.S.G. No..",'E');
										return false;
									}
						   	    }
					   	     }
					   	  
	    			    }
						if(P_intCOLID == TB2_TSFDS)
	    			    {
					   	   if(tblTSFAL.getValueAt(P_intROWID,TB2_TSFDS).toString().length()>0)
						   {
					   		   tblTSFAL.setValueAt(tblTSFAL.getValueAt(P_intROWID,TB2_TSFDS).toString().replace("'","`"),P_intROWID,TB2_TSFDS);
					   	   } 
					   	  
				   		   if(tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_CHKFL).toString().equals("true"))
				   		   {  
				   			   strTSFNO = tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_TSFNO).toString();
				   			   strTSFDS = tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_TSFDS).toString();
				   		   }
	    			    }
					}
					catch(Exception e)
					{
						setMSG(e,"error in tblTSFAL InputVerifier  ");
					}
			    }
		   		else if(getSource()==tblTSCAS)
			    {
		   			try
		   			{
			   			if(P_intCOLID == TB3_TSCNO)
	    			    {
					   	     for(int j=0;j<tblTSCAS.getRowCount();j++)
					   	     {	
						   		//if(tblTSFAL.getValueAt(j,TB2_CHKFL).toString().equals("true"))
						   		{
						   			if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCNO).toString().length()>0 && tblTSCAS.getValueAt(j,TB3_TSCNO).toString().length()>0 && j !=P_intROWID)
						   			if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCNO).toString().compareTo(tblTSCAS.getValueAt(j,TB3_TSCNO).toString().trim())==0)
									{
										setMSG("This Cause No Already Exist in the table for "+txtTSGNO_TS.getText()+" T.S.G. No..",'E');
										return false;
									}
						   	    }
					   	     }
	    			    }
			   			else if(P_intCOLID == TB3_TSCDS)
	    			    {
							if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCDS).toString().length()>0)
								tblTSCAS.setValueAt(tblTSCAS.getValueAt(P_intROWID,TB3_TSCDS).toString().replace("'","`"),P_intROWID,TB3_TSCDS);
							
	    			    }
						else if(P_intCOLID == TB3_TSCRM)
	    			    {
							if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCRM).toString().length()>0)
								tblTSCAS.setValueAt(tblTSCAS.getValueAt(P_intROWID,TB3_TSCRM).toString().replace("'","`"),P_intROWID,TB3_TSCRM);
	    			    }
		   			}
					catch(Exception e)
					{
						setMSG(e,"error in tblTSCAS InputVerifier  ");
					}
			    }
			 }
		   	 catch(Exception L_E)
		   	 {
		   	     setMSG(L_E,"Table Input Verifier");
		   	 }
		   	  setMSG("",'N');
		   	  return true;
		   }
		} 
	
}

	
	
	