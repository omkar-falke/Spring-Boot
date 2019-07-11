/*
System Name   : Labortory Information Management System
Program Name  : Product Specification Entry Screen.
Program Desc. :	Program to view Product Master Specification and to enter new 
                quality parameter specifications.
Author        : Mr.S.R.Mehesare
Date          : 17 August 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import javax.swing.JComboBox;import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComponent;import javax.swing.InputVerifier;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.sql.ResultSet;import javax.swing.JOptionPane;
import java.util.*;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Product Master and quality parameter entry		

Purpose : This module is used for addition, modification and enquiry of  Quality 
parameters specifications for a grade.It is aslo used for adding a new grade in 
product master.

List of tables used :
Table Name  Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_QPMST    QP_QCATP,QP_TSTTP,QP_PRDCD,
            QP_QPRCD,QP_STRDT,QP_SRLNO               #        #        #
CO_CDTRN    CMT_CGMTP,CMT_CGSTP,CMT_CODCD                              #
CO_PRMST    PR_PRDCD                                 #        #        #
CO_CTMST    CT_MATCD                                 #        #        #
CO_CTTRN									         #
--------------------------------------------------------------------------------------


List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size        Description
--------------------------------------------------------------------------------------
cmbPRDTP    CMT_CODCD      CO_CDTRN	     varchar(15)      Product Type
txtPRDCD    PR_PRDCD       CO_PRMST      varchar(15)      Product Code
txtPRDDS    PR_PRDDS       CO_PRMST      varchar(45)      Grade
txtSTRDT    QP_STRDT       CO_QPMST      date             Specification strat date
txtENDDT    QP_ENDDT       CO_QPMST      date             Specification end date
txtSRLNO    QP_SRLNO       CO_QPMST      varchar(5)       Serial No.
TB_QPRCD    QP_QPRCD       CO_QPMST      varchar(3)       Quality para. code.
TB_QPRSL    QP_QPRCD       CO_QPMST      varchar(3)       Quality para. Srl.No.
TB_QPRDS    QP_QPRDS       CO_QPMST      varchar(30)      Quality para desc
TB_UOMDS    QP_UOMDS       CO_QPMST      varchar(15)      Uom Desc.
TB_TSMDS    QP_TSMDS       CO_QPMST      varchar(20)      TEST method desc
TB_NPFVL    QP_NPFVL       CO_QPMST      decimal(12,3)    Numeric para from
TB_NPTVL    QP_NPTVL       CO_QPMST      decimal(12,3)    Numeric para To
TB_CMPFL    QP_CMPFL       CO_QPMST      varchar(1)       Spec compare flag
TB_PRGFL    QP_PRGFL       CO_QPMST      varchar(10)      Program flag
TB_TXTVL    QP_TXTVL       CO_QPMST      varchar(20)      Text value
TB_ORDBY    QP_ORDBY       CO_QPMST      decimal(2,0)     Order by	
--------------------------------------------------------------------------------------
<B> Logic </B>
<I><B>To Insert New Record </B>
At First the default Quality Parameter Specification are fetched for 
    1) Serial Number = "00000" 
    2) End Date = null
    3) PR_PRDCD = given Product Code.
  - If the Default Quality parameter Specifications are avaliable then Addition of 
    new quality parameter is not allowed.
  - Hence to Specify new Specifications, We have to modify the default Specifications.
  - And if Default Specifications is not available then Specification Entry is allowed.
  - These new Quality parameter Details are inserted in the table CO_QPMST
<B>To modify existing Record </B>
  - If Default Quality parameter Specifications are already given & we have to 
    Specify some new Specifications, 
  - For this we have to modify existing Specificationsto make the new Specification
    available.
  - For that End Date of old Specification is updated. Hence these Specifications are 
    only avalible till the given End Date.
  - After End Date the latest Specifications are avaliable & the Old Specifications are
    kept as it is for further referance.

Before Entry of new Specifications End Date of old Specifications is Updated in Table CO_QPMST 
as QP_ENDDT = Given Date for condiations
   1) QP_QCATP = given QCA Type
   2) AND QP_TSTTP = "0103"
   3) AND QP_PRDCD = given product Code.
   4) AND QP_STRDT = data fetched from database.
   5) AND QP_SRLNO =Given Serial Number
   6) AND QP_ENDDT is null";
After he Successful updation of End Date 
New Specifications are inserted for which Start Date is a date, one day greater than 
   end Date of Old Specifications

<B>Validations :</B>    
	- First four digits of product codes are validated against the selected product type
      count(*) for SYS/PRXXPRS/SUBSTRING(LM_PRDCD,0,4)  and cmt_chp01 = LM_PRDTP
    - Product code length validation.
    - Specification start date ,end date validation with current date.
    - Specification end date > specification start date validation.
    - Valid values for program flag (combination of C,B,T) separated by _C,B,T		
      e.g C_B_T	for composite / bag / test certificate.
    - Order by fields specifies the order in which quality para. will appear on certificate.
    - QP_CMPFL 	: if 'Y'     Test values are to be checked against the specification range
			         'N'     Range checking is not required.		
    - For modifications in specification value, Specification end date should be updated and new specification set should be entered.
    - For default entries in test certificate, Serial no. is '00000', which can be replaced by the Group code for specific case and additional parameters and their range can be specified..
</I> */

public class co_meqpm extends cl_pbase
{									/**JComboBox to display & to select Product Type.*/
	private JComboBox cmbPRDTP;		/**JTextField to display & to enter Product Code.*/
	private JTextField txtPRDCD;	/**JTextField to display Product Code Description.*/ 
	private JTextField txtPRDDS;	/**JTextField to display Unit of Measurement.*/ 
	private JTextField txtUOM;		/**JTextField to display Quality Flag.*/ 
	private JTextField txtQLTFL;	/**JTextField to display Quality Flag Description.*/
	private JTextField txtQLTDS;	/**JTextField to display & to enter Starting Date for the Quality Parameter Specifications to use.*/	
	private JTextField txtSTRDT;	/**JTextField to display & to enter ending Date for the Quality Parameter Specifications in use.*/	
	private JTextField txtENDDT;	/**JTextField to display & to enter Serial Number Or Group Code.*/	
	private JTextField txtSRLNO;	/**JTextField to enter Referance Product Code.*/	
	private JTextField txtPRDRF;	/**JTextField to display Serial Description.*/	
	private JTextField txtSRLDS;	/** JTable to display & to enter Quality parameter specifications.*/
    private cl_JTable tblQPRDL;	
									/** JTextField to apply length validation while entering Quality Parameter Code.*/
	private JTextField txtQPRCD;	/** JTextField to apply length validation while entering Quality Parameter Srl.No.*/
	//private JTextField txtQPRSL;	/** JTextField to attach to From Value Column to apply some validation Checks.*/
	private JTextField txtFRVAL;	/** JTextField to attach to To Value Column to apply some validation Checks.*/
	private JTextField txtTOVAL;	/** JTextField to attach to Check flag Column to apply some validation Checks.*/
	private JTextField txtCMPFL;	/** JTextField to attach to OrderBy Column to apply some validation Checks.*/
	private JTextField txtORDBY;	/** JTextField to attach to Program flag Column to apply some validation Checks.*/
	private JTextField txtPRGFL;
									/** Final Integer to represent Check Flag Column.*/
	private final int TB_CHKFL =0;	/** Final Integer to represent Quality Paramter Column.*/
	private final int TB_QPRCD =1;	/** Final Integer to represent Quality Paramter Description Column.*/
	//private final int TB_QPRSL =2;	/** Final Integer to represent Quality Paramter Description Column.*/
	private final int TB_QPRDS =2;	/** Final Integer to represent Unit of Measurement Column.*/
	private final int TB_UOMDS =3;	/** Final Integer to represent test Method Description Column.*/
	private final int TB_TSMDS =4;	/** Final Integer to represent From Value Column which Specifies the Lower Limit of quality parameter value.*/
	private final int TB_NPFVL =5;	/** Final Integer to represent To Value Column which Specifies the Upper limit of quality parameter value.*/
	private final int TB_NPTVL =6;	/** Final Integer to represent Check Flag Column.*/
	private final int TB_CMPFL =7;	/** Final Integer to represent Program Flag Column.*/
	private final int TB_PRGFL =8;	/** Final Integer to represent Other Specifications Column.*/
	private final int TB_TXTVL =9;	/** Final Integer to represent OrderBy Column.*/
	private final int TB_ORDBY =10;	/** Final Integer to represent Test Specification Column.*/
	private final int TB_TSTSP =11;	/** Final Integer to represent Test Condition Column.*/
	private final int TB_TSTCN =12;		
									/** String variable for Product Type.*/	
	private String strPRDTP;		/** String variable for previous product Type.*/	
	private String strOPRDTP="";	/** String variable for Product Code.*/	
	private String strPRDCD;		/** String variable for previous Product Code.*/	
	private String strOPRDCD="";	/** String variable for Material Description.*/
	private String strMATDS;		/** String variable forUnit of Measurement.*/			
	private String strUOMCD;		/** String variable for Original Serial Number.*/
	private String strORGSRL;		
												/** Final String to reperesent Styrene raw Material.*/
	private final String strSTYRN_fn = "11";	/** Final String to reperesent PolyStyrene.*/
	private final String strPSPRD_fn = "01";	/** Final String to reperesent Speciality PolyStyrene.*/
	private final String strSPSPR_fn = "02";	/** Final String to reperesent Test Type.*/
	private final String strTSTTP_fn = "0103";	/** Final String to reperesent General Purpose Polystyrene Group.*/
	private final String strCOLGP_fn = "5211";	/** Final String to reperesent High Impact Polystyrene Group.*/
	private final String strCOLHI_fn = "5212";	/** Final String to reperesent Default Serial Number.*/
	private final String strDFTSR_fn = "00000";
								/** Integer Variable to Count the number of Records fetched.*/
	private int L_intRECCT =0;	/** Integer variable to reresent the selected Row Number.*/
	private int intSELROW;		/** Integer variable to reresent total number of Rows in the Table..*/
	private int intROWCT =50;	
										/** FileOutputStream to generate the Report File From Stream of Data.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream to hold data in the of DataStream to generate the Report File.*/
	private DataOutputStream dosREPORT;
						
