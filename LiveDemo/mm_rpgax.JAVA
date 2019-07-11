/*
System Name		: Materials Management System
Program Name	: ABC / XYZ Analysis Reports
Author			: Mr S.R.Mehesare

Modified Date	: 09/11/2005
Documented Date	: 
Version			: v2.0.0
*/

import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.Color;import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import javax.swing.BorderFactory;import javax.swing.JLabel;import javax.swing.JPanel;
import javax.swing.JTextField;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import javax.swing.JComponent;import javax.swing.InputVerifier;import java.sql.CallableStatement;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.util.Vector;import java.sql.ResultSet;import java.awt.Font;import java.util.StringTokenizer;

/**<pre>
System Name : Material Management System.
 
Program Name : ABC / XYZ Analysis Reports

Purpose : A common report program for Stock and Issue Analysis viz. : 
        1) ABC analysis : Issue valuation as per indices given by user  
        2) XYZ analysis : Stock valuation as per indices given by user 
        3) HML analysis : Stock valuation as per item values entered by user

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                     #
MM_STMST       ST_MATCD, ST_STRTP                             #
MM_ISMST       IS_MATCD, IS_STRTP, IS_ISSNO                             #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name   Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
cmbRPTTP                                                 Report Type
chbRECAL                                                 To Specify Updatation of values
txtFMDAT     IS_ISSDT       MM_ISMST      Date           Issue Date
txtTODAT     IS_ISSDT       MM_ISMST      Date           Issue Date
txtA_VAL                                                 Value of A for ABC analysis
txtB_VAL                                                 Value of B for ABC analysis
txtC_VAL                                                 Value of C for ABC analysis
txtFMDATX    IS_ISSDT       MM_ISMST      Date           Issue Date
txtTODATX    IS_ISSDT       MM_ISMST      Date           Issue Date
txtX_VAL                                                 Value of X for ABC analysis
txtY_VAL                                                 Value of Y for ABC analysis
txtZ_VAL                                                 Value of Z for ABC analysis
txtH_VAL                                                 Value of H for ABC analysis
txtM_VAL                                                 Value of M for ABC analysis
--------------------------------------------------------------------------------------
Features : 
   Report Criterias Available :  
      ABC / XYZ Analisys :
         - For Raw Material in selected Store
         - For Packing Material in selected Store
         - For Spares in selected Store
      HML Analysis
         - For selected Store Type
         - All reports are to date reports
         - If Checkbox "Re-calculate" is checked, back-end is updated. Otherwise, only report is generated using existing tags.
     For ABC/XYZ analysis, data is taken into a vector and processed in a common loop.
     For HML analysis, tags are set depending on STP_YCLRT by calling procedure setHML().
     For HML analysis, materials having stock on hand zero are not displayed.    

<I>
<B>Query : </B>
 1)For ABC Analysis Report Data is taken from MM_ISMST & CO_CTMST for condiations :-
    1) IS_MATCD like Report type Code
    2) AND IS_MMSBS = Specified Sub System code
    3) AND CT_MATCD = IS_MATCD 
    4) AND IS_ISSDT between Specified Date Range.
    5) AND IS_ISSVL > 0 
   group by IS_MATCD, CT_MATDS,CT_UOMCD

 2)For XYZ Analysis Report Data is taken from MM_STMST,MM_STPRC & CO_CTMST for condiations :-
    1) ST_MATCD like Report Type Code.
    2) AND ST_MMSBS = Specified sub System Code
    3) AND STP_MMSBS = ST_MMSBS 
    4) AND ST_MATCD = STP_MATCD 
    5) AND STP_STRTP = ST_STRTP 
    6) AND  CT_MATCD = ST_MATCD 
   group by ST_MATCD,CT_MATDS,CT_UOMCD

 3)For HML Analysis Report Data is taken from MM_STMST & MM_STPRC for condiations :-
    1) ST_STRTP = Specified Store Type
    2) AND STP_MMSBS = ST_MMSBS 
    3) AND STP_STRTP = ST_STRTP 
    4) AND STP_MATCD = ST_MATCD 
    5) AND ifnull(ST_STKQT,0) > 0
    6) AND ST_HMLFL in ('H','M','L') as per specifications provided.

<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then current Date.
</I> */

