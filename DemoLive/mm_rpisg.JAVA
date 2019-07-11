/*
System Name		: Materials Management System
Program Name	: Main Group Wise Issue Statement 
Author			: Mr. S.R.Mehesare

Modified Date	: 11/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.sql.ResultSet;import java.util.Date;import java.util.Hashtable;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : Main Group Wise Issue Statement 

Purpose : This program generates Report for Main Group Wise Issue for given date Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_ISMST       IS_STRTP,IS_ISSTP,IS_ISSNO,IS_MATCD,IS_TAGNO,IS_BATNO   # 
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtMATGR   IS_MATCD        MM_ISMST      Varchar(10)  Material Group Code
txtFMDAT   IS_ISSDT        MM_ISMST      Timestamp    Issue Date
txtTODAT   IS_ISSDT        MM_ISMST      Timestamp    Issue Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   Data is taken from MM_ISMST and CO_CTMST for condiations :- 
     1) CT_MATCD = IS_MATCD
     2) AND IS_STRTP = selected Dtore Type
     3) AND SUBSTR(IS_MATCD,1,2) = given Main Group Code.
     4) AND IS_ISSDT between given date range.
     5) AND IS_ISSQT > 0
       
<B>Validations & Other Information:</B>
    - Main Group Code Given must be valid.
    - To date must be greater than from date & smaller than current date.
</I> */

class mm_rpisg extends cl_rbase
{									/** JTextField to display & to enter Main group Code.*/
	private JTextField txtMATGR;	/** JTextField to display & to enter From Date.*/
	private JTextField txtFMDAT;	/** JTextField to display & to enter To Date.*/
	private JTextField txtTODAT;	/** Integer variable to count total Items found.*/
	private int intITMCT;			/** Integer variable to count number of rows fetched to block the Report if data not Found. */
	private int intRECCT;			/**	String varaible for Material code.*/		
	private String strMATCD="";		/** String variable for generated Report File Name.*/
	private String strFILNM;		/** Hashtable to hold Department code with Description.*/
	private Hashtable<String,String> hstDPTCD;		/** FileOutputStrem  Object to generate the Report file from stream of data.*/
	private FileOutputStream fosREPORT;/** DataOutputStream object to generate the Stream of report data.*/
	private DataOutputStream dosREPORT;
	private String strDOTLN = "------------------------------------------------------------------------------------------------";
	public mm_rpisg()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Main Group"),4,3,1,1,this,'L');
			add(txtMATGR = new TxtNumLimit(2.0),4,4,1,1,this,'L');
			add(new JLabel("From Date"),5,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),5,4,1,1,this,'L');
			add(new JLabel("To Date"),6,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),6,4,1,1,this,'L');			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
			hstDPTCD = new Hashtable<String,String>();			
			// Method To Store Department Code And Departmetn Name In Hashtable hstDPTCD			
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'COXXDPT'";
			M_rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstDPTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));			
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In Constructor");
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
					txtMATGR.requestFocus();
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
			else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
			{
				strMATCD = "";
				intITMCT = 0;
				cl_dat.M_PAGENO = 0;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtMATGR)
			{
				M_strHLPFLD = "txtMATGR";
				M_strSQLQRY = "select CT_GRPCD,CT_MATDS from CO_CTMST where ";
				if(txtMATGR.getText().trim().length() > 0)
					M_strSQLQRY += "CT_GRPCD like '"+txtMATGR.getText().trim()+"' and ";
				M_strSQLQRY += "CT_CODTP = 'MG' order by CT_GRPCD";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Group Code","Description"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtMATGR)
			{
				if(txtMATGR.getText().length() == 2)
					txtFMDAT.requestFocus();				
				else
				{
					txtMATGR.requestFocus();
					setMSG("Enter Main Group Code Or Press 'F1' For Help..",'E');
				}					
			}
			if((M_objSOURC == txtFMDAT )&&(txtFMDAT.getText().length() == 10))
			{				
				txtTODAT.requestFocus();
			}
			if((M_objSOURC == txtTODAT )&&(txtTODAT.getText().length() == 10))
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}			
		}
	}
	/**
	 * Method to execuate the F1 Help for the selected component.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtMATGR")
		{
			txtMATGR.setText(cl_dat.M_strHLPSTR_pbst);
			txtFMDAT.requestFocus();
		}	
	}	
	/**
	 * Method to validate the Inputs before execuation of the SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtMATGR.getText().trim().length() == 0)
			{
				setMSG("Enter Main Group Code ",'E');
				txtMATGR.requestFocus();
				return false;
			}
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
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
				setMSG("Date Should Not Be Grater Than Today..",'E');
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In vldDATA");
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpisg.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpisg.doc";			
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Main Group Wise Issue Statement"," ");
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
	 * Method to fetch data from the database & to club it with Header & footer.
	 */
	public void getDATA()
	{
		try
		{
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Main Group Wise Issue Statement</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}			
						
			prnHEADER();
			
			M_strSQLQRY = "SELECT IS_ISSNO,IS_MATCD,IS_ISSQT,IS_DPTCD,CT_MATDS,CT_UOMCD "
				+"FROM MM_ISMST,CO_CTMST WHERE CT_MATCD = IS_MATCD AND "
				+"IS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IS_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"SUBSTRING(IS_MATCD,1,2) = '"+txtMATGR.getText().trim()+"' AND IS_ISSDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()))+"' AND "
				+"IS_ISSQT > 0 ORDER BY IS_MATCD,IS_DPTCD ";
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
					if(!strMATCD.equals(nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"")))
					{
						strMATCD = nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),"");
						dosREPORT.writeBytes("\n\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),67));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4));
						intITMCT++;
						cl_dat.M_intLINNO_pbst += 2;
				   	}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",20));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_ISSNO"),""),20));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IS_ISSQT"),""),20));
					dosREPORT.writeBytes(String.valueOf(hstDPTCD.get(nvlSTRVL(M_rstRSSET.getString("IS_DPTCD"),""))));
					cl_dat.M_intLINNO_pbst += 1;
				}
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Total Items : "+intITMCT);
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
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	 * Method to generate the header of the Report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO ++;
			dosREPORT.writeBytes("\n"+cl_dat.M_strCMPNM_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"List Of Issue From "+txtFMDAT.getText()+" To "+txtTODAT.getText()+" For Material Group "+txtMATGR.getText().trim(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date     : "+cl_dat.M_strLOGDT_pbst+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No. : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("Material Code And Description                                                 UOM"+"\n");			
			dosREPORT.writeBytes("                    Issue Number        Issue Quantity      Department"+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}