	private INPVF objINPVF = new INPVF();	/** Table Input Verifier to manage the data fetchingfor selected Lot Number.*/
	private TableInputVerifier TBLINPVF;
	private TBLINPVF objTBLVRF;
	private int intRWCNT =0;
	co_meqpm()
	{
		super(1);
		setMatrix(20,8);
		add(new JLabel("Prod. Type"),2,1,1,.8,this,'R');
		add(cmbPRDTP = new JComboBox(),2,2,1,1.2,this,'L');
		add(new JLabel("Prod. Code"),2,3,1,.8,this,'R');
		add(txtPRDCD = new JTextField(),2,4,1,1.1,this,'L');
		add(new JLabel("Description"),2,5,1,.8,this,'R');
		add(txtPRDDS = new JTextField(),2,6,1,2,this,'L');
		
		add(new JLabel("UOM"),3,1,1,.8,this,'R');
		add(txtUOM = new JTextField(),3,2,1,1.2,this,'L');
		add(new JLabel("Quality Flag"),3,3,1,.8,this,'R');
		add(txtQLTFL = new JTextField(),3,4,1,1.1,this,'L');
		add(new JLabel("Description"),3,5,1,.8,this,'R');
		add(txtQLTDS = new JTextField(),3,6,1,2,this,'L');
		
		add(new JLabel("From Date"),4,1,1,.8,this,'R');
		add(txtSTRDT = new TxtDate(),4,2,1,1.2,this,'L');
		add(new JLabel("To Date"),4,3,1,.8,this,'R');
		add(txtENDDT = new TxtDate(),4,4,1,1.1,this,'L');
		add(new JLabel("SrNo/Group"),4,5,1,.8,this,'R');
		add(txtSRLNO = new JTextField(),4,6,1,.5,this,'L');
		add(txtSRLDS= new JTextField(),4,7,1,1.5,this,'R');
		
		add(new JLabel("Product Ref."),5,1,1,.8,this,'R');
		add(txtPRDRF = new JTextField(),5,2,1,2,this,'L');
		
		String[] L_strTSDHD = {"Select","Para. Code","para. Description ","UOM ","Test Method Desc","From Range","To Range","Check Flag(Y/N)","Prog. Flag","Other Specs.","Order by","Test Spec","Test Cond"};
		int[] L_intTSTSZ = {20,70,150,60,80,70,70,70,70,60,10,200,200};
		tblQPRDL = crtTBLPNL1(this,L_strTSDHD,intROWCT,7,1,10.3,8,L_intTSTSZ,new int[]{0});				
		
		cmbPRDTP.addItem("01 Polystyrene");
		cmbPRDTP.addItem("02 Sp Polystyrene");
		cmbPRDTP.addItem("11 Styrene");
		
		objTBLVRF = new TBLINPVF();
		tblQPRDL.setInputVerifier(objTBLVRF);
			
		txtPRDCD.setInputVerifier(objINPVF);
		//txtSTRDT.setInputVerifier(objINPVF);
		txtENDDT.setInputVerifier(objINPVF);
		txtSRLNO.setInputVerifier(objINPVF);
					
		tblQPRDL.setCellEditor(TB_QPRCD, txtQPRCD = new TxtLimit(3));
		//tblQPRDL.setCellEditor(TB_QPRSL, txtQPRSL = new TxtLimit(2));
		tblQPRDL.setCellEditor(TB_NPFVL, txtFRVAL = new TxtNumLimit(12.3));
		tblQPRDL.setCellEditor(TB_NPTVL, txtTOVAL = new TxtNumLimit(12.3));
		tblQPRDL.setCellEditor(TB_CMPFL, txtCMPFL = new TxtLimit(1));
		tblQPRDL.setCellEditor(TB_ORDBY, txtORDBY = new TxtNumLimit(2));
		tblQPRDL.setCellEditor(TB_PRGFL, txtPRGFL = new TxtLimit(5));
		
		txtQPRCD.addKeyListener(this);txtQPRCD.addFocusListener(this);			
		//txtQPRSL.addKeyListener(this);txtQPRSL.addFocusListener(this);			
		txtFRVAL.addKeyListener(this);txtFRVAL.addFocusListener(this);				
		txtTOVAL.addKeyListener(this);txtTOVAL.addFocusListener(this);				
		txtCMPFL.addKeyListener(this);txtCMPFL.addFocusListener(this);						
		txtORDBY.addKeyListener(this);txtORDBY.addFocusListener(this);				
		txtPRGFL.addKeyListener(this);txtPRGFL.addFocusListener(this);
		
		setENBL(false);
	}
	/**
	 * Method to enable & disable the Components according to requriements
	 * @param P_flgSTAT boolean argument to pass the state of the variable.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)		
		{			
			setMSG("Select an Option..",'N');
			return;
		}		
		cmbPRDTP.setEnabled(true);
		txtPRDCD.setEnabled(true);
		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))) 
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
			{		
				txtSTRDT.setEnabled(true);			
				txtSRLNO.setEnabled(true);
				txtPRDRF.setEnabled(true);
				cl_dat.M_btnSAVE_pbst.setEnabled(false);				
			}		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{	
				txtPRDRF.setEnabled(true);
				txtSTRDT.setEnabled(true);				
				txtENDDT.setEnabled(true);	
				//txtSRLNO.setEnabled(true);
				txtSRLNO.setEnabled(false);
			}						
			tblQPRDL.cmpEDITR[TB_CHKFL].setEnabled(true);
			tblQPRDL.cmpEDITR[TB_QPRCD].setEnabled(true);
			//tblQPRDL.cmpEDITR[TB_QPRSL].setEnabled(true);
			tblQPRDL.cmpEDITR[TB_NPFVL].setEnabled(true);
			tblQPRDL.cmpEDITR[TB_NPTVL].setEnabled(true);
			tblQPRDL.cmpEDITR[TB_CMPFL].setEnabled(true);
			tblQPRDL.cmpEDITR[TB_PRGFL].setEnabled(true);
			tblQPRDL.cmpEDITR[TB_TXTVL].setEnabled(true);
			tblQPRDL.cmpEDITR[TB_ORDBY].setEnabled(true);
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))		
		{
			txtSTRDT.setEnabled(true);	
			txtPRDRF.setEnabled(false);
		}
	
	}				
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{				
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			{								
				setMSG("Please Select an option ..",'N');
				setENBL(false);
			}		
			else
			{								
				setMSG("Please Enter Test Type OR Press F1 to select from List..",'N');				
				setENBL(false);
				cmbPRDTP.requestFocus();				
			}
		}							
		else if(M_objSOURC == cmbPRDTP)
		{
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			if(!strPRDTP.equals(strOPRDTP))
			{				
				txtPRDCD.setText("");
				txtPRDDS.setText("");
				txtUOM.setText("");
				txtQLTFL.setText("");
				txtQLTDS.setText("");
				txtSTRDT.setText("");
				txtENDDT.setText("");
				txtSRLNO.setText("");
				txtPRDRF.setText("");
				txtSRLDS.setText("");
				tblQPRDL.clrTABLE();
				tblQPRDL.clrTABLE();						
			}			
			///if(strPRDTP.equals(strSPSPR_fn))
			///	txtPRDRF.setEnabled(true);				
			txtPRDCD.requestFocus();
		}
		/*if (M_objSOURC == tblQPRDL.cmpEDITR[TB_QPRCD])
		{							
			intSELROW = tblQPRDL.getSelectedRow();
		}*/
	else if(M_objSOURC == txtPRDRF)
	{
		int i=0;
		java.util.Date L_TMPDT;
		ResultSet L_rstRSSET;
		String L_TSMCD,L_strTSMDS;
		try
		{
			if(txtPRDRF.getText().trim().length() == 0)
				return;
			if(tblQPRDL.isEditing())
				tblQPRDL.getCellEditor().stopCellEditing();
			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			tblQPRDL.clrTABLE();
			M_strSQLQRY = "SELECT * FROM CO_QPMST WHERE ";//QP_QCATP = '" +strPRDTP +"'";
			M_strSQLQRY += " QP_PRDCD ='"+txtPRDRF.getText()+"'";
			M_strSQLQRY += " AND QP_SRLNO ='"+strDFTSR_fn.trim()+"'";
			M_strSQLQRY += " AND QP_ENDDT is null order by QP_ORDBY ";			
			strORGSRL ="";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				while(L_rstRSSET.next())
				{
					if(i==0)
					{					
						txtSTRDT.setText(M_fmtLCDAT.format(L_rstRSSET.getDate("QP_STRDT")));				
						strORGSRL = nvlSTRVL(L_rstRSSET.getString("QP_SRLNO"),"");
						txtSRLNO.setText(strORGSRL);	
					}
					tblQPRDL.setValueAt(new Boolean(true),i,TB_CHKFL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),""),i,TB_QPRCD);
					//tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_QPRSL"),""),i,TB_QPRSL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_QPRDS"),""),i,TB_QPRDS);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_UOMDS"),""),i,TB_UOMDS);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_TSMDS"),""),i,TB_TSMDS);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_NPFVL"),""),i,TB_NPFVL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_NPTVL"),""),i,TB_NPTVL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_CMPFL"),""),i,TB_CMPFL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_PRGFL"),""),i,TB_PRGFL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_TXTVL"),""),i,TB_TXTVL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_ORDBY"),""),i,TB_ORDBY);
					i++;
				}			
				L_rstRSSET.close();
				
			}
			
		}
		catch(Exception L_E)
		{
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_E,"getSPCDTL");
		}
	 }
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);					
				if(M_objSOURC == txtPRDCD)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					M_strHLPFLD = "txtPRDCD";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
					if(strPRDTP.equals(strSTYRN_fn))
				      {
						M_strSQLQRY = "select distinct QP_PRDCD,CT_MATDS,CT_UOMCD from CO_QPMST,CO_CTMST where QP_QCATP = '";
						M_strSQLQRY += strPRDTP +"' AND CT_MATCD = QP_PRDCD order by QP_PRDCD";						
						String L_arrHDR[] = {"Material Code","Description","UOM CODE"};					
						cl_hlp(M_strSQLQRY,2,1,L_arrHDR,3,"CT");
					}
					else
					{
						M_strSQLQRY = "select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_PRDTP = '";
						M_strSQLQRY += strPRDTP +"' AND PR_STSFL <> 'X' ";
						if(txtPRDCD.getText().length() >0)
							M_strSQLQRY += " AND PR_PRDCD like '"+txtPRDCD.getText().trim() +"%'";
						M_strSQLQRY += " order by PR_PRDCD";						
						String L_arrHDR[] = {"Prod Code","Description"};
						cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");				
					}
					}
					else
					{

					if(strPRDTP.equals(strSTYRN_fn))
				      {
						M_strSQLQRY = "select distinct QP_PRDCD,CT_MATDS,CT_UOMCD from CO_QPMST,CO_CTMST where QP_QCATP = '";
						M_strSQLQRY += strPRDTP +"' AND CT_MATCD = QP_PRDCD order by QP_PRDCD";						
						String L_arrHDR[] = {"Material Code","Description","UOM CODE"};					
						cl_hlp(M_strSQLQRY,2,1,L_arrHDR,3,"CT");
					}
					else
					{
						M_strSQLQRY = "select distinct QP_PRDCD,PR_PRDDS from CO_QPMST,CO_PRMST where QP_PRDCD = PR_PRDCD AND PR_PRDTP = '";
						M_strSQLQRY += strPRDTP +"' AND PR_STSFL <> 'X' ";
						if(txtPRDCD.getText().length() >0)
							M_strSQLQRY += " AND PR_PRDCD like '"+txtPRDCD.getText().trim() +"%'";
						M_strSQLQRY += " order by QP_PRDCD";						
						String L_arrHDR[] = {"Prod Code","Description"};
						cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");				
					}
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(M_objSOURC == txtSRLNO)
				{
					M_strHLPFLD = "txtSRLNO";
					cl_dat.M_flgHELPFL_pbst = true;
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " AND CMT_CGSTP = 'QCXXTCG'";
					String L_arrHDR[] = {"Group Code","Description"};
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);					
				}
				if(M_objSOURC == txtPRDRF)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					if(txtPRDCD.getText().trim().length() ==0)
					{
						setMSG("Please Enter Product code first..",'E');
						return;
					}
					M_strHLPFLD = "txtPRDRF";
					if((txtPRDCD.getText().trim().substring(0,4).equals(strCOLGP_fn))||(txtPRDCD.getText().trim().substring(0,4).equals(strCOLHI_fn)))
					{
						M_strSQLQRY = "select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_PRDTP = '01' ";					
						M_strSQLQRY +=" AND PR_PRDCD like '"+txtPRDCD.getText().trim().substring(0,1)+"1"+txtPRDCD.getText().trim().substring(2,4)+"%'";
						M_strSQLQRY +=" AND isnull(PR_STSFL,'') <> 'X'  order by PR_PRDCD";
					}
					else
					{
						M_strSQLQRY = "select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_PRDTP = '"+ cmbPRDTP.getSelectedItem().toString().substring(0,2) +"'";;						
						M_strSQLQRY +=" AND PR_PRDCD like '"+txtPRDCD.getText().trim().substring(0,4)+"%'";
						M_strSQLQRY +=" AND isnull(PR_STSFL,'') <> 'X'  order by PR_PRDCD";
					}	
					String L_arrHDR[] = {"Prod Code","Description"};
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				if(M_objSOURC == txtSTRDT)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtSTRDT";
					String L_arrHDR[] ={"Start Date","Serial No./Group Code","End Date"};
					M_strSQLQRY = "select distinct QP_STRDT,QP_SRLNO,QP_ENDDT from CO_QPMST where QP_QCATP = '"+ strPRDTP +"'";
					M_strSQLQRY +=" AND QP_PRDCD = '"+strPRDCD +"' order by QP_STRDT";						
				   	cl_hlp(M_strSQLQRY,2,1,L_arrHDR,3,"CT");
				}			
				else if (M_objSOURC == tblQPRDL.cmpEDITR[TB_QPRCD])
				{	
					intSELROW = tblQPRDL.getSelectedRow();
					//if(tblQPRDL.getValueAt(intSELROW,TB_QPRCD).toString().length() == 0)
					//{						
						cl_dat.M_flgHELPFL_pbst = true;
						M_strHLPFLD = "txtQPRCD";					
						//M_strSQLQRY = "select SUBSTRING(QS_QPRCD,1,3)TS_QPRCD,QS_QPRDS,QS_UOMCD,QS_TSMCD,QS_ORDBY  from co_qsmst "; //,QS_QPRDS,QS_UOMCD,QS_TSMCD,QS_ORDBY 
						M_strSQLQRY = "select SUBSTRING(QS_QPRCD,1,3)TS_QPRCD,QS_QPRDS1,QS_UOMCD1,QS_TSMCD1,QS_TSTSP,QS_TSTCN,QS_ORDBY  from co_qsmst where QS_PRDTP = 'FG' "; //,QS_QPRDS,QS_UOMCD,QS_TSMCD,QS_ORDBY 
						if(intSELROW > 1)
						{
							M_strSQLQRY += " and SUBSTRING(QS_QPRCD,1,3) NOT IN(";												
							for(int i=0;i<intSELROW-1;i++) 
							{
								M_strSQLQRY += "'"+tblQPRDL.getValueAt(i,TB_QPRCD).toString().trim()+"',";
							}						
							M_strSQLQRY += "'" +tblQPRDL.getValueAt(intSELROW-1,TB_QPRCD).toString().trim()+"')";
						}
						M_strSQLQRY += " order by QS_QPRDS1";
						String L_arrQPR[] = {"Code","Description","UOM","Test Method","Test Cond","Test Spec","Order By"};
						//System.out.println("M_strSQLQRY(f1)>>"+M_strSQLQRY);
						M_flgBIGHLP = true;
						cl_hlp(M_strSQLQRY,1,1,L_arrQPR,7,"CT");
					//}
				}
				//else if (M_objSOURC == tblQPRDL.cmpEDITR[TB_QPRSL])
				//{							
				//	intSELROW = tblQPRDL.getSelectedRow();
				//		cl_dat.M_flgHELPFL_pbst = true;
				//		M_strHLPFLD = "txtQPRSL";					
				//		M_strSQLQRY = "select RM_SRLNO,RM_QPRDS,RM_TSTSP, RM_TSTCN, RM_UOMDS,RM_TSTMT from QP_RMMST where RM_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RM_QPRCD = '"+tblQPRDL.getValueAt(intSELROW,TB_QPRCD).toString()+"' order by RM_SRLNO";
				//		String L_arrQPRSL[] = {"Code","Parameter Detail","Test Spec.","Test Cond.","UOM","Test Method"};
				//		M_flgBIGHLP = true;
				//		cl_hlp(M_strSQLQRY,1,1,L_arrQPRSL,6,"CT");
				//}
				else if(M_objSOURC == tblQPRDL.cmpEDITR[TB_PRGFL])
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPRGFL";
					M_strSQLQRY = "select CMT_SHRDS,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
					M_strSQLQRY += "'SYS'"+ " AND  CMT_CGSTP =" +  "'QCXXPCT'";
					String L_arrHDR[] = {"Program Groups","Description"};
					cl_hlp(M_strSQLQRY,2,1,L_arrHDR,2,"CT");
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
	    }
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			if(M_objSOURC == cmbPRDTP)
			{
				txtPRDCD.requestFocus();
			}	
			if(M_objSOURC == txtPRDCD)
			{			
				strPRDCD = txtPRDCD.getText().trim();			
				txtPRDDS.setText("");
				txtUOM.setText("");
				txtQLTFL.setText("");
				txtQLTDS.setText("");
				txtSTRDT.setText("");
				txtENDDT.setText("");
				txtSRLNO.setText("");
				txtPRDRF.setText("");
				txtSRLDS.setText("");
				tblQPRDL.clrTABLE();			
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{				
					if(strPRDTP.equals(strSTYRN_fn))			
					{
						setMSG("Addition of Data is Not Allowed here..",'E');
						return;
					}					
					txtSTRDT.setText(cl_dat.M_strLOGDT_pbst);
					txtSRLNO.setText(strDFTSR_fn);
				}			
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{				
					if(strPRDTP.equals(strSTYRN_fn))			
					{
						txtPRDDS.setText(strMATDS);
						txtUOM.setText(strUOMCD);
						getSPCDTL("STY",txtSTRDT.getText().trim(),txtSRLNO.getText().trim());
						return;
					}
					else
					{
						getPRDDTL();
						getSPCDTL("CUR","","");
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
					}					
				}	
				if(!strPRDTP.equals(strSTYRN_fn))
				{	
					getPRDDTL();
					//added
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						getSPCDTL("CUR","","");
				}
				else 					//added
				{
					//txtPRDDS.setText(LM_MATDS);
					//txtUOM.setText(LM_UOMCD);
					getSPCDTL("STY",txtSTRDT.getText().trim(),txtSRLNO.getText().trim());
					
				}	
				txtSTRDT.requestFocus();							
			}						
			else if(M_objSOURC == txtPRDDS)
			{
				txtUOM.setText("MT");
				txtQLTFL.requestFocus();
			}
			else if(M_objSOURC == txtSTRDT)
			{		
				if(txtSTRDT.getText().length()>0)
				{		
					txtSRLNO.requestFocus();
					if(txtSRLNO.getText().trim().length() ==0)
						setMSG("Please Enter Serial No./ Group Code OR Press F1 to select form list..",'N');
				}
			}
			else if(M_objSOURC == txtENDDT)
			{
				txtPRDRF.requestFocus();
			}
			/*else if(M_objSOURC == txtENDDT)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					txtSRLNO.requestFocus();
					setMSG("Please Enter Serial Number / Group Code..",'N');
				}
			}*/
			else if(M_objSOURC == txtSRLNO)
			{				
				tblQPRDL.clrTABLE();
				if(getSPCDTL("SPC","",txtSRLNO.getText().trim())) // specifications for this serial where qp_enddt is null
				{
					if(txtENDDT.getText().trim().length() == 0)
					{
						setMSG("Specifications for the given serial No. Already exists.. ",'E');
						cl_dat.M_btnSAVE_pbst.setEnabled(false);
					}				
				}
				else
				{
					if(strPRDTP.equals(strPSPRD_fn))
					{														
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
						tblQPRDL.setColumnSelectionInterval(TB_QPRCD,TB_QPRCD);
						tblQPRDL.setRowSelectionInterval(0,0);
						tblQPRDL.editCellAt(0,TB_QPRCD);
						tblQPRDL.cmpEDITR[TB_QPRCD].requestFocus();
						setMSG("Enter the specification range..",'N');
					}
					else if(strPRDTP.equals(strSPSPR_fn))
					{						
						getSPSPAR(txtPRDCD.getText().trim());
						txtPRDRF.setEnabled(true);
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
						txtPRDRF.requestFocus();
					}
				}			
				tblQPRDL.setColumnSelectionInterval(TB_QPRCD,TB_QPRCD);
				tblQPRDL.setRowSelectionInterval(0,0);
				tblQPRDL.editCellAt(0,TB_QPRCD);
				tblQPRDL.cmpEDITR[TB_QPRCD].requestFocus();				
			}	
			else if(M_objSOURC == txtPRDRF)
			{
				tblQPRDL.setColumnSelectionInterval(TB_QPRCD,TB_QPRCD);
				tblQPRDL.setRowSelectionInterval(0,0);
				tblQPRDL.editCellAt(0,TB_QPRCD);
				tblQPRDL.cmpEDITR[TB_QPRCD].requestFocus();
			}			
		}
	}	
	/**
	 *Supper Method overrided to execute the F1 Help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();						
		if (M_strHLPFLD.equals("txtPRDCD"))
		{			
			txtPRDCD.setText(cl_dat.M_strHLPSTR_pbst);						
			cl_dat.M_flgHELPFL_pbst = false;
			if(strPRDTP.equals(strSTYRN_fn))
			{				
				strMATDS =String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
				strUOMCD =String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim();								
			}						
		}
		else if (M_strHLPFLD.equals("txtSRLNO"))
		{
			txtSRLNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtSRLDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
		}
		else if (M_strHLPFLD.equals("txtPRDRF"))
		{
			txtPRDRF.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRDRF.requestFocus();
			cl_dat.M_flgHELPFL_pbst = false;
		}
		else if (M_strHLPFLD.trim().equals("txtSTRDT"))
	    {
	        this.setCursor(cl_dat.M_curWTSTS_pbst);			
			String L_strSTRDT = cl_dat.M_strHLPSTR_pbst;
			String L_strSRLNO = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim();
			String L_ENDDT = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim();
			
			txtSTRDT.setText(cl_dat.M_strHLPSTR_pbst);
			txtENDDT.setText(L_ENDDT);
			txtSRLNO.setText(L_strSRLNO);
	        	txtSTRDT.requestFocus();		
			getSPCDTL("SPC",txtSTRDT.getText().trim(),txtSRLNO.getText().trim());
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			cl_dat.M_flgHELPFL_pbst = false;
	    }
		else if (M_strHLPFLD.trim().equals("txtQPRCD"))
	    {        
			if(tblQPRDL.isEditing())
				tblQPRDL.getCellEditor().stopCellEditing();
			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
			
	      	cl_dat.M_flgHELPFL_pbst = false;
			String L_strQPRCD = cl_dat.M_strHLPSTR_pbst;			
			
			tblQPRDL.setValueAt(new Boolean(true),intSELROW,TB_CHKFL);
			tblQPRDL.setValueAt(L_strQPRCD,intSELROW,TB_QPRCD);

			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
			tblQPRDL.editCellAt(intSELROW,TB_QPRCD);
			tblQPRDL.cmpEDITR[TB_QPRCD].requestFocus();
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
		}
		//else if (M_strHLPFLD.trim().equals("txtQPRSL"))
	    //{        
		//	if(tblQPRDL.isEditing())
		//		tblQPRDL.getCellEditor().stopCellEditing();
		//	tblQPRDL.setRowSelectionInterval(0,0);
		//	tblQPRDL.setColumnSelectionInterval(0,0);
			
	    // 	cl_dat.M_flgHELPFL_pbst = false;
		//	StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		//	tblQPRDL.setValueAt(new Boolean(true),intSELROW,TB_CHKFL);
		//	tblQPRDL.setValueAt(L_STRTKN.nextToken(),intSELROW,TB_QPRDS);
		//	tblQPRDL.setValueAt(L_STRTKN.nextToken(),intSELROW,TB_TSTSP);
		//	tblQPRDL.setValueAt(L_STRTKN.nextToken(),intSELROW,TB_TSTCN);
		//	tblQPRDL.setValueAt(L_STRTKN.nextToken(),intSELROW,TB_UOMDS);
		//	tblQPRDL.setValueAt(L_STRTKN.nextToken(),intSELROW,TB_TSMDS);
			
		//	tblQPRDL.setRowSelectionInterval(0,0);
		//	tblQPRDL.setColumnSelectionInterval(0,0);
		//	cl_dat.M_btnSAVE_pbst.setEnabled(true);
		//}
		else if (M_strHLPFLD.trim().equals("txtPRGFL"))
	    {
			if(tblQPRDL.isEditing())
				tblQPRDL.getCellEditor().stopCellEditing();
			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
						
	        tblQPRDL.setValueAt(cl_dat.M_strHLPSTR_pbst,intSELROW,TB_PRGFL);
			cl_dat.M_flgHELPFL_pbst = false;
			
			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
			tblQPRDL.editCellAt(intSELROW,TB_PRGFL);
			tblQPRDL.cmpEDITR[TB_PRGFL].requestFocus();
	    }
	}				
/**
 * Method to fetch the Product Details.
 */
	private boolean getPRDDTL()
	{
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "SELECT PR_PRDDS,PR_UOMCD,PR_STSFL,CMT_CODDS FROM CO_PRMST,CO_CDTRN WHERE CMT_CGMTP = 'STS'";
			M_strSQLQRY += " AND CMT_CGSTP = 'PRXXPRD' AND CMT_CODCD = PR_STSFL AND PR_PRDCD ='";
			M_strSQLQRY += strPRDCD +"'";
			
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
				{
					setMSG("Fetching the Product Details ..",'N');
					txtPRDDS.setText(nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),""));
					txtUOM.setText(nvlSTRVL(L_rstRSSET.getString("PR_UOMCD"),""));
					txtQLTFL.setText(nvlSTRVL(L_rstRSSET.getString("PR_STSFL"),""));
					txtQLTDS.setText(nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
					L_rstRSSET.close();
					return true;
				}
				else
				{
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						setMSG("Product Details not Found..",'E');
				}
				L_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getPRDDTL");
			return false;
		}
		return false;
	}
	/**
	 * Method to fetch the Quality parameter Specifications
	 * @param P_strCOND String argument to pass condiation
	 * @param P_strSTRDT String argument to pass Start Date
	 * @param P_strSRLNO String argument to pass Serial Number. 
	 */
	private boolean getSPCDTL(String P_strCOND,String P_strSTRDT,String P_strSRLNO)
	{
		int i=0;
		java.util.Date L_TMPDT;
		ResultSet L_rstRSSET;
		String L_TSMCD,L_strTSMDS;
		try
		{
			intRWCNT =0;
			if(tblQPRDL.isEditing())
				tblQPRDL.getCellEditor().stopCellEditing();
			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);
			tblQPRDL.clrTABLE();
			M_strSQLQRY = "SELECT * FROM CO_QPMST WHERE QP_QCATP = '" +strPRDTP +"'";
			M_strSQLQRY += " AND QP_PRDCD ='"+strPRDCD+"'";
			if(P_strCOND.trim().equals("CUR"))
			{
				M_strSQLQRY += " AND QP_SRLNO ='"+strDFTSR_fn.trim()+"'";
				M_strSQLQRY += " AND QP_ENDDT is null order by QP_ORDBY ";			
			}
			else if(P_strCOND.trim().equals("SPC"))
			{
				M_strSQLQRY += " AND QP_SRLNO ='"+txtSRLNO.getText().trim()+"'";//P_strSRLNO
				if(txtENDDT.getText().length() == 0)
					M_strSQLQRY += " AND QP_ENDDT is null";
				else	
					M_strSQLQRY += " AND QP_STRDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strSTRDT))+"' ORDER BY QP_ORDBY";
			}
			else if(P_strCOND.trim().equals("STY"))			
				M_strSQLQRY += " order by QP_NPFVL ";			
			strORGSRL ="";
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				while(L_rstRSSET.next())
				{
					if(i==0)
					{					
						txtSTRDT.setText(M_fmtLCDAT.format(L_rstRSSET.getDate("QP_STRDT")));				
						strORGSRL = nvlSTRVL(L_rstRSSET.getString("QP_SRLNO"),"");
						txtSRLNO.setText(strORGSRL);	
						if(P_strCOND.trim().equals("SPC"))
						{
							L_TMPDT = L_rstRSSET.getDate("QP_ENDDT");
							if(L_TMPDT != null)
							{							
								txtENDDT.setText(M_fmtLCDAT.format(L_TMPDT));				
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
								{
									cl_dat.M_btnSAVE_pbst.setEnabled(false);
								}
							}
							else if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))							
								cl_dat.M_btnSAVE_pbst.setEnabled(true);	
							if(!txtSRLNO.getText().trim().equals(strDFTSR_fn.trim()))							
								txtSRLDS.setText(cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXTCG",txtSRLNO.getText().trim()));
						
						}
					}
					tblQPRDL.setValueAt(new Boolean(true),i,TB_CHKFL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_QPRCD"),""),i,TB_QPRCD);
					//tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_QPRSL"),""),i,TB_QPRSL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_QPRDS"),""),i,TB_QPRDS);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_UOMDS"),""),i,TB_UOMDS);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_TSMDS"),""),i,TB_TSMDS);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_NPFVL"),""),i,TB_NPFVL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_NPTVL"),""),i,TB_NPTVL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_CMPFL"),""),i,TB_CMPFL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_PRGFL"),""),i,TB_PRGFL);
					if(strPRDTP.equals(strSTYRN_fn))
						tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_STDVL"),""),i,TB_TXTVL);
					else
						tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_TXTVL"),""),i,TB_TXTVL);
					tblQPRDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("QP_ORDBY"),""),i,TB_ORDBY);
					i++;
				}	
				intRWCNT =i;		
				L_rstRSSET.close();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))				
				{					
					/*try
					{					
						M_calLOCAL.setTime(M_fmtLCDAT.parse(txtSTRDT.getText().trim()));
						M_calLOCAL.add(java.util.Calendar.DATE,+ 1);
       					txtENDDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
					}
					catch(Exception L_EX)
					{	       
						setMSG(L_EX, "calDATE");						
					}*/		
					txtENDDT.requestFocus();
				}
			}
			if(i > 0)
				return true;	
			else
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))			
					setMSG("Specifications not available for the entered date..",'E');			
				return false;
			}
		}
		catch(Exception L_E)
		{
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_E,"getSPCDTL");
		}
		return true;
	}		
	/**
	 * Method to validate the data before execution of the	
	 */
	boolean vldDATA()
	{
		try
		{	
			if(txtPRDCD.getText().trim().length() == 0)			
			{
				setMSG("Please Enter valid Product Code, it cannot be empty ..",'E');				
				txtPRDCD.requestFocus();
				return false;
			}
			else if(txtSTRDT.getText().trim().length() == 0)			
			{	
				setMSG("Please Enter valid start date ..",'E');
				txtSTRDT.requestFocus();
				return false;
			}
			else if(txtSRLNO.getText().trim().length() == 0)			
			{	
				setMSG("Please Enter valid Serial No./ Grade Code ..",'E');
				txtSRLNO.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(txtENDDT.getText().trim().equals(""))
				{
					setMSG("End date can not be empty for modification of parameters..",'E');
					txtENDDT.requestFocus();
					return false;
				}
				else
				{
					if (M_fmtLCDAT.parse(txtENDDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtSTRDT.getText().trim()))<0)			
					{			    
						setMSG("End Date must be greater then Start Date..",'E');										
						return false;
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))			
					{
						if(!vldENDDT(txtENDDT.getText().trim()))
							cl_dat.M_btnSAVE_pbst.setEnabled(false);	
						else			
							cl_dat.M_btnSAVE_pbst.setEnabled(true);	

					}

				}
			}
			for(int i=0;i<intROWCT;i++)
			{
				if(tblQPRDL.getValueAt(i,TB_CHKFL).toString().equals("true"))
				{
					if(tblQPRDL.getValueAt(i,TB_QPRCD).toString().trim().length() ==0)
					{
						setMSG("Quality parameter code can not be blank..",'E');
						return false;
					}
					//else if(tblQPRDL.getValueAt(i,TB_QPRSL).toString().trim().length() ==0)
					//{
					//	setMSG("Quality parameter Srl.No. can not be blank..",'E');
					//	return false;
					//}
					else if(tblQPRDL.getValueAt(i,TB_QPRDS).toString().trim().length() ==0)
					{
						setMSG("Quality parameter Desc can not be blank..",'E');
						return false;
					}
					else if(tblQPRDL.getValueAt(i,TB_CMPFL).toString().trim().length() ==0)
					{
						setMSG("Check flag value can not be blank..",'E');
						return false;
					}
					else if(tblQPRDL.getValueAt(i,TB_PRGFL).toString().trim().length() ==0)
					{
						setMSG("Program flag value can not be blank..",'E');
						return false;
					}
					else if(tblQPRDL.getValueAt(i,TB_ORDBY).toString().trim().length() ==0)
					{
						setMSG("Order by value can not be blank..",'E');
						return false;
					}
					else if(tblQPRDL.getValueAt(i,TB_CMPFL).toString().trim().equals("Y"))
					{
						if(tblQPRDL.getValueAt(i,TB_NPFVL).toString().trim().length() == 0)
						{
							if(tblQPRDL.getValueAt(i,TB_NPTVL).toString().trim().length() == 0)
							{
								setMSG("For check flag value as 'Y' at least one value must be present",'E');
								return false;
							}
						}
						
					}
				}
			}
			return true;				
		}
		catch(Exception L_EX)
		{
			return false;
		}
	}
	/**
	 * Method to perform addition, Modification & deletion of data from the data base.
	 */
	void exeSAVE()
	{
		String L_strQPRCD="";
		//String L_strQPRSL="";
		String L_strSTRDT="";
		String L_strSRLNO="";
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(!vldDATA())
				return ;
			else
				setMSG("",'E');
			if(tblQPRDL.isEditing())
				tblQPRDL.getCellEditor().stopCellEditing();
			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
			
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{								
				//exeADDREC();
				setMSG("Saving of specification data is in progress..",'N'); 
				L_strSTRDT = txtSTRDT.getText().trim();
				L_strSRLNO = txtSRLNO.getText().trim();
				for(int i=0;i<intROWCT;i++)
				{
					L_strQPRCD = tblQPRDL.getValueAt(i,TB_QPRCD).toString();
					if(tblQPRDL.getValueAt(i,TB_CHKFL).toString().trim().equals("true"))
						exeADDQPR(L_strQPRCD.trim(),i,L_strSTRDT);
				}			 
			/*	if(cl_dat.M_flgLCUPD_pbst)
				{					
					setMSG("Data has been saved ..",'N');
					exeEMLOPT(txtSTRDT.getText().trim());
				}
				else				
					setMSG("Error in saving the data ..",'E');*/
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{								
				String L_strNPFVL ="";
				String L_strNPTVL ="";
				String L_strTXTVL ="";
				String L_strCMPFL ="";
				String L_strPRGFL ="";
				String L_strUOMDS ="";
				String L_strTSMDS ="";
				String L_strORDBY ="";	
				String L_strNEWDT ="";
				String L_strSQLQRY="";
				boolean L_flgFOUND= false;
				
				L_strSTRDT = txtSTRDT.getText().trim();
				L_strSRLNO = txtSRLNO.getText().trim();
				//L_strNEWDT = cc_dattm.getABDATE(txtENDDT.getText().trim(),1,'A');
				L_strNEWDT = txtENDDT.getText().trim();
				
				//// if default Specifications Exist & user want to add Specifications For other Group
				//// through Modification, this is is applicablt to handle such situation.
				/*L_strSQLQRY =" Select QP_STRDT from CO_QPMST";
				L_strSQLQRY +=" WHERE QP_QCATP ='"+strPRDTP.trim()+"'";
				L_strSQLQRY +=" AND QP_TSTTP ='"+strTSTTP.trim()+"'";
				L_strSQLQRY +=" AND QP_PRDCD ='"+strPRDCD.trim()+"'";
				L_strSQLQRY +=" AND QP_STRDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strSTRDT))+"'";
				L_strSQLQRY +=" AND QP_SRLNO ='"+txtSRLNO.getText().trim()+"'";
				L_strSQLQRY +=" AND QP_ENDDT is null";
				M_rstRSSET= cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())					
						L_flgFOUND = true;
					else
						L_flgFOUND= false;
					
					M_rstRSSET.close();
				}
				if(L_flgFOUND == true)*/
				{
					L_strSQLQRY ="UPDATE CO_QPMST set QP_ENDDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().trim()))+"',";
					L_strSQLQRY +="QP_TRNFL ='0',";
					L_strSQLQRY +="QP_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"',";
					L_strSQLQRY +="QP_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					L_strSQLQRY +=" WHERE QP_QCATP ='"+strPRDTP.trim()+"'";
					L_strSQLQRY +=" AND QP_TSTTP ='"+strTSTTP_fn.trim()+"'";
					L_strSQLQRY +=" AND QP_PRDCD ='"+strPRDCD.trim()+"'";
					L_strSQLQRY +=" AND QP_STRDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strSTRDT))+"'";
					L_strSQLQRY +=" AND QP_SRLNO ='"+strORGSRL.trim()+"'";
					L_strSQLQRY +=" AND QP_ENDDT is null";
					// close the previous specification range
					cl_dat.exeSQLUPD(L_strSQLQRY,"setLCLUPD");
				}
				for(int i=0;i<intROWCT;i++)
				{
					if(tblQPRDL.getValueAt(i,TB_CHKFL).toString().equals("true"))
					{
						L_strQPRCD = tblQPRDL.getValueAt(i,TB_QPRCD).toString();
						try
						{					
							M_calLOCAL.setTime(M_fmtLCDAT.parse(txtENDDT.getText().trim()));
							M_calLOCAL.add(java.util.Calendar.DATE,+ 1);
       						L_strNEWDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
						}
						catch(Exception L_EX)
						{	       
							setMSG(L_EX, "calDATE");						
						}		
						if(L_strQPRCD.trim().length() >0)
							exeADDQPR(L_strQPRCD,i,L_strNEWDT);
					}
				}				
			}					
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exesave"))
				{						
					exeMAILTO();
					//exeEMLOPT(txtSTRDT.getText().trim());
					clrCOMP();					
					tblQPRDL.clrTABLE();
				}	
				else
					setMSG("Error in Saving/Retrieving",'E');
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}		
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	public void exeMAILTO()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{								
				String L_strSQLQRY1,L_strSQLQRY2;
				ResultSet L_rstRSSET1,L_rstRSSET2;
				java.util.Date L_datNXTDT;
				Hashtable<String,String> hstFMQPR = new Hashtable<String,String>();
				Hashtable<String,String> hstTOQPR = new Hashtable<String,String>();
				
				String L_strBODY ="Quality Parameter Specs Modified for Product '"+txtPRDDS.getText().trim()+"' With Following Details \n\n";
				L_strBODY   +="Parameter Modified            Old Values                    New Values                    UOM\n\n";
				//L_strBODY +="Modified                      min - max                     min - max                        \n\n";
							
				M_calLOCAL.setTime(M_fmtLCDAT.parse(txtENDDT.getText().trim()));      
				M_calLOCAL.add(java.util.Calendar.DATE,1);
				L_datNXTDT = M_calLOCAL.getTime();

				L_strSQLQRY1 = " select QP_QPRCD,QP_NPFVL,QP_NPTVL,QP_UOMDS from co_qpmst where QP_ENDDT='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText().trim()))+"'  order by qp_qprcd";
				L_strSQLQRY2 = " select QP_QPRCD,QP_NPFVL,QP_NPTVL,QP_UOMDS from co_qpmst where QP_STRDT='"+M_fmtDBDAT.format(L_datNXTDT)+"'  order by qp_qprcd";
				L_rstRSSET1 = cl_dat.exeSQLQRY1(L_strSQLQRY1);
				L_rstRSSET2 = cl_dat.exeSQLQRY2(L_strSQLQRY2);
				//System.out.println("L_strSQLQRY1>>"+L_strSQLQRY1);
				//System.out.println("L_strSQLQRY2>>"+L_strSQLQRY2);
				int i=0;
				String strTEMP="";
				/*if(L_rstRSSET1 != null && L_rstRSSET2 !=null)
				{
					while(L_rstRSSET1.next() && L_rstRSSET2.next())
					{
						if(!L_rstRSSET1.getString("QP_NPFVL").equals(L_rstRSSET2.getString("QP_NPFVL")) ||
						   !L_rstRSSET1.getString("QP_NPTVL").equals(L_rstRSSET2.getString("QP_NPTVL")))
						{
							L_strBODY +=L_rstRSSET1.getString("QP_QPRCD");
							for(i=L_rstRSSET1.getString("QP_QPRCD").length();i<=30;i++) 
								L_strBODY +=" ";
							
							if(L_rstRSSET1.getInt("QP_NPFVL")==0 && L_rstRSSET1.getInt("QP_NPTVL")!=0)
								strTEMP=L_rstRSSET2.getString("QP_NPTVL")+" max";
							if(L_rstRSSET1.getInt("QP_NPFVL")!=0 && L_rstRSSET1.getInt("QP_NPTVL")==0)
								strTEMP=L_rstRSSET1.getString("QP_NPFVL")+" min";
							if(L_rstRSSET1.getInt("QP_NPFVL")==0 && L_rstRSSET1.getInt("QP_NPTVL")==0)
								strTEMP=" - ";
							if(L_rstRSSET1.getInt("QP_NPFVL")!=0 && L_rstRSSET1.getInt("QP_NPTVL")!=0)
								strTEMP=L_rstRSSET1.getString("QP_NPFVL")+" To "+L_rstRSSET1.getString("QP_NPTVL");
							
							L_strBODY +=strTEMP;
							for(i=strTEMP.length();i<=30;i++) 
								L_strBODY +=" ";
							
							if(L_rstRSSET2.getInt("QP_NPFVL")==0 && L_rstRSSET2.getInt("QP_NPTVL")!=0)
								strTEMP=L_rstRSSET2.getString("QP_NPTVL")+" max";
							if(L_rstRSSET2.getInt("QP_NPFVL")!=0 && L_rstRSSET2.getInt("QP_NPTVL")==0)
								strTEMP=L_rstRSSET2.getString("QP_NPFVL")+" min";
							if(L_rstRSSET2.getInt("QP_NPFVL")==0 && L_rstRSSET2.getInt("QP_NPTVL")==0)
								strTEMP=" - ";
							if(L_rstRSSET2.getInt("QP_NPFVL")!=0 && L_rstRSSET2.getInt("QP_NPTVL")!=0)
								strTEMP=L_rstRSSET2.getString("QP_NPFVL")+" To "+L_rstRSSET2.getString("QP_NPTVL");
							
							L_strBODY +=strTEMP;
							for(i=strTEMP.length();i<=30;i++) 
								L_strBODY +=" ";
							
							L_strBODY +=L_rstRSSET1.getString("QP_UOMDS")+"\n";
						}	
					}
				}*/
				
				

				if(L_rstRSSET1 != null)
				{
					while(L_rstRSSET1.next())
					{
						hstFMQPR.put(L_rstRSSET1.getString("QP_QPRCD"),L_rstRSSET1.getString("QP_NPFVL")+"~"+L_rstRSSET1.getString("QP_NPTVL")+"~"+(L_rstRSSET1.getString("QP_UOMDS").equals("")?"XX":L_rstRSSET1.getString("QP_UOMDS")));
					}
				}		
				if(L_rstRSSET2 != null)
				{
					while(L_rstRSSET2.next())
					{
						hstTOQPR.put(L_rstRSSET2.getString("QP_QPRCD"),L_rstRSSET2.getString("QP_NPFVL")+"~"+L_rstRSSET2.getString("QP_NPTVL")+"~"+(L_rstRSSET2.getString("QP_UOMDS").equals("")?"XX":L_rstRSSET2.getString("QP_UOMDS")));
					}
				}		
				
				for (Enumeration<String> e = hstFMQPR.keys(); e.hasMoreElements();)
				{	
					String strFMKEY= e.nextElement();
					if(hstTOQPR.containsKey(strFMKEY))
					{
						String[] staFMVAL = new String[3];
						String[] staTOVAL = new String[3];
						staFMVAL = hstFMQPR.get(strFMKEY).split("~");
						staTOVAL = hstTOQPR.get(strFMKEY).split("~");
							
						String strFM_NPFVL=staFMVAL[0];
						String strFM_NPTVL=staFMVAL[1];
						String strFM_UOMDS=staFMVAL[2];
						
						String strTO_NPFVL=staTOVAL[0];
						String strTO_NPTVL=staTOVAL[1];
						String strTO_UOMDS=staTOVAL[2];
						
						if(!strFM_NPFVL.equals(strTO_NPFVL)||!strFM_NPTVL.equals(strTO_NPTVL))
						{
							L_strBODY +=strFMKEY;
							for(i=strFMKEY.length();i<=30;i++) 
								L_strBODY +=" ";
									
							if(Float.parseFloat(strFM_NPFVL)==0 && Float.parseFloat(strFM_NPTVL)!=0)
								strTEMP=strFM_NPTVL+" max";
							if(Float.parseFloat(strFM_NPFVL)!=0 && Float.parseFloat(strFM_NPTVL)==0)
								strTEMP=strFM_NPFVL+" min";
							if(Float.parseFloat(strFM_NPFVL)==0 && Float.parseFloat(strFM_NPTVL)==0)
								strTEMP=" - ";
							if(Float.parseFloat(strFM_NPFVL)!=0 && Float.parseFloat(strFM_NPTVL)!=0)
								strTEMP=strFM_NPFVL+" To "+strFM_NPTVL;
									
							L_strBODY +=strTEMP;
							for(i=strTEMP.length();i<=30;i++) 
								L_strBODY +=" ";
									
							if(Float.parseFloat(strTO_NPFVL)==0 && Float.parseFloat(strTO_NPTVL)!=0)
								strTEMP=strTO_NPTVL+" max";
							if(Float.parseFloat(strTO_NPFVL)!=0 && Float.parseFloat(strTO_NPTVL)==0)
								strTEMP=strTO_NPFVL+" min";
							if(Float.parseFloat(strTO_NPFVL)==0 && Float.parseFloat(strTO_NPTVL)==0)
								strTEMP=" - ";
							if(Float.parseFloat(strTO_NPFVL)!=0 && Float.parseFloat(strTO_NPTVL)!=0)
								strTEMP=strTO_NPFVL+" To "+strTO_NPTVL;
									
							L_strBODY +=strTEMP;
							for(i=strTEMP.length();i<=30;i++) 
								L_strBODY +=" ";
									
							if(strFM_UOMDS.equals("XX"))
								L_strBODY +="\n";
							else 
								L_strBODY +=strFM_UOMDS+"\n";
						}	
					}
				}
				L_strBODY +=" \n With Effect From '"+M_fmtLCDAT.format(L_datNXTDT)+"'";
				//JOptionPane.showMessageDialog(null,L_strBODY);						
				cl_eml ocl_eml = new cl_eml();
				ocl_eml.sendfile("bc_gandhi@spl.co.in",null,"Quality Specs Modified",L_strBODY);
				ocl_eml.sendfile("css@spl.co.in",null,"Quality Specs Modified",L_strBODY);
				ocl_eml.sendfile("sr_deshpande@spl.co.in",null,"Quality Specs Modified",L_strBODY);
				setMSG("mail sent to bc_gandhi@spl.co.in and css@spl.co.in",'N');	
			}							
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeMAILTO()");
		}	
	}	
	
	
	
	/**
	 * Method to insert Quality parameter Details in the table CO_QPMST
	 * @param L_strQPRCD String argument to pass Quality parameter Code.
	 * @param P_intROWNO int argument to pass Row index.
	 * @param P_strSTRDT String argumnet to pass Start Date.
	 */
	private void exeADDQPR(String L_strQPRCD,int P_intROWNO,String P_strSTRDT)
	{
		try
		{
			//String L_strQPRSL ="";
			String L_strQPRDS ="";
			String L_strUOMDS ="";
			String L_strTSMDS ="";
			String L_strORDBY ="";
			String L_strSTRDT ="";
			String L_strSRLNO ="";
			String L_strNPFVL ="";
			String L_strNPTVL ="";
			String L_strTXTVL ="";
			String L_strCMPFL ="";
			String L_strPRGFL ="";
			L_strSTRDT = P_strSTRDT;
			if(L_strQPRCD.trim().length() >0)
			{
				//L_strQPRSL = tblQPRDL.getValueAt(P_intROWNO,TB_QPRSL).toString();
				L_strQPRDS = tblQPRDL.getValueAt(P_intROWNO,TB_QPRDS).toString();
				L_strUOMDS = tblQPRDL.getValueAt(P_intROWNO,TB_UOMDS).toString();
				L_strTSMDS = tblQPRDL.getValueAt(P_intROWNO,TB_TSMDS).toString();
				L_strORDBY = tblQPRDL.getValueAt(P_intROWNO,TB_ORDBY).toString();
						
				L_strNPFVL = tblQPRDL.getValueAt(P_intROWNO,TB_NPFVL).toString();
				L_strNPTVL = tblQPRDL.getValueAt(P_intROWNO,TB_NPTVL).toString();
				L_strCMPFL = tblQPRDL.getValueAt(P_intROWNO,TB_CMPFL).toString();
				L_strPRGFL = tblQPRDL.getValueAt(P_intROWNO,TB_PRGFL).toString();
				L_strTXTVL = tblQPRDL.getValueAt(P_intROWNO,TB_TXTVL).toString();
				L_strSTRDT = txtSTRDT.getText().trim();
				L_strSRLNO = txtSRLNO.getText().trim();
				 
				M_strSQLQRY =  "INSERT INTO CO_QPMST(QP_QCATP,QP_TSTTP,QP_PRDCD,QP_QPRCD,QP_STRDT,QP_SRLNO,QP_QPRDS,QP_UOMDS,QP_TSMDS,QP_NPFVL,QP_NPTVL,QP_CMPFL,QP_PRGFL,QP_TXTVL,QP_ORDBY,QP_TRNFL,QP_LUSBY,QP_LUPDT)VALUES(";
				M_strSQLQRY += "'"+ strPRDTP.trim() +"',";
				M_strSQLQRY += "'"+ strTSTTP_fn.trim() +"',";
				M_strSQLQRY += "'"+ strPRDCD.trim() +"',";
				M_strSQLQRY += "'"+ L_strQPRCD.trim() +"',";
				//M_strSQLQRY += "'"+ L_strQPRSL.trim() +"',";
				M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strSTRDT))+"',";
				M_strSQLQRY += "'"+ L_strSRLNO +"',";
				M_strSQLQRY += "'"+ L_strQPRDS +"',";
				M_strSQLQRY += "'"+ L_strUOMDS +"',";
				M_strSQLQRY += "'"+ L_strTSMDS +"',";
				if(L_strNPFVL.trim().length() >0)
					M_strSQLQRY +=L_strNPFVL+",";
				else
					 M_strSQLQRY +="0,";
				 
				if(L_strNPTVL.trim().length() >0)
					M_strSQLQRY +=L_strNPTVL+",";
				else
					 M_strSQLQRY +="0,";
				M_strSQLQRY +="'"+L_strCMPFL+"',";
				M_strSQLQRY +="'"+L_strPRGFL+"',";
				M_strSQLQRY +="'"+L_strTXTVL+"',";
				if(L_strORDBY.trim().length() >0)
					M_strSQLQRY += L_strORDBY+",";
				else
					 M_strSQLQRY +="0,";
				M_strSQLQRY +="'0',";	 
				M_strSQLQRY +="'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"' )";
		        cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeADDQPR");
		}	
	}
	/**
	 * Method to fetch the SPS Quality Parameter Details.
	 * @param P_strPRDCD String argument to pass Product Code. 
	 */
	private int getSPSPAR(String P_strPRDCD)
	{
		int i=0;
		try
		{
			if(tblQPRDL.isEditing())
				tblQPRDL.getCellEditor().stopCellEditing();
			tblQPRDL.setRowSelectionInterval(0,0);
			tblQPRDL.setColumnSelectionInterval(0,0);
			
			/*M_strSQLQRY = "select distinct QP_QPRCD,CMT_CODDS,CMT_CCSVL,CMT_CHP02,CMT_NMP01 from co_qpmst,co_cdtrn where QP_QCATP = '"+strPRDTP+"'";
	        M_strSQLQRY += " AND CMT_CODCD = QP_QPRCD AND CMT_CGMTP ='SYS' AND CMT_CGSTP ='QCXXQPM'";
			M_strSQLQRY += " AND QP_PRDCD like '"+ P_strPRDCD.substring(0,6)+"%'";
			M_strSQLQRY += " ORDER BY CMT_NMP01";*/
	        M_strSQLQRY = "select distinct QP_QPRCD,QS_QPRDS1,QS_UOMCD1,QS_TSMCD1,QS_TSTCN,QS_TSTSP,QS_ORDBY from co_qpmst,co_qsmst where QP_QCATP = '"+strPRDTP+"'";
	        M_strSQLQRY += " AND QS_QPRCD = QP_QPRCD AND ";
			M_strSQLQRY += " QP_PRDCD like '"+ P_strPRDCD.substring(0,6)+"%'";
			M_strSQLQRY += " ORDER BY QS_ORDBY";

			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			while (M_rstRSSET.next())
			{
				tblQPRDL.setValueAt(new Boolean(true),i,TB_CHKFL);
				tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),""),i,TB_QPRCD);
				//tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRSL"),""),i,TB_QPRSL);
				tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_QPRDS1"),""),i,TB_QPRDS);
				tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_UOMCD1"),""),i,TB_UOMDS);
				tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_TSMCD1"),""),i,TB_TSMDS);
				tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_TSTCN"),""),i,TB_TSTCN);
				tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_TSTSP"),""),i,TB_TSTSP);
				tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_ORDBY"),""),i,TB_ORDBY);
				tblQPRDL.setValueAt("N",i,TB_CMPFL);
				tblQPRDL.setValueAt("C_B",i,TB_PRGFL);
			    i++;
			}
			if(i==0)
			{
				
				/*M_strSQLQRY = "select distinct QP_QPRCD,CMT_CODDS,CMT_CCSVL,CMT_CHP02,CMT_NMP01 from co_qpmst,co_cdtrn where QP_QCATP = '"+strPRDTP+"'";
				M_strSQLQRY += " AND CMT_CODCD = QP_QPRCD AND CMT_CGMTP ='SYS' AND CMT_CGSTP ='QCXXQPM'";
				M_strSQLQRY += " AND QP_PRDCD like '"+ P_strPRDCD.substring(0,4)+"%'";
				M_strSQLQRY += " ORDER BY CMT_NMP01";*/
				M_strSQLQRY = "select distinct QP_QPRCD,QS_QPRDS1,QS_UOMCD1,QS_TSMCD1,QS_TSTCN,QS_TSTSP,QS_ORDBY from co_qpmst,co_qsmst where QP_QCATP = '"+strPRDTP+"'";
				M_strSQLQRY += " AND QS_QPRCD = QP_QPRCD ";
				M_strSQLQRY += " AND QP_PRDCD like '"+ P_strPRDCD.substring(0,4)+"%'";
				M_strSQLQRY += " ORDER BY QS_ORDBY";

				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
				while (M_rstRSSET.next())
				{
					tblQPRDL.setValueAt(new Boolean(true),i,TB_CHKFL);
					tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRCD"),""),i,TB_QPRCD);
					//tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QP_QPRSL"),""),i,TB_QPRSL);
					tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_QPRDS1"),""),i,TB_QPRDS);
					tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_UOMCD1"),""),i,TB_UOMDS);
					tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_TSMCD1"),""),i,TB_TSMDS);
					tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_TSTCN"),""),i,TB_TSTCN);
					tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_TSTSP"),""),i,TB_TSTSP);
					tblQPRDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("QS_ORDBY"),""),i,TB_ORDBY);
					tblQPRDL.setValueAt("N",i,TB_CMPFL);
					tblQPRDL.setValueAt("C_B",i,TB_PRGFL);
				    i++;
				}	
			}
		}
		catch(Exception L_SE)
		{
				setMSG("Error3"+L_SE.toString(),'E');
		}
		return i;
	}


