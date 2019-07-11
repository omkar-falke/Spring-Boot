/*
System Name   : Material Management System
Program Name  : List Of Non Moved Items 
Program Desc. : 
Author        : Mr.S.R.mehesare
Date          : 18/10/2005
Version       : MMS v2.0.0
*/

import java.sql.Date;import java.sql.ResultSet;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComboBox;import javax.swing.InputVerifier;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;import javax.swing.JComponent;
/**<pre>
System Name : Material Management System.
 
Program Name : Non Moved Items

Purpose : Generates a list of Non Moved items for the selected Criteria.
(Obsolete / Essential / Surplus / All MArked / All Non Marked)

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
MM_STPRC       STP_MMSBS,STP_STRTP,STP_MATCD                           #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMGRP    ST_MATCD                MM_STMST           VARCHAR(10)   Material Code
txtTOGRP    ST_MATCD                MM_STMST           VARCHAR(10)   Material Code
txtNOMON    ST_PISDT                MM_STMST           Date          Purchase Date
txtLOCCD    ST_LOCCD                MM_STMST           VARCHAR(5)    Location Code
txtMATVL    ST_STKQT * STP_YCLRT    MM_STMST, MM_STPRC               Total Value
cmbMATCT                                                             Material Category
cmbREPOR                                                             Report Order
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
For Obsolete Material Category, Data is taken from  MM_STMST and MM_STPRC  for condiations :-
    1) ST_STRTP = STP_STRTP 
    2) AND ST_MATCD = STP_MATCD 
    3) AND (days(CURRENT_DATE) - days(ST_PISDT)) >= Given Nuber of Days 
    4) AND SUBSTRING(ST_MATCD,1,2) between given Range of Material Group Code
    5) AND isnull(ST_STSFL,'') = '9' 
    6) AND isnull(ST_STKQT,0) > 0 
    7) AND ST_STRTP = Specified Store Type.
    8) AND isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= given Material value
   If Location Code is Given 				
    9) AND ST_LOCCD = Given Location Code

For Essential Material Category, Data is taken from  MM_STMST and MM_STPRC  for condiations :-
   1) ST_STRTP = STP_STRTP 
   2) AND ST_MATCD = STP_MATCD 
   3) AND (days(CURRENT_DATE) - days(ST_PISDT)) >= "+L_intDAY+" 
   4) AND SUBSTRING(ST_MATCD,1,2) between given Range of Material Group Code
   5) AND isnull(ST_STKQT,0) > 0 
   6) AND isnull(ST_STSFL,'') <> 'X' "
   7) AND ST_STRTP = Specified Store Type
   8) AND isnull(ST_VEDFL,'') = 'E' 
   9) AND ST_STKQT * STP_YCLRT >= Specified Value
  If Location Code is Given 				
  10) AND ST_LOCCD = Given Location Code

For Surplus Material Category, Data is taken from  MM_STMST and MM_STPRC  for condiations :-
   1) ST_STRTP = STP_STRTP 
   2) AND ST_MATCD = STP_MATCD 
   3) AND ST_STRTP = Specified Store Type
   4) AND (days(CURRENT_DATE) - days(ST_PISDT)) >= Specified Datys
   5) AND SUBSTRING(ST_MATCD,1,2) between given Range of Material Group Code
   6) AND isnull(ST_STKQT,0) > 0 
   7) AND isnull(ST_SRPQT,0) > 0 
   8) AND isnull(ST_STSFL,'') <> 'X' 
   9) AND ST_STKQT * STP_YCLRT >= Specified Value
 If Location Code is Given 				
  10) AND ST_LOCCD = Given Location Code

For All(Marked) Category, Data is taken from  MM_STMST and MM_STPRC  for condiations :-
   1) ST_STRTP = STP_STRTP 
   2) AND ST_MATCD = STP_MATCD 
   3) AND (days(CURRENT_DATE) - days(ST_PISDT)) >= Specified Number of Dats
   4) AND SUBSTRING(ST_MATCD,1,2)  between given Range of Material Group Code
   5) AND isnull(ST_STSFL,'') <> 'X' 
   6) AND ST_STRTP = Specified Store Type
   7) AND isnull(ST_STKQT,0) > 0 
   8) AND ((isnull(ST_STSFL,' ') = '9' OR isnull(ST_VEDFL,' ') = 'E' OR isnull(ST_SRPQT,0) > 0)) 
   9) AND isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= Specified Material Value
 If Location Code is Given 				
  10) AND ST_LOCCD = Given Location Code

For All(NonMarked) Category, Data is taken from  MM_STMST and MM_STPRC  for condiations :-
   1) ST_STRTP = STP_STRTP 
   2) AND ST_MATCD = STP_MATCD 
   3) AND (days(CURRENT_DATE) - days(ST_PISDT)) >= Specified Number of Days
   4) AND SUBSTRING(ST_MATCD,1,2) between given Range of Material Group Code
   5) AND ST_STRTP = Specified Store Type 
   6) AND isnull(ST_STKQT,0) > 0 
   7) AND isnull(ST_STSFL,' ') NOT IN ('X','9') 
   8) AND isnull(ST_VEDFL,' ') = 'D' 
   9) AND isnull(ST_SRPQT,0) = 0 
  10) AND isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= Specified Material Value
 If Location Code is Given 				
  11) AND ST_LOCCD = Given Location Code

<B>Validations & Other Information:</B>    
    - From Material Group & To Material Group must be valid.
</I> */

class mm_rpnmv extends cl_rbase
{									/** JTextField to display & to enter Group Code.*/
	private JTextField txtFMGRP;	/** JTextfield to display & to enter To Group code.*/
	private JTextField txtTOGRP;	/** JTextField to display & to enter Number of month.*/
	private JTextField txtNOMON;	/** JComboBox to display to specify Material Type.*/
	private JTextField txtLOCCD;	/** JTextField to display to specify Location Code.*/
	private JComboBox cmbMATCT;		/** JTextField to display & to specify Material value.*/
	private JTextField txtMATVL;	/** JComboBox to specify Report Order.*/
	private JComboBox cmbREPOR;		/** String variable for number of month.*/
	private String strNOMON;		/** String variable for rate of material.*/
	private String strRATE;			/** Integer Variabel for item count.*/
	private int intITEM;			/** Double variabel for Value Of Items.*/
	private double dblVALUE;		/** Double variabel for Surplus Value.*/
	private double dblSRPLS;		/** FileOutputStream Object to hold Report Data in the Stream of data.*/
	private FileOutputStream fosREPORT ;   /** DataOutputStream Object to generate the T=Report File From the stream of data.*/
    private DataOutputStream dosREPORT ;	/** String variable for generated Report File Name.*/
	private String strFILNM;				/** Integer variable to count the number of records fetched to block the report if no data found.*/
	private int intRECCT;
	private String strORDER =" ORDER BY ST_PISDT";
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	
	mm_rpnmv()
	{
		super(2);	
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);				
			add(new JLabel("From Group"),4,4,1,1.3,this,'R');
			add(txtFMGRP = new TxtNumLimit(2.0),4,5,1,1.3,this,'L');
			add(new JLabel("To Group"),5,4,1,1.3,this,'R');
			add(txtTOGRP = new TxtNumLimit(2.0),5,5,1,1.3,this,'L');
			add(new JLabel("No. Of. Month"),6,4,1,1.3,this,'R');
			add(txtNOMON = new TxtNumLimit(10.0),6,5,1,1.3,this,'L');
			add(new JLabel("Material Category"),7,4,1,1.3,this,'R');
			add(cmbMATCT = new JComboBox(),7,5,1,1.3,this,'L');
			add(new JLabel("Value Of Material"),8,4,1,1.3,this,'R');
			add(txtMATVL = new TxtNumLimit(10.0),8,5,1,1.3,this,'L');
			add(new JLabel("Report Order"),9,4,1,1.3,this,'R');
			add(cmbREPOR = new JComboBox(),9,5,1,1.3,this,'L');
			add(new JLabel("Location like"),10,4,1,1.3,this,'R');
			add(txtLOCCD = new TxtLimit(5),10,5,1,1.3,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
			cmbMATCT.addItem("Obsolete");
			cmbMATCT.addItem("Essential");
			cmbMATCT.addItem("Vital");
			cmbMATCT.addItem("Surplus");
			cmbMATCT.addItem("All(Marked)");
			cmbMATCT.addItem("All(NonMarked)");
			
			cmbREPOR.addItem("Month Wise");
			cmbREPOR.addItem("Value Wise");
			cmbREPOR.addItem("Material Wise");
			cmbREPOR.addItem("Location Wise");			
		
			txtFMGRP.setInputVerifier(new INPVF());
			txtTOGRP.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{				
				setENBL(true);
				setMSG("Enter Group Code Or Press 'F1' For Help ",'N');
				txtFMGRP.requestFocus();
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			intITEM = 0;
			dblVALUE = 0.0;
		}
				
		if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
			{
				txtFMGRP.requestFocus();
				setMSG("",'N');
			}
		}
		if(M_objSOURC == cmbREPOR)
		{			
			if(cmbREPOR.getSelectedItem().toString().equals("Month Wise"))
				strORDER = " ORDER BY ST_PISDT";
			if(cmbREPOR.getSelectedItem().toString().equals("Value Wise"))
				strORDER = " ORDER BY ST_PPOVL";
			if(cmbREPOR.getSelectedItem().toString().equals("Material Wise"))
				strORDER = " ORDER BY ST_MATCD";
			if(cmbREPOR.getSelectedItem().toString().equals("Location Wise"))
				strORDER = " ORDER BY ST_LOCCD";
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);	
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtFMGRP)
			{
				setMSG("",'N');
				M_strHLPFLD = "txtFMGRP";
				M_strSQLQRY = "SELECT DISTINCT SUBSTRING(ST_MATCD,1,2),CT_MATDS  FROM MM_STMST,CO_CTMST WHERE "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ST_MATCD,1,2) = SUBSTRING(CT_MATCD,1,2) AND CT_CODTP = 'MG' AND "
					+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtFMGRP.getText().trim().length() > 0)							 
					M_strSQLQRY += "AND SUBSTRING(ST_MATCD,1,2) LIKE '"+txtFMGRP.getText().trim()+"%' ";
				M_strSQLQRY += "ORDER BY SUBSTRING(ST_MATCD,1,2) ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Group Name"},2,"CT");
			}
			if(M_objSOURC == txtTOGRP)
			{
				setMSG("",'N');
				M_strHLPFLD = "txtTOGRP";
				M_strSQLQRY = "SELECT DISTINCT SUBSTRING(ST_MATCD,1,2),CT_MATDS  FROM MM_STMST,CO_CTMST WHERE "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ST_MATCD,1,2) = SUBSTRING(CT_MATCD,1,2) AND CT_CODTP = 'MG' AND "
					+"ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtTOGRP.getText().trim().length() > 0)							 
					M_strSQLQRY += "AND SUBSTRING(ST_MATCD,1,2) LIKE '"+txtTOGRP.getText().trim()+"%' ";
				M_strSQLQRY += "ORDER BY SUBSTRING(ST_MATCD,1,2) ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Group Name"},2,"CT");
			}
			if(M_objSOURC == txtLOCCD)
			{
				setMSG("",'N');
				M_strHLPFLD = "txtLOCCD";
				M_strSQLQRY = "SELECT DISTINCT ST_LOCCD  FROM MM_STMST WHERE "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
				if(txtLOCCD.getText().trim().length() > 0)							 
					M_strSQLQRY += "AND ST_LOCCD LIKE '"+txtLOCCD.getText().trim()+"%' ";
				M_strSQLQRY += "ORDER BY ST_LOCCD ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Location Code"},1,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbREPOR)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == txtFMGRP)
				setMSG("Enter From Group Code, Press 'F1' For Help .. ",'N');
			if(M_objSOURC == txtTOGRP)
				setMSG("Enter To Group Code, Press 'F1' For Help ..",'N');
			if(M_objSOURC == txtNOMON)
				setMSG("Enter Number Of Month..",'N');
			if(M_objSOURC == cmbMATCT)
				setMSG("Select Material Category ..",'N');
			if(M_objSOURC == txtMATVL)
				setMSG("Enter Material Value ..",'N');
			if(M_objSOURC == cmbREPOR)
				setMSG("Select Report Order ..",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"focusGained");
		}
	}
	/**
	 * Super class method overrided to execuate the F1 Help for selected Field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMGRP")
		{
			txtFMGRP.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOGRP.requestFocus();
		}
		if(M_strHLPFLD == "txtTOGRP")
		{
			txtTOGRP.setText(cl_dat.M_strHLPSTR_pbst);
			txtNOMON.requestFocus();
		}
		if(M_strHLPFLD == "txtLOCCD")
		{
			txtLOCCD.setText(cl_dat.M_strHLPSTR_pbst);
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
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpnmv.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpnmv.doc";
			
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"List Of Non Moved Items"," ");
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
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA()
	{
		try
		{
			if(!vldDATA())
				return;
			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>List Of Non Moved Items</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			intRECCT = 0;
			intITEM = 0;
			dblVALUE = 0.0;
			dblSRPLS = 0.0;
			//Conver number of month in to number of days
			int L_intDAY = Integer.parseInt(txtNOMON.getText().toString());
			L_intDAY *= 30;
			prnHEADER();
			if(cmbMATCT.getSelectedItem().toString().equals("Obsolete"))	//Obsolete
			{
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_PISDT,ST_PGRDT,ST_PMRDT,ST_PSNDT,"
					+"ST_STKQT,ST_PPOVL,ST_WAVRT,(day(getdate()) - day(ST_PISDT)) L_MON,ST_LOCCD,STP_YCLRT "
					+"FROM MM_STMST,MM_STPRC WHERE ST_CMPCD=STP_CMPCD and ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND "
					+"(day(getdate()) - day(ST_PISDT)) >= "+L_intDAY+" AND "
					+"SUBSTRING(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText().trim()+"' AND '"+txtTOGRP.getText().trim()+"' AND "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STSFL,'') = '9' AND isnull(ST_STKQT,0) > 0 AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= "+txtMATVL.getText() ;
				if(txtLOCCD.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtLOCCD.getText().trim()+"%'";	
				M_strSQLQRY += strORDER;				
			}
			else if(cmbMATCT.getSelectedItem().toString().equals("Essential"))	//essential
			{
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_PISDT,ST_PGRDT,ST_PMRDT,ST_PSNDT,"
					+"ST_STKQT,ST_PPOVL,ST_WAVRT,(day(getdate()) - day(ST_PISDT)) L_MON,ST_LOCCD,STP_YCLRT "
					+"FROM MM_STMST,MM_STPRC WHERE ST_CMPCD = STP_CMPCD and ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND "
					+"(day(getdate()) - day(ST_PISDT)) >= "+L_intDAY+" AND SUBSTRING(ST_MATCD,1,2) "
					+"BETWEEN '"+txtFMGRP.getText().trim()+"' AND '"+txtTOGRP.getText().trim()+"' AND "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_STKQT,0) > 0 AND isnull(ST_STSFL,'') <> 'X' "
					+"AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_VEDFL,'') = 'E' AND "
					+"isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= "+txtMATVL.getText();
				if(txtLOCCD.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtLOCCD.getText().trim()+"%'";	
				M_strSQLQRY += strORDER;					
			}
			else if(cmbMATCT.getSelectedItem().toString().equals("Vital"))	//essential
			{
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_PISDT,ST_PGRDT,ST_PMRDT,ST_PSNDT,"
					+"ST_STKQT,ST_PPOVL,ST_WAVRT,(day(getdate()) - day(ST_PISDT)) L_MON,ST_LOCCD,STP_YCLRT "
					+"FROM MM_STMST,MM_STPRC WHERE ST_CMPCD=STP_CMPCD and ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND "
					+"(day(getdate()) - day(ST_PISDT)) >= "+L_intDAY+" AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ST_MATCD,1,2) "
					+"BETWEEN '"+txtFMGRP.getText().trim()+"' AND '"+txtTOGRP.getText().trim()+"' AND "
					+"isnull(ST_STKQT,0) > 0 AND isnull(ST_STSFL,'') <> 'X' "
					+"AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_VEDFL,'') = 'V' AND "
					+"isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= "+txtMATVL.getText();
				if(txtLOCCD.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtLOCCD.getText().trim()+"%'";	
				M_strSQLQRY += strORDER;					
			}
			else if(cmbMATCT.getSelectedItem().toString().equals("Surplus"))	//surplus  
			{
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_PISDT,ST_PGRDT,ST_PMRDT,ST_PSNDT,"
					+"ST_STKQT,ST_PPOVL,ST_WAVRT,ST_MINLV,(day(getdate()) - day(ST_PISDT)) L_MON,"
					+"ST_LOCCD,STP_YCLRT FROM MM_STMST ,MM_STPRC WHERE ST_CMPCD=STP_CMPCD and ST_STRTP = STP_STRTP AND "
					+"ST_MATCD = STP_MATCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
					+"AND (day(getdate()) - day(ST_PISDT)) >= "+L_intDAY+" AND "
					+"SUBSTRING(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText().trim()+"' AND '"
					+txtTOGRP.getText().trim()+"' AND isnull(ST_STKQT,0) > 0 AND "
					+"isnull(ST_SRPQT,0) > 0 AND isnull(ST_STSFL,'') <> 'X' AND "
					+"isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= "+txtMATVL.getText();
				if(txtLOCCD.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtLOCCD.getText().trim()+"%'";	
				M_strSQLQRY += strORDER;
			}
			else if(cmbMATCT.getSelectedItem().toString().equals("All(Marked)"))	//All Marked
			{
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_PISDT,ST_PGRDT,ST_PMRDT,ST_PSNDT,ST_MINLV,"
					+"ST_STKQT,ST_PPOVL,ST_WAVRT,(day(getdate()) - day(ST_PISDT)) L_MON,ST_LOCCD,STP_YCLRT FROM "
					+"MM_STMST,MM_STPRC WHERE ST_CMPCD = STP_CMPCD and ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD and  "
					+"(day(getdate()) - day(ST_PISDT)) >= "+L_intDAY+" AND "
					+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ST_MATCD,1,2) BETWEEN '"+txtFMGRP.getText().trim()+"' AND '"+txtTOGRP.getText().trim()+"' AND "
					+"isnull(ST_STSFL,'') <> 'X' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STKQT,0) > 0 AND "
					+"((isnull(ST_STSFL,' ') = '9' OR isnull(ST_VEDFL,' ') = 'E' OR isnull(ST_SRPQT,0) > 0)) AND "
					+"isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= "+txtMATVL.getText();
				if(txtLOCCD.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtLOCCD.getText().trim()+"%'";	
				M_strSQLQRY += strORDER;
			}
			else if(cmbMATCT.getSelectedItem().toString().equals("All(NonMarked)"))	//All Non Marked
			{
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,ST_PISDT,ST_PGRDT,ST_PMRDT,ST_PSNDT,ST_MINLV,"
					+"ST_STKQT,ST_PPOVL,ST_WAVRT,(day(getdate()) - day(ST_PISDT)) L_MON,ST_LOCCD,STP_YCLRT FROM "
					+"MM_STMST,MM_STPRC WHERE ST_CMPCD=STP_CMPCD and ST_STRTP = STP_STRTP AND ST_MATCD = STP_MATCD AND "
					+"(day(getdate()) - day(ST_PISDT)) >= "+L_intDAY+" AND SUBSTRING(ST_MATCD,1,2) "
					+"BETWEEN '"+txtFMGRP.getText().trim()+"' AND '"+txtTOGRP.getText().trim()+"'  "
					+"AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(ST_STKQT,0) > 0 AND "
					+"isnull(ST_STSFL,' ') NOT IN ('X','9') AND isnull(ST_VEDFL,' ') = 'D' "
					+"AND isnull(ST_SRPQT,0) = 0 AND isnull(ST_STKQT,0) * isnull(STP_YCLRT,0) >= "+txtMATVL.getText();
				if(txtLOCCD.getText().trim().length() >0)
					M_strSQLQRY += " AND ST_LOCCD like '"+txtLOCCD.getText().trim()+"%'";	
				M_strSQLQRY += strORDER;
			}
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					//Conver number of days in to number of month dividing by 30 days
					L_intDAY = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("L_MON"),"0"))/30;
					String L_strMON = nvlSTRVL(M_rstRSSET.getString("L_MON"),"0");
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),""),23));
					intITEM++;		//Increment Item Count Counter by One
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),5));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),5));
					if(M_rstRSSET.getDate("ST_PISDT") != null)
						dosREPORT.writeBytes(padSTRING('L',M_fmtLCDAT.format(M_rstRSSET.getDate("ST_PISDT")),23));
					else
						dosREPORT.writeBytes(padSTRING('L',"",23));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"),14));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"))*Float.parseFloat(nvlSTRVL(M_rstRSSET.getString("STP_YCLRT"),"0")),2),13));
					dblVALUE = dblVALUE + (Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0")))*Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("STP_YCLRT"),"0"));	//Add Value To Value Counter
					if(cmbMATCT.getSelectedIndex() == 4 || cmbMATCT.getSelectedIndex() == 5)
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"0"),13));
					else
						dosREPORT.writeBytes(padSTRING('L',"0",13));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),45));
					if(M_rstRSSET.getDate("ST_PGRDT") != null)
						dosREPORT.writeBytes(padSTRING('L',M_fmtLCDAT.format(M_rstRSSET.getDate("ST_PGRDT")),11));
					else
						dosREPORT.writeBytes(padSTRING('L',"",11));					
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ST_PPOVL"),"0"),14));
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(L_intDAY),13));
					if(cmbMATCT.getSelectedIndex() == 4 || cmbMATCT.getSelectedIndex() == 5)
					{
						dosREPORT.writeBytes(padSTRING('L',String.valueOf(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"0"))*Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("STP_YCLRT"),"0"))),13));
						dblSRPLS = dblSRPLS + Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("ST_MINLV"),"0"))*Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("STP_YCLRT"),"0"));
					}
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst += 3;
				}
			}
			dosREPORT.writeBytes(strDOTLN+"\n");				
			dosREPORT.writeBytes(padSTRING('R',"Total Items : "+intITEM,20));
			dosREPORT.writeBytes(padSTRING('R',"Value Of Items : "+setNumberFormat(dblVALUE,2),45));
			if(cmbMATCT.getSelectedIndex() == 4 || cmbMATCT.getSelectedIndex() == 5)
				dosREPORT.writeBytes(padSTRING('R',"Surplus Value : "+setNumberFormat(dblSRPLS,2),30));
				
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
			setMSG(L_EX,"exePRINT");
		}
	}
	
	/*
	* Method to validate the inputs before execuation of the SQL Query.
	*/
	boolean vldDATA()
	{
		if(txtFMGRP.getText().trim().length() == 0)
		{
			setMSG("Enter From Group Code..",'E');
			txtFMGRP.requestFocus();
			return false;
		}
		else if(txtTOGRP.getText().trim().length() == 0)
		{
			setMSG("Enter To Group Code..",'E');
			txtTOGRP.requestFocus();
			return false;
		}
		else if(Integer.parseInt(txtTOGRP.getText()) < Integer.parseInt(txtFMGRP.getText()))
		{
			setMSG("To Group Should Be Grater Than Or Equal To From Group Code..",'E');
			txtTOGRP.requestFocus();			
			return false;
		}
		else if(txtNOMON.getText().trim().length() == 0)
		{
			setMSG("Enter No Of Month..",'E');
			txtNOMON.requestFocus();
			return false;
		}		
		else if(txtMATVL.getText().trim().length() == 0)
		{
			setMSG("Enter Material Value..",'E');
			txtMATVL.requestFocus();
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
		return true;		
	}
	/**
	 * Method to generate the Header part of the report.
	 */
	void prnHEADER()
	{
		try
		{		
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n"+cl_dat.M_strCMPNM_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Non Moved "+cmbMATCT.getSelectedItem().toString()+" Items List For Period Over "+txtNOMON.getText()+" Months",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Item Code              UOM  Location         Pr.Iss Date   Stk.On.Hand       Value    Splus Qty."+"\n");			
			dosREPORT.writeBytes("Description                                  Pr.Rec.Date  Pr. P.O.Rate  No.Of.Month  Splus Value"+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			cl_dat.M_intLINNO_pbst = 9;
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
				if(input == txtFMGRP || input == txtTOGRP )
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3("Select count(*) from MM_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(ST_MATCD,1,2) like '"+((JTextField)input).getText()+"' AND ST_STRTP = '"+M_strSBSCD.substring(2,4)+"'");
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)<= 0)
							{
								setMSG("Invalid Material Group ",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Material Group ",'E');
							return false;
						}
			
					}
					else
					{
						setMSG("Invalid Material Group ",'E');
						return false;
					}
				}				
				return true;
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"verify");
				return false;
			}
		}
	}
}

