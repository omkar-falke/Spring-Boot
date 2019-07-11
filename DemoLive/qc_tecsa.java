/*
System Name   : Lebortory Information Management System
Program Name  : Data Entry Screen Competitor Sample Analysis
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : September 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
import java.sql.ResultSet;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTable;import java.util.Hashtable;import javax.swing.JComboBox;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.math.BigDecimal;import java.util.StringTokenizer;

import javax.swing.JTabbedPane;import javax.swing.JPanel;import java.awt.Color;

/** <P><PRE >
<b>System Name :</b> Labortoty Information Management System.
 
<b>Program Name :</b> Data Entry Screen Competitor Sample Analysis

###############################################
<b>Purpose :</b> This Entry Screen is used to enter the details of various water Test 
carried Out at different Time for different water Type & Test Type.
			
List of tables used :
Table Name      Primary key                          Operation done
                                               Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
QC_WTTRN      WTT_QCATP,WTT_TSTTP,WTT_TSTNO,      #        #       #       #	
              WTT_TSTDT,WTT_WTRTP
QC_RMMST      RM_QCATP,RM_TSTTP,RM_TSTNO          #        #       #       #
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD                #       #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name    Table name    Type/Size      Description
----------------------------------------------------------------------------------------------------------
txtTSTTP    CMT_CODCD      CO_CDTRN      VARCHAR(15)    Test Type
txtTSTDS    CMT_CODDS      CO_CDTRN      VARCHAR(30)    Test Description
txtTSTNO    CMT_CCSVL      CO_CDTRN      VARCHAR(4)     Test Number
txtTSTDT,   WTT_TSTDT      QC_WTTRN      Timestamp      Test date & time
txtTSTTM    
txtREMDS    RM_REMDS       QC_RMMST      VARCHAR((200)   Remark
txtWTRTP    WTT_WTRTP      QC_WTTRN      VARCHAR((2)     Water type	
txtWTRDS    CMT_CODDS      CO_CDTRN      VARCHAR((30)    Water code desc
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description         Display Columns                  Table Name
----------------------------------------------------------------------------------------------------------
txtTSTTP    Test Type & Description	    CMT_CODCD,CMT_CODDS              CO_CDTRN
                                        (SYS/QCXXTST),NMP01 = 4
txtTSTNO    Test No, Water code,        WTT_TSTNO,WTT_WTRTP,WTT_TSTDT,   QC_WTTRN
            Test Date & Test Details    WTT_TSTTP
txtWTRTP    Water Code & description    CMT_CODCD,CMT_CODDS              CO_CDTRN
                                        (SYS/QCXXWTP)
                                        CMT_MODLS like '%Test type %'
----------------------------------------------------------------------------------------------------------
<I><B>
Logic:</B>
Each water test has some quality parameters. & these parameters change as per 
new standards adopted. 
System manages these parameters of each test & makes Latest parameters available.
According to the new standards, some new parameters are added and some unwanted
are removed for specific Water Test.
Hence to generate report every time list of parameters are generated dynamically as :-
   1) Latest parameters are fetched from the database associated with perticular water Test.
   2) These parameters are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    "WTT_"+ Vector.elementAt(i)+"VL"  
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

<b>Test Parameter Details are taken from CO_QSMST for conditions</b>
   1) QS_TSTLS like '% given Test Type %' ";
   

Test Details such as Numbers,QCA Type,Test Type,Test Report Type are taken from QC_WTTRN 
     1) WTT_TSTDT Between given Date range.
     2) AND WTT_TSTTP = '0401'

Remark is taken from QC_RMMST
     1) RM_QCATP = Given QCA Type
     2) AND RM_TSTTP ='0401' 
     3) AND RM_TSTNO = Test number fetched from QC_WTTRN For Conation
               i)WTT_QCATP = given QCA Type
               ii)AND WTT_TSTTP ='0401' 
               iii)AND DATE(WTT_TSTDT) = Current date.
Serial Number to add the Test Details for Current date (at first time) is generated as
Finacial Years Last integer + given QCA Type Code + "Last Test Number + 1"
i.e. Finacial year 2005, QCA Type Code "01" & Last Test Number 280
Hence the generated Test Number is 50100281.
Last Test Number is taken from CO_CDTRN
     cl_dat.getPRMCOD("CMT_CCSVL","D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",L_strCODCD);									
If Serial Number is already generated then it is Taken from QC_WTTRN for Conditation
     1) WTT_QCATP = Selected QCA Type
     2) AND WTT_TSTTP = Given Test Type.
     3) AND Date(WTT_TSTDT) = Current Date.

Water Type Code & Description are taken from CO_CDTRN for Condiation
     1) CMT_CGMTP = 'SYS' 
     2) AND CMT_CGSTP = 'QCXXWTP'
     3) AND CMT_MODLS like given Test Type Code.					`

Test details are stored & fetched from talbe WTT_WTTRN for given Test Type, Text Number,
Water Type, Date & Time

Validations :
    1) Test Type must be valid.
    2) Test Numnber must be valid.
    3) Data & Time entered must be smaller than the current dat & time.
    4) Water Type Code must be valid.</I>
    
 
    Stages 0: Fresh
           2 : Forwarded for Approval
           4 : Approved for Testing
           5 : Analysis completed
           6 : Forwarded for Approval after Testing
           7 : Approved after testing    
           
    E-mail Initmations - SEF Additon by CSS
                       - Forwarding : intimation to KVM for Approval
                       - Approval stage 1 : Intimation to CSS, QCA for testing
                       - Forwarded after testing : Intimation to KVM for 2nd Approval
                       - APPROVAL STAGE 2 : iNTIMATION TO QCA /CSS 
###########################################
*/

