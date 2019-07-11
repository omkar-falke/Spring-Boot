/*
System Name		: Materials Management System
Program Name	: Deprtment wise ABC / XYZ Analysis Reports
Author			: Mr.S.R.Mehesare

Modified Date	: 08/11/2005 // Errors in XYZ report
Documented Date	: 08/11/2005
Version			: v2.0.0
*/

import java.io.FileOutputStream;
import java.io.DataOutputStream;import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import javax.swing.BorderFactory;import javax.swing.JComboBox;import javax.swing.JLabel;
import javax.swing.JPanel;import javax.swing.JTextField;import javax.swing.JCheckBox;
import javax.swing.JComponent;import javax.swing.InputVerifier;import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.util.Vector;
import java.util.StringTokenizer;import java.util.Arrays;import java.sql.ResultSet;import java.awt.Font;

/**<pre>
System Name  : Material Management System.
 
Program Name : Department wise ABC / XYZ Analysis Reports 

Purpose : A common report program for : 
         1) Department wise ABC Analysis 
         2) Department wise XYZ Analysis 

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #
HR_EPMST       EP_EMPNO                                                #
MM_STPRC       STP_STRTP,STP_MATCD                                     #
MM_ISMST       IS_STRTP,IS_MATCD,IS_ISSNO,IS_TAGNO                     #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name     Column Name    Table name     Type/Size    Description
--------------------------------------------------------------------------------------
rdbABC                                                    ABC Report Type
rdbXYZ                                                    XYZ Report Type
cmbRPTTP                                                  Report Type
txtFMDAT      IS_ISSDT        MM_ISMST       Date         Issue Date.
txtTODAT      IS_ISSDT        MM_ISMST       Date         Issue Date.
txtA_VAL                                                  Value of A for ABC Report
txtB_VAL                                                  Value of B for ABC Report
txtC_VAL                                                  Value of C for ABC Report
txtDPTCD      EP_DPTCD         HR_EPMST      VARCHAR(3)   Department Code
txtDPTNM      CMT_CODDS        CO_CDTRN      VARCHAR(3)   Department Name
txtX_VAL                                                  Value of X for XYZ Report.
txtY_VAL                                                  Value of Y for XYZ Report.
txtZ_VAL                                                  Value of Z for XYZ Report.
--------------------------------------------------------------------------------------
Report Criterias Available :  
    1) Deparment is retrieved from HRMS by using Employee code in SA_USMST
    2) For users from user type MM000, AU01and M010, Department entry option is open. (User types are hard coded in a final string array)
       - ABC : For a given period
       - XYZ : Upto a given date
These reports are available in dos format only. Separate panels are used for input components for separate reports
<I>
<B>Query : </B>
For ABC Report Data is taken from MM_ISMST & CO_CTMST for condiations :-
    1) STP_MATCD like Specified Report Type Code
    2) AND  CT_MATCD = IS_MATCD  
    3) AND IS_STRTP = Specified Store type 
    4) AND IS_DPTCD = specified Department Code
    5) AND IS_ISSDT between Specified Date Range.
    6) AND IS_ISSVL > 0 
    group by IS_MATCD, CT_MATDS,CT_UOMCD order by IS_ISSVL desc");

For XYZ Report Data is taken from MM_ISMST & CO_CTMST for condiations :-
    1) IS_MATCD like like Specified Report Type Code
    2) AND CT_MATCD = STP_MATCD 
    3) AND CT_OWNBY = Specified department Code 
    4) AND STP_STRTP = Specified Store Type 
    5) AND STP_YCSVL>0 order by STP_YCSVL desc");
  
<B>Validations & Other Information:</B>    
    - Date Range must be valid i.e. To date must be smaller than current date & greater than from Date.
    - Department Code Specified must be valid.
</I> */
class mm_rpdax extends cl_rbase
{
									/**	JRedioButton to Specify ABC analysis.*/
	private JRadioButton rdbABC;	/**	JRedioButton to Specify XYZ analysis.*/
	private JRadioButton rdbXYZ;	/**	JCombo to specify the Report type.*/
	private cl_Combo cmbRPTTP;		/** JTextField to specify Date Range for XYZ Analysis.*/	
	private JTextField txtFMDAT;	/** JTextField to specify Date Range for XYZ Analysis.*/	
	private JTextField txtTODAT;	/**	JTextField to specify Value of A index in ABC analysis.*/
	private JTextField txtA_VAL;	/**	JTextField to specify Value of B index in ABC analysis.*/
	private JTextField txtB_VAL;	/**	JTextField to specify Value of C index in ABC analysis.*/
	private JTextField txtC_VAL;	/**	JPanel to display components for ABC analysis.*/
	private JPanel pnlABC;			/**	JTextField to specify the Department Code.*/	
	private JTextField txtDPTCD;	/**	JtextField to specify the Department Name.*/
	private JTextField txtDPTNM;	/**	JPanel to display components for Department.*/
	private JPanel pnlDPTCD;		/**	To Date in XYZ analysis */	
	private JTextField txtTODATX;	/** Value of X index in XYZ analysis */
	private JTextField txtX_VAL;	/** Value of Y index in XYZ analysis */
	private JTextField txtY_VAL;	/** Value of Z index in XYZ analysis */
	private JTextField txtZ_VAL;	/** Panel for XYZ analysis */
	private JPanel pnlXYZ;			/** String variable for Department Name.*/
	private String strDPTNM;		/**	Vector for details of records retrieved as : String[] of MATCD,MATDS,UOM,sum(ISSQT),sum(ISSVL) */
	private Vector<String[]> vtrISKEY;		/**	Vector for Values of records retrieved as : sum(ISSVL) */
	private Vector<Float> vtrISSVL;		/** String variable for generated report File Name.*/
	private String strFILNM;		/** Integer variable to count the Number Records fetched to block the report if no data found.*/
	private int intRECCT;			/**	DataOutputStream Object to generate & to hold the report data in the Stream.*/
	private DataOutputStream dosREPORT;	/**	FileOutputStream Object to generate the Report file from the stream of data.*/
	private FileOutputStream fosREPORT;	/** Final String Array for User Type.*/
	private final String[] staUSRTP_fn = new String[]{"MM000","AU1","MM010"};/** String variable to append Dotted Line in the Report file. */		
	private String strDOTLN = "---------------------------------------------------------------------------------------------------------";
	//private String strUSRTP;	
	mm_rpdax()
	{
		super(2);
		try
		{
			setMatrix(20,6);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			
			setMatrix(20,24);
			JPanel L_pnlANATP=new JPanel(null);
			add(rdbABC=new JRadioButton("ABC"),1,2,1,4,L_pnlANATP,'L');
			add(rdbXYZ=new JRadioButton("X-Y-Z"),1,7,1,4,L_pnlANATP,'L');
			ButtonGroup L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbABC);L_btgTEMP.add(rdbXYZ);
			L_pnlANATP.setBorder(BorderFactory.createTitledBorder("    Analysis Type    "));
			add(L_pnlANATP,3,8,2,11.1,this,'L');
			
			pnlDPTCD=new JPanel(null);
			add(new JLabel("Department"),1,2,1,6,pnlDPTCD,'L');
			add(txtDPTCD=new TxtNumLimit(3.0),1,5,1,2,pnlDPTCD,'L');
			add(txtDPTNM=new JTextField(),1,7,0.95,7,pnlDPTCD,'L');

			txtDPTNM.setEnabled(false);
			add(pnlDPTCD,5,7,2,13.1,this,'L');			
			
			add(new JLabel("Report Type"),7,8,1,3,this,'L');
			add(cmbRPTTP=new cl_Combo(),7,11,1,8,this,'L');
			cmbRPTTP.addItem("Select Report Type","");
			cmbRPTTP.addItem("Packing Material","69");
			cmbRPTTP.addItem("Raw Material","68");
			cmbRPTTP.addItem("Spares","");
			
			JLabel L_lblTEMP1=new JLabel(" /"),L_lblTEMP2=new JLabel(" /");
			L_lblTEMP1.setFont(new Font("Arial",Font.BOLD,20));
			L_lblTEMP2.setFont(new Font("Arial",Font.BOLD,20));
			pnlABC=new JPanel(null);
			add(new JLabel("A / B / C Values"),1,2,1,3,pnlABC,'L');
			add(txtA_VAL=new TxtNumLimit(2.0),1,5,1,2,pnlABC,'L');
			add(L_lblTEMP1,1,7,1,1,pnlABC,'R');
			add(txtB_VAL=new TxtNumLimit(2.0),1,8,1,2,pnlABC,'L');
			add(L_lblTEMP2,1,10,1,1,pnlABC,'L');
			add(txtC_VAL=new TxtNumLimit(2.0),1,11,1,2,pnlABC,'L');
			add(new JLabel("From date"),2,2,1,3,pnlABC,'L');
			add(txtFMDAT=new TxtDate(),2,5,1,3,pnlABC,'L');
			add(new JLabel("To date"),3,2,1,3,pnlABC,'L');
			add(txtTODAT=new TxtDate(),3,5,1,3,pnlABC,'L');
			add(pnlABC,8,7,5,13,this,'L');
			pnlABC.setVisible(false);
			JLabel L_lblTEMP3=new JLabel(" /"),L_lblTEMP4=new JLabel(" /");
			L_lblTEMP3.setFont(new Font("Arial",Font.BOLD,20));
			L_lblTEMP4.setFont(new Font("Arial",Font.BOLD,20));
			pnlXYZ=new JPanel(null);
			add(new JLabel("X / Y / Z Values"),1,2,1,3,pnlXYZ,'L');
			add(txtX_VAL=new TxtNumLimit(2.0),1,5,1,2,pnlXYZ,'L');
			add(L_lblTEMP3,1,7,1,1,pnlXYZ,'R');
			add(txtY_VAL=new TxtNumLimit(2.0),1,8,1,2,pnlXYZ,'L');
			add(L_lblTEMP4,1,10,1,1,pnlXYZ,'L');
			add(txtZ_VAL=new TxtNumLimit(2.0),1,11,1,2,pnlXYZ,'L');
			add(new JLabel("To date"),2,2,1,3,pnlXYZ,'L');
			add(txtTODATX=new TxtDate(),2,5,1,3,pnlXYZ,'L');
			add(pnlXYZ,9,7,4,13,this,'L');
			pnlXYZ.setVisible(false);
			setMatrix(20,6);
			M_pnlRPFMT.setVisible(true);
			cmbRPTTP.addActionListener(this);		
			txtDPTCD.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super class method overrided to enable & disable the Components.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		txtDPTNM.setEnabled(false);
	}
	public void actionPerformed(ActionEvent P_AE)
	{
		super.actionPerformed(P_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					rdbABC.requestFocus();
					setENBL(true);
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == rdbABC)
			{
				if(rdbABC.isSelected())
				{
					pnlXYZ.setVisible(false);
					pnlABC.setVisible(true);
				}
				if(pnlDPTCD.isVisible())
				{
					txtDPTCD.requestFocus();
					txtDPTNM.setEnabled(false);
				}
				else
					cmbRPTTP.requestFocus();
			}
			else if(M_objSOURC == rdbXYZ)
			{
				if(rdbXYZ.isSelected())
				{
					pnlXYZ.setVisible(true);
					pnlABC.setVisible(false);
				}
				cmbRPTTP.requestFocus();
			}
			else if(M_objSOURC == txtTODAT || M_objSOURC == txtTODATX )
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	
	public void keyPressed(KeyEvent P_KE)
	{
		super.keyPressed(P_KE);
		try
		{
			if(M_objSOURC == txtDPTCD && P_KE.getKeyCode() == P_KE.VK_F1)
			{
				M_strHLPFLD = "txtDPTCD";
				cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT' and isnull(cmt_stsfl,' ') <>'X' ORDER BY CMT_CODCD" ,1,1,new String[] {"Dept Code","Name"},2 ,"CT");
			}
			else if(P_KE.getKeyCode() == P_KE.VK_ENTER)
				((JComponent)M_objSOURC).transferFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	public void focusGained(FocusEvent P_FE)
	{
		super.focusGained(P_FE);
		try
		{
			if(!M_flgERROR)
			{
				if(M_objSOURC == txtA_VAL)
					setMSG("Enter 'A' Index ..",'N');
				else if(M_objSOURC == txtB_VAL)
					setMSG("Enter 'B' Index ..",'N');
				else if(M_objSOURC == txtC_VAL)
				{
					if(txtC_VAL.getText().length()==0)
					{
						if(txtA_VAL.getText().length()>0 && txtB_VAL.getText().length()>0 && txtC_VAL.getText().length()==0)
						{
							if(100.0f-Float.parseFloat(txtB_VAL.getText())-Float.parseFloat(txtA_VAL.getText())<=0.0f)
							{
								setMSG("Invalid B index ..",'E');
								txtB_VAL.requestFocus();
								return;
							}
							txtC_VAL.setText(setNumberFormat(100.0f-Float.parseFloat(txtB_VAL.getText())-Float.parseFloat(txtA_VAL.getText()),0));
							txtC_VAL.select(0,txtC_VAL.getText().length());
						}
					}
					setMSG("Enter 'C' Index ..",'N');
				}
				else if(M_objSOURC == txtX_VAL)
					setMSG("Enter 'X' Index ..",'N');
				else if(M_objSOURC == txtY_VAL)
					setMSG("Enter 'Y' Index ..",'N');
				else if(M_objSOURC == txtZ_VAL)
				{
					if(txtZ_VAL.getText().length()==0)
					{
						if(txtX_VAL.getText().length()>0 && txtY_VAL.getText().length()>0 && txtZ_VAL.getText().length()==0)
						{
							if(100.0f-Float.parseFloat(txtY_VAL.getText())-Float.parseFloat(txtX_VAL.getText())<=0)
							{
								setMSG("Invalid Y index ..",'E');
								txtY_VAL.requestFocus();
								return;
							}
							txtZ_VAL.setText(setNumberFormat(100.0f-Float.parseFloat(txtY_VAL.getText())-Float.parseFloat(txtX_VAL.getText()),0));
							txtZ_VAL.select(0,txtZ_VAL.getText().length());
						}
					}
					setMSG("Enter 'Z' Index ..",'N');
				}
				else if(M_objSOURC == txtFMDAT )
					setMSG("Enter From Date ..",'N');
				else if(M_objSOURC == txtTODAT )
				{
					txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText());
					txtTODAT.select(0,txtTODAT.getText().length());
					setMSG("Enter To Date ..",'N');
				}
				else if(M_objSOURC == txtTODATX)
				{
					txtTODATX.setText(cl_dat.M_txtCLKDT_pbst.getText());
					txtTODATX.select(0,txtTODATX.getText().length());
					setMSG("Enter To Date ..",'N');
				}
				else if(M_objSOURC == cmbRPTTP)//9223488843 kumar alainz india
					setMSG("Select Report Type ..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"FocusGained");
		}
	}
	/** 
	 * Super class method overrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			cl_dat.M_wndHLP_pbst=null;
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				txtDPTCD.setText(L_STRTKN.nextToken());
				txtDPTNM.setText(L_STRTKN.nextToken());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpdax.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpdax.doc";
			
			getDATA();
			
			if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Deprtment wise ABC / XYZ Analysis Reports"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to validate the inputs before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		try
		{
			cl_dat.M_PAGENO = 0;						
			setMSG("",'N');
			if(pnlDPTCD.isVisible() && txtDPTCD.getText().length() == 0)
			{
				setMSG("Please enter Department ..",'E');
				txtDPTCD.requestFocus();
				return false;
			}
			if(rdbABC.isSelected())
			{//ABC ANALYSIS
				if(txtA_VAL.getText().length()==0)
				{
					setMSG("Please enter A Index ..",'E');
					txtC_VAL.requestFocus();
					return false;
				}
				if(txtB_VAL.getText().length()==0)
				{
					setMSG("Please enter B Index ..",'E');
					txtB_VAL.requestFocus();
					return false;
				}
				if(txtC_VAL.getText().length()==0)
				{
					setMSG("Please enter C Index ..",'E');
					txtC_VAL.requestFocus();
					return false;
				}
				if(Float.parseFloat(txtA_VAL.getText())+Float.parseFloat(txtB_VAL.getText())+Float.parseFloat(txtC_VAL.getText())!=100.0f)
				{
					setMSG("Total of A, B, C indices should be 100 ..",'E');
					txtC_VAL.requestFocus();
					return false;
				}
				if(txtFMDAT.getText().length()==0)
				{
					setMSG("Please enter From Date ..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				if(txtTODAT.getText().length()==0)
				{
					setMSG("Please enter To Date ..",'E');
					txtTODAT.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("To date cannot be greater than today ..",'E');
					txtTODAT.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtTODAT.getText()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText()))<0)
				{
					setMSG("To date cannot be smaller than From Date ..",'E');
					txtTODAT.requestFocus();
					return false;
				}
			}
			else if(rdbXYZ.isSelected())
			{
				if(txtX_VAL.getText().length()==0)
				{
					setMSG("Please enter X Index ..",'E');
					txtX_VAL.requestFocus();
					return false;
				}
				if(txtY_VAL.getText().length()==0)
				{
					setMSG("Please enter Y Index ..",'E');
					txtY_VAL.requestFocus();
					return false;
				}
				if(txtZ_VAL.getText().length()==0)
				{
					setMSG("Please enter Z Index ..",'E');
					txtZ_VAL.requestFocus();
					return false;
				}
				if(Float.parseFloat(txtX_VAL.getText())+Float.parseFloat(txtY_VAL.getText())+Float.parseFloat(txtZ_VAL.getText())<100.0f)
				{
					setMSG("Total of X, Y, Z indices should be 100 ..",'E');
					txtZ_VAL.requestFocus();
					return false;
				}
				if(txtTODATX.getText().length()==0)
				{
					setMSG("Please enter To Date ..",'E');
					txtTODATX.requestFocus();
					return false;
				}
				if(M_fmtLCDAT.parse(txtTODATX.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("To date cannot be greater than today ..",'E');
					txtTODATX.requestFocus();
					return false;
				}
			}
			else
			{
				setMSG("Please select Analysis Method ..",'E');
				rdbABC.requestFocus();
				return false;
			}			
			if(cmbRPTTP.getSelectedIndex()==0)
			{
				setMSG("Please select Report Type ..",'E');
				cmbRPTTP.requestFocus();
				return false;
			}			
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}			
	/**
	 * Method to fetch data feom the databse & to club it with header & footer of the Report.
	 */
	void getDATA()
	{
		try
		{
			String L_strDPTCD="";	//Retrieve department Code
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Deprtment wise ABC / XYZ Analysis Reports</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			if(pnlDPTCD.isVisible())
			{
				L_strDPTCD=txtDPTCD.getText();
				strDPTNM=txtDPTNM.getText();
			}			
			String L_strCOLNM="";//Name of the column to be updated in back end
			String L_strFILTR="where ";//Report type filter
			if(cmbRPTTP.getSelectedIndex() < 3)//Raw material or pkg. material selected
				L_strFILTR+=(rdbXYZ.isSelected() ? " STP_MATCD " : " IS_MATCD ")+"  like '"+cmbRPTTP.getITMCD()+"%' ";
			else//Spares selected
				L_strFILTR+= " substring("+(rdbXYZ.isSelected() ? " STP_MATCD " : " IS_MATCD ")+",1,2) not in ('"+cmbRPTTP.getITMCDAt(1)+"','"+cmbRPTTP.getITMCDAt(2)+"') ";
			if(rdbABC.isSelected())
			{//For ABC analysis
				M_rstRSSET=cl_dat.exeSQLQRY0("Select IS_MATCD,CT_MATDS,CT_UOMCD,sum(IS_ISSQT) IS_ISSQT, sum(IS_ISSVL) IS_ISSVL from MM_ISMST,CO_CTMST "+L_strFILTR+" and  CT_MATCD=IS_MATCD and IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4) +"' AND IS_DPTCD='"+L_strDPTCD+"' and IS_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and IS_ISSVL>0 group by IS_MATCD, CT_MATDS,CT_UOMCD order by IS_ISSVL desc");
				L_strCOLNM="ST_ABCFL";
			}
			else if(rdbXYZ.isSelected())
			{//For XYZ analysis		
				M_rstRSSET=cl_dat.exeSQLQRY0("Select STP_MATCD,STP_MATDS,STP_UOMCD, isnull(STP_YCSQT,0) ST_STKVL,isnull(STP_YCSVL,0) ST_STKQT from MM_STPRC, CO_CTMST "+L_strFILTR+" and CT_MATCD=STP_MATCD and isnull(CT_OWNBY,'')='"+L_strDPTCD+"' AND STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP ='"+M_strSBSCD.substring(2,4)+"' and STP_YCSVL>0 order by STP_YCSVL desc");//and IS_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and ST_STKVL>0 group by ST_MATCD, CT_MATDS,CT_UOMCD order by ST_STKVL desc");;
				L_strCOLNM="ST_XYZFL";
			}
			if(M_rstRSSET==null)
			{
				setMSG("No data Found ..",'E');
				return;
			}
			float L_fltTOTAL=0.0f;//Grand total
			float L_fltA_VAL=Float.parseFloat(nvlSTRVL(txtA_VAL.getText(),"0"))/100f;//A  index 
			float L_fltB_VAL=Float.parseFloat(nvlSTRVL(txtB_VAL.getText(),"0"))/100f;//B  index
			float L_fltC_VAL=Float.parseFloat(nvlSTRVL(txtC_VAL.getText(),"0"))/100f;//C  index
			double L_dblTOTVL=0;//Grand Total
			if(rdbXYZ.isSelected())
			{//Assign values of X,Y,Z to cariables for A,B,C if XYZ is selected
				L_fltA_VAL=Float.parseFloat(txtX_VAL.getText())/100f;
				L_fltB_VAL=Float.parseFloat(txtY_VAL.getText())/100f;
				L_fltC_VAL=Float.parseFloat(txtZ_VAL.getText())/100f;
			}
			vtrISKEY=new Vector<String[]>(50,10);
			vtrISSVL=new Vector<Float>(50,10);
			while(M_rstRSSET.next())
			{
				intRECCT = 1;
			//put issue value in a vector
				vtrISSVL.addElement(new Float(M_rstRSSET.getFloat(5)));//"IS_ISSVL")));
			//put all details in a vector
				vtrISKEY.addElement(new String []{M_rstRSSET.getString(1),//"IS_MATCD"),
												  M_rstRSSET.getString(2),//"CT_MATDS"),
												  M_rstRSSET.getString(3),//"CT_UOMCD"),
												  M_rstRSSET.getString(4),//"IS_ISSQT"),
												  M_rstRSSET.getString(5)});//"IS_ISSVL")});
			//Calculate grand total 
				L_fltTOTAL += M_rstRSSET.getFloat(5);//"IS_ISSVL");
			}
			if(vtrISSVL.size()==0)
			{
				setMSG("No data found ..",'E');
			}		
			M_rstRSSET.close();
			float L_fltTOT_A=0.0f;//Subtotal of A catagory
			float L_fltTOT_B=0.0f;//Subtotal of B catagory
			float L_fltTOT_C=0.0f;//Subtotal of C catagory
			String [] L_staTEMP=null;
			String L_strABCFL="",L_strFL_A="A",L_strFL_B="B",L_strFL_C="C";//Strings to store values of flags
			L_fltA_VAL=L_fltA_VAL*L_fltTOTAL;
			L_fltB_VAL=L_fltB_VAL*L_fltTOTAL;
			L_fltC_VAL=L_fltC_VAL*L_fltTOTAL;
			if(rdbXYZ.isSelected())
			{//Re-define values of tags if XYZ is selected
				L_strFL_A="X";
				L_strFL_B="Y";
				L_strFL_C="Z";
			}
			prnHEADER();
			boolean L_flgCAT_A=true,L_flgCAT_B=true,L_flgCAT_C=true;//Flag to indicate whether the catagory is exhausted
			for(int i=0;i<vtrISKEY.size();i++)
			{
				if(cl_dat.M_intLINNO_pbst > 50)
				{					
					dosREPORT.writeBytes("\n"+strDOTLN);
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				L_staTEMP=(String[])vtrISKEY.elementAt(i);
				L_dblTOTVL+=((Float)vtrISSVL.elementAt(i)).floatValue();
				if(L_fltTOT_A+((Float)vtrISSVL.elementAt(i)).floatValue()<L_fltA_VAL && L_flgCAT_A)
				{//Catagory A or X
					L_fltTOT_A+=((Float)vtrISSVL.elementAt(i)).floatValue();
					L_strABCFL=L_strFL_A;
				}
				else if(L_fltTOT_B+((Float)vtrISSVL.elementAt(i)).floatValue()+L_fltTOT_A<L_fltB_VAL+L_fltA_VAL && L_flgCAT_B )
				{//Catagory B or Y
					L_flgCAT_A=false;
					L_fltTOT_B+=((Float)vtrISSVL.elementAt(i)).floatValue();
					L_strABCFL=L_strFL_B;
				}
				else
				{//Catagory C or Z
					L_flgCAT_B=false;
					L_strABCFL=L_strFL_C;
					L_fltTOT_C+=((Float)vtrISSVL.elementAt(i)).floatValue();
				}
			//Write to file
				dosREPORT.writeBytes(padSTRING('R',L_strABCFL,5));
				dosREPORT.writeBytes(padSTRING('R',L_staTEMP[0],12));
				dosREPORT.writeBytes(padSTRING('R',L_staTEMP[1],70-12-5));
				dosREPORT.writeBytes(padSTRING('R',L_staTEMP[2],4));
				dosREPORT.writeBytes(padSTRING('L',L_staTEMP[3],15));
				dosREPORT.writeBytes(padSTRING('L',L_staTEMP[4],15)+"\n");
				cl_dat.M_intLINNO_pbst++;
			}
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			dosREPORT.writeBytes(strDOTLN+"\n");
	//DISPLAY GRAND TOTAL
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('L',"TOTAL VALUE : ",15)+setNumberFormat(L_dblTOTVL,2));
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strEJT);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	* Method to generate the header part of the report.
	*/	
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			if(rdbABC.isSelected())
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");				
				dosREPORT.writeBytes("\n"+padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-21)+"Date : "+cl_dat.M_strLOGDT_pbst+"\n");			
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes(padSTRING('R',"Department : "+strDPTNM,strDOTLN.length()-21)+"Page No : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");						
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes("ABC Analysis Report of "+cmbRPTTP.getSelectedItem()+" from "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem()+" ( A : "+txtA_VAL.getText()+"; B : "+txtB_VAL.getText()+"; C : "+txtC_VAL.getText()+" )"+"\n");			
				cl_dat.M_intLINNO_pbst++;
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</B>");				
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes(padSTRING('R',"Material Catagory, Code & Description",70));
				dosREPORT.writeBytes(padSTRING('R',"UOM",4));
				dosREPORT.writeBytes(padSTRING('L',"Qty. ISSUED",15));
				dosREPORT.writeBytes(padSTRING('L',"VAULE",15)+"\n");			
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes(strDOTLN+"\n");
			}
			else if(rdbXYZ.isSelected())
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<B>");				
				dosREPORT.writeBytes("\n"+padSTRING('R',"SUPREME PETROCHEM LTD.",60)+"Date : "+cl_dat.M_strLOGDT_pbst+"\n");			
				cl_dat.M_intLINNO_pbst++;				
				dosREPORT.writeBytes(padSTRING('R',"XYZ Analysis Report : [X : "+txtX_VAL.getText()+"; Y : "+txtY_VAL.getText()+"; Z : "+txtZ_VAL.getText()+"] of "+strDPTNM,76));
				dosREPORT.writeBytes(padSTRING('L',"Page No : "+Integer.toString(++cl_dat.M_PAGENO),20)+"\n");			
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes(padSTRING('R',"For "+cmbRPTTP.getSelectedItem()+" from "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),76)+"\n");			
				cl_dat.M_intLINNO_pbst++;
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes(padSTRING('R',"Material Catagory, Code & Description",70));
				dosREPORT.writeBytes(padSTRING('R',"UOM",4));
				dosREPORT.writeBytes(padSTRING('L',"STOCK Qty.",15));
				dosREPORT.writeBytes(padSTRING('L',"VAULE",15)+"\n");
				cl_dat.M_intLINNO_pbst++;
				dosREPORT.writeBytes(strDOTLN+"\n");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}	
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(input instanceof JTextField)
					if(((JTextField)input).getText().length()==0)
						return true;
				if(input == txtDPTCD)
				{
					ResultSet L_rstRSSET=cl_dat.exeSQLQRY0("Select * from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='COXXDPT' and CMT_CODCD='"+txtDPTCD.getText()+"'");
					if(L_rstRSSET !=null)
					{
						if(L_rstRSSET.next())
						{
							txtDPTNM.setText(L_rstRSSET.getString("CMT_CODDS"));
							return true;
						}
						L_rstRSSET.close();
					}
					setMSG("Invalid Department ..",'E');
					return false;
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"InputVerifier");				
				return false;
			}
			return true;
		}
	}
}
