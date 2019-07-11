/*
System Name   : Finished Goods Inventory Management System
Program Name  : Codes Master Entry Form
Program Desc. : Entry for new codes within Masters is done through this form.
Author        : Mr.S.R.Mehesare
Date          : 6th May 2005
Version       : v2.0.0

Modificaitons  : 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import javax.swing.DefaultCellEditor;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableColumnModel;import javax.swing.InputVerifier;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.table.*;import javax.swing.JComponent;import javax.swing.JTable;
import java.awt.event.*;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.Color;

/**
<P><PRE style = font-size : 10 pt >
<b>System Name :</b> Finished Goods Inventory Management System.
 
<b>Program Name :</b> Codes Master Entry Form

<b>Purpose :</b> Entry for New codes is done through this form.These codes are 
used in other entry screens to avoid hardcoding of the codes.

List of tables used :
Table Name   Primary key                         Operation done
                                             Insert  Update  Query  Delete	
-----------------------------------------------------------------------------
CO_CDMST     CM_CGMTP,CM_CGSTP                 #        #      #
CO_CDTRN     CMT_CGMTP,CMT_CGSTP,CMT_CODCD     #        #      #       #
-----------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name Column Name   Table name         Type/Size    Description
-----------------------------------------------------------------------------
txtCGMTP   CMT/CM_CGMTP  CO_CDMST/CO_CDTRN  VARCHAR(3)   Main group	
txtCGSTP   CMT/CM_CGMTP  CO_CDMST/CO_CDTRN  VARCHAR(7)   Sub group	
txtCGRDS   CMT/CM_CGMTP  CO_CDMST/CO_CDTRN  VARCHAR(15)  Code Description
txtMODFL   CMT/CM_CGMTP  CO_CDMST/CO_CDTRN  VARCHAR(1)   Modification Flag
txtCODLN   CMT/CM_CGMTP  CO_CDMST/CO_CDTRN  VARCHAR(2)   Code Length
txtCODRM   CMT/CM_CGMTP  CO_CDMST/CO_CDTRN  VARCHAR(40)  Remarks
txtAPPLS   CMT/CM_CGMTP  CO_CDMST/CO_CDTRN  VARCHAR(200) Application Description

TB_CODCD   CM_CODCD      CO_CDTRN           VARCHAR(15)   Code	
TB_CODDS   CM_CODDS      CO_CDTRN           VARCHAR(30)   Description	
TB_SHRDS   CM_SHRDS      CO_CDTRN           VARCHAR(15)   Short Description
TB_CHP01   CM_CHP01      CO_CDTRN           VARCHAR(20)   Charector Parameter1
TB_CHP02   CM_CHP02      CO_CDTRN           VARCHAR(20)   Charector Parameter2
TB_NMP01   CM_NMP01      CO_CDTRN           DECIMAL(12,3) Numeric Parameter1
TB_NMP02   CM_NMP02      CO_CDTRN           DECIMAL(12,3) Numeric Parameter2  
TB_NCSVL   CM_NCSVL      CO_CDTRN           DECIMAL(12,3) Numeric Constant 
TB_CCSVL   CM_CCSVL      CO_CDTRN           VARCHAR(15)   Charector Constant
TB_MODLS   CM_MODLS      CO_CDTRN           VARCHAR(200)  Module List
TB_STSFL   CM_STSFL      CO_CDTRN           VARCHAR(1)    Status Flag
-----------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description  Display Columns     Table Name
-----------------------------------------------------------------------------
txtCGMTP   Main group	         CMT/CM_CGMTP        CO_CDMST/CO_CDTRN
txtCGSTP   Sub group	         CMT/CM_CGMTP        CO_CDMST/CO_CDTRN
-----------------------------------------------------------------------------

Tables Used For Query :</b>1)CO_CDMST             
                       2)CO_CDTRN                       
<B>Queries:</b>
<I><B>To insert OR modify Record :</B> 
     At First the Main Group & sub Group is checked into the data base.
         if Data not found then new Main group & subgroup is inserted 
         if found the corresponding Data is updated.
     If the master entry successful the transaction data is also inserted.
         The same logic is used to manage insertion & modification of records.

<B>To Delete Records : </B>
       i) For Deletation Status flag is updated to 'X'.
       ii) If all Records from Transaction Table are marked as 'X' & Successful, then 
          Corresponding Master Record is also marked as 'X' for Deletion.
</I>
Validations : 
   - For new addition Main group & within that main group subgroup must be unique.
   - Modification of main group, subgroup & code is not allowed.  
*/

