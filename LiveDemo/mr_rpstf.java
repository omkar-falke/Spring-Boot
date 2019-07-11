/*
System Name    : Marketing System
Program Name   : Stock Transfer 
Program Desc.  : 
Author         :  Zaheer Alam Khan
Date           : 06/11/2006
Version        : v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/




import java.sql.Date;import java.sql.ResultSet;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;

/**

System Name    : Marketing System
Program Name   : Stock Transfer Report
Purpose        : The Purpose of This report is to find out the Stock Transfer from Location wise
                 Grade wise And Invoice wise as per Date Range.
                 And Also find out how much Stock have a Consignment Stockist. 

List of tables used :
Table Name              Primary key                                             Operation done
                                                            Insert   Update   Query   Delete	
-----------------------------------------------------------------------------------------------
CO_CDTRN          CMT_CGMTP,CMT_CGSTP,CMT_CODCD                                  #
CO_PTMST			   PT_PRTTP,PT_PRTCD					                     #
MR_IVTR1          IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP                        #
-----------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT   IVT_INVDT                 MR_IVTR1			Data         Invoice Date
txtTODAT   IVT_INVDT				 MR_IVTR1           Date         Invoice date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
        In this Report There are Three radioButton.
         1)Invoice Deatils
         2)Loc. Wise Summary
         3)Grade Wise Summary

    For Invoice Details Report Data is taken from MR_IVTR1 And CO_PTMST  on This condition.

		 1) PT_PRTTP ='C' ";
         2) AND PT_PRTCD = IVT_CNSCD
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0 ";
         5) AND date(IVT_INVDT) BETWEEN '"+txtFMDAT.getText()+"' AND '"+txtTODAT.getText()+"' ";
         6) AND isnull(IVT_INVNO,'') <>''
         7) AND isnull(ivt_stsfl,'') <>'X' ";
	     8) ORDER BY IVT_CNSCD,IVT_INVNO ";

    For Loction Wise Summary Data is taken from MR_IVTR1 and CO_PTMST on This condition.
               
         1) PT_PRTTP ='C'
         2) AND PT_PRTCD = IVT_CNSCD 
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0
         5) AND date(IVT_INVDT) BETWEEN txtFMDAT.getText() AND txtTODAT.getText()
         6) AND isnull(ivt_invno,'') <>''
         7) AND isnull(ivt_stsfl,'') <>'X' ";
		 8) GROUP BY IVT_DSRCD,IVT_CNSCD,PT_PRTNM,PT_DSTCD,IVT_PRDDS ";
         9) ORDER BY IVT_CNSCD,IVT_PRDDS";
   
    For Grade wise Summary Data is taken from MR_IVTR1 Amd CO_PTMST on This Condition
         1) PT_PRTTP ='C'
         2) AND PT_PRTCD = IVT_CNSCD
         3) AND IVT_SALTP ='04'
         4) AND ivt_invqt >0
         5) AND date(IVT_INVDT) BETWEEN txtFMDAT.getText() AND txtTODAT.getText()
         6) AND isnull(ivt_invno,'') <>''";
         7) AND isnull(ivt_stsfl,'') <>'X'
         8) GROUP BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
		 9) ORDER BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
   

<B>Validations & Other Information:</B>    
     -  Date Should be valid
     -  From date must  be less then To Date
</I> */


class mr_rpstf extends cl_rbase
{								
	private JRadioButton rdbDETAL;       /**RadioButton For Detail Report    */
	private JRadioButton rdbLOCTN;       /**RadioButton For Location wise Summary Report */
	private JRadioButton rdbGRADE;       /** RadioButton For Grade wise Summary Report */
	private ButtonGroup btgRPTTP;         /**ButtonGroup Variable to Add RadioButton    */
	private JTextField txtFMDAT;         /** JtextField to display & enter Date to generate the Report.*/
	private JTextField txtTODAT;         /** String Variable for date.*/
	private String strFILNM;	         /** String Variable for generated Report file Name.*/ 
	private int intRECCT;		         /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
	private Hashtable<String,String> hstDSTCD;           /** Hashtable Variable for Distination  */ 
	private Hashtable<String,String> hstDSRCD;
	private String strINVNO="";            /** String Variable for Invoice Number*/ 
	private String strPINVNO="";           /** String Variable for previous Invoice Number*/ 
	private String strINVVL="";            /** String Variable for Invoice Value*/ 
	private String strPRDDS="";            /** String Variable for Product Code*/ 
	private String strPPRDDS="";           /** String Variable for previous Product Code*/ 
	private String strCNSCD="";            /** String Variable for Consignee Code*/ 
	private String strPCNSCD="";           /** String Variable for previous Consignee Code*/ 

