/*
System Name   : Material Management System
Program Name  : List Of Daily Material received
Program Desc. : 
Author        : 
Date          : 07/10/2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :10/03/06
Modified det.  :
Version        :
*/

import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;


/**<pre>
System Name : Material Management System.
 
Program Name : List Of Pending GRIN

Purpose : This program generates Report for List Of Pending GRIN for given date Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_WBTRN       WB_DOCTP,WB_DOCNO,WB_SRLNO                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    WB_GINDT                MM_WBTRN           Date          Gate-In Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   For List Of Pending DMR Report Data is taken from MM_WBTRN for condiations :- 
        1) ifnull(WB_DOCRF,' ') = ' ' 
        2) AND ifnull(WB_DOCTP,' ') = '02' 
        3) AND WB_GINDT IS NOT null 
        4) AND ifnull(WB_STSFL,'') <> 'X'

For Daily Material List Report Data id taken from MM_WBTRN for :-
        1) WB_DOCTP = '02' 
        2) AND date(WB_GINDT) =is between the Form Date to ToDate
        3) AND ifnull(WB_STSFL,'') <> 'X'

<B>Validations & Other Information:</B>    
    - For Daily Material List Gate in Date Entered must be valid.
</I> */

class mm_rpdmr extends cl_rbase
{								
	private JComboBox cmbRPTOP;	   /** JComboBox to display & select Report.*/
	private JLabel lblFMDAT;       /** JLabel to display message on the Screen.*/
	private JLabel lblTODAT;       /**JLabel to display message on the Screen.*/
	private JTextField txtFMDAT;   /** JtextField to display & enter Date to generate the Report.*/
	private JTextField txtTODAT;   /** String Variable for date.*/
	private String strFMDAT;		/** String Variable for date.*/ 
	private String strTODAT;		/** String Variable for date.*/ 
	private String strISOD1="";  	/** String Variable for ISO DOC Number.*/
	private String strISOD2="";	    /** String Variable for ISO DOC Number.*/
	private String strISOD3="";     /** String Variable for ISO DOC Number.*/ 	
	private String strFILNM;	    /** String Variable for generated Report file Name.*/ 
	private int intRECCT;		    /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	mm_rpdmr()
	{
		super(2);
		try
		{
			cmbRPTOP = new JComboBox();
			cmbRPTOP.addItem("Daily Material List");
			cmbRPTOP.addItem("List Of Pending DMR");
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		
			setMatrix(20,8);
			add(new JLabel("Report Type"),4,3,1,1,this,'L');
			add(cmbRPTOP,4,4,1,1.5,this,'L');
			add(lblFMDAT = new JLabel("From Date "),5,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),5,4,1,1.5,this,'L');
			
			add(lblTODAT = new JLabel("To Date "),6,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),6,4,1,1.5,this,'L');
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
				+ " AND CMT_CGSTP = 'MMXXRPT' AND isnull(CMT_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(M_rstRSSET != null)
			{
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strTEMP.equals(""))
					{
						if(L_strTEMP.equals("MM_RPDMR01"))
							strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPDMR02"))
							strISOD2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPDMR03"))
							strISOD3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					}					
				}
				M_rstRSSET.close();
			}		
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
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
				txtFMDAT.requestFocus();
				setMSG("Please Enter Date to generate the Report..",'N');
			}
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		else if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
			{
				cmbRPTOP.requestFocus();
				setMSG("Select Report Option..",'N');
			}
		}
		else if(M_objSOURC == cmbRPTOP)
		{
			if(cmbRPTOP.getSelectedIndex() == 1)
			{
				lblFMDAT.setVisible(false);
				txtFMDAT.setVisible(false);
				lblTODAT.setVisible(false);
				txtTODAT.setVisible(false);
			}
			else
			{
				lblFMDAT.setVisible(true);
				txtFMDAT.setVisible(true);
				lblTODAT.setVisible(true);
				txtTODAT.setVisible(true);
				txtFMDAT.requestFocus();
				txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
				txtFMDAT.select(0,txtFMDAT.getText().length());
			}
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)		
			cl_dat.M_PAGENO = 0;		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbRPTOP)
			{
				txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
				txtFMDAT.select(0,txtFMDAT.getText().length());
				if(cmbRPTOP.getSelectedIndex() == 0)
					txtFMDAT.requestFocus();
				else
					cl_dat.M_btnSAVE_pbst.requestFocus();
					
			}
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					txtTODAT.requestFocus();
				else
				{
					txtFMDAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			
			if(M_objSOURC == txtTODAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
					
				else
				{
					txtFMDAT.requestFocus();
					setMSG("Enter Date",'N');
				}
			}
			
			
			setMSG("",'N');
		}
	}
	/**
	 * Method to fetch data from Database & club it with Header & Footer.
	 */
	public void getDATA()
	{		
		java.sql.Date jdtTEMP;
		setCursor(cl_dat.M_curWTSTS_pbst);
		String L_strFMDAT = txtFMDAT.getText().trim();
       	strFMDAT = L_strFMDAT.substring(6,10);
		strFMDAT += "-";
		strFMDAT += L_strFMDAT.substring(3,5);
		strFMDAT += "-";
		strFMDAT += L_strFMDAT.substring(0,2);	
			
		String L_strTODAT = txtTODAT.getText().trim();
		strTODAT = L_strTODAT.substring(6,10);
		strTODAT += "-";
		strTODAT += L_strTODAT.substring(3,5);
		strTODAT += "-";
		strTODAT += L_strTODAT.substring(0,2);	
		
		try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>"+ cmbRPTOP.getSelectedItem().toString()+"</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}				
			
			prnHEADER();
			
			if(cmbRPTOP.getSelectedIndex() == 0)
			
			M_strSQLQRY = "SELECT WB_DOCNO,WB_GINDT,WB_CHLNO,WB_CHLDT,WB_PRTCD,WB_PRTDS,WB_MATDS,WB_PORNO,WB_REMGT "
					+"FROM MM_WBTRN WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '02' AND CONVERT(varchar,WB_GINDT,101) BETWEEN '"+strFMDAT+"' "
					+"AND '"+strTODAT+"' "+"AND isnull(WB_STSFL,'') <> 'X' ORDER BY WB_DOCNO";
			
			else
				M_strSQLQRY = "SELECT WB_DOCNO,WB_GINDT,WB_CHLNO,WB_CHLDT,WB_PRTCD,WB_PRTDS,"
					+"WB_MATDS,WB_REMGT,WB_CHLQT,(days(CURRENT_DATE) - days(WB_GINDT)) WB_PENDY "
					+"FROM MM_WBTRN WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(WB_DOCRF,' ') = ' ' AND isnull(WB_DOCTP,' ') = '02' AND "
					+"WB_GINDT IS NOT null AND isnull(WB_STSFL,'') <> 'X' ORDER BY WB_DOCNO";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{ 
				while(M_rstRSSET.next())
				{
					intRECCT=1;
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					if(cmbRPTOP.getSelectedIndex() == 0)
					{
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),""),12));	//DMR Number
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),""),12));	//Chalan No
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_PRTCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),""),48));	//Party Code and Description
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_PORNO"),""),24));	//P.O. Number
					}
					else
					{
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),""),12));	//DMR Number
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_PRTCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),""),62));	//Party Code and Description
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),""),12));	//Chalan No
					}
					dosREPORT.writeBytes("\n");
					//DMR Date
					jdtTEMP = M_rstRSSET.getDate("WB_GINDT");
					if(jdtTEMP != null)
						dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(jdtTEMP),12));
					else
						dosREPORT.writeBytes(padSTRING('R',"",12));
					
					if(cmbRPTOP.getSelectedIndex() == 0)
					{						
						jdtTEMP = M_rstRSSET.getDate("WB_CHLDT");//Chalan Date
						if(jdtTEMP != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(jdtTEMP),12));
						else
							dosREPORT.writeBytes(padSTRING('R',"",12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),""),48));	//Material Description
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_REMGT"),""),24));	//P.O. Quantity
					}
					else
					{
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),""),51));	//Material Description
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),""),11));
						jdtTEMP = M_rstRSSET.getDate("WB_CHLDT");
						if(jdtTEMP != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(jdtTEMP),11));
						else
							dosREPORT.writeBytes(padSTRING('R',"",11));
						
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("WB_PENDY"),""),11));	//Pending Days
					}
					dosREPORT.writeBytes("\n\n");
					cl_dat.M_intLINNO_pbst += 3;
				}
			}
			dosREPORT.writeBytes(strDOTLN);		
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			    fosREPORT.close();
			    dosREPORT.close();
			    setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to generate the header of the Report.
	 */
	void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO ++;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("\n");
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length()-1)+"\n");				
				dosREPORT.writeBytes(padSTRING('L',strISOD1,strDOTLN.length()-1)+"\n");				
				dosREPORT.writeBytes(padSTRING('L',strISOD2,strDOTLN.length()-1)+"\n");				
				dosREPORT.writeBytes(padSTRING('L',strISOD3,strDOTLN.length()-1)+"\n");				
				dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length()-1)+"\n");				
			}
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			if(cmbRPTOP.getSelectedIndex() == 0)
				dosREPORT.writeBytes(padSTRING('R',"Daily Materail Receipt Report As On : "+txtFMDAT.getText(),strDOTLN.length()-21));
			else
				dosREPORT.writeBytes(padSTRING('R',"List Of Pending DMR For GRIN",strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(strDOTLN +"\n");			
			if(cmbRPTOP.getSelectedIndex() == 0)
				dosREPORT.writeBytes("DMR No      Chl No      Vender Code And Name                            P.O. No.   ");
			else
				dosREPORT.writeBytes("DMR No      Vender Code And Name                                          Challan No     Pending");
			dosREPORT.writeBytes("\n");
			if(cmbRPTOP.getSelectedIndex() == 0)
				dosREPORT.writeBytes("DMR Date    Chl Date    Material Description                            Quantity   ");
			else
				dosREPORT.writeBytes("DMR Date    Material Description                               Quantity   Challan Dt.       Days");			
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 12;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(cmbRPTOP.getSelectedIndex() == 0)
			{
				if(txtFMDAT.getText().trim().length() != 10)
				{
					setMSG("Enter the DMR Date..",'E');
					return false;
				}
				
				if(txtTODAT.getText().trim().length() != 10)
				{
					setMSG("Enter the Proper DMR Date..",'E');
					return false;
				}
				
				if(txtFMDAT.getText().trim().length()==10 && txtTODAT.getText().trim().length()==10)
				{
					//System.out.println("in Second IF");
					setMSG(" ",'N');			
					
					if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
					{
						setMSG("From Date can not be greater than TO Date's date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
				}
				
				
					if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{
						setMSG("Date can not be greater than today's date..",'E');
						txtTODAT.requestFocus();
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	/**
	 * Method to generate the Report & send to the selected Destination.
	 */
	public void exePRINT()
	{
		//System.out.println("IN Print");
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rpdmr.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpdmr.doc";
			
			getDATA();
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,cmbRPTOP.getSelectedItem().toString()," ");				
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
}