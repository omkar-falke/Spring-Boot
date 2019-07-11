/*
System Name		: Materials Management System
Program Name	: Receipt Summary
Author			: Mr. S.R.Mehesare
Modified Date	: 18/10/2005
Documented Date	: 
Version			: MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.KeyEvent;import java.sql.ResultSet;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : Receipt Summary

Purpose : In Receipt summary report sub sub group code, description and Receipt Value is displayed. 

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    GR_ACPDT                MM_GRMST           Date          Acepted Date
txtTODAT    GR_ACPDT                MM_GRMST           Date          Acepted Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from MM_GRMST and CO_CTMST for condiations :-
    1) SUBSTRING(CT_MATCD,1,6) = SUBSTRING(GR_MATCD,1,6) 
    2) AND GR_STRTP = specified Store Type 
    3) AND CT_CODTP = 'SS' 
    4) AND isnull(GR_ACPQT,0) > 0 
    5) AND GR_ACPDT = the given Date range.
<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then current Date.
</I> */

class mm_rprcs extends cl_rbase
{								/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtFMDAT;/** JTextField to display to enter To date to specify date range.*/
	private JTextField txtTODAT;/** String variable for Store Type.*/
	private String strSTRTP;	/** String variable for Store Type Description.*/
	private String strSTRNM;	/** String variable for generated Report File Name.*/
	private String strFILNM;	/** Integer Variable to count the Number of records fetched to block the Report if No data Found.*/
	private int intRECCT;		/** DataOutputStream to generate hld the stream of data.*/
	private DataOutputStream dosREPORT;/** FileOutputStream to generate the Report file form stream of data.*/
	private FileOutputStream fosREPORT;/** String variable to print Dotted Line in the Report.*/	
	private String strDOTLN = "------------------------------------------------------------------------------------------------";
	public mm_rprcs()
	{
		super(2);
		setMatrix(20,8);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);					
		add(new JLabel("From Date"),4,4,1,1,this,'L');
		add(txtFMDAT = new TxtDate(),4,5,1,1,this,'L');
		add(new JLabel("To Date"),5,4,1,1,this,'L');
		add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');
		M_pnlRPFMT.setVisible(true);
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC ==  cl_dat.M_btnSAVE_pbst)
			{
				cl_dat.M_PAGENO = 0;
				cl_dat.M_intLINNO_pbst = 0;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == txtFMDAT)			
			setMSG("Enter From Date..",'N');			
		else if(M_objSOURC == txtTODAT)			
			setMSG("Enter To Date..",'N');		
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{					
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					txtTODAT.requestFocus();
			}
			else if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
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
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rprcs.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rprcs.doc";
			
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Receipt Summary"," ");
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
	 * Method to validate the data before execuation of the SQL Quary.
	 */
	boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("From Date Should Not Be Grater Than Todays Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("To Date Should Not Be Grater Than Todays Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA()
	{
		if(!vldDATA())
			return;		
		try
		{
			intRECCT = 0; 
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Receipt Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
				
			prnHEADER();
				
			M_strSQLQRY = "SELECT SUBSTRING(GR_MATCD,1,6) GR_MATCD,CT_MATDS,"
				+"sum(isnull(GR_GRNRT,0) * isnull(GR_ACPQT,0)) GR_MATVL "
				+"FROM MM_GRMST,CO_CTMST WHERE SUBSTRING(CT_MATCD,1,6) = SUBSTRING(GR_MATCD,1,6) AND "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND CT_CODTP = 'SS' AND isnull(GR_ACPQT,0) > 0 AND "
				+"GR_ACPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
				+"GROUP BY SUBSTRING(GR_MATCD,1,6),CT_MATDS ORDER BY SUBSTRING(GR_MATCD,1,6) ";
							
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1; 
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),12));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),65));					
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_MATVL").toString()),2),15));
					cl_dat.M_intLINNO_pbst++;
				}
                M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n"+strDOTLN);
			
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
	 * Method to generate Header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			if(cl_dat.M_PAGENO == 1)
			{
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY0("SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSST' AND CMT_CODCD = '"+M_strSBSCD.substring(2,4)+"'");
				if(L_rstRSSET !=null)
				{
					if(L_rstRSSET.next())
						strSTRNM = L_rstRSSET.getString("CMT_CODDS").toString();
					L_rstRSSET.close();
				}
			}
			
			dosREPORT.writeBytes("\n"+ cl_dat.M_strCMPNM_pbst +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"SUB SUB GROUP RECEIPT SUMMRY ",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes("Period From : "+txtFMDAT.getText()+" To : "+txtTODAT.getText() +"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Group Code And Description                                                      Value In Rs."+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}