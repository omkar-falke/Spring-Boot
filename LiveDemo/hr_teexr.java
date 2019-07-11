/**
 * Application System : Human Resources Management System
 * Module : Attendance Exceptions Regularisation
 * Program Name : hr_teexr.java
 * Author : AMJ
 * Modified by : SRD
 * Purpose : Processing of Shift schedule and Attendance record and fetching and displaying exceptions for verification
 * and authorisation in interactive mode.


System Name     : Attendance System

Program Name    :  Exception Regularization
Source Directory : f:\source\splerp2\hr_teexr.java                        Executable Directory : f:\exec\splerp2\hr_teexr.class

Purpose : This module maintains Shift Working Exception Authorization for Specified Department, Period and specified employee category.

List of tables used :
 Table Name		Primary key	            						Operation done
	                        									Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
HR_SWMST        SW_SBSCD,SW_EMPNO,SW_WRKDT                        /       /          /        /
HR_EPMST		EP_EMPNO						    		                         /
HR_SSTRN        SS_SBSCD,SS_EMPNO,SS_WRKDT                                           /          
HR_SWTRN        SWT_SBSCD,SWT_EMPNO,SWT_WRKDT,SWT_SRLNO                              /
CO_CDTRN		CMT_CGMTP,CMT_CGSTP,CMT_CODCD				                         /
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name		Column Name			Table name		Type/Size		Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
TxtDPTCD		EP_DPTCD			HR_EPMST		VARCHAR(3)		Department Code
TxtEMPCT		CMT_CODCD			CO_CDTRN		VARCHAR(2)		Employee Category
TxtWRKDT		SW_WRKDT			HR_SWMST		DATE			Work Date

for Table with Exceptions (tblTEEXR)
TB_EMPNO		SW_EMPNO			HR_SWMST		VARCHAR(8)		Employee No
TB_EMPNM		EP_EMPNM			HR_EPMST		VARCHAR(20)		Employee Name
TB_INCTM		SW_INCTM			HR_SWMST		TIMESTAMP		Incomming Time
TB_OUTTM		SW_OUTTM			HR_SWMST		TIMESTAMP		Outgoing Time
TB_ORGSH		SW_ORGSH			HR_SWMST		VARCHAR(1)		Original Shift
TB_CURSH		SW_CURSH			HR_SWMST		VARCHAR(1)		Running Shift
TB_WRKSH		SW_WRKSH			HR_SWMST		VARCHAR(1)		Working Shift
TB_ACTWK		SW_ACTWK			HR_SWMST		TIME  		    Actual Working
TB_SHRWK		SW_SHRWK			HR_SWMST		TIME            Short Working
TB_EXTWK		SW_EXTWK			HR_SWMST		TIME     		Extra Working
TB_OVTWK		SW_OVTWK			HR_SWMST		TIME     		Over Time
TB_LVECD		SW_LVECD			HR_SWMST		VARCHAR(2)    	Leave Code
TB_EMPCT        EP_EMPCT            HR_EPMST        VARCHAR(1)      Employee Category

for Table without Exceptions (tblTEEXR1)
TB_EMPNO1		SW_EMPNO			HR_SWMST		VARCHAR(8)		Employee No
TB_EMPNM1		SW_EMPNM			HR_SWMST		VARCHAR(20)		Employee Name
TB_INCTM1		SW_INCTM			HR_SWMST		TIMESTAMP		Incomming Time
TB_OUTTM1		SW_OUTTM			HR_SWMST		TIMESTAMP		Outgoing Time
TB_WRKSH1		SW_WRKSH			HR_SWMST		VARCHAR(1)		Working Shift
TB_ACTWK1		SW_ACTWK			HR_SWMST		TIME  		    Actual Working
TB_LVECD1		SW_LVECD			HR_SWMST		VARCHAR(2)    	Leave Code
TB_EMPCT_SW       EP_EMPCT            HR_EPMST        VARCHAR(1)      Employee Category
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description		         Display Columns			Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
TxtDPTCD	Department Code,Description	     cmt_codcd,cmt_codds		CO_CDTRN-SYS/COXXDPT
TxtEMPCT	Employee Category,Description	 cmt_codcd,cmt_codds		CO_CDTRN-SYS/HRXXEPC
-----------------------------------------------------------------------------------------------------------------------------------------------------

Validations :

Shift records will be picked up from hr_sstrn as per specified period (ss_wrkdt)
Data will be picked up from hr_swtrn as per specified period (ss_wrkdt)
If Emp. Category is not specified all Emp.Categories will be listed

Other  Requirements :
->'Records With Exception' table contains records that have some exceptions.
->User can Remove all the Exceptions.
->If Incomming Or Outgoing time is not displayed then user can mannually enter ACTUAL IN TIME and ACTUL OUT TIME.
->Hours Worked, Less Working, Extra Working, Over Time will be calculated form incomming times and outgoing times
  depending on Worked Shift. 
->User Can Change Worked Shift Manually.
->According to Worked Shift Standard in time and Sandard out Time will be calculated.
  and again Hours Worked, Less Working, Extra Working, Over Time will be calculated form incomming times and outgoing times
  depending on Worked Shift. 
->'Records without Exception' Table contains records that dont have any exceptions.
->User can Authorise by clicking check box for Authorization.
->When user clicks on View checkbox then in-time and out-time for previous , current and next day will be displayed.
->status flag becomes '9' for authorised records and for the records without exceptions.
->status flag becomes '2' for modified records but not authorised.

Exceptions:-
->If any one of in-time or out-time is blank.
->if both are blank without leave or weekly off.
->If employee has leave,in-time and out-time on a perticular day.
->in-time and out-time on weekly off.

How to handle Exception for Leave
->if user comes to work on leave day then leave must be deleted from leave transaction.
->if there is leave for perticular day then enter that leave in leave transaction.
 

Added new tab for Over Time Authorization.
-> data will processed form hr_swtrn,hr_swmst,hr_extrn to create data for OT.
-> that processed data either will be authorized or be rejected accordingly.
-> authorized data will be calculated as total OT for that employee and will be stored in hr_swmst;
*/

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter.*;
import javax.swing.JTable.*;
import javax.swing.JTable;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.sql.Time;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.Timestamp;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.sql.CallableStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.Timestamp;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

/**
 */
public class hr_teexr extends cl_pbase
{
	JTextField txtEMPNO3;
	JTextField txtDPTCD;
	JTextField txtEMPCT;
	JTextField txtWRKDT1;
	JTextField txtWRKDT2;
	JTextField txtWRKDT;
	JTextField txtSTRDT;
	JTextField txtENDDT;
	
	 //// used in view data that shows previous working of the employee
	JButton btnSETTM;
	int TB1_CHKFL = 0;
	int TB1_WRKDT = 1;
	int TB1_SRLNO = 2;
	int TB1_INCTM = 3;
	int TB1_OUTTM = 4;

	JPanel pnlVEWAT;
	JPanel pnlCOTRN;
	JLabel lblEMPNM;
	JTextField txtINCTM_VW;
	JTextField txtOUTTM_VW;
	/////////////////////////////////////
	
	JButton btnRUN_MN;
	JButton btnRUN_EX;
	//JButton btnRUN_LDG;
	JButton btnRUN_OT;
	JButton btnRUN_EP;
	JButton btnOTREP;
	JCheckBox chkVEWAT;
	JCheckBox chkVEWAT_OT,chkVEWAT_OT1,chkAUTHC_OT,chkREJCT_OT;
	JCheckBox chkVEWAT_EP,chkVEWAT_EP1,chkAUTHC_EP;
	JCheckBox chkAUTHC;
	java.util.Date datWRKDT_2;		// two day Previous
	java.util.Date datWRKDT_1;		// Previous day
	java.util.Date datWRKDT1;		// Current Working day
	java.util.Date datWRKDT2;		// Next Day
	java.util.Date datWRKDT3;		// Next Day Ending date
	java.util.Date datWRKDT;		// Working date to be considered depending on In/Out time
	private String strEMPNO_LOGIN;
	
	JLabel lblEMPNM3,lblDPTNM,lblEMPCT;
	private cl_JTable tblTEEXR, tblVEWAT,tblCOTRN,tblOVTDT,tblAUOVT,tblEPSDT,tblAUEPS; 
	
	////for Exception Table
	private static CallableStatement cstSWTRN;
	private final int TB_CHKFL=0;
	private final int TB_EMPNO=1;
	private final int TB_EMPNM=2;
	private final int TB_INCTM=3;
	private final int TB_OUTTM=4;
	//private final int TB_ORGSH=5;
	private final int TB_CURSH=5;
	private final int TB_WRKSH=6;
	boolean flgALLDPT;
	
	private final int TB_ACTWK=7;
	private final int TB_OVTWK=8;
	private final int TB_LVECD=9;
	private final int TB_LVEQT=10;
	private final int TB_AUTHC=11;
	private final int TB_OBJCD=12;
	private final int TB_INCTM_A=13;			 //Incoming Time Actual
	private final int TB_OUTTM_A=14;			 //Outgoing Time Actual

	private final int TB_EMPCT_SW=15;				 //Employee category
	private final int TB_VEWAT=16;
	private final int TB_INCTM_S=17;             //Incoming Time Standard
	private final int TB_OUTTM_S=18;			 //Outgoing Time Standard
	private final int TB_EXPFL=19;			 //Exception flag
	private final int TB_WRKDT=20;			 //Outgoing Time Actual
	private final int TB_SHRWK=21;
	private final int TB_EXTWK=22;
	private final int TB_REFDT=23;		// Reference date for Comp Off / Off Change availing
	public Hashtable<String,String> hstLVDTL;
	public Hashtable<String,String> hstMAXTM_SWT;
	public Hashtable<String,String> hstMINTM_SWT;
	public Hashtable<String,String> hstAUTREC;
	private Hashtable<String,String[]> hstEMPTM=new Hashtable<String,String[]>();
	private Hashtable<String,String> hstINCST=new Hashtable<String,String>();
	private Hashtable<String,String> hstOUTST=new Hashtable<String,String>();
	private Hashtable<String,String> hstWRKSH=new Hashtable<String,String>();
	private Vector<String> vtrEMPTM=new Vector<String>();
	public Vector<String> vtrCOTRN_W=new Vector<String>();
	public Vector<String> vtrCOTRN_A=new Vector<String>();
	///for Regular table
	private cl_JTable tblTEEXR1; 
	private cl_JTable tblOBJCD; 
	private final int TB_CHKFL1=0;
	private final int TB_EMPNO1=1;
	private final int TB_EMPNM1=2;
	private final int TB_INCTM1=3;
	private final int TB_OUTTM1=4;
	private final int TB_WRKSH1=5;
	private final int TB_ACTWK1=6;
	private final int TB_LVECD1=7;
	private final int TB_LVEQT1=8;
	private final int TB_EMPCT_SW1=9;
	private final int TB_WRKDT1=10;
	private final int TB_SHRWK1=11;
	private final int TB_OVTHR=12;
	//private final int TB_POTTM=13;
	private final int TB_PEDTM=13;
	private final int TB_PEATM=14;
	private final int TB_OEXTM=15;	
	private final int TB_PRSFL=16;
	private final int TB_WRKVL=17;
	
	
	//for tblOVTDT
	private final int TB_CHKFL_OT=0;
	private final int TB_WRKDT_OT=1;
	private final int TB_EMPCT_OT=2;
	private final int TB_EMPNO_OT=3;
	private final int TB_EMPNM_OT=4;
	private final int TB_WRKSH_OT=5;
	private final int TB_ESTTM_OT=6;
	private final int TB_EENTM_OT=7;
	private final int TB_OVTWK_OT=8;
	private final int TB_OVTHR_OT=9;
	private final int TB_AUTHC_OT=10;
	private final int TB_REJCT_OT=11;
	private final int TB_INCTM_OT=12;
	private final int TB_OUTTM_OT=13;
	private final int TB_AUTBY_OT=14;
	private final int TB_VEWAT_OT=15;
	private final int TB_POTTM_OT=16;
	private final int TB_SHRWK_OT=17;
	private final int TB_STRTM_OT=18;
	private final int TB_ENDTM_OT=19;
	private final int TB_INCST_OT=20;
	private final int TB_OUTST_OT=21;
	private final int TB_MNLFL_OT=22;
	
	
	int TB_CHKFL_EP = 0; 				
	int TB_WRKDT_EP = 1;              
	int TB_EMPNO_EP = 2;              
	int TB_EMPNM_EP = 3;              
	int TB_EOTTM_EP = 4; 				
	int TB_EINTM_EP = 5;              
	int TB_AOTTM_EP = 6; 				
	int TB_AINTM_EP = 7;              
	int TB_AUTHC_EP = 8;
	int TB_VEWAT_EP = 9; 
	int TB_OUTTM_EP = 10;	
	int TB_INCTM_EP = 11;
	int TB_SECTM_EP = 12;              
	int TB_DOCNO_EP = 13; 				
	int TB_WRKSH_EP = 14;              
	//int TB_EXPRD_EP = 15;				
	int TB_PEDTM_EP = 15;				
	int TB_PEATM_EP = 16;				
	int TB_OEXTM_EP = 17;				
    //int TB_OTGFL_EP = 18;				
	int TB_OFPFL_EP = 18;				
    int TB_AUTBY_EP = 19;             
    int TB_INCST_EP = 20; 			
    int TB_OUTST_EP = 21;          
	
	private boolean flgCHK_EXIST = false;
	private SimpleDateFormat fmtDBDATTM = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss"); 
	private SimpleDateFormat fmtDBDATTM_YMD = new SimpleDateFormat("yyyy-MM-dd"); 
	private SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("yyyyMMdd"); 
	private SimpleDateFormat fmtYYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm"); 
	private SimpleDateFormat fmtHHMMSS = new SimpleDateFormat("HH:mm:ss"); 
	private SimpleDateFormat fmtHHMM = new SimpleDateFormat("HH:mm"); 
	protected SimpleDateFormat fmtLCDTM_L=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	/**	For Date in Locale format "dd/MM/yyyy" */
	
	private String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	
	private JPanel pnlEPDTL;
	
	//M_fmtLCDTM.format(L_tmpDATTM)
	////for Exception Table
	JTextField txtEMPNO;
	JTextField txtEMPNM;
	JTextField txtINCTM;
	JTextField txtOUTTM;
	//JTextField txtORGSH;
	JTextField txtCURSH;
	JTextField txtWRKSH;
	JTextField txtACTWK;
	JTextField txtSHRWK;
	JTextField txtEXTWK;
	JTextField txtOVTWK;
	JTextField txtLVECD;
	JTextField txtLVEQT;
	JTextField txtINCTM_A;
	JTextField txtOUTTM_A;
	JTextField txtINCTM_S;
	JTextField txtOUTTM_S;
	JTextField txtEMPCT1;
	
	////for Regular Table
	JTextField txtEMPNO1;
	JTextField txtEMPNM1;
	JTextField txtINCTM1;
	JTextField txtOUTTM1;
	JTextField txtWRKSH1;
	JTextField txtACTWK1;
	JTextField txtLVECD1;
	JTextField txtLVEQT1;
	JTextField txtEMPCT11;
	JTextField txtCHKFL;
	
	////for overtime table
	JTextField txtEMPNO_OT,txtWRKDT_OT,txtSTRTM_OT,txtENDTM_OT,txtINCTM_OT,txtOUTTM_OT;
	JLabel lblSHRWK,lblPOTTM;
	private JCheckBox chkREDFN;////to change status flag from 9-2 so that record will come back to exception table
	private JCheckBox chkCLRTM;////check box to clear in-out times if leave code is available
	//private JCheckBox chkABEMP;
	private JCheckBox chkFETCH,chkFETCH_OT;
	private  JTabbedPane tbpMAIN;
    private  JPanel	pnlEXPDT;            /** Panel for History details */
	private  JPanel	pnlOVTDT;            /** Panel for Grade details */
	private  JPanel	pnlEPSDT;
	private  JPanel	pnlOBJCD;
	JLabel lblREGLR;
	JLabel lblEXCPN;
	String strCURDT;
	String strGRCIN = "00:15";    // incoming grace period
	String strMINOT = "00:30";
	String strMINST = "00:15";
	
	// variables used for storing perticular data in to hstEMPTM.
	// hstEMPTM ==> (String (1), String[] (2))
	// (1) ==>(LP_EMPNO+"|"+LP_WRKDT+"|"+LP_DATTM)
	// (2) ==>( TEMP[intINOFL] = I/O ) indicates intime or out time
	//        ( TEMP[intSWMFL] = Y ) If time is from HR_SWMST
	//		  ( TEMP[intEXTFL] = O/P ) If time is from HR_EXTRN and whether its official or personal
	//        ( TEMP[intRETTM] = EX_INCTM ) If Exit Pass is official and has returning time
	int intINOFL=1,
		intSWMFL=2,
		intEXTFL=3,
		intRETTM=4;
			
	int intROWCNT=0;
	int intROWCNT1=0;
	String strWHRSTR_SELOT;

	
	////FOR EXIT PASS MODULE
	
	JTextField txtINCTM_EP,txtOUTTM_EP;
	
	Vector<String> vtrSWTIN;
	Vector<String> vtrSWTOT;
	int intROWCNT_EP=0,intROWCNT1_EP=0;        // Row count for tblEPSDT
	
	hr_teexr(java.sql.Connection con,String strDATE)
	{
	    super();
	    try
	    {
			setMatrix(20,20);
			dspENTSCR();
			cl_dat.M_conSPDBA_pbst=con;
			cl_dat.M_strCMPCD_pbst="01";
			M_strSBSCD="010100";
		    cl_dat.M_stmSPDBA_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
            cl_dat.M_stmSPDBQ_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
            cl_dat.M_stmSPDBQ_pbst1 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
            cl_dat.M_stmSPDBQ_pbst2 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
            cl_dat.M_stmSPDBQ_pbst3 = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
			cl_dat.M_hstMKTCD_pbst = new Hashtable<String,String[]>();
			cl_dat.M_strUSRCD_pbst = "SYS";
			strCURDT=strDATE;
			crtHSTMKT();
		}catch(Exception L_EX)
	    {
	        setMSG(L_EX,"hr_teexr(String)");
	    }
	}

	hr_teexr()
	{
	    super(2);
	    try
	    {
			setMatrix(20,20);
			flgALLDPT = false;
			if(cl_dat.M_strUSRCD_pbst.equals("SKS") || cl_dat.M_strUSRCD_pbst.equals("S_J") || cl_dat.M_strUSRCD_pbst.equals("PDP") || cl_dat.M_strUSRCD_pbst.equals("RPN") || cl_dat.M_strUSRCD_pbst.equals("SDD"))
				flgALLDPT = true;
			dspENTSCR();
			if(!setEMPNO_LOGIN(cl_dat.M_strUSRCD_pbst))
				JOptionPane.showMessageDialog(null, "Base details for "+cl_dat.M_strUSRCD_pbst+"  Login are not available" , "Error Message", JOptionPane.ERROR_MESSAGE); 			
			strCURDT=cl_dat.M_txtCLKDT_pbst.getText();
			hstMAXTM_SWT = new Hashtable<String,String>();
			hstMINTM_SWT = new Hashtable<String,String>();
			hstAUTREC = new Hashtable<String,String>();
		}catch(Exception L_EX)
	    {
	        setMSG(L_EX,"hr_teexr()");
	    }
	}

