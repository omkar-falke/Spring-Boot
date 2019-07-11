/*
System Name   : Material Management System
Program Name  : WeighBridge Registers
Program Desc. :
Author        : Mr. S.R.Mehesare
Date          : 02 Apr 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import javax.swing.JComboBox;import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.DataOutputStream;import java.io.FileOutputStream;
/**
 * <pre><b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Weigh Bridge Details

<b>Purpose :</b>This module prints the weighment details between given date-times.

List of tables used :
Table Name   Primary key                          Operation done
                                             Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
MM_WBTRN     WB_DOCTP,WB_DOCNO,WB_SRLNO                          #
MR_IVTRN     IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP             #
CO_CDTRN     CMT_CGMTP,CMT_CGSTP,CMT_CODCD                       #		   
CO_CTMST     CT_MATCD                                            #
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name     Table name    Type/Size       Description
--------------------------------------------------------------------------------
cmbGINTP    WB_DOCTP        MM_WBTRN      Varchar(2)      Gate-In Type
txtFRMDT    WB_OUTTM        MM_WBTRN      Timestamp       From Date
txtTORDT    WB_OUTTM        MM_WBTRN      Timestamp       To Date
--------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b>1)MM_WBTRN                        
                       2)MR_IVTRN
                       3)CO_CDTRN                                               
                       4)CO_CTMST
<B>Conditions Give in Query:</B>
  - Gate-In Code & Description is take from CO_CDTRN for condition
         CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'
  
  - For Despatch Data is taken from MM_WBTRN, MR_IVTRN and CO_CDTRN for following condition
     1)WB_DOCTP = Selected Gate-In Type("03Despatch")
     2)and WB_OUTTM between given Date-Time range
     3)and IVT_GINNO = WB_DOCNO
     4)and CMT_CGMTP = 'SYS' 
     5)and CMT_CGSTP = 'FGXXPKG'
     6)and CMT_CODCD = IVT_PKGTP
    Group by 
      WB_WBRNO, WB_MATDS, WB_DOCNO, WB_LRYNO, WB_TPRDS, 
      WB_INCTM, WB_OUTTM, WB_PRTDS, WB_NETWT, WB_REMWB

  - For Tanker Data is taken from MM_WBTRN & CO_CDTRN for following condition
     1)WB_DOCTP = Selected Gate-In Type ("01Tanker")
     2)and WB_OUTTM between given Date-Time range.
     3)and CMT_CGMTP = 'SYS' 
     4)and CMT_CGSTP = 'COXXDST'
     5)and CMT_CODCD = WB_LOCCD

  - For Others, Stores & Spares Data is taken from MM_WBTRN for following condition
     1)WB_DOCTP = Selected Gate-In Type ("02Strores & Spares" OR "04Others")
     2)and WB_OUTTM between given Date-Time range.
  
<b>Validations :</b>
	- Gate-In types are identified as follows
		01 - Tanker
		02 - Stores & Spares
		03 - Despatch
		04 - Others
	- Dates should not be greater than current Date.	
	- To Date must be greater than For Date.
 */