class mm_rpgax extends cl_rbase
{	
										/**	JRedioButton to specify ABC analysis Report.*/
	private JRadioButton rdbABC;		/**	JRedioButton to specify XYZ analysis report.*/
	private JRadioButton rdbXYZ;		/**	JRedioButton to Specify HML analysis Report.*/
	private JRadioButton rdbHML;		/**	JComboBox to specify Report Type.*/
	private cl_Combo cmbRPTTP;			/**	JCheckBox to Recalculate and update back-end */
	private JCheckBox chbRECAL;			/** JLabel to display report type on the Screen.*/
	private JLabel lblRPTTP;			/**	JTextField to specify & display From Date in ABC analysis.*/	
	private JTextField txtFMDAT;		/**	JTextField to specify & display To Date in XYZ analysis.*/
	private JTextField txtTODAT;		/**	JTextField to specify & display Value of A index in ABC analysis.*/
	private JTextField txtA_VAL;		/**	JTextField to specify & display Value of B index in ABC analysis.*/
	private JTextField txtB_VAL;		/**	JTextField to specify & display Value of C index in ABC analysis.*/
	private JTextField txtC_VAL;		/**	JPanel for ABC analysis */
	private JPanel pnlABC;				/**	JTextField to specify & display From Date in XYZ analysis.*/	
	private JTextField txtGRPCD;		/**	JTextField to specify & display To Date in XYZ analysis.*/
	private JTextField txtFMDATX;		/**	JTextField to specify & display To Date in XYZ analysis.*/
	private JTextField txtTODATX;		/**	JTextField to specify & display Value of X index in XYZ analysis.*/
	private JTextField txtX_VAL;		/**	JTextField to specify & display Value of Y index in XYZ analysis.*/
	private JTextField txtY_VAL;		/**	JTextField to specify & display Value of Z index in XYZ analysis.*/
	private JTextField txtZ_VAL;		/**	JPanel for XYZ analysis.*/
	private JPanel pnlXYZ;				/**	JTextField to specify & display Value of H index in HML analysis.*/	
	private JTextField txtH_VAL;		/**	JTextField to specify & display Value of M index in HML analysis.*/
	private JTextField txtM_VAL;		/** JCheckBox to Notify the H Value for HML analysis Report.*/
	private JCheckBox chbH_VAL;			/** JCheckBox to Notify the M Value for HML analysis Report.*/
	private JCheckBox chbM_VAL;			/** JCheckBox to Notify the M Value for HML analysis Report.*/
	private JCheckBox chbL_VAL;			/** JPanel to display components for HML analysis Report.*/
	private JPanel pnlHML;
	private JLabel lblMATDS;									/**	Vector for details of records retrieved as : String[] of MATCD,MATDS,UOM,sum(ISSQT),sum(ISSVL) */	
	private Vector<String[]> vtrISKEY;			/**	Vector for Values of records retrieved as : sum(ISSVL) */
	private Vector<Float> vtrISSVL;			/** String variable for generated  report file name.*/		
	private String strFILNM;			/** Integer variable to count the number of records fetched to block the report if no data found. */
	private int intRECCT;				/** DataOutputStream Object to generate & hold the Stream of Report data.*/
	private DataOutputStream dosREPORT;	/** FileoutputStream Object to generate the report file from the stream of data.*/
	private FileOutputStream fosREPORT; /** String variable to append the Dotted line in the Report.*/
	private String strDOTLN = "------------------------------------------------------------------------------------------------------------";
	private INPVF oINPVF;
	