class co_mecdm extends cl_pbase implements KeyListener, FocusListener
{									/** JTextField to display & enter Main Group.*/
	private JTextField txtCGMTP;	/** JTextField to display & enter Sub Group.*/
	private JTextField txtCGSTP;	/** JTextField to display & enter Group Description.*/
	private JTextField txtCGRDS;	/** JTextField to display & enter Modification Flag.*/
	private JTextField txtMODFL;	/** JTextField to display & enter Code Length.*/
	private JTextField txtCODLN;	/** JTextField to display & enter Code Remarks.*/
	private JTextField txtCODRM;	/** JTextField to display & enter Application List.*/
	private JTextField txtAPPLS;	/** JTextField to display & enter Code Details.*/
	private cl_JTable tblITMDT;	
									/** JTextField to apply validity checks while entering Code in the JTable.*/
	private JTextField txtCODCD;	/** JTextField to apply validity checks while entering Code Description in the JTable.*/
	private JTextField txtCODDS;	/** JTextField to apply validity checks while entering Short Description in the JTable.*/
	private JTextField txtSHRDS;	/** JTextField to apply validity checks while entering Charector Constant value in the JTable.*/
	private JTextField txtCCSVL;	/** JTextField to apply validity checks while entering Charector Parameter 1 in the JTable.*/
	private JTextField txtCHP01;	/** JTextField to apply validity checks while entering Charector Parameter 2 in the JTable.*/
	private JTextField txtCHP02;	/** JTextField to apply validity checks while entering Numeric Parameter1 in the JTable.*/
	private JTextField txtNMP01;	/** JTextField to apply validity checks while entering Numeric Parameter2 in the JTable.*/
	private JTextField txtNMP02;	/** JTextField to apply validity checks while entering Numeric Constant  in the JTable.*/
	private JTextField txtNCSVL;	/** JTextField to apply validity checks while entering Status Flag in the JTable.*/
	private JTextField txtSTSFL;	/** JTextField to apply validity checks while entering Modul List in the JTable.*/
	private JTextField txtMODLS;
									/** String variable for Code Group Main Type.*/
	private String strCGMTP;		/** String variable for Code Group sub Type.*/
	private String strCGSTP;		/** String variable for Code Group Description.*/
	private String strCGRDS;		/** String variable for Code Modification Flag.*/
	private String strMODFL;		/** String variable for Code Length.*/
	private String strCODLN;		/** String variable for Code Remarks.*/
	private String strCODRM;		/** String variable for Application Length.*/
	private String strAPPLS;		/** String variable for Code Length.*/
	private String strDBCDL;		/** String variable for Status Flag.*/
	private String strSTSFL;
									/** String variable for Code.*/
	private String strCODCD;		/** String variable for Code Description.*/
	private String strCODDS;		/** String variable for Short Description.*/
	private String strSHRDS;		/** String variable for Charector Parameter1.*/
	private String strCHP01;		/** String variable for Charector Parameter2.*/
	private String strCHP02;		/** String variable for Numerical Parameter1.*/
	private String strNMP01;		/** String variable for Numerical Parameter2.*/
	private String strNMP02;		/** String variable for Numerical Constant.*/
	private String strNCSVL;		/** String variable for Charector Constant*/
	private String strCCSVL;		/** String variable for Module List.*/
	private String strMODLS;		/** Integer variable to count number of Records fetched.*/
	private int intRECCT;
										/** Final Integer variable to Represent Check Flag Column.*/
    private final int TB_CHKFL = 0;		/** Final Integer variable to Represent Code Column.*/
    private final int TB_CODCD = 1;		/** Final Integer variable to Represent Code Description Column.*/
    private final int TB_CODDS = 2;		/** Final Integer variable to Represent Short Description Column.*/
    private final int TB_SHRDS = 3;		/** Final Integer variable to Represent Charector Parameter1 Column.*/
	private final int TB_CHP01 = 4;		/** Final Integer variable to Represent Charector Parameter2 Column.*/
    private final int TB_CHP02 = 5;		/** Final Integer variable to Represent Numeric Parameter1 Column.*/
    private final int TB_NMP01 = 6;		/** Final Integer variable to Represent Numeric Parameter2 Column.*/
    private final int TB_NMP02 = 7;		/** Final Integer variable to Represent Numeric Constant Column.*/
    private final int TB_NCSVL = 8;		/** Final Integer variable to Represent Charector Constant Column.*/
    private final int TB_CCSVL = 9;		/** Final Integer variable to Represent Module List Column.*/
    private final int TB_MODLS = 10;	/** Final Integer variable to Represent Ststus Flag Column.*/
	private final int TB_STSFL = 11;    
										/**	Integer variable to assign & display Serial Number for Codes.*/
	private int intSRLNO = 0;			/** Innear class for Table Input Data Verification.*/
	private TableInputVerifier TBLINPVF;/** Innear class object for Table Input Data Verification.*/
	private TBLINPVF objTBLVRF;			/** Input varifier for master data validity Check.*/
	private INPVF objINPVR = new INPVF();
	private final int intROWCT_fn = 400;
	co_mecdm()
	{
		super(1);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Main Group"),2,1,1,.8,this,'R');
			add(txtCGMTP = new TxtLimit(3),2,2,1,1,this,'L');
			add(new JLabel("Sub Group"),2,3,1,.9,this,'R');
			add(txtCGSTP = new TxtLimit(7),2,4,1,1,this,'L');
			add(new JLabel("Description"),2,5,1,.8,this,'R');
			add(txtCGRDS = new TxtLimit(30),2,6,1,2.7,this,'L');			
			
			add(new JLabel("Mod.Flag"),3,1,1,.8,this,'R');
			add(txtMODFL = new TxtLimit(3),3,2,1,1,this,'L');
			add(new JLabel("Code Length"),3,3,1,.9,this,'R');
			add(txtCODLN = new TxtLimit(7),3,4,1,1,this,'L');
			add(new JLabel("Appl. Desc"),3,5,1,.8,this,'R');
			add(txtAPPLS = new TxtLimit(30),3,6,1,2.7,this,'L');
			
			add(new JLabel("Remark"),4,1,1,.8,this,'R');
			add(txtCODRM = new TxtLimit(40),4,2,1,5,this,'L');
			
			String[] L_COLHD = {"Select","Code","Description","Short Desc","Ch.Para1","Ch.Para2,","N.Para1","N.Para2","N.const","Ch.const","Mod List","Flag"};
      		int[] L_COLSZ = {50,75,150,110,50,50,50,45,45,45,45,35};	    				
			tblITMDT = crtTBLPNL1(this,L_COLHD,intROWCT_fn,6,1,10,8,L_COLSZ,new int[]{0});				
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);	
         
