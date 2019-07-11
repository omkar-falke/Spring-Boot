/**System Name:Plant Maintenance.
 
Program Name:Equipment Base Detail

Purpose : This module used for accepting the Equipment Detail ,Equipment's Specifications ,Trouble Shooting Guide & PM/OH Checklist.
 
Source Directory: f:\source\splerp3\pm_teeqd.java                         
Executable Directory: F:\exec\splerp3\pm_teeqd.class

 
List of tables used:
Table Name		Primary key											Operation done
																Insert	Update	   Query    Delete	
------------------------------------------------------------------------------------------------------------------------
PM_EQMST		EQ_EQPID,EQ_CMPCD                         	/	      /          /        /
PM_ESTRN		ES_SRLNO,ES_EQPID,ES_CMPCD                	/	      /          /        /
PM_TSMST		TS_TSCNO,TS_TSFNO,TS_TSGNO,TS_CMPCD       	/         /          /        /
PM_TGMST		TG_TAGNO,TG_CMPCD                                                /
PM_ATMST		AT_ATIND,AT_SRLNO,AT_EQSTP,AT_EQMTP,AT_CMPCD                     /          
PM_MPTRN        MP_STPNO,MP_MSSNO,MP_CMPCD                	/ 	  	  /		     /        /
PM_MSMST		MS_CMPCD,MS_MSSNO						  	/         /          /
PM_WRMST        WR_WRKNO,WR_CMPCD											     /
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
//txtTSGNO		EQ_TSGNO    	PM_EQMST		VARCHAR(8),     T.S.G NO                   
txtDRGNO		EQ_DRGNO     	PM_EQMST		VARCHAR(20),    Drawing No                    
btgRNIND		EQ_RNIND     	PM_EQMST		VARCHAR(1),     Runlog Ind                     
txtMATGR		EQ_MATGR    	PM_EQMST		VARCHAR(6),     Material Group                    
txtMFRCD		EQ_MFRCD     	PM_EQMST		VARCHAR(5),     Manufacture Code 
txtMFRNM		PT_PRTNM     	CO_PTMST		VARCHAR(40),    Manufacture Name
txtADR01		PT_ADR01M     	CO_PTMST		VARCHAR(40),    Manufacture Address 
txtCTYNM		PT_CTYNM     	CO_PTMST		VARCHAR(15),    Manufacture City                    
btgICHCD		EQ_ICHCD     	PM_EQMST		VARCHAR(1),     Interchange Id.                    
txtPORNO		EQ_PORNO     	PM_EQMST		VARCHAR(15),    Purchase Order No                   
txtPORDT		EQ_PORDT     	PM_EQMST		DATE,           Purchase Order Date                     
txtPORVL		EQ_PORVL     	PM_EQMST		DECIMAL(12),    Purchase Order Value 
txtHRSRN		EQ_HRSRN     	PM_EQMST		DECIMAL(12),    Hours Run                 
txtINSDT		EQ_INSDT     	PM_EQMST		DATE,    		Installed Date 
txtMNTVL		EQ_MNTVL     	PM_EQMST		DECIMAL(12,2),  Maintenance Cost                              
-----------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on  Specification screen:
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
txtTSGNO		TS_TSGNO    	PM_TSMST		VARCHAR(8),     Trouble Shooting Guide No.                    
txtTSGDS		TS_TSGDS     	PM_TSMST		VARCHAR(50),    Trouble Shooting Guide Description                    
txtTSFNO		TS_TSFNO   		PM_TSMST		VARCHAR(3),     T.S.G Failure No.                     
txtTSFDS		TS_TSFDS     	PM_TSMST		VARCHAR(60),    T.S.G Failure Description.                   
txtTSCNO		TS_TSCNO     	PM_TSMST		VARCHAR(3),     T.S.G Cause No.                     
txtTSCDS		TS_TSCDS     	PM_TSMST		VARCHAR(300),   T.S.G Cause Description.                     
txtTSCRM		TS_TSCRM     	PM_TSMST		VARCHAR(300),   T.S.G Remedy                     
-----------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on PM/OH checklist screen:
Field Name		Column Name		Table name		Type/Size		Description
-----------------------------------------------------------------------------------------------------------------------			
txtMSSNO		MP_MSSNO    	PM_MPTRN		VARCHAR(8),     Maintenance spec sheet No.                    
txtMSSDS		MS_MSSDS     	PM_MSMST		VARCHAR(30),    Maintenance spec sheet Description                    
txtSTPNO		MP_STPNO   		PM_MPTRN		VARCHAR(3),     Step No.                     
txtJOBDS		MP_JOBDS    	PM_MPTRN		VARCHAR(300),   Job Description.                   
txtOBSDS		MP_OBSDS     	PM_MPTRN		VARCHAR(200),   Desired Observation.                     
        
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
txtMSSNO	 Mss. No, Description             	    MP_MSSNO,MS_MSSDS 		   	PM_MPTRN,PM_MSMST
txtMATGR     Material Group,Decscription			CT_MATCD,CT_MATDS           CO_CTMST /CT_MATCD = '0000000A'
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
	
In Equipment - Specification:
    -> Fetches data from the attrib(pm_atmst), Equipment Detail(pm_eqmst) tables for the addition of the Specification, belongs to the given the Equipment type & sub-type.
    -> Fetches data from the Equipment Details(pm_eqmst) ,Equipment_Spcs(pm_estrn) tables for modification of the existing Specification for the Equipment type & sub-type.
	-> Generated the Plan,area & Criticality of the Given Tag No, it will not be added/updated in pm_estrn.
	-> Validating whether the Serial No & Attribute are not avaliable .
	
In Equipment- Trouble Shooting Guide:
	-> Facility is provided to accept trouble, Cause & Remedy details of given T.S.G. No.
	-> Any observation of above can be added/updated & deleted in future.
	-> Validating whether the failure no,Descp & Cause No,Descp are not avaliable. 
	
In Equipment- Maintenance Spec. sheet:
	-> Captured step no, job description ,desired observation & Mss No of given Eqpt Id.
	-> Two types of checklist are captured where one type has each step having Step description & Desired observation values to indicate the desired observation expected against each of these activity.
	-> The other type captures against each step the step description only.
	-> Validating whether the step no & Job Descp are not avaliable. 

Maintenance History:
	-> Fetch Maintenance history records for PM jobs from work master (pm_wrmst) table of given Eqpt Id.
	-> Maintenance History tab will be enabled while select Enquiry option otherwise disable becoze.
	-> User can not be added/updated & deleted Maintenance History records which can be only generated for Enquiry.
**/

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.util.StringTokenizer;
import java.sql.ResultSet;
import java.util.Hashtable;

class pm_teeqd extends cl_pbase 
{
	
	private JTextField txtEQPID,txtEQPDS;
	private JTextField txtTAGNO,txtTAGDS;
	private JTextField txtPLNCD,txtPLNDS;
	private JTextField txtARACD,txtARADS;
	private JTextField txtCRICD,txtCRIDS;
	private JTextField txtEQMTP,txtEQSTP,txtEQMTP_DS,txtEQSTP_DS;
	private JTextField txtDPTCD,txtDPTNM;
    private JTextField txtDRGNO,txtMATGR,txtMATDS,txtMFRCD,txtMFRNM,txtADR01,txtCTYNM,txtPORNO,txtPORDT,txtPORVL,txtHRSRN,txtINSDT,txtMNTVL;
    private JTextField txtTSGNO,txtTSGDS;
    private JTextField txtMSSNO,txtMSSDS;
    
	private JTabbedPane jtpMANTAB,jtpEQPTAB;          
	private JPanel pnlEQPDL,pnlEQPSP,pnlTSGDL,pnlMANTAB,pnlMSSDL,pnlHSTRY; 
	
	private JRadioButton  rdbRNIYS,rdbRNINO;
	private JRadioButton rdbICHNO,rdbICHYS;
	private ButtonGroup  btgRNIND,btgICHCD;
	
	private JButton btnNEXT;
	
	private  cl_JTable tblEQPSP,tblTSFAL,tblTSCAS,tblMSSDL,tblMATCD,tblFALCD,tblWRKDN,tblMSSNO; 
	private INPVF oINPVF;
	private TBLINPVF objTBLVRF;
	
    int TB1_CHKFL = 0; 				JCheckBox chkCHKFL;
    int TB1_SRLNO = 1;              JTextField txtSRLNO;
	int TB1_ATDSC = 2;              JTextField txtATDSC;// text box for Attribute desc.
	int TB1_SPDSC = 3;              JTextField txtSPDSC;//for Specification desc.
	
	int TB2_CHKFL = 0; 			    JCheckBox chkCHKFL1;
	int TB2_TSFNO = 1;              JTextField txtTSFNO;
	int TB2_TSFDS = 2;              JTextField txtTSFDS;
	 
	int TB3_CHKFL = 0; 			    JCheckBox chkCHKFL2;
	int TB3_TSCNO = 1;              JTextField txtTSCNO;
	int TB3_TSCDS = 2;              JTextField txtTSCDS;
	int TB3_TSCRM = 3;              JTextField txtTSCRM;
	
	int TB4_CHKFL = 0; 			    JCheckBox chkCHKFL3;
	int TB4_STPNO = 1;              JTextField txtSTPNO;
	int TB4_JOBDS = 2;              JTextField txtJOBDS;
	int TB4_OBSDS = 3;              JTextField txOBSDS;
	
	int TB5_CHKFL = 0; 			    JCheckBox chkCHKFL4;
	int TB5_MATCD = 1;              JTextField txtMATCD;
	int TB5_MATDS = 2;              JTextField txtMATDS_MH;
	int TB5_UOMCD= 3;               JTextField txtUOMCD;
	int TB5_QTYRQ = 4;              JTextField txtQTYRQ;
	
	int TB6_CHKFL = 0; 			    JCheckBox chkCHKFL5;
	int TB6_FALCD = 1;              JTextField txtFALCD;
	int TB6_FALDS = 1;              JTextField txtFALDS;
	int TB6_RSNCD = 2;              JTextField txtRSNCD;
	int TB6_RSNDS = 2;              JTextField txtRSNDS;
	
	int TB7_CHKFL = 0; 			    JCheckBox chkCHKFL6;
	int TB7_WORNO= 1;               JTextField txtWORNO;
	int TB7_WRKDN = 2;              JTextField txtWRKDN;
	int TB7_REMDS= 3;               JTextField txtREMDS;
	
	Hashtable<String,String> hstSPDSC;/**display Specification Description **/
	Hashtable<String,String> hstMATDS;
	Hashtable<String,String[]> hstCDTRN;
	Hashtable<String,String[]> hstCTMST;
	
	private int intCDTRN_TOT = 2;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;	
   
    private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;
	
