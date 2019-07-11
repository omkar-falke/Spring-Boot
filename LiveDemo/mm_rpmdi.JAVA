/*
System Name   : Material Management System
Program Name  : List Of Modvatable Issue
Program Desc. : 
Author        : Mr S.R.Mehesare
Date          : 08/10/2005
Version       : MMS v2.0.0
*/

import java.sql.Date;
import java.sql.ResultSet;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : List Of Modvatable Issue

Purpose : This program generates Report for List Of Modvatable Issue for given date Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_ISMST       IS_STRTP,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_TAGNO,IS_BATNO   #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT   IS_AUTDT        MM_ISMST      Timestamp    Authorization Date & Time
txtTODAT   IS_AUTDT        MM_ISMST      Timestamp    Authorization Date & Time
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
    Data is taken from MM_ISMST and MM_STMST for condiations :- 
       1) ST_STRTP = IS_STRTP 
       2) AND ST_MATCD = IS_MATCD 
       3) AND IS_STRTP = Selected Store Type
       4) AND DATE(IS_AUTDT) Between given date Range
       5) AND isnull(IS_STSFL,' ') <> 'X' 
       6) AND isnull(ST_STSFL,'') <> 'X' 
       7) AND isnull(IS_MDVQT,0) > 0

<B>Validations & Other Information:</B>
    - To date must be greater than from date & smaller than current date.
</I> */

class mm_rpmdi extends cl_rbase
{									/** JTextField to display & enter From date to specify date range for the Report.*/
	private JTextField txtFMDAT;	/** JTextField to display & enter To date to specify date range for the Report.*/
	private JTextField txtTODAT;	/** String variable for generated report File Name.*/
	private String strFILNM;		/** Integer variable to count the number of records fetched to block the report if data not found.*/
	private int intRECCT;				/** FileOutputStream Object to generate the Report file from stream of data.*/
	private FileOutputStream fosREPORT;/** DataOutputStream Object to hold stream of data to generate the Report.*/
    private DataOutputStream dosREPORT;
	String strDOTLN = "------------------------------------------------------------------------------------------------";
	
	public mm_rpmdi()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);								
			add(new JLabel("From Date"),4,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),4,4,1,1,this,'L');
					
			add(new JLabel("To Date."),5,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),5,4,1,1,this,'L');
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
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
					setENBL(true);				
				}
				else
					setENBL(false);
			}
			if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
				cl_dat.M_PAGENO = 0;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{	
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length()==10)					
				{
					txtTODAT.requestFocus();
					setMSG("Please Enter Date to generate the Reort..",'N');
				}
			}
			if(M_objSOURC == txtTODAT) 
			{
				if(txtTODAT.getText().trim().length()==10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					setMSG("Please Enter date to generate the Report..",'N');
			}
		}
	}
	/**
	 * Method to validate Inputs before execuation of SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Grater Than Today Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Grater Than Today Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				txtTODAT.requestFocus();
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
	}/**
	 * Method to generate the Report File & to send at selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpmdi.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpmdi.doc";
			intRECCT = 0;
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM," List Of Modvatable Issue"," ");
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
	 * Mthod to fetch data from database & to club it with header & footer.
	 */
	public void getDATA()
	{
		try
		{
			Date tmpDATE;String L_strMATCD = "";
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> List Of Modvatable Issue </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			
			prnHEADER();
				
			M_strSQLQRY = "SELECT IS_MATCD,IS_ISSNO,IS_AUTDT,IS_MDVQT,ST_MATDS,ST_UOMCD FROM "
				+"MM_ISMST,MM_STMST WHERE ST_CMPCD = IS_CMPCD and ST_STRTP = IS_STRTP AND ST_MATCD = IS_MATCD "
				+"AND IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4)+"' "
				+"AND CONVERT(varchar,IS_AUTDT,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' "
				+"AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
			    +"AND isnull(IS_STSFL,' ') <> 'X' AND isnull(ST_STSFL,'') <> 'X' AND "
				+"isnull(IS_MDVQT,0) > 0 ORDER BY IS_MATCD,IS_ISSNO ";
							
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);							
				
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					if(!L_strMATCD.equals(M_rstRSSET.getString("IS_MATCD")))
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),47));
						dosREPORT.writeBytes(padSTRING('R'," ",1));
						L_strMATCD = nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"");
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",63));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),""),11));
					tmpDATE = M_rstRSSET.getDate("IS_AUTDT");
					if(tmpDATE != null)
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(tmpDATE),11));
					else
						dosREPORT.writeBytes(padSTRING('R'," ",11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IS_MDVQT"),"0"),11));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 2;
				}
				M_rstRSSET.close();
			}
			
			dosREPORT.writeBytes(strDOTLN +"\n");
			setMSG("Report generation Completed..",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
				setMSG("For HTML file Printing Please select Page Size 210-305 mm  through Page Setup..",'N');
			}												
			dosREPORT.close();
			fosREPORT.close();			
			setCursor(cl_dat.M_curDFSTS_pbst);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
			setCursor(cl_dat.M_curDFSTS_pbst);			
		}
	}
	/**
	 * Method to generate the header part of the Report.
	 */
	public void prnHEADER()
	{
		try
		{			
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,96)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"List Of Modvatable Issues From "+txtFMDAT.getText()+" To "+txtTODAT.getText(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");
			dosREPORT.writeBytes(strDOTLN +"\n");			
			dosREPORT.writeBytes("Item Code  UOM Description                                     Issue No. Issue Date  Modvat Qty."+"\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}