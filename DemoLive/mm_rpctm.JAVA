/*
System Name   : Material Management System
Program Name  : Catalogue Master Report
Program Desc. : List Of Catalouge Items With Details.
Author        : Mr. S.R.Mehesare
Date          : 30/09/2005
Version       : MMS 2.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
/**<pre>
<B>System Name :</B> Material Management System.
 
<B>Program Name :</B> List Of Catalouge Items With Details.

<B>Purpose :</B> This module gives List of List Of Catalouge Items With Details between given 
Material Codes.

List of tables used :
Table Name    Primary key                            Operation done
                                          Insert   Update   Query   Delete	
-----------------------------------------------------------------------------
CO_CTMST      CT_GRPCD,CT_CODTP,CT_MATCD                       #   
CO_CTTRN      CTT_GRPCD,CTT_CODTP,CTT_MATCD,                 
              CTT_LVLNO,CTT_LINNO                              #
-----------------------------------------------------------------------------

List of  fields entered/displayed on screen :
Field Name  Column Name        Table name   Type/Size      Description
-----------------------------------------------------------------------------
txtFMMAT    CT_MATCD,CT_MATDS  CO_CTMST     VARCHAR(10)    Material Code
txtTOMAT    CT_MATCD,CT_MATDS  CO_CTMST     VARCHAR(10)    Material Code
-----------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b>1)CO_CTMST
                       2)CO_CTTRN
<B>Conditions Give in Query:</B>     
   Report data is taken from CO_CTMST and CO_CTTRN for condiations
      1) CTT_MATCD = CT_MATCD AND CTT_CODTP = CT_CODTP";
   if only From Material code is given
      2) AND CT_MATCD >= Given from Material Code.
    if only To Material code is given
      3) AND CT_MATCD <= Given To Material Code.
    if From & To Material code is given
      4) AND CT_MATCD between given Material Codes.
			
<b>Validations :</b>
    - Materal code Given to generate the report must be valid.
    - From Material code must be smaller than or equal to the To material code.
    - To Material code must be greater than or equal to the From material code.
*/

class mm_rpctm extends cl_rbase
{										/** JTextField for From Material Code.*/
	private JTextField txtFMMAT;		/** JTextField for to material code.*/
	private JTextField txtTOMAT;		/** Text Containing.*/
	private JTextField txtTXTCN;		/** Main Group.*/
	private JTextField txtMGPCD;		/** String variable for generated Report File Name.*/
	private String strFILNM;			/** DataOutputStrem object to generate the report Stream.*/
	private FileOutputStream fosREPORT;	/** FileOutPutStream object to generate the Report file from data stream.*/
	private DataOutputStream dosREPORT; /** String variable to Print dotted line in the Report.*/
	private String strDOTLN = "-----------------------------------------------------------------------------------------------------------";
										/** Integer Variable to Count Number of Records Fetched.*/
	int intRECCT = 0;
	