	mm_rpgax()
	{
		super(2);
		try
		{
			setMatrix(20,6);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			JPanel L_pnlANATP=new JPanel(null);
			add(rdbABC=new JRadioButton("ABC"),1,1,1,1,L_pnlANATP,'L');
			add(rdbXYZ=new JRadioButton("X-Y-Z"),1,2,1,1,L_pnlANATP,'L');
			add(rdbHML=new JRadioButton("H-M-L"),1,3,1,1,L_pnlANATP,'L');
			ButtonGroup L_btgTEMP=new ButtonGroup();
			L_btgTEMP.add(rdbABC);L_btgTEMP.add(rdbXYZ);L_btgTEMP.add(rdbHML);
			L_pnlANATP.setBorder(BorderFactory.createTitledBorder("Analysis Type"));
			add(L_pnlANATP,2,2,2,3.1,this,'L');
			add(lblRPTTP=new JLabel("Report Type"),4,2,1,1,this,'L');
			add(cmbRPTTP=new cl_Combo(),4,3,1,2,this,'L');
			cmbRPTTP.addItem("Select Report Type","");
			cmbRPTTP.addItem("Packing Material","69");
			cmbRPTTP.addItem("Raw Material","68");
			cmbRPTTP.addItem("Spares","");
			add(new JLabel("Group Code"),5,2,1,1,this,'L');
			add(txtGRPCD=new TxtLimit(2),5,3,1,1,this,'L');
			add(lblMATDS=new JLabel(""),5,4,1,2,this,'L');
			
			setMatrix(20,24);
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
			add(pnlABC,6,7,5,13,this,'L');
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
			add(pnlXYZ,6,7,4,13,this,'L');
			pnlXYZ.setVisible(false);
						
			pnlHML=new JPanel(null);
			add(new JLabel("High Value"),1,2,1,3,pnlHML,'L');
			add(txtH_VAL=new TxtNumLimit(12.2),1,5,1,4,pnlHML,'L');
			add(new JLabel("Medium Value"),2,2,1,3,pnlHML,'L');
			add(txtM_VAL=new TxtNumLimit(12.2),2,5,1,4,pnlHML,'L');
			add(new JLabel("Report Option"),4,1,1,3,pnlHML,'L');
			add(chbH_VAL=new JCheckBox("High "),4,4,1,3,pnlHML,'L');
			add(chbM_VAL=new JCheckBox("Medium "),4,7,1,3,pnlHML,'L');
			add(chbL_VAL=new JCheckBox("Low "),4,11,1,3,pnlHML,'L');
			add(pnlHML,6,7,5,13,this,'L');
			pnlHML.setVisible(false);
			oINPVF=new INPVF();

			txtGRPCD.setInputVerifier(oINPVF);
					
			add(chbRECAL=new JCheckBox("Re - Calculate"),11,8,1,5,this,'L');
			chbRECAL.setVisible(false);
			cmbRPTTP.addActionListener(this);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	public void actionPerformed(ActionEvent P_AE)
	{
		super.actionPerformed(P_AE);
		try
		{
			if(txtGRPCD.getText().length()==0)
				lblMATDS.setText("");
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
					pnlHML.setVisible(false);
					pnlABC.setVisible(true);
				}
				lblRPTTP.setVisible(true);
				cmbRPTTP.setVisible(true);
				cmbRPTTP.requestFocus();
			}
			else if(M_objSOURC == rdbXYZ)
			{
				if(rdbXYZ.isSelected())
				{
					pnlXYZ.setVisible(true);
					pnlABC.setVisible(false);
					pnlHML.setVisible(false);
				}
				lblRPTTP.setVisible(true);
				cmbRPTTP.setVisible(true);
				cmbRPTTP.requestFocus();
			}
			else if(M_objSOURC == rdbHML)
			{
				if(rdbHML.isSelected())
				{
					pnlHML.setVisible(true);
					pnlABC.setVisible(false);
					pnlXYZ.setVisible(false);
					txtH_VAL.requestFocus();
				}
				lblRPTTP.setVisible(false);
				cmbRPTTP.setVisible(false);
			}
			else if(M_objSOURC == chbRECAL)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC != cl_dat.M_btnSAVE_pbst)
				((JComponent)M_objSOURC).transferFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
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
							txtZ_VAL.setText(setNumberFormat(100.0f-Float.parseFloat(txtY_VAL.getText())-Float.parseFloat(txtX_VAL.getText()),0));
							txtZ_VAL.select(0,txtZ_VAL.getText().length());
						}
					}
					setMSG("Enter 'Z' Index ..",'N');
				}
				else if(M_objSOURC == txtFMDAT || M_objSOURC == txtFMDATX)
					setMSG("Enter From Date ..",'N');
				else if(M_objSOURC == txtTODAT || M_objSOURC == txtTODATX)
					setMSG("Enter To Date ..",'N');
				else if(M_objSOURC == cmbRPTTP)//9223488843 kumar alainz india
					setMSG("Select Report Type ..",'N');
				else if(M_objSOURC == chbRECAL)
					setMSG("Recalculates the Indeices and updates data ..",'N');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"FocusGained");
		}
	}	
	public void keyPressed(KeyEvent P_KE)
	{
		super.keyPressed(P_KE);
		if(P_KE.getKeyCode()==P_KE.VK_F1)
		{
			if(M_objSOURC == txtGRPCD)
			{
        		cl_dat.M_flgHELPFL_pbst = true;
        		M_strHLPFLD = "txtGRPCD";
        		String L_ARRHDR[] = {"Group Code","Material Code","Material Description"};
        		M_strSQLQRY = " select ct_grpcd,ct_matcd,ct_matds from co_ctmst";
				M_strSQLQRY += " where ct_matcd like '%0000000A' and ct_stsfl<>'X'";
				cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");				
			}	
		}
		
		if(P_KE.getKeyCode()==P_KE.VK_ENTER)
		{
			if(M_objSOURC == chbRECAL)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	
	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtGRPCD"))
			{	
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtGRPCD.setText(L_STRTKN.nextToken());
				L_STRTKN.nextToken();
				lblMATDS.setText(L_STRTKN.nextToken());
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}

	/**
	 * Method to validate the inputs given before execuation of the SQl Query.
	 */
	boolean vldDATA()
	{
		try
		{
			setMSG("",'N');
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;
			if(txtGRPCD.getText().length()==0)
			{
				setMSG("Please enter Group Code ..",'E');
				txtGRPCD.requestFocus();
				return false;
			}	
			if(rdbHML.isSelected())
			{//HML ANALYSIS
				if(!chbRECAL.isSelected())
				{
					txtH_VAL.setText("10000.00");
					txtM_VAL.setText("1000.00");
				}
				if(txtH_VAL.getText().length()==0)
				{
					setMSG("Please enter H Value ..",'E');
					txtH_VAL.requestFocus();
					return false;
				}
				if(txtM_VAL.getText().length()==0)
				{
					setMSG("Please enter M Value ..",'E');
					txtM_VAL.requestFocus();
					return false;
				}
				if(Float.parseFloat(txtM_VAL.getText())>Float.parseFloat(txtH_VAL.getText()))
				{
					setMSG("M value cannot be greater than H value ..",'E');
					txtM_VAL.requestFocus();
					return false;
				}
				if(!(chbH_VAL.isSelected() || chbM_VAL.isSelected() || chbL_VAL.isSelected()))
			    {
					setMSG("Please select values to display ..",'E');
					chbH_VAL.requestFocus();
					return false;
				}
			}
			else if(rdbABC.isSelected())
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
			if(cmbRPTTP.isVisible() && cmbRPTTP.getSelectedIndex()==0)
			{
				setMSG("Please select Report Type ..",'E');
				cmbRPTTP.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) && M_cmbDESTN.getSelectedIndex()==0)
			{
				setMSG("Please select Printer ..",'E');
				M_cmbDESTN.requestFocus();
				return false;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst) && M_cmbDESTN.getItemCount()==0)
			{
				setMSG("Please enter E-mail Receipents ..",'E');
				M_cmbDESTN.requestFocus();
				return false;
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
	 * Method to generate the Report & to forward it to specified destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpgax.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpgax.doc";
			
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"ABC / XYZ Analysis Reports"," ");
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
	 * Method to fetch data from the database & to club it with header & footer of the Report.
	 */
	void getDATA()
	{
		try
		{
			intRECCT = 0; 
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			int L_intH_CNT =0;
			int L_intM_CNT =0;
			int L_intL_CNT =0;
			
			float L_fltH_TOT =0;
			float L_fltM_TOT =0;
			float L_fltL_TOT =0;
			
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>ABC / XYZ Analysis Reports</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			if(rdbHML.isSelected())
			{
				if(chbRECAL.isSelected())
				{
					CallableStatement L_cstTEMP=cl_dat.M_conSPDBA_pbst.prepareCall("{call procedure setHML(?,?,?,?)}");
					L_cstTEMP.setString(1,cl_dat.M_strCMPCD_pbst);
					L_cstTEMP.setString(2,M_strSBSCD.substring(2,4));
					L_cstTEMP.setFloat(3,Float.parseFloat(txtH_VAL.getText()));
					L_cstTEMP.setFloat(4,Float.parseFloat(txtM_VAL.getText()));
					L_cstTEMP.executeUpdate();
				}
				String L_strFILTR="";
				if(!(chbH_VAL.isSelected() && chbM_VAL.isSelected() && chbL_VAL.isSelected()))
				{
					if(chbH_VAL.isSelected()) 
						L_strFILTR="'H'";
					if(chbM_VAL.isSelected()) 
					{
						if(L_strFILTR.length()>1)
							L_strFILTR+=",'M'";
						else
							L_strFILTR="'M'";
					}
					if(chbL_VAL.isSelected()) 
					{
						if(L_strFILTR.length()>1)
							L_strFILTR+=",'L'";
						else
							L_strFILTR="'L'";
					}
					if(L_strFILTR.length()>4)
						L_strFILTR=" and ST_HMLFL in ("+L_strFILTR+")";
					else if(L_strFILTR.length()>1)
						L_strFILTR=" and ST_HMLFL = "+L_strFILTR+" ";
				}
				prnHEADER();
				int L_intCOUNT=0;
				M_strSQLQRY="Select ST_MATCD,ST_MATDS,ST_UOMCD,STP_YCLRT,ST_HMLFL,ST_STKQT FROM MM_STMST,MM_STPRC where ST_STRTP='"+M_strSBSCD.substring(2,4)+"' and  STP_CMPCD = ST_CMPCD and STP_MMSBS=ST_MMSBS and STP_STRTP=ST_STRTP and STP_MATCD=ST_MATCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ST_MATCD,1,2)='"+txtGRPCD.getText().trim()+"' AND isnull(ST_STKQT,0)>0 "+L_strFILTR;
				M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
				System.out.println("M_strSQLQRY1>>>>"+M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{	
						intRECCT = 1;
						if(cl_dat.M_intLINNO_pbst>65)
						{
							dosREPORT.writeBytes("\n"+strDOTLN);					
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER();
						}
						dosREPORT.writeBytes(padSTRING('R',Integer.toString(++L_intCOUNT),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),55));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_YCLRT"),""),15));
						dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),""),4));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),""),15)+"\n");					
						if(nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),"").equals("H"))
						{
						    L_intH_CNT++; 
						    L_fltH_TOT = L_fltH_TOT + M_rstRSSET.getFloat("STP_YCLRT") * M_rstRSSET.getFloat("ST_STKQT");  
						}
						else if(nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),"").equals("M"))
						{
						    L_intM_CNT++; 
						    L_fltM_TOT = L_fltM_TOT + M_rstRSSET.getFloat("STP_YCLRT") * M_rstRSSET.getFloat("ST_STKQT");    
						}
						else if(nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),"").equals("L"))
						{
						    L_intL_CNT++;
						    L_fltL_TOT = L_fltL_TOT + M_rstRSSET.getFloat("STP_YCLRT") * M_rstRSSET.getFloat("ST_STKQT");  
						}
						cl_dat.M_intLINNO_pbst++;
					}
					M_rstRSSET.close();
				}
				dosREPORT.writeBytes(strDOTLN+"\n");
    			dosREPORT.writeBytes("     Count        Value  "+"\n");
    			dosREPORT.writeBytes(" H : "+padSTRING('L',String.valueOf(L_intH_CNT),5) +padSTRING('L',setNumberFormat(L_fltH_TOT,2),13)+"\n");
    			dosREPORT.writeBytes(" M : "+padSTRING('L',String.valueOf(L_intM_CNT),5) +padSTRING('L',setNumberFormat(L_fltM_TOT,2),13)+"\n");
    			dosREPORT.writeBytes(" L : "+padSTRING('L',String.valueOf(L_intL_CNT),5) +padSTRING('L',setNumberFormat(L_fltL_TOT,2),13)+"\n");
			
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
				return;
			}
			String L_strCOLNM="";//Name of the column to be updated in back end
			String L_strFILTR="where ";//Report type filter
			if(cmbRPTTP.getSelectedIndex() < 3)//Raw material or pkg. material selected
				L_strFILTR+=(rdbABC.isSelected() ? " IS_MATCD " : " ST_MATCD ")+"like '"+cmbRPTTP.getITMCD()+"%' ";
			else//Spares selected
				L_strFILTR+= " substring("+(rdbABC.isSelected() ? " IS_MATCD " : " ST_MATCD ")+",1,2) not in ('"+cmbRPTTP.getITMCDAt(1)+"','"+cmbRPTTP.getITMCDAt(2)+"') ";
			if(rdbABC.isSelected())
			{//For ABC analysis
				M_strSQLQRY="Select IS_MATCD,CT_MATDS,CT_UOMCD,sum(IS_ISSQT) IS_ISSQT, sum(IS_ISSVL) IS_ISSVL from MM_ISMST,CO_CTMST "+L_strFILTR+"  AND SUBSTRING(IS_MATCD,1,2)='"+txtGRPCD.getText().trim()+"' and  IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_MMSBS='"+M_strSBSCD+"' and  CT_MATCD=IS_MATCD and IS_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and IS_ISSVL>0 group by IS_MATCD, CT_MATDS,CT_UOMCD order by IS_ISSVL desc";
				M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
				System.out.println("M_strSQLQRY2>>>>"+M_strSQLQRY);
				L_strCOLNM="ST_ABCFL";
			}
			else if(rdbXYZ.isSelected())
			{//For XYZ analysis		
				M_strSQLQRY="Select ST_MATCD,CT_MATDS,CT_UOMCD, isnull(sum(ST_STKQT),0) ST_STKQT,isnull(sum(ST_STKqt*STP_YCLRT),0) ST_STKVL from MM_STMST,MM_STPRC,CO_CTMST "+L_strFILTR+"  AND SUBSTRING(ST_MATCD,1,2)='"+txtGRPCD.getText().trim()+"' AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MMSBS='"+M_strSBSCD+"' and STP_CMPCD = ST_CMPCD and STP_MMSBS=ST_MMSBS AND ST_MATCD=STP_MATCD AND STP_STRTP=ST_STRTP and  CT_MATCD=ST_MATCD group by ST_MATCD,CT_MATDS,CT_UOMCD order by ST_STKVL desc";//and IS_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' and ST_STKVL>0 group by ST_MATCD, CT_MATDS,CT_UOMCD order by ST_STKVL desc";
				M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
				System.out.println("M_strSQLQRY3>>>>"+M_strSQLQRY);
				L_strCOLNM="ST_XYZFL";
			}
			if(M_rstRSSET==null)
			{
				setMSG("No data Found ..",'E');
				return;
			}
			float L_fltTOTAL=0.0f;//Grand total
			float L_fltA_VAL=0;
			float L_fltB_VAL=0;
			float L_fltC_VAL=0;

            float L_fltA_TOT=0;
			float L_fltB_TOT=0;
			float L_fltC_TOT=0;

            int L_intA_CNT=0;
			int L_intB_CNT=0;
			int L_intC_CNT=0;

			if(rdbXYZ.isSelected())
			{//Assign values of X,Y,Z to cariables for A,B,C if XYZ is selected
				L_fltA_VAL=Float.parseFloat(txtX_VAL.getText())/100f;
				L_fltB_VAL=Float.parseFloat(txtY_VAL.getText())/100f;
				L_fltC_VAL=Float.parseFloat(txtZ_VAL.getText())/100f;
			}
			else
			{
				L_fltA_VAL=Float.parseFloat(txtA_VAL.getText())/100f;//A  index 
				L_fltB_VAL=Float.parseFloat(txtB_VAL.getText())/100f;//B  index
				L_fltC_VAL=Float.parseFloat(txtC_VAL.getText())/100f;//C  index
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
				if(cl_dat.M_intLINNO_pbst>65)
				{
					dosREPORT.writeBytes("\n"+strDOTLN);					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				L_staTEMP=(String[])vtrISKEY.elementAt(i);
				if(L_fltTOT_A+((Float)vtrISSVL.elementAt(i)).floatValue()<L_fltA_VAL && L_flgCAT_A)
				{//Catagory A or X
					L_fltTOT_A+=((Float)vtrISSVL.elementAt(i)).floatValue();
					L_strABCFL=L_strFL_A;
					L_intA_CNT++;
					L_fltA_TOT +=((Float)vtrISSVL.elementAt(i)).floatValue();
				}
				else if(L_fltTOT_B+((Float)vtrISSVL.elementAt(i)).floatValue()+L_fltTOT_A<L_fltB_VAL+L_fltA_VAL && L_flgCAT_B )
				{//Catagory B or Y
					L_flgCAT_A=false;
					L_fltTOT_B+=((Float)vtrISSVL.elementAt(i)).floatValue();
					L_strABCFL=L_strFL_B;
					L_intB_CNT++;
					L_fltB_TOT +=((Float)vtrISSVL.elementAt(i)).floatValue();
				}
				else
				{//Catagory C or Z
					L_flgCAT_B=false;
					L_strABCFL=L_strFL_C;
					L_fltTOT_C+=((Float)vtrISSVL.elementAt(i)).floatValue();
					L_intC_CNT++;
					L_fltC_TOT +=((Float)vtrISSVL.elementAt(i)).floatValue();
				}
			//Write to file
				dosREPORT.writeBytes(padSTRING('R',L_strABCFL,5));
				dosREPORT.writeBytes(padSTRING('R',L_staTEMP[0],12));
				dosREPORT.writeBytes(padSTRING('R',L_staTEMP[1],70-12-5));
				dosREPORT.writeBytes(padSTRING('R',L_staTEMP[2],4));
				dosREPORT.writeBytes(padSTRING('L',L_staTEMP[3],15));
				dosREPORT.writeBytes(padSTRING('L',L_staTEMP[4],15)+"\n");				
				cl_dat.M_intLINNO_pbst++;
				if(chbRECAL.isSelected())
				{//If recalculate selected, update back end
					M_strSQLQRY="Update MM_STMST set "+L_strCOLNM+" = '"+L_strABCFL+"' where  ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MMSBS='"+M_strSBSCD+"' and ST_MATCD='"+L_staTEMP[0]+"' and ST_STRTP='"+M_strSBSCD.substring(2,4)+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
				if(!cl_dat.M_flgLCUPD_pbst)
				{
					setMSG("Data updation failed ..",'E');
					break;
				}
			}
			if(!cl_dat.exeDBCMT("exePRINT"))
			{
				setMSG("Data updation failed ..",'E');
				return;
			}
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			dosREPORT.writeBytes(strDOTLN+"\n");
			dosREPORT.writeBytes("     Count        Value  "+"\n");
			dosREPORT.writeBytes(" "+L_strFL_A+" : "+padSTRING('L',String.valueOf(L_intA_CNT),5) +padSTRING('L',setNumberFormat(L_fltA_TOT,2),13)+"\n");
			dosREPORT.writeBytes(" "+L_strFL_B+" : "+padSTRING('L',String.valueOf(L_intB_CNT),5) +padSTRING('L',setNumberFormat(L_fltB_TOT,2),13)+"\n");
			dosREPORT.writeBytes(" "+L_strFL_C+" : "+padSTRING('L',String.valueOf(L_intC_CNT),5) +padSTRING('L',setNumberFormat(L_fltC_TOT,2),13)+"\n");
			
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
	 * Method to generate the Header part of the report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			if(rdbHML.isSelected())
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())			
					dosREPORT.writeBytes("<B>");				
				dosREPORT.writeBytes("\nSUPREME PETROCHEM LTD."+"\n");							
				dosREPORT.writeBytes(padSTRING('R',"HML Analysis Report : [H > "+txtH_VAL.getText()+"; M > "+txtM_VAL.getText()+"; L < "+txtM_VAL.getText()+" ]",strDOTLN.length()-22)+"Date    : "+cl_dat.M_strLOGDT_pbst+"\n");				
				dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),strDOTLN.length()-22) + "Page No : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");				
				dosREPORT.writeBytes(strDOTLN+"\n");				
				dosREPORT.writeBytes(padSTRING('R',"S.N.",4));
				dosREPORT.writeBytes(padSTRING('R',"Mat. Code",12));
				dosREPORT.writeBytes(padSTRING('R',"Description",55));
				dosREPORT.writeBytes(padSTRING('R',"UOM",3));
				dosREPORT.writeBytes(padSTRING('L',"Mat. Rate",15));
				dosREPORT.writeBytes(padSTRING('C',"Flag",4));
				dosREPORT.writeBytes(padSTRING('L',"Stock",15)+"\n");				
				dosREPORT.writeBytes(strDOTLN+"\n");				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())			
					dosREPORT.writeBytes("</B>");
				cl_dat.M_intLINNO_pbst = 7;
			}			
			else if(rdbABC.isSelected())
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())			
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes("\n"+padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-22) + "Date    : "+cl_dat.M_strLOGDT_pbst +"\n");				
				dosREPORT.writeBytes(padSTRING('R',"ABC Analysis Report : [A : "+txtA_VAL.getText()+"; B : "+txtB_VAL.getText()+"; C : "+txtC_VAL.getText()+"]",strDOTLN.length()-22));
				dosREPORT.writeBytes("Page No : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");							
				dosREPORT.writeBytes(padSTRING('R',"Of "+cmbRPTTP.getSelectedItem()+" from "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),80)+"\n");				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())			
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes(strDOTLN+"\n");				
				dosREPORT.writeBytes(padSTRING('R',"Material Catagory, Code & Description",70));
				dosREPORT.writeBytes(padSTRING('R',"UOM",4));
				dosREPORT.writeBytes(padSTRING('L',"ISSUED Qty.",15));
				dosREPORT.writeBytes(padSTRING('L',"VAULE",15)+"\n");							
				dosREPORT.writeBytes(strDOTLN+"\n");
				cl_dat.M_intLINNO_pbst = 6;
			}
			else if(rdbXYZ.isSelected())
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())			
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes("\n"+padSTRING('R',"SUPREME PETROCHEM LTD.",strDOTLN.length()-22) + "Date    : "+cl_dat.M_strLOGDT_pbst+"\n");				
				dosREPORT.writeBytes(padSTRING('R',"XYZ Analysis Report : [X : "+txtX_VAL.getText()+"; Y : "+txtY_VAL.getText()+"; Z : "+txtZ_VAL.getText()+"]",strDOTLN.length()-22));
				dosREPORT.writeBytes("Page No : "+Integer.toString(++cl_dat.M_PAGENO)+"\n");							
				dosREPORT.writeBytes(padSTRING('R',"Of "+cmbRPTTP.getSelectedItem()+" from "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem(),76)+"\n");				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())			
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes(strDOTLN+"\n");				
				dosREPORT.writeBytes(padSTRING('R',"Material Catagory, Code & Description",70));
				dosREPORT.writeBytes(padSTRING('R',"UOM",4));
				dosREPORT.writeBytes(padSTRING('L',"STOCK Qty.",15));
				dosREPORT.writeBytes(padSTRING('L',"VAULE",15)+"\n");							
				dosREPORT.writeBytes(strDOTLN+"\n");
				cl_dat.M_intLINNO_pbst = 6;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}			
	}	
	

	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;
				if(input == txtGRPCD)
				{
					M_strSQLQRY = " select ct_matds from co_ctmst";
					M_strSQLQRY += " where ct_grpcd = '"+txtGRPCD.getText().trim()+"' and ct_matcd like '%0000000A' and ct_stsfl <> 'X'";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						lblMATDS.setText(M_rstRSSET.getString("ct_matds"));
						setMSG("",'N');
					}	
					else
					{
						setMSG("Enter Valid Material Code",'E');
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

}