	private void dspENTSCR()
	{
		try
		{
			//add(lblFRMDS,4,10,1,3,this,'L');
			
			add(new JLabel("Dept "),2,1,1,2,this,'L');     
			add(txtDPTCD=new TxtLimit(10),2,3,1,2,this,'L');
			add(lblDPTNM=new JLabel(),2,5,1,2,this,'L');

			add(new JLabel("Emp.No. "),3,1,1,2,this,'L');     
			add(txtEMPNO3=new TxtLimit(4),3,3,1,2,this,'L');
			add(lblEMPNM3=new JLabel(),3,5,1,2,this,'L');

			add(new JLabel("Emp Category"),4,1,1,2,this,'L');
			add(txtEMPCT=new TxtLimit(3),4,3,1,2,this,'L');
			add(lblEMPCT=new JLabel(""),4,5,1,2,this,'L');
			
			add(new JLabel("Start Date "),5,1,1,2,this,'L');     
			add(txtSTRDT=new TxtDate(),5,3,1,2,this,'L');
			
			add(new JLabel("End Date "),6,1,1,2,this,'L');     
			add(txtENDDT=new TxtDate(),6,3,1,2,this,'L');
			
			add(btnRUN_MN=new JButton("DISPLAY"),6,5,1,2,this,'L');
			//add(btnRUN_LDG=new JButton("Proc.LDG."),6,8,1,2,this,'L');
			
			tbpMAIN = new JTabbedPane();
			add(tbpMAIN,7,1,12,20,this,'L');
			
			pnlEXPDT = new JPanel(null);
		    pnlOVTDT = new JPanel(null);
			pnlEPSDT = new JPanel(null);
			pnlOBJCD = new JPanel(null);
			
			tbpMAIN.addTab("Exceptions Authorization",pnlEXPDT);			
			tbpMAIN.addTab("Exit Pass Authorization",pnlEPSDT);
			tbpMAIN.addTab("Over Time Authorization",pnlOVTDT);
			tbpMAIN.addTab("Deviations",pnlOBJCD);
						

			
			//add(chkABEMP=new JCheckBox("Absent Employees",false),5,4,1,3,this,'L');
			add(chkFETCH=new JCheckBox("Re-Process",false),1,1,1,3,pnlEXPDT,'L');
			add(btnRUN_EX=new JButton("RUN"),1,4,1,1.5,pnlEXPDT,'L');

			add(chkREDFN=new JCheckBox("Re Define As Exception",false),1,8,1,3,pnlEXPDT,'L');
			add(chkCLRTM=new JCheckBox("Clear In-Out times",false),10,1,1,5,pnlEXPDT,'L');

			////for Exception Table
			add(lblREGLR = new JLabel(" Exception Records "),2,1,1,5,pnlEXPDT,'L');     
			lblREGLR.setForeground(Color.blue);
			String[] L_strTBLHD1 = {" ",        "EpNo", "Emp Name",     "InTm",    "OutTm",  "CUR Shft","WRKD Shft", "WRKD Hrs",   "OT (calculated)",        "LVE",       "Qty",       "Auth", "Deviation","ACTUAL IN TM",  "ACTUAL OUT TM",         "Cat",        "View",    "STD IN TM",   "STD OUT TM",  "Exception",     "Wrk.Date",    "Short Wrkg.",  "Extra Wkg.","Ref.Date"};
			int[] L_intCOLSZ1 = {15,35,90,40,40,30,30,40,10,30,30,30,30,100,100,25,30,10,10,20,10,10,10,10};
			tblTEEXR = crtTBLPNL1(pnlEXPDT,L_strTBLHD1,1000,3,1,7,19,L_intCOLSZ1,new int[]{0,TB_AUTHC,TB_OBJCD,TB_VEWAT});
			
			//// for Over time
			add(chkFETCH_OT=new JCheckBox("Re-Process",false),1,1,1,3,pnlOVTDT,'L');
			add(btnRUN_OT=new JButton("RUN"),1,4,1,1.5,pnlOVTDT,'L');
			add(new JLabel("Short Working :"),1,8,1,2,pnlOVTDT,'L');
			add(lblSHRWK=new JLabel(""),1,10,1,1,pnlOVTDT,'L');
			add(new JLabel("Personal Exit :"),2,8,1,2,pnlOVTDT,'L');
			add(lblPOTTM=new JLabel(""),2,10,1,1,pnlOVTDT,'L');
			add(btnOTREP=new JButton("OT-Rep"),7,14,1,2,pnlOVTDT,'L');
			
			String[] L_strTBLHD_OT = {" ","Work Dt","Cat","EpNo","Emp Name","Shft","R-start time","R-End Time","Extra Work","OT/CO Hrs.","Auth","Un-Auth.","In Time","Out Time","Auth By","View","Personal Exit","Start Time","End Time","Short Working","Std.In","Std.Out","Manual"};
			int[] L_intCOLSZ_OT ={10,80,30,50,120,30,50,50,50,50,40,40,80,80,40,40,10,10,10,10,10,10,10,10,10};
			tblOVTDT = crtTBLPNL1(pnlOVTDT,L_strTBLHD_OT,200,3,1,4,19,L_intCOLSZ_OT,new int[]{0,TB_AUTHC_OT,TB_AUTHC_OT,TB_REJCT_OT,TB_VEWAT_OT,TB_MNLFL_OT});
			tblAUOVT = crtTBLPNL1(pnlOVTDT,L_strTBLHD_OT,200,8,1,3,19,L_intCOLSZ_OT,new int[]{0,TB_AUTHC_OT,TB_AUTHC_OT,TB_REJCT_OT,TB_VEWAT_OT,TB_MNLFL_OT});
			
			tblOVTDT.setCellEditor(TB_VEWAT_OT,chkVEWAT_OT=new JCheckBox());
			tblOVTDT.setCellEditor(TB_AUTHC_OT,chkAUTHC_OT=new JCheckBox());
			tblOVTDT.setCellEditor(TB_REJCT_OT,chkREJCT_OT=new JCheckBox());
			tblOVTDT.setCellEditor(TB_EMPNO_OT,txtEMPNO_OT=new TxtLimit(4));
			tblOVTDT.setCellEditor(TB_WRKDT_OT,txtWRKDT_OT=new TxtDate());
			tblOVTDT.setCellEditor(TB_STRTM_OT,txtSTRTM_OT=new TxtTime());
			tblOVTDT.setCellEditor(TB_ENDTM_OT,txtENDTM_OT=new TxtTime());
			tblOVTDT.setCellEditor(TB_INCTM_OT,txtINCTM_OT=new TxtLimit(16));
			tblOVTDT.setCellEditor(TB_OUTTM_OT,txtOUTTM_OT=new TxtLimit(16));
			
			tblAUOVT.setCellEditor(TB_VEWAT_OT,chkVEWAT_OT1=new JCheckBox());
			
			((JTextField)tblOVTDT.cmpEDITR[TB_EMPCT_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_EMPNM_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_WRKSH_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_OVTWK_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_STRTM_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_ENDTM_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_SHRWK_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_POTTM_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_ESTTM_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_EENTM_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_INCTM_OT]).setEditable(false);
			((JTextField)tblOVTDT.cmpEDITR[TB_OUTTM_OT]).setEditable(false);
			
			tblAUOVT.setEnabled(false);
			((JCheckBox)tblAUOVT.cmpEDITR[TB_VEWAT_OT]).setEnabled(true);
			((JCheckBox)tblAUOVT.cmpEDITR[TB_CHKFL_OT]).setEnabled(true);
			
			tblOVTDT.setInputVerifier(new TBLINPVF());
			txtEMPNO_OT.addKeyListener(this);
			
			///for Exit Pass 
			add(btnRUN_EP=new JButton("RUN"),1,4,1,1.5,pnlEPSDT,'L');
			
			add(new JLabel(" Exit Passes to be Authorized"),2,1,1,5,pnlEPSDT,'L');     
    		String[] L_strTBLHD_EP = {"","Date","Emp No","Emp Name","EP-Out","EP-In","Out Time","In Time","Auth","View","Actual-Out Time","Actual-In Time","Sec TM","DOC No","Shf","(Duty Hrs)Exit Period","(After Duty)Exit Period","(Official)Exit Period","O/P","Auth By","STD IN","STD OUT"};
    		int[] L_intCOLSZ_EP = {10,70,60,100,70,70,70,70,40,25,80,80,70,70,30,70,70,70,25,50,70,70};
    		tblEPSDT= crtTBLPNL1(pnlEPSDT,L_strTBLHD_EP,200,3,1,4,19,L_intCOLSZ_EP,new int[]{0,TB_AUTHC_EP,TB_VEWAT_EP});

			add(new JLabel("Authorized Exit Passes:"),7,1,1,5,pnlEPSDT,'L');
			tblAUEPS= crtTBLPNL1(pnlEPSDT,L_strTBLHD_EP,200,8,1,3,19,L_intCOLSZ_EP,new int[]{0,TB_AUTHC_EP,TB_VEWAT_EP});
			
			tblEPSDT.setCellEditor(TB_AUTHC_EP,chkAUTHC_EP=new JCheckBox());
			tblEPSDT.setCellEditor(TB_VEWAT_EP,chkVEWAT_EP=new JCheckBox());
			tblEPSDT.setCellEditor(TB_INCTM_EP,txtINCTM_EP=new TxtLimit(16));
			tblEPSDT.setCellEditor(TB_OUTTM_EP,txtOUTTM_EP=new TxtLimit(16));
			tblAUEPS.setCellEditor(TB_VEWAT_EP,chkVEWAT_EP1=new JCheckBox());
			tblEPSDT.setInputVerifier(new TBLINPVF());
			
			tblEPSDT.setEnabled(false);
			((JCheckBox)tblEPSDT.cmpEDITR[TB_CHKFL_EP]).setEnabled(true);
			((JCheckBox)tblEPSDT.cmpEDITR[TB_AUTHC_EP]).setEnabled(true);
			((JCheckBox)tblEPSDT.cmpEDITR[TB_VEWAT_EP]).setEnabled(true);
			if(flgALLDPT)
			{
				((JTextField)tblEPSDT.cmpEDITR[TB_INCTM_EP]).setEnabled(true);
				((JTextField)tblEPSDT.cmpEDITR[TB_OUTTM_EP]).setEnabled(true);
			}
			tblAUEPS.setEnabled(false);
			((JCheckBox)tblAUEPS.cmpEDITR[TB_VEWAT_EP]).setEnabled(true);
			
			////for Regular Table
			add(lblEXCPN = new JLabel(" Regular Records "),1,8,1,5,this,'L');     
			lblEXCPN.setForeground(Color.blue);
			String[] L_strTBLHD2 = {" ","Emp No.","Emp Name","InTm","OutTm","WRKD Shft","WRKD Hrs","LVE","Qty","Cat","Wrk.Date","Short Wrk","OT Hrs","(Duty Hrs) P-Exit","(Non-Duty Hrs) P-Exit","O-Exit","P Flag","Wrk Days"};
			int[] L_intCOLSZ2 = {15,50,120,40,40,70,70,30,30,30,50,30,30,30,30,30,30,30};
			tblTEEXR1 = crtTBLPNL1(this,L_strTBLHD2,10000,2,8,5,13,L_intCOLSZ2,new int[]{0});			
			
			//// for deviation table
			
			add(new JLabel("Unauthorized Presence / Absence (Deviations) "),2,1,1,5,pnlOBJCD,'L');     
			tblOBJCD = crtTBLPNL1(pnlOBJCD,L_strTBLHD2,200,3,1,8,19,L_intCOLSZ2,new int[]{0});			
			
			hstLVDTL = new Hashtable<String,String>();
			
			////for Exception Table 
			tblTEEXR.setCellEditor(TB_EMPNO,txtEMPNO=new TxtLimit(8));
			tblTEEXR.setCellEditor(TB_EMPNM,txtEMPNM=new TxtLimit(20));
			tblTEEXR.setCellEditor(TB_INCTM,txtINCTM=new TxtLimit(5));
			tblTEEXR.setCellEditor(TB_OUTTM,txtOUTTM=new TxtLimit(5));
			//tblTEEXR.setCellEditor(TB_ORGSH,txtORGSH=new TxtLimit(8));
			tblTEEXR.setCellEditor(TB_CURSH,txtCURSH=new TxtLimit(8));
			tblTEEXR.setCellEditor(TB_WRKSH,txtWRKSH=new TxtLimit(8));
			tblTEEXR.setCellEditor(TB_ACTWK,txtACTWK=new TxtLimit(5));
			tblTEEXR.setCellEditor(TB_SHRWK,txtSHRWK=new TxtLimit(5));
			tblTEEXR.setCellEditor(TB_EXTWK,txtEXTWK=new TxtLimit(5));
			tblTEEXR.setCellEditor(TB_OVTWK,txtOVTWK=new TxtLimit(5));
			tblTEEXR.setCellEditor(TB_LVECD,txtLVECD=new TxtLimit(2));
			tblTEEXR.setCellEditor(TB_LVEQT,txtLVEQT=new TxtNumLimit(4.1));
			tblTEEXR.setCellEditor(TB_AUTHC,chkAUTHC=new JCheckBox());
			tblTEEXR.setCellEditor(TB_INCTM_A,txtINCTM_A=new TxtLimit(16));
			tblTEEXR.setCellEditor(TB_OUTTM_A,txtOUTTM_A=new TxtLimit(16));
			tblTEEXR.setCellEditor(TB_INCTM_S,txtINCTM_S=new TxtLimit(16));
			tblTEEXR.setCellEditor(TB_OUTTM_S,txtOUTTM_S=new TxtLimit(16));
			tblTEEXR.setCellEditor(TB_EMPCT_SW,txtEMPCT1 =new TxtLimit(3));
			tblTEEXR.setCellEditor(TB_VEWAT,chkVEWAT=new JCheckBox());
			tblTEEXR.setCellEditor(TB_EXPFL,txtCHKFL =new TxtLimit(50));
			
			////for Regular Table 
			tblTEEXR1.setCellEditor(TB_EMPNO1,txtEMPNO1=new TxtLimit(8));
			tblTEEXR1.setCellEditor(TB_EMPNM1,txtEMPNM1=new TxtLimit(20));
			tblTEEXR1.setCellEditor(TB_INCTM1,txtINCTM1=new TxtLimit(5));
			tblTEEXR1.setCellEditor(TB_OUTTM1,txtOUTTM1=new TxtLimit(5));
			tblTEEXR1.setCellEditor(TB_WRKSH1,txtWRKSH1=new TxtLimit(8));
			tblTEEXR1.setCellEditor(TB_ACTWK1,txtACTWK1=new TxtLimit(8));
			tblTEEXR1.setCellEditor(TB_LVECD1,txtLVECD1=new TxtLimit(2));
			tblTEEXR1.setCellEditor(TB_LVEQT1,txtLVEQT1=new TxtNumLimit(4.1));
			tblTEEXR1.setCellEditor(TB_EMPCT_SW1,txtEMPCT11 =new TxtLimit(3));	
			
			//tblTEEXR.addMouseListener(this);
			INPVF oINPVF=new INPVF();
			tblTEEXR.setInputVerifier(new TBLINPVF());
			txtINCTM_A.addFocusListener(this);
			txtOUTTM_A.addFocusListener(this);
			txtINCTM_OT.addFocusListener(this);
			txtOUTTM_OT.addFocusListener(this);
			txtINCTM_EP.addFocusListener(this);
			txtOUTTM_EP.addFocusListener(this);
			//tblTEEXR.addKeyListener(this);
			txtLVECD.addKeyListener(this);
			btnRUN_MN.addKeyListener(this);
			//btnRUN_LDG.addKeyListener(this);
			btnRUN_EX.addKeyListener(this);
			btnRUN_OT.addKeyListener(this);
			btnRUN_EP.addKeyListener(this);
			btnOTREP.addKeyListener(this);
			txtEMPNO3.setInputVerifier(oINPVF);
			txtDPTCD.setInputVerifier(oINPVF);
			txtEMPCT.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);			
			((JCheckBox) tblTEEXR.cmpEDITR[TB_VEWAT]).addMouseListener(this);
			((JCheckBox) tblTEEXR.cmpEDITR[TB_AUTHC]).addMouseListener(this);
			
			((JCheckBox) tblOVTDT.cmpEDITR[TB_VEWAT_OT]).addMouseListener(this);
			((JCheckBox) tblAUOVT.cmpEDITR[TB_VEWAT_OT]).addMouseListener(this);
			((JCheckBox) tblOVTDT.cmpEDITR[TB_AUTHC_OT]).addMouseListener(this);
			((JCheckBox) tblOVTDT.cmpEDITR[TB_REJCT_OT]).addMouseListener(this);
			
			((JCheckBox) tblEPSDT.cmpEDITR[TB_VEWAT_EP]).addMouseListener(this);
			((JCheckBox) tblEPSDT.cmpEDITR[TB_AUTHC_EP]).addMouseListener(this);
			
			chkREDFN.addKeyListener(this);
			chkCLRTM.addKeyListener(this);
			setENBL(true);
			
			////fields which are to be put disabled
			((JTextField)tblTEEXR1.cmpEDITR[TB_EMPNO1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_EMPNM1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_INCTM1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_OUTTM1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_WRKSH1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_ACTWK1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_LVECD1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_LVEQT1]).setEditable(false);
			((JTextField)tblTEEXR1.cmpEDITR[TB_EMPCT_SW1]).setEditable(false);

			((JTextField)tblTEEXR.cmpEDITR[TB_EMPNO]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_EMPNM]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_INCTM]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_OUTTM]).setEditable(false);
			//((JTextField)tblTEEXR.cmpEDITR[TB_ORGSH]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_CURSH]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_ACTWK]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_SHRWK]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_EXTWK]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_OVTWK]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_LVECD]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_LVEQT]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_INCTM_S]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_OUTTM_S]).setEditable(false);
			((JTextField)tblTEEXR.cmpEDITR[TB_EMPCT_SW]).setEditable(false);
			if(!flgALLDPT)
			{
				((JTextField)tblTEEXR.cmpEDITR[TB_INCTM_A]).setEditable(false);
				((JTextField)tblTEEXR.cmpEDITR[TB_OUTTM_A]).setEditable(false);
			}
			vtrSWTIN = new Vector<String>();
			vtrSWTOT = new Vector<String>();
		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"dspENTSCR");
	    }
			
	}
	
	/** Fetching Employee code for specified user login
	 */ 
	private boolean setEMPNO_LOGIN(String LP_USRCD)
	{   
		try
		{
				M_strSQLQRY = "select US_EMPCD from SA_USMST where US_USRCD='"+LP_USRCD+"'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					return false;
				strEMPNO_LOGIN = L_rstRSSET.getString("US_EMPCD");
				L_rstRSSET.close();
		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"setEMPNO_LOGIN");
	    }
		return true;
	}      

	
	/**
	*/
	public void mousePressed(MouseEvent L_ME)
	{
		super.mousePressed(L_ME);
		try
		{

		}
		catch(Exception e){setMSG(e,"mousePressed");}
	}
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			String[] strINOUT = {"",""};
			if(M_objSOURC==chkVEWAT)
			{	
				tblTEEXR.setValueAt(new Boolean(false),tblTEEXR.getSelectedRow(),TB_VEWAT); 				
				if(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_EMPNO).toString().length()==4)
				{
					strINOUT = dspVEWAT(tblTEEXR,tblTEEXR.getSelectedRow(),TB_EMPNO,TB_EMPNM,TB_WRKDT);
					if(strINOUT[0].length()>0)
					{
						tblTEEXR.setValueAt(strINOUT[0],tblTEEXR.getSelectedRow(),TB_INCTM_A);
						tblTEEXR.setValueAt(strINOUT[0].substring(11,16),tblTEEXR.getSelectedRow(),TB_INCTM);
					}
					if(strINOUT[1].length()>0)
					{
						tblTEEXR.setValueAt(strINOUT[1],tblTEEXR.getSelectedRow(),TB_OUTTM_A);
						tblTEEXR.setValueAt(strINOUT[1].substring(11,16),tblTEEXR.getSelectedRow(),TB_OUTTM);
					}
					setWRKHR1(tblTEEXR.getSelectedRow());
				}
			}
			if(M_objSOURC==chkVEWAT_OT)
			{	
				tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_VEWAT_OT); 				
				if(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_EMPNO_OT).toString().length()==4)
				{
					strINOUT = dspVEWAT(tblOVTDT,tblOVTDT.getSelectedRow(),TB_EMPNO_OT,TB_EMPNM_OT,TB_WRKDT_OT);
				}
			}
			else if(M_objSOURC==chkVEWAT_OT1)
			{	
				tblAUOVT.setValueAt(new Boolean(false),tblAUOVT.getSelectedRow(),TB_VEWAT_OT); 				
				if(tblAUOVT.getValueAt(tblAUOVT.getSelectedRow(),TB_EMPNO_OT).toString().length()==4)
					strINOUT = dspVEWAT(tblAUOVT,tblAUOVT.getSelectedRow(),TB_EMPNO_OT,TB_EMPNM_OT,TB_WRKDT_OT);
			}
			else if(M_objSOURC==chkVEWAT_EP)
			{	
				tblEPSDT.setValueAt(new Boolean(false),tblEPSDT.getSelectedRow(),TB_VEWAT_EP);
				if(tblEPSDT.getValueAt(tblEPSDT.getSelectedRow(),TB_EMPNO_EP).toString().length()==4)
				{
					strINOUT = dspVEWAT(tblEPSDT,tblEPSDT.getSelectedRow(),TB_EMPNO_EP,TB_EMPNM_EP,TB_WRKDT_EP);
					if(strINOUT[0].length()>0)
					{
						tblEPSDT.setValueAt(strINOUT[0],tblEPSDT.getSelectedRow(),TB_INCTM_EP);
						tblEPSDT.setValueAt(strINOUT[0].substring(11,16),tblEPSDT.getSelectedRow(),TB_AINTM_EP);
					}
					if(strINOUT[1].length()>0)
					{
						tblEPSDT.setValueAt(strINOUT[1],tblEPSDT.getSelectedRow(),TB_OUTTM_EP);
						tblEPSDT.setValueAt(strINOUT[1].substring(11,16),tblEPSDT.getSelectedRow(),TB_AOTTM_EP);
					}
					setEXTIME(tblEPSDT.getSelectedRow());
				}
			}
			else if(M_objSOURC==chkVEWAT_EP1)
			{	
				tblAUEPS.setValueAt(new Boolean(false),tblAUEPS.getSelectedRow(),TB_VEWAT_EP);
				if(tblAUEPS.getValueAt(tblAUEPS.getSelectedRow(),TB_EMPNO_EP).toString().length()==4)
					strINOUT = dspVEWAT(tblAUEPS,tblAUEPS.getSelectedRow(),TB_EMPNO_EP,TB_EMPNM_EP,TB_WRKDT_EP);
			}
			else if(M_objSOURC==chkAUTHC_OT)
			{	
				if(((JCheckBox) tblOVTDT.cmpEDITR[TB_AUTHC_OT]).isSelected())
				{	
					if(!vldOVTDT(tblOVTDT.getSelectedRow()))
					{
						tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_AUTHC_OT);
						tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_CHKFL_OT);
						return;
					}
					tblOVTDT.setValueAt(new Boolean(true),tblOVTDT.getSelectedRow(),TB_CHKFL_OT); 
					tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_REJCT_OT); 
					tblOVTDT.setValueAt(cl_dat.M_strUSRCD_pbst,tblOVTDT.getSelectedRow(),TB_AUTBY_OT); 
					
					if(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_POTTM_OT).toString().equals(""))
						lblPOTTM.setText("00:00");
					else
						lblPOTTM.setText(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_POTTM_OT).toString());
					
					if(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_SHRWK_OT).toString().equals(""))
						lblSHRWK.setText("00:00");
					else
						lblSHRWK.setText(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_SHRWK_OT).toString());
				}
				else if(!((JCheckBox) tblOVTDT.cmpEDITR[TB_AUTHC_OT]).isSelected())
				{
					lblPOTTM.setText("");
					lblSHRWK.setText("");
					tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_CHKFL_OT); 
					tblOVTDT.setValueAt("",tblOVTDT.getSelectedRow(),TB_AUTBY_OT); 
				}
			}
			else if(M_objSOURC==chkAUTHC_EP)
			{	
				if(((JCheckBox) tblEPSDT.cmpEDITR[TB_AUTHC_EP]).isSelected())
				{	
					/*if(!vldOVTDT(tblOVTDT.getSelectedRow()))
					{
						tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_AUTHC_OT);
						tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_CHKFL_OT);
						return;
					}*/
					tblEPSDT.setValueAt(new Boolean(true),tblEPSDT.getSelectedRow(),TB_CHKFL_EP); 
				}
				else if(!((JCheckBox) tblEPSDT.cmpEDITR[TB_AUTHC_EP]).isSelected())
				{
					tblEPSDT.setValueAt(new Boolean(false),tblEPSDT.getSelectedRow(),TB_CHKFL_EP); 
				}
			}
			else if(M_objSOURC==chkREJCT_OT)
			{	
				if(((JCheckBox) tblOVTDT.cmpEDITR[TB_REJCT_OT]).isSelected())
				{	
					lblPOTTM.setText("");
					lblSHRWK.setText("");
					if(!vldOVTDT(tblOVTDT.getSelectedRow()))
					{
						tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_REJCT_OT);
						tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_CHKFL_OT);
						return;
					}
					tblOVTDT.setValueAt(new Boolean(true),tblOVTDT.getSelectedRow(),TB_CHKFL_OT); 
					tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_AUTHC_OT); 
					tblOVTDT.setValueAt(cl_dat.M_strUSRCD_pbst,tblOVTDT.getSelectedRow(),TB_AUTBY_OT); 
				}
				else if(!((JCheckBox) tblOVTDT.cmpEDITR[TB_REJCT_OT]).isSelected())
				{
					lblPOTTM.setText("");
					lblSHRWK.setText("");
					tblOVTDT.setValueAt(new Boolean(false),tblOVTDT.getSelectedRow(),TB_CHKFL_OT); 
					tblOVTDT.setValueAt("",tblOVTDT.getSelectedRow(),TB_AUTBY_OT); 
				}
			}			
			if (M_objSOURC==chkAUTHC)
			{	
				if(((JCheckBox) tblTEEXR.cmpEDITR[TB_AUTHC]).isSelected())
				{	
					if(!vldTEEXR(tblTEEXR.getSelectedRow()))
					{
						tblTEEXR.setValueAt(new Boolean(false),tblTEEXR.getSelectedRow(),TB_AUTHC);
						return;
					}
					String L_strMINTM_SWT = hstMINTM_SWT.containsKey(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_WRKDT).toString()+tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_EMPNO).toString()) ? hstMINTM_SWT.get(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_WRKDT).toString()+tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_EMPNO).toString()) : "";
					String L_strMAXTM_SWT = hstMAXTM_SWT.containsKey(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_WRKDT).toString()+tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_EMPNO).toString()) ? hstMAXTM_SWT.get(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_WRKDT).toString()+tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_EMPNO).toString()) : "";
					if(!L_strMINTM_SWT.equals("") && !L_strMINTM_SWT.equalsIgnoreCase(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_INCTM_A).toString()))
						JOptionPane.showMessageDialog(this,"Actual (punched) Incoming :"+L_strMINTM_SWT+ " \n Entered Incoming  :"+tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_INCTM_A).toString()+"\n Please Verify ...","Time Descripnacy...",JOptionPane.INFORMATION_MESSAGE);
					if(!L_strMAXTM_SWT.equals("") && !L_strMAXTM_SWT.equalsIgnoreCase(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_OUTTM_A).toString()))
						JOptionPane.showMessageDialog(this,"Actual (punched) Outgoing :"+L_strMAXTM_SWT+ " \n Entered Outgoing  :"+tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_OUTTM_A).toString()+"\n Please Verify ...","Time Descripnacy...",JOptionPane.INFORMATION_MESSAGE);
					
					// pending OFF change or work on PH tracking
					// Auto-linking of Off change blocked (To be considered in Off-change authorisation module)
					//*****************************************************************************************
					/*String[] staOCHDT={"",""};
					if(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_INCTM_A).toString().equals("") && tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_OUTTM_A).toString().equals("") && tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_LVECD).toString().equals(""))
					{	
						for(int i=0;i<vtrCOTRN_W.size();i++)
							if(vtrCOTRN_W.get(i).substring(0,4).equals(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_EMPNO).toString()))
							{
								staOCHDT = dspCOTRN(vtrCOTRN_W.get(i).substring(0,4));
								break;
							}
						
						
						tblTEEXR.setValueAt(staOCHDT[0],tblTEEXR.getSelectedRow(),TB_REFDT);
						tblTEEXR.setValueAt(staOCHDT[1],tblTEEXR.getSelectedRow(),TB_LVECD);
					}*/
					
					tblTEEXR.setValueAt(new Boolean(true),tblTEEXR.getSelectedRow(),TB_CHKFL); 
				}	
			}
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}	
	
	private boolean vldOVTDT(int P_intROWNO)
	{
		try
		{
		   setMSG("",'N');
		   if(tblOVTDT.getValueAt(P_intROWNO,TB_STRTM_OT).toString().length()<5)
		   {
			   setMSG("Please Enter Start Time in the Table for "+tblOVTDT.getValueAt(P_intROWNO,TB_EMPNM_OT).toString(),'E');
			   return false;   
		   }
		   else if(tblOVTDT.getValueAt(P_intROWNO,TB_ENDTM_OT).toString().length()<5)
		   {
			   setMSG("Please Enter End Time in the Table for "+tblOVTDT.getValueAt(P_intROWNO,TB_EMPNM_OT).toString(),'E');
			   return false;   
		   }
		   else if(tblOVTDT.getValueAt(P_intROWNO,TB_OVTHR_OT).toString().length()<5)
		   {
			   setMSG("Please Enter Over Time in the Table for "+tblOVTDT.getValueAt(P_intROWNO,TB_EMPNM_OT).toString(),'E');
			   return false;   
		   }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTEEXR()");
		}
		return true;			
	}	

	private boolean vldOVTDT1()
	{
		try
		{
			for(int i=0;i<tblOVTDT.getRowCount();i++)
			{
				if(tblOVTDT.getValueAt(i,TB_CHKFL_OT).toString().equals("true"))
				{
					if(tblOVTDT.getValueAt(i,TB_AUTHC_OT).toString().equals("false") && tblOVTDT.getValueAt(i,TB_REJCT_OT).toString().equals("false"))
					{
						setMSG("Please Click Either Authorize or Ignore for "+tblOVTDT.getValueAt(i,TB_EMPNM_OT).toString(),'E');
						return false;
					}
				}
			}
			for(int i=0;i<tblOVTDT.getRowCount();i++)
			{
				if(tblOVTDT.getValueAt(i,TB_CHKFL_OT).toString().equals("true"))
				{
					for(int j=i+1;j<tblOVTDT.getRowCount();j++)
					{
						if(tblOVTDT.getValueAt(i,TB_EMPNO_OT).toString().equals(tblOVTDT.getValueAt(j,TB_EMPNO_OT).toString())
						&& tblOVTDT.getValueAt(i,TB_WRKDT_OT).toString().equals(tblOVTDT.getValueAt(j,TB_WRKDT_OT).toString()))
						if(tblOVTDT.getValueAt(i,TB_CHKFL_OT).toString().equals("true") && tblOVTDT.getValueAt(j,TB_AUTHC_OT).toString().equals("false") && tblOVTDT.getValueAt(j,TB_REJCT_OT).toString().equals("false"))
						{
							setMSG("Please Select All Records(Either Auth. Or Ignore) for "+tblOVTDT.getValueAt(i,TB_EMPNM_OT).toString(),'E');
							return false;
						}
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTEEXR()");
		}
		return true;			
	}	
	
/**
 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(L_FE.getSource().equals(txtINCTM_A) && txtINCTM_A.getText().length()<12)
				txtINCTM_A.setText(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_INCTM_S).toString());
			if(L_FE.getSource().equals(txtOUTTM_A) && txtOUTTM_A.getText().length()<12)
				txtOUTTM_A.setText(tblTEEXR.getValueAt(tblTEEXR.getSelectedRow(),TB_OUTTM_S).toString());
			
			if(L_FE.getSource().equals(txtINCTM_OT))
			{
				if(flgALLDPT && tblOVTDT.getSelectedRow()>=intROWCNT)
				  ((JTextField)tblOVTDT.cmpEDITR[TB_INCTM_OT]).setEditable(true);
				else
				  ((JTextField)tblOVTDT.cmpEDITR[TB_INCTM_OT]).setEditable(false);

				if(txtINCTM_OT.getText().length()<12)
				{
					if(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_INCST_OT).toString().length() > 0)
						txtINCTM_OT.setText(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_INCST_OT).toString());
					else
						txtINCTM_OT.setText(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_WRKDT_OT).toString()+" ");
				}
			}
			else if(L_FE.getSource().equals(txtOUTTM_OT))
			{
				if(flgALLDPT && tblOVTDT.getSelectedRow()>=intROWCNT)
				  ((JTextField)tblOVTDT.cmpEDITR[TB_OUTTM_OT]).setEditable(true);
				else
				  ((JTextField)tblOVTDT.cmpEDITR[TB_OUTTM_OT]).setEditable(false);
				
				if(txtOUTTM_OT.getText().length()<12)
				{
					if(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_OUTST_OT).toString().length() > 0)
						txtOUTTM_OT.setText(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_OUTST_OT).toString());
					else
						txtOUTTM_OT.setText(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_WRKDT_OT).toString()+" ");
				}
			}
			else if(L_FE.getSource().equals(txtINCTM_EP))
			{
				if(txtINCTM_EP.getText().length()<16)
					txtINCTM_EP.setText(tblEPSDT.getValueAt(tblEPSDT.getSelectedRow(),TB_WRKDT_EP).toString()+" ");
			}
			else if(L_FE.getSource().equals(txtOUTTM_EP))
			{
				if(txtOUTTM_EP.getText().length()<16)
					txtOUTTM_EP.setText(tblEPSDT.getValueAt(tblEPSDT.getSelectedRow(),TB_WRKDT_EP).toString()+" ");
			}			
		}	
		catch(Exception E)
		{
			setMSG(E,"FocusGained");
		}
	}
	

/**
 */	
	void setENBL(boolean L_flgSTAT)
	{
	    super.setEnabled( L_flgSTAT);
		
	}
	
/**
 */
	public void actionPerformed(ActionEvent L_AE)
	{
		////System.out.println("actionperformed");
		super.actionPerformed(L_AE);
		try
		{
			if(txtEMPNO3.getText().length()==0)
				lblEMPNM3.setText("");	
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");	
			if(txtEMPCT.getText().length()==0)
				lblEMPCT.setText("");	
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{	
				tblTEEXR.clrTABLE();
				tblTEEXR1.clrTABLE();
				if(tblTEEXR.isEditing())
					tblTEEXR.getCellEditor().stopCellEditing();
				tblTEEXR.setRowSelectionInterval(0,0);
				tblTEEXR.setColumnSelectionInterval(0,0);
				tblOVTDT.clrTABLE();
				inlTBLEDIT(tblOVTDT);
				tblOBJCD.clrTABLE();
				inlTBLEDIT(tblOBJCD);
				if(txtSTRDT.getText().length()<8)
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));      
					M_calLOCAL.add(Calendar.DATE,-1);    
					txtSTRDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				}
				txtDPTCD.requestFocus();
			}	
			else if(M_objSOURC == txtEMPNO3 && !flgALLDPT)
			{	
				if(txtDPTCD.getText().length()<3)
				{
					setMSG("Please Enter Department Code",'E');
					txtDPTCD.requestFocus();
				}	
			}
			else if(M_objSOURC == btnSETTM)
			{	
				if(tblVEWAT.getSelectedColumn()==TB1_INCTM)
					txtINCTM_VW.setText(tblVEWAT.getValueAt(tblVEWAT.getSelectedRow(),TB1_INCTM).toString().substring(0,16));
				else if(tblVEWAT.getSelectedColumn()==TB1_OUTTM)
					txtOUTTM_VW.setText(tblVEWAT.getValueAt(tblVEWAT.getSelectedRow(),TB1_OUTTM).toString().substring(0,16));
			}	
			else if(M_objSOURC == btnRUN_EX || M_objSOURC == btnRUN_MN)   //// button verifies data entered
			{	
				if(txtSTRDT.getText().length() < 10)
				{
					setMSG("Please Enter Start Date",'E');
					txtSTRDT.requestFocus();
				}
				else if(txtENDDT.getText().length() < 10)
				{
					setMSG("Please Enter End Date",'E');
					txtENDDT.requestFocus();
				}
				else if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>=0)
				{
					setMSG("From date can not be greater than or equals Today's date..",'E');
					txtSTRDT.requestFocus();
				}
				else if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>=0)
				{
					setMSG("TO date can not be greater than or equals Today's date..",'E');
				}
				else if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
				}
				else getDATA();
			}
			/*else if(M_objSOURC == btnRUN_LDG)   //// button verifies data entered
			{	
				if(txtSTRDT.getText().length() < 10)
				{
					setMSG("Please Enter Start Date",'E');
					txtSTRDT.requestFocus();
				}
				else if(txtENDDT.getText().length() < 10)
				{
					setMSG("Please Enter End Date",'E');
					txtENDDT.requestFocus();
				}
				else
					prcLDGR(txtEMPNO.getText(),txtEMPCT.getText(),txtDPTCD.getText(),txtSTRDT.getText(),txtENDDT.getText());
			}*/
			else if(M_objSOURC == btnRUN_OT)   //// button verifies data entered
			{	
				if(txtSTRDT.getText().length() < 10)
				{
					setMSG("Please Enter Start Date",'E');
					txtSTRDT.requestFocus();
				}
				else if(txtENDDT.getText().length() < 10)
				{
					setMSG("Please Enter End Date",'E');
					txtENDDT.requestFocus();
				}
				else
				{
					//// If reprocess is req then chkFETCH_OT selected and pressed run.
					if(chkFETCH_OT.isSelected())
					{	
					    				
						prcOVTDT1();
					}
					getOTTRN();
				}
			}
			else if(M_objSOURC == btnRUN_EP)   //// button verifies data entered
			{	
				if(txtSTRDT.getText().length() < 10)
				{
					setMSG("Please Enter Start Date",'E');
					txtSTRDT.requestFocus();
				}
				else if(txtENDDT.getText().length() < 10)
				{
					setMSG("Please Enter End Date",'E');
					txtENDDT.requestFocus();
				}
				else
				{
					getDATA_EP();/**fetch record of Emp with multiple in/out**/
				}
			}
			else if(M_objSOURC == btnOTREP)   //// button verifies data entered
			{
				genRPTFL();
			}
			else if (M_objSOURC==chkREDFN)
			{	
				int P_intROWNO=0;
				boolean flgSELRW=false;////flag to check whether atleast 1 row from tblTEEXR1 is selected
				for(P_intROWNO=0;P_intROWNO<tblTEEXR1.getRowCount();P_intROWNO++)
				{
					if(tblTEEXR1.getValueAt(P_intROWNO,TB_CHKFL1).toString().equals("true"))
					{
						flgSELRW=true;
					}
				}
				for(P_intROWNO=0;P_intROWNO<tblOBJCD.getRowCount();P_intROWNO++)
				{
					if(tblOBJCD.getValueAt(P_intROWNO,TB_CHKFL1).toString().equals("true"))
					{
						flgSELRW=true;
					}
				}
				if(flgSELRW==false)
				{	
					setMSG("Please Select atleast 1 row from the Table",'E');
					chkREDFN.setSelected(false);
				}	
				else
				{
					for(P_intROWNO=0;P_intROWNO<tblTEEXR1.getRowCount();P_intROWNO++)
					{
						if(tblTEEXR1.getValueAt(P_intROWNO,TB_CHKFL1).toString().equals("true"))
						{
							setREDFN(tblTEEXR1,P_intROWNO);
						}
					}
					for(P_intROWNO=0;P_intROWNO<tblOBJCD.getRowCount();P_intROWNO++)
					{
						if(tblOBJCD.getValueAt(P_intROWNO,TB_CHKFL1).toString().equals("true"))
						{
							setREDFN(tblOBJCD,P_intROWNO);
						}
					}
					if(cl_dat.exeDBCMT("HR_SWMST"))
					{
						tblTEEXR.clrTABLE();			
						tblTEEXR1.clrTABLE();
						tblOBJCD.clrTABLE();
						chkREDFN.setSelected(false);
						setMSG("record updated successfully",'N');
					}
					else
					{
					   	JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
					    setMSG("Error in updating data..",'E');
					}
				}
			}
			/////if any of the time is available and leave is also avail then allow to clear times.
			else if (M_objSOURC==chkCLRTM)
			{	
				int P_intROWNO=0;
				boolean flgSELRW=false;////flag to check whether atleast 1 row from tblTEEXR1 is selected
				for(P_intROWNO=0;P_intROWNO<tblTEEXR.getRowCount();P_intROWNO++)
				{
					if(tblTEEXR.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
					{
						flgSELRW=true;
					}
				}
				if(flgSELRW==false)
				{	
					setMSG("Please Select atleast 1 row from the Table",'E');
					chkCLRTM.setSelected(false);
				}	
				else
				{
					/*for(P_intROWNO=0;P_intROWNO<tblTEEXR.getRowCount();P_intROWNO++)
					{
						if(tblTEEXR.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
						{
							if(tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().length()==0)
							{	
								setMSG("Leave code does not exists for "+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNM).toString(),'E');
								chkCLRTM.setSelected(false);
								return;								
							}	
						}
					}*/
					for(P_intROWNO=0;P_intROWNO<tblTEEXR.getRowCount();P_intROWNO++)
					{
						if(tblTEEXR.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
						{
							if(/*tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().length()>0
							   &&*/ (tblTEEXR.getValueAt(P_intROWNO,TB_INCTM).toString().length()>0
									|| tblTEEXR.getValueAt(P_intROWNO,TB_OUTTM).toString().length()>0
									|| tblTEEXR.getValueAt(P_intROWNO,TB_INCTM_A).toString().length()>0
									|| tblTEEXR.getValueAt(P_intROWNO,TB_OUTTM_A).toString().length()>0
								   )
							   )
							{
								tblTEEXR.setValueAt("",P_intROWNO,TB_INCTM);
								tblTEEXR.setValueAt("",P_intROWNO,TB_OUTTM);
								tblTEEXR.setValueAt("",P_intROWNO,TB_INCTM_A);
								tblTEEXR.setValueAt("",P_intROWNO,TB_OUTTM_A);
								tblTEEXR.setValueAt(new Boolean(false),P_intROWNO,TB_AUTHC);
							}
							else
								setMSG("No Exception exists for "+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNM).toString(),'E');
						}
					}
				}
			}

		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is action Performed");
		}
	}

	/**method to generate off change Notice report**/
	void genRPTFL()
	{
		try
		{
			//vtrWRKDT.clear();

			FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
			DataOutputStream D_OUT ;
			String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpotd.html";
 			int L_intSRLNO=0,L_intSRLNO1=0;
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			setMSG("Printing Report..",'N');
			
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			Runtime r = Runtime.getRuntime();
			Process p = null;
		    p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
		    
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size :9 pt \">");    
			D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			D_OUT.writeBytes("<br>");
		
			D_OUT.writeBytes("<table  border=0><tr><td align='left' width='25%' rowspan='2'><IMG src=\\\\192.168.10.207\\user\\exec\\splerp3\\spllogo_old.gif style=\"HEIGHT: 68px; LEFT: 0px; TOP: 0px; WIDTH: 68px\"></td><td align=center WIDTH='50%'><STRONG><FONT face=Arial size=5>"+cl_dat.M_strCMPNM_pbst+"</FONT></STRONG></td><td WIDTH='25%'>&nbsp;</td></tr>" +
						"<tr><td>&nbsp;</td><td align=right><FONT face=Arial size=2>DATE:"+cl_dat.M_strLOGDT_pbst+"</FONT></td></tr>"+
						"<tr><td align=left><STRONG><FONT face=Arial size=2>DEPARTMENT:"+lblDPTNM.getText()+"</FONT></STRONG></td><td>&nbsp;</td><td align=right><STRONG><FONT face=Arial size=2>TO:<strong><strong>HRD DIVISION</strong></strong></STRONG></FONT></td></tr>"+
						"<tr><td align=left><STRONG><FONT face=Arial size=2>Period From: "+txtSTRDT.getText()+ "</FONT></STRONG></td>");
			D_OUT.writeBytes("<td align=center><STRONG><FONT face=Arial size=2>Details of overtime hours performed by the employee of our department</FONT></STRONG></td><td>&nbsp;</td></tr>") ;
					
						//"<tr><td align=left><STRONG><FONT face=Arial size=2>Period From: "+txtSTRDT.getText()+ "</FONT></STRONG></td><td align=center><STRONG><FONT face=Arial size=2>Shift change/Off change details</FONT></STRONG></td><td>&nbsp;</td></tr>"+
			D_OUT.writeBytes("<tr><td align=left><STRONG><FONT face=Arial size=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To: "+txtENDDT.getText()+"</FONT></STRONG></td><td>&nbsp;</td><td>&nbsp;</td></tr></table>" +		
						
						" </HEAD> <BODY><P><PRE style =\" font-size :10 pt \">");    
			D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				
			
			//TABLE HEADING			
			D_OUT.writeBytes("<p><TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray  cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
			D_OUT.writeBytes("<tr>");
		    D_OUT.writeBytes("<th align ='center' width='1%' rowspan='2'><b><FONT face='Arial' size='2'>Sr.No</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='5%' rowspan='2'><b><FONT face='Arial' size='2'>Name of the Employee</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='2%' rowspan='2'><b><FONT face='Arial' size='2'>Emp No.</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='2%' colspan='3'><b><FONT face='Arial' size='2'>Original Shift</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='2%' colspan='2'><b><FONT face='Arial' size='2'>OT Hours</font></b></th>");
			D_OUT.writeBytes("<th align ='center' width='2%' rowspan='2'><b><FONT face='Arial' size='2'>Total OT Hrs.</font></b></th>");
			
			D_OUT.writeBytes("</tr>");
			
			D_OUT.writeBytes("<tr>");
			  D_OUT.writeBytes("<th align ='center' width='1%'><b><FONT face='Arial' size='2'>Work Date</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='1%'><b><FONT face='Arial' size='2'>From</font></b></th>");
			D_OUT.writeBytes("<th align ='center' width='1%'><b><FONT face='Arial' size='2'>To</font></b></th>");
			D_OUT.writeBytes("<th align ='center' width='1%'><b><FONT face='Arial' size='2'>From</font></b></th>");
			D_OUT.writeBytes("<th align ='center' width='1%'><b><FONT face='Arial' size='2'>To</font></b></th>");
			D_OUT.writeBytes("</tr>");
			
			//display records in report table
							
			for(int i=0;i<intROWCNT1;i++)
			{
				if(tblAUOVT.getValueAt(i,TB_CHKFL_OT).toString().equals("true")) 
				if(tblAUOVT.getValueAt(i,TB_AUTHC_OT).toString().equals("true"))
				{
					L_intSRLNO+=1;
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes("<td align ='center' width='1%'><FONT face='Arial' size='2'>"+L_intSRLNO+"</font></td>");
					D_OUT.writeBytes("<td align ='left' width='5%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_EMPNM_OT)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_EMPNO_OT)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_WRKDT_OT)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_INCST_OT).toString().substring(11,16)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_OUTST_OT).toString().substring(11,16)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_STRTM_OT).toString()+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_ENDTM_OT).toString()+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='2'>"+tblAUOVT.getValueAt(i,TB_OVTHR_OT).toString()+"</font></td>");
					D_OUT.writeBytes("</tr>");
				}
			}
			D_OUT.writeBytes("</p></table><br><br>");
			D_OUT.writeBytes("\n"+padSTRING('R',"_______________________________________",65));
			D_OUT.writeBytes(padSTRING('L',"___________",55));
			D_OUT.writeBytes("\n"+padSTRING('R',"  SHIFT IN-CHARGE/ENGINEER/OFFICER",65));
			D_OUT.writeBytes(padSTRING('L',"  HOS/HOD",45));
			

			D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			
			D_OUT.close();
			F_OUT.close();
		
			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genRPTFL()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	private void setREDFN(cl_JTable LP_TBL,int P_intROWNO) 
	{
		try
		{
			if(vtrCOTRN_A.contains(LP_TBL.getValueAt(P_intROWNO,TB_EMPNO1).toString()+"|"+LP_TBL.getValueAt(P_intROWNO,TB_WRKDT1).toString()+"|"+LP_TBL.getValueAt(P_intROWNO,TB_LVECD1).toString()))
				{JOptionPane.showMessageDialog(this,"Redefining blocked for "+LP_TBL.getValueAt(P_intROWNO,TB_EMPNO1).toString()+" ( has already availed "+LP_TBL.getValueAt(P_intROWNO,TB_LVECD1)+" for "+LP_TBL.getValueAt(P_intROWNO,TB_WRKDT1).toString()+")","Message ....",JOptionPane.INFORMATION_MESSAGE); return;}
			String L_strWHRSTR_SW = " SW_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO='"+LP_TBL.getValueAt(P_intROWNO,TB_EMPNO1).toString()+"' and sw_wrkdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TBL.getValueAt(P_intROWNO,TB_WRKDT1).toString()))+"' ";
			String L_strWHRSTR_OT = " OT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and OT_EMPNO='"+LP_TBL.getValueAt(P_intROWNO,TB_EMPNO1).toString()+"' and ot_wrkdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TBL.getValueAt(P_intROWNO,TB_WRKDT1).toString()))+"' ";
			String L_strWHRSTR_EX = " EX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and EX_EMPNO='"+LP_TBL.getValueAt(P_intROWNO,TB_EMPNO1).toString()+"' and ex_docdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_TBL.getValueAt(P_intROWNO,TB_WRKDT1).toString()))+"' ";
			String strSQLQRY1;
			strSQLQRY1 = " update HR_SWMST set SW_STSFL = '2',SW_OVTHR='00:00',SW_OVTFL=''"; 
			strSQLQRY1+= " where "+L_strWHRSTR_SW;
			cl_dat.exeSQLUPD(strSQLQRY1,"updSWMST_STS");
			//System.out.println("strSQLQRY1>"+strSQLQRY1);

			strSQLQRY1 = " update HR_OTTRN set OT_STSFL = '0'"; 
			strSQLQRY1+= " where "+L_strWHRSTR_OT;
			cl_dat.exeSQLUPD(strSQLQRY1,"updSWMST_STS");
									
			strSQLQRY1 = " update HR_EXTRN set EX_STSFL = '2'"; 
			strSQLQRY1+= " where "+L_strWHRSTR_EX;
			cl_dat.exeSQLUPD(strSQLQRY1,"updSWMST_STS");
			//System.out.println("strSQLQRY1>"+strSQLQRY1);
									
			cl_dat.M_flgLCUPD_pbst = true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setREDFN()"); 
		}
	}
	/**
	 * Method to get Data from HR_SWTRN table in tblEPSDT
	 * In time -without initial In time
	 * Out time - without final Out time
	 * */
	private void getDATA_EP() 
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			tblEPSDT.clrTABLE();
			setMSG("Fetching data ...",'N');
			inlTBLEDIT(tblEPSDT);
			tblAUEPS.clrTABLE();
			inlTBLEDIT(tblAUEPS);
			
			java.sql.Timestamp L_tmsINCST=null,L_tmsOUTST=null;
			java.sql.Timestamp L_tmsINCTM=null,L_tmsOUTTM=null;
			java.sql.Timestamp L_tmsINCTM_EX=null,L_tmsOUTTM_EX=null;
			java.sql.Timestamp L_tmsXXXTM1=null,L_tmsXXXTM2=null;
			
			String L_strWRKDT="",L_strWRKSH="",L_strEMPNO="",L_strEMPNO1="",L_strEMPNM="",L_strDOCNO="",L_strOFPFL="",L_strAUTBY="";
		
			SimpleDateFormat L_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
			vtrSWTIN.clear();
			vtrSWTOT.clear();
			M_strSQLQRY= " select swt_wrkdt,swt_empno,swt_inctm,swt_outtm";
			M_strSQLQRY+=" from hr_swtrn";
			M_strSQLQRY+=" where swt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString()))+"'";
			M_strSQLQRY+=" order by swt_wrkdt,swt_empno,swt_srlno";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("swt_inctm")!=null)
						vtrSWTIN.add(M_fmtLCDAT.format(M_rstRSSET.getDate("swt_wrkdt"))+"|"+M_rstRSSET.getString("swt_empno")+"|"+M_fmtLCDTM.format(M_rstRSSET.getTimestamp("swt_inctm")));
					if(M_rstRSSET.getString("swt_outtm")!=null)
						vtrSWTOT.add(M_fmtLCDAT.format(M_rstRSSET.getDate("swt_wrkdt"))+"|"+M_rstRSSET.getString("swt_empno")+"|"+M_fmtLCDTM.format(M_rstRSSET.getTimestamp("swt_outtm")));
				}
			}
			M_rstRSSET.close();
			
			M_strSQLQRY= " select distinct ex_docdt,ex_empno,ex_eottm,ex_eintm,ex_outtm,ex_inctm,ex_pedtm,ex_peatm,ex_oextm,ex_sectm,ex_docno,ex_OFPFL,ex_autby,ex_shfcd,ex_stsfl,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' ' + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  ep_empnm";
			M_strSQLQRY+=" from hr_extrn,hr_epmst";
			M_strSQLQRY+=" where ex_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and ex_cmpcd = ep_cmpcd and ex_empno = ep_empno and isnull(ex_stsfl,'') in ('2','3') and isnull(ep_stsfl,'') <> 'U' and ep_lftdt is null and ex_docdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString()))+"'";
			M_strSQLQRY+=  (txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "")+(txtEMPNO3.getText().length()>0 ? " and EP_EMPNO = '"+ txtEMPNO3.getText() .toString().trim() +"'" : "");
			M_strSQLQRY+=" order by ex_docdt,ex_empno";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			
			L_tmsXXXTM1 = null;
			L_tmsXXXTM2 = null;
			boolean L_flgXXXTM1 = false;
			boolean L_flgXXXTM2 = false;
			boolean L_flgEXTAVL = false;
			intROWCNT_EP=0;
			intROWCNT1_EP=0;
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("ex_stsfl").equals("2"))
					{
						getEXPDL(M_rstRSSET,tblEPSDT,intROWCNT_EP);
						intROWCNT_EP++;
					}
					else
					{
						getEXPDL(M_rstRSSET,tblAUEPS,intROWCNT1_EP);
						if(M_rstRSSET.getString("ex_stsfl").equals("3"))
						{
							tblAUEPS.setValueAt(new Boolean(true),intROWCNT1_EP,TB_AUTHC_EP);	
						}
						//if(M_rstRSSET.getTime("ex_aottm")!= null)
						//	tblAUEPS.setValueAt(fmtHHMM.format(M_rstRSSET.getTime("ex_aottm")).substring(0,5),intROWCNT1_EP,TB_EXPRD_EP);
						if(M_rstRSSET.getTime("ex_pedtm")!= null)
							tblAUEPS.setValueAt(fmtHHMM.format(M_rstRSSET.getTime("ex_pedtm")).substring(0,5),intROWCNT1_EP,TB_PEDTM_EP);
						if(M_rstRSSET.getTime("ex_peatm")!= null)
							tblAUEPS.setValueAt(fmtHHMM.format(M_rstRSSET.getTime("ex_peatm")).substring(0,5),intROWCNT1_EP,TB_PEATM_EP);
						if(M_rstRSSET.getTime("ex_oextm")!= null)
							tblAUEPS.setValueAt(fmtHHMM.format(M_rstRSSET.getTime("ex_oextm")).substring(0,5),intROWCNT1_EP,TB_OEXTM_EP);
						intROWCNT1_EP++;
					}
					
					//tblEPSDT.setValueAt(new Boolean(true),i,TB_CHKFL_EP);	

					//tblEPSDT.setValueAt(L_tmsINCST==null ? "" : M_fmtLCDTM.format(L_tmsINCST),intROWCNT,TB_INCST_EP);
					//tblEPSDT.setValueAt(L_tmsOUTST==null ? "" : M_fmtLCDTM.format(L_tmsOUTST),intROWCNT,TB_OUTST_EP);
				}
			}
			M_rstRSSET.close();

			for(int i=0;i<tblEPSDT.getRowCount();i++)
			{
				if(tblEPSDT.getValueAt(i,TB_CHKFL_EP).toString().equals("true"))
					setEXTIME(i);
			}
			//exeEXTPS();
			
		}
		catch(Exception L_EX)
		{
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getDATA_EP()"); 
		}
		setMSG("data fetching completed ...",'N');
       this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	/**Method to Calculate Exit Period**/
	void setEXTIME(int LP_ROWID)
	{
		try
	    {
			String L_strPEDTM="00:00";
			String L_strPEATM="00:00";
			String L_strOEXTM="00:00";
			String L_strOFPFL=tblEPSDT.getValueAt(LP_ROWID,TB_OFPFL_EP).toString();
			String L_strINCTM=tblEPSDT.getValueAt(LP_ROWID,TB_INCTM_EP).toString();
			String L_strOUTTM=tblEPSDT.getValueAt(LP_ROWID,TB_OUTTM_EP).toString();
			String L_strINCST=tblEPSDT.getValueAt(LP_ROWID,TB_INCST_EP).toString();
			String L_strOUTST=tblEPSDT.getValueAt(LP_ROWID,TB_OUTST_EP).toString();
			
			if(L_strINCTM.equals(""))
			{
				// Exit (Non-returnable) within duty hours
				if(M_fmtLCDTM.parse(L_strOUTTM).compareTo(M_fmtLCDTM.parse(L_strOUTST))<=0 && M_fmtLCDTM.parse(L_strOUTTM).compareTo(M_fmtLCDTM.parse(L_strINCST))>=0)
				{
					L_strINCTM = L_strOUTST;
				}
				else
				{
					// Unaccoutable Exit (to be ignored)
					tblEPSDT.setValueAt(L_strPEDTM,LP_ROWID,TB_PEDTM_EP);
					tblEPSDT.setValueAt(L_strPEATM,LP_ROWID,TB_PEATM_EP);
					tblEPSDT.setValueAt(L_strOEXTM,LP_ROWID,TB_OEXTM_EP);
					return;
				}
			}

			// Official Exit
			if(L_strOFPFL.equals("O"))
			{
				L_strOEXTM = calTIME(L_strINCTM,L_strOUTTM);
				tblEPSDT.setValueAt(L_strPEDTM,LP_ROWID,TB_PEDTM_EP);
				tblEPSDT.setValueAt(L_strPEATM,LP_ROWID,TB_PEATM_EP);
				tblEPSDT.setValueAt(L_strOEXTM,LP_ROWID,TB_OEXTM_EP);
				return;
			}
				
			Date L_datINCTM=M_fmtLCDTM.parse(L_strINCTM);
			Date L_datOUTTM=M_fmtLCDTM.parse(L_strOUTTM);
			Date L_datINCST=M_fmtLCDTM.parse(L_strINCST);
			Date L_datOUTST=M_fmtLCDTM.parse(L_strOUTST);
			
			//Exit pass (returnable) within duty hours
			if((L_datOUTTM.compareTo(L_datOUTST)<=0 && L_datOUTTM.compareTo(L_datINCST)>=0) && (L_datINCTM.compareTo(L_datOUTST)<=0 && L_datINCTM.compareTo(L_datINCST)>=0))
					L_strPEDTM = calTIME(L_strINCTM,L_strOUTTM);
			// Exit pass out of duty hours
			//         OUT before Shift Start                 IN before Shift Start                        OUT after Shift end              IN after Shift End
			else if((L_datOUTTM.compareTo(L_datINCST)<0 && L_datINCTM.compareTo(L_datINCST)<0)  || (L_datOUTTM.compareTo(L_datOUTST)>0 && L_datINCTM.compareTo(L_datOUTST)>0))
					L_strPEATM = calTIME(L_strINCTM,L_strOUTTM);
			// Exit pass OUT before duty hours and IN within duty hours
			//        Out before Shift Starting                    IN after Shift Start               IN before Shift End
			else if(L_datOUTTM.compareTo(L_datINCST)<0 && (L_datINCTM.compareTo(L_datINCST)>0 && L_datINCTM.compareTo(L_datOUTST)<0 ))
			{
					L_strPEATM = calTIME(L_strINCST,L_strOUTTM);
					L_strPEDTM = calTIME(L_strINCTM,L_strINCST);
			}
			// Exit pass OUT within duty hours and IN after duty hours
			//        Out after Shift Start      Out before Shift end                                IN after Shift End
			else if((L_datOUTTM.compareTo(L_datINCST)>0 && L_datOUTTM.compareTo(L_datOUTST)<0 ) && L_datINCTM.compareTo(L_datOUTST)>0)
			{
					L_strPEDTM = calTIME(L_strOUTST,L_strOUTTM);
					L_strPEATM = calTIME(L_strINCTM,L_strOUTST);
			}
			
			
			tblEPSDT.setValueAt(L_strPEDTM,LP_ROWID,TB_PEDTM_EP);
			tblEPSDT.setValueAt(L_strPEATM,LP_ROWID,TB_PEATM_EP);
			tblEPSDT.setValueAt(L_strOEXTM,LP_ROWID,TB_OEXTM_EP);


		}
	    catch(Exception L_EX)
	    {
		    setMSG(L_EX, "setEXTIME");
		}            
	}
	
	/**Method to auto generation of Exit Pass**/
	private void exeEXTPS()
	{
		try
		{
			for(int i=0;i<tblEPSDT.getRowCount();i++)
			{
				if(tblEPSDT.getValueAt(i,TB_WRKDT_EP).toString().length()>0 && tblEPSDT.getValueAt(i,TB_EMPNO_EP).toString().length()>0)
				{
					for(int j=0;j<vtrSWTOT.size();j++)
					{
						String strVTRVL = vtrSWTOT.get(j).substring(0,15);
						String strTBLVL = tblEPSDT.getValueAt(i,TB_WRKDT_EP).toString()+"|"+tblEPSDT.getValueAt(i,TB_EMPNO_EP).toString();
						String L_strSUBTM = "";
						String L_strSUBTM1 = "";
						if(strVTRVL.equals(strTBLVL))	
						{
							String strVTRVL_TM = vtrSWTOT.get(j).substring(16,32);
							String strTBLVL_TM = tblEPSDT.getValueAt(i,TB_SECTM_EP).toString();
							String strTBLVL_ETM = tblEPSDT.getValueAt(i,TB_EOTTM_EP).toString();
							
							//checks for the security time and expected out time, and accordingly sets in-out times
							if(M_fmtLCDTM.parse(strVTRVL_TM).compareTo(M_fmtLCDTM.parse(strTBLVL_TM))>=0)
								L_strSUBTM=calTIME(strVTRVL_TM,strTBLVL_TM);
							else if(M_fmtLCDTM.parse(strVTRVL_TM).compareTo(M_fmtLCDTM.parse(strTBLVL_TM))<=0)
								L_strSUBTM=calTIME(strTBLVL_TM,strVTRVL_TM);
							
							if(M_fmtLCDTM.parse(strVTRVL_TM).compareTo(M_fmtLCDTM.parse(strTBLVL_ETM))>=0)
								L_strSUBTM1=calTIME(strVTRVL_TM,strTBLVL_ETM);
							else if(M_fmtLCDTM.parse(strVTRVL_TM).compareTo(M_fmtLCDTM.parse(strTBLVL_ETM))<=0)
								L_strSUBTM1=calTIME(strTBLVL_ETM,strVTRVL_TM);
							
							//if security time or expected out time - out time <=10 mins then sets default out time in table
							if((fmtHHMM.parse(L_strSUBTM).compareTo(fmtHHMM.parse("00:30"))<=0) || fmtHHMM.parse(L_strSUBTM1).compareTo(fmtHHMM.parse("00:30"))<=0)
							{
								tblEPSDT.setValueAt(strVTRVL_TM,i,TB_OUTTM_EP);
								tblEPSDT.setValueAt(strVTRVL_TM.substring(11,16),i,TB_AOTTM_EP);
								//checks for next in time for corresponding out time
								for(int l=0;l<vtrSWTIN.size();l++)
								{
									String strVTRVL1 = vtrSWTIN.get(l).substring(0,15);
									String strTBLVL1 = tblEPSDT.getValueAt(i,TB_WRKDT_EP).toString()+"|"+tblEPSDT.getValueAt(i,TB_EMPNO_EP).toString();
									//System.out.println(strVTRVL+"<<>>"+strTBLVL);
									if(strVTRVL1.equals(strTBLVL1))	
									{
										//System.out.println(strVTRVL1+"<<>>"+strTBLVL1);
										String strVTRVL_TM1 = vtrSWTIN.get(l).substring(16,32);
										String strTBLVL_TM1 = strVTRVL_TM;
										String strTBLVL_STD = tblEPSDT.getValueAt(i,TB_OUTST_EP).toString();
										//if(M_fmtLCDTM.parse(strVTRVL_TM1).compareTo(M_fmtLCDTM.parse(strTBLVL_TM1))>=0
										//   && M_fmtLCDTM.parse(strVTRVL_TM1).compareTo(M_fmtLCDTM.parse(strTBLVL_STD))<=0)
										if(M_fmtLCDTM.parse(strVTRVL_TM1).compareTo(M_fmtLCDTM.parse(strTBLVL_TM1))>=0)
										{
											tblEPSDT.setValueAt(strVTRVL_TM1,i,TB_INCTM_EP);
											tblEPSDT.setValueAt(strVTRVL_TM1.substring(11,16),i,TB_AINTM_EP);
											break;
										}
									}
								}
								setEXTIME(i);
								tblEPSDT.setValueAt(new Boolean(true),i,TB_CHKFL_EP);	
								tblEPSDT.setValueAt(new Boolean(true),i,TB_AUTHC_EP);
							}
						}
					}
				}
			}
		}
		catch (Exception L_EX)
		{
			setMSG("Error in exeEXTPS : "+L_EX,'E');
		}
	}

	
	/**
	 * Method to get Data from HR_SWTRN table in tblEPSDT
	 * In time -without initial In time
	 * Out time - without final Out time
	 * */
	void getEXPDL(ResultSet LP_RSSET,cl_JTable LP_TBL,int LP_ROWNO)
	{
		try
		{
			LP_TBL.setValueAt(M_fmtLCDAT.format(LP_RSSET.getDate("ex_docdt")),LP_ROWNO,TB_WRKDT_EP);
			LP_TBL.setValueAt(LP_RSSET.getString("ex_empno"),LP_ROWNO,TB_EMPNO_EP);
			LP_TBL.setValueAt(LP_RSSET.getString("ep_empnm"),LP_ROWNO,TB_EMPNM_EP);
			LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("ex_eottm")),LP_ROWNO,TB_EOTTM_EP);
			if(LP_RSSET.getString("ex_eintm")!= null)
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("ex_eintm")),LP_ROWNO,TB_EINTM_EP);
			if(LP_RSSET.getString("ex_sectm")!= null)
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("ex_sectm")),LP_ROWNO,TB_SECTM_EP);
			LP_TBL.setValueAt(LP_RSSET.getString("ex_docno"),LP_ROWNO,TB_DOCNO_EP);
			LP_TBL.setValueAt(LP_RSSET.getString("ex_OFPFL"),LP_ROWNO,TB_OFPFL_EP);
			LP_TBL.setValueAt(LP_RSSET.getString("ex_autby"),LP_ROWNO,TB_AUTBY_EP);
			LP_TBL.setValueAt(LP_RSSET.getString("ex_shfcd"),LP_ROWNO,TB_WRKSH_EP);
			setWRKDTX(M_fmtLCDAT.format(LP_RSSET.getDate("ex_docdt")));
			LP_TBL.setValueAt(M_fmtLCDTM.format(fmtDBDATTM.parse(getSHFTM_ST(LP_RSSET.getString("ex_shfcd"),"IN"))),LP_ROWNO,TB_INCST_EP);
			LP_TBL.setValueAt(M_fmtLCDTM.format(fmtDBDATTM.parse(getSHFTM_ST(LP_RSSET.getString("ex_shfcd"),"OUT"))),LP_ROWNO,TB_OUTST_EP);
			if(LP_RSSET.getString("ex_outtm")!= null)
			{
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("ex_outtm")),LP_ROWNO,TB_OUTTM_EP);
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("ex_outtm")).substring(11,16),LP_ROWNO,TB_AOTTM_EP);
				LP_TBL.setValueAt(new Boolean(true),LP_ROWNO,TB_CHKFL_EP);	
				LP_TBL.setValueAt(new Boolean(true),LP_ROWNO,TB_AUTHC_EP);
			}
			if(LP_RSSET.getString("ex_inctm")!= null)
			{
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("ex_inctm")),LP_ROWNO,TB_INCTM_EP);
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("ex_inctm")).substring(11,16),LP_ROWNO,TB_AINTM_EP);
			}
		}
		catch(Exception EX)
		{
			setMSG(EX,"getEXPDL()"); 
		}
	}
	
	
	/**Validation for enter only selected row data**/
	boolean vldDATA_EP()
	{
		try
		{
			if(txtSTRDT.getText().trim().length() ==0)
	    	{
				txtSTRDT.requestFocus();
	    		setMSG("Enter the From Date",'E');
	    		return false;
	    	}
			else if(txtENDDT.getText().trim().length() ==0)
	    	{
				txtENDDT.requestFocus();
	    		setMSG("Enter the To Date..",'E');
	    		return false;
	    	}
			
			for(int P_intROWNO=0;P_intROWNO<tblEPSDT.getRowCount();P_intROWNO++)
			{
				if(tblEPSDT.getValueAt(P_intROWNO,TB_CHKFL_EP).toString().equals("true"))
				{
					if(!tblEPSDT.getValueAt(P_intROWNO,TB_AUTHC_EP).toString().equals("true"))
					{
						setMSG("Authorization Not Selected for "+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+ " Emp No at Date "+tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()+" in the table..",'E');
						return false;
					}
					if(tblEPSDT.getValueAt(P_intROWNO,TB_AOTTM_EP).toString().length() == 0)
					{
						setMSG("Actual Out Time not entered for "+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+ " Emp No at Date "+tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()+" in the table..",'E');
						return false;
					}
					if(tblEPSDT.getValueAt(P_intROWNO,TB_AOTTM_EP).toString().length()>0 && tblEPSDT.getValueAt(P_intROWNO,TB_AINTM_EP).toString().length()>0)
					if(M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString().trim()+" "+tblEPSDT.getValueAt(P_intROWNO,TB_AOTTM_EP).toString())).compareTo(M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString().trim()+" "+tblEPSDT.getValueAt(P_intROWNO,TB_AINTM_EP).toString())))>0)
					{
						setMSG("In Time should be greater than Out Time for "+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+ " Emp No at Date "+tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()+" in the table..",'E');
						return false;
					}
				}
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATA_EP()");		
		}
		return true;
	}

	/**Validation for Authorize whether all exit passes of employee are selected or not**/
	private boolean vldEXTPS()
	{
		String L_strEMPNO;
		int L_intOPTN;
		for(int i=0;i<tblEPSDT.getRowCount();i++)
		{
			if(tblEPSDT.getValueAt(i,TB_CHKFL_EP).toString().equals("true"))
			{
				L_strEMPNO=tblEPSDT.getValueAt(i,TB_EMPNO_EP).toString();
				for(int j=0;j<tblEPSDT.getRowCount();j++)
				{
					if(tblEPSDT.getValueAt(j,TB_CHKFL_EP).toString().equals("false")
					&& tblEPSDT.getValueAt(j,TB_EMPNO_EP).toString().equals(L_strEMPNO))
					{
						L_intOPTN=JOptionPane.showConfirmDialog( this," Exit Pass Entry for "+L_strEMPNO+" Is Not Entered. \n Do You Want To Authorize Anyway ?","Exit Pass Validation",JOptionPane.OK_CANCEL_OPTION);
						if(L_intOPTN==2 || L_intOPTN==-1)
							return false;
						return true;
					}
				}	
				L_intOPTN=JOptionPane.showConfirmDialog( this,"Do You Want To Authorize Selected Exit Pass ?","Exit Pass Validation",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN==2 || L_intOPTN==-1)
					return false;
				return true;
			}
		}	
		return true;
	}
	

	/**
	 * Re-fetching data from HR_SLTRN1 to HR_SWTRN for specified date
	 * 
	 */
	private void getATTNDT()
	{
		try
		{
			String strWHRSTR_ATNDT = " SLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			strWHRSTR_ATNDT+= " and SLT_STSFL='2' and CONVERT(varchar,SLT_PNCTM,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			String strSQLQRY1 = " update HR_SLTRN1 set SLT_STSFL = '1'"; 
			strSQLQRY1+= " where "+strWHRSTR_ATNDT;
			System.out.println(strSQLQRY1);	
			cl_dat.exeSQLUPD(strSQLQRY1,"getATTNDT");
			
   			//String L_strTMP = "Select distinct slt_cmpcd,  slt_empcd, slt_pnctm, slt_inocd, slt_trmid, slt_stsfl from hr_sltrn1 where slt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and slt_stsfl='1'  order by slt_pnctm desc";
   			String L_strTMP = "Select distinct slt_cmpcd, ep_empno, ep_accrf, slt_empcd, slt_pnctm, slt_inocd, slt_trmid, slt_stsfl from hr_sltrn1,hr_epmst where slt_cmpcd  = '"+cl_dat.M_strCMPCD_pbst+"' and ep_cmpcd=slt_cmpcd and ep_sapno=slt_empcd and slt_stsfl='1'  order by slt_pnctm desc";
            System.out.println(L_strTMP);
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strTMP);
            System.out.println("rs not null");
                
            if(L_rstRSSET == null)
                return;
            System.out.println("rs not null");
           	String L_strCMPCD = "";
           	String L_strEMPNO = "";
           	String L_strACCRF = "";
           	String L_strEMPCD = "";
            String L_strTRMID = "";
			String L_strPNCDTM = "";
            String L_strINOCD = "";
          	String L_STRQRY1 = "";
          	String L_STRSQL = "";
          	ResultSet L_rstRSSET2;
            while(L_rstRSSET.next())
            {
            	//L_strEMPNO = L_rstRSSET.getString("ep_empno");
            	L_strCMPCD = L_rstRSSET.getString("slt_cmpcd");
            	L_strACCRF = L_rstRSSET.getString("ep_accrf");
            	L_strEMPNO = L_rstRSSET.getString("ep_empno");
            	L_strEMPCD = L_rstRSSET.getString("slt_empcd");
                L_strTRMID = L_rstRSSET.getString("slt_trmid");
				L_strPNCDTM = L_rstRSSET.getString("slt_pnctm");
                L_strINOCD = L_rstRSSET.getString("slt_inocd");
            	L_STRQRY1 = "Select count(*) slt_count from hr_slhst where slt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and slt_empcd = '"+L_strEMPCD+"' and slt_pnctm='"+L_strPNCDTM+"' and slt_inocd='"+L_strINOCD+"'";
	            System.out.println(L_STRQRY1);
	            L_rstRSSET2 = cl_dat.exeSQLQRY1(L_STRQRY1);
	            if(L_rstRSSET2 !=null && L_rstRSSET2.next())
	            if(L_rstRSSET2.getInt("slt_count") == 0)
				{
		            L_rstRSSET2.close();
				    L_STRSQL = "insert into HR_SLHST(SLT_CMPCD,SLT_EMPNO,SLT_EMPCD,SLT_PNCTM,SLT_INOCD,SLT_TRMID,SLT_STSFL) values ("
							 + "'"+L_strCMPCD+"','XXXX','"+L_strEMPCD+"','"+L_strPNCDTM+"','"+L_strINOCD+"','"+L_strTRMID+"','1')";
				    System.out.println(L_STRSQL);
					cl_dat.exeSQLUPD(L_STRSQL,"getATTNDT");
				}


	   			L_STRQRY1 = "Select count(*) slt_count from ho_sltrn where slt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and slt_empno = '"+L_strEMPNO+"' and slt_pnctm='"+L_strPNCDTM+"' and slt_inocd='"+L_strINOCD+"'";
	            System.out.println(L_STRQRY1);
	            L_rstRSSET2 = cl_dat.exeSQLQRY1(L_STRQRY1);
	            if(L_rstRSSET2 !=null && L_rstRSSET2.next())
	            if(L_rstRSSET2.getInt("slt_count") == 0)
				{
		            L_rstRSSET2.close();
	
				    L_STRSQL = "insert into HO_SLTRN(SLT_CMPCD,SLT_EMPNO,SLT_ACCRF,SLT_PNCTM,SLT_INOCD,SLT_TRMID,SLT_STSFL) values ("
							 + "'"+L_strCMPCD+"','"+L_strEMPNO+"','"+L_strACCRF+"','"+L_strPNCDTM+"','"+L_strINOCD+"','"+L_strTRMID+"','1')";
				    System.out.println(L_STRSQL);
				    cl_dat.exeSQLUPD(L_STRSQL,"getATTNDT");
					cl_dat.exeDBCMT("HO_SLTRN");
				}
            }
			
            cstSWTRN = cl_dat.M_conSPDBA_pbst.prepareCall("{call updSWTRN_SLT_fn()}");
            cstSWTRN.executeUpdate();
			cl_dat.M_flgLCUPD_pbst = true;

			strWHRSTR_ATNDT = " SLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			strWHRSTR_ATNDT+= " and SLT_STSFL='1' and CONVERT(varchar,SLT_PNCTM,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			strSQLQRY1 = " update HR_SLTRN1 set SLT_STSFL = '2'"; 
			strSQLQRY1+= " where "+strWHRSTR_ATNDT;
			//System.out.println(strSQLQRY1);	
			cl_dat.exeSQLUPD(strSQLQRY1,"getATTNDT");
		}		
		catch(Exception E)
		{
			setMSG(E," : prcOVTDT1()");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);	
	}

	/**
	 * data from hr_swtrn , hr_swmst processed and data is stored in to hr_teota.
	 * 
	 */
	private void prcOVTDT1()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Please Wait......",'N');
			String L_strEMPNO="";
			String L_strWRKDT="";
			String L_strINCTM="";
			String L_strOUTTM="";
			
			/**Used for using common where string for deleteing and selecting records from hr_ottrn.
			 */
			
			strWHRSTR_SELOT = " SW_CMPCD=OT_CMPCD and SW_WRKDT=OT_WRKDT and SW_EMPNO=OT_EMPNO and EP_CMPCD=OT_CMPCD and EP_EMPNO=OT_EMPNO and OT_STSFL = '0' and OT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			strWHRSTR_SELOT+= " and SW_STSFL='9' and ot_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			if(txtEMPNO3.getText().length()>0)
				strWHRSTR_SELOT+= " and ep_empno='"+txtEMPNO3.getText().trim()+"'";
			if(txtDPTCD.getText().length()>0)
				strWHRSTR_SELOT+= " and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
			if(txtEMPCT.getText().length()>0)
				strWHRSTR_SELOT+= " and ep_empct='"+txtEMPCT.getText().trim()+"'";
			

			String L_strWHRSTR =" isnull(OT_STSFL,'') not in('1','X') and isnull(OT_MNLFL,'')<>'Y' and ";
			L_strWHRSTR+= " ot_cmpcd + ot_empno + char(ot_wrkdt) + char(ot_strtm) in (Select ot_cmpcd + ot_empno + char(ot_wrkdt) + char(ot_strtm) ";
			L_strWHRSTR+= " from HR_EPMST,HR_OTTRN,HR_SWMST";
			L_strWHRSTR+= " where "+strWHRSTR_SELOT;
			L_strWHRSTR+= " )"; 
			boolean flgCHK_EXIST =	chkEXIST("HR_OTTRN", L_strWHRSTR);
			if(flgCHK_EXIST)
			{
				/** records which are not authorized or rejected and not manually entered will be deleted.*/
				M_strSQLQRY = " delete from hr_ottrn where "+L_strWHRSTR;
				//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
			}
			/** Data from hr_swmst,hr_swtrn,hr_extrn is selected and inserted into hashtable and vectors for further processing.
			 */
			String L_strEPWHR = " and ep_empct='TM'";
			if(txtEMPNO3.getText().length()>0)
				L_strEPWHR+= " and ep_empno='"+txtEMPNO3.getText().trim()+"'";
			if(txtDPTCD.getText().length()>0)
				L_strEPWHR+= " and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
			if(txtEMPCT.getText().length()>0)
				L_strEPWHR+= " and ep_empct='"+txtEMPCT.getText().trim()+"'";
			
			String L_strOTWHR_SWT = " and swt_empno + char(swt_wrkdt) not in (select ot_empno + char(ot_wrkdt) from hr_ottrn where ot_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"' and OT_STSFL in ('1','X'))";
			String L_strOTWHR_SWM = " and sw_empno + char(sw_wrkdt) not in (select ot_empno + char(ot_wrkdt) from hr_ottrn where ot_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"' and OT_STSFL in ('1','X'))";
			String L_strOTWHR_EXT = " and ex_empno + char(ex_docdt) not in (select ot_empno + char(ot_wrkdt) from hr_ottrn where ot_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"' and OT_STSFL in ('1','X'))";
						
						
			/*M_strSQLQRY = " select swt_empno,swt_wrkdt,swt_inctm,swt_outtm,sw_inctm,sw_outtm,sw_pottm,sw_oottm from hr_swtrn,hr_swmst,hr_epmst";
			M_strSQLQRY+= " where sw_cmpcd=ep_cmpcd and sw_empno=ep_empno and sw_cmpcd=swt_cmpcd and sw_wrkdt=swt_wrkdt and sw_empno=swt_empno";
			M_strSQLQRY+= " and swt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			M_strSQLQRY+= " and ep_empct='TM'";
			if(txtDPTCD.getText().length()>0)
				M_strSQLQRY+= " and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
			if(txtEMPCT.getText().length()>0)
				M_strSQLQRY+= " and ep_empct='"+txtEMPCT.getText().trim()+"'";
			M_strSQLQRY+= " order by swt_wrkdt,swt_empno,swt_inctm";
			System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			*/
			hstEMPTM.clear();
			hstINCST.clear();
			hstOUTST.clear();
			hstWRKSH.clear();
			vtrEMPTM.clear();
			M_strSQLQRY = " select swt_empno,swt_wrkdt,swt_inctm,swt_outtm from hr_swtrn,hr_epmst";
			M_strSQLQRY+= " where swt_cmpcd=ep_cmpcd and swt_empno=ep_empno";
			M_strSQLQRY+= " and swt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			M_strSQLQRY+= L_strEPWHR + L_strOTWHR_SWT;
			M_strSQLQRY+= " order by swt_wrkdt,swt_empno,swt_inctm";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strEMPNO=M_rstRSSET.getString("SWT_EMPNO");
					L_strWRKDT=fmtYYYYMMDD.format(M_rstRSSET.getDate("SWT_WRKDT"));
					L_strINCTM = (M_rstRSSET.getString("SWT_INCTM") != null ? fmtYYYYMMDDHHMM.format(M_rstRSSET.getTimestamp("SWT_INCTM")):"");
					L_strOUTTM = (M_rstRSSET.getString("SWT_OUTTM") != null ? fmtYYYYMMDDHHMM.format(M_rstRSSET.getTimestamp("SWT_OUTTM")):"");
					
					if(!L_strINCTM.equals(""))
					{
						String L_key = L_strEMPNO+"|"+L_strWRKDT+"|"+L_strINCTM;
						putHSVTR(L_strEMPNO,L_strWRKDT,L_strINCTM,intINOFL,"I","I");
						if(!vtrEMPTM.contains(L_key))
							vtrEMPTM.add(L_key);						
					}
					if(!L_strOUTTM.equals(""))
					{
						String L_key = L_strEMPNO+"|"+L_strWRKDT+"|"+L_strOUTTM;
						putHSVTR(L_strEMPNO,L_strWRKDT,L_strOUTTM,intINOFL,"O","O");
						if(!vtrEMPTM.contains(L_key))
							vtrEMPTM.add(L_key);						
					}
				}
			}
			M_rstRSSET.close();
			
			M_strSQLQRY = " select sw_wrkdt,sw_empno,sw_inctm,sw_outtm,sw_incst,sw_outst,sw_wrksh from hr_swmst,hr_epmst";
			M_strSQLQRY+= " where sw_cmpcd=ep_cmpcd and sw_empno=ep_empno";
			M_strSQLQRY+= " and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			M_strSQLQRY+= L_strEPWHR + L_strOTWHR_SWM;
			M_strSQLQRY+= " order by sw_wrkdt,sw_empno,sw_inctm";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strEMPNO=M_rstRSSET.getString("SW_EMPNO");
					L_strWRKDT=fmtYYYYMMDD.format(M_rstRSSET.getDate("SW_WRKDT"));
					L_strINCTM = (M_rstRSSET.getString("SW_INCTM") != null ? fmtYYYYMMDDHHMM.format(M_rstRSSET.getTimestamp("SW_INCTM")):"");
					L_strOUTTM = (M_rstRSSET.getString("SW_OUTTM") != null ? fmtYYYYMMDDHHMM.format(M_rstRSSET.getTimestamp("SW_OUTTM")):"");
						
					if(!L_strINCTM.equals(""))
					{
						putHSVTR(L_strEMPNO,L_strWRKDT,L_strINCTM,intSWMFL,"I","Y");
						if(!vtrEMPTM.contains(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strINCTM))
							vtrEMPTM.add(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strINCTM);
					}	
					if(!L_strOUTTM.equals(""))
					{
						putHSVTR(L_strEMPNO,L_strWRKDT,L_strOUTTM,intSWMFL,"O","Y");
						if(!vtrEMPTM.contains(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strOUTTM))
							vtrEMPTM.add(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strOUTTM);
					}
					if(!hstINCST.containsKey(L_strEMPNO+"|"+L_strWRKDT))
						hstINCST.put(L_strEMPNO+"|"+L_strWRKDT,M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")));
					if(!hstOUTST.containsKey(L_strEMPNO+"|"+L_strWRKDT))
						hstOUTST.put(L_strEMPNO+"|"+L_strWRKDT,M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")));
					if(!hstWRKSH.containsKey(L_strEMPNO+"|"+L_strWRKDT))
						hstWRKSH.put(L_strEMPNO+"|"+L_strWRKDT,M_rstRSSET.getString("SW_WRKSH"));
				}
			}
			M_rstRSSET.close();
			
			M_strSQLQRY = " select ex_docdt,ex_empno,ex_inctm,ex_outtm,ex_ofpfl from hr_extrn,hr_epmst";
			M_strSQLQRY+= " where ex_cmpcd=ep_cmpcd and ex_empno=ep_empno";
			M_strSQLQRY+= " and ex_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and ex_docdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			M_strSQLQRY+= L_strEPWHR + L_strOTWHR_EXT;
			M_strSQLQRY+= " order by ex_docdt,ex_empno,ex_inctm";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strEMPNO=M_rstRSSET.getString("EX_EMPNO");
					L_strWRKDT=fmtYYYYMMDD.format(M_rstRSSET.getDate("EX_DOCDT"));
					L_strINCTM = (M_rstRSSET.getString("EX_INCTM") != null ? fmtYYYYMMDDHHMM.format(M_rstRSSET.getTimestamp("EX_INCTM")):"");
					L_strOUTTM = (M_rstRSSET.getString("EX_OUTTM") != null ? fmtYYYYMMDDHHMM.format(M_rstRSSET.getTimestamp("EX_OUTTM")):"");

					String L_strOFPFL = M_rstRSSET.getString("EX_OFPFL");
					
					if(!L_strINCTM.equals(""))
					{
						putHSVTR(L_strEMPNO,L_strWRKDT,L_strINCTM,intEXTFL,"I",L_strOFPFL);
						if(!vtrEMPTM.contains(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strINCTM))
							vtrEMPTM.add(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strINCTM);
						//putHSVTR(L_strEMPNO,L_strWRKDT,L_strINCTM,intRETTM,"I",L_strINCTM);
					}
							
					if(!L_strOUTTM.equals(""))
					{
						putHSVTR(L_strEMPNO,L_strWRKDT,L_strOUTTM,intEXTFL,"O",L_strOFPFL);
						if(!vtrEMPTM.contains(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strOUTTM))
							vtrEMPTM.add(L_strEMPNO+"|"+L_strWRKDT+"|"+L_strOUTTM);
						putHSVTR(L_strEMPNO,L_strWRKDT,L_strOUTTM,intRETTM,"O",L_strINCTM);
					}
				}
			}
			M_rstRSSET.close();
			
			/*for(int i=0;i<vtrEMPTM.size();i++)
			{
				if(vtrEMPTM.get(i).toString().substring(0,4).equals("5102"))
				//if(vtrEMPTM.get(i).toString().length()<26)
				{	
					String[] L_strTEMP = hstEMPTM.get(vtrEMPTM.get(i));
					System.out.println(vtrEMPTM.get(i).toString()+">>>>>intINOFL>>"+L_strTEMP[1]+"intSWMFL>>"+L_strTEMP[2]+"intEXTFL>>"+L_strTEMP[3]+"intRETTM>>"+L_strTEMP[4]);
					//System.out.println(vtrEMPTM.get(i).toString());
					//System.out.println(">>>>>intINOFL>>"+L_strTEMP[1]);
					//System.out.println("intSWMFL>>"+L_strTEMP[2]);
					//System.out.println("intEXTFL>>"+L_strTEMP[3]);
				}
			}*/
			
			// sorts the elements of vector accordingly
			exeSORT(vtrEMPTM);
			
			// records from hashtable are processed employee no wise and working date wise.
			L_strEMPNO = "";
			L_strWRKDT = ""; 
			String L_strEMPNO1 = "";
			String L_strWRKDT1 = "";
			for(int i=0;i<vtrEMPTM.size();i++)
			{
				L_strEMPNO = vtrEMPTM.get(i).substring(0,4);
				//if(!L_strEMPNO.equals("5102"))
				//	continue;
				L_strWRKDT = M_fmtLCDAT.format(fmtYYYYMMDD.parse(vtrEMPTM.get(i).substring(5,13))); 
				if(!L_strEMPNO.equals(L_strEMPNO1) || !L_strWRKDT.equals(L_strWRKDT1))
					getOTREC(L_strEMPNO,L_strWRKDT);
				
				L_strEMPNO1 = vtrEMPTM.get(i).substring(0,4);
				L_strWRKDT1 = M_fmtLCDAT.format(fmtYYYYMMDD.parse(vtrEMPTM.get(i).substring(5,13)));
				
			}
			chkFETCH_OT.setSelected(false);
		}		
		catch(Exception E)
		{
			setMSG(E," : prcOVTDT1()");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);	
	}

	/** all the times for the perticular employee are stored in hstEMPTM.
	 *  selected all the times for pericular employee for perticular working date 1-1.
	 *  for each incomming time searched for out time pair.
	*/ 
	private void getOTREC(String LP_EMPNO, String LP_WRKDT)
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);	
			String[] staVALUS0 = null;
			String[] staVALUS1 = null;
			String[] staVALUS2 = null;
			String strEMPNO = "";
			String strWRKDT = ""; 
			int i = 0;
			int i1 = 0;
			int j = 0;
			int j1 = 0;
			int k = 0;
			String strINOTM0 = "";
			String strINOFL0 = "";
			String strSWMFL0 = "";
			String strEXTFL0 = "";
			String strRETTM0 = "";

			String strINOTM1 = "";
			String strINOFL1 = "";
			String strSWMFL1 = "";
			String strEXTFL1 = "";
			String strRETTM1 = "";
			
			String strINOTM2 = "";
			String strINOFL2 = "";
			String strSWMFL2 = "";
			String strEXTFL2 = "";
			String strRETTM2 = "";

			String strSTRTM="";
			String strENDTM="";
			for(i=0;i<vtrEMPTM.size();i++)
			{
				strEMPNO = vtrEMPTM.get(i).substring(0,4);
				strWRKDT = M_fmtLCDAT.format(fmtYYYYMMDD.parse(vtrEMPTM.get(i).substring(5,13)));
				if(!vtrEMPTM.get(i).substring(0,4).equals(LP_EMPNO) || !M_fmtLCDAT.format(fmtYYYYMMDD.parse(vtrEMPTM.get(i).substring(5,13))).equals(LP_WRKDT))
					continue;
				//System.out.println(vtrEMPTM.get(i).substring(0,4));
				//System.out.println(vtrEMPTM.get(i).substring(5,13));
				//System.out.println("001");
				//System.out.println(">>"+LP_EMPNO+" - "+LP_WRKDT+"     :      "+LP_STRTM+" - "+LP_ENDTM+"     :     "+LP_INCST+" - "+LP_OUTST);
				String strINOTM = M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(vtrEMPTM.get(i).substring(14,26)));
				String strINCST = hstINCST.get(strEMPNO+"|"+fmtYYYYMMDD.format(M_fmtLCDAT.parse(strWRKDT)));
				String strOUTST = hstOUTST.get(strEMPNO+"|"+fmtYYYYMMDD.format(M_fmtLCDAT.parse(strWRKDT)));
				String strWRKSH = hstWRKSH.get(strEMPNO+"|"+fmtYYYYMMDD.format(M_fmtLCDAT.parse(strWRKDT)));
				
				staVALUS0 = hstEMPTM.get(vtrEMPTM.get(i));
				strINOTM0 = M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(vtrEMPTM.get(i).substring(14,26)));
				strINOFL0 = staVALUS0[intINOFL];
				strSWMFL0 = staVALUS0[intSWMFL];
				strEXTFL0 = staVALUS0[intEXTFL];
				strRETTM0 = staVALUS0[intRETTM].equals("") ? "" : M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(staVALUS0[intRETTM]));
				//System.out.println("[0] "+strINOTM0+" : "+strINOFL0+" : "+strSWMFL0+" : "+strEXTFL0+" : "+strRETTM0);
				//System.out.println(strEMPNO+" "+strWRKDT+" "+strINOTM0);
				if(!strINOFL0.equals("I"))
				{
					if(M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strOUTST))<0  || (M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strOUTST))>=0 && fmtHHMM.parse(calTIME(strINOTM0,strOUTST)).compareTo(fmtHHMM.parse(strMINST))<0))
						continue;
					
				}
				//System.out.println(" strINOTM0 : "+strINOTM0);
				i1 = chkSWMFL(LP_EMPNO,LP_WRKDT,i,strINOFL0);
				if(i != i1)
				{
					i = i1;
					staVALUS0 = hstEMPTM.get(vtrEMPTM.get(i));
					strINOTM0 = M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(vtrEMPTM.get(i).substring(14,26)));
					strINOFL0 = staVALUS0[intINOFL];
					strSWMFL0 = staVALUS0[intSWMFL];
					strEXTFL0 = staVALUS0[intEXTFL];
					strRETTM0 = staVALUS0[intRETTM].equals("") ? "" : M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(staVALUS0[intRETTM]));
				}
				//else if(strEXTFL0.equals("O"))
				//	continue;
					//System.out.println("004");
					//System.out.println(strINOTM0);
					//System.out.println(strINCST);
					//System.out.println(strOUTST);
				
				//Verification / Cotrolling conditions
				// (1) In Time & out time within standard duty hours	
				// (2) In time before duty hours with extra time less than 15 minutes
				// (3) Out time after duty hours with extra time less than 15 minutes
				
				//System.out.println(" strINOTM0 (1): "+strINOTM0);
				
				if(M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strINCST))<0 && fmtHHMM.parse(calTIME(strINCST,strINOTM0)).compareTo(fmtHHMM.parse(strMINST))<0)
					if(!strINOFL0.equals("I") && M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strOUTST))<0  || (M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strOUTST))>=0 && fmtHHMM.parse(calTIME(strINOTM0,strOUTST)).compareTo(fmtHHMM.parse(strMINST))<0))
						continue; // (2)
						
				// Overtime Categories
				// [1] Out of standard working time (Before (a) / After (b) duty hours) with OT hours in continuation 
				// [2] Starting before duty hours & continuing in duty hours
				// [3] Starting with standard out time & continuing after duty hours
				// [4] Over time hours with Official Exit Pass - Returning
				// [5] Overtime hours with official exit pass - Non returning
				// [6] Overtime hours with personal exit pass - Returning
				// [7] Overtime hours with personal exit pass - Non returning
				// [8] Overtime for working started before duty hours and ending after duty hours
				
					
				
				for(j = i+1;j<vtrEMPTM.size();j++)
				{
					if(!vtrEMPTM.get(j).substring(0,4).equals(LP_EMPNO) || !M_fmtLCDAT.format(fmtYYYYMMDD.parse(vtrEMPTM.get(j).substring(5,13))).equals(LP_WRKDT))
						break;
					staVALUS1 = hstEMPTM.get(vtrEMPTM.get(j));
					strINOTM1 = M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(vtrEMPTM.get(j).substring(14,26)));
					strINOFL1 = staVALUS1[intINOFL];
					strSWMFL1 = staVALUS1[intSWMFL];
					strEXTFL1 = staVALUS1[intEXTFL];
					strRETTM1 = staVALUS1[intRETTM].equals("") ? "" : M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(staVALUS1[intRETTM]));
					j1 = chkSWMFL(LP_EMPNO,LP_WRKDT,j,strINOFL1);
					if(j != j1)
					{
						j = j1;
						staVALUS1 = hstEMPTM.get(vtrEMPTM.get(j));
						strINOTM1 = M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(vtrEMPTM.get(j).substring(14,26)));
						strINOFL1 = staVALUS1[intINOFL];
						strSWMFL1 = staVALUS1[intSWMFL];
						strEXTFL1 = staVALUS1[intEXTFL];
						strRETTM1 = staVALUS1[intRETTM].equals("") ? "" : M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(staVALUS1[intRETTM]));
					}
					//System.out.println("[1] "+strINOTM1+" : "+strINOFL1+" : "+strSWMFL1+" : "+strEXTFL1+" : "+strRETTM1);
					if (M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strINCST))>=0 && M_fmtLCDTM.parse(strINOTM1).compareTo(M_fmtLCDTM.parse(strOUTST))<=0)
						break; // (1)
					if (strINOFL1.equals("O") && strEXTFL1.equals("O"))
					{
						if(strRETTM1.equals(""))
						{
							// Emploee has continued to work after duty hours and gone out with Non-returning Exit Pass
							if(M_fmtLCDTM.parse(strINOTM1).compareTo(M_fmtLCDTM.parse(strOUTST))>0)
								strINOTM0 = strOUTST;
							else
							{
								j=vtrEMPTM.size();
								i=vtrEMPTM.size();
								break;
							}
						}
						else
						{
							for(k = j+1;k<vtrEMPTM.size();k++)
							{
								if(!vtrEMPTM.get(k).substring(0,4).equals(LP_EMPNO) || !M_fmtLCDAT.format(fmtYYYYMMDD.parse(vtrEMPTM.get(k).substring(5,13))).equals(LP_WRKDT))
									break;
								staVALUS2 = hstEMPTM.get(vtrEMPTM.get(k));
								strINOTM2 = M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(vtrEMPTM.get(k).substring(14,26)));
								strINOFL2 = staVALUS2[intINOFL];
								strSWMFL2 = staVALUS2[intSWMFL];
								strEXTFL2 = staVALUS2[intEXTFL];
								strRETTM2 = staVALUS2[intRETTM].equals("") ? "" : M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(staVALUS2[intRETTM]));
								//System.out.println("[2] "+strINOTM2+" : "+strINOFL2+" : "+strSWMFL2+" : "+strEXTFL2+" : "+strRETTM2);
									
								if(strINOFL2.equals("I") && strINOTM2.equals(strRETTM1))
								{
									break;
								}
							}
							j=k;
							continue;
							/*strINOTM1 = strINOTM2;
							strINOFL1 = strINOFL2;
							strSWMFL1 = strSWMFL2;
							strEXTFL1 = strEXTFL2;
							strRETTM1 = strRETTM2;*/
						}
					}
					
					//System.out.println("[3] "+strINOTM1+" : "+strINOFL1+" : "+strSWMFL1+" : "+strEXTFL1+" : "+strRETTM1);
						
					if(strINOFL1.equals("O") && M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strINCST))<=0)  
					{
						if(M_fmtLCDTM.parse(strINOTM1).compareTo(M_fmtLCDTM.parse(strINCST))>=0 && fmtHHMM.parse(calTIME(strINCST,strINOTM0)).compareTo(fmtHHMM.parse(strMINOT))>=0) // [2]
							{strSTRTM = strINOTM0; strENDTM = strINCST; setOTTRN(strEMPNO,strWRKDT,strSTRTM,strENDTM,"0","");}
						else  if(M_fmtLCDTM.parse(strINOTM1).compareTo(M_fmtLCDTM.parse(strINCST))<= 0 && fmtHHMM.parse(calTIME(strINOTM1,strINOTM0)).compareTo(fmtHHMM.parse(strMINOT))>=0)
							{strSTRTM = strINOTM0; strENDTM = strINOTM1; setOTTRN(strEMPNO,strWRKDT,strSTRTM,strENDTM,"0","");} // [1a]
					}
					//if (staVALUS1[intINOFL].equals("O") && fmtHHMM.parse(calTIME(strINOTM1,strOUTTM)).compareTo(fmtHHMM.parse(strMINOT))<0)
					//	continue;
						
					//System.out.println("009");
					if(staVALUS1[intINOFL].equals("O") && M_fmtLCDTM.parse(strINOTM1).compareTo(M_fmtLCDTM.parse(strOUTST))>= 0 )
					{
						if(M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strOUTST)) <= 0 && fmtHHMM.parse(calTIME(strINOTM1,strOUTST)).compareTo(fmtHHMM.parse(strMINOT))>=0)
							{strSTRTM = strOUTST; strENDTM = strINOTM1; setOTTRN(strEMPNO,strWRKDT,strSTRTM,strENDTM,"0","");} // [1b]
						else if(M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strOUTST)) >= 0 && fmtHHMM.parse(calTIME(strINOTM1,strINOTM0)).compareTo(fmtHHMM.parse(strMINOT))>=0)
							{strSTRTM = strINOTM0; strENDTM = strINOTM1; setOTTRN(strEMPNO,strWRKDT,strSTRTM,strENDTM,"0","");} // [1b]
					}
					break;
				}
				i=j;
			}
			setMSG("Processing Is Over ......",'N');
		}
		catch(Exception E)
		{
			setMSG(E," : getOTREC()");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	
	private int chkSWMFL(String LP_EMPNO, String LP_WRKDT,int LP_RECCT, String LP_INOFL)
	{
		try
		{
			String [] staVALUSX = null;
			String strINOTMX = "";
			String strINOFLX = "";
			String strSWMFLX = "";
			for(int x = LP_RECCT+1;x<vtrEMPTM.size();x++)
			{
				if(!vtrEMPTM.get(x).substring(0,4).equals(LP_EMPNO) || !M_fmtLCDAT.format(fmtYYYYMMDD.parse(vtrEMPTM.get(x).substring(5,13))).equals(LP_WRKDT))
					return LP_RECCT;
				staVALUSX = hstEMPTM.get(vtrEMPTM.get(x));
				strINOTMX = M_fmtLCDTM.format(fmtYYYYMMDDHHMM.parse(vtrEMPTM.get(x).substring(14,26)));
				strINOFLX = staVALUSX[intINOFL];
				strSWMFLX = staVALUSX[intSWMFL];
				if(!strINOFLX.equals(LP_INOFL))
					return LP_RECCT;
				if(strSWMFLX.equals("Y"))
					return x;
			}
		}
		catch(Exception E)
		{
			setMSG(E," : chkSWMFL()");
		}
		return LP_RECCT;
	}
	
	
	private void setOTTRN(String LP_EMPNO, String LP_WRKDT, String LP_STRTM, String LP_ENDTM,String LP_STSFL,String LP_MNLFL)
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);	
			setMSG("Please Wait ......",'N');
			//System.out.println("xxx>>"+LP_EMPNO+" - "+LP_WRKDT+"     :      "+LP_STRTM+" - "+LP_ENDTM+"     :     ");
		
		
			String L_strEFFTM;
			String strSQLQRY="";
		
			String L_RNDIN = exeRNDOF(LP_STRTM.substring(11,16),"IN");
			String L_RNDOT = exeRNDOF(LP_ENDTM.substring(11,16),"OUT");
			//String L_RNDIN = LP_STRTM.substring(11,16);
			//String L_RNDOT = LP_ENDTM.substring(11,16);

			String L_TMDIF = calTIME(LP_ENDTM.substring(0,10)+" "+L_RNDOT,LP_STRTM.substring(0,10)+" "+L_RNDIN);
			//System.out.println("L_TMDIF>>"+L_TMDIF);
			if(fmtHHMM.parse(L_TMDIF).compareTo(fmtHHMM.parse(strMINOT))<=0)
			{
				return;
			}
					
			String L_strWHRSTR =" OT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and OT_EMPNO = '"+LP_EMPNO+"'";
				   L_strWHRSTR+=" and OT_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_WRKDT))+"' and OT_STRTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_STRTM))+"'"; 
			boolean flgCHK_EXIST =	chkEXIST("HR_OTTRN", L_strWHRSTR);
			if(!flgCHK_EXIST)
			{
				strSQLQRY = " Insert into HR_OTTRN(OT_CMPCD,OT_EMPNO,OT_WRKDT,OT_STRTM,OT_ENDTM,OT_ESTTM,OT_EENTM,OT_OVTWK,OT_MNLFL,OT_STSFL,OT_TRNFL,OT_LUSBY,OT_LUPDT) values(";
				strSQLQRY +=" '"+cl_dat.M_strCMPCD_pbst+"',";
				strSQLQRY +=" '"+LP_EMPNO+"',";
				strSQLQRY +=" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_WRKDT))+"',";
				strSQLQRY +=" '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_STRTM))+"',";
				if(LP_ENDTM == null)
					strSQLQRY +=" null,";
				else
					strSQLQRY +=" '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_ENDTM))+"',";
					
				if(L_RNDIN == null)
					strSQLQRY +=" null,";
				else
					strSQLQRY +=" '"+L_RNDIN+"',";
					
				if(L_RNDOT == null)
					strSQLQRY +=" null,";
				else
					strSQLQRY +=" '"+L_RNDOT+"',";
					
				if(L_TMDIF.equals(""))
					strSQLQRY +=" null,";
				else
					strSQLQRY +=" '"+L_TMDIF+"',";
				
				strSQLQRY+="'"+LP_MNLFL+"',";
				strSQLQRY+="'"+LP_STSFL+"',";
				strSQLQRY+="'0',";
				strSQLQRY+="'"+cl_dat.M_strUSRCD_pbst+"',";
				strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
			}
			else
			{
				if(LP_ENDTM != null)
				{
					strSQLQRY =" update HR_OTTRN set OT_ENDTM ='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_ENDTM))+"',OT_EENTM='"+L_RNDOT+"',OT_OVTWK='"+L_TMDIF+"',OT_STSFL='"+LP_STSFL+"'";
					strSQLQRY +=" where "+L_strWHRSTR;
				}
			}
			//System.out.println("strSQLQRY>>"+strSQLQRY);
			cl_dat.exeSQLUPD(strSQLQRY,"updSWMST_STS");
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeDBCMT("setOTTRN");
		}
		catch(Exception E)
		{
			setMSG(E," : setOTTRN()");	
		}
		setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	
	private void getOTTRN()
	{
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			tblOVTDT.clrTABLE();
			inlTBLEDIT(tblOVTDT);
			intROWCNT=0;
			intROWCNT1=0;
			M_strSQLQRY = " Select OT_WRKDT,EP_EMPCT,OT_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,SW_WRKSH,OT_STRTM,OT_ENDTM,OT_OVTWK,SW_POTTM,SW_SHRWK,OT_ESTTM,OT_EENTM,OT_STSFL,SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST";
			M_strSQLQRY+= " from HR_EPMST,HR_OTTRN,HR_SWMST";
			
			M_strSQLQRY+= " where SW_CMPCD=OT_CMPCD and SW_WRKDT=OT_WRKDT and SW_EMPNO=OT_EMPNO and EP_CMPCD=OT_CMPCD and EP_EMPNO=OT_EMPNO and OT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			//M_strSQLQRY+= " and OT_STSFL = '0' ";
			M_strSQLQRY+= " and SW_STSFL='9' and ot_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			if(txtEMPNO3.getText().length()>0)
				M_strSQLQRY+= " and ep_empno='"+txtEMPNO3.getText().trim()+"'";
			if(txtDPTCD.getText().length()>0)
				M_strSQLQRY+= " and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
			if(txtEMPCT.getText().length()>0)
				M_strSQLQRY+= " and ep_empct='"+txtEMPCT.getText().trim()+"'";
			M_strSQLQRY+= " order by ot_wrkdt,ot_empno,ot_strtm";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("OT_STSFL").equals("0"))
					{
						setOTTBL(tblOVTDT,M_rstRSSET,intROWCNT);
						intROWCNT++;
					}
					else if(M_rstRSSET.getString("OT_STSFL").equals("1") || M_rstRSSET.getString("OT_STSFL").equals("X"))
					{
						setOTTBL(tblAUOVT,M_rstRSSET,intROWCNT1);
						intROWCNT1++;
						
					}
					
					/*tblOVTDT.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("OT_WRKDT")),intROWCNT,TB_WRKDT_OT);
					tblOVTDT.setValueAt(M_rstRSSET.getString("EP_EMPCT"),intROWCNT,TB_EMPCT_OT);
					tblOVTDT.setValueAt(M_rstRSSET.getString("OT_EMPNO"),intROWCNT,TB_EMPNO_OT);
					tblOVTDT.setValueAt(M_rstRSSET.getString("EP_EMPNM"),intROWCNT,TB_EMPNM_OT);
					tblOVTDT.setValueAt(M_rstRSSET.getString("SW_WRKSH"),intROWCNT,TB_WRKSH_OT);
					tblOVTDT.setValueAt(M_rstRSSET.getString("OT_STRTM").substring(11,16),intROWCNT,TB_STRTM_OT);
					tblOVTDT.setValueAt((M_rstRSSET.getString("OT_ENDTM")!=null ? nvlSTRVL(M_rstRSSET.getString("OT_ENDTM").substring(11,16),"") : ""),intROWCNT,TB_ENDTM_OT);
					if(M_rstRSSET.getString("OT_OVTWK")!=null)
					{
						tblOVTDT.setValueAt(M_rstRSSET.getString("OT_OVTWK").substring(0,5),intROWCNT,TB_OVTWK_OT);
						tblOVTDT.setValueAt(M_rstRSSET.getString("OT_OVTWK").substring(0,5),intROWCNT,TB_OVTHR_OT);
					}
					if(M_rstRSSET.getString("SW_POTTM")!=null)
						tblOVTDT.setValueAt(M_rstRSSET.getString("SW_POTTM").substring(0,5),intROWCNT,TB_POTTM_OT);
					if(M_rstRSSET.getString("SW_SHRWK")!=null)
						tblOVTDT.setValueAt(M_rstRSSET.getString("SW_SHRWK").substring(0,5),intROWCNT,TB_SHRWK_OT);
					if(M_rstRSSET.getString("OT_ESTTM")!=null)
						tblOVTDT.setValueAt(M_rstRSSET.getString("OT_ESTTM").substring(0,5),intROWCNT,TB_ESTTM_OT);
					if(M_rstRSSET.getString("OT_EENTM")!=null)
						tblOVTDT.setValueAt(M_rstRSSET.getString("OT_EENTM").substring(0,5),intROWCNT,TB_EENTM_OT);
					tblOVTDT.setValueAt(fmtLCDTM_L.format(M_rstRSSET.getTimestamp("OT_STRTM")).substring(0,16),intROWCNT,TB_INCTM_OT);
 					tblOVTDT.setValueAt((M_rstRSSET.getString("OT_ENDTM")!=null ? fmtLCDTM_L.format(M_rstRSSET.getTimestamp("OT_ENDTM")).substring(0,16) : ""),intROWCNT,TB_OUTTM_OT);
					if(M_rstRSSET.getString("SW_INCST")!=null)
						tblOVTDT.setValueAt(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")),intROWCNT,TB_INCST_OT);
					if(M_rstRSSET.getString("SW_OUTST")!=null)
						tblOVTDT.setValueAt(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")),intROWCNT,TB_OUTST_OT);
					intROWCNT++;
					*/
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception E)
		{
			setMSG(E," : getOTTRN()");	
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
		
	private void setOTTBL(cl_JTable LP_TBL,ResultSet LP_RSSET,int LP_ROW)
	{
		try
		{	
			LP_TBL.setValueAt(M_fmtLCDAT.format(LP_RSSET.getDate("OT_WRKDT")),LP_ROW,TB_WRKDT_OT);
			LP_TBL.setValueAt(LP_RSSET.getString("EP_EMPCT"),LP_ROW,TB_EMPCT_OT);
			LP_TBL.setValueAt(LP_RSSET.getString("OT_EMPNO"),LP_ROW,TB_EMPNO_OT);
			LP_TBL.setValueAt(LP_RSSET.getString("EP_EMPNM"),LP_ROW,TB_EMPNM_OT);
			LP_TBL.setValueAt(LP_RSSET.getString("SW_WRKSH"),LP_ROW,TB_WRKSH_OT);
			LP_TBL.setValueAt(LP_RSSET.getString("OT_STRTM").substring(11,16),LP_ROW,TB_STRTM_OT);
			LP_TBL.setValueAt((LP_RSSET.getString("OT_ENDTM")!=null ? nvlSTRVL(LP_RSSET.getString("OT_ENDTM").substring(11,16),"") : ""),LP_ROW,TB_ENDTM_OT);
			if(LP_RSSET.getString("OT_OVTWK")!=null)
			{
				LP_TBL.setValueAt(LP_RSSET.getString("OT_OVTWK").substring(0,5),LP_ROW,TB_OVTWK_OT);
				LP_TBL.setValueAt(LP_RSSET.getString("OT_OVTWK").substring(0,5),LP_ROW,TB_OVTHR_OT);
			}
			if(LP_RSSET.getString("SW_POTTM")!=null)
				LP_TBL.setValueAt(LP_RSSET.getString("SW_POTTM").substring(0,5),LP_ROW,TB_POTTM_OT);
			if(LP_RSSET.getString("SW_SHRWK")!=null)
				LP_TBL.setValueAt(LP_RSSET.getString("SW_SHRWK").substring(0,5),LP_ROW,TB_SHRWK_OT);
			if(LP_RSSET.getString("OT_ESTTM")!=null)
				LP_TBL.setValueAt(LP_RSSET.getString("OT_ESTTM").substring(0,5),LP_ROW,TB_ESTTM_OT);
			if(LP_RSSET.getString("OT_EENTM")!=null)
				LP_TBL.setValueAt(LP_RSSET.getString("OT_EENTM").substring(0,5),LP_ROW,TB_EENTM_OT);
			LP_TBL.setValueAt(fmtLCDTM_L.format(LP_RSSET.getTimestamp("OT_STRTM")).substring(0,16),LP_ROW,TB_INCTM_OT);
 			LP_TBL.setValueAt((LP_RSSET.getString("OT_ENDTM")!=null ? fmtLCDTM_L.format(LP_RSSET.getTimestamp("OT_ENDTM")).substring(0,16) : ""),LP_ROW,TB_OUTTM_OT);
			if(LP_RSSET.getString("SW_INCST")!=null)
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_INCST")),LP_ROW,TB_INCST_OT);
			if(LP_RSSET.getString("SW_OUTST")!=null)
				LP_TBL.setValueAt(M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_OUTST")),LP_ROW,TB_OUTST_OT);
			if(LP_RSSET.getString("OT_STSFL").equals("1"))
				LP_TBL.setValueAt(new Boolean(true),LP_ROW,TB_AUTHC_OT);
			else if(LP_RSSET.getString("OT_STSFL").equals("X"))
				LP_TBL.setValueAt(new Boolean(true),LP_ROW,TB_REJCT_OT);
				
			//intROWCNT++;
		}
		catch(Exception E)
		{
			setMSG(E," : setOTTBL()");	
		}
	}
	
	private void putHSVTR(String LP_EMPNO,String LP_WRKDT,String LP_DATTM,int LP_INDEX,String LP_VALUE,String LP_INSFL)
	{
		try
		{
			String L_key = LP_EMPNO+"|"+LP_WRKDT+"|"+LP_DATTM;
			
			if(hstEMPTM.containsKey(L_key))
			{
				String[] L_strTEMP = hstEMPTM.get(L_key);
				if(L_strTEMP[intINOFL].equals(LP_VALUE))
				{
					L_strTEMP[LP_INDEX]=LP_INSFL;
					hstEMPTM.put(L_key,L_strTEMP);
				}
				else
				{
					//System.out.println("Mismatch Found at "+L_key);
					L_strTEMP[LP_INDEX]=LP_INSFL;
					//L_strTEMP[intINOFL]=LP_VALUE;
					hstEMPTM.put(L_key,L_strTEMP);
				}
			}
			else
			{
				String[] L_strTEMP={"","","","",""};
				L_strTEMP[intINOFL]=LP_VALUE;
				L_strTEMP[LP_INDEX]=LP_INSFL;
				hstEMPTM.put(L_key,L_strTEMP);
			}
		}
		catch(Exception E)
		{
			setMSG(E," : putHSTBL()");
		}
	}		  
		

	private  void  exeSORT(Vector<String> LP_VTR)
	{
	    try
	    {
	 	   for(int i=0; i<LP_VTR.size();i++)
	 	   {
	 		  for(int j=i+1;j<LP_VTR.size();j++)
	 		  {
	 			 if(LP_VTR.get(j).toString().compareTo(LP_VTR.get(i).toString())<1)
	 			 {
	 				 String L_strTEMP = LP_VTR.get(j).toString();
	 				 LP_VTR.set(j,LP_VTR.get(i));
	 				 LP_VTR.set(i,L_strTEMP);
	 			 } 	   
	 		  }
	 	   }   
	    }
	    catch(Exception L_E)
	    {
			System.out.println("exeSORT : "+L_E);
	    }  
	}
	

	/** round off the time in multiples of 15 e.g 13:27 ==> 13:25
	 * if its IN time its rounded to upper level.
	 * if its OUT time its rounded to lower level
	 */
	private String exeRNDOF(String LP_STRTM,String LP_INOUT)
	{
		String strTIME="";
		try
		{
			String strHRS=LP_STRTM.substring(0,2);
			String strMIN=LP_STRTM.substring(3,5);
			
			int intMIN = Integer.parseInt(strMIN);
			int intHRS = Integer.parseInt(strHRS);
			int intREM = intMIN % 15;
			int intDIV = intMIN / 15;
			
			if(LP_INOUT.equals("OUT"))
			{
				intMIN = intMIN - intREM;
			}
			else if(LP_INOUT.equals("IN"))
			{
				if(intREM > 0)
				{
					intMIN = 15 * (intDIV+1);
				}
				if(intMIN == 60)
				{
					intMIN=0;
					intHRS = intHRS + 1; 
				}
			}
			if(String.valueOf(intHRS).length()==1)
				strHRS = "0"+String.valueOf(intHRS);
			else
				strHRS = String.valueOf(intHRS);
			
			if(String.valueOf(intMIN).length()==1)
				strMIN = "0"+String.valueOf(intMIN);
			else
				strMIN = String.valueOf(intMIN);
			
			strTIME = strHRS+":"+strMIN;
		}
		catch(Exception E)
		{	
			setMSG(E,"exeRNDOF");
		}
		return strTIME;
	}
	
/**
 */	
	public void keyPressed(KeyEvent L_KE)
	{
	    super.keyPressed( L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        	{
				if(M_objSOURC==txtDPTCD)
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
					//M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and cmt_codcd in ";
					//M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=US_EMPCD)";        			
        		    M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "+ (flgALLDPT ? "" : " and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"') and SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2)='"+M_strSBSCD.substring(0,2)+"')");					
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				if(M_objSOURC==txtEMPNO3)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO3";
        			String L_ARRHDR[] = {"Employee No","Name"};
        			//M_strSQLQRY = "select EP_EMPNO,EP_LSTNM||' '||SUBSTRING(EP_FSTNM,1,1)||'.'||SUBSTRING(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_DPTCD = '"+txtDPTCD.getText()+"' and EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"') and ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
					M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'" +(txtDPTCD.getText().length()>0 ? "AND EP_DPTCD = '"+txtDPTCD.getText()+"' ":"") +" and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
					//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
        		else if(M_objSOURC==txtEMPCT)
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPCT";
        			String L_ARRHDR[] = {"Code","Category"};
        			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXEPC' " +(flgALLDPT ? "": " and rtrim(ltrim(cmt_codcd)) in (Select distinct ep_empct from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"')")+" order by cmt_codcd";
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				else if(M_objSOURC==tblTEEXR.cmpEDITR[TB_LVECD])
				{
        				cl_dat.M_flgHELPFL_pbst = true;
        				M_strHLPFLD = "txtLVECD";
        				String L_ARRHDR[] = {"Code","Leave"};
        				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN  where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXLVE'  order by cmt_codcd";
						//System.out.println(M_strSQLQRY);
        				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
				}
				if(M_objSOURC==txtEMPNO_OT)
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
					if(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_WRKDT_OT).toString().length()==0)
					{
						setMSG("Please Enter Working Date First.",'E');
						return;
					}
        			M_strHLPFLD = "txtEMPNO_OT";
        			String L_ARRHDR[] = {"Employee No","Name","Cat","Shift","Personal Out","Short Work"};
					M_strSQLQRY = " select EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,EP_EMPCT,SW_WRKSH,isnull(SW_POTTM,'00:00') SW_POTTM,isnull(SW_SHRWK,'00:00') SW_SHRWK";
					M_strSQLQRY+= " from HR_EPMST,HR_SWMST";
					M_strSQLQRY+= " where EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO and SW_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblOVTDT.getValueAt(tblOVTDT.getSelectedRow(),TB_WRKDT_OT).toString()))+"' AND EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";       			
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,6,"CT");
        		}		

			}	
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
        	{
				if(M_objSOURC==txtDPTCD)
					txtEMPNO3.requestFocus();
				if(M_objSOURC==txtEMPNO3)
					txtEMPCT.requestFocus();
				else if(M_objSOURC==txtEMPCT)
					txtSTRDT.requestFocus();
				else if(M_objSOURC==txtSTRDT)
					txtENDDT.requestFocus();
				//else if(M_objSOURC==txtENDDT)
				//	btnRUN_EX.requestFocus();
			}
	    }catch(Exception L_EX)
		  {
				setMSG(L_EX,"This is KeyPressed");
		  }
	}
	

