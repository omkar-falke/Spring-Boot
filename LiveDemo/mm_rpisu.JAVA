/*
System Name		: Materials Management System
Program Name	: Issue Summary
Author			: Mr.S.R.Mehesare

Modified Date	: 08/10/2005
Documented Date	: 
Version			: v2.0.0
*/
import java.sql.ResultSet;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream;import java.io.DataOutputStream;
/**<pre>
System Name : Material Management System.
 
Program Name : Issue Summary.

Purpose : This program generates sub sub group wise Issue summary for given Date Range.
sub-sub group, description and sum of Issue Value(Issue Rate * Issue qty) is printed.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_MATCD                                                #
MM_ISMST       IS_STRTP,IS_ISSTO,IS_ISSNO,IS_MATCD,IS_TAGNO,IS_BATNO   #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    IS_ISSDT       MM_ISMST      Date          Issue Date
txtTODAT    IS_ISSDT       MM_ISMST      Date          Issue Date
--------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)MM_ISMST
                        2)CO_CTMST
<B>Conditions Give in Query:</b>
    Data is taken from MM_ISMST and CO_CTMST for condiations :- 
        1) SUBSTR(CT_MATCD,1,6) = SUBSTR(IS_MATCD,1,6) 
        2) AND IS_STRTP = Selected Store Type
        3) AND CT_CODTP = 'SS' 
        4) AND ifnull(IS_ISSQT,0) > 0 
        5) AND IS_ISSDT Between given date Range.		
       
<B>Validations & Other Information:</B>
    - To Date must be smaller than current date & greater than From date.
</I> */
class mm_rpisu extends cl_rbase
{									/** JTextField to display & enter date.*/
	private JTextField txtFMDAT;	/** JTextField to display & enter date.*/
	private JTextField txtTODAT;	/** String variable for store Type.*/
	private String strSTRTP;		/** String variable for generated Report file Name.*/	
	private String strFILNM;		/** DataOutputStream to hold stream of data to generate the report file.*/
	private DataOutputStream dosREPORT;/** FileOutputStream to generate the report file from the stream of data.*/
	private FileOutputStream fosREPORT;	/** Integer variable to count the Number of records fetched to block report if data not found.*/
	private int intRECCT;
	private String  strDOTLN = "------------------------------------------------------------------------------------------------";
	public mm_rpisu()
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
			add(new JLabel("To Date"),5,3,1,1,this,'L');
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
				{
					setENBL(false);
					setMSG("Please Select an Option..",'N');
				}
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
					setMSG("please enter date to generate report..",'N');
				}
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)				
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					setMSG("TO Date cannot be blank please enter it..",'N');
			}
		}
	}
	/**
	 * Method to validate the inputs given before execuation of SQL Query. 
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
	 * Method to generate the Report File & to send at selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpisu.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpisu.doc";
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Issue Summary"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to generate Report & to set it to specified gestination.
	 */
	public void getDATA()
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
		try
		{	
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Issue Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}

			prnHEADER();

			M_strSQLQRY = "SELECT SUBSTRING(IS_MATCD,1,6) IS_MATCD,CT_MATDS,"
				+"sum(isnull(IS_ISSRT,0) * isnull(IS_ISSQT,0)) IS_ISSVL "
				+"FROM MM_ISMST,CO_CTMST WHERE SUBSTRING(CT_MATCD,1,6) = SUBSTRING(IS_MATCD,1,6) AND "
				+"IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND CT_CODTP = 'SS' AND isnull(IS_ISSQT,0) > 0 AND "
				+"IS_ISSDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
				+"GROUP BY SUBSTRING(IS_MATCD,1,6),CT_MATDS ORDER BY SUBSTRING(IS_MATCD,1,6) ";						
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst >56)
					{						
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER();						
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),""),12));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),65));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(M_rstRSSET.getString("IS_ISSVL").toString()),2),15));
					cl_dat.M_intLINNO_pbst++;
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n"+strDOTLN);
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
		}
		setCursor(cl_dat.M_curDFSTS_pbst);	
	}
	/**
	 * Method to generate Header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{
			String L_strSTRNM="";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY0("SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'MMXXSST' AND CMT_CODCD = '"+M_strSBSCD.substring(2,4)+"'");
			if(L_rstRSSET !=null)
			{
				if(L_rstRSSET.next())
					L_strSTRNM = L_rstRSSET.getString("CMT_CODDS").toString();
				L_rstRSSET.close();
			}
			cl_dat.M_PAGENO ++;
			dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('R',"SUB SUB GROUP ISSUE SUMMRY ",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+L_strSTRNM,strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");
			dosREPORT.writeBytes("Period From : "+txtFMDAT.getText()+" To : "+txtTODAT.getText()+"\n");
			dosREPORT.writeBytes(strDOTLN +"\n");
			dosREPORT.writeBytes("Sub Sub Group Code And Description                                              Value In Rs."+"\n");
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}