	private String strBASVL="";
	private String strEXCVL="";
	private String strEDCVL="";
		
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	mr_rpstf()
	{
		super(1);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			btgRPTTP=new ButtonGroup();
		
			setMatrix(20,8);
			add(rdbDETAL = new JRadioButton("Invoice Details",true),3,3,1,2,this,'L');
			add(rdbLOCTN = new JRadioButton("Loc Wise Summary"),4,3,1,2,this,'L');
			add(rdbGRADE = new JRadioButton("Grade Wise Summary"),5,3,1,2,this,'L');
			btgRPTTP.add(rdbDETAL);
			btgRPTTP.add(rdbLOCTN);
			btgRPTTP.add(rdbGRADE);
			add(new JLabel("From Date "),6,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),6,4,1,1,this,'L');
			
			add(new JLabel("To Date "),7,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),7,4,1,1,this,'L');
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);	
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDST' AND CMT_STSFL <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstDSTCD = new Hashtable<String,String>(50);
			if(M_rstRSSET !=null)
			{
			    while(M_rstRSSET.next())
			        hstDSTCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP ='G'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstDSRCD = new Hashtable<String,String>(50);
			if(M_rstRSSET !=null)
			{
			    while(M_rstRSSET.next())
			        hstDSRCD.put(M_rstRSSET.getString("PT_PRTCD"),M_rstRSSET.getString("PT_PRTNM"));
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
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
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)		
			cl_dat.M_PAGENO = 0;		
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
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
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
			
			if(txtFMDAT.getText().trim().length()==0)
			{
				setMSG("Please Enter From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length()==0)
			{
				setMSG("Please Enter To Date ..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
			{
				setMSG("To Date can not be Less than From Date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}		
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date can not be greater than today's date..",'E');
				txtTODAT.requestFocus();
				return false;
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpstf.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpstf.doc";
			
			getDATA();
			if(dosREPORT !=null)
			    dosREPORT.close();
			if(fosREPORT !=null)
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Transferr"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	
	/**
	 * Method to fetch data from Database & club it with Header & Footer.
	 */
	public void getDATA()
	{		
	    String strDSRCD = "",strCNSCD ="";
	    String strPDSRCD = "",strPCNSCD ="";
	    float L_fltLOCQT =0.0f;
	    float L_fltTOTQT =0.0f;
        float L_fltGRTOT =0.0f;
		java.sql.Date jdtTEMP;
		setCursor(cl_dat.M_curWTSTS_pbst);
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Stock Transfer</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}				
			
			prnHEADER();
			if(rdbDETAL.isSelected())
				dspDETAL();
			else if(rdbGRADE.isSelected())
				dspGRDDTL();
				
			else
			{
				M_strSQLQRY = "SELECT IVT_DSRCD,IVT_CNSCD,PT_PRTNM,PT_DSTCD,IVT_PRDDS,SUM(IVT_INVQT)STKQT ";
				M_strSQLQRY += " FROM MR_IVTR1,CO_PTMST WHERE PT_PRTTP ='C' AND PT_PRTCD = IVT_CNSCD AND ";
				M_strSQLQRY += " IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='04' and ivt_invqt >0 and isnull(ivt_invno,'') <>'' and isnull(ivt_stsfl,'') <>'X' ";
				M_strSQLQRY += " and CONVERT(varchar,IVT_INVDT,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
				M_strSQLQRY += " GROUP BY IVT_DSRCD,IVT_CNSCD,PT_PRTNM,PT_DSTCD,IVT_PRDDS ";
				M_strSQLQRY += " ORDER BY IVT_CNSCD,IVT_PRDDS";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET != null)
				{ 
					while(M_rstRSSET.next())
					{
						intRECCT=1;
						if(cl_dat.M_intLINNO_pbst >65)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes(strDOTLN);
							cl_dat.M_intLINNO_pbst = 0;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
							    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></P>");
							prnHEADER();
						}
						strDSRCD = nvlSTRVL(M_rstRSSET.getString("IVT_DSRCD"),"");
						if(!strDSRCD.equals(strPDSRCD))
						{
						    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					                prnFMTCHR(dosREPORT,M_strBOLD);
					            if(M_rdbHTML.isSelected())
					                dosREPORT.writeBytes("<B>");
						    if(strPDSRCD !="")
						    {
						        dosREPORT.writeBytes(padSTRING('L',"Total : ",70));
						        dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltLOCQT,3),12));
						        dosREPORT.writeBytes("\n");
						        cl_dat.M_intLINNO_pbst += 1;
						        dosREPORT.writeBytes(padSTRING('L',"Total : ",70));
						        dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTOTQT,3),12));
						        dosREPORT.writeBytes("\n");
				    		    cl_dat.M_intLINNO_pbst += 1;
						    }
						  	//dosREPORT.writeBytes(hstDSRCD.get(strDSRCD).toString());    
						  	if(hstDSRCD.containsKey((String)strDSRCD))
						  	    dosREPORT.writeBytes(hstDSRCD.get(strDSRCD).toString());    
						  	else    
						  	    dosREPORT.writeBytes(strDSRCD);    
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					                prnFMTCHR(dosREPORT,M_strNOBOLD);
					        if(M_rdbHTML.isSelected())
					              dosREPORT.writeBytes("</B>");
				    		cl_dat.M_intLINNO_pbst += 1;
				    		strPDSRCD = strDSRCD;
				    		strPCNSCD = "";
				    		L_fltTOTQT =0.0f;
				    		L_fltLOCQT =0.0f;
						}
						strCNSCD = nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
						if(!strCNSCD.equals(strPCNSCD))
						{
						    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					                prnFMTCHR(dosREPORT,M_strBOLD);
					         if(M_rdbHTML.isSelected())
					                dosREPORT.writeBytes("<B>");        
						    if(strPCNSCD !="")
						    {
						        dosREPORT.writeBytes(padSTRING('L',"Total : ",70));
						        dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltLOCQT,3),12));
						        dosREPORT.writeBytes("\n");
						        cl_dat.M_intLINNO_pbst += 1;
						    }
						    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),40));	//DMR Number
						   // dosREPORT.writeBytes(padSTRING('R',hstDSTCD.get(nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")).toString(),15));	//DMR Number
						   if(hstDSTCD.containsKey((String)nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")))
						        dosREPORT.writeBytes(padSTRING('R',hstDSTCD.get(nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")).toString(),15));	//DMR Number
						   else
						        dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_DSTCD"),15));
       						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					                prnFMTCHR(dosREPORT,M_strNOBOLD);
					         if(M_rdbHTML.isSelected())
					                dosREPORT.writeBytes("</B>");        
       						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),15));	//Party Code and Description
    						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STKQT"),""),12));	//Chalan No
    						dosREPORT.writeBytes(padSTRING('L',"",10));	//Chalan No
    						dosREPORT.writeBytes(padSTRING('L',"",10));	//Chalan No
							dosREPORT.writeBytes("\n");
				    		cl_dat.M_intLINNO_pbst += 1;
				    		strPCNSCD = strCNSCD;
				    		L_fltLOCQT =0.0f;
						}
						else
						{  
						    dosREPORT.writeBytes(padSTRING('R',"",55));
    						//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),25));	//DMR Number
    						//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),""),5));	//DMR Number
    						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),15));	//Party Code and Description
    						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STKQT"),""),12));	//Chalan No
    						dosREPORT.writeBytes(padSTRING('L',"",10));	//Chalan No
    						dosREPORT.writeBytes(padSTRING('L',"",10));	//Chalan No
    						dosREPORT.writeBytes("\n");
    						cl_dat.M_intLINNO_pbst += 1;
						}
						L_fltLOCQT += M_rstRSSET.getFloat("STKQT");
						L_fltTOTQT += M_rstRSSET.getFloat("STKQT");
				        L_fltGRTOT += M_rstRSSET.getFloat("STKQT");
					}
				}
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				    prnFMTCHR(dosREPORT,M_strBOLD);
				 if(M_rdbHTML.isSelected())
					 dosREPORT.writeBytes("<B>");    
				dosREPORT.writeBytes(padSTRING('L',"Total : ",70));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltLOCQT,3),12));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
   				dosREPORT.writeBytes(padSTRING('L',"Total : ",70));
   				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTOTQT,3),12));
				dosREPORT.writeBytes("\n");
	       
				cl_dat.M_intLINNO_pbst += 1;
           		dosREPORT.writeBytes(strDOTLN+"\n");		
				dosREPORT.writeBytes(padSTRING('L',"Grand Total : ",70));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltGRTOT,3),12));
				dosREPORT.writeBytes("\n");
			
			}
		    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    prnFMTCHR(dosREPORT,M_strNOBOLD);
			 if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");    
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
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	 * Function For detail Report
	 */
	private void dspDETAL()
	{
		int L_intRECCT=0;
		int L_intCNSCT=0;
		double L_dblGRTOT=0.0;
		
		double L_dblTOTAL=0.0;
		double L_dblTOTQT=0.0;
		double L_dblASSVL=0.0;
		double L_dblEXCVL=0.0;
		double L_dblEDCVL=0.0;

		double L_dblGRTOTAL=0.0;
		double L_dblGRASSVL=0.0;
		double L_dblGREXCVL=0.0;
		double L_dblGREDCVL=0.0;
		
		try
		{
			M_strSQLQRY = "SELECT IVT_CNSCD,IVT_INVNO,PT_PRTNM,PT_DSTCD,IVT_INVDT,IVT_PRDDS,IVT_INVQT,IVT_INVRT,IVT_ASSVL,IVT_EXCVL, ";
			M_strSQLQRY+= " isnull(IVT_EDCVL,0)+isnull(IVT_EHCVL,0) IVT_EDHVL,IVT_NETVL,IVT_INVVL FROM MR_IVTR1,CO_PTMST WHERE PT_PRTTP ='C' ";
			M_strSQLQRY +=" AND PT_PRTCD = IVT_CNSCD AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='04' and ivt_invqt >0 ";
			M_strSQLQRY +=" and isnull(IVT_INVNO,'') <>'' and isnull(ivt_stsfl,'') <>'X' ";
			M_strSQLQRY += "and CONVERT(varchar,IVT_INVDT,101) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
			M_strSQLQRY += " ORDER BY IVT_CNSCD,IVT_INVNO ";
			
		/*
			M_strSQLQRY = "SELECT IVT_INVNO,IVT_INVDT,IVT_PRDDS,IVT_INVQT,IVT_INVRT,IVT_ASSVL,IVT_EXCVL, ";
			M_strSQLQRY+= "IVT_EDCVL,IVT_NETVL,IVT_INVVL FROM MR_IVTR1 WHERE  ";
			M_strSQLQRY +=" IVT_SALTP ='04' and ivt_invqt >0 ";
			M_strSQLQRY +=" and isnull(IVT_INVNO,'') <>'' and isnull(ivt_stsfl,'') <>'X' ";
			M_strSQLQRY += "and date(IVT_INVDT) BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ";
			M_strSQLQRY += " ORDER BY IVT_INVNO ";
		*/
			//System.out.println(" M_strSQLQRY"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{ 
				while(M_rstRSSET.next())
				{
					
					if(cl_dat.M_intLINNO_pbst >65)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);
						cl_dat.M_intLINNO_pbst = 0;
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
						    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></P>");
						prnHEADER();
					}
					strINVNO=nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"");
					strCNSCD=nvlSTRVL(M_rstRSSET.getString("IVT_CNSCD"),"");
					if(!strCNSCD.equals(strPCNSCD))
					{
						if(!strINVNO.equals(strPINVNO))
						{
							if(L_intRECCT>1)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");   
								dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+setNumberFormat(Double.parseDouble(strINVVL),0),97));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst +=1;
								
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
								
							}
							cl_dat.M_intLINNO_pbst +=1;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");   
							if(L_intCNSCT>1)
							{
								dosREPORT.writeBytes(padSTRING('L'," Total : "+setNumberFormat(L_dblTOTQT,3),44));
								dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblASSVL,0),22));
								dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblEXCVL,0),10));
								dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblEDCVL,0),9));
								dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblTOTAL,0),12));
								dosREPORT.writeBytes("\n\n");
								cl_dat.M_intLINNO_pbst +=2;
								L_dblGRTOTAL +=L_dblTOTAL;
								L_dblTOTAL=0.0;
								L_dblASSVL=0.0;
								L_dblEXCVL=0.0;
								L_dblEDCVL=0.0;
								L_dblTOTQT =0;
								L_intCNSCT=0;

							}
							
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"("+strCNSCD+")",46));
							if(hstDSTCD.containsKey((String)nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")))
								dosREPORT.writeBytes(padSTRING('R',"("+hstDSTCD.get(nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")).toString()+")",15));	//DMR Number
							else
								dosREPORT.writeBytes(padSTRING('R',"("+M_rstRSSET.getString("PT_DSTCD")+")",15));
							
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst +=1;
							L_intRECCT=0;
							dosREPORT.writeBytes(padSTRING('R',strINVNO,10));
							strINVVL=nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"");
							strBASVL=nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),"");
							L_dblASSVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),"0"));
							L_dblEXCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EXCVL"),"0"));
							L_dblEDCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EDHVL"),"0"));
							L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
							L_dblTOTQT+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));

							strPCNSCD=strCNSCD;
						}
					}
					else
					{
						if(!strINVNO.equals(strPINVNO))
						{
							if(L_intRECCT>1)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<B>");   
								
								dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+setNumberFormat(Double.parseDouble(strINVVL),0),97));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst +=1;
								
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strNOBOLD);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("</B>");   
								
							}
						//	dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+setNumberFormat(Double.parseDouble(strINVVL),0),97));
						//	dosREPORT.writeBytes("\n");
						//	cl_dat.M_intLINNO_pbst +=1;
							L_intRECCT=0;
							dosREPORT.writeBytes(padSTRING('R',strINVNO,10));
							strINVVL=nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"");
						
							L_dblASSVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),"0"));
							L_dblEXCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EXCVL"),"0"));
							L_dblEDCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EDHVL"),"0"));
							L_dblTOTQT+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
							L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
						}
						else
						{
							dosREPORT.writeBytes(padSTRING('R',"",10));
							//// check it
							L_dblASSVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),"0"));
							L_dblEXCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EXCVL"),"0"));
							L_dblEDCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EDHVL"),"0"));
							///L_dblTOTAL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVVL"),"0"));
							L_dblTOTQT+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
						}
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IVT_INVDT")).toString(),""),11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),16));
					//dosREPORT.writeBytes(padSTRING('R',"",2));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),""),7));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_INVRT"),0),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_ASSVL"),0),12));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_EXCVL"),0),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_EDHVL"),0),9));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(M_rstRSSET.getDouble("IVT_NETVL"),0),12));
					
					L_dblGRTOT+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"0"));
					L_dblGRASSVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_ASSVL"),"0"));
					L_dblGREXCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EXCVL"),"0"));
					L_dblGREDCVL+=Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("IVT_EDHVL"),"0"));
						
					intRECCT=1;
					L_intRECCT++;
					L_intCNSCT++;
					strPINVNO=strINVNO;
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");   
					
				if(intRECCT>1)
				{
					dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+setNumberFormat(Double.parseDouble(strINVVL),0),97));
					//dosREPORT.writeBytes(padSTRING('L'," Invoice Value : "+strINVVL,97));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
				}
				if(L_intCNSCT>1)
				{
					dosREPORT.writeBytes(padSTRING('L'," Total : "+setNumberFormat(L_dblTOTQT,3),44));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblASSVL,0),22));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblEXCVL,0),10));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblEDCVL,0),9));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblTOTAL,0),12));

					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
					L_dblGRTOTAL +=L_dblTOTAL;				
					L_dblTOTAL=0.0;
					L_dblASSVL=0.0;
					L_dblEXCVL=0.0;
					L_dblEDCVL=0.0;
					L_dblTOTQT=0.0;

				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
