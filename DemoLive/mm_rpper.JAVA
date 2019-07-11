/*
System Name		: Materials Management System
Program Name	: Perpetual Inventory / Discrepancy Report 
Author			: Mr S.R.Mehesare
Modified Date	: 11/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.sql.ResultSet;import java.util.Date;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;

/**<pre>
System Name : Material Management System.
 
Program Name : Perpetual Inventory / Discrepancy Report .

Purpose : This program generates Report for Perpetual Inventory / Discrepancy Report for 
given Material Code Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
MM_PIMST       PI_MMSBS,PI_STRTP,PI_PINNO,PI_MATCD                     #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
cmbRPTOP    Report Type
txtFMSUB    CT_MATCD       MM_CTMST      VARCHAR(10)   Material Code
txtTOSUB    CT_MATCD       MM_CTMST      VARCHAR(10)   Material Code
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   For External call, Data is taken from MM_PIMST and MM_STMST for condiations :- 				
       1) ST_STRTP = PI_STRTP 
       2) AND ST_MATCD = PI_MATCD 
       3) AND PI_STRTP = Selected Store Type
       4) AND PI_PINNO = Perpetual number Passed from the Other Class is used.
			
   For Perpetual Inventory, Data is taken from MM_PIMST and MM_STMST for condiations :-	
       1) ST_STRTP = PI_STRTP 
       2) AND ST_MATCD = PI_MATCD 
       3) AND PI_STRTP = Selected Store Type
       4) PI_PINNO = given Perpetual number
				
   For Discrepancy, Data is taken from MM_PIMST and MM_STMST for condiations :-	
       1) ST_STRTP = PI_STRTP 
       2) AND ST_MATCD = PI_MATCD 
       3) AND PI_STRTP = selected Store type
       4) AND PI_PINNO = given Perpetual number
       5) AND SUBSTRING(PI_MATCD,1,6) between given range of material code.
       6) AND isnull(PI_STKQT,0) - isnull(PI_ACTQT,0) <> 0
       7) AND isnull(PI_STSFL,' ') <> 'X' 
       8) AND isnull(ST_STSFL,'') <> 'X'

<B>Validations & Other Information:</B>
    - The Material Codes entered must be valid.
    - Perpetual Number given must be valid.
</I> */

class mm_rpper extends cl_rbase
{										/** JComboBox to display & to select Report type. */
	public JComboBox cmbRPTOP;			/** JTextField for Perpetual inventory Number.*/
	public TxtNumLimit txtPINNO;		/** JTextField for From Sub Sub Group Code.*/
	private TxtNumLimit txtFMSUB;		/** JTextField For To Sub Sub Group Code.*/
	private TxtNumLimit txtTOSUB;		/** String variable for Perpetual Inventory Date.*/
	private String strPINDT;			/** String variable for Perpetual Inventory Number*/
	private String strPINNO;			/** boolean variable to specify if it is called by external program it is true.*/
	private boolean blnEXTCL;			/** String variable for generated report File Name.*/
	private String strFILNM;			/** Integer variable for item count.*/
	private int intITMCT;				/** Integer variable to count the number of records fetched to block the Report if Data not Found.*/
	private int intRECCT;				/** DataOutputStream Object to hold the stream of data to generate the Report.*/
	private DataOutputStream dosREPORT;	/** FileOutputStream Object to generate the Report File from the stream of data.*/
	private FileOutputStream fosREPORT;	
	private String strDOTLN ="-------------------------------------------------------------------------------------------------";
	public mm_rpper(String P_strSBSCD,String a)	
	{
		super();
		M_strSBSCD = P_strSBSCD;
		strPINNO = a;
		blnEXTCL = true;		//set extcl true if called by external program
		M_rdbTEXT.setSelected(true);
	}
	public mm_rpper()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
						
