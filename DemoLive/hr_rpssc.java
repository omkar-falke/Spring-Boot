
/*
	System Name : Human Resource Management System.
	Program Name :  Attendance Schedule
	Source Directory : f:\source\splerp2\hr_rpssc..java                        Executable Directory : f:\exec\splerp2\hr_rpssc.class

	Purpose : This module displays / prints defined Shift Schedule for Specified Department, Period and specified employee category.

List of tables used :
Table Name		Primary key									Operation done
														Insert	Update	   Query    Delete	
 - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
HR_SSTRN		SS_CMPCD,SS_EMPNO,SS_WRKDT, SS_SRLNO						   /
HR_EPMST		EP_EMPNO													   /
CO_CDTRN		CMT_CGMTP,CMT_CGSTP,CMT_CODCD								   /
HR_SWMST		SW_SBSCD,SW_EMPNO,SW_WRKDT									   /
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name	Column Name			Table name			Type/Size	Description
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description		Display Columns			Table Name
- - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - - - - - -  - - - - - - - - -
txtDPTCD	Department Code		cmt_codcd,cmt_codds			CO_CDTRN-SYS/COXXDPT
txtFMDAT	From Date			ss_wrkdt					HR_SSTRN
txtTODAT	To Date				ss_wrkdt					HR_SSTRN
txtEMPTP	Emp.Category		ep_desgn					HR_EPMST
RdbORGSH	Original Shift		ss_orgsh					HR_SSTRN
RdbCURSH	Current Shift		ss_cursh					HR_SSTRN
RdbNEWRP	Running Report		user selection				HR_SSTRN
RdbOLDRP	Old Report			user selection				HR_SWMST
Tb1_empno	Emp.No.				Ss_empno					HR_SSTRN
Tbl_empnm	Emp.Name			ep_fulnm					HR_EPMST
Tbl_shfcd1	Shift Code Day1		ss_shfcd					HR_SSTRN
Tbl_shfcd2	Shift Code Day2		ss_shfcd					HR_SSTRN

(Dynamic Creation, as per date range)

Tbl_totd1s1	No.of Emp. Day1 Sh.1		count(ep_empno)				sh_wrkdt = day1   sh_shfcd = shift1
Tbl_totd1s2	No.of Emp. Day1 Sh.2		count(ep_empno)				sh_wrkdt = day1   sh_shfcd = shift2

Tbl_totsh2	No.of Employees in Sh.2		count(ep_empno)				sh_wrkdt = day1   sh_shfcd = shift2
Tbl_totsh3	No.of Employees in Sh.3		count(ep_empno)				sh_wrkdt = day1   sh_shfcd = shift3
-----------------------------------------------------------------------------------------------------------------------------------------------------
Validations :
 1>LUSBY & LUPDT columns to be updated in addition, modification & deletion mode.
 2>Shift records will be picked up from hr_sstrn as per specified period (ss_wrkdt between FMDAT and TODAT)
 3>If department is not specified details for all departments will be fetched
 4>If Emp. Category is not specified all Emp.Categories will be listed
	
Other  Requirements :
 1>Daywise Shift columns in display table are generated dynamically according to the date range specified by the user.
 2>Off day will be displayed in bold font.
 3>Report will be generated in similar fashion as details are displayed in the table.
 4>rdbORGSH gives original shift schedule of the employees and rdbCURSH gives current running shift schedule of the employees.
 5>rdbOLDRP gives old report of the employees and rdbNEWRP gives running report of the employees. 
		if rdbOLDRP is selected data will be fetched from HR_SWMST.
		if rdbNEWRP is selected data will be fetched from HR_SSTRN.

a. Consecutive working between two weekly offs should not be more than 10 days. 
b. Minimum working between two weekly offs should not be less than 03 days. 
c. Two weekly offs can not be combined. 
d. Scheduled weekly off can be shifted due to exigencies of work within above rules. 
�

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


class hr_rpssc extends cl_rbase 
{										//TextField for Department Code
	private JTextField txtDPTCD;		//TextField for Emp Category
	//private JTextField txtEMPCT;		//TextField for From Date
	private JTextField txtFMDAT;		//TextField for To Date
	private JTextField txtTODAT;		//Label For Department Name
	private JLabel lblDPTNM;			//Radio Button for original shift
	private JRadioButton rdbORGSH;		//Radio Button for current shift
	private JRadioButton rdbCURSH;		
	private JRadioButton rdbWRKSH;		//Radio Button for Old Report
	private JRadioButton rdbOLDRP;		//Radio Button for New Report
	private JRadioButton rdbNEWRP;		//Radio Button for Shift cahnge of current report
	private ButtonGroup btgSHIFT;		//button Group for rdbOLDRP and rdbNEWRP
	private ButtonGroup btgREPRT;		
	private ButtonGroup btgRPTTP;		//Check box for Status Verification
	private ButtonGroup btgREGLR;
	private JRadioButton rdbSTSFL;
	private JRadioButton rdbSFCHG;
	private JRadioButton rdbREGLR,rdbREGLR_DTL,rdbREGLR_SUM;
	private JRadioButton rdbLEGDV;
	private	String PFTXXXDT;
    private Object[] arrHSTKEY;      // Columnwise date (keyvalue) used as a reference while generating report records
    private Hashtable<String,String> hstHDRVAL;     // Hash table for storing datewise column headings
    private Hashtable<String,String> hstDTLVAL_ORG;  // Hash table for storing & printing detail line contents
    private Hashtable<String,String> hstDTLVAL_CUR;  // Hash table for storing & printing detail line contents
    private Hashtable<String,String> hstDTLVAL_WRK;  // Hash table for storing & printing detail line contents
    private Hashtable<String,String> hstDTLVAL_01;  // Hash table for storing & printing detail line contents
    private Hashtable<String,String> hstDTLVAL_02;  // Hash table for storing & printing detail line additional contents (Approval status, Total Employees etc.)
    private Hashtable<String,String> hstDTLVAL_03;  // Hash table for storing & printing detail line additional contents (No. of Employees on off etc.)
	protected SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("yyyyMMdd"); // format for keyvalue of report column
	protected SimpleDateFormat fmtDD = new SimpleDateFormat("dd");             // format for report column headings
	private SimpleDateFormat fmtDBDATTM = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss"); 
	private SimpleDateFormat fmtDBDATTM_YMD = new SimpleDateFormat("yyyy-MM-dd"); 
	private	java.util.Date datSHFDT;

	private	java.util.Date datWRKDT0;		// Previous day
	private	java.util.Date datWRKDT1;		// Current Working day
	private	java.util.Date datWRKDT2;		// Next Day
	private	java.util.Date datWRKDT3;		// Next Day Ending date
	private	java.util.Date datWRKDT;		// Working date to be considered depending on In/Out time
	
	
	private	String strEMPNO="";
	private	String strEMPCT="";
	private String strRPFNM;
	private String strRPLOC;
	private String strAUTHR="";
	private boolean flgAUTHR=false;
	private String strLSTOFDT="";
	//private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpssc.doc";
	//private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
	private DataOutputStream D_OUT ;
	private boolean flgRPTEND=false;
	private java.util.Date L_newDATE;
	private boolean L_flgEOF = false,L_flgOFF=false;
	private boolean flgREGLR_SUM=false;
	cl_JTable tblEPCAT;
	int TB_CHKFL = 0; 
    int TB_ECTDS = 1;
    int TB_ECTCD = 2;
	String strCATDT = "";	
	hr_rpssc()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,8);
        	hstHDRVAL = new Hashtable<String,String>(); 
        	hstDTLVAL_01 = new Hashtable<String,String>(); 
        	hstDTLVAL_02 = new Hashtable<String,String>(); 
        	hstDTLVAL_03 = new Hashtable<String,String>(); 
			hstDTLVAL_ORG = new Hashtable<String,String>();
			hstDTLVAL_CUR = new Hashtable<String,String>();
			hstDTLVAL_WRK = new Hashtable<String,String>();	
			rdbLEGDV = new JRadioButton("Statutary Compliance");
			rdbREGLR = new JRadioButton("Regular");
			rdbREGLR_DTL = new JRadioButton("Detail");
			rdbREGLR_SUM = new JRadioButton("Summary");
			rdbSTSFL = new JRadioButton("Status Verification");
			rdbSFCHG = new JRadioButton("Shift Change");
			add(rdbORGSH=new JRadioButton("Original"),3,3,1,1,this,'L');
			add(rdbCURSH=new JRadioButton("Planned"),3,4,1,1,this,'L');
			add(rdbWRKSH=new JRadioButton("Worked"),3,5,1,1,this,'L');
			
			add(rdbOLDRP=new JRadioButton("Old Report"),4,3,1,1,this,'L');
			add(rdbNEWRP=new JRadioButton("Running Report"),4,4,1,1,this,'L');

			add(new JLabel("Department") ,5,3,1,1,this,'L');
        	add(txtDPTCD=new TxtLimit(3),5,4,1,1, this,'L');
			add(lblDPTNM=new JLabel(),5,5,1,3, this,'L');

        	add(new JLabel("Employee Category"), 6,3,1,1 ,this,'L');
        	//add(txtEMPCT =new TxtLimit(1),6,4,1,0.5 ,this,'L');
			String[] L_strTBLHD1 = {" ","Category","Cat"};
			int[] L_intCOLSZ1 ={10,100,100};
			tblEPCAT = crtTBLPNL1(this,L_strTBLHD1,10,6,4,4,2,L_intCOLSZ1,new int[]{0});

        	add( new JLabel ("Period From"),11,3,1,1,this,'L');
        	add(txtFMDAT=new TxtDate(),11,4,1,1,this,'L');

        	add(new JLabel("To") ,12,3,1,1,this,'L');
        	add(txtTODAT=new TxtDate(),12,4,1,1,this,'L');
			add(rdbREGLR,7,6,1,2,this,'L');
			add(rdbREGLR_SUM,7,8,1,2,this,'L');
			add(rdbREGLR_DTL,8,8,1,2,this,'L');
			add(rdbSTSFL,8,6,1,2,this,'L');
			add(rdbSFCHG,9,6,1,2,this,'L'); 
			add(rdbLEGDV,10,6,1,2,this,'L'); 
			
			btgSHIFT=new ButtonGroup();
			btgREPRT=new ButtonGroup();
			btgRPTTP=new ButtonGroup();
			btgREGLR=new ButtonGroup();
			
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			INPVF oINPVF=new INPVF();
			//txtEMPCT.setInputVerifier(oINPVF);
			txtDPTCD.setInputVerifier(oINPVF);
			txtFMDAT.setInputVerifier(oINPVF);
			txtTODAT.setInputVerifier(oINPVF);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			btgSHIFT.add(rdbCURSH);btgSHIFT.add(rdbORGSH);btgSHIFT.add(rdbWRKSH);
			btgREPRT.add(rdbNEWRP);btgREPRT.add(rdbOLDRP);
			btgRPTTP.add(rdbSTSFL);btgRPTTP.add(rdbSFCHG);btgRPTTP.add(rdbREGLR);btgRPTTP.add(rdbLEGDV);
			btgREGLR.add(rdbREGLR_SUM);btgREGLR.add(rdbREGLR_DTL);
			rdbNEWRP.setSelected(true);
			rdbCURSH.setSelected(true);
			rdbREGLR.setSelected(true);
			rdbREGLR_SUM.setSelected(true);
			M_rdbHTML.setSelected(true);
			rdbORGSH.setVisible(false);
			rdbOLDRP.setVisible(false);
			rdbNEWRP.setVisible(false);
			
			int i=0;
			M_strSQLQRY = " Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
        	M_strSQLQRY+= " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXEPC' order by cmt_codcd";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET!=null && M_rstRSSET.next())
			{
				tblEPCAT.setValueAt(M_rstRSSET.getString("CMT_CODCD"),i,TB_ECTCD);
				tblEPCAT.setValueAt(M_rstRSSET.getString("CMT_CODDS"),i,TB_ECTDS);
				tblEPCAT.setValueAt(new Boolean(true),i,TB_CHKFL);
				i++;
			}
			
			strCATDT="";
			for(i=0;i<tblEPCAT.getRowCount();i++)
			{
				if(tblEPCAT.getValueAt(i,TB_CHKFL).toString().equals("true") && tblEPCAT.getValueAt(i,TB_ECTCD).toString().length()>0)
				{
					if(!strCATDT.equals(""))
						strCATDT += ",";
					strCATDT += "'"+tblEPCAT.getValueAt(i,TB_ECTCD).toString()+"'";
				}
			}
				
			tblEPCAT.addMouseListener(this);
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");	

			if(M_rdbHTML.isSelected() && rdbREGLR.isSelected() && rdbREGLR_SUM.isSelected())
			    flgREGLR_SUM = true;
			else
				flgREGLR_SUM = false;
			
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				String L_strXXDAT = cl_dat.M_txtCLKDT_pbst.getText();
				if(txtFMDAT.getText().length()<8)
					txtFMDAT.setText("01/"+L_strXXDAT.substring(3));
				M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strXXDAT));      
				M_calLOCAL.add(Calendar.MONTH,1);    
				L_strXXDAT = M_fmtLCDAT.format(M_calLOCAL.getTime());
				M_calLOCAL.setTime(M_fmtLCDAT.parse("01/"+L_strXXDAT.substring(3)));      
				M_calLOCAL.add(Calendar.DATE,-1);    
				L_strXXDAT = M_fmtLCDAT.format(M_calLOCAL.getTime());
				if(txtTODAT.getText().length()<8)
					txtTODAT.setText(L_strXXDAT);
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
					M_cmbDESTN.requestFocus(); 
					setENBL(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
					setENBL(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst)){
					setENBL(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst)){
					setENBL(true);
				}
			}
			if(rdbREGLR.isSelected())
			{
				rdbREGLR_DTL.setVisible(true);
				rdbREGLR_SUM.setVisible(true);
			}
			else
			{
				rdbREGLR_DTL.setVisible(false);
				rdbREGLR_SUM.setVisible(false);
			}
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"actionPerformed");
		}
	}

	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			if(L_ME.getSource() == tblEPCAT)
			{
				strCATDT="";
				for(int i=0;i<tblEPCAT.getRowCount();i++)
				{
					if(tblEPCAT.getValueAt(i,TB_CHKFL).toString().equals("true") && tblEPCAT.getValueAt(i,TB_ECTCD).toString().length()>0)
					{
						if(!strCATDT.equals(""))
							strCATDT += ",";
						strCATDT += "'"+tblEPCAT.getValueAt(i,TB_ECTCD).toString()+"'";
					}
				}
				if(strCATDT.length()==0)
					strCATDT="''";
			}
		}
		catch(Exception e)
		{
			System.out.println("inside mousepressed() : "+e);	
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
        		    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
        			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
        								//help for Employee Category
        		/*if(M_objSOURC==txtEMPCT)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPCT";
        			String L_ARRHDR[] = {"Code","Category"};
        			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
        			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXEPC' and trim(cmt_codcd) in (Select distinct ep_empct from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"') order by cmt_codcd";
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}*/
			}	
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
        	{
				if(M_objSOURC==txtDPTCD)
					//txtEMPCT.requestFocus();
				//if(M_objSOURC==txtEMPCT)
					txtFMDAT.requestFocus();
				if(M_objSOURC==txtFMDAT)
					txtTODAT.requestFocus();
				if(M_objSOURC==txtTODAT)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is KeyPressed");
		}	
	
	}
	
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
			/*if(M_strHLPFLD.equals("txtEMPCT"))
			{
			    txtEMPCT.setText(cl_dat.M_strHLPSTR_pbst);
			}*/
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}
	
	// Generating Report
	void genRPTFL()			
	{
		try
		{

			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT);
							//create Array of Heading i.e. start date to end date
			crtHDRARR();	//header generation	
			genRPHDR();		

			//String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' "+(txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' and EP_EMPCT in ("+strCATDT+")"+ (txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_TRN = "_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strJOIN_SS = " on ss_empno=ep_empno and ss_cmpcd=ep_cmpcd and SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  ss_wrkdt is not null and ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strJOIN_SW = " on sw_cmpcd=ep_cmpcd and sw_empno=ep_empno and SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt is not null and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strPREFX = rdbWRKSH.isSelected() ? "SW" : "SS";
			String XX_WRKDT = rdbWRKSH.isSelected() ? "SW_WRKDT" : "SS_WRKDT";
			String XX_STSFL = rdbWRKSH.isSelected() ? "SW_STSFL" : "SS_STSFL";
			String XX_XXXSH = "";
			
			String XX_ORGSH = L_strPREFX+"_ORGSH";
			String XX_CURSH = L_strPREFX+"_CURSH";
			String XX_WRKSH = L_strPREFX.equalsIgnoreCase("SS") ? "' '" : L_strPREFX+"_WRKSH";
			
			String HR_XXTRN = rdbWRKSH.isSelected() ? "HR_SWMST" : "HR_SSTRN";
			String JOIN_XX  = rdbWRKSH.isSelected() ? L_strJOIN_SW : L_strJOIN_SS;


			if(rdbNEWRP.isSelected())
			{
				M_strSQLQRY = "select EP_EMPCT,ep_empno,ep_fstnm,"+XX_WRKDT+" WRKDT,"+XX_STSFL+" STSFL,isnull("+XX_ORGSH+",' ') ORGSH,isnull("+XX_CURSH+",' ') CURSH,isnull("+XX_WRKSH+",' ') WRKSH, ";	
				M_strSQLQRY += "  rtrim(ltrim(isnull(ep_lstnm,' '))) + ' ' + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ";
				M_strSQLQRY += " from HR_EPMST left outer join "+HR_XXTRN+" "+JOIN_XX+" where "+L_strWHRSTR_MST+" order by EP_EMPCT,ep_empno,"+XX_WRKDT+"";
			}
			/*else
			{
				M_strSQLQRY = "select EP_EMPCT,ep_empno,ep_fstnm,sw_wrkdt wrkdt,sw_stsfl stsfl,sw_"+L_strXXXSH+" shfcd, ";
				M_strSQLQRY += " trim(isnull(ep_lstnm,' '))||' '  ||left(isnull(ep_fstnm,' '),1)||'.'||left(isnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
				M_strSQLQRY += " from HR_EPMST left outer join hr_swmst "+L_strJOIN_SW+" where "+L_strWHRSTR_MST+" order by EP_EMPCT,ep_empnm,sw_wrkdt";
			}*/
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
			{	
				//System.out.println("inside");
				return;
			}	
            boolean L_flgEOF = false;
			while(!L_flgEOF)
			{
				if(M_rstRSSET.getDate("wrkdt") == null)
					{if(!M_rstRSSET.next())	{L_flgEOF = true; break;} else continue;}
				strEMPCT=M_rstRSSET.getString("ep_empct");
				D_OUT.writeBytes(padSTRING('R',"",6));
				if(flgREGLR_SUM)
					D_OUT.writeBytes("<tr><td align = 'center'><B>");
				//D_OUT.writeBytes(padSTRING('R',getCODVL("SYSHRXXEPC"+M_rstRSSET.getString("EP_EMPCT"),cl_dat.M_intCODDS_pbst),15));
				D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),10));
				if(flgREGLR_SUM)
					D_OUT.writeBytes("</B></td>");
				if(flgREGLR_SUM)
				{
					D_OUT.writeBytes("<td>");
					D_OUT.writeBytes("&nbsp;");
					D_OUT.writeBytes("</td>");
					for( int i=0; i<arrHSTKEY.length; i++)
					{
						java.util.Date datDATE = fmtYYYYMMDD.parse(arrHSTKEY[i].toString());
						//System.out.println(datDATE.toString());
						if(flgREGLR_SUM)
							D_OUT.writeBytes("<td align = 'center'><B>");
						D_OUT.writeBytes(padSTRING('L',""+datDATE.toString().substring(0,1),3));
						if(flgREGLR_SUM)
							D_OUT.writeBytes("</B></td>");
					}
					D_OUT.writeBytes("</tr>");
				}
				if(!flgREGLR_SUM)
					crtNWLIN();
				while(!L_flgEOF && strEMPCT.equals(M_rstRSSET.getString("ep_empct")))
				{
					if(M_rstRSSET.getDate("wrkdt") == null)
						{if(!M_rstRSSET.next())	{L_flgEOF = true; break;} else continue;}
					strEMPCT=M_rstRSSET.getString("ep_empct");
					strEMPNO=M_rstRSSET.getString("ep_empno");
					if(flgREGLR_SUM)
						D_OUT.writeBytes("<tr><td align = 'center'>");
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNO"),6));
					if(flgREGLR_SUM)
						D_OUT.writeBytes("</td><td width='150'>");
					D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),15));
					if(flgREGLR_SUM)
						D_OUT.writeBytes("</td>");
					hstDTLVAL_ORG.clear();
					hstDTLVAL_CUR.clear();
					hstDTLVAL_WRK.clear();
					hstDTLVAL_02.clear();
					while(!L_flgEOF && strEMPCT.equals(M_rstRSSET.getString("ep_empct")) && strEMPNO.equals(M_rstRSSET.getString("ep_empno")))
					{ 
						//System.out.println(">>org>>"+M_rstRSSET.getString("shfcd")+">>cur>>"+M_rstRSSET.getString("shfch"));
						//if(rdbSFCHG.isSelected() && M_rstRSSET.getString("shfcd").compareTo(M_rstRSSET.getString("shfch"))!=0)
						//{	
						//	hstDTLVAL_01.put(fmtYYYYMMDD.format(M_rstRSSET.getDate("wrkdt")),"*");
						//}	
						//else
						hstDTLVAL_ORG.put(fmtYYYYMMDD.format(M_rstRSSET.getDate("wrkdt")),M_rstRSSET.getString("ORGSH"));
						hstDTLVAL_CUR.put(fmtYYYYMMDD.format(M_rstRSSET.getDate("wrkdt")),M_rstRSSET.getString("CURSH"));
						hstDTLVAL_WRK.put(fmtYYYYMMDD.format(M_rstRSSET.getDate("wrkdt")),M_rstRSSET.getString("WRKSH"));
						hstDTLVAL_02.put(fmtYYYYMMDD.format(M_rstRSSET.getDate("wrkdt")),M_rstRSSET.getString("stsfl"));
						if(!M_rstRSSET.next())
							{L_flgEOF = true; break;}
					}
					String L_strDTLVL = "";
					String L_strDTLVL_ORG = "";
					String L_strDTLVL_CUR = "";
					String L_strDTLVL_WRK = "";
					String L_strHSTSL="";
					//display all the shift codes form start date to end date
					for( int i=0; i<arrHSTKEY.length; i++)
					{
						L_strDTLVL_ORG = hstDTLVAL_ORG.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_ORG.get(arrHSTKEY[i].toString()).toString() : "-"; 
						L_strDTLVL_CUR = hstDTLVAL_CUR.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_CUR.get(arrHSTKEY[i].toString()).toString() : "-"; 
						L_strDTLVL_WRK = hstDTLVAL_WRK.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_WRK.get(arrHSTKEY[i].toString()).toString() : "-"; 
						if(rdbREGLR.isSelected() || rdbLEGDV.isSelected())
						{
							
							if(rdbORGSH.isSelected())
							{	
								L_strDTLVL = L_strDTLVL_ORG;
								L_strHSTSL="ORG";
							}	
							else if(rdbCURSH.isSelected())
							{	
								L_strDTLVL = L_strDTLVL_CUR;
								L_strHSTSL="CUR";
							}	
							else if(rdbWRKSH.isSelected())
							{
								L_strDTLVL = L_strDTLVL_WRK;
								L_strHSTSL="WRK";
							}	
						}
						else if(rdbSFCHG.isSelected())
						{
							if(rdbCURSH.isSelected())
								L_strDTLVL = L_strDTLVL_ORG.equals(L_strDTLVL_CUR) ? L_strDTLVL_ORG : "#";
							else if(rdbWRKSH.isSelected())
								L_strDTLVL = L_strDTLVL_ORG.equals(L_strDTLVL_WRK) ? L_strDTLVL_ORG : "#";
						}
						/*else if(rdbLEGDV.isSelected())
						{
							String L_strSHFDT=arrHSTKEY[i].toString();
							L_strDTLVL = "";
							if(rdbORGSH.isSelected())
							{
								L_strDTLVL +=  chkLEGDV("1",L_strDTLVL_ORG_1, L_strDTLVL_ORG,i);
								L_strDTLVL +=  chkLEGDV("2",L_strDTLVL_ORG_2, L_strDTLVL_ORG,i);
							}
							else if(rdbCURSH.isSelected())
							{
								L_strDTLVL +=  chkLEGDV("1",L_strDTLVL_CUR_1, L_strDTLVL_CUR,i);
								L_strDTLVL +=  chkLEGDV("2",L_strDTLVL_CUR_2, L_strDTLVL_CUR,i);
							}
							else if(rdbWRKSH.isSelected())
							{
								L_strDTLVL +=  chkLEGDV("1",L_strDTLVL_WRK_1, L_strDTLVL_WRK,i);
								L_strDTLVL +=  chkLEGDV("2",L_strDTLVL_WRK_2, L_strDTLVL_WRK,i);
							}
							
						}*/
						if(flgREGLR_SUM)
							D_OUT.writeBytes("<td align = 'center'>");
						D_OUT.writeBytes(padSTRING('L',""+L_strDTLVL,3));
						if(flgREGLR_SUM)
							D_OUT.writeBytes("</td>");
					}
					if(!flgREGLR_SUM)
						crtNWLIN();
					//display status whether it is authorised or unauthorised or processed 
					if(rdbSTSFL.isSelected())
					{
						D_OUT.writeBytes(padSTRING('R',"",6+15));
						for( int i=0; i<arrHSTKEY.length; i++)
						{
							 L_strDTLVL = hstDTLVAL_02.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_02.get(arrHSTKEY[i].toString()).toString() : "";
																						//unauthorised
							 L_strDTLVL = L_strDTLVL.equals("0") ? "." : L_strDTLVL;	//authorised
							 L_strDTLVL = L_strDTLVL.equals("1") ? "-" : L_strDTLVL;	//processed
							 L_strDTLVL = L_strDTLVL.equals("2") ? "*" : L_strDTLVL;	
							 D_OUT.writeBytes(padSTRING('L',""+L_strDTLVL,3));
						}
						if(!flgREGLR_SUM)
							crtNWLIN();
					}
					else if(rdbLEGDV.isSelected())
					{
						L_strDTLVL = "";
						L_strDTLVL_ORG = "";
						L_strDTLVL_CUR = "";
						L_strDTLVL_WRK = "";
						String L_strDTLVL_ORG_1 = "";
						String L_strDTLVL_CUR_1 = "";
						String L_strDTLVL_WRK_1 = "";
						String L_strDTLVL_ORG_2 = "";
						String L_strDTLVL_CUR_2 = "";
						String L_strDTLVL_WRK_2 = "";
						D_OUT.writeBytes(padSTRING('R',"",6+15));
						for( int i=0; i<arrHSTKEY.length; i++)
						{
							//String L_strSHFDT=arrHSTKEY[i].toString();
							if(i>0)
							{
								L_strDTLVL_ORG_1 = hstDTLVAL_ORG.containsKey(arrHSTKEY[i-1].toString()) ? hstDTLVAL_ORG.get(arrHSTKEY[i-1].toString()).toString() : "-"; 
								L_strDTLVL_CUR_1 = hstDTLVAL_CUR.containsKey(arrHSTKEY[i-1].toString()) ? hstDTLVAL_CUR.get(arrHSTKEY[i-1].toString()).toString() : "-"; 
								L_strDTLVL_WRK_1 = hstDTLVAL_WRK.containsKey(arrHSTKEY[i-1].toString()) ? hstDTLVAL_WRK.get(arrHSTKEY[i-1].toString()).toString() : "-"; 
								L_strDTLVL_ORG_2 = hstDTLVAL_ORG.containsKey(arrHSTKEY[i-1].toString()) ? (hstDTLVAL_ORG.get(arrHSTKEY[i-1].toString()).toString().equalsIgnoreCase("O") ? arrHSTKEY[i-1].toString() : L_strDTLVL_ORG_2) : "-"; 
								L_strDTLVL_CUR_2 = hstDTLVAL_ORG.containsKey(arrHSTKEY[i-1].toString()) ? (hstDTLVAL_CUR.get(arrHSTKEY[i-1].toString()).toString().equalsIgnoreCase("O") ? arrHSTKEY[i-1].toString() : L_strDTLVL_CUR_2) : "-"; 
								L_strDTLVL_WRK_2 = hstDTLVAL_ORG.containsKey(arrHSTKEY[i-1].toString()) ? (hstDTLVAL_WRK.get(arrHSTKEY[i-1].toString()).toString().equalsIgnoreCase("O") ? arrHSTKEY[i-1].toString() : L_strDTLVL_WRK_2) : "-"; 
							}
							L_strDTLVL_ORG = hstDTLVAL_ORG.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_ORG.get(arrHSTKEY[i].toString()).toString() : "-"; 
							L_strDTLVL_CUR = hstDTLVAL_CUR.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_CUR.get(arrHSTKEY[i].toString()).toString() : "-"; 
							L_strDTLVL_WRK = hstDTLVAL_WRK.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_WRK.get(arrHSTKEY[i].toString()).toString() : "-"; 
							L_strDTLVL = "";
							if(rdbORGSH.isSelected())
							{
								L_strDTLVL +=  chkLEGDV("1",L_strDTLVL_ORG_1, L_strDTLVL_ORG,i);
								L_strDTLVL +=  chkLEGDV("2",L_strDTLVL_ORG_2, L_strDTLVL_ORG,i);
							}
							else if(rdbCURSH.isSelected())
							{
								L_strDTLVL +=  chkLEGDV("1",L_strDTLVL_CUR_1, L_strDTLVL_CUR,i);
								L_strDTLVL +=  chkLEGDV("2",L_strDTLVL_CUR_2, L_strDTLVL_CUR,i);
							}
							else if(rdbWRKSH.isSelected())
							{
								L_strDTLVL +=  chkLEGDV("1",L_strDTLVL_WRK_1, L_strDTLVL_WRK,i);
								L_strDTLVL +=  chkLEGDV("2",L_strDTLVL_WRK_2, L_strDTLVL_WRK,i);
							}
							D_OUT.writeBytes(padSTRING('L',""+L_strDTLVL,3));
						}
						if(!flgREGLR_SUM)
							crtNWLIN();
					}
				}
				if(!flgREGLR_SUM)
					crtNWLIN();
			}
			if(flgREGLR_SUM)
				D_OUT.writeBytes("</tr></table>");
				//M_rstRSSET.close();
			else
			{
				crtNWLIN();
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();			//method to print summary 
			}
			if(rdbSFCHG.isSelected())
				prnSFCHGSUM();
			else if((rdbREGLR.isSelected() || rdbSTSFL.isSelected()) && !flgREGLR_SUM)
				prnREGLRSUM();
			else if(rdbLEGDV.isSelected())
				prnLEGDVSUM();
			L_flgOFF=false;
			flgRPTEND=true;
			flgAUTHR=false;
			strAUTHR="";
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			if(M_rstRSSET==null)
			{
				M_rstRSSET.close();
			}	
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/** Verification mehod for legal deviation
	 */
	private String chkLEGDV(String LP_LEGCD,String LP_PRVVL, String LP_SHFCD,int LP_DAYVL)
	{
		//System.out.println(strEMPNO+"   chkLEGDV :   "+LP_LEGCD+"  "+LP_PRVVL+"    "+LP_SHFCD+"   "+LP_DAYVL);
		String L_strRETVL = "";
		long L_intDFFVL = 0;
		try
		{
			//if(LP_PRVVL.equals(""))
			//	return "-";
			if(LP_LEGCD.equals("1"))
			{
				if(!LP_SHFCD.equalsIgnoreCase("O"))
				{
					L_intDFFVL = getDTDIFF(getSHFTM_ST(LP_SHFCD,"IN",LP_DAYVL),getSHFTM_ST(LP_PRVVL,"OUT",LP_DAYVL-1),"HH");
					if(L_intDFFVL > 0 && L_intDFFVL <12)
					   L_strRETVL = "1";
				}
			}	
			else if(LP_LEGCD.equals("2"))
			{
				if(LP_SHFCD.equalsIgnoreCase("O"))
				{
					L_intDFFVL = getDTDIFF(fmtDBDATTM.format(fmtYYYYMMDD.parse(arrHSTKEY[LP_DAYVL].toString())),	fmtDBDATTM.format(fmtYYYYMMDD.parse(LP_PRVVL)),"DD");
					if((L_intDFFVL > 0 && L_intDFFVL < 3) || L_intDFFVL > 10)
					   L_strRETVL = "2";
				}
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkLEGDV");
		}
		return L_strRETVL;
	}

	
	/*
	private String isOFFLEGAL(String LP_SHFCD1,String L_SHFDT1)
	{
		System.out.println("inside");
		String L_strRETVL=LP_SHFCD1;
		String L_strSHFDT1="";
		String L_strSHFDT2="";
		
		if(strLSTOFDT.equals(""))
			strLSTOFDT=L_SHFDT1;
		else
		{	
			System.out.println(strLSTOFDT+">>>>>>>"+strLSTOFDT.substring(0,4)+"/"+strLSTOFDT.substring(4,6)+"/"+strLSTOFDT.substring(6,8)+".00"+".00");
			L_strSHFDT1=strLSTOFDT.substring(0,4)+"/"+strLSTOFDT.substring(4,6)+"/"+strLSTOFDT.substring(6,8)+".00"+".00"+".00";
			L_strSHFDT2=L_SHFDT1.substring(0,4)+"/"+L_SHFDT1.substring(4,6)+"/"+L_SHFDT1.substring(6,8)+".00"+".00"+".00";
			if(getDTDIFF(L_strSHFDT1,L_strSHFDT2,"DD")>=10)
				L_strRETVL="#";	
		}		
		strLSTOFDT=L_SHFDT1;
		
		return L_strRETVL;
	}	
	
	
	private String isSFTLEGAL(String LP_SHFCD1,String L_SHFDT1,int i,String L_strHSTFL)
	{
		try
		{
			java.util.Date L_datSHFDT1,L_datSHFDT2;
			String LP_SHFCD2="";
			String L_SHFDT2="";
			String L_strRETVL=LP_SHFCD1;
			String L_strIN="",L_strOUT="";
			if(L_strHSTFL.equals("ORG"))
				LP_SHFCD2 = hstDTLVAL_ORG.containsKey(arrHSTKEY[i-1].toString()) ? hstDTLVAL_ORG.get(arrHSTKEY[i-1].toString()).toString() : "-"; 
			if(L_strHSTFL.equals("CUR"))
				LP_SHFCD2 = hstDTLVAL_CUR.containsKey(arrHSTKEY[i-1].toString()) ? hstDTLVAL_CUR.get(arrHSTKEY[i-1].toString()).toString() : "-"; 
			if(L_strHSTFL.equals("WRK"))
				LP_SHFCD2 = hstDTLVAL_WRK.containsKey(arrHSTKEY[i-1].toString()) ? hstDTLVAL_WRK.get(arrHSTKEY[i-1].toString()).toString() : "-"; 
			L_SHFDT2=arrHSTKEY[i-1].toString();
			
			//for the comparison of previous shift to next shift 

			L_datSHFDT1=M_fmtLCDAT.parse(L_SHFDT1.substring(6,8)+"/"+L_SHFDT1.substring(4,6)+"/"+L_SHFDT1.substring(0,4));
			L_datSHFDT2=M_fmtLCDAT.parse(L_SHFDT2.substring(6,8)+"/"+L_SHFDT2.substring(4,6)+"/"+L_SHFDT2.substring(0,4));
			
			L_strIN = getSHFTM_ST(LP_SHFCD1,"IN",L_datSHFDT1,i);
			L_strOUT = getSHFTM_ST(LP_SHFCD2,"OUT",L_datSHFDT2,i);
			System.out.println(">>"+L_strIN);
			if(!(LP_SHFCD2.equals("O") || LP_SHFCD1.equals("O")))
			{	
				if(getDTDIFF(L_strIN,L_strOUT,"HH")<12)
					L_strRETVL="*";	
			}
				
			return L_strRETVL;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return "";
	}		
	*/
	private long getDTDIFF(String LP_strDAT1,String LP_strDAT2,String LP_RTNTP)// date format YYYY-mm-dd.hh.mm.ss 
	{
		long L_intRETVL=0;
		if(LP_strDAT1.length()<16 || LP_strDAT2.length()<16)
			return L_intRETVL;
		try
		{
			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			long milliseconds1;
			long milliseconds2;
			long diff;
			long diffSeconds;
			long diffMinutes;
			long diffHours;				//diff betn todays in time and yesterdays out time 		
			long diffDays;
			calendar1.set(Integer.parseInt(LP_strDAT1.substring(0,4)),Integer.parseInt(LP_strDAT1.substring(5,7)),Integer.parseInt(LP_strDAT1.substring(8,10)),Integer.parseInt(LP_strDAT1.substring(11,13)),Integer.parseInt(LP_strDAT1.substring(14,16)),Integer.parseInt(LP_strDAT1.substring(17,19)));
			calendar2.set(Integer.parseInt(LP_strDAT2.substring(0,4)),Integer.parseInt(LP_strDAT2.substring(5,7)),Integer.parseInt(LP_strDAT2.substring(8,10)),Integer.parseInt(LP_strDAT2.substring(11,13)),Integer.parseInt(LP_strDAT2.substring(14,16)),Integer.parseInt(LP_strDAT2.substring(17,19)));
			milliseconds1 = calendar1.getTimeInMillis();
			milliseconds2 = calendar2.getTimeInMillis();
			diff = milliseconds1 - milliseconds2;
			if(LP_RTNTP.equals("SS"))
				L_intRETVL= diff / 1000;
			else if(LP_RTNTP.equals("MM"))
				L_intRETVL= diff / (60 * 60 * 1000);
			else if(LP_RTNTP.equals("HH"))
				L_intRETVL= diff / (60 * 60 * 1000);
			else if(LP_RTNTP.equals("DD"))
				L_intRETVL= diff / (24 * 60 * 60 * 1000);

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDTDIFF");
		}
		//System.out.println("getDTDIFF : "+LP_strDAT1+"   "+LP_strDAT2+"   "+LP_RTNTP+"   L_intRETVL : "+L_intRETVL);
		return L_intRETVL;
	}
	
/*
	private String getSHFTM_ST(String LP_SHFCD,String LP_SHFFL,java.util.Date datWRKDT)
	{
		String L_strRETVL = "";
		java.util.Date datWRKDT1;
		java.util.Date datWRKDT2;
		SimpleDateFormat fmtDBDATTM_YMD = new SimpleDateFormat("yyyy-MM-dd"); 
		try
		{
			datWRKDT1 = datWRKDT;
			M_calLOCAL.setTime(datWRKDT);      
			M_calLOCAL.add(Calendar.DATE,1);    
			datWRKDT1 = M_calLOCAL.getTime();
			
			if(!cl_dat.M_hstMKTCD_pbst.containsKey("MSTCOXXSHF"+LP_SHFCD))
				return L_strRETVL;
			if(LP_SHFFL.equalsIgnoreCase("IN"))
			{
				datWRKDT = Integer.parseInt(getCODVL("MSTCOXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)) >= 7 ? datWRKDT : datWRKDT1;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("MSTCOXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)+"."+getCODVL("MSTCOXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(3,5)+".00";
			}
				
			if(LP_SHFFL.equalsIgnoreCase("OUT"))
			{
				datWRKDT = Integer.parseInt(getCODVL("MSTCOXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)) <= 7 ? datWRKDT1 : datWRKDT;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("MSTCOXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)+"."+getCODVL("MSTCOXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(3,5)+".00";
			}
	
		}
		catch(Exception L_EX){
			setMSG(L_EX,"getSHFTM_ST");
		}
		return L_strRETVL;
	}	
*/	
	private String subTIME(String P_strSTRTM,String P_strNEWTM)
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
		
//this method creates hashtable for report heading
	private void crtHDRARR()	
	{
		try
		{
            hstHDRVAL.clear() ;
            java.util.Date L_datRUNDT = M_fmtLCDAT.parse(txtFMDAT.getText());
			while(true)
			{
			  	hstHDRVAL.put(fmtYYYYMMDD.format(L_datRUNDT),fmtDD.format(L_datRUNDT));
			  	if(L_datRUNDT.compareTo(M_fmtLCDAT.parse(txtTODAT.getText()))==0)
			  		break;
			  	M_calLOCAL.setTime(L_datRUNDT);      
			  	M_calLOCAL.add(Calendar.DATE,+1);    
			  	L_datRUNDT = M_calLOCAL.getTime();
			}
			setHST_ARR(hstHDRVAL);
				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtHDRARR");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
		
		
	//this method gives report summary
	private void prnREGLRSUM()	
	{
		try
		{
			String strSHFCD="";	
			String L_strDTLVL = "";
			int i;
			
			//String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' "+(txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' and EP_EMPCT in ("+strCATDT+")" +(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_TRN = "_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strJOIN_SS = " on ss_empno=ep_empno  and ss_cmpcd=ep_cmpcd and SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ss_wrkdt is not null and ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strJOIN_SW = " on sw_empno=ep_empno  and sw_cmpcd=ep_cmpcd and SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sw_wrkdt is not null  and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strPREFX = rdbWRKSH.isSelected() ? "SW" : "SS";
			String XX_WRKDT = rdbWRKSH.isSelected() ? "SW_WRKDT" : "SS_WRKDT";
			String XX_STSFL = rdbWRKSH.isSelected() ? "SW_STSFL" : "SS_STSFL";
			String XX_XXXSH = "";
			XX_XXXSH = rdbORGSH.isSelected() ? L_strPREFX+"_ORGSH" : XX_XXXSH;
			XX_XXXSH = rdbCURSH.isSelected() ? L_strPREFX+"_CURSH" : XX_XXXSH;
			XX_XXXSH = rdbWRKSH.isSelected() ? L_strPREFX+"_WRKSH" : XX_XXXSH;
			String HR_XXTRN = rdbWRKSH.isSelected() ? "HR_SWMST" : "HR_SSTRN";
			String JOIN_XX  = rdbWRKSH.isSelected() ? L_strJOIN_SW : L_strJOIN_SS;

			if(rdbNEWRP.isSelected())
			{
				M_strSQLQRY = "select "+XX_WRKDT+" WRKDT,isnull("+XX_XXXSH+",' ') shfcd,count(*) recct ";
				M_strSQLQRY += " from HR_EPMST left outer join "+HR_XXTRN+" "+JOIN_XX+" where "+L_strWHRSTR_MST+" group by "+XX_XXXSH+","+XX_WRKDT+" order by "+XX_XXXSH+","+XX_WRKDT+"";
			}

			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
   				return;
			D_OUT.writeBytes(padSTRING('R',"Summary",6+15));
	        for(i=0; i<arrHSTKEY.length; i++)
			{
				D_OUT.writeBytes(padSTRING('L',""+hstHDRVAL.get(arrHSTKEY[i].toString()).toString(),3));
			}
			crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			
            L_flgEOF = false;
			hstDTLVAL_02.clear();
			hstDTLVAL_03.clear();
			//display count of vorious shifts in the summary
			while(!L_flgEOF)
			{
				if(M_rstRSSET.getDate("wrkdt") == null)
					{if(!M_rstRSSET.next())	{L_flgEOF = true; break;} else continue;}
				
				strSHFCD=M_rstRSSET.getString("shfcd");
				hstDTLVAL_01.clear();
				while(!L_flgEOF && strSHFCD.equals(M_rstRSSET.getString("shfcd")))
				{
					if(M_rstRSSET.getDate("wrkdt") == null)
					{if(!M_rstRSSET.next())	{L_flgEOF = true; break;} else continue;}
					
					if(M_rstRSSET.getString("shfcd").equalsIgnoreCase("O"))
						hstDTLVAL_03.put(fmtYYYYMMDD.format(M_rstRSSET.getDate("wrkdt")),M_rstRSSET.getString("recct"));
					else
						hstDTLVAL_01.put(fmtYYYYMMDD.format(M_rstRSSET.getDate("wrkdt")),M_rstRSSET.getString("recct"));
					if(!M_rstRSSET.next())
							{L_flgEOF = true; break;}
				}
				if(strSHFCD.equalsIgnoreCase("O"))
					continue;
				L_strDTLVL = "";
				D_OUT.writeBytes(padSTRING('R'," ",11)+padSTRING('R',"Shift "+strSHFCD,10));
				for(i=0; i<arrHSTKEY.length; i++)
				{
					L_strDTLVL = hstDTLVAL_01.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_01.get(arrHSTKEY[i].toString()).toString() : "-";
					D_OUT.writeBytes(padSTRING('L',""+L_strDTLVL,3));
					if(!L_strDTLVL.equals("-"))
					{
						String L_strDTLVL_STS = hstDTLVAL_02.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_02.get(arrHSTKEY[i].toString()).toString() : "0";
						hstDTLVAL_02.put(arrHSTKEY[i].toString(),String.valueOf(Integer.parseInt(L_strDTLVL)+Integer.parseInt(L_strDTLVL_STS)));
					}
				}
				crtNWLIN();
			}
			//display count of total in the summary
			D_OUT.writeBytes(padSTRING('R'," ",11)+padSTRING('R',"Total ",10));
			for(i=0; i<arrHSTKEY.length; i++)
			{
				L_strDTLVL = hstDTLVAL_02.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_02.get(arrHSTKEY[i].toString()).toString() : "-";
				D_OUT.writeBytes(padSTRING('L',""+L_strDTLVL,3));
			}
			crtNWLIN();
			//dispaly count off's in the summary
			D_OUT.writeBytes(padSTRING('R'," ",11)+padSTRING('R',"Offs ",10));
			for(i=0; i<arrHSTKEY.length; i++)
			{
				L_strDTLVL = hstDTLVAL_03.containsKey(arrHSTKEY[i].toString()) ? hstDTLVAL_03.get(arrHSTKEY[i].toString()).toString() : "-";
				D_OUT.writeBytes(padSTRING('L',""+L_strDTLVL,3));
			}
			crtNWLIN();
			M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnREGLRSUM");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}	
	

	//this method pronts summary part for Legal Deviation
	private void prnLEGDVSUM()	
	{
		try
		{
			crtNWLIN();
			D_OUT.writeBytes(" Deviation Status   "+padSTRING('R',"Code 1 indicates : No.of hours gap between two successive Shifts is not maintained",130));
			crtNWLIN();
			D_OUT.writeBytes("                    "+padSTRING('R',"Code 2 indicates : No.of days gap between two successive Offs is not maintained",130));
			crtNWLIN();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnLEGDVSUM");
		}
	}	
	
	
	
	private void prnSFCHGSUM()	
	{
		try
		{
			//String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' "+(txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' and EP_EMPCT in ("+strCATDT+")" +(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
			String L_strWHRSTR_TRN = "_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strJOIN_SS = " on ss_empno=ep_empno and ss_cmpcd=ep_cmpcd and ss_wrkdt is not null and ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strJOIN_SW = " on sw_empno=ep_empno and sw_cmpcd=ep_cmpcd and ss_wrkdt is not null  and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
			String L_strXXXSC = (rdbORGSH.isSelected() ? "ORGSH" : (rdbCURSH.isSelected() ? "CURSH" : "WRKSH")) ;
			if(rdbNEWRP.isSelected())
			{
				
				//M_strSQLQRY = "select distinct EP_EMPCT,ep_empno,ep_fstnm,ss_wrkdt wrkdt,ss_stsfl stsfl,ss_orgsh , ss_cursh,sw_wrksh,";
				//M_strSQLQRY += "  trim(isnull(ep_lstnm,' '))||' '  ||left(isnull(ep_fstnm,' '),1)||'.'||left(isnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
				//M_strSQLQRY += " from HR_EPMST,HR_SSTRN,HR_SWMST ";
				//M_strSQLQRY += " where ss_empno=ep_empno  and ss_empno=sw_empno and  sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"' and ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"' and "+L_strWHRSTR_MST+"";
				//M_strSQLQRY += " order by EP_EMPCT,ep_empnm,ss_wrkdt";
								
				M_strSQLQRY = "select EP_EMPCT,ep_empno,ep_fstnm,ss_wrkdt wrkdt,ss_stsfl stsfl,ss_orgsh shfcd,";
				M_strSQLQRY += "ss_"+L_strXXXSC+" shfch,";
				M_strSQLQRY += "  rtrim(ltrim(isnull(ep_lstnm,' ')))+' ' + left(isnull(ep_fstnm,' '),1) +'.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ";
 				M_strSQLQRY += " from HR_EPMST left outer join hr_sstrn "+L_strJOIN_SS+" where "+L_strWHRSTR_MST+" order by EP_EMPCT,ep_empnm,ss_wrkdt";
			}
			/*else
			{
				M_strSQLQRY = "select EP_EMPCT,ep_empno,ep_fstnm,sw_wrkdt wrkdt,sw_stsfl stsfl,sw_"+L_strXXXSH+" shfcd, ";
				M_strSQLQRY += " trim(isnull(ep_lstnm,' '))||' '  ||left(isnull(ep_fstnm,' '),1)||'.'||left(isnull(ep_mdlnm,' '),1)||'.'  EP_EMPNM ";
				M_strSQLQRY += " from HR_EPMST left outer join hr_swmst "+L_strJOIN_SW+" where "+L_strWHRSTR_MST+" order by EP_EMPCT,ep_empnm,sw_wrkdt";
			}*/
			//System.out.println(">>>>>>>>>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next() || M_rstRSSET==null)
				return;
            boolean L_flgEOF = false;

			D_OUT.writeBytes(padSTRING('R',"Summary",6+15));
			crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
			D_OUT.writeBytes("      Emp No    Name                Date       Org Shift    Current Shift    Worked Shift                         ");
			crtNWLIN();
			D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------");
			crtNWLIN();
	
			while(!L_flgEOF)
			{
				if(M_rstRSSET.getDate("wrkdt") == null)
					{if(!M_rstRSSET.next())	{L_flgEOF = true; break;} else continue;}
				strEMPCT=M_rstRSSET.getString("ep_empct");
				D_OUT.writeBytes(padSTRING('R',"",6));
				crtNWLIN();
				while(!L_flgEOF && strEMPCT.equals(M_rstRSSET.getString("ep_empct")))
				{
					if(M_rstRSSET.getDate("wrkdt") == null)
						{if(!M_rstRSSET.next())	{L_flgEOF = true; break;} else continue;}
					strEMPCT=M_rstRSSET.getString("ep_empct");
					strEMPNO=M_rstRSSET.getString("ep_empno");
					
					while(!L_flgEOF && strEMPCT.equals(M_rstRSSET.getString("ep_empct")) && strEMPNO.equals(M_rstRSSET.getString("ep_empno")))
					{ 	
						if(M_rstRSSET.getString("shfcd").compareTo(M_rstRSSET.getString("shfch"))!=0)
						{
							try{
							D_OUT.writeBytes(padSTRING('R',"",6));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("ep_empno"),10));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("ep_empnm"),20));							
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("wrkdt"),10));							
							D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("shfcd"),10));							
							D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("shfch"),17));							
							//D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("ss_orgsh"),17));							
							//D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("ss_cursh"),17));							
							//D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("sw_wrksh"),17));							
							crtNWLIN();
							}catch(Exception L_EX){setMSG(L_EX,"inside");}
						}
						if(!M_rstRSSET.next())
							{L_flgEOF = true; break;}
					}	
				}
				crtNWLIN();
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnSFCHGSUM");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}	
	
	
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >70)
				{
					genRPFTR();
					genRPHDR();
				}
			}	
			else if(cl_dat.M_intLINNO_pbst >60)
			{		
				genRPFTR();
				genRPHDR();			
			}			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

	
	void genRPHDR()
	{
		try
		{
			if(!flgREGLR_SUM)
			{	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{	
					prnFMTCHR(D_OUT,M_strNOCPI17);
					prnFMTCHR(D_OUT,M_strCPI10);
					prnFMTCHR(D_OUT,M_strCPI17);				
					prnFMTCHR(D_OUT,M_strBOLD);
					prnFMTCHR(D_OUT,M_strNOENH);
				}	
				if(M_rdbHTML.isSelected())
				{
					D_OUT.writeBytes("<b>");
					D_OUT.writeBytes("<HTML><HEAD><Title>Attendance System</title> </HEAD> <BODY><P><PRE style =\" font-size : 7 pt \">");    
					D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				}	
				cl_dat.M_PAGENO +=1;
				crtNWLIN();
    			prnFMTCHR(D_OUT,M_strBOLD);
				if(flgAUTHR==false)
				{
					String L_strJOIN_SS = " on ss_empno=ep_empno and ss_cmpcd=ep_cmpcd and ss_wrkdt is not null and ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
					String L_strJOIN_SW = " on sw_empno=ep_empno and sw_cmpcd=ep_cmpcd and sw_wrkdt is not null  and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText() .toString().trim()))+"'";
					//String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' "+(txtEMPCT.getText().length()>0 ? " and EP_EMPCT= '"+txtEMPCT.getText() .toString().trim() +"'" : "")+(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
					String L_strWHRSTR_MST = " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ltrim(str(EP_HRSBS,20,0)),1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_STSFL<>'U' and EP_EMPCT in ("+strCATDT+")" +(txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "");
					String XX_STSFL = rdbWRKSH.isSelected() ? "SW_STSFL" : "SS_STSFL";
					String HR_XXTRN = rdbWRKSH.isSelected() ? "HR_SWMST" : "HR_SSTRN";
					String JOIN_XX  = rdbWRKSH.isSelected() ? L_strJOIN_SW : L_strJOIN_SS;
					if(rdbNEWRP.isSelected())
					{
						M_strSQLQRY = "select count(*) from HR_EPMST left outer join "+HR_XXTRN+" "+JOIN_XX+" where "+L_strWHRSTR_MST+" and "+XX_STSFL+"='0'";
					}
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					M_rstRSSET.next();
					if(M_rstRSSET.getInt(1)>0)
						strAUTHR="Unauthorised";
					else strAUTHR="Authorised";		
				}
				flgAUTHR=true;
				D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
    			D_OUT.writeBytes(padSTRING('L',"Report Date: ",53));
				D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
    			crtNWLIN();
    			//D_OUT.writeBytes(padSTRING('R',"Shift Schedule Query / Report  ",50));
    			D_OUT.writeBytes(padSTRING('R',"Shift Schedule for the period "+txtFMDAT.getText()+" to "+txtTODAT.getText(),60));
				D_OUT.writeBytes(padSTRING('L',"Page No    : ",43));
				D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
				prnFMTCHR(D_OUT,M_strNOBOLD);
    			crtNWLIN();
				//D_OUT.writeBytes(padSTRING('R',"Shift Schedule for the period "+txtFMDAT.getText()+" to "+txtTODAT.getText(),70));
    			//crtNWLIN();
				if(txtDPTCD.getText().length()>0)
					{D_OUT.writeBytes(padSTRING('R',"Department          : "+lblDPTNM.getText(),50));	crtNWLIN();}	
				//if(txtEMPCT.getText().length()>0)
				if(strCATDT.length()>0)
					{D_OUT.writeBytes(padSTRING('R',"Employee Category   : "+strCATDT,50));	crtNWLIN();}
				if(rdbLEGDV.isSelected())
					D_OUT.writeBytes(padSTRING('R',"Report Type          : Statutary Compliance based on "+(rdbORGSH.isSelected() ? "Original Shift " : (rdbCURSH.isSelected() ? "Current Shift " : "Worked Shift")),70));		   
				else if(rdbSFCHG.isSelected())
					D_OUT.writeBytes(padSTRING('R',"Report Type          : Shift Change based on "+(rdbORGSH.isSelected() ? "Original Shift " : (rdbCURSH.isSelected() ? "Current Shift " : "Worked Shift")),70));
				else
					D_OUT.writeBytes(padSTRING('R',"Report Type          : "+(rdbORGSH.isSelected() ? "Original " : (rdbCURSH.isSelected() ? "Current " : "Worked"))+"("+strAUTHR+")" ,70));
				crtNWLIN();
			}
			else if(flgREGLR_SUM)
			{
				D_OUT.writeBytes("<p><TABLE border=0 width=\"100%\" align=center>");
				D_OUT.writeBytes("<tr><td align = 'center'><PRE style =\" font-size : 20 pt \">");
				D_OUT.writeBytes(cl_dat.M_strCMPNM_pbst);
				D_OUT.writeBytes("</td></tr>");
				if(txtDPTCD.getText().length()>0)
				{
					D_OUT.writeBytes("<tr><td align = 'left'>");
					D_OUT.writeBytes(padSTRING('R',"Department          : "+lblDPTNM.getText(),50));
					D_OUT.writeBytes("</td></tr>");
				}	
				D_OUT.writeBytes("<tr><td align = 'left'>");
    			D_OUT.writeBytes(padSTRING('R',"Shift Schedule for the period "+txtFMDAT.getText()+" to "+txtTODAT.getText(),60));
				D_OUT.writeBytes("</td></tr></table>");
			}
			if(flgREGLR_SUM)
			{
				D_OUT.writeBytes("<p><TABLE border=1 borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray  cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
				D_OUT.writeBytes("<tr>");
			}
			else
			{
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();
			}
			if(flgREGLR_SUM)
				D_OUT.writeBytes("<td align = 'center'><B>");
			D_OUT.writeBytes("EmpNo ");
			if(flgREGLR_SUM)
				D_OUT.writeBytes("</B></td><td><B>");
			D_OUT.writeBytes("Emp Name       ");
			if(flgREGLR_SUM)
				D_OUT.writeBytes("</B></td>");
	        for( int i=0; i<arrHSTKEY.length; i++)
			{
				if(flgREGLR_SUM)
					D_OUT.writeBytes("<td><B>");
				D_OUT.writeBytes(padSTRING('L',""+hstHDRVAL.get(arrHSTKEY[i].toString()).toString(),3));
				if(flgREGLR_SUM)
					D_OUT.writeBytes("</B></td>");
			}
			if(flgREGLR_SUM)
				D_OUT.writeBytes("</tr>");
			else
			{
				crtNWLIN();
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(flgREGLR_SUM)
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
			//System.out.println(L_IOE.toString());
		}
	}
	
	// generating report footer
	
	void genRPFTR()
	{
		try
		{
			if(!flgREGLR_SUM)
			{
				cl_dat.M_intLINNO_pbst = 0;	//new test
				D_OUT.writeBytes(padSTRING('L',"",65));
				crtNWLIN();
				D_OUT.writeBytes("------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();
				crtNWLIN(); crtNWLIN();
				D_OUT.writeBytes(padSTRING('L',flgRPTEND ? "(Head Of the Department)" : "",100)); crtNWLIN();
				D_OUT.writeBytes(padSTRING('L',flgRPTEND ? "------------------------" : "",100)); crtNWLIN();
				D_OUT.writeBytes(padSTRING('R',(rdbSTSFL.isSelected() ? "Shift Status :  [.] Unauthorised  [-] Authorised  [*] Processed" : ""),100)); crtNWLIN();
				prnFMTCHR(D_OUT,M_strNOCPI17);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					prnFMTCHR(D_OUT,M_strEJT);			
				if(M_rdbHTML.isSelected())
					D_OUT.writeBytes("<P CLASS = \"breakhere\">");
			}
			else if(flgREGLR_SUM)
			{
				D_OUT.writeBytes("<p><TABLE border=0 width=\"100%\" align=center>");
				D_OUT.writeBytes("<tr><td align = 'left'>");
				D_OUT.writeBytes("Remarks - A = 0700 TO 1500hrs , B= 1500 TO 2300hrs , C= 2300 TO 0700hrs");
				D_OUT.writeBytes("</td></tr>");
				D_OUT.writeBytes("<tr><td align = 'right'><PRE style =\" font-size : 13 pt \"><B>");
				D_OUT.writeBytes("HOD");
				D_OUT.writeBytes("</B></td></tr></table>");
			}
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}	

	boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().length()==0)
			{
				setMSG("Enter From Date",'E');
				txtFMDAT.requestFocus();
				return false;
			}				
			if(txtTODAT.getText().length()==0)
			{
				setMSG("Enter To Date",'E');
				txtTODAT.requestFocus();
				return false;
			}				
		}
		catch(Exception L_VLD)
		{
			return false;
		}
		return true;
	}
	
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			strRPLOC = cl_dat.M_strREPSTR_pbst;
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "hr_rpssc.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpssc.doc";
			genRPTFL();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				*/	
				}	
				else 
			    {    
					Runtime r = Runtime.getRuntime();
					Process p = null;					    
					p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
			    if(M_rdbHTML.isSelected())
			        p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
			    else
			        p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Shift Schedule Query/Report"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}
	
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		rdbSTSFL.setEnabled(false);
		rdbSFCHG.setEnabled(false);
		rdbLEGDV.setEnabled(false);
	}

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
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' AND ";
					M_strSQLQRY += " CMT_CODCD= '"+txtDPTCD.getText().toString().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
						setMSG("",'N');
						return true;
					}	
					else
					{
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
				}	
				/*if(input == txtEMPCT)
				{
					M_strSQLQRY = "Select distinct EP_EMPCT from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_DPTCD = '"+txtDPTCD.getText() .toString() .trim() +"' and  EP_EMPCT= '"+txtEMPCT.getText() .toString() .trim() +"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						setMSG("",'N');
						return true;

					}	
					else
					{
						setMSG("Enter Valid Employee Category",'E');
						return false;
					}
				}*/	
				if(input == txtFMDAT)
				{
					/*if(M_fmtLCDAT.parse(txtFMDAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("From date can not be greater than Today's date..",'E');
						return false;
					}*/
					//txtTODAT.requestFocus();
				}
				if(input == txtTODAT)
				{
					/*if(M_fmtLCDAT.parse(txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("TO date can not be greater than Today's date..",'E');
						return false;
					}*/
					if(M_fmtLCDAT.parse(txtFMDAT.getText()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText()))>0)
					{
						setMSG("Invalid Date Range..",'E');
						return false;
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"INPVF");
			}
			return true;
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

	/** Fetching Standard In / Out Time for specified shift
	 * Returns Timestamp value in String format "yyyy-MM-dd-HH.mm.ss"
	 */ 
	private String getSHFTM_ST(String LP_SHFCD,String LP_SHFFL,int LP_DAYVL)
	{
		String L_strRETVL = "";
		//System.out.println("getSHFTM_ST : "+LP_SHFCD+" / "+LP_SHFFL);
		if(LP_SHFCD.equalsIgnoreCase("O"))
			return L_strRETVL;
		try
		{
			if(!cl_dat.M_hstMKTCD_pbst.containsKey("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD))
				return L_strRETVL;
			datWRKDT1 = fmtYYYYMMDD.parse(arrHSTKEY[LP_DAYVL].toString());
			
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,+1);    
			datWRKDT2 = M_calLOCAL.getTime();
			if(LP_SHFFL.equalsIgnoreCase("IN"))
			{
				datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)) >= 7 ? datWRKDT1 : datWRKDT2;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)+"."+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(3,5)+".00";
			}
				
			if(LP_SHFFL.equalsIgnoreCase("OUT"))
			{
				datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)) <= 7 ? datWRKDT2 : datWRKDT1;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)+"."+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(3,5)+".00";
			}
	
		}
		catch(Exception L_EX){
			setMSG(L_EX,"getSHFTM_ST");
		}
		return L_strRETVL;
	}	


}
