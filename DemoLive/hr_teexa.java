/**
System Name:Human Resource System.
 
Program Name: Exit Pass Authorization
Author : SSG
Purpose : Fetching Exit Pass Record and Attendance record for verification
 
Source Directory: f:\source\splerp3\hr_teexa.java                         
Executable Directory: F:\exec\splerp3\hr_teexa.class

 
List of tables used:
Table Name		Primary key											Operation done
															Insert	Update	   Query    Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
HR_EXTRN		EX_DOCNO, EX_CMPCD                             /	  /           /         /
                 
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on screen:
Field Name		Column Name		Table name		Type/Size	Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtDOCNO		EX_DOCNO		HR_EXTRN		Varchar(8)		DOC No	
txtOFPFL		EX_OFPFL		HR_EXTRN		Varchar(1)		Official/Personal flag.	
txtAOTTM        SWT_INCTM       HR_SWTRN        TIMESTAMP       Actual Out Time
txtAINTM        SWT_OUTTM       HR_SWTRN        TIMESTAMP       Actual In Time	
txtEMPNO		SWT_EMPNO		HR_SWTRN		Varchar(4)  	Employee No
txtWRKDT        SWT_WRKDT       HR_SWTRN		Date			Work Date
txtEXPRD        SWT_OUTTM       HR_EXTRN        Time            Exit Period
txtWRKSH        SW_WRKSH        HR_SWTRN        Varchar (1)     WRK Shift
txtINCTM_S      SWT_INCTM       HR_SWTRN        TIMESTAMP       STD In Time
txtOUTTM_S      SWT_OUTTM       HR_SWMST        TIMESTAMP       STD Out Time
-----------------------------------------------------------------------------------------------------------------------------------------------------


List of fields with help facility: 
Field Name	Display Description		    		Display Columns							Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
TxtEMPNO	Employee No,Employee Name			EP_EMPNO,EP_EMPNM       				HR_EPMTS
TxtDOCNO	DOC No, DOC Date ,off/per flag  	EX_DOCNO,EX_DOCDT,EX_OFPFL,
			shift,Out time,In time,Auth by		EX_SHFCD,EX_EOTTM,EX_EINTM,EX_AUTBY		HR_EXTRN
TxtAUTBY    Auth BY								cmt_ccsvl								CO_CDTRN
-----------------------------------------------------------------------------------------------------------------------------------------------------


Validations & Other Information:
    - Enter valid Employee No, Work Shift & DOC NO.
  	- From Date must be Smaller Than Or Equal to To Date.
 	- To Date must not be Grater Than Todays Date.
    - To Date must be greater than Or Equal to From Date.
    - In Date Time must be greater than Out Date time.
    - Out time can not be empty.
    
Requirements:
	- List of employees worked within specified period will be displayed.
	- Emp with multiple in/out are displayed for exit pass Authorization.
	- Emp In / Out Time will be taken from machine data  i.e from hr_swtrn.
	- Corresponding Doc no will be assigned for existing In /Out Time. 
	- DOC No help screen will be generated in which all the exit passes belongs to employee no & date will be displayed.
	- DOC NO will not be repeat & display only HOD Authorization of status '2' DOC No in help screen.
	- If Dispaly  Official/Personal flag & 'Authorized by' from Exit Pass Record through DOC NO help screen (hr_extrn).
	- If user want to change Auth By ,while Press F1 to select from List.
	- If In Time is left blank then,calculated Exit Period  using standard shift-end time with shift code wise.
	- Out time will be without final out time & In time will be without initial In time.
	- While punching time not avaliable,In this condition user need to Enable field of wrk date,emp no & Auth by to save new record in hr_extrn .
	- If user enter new record, in this condition display Emp name & Work Shift enter on Emp No.
	- Official / Personal flag display that depend on exit pass type.(i.e. 901=O, 902=P).
	- Exit period calculated with corresponding In / Out time. 
	- User can modify Actual Out/In time,wrk shift,Exit period of the employee which will be recorded in Exit Pass record & Attendance record (hr_extrn,hr_swmst),However Punching time are ptotected from alteration.
	- HRD Authorized Exit Pass with Status '3'(i.e consider HRD Authorization). 
	- If user clicks on Cpy checkbox,copy selected punching time in Actual In/Out time field.
	- When user clicks on View checkbox, it will be display work date,in-time and out-time for previous ,current and next day in JOptionPane.
	- While user click on view UnLinked Exit Pass checkbox,it will be display all Unlinked Exit Pass.(i.e. not Authorized)
	- Update hr_swmst to set sw_epcfl 'Y' for fetch only unAuthorized record to Authorized..
	
	
**/

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;


class hr_teexa extends cl_pbase 
{
	
	private JTextField txtFMDAT;
	private JTextField txtTODAT;
	private JTextField txtEMPCD;//text box emp no field
	private JCheckBox chkULEXP,chkATCOF;
	
	private JButton btnPROCS;
	
	private JPanel pnlVEWAT,pnlULEXP; /**panel for view & Unlinked exit pass chkbox**/
	private JLabel lblEMPNM,lblEMPNM1,lblEMPCD;/**label to display Emp Name in main Screen, view pane & Unlinked Exit pass Panel resp. **/
	
	java.util.Date datWRKDT0;    // Previous day
	java.util.Date datWRKDT1;    // Current day
	java.util.Date datWRKDT2;	//Next day
	
	private  cl_JTable tblEXPDL,tblAUEXP,tblVEWAT,tblULEXP; /**table for display multiple In/Out emp in main screen ,view pane & unlinked Exit Pass panel resp. **/
	private INPVF oINPVF;
	private TBLINPVF objTBLVRF;
	CallableStatement updSWMST_EXP;
    
    
    int TB1_CHKFL = 0; 				JCheckBox chkCHKFL;
	int TB1_WRKDT = 1;              JTextField txtWRKDT;
	int TB1_EMPNO = 2;              JTextField txtEMPNO;
	int TB1_EMPNM = 3;              JTextField txtEMPNM;
	int TB1_EOTTM = 4; 				JTextField txtEOTTM;  //Outgoing Time Actual
	int TB1_EINTM = 5;              JTextField txtEINTM; //Incoming Time Actual
	int TB1_AOTTM = 6; 				JTextField txtAOTTM;  //Outgoing Time Actual
	int TB1_AINTM = 7;              JTextField txtAINTM; //Incoming Time Actual
	int TB1_VEWAT = 8;              JCheckBox chkVEWAT;
	int TB1_SECTM = 9;              JTextField txtSECTM; 
	int TB1_DOCNO = 10; 				JTextField txtDOCNO;
	int TB1_WRKSH = 11;              JTextField txtWRKSH;
	int TB1_EXPRD = 12;				JTextField txtEXPRD;
    int TB1_OTGFL = 13;				
	int TB1_OFPFL = 14;				JTextField txtOFPFL;
    int TB1_AUTBY = 15;             JTextField txtAUTBY;
    int TB1_INCTM_S = 16; 			JTextField txtINCTM_S;
    int TB1_OUTTM_S = 17;           JTextField txtOUTTM_S;
    
    String L_strEXPRD="";   // String for Calculate Exit period
    int intROWCNT=0,intROWCNT1=0;;        // Row count for tblEXPDL
    Hashtable<String,String> hstEXTPS;
	Hashtable<String,String> hstSTDIN;
	Hashtable<String,String> hstSTDOT;
	Vector<String> vtrSWTIN;
	Vector<String> vtrSWTOT;
	
