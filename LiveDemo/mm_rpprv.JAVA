/*
System Name   : Material Management System
Program Name  : Preservation Activity Report 
Program Desc. : 
Author        : Mr. S.R.Mehesare
Date          : 11/10/2005
Version       : MMS v2.0.0
*/

import java.sql.ResultSet;import javax.swing.JComponent;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.InputVerifier;import java.awt.event.FocusEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : Preservation Activity Report.

Purpose : This program generates Preservation Activity Report for given 
Material Code Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
MM_PVMST                                                               #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMMAT    CT_MATCD       MM_CTMST      VARCHAR(10)   Material Code
txtTOMAT    CT_MATCD       MM_CTMST      VARCHAR(10)   Material Code
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
    Data is taken from CO_CTMST,MM_PVMST and MM_STMST for conditions :- 
      1) CT_PSVCD = PV_PSVCD 
      2) AND ST_MATCD = CT_MATCD 
      3) AND CT_PSVFL = 'Y' 
      4) AND IFNULL(CT_STSFL,'') <> 'X'
      5) AND IFNULL(ST_STSFL,'') <> 'X' 
      6) AND CT_MATCD Between given range of Material Codes.

<B>Validations & Other Information:</B>
    - The Material Codes entered must be valid.
</I> */

class mm_rpprv extends cl_rbase
{										/** JTextField to display & to enter From Material code.*/
	private JTextField txtFMMAT;		/** JTextField to display & to enter To Material code.*/
	private JTextField txtTOMAT;		/** String variable for Material code to check with previous one in printing.*/
	private String strMATCD;			/** String variable for Preservation activity description used to check description exceeds 43 character	 */
	private String strACTDS;			/** Integer variable for item count. */
	private int intITMCT;				/** FileOutputStream Object to generate the Report file from the stream of data.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream Object to hold the report data in stream to generate the Report.*/
	private DataOutputStream dosREPORT;	/** String variable for the generated Report File Name.*/
	private String strFILNM;			/** Integer variable to count the number of records fetched to block th Report if no Data found.*/
	private int intRECCT;
	private String strDOTLN ="------------------------------------------------------------------------------------------------";
	private INPVF objINPVR = new INPVF();
	public mm_rpprv()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			add(new JLabel("From Material Code"),4,3,1,1.3,this,'R');
			add(txtFMMAT = new TxtNumLimit(10.0),4,4,1,1,this,'L');
			add(new JLabel("To Material Code"),5,3,1,1.3,this,'R');
			add(txtTOMAT = new TxtNumLimit(10.0),5,4,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			txtFMMAT.setInputVerifier(objINPVR);
			txtTOMAT.setInputVerifier(objINPVR);
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
				txtFMMAT.requestFocus();
				setENBL(true);
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
		}		
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtFMMAT)
			{
				M_strHLPFLD = "txtFMMAT";
				M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_PSVFL = 'Y' ";
				if(txtTOMAT.getText().trim().length() == 10)
					M_strSQLQRY += "AND CT_MATCD <='"+txtTOMAT.getText().trim()+"' ";
				if(txtFMMAT.getText().trim().length() > 0)
					M_strSQLQRY += "AND CT_MATCD LIKE '"+txtFMMAT.getText().trim()+"%' ";
				M_strSQLQRY += "AND isnull(CT_STSFL,'') <> 'X' ORDER BY CT_MATCD ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");					
			}
			if(M_objSOURC == txtTOMAT)
			{
				M_strHLPFLD = "txtTOMAT";
				M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_PSVFL = 'Y' ";
				if(txtFMMAT.getText().trim().length() == 10)
					M_strSQLQRY += "AND CT_MATCD >='"+txtFMMAT.getText().trim()+"' ";
				if(txtTOMAT.getText().trim().length() > 0)
					M_strSQLQRY += "AND CT_MATCD LIKE '"+txtTOMAT.getText().trim()+"%' ";
				M_strSQLQRY += "AND isnull(CT_STSFL,'') <> 'X' ORDER BY CT_MATCD ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");					
			}
		}		
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMMAT)
			{
				if(txtFMMAT.getText().trim().length() == 10)
					txtTOMAT.requestFocus();
			}
			if(M_objSOURC == txtTOMAT)
				cl_dat.M_btnSAVE_pbst.requestFocus();		
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtFMMAT)
			setMSG("Enter From Preservation Material Code Or Press 'F1' to select from List ..",'N');
		if(M_objSOURC == txtTOMAT)
			setMSG("Enter To Preservation Material Code Or Press 'F1' to select from List.. ",'N');
	}
	/**
	 * Super class method overrided  to execute F1 help for selected component.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtFMMAT"))
		{
			txtFMMAT.setText(cl_dat.M_strHLPSTR_pbst);			
		}
		if(M_strHLPFLD.equals("txtTOMAT"))
		{
			txtTOMAT.setText(cl_dat.M_strHLPSTR_pbst);			
		}
	}	
	
	/**
	 * Method to validate inputs data before execuation of Query.
	 */
	public boolean vldDATA()
	{
		try
		{	
			if(txtFMMAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Preservation Material Code Or Press 'F1' For Help ..",'E');
				txtFMMAT.requestFocus();
				return false;
			}
			if(txtTOMAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Preservation Material Code Or Press 'F1' For Help ..",'E');
				txtTOMAT.requestFocus();
				return false;
			}
			if(Double.parseDouble(txtFMMAT.getText()) > Double.parseDouble(txtTOMAT.getText()))
			{
				setMSG("To Material Code Should Be Grater Than Or Equal to From Material Code ..",'E');
				txtTOMAT.requestFocus();
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
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
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
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpprv.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpprv.doc";			
			
			getDATA();
			
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
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
		        {    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Preservation Activity Report"," ");
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
	 * Method to fetch data from the database & club it with header &  footer of the Report.
	 */
	public void getDATA()
	{
		try
		{	
			strMATCD = " ";
			strACTDS = "";
			intITMCT = 0;
			intRECCT = 0;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generating Report..Wait For Some Time...",'N');
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Preservation Activity Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}			
						
			prnHEADER();
			
			M_strSQLQRY = "SELECT CT_MATCD,CT_UOMCD,ST_LOCCD,ST_STKQT,PV_ACTSR,PV_ACTDS,"
				+"CT_MATDS FROM CO_CTMST,MM_PVMST,MM_STMST WHERE CT_PSVCD = PV_PSVCD AND "
				+"ST_MATCD = CT_MATCD AND PV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CT_PSVFL = 'Y' AND isnull(CT_STSFL,'') <> 'X' "
				+"AND isnull(ST_STSFL,'') <> 'X' AND CT_MATCD BETWEEN '"
				+txtFMMAT.getText().trim()+"' AND '"+txtTOMAT.getText().trim()+"' ";		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					strACTDS = "";												
					if(!strMATCD.equals(M_rstRSSET.getString("CT_MATCD")))
					{
						dosREPORT.writeBytes("\n\n");
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CT_MATCD"),20));
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CT_UOMCD"),5));
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("ST_LOCCD"),10));
						dosREPORT.writeBytes(padSTRING('L',M_rstRSSET.getString("ST_STKQT"),11));
						dosREPORT.writeBytes(padSTRING('L',"",6));
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PV_ACTSR"),3));
						cl_dat.M_intLINNO_pbst += 2;
						//check preservatiuon detail is grater than 43 character
						if(M_rstRSSET.getString("PV_ACTDS").toString().length() > 43)
						{
							dosREPORT.writeBytes(M_rstRSSET.getString("PV_ACTDS").toString().substring(0,43));
							strACTDS = M_rstRSSET.getString("PV_ACTDS").toString().substring(43,M_rstRSSET.getString("PV_ACTDS").toString().length());
						}
						else
							dosREPORT.writeBytes(M_rstRSSET.getString("PV_ACTDS"));
						strMATCD = M_rstRSSET.getString("CT_MATCD");
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("CT_MATDS"),52));
						if(strACTDS.length() > 0)
						{
							dosREPORT.writeBytes(padSTRING('L',"",3));
							dosREPORT.writeBytes(strACTDS);
						}
						intITMCT++;
					}
					else	// if material code is equal to previous one
					{
						if(strACTDS.length() > 0)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',"",52));
							System.out.println(1);
							cl_dat.M_intLINNO_pbst += 1;
						}
						if(M_rstRSSET.getString("PV_ACTDS").toString().length() > 43)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',"",52));
							dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PV_ACTSR"),3));
							dosREPORT.writeBytes(M_rstRSSET.getString("PV_ACTDS").toString().substring(0,43));
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(padSTRING('R',"",55));
							dosREPORT.writeBytes(M_rstRSSET.getString("PV_ACTDS").toString().substring(43,M_rstRSSET.getString("PV_ACTDS").toString().length()));
							cl_dat.M_intLINNO_pbst += 2;
						}
					}
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
				}
				M_rstRSSET.close();
				dosREPORT.writeBytes("\n\nTotal No. Of Items : "+intITMCT);	//print the total number of items in the report
				dosREPORT.writeBytes("\n"+strDOTLN + "\n\n");
				dosREPORT.writeBytes("        Attended By								Stores Officer");
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
	 * Method to generate the Header part of the Report.
	 */
	void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO ++;			
			dosREPORT.writeBytes("\n"+ cl_dat.M_strCMPNM_pbst+ "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Preservation Activity ",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Material Code       UOM  Location  Qty On Hand      Activity "+"\n");			
			dosREPORT.writeBytes("Description" +"\n");			
			dosREPORT.writeBytes(strDOTLN);			
			cl_dat.M_intLINNO_pbst = 7;			
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
				if((input == txtFMMAT) && (txtFMMAT.getText().trim().length() == 10))
				{	
					M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_PSVFL = 'Y' ";
					if(txtTOMAT.getText().trim().length() == 10)
						M_strSQLQRY += "AND CT_MATCD <='"+txtTOMAT.getText().trim()+"' ";					
					M_strSQLQRY += "AND CT_MATCD = '"+ txtFMMAT.getText().trim()+"' ";
					M_strSQLQRY += "AND isnull(CT_STSFL,'') <> 'X'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Material Code..",'E');
							L_rstRSSET.close();
							return false;
						}
					}
				}
				if((input == txtTOMAT) && (txtTOMAT.getText().trim().length() == 10))
				{
					M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST WHERE CT_PSVFL = 'Y' ";
					if(txtFMMAT.getText().trim().length() == 10)
						M_strSQLQRY += "AND CT_MATCD >='"+txtFMMAT.getText().trim()+"' ";					
					M_strSQLQRY += "AND CT_MATCD LIKE '"+txtTOMAT.getText().trim()+"' ";
					M_strSQLQRY += "AND isnull(CT_STSFL,'') <> 'X'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}
						else
						{
							setMSG("Invalid Material Code..",'E');
							L_rstRSSET.close();
							return false;
						}
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
	}	
}