private boolean vldENDDT(String LP_ENDDT)
{
	try
	{
			//String L_strNEWDT = cc_dattm.getABDATE(txtENDDT.getText().trim(),1,'A');
			M_calLOCAL.setTime(M_fmtLCDAT.parse(txtENDDT.getText().trim()));
			M_calLOCAL.add(java.util.Calendar.DATE,1);
			String L_strNEWDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
			
			if (M_fmtLCDAT.parse(txtENDDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)			
			{
				M_strSQLQRY = "select count(*)L_CNT from pr_ltmst where LT_CLSFL ='9' AND LT_CPRCD = '"+txtPRDCD.getText().trim()+"'";
				//M_strSQLQRY += " AND date(LT_CLSTM) between "+  M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strNEWDT))+" AND " +  M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				//M_strSQLQRY += " AND date(LT_CLSTM) between '"+ L_strNEWDT +"' AND " + "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			//	M_strSQLQRY += " AND LT_CLSTM between '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst+" 00:00")) +"' AND " + "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strNEWDT+" 00:00"))+"'";
				M_strSQLQRY += " AND LT_CLSTM between '"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strNEWDT+" 00:00")) +"' AND " + "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_strLOGDT_pbst +" 00:00"))+"'";

				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				if(M_rstRSSET.next())
				if(M_rstRSSET.getInt("L_CNT") >0)		
				{
					setMSG("Lots has been classified between the end date AND current date..",'E');
					return false;
				}
				else
				{
					setMSG("",'N');
					return true;		
				}
			}
			else
			{
				setMSG("",'N');
			}
	}
	catch(Exception L_SE)
	{
		setMSG("Error3"+L_SE.toString(),'E');
		return false;		
	}
	return true;
}
	/**
	* Method to generate the DOC file & to sed it as Email to the corresponding Departments.
	* @param P_strSTRDT String argument to pass start Date.
	*/
	private void exeEMLOPT(String P_strSTRDT)
	{
		ResultSet L_rstRSSET;
		String L_strSQLQRY ="",L_EMLID ="";
		String L_STROP ="";

		try
		{
			fosREPORT = new FileOutputStream(cl_dat.M_strREPSTR_pbst+"QC_SPCDT.doc");
			dosREPORT = new DataOutputStream(fosREPORT);
			dosREPORT.writeBytes("\n");			
			dosREPORT.writeBytes("Product Specifications for Grade   :  "+txtPRDDS.getText().trim());
			dosREPORT.writeBytes("\n\n");			
			dosREPORT.writeBytes("Product Type                       :  "+cmbPRDTP.getSelectedItem().toString().substring(2));			
			dosREPORT.writeBytes("\n\n");			
			dosREPORT.writeBytes("Specification Start Date : "+P_strSTRDT);
			dosREPORT.writeBytes("\n\n");			
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------ ");
				dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Code    Description                   Unit      TSM Code  Specification Range  Check Range" );
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n\n");			
			for(int i=0;i<intROWCT;i++)
			{
				if(tblQPRDL.getValueAt(i,TB_QPRCD).toString().trim().length()>0)
				{   
					dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_QPRCD).toString().trim(),8));
					dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_QPRDS).toString(),30));
					dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_UOMDS).toString(),10));
					dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_TSMDS).toString(),10));
					dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_NPFVL).toString(),12));
					dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_NPTVL).toString(),12));
					dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_CMPFL).toString(),5));
					//dosREPORT.writeBytes(padSTRING('R',tblQPRDL.getValueAt(i,TB_PRGFL).toString(),5));
					dosREPORT.writeBytes("\n");
				}
			}	
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n\n");			
			dosREPORT.writeBytes("You can Update Your Register.");
			dosREPORT.writeBytes("\n\n");			

			dosREPORT.writeBytes("K.V.Mujumdar");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("CE(Operations)");
			dosREPORT.writeBytes("\n");
			dosREPORT.close();
			fosREPORT.close();
		
			cl_eml ocl_eml = new cl_eml();
			L_strSQLQRY="Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='EML' AND CMT_CGSTP='QCXXGCH'";
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET !=null)
			{
				while(L_rstRSSET.next())	
				{
					L_EMLID = L_rstRSSET.getString("CMT_CODDS");
					if(L_EMLID !=null)
						//ocl_eml.sendfile(L_EMLID.trim(),cl_dat.M_strREPSTR_pbst+"QC_SPCDT.doc","Information of Reclassified Lot. ","");					
					setMSG("File is forwarded through E-Mail, Successfuly ",'N');				    
								
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E.toString(),'E');
		}
	}
	class INPVF extends InputVerifier
	{	
		public boolean verify( JComponent input)
		{
			try
			{
				if((input == txtPRDCD) &&(txtPRDCD.getText().trim().length() == 10))
				{							
					if(strPRDTP.equals(strSTYRN_fn))
				    {
						M_strSQLQRY = "select distinct QP_PRDCD,CT_MATDS,CT_UOMCD from CO_QPMST,CO_CTMST where QP_QCATP = '";
						M_strSQLQRY += strPRDTP +"' AND CT_MATCD = QP_PRDCD order by QP_PRDCD";																		
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET!= null)
						{							
							if(M_rstRSSET.next())
							{							
								txtPRDDS.setText(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));								
								M_rstRSSET.close();
								return true;
							}
							else
							{
								setMSG("Invalid Product Code, Press F1 to Select from List..",'E');
								M_rstRSSET.close();
								return false;
							}				
						}
					}
					else
					{
						M_strSQLQRY = "select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_PRDTP = '";
						M_strSQLQRY += strPRDTP +"' AND PR_STSFL <> 'X' AND PR_PRDCD = '"+ txtPRDCD.getText().trim() +"'";						
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET!= null)
						{						
							if(M_rstRSSET.next())
							{							
								txtPRDDS.setText(nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));							
								M_rstRSSET.close();
								return true;
							}
							else
							{
								setMSG("Invalid Product Code, Press F1 to Select from List..",'E');
								M_rstRSSET.close();
								return false;
							}	
						}
					}
				}				
				if(input == txtENDDT)
				{
					//System.out.println("End Date input verifier");
					if(txtENDDT.getText().length() == 0)
						return true;
					if (M_fmtLCDAT.parse(txtENDDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtSTRDT.getText().trim()))<0)			
					{			    
						setMSG("End Date must be greater then Start Date..",'E');										
						return false;
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))			
					{
						if(!vldENDDT(txtENDDT.getText().trim()))
							cl_dat.M_btnSAVE_pbst.setEnabled(false);	
						else
							cl_dat.M_btnSAVE_pbst.setEnabled(true);	
					}
				}
				if((input == txtSRLNO) &&(txtSRLNO.getText().trim().length() == 5))
				{
					if(txtSRLNO.getText().equals(strDFTSR_fn))
					{
						//txtSRLDS.setText("Common Spec.");
						return true;	
					}
					M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " AND CMT_CGSTP = 'QCXXTCG' AND CMT_CODCD = '"+ txtSRLNO.getText().trim() +"'";					
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!= null)
					{
						if(M_rstRSSET.next())
						{
							txtSRLDS.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
							M_rstRSSET.close();														
						}
						else
						{
							
							setMSG("Invalid Serial Number, Press F1 to select from List..",'E');
							M_rstRSSET.close();
							return false;
						}				
					}
				}
			}
	        catch (Exception L_EX)
	        {
				setMSG(L_EX,"INPVF");
	            return false;
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
				if(P_intCOLID == TB_QPRCD)
				{
					String L_strCODCD = "";					
					L_strCODCD="";;		
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))			
					 return true;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))			
					{ 
						if(P_intROWID < intRWCNT -1)
							return true;
					}
					String L_strTEMP = tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_QPRCD).toString();
					if(L_strTEMP.length() == 0)					
						return true;
					
					for(int i=0; i<intROWCT; i++)
					{							
						if (i ==  tblQPRDL.getSelectedRow())						
							continue;						
						L_strCODCD = (tblQPRDL.getValueAt(i,TB_QPRCD).toString());
						if(!L_strCODCD.equals(""))
						{							
							if(L_strCODCD.equals(L_strTEMP))
							{								
								setMSG("Duplicate Quality Parameter is not allowed..",'E');
								return false;
							}							
						}
					}								
					M_strSQLQRY = "select SUBSTRING(QS_QPRCD,1,3)TS_QPRCD,QS_QPRDS1,QS_UOMCD1,QS_TSMCD1,QS_TSTCN,QS_TSTSP,QS_ORDBY from co_qsmst ";
					M_strSQLQRY += " where SUBSTRING(QS_QPRCD,1,3) = '"+ L_strTEMP+"'";								
					//System.out.println("M_strSQLQRY(verify)>>"+M_strSQLQRY);					
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
						//	if(tblQPRDL.isEditing())
						//		tblQPRDL.getCellEditor().stopCellEditing();
						//	tblQPRDL.setRowSelectionInterval(0,0);
						//	tblQPRDL.setColumnSelectionInterval(0,0);
														
							String L_strQPRCD = L_strTEMP;
							String L_strQPRDS = nvlSTRVL(M_rstRSSET.getString("QS_QPRDS1"),"");
							String L_strUOMDS = nvlSTRVL(M_rstRSSET.getString("QS_UOMCD1"),"");
							String L_strTSMDS = nvlSTRVL(M_rstRSSET.getString("QS_TSMCD1"),"");
							String L_strTSTCN = nvlSTRVL(M_rstRSSET.getString("QS_TSTCN"),"");
							String L_strTSTSP = nvlSTRVL(M_rstRSSET.getString("QS_TSTSP"),"");
							String L_strORDBY = nvlSTRVL(M_rstRSSET.getString("QS_ORDBY"),"");
			
							tblQPRDL.setValueAt(new Boolean(true),tblQPRDL.getSelectedRow(),TB_CHKFL);
							tblQPRDL.setValueAt(L_strQPRCD,tblQPRDL.getSelectedRow(),TB_QPRCD);
							tblQPRDL.setValueAt(L_strQPRDS,tblQPRDL.getSelectedRow(),TB_QPRDS);
							tblQPRDL.setValueAt(L_strUOMDS,tblQPRDL.getSelectedRow(),TB_UOMDS);
							tblQPRDL.setValueAt(L_strTSMDS,tblQPRDL.getSelectedRow(),TB_TSMDS);
							tblQPRDL.setValueAt(L_strTSTCN,tblQPRDL.getSelectedRow(),TB_TSTCN);
							tblQPRDL.setValueAt(L_strTSTSP,tblQPRDL.getSelectedRow(),TB_TSTSP);
							tblQPRDL.setValueAt(L_strORDBY,tblQPRDL.getSelectedRow(),TB_ORDBY);
							tblQPRDL.setValueAt("C_B",tblQPRDL.getSelectedRow(),TB_PRGFL);
							tblQPRDL.setValueAt("N",tblQPRDL.getSelectedRow(),TB_CMPFL);
			
							//tblQPRDL.setRowSelectionInterval(0,0);
							//tblQPRDL.setColumnSelectionInterval(0,0);
							tblQPRDL.editCellAt(tblQPRDL.getSelectedRow(),TB_QPRCD);
							tblQPRDL.cmpEDITR[TB_QPRCD].requestFocus();
						}
						else
						{
							setMSG("Invalid Quality Parameter, Press F1 To Select from List..",'E');
							M_rstRSSET.close();
							return false;
						}									
					}
																																										
				}
				//if(P_intCOLID == TB_QPRSL)
				//{
				//	String L_strCODCD = "";					
				//	L_strCODCD="";;		
				//	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))			
				//	 return true;
				//	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))			
				//	{ 
				//		if(P_intROWID < intRWCNT -1)
				//			return true;
				//	}
				//	String L_strTEMP = tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_QPRSL).toString();										
				//	if(L_strTEMP.length() == 0)					
				//		return true;
				//	if(L_strTEMP.equals("00"))
				//		return true;
				//	M_strSQLQRY = "select RM_SRLNO,RM_QPRDS,RM_TSTSP, RM_TSTCN, RM_UOMDS,RM_TSTMT from QP_RMMST where RM_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and RM_QPRCD = '"+tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_QPRCD).toString()+"'  and RM_SRLNO = '"+tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_QPRSL).toString()+"' order by RM_SRLNO";
				//	M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				//	if(M_rstRSSET != null)
				//	{
				//		if(M_rstRSSET.next())
				//		{
				//										
				//			String L_strQPRSL = L_strTEMP.toUpperCase();
				//			String L_strQPRDS1 = nvlSTRVL(M_rstRSSET.getString("RM_QPRDS"),"");
				//			String L_strUOMDS1 = nvlSTRVL(M_rstRSSET.getString("RM_UOMDS"),"");
				//			String L_strTSTSP = nvlSTRVL(M_rstRSSET.getString("RM_TSTSP"),"");
				//			String L_strTSTCN = nvlSTRVL(M_rstRSSET.getString("RM_TSTCN"),"");
				//			String L_strTSTMT = nvlSTRVL(M_rstRSSET.getString("RM_TSTMT"),"");
			
				//			tblQPRDL.setValueAt(new Boolean(true),tblQPRDL.getSelectedRow(),TB_CHKFL);
				//			tblQPRDL.setValueAt(L_strTSTSP,tblQPRDL.getSelectedRow(),TB_TSTSP);
				//			tblQPRDL.setValueAt(L_strTSTCN,tblQPRDL.getSelectedRow(),TB_TSTCN);
				//			tblQPRDL.setValueAt(L_strQPRDS1,tblQPRDL.getSelectedRow(),TB_QPRDS);
				//			tblQPRDL.setValueAt(L_strUOMDS1,tblQPRDL.getSelectedRow(),TB_UOMDS);
				//			tblQPRDL.setValueAt(L_strTSTMT,tblQPRDL.getSelectedRow(),TB_TSMDS);
				//			tblQPRDL.editCellAt(tblQPRDL.getSelectedRow(),TB_QPRSL);
				//			tblQPRDL.cmpEDITR[TB_QPRSL].requestFocus();
				//		}
				//		else
				//		{
				//			setMSG("Invalid Quality Parameter Srl No., Press F1 To Select from List..",'E');
				//			M_rstRSSET.close();
				//			return false;
				//		}									
				//	}
																																										
				//}
				if(P_intCOLID == TB_CMPFL)
				{
					if(!tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_CMPFL).toString().equals(""))
					{
						String L_strTEMP = tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_CMPFL).toString();
						if((L_strTEMP.equals("y"))||(L_strTEMP.equals("n")))						
						{
							tblQPRDL.setValueAt(L_strTEMP.toUpperCase(), tblQPRDL.getSelectedRow(),TB_CMPFL);						
							return true;
						}
						else if((L_strTEMP.equals("Y"))||(L_strTEMP.equals("N")))
							return true;
						else
						{							
							setMSG("Check Flag must be 'Y' for Yes or 'N' for No..",'E');
							return false;
						}													
					}
					else
					{
						setMSG("Check Flag cannot be Blank..",'E');
						return false;
					}
				}
				if(P_intCOLID == TB_ORDBY)
				{
					String L_strCODCD = "";
					String L_strORDBY="";;					
					String L_strTEMP = tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_ORDBY).toString();					
					if(L_strTEMP.length() == 0)					
						return true;					
					for(int i=0; i<intROWCT; i++)
					{												
						if (i ==  tblQPRDL.getSelectedRow())						
							continue;						
						L_strORDBY = tblQPRDL.getValueAt(i,TB_ORDBY).toString();												
						if(!L_strORDBY.equals(""))
						{							
							if(L_strORDBY.equals(L_strTEMP))
							{
								setMSG("Duplicate order position is not allowed..",'E');
								return false;													
							}
						}
					}
				}
				if(P_intCOLID == TB_PRGFL)
				{					
					M_strSQLQRY = "select CMT_SHRDS,CMT_CODDS from CO_CDTRN where CMT_CGMTP = ";
					M_strSQLQRY += "'SYS'"+ " AND  CMT_CGSTP =" +  "'QCXXPCT' AND CMT_SHRDS ='"+ tblQPRDL.getValueAt(tblQPRDL.getSelectedRow(),TB_PRGFL).toString() +"'";
					
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET!= null)
					{
						if(M_rstRSSET.next())						
							M_rstRSSET.close();																				
						else
						{
							setMSG("Invalid Program Flag, Press F1 to select from List..",'E');
							M_rstRSSET.close();
							return false;
						}				
					}
				}					
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"table verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}
   public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC==txtQPRCD) 
			{
				setMSG("Press F1 to select Parameter Code & then press Enter.",'N');
				if(tblQPRDL.getSelectedRow()<intRWCNT)
					((JTextField)tblQPRDL.cmpEDITR[TB_QPRCD]).setEditable(false);
				else
					((JTextField)tblQPRDL.cmpEDITR[TB_QPRCD]).setEditable(true);
			}
			//else if(M_objSOURC==txtQPRSL) 
			//{
			//	setMSG("Press F1 to select Q.Para Srl.No. & then press Enter.",'N');
				//if(tblQPRDL.getSelectedRow()<intRWCNT)
				//	((JTextField)tblQPRDL.cmpEDITR[TB_QPRSL]).setEditable(false);
				//else
				//	((JTextField)tblQPRDL.cmpEDITR[TB_QPRSL]).setEditable(true);
			//}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"focus gained");
		}
	}


}