			setMSG("Select an Option..",'N');
			
			objTBLVRF = new TBLINPVF();
			tblITMDT.setInputVerifier(objTBLVRF);		
			txtCGMTP.setInputVerifier(objINPVR);			
			txtCGSTP.setInputVerifier(objINPVR);
			tblITMDT.setCellEditor(TB_CODCD, txtCODCD = new TxtLimit(15));						
			tblITMDT.setCellEditor(TB_CODDS, txtCODDS = new TxtLimit(50));
			tblITMDT.setCellEditor(TB_SHRDS, txtSHRDS = new TxtLimit(15));
			tblITMDT.setCellEditor(TB_CHP01, txtCHP01 = new TxtLimit(20));
			tblITMDT.setCellEditor(TB_CHP02, txtCHP02 = new TxtLimit(20));
			tblITMDT.setCellEditor(TB_NMP01, txtNMP01 = new TxtNumLimit(12.3));
			tblITMDT.setCellEditor(TB_NMP02, txtNMP02 = new TxtNumLimit(12.3));
			tblITMDT.setCellEditor(TB_NCSVL, txtNCSVL = new TxtNumLimit(12.3));
			tblITMDT.setCellEditor(TB_CCSVL, txtCCSVL = new TxtLimit(15));
			tblITMDT.setCellEditor(TB_MODLS, txtMODLS = new TxtLimit(200));
			tblITMDT.setCellEditor(TB_STSFL, txtSTSFL = new TxtLimit(1));
			
			txtCODCD.addKeyListener(this);txtCODCD.addFocusListener(this);
			txtCODDS.addKeyListener(this);txtCODDS.addFocusListener(this);
			txtSHRDS.addKeyListener(this);txtSHRDS.addFocusListener(this);
			txtCHP01.addKeyListener(this);txtCHP01.addFocusListener(this);
			txtCHP02.addKeyListener(this);txtCHP02.addFocusListener(this);
			txtNMP01.addKeyListener(this);txtNMP01.addFocusListener(this);
			txtNMP02.addKeyListener(this);txtNMP02.addFocusListener(this);
			txtNCSVL.addKeyListener(this);txtNCSVL.addFocusListener(this);
			txtCCSVL.addKeyListener(this);txtCCSVL.addFocusListener(this);
			txtMODLS.addKeyListener(this);txtMODLS.addFocusListener(this);
			txtSTSFL.addKeyListener(this);txtSTSFL.addFocusListener(this);					
			