	protected SimpleDateFormat fmtLCDTM_L=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	/** For Calender in Locale format	Required for calculations /comparision of data/datetime values */	
	private SimpleDateFormat fmtHHMM = new SimpleDateFormat("HH:mm"); 
	hr_teexa()		/*  Constructor   */
	{
		super(1);
		try
		{
			setMatrix(20,8);	
			add(new JLabel("From Date"),2,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),2,4,1,1,this,'L');
			
			add(new JLabel("To Date"),2,5,1,1,this,'L');
			add(txtTODAT = new TxtDate(),2,6,1,1,this,'L');

			add(btnPROCS= new JButton("PROCESS"),2,7,1,1,this,'L');
			
			add(new JLabel("Employee No"),3,3,1,1,this,'L');
			add(txtEMPCD = new TxtLimit(4),3,4,1,1,this,'L');
			add(lblEMPNM = new JLabel(),3,5,1,1.5,this,'L');
			add(chkULEXP=new JCheckBox("View Unlinked Exit Pass"),3,7,1,2,this,'L'); 
			add(chkATCOF=new JCheckBox("Auto Confirmation"),4,7,1,2,this,'L'); 
    		
    		String[] L_strTBLHD = {"","Date","Emp No","Emp Name","EP-Out","EP-In","Out Time","In Time","View","Sec TM","DOC No","Shf","Exit Period","Ex aftr std tm","O/P","Auth By","STD IN","STD OUT"};
    		int[] L_intCOLSZ = {10,70,60,100,70,70,70,70,25,70,70,30,70,25,25,50,70,70};
    		tblEXPDL= crtTBLPNL1(this,L_strTBLHD,800,5,1,7,7.5,L_intCOLSZ,new int[]{0,TB1_VEWAT,TB1_OTGFL});
    		
    		tblEXPDL.addKeyListener(this);
    	   
    		tblEXPDL.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
    		tblEXPDL.setCellEditor(TB1_WRKDT,txtWRKDT = new TxtDate());
    		tblEXPDL.setCellEditor(TB1_EMPNO,txtEMPNO = new TxtLimit(4));
    		tblEXPDL.setCellEditor(TB1_EMPNM,txtEMPNM = new TxtLimit(50));
    		tblEXPDL.setCellEditor(TB1_WRKSH,txtWRKSH = new TxtLimit(1));
    		tblEXPDL.setCellEditor(TB1_EOTTM,txtAOTTM = new TxtTime()); 
    		tblEXPDL.setCellEditor(TB1_EINTM,txtAINTM=new TxtTime());
			tblEXPDL.setCellEditor(TB1_AOTTM,txtAOTTM = new TxtTime()); 
    		tblEXPDL.setCellEditor(TB1_AINTM,txtAINTM=new TxtTime());
    		tblEXPDL.setCellEditor(TB1_DOCNO,txtDOCNO=new TxtNumLimit(8));
    		tblEXPDL.setCellEditor(TB1_OFPFL,txtOFPFL = new TxtLimit(1));
    		tblEXPDL.setCellEditor(TB1_AUTBY,txtAUTBY = new TxtLimit(6));
    		tblEXPDL.setCellEditor(TB1_VEWAT,chkVEWAT = new JCheckBox());
    		
    	
    		txtAOTTM.addKeyListener(this);
    		txtAINTM.addKeyListener(this);
    		txtDOCNO.addKeyListener(this);
    		txtWRKSH.addKeyListener(this);
			txtEMPNO.addKeyListener(this);
			txtAUTBY.addKeyListener(this);
			
			txtWRKDT.addFocusListener(this);
			txtEMPNO.addFocusListener(this);
			
			add(new JLabel("Authorized Exit Passes:"),12,1,1,2,this,'L');
			tblAUEXP= crtTBLPNL1(this,L_strTBLHD,800,13,1,5,7.5,L_intCOLSZ,new int[]{0,TB1_VEWAT,TB1_OTGFL});
			tblAUEXP.addKeyListener(this);
			//tblAUEXP.setCellEditor(TB1_VEWAT,chkVEWAT = new JCheckBox());
			
			oINPVF=new INPVF();
			objTBLVRF = new TBLINPVF();
    		txtEMPCD.setInputVerifier(oINPVF);
    		txtFMDAT.setInputVerifier(oINPVF);
    		txtTODAT.setInputVerifier(oINPVF);
    		tblEXPDL.setInputVerifier(objTBLVRF);
    		
    		((JCheckBox) tblEXPDL.cmpEDITR[TB1_VEWAT]).addMouseListener(this);
    		//((JCheckBox) tblAUEXP.cmpEDITR[TB1_VEWAT]).addMouseListener(this);
			((JTextField) tblEXPDL.cmpEDITR[TB1_AOTTM]).addMouseListener(this);
	
			((JTextField)tblEXPDL.cmpEDITR[TB1_EMPNM]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_WRKSH]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_OFPFL]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_EXPRD]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_EOTTM]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_EINTM]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_SECTM]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_DOCNO]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_INCTM_S]).setEditable(false);
			((JTextField)tblEXPDL.cmpEDITR[TB1_OUTTM_S]).setEditable(false);
			
			setENBL(false);
    		setVisible(true);
    		
			hstSTDIN = new Hashtable<String,String>();
			hstSTDOT = new Hashtable<String,String>();
			vtrSWTIN = new Vector<String>();
			vtrSWTOT = new Vector<String>();
			
			String L_strSQLQRY= " Select CMT_CODCD,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='M"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP='COXXSHF'";
			ResultSet L_rstRSSET= cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					hstSTDIN.put(L_rstRSSET.getString("CMT_CODCD"),L_rstRSSET.getString("CMT_CHP01"));
					hstSTDOT.put(L_rstRSSET.getString("CMT_CODCD"),L_rstRSSET.getString("CMT_CHP02"));
				}
				L_rstRSSET.close();
			}	
    		
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
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					txtFMDAT.requestFocus();
					tblAUEXP.setEnabled(false);
					//((JCheckBox)tblAUEXP.cmpEDITR[TB1_VEWAT]).setEnabled(true);
					((JCheckBox)tblAUEXP.cmpEDITR[TB1_OTGFL]).setEnabled(true);
					/**set previous date of current date in From date text box**/
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));      
						M_calLOCAL.add(Calendar.DATE,-1);    
						txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						txtTODAT.setText("");
						setMSG("Please enter date..",'N');
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						clrCOMP();
						tblEXPDL.setEnabled(false);
					}
				}
				else
					setENBL(false);
				
			}
			
		    else if(M_objSOURC == txtEMPCD)
			{
				if(txtEMPCD.getText().length()==0)
					lblEMPNM.setText("");
			}
			else if (M_objSOURC==chkULEXP)
			{
				if(chkULEXP.isSelected())
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					dspULEXP();   /**generate Unlinked Exit Pass in pnlULEXP panel after click on Unlinked Exit Pass chkbox.**/
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if (M_objSOURC==chkATCOF)
			{
				if(chkATCOF.isSelected())
				{
					//this.setCursor(cl_dat.M_curWTSTS_pbst);
					exeEXTPS();   /**generate Exit Pass in tabel,when  click on display Exit Pass chkbox.*/
					//this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				chkATCOF.setSelected(false);
			}
			
			/**Locking /Closing Date update in CO_CDTRN table**/
			else if(M_objSOURC==btnPROCS)
			{
				if(txtFMDAT.getText().length()<10 || txtTODAT.getText().length()<10)
				{
					setMSG("Please Enter Dates to Process...",'E');
					return;
				}
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				updSWMST_EXP = cl_dat.M_conSPDBA_pbst.prepareCall("call updSWMST_EXP(?,?,?)");
				updSWMST_EXP.setString(1,cl_dat.M_strCMPCD_pbst);
				updSWMST_EXP.setString(2,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText())));
				updSWMST_EXP.setString(3,M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText())));
				updSWMST_EXP.executeUpdate();
				this.setCursor(cl_dat.M_curDFSTS_pbst);
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
				if(M_objSOURC == txtFMDAT)
				{
					txtTODAT.requestFocus();
					txtTODAT.setText(txtFMDAT.getText());
					if(txtTODAT.getText().length()==0)
						setMSG("Enter To Date..",'N');
				}
				else if(M_objSOURC == txtTODAT)
				{
					txtEMPCD.requestFocus();
					setMSG("Enter Employee No or Press F1 to Select form List..",'N');
				}
				else if(M_objSOURC == txtEMPCD)
				{
					if(txtFMDAT.getText().length()>0 && txtTODAT.getText().length()==0)
					{
						txtTODAT.requestFocus();
						setMSG("Please Enter To Date",'E');
					}
					if(txtEMPCD.getText().length()==0)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
						{
							tblEXPDL.clrTABLE();
							inlTBLEDIT(tblEXPDL);
							getDATA();/**fetch record of Emp with multiple in/out**/
						}
						
					}
					tblEXPDL.requestFocus();
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{	
				/**Help screen for text box emp no**/
				if(M_objSOURC==txtEMPCD)		
				{
						cl_dat.M_flgHELPFL_pbst = true;
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						M_strHLPFLD = "txtEMPCD";
						String L_ARRHDR[] = {"Employee No","Employee Name"};
	    				
						/*M_strSQLQRY = " select distinct swt_empno,swt_wrkdt from hr_swtrn  where swt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
						//if(txtFMDAT.getText().trim().length()>0 && txtTODAT.getText().trim().length()>0)
							 M_strSQLQRY+= " and swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
						M_strSQLQRY+= " group by swt_empno,swt_wrkdt having count(*)>1 ";
						M_strSQLQRY += " order by swt_empno";*/
						M_strSQLQRY = " select EP_EMPNO,EP_LSTNM||' '||EP_FSTNM||' '||EP_MDLNM EP_EMPNM,EP_DPTNM,EP_DPTCD from HR_EPMST where  ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null";
						M_strSQLQRY+=" and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";	
						if(txtEMPCD.getText().length() >0)				
							M_strSQLQRY += " AND EP_EMPNO like '"+txtEMPCD.getText().trim()+"%'";
						M_strSQLQRY += " order by EP_EMPNO";
						
	    				//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
	    				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
	    				this.setCursor(cl_dat.M_curDFSTS_pbst);
    			}
				else if(M_objSOURC==txtDOCNO)		
				{
						cl_dat.M_flgHELPFL_pbst = true;
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						M_strHLPFLD = "txtDOCNO";
						String L_ARRHDR[] = {"DOC No","Off/Pers","Auth By","Shift","Out Time","In Time","Sec.Out Tm "};
	    				M_strSQLQRY = "select EX_DOCNO,EX_OFPFL,EX_AUTBY,EX_SHFCD,EX_EOTTM,EX_EINTM,EX_SECTM from HR_EXTRN where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
	    				M_strSQLQRY += " and EX_EMPNO='"+tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_EMPNO)+"'";
	    				M_strSQLQRY += " and EX_DOCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_WRKDT).toString()))+"'";
	    				M_strSQLQRY += " and EX_STSFL = '2' order by EX_DOCNO";
	    				M_flgBIGHLP=true;
	    				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,7,"CT");
	    				this.setCursor(cl_dat.M_curDFSTS_pbst);
	    				
    			}
				else if(M_objSOURC == txtAUTBY)
				{
					M_strHLPFLD = "txtAUTBY";
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY = " select distinct cmt_ccsvl from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and substr(cmt_codcd,1,4)='"+tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_EMPNO).toString()+"'";
					if(tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_AUTBY).toString().length()>0)				
						M_strSQLQRY += " AND cmt_ccsvl like '"+tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_AUTBY).toString()+"%'";

					M_strSQLQRY += " order by cmt_ccsvl";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Sanctioning Authority"},1,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);

				}	
				/**Help screen for emp no in table**/
				else if(M_objSOURC == txtEMPNO)
				{
					M_strHLPFLD = "txtEMPNO";
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					String L_ARRHDR[] = {"Employee No","Employee Name","Work Shift","STD IN","STD OUT"};
					
					M_strSQLQRY = "select EP_EMPNO, EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM,SW_WRKSH,SW_INCST,SW_OUTST";
					M_strSQLQRY+=" from HR_EPMST left outer join HR_SWMST on SW_CMPCD=EP_CMPCD and SW_EMPNO=EP_EMPNO and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_WRKDT).toString()))+"' ";
					M_strSQLQRY+=" where ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' ";
					if(tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_EMPNO).toString().length()>0)				
						M_strSQLQRY += " AND EP_EMPNO like '"+tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_EMPNO).toString()+"%'";
					M_strSQLQRY += " order by EP_EMPNO";
					
					//System.out.println(">>>>EMPNO F1>>>>"+M_strSQLQRY);
    				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,5,"CT");
    				this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"keypressed");		
		}		
		
	}
	/**method to focus Gained in empty field to add new record & in wrk date to set To Date**/
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtWRKDT)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				/**enabled & disabled table field **/
				  if(tblEXPDL.getSelectedRow()>=intROWCNT )
				 // if(tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_WRKDT).toString().length()==0 && tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_EMPNO).toString().length()==0)
				  {
					  ((JTextField)tblEXPDL.cmpEDITR[TB1_WRKDT]).setEditable(true);
					  ((JTextField)tblEXPDL.cmpEDITR[TB1_EMPNO]).setEditable(true);
				  }
				  else
				  {
					  ((JTextField)tblEXPDL.cmpEDITR[TB1_WRKDT]).setEditable(false);
					  ((JTextField)tblEXPDL.cmpEDITR[TB1_EMPNO]).setEditable(false); 
				  }
			}
		
			if(tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_WRKDT).toString().equals(""))
			{
				/**set To Date in Wrk Date field of table **/
				tblEXPDL.setValueAt(txtTODAT.getText(),tblEXPDL.getSelectedRow(),TB1_WRKDT);
			}
		}
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtEMPCD"))
			{
			      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				  txtEMPCD.setText(L_STRTKN.nextToken());
				  //lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
			}
			else if(M_strHLPFLD.equals("txtDOCNO"))
			{
				String [] staDATA = null;
				staDATA = cl_dat.M_strHELP_pbst.replace('|','~').split("~");
				//System.out.println(staDATA[0]+">>"+staDATA[1]+">>"+staDATA[2]+">>"+staDATA[3]+">>"+staDATA[4]+">>"+staDATA[5]+">>"+staDATA[6]);
				txtDOCNO.setText(staDATA[0]);
				tblEXPDL.setValueAt(new Boolean(true),tblEXPDL.getSelectedRow(),TB1_CHKFL);
			    tblEXPDL.setValueAt(staDATA[1],tblEXPDL.getSelectedRow(),TB1_OFPFL);
			    tblEXPDL.setValueAt(staDATA[2],tblEXPDL.getSelectedRow(),TB1_AUTBY);
				if(staDATA.length>4 && staDATA[4].length()>1 && tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_AOTTM).toString().equals(""))
					tblEXPDL.setValueAt(staDATA[4].substring(11,16),tblEXPDL.getSelectedRow(),TB1_AOTTM);  
				if(staDATA.length>5 && staDATA[5].length()>1 && tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_AINTM).toString().equals(""))
					tblEXPDL.setValueAt(staDATA[5].substring(11,16),tblEXPDL.getSelectedRow(),TB1_AINTM);  
	    		if(staDATA.length>6 && staDATA[6].length()>1)
					tblEXPDL.setValueAt(staDATA[6].substring(11,16),tblEXPDL.getSelectedRow(),TB1_SECTM);
			}
			else if(M_strHLPFLD.equals("txtAUTBY"))
			{
			      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				  txtAUTBY.setText(L_STRTKN.nextToken());
				 
			}
			else if(M_strHLPFLD.equals("txtEMPNO"))
			{
			      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			      txtEMPNO.setText(L_STRTKN.nextToken());
			      tblEXPDL.setValueAt(L_STRTKN.nextToken(),tblEXPDL.getSelectedRow(),TB1_EMPNM);
			      tblEXPDL.setValueAt(L_STRTKN.nextToken(),tblEXPDL.getSelectedRow(),TB1_WRKSH);
			      tblEXPDL.setValueAt(L_STRTKN.nextToken(),tblEXPDL.getSelectedRow(),TB1_INCTM_S);
			      tblEXPDL.setValueAt(L_STRTKN.nextToken(),tblEXPDL.getSelectedRow(),TB1_OUTTM_S);
			}
		}
		catch(Exception E_VR)
		{
			E_VR.printStackTrace();
			setMSG(E_VR,"exeHLPOK()");		
		}
	}	
	
	/**Validation for enter only selected row data**/
	boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() ==0)
	    	{
				txtFMDAT.requestFocus();
	    		setMSG("Enter the From Date",'E');
	    		return false;
	    	}
			else if(txtTODAT.getText().trim().length() ==0)
	    	{
				txtTODAT.requestFocus();
	    		setMSG("Enter the To Date..",'E');
	    		return false;
	    	}
			
			for(int P_intROWNO=0;P_intROWNO<tblEXPDL.getRowCount();P_intROWNO++)
			{
				if(tblEXPDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					if(tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString().length()==0)
					{
						setMSG("Enter Employee No at Selected row in the table..",'E');
						return false;
						
					}
					if(tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString().length()==0)
					{
						setMSG("Enter Work Date for "+tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString()+" in the table..",'E');
						return false;
						
					}
					if(tblEXPDL.getValueAt(P_intROWNO,TB1_DOCNO).toString().length()==0)
					{
						setMSG("Enter DOC No for "+tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString()+ "Emp No at Date "+tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()+" in the table..",'E');
						return false;
					}
					if(tblEXPDL.getValueAt(P_intROWNO,TB1_AOTTM).toString().length() == 0)
					{
						setMSG("Actual Out Time not entered for "+tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString()+ " Emp No at Date "+tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()+" in the table..",'E');
						return false;
					}
					/*if(tblEXPDL.getValueAt(P_intROWNO,TB1_AINTM).toString().length() == 0)
					{
						setMSG("Actual In Time not entered at row "+Integer.valueOf(P_intROWNO+1)+" in the table",'E');
						return false;
					}*/
					
					if(tblEXPDL.getValueAt(P_intROWNO,TB1_AOTTM).toString().length()>0 && tblEXPDL.getValueAt(P_intROWNO,TB1_AINTM).toString().length()>0)
					if(M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString().trim()+" "+tblEXPDL.getValueAt(P_intROWNO,TB1_AOTTM).toString())).compareTo(M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString().trim()+" "+tblEXPDL.getValueAt(P_intROWNO,TB1_AINTM).toString())))>0)
					{
						setMSG("In Time should be greater than Out Time for "+tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString()+ " Emp No at Date "+tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()+" in the table..",'E');
						return false;
					}
					
					for(int i=0;i<tblEXPDL.getRowCount();i++)
					{
						if(tblEXPDL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
						{
							if(tblEXPDL.getValueAt(P_intROWNO,TB1_DOCNO).toString().length()>0 && tblEXPDL.getValueAt(i,TB1_DOCNO).toString().length()>0 && i!=P_intROWNO)
							if(tblEXPDL.getValueAt(P_intROWNO,TB1_DOCNO).toString().compareTo(tblEXPDL.getValueAt(i,TB1_DOCNO).toString().trim())==0)
							{
								setMSG("DOC NO should be Unique for "+tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString()+ "Emp No at Date "+tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()+" in the table..",'E');
								return false;
							}
						}
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
	
	/**method to clear component after click on Authorize button  **/
	void clrCOMP()
	{
		try
		{
			txtFMDAT.setText("");
			txtTODAT.setText("");
			txtEMPCD.setText(""); 
			tblEXPDL.clrTABLE();
			inlTBLEDIT(tblEXPDL);
			tblAUEXP.clrTABLE();
			inlTBLEDIT(tblAUEXP);
		}	
		catch(Exception E)
		{
			setMSG(E,"clrCOMP()");			
		}	
	}
	
   /**authorize selected row record after click on authorized button**/
	void exeSAVE()
	{
		try
		{
			int P_intROWNO=0;
			if(!vldDATA()) 
			{
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
			{
				
				if(!vldEXTPS())/**validation to Authorize  **/
				{
					return;
				}
				
				for(P_intROWNO=0;P_intROWNO<tblEXPDL.getRowCount();P_intROWNO++)
				{
					if(tblEXPDL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeAUTREC(P_intROWNO); //update record
						setMSG("Records Authorized Successfully..",'N');	
					}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE")) 
			{
				setMSG("Record Updated Successfully...",'N');	
				//clrCOMP();	
				getDATA();		 /**fetch Exit pass entry & authorized data after that update.**/		
			}
		}
		catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeSAVE"); 
	    }
	} 
	
	/**Validation for Authorize **/
	private boolean vldEXTPS()
	{
		String L_strEMPNO;
		int L_intOPTN;
		for(int i=0;i<tblEXPDL.getRowCount();i++)
		{
			if(tblEXPDL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
			{
				L_strEMPNO=tblEXPDL.getValueAt(i,TB1_EMPNO).toString();
				for(int j=0;j<tblEXPDL.getRowCount();j++)
				{
					if(tblEXPDL.getValueAt(j,TB1_CHKFL).toString().equals("false")
					&& tblEXPDL.getValueAt(j,TB1_EMPNO).toString().equals(L_strEMPNO))
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
	
	/** Method to update hr_extrn record with status '3'to authorized & hr_swmst record with status 'Y'
	 *  to display only unAuthorized record 
	 *  */
	private void exeAUTREC(int P_intROWNO)
	{ 
	  try
	  {
		    inlTBLEDIT(tblEXPDL);
			M_strSQLQRY =" Update HR_EXTRN set";
			M_strSQLQRY +=" EX_SHFCD='"+tblEXPDL.getValueAt(P_intROWNO,TB1_WRKSH).toString()+"',";
			M_strSQLQRY +=" EX_OUTTM='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()+" "+tblEXPDL.getValueAt(P_intROWNO,TB1_AOTTM).toString()))+"',";
			if(tblEXPDL.getValueAt(P_intROWNO,TB1_AINTM).toString().length()>0)
				M_strSQLQRY +=" EX_INCTM='"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()+" "+tblEXPDL.getValueAt(P_intROWNO,TB1_AINTM).toString()))+"',";
			if(tblEXPDL.getValueAt(P_intROWNO,TB1_OTGFL).toString().equals("true"))
			{
				M_strSQLQRY +=" EX_OTGFL = 'Y',";	//STSFL
				M_strSQLQRY +=" EX_AOTTM='00:00',";
			}
			else
				M_strSQLQRY +=" EX_AOTTM='"+tblEXPDL.getValueAt(P_intROWNO,TB1_EXPRD).toString()+"',";
			M_strSQLQRY +=" EX_STSFL = '3',";	//STSFL
			M_strSQLQRY +=" EX_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY +=" EX_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY +=" where EX_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' and EX_EMPNO= '"+tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString()+"'";
			M_strSQLQRY +=" AND EX_DOCDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()))+"'";
			M_strSQLQRY +=" AND EX_DOCNO= '"+tblEXPDL.getValueAt(P_intROWNO,TB1_DOCNO).toString()+"'";
			//System.out.println(">>>UPDATE>>"+M_strSQLQRY);
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			
			//M_strSQLQRY =" Update HR_SWMST set";
			//M_strSQLQRY +=" SW_EPCFL = 'Y'";
			//M_strSQLQRY +=" where SW_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"' and SW_EMPNO= '"+tblEXPDL.getValueAt(P_intROWNO,TB1_EMPNO).toString()+"'";
			//M_strSQLQRY +=" AND SW_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEXPDL.getValueAt(P_intROWNO,TB1_WRKDT).toString()))+"'";
			//System.out.println(">>>UPDATE>>"+M_strSQLQRY);
			//cl_dat.M_flgLCUPD_pbst = true;
			//cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	  }
	  catch(Exception L_EX)
      {
         cl_dat.M_flgLCUPD_pbst=false;
		 cl_dat.exeDBCMT("exeAUTREC");
		 this.setCursor(cl_dat.M_curDFSTS_pbst);
         setMSG(L_EX,"exeAUTREC()"); 
      }
	}

	
	/**
	 * Method to get Data from HR_SWTRN table in tblEXPDL
	 * In time -without initial In time
	 * Out time - without final Out time
	 * */
	private void getDATA() 
	{
		try
		{ 
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			tblEXPDL.clrTABLE();
			setMSG("Fetching Records ...",'N');
			inlTBLEDIT(tblEXPDL);
			tblAUEXP.clrTABLE();
			inlTBLEDIT(tblAUEXP);
			
			java.sql.Timestamp L_tmsINCST=null,L_tmsOUTST=null;
			java.sql.Timestamp L_tmsINCTM=null,L_tmsOUTTM=null;
			java.sql.Timestamp L_tmsINCTM_EX=null,L_tmsOUTTM_EX=null;
			java.sql.Timestamp L_tmsXXXTM1=null,L_tmsXXXTM2=null;
			
			String L_strWRKDT="",L_strWRKSH="",L_strEMPNO="",L_strEMPNO1="",L_strEMPNM="",L_strDOCNO="",L_strOFPFL="",L_strAUTBY="";
		
			SimpleDateFormat L_fmtLCDTM=new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
			/*M_strSQLQRY= " select distinct A.swt_wrkdt,A.swt_empno,a.swt_srlno,A.swt_inctm,ifnull(A.swt_incfl,'') swt_incfl,A.swt_outtm,ifnull(A.swt_outfl,'') swt_outfl,sw_wrksh,trim(ifnull(ep_lstnm,' '))||' '||left(ifnull(ep_fstnm,' '),1) ||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  ep_empnm, sw_inctm,sw_outtm,sw_incst,sw_outst";
			M_strSQLQRY+= " from hr_swtrn A,hr_epmst,hr_swmst";
			M_strSQLQRY+= " where sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt=A.swt_wrkdt and sw_empno=A.swt_empno and sw_stsfl='9' and ifNull(sw_epcfl,'') <> 'Y' and (sw_inctm is not null and sw_outtm is not null) and  a.swt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and a.swt_cmpcd = ep_cmpcd and A.swt_empno =ep_empno and ep_stsfl <> 'U' and ep_lftdt is null and A.swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
			if(txtEMPCD.getText().length()>0)
				M_strSQLQRY+= " and A.swt_empno='"+txtEMPCD.getText()+"'";
			M_strSQLQRY+= " and char(a.swt_wrkdt)||A.swt_empno in(select char(b.swt_wrkdt)||b.swt_empno from hr_swtrn B where B.swt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and  b.swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"' ";
			M_strSQLQRY+= " group by char(b.swt_wrkdt) ||b.swt_empno having count(*)>1)";
			M_strSQLQRY+= " union select distinct A.swt_wrkdt,A.swt_empno,a.swt_srlno,A.swt_inctm,ifnull(A.swt_incfl,'') swt_incfl,A.swt_outtm,ifnull(A.swt_outfl,'') swt_outfl,sw_wrksh,trim(ifnull(ep_lstnm,' '))||' '||left(ifnull(ep_fstnm,' '),1) ||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  ep_empnm, sw_inctm,sw_outtm,sw_incst,sw_outst";
			M_strSQLQRY+= " from hr_swtrn A ,hr_epmst,hr_swmst";
			M_strSQLQRY+= " where sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_wrkdt=A.swt_wrkdt and sw_empno=A.swt_empno and sw_stsfl='9' and ifNull(sw_epcfl,'') <> 'Y' and (sw_inctm is not null and sw_outtm is not null) and  a.swt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and a.swt_cmpcd = ep_cmpcd and A.swt_empno =ep_empno and ep_stsfl <> 'U' and ep_lftdt is null and A.swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
			if(txtEMPCD.getText().length()>0)
				M_strSQLQRY+= " and A.swt_empno='"+txtEMPCD.getText()+"'";
			M_strSQLQRY+= " and char(a.swt_wrkdt)||A.swt_empno  in (select char(ex_docdt)||ex_empno from hr_extrn   where ex_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and  ex_docdt between  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"') ";
			M_strSQLQRY+= " order by swt_wrkdt,swt_empno,swt_srlno";
			
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			*/
			
			vtrSWTIN.clear();
			vtrSWTOT.clear();
			M_strSQLQRY= " select swt_wrkdt,swt_empno,swt_inctm,swt_outtm";
			M_strSQLQRY+=" from hr_swtrn";
			M_strSQLQRY+=" where swt_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and swt_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"' order by swt_wrkdt,swt_empno,swt_srlno";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("swt_inctm")!=null)
						vtrSWTIN.add(M_fmtLCDAT.format(M_rstRSSET.getDate("swt_wrkdt"))+"|"+M_rstRSSET.getString("swt_empno")+"|"+M_rstRSSET.getString("swt_inctm").substring(11,16));
					if(M_rstRSSET.getString("swt_outtm")!=null)
						vtrSWTOT.add(M_fmtLCDAT.format(M_rstRSSET.getDate("swt_wrkdt"))+"|"+M_rstRSSET.getString("swt_empno")+"|"+M_rstRSSET.getString("swt_outtm").substring(11,16));
				}
			}
			M_rstRSSET.close();
			
			M_strSQLQRY= " select distinct ex_docdt,ex_empno,ex_eottm,ex_eintm,ex_outtm,ex_inctm,ex_aottm,ex_otgfl,ex_sectm,ex_docno,ex_OFPFL,ex_autby,ex_shfcd,ex_stsfl,trim(ifnull(ep_lstnm,' '))||' '||left(ifnull(ep_fstnm,' '),1) ||'.'||left(ifnull(ep_mdlnm,' '),1)||'.'  ep_empnm";
			M_strSQLQRY+=" from hr_extrn,hr_epmst";
			M_strSQLQRY+=" where ex_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and ex_cmpcd = ep_cmpcd and ex_empno = ep_empno and ifNull(ex_stsfl,'') in ('2','3') and ifnull(ep_stsfl,'') <> 'U' and ep_lftdt is null and ex_docdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'  AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
			M_strSQLQRY+=" order by ex_docdt,ex_empno";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			
			L_tmsXXXTM1 = null;
			L_tmsXXXTM2 = null;
			boolean L_flgXXXTM1 = false;
			boolean L_flgXXXTM2 = false;
			boolean L_flgEXTAVL = false;
			intROWCNT=0;
			intROWCNT1=0;
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("ex_stsfl").equals("2"))
					{
						getEXPDL(M_rstRSSET,tblEXPDL,intROWCNT);
						intROWCNT++;
					}
					else
					{
						getEXPDL(M_rstRSSET,tblAUEXP,intROWCNT1);
						if(M_rstRSSET.getString("ex_outtm")!= null)
							tblAUEXP.setValueAt(M_rstRSSET.getString("ex_outtm").substring(11,16),intROWCNT1,TB1_AOTTM);
						if(M_rstRSSET.getString("ex_inctm")!= null)
							tblAUEXP.setValueAt(M_rstRSSET.getString("ex_inctm").substring(11,16),intROWCNT1,TB1_AINTM);
						if(M_rstRSSET.getTime("ex_aottm")!= null)
							tblAUEXP.setValueAt(fmtHHMM.format(M_rstRSSET.getTime("ex_aottm")).substring(0,5),intROWCNT1,TB1_EXPRD);
						if(nvlSTRVL(M_rstRSSET.getString("ex_otgfl"),"").equals("Y"))
							tblAUEXP.setValueAt(new Boolean(true),intROWCNT1,TB1_OTGFL);
						else
							tblAUEXP.setValueAt(new Boolean(false),intROWCNT1,TB1_OTGFL);
						intROWCNT1++;
					}
					
					//tblEXPDL.setValueAt(new Boolean(true),i,TB1_CHKFL);	

					//tblEXPDL.setValueAt(L_tmsINCST==null ? "" : M_fmtLCDTM.format(L_tmsINCST),intROWCNT,TB1_INCTM_S);
					//tblEXPDL.setValueAt(L_tmsOUTST==null ? "" : M_fmtLCDTM.format(L_tmsOUTST),intROWCNT,TB1_OUTTM_S);
				}
			}
			M_rstRSSET.close();

			exeEXTPS();
			
		}
		catch(Exception L_EX)
		{
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getDATA()"); 
		}
       this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Method to get Data from HR_SWTRN table in tblEXPDL
	 * In time -without initial In time
	 * Out time - without final Out time
	 * */
	void getEXPDL(ResultSet LP_RSSET,cl_JTable LP_TBL,int LP_ROWNO)
	{
		try
		{
			LP_TBL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("ex_docdt")),LP_ROWNO,TB1_WRKDT);
			LP_TBL.setValueAt(M_rstRSSET.getString("ex_empno"),LP_ROWNO,TB1_EMPNO);
			LP_TBL.setValueAt(M_rstRSSET.getString("ep_empnm"),LP_ROWNO,TB1_EMPNM);
			LP_TBL.setValueAt(M_rstRSSET.getString("ex_eottm").substring(11,16),LP_ROWNO,TB1_EOTTM);
			if(M_rstRSSET.getString("ex_eintm")!= null)
				LP_TBL.setValueAt(M_rstRSSET.getString("ex_eintm").substring(11,16),LP_ROWNO,TB1_EINTM);
			if(M_rstRSSET.getString("ex_sectm")!= null)
				LP_TBL.setValueAt(M_rstRSSET.getString("ex_sectm").substring(11,16),LP_ROWNO,TB1_SECTM);
			LP_TBL.setValueAt(M_rstRSSET.getString("ex_docno"),LP_ROWNO,TB1_DOCNO);
			LP_TBL.setValueAt(M_rstRSSET.getString("ex_OFPFL"),LP_ROWNO,TB1_OFPFL);
			LP_TBL.setValueAt(M_rstRSSET.getString("ex_autby"),LP_ROWNO,TB1_AUTBY);
			LP_TBL.setValueAt(M_rstRSSET.getString("ex_shfcd"),LP_ROWNO,TB1_WRKSH);
			LP_TBL.setValueAt(hstSTDIN.get(M_rstRSSET.getString("ex_shfcd")),LP_ROWNO,TB1_INCTM_S);
			LP_TBL.setValueAt(hstSTDOT.get(M_rstRSSET.getString("ex_shfcd")),LP_ROWNO,TB1_OUTTM_S);
			
		}
		catch(Exception EX)
		{
			setMSG(EX,"getEXPDL()"); 
		}
	}
	
	void inlTBLEDIT(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}	
	
	
	/**Method to Calculate Exit Period**/
	 void setEXTIME(int LP_ROWID)
	    {
			try
	        {
				/**calculate Actual exit period*/
			   if(tblEXPDL.getValueAt(LP_ROWID,TB1_AOTTM).toString().length()>0 && tblEXPDL.getValueAt(LP_ROWID,TB1_AINTM).toString().length()>0)
				{
					L_strEXPRD = subTIME(tblEXPDL.getValueAt(LP_ROWID,TB1_AINTM).toString().substring(0,5),tblEXPDL.getValueAt(LP_ROWID,TB1_AOTTM).toString().substring(0,5));
					tblEXPDL.setValueAt(L_strEXPRD,LP_ROWID,TB1_EXPRD);
					
				}
				else if(tblEXPDL.getValueAt(LP_ROWID,TB1_AOTTM).toString().length()>0  && tblEXPDL.getValueAt(LP_ROWID,TB1_WRKSH).toString().length()==1)
				{
					
					L_strEXPRD = subTIME(tblEXPDL.getValueAt(LP_ROWID,TB1_OUTTM_S).toString(),tblEXPDL.getValueAt(LP_ROWID,TB1_AOTTM).toString().substring(0,5));
					tblEXPDL.setValueAt(L_strEXPRD,LP_ROWID,TB1_EXPRD);
 				}
			}
	        catch(Exception L_EX)
	        {
			    setMSG(L_EX, "setEXTIME");
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
			if(M_objSOURC==chkVEWAT)
			{	
				//tblEXPDL.setValueAt(new Boolean(false),tblEXPDL.getSelectedRow(),TB1_VEWAT); 				
				if(tblEXPDL.getValueAt(tblEXPDL.getSelectedRow(),TB1_EMPNO).toString().length()==4 )
					{dspVEWAT(tblEXPDL,tblEXPDL.getSelectedRow());}
				/*else if(tblAUEXP.getValueAt(tblAUEXP.getSelectedRow(),TB1_EMPNO).toString().length()==4)
				{
					System.out.println("inside mouse");
					dspVEWAT(tblAUEXP,tblAUEXP.getSelectedRow());
				}*/
			}
			else if (M_objSOURC==txtAOTTM)
					tblEXPDL.setValueAt(new Boolean(true),tblEXPDL.getSelectedRow(),TB1_CHKFL);
			
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}	
	/**Method to view Previous,Current & Next day In/Out time**/
	private void dspVEWAT(cl_JTable LP_TBL,int LP_ROWID)
	{
		//int TB2_CHKFL = 0;
		//int TB2_WRKDT = 1;
		//int TB2_INCTM = 2;
		//int TB2_OUTTM = 3;
		
		int TB2_CHKFL = 0;
		int TB2_WRKDT = 1;
		int TB2_SRLNO = 2;
		int TB2_INCTM = 3;
		int TB2_INCDL = 4;
		int TB2_OUTTM = 5;
		int TB2_OUTDL = 6;

		try
		{
			/*if(pnlVEWAT==null)
			{
				pnlVEWAT=new JPanel(null);
				String[] L_staCOLHD = {"","Date","In Time","Out Time"};
				int[] L_inaCOLSZ = {10,80,110,110};
				add(lblEMPNM1 = new JLabel(""),1,1,1,4,pnlVEWAT,'L');
				tblVEWAT = crtTBLPNL1(pnlVEWAT,L_staCOLHD,10,2,1,7,3,L_inaCOLSZ,new int[]{0});
			}*/
			if(pnlVEWAT==null)
			{
				pnlVEWAT=new JPanel(null);
				String[] L_staCOLHD = {"Chk","Date","S.No.","In Time","X","Out Time","X"};
				int[] L_inaCOLSZ = {20,80,30,100,20,100,20};
				add(lblEMPNM1 = new JLabel(""),1,1,1,4,pnlVEWAT,'L');
				tblVEWAT = crtTBLPNL1(pnlVEWAT,L_staCOLHD,50,2,1,7,10,L_inaCOLSZ,new int[]{0,4,6});
			}
			setWRKDTX(LP_TBL.getValueAt(LP_TBL.getSelectedRow(),TB2_WRKDT).toString());
			
			M_strSQLQRY = "select swt_wrkdt,swt_inctm,swt_outtm,ifNull(swt_incfl,'') swt_incfl,ifNull(swt_outfl,'') swt_outfl from hr_swtrn where  SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY+= " and swt_wrkdt between '"+M_fmtDBDAT.format(datWRKDT0)+"' and '"+M_fmtDBDAT.format(datWRKDT2)+"' and swt_stsfl not in ('X') and SWT_EMPNO = '"+LP_TBL.getValueAt(LP_ROWID,TB1_EMPNO).toString()+"'";
			M_strSQLQRY+= " order by swt_wrkdt ";
			//System.out.println("VEWAT"+M_strSQLQRY);
			
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//java.sql.Timestamp L_tmsINCTM = null,L_tmsOUTTM = null;
			
			tblVEWAT.clrTABLE();
			inlTBLEDIT(tblVEWAT);
			int i =0;
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				while (true)
				{
						tblVEWAT.setValueAt(M_fmtLCDAT.format(L_rstRSSET.getDate("SWT_WRKDT")),i,TB2_WRKDT);
						
						if(L_rstRSSET.getTimestamp("SWT_INCTM")!=null)
						{
							tblVEWAT.setValueAt(fmtLCDTM_L.format(L_rstRSSET.getTimestamp("SWT_INCTM")) ,i,TB2_INCTM);
							if(L_rstRSSET.getString("SWT_INCFL").equals("X"))
								tblVEWAT.setValueAt(new Boolean(true),i,TB2_INCDL);
						}
						if(L_rstRSSET.getTimestamp("SWT_OUTTM")!=null)
						{
							tblVEWAT.setValueAt(fmtLCDTM_L.format(L_rstRSSET.getTimestamp("SWT_OUTTM")) ,i,TB2_OUTTM);
							if(L_rstRSSET.getString("SWT_OUTFL").equals("X"))
								tblVEWAT.setValueAt(new Boolean(true),i,TB2_OUTDL);
						}
						if(!L_rstRSSET.next())
							break;
						i++;
						
				}
				L_rstRSSET.close();
			}
			lblEMPNM1.setText(LP_TBL.getValueAt(LP_ROWID,TB1_EMPNO).toString());
			pnlVEWAT.setSize(200,100);
			pnlVEWAT.setPreferredSize(new Dimension(500,200));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVEWAT,"Punching details",JOptionPane.OK_CANCEL_OPTION);
			if(L_intOPTN == 0)
			{
				String strWHRSTR="";
				for(i=0;i<tblVEWAT.getRowCount();i++)
				{
					if(tblVEWAT.getValueAt(i,TB2_INCTM).toString().length()>0)
					{
						strWHRSTR =" SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SWT_EMPNO='"+LP_TBL.getValueAt(LP_ROWID,TB1_EMPNO).toString()+"'";
						strWHRSTR+=" and SWT_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblVEWAT.getValueAt(i,TB2_WRKDT).toString()))+"'";
						M_strSQLQRY = " Update HR_SWTRN set";
    					if(tblVEWAT.getValueAt(i,TB2_INCDL).toString().equals("true"))
    						M_strSQLQRY +=" SWT_INCFL='X'";
						else
							M_strSQLQRY +=" SWT_INCFL=''";
						M_strSQLQRY +=" where "+strWHRSTR;
						M_strSQLQRY +=" and SWT_INCTM='"+M_fmtDBDTM.format(fmtLCDTM_L.parse(tblVEWAT.getValueAt(i,TB2_INCTM).toString()))+"'";
						//System.out.println("M_strSQLQRY1>>"+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
					if(tblVEWAT.getValueAt(i,TB2_OUTTM).toString().length()>0)
					{
						strWHRSTR =" SWT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SWT_EMPNO='"+LP_TBL.getValueAt(LP_ROWID,TB1_EMPNO).toString()+"'";
						strWHRSTR+=" and SWT_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblVEWAT.getValueAt(i,TB2_WRKDT).toString()))+"'";					
						M_strSQLQRY = " Update HR_SWTRN set";
    					if(tblVEWAT.getValueAt(i,TB2_OUTDL).toString().equals("true"))
							M_strSQLQRY +=" SWT_OUTFL='X'";
						else
							M_strSQLQRY +=" SWT_OUTFL=''";
						M_strSQLQRY +=" where "+strWHRSTR;
						M_strSQLQRY +=" and SWT_OUTTM='"+M_fmtDBDTM.format(fmtLCDTM_L.parse(tblVEWAT.getValueAt(i,TB2_OUTTM).toString()))+"'";
						//System.out.println("M_strSQLQRY2>>"+M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
					}
				}
				if(cl_dat.exeDBCMT("exeSAVE"))
					setMSG("",'N');
				else
				    setMSG("Error in updating data..",'E');
			}
		}

		catch (Exception L_EX)
		{
			//L_EX.printStackTrace();
			setMSG("Error in dspVEWAT : "+L_EX,'E');
		}
	}
	
	
	void setWRKDTX(String LP_WRKDT)
	{
		try
		{
			datWRKDT1 = M_fmtLCDAT.parse(LP_WRKDT);
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,-1);    
			datWRKDT0 = M_calLOCAL.getTime();
			
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,+1);    
			datWRKDT2 = M_calLOCAL.getTime();
			
			/*M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,+2);
			datWRKDT3 = M_calLOCAL.getTime();*/
		}
		catch(Exception L_EX){
			setMSG(L_EX,"setWRKDTX");
		}
	}
	
	
	/**Method to display unlinked Exit Pass**/
	private void dspULEXP()
	{
		
		 int TB3_CHKFL = 0;
		 int TB3_WRKDT = 1;
		 int TB3_EMPNO = 2;
		 int TB3_DOCNO = 3;
		 int TB3_OFPFL = 4;
		 int TB3_AUTBY = 5;
		 int TB3_WRKSH = 6;
		 int TB3_OUTTM = 7;
		 int TB3_INCTM = 8;
		
		try
		{
			if(txtFMDAT.getText().trim().length() ==0)
	    	{
				txtFMDAT.requestFocus();
	    		setMSG("Enter the From Date",'E');
	    		return ;
	    	}
			else if(txtTODAT.getText().trim().length() ==0)
	    	{
				txtTODAT.requestFocus();
	    		setMSG("Enter the To Date..",'E');
	    		return ;
	    	}
			
			if(pnlULEXP==null)
			{
				pnlULEXP=new JPanel(null);
				String[] L_staCOLHD1 = {"","Date","Emp No","DOC No","Off/Per","Auth By","WrkSh","Out Time","In Time"};
				int[] L_inaCOLSZ1 = {10,75,40,80,40,45,45,70,70};
				add(lblEMPCD = new JLabel(""),1,1,1,4,pnlULEXP,'L');
				tblULEXP = crtTBLPNL1(pnlULEXP,L_staCOLHD1,100,2,1,7,4,L_inaCOLSZ1,new int[]{0});
			}
			
			M_strSQLQRY = "select EX_EMPNO,EX_DOCDT,EX_DOCNO,EX_OFPFL,EX_AUTBY,EX_SHFCD,EX_EOTTM,EX_EINTM from HR_EXTRN where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			if(txtEMPCD.getText().toString().length()>0)
				M_strSQLQRY +=" and EX_EMPNO='"+txtEMPCD.getText().toString()+"'";
			M_strSQLQRY+= " and EX_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"'AND'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
			M_strSQLQRY +=" and EX_STSFL= '2'order by EX_DOCDT";
			//System.out.println("ULEXP"+M_strSQLQRY);
			ResultSet L_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);
			tblULEXP.clrTABLE();
			inlTBLEDIT(tblULEXP);
			int i =0;
			if(L_rstRSSET1 != null)
			{
				while (L_rstRSSET1.next())
				{
					if(L_rstRSSET1.getDate("EX_DOCDT")!=null)
						tblULEXP.setValueAt(M_fmtLCDAT.format(L_rstRSSET1.getDate("EX_DOCDT")),i,TB3_WRKDT);
					tblULEXP.setValueAt(nvlSTRVL(L_rstRSSET1.getString("EX_EMPNO"),""),i,TB3_EMPNO);
					tblULEXP.setValueAt(nvlSTRVL(L_rstRSSET1.getString("EX_DOCNO"),""),i,TB3_DOCNO);
					tblULEXP.setValueAt(nvlSTRVL(L_rstRSSET1.getString("EX_OFPFL"),""),i,TB3_OFPFL);
					tblULEXP.setValueAt(nvlSTRVL(L_rstRSSET1.getString("EX_AUTBY"),""),i,TB3_AUTBY);
					tblULEXP.setValueAt(nvlSTRVL(L_rstRSSET1.getString("EX_SHFCD"),""),i,TB3_WRKSH);
					if(L_rstRSSET1.getTimestamp("EX_EINTM")!=null)
						tblULEXP.setValueAt(M_fmtLCDTM.format(L_rstRSSET1.getTimestamp("EX_EINTM")).substring(11) ,i,TB3_INCTM);
					if(L_rstRSSET1.getTimestamp("EX_EOTTM")!=null)
						tblULEXP.setValueAt(M_fmtLCDTM.format(L_rstRSSET1.getTimestamp("EX_EOTTM")).substring(11) ,i,TB3_OUTTM);
				
						i++;	
				}
				
				L_rstRSSET1.close();
			}
			
			if(txtEMPCD.getText().toString().length()>0)
				lblEMPCD.setText(txtEMPCD.getText().toString());
			pnlULEXP.setSize(300,200);
			pnlULEXP.setPreferredSize(new Dimension(600,300));
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlULEXP,"UnLinked Exit Pass",JOptionPane.OK_CANCEL_OPTION);
			if( L_intOPTN==0 || L_intOPTN==2 || L_intOPTN==-1)
				chkULEXP.setSelected(false);
		
		}
		catch (Exception L_EX)
		{
			//L_EX.printStackTrace();
			setMSG("Error in dspULEXP : "+L_EX,'E');
		}
	}
	
	
	
	/**Method to auto generation of Exit Pass**/
	private void exeEXTPS()
	{
		try
		{
			if(!vldLKEXP())
				return;
			
    		/*hstEXTPS = new Hashtable<String,String>();
			String L_strSQLQRY= " Select EX_EMPNO,EX_DOCDT,EX_DOCNO,EX_SECTM,EX_OFPFL,EX_AUTBY from HR_EXTRN where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			L_strSQLQRY += " and EX_STSFL= '2' and EX_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
			ResultSet L_rstRSSET= cl_dat.exeSQLQRY1(L_strSQLQRY);
			System.out.println("L_strSQLQRY>>"+L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					hstEXTPS.put(nvlSTRVL(L_rstRSSET.getString("EX_EMPNO"),"")+"|"+M_fmtLCDAT.format(L_rstRSSET.getDate("EX_DOCDT")),L_rstRSSET.getTimestamp("EX_SECTM")==null ? "" : M_fmtLCDTM.format(L_rstRSSET.getTimestamp("EX_SECTM")).substring(11)
							+"!"+nvlSTRVL(L_rstRSSET.getString("EX_DOCNO"),"")+"!"+nvlSTRVL(L_rstRSSET.getString("EX_OFPFL"),"")+"!"+nvlSTRVL(L_rstRSSET.getString("EX_AUTBY"),""));	
					
				}
				L_rstRSSET.close();
				//System.out.println("hstEXTPS"+hstEXTPS);
			}	
			for(int i=0;i<tblEXPDL.getRowCount();i++)
			{
				if(tblEXPDL.getValueAt(i,TB1_EMPNO).toString().length()>0 && tblEXPDL.getValueAt(i,TB1_AOTTM).toString().length()>0)
				{
					if(hstEXTPS.containsKey(tblEXPDL.getValueAt(i,TB1_EMPNO)+"|"+tblEXPDL.getValueAt(i,TB1_WRKDT)))
					{
						String[] L_strEXTPS=hstEXTPS.get(tblEXPDL.getValueAt(i,TB1_EMPNO)+"|"+tblEXPDL.getValueAt(i,TB1_WRKDT)).split("!");
						String L_strSUBTM="";
						if(tblEXPDL.getValueAt(i,TB1_AOTTM).toString().compareTo(L_strEXTPS[0]) < 0)
							L_strSUBTM=subTIME(L_strEXTPS[0],tblEXPDL.getValueAt(i,TB1_AOTTM).toString());
						else if(tblEXPDL.getValueAt(i,TB1_AOTTM).toString().compareTo(L_strEXTPS[0]) > 0)
							L_strSUBTM=subTIME(tblEXPDL.getValueAt(i,TB1_AOTTM).toString(),L_strEXTPS[0]);
						if(L_strEXTPS[0].length()>0)
						if(L_strEXTPS[0].equals(tblEXPDL.getValueAt(i,TB1_AOTTM).toString()) || L_strSUBTM.compareTo("00:10")<=0)
						{
							tblEXPDL.setValueAt(L_strEXTPS[0],i,TB1_SECTM);
							tblEXPDL.setValueAt(L_strEXTPS[1].toString(),i,TB1_DOCNO);
							tblEXPDL.setValueAt(L_strEXTPS[2].toString(),i,TB1_OFPFL);
							tblEXPDL.setValueAt(L_strEXTPS[3].toString(),i,TB1_AUTBY);
							tblEXPDL.setValueAt(new Boolean(true),i,TB1_CHKFL);	
						}
					}
				}
			}*/
			
			for(int i=0;i<tblEXPDL.getRowCount();i++)
			{
				if(tblEXPDL.getValueAt(i,TB1_WRKDT).toString().length()>0 && tblEXPDL.getValueAt(i,TB1_EMPNO).toString().length()>0)
				{
					for(int j=0;j<vtrSWTOT.size();j++)
					{
						String strVTRVL = vtrSWTOT.get(j).substring(0,15);
						String strTBLVL = tblEXPDL.getValueAt(i,TB1_WRKDT).toString()+"|"+tblEXPDL.getValueAt(i,TB1_EMPNO).toString();
						String L_strSUBTM = "";
						if(strVTRVL.equals(strTBLVL))	
						{
							//System.out.println(strVTRVL+"<<>>"+strTBLVL);
							String strVTRVL_TM = vtrSWTOT.get(j).substring(16,21);
							String strTBLVL_TM = tblEXPDL.getValueAt(i,TB1_SECTM).toString();
							if(fmtHHMM.parse(strVTRVL_TM).compareTo(fmtHHMM.parse(strTBLVL_TM))>=0)
								L_strSUBTM=subTIME(strVTRVL_TM,strTBLVL_TM);
							else if(fmtHHMM.parse(strVTRVL_TM).compareTo(fmtHHMM.parse(strTBLVL_TM))<=0)
								L_strSUBTM=subTIME(strTBLVL_TM,strVTRVL_TM);
							if(fmtHHMM.parse(L_strSUBTM).compareTo(fmtHHMM.parse("00:10"))<=0)
							{
								tblEXPDL.setValueAt(strVTRVL_TM,i,TB1_AOTTM);
								for(int l=0;l<vtrSWTIN.size();l++)
								{
									String strVTRVL1 = vtrSWTIN.get(l).substring(0,15);
									String strTBLVL1 = tblEXPDL.getValueAt(i,TB1_WRKDT).toString()+"|"+tblEXPDL.getValueAt(i,TB1_EMPNO).toString();
									//System.out.println(strVTRVL+"<<>>"+strTBLVL);
									if(strVTRVL1.equals(strTBLVL1))	
									{
										//System.out.println(strVTRVL1+"<<>>"+strTBLVL1);
										String strVTRVL_TM1 = vtrSWTIN.get(l).substring(16,21);
										String strTBLVL_TM1 = strVTRVL_TM;
										String strTBLVL_STD = tblEXPDL.getValueAt(i,TB1_OUTTM_S).toString();
										if(fmtHHMM.parse(strVTRVL_TM1).compareTo(fmtHHMM.parse(strTBLVL_TM1))>=0
										   && fmtHHMM.parse(strVTRVL_TM1).compareTo(fmtHHMM.parse(strTBLVL_STD))<=0)
										{
											tblEXPDL.setValueAt(strVTRVL_TM1,i,TB1_AINTM);
										}
									}
								}
								setEXTIME(i);
								tblEXPDL.setValueAt(new Boolean(true),i,TB1_CHKFL);	
								if((fmtHHMM.parse(strVTRVL_TM).compareTo(fmtHHMM.parse(tblEXPDL.getValueAt(i,TB1_OUTTM_S).toString())))>0
								|| (fmtHHMM.parse(strVTRVL_TM).compareTo(fmtHHMM.parse(tblEXPDL.getValueAt(i,TB1_INCTM_S).toString())))<0)
								{
									tblEXPDL.setValueAt(new Boolean(true),i,TB1_OTGFL);	
								}
							}
						}
					}
					/*if(hstEXTPS.containsKey(tblEXPDL.getValueAt(i,TB1_EMPNO)+"|"+tblEXPDL.getValueAt(i,TB1_WRKDT)))
					{
						String[] L_strEXTPS=hstEXTPS.get(tblEXPDL.getValueAt(i,TB1_EMPNO)+"|"+tblEXPDL.getValueAt(i,TB1_WRKDT)).split("!");
						String L_strSUBTM="";
						if(tblEXPDL.getValueAt(i,TB1_AOTTM).toString().compareTo(L_strEXTPS[0]) < 0)
							L_strSUBTM=subTIME(L_strEXTPS[0],tblEXPDL.getValueAt(i,TB1_AOTTM).toString());
						else if(tblEXPDL.getValueAt(i,TB1_AOTTM).toString().compareTo(L_strEXTPS[0]) > 0)
							L_strSUBTM=subTIME(tblEXPDL.getValueAt(i,TB1_AOTTM).toString(),L_strEXTPS[0]);
						if(L_strEXTPS[0].length()>0)
						if(L_strEXTPS[0].equals(tblEXPDL.getValueAt(i,TB1_AOTTM).toString()) || L_strSUBTM.compareTo("00:10")<=0)
						{
							tblEXPDL.setValueAt(L_strEXTPS[0],i,TB1_SECTM);
							tblEXPDL.setValueAt(L_strEXTPS[1].toString(),i,TB1_DOCNO);
							tblEXPDL.setValueAt(L_strEXTPS[2].toString(),i,TB1_OFPFL);
							tblEXPDL.setValueAt(L_strEXTPS[3].toString(),i,TB1_AUTBY);
							tblEXPDL.setValueAt(new Boolean(true),i,TB1_CHKFL);	
						}
					}*/
				}
			}
			
		}
		catch (Exception L_EX)
		{
			setMSG("Error in exeEXTPS : "+L_EX,'E');
		}
	}
	
	/**validation for link Exit Pass **/
	private boolean vldLKEXP()
	{
		int L_intOPTN;
		
		if(chkATCOF.isSelected())
		{
			L_intOPTN=JOptionPane.showConfirmDialog( this,"Do You Want To Link Exit Pass between "+txtFMDAT.getText()+" and "+txtTODAT.getText()+" ?","Exit Pass Link Validation",JOptionPane.OK_CANCEL_OPTION);
			if(L_intOPTN==2 || L_intOPTN==-1)
				return false;
			
			return true;
		}
		return true;
	}
	
	/**Method to verify EMP NO From Date & To Date**/
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;
				if(input == txtFMDAT)
				{	
				   /*if(txtFMDAT.getText().toString().length()== 0)
					{
						setMSG("Enter From Date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}*/
				  	if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("From Date Should Not Be Grater Than Todays Date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
				}	
				if(input == txtTODAT)
				{
					/*if(txtTODAT.getText().trim().length() == 0)
					{
						setMSG("Enter To Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}*/
					if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("To Date Should Not Be Grater Than Todays Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
					if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
					{
						setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
						txtTODAT.requestFocus();
						return false;
					}
				}
				if(input == txtEMPCD)
				{
					M_strSQLQRY = " select EP_EMPNO,EP_LSTNM||' '||EP_FSTNM||' '||EP_MDLNM EP_EMPNM,EP_DPTNM,EP_DPTCD from HR_EPMST where  ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null";
					M_strSQLQRY+=" and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";				
					M_strSQLQRY += " AND EP_EMPNO ='"+txtEMPCD.getText().trim()+"'";
					
					//System.out.println("INPVF EMPNO : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))	
							getDATA();
					}	
					else
					{
						setMSG("Enter Valid Employee Code",'E');
						return false;
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
	
	/**class to verify DOC No & Work Shift**/
	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{	
			try
			{
				if(getSource()==tblEXPDL)
			    {
					if(tblEXPDL.getValueAt(P_intROWID,P_intCOLID).toString().length()== 0)
						return true;
					
					if(P_intCOLID == TB1_AOTTM || P_intCOLID == TB1_AINTM)
					{
						setEXTIME(P_intROWID);
					}
					
					/*if(P_intCOLID == TB1_DOCNO)
    			    {
						
						M_strSQLQRY = "select EX_DOCNO,EX_OFPFL,EX_AUTBY,EX_SHFCD,EX_EOTTM,EX_EINTM,EX_SECTM from HR_EXTRN where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'and EX_STSFL = '2'";
						M_strSQLQRY += " and EX_EMPNO='"+tblEXPDL.getValueAt(P_intROWID,TB1_EMPNO)+"'";
	    				M_strSQLQRY += " and EX_DOCDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEXPDL.getValueAt(P_intROWID,TB1_WRKDT).toString()))+"'";
	    				M_strSQLQRY += " and EX_DOCNO='"+tblEXPDL.getValueAt(P_intROWID,TB1_DOCNO)+"'";
	    			//	System.out.println("INPVF DOCNO : "+M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							tblEXPDL.setValueAt(new Boolean(true),P_intROWID,TB1_CHKFL);
							tblEXPDL.setValueAt(M_rstRSSET.getString("EX_OFPFL"),P_intROWID,TB1_OFPFL);
     					    tblEXPDL.setValueAt(M_rstRSSET.getString("EX_AUTBY"),P_intROWID,TB1_AUTBY);
							if(tblEXPDL.getValueAt(P_intROWID,TB1_AOTTM).toString().equals(""))
								if(M_rstRSSET.getString("EX_EOTTM")!=null)
									tblEXPDL.setValueAt(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("EX_EOTTM")).substring(11,16),P_intROWID,TB1_AOTTM);  
							if(tblEXPDL.getValueAt(P_intROWID,TB1_AINTM).toString().equals(""))
								if(M_rstRSSET.getString("EX_EINTM")!=null)
									tblEXPDL.setValueAt(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("EX_EINTM")).substring(11,16),P_intROWID,TB1_AINTM);  
							if(M_rstRSSET.getString("EX_SECTM")!=null)
								tblEXPDL.setValueAt(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("EX_SECTM")).substring(11,16),P_intROWID,TB1_SECTM);  
				  
							setMSG("",'N');
							setEXTIME(P_intROWID);
						}	
						else
						{
							setMSG("Enter Valid DOC No.",'E');
							return false;
						}
						M_rstRSSET.close();
						
						for(int i=0;i<tblEXPDL.getRowCount();i++)
						{
							if(tblEXPDL.getValueAt(i,TB1_CHKFL).toString().equals("true"))
							{
								if(tblEXPDL.getValueAt(P_intROWID,TB1_DOCNO).toString().length()>0 && tblEXPDL.getValueAt(i,TB1_DOCNO).toString().length()>0 && i!=P_intROWID)
								if(tblEXPDL.getValueAt(P_intROWID,TB1_DOCNO).toString().compareTo(tblEXPDL.getValueAt(i,TB1_DOCNO).toString().trim())==0)
								{
									setMSG("DOC NO can not be Repeat..",'E');
									return false;
								}
							}
						}
    			    }*/
				
					if(P_intCOLID==TB1_EMPNO)
					{
							M_strSQLQRY = "select EP_EMPNO, EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM,SW_WRKSH,SW_INCST,SW_OUTST";
							M_strSQLQRY+=" from HR_EPMST left outer join HR_SWMST on SW_CMPCD=EP_CMPCD and SW_EMPNO=EP_EMPNO and SW_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEXPDL.getValueAt(P_intROWID,TB1_WRKDT).toString()))+"' ";
							M_strSQLQRY+=" where ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' ";
							M_strSQLQRY += " AND EP_EMPNO = '"+tblEXPDL.getValueAt(P_intROWID,TB1_EMPNO).toString()+"'";
							/*M_strSQLQRY = "select EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM,SW_WRKSH,SW_INCST,SW_OUTST";
							M_strSQLQRY+=" from HR_EPMST,HR_SWMST";
							M_strSQLQRY+=" where ifnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and ep_empno = sw_empno AND SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
							M_strSQLQRY+=" AND SW_EMPNO='"+tblEXPDL.getValueAt(P_intROWID,TB1_EMPNO).toString()+"' ";
							M_strSQLQRY+=" AND SW_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblEXPDL.getValueAt(P_intROWID,TB1_WRKDT).toString()))+"' AND SW_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
								*/
							//System.out.println(M_strSQLQRY);
							
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);						
		                    if(M_rstRSSET.next() && M_rstRSSET!=null)
		                    {
		                    	tblEXPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ep_empnm"),""),P_intROWID,TB1_EMPNM);
		                     	tblEXPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),P_intROWID,TB1_WRKSH);
		                     	
		                     	if(M_rstRSSET.getTimestamp("sw_incst")!=null)
		    						tblEXPDL.setValueAt(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("sw_incst")),P_intROWID,TB1_INCTM_S);
		    					if(M_rstRSSET.getTimestamp("sw_outst")!=null)
		    						tblEXPDL.setValueAt(M_fmtLCDTM.format(M_rstRSSET.getTimestamp("sw_outst")),P_intROWID,TB1_OUTTM_S);
		                     	
		                    	setMSG("",'N');
		                    }  
		                    else
							{
								setMSG("Enter valid Employee No",'E');
								return false;
							}
		                	M_rstRSSET.close();
					}
					if(P_intCOLID==TB1_AOTTM)
					{
						if(tblEXPDL.getValueAt(P_intROWID,TB1_AOTTM).toString().length()==5)
						if((fmtHHMM.parse(tblEXPDL.getValueAt(P_intROWID,TB1_AOTTM).toString()).compareTo(fmtHHMM.parse(tblEXPDL.getValueAt(P_intROWID,TB1_OUTTM_S).toString())))>0
						|| (fmtHHMM.parse(tblEXPDL.getValueAt(P_intROWID,TB1_AOTTM).toString()).compareTo(fmtHHMM.parse(tblEXPDL.getValueAt(P_intROWID,TB1_INCTM_S).toString())))<0)
						{
							tblEXPDL.setValueAt(new Boolean(true),P_intROWID,TB1_OTGFL);
						}
						else
							tblEXPDL.setValueAt(new Boolean(false),P_intROWID,TB1_OTGFL);
					}
					if(P_intCOLID==TB1_AINTM)
					{
						if(tblEXPDL.getValueAt(P_intROWID,TB1_AOTTM).toString().length()>0 && tblEXPDL.getValueAt(P_intROWID,TB1_AINTM).toString().length()>0)
						if(M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEXPDL.getValueAt(P_intROWID,TB1_WRKDT).toString().trim()+" "+tblEXPDL.getValueAt(P_intROWID,TB1_AOTTM).toString())).compareTo(M_fmtDBDTM.format(M_fmtLCDTM.parse(tblEXPDL.getValueAt(P_intROWID,TB1_WRKDT).toString().trim()+" "+tblEXPDL.getValueAt(P_intROWID,TB1_AINTM).toString())))>0)
						{
							setMSG("Actual In Time must be greater than Act Out Time ..",'E');
							return false;
						}
					}
			    }
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"class TBLINPVF");
			}
			return true;
		}
    }
	
}
	
	
	