/**
 */	
	void exeHLPOK()
	{
	    super.exeHLPOK() ;
		try
		{
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
				txtDPTCD.setText(L_STRTKN.nextToken());
				lblDPTNM.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtEMPNO3"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
				txtEMPNO3.setText(L_STRTKN.nextToken());
				lblEMPNM3.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtEMPCT"))
			{
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
				txtEMPCT.setText(L_STRTKN.nextToken());
				lblEMPCT.setText(L_STRTKN.nextToken());
			}		
			else if(M_strHLPFLD.equals("txtLVECD"))
			{
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				((JTextField)tblTEEXR.cmpEDITR[TB_LVECD]).setText(L_STRTKN.nextToken());
			}	
			if(M_strHLPFLD.equals("txtEMPNO_OT"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
				txtEMPNO_OT.setText(L_STRTKN.nextToken());
				tblOVTDT.setValueAt(L_STRTKN.nextToken(),tblOVTDT.getSelectedRow(),TB_EMPNM_OT);
				tblOVTDT.setValueAt(L_STRTKN.nextToken(),tblOVTDT.getSelectedRow(),TB_EMPCT_OT);
				tblOVTDT.setValueAt(L_STRTKN.nextToken(),tblOVTDT.getSelectedRow(),TB_WRKSH_OT);
				tblOVTDT.setValueAt(L_STRTKN.nextToken().substring(0,5),tblOVTDT.getSelectedRow(),TB_POTTM_OT);
				tblOVTDT.setValueAt(L_STRTKN.nextToken().substring(0,5),tblOVTDT.getSelectedRow(),TB_SHRWK_OT);
			}
		
		}catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}	
	}

/**
 */	
	void getDATA()
	{
		//System.out.println("inside getdata");
		String M_strSQLQRY1="",M_strSQLQRY2="";
		ResultSet rstRSSET1,rstRSSET2;
		boolean L_flgUPD;
		int L_intCNT=0;					
		int L_intCNT1=0;					
		int L_intCNT1_OBJCD=0;					
		try
		{
			if(!vldDATA())
				return;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching Data,Please wait....",'N');
			tblTEEXR.clrTABLE();
			tblTEEXR1.clrTABLE();
			tblOBJCD.clrTABLE();
			inlTBLEDIT(tblTEEXR);
			
			//**setWRKDTX(txtWRKDT.getText().toString().trim());
			//datWRKDT1 = M_fmtLCDAT.parse(txtWRKDT.getText().toString().trim());
			//M_calLOCAL.setTime(datWRKDT1);      
			//M_calLOCAL.add(Calendar.DATE,-1);    
			//datWRKDT_1 = M_calLOCAL.getTime();
			
			//M_calLOCAL.setTime(datWRKDT1);      
			//M_calLOCAL.add(Calendar.DATE,+1);    
			//datWRKDT2 = M_calLOCAL.getTime();
			
			//M_calLOCAL.setTime(datWRKDT1);      
			//M_calLOCAL.add(Calendar.DATE,+2);
			//datWRKDT3 = M_calLOCAL.getTime();
			
			tblTEEXR.setRowSelectionInterval(0,0);
			tblTEEXR.setColumnSelectionInterval(0,0);

			//String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_STSFL<>'U' "+(txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ep_jondt <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"') "+(txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "")+(txtEMPNO3.getText().length()>0 ? " and EP_EMPNO = '"+ txtEMPNO3.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_SS = " SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"' and SS_STSFL <> 'X' ";
			String L_strWHRSTR_SW = " SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
			String L_strWHRSTR_AL1 = " SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and swt_inctm is not null and  swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"' and swt_stsfl not in ('2','X')";
			String L_strWHRSTR_AL2 = " SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and swt_outtm is not null and  swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"' and swt_stsfl not in ('2','X')";

			hstLVDTL.clear();
			if(chkFETCH.isSelected())
			{	
			    getATTNDT();
				setMSG("Fetching Data,Please wait....",'N');
				M_strSQLQRY=" select SW_EMPNO,SW_WRKDT,isnull(SW_LVECD,'') SW_LVECD,isnull(SW_LVEQT,0) SW_LVEQT ";
				M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and SW_CMPCD=EP_CMPCD and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW+" and SW_STSFL not in ('X')";
					//System.out.println("M_strSQLQRY>>>>>>>>>>>>>>>>>>>>>>>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{	
					while(M_rstRSSET.next())
					{	//System.out.println("M_rstRSSET.getString(SW_LVECD)>>"+M_rstRSSET.getString("SW_LVECD"));
						if(M_rstRSSET.getString("SW_LVECD") != null && M_rstRSSET.getString("SW_LVECD").length()==2)
							hstLVDTL.put(M_rstRSSET.getString("sw_empno")+M_rstRSSET.getString("sw_wrkdt"),nvlSTRVL(M_rstRSSET.getString("sw_lvecd"),""));
					}
					M_rstRSSET.close();
				}	
				
				//System.out.println("Hash Table values>>"+hstLVDTL);			

				//String L_strWHRSTR_AL1 = " epa_pnctm is not null and epa_inocd = '0' and epa_pnctm between '"+fmtDBDATTM_YMD.format(datWRKDT1)+"-06.30.00' and '"+fmtDBDATTM_YMD.format(datWRKDT2)+"-06.30.00'";
				//String L_strWHRSTR_AL2 = " epa_pnctm is not null and epa_inocd = '1' and epa_pnctm between '"+fmtDBDATTM_YMD.format(datWRKDT1)+"-10.00.00' and '"+fmtDBDATTM_YMD.format(datWRKDT2)+"-10.00.00'";

				
				prcDATA(txtSTRDT.getText(),txtENDDT.getText(),txtEMPNO3.getText().toString().trim(),txtDPTCD.getText().toString().trim(),txtEMPCT.getText().toString().trim());

				/*M_strSQLQRY=" select SS_SBSCD,SS_EMPNO,SS_WRKDT, ifnull(SS_ORGSH,'') SS_ORGSH, ifnull(SS_CURSH,'') SS_CURSH, ifnull(SS_LVECD,'') SS_LVECD, CMT_CHP01, CMT_CHP02 ";
				M_strSQLQRY+= " from HR_SSTRN,CO_CDTRN where CMT_CGMTP = 'M"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'COXXSHF' and CMT_CODCD=SS_CURSH and "+L_strWHRSTR_SS+" and SS_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") order by ss_empno";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			
				//System.out.println("Query>>>"+M_strSQLQRY);
				exeSWMUPD_SS(M_rstRSSET);

				//M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,swt_srlno,min(swt_inctm) swt_PNCTM ";
				//M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL1+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt,swt_srlno order by swt_empno,swt_wrkdt,swt_srlno";
				M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,min(swt_inctm) swt_PNCTM ";
				M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL1+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt order by swt_empno,swt_wrkdt";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				exeSWMUPD_AL(M_rstRSSET, "IN");

				//M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,swt_srlno,max(swt_outtm) swt_PNCTM ";
				//M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL2+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt,swt_srlno order by swt_empno,swt_wrkdt,swt_srlno";
				M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,max(swt_outtm) swt_PNCTM ";
				M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL2+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt order by swt_empno,swt_wrkdt";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				exeSWMUPD_AL(M_rstRSSET, "OUT");

				String L_strWHRSTR_LV = " lvt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and lvt_lvedt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText().toString().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().toString().trim()))+"'";
				M_strSQLQRY=  " select ifnull(LVT_LVECD,'') LVT_LVECD,ifNull(LVT_LVEQT,0) LVT_LVEQT,ifnull(LVT_EMPNO,'') LVT_EMPNO,ifnull(LVT_STSFL,'') LVT_STSFL, LVT_LVEDT ";
				M_strSQLQRY+= " from HR_LVTRN where "+L_strWHRSTR_LV+" and LVT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") ";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				exeSWMUPD_LV(M_rstRSSET);
			
				M_strSQLQRY=" update HR_SWMST set SW_WRKSH = SW_CURSH where SW_WRKSH is null and ifnull(SW_CURSH,'X')<> 'X' and "+L_strWHRSTR_SW;
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"");

				
				M_strSQLQRY=" select SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_ORGSH,SW_CURSH,ifnull(SW_WRKSH,'X') SW_WRKSH,SW_ACTWK, SW_SHRWK, SW_EXTWK, SW_OVTWK, SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT,trim(ifnull(ep_lstnm,' '))||' '  ||left(ifnull(ep_fstnm,' '),1)||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
				M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and SW_CMPCD = EP_CMPCD and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW;
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				setSTDTM(M_rstRSSET);
				
				M_strSQLQRY=" update hr_swmst set sw_lvecd='WO',sw_lveqt=1.0 where sw_wrksh = 'O' and ifnull(sw_lvecd,'') = ''   and "+L_strWHRSTR_SW;
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				cl_dat.exeDBCMT("W.Off");			
				
				
				M_strSQLQRY=" select SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_ORGSH,SW_CURSH,SW_WRKSH,SW_ACTWK, SW_SHRWK, SW_EXTWK, SW_OVTWK, SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT,trim(ifnull(ep_lstnm,' '))||' '  ||left(ifnull(ep_fstnm,' '),1)||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
				M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and SW_CMPCD = EP_CMPCD and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW;
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				
				updWRKHR(M_rstRSSET);*/
			}
			chkFETCH.setSelected(false);
			hstMINTM_SWT.clear();
			hstMAXTM_SWT.clear();
			M_strSQLQRY=" select SWT_WRKDT,SWT_EMPNO, min(SWT_INCTM) SWT_INCTM,max(SWT_OUTTM) SWT_OUTTM from HR_SWTRN where char(SWT_WRKDT) + SWT_EMPNO in (select char(SW_WRKDT) + SW_EMPNO from HR_SWMST where  "+L_strWHRSTR_SW+") group by SWT_WRKDT,SWT_EMPNO order by SWT_WRKDT,SWT_EMPNO";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			while(M_rstRSSET!=null && M_rstRSSET.next())	
			{
				hstMINTM_SWT.put(M_fmtLCDAT.format(M_rstRSSET.getDate("SWT_WRKDT"))+M_rstRSSET.getString("SWT_EMPNO"),(M_rstRSSET.getString("SWT_INCTM")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SWT_INCTM")) : ""));
				if(M_rstRSSET.getTimestamp("SWT_OUTTM")!=null && M_rstRSSET.getTimestamp("SWT_INCTM")!=null)
				{
					if(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SWT_OUTTM")).compareTo(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SWT_INCTM")))<=0)
					    hstMAXTM_SWT.put(M_fmtLCDAT.format(M_rstRSSET.getDate("SWT_WRKDT"))+M_rstRSSET.getString("SWT_EMPNO"),"");
					else
						hstMAXTM_SWT.put(M_fmtLCDAT.format(M_rstRSSET.getDate("SWT_WRKDT"))+M_rstRSSET.getString("SWT_EMPNO"),(M_rstRSSET.getString("SWT_OUTTM")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SWT_OUTTM")) : ""));
				}
				else
					hstMAXTM_SWT.put(M_fmtLCDAT.format(M_rstRSSET.getDate("SWT_WRKDT"))+M_rstRSSET.getString("SWT_EMPNO"),(M_rstRSSET.getString("SWT_OUTTM")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SWT_OUTTM")) : ""));
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			// Fetching records from HR_SWMST for displaying in table
			M_strSQLQRY=" select CO_EMPNO,CO_WRKDT,CO_LVECD from HR_COTRN where  ";
			M_strSQLQRY+=" CO_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and CO_STSFL = 'W' and CO_REFDT is null and CO_EMPNO in (select SW_EMPNO from  HR_SWMST where "+ L_strWHRSTR_SW+")";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			vtrCOTRN_W.clear();
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(!vtrCOTRN_W.contains(M_rstRSSET.getString("CO_EMPNO")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("CO_WRKDT"))+"|"+M_rstRSSET.getString("CO_LVECD")))
						vtrCOTRN_W.add(M_rstRSSET.getString("CO_EMPNO")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("CO_WRKDT"))+"|"+M_rstRSSET.getString("CO_LVECD"));
				}
				M_rstRSSET.close();
			}				
			M_strSQLQRY=" select CO_EMPNO,CO_WRKDT,CO_REFDT,CO_LVECD from HR_COTRN where  ";
			M_strSQLQRY+=" CO_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and CO_STSFL = 'A' and CO_REFDT is not null and CO_EMPNO in (select SW_EMPNO from  HR_SWMST where "+ L_strWHRSTR_SW+")";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			vtrCOTRN_A.clear();
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					if(!vtrCOTRN_A.contains(M_rstRSSET.getString("CO_EMPNO")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("CO_WRKDT"))+"|"+M_rstRSSET.getString("CO_LVECD")))
						vtrCOTRN_A.add(M_rstRSSET.getString("CO_EMPNO")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("CO_WRKDT"))+"|"+M_rstRSSET.getString("CO_LVECD"));
					if(!vtrCOTRN_A.contains(M_rstRSSET.getString("CO_EMPNO")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("CO_REFDT"))+"|"+M_rstRSSET.getString("CO_LVECD")))
						vtrCOTRN_A.add(M_rstRSSET.getString("CO_EMPNO")+"|"+M_fmtLCDAT.format(M_rstRSSET.getDate("CO_REFDT"))+"|"+M_rstRSSET.getString("CO_LVECD"));
				}
				M_rstRSSET.close();
			}				
			
			M_strSQLQRY=" select SW_SBSCD,SW_WRKDT,SW_EMPNO,isnull(SW_ORGSH,'') SW_ORGSH,isnull(SW_CURSH,'') SW_CURSH,isnull(SW_WRKSH,'') SW_WRKSH,isnull(SW_ACTWK,'00:00') SW_ACTWK,isnull(SW_SHRWK,'00:00') SW_SHRWK,isnull(SW_EXTWK,'00:00') SW_EXTWK,isnull(SW_OVTWK,'00:00') SW_OVTWK,isnull(SW_OVTHR,'00:00') SW_OVTHR,isnull(SW_POTTM,'00:00') SW_POTTM,SW_INCTM,SW_OUTTM,isnull(SW_LVECD,'') SW_LVECD,isnull(SW_LVEQT,0) SW_LVEQT,SW_INCST,SW_OUTST,isnull(SW_STSFL,'') SW_STSFL,isnull(SW_OBJCD,'00') SW_OBJCD,EP_EMPCT,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '   + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM, isnull(SW_PRSFL,'') SW_PRSFL, isnull(SW_WRKVL,0.000) SW_WRKVL, isnull(SW_PEDTM,'00:00') SW_PEDTM, isnull(SW_PEATM,'00:00') SW_PEATM, isnull(SW_OEXTM,'00:00') SW_OEXTM ";
			M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and SW_CMPCD = EP_CMPCD and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW;
			//if(!chkABEMP.isSelected())
			//{	
			//	M_strSQLQRY+=" and ((SW_INCTM IS NOT NULL or SW_INCTM IS NOT NULL) and ifnull(SW_LVECD,'')='') ";
			//}	
			M_strSQLQRY+=" and SW_STSFL not in ('X')";
			M_strSQLQRY+=" order by SW_WRKDT,SW_EMPNO";
				
			//System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			L_intCNT=0;
			L_intCNT1=0;
			L_intCNT1_OBJCD=0;
			
			String strLVECD,L_strEXPFL,L_strSQLQRY;
			ResultSet L_rstRSSET;
			
			
			////Exception Flag and its meaning is Stored in the Hash Table
			Hashtable<String,String> L_hstEXPFL=new Hashtable<String,String>(); 			
			try
			{
					L_strSQLQRY  = " Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					L_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HR01EXC'";
					L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET!=null)
					{
						while(L_rstRSSET.next())
							L_hstEXPFL.put(L_rstRSSET.getString("CMT_CODCD"),L_rstRSSET.getString("CMT_CODDS"));
						L_rstRSSET.close();
					}				
			}
			catch(Exception L_EX){
				setMSG(L_EX,"fetching EXPFL()");
			}		
			//////////////////////////////////////////////////////////////////
			
			
			String L_strCHGTM="";////var to calculate diff between standard in time and actual intime.
			boolean flgCHGTM;
			while(M_rstRSSET!=null && M_rstRSSET.next())	
			{
				L_strEXPFL="00";
				L_strCHGTM="";
				strLVECD=M_rstRSSET.getString("SW_LVECD");
				flgCHGTM=false;
				////to check whether punching time matches with assg. shift
				if(M_rstRSSET.getTimestamp("SW_INCTM") != null)
				{	
					if(M_rstRSSET.getTimestamp("SW_INCTM").compareTo(M_rstRSSET.getTimestamp("SW_INCST"))>0)
						L_strCHGTM = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")));
					else if(M_rstRSSET.getTimestamp("SW_INCST").compareTo(M_rstRSSET.getTimestamp("SW_INCTM"))>0)
						L_strCHGTM = calTIME(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")));
				}	
				if(!L_strCHGTM.equals("") && fmtHHMM.parse(L_strCHGTM).compareTo(fmtHHMM.parse("01:00"))>0)
			 		flgCHGTM=true;
					
				////////////////////////////////////////////////////////////
				
				//System.out.println("SW_EMPNO>>"+M_rstRSSET.getString("SW_EMPNO"));
				//System.out.println("L_strCHGTM>>"+L_strCHGTM);
				//System.out.println("flgCHGTM>>"+flgCHGTM);
				L_strEXPFL="00";
				if(M_rstRSSET.getString("SW_STSFL").equals("9"))/// Authorised record
					L_strEXPFL="00";
				else if(M_rstRSSET.getString("SW_INCTM") == null && M_rstRSSET.getString("SW_OUTTM") == null && M_rstRSSET.getString("SW_LVECD").equals("WO") && !M_rstRSSET.getString("SW_STSFL").equals("2"))/// Valid weekly off
					L_strEXPFL="00";
				else if(M_rstRSSET.getString("SW_INCTM")==null)///in punching missing
					L_strEXPFL="01";
				else if(M_rstRSSET.getString("SW_OUTTM")==null)///out punching missing
					L_strEXPFL="02";
				else if(flgCHGTM)////Punching time does not match with assigned Shift
					L_strEXPFL="03";
				else if((M_rstRSSET.getString("SW_INCTM")!=null || M_rstRSSET.getString("SW_OUTTM")!=null) && M_rstRSSET.getString("SW_LVECD").equals("WO"))///working on weekly off
					L_strEXPFL="04";
				else if((M_rstRSSET.getString("SW_INCTM")!=null || M_rstRSSET.getString("SW_OUTTM")!=null) && (M_rstRSSET.getString("SW_LVECD").equals("CL") || M_rstRSSET.getString("SW_LVECD").equals("PL") || M_rstRSSET.getString("SW_LVECD").equals("SL")))///working on sanc leave
					L_strEXPFL="05";
				else if((M_rstRSSET.getString("SW_INCTM")!=null || M_rstRSSET.getString("SW_OUTTM")!=null) && M_rstRSSET.getString("SW_LVECD").equals("PH"))///working on RH
					L_strEXPFL="06";
				else if(fmtHHMMSS.parse(M_rstRSSET.getString("SW_SHRWK")).compareTo(fmtHHMMSS.parse("00:15:00"))>0)////Partial working
					L_strEXPFL="10";
				else if((M_rstRSSET.getString("SW_INCTM")==null && M_rstRSSET.getString("SW_OUTTM")==null) && (!M_rstRSSET.getString("SW_LVECD").equals("WO") && M_rstRSSET.getString("SW_LVECD").length()==2 && !M_rstRSSET.getString("SW_STSFL").equals("9")))///Leave Confirmation
					L_strEXPFL="88";
				else if(M_rstRSSET.getString("SW_WRKSH").equals("") || M_rstRSSET.getString("SW_WRKSH").equals(" ") || M_rstRSSET.getString("SW_STSFL").equals("2")) /// Working shift not defined
					L_strEXPFL="99";
				else if((M_rstRSSET.getString("SW_INCTM") == null || M_rstRSSET.getString("SW_OUTTM") == null) && !M_rstRSSET.getString("SW_LVECD").equals("WO"))/// Absent Employees
					L_strEXPFL="99";
				//else if((M_rstRSSET.getString("SW_INCTM")==null && M_rstRSSET.getString("SW_OUTTM")==null) && !M_rstRSSET.getString("SW_LVECD").equals(""))///
				//	L_strEXPFL="00";
				//System.out.println(M_rstRSSET.getString("SW_EMPNO")+"  "+L_strEXPFL);
				if(!L_strEXPFL.equals("00"))
				{
					///////Exceptions Table
					tblTEEXR.setValueAt(M_rstRSSET.getString("EP_EMPNM"),L_intCNT,TB_EMPNM);
					tblTEEXR.setValueAt(M_rstRSSET.getString("SW_EMPNO"),L_intCNT,TB_EMPNO);
					//tblTEEXR.setValueAt(M_rstRSSET.getString("SW_ORGSH"),L_intCNT,TB_ORGSH);
					tblTEEXR.setValueAt(M_rstRSSET.getString("SW_CURSH"),L_intCNT,TB_CURSH);
					if(M_rstRSSET.getString("SW_WRKSH")==null)
						tblTEEXR.setValueAt(M_rstRSSET.getString("SW_CURSH"),L_intCNT,TB_WRKSH);
					else
						tblTEEXR.setValueAt(M_rstRSSET.getString("SW_WRKSH"),L_intCNT,TB_WRKSH);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_INCTM")!=null ? nvlSTRVL(M_rstRSSET.getString("SW_INCTM").substring(11,16),"") : ""),L_intCNT,TB_INCTM);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_OUTTM")!=null ? nvlSTRVL(M_rstRSSET.getString("SW_OUTTM").substring(11,16),"") : ""),L_intCNT,TB_OUTTM);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_ACTWK")!=null ? nvlSTRVL(M_rstRSSET.getString("SW_ACTWK").substring(0,5),"") : ""),L_intCNT,TB_ACTWK);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_SHRWK")!=null ? nvlSTRVL(M_rstRSSET.getString("SW_SHRWK").substring(0,5),"") : ""),L_intCNT,TB_SHRWK);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_EXTWK")!=null ? nvlSTRVL(M_rstRSSET.getString("SW_EXTWK").substring(0,5),"") : ""),L_intCNT,TB_EXTWK);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_OVTWK")!=null ? nvlSTRVL(M_rstRSSET.getString("SW_OVTWK").substring(0,5),"") : ""),L_intCNT,TB_OVTWK);
					tblTEEXR.setValueAt(M_rstRSSET.getString("SW_LVECD"),L_intCNT,TB_LVECD);
					tblTEEXR.setValueAt(M_rstRSSET.getString("SW_LVEQT"),L_intCNT,TB_LVEQT);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_INCTM")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")) : ""),L_intCNT,TB_INCTM_A);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_OUTTM")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTTM")) : ""),L_intCNT,TB_OUTTM_A);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_INCST")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")) : ""),L_intCNT,TB_INCTM_S);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_OUTST")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")) : ""),L_intCNT,TB_OUTTM_S);
					tblTEEXR.setValueAt((M_rstRSSET.getString("SW_WRKDT")!=null ? M_fmtLCDAT.format(M_rstRSSET.getTimestamp("SW_WRKDT")) : ""),L_intCNT,TB_WRKDT);
					tblTEEXR.setValueAt(M_rstRSSET.getString("EP_EMPCT"),L_intCNT,TB_EMPCT_SW);
					tblTEEXR.setValueAt(L_hstEXPFL.get(L_strEXPFL).toString(),L_intCNT,TB_EXPFL);
					L_intCNT++;
				}
				else
				{
					///////regular table
					if(M_rstRSSET.getString("SW_OBJCD").equals("00"))
						{setTEEXR1(M_rstRSSET,tblTEEXR1,L_intCNT1);		L_intCNT1++;}
					else if(!M_rstRSSET.getString("SW_OBJCD").equals("00"))
						{setTEEXR1(M_rstRSSET,tblOBJCD,L_intCNT1_OBJCD);	L_intCNT1_OBJCD++;}
					if(M_rstRSSET.getString("SW_STSFL").equals("0"))
					{
						prcLDGR1(M_rstRSSET.getString("SW_EMPNO"),M_rstRSSET.getString("EP_EMPCT"),M_fmtLCDAT.format(M_rstRSSET.getTimestamp("SW_WRKDT")),(M_rstRSSET.getString("SW_INCTM")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")) : ""),(M_rstRSSET.getString("SW_OUTTM")!=null ? M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTTM")) : ""),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCST")),M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTST")),fmtHHMM.format(M_rstRSSET.getTime("SW_PEDTM")));
						updSWMST_STS(M_rstRSSET.getString("SW_EMPNO"),M_rstRSSET.getDate("SW_WRKDT"));
					}
				}	
			}
			if(M_rstRSSET==null)
				M_rstRSSET.close();
			setMSG("",'N');
			setCursor(cl_dat.M_curDFSTS_pbst);	
		}
		catch(Exception L_EX){
			setMSG(L_EX,"getDATA()");
		}
	}


