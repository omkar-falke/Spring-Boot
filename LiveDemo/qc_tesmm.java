/*
System Name   : Lebortory Information Management System
Program Name  : In Process \Other Test details.
Program Desc. : Entery screen for In Process and Other Test details.
Author        : Mr.S.R.Mehesare
Date          : 9 JULY 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.sql.ResultSet;import java.sql.Timestamp;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JTable;import javax.swing.JComboBox;
import javax.swing.InputVerifier;import java.util.StringTokenizer;
import java.util.Vector;import javax.swing.table.DefaultTableColumnModel;
import javax.swing.DefaultCellEditor;import javax.swing.JComponent;
/**
<P><PRE>
<b>System Name :</b> Lebortory Information Management System
 
<b>Program Name :</b> In Process \ Other Test details.

<b>Purpose :</b>This module is used for addition, modification,deletion and enquiry 
     of  REB Analysis,TBC bed analysis,SM Tank content ,Sm tankers, FO/Mo entries. 
     It updates Test details in qc_smtrn and remarks  in (qc_rmmst) table.Test number
     generation and updating is done using codes transaction.

List of tables used :
Table Name     Primary key                         Operation done
                                             Insert  Update  Query  Delete	
----------------------------------------------------------------------------------------------
QC_SMTRN       SMT_QCATP,SMT_TSTTP,SMT_TSTNO,
               SMT_MORTP,SMT_TSTDT,SMT_TSTRF   #        #      #      #
QC_RMMST       RM_QCATP,RM_TSTTP,RM_TSTNO      #        #      #      #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD            #      #
CO_PRMST       PR_PRDCD                                        #
----------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name   Table name    Type/Size       Description
----------------------------------------------------------------------------------------------
cmbPRDTP    CMT_CODCD     CO_CDTRN      VARCHAR(15)     Lab Type
txtTSTTP    CMT_CODCD     CO_CDTRN      VARCHAR(15)     Test Type
txtTSTDS    CMT_CODDS     CO_CDTRN      VARCHAR(30)     Test Description
txtTSTNO    CMT_CCSVL     CO_CDTRN			
txtTSTBY    CMT_CODCD     CO_CDTRN      VARCHAR(15)     Tested by
txtMORTP    SMT_MORTP     QC_SMTRN      VARCHAR(2)      Material of origin	
txtTSTRF    SMT_TSTRF     QC_SMTRN      VARCHAR(12)     Test Ref.
txtPRDCD    PR_PRDCD      CO_PRMST      VARCHAR(10)     Product code
txtPRDDS    PR_PRDDS      CO_PRMST      VARCHAR(45)     Grade
txtTNKCT    SMT_TNKCT     QC_SMTRN      decimal(2,0)    Tanker count		
txtTKLCD    SMT_TKLCD     QC_SMTRN      VARCHAR(2)      Locaton desc.
txtVEHNO    RM_REMDS      QC_RMMST      VARCHAR(200)    Remark(Vehical no)	
txtTSTDT    SMT_TSTDT     QC_SMTRN      Timestamp       Test date & time
txtQLTFL    SMT_QLTFL     QC_SMTRN      VARCHAR(1)      Quality Flag
txtRMKDS    RM_REMDS      QC_RMMST      VARCHAR(200)    Remark
----------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description         Display Columns                Table Name
----------------------------------------------------------------------------------------------
txtTSTTP    Test type, desc             CMT_CODCD,CMT_CODDS            CO_CDTRN
txtTSTBY    User Initial, Name          CMT_CODCD,CMT_CODDS            CO_CDTRN/AUT/QCXXTST
                                        CMT_CHP01 = LM_QCATP
txtTSTNO    Test No,Test date,Test Ref. SMT_TSTNO,SMT_TSTDT,SMT_TSTRF  QC_SMTRN
txtTSTRF    Test Ref., Description      CMT_CODCD,CMT_CODDS            CO_CDTRN
----------------------------------------------------------------------------------------------

<B>Queries:</b>
<I><B>To Insert New Record :</B> 
To insert new Record at first Serial Number is generated for the Test.
   Last updated Serial Number is fetched from table CO_CDTRNfor condiations
   1) CMT_CGMTP = "D"+cl_dat.M_strCMPCD_pbst
   2) CMT_CGSTP = "QCXXTST"
   3) CMT_CODCD = txtTSTTP.getText().trim()+"_"+ cl_dat.M_strFNNYR_pbst.substring(3) + M_strSBSCD.substring(0,2);
   then this Serial number is incremented by one & appended to finanacial Year Char & QCA Type 2 Char + Serial 
   Number of 5 Char. i.e. financial Year 6, QCA Type 01 for Polystyrene & Serial Number 210
   then the Serial Number will be 61000211

Quality parameter Values are inserted in the Table QC_SMTRN.

For test Type is 0301 OR 0303
    Fields WB_QLLRF, WB_SMPTM, WB_TSTTM, WB_LUSBY & WB_LUPDTis updated in Teble MM_WBTRN for
      1) WB_DOCTP = '01' 
      2) AND WB_DOCNO in all the gate-in for given day & material.
      if above updataion is not successful the Serial number is reverted in CO_CDTRN.

For Test Type 0304 Test number is Updated In the Table CO_CDTRN for
   1) CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' 
   2) AND CMT_CGSTP ='QCXXTST' 
   3) AND CMT_CODCD =given Test Type +"_"+ one char of Finanacial Year + given QCA Type.	
   
If Remark is Given the it is inserted in the Table QC_RMMST
After All these Last five Charectors of Serial Number is inserted in the Table CO_CDTRN.

<B>To Update Records : </B>
Quality Parameter Details With Values are updated in the table QC_SMTRN for
   1) SMT_QCATP = given QCA Type
   2) AND SMT_TSTTP = given Test Type
   3) AND SMT_TSTNO = given Test Number
   4) AND SMT_MORTP = given Material Origin Type
   5) AND SMT_TSTRF = given Test Refarance
   6) AND SMT_TSTDT = given Test DAte & Time.

if Remark is already present & it is modified then it is updated in QC_RMMST for 
   1) RM_QCATP = given QCA Type
   2) AND RM_TSTTP = given Test Type
   3) AND RM_TSTNO = given Test Number.
if Remark Is Not present & in Modification it is provided the it is inserted in QC_RMMST.

After all this MM_WBTRN  is update as
   WB_QLLRF = null,WB_SMPTM =null,WB_TSTTM =null,WB_LUSBY = user Code & WB_LUPDT = 
Current Date for WB_DOCTP = '01' and WB_DOCNO in(All the gate In Numbers)
											      
<B>To Delete Records : </B>
For the effect of deletation of test details from table QC_SMTRN are marked as 'X' for
    1) SMT_QCATP = given QCA Type
    2) AND SMT_TSTTP = given Test Type
    3) AND SMT_TSTNO = given Test Number
    4) AND SMT_MORTP = given Material Origin Type.
    5) AND SMT_TSTRF = given Test Refarance
    6) AND SMT_TSTDT = given Sample Date & Time

For the Effect of Deletation of Remark its ststus flag is marked as 'X' in Table RM_REMDS
    For 1) RM_QCATP = given QCA Type.
    2) AND RM_TSTTP = given Test Type.
    3) AND RM_TSTNO = given Test Number.

</I>
Validations :  
 - Test Remark if entered is saved in QC_RMMST .
      (RM_REMDS with RM_TSTTP = LM_TSTTP and RM_TSTNO = LM_TSTNO)  
 - Quality parameters  are fecthed from  CO_CDTRN table and displayed in Jtable.
 - Tested by / Test date and Time validation is done.
 - Test No generation / Updation is done using -
      exeSRLGET( "D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",LM_CODCD)	
      exeSRLGET( "D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",LM_CODCD)	
      where	LM_CODCD = LM_TSTTP +"_"+ fin. year digit + LM_QCATP
 - Tanker no. are fetched / updated in QC_RMMST with RM_TSTTP ='VEH'
*/