class mm_rpwrg extends cl_rbase
{										/** JTextField to insert From Date.*/
	private JTextField txtFMDAT;		/** JTextField to insert To Date.*/
	private JTextField txtTODAT;		/** JTextField to insert From Time.*/
    private JTextField txtFMTIM;		/** JTextField to insert To Time.*/
    private JTextField txtTOTIM;		/** JComboBox to select Gate-In Type.*/
    private JComboBox cmbGINTP;			/** String variable for generate Report File Name.*/
    private String strFILNM;			
	private int intRECCT;				     	
	private FileOutputStream fosREPORT;
    private DataOutputStream dosREPORT;		
	final String strDESPT_fn = "03";
	mm_rpwrg()
	{
		super(2); 
		try
		{				
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Gate-in Type"),4,3,1,1,this,'L');
			add(cmbGINTP=new JComboBox(),4,4,1,1.5,this,'L');			
		    add(new JLabel("From Date"),5,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),5,4,1,1,this,'L');
			add(txtFMTIM = new TxtTime(),5,5,1,.7,this,'L');
			add(new JLabel("To Date"),6,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),6,4,1,1,this,'L');
			add(txtTOTIM = new TxtTime(),6,5,1,.7,this,'L');						
			M_pnlRPFMT.setVisible(true);								    
		    M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			cmbGINTP.addItem("Select Gate-In Type");
            if(M_rstRSSET != null)    			        		
		    {		       
        	    while(M_rstRSSET.next())
        	    {        		         
        		     cmbGINTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));        			    
        	    }
        	    M_rstRSSET.close();
            }        			   
			setENBL(false);	  
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
    }
	/**
     * Super class method overrided to inhance its funcationality to set Components enable & 
     * disable, according to requriement.
     */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtTODAT.setEnabled(L_flgSTAT);
		txtTOTIM.setEnabled(L_flgSTAT);
		txtFMDAT.setEnabled(L_flgSTAT);
		txtFMTIM.setEnabled(L_flgSTAT);
		if (L_flgSTAT==true)
		{
			 if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
			 {			                
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);															
				txtFMDAT.setText(calFMDAT(cl_dat.M_strLOGDT_pbst));
				txtFMTIM.setText("07:00");
				txtTOTIM.setText("07:00");	
			 }
		}
		else 
		{			
			if ((cmbGINTP.getSelectedIndex()==0) || (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0))
			{
				txtTODAT.setText("");
				txtFMDAT.setText("");			    
				txtTOTIM.setText("");			    
				txtFMTIM.setText("");
			}
		}
	}				
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);	
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{					
			if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)					    
				setENBL(false);	
			else 
				setENBL(true);			    
		}    
		if(M_objSOURC == cmbGINTP)
		{
		    if (cmbGINTP.getSelectedIndex()!=0)   
		    {
		       setENBL(true);		       		      
			   txtFMDAT.requestFocus();
		    }
		    else
		    {		        
				setENBL(false);	
				cmbGINTP.setEnabled(true);  
			    M_cmbDESTN.setEnabled(true); 			   			    
			    M_pnlRPFMT.setEnabled(true);
			    M_rdbHTML.setEnabled(true);
			    M_rdbTEXT.setEnabled(true);
		    }			
		}
		if(M_objSOURC == txtFMDAT)
			txtFMTIM.requestFocus();
		if(M_objSOURC == txtFMTIM)
			txtTODAT.requestFocus();
		if(M_objSOURC == txtTODAT)
			txtTOTIM.requestFocus();
		if(M_objSOURC == txtTOTIM)
			cl_dat.M_btnSAVE_pbst.requestFocus();
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
			cl_dat.M_PAGENO = 0;
	}	
	/**
	Method to print, display report as per selection.
	*/
	void exePRINT()
	{
		if(vldDATA())
		{
			try
			{
			    if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpwrg.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpwrg.doc";				
				getDATA();				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT >0)
					{
					    if (M_rdbTEXT.isSelected())
					        doPRINT(strFILNM);
					    else 
                        {    
		                    Runtime r = Runtime.getRuntime();
						    Process p = null;								
					        p  = r.exec("C:\\windows\\iexplore.exe "+strFILNM); 
					        setMSG("For Printing, Select File Menu, then Print  ..",'N');
					    }    
					}
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				     Runtime r = Runtime.getRuntime();
					 Process p = null;										 
					 if(intRECCT >0)
					 {						
					     if(M_rdbHTML.isSelected())
					        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
					     else
    					    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
	    			  }
 		    		  else
						  setMSG("No data found, Please check the Inputs given ..",'E');				    
				}				
				else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(M_strEMLNM))
				{
		    		if(intRECCT == 0)
					   return;					
					String L_strTEMP = (String.valueOf(cmbGINTP.getSelectedItem()).toString()).substring(2,(String.valueOf(cmbGINTP.getSelectedItem()).toString()).length());									   
					cl_eml ocl_eml = new cl_eml();				    
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Weigh Bridge Entry Details for "+L_strTEMP," ");
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
	/**
	Method to validate data inserted, before execution of query,To check for blank input, wrong 
	Date & time.
	*/
	boolean vldDATA()
	{
		try
		{	
			if(cmbGINTP.getSelectedIndex() == 0)			
			{
				setMSG("Please Select Gate-in type .. ",'E');
				cmbGINTP.requestFocus();
				return false;
			}	
			else if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Spacify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter valid To-Date To Spacify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}		
			if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				txtTODAT.requestFocus();
				return false;
			}
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("Please Note that From-Date must be greater than To-Date .. ",'E');								
				txtFMDAT.setText(calFMDAT(cl_dat.M_strLOGDT_pbst));            
				txtFMDAT.requestFocus();
				return false;
			}				
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getSelectedIndex() == 0)
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
			return false;
		}
	}	
	/**
	Method to fetch Data from tables MM_WBTRN, MR_IVTRN and CO_CDTRN for given Date-Time 
	range & selected Gate-In Type.
	*/
	private void getDATA()
	{ 	    
		int L_intSRLNO = 0;
		String L_strFMDAT,L_strTODAT;
		cl_dat.M_intLINNO_pbst = 0;
		final String L_strRAWMT = "01";		
		String L_strGINNO,L_strLRYNO,L_strTPRDS,L_strINCTM;
		String L_strOUTTM,L_strPRTDS,L_strCHLQT,L_strNETWT;
		String L_strDIFQT,L_strREMWB,L_strBAGWT;
		String L_strWBRNO,L_strOWBRNO,L_strMATDS,L_strOMATDS;
		float L_fltTCHLQT,L_fltTNETWT,L_fltTDIFQT,L_fltTBAGWT;		
		String L_strDESTN=M_txtDESTN.getText().trim();		
		intRECCT = 0;		
		StringBuffer L_stbPRNSTR;
		L_stbPRNSTR = new StringBuffer("");
		String L_strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
	    try
	    {	        
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Vechicle Entry Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
    		prnHEADER();	        		    		
			L_strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim()+" " + txtFMTIM.getText().trim()));
			L_strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim() +" "+ txtTOTIM.getText().trim()));				
			setMSG("Report Generation is in Progress.......",'N');			   								
			if(L_strGINTP.equals(strDESPT_fn))	// Despatch (by decucting Weight of Empty Bags)
			{
				M_strSQLQRY = "Select WB_WBRNO,WB_MATDS,WB_DOCNO,WB_LRYNO,WB_TPRDS,WB_INCTM,WB_OUTTM,";
				M_strSQLQRY += "WB_PRTDS L_strPRTDS,WB_NETWT,WB_REMWB,sum(IVT_LADQT) L_strCHLQT,";
				M_strSQLQRY += "isnull(sum(IVT_INVPK*CMT_NMP01/1000),0) L_strBAGWT";
				M_strSQLQRY += " from MM_WBTRN,MR_IVTRN,CO_CDTRN ";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + L_strGINTP  + "'";
				M_strSQLQRY += " and WB_OUTTM between '"+L_strFMDAT+"' and '"+L_strTODAT+"'";
				M_strSQLQRY += " and WB_STSFL <> 'X'";
				M_strSQLQRY += " and IVT_CMPCD=WB_CMPCD and IVT_GINNO = WB_DOCNO";
				M_strSQLQRY += " and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG'";
				M_strSQLQRY += " and CMT_CODCD = IVT_PKGTP";
				M_strSQLQRY += " group by WB_WBRNO,WB_MATDS,WB_DOCNO,WB_LRYNO,WB_TPRDS,";
				M_strSQLQRY += "WB_INCTM,WB_OUTTM,WB_PRTDS,WB_NETWT,WB_REMWB";
				M_strSQLQRY += " order by WB_WBRNO,WB_MATDS,WB_DOCNO,WB_INCTM";
			}
			else if(L_strGINTP.equals(L_strRAWMT))	// Tankers
			{	
				M_strSQLQRY = "Select WB_WBRNO,WB_MATDS,WB_DOCNO,WB_LRYNO,WB_TPRDS,";
				M_strSQLQRY += "WB_INCTM,WB_OUTTM,CMT_CODDS L_strPRTDS,WB_CHLQT L_strCHLQT,";
				M_strSQLQRY += "WB_NETWT,WB_REMWB,'' L_strBAGWT";
				M_strSQLQRY += " from MM_WBTRN,CO_CDTRN";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + L_strGINTP  + "'";
				M_strSQLQRY += " and WB_OUTTM between '"+L_strFMDAT+"' and '"+L_strTODAT+"'";
				M_strSQLQRY += " and isnull(WB_STSFL,'') <> 'X'";					
				M_strSQLQRY += " and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDST'";
				M_strSQLQRY += " and CMT_CODCD = WB_LOCCD";
				M_strSQLQRY += " order by WB_WBRNO,WB_MATDS,WB_DOCNO,WB_INCTM";
			}
			else	// S/S & Others
			{
				M_strSQLQRY = "Select WB_WBRNO,WB_MATDS,WB_DOCNO,WB_LRYNO,WB_TPRDS,";
				M_strSQLQRY += "WB_INCTM,WB_OUTTM,WB_PRTDS L_strPRTDS,WB_CHLQT L_strCHLQT,";
				M_strSQLQRY += "WB_NETWT,WB_REMWB,'' L_strBAGWT";
				M_strSQLQRY += " from MM_WBTRN";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + L_strGINTP  + "'";
				M_strSQLQRY += " and WB_OUTTM between '"+L_strFMDAT+"' and '"+ L_strTODAT+"'";
				M_strSQLQRY += " and WB_STSFL <> 'X'";
				M_strSQLQRY += " order by WB_WBRNO,WB_MATDS,WB_DOCNO,WB_INCTM";
			}											   
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			L_strOWBRNO = "";
			L_strOMATDS = "";
			L_fltTCHLQT = L_fltTNETWT = L_fltTDIFQT = L_fltTBAGWT = 0;							
			cl_dat.M_PAGENO = 1;			
			while(M_rstRSSET.next())
			{
				L_intSRLNO = intRECCT + 1;
				L_strWBRNO = nvlSTRVL(M_rstRSSET.getString("WB_WBRNO"),"");
				L_strGINNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"");
				L_strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"");
				L_strTPRDS = (nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"") + "                 ").substring(0,16);
				L_strINCTM = M_fmtLCDTM.format(M_rstRSSET.getTimestamp("WB_INCTM"));
				L_strOUTTM = M_fmtLCDTM.format(M_rstRSSET.getTimestamp("WB_OUTTM"));
				L_strPRTDS = (nvlSTRVL(M_rstRSSET.getString("L_strPRTDS"),"") + "                  ").substring(0,17);				
				L_strCHLQT = setNumberFormat(M_rstRSSET.getDouble("L_strCHLQT"),3);
				L_strNETWT = setNumberFormat(M_rstRSSET.getDouble("WB_NETWT"),3);
				if(M_rstRSSET.getString("L_strBAGWT").length()>0)
					L_strBAGWT = setNumberFormat(Double.parseDouble(M_rstRSSET.getString("L_strBAGWT")),3);
				else 
					L_strBAGWT = "0.000";
				L_strDIFQT = setNumberFormat((Double.parseDouble(L_strNETWT) - Double.parseDouble(L_strBAGWT) - Double.parseDouble(L_strCHLQT)),3);
				L_strREMWB = nvlSTRVL(M_rstRSSET.getString("WB_REMWB"),"");
				L_strMATDS = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"");					
				if(L_stbPRNSTR.length() != 0)
					L_stbPRNSTR.delete(0,L_stbPRNSTR.length());									
				if(!L_strWBRNO.equals(L_strOWBRNO))
				{
					L_strOMATDS = L_strMATDS;
					if(!L_strOWBRNO.equals(""))
					{
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes("                                                                                         Total : ");						
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTCHLQT)),3),9) 
							 + padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTNETWT)),3),9) 
							 + padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTDIFQT)),3),9));
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</B>");
						if(L_strGINTP.equals(strDESPT_fn))
						{							
							if(M_rdbHTML.isSelected())
    							dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTBAGWT)),3),10));
							if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</B>");
						}
						dosREPORT.writeBytes("\n\n");
						cl_dat.M_intLINNO_pbst += 2;
						if(cl_dat.M_intLINNO_pbst > 64)
						{							
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);							
							prnHEADER(); 
						}					
						L_fltTCHLQT = L_fltTNETWT = L_fltTDIFQT = L_fltTBAGWT = 0;												
					}					
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("Weibridge No. : " + L_strWBRNO + "\n\n");
					cl_dat.M_intLINNO_pbst += 2;
					if(cl_dat.M_intLINNO_pbst > 64)
					{			
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER(); 
					}		
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("</B>");
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("Material      : " + L_strMATDS + "\n\n");
					cl_dat.M_intLINNO_pbst += 2;
					if(cl_dat.M_intLINNO_pbst > 64)
					{			
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER(); 
					}		
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("</B>");					
					L_strOWBRNO = L_strWBRNO;
				}
				else if(!L_strMATDS.equals(L_strOMATDS))
				{
					L_strOMATDS = L_strMATDS;
					if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("                                                                                         Total : ");					
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTCHLQT)),3),9) 
						+ padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTNETWT)),3),9) 
						+ padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTDIFQT)),3),9));
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("</B>");
					if(L_strGINTP.equals(strDESPT_fn))
					{
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTBAGWT)),3),10));
					}
					L_fltTCHLQT = L_fltTNETWT = L_fltTDIFQT = L_fltTBAGWT = 0;										
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
					if(cl_dat.M_intLINNO_pbst > 64)
					{			
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER(); 
					}								
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes("Material      : " + L_strMATDS + "\n\n");
					cl_dat.M_intLINNO_pbst += 2;
					if(cl_dat.M_intLINNO_pbst > 64)
					{			
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER(); 
					}								
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("</B>");							
				}					
				L_fltTCHLQT += Float.parseFloat(L_strCHLQT);
				L_fltTNETWT += Float.parseFloat(L_strNETWT);
				L_fltTBAGWT += Float.parseFloat(L_strBAGWT);
				L_fltTDIFQT += Float.parseFloat(L_strDIFQT);					
				L_stbPRNSTR.append(padSTRING('L',String.valueOf(L_intSRLNO),3));
				L_stbPRNSTR.append(" ");
				L_stbPRNSTR.append(padSTRING('R',L_strGINNO,9));
				L_stbPRNSTR.append(padSTRING('R',L_strLRYNO,14));
				L_stbPRNSTR.append(padSTRING('R',L_strTPRDS,18));
				L_stbPRNSTR.append(padSTRING('R',L_strINCTM,18));
				L_stbPRNSTR.append(padSTRING('R',L_strOUTTM,18));
				L_stbPRNSTR.append(padSTRING('R',L_strPRTDS,17));
				L_stbPRNSTR.append(padSTRING('L',L_strCHLQT,8));
				L_stbPRNSTR.append(padSTRING('L',L_strNETWT,9));
				L_stbPRNSTR.append(padSTRING('L',L_strDIFQT,9));
				L_stbPRNSTR.append("  ");
				if(L_strGINTP.equals(strDESPT_fn))
					L_stbPRNSTR.append(padSTRING('L',L_strBAGWT,8));
				L_stbPRNSTR.append("\n");
				cl_dat.M_intLINNO_pbst += 1;
				if(cl_dat.M_intLINNO_pbst > 64)
				{			
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);					
					prnHEADER(); 
				}								
				intRECCT++;
				dosREPORT.writeBytes(L_stbPRNSTR.toString());					
			}
			// To print the Total of the last record
			if(intRECCT > 0)
			{
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes("                                                                                         Total : ");				
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTCHLQT)),3),9) + 
					padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTNETWT)),3),9) + 
					padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTDIFQT)),3),9));				
				if(L_strGINTP.equals(strDESPT_fn))
				{
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_fltTBAGWT)),3),10));
				}
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				if(cl_dat.M_intLINNO_pbst > 64)
				{			
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);					
					prnHEADER(); 
				}		
			}					
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------");
			
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);								
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");     										
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();        	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");			
		}
	}		
	/**
	Method to generate the Header part of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {	
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			String L_strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
			dosREPORT.writeBytes("\n\n\n\n\n");								
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",101));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");
			dosREPORT.writeBytes(padSTRING('R',"WeighBridge Entry Details for" + String.valueOf(cmbGINTP.getSelectedItem()).substring(2),101));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");
			dosREPORT.writeBytes("Vehicles from " + txtFMDAT.getText().trim() + " To " + txtTODAT.getText().trim() + " between " + txtFMTIM.getText().trim() + " Hrs. to " + txtTOTIM.getText().trim() + " Hrs.\n");
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------\n");
			if(L_strGINTP.equals(strDESPT_fn))
				dosREPORT.writeBytes("Sr. Gate-In  Vehicle No.   Transporter       In Time           Out Time          Party              Loaded      Net     Diff    Bag Wt\n");
			else
				dosREPORT.writeBytes("Sr. Gate-In  Vehicle No.   Transporter       In Time           Out Time          Party              Chalan      Net     Diff\n");
			dosREPORT.writeBytes("No. No.                                                                                                Qty      Qty      Qty\n");
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst += 11;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX,"prnHEADER");
		}
	}		
	/**
	Method to calculate From-Date one day smaller than To-Date.
	@param P_strTODAT String argument to pass To-date.
	*/
    private String calFMDAT(String P_strTODAT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strTODAT));
		    M_calLOCAL.add(java.util.Calendar.DATE,-1);																
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{
	       setMSG(L_EX,"calFMDAT");
	       return (cl_dat.M_strLOGDT_pbst);
        }					
	}
}
