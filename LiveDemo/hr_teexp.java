/**System Name:Human Resource System.
 
Program Name: Exit Pass Entry

Source Directory: f:\source\splerp3\hr_teexp.java                         

Executable Directory: F:\exec\splerp3\hr_teexp.class

Purpose: Exit Pass Entry.

List of tables used:
Table Name		Primary key											Operation done
															Insert	Update	   Query    Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
HR_EXTRN		EX_DOCNO, EX_DOCDT, EX_CMPCD                   /	  /           /         /
                 
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields accepted/displayed on screen:
Field Name		Column Name		Table name		Type/Size		Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtDOCNO		EX_DOCNO		HR_EXTRN		Varchar(8)		DOC No	
txtDOCDT		EX_DOCDT		HR_EXTRN		Date  			DOC Date
txtEMPNO		EX_EMPNO		HR_EXTRN		Varchar(4)  	Employee No
txtEMPNM 		EX_EMPNM		HR_EXTRN		Varchar(50) 	Employee Name
txtDPTNM		EX_DPTNM		HR_EXTRN		varchar(20)  	Department name
txtREMDS		EX_REMDS		HR_EXTRN		Varchar(40)  	Purpose
txtEOTTM		EX_EOTTM		HR_EXTRN		Date Time  	    Exit OUT time
txtEINTM		EX_EINTM		HR_EXTRN		Date Time  	    Exit IN time
txtAUTBY		EX_AUTBY		HR_EXTRN		Varchar(3)		Authorized by
-----------------------------------------------------------------------------------------------------------------------------------------------------


List of fields with help facility: 
Field Name	Display Description		    Display Columns			Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtEMPNO	Employee No,Employee NAME	EP_EMPNO,EP_EMPNM       HR_EPMTS
txtDOCNO	DOC No, DOC Date            EX_DOCNO, EX_DOCDT		HR_EXTRN
txtSHFCD    Shift code, Description     cmt_codcd,cmt_codds     co_cdtrn   
txtAUTBY    Sanctioning Authority       cmt_ccsvl               co_cdtrn
-----------------------------------------------------------------------------------------------------------------------------------------------------


Validations & Other Information:
    - Enter valid Employee No,work shift & Authorized by.
  	- Doc Date must be current date.
  	- Out time has to be greater than clock time & can not be blank.
  	- In time has to be greater than Out Time
    
    
Requirements:
	- When click on save button to save data,then display DOC NO in JOptionpane. 
	- When user select Personel option,it will be set EX_OFLFL='P' & generate DOC No initial 902 . 
	- When user select Official option,it will be set EX_OFLFL='O' & generate DOC No initial 901 .
	- User can delete selected data .
	- Print records using DOC No.reference.
	- Authorized person should be leave sanctioning authoriyt or Recommending Authority.
    - In Exit Pass entry tab user can be enter exit pass data with status '0' i.e consider Exit pass entry.
    - Authorized tab will be enabled while select Authorization option otherwise disable.
    - If Authorized person will not be present,While User can be enter self Exit Pass, after that Authorized person will be Authorized with status '1'(i.e.HOD Authorization) & with change Auth by.
    - For Authorization ,Authorize person can be select perticular row of exit pass.  
    - In Authorization tab display two table ,one for display pending exit pass (with status '0') to Authorized & in next table display Authorized exit pass(with status '1') by Authority.
*/    

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class hr_teexp extends cl_pbase implements MouseListener, KeyListener
{
	private JTextField txtDOCNO;			
	private JTextField txtDOCDT;
	private JTextField txtFMDAT;
	private JTextField txtTODAT;
	private JTextField txtEMPNO;
	private JTextField txtEMPNM;
	private JTextField txtDPTCD;
	private JTextField txtREMDS;
	private JTextField txtEINTM;			
	private JTextField txtEOTTM;
	private JTextField txtAUTBY;
	private JTextField txtSHFCD;
	
	private JLabel lblEINTM,lblAUTBY;
	
	private cl_JTable tblEXPDTL,tblEXPDTL1;
	private JTabbedPane jtpMANTAB;                         /** one tab for Exit Pass Entry & another for Authorization.*/
	private JPanel pnlEXPTB,pnlAUTTB;   
	
	Hashtable<String,String> hstEMPDL;
	private JRadioButton rdbPERSL,rdbOFICL;
	private JButton btnPRINT;
	private int addFLAG=0;//if addition in combo not selected
	INPVF oINPVF;
	hr_rpexp ohr_rpexp;

	
	/**column for exit pass table**/
	private final int TB1_CHKFL =0;       
	private final int TB1_DOCDT =1;
	private final int TB1_EMPNO =2;
	private final int TB1_EMPNM =3;
	private final int TB1_DPTNM =4;
	private final int TB1_OUTTM =5;
	private final int TB1_INCTM =6;
	private final int TB1_DOCNO =7;
	private final int TB1_WRKSH =8;
	private final int TB1_REMDS =9;
	private final int TB1_OFPFL =10;
	private final int TB1_AUTBY =11;
	private final int TB1_STSFL =12;
	hr_teexp()
	{
		super(1);
		setMatrix(20,8);
		try
		{
			
			pnlEXPTB = new JPanel();
			pnlAUTTB = new JPanel();
			pnlEXPTB.setLayout(null);
			pnlAUTTB.setLayout(null);
		
			jtpMANTAB=new JTabbedPane();
			jtpMANTAB.addMouseListener(this);
			
			jtpMANTAB.add(pnlEXPTB,"Exit Pass Entry");
			jtpMANTAB.add(pnlAUTTB,"Authorization");
			
			
			add(new JLabel("EMP NO."),1,2,1,1,pnlEXPTB,'L');
			add(txtEMPNO = new TxtLimit(4),1,3,1,1,pnlEXPTB,'L');
			
			add(new JLabel("DOC NO."),2,2,1,1,pnlEXPTB,'L');
			add(txtDOCNO = new TxtLimit(8),2,3,1,1,pnlEXPTB,'L');
			
			add(btnPRINT= new JButton("PRINT"),1,7,1,1,pnlEXPTB,'L');
			
			add(new JLabel("Doc Date."),2,5,1,1,pnlEXPTB,'L');
			add(txtDOCDT = new TxtDate(),2,6,1,1,pnlEXPTB,'L');
			
			add(new JLabel("EMP Name"),3,2,1,1,pnlEXPTB,'L');
			add(txtEMPNM = new TxtLimit(50),3,3,1,2,pnlEXPTB,'L');
			
			add(new JLabel("Dept Name"),3,5,1,1,pnlEXPTB,'L');
			add(txtDPTCD = new TxtLimit(15),3,6,1,1,pnlEXPTB,'L');
			
			add(new JLabel("Purpose"),4,2,1,1,pnlEXPTB,'L');
			add(txtREMDS= new TxtLimit(40),4,3,1,4,pnlEXPTB,'L');

			add(new JLabel("Personal"),5,3,1,0.7,pnlEXPTB,'R');
			add(rdbPERSL = new JRadioButton(),5,4,1,0.5,pnlEXPTB,'L');

			add(new JLabel("Official"),5,5,1,0.5,pnlEXPTB,'R');			
			add(rdbOFICL = new JRadioButton(),5,6,1,0.5,pnlEXPTB,'L');

			//ButtonGroup bg=new ButtonGroup();
			//bg.add(rdbPERSL);
			//bg.add(rdbOFICL);
			rdbPERSL.setSelected(false);	
			rdbOFICL.setSelected(false);
			
			add(new JLabel("Shift"),6,2,1,1,pnlEXPTB,'L');			
			add(txtSHFCD = new TxtLimit(1),6,3,1,1,pnlEXPTB,'L');
			
			add(new JLabel("Expected Out Time"),7,2,1,2,pnlEXPTB,'L');
			add(txtEOTTM = new TxtTime(),7,3,1,1,pnlEXPTB,'L');

			add(lblEINTM=new JLabel("Expected IN Time"),7,4,1,2,pnlEXPTB,'L');
			add(txtEINTM = new TxtTime(),7,5,1,1,pnlEXPTB,'L');

			add(lblAUTBY=new JLabel("Authorized by"),8,2,1,2,pnlEXPTB,'L');
			add(txtAUTBY = new TxtLimit(3),8,3,1,1,pnlEXPTB,'L');
		
			/**table for authorization**/
			
			String[] L_strCOLHD = {"","Doc Date","Emp No","Emp Name","Dept","Out Time","In Time","DOC No","WrkShf","Purpose","Off/Pers","Auth By","Status"};
      		int[] L_intCOLSZ= {20,80,50,110,80,70,70,70,40,210,40,50,80};	    				
			tblEXPDTL = crtTBLPNL1(pnlAUTTB,L_strCOLHD,50,2,1,5,8,L_intCOLSZ,new int[]{0});
			
			tblEXPDTL.addMouseListener(this);
			add(new JLabel("From Date"),8,4,1,1,pnlAUTTB,'L');
			add(txtFMDAT=new TxtDate(),8,5,1,1,pnlAUTTB,'L');
			add(new JLabel("To Date"),8,6,1,1,pnlAUTTB,'L');
			add(txtTODAT=new TxtDate(),8,7,1,1,pnlAUTTB,'L');
			txtFMDAT.addKeyListener(this);
			tblEXPDTL1 = crtTBLPNL1(pnlAUTTB,L_strCOLHD,50,9,1,5,8,L_intCOLSZ,new int[]{0});
			//add(pnlAUTTB,1,1,15,10,jtpMANTAB,'L');
			tblEXPDTL.addFocusListener(this);
			tblEXPDTL1.addFocusListener(this);
			
			add(jtpMANTAB,1,1,15,8,this,'L');
			oINPVF=new INPVF();
			txtEMPNO.setInputVerifier(oINPVF);
			txtDOCNO.setInputVerifier(oINPVF);
			txtEOTTM.setInputVerifier(oINPVF);
			txtEINTM.setInputVerifier(oINPVF);			
            txtSHFCD.setInputVerifier(oINPVF);
           
			tblEXPDTL.clrTABLE();
			tblEXPDTL1.clrTABLE();
			
			setENBL(false);
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}

	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == txtEMPNO)
		{
			txtEMPNO.requestFocus();
			setMSG("Enter the Employee No.or Press F1 to Select form List..",'N');
		}
		else if(M_objSOURC == txtAUTBY)
		{
			txtAUTBY.requestFocus();
			setMSG("Enter the Authorized by or Press F1 to Select form List..",'N');
		}
				
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				txtEMPNO.requestFocus();
				jtpMANTAB.setEnabledAt(1,false);
				jtpMANTAB.setSelectedIndex(0);
				clrCOMP();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					
					txtEMPNM.setEnabled(false);
					txtDPTCD.setEnabled(false);
					txtDOCNO.setEnabled(false);	
					lblEINTM.setEnabled(false);
					txtEINTM.setEnabled(false);
					lblAUTBY.setEnabled(false);
					txtAUTBY.setEnabled(false);
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					{
						addFLAG=0;
						
						setMSG("Enter the DOC No. or Press F1 to Select from List..",'N');
						txtDOCNO.setEnabled(true);
						txtDOCDT.setEnabled(false);
						txtEMPNM.setEnabled(false);
						txtDPTCD.setEnabled(false);
						txtREMDS.setEnabled(false);
						txtEOTTM.setEnabled(false);
						txtEINTM.setEnabled(false);
						txtAUTBY.setEnabled(false);
						txtSHFCD.setEnabled(false);
						rdbOFICL.setEnabled(false);
						rdbPERSL.setEnabled(false);
						
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						addFLAG=0;
						txtDOCNO.setEnabled(true);
						setMSG(" For Modification first Enter PR No. or Press F1 to Select from List..",'N');
						
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
		    		{	
						jtpMANTAB.setEnabledAt(1,true);
						jtpMANTAB.setSelectedIndex(1);
						tblEXPDTL.setEnabled(false);
						tblEXPDTL1.setEnabled(false);
						txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
						txtTODAT.setText(txtFMDAT.getText());
						if(jtpMANTAB.getSelectedIndex()==1)
						{
							((JCheckBox)tblEXPDTL.cmpEDITR[TB1_CHKFL]).setEnabled(true);
								getEXPDTL();   /**fetch Exit pass entry & authorized data in perticular table, while click & select Authorization tab & option resp.**/			
						}
		    		}
					
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{				
						setMSG("Enter the Emp No or Press F1 to Select from List....",'N');
						
						addFLAG=1;  //adddion option in combo selected
						
						txtEOTTM.setText(addTIME(cl_dat.M_txtCLKTM_pbst.getText().substring(1,6),"00:15"));
					}				

					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
						btnPRINT.setEnabled(false);

				}

				else
					setENBL(false);

				 
			}
			else if(M_objSOURC == txtDOCNO) 
			{
				if(txtDOCNO.getText().length()==0)
					clrCOMP();
			}
			
			else if(M_objSOURC==rdbOFICL)
			{
				rdbOFICL.setSelected(true);
				rdbPERSL.setSelected(false);
			}			
			else if(M_objSOURC==rdbPERSL)
			{
				rdbPERSL.setSelected(true);
				rdbOFICL.setSelected(false);
			}
			/**Print Exit Pass Slip**/
			else if(M_objSOURC ==btnPRINT)
			{
				
				//System.out.println("***"+txtDOCNO.getText()+" "+txtDOCDT.getText());
				
				ohr_rpexp = new hr_rpexp();
				ohr_rpexp.exePRNTPRM(txtDOCNO.getText(),txtDOCDT.getText());
				/*
				Runtime r = Runtime.getRuntime();
				Process p = null;					    
				p  = r.exec("c:\\windows\\iexplore.exe "+cl_dat.M_strREPSTR_pbst+"hr_rpexp.html"); 
				setMSG("For Printing Select File Menu, then Print  ..",'N');	
				*/
				//below code allows user to select from printer 
				//JComboBox L_cmbLOCAL = ohr_rpexp.getPRNLS();				
				//ohr_rpexp.doPRINT(cl_dat.M_strREPSTR_pbst+"hr_rpexp.html",L_cmbLOCAL.getSelectedIndex());
				//btnPRINT.setEnabled(false);
				//if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)|| cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				//btnPRINT.setEnabled(false);

			} 
			
			if(M_objSOURC == txtTODAT)
    		{
				if(txtFMDAT.getText().length()>0 && txtTODAT.getText().length()>0)
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))						
						getEXPDTL(); /**fetch Exit pass entry & authorized data in perticular table while enter on To Date**/     	
		    }
			
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			setMSG(e,"error in action performed");
		}
	}

	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{
				if(M_objSOURC == txtDOCNO)
				{
					if(txtEMPNO.getText().length()>0)
					{
						M_strHLPFLD = "txtDOCNO";
						cl_dat.M_flgHELPFL_pbst = true;
						setCursor(cl_dat.M_curWTSTS_pbst);
	
						M_strSQLQRY=" SELECT EX_DOCNO,EX_DOCDT,EX_OFPFL from HR_EXTRN  ";
						M_strSQLQRY+=" where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";	
						M_strSQLQRY+=" and EX_EMPNO='"+txtEMPNO.getText()+"'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
							M_strSQLQRY+= " and EX_STSFL ='0'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
							M_strSQLQRY+= " and isnull(EX_STSFL,'')<>'X'";
						if(txtDOCNO.getText().length()>0)				
							M_strSQLQRY += " AND EX_DOCNO like '"+txtDOCNO.getText().trim()+"%'";
	
						M_strSQLQRY += " order by EX_DOCNO";
						//System.out.println("txtDOCNO>>"+M_strSQLQRY);
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date","OFF/PERS Flag"},3,"CT");
						setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}	
				
				else if(M_objSOURC == txtEMPNO)
				{
					M_strHLPFLD = "txtEMPNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY = " select EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM,EP_DPTNM from HR_EPMST where  isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
					M_strSQLQRY+=" and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(exeSICCHK(cl_dat.M_strEMPNO_pbst) ? "" : " and ep_dptcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN') and SUBSTRING(cmt_codcd,6,4)='"+cl_dat.M_strEMPNO_pbst+"'))");
					
					//M_strSQLQRY+=" and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(exeSICCHK(cl_dat.M_strEMPNO_pbst) ? "" : " and ep_empno in (select SUBSTRING(cmt_codcd,1,4) from co_cdtrn where cmt_cgmtp = 'A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN','HR"+cl_dat.M_strCMPCD_pbst+"EXA') and SUBSTRING(cmt_codcd,6,4)='"+cl_dat.M_strEMPNO_pbst+"') ");
					
					if(txtEMPNO.getText().length() >0)				
						M_strSQLQRY += " AND EP_EMPNO like '"+txtEMPNO.getText().trim()+"%'";

					M_strSQLQRY += " order by EP_EMPNO";
					//System.out.println("txtEMPNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,2,1,new String[]{"EMP No","EMP Name","Dept Name"},3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);

				}
			
				else if(M_objSOURC == txtSHFCD)
				{
					M_strHLPFLD = "txtSHFCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);

					M_strSQLQRY = " select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='M"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='COXXSHF'";

					if(txtSHFCD.getText().length()>0)				
						M_strSQLQRY += " AND cmt_codcd like '"+txtSHFCD.getText().trim()+"%'";

					M_strSQLQRY += " order by cmt_codcd";
					//System.out.println("txtDOCNO>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,2,new String[]{"Shift Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);

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
				if(jtpMANTAB.getSelectedIndex()==0)
				{			
					if(M_objSOURC == txtEMPNO)
					{
						txtEMPNM.setText("");
						txtDPTCD.setText("");
						txtDOCNO.setText("");
						txtDOCDT.setText("");
						txtREMDS.setText("");
						txtSHFCD.setText("");
						txtEOTTM.setText("");
						if(rdbPERSL.isSelected())
							rdbPERSL.setSelected(false);
						else if(rdbOFICL.isSelected())
							rdbOFICL.setSelected(false);

						
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
							txtDOCNO.requestFocus();
						else
						{
							txtEOTTM.setText(addTIME(cl_dat.M_txtCLKTM_pbst.getText().substring(1,6),"00:15"));
							txtDOCDT.requestFocus();
							txtDOCDT.setText(cl_dat.M_strLOGDT_pbst);
							txtDOCDT.setSelectionStart(0);
							txtDOCDT.setSelectionEnd(cl_dat.M_strLOGDT_pbst.length());
						}
					}
					else if(M_objSOURC == txtDOCNO)
					{
						txtDOCDT.setText("");
						txtREMDS.setText("");
						txtSHFCD.setText("");
						txtEOTTM.setText("");
						txtDOCDT.requestFocus();
					}
					else if(M_objSOURC == txtDOCDT)
					{
						
						setSHIFT(txtEMPNO.getText(),txtDOCDT.getText());
						txtREMDS.requestFocus();
						if(addFLAG==1)setMSG("Enter Reason for going out.",'N');
					}
					else if(M_objSOURC == txtREMDS)
					{
						txtREMDS.setText(txtREMDS.getText().replace("'","`"));
						txtSHFCD.requestFocus();
						if(addFLAG==1)setMSG("Enter Shift or Press F1 to Select form List..",'N');
					}
					
					else if(M_objSOURC == txtSHFCD)
					{
						//if(txtEOTTM.getText().length()==0)
						//	txtEOTTM.setText(cl_dat.M_txtCLKTM_pbst.getText());
						txtEOTTM.requestFocus();
						if(addFLAG==1)setMSG("Enter Out Time.",'N');
						
					}			
					
					/*else if(M_objSOURC == txtEOTTM)
					{
						//if(txtEINTM.getText().length()==0)
						//	txtEINTM.setText(cl_dat.M_txtCLKTM_pbst.getText());
						
						txtEINTM.requestFocus();
						if(addFLAG==1)setMSG("Enter In Time.",'N');
					}
					else if(M_objSOURC == txtEINTM)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							txtAUTBY.requestFocus();
							setMSG("Enter Authorized by or Press F1 to Select form List....",'N');
						}					
						else
						cl_dat.M_btnSAVE_pbst.requestFocus();	
					}
					else if(M_objSOURC == txtAUTBY)
					{
						cl_dat.M_btnSAVE_pbst.requestFocus();	
					}*/
					
					else if(M_objSOURC == txtEOTTM)
					{
						cl_dat.M_btnSAVE_pbst.requestFocus();	
					}
				}
				if(jtpMANTAB.getSelectedIndex()==1)
	    		{	
					if(M_objSOURC == txtFMDAT)
					{
						txtTODAT.setText(txtFMDAT.getText());
						txtTODAT.requestFocus();
						setMSG("Enter To Date.",'N');
					}
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
		cl_dat.M_flgHELPFL_pbst = false;
		if(M_strHLPFLD.equals("txtEMPNO"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtEMPNO.setText(L_STRTKN.nextToken());
			txtEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
			txtDPTCD.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtDOCNO"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtDOCNO.setText(L_STRTKN.nextToken());
			txtDOCDT.setText(L_STRTKN.nextToken());
		}
		else if(M_strHLPFLD.equals("txtAUTBY"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtAUTBY.setText(L_STRTKN.nextToken());
			
		}
		else if(M_strHLPFLD.equals("txtSHFCD"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtSHFCD.setText(L_STRTKN.nextToken());
			
		}
	}
	
/***Method to check Shift Incharge**/
	private boolean exeSICCHK(String LP_strEMPNO)
	{
		boolean flgSICCHK = false;
		try
		{
			M_strSQLQRY= " SELECT count(*) CNT from CO_CDTRN where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXSIC' and cmt_codcd = '"+LP_strEMPNO+"'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				if(L_rstRSSET.getInt("CNT")>0)
					flgSICCHK = true;
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)	
		{
			setMSG("Error in exeSICCHK : "+L_EX,'E');
		}
			return flgSICCHK;
	}
	

	/** Method to display Exit Pass record with status flag '0' in first table & '1' in second table . */
	private void getEXPDTL()
	{        
		try  
		{   
			if(txtFMDAT.getText().length()==0)
			{
				setMSG("Please Enter From Date...",'E');
				txtFMDAT.requestFocus();
				return;
			}
			if(txtTODAT.getText().length()==0)
			{
				setMSG("Please Enter To Date...",'E');
				txtTODAT.requestFocus();
				return;
			}
			boolean flgSICCHK = exeSICCHK(cl_dat.M_strEMPNO_pbst);
			
			tblEXPDTL.clrTABLE();
			inlTBLEDIT(tblEXPDTL);
			tblEXPDTL1.clrTABLE();
			inlTBLEDIT(tblEXPDTL1);
			
			int L_ROWNO = 0;
			int L_ROWNO1 = 0;
			java.sql.Timestamp L_tmsINCTM=null,L_tmsOUTTM=null;
		    
			M_strSQLQRY= " SELECT EX_DOCDT,EX_EMPNO,EX_DOCNO,EX_REMDS,EX_EOTTM,EX_EINTM,EX_AOTTM,EX_OFPFL,";
			M_strSQLQRY+= " EX_AUTBY,EX_SHFCD,EX_STSFL,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '  + left(isnull(ep_fstnm,' '),1) + '.' + left(isnull(ep_mdlnm,' '),1) + '.'  EP_EMPNM ,EP_DPTNM,CMT_CODDS ";
			M_strSQLQRY+= " from HR_EXTRN,HR_EPMST,CO_CDTRN ";
			M_strSQLQRY+= " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO=EX_EMPNO AND EP_CMPCD=EX_CMPCD AND isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and CMT_CGMTP='STS' AND CMT_CGSTP='HRXXEXP' AND CMT_CODCD=EX_STSFL AND EX_STSFL='0'";
			if(!flgSICCHK)
			{
				M_strSQLQRY+= " and ep_empno in (select distinct SUBSTRING(cmt_codcd,1,4) from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(cmt_codcd,6,4) ='"+cl_dat.M_strEMPNO_pbst+"'";
				M_strSQLQRY+= " and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN','HR"+cl_dat.M_strCMPCD_pbst+"EXA')) ";
			}
			M_strSQLQRY+= " order by EX_DOCDT,EX_EMPNO ";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_tmsINCTM=M_rstRSSET.getTimestamp("EX_EINTM");
					L_tmsOUTTM=M_rstRSSET.getTimestamp("EX_EOTTM");
					tblEXPDTL.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")),L_ROWNO,TB1_DOCDT);	
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_EMPNO"),""),L_ROWNO,TB1_EMPNO);
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_EMPNM"),""),L_ROWNO,TB1_EMPNM);
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""),L_ROWNO,TB1_DPTNM);
					tblEXPDTL.setValueAt(L_tmsOUTTM==null ? "" : M_fmtLCDTM.format(L_tmsOUTTM).substring(11),L_ROWNO,TB1_OUTTM);
					tblEXPDTL.setValueAt(L_tmsINCTM==null ? "" : M_fmtLCDTM.format(L_tmsINCTM).substring(11),L_ROWNO,TB1_INCTM);
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_DOCNO"),""),L_ROWNO,TB1_DOCNO);
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_SHFCD"),""),L_ROWNO,TB1_WRKSH);
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_REMDS"),""),L_ROWNO,TB1_REMDS);
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_OFPFL"),""),L_ROWNO,TB1_OFPFL);
					tblEXPDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),L_ROWNO,TB1_STSFL);
					L_ROWNO ++;
				}
			}
				
			M_rstRSSET.close();
				
			M_strSQLQRY= " SELECT EX_DOCDT,EX_EMPNO,EX_DOCNO,EX_REMDS,EX_EOTTM,EX_EINTM,EX_AOTTM,EX_OFPFL,";
			M_strSQLQRY+= " EX_AUTBY,EX_SHFCD,EX_STSFL,rtrim(ltrim(isnull(ep_lstnm,' '))) + ' '  + left(isnull(ep_fstnm,' '),1)+'.'+ left(isnull(ep_mdlnm,' '),1)+'.'  EP_EMPNM ,EP_DPTNM,CMT_CODDS";
			M_strSQLQRY+= " from HR_EXTRN,HR_EPMST,CO_CDTRN";
			M_strSQLQRY+= " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO=EX_EMPNO AND EP_CMPCD=EX_CMPCD AND isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null and CMT_CGMTP='STS' AND CMT_CGSTP='HRXXEXP' AND CMT_CODCD=EX_STSFL AND EX_STSFL='1'";
			if(!flgSICCHK)
			{
				M_strSQLQRY+= " and ep_empno in (select distinct SUBSTRING(cmt_codcd,1,4) from co_cdtrn where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and SUBSTRING(cmt_codcd,6,4) ='"+cl_dat.M_strEMPNO_pbst+"'";
				M_strSQLQRY+= " and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN','HR"+cl_dat.M_strCMPCD_pbst+"EXA')) ";
			}
			M_strSQLQRY+= " and CONVERT(varchar,EX_DOCDT,1) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().toString()))+"' ";
			M_strSQLQRY+= " AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().toString()))+"'";
			M_strSQLQRY += " order by EX_DOCDT,EX_EMPNO";
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_tmsINCTM=M_rstRSSET.getTimestamp("EX_EINTM");
					L_tmsOUTTM=M_rstRSSET.getTimestamp("EX_EOTTM");
					tblEXPDTL1.setValueAt(M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")),L_ROWNO1,TB1_DOCDT);	
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_EMPNO"),""),L_ROWNO1,TB1_EMPNO);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_EMPNM"),""),L_ROWNO1,TB1_EMPNM);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""),L_ROWNO1,TB1_DPTNM);
					tblEXPDTL1.setValueAt(L_tmsOUTTM==null ? "" : M_fmtLCDTM.format(L_tmsOUTTM).substring(11),L_ROWNO1,TB1_OUTTM);
					tblEXPDTL1.setValueAt(L_tmsINCTM==null ? "" : M_fmtLCDTM.format(L_tmsINCTM).substring(11),L_ROWNO1,TB1_INCTM);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_DOCNO"),""),L_ROWNO1,TB1_DOCNO);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_SHFCD"),""),L_ROWNO1,TB1_WRKSH);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_REMDS"),""),L_ROWNO1,TB1_REMDS);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_OFPFL"),""),L_ROWNO1,TB1_OFPFL);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("EX_AUTBY"),""),L_ROWNO1,TB1_AUTBY);
					tblEXPDTL1.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),L_ROWNO1,TB1_STSFL);
					L_ROWNO1 ++;
					
				}
			}
			M_rstRSSET.close();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getEXPDTL");
		}
		
	}
	
	
	private void inlTBLEDIT(JTable P_tblTBLNM)
	{
		if(!P_tblTBLNM.isEditing())
			return;
		P_tblTBLNM.getCellEditor().stopCellEditing();
		P_tblTBLNM.setRowSelectionInterval(0,0);
		P_tblTBLNM.setColumnSelectionInterval(0,0);
	}
	
	private void setSHIFT(String LP_EMPNO,String LP_DOCDT) 
	{
		try
		{ 
			M_strSQLQRY= " SELECT SS_CURSH from HR_SSTRN where SS_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' AND ss_wrkdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_DOCDT))+"' and SS_EMPNO = '"+LP_EMPNO+"'";
			//System.out.println(">>>select>>"+ M_strSQLQRY );
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null && M_rstRSSET.next())
				if(txtSHFCD.getText().length()==0)
					txtSHFCD.setText(M_rstRSSET.getString("SS_CURSH"));
			M_rstRSSET.close();
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setSHIFT()"); 
		}
	}
	
	/**method to generate save record of Exit Pass Entry in respective text box. **/
	private void getDATA() 
	{
		try
		{ 
			String L_strEOTTM="";
			String L_strEINTM="";
			java.sql.Timestamp L_tmpEOTTM; 
			java.sql.Timestamp L_tmpEINTM;

			String L_strEMPNO="";
			String L_strEMPDL="";
			M_strSQLQRY= " SELECT EX_DOCDT,EX_EMPNO,EX_REMDS,EX_EOTTM,EX_OFPFL,EX_EINTM,EX_AUTBY,EX_SHFCD,EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM,EP_DPTNM,EP_DPTCD from HR_EXTRN,HR_EPMST where";
			M_strSQLQRY+= " EP_CMPCD=EX_CMPCD and EP_EMPNO=EX_EMPNO and EP_STSFL<>'U' and EP_LFTDT is null  and EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EX_DOCNO = '"+txtDOCNO.getText()+"'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				M_strSQLQRY+= " AND EX_STSFL in('0','1')";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				M_strSQLQRY+= " and isnull(EX_STSFL,'')<>'X'";
			System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY); 
			if(M_rstRSSET != null && M_rstRSSET.next())
			{
				/*if(!(M_rstRSSET.getDate("EX_DOCDT")==null))
					txtDOCDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")));
				else
					txtDOCDT.setText("");

				//L_strEMPNO=nvlSTRVL(M_rstRSSET.getString("EX_EMPNO"),"");
				//txtEMPNO.setText(L_strEMPNO);*/
				txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("EX_REMDS"),""));

				L_tmpEOTTM = M_rstRSSET.getTimestamp("EX_EOTTM");
				if(L_tmpEOTTM != null)
				{
					L_strEOTTM = M_fmtLCDTM.format(L_tmpEOTTM);
					if(L_strEOTTM.toString().length()>=11)
						txtEOTTM.setText(L_strEOTTM.substring(11));
				}
				L_tmpEINTM = M_rstRSSET.getTimestamp("EX_EINTM");
				if(L_tmpEINTM != null)
				{
					L_strEINTM = M_fmtLCDTM.format(L_tmpEINTM);
					if(L_strEINTM.toString().length()>=11)
						txtEINTM.setText(L_strEINTM.substring(11));
				}
				txtAUTBY.setText(nvlSTRVL(M_rstRSSET.getString("EX_AUTBY"),""));
				txtSHFCD.setText(nvlSTRVL(M_rstRSSET.getString("EX_SHFCD"),""));
				if(nvlSTRVL(M_rstRSSET.getString("EX_OFPFL"),"").equals("P"))
					rdbPERSL.setSelected(true);
				else
					rdbOFICL.setSelected(false);

				if(nvlSTRVL(M_rstRSSET.getString("EX_OFPFL"),"").equals("O"))
					rdbOFICL.setSelected(true);
				else
					rdbOFICL.setSelected(false);
				
				//txtEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
				//txtDPTCD.setText(M_rstRSSET.getString("EP_DPTNM"));
			}
			M_rstRSSET.close();
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA()"); 
		}
	}

	/**Validation for Exit Pass Entry tab**/
	boolean vldDATA()
	{
		cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			if(txtEMPNO.getText().trim().length() ==0)
			{
				txtEMPNO.requestFocus();
				setMSG("Enter the Employee No",'E');
				return false;
			}
			else if(txtDOCDT.getText().trim().length() ==0)
			{
				txtDOCDT.requestFocus();
				setMSG("Enter the DOC Date",'E');
				return false;
			}
			else if(txtREMDS.getText().trim().length() ==0)
			{
				txtREMDS.requestFocus();
				setMSG("Enter the Purpose",'E');
				return false;
			}
			else if(txtSHFCD.getText().trim().length() ==0)
			{
				txtSHFCD.requestFocus();
				setMSG("Enter the Shift",'E');
				return false;
			}
			else if(txtEOTTM.getText().trim().length() ==0)
			{
				txtEOTTM.requestFocus();
				setMSG("Enter the Out Time",'E');
				return false;
			}
			else if((rdbOFICL.isSelected()==false) && (rdbPERSL.isSelected()==false))
			{
				rdbPERSL.requestFocus();
				setMSG("Select either Personal or Official Button",'E');
				return false;
			}

		}
		catch(Exception E_VR)
		{
			setMSG(E_VR,"vldDATA()");		
		}
		return true;
	}
/** method to click on Save button to save, update, delete, Enqiry & authorization ***/
	void exeSAVE()
	{
		try
		{
			
			if(jtpMANTAB.getSelectedIndex()== 0)
			{	
				if(!vldDATA()) 
				{
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
				{
					exeADDREC();
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))	
				{
					//exeMODREC();
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))	
				{
					exeDELREC();
				}
	
				if(cl_dat.exeDBCMT("exeSAVE")) 
				{
					if(!(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst)))
					{
						String L_strDOCNO=txtDOCNO.getText();
						String L_strDOCDT=txtDOCDT.getText();
						clrCOMP();
						txtDOCNO.setText(L_strDOCNO);
						txtDOCDT.setText(L_strDOCDT);
					}
					else
						clrCOMP();
				}
			}
			if(jtpMANTAB.getSelectedIndex()== 1)
			{	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))	
					exeAUTREC();
				
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
		}
	}


	/**update Exit Pass Entry Record with status '1' in Authorization tab **/
	private void exeAUTREC()
	{ 
		try
		{
				boolean flgSELRW=false;////flag to check whether atleast 1 row from tblTEEXR1 is selected
				for(int P_intROWNO=0;P_intROWNO<tblEXPDTL.getRowCount();P_intROWNO++)
				{
					if(tblEXPDTL.getValueAt(P_intROWNO,TB1_CHKFL).toString().equals("true"))
					{
						flgSELRW=true;
					}
				}
				if(flgSELRW==false)
					setMSG("Please Select atleast 1 row from the Table",'E');
				else
				{	
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_flgLCUPD_pbst = true;	
					for(int i=0;i<tblEXPDTL.getRowCount();i++)
					{
					    if(tblEXPDTL.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
					    {
							M_strSQLQRY ="UPDATE HR_EXTRN SET ";
							M_strSQLQRY += " EX_STSFL = '1',EX_AUTBY ='"+cl_dat.M_strUSRCD_pbst+"' ";
							M_strSQLQRY += " where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  EX_DOCNO = '" + tblEXPDTL.getValueAt(i,TB1_DOCNO).toString().trim()+"'";
							//System.out.println(">>>UPDATE>>"+M_strSQLQRY);
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							
					    }
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Record Updated Successfully...",'N');	
						tblEXPDTL.clrTABLE();
						inlTBLEDIT(tblEXPDTL);
						getEXPDTL();		 /**fetch Exit pass entry & authorized data after that update.**/		
					}	
									
				}
				
		}
		catch(Exception E)
		{
			setMSG(E," Error In exeAUTREC....");
		}
	}
	
	/**method to insert record in hr_extrn database table.& update Doc No**/
	private void exeADDREC()
	{ 
		try
		{
			//this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			String L_strTEMP1 = "";
			String strDOCNO = "";			
			String L_strSBSCD = "";
			int L_intTSTNO =0;

			M_strSQLQRY = " Select CMT_CCSVL from CO_CDTRN where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='HRXXEXP' AND CMT_CODCD='"+cl_dat.M_strFNNYR_pbst.substring(3,4)+(rdbOFICL.isSelected()?"01":"02")+"' AND isnull(CMT_STSFL,'')<>'X'";
			//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			if(M_rstRSSET!= null)
			{				
				if(M_rstRSSET.next())				
					L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),""); 		
				M_rstRSSET.close();
			}	
			
			L_intTSTNO = Integer.valueOf(L_strTEMP1).intValue()+1;
		
			strDOCNO = cl_dat.M_strFNNYR_pbst.substring(3,4)+(rdbPERSL.isSelected() ? "02" : "01")
			+ "00000".substring(0,5 - String.valueOf(L_intTSTNO).length())
			+String.valueOf(L_intTSTNO);	
			txtDOCNO.setText(strDOCNO);	
			
           M_strSQLQRY =" insert into HR_EXTRN (EX_DOCNO,EX_DOCDT,EX_EMPNO,EX_OFPFL,EX_AUTBY,EX_EOTTM,EX_EINTM,"
				+"EX_TRNFL,EX_STSFL,EX_LUSBY,EX_LUPDT,EX_CMPCD,EX_REMDS,EX_SHFCD,EX_SBSCD,EX_GENTM)"
				+"  VALUES('" + strDOCNO +"',";

			if(txtDOCDT.getText().trim().length()>0)
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDOCDT.getText()))+"',";
			else 
				M_strSQLQRY += "null,";	 

			M_strSQLQRY += "'"+txtEMPNO.getText()+"',";
			M_strSQLQRY += "'"+(rdbOFICL.isSelected()?"O":"P")+"',"; 
			M_strSQLQRY += "'"+txtAUTBY.getText()+"',";
			M_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDOCDT.getText()+" "+txtEOTTM.getText()))+"',";
			
			if(txtEINTM.getText().trim().length()>0)  //if not considered then parse error
				M_strSQLQRY += "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDOCDT.getText()+" "+txtEINTM.getText()))+"',";
			else 
				M_strSQLQRY += "null,";		
			
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'0',";
			//M_strSQLQRY += "'1',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"',";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+txtREMDS.getText().trim()+"',";
			M_strSQLQRY += "'"+txtSHFCD.getText().trim()+"',";			
			M_strSQLQRY += "'"+M_strSBSCD+"',current_timestamp)";
			
			// System.out.println(">>>Insert>>"+ M_strSQLQRY );
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");

			if(cl_dat.M_flgLCUPD_pbst == true)
			{
				JOptionPane.showMessageDialog(this,"Please Note the DOC Number\n"+strDOCNO,"",JOptionPane.ERROR_MESSAGE);
				M_strSQLQRY = "Update CO_CDTRN set CMT_CCSVL ='"+ strDOCNO.substring(3) +"' where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='HRXXEXP' ";
				M_strSQLQRY+=" AND CMT_CODCD='"+cl_dat.M_strFNNYR_pbst.substring(3,4)+(rdbPERSL.isSelected() ? "02" : "01")+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}

			setCursor(cl_dat.M_curDFSTS_pbst);

		}

		catch(Exception L_EX)
		{
			//L_EX.printStackTrace();
			setMSG(L_EX,"exeADDREC()"); 
		}
	}
	
/** Method to delete record**/
	private void exeDELREC() 
	{ 
	  try
	  {
		  	M_strSQLQRY = "UPDATE HR_EXTRN SET";	
			M_strSQLQRY +="	EX_STSFL='X'";	
			M_strSQLQRY +=" where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY +=" AND EX_DOCNO='"+txtDOCNO.getText().toString()+"'";
		
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			//System.out.println(">>>Delete>>"+M_strSQLQRY);
	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"exeDELREC()");		
	  }
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

	/**Verify Doc No, Emp No, Shift, Out time ,In time & Auth by to enter valid data***/
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{

				if(((JTextField)input).getText().length() == 0)
					return true;
				if(jtpMANTAB.getSelectedIndex()== 0)
				{
					if(input == txtEMPNO)
					{	
						try{
							M_strSQLQRY = " select EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM,EP_DPTNM from HR_EPMST where  isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
							M_strSQLQRY+=" and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(exeSICCHK(cl_dat.M_strEMPNO_pbst) ? "" : " and ep_dptcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN') and SUBSTRING(cmt_codcd,6,4)='"+cl_dat.M_strEMPNO_pbst+"'))");
							M_strSQLQRY += " AND EP_EMPNO = '"+txtEMPNO.getText().trim()+"'";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
	
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
								txtDPTCD.setText(M_rstRSSET.getString("EP_DPTNM"));
	
							}
							else
							{
								setMSG("Enter valid Employee No of Your Dept ",'E');
								return false;
							}
							M_rstRSSET.close();
						}
						catch(Exception e)
						{
							//e.printStackTrace();
							setMSG(e,"error in InputVerifier ");
						}
	
					}
					else if(input == txtDOCNO)
					{
						try
						{
							if(txtDOCNO.getText().length()>0)
							{
								M_strSQLQRY=" SELECT EX_DOCNO,EX_DOCDT,EX_OFPFL from HR_EXTRN  ";
								M_strSQLQRY+=" where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'";
								M_strSQLQRY+=" and EX_EMPNO='"+txtEMPNO.getText()+"'";
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
									M_strSQLQRY+= " and EX_STSFL ='0'";
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
									M_strSQLQRY+= " and isnull(EX_STSFL,'')<>'X'";
								M_strSQLQRY += " AND EX_DOCNO ='"+txtDOCNO.getText().trim()+"'";	
								//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
								M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
								if(M_rstRSSET.next() && M_rstRSSET!=null)
								{
									txtDOCDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")));
									getDATA();
								}
								else
								{
									setMSG("Enter Valid Doc No",'E');
									return false;
								}
								M_rstRSSET.close();
							}
						}
						catch(Exception e)
						{
							setMSG(e,"error in InputVerifier1 ");
						}
					}
					
					else if(input == txtSHFCD)
					{
						try
						{
							M_strSQLQRY = " select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='M"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='COXXSHF' and cmt_codcd='"+txtSHFCD.getText().trim().toUpperCase()+"' ";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET.next() && M_rstRSSET!=null)
							{
								txtSHFCD.setText(txtSHFCD.getText().trim().toUpperCase());
							}
							else
							{
								setMSG("enter valid Work Shift",'E');
								return false;
							}
							M_rstRSSET.close();
						}
						catch(Exception e)
						{
							setMSG(e,"error in InputVerifier3 ");
						}
					}
					
					
					else if(input == txtEINTM)
					{
						if(txtDOCDT.getText().trim().length()>0)
						{
							if(txtEINTM.getText().trim().length()==0)
							{
								return true;
							}
							else if(M_fmtLCDTM.parse(txtDOCDT.getText().trim()+" "+txtEINTM.getText().trim()).compareTo(M_fmtLCDTM.parse(txtDOCDT.getText().trim()+" "+txtEOTTM.getText().trim()))<=0)
							{
								txtEINTM.requestFocus();
								setMSG("In time must be greater than Out Time",'E');
								return false;
							}
							
						}
					}
	
					else if(input == txtEOTTM)
					{	
						if(txtDOCDT.getText().trim().length()>0)
						{
							if(txtEOTTM.getText().trim().length()==0)
							{
								txtEOTTM.requestFocus();
								setMSG("OUT time can not be empty",'E');						
								return false;
							}
							
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

}