class qc_tesmm extends cl_pbase
{ 									/**	JTextField to enter & display Test Type.*/
	private JTextField txtTSTTP;	/**	JTextField to  display Test Type Description.*/	
	private JTextField txtTSTDS;	/**	JTextField to enter & display Material Origin.*/
	private JTextField txtMORTP;	/**	JTextField to enter & display Test Numner.*/
	private JTextField txtTSTNO;	/**	JTextField to enter * display initial of the Person completed the Test.*/
	private JTextField txtTSTBY;	/**	JTextField to enter & display Test Referance.*/
	private JTextField txtTSTRF;	/**	JTextField to enter Test Refarance Description.*/
	private JTextField txtTRFDS;	/**	JTextField to enter & display Product Code.*/
	private JTextField txtPRDCD;	/**	JTextField to display Product Code Description.*/
	private JTextField txtPRDDS;	/**	JTextField to enter & display Tanker Count.*/
	private JTextField txtTNKCT;	/**	JTextField to enter & display Location Code.*/ 
	private JTextField txtTKLCD;	/**	JTextField to enter & display Test Vehicle Number.*/
	private JTextField txtVEHNO;	/**	JTextField to enter & display Test Sample Date.*/
	private JTextField txtSMPDT;	/**	JTextField to enter & display Test Sample Time.*/
	private JTextField txtSMPTM;	/**	JTextField to enter & display Test Quality Flag.*/
	private JTextField txtQLTFL;	/**	JTextField to enter & display Test Remark.*/
	private JTextField txtRMKDS;	/**	JTable to enter & display Test Details.*/
	private cl_JTable tblTSTDL;		/**	JTable to enter & display Tanker Details.*/
	private cl_JTable tblTNKDL;
									/**	JTextField to enter Test parameter values.*/
	private JTextField txtVALUE;	/**	JTextField to enter Tanker Number.*/
	private JTextField txtTNKNO;	/**	JTextField to enter Gate in Date.*/ 
	private JTextField txtGINDT;	/**	JTextField to enter Gate in Time.*/ 
	private JTextField txtGINTM;	/**	JTextField to enter Gate In Number.*/
	private JTextField txtGINNO;
										/**Final Integer to represent Check Flag column of Test Details Table.*/
	private final int TB1_CHKFL = 0;	/**Final Integer to represent Quality Parameter column of Test Details Table.*/
	private final int TB1_CODCD = 1;	/**Final Integer to represent Quality Parameter Description column of Test Details Table.*/
	private final int TB1_CODDS = 2;	/**Final Integer to represent Unit of Measurement column of Test Details Table.*/
	private final int TB1_UOMDS = 3;	/**Final Integer to represent Value of Quality Parameter column of Test Details Table.*/
	private final int TB1_VALUE = 4;	
										/**Final Integer to represent Check Flag column of Tanker Details Table.*/
	private final int TB2_CHKFL = 0;	/**Final Integer to represent Tanker Number column of Tanker Details Table.*/
	private final int TB2_TNKNO = 1;	/**Final Integer to represent Gate In Date column of Tanker Details Table.*/
	private final int TB2_GINDT = 2;	/**Final Integer to represent Gate in Time column of Tanker Details Table.*/
	private final int TB2_GINTM = 3;	/**Final Integer to represent Gate In Number column of Tanker Details Table.*/
	private final int TB2_GINNO = 4;
										/** String variable for QCA Type.*/
	private String strQCATP;			/** String variable for old QCA Type.*/
	private String strOQCATP="";		/** String variable for QCA Type.*/
	private String strTABSF="";			/** String variable for Table status Flag.*/
	private String strPRDDS="";			/** String variable for Product Description.*/
										/** String variable for Remark*/
	private String strRMKDS="";			/** String variable for old Remark*/
	private String strORMKDS="";		/** String variable for vehicle List.*/
	private String strVNOLS="";			/** String variable for old vehicle List.*/
	private String strOVNOLS=""; 
											/** Final Integer for Total Rows in Test Detail Table.*/
	private final int intROWCT1_fn = 50;	/** Final Integer for Total Rows in Tanker Detail Table.*/
	private final int intROWCT2_fn = 200;
											
	                                        	/**	Final String Representing the Code for Tanker Gate in Type.*/	
	private final String strGINTP_fn ="01"; 	/**	Final String Representing the Vehicle list Type Remark to manage the vehicle 
												list updation in remark Table.*/		
	private final String strRMVHTP_fn = "VEH";
											/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();	/**	Vector Object for hold Quality Parameter Code.*/				
	private Vector<String> vtrQPRCD = new Vector<String>();	/** String variable for Quality Flag.*/
	private String strQLTFL;				/** String variable for Test Number.*/
	private String strTSTNO;			    /** String variable for Transporter Type.*/ 
    private String strTRNTP;            		
											/** String variable for Serial Number.*/ 
	private String strSRLNO,strSRLNO1;		/** String variable for Tanker List.*/
	private String strTNKSTR ="";			/** String variable for Gate In Numbers*/
	private String strGINNO ="";			/** String variable for old Gate In Number.*/
	private String strOGINNO ="";			/** Integer variable for Row Number.*/
    private int intROWNO;					/** String variable for Test Type.*/
	private String strTSTTP="";				/** String variable for Financial Year Digit.*/
	private String strYRDGT;				/** Integer variable for Tanker Count.*/
	