	public mm_rpctm()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);									
			setMatrix(20,8);
			add(new JLabel("Main Group"),3,4,1,1.3,this,'R');
			add(txtMGPCD = new TxtLimit(2),3,5,1,0.5,this,'L');
			add(new JLabel("From Material Code"),4,4,1,1.3,this,'R');
			add(txtFMMAT = new TxtLimit(10),4,5,1,1,this,'L');
			add(new JLabel("To Material Code"),5,4,1,1.3,this,'R');
			add(txtTOMAT = new TxtLimit(10),5,5,1,1,this,'L');
			add(new JLabel("Text Containing"),6,4,1,1.3,this,'R');
			add(txtTXTCN = new TxtLimit(50),6,5,1,4,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor..");
		}
	}			
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter Material code OR Press F1 to Select from List..",'N');				
				setENBL(true);				
				txtFMMAT.requestFocus();
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}				
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst = 0;			
		}				
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);			
			if(M_objSOURC == txtFMMAT)
			{
				M_strHLPFLD = "txtFMMAT";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST where isnull(CT_STSFL,'')<>'X'";
				if(txtMGPCD.getText().trim().length()==2)
					M_strSQLQRY += " AND SUBSTRING(CT_MATCD,1,2)= '"+txtMGPCD.getText().trim()+"'";
				if(txtTXTCN.getText().trim().length()>0)
					M_strSQLQRY += " AND upper(CT_MATDS) like '%"+txtTXTCN.getText().trim()+"%'";
				if(txtFMMAT.getText().trim().length() == 10  && txtTOMAT.getText().trim().length() == 10)
					M_strSQLQRY += " AND CT_MATCD between '"+txtFMMAT.getText()+"' and '"+txtTOMAT.getText()+"'";
				M_strSQLQRY += " ORDER BY CT_MATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");
			}			
			if(M_objSOURC == txtTOMAT)
			{
				M_strHLPFLD = "txtTOMAT";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST where isnull(CT_STSFL,'')<>'X'";
				if(txtTOMAT.getText().trim().length() == 10)
					M_strSQLQRY += " AND CT_MATCD LIKE '"+txtTOMAT.getText()+"%'";
				if(txtFMMAT.getText().trim().length() > 0)
					M_strSQLQRY += " AND CT_MATCD >= '"+txtFMMAT.getText()+"'";
				M_strSQLQRY += " ORDER BY CT_MATCD";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Material Code","Description"},2,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}		
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMMAT)
			{
				if(txtFMMAT.getText().trim().length() == 10)
				{
					try
					{
						M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST where isnull(CT_STSFL,'')<>'X'";
						if(txtTOMAT.getText().trim().length() == 10)
							M_strSQLQRY += " AND CT_MATCD <= '"+txtTOMAT.getText()+"'";					
						M_strSQLQRY += " AND CT_MATCD ='"+txtFMMAT.getText()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())
							{
								M_rstRSSET.close();
							}
							else
							{
								setMSG("Invalid Material Code, Press F1 To Select Form list..",'E');
								M_rstRSSET.close();
								return;
							}						
						}
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"M_objSOURC == txtFMMAT");
					}
				}				
				else if(txtFMMAT.getText().trim().length() > 0)
				{
					setMSG("Invalid Material Code, Press F1 To Select Form list..",'E');					
					return;
				}				
				txtTOMAT.requestFocus();
				setMSG("Please Enter Material code OR Press F1 to Select from List..",'N');				
			}
			else if(M_objSOURC == txtTOMAT)	
			{
				if(txtTOMAT.getText().trim().length() == 10)
				{
					try
					{
						M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS FROM CO_CTMST where isnull(CT_STSFL,'')<>'X'";
						if(txtFMMAT.getText().trim().length() == 10)
							M_strSQLQRY += " AND CT_MATCD >=+ '"+txtFMMAT.getText()+"'";					
						M_strSQLQRY += " AND CT_MATCD ='"+txtTOMAT.getText()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							if(M_rstRSSET.next())							
								M_rstRSSET.close();							
							else
							{
								setMSG("Invalid Material Code, Press F1 To Select Form list..",'E');
								M_rstRSSET.close();
								return;
							}						
						}
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"M_objSOURC == txtTOMAT");
					}
				}				
				else if(txtTOMAT.getText().trim().length() > 0)
				{
					setMSG("Invalid Material Code, Press F1 To Select Form list..",'E');					
					return;
				}				
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMMAT")
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtFMMAT.setText(cl_dat.M_strHLPSTR_pbst);
			if(txtTOMAT.getText().trim().length() == 0)
				txtTOMAT.setText(txtFMMAT.getText().trim());
			txtTOMAT.select(0,txtTOMAT.getText().trim().length());
			txtTOMAT.requestFocus();
		}
		if(M_strHLPFLD == "txtTOMAT")
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtTOMAT.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
	
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpctm.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpctm.doc";
			
			getDATA();
						
			if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				intRECCT = 0;
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Catalogue Master Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}	
	public void getDATA()		
	{
		String L_strMATCD = "";
		String L_strPRITM = "";
		String L_strITMCD = "";
		String L_strLVLNO = "";
		String L_strPRLVL = "";
		//intRECCT = 0;
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	   
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Catalogue Master Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			
			prnHEADER();			
			M_strSQLQRY = "SELECT CT_MATCD,CT_MATDS,CT_UOMCD,CT_PRTNO,CT_MATRF,CT_STSFL,";
			M_strSQLQRY +=" CTT_LVLNO,CTT_LINNO,CTT_MATDS,CTT_PRTFL FROM CO_CTMST,CO_CTTRN WHERE";
			M_strSQLQRY +=" CTT_MATCD = CT_MATCD AND CTT_CODTP = CT_CODTP";
			
			if((txtFMMAT.getText().trim().length()==10) || (txtTOMAT.getText().trim().length()==10))
			{	
				if((txtFMMAT.getText().trim().length()==10) && (txtTOMAT.getText().trim().length()==10))
				{
					if(txtFMMAT.getText().equals(txtTOMAT.getText().trim()))
						M_strSQLQRY +=" AND CT_MATCD ='"+txtFMMAT.getText().trim()+"'";
					else
						M_strSQLQRY +=" AND CT_MATCD BETWEEN '"+txtFMMAT.getText().trim()+"' AND '"+txtTOMAT.getText().trim()+"'";
				}
				else if(txtFMMAT.getText().trim().length()==10) 
					 M_strSQLQRY +=" AND CT_MATCD =>'"+txtFMMAT.getText().trim()+"'";
				else if(txtTOMAT.getText().trim().length()==10)
					 M_strSQLQRY +=" AND CT_MATCD <='"+txtTOMAT.getText().trim()+"'";
			}							
			M_strSQLQRY +=" ORDER BY CT_MATCD,CTT_LVLNO,CTT_LINNO";						
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
				{	
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);						
						prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();				
						L_strPRITM = "";
					}					
					L_strITMCD = M_rstRSSET.getString("CT_MATCD");
					L_strLVLNO = M_rstRSSET.getString("CTT_LVLNO");
					if(!L_strPRITM.equals(L_strITMCD))
					{
						if(L_strPRITM.length() > 0 )
							dosREPORT.writeBytes("\n"+strDOTLN);
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CTT_LVLNO"),""),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_PRTNO"),""),21));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATRF"),""),11));
						if(nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"").equals("9"))
							dosREPORT.writeBytes(padSTRING('R',"Y",5));
						else
							dosREPORT.writeBytes(padSTRING('R',"",5));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),50));
						//copy current material code to the temparary string varialble for matcd
						L_strPRITM = L_strITMCD;
						cl_dat.M_intLINNO_pbst += 2;
					}
					else
					{
						if(!L_strPRLVL.equals(L_strLVLNO))
						{
							if(L_strPRLVL.length() > 0 )
							{
								dosREPORT.writeBytes("\n"+strDOTLN);
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),11));
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CTT_LVLNO"),""),4));
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_PRTNO"),""),21));
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4));
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATRF"),""),11));
								if(nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"").equals("9"))
									dosREPORT.writeBytes(padSTRING('R',"Y",5));
								else
									dosREPORT.writeBytes(padSTRING('R',"",5));
								dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),50));
								cl_dat.M_intLINNO_pbst += 2;
							}
							//copy current material code to the temparary string varialble for matcd
							L_strPRLVL = L_strLVLNO;							
						}
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",56));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),""),3));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),""),44));
					if(nvlSTRVL(M_rstRSSET.getString("CTT_PRTFL"),"").equals("Y"))
						dosREPORT.writeBytes(padSTRING('L',"Y",3));
					else
						dosREPORT.writeBytes(padSTRING('L',"N",3));
					cl_dat.M_intLINNO_pbst += 1;					
				}
				dosREPORT.writeBytes("\n"+strDOTLN);
				L_strMATCD = "";
			}						
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);												
			}			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
				setMSG("For HTML Report Printing, Please insert 120 column Page..",'N');
			}	
			dosREPORT.close();
			fosREPORT.close();			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"exePRINT");
		}
	}
	/** 
	 * Method to generate the Header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{			
			cl_dat.M_PAGENO += 1;						
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LIMITED",strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Material Catalogue List ",strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
			dosREPORT.writeBytes("Iteam Code Lvl Part Number          UOM Cross      Obs. Iteam Description                            Print"+"\n");			
			dosREPORT.writeBytes("                                        Ref. Code  Tag  Line No And Description                       Flag"+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	Method to validate Data given, before executionof Query, to check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{	
			if((txtFMMAT.getText().trim().length()>0)&&(txtFMMAT.getText().trim().length()<10))
			{
				setMSG("Invalid Material Code Press F1 to select from list..",'E');
				txtFMMAT.requestFocus();
				return false;
			}
			if((txtTOMAT.getText().trim().length()>0)&&(txtTOMAT.getText().trim().length()<10))
			{
				setMSG("Invalid Material Code Press F1 to select from list..",'E');
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
}