	String strTSFNO ="",strTSFDS ="";
	int intROW_SPECF=0,intROW_TSFAL=0,intROW_TSCAS=0,intROW_ATDSC=0,intROW_MSSNO=0,intROW_MSSDL=0;
	pm_teeqd()		/*  Constructor   */
	{
		super(1);
		try
		{
			setMatrix(20,20);	
			
			pnlEQPDL = new JPanel();
			pnlEQPSP = new JPanel();
			pnlTSGDL = new JPanel();
			pnlMSSDL = new JPanel();
			pnlMANTAB = new JPanel();
			pnlHSTRY = new JPanel();
			pnlMANTAB.setLayout(null);
			pnlTSGDL.setLayout(null);
			pnlEQPDL.setLayout(null);
			pnlEQPSP.setLayout(null);
			pnlMSSDL.setLayout(null);
			pnlHSTRY.setLayout(null);
		
			jtpMANTAB=new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			
			jtpEQPTAB=new JTabbedPane();
			jtpEQPTAB.addMouseListener(this);
			
			jtpMANTAB.add(pnlMANTAB,"Equipment Base Details");
			
			jtpEQPTAB.add(pnlEQPDL,"Equipment Detail");
			jtpEQPTAB.add(pnlEQPSP,"Equipment Specification");
			jtpEQPTAB.add(pnlTSGDL,"Trouble Shooting Guide");
			jtpEQPTAB.add(pnlMSSDL,"Maint.Spec Sheet");
			jtpEQPTAB.add(pnlHSTRY,"Maintenance History");
			
			add(new JLabel("Tag No :"),1,1,1,2,pnlMANTAB,'L');
			add(txtTAGNO = new TxtLimit(12),1,3,1,2,pnlMANTAB,'L');
			add(txtTAGDS = new TxtLimit(50),1,5,1,6,pnlMANTAB,'L');
			
			add(new JLabel("Plant Code :"),1,11,1,2,pnlMANTAB,'L');
			add(txtPLNCD = new TxtLimit(3),1,13,1,1,pnlMANTAB,'L');
			add(txtPLNDS = new TxtLimit(10),1,14,1,4,pnlMANTAB,'L');
			
			add(new JLabel("Equipment Id :"),2,1,1,2,pnlMANTAB,'L');
			add(txtEQPID = new TxtLimit(12),2,3,1,2,pnlMANTAB,'L');
			add(txtEQPDS = new TxtLimit(50),2,5,1,6,pnlMANTAB,'L');
		
			add(new JLabel("Area Code :"),2,11,1,2,pnlMANTAB,'L');
			add(txtARACD = new TxtLimit(3),2,13,1,1,pnlMANTAB,'L');
			add(txtARADS = new TxtLimit(10),2,14,1,4,pnlMANTAB,'L');
			
			add(new JLabel("Criticality :"),3,11,1,2,pnlMANTAB,'L');
			add(txtCRICD = new TxtLimit(1),3,13,1,1,pnlMANTAB,'L');
			add(txtCRIDS = new TxtLimit(5),3,14,1,4,pnlMANTAB,'L');
						
			add(new JLabel("Equipment Type:"),3,1,1,2.5,pnlMANTAB,'L');  
			add(txtEQMTP = new TxtLimit(3),3,3,1,2,pnlMANTAB,'L'); 
			add(txtEQMTP_DS = new TxtLimit(50),3,5,1,6,pnlMANTAB,'L'); 
			add(new JLabel("Sub Type:"),4,1,1,2,pnlMANTAB,'L');  
			add(txtEQSTP = new TxtLimit(3),4,3,1,2,pnlMANTAB,'L');
			add(txtEQSTP_DS = new TxtLimit(50),4,5,1,6,pnlMANTAB,'L');
			
			add(new JLabel("Department:"),4,11,1,2,pnlMANTAB,'L');  
			add(txtDPTCD = new TxtLimit(3),4,13,1,1,pnlMANTAB,'L');
			add(txtDPTNM = new TxtLimit(15),4,14,1,4,pnlMANTAB,'L');
		
			add(new JLabel("Installed Date"),1,1,1,2,pnlEQPDL,'L');  
			add(txtINSDT = new TxtDate(),1,3,1,2,pnlEQPDL,'L');
			add(new JLabel("Drawing No."),1,5,1,2,pnlEQPDL,'L');  
			add(txtDRGNO = new TxtLimit(20),1,7,1,4,pnlEQPDL,'L');
		
			add(new JLabel("Mfr. Code"),2,1,1,2,pnlEQPDL,'L');  
			add(txtMFRCD = new TxtLimit(5),2,3,1,2,pnlEQPDL,'L');
			add(txtMFRNM = new TxtLimit(20),2,5,1,7,pnlEQPDL,'L');
			add(txtADR01 = new TxtLimit(20),3,5,1,7,pnlEQPDL,'L');
			add(txtCTYNM = new TxtLimit(20),4,5,1,4,pnlEQPDL,'L');
			
			add(new JLabel("Runlog Ind"),2,12,1,2,pnlEQPDL,'L');  
			add(rdbRNIYS=new JRadioButton("Yes"),2,14,1,2,pnlEQPDL,'L');
			add(rdbRNINO=new JRadioButton("No"),2,16,1,1.5,pnlEQPDL,'L');
			btgRNIND=new ButtonGroup();
			btgRNIND.add(rdbRNIYS); 
			btgRNIND.add(rdbRNINO); 
			rdbRNINO.setSelected(true);
			
			add(new JLabel("Interchange Id."),3,12,1,2,pnlEQPDL,'L');  
			add(rdbICHYS=new JRadioButton("Yes"),3,14,1,2,pnlEQPDL,'L');
			add(rdbICHNO=new JRadioButton("No"),3,16,1,1.5,pnlEQPDL,'L');
			btgICHCD=new ButtonGroup();
			btgICHCD.add(rdbICHYS); 
			btgICHCD.add(rdbICHNO);  
			rdbICHNO.setSelected(true);
			
			add(new JLabel("Material Group"),5,1,1,2,pnlEQPDL,'L');  
			add(txtMATGR = new TxtLimit(10),5,3,1,2,pnlEQPDL,'L');
			add(txtMATDS = new TxtLimit(60),5,5,1,7,pnlEQPDL,'L');
			
			add(new JLabel("P.O. Number"),6,1,1,2,pnlEQPDL,'L');  
			add(txtPORNO = new TxtLimit(15),6,3,1,2.5,pnlEQPDL,'L');
			add(new JLabel("P.O. Date"),6,6,1,2,pnlEQPDL,'L');  
			add(txtPORDT = new TxtDate(),6,8,1,2.5,pnlEQPDL,'L');
			add(new JLabel("P.O. Value"),6,11,1,2,pnlEQPDL,'L');  
			add(txtPORVL = new TxtNumLimit(12),6,13,1,3,pnlEQPDL,'L');
		
			add(new JLabel("Hours Run"),7,1,1,2,pnlEQPDL,'L');  
			add(txtHRSRN = new TxtNumLimit(12),7,3,1,2.5,pnlEQPDL,'L');
			
			add(new JLabel("Maint.Cost"),7,6,1,2.5,pnlEQPDL,'L');  
			add(txtMNTVL = new TxtNumLimit(12.2),7,8,1,2.5,pnlEQPDL,'L');
		
    		String[] L_strTBLHD = {"","Serial No","Attribute Description","Specification Description"};
    		int[] L_intCOLSZ = {10,70,300,350};
    		tblEQPSP= crtTBLPNL1(pnlEQPSP,L_strTBLHD,100,1,2,9,15,L_intCOLSZ,new int[]{0});
    		
    		tblEQPSP.addKeyListener(this);
    		tblEQPSP.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
    		tblEQPSP.setCellEditor(TB1_SRLNO,txtSRLNO = new TxtLimit(3));
    		tblEQPSP.setCellEditor(TB1_ATDSC,txtATDSC = new TxtLimit(20));
    		tblEQPSP.setCellEditor(TB1_SPDSC,txtSPDSC = new TxtLimit(50));
    		txtSPDSC.addFocusListener(this);
    		
    		add(jtpMANTAB,1,2,18,19,this,'L');
    		add(jtpEQPTAB,6,1,12,18,pnlMANTAB,'L');
    		
			oINPVF=new INPVF();
			txtTAGNO.setInputVerifier(oINPVF);
			txtEQPID.setInputVerifier(oINPVF);
		
			txtEQMTP.setInputVerifier(oINPVF);
			txtEQSTP.setInputVerifier(oINPVF);
			txtDPTCD.setInputVerifier(oINPVF);
			txtMATGR.setInputVerifier(oINPVF);
			txtMFRCD.setInputVerifier(oINPVF);
			txtINSDT.setInputVerifier(oINPVF);
			txtPORNO.setInputVerifier(oINPVF);
			txtPORDT.setInputVerifier(oINPVF);
			
			add(new JLabel("T.S.G. No"),1,2,1,2,pnlTSGDL,'L');  
			add(txtTSGNO = new TxtLimit(8),1,4,1,2,pnlTSGDL,'L');
			add(new JLabel("Description"),1,6,1,2,pnlTSGDL,'L');  
			add(txtTSGDS = new TxtLimit(50),1,8,1,7,pnlTSGDL,'L');
			
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
    		txtTSGNO.setInputVerifier(oINPVF);
    		
    		add(new JLabel("MSS No"),1,2,1,2,pnlMSSDL,'L');  
			add(txtMSSNO = new TxtLimit(8),1,4,1,2,pnlMSSDL,'L');
			add(new JLabel("Description"),1,6,1,2,pnlMSSDL,'L');  
			add(txtMSSDS = new TxtLimit(30),1,8,1,7,pnlMSSDL,'L');
			
    		String[] L_strTBLHD3 = {"","Step No","Job Description","Desired Observation"};
    		int[] L_intCOLSZ3 = {10,50,360,350};
    		tblMSSDL= crtTBLPNL1(pnlMSSDL,L_strTBLHD3,100,3,2,8,16,L_intCOLSZ3,new int[]{0});
    		
    		tblMSSDL.addKeyListener(this);
    		tblMSSDL.setCellEditor(TB4_CHKFL,chkCHKFL3=new JCheckBox());
    		tblMSSDL.setCellEditor(TB4_STPNO,txtSTPNO = new TxtLimit(3));
    		tblMSSDL.setCellEditor(TB4_JOBDS,txtJOBDS = new TxtLimit(300));
    		tblMSSDL.setCellEditor(TB4_OBSDS,txOBSDS = new TxtLimit(200));
    		txtSTPNO.addFocusListener(this);
    		txtMSSNO.setInputVerifier(oINPVF);
    		
    		objTBLVRF = new TBLINPVF();
    		tblTSFAL.setInputVerifier(objTBLVRF);
    		tblEQPSP.setInputVerifier(objTBLVRF);
    		tblTSCAS.setInputVerifier(objTBLVRF);
    		tblMSSDL.setInputVerifier(objTBLVRF);
    		
    		String[] L_strTBLHD6 = {"","Work No","Work Done","Remedy"};
    		int[] L_intCOLSZ6 = {10,70,400,200};
    		tblWRKDN= crtTBLPNL1(pnlHSTRY,L_strTBLHD6,50,1,2,4,15,L_intCOLSZ6,new int[]{0});
    		
    		String[] L_strTBLHD4 = {"","Material Code","Description","Unit","Quantity"};
    		int[] L_intCOLSZ4 = {10,100,100,30,70};
    		tblMATCD= crtTBLPNL1(pnlHSTRY,L_strTBLHD4,200,6,2,4,8,L_intCOLSZ4,new int[]{0});
    		
    		String[] L_strTBLHD5 = {"","Fail Code","Description","Rsn.Code","Description"};
    		int[] L_intCOLSZ5 = {10,50,100,50,100};
    		tblFALCD= crtTBLPNL1(pnlHSTRY,L_strTBLHD5,200,6,11,4,6,L_intCOLSZ5,new int[]{0});
    		
    		setENBL(false);
    		
			hstCDTRN = new Hashtable<String,String[]>();
			hstCDTRN.clear();
			
			crtCDTRN("'MSTPMXXPLN','MSTPMXXARA','MSTPMXXCRT', 'MSTPMXXEQT','MSTPMXXEST','SYSCOXXDPT','MSTPMXXRSN','MSTPMXXFLR'","",hstCDTRN);
			
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
				jtpEQPTAB.setEnabledAt(4,false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					txtTAGNO.requestFocus();
					
				    txtTAGDS.setEnabled(false);
					txtEQSTP_DS.setEnabled(false);
				    txtEQMTP_DS.setEnabled(false);
				    txtPLNCD.setEnabled(false);
					txtARACD.setEnabled(false);
					txtCRICD.setEnabled(false);
					txtPLNDS.setEnabled(false);
					txtARADS.setEnabled(false);
					txtCRIDS.setEnabled(false);
					txtDPTNM.setEnabled(false);
					txtMATDS.setEnabled(false);
					txtMFRNM.setEnabled(false);
					txtADR01.setEnabled(false);
					txtCTYNM.setEnabled(false);
					
					((JTextField)tblEQPSP.cmpEDITR[TB1_SRLNO]).setEditable(false);
					((JTextField)tblEQPSP.cmpEDITR[TB1_ATDSC]).setEditable(false);
					tblMATCD.setEnabled(false);
					tblFALCD.setEnabled(false);
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						txtEQPDS.setEnabled(false);
						txtEQMTP.setEnabled(false);
						txtEQSTP.setEnabled(false);
						txtDRGNO.setEnabled(false);
						rdbRNIYS.setEnabled(false);
						rdbRNINO.setEnabled(false);
						txtDPTCD.setEnabled(false);
						txtMATGR.setEnabled(false);
						txtMFRCD.setEnabled(false);
						rdbICHYS.setEnabled(false);
						rdbICHNO.setEnabled(false);
						txtPORNO.setEnabled(false);
						txtPORDT.setEnabled(false);
						txtPORVL.setEnabled(false);
						txtHRSRN.setEnabled(false);
						txtINSDT.setEnabled(false);
						txtMNTVL.setEnabled(false);
						txtTSGDS.setEnabled(false);
						txtMSSDS.setEnabled(false); 

						tblEQPSP.setEnabled(false);
						tblMSSDL.setEnabled(false);
						txtEQMTP.setEnabled(false);
						txtEQSTP.setEnabled(false);
					}
					else
					{
						 txtEQPDS.setEnabled(true);
						 txtTAGNO.setEnabled(true);
						 txtEQMTP.setEnabled(true);
						 txtEQSTP.setEnabled(true);
						 txtDRGNO.setEnabled(true);
						 rdbRNIYS.setEnabled(true);
						 rdbRNINO.setEnabled(true);
						 txtDPTCD.setEnabled(true);
						 txtDPTCD.setEnabled(true);
						 txtMATGR.setEnabled(true);
						 txtMFRCD.setEnabled(true);
						 rdbICHYS.setEnabled(true);
						 rdbICHNO.setEnabled(true);
						 txtPORNO.setEnabled(true);
						 txtPORDT.setEnabled(true);
						 txtPORVL.setEnabled(true);
						 txtHRSRN.setEnabled(true);
						 txtINSDT.setEnabled(true);
						 txtMNTVL.setEnabled(true);
						 txtTSGDS.setEnabled(true);
						 txtMSSDS.setEnabled(true);
						 
						 tblEQPSP.setEnabled(true);
						 tblMSSDL.setEnabled(true);
						 txtEQMTP.setEnabled(true);
						 txtEQSTP.setEnabled(true);
					 }
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						txtEQMTP.setEnabled(false);
						txtEQSTP.setEnabled(false);
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						jtpEQPTAB.setEnabledAt(4,true);
				}
				else
					setENBL(false);
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"actionPerformed");		
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
					txtTAGDS.setText("");
					txtPLNCD.setText("");
					txtARACD.setText("");
					txtCRICD.setText("");
					txtPLNDS.setText("");
					txtARADS.setText("");
					txtCRIDS.setText("");
					txtEQPID.setText("");
					txtEQPDS.setText("");
					clrCOMP();
					