	//private String LM_ADDPR ="0105";			
	private int intTNKCT =0;	/** Flag to manage the insertation or updation of Remarks.*/
	boolean flgMODRM = false;	/** Flag to manage the insertation or updation of Remarks.*/
	boolean flgMODVE = false;
	boolean flgULCLR = false;	
	private final String strTBC_fn = "0202";
	private final String strREB_fn = "0203";
	private final String strFOMO_fn = "0301";
	private final String strSMTCA_fn = "0302";
	private final String strSCTA_fn = "0303";
	private final String strSCV_fn = "0304";
	private final String strHSD_fn = "0305";
	boolean flgFIRST = true;
    qc_tesmm()
	{
		super(2);
		try
		{								
			setMatrix(20,8);			
			add(new JLabel("Test Type"),2,1,1,.9,this,'R');
			add(txtTSTTP = new TxtNumLimit(4),2,2,1,1.2,this,'L');			
			add(new JLabel("Test Name"),2,3,1,.7,this,'R');			
			add(txtTSTDS = new JTextField(),2,4,1,3,this,'L');
		
			add(new JLabel("Test No."),3,1,1,.9,this,'R');
			add(txtTSTNO = new TxtNumLimit(8),3,2,1,1.2,this,'L');			
			add(new JLabel("Tested By"),3,3,1,.7,this,'R');
			add(txtTSTBY = new TxtLimit(3),3,4,1,1,this,'L');			
			add(new JLabel("Mat. Origin"),3,5,1,.7,this,'R');
			add(txtMORTP = new JTextField(),3,6,1,1,this,'L');
			
			add(new JLabel("Sample Ref."),4,1,1,.9,this,'R');
			add(txtTSTRF = new TxtLimit(12),4,2,1,1.2,this,'L');
			add(new JLabel("Ref. Desc."),4,3,1,.7,this,'R');
			add(txtTRFDS = new JTextField(),4,4,1,1,this,'L');						
			add(new JLabel("Prod. Code"),4,5,1,.7,this,'R');
			add(txtPRDCD = new JTextField(),4,6,1,1,this,'L');			
			add(txtPRDDS = new JTextField(),4,7,1,1.3,this,'L');
		
			add(new JLabel("Defined Date"),5,1,1,.9,this,'R');
			add(txtSMPDT = new TxtDate(),5,2,1,1.2,this,'L');
			add(new JLabel("Time"),5,3,1,.7,this,'R');
			add(txtSMPTM  = new TxtTime(),5,4,1,1,this,'L');			
			add(new JLabel("Location"),5,5,1,.7,this,'R');
			add(txtTKLCD = new TxtLimit(3),5,6,1,1,this,'L');			
			add(new JLabel("Count"),5,7,1,.5,this,'L'); 
			add(txtTNKCT = new JTextField(),5,7,1,.5,this,'R');
	
			add(new JLabel("QLT (OK)"),6,1,1,.9,this,'R');
			add(txtQLTFL = new JTextField(),6,2,1,1.2,this,'L');
			
			add(new JLabel("Vehicle No.  (for Tankers Only)"),7,2,1,2,this,'R');
			add(txtVEHNO = new JTextField(),7,3,1,5.3,this,'L');			
			add(new JLabel("Remark"),8,1,1,.9,this,'R');
			add(txtRMKDS = new JTextField(),8,2,1,6.3,this,'L');
			
			add(new JLabel("Test Details"),9,1,1,1,this,'R');
			add(new JLabel("Tanker Details"),9,5,1,1,this,'R');
	
			intROWNO = 0;
			strYRDGT = cl_dat.M_strFNNYR_pbst.substring(3,4);
			
			String[] L_COLHD = {"Select","Para Code","Description","UOM","Value"};
      		int[] L_COLSZ = {37,70,125,50,80};	    				
			tblTSTDL = crtTBLPNL1(this,L_COLHD,intROWCT1_fn,10,1,8.4,4,L_COLSZ,new int[]{0});	
	
			String[] L_COLHD1 = {"Select","Tanker No.","GateIn Date","Time","GateIn No."};
      		int[] L_COLSZ1 = {20,90,75,50,67};	    				
			tblTNKDL = crtTBLPNL1(this,L_COLHD1,intROWCT2_fn,10,5,8.4,3.4,L_COLSZ1,new int[]{0});									
			
			tblTSTDL.setCellEditor(TB1_VALUE, txtVALUE = new TxtNumLimit(10.3));						
			txtVALUE.addKeyListener(this);txtVALUE.addFocusListener(this);			
		
			txtTSTTP.setInputVerifier(objINPVR);
			txtTSTNO.setInputVerifier(objINPVR);
			txtTSTBY.setInputVerifier(objINPVR);
			txtPRDCD.setInputVerifier(objINPVR);
			txtSMPDT.setInputVerifier(objINPVR);
			txtTKLCD.setInputVerifier(objINPVR);
		
			setENBL(false);		
			txtTSTNO.setEnabled(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}			
	/**
	 * Super class Method overrided to enable or disable the component according to the Requriement
	 * @param P_flgSTAT boolean argument to pass State of the Component.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);		
		if(flgFIRST == true)
		{
			flgFIRST = false;
			return;
		}
		if((cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0) && (txtTSTTP.getText().trim().length()== 4))
		{
			strQCATP = M_strSBSCD.substring(2,4);	
			String L_strTEMP = txtTSTTP.getText().trim();
			strTSTTP = L_strTEMP;
			txtTSTTP.setEnabled(true);							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{			
				txtTSTBY.setEnabled(true);				
				txtMORTP.setEnabled(true);				
				txtSMPDT.setEnabled(true);
				txtSMPTM.setEnabled(true);												
				   
				if((L_strTEMP.equals(strFOMO_fn))||(L_strTEMP.equals(strSMTCA_fn))||(L_strTEMP.equals(strSCTA_fn))||(L_strTEMP.equals(strSCV_fn))||(L_strTEMP.equals(strHSD_fn)))
					txtQLTFL.setEnabled(true);									
				if((L_strTEMP.equals(strTBC_fn))||(L_strTEMP.equals(strREB_fn))||(L_strTEMP.equals(strSMTCA_fn))||(L_strTEMP.equals(strSCV_fn)))
					txtTSTRF.setEnabled(true);				
				if(L_strTEMP.equals(strFOMO_fn))
				{
					txtPRDCD.setEnabled(true);
					strTRNTP = "3";
				}
				else if(L_strTEMP.equals(strHSD_fn))
				{
					txtPRDCD.setEnabled(true);
					strTRNTP = "2";
				}
				else
					strTRNTP = "1";								
				if(L_strTEMP.equals(strSCTA_fn))
				{
					txtTKLCD.setEnabled(true);
					// added
					txtPRDCD.setEnabled(true);
				}
			}
			else
				txtTSTNO.setEnabled(true);										
		
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
			{		
				txtRMKDS.setEnabled(true);								
				tblTSTDL.cmpEDITR[TB1_VALUE].setEnabled(true);									
				tblTNKDL.cmpEDITR[TB2_CHKFL].setEnabled(true);						
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);				
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			{
				clrCOMP();				
				setMSG("Please Select an option ..",'N');				
				setENBL(false);
			}		
			else
			{							
				strQCATP = M_strSBSCD.substring(2,4);
				setMSG("Please Enter Test Type OR Press F1 to select from List..",'N');				
				setENBL(false);
				txtTSTTP.setEnabled(true);
				txtTSTTP.requestFocus();
			}
		}				
		else if(M_objSOURC == txtTSTTP)
		{	
			try
			{
			   	setENBL(false);	
				txtTSTNO.setText("");	
				txtMORTP.setText("");
				txtSMPDT.setText("");			
				txtSMPTM.setText("");
				txtTSTRF.setText("");
				txtTRFDS.setText("");
				txtRMKDS.setText("");
				txtPRDCD.setText("");
				txtPRDDS.setText("");
				tblTSTDL.clrTABLE();
				tblTNKDL.clrTABLE();
				if(txtTSTTP.getText().trim().length()== 4)
				{				
					M_strSQLQRY = "select CMT_CODDS,CMT_CCSVL from CO_CDTRN where CMT_CGMTP = 'SYS'"
					+ " AND CMT_CGSTP = 'QCXXTST' AND CMT_CODCD = '" + txtTSTTP.getText().trim()+"'"
					+ " AND CMT_NMP02 = 1 AND CMT_CHP02 = '"+ strQCATP +"'"; 								
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							strTSTTP = txtTSTTP.getText().trim();	
						    txtTSTDS.setText(nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
						    strTABSF = L_rstRSSET.getString("CMT_CCSVL").trim();
							L_rstRSSET.close();
					            /**** for temporary use */
					/*		String L_strTEMP = txtTSTTP.getText().trim();
							if((L_strTEMP.equals("strTBC_fn"))||(L_strTEMP.equals(strREB_fn)))							
								strQCATP="01";							
							else
								strQCATP="11";*/
							/***##############################*/					 
		  				}
						else
						{
							L_rstRSSET.close();
							setMSG("Invalid Test type Press F1 to select from List..",'E');
							return;
						}
					}																	
					getQPRDS();					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtTSTBY.setText(cl_dat.M_strUSRCD_pbst);
						setMSG("Enter initial of the Person who completed the test Or Press F1..",'N');
						txtTSTBY.requestFocus();
					}
					else
					{
						txtTSTNO.requestFocus();
						setMSG("Please Enter Test Number..",'N');
					}
						
				}								
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == txtTSTTP");
			}
		}    						
		else if (M_objSOURC == txtTSTNO)
		{
			String L_TSTDTM ="",L_strPRDCD="",strPRDDS="",L_strREFDS="";
			String L_STRDTM="";int i = 0;
			ResultSet L_rstRSSET;
			Timestamp tmpTMSTP;
			String L_strSQLQRY="";
			
			if(tblTSTDL.isEditing())
				tblTSTDL.getCellEditor().stopCellEditing();
			tblTSTDL.setRowSelectionInterval(0,0);
			tblTSTDL.setColumnSelectionInterval(0,0);
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			try
			{
				M_strSQLQRY = "Select * from QC_SMTRN where";								
				M_strSQLQRY += " SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP = '"+strQCATP.trim()+"' AND SMT_TSTTP ='"+txtTSTTP.getText().trim()+"' AND SMT_TSTNO = '";				
			    M_strSQLQRY += txtTSTNO.getText().trim()+"'";				
			    M_strSQLQRY += " and SMT_STSFL <> 'X'";				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    if(M_rstRSSET !=null)
				{					
					if(M_rstRSSET.next())
					{												
						tmpTMSTP =M_rstRSSET.getTimestamp("SMT_TSTDT");
						if(tmpTMSTP!= null)
							L_TSTDTM = M_fmtLCDTM.format(tmpTMSTP);
						else
							L_TSTDTM="";
						txtSMPDT.setText(L_TSTDTM);
						
						txtMORTP.setText(M_rstRSSET.getString("SMT_MORTP"));
						txtTSTRF.setText(M_rstRSSET.getString("SMT_TSTRF"));
						txtTSTBY.setText(M_rstRSSET.getString("SMT_TSTBY"));						
						txtTNKCT.setText(nvlSTRVL(M_rstRSSET.getString("SMT_TNKCT"),""));
						txtTKLCD.setText(nvlSTRVL(M_rstRSSET.getString("SMT_TKLCD"),""));
						txtQLTFL.setText(nvlSTRVL(M_rstRSSET.getString("SMT_QLTFL"),""));						
						txtTSTBY.setText(M_rstRSSET.getString("SMT_TSTBY"));					
						L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("SMT_PRDCD"),"");																						
						
						//strPRDDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""); 
						
						L_strSQLQRY ="Select CT_MATDS from CO_CTMST where ct_stsfl <>'X' and ct_matcd ='"+L_strPRDCD.trim()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
						if(L_rstRSSET !=null)
						{
							if(L_rstRSSET.next())
							{
								strPRDDS = nvlSTRVL(L_rstRSSET.getString("CT_MATDS"),""); 
							}
							L_rstRSSET.close();
						}						
						L_strREFDS= cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXLOT",txtTSTTP.getText().trim() +"_"+txtTSTRF.getText().trim());												
						txtPRDCD.setText(L_strPRDCD);						
						txtPRDDS.setText(strPRDDS);						
						txtTRFDS.setText(L_strREFDS);												
						tmpTMSTP = M_rstRSSET.getTimestamp("SMT_TSTDT");						
						if(tmpTMSTP != null)
						{
							String strTEMP = M_fmtLCDTM.format(tmpTMSTP);						
							txtSMPDT.setText(strTEMP.substring(0,10).trim());
							txtSMPTM.setText(strTEMP.substring(11).trim());							
						}													
						for(int j=0;j<vtrQPRCD.size();j++)
						{
							tblTSTDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SMT_"+vtrQPRCD.elementAt(j).toString().trim()+"VL"),""),j,TB1_VALUE);							
						}					
					}
					M_rstRSSET.close();
				}								
				M_strSQLQRY = "Select RM_TSTTP,RM_REMDS from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP.trim() + "'";				
				M_strSQLQRY += " AND RM_TSTTP IN('VEH','"+ txtTSTTP.getText().trim() +"')";				
				M_strSQLQRY += " AND RM_TSTNO = '"+	txtTSTNO.getText().trim()+"'";				
				M_strSQLQRY += " AND isnull(RM_STSFL,'') <>'X'";
								
				L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET !=null)
				{					
					String L_strTEMP="";
					strORMKDS="";strOVNOLS="";
					while(L_rstRSSET.next())
					{						
						L_strTEMP = nvlSTRVL(L_rstRSSET.getString("RM_TSTTP"),"").trim();												
						if(L_strTEMP.equals("VEH"))//for only 0301
						{
							 txtVEHNO.setText(L_rstRSSET.getString("RM_REMDS").trim());
							 strOVNOLS = txtVEHNO.getText().trim();							 
						}						
						else if(L_strTEMP.equals(strTSTTP.trim()))
						{
							txtRMKDS.setText(L_rstRSSET.getString("RM_REMDS").trim());
							strORMKDS = txtRMKDS.getText().trim();							
						}						
					}
					L_rstRSSET.close();
				}				
				//if((cmbPRDTP.getSelectedItem().toString().trim().substring(0,2).equals(strPRSTY_fn)) && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				if((txtTSTTP.getText().trim().equals(strFOMO_fn))||(txtTSTTP.getText().trim().equals(strSCTA_fn))||(txtTSTTP.getText().trim().equals(strHSD_fn)))
				{		
				    tblTNKDL.clrTABLE();			
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{						
						int L_intTEMP = 0;
						L_intTEMP = getTNKDT("ENQ",txtTSTNO.getText().trim(),0,L_strPRDCD);						
						getTNKDT("ADD",txtTSTNO.getText().trim(),L_intTEMP,L_strPRDCD);						
					}
					else				
						getTNKDT("ENQ",txtTSTNO.getText().trim(),0,L_strPRDCD);
				}				
				//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
				if(txtTSTNO.getText().trim().length()== 8)
				{
					txtRMKDS.requestFocus();
					setMSG("Enter Remark if any..",'N');
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch (Exception L_EX)
			{
				this.setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_EX,"M_objSOURC == txtTSTNO");
			}			
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);       
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{                
				if(M_objSOURC == txtTSTTP)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTSTTP";					
					String[] L_arrTBHDR = {"Test Type.","Description."};					
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+ " CMT_CGMTP = 'SYS' and CMT_CGSTP = 'QCXXTST'"
						+ " AND CMT_CHP02 ='" + strQCATP + "'";
					if(txtTSTTP.getText().length() >0)
					    M_strSQLQRY += " AND CMT_CODCD like '"+txtTSTTP.getText().trim() +"%'";
					M_strSQLQRY += " order by CMT_CODCD";					
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtPRDCD)
				{					
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPRDCD";
					String[] L_arrTBHDR = {"Product code","Description"};										
					M_strSQLQRY = "Select distinct TK_MATCD,TK_MATDS from MM_TKMST";//TK_TNKNO,
					M_strSQLQRY += " where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(TK_STSFL,'') <> 'X'" ;
					M_strSQLQRY += " AND TK_TNKTP = '01'" ;
					M_strSQLQRY += " AND TK_MATDS <> ''";
					M_strSQLQRY += " order by TK_MATDS";					
					cl_hlp(M_strSQLQRY,2,1,L_arrTBHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}				
				else if(M_objSOURC == txtTKLCD)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTKLCD";
					String[] L_arrTBHDR = {"Location Code","Description."};
   				    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where"
					+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'QC11TKL' order by CMT_CODCD";
					
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			   else if(M_objSOURC == txtTSTNO)
			   {
				   this.setCursor(cl_dat.M_curWTSTS_pbst);
   				   cl_dat.M_flgHELPFL_pbst = true;
				   M_strHLPFLD = "txtTSTNO";
				 /* if(txtTSTTP.getText().trim().equals(LM_ADDPR))
				   {
    				    M_strSQLQRY  = "Select distinct QTT_TSTRF,QTT_TSTDT,QTT_TSTNO from QC_QTTRN where"
						+" QTT_QCATP ='"+ strQCATP.trim() 
						+"' AND QTT_TSTTP = '"+ txtTSTTP.getText().trim() +"'";													
						if(txtTSTNO.getText().trim().length()>0)
							M_strSQLQRY +=" and QTT_TSTNO like '" + txtTSTNO.getText().trim() + "%'";
						M_strSQLQRY += " and QTT_STSFL <> 'X'";
						M_strSQLQRY += " order by QTT_TSTRF,QTT_TSTDT DESC";    
				   }*/
				 
					M_strSQLQRY = "select SMT_TSTNO,SMT_TSTDT,SMT_TSTRF from QC_SMTRN where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP ='"+strQCATP +"'";
					M_strSQLQRY += " and SMT_TSTTP ='"+txtTSTTP.getText().trim()+"' and SMT_STSFL <> 'X' ";
					if(txtTSTNO.getText().length() >0)
					    M_strSQLQRY += " AND SMT_TSTNO like '"+txtTSTNO.getText().trim() +"%'";
			        M_strSQLQRY += " order by SMT_TSTNO desc";				   					
				    String L_arrTBHDR[] ={ "Test No.  ","Test Date ","Test Reference"};
				   	cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,3,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);					
             	}
				if(M_objSOURC == txtTSTBY)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTSTBY";
				    M_strSQLQRY = " ";
                    M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
                    M_strSQLQRY += "'A"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'QCXXTST' order by CMT_CODCD";
                    M_strHLPFLD = "txtTSTBY";
					String[] L_arrTBHDR = {"Initial","Name"};									 	
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtMORTP)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMORTP";
				    M_strSQLQRY = " ";
                    M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from co_cdtrn where CMT_CGMTP = ";
                    M_strSQLQRY += "'SYS' and CMT_CGSTP = 'QCXXMOR'  and cmt_modls like '%";
					M_strSQLQRY += txtTSTTP.getText().trim() +"%'";
					M_strSQLQRY += " order by CMT_CODCD";                   
					String[] L_arrTBHDR = {"Code","Description"};									 	
				 	cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
                else if(M_objSOURC == txtTSTRF)
			    {
			        // for test types strTBC_fn,0203,0302, data is coming from codes trans table
					if(strTABSF.equals("CD"))
					{
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtTSTRF";						
            			M_strSQLQRY = "select SUBSTRING(CMT_CODCD,6),CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
						M_strSQLQRY += "'SYS' and CMT_CGSTP = 'QCXXLOT' and CMT_CODCD like '";
						M_strSQLQRY += txtTSTTP.getText().trim()+"%'";
						M_strSQLQRY += "order by SUBSTRING(CMT_CODCD,6)";
						String[] L_arrTBHDR ={" Test Ref.","Description"};
						cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,2,"CT");
						this.setCursor(cl_dat.M_curDFSTS_pbst);
					}
					else if(strTABSF.equals("CS"))// for test 0304, SM consignment vessel data is coming from TF Consignment data
					{
				    	M_strHLPFLD = "txtTSTRF";
    					cl_dat.M_flgHELPFL_pbst = true;										
    					String L_ARRHDR[] = {"Consignment Number","Description","Date","Product Code"};
    					M_strSQLQRY = "select distinct BE_CONNO,BE_CONDS,BE_CONDT,BE_MATCD from MM_BETRN where"
    						+" BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_STRTP ='04' ";
                        if(txtTSTRF.getText().length() >0)    
                            M_strSQLQRY +=" AND BE_CONNO like '"+txtTSTRF.getText().trim()+"'";
                        M_strSQLQRY +=" order by BE_CONDT desc";	
                     	cl_hlp	(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");					
					}
					
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"F1 Help..");                            
			}
		}
		if (L_KE.getKeyCode()== L_KE.VK_ENTER)
        {  						
			if((M_objSOURC == txtTSTBY) && (txtTSTBY.getText().trim().length() == 3))
			{
				txtMORTP.requestFocus();
				setMSG("Enter Material Origin Or Press F1 to select from list..",'N');				
			}			
			if (M_objSOURC == txtMORTP)
			{	 //strTBC_fn,0203,0302				
				if((txtTSTTP.getText().trim().equals(strTBC_fn)) ||(txtTSTTP.getText().trim().equals(strREB_fn))||(txtTSTTP.getText().trim().equals(strSMTCA_fn))||(txtTSTTP.getText().trim().equals(strSCV_fn)))
				{									    
		            txtTSTRF.setEnabled(true);
                    txtTSTRF.requestFocus();					
                    setMSG("Please enter sample referance..",'N');
				}									
				else if((txtTSTTP.getText().trim().equals(strFOMO_fn))||(txtTSTTP.getText().trim().equals(strHSD_fn)))
				{						
					txtPRDCD.requestFocus();
					setMSG("Please Enter Product Code Or press F1 to Select From List..",'N');
					intTNKCT = 1;
					txtTNKCT.setText("1");
				}
                else if(txtTSTTP.getText().trim().equals(strHSD_fn))
				{						
					txtPRDCD.requestFocus();
					setMSG("Please Enter Product Code Or press F1 to Select From List..",'N');
					intTNKCT = 1;
					txtTNKCT.setText("1");
				}								
				else
				{
				    // changed
				    txtPRDCD.requestFocus();
					setMSG("Please Enter Product Code Or press F1 to Select From List..",'N');
				//	txtSMPDT.requestFocus();
				//	setMSG("Enter Test Date..",'N');
					if(txtSMPDT.getText().trim().length() == 0)
					{
						txtSMPDT.setText(cl_dat.M_strLOGDT_pbst);
						txtSMPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());				
					}
				}
			}
			else if (M_objSOURC == txtPRDCD)
			{			
				txtSMPDT.requestFocus();
				setMSG("Enter Test Date..",'N');
				if(txtSMPDT.getText().trim().length() == 0)
				{
					txtSMPDT.setText(cl_dat.M_strLOGDT_pbst);
					txtSMPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());				
				}								
			}
			else if (M_objSOURC == txtTKLCD)
			{
				txtTKLCD.setText(txtTKLCD.getText().trim().toUpperCase());
				if((strTSTTP.equals(strFOMO_fn))||(strTSTTP.equals(strSMTCA_fn))||(strTSTTP.equals(strSCTA_fn))||(strTSTTP.equals(strSCV_fn))||(strTSTTP.equals(strHSD_fn)))
				{
					txtQLTFL.setText("Y");
					txtQLTFL.requestFocus();
					setMSG("Please Enter Test result \"Y\" For Ok & \"N\" for NOT OK",'N');				
				}
			}
			else if (M_objSOURC == txtTSTRF)
			{
				if(txtTSTRF.getText().trim().length() != 0)					
				{					
					txtSMPDT.requestFocus();
					setMSG("Enter Test Date..",'N');
					if(txtSMPDT.getText().trim().length()==0)
					{
						txtSMPDT.setText(cl_dat.M_strLOGDT_pbst);
						txtSMPTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
					}											
				}
			}																		
			else if (M_objSOURC == txtQLTFL)
			{
				txtQLTFL.setText(txtQLTFL.getText().trim());
				if((txtQLTFL.getText().trim().equals("Y"))||(txtQLTFL.getText().trim().equals("N")))
				{
					setMSG("Enter Remark if any...",'N');					
					txtRMKDS.requestFocus();										
				}
				else				
					setMSG("Value of Quality Flag to be entered as Y or N ..",'E');									
			}
			else if(M_objSOURC == txtSMPDT)
			{
				if(txtSMPDT.getText().trim().length()>0) 
				{
					txtSMPTM.requestFocus();
					setMSG("Please Enter Time..",'N');
				}
			}
			else if(M_objSOURC == txtSMPTM)
			{
				if(txtSMPTM.getText().trim().length()>0) 
				{					
				    if((txtTSTTP.getText().trim().equals(strTBC_fn))||(txtTSTTP.getText().trim().equals(strREB_fn)))					
					{						
						txtRMKDS.requestFocus();
						setMSG("Please Enter Remark if any..",'N');
						return;
					}					
					if(txtTSTTP.getText().trim().equals(strSCTA_fn))
					{
						txtTKLCD.requestFocus();
						setMSG("Please Enter Location Code Or Press F1 to Select from List..",'N');
					}
					else
					{
						if((strTSTTP.equals(strFOMO_fn))||(strTSTTP.equals(strSMTCA_fn))||(strTSTTP.equals(strSCTA_fn))||(strTSTTP.equals(strSCV_fn))||(strTSTTP.equals(strHSD_fn)))
						{
							txtQLTFL.setText("Y");
							txtQLTFL.requestFocus();
							setMSG("Please Enter Test result \"Y\" For Ok & \" N\" for NOT OK",'N');
						}
					}
				}
			}
			else if(M_objSOURC == txtQLTFL)
			{
				txtRMKDS.requestFocus();
				setMSG("Please Enter Remarks if any..",'N');
			}
			else if (M_objSOURC == txtRMKDS)
			{
				tblTSTDL.requestFocus();
				tblTSTDL.setRowSelectionInterval(0,0);				
				tblTSTDL.setColumnSelectionInterval(TB1_VALUE,TB1_VALUE);
				tblTSTDL.editCellAt(0,TB1_VALUE);				
    			tblTSTDL.cmpEDITR[TB1_VALUE].requestFocus();
				setMSG("Enter Quality paramete Details..",'N');
			}
		}						
	}
	/**
	 * Super class method to execute the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtTSTTP"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTSTTP.setText(cl_dat.M_strHLPSTR_pbst);
			txtTSTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtTSTTP.requestFocus();
			setMSG("Enter Test Type OR press F1 to select from List..",'N');
		}
		else if(M_strHLPFLD.equals("txtTSTBY"))
		{
              cl_dat.M_flgHELPFL_pbst = false;
			  txtTSTBY.setText(cl_dat.M_strHLPSTR_pbst);
		}
		else if(M_strHLPFLD.equals("txtPRDCD"))
		{			
			cl_dat.M_flgHELPFL_pbst = false;
			txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRDDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
			getTNKDT("ADD","",0,txtPRDCD.getText().trim());
		}
		else if(M_strHLPFLD.equals("txtTSTNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTSTNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
		else if(M_strHLPFLD.equals("txtMORTP"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtMORTP.setText(cl_dat.M_strHLPSTR_pbst);
		}
		else if(M_strHLPFLD.equals("txtTKLCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTKLCD.setText(cl_dat.M_strHLPSTR_pbst);
		}
		else if(M_strHLPFLD.equals("txtTSTRF"))
		{
			int i=0;
			cl_dat.M_flgHELPFL_pbst = false;
			txtTSTRF.setText(cl_dat.M_strHLPSTR_pbst);
			txtTRFDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			 
			if(strTSTTP.equals(strSCV_fn))
			   txtPRDCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());			 
		}
    }	
	/**
	 * Super class method overrided here to inhance its functionality, to perform 
	 * Database operations.
	 */
	void exeSAVE()
	{
		String L_strTNKER = "";
		String L_strTEMP="0";String L_strCODCD="";int L_intTSTNO=0;String L_strTSTNO="";
		try
		{				
			if(!vldDATA())			
				return ;			
			else
				setMSG("",'E');	
			
			strQLTFL =txtQLTFL.getText().trim();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				strTNKSTR="";				
				if(strTABSF.equals("TF"))
				{	intTNKCT = 0;
					strGINNO = "";
					for(int i=0;i<intROWCT2_fn;i++)
					{
						if(tblTNKDL.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
						{														
							if(intTNKCT == 0)
							{
								strTNKSTR = tblTNKDL.getValueAt(i,TB2_TNKNO).toString().trim();
								strGINNO  = "'"+tblTNKDL.getValueAt(i,TB2_GINNO).toString().trim()+"'";								
							}
							else
							{
								strTNKSTR += ","+tblTNKDL.getValueAt(i,TB2_TNKNO).toString().trim();
								strGINNO  += ",'"+tblTNKDL.getValueAt(i,TB2_GINNO).toString().trim()+"'";							
							}
							intTNKCT++;
						}
					}					
					L_strTNKER = strTNKSTR;
				}								
				if((txtTSTTP.getText().trim().equals(strFOMO_fn))||(txtTSTTP.getText().trim().equals(strSCTA_fn))||(txtTSTTP.getText().trim().equals(strHSD_fn)))
				{
					strTSTNO = genLOTSRL();
					txtTSTRF.setText(strTSTNO);
				}
				else
				{				
					L_strCODCD = txtTSTTP.getText().trim() + "_" + cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim();					
					
					L_strTEMP = cl_dat.getPRMCOD("CMT_CCSVL","D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",L_strCODCD);															
					L_intTSTNO = Integer.valueOf(L_strTEMP).intValue()+1;										
					strTSTNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()
						+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
						+ String.valueOf(L_intTSTNO);					
				}				
				txtTSTNO.setText(strTSTNO);	
												
				//exeSMTADD();				
				M_strSQLQRY = "Insert into QC_SMTRN(SMT_CMPCD,SMT_QCATP,SMT_TSTTP,SMT_TSTNO,SMT_MORTP,SMT_TSTRF,SMT_TSTDT,SMT_TSTBY,SMT_TKLCD";				
				M_strSQLQRY += ",SMT_TNKCT";
				for(int i=0;i<vtrQPRCD.size();i++)
				{
					if(tblTSTDL.getValueAt(i,TB1_VALUE).toString().trim().length() >0)
					M_strSQLQRY+=","+"SMT_"+vtrQPRCD.elementAt(i).toString().trim()+"VL";
				}								
				M_strSQLQRY += ",SMT_PRDCD,SMT_ADDBY,SMT_ADDTM,SMT_QLTFL,SMT_STSFL,SMT_TRNFL,SMT_LUSBY,SMT_LUPDT) values(";
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
				M_strSQLQRY += "'"+ strQCATP+"',";
				M_strSQLQRY += "'"+ strTSTTP+"',";
				M_strSQLQRY += "'"+ strTSTNO +"',"; 
				M_strSQLQRY += "'"+ txtMORTP.getText().trim()+"',";
				M_strSQLQRY += "'"+ txtTSTRF.getText().trim()+"',";				
				M_strSQLQRY += "'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSMPDT.getText().trim()+" "+ txtSMPTM.getText().trim()))+"',";				
				M_strSQLQRY += "'"+ txtTSTBY.getText().trim()+"',";
				M_strSQLQRY += "'"+ txtTKLCD.getText().trim().toUpperCase()+"',";	
				M_strSQLQRY += String.valueOf(intTNKCT).trim()+",";				
				for(int i=0;i<vtrQPRCD.size();i++)
				{
					if(tblTSTDL.getValueAt(i,TB1_VALUE).toString().trim().length() >0)
					M_strSQLQRY += tblTSTDL.getValueAt(i,TB1_VALUE).toString()+",";
				}
				M_strSQLQRY += "'"+txtPRDCD.getText().trim()+"',";
				M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";										
				M_strSQLQRY += "'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSMPDT.getText().trim()+" "+ txtSMPTM.getText().trim()))+"',";				
			//	M_strSQLQRY +="'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+ cl_dat.M_txtCLKTM_pbst.getText().trim())) +"',";
				M_strSQLQRY += "'"+strQLTFL+"',";
				M_strSQLQRY += getUSGDTL("SMT",'I',"") + ")";
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");				
								
				strORMKDS="";strOVNOLS="";
				if(txtRMKDS.getText().trim().length() >0)
					exeRMSAVE(strTSTTP,txtRMKDS.getText().trim());
				if(strTNKSTR.trim().length() > 0)
					exeRMSAVE(strRMVHTP_fn,strTNKSTR);
									
				if(cl_dat.M_flgLCUPD_pbst)
				{
					if((strTSTTP.equals(strFOMO_fn))||(strTSTTP.equals(strSCTA_fn))||(strTSTTP.equals(strHSD_fn)))
					{
						if(!updWBTRN(txtTSTNO.getText().trim(),strGINNO))					    
							setMSG("Error in Adding the Record ..",'E');						
						else						
							updLOTSRL();													
					}
					else	
					{	
						M_strSQLQRY ="Update CO_CDTRN SET CMT_CCSVL ='"+txtTSTNO.getText().substring(3)+"'";
						M_strSQLQRY += " WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP ='QCXXTST' AND CMT_CODCD ='"+txtTSTTP.getText()+"_"+cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}					
				}				
			}						
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))//exeMODREC();
			{				
				String L_strSQLQRY="";
				ResultSet L_rstRSSET;
				int L_intCOUNT = 0;
				boolean L_UPDTNK = false;
				flgMODVE = false;	
				 				
				intTNKCT = 0;
				strTNKSTR="";
				strGINNO = "";
				for(int i=0;i<intROWCT2_fn;i++)
				{
					if(tblTNKDL.getValueAt(i,TB2_CHKFL).toString().trim().equals("true"))
					{						
						if(intTNKCT == 0)
						{
							strTNKSTR = tblTNKDL.getValueAt(i,TB2_TNKNO).toString().trim();
							strGINNO  = "'"+tblTNKDL.getValueAt(i,TB2_GINNO).toString().trim()+"'";							
						}
						else
						{
							strTNKSTR += ","+tblTNKDL.getValueAt(i,TB2_TNKNO).toString().trim();
							strGINNO  += ",'"+tblTNKDL.getValueAt(i,TB2_GINNO).toString().trim()+"'";							
						}
						intTNKCT++;
					}					
				}	
				M_strSQLQRY = "Update QC_SMTRN set";
				M_strSQLQRY +=" SMT_TRNFL ='0',";
				M_strSQLQRY +="SMT_QLTFL ='"+strQLTFL+"',";				
				String L_strTEMP1 = "";
				for(int i=0;i<tblTSTDL.getRowCount();i++)
				{
					if(tblTSTDL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
					{
						L_strTEMP1 = tblTSTDL.getValueAt(i,TB1_VALUE).toString().trim();
						if(L_strTEMP1.equals(""))
							L_strTEMP1 = null;
								
						M_strSQLQRY += "SMT_"+vtrQPRCD.elementAt(i).toString()+"VL"+"="+L_strTEMP1+",";
					}
				}														
				if(strTABSF.equals("TF"))
				{					
					if(!strGINNO.trim().equals(strOGINNO))
						L_UPDTNK = true;
					else
						L_UPDTNK = false;
					
					M_strSQLQRY +="SMT_TNKCT ="+intTNKCT+",";
				    M_strSQLQRY +="SMT_TKLCD ='"+txtTKLCD.getText().trim().toUpperCase()+"',";
				 	M_strSQLQRY +="SMT_PRDCD ='"+txtPRDCD.getText().trim()+"',";
				}
				M_strSQLQRY +="SMT_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY +="SMT_LUPDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY +=" where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP ='"+strQCATP.trim()+"'";
				M_strSQLQRY +=" AND SMT_TSTTP ='"+strTSTTP.trim()+"'";
				M_strSQLQRY +=" AND SMT_TSTNO ='"+txtTSTNO.getText().trim()+"'";
				M_strSQLQRY +=" AND SMT_MORTP ='"+txtMORTP.getText().trim()+"'";
				M_strSQLQRY +=" AND SMT_TSTRF ='"+txtTSTRF.getText().trim()+"'";
				M_strSQLQRY +=" AND SMT_TSTDT ='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSMPDT.getText().trim()+" "+txtSMPTM.getText().trim()))+"'";					
				cl_dat.M_flgLCUPD_pbst = true;				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				setMSG("Modifying the test record..",'N'); 				
				exeRMSAVE(strTSTTP,txtRMKDS.getText().trim());				
				if(strTABSF.equals("TF"))
				{					
					exeRMSAVE(strRMVHTP_fn,strTNKSTR);
				}
				if(L_UPDTNK)
				{
					delWBTRN("MOD");		// removes previous quality reference
					updWBTRN(txtTSTNO.getText().trim(),strGINNO); // updates currnt references
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))			
			{
				//exeDELREC();				
				ResultSet L_rstRSSET;
				String L_strSQLQRY="";
				M_strSQLQRY = "Update QC_SMTRN SET ";			
				M_strSQLQRY += getUSGDTL("SMT",'U',"X");
				M_strSQLQRY +=" where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP ='"+strQCATP.trim()+"'";			
				M_strSQLQRY +=" AND SMT_TSTTP ='"+strTSTTP.trim()+"'";			
				M_strSQLQRY +=" AND SMT_TSTNO ='"+txtTSTNO.getText().trim()+"'";			
				M_strSQLQRY +=" AND SMT_MORTP ='"+txtMORTP.getText().trim()+"'";			
				M_strSQLQRY +=" AND SMT_TSTRF ='"+txtTSTRF.getText().trim()+"'";						
				M_strSQLQRY +=" AND SMT_TSTDT ='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSMPDT.getText().trim()+" "+txtSMPTM.getText().trim()))+"'";			
				cl_dat.M_flgLCUPD_pbst = true;
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				setMSG("Deleting the test record..",'N'); 				
				if(strORMKDS.length()>0)
				{
					M_strSQLQRY = "Update QC_RMMST set ";
					M_strSQLQRY += getUSGDTL("RM",'U',"X");				
					M_strSQLQRY +=" WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"+strQCATP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTTP ='"+strTSTTP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTNO ='"+txtTSTNO.getText().trim()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					setMSG("Deleting the Remark record..",'N'); 
				}
				if(strTABSF.equals("TF"))
				{
					delWBTRN("DEL");
				}
			}						
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
				clrCOMP();
				setENBL(false);
			}
			else
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'N');
			}
			
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
	}									
	/**
	 * Method to perform validy check of the Data entered, Before inserting 
	 *new data in the data base.
	 */
	boolean vldDATA()
	{
		try
		{
			strTSTTP = txtTSTTP.getText().trim();
			if(tblTNKDL.isEditing())
				tblTNKDL.getCellEditor().stopCellEditing();
			tblTNKDL.setRowSelectionInterval(0,0);
			tblTNKDL.setColumnSelectionInterval(0,0);
			
			if(tblTSTDL.isEditing())
				tblTSTDL.getCellEditor().stopCellEditing();
			tblTSTDL.setRowSelectionInterval(0,0);
			tblTSTDL.setColumnSelectionInterval(0,0);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
			    if((txtTSTTP.getText().trim().equals(strFOMO_fn))||(txtTSTTP.getText().trim().equals(strSCTA_fn))||(txtTSTTP.getText().trim().equals(strHSD_fn)))
			        if(flgULCLR)
			        {
			            setMSG("Unloading clearance is given,Test values can not be changed..",'E');
			            return false;
			        }
			}
			if(txtTSTTP.getText().trim().length()==0)
			{
				setMSG("Test Type cannot be Blank.. Please Enter Test Type..",'E');
				txtTSTTP.requestFocus();
				return false;
			}
			if(txtTSTTP.getText().trim().equals(strSCTA_fn))
			{
				if(txtTKLCD.getText().trim().length()==0)
				{
					setMSG("Please Enter Location Code..",'E');
					txtTKLCD.requestFocus();
					return false;
				}
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(txtTSTNO.getText().trim().length()==0)
				{
					setMSG("Test Number cannot be Blank, Please Enter Test Number..",'E');
					return false;
				}
			}
			else
			{			
				if(txtTSTBY.getText().trim().length()==0)
				{
					setMSG("Tested By field cannot be Blank.. Please Enter it..",'E');
					//txtTSTBY.requestFocus();
					return false;
				}
				if(txtMORTP.getText().trim().length()==0)
				{
					setMSG("Material origin field cannot be Blank.. Please Enter it..",'E');
					txtMORTP.requestFocus();
					return false;
				}
				if(txtSMPDT.getText().trim().length()==0)
				{
					setMSG("Sample Date cannot be Blank.. Please Enter it..",'E');
					txtSMPDT.requestFocus();
					return false;
				}
				if (M_fmtLCDAT.parse(txtSMPDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
				{			    
					setMSG("Please Enter valid Date .. ",'E');							
					txtSMPDT.requestFocus();
					return false;
				}
				if(txtSMPTM.getText().trim().length()==0)
				{
					setMSG("Sample Date-Time cannot be Blank.. Please Enter it..",'E');
					txtSMPTM.requestFocus();
					return false;
				}
				if(M_fmtLCDTM.parse(txtSMPDT.getText().trim()+" "+txtSMPTM.getText().trim()).compareTo(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))>0)
				{
					setMSG("Date Time can not be smaller than current Date Time..",'E');
					txtSMPDT.requestFocus();
					return false;
				}																					
				else if((txtTSTTP.getText().trim().equals(strTBC_fn))||(txtTSTTP.getText().trim().equals(strREB_fn)))				
				{
					if(txtTSTRF.getText().trim().length()==0)
					{
						setMSG("Sample Referance cannot be Blank.. Please Enter it..",'E');
						txtTSTRF.requestFocus();
						return false;
					}
				}
			}
			boolean L_flgTNKSL = false;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		    	if((txtTSTTP.getText().trim().equals(strFOMO_fn))||(txtTSTTP.getText().trim().equals(strSCTA_fn))||(txtTSTTP.getText().trim().equals(strHSD_fn)))
    	    	{
    	    	    for(int i=0;i<tblTNKDL.getRowCount();i++)
    	    	    {
    	    	        if(tblTNKDL.getValueAt(i,0).toString().equals("true"))
                        {    
    	    	            L_flgTNKSL = true;
    	    	            break;  
                        }
    	    	    }
    	    	    if(!L_flgTNKSL)
    	    	    {
    	    	        setMSG("Please select the Tanker Numbers ..",'E');
    	    	        return false;
    	    	    }
    	    	}
		    	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}		
	/**
	 * Method to get the quality parameter Description for given quality parameter.
	 */
	private void getQPRDS()
	{      
		String L_strCODCD="";
	    try
	    {
			if(tblTSTDL.isEditing())
				tblTSTDL.getCellEditor().stopCellEditing();
			tblTSTDL.setRowSelectionInterval(0,0);
			tblTSTDL.setColumnSelectionInterval(0,0);
			
			vtrQPRCD.removeAllElements();
	       /* M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CCSVL,CMT_CHP02 from CO_CDTRN where"
			+ " CMT_CGMTP ='SYS' AND CMT_CGSTP = 'QCXXQPR'"
			+ " AND CMT_MODLS like '%"+ txtTSTTP.getText().trim() +"%'"+" order by CMT_CHP02";*/			
			M_strSQLQRY = "Select QS_QPRCD,QS_QPRDS,QS_UOMCD from CO_QSMST where"
			+ " QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QS_TSTLS like '%"+ txtTSTTP.getText().trim() +"%'"+" order by QS_ORDBY";			
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			intROWNO = 0;
			if(L_rstRSSET !=null)
			{				
				while (L_rstRSSET.next())
				{					
					L_strCODCD = L_rstRSSET.getString("QS_QPRCD");					
					vtrQPRCD.addElement(L_strCODCD);	
					tblTSTDL.setValueAt(L_strCODCD,intROWNO,TB1_CODCD); 
					tblTSTDL.setValueAt(L_rstRSSET.getString("QS_QPRDS"),intROWNO,TB1_CODDS); 
					tblTSTDL.setValueAt(L_rstRSSET.getString("QS_UOMCD"),intROWNO,TB1_UOMDS); 
					intROWNO ++;
				}
				L_rstRSSET.close();
			}
		}
	    catch(Exception L_E)
	    {
			setMSG(L_E,"getQPRDS");
		}
	}	
	/**
	 * Method to get Tanker Details.
	 * @param P_strOPTTP String argumant to pass operation Type.
	 * @param P_strTSTNO String argument to pass Test Number.
	 * @param P_intCOUNT int argument to pass initial Tanker Count for the operation of modification.
	 * @param P_strMATCD String argument to pass Material Code.
	 */
	private int getTNKDT(String P_strOPTTP,String P_strTSTNO,int P_intCOUNT, String P_strMATCD)
	{
        boolean flgUNCLR = false;
		int L_intCOUNT =0;
		String L_strLRYNO="",L_strGINDT="",L_strDOCRF="";        
		Timestamp tmpTMSTP;
		try
		{
			if(tblTNKDL.isEditing())
				tblTNKDL.getCellEditor().stopCellEditing();
			tblTNKDL.setRowSelectionInterval(0,0);
			tblTNKDL.setColumnSelectionInterval(0,0);
			// added 
			//tblTNKDL.clrTABLE();
			M_strSQLQRY = "Select distinct WB_LRYNO,WB_GINDT,WB_DOCNO,WB_QLLRF,WB_UCLBY from MM_WBTRN"
				+" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP ='"+strGINTP_fn +"'" ;
			if (P_strMATCD.trim().length()>0)
				M_strSQLQRY +=" AND WB_MATCD ='"+ P_strMATCD +"'";
			if(P_strOPTTP.equals("ADD"))
			{
				L_intCOUNT = P_intCOUNT ;			
				M_strSQLQRY +=" AND WB_GINDT is not null";				
				M_strSQLQRY +=" AND WB_QLLRF is null";				
				M_strSQLQRY +=" Order by WB_GINDT DESC";			
			}
			else if(P_strOPTTP.equals("ENQ"))
			{
                flgUNCLR = false;    
				M_strSQLQRY += " AND WB_QLLRF ='"+P_strTSTNO.trim() +"'";
				M_strSQLQRY +=" Order by WB_GINDT DESC";
			}			
		//	System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET!= null)
			{				
				while(L_rstRSSET.next())
				{
					L_strLRYNO = nvlSTRVL(L_rstRSSET.getString("WB_LRYNO"),"");										
					tmpTMSTP = L_rstRSSET.getTimestamp("WB_GINDT");
					if(tmpTMSTP!= null)
						L_strGINDT = M_fmtLCDTM.format(tmpTMSTP);					
					else
						L_strGINDT="";
					L_strDOCRF = nvlSTRVL(L_rstRSSET.getString("WB_DOCNO"),"");
					tblTNKDL.setValueAt(L_strLRYNO,L_intCOUNT,TB2_TNKNO);
					tblTNKDL.setValueAt(L_strGINDT.substring(0,10).trim(),L_intCOUNT,TB2_GINDT);
					tblTNKDL.setValueAt(L_strGINDT.substring(11).trim(),L_intCOUNT,TB2_GINTM);
					tblTNKDL.setValueAt(L_strDOCRF,L_intCOUNT,TB2_GINNO);
					if(P_strOPTTP.equals("ENQ"))
					    if(nvlSTRVL(L_rstRSSET.getString("WB_UCLBY"),"").length() >0)
                           flgULCLR = true; 
					if(P_strOPTTP.equals("ENQ")) //||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					{
						tblTNKDL.setValueAt(new Boolean(true),L_intCOUNT,0);
						if(L_intCOUNT ==0)						
							strOGINNO  = "'"+tblTNKDL.getValueAt(L_intCOUNT,TB2_GINNO).toString().trim()+"'";						
						else						
							strOGINNO  += ",'"+tblTNKDL.getValueAt(L_intCOUNT,TB2_GINNO).toString().trim()+"'";												
					}
					L_intCOUNT ++;
				}				
				L_rstRSSET.close();
			}
			return L_intCOUNT;
		}
		catch (Exception L_EX)
        {
			setMSG(L_EX,"getTNKDT");
			return L_intCOUNT;
        }
	}
	/**
	 *Method to update the Test No, Sample DateTime & test DateTime.
	 * @param P_strQLLRF String argumnet to pass Test Number.
	 * @param P_strGINNO String Argument to pass Gate In Number.
	 */
	private boolean updWBTRN(String P_strQLLRF,String P_strGINNO)
	{
		int L_intCOUNT = 0;
		ResultSet L_rstRSSET;
		try
		{			
			setMSG("Updating the tanker data..",'N');
			M_strSQLQRY = "UPDATE MM_WBTRN set WB_QLLRF ='"+P_strQLLRF+"',";
			M_strSQLQRY += " WB_SMPTM ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtSMPDT.getText().trim()+" "+txtSMPTM.getText().trim())) +"',";
			M_strSQLQRY += " WB_TSTTM ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
			M_strSQLQRY += " WB_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += " WB_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '01' and WB_DOCNO in("+P_strGINNO+")";					
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		//////// this block added on 13/08/2003 to handle the double challan case.
			if(cl_dat.M_flgLCUPD_pbst)
			{
				M_strSQLQRY = "select WB_DOCTP from MM_WBTRN WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '01' AND WB_QLLRF is null";
				M_strSQLQRY += " AND getdate() - CONVERT(varchar,WB_GINDT,103) > 3 ";
				M_strSQLQRY += " AND WB_GINDT is not null ";
				L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(L_rstRSSET != null)
				{
					if(L_rstRSSET.next())
					{
						L_intCOUNT = 1;
					}
					L_rstRSSET.close();
				}					
				if(L_intCOUNT > 0)//if(cl_dat.getRECCNT("SP","ACT",M_strSQLQRY) >0)
				{
					M_strSQLQRY = "UPDATE MM_WBTRN set WB_QLLRF ='DCH',"; // DCH for double challan
					M_strSQLQRY += "WB_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "WB_LUPDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY += " WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '01' and WB_QLLRF is null";
					M_strSQLQRY += " and getdate() - CONVERT(varchar,WB_GINDT,103) > 3 ";
					M_strSQLQRY += " and WB_GINDT is not null ";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst)
						return true;
					else
						return false;
				}
				else
					return true;
			}
		///////////	
			else return false;						
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"updLOTSRL");			
		}
		return false;
	}	
	/**
	 * Method to update the Remark
	 * @param P_strTSTTP String argument to pass Test Type.
	 * @param P_strRMKDS String argument to pass Remark Description.	 
	 */
	private void exeRMSAVE(String P_strTSTTP,String P_strRMKDS)
	{
		try
		{			
			if((!strORMKDS.equals(P_strRMKDS)) ||(!strOVNOLS.equals(P_strRMKDS)))			
			{								
				if(((strORMKDS.length()!=0)&&(!P_strTSTTP.equals("VEH"))) ||((strOVNOLS.length()!=0) && (P_strTSTTP.equals("VEH"))))
				{
					M_strSQLQRY = "Update qc_rmmst set ";
					M_strSQLQRY +="RM_TRNFL ='0',";
					if(P_strRMKDS.length()>0)				
						M_strSQLQRY +="RM_REMDS ='"+P_strRMKDS.trim()+"',";				
					else				
						M_strSQLQRY +="RM_STSFL ='X',";
					M_strSQLQRY +="RM_LUSBY ='"+cl_dat.M_strUSRCD_pbst.trim()+"',";	
					M_strSQLQRY +="RM_LUPDT ='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY +=" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='"+strQCATP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTTP ='"+P_strTSTTP.trim()+"'";
					M_strSQLQRY +=" AND RM_TSTNO ='"+txtTSTNO.getText().trim()+"'";				
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					setMSG("Updating the Remark record..",'N'); 	
				}
				else if((strOVNOLS.equals("")) ||(strORMKDS.equals("")))
				{
					M_strSQLQRY = "Insert into QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)values(";
					M_strSQLQRY +="'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY +="'"+strQCATP.trim()+"',";
					M_strSQLQRY +="'"+P_strTSTTP.trim()+"',";
					M_strSQLQRY +="'"+txtTSTNO.getText().trim()+"',";
					M_strSQLQRY +="'"+P_strRMKDS.trim()+"',";
					M_strSQLQRY +="'0',";
					M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY +="'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					setMSG("Updating the Remark record..",'N'); 
				}
			}			
		}
		catch(Exception L_EX)	
		{
			setMSG(L_EX,"exeRMSAVE");
		}
	}		
	/**
	 * Method to update Lot serial Number.
	 */
	private boolean updLOTSRL()
	{
		try
		{
			M_strSQLQRY = "UPDATE CO_CDTRN set CMT_CCSVL = " + "'" + strSRLNO1+ "',CMT_TRNFL ='0'";
			M_strSQLQRY += " WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'QCXXTFM'";
			M_strSQLQRY += " AND CMT_CODCD = "+"'"+strYRDGT+strTRNTP.trim() + "'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst)
				return true;
			else
				return false;		
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"updLOTSRL");			
		}
		return false;
	}	
	/**
	 * Method to initialise the serial Number, Sample DateTime & Test DateTime
	 * @param P_strOPRFL String argument to pass operation Type.
	 */
	private boolean delWBTRN(String P_strOPRFL)
	{
		String strTEMP="";
		try
		{
			M_strSQLQRY = "UPDATE MM_WBTRN set WB_QLLRF = null,"; // status flag
			M_strSQLQRY += "WB_SMPTM = null,WB_TSTTM = null,";
			M_strSQLQRY += "WB_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "WB_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '01' and WB_DOCNO in(";
			if(P_strOPRFL.equals("DEL"))
			{				
				for(int i=0;i<tblTNKDL.getRowCount();i++)
				{
					strTEMP = tblTNKDL.getValueAt(i,TB2_GINNO).toString().trim();
					if(strTEMP.length()>0)
					{
						if(i == 0)						
							M_strSQLQRY += "'"+ strTEMP +"'";						
						else						
							M_strSQLQRY += ",'"+ strTEMP +"'";						
					}
				}
			}
			else if(P_strOPRFL.equals("MOD"))
			{				
	 		 	for(int i=0;i<intROWCT2_fn;i++)
				{
					strTEMP = tblTNKDL.getValueAt(i,TB2_GINNO).toString().trim();
					if(strTEMP.length()>0)
					{									
			 			if(i == 0)			 				
			 				M_strSQLQRY += "'"+ strTEMP +"'";
			 			else			 				
			 				M_strSQLQRY += ",'"+ strTEMP +"'";			 							 			
					}
				}
			}			
			M_strSQLQRY +=")";			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst)
				return true;
			else
				return false;		
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"updLOTSRL");			
		}
		return false;
	}
	/**
	 * Method to generate the Lot Serial number.
	 */
	private String genLOTSRL()
	{
		try
		{
			strSRLNO1 = "";
	        strSRLNO = cl_dat.getPRMCOD("CMT_CCSVL","D"+cl_dat.M_strCMPCD_pbst,"QCXXTFM",strYRDGT.trim()+strTRNTP.trim());			
			if(strSRLNO != null)
			{
				int i = Integer.valueOf(strSRLNO).intValue();
				i = i + 1;
				strSRLNO1 = strSRLNO1.valueOf(i);			
				int L_STRLEN = 6 - strSRLNO1.trim().length();
				for (int j=0;j < L_STRLEN;j++)
				    strSRLNO1 = "0" + strSRLNO1;
				strSRLNO = strYRDGT + strTRNTP.trim()+strSRLNO1;								
				return strSRLNO;				
			}
	        else
	           return "";			
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"genLOTSRL");
			return "";
		}		
	}	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{			
				if((input == txtTSTNO) && (txtTSTNO.getText().trim().length() == 8))
				{
					M_strSQLQRY = "select SMT_TSTNO,SMT_TSTDT,SMT_TSTRF from QC_SMTRN"
						+" where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP ='"+strQCATP +"'"
						+" AND SMT_TSTTP ='"+txtTSTTP.getText().trim()
						+"' AND SMT_STSFL <> 'X' order by SMT_TSTNO desc";										
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
							L_rstRSSET.close();													
						else
						{
							L_rstRSSET.close();
							setMSG("mmmm Invalid Test number..",'E');
							return false;
						}						
					}
				}				
				if((input == txtTSTBY) && (txtTSTBY.getText().trim().length() == 3))
				{					
					int L_intCOUNT=0;
					String L_strCODCD = "";										
					txtTSTBY.setText(txtTSTBY.getText().trim().toUpperCase());											
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
					M_strSQLQRY += "'A"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'QCXXTST' and CMT_CODCD = '" + txtTSTBY.getText().trim().toUpperCase() + "'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							{							
								M_strSQLQRY = "select CMT_CODCD from co_cdtrn where CMT_CGMTP = ";
								M_strSQLQRY += "'SYS' and CMT_CGSTP = 'QCXXMOR'  and CMT_MODLS like '%";
								M_strSQLQRY += txtTSTTP.getText().trim() +"%'";
								L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
								if(L_rstRSSET !=null)
								{
									while(L_rstRSSET.next())
									{
										L_intCOUNT ++;
										L_strCODCD = L_rstRSSET.getString("CMT_CODCD");
									}
									L_rstRSSET.close();
									if(L_intCOUNT ==1)
										txtMORTP.setText(L_strCODCD);									
									setMSG("please enter the Material origin value ..",'N');	
								}
							}
						}
						else
						{
						     setMSG("You are not Authorised to do this test ...",'E');							 
							 return false;
						}
						
					}				
				}
				if((input == txtPRDCD) && (txtPRDCD.getText().trim().length() == 10))
				{
					M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST where"
						+ " CT_MATCD = '"+ txtPRDCD.getText().trim() +"'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())						
							L_rstRSSET.close();							
						else
						{
							L_rstRSSET.close();							
							setMSG("Invalid Product Code..",'E');
							return false;
						}
						L_rstRSSET.close();
					}
				}
				if((input ==  txtMORTP)&&(txtMORTP.getText().trim().length()==2))
				{					
                    M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
                    M_strSQLQRY += "'SYS' and CMT_CGSTP = 'QCXXMOR'  and CMT_MODLS like '%";
					M_strSQLQRY += txtTSTTP.getText().trim() +"%'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
							L_rstRSSET.close();														
						else
						{
							L_rstRSSET.close();							
							setMSG("Invalid Material Origin..",'E');
							return false;
						}						
					}
				}	
				if((input ==  txtTSTRF)&&(txtTSTRF.getText().trim().length()==4))
				{
					M_strSQLQRY = "select SUBSTRING(CMT_CODCD,6),CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
					M_strSQLQRY += "'SYS' and CMT_CGSTP = 'QCXXLOT' and CMT_CODCD like '";
					M_strSQLQRY += txtTSTTP.getText().trim()+"%'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
							L_rstRSSET.close();													
						else
						{
							L_rstRSSET.close();							
							setMSG("Invalid Test Refarance ..",'E');
							return false;
						}						
					}
				}
				if((input ==  txtTKLCD)&&(txtTKLCD.getText().trim().length()==3))
				{ 
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where"
					+" CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'QC11TKL'"
					+" AND CMT_CODCD ='"+ txtTKLCD.getText().trim().toUpperCase()+"'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())						
							L_rstRSSET.close();														
						else
						{
							L_rstRSSET.close();							
							setMSG("Invalid Location Code ..",'E');
							return false;
						}						
					}
				}							
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			return true;
		}
	}	
}	