/*
System Name		: Materials Management System
Program Name	: Monthly Stock Status
Author			: Mr.S.R.Mehesare
Modified Date	: 10/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import java.util.Date;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.sql.ResultSet;import java.text.SimpleDateFormat;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
/**<pre>
System Name : Material Management System.
 
Program Name :Monthly Stock Status

Purpose : This program generates Report for Monthly Stock Status for given Sub Sub Group Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_STPRC       STP_MMSBS,STP_STRTP,STP_MATCD                            #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMSUB    STP_MATCD       MM_STPRC     VARCHAR(10)   Material Code.
txtTOSUB    STP_MATCD       MM_STPRC     VARCHAR(10)   Material Code.
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
    Data is taken from MM_STPRC for condiations :- 
       1) STP_STRTP = selected Store Type	
       2) AND SUBSTRING(STP_MATCD,1,6) Between given Sub Sub Group Range.			

<B>Validations & Other Information:</B>
    - Given sub Sub Group must be valid
    - From Sub sub Group must be smaller than To Sub Sub group.
*/

class mm_rpmss extends cl_rbase
{										/** JTextField to display & enter From Sub Sub Group Code.*/
	private JTextField txtFMSUB;		/** JTextField to display & enter To Sub Sub Group Code.*/
	private JTextField txtTOSUB;		/** String variable for storing String formated date.*/
	private String strDATE;				/** String variable for genrated report File Name.*/
	private String strFILNM;			/** Integer variale to count the total number of items.*/
	private int intCOUNT;				/** FileOutputStream Object to generate the Report File from the Stream of Data.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream Object to hold stream of data to generate Report File.*/
	private DataOutputStream dosREPORT;	/** Integer variable to count the number of records fetched to block the Report if no data found.*/
	private int intRECCT;
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	public mm_rpmss()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("From Sub Sub Group"),4,4,1,1.3,this,'R');
			add(txtFMSUB = new TxtNumLimit(6.0),4,5,1,1,this,'L');
			add(new JLabel("To Sub Sub Group"),5,4,1,1.3,this,'R');
			add(txtTOSUB = new TxtNumLimit(6.0),5,5,1,1,this,'L');			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor ");		
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				txtFMSUB.requestFocus();
				setMSG("Please Enter From Sub Sub Code to generate the Report.",'N');
				setENBL(true);
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			intCOUNT = 0;
			cl_dat.M_PAGENO = 0;
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);	
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC == txtFMSUB)
			{
				M_strHLPFLD = "txtFMSUB";
				M_strSQLQRY ="SELECT DISTINCT SUBSTRING(CT_MATCD,1,6),CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='SS' ";
				if(txtFMSUB.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_MATCD LIKE '"+txtFMSUB.getText().trim() +"%'";
				if(txtTOSUB.getText().trim().length() == 6)
					M_strSQLQRY += " AND CT_MATCD <= '"+txtTOSUB.getText().trim() +"'";
				M_strSQLQRY += " ORDER BY SUBSTRING(CT_MATCD,1,6)";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Sub Sub Group Code","Group Name"},2,"CT");
			}
			if(M_objSOURC == txtTOSUB)
			{
				M_strHLPFLD = "txtTOSUB";
				M_strSQLQRY ="SELECT DISTINCT SUBSTRING(CT_MATCD,1,6),CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='SS' ";
				if(txtTOSUB.getText().trim().length() >0)
					M_strSQLQRY += " AND CT_MATCD LIKE '"+txtTOSUB.getText().trim() +"%' ";
				if(txtFMSUB.getText().trim().length() == 6)
					M_strSQLQRY += " AND CT_MATCD >='"+txtFMSUB.getText().trim() +"'";
				M_strSQLQRY += " ORDER BY SUBSTRING(CT_MATCD,1,6)";				
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Sub Sub Group Code","Group Name"},2,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}		
		else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC == txtFMSUB)
				{
					if(txtFMSUB.getText().trim().length() == 6)
					{
						M_strSQLQRY ="SELECT DISTINCT SUBSTRING(CT_MATCD,1,6),CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='SS' ";				
						M_strSQLQRY += " AND CT_MATCD ='"+txtFMSUB.getText().trim() +"' ";
						if(txtTOSUB.getText().trim().length() == 6)
							M_strSQLQRY += " AND CT_MATCD <='"+txtTOSUB.getText().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								txtTOSUB.requestFocus();
								setMSG("Please enter to Sub Sub Group to generate the Report..",'N');
							}
							else							
								setMSG("Invalid Sub Sub Group Press F1 to select from List..",'N');							
							M_rstRSSET.close();
						}
					}
					else
						setMSG("To generate the Report, Sub Sub Group cannot be Blank..",'N');
						
				}
				if(M_objSOURC == txtTOSUB)
				{
					if(txtTOSUB.getText().trim().length()== 6)
					{
						M_strSQLQRY ="SELECT DISTINCT SUBSTRING(CT_MATCD,1,6),CT_MATDS FROM CO_CTMST WHERE CT_CODTP ='SS' ";				
						M_strSQLQRY += " AND CT_MATCD ='"+txtTOSUB.getText().trim() +"' ";
						if(txtFMSUB.getText().trim().length() == 6)
							M_strSQLQRY += " AND CT_MATCD >='"+txtFMSUB.getText().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								txtTOSUB.requestFocus();
								setMSG("Please enter to Sub Sub Group to generate the Report..",'N');
							}
							else							
								setMSG("To generate the Report, Sub Sub Group cannot be Blank..",'N');							
							M_rstRSSET.close();
						}
						cl_dat.M_btnSAVE_pbst.requestFocus();			
					}
					else
						setMSG("To generate the Report, Sub Sub Group cannot be Blank..",'N');
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_ENTER");
			}
		}		
	}
	/**
	 * super Class Method overrrided to execuate the F1 Help for selected Field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMSUB")
		{
			txtFMSUB.setText(cl_dat.M_strHLPSTR_pbst);
			txtTOSUB.setEnabled(true);
			txtTOSUB.requestFocus();
		}
		if(M_strHLPFLD == "txtTOSUB")
		{
			txtTOSUB.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpmss.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpmss.doc";
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Monthly Stock Status"," ");
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
	 * Method to validate the Inputs before Execuation of the SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtFMSUB.getText().trim().length() < 6)
			{
				setMSG("Enter From Sub Sub Group Code Or Press F1 For Help..",'E');
				txtFMSUB.requestFocus();
				return false;
			}
			if(txtTOSUB.getText().trim().length() < 6)
			{
				setMSG("Enter To Sub Sub Group Code Or Press F1 For Help..",'E');
				txtTOSUB.requestFocus();
				return false;
			}
			if(Integer.parseInt(txtFMSUB.getText().trim()) > Integer.parseInt(txtTOSUB.getText().trim()))
			{
				setMSG("To Sub Sub Group Code Should Be Grater Than From Sub Sub Group Code..",'E');
				txtTOSUB.requestFocus();
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
	 * Mehod to fetch data from database & club it with Header & footer of the Report.
	 */
	public void getDATA()
	{
		try
		{	
			intCOUNT = 0;
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Monthly Stock Status</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			
			prnHEADER();
			
			SimpleDateFormat L_sdtTEMP = new SimpleDateFormat("MMMMM,yyyy");
			//take processing date form the co_cdtrn table that is updated in monthly pocessing 
			//date is in dd/mm/yyyy format format it to above format
			M_strSQLQRY = "SELECT CMT_CCSVL FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'MMXXPRC' AND CMT_CODCD = 'MONTH' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
					strDATE = L_sdtTEMP.format(M_fmtLCDAT.parse(M_rstRSSET.getString("CMT_CCSVL")));					
				M_rstRSSET.close();
			}
			M_strSQLQRY = "SELECT STP_MATCD,STP_MATDS,STP_UOMCD,STP_MOSQT,STP_MCSQT, "
				+"(isnull(STP_MCRCQ,0) - isnull(STP_MORCQ,0)) RECQT, "	//receipt quantity
				+"(isnull(STP_MCMRQ,0) - isnull(STP_MOMRQ,0)) MRNQT, "	//mrn quantity
				+"(isnull(STP_MCISQ,0) - isnull(STP_MOISQ,0)) ISSQT, "	//issue quantity
				+"(isnull(STP_MCSAQ,0) - isnull(STP_MOSAQ,0)) SANQT "	//san quantity
				+"FROM MM_STPRC_280210 WHERE STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
			if(txtFMSUB.getText().trim().length() > 0 || txtTOSUB.getText().trim().length() > 0)
				M_strSQLQRY += " AND SUBSTRING(STP_MATCD,1,6) BETWEEN '"+txtFMSUB.getText().trim()+"' AND '"+txtTOSUB.getText().trim()+"' ";
			M_strSQLQRY += "ORDER BY STP_MATCD";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst >58)	
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();	
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATCD"),""),23));		//print material code
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_UOMCD"),""),24));		//print unit of measurmet 
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_MOSQT"),""),15));		//print montly opening quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("RECQT"),"0"),11));		//print receit quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ISSQT"),"0"),13));		//print issue quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_MCSQT"),""),11));		//print monthly closing quantity
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("STP_MATDS"),""),60));		//print material description
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MRNQT"),"0"),13));		//print mrn quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SANQT"),"0"),13));		//print san quantity
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 3;
					intCOUNT++;
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "SELECT sum(isnull(STP_MOSQT,0)) STP_MOSQT,sum(isnull(STP_MCSQT,0)) STP_MCSQT, "
				+"sum((isnull(STP_MCRCQ,0) - isnull(STP_MORCQ,0))) RECQT, "	//receipt quantity
				+"sum((isnull(STP_MCMRQ,0) - isnull(STP_MOMRQ,0))) MRNQT, "	//mrn quantity
				+"sum((isnull(STP_MCISQ,0) - isnull(STP_MOISQ,0))) ISSQT, "	//issue quantity
				+"sum((isnull(STP_MCSAQ,0) - isnull(STP_MOSAQ,0))) SANQT "	//san quantity
				+"FROM MM_STPRC_280210 WHERE STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_STRTP = '"+M_strSBSCD.substring(2,4)+"' ";
			if(txtFMSUB.getText().trim().length() > 0 || txtTOSUB.getText().trim().length() > 0)
				M_strSQLQRY += " AND SUBSTRING(STP_MATCD,1,6) BETWEEN '"+txtFMSUB.getText().trim()+"' AND '"+txtTOSUB.getText().trim()+"' ";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			dosREPORT.writeBytes(strDOTLN+"\n");			
			if(M_rstRSSET != null && M_rstRSSET.next())
			{
					dosREPORT.writeBytes(padSTRING('R',"Total :",23+24));		//print material code
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_MOSQT"),""),15));		//print montly opening quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("RECQT"),"0"),11));		//print receit quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("ISSQT"),"0"),13));		//print issue quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STP_MCSQT"),""),11));		//print monthly closing quantity
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R'," ",60));		//print material description
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("MRNQT"),"0"),13));		//print mrn quantity
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SANQT"),"0"),13));		//print san quantity
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(strDOTLN);			
			}
			dosREPORT.writeBytes("\n Total No Of Items : "+intCOUNT+"\n");	//print total no of items
			dosREPORT.writeBytes( strDOTLN+"\n");			
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
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	/**
	 * Method to generate the Header part of the Report.
	 */
	public void prnHEADER()
	{
		try
		{			
			cl_dat.M_PAGENO++ ;
			dosREPORT.writeBytes("\n"+cl_dat.M_strCMPNM_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stock Statement For The Month Of "+strDATE,strDOTLN.length()-21));
			dosREPORT.writeBytes("Date     : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No. : "+cl_dat.M_PAGENO+"\n");			
			dosREPORT.writeBytes(strDOTLN + "\n");			
			dosREPORT.writeBytes("Material Code         UOM                             Op. Bal.   Receipts        Issue   Cl. Bal."+"\n");			
			dosREPORT.writeBytes("Description                                                       Returns  Adjustments"+"\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");
			cl_dat.M_intLINNO_pbst = 8;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}