					txtEQPID.requestFocus();
				}
				else if(M_objSOURC == txtEQPID)
				{
					txtEQPID.setText(txtEQPID.getText().toUpperCase());
					if(txtEQPID.getText().length()==0)
						txtEQPDS.setText("");
					txtEQPDS.requestFocus();
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						setMSG("Enter Equipment Description..",'N');
					
				}
				else if(M_objSOURC == txtEQPDS)
				{
					txtEQPDS.setText(txtEQPDS.getText().replace("'","`"));
					txtEQPDS.setText(txtEQPDS.getText().toUpperCase());
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtEQMTP.requestFocus();
						setMSG("Enter Equipment Type or Press F1 to Select from List..",'N');
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						txtDPTCD.requestFocus();
						setMSG("Enter Department Code or Press F1 to Select from List..",'N');
					}
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
					txtDPTCD.requestFocus();
					setMSG("Enter Department Code or Press F1 to Select from List..",'N');
					
				}
				else if(M_objSOURC == txtDPTCD)
				{
					if(txtDPTCD.getText().length()==0)
						txtDPTNM.setText("");
						
					if(jtpEQPTAB.getSelectedIndex()==0)
					{
						txtINSDT.requestFocus();
						setMSG("Enter Installed Date..",'N');
					}
					else if(jtpEQPTAB.getSelectedIndex()==1)
					{
						tblEQPSP.requestFocus();
						setMSG("Enter Specification Desc..",'N');
					}
					else if(jtpEQPTAB.getSelectedIndex()==2)
					{ 
							txtTSGNO.requestFocus();
							setMSG("Enter TSG No or Press F1 to Select from List..",'N');
					}
					else if(jtpEQPTAB.getSelectedIndex()==3)
					{ 
							txtMSSNO.requestFocus();
							setMSG("Enter Mss No or Press F1 to Select from List..",'N');
					}
				}
				/*else if(M_objSOURC == txtPLNCD)
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
					else if(M_objSOURC == txtINSDT)
					{
						txtDRGNO.requestFocus();
						setMSG("Enter Drawing. No..",'N');
					}
					else if(M_objSOURC == txtDRGNO)
					{
						txtDRGNO.setText(txtDRGNO.getText().replace("'","`"));
						txtDRGNO.setText(txtDRGNO.getText().toUpperCase());
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
						}
						
						setMSG("Enter Material Group or Press F1 to Select from List..",'N');
					}
					else if(M_objSOURC == txtMATGR)
					{
						if(txtMATGR.getText().length()==0)
							txtMATDS.setText("");
						txtMATGR.setText(txtMATGR.getText().replace("'","`"));
						txtMATGR.setText(txtMATGR.getText().toUpperCase());
						txtPORNO.requestFocus();
						setMSG("Enter P.O. No or Press F1 to Select from List..",'N');
					}
					else if(M_objSOURC == txtPORNO)
					{
						txtPORNO.setText(txtPORNO.getText().replace("'","`"));
						txtPORNO.setText(txtPORNO.getText().toUpperCase());
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
						txtMNTVL.requestFocus();
						setMSG("Enter Maintenance Value..",'N');
					}
					
					else if(M_objSOURC == txtMNTVL)
						cl_dat.M_btnSAVE_pbst.requestFocus();
					else if(M_objSOURC == txtTSGNO)
					{
						txtTSGDS.setText("");
						tblTSFAL.clrTABLE();
						inlTBLEDIT(tblTSFAL);
						tblTSCAS.clrTABLE();
						inlTBLEDIT(tblTSCAS);
						
						txtTSGDS.requestFocus();
						setMSG("Enter T.S.G. No Description..",'N');
					}	
					else if(M_objSOURC == txtTSGDS)
					{
						txtTSGDS.setText(txtTSGDS.getText().replace("'","`"));
						txtTSGDS.setText(txtTSGDS.getText().toUpperCase());
						tblTSFAL.requestFocus();
						setMSG("Enter Failure No..",'N');
					}
					else if(M_objSOURC == txtMSSNO)
					{
						txtMSSDS.setText("");
						tblMSSDL.clrTABLE();
						inlTBLEDIT(tblMSSDL);
						txtMSSDS.requestFocus();
						setMSG("Enter Mss Description..",'N');
					}
					else if(M_objSOURC == txtMSSDS)
					{
						txtMSSDS.setText(txtMSSDS.getText().replace("'","`"));
						txtMSSDS.setText(txtMSSDS.getText().toUpperCase());
						tblMSSDL.requestFocus();
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
					M_strSQLQRY+= " where TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
					if(txtTAGNO.getText().length()>0)				
						M_strSQLQRY+= " AND TG_TAGNO like '"+txtTAGNO.getText().trim()+"%'";
					M_strSQLQRY+= " order by TG_TAGNO ";
					//System.out.println("txtTAGNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Tag No","Tag Description","Plant Code","Area Code","Criticality"},5,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtEQPID &&  !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
						M_strHLPFLD = "txtEQPID";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
					
						M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS from PM_EQMST";
						M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EQ_STSFL,'')<>'X'";
						if(txtTAGNO.getText().length()>0)		
							M_strSQLQRY+= " AND EQ_TAGNO='"+txtTAGNO.getText()+"'";
						if(txtEQPID.getText().length()>0)				
							M_strSQLQRY+= " AND EQ_EQPID like '"+txtEQPID.getText().trim()+"%'";
						M_strSQLQRY+= " order by EQ_EQPID";
						//System.out.println("EQPT_ID>>"+M_strSQLQRY);
					
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Id","Equipment Description"},2,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
				}
				
				/*else if(M_objSOURC == txtARACD)
				{
					M_strHLPFLD = "txtARACD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXARA' AND isnull(CMT_STSFL,'')<>'X' ";
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
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXPLN' AND isnull(CMT_STSFL,'')<>'X'";
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
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXCRT' AND isnull(CMT_STSFL,'')<>'X'";
					if(txtCRICD.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtCRICD.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Criticality Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	*/
				else if(M_objSOURC == txtEQMTP)
				{
					M_strHLPFLD = "txtEQMTP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXEQT' AND isnull(CMT_STSFL,'')<>'X' ";
					if(txtEQMTP.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtEQMTP.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Type","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				else if(M_objSOURC == txtEQSTP)
				{
					M_strHLPFLD = "txtEQSTP";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY=" SELECT cmt_codcd,cmt_codds from co_cdtrn ";
					M_strSQLQRY+= " where CMT_CGMTP ='MST' AND CMT_CGSTP = 'PMXXEST' AND isnull(CMT_STSFL,'')<>'X'";
					if(txtEQSTP.getText().length()>0)				
						M_strSQLQRY+= " AND cmt_codcd like '"+txtEQSTP.getText().trim()+"%'";
					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtCRICD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Equipment Sub Type","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
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
				else if(M_objSOURC==txtMATGR)		
				{
					M_strHLPFLD = "txtMATGR";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" select ct_matcd,ct_matds from co_ctmst where  SUBSTRING(ct_matcd,3,8) = '0000000A' and isnull(CT_STSFL,'') <> 'X'";
					if(txtMATGR.getText().length() >0)
						M_strSQLQRY += " AND ct_matcd like '"+txtMATGR.getText().trim()+"%'";
					M_strSQLQRY += " order by ct_matcd";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Material Group","Description"},2,"CT");
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
				else if(M_objSOURC==txtPORNO)		
				{
					M_strHLPFLD = "txtPORNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" select po_strtp,po_porno,po_pordt,po_porqt,po_porrt,po_porvl from mm_pomst where po_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
					M_strSQLQRY += " AND po_matcd ='"+txtMATGR.getText().trim()+"'";
					if(txtPORNO.getText().length() >0)
						M_strSQLQRY += " AND po_porno like '"+txtPORNO.getText().trim()+"%'";
					M_strSQLQRY += " order by po_pordt desc";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Store Type","P.O.No","P.O.Date","P.O.Qty","P.O.Rate","P.O.Value"},6,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
    			}
				else if(M_objSOURC==txtTSGNO)	
				{
					M_strHLPFLD = "txtTSGNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY=" SELECT distinct TS_TSGNO,TS_TSGDS from PM_TSMST ";
					M_strSQLQRY+= " where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TS_STSFL,'')<>'X'";
					if(txtTSGNO.getText().length() >0)
						M_strSQLQRY += " AND TS_TSGNO like '"+txtTSGNO.getText().trim()+"%'";
					M_strSQLQRY += " order by TS_TSGNO";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"T.S.G. NO","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
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
		else if(M_objSOURC == txtEQPID)
		{
			txtEQPID.requestFocus();
			if( jtpEQPTAB.getSelectedIndex()==0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				setMSG("Enter Equipment ID..",'N');
			else
				setMSG("Enter Equipment ID or Press F1 to Select from List..",'N');
		}
	
		else if(M_objSOURC == txtSPDSC)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)
			|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(tblEQPSP.getSelectedRow()>=intROW_ATDSC)
				{
				  ((JCheckBox)tblEQPSP.cmpEDITR[TB1_CHKFL]).setEnabled(false);
				  ((JTextField)tblEQPSP.cmpEDITR[TB1_SPDSC]).setEditable(false);
				}
				else
				{
				  ((JCheckBox)tblEQPSP.cmpEDITR[TB1_CHKFL]).setEnabled(true);
				  ((JTextField)tblEQPSP.cmpEDITR[TB1_SPDSC]).setEditable(true); 
				}
			}
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
		     
			      /**Select one row of Failure table at a time**/
			      for(int i=0;i<tblTSFAL.getRowCount();i++)
				  {
					 if(tblTSFAL.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					 {
						if(i != tblTSFAL.getSelectedRow())
						tblTSFAL.setValueAt(new Boolean(false),i,TB2_CHKFL);
					 }
				  }
		     }
			else if(L_KE.getSource().equals(chkCHKFL6))
		     {
			      if(((JCheckBox)tblWRKDN.cmpEDITR[TB7_CHKFL]).isSelected())
			      {
			    	if(tblWRKDN.getValueAt(tblWRKDN.getSelectedRow(),TB7_CHKFL).toString().equals("true"))
				   	{  
			    	   if(tblWRKDN.getValueAt(tblWRKDN.getSelectedRow(),TB7_WORNO).toString().length()>0)
			    	   {
			    		   getMATCD();
			    	   }
				   	}
			      }
			      else
				  {  
			    	  tblMATCD.clrTABLE(); 
			    	  tblFALCD.clrTABLE(); 
				  }
		     
			      /**Select one row of Failure table at a time**/
			      for(int i=0;i<tblWRKDN.getRowCount();i++)
				  {
					 if(tblWRKDN.getValueAt(i,TB7_CHKFL).toString().equals("true"))
					 {
						if(i != tblWRKDN.getSelectedRow())
							tblWRKDN.setValueAt(new Boolean(false),i,TB7_CHKFL);
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
			}
			else if(M_strHLPFLD.equals("txtTAGNO"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtTAGNO.setText(L_STRTKN.nextToken());
				 txtTAGDS.setText(L_STRTKN.nextToken());
				 txtPLNCD.setText(L_STRTKN.nextToken());
				 txtARACD.setText(L_STRTKN.nextToken());
				 txtCRICD.setText(L_STRTKN.nextToken());
				 txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				 txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				 txtCRIDS.setText(getCDTRN("MSTPMXXCRT"+txtCRICD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
				
			}
		/*	else if(M_strHLPFLD.equals("txtPLNCD"))
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
			}*/
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
			else if(M_strHLPFLD.equals("txtTSGNO"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtTSGNO.setText(L_STRTKN.nextToken());
				 txtTSGDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtMATGR"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtMATGR.setText(L_STRTKN.nextToken());
				 txtMATDS.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtMFRCD"))
			{
				String [] strMFDTA = null;
				strMFDTA = cl_dat.M_strHELP_pbst.replace('|','~').split("~");
				txtMFRCD.setText(strMFDTA[0]);
				txtMFRNM.setText(strMFDTA[1]);
				txtADR01.setText(strMFDTA[2]);
				
				if(strMFDTA.length>3 && strMFDTA[3].length()>1 )
					txtCTYNM.setText(strMFDTA[3]); 
				
			}
			else if(M_strHLPFLD.equals("txtPORNO"))
			{
				String [] strPODTA = null;
				strPODTA = cl_dat.M_strHELP_pbst.replace('|','~').split("~");
				txtPORNO.setText(strPODTA[1]);
				txtPORDT.setText(strPODTA[2]);
				txtPORVL.setText(strPODTA[5]);
				
			}
			else if(M_strHLPFLD.equals("txtMSSNO"))
			{
				 StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				 txtMSSNO.setText(L_STRTKN.nextToken());
				 txtMSSDS.setText(L_STRTKN.nextToken());
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
			String L_strDPTCD="",L_strMFRCD="",L_strEQMTP="",L_strEQSTP="",L_strMATGR="",L_strMATDS="";
		
			M_strSQLQRY= " SELECT EQ_EQMTP,EQ_EQSTP,EQ_DPTCD,EQ_TSGNO,EQ_MSSNO,EQ_DRGNO,EQ_MATGR,EQ_MFRCD,EQ_RNIND,EQ_ICHCD,EQ_PORNO,EQ_PORDT,EQ_PORVL,EQ_HRSRN,EQ_INSDT,EQ_MNTVL";
			M_strSQLQRY+= " FROM PM_EQMST";
			M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EQ_EQPID = '"+txtEQPID.getText()+"'  AND isnull(EQ_STSFL,'')<>'X'";
			
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 

			if(M_rstRSSET != null)
			{
				while( M_rstRSSET.next())
				{
					L_strEQMTP=nvlSTRVL(M_rstRSSET.getString("EQ_EQMTP"),"");
					L_strEQSTP=nvlSTRVL(M_rstRSSET.getString("EQ_EQSTP"),"");
					L_strDPTCD=nvlSTRVL(M_rstRSSET.getString("EQ_DPTCD"),"");
					L_strMATGR=nvlSTRVL(M_rstRSSET.getString("EQ_MATGR"),"");
					
					txtEQMTP.setText(L_strEQMTP);
					txtEQSTP.setText(L_strEQSTP);
					if(L_strEQMTP.length()>0)
						txtEQMTP_DS.setText(getCDTRN("MSTPMXXEQT"+L_strEQMTP,"CMT_CODDS",hstCDTRN));
					if(L_strEQSTP.length()>0)
						txtEQSTP_DS.setText(getCDTRN("MSTPMXXEST"+L_strEQSTP,"CMT_CODDS",hstCDTRN));
					
					txtDPTCD.setText(L_strDPTCD);
					if(txtDPTCD.getText().length()>0)
						txtDPTNM.setText(getCDTRN("SYSCOXXDPT"+L_strDPTCD,"CMT_CODDS",hstCDTRN));
					
					txtMATGR.setText(L_strMATGR);
				
					txtTSGNO.setText(nvlSTRVL(M_rstRSSET.getString("EQ_TSGNO"),""));
					L_strMFRCD=nvlSTRVL(M_rstRSSET.getString("EQ_MFRCD"),"");
					txtMFRCD.setText(L_strMFRCD);
					
					txtDRGNO.setText(nvlSTRVL(M_rstRSSET.getString("EQ_DRGNO"),""));
					
					if(nvlSTRVL(M_rstRSSET.getString("EQ_RNIND"),"").equals("Y"))
						rdbRNIYS.setSelected(true);
					else
						rdbRNIYS.setSelected(false);
					if(nvlSTRVL(M_rstRSSET.getString("EQ_RNIND"),"").equals("N"))
						rdbRNINO.setSelected(true);
					else
						rdbRNINO.setSelected(false);
					
					if(nvlSTRVL(M_rstRSSET.getString("EQ_RNIND"),"").equals("Y"))
						rdbICHYS.setSelected(true);
					else
						rdbICHYS.setSelected(false);
					if(nvlSTRVL(M_rstRSSET.getString("EQ_ICHCD"),"").equals("N"))
						rdbICHNO.setSelected(true);
					else
						rdbICHNO.setSelected(false);
					
					txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("EQ_PORNO"),""));
					if(!(M_rstRSSET.getDate("EQ_PORDT")==null))
						txtPORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EQ_PORDT")));
					txtPORVL.setText(nvlSTRVL(M_rstRSSET.getString("EQ_PORVL"),""));
					txtHRSRN.setText(nvlSTRVL(M_rstRSSET.getString("EQ_HRSRN"),""));
					if(!(M_rstRSSET.getDate("EQ_INSDT")==null))
						txtINSDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EQ_INSDT")));
					txtMNTVL.setText(nvlSTRVL(M_rstRSSET.getString("EQ_MNTVL"),""));
					txtMSSNO.setText(nvlSTRVL(M_rstRSSET.getString("EQ_MSSNO"),""));
					
				}
			}
			
            M_rstRSSET.close();
            
            if(L_strMATGR.length()>0)
            {
	            String L_strSQLQRY1= "select ct_matds from co_ctmst where  SUBSTRING(ct_matcd,3,8) = '0000000A' and isnull(CT_STSFL,'') <> 'X' ";
	            L_strSQLQRY1+= " AND ct_matcd='"+L_strMATGR+"' ";
	            ResultSet L_rstRSSET1= cl_dat.exeSQLQRY(L_strSQLQRY1);
				//System.out.println(">>>"+L_strSQLQRY1);
				if(L_rstRSSET1 != null)
				{
					while(L_rstRSSET1.next())
					{
						txtMATDS.setText(nvlSTRVL(L_rstRSSET1.getString("ct_matds"),""));
					}
					L_rstRSSET1.close();
				}
            }
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
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	/**
	 * This Method to fetch Attribute. */
	private void getATTRB() 
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strSPDSC="",L_strSRLNO="";
			intROW_ATDSC=0;
			
			tblEQPSP.clrTABLE();
			inlTBLEDIT(tblEQPSP);
			setMSG("Fetching Records ...",'N');
			
			M_strSQLQRY= " SELECT AT_SRLNO,AT_ATDSC FROM PM_ATMST ";
			M_strSQLQRY+= " where AT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(AT_STSFL,'')<>'X' ";
			M_strSQLQRY+= " AND AT_EQMTP='"+txtEQMTP.getText()+"'";
			M_strSQLQRY+= " AND AT_EQSTP='"+txtEQSTP.getText()+"' ";
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strSRLNO=nvlSTRVL(M_rstRSSET.getString("AT_SRLNO"),"");
					tblEQPSP.setValueAt(L_strSRLNO,intROW_ATDSC,TB1_SRLNO);
					tblEQPSP.setValueAt(nvlSTRVL(M_rstRSSET.getString("AT_ATDSC"),""),intROW_ATDSC,TB1_ATDSC);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						if(hstSPDSC.containsKey(L_strSRLNO))
						{
							L_strSPDSC = hstSPDSC.get(L_strSRLNO).toString();
							tblEQPSP.setValueAt(L_strSPDSC,intROW_ATDSC,TB1_SPDSC);
						}
					}
					
					intROW_ATDSC++;
				}
				 
				 if(intROW_ATDSC == 0 && jtpEQPTAB.getSelectedIndex()==1)
						setMSG("No Attribute Found for Specification ..",'E');  
			}
            M_rstRSSET.close();
           
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"getATTRB");		
		}	
			this.setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	
	/**
	 * This Method to get Data from pm_estrn for Equipment specification */
	private void getEQPSP() 
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Records ...",'N');
			tblEQPSP.clrTABLE();
			inlTBLEDIT(tblEQPSP);
			
			hstSPDSC = new Hashtable<String,String>();
		
			M_strSQLQRY= " SELECT ES_SRLNO,ES_ATDSC,ES_SPDSC";
			M_strSQLQRY+= " FROM PM_ESTRN";
			M_strSQLQRY+= " where ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ES_EQPID='"+txtEQPID.getText()+"' AND ES_EQMTP='"+txtEQMTP.getText()+"' AND ES_EQSTP='"+txtEQSTP.getText()+"'AND isnull(ES_STSFL,'')<>'X' ";
		    //System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstSPDSC.put(nvlSTRVL(M_rstRSSET.getString("ES_SRLNO"),""),nvlSTRVL(M_rstRSSET.getString("ES_SPDSC"),""));
				}
			}
            M_rstRSSET.close();
            getATTRB();
           
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getEQPSP()"); 
			
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	/**
	 * This Method to get the number of failue for the given TSG No. */
	private void getTSFDL() 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Records ...",'N');
			intROW_TSFAL=0;
			
			M_strSQLQRY= " SELECT distinct TS_TSGDS,TS_TSFNO,TS_TSFDS";
			M_strSQLQRY+= " FROM PM_TSMST";
			M_strSQLQRY+= " where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TS_STSFL,'')<>'X' ";
			M_strSQLQRY+= " AND TS_TSGNO='"+txtTSGNO.getText()+"'";
		
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 

			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					txtTSGDS.setText(nvlSTRVL(M_rstRSSET.getString("TS_TSGDS"),""));
					tblTSFAL.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSFNO"),""),intROW_TSFAL,TB2_TSFNO);
					tblTSFAL.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSFDS"),""),intROW_TSFAL,TB2_TSFDS);
					
					intROW_TSFAL++;
				}
			}
            M_rstRSSET.close();
          
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSFDL()"); 
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	/**
	 * This Method to Fetch Causes of corresponding Failure No. */
	private void getTSCDL(String LP_strTSFNO) 
	{
		try
		{ 
			tblTSCAS.clrTABLE();
			inlTBLEDIT(tblTSCAS);
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			
			intROW_TSCAS=0;
			
			M_strSQLQRY= " SELECT TS_TSCNO,TS_TSCDS,TS_TSCRM";
			M_strSQLQRY+= " FROM PM_TSMST";
			M_strSQLQRY+= " where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TS_STSFL,'')<>'X' ";
			M_strSQLQRY+= " AND TS_TSGNO='"+txtTSGNO.getText()+"' and TS_TSFNO='"+LP_strTSFNO+"' ";
		
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					tblTSCAS.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSCNO"),""),intROW_TSCAS,TB3_TSCNO);
					tblTSCAS.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSCDS"),""),intROW_TSCAS,TB3_TSCDS);
					tblTSCAS.setValueAt(nvlSTRVL(M_rstRSSET.getString("TS_TSCRM"),""),intROW_TSCAS,TB3_TSCRM);
					tblTSCAS.setValueAt(new Boolean(true),intROW_TSCAS,TB3_CHKFL);	
					intROW_TSCAS++;
				}
			}
            M_rstRSSET.close();
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSCDL()"); 
		}
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * This Method to fetch Maintenance Spec. records. */
	private void getMSSNO() 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			intROW_MSSNO=0;
			intROW_MSSDL=0;
			
			M_strSQLQRY= " SELECT distinct MP_MSSNO,MP_STPNO,MP_JOBDS,MP_OBSDS,MS_MSSDS";
			M_strSQLQRY+= " FROM PM_MPTRN,PM_MSMST";
			M_strSQLQRY+= " where MP_CMPCD=MS_CMPCD AND MP_MSSNO=MS_MSSNO AND MP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(MP_STSFL,'')<>'X' ";
			M_strSQLQRY+= " AND MP_MSSNO='"+txtMSSNO.getText()+"' ";
			//M_strSQLQRY+= " AND MP_MSSNO='"tblMSSNO.getValueAt(intROW_MSSNO,TB8_MSSNO)+"' ";
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					//tblMSSNO.setValueAt(nvlSTRVL(M_rstRSSET.getString("MP_MSSNO"),""),intROW_MSSNO,TB8_MSSNO);
					//tblMSSNO.setValueAt(nvlSTRVL(M_rstRSSET.getString("MS_MSSDS"),""),intROW_MSSNO,TB8_MSSDS);
					
					txtMSSDS.setText(nvlSTRVL(M_rstRSSET.getString("MS_MSSDS"),""));
					tblMSSDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MP_STPNO"),""),intROW_MSSDL,TB4_STPNO);
					tblMSSDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MP_JOBDS"),""),intROW_MSSDL,TB4_JOBDS);
					tblMSSDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("MP_OBSDS"),""),intROW_MSSDL,TB4_OBSDS);
					//intROW_MSSNO++;
					intROW_MSSDL++;
				}
			}
            M_rstRSSET.close();
			
        }	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMSSNO()"); 
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	
	/** Method to get Data from PM_WRMST table*/
	  void getHSTRY()
	  {
		  try
		  {
			  this.setCursor(cl_dat.M_curWTSTS_pbst);
			  tblFALCD.clrTABLE();
	  		  tblMATCD.clrTABLE();
	  		 
			  int L_CNT=0;
			  M_strSQLQRY = "select WO_NO,WRK_DONE,RMRKS";
			  M_strSQLQRY+=	" FROM IMPACT.WO";
			  M_strSQLQRY+= " where EQPT_ID ='"+txtEQPID.getText()+"'";
			 // M_strSQLQRY+= " where WR_EQPID ='"+txtEQPID.getText()+"'";
			//  M_strSQLQRY+= " and isnull(WR_STSFL,'')<>'X'";
			//  M_strSQLQRY+= " and WR_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'"; 
			  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			 // System.out.println("getHSTRY"+M_strSQLQRY);
			  if(M_rstRSSET !=null)                           
			  { 
				 while(M_rstRSSET.next())
				 {
					 //txtWRKDN.setText(nvlSTRVL(M_rstRSSET.getString("WR_WRKDN"),""));
					// txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("WR_REMDS"),""));
					 //tblWRKDN.setValueAt(nvlSTRVL(M_rstRSSET.getString("WR_WORNO"),""),L_CNT,TB7_WORNO);
					//tblWRKDN.setValueAt(nvlSTRVL(M_rstRSSET.getString("WR_WRKDN"),""),L_CNT,TB7_WRKDN);
					//tblWRKDN.setValueAt(nvlSTRVL(M_rstRSSET.getString("WR_REMDS"),""),L_CNT,TB7_REMDS);
					 tblWRKDN.setValueAt(nvlSTRVL(M_rstRSSET.getString("WO_NO"),""),L_CNT,TB7_WORNO);
					tblWRKDN.setValueAt(nvlSTRVL(M_rstRSSET.getString("WRK_DONE"),""),L_CNT,TB7_WRKDN);
					tblWRKDN.setValueAt(nvlSTRVL(M_rstRSSET.getString("RMRKS"),""),L_CNT,TB7_REMDS);
					 L_CNT++;
			     }
			  }
			  M_rstRSSET.close(); 
		  }	
		  catch(Exception L_E)
		  {
			setMSG(L_E,"getHSTRY");
		  }
		  this.setCursor(cl_dat.M_curDFSTS_pbst);
	  }
	  /** Method to get Data from PM_WRMST table*/
	  void getMATCD()
	  {
		  try
		  {
			  this.setCursor(cl_dat.M_curWTSTS_pbst);
			  tblFALCD.clrTABLE();
	  		  tblMATCD.clrTABLE();
	  		 
			  int L_CNT1=0,L_CNT2=0;
			  M_strSQLQRY = "select MA_MATCD,MA_QTYRQ,MA_UOMCD,FL_FALCD,FL_RSNCD,CT_MATDS FROM PM_MAMST,PM_FLMST,CO_PTMST";
			  M_strSQLQRY+= " where MA_MATCD=CT_MATCD AND MA_WORNO=FL_WORNO AND WO_CMPCD=FL_CMPCD AND MA_WORNO ='"+ tblWRKDN.getValueAt(tblWRKDN.getSelectedRow(),TB7_WRKDN).toString()+"'";
			  M_strSQLQRY+= " and isnull(MA_STSFL,'')<>'X'";
			  M_strSQLQRY+= " and MA_CMPCD ='"+cl_dat.M_strCMPCD_pbst+"'"; 
			  M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			  System.out.println("getHSTRY"+M_strSQLQRY);
			  if(M_rstRSSET !=null)                           
			  { 
				 while(M_rstRSSET.next())
				 {
					 tblMATCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("MA_MATCD"),""),L_CNT1,TB5_MATCD);
					 tblMATCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("MA_QTYRQ"),""),L_CNT1,TB5_QTYRQ);
					 tblMATCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("MA_UOMCD"),""),L_CNT1,TB5_UOMCD);
					 tblFALCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("FL_FALCD"),""),L_CNT2,TB6_FALCD);
					 if(tblFALCD.getValueAt(L_CNT2,TB6_FALCD).toString().length()>0)
						 tblFALCD.setValueAt(getCDTRN("MSTPMXXFLR"+tblFALCD.getValueAt(L_CNT2,TB6_FALCD).toString(),"CMT_CODDS",hstCDTRN),L_CNT2,TB6_FALDS);
					 tblFALCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("FL_RSNCD"),""),L_CNT2,TB6_RSNCD);
					 if(tblFALCD.getValueAt(L_CNT2,TB6_RSNCD).toString().length()>0)
						 tblFALCD.setValueAt(getCDTRN("MSTPMXXRSN"+tblFALCD.getValueAt(L_CNT2,TB6_RSNCD).toString(),"CMT_CODDS",hstCDTRN),L_CNT2,TB6_RSNDS);
					 L_CNT1++;
					 L_CNT2++;
			     }
			  }
			  M_rstRSSET.close();		
		  }	
		  catch(Exception L_E)
		  {
			setMSG(L_E,"getMATCD");
		  }
		  this.setCursor(cl_dat.M_curDFSTS_pbst);
	  }
	
	
	void clrCOMP1()
	{
		try
		{
			txtTAGNO.setText("");
			txtTAGDS.setText("");
			txtEQPID.setText("");
			txtEQPDS.setText("");
			txtPLNCD.setText(""); 
			txtARACD.setText(""); 
			txtCRICD.setText("");
			txtPLNDS.setText(""); 
			txtARADS.setText(""); 
			txtCRIDS.setText("");
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
			txtEQMTP.setText("");
			txtEQMTP_DS.setText(""); 
			txtEQSTP.setText(""); 
			txtEQSTP_DS.setText("");
			
			txtDRGNO.setText("");
			txtDPTCD.setText("");
			txtDPTNM.setText("");
			txtMATGR.setText("");
			txtMATDS.setText("");
			txtMFRCD.setText(""); 
			txtMFRNM.setText(""); 
			txtADR01.setText(""); 
			txtCTYNM.setText(""); 
			txtPORNO.setText("");
			txtPORDT.setText(""); 
			txtPORVL.setText(""); 
			txtHRSRN.setText("");
			txtINSDT.setText(""); 
			txtMNTVL.setText("");
			rdbRNIYS.setSelected(false);
			rdbRNINO.setSelected(false);
			rdbICHYS.setSelected(false);
			rdbICHNO.setSelected(false);
			
			tblEQPSP.clrTABLE();
			inlTBLEDIT(tblEQPSP);
		
			txtTSGNO.setText("");
			txtTSGDS.setText("");
			tblTSFAL.clrTABLE();
			inlTBLEDIT(tblTSFAL);
			tblTSCAS.clrTABLE();
			inlTBLEDIT(tblTSCAS);
			
			txtMSSNO.setText("");
			txtMSSDS.setText("");
			tblMSSDL.clrTABLE();
			inlTBLEDIT(tblMSSDL);
			
			tblWRKDN.clrTABLE();
			inlTBLEDIT(tblWRKDN);
			tblMATCD.clrTABLE();
			inlTBLEDIT(tblMATCD);
			tblFALCD.clrTABLE();
			inlTBLEDIT(tblFALCD);
			
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
	
	
	
	/**Validation for enter  data**/
	boolean vldDATA()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
				return true;
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
			else if(txtEQSTP.getText().trim().length() ==0)
	    	{
				txtEQSTP.requestFocus();
	    		setMSG("Enter Equipment Sub Type..",'E');
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
			else if(txtDPTCD.getText().trim().length() ==0)
	    	{
				txtDPTCD.requestFocus();
	    		setMSG("Enter Department Code..",'E');
	    		return false;
	    	}
			else if(txtINSDT.getText().length()>0 && txtPORDT.getText().length()>0)
			{
				if(M_fmtLCDAT.parse(txtINSDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtPORDT.getText().toString()))<0)
				{
					setMSG("Installed order date must be greater than Purchase order date",'E');
					txtINSDT.requestFocus();
					return false;
				}
			}
			else if(txtPORDT.getText().length()>0)
			{
				if(M_fmtLCDAT.parse(txtPORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Purchase order date must be Samller than current date",'E');
					txtPORDT.requestFocus();
					return false;
				}
				else if(M_fmtLCDAT.parse(txtPORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtINSDT.getText().toString()))>0)
				{
					setMSG("Purchase order date must be Samller than Installed date",'E');
					txtPORDT.requestFocus();
					return false;
				}
			}
			
	    	for(int P_intROWNO=0;P_intROWNO<tblEQPSP.getRowCount();P_intROWNO++)
			{
				if(tblEQPSP.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					if(tblEQPSP.getValueAt(P_intROWNO,TB1_SRLNO).toString().length()==0)
					{
			    		setMSG("Enter Serial No in Spec..",'E');
			    		return false;
					}
					if(tblEQPSP.getValueAt(P_intROWNO,TB1_ATDSC).toString().length()==0)
					{
			    		setMSG("Enter Attribute Description..",'E');
			    		return false;
					}
					if(tblEQPSP.getValueAt(P_intROWNO,TB1_SPDSC).toString().length()==0)
					{
			    		setMSG("Enter Specification Desc in Spec..",'E');
			    		return false;
					}
				}
	    	}
	    	for(int P_intROWNO=0;P_intROWNO<tblTSFAL.getRowCount();P_intROWNO++)
			{
				if(tblTSFAL.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
				{
					if(tblTSFAL.getValueAt(P_intROWNO,TB2_TSFNO).toString().length()==0)
					{
			    		setMSG("Enter Failure Code in TSG..",'E');
			    		return false;
					}
					if(tblTSFAL.getValueAt(P_intROWNO,TB2_TSFDS).toString().length()==0)
					{
			    		setMSG("Enter Failure Description in TSG..",'E');
			    		return false;
					}
					
				}
	    	}
	    	boolean L_flgCHKFL2= false;
	    	for(int k=0;k<tblTSCAS.getRowCount();k++)
			{
				if( tblTSCAS.getValueAt(k,TB3_CHKFL).toString().equals("true"))
				{
					L_flgCHKFL2= true;
				}
			}
			
			if(L_flgCHKFL2== false)
			{
				if(tblTSCAS.getValueAt(tblTSCAS.getSelectedRow(),TB3_TSCNO).toString().length()>0)
				{
					setMSG("Please Select Cause No in TSG..",'E');				
					return false;
				}
			}		
	    	for(int P_intROWNO=0;P_intROWNO<tblTSCAS.getRowCount();P_intROWNO++)
			{
	    		if(tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_TSFNO).toString().length()>0 && tblTSFAL.getValueAt(tblTSFAL.getSelectedRow(),TB2_CHKFL).toString().equals("true"))
				{
					if(tblTSCAS.getValueAt(0,TB3_TSCNO).toString().length()==0)
					{
						System.out.println("P_intROWNO"+P_intROWNO);
			    		setMSG("Enter Cause No in TSG..",'E');
			    		return false;
					}
				}
				if(tblTSCAS.getValueAt(P_intROWNO,TB3_CHKFL).toString().equals("true"))
				{
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString().length()==0)
					{
			    		setMSG("Enter Cause No in TSG..",'E');
			    		return false;
					}
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCDS).toString().length()==0)
					{
			    		setMSG("Enter Cause Description in TSG..",'E');
			    	    return false;
					}
				}
	    	}
	    		
	    	
			for(int P_intROWNO=0;P_intROWNO<tblMSSDL.getRowCount();P_intROWNO++)
			{
				if(tblMSSDL.getValueAt(P_intROWNO,TB4_CHKFL).toString().equals("true"))
				{
					if(tblMSSDL.getValueAt(P_intROWNO,TB4_STPNO).toString().length()==0)
					{
			    		setMSG("Enter Step No in Maint.Spec Sheet..",'E');
			    		return false;
					}
					if(tblMSSDL.getValueAt(P_intROWNO,TB4_JOBDS).toString().length()==0)
					{
			    		setMSG("Enter Job Desc in Maint.Spec Sheet.. ",'E');
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
					exeADDEDL();
				
				for(P_intROWNO=0;P_intROWNO<tblEQPSP.getRowCount();P_intROWNO++)
				{
					if(tblEQPSP.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
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
				for(P_intROWNO=0;P_intROWNO<tblMSSDL.getRowCount();P_intROWNO++)
				{
					if(tblMSSDL.getValueAt(P_intROWNO,TB4_CHKFL).toString().equals("true"))
					{
						exeADDMSP(P_intROWNO);
						exeADDMSS();
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
					exeMODEDL();
				
				for(P_intROWNO=0;P_intROWNO<tblEQPSP.getRowCount();P_intROWNO++)
				{
					if(tblEQPSP.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeMODESP(P_intROWNO);
					}
				}
				
				for(P_intROWNO=0;P_intROWNO<tblTSCAS.getRowCount();P_intROWNO++)
				{
					if(tblTSCAS.getValueAt(P_intROWNO,TB3_CHKFL).toString().equals("true"))
					{
				    	exeADDTSG(P_intROWNO);
					}
				}
				for(P_intROWNO=0;P_intROWNO<tblMSSDL.getRowCount();P_intROWNO++)
				{
					if(tblMSSDL.getValueAt(P_intROWNO,TB4_CHKFL).toString().equals("true"))
					{
						exeADDMSP(P_intROWNO);
						exeADDMSS();
					}
				}
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
				exeDELEDL();
				
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
			this.setCursor(cl_dat.M_curWTSTS_pbst);
		    cl_dat.M_flgLCUPD_pbst = true;
		    M_strSQLQRY =" insert into PM_EQMST (EQ_TAGNO,EQ_EQPID,EQ_EQPDS,EQ_EQMTP,EQ_EQSTP,EQ_PLNCD,EQ_ARACD,EQ_DPTCD,EQ_TSGNO,EQ_DRGNO,EQ_RNIND,EQ_MATGR,EQ_MFRCD,EQ_ICHCD,EQ_PORNO,EQ_PORDT,EQ_PORVL,EQ_HRSRN,EQ_INSDT,EQ_MNTVL,EQ_MSSNO,"
			+"EQ_TRNFL,EQ_STSFL,EQ_LUSBY,EQ_LUPDT,EQ_CMPCD,EQ_SBSCD)"
			+"  VALUES('" + txtTAGNO.getText().toString()+"',";
	  	
	  		M_strSQLQRY += "'"+txtEQPID.getText().toString()+"',";
			M_strSQLQRY += "'"+txtEQPDS.getText().toString()+"',";
			M_strSQLQRY += "'"+txtEQMTP.getText().toString()+"',";
			M_strSQLQRY += "'"+txtEQSTP.getText().toString()+"',";
			M_strSQLQRY += "'"+txtPLNCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtARACD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtDPTCD.getText().toString()+"',";
			M_strSQLQRY += "'"+txtTSGNO.getText().toString()+"',";
			M_strSQLQRY += "'"+txtDRGNO.getText().toString()+"',";
			
			if(rdbRNIYS.isSelected())
				M_strSQLQRY += "'Y',";
			else 
				M_strSQLQRY += "'N',";
			
			M_strSQLQRY += "'"+txtMATGR.getText().toString()+"',";
			M_strSQLQRY += "'"+txtMFRCD.getText().toString()+"',";
			
			if(rdbICHYS.isSelected())
				M_strSQLQRY += "'Y',";
			else
				M_strSQLQRY += "'N',";
			
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
			if(txtMNTVL.getText().length()>0)
				M_strSQLQRY += "'"+txtMNTVL.getText().toString()+"',";
			else 
				  M_strSQLQRY += "0,"; 
			M_strSQLQRY += "'"+txtMSSNO.getText().toString()+"',";
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
			M_strSQLQRY += "'"+M_strSBSCD+"')";
			
			//System.out.println(">>>Insert>>"+ M_strSQLQRY );
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false; 
			  cl_dat.exeDBCMT("exeADDEDL");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDEDL()"); 
		  }
	  }
	  
	  /**Method to Modify Records of pm_eqmst(Equipment Detail records)**/
	  private void exeMODEDL() 
		{
		    try
		    {
	    	    M_strSQLQRY = " Update PM_EQMST set";
	    		M_strSQLQRY += " EQ_TAGNO='"+txtTAGNO.getText().toString()+"',";
		    	M_strSQLQRY += " EQ_PLNCD='"+txtPLNCD.getText().toString()+"',";
		    	M_strSQLQRY += " EQ_ARACD='"+txtARACD.getText().toString()+"',";
		    	if(txtEQPDS.getText().length()>0)
		    		M_strSQLQRY += " EQ_EQPDS='"+txtEQPDS.getText().toString()+"',";
		    	//if(txtEQMTP.getText().length()>0)
		    	//	M_strSQLQRY += " EQ_EQMTP='"+txtEQMTP.getText().toString()+"',";
		    	//if(txtEQSTP.getText().length()>0)
		    	//	M_strSQLQRY += " EQ_EQSTP='"+txtEQSTP.getText().toString()+"',";
		    	if(txtDPTCD.getText().length()>0)
		    		M_strSQLQRY += " EQ_DPTCD='"+txtDPTCD.getText().toString()+"',";
		    	if(txtTSGNO.getText().length()>0)
		    		M_strSQLQRY += " EQ_TSGNO='"+txtTSGNO.getText().toString()+"',";
		    	if(txtDRGNO.getText().length()>0)
		    		M_strSQLQRY += " EQ_DRGNO='"+txtDRGNO.getText().toString()+"',";
		    	
		    	if(rdbRNIYS.isSelected())
					M_strSQLQRY += " EQ_RNIND = 'Y',";
		    	else
					M_strSQLQRY += " EQ_RNIND = 'N',";
		    
		    	if(txtMATGR.getText().length()>0)
		    		M_strSQLQRY += " EQ_MATGR='"+txtMATGR.getText().toString()+"',";
		    	if(txtMFRCD.getText().length()>0)
		    		M_strSQLQRY += " EQ_MFRCD='"+txtMFRCD.getText().toString()+"',";
		    	
		    	if(rdbICHYS.isSelected())
					M_strSQLQRY += " EQ_ICHCD = 'Y',";
		    	else
					M_strSQLQRY += " EQ_ICHCD = 'N',";
		    		
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
		    	if(txtMSSNO.getText().length()>0)
		    		M_strSQLQRY += " EQ_MSSNO='"+txtMSSNO.getText().toString()+"',";
				M_strSQLQRY += " EQ_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += " EQ_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY += " where EQ_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
				M_strSQLQRY += " AND EQ_EQPID= '"+txtEQPID.getText().toString()+"'";
		    	
				//System.out.println(">>>update>>"+M_strSQLQRY);  
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODEDL()");
	    }
	}
	
	  
	  /** Method to insert records in pm_estrn(Equipment Specification) table.  */
	  private void exeADDESP(int P_intROWNO)
	  { 
		  try
		  {
				this.setCursor(cl_dat.M_curWTSTS_pbst);
			    cl_dat.M_flgLCUPD_pbst = true;
				M_strSQLQRY =" insert into PM_ESTRN (ES_EQPID,ES_EQMTP,ES_EQSTP,ES_SRLNO,ES_ATDSC,ES_SPDSC,ES_TRNFL,ES_STSFL,ES_LUSBY,ES_LUPDT,ES_CMPCD,ES_SBSCD)";
				M_strSQLQRY += " values (";
				M_strSQLQRY += "'"+txtEQPID.getText().toString()+"',";
				M_strSQLQRY += "'"+txtEQMTP.getText().toString()+"',";
				M_strSQLQRY += "'"+txtEQSTP.getText().toString()+"',";
				M_strSQLQRY += "'"+tblEQPSP.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"',";
				M_strSQLQRY += "'"+tblEQPSP.getValueAt(P_intROWNO,TB1_ATDSC).toString()+"',";
				M_strSQLQRY += "'"+tblEQPSP.getValueAt(P_intROWNO,TB1_SPDSC).toString()+"',";
			
				M_strSQLQRY += "'0',";
				M_strSQLQRY += "'0',";
				M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
				M_strSQLQRY += "'"+M_strSBSCD+"')";
				
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				//System.out.println("insert"+M_strSQLQRY);
		
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false; 
			  cl_dat.exeDBCMT("exeADDESP");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDESP()");
		  }
	  }
	  
	 /** Method to insert records in pm_estrn(Equipment Specification) table.  */
	  private void exeMODESP(int P_intROWNO)
	  { 
		  try
		  {
			  	this.setCursor(cl_dat.M_curWTSTS_pbst);
			    
				String strSQLQRY=" select count(*) from PM_ESTRN where";
				strSQLQRY+=" ES_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ES_STSFL,'')<>'X' AND ES_EQPID='"+txtEQPID.getText().toString()+"'";
				strSQLQRY += " AND  ES_SRLNO='"+tblEQPSP.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"'";
				ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count>>"+strSQLQRY);
				if(rstRSSET.next() && rstRSSET != null)
				{
					if(rstRSSET.getInt(1)>0)
					{
						M_strSQLQRY = " Update PM_ESTRN set";

						if(tblEQPSP.getValueAt(P_intROWNO,TB1_SPDSC).toString().length()>0)
							M_strSQLQRY += " ES_SPDSC='"+tblEQPSP.getValueAt(P_intROWNO,TB1_SPDSC).toString()+"',";
						
						M_strSQLQRY += " ES_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += " ES_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						M_strSQLQRY += " where ES_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
						M_strSQLQRY += " AND ES_EQPID= '"+txtEQPID.getText().toString()+"'";
						M_strSQLQRY += " AND  ES_SRLNO='"+tblEQPSP.getValueAt(P_intROWNO,TB1_SRLNO).toString()+"'";
							
						//System.out.println(">>>update>>"+M_strSQLQRY);  
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
								
					}
					else
					{
						exeADDESP(P_intROWNO);
					}
				}
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false; 
			  cl_dat.exeDBCMT("exeMODESP");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeMODESP()");
		  }
	  }
	  
	  /** Method to insert records in pm_tsmst(Trouble Shooting Guide) table.  */
	  private void exeADDTSG(int P_intROWNO)
	  { 
		  try
		  {
			  	this.setCursor(cl_dat.M_curWTSTS_pbst);
			    String strSQLQRY=" select count(*) from PM_TSMST where";
				strSQLQRY+=" TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(TS_STSFL,'')<>'X' AND TS_TSGNO='"+txtTSGNO.getText().toString()+"'";
				strSQLQRY+=" AND TS_TSFNO='"+strTSFNO+"' AND TS_TSCNO='"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString()+"'";
				ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count>>"+strSQLQRY);
				if(rstRSSET.next() && rstRSSET!= null)
				{
					if(rstRSSET.getInt(1)>0)
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
						M_strSQLQRY += " AND TS_TSGNO= '"+txtTSGNO.getText().toString()+"'";
						M_strSQLQRY += " AND TS_TSFNO='"+strTSFNO+"'";
						if(tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString().length()>0)
							M_strSQLQRY += " AND TS_TSCNO='"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString()+"'";
						
						//System.out.println(">>>update>>"+M_strSQLQRY);  
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else
					{
				  		cl_dat.M_flgLCUPD_pbst = true;
				  		M_strSQLQRY =" insert into PM_TSMST (TS_TSGNO,TS_TSGDS,TS_TSFNO,TS_TSFDS,TS_TSCNO,TS_TSCDS,TS_TSCRM,TS_TRNFL,TS_STSFL,TS_LUSBY,TS_LUPDT,TS_CMPCD,TS_SBSCD)";
						M_strSQLQRY += " values (";
						M_strSQLQRY += "'"+txtTSGNO.getText().toString()+"',";
						M_strSQLQRY += "'"+txtTSGDS.getText().toString()+"',";
						
				  	    M_strSQLQRY += "'"+strTSFNO+"',";
						M_strSQLQRY += "'"+strTSFDS+"',";
						
				  		M_strSQLQRY += "'"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCNO).toString()+"',";
						M_strSQLQRY += "'"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCDS).toString()+"',";
						M_strSQLQRY += "'"+tblTSCAS.getValueAt(P_intROWNO,TB3_TSCRM).toString()+"',";
				    	
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
						M_strSQLQRY += "'"+M_strSBSCD+"')";
						
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						//System.out.println("insert"+M_strSQLQRY);
					}
				}
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false;
			  cl_dat.exeDBCMT("exeADDTSG");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDTSG()");
		  }
	  }
	  
	  /** Method to insert records in PM_MPTRN(PM/OH Checklist) table.  */
	  private void exeADDMSP(int P_intROWNO)
	  { 
		  try
		  {
			    String strSQLQRY=" select count(*) from PM_MPTRN where";
				strSQLQRY+=" MP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(MP_STSFL,'')<>'X' AND MP_MSSNO='"+txtMSSNO.getText().toString()+"' AND MP_STPNO='"+tblMSSDL.getValueAt(P_intROWNO,TB4_STPNO).toString()+"'";
				ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count>>"+strSQLQRY);
				if(rstRSSET.next() && rstRSSET!= null)
				{
					if(rstRSSET.getInt(1)>0)
					{
						M_strSQLQRY = " Update PM_MPTRN set";
						
				    	M_strSQLQRY += " MP_JOBDS='"+tblMSSDL.getValueAt(P_intROWNO,TB4_JOBDS).toString()+"',";
				    	if(tblMSSDL.getValueAt(P_intROWNO,TB4_OBSDS).toString().length()>0)
				    		M_strSQLQRY += " MP_OBSDS='"+tblMSSDL.getValueAt(P_intROWNO,TB4_OBSDS).toString()+"',";
				    		
						M_strSQLQRY += " MP_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += " MP_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						M_strSQLQRY += " where MP_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
						M_strSQLQRY += " AND MP_MSSNO='"+txtMSSNO.getText().toString()+"'";
						//M_strSQLQRY += " AND MP_MSSNO='"+tblMSSNO.getValueAt(P_intROWNO,TB8_MSSNO).toString()+"'";
						M_strSQLQRY += " AND MP_STPNO='"+tblMSSDL.getValueAt(P_intROWNO,TB4_STPNO).toString()+"'";
						
						//System.out.println(">>>update>>"+M_strSQLQRY);  
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else
					{
				  		this.setCursor(cl_dat.M_curWTSTS_pbst);
				  		cl_dat.M_flgLCUPD_pbst = true;
				  		M_strSQLQRY =" insert into PM_MPTRN (MP_MSSNO,MP_STPNO,MP_JOBDS,MP_OBSDS,MP_TRNFL,MP_STSFL,MP_LUSBY,MP_LUPDT,MP_CMPCD,MP_SBSCD)";
						M_strSQLQRY += " values (";
						M_strSQLQRY += "'"+txtMSSNO.getText().toString()+"',";
						
				  		M_strSQLQRY += "'"+tblMSSDL.getValueAt(P_intROWNO,TB4_STPNO).toString()+"',";
						M_strSQLQRY += "'"+tblMSSDL.getValueAt(P_intROWNO,TB4_JOBDS).toString()+"',";
						M_strSQLQRY += "'"+tblMSSDL.getValueAt(P_intROWNO,TB4_OBSDS).toString()+"',";
				    	
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
						M_strSQLQRY += "'"+M_strSBSCD+"')";
						
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						//System.out.println("insert IN PM_MPTRN>>>"+M_strSQLQRY);
					}
				}
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false;
			  cl_dat.exeDBCMT("exeADDMSP");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDMSP()");
		  }
	  }
	  /** Method to insert records in PM_MSMST(Maintenance Spec Sheet Master) table.  */
	  private void exeADDMSS()
	  { 
		  try
		  {
			    String strSQLQRY=" select count(*) from PM_MSMST where";
				strSQLQRY+=" MS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(MS_STSFL,'')<>'X' AND MS_MSSNO='"+txtMSSNO.getText().toString()+"' ";
				ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
				//System.out.println(">>>Count>>"+strSQLQRY);
				if(rstRSSET.next() && rstRSSET!= null)
				{
					if(rstRSSET.getInt(1)>0)
					{
						M_strSQLQRY = " Update PM_MSMST set";
						M_strSQLQRY += " MS_MSSDS = '"+txtMSSDS.getText().toString()+"',";
						M_strSQLQRY += " MS_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += " MS_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						M_strSQLQRY += " where MS_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
						M_strSQLQRY += " AND MS_MSSNO='"+txtMSSNO.getText()+"'";
						//System.out.println(">>>update IN PM_MSMST>>"+M_strSQLQRY);  
						cl_dat.M_flgLCUPD_pbst = true;
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					else
					{
				  		this.setCursor(cl_dat.M_curWTSTS_pbst);
				  		cl_dat.M_flgLCUPD_pbst = true;
				  		M_strSQLQRY =" insert into PM_MSMST (MS_MSSNO,MS_MSSDS,MS_TRNFL,MS_STSFL,MS_LUSBY,MS_LUPDT,MS_CMPCD,MS_SBSCD)";
						M_strSQLQRY += " values (";
						M_strSQLQRY += "'"+txtMSSNO.getText().toString()+"',";
						M_strSQLQRY += "'"+txtMSSDS.getText().toString()+"',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
						M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";		
						M_strSQLQRY += "'"+M_strSBSCD+"')";
						
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
						//System.out.println("insert IN PM_MSMST>>>"+M_strSQLQRY);
					}
				}
		  }
		  catch(Exception L_EX)
		  {
			  cl_dat.M_flgLCUPD_pbst=false;
			  cl_dat.exeDBCMT("exeADDMSS");
			  this.setCursor(cl_dat.M_curDFSTS_pbst);
			  setMSG(L_EX,"exeADDMSS()");
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
				//System.out.println(">>>Delete>>"+M_strSQLQRY);
		  }
		  catch(Exception L_EX)
		  {
		     setMSG(L_EX,"exeDELEQP()");		
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
	 * @param   LP_FLDNM        Name of the field for which data is to be extracted
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
                   // System.out.println("LP_HSTNM"+LP_HSTNM);
			
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
							M_strSQLQRY=" SELECT distinct TG_TAGNO,TG_TAGDS,TG_PLNCD,TG_ARACD,TG_CRICD from PM_TGMST where TG_TAGNO='"+txtTAGNO.getText().toUpperCase()+"'";
							M_strSQLQRY+= " AND TG_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TG_STSFL,'')<>'X'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtTAGNO.setText(txtTAGNO.getText().toUpperCase());
								txtTAGDS.setText(nvlSTRVL(M_rstRSSET.getString("TG_TAGDS"),""));
								txtPLNCD.setText(nvlSTRVL(M_rstRSSET.getString("TG_PLNCD"),""));
								txtARACD.setText(nvlSTRVL(M_rstRSSET.getString("TG_ARACD"),""));
								txtCRICD.setText(nvlSTRVL(M_rstRSSET.getString("TG_CRICD"),""));
								txtPLNDS.setText(getCDTRN("MSTPMXXPLN"+txtPLNCD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
								txtARADS.setText(getCDTRN("MSTPMXXARA"+txtARACD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
								txtCRIDS.setText(getCDTRN("MSTPMXXCRT"+txtCRICD.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
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
				else if(input == txtPORNO)
		    	{
					if(txtMATGR.getText().length()==0)
					{
						setMSG("Enter Material Group..",'E');
						txtMATGR.requestFocus();
						return false;
					}
					if(txtPORNO.getText().length()>0)
					{
						M_strSQLQRY=" select po_strtp,po_porno,po_pordt,po_porqt,po_porrt,po_porvl from mm_pomst where po_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' AND po_porno = '"+txtPORNO.getText().trim()+"'";
						M_strSQLQRY += " AND po_matcd ='"+txtMATGR.getText().trim()+"'";
						//System.out.println("INPVF  P.O.NO : "+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							txtPORNO.setText(txtPORNO.getText());
							txtPORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("po_pordt")));
							txtPORVL.setText(nvlSTRVL(M_rstRSSET.getString("po_porqt"),""));
						}
						else
						{
							setMSG("Enter valid P.O.No..",'E');
							return false;
						}
						M_rstRSSET.close();
					}
		    	}
				if(((JTextField)input).getText().length() == 0)
					return true;
				
				else if(input == txtEQPID)
				{	
					try
					{
						/*if(txtEQPID.getText().length()==0)
						{
							setMSG("Enter Equipment Id..",'E');
							txtEQPID.requestFocus();
							return false;
						}*/
						String strEQPID=txtEQPID.getText().toString();
						
						if(txtEQPID.getText().length()>0)
						{
							M_strSQLQRY=" SELECT EQ_EQPID,EQ_EQPDS from PM_EQMST  ";
							M_strSQLQRY+= " where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EQ_STSFL,'')<>'X' AND EQ_EQPID ='"+txtEQPID.getText().toUpperCase()+"' ";
							
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								L_strEQPID_DL=nvlSTRVL(M_rstRSSET.getString("EQ_EQPID"),"");
								if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
								{
									txtEQPID.setText(L_strEQPID_DL);
									txtEQPDS.setText(nvlSTRVL(M_rstRSSET.getString("EQ_EQPDS"),""));
									clrCOMP();
									getEQPDL();/**Fetch Equipment detail records*/
									
									txtEQMTP_DS.setText(getCDTRN("MSTPMXXEQT"+txtEQMTP.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
									if(txtEQSTP.getText().length()>0)
										txtEQSTP_DS.setText(getCDTRN("MSTPMXXEST"+txtEQSTP.getText().toString().trim(),"CMT_CODDS",hstCDTRN));
									
									getEQPSP(); /**Fetch Equipment Specification records*/
									if(txtTSGNO.getText().length()>0)
										getTSFDL();
									//if(txtMSSNO.getText().length()>0)
										getMSSNO();
									
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
										getHSTRY();
								}
							}
							else
							{		
								if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
								{
									setMSG("Enter valid Equipment Id..",'E');
									txtEQPID.requestFocus();
									return false;
								}
							}
							M_rstRSSET.close();
						}
						if(strEQPID.equals(L_strEQPID_DL) && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
						{
							setMSG("This Equipment Id Already exist..",'E');
							txtEQPID.requestFocus();
							return false;
						}
							
						
					}
					catch(Exception e)
					{
						setMSG(e,"error in Equipment id  InputVerifier ");
					}
				}

				
				else if(input == txtEQMTP)
				{	
					try
					{
						if(txtEQMTP.getText().length()>0)
						{
							txtEQMTP.setText(txtEQMTP.getText().toUpperCase());
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
						if(txtEQSTP.getText().length()>0)
						{
							txtEQSTP.setText(txtEQSTP.getText().toUpperCase());
							if(!hstCDTRN.containsKey("MSTPMXXEST"+txtEQSTP.getText().toString().toUpperCase()))
							{
								setMSG("Enter Valid Equipment Sub Type ",'E');
								return false;
							}
							else
							{
								getATTRB();
								txtEQSTP_DS.setText(getCDTRN("MSTPMXXEST"+txtEQSTP.getText().toString().toUpperCase(),"CMT_CODDS",hstCDTRN));
							}	
						}	
					}
					catch(Exception e)
					{
						setMSG(e,"error in  Equipment Sub Type InputVerifier");
					}
				}
				/*
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
						txtARACD.setText(txtARACD.getText().toUpperCase());
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

				}*/
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
				
				else if(input == txtTSGNO)
				{	
					try
					{	
						if(txtTSGNO.getText().length()>0 )
			    		{
							M_strSQLQRY=" Select TS_TSGNO,TS_TSGDS from PM_TSMST where TS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TS_STSFL,'')<>'X'";
							M_strSQLQRY+= " AND TS_TSGNO ='"+txtTSGNO.getText().toUpperCase()+"' ";
						
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtTSGNO.setText(txtTSGNO.getText());
								txtTSGNO.setText(nvlSTRVL(M_rstRSSET.getString("TS_TSGNO"),""));
								getTSFDL();
							}	
							else
							{
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
								{
									setMSG("Enter valid T.S.G No ",'E');
									txtTSGNO.requestFocus();
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
				else if(input == txtMATGR)
				{	
					try{
						if(txtMATGR.getText().length()>0)
						{
							M_strSQLQRY=" select ct_matcd,ct_matds from co_ctmst where  SUBSTRING(ct_matcd,3,8) = '0000000A' and isnull(CT_STSFL,'') <> 'X'";
							M_strSQLQRY += " AND ct_matcd ='"+txtMATGR.getText().toUpperCase()+"'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtMATGR.setText(txtMATGR.getText());
								txtMATDS.setText(nvlSTRVL(M_rstRSSET.getString("ct_matds"),""));	
							}
							else
							{
								setMSG("Enter valid Material Group ",'E');
								return false;
							}
							M_rstRSSET.close();
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Material Group InputVerifier");
					}
				}
				else if(input == txtPORNO)
		    	{
					if(txtMATGR.getText().length()==0)
					{
						setMSG("Enter Material Group..",'E');
						txtMATGR.requestFocus();
						return false;
					}
					if(txtPORNO.getText().length()>0)
					{
						M_strSQLQRY=" select po_strtp,po_porno,po_pordt,po_porqt,po_porrt,po_porvl from mm_pomst where po_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' AND po_porno = '"+txtPORNO.getText().trim()+"'";
						M_strSQLQRY += " AND po_matcd ='"+txtMATGR.getText().trim()+"'";
						//System.out.println("INPVF  P.O.NO : "+M_strSQLQRY);
						M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							txtPORNO.setText(txtPORNO.getText());
							txtPORDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("po_pordt")));
							txtPORVL.setText(nvlSTRVL(M_rstRSSET.getString("po_porvl"),""));
						}
						else
						{
							setMSG("Enter valid P.O.No..",'E');
							return false;
						}
						M_rstRSSET.close();
					}
		    	}
				else if(input == txtMFRCD)
				{	
					try{
						if(txtMFRCD.getText().length()>0)
						{
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
					}
					catch(Exception e)
					{
						setMSG(e,"error in Mfr.Code InputVerifier");
					}
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
								getMSSNO();
							}	
							else
							{
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
								{
									setMSG("Enter valid Mss No ",'E');
									txtMSSNO.requestFocus();
									return false;
								}
							}
							M_rstRSSET.close();
						}
					}
					catch(Exception e)
					{
						setMSG(e,"error in Mss No InputVerifier  ");
					}
				}
				else if(input == txtINSDT)
				{	
					if(txtINSDT.getText().length()>0)
					{
						if(M_fmtLCDAT.parse(txtINSDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
						{
							setMSG("Installed Date must be Samller than current date..",'E');
							txtINSDT.requestFocus();
							return false;
						}
					}
				
				}    
				
				else if(input == txtPORDT)
				{
					if(txtPORDT.getText().length()>0)
					{
						if(M_fmtLCDAT.parse(txtPORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
						{
							setMSG("Purchase order date must be Samller than current date",'E');
							txtPORDT.requestFocus();
							return false;
						}
						else if(M_fmtLCDAT.parse(txtPORDT.getText().toString()).compareTo(M_fmtLCDAT.parse(txtINSDT.getText().toString()))>0)
						{
							setMSG("Purchase order date must be Samller than Installed date",'E');
							txtPORDT.requestFocus();
							return false;
						}
					}
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
		   		if(getSource()==tblEQPSP)
			    {
		   			try
		   			{
						if(P_intCOLID == TB1_ATDSC)
	    			    {
							if(tblEQPSP.getValueAt(P_intROWID,TB1_ATDSC).toString().length()>0)
							{
								tblEQPSP.setValueAt(tblEQPSP.getValueAt(P_intROWID,TB1_ATDSC).toString().replace("'","`"),P_intROWID,TB1_ATDSC);
								tblEQPSP.setValueAt(tblEQPSP.getValueAt(P_intROWID,TB1_ATDSC).toString().toUpperCase(),P_intROWID,TB1_ATDSC);
							}
						}
						else if(P_intCOLID == TB1_SPDSC)
	    			    {
							if(tblEQPSP.getValueAt(P_intROWID,TB1_SPDSC).toString().length()>0)
							{
								tblEQPSP.setValueAt(tblEQPSP.getValueAt(P_intROWID,TB1_SPDSC).toString().toUpperCase(),P_intROWID,TB1_SPDSC);
								tblEQPSP.setValueAt(tblEQPSP.getValueAt(P_intROWID,TB1_SPDSC).toString().replace("'","`"),P_intROWID,TB1_SPDSC);
							}
	    			    }
				    }
					catch(Exception e)
					{
						setMSG(e,"error in tblEQPSP InputVerifier  ");
					}
			    }
		   		
		   		else if(getSource()==tblTSFAL)
			    {
					try
					{
						if(P_intCOLID == TB2_TSFNO)
	    			    {
							tblTSFAL.setValueAt(tblTSFAL.getValueAt(P_intROWID,TB2_TSFNO).toString().toUpperCase(),P_intROWID,TB2_TSFNO);
					   	     for(int j=0;j<tblTSFAL.getRowCount();j++)
					   	     {	
					   	    	if(tblTSFAL.getValueAt(P_intROWID,TB2_TSFNO).toString().length()>0 && tblTSFAL.getValueAt(j,TB2_TSFNO).toString().length()>0 && j !=P_intROWID)
					   			if(tblTSFAL.getValueAt(P_intROWID,TB2_TSFNO).toString().compareTo(tblTSFAL.getValueAt(j,TB2_TSFNO).toString().trim())==0)
								{
									setMSG("This Failure No Already Exist for "+txtTSGNO.getText()+" T.S.G. No..",'E');
									return false;
								}
					   	     }
	    			    }
						if(P_intCOLID == TB2_TSFDS)
	    			    {
					   	    if(tblTSFAL.getValueAt(P_intROWID,TB2_CHKFL).toString().equals("true"))
					   		{
				   		    	for(int i=0;i<tblTSFAL.getRowCount();i++)
				   		    	{	
					   		    	if(tblTSFAL.getValueAt(i,TB2_CHKFL).toString().equals("true") && i!=P_intROWID)
						   		    {
					   		    		setMSG("Please Select One Row at a Time",'E');
						   				 return false;
						   		    }
				   		    	}
					   		  tblTSFAL.setValueAt(tblTSFAL.getValueAt(P_intROWID,TB2_TSFDS).toString().toUpperCase(),P_intROWID,TB2_TSFDS);
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
			   				tblTSCAS.setValueAt(tblTSCAS.getValueAt(P_intROWID,TB3_TSCNO).toString().toUpperCase(),P_intROWID,TB3_TSCNO);
					   	     for(int j=0;j<tblTSCAS.getRowCount();j++)
					   	     {	
					   			if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCNO).toString().length()>0 && tblTSCAS.getValueAt(j,TB3_TSCNO).toString().length()>0 && j !=P_intROWID)
					   			if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCNO).toString().compareTo(tblTSCAS.getValueAt(j,TB3_TSCNO).toString().trim())==0)
								{
									setMSG("This Cause No Already Exist for "+txtTSGNO.getText()+" T.S.G. No..",'E');
									return false;
								}
					   	     }
	    			    }
			   			else if(P_intCOLID == TB3_TSCDS)
	    			    {
							if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCDS).toString().length()>0)
							{
								tblTSCAS.setValueAt(tblTSCAS.getValueAt(P_intROWID,TB3_TSCDS).toString().toUpperCase(),P_intROWID,TB3_TSCDS);
								tblTSCAS.setValueAt(tblTSCAS.getValueAt(P_intROWID,TB3_TSCDS).toString().replace("'","`"),P_intROWID,TB3_TSCDS);
							}
	    			    }
						else if(P_intCOLID == TB3_TSCRM)
	    			    {
							if(tblTSCAS.getValueAt(P_intROWID,TB3_TSCRM).toString().length()>0)
							{
								tblTSCAS.setValueAt(tblTSCAS.getValueAt(P_intROWID,TB3_TSCRM).toString().toUpperCase(),P_intROWID,TB3_TSCRM);
								tblTSCAS.setValueAt(tblTSCAS.getValueAt(P_intROWID,TB3_TSCRM).toString().replace("'","`"),P_intROWID,TB3_TSCRM);
	    			   
							}
	    			    }
		   			}
					catch(Exception e)
					{
						setMSG(e,"error in tblTSCAS InputVerifier  ");
					}
			    }
		   		else if(getSource()==tblMSSDL)
			    {
		   			try
		   			{
			   			if(P_intCOLID == TB4_STPNO)
	    			    {
			   				tblMSSDL.setValueAt(tblMSSDL.getValueAt(P_intROWID,TB4_STPNO).toString().toUpperCase(),P_intROWID,TB4_STPNO);
					   	     for(int j=0;j<tblMSSDL.getRowCount();j++)
					   	     {	
					   	    	if(tblMSSDL.getValueAt(P_intROWID,TB4_STPNO).toString().length()>0 && tblMSSDL.getValueAt(j,TB4_STPNO).toString().length()>0 && j !=P_intROWID)
					   			if(tblMSSDL.getValueAt(P_intROWID,TB4_STPNO).toString().compareTo(tblMSSDL.getValueAt(j,TB4_STPNO).toString().trim())==0)
								{
									setMSG("This Step No Already Exist for "+txtMSSNO.getText()+" Mss No..",'E');
									return false;
								}
					   	     }
	    			    }
			   			else if(P_intCOLID == TB4_JOBDS)
	    			    {
							if(tblMSSDL.getValueAt(P_intROWID,TB4_JOBDS).toString().length()>0)
							{
								tblMSSDL.setValueAt(tblMSSDL.getValueAt(P_intROWID,TB4_JOBDS).toString().toUpperCase(),P_intROWID,TB4_JOBDS);
								tblMSSDL.setValueAt(tblMSSDL.getValueAt(P_intROWID,TB4_JOBDS).toString().replace("'","`"),P_intROWID,TB4_JOBDS);
							}
	    			    }
						else if(P_intCOLID == TB4_OBSDS)
	    			    {
							if(tblMSSDL.getValueAt(P_intROWID,TB4_OBSDS).toString().length()>0)
							{
								tblMSSDL.setValueAt(tblMSSDL.getValueAt(P_intROWID,TB4_OBSDS).toString().toUpperCase(),P_intROWID,TB4_OBSDS);
								tblMSSDL.setValueAt(tblMSSDL.getValueAt(P_intROWID,TB4_OBSDS).toString().replace("'","`"),P_intROWID,TB4_OBSDS);
							}
	    			    }
		   			}
					catch(Exception e)
					{
						setMSG(e,"error in tblMSSDL InputVerifier  ");
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

	
	
	