class qc_tecsa extends cl_pbase
{									/** JComboBox to select & display Sample Type.*/
	private JComboBox cmbSMPTP;		/** JTextField to display & enter SEF Number.*/
	private JTextField txtSEFNO;	/** JTextField to enter & display the Location from where the Sample is collected..*/
	private JTextField txtSCLFR;	/** JTextField to enter & display SEF Date.*/
	private JTextField txtSEFDT;	/** JTextField to enter & display SEF By.*/
	private JTextField txtSEFBY;	/** JTextField to enter & display the Name of Manufacture of the Sample.*/
	private JTextField txtMFGBY;	/** JTextField to enter & display the Location from where the the sample is collested.*/
	//private JTextField txtSCLRF;	/** JTextField to enter & display Sample Collection Date.*/
	private JTextField txtSCLDT;	/** JTextField to enter & display The Name of the Person who collected the Sample.*/
	private JTextField txtSCLBY;	/** JTextField to display the Sample Description.*/
	private JTextField txtSMPDS;	/** JTextField to enter & display the Sample Purpose.*/
	private JTextField txtSMPPR;	/** JTextField to enter & display the Product Category.*/
	private JTextField txtPRDCT;	/** JTextField to enter & display the Grade Description.*/
	private JTextField txtGRDDS;	/** JTextField to enter & display the Lot Number.*/
	private JTextField txtLOTNO;	/** JTextField to enter & display the Batch Number.*/
	private JTextField txtBATNO;	/** JTextField to enter & display the Zone Code.*/
	private JTextField txtZONCD;	/** JTextField to enter & display the Sample Quantity Collected.*/
	private JTextField txtSMPQT;	/** JTextField to enter & display the Remark for Sample Details.*/
	private JTextField txtRMRK1;	/** JTextField to Attavhed to the Quality parameter Code to appply validity check.*/
	private JTextField txtQPRCD;	/** JTextField to enter & display the Special Remarks for QCA Only.*/
	private JTextField txtRMRK3;
									/** JLabel to display Messages on the screen.*/
	private JLabel lbl2,lbl3,lbl4;	/** JComboBox to select the authority to forward the record for Authorization.*/
	private JComboBox cmbAPRBY;
									/** JTabbedPane to hold two different tabbes.*/
	private JTabbedPane jtpMAIN;	/** JPanel to hold Sample Details Displaying Components.*/
	private JPanel pnlCSSDL;		/** JPanel to hold Sample Testing Details Displaying Components.*/
	private JPanel pnlQCADL;
									/** JTextField to enter & display the Test Number.*/
	private JTextField txtTSTNO;	/** JTextField to enter & display the Test Date.*/
	private JTextField txtTSTDT;	/** JTextField to enter & display the Test Time.*/
	private JTextField txtTSTTM;	/** JTextField to enter & display the initial of the Person, performed the Testing.*/
	private JTextField txtTSTBY;	/** JComboBox to display & select Product Type.*/
	private JComboBox cmbPRDTP;		/** JTextField to enter & display SPL Eqivalent Grade.*/
	private JTextField txtSPPRD;	/** JTextField to display SPL Equivalent Grade Description.*/	
	private JTextField txtSPPDS;	/** JTextField to enter & display Remark for QCA Testing.*/
	private JTextField txtRMRK2;	/** JTable to enter & display the Testing Details.*/
	private cl_JTable tblITMDL;	
//	private JTextField txtRESLT;
									/** Integer variable to represent the Check Flag Column.*/
	private int TBL_CHKFL = 0;		/** Integer variable to represent the Quality parameter Code Column.*/
	private int TBL_QPRCD = 1;		/** Integer variable to represent the Code Description Column.*/
	private int TBL_CODDS = 2;		/** Integer variable to represent the Unit of Measurement Column.*/
	private int TBL_UOMCD = 3;		/** Integer variable to represent the SPL Equivalent Grade Specifications.*/								
	private int TBL_SPGRD = 4;		/** Integer variable to represent the trsting Result Value.*/
	private int TBL_RESLT = 5;      /** Integer variable to represent the test results which are allowed to be viewed by CSS*/
	private int TBL_ALLOW = 6;
									/** String variable for QCA Type.*/		
	private String strQCATP = "";	/** String variable for Old Sample detail Remark fetched from DataBase.*/
	private String strORMRK1="";	/** String variable for Old QCA Sample Testing details Remark fetched from DataBase.*/
	private String strORMRK2="";	/** String variable for Old QCA Special Remark (visible to QCA only) fetched from DataBase.*/
	private String strORMRK3="";	/** Integer variable for Total Row Count.*/
	private int intROWCT = 50;		/** Integer variable to represent currrent working Row.*/	
	int intSELROW=0;				/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();	
									/** String variable for Working Class Name.*/
	private String strCLSNM = "";	/** String variable for Sample Type.*/
	private String strSMPTP = "";	/** String variable for Test Number.*/
	private String strTSTNO;		/** String variable for Product Type.*/
	private String strPRDTP = "01";	/** String variable for Status Flag.*/
	private String strSTSFL;		/** HashTable to hold Zone Code.*/
	private Hashtable<String,String> hstZONCD;		/** HashTable to hold SPL Specifications for the given Grade.*/
	private Hashtable<String,String> hstSPGRD;
	private Hashtable<String,String> hstSTSFL;
	private String strEMLQC = "va_mulay@spl.co.in";
	private String strTSTLS ="";
	private JComboBox cmbAUTBY;				/** Competitor Sample Special Remark For QCA only.*/
	private final String strSPLRM ="CSSR";
	private Hashtable<String,String> hstQPRDS;
	private Hashtable<String,String> hstUOMCD;
	private String strSEFNO;
	qc_tecsa()
	{
		super(2);
		try
		{
		    strCLSNM = getClass().getName();
			setMatrix(20,6);
			jtpMAIN = new JTabbedPane();
			jtpMAIN.addMouseListener(this);
			
			pnlCSSDL = new JPanel();
			pnlQCADL = new JPanel();
			pnlCSSDL.setLayout(null);
			pnlQCADL.setLayout(null);
            	
			add(new JLabel("Sample Type."),1,1,1,.7,this,'R');
			add(cmbSMPTP = new JComboBox(),1,2,1,1.6,this,'L');
			add(new JLabel("SEF No."),1,4,1,.5,this,'R');
			add(txtSEFNO = new TxtLimit(8),1,5,1,1,this,'L');
			
			add(new JLabel("SEF By / Date"),2,1,1,.7,this,'R');
			add(txtSEFBY = new TxtLimit(3),2,2,1,1,this,'L');
			//add(new JLabel("SEF Date"),2,4,1,.5,this,'R');
			add(txtSEFDT = new TxtDate(),2,3,1,1,this,'L');
			add(lbl3 = new JLabel("Status : "),2,4,1,.5,this,'R');
			add(lbl2 = new JLabel(),2,5,1,2,this,'L');
			lbl2.setForeground(Color.blue);
			lbl3.setForeground(Color.blue);
			
			add(new JLabel("Sample Desc."),2,1,1,.8,pnlCSSDL,'R');
			add(txtSMPDS = new TxtLimit(30),2,2,1,4,pnlCSSDL,'L');
			
			add(new JLabel("Sample Qty."),3,1,1,.8,pnlCSSDL,'R');
			add(txtSMPQT = new TxtNumLimit(6.3),3,2,1,1,pnlCSSDL,'L');
			add(new JLabel("Purpose"),3,3,1,.8,pnlCSSDL,'R');
			add(txtSMPPR = new TxtLimit(100),3,4,1,2.8,pnlCSSDL,'L');
			
			add(new JLabel("Prd. Category"),4,1,1,.8,pnlCSSDL,'R');
			add(txtPRDCT = new TxtLimit(15),4,2,1,1,pnlCSSDL,'L');
			add(new JLabel("Grade"),4,3,1,.8,pnlCSSDL,'R');
			add(txtGRDDS = new TxtLimit(30),4,4,1,2.8,pnlCSSDL,'L');
			
			add(new JLabel("Collected Date"),5,1,1,.8,pnlCSSDL,'R');
			add(txtSCLDT = new TxtDate(),5,2,1,1,pnlCSSDL,'L');
			add(new JLabel("Collected From"),5,3,1,.8,pnlCSSDL,'R');
			add(txtSCLFR = new TxtLimit(50),5,4,1,2.8,pnlCSSDL,'L');
			
			add(new JLabel("Collected by"),6,1,1,.8,pnlCSSDL,'R');
			add(txtSCLBY = new TxtLimit(30),6,2,1,3,pnlCSSDL,'L');
			
			add(new JLabel("MFG By"),7,1,1,.8,pnlCSSDL,'R');
			add(txtMFGBY = new TxtLimit(40),7,2,1,3,pnlCSSDL,'L');
			
			add(new JLabel("Zone Code"),8,1,1,.8,pnlCSSDL,'R');
			add(txtZONCD = new JTextField(),8,2,1,1,pnlCSSDL,'L');
			add(new JLabel("Batch No."),8,3,1,.8,pnlCSSDL,'R');
			add(txtBATNO = new TxtLimit(15),8,4,1,1,pnlCSSDL,'L');
			add(new JLabel("Lot No."),8,5,1,.5,pnlCSSDL,'R');
			add(txtLOTNO = new TxtLimit(20),8,6,1,.8,pnlCSSDL,'L');			

			add(new JLabel("Remarks"),9,1,1,.8,pnlCSSDL,'R');
			add(txtRMRK1 = new TxtLimit(200),9,2,1,4.8,pnlCSSDL,'L');
			
			add(new JLabel("Forwarded To"),10,1,1,.8,pnlCSSDL,'R');
			add(cmbAPRBY = new JComboBox(),10,2,1,1,pnlCSSDL,'L');					
			
			add(new JLabel("SPL Test No."),1,1,1,.7,pnlQCADL,'R');
			add(txtTSTNO = new TxtLimit(8),1,2,1,1,pnlQCADL,'L');						
			add(new JLabel("Test Date Time"),1,3,1,.8,pnlQCADL,'R');
			add(txtTSTDT = new TxtDate(),1,4,1,.6,pnlQCADL,'L');			
			add(txtTSTTM = new TxtTime(),1,4,1,.4,pnlQCADL,'R');			
			add(new JLabel("Tested By"),1,5,1,.6,pnlQCADL,'R');
			add(txtTSTBY  = new TxtLimit(3),1,6,1,.8,pnlQCADL,'L');
			
			add(new JLabel("Product Type"),2,1,1,.7,pnlQCADL,'R');
			add(cmbPRDTP = new JComboBox(),2,2,1,1,pnlQCADL,'L');
			add(new JLabel("SPL Grade"),2,3,1,.8,pnlQCADL,'R');
			add(txtSPPRD = new TxtLimit(15),2,4,1,1,pnlQCADL,'L');			
			add(txtSPPDS = new TxtLimit(30),2,5,1,1.8,pnlQCADL,'L');
									
			add(new JLabel("Remarks"),3,1,1,.7,pnlQCADL,'R');
			add(txtRMRK2 = new TxtLimit(200),3,2,1,4.8,pnlQCADL,'L');
			
			add(new JLabel("Forwarded To"),4,1,1,.8,pnlQCADL,'R');
			add(cmbAUTBY = new JComboBox(),4,2,1,1,pnlQCADL,'L');	
						
		    if(strCLSNM.equals("qc_tecs1"))
			    tblITMDL = crtTBLPNL1(pnlQCADL,new String[]{"Select","Para.Code","Description","UOM","SPL Grade Specification","Result","Allow"},intROWCT,5,2,8.4,4.6,new int[]{30,60,200,75,100,80,30},new int[]{TBL_CHKFL,TBL_ALLOW});	
			else
				tblITMDL = crtTBLPNL1(pnlQCADL,new String[]{"Select","Para.Code","Description","UOM","SPL Grade Specification","Result"},intROWCT,5,2,8.4,4.6,new int[]{30,70,200,75,100,95},new int[]{0});	
			
			add(lbl4 = new JLabel("Sp. Remarks"),14,1,1,.8,pnlQCADL,'R');
			add(txtRMRK3 = new JTextField(),14,2,1,4.8,pnlQCADL,'L');	
			
			jtpMAIN.addTab("Sample Details",pnlCSSDL);
			jtpMAIN.addTab("Testing Details",pnlQCADL);			
			add(jtpMAIN,3,1,16,6,this,'L');
			
			setENBL(false);
			jtpMAIN.setSelectedIndex(0);
			
			hstZONCD = new Hashtable<String,String>();
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP ='SYSMR00ZON'";
			M_strSQLQRY += " AND isnull(CMT_STSFL,'') <> 'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)    			        		
			{
				while(M_rstRSSET.next())				
					hstZONCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));				
				M_rstRSSET.close();
			}
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP ='A"+cl_dat.M_strCMPCD_pbst+"QCXXCSA'";
			M_strSQLQRY += " AND isnull(CMT_STSFL,'') <> 'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			cmbAPRBY.addItem("Select");
			cmbAUTBY.addItem("Select");
			if(M_rstRSSET != null)    			        		
			{
				while(M_rstRSSET.next())
				{
					cmbAPRBY.addItem(M_rstRSSET.getString("CMT_CODCD"));
					cmbAUTBY.addItem(M_rstRSSET.getString("CMT_CODCD"));
				}				
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'SYS'"
				+ " AND CMT_CGSTP ='QCXXTST' AND CMT_CODCD like '%09%'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbSMPTP.addItem(L_strQPRCD +" "+ nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}			
			
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
						cmbPRDTP.addItem(L_strQPRCD+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}	
			hstSTSFL = new Hashtable<String,String>();
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP ='STSQCXXCSA'";
			M_strSQLQRY += " AND isnull(CMT_STSFL,'') <> 'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET != null)    			        		
			{
				String L_strTEMP = "";
				while(M_rstRSSET.next())
				{
					L_strTEMP = M_rstRSSET.getString("CMT_CODCD");
					if(!L_strTEMP.equals(""))
						hstSTSFL.put(L_strTEMP,M_rstRSSET.getString("CMT_CODDS"));
				}				
				M_rstRSSET.close();
			}
			txtSEFNO.setInputVerifier(objINPVR);
			txtSPPRD.setInputVerifier(objINPVR);
			txtRMRK1.setInputVerifier(objINPVR);
			txtRMRK2.setInputVerifier(objINPVR);
			tblITMDL.setCellEditor(TBL_QPRCD, txtQPRCD = new TxtLimit(3));
			txtQPRCD.addKeyListener(this);txtQPRCD.addFocusListener(this);
		//	tblITMDL.setCellEditor(TBL_RESLT, txtRESLT = new TxtNumLimit(10.3));
			//txtRESLT.addKeyListener(this);txtRESLT.addFocusListener(this);			
			
			if(strCLSNM.equals("qc_tecs1"))
			{
				jtpMAIN.setSelectedIndex(1);
				lbl4.setVisible(true);
				txtRMRK3.setVisible(true);
			}
			else
			{
				jtpMAIN.setSelectedIndex(0);
				lbl4.setVisible(false);
				txtRMRK3.setVisible(false);
			}
			
			hstQPRDS = new Hashtable<String,String>();
			hstUOMCD = new Hashtable<String,String>();
			String L_strSQLQRY = "Select QS_QPRCD,QS_QPRDS,QS_UOMCD from CO_QSMST";
			L_strSQLQRY += " where isnull(QS_STSFL,'')<>'X'";			
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(M_rstRSSET!= null)
			{
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");
					if(!L_strTEMP.equals(""))
					{
						hstQPRDS.put(L_strTEMP,nvlSTRVL(M_rstRSSET.getString("QS_QPRDS"),""));
						hstUOMCD.put(L_strTEMP,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));
					}
				}				
			}
			M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");		
		}
	}
	/**
	 * Method super class method overrided to enable & disable components
	 * according to the requriement.
	 * @param P_flgSTAT boolean argument to pass state of the Component.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			return ;
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			txtSEFNO.requestFocus();
		txtSPPDS.setEnabled(false);
		txtSEFBY.setEnabled(false);
		if(strCLSNM.equals("qc_tecs1"))// QCA login
		{
			setCSCOM();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{			
				setQCCOM();
				txtRMRK3.setEnabled(false);
				tblITMDL.cmpEDITR[TBL_CHKFL].setEnabled(false);
    			tblITMDL.cmpEDITR[TBL_QPRCD].setEnabled(false);	
    			tblITMDL.cmpEDITR[TBL_RESLT].setEnabled(false);
			}
			else
    	    {
				txtRMRK3.setEnabled(true);
				tblITMDL.cmpEDITR[TBL_CHKFL].setEnabled(true);
    			tblITMDL.cmpEDITR[TBL_QPRCD].setEnabled(true);
    			tblITMDL.cmpEDITR[TBL_RESLT].setEnabled(true);
    		}
		}
		else if(strCLSNM.equals("qc_tecsa"))// css login
		{
			setQCCOM();/// to disable QCA Component				
			jtpMAIN.setSelectedIndex(0);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
				setCSCOM();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				cmbAPRBY.setEnabled(true);				
				return;
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))			
				return;							
		}
		if(strCLSNM.equals("qc_tecs1"))
			jtpMAIN.setSelectedIndex(1);
		else		
			jtpMAIN.setSelectedIndex(0);
		this.updateUI();
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);			
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
				{
					setMSG("select an option",'N');
					setENBL(false);
				}					
				else
				{						
					if((strCLSNM.equals("qc_tecs1")) && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
					{
						setMSG("Deletion of test details is not Allowed..",'E');
						return;
					}
				    strQCATP = M_strSBSCD.substring(2,4);															
					setMSG("Enter Test Type OR Press F1 to select from list..",'N');
					setENBL(true);					
					txtSEFNO.requestFocus();
					setMSG("Please Enter SEF Number..",'N');					
				}
				clrCOMP();				
				if(strCLSNM.equals("qc_tecs1"))
					jtpMAIN.setSelectedIndex(1);
				else
					jtpMAIN.setSelectedIndex(0);
				txtSEFNO.requestFocus();
				setMSG("Please Enter SEF Number..",'N');
			}
			else if(M_objSOURC == cmbSMPTP)
			{
				strSMPTP = cmbSMPTP.getSelectedItem().toString().trim().substring(0,4);
				txtSEFNO.requestFocus();
				setMSG("Please Enter SEF Number..",'N');
			}
			else if(M_objSOURC == txtZONCD)
			{				
				if(hstZONCD.containsKey(txtZONCD.getText().substring(0,2)))
					txtZONCD.setText(txtZONCD.getText().substring(0,2).trim()+" "+hstZONCD.get(txtZONCD.getText().substring(0,2).trim()));
				else
				{					
					setMSG("Invalid Zone Code, Press F1 to select From List..",'E');
					return;
				}
				txtBATNO.requestFocus();
				setMSG("Please Enter Batch number..",'N');
			}
			if(M_objSOURC == txtSEFNO) //&& (txtSEFNO.getText().trim().length() == 8))
			{
				strSEFNO = txtSEFNO.getText().trim();
				clrCOMP1();
				txtSEFNO.setText(strSEFNO);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{					
					if(strCLSNM.equals("qc_tecsa"))
					{					
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							txtSEFBY.setText(cl_dat.M_strUSRCD_pbst);
						txtSEFDT.requestFocus();
						if(txtSEFDT.getText().trim().length()==0)
							txtSEFDT.setText(cl_dat.M_strLOGDT_pbst);
						setMSG("Enter SEF Date..",'N');						
						return;
					}
					else
					{
						getDATA();						
						txtTSTDT.requestFocus();
						if(txtTSTDT.getText().trim().length() == 0)
							txtTSTDT.setText(cl_dat.M_strLOGDT_pbst);
						setMSG("Enter Test Date ..",'N');						
						return;
					}				
				}				
				getDATA();				
			}
			if(M_objSOURC == txtSPPRD)
			{				
				//txtSPPRD.setText(txtSPPRD.getText().trim().toUpperCase());
				txtRMRK2.requestFocus();				
				setMSG("Enter Remark if any..",'N');					
				String L_strTEMP = "";					
				if(txtSPPRD.getText().trim().length()==10)
				{
					M_strSQLQRY =  "Select PR_PRDCD,PR_PRDDS from CO_PRMST ";
					M_strSQLQRY += " where isnull(PR_STSFL,'') <> 'X' AND PR_PRDCD ='"+txtSPPRD.getText().trim()+"'";						
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
					if(M_rstRSSET !=null)
					{
						if(M_rstRSSET.next())
						{
							txtSPPDS.setText(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));							
							getQPRNG();
						}
						M_rstRSSET.close();
					}						
				}				
			}
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);       
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{                
				if(M_objSOURC == txtSEFNO) 
				{
					if((strCLSNM.equals("qc_tecsa")) && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
						return;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSEFNO";					
					String[] L_arrTBHDR = {"SEF No.","SEF Date","SEF By","Sample Type","Manufacturer"};					
					M_strSQLQRY = "Select SP_SEFNO,SP_SEFDT,SP_SEFBY,SP_SMPTP,SP_MFGBY from QC_SPMST where";
					M_strSQLQRY += " SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD = '"+ M_strSBSCD.trim() +"'";
					M_strSQLQRY += " AND SP_QCATP = '"+ M_strSBSCD.substring(2,4).trim() +"'";
					M_strSQLQRY += " AND isnull(SP_STSFL,'') <> 'X'";	
					if(txtSEFNO.getText().trim().length()>0)
						M_strSQLQRY += " AND SP_SEFNO like'"+ txtSEFNO.getText().trim() +"%'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						M_strSQLQRY += " AND SP_STSFL IN('2','6') ";
						//M_strSQLQRY += " AND SP_FRWTO = 'cl_dat.M_strUSRCD_pbst'";	
					}	
					else
					{
    					if(strCLSNM.equals("qc_tecsa"))
    					{						
    						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
    							M_strSQLQRY += " AND SP_STSFL  < '4' ";
    						M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP +"'";
    					}
    					else
    					{
    						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
    							M_strSQLQRY += " AND SP_STSFL = '4' ";
    						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
    							M_strSQLQRY += " AND SP_STSFL in('5','6') ";
    						//else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
    						//	M_strSQLQRY += " AND SP_STSFL > '3' ";
    					}	
                    }					
					M_strSQLQRY += " order by SP_SEFNO";					
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,5,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtZONCD)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtZONCD";
					String[] L_arrTBHDR = {"Zone Code","Description"};
					M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP ='SYSMR00ZON'";
					M_strSQLQRY += " AND isnull(CMT_STSFL,'') <> 'X'";				
					cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSPPRD)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSPPRD";
					String[] L_arrTBHDR = {"Product Code","Description"};
					M_strSQLQRY =  "Select PR_PRDCD,PR_PRDDS from CO_PRMST ";
					M_strSQLQRY += " where isnull(PR_STSFL,'') <> 'X' order by PR_PRDDS";
					cl_hlp(M_strSQLQRY,2,1,L_arrTBHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtMFGBY)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
   					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMFGBY";
					String[] L_arrTBHDR = {"Manufacture Name"};
					M_strSQLQRY =  "Select distinct SP_MFGBY from QC_SPMST ";
					M_strSQLQRY += " where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(SP_STSFL,'') <> 'X' order by SP_MFGBY";
					cl_hlp(M_strSQLQRY,2,1,L_arrTBHDR,1,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				/*else if (M_objSOURC == tblITMDL.cmpEDITR[TBL_QPRCD])
				{							
					intSELROW = tblITMDL.getSelectedRow();
					if(tblITMDL.getValueAt(intSELROW,TBL_QPRCD).toString().length() == 0)
					{						
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtQPRCD";					
						M_strSQLQRY = "select SUBSTRING(QS_QPRCD,1,3)TS_QPRCD,QS_QPRDS,QS_UOMCD,QS_TSMCD from CO_QSMST "; //,QS_QPRDS,QS_UOMCD,QS_TSMCD,QS_ORDBY 
						if(intSELROW > 1)
						{
							M_strSQLQRY += "where SUBSTRING(QS_QPRCD,1,3) NOT IN(";												
							for(int i=0;i<intSELROW-1;i++) 
							{
								M_strSQLQRY += "'"+tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim()+"',";
							}						
							M_strSQLQRY += "'" +tblITMDL.getValueAt(intSELROW-1,TBL_QPRCD).toString().trim()+"')";
						}
						M_strSQLQRY +=" order by TS_QPRCD";						
						String L_arrQPR[] = {"Code","Description","UOM","Specification"};
						cl_hlp(M_strSQLQRY,1,1,L_arrQPR,4,"CT");
					}
				}*/
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"F1 Help..");                            
			}
		}
		if (L_KE.getKeyCode()== L_KE.VK_ENTER)
        {  
			/*if((M_objSOURC == txtSEFBY) && (txtSEFBY.getText().trim().length() == 3))
			{					
				txtSEFDT.requestFocus();
				setMSG("Please Enter SEF Date..",'N');
				if(txtSEFDT.getText().trim().length()==0)
					txtSEFDT.setText(cl_dat.M_strLOGDT_pbst);
				//txtSEFBY.setText(txtSEFBY.getText().trim().toUpperCase());/// initcap
			}*/
			if((M_objSOURC == txtSEFDT) && (txtSEFDT.getText().trim().length() == 10))
			{				
				if(jtpMAIN.getSelectedIndex() == 0)
				{
					txtSMPDS.requestFocus();					
					setMSG("Enter Sample Description OR press F1 to select from List..",'N');
				}
				else
				{
					txtTSTNO.requestFocus();					
					setMSG("Enter Test Number OR press F1 to select from List..",'N');
				}				
			}												
			if(M_objSOURC == txtSMPDS)
			{
				txtSMPQT.requestFocus();
				//txtSMPDS.setText(txtSMPDS.getText().toUpperCase().trim());
				setMSG("Enter Sample Quantity..",'N');
			}
			if(M_objSOURC == txtSMPQT)// && (txtSMPQT.getText().trim().length() >0))
			{
				txtSMPPR.requestFocus();
				setMSG("Enter the Purpose of the Material..",'N');
			}
			if(M_objSOURC == txtSMPPR) //&& (txtSMPPR.getText().trim().length() >0))
			{
				txtPRDCT.requestFocus();
				//txtSMPPR.setText(txtSMPPR.getText().toUpperCase().trim());
				setMSG("Enter Product Category ..",'N');
			}
			if(M_objSOURC == txtPRDCT)// && (txtPRDCT.getText().trim().length() >0))
			{
				txtGRDDS.requestFocus();
				setMSG("Enter the Grade ..",'N');
			}
			if(M_objSOURC == txtGRDDS) //&& (txtGRDDS.getText().trim().length() >0))
			{
				txtSCLDT.requestFocus();
				if(txtSCLDT.getText().trim().length()==0)
					txtSCLDT.setText(cl_dat.M_strLOGDT_pbst);
				setMSG("Enter the Grade OR Press F1 to select from list..",'N');
			}
			if((M_objSOURC == txtSCLDT) && (txtSCLDT.getText().trim().length() ==10))
			{
				txtSCLFR.requestFocus();
				setMSG("Enter the Grade OR Press F1 to select from list..",'N');
			}
			if(M_objSOURC == txtSCLFR)
			{
				txtSCLBY.requestFocus();
				setMSG("Enter the initial of collector OR Press F1 to select from list..",'N');
			}
			if(M_objSOURC == txtSCLBY)
			{
				//txtSCLBY.setText(txtSCLBY.getText().trim().toUpperCase());
				txtMFGBY.requestFocus();
				setMSG("Enter the Manufacturer Name..",'N');
			}
			if(M_objSOURC == txtMFGBY)
			{
				txtZONCD.requestFocus();
				setMSG("Enter the Zone Code OR Press F1 to select from list..",'N');
			}
			if(M_objSOURC == txtBATNO)
			{
				txtLOTNO.requestFocus();
				setMSG("Enter the Lot Number..",'N');
			}
			if(M_objSOURC == txtLOTNO)
			{
				txtRMRK1.requestFocus();
				setMSG("Enter the Remark if any..",'N');
			}
			if(M_objSOURC == txtRMRK1)
			{
				cmbAPRBY.requestFocus();
				//txtRMRK1.setText(txtRMRK1.getText().trim().toUpperCase());
				setMSG("Select the initial of the Authority for the Authorization..",'N');
			}
			if(M_objSOURC == txtRMRK2)
			{
				cmbAUTBY.requestFocus();
				//txtRMRK1.setText(txtRMRK1.getText().trim().toUpperCase());
				setMSG("Select the initial of the Authority for the Authorization..",'N');
			}
			//if(M_objSOURC == cmbAPRBY)			
			//	cl_dat.M_btnSAVE_pbst.requestFocus();			
			if(M_objSOURC == cmbPRDTP)
			{
				strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
				txtSPPRD.requestFocus();
				setMSG("Enter SPL equivalent grade OR Press F1 to select from List..",'N');
			}
			if(M_objSOURC == txtTSTDT)
			{
				txtTSTTM.requestFocus();
				if(txtTSTTM.getText().trim().length() == 0)
					txtTSTTM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
				setMSG("Enter SPL equivalent grade OR Press F1 to select from List..",'N');
			}
			if(M_objSOURC == txtTSTTM)
			{
				txtTSTBY.requestFocus();
				txtTSTBY.setText(cl_dat.M_strUSRCD_pbst);
				setMSG("Enter initial of the person who performed the test..",'N');								
			}
			if(M_objSOURC == txtTSTBY)
			{
				txtTSTBY.setText(txtTSTBY.getText().trim().toUpperCase());
				txtSPPRD.requestFocus();
				setMSG("Enter SPL equivalent grade OR Press F1 to select from List..",'N');
			}			
			if(M_objSOURC == txtRMRK2)
			{				
				tblITMDL.setRowSelectionInterval(0,0);
				tblITMDL.setColumnSelectionInterval(TBL_QPRCD,TBL_QPRCD);
				tblITMDL.editCellAt(0,TBL_QPRCD);
				tblITMDL.cmpEDITR[TBL_QPRCD].requestFocus();
				setMSG("Enter Quality Parameter OR press F1 to select from List..",'N');
			}
		}
	}
	/**
	 * Super class method to execute the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtSEFNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtSEFNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtSEFDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtSEFBY.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim());			
		}
		else if(M_strHLPFLD.equals("txtZONCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtZONCD.setText(cl_dat.M_strHLPSTR_pbst+" "+String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
		}
		else if(M_strHLPFLD.equals("txtSPPRD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtSPPRD.setText(cl_dat.M_strHLPSTR_pbst);
			txtSPPDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
		}
		else if(M_strHLPFLD.equals("txtMFGBY"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtMFGBY.setText(cl_dat.M_strHLPSTR_pbst);
		}		
		else if (M_strHLPFLD.trim().equals("txtQPRCD"))
	    {        
			if(tblITMDL.isEditing())
				tblITMDL.getCellEditor().stopCellEditing();
			tblITMDL.setRowSelectionInterval(0,0);
			tblITMDL.setColumnSelectionInterval(0,0);
			
	      	cl_dat.M_flgHELPFL_pbst = false;
			String L_strQPRCD = cl_dat.M_strHLPSTR_pbst;			
			
			tblITMDL.setValueAt(new Boolean(true),intSELROW,TBL_CHKFL);
			tblITMDL.setValueAt(L_strQPRCD,intSELROW,TBL_QPRCD);
			
			tblITMDL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim(),intSELROW,TBL_CODDS);
			tblITMDL.setValueAt(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim(),intSELROW,TBL_UOMCD);			
			tblITMDL.editCellAt(intSELROW,TBL_QPRCD);
			tblITMDL.cmpEDITR[TBL_QPRCD].requestFocus();			
		}
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
			String L_strSTSFL = "0";
			int L_intTEMP = 0;
			tblITMDL.setRowSelectionInterval(0,0);
			tblITMDL.setColumnSelectionInterval(0,0);
			tblITMDL.editCellAt(0,0);
			if(strCLSNM.equals("qc_tecsa"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{							
					M_strSQLQRY = "Insert into QC_SPMST(SP_CMPCD,SP_SBSCD,SP_QCATP,SP_SMPTP,SP_SEFNO,SP_SEFDT,SP_SEFBY,"
					+" SP_SCLFR,SP_SCLDT,SP_SCLBY,SP_MFGBY,SP_SMPDS,SP_SMPPR,SP_PRDCT,SP_GRDDS,SP_LOTNO,SP_BATNO,SP_SMPQT,SP_ZONCD,";
					if(cmbAPRBY.getSelectedItem().toString().trim().length()==3)				
						M_strSQLQRY +="SP_FRWTO,";				
						
					M_strSQLQRY +=" SP_STSFL,SP_TRNFL,SP_LUSBY,SP_LUPDT) VALUES("
					+"'"+ cl_dat.M_strCMPCD_pbst +"',"
					+"'"+ M_strSBSCD+"','"+ M_strSBSCD.substring(2,4)+"',"
					+"'"+ strSMPTP +"',"
					+"'"+ txtSEFNO.getText().trim() +"',"
					+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSEFDT.getText().trim())) +"',"
					+"'"+ txtSEFBY.getText().trim() +"',"
					+"'"+ txtSCLFR.getText().trim() +"',"
					+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSCLDT.getText().trim())) +"',"
					+"'"+ txtSCLBY.getText().trim() +"',"
					+"'"+ txtMFGBY.getText().trim() +"',"
					+"'"+ txtSMPDS.getText().trim() +"',"
					+"'"+ txtSMPPR.getText().trim() +"',"
					+"'"+ txtPRDCT.getText().trim() +"',"
					+"'"+ txtGRDDS.getText().trim() +"',"
					+"'"+ txtLOTNO.getText().trim() +"',"
					+"'"+ txtBATNO.getText().trim() +"',"
					+ txtSMPQT.getText().trim() +","
					+"'"+ txtZONCD.getText().trim().substring(0,2) +"',";
					if(cmbAPRBY.getSelectedItem().toString().trim().length()== 3)
					{
						M_strSQLQRY +="'"+ cmbAPRBY.getSelectedItem().toString().trim() +"',";
						L_strSTSFL = "2";
					}					
					M_strSQLQRY += "'"+ L_strSTSFL +"',";
					M_strSQLQRY += "'',";
					M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";					
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");						
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
					if((txtRMRK1.getText().trim().length()>0) && (cl_dat.M_flgLCUPD_pbst == true))
					{
						M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
						M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'" +strQCATP.trim()+"',";
						M_strSQLQRY += "'" +strSMPTP+"',";
						M_strSQLQRY += "'" +txtSEFNO.getText().trim()+"',";
						M_strSQLQRY += "'" +txtRMRK1.getText().trim()+"',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' )";						
	 					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");						
						setMSG("Updating the Remark ..",'N');
						if(cl_dat.M_flgLCUPD_pbst == false)
						{
							setMSG("Error in Saving Data ..",'E');
							return;
						}
					}					
					if(L_strSTSFL.equals("2"))
					{
						M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+ cmbAPRBY.getSelectedItem().toString().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								String L_strEML = M_rstRSSET.getString("US_EMLRF");
								cl_eml ocl_eml = new cl_eml();
								ocl_eml.sendfile(L_strEML,null,"Competitors Sample No."+txtSEFNO.getText().trim()+" is pending for Testing Approval.","Competitors Sample No. "+ txtSEFNO.getText().trim()+" is pending for Testing Approval.");
							}						
							M_rstRSSET.close();
						}
					}
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{					
					L_intTEMP = Integer.parseInt(strSTSFL);
					if(L_intTEMP >3)  // Approval and above stages
					{							
						if(strCLSNM.equals("qc_tecsa"))
						{
							setENBL(true);
							setMSG("This Sample is already Authorised, you cannot modify it..",'E');
							M_rstRSSET.close();
							return ;
						}						
					}					
					M_strSQLQRY = "update QC_SPMST set "
					+"SP_PRDTP = '"+ strPRDTP +"',"
					+"SP_SEFDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSEFDT.getText().trim())) +"',"
					+"SP_SEFBY = '"+ txtSEFBY.getText().trim() +"',"							 
					+"SP_SCLFR = '"+ txtSCLFR.getText().trim() +"',"
					+"SP_SCLDT= '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSCLDT.getText().trim())) +"',"
					+"SP_SCLBY = '"+ txtSCLBY.getText().trim() +"',"
					+"SP_MFGBY = '"+ txtMFGBY.getText().trim() +"',"
					+"SP_SMPDS = '"+ txtSMPDS.getText().trim() +"',"
					+"SP_SMPPR = '"+ txtSMPPR.getText().trim() +"',"
					+"SP_PRDCT = '"+ txtPRDCT.getText().trim() +"',"
					+"SP_GRDDS = '"+ txtGRDDS.getText().trim() +"',"
					+"SP_LOTNO = '"+ txtLOTNO.getText().trim() +"',"
					+"SP_BATNO = '"+ txtBATNO.getText().trim() +"',"
					+"SP_SMPQT = "+ txtSMPQT.getText().trim() +","
					+"SP_ZONCD = '"+ txtZONCD.getText().trim().substring(0,2) +"',";
					
					if(cmbAPRBY.getSelectedItem().toString().trim().length() == 3)
					{
						M_strSQLQRY +="SP_FRWTO ='"+ cmbAPRBY.getSelectedItem().toString().trim() +"',";
						L_strSTSFL = "2";
					}
					/*else
					{
						M_strSQLQRY +="SP_FRWTO = null,";
						L_strSTSFL = "0";
					}*/																																												
					M_strSQLQRY += "SP_STSFL ='"+ L_strSTSFL +"',";
					M_strSQLQRY += "SP_TRNFL = '',";
					M_strSQLQRY += "SP_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "SP_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY += " where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD ='"+ M_strSBSCD +"'";
					M_strSQLQRY += " AND SP_QCATP ='"+ M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP+"'";
					M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim()+"'";													
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Seaving Details..",'E');
						return;		   
					}					
					if(!strORMRK1.equals(txtRMRK1.getText().trim()))
					{
						if(txtRMRK1.getText().trim().equals(""))
						{
							M_strSQLQRY = "Delete from QC_RMMST ";			
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP +"'";
							M_strSQLQRY += " AND RM_TSTTP = '" +strSMPTP+"'";
							M_strSQLQRY += " AND RM_TSTNO = '" +txtSEFNO.getText().trim()+"'";						
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
						else if(strORMRK1.length() == 0) 
						{
							M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
							M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'" +strQCATP.trim()+"',";
							M_strSQLQRY += "'" +strSMPTP+"',";
							M_strSQLQRY += "'" +txtSEFNO.getText().trim()+"',";
							M_strSQLQRY += "'" +txtRMRK1.getText().trim()+"',";
							M_strSQLQRY += "'0',";
							M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";						
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
						else
						{
							M_strSQLQRY = "update QC_RMMST set RM_REMDS ='"+ txtRMRK1.getText().trim() +"'";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP.trim() +"'";
							M_strSQLQRY += " AND RM_TSTTP = '" +strSMPTP+ "'";
							M_strSQLQRY += " AND RM_TSTNO = '" +txtSEFNO.getText().trim()+ "'";						
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
					}					
					if(L_strSTSFL.equals("2"))
					{
						M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+ cmbAPRBY.getSelectedItem().toString().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								String L_strEML = M_rstRSSET.getString("US_EMLRF");
								cl_eml ocl_eml = new cl_eml();
								ocl_eml.sendfile(L_strEML,null,"Competitors Sample No. "+ txtSEFNO.getText().trim()+" is pending for Testing Approval." ,"Competitors Sample No. "+ txtSEFNO.getText().trim()+" is pending for Testing Approval.");
							}						
							M_rstRSSET.close();
						}
					}

				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					L_intTEMP = Integer.parseInt(strSTSFL);
					if(L_intTEMP >2)
					{							
						setENBL(true);
						if(L_intTEMP == 4)
						    setMSG("This Sample is Approved for testing, cannot delete ..",'E');
						else
						    setMSG("This Sample is Held for Discussion, cannot delete ..",'E');
						M_rstRSSET.close();
						return ;
					}										
					M_strSQLQRY = "update QC_SPMST SET ";
					M_strSQLQRY += getUSGDTL("SP",'U',"X");
					M_strSQLQRY += " where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD ='"+ M_strSBSCD +"'";
					M_strSQLQRY += " AND SP_QCATP ='"+ M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP+"'";
					M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim()+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
					if((strORMRK1.length()>0) && (cl_dat.M_flgLCUPD_pbst == true))
					{					
						M_strSQLQRY = "Delete from QC_RMMST ";			
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP +"'";
						M_strSQLQRY += " AND RM_TSTTP = '" +strSMPTP+"'";
						M_strSQLQRY += " AND RM_TSTNO = '" +txtSEFNO.getText().trim()+"'";					
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(cl_dat.M_flgLCUPD_pbst == false)
						{
							setMSG("Error in Saving Data ..",'E');
							return;
						}
					}
				}
			}
			else if(strCLSNM.equals("qc_tecs1"))//QCA login
			{				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					M_strSQLQRY = "INSERT INTO QC_PSMST (PS_CMPCD,PS_SBSCD,PS_QCATP,PS_TSTTP,PS_TSTNO,PS_TSTDT,PS_LOTNO,PS_RCLNO,";
					for(int i=0;i<intROWCT; i++)
					{
						if(tblITMDL.getValueAt(i,TBL_CHKFL).toString().equals("true"))							
							M_strSQLQRY += "PS_"+tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim()+"VL"+",";							
					}
				//	if(cmbAPRBY.getSelectedItem().toString().trim().length()==3)				
				//		M_strSQLQRY +="SP_FRWTO,";				
					M_strSQLQRY += "PS_TSTBY,PS_ADDTM,PS_ADDBY,PS_PRDTP,PS_STSFL,PS_TRNFL,PS_LUSBY,PS_LUPDT)"; 
					M_strSQLQRY += " VALUES(";
					M_strSQLQRY += "'"+ cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+ M_strSBSCD.trim()+"',";
					M_strSQLQRY += "'"+ strQCATP.trim()+"',";
					M_strSQLQRY += "'"+ strSMPTP +"',";
					M_strSQLQRY += "'"+ txtTSTNO.getText().trim()+"','";					
					M_strSQLQRY += M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"',";					
					M_strSQLQRY += "'"+ txtTSTNO.getText().trim()+"',";////Lot number in PSMST is of 8 char wide & in SPMST it is 20 char wide
					M_strSQLQRY += "'00',";	
					strTSTLS ="";
					for(int i=0;i<intROWCT;i++)
					{
						if(tblITMDL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(tblITMDL.getValueAt(i,TBL_RESLT).toString().trim().length() > 0)
								M_strSQLQRY +=tblITMDL.getValueAt(i,TBL_RESLT).toString().trim()+",";		
							else
								M_strSQLQRY +="null,";	
						}
						if(tblITMDL.getValueAt(i,TBL_ALLOW).toString().trim().equals("true"))
						{
						    if(tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim().length() >0)
						        strTSTLS += tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim() +",";
						}

					}	
					M_strSQLQRY += "'"+ txtTSTBY.getText().trim()+"',";					
					M_strSQLQRY += "'"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst +" "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
					M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+ strPRDTP +"',";
					M_strSQLQRY += getUSGDTL("PS",'I',"") + ")";
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}					
					///////if QCA parameters entered, status flag is updated along with tstDate & Test number.
					M_strSQLQRY = "update QC_SPMST set ";
					M_strSQLQRY += "SP_TSTNO = '"+txtTSTNO.getText().trim() +"',";
					M_strSQLQRY += "SP_PRDTP = '"+ strPRDTP +"',";
					M_strSQLQRY += "SP_TSTDT = '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"',";
					M_strSQLQRY += "SP_SPPRD ='"+ txtSPPRD.getText().trim()+"',";
					if(cmbAUTBY.getSelectedItem().toString().trim().length()==3)				
				    	M_strSQLQRY += "SP_STSFL ='6',";
				    else
				    	M_strSQLQRY += "SP_STSFL ='5',";
				    M_strSQLQRY +="SP_TSTLS ='"+strTSTLS +"',";	
					M_strSQLQRY += "SP_TRNFL = '',";
					M_strSQLQRY += "SP_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "SP_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					M_strSQLQRY += "where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD ='"+ M_strSBSCD +"'";
					M_strSQLQRY += " AND SP_QCATP ='"+ M_strSBSCD.substring(2,4)+"'";
					M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP+"'";
					M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim()+"'";				
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
					//// update test number in CO_CDTRN
					M_strSQLQRY ="Update CO_CDTRN SET CMT_CCSVL ='"+txtTSTNO.getText().substring(3)+"'";
					M_strSQLQRY += " WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP ='QCXXTST' AND CMT_CODCD ='"+ strSMPTP + "_" + cl_dat.M_strFNNYR_pbst.substring(3,4)+"'";
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");															
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
					if((txtRMRK2.getText().trim().length()>0) && (cl_dat.M_flgLCUPD_pbst == true))
					{						
						M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
						M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'" +strQCATP.trim()+"',";
						M_strSQLQRY += "'" +strSMPTP+"',";
						M_strSQLQRY += "'" +txtTSTNO.getText().trim()+"',";
						M_strSQLQRY += "'" +txtRMRK2.getText().trim()+"',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' )";						
	 					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");;						
						if(cl_dat.M_flgLCUPD_pbst == false)
						{
							setMSG("Error in Saving Data ..",'E');
							return;
						}
						setMSG("Updating the Remark ..",'N');
					}//to add QCA special Remark
					
					if((txtRMRK3.getText().trim().length()>0) && (cl_dat.M_flgLCUPD_pbst == true))
					{						
						M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
						M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
						M_strSQLQRY += "'" +strQCATP.trim()+"',";
						M_strSQLQRY += "'" +strSPLRM+"',";
						M_strSQLQRY += "'" +txtTSTNO.getText().trim()+"',";
						M_strSQLQRY += "'" +txtRMRK3.getText().trim()+"',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
						M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' )";						
	 					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");						
						if(cl_dat.M_flgLCUPD_pbst == false)
						{
							setMSG("Error in Saving Data ..",'E');
							return;
						}
						setMSG("Updating the Remark ..",'N');
					}
					String L_strTEMP2="";
					if(txtSEFBY.getText().trim().length()==3)
						L_strTEMP2 =",'"+ txtSEFBY.getText().trim()+"'";
				    if(cmbAUTBY.getSelectedItem().toString().trim().length()==3)				
				    {
    				   	M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+cmbAUTBY.getSelectedItem().toString().trim()+"'";
    					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    					if(M_rstRSSET != null)
    					{
    						String L_strEML="";
    						while(M_rstRSSET.next())
    						{
    							L_strEML = M_rstRSSET.getString("US_EMLRF");
    							cl_eml ocl_eml = new cl_eml();
    							ocl_eml.sendfile(L_strEML,null,"Competitors Sample No. "+txtSEFNO.getText().trim()+" is Forwarded for Approval after Testing.","Competitors Sample No. "+txtSEFNO.getText().trim()+" is Forwarded for Approval after Testing.");
    						}						
    						M_rstRSSET.close();
    					}
				    }
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{			
					String L_strTEMP1="";
					L_intTEMP = Integer.parseInt(strSTSFL);
					if(L_intTEMP >6)  // IInd Approval
					{							
						setENBL(true);
						setMSG("This Sample is Authorised ater testing, you cannot modify it..",'E');
						M_rstRSSET.close();
						return ;
					}		
					strTSTLS ="";
					M_strSQLQRY = "Update QC_PSMST set ";					
					for(int i=0;i<intROWCT; i++)
					{
						if(tblITMDL.getValueAt(i,TBL_CHKFL).toString().equals("true"))													
						{
							L_strTEMP1 = tblITMDL.getValueAt(i,TBL_RESLT).toString().trim();
							if(L_strTEMP1.equals(""))
								L_strTEMP1 ="null";
							M_strSQLQRY += "PS_"+tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim()+"VL = "+ L_strTEMP1 +",";
						}
						if(tblITMDL.getValueAt(i,TBL_ALLOW).toString().trim().equals("true"))
						{
						    if(tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim().length() >0)
						        strTSTLS += tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim() +",";
						}
					}					
					M_strSQLQRY += "PS_TSTDT ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"',";
					M_strSQLQRY += "PS_TSTBY ='"+ txtTSTBY.getText().trim()+"',";
					M_strSQLQRY += "PS_PRDTP ='"+ strPRDTP +"',";
					M_strSQLQRY += "PS_STSFL ='U',";
					M_strSQLQRY += "PS_TRNFL ='',";																				
					M_strSQLQRY += "PS_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
					M_strSQLQRY += "PS_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"'";
					M_strSQLQRY += " where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_SBSCD ='"+ M_strSBSCD.trim()+"'";
					M_strSQLQRY += " AND PS_QCATP = '"+ strQCATP.trim()+"'";
					M_strSQLQRY += " AND PS_TSTTP = '"+ strSMPTP +"'";										
					M_strSQLQRY += " AND PS_LOTNO = '"+ txtTSTNO.getText().trim()+"'";
					M_strSQLQRY += " AND PS_RCLNO = '00'";
					M_strSQLQRY += " AND PS_TSTNO = '"+ txtTSTNO.getText().trim()+"'";
					//M_strSQLQRY += " AND PS_TSTDT = '"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTSTDT.getText().trim()+" "+txtTSTTM.getText().trim()))+"'";											
					System.out.println(M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
					if(cmbAUTBY.getSelectedItem().toString().trim().length()==3)				
				    {
    			   		M_strSQLQRY = "update QC_SPMST SET ";
    			   		M_strSQLQRY += " SP_TSTLS ='"+strTSTLS +"',";
    			   		M_strSQLQRY += getUSGDTL("SP",'U',"6");
					    M_strSQLQRY += " where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD ='"+ M_strSBSCD +"'";
					    M_strSQLQRY += " AND SP_QCATP ='"+ M_strSBSCD.substring(2,4)+"'";
					    M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP+"'";
					    M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim()+"'";
			        	cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    					if(cl_dat.M_flgLCUPD_pbst == false)
						{
							setMSG("Error in Saving Data ..",'E');
							return;
						}
    				   	M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+cmbAUTBY.getSelectedItem().toString().trim()+"'";
    					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    					if(M_rstRSSET != null)
    					{
    						String L_strEML="";
    						while(M_rstRSSET.next())
    						{
    							L_strEML = M_rstRSSET.getString("US_EMLRF");
    							cl_eml ocl_eml = new cl_eml();
    							ocl_eml.sendfile(L_strEML,null,"Competitors Sample No. "+txtSEFNO.getText().trim()+" is Forwarded for Approval after Testing.","Competitors Sample No. "+txtSEFNO.getText().trim()+" is Forwarded for Approval after Testing.");
    						}						
    						M_rstRSSET.close();
    					}
				    }
				    else
				    {
				        M_strSQLQRY = "update QC_SPMST SET ";
    			   		M_strSQLQRY += " SP_TSTLS ='"+strTSTLS +"',";
    			   		M_strSQLQRY += getUSGDTL("SP",'U',null);
					    M_strSQLQRY += " where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD ='"+ M_strSBSCD +"'";
					    M_strSQLQRY += " AND SP_QCATP ='"+ M_strSBSCD.substring(2,4)+"'";
					    M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP+"'";
					    M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim()+"'";
                        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(cl_dat.M_flgLCUPD_pbst == false)
						{
							setMSG("Error in Saving Data ..",'E');
							return;
						}
				    }
					if(!strORMRK2.equals(txtRMRK2.getText().trim()))
					{
						if(txtRMRK2.getText().trim().equals(""))
						{
							M_strSQLQRY = "Delete from QC_RMMST ";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP +"'";
							M_strSQLQRY += " AND RM_TSTTP = '"+ strSMPTP +"'";
							M_strSQLQRY += " AND RM_TSTNO = '"+ txtTSTNO.getText().trim() +"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
						else if(strORMRK2.length() == 0) 
						{
							M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
							M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'" +strQCATP.trim()+"',";
							M_strSQLQRY += "'" +strSMPTP+"',";
							M_strSQLQRY += "'" +txtTSTNO.getText().trim()+"',";
							M_strSQLQRY += "'" +txtRMRK2.getText().trim()+"',";
							M_strSQLQRY += "'0',";
							M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
						else
						{
							M_strSQLQRY = "update QC_RMMST set RM_REMDS ='"+ txtRMRK2.getText().trim() +"'";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP.trim() +"'";
							M_strSQLQRY += " AND RM_TSTTP = '" +strSMPTP+ "'";
							M_strSQLQRY += " AND RM_TSTNO = '" +txtTSTNO.getText().trim()+ "'";							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
					}					
					// QCA Only visible remark functionality.
					if(!strORMRK3.equals(txtRMRK3.getText().trim()))
					{
						if(txtRMRK3.getText().trim().equals(""))
						{
							M_strSQLQRY = "Delete from QC_RMMST ";			
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP +"'";
							M_strSQLQRY += " AND RM_TSTTP = '" +strSPLRM+"'";
							M_strSQLQRY += " AND RM_TSTNO = '" +txtTSTNO.getText().trim()+"'";							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
						else if(strORMRK3.length() == 0) 
						{
							M_strSQLQRY = "INSERT INTO QC_RMMST(RM_CMPCD,RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES( " ;
							M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'" +strQCATP.trim()+"',";
							M_strSQLQRY += "'" +strSPLRM+"',";
							M_strSQLQRY += "'" +txtTSTNO.getText().trim()+"',";
							M_strSQLQRY += "'" +txtRMRK3.getText().trim()+"',";
							M_strSQLQRY += "'0',";
							M_strSQLQRY += "'"+ cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
						else
						{
							M_strSQLQRY = "update QC_RMMST set RM_REMDS ='"+ txtRMRK3.getText().trim() +"'";
							M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ strQCATP.trim() +"'";
							M_strSQLQRY += " AND RM_TSTTP = '" +strSPLRM+ "'";
							M_strSQLQRY += " AND RM_TSTNO = '" +txtTSTNO.getText().trim()+ "'";							
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							if(cl_dat.M_flgLCUPD_pbst == false)
							{
								setMSG("Error in Saving Data ..",'E');
								return;
							}
						}
					}					
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					setMSG("Deletion is not allowed..",'N');
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{	
			    String L_strAUTST ="";
				if(strSTSFL.equals("2"))
				  L_strAUTST ="4";			
				else if(strSTSFL.equals("6"))
				  L_strAUTST ="7";			
				     
				 M_strSQLQRY = "update QC_SPMST set ";
				 if(L_strAUTST.equals("4"))
				 {
    			 	M_strSQLQRY += "SP_APRDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"',";
    			 	M_strSQLQRY += "SP_APRBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
				 }
				 else if(L_strAUTST.equals("7"))
				 {
				     M_strSQLQRY += "SP_AUTDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"',";
    			 	M_strSQLQRY += "SP_AUTBY = '"+ cl_dat.M_strUSRCD_pbst+"',";   
				 }
				 M_strSQLQRY += "SP_STSFL ='"+L_strAUTST +"',";
				 M_strSQLQRY += "SP_TRNFL = '',";
				 M_strSQLQRY += "SP_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',";
				 M_strSQLQRY += "SP_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				 M_strSQLQRY += " where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD ='"+ M_strSBSCD +"'";
				 M_strSQLQRY += " AND SP_QCATP ='"+ M_strSBSCD.substring(2,4)+"'";
				 M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP+"'";
				 M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim()+"'";
				 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				 if(cl_dat.M_flgLCUPD_pbst == false)
				{
					setMSG("Error in Saving Data ..",'E');
					return;
				}
				 ///*************************** email for QCA-? & css-SEFBY ****/
				 String L_strTEMP2="";
				 if(txtSEFBY.getText().trim().length()==3)
				 	L_strTEMP2 =",'"+ txtSEFBY.getText().trim()+"'";
					
				 //M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD in ('"+ cmbAPRBY.getSelectedItem().toString().trim()+"'" +L_strTEMP2+ "')";
				 if(L_strAUTST.equals("4"))
				 {
    			 	M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+ txtSEFBY.getText().trim()+ "'";
    			 	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    			 	if(M_rstRSSET != null)
    			 	{
    			 		if(M_rstRSSET.next())
    			 		{
    			 			String L_strEML = M_rstRSSET.getString("US_EMLRF");
    			 	        cl_eml ocl_eml = new cl_eml();		
    			 			ocl_eml.sendfile(L_strEML,null,"Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved for Testing.","Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved for Testing.");
    			 			ocl_eml.sendfile(strEMLQC,null,"Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved for Testing.","Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved for Testing.");
    			 		}						
    			 		M_rstRSSET.close();
    			 	}
				}
				else if(L_strAUTST.equals("7")) // IInd Approval
				{
    				M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+ txtSEFBY.getText().trim()+ "'";
    				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    				if(M_rstRSSET != null)
    				{
    					if(M_rstRSSET.next())
    					{
    						String L_strEML = M_rstRSSET.getString("US_EMLRF");
    				        cl_eml ocl_eml = new cl_eml();		
    						ocl_eml.sendfile(L_strEML,null,"Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved after Testing.","Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved after Testing.");
    						ocl_eml.sendfile(strEMLQC,null,"Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved after Testing.","Competitors Sample No. "+ txtSEFNO.getText().trim()+" is Approved after Testing.");
    					}						
    					M_rstRSSET.close();
    				}
				}
			}			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{				
				clrCOMP();
				lbl2.setText("");
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
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'E');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Error in saving data..",'E');
			}
			if(strCLSNM.equals("qc_tecs1"))
				jtpMAIN.setSelectedIndex(1);
			else
				jtpMAIN.setSelectedIndex(0);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
	}
	boolean vldDATA()
	{
		try
		{
			if(tblITMDL.isEditing())
				tblITMDL.getCellEditor().stopCellEditing();
			tblITMDL.setRowSelectionInterval(0,0);
			tblITMDL.setColumnSelectionInterval(0,0);
			if(txtSEFNO.getText().trim().length()==0)
			{
				txtSEFNO.requestFocus();
				setMSG("Invalid SEF Number, Press F1 to select from list..",'E');
				return false;				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	void getDATA()
	{				
		try
		{	
			String L_strSQLQRY ="";
			int L_intQPRCT = 0;
			strSTSFL = "";
			cmbAPRBY.setSelectedItem("Select");
			String L_strTEMP="";
			strTSTNO = "";
			/// CSS Details
			M_strSQLQRY = "Select * from QC_SPMST where SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD ='"+M_strSBSCD+"'"
			+" AND SP_QCATP ='"+ M_strSBSCD.substring(2,4) +"'"
			+" AND SP_SMPTP ='"+ strSMPTP +"'"
			+" AND SP_SEFNO ='"+ txtSEFNO.getText().trim() +"'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				M_strSQLQRY += " AND SP_STSFL IN('2','6')";	
				System.out.println(M_strSQLQRY);		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{				
				java.sql.Date  L_datTEMP;
				L_strTEMP="";
				if(M_rstRSSET.next())
				{					
					strSTSFL = nvlSTRVL(M_rstRSSET.getString("SP_STSFL"),"");	
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) && (strCLSNM.equals("qc_tecsa")))
        			{								
        				if(Integer.parseInt(strSTSFL)>3)
        				{
        					setCSCOM();
        					setMSG("Modification is not allowed at this stage..",'E');
        					return;
        				}
        				else
        					setENBL(true);
        			}
        			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) && (strCLSNM.equals("qc_tecs1")))
        			{								
        				if(Integer.parseInt(strSTSFL)>5)
        				{
        					//setCSCOM();
        					setMSG("Modification is not allowed at this stage..",'E');
        					return;
        				}
        			}
					txtSEFBY.setText(nvlSTRVL(M_rstRSSET.getString("SP_SEFBY"),""));															
					
					L_datTEMP = (M_rstRSSET.getDate("SP_SEFDT"));
					if(L_datTEMP != null)
						txtSEFDT.setText(M_fmtLCDAT.format(L_datTEMP));
					else
						txtSEFDT.setText("");														 
					txtSCLFR.setText(nvlSTRVL(M_rstRSSET.getString("SP_SCLFR"),""));
								
					L_datTEMP = (M_rstRSSET.getDate("SP_SCLDT"));
					if(L_datTEMP != null)
						txtSCLDT.setText(M_fmtLCDAT.format(L_datTEMP));
					else
						txtSCLDT.setText("");					
					
					txtSCLBY.setText(nvlSTRVL(M_rstRSSET.getString("SP_SCLBY"),""));									
					txtMFGBY.setText(nvlSTRVL(M_rstRSSET.getString("SP_MFGBY"),""));
					txtSMPDS.setText(nvlSTRVL(M_rstRSSET.getString("SP_SMPDS"),""));
					txtSMPPR.setText(nvlSTRVL(M_rstRSSET.getString("SP_SMPPR"),""));
					txtPRDCT.setText(nvlSTRVL(M_rstRSSET.getString("SP_PRDCT"),""));
					txtGRDDS.setText(nvlSTRVL(M_rstRSSET.getString("SP_GRDDS"),""));
					txtLOTNO.setText(nvlSTRVL(M_rstRSSET.getString("SP_LOTNO"),""));
					txtBATNO.setText(nvlSTRVL(M_rstRSSET.getString("SP_BATNO"),""));
					txtSMPQT.setText(nvlSTRVL(M_rstRSSET.getString("SP_SMPQT"),""));	
					strTSTLS = nvlSTRVL(M_rstRSSET.getString("SP_TSTLS"),"");
					strTSTNO = nvlSTRVL(M_rstRSSET.getString("SP_TSTNO"),"");					
					
					if(hstSTSFL.containsKey(strSTSFL))
						lbl2.setText(hstSTSFL.get(strSTSFL.trim()).toString());					
										
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("SP_FRWTO"),"");
					if(L_strTEMP.trim().length()==3)					
						cmbAPRBY.setSelectedItem(L_strTEMP);						

					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("SP_ZONCD"),"");
					if(hstZONCD.containsKey(L_strTEMP.trim()))
						txtZONCD.setText(L_strTEMP+" "+hstZONCD.get(L_strTEMP.trim()).toString());
					///////QCA Details					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("SP_STSFL"),"");					
					if(L_strTEMP.equals("6"))
					{						
						txtTSTNO.setText(strTSTNO);//(nvlSTRVL(M_rstRSSET.getString("SP_TSTNO"),""));						
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("SP_SPPRD"),"");
						txtSPPRD.setText(L_strTEMP);						
						//product code -> Description						
						M_strSQLQRY =  "Select PR_PRDCD,PR_PRDDS from CO_PRMST ";
						M_strSQLQRY += " where isnull(PR_STSFL,'') <> 'X' AND PR_PRDCD ='"+ L_strTEMP +"'order by PR_PRDDS";						
						System.out.println(M_strSQLQRY);
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);		
						if(L_rstRSSET !=null)
						{					
							if(L_rstRSSET.next())
							{
								txtSPPDS.setText(nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),""));
							}
							L_rstRSSET.close();
						}						
					}															
				}
				M_rstRSSET.close();						
			}
			
			if(strCLSNM.equals("qc_tecs1"))/// for QCA to ADD new Data & hnce to generate the Test No.
			{
				String L_strCODCD="";
				L_strTEMP="";
				int L_intTSTNO = 0;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{					
					L_strCODCD = strSMPTP + "_" + cl_dat.M_strFNNYR_pbst.substring(3,4);// + strQCATP.trim();					
					L_strTEMP = cl_dat.getPRMCOD("CMT_CCSVL","D"+cl_dat.M_strCMPCD_pbst,"QCXXTST",L_strCODCD);					
					L_intTSTNO = Integer.valueOf(L_strTEMP).intValue()+1;										
					strTSTNO = cl_dat.M_strFNNYR_pbst.substring(3,4) + strQCATP.trim()
						+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
						+ String.valueOf(L_intTSTNO);														
				}
			}
			txtTSTNO.setText(strTSTNO);
			strORMRK1="";strORMRK2="";strORMRK3="";			
			M_strSQLQRY = "Select RM_REMDS,RM_TSTTP,RM_TSTNO from QC_RMMST where";
			M_strSQLQRY += " RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='" +strQCATP.trim()+"'";
			
			if(strCLSNM.equals("qc_tecs1")) ///QCA
			{
				M_strSQLQRY += " AND RM_TSTTP + RM_TSTNO in('" +strSMPTP + txtSEFNO.getText().trim()+"'";
				if(txtTSTNO.getText().trim().length()>0)
					M_strSQLQRY += ",'"+strSMPTP + txtTSTNO.getText().trim()+"','"+ strSPLRM + txtTSTNO.getText().trim()+"')" ;
				else
					M_strSQLQRY += ")";

			}				
			else if(strCLSNM.equals("qc_tecsa"))////CSS
			{
				M_strSQLQRY += " AND RM_TSTTP + RM_TSTNO in('" +strSMPTP + txtSEFNO.getText().trim()+"','";
				M_strSQLQRY += strSMPTP + txtTSTNO.getText().trim()+"','"+ strSPLRM + txtTSTNO.getText().trim()+"')" ;
			}
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{		
				String L_strTEMP1 ="";
				while(M_rstRSSET.next())
				{
					L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("RM_TSTTP"),"").trim();
					L_strTEMP1 = L_strTEMP1 + nvlSTRVL(M_rstRSSET.getString("RM_TSTNO"),"").trim();
					
					if(L_strTEMP1.equals(strSMPTP + txtSEFNO.getText().trim()))
					{
						strORMRK1 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"").trim();
						txtRMRK1.setText(strORMRK1);
					}
					else if(L_strTEMP1.equals(strSMPTP + txtTSTNO.getText().trim()))
					{
						strORMRK2 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"").trim();
						txtRMRK2.setText(strORMRK2);
					}
					else if(L_strTEMP1.equals(strSPLRM + txtTSTNO.getText().trim()))
					{
						strORMRK3 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"").trim();
						txtRMRK3.setText(strORMRK3);
					}
				}
				M_rstRSSET.close();
			}
			
			if(tblITMDL.isEditing())
				tblITMDL.getCellEditor().stopCellEditing();
			tblITMDL.setRowSelectionInterval(0,0);
			tblITMDL.setColumnSelectionInterval(0,0);
			
			if(strCLSNM.equals("qc_tecsa"))/// for CSS login only QC Parameter having value spacified by QCA has to display
			{
				L_intQPRCT = 0;				
				StringTokenizer L_stkTEMP = new StringTokenizer(strTSTLS,",");
				while(L_stkTEMP.hasMoreTokens())
				{
					L_strTEMP =L_stkTEMP.nextToken();
					tblITMDL.setValueAt(L_strTEMP,L_intQPRCT,TBL_QPRCD);
					if(hstQPRDS.containsKey(L_strTEMP))
						tblITMDL.setValueAt(hstQPRDS.get(L_strTEMP).toString(),L_intQPRCT,TBL_CODDS);	
					if(hstUOMCD.containsKey(L_strTEMP))
						tblITMDL.setValueAt(hstUOMCD.get(L_strTEMP).toString(),L_intQPRCT,TBL_UOMCD);
					L_intQPRCT++;
				}
			}
			else if(strCLSNM.equals("qc_tecs1"))// has to display all Quality parameter with default check flag & sequance
			{
				L_intQPRCT = 0;
				L_strSQLQRY = "Select CMT_CODCD,CMT_CHP01,CMT_CHP02,QS_QPRDS,QS_UOMCD,QS_TSMCD from CO_QSMST,CO_CDTRN";
				L_strSQLQRY += " where isnull(CMT_STSFL,'')<>'X' AND upper(CMT_CODCD) = upper(QS_QPRCD) ";
				L_strSQLQRY += " AND CMT_CGMTP ='SYS' AND CMT_CGSTP = 'QCXXCSA'  order by CMT_CHP02";				
				System.out.println(L_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(M_rstRSSET != null)
				{	
					String L_strTEMP2 = "";
					tblITMDL.clrTABLE();
					while (M_rstRSSET.next())
					{			
						tblITMDL.setValueAt(M_rstRSSET.getString("CMT_CODCD"),L_intQPRCT,TBL_QPRCD);
						tblITMDL.setValueAt(M_rstRSSET.getString("QS_QPRDS"),L_intQPRCT,TBL_CODDS);						
				        tblITMDL.setValueAt(M_rstRSSET.getString("QS_UOMCD"),L_intQPRCT,TBL_UOMCD);
				        L_strTEMP2 = M_rstRSSET.getString("CMT_CHP01");
						if(L_strTEMP2.equals("Y"))
							tblITMDL.setValueAt(Boolean.TRUE,L_intQPRCT,TBL_ALLOW);
				        L_intQPRCT ++;	
					}
					M_rstRSSET.close();
				}
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				L_strSQLQRY  = "Select * from QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '" + M_strSBSCD.substring(2,4) + "'";
				L_strSQLQRY += " AND PS_TSTTP='" + strSMPTP +"'";				
				L_strSQLQRY += " AND PS_TSTNO = '"+ txtTSTNO.getText().trim() +"'";
				L_strSQLQRY += " AND isnull(PS_STSFL,'') <> 'X'";	
				System.out.println(L_strSQLQRY);			
	   			M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
				String L_strQPRCD = "";
				if(M_rstRSSET !=null)
				{
					if (M_rstRSSET.next())
					{				
						java.sql.Timestamp L_tmsTEMP = M_rstRSSET.getTimestamp("PS_TSTDT");
						if(L_tmsTEMP!= null)
						{
    	   					L_strTEMP = M_fmtLCDTM.format(L_tmsTEMP);
    						txtTSTDT.setText(L_strTEMP.substring(0,10));
    						txtTSTTM.setText(L_strTEMP.substring(11,16));
						}
						txtTSTBY.setText(nvlSTRVL(M_rstRSSET.getString("PS_TSTBY"),""));
	   					for(int i=0;i<L_intQPRCT;i++)
						{
						    L_strQPRCD = tblITMDL.getValueAt(i,TBL_QPRCD).toString().trim();
	   	 					tblITMDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("PS_"+L_strQPRCD+"VL"),""),i,TBL_RESLT);	   	 				
	   	 				}				
					}
				}
			}			
			if(txtSPPRD.getText().trim().length() == 10)
				getQPRNG();
			/*if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) && (strCLSNM.equals("qc_tecsa")))
			{								
				if(Integer.parseInt(strSTSFL)>3)
				{
					
					setCSCOM();
					setMSG("This Sample is authorised, you cannot modify it..",'E');
				}
				else
					setENBL(true);
			}*/
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}	
	}
	void setCSCOM()/// for QCA login to disable css components
	{
		txtSEFDT.setEnabled(false);
		txtSEFBY.setEnabled(false);
		txtSMPDS.setEnabled(false);
		txtSMPQT.setEnabled(false);
		txtSMPPR.setEnabled(false);
		txtPRDCT.setEnabled(false);
		txtGRDDS.setEnabled(false);
		txtSCLDT.setEnabled(false);
		txtSCLFR.setEnabled(false);
		txtSCLBY.setEnabled(false);
		txtSCLBY.setEnabled(false);
		txtMFGBY.setEnabled(false);
		txtZONCD.setEnabled(false);
		txtBATNO.setEnabled(false);
		txtLOTNO.setEnabled(false);
		txtRMRK1.setEnabled(false);
		txtTSTNO.setEnabled(false);
		cmbAPRBY.setEnabled(false);
		tblITMDL.cmpEDITR[TBL_CHKFL].setEnabled(false);
		tblITMDL.cmpEDITR[TBL_QPRCD].setEnabled(false);
		tblITMDL.cmpEDITR[TBL_CODDS].setEnabled(false);
		tblITMDL.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblITMDL.cmpEDITR[TBL_SPGRD].setEnabled(false);
		tblITMDL.cmpEDITR[TBL_RESLT].setEnabled(false);
	}
	void setQCCOM()  // for qca login to disable css components
	{
		txtTSTNO.setEnabled(false);
		txtSPPRD.setEnabled(false);
		txtRMRK2.setEnabled(false);
		txtTSTTM.setEnabled(false);
		txtTSTDT.setEnabled(false);
		txtTSTBY.setEnabled(false);
		cmbPRDTP.setEnabled(false);
		tblITMDL.setEnabled(false);
	}
	void getQPRNG()//get quality parameter Range
	{
		try
		{
			String L_strTEMP="",L_strSPDRD = "";
			BigDecimal bgdNPFVL,bgdNPTVL;
			hstSPGRD = new Hashtable<String,String>();
			String L_strSQLQRY = "select QP_QPRCD,QP_NPFVL,QP_NPTVL from CO_QPMST where QP_TSTTP ="+"'0103'";
			L_strSQLQRY +=" AND QP_PRDCD ='"+ txtSPPRD.getText().trim()+"'";
			L_strSQLQRY +=" AND QP_ENDDT is null";			
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);		
			if(L_rstRSSET !=null)
			{					
				while(L_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),"");
					/*deprecated in 1.6			
					bgdNPFVL = L_rstRSSET.getBigDecimal("QP_NPFVL",3);
					bgdNPTVL = L_rstRSSET.getBigDecimal("QP_NPTVL",3);
					*/
					bgdNPFVL = L_rstRSSET.getBigDecimal("QP_NPFVL");
					bgdNPTVL = L_rstRSSET.getBigDecimal("QP_NPTVL");
					if((bgdNPFVL == null)&&(bgdNPTVL == null))
						L_strSPDRD = "-";
					else if(bgdNPFVL.floatValue() == 0.0)
					{
						if(bgdNPTVL.floatValue() == 0.0)
							L_strSPDRD = "-";
						else
						   L_strSPDRD = bgdNPTVL.intValue() + " max";
					}
					else
					{
						if(bgdNPTVL.floatValue()== 0.0)
							L_strSPDRD =bgdNPFVL.intValue()+" min";
						else 
						{
							if(bgdNPFVL.floatValue()== bgdNPTVL.floatValue())
								L_strSPDRD = bgdNPFVL.floatValue()+"";
							else
								L_strSPDRD = bgdNPFVL.floatValue() + " - "+bgdNPTVL.floatValue();
						}
					}
					if(!L_strTEMP.equals(""))
						hstSPGRD.put(L_strTEMP,L_strSPDRD);
				}						
				L_rstRSSET.close();
			}
			for(int i=0;i<intROWCT;i++)
			{
				L_strTEMP = tblITMDL.getValueAt(i,TBL_QPRCD).toString();
				if((L_strTEMP.length()>0) && (hstSPGRD.containsKey(L_strTEMP)))
				{
					tblITMDL.setValueAt(hstSPGRD.get(L_strTEMP),i,TBL_SPGRD);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getQPRNG");
		}
	}
	void clrCOMP1()
	{		
		txtTSTNO.setText("");
		txtTSTDT.setText("");
		txtTSTTM.setText("");
		txtTSTBY.setText("");				
		txtSPPRD.setText("");
		txtSPPDS.setText("");
		txtRMRK2.setText("");		
		tblITMDL.clrTABLE();		
		cmbPRDTP.setSelectedIndex(1);	
		txtSEFNO.setText("");
		txtSCLFR.setText("");
		txtSEFDT.setText("");		
		txtSEFBY.setText("");
		txtMFGBY.setText("");		
		txtSCLDT.setText("");		
		txtSCLBY.setText("");
		txtSMPDS.setText("");
		txtSMPPR.setText("");		
		txtPRDCT.setText("");
		txtGRDDS.setText("");
		txtLOTNO.setText("");
		txtBATNO.setText("");		
		txtZONCD.setText("");
		txtSMPQT.setText("");
		txtRMRK1.setText("");
		txtQPRCD.setText("");
		txtRMRK3.setText("");				
		lbl2.setText("");		
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{	
				/*if((input == txtSEFNO) && (txtSEFNO.getText().trim().length() == 4))
				{
					M_strSQLQRY = "Select SP_SEFNO,SP_SEFDT,SP_SEFBY,SP_SMPTP from QC_SPMST where";
					M_strSQLQRY += " SP_SBSCD = '"+ M_strSBSCD.trim() +"'";
					M_strSQLQRY += " AND SP_QCATP = '"+ M_strSBSCD.substring(2,4).trim() +"'";
					M_strSQLQRY += " AND isnull(SP_STSFL,'') <> 'X'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						M_strSQLQRY += " AND SP_STSFL = '2'";
						//M_strSQLQRY += " AND SP_FRWTO = 'cl_dat.M_strUSRCD_pbst'";	
					}					
					M_strSQLQRY += " AND SP_SMPTP ='"+ strSMPTP +"'";
					M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim() +"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{								
							M_rstRSSET.close();							
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) && (strCLSNM.equals("qc_tecsa")))
							{
								setMSG("SEF No already exists, try another..",'E');
								return false;								
							}							
						}
						else
						{
							M_rstRSSET.close();
							if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) && (strCLSNM.equals("qc_tecsa")))
							{
								setMSG("Invalid SEF number, Press F1 to select from List..",'E');
								return false;				
							}
						}									
					}				
				}				*/
				if((input == txtSPPRD) &&(txtSPPRD.getText().trim().length()==10))
				{					
					M_strSQLQRY =  " Select PR_PRDDS from CO_PRMST ";
					M_strSQLQRY += " where isnull(PR_STSFL,'') <> 'X' AND PR_PRDCD ='"+txtSPPRD.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if (M_rstRSSET != null)
					{  
						if(M_rstRSSET.next())
						{								
							M_rstRSSET.close();														
							return true;								
						}
						else
						{
							M_rstRSSET.close();
							setMSG("Invalid Product Code, Press F1 to select from List..",'E');
							return false;				
						}							
					}
				}
				if((input == txtRMRK1) &&(txtRMRK1.getText().trim().length() >0))
				{
					String strTEMP = txtRMRK1.getText().trim();
					if(strTEMP.length() == 0)
						return true;
					if((strTEMP.indexOf("'") >=0)||(strTEMP.indexOf("\"") >=0))
					{
						setMSG("Special Characters as \' & \" are not allowed ..",'E');
						return false;
					}
				}
				if((input == txtRMRK2) &&(txtRMRK2.getText().trim().length() >0))
				{
					String strTEMP = txtRMRK2.getText().trim();
					if(strTEMP.length() == 0)
						return true;
					if((strTEMP.indexOf("'") >=0)||(strTEMP.indexOf("\"") >=0))
					{
						setMSG("Special Characters as \' & \" are not allowed ..",'E');
						return false;
					}
				
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;	
		}
	}
}			