 /*
System Name   : HRMS Leave Transaction Entry
Program Desc. : Leave Transactions.



System Name : Human Resurce Management System.
Program Name :  Leave Transactions Entry
Source Directory : f:\source\splerp2\hr_telve..java                        Executable Directory : f:\exec\splerp2\hr_telve.class

Purpose : This module captures  Leave transactions for Employee as per Company Rules. and gives a provision to Authorise the same.

List of tables used :
Table Name		Primary key							Operation done
													Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
HR_SSTRN		SS_SBSCD,SS_EMPNO,SS_WRKDT				/	/		    /	     /
HR_LVTRN		LVT_SBSCD,LVT_EMPNO,LVT_STRDT			/	/		    /	     /
CO_CDTRN		CMT_CGMTP,CMT_CGSTP,CMT_CODCD						    /
HR_EPMST		PT_PRTTP,PT_PRTCD				    		    	    /
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name			Table name			Type/Size	Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description		Display Columns			Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtDPTCD	Department code			ep_dptcd				HR_EPMST
txtRCMBY	Recommending Auth		cmt_chp01				co_cdtrn	AUT / HRXXLRC / txtDPTCD
txtAUTBY	Sanctioning Authority	cmt_chp01				co_cdtrn  	AUT / HRXXLAU / txtDPTCD
txtEMPCT	Employee Category		EP_EMPCT				HR_EPMST
txtLVECD	Leave Code				LVT_LVECD				HR_LVTRN
									ss_lvecd				HR_SSTRN
									co_cdtrn				SYS/HRXXLVE
txtLVEQT	Total No.of days		User Entry				------
txtAPPDT	Appl.Date				lvt_appdt				HR_LVTRN
txtEMPNO	Employee No.			LVT_EMPNO				HR_LVTRN
txtSTRDT	Leave Start date		User Entry				------
txtENDDT	Leave End date			User Entry				------
txtRSNDS	Reason					lvt_rsnds				HR_LVTRN

Leave Balance
TB1_YOPCL	Year Opening CL			EP_YOPCL				HR_EPMST				
TB1_YOPPL	Year Opening PL			EP_YOPPL				HR_EPMST				
TB1_YOPRH	Year Opening RH			EP_YOPRH				HR_EPMST				
TB1_YOPSL	Year Opening SL			EP_YOPSL				HR_EPMST				
TB1_CURCL	Balance CL				EP_CURCL				HR_EPMST				
TB1_CURPL	Balance PL				EP_CURPL				HR_EPMST				
TB1_CURRH	Balance RH				EP_CURRH				HR_EPMST				
TB1_CURSL	Balance SL				EP_CURSL				HR_EPMST				


Leave Distribution
TB2_LVEDT	Laeve Date				LVT_LVEDT				HR_LVTRN				
TB2_LVEQT	Laeve Qty. (Days)		LVT_LVEQT				HR_LVTRN
TB1_YOPXX	Year Opening leave		EP_YOPXX				HR_EPMST				
TB1_YTDXX	Year to Date leave		EP_YTDXX				HR_EPMST				
TB1_UAUXX	Unauthorized leave		sum(LVT_LVEQT)			HR_LVTRN		
									lvt_stsfl in ('0','8','9')
TB1_YTDSL	Balance SL				EP_YTDSL				HR_EPMST				
TB1_YTDEA	Balance EO/A			EP_YTDEA				HR_EPMST				
TB1_YTDEU	Balance EO/U			EP_YTDEU				HR_EPMST
TB1_YTDPL	Balance PL				EP_YTDPL				HR_EPMST				
TB1_YTDRH	Balance RH				EP_YTDRH				HR_EPMST				
TB1_YTDSL	Balance SL				EP_YTDSL				HR_EPMST				
TB1_YTDEU	Balance EO/U			EP_YTDEU				HR_EPMST
tb1_empno	Employee No.			ss_empno				HR_SSTRN
									ivt_empno				HR_LVTRN
tb1_empnm	Employee Name			ss_empnm				HR_EPMST
tb1_lve01	Leave Code for Day 01	ss_lvecd,ivt_lvecd		HR_SSTRN, HR_LVTRN
tb1_lve02	Leave Code for Day 02	ss_lvecd,ivt_lvecd		HR_SSTRN , HR_LVTRN
tb1_.....	Leave Code for Day ...	ss_lvecd,ivt_lvecd		HR_SSTRN , HR_LVTRN
tb1_.....	Leave Code for Day ...	ss_lvecd,ivt_lvecd		HR_SSTRN , HR_LVTRN
-----------------------------------------------------------------------------------------------------------------------------------------------------
Validations :
->LUSBY & LUPDT columns to be updated in addition, modification & deletion mode.
->Before saving leave transactions it should be validated against Company rules defined for Sanctionig of Leave.
(Company rules for sanctioning of leave are defined on separate sheet)


Other  Requirements :
->While fetching the leave code in Distribution table, it will be fetched from HR_SSTRN / HR_SWMST
->While saving the entries from Entry table, it will be saved in HR_SSTRN and HR_LVTRN simultaneouslly
->(HR_SSTRN record will be processed so as to transfer the leave details in HR_SWMST)
->Department to which the logging in person is authorised are only displayed while selecting Department Code.
->Leave Recommending and Sanctioning autorities for specified department are only available for selection.
->User can either select Employee Category and then employee code within the specified category, or can select employee code directly in this case Employee category of the specified employee number will be picked up from employee master and will be displayed in Employee Category Text Box.
->After selecting Employee Number Dept.Code, Leave Recommending & sanctioning authority and Employee category etc. will be freezed for selection
->Default leave code could be entered in case there is no mixup of more than one leave types in leave application
->Leave Starting Date, Ending date, Total No. of days for which the leave is to be availed and Date of application will be specified by the user.
->The entries in Leave distribution table will be populated programatically using Starting Date and Ending date, The leave type will be taken from default leave type specified. Paid Holidays, Restricted Holidays, Weekly Offs etc. will be picked up from respective table as applicable and will get populated in Leave Type column.
->Leave qty will be populated as 1.0 against each day in Leave distribution table.
->Total No. of leave from Leave Qty. column will have to be matched against Total No. of leave days specified by the user.
->User will make  cahanges in Leave type column and Leave Qty.column as desired.
->Leave type sequence and quantity will be verified against the company rules and employee eligibility, In case any descripancies are identified, user will be prompted for correcting the same.
->If selected leave pattern demands for special authorisation, then user will  have option to save the record, which will be saved under "Waiting for Special Sanction" status.
->While saving the leave details in Transaction Table (hr_lvtrn), entries from Leave distribution table will be grouped against leave type and date range, before saving in Leave Transaction table.
->Leave status will also be saved in Shift Schedule Transaction (hr_sstrn), which will be updated in shift working master through Shif Schedule processing

Rules for Leave validation and Processing
->P.L. should be minimum three days
->No.of P.L. applications within a year should not exceed 3
(In exceptional cases, special sanction should be taken)
->CL and RH can not be clubbed with S.L. (Unless a special sanction has been taken)
->Leave granted should be validated against balance availabe under the leave category.

Leave balance will be updated only after Authorisation of the Leave Application (along with special sanction, wherever necessary)
->If leave is not autorised (or waiting for special saction) and the employee does not report on duty for the particular day. It will be treated as Extra Ordinary Unauthorised leave.
->If leave applied has not been availed, and employee reports to duty on leave day.
->If the leave is waiting for approval (or special sanction), it will be treated as cancelled.
->If the leave is authorised one, then a separate leave cancellation record will be generated, whrere in employee has to enter the reason for Leave Cancellation.
->If the cancelled leave has been already processed, the processing will be reversed and corresponding qty. of leave balance will get released. 

Leave Status is stored in CO_CDTRN with STSHRXXLVT.

*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;	
import java.lang.*;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.JTabbedPane;
	
class hr_telve extends cl_pbase
{
	JTextField txtDPTCD;
	JTextField txtRCMBY;
    JTextField txtSCNBY;
    JTextField txtAPPDT;
    JTextField txtLVEQT1;
	JTextField txtEMPNO;
	JTextField txtDESGN;
	JTextField txtMMGRD;
    JTextField txtSTRDT;
	JTextField txtENDDT;
	JTextField txtCONNO;
	JTextField txtRSNDS;
	JTextField txtLVECD3;
	JTextField txtRSNDS3;   ///textbox for cancel reason
	JTextField txtPLCNM;
	JTextField txtORGNM;
	
	private JLabel lblDPTNM;
	private JLabel lblEMPNM;
	private JLabel lblTOTAL;
	private JLabel lblDSPTT;
	//private JLabel lblSTSFL;
	//private JLabel lblSTSDS;
	private JLabel lblRSNDS3;
	private JLabel lblINFMN1;
	private JLabel lblINFMN2;
	private int cntINFMN=0;
	private JButton btnVERFY;
	private Hashtable<String,String> hstHDRVAL;
	private Hashtable<String,String[]> hstCDTRN;			// Details of all codes used in program
	private Hashtable<String,String> hstCODDS;			// Code No. from Code Description
	private Hashtable<String,String[]> hstLVDTL;         // Leave detail for entire year
	private Hashtable<String,String> hstYOPDTL;        // Year opening & Year to date leaves
	private Object[] arrHSTKEY;
    private JCheckBox chkSSCAUT;        // Provision to opt for Special Sanction
    private JCheckBox chkHDSTS;        // held for discussion status
	private JCheckBox chkCOVEH;
	private JCheckBox chkBUS;
	private JCheckBox chkTRAIN;
	private JCheckBox chkAIR;
	private JCheckBox chkHRVEH;
	private  JTabbedPane tbpMAIN;
	private  JComboBox cmbTRNTP;
    private  JPanel	pnlHSTRY;            /** Panel for History details */
	private  JPanel	pnlAUTHR;            /** Panel for Grade details */
	private  JPanel	pnlTRDTY;            /** Panel for Grade details */

    private TBLINPVF objTBLVRF;
	private INPVF oINPVF;
    private  cl_JTable tblTELVE;
	private  cl_JTable tblDSTRB;
    private  cl_JTable tblLEAVE;
	private  cl_JTable tblAUTHR;
	protected SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    protected SimpleDateFormat fmtDD = new SimpleDateFormat("dd");
	private String M_strSQLQRY1="";
	private String strCMPCD_LOGIN;
	private String strEMPNO_LOGIN;
	private String strHRXXRHX="", strHRXXPHX="",strHRXXRHY="", strHRXXPHY="";
	private String strHRXXCLQ="";
	private String strYSTDT = "";
	private String strYENDT = "";
	private String strSTR_YOPDT;
	private String strEND_YOPDT;
	private String strVERFY_AUT = "";
	private String strVERFY_SSC = "";
	private String strVERFY_VLD = "";
	private String strYRDGT_CUR = "";
	private String strYRDGT_PRV = "";
	private int intLVT_STSFL=1;
	private int intLVT_SSCFL=0;
	private int cntCL=0,cntPL=0,cntRH=0,cntSL=0;
	private boolean flgALLDPT = false;
	private boolean flgSSNAUT=false;
	private boolean flgLVSSN=false;
	//for tblLEAVE
	int TB1_CHKFL = 0; 
    int TB1_LVECD = 1;                   JTextField txtLVECD1;
    int TB1_YOPXX = 2;                   JTextField txtYOPXX;
    int TB1_YTDXX = 3;                   JTextField txtYTDXX;
	int TB1_UAUXX = 4;                   JTextField txtUAUXX;
 
	
	//for tblDSTRB
	int TB2_CHKFL = 0;										
	int TB2_LVEDT = 1;                    JTextField txtLVEDT;
    int TB2_LVEDY = 2;                    JTextField txtLVEDY;	
    int TB2_LVECD = 3;                    JTextField txtLVECD;
	int TB2_LVEQT = 4;                    JTextField txtLVEQT;
	int TB2_SRLNO = 5;                    JTextField txtSRLNO;

	//for tblTELVE
	int TB3_CHKFL = 0;					  JCheckBox chkCHKFL3;
	int TB3_APPDT = 1;					  JTextField txtAPPDT1;				
    int TB3_STRDT = 2;					  JTextField txtSTRDT1;									
	int TB3_ENDDT = 3;					  JTextField txtENDDT1;									
    int TB3_LVECD = 4;					  JTextField txtLVECD2;
	int TB3_LVEQT = 5;					  JTextField txtLVEQT2;
	int TB3_RSNDS = 6;					  JTextField txtRSNDS1;								
    int TB3_RCMBY = 7;					  JTextField txtRCMBY1;
	int TB3_SCNBY = 8;					  JTextField txtSCNBY1;
	int TB3_STSDS = 9;					  JTextField txtSTSFL1;    
	int TB3_REFDT = 10;					  JTextField txtREFDT1;					
	
	//for tblAUTHR
	int TB4_CHKFL = 0;										
	int TB4_EMPNO = 1;					  JTextField txtEMPNO1;				
    int TB4_EMPNM = 2;					  JTextField txtEMPNM;									
	int TB4_APPDT = 3;					  JTextField txtAPPDT2;									
	int TB4_REFDT = 4;					  JTextField txtREFDT2;									
	int TB4_STSDS = 5;					  JTextField txtSSCFL1;

	/** Array elements for records picked up from Code Transactoion */
	private int intCDTRN_TOT = 10;			
    private int intAE_CMT_CODCD = 0;		
    private int intAE_CMT_CODDS = 1;		
    private int intAE_CMT_SHRDS = 2;		
    private int intAE_CMT_CHP01 = 3;		
    private int intAE_CMT_CHP02 = 4;		
    private int intAE_CMT_NMP01 = 5;		
    private int intAE_CMT_NMP02 = 6;		
    private int intAE_CMT_CCSVL = 7;		
    private int intAE_CMT_NCSVL = 8;
    private int intAE_CMT_MODLS = 9;
	

	
	/** Array elements for leave detail storing / accessing */
	private int intLVDTL_TOT = 6;			
    private int intAE_LVT_LVEDT = 0;		
    private int intAE_LVT_SRLNO = 1;		
    private int intAE_LVT_LVECD = 2;		
    private int intAE_LVT_LVEQT = 3;		
    private int intAE_LVT_SSCFL = 4;		
    private int intAE_LVT_STSFL = 5;		
	
	
	/** Variables for Code Transaction Table
	 */
	private String strCGMTP;		
	private String strCGSTP;		
	private String strCODCD;		
	private String strDPTCD="";
	private String strDPTNM="";
	private String strAPPDT="";
	private String strREFDT="";
	private String strRCMBY="";
	private String strSCNBY="";
	private String strHRXXXSN="";
	private String strLEAVE_EN = "Leave Entry";
	private String strTDUTY_EN = "Tour Duty";
	private String strPLENC_EN = "PL Encashment";
	private boolean flgPLENC_EN = false;
	
	/* Constructor */	
	hr_telve()
	{
	    super(2);
		try
		{
			if(cl_dat.M_strUSRCD_pbst.equals("SKS") || cl_dat.M_strUSRCD_pbst.equals("MMN") || cl_dat.M_strUSRCD_pbst.equals("S_J") || cl_dat.M_strUSRCD_pbst.equals("PDP") || cl_dat.M_strUSRCD_pbst.equals("RPN"))
			   flgALLDPT = true;
		    setMatrix(20,20);
			//strEMPNO_LOGIN = getEMPNO(cl_dat.M_strUSRCD_pbst);
			add(new JLabel("Transaction Type"),1,1,1,2.5,this,'L');
			add(cmbTRNTP=new JComboBox(),1,3,1,3.5,this,'L');
			cmbTRNTP.addItem(strLEAVE_EN);
			cmbTRNTP.addItem(strTDUTY_EN);
			cmbTRNTP.addItem(strPLENC_EN);
			
			add(new JLabel("Department"),1,7,1,2,this,'L');
			add(txtDPTCD = new TxtLimit(3),1,9,1,1,this,'L');
			add(lblDPTNM=new JLabel(),1,10,1,2, this,'L');
			
			add(new JLabel("Employee Code"),1,12,1,2,this,'L');
			add(txtEMPNO = new TxtLimit(4),1,14,1,1,this,'L');
			add(lblEMPNM=new JLabel(),1,15,1,3,this,'L');
			
			add(new JLabel("Rcm.By."),1,18,1,2,this,'L');
			add(txtRCMBY = new TxtLimit(5),1,20,1,1,this,'L');

			add(new JLabel("Snc.By."),2,18,1,2,this,'L');
			add(txtSCNBY = new TxtLimit(5),2,20,1,1,this,'L');
			
			add(new JLabel("Designation"),2,1,1,2,this,'L');
			add(txtDESGN = new TxtLimit(10),2,3,1,2,this,'L');
			
			add(new JLabel("Grade"),2,7,1,2,this,'L');
			add(txtMMGRD = new TxtLimit(10),2,9,1,1,this,'L');

			add(new JLabel("Appl. Date"),3,1,1,2,this,'L');
			add(txtAPPDT = new TxtDate(),3,3,1,2,this,'L');
			
			add(new JLabel("No. Of. Days"),3,7,1,2,this,'L');
			add(txtLVEQT1 = new TxtNumLimit(4.1),3,9,1,1,this,'L');
			
			add(new JLabel("Start Date"),4,1,1,2,this,'L');
			add(txtSTRDT = new TxtDate(),4,3,1,2,this,'L');
			
			add(new JLabel("End Date"),4,7,1,2,this,'L');
			add(txtENDDT = new TxtDate(),4,9,1,2,this,'L');
			
			add(new JLabel("Contact No"),5,1,1,2,this,'L');
			add(txtCONNO = new TxtLimit(30),5,3,1,3,this,'L');
			
			add(new JLabel("Purpose"),6,1,1,2,this,'L');
			add(txtRSNDS = new TxtLimit(100),6,3,1,7,this,'L');
			
        	add(lblRSNDS3=new JLabel(),7,1,1,3,this,'L');
			add(txtRSNDS3 = new TxtLimit(100),7,4,1,7,this,'L');
			add(lblINFMN1 = new JLabel(""),7,7,2,4,this,'L');
			add(lblINFMN2 = new JLabel(""),8,7,2,4,this,'L');
			lblRSNDS3.setText("Reason For Cancellation");
			
			add(chkSSCAUT=new JCheckBox("Opt for Spl.Sanction", false),8,1,1,3,this,'L');
			add(chkHDSTS=new JCheckBox("Held for Discussion", false),8,4,1,3,this,'L');
			add(new JLabel("Leave Code"),9,2,1,3,this,'L');
			add(txtLVECD3 = new TxtLimit(2),9,4,1,1,this,'L');
			
			lblRSNDS3.setVisible(false);
			txtRSNDS3.setVisible(false);
			lblINFMN1.setVisible(false);
			lblINFMN2.setVisible(false);
			
			add(btnVERFY=new JButton("Verify"),9,5,1,2,this,'L');
			pnlHSTRY = new JPanel(null);
		    pnlAUTHR = new JPanel(null);			
			pnlTRDTY = new JPanel(null);			
			tbpMAIN = new JTabbedPane();
			add(tbpMAIN,8,7,10,15,this,'L');
			tbpMAIN.addTab("Leave History",pnlHSTRY);			
			tbpMAIN.addTab("Authorisation",pnlAUTHR);
			tbpMAIN.addTab(strTDUTY_EN,pnlTRDTY);
			if(!setEMPNO_LOGIN(cl_dat.M_strUSRCD_pbst))
				JOptionPane.showMessageDialog(null, "Base details for "+cl_dat.M_strUSRCD_pbst+"  Login are not available" , "Error Message", JOptionPane.ERROR_MESSAGE); 
				
			String L_strCMPCD = cl_dat.M_strCMPCD_pbst;
			strYRDGT_CUR = String.valueOf(Integer.parseInt(fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4))).substring(3,4);
			strYRDGT_PRV = String.valueOf(Integer.parseInt(fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4))-1).substring(3,4);
			//System.out.println(" strYRDGT_PRV : "+strYRDGT_PRV);
			strHRXXRHX = "SYSHR"+cl_dat.M_strCMPCD_pbst+"RH"+strYRDGT_CUR;
			strHRXXPHX = "SYSHR"+cl_dat.M_strCMPCD_pbst+"PH"+strYRDGT_CUR;
			strHRXXRHY = "SYSHR"+cl_dat.M_strCMPCD_pbst+"RH"+strYRDGT_PRV;
			strHRXXPHY = "SYSHR"+cl_dat.M_strCMPCD_pbst+"PH"+strYRDGT_PRV;
			strHRXXCLQ = "SYSHR"+cl_dat.M_strCMPCD_pbst+"CLQ";
			hstLVDTL = new Hashtable<String,String[]>();
			hstHDRVAL= new Hashtable<String,String>(); 
			hstCDTRN = new Hashtable<String,String[]>();
			hstYOPDTL = new Hashtable<String,String>();
			hstCODDS = new Hashtable<String,String>();
			hstCDTRN.clear();
			hstCODDS.clear();
			
			crtCDTRN("'STSHRXXLVT','SYSHRXXLVE','"+strHRXXRHX+"','"+strHRXXPHX+"','"+strHRXXRHY+"','"+strHRXXPHY+"','"+strHRXXCLQ+"'","",hstCDTRN);
			crtCDTRN("'SYSCOXXDPT'"," and  cmt_codcd in (select distinct EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(flgALLDPT ? "" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','"+strHRXXXSN+"') "+(flgALLDPT ? "" : " and cmt_codcd like '_____"+strEMPNO_LOGIN+"'")+")")+")",hstCDTRN);
			crtCDTRN("'A"+cl_dat.M_strCMPCD_pbst+"HR"+cl_dat.M_strCMPCD_pbst+"LRC'",(flgALLDPT ? "" : " and cmt_codcd like '_____"+strEMPNO_LOGIN+"'"),hstCDTRN);
			crtCDTRN("'A"+cl_dat.M_strCMPCD_pbst+strHRXXXSN+"'",(flgALLDPT ? "" : " and  cmt_codcd like '_____"+strEMPNO_LOGIN+"'"),hstCDTRN);
			if(!flgALLDPT)
				lblDPTNM.setText(getCDTRN("SYSCOXXDPT"+txtDPTCD.getText(),"CMT_CODDS",hstCDTRN));
			
			String[] L_strTBLHD1 = {"","Leave","Year Opening","Current Balance","Pending Approval"};
			int[] L_intCOLSZ1 = {20,60,90,90,100};
			tblLEAVE = crtTBLPNL1(this,L_strTBLHD1,200,3,12,5,8,L_intCOLSZ1,new int[]{0});
			
			
			String[] L_strTBLHD2 = {"","Date","Day","Leave","Days","SrlNo"};
			int[] L_intCOLSZ2 = {20,70,40,50,50,35};
			tblDSTRB = crtTBLPNL1(this,L_strTBLHD2,200,10,1,8,6,L_intCOLSZ2,new int[]{0});
			add(lblTOTAL=new JLabel(),18,2,1,3, this,'L');
			add(lblDSPTT=new JLabel(),18,4,1,3, this,'L');
			
			add(new JLabel("Status : "),18,7,1,2, this,'L');
			//add(lblSTSFL=new JLabel(),18,9,1,1, this,'L');
			//add(lblSTSDS=new JLabel(),18,10,1,6, this,'L');
			
			lblTOTAL.setText("Total Days :");
			String[] L_strTBLHD3 = {"","App.Date","From","To","L.Type","NoOfDays","Reason","Recmnd By","App. By","Auth Status","Ref. Date"};
			int[] L_intCOLSZ3 = {20,70,70,70,50,50,100,90,90,90,10};
			tblTELVE = crtTBLPNL1(pnlHSTRY,L_strTBLHD3,200,1,1,8,15,L_intCOLSZ3,new int[]{0});
			
			String[] L_strTBLHD4 = {"","Emp No.","Name","App.Date","Start Date","Status"};
			int[] L_intCOLSZ4 = {20,70,100,90,90,100};
			tblAUTHR = crtTBLPNL1(pnlAUTHR,L_strTBLHD4,200,1,1,8,12,L_intCOLSZ4,new int[]{0});
			
			add(new JLabel("Visiting Place"),1,2,1,3,pnlTRDTY,'L');
			add(txtPLCNM = new TxtLimit(10),1,5,1,3,pnlTRDTY,'L');

			add(new JLabel("Visiting Organization"),1,8,1,3,pnlTRDTY,'L');
			add(txtORGNM = new TxtLimit(20),1,11,1,3,pnlTRDTY,'L');
			
			add(new JLabel("Mode Of Journey"),3,2,1,3,pnlTRDTY,'L');
			add(chkCOVEH = new JCheckBox("Co/Own Vehicle"),4,2,1,3,pnlTRDTY,'L');
			add(chkBUS = new JCheckBox("Bus"),4,5,1,2,pnlTRDTY,'L');
			add(chkTRAIN = new JCheckBox("Train"),4,7,1,2,pnlTRDTY,'L');
			add(chkAIR = new JCheckBox("Air"),4,9,1,2,pnlTRDTY,'L');
			add(chkHRVEH = new JCheckBox("Hired Vehicle"),4,11,1,3,pnlTRDTY,'L');
			
			objTBLVRF = new TBLINPVF();
			oINPVF=new INPVF();
			
			tblLEAVE.addKeyListener(this);
			tblLEAVE.setCellEditor(TB1_LVECD,txtLVECD1=new TxtLimit(2));
			tblLEAVE.setCellEditor(TB1_YOPXX,txtYOPXX =new TxtNumLimit(5.1));
			tblLEAVE.setCellEditor(TB1_YTDXX,txtYTDXX =new TxtNumLimit(5.1));
			tblLEAVE.setCellEditor(TB1_UAUXX,txtUAUXX =new TxtNumLimit(5.1));
			
			tblDSTRB.addKeyListener(this);
			tblDSTRB.setCellEditor(TB2_LVEDT,txtLVEDT =new TxtLimit(10));
			tblDSTRB.setCellEditor(TB2_LVEDY,txtLVEDY =new TxtLimit(3));
			tblDSTRB.setCellEditor(TB2_LVEQT,txtLVEQT =new TxtNumLimit(4.1));
			tblDSTRB.setCellEditor(TB2_LVECD,txtLVECD =new TxtLimit(2));
			tblDSTRB.setCellEditor(TB2_SRLNO,txtSRLNO =new TxtNumLimit(1));
			
			tblTELVE.addKeyListener(this);
			tblTELVE.addMouseListener(this);
			tblTELVE.setCellEditor(TB3_CHKFL,chkCHKFL3=new JCheckBox());
			tblTELVE.setCellEditor(TB3_REFDT,txtREFDT1=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_STRDT,txtSTRDT1=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_ENDDT,txtENDDT1=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_LVECD,txtLVECD2=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_LVEQT,txtLVEQT2=new TxtNumLimit(4.1));
			tblTELVE.setCellEditor(TB3_RSNDS,txtRSNDS1=new TxtLimit(100));
			tblTELVE.setCellEditor(TB3_RCMBY,txtRCMBY1=new TxtLimit(5));
			tblTELVE.setCellEditor(TB3_SCNBY,txtSCNBY1=new TxtLimit(5));
			tblTELVE.setCellEditor(TB3_STSDS,txtSTSFL1 =new TxtLimit(15));
			tblTELVE.setCellEditor(TB3_APPDT,txtAPPDT1=new TxtLimit(10));
			
			tblAUTHR.addKeyListener(this);
			tblAUTHR.setCellEditor(TB4_EMPNO,txtEMPNO1=new TxtLimit(4));
			tblAUTHR.setCellEditor(TB4_EMPNM,txtEMPNM=new TxtLimit(20));
			tblAUTHR.setCellEditor(TB4_REFDT,txtREFDT2=new TxtLimit(10));
			tblAUTHR.setCellEditor(TB4_STSDS,txtSSCFL1=new TxtLimit(10));
			tblAUTHR.setCellEditor(TB4_APPDT,txtAPPDT2=new TxtLimit(10));

			txtRSNDS.setInputVerifier(oINPVF);			
			txtDPTCD.setInputVerifier(oINPVF);			
			txtEMPNO.setInputVerifier(oINPVF);			
			txtAPPDT.setInputVerifier(oINPVF);			
			txtSTRDT.setInputVerifier(oINPVF);			
			txtENDDT.setInputVerifier(oINPVF);			
			txtRCMBY.setInputVerifier(oINPVF);			
			txtSCNBY.setInputVerifier(oINPVF);			
			txtLVECD3.setInputVerifier(oINPVF);			
			tblLEAVE.setInputVerifier(objTBLVRF);
			tblDSTRB.setInputVerifier(objTBLVRF);
			tblTELVE.setInputVerifier(objTBLVRF);
			tblAUTHR.setInputVerifier(objTBLVRF);
			txtLVECD.addKeyListener(this);
			txtLVEQT.addKeyListener(this);
			btnVERFY.addKeyListener(this);
			chkSSCAUT.setSelected(false);
			chkHDSTS.setSelected(false);
			chkHDSTS.setEnabled(false);
			txtLVEQT.addFocusListener(this);
			txtSRLNO.addFocusListener(this);
			((JCheckBox) tblTELVE.cmpEDITR[TB3_CHKFL]).addMouseListener(this);
			setENBL(false);
			cmpINIT();   	/** Initializes Componants with respect to the option selected	 */
			tbpMAIN.setEnabledAt(2,false);
			strHRXXXSN= "HR"+cl_dat.M_strCMPCD_pbst+"LSN";
			//System.out.println("strHRXXXSN>>"+strHRXXXSN);
		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"Constructor");
	    }
	}       // end of constructor

	hr_telve(JPanel LP_PANEL)
	{
	    super(2);
		try
		{
			if(cl_dat.M_strUSRCD_pbst.equals("SKS") || cl_dat.M_strUSRCD_pbst.equals("S_J") || cl_dat.M_strUSRCD_pbst.equals("PDP") || cl_dat.M_strUSRCD_pbst.equals("RPN"))
			   flgALLDPT = true;
		    setMatrix(20,20);
			//strEMPNO_LOGIN = getEMPNO(cl_dat.M_strUSRCD_pbst);
			add(new JLabel("Transaction Type"),1,1,1,2.5,LP_PANEL,'L');
			add(cmbTRNTP=new JComboBox(),1,3,1,3.5,LP_PANEL,'L');
			cmbTRNTP.addItem(strLEAVE_EN);
			cmbTRNTP.addItem(strTDUTY_EN);
			cmbTRNTP.addItem(strPLENC_EN);
			
			add(new JLabel("Department"),1,7,1,2,LP_PANEL,'L');
			add(txtDPTCD = new TxtLimit(3),1,9,1,1,LP_PANEL,'L');
			add(lblDPTNM=new JLabel(),1,10,1,2, LP_PANEL,'L');
			
			add(new JLabel("Employee Code"),1,12,1,2,LP_PANEL,'L');
			add(txtEMPNO = new TxtLimit(4),1,14,1,1,LP_PANEL,'L');
			add(lblEMPNM=new JLabel(),1,15,1,3,LP_PANEL,'L');
			
			add(new JLabel("Rcm.By."),1,18,1,2,LP_PANEL,'L');
			add(txtRCMBY = new TxtLimit(5),1,20,1,1,LP_PANEL,'L');

			add(new JLabel("Snc.By."),2,18,1,2,LP_PANEL,'L');
			add(txtSCNBY = new TxtLimit(5),2,20,1,1,LP_PANEL,'L');
			
			add(new JLabel("Designation"),2,1,1,2,LP_PANEL,'L');
			add(txtDESGN = new TxtLimit(10),2,3,1,2,LP_PANEL,'L');
			
			add(new JLabel("Grade"),2,7,1,2,LP_PANEL,'L');
			add(txtMMGRD = new TxtLimit(10),2,9,1,1,LP_PANEL,'L');

			add(new JLabel("Appl. Date"),3,1,1,2,LP_PANEL,'L');
			add(txtAPPDT = new TxtDate(),3,3,1,2,LP_PANEL,'L');
			
			add(new JLabel("No. Of. Days"),3,7,1,2,LP_PANEL,'L');
			add(txtLVEQT1 = new TxtNumLimit(4.1),3,9,1,1,LP_PANEL,'L');
			
			add(new JLabel("Start Date"),4,1,1,2,LP_PANEL,'L');
			add(txtSTRDT = new TxtDate(),4,3,1,2,LP_PANEL,'L');
			
			add(new JLabel("End Date"),4,7,1,2,LP_PANEL,'L');
			add(txtENDDT = new TxtDate(),4,9,1,2,LP_PANEL,'L');
			
			add(new JLabel("Contact No"),5,1,1,2,LP_PANEL,'L');
			add(txtCONNO = new TxtLimit(30),5,3,1,3,LP_PANEL,'L');
			
			add(new JLabel("Purpose"),6,1,1,2,LP_PANEL,'L');
			add(txtRSNDS = new TxtLimit(100),6,3,1,7,LP_PANEL,'L');
			
        	add(lblRSNDS3=new JLabel(),7,1,1,3,LP_PANEL,'L');
			add(txtRSNDS3 = new TxtLimit(100),7,4,1,7,LP_PANEL,'L');
			add(lblINFMN1 = new JLabel(""),7,7,2,4,LP_PANEL,'L');
			add(lblINFMN2 = new JLabel(""),8,7,2,4,LP_PANEL,'L');
			lblRSNDS3.setText("Reason For Cancellation");
			
			add(chkSSCAUT=new JCheckBox("Opt for Spl.Sanction", false),8,1,1,3,LP_PANEL,'L');
			add(chkHDSTS=new JCheckBox("Held for Discussion", false),8,4,1,3,LP_PANEL,'L');
			
			add(new JLabel("Leave Code"),9,2,1,3,LP_PANEL,'L');
			add(txtLVECD3 = new TxtLimit(2),9,4,1,1,LP_PANEL,'L');
			
			lblRSNDS3.setVisible(false);
			txtRSNDS3.setVisible(false);
			lblINFMN1.setVisible(false);
			lblINFMN2.setVisible(false);
			
			add(btnVERFY=new JButton("Verify"),9,5,1,2,LP_PANEL,'L');
			pnlHSTRY = new JPanel(null);
		    pnlAUTHR = new JPanel(null);			
			pnlTRDTY = new JPanel(null);			
			tbpMAIN = new JTabbedPane();
			add(tbpMAIN,8,7,10,15,LP_PANEL,'L');
			tbpMAIN.addTab("Leave History",pnlHSTRY);			
			tbpMAIN.addTab("Authorisation",pnlAUTHR);
			tbpMAIN.addTab(strTDUTY_EN,pnlTRDTY);
			if(!setEMPNO_LOGIN(cl_dat.M_strUSRCD_pbst))
				JOptionPane.showMessageDialog(null, "Base details for "+cl_dat.M_strUSRCD_pbst+"  Login are not available" , "Error Message", JOptionPane.ERROR_MESSAGE); 
				
			String L_strCMPCD = cl_dat.M_strCMPCD_pbst;
			strYRDGT_CUR = String.valueOf(Integer.parseInt(fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4))).substring(3,4);
			strYRDGT_PRV = String.valueOf(Integer.parseInt(fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4))-1).substring(3,4);
			//System.out.println(" strYRDGT_PRV : "+strYRDGT_PRV);
			strHRXXRHX = "SYSHR"+cl_dat.M_strCMPCD_pbst+"RH"+strYRDGT_CUR;
			strHRXXPHX = "SYSHR"+cl_dat.M_strCMPCD_pbst+"PH"+strYRDGT_CUR;
			strHRXXRHY = "SYSHR"+cl_dat.M_strCMPCD_pbst+"RH"+strYRDGT_PRV;
			strHRXXPHY = "SYSHR"+cl_dat.M_strCMPCD_pbst+"PH"+strYRDGT_PRV;
			strHRXXCLQ = "SYSHR"+cl_dat.M_strCMPCD_pbst+"CLQ";
			hstLVDTL = new Hashtable<String,String[]>();
			hstHDRVAL= new Hashtable<String,String>(); 
			hstCDTRN = new Hashtable<String,String[]>();
			hstYOPDTL = new Hashtable<String,String>();
			hstCODDS = new Hashtable<String,String>();
			hstCDTRN.clear();
			hstCODDS.clear();
			
			crtCDTRN("'STSHRXXLVT','SYSHRXXLVE','"+strHRXXRHX+"','"+strHRXXPHX+"','"+strHRXXRHY+"','"+strHRXXPHY+"','"+strHRXXCLQ+"'","",hstCDTRN);
			crtCDTRN("'SYSCOXXDPT'"," and  cmt_codcd in (select distinct EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(flgALLDPT ? "" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','"+strHRXXXSN+"') "+(flgALLDPT ? "" : " and cmt_codcd like '_____"+strEMPNO_LOGIN+"'")+")")+")",hstCDTRN);
			crtCDTRN("'A"+cl_dat.M_strCMPCD_pbst+"HR"+cl_dat.M_strCMPCD_pbst+"LRC'",(flgALLDPT ? "" : " and cmt_codcd like '_____"+strEMPNO_LOGIN+"'"),hstCDTRN);
			crtCDTRN("'A"+cl_dat.M_strCMPCD_pbst+strHRXXXSN+"'",(flgALLDPT ? "" : " and  cmt_codcd like '_____"+strEMPNO_LOGIN+"'"),hstCDTRN);
			if(!flgALLDPT)
				lblDPTNM.setText(getCDTRN("SYSCOXXDPT"+txtDPTCD.getText(),"CMT_CODDS",hstCDTRN));
			
			String[] L_strTBLHD1 = {"","Leave","Year Opening","Current Balance","Pending Approval"};
			int[] L_intCOLSZ1 = {20,60,90,90,100};
			tblLEAVE = crtTBLPNL1(LP_PANEL,L_strTBLHD1,200,3,12,5,8,L_intCOLSZ1,new int[]{0});
			
			
			String[] L_strTBLHD2 = {"","Date","Day","Leave","Days","SrlNo"};
			int[] L_intCOLSZ2 = {20,70,40,50,50,35};
			tblDSTRB = crtTBLPNL1(LP_PANEL,L_strTBLHD2,200,10,1,8,6,L_intCOLSZ2,new int[]{0});
			add(lblTOTAL=new JLabel(),18,2,1,3, LP_PANEL,'L');
			add(lblDSPTT=new JLabel(),18,4,1,3, LP_PANEL,'L');
			
			add(new JLabel("Status : "),18,7,1,2, LP_PANEL,'L');
			//add(lblSTSFL=new JLabel(),18,9,1,1, this,'L');
			//add(lblSTSDS=new JLabel(),18,10,1,6, this,'L');
			
			lblTOTAL.setText("Total Days :");
			String[] L_strTBLHD3 = {"","App.Date","From","To","L.Type","NoOfDays","Reason","Recmnd By","App. By","Auth Status","Ref. Date"};
			int[] L_intCOLSZ3 = {20,70,70,70,50,50,100,90,90,90,10};
			tblTELVE = crtTBLPNL1(pnlHSTRY,L_strTBLHD3,200,1,1,8,13,L_intCOLSZ3,new int[]{0});
			
			String[] L_strTBLHD4 = {"","Emp No.","Name","App.Date","Start Date","Status"};
			int[] L_intCOLSZ4 = {20,70,100,90,90,100};
			tblAUTHR = crtTBLPNL1(pnlAUTHR,L_strTBLHD4,200,1,1,8,13,L_intCOLSZ4,new int[]{0});
			
			add(new JLabel("Visiting Place"),1,2,1,3,pnlTRDTY,'L');
			add(txtPLCNM = new TxtLimit(10),1,5,1,3,pnlTRDTY,'L');

			add(new JLabel("Visiting Organization"),1,8,1,3,pnlTRDTY,'L');
			add(txtORGNM = new TxtLimit(20),1,11,1,3,pnlTRDTY,'L');
			
			add(new JLabel("Mode Of Journey"),3,2,1,3,pnlTRDTY,'L');
			add(chkCOVEH = new JCheckBox("Co/Own Vehicle"),4,2,1,3,pnlTRDTY,'L');
			add(chkBUS = new JCheckBox("Bus"),4,5,1,2,pnlTRDTY,'L');
			add(chkTRAIN = new JCheckBox("Train"),4,7,1,2,pnlTRDTY,'L');
			add(chkAIR = new JCheckBox("Air"),4,9,1,2,pnlTRDTY,'L');
			add(chkHRVEH = new JCheckBox("Hired Vehicle"),4,11,1,3,pnlTRDTY,'L');
			
			objTBLVRF = new TBLINPVF();
			oINPVF=new INPVF();
			
			tblLEAVE.addKeyListener(this);
			tblLEAVE.setCellEditor(TB1_LVECD,txtLVECD1=new TxtLimit(2));
			tblLEAVE.setCellEditor(TB1_YOPXX,txtYOPXX =new TxtNumLimit(5.1));
			tblLEAVE.setCellEditor(TB1_YTDXX,txtYTDXX =new TxtNumLimit(5.1));
			tblLEAVE.setCellEditor(TB1_UAUXX,txtUAUXX =new TxtNumLimit(5.1));
			
			tblDSTRB.addKeyListener(this);
			tblDSTRB.setCellEditor(TB2_LVEDT,txtLVEDT =new TxtLimit(10));
			tblDSTRB.setCellEditor(TB2_LVEDY,txtLVEDY =new TxtLimit(3));
			tblDSTRB.setCellEditor(TB2_LVEQT,txtLVEQT =new TxtNumLimit(4.1));
			tblDSTRB.setCellEditor(TB2_LVECD,txtLVECD =new TxtLimit(2));
			tblDSTRB.setCellEditor(TB2_SRLNO,txtSRLNO =new TxtNumLimit(1));
			
			tblTELVE.addKeyListener(this);
			tblTELVE.addMouseListener(this);
			tblTELVE.setCellEditor(TB3_CHKFL,chkCHKFL3=new JCheckBox());
			tblTELVE.setCellEditor(TB3_REFDT,txtREFDT1=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_STRDT,txtSTRDT1=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_ENDDT,txtENDDT1=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_LVECD,txtLVECD2=new TxtLimit(10));
			tblTELVE.setCellEditor(TB3_LVEQT,txtLVEQT2=new TxtNumLimit(4.1));
			tblTELVE.setCellEditor(TB3_RSNDS,txtRSNDS1=new TxtLimit(100));
			tblTELVE.setCellEditor(TB3_RCMBY,txtRCMBY1=new TxtLimit(5));
			tblTELVE.setCellEditor(TB3_SCNBY,txtSCNBY1=new TxtLimit(5));
			tblTELVE.setCellEditor(TB3_STSDS,txtSTSFL1 =new TxtLimit(15));
			tblTELVE.setCellEditor(TB3_APPDT,txtAPPDT1=new TxtLimit(10));
			
			tblAUTHR.addKeyListener(this);
			tblAUTHR.setCellEditor(TB4_EMPNO,txtEMPNO1=new TxtLimit(4));
			tblAUTHR.setCellEditor(TB4_EMPNM,txtEMPNM=new TxtLimit(20));
			tblAUTHR.setCellEditor(TB4_REFDT,txtREFDT2=new TxtLimit(10));
			tblAUTHR.setCellEditor(TB4_STSDS,txtSSCFL1=new TxtLimit(10));
			tblAUTHR.setCellEditor(TB4_APPDT,txtAPPDT2=new TxtLimit(10));

			txtRSNDS.setInputVerifier(oINPVF);			
			txtDPTCD.setInputVerifier(oINPVF);			
			txtEMPNO.setInputVerifier(oINPVF);			
			txtAPPDT.setInputVerifier(oINPVF);			
			txtSTRDT.setInputVerifier(oINPVF);			
			txtENDDT.setInputVerifier(oINPVF);			
			txtRCMBY.setInputVerifier(oINPVF);			
			txtSCNBY.setInputVerifier(oINPVF);			
			txtLVECD3.setInputVerifier(oINPVF);			
			tblLEAVE.setInputVerifier(objTBLVRF);
			tblDSTRB.setInputVerifier(objTBLVRF);
			tblTELVE.setInputVerifier(objTBLVRF);
			tblAUTHR.setInputVerifier(objTBLVRF);
			txtLVECD.addKeyListener(this);
			txtLVEQT.addKeyListener(this);
			btnVERFY.addKeyListener(this);
			chkSSCAUT.setSelected(false);
			chkHDSTS.setSelected(false);
			chkHDSTS.setEnabled(false);
			txtLVEQT.addFocusListener(this);
			txtSRLNO.addFocusListener(this);
			((JCheckBox) tblTELVE.cmpEDITR[TB3_CHKFL]).addMouseListener(this);
			setENBL(false);
			cmpINIT();   	/** Initializes Componants with respect to the option selected	 */
			tbpMAIN.setEnabledAt(2,false);
			strHRXXXSN= "HR"+cl_dat.M_strCMPCD_pbst+"LSN";
			cl_dat.M_cmbOPTN_pbst.setSelectedItem(cl_dat.M_OPAUT_pbst);
			//System.out.println("strHRXXXSN>>"+strHRXXXSN);
		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"Constructor1");
	    }
	}       // end of constructor

	/** Fetching Employee code for specified user login
	 */ 
	private boolean setEMPNO_LOGIN(String LP_USRCD)
	{   
		try
		{
				M_strSQLQRY = "select US_EMPCD from SA_USMST where US_USRCD='"+LP_USRCD+"'";
				System.out.println(M_strSQLQRY);
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					return false;
				strEMPNO_LOGIN = L_rstRSSET.getString("US_EMPCD");
				L_rstRSSET.close();
				//System.out.println("001  "+strEMPNO_LOGIN);
				M_strSQLQRY = "select EP_CMPCD,EP_DPTCD from HR_EPMST where ltrim(str(EP_EMPNO,20,0))='"+strEMPNO_LOGIN+"'";
				System.out.println(M_strSQLQRY);
				L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					return false;
				strCMPCD_LOGIN = L_rstRSSET.getString("EP_CMPCD");
				txtDPTCD.setText(L_rstRSSET.getString("ep_dptcd"));
				L_rstRSSET.close();

		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"setEMPNO_LOGIN");
	    }
		return true;
	}      

	
	/* super class Method overrided to enhance its functionality, to enable & disable components 
       according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{   
        super.setENBL(L_flgSTAT);
		try
		{
			tblLEAVE.setEnabled(false);
			tblTELVE.setEnabled(false);
		}
		catch(Exception E)
		{
			setMSG(E,"setENBL()");			
		}
	}
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(L_FE.getSource().equals(txtLVEQT))
				txtLVEQT.select(0,txtLVEQT.getText().length());				
			if(L_FE.getSource().equals(txtSRLNO))
				txtSRLNO.select(0,txtSRLNO.getText().length());
		}	
		catch(Exception E)
		{
			setMSG(E,"FocusGained");			
		}
	}

	
	void clrCOMP()
	{
		super.clrCOMP();
		try
		{
			lblEMPNM.setText("");
			lblDPTNM.setText("");
			lblDSPTT.setText("");
			txtDPTCD.setText("");
			txtRCMBY.setText("");
			txtSCNBY.setText("");
			txtAPPDT.setText("");
			txtLVEQT1.setText("");
			txtEMPNO.setText("");
			txtDESGN.setText("");
			txtMMGRD.setText("");
			txtSTRDT.setText("");
			txtENDDT.setText("");
			txtCONNO.setText("");
			txtPLCNM.setText("");
			txtORGNM.setText("");
			txtRSNDS.setText("");
			txtLVECD3.setText("");
			chkCOVEH.setSelected(false);
			chkBUS.setSelected(false);
			chkTRAIN.setSelected(false);
			chkAIR.setSelected(false);
			chkHRVEH.setSelected(false);
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
	
	void clrEDITR(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}	
	
	void clrCOMP1()
	{
		String L_strEMPNO=txtEMPNO.getText();
		String L_strAPPDT=txtAPPDT.getText();
		String L_strREFDT=txtSTRDT.getText();
		String L_strDPTCD=txtDPTCD.getText();
		super.clrCOMP();
		txtEMPNO.setText(L_strEMPNO);
		txtAPPDT.setText(L_strAPPDT);
		txtSTRDT.setText(L_strREFDT);
		txtDPTCD.setText(L_strDPTCD);
	}
	
	/** Gives Total of days entered into the table
	 */
	public void getTOTDS()  
	{
		try
		{
			float L_fltTOTAL=0;
			for(int i=0;i<tblDSTRB.getRowCount();i++)
				if(tblDSTRB.getValueAt(i,TB2_LVEDT).toString().length()>0)
				{
					L_fltTOTAL+=Float.parseFloat(tblDSTRB.getValueAt(i,TB2_LVEQT).toString());
				}	
			lblDSPTT.setText(""+L_fltTOTAL);
		}
		catch(Exception E)
		{
			setMSG(E,"getTOTDS()");			
		}
	}	
	
	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{	
			int i;
			try
			{
				if(((JCheckBox) tblAUTHR.cmpEDITR[TB4_CHKFL]).isSelected())
				{
					chkHDSTS.setSelected(false);
					for(i=0;i<tblAUTHR.getRowCount();i++)
					{
						if(tblAUTHR.getValueAt(i,TB2_CHKFL).toString().equals("true"))
						{
							if(i != P_intROWID)
							tblAUTHR.setValueAt(new Boolean(false),i,TB2_CHKFL);
						}
					}
					
					((JCheckBox) tblAUTHR.cmpEDITR[TB4_CHKFL]).setSelected(false);
					txtEMPNO.setText(tblAUTHR.getValueAt(P_intROWID,TB4_EMPNO).toString());
					txtAPPDT.setText(tblAUTHR.getValueAt(P_intROWID,TB4_APPDT).toString());
					txtSTRDT.setText(tblAUTHR.getValueAt(P_intROWID,TB4_REFDT).toString());
					//lblSTSDS.setText(tblAUTHR.getValueAt(P_intROWID,TB4_STSDS).toString());
					tbpMAIN.setSelectedIndex(0);
					lblINFMN1.setVisible(false);
					lblINFMN2.setVisible(false);
					getDSTRB();
					crtYOPDTL();
					setYOPDTL();
					getTELVE();
				}

				if(getSource()==tblDSTRB)
			    {
			        if(P_intCOLID == TB2_LVECD)
    			    {
						if(tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().length()>0)
						{	
							//if(hstCDTRN.containsKey("SYSHRXXLVE"+((JTextField)tblDSTRB.cmpEDITR[P_intCOLID]).toString().trim().toUpperCase())) 
							//System.out.println("Searching for :		SYSHRXXLVE"+tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase());
							if(!hstCDTRN.containsKey("SYSHRXXLVE"+tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase())) // Looking for valid and accountable leave
									{setMSG("Enter Valid Leave Code",'E');	return false;}
							//System.out.println(" CHP02 : "+getCDTRN("SYSHRXXLVE"+tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase(),"CMT_CHP02",hstCDTRN));
							if(!getCDTRN("SYSHRXXLVE"+tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase(),"CMT_CHP02",hstCDTRN).equals("1"))
									{setMSG("Enter Valid Leave Code",'E');	return false;}
							//System.out.println("Searching : "+strHRXXRHX+fmtYYYYMMDD.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWID,TB2_LVEDT).toString())));
							if(tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase().equals("RH"))
								if(!hstCDTRN.containsKey(strHRXXRHX+fmtYYYYMMDD.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWID,TB2_LVEDT).toString()))))
									{setMSG("There is No RH on this date",'E');	return false;}
							if(tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase().equals("PH"))
								if(!hstCDTRN.containsKey(strHRXXPHX+fmtYYYYMMDD.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWID,TB2_LVEDT).toString()))))
									{setMSG("There is No PH on this date",'E');	return false;}
							
							if(txtLVECD3.getText().trim().toUpperCase().equals("TD") || cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
							   if(!tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase().equals("TD"))
							   {
									setMSG("Please Enter Leave Code As TD (tour duty)",'E');	
									return false;
							   }
							if(!cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
							   if(tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase().equals("TD"))
							   {
									setMSG("Please Enter valid leave code (tour duty is not allowed)..",'E');	
									return false;
							   }
							tblDSTRB.setValueAt(tblDSTRB.getValueAt(P_intROWID,TB2_LVECD).toString().trim().toUpperCase(),P_intROWID,P_intCOLID);
						}
						setMSG("",'N');
    			    }
					if(P_intCOLID == TB2_LVEQT)
    			    {
						getTOTDS();
						if(Float.parseFloat(tblDSTRB.getValueAt(P_intROWID,TB2_LVEQT).toString()) == 0.5
						   && Integer.parseInt(tblDSTRB.getValueAt(P_intROWID,TB2_SRLNO).toString()) == 0)
						{
							tblDSTRB.setValueAt("2",P_intROWID,TB2_SRLNO);
						}	
					}	
				}
				else if(getSource()==tblTELVE)
			    {
			        if(P_intCOLID == TB3_CHKFL)
    			    {

						for(i=0;i<tblTELVE.getRowCount();i++)
						{
							if(tblTELVE.getValueAt(i,TB2_CHKFL).toString().equals("true"))
							{
								if(i != P_intROWID)
								tblTELVE.setValueAt(new Boolean(false),i,TB2_CHKFL);
							}
						}

						tblTELVE.setRowColor(P_intROWID,Color.BLACK);
						if(tblTELVE.getValueAt(P_intROWID,TB3_CHKFL).toString().equals("true") && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							tblTELVE.setRowColor(P_intROWID,Color.BLUE);
							txtSTRDT.setText(tblTELVE.getValueAt(P_intROWID,TB3_REFDT).toString()); 
							txtAPPDT.setText(tblTELVE.getValueAt(P_intROWID,TB3_APPDT).toString()); 
							//lblSTSDS.setText(tblTELVE.getValueAt(P_intROWID,TB3_STSDS).toString());
							exeDSPAPP(txtSTRDT.getText());
						}
							//{tblTELVE.setRowColor(P_intROWID,Color.BLUE);txtAPPDT.setText(tblTELVE.getValueAt(P_intROWID,TB3_APPDT).toString()); exeDSPAPP(txtAPPDT.getText());}
					}
				}

				if(M_rstRSSET != null)
					M_rstRSSET.close();
		
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"class TBLINPVF");
			}
			return true;
		}
    }
	
	/** Validate data entered by user, Format all text */
	
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
					
					if(!hstCDTRN.containsKey("SYSCOXXDPT"+txtDPTCD.getText().toString().trim()) && !flgALLDPT)
				   {
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
					else if(!flgALLDPT)
						lblDPTNM.setText(getCDTRN("SYSCOXXDPT"+txtDPTCD.getText().toString().trim(),"CMT_SHRDS",hstCDTRN));

					
				}	
				if(input == txtEMPNO)
				{
					M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,EP_DESGN,EP_MMGRD,EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and EP_LFTDT is null "+ (flgALLDPT ? "" : " AND EP_DPTCD = '"+txtDPTCD.getText()+"'  and EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','"+strHRXXXSN+"') "+(flgALLDPT ? "" : " and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"'")+") ")+" and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and ep_empno = '"+txtEMPNO.getText()+"'";
					//System.out.println("INPVF EMPNO : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
						txtDESGN.setText(M_rstRSSET.getString("EP_DESGN"));
						txtMMGRD.setText(M_rstRSSET.getString("EP_MMGRD"));
						txtDPTCD.setText(M_rstRSSET.getString("EP_DPTCD"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Employee Code",'E');
						return false;
					}
					setREFDT(txtEMPNO.getText());
					crtYOPDTL();
                    strYSTDT = "01/01/"+fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4);
	                strYENDT = "31/12/"+fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4);
					crtLVDTL(txtEMPNO.getText());
					setYOPDTL();
					getTELVE();
				}
				if(input == txtRCMBY)
				{
					
					M_strSQLQRY = "Select EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_LFTDT is null  AND EP_SHRNM = '"+txtRCMBY.getText().toUpperCase()+"' "+(flgALLDPT ? "" : " and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' "+(flgALLDPT ? "" : "and cmt_codcd like '"+txtEMPNO.getText().toUpperCase()+"%'")+")");
					M_strSQLQRY += "union Select EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1)+'.' from HR_EPMST where  EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_LFTDT is null AND EP_SHRNM = '"+txtRCMBY.getText()+"' "+(flgALLDPT ? "" : " and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = '"+strHRXXXSN+"' "+(flgALLDPT ? "" : " and cmt_codcd like '"+txtEMPNO.getText()+"%'")+")");
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtRCMBY.setText(txtRCMBY.getText().trim().toUpperCase());
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Reccomending Person",'E');
						return false;
					}
				}
				if(input == txtSCNBY)
				{
					M_strSQLQRY = " Select EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_LFTDT is null  AND EP_SHRNM = '"+txtSCNBY.getText().toUpperCase()+"' "+(flgALLDPT ? "" : " and EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = '"+strHRXXXSN+"'"+(flgALLDPT ? "" : " and cmt_codcd like '"+txtEMPNO.getText().toUpperCase()+"%'")+")");
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					//System.out.println(">>>>SCNBY>>>>"+M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtSCNBY.setText(txtSCNBY.getText().trim().toUpperCase());
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Approving Person",'E');
						return false;
					}
				}
				if(input == txtRSNDS)
				{
					if(input == txtRSNDS)
					{
						if(txtRSNDS.getText().contains("'"))
							txtRSNDS.setText(txtRSNDS.getText().replace("'","`"));
					}	
				}	
				
				if(input == txtLVECD3)
				{
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXLVE' and CMT_CODCD='"+txtLVECD3.getText().trim().toUpperCase()+"' and CMT_CHP02='1'";
					M_strSQLQRY += " order by CMT_CODCD";
        			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					//System.out.println(">>>>LVECD>>>>"+M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						txtLVECD3.setText(txtLVECD3.getText().trim().toUpperCase());
						if(!txtLVECD3.getText().equals("TD") && cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
						{
						 	setMSG("Please Enter Leave Code As TD (tour duty)",'E');	
						 	return false;
						}
						if(txtLVECD3.getText().equals("TD"))
						{	
							if(!cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
							{
								setMSG("Please Select Transaction Type as Tour Duty..",'E');
								return false;	
							}
							else
								setMSG("",'N');
						}
					}	
					else 
					{
						setMSG("Enter Valid Leave Code",'E');
						return false;	
					}
					dspLVECD();
				}
				if(input == txtAPPDT)
				{
					if(M_fmtLCDAT.parse(txtAPPDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("Application date can not be greater than Today's date..",'E');
						return false;
					}
					
				//	if(!exeDSPAPP(txtAPPDT.getText()))
				//		return false;
				}
				if(input == txtSTRDT)
				{
					if(!chkYOPDT(txtSTRDT.getText()))
					{
						return false;
					}	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						if(vldDAT(txtSTRDT.getText()))
						{
							setMSG("Application For The Given Date Already Exist..",'E');
							return false;
						}	
					if(!exeDSPAPP(txtSTRDT.getText()))
						return false;
					if(txtLVEQT1.getText().length()>0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						calENDDT();
				}
				if(input == txtENDDT)
				{
					if(!chkYOPDT(txtENDDT.getText()))
					{
						return false;
					}	
					if(txtSTRDT.getText().length()>0 && M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
					{
						setMSG("Invalid Date Range..",'E');
						return false;
					}
					if(txtSTRDT.getText().length()>0 && vldDAT(txtENDDT.getText()))
					{
						setMSG("Application For The Given Date Already Exist...",'E');
						return false;
					}
					if(txtSTRDT.getText().length()>0 && vldDATE1())
					{
						setMSG("Application For The Given Date Already Exist....",'E');
						return false;
					}
					
					if(txtSTRDT.getText().length()>0 && cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						getDSTRBADD();
						getTOTDS();
					//cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"class INPVF");		
			}
			return true;
		}
	}	
	

	private boolean chkYOPDT(String LP_DATE)
	{
		try
		{
			String L_strYOPYR="";
			M_strSQLQRY = " select EP_YOPDT from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_LFTDT is null AND ep_empno='"+txtEMPNO.getText()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				L_strYOPYR=M_fmtLCDAT.format(M_rstRSSET.getDate("EP_YOPDT")).substring(6,10);
				M_rstRSSET.close();
				if(M_fmtLCDAT.parse(LP_DATE).compareTo(M_fmtLCDAT.parse("01/01/"+L_strYOPYR))<0 ||
				   M_fmtLCDAT.parse(LP_DATE).compareTo(M_fmtLCDAT.parse("31/12/"+L_strYOPYR))>0)
				{
					setMSG("Leaves Only For Calendar Year "+L_strYOPYR+" are Allowed..",'E');				
					return false;							
				}	
			}
		}
		catch(Exception E)
		{
			System.out.println("Error in chkYOPDT"+E);
		}	
		return true;
	}	
	
	/** Displaying Leave application detail for Specified date
	 */
	private boolean exeDSPAPP(String LP_STRDT)
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			for(int i=0;i<tblTELVE.getRowCount();i++)
				if(tblTELVE.getValueAt(i,TB3_REFDT).toString().length()>0)
				{
					if(M_fmtLCDAT.parse(LP_STRDT).compareTo(M_fmtLCDAT.parse(tblTELVE.getValueAt(i,TB3_REFDT).toString()))==0
					   && !tblTELVE.getValueAt(i,TB3_STSDS).toString().equals("Cancelled"))
					{
						setMSG("Leave has been already applied during this period ...",'E');
						return false;
					}			
				}
			//java.util.Date L_datFSTDT=M_fmtLCDAT.parse(LP_STRDT);
			//java.util.Date L_datSNDDT;
				
			//M_calLOCAL.setTime(L_datFSTDT);      
			//M_calLOCAL.add(Calendar.DATE,1);    
			//L_datSNDDT = M_calLOCAL.getTime();
			//txtSTRDT.setText(M_fmtLCDAT.format(L_datSNDDT));
					
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) 
			|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)
			|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{	
				if(txtAPPDT.getText().length()>0)
					getDSTRB();
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeDSPAPP");
		}
		return true;
}
	
	/** Method to validate date range
	 */
	public boolean vldDATE1()
	{
		int i=0;
		try
		{
			for(i=0;i<tblTELVE.getRowCount();i++)
			if(tblTELVE.getValueAt(i,TB3_STRDT).toString().length()>0)
			{
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(tblTELVE.getValueAt(i,TB3_STRDT).toString()))<0 &&
				   M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(tblTELVE.getValueAt(i,TB3_ENDDT).toString()))>0 &&
				   !tblTELVE.getValueAt(i,TB3_STSDS).toString().equals("Cancelled"))
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
	public boolean vldDAT(String LP_DATE)
	{
		int i=0;
		try
		{
		for(i=0;i<tblTELVE.getRowCount();i++)
			if(tblTELVE.getValueAt(i,TB3_STRDT).toString().length()>0)
			{
				if(M_fmtLCDAT.parse(LP_DATE).compareTo(M_fmtLCDAT.parse(tblTELVE.getValueAt(i,TB3_STRDT).toString()))>=0 &&
				   M_fmtLCDAT.parse(LP_DATE).compareTo(M_fmtLCDAT.parse(tblTELVE.getValueAt(i,TB3_ENDDT).toString()))<=0 &&
				   !tblTELVE.getValueAt(i,TB3_STSDS).toString().equals("Cancelled"))
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
	
	/** sets value of leave code in the table respect to leave code entered in txtLVECD3 text box
	 */
	public void dspLVECD()
	{
		try
		{
			for(int i=0;i<tblDSTRB.getRowCount();i++)
				if(tblDSTRB.getValueAt(i,TB2_LVEDT).toString().length()>0)
				{
					if(tblDSTRB.getValueAt(i,TB2_LVECD).toString().length()==0)
					{
						tblDSTRB.setValueAt(txtLVECD3.getText(),i,TB2_LVECD);
					}
				}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"dspLVECD()");		
		}
	}
	
	/** method calculates end date by adding tota no. of days to starting date 
	 */
	public void calENDDT()
	{
		try
		{
			java.util.Date L_datSTRDT,L_datENDDT;
			float L_fltLVEQT=Float.parseFloat(txtLVEQT1.getText());
			int L_intLVEQT=(int) Math.ceil(L_fltLVEQT);
			L_datSTRDT=M_fmtLCDAT.parse(txtSTRDT.getText());
			M_calLOCAL.setTime(L_datSTRDT);      
			M_calLOCAL.add(Calendar.DATE,L_intLVEQT-1);    
			L_datSTRDT = M_calLOCAL.getTime();
			txtENDDT.setText(M_fmtLCDAT.format(L_datSTRDT));
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"calENDDT()");		
		}
	}
	
	
	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{

		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}
		
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        	{						
										//help for Department Code
        		if(M_objSOURC==txtDPTCD)		
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
        		    M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "+(flgALLDPT ? "" : " and  cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','"+strHRXXXSN+"') "+(flgALLDPT ? "" : " and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"'")+"))");
					
					/*Query for perticular DPTCD depending on user*/
					/*M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and cmt_codcd in ";
					M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=US_EMPCD)";
					*/
		   			//System.out.println(">>DPTCD>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
        								//help for Employee Category
				if(M_objSOURC==txtEMPNO)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
					String L_strDPTCHK = txtDPTCD.getText().length() == 3 ? " and EP_DPTCD = '"+txtDPTCD.getText()+"' " : "";
        			String L_ARRHDR[] = {"Emp No","Emp Name","Designation","Grade"};
        			M_strSQLQRY = "select EP_EMPNO,EP_LSTNM + ' '+ SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' EP_EMPNM,EP_DESGN,EP_MMGRD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+ (flgALLDPT ? L_strDPTCHK : " AND EP_DPTCD = '"+txtDPTCD.getText()+"'  and EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','"+strHRXXXSN+"') "+(flgALLDPT ? "" : " and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"'")+")")+" and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null order by ep_empno";
					//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,4,"CT");
        		}
				if(M_objSOURC==txtRCMBY)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtRCMBY";
        			String L_ARRHDR[] = {"Code","Name"};
					M_strSQLQRY = "Select EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(flgALLDPT ? "" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' "+(flgALLDPT ? "" : " and cmt_codcd like '"+txtEMPNO.getText()+"%'")+")");
					M_strSQLQRY += "union Select EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) + '.' from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and EP_LFTDT is null "+(flgALLDPT ? "" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = '"+strHRXXXSN+"'"+(flgALLDPT ? "" : " and cmt_codcd like '"+txtEMPNO.getText()+"%'")+")");
					System.out.println(">>>>RCMBY>>>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				if(M_objSOURC==txtSCNBY)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtSCNBY";
        			String L_ARRHDR[] = {"Code","Name"};
					M_strSQLQRY = " Select EP_SHRNM,EP_LSTNM + ' ' + SUBSTRING(EP_FSTNM,1,1) + '.' + SUBSTRING(EP_MDLNM,1,1) +'.' from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ep_lftdt is null "+(flgALLDPT ? "" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,6,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = '"+strHRXXXSN+"'"+(flgALLDPT ? "" : " and cmt_codcd like '"+txtEMPNO.getText()+"%'")+")");
					System.out.println(">>>>SCNBY>>>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				if(M_objSOURC==txtLVECD3)		
        		{
        			M_strHLPFLD = "txtLVECD3";	
        		    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXLVE' and CMT_CHP02='1'";
					M_strSQLQRY += " order by CMT_CODCD";
					//System.out.println(">>>>LVECD>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Leave Code", "Leave Discription"},2,"CT",new int[]{107,400});
        		}
				if(M_objSOURC == tblDSTRB.cmpEDITR[TB2_LVECD])
    			{
        		    M_strHLPFLD = "txtLVECD";	
        		    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXLVE' and CMT_CHP02='1'";
					M_strSQLQRY += " order by CMT_CODCD";
					//System.out.println(">>>>LVECD>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Leave Code", "Leave Discription"},2,"CT",new int[]{107,400});
    			}

			}
			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
        	{
				if(M_objSOURC==txtDPTCD)
					txtEMPNO.requestFocus();
				else if(M_objSOURC==txtEMPNO)
				{
					setRCMSSN();
					txtAPPDT.requestFocus();
				}
				else if(M_objSOURC==txtAPPDT)
				{	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) ||
					   cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) ||
					   cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			
						txtSTRDT.requestFocus();
					else
						txtLVEQT1.requestFocus();
				}	
				else if(M_objSOURC==txtLVEQT1)
				{	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					   txtCONNO.requestFocus();
					else
						txtSTRDT.requestFocus();
				}	
				else if(M_objSOURC==txtCONNO)
				{	
				   txtRSNDS.requestFocus();
				}
				else if(M_objSOURC==txtSTRDT)
				{	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					   txtRSNDS3.requestFocus();
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					   txtCONNO.requestFocus();
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					   txtCONNO.requestFocus();
					else
						txtENDDT.requestFocus();
				}	
				else if(M_objSOURC==txtENDDT)
					txtCONNO.requestFocus();
				else if(M_objSOURC==txtRSNDS)
					txtLVECD3.requestFocus();
				else if(M_objSOURC==txtLVECD3)
					btnVERFY.requestFocus();

				else if(M_objSOURC==txtRCMBY)
					txtSCNBY.requestFocus();
				else if(M_objSOURC==txtSCNBY)
					txtCONNO.requestFocus();
				else if(M_objSOURC==txtPLCNM)
					txtORGNM.requestFocus();
			}

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
	/** Putting default values in Recommended By, Sanctione By entry box
	 */
	private void setRCMSSN()
	{
		ResultSet L_rstRSSET = null;
		try
		{
			if(!flgALLDPT && hstCDTRN.containsKey("AUTHR"+cl_dat.M_strCMPCD_pbst+"LRC"+txtEMPNO.getText()+"_"+strEMPNO_LOGIN))
				txtRCMBY.setText(getCDTRN("AUTHR"+cl_dat.M_strCMPCD_pbst+"LRC"+txtEMPNO.getText()+"_"+strEMPNO_LOGIN,"CMT_CCSVL",hstCDTRN));
			else
			{
				//M_strSQLQRY = "select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC' "+(flgALLDPT ? "" : " and substr(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'");
				M_strSQLQRY = "select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"LRC'  and SUBSTRING(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'";
				L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				//System.out.println("M_strSQLQRY1>>"+M_strSQLQRY);
				if(L_rstRSSET != null && L_rstRSSET.next())
				{
					//if(txtRCMBY.getText().length()!=3)
						txtRCMBY.setText(L_rstRSSET.getString("cmt_ccsvl"));
						L_rstRSSET.close();
				}
			}
			if(!flgALLDPT && hstCDTRN.containsKey("AUT"+strHRXXXSN+txtEMPNO.getText()+"_"+strEMPNO_LOGIN))
				txtSCNBY.setText(getCDTRN("AUT"+strHRXXXSN+txtEMPNO.getText()+"_"+strEMPNO_LOGIN,"CMT_CCSVL",hstCDTRN));
			else
			{
				//M_strSQLQRY = "select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = '"+strHRXXXSN+"'"+(flgALLDPT ? "" : " and substr(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'");
				M_strSQLQRY = "select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp = '"+strHRXXXSN+"' and SUBSTRING(cmt_codcd,1,4)='"+txtEMPNO.getText()+"'";
				L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				//System.out.println("M_strSQLQRY2>>"+M_strSQLQRY);
				if(L_rstRSSET != null && L_rstRSSET.next())
				{
					//if(txtSCNBY.getText().length()!=3)
						txtSCNBY.setText(L_rstRSSET.getString("cmt_ccsvl"));
						L_rstRSSET.close();
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setRCMSSC");
		}
	}
	
		/* method for Help*/	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDPTCD.setText(L_STRTKN.nextToken());
				lblDPTNM.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNO.setText(L_STRTKN.nextToken());
				lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
				txtDESGN.setText(L_STRTKN.nextToken());
				txtMMGRD.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtRCMBY"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtRCMBY.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtSCNBY"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtSCNBY.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtLVECD3"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtLVECD3.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtLVECD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtLVECD.setText(L_STRTKN.nextToken());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}

	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{	
			if(tbpMAIN.getSelectedIndex()==1)
			{	
				lblINFMN1.setVisible(true);
				lblINFMN2.setVisible(true);
			}	
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{	
				tbpMAIN.setSelectedIndex(0);
				tbpMAIN.setEnabledAt(2,false);
				lblINFMN1.setVisible(false);
				lblINFMN2.setVisible(false);
				txtDPTCD.requestFocus();
				tblLEAVE.clrTABLE();
				tblDSTRB.clrTABLE();
				tblTELVE.clrTABLE();
				tblAUTHR.clrTABLE();
				clrEDITR(tblLEAVE);
				clrEDITR(tblDSTRB);
				clrEDITR(tblTELVE);
				clrEDITR(tblAUTHR);
				txtRSNDS3.setVisible(false);
				lblRSNDS3.setVisible(false);
				txtDPTCD.setText(strDPTCD);
				lblDPTNM.setText(strDPTNM);
				setALLCOMPS();
				chkHDSTS.setSelected(false);
				chkHDSTS.setEnabled(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					clrCOMP();
					setENBL(false);			
					((JCheckBox)tblAUTHR.cmpEDITR[TB4_CHKFL]).setEnabled(true);
					chkHDSTS.setSelected(false);
					chkHDSTS.setEnabled(true);
					tbpMAIN.setSelectedIndex(1);
					lblINFMN1.setVisible(true);
					lblINFMN2.setVisible(true);
					getAUTHR();
				}
				tblTELVE.setEnabled(false);
				tblLEAVE.setEnabled(false);
				tblAUTHR.setEnabled(false);
				((JCheckBox)tblTELVE.cmpEDITR[TB3_CHKFL]).setEnabled(true);
				((JCheckBox)tblDSTRB.cmpEDITR[TB2_CHKFL]).setEnabled(true);
				((JCheckBox)tblAUTHR.cmpEDITR[TB2_CHKFL]).setEnabled(true);
				((JTextField)tblDSTRB.cmpEDITR[TB2_LVEDT]).setEditable(false);
				((JTextField)tblDSTRB.cmpEDITR[TB2_LVEDY]).setEditable(false);
				txtDESGN.setEnabled(false);
				txtMMGRD.setEnabled(false);
			}
			if(M_objSOURC == cmbTRNTP)
		    {
				tbpMAIN.setSelectedIndex(0);
				flgPLENC_EN = false;
				if(cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
				{
					tbpMAIN.setEnabledAt(2,true);
					txtLVECD3.setText("TD");
					strHRXXXSN= "HR"+cl_dat.M_strCMPCD_pbst+"TSN";
					txtSTRDT.setEnabled(true);
					txtENDDT.setEnabled(true);
					//System.out.println("strHRXXXSN>>>"+strHRXXXSN);
				}
				else if(cmbTRNTP.getSelectedItem().equals(strPLENC_EN))
				{
					flgPLENC_EN = true;
					tbpMAIN.setEnabledAt(2,false);
					txtLVECD3.setText("PE");
					strHRXXXSN= "HR"+cl_dat.M_strCMPCD_pbst+"TSN";
					txtSTRDT.setEnabled(false);
					txtENDDT.setEnabled(false);
					//System.out.println("strHRXXXSN>>>"+strHRXXXSN);
				}
				else
				{
					tbpMAIN.setEnabledAt(2,false);
					txtLVECD3.setText("");
					strHRXXXSN= "HR"+cl_dat.M_strCMPCD_pbst+"LSN";
					txtSTRDT.setEnabled(true);
					txtENDDT.setEnabled(true);
					//System.out.println("strHRXXXSN>>>"+strHRXXXSN);
				}
				if(txtEMPNO.getText().trim().length()>0)
					setRCMSSN();			
			}
			if(M_objSOURC == btnVERFY)   //// button verifies data entered
			{	
				if(!vldDATA())
				{
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
				if(!exeVERIFY())
					return;
			}	
			
			if(txtDPTCD.getText().length()==0 && !flgALLDPT)
				lblDPTNM.setText("");	
			if(txtEMPNO.getText().length()==0)
			{	
				lblEMPNM.setText("");
				txtDESGN.setText("");
				txtMMGRD.setText("");
			}
		}	
	    catch(Exception L_EA)
	    {
	        setMSG(L_EA,"Action Performed");
	    }
	}
	
	/** sets all components as decided.
	 * whenever called.
	 */
	private void setALLCOMPS()
	{
		try{
			txtDPTCD.requestFocus();
			tblLEAVE.setEnabled(false);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				setENBL(true);
				txtRCMBY.setText(strRCMBY);	
				txtSCNBY.setText(strSCNBY);	
				txtAPPDT.setText(strREFDT);	
				//txtSTRDT.setText(strREFDT);
			}	
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				setENBL(false);
				txtDPTCD.setEnabled(true);
				txtEMPNO.setEnabled(true);
				txtSTRDT.setEnabled(true);
				txtAPPDT.setEnabled(true);
			}   
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
				setENBL(false);
				txtRSNDS3.setVisible(true);
				lblRSNDS3.setVisible(true);
				txtDPTCD.setEnabled(true);
				txtEMPNO.setEnabled(true);
				txtAPPDT.setEnabled(true);
				txtSTRDT.setEnabled(true);
				txtRSNDS3.setEnabled(true);
			}	
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 	
			{
				setENBL(true);
				//txtSTRDT.setEnabled(false);
				txtENDDT.setEnabled(false);
			}
				((JCheckBox)tblTELVE.cmpEDITR[TB3_CHKFL]).setEnabled(true);
				((JCheckBox)tblDSTRB.cmpEDITR[TB2_CHKFL]).setEnabled(true);
				((JCheckBox)tblAUTHR.cmpEDITR[TB2_CHKFL]).setEnabled(true);			
		}
		catch(Exception E)
		{
			System.out.println("Inside setALLCOMPS :"+E);
		}	
	}
			   
	/** Verify whether entered data is correct
	 */
	public boolean exeVERIFY()
	{
		try
		{
			int i=0;
			strVERFY_VLD="";
			strVERFY_AUT="";
			strVERFY_SSC="";
			crtLVDTL(txtEMPNO.getText());
			crtLVDTL_ENT();
			setHST_ARR(hstLVDTL);			
			/**	Leaves must be available for each Leave code
			 */
			if(chkCNTLVE("CL",cntCL))
				setADDMSG("Available Balance for CL is Less than Required","VLD");
			
			if(chkCNTLVE("PL",cntPL))
				setADDMSG("Available Balance for PL is Less than Required","VLD");
			
			if(chkCNTLVE("RH",cntRH))
				setADDMSG("Available Balance for RH is Less than Required","VLD");
			
			if(chkCNTLVE("SL",cntSL))
				setADDMSG("Available Balance for SL is Less than Required","VLD");

			/**	No. of Days Should match with Total Days
			 */
			if(txtLVEQT1.getText().length()>0 && Double.parseDouble(txtLVEQT1.getText())!=Double.parseDouble(lblDSPTT.getText()))
				setADDMSG("No. of Days does not match with Total Days","VLD");
			
			/**	Leave Quantity must be 0.5 or 1.0
			 */
				for(i=0;i<tblDSTRB.getRowCount();i++)
				{
					if(tblDSTRB.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					{
						if(Float.parseFloat(tblDSTRB.getValueAt(i,TB2_LVEQT).toString())!=1 &&
						   Float.parseFloat(tblDSTRB.getValueAt(i,TB2_LVEQT).toString())!=0.5)
						{
							setADDMSG("Leave Quantity must be 0.5 or 1.0 for "+tblDSTRB.getValueAt(i,TB2_LVEDT).toString(),"VLD");
							break;
						}
					}
				}
			
			/**	Serial No. must be 0 if days=1 o/w 1/2 for 0.5
			 */
			for(i=0;i<tblDSTRB.getRowCount();i++)
			{
				if(tblDSTRB.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					if(Float.parseFloat(tblDSTRB.getValueAt(i,TB2_LVEQT).toString())==1 && 
					   Integer.parseInt(tblDSTRB.getValueAt(i,TB2_SRLNO).toString())!=0)
						{
							setADDMSG("Serial No. Must Be 0 for Full Day for "+tblDSTRB.getValueAt(i,TB2_LVEDT).toString(),"VLD");
							break;
						}
					if(Float.parseFloat(tblDSTRB.getValueAt(i,TB2_LVEQT).toString())==0.5)
					{
						if(!(Integer.parseInt(tblDSTRB.getValueAt(i,TB2_SRLNO).toString())==1 || Integer.parseInt(tblDSTRB.getValueAt(i,TB2_SRLNO).toString())==2))
						{	
							setADDMSG("Serial No. Must Be 1(1st Half) or 2(2nd Half) for half day for "+tblDSTRB.getValueAt(i,TB2_LVEDT).toString(),"VLD");
							break;
						}
					}
				}
			}
			
			/** page4-2  CL must be < 3
			 */
			if(getOCCR_APP("CL")>3)
				setADDMSG("More Than 3 CL's are not Allowed","VLD");
			
			/** page10-8 PL must be >3
			 */
			int L_intTOTPL_APP = getOCCR_APP("PL");
			if(L_intTOTPL_APP>0 && L_intTOTPL_APP<3)
				setADDMSG("Less Than 3 PL's are not Allowed","VLD");

			/**	page4-7 && page6-2 CL will not be allowed to be combined with any other leave
			 *					   SL will not be allowed to be combined with CL	
			 */
			
			chkCLUB("CL","PL","VLD1");
			chkCLUB("CL","SL","VLD1");
			chkLVECD_APP("VLD");
			
			
			/** Casual Leaves must be continuous
			 */
			if(getOCCR_APP("CL")!=getOCCR_SSN("CL"))
					setADDMSG("CL's must be Continuous","VLD");
			if(getOCCR_APP("SL")!=getOCCR_SSN("SL"))
					setADDMSG("SL's must be Continuous","VLD");
			if(getOCCR_APP("PL")!=getOCCR_SSN("PL"))
					setADDMSG("PL's must be Continuous","VLD");
				
			int intCONFIRM; 
			
			/**page4-5 Paid Holidays , Weekly Offs , Restricted holidays or compensatory offs can be 
			 * either prefixed or suffixed to Leave at the sole descretion of leave sanctioning authority.
			 */
			chkPFXSFX("CL","RH","SSC");
			chkPFXSFX("CL","PH","SSC");
			
			/**page4-6 No SandWitch leave
			 */
			chkSNDWH_CL("VLD1");
			
			if(!strVERFY_SSC.equals(""))
			{	
				setADDMSG("Special Sanction Required.... Do U Want To Continue?","SSC");						
				intCONFIRM=JOptionPane.showConfirmDialog(null,strVERFY_SSC, "Warning", JOptionPane.YES_NO_OPTION);
				if(intCONFIRM==0)
				{	
					intLVT_STSFL=9;
					intLVT_SSCFL=1;
				}	
				else
				{	
					intLVT_STSFL=0;
					intLVT_SSCFL=0;
				}
			}
			if(!strVERFY_VLD.equals(""))
			{	
				JOptionPane.showMessageDialog(null, strVERFY_VLD, "Verify", JOptionPane.ERROR_MESSAGE); 
				return false;
			}	

			if(strVERFY_VLD.equals("") && strVERFY_SSC.equals(""))
				setMSG("Verification Successful",'N');
			strVERFY_SSC="";
			strVERFY_VLD="";
		}	
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"exeVERIFY");
	    }
		return true;
	}	
	
	
	/**	this method checks for blank values in the tblDSTRB
	*/	
	public boolean chkNULL(int LP_COL)
	{
		try
		{
			int i=0;
			for(i=0;i<tblDSTRB.getRowCount();i++)
			{
				if(tblDSTRB.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					if(tblDSTRB.getValueAt(i,LP_COL).toString().length()==0)
					{
						return true;
					}	
				}
			}
			return false;
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkNULL()");
	    }
		return false;
	}
	
	/** find out count of required leaves entered and available balance of leaves
	 	and accordingly returns result if balance is available
	 */
	public boolean chkCNTLVE(String LP_LVECD,int LP_balDSTRB)
	{
		try
		{
			float L_cntREQRD=0;
			float L_cntAVAIL=0;
			int i=0;
			for(i=0;i<tblDSTRB.getRowCount();i++)
			{
				if(tblDSTRB.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					if(tblDSTRB.getValueAt(i,TB2_LVECD).toString().equals(LP_LVECD))
					{
						L_cntREQRD+=Float.parseFloat(tblDSTRB.getValueAt(i,TB2_LVEQT).toString());
					}	
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{	
				for(i=0;i<tblLEAVE.getRowCount();i++)
				{
					if(tblLEAVE.getValueAt(i,TB1_LVECD).toString().equals(LP_LVECD))
					{
						L_cntAVAIL=Float.parseFloat(tblLEAVE.getValueAt(i,TB1_YTDXX).toString())-Float.parseFloat(tblLEAVE.getValueAt(i,TB1_UAUXX).toString());
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) ||
			   cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{	
				for(i=0;i<tblLEAVE.getRowCount();i++)
				{
					if(tblLEAVE.getValueAt(i,TB1_LVECD).toString().equals(LP_LVECD))
					{
						L_cntAVAIL=Float.parseFloat(tblLEAVE.getValueAt(i,TB1_YTDXX).toString())-Float.parseFloat(tblLEAVE.getValueAt(i,TB1_UAUXX).toString())+LP_balDSTRB;
					}
				}
			}
			if(L_cntREQRD > L_cntAVAIL)
				return true;
			else		
				return false;
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkCNTLVE()");
	    }
		return false;
	}

	/**  checks for Continuous Leave Code
	 */
	public boolean chkCONTIGS(String LP_LCOD,int LP_CNT)
	{
		try
		{
			int i=0;
			int L_CNT=1;
			String L_strRETVL="";
			for(i=0;i<tblDSTRB.getRowCount()-1;i++)
			{
				if(tblDSTRB.getValueAt(i+1,TB2_CHKFL).toString().equals("true"))
				{
					if(tblDSTRB.getValueAt(i,TB2_LVECD).toString().equals(LP_LCOD))
					{	
						if(tblDSTRB.getValueAt(i+1,TB2_LVECD).toString().equals(LP_LCOD))
						{	
							L_CNT++;						
						}
						else
						{
							if(LP_CNT!=L_CNT)
							return true;
						}	
					}
				}	
			}
			return false;
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkCONTIGS()");
	    }
		return false;
	}

	
	
	/**  checks for Prefix / Suffix category attached to the leave
	 */
	public void chkSNDWH_CL(String LP_VLDTP)
	{
		try
		{
			String L_strLVECD_SW = "CL";
			String L_strLVDTL_STR = getLVDTL_STR();
			String L_strLVDTL_END = getLVDTL_END();
			int L_intRECCT = 0;
			String L_strSWCAT1 = "RH";
			String L_strSWCAT2 = "PH";
			String L_strSWCAT3 = "WO";
			String L_LV01 = "", L_LV02 = "";
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_STR))<0)
					continue;
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_END))>0)
					continue;
				L_intRECCT +=1;
				if(L_intRECCT < 3)
					continue;
				if(!getLVDTL(arrHSTKEY[i-1].toString(),"LVT_LVECD").equals(L_strLVECD_SW))
					continue;
				L_LV01 = getLVDTL(arrHSTKEY[i-2].toString(),"LVT_LVECD");
				L_LV02 = getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD");
				if((L_LV01.equals(L_strSWCAT1) || L_LV01.equals(L_strSWCAT2) || L_LV01.equals(L_strSWCAT3)) && (L_LV02.equals(L_strSWCAT1) || L_LV02.equals(L_strSWCAT2) || L_LV02.equals(L_strSWCAT3)))
					setADDMSG(L_strLVECD_SW+" Sandwitched on "+ M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i-1].toString()))+" with "+L_LV01+" and "+L_LV02,LP_VLDTP);
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkPFXSFX()");
	    }
	}
	

	/**  checks for Prefix / Suffix category attached to the leave
	 */
	public void chkPFXSFX(String LP_LCOD1,String LP_LCOD2,String LP_VLDTP)
	{
		try
		{
			String L_strLVDTL_STR = getLVDTL_STR();
			String L_strLVDTL_END = getLVDTL_END();
			int L_intRECCT = 0;
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_STR))<0)
					continue;
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_END))>0)
					continue;
				L_intRECCT +=1;
				if(L_intRECCT < 2)
					continue;
				if(getLVDTL(arrHSTKEY[i-1].toString(),"LVT_LVECD").equals(LP_LCOD1) && getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LCOD2))
					setADDMSG(LP_LCOD2+" Suffixed to "+LP_LCOD1+" on "+ M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString())),LP_VLDTP);
				if(getLVDTL(arrHSTKEY[i-1].toString(),"LVT_LVECD").equals(LP_LCOD2) && getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LCOD1))
					setADDMSG(LP_LCOD2+" Prefixed to "+LP_LCOD1+" on "+ M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString())),LP_VLDTP);
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkPFXSFX()");
	    }
	}
	

		/**  checks for Prefix / Suffix category attached to the leave
	 */
	public void setADDMSG(String LP_MSGDS,String LP_VLDTP)
	{
		try
		{
			if(LP_VLDTP.equals("SSC"))
			   strVERFY_SSC += LP_MSGDS+"\n";
			else if(LP_VLDTP.equals("AUT"))
			   strVERFY_AUT += LP_MSGDS+"\n";
			else if(LP_VLDTP.equals("VLD"))
			   strVERFY_VLD += LP_MSGDS+"\n";
			else if(LP_VLDTP.equals("VLD1") && chkSSCAUT.isSelected())
			   strVERFY_SSC += LP_MSGDS+"\n";
			else if(LP_VLDTP.equals("VLD1") && !chkSSCAUT.isSelected())
			   strVERFY_VLD += "***"+LP_MSGDS+"*** \n";
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"setADDMSG");
	    }
	}


	
	/**  Returns leave count within application
	 */
	public int getOCCR_APP(String LP_LVECD)
	{
		int L_intRECCT = 0;
		try
		{
			String L_strLVDTL_STR = getLVDTL_STR();
			String L_strLVDTL_END = getLVDTL_END();
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_STR))<0)
					continue;
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_END))>0)
					continue;
				if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LVECD))
					L_intRECCT += 1;
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getOCCR_APP()");
	    }
		return L_intRECCT;
	}


	
	
	/**  Validates Leave Codes within the application
	 */
	public int chkLVECD_APP(String LP_VLDTP)
	{
		int L_intRECCT = 0;
		try
		{
			String L_strLVDTL_STR = getLVDTL_STR();
			String L_strLVDTL_END = getLVDTL_END();
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_STR))<0)
					continue;
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_END))>0)
					continue;
				if(!getCDTRN("SYSHRXXLVE"+getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD"),"CMT_CHP02",hstCDTRN).equals("1"))
					setADDMSG("Invalid leave code "+getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD")+" Entered on "+M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString())),LP_VLDTP);
				if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals("RH") || getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals("PH"))
				   if(!hstCDTRN.containsKey("SYSHR"+cl_dat.M_strCMPCD_pbst+getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD")+strYRDGT_CUR+arrHSTKEY[i].toString()) && !hstCDTRN.containsKey("SYSHR"+cl_dat.M_strCMPCD_pbst+getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD")+strYRDGT_PRV+arrHSTKEY[i].toString()))
						setADDMSG("There is No "+getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD")+" declared on "+M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString())),LP_VLDTP);
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getOCCR_APP()");
	    }
		return L_intRECCT;
	}
	
	
	/**  Returns leave count in succession within application
	 */
	public int getOCCR_SSN(String LP_LVECD)
	{
		int L_intRECCT = 0;
		try
		{
			String L_strLVDTL_STR = getLVDTL_STR();
			String L_strLVDTL_END = getLVDTL_END();
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_STR))<0)
					continue;
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_END))>0)
					continue;
				if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LVECD))
					L_intRECCT += 1;
				else if(L_intRECCT>0)
					break;
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getOCCR_APP()");
	    }
		return L_intRECCT;
	}
	
	
	
	/**  Returns leave count overall
	 */
	public int getOCCR_ALL(String LP_LVECD)
	{
		int L_intRECCT = 0;
		try
		{
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LVECD))
					L_intRECCT += 1;
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getOCCR_ALL()");
	    }
		return L_intRECCT;
	}
	
	
	/**  checks for combination of leave codes within the application
	 */
	public void chkCOMB(String LP_LCOD1,String LP_LCOD2,String LP_VLDTP)
	{
		try
		{
			int i=0;
			String L_strLVDTL_STR = getLVDTL_STR();
			String L_strLVDTL_END = getLVDTL_END();
			boolean L_flgLCOD1 = false;
			boolean L_flgLCOD2 = false;
			String L_strDAT1 = "";
			String L_strDAT2 = "";
			for(i=0;i<arrHSTKEY.length;i++)
			{
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_STR))<0)
					continue;
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_END))>0)
					continue;
				if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LCOD1))
					{L_flgLCOD1 = true; L_strDAT1 = M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()));}
				if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LCOD2))
					{L_flgLCOD2 = true; L_strDAT2 = M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()));}
			}
			if(L_flgLCOD1 && L_flgLCOD2)
				setADDMSG("  "+LP_LCOD1+ "  and "+LP_LCOD2+ "  used in Combination on "+L_strDAT1+"  and "+L_strDAT2,LP_VLDTP);
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkCOMB()");
	    }
	}

	
	/**  checks for clubbing of leave within Application
	 */
	public void chkCLUB(String LP_LCOD1,String LP_LCOD2,String LP_VLDTP)
	{
		try
		{
			String L_strLVDTL_STR = getLVDTL_STR();
			String L_strLVDTL_END = getLVDTL_END();
			int L_intRECCT = 0;
			for(int i=0;i<arrHSTKEY.length;i++)
			{
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_STR))<0)
					continue;
				if(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).compareTo(fmtYYYYMMDD.parse(L_strLVDTL_END))>0)
					continue;
				L_intRECCT += 1;
				if(L_intRECCT < 2)
					continue;
				if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LCOD1) && getLVDTL(arrHSTKEY[i-1].toString(),"LVT_LVECD").equals(LP_LCOD2))
					setADDMSG("  "+LP_LCOD1+ "  and "+LP_LCOD2+ "  clubbed on  "+M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString())),LP_VLDTP);
				else if(getLVDTL(arrHSTKEY[i].toString(),"LVT_LVECD").equals(LP_LCOD2) && getLVDTL(arrHSTKEY[i-1].toString(),"LVT_LVECD").equals(LP_LCOD1))
					setADDMSG("  "+LP_LCOD2+ "  and "+LP_LCOD1+ "  clubbed on  "+M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString())),LP_VLDTP);
			}
				
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkCOMB()");
	    }
	}
	
	
	/**
	 */
	private String getLVDTL_STR()
	{
		String L_strYYYYMMDD_STR = "", L_strYYYYMMDD_RUN = "";
		try
		{
			L_strYYYYMMDD_STR = fmtYYYYMMDD.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(0,TB2_LVEDT).toString()));
			L_strYYYYMMDD_RUN = fmtYYYYMMDD.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(0,TB2_LVEDT).toString()));
			while(true)
			{
				if(hstLVDTL.containsKey(L_strYYYYMMDD_RUN))
				{
					L_strYYYYMMDD_STR = L_strYYYYMMDD_RUN;
					M_calLOCAL.setTime(fmtYYYYMMDD.parse(L_strYYYYMMDD_RUN));      
					M_calLOCAL.add(Calendar.DATE,-1);    
					L_strYYYYMMDD_RUN = fmtYYYYMMDD.format(M_calLOCAL.getTime());
				}
				else
					break;
			}
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getLVDTL_STR()");
	    }
		return L_strYYYYMMDD_STR;
	}
	

	
	/**
	 */
	private String getLVDTL_END()
	{
		String L_strYYYYMMDD_RUN = "",L_strYYYYMMDD_END = "";
		try
		{
			L_strYYYYMMDD_RUN =  fmtYYYYMMDD.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(0,TB2_LVEDT).toString()));
			//System.out.println("END : 1 "+L_strYYYYMMDD_RUN);
			for(int i=0;i<tblDSTRB.getRowCount();i++)
			{
				if(tblDSTRB.getValueAt(i,TB2_LVEDT).toString().length()<10)
					break;
				L_strYYYYMMDD_RUN =  fmtYYYYMMDD.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(i,TB2_LVEDT).toString()));
				//System.out.println("END : 2 "+L_strYYYYMMDD_RUN);
			
			}
			L_strYYYYMMDD_END = L_strYYYYMMDD_RUN;
			M_calLOCAL.setTime(fmtYYYYMMDD.parse(L_strYYYYMMDD_RUN));      
			M_calLOCAL.add(Calendar.DATE,+1);    
			L_strYYYYMMDD_RUN = fmtYYYYMMDD.format(M_calLOCAL.getTime());
 			//System.out.println("END : 3 "+L_strYYYYMMDD_RUN);
			while(true)
			{
				if(hstLVDTL.containsKey(L_strYYYYMMDD_RUN))
				{
					L_strYYYYMMDD_END = L_strYYYYMMDD_RUN;
					M_calLOCAL.setTime(fmtYYYYMMDD.parse(L_strYYYYMMDD_RUN));      
					M_calLOCAL.add(Calendar.DATE,+1);    
					L_strYYYYMMDD_RUN = fmtYYYYMMDD.format(M_calLOCAL.getTime());
 					//System.out.println("END : 3 "+L_strYYYYMMDD_RUN);
				}
				else
					break;
			}
			
 			//System.out.println("END : 3 "+L_strYYYYMMDD_END);
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getLVDTL_END()");
	    }
		return L_strYYYYMMDD_END;
	}
	
	
	
	/**returns total count of the leave code in the tblDSTRB table
	 */
	public int chkTOTALCNT(int LP_COL,String LP_LCOD)
	{
		try
		{
			int i=0,L_intTOTAL=0;
			//String strVERFY="";
			for(i=0;i<tblDSTRB.getRowCount();i++)
			{
				if(tblDSTRB.getValueAt(i,TB2_CHKFL).toString().equals("true"))
				{
					if(tblDSTRB.getValueAt(i,LP_COL).toString().equals(LP_LCOD))
					{
						L_intTOTAL++;
					}	
				}
			}
			return L_intTOTAL;
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"chkTOTALCNT()");
	    }
		return 0;
	}

	/** this methode initializes some componants 
	 */
	public void cmpINIT()
	{
		try
		{
			////initializes DPTCD
			clrCOMP();
			M_strSQLQRY = " select cmt_codcd,cmt_shrds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and cmt_codcd in ";
			M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO=US_EMPCD)";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				strDPTCD=M_rstRSSET.getString("cmt_codcd");
				strDPTNM=M_rstRSSET.getString("cmt_shrds");
			}
			
			//strAPPDT=cl_dat.M_strLOGDT_pbst;
			strREFDT=cl_dat.M_strLOGDT_pbst;

			if(M_rstRSSET != null)
				M_rstRSSET.close();				
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"cmpINIT()");
	    }
	}
	

	
	public boolean setLOGINDTL()
	{
		try
		{
			////initializes DPTCD
			//clrCOMP();
			M_strSQLQRY = "select US_EMPCD,ep_dptcd from sa_usmst,hr_epmst where us_usrcd = '"+cl_dat.M_strUSRCD_pbst+"' and US_EMPCD = ep_empno and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_LFTDT is null AND SUBSTRING(ep_hrsbs,1,2)='01' ";
			//System.out.println(">>>>>>>>>>>>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET == null || !M_rstRSSET.next())
				return false;
			txtDPTCD.setText(M_rstRSSET.getString("ep_dptcd"));
			//System.out.print(txtDPTCD.getText());
			strEMPNO_LOGIN = M_rstRSSET.getString("US_EMPCD");
			M_rstRSSET.close();				
		}
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"setLOGINDTL()");
	    }
		return true;
	}
	
	
	/** methode to enter data in to the tblAUTHR table
	 */
	public void getAUTHR()
	{
		try
		{
			int L_CNT=0;
			clrEDITR(tblAUTHR);
			tblAUTHR.clrTABLE();
			M_strSQLQRY =" select count(*) CNT from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp = 'HR"+cl_dat.M_strCMPCD_pbst+"SSN' and cmt_codcd in (select US_EMPCD from SA_USMST where US_USRCD =  '"+cl_dat.M_strUSRCD_pbst.trim()+"')";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			M_rstRSSET.next();
			//System.out.println(">>>>>>>>>>>"+M_rstRSSET.getInt("CNT"));
			if(M_rstRSSET.getInt("CNT")>0)
				flgSSNAUT = true;	
			else
				flgSSNAUT = false;	
			M_rstRSSET.close();
			//M_strSQLQRY =" Select distinct LVT_EMPNO,trim(ifnull(ep_lstnm,' '))||' '  ||left(ifnull(ep_fstnm,' '),1)||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM,LVT_APPDT,CMT_CODDS from";
			//M_strSQLQRY+=" HR_LVTRN,HR_EPMST,CO_CDTRN where";
			//M_strSQLQRY+=" (LVT_SCNBY='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=LVT_EMPNO and CMT_CGMTP='STS' and CMT_CGSTP='HRXXLVT' and CMT_CODCD=LVT_STSFL and LVT_STSFL not in ('0','2','3','X')) ";
			//if(flgSSNAUT)
			//	M_strSQLQRY+=" or ( lvt_SSCFL ='1' and substr(lvt_sbscd,1,2) = '"+M_strSBSCD.substring(0,2)+"')";
			
			M_strSQLQRY =" Select distinct LVT_EMPNO,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '   + left(isnull(ep_fstnm,' '),1) + '.'+ left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM,LVT_REFDT,LVT_APPDT,LVT_STSFL,cmt_codcd,CMT_CODDS"; 
			M_strSQLQRY+=" from HR_LVTRN,HR_EPMST,CO_CDTRN where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
			M_strSQLQRY+= flgALLDPT ? "" : " and (LVT_SCNBY='"+cl_dat.M_strUSRCD_pbst+"' ) "; 
			//if(flgSSNAUT)
			//	M_strSQLQRY+=" or ( lvt_SSCFL ='1' and lvt_cmpcd  = '"+cl_dat.M_strCMPCD_pbst+"'))";
			M_strSQLQRY+="  and ( EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and EP_LFTDT is null and EP_CMPCD = LVT_CMPCD and EP_EMPNO = LVT_EMPNO and CMT_CGMTP='STS' and CMT_CGSTP='HRXXLVT' and CMT_CODCD=LVT_STSFL and LVT_STSFL not in ('0','2','3','X'))";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println("QRY>>>>>>>"+M_strSQLQRY);			
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{	
					//System.out.println(">>>>"+M_rstRSSET.getString("CMT_CODDS"));
					tblAUTHR.setValueAt(M_rstRSSET.getString("LVT_EMPNO"),L_CNT,TB4_EMPNO);
					tblAUTHR.setValueAt(M_rstRSSET.getString("EP_EMPNM"),L_CNT,TB4_EMPNM);
					tblAUTHR.setValueAt(M_fmtLCDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT")))),L_CNT,TB4_REFDT);
					if(M_rstRSSET.getString("CMT_CODCD").equals("8"))
						tblAUTHR.setRowColor(L_CNT,Color.RED);
					else
						tblAUTHR.setRowColor(L_CNT,Color.BLACK);
					tblAUTHR.setValueAt(M_rstRSSET.getString("CMT_CODDS"),L_CNT,TB4_STSDS);
					tblAUTHR.setValueAt(M_fmtLCDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")))),L_CNT,TB4_APPDT);
					L_CNT++;
				}	
				M_rstRSSET.close();
				cntINFMN = L_CNT;
			}	
		}	
		catch(Exception L_EA)
	    {
	        setMSG(L_EA,"getAUTHR()");
	    }	
	}

	/** methode to enter data in to the tblTELVE table
	 */
	public void getTELVE()
	{
		try
		{
			int L_CNT=0;
			clrEDITR(tblTELVE);
			tblTELVE.clrTABLE();
			//String L_strSELSTR=" LVT_APPDT,LVT_LVEDT,LVT_LVECD,ifNull(LVT_LVEQT,0) LVT_LVEQT,ifNull(LVT_RSNDS,'') LVT_RSNDS,ifNull(LVT_RCMBY,'') LVT_RCMBY,ifNull(LVT_SCNBY,'') LVT_SCNBY,ifNull(LVT_STSFL,'') LVT_STSFL";
			String L_strSELSTR=" LVT_APPDT,LVT_REFDT,LVT_LVEDT,LVT_LVECD,isnull(LVT_LVEQT,0) LVT_LVEQT,isnull(LVT_RSNDS,'') LVT_RSNDS,isnull(LVT_RCMBY,'') LVT_RCMBY,isnull(LVT_SCNBY,'') LVT_SCNBY,isnull(LVT_STSFL,'') LVT_STSFL";
			String L_strWHRSTR=" LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and year(lvt_lvedt) =  (select year(ep_yopdt) from hr_epmst where ep_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ep_empno = lvt_empno)";//and ifNull(LVT_STSFL,'')<>'X'"; 
			
			M_strSQLQRY=" Select "+L_strSELSTR+" from HR_LVTRN ";
			M_strSQLQRY+=" where "+L_strWHRSTR+"";
			M_strSQLQRY+=" order by LVT_LVEDT,LVT_APPDT,LVT_LVECD";
			//System.out.println(">>getTELVE>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null && M_rstRSSET.next())
			{
				String L_strLVECD,L_strRSNDS,L_strRCMBY,L_strSCNBY;
				float L_fltLVEQT;
				java.util.Date L_datAPPDT,L_datREFDT,L_datSTRDT,L_datENDDT;
				String L_strSTSFL;
				L_datAPPDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
				L_datREFDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT")));
				L_strLVECD=M_rstRSSET.getString("LVT_LVECD");
				L_datSTRDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
				L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
				L_fltLVEQT=M_rstRSSET.getFloat("LVT_LVEQT");		   
				L_strRSNDS=M_rstRSSET.getString("LVT_RSNDS");		   
				L_strRCMBY=M_rstRSSET.getString("LVT_RCMBY");		   
				L_strSCNBY=M_rstRSSET.getString("LVT_SCNBY");		   
				L_strSTSFL=M_rstRSSET.getString("LVT_STSFL");		   
				while(M_rstRSSET.next())
				{
					//System.out.println("L_datAPPDT>>"+L_datAPPDT+">>"+M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
					//System.out.println("L_strLVECD>>"+L_strLVECD+">>"+M_rstRSSET.getString("LVT_LVECD"));
					//if(!L_datAPPDT.equals(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT"))))
					try{
					if(!L_datREFDT.equals(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT"))))
					   || !L_datAPPDT.equals(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT"))))
					   || !L_strSTSFL.equals(M_rstRSSET.getString("LVT_STSFL"))
					   || !L_strLVECD.equals(M_rstRSSET.getString("LVT_LVECD")))
					{
						tblTELVE.setRowColor(L_CNT,Color.BLACK);
						if(txtSTRDT.getText().length()==10 && txtAPPDT.getText().length()==10 && M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(L_datREFDT)==0 && M_fmtLCDAT.parse(txtAPPDT.getText()).compareTo(L_datAPPDT)==0)
						{
						   tblTELVE.setRowColor(L_CNT,Color.BLUE);
						   tblTELVE.setValueAt(new Boolean(true),L_CNT,TB3_CHKFL);
						}
						tblTELVE.setValueAt(M_fmtLCDAT.format(L_datAPPDT),L_CNT,TB3_APPDT);
						tblTELVE.setValueAt(M_fmtLCDAT.format(L_datREFDT),L_CNT,TB3_REFDT);
						tblTELVE.setValueAt(M_fmtLCDAT.format(L_datSTRDT),L_CNT,TB3_STRDT);
						tblTELVE.setValueAt(M_fmtLCDAT.format(L_datENDDT),L_CNT,TB3_ENDDT);
						tblTELVE.setValueAt(L_strLVECD,L_CNT,TB3_LVECD);
						tblTELVE.setValueAt(""+L_fltLVEQT,L_CNT,TB3_LVEQT);
						tblTELVE.setValueAt(L_strRSNDS,L_CNT,TB3_RSNDS);
						tblTELVE.setValueAt(L_strRCMBY,L_CNT,TB3_RCMBY);
						tblTELVE.setValueAt(L_strSCNBY,L_CNT,TB3_SCNBY);
						tblTELVE.setValueAt(getCDTRN("STSHRXXLVT"+L_strSTSFL,"CMT_CODDS",hstCDTRN),L_CNT,TB3_STSDS);
													 
						L_CNT++;
						
						L_datAPPDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
						L_datREFDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT")));
						L_strLVECD=M_rstRSSET.getString("LVT_LVECD");
						L_datSTRDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
						L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
						L_fltLVEQT=M_rstRSSET.getFloat("LVT_LVEQT");		   
						L_strRSNDS=M_rstRSSET.getString("LVT_RSNDS");		   
						L_strRCMBY=M_rstRSSET.getString("LVT_RCMBY");		   
						L_strSCNBY=M_rstRSSET.getString("LVT_SCNBY");		   
						L_strSTSFL=M_rstRSSET.getString("LVT_STSFL");		   
					}
					else
					{
						L_fltLVEQT+=M_rstRSSET.getFloat("LVT_LVEQT");
						L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
					}
					}catch(Exception E){System.out.println("inside");}
				}
				try{
				tblTELVE.setRowColor(L_CNT,Color.BLACK);
				//if(txtAPPDT.getText().length()==10 && M_fmtLCDAT.parse(txtAPPDT.getText()).compareTo(L_datAPPDT)==0)
				if(txtSTRDT.getText().length()==10 && txtAPPDT.getText().length()==10 && M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(L_datREFDT)==0 && M_fmtLCDAT.parse(txtAPPDT.getText()).compareTo(L_datAPPDT)==0)
				{
				   tblTELVE.setRowColor(L_CNT,Color.BLUE);
				   tblTELVE.setValueAt(new Boolean(true),L_CNT,TB3_CHKFL);
				}
				tblTELVE.setValueAt(M_fmtLCDAT.format(L_datAPPDT),L_CNT,TB3_APPDT);
				tblTELVE.setValueAt(M_fmtLCDAT.format(L_datREFDT),L_CNT,TB3_REFDT);
				tblTELVE.setValueAt(M_fmtLCDAT.format(L_datSTRDT),L_CNT,TB3_STRDT);
				tblTELVE.setValueAt(M_fmtLCDAT.format(L_datENDDT),L_CNT,TB3_ENDDT);
				tblTELVE.setValueAt(L_strLVECD,L_CNT,TB3_LVECD);
				tblTELVE.setValueAt(""+L_fltLVEQT,L_CNT,TB3_LVEQT);
				tblTELVE.setValueAt(L_strRSNDS,L_CNT,TB3_RSNDS);
				tblTELVE.setValueAt(L_strRCMBY,L_CNT,TB3_RCMBY);
				tblTELVE.setValueAt(L_strSCNBY,L_CNT,TB3_SCNBY);
				tblTELVE.setValueAt(getCDTRN("STSHRXXLVT"+L_strSTSFL,"CMT_CODDS",hstCDTRN),L_CNT,TB3_STSDS);
				inlTBLEDIT(tblTELVE);
				tblTELVE.setValueAt(new Boolean(true),L_CNT+2,TB3_CHKFL);
				tblTELVE.setValueAt(new Boolean(false),L_CNT+2,TB3_CHKFL);
				}catch(Exception E){System.out.println("inside1");}
			}
				//System.out.println("in else");	
			
				if(M_rstRSSET != null)
					M_rstRSSET.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTELVE"); 
		}
	}


	/**
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				String L_strLVECD,L_strRSNDS,L_strRCMBY,L_strSCNBY;
				float L_fltLVEQT;
				java.util.Date L_datAPPDT,L_datSTRDT,L_datENDDT;
				String L_strSTSFL;
				L_datAPPDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
				L_strLVECD=M_rstRSSET.getString("LVT_LVECD");
				L_datSTRDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
				L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
				L_fltLVEQT=M_rstRSSET.getFloat("LVT_LVEQT");		   
				L_strRSNDS=M_rstRSSET.getString("LVT_RSNDS");		   
				L_strRCMBY=M_rstRSSET.getString("LVT_RCMBY");		   
				L_strSCNBY=M_rstRSSET.getString("LVT_SCNBY");		   
				L_strSTSFL=M_rstRSSET.getString("LVT_STSFL");		   
				while(M_rstRSSET.next())
				{
					//System.out.println("L_datAPPDT>>"+L_datAPPDT+">>"+M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
					//System.out.println("L_strLVECD>>"+L_strLVECD+">>"+M_rstRSSET.getString("LVT_LVECD"));
					if(!L_datAPPDT.equals(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT"))))
					   || !L_strLVECD.equals(M_rstRSSET.getString("LVT_LVECD")))
					{
						if(txtAPPDT.getText().equals(M_fmtLCDAT.format(L_datAPPDT)))
						{
						   tblTELVE.setRowColor(L_CNT,Color.RED);
						   tblTELVE.setValueAt(new Boolean(true),L_CNT,TB3_CHKFL);
						}
						tblTELVE.setValueAt(M_fmtLCDAT.format(L_datAPPDT),L_CNT,TB3_APPDT);
						tblTELVE.setValueAt(M_fmtLCDAT.format(L_datSTRDT),L_CNT,TB3_STRDT);
						tblTELVE.setValueAt(M_fmtLCDAT.format(L_datENDDT),L_CNT,TB3_ENDDT);
						tblTELVE.setValueAt(L_strLVECD,L_CNT,TB3_LVECD);
						tblTELVE.setValueAt(""+L_fltLVEQT,L_CNT,TB3_LVEQT);
						tblTELVE.setValueAt(L_strRSNDS,L_CNT,TB3_RSNDS);
						tblTELVE.setValueAt(L_strRCMBY,L_CNT,TB3_RCMBY);
						tblTELVE.setValueAt(L_strSCNBY,L_CNT,TB3_SCNBY);
						tblTELVE.setValueAt(getCDTRN("STSHRXXLVT"+L_strSTSFL,"CMT_CODDS",hstCDTRN),L_CNT,TB3_STSDS);
													 
						L_CNT++;
						
						L_datAPPDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT")));
						L_strLVECD=M_rstRSSET.getString("LVT_LVECD");
						L_datSTRDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
						L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
						L_fltLVEQT=M_rstRSSET.getFloat("LVT_LVEQT");		   
						L_strRSNDS=M_rstRSSET.getString("LVT_RSNDS");		   
						L_strRCMBY=M_rstRSSET.getString("LVT_RCMBY");		   
						L_strSCNBY=M_rstRSSET.getString("LVT_SCNBY");		   
						L_strSTSFL=M_rstRSSET.getString("LVT_STSFL");		   
					}
					else
					{
						L_fltLVEQT+=M_rstRSSET.getFloat("LVT_LVEQT");
						L_datENDDT=M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")));		   
					}	
				}
				tblTELVE.setValueAt(M_fmtLCDAT.format(L_datAPPDT),L_CNT,TB3_APPDT);
				tblTELVE.setValueAt(M_fmtLCDAT.format(L_datSTRDT),L_CNT,TB3_STRDT);
				tblTELVE.setValueAt(M_fmtLCDAT.format(L_datENDDT),L_CNT,TB3_ENDDT);
				tblTELVE.setValueAt(L_strLVECD,L_CNT,TB3_LVECD);
				tblTELVE.setValueAt(""+L_fltLVEQT,L_CNT,TB3_LVEQT);
				tblTELVE.setValueAt(L_strRSNDS,L_CNT,TB3_RSNDS);
				tblTELVE.setValueAt(L_strRCMBY,L_CNT,TB3_RCMBY);
				tblTELVE.setValueAt(L_strSCNBY,L_CNT,TB3_SCNBY);
				tblTELVE.setValueAt(getCDTRN("STSHRXXLVT"+L_strSTSFL,"CMT_CODDS",hstCDTRN),L_CNT,TB3_STSDS);
				
	 */
	
	
	/** methode to enter data in to the tblDSTRB table if ADDITION option button is selected 
	 */
	public void getDSTRBADD()
	{
		try
        {
			java.util.Date L_datSTRDT,L_datENDDT;
			int L_CNT=0;
			clrEDITR(tblDSTRB);
			hstHDRVAL.clear() ;
			tblDSTRB.clrTABLE();
			L_datSTRDT=M_fmtLCDAT.parse(txtSTRDT.getText());
			L_datENDDT=M_fmtLCDAT.parse(txtENDDT.getText());
			while(L_datSTRDT.compareTo(L_datENDDT)<=0)
			{
				hstHDRVAL.put(fmtYYYYMMDD.format(L_datSTRDT),fmtDD.format(L_datSTRDT));
				M_calLOCAL.setTime(L_datSTRDT);      
				M_calLOCAL.add(Calendar.DATE,1);    
				L_datSTRDT = M_calLOCAL.getTime();
			}	
			setHST_ARR(hstHDRVAL);
			int i=0;
			for(i = 0;i < arrHSTKEY.length; i++)
			{
					tblDSTRB.setValueAt(M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString())),i,TB2_LVEDT);
					tblDSTRB.setValueAt(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()).toString().substring(0,3),i,TB2_LVEDY);
					tblDSTRB.setValueAt("1.0",i,TB2_LVEQT);
					tblDSTRB.setValueAt("0",i,TB2_SRLNO);
					tblDSTRB.setValueAt(new Boolean(true),i,TB2_CHKFL);
			}
			i=0;	
			M_strSQLQRY=" select SS_WRKDT,SS_LVECD from HR_SSTRN where";
			M_strSQLQRY+=" SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SS_EMPNO='"+txtEMPNO.getText()+"' and"; 
			M_strSQLQRY+=" SS_WRKDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[i].toString()))))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(fmtYYYYMMDD.parse(arrHSTKEY[arrHSTKEY.length-1].toString()))))+"' order by SS_WRKDT";
			//System.out.println(">>>QRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{	
				//System.out.println("inside if");
				while(M_rstRSSET.next())
				{		
					if(M_fmtLCDAT.parse(tblDSTRB.getValueAt(i,TB2_LVEDT).toString()).compareTo(M_rstRSSET.getDate("SS_WRKDT"))==0)
					{
						//tblDSTRB.setValueAt(M_rstRSSET.getString("SS_LVECD"),i,TB2_LVECD);
						
						//modified to remove WO and RH to be entered in the table
						if(M_rstRSSET.getString("SS_LVECD").equals("PH"))
							tblDSTRB.setValueAt(M_rstRSSET.getString("SS_LVECD"),i,TB2_LVECD);
					}
					else
					{
						setMSG("Error in Fetching Date",'E');
					}	
					i++;
				}	
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"getDSTRBADD()");
        }
	}	

	/** methode to enter data in to the tblDSTRB table if ADDITION option button is NOT SELECTED 
	 */
	public void getDSTRB()
	{
		try
        {
			int L_CNT=0;
			float L_fltTOTAL=0;
			boolean L_flgSTRDT=true;
			clrEDITR(tblDSTRB);
			tblDSTRB.clrTABLE();
			String L_strDPTCD="",L_strDPTNM="",L_strAPPDT="",L_strRCMBY="",L_strSCNBY="",L_strDESGN="",L_strMMGRD="",L_strLVECD3="",L_strSTRDT="",L_strENDDT="",L_strCONNO="",L_strPLCNM="",L_strORGNM="",L_strMOJCD="",L_strRSNDS="";
			
			M_strSQLQRY = " Select CMT_CODCD,CMT_SHRDS,LVT_LVECD,LVT_LVEQT,LVT_LVEDT,LVT_APPDT,LVT_REFDT,LVT_SRLNO,LVT_RCMBY,LVT_SCNBY,isnull(LVT_DESGN,'') LVT_DESGN,isnull(LVT_MMGRD,'') LVT_MMGRD,isnull(LVT_CONNO,'') LVT_CONNO,isnull(LVT_PLCNM,'') LVT_PLCNM,isnull(LVT_ORGNM,'') LVT_ORGNM,isnull(LVT_MOJCD,'') LVT_MOJCD,LVT_RSNDS from HR_LVTRN,CO_CDTRN where";
			M_strSQLQRY+= " LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_APPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtAPPDT.getText()))+"' and";
			M_strSQLQRY+= " LVT_REFDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and";
			M_strSQLQRY+= " LVT_EMPNO='"+txtEMPNO.getText()+"' ";
			//M_strSQLQRY+= " LVT_STSFL='"+lblSTSFL.getText()+"' ";
			M_strSQLQRY+= " and cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and cmt_codcd in(select EP_DPTCD from HR_EPMST where EP_EMPNO='"+txtEMPNO.getText()+"' and EP_EMPNO=LVT_EMPNO and EP_CMPCD=LVT_CMPCD and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"') ";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				M_strSQLQRY+= " and isnull(LVT_STSFL,'') not in('2','3','8')";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				M_strSQLQRY+= " and isnull(LVT_STSFL,'')<>'X'";
			//System.out.println(">>>>>>>>>>>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{	
				while(M_rstRSSET.next())
				{	
					tblDSTRB.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT")),L_CNT,TB2_LVEDT);		
					tblDSTRB.setValueAt(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT"))).toString().substring(0,3),L_CNT,TB2_LVEDY);					
					tblDSTRB.setValueAt(M_rstRSSET.getString("LVT_LVEQT"),L_CNT,TB2_LVEQT);
					tblDSTRB.setValueAt(M_rstRSSET.getString("LVT_LVECD"),L_CNT,TB2_LVECD);
					tblDSTRB.setValueAt(M_rstRSSET.getString("LVT_SRLNO"),L_CNT,TB2_SRLNO);
					tblDSTRB.setValueAt(new Boolean(true),L_CNT,TB2_CHKFL);
					
					L_strDPTCD=M_rstRSSET.getString("CMT_CODCD");
					L_strDPTNM=M_rstRSSET.getString("CMT_SHRDS");
					L_strRCMBY=M_rstRSSET.getString("LVT_RCMBY");
					L_strSCNBY=M_rstRSSET.getString("LVT_SCNBY");
					L_strDESGN=M_rstRSSET.getString("LVT_DESGN");
					L_strMMGRD=M_rstRSSET.getString("LVT_MMGRD");
					L_strLVECD3=M_rstRSSET.getString("LVT_LVECD");
					L_strAPPDT=M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_APPDT"));
					if(L_flgSTRDT==true)
					{
						L_strSTRDT=M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_REFDT"));
						L_flgSTRDT=false;
					}	
					L_strENDDT=M_fmtLCDAT.format(M_rstRSSET.getDate("LVT_LVEDT"));
					L_strCONNO=M_rstRSSET.getString("LVT_CONNO");
					L_strPLCNM=M_rstRSSET.getString("LVT_PLCNM");
					L_strORGNM=M_rstRSSET.getString("LVT_ORGNM");
					L_strMOJCD=M_rstRSSET.getString("LVT_MOJCD");
					L_strRSNDS=M_rstRSSET.getString("LVT_RSNDS");
					L_CNT++;
					L_fltTOTAL+=Float.parseFloat(M_rstRSSET.getString("LVT_LVEQT"));
				}
				//System.out.println("????????"+L_strDPTCD);
				if(txtDPTCD.getText().length()==0 && !flgALLDPT)
				{	
					txtDPTCD.setText(L_strDPTCD);
					lblDPTNM.setText(L_strDPTNM);
				}
				txtAPPDT.setText(L_strAPPDT);
				txtRCMBY.setText(L_strRCMBY);
				txtSCNBY.setText(L_strSCNBY);
				if(L_strDESGN.length()>0)
					txtDESGN.setText(L_strDESGN);
				if(L_strMMGRD.length()>0)
					txtMMGRD.setText(L_strMMGRD);
				txtLVEQT1.setText(""+L_fltTOTAL);
				txtSTRDT.setText(L_strSTRDT);
				txtENDDT.setText(L_strENDDT);
				txtCONNO.setText(L_strCONNO);
				txtRSNDS.setText(L_strRSNDS);
				if(L_strLVECD3.equals("TD"))
				{		
					cmbTRNTP.setSelectedItem(strTDUTY_EN);
					tbpMAIN.setSelectedIndex(2);
					txtPLCNM.setText(L_strPLCNM);
					txtORGNM.setText(L_strORGNM);
					if(L_strMOJCD.substring(0,1).equals("C"))
						chkCOVEH.setSelected(true);
					if(L_strMOJCD.substring(1,2).equals("B"))
						chkBUS.setSelected(true);
					if(L_strMOJCD.substring(2,3).equals("T"))
						chkTRAIN.setSelected(true);
					if(L_strMOJCD.substring(3,4).equals("A"))
						chkAIR.setSelected(true);
					if(L_strMOJCD.substring(4,5).equals("H"))
						chkHRVEH.setSelected(true);
				}
				else if(L_strLVECD3.equals("PE"))
				{		
					cmbTRNTP.setSelectedItem(strPLENC_EN);
				}
				else 
				{
					cmbTRNTP.setSelectedItem(strLEAVE_EN);
					tbpMAIN.setSelectedIndex(0);
				}
				lblDSPTT.setText(""+L_fltTOTAL);
				cntCL = chkTOTALCNT(TB2_LVECD,"CL");
				cntPL = chkTOTALCNT(TB2_LVECD,"PL");
				cntRH = chkTOTALCNT(TB2_LVECD,"RH");
				cntSL = chkTOTALCNT(TB2_LVECD,"SL");
			}
				if(M_rstRSSET != null)
					M_rstRSSET.close();			
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"getDSTRB");
        }
	}	
	
	private void setHST_ARR(Hashtable LP_HSTNM)
	{
		try
		{
			arrHSTKEY = LP_HSTNM.keySet().toArray();
			Arrays.sort(arrHSTKEY);
		}
		catch(Exception e){setMSG(e,"setHST_ARR");}
	}
		
	/* method to get the value of pending approvals i.e. LVT_STSFL not in(0,2,3,X)*/
	public float exePENDING(String LP_strLVECD)
	{
		try
		{
			String L_strSQLQRY1="";
			ResultSet L_rstRSSET1;	
		
			L_strSQLQRY1 =" Select sum(LVT_LVEQT) SUM from HR_LVTRN where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and";
			L_strSQLQRY1+=" LVT_LVECD='"+LP_strLVECD+"' and isnull(LVT_STSFL,'') <> '0' and isnull(LVT_STSFL,'') <> '2' and isnull(LVT_STSFL,'') <> '3' and isnull(LVT_STSFL,'') <> 'X'"; 
			L_rstRSSET1 = cl_dat.exeSQLQRY1(L_strSQLQRY1);
			if(L_rstRSSET1.next() && L_rstRSSET1 !=null)
			{
				float L_fltSUM = L_rstRSSET1.getFloat("SUM");
				L_rstRSSET1.close();
				return L_fltSUM;
			}
			else return 0;
		}
		catch(Exception L_EX)
        {
            setMSG(L_EX,"exePENDING()");
        }
		return 0;
	}

	
	/** method to set Year Opening values in tblLEAVE
	 */
	public void setYOPDTL()
    {
        try
        {
			int L_CNT=0;
			clrEDITR(tblLEAVE);
			tblLEAVE.clrTABLE();
			tblLEAVE.setValueAt("CL",L_CNT,TB1_LVECD);
			tblLEAVE.setValueAt(getYOPDTL("YOPCL"),L_CNT,TB1_YOPXX);
			tblLEAVE.setValueAt(getYOPDTL("YTDCL"),L_CNT,TB1_YTDXX);
			tblLEAVE.setValueAt(""+exePENDING("CL"),L_CNT,TB1_UAUXX);				
			L_CNT++;
			tblLEAVE.setValueAt("PL",L_CNT,TB1_LVECD);
			tblLEAVE.setValueAt(getYOPDTL("YOPPL"),L_CNT,TB1_YOPXX);
			tblLEAVE.setValueAt(getYOPDTL("YTDPL"),L_CNT,TB1_YTDXX);
			tblLEAVE.setValueAt(""+exePENDING("PL"),L_CNT,TB1_UAUXX);				
			L_CNT++;
			tblLEAVE.setValueAt("RH",L_CNT,TB1_LVECD);
			tblLEAVE.setValueAt(getYOPDTL("YOPRH"),L_CNT,TB1_YOPXX);
			tblLEAVE.setValueAt(getYOPDTL("YTDRH"),L_CNT,TB1_YTDXX);
			tblLEAVE.setValueAt(""+exePENDING("RH"),L_CNT,TB1_UAUXX);				
			L_CNT++;
			tblLEAVE.setValueAt("SL",L_CNT,TB1_LVECD);
			tblLEAVE.setValueAt(getYOPDTL("YOPSL"),L_CNT,TB1_YOPXX);
			tblLEAVE.setValueAt(getYOPDTL("YTDSL"),L_CNT,TB1_YTDXX);
			tblLEAVE.setValueAt(""+exePENDING("SL"),L_CNT,TB1_UAUXX);				
			L_CNT++;
		}
		catch(Exception L_EX)
        {
            setMSG(L_EX,"setYOPDTL()");
        }
	}
	
	
	/** methode to enter data in to the tblDLEAVE i.e. each users year opening balance and 
	 	current bal and pending leaves
	 */
	public void crtYOPDTL()
    {
        try
        {
			String L_strSELSTR=" isnull(EP_YOPCL,0) EP_YOPCL,isnull(EP_YTDCL,0) EP_YTDCL,isnull(EP_YOPPL,0) EP_YOPPL,isnull(EP_YTDPL,0) EP_YTDPL,isnull(EP_YOPRH,0) EP_YOPRH,isnull(EP_YTDRH,0) EP_YTDRH,isnull(EP_YOPSL,0) EP_YOPSL,isnull(EP_YTDSL,0) EP_YTDSL, EP_JONDT ";
			String L_strWHRSTR=" EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_LFTDT is null AND EP_EMPNO='"+txtEMPNO.getText()+"'"; 
			
			M_strSQLQRY=" Select "+L_strSELSTR+" from HR_EPMST ";
			M_strSQLQRY+=" where "+L_strWHRSTR+"";
			//System.out.println(">>getLEAVE>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstYOPDTL.clear();
			if(!M_rstRSSET.next() || M_rstRSSET ==null)
				{setMSG("No Data Found",'E'); return;}

			hstYOPDTL.put("YOPCL",M_rstRSSET.getString("EP_YOPCL"));
			hstYOPDTL.put("YTDCL",M_rstRSSET.getString("EP_YTDCL"));
			hstYOPDTL.put("YOPPL",M_rstRSSET.getString("EP_YOPPL"));
			hstYOPDTL.put("YTDPL",M_rstRSSET.getString("EP_YTDPL"));
			hstYOPDTL.put("YOPRH",M_rstRSSET.getString("EP_YOPRH"));
			hstYOPDTL.put("YTDRH",M_rstRSSET.getString("EP_YTDRH"));
			hstYOPDTL.put("YOPSL",M_rstRSSET.getString("EP_YOPSL"));
			hstYOPDTL.put("YTDSL",M_rstRSSET.getString("EP_YTDSL"));
			String L_strJONDT = M_fmtLCDAT.format(M_rstRSSET.getDate("EP_JONDT"));
			M_rstRSSET.close();
			//System.out.println(L_strJONDT+" - " +cl_dat.M_strLOGDT_pbst+M_fmtLCDAT.parse(L_strJONDT).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)));
			if(M_fmtLCDAT.parse(L_strJONDT).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				putELGQOT(L_strJONDT,cl_dat.M_strLOGDT_pbst);
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"crtYOPDTL");
        }
    }

	/**  Recording Quota Eligibility of CL & SL for newly joined person
	 */
	private  void putELGQOT(String LP_JONDT, String LP_CURDT)
	{
		String L_strQRTNMB="", L_strQRTKEY = "",  L_strELGQOT = "";
		try
		{
			L_strQRTNMB = getQRTNMB(LP_CURDT);
			L_strQRTKEY = getQRTKEY(LP_CURDT,"CL");
			L_strELGQOT = getELGQOT(LP_JONDT,L_strQRTKEY,L_strQRTNMB,"CL");
			hstYOPDTL.put("ELGCL",L_strELGQOT);
			L_strQRTKEY = getQRTKEY(LP_CURDT,"SL");
			L_strELGQOT = getELGQOT(LP_JONDT,L_strQRTKEY,L_strQRTNMB,"SL");
			hstYOPDTL.put("ELGSL",L_strELGQOT);
			
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"putELGQOT");
        }
    }
	
	/**
	 */
	private String getQRTNMB(String LP_CURDT)
	{
		String L_strRETSTR = "";
		try
		{
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"getQRTNMB");
        }
		return L_strRETSTR;
	}
	

	/**
	 */
	private String getQRTKEY(String LP_CURDT, String LP_LVECD)
	{
		String L_strRETSTR = "";
		try
		{
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"getQRTKEY");
        }
		return L_strRETSTR;
	}
	

	/**
	 */
	private String getELGQOT(String LP_JONDT,String LP_QRTKEY, String LP_QRTNMB,String LP_LVECD)
	{
		String L_strRETSTR = "";
		try
		{
		}
        catch(Exception L_EX)
        {
            setMSG(L_EX,"getELGQOT");
        }
		return L_strRETSTR;
	}
	
	
	
	/** methode to get Year Opening values from hstYOPDTL
	 */
	public String getYOPDTL(String LP_KEYVL)
    {
        try
        {
			if(hstYOPDTL.containsKey(LP_KEYVL))
				return hstYOPDTL.get(LP_KEYVL).toString();
			else
				setMSG(LP_KEYVL+" Not found in hstYOPDTL",'E');
		}
		catch(Exception L_EX)
        {
            setMSG(L_EX,"setYOPDTL()");
        }
		return "0.0";
	}
	

		/** methode to get Year Opening values from hstYOPDTL
	 */
	public void setREFDT(String LP_EMPNO)
    {
        try
        {
			String  L_strSQLQRY1 = "", L_strSQLQRY2 = "";
			L_strSQLQRY1 = "select lvt_lvedt,lvt_appdt,lvt_stsfl from hr_lvtrn where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and lvt_empno = '"+txtEMPNO.getText() + "'  order by lvt_lvedt";
			//System.out.println(L_strSQLQRY1);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY1);
			if (!L_rstRSSET.next() || L_rstRSSET == null)
				return;
			java.util.Date L_datREFDT = L_rstRSSET.getDate("LVT_LVEDT");

			java.util.Date L_datLVEDT1 = L_rstRSSET.getDate("LVT_LVEDT");
			java.util.Date L_datLVEDT = L_rstRSSET.getDate("LVT_LVEDT");
			
			java.util.Date L_datAPPDT = L_rstRSSET.getDate("LVT_APPDT");
			java.util.Date L_datAPPDT1 = L_rstRSSET.getDate("LVT_APPDT");

			String L_strSTSFL = L_rstRSSET.getString("LVT_STSFL");
			String L_strSTSFL1 = L_rstRSSET.getString("LVT_STSFL");
			
			while(true)
			{
				L_datLVEDT = L_rstRSSET.getDate("LVT_LVEDT");
				L_datAPPDT = L_rstRSSET.getDate("LVT_APPDT");
				L_strSTSFL = L_rstRSSET.getString("LVT_STSFL");

				if(L_datLVEDT.compareTo(L_datLVEDT1)>0 ||
				   L_datAPPDT.compareTo(L_datAPPDT1)!=0 ||
				   L_strSTSFL.compareTo(L_strSTSFL1)!=0  )
				{	
					L_datREFDT = L_rstRSSET.getDate("LVT_LVEDT");
				}
				M_calLOCAL.setTime(L_datLVEDT);      
				M_calLOCAL.add(Calendar.DATE,1);
				L_datLVEDT1 = M_calLOCAL.getTime();
				L_datAPPDT1 = L_datAPPDT;
				L_strSTSFL1 = L_strSTSFL;
				L_strSQLQRY2 = "update hr_lvtrn set lvt_refdt = '"+M_fmtDBDAT.format(L_datREFDT)+"' where  LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and lvt_empno = '"+txtEMPNO.getText() + "' and lvt_lvedt = '"+M_fmtDBDAT.format(L_datLVEDT)+"'";
				//System.out.println(L_strSQLQRY2);
				cl_dat.exeSQLUPD(L_strSQLQRY2,"");
				if(!L_rstRSSET.next())
					break;
			}
			if(L_rstRSSET != null)
				L_rstRSSET.close();
		}
		catch(Exception L_EX)
        {
            setMSG(L_EX,"setREFDT()");
        }
	}

	
	
	
	
    /** method to validate data  */
	boolean vldDATA()
	{
		try
		{
			setMSG("",'N');
			if(txtDPTCD.getText().trim().length() ==0 && !flgALLDPT)
    		{
				txtDPTCD.requestFocus();
    			setMSG("Enter Department Code",'E');
    			return false;
			}
			else if(txtEMPNO.getText().trim().length() ==0)
    		{
				txtEMPNO.requestFocus();
    			setMSG("Enter the Employee Code",'E');
    			return false;
    		}
			else if(txtAPPDT.getText().trim().length() ==0)
    		{
				txtAPPDT.requestFocus();
    			setMSG("Enter the Application Date",'E');
    			return false;
    		}
			else if(txtRCMBY.getText().trim().length() ==0)
    		{
				txtRCMBY.requestFocus();
    			setMSG("Enter the Recoomending Person",'E');
    			return false;
    		}
			else if(txtSCNBY.getText().trim().length() ==0)
    		{
				txtSCNBY.requestFocus();
    			setMSG("Enter the Approving Person",'E');
    			return false;
    		}
			else if(txtLVEQT1.getText().trim().length() ==0)
    		{
				txtLVEQT1.requestFocus();
    			setMSG("Enter the No Of Days",'E');
    			return false;
    		}
			else if(!flgPLENC_EN && txtSTRDT.getText().trim().length() ==0)
    		{
				txtSTRDT.requestFocus();
    			setMSG("Enter the Start Date",'E');
    			return false;
    		}
			else if(!flgPLENC_EN && txtENDDT.getText().trim().length() ==0)
    		{
				txtENDDT.requestFocus();
    			setMSG("Enter the End Date",'E');
    			return false;
    		}
			else if(txtRSNDS.getText().trim().length() ==0)
    		{
				txtRSNDS.requestFocus();
    			setMSG("Enter the Reason",'E');
    			return false;
    		}
			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				if(txtRSNDS3.getText().trim().length() ==0)
    			{
					txtRSNDS3.requestFocus();
    				setMSG("Enter the Reason",'E');
    				return false;
    			}
			}
			else if(!flgPLENC_EN && chkNULL(TB2_LVECD))
			{
    			setMSG("Enter the Leve Code in The Table",'E');
    			return false;
    		}
			else if(!flgPLENC_EN && chkNULL(TB2_LVEDT))
			{
    			setMSG("Enter the Leve Date in The Table",'E');
    			return false;
    		}
			
			else if(!flgPLENC_EN && chkNULL(TB2_SRLNO))
			{
    			setMSG("Enter the Serial No. in The Table",'E');
    			return false;
    		}	

			else if(!flgPLENC_EN && chkNULL(TB2_LVEQT))			
			{
    			setMSG("Enter the Leve Qty in The Table",'E');
    			return false;
    		}
			else if(cmbTRNTP.getSelectedItem().equals(strLEAVE_EN))
			{
				for(int i=0;i<tblDSTRB.getRowCount();i++)
				{
					if(tblDSTRB.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					{
						if(tblDSTRB.getValueAt(i,TB2_LVECD).toString().equals("TD"))
						{
							setMSG("Enter valid Leve Code (TD is not allowed) in The Table",'E');
							return false;
						}	
					}
				}
			}
			else if(cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
			{
				for(int i=0;i<tblDSTRB.getRowCount();i++)
				{
					if(tblDSTRB.getValueAt(i,TB2_CHKFL).toString().equals("true"))
					{
						if(!tblDSTRB.getValueAt(i,TB2_LVECD).toString().equals("TD"))
						{
							setMSG("Enter valid Leve Code (TD) in The Table",'E');
							return false;
						}	
					}
				}
				if(txtPLCNM.getText().trim().length() ==0)
    			{
					tbpMAIN.setSelectedIndex(2);
					txtPLCNM.requestFocus();
    				setMSG("Please Enter Visiting Place..",'E');
    				return false;
    			}
				if(txtORGNM.getText().trim().length() ==0)
    			{
					tbpMAIN.setSelectedIndex(2);
					txtORGNM.requestFocus();
    				setMSG("Please Enter Visiting Organization..",'E');
    				return false;
    			}
				if(!(chkCOVEH.isSelected()==true || chkBUS.isSelected()==true || chkTRAIN.isSelected()==true || chkAIR.isSelected()==true || chkHRVEH.isSelected()==true))
    			{
					tbpMAIN.setSelectedIndex(2);
					txtPLCNM.requestFocus();
    				setMSG("Please Select Mode Of Journey..",'E');
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
    
	/* method to save data   */
	void exeSAVE()
    {
		int P_intROWNO=0;;
       try
		{
		    if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{	
				intLVT_STSFL=1;
				intLVT_SSCFL=0;
				if(!flgPLENC_EN)
				if(!exeVERIFY())
					return;
				//System.out.println("------->"+intLVT_STSFL);
				//System.out.println("------->"+intLVT_SSCFL);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{	
					if(intLVT_STSFL==0)
					   return;
					if(!flgSSNAUT && intLVT_STSFL==9)
					{	
						setMSG("Special Sanction Authority Reqd...",'E');
						return;
					}   
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
			{
				if(flgPLENC_EN)
					exeADDREC(0);
				else
				{
					for(P_intROWNO=0;P_intROWNO<tblDSTRB.getRowCount();P_intROWNO++)
					{
						if(tblDSTRB.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
						{
							exeADDREC(P_intROWNO);
						}
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
				for(P_intROWNO=0;P_intROWNO<tblDSTRB.getRowCount();P_intROWNO++)
				{
					if(tblDSTRB.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
					{
						exeMODREC(P_intROWNO);
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
			{
			    for(P_intROWNO=0;P_intROWNO<tblDSTRB.getRowCount();P_intROWNO++)
				{
					if(tblDSTRB.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
					{
						exeDELREC(P_intROWNO);
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
			{
				// If employee is present for the day then he is not allowed to enter leave.
				for(P_intROWNO=0;P_intROWNO<tblDSTRB.getRowCount();P_intROWNO++)
				{
					if(tblDSTRB.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true") && tblDSTRB.getValueAt(P_intROWNO,TB2_LVEQT).toString().equals("1.0") && !tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString().equals("TD") && !tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString().equals("PE"))
					{
						M_strSQLQRY = " Select SW_WRKDT,SW_INCTM,SW_OUTTM from HR_SWMST";
						M_strSQLQRY +=" where SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SW_EMPNO='"+txtEMPNO.getText()+"' AND SW_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"' and sw_actwk>'05.00.00'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						//System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
						if(M_rstRSSET != null && M_rstRSSET.next())
						{
							if(M_rstRSSET.getString("SW_INCTM")!=null && M_rstRSSET.getString("SW_OUTTM")!=null)
							{
								setMSG("Emp is present on "+M_fmtLCDAT.format(M_rstRSSET.getDate("SW_WRKDT"))+" In Time:"+M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_INCTM")).substring(11,16)+" Out Time:"+M_fmtLCDTM.format(M_rstRSSET.getTimestamp("SW_OUTTM")).substring(11,16),'E');
								M_rstRSSET.close();
								return;
							}
						}
					}
				}				
			    for(P_intROWNO=0;P_intROWNO<tblDSTRB.getRowCount();P_intROWNO++)
				{
					if(tblDSTRB.getValueAt(P_intROWNO,TB2_CHKFL).toString().equals("true"))
					{
						exeAUTHR(P_intROWNO);
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				tblLEAVE.clrTABLE();			
				tblDSTRB.clrTABLE();			
				tblTELVE.clrTABLE();
				String strHDSTS = "N";
				if(chkHDSTS.isSelected())
					strHDSTS="Y";				
				clrCOMP1();
	  			M_strSQLQRY = "select EP_EMLRF,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' ' + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM,lvt_empno,lvt_lvecd,cmt_codds, lvt_rsnds,min(lvt_lvedt) lvt_strdt,max(lvt_lvedt) lvt_enddt from hr_lvtrn,HR_EPMST,co_cdtrn where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and lvt_empno = ep_empno and EP_LFTDT is null and LVT_EMPNO='"+txtEMPNO.getText()+"' and cmt_cgmtp='SYS' and cmt_cgstp='HRXXLVE' and cmt_codcd=LVT_LVECD and LVT_REFDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' group by EP_EMLRF,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '   + left(isnull(ep_fstnm,' '),1) +'.' + left(isnull(ep_mdlnm,' '),1) + '.',lvt_empno,lvt_lvecd,cmt_codds,lvt_rsnds";
				M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				String L_strLVMSG = "";
				String L_strEMLRF = "";
				if(M_rstRSSET != null && M_rstRSSET.next())
				{
					
					L_strLVMSG = M_rstRSSET.getString("cmt_codds") + " for "+M_rstRSSET.getString("ep_empnm")+" (Purpose:"+M_rstRSSET.getString("lvt_rsnds")+")  "+(M_rstRSSET.getString("lvt_strdt").equals(M_rstRSSET.getString("lvt_enddt")) ? " on "+M_rstRSSET.getString("lvt_strdt") : " for the period "+M_fmtLCDAT.format(M_rstRSSET.getDate("lvt_strdt"))+ " to "+M_fmtLCDAT.format(M_rstRSSET.getDate("lvt_enddt")));
					L_strEMLRF = M_rstRSSET.getString("ep_emlrf");
					M_rstRSSET.close();
				}
				
				//if(txtLVECD3.getText().equals("TD"))
				//	L_strLVMSG = "Tour Duty ";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
				{
					getDSTRB();
					crtYOPDTL();
					setYOPDTL();
					getAUTHR();
					getTELVE();
					if(strHDSTS.equals("N"))
						JOptionPane.showMessageDialog(null,L_strLVMSG+" Authorized Successfully", "Verify", JOptionPane.INFORMATION_MESSAGE); 
					else
						JOptionPane.showMessageDialog(null,L_strLVMSG+" Held For Discussion", "Verify", JOptionPane.INFORMATION_MESSAGE); 
					chkHDSTS.setSelected(false);
				}
				else setALLCOMPS();
				
				if(flgLVSSN==true)
				{	
					String strBODY;
					
					//M_strSQLQRY = " Select EP_EMLRF,trim(ifnull(ep_lstnm,' '))||' '  ||left(ifnull(ep_fstnm,' '),1)||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM";
					//M_strSQLQRY +=" from HR_EPMST where";
					//M_strSQLQRY +=" EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'  and EP_LFTDT is null AND EP_EMPNO='"+txtEMPNO.getText()+"'";
					//M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					//System.out.println("M_strSQLQRY"+M_strSQLQRY);
					//if(M_rstRSSET != null && M_rstRSSET.next())
					//{
						strBODY = L_strLVMSG+(strHDSTS.equals("Y") ? " is Held For Discussion." : " is Sanctioned.");
						System.out.println(strBODY);
						//if(strHDSTS.equals("Y"))
						//	strBODY = strLVETP+" For '"+M_rstRSSET.getString("EP_EMPNM")+"' for the Period '"+txtSTRDT.getText()+"' to '"+txtENDDT.getText()+"' is Held For Discussion";
						//else	
						//	strBODY = strLVETP+" For '"+M_rstRSSET.getString("EP_EMPNM")+"' for the Period '"+txtSTRDT.getText()+"' to '"+txtENDDT.getText()+"' is Sanctioned";
						
						cl_eml ocl_eml = new cl_eml();
						ocl_eml.sendfile(L_strEMLRF,null,strBODY,strBODY);
						M_rstRSSET.close();
						//if(strHDSTS.equals("Y"))
						//	ocl_eml.sendfile(M_rstRSSET.getString("EP_EMLRF"),null,L_strLVMSG+" Held For Discussion",strBODY);
						//else
						//	ocl_eml.sendfile(M_rstRSSET.getString("EP_EMLRF"),null,L_strLVMSG+" Sanctioned",strBODY);	
						//M_rstRSSET.close();
					//}	
					//else 
					//	setMSG("Email Refference For Employee does not Exist..",'E'); 
					flgLVSSN=false;
				}
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
           setMSG(L_EX,"exeSAVE"); 
        }
    }

	private void exeAUTHR(int P_intROWNO)
	{ 
	  try
	  {

			if(flgSSNAUT)
				M_strSQLQRY +=" LVT_SSCFL='2',";
			if(chkHDSTS.isSelected())
				M_strSQLQRY +=" LVT_STSFL='8'";
			else
				M_strSQLQRY +=" LVT_STSFL='2'";
			//M_strSQLQRY +=" LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_APPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtAPPDT.getText()))+"'";
			M_strSQLQRY +=" where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_REFDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"'";
			//System.out.println(">>>update>>"+M_strSQLQRY);


  			M_strSQLQRY = " Update HR_LVTRN set";
			if(flgSSNAUT)
				M_strSQLQRY +=" LVT_SSCFL='2',";
			if(chkHDSTS.isSelected())
				M_strSQLQRY +=" LVT_STSFL='8'";
			else
				M_strSQLQRY +=" LVT_STSFL='2'";
			//M_strSQLQRY +=" LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_APPDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtAPPDT.getText()))+"'";
			M_strSQLQRY +=" where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_REFDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"'";
			//System.out.println(">>>update>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
		    
			M_strSQLQRY=" select count(*),SW_INCTM,SW_OUTTM,isnull(SW_LVECD,'') SW_LVECD from HR_SWMST where";
			M_strSQLQRY+=" SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SW_EMPNO = '"+txtEMPNO.getText()+"' and";
			M_strSQLQRY+=" SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'"; 	
			M_strSQLQRY+=" group by SW_INCTM,SW_OUTTM,SW_LVECD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>Count>>"+M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				// if lveqt = 0.5 then leave will be updated directly.
				// but if lveqt 1.0 then checks for inctm and outtm and then only updates.
				if(M_rstRSSET.getInt(1)>0 && M_rstRSSET.getString("SW_LVECD").equals("") && (tblDSTRB.getValueAt(P_intROWNO,TB2_LVEQT).toString().equals("0.5") || M_rstRSSET.getString("SW_INCTM")==null && M_rstRSSET.getString("SW_OUTTM")==null))
				{
					M_strSQLQRY1=" Update HR_SWMST set SW_LVECD= '"+tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString()+"',SW_LVEQT= '"+tblDSTRB.getValueAt(P_intROWNO,TB2_LVEQT).toString()+"' where";
					M_strSQLQRY1+=" SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SW_EMPNO = '"+txtEMPNO.getText()+"' and";
					M_strSQLQRY1+=" SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'"; 	
					//System.out.println(">>>insert or update>>"+M_strSQLQRY1);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY1 ,"setLCLUPD");	
				}					
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();	
				
			insSSTRN(P_intROWNO);

			flgLVSSN=true;
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeAUTHR");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeAUTHR"); 
      }
	}
	
    /* On Save Button click data is inserted or updated into the respective tables */
	private void exeADDREC(int P_intROWNO)
	{ 
	  try
	  {
			String strSQLQRY;
			ResultSet rstRSSET;
			strSQLQRY=" select count(*) from HR_LVTRN where";
			strSQLQRY+=" LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO = '"+txtEMPNO.getText()+"' and LVT_STSFL='X' and";
			strSQLQRY+=" LVT_LVEDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse((flgPLENC_EN ? txtAPPDT.getText() : tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString())))+"'"; 	
			
			rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			System.out.println(">>>Count>>"+strSQLQRY);
			if(rstRSSET.next() && rstRSSET !=null)
			{
				if(rstRSSET.getInt(1)>0)
				{
					M_strSQLQRY =" update HR_LVTRN set";
					M_strSQLQRY += " LVT_SBSCD = '"+M_strSBSCD+"' ,";
					M_strSQLQRY += " LVT_SRLNO = '"+(flgPLENC_EN ? "1" : tblDSTRB.getValueAt(P_intROWNO,TB2_SRLNO).toString())+"' ,";
					M_strSQLQRY += " LVT_LVECD = '"+(flgPLENC_EN ? "PE" : tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString())+"' ,";
					M_strSQLQRY += " LVT_LVEQT = '"+(flgPLENC_EN ? txtLVEQT1.getText() : tblDSTRB.getValueAt(P_intROWNO,TB2_LVEQT).toString())+"' ,";
					M_strSQLQRY += " LVT_RCMBY = '"+txtRCMBY.getText()+"' ,";
					M_strSQLQRY += " LVT_SCNBY = '"+txtSCNBY.getText()+"' ,";
					M_strSQLQRY += " LVT_DESGN = '"+txtDESGN.getText()+"' ,";
					M_strSQLQRY += " LVT_MMGRD = '"+txtMMGRD.getText()+"' ,";
					M_strSQLQRY += " LVT_SSCFL = '"+intLVT_SSCFL+"' ,";
					M_strSQLQRY += " LVT_APPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtAPPDT.getText()))+"' ,";
					M_strSQLQRY += " LVT_REFDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(flgPLENC_EN ? txtAPPDT.getText() : txtSTRDT.getText()))+"' ,";
					M_strSQLQRY += " LVT_CONNO = '"+txtCONNO.getText()+"' ,";
					M_strSQLQRY += " LVT_RSNDS = '"+txtRSNDS.getText()+"' ,";
					M_strSQLQRY += " LVT_STSFL = '"+intLVT_STSFL+"' ,";
					M_strSQLQRY += " LVT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' ,";
					M_strSQLQRY += " LVT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
					if(cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
					{
						M_strSQLQRY += " ,LVT_PLCNM = '"+txtPLCNM.getText()+"' ,";
						M_strSQLQRY += " LVT_ORGNM = '"+txtORGNM.getText()+"' ,";
						String L_strMOJ="";
    					L_strMOJ+=chkCOVEH.isSelected()==true?"C":"X";
						L_strMOJ+=chkBUS.isSelected()==true?"B":"X";
						L_strMOJ+=chkTRAIN.isSelected()==true?"T":"X";
						L_strMOJ+=chkAIR.isSelected()==true?"A":"X";
						L_strMOJ+=chkHRVEH.isSelected()==true?"H":"X";
						//System.out.println("L_strMOJ>>>>"+L_strMOJ);
						M_strSQLQRY += " LVT_MOJCD = '"+L_strMOJ+"'";
					}
					M_strSQLQRY +=" where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_LVEDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'";
				}
				else
				{	
					M_strSQLQRY =" insert into HR_LVTRN(LVT_CMPCD,LVT_SBSCD,LVT_EMPNO,LVT_LVEDT,LVT_SRLNO,LVT_LVECD,LVT_LVEQT,LVT_RCMBY,LVT_SCNBY,LVT_DESGN,LVT_MMGRD,LVT_SSCFL,LVT_APPDT,LVT_REFDT,LVT_CONNO,LVT_RSNDS,LVT_STSFL,LVT_LUSBY,LVT_LUPDT,LVT_TRNFL";
					if(cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
						M_strSQLQRY += ",LVT_PLCNM,LVT_ORGNM,LVT_MOJCD";
					M_strSQLQRY += " )";
					M_strSQLQRY += " values (";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+M_strSBSCD+"',";
					M_strSQLQRY += "'"+txtEMPNO.getText()+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(flgPLENC_EN ? txtAPPDT.getText() : tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"',";
					M_strSQLQRY += "'"+(flgPLENC_EN ? "1" : tblDSTRB.getValueAt(P_intROWNO,TB2_SRLNO).toString())+"',";
					M_strSQLQRY += "'"+(flgPLENC_EN ? "PE" : tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString())+"',";
					M_strSQLQRY += "'"+(flgPLENC_EN ? txtLVEQT1.getText() : tblDSTRB.getValueAt(P_intROWNO,TB2_LVEQT).toString())+"',";
					M_strSQLQRY += "'"+txtRCMBY.getText()+"',";
					M_strSQLQRY += "'"+txtSCNBY.getText()+"',";
					M_strSQLQRY += "'"+txtDESGN.getText()+"',";
					M_strSQLQRY += "'"+txtMMGRD.getText()+"',";
					M_strSQLQRY += "'"+intLVT_SSCFL+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtAPPDT.getText()))+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(flgPLENC_EN ? txtAPPDT.getText() : txtSTRDT.getText()))+"',";
					M_strSQLQRY += "'"+txtCONNO.getText()+"',";
					M_strSQLQRY += "'"+txtRSNDS.getText()+"',";
					M_strSQLQRY += "'"+intLVT_STSFL+"',";
					M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
					M_strSQLQRY += "'0'";
					if(cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
					{
						M_strSQLQRY += ",'"+txtPLCNM.getText()+"',";
						M_strSQLQRY += "'"+txtORGNM.getText()+"',";
						String L_strMOJ="";
    					L_strMOJ+=chkCOVEH.isSelected()==true?"C":"X";
						L_strMOJ+=chkBUS.isSelected()==true?"B":"X";
						L_strMOJ+=chkTRAIN.isSelected()==true?"T":"X";
						L_strMOJ+=chkAIR.isSelected()==true?"A":"X";
						L_strMOJ+=chkHRVEH.isSelected()==true?"H":"X";
						//System.out.println("L_strMOJ>>>>"+L_strMOJ);
						M_strSQLQRY += "'"+L_strMOJ+"'";
					}
					M_strSQLQRY += ")";
				}
			System.out.println(">>>insert>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			rstRSSET.close();
		}
		else
			setMSG("No Data Found in exeMODREC()",'E');		
		  //insSSTRN(P_intROWNO);
			
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeADDREC");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeADDREC()"); 
      }
	}
	
	/** Insertion Or Updations are made on HR_SSTRN
	 */
	private void insSSTRN(int P_intROWNO)
	{
		try
		{
			M_strSQLQRY=" select count(*) from HR_SSTRN where";
			M_strSQLQRY+=" SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SS_EMPNO = '"+txtEMPNO.getText()+"' and";
			M_strSQLQRY+=" SS_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'"; 	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>Count>>"+M_strSQLQRY);
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				if(M_rstRSSET.getInt(1)>0)
				{
					M_strSQLQRY1=" Update HR_SSTRN set SS_LVECD= '"+tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString()+"' where";
					M_strSQLQRY1+=" SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SS_EMPNO = '"+txtEMPNO.getText()+"' and";
					M_strSQLQRY1+=" SS_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'"; 	
				}
				else
				{
					M_strSQLQRY1=" insert into HR_SSTRN(SS_CMPCD,SS_SBSCD,SS_EMPNO,SS_WRKDT,SS_LVECD,SS_STSFL,SS_LUSBY,SS_LUPDT,SS_TRNFL)";
					M_strSQLQRY1 += " values (";
					M_strSQLQRY1 += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY1 += "'"+M_strSBSCD+"',";
					M_strSQLQRY1 += "'"+txtEMPNO.getText()+"',";
					M_strSQLQRY1 += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"',";					
					M_strSQLQRY1 += "'"+tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString()+"',";
					M_strSQLQRY1 += "'0',";
					M_strSQLQRY1 += "'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY1 += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
					M_strSQLQRY1 += "'0')";
				}	
				
					//System.out.println(">>>insert or update>>"+M_strSQLQRY1);
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY1 ,"setLCLUPD");	
			}
				if(M_rstRSSET != null)
					M_rstRSSET.close();			
		}	
		catch(Exception L_EX)
		{
			cl_dat.M_flgLCUPD_pbst=false;
			cl_dat.exeDBCMT("insSSTRN");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"insSSTRN()"); 
		}
	}	
	
	/** if eave is recommended i.e. if LVT_STSFL=1 delete that record
	 *	otherwise update that record , set stsfl='X'
	 */
	private void exeDELREC(int P_intROWNO)
	{
	  try
	  {
		  String strSQLQRY;
		  ResultSet rstRSSET;
		  strSQLQRY  =" Select LVT_STSFL from HR_LVTRN";
		  strSQLQRY +=" where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_LVEDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'";
		  rstRSSET=cl_dat.exeSQLQRY(strSQLQRY);
		  if(rstRSSET !=null && rstRSSET.next())
		  {		  
				if(rstRSSET.getString("LVT_STSFL").equals("1"))	
				{
						M_strSQLQRY = "Delete from HR_LVTRN";			  
						M_strSQLQRY +=" where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_LVEDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'";
				}
				else
				{	  
						M_strSQLQRY = " Update HR_LVTRN set";
    					M_strSQLQRY +=" LVT_STSFL='X',";
						M_strSQLQRY +=" LVT_RSNDS='"+txtRSNDS3.getText()+"' where";
						M_strSQLQRY +=" LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_LVEDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'";
				}
		  
				//System.out.println(">>>delete>>"+M_strSQLQRY);
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");				
				rstRSSET.close();
		  }
		  else
			setMSG("Error while Retrieving data in exeDELREC()",'E');	  
 	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELREC()");		
	  }
	}
	
	private void exeMODREC(int P_intROWNO)
	{
	    try
	    {
			M_strSQLQRY = " Update HR_LVTRN set";
			M_strSQLQRY +=" LVT_LVECD='"+tblDSTRB.getValueAt(P_intROWNO,TB2_LVECD).toString()+"',";
			M_strSQLQRY +=" LVT_LVEQT='"+tblDSTRB.getValueAt(P_intROWNO,TB2_LVEQT).toString()+"',";
			M_strSQLQRY +=" LVT_CONNO='"+txtCONNO.getText().trim()+"',";
			M_strSQLQRY +=" LVT_RSNDS='"+txtRSNDS.getText().trim()+"',";
			M_strSQLQRY +=" LVT_RCMBY='"+txtRCMBY.getText().trim()+"',";
			M_strSQLQRY +=" LVT_SCNBY='"+txtSCNBY.getText().trim()+"',";
			M_strSQLQRY +=" LVT_DESGN='"+txtDESGN.getText().trim()+"',";
			M_strSQLQRY +=" LVT_MMGRD='"+txtMMGRD.getText().trim()+"',";
			M_strSQLQRY +=" LVT_SSCFL='"+intLVT_SSCFL+"',";
			M_strSQLQRY +=" LVT_STSFL='"+intLVT_STSFL+"',";
			M_strSQLQRY +=" LVT_SRLNO='"+tblDSTRB.getValueAt(P_intROWNO,TB2_SRLNO).toString()+"'";
			if(cmbTRNTP.getSelectedItem().equals(strTDUTY_EN))
			{
				M_strSQLQRY += " ,LVT_PLCNM = '"+txtPLCNM.getText()+"' ,";
				M_strSQLQRY += " LVT_ORGNM = '"+txtORGNM.getText()+"' ,";
				String L_strMOJ="";
    			L_strMOJ+=chkCOVEH.isSelected()==true?"C":"X";
				L_strMOJ+=chkBUS.isSelected()==true?"B":"X";
				L_strMOJ+=chkTRAIN.isSelected()==true?"T":"X";
				L_strMOJ+=chkAIR.isSelected()==true?"A":"X";
				L_strMOJ+=chkHRVEH.isSelected()==true?"H":"X";
				//System.out.println("L_strMOJ>>>>"+L_strMOJ);
				M_strSQLQRY += " LVT_MOJCD = '"+L_strMOJ+"'";
			}
			M_strSQLQRY +="  where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LVT_EMPNO='"+txtEMPNO.getText()+"' and LVT_LVEDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblDSTRB.getValueAt(P_intROWNO,TB2_LVEDT).toString()))+"'";
			
			//System.out.println(">>>update>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	    }
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODREC()");
	    }
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
                        staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
                        staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
                        staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
                        staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
                        staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
                        staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
                        staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
                        staCDTRN[intAE_CMT_MODLS] = getRSTVAL(L_rstRSSET,"CMT_MODLS","C");
                        LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
				//if(strCGSTP.equals("HR01RH8"))
				//	System.out.println("Adding : "+strCGMTP+strCGSTP+strCODCD);
				hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
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


		/** One time data capturing for specified codes from HR_LVTRN
		 * into the Hash Table
		 */
         private void crtLVDTL(String LP_EMPNO)
        {
			String L_strSQLQRY = "";
            try
            {
                strYSTDT = "01/01/"+fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4);
	            strYENDT = "31/12/"+fmtYYYYMMDD.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)).substring(0,4);
				hstLVDTL.clear();
		        L_strSQLQRY = "select * from hr_lvtrn where LVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lvt_empno = '"+LP_EMPNO+"' and lvt_lvedt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strYSTDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strYENDT))+"' order by lvt_lvedt";
			    //System.out.println(L_strSQLQRY);
		        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
                if(L_rstRSSET == null || !L_rstRSSET.next())
                {
					L_rstRSSET.close();
		            //setMSG("Records not found in HR_LVTRN",'E');
                    return;
                }
    			
                while(true)
                {
						String L_strLVEDT = getRSTVAL(L_rstRSSET,"LVT_LVEDT","D");
                        String[] staLVDTL = new String[intLVDTL_TOT];
                        staLVDTL[intAE_LVT_LVEDT] = getRSTVAL(L_rstRSSET,"LVT_LVEDT","D");
                        staLVDTL[intAE_LVT_SRLNO] = getRSTVAL(L_rstRSSET,"LVT_SRLNO","C");
                        staLVDTL[intAE_LVT_LVECD] = getRSTVAL(L_rstRSSET,"LVT_LVECD","C");
                        staLVDTL[intAE_LVT_LVEQT] = getRSTVAL(L_rstRSSET,"LVT_LVEQT","C");
                        staLVDTL[intAE_LVT_SSCFL] = getRSTVAL(L_rstRSSET,"LVT_SSCFL","C");
                        staLVDTL[intAE_LVT_STSFL] = getRSTVAL(L_rstRSSET,"LVT_STSFL","C");
						if(!getRSTVAL(L_rstRSSET,"LVT_STSFL","C").equals("X"))
						{
							hstLVDTL.put(fmtYYYYMMDD.format(M_fmtLCDAT.parse(L_strLVEDT)),staLVDTL);
							//System.out.println("Adding : "+fmtYYYYMMDD.format(M_fmtLCDAT.parse(L_strLVEDT))+"   "+getRSTVAL(L_rstRSSET,"LVT_LVECD","C"));
						}
                        if (!L_rstRSSET.next())
                                break;
                }
                L_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtLVDTL");
            }
		}
		 


		 
		 
		/** Updating leave entries into hash table hstLVDTL
		 * 
		 */
         private void crtLVDTL_ENT()
        {
			String L_strSQLQRY = "";
            try
            {
				for(int i=0;i<tblDSTRB.getRowCount();i++)
				{
					if(tblDSTRB.getValueAt(i,TB2_LVEDT).toString().length()<10)
						break;
					String L_strLVEDT = tblDSTRB.getValueAt(i,TB2_LVEDT).toString();
                    String[] staLVDTL = new String[intLVDTL_TOT];
                    staLVDTL[intAE_LVT_LVEDT] = tblDSTRB.getValueAt(i,TB2_LVEDT).toString();
                    staLVDTL[intAE_LVT_SRLNO] = tblDSTRB.getValueAt(i,TB2_SRLNO).toString();
                    staLVDTL[intAE_LVT_LVECD] = tblDSTRB.getValueAt(i,TB2_LVECD).toString();
                    staLVDTL[intAE_LVT_LVEQT] = tblDSTRB.getValueAt(i,TB2_LVEQT).toString();
                    staLVDTL[intAE_LVT_SSCFL] = "0";
                    staLVDTL[intAE_LVT_STSFL] = "0";
					//if(!tblDSTRB.getValueAt(i,TB2_LVECD).toString().equals("XX"))
					//{
	                    hstLVDTL.put(fmtYYYYMMDD.format(M_fmtLCDAT.parse(L_strLVEDT)),staLVDTL);
						//System.out.println("Adding (1) : "+fmtYYYYMMDD.format(M_fmtLCDAT.parse(L_strLVEDT))+"   "+tblDSTRB.getValueAt(i,TB2_LVECD).toString());
					//}
				}
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtLVDTL_ENT");
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
                else if (LP_FLDNM.equals("CMT_SHRDS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
                else if (LP_FLDNM.equals("CMT_CHP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
                else if (LP_FLDNM.equals("CMT_CHP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
                else if (LP_FLDNM.equals("CMT_NMP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
                else if (LP_FLDNM.equals("CMT_NMP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
                else if (LP_FLDNM.equals("CMT_NCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
                else if (LP_FLDNM.equals("CMT_CCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
                else if (LP_FLDNM.equals("CMT_MODLS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_MODLS];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return "";
        }
		 

		/**
		 */
        private String getLVDTL(String LP_LVDTL_KEY, String LP_FLDNM)
        {
        try
        {
				if(!hstLVDTL.containsKey(LP_LVDTL_KEY))
					{setMSG(LP_LVDTL_KEY+" not found in hstLVDTL hash table",'E'); return " ";}
                if (LP_FLDNM.equals("LVT_LVEDT"))
                        return ((String[])hstLVDTL.get(LP_LVDTL_KEY))[intAE_LVT_LVEDT];
                else if (LP_FLDNM.equals("LVT_LVECD"))
                        return ((String[])hstLVDTL.get(LP_LVDTL_KEY))[intAE_LVT_LVECD];
                else if (LP_FLDNM.equals("LVT_LVEQT"))
                        return ((String[])hstLVDTL.get(LP_LVDTL_KEY))[intAE_LVT_LVEQT];
                else if (LP_FLDNM.equals("LVT_SSCFL"))
                        return ((String[])hstLVDTL.get(LP_LVDTL_KEY))[intAE_LVT_SSCFL];
                else if (LP_FLDNM.equals("LVT_STSFL"))
                        return ((String[])hstLVDTL.get(LP_LVDTL_KEY))[intAE_LVT_STSFL];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getLVDTL");
		}
        return "";
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




	
	
	/**
	 */
	public void mousePressed(MouseEvent L_ME)
	{
		super.mousePressed(L_ME);
		try
		{
			if (M_objSOURC==chkCHKFL3)
				objTBLVRF.setSource(tblTELVE);
		}
		catch(Exception e){setMSG(e,"mousePressed");}
	}

	
	/**
	 * <b>TASKS : </b><br>
	 * Reset System idle time timer at every click<br>
	 * Copy refernce of soure to M_objSOURC
	 */
	public void mouseClicked(MouseEvent L_ME)
	{
		super.mouseClicked(L_ME);
		try
		{
		}
		catch(Exception L_EX){setMSG(L_EX,"mouseClicked");}
	} /** Initializing table editing before poulating/capturing data
 */
private void inlTBLEDIT(JTable P_tblTBLNM)
{
	if(!P_tblTBLNM.isEditing())
		return;
	P_tblTBLNM.getCellEditor().stopCellEditing();
	P_tblTBLNM.setRowSelectionInterval(0,0);
	P_tblTBLNM.setColumnSelectionInterval(0,0);
			
}
}

