/**System Name:Human Resource System.
 
Program Name:Off Change Notice module

Purpose : This module used for accepting & generating the Off change Notice (before given weekly off).  

Source Directory: f:\source\splerp3\hr_tesoc.java                         

Executable Directory: F:\exec\splerp3\hr_tesoc.class

Purpose: Off Change Entry.

List of tables used:
Table Name		Primary key											Operation done
															Insert	Update	   Query    Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
		CO_CMPCD,CO_EMPNO,CO_WRKDT,CO_LVECD           /	     /           /         /
                 
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on screen:
Field Name		Column Name		Table name		Type/Size		Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtEMPNO_TB		CO_EMPNO        HR_COTRN        VARCHAR(4)	    Employee No
txtWRKDT		CO_WRKDT        HR_COTRN        Date            Original weekly off           
txtREFDT		CO_REFDT        HR_COTRN        DATE,           Changed weekly off                 

-----------------------------------------------------------------------------------------------------------------------------------------------------


List of fields with help facility: 
Field Name	Display Description		    Display Columns			         Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtEMPNO_TB	Employee No,Employee NAME	EP_EMPNO,EP_EMPNM,ep_dptnm       HR_EPMTS
txtDPTCD    Dept Code,Description       CMT_CODCD,CMT_CODDS              CO_CDTRN
txtWRKDT    Original weekly off         SW_WRKDT                         HR_SWMST 
-----------------------------------------------------------------------------------------------------------------------------------------------------


Validations & Other Information:

    - Enter valid Employee No,Original weekly off shift 
  	- From Date must be Smaller Than Or Equal to To Date.
 	- To Date must not be Grater Than Todays Date.
    - To Date must be greater than Or Equal to From Date. 
    - Proposed Off Date can not be greater & less than Original Off Date.
    
Requirements:
	
	-> User will be added,updated & Deleted records of corresponding Emp No .
	-> Fetch records in table, while enter From & To Date.
	-> If user want to generate all records of Department,while enter Dept code & press to display records in table.
	-> While click on Generate button ,it will be generate Off Change Notice report of given records in table. 
	-> User can be generate report before saving of records in database table.
*/    

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.Dimension;
import javax.swing.JPanel;
public class hr_tesoc extends cl_pbase
{
	private JTextField txtDPTCD,txtEMPNO;
	//private JTextField txtMONTH,txtYEAR;
	private JTextField txtSTRDT,txtENDDT;
	
	private JLabel lblDPTNM,lblEMPNM,lblSTRDT,lblENDDT,lblMONTH;
	
	private cl_JTable tblWEKOF,tblCOTRN;
	JPanel pnlCOTRN;
	private JButton btnGENRP,btnRUN;

	private INPVF oINPVF;
	private TBLINPVF objTBLVRF;
	
	/**column for  JTable**/
	private final int TB1_CHKFL =0;     JCheckBox chkCHKFL;
	private final int TB1_EMPNO =1;     JTextField txtEMPNO_TB;
	private final int TB1_EMPNM =2;
	private final int TB1_DPTNM =3;
	private final int TB1_WRKDT =4;     JTextField txtWRKDT;
	private final int TB1_REFDT =5;     JTextField txtREFDT;
	private final int TB1_AUTHC =6;     JCheckBox chkAUTHC;
	
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpsoc.html";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
    
    private Vector<String> vtrWRKDT;
    boolean flgALLDPT;
	
    String M_strADDRS="",M_strSTRDT="",M_strENDDT="",M_strREFDT="",M_strREFDT1="";
    int L_intROWNO=0;
	hr_tesoc()
	{
		super(1);
		setMatrix(20,20);
		flgALLDPT = false;
		if(cl_dat.M_strUSRCD_pbst.equals("SKS") || cl_dat.M_strUSRCD_pbst.equals("RSL") || cl_dat.M_strUSRCD_pbst.equals("PDP") || cl_dat.M_strUSRCD_pbst.equals("RPN") || cl_dat.M_strUSRCD_pbst.equals("MMN"))
			flgALLDPT = true;
		try
		{
			
			add(new JLabel("Department"),2,2,1,2,this,'L');
			add(txtDPTCD = new TxtLimit(3),2,4,1,2,this,'L');
			add(lblDPTNM = new JLabel(),2,6,1,2,this,'L');
			
			add(new JLabel("Employee No."),2,8,1,2,this,'L');
			add(txtEMPNO = new TxtLimit(4),2,10,1,2,this,'L');
			add(lblEMPNM = new JLabel(),2,12,1,2,this,'L');

			//add(lblMONTH=new JLabel("Month"),2,10,1,2,this,'L');
			//add(txtMONTH = new TxtNumLimit(2),2,12,1,1,this,'L');
			//add(txtYEAR = new TxtNumLimit(4),2,13,1,2,this,'L');
			
			add(lblSTRDT=new JLabel("Form Date"),3,2,1,2,this,'L');
			add(txtSTRDT= new TxtDate(),3,4,1,2,this,'L');
			
			add(lblENDDT=new JLabel("To Date"),3,8,1,2,this,'L');
			add(txtENDDT = new TxtDate(),3,10,1,2,this,'L');
			
      		add(btnRUN=new JButton("DISPLAY"),4,6,1,2,this,'L');
			add(btnGENRP= new JButton("Gen.Rep"),4,12,1,2,this,'L');
			
			String[] L_strCOLHD = {"","Emp No","Emp Name","Dept","Original Weekly Off","Proposed Weekly Off","Auth"};
      		int[] L_intCOLSZ= {20,70,150,70,120,120,40};	    				
      		tblWEKOF = crtTBLPNL1(this,L_strCOLHD,150,6,2,8,15,L_intCOLSZ,new int[]{0,6});
      		
      		tblWEKOF.addKeyListener(this);
			tblWEKOF.addFocusListener(this);
			
      		tblWEKOF.setCellEditor(TB1_CHKFL,chkCHKFL=new JCheckBox());
      		tblWEKOF.setCellEditor(TB1_EMPNO,txtEMPNO_TB = new TxtLimit(4));
      		tblWEKOF.setCellEditor(TB1_WRKDT,txtWRKDT = new TxtDate());
      		tblWEKOF.setCellEditor(TB1_REFDT,txtREFDT = new TxtDate());
			tblWEKOF.setCellEditor(TB1_AUTHC,chkAUTHC = new JCheckBox());
			chkAUTHC.addFocusListener(this);
			
       		txtEMPNO_TB.addKeyListener(this);
    		txtWRKDT.addKeyListener(this);
    		txtREFDT.addKeyListener(this);
    		txtEMPNO_TB.addFocusListener(this);
    		txtWRKDT.addFocusListener(this);
    		chkCHKFL.addFocusListener(this);
    		btnRUN.addKeyListener(this);
    		((JCheckBox) tblWEKOF.cmpEDITR[TB1_AUTHC]).addMouseListener(this);
			
			objTBLVRF = new TBLINPVF();
			tblWEKOF.setInputVerifier(objTBLVRF);
			
			((JTextField)tblWEKOF.cmpEDITR[TB1_EMPNM]).setEditable(false);
			((JTextField)tblWEKOF.cmpEDITR[TB1_DPTNM]).setEditable(false);
			
			
			oINPVF=new INPVF();
			txtDPTCD.setInputVerifier(oINPVF);
			txtEMPNO.setInputVerifier(oINPVF);
			//txtMONTH.setInputVerifier(oINPVF);
			//txtYEAR.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			
			setENBL(false);
			/*txtSTRDT.setVisible(false);
			txtENDDT.setVisible(false);
			lblSTRDT.setVisible(false);
			lblENDDT.setVisible(false);
			*/
			vtrWRKDT = new Vector<String>();
			
			/**M_strADDRS String Varible to store Company address**/
			M_strSQLQRY =" Select CP_ADR01,CP_ADR02,CP_ADR03,CP_ADR04,CP_PINCD,CP_CTYNM from CO_CPMST ";
			M_strSQLQRY+=" where CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>>M_strSQLQRY>>>>"+M_strSQLQRY);	
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				 M_strADDRS=M_rstRSSET.getString("CP_ADR01")+" "+M_rstRSSET.getString("CP_ADR02")+" "+M_rstRSSET.getString("CP_ADR03")+" "+M_rstRSSET.getString("CP_ADR04")+" "+M_rstRSSET.getString("CP_CTYNM")+" "+M_rstRSSET.getString("CP_PINCD");
				//System.out.println("M_strADDRS>>>>"+M_strADDRS);
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}

	public void mouseReleased(MouseEvent L_ME)
	{
		try
		{
			if(M_objSOURC==chkAUTHC)
			{	
				if(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_WRKDT).toString().length()==0 ||				   tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_REFDT).toString().length()==0)
				{
					setMSG("Please Enter weekly off date and Proposed weekly off date",'E');
					tblWEKOF.setValueAt(new Boolean(false),tblWEKOF.getSelectedRow(),TB1_CHKFL); 
					tblWEKOF.setValueAt(new Boolean(false),tblWEKOF.getSelectedRow(),TB1_AUTHC); 					return;
				}				if(((JCheckBox) tblWEKOF.cmpEDITR[TB1_AUTHC]).isSelected())
				{						tblWEKOF.setValueAt(new Boolean(true),tblWEKOF.getSelectedRow(),TB1_CHKFL); 
				}
				else if(!((JCheckBox) tblWEKOF.cmpEDITR[TB1_AUTHC]).isSelected())
				{
					tblWEKOF.setValueAt(new Boolean(false),tblWEKOF.getSelectedRow(),TB1_CHKFL); 
				}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"MouseReleased ");
		}
	}	
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				tblWEKOF.clrTABLE();
				inlTBLEDIT(tblWEKOF);
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					
					//txtMONTH.setText(cl_dat.M_strLOGDT_pbst.substring(3,5));
					//txtYEAR.setText(cl_dat.M_strLOGDT_pbst.substring(6));
					
					txtSTRDT.setText("01/"+cl_dat.M_strLOGDT_pbst.substring(3,10));
					txtENDDT.setText(cl_dat.M_strLOGDT_pbst);
					
					setENBL(true);
					btnGENRP.setEnabled(false);
					/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						txtYEAR.requestFocus();
						txtMONTH.setVisible(true);
						txtYEAR.setVisible(true);
						lblMONTH.setVisible(true);
						
						txtSTRDT.setVisible(false);
						txtENDDT.setVisible(false);
						lblSTRDT.setVisible(false);
						lblENDDT.setVisible(false);
					}
					else
					{
						txtENDDT.requestFocus();
						txtMONTH.setVisible(false);
						txtYEAR.setVisible(false);
						lblMONTH.setVisible(false);
						
						txtSTRDT.setVisible(true);
						txtENDDT.setVisible(true);
						lblSTRDT.setVisible(true);
						lblENDDT.setVisible(true);
					}*/
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						tblWEKOF.cmpEDITR[TB1_WRKDT].setEnabled(false);
						tblWEKOF.cmpEDITR[TB1_REFDT].setEnabled(false);
					}
					else
					{
						tblWEKOF.cmpEDITR[TB1_WRKDT].setEnabled(true);
						tblWEKOF.cmpEDITR[TB1_REFDT].setEnabled(true);
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						btnGENRP.setEnabled(true);
						txtDPTCD.setText("");
						txtEMPNO.setText("");
						lblDPTNM.setText("");
						lblEMPNM.setText("");
						tblWEKOF.clrTABLE();
						inlTBLEDIT(tblWEKOF);
					}
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC ==btnGENRP)
			{
				if(!vldDATA()) 
				{
					return;
				}
				genRPTFL();
			}
			else if(M_objSOURC == txtENDDT)
			{
				//if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && txtSTRDT.getText().length()>0 && txtENDDT.getText().length()>0)
				//	getDATA();
				btnRUN.requestFocus();	
			}
			else if(M_objSOURC == btnRUN)   //// button verifies data entered
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
				else getDATA();
			}
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");
			if(txtEMPNO.getText().length()==0)
				lblEMPNM.setText("");
		}
		catch(Exception e)
		{
			setMSG(e,"error in action performed");
		}
	}

	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{	
			if(M_objSOURC == txtEMPNO_TB)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					if(tblWEKOF.getSelectedRow()>=L_intROWNO)
					{
					  ((JCheckBox)tblWEKOF.cmpEDITR[TB1_CHKFL]).setEnabled(false);
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_EMPNO]).setEditable(false);
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_WRKDT]).setEditable(false);
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_REFDT]).setEditable(false);
					}
					else
					{
					  ((JCheckBox)tblWEKOF.cmpEDITR[TB1_CHKFL]).setEnabled(true);
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_EMPNO]).setEditable(true);
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_WRKDT]).setEditable(true); 
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_REFDT]).setEditable(true); 
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					 ((JCheckBox)tblWEKOF.cmpEDITR[TB1_CHKFL]).setEnabled(true);
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_EMPNO]).setEditable(true);
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_WRKDT]).setEditable(true); 
					  ((JTextField)tblWEKOF.cmpEDITR[TB1_REFDT]).setEditable(true); 
				}
			}
			/*else if(M_objSOURC == txtWRKDT)
			{
				if(txtMONTH.getText().length()>0 && txtYEAR.getText().length()>0)
				{
					M_strSTRDT="01/"+txtMONTH.getText()+"/"+txtYEAR.getText();
					M_calLOCAL.setTime(M_fmtLCDAT.parse(M_strSTRDT)); 
					int M_intLSTDT = M_calLOCAL.getActualMaximum(Calendar.DATE);
					M_strENDDT=M_intLSTDT+"/"+txtMONTH.getText()+"/"+txtYEAR.getText();
				}
			}*/
		}
		catch(Exception E)
		{
			setMSG("FocusGained",'E');                            
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{
				if(M_objSOURC==txtDPTCD)		
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
					//M_strSQLQRY=" Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
					//M_strSQLQRY+=" and CMT_STSFL <> 'X'";
		   		    M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "+ (flgALLDPT ? "" : " and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select substr(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR01LRC','HR01LSN') and substr(cmt_codcd,6,4)='"+cl_dat.M_strEMPNO_pbst+"') and substr(EP_HRSBS,1,2)='"+M_strSBSCD.substring(0,2)+"')");
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				if(M_objSOURC==txtEMPNO)		
        		{
					if(!flgALLDPT && txtDPTCD.getText().length()==0)
					{
						setMSG("Please Enter Department Code first...",'E');
						txtDPTCD.requestFocus();
						txtEMPNO.setText("");
						return;
					}
						
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
        			String L_ARRHDR[] = {"No","Name"};
        			M_strSQLQRY = "select distinct EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U'";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY += " and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
					M_strSQLQRY += " order by EP_EMPNO";
					//System.out.println(">>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
				//help for Employee Category
				else if(M_objSOURC==txtEMPNO_TB)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO_TB";
        			String L_ARRHDR[] = {"Emo No","Emp Name","Dept"};
        			M_strSQLQRY = "select distinct EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM,EP_DPTNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U'";
					if(txtEMPNO.getText().length()>0)
						M_strSQLQRY += " and EP_EMPNO = '"+txtEMPNO.getText().trim()+"'";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY += " and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
					M_strSQLQRY += " order by EP_EMPNO";
					//System.out.println(">>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,3,"CT");
        		}
				else if(M_objSOURC==txtWRKDT)		
        		{
					cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtWRKDT";
        			String L_ARRHDR[] = {"Original Weekly Off Date"};
        			M_strSQLQRY = "select sw_wrkdt from hr_swmst where sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_empno='"+tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_EMPNO)+"' and sw_lvecd='WO'";
        			//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && txtMONTH.getText().length()>0 && txtYEAR.getText().length()>0 )
        			//	M_strSQLQRY +=" and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strENDDT))+"'";
        			if(txtSTRDT.getText().length()>0 && txtENDDT.getText().length()>0 )
        				M_strSQLQRY +=" and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
        			M_strSQLQRY += " order by sw_wrkdt";
					//System.out.println(">>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
        		}
				else if(M_objSOURC==txtREFDT)		
        		{
					// pending OFF change or work on PH tracking
					// Auto-linking of Off change 
					//*****************************************************************************************
					dspCOTRN();
        		}
			}
			catch(Exception L_NPE)
			{
				setMSG("error in F1",'E');                            
			}
		}
		
		if (L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
			try
			{		
				if(M_objSOURC == txtDPTCD)
					txtEMPNO.requestFocus();
				if(M_objSOURC == txtEMPNO)
					txtSTRDT.requestFocus();
				/*else if(M_objSOURC == txtMONTH)
				{
					txtYEAR.requestFocus();	
				}
				else if(M_objSOURC == txtYEAR)
				{
					tblWEKOF.requestFocus();	
					setMSG("Enter Employee No or Press F1 to Select form List..",'N');
				}*/
				else if(M_objSOURC == txtSTRDT)
				{
					txtENDDT.requestFocus();
					setMSG("Enter To Date & Press Enter Key to Display Records in table..",'N');
				}
				else if(M_objSOURC == txtENDDT)
				{
					btnRUN.requestFocus();
				}
				
			}
			catch(Exception L_NPE)
			{
				setMSG("error in ENTER",'E');                            
			}
		}
	}
	
	public void exeHLPOK()
	{
		super.exeHLPOK();
		
		if(M_strHLPFLD.equals("txtDPTCD"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtDPTCD.setText(L_STRTKN.nextToken());
			lblDPTNM.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtEMPNO"))
		{
		    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtEMPNO.setText(L_STRTKN.nextToken());
			lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
		}
		else if(M_strHLPFLD.equals("txtEMPNO_TB"))
		{
		      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		      txtEMPNO_TB.setText(L_STRTKN.nextToken());
		      tblWEKOF.setValueAt(L_STRTKN.nextToken(),tblWEKOF.getSelectedRow(),TB1_EMPNM);
		      tblWEKOF.setValueAt(L_STRTKN.nextToken(),tblWEKOF.getSelectedRow(),TB1_DPTNM);
		}
		else if(M_strHLPFLD.equals("txtWRKDT"))
		{
		      StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		      txtWRKDT.setText(L_STRTKN.nextToken());
		}
	}
	
	// pending OFF change or work on PH tracking
	// Auto-linking of Off change 
	//*****************************************************************************************
	private void dspCOTRN()
	{
		String LP_EMPNO = tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_EMPNO).toString();
		try
		{
			int TB_CHKFL_CO = 0;
			int TB_WRKDT_CO = 1;
			
			pnlCOTRN=new JPanel(null);
			int L_intROW=0;
			String[] L_staCOLHD = {"","Date"};
			int[] L_inaCOLSZ = {10,150};
			String L_strDSPMSG = LP_EMPNO+" has pending OFF - Change For the Following Dates";
			add(new JLabel(L_strDSPMSG),1,1,2,9,pnlCOTRN,'L');
			tblCOTRN = crtTBLPNL1(pnlCOTRN,L_staCOLHD,10,3,1,3,5,L_inaCOLSZ,new int[]{0});
			tblCOTRN.setEnabled(false);
			((JCheckBox)tblCOTRN.cmpEDITR[TB_CHKFL_CO]).setEnabled(true);
			pnlCOTRN.setSize(400,200);
			pnlCOTRN.setPreferredSize(new Dimension(400,200));
			

			// Fetching records from HR_SWMST for displaying in table
			M_strSQLQRY =" select SW_WRKDT from HR_SWMST where  ";
			M_strSQLQRY+=" SW_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ifNull(SW_LVECD,'')='' and SW_INCTM is null and SW_OUTTM is null";
			M_strSQLQRY+=" and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";			
			M_strSQLQRY+=" and sw_empno='"+LP_EMPNO+"' order by SW_WRKDT";
			
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
					tblCOTRN.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("SW_WRKDT")),L_intROW,TB_WRKDT_CO);
					L_intROW++;
				}
				M_rstRSSET.close();
			}					
			
			int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlCOTRN,"Off-Change details",JOptionPane.OK_CANCEL_OPTION);
			if(L_intOPTN == 0)
			{
				for(int i=0;i<tblCOTRN.getRowCount();i++)
				{
					if(tblCOTRN.getValueAt(i,TB_CHKFL_CO).toString().equals("true"))
					{
						//System.out.println(tblCOTRN.getValueAt(tblCOTRN.getSelectedRow(),TB_WRKDT_CO).toString());
						String L_strREFDT=tblCOTRN.getValueAt(tblCOTRN.getSelectedRow(),TB_WRKDT_CO).toString();
						((JTextField)tblWEKOF.cmpEDITR[TB1_REFDT]).setText(L_strREFDT);
					}
				}
			}
		}

		catch (Exception L_EX)
		{
			setMSG("Error in dspCOTRN : "+L_EX,'E');
		}
	}
	
	/**method to fetch record in table**/
	void getDATA()
	{
		try
		{
			tblWEKOF.clrTABLE();
			inlTBLEDIT(tblWEKOF);
			
			L_intROWNO=0;
			
			String L_strWHRSTR = "";
			if(txtSTRDT.getText().length()>0 && txtENDDT.getText().length()>0)
				L_strWHRSTR +=" and co_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
			if(txtEMPNO.getText().trim().length()>0)
				L_strWHRSTR+= " and ep_empno='"+txtEMPNO.getText().trim()+"'";
			if(txtDPTCD.getText().trim().length()>0)
				L_strWHRSTR+= " and ep_dptcd='"+txtDPTCD.getText().trim()+"'";
			
			setMSG("Fetching Records..",'N');
			
			M_strSQLQRY  =" select co_empno,co_wrkdt,co_refdt,co_stsfl,ep_lstnm||' '||substr(ep_fstnm,1,1)||'.'||substr(ep_mdlnm,1,1)||'.' ep_empnm,ep_dptnm";
			M_strSQLQRY+= " from  hr_cotrn,hr_epmst";
			M_strSQLQRY+= " where co_cmpcd=ep_cmpcd and co_empno = ep_empno and co_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				M_strSQLQRY +=" and co_stsfl='A'";
			M_strSQLQRY +=" and co_lvecd='OC' and ifnull(co_stsfl,'')<>'X'"+L_strWHRSTR;
			M_strSQLQRY +=" order by co_wrkdt,co_empno";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					tblWEKOF.setValueAt(M_rstRSSET.getString("co_empno"),L_intROWNO,TB1_EMPNO);
					tblWEKOF.setValueAt(M_rstRSSET.getString("ep_empnm"),L_intROWNO,TB1_EMPNM);
					tblWEKOF.setValueAt(M_rstRSSET.getString("ep_dptnm"),L_intROWNO,TB1_DPTNM);
					if(M_rstRSSET.getDate("co_wrkdt")!=null)
						tblWEKOF.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("co_wrkdt")),L_intROWNO,TB1_WRKDT);
					if(M_rstRSSET.getDate("co_refdt")!=null)
					{
						tblWEKOF.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("co_refdt")),L_intROWNO,TB1_REFDT);
						tblWEKOF.setValueAt(new Boolean(true),L_intROWNO,TB1_CHKFL);	
					}
					if(M_rstRSSET.getString("co_stsfl").equals("A"))
						tblWEKOF.setValueAt(new Boolean(true),L_intROWNO,TB1_AUTHC);	
					else 
						tblWEKOF.setValueAt(new Boolean(false),L_intROWNO,TB1_AUTHC);	
					L_intROWNO++;
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			if(L_intROWNO==0)
				setMSG("No Data Found..",'E');
			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	/**method to generate off change Notice report**/
	void genRPTFL()
	{
		try
		{
			//vtrWRKDT.clear();

			int L_intSRLNO=0,L_intSRLNO1=0;
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			
			setMSG("Printing Report..",'N');
			
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			strRPFNM = strRPLOC + "hr_rpsoc.html";

			Runtime r = Runtime.getRuntime();
			Process p = null;
		    p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
		    
			D_OUT.writeBytes("<HTML><HEAD><Title></title> </HEAD> <BODY><P><PRE style =\" font-size :9 pt \">");    
			D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
		
			cl_dat.M_PAGENO +=1;
			
			D_OUT.writeBytes("<p>&nbsp;</p>");
			crtTBL(D_OUT,0);
			D_OUT.writeBytes("<tr><td width='60%'>&nbsp;</td><td align='right' WIDTH='40%'><STRONG><FONT face='Arial' size='3'>"+cl_dat.M_strCMPNM_pbst+"</FONT></STRONG></td></tr>"+
							 "<tr><td>&nbsp;</td><td align='right'><FONT face='Arial' size='3'>"+M_strADDRS+"</FONT></td></tr></p></table>"+
							 "<br><br><p align='right'><FONT face='Arial' size='3'>Date:"+cl_dat.M_strLOGDT_pbst+"</FONT></p>"+
							 "<p align='left'><FONT face='Arial' size='3'>To,</FONT></p><br>"+
							 "<p align='left'><b><FONT face='Arial' size='3'>The Joined Director,</FONT></b></p>"+
							 "<p align='left'><FONT face='Arial' size='3'>Office of the Joint Director,</FONT></p>"+
							 "<p align='left'><FONT face='Arial' size='3'>Directorate of Industrial safety & Health, Raigad,</FONT></p>"+
							 "<p align='left'><FONT face='Arial' size='3'>6th Floor,Konkan Bhavan Annex,</FONT></p>"+
							 "<p align='left'><FONT face='Arial' size='3'>Room No.627,Belapur,C.B.D.,</FONT></p>"+
							 "<p align='left'><b><u><FONT face='Arial' size='3'>NAVI MUMBAI - 400 614.</FONT></u></b></p>"+
							 "<p >&nbsp;</p><p >&nbsp;</p>"+
							 "<p align='center'><b><FONT face='Arial' size='3'><u>Sub:Notice of change in weekly off.</u></FONT></b></p><br><br>"+
							 "<p align='left'><FONT face='Arial' size='3'>Dear Sir/Madam,</FONT></p><br><br>"+
							 "<p align='left'><FONT face='Arial' size='3'>As per sub-section (1) of Section 52 (CHAPTER-VI) of the Factories Act, 1948, we give below</FONT></p><br><br>"+
							 "<p align='left'><FONT face='Arial' size='3'>the notice of change in weekly off of the following employees.</FONT></p><br><br>");
							
			//TABLE HEADING			
			crtTBL(D_OUT,1);
			D_OUT.writeBytes("<tr>");
		    D_OUT.writeBytes("<th align ='center' width='1%' ><b><FONT face='Arial' size='3'>Sr.No</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='5%' ><b><FONT face='Arial' size='3'>Name of the Employee</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='2%' ><b><FONT face='Arial' size='3'>Emp No.</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='2%' ><b><FONT face='Arial' size='3'>Dept</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='2%' ><b><FONT face='Arial' size='3'>Date of original weekly off</font></b></th>");
		    D_OUT.writeBytes("<th align ='center' width='2%' ><b><FONT face='Arial' size='3'>Date of changed weekly off</font></b></th>");
			D_OUT.writeBytes("</tr>");
			//display records in report table
							
			for(int i=0;i<tblWEKOF.getRowCount();i++)
			{
				if(tblWEKOF.getValueAt(i,TB1_CHKFL).toString().equals("true") && tblWEKOF.getValueAt(i,TB1_EMPNO).toString().length()>0)
				{
					//vtrWRKDT.add(tblWEKOF.getValueAt(i,TB1_WRKDT).toString());
					//exeSORT(vtrWRKDT);
					L_intSRLNO+=1;
					D_OUT.writeBytes("<tr>");
					D_OUT.writeBytes("<td align ='center' width='1%'><FONT face='Arial' size='3'>"+L_intSRLNO+"</font></td>");
					D_OUT.writeBytes("<td align ='left' width='5%'><FONT face='Arial' size='3'>"+tblWEKOF.getValueAt(i,TB1_EMPNM)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='3'>"+tblWEKOF.getValueAt(i,TB1_EMPNO)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='3'>"+tblWEKOF.getValueAt(i,TB1_DPTNM)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='3'>"+tblWEKOF.getValueAt(i,TB1_WRKDT)+"</font></td>");
					D_OUT.writeBytes("<td align ='center' width='2%'><FONT face='Arial' size='3'>"+tblWEKOF.getValueAt(i,TB1_REFDT)+"</font></td>");
					D_OUT.writeBytes("</tr>");
				}
			}
			
			D_OUT.writeBytes("</p></table><br><br>");
			
			D_OUT.writeBytes("<p align ='left' width='2%'><FONT face='Arial' size='3'>The change in weekly off/s is/are due to exigencies of work/mutual exchange of the employees.</font></p>"+
					         "<br><p align ='left' width='2%'><FONT face='Arial' size='3'>Kindly accord your approval.</font></p>"+
					         "<br><p align ='left' width='2%'><FONT face='Arial' size='3'>Thanking You,</font></p>"+
					         "<p align ='right' width='2%'><FONT face='Arial' size='3'>Yours Faithfully,</font></p>"+
					         "<p align ='right' width='2%'><FONT face='Arial' size='3'>For SUPREME PETROCHEM LTD.</font></p>"+
					         "<p >&nbsp;</p><p >&nbsp;</p>"+
					         "<p align ='right' width='2%'><FONT face='Arial' size='3'>(K.V.MUJUMDAR)</font></p>"+
					         "<p align ='right' width='2%'><FONT face='Arial' size='3'>CHIEF EXECUTIVE(OPERATIONS)</font></p>"+
					         "<p align ='left' width='2%'><FONT face='Arial' size='3'>cc: Main Notice Board/Time Office</font></p>"+
					         "<p align ='left' width='2%'><FONT face='Arial' size='3'>cc: Concerned Employees/Concernec HODs.</font></p>");
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
	
	 private  void  exeSORT(Vector<String> LP_VTR)
    {
	   try
	   {
		   for(int i=0; i<LP_VTR.size();i++)
		   {
			  for(int j=i+1;j<LP_VTR.size();j++)
			  {
				 if(M_fmtLCDAT.parse(LP_VTR.get(j)).compareTo(M_fmtLCDAT.parse(LP_VTR.get(i)))<0)
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
		 setMSG(L_E,"exeSORT");
	   }  
    }
	 
	boolean vldDATA()
	{
		try
		{
			boolean flgSELRW=false;////flag to check whether atleast 1 row is selected
			
			for(int P_intROWNO=0;P_intROWNO<tblWEKOF.getRowCount();P_intROWNO++)
			{
				if(tblWEKOF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					flgSELRW=true;
					
					if(tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString().length()==0)
					{
						setMSG("Enter Employee No at Selected row in the table..",'E');
						return false;
						
					}
					else if(tblWEKOF.getValueAt(P_intROWNO,TB1_WRKDT).toString().length()==0)
					{
						setMSG("Enter Original Weekly Off Date for "+tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString()+" in the table..",'E');
						return false;
					}
					else if(tblWEKOF.getValueAt(P_intROWNO,TB1_REFDT).toString().length()==0)
					{
						setMSG("Enter Proposed Weekly Off Date for "+tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString()+" in the table..",'E');
						return false;
					}
				}
			}
			if(flgSELRW==false)
			{
				if(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_EMPNO).toString().length()>0)
				{
					setMSG("Please Select atleast 1 row from the Table",'E');
				}
				else
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						setMSG("Please Enter Records in Table ",'E');
				}
				return false;
			}
			else
				setMSG("",'N');
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATA()");		
		}
		return true;
	}
	
	boolean vldDATA_SW()
	{
		try
		{
			String L_strWHRSTR = "";
			for(int P_intROWNO=0;P_intROWNO<tblWEKOF.getRowCount();P_intROWNO++)
			{
				if(tblWEKOF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					L_strWHRSTR =" SW_EMPNO='"+tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString()+"'";
					L_strWHRSTR+=" and SW_WRKDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_REFDT).toString()))+"'";

					M_strSQLQRY =" select sw_lvecd from hr_swmst where"+L_strWHRSTR;
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						if(M_rstRSSET.getString("SW_LVECD").length()>0)
						{
							setMSG(tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString()+" already have entered leave for "+tblWEKOF.getValueAt(P_intROWNO,TB1_REFDT).toString()+" ,Please verify...",'E');
							return false;
						}
					}
				}
			}
		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATA_SW()");		
		}
		return true;
	}	
	
	/**method to insert record in hr_octrn database table**/
	private void exeADDREC(int P_intROWNO)
	{ 
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			String strSQLQRY=" select count(*) from HR_COTRN where";
			strSQLQRY+=" CO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ifnull(CO_STSFL,'')<>'X' AND CO_EMPNO='"+tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO)+"'";
			strSQLQRY += " AND CO_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_WRKDT).toString()))+"'";
			strSQLQRY += " AND CO_LVECD='OC'";
			ResultSet rstRSSET = cl_dat.exeSQLQRY(strSQLQRY);
			//System.out.println(">>>Count>>"+strSQLQRY);
			if(rstRSSET.next() && rstRSSET != null)
			{
				if(rstRSSET.getInt(1)>0)
				{
					 exeMODREC(P_intROWNO);
				}
				else
				{
		            M_strSQLQRY  =" insert into HR_COTRN (CO_EMPNO,CO_WRKDT,CO_REFDT,CO_LVECD,";
					M_strSQLQRY +=" CO_TRNFL,CO_STSFL,CO_LUSBY,CO_LUPDT,CO_CMPCD,CO_SBSCD)";
					M_strSQLQRY +=" VALUES('" +tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO)+"',";
		            M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_WRKDT).toString()))+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_REFDT).toString()))+"',";
					M_strSQLQRY += "'OC',";
					M_strSQLQRY += "'0',";
					M_strSQLQRY += (tblWEKOF.getValueAt(P_intROWNO,TB1_AUTHC).toString().equals("true")?"'A',":"'W',");
					M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";	
					M_strSQLQRY += "'"+M_strSBSCD+"')";
					
					//System.out.println(">>>Insert>>"+ M_strSQLQRY);
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}

				if(tblWEKOF.getValueAt(P_intROWNO,TB1_AUTHC).toString().equals("true"))
				{
					M_strSQLQRY  = " Update HR_SWMST set";
	    			M_strSQLQRY += " SW_LVECD ='OC'";
					M_strSQLQRY += " where SW_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
					M_strSQLQRY += " AND SW_EMPNO= '"+tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString()+"'";
					M_strSQLQRY += " AND SW_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_REFDT).toString()))+"'";
					//System.out.println(">>>update_swm>>"+M_strSQLQRY);  
					cl_dat.M_flgLCUPD_pbst = true;
					cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				}
			}

			setCursor(cl_dat.M_curDFSTS_pbst);

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeADDREC()"); 
		}
	}
	
	/**Method to Modify Records of ht_cotrn  database table**/
	  private void exeMODREC(int P_intROWNO) 
	  {
	  try
	    {
    	    M_strSQLQRY = " Update HR_COTRN set";
    	
	    	if(tblWEKOF.getValueAt(P_intROWNO,TB1_REFDT).toString().length()>0)
	    		M_strSQLQRY += " CO_REFDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_REFDT).toString()))+"',";
	    	M_strSQLQRY += " CO_STSFL ="+(tblWEKOF.getValueAt(P_intROWNO,TB1_AUTHC).toString().equals("true")?"'A',":"'W',");
			M_strSQLQRY += " CO_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += " CO_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where CO_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " AND CO_EMPNO= '"+tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString()+"'";
			M_strSQLQRY += " AND CO_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_WRKDT).toString()))+"'";
			M_strSQLQRY += " AND CO_LVECD= 'OC'";
			//System.out.println(">>>update>>"+M_strSQLQRY);  
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
		}
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"exeMODREC()");
	    }
	}
	  /**
		 * Delete Records From HR_COTRN Table*/
		private void exeDELREC(int P_intROWNO) 
		{ 
		  try
		  {
	  		M_strSQLQRY = "UPDATE HR_COTRN SET";	
			M_strSQLQRY += " CO_STSFL='X'";	
			M_strSQLQRY += " where CO_CMPCD= '"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " AND CO_EMPNO= '"+tblWEKOF.getValueAt(P_intROWNO,TB1_EMPNO).toString()+"'";
			M_strSQLQRY += " AND CO_WRKDT= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWNO,TB1_WRKDT).toString()))+"'";
			M_strSQLQRY += " AND CO_LVECD='OC'";
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			//System.out.println(">>>Delete>>"+M_strSQLQRY);
		  }
		  catch(Exception L_EX)
		  {
		     setMSG(L_EX,"exeDELREC()");		
		  }
		}
	
	
/** method to click on Save button to save records ***/
	void exeSAVE()
	{
		try
		{
			int P_intROWNO=0;
			if(!vldDATA()) 
			{
				return;
			}
			if(!vldDATA_SW()) 
			{
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) ||
			   cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
			{
				for(P_intROWNO=0;P_intROWNO<tblWEKOF.getRowCount();P_intROWNO++)
				{
					if(tblWEKOF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						exeADDREC(P_intROWNO);
					}
				}
			}
			/*for(P_intROWNO=0;P_intROWNO<tblWEKOF.getRowCount();P_intROWNO++)
			{
				if(tblWEKOF.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
					{
						exeADDREC(P_intROWNO);
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
					{
						exeMODREC(P_intROWNO);
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
					{
						exeDELREC(P_intROWNO);
					}
				}
			}*/
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG(" Data Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					setMSG(" Data Modified Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
				tblWEKOF.clrTABLE();
				inlTBLEDIT(tblWEKOF);
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
	
	void inlTBLEDIT(cl_JTable tblTABLE)
	{
		if(tblTABLE.isEditing())
			tblTABLE.getCellEditor().stopCellEditing();
		tblTABLE.setRowSelectionInterval(0,0);
		tblTABLE.setColumnSelectionInterval(0,0);
	}	
	
	/**Method to creat HTML Table*/
	private void crtTBL(DataOutputStream L_DOUT,int P_intBORDR) throws Exception
	{
		L_DOUT.writeBytes("<p><TABLE border="+P_intBORDR+" borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray  cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
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
					if(txtDPTCD.getText().length()>0)
					{
						M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
						M_strSQLQRY+=" and CMT_STSFL <> 'X' and CMT_CODCD='"+txtDPTCD.getText().trim()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET.next() && M_rstRSSET!=null)
						{
							lblDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
							setMSG("",'N');
							//if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && txtSTRDT.getText().length()==0 && txtENDDT.getText().length()==0)
							//	getDATA();
						}	
						else
						{
							setMSG("Enter Valid Department Code",'E');
							return false;
						}	
					}
				}	
				if(input == txtEMPNO)
				{
					if(!flgALLDPT && txtDPTCD.getText().length()==0)
					{
						setMSG("Please Enter Department Code first...",'E');
						txtDPTCD.requestFocus();
						txtEMPNO.setText("");
						return false;
					}
					M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText().trim()+"' and EP_STSFL <> 'U' ";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY +=" and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
					//System.out.println("<<<<"+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Employee Code",'E');
						return false;
					}			
				}	
				/*else if(input == txtMONTH)
				{ 
					if(txtMONTH.getText().length()==1)
						txtMONTH.setText("0"+txtMONTH.getText());
					else if(txtMONTH.getText().equals("00"))
					{
						setMSG("Enter valid Month..",'E');
						return false;
					}
					else if(txtMONTH.getText().compareTo("12")>0)
					{
						setMSG("Enter valid Month..",'E');
						return false;
					}
					else if(M_fmtLCDAT.parse("01/"+txtMONTH.getText()+"/"+txtYEAR.getText().toString().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))> 0)
					{
						setMSG("Month can not be greater than Current Month..",'E');
						txtMONTH.requestFocus();
						return false;
					}
				}
				else if(input == txtYEAR)
				{ 
					if(Integer.parseInt(txtYEAR.getText())>Integer.parseInt(cl_dat.M_strLOGDT_pbst.substring(6)))
					{
						setMSG("Year can not be greater than Current Year..",'E');
						txtYEAR.requestFocus();
						return false;
					}
					else if(txtYEAR.getText().length()<4)
					{
						setMSG("please Enter valid Year..",'E');
						txtYEAR.requestFocus();
						return false;
					}
					else if(M_fmtLCDAT.parse("01/"+txtMONTH.getText()+"/"+txtYEAR.getText().toString().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))> 0)
					{
						setMSG("Month can not be greater than Current Month..",'E');
						txtMONTH.requestFocus();
						return false;
					}
				}*/
				else if(input == txtSTRDT)
				{
					if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("From date can not be greater than Today's date..",'E');
						return false;
					}
				}
				else if(input == txtENDDT)
				{
					if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG("To date can not be greater than Today's date..",'E');
						return false;
					}
					if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText()))>0)
					{
						setMSG("Invalid Date Range..",'E');
						return false;
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
	
	/**class to verify DOC No & Work Shift**/
	private class TBLINPVF extends TableInputVerifier
    {
		public boolean verify(int P_intROWID,int P_intCOLID)
		{	
			try
			{
				if(tblWEKOF.getValueAt(P_intROWID,P_intCOLID).toString().length()== 0)
					return true;
				
				if(getSource()==tblWEKOF)
			    {
					if(P_intCOLID==TB1_EMPNO)
					{
						M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM,ep_dptnm from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+tblWEKOF.getValueAt(P_intROWID,TB1_EMPNO)+"' and EP_STSFL <> 'U' ";
						if(txtEMPNO.getText().length()>0)
							M_strSQLQRY +=" and EP_EMPNO = '"+txtEMPNO.getText().trim()+"'";
						if(txtDPTCD.getText().length()>0)
							M_strSQLQRY +=" and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
						//System.out.println("<<<<"+M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);						
	                    if(M_rstRSSET.next() && M_rstRSSET!=null)
	                    {
	                    	tblWEKOF.setValueAt(M_rstRSSET.getString("EP_EMPNO"),P_intROWID,TB1_EMPNO);
	                    	tblWEKOF.setValueAt(M_rstRSSET.getString("ep_empnm"),P_intROWID,TB1_EMPNM);
	                    	tblWEKOF.setValueAt(M_rstRSSET.getString("ep_dptnm"),P_intROWID,TB1_DPTNM);
	                     	
	                    	setMSG("",'N');
	                    }  
	                    else
						{
							setMSG("Enter valid Employee No",'E');
							return false;
						}
	                	M_rstRSSET.close();
					}
					else if(P_intCOLID==TB1_WRKDT)
					{
						if(tblWEKOF.getValueAt(P_intROWID,TB1_WRKDT).toString().length()>0)
						{
							M_strSQLQRY = "select sw_wrkdt from hr_swmst where sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_empno='"+tblWEKOF.getValueAt(P_intROWID,TB1_EMPNO)+"' and sw_lvecd='WO'";
							//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst) && txtMONTH.getText().length()>0 && txtYEAR.getText().length()>0 )
		        			//	M_strSQLQRY +=" and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_strENDDT))+"'";
							if(txtSTRDT.getText().length()>0 && txtENDDT.getText().length()>0)
		        				M_strSQLQRY +=" and sw_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
		        			M_strSQLQRY +=" and sw_wrkdt='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWID,TB1_WRKDT).toString()))+"'";
		        			//System.out.println("<<<<"+M_strSQLQRY);
		
							M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);						
		                    if(M_rstRSSET!=null && M_rstRSSET.next())
		                    {
		                    	//tblWEKOF.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("sw_wrkdt")),P_intROWID,TB1_WRKDT);
		                    	tblWEKOF.setValueAt(tblWEKOF.getValueAt(P_intROWID,TB1_WRKDT),P_intROWID,TB1_WRKDT);
		                    	setMSG("",'N');
		                    }  
		                    else
							{
								setMSG("Enter valid Original Weekly off",'E');
								return false;
							}
		                    M_rstRSSET.close();
		                    
		                   for(int i=0;i<tblWEKOF.getRowCount();i++)
							{
		                    	if(i!=tblWEKOF.getSelectedRow())
		                    	{
		                    		if(tblWEKOF.getValueAt(i,TB1_EMPNO).equals(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_EMPNO)))
			                    	if(tblWEKOF.getValueAt(i,TB1_WRKDT).toString().equals(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_WRKDT).toString()))
									{
			                    		setMSG("This Original Weekly Off Already exist for "+tblWEKOF.getValueAt(i,TB1_EMPNM),'E');
			                    		return false;
									}
		                    	}
							}
						}
					}
					else if(P_intCOLID==TB1_REFDT)
					{
						if(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWID,TB1_REFDT).toString()).compareTo(M_fmtLCDAT.parse(tblWEKOF.getValueAt(P_intROWID,TB1_WRKDT).toString()))==0)
						{
							setMSG("Proposed Off Date must be greater than Original Off Date",'E');
							return false;
						}
						if(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_WRKDT).toString().length()>0 )
						{
							M_calLOCAL.setTime(M_fmtLCDAT.parse(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_WRKDT).toString()));
							M_calLOCAL.add(Calendar.DATE,3);
							M_strREFDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
							//System.out.println("M_strREFDT>>>>"+M_strREFDT);
							
							M_calLOCAL.setTime(M_fmtLCDAT.parse(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_WRKDT).toString()));
							M_calLOCAL.add(Calendar.DATE,-3);
							M_strREFDT1=M_fmtLCDAT.format(M_calLOCAL.getTime());
							//System.out.println("M_strREFDT1>>>>"+M_strREFDT1);
						
							/*if(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_REFDT).toString().length()>0)
							{
								if(M_fmtLCDAT.parse(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_REFDT).toString()).compareTo(M_fmtLCDAT.parse(M_strREFDT))>0 || M_fmtLCDAT.parse(tblWEKOF.getValueAt(tblWEKOF.getSelectedRow(),TB1_REFDT).toString()).compareTo(M_fmtLCDAT.parse(M_strREFDT1))<0)
								{
									JOptionPane.showConfirmDialog(null,"Do You Want To Change Proposed Weekly Off ?","Proposed Weekly Off Validation",JOptionPane.OK_CANCEL_OPTION);
								}
							}*/
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