			add(new JLabel("Report Type"),4,3,1,1.5,this,'R');
			add(cmbRPTOP = new JComboBox(),4,4,1,1.5,this,'L');
			add(new JLabel("Perpetual Inventory No"),5,3,1,1.5,this,'R');
			add(txtPINNO = new TxtNumLimit(8.0),5,4,1,1.5,this,'L');
			add(new JLabel("From Sub Sub Group"),6,3,1,1.5,this,'R');
			add(txtFMSUB = new TxtNumLimit(6.0),6,4,1,1.5,this,'L');
			add(new JLabel("To Sub Sub Group"),7,3,1,1.5,this,'R');
			add(txtTOSUB = new TxtNumLimit(6.0),7,4,1,1.5,this,'L');
			cmbRPTOP.addItem("Perpetual Inventory");
			cmbRPTOP.addItem("Discrepancy");
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
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
				txtPINNO.requestFocus();
				cmbRPTOP.setEnabled(true);
				txtPINNO.setEnabled(true);
				txtFMSUB.setEnabled(false);
				txtTOSUB.setEnabled(false);				
			}
			else
				setENBL(false);	
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			blnEXTCL = false;	//initialise false if called by internal program			
			intITMCT = 1;
			strPINDT = "";
		}
		if(M_objSOURC == cmbRPTOP)
		{			
			txtPINNO.setText("");
			txtFMSUB.setText("");
			txtTOSUB.setText("");
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				txtFMSUB.setEnabled(false);	
				txtTOSUB.setEnabled(false);
			}
			else
			{
				txtFMSUB.setEnabled(true);	
				txtTOSUB.setEnabled(true);
			}
			txtPINNO.requestFocus();
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(M_objSOURC == cmbRPTOP)
				setMSG("Select Report Option..",'N');
			if(M_objSOURC == txtPINNO)
				setMSG("Enter Perpetual Number Or 'F1' For Help..",'N');
			if(M_objSOURC == txtFMSUB)
				setMSG("Enter From Sub Sub Group Code Or 'F1' For Help..",'N');
			if(M_objSOURC == txtTOSUB)
				setMSG("Enter To Sub Sub Group Code Or 'F1' For Help..",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Focus Gained");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if((M_objSOURC == txtPINNO) && (txtPINNO.getText().trim().length()==8))
			{
				if(cmbRPTOP.getSelectedIndex() == 0)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					txtFMSUB.requestFocus();
			}
			if(M_objSOURC == txtFMSUB)
			{
				if(txtFMSUB.getText().trim().length()==6)
					txtTOSUB.requestFocus();
				else
					setMSG("Invalid Material Sub Group Code..",'E');
			}
			if(M_objSOURC == txtTOSUB)
				cl_dat.M_btnSAVE_pbst.requestFocus();			
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPINNO)
			{
				M_strHLPFLD = "txtPINNO";
				M_strSQLQRY = "SELECT DISTINCT PI_PINNO,PI_PINDT FROM MM_PIMST WHERE ";
				if(txtPINNO.getText().trim().length() > 0)
					M_strSQLQRY += "PI_PINNO LIKE '"+txtPINNO.getText().trim()+"%' AND ";
				M_strSQLQRY += "PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(PI_STSFL,' ') <> 'X' ORDER BY PI_PINNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"PERPETUAL INVENTORY NO","PERPETURAL INV DATE"},2,"CT");
			}
			if(M_objSOURC == txtFMSUB)
			{
				M_strHLPFLD = "txtFMSUB";
				M_strSQLQRY = "SELECT SUBSTRING(CT_MATCD,1,6)MATCD,CT_MATDS FROM CO_CTMST WHERE "
					+"isnull(CT_CODTP,' ') = 'SS' AND ";
				if(txtTOSUB.getText().trim().length() == 6 )
					M_strSQLQRY += "SUBSTRING(CT_MATCD,1,6) <= '"+txtTOSUB.getText().trim()+"' AND ";
				if(txtFMSUB.getText().trim().length() > 0)
					M_strSQLQRY += "SUBSTRING(CT_MATCD,1,6) LIKE '"+txtFMSUB.getText().trim()+"%' AND ";
				M_strSQLQRY += "isnull(CT_STSFL,'') <> 'X' ORDER BY SUBSTRING(CT_MATCD,1,6) ASC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"SUB SUB GROUP CODE","DESCRIPTION"},2,"CT");
			}
			if(M_objSOURC == txtTOSUB)
			{
				M_strHLPFLD = "txtTOSUB";
				M_strSQLQRY = "SELECT SUBSTRING(CT_MATCD,1,6)MATCD,CT_MATDS FROM CO_CTMST WHERE "
					+"isnull(CT_CODTP,' ') = 'SS' AND ";
				if(txtFMSUB.getText().trim().length() == 6)
					M_strSQLQRY += "SUBSTRING(CT_MATCD,1,6) >= '"+txtFMSUB.getText().trim()+"' AND ";
				if(txtTOSUB.getText().trim().length() > 0)
					M_strSQLQRY += "SUBSTRING(CT_MATCD,1,6) LIKE '"+txtTOSUB.getText().trim()+"%' AND ";
				M_strSQLQRY += "isnull(CT_STSFL,'') <> 'X' ORDER BY SUBSTRING(CT_MATCD,1,6) ASC";
				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"SUB SUB GROUP CODE","DESCRIPTION"},2,"CT");
			}
		}
	}
	/**
	 * super Class Method to execuate the F1 help for the selected field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPINNO")
		{
			txtPINNO.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD == "txtFMSUB")
		{
			txtFMSUB.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD == "txtTOSUB")
		{
			txtTOSUB.setText(cl_dat.M_strHLPSTR_pbst);			
		}
	}		
	/**
	 * Method to generate the Report File & to send at selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    
			getDATA(txtPINNO.getText());
			
			if(dosREPORT != null)
				dosREPORT.close();
			if(fosREPORT != null)
				fosREPORT.close();
			
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				//if (M_rdbTEXT.isSelected() || blnEXTCL)
				    doPRINT(strFILNM);
				//else 
		        //{    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				//}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Perpetual Inventory / Discrepancy Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}
	/** 
	 * Method to check the validity of the Input Data before execuation of SQL Query.
	 */
	public boolean vldDATA()
	{
		if(blnEXTCL)	//Return true if called externaly by other program i.e perpetural inventory entry
			return true;
		if(txtPINNO.getText().trim().length() < 8)
		{
			txtPINNO.requestFocus();
			setMSG("Enter Perpetual Inventory Number Or Press 'F1' For Help..",'E');
			return false;
		}
		if(txtFMSUB.getText().trim().length() > 0 && txtTOSUB.getText().trim().length() > 0)
		{
			if(Integer.parseInt(txtFMSUB.getText().toString()) > Integer.parseInt(txtTOSUB.getText().toString()))
			{
				setMSG("To Code Should Be Grater Than Or Equal To From Code",'E');
				txtTOSUB.requestFocus();
				return false;
			}
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
	 * Method to fetch data from the database & club it with Header & footer.
	 */
	public void getDATA(String P_strPINNO)
	{
		boolean L_blnFIRST = true;	
		intITMCT = 0;
		strPINDT = "";
		java.util.Date tmpDATE;		
		try
		{
			if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpper.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpper.doc";			
			
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generating Report..Wait For Some Time...",'N');
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Perpetual Inventory / Discrepancy Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			
		//	prnHEADER();
			
			M_strSQLQRY = "SELECT PI_PINNO,PI_PINDT FROM MM_PIMST WHERE "
			+"PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PI_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND PI_PINNO = '"
			+strPINNO+"'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					tmpDATE = M_rstRSSET.getDate("PI_PINDT");
					if(tmpDATE != null)
						strPINDT = M_fmtLCDAT.format(tmpDATE);	
					else
						strPINDT = "";
				}
				M_rstRSSET.close();
			}
			prnHEADER();
			
			if(blnEXTCL)//Query for External called program
				M_strSQLQRY = "SELECT PI_PINNO,PI_PINDT,PI_MATCD,ST_MATDS,ST_UOMCD,"
					+"ST_LOCCD FROM MM_PIMST,MM_STMST WHERE ST_CMPCD=PI_CMPCD and ST_STRTP = PI_STRTP AND "
					+"ST_MATCD = PI_MATCD AND PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PI_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
					+"AND PI_PINNO = '"+strPINNO+"' ORDER BY PI_STRTP,PI_MATCD";
			
			else if(cmbRPTOP.getSelectedIndex() == 0)
				M_strSQLQRY = "SELECT PI_PINNO,PI_PINDT,PI_MATCD,ST_MATDS,ST_UOMCD,ST_LOCCD FROM MM_PIMST,"
					+"MM_STMST WHERE ST_CMPCD=PI_CMPCD and ST_STRTP = PI_STRTP AND ST_MATCD = PI_MATCD AND "
					+"PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PI_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"PI_PINNO = '"+txtPINNO.getText().trim()+"' ORDER BY PI_STRTP,PI_MATCD";
			else
			{
				M_strSQLQRY = "SELECT PI_PINNO,PI_PINDT,PI_MATCD,PI_STKQT,PI_ACTQT,"
					+"(isnull(PI_STKQT,0) - isnull(PI_ACTQT,0))PI_DFFQT,ST_MATDS,ST_UOMCD,"
					+"ST_LOCCD FROM MM_PIMST,MM_STMST WHERE ST_CMPCD=PI_CMPCD and ST_STRTP = PI_STRTP AND "
					+"ST_MATCD = PI_MATCD AND PI_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PI_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
				    +" AND PI_PINNO = '"+txtPINNO.getText().trim()+"' ";
				if(txtFMSUB.getText().trim().length() > 0 && txtTOSUB.getText().trim().length() > 0 )
					M_strSQLQRY +=" AND SUBSTRING(PI_MATCD,1,6) BETWEEN  '"+txtFMSUB.getText().trim()+"' "
						+" AND '"+txtTOSUB.getText().trim()+"' ";
				M_strSQLQRY += " AND isnull(PI_STKQT,0) - isnull(PI_ACTQT,0) <> 0 "
					+" AND isnull(PI_STSFL,' ') <> 'X' AND isnull(ST_STSFL,'') <> 'X' "
					+" ORDER BY PI_STRTP,PI_MATCD";
			}
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				intRECCT =0;
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					L_blnFIRST = false;
					if(cl_dat.M_intLINNO_pbst > 60)	// Check If Line No is grater than 56 if yes then page break
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					//Method called if externaly called Or Report option index is 0 for internally
					if(blnEXTCL || cmbRPTOP.getSelectedIndex() == 0)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PI_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),60));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),8));
						dosREPORT.writeBytes("\n");  //Added
						cl_dat.M_intLINNO_pbst += 2;
					}
					else
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PI_MATCD"),""),34));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),5));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),10));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PI_STKQT"),""),15));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PI_ACTQT"),""),16));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("PI_DFFQT"),""),16));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""));
						cl_dat.M_intLINNO_pbst += 2;
					}
					intITMCT++;
				}
				if(L_blnFIRST)
					dosREPORT.writeBytes("\n   N I L...");
				dosREPORT.writeBytes("\n"+strDOTLN);
				dosREPORT.writeBytes("\nTotal Items : "+intITMCT+"           Prepared By                                   Checked By");
				setMSG("Report generation Completed..",'N');
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}	
	/**
	 * Method to generate the Report Header.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n"+cl_dat.M_strCMPNM_pbst+"\n");			
			//check called externally of report option index is 0 for internaly
			if(blnEXTCL || cmbRPTOP.getSelectedIndex() == 0)
				dosREPORT.writeBytes(padSTRING('R',"Perpetual Inventory Report ",strDOTLN.length()-23));
			else
				dosREPORT.writeBytes(padSTRING('R',"Discrepancy Report ",strDOTLN.length()-23));
			dosREPORT.writeBytes("Date      : "+cl_dat.M_strLOGDT_pbst+"\n");			
			
			
			//print if called externally by perpetual inverntry program
			if(blnEXTCL)
				dosREPORT.writeBytes(padSTRING('R',"Perpetual Inventory No. : "+strPINNO,strDOTLN.length()-23));
			else
				dosREPORT.writeBytes(padSTRING('R',"Perpetual Inventory No. : "+txtPINNO.getText().trim(),strDOTLN.length()-23));
			dosREPORT.writeBytes("P.I. Date : "+strPINDT+"\n");						
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-23));
			dosREPORT.writeBytes("Page No   : "+cl_dat.M_PAGENO +"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");
			//check called externally of report option index is 0 for intern
			if(blnEXTCL || cmbRPTOP.getSelectedIndex() == 0)  
				dosREPORT.writeBytes("Material Code & Description                                            UOM Location   Stock Qty.");
			else
			{
				dosREPORT.writeBytes("Material Code                     UOM  Location     Qty. On Hand     Actual Qty.  Difference Qty"+"\n");				
				dosREPORT.writeBytes("Material Description");
				cl_dat.M_intLINNO_pbst += 1;
			}			
			dosREPORT.writeBytes("\n"+strDOTLN);
			cl_dat.M_intLINNO_pbst = 8;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}