private void setTEEXR1(ResultSet LP_RSSET,cl_JTable LP_TBLNM,int LP_ROWCT)
{
	try
	{
					LP_TBLNM.setValueAt(LP_RSSET.getString("EP_EMPNM"),LP_ROWCT,TB_EMPNM1);
					LP_TBLNM.setValueAt(LP_RSSET.getString("SW_EMPNO"),LP_ROWCT,TB_EMPNO1);
					LP_TBLNM.setValueAt(LP_RSSET.getString("SW_WRKSH"),LP_ROWCT,TB_WRKSH1);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_INCTM")!=null ? nvlSTRVL(LP_RSSET.getString("SW_INCTM").substring(11,16),"") : ""),LP_ROWCT,TB_INCTM1);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_OUTTM")!=null ? nvlSTRVL(LP_RSSET.getString("SW_OUTTM").substring(11,16),"") : ""),LP_ROWCT,TB_OUTTM1);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_ACTWK")!=null ? nvlSTRVL(LP_RSSET.getString("SW_ACTWK"),"") : ""),LP_ROWCT,TB_ACTWK1);
					LP_TBLNM.setValueAt(LP_RSSET.getString("SW_LVECD"),LP_ROWCT,TB_LVECD1);
					LP_TBLNM.setValueAt(LP_RSSET.getString("SW_LVEQT"),LP_ROWCT,TB_LVEQT1);
					LP_TBLNM.setValueAt(LP_RSSET.getString("EP_EMPCT"),LP_ROWCT,TB_EMPCT_SW1);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_WRKDT")!=null ? M_fmtLCDAT.format(LP_RSSET.getTimestamp("SW_WRKDT")) : ""),LP_ROWCT,TB_WRKDT1);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_SHRWK")!=null ? nvlSTRVL(LP_RSSET.getString("SW_SHRWK"),"") : ""),LP_ROWCT,TB_SHRWK1);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_OVTHR")!=null ? nvlSTRVL(LP_RSSET.getString("SW_OVTHR"),"") : ""),LP_ROWCT,TB_OVTHR);
					//LP_TBLNM.setValueAt((LP_RSSET.getString("SW_POTTM")!=null ? nvlSTRVL(LP_RSSET.getString("SW_POTTM"),"") : ""),LP_ROWCT,TB_POTTM);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_PEDTM")!=null ? nvlSTRVL(LP_RSSET.getString("SW_PEDTM"),"") : ""),LP_ROWCT,TB_PEDTM);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_PEATM")!=null ? nvlSTRVL(LP_RSSET.getString("SW_PEATM"),"") : ""),LP_ROWCT,TB_PEATM);
					LP_TBLNM.setValueAt((LP_RSSET.getString("SW_OEXTM")!=null ? nvlSTRVL(LP_RSSET.getString("SW_OEXTM"),"") : ""),LP_ROWCT,TB_OEXTM);
					LP_TBLNM.setValueAt(LP_RSSET.getString("SW_PRSFL"),LP_ROWCT,TB_PRSFL);
					LP_TBLNM.setValueAt(LP_RSSET.getString("SW_WRKVL"),LP_ROWCT,TB_WRKVL);
		}
		catch(Exception L_EX){
			setMSG(L_EX,"setTEEXR1()");
		}
}
	
	
	void prcLDGR(String LP_EMPNO,String LP_EMPCT,String LP_DPTCD,String LP_STRDT,String LP_ENDDT)
	{
		//System.out.println("inside getdata");
		try
		{
			if(!vldDATA())
				return;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Processing Data,Please wait....",'N');
			ResultSet L_rstRSSET = null;
			
			
			tblTEEXR.setRowSelectionInterval(0,0);
			tblTEEXR.setColumnSelectionInterval(0,0);

			String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ep_jondt <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"') "+(LP_EMPCT.length()>0 ? " and EP_EMPCT= '"+LP_EMPCT.trim() +"'" : "")+(LP_DPTCD.length()>0 ? " and EP_DPTCD = '"+ LP_DPTCD.trim() +"'" : "")+(LP_EMPNO.length()>0 ? " and EP_EMPNO = '"+ LP_EMPNO.trim() +"'" : "");
			String L_strWHRSTR_SW = " SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT.trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT.trim()))+"'";
			String L_strWHRSTR_EX = " EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ex_docdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT.trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT.trim()))+"'";
			
			M_strSQLQRY=" select SW_EMPNO,SW_WRKDT,SW_SBSCD,SW_WRKDT,SW_EMPNO,isnull(SW_WRKSH,'') SW_WRKSH,SW_INCTM,SW_OUTTM,isnull(SW_LVECD,'') SW_LVECD,isnull(SW_LVEQT,0) SW_LVEQT,SW_INCST,SW_OUTST,isnull(SW_STSFL,'') SW_STSFL,EP_EMPCT,isnull(SW_PEDTM,'00:00') SW_PEDTM ";
			M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and SW_CMPCD = EP_CMPCD and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW;
			M_strSQLQRY+=" and SW_STSFL not in ('X')";
			M_strSQLQRY+=" order by SW_WRKDT,SW_EMPNO";
				
			//System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			String 	strEMPNO = "";
			String 	strWRKDT = "";
			String 	strINOTM0 = "00:00";
			String 	strINCST  = "00:00";
			String 	strINOTM1 = "00:00";
			String 	strOUTST  = "00:00";
			String 	strPEDTM  = "00:00";
			String 	strEMPCT  = "";
			String 	strSHRWK_TOT = "00:00";
			String 	strSHRWK_DAY = "00:00";
			String 	strSHRWK_IN = "00:00";
			String 	strSHRWK_OUT = "00:00";
			double dblSHRWK_DAY = 0.000;
			
			boolean flgCHGTM;
			while(L_rstRSSET!=null && L_rstRSSET.next())	
			{
				if(L_rstRSSET.getString("SW_INCTM") == null)
					continue;
				if(L_rstRSSET.getString("SW_OUTTM") == null)
					continue;
				strEMPNO = L_rstRSSET.getString("SW_EMPNO");
				//if(!strEMPNO.equals("5031"))
				//	continue;
				strWRKDT = M_fmtLCDAT.format(L_rstRSSET.getDate("SW_WRKDT"));
				strINOTM0 = M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_INCTM"));
				strINCST  = M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_INCST"));
				strINOTM1 = M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_OUTTM"));
				strOUTST  = M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_OUTST"));
				strPEDTM  = fmtHHMM.format(L_rstRSSET.getTime("SW_PEDTM"));
				strEMPCT  = L_rstRSSET.getString("EP_EMPCT");
				
				prcLDGR1(strEMPNO,strEMPCT,strWRKDT,strINOTM0,strINOTM1,strINCST,strOUTST,strPEDTM);

				/*strSHRWK_TOT = "00:00";
				strSHRWK_DAY = "00:00";
				strSHRWK_IN = "00:00";
				strSHRWK_OUT = "00:00";
				dblSHRWK_DAY = 0.000;
				
					
				//System.out.println(strINOTM0+">>"+strINCST+">>"+strINOTM1+">>"+strOUTST);
				if(M_fmtLCDTM.parse(strINOTM0).compareTo(M_fmtLCDTM.parse(strINCST))>0)  
				{
					if(fmtHHMM.parse(calTIME(strINOTM0,strINCST)).compareTo(fmtHHMM.parse(strGRCIN))>=0) 
						strSHRWK_IN = calTIME(strINOTM0,strINCST);
				}
				if(M_fmtLCDTM.parse(strINOTM1).compareTo(M_fmtLCDTM.parse(strOUTST))<0)  
				{
						strSHRWK_OUT = calTIME(strOUTST,strINOTM1);
				}
				System.out.println(strSHRWK_IN+">>"+strSHRWK_OUT);
				strSHRWK_TOT = addTIME(exeRNDOF(strSHRWK_IN,"IN"),exeRNDOF(strSHRWK_OUT,"IN"));
				if(strEMPCT.equals("OFF") && fmtHHMM.parse(strSHRWK_TOT).compareTo(fmtHHMM.parse("03:00"))<0)
					strSHRWK_TOT = "00:00";
				//dblSHRWK_DAY = Double.parseDouble(strSHRWK_DAY.substring(0,2)+"."+strSHRWK_DAY.substring(3,5));
				dblSHRWK_DAY = Double.parseDouble(calRND_HOURS(subTIME("08:00",strSHRWK_TOT)))/8; // how much he worked in a day. 1/total working.
				M_strSQLQRY = "update hr_swmst set SW_SHRWK = '"+strSHRWK_TOT+":00', sw_prsfl = 'P', sw_wrkvl = "+dblSHRWK_DAY+" where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strWRKDT))+"' and SW_EMPNO = '"+strEMPNO+"'";
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				cl_dat.exeDBCMT("prcLDGR");*/	
			}
			if(L_rstRSSET==null)
				L_rstRSSET.close();
			setMSG("",'N');
			setCursor(cl_dat.M_curDFSTS_pbst);	
		}
		catch(Exception L_EX){
			setMSG(L_EX,"prcLDGR()");
		}
	}
	
	
	
	void prcLDGR1(String LP_EMPNO,String LP_EMPCT,String LP_WRKDT,String LP_INOTM0,String LP_INOTM1,String LP_INCST,String LP_OUTST, String LP_PEDTM)
	{
		//System.out.println("inside getdata");
		try
		{
				if(LP_INOTM0.equals("") ||  LP_INOTM1.equals(""))
				   return;
				String strSHRWK_TOT = "00:00";
				String strSHRWK_DAY = "00:00";
				String strSHRWK_IN = "00:00";
				String strSHRWK_OUT = "00:00";
				double dblSHRWK_DAY = 0.000;
					//System.out.println(strINOTM0+">>"+strINCST+">>"+strINOTM1+">>"+strOUTST);
				if(M_fmtLCDTM.parse(LP_INOTM0).compareTo(M_fmtLCDTM.parse(LP_INCST))>0)  
				{
					if(fmtHHMM.parse(calTIME(LP_INOTM0,LP_INCST)).compareTo(fmtHHMM.parse(strGRCIN))>=0) 
						strSHRWK_IN = calTIME(LP_INOTM0,LP_INCST);
				}
				if(M_fmtLCDTM.parse(LP_INOTM1).compareTo(M_fmtLCDTM.parse(LP_OUTST))<0)  
				{
						strSHRWK_OUT = calTIME(LP_OUTST,LP_INOTM1);
				}
				
				//System.out.println(strSHRWK_IN+">>"+strSHRWK_OUT);
				strSHRWK_TOT = addTIME(exeRNDOF(strSHRWK_IN,"IN"),exeRNDOF(strSHRWK_OUT,"IN"));
				strSHRWK_TOT = addTIME(strSHRWK_TOT,exeRNDOF(LP_PEDTM,"IN"));
				//strSHRWK_TOT = addTIME(strSHRWK_IN,strSHRWK_OUT);
				//strSHRWK_TOT = addTIME(strSHRWK_TOT,LP_PEDTM);
				
				if(LP_EMPCT.equals("OFF") && fmtHHMM.parse(strSHRWK_TOT).compareTo(fmtHHMM.parse("03:00"))<0)
					strSHRWK_TOT = "00:00";
				//dblSHRWK_DAY = Double.parseDouble(strSHRWK_DAY.substring(0,2)+"."+strSHRWK_DAY.substring(3,5));
				dblSHRWK_DAY = Double.parseDouble(calRND_HOURS(subTIME("08:00",strSHRWK_TOT)))/8; // how much he worked in a day. 1/total working.
				M_strSQLQRY = "update hr_swmst set SW_SHRWK = '"+strSHRWK_TOT+":00', sw_prsfl = 'P', sw_wrkvl = "+dblSHRWK_DAY+" where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_WRKDT))+"' and SW_EMPNO = '"+LP_EMPNO+"'";
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				cl_dat.exeDBCMT("prcLDGR");			
		}
		catch(Exception L_EX){
			setMSG(L_EX,"prcLDGR1()");
		}
	}

	/**
	 * sets time from 60 hrs calculation to 100 hrs. e.g if 47.30 >> 47.50
	 * if 14.15 >> 14.30    if 12.45 >> 12.75 
	 */
	private String calRND_HOURS(String LP_TIME)
	{
		try
		{
			int intMNS = Integer.parseInt(LP_TIME.substring(3,5));
			int intMNS_RND = 100*intMNS/60;
			String HRS = LP_TIME.substring(0,2);
			String MNS = (String.valueOf(intMNS_RND).length()==1?"0"+intMNS_RND:""+intMNS_RND);
			//System.out.println(LP_TIME+">>>>>>>>"+HRS+"."+MNS);
			return HRS+"."+MNS;
		}
		catch(Exception E)
		{
			setMSG(E,"calRND_HOURS()");
		}
		return LP_TIME;
	}
	
	void setWRKDTX(String LP_WRKDT)
	{
		try
		{
			//datWRKDT1 = M_fmtLCDAT.parse(LP_WRKDT);
			//M_calLOCAL.setTime(datWRKDT1);      
			//M_calLOCAL.add(Calendar.DATE,-1);    
			//datWRKDT_1 = M_calLOCAL.getTime();
			
			//M_calLOCAL.setTime(datWRKDT1);      
			//M_calLOCAL.add(Calendar.DATE,+1);    
			//datWRKDT2 = M_calLOCAL.getTime();
			
			//M_calLOCAL.setTime(datWRKDT1);      
			//M_calLOCAL.add(Calendar.DATE,+2);
			//datWRKDT3 = M_calLOCAL.getTime();
		
				datWRKDT1 = M_fmtLCDAT.parse(LP_WRKDT);
				M_calLOCAL.setTime(datWRKDT1);      
				M_calLOCAL.add(Calendar.DATE,-2);    
				datWRKDT_2 = M_calLOCAL.getTime();

				datWRKDT1 = M_fmtLCDAT.parse(LP_WRKDT);
				M_calLOCAL.setTime(datWRKDT1);      
				M_calLOCAL.add(Calendar.DATE,-1);    
				datWRKDT_1 = M_calLOCAL.getTime();
				
				M_calLOCAL.setTime(datWRKDT1);      
				M_calLOCAL.add(Calendar.DATE,+1);    
				datWRKDT2 = M_calLOCAL.getTime();
				
				M_calLOCAL.setTime(datWRKDT1);      
				M_calLOCAL.add(Calendar.DATE,+2);
				datWRKDT3 = M_calLOCAL.getTime();
		}
		catch(Exception L_EX){
			setMSG(L_EX,"setWRKDTX");
		}
	}
	
	
	void prcDATA(String LP_STRDT,String LP_ENDDT,String LP_EMPNO,String LP_DPTCD,String LP_EMPCT)
	{
		//System.out.println("LP_STRDT>>>>>"+LP_STRDT);
		//System.out.println("LP_ENDDT>>>>>"+LP_ENDDT);
		
		String M_strSQLQRY1="",M_strSQLQRY2="";
		ResultSet rstRSSET1,rstRSSET2;
		boolean L_flgUPD;
		int L_intCNT=0;					
		int L_intCNT1=0;					
		try
		{
			/*tblTEEXR.clrTABLE();
			tblTEEXR1.clrTABLE();
			inlTBLEDIT(tblTEEXR);
			
			tblTEEXR.setRowSelectionInterval(0,0);
			tblTEEXR.setColumnSelectionInterval(0,0);
			*/
			//String L_strWHRSTR_MST = " EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"'  and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"') ";
			String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ep_jondt <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' and (ep_lftdt is null or ep_lftdt > '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"') "+(LP_EMPCT.length()>0 ? " and EP_EMPCT= '"+LP_EMPCT.toString().trim() +"'" : "")+(LP_DPTCD.length()>0 ? " and EP_DPTCD = '"+ LP_DPTCD.toString().trim() +"'" : "")+(LP_EMPNO.length()>0 ? " and EP_EMPNO = '"+ LP_EMPNO.toString().trim() +"'" : "");
			String L_strWHRSTR_SS = " SS_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ss_wrkdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  and SS_STSFL <> 'X' ";
			String L_strWHRSTR_SW = " SW_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt  between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"' ";
			String L_strWHRSTR_AL1 = " SWT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and swt_inctm is not null and  swt_wrkdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  and swt_stsfl not in ('2','X')";
			String L_strWHRSTR_AL2 = " SWT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and swt_outtm is not null and  swt_wrkdt  between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  and swt_stsfl not in ('2','X')";

			hstLVDTL.clear();
			M_strSQLQRY=" select SW_EMPNO,SW_WRKDT,SW_LVECD,SW_LVECD";
			M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and SW_CMPCD = EP_CMPCD and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW+" and SW_STSFL not in ('2','X')";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
            if(M_rstRSSET != null)
            {	
				while(M_rstRSSET.next())
				{	
					if(M_rstRSSET.getString("SW_LVECD") != null)
						hstLVDTL.put(M_rstRSSET.getString("sw_empno")+M_rstRSSET.getString("sw_wrkdt"),nvlSTRVL(M_rstRSSET.getString("sw_lvecd"),""));
				}
				M_rstRSSET.close();
			}	

			System.out.println("Processing SWMST....");
			//M_strSQLQRY=" select SS_SBSCD,SS_EMPNO, SS_WRKDT,ifnull(SS_ORGSH,'') SS_ORGSH, ifnull(SS_CURSH,'') SS_CURSH, ifnull(SS_LVECD,'') SS_LVECD, CMT_CHP01, CMT_CHP02 ";
			M_strSQLQRY=" select SS_SBSCD,SS_EMPNO, SS_WRKDT, isnull(SS_CURSH,'') SS_CURSH, isnull(SS_LVECD,'') SS_LVECD, CMT_CHP01, CMT_CHP02 ";
			M_strSQLQRY+= " from HR_SSTRN,CO_CDTRN where CMT_CGMTP = 'M"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'COXXSHF' and CMT_CODCD=SS_CURSH and "+L_strWHRSTR_SS+" and SS_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") order by ss_empno";
			ResultSet L_rstRSSET_SS = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//System.out.println("Query>>>exeSWMUPD_SS>>>"+M_strSQLQRY);
            exeSWMUPD_SS(L_rstRSSET_SS);
			System.out.println("Processed SWMST....");
			
			System.out.println("Processing SWMST(IN)....");
			//M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,swt_srlno,min(swt_inctm) swt_PNCTM ";
			//M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL1+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt,swt_srlno order by swt_empno,swt_wrkdt,swt_srlno";
			M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,min(swt_inctm) swt_PNCTM ";
			M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL1+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt order by swt_empno,swt_wrkdt";
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//System.out.println(">>>>exeSWMUPD_AL IN >>>>"+M_strSQLQRY);
            exeSWMUPD_AL(M_rstRSSET, "IN");
			System.out.println("Processed SWMST(IN)....");

			System.out.println("Processed SWMST(OUT)....");
			//M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,swt_srlno,max(swt_outtm) swt_PNCTM ";
			//M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL2+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt,swt_srlno order by swt_empno,swt_wrkdt,swt_srlno";
			M_strSQLQRY=  " select swt_EMPNO,swt_wrkdt,max(swt_outtm) swt_PNCTM ";
			M_strSQLQRY+= " from HR_SWTRN where "+L_strWHRSTR_AL2+"  and SWT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") group by swt_empno,swt_wrkdt order by swt_empno,swt_wrkdt";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
            exeSWMUPD_AL(M_rstRSSET, "OUT");
			System.out.println("Processed SWMST(OUT)....");

			System.out.println("Processing SWMST(LV)....");
			String L_strWHRSTR_LV = " LVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and lvt_lvedt  between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_STRDT))+"' and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_ENDDT))+"'  ";
			M_strSQLQRY=  " select LVT_LVECD,isnull(LVT_LVEQT,0) LVT_LVEQT,LVT_EMPNO,LVT_STSFL,LVT_LVEDT";
			M_strSQLQRY+= " from HR_LVTRN where "+L_strWHRSTR_LV+"  and LVT_LVECD <> 'PE' and LVT_EMPNO in (select EP_EMPNO from HR_EPMST where "+L_strWHRSTR_MST+ ") ";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
            exeSWMUPD_LV(M_rstRSSET);
			System.out.println("Processed SWMST(LV)....");
			
			M_strSQLQRY=" update HR_SWMST set SW_WRKSH = SW_CURSH where SW_WRKSH is null and isnull(SW_CURSH,' ')<> ' ' and "+L_strWHRSTR_SW;
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"");

			System.out.println("Processing Standard time....");
			//M_strSQLQRY=" select SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_ORGSH,ifnull(SW_CURSH,' ') SW_CURSH,ifnull(SW_WRKSH,' ') SW_WRKSH,SW_ACTWK, SW_SHRWK, SW_EXTWK, SW_OVTWK, SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT,trim(ifnull(ep_lstnm,' '))||' '  ||left(ifnull(ep_fstnm,' '),1)||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
			M_strSQLQRY=" select SW_SBSCD,SW_EMPNO,SW_WRKDT,isnull(SW_CURSH,' ') SW_CURSH,isnull(SW_WRKSH,' ') SW_WRKSH,SW_ACTWK, SW_SHRWK, SW_EXTWK, SW_OVTWK, SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '  + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ";
			M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW;
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			setSTDTM(M_rstRSSET);
			System.out.println("Processed Standard time....");
			
			M_strSQLQRY=" update hr_swmst set sw_lvecd='WO',sw_lveqt=1.0 where sw_wrksh = 'O' and isnull(sw_lvecd,'') = ''   and "+L_strWHRSTR_SW;
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			cl_dat.exeDBCMT("W.Off");			
			
			
			System.out.println("Processing Work Hrs....");
			//M_strSQLQRY=" select SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_ORGSH,ifnull(SW_CURSH,' ') SW_CURSH,ifnull(SW_WRKSH,' ') SW_WRKSH,SW_ACTWK, SW_SHRWK, SW_EXTWK, SW_OVTWK, SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT,trim(ifnull(ep_lstnm,' '))||' '  ||left(ifnull(ep_fstnm,' '),1)||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
			M_strSQLQRY=" select SW_SBSCD,SW_EMPNO,SW_WRKDT,isnull(SW_CURSH,' ') SW_CURSH,isnull(SW_WRKSH,' ') SW_WRKSH,SW_ACTWK, SW_SHRWK, SW_EXTWK, SW_OVTWK, SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '  + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ";
			M_strSQLQRY+=" from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and "+L_strWHRSTR_MST+" and "+L_strWHRSTR_SW;
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			updWRKHR(M_rstRSSET);
			System.out.println("Processed Work Hrs....");
			
		}
		catch(Exception L_EX){
			setMSG(L_EX,"prcDATA()");
		}
	}
	
	
	/** updates SW_STSFL=9 if SW_STSFL=0 and if there is no exception. 
	 */
	private void updSWMST_STS(String LP_EMPNO,java.util.Date LP_WRKDT)
	{
		try
		{
			String L_strWHRSTR_MST = " EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ep_jondt <= '"+LP_WRKDT+"' and (ep_lftdt is null or ep_lftdt > '"+LP_WRKDT+"') "+(txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "")+(txtEMPNO3.getText().length()>0 ? " and EP_EMPNO = '"+ txtEMPNO3.getText().toString().trim() +"'" : "");
			String L_strWHRSTR_SW = " SW_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt = '"+LP_WRKDT+"' ";
			String strSQLQRY1;
			strSQLQRY1 = " update HR_SWMST set SW_STSFL = '9'"; 
			strSQLQRY1+= " where SW_EMPNO ='"+LP_EMPNO.trim()+"' and "+L_strWHRSTR_SW;
			cl_dat.exeSQLUPD(strSQLQRY1,"updSWMST_STS");
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeDBCMT("exeSWMST_STS");
			//System.out.println(">>update"+strSQLQRY1);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updSWMST_STS");
		}
	}
	
	
	
	/** Fetching Shift Standard Time
	 */
	private String getSHFTM_ST(String LP_SHFCD,String LP_SHFFL)
	{
		String L_strRETVL = "";
		if(LP_SHFCD.toUpperCase().equals("O") || LP_SHFCD.toUpperCase().trim().equals(""))
		   LP_SHFCD = "G";
		try
		{
			if(!cl_dat.M_hstMKTCD_pbst.containsKey("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD))
				return L_strRETVL;
			if(LP_SHFFL.equalsIgnoreCase("IN"))
			{
				if(cl_dat.M_strCMPCD_pbst.equals("11"))
					datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)) >= 6 ? datWRKDT1 : datWRKDT2;
				else
					datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)) >= 7 ? datWRKDT1 : datWRKDT2;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)+"."+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(3,5)+".00";
			}
			if(LP_SHFFL.equalsIgnoreCase("OUT"))
			{
				if(cl_dat.M_strCMPCD_pbst.equals("11"))
					datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)) <= 6 ? datWRKDT2 : datWRKDT1;
				else
					datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)) <= 7 ? datWRKDT2 : datWRKDT1;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)+"."+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(3,5)+".00";
			}
			//System.out.println(M_fmtLCDAT.format(datWRKDT1)+"/"+LP_SHFCD+"   "+L_strRETVL);	
		}
		catch(Exception L_EX){
			setMSG(L_EX,"getSHFTM_ST");
		}	
		return L_strRETVL;
	}	
	
	
	/**
	 */
	public void exeSWMUPD_SS(ResultSet LP_RSSET)
	{
		try
		{
			if(LP_RSSET==null || !LP_RSSET.next())
				return;
			String  L_strEMPNO,L_strWRKDT, L_strINCST, L_strOUTST, L_strWHRSTR_SW, L_strWHRSTR_SS;
			//System.out.println("001");
			while(true)	
			{
				cl_dat.M_flgLCUPD_pbst = true;
				L_strEMPNO = LP_RSSET.getString("SS_EMPNO");
				L_strWRKDT = M_fmtLCDAT.format(LP_RSSET.getDate("SS_WRKDT"));

				setWRKDTX(L_strWRKDT);
				
				L_strINCST = getSHFTM_ST(LP_RSSET.getString("SS_CURSH"),"IN");
				L_strOUTST = getSHFTM_ST(LP_RSSET.getString("SS_CURSH"),"OUT");
				
				//datWRKDT = Integer.parseInt(LP_RSSET.getString("CMT_CHP01").substring(0,2)) >= 7 ? datWRKDT1 : datWRKDT2;
				//L_strINCST = fmtDBDATTM_YMD.format(datWRKDT)+"-"+LP_RSSET.getString("CMT_CHP01").substring(0,2)+"."+LP_RSSET.getString("CMT_CHP01").substring(3,5)+".00";
				
				//datWRKDT = Integer.parseInt(LP_RSSET.getString("CMT_CHP02").substring(0,2)) <= 7 ? datWRKDT2 : datWRKDT1;
				//L_strOUTST = fmtDBDATTM_YMD.format(datWRKDT)+"-"+LP_RSSET.getString("CMT_CHP02").substring(0,2)+"."+LP_RSSET.getString("CMT_CHP02").substring(3,5)+".00";
				
				L_strWHRSTR_SW = " SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO = '"+L_strEMPNO+"' and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'";
				L_strWHRSTR_SS = " SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SS_EMPNO = '"+L_strEMPNO+"' and SS_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'";
				flgCHK_EXIST =	chkEXIST("HR_SWMST", L_strWHRSTR_SW);
				
				if(!flgCHK_EXIST)
				{
					//System.out.println("flgCHK_EXIST>>"+flgCHK_EXIST);
					//M_strSQLQRY="insert into HR_SWMST(SW_CMPCD,SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_ORGSH,SW_CURSH,SW_WRKSH,SW_INCST,SW_OUTST,SW_LVECD,SW_STSFL,SW_LUSBY,SW_LUPDT,SW_TRNFL) values (";
					M_strSQLQRY="insert into HR_SWMST(SW_CMPCD,SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_CURSH,SW_WRKSH,SW_INCST,SW_OUTST,SW_LVECD,SW_STSFL,SW_LUSBY,SW_LUPDT,SW_TRNFL) values (";
					M_strSQLQRY+= setINSSTR("SW_CMPCD",cl_dat.M_strCMPCD_pbst,"C");
					M_strSQLQRY+= setINSSTR("SW_SBSCD",M_strSBSCD,"C");
					M_strSQLQRY+= setINSSTR("SW_EMPNO",LP_RSSET.getString("SS_EMPNO"),"C");
					M_strSQLQRY+= setINSSTR("SW_WRKDT",L_strWRKDT,"D");
					//M_strSQLQRY+= setINSSTR("SW_ORGSH",LP_RSSET.getString("SS_ORGSH"),"C");
					M_strSQLQRY+= setINSSTR("SW_CURSH",LP_RSSET.getString("SS_CURSH"),"C");
					M_strSQLQRY+= setINSSTR("SW_WRKSH",LP_RSSET.getString("SS_CURSH"),"C");
					M_strSQLQRY+= setINSSTR("SW_INCST",L_strINCST,"C");
					M_strSQLQRY+= setINSSTR("SW_OUTST",L_strOUTST,"C");
					if(nvlSTRVL(LP_RSSET.getString("SS_LVECD"),"").equals("PH"))
						M_strSQLQRY+= setINSSTR("SW_LVECD",LP_RSSET.getString("SS_LVECD"),"C");
					else
						M_strSQLQRY+= setINSSTR("SW_LVECD","","C");
					M_strSQLQRY+= setINSSTR("SW_STSFL","0","C");
					M_strSQLQRY+= setINSSTR("SW_LUSBY",cl_dat.M_strUSRCD_pbst,"C");
					M_strSQLQRY+= setINSSTR("SW_LUPDT",strCURDT,"D");
					M_strSQLQRY+= "'0')";
				}
				if(flgCHK_EXIST)
				{
					M_strSQLQRY="update HR_SWMST set ";
					M_strSQLQRY+="SW_CURSH='"+LP_RSSET.getString("SS_CURSH")+"'";
					//M_strSQLQRY+="SW_ORGSH='"+LP_RSSET.getString("SS_ORGSH")+"'";
					if(!hstLVDTL.containsKey(L_strEMPNO+L_strWRKDT) &&  nvlSTRVL(LP_RSSET.getString("SS_LVECD"),"").equals("PH"))
						M_strSQLQRY+=",SW_LVECD='"+LP_RSSET.getString("SS_LVECD")+"' ";
					M_strSQLQRY+=" where "+L_strWHRSTR_SW;
					//M_strSQLQRY+="SW_INCST='"+L_strINCST+"',";
					//M_strSQLQRY+="SW_OUTST='"+L_strOUTST+"' where "+L_strWHRSTR_SW;
				}	
				//System.out.println("CHK_EXIST : exeSWMUPD_SS "+M_strSQLQRY);			
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeDBCMT("exeSWMUPD_SS");

				//M_strSQLQRY="update HR_SSTRN set SS_STSFL='2' where "+L_strWHRSTR_SS;
				////System.out.println(M_strSQLQRY);
				//cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				//cl_dat.exeDBCMT("exeSWMUPD_SS");
				
				if(!LP_RSSET.next())
				   break;
			}
			if(M_rstRSSET==null)
				M_rstRSSET.close();
		}
		catch(Exception E)
		{
			setMSG(E,"exeSWMUPD_SS");
		}
	}


	
				

	/** updates leave code in HR_SWMST if Employee has any leave in HR_LVTRN on that day
	 */
	public void exeSWMUPD_LV(ResultSet LP_RSSET)
	{
		try
		{
			String L_strEMPNO="",L_strWHRSTR_SW="",L_strWRKDT="";
			
			if(LP_RSSET==null || !LP_RSSET.next())
				return;
			while(true)	
			{
				cl_dat.M_flgLCUPD_pbst = true;
				L_strEMPNO = LP_RSSET.getString("LVT_EMPNO");
				L_strWRKDT = M_fmtLCDAT.format(LP_RSSET.getDate("LVT_LVEDT"));
				//System.out.println(L_strEMPNO+ " - "+ L_strWRKDT);
				L_strWHRSTR_SW = " SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO = '"+L_strEMPNO+"' and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'"; // and SW_STSFL not in ('9','X')";
				
				//i.e. if leave is cancelled later
				if(LP_RSSET.getString("LVT_STSFL").equals("X"))
				{
					M_strSQLQRY="update HR_SWMST set ";
					M_strSQLQRY+="SW_LVECD='',SW_LVEQT=0";
					M_strSQLQRY+=" where "+L_strWHRSTR_SW;
				}				   
				else if(LP_RSSET.getString("LVT_LVECD") != null)
				{	
					M_strSQLQRY="update HR_SWMST set ";
					M_strSQLQRY+="SW_LVECD='"+LP_RSSET.getString("LVT_LVECD")+"',";
					M_strSQLQRY+="SW_LVEQT='"+LP_RSSET.getString("LVT_LVEQT")+"'";
					M_strSQLQRY+=" where "+L_strWHRSTR_SW;
				}	
				//System.out.println(M_strSQLQRY);			
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeDBCMT("exeSWMUPD_LV");				
				if(!LP_RSSET.next())
				   break;
			}
			LP_RSSET.close();
		}
		catch(Exception E)
		{
			setMSG(E,"exeSWMUPD_LV");			
		}
	}
	
	
	
	/**
	 */
	public void exeSWMUPD_AL(ResultSet LP_RSSET,String LP_INOUT)
	{
		try
		{
			if(LP_RSSET==null || !LP_RSSET.next())
				return;
			String  L_strEMPNO,  L_strWHRSTR_SW,  L_strPNCTM, L_strXXXTM;
			while(true)	
			{
				cl_dat.M_flgLCUPD_pbst = true;
				L_strEMPNO = LP_RSSET.getString("SWT_EMPNO");
                //System.out.println("Updating Shift Schedule : "+L_strEMPNO);
				//setMSG("Updating Shift Schedule : "+L_strEMPNO,'N');
				String L_strINOCD = LP_INOUT.equalsIgnoreCase("IN") ? "0" : "1";
				String L_strWRKDT = M_fmtLCDAT.format(LP_RSSET.getDate("SWT_WRKDT"));
				//String L_strSRLNO = LP_RSSET.getString("SWT_SRLNO");
				//if(!txtWRKDT.getText().equals(L_strWRKDT))
				//	{if(!LP_RSSET.next())  break; else continue;}
				
				L_strWHRSTR_SW = " SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO = '"+L_strEMPNO+"' and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'";
				//System.out.println(L_strWHRSTR);
				//L_strWHRSTR_AL = " SWT_SBSCD = '"+M_strSBSCD+"' and SWT_EMPNO = '"+L_strEMPNO+"' and SWT_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"' and SWT_SRLNO = '"+L_strSRLNO+"'";
				flgCHK_EXIST = chkEXIST("HR_SWMST", L_strWHRSTR_SW);
				L_strXXXTM = LP_INOUT.equals("IN") ? "INCTM" : "OUTTM";

				if(!flgCHK_EXIST)
				{	
					M_strSQLQRY="insert into HR_SWMST(SW_CMPCD,SW_SBSCD,SW_EMPNO,SW_WRKDT,SW_"+L_strXXXTM+",SW_STSFL,SW_TRNFL,SW_LUSBY,SW_LUPDT) values(";
					M_strSQLQRY+="'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY+="'"+M_strSBSCD+"',";
					M_strSQLQRY+="'"+L_strEMPNO+"',";
					M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"',";
					M_strSQLQRY+="'"+M_fmtDBDTM.format(LP_RSSET.getTimestamp("SWT_PNCTM"))+".0000',";
					M_strSQLQRY+="'0',";
					M_strSQLQRY+="'0',";
					M_strSQLQRY+="'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY+="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"')";
					//System.out.println(M_strSQLQRY);			
				}
				if(flgCHK_EXIST)
				{
					M_strSQLQRY="update HR_SWMST set ";
					M_strSQLQRY+="SW_"+L_strXXXTM+" = '"+M_fmtDBDTM.format(LP_RSSET.getTimestamp("SWT_PNCTM"))+".0000' where SW_"+L_strXXXTM+" is null and SW_STSFL <> '9' and "+L_strWHRSTR_SW;
					////System.out.println(M_strSQLQRY);			
				}	
				//System.out.println("CHK_EXIST1 :exeSWMUPD_AL "+M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				cl_dat.exeDBCMT("exeSWMUPD_AL");
				
				//M_strSQLQRY="update HR_EPALG set SS_STSFL='2' where "+L_strWHRSTR_AL;
				////System.out.println(M_strSQLQRY);
				//cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				//cl_dat.exeDBCMT("exeSWMUPD_AL");
				
				if(!LP_RSSET.next())
				   break;
			}
			if(M_rstRSSET==null)
				M_rstRSSET.close();
		}
		catch(Exception E)
		{
			setMSG(E,"exeSWMUPD_AL");			
		}
	}


/**
 */
	void insRECRD(int P_intROWNO)
	{
		try{
			
						
			}catch(Exception L_EX)
			 {
				setMSG(L_EX,"This is insRECRD()");
			 }
	}
	
/**
 */
	void updQUERY(int P_intROWNO)
	{
		try{
		
			}
			catch(Exception L_EX)
			 {
				setMSG(L_EX,"This is updQUERY");
			 }	
	}	
	

	
	
	/**
	 */
	void updWRKHR(ResultSet LP_RSSET)
	{
		try
		{
			if(LP_RSSET != null)
			{
				while(LP_RSSET.next())
						updWRKHR1(LP_RSSET);
				LP_RSSET.close();
			}

		}
			catch(Exception L_EX)
			{
			    setMSG(L_EX,"updWRKHR");
			}
	}

	

/**
 */
	void setSTDTM(ResultSet LP_RSSET)
	{
		try
		{
			while(LP_RSSET.next())
			{
				setWRKDTX(M_fmtLCDAT.format(LP_RSSET.getDate("SW_WRKDT")));
				String L_strINCST = getSHFTM_ST(LP_RSSET.getString("SW_WRKSH"),"IN");
				String L_strOUTST = getSHFTM_ST(LP_RSSET.getString("SW_WRKSH"),"OUT");
				M_strSQLQRY = "update HR_SWMST set SW_INCST = '"+L_strINCST+"' ,  SW_OUTST = '"+L_strOUTST+"'  where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and SW_EMPNO = '"+LP_RSSET.getString("SW_EMPNO")+"' and SW_WRKDT = '"+M_fmtDBDAT.format(LP_RSSET.getDate("SW_WRKDT"))+"'";
				//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				//cl_dat.M_flgLCUPD_pbst = true;
			}
			if(LP_RSSET!=null)
				LP_RSSET.close();
			cl_dat.exeDBCMT("setSTDTM");
	
		}
			catch(Exception L_EX)
			{
			    setMSG(L_EX,"setSTDTM");
			}
	}
	
	