//				dosREPORT.writeBytes(padSTRING('L'," Grand Total : "+setNumberFormat(L_dblGRTOT,3),44));
					dosREPORT.writeBytes(padSTRING('L'," Grand Total : "+setNumberFormat(L_dblGRTOT,3),44));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblGRASSVL,0),22));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblGREXCVL,0),10));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblGREDCVL,0),9));
					dosREPORT.writeBytes(padSTRING('L'," "+setNumberFormat(L_dblGRTOTAL,0),12));

				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");  
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspDETAL");
		}
	}
	/**
	 * Function For Grade Wise summary Report
	 */
	private void dspGRDDTL()
	{
		String strDSRCD = "",strCNSCD ="";
	    String strPDSRCD = "",strPCNSCD ="";
	    strPRDDS="";
	    strPPRDDS="";
		
		//float L_fltLOCQT =0.0f;
	    float L_fltTOTQT =0.0f;
        float L_fltGRTOT =0.0f;
		try
		{
			M_strSQLQRY = "SELECT IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD,SUM(IVT_INVQT)STKQT";
			M_strSQLQRY += " FROM MR_IVTR1,CO_PTMST WHERE PT_PRTTP ='C' AND PT_PRTCD = IVT_CNSCD AND ";
			M_strSQLQRY += " IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP ='04' and ivt_invqt >0 and isnull(ivt_invno,'') <>''";
			M_strSQLQRY += " and isnull(ivt_stsfl,'') <>'X' and CONVERT(varchar,IVT_INVDT,101) BETWEEN ";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"'";
			M_strSQLQRY += " GROUP BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
			M_strSQLQRY += " ORDER BY IVT_PRDCD,IVT_PRDDS,IVT_CNSCD,PT_PRTNM,PT_DSTCD";
			//System.out.println("GRade Details "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{ 
				while(M_rstRSSET.next())
				{
					intRECCT=1;
					if(cl_dat.M_intLINNO_pbst >65)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(strDOTLN);
						cl_dat.M_intLINNO_pbst = 0;
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
						    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></P>");
						prnHEADER();
					}
					
					strPRDDS=nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),"");
					if(!strPRDDS.equals(strPPRDDS))
					{
						if(!strPPRDDS.equals(""))
						{
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");    
							dosREPORT.writeBytes(padSTRING('L',"Total : ",80));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTOTQT,3),10));
							L_fltTOTQT=0;
							dosREPORT.writeBytes("\n\n");	
							 cl_dat.M_intLINNO_pbst += 2;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");    

						}
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),20));
						strPPRDDS=strPRDDS;
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",20));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),40));
					if(hstDSTCD.containsKey((String)nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")))
						dosREPORT.writeBytes(padSTRING('R',hstDSTCD.get(nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),"")).toString(),15));	//DMR Number
				    else
						dosREPORT.writeBytes(padSTRING('R',M_rstRSSET.getString("PT_DSTCD"),15));
       						
					//dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_DSTCD"),""),15));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("STKQT"),""),15));
					dosREPORT.writeBytes("\n");
					 cl_dat.M_intLINNO_pbst += 1;
					L_fltGRTOT += M_rstRSSET.getFloat("STKQT");
					L_fltTOTQT += M_rstRSSET.getFloat("STKQT");
					
				}
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    prnFMTCHR(dosREPORT,M_strBOLD);
			 if(M_rdbHTML.isSelected())
				 dosREPORT.writeBytes("<B>");    
			dosREPORT.writeBytes(padSTRING('L',"Total : ",80));
   			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTOTQT,3),10));
			dosREPORT.writeBytes("\n");
	       
			cl_dat.M_intLINNO_pbst += 1;
           	dosREPORT.writeBytes(strDOTLN+"\n");		
			dosREPORT.writeBytes(padSTRING('L',"Grand Total : ",80));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltGRTOT,3),10));
			
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspGRDDTL");
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
			 if(M_rdbHTML.isSelected())
				 dosREPORT.writeBytes("<B>");	
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			//dosREPORT.writeBytes(padSTRING('R',"Stock Details on Consignment Stockist as on  : "+cl_dat.M_strLOGDT_pbst,strDOTLN.length()-21));
			if(rdbDETAL.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"Stock Transfer Details from "+txtFMDAT.getText()+" To "+txtTODAT.getText(),strDOTLN.length()-21));
			else
				dosREPORT.writeBytes(padSTRING('R',"Stock Transfer Summary from "+txtFMDAT.getText()+" To "+txtTODAT.getText(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			 if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");	
			dosREPORT.writeBytes(strDOTLN +"\n");
			if(rdbDETAL.isSelected())
			{
				dosREPORT.writeBytes("Consignment Stockist                          Location \n");
				dosREPORT.writeBytes("Inv.No    Inv. Date  Grade Name     Inv Qty.   Rate/MT   Basic Val    Excise     CESS  Tot Amount");
				cl_dat.M_intLINNO_pbst = 1;
			}
			//dosREPORT.writeBytes("Consignee                          Destination    Grade           Stock Trf.      Sale     Stock ");
			else if(rdbGRADE.isSelected())
			{
				dosREPORT.writeBytes("Grade               Consignment Stockist                    Location              Trf. Qty ");
			}
			else
				dosREPORT.writeBytes("Consignment Stockist                    Location       Grade              Trf. Qty      ");
			//dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"getRPHDT ");
		}
	}
	
	
}