			cl_dat.M_flgHELPFL_pbst = false;
			setENBL(false);	
			tblITMDT.setSelectionForeground(Color.blue);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
	}	
	/**
	 * Super class method override to inhance its functionality according to Requriment.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		clrCOMP();
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)	
		{
			intSRLNO = 0;
			txtCGMTP.setEnabled(true);
			txtCGSTP.setEnabled(true);	
		}				
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			tblITMDT.cmpEDITR[TB_CHKFL].setEnabled(false);
		else
			tblITMDT.cmpEDITR[TB_CHKFL].setEnabled(true);		
		
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{			
			txtCGRDS.setEnabled(true);
			txtMODFL.setEnabled(true);
			txtCODLN.setEnabled(true);
			txtCODRM.setEnabled(true);
			txtAPPLS.setEnabled(true);
			
			tblITMDT.cmpEDITR[TB_CODCD].setEnabled(true);						
			tblITMDT.cmpEDITR[TB_CODDS].setEnabled(true);									
			tblITMDT.cmpEDITR[TB_SHRDS].setEnabled(true);
			tblITMDT.cmpEDITR[TB_CHP01].setEnabled(true);
			tblITMDT.cmpEDITR[TB_CHP02].setEnabled(true);
			tblITMDT.cmpEDITR[TB_NMP01].setEnabled(true);
			tblITMDT.cmpEDITR[TB_NMP02].setEnabled(true);
			tblITMDT.cmpEDITR[TB_NCSVL].setEnabled(true);
			tblITMDT.cmpEDITR[TB_CCSVL].setEnabled(true);
			tblITMDT.cmpEDITR[TB_MODLS].setEnabled(true);
			tblITMDT.cmpEDITR[TB_STSFL].setEnabled(true);					
		}			
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);						
			if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				setENBL(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)					
				{
					txtCGMTP.requestFocus();					
					setMSG("Enter Main Group OR Press F1 for Help..",'N');										
				}
				else
				{					
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					setMSG("Select an Option..",'N');
				}				
			}			
			if(M_objSOURC == txtCGSTP)
			{	
				txtCGSTP.setText(strCGSTP.toUpperCase().trim());
				if((txtCGMTP.getText().trim().length()==3) && (txtCGSTP.getText().trim().length()==7))
				{
					M_strSQLQRY = "Select CM_CGSTP from CO_CDMST where ";
					M_strSQLQRY += " CM_CGMTP='"+txtCGMTP.getText().trim().toUpperCase()+"'";
					M_strSQLQRY += " AND CM_CGSTP='"+txtCGSTP.getText().trim().toUpperCase()+"'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(M_rstRSSET != null)
					{											
						if(M_rstRSSET.next())
						{	
							M_rstRSSET.close();
							getDATA();
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
								txtCGRDS.requestFocus();
							else
								cl_dat.M_btnSAVE_pbst.requestFocus();
						}
						else						
							M_rstRSSET.close();						
					}											
				}
				else
				{
					setMSG("Main Group OR Sub Group Code Cannot be blank..",'E');
					return;
				}																								
			}
			if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
			{
				setENBL(false);
				txtCGMTP.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	
	public void focusGained(FocusEvent L_FE)
	{			
		super.focusGained(L_FE);				
		if(M_objSOURC == tblITMDT.cmpEDITR[TB_CODCD])
	    {								
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))
			{																		
				if(intSRLNO > tblITMDT.getSelectedRow())
					 ((JTextField)tblITMDT.cmpEDITR[TB_CODCD]).setEditable(false);					 								
				else								
					((JTextField)tblITMDT.cmpEDITR[TB_CODCD]).setEditable(true);				
			}
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);			
			strCGMTP = txtCGMTP.getText().toString().trim().toUpperCase();
			strCGSTP = txtCGSTP.getText().toString().trim().toUpperCase();
			strCGRDS = txtCGRDS.getText().toString().trim();
			strCODLN = txtCODLN.getText().toString().trim();			
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC == txtCGMTP)
				{					
					txtCGSTP.setText("");
					txtCGMTP.setText((txtCGMTP.getText().trim()).toUpperCase());
					try
					{
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtCGMTP";
						String L_ARRHDR[] = {"Main Group"};
						M_strSQLQRY = "Select distinct CM_CGMTP from CO_CDMST";
						if((txtCGMTP.getText().trim()).length()>0)
							M_strSQLQRY += " where CM_CGMTP like '"+ txtCGMTP.getText().trim()+ "%'";
						M_strSQLQRY += " order by CM_CGMTP";													
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");						
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"hlpCGMTP");
					}
				}
				else if(M_objSOURC == txtCGSTP)
				{									
					if((txtCGMTP.getText().trim()).length()==0)
					{
						setMSG("Please Enter Main group first..",'E');
						return;
					}
					txtCGSTP.setText((txtCGSTP.getText().trim()).toUpperCase());
					try
					{
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtCGSTP";
						String L_ARRHDR[] = {"Sub Group","Description"};
						M_strSQLQRY = "Select CM_CGSTP,CM_CGRDS from CO_CDMST";
						M_strSQLQRY += " where CM_CGMTP='"+strCGMTP+"'";
						if((txtCGSTP.getText().trim()).length()>0)	
							M_strSQLQRY += " AND CM_CGSTP like '"+txtCGSTP.getText().trim().toUpperCase()+"%'";
						M_strSQLQRY += " order by CM_CGSTP";						
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");						
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"hlpCGMTP");
					}
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if (M_objSOURC == txtCGMTP)
				{						
					txtCGSTP.setText("");
					txtCGRDS.setText("");
					txtMODFL.setText("");
					txtCODLN.setText("");
					txtCODRM.setText("");
					txtAPPLS.setText("");
					intSRLNO = 0;
					tblITMDT.clrTABLE();
					if(txtCGMTP.getText().trim().length() == 0)
					{						
						setMSG("Main Group cannot be blank..",'N');
						return;
					}
					txtCGMTP.setText(strCGMTP.toUpperCase().trim());					
					txtCGSTP.requestFocus();					
				}
				else if(M_objSOURC == txtCGSTP)
				{											
					txtMODFL.setText("");
					txtCODLN.setText("");
					txtCODRM.setText("");
					txtAPPLS.setText("");
					intSRLNO = 0;
					tblITMDT.clrTABLE();
					if(txtCGSTP.getText().trim().length() == 0)
					{
						setMSG("Subgroup cannot be blank..",'N');						
						return;
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						setMSG("Please Enter Description..",'N');
						txtCGRDS.requestFocus();
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						setMSG("Please Enter Modification Flag..",'N');
						txtMODFL.requestFocus();
					}
				}
				else if(M_objSOURC == txtCGRDS)
				{					
					txtMODFL.requestFocus();
					setMSG("Please Enter Code length..",'N');
				}										
				else if(M_objSOURC == txtMODFL)
				{
					txtCODLN.requestFocus();
					setMSG("Enter Code length..",'N');
				}
				else if(M_objSOURC == txtCODLN)
				{
					txtAPPLS.requestFocus();
					setMSG("Enter Application List",'N');
				}				
				else if(M_objSOURC == txtAPPLS)
				{	
					txtCODRM.requestFocus();
					setMSG("Please Enter Remarks if any..",'N');
				}
				else if(M_objSOURC == txtCODLN)
				{
					txtAPPLS.requestFocus();
					setMSG("Enter Application List..",'N');
				}
				else if(M_objSOURC == txtCODRM)
				{
					tblITMDT.requestFocus();
					setMSG("Enter Code & code details to add..",'N');
				}								
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}			
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtCGMTP"))
			{
				txtCGRDS.setText("");
				txtMODFL.setText("");
				txtCODLN.setText("");
				txtCODRM.setText("");
				txtAPPLS.setText("");
				intSRLNO = 0;
				tblITMDT.clrTABLE();
				txtCGMTP.setText((cl_dat.M_strHLPSTR_pbst).toUpperCase());				
				if(txtCGMTP.getText().trim().length()>0)
					txtCGSTP.requestFocus();				
				else
					txtCGMTP.requestFocus();
			}
			if(M_strHLPFLD.equals("txtCGSTP"))
			{
				txtMODFL.setText("");
				txtCODLN.setText("");
				txtCODRM.setText("");
				txtAPPLS.setText("");
				intSRLNO = 0;
				tblITMDT.clrTABLE();
				txtCGSTP.setText((cl_dat.M_strHLPSTR_pbst).toUpperCase());
				if(txtCGMTP.getText().trim().length()>0)
					txtCGSTP.requestFocus();
			}							
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Super class method overrided here to inhance the functionality of this method 
	 *AND to perform Data Input Output operation with the DataBase.
	 */
	void exeSAVE()
	{
		try
		{			
			int i = 0;
			this.setCursor(cl_dat.M_curWTSTS_pbst);			
			cl_dat.M_flgLCUPD_pbst = true;
			if(!vldDATA())
				return;
			
			strCGMTP = txtCGMTP.getText().toString().trim().toUpperCase();
			strCGSTP = txtCGSTP.getText().toString().trim().toUpperCase();
			strCGRDS = txtCGRDS.getText().toString().trim();
			strCODLN = txtCODLN.getText().toString().trim();
			strCODRM = txtCODRM.getText().toString().trim();
			strMODFL = txtMODFL.getText().toString().trim();
			strAPPLS = txtAPPLS.getText().toString().trim();			
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)))			
			{				
				M_strSQLQRY = "Select CM_CGMTP from CO_CDMST";
				M_strSQLQRY += " where CM_CGMTP = '"+txtCGMTP.getText().trim().toUpperCase()+"'";//strCGMTP+"'";
				M_strSQLQRY += " AND CM_CGSTP = '"+txtCGSTP.getText().trim().toUpperCase()+"'";//strCGSTP+"'";	
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
				if(M_rstRSSET != null)
				{											
					if(M_rstRSSET.next())
					{	
//						System.out.println("Main Gr, Sub Gr. Exists...");
						M_strSQLQRY = "Update CO_CDMST set";
						M_strSQLQRY += " CM_CGRDS = '"+strCGRDS+"',";
						M_strSQLQRY += " CM_CODRM = '"+strCODRM+"',";						
						M_strSQLQRY += " CM_TRNFL = '0',";
						M_strSQLQRY += " CM_MODFL = '"+ strMODFL +"',";
						M_strSQLQRY += " CM_APPLS = '"+ strAPPLS +"',";
						M_strSQLQRY += " CM_LUSBY ='" + cl_dat.M_strUSRCD_pbst+ "',";
						M_strSQLQRY += " CM_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"'";
						M_strSQLQRY += " where CM_CGMTP = '"+strCGMTP+"'";
						M_strSQLQRY += " AND CM_CGSTP = '"+strCGSTP+"'";
//						System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
//						System.out.println("Not Exists...");
						M_strSQLQRY = "Insert into CO_CDMST(CM_CGMTP,CM_CGSTP,CM_CGRDS,";
						M_strSQLQRY += "CM_CODLN,CM_MODFL,CM_APPLS,CM_CODRM,CM_TRNFL,";
						M_strSQLQRY += "CM_LUSBY,CM_LUPDT) values (";
						M_strSQLQRY += "'" + strCGMTP+ "',";
						M_strSQLQRY += "'" + strCGSTP+ "',";
						M_strSQLQRY += "'" + strCGRDS+ "',";
						M_strSQLQRY += strCODLN+ ",";
						M_strSQLQRY += "'" + strMODFL+ "',";
						M_strSQLQRY += "'" + strAPPLS+ "',";
						M_strSQLQRY += "'" + strCODRM+ "',";
						M_strSQLQRY += "'0',";
						M_strSQLQRY += "'" + cl_dat.M_strUSRCD_pbst+ "','";
						M_strSQLQRY += M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"')";
//						System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");			
					}					
					M_rstRSSET.close();
				}
				if(cl_dat.M_flgLCUPD_pbst == true)
				{
					for(i = 0;i < tblITMDT.getRowCount()-1;i++)
					{
						if(tblITMDT.getValueAt(i,TB_CHKFL).toString().trim().equals("true"))
						{
							strCODCD = tblITMDT.getValueAt(i,TB_CODCD).toString().trim().toUpperCase();
							strCODDS = tblITMDT.getValueAt(i,TB_CODDS).toString().trim();
							strSHRDS = tblITMDT.getValueAt(i,TB_SHRDS).toString().trim();
							strCHP01 = tblITMDT.getValueAt(i,TB_CHP01).toString().trim();
							strCHP02 = tblITMDT.getValueAt(i,TB_CHP02).toString().trim();
							strNMP01 = nvlSTRVL(tblITMDT.getValueAt(i,TB_NMP01).toString(),"0");
							strNMP02 = nvlSTRVL(tblITMDT.getValueAt(i,TB_NMP02).toString(),"0");
							strNCSVL = nvlSTRVL(tblITMDT.getValueAt(i,TB_NCSVL).toString(),"0");							
							strCCSVL = tblITMDT.getValueAt(i,TB_CCSVL).toString().trim();
							strMODLS = tblITMDT.getValueAt(i,TB_MODLS).toString().trim();
														
							M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN";
							M_strSQLQRY += " where CMT_CGMTP = '"+txtCGMTP.getText().trim().toUpperCase()+"'";
							M_strSQLQRY += " AND CMT_CGSTP = '"+txtCGSTP.getText().trim().toUpperCase()+"'";
							M_strSQLQRY += " AND CMT_CODCD = '"+strCODCD+"'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
							if(M_rstRSSET != null)
							{
								if((M_rstRSSET.next()) && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
								{		//if exists then modify	the Records in Transaction table.									
									M_strSQLQRY = "Update CO_CDTRN set ";
									M_strSQLQRY += "CMT_CODDS = '"+strCODDS+ "',";
									M_strSQLQRY += "CMT_SHRDS = '"+strSHRDS+ "',";
									M_strSQLQRY += "CMT_CHP01 = '"+strCHP01+ "',";
									M_strSQLQRY += "CMT_CHP02 = '"+strCHP02+ "',";
									M_strSQLQRY += "CMT_NMP01 = "+strNMP01+ ",";
									M_strSQLQRY += "CMT_NMP02 = "+strNMP02+ ",";
									M_strSQLQRY += "CMT_NCSVL = "+strNCSVL+ ",";
									M_strSQLQRY += "CMT_CCSVL = '"+strCCSVL+ "',";
									M_strSQLQRY += "CMT_MODLS = '"+strMODLS+ "',";								
									M_strSQLQRY += getUSGDTL("CMT",'U',"");								
									M_strSQLQRY += " where CMT_CGMTP = '"+strCGMTP+"'";
									M_strSQLQRY += " AND CMT_CGSTP = '"+strCGSTP+"'";
									M_strSQLQRY += " AND CMT_CODCD = '"+strCODCD+"'";																						
								}
								else 
								{	
									M_strSQLQRY = "Insert into CO_CDTRN(CMT_CGMTP,CMT_CGSTP,CMT_CODCD,";
									M_strSQLQRY += "CMT_CODDS,CMT_SHRDS,CMT_CHP01,CMT_CHP02,CMT_NMP01,";
									M_strSQLQRY += "CMT_NMP02,CMT_NCSVL,CMT_CCSVL,CMT_MODLS,CMT_TRNFL,CMT_STSFL,";
									M_strSQLQRY += "CMT_LUSBY,CMT_LUPDT) values (";
									M_strSQLQRY += "'" + strCGMTP+ "',";
									M_strSQLQRY += "'" + strCGSTP+ "',";
									M_strSQLQRY += "'" + strCODCD+ "',";
									M_strSQLQRY += "'" + strCODDS+ "',";
									M_strSQLQRY += "'" + strSHRDS+ "',"; 
									M_strSQLQRY += "'" + strCHP01+ "',";
									M_strSQLQRY += "'" + strCHP02+ "',";
									M_strSQLQRY += strNMP01+ ",";
									M_strSQLQRY += strNMP02+ ",";
									M_strSQLQRY += strNCSVL+ ",";
									M_strSQLQRY += "'" + strCCSVL+ "',";
									M_strSQLQRY += "'" + strMODLS+ "',";
									M_strSQLQRY += getUSGDTL("CMT",'I',"")+")";		                                    
								}
//								System.out.println(M_strSQLQRY);
								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
							M_rstRSSET.close();
						}						
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				intRECCT = 0;
				for(i = 0;i < tblITMDT.getRowCount()-1;i++)
				{
					if(tblITMDT.getValueAt(i,TB_CHKFL).toString().trim().equals("true"))
					{
						strCODCD = tblITMDT.getValueAt(i,TB_CODCD).toString().trim().toUpperCase();
						M_strSQLQRY = "Update CO_CDTRN set ";								
						M_strSQLQRY += getUSGDTL("CMT",'U',"X");
						M_strSQLQRY += " where CMT_CGMTP = '"+strCGMTP+"'";
						M_strSQLQRY += " AND CMT_CGSTP = '"+strCGSTP+"'";
						M_strSQLQRY += " AND CMT_CODCD = '"+strCODCD+"'";
						intRECCT++;												
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					}					
				}							
				/*if((intRECCT ==intSRLNO) && (cl_dat.M_flgLCUPD_pbst = true))
				{
					M_strSQLQRY = "Update CO_CDMST set ";					
					M_strSQLQRY += getUSGDTL("CM",'U',"X");
					M_strSQLQRY += " where CM_CGMTP = '"+strCGMTP+"'";
					M_strSQLQRY += " AND CM_CGSTP = '"+strCGSTP+"'";					
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
				}*/
			}
			setMSG("",'N');
			if(cl_dat.exeDBCMT("exeSAVE"))
			{				
				clrCOMP();				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))				
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');								 
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
	
	/** Initializing components before accepting data
	 */
	void clrCOMP()
	{
		try
		{
			strCGMTP = txtCGMTP.getText().toUpperCase();
			strCGSTP = txtCGSTP.getText().toUpperCase();
			super.clrCOMP();
			txtCGMTP.setText(strCGMTP);
			txtCGSTP.setText(strCGSTP);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"clrCOMP");			
		}
	}
	
	boolean vldDATA()
	{
		if(tblITMDT.isEditing())
				tblITMDT.getCellEditor().stopCellEditing();
		tblITMDT.setRowSelectionInterval(0,0);
		tblITMDT.setColumnSelectionInterval(0,0);
		if((txtCGMTP.getText().trim()).length()==0)
		{
			setMSG("Main Group cannot be blank..",'E');
			txtCGSTP.requestFocus();
			return false;
		}		
		if((txtCGSTP.getText().trim()).length()==0)
		{
			setMSG("Subgroup cannot be blank..",'E');
			txtCGSTP.requestFocus();
			return false;
		}
		if((txtCODLN.getText().trim()).length()==0)
		{
			setMSG("Code Length cannot be blank, By default it is Zero..",'E');
			txtCODLN.setText("0");
		}				
		//if no row selected.		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{				
			boolean L_flgCHKFL= false;
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{				
				if (tblITMDT.getValueAt(i,TB_CHKFL).toString().equals("true")) 
				{				
					L_flgCHKFL= true;
					break;
				}				
			}			
			if (L_flgCHKFL== false)
			{
				setMSG("No row Selected..",'E');				
				return false;
			}			
		}
		//if tryed to delete blank Row
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
		{			
			for(int i=0; i<tblITMDT.getRowCount(); i++)
			{								
				if (tblITMDT.getValueAt(i,TB_CHKFL).toString().equals("true"))
				{			
					if(i>= intSRLNO)
					{
						setMSG("Blank Row cannot be Deleted..",'E');
						return false;
					}
				}
			}
		}		
		// if the user tried to modify data when addition is selected.
		if( cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))			
		{								
			if (intSRLNO!=0)
			{
				for(int i=0; i<intSRLNO; i++)
				{				
					if (tblITMDT.getValueAt(i,TB_CHKFL).toString().equals("true"))
					{					
						setMSG("Insert Data at very first Blank Row & remove check mark of upper Rows..",'E');								
						return false;					
					}
				}
			}		
		}
		//To Check the data entered in the table Row for not to be Blank.
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		{						
			for(int i=0; i<intROWCT_fn -1; i++)
			{	
				if (tblITMDT.getValueAt(i,TB_CHKFL).toString().equals("true"))
				{
					if((tblITMDT.getValueAt(i,TB_CODCD).toString()).length() == 0)
					{
						setMSG("Code can not be Blank..",'E');
						tblITMDT.editCellAt(i,TB_CODCD);
						tblITMDT.cmpEDITR[TB_CODCD].requestFocus();
						return false;					
					}
					else if(tblITMDT.getValueAt(i,TB_CODDS).toString().trim().length() == 0)
					{
						setMSG("Description cannot be Blank..",'E');
						tblITMDT.editCellAt(i,TB_CODDS);
						tblITMDT.cmpEDITR[TB_CODDS].requestFocus();
						return false;
					}
					else if(tblITMDT.getValueAt(i,TB_SHRDS).toString().trim().length() == 0)
					{
						setMSG("Description cannot be Blank..",'E');
						tblITMDT.editCellAt(i,TB_SHRDS);
						tblITMDT.cmpEDITR[TB_SHRDS].requestFocus();
						return false;
					}
				}	
			}		
		}
		return true;
	}				
	/**
	 * Method to fetch data from the database & display it in the corrresponding JTextField & JTable.
	 */
	private void getDATA()
	{
		try
		{
			intSRLNO = 0;			
			tblITMDT.clrTABLE();
			txtCGRDS.setText("");
			txtMODFL.setText("");
			txtCODLN.setText("");
			txtCODRM.setText("");
			txtAPPLS.setText("");			
			if(tblITMDT.isEditing())
				tblITMDT.getCellEditor().stopCellEditing();
			tblITMDT.setRowSelectionInterval(0,0);
			tblITMDT.setColumnSelectionInterval(0,0);			
						
			M_strSQLQRY = "Select * from CO_CDTRN,CO_CDMST where CM_CGMTP=CMT_CGMTP ";
			M_strSQLQRY += " AND CM_CGSTP = CMT_CGSTP AND CMT_CGMTP='"+txtCGMTP.getText().trim().toUpperCase()+"'";
			M_strSQLQRY += " AND CMT_CGSTP='"+txtCGSTP.getText().trim().toUpperCase()+"'";
			M_strSQLQRY += " AND isnull(CMT_STSFL,'') <> 'X'";
			M_strSQLQRY += " order by CMT_CODCD";
			/*if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
				M_strSQLQRY += " AND CMT_STSFL in ('0','X')";
			else
				M_strSQLQRY += " AND CMT_STSFL not in 'X'";*/			
			int i = 0;			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{		
					if(intSRLNO == 0)
					{
						strCGRDS = nvlSTRVL(M_rstRSSET.getString("CM_CGRDS"),"");	
						strAPPLS = nvlSTRVL(M_rstRSSET.getString("CM_APPLS"),"0"); 
						strMODFL = nvlSTRVL(M_rstRSSET.getString("CM_MODFL"),"");
						strDBCDL = nvlSTRVL(M_rstRSSET.getString("CM_CODLN"),"");
						strCODRM = nvlSTRVL(M_rstRSSET.getString("CM_CODRM"),"");
					}					
					strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");					
					strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"");					
					strCHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					strCHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"");					
					strNMP01 = nvlSTRVL(M_rstRSSET.getString("CMT_NMP01"),"0");
					strNMP02 = nvlSTRVL(M_rstRSSET.getString("CMT_NMP02"),"0");					
					strNCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),"0");
					strCCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					strMODLS = nvlSTRVL(M_rstRSSET.getString("CMT_MODLS"),"");
					strSTSFL = nvlSTRVL(M_rstRSSET.getString("CMT_STSFL"),"0");
										
					intSRLNO +=1;					
					tblITMDT.setValueAt(strCODCD.trim(),i,TB_CODCD);
					tblITMDT.setValueAt(strCODDS.trim(),i,TB_CODDS);
					tblITMDT.setValueAt(strSHRDS.trim(),i,TB_SHRDS);
					tblITMDT.setValueAt(strCHP01.trim(),i,TB_CHP01);
					tblITMDT.setValueAt(strCHP02.trim(),i,TB_CHP02);
					tblITMDT.setValueAt(strNMP01.trim(),i,TB_NMP01);
					tblITMDT.setValueAt(strNMP02.trim(),i,TB_NMP02);
					tblITMDT.setValueAt(strNCSVL.trim(),i,TB_NCSVL);
					tblITMDT.setValueAt(strCCSVL.trim(),i,TB_CCSVL);
					tblITMDT.setValueAt(strMODLS.trim(),i,TB_MODLS);
					tblITMDT.setValueAt(strSTSFL.trim(),i,TB_STSFL);
					i++;
				}
				txtCGRDS.setText(strCGRDS.trim());
				txtCODLN.setText(strDBCDL.trim());
				txtCODRM.setText(strCODRM.trim());
				txtAPPLS.setText(strAPPLS.trim());
				txtMODFL.setText(strMODFL.trim());
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{				
				if((input == txtCGMTP) && (txtCGMTP.getText().trim().length() == 3))
				{								
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						return true;					
					
					M_strSQLQRY = "select CM_CGMTP from CO_CDMST";
					M_strSQLQRY +=" where CM_CGMTP ='" + txtCGMTP.getText().trim().toUpperCase()+ "'";						
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
							setMSG( "Invalid Main Group ..",'E');
							return false;							
						}															
					}
				}													
				if((input == txtCGSTP) && (txtCGMTP.getText().trim().length() == 3) && (txtCGSTP.getText().trim().length() == 7))
				{		
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						return true;
					M_strSQLQRY = "select CM_CGMTP from CO_CDMST";
					M_strSQLQRY +=" where CM_CGMTP ='" + txtCGMTP.getText().trim().toUpperCase()+ "'";
					M_strSQLQRY +=" AND CM_CGSTP ='" + txtCGSTP.getText().trim().toUpperCase()+ "'";
//					System.out.println(M_strSQLQRY);
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
							setMSG( "Main Group OR Sub Group does not exist..",'E');
							return false;							
						}												
					}				
				}				
			}
			catch(Exception L_EX)
			{				
				setMSG(L_EX,"InputVerify");
				setCursor(cl_dat.M_curDFSTS_pbst);				
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
				if(P_intCOLID == TB_CODCD)
				{						
					String L_strCODCD;
					int L_intROCNT = 0;
					int L_intCOUNT=0;
					String L_strTEMP="";
					L_strTEMP = tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TB_CODCD).toString().toUpperCase();					
					if(L_strTEMP.length()==0)					
						return true;
					
					for(int i=0; i<intROWCT_fn -1; i++)
					{							
						L_strCODCD = (tblITMDT.getValueAt(i,TB_CODCD).toString()).toUpperCase();
						if(L_strCODCD.equals(L_strTEMP))						
							L_intROCNT ++;						
						if(L_intROCNT >1)
						{
							setMSG("Duplicate values of code are not allowed..",'E');
							return false;
						}													
					}				
				}				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"TableVerify");
				setCursor(cl_dat.M_curDFSTS_pbst);				
			}
			return true;
		}
	}	
}

/*	if((txtCGMTP.getText().trim().length() > 0)&&(txtCGSTP.getText().trim().length() > 0))
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							return true;
						}
						M_strSQLQRY = "select CMT_CODCD from CO_CDTRN";
						M_strSQLQRY +=" where CMT_CGMTP ='" + txtCGMTP.getText().trim().toUpperCase()+ "'";
						M_strSQLQRY +=" AND CMT_CGSTP ='" + txtCGSTP.getText().trim().toUpperCase()+ "'";
						M_strSQLQRY +=" AND CMT_CODCD ='" + tblITMDT.getValueAt(tblITMDT.getSelectedRow(),TB_CODCD).toString().toUpperCase()+ "'";
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
						if (M_rstRSSET != null)
						{  															
							if(M_rstRSSET.next())
							{				
								setMSG("",'N');
								M_rstRSSET.close();
								return true;								
							}
							else 
							{
								M_rstRSSET.close();
								setMSG( "Code already exist, Please Enter another..",'E');
								return false;		
							}														
						}					
					}*/					