/**
 */
	void updWRKHR1(ResultSet LP_RSSET)
	{
		try
		{
			String L_strSHRWK1="00:00", L_strSHRWK2="00:00", L_strEXTWK1="00:00", L_strEXTWK2="00:00", L_strACTWK="00:00";
			//setMSG("Updating Working Hours : "+LP_RSSET.getString("SW_EMPNO"),'N');
			if(LP_RSSET.getTimestamp("SW_INCTM") != null  && LP_RSSET.getTimestamp("SW_INCST") != null)
			{
				if(LP_RSSET.getTimestamp("SW_INCTM").compareTo(LP_RSSET.getTimestamp("SW_INCST"))>0)
  					L_strSHRWK1 = calTIME(M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_INCTM")),M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_INCST")));
				else
  					L_strEXTWK1 = calTIME(M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_INCST")),M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_INCTM")));
			}
			if(LP_RSSET.getTimestamp("SW_OUTTM") != null  && LP_RSSET.getTimestamp("SW_OUTST") != null)
			{
				if(LP_RSSET.getTimestamp("SW_OUTTM").compareTo(LP_RSSET.getTimestamp("SW_OUTST"))>0)
  					L_strEXTWK2 = calTIME(M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_OUTTM")),M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_OUTST")));
				else
  					L_strSHRWK2 = calTIME(M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_OUTST")),M_fmtLCDTM.format(LP_RSSET.getTimestamp("SW_OUTTM")));
			}
			if(LP_RSSET.getTimestamp("SW_INCTM") != null  && LP_RSSET.getTimestamp("SW_INCST") != null && LP_RSSET.getTimestamp("SW_OUTTM") != null  && LP_RSSET.getTimestamp("SW_OUTST") != null)
			{
				String L_strSHRWK = addTIME(L_strSHRWK1,L_strSHRWK2);
				String L_strEXTWK = addTIME(L_strEXTWK1,L_strEXTWK2);
				String L_strSHFTM = LP_RSSET.getString("SW_WRKSH").equals("G") ? "08:30" : "08:00";
				L_strACTWK = subTIME(addTIME(L_strSHFTM,addTIME(L_strEXTWK1,L_strEXTWK2)),addTIME(L_strSHRWK1,L_strSHRWK2));
				L_strACTWK = L_strACTWK.substring(0,1).equals("-") ? "00:00" : L_strACTWK;
				L_strACTWK = Integer.parseInt(L_strACTWK.substring(0,2))>=24 ? "23:59" : L_strACTWK;
				L_strACTWK = L_strACTWK.substring(3,4).equals("-") ? "00:00" : L_strACTWK;
				String L_strOVTWK = subTIME(addTIME(L_strEXTWK1,L_strEXTWK2),addTIME(L_strSHRWK1,L_strSHRWK2));
				//L_strOVTWK = ((Integer.parseInt(L_strOVTWK.substring(0,2))>=1 && LP_RSSET.getString("EP_EMPCT").equalsIgnoreCase("T")) ? L_strOVTWK : "00:00");
				if((!LP_RSSET.getString("EP_EMPCT").equalsIgnoreCase("OFF") && !LP_RSSET.getString("EP_EMPCT").equalsIgnoreCase("RTO")) || LP_RSSET.getString("EP_EMPCT").equalsIgnoreCase("W"))
					L_strOVTWK = fmtHHMM.parse(L_strOVTWK).compareTo(fmtHHMM.parse("00:29"))>0  ? L_strOVTWK : "00:00";
				else if(LP_RSSET.getString("EP_EMPCT").equalsIgnoreCase("O"))
					L_strOVTWK = fmtHHMM.parse(L_strOVTWK).compareTo(fmtHHMM.parse("05:59"))>0  ? L_strOVTWK : "00:00";
				//M_strSQLQRY = "update HR_SWMST set SW_SHRWK = '"+L_strSHRWK+"' ,  SW_EXTWK = '"+L_strEXTWK+"' ,  SW_ACTWK = '"+L_strACTWK+"', SW_OVTWK = '"+L_strOVTWK+"' where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO = '"+LP_RSSET.getString("SW_EMPNO")+"' and SW_WRKDT = '"+M_fmtDBDAT.format(LP_RSSET.getDate("SW_WRKDT"))+"'";
				M_strSQLQRY = "update HR_SWMST set SW_EXTWK = '"+L_strEXTWK+"' ,  SW_ACTWK = '"+L_strACTWK+"', SW_OVTWK = '"+L_strOVTWK+"' where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO = '"+LP_RSSET.getString("SW_EMPNO")+"' and SW_WRKDT = '"+M_fmtDBDAT.format(LP_RSSET.getDate("SW_WRKDT"))+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeDBCMT("updWRKHR1");
			}
		}
			catch(Exception L_EX)
			{
			    setMSG(L_EX,"updWRKHR1");
			}
	}

	/**
	 */
	void setWRKHR()
	{
		try
		{
			for(int i=0;i<tblTEEXR.getRowCount();i++)
			{
					if(tblTEEXR.getValueAt(i,TB_EMPNO).toString().length()!=4)
						break;
					setWRKHR1(i);
			}

		}
			catch(Exception L_EX)
			{
			    setMSG(L_EX,"setWRKHR");
			}
	}

	
	void setWRKHR1(int LP_ROWID)
	{
		try
		{
			tblTEEXR.setValueAt(new Boolean(true),LP_ROWID,TB_CHKFL);
			String L_strSHRWK1="00:00", L_strSHRWK2="00:00", L_strEXTWK1="00:00", L_strEXTWK2="00:00", L_strACTWK="00:00";
			if(tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_A).toString().length()>5 && tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_S).toString().length()>5)
			{
				if(M_fmtLCDTM.parse(tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_A).toString()).compareTo(M_fmtLCDTM.parse(tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_S).toString()))>0)
  					L_strSHRWK1 = calTIME(tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_A).toString(),tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_S).toString());
				else
  					L_strEXTWK1 = calTIME(tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_S).toString(),tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_A).toString());
			}
			if(tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_A).toString().length()>5 && tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_S).toString().length()>5)
			{
				if(M_fmtLCDTM.parse(tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_A).toString()).compareTo(M_fmtLCDTM.parse(tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_S).toString()))>0)
  					L_strEXTWK2 = calTIME(tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_A).toString(),tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_S).toString());
				else
  					L_strSHRWK2 = calTIME(tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_S).toString(),tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_A).toString());
			}
			if(tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_A).toString().length()>5 && tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_A).toString().length()>5 && tblTEEXR.getValueAt(LP_ROWID,TB_OUTTM_S).toString().length()>5 && tblTEEXR.getValueAt(LP_ROWID,TB_INCTM_S).toString().length()>5)
			{
				tblTEEXR.setValueAt(addTIME(L_strSHRWK1,L_strSHRWK2),LP_ROWID,TB_SHRWK);
				tblTEEXR.setValueAt(addTIME(L_strEXTWK1,L_strEXTWK2),LP_ROWID,TB_EXTWK);
				String L_strSHFTM = tblTEEXR.getValueAt(LP_ROWID,TB_WRKSH).toString().equals("G") ? "08:30" : "08:00";
				L_strACTWK = subTIME(addTIME(L_strSHFTM,addTIME(L_strEXTWK1,L_strEXTWK2)),addTIME(L_strSHRWK1,L_strSHRWK2));
				L_strACTWK = L_strACTWK.substring(0,1).equals("-") ? "00:00" : L_strACTWK;
				L_strACTWK = Integer.parseInt(L_strACTWK.substring(0,2))>=24 ? "23:59" : L_strACTWK;
				L_strACTWK = L_strACTWK.substring(3,4).equals("-") ? "00:00" : L_strACTWK;

				tblTEEXR.setValueAt(L_strACTWK,LP_ROWID,TB_ACTWK);
				String L_strOVTWK = subTIME(addTIME(L_strEXTWK1,L_strEXTWK2),addTIME(L_strSHRWK1,L_strSHRWK2));
				//System.out.println("L_strOVTWK.substring(0,2)>>"+L_strOVTWK.substring(0,2));
				tblTEEXR.setValueAt(((Integer.parseInt(L_strOVTWK.substring(0,2))>=1 && (!tblTEEXR.getValueAt(LP_ROWID,TB_EMPCT_SW).toString().equalsIgnoreCase("OFF") && !tblTEEXR.getValueAt(LP_ROWID,TB_EMPCT_SW).toString().equalsIgnoreCase("OFF"))) ? L_strOVTWK : "00:00"),LP_ROWID,TB_OVTWK);
			}

		}
			catch(Exception L_EX)
			{
			    setMSG(L_EX,"setWRKHR1");
			}
	}
	
	
/**
 * Saving records in HR_SWMST from Entry Table
 */	
	void exeSAVE()
	{
		int P_intROWNO=0;
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
				//if(tbpMAIN.getSelectedItem().toString().equals("Exceptions Authorization"))
				if(tbpMAIN.getTitleAt(tbpMAIN.getSelectedIndex()).toString().equals("Exceptions Authorization"))
			    {
					for(P_intROWNO=0;P_intROWNO<tblTEEXR.getRowCount();P_intROWNO++)
					{
						if(tblTEEXR.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
						{
							if(!vldTEEXR(P_intROWNO))
							{
								return;
							}
							
											
						}
					}
					
					for(P_intROWNO=0;P_intROWNO<tblTEEXR.getRowCount();P_intROWNO++)
					{
						if(tblTEEXR.getValueAt(P_intROWNO,TB_CHKFL).toString().equals("true"))
						{
							exeMODREC(P_intROWNO);
							String L_strEMPNO= tblTEEXR.getValueAt(P_intROWNO,TB_EMPNO).toString();
							String L_strEMPCT= tblTEEXR.getValueAt(P_intROWNO,TB_EMPCT_SW).toString();
							String L_strDPTCD= txtDPTCD.getText();
							String L_strSTRDT= tblTEEXR.getValueAt(P_intROWNO,TB_WRKDT).toString();
							String L_strENDDT= tblTEEXR.getValueAt(P_intROWNO,TB_WRKDT).toString();
							prcLDGR(L_strEMPNO,L_strEMPCT,L_strDPTCD,L_strSTRDT,L_strENDDT);
						}
					}
				}
				//else if(tbpMAIN.getSelectedItem().toString().equals("Over Time Authorization"))
				else if(tbpMAIN.getTitleAt(tbpMAIN.getSelectedIndex()).toString().equals("Over Time Authorization"))
			    {
					for(P_intROWNO=0;P_intROWNO<tblOVTDT.getRowCount();P_intROWNO++)
					{
						if(tblOVTDT.getValueAt(P_intROWNO,TB_CHKFL_OT).toString().equals("true"))
						{
							if(!vldOVTDT(P_intROWNO))
							{
								return;
							}
						}
					}
					if(!vldOVTDT1())
					{
						return;
					}
					String strOVTHR="00:00";
					for(P_intROWNO=0;P_intROWNO<tblOVTDT.getRowCount();P_intROWNO++)
					{
						if(tblOVTDT.getValueAt(P_intROWNO,TB_CHKFL_OT).toString().equals("true"))
						{
							inlTBLEDIT(tblOVTDT);
							if(tblOVTDT.getValueAt(P_intROWNO,TB_AUTHC_OT).toString().equals("true"))
							{
								if(tblOVTDT.getValueAt(P_intROWNO,TB_AUTHC_OT).toString().equals("true"))
									strOVTHR = addTIME(strOVTHR,tblOVTDT.getValueAt(P_intROWNO,TB_OVTHR_OT).toString());
									
								if(!(tblOVTDT.getValueAt(P_intROWNO,TB_EMPNO_OT).toString().equals(tblOVTDT.getValueAt(P_intROWNO+1,TB_EMPNO_OT).toString())
								  && tblOVTDT.getValueAt(P_intROWNO,TB_WRKDT_OT).toString().equals(tblOVTDT.getValueAt(P_intROWNO+1,TB_WRKDT_OT).toString())))
								{	
									M_strSQLQRY = " update HR_SWMST set ";
									M_strSQLQRY+= " SW_OVTHR = '"+strOVTHR+"',";
									M_strSQLQRY+= " sw_ovtfl = 'O'";
									M_strSQLQRY+= " where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO = '"+tblOVTDT.getValueAt(P_intROWNO,TB_EMPNO_OT).toString()+"' and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblOVTDT.getValueAt(P_intROWNO,TB_WRKDT_OT).toString()))+"' ";
									cl_dat.M_flgLCUPD_pbst = true;
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
									strOVTHR="00:00";
								}
							}
							
							String L_strSTSFL = "";
							if(tblOVTDT.getValueAt(P_intROWNO,TB_AUTHC_OT).toString().equals("true"))
								L_strSTSFL = "1";
							else if(tblOVTDT.getValueAt(P_intROWNO,TB_REJCT_OT).toString().equals("true"))
								L_strSTSFL+= "X";
							else L_strSTSFL+= "0";
							String L_strEMPNO = tblOVTDT.getValueAt(P_intROWNO,TB_EMPNO_OT).toString();
							String L_strWRKDT = tblOVTDT.getValueAt(P_intROWNO,TB_WRKDT_OT).toString();
							String L_strINCTM = tblOVTDT.getValueAt(P_intROWNO,TB_INCTM_OT).toString();
							String L_strOUTTM = tblOVTDT.getValueAt(P_intROWNO,TB_OUTTM_OT).toString();
							String L_strMNLFL ="";
							if(tblOVTDT.getValueAt(P_intROWNO,TB_MNLFL_OT).toString().equals("true"))
								L_strMNLFL = "Y";
							setOTTRN(L_strEMPNO,L_strWRKDT,L_strINCTM,L_strOUTTM,L_strSTSFL,L_strMNLFL);
						}
					}
				}
				else if(tbpMAIN.getTitleAt(tbpMAIN.getSelectedIndex()).toString().equals("Exit Pass Authorization"))
			    {
					if(!vldDATA_EP()) 
					{
						return;
					}
					if(!vldEXTPS())/**validation to Authorize  **/
					{
						return;
					}
					for(P_intROWNO=0;P_intROWNO<tblEPSDT.getRowCount();P_intROWNO++)
					{
						if(tblEPSDT.getValueAt(P_intROWNO,TB_CHKFL_EP).toString().equals("true"))
						{
							exeAUTREC_EP(P_intROWNO); //update record
						}
					}
					hstAUTREC.clear();
					for(P_intROWNO=0;P_intROWNO<tblEPSDT.getRowCount();P_intROWNO++)
					{
						if(tblEPSDT.getValueAt(P_intROWNO,TB_CHKFL_EP).toString().equals("true"))
						{
							exeAUTREC_SWM(P_intROWNO); //update record
							hstAUTREC.put(tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString())),"");
						}
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				tblTEEXR.clrTABLE();
				tblTEEXR1.clrTABLE();
				tblOVTDT.clrTABLE();
				tblEPSDT.clrTABLE();
				tblAUEPS.clrTABLE();
				tblOBJCD.clrTABLE();
				//clrCOMP();
				setMSG("record updated successfully",'N');
			}
			else
			{
			   	JOptionPane.showMessageDialog(this,"Error in modifying data ","Error",JOptionPane.INFORMATION_MESSAGE);
			    setMSG("Error in updating data..",'E');
			}
		}
		catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is exeSave");
		}
	}
	
	
	/** Method to insert record in to HR_COTRN
	 *  record with more than 5 hrs and with leave code as PH,CO,WO.
	 *  */
	private boolean insCOTRN(int P_intROWNO,String LP_LVECD)
	{
	  try
	  {
		  String strSQLQRY="";
		  
			String L_strWHRSTR_CO = " CO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and CO_EMPNO = '"+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNO).toString()+"' and CO_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEEXR.getValueAt(P_intROWNO,TB_WRKDT).toString()))+"' and CO_LVECD = '"+LP_LVECD+"'";
			flgCHK_EXIST = chkEXIST("HR_COTRN", L_strWHRSTR_CO);
			//System.out.println("L_strWHRSTR_CO>>"+L_strWHRSTR_CO);
			if(!flgCHK_EXIST)
			{
				strSQLQRY = " Insert into HR_COTRN(CO_CMPCD,CO_EMPNO,CO_WRKDT,CO_SHFCD,CO_LVECD,CO_WRKBY,CO_STSFL,CO_TRNFL,CO_LUSBY,CO_LUPDT,CO_SBSCD) values(";
				strSQLQRY +=" '"+cl_dat.M_strCMPCD_pbst+"',";
				strSQLQRY +=" '"+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNO).toString()+"',";
				strSQLQRY +=" '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEEXR.getValueAt(P_intROWNO,TB_WRKDT).toString()))+"',";
				strSQLQRY +=" '"+tblTEEXR.getValueAt(P_intROWNO,TB_WRKSH).toString()+"',";
				strSQLQRY +=" '"+LP_LVECD+"',";
				strSQLQRY +=" '"+cl_dat.M_strUSRCD_pbst+"',";
				strSQLQRY +="'W',";
				strSQLQRY +="'0',";
				strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
				strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
				strSQLQRY +="'"+M_strSBSCD+"')";
				//System.out.println("insCOTRN : "+strSQLQRY);
				cl_dat.exeSQLUPD(strSQLQRY,"insCOTRN");
			}
			/*else
			{
				strSQLQRY= "Select CO_WRKFL from HR_COTRN where "+L_strWHRSTR_CO;
				ResultSet rstRSSET1 = cl_dat.exeSQLQRY(strSQLQRY);
				if(rstRSSET1!=null && rstRSSET1.next())
				{
					if(rstRSSET1.getString("CO_WRKFL").equals("A"))
					{
						JOptionPane.showMessageDialog(null, "Day already made avail for Off-Change "+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNM).toString()+" so cannnot modify" , "Error Message", JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
			}*/
			//System.out.println("strSQLQRY>>"+strSQLQRY);
			return true;
			
	  }
	  catch(Exception L_EX)
      {
         setMSG(L_EX,"exeAUTREC()"); 
      }
	  return false;
	}

	//Auto-linking of off-change shifted to Off-Change Authorisation module
	//**********************************************************************

	/** Method to update record in to HR_COTRN
	 *  after availing Off change, Comp Off, RH against PH-Working etc.
	 *  */
	/*private boolean updCOTRN(int P_intROWNO)
	{
	  try
	  {
		  String strSQLQRY="";
		  
			String L_strWHRSTR_CO = " CO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and CO_EMPNO = '"+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNO).toString()+"' and CO_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEEXR.getValueAt(P_intROWNO,TB_REFDT).toString()))+"' and CO_LVECD = '"+tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString()+"'";
			flgCHK_EXIST = chkEXIST("HR_COTRN", L_strWHRSTR_CO);
			if(flgCHK_EXIST)
			{
				strSQLQRY = " update HR_COTRN set ";
				strSQLQRY +=" CO_STSFL = 'A',";
				strSQLQRY +=" CO_REFDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEEXR.getValueAt(P_intROWNO,TB_WRKDT).toString()))+"',";
				strSQLQRY +=" CO_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				strSQLQRY +=" CO_TRNFL = '0',";
				strSQLQRY +=" CO_REFBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				strSQLQRY +=" CO_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' where "+L_strWHRSTR_CO;
				//System.out.println("updCOTRN : "+strSQLQRY);
				cl_dat.exeSQLUPD(strSQLQRY,"insCOTRN");
			}
			return true;
			
	  }
	  catch(Exception L_EX)
      {
         setMSG(L_EX,"exeAUTREC()"); 
      }
	  return false;
	}*/
	
	
	/** Method to update hr_extrn record with status '3'to authorized & hr_swmst record with status 'Y'
	 *  to display only unAuthorized record 
	 *  */
	private void exeAUTREC_EP(int P_intROWNO)
	{
	  try
	  {
		    inlTBLEDIT(tblEPSDT);
			M_strSQLQRY =" Update HR_EXTRN set";
			M_strSQLQRY +=" EX_SHFCD='"+tblEPSDT.getValueAt(P_intROWNO,TB_WRKSH_EP).toString()+"',";
			M_strSQLQRY +=" EX_OUTTM='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEPSDT.getValueAt(P_intROWNO,TB_OUTTM_EP).toString()))+"',";
			if(tblEPSDT.getValueAt(P_intROWNO,TB_INCTM_EP).toString().length()>0)
				M_strSQLQRY +=" EX_INCTM='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEPSDT.getValueAt(P_intROWNO,TB_INCTM_EP).toString()))+"',";
			//if(tblEPSDT.getValueAt(P_intROWNO,TB_OTGFL_EP).toString().equals("true"))
			//{
			//	M_strSQLQRY +=" EX_OTGFL = 'Y',";	//STSFL
				//M_strSQLQRY +=" EX_AOTTM='00:00',";
			//}
			//else
			M_strSQLQRY +=" EX_PEDTM='"+tblEPSDT.getValueAt(P_intROWNO,TB_PEDTM_EP).toString()+"',";
			M_strSQLQRY +=" EX_PEATM='"+tblEPSDT.getValueAt(P_intROWNO,TB_PEATM_EP).toString()+"',";
			M_strSQLQRY +=" EX_OEXTM='"+tblEPSDT.getValueAt(P_intROWNO,TB_OEXTM_EP).toString()+"',";
			M_strSQLQRY +=" EX_STSFL = '3',";	//STSFL
			M_strSQLQRY +=" EX_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY +=" EX_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY +=" where EX_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' and EX_EMPNO= '"+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+"'";
			M_strSQLQRY +=" AND EX_DOCDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()))+"'";
			M_strSQLQRY +=" AND EX_DOCNO= '"+tblEPSDT.getValueAt(P_intROWNO,TB_DOCNO_EP).toString()+"'";
			////System.out.println(">>>UPDATE>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			
			//M_strSQLQRY =" Update HR_SWMST set";
			//M_strSQLQRY +=" SW_EPCFL = 'Y'";
			//M_strSQLQRY +=" where SW_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO= '"+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+"'";
			//M_strSQLQRY +=" AND SW_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()))+"'";
			//System.out.println(">>>UPDATE>>"+M_strSQLQRY);
			//cl_dat.M_flgLCUPD_pbst = true;
			//cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	  }
	  catch(Exception L_EX)
      {
         setMSG(L_EX,"exeAUTREC()"); 
      }
	}
	

	
	private void exeAUTREC_SWM(int P_intROWNO)
	{
	  try
	  {
			if(hstAUTREC.containsKey(tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()))))
				  return;
		    inlTBLEDIT(tblEPSDT);
			M_strSQLQRY ="select EX_INCTM,EX_OUTTM,SW_INCTM,SW_OUTTM,EX_PEDTM,EX_PEATM,EX_OEXTM,EX_OFPFL from HR_EXTRN,HR_SWMST where EX_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' and EX_EMPNO= '"+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+"' ";
			M_strSQLQRY +=" AND EX_DOCDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()))+"' and EX_STSFL='3' and EX_CMPCD = SW_CMPCD and EX_EMPNO=SW_EMPNO and EX_DOCDT = SW_WRKDT order by EX_OUTTM";

			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET == null || !L_rstRSSET.next())
				return;
			String L_strPEDTM="00:00";
			String L_strPEATM="00:00";
			String L_strOEXTM="00:00";
			while(true)
			{
				if(L_rstRSSET.getTimestamp("SW_INCTM")==null || L_rstRSSET.getTimestamp("SW_OUTTM") == null)
					{if(!L_rstRSSET.next()) break; else continue;}
				String L_strINCTM_EX=M_fmtLCDTM.format(L_rstRSSET.getTimestamp("EX_INCTM"));
				String L_strOUTTM_EX=M_fmtLCDTM.format(L_rstRSSET.getTimestamp("EX_OUTTM"));
				String L_strINCTM_SW=M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_INCTM"));
				String L_strOUTTM_SW=M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_OUTTM"));
				String L_strPEDTM_EX = fmtHHMM.format(L_rstRSSET.getTime("EX_PEDTM"));
				String L_strPEATM_EX = fmtHHMM.format(L_rstRSSET.getTime("EX_PEATM"));
				String L_strOEXTM_EX = fmtHHMM.format(L_rstRSSET.getTime("EX_OEXTM"));
				Date L_datINCTM_SW=M_fmtLCDTM.parse(L_strINCTM_SW);
				Date L_datOUTTM_SW=M_fmtLCDTM.parse(L_strOUTTM_SW);
				Date L_datINCTM_EX=M_fmtLCDTM.parse(L_strINCTM_EX);
				Date L_datOUTTM_EX=M_fmtLCDTM.parse(L_strOUTTM_EX);
				// Exit pass within duty hours
				// Exit-OUT after DUTY-IN time                   Exit-IN before DUTY-OUT time
				if(L_datOUTTM_EX.compareTo(L_datINCTM_SW)>0 && L_datINCTM_EX.compareTo(L_datOUTTM_SW)<0 && L_rstRSSET.getString("EX_OFPFL").equals("P"))
					L_strPEDTM = addTIME(L_strPEDTM,L_strPEDTM_EX);
				L_strPEATM = addTIME(L_strPEATM,L_strPEATM_EX);
				L_strOEXTM = addTIME(L_strOEXTM,L_strOEXTM_EX);
				if(!L_rstRSSET.next())
					 break;
			}
			L_rstRSSET.close();
			
			M_strSQLQRY = "update HR_SWMST set SW_PEDTM = '"+L_strPEDTM+"', SW_PEATM = '"+L_strPEATM+"', SW_OEXTM = '"+L_strOEXTM+"' where  SW_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO= '"+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+"' ";
			M_strSQLQRY +=" AND SW_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()))+"'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.exeDBCMT("exeAUTREC_SWM");
			M_strSQLQRY = "select SW_EMPNO,SW_WRKDT,SW_INCTM,SW_OUTTM,SW_INCST,SW_OUTST,EP_EMPCT, isnull(SW_PEDTM,'00:00') SW_PEDTM from HR_SWMST,HR_EPMST where SW_EMPNO = EP_EMPNO and SW_CMPCD = EP_CMPCD ";
			M_strSQLQRY += " and SW_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO= '"+tblEPSDT.getValueAt(P_intROWNO,TB_EMPNO_EP).toString()+"' ";
			M_strSQLQRY +=" AND SW_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEPSDT.getValueAt(P_intROWNO,TB_WRKDT_EP).toString()))+"' ";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET == null || !L_rstRSSET.next())
				return;
			prcLDGR1(L_rstRSSET.getString("SW_EMPNO"),L_rstRSSET.getString("EP_EMPCT"),M_fmtLCDAT.format(L_rstRSSET.getTimestamp("SW_WRKDT")),M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_INCTM")),M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_OUTTM")),M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_INCST")),M_fmtLCDTM.format(L_rstRSSET.getTimestamp("SW_OUTST")),fmtHHMM.format(L_rstRSSET.getTime("SW_PEDTM")));
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
			
	  }
	  catch(Exception L_EX)
      {
         setMSG(L_EX,"exeAUTREC_SWM()"); 
      }
	}

	
	
	
	private boolean vldTEEXR(int P_intROWNO)
	{
		//if(tblTEEXR.getValueAt(P_intROWNO,TB_INCTM_A).toString().length()==0)	
		try
		{
		   if(tblTEEXR.getValueAt(P_intROWNO,TB_INCTM_A).toString().length()<16 && tblTEEXR.getValueAt(P_intROWNO,TB_OUTTM_A).toString().length()<16 && tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().length()==2)
				return true;   
		   
		   if(tblTEEXR.getValueAt(P_intROWNO,TB_INCTM_A).toString().trim().length()==0 && tblTEEXR.getValueAt(P_intROWNO,TB_OUTTM_A).toString().length()==0)
			   return true; 
		   if(tblTEEXR.getValueAt(P_intROWNO,TB_INCTM_A).toString().length()<16)
		   {
				setMSG("Please Enter In Time in the Table for "+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNM).toString(),'E');
				return false;
		   }
		   if(tblTEEXR.getValueAt(P_intROWNO,TB_OUTTM_A).toString().length()<16)
		   {
				setMSG("Please Enter Out Time in the Table for "+tblTEEXR.getValueAt(P_intROWNO,TB_EMPNM).toString(),'E');
				return false;
		   }		   
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTEEXR()");
		}
		return true;			
	}	
	
	private void exeMODREC(int P_intROWNO)
	{ 
	  int i;
	  try
	  {
			i=P_intROWNO;	  
			inlTBLEDIT(tblTEEXR);
			String L_strSTSFL = "";
			if(tblTEEXR.getValueAt(i,TB_ACTWK).toString().length()!=0)
				if((tblTEEXR.getValueAt(i,TB_WRKSH).toString().trim().length()==1 && Integer.parseInt(tblTEEXR.getValueAt(i,TB_ACTWK).toString().substring(0,2))>4) || tblTEEXR.getValueAt(i,TB_LVECD).toString().trim().length()>1)
					L_strSTSFL = "3";

			M_strSQLQRY = " update HR_SWMST set ";
			
			if(tblTEEXR.getValueAt(i,TB_ACTWK).toString().length()==0)
				M_strSQLQRY+= " SW_ACTWK = '00:00:00',";
			else 
				M_strSQLQRY+= " SW_ACTWK = '"+tblTEEXR.getValueAt(i,TB_ACTWK).toString()+"',";	
			
			//if(tblTEEXR.getValueAt(i,TB_SHRWK).toString().length()==0)
			//	M_strSQLQRY+= " SW_SHRWK = '00:00:00',";
			//else 
			//	M_strSQLQRY+= " SW_SHRWK = '"+tblTEEXR.getValueAt(i,TB_SHRWK).toString()+"',";
			
			if(tblTEEXR.getValueAt(i,TB_EXTWK).toString().length()==0)
				M_strSQLQRY+= " SW_EXTWK = '00:00:00',";
			else 
				M_strSQLQRY+= " SW_EXTWK = '"+tblTEEXR.getValueAt(i,TB_EXTWK).toString()+"',";
			
			if(tblTEEXR.getValueAt(i,TB_OVTWK).toString().length()==0)
				M_strSQLQRY+= " SW_OVTWK = '00:00:00',";
			else 
				M_strSQLQRY+= " SW_OVTWK = '"+tblTEEXR.getValueAt(i,TB_OVTWK).toString()+"',";
			
			M_strSQLQRY+= " SW_LVECD = '"+tblTEEXR.getValueAt(i,TB_LVECD).toString()+"',";
			M_strSQLQRY+= " SW_LVEQT = '"+tblTEEXR.getValueAt(i,TB_LVEQT).toString()+"',";
			
			if(tblTEEXR.getValueAt(i,TB_INCTM_A).toString().length()==0)
				M_strSQLQRY+= " SW_INCTM = null,";
			else 
				M_strSQLQRY+= " SW_INCTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblTEEXR.getValueAt(i,TB_INCTM_A).toString()))+"',";
	
			if(tblTEEXR.getValueAt(i,TB_OUTTM_A).toString().length()==0)
				M_strSQLQRY+= " SW_OUTTM = null,";
			else 
				M_strSQLQRY+= " SW_OUTTM = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblTEEXR.getValueAt(i,TB_OUTTM_A).toString()))+"',";
			
			if(tblTEEXR.getValueAt(i,TB_INCTM_S).toString().length()==0)
				M_strSQLQRY+= " SW_INCST = null,";
			else 
				M_strSQLQRY+= " SW_INCST = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblTEEXR.getValueAt(i,TB_INCTM_S).toString()))+"',";
			
			if(tblTEEXR.getValueAt(i,TB_OUTTM_S).toString().length()==0)
				M_strSQLQRY+= " SW_OUTST = null,";
			else 
				M_strSQLQRY+= " SW_OUTST = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblTEEXR.getValueAt(i,TB_OUTTM_S).toString()))+"',";
			
			M_strSQLQRY+= " SW_WRKSH = '"+tblTEEXR.getValueAt(i,TB_WRKSH).toString()+"',";
			if(tblTEEXR.getValueAt(i,TB_AUTHC).toString().equals("true"))
				M_strSQLQRY+= " SW_STSFL = '9',";
			else
				M_strSQLQRY+= " SW_STSFL = '2',";
			if(tblTEEXR.getValueAt(i,TB_OBJCD).toString().equals("true"))
				M_strSQLQRY+= " SW_OBJCD = '01'";
			else
				M_strSQLQRY+= " SW_OBJCD = '00'";
			M_strSQLQRY+= " where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and SW_EMPNO = '"+tblTEEXR.getValueAt(i,TB_EMPNO)+"' and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblTEEXR.getValueAt(i,TB_WRKDT).toString()))+"' ";
									   
			//System.out.println(">>Last update>>>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			//// inserts data into HR_COTRN if employee has working > 5.00 hrs and leave code is PH,CO,WO
			/// checks for whether times in table are available.
			// Employee working on Weekly off, will get off change (off on other day) - Generates separate record in HR_COTRN
			// Officer Employee working on PH. will get additional RH - which is separately recorded in HR_COTRN.
							
			//   Worked on Off day                                                                                                                           Leave taken on off day (with off change)
			if((tblTEEXR.getValueAt(P_intROWNO,TB_INCTM_A).toString().length()>0 && tblTEEXR.getValueAt(P_intROWNO,TB_OUTTM_A).toString().length()>0)
			   && (tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().equals("PH") || tblTEEXR.getValueAt(P_intROWNO,TB_CURSH).toString().equals("O")))
			{	// outtime - intime >= "05:00" Hrs.
				if(fmtHHMM.parse(tblTEEXR.getValueAt(P_intROWNO,TB_ACTWK).toString()).compareTo(fmtHHMM.parse("05:00"))>=0)
				{
					if((tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().equals("PH") && tblTEEXR.getValueAt(P_intROWNO,TB_EMPCT_SW).toString().equalsIgnoreCase("OFF")))
						 insCOTRN(P_intROWNO,"CR");
					if(tblTEEXR.getValueAt(P_intROWNO,TB_CURSH).toString().equals("O"))
						 insCOTRN(P_intROWNO,"OC");
				}
			}
			else if(tblTEEXR.getValueAt(P_intROWNO,TB_CURSH).toString().equals("O") && tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().length()==2 && !tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().equals("WO"))
				 insCOTRN(P_intROWNO,"OC");
			//Auto-linking of off-change shifted to Off-Change Authorisation module
			//**********************************************************************
			/*if((tblTEEXR.getValueAt(P_intROWNO,TB_INCTM_A).toString().length()==0 && tblTEEXR.getValueAt(P_intROWNO,TB_OUTTM_A).toString().length()==0)
			   && (tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().equals("OC") || tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().equals("CR") || tblTEEXR.getValueAt(P_intROWNO,TB_LVECD).toString().equals("CO")))
			{	// outtime - intime >= "05:00" Hrs.
					updCOTRN(P_intROWNO);
			}*/
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeMODREC");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeMODREC()"); 
      }
	}


/**
 */	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
						return true;
				
				if(input == txtDPTCD)
				{
					//M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and CMT_CODCD= '"+txtDPTCD.getText().toString().trim()+"' and cmt_codcd in ";
					//M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=US_EMPCD)";

					M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' and cmt_codcd = '"+txtDPTCD.getText().trim()+"' "+(flgALLDPT ? "" : " and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"') and EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"')");					
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblDPTNM.setText(M_rstRSSET.getString("CMT_SHRDS"));
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
				}	
				if(input == txtEMPNO3)
				{
					M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' '+ SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM from HR_EPMST where EP_EMPNO='"+txtEMPNO3.getText()+"' AND EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'" +(txtDPTCD.getText().length()>0 ? "AND EP_DPTCD = '"+txtDPTCD.getText()+"' ":"") +" and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblEMPNM3.setText(M_rstRSSET.getString("EP_EMPNM"));
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Employee No.",'E');
						return false;
					}
				}	
				if(input == txtEMPCT)
				{
        			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXEPC' and CMT_CODCD='"+txtEMPCT.getText().trim().toUpperCase()+"'"+ (flgALLDPT ? "" : " and ltrim(rtrim(cmt_codcd)) in (Select distinct ep_empct from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(flgALLDPT ? "" : " and EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"' and  EP_EMPCT= '"+txtEMPCT.getText().toString().trim()+"')"))+"  order by cmt_codcd";
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblEMPCT.setText(M_rstRSSET.getString("CMT_CODDS"));
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Employee Category",'E');
						return false;
					}
				}	
				if(input == txtSTRDT)
				{
					if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>=0)
					{
						setMSG("From date can not be greater than or equals Today's date..",'E');
						txtSTRDT.requestFocus();
						return false;
					}
					txtENDDT.setText(txtSTRDT.getText());
				}
				if(input == txtENDDT)
				{
					if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>=0)
					{
						setMSG("TO date can not be greater than or equals Today's date..",'E');
						return false;
					}
					if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
					{
						setMSG("Invalid Date Range..",'E');
						return false;
					}
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"Input Verifier");		
			}
			return true;
		}
	}	
	
	/** Checking key in table for record existance
	 */
	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
	{
		boolean L_flgCHKFL = false;
		try
		{
			M_strSQLQRY =	"select count(*) CNT from "+LP_TBLNM + " where "+ LP_WHRSTR;
			//System.out.println(" chkEXIST : "+M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				if(L_rstRSSET.getInt("CNT")>0)
					L_flgCHKFL = true;
				L_rstRSSET.close();
				//System.out.println(">>L_flgCHKFL>>"+L_flgCHKFL);
			}
		}
		catch (Exception L_EX)	
		{setMSG("Error in chkEXIST : "+L_EX,'E');}
		return L_flgCHKFL;
	}

	
	
	/** Generating string for Insertion Query
	 * @param	LP_FLDNM	Field name to be inserted
	 * @param	LP_FLDVL	Content / value of the field to be inserted
	 * @param	LP_FLDTP	Type of the field to be inserted
	 */
	private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
	try 
	{
		////System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
		if (LP_FLDTP.equals("C"))
			 //return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
	 		return  "'"+LP_FLDVL+"',";
		else if (LP_FLDTP.equals("N"))
	        // return nvlSTRVL(LP_FLDVL,"0") + ",";
			return  "'"+LP_FLDVL+"',";
	 	else if (LP_FLDTP.equals("D"))
			 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
		else if (LP_FLDTP.equals("T"))
			 return   (LP_FLDVL.trim().length()>=5) ? ("'"+LP_FLDVL.trim()+"',") : "null,";
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
		////System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
		if (LP_FLDTP.equals("C"))
			 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
	 	else if (LP_FLDTP.equals("N"))
	         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
	 	else if (LP_FLDTP.equals("D"))
			 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
		else if (LP_FLDTP.equals("T"))
			 return   (LP_FLDNM + " = "+(LP_FLDVL.trim().length()>=5 ? ("'"+LP_FLDVL.trim()+"',") : "null,"));
		else return " ";
	        }
	    catch (Exception L_EX) 
		{setMSG("Error in setUPDSTR : "+L_EX,'E');}
	return " ";
	}



    /**
    Method to add time given in second parameter to the time in first parameter 
    and returns the result in HH:MM format
	@param P_strSTRTM String argument to pass Starting Time.
	@param P_strNEWTM String argument to pass New Time.
    */
    private String addTIME(String P_strSTRTM,String P_strNEWTM)
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
            int  L_intTOTHR = (L_intSTRMN+L_intNEWMN) / 60;
            int  L_intTOTMN = (L_intSTRMN+L_intNEWMN)- ((L_intSTRMN+L_intNEWMN)/60)*60;
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
		    setMSG(L_EX, "addTIME");
    		return "";
		}            
    }

	
    /**
    Method to add time given in second parameter to the time in first parameter 
    and returns the result in HH:MM format
	@param P_strSTRTM String argument to pass Starting Time.
	@param P_strNEWTM String argument to pass New Time.
    */
    private String subTIME(String P_strSTRTM,String P_strNEWTM)
    {
	    String L_strRETSTR = "";
		try
        {
			if(P_strSTRTM.compareTo(P_strNEWTM) < 0)
			{
				String P_strXXXTM = P_strSTRTM;
				P_strSTRTM = P_strNEWTM;
				P_strNEWTM = P_strXXXTM;
			}
				
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
			setMSG(L_EX,"calTIME");
		}
		return L_strDIFTM;
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


boolean vldDATA()
	{
		try
		{
			if(txtDPTCD.getText().length()<3 && !flgALLDPT)
			{
				setMSG("Please Enter Department Code",'E');
				txtDPTCD.requestFocus();
				return false;
			}	
			if(txtSTRDT.getText().length()<10)
			{
				setMSG("Please Enter Start Date",'E');
				txtSTRDT.requestFocus();
				return false;
			}
			if(txtENDDT.getText().length()<10)
			{
				setMSG("Please Enter End Date",'E');
				txtSTRDT.requestFocus();
				return false;
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}


	/**
	 */
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{	
				if(getSource()==tblTEEXR)
				{
					if(P_intCOLID==TB_WRKSH)
					{
						if(getSource().getValueAt(P_intROWID,P_intCOLID).toString().length()==0)
							return true;
						//System.out.println("Entered Shift : "+((JTextField) tblTEEXR.cmpEDITR[TB_WRKSH]).getText().toString());
						setWRKDTX(tblTEEXR.getValueAt(P_intROWID,TB_WRKDT).toString());
						tblTEEXR.setValueAt(M_fmtLCDTM.format(fmtDBDATTM.parse(getSHFTM_ST(((JTextField) tblTEEXR.cmpEDITR[TB_WRKSH]).getText().toString(),"IN"))),P_intROWID,TB_INCTM_S);
						tblTEEXR.setValueAt(M_fmtLCDTM.format(fmtDBDATTM.parse(getSHFTM_ST(((JTextField) tblTEEXR.cmpEDITR[TB_WRKSH]).getText().toString(),"OUT"))),P_intROWID,TB_OUTTM_S);
						setWRKHR1(P_intROWID);
					}
					try{
					if(P_intCOLID==TB_INCTM_A)
					{
						if(getSource().getValueAt(P_intROWID,P_intCOLID).toString().length()==0)
							return true;
						if(getSource().getValueAt(P_intROWID,P_intCOLID).toString().length()<16)
						{
							setMSG("Please Enter Date and Time Properly",'E');
							return false;
						}	
						if(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_fmtLCDTM.parse(tblTEEXR.getValueAt(P_intROWID,TB_INCTM_A).toString()))).compareTo(M_fmtLCDAT.parse(tblTEEXR.getValueAt(P_intROWID,TB_WRKDT).toString()))<0)
						{
							setMSG("In time must be greter than working Date..",'E');
							return false;
						}
						tblTEEXR.setValueAt((((JTextField) tblTEEXR.cmpEDITR[TB_INCTM_A]).getText().toString()).substring(11,16),P_intROWID,TB_INCTM);
						setWRKHR1(P_intROWID);
					}
					setMSG("",'N');
					if(P_intCOLID==TB_OUTTM_A)
					{
						if(getSource().getValueAt(P_intROWID,P_intCOLID).toString().length()==0)
							return true;
						if(getSource().getValueAt(P_intROWID,P_intCOLID).toString().length()<16)
						{
							setMSG("Please Enter Date and Time Properly",'E');
							return false;
						}	
						if(M_fmtLCDTM.parse(tblTEEXR.getValueAt(P_intROWID,TB_OUTTM_A).toString()).compareTo(M_fmtLCDTM.parse(tblTEEXR.getValueAt(P_intROWID,TB_INCTM_A).toString()))<0)
						{
							setMSG("End time must be greter than Start time.",'E');
							return false;
						}
						if(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_fmtLCDTM.parse(tblTEEXR.getValueAt(P_intROWID,TB_OUTTM_A).toString()))).compareTo(M_fmtLCDAT.parse(tblTEEXR.getValueAt(P_intROWID,TB_WRKDT).toString()))<0)
						{
							setMSG("Out time must be greter than working Date..",'E');
							return false;
						}
						tblTEEXR.setValueAt((((JTextField) tblTEEXR.cmpEDITR[TB_OUTTM_A]).getText().toString()).substring(11,16),P_intROWID,TB_OUTTM);
						setWRKHR1(P_intROWID);
					}
					setMSG("",'N');
					}catch(Exception L_EX){setMSG(L_EX,"inside");}
				}
				if(getSource()==tblOVTDT)
				{	
					if(P_intCOLID==TB_EMPNO_OT)
					{
						if(tblOVTDT.getValueAt(P_intROWID,P_intCOLID).toString().length()==0)
							return true;
						if(tblOVTDT.getValueAt(P_intROWID,TB_WRKDT_OT).toString().length()==0)
						{
							setMSG("Please Enter Working Date First.",'E');
							return false;
						}
						M_strSQLQRY = " select EP_EMPCT,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,SW_WRKSH,isnull(SW_POTTM,'00:00') SW_POTTM,isnull(SW_SHRWK,'00:00') SW_SHRWK";
						M_strSQLQRY+= " from HR_EPMST,HR_SWMST";
						M_strSQLQRY+= " where EP_CMPCD=SW_CMPCD and EP_EMPNO=SW_EMPNO and SW_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblOVTDT.getValueAt(P_intROWID,TB_WRKDT_OT).toString()))+"' and EP_EMPNO='"+tblOVTDT.getValueAt(P_intROWID,TB_EMPNO_OT).toString()+"' AND EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							tblOVTDT.setValueAt(M_rstRSSET.getString("EP_EMPCT"),P_intROWID,TB_EMPCT_OT);
							tblOVTDT.setValueAt(M_rstRSSET.getString("EP_EMPNM"),P_intROWID,TB_EMPNM_OT);
							tblOVTDT.setValueAt(M_rstRSSET.getString("SW_WRKSH"),P_intROWID,TB_WRKSH_OT);
							tblOVTDT.setValueAt(M_rstRSSET.getString("SW_POTTM").substring(0,5),P_intROWID,TB_POTTM_OT);
							tblOVTDT.setValueAt(M_rstRSSET.getString("SW_SHRWK").substring(0,5),P_intROWID,TB_SHRWK_OT);
							if(P_intROWID>=intROWCNT)
								tblOVTDT.setValueAt(new Boolean(true),P_intROWID,TB_MNLFL_OT);
							setMSG("",'N');
							M_rstRSSET.close();
							return true;
						}	
						else
						{
							setMSG("Enter Valid Employee No.",'E');
							return false;
						}
					}
					else if(P_intCOLID==TB_INCTM_OT)
					{
						String L_strEMPNO = tblOVTDT.getValueAt(P_intROWID,TB_EMPNO_OT).toString();
						String L_strINCTM = tblOVTDT.getValueAt(P_intROWID,TB_INCTM_OT).toString();
						String L_strWRKDT = tblOVTDT.getValueAt(P_intROWID,TB_WRKDT_OT).toString();
						if(L_strINCTM.length()==0)
							return true;
						if(L_strINCTM.length()<16)
						{
							setMSG("Please Enter Date and Time Properly",'E');
							return false;
						}
						if(vldDATTM(L_strEMPNO,L_strINCTM,P_intROWID))
						{
							setMSG("OT For The Given Date Already Exist..",'E');
							return false;
						}
			
						String L_strWHRSTR =" OT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and OT_EMPNO = '"+L_strEMPNO+"' and OT_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'";
						L_strWHRSTR+=" and '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strINCTM))+"' between ot_strtm and ot_endtm"; 
						boolean flgCHK_EXIST =	chkEXIST("HR_OTTRN", L_strWHRSTR);
						if(flgCHK_EXIST)
						{
							setMSG("OT For The Given Date Already Exist..",'E');
							return false;
						}
						tblOVTDT.setValueAt((((JTextField) tblOVTDT.cmpEDITR[TB_INCTM_OT]).getText().toString()).substring(11,16),P_intROWID,TB_STRTM_OT);
					}
					else if(P_intCOLID==TB_OUTTM_OT)
					{
						String L_strEMPNO = tblOVTDT.getValueAt(P_intROWID,TB_EMPNO_OT).toString();
						String L_strSTRTM = tblOVTDT.getValueAt(P_intROWID,TB_STRTM_OT).toString();
						String L_strENDTM = tblOVTDT.getValueAt(P_intROWID,TB_ENDTM_OT).toString();
						String L_strOUTTM = tblOVTDT.getValueAt(P_intROWID,TB_OUTTM_OT).toString();
						String L_strINCTM = tblOVTDT.getValueAt(P_intROWID,TB_INCTM_OT).toString();
						String L_strWRKDT = tblOVTDT.getValueAt(P_intROWID,TB_WRKDT_OT).toString();
						
						//if(tblOVTDT.getValueAt(P_intROWID,TB_ENDTM_OT).toString().length()==0)
						//	return true;
						if(L_strOUTTM.length()<16)
						{
							setMSG("Please Enter Date and Time Properly",'E');
							return false;
						}	
						tblOVTDT.setValueAt((((JTextField) tblOVTDT.cmpEDITR[TB_OUTTM_OT]).getText().toString()).substring(11,16),P_intROWID,TB_ENDTM_OT);
						
						if(L_strSTRTM.length()==0)
						{
							setMSG("Please Enter Starting Time.",'E');
							return false;
						}
						if(M_fmtLCDTM.parse(L_strOUTTM).compareTo(M_fmtLCDTM.parse(L_strINCTM))<0)
						{
							setMSG("End time must be greter than Start time.",'E');
							return false;
						}
						if(vldDATTM(L_strEMPNO,L_strOUTTM,P_intROWID))
						{
							setMSG("OT For The Given Date Already Exist..",'E');
							return false;
						}
						if(vldDATTM1(L_strEMPNO,L_strOUTTM,P_intROWID))
						{
							setMSG("OT For The Given Date Already Exist..",'E');
							return false;
						}
						
						String L_strWHRSTR =" OT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and OT_EMPNO = '"+L_strEMPNO+"' and OT_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'";
						L_strWHRSTR+=" and (('"+M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strOUTTM))+"' between ot_strtm and ot_endtm) or "; 
						L_strWHRSTR+=" ('"+M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strINCTM))+"' < ot_strtm and '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strOUTTM))+"' > ot_endtm))"; 
						boolean flgCHK_EXIST =	chkEXIST("HR_OTTRN", L_strWHRSTR);
						if(flgCHK_EXIST)
						{
							setMSG("OT For The Given Date Already Exist..",'E');
							return false;
						}
						
						String L_strRNDIN = exeRNDOF(L_strSTRTM,"IN");
						String L_strRNDOT = exeRNDOF(L_strENDTM,"OUT");
						//String L_strRNDIN = L_strSTRTM;
						//String L_strRNDOT = L_strENDTM;
						
						if((L_strOUTTM.substring(0,10)+" "+L_strRNDOT).compareTo(L_strINCTM.substring(0,10)+" "+L_strRNDIN)<0)
						{
							setMSG("End time less than Start Time After Rounding Off.",'E');
							return false;
						}
						
						String L_strTMDIF = calTIME(L_strOUTTM.substring(0,10)+" "+L_strRNDOT,L_strINCTM.substring(0,10)+" "+L_strRNDIN);
						if(fmtHHMM.parse(L_strTMDIF).compareTo(fmtHHMM.parse(strMINOT))>=0)
						{
							tblOVTDT.setValueAt(L_strTMDIF,P_intROWID,TB_OVTWK_OT);
							tblOVTDT.setValueAt(L_strTMDIF,P_intROWID,TB_OVTHR_OT);
							tblOVTDT.setValueAt(L_strRNDIN,P_intROWID,TB_ESTTM_OT);
							tblOVTDT.setValueAt(L_strRNDOT,P_intROWID,TB_EENTM_OT);
							setMSG("",'E');
						}
						else
						{
							setMSG("Diff between Start Time and End Time must be 30 mins or above.."+L_strRNDIN+" : "+L_strRNDOT,'E');
							return false;
						}
					}
				}
				if(getSource()==tblEPSDT)
				{
					if(P_intCOLID==TB_OUTTM_EP)
					{
						String L_strOUTTM = tblEPSDT.getValueAt(P_intROWID,TB_OUTTM_EP).toString();
						if(L_strOUTTM.length()==0)
							return true;
						if(L_strOUTTM.length()<16)
						{
							setMSG("Please Enter Date and Time Properly",'E');
							return false;
						}
						tblEPSDT.setValueAt((((JTextField) tblEPSDT.cmpEDITR[TB_OUTTM_EP]).getText().toString()).substring(11,16),P_intROWID,TB_AOTTM_EP);
						setEXTIME(P_intROWID);
						
						
					}
					else if(P_intCOLID==TB_INCTM_EP)
					{
						String L_strOUTTM = tblEPSDT.getValueAt(P_intROWID,TB_OUTTM_EP).toString();
						String L_strINCTM = tblEPSDT.getValueAt(P_intROWID,TB_INCTM_EP).toString();
						
						if(L_strINCTM.length()<16)
						{
							setMSG("Please Enter Date and Time Properly",'E');
							return false;
						}	
						if(M_fmtLCDTM.parse(L_strINCTM).compareTo(M_fmtLCDTM.parse(L_strOUTTM))<0)
						{
							setMSG("In time must be greter than Out time.",'E');
							return false;
						}
 						tblEPSDT.setValueAt((((JTextField) tblEPSDT.cmpEDITR[TB_INCTM_EP]).getText().toString()).substring(11,16),P_intROWID,TB_AINTM_EP);
						setEXTIME(P_intROWID);
					}
				}
			}catch(Exception e)
			{
				setMSG(e,"TBLINPVF");
				setMSG("Invalid Data ..",'E');				
				return false;
			}
			return true;
		}
	}

	/** Method to validate date range
	 */
	public boolean vldDATTM1(String LP_EMPNO,String LP_DATE,int LP_ROW)
	{
		int i=0;
		try
		{
		for(i=0;i<tblOVTDT.getRowCount();i++)
			if(i!=LP_ROW)
			{
				if(LP_EMPNO.equals(tblOVTDT.getValueAt(i,TB_EMPNO_OT).toString()))
					if(M_fmtLCDTM.parse(LP_DATE).compareTo(M_fmtLCDTM.parse(tblOVTDT.getValueAt(i,TB_INCTM_OT).toString()))<0 &&
					   M_fmtLCDTM.parse(LP_DATE).compareTo(M_fmtLCDTM.parse(tblOVTDT.getValueAt(i,TB_OUTTM_OT).toString()))>0)
					{
						return true;
					}
			}
			return false;
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATE1()");		
		}
		return false;
	}	
	
	
	/** Method to validate date i.e. for is given date already exists 
	 */
	public boolean vldDATTM(String LP_EMPNO,String LP_DATE,int LP_ROW)
	{
		int i=0;
		try
		{
		for(i=0;i<tblOVTDT.getRowCount();i++)
			if(i!=LP_ROW)
			{
				if(LP_EMPNO.equals(tblOVTDT.getValueAt(i,TB_EMPNO_OT).toString()))
					if(M_fmtLCDTM.parse(LP_DATE).compareTo(M_fmtLCDTM.parse(tblOVTDT.getValueAt(i,TB_INCTM_OT).toString()))>=0 &&
					   M_fmtLCDTM.parse(LP_DATE).compareTo(M_fmtLCDTM.parse(tblOVTDT.getValueAt(i,TB_OUTTM_OT).toString()))<=0)
					{
						return true;
					}
			}
			return false;
		}	
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDAT()");		
		}
			return false;
	}	

	
	private String[] dspCOTRN(String LP_EMPNO)
	{
		String[] L_staOCHDT={"",""};
		try
		{
			int TB_CHKFL_CO = 0;
			int TB_WRKDT_CO = 1;
			int TB_LVECD_CO = 2;
			
			pnlCOTRN=new JPanel(null);
			int L_intROW=0;
			String[] L_staCOLHD = {"","Date","Leave Code"};
			int[] L_inaCOLSZ = {10,150,150};
			String L_strDSPMSG = LP_EMPNO+" has pending OFF - Change For the Following Dates, Please Select Date To avail Leave";
			add(new JLabel(L_strDSPMSG),1,1,2,9,pnlCOTRN,'L');
			tblCOTRN = crtTBLPNL1(pnlCOTRN,L_staCOLHD,50,3,1,4,9,L_inaCOLSZ,new int[]{0});
			tblCOTRN.setEnabled(false);
			((JCheckBox)tblCOTRN.cmpEDITR[TB_CHKFL_CO]).setEnabled(true);
			pnlCOTRN.setSize(200,100);
			pnlCOTRN.setPreferredSize(new Dimension(600,200));
			
			for(int i=0;i<vtrCOTRN_W.size();i++)
				if(vtrCOTRN_W.get(i).substring(0,4).equals(LP_EMPNO))
				{
					tblCOTRN.setValueAt(vtrCOTRN_W.get(i).substring(5,15),L_intROW,TB_WRKDT_CO);
					tblCOTRN.setValueAt(vtrCOTRN_W.get(i).substring(16,18),L_intROW,TB_LVECD_CO);
					L_intROW++;
				}
			
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlCOTRN,"Off-Change details",JOptionPane.OK_CANCEL_OPTION);
			if(L_intOPTN == 0)
			{
				for(int i=0;i<tblCOTRN.getRowCount();i++)
				{
					if(tblCOTRN.getValueAt(i,TB_CHKFL_CO).toString().equals("true"))
					{
						L_staOCHDT[0] = tblCOTRN.getValueAt(tblCOTRN.getSelectedRow(),TB_WRKDT_CO).toString();
						L_staOCHDT[1] = tblCOTRN.getValueAt(tblCOTRN.getSelectedRow(),TB_LVECD_CO).toString();
						return L_staOCHDT;
					}
				}
			}
		}

		catch (Exception L_EX)
		{
			setMSG("Error in dspCOTRN : "+L_EX,'E');
		}
		return L_staOCHDT;
	}
	
	/**
	 * Displays Attendance Punching details for verification
	 *
	 */
	private String[] dspVEWAT(cl_JTable LP_TABLE,int LP_ROWID,int LP_EMPNO,int LP_EMPNM,int LP_WRKDT)
	{
		String[] strINOUT = {"",""};
		try
		{
			pnlVEWAT=new JPanel(null);
			pnlCOTRN=new JPanel(null);
			String[] L_staCOLHD = {"Chk","Date","S.No.","In Time","Out Time"};
			int[] L_inaCOLSZ = {20,80,30,150,150};
			
			add(lblEMPNM = new JLabel(""),1,1,1,4,pnlVEWAT,'L');
			add(btnSETTM = new JButton("Set Time"),1,10,1,2,pnlVEWAT,'L');
			btnSETTM.addKeyListener(this);
			add(new JLabel("In-Time"),2,10,1,3,pnlVEWAT,'L');
			add(txtINCTM_VW = new TxtLimit(16),3,10,1,3,pnlVEWAT,'L');
			add(new JLabel("Out-Time"),4,10,1,3,pnlVEWAT,'L');
			add(txtOUTTM_VW = new TxtLimit(16),5,10,1,3,pnlVEWAT,'L');
			
			tblVEWAT = crtTBLPNL1(pnlVEWAT,L_staCOLHD,50,2,1,4,9,L_inaCOLSZ,new int[]{0});
			tblVEWAT.setEnabled(false);
			txtINCTM_VW.setEnabled(false);
			txtOUTTM_VW.setEnabled(false);
			//**setWRKDTX(txtWRKDT.getText().toString().trim());
			setWRKDTX(LP_TABLE.getValueAt(LP_TABLE.getSelectedRow(),LP_WRKDT).toString());
			String L_strWHRSTR_AL1 = " SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and  swt_wrkdt between '"+M_fmtDBDAT.format(datWRKDT_2)+"' and '"+M_fmtDBDAT.format(datWRKDT3)+"' and swt_stsfl not in ('X') and SWT_EMPNO = '"+LP_TABLE.getValueAt(LP_ROWID,LP_EMPNO).toString()+"'";		
			M_strSQLQRY = "select swt_wrkdt,swt_srlno,swt_inctm,swt_outtm,isnull(swt_incfl,'') swt_incfl,isnull(swt_outfl,'') swt_outfl from hr_swtrn where "+L_strWHRSTR_AL1+" order by swt_wrkdt,swt_srlno,swt_inctm,swt_outtm";
			
			//System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			java.util.Date L_datWRKDT = null,L_datWRKDT_OLD = null;
			java.sql.Timestamp L_tmsINCTM = null,L_tmsOUTTM = null;
			java.sql.Time L_timWRKTM = null;
			
			tblVEWAT.clrTABLE();
			inlTBLEDIT(LP_TABLE);
			inlTBLEDIT(tblVEWAT);
			int i =0;
			//boolean L_flgIN = false;
			String L_strSRLNO = "1";
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				while (true)
				{
					tblVEWAT.setValueAt(M_fmtLCDAT.format(L_rstRSSET.getDate("SWT_WRKDT")),i,TB1_WRKDT);
					tblVEWAT.setValueAt(L_rstRSSET.getString("SWT_SRLNO"),i,TB1_SRLNO);
					if(L_rstRSSET.getTimestamp("SWT_INCTM")!=null)
					{
						tblVEWAT.setValueAt(fmtLCDTM_L.format(L_rstRSSET.getTimestamp("SWT_INCTM")) ,i,TB1_INCTM);
						//if(L_rstRSSET.getString("SWT_INCFL").equals("X"))
						//	tblVEWAT.setValueAt(new Boolean(true),i,TB1_INCDL);
					}
					if(L_rstRSSET.getTimestamp("SWT_OUTTM")!=null)
					{
						tblVEWAT.setValueAt(fmtLCDTM_L.format(L_rstRSSET.getTimestamp("SWT_OUTTM")) ,i,TB1_OUTTM);
						//if(L_rstRSSET.getString("SWT_OUTFL").equals("X"))
						//	tblVEWAT.setValueAt(new Boolean(true),i,TB1_OUTDL);
					}
					if(!L_rstRSSET.next())
						break;
					i++;
				}
			}
			if(L_rstRSSET != null)
				L_rstRSSET.close();
			
			lblEMPNM.setText(LP_TABLE.getValueAt(LP_ROWID,LP_EMPNO).toString()+"  "+LP_TABLE.getValueAt(LP_ROWID,LP_EMPNM).toString());
			pnlVEWAT.setSize(200,100);
			pnlVEWAT.setPreferredSize(new Dimension(600,200));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVEWAT,"Punching details",JOptionPane.OK_CANCEL_OPTION);
			
			if(L_intOPTN == 0)
			{
				strINOUT[0] = txtINCTM_VW.getText();
				strINOUT[1] = txtOUTTM_VW.getText();
			}
			
			/*if(L_intOPTN == 0)
			{
				String strWHRSTR="";
				for(i=0;i<tblVEWAT.getRowCount();i++)
				{
					if(tblVEWAT.getValueAt(i,TB1_INCTM).toString().length()>0)
					{
						strWHRSTR =" SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SWT_EMPNO='"+tblTEEXR.getValueAt(LP_ROWID,TB_EMPNO).toString()+"'";
						strWHRSTR+=" and SWT_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblVEWAT.getValueAt(i,TB1_WRKDT).toString()))+"'";											
						M_strSQLQRY = " Update HR_SWTRN set";
						if(tblVEWAT.getValueAt(i,TB1_INCDL).toString().equals("true"))
    						M_strSQLQRY +=" SWT_INCFL='X'";
						else
							M_strSQLQRY +=" SWT_INCFL=''";
						M_strSQLQRY +=" where "+strWHRSTR;
						M_strSQLQRY +=" and SWT_INCTM='"+M_fmtDBDTM.format(fmtLCDTM_L.parse(tblVEWAT.getValueAt(i,TB1_INCTM).toString()))+"'";
						//System.out.println("M_strSQLQRY1>>"+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					if(tblVEWAT.getValueAt(i,TB1_OUTTM).toString().length()>0)
					{
						strWHRSTR =" SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SWT_EMPNO='"+tblTEEXR.getValueAt(LP_ROWID,TB_EMPNO).toString()+"'";
						strWHRSTR+=" and SWT_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblVEWAT.getValueAt(i,TB1_WRKDT).toString()))+"'";					
						M_strSQLQRY = " Update HR_SWTRN set";
    					if(tblVEWAT.getValueAt(i,TB1_OUTDL).toString().equals("true"))
							M_strSQLQRY +=" SWT_OUTFL='X'";
						else
							M_strSQLQRY +=" SWT_OUTFL=''";
						M_strSQLQRY +=" where "+strWHRSTR;
						M_strSQLQRY +=" and SWT_OUTTM='"+M_fmtDBDTM.format(fmtLCDTM_L.parse(tblVEWAT.getValueAt(i,TB1_OUTTM).toString()))+"'";
						//System.out.println("M_strSQLQRY2>>"+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				if(cl_dat.exeDBCMT("exeSAVE"))
					setMSG("",'N');
				else
				    setMSG("Error in updating data..",'E');
			}*/
		}

		catch (Exception L_EX)
		{
			setMSG("Error in dspVEWAT : "+L_EX,'E');
		}
		return strINOUT;
	}
	
	/**
	 */
	private java.util.Date getWRKDT(java.sql.Timestamp LP_WRKTM, String LP_INOCD)
	{
		java.util.Date L_datWRKDT = null;
		try
		{
			java.util.Date L_datWRKDT_1 = null;
			SimpleDateFormat L_fmtHHMMSS = new SimpleDateFormat("HH:mm:ss"); 

			L_datWRKDT = M_fmtLCDAT.parse(M_fmtLCDAT.format(LP_WRKTM));
					
			M_calLOCAL.setTime(L_datWRKDT);      
			M_calLOCAL.add(Calendar.DATE,-1);    
			L_datWRKDT_1 = M_calLOCAL.getTime();
			////System.out.println(M_fmtLCDTM.format(LP_WRKTM));
			////System.out.println(M_fmtLCDAT.format(LP_WRKTM)+" 06:00");
			java.util.Date L_date1 = M_fmtLCDTM.parse(M_fmtLCDTM.format(LP_WRKTM));
			java.util.Date L_date2 = M_fmtLCDTM.parse(M_fmtLCDAT.format(LP_WRKTM)+" 06:00");
			if(LP_INOCD.equals("0"))
				if((M_fmtLCDTM.parse(M_fmtLCDTM.format(LP_WRKTM))).compareTo(M_fmtLCDTM.parse(M_fmtLCDAT.format(LP_WRKTM)+" 06:00"))<0)
					L_datWRKDT = L_datWRKDT_1;
					
			if(LP_INOCD.equals("1"))
				if((M_fmtLCDTM.parse(M_fmtLCDTM.format(LP_WRKTM))).compareTo(M_fmtLCDTM.parse(M_fmtLCDAT.format(LP_WRKTM)+" 10:00"))<0)
					L_datWRKDT = L_datWRKDT_1;
			
			//System.out.println(M_fmtLCDTM.format(LP_WRKTM)+"   "+LP_INOCD+"   "+M_fmtLCDAT.format(L_datWRKDT));
		}

		catch (Exception L_EX)
		{
			setMSG("Error in getWRKDT : "+L_EX,'E');
		}
		return(L_datWRKDT);
	}
	
	private void crtHSTMKT()
	{
	    try
	    {
	        String L_strPHSKEY ="",L_strHSKEY="";
	        String[] L_staVALUE = new String[]{"","","","","","",""};
	        int L_intCNT =0;
	        cl_dat.M_hstMKTCD_pbst = new Hashtable<String,String[]>();
	        // String L_strSQLQRY = "SELECT * FROM CO_CDTRN WHERE CMT_CGMTP||CMT_CGSTP IN("+
	        //"'SYSMRXXDTP','SYSMRXXPMM','SYSMRXXRGN','SYSMR00SAL','SYSMR00ZON','SYSMRXXDLT','SYSMRXXPTT','SYSMRXXLAU','SYSFGXXPKG') order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
	        String L_strSQLQRY = "SELECT * FROM CO_CDTRN A WHERE A.CMT_CGMTP + A.CMT_CGSTP IN "+
			"(SELECT B.CMT_CODCD FROM CO_CDTRN B WHERE B.CMT_CGMTP='CAT' AND B.CMT_CGSTP = 'COXXHST' AND isnull(CMT_STSFL,' ') <> 'X') order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
	        ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
	        //System.out.println("call to crtHSTMKT"+L_strSQLQRY);
	        if(L_rstRSSET !=null)
	        {
	            while(L_rstRSSET.next())
	            {
	                L_strHSKEY = L_rstRSSET.getString("CMT_CGMTP")+L_rstRSSET.getString("CMT_CGSTP")+nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");
	                if(!L_strHSKEY.equals(L_strPHSKEY))
	                {
	                    L_staVALUE = new String[8];
	                    L_staVALUE[cl_dat.M_intCODDS_pbst] = L_rstRSSET.getString("CMT_CODDS");
	                    L_staVALUE[cl_dat.M_intSHRDS_pbst] = L_rstRSSET.getString("CMT_SHRDS");
	                    L_staVALUE[cl_dat.M_intCHP01_pbst] = L_rstRSSET.getString("CMT_CHP01");
	                    L_staVALUE[cl_dat.M_intCHP02_pbst] = L_rstRSSET.getString("CMT_CHP02");
	                    L_staVALUE[cl_dat.M_intCCSVL_pbst] = L_rstRSSET.getString("CMT_CCSVL");
	                    L_staVALUE[cl_dat.M_intNMP01_pbst] = L_rstRSSET.getString("CMT_NMP01");
	                    L_staVALUE[cl_dat.M_intNMP02_pbst] = L_rstRSSET.getString("CMT_NMP02");
	                    L_staVALUE[cl_dat.M_intNCSVL_pbst] = L_rstRSSET.getString("CMT_NCSVL");
	                    cl_dat.M_hstMKTCD_pbst.put(L_strHSKEY,L_staVALUE);
	                    L_intCNT++;
	                }
	            }
	            if(L_intCNT >0)
	                cl_dat.M_hstMKTCD_pbst.put(L_strHSKEY,L_staVALUE);
	            //System.out.println(cl_dat.M_hstMKTCD_pbst.size());    
	         L_rstRSSET.close();    
	        }
	        
	        
	    }
	    catch(Exception L_E)
	    {
	       System.out.println("crtHSTMKT "+L_E.toString()); 
	    }
	}
		
}

