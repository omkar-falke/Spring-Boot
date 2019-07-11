/*
System Name   : Material Management System
Program Name  : Raw Material Stock Statement for Control Room(TankFarm System)
Program Desc. : User selects the Receipt Date & the destination for the report as screen,
				printer or file.
Author        : Mr.S.R.Mehesare
Date          : 16th Feb 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :        
*/
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;
/**<pre>
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Raw Material Stock Statement

<b>Purpose :</b> This module gives information about the Stock Status of Liquid Raw 
Material, for a given date. Details of Dip, Opening stock, Receipt, Transfer,
Issue and Closing Stock are displayed.

List of tables used : 
Table Name   Primary key                          Operations Performed
                                            Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
MM_DPTRN     DP_MEMTP,DP_MEMNO,DP_TNKNO                         #				    	     
CO_CTMST     CT_MATCD                                           #
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name   Column Name      Table name   Type/Size    Description
--------------------------------------------------------------------------------
txtMEMDT     DP_MEMDT         MM_DPTRN     Timestamp    Memo Date
--------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b>1)MM_DPTRN             
                       2)CO_CTMST
<B>Conditions Give in Query:</b>
             Data is taken from MM_DPTRN and CO_CTMST for the given Date. 		
             1)where DP_MEMTP =  '81'
             2)and Date(DP_MEMDT) =  date given by user.
             3)and DP_MATCD = CT_MATCD.             
             4)order by DP_MATCD
</I>
<b>Validations :</b>
	1) Stock Date should not be greater than today.	
	2) Details are displayed as per the Regular Dip Entry.(Memo Type -81)
	3) Details are displayed, after entry of Dip Register for a given Date.  
 */

class mm_rprms extends cl_rbase
{											/** JTextField to accept input Date.*/				  
	JTextField txtMEMDT;					/** Data Output Stream object to generate Report.*/
	DataOutputStream dosREPORT;				/** File Output Stream object for File Handling.*/
	FileOutputStream fosREPORT;				/** String variable for Tank Number.*/
	private String strTNKNO;				/** String variable for Material Description.*/
	private String strMATDS;				/** String variable for Opening Quantity.*/
	private String strMOPQT;				/** String variable for Material Reciept Quantity.*/
	private String strRCTQT;				/** String variable for Transfer quantity.*/
	private String strTRNQT;				/** String variable for Issue Quantity.*/
	private String strISSQT;				/** String variable for Closeing Quantity*/
	private String strMCLQT;				/** String variable for Dip Value.*/
	private String strDIPVL;				/** String variable for Tempreture.*/
	private String strTMPVL;				/** String variable for Density Value.*/		
	private String strDENVL;				/** String variable for Old Material Description.*/
	private String strOMATDS="";			/** String variable for Unit of Measurement.*/	
	private String strUOMCD;				/** Integer variable to Count the Records Retrieved.*/
	private int intRECCT;					/** String variable for Generated report File Name.*/
	private String strFILNM;				/** String variable for Memo Type.*/	
	private final String strMEMTP_fn = "81";
	mm_rprms()
	{
		super(2);
		try
		{				
			cl_dat.M_PAGENO =1;
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Stock Date"),4,4,1,1,this,'L');
			add(txtMEMDT=new TxtDate(),4,5,1,1,this,'L');						
			M_pnlRPFMT.setVisible(true);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"constructor");
		}	
	}		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{					
			if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)					    
				txtMEMDT.setEnabled(false);
			else 
			{
				txtMEMDT.setEnabled(true);
				if (txtMEMDT.getText().equals(""))
				{
					try
					{
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);																
						txtMEMDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));					
					}
					catch(Exception L_EX)
					{
						setMSG(L_EX,"Date Calculation");
					}
				}
			}
		}  
		if((M_objSOURC == txtMEMDT) && (txtMEMDT.getText().length()!=0))
			cl_dat.M_btnSAVE_pbst.requestFocus();
		else 
			txtMEMDT.requestFocus();		
	}	
	/**
	Method to print OR display report as per selection.
	*/
	void exePRINT()
	{
		if(vldDATA())
		{
			try
			{
			    if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rprms.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rprms.doc";				
				getALLREC();
				if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT >0)
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
					 if(intRECCT >0)
					 {						
					     if(M_rdbHTML.isSelected())
					        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
					     else
    					    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
	    			  }
 		    		  else
						  setMSG("No data found, Please check Date..",'E');				    
				}				
				else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(M_strEMLNM))
			    {
		    	    cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Raw Material Stock Statement for Control Room(TankFarm System)"," ");
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
	Method to validate data, before execution of Query, To check for blank input and valid Date
	*/
	boolean vldDATA()
	{
		try
		{	
			if (M_fmtLCDAT.parse(txtMEMDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Memo Date can not be greater than current date .. ",'E');				
				txtMEMDT.setText(cl_dat.M_strLOGDT_pbst);
				txtMEMDT.requestFocus();
				return false;
			}				
			else if(txtMEMDT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid Date ..",'E');
				txtMEMDT.requestFocus();
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
			setMSG(L_EX,"vldDATA");			
			return false;
			
		}
	}
	/**
	Method to fetch Data from table MM_DPTRN for the given memo date(DP_MEMDT) .
	*/
	private void getALLREC()
	{ 	    		
		String L_strMEMDT;
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
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Row Material Stock Statement </title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    							
    		prnHEADER();			
    		// To fetch data from the DB.            
			L_strMEMDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtMEMDT.getText().trim()));					
			intRECCT = 0;			
			setMSG("Report Generation in Progress.......",'N');			
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 1;						
			M_strSQLQRY = "Select DP_TNKNO,DP_MATCD,CT_MATDS,DP_DENVL,DP_DIPVL,DP_TMPVL,";
			M_strSQLQRY += "DP_DOPQT,DP_RCTQT,DP_TRNQT,DP_ISSQT,DP_DCLQT,CT_UOMCD ";
			M_strSQLQRY += "from MM_DPTRN,CO_CTMST ";
			M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strMEMTP_fn + "'";
			M_strSQLQRY += " and CONVERT(varchar,DP_MEMDT,101) = '" + L_strMEMDT;
			M_strSQLQRY += "' and DP_MATCD = CT_MATCD";
			M_strSQLQRY += " order by DP_MATCD";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					strTNKNO = nvlSTRVL(M_rstRSSET.getString("DP_TNKNO"),"").trim();
					strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"").trim();
					strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"").trim();
					strDENVL = M_rstRSSET.getString("DP_DENVL");
					strDIPVL = M_rstRSSET.getString("DP_DIPVL");
					strTMPVL = M_rstRSSET.getString("DP_TMPVL");
					strRCTQT = M_rstRSSET.getString("DP_RCTQT");
					strMOPQT = M_rstRSSET.getString("DP_DOPQT");
					strTRNQT = M_rstRSSET.getString("DP_TRNQT");
					strISSQT = M_rstRSSET.getString("DP_ISSQT");
					strMCLQT = M_rstRSSET.getString("DP_DCLQT");				
					if(cl_dat.M_intLINNO_pbst > 65)
					{		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						{
							prnFMTCHR(dosREPORT,M_strEJT);										
							prnFMTCHR(dosREPORT,M_strNOCPI17);			    
							prnFMTCHR(dosREPORT,M_strCPI10);		
							prnFMTCHR(dosREPORT,M_strCPI17);			    					
						}
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						prnHEADER(); 
					}
					if(!(strMATDS.equals(strOMATDS)))					
					{
						dosREPORT.writeBytes("\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(dosREPORT,M_strBOLD);						
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    						dosREPORT.writeBytes(padSTRING('R'," ",15)); 
						dosREPORT.writeBytes(padSTRING('R',strMATDS,20));					
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
							prnFMTCHR(dosREPORT,M_strNOBOLD);						
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						dosREPORT.writeBytes(padSTRING('R',strUOMCD,5));
						strOMATDS = strMATDS;
					}
					else
					{	
					if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    					dosREPORT.writeBytes(padSTRING('R'," ",15)); 	
					dosREPORT.writeBytes(padSTRING('R'," ",25));	
						strOMATDS=strMATDS;					
					}						
					dosREPORT.writeBytes(padSTRING('R',strTNKNO,10));				
					dosREPORT.writeBytes(padSTRING('L',strDENVL,8));
					dosREPORT.writeBytes(padSTRING('L',strDIPVL,10));
					dosREPORT.writeBytes(padSTRING('L',strTMPVL,10));
					dosREPORT.writeBytes(padSTRING('L',strMOPQT,15));
					dosREPORT.writeBytes(padSTRING('L',strRCTQT,13));
					dosREPORT.writeBytes(padSTRING('L',strTRNQT,13));
					dosREPORT.writeBytes(padSTRING('L',strISSQT,13));
					dosREPORT.writeBytes(padSTRING('L',strMCLQT,15) + "\n");
					intRECCT++;				
					cl_dat.M_intLINNO_pbst++;
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("\n");
			if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    			dosREPORT.writeBytes(padSTRING('R'," ",15)); 							
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------\n");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strEJT);				
				prnFMTCHR(dosREPORT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			dosREPORT.close();
			fosREPORT.close();									
		}
	 	catch(Exception L_EX)
		{		    		    
			setMSG(L_EX,"getALLREC");
		}	
	}
	/**
	Method to Generate the Header part of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {					
			dosREPORT.writeBytes("\n\n\n\n\n");								
			if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    			dosREPORT.writeBytes(padSTRING('R'," ",15)); 							
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",109));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");
			if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    			dosREPORT.writeBytes(padSTRING('R'," ",15)); 							
			dosREPORT.writeBytes(padSTRING('R',"R.M.Stock Statement for " +txtMEMDT.getText().trim() ,109));				
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");			
			if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    			dosREPORT.writeBytes(padSTRING('R'," ",15)); 							
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------\n");
			if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    			dosREPORT.writeBytes(padSTRING('R'," ",15)); 							
			dosREPORT.writeBytes("Material            UOM  Tank no.  Specific       Dip      Temp        Opening     Receipts     Transfer        Issue        Closing\n");
			if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    			dosREPORT.writeBytes(padSTRING('R'," ",15));
			dosREPORT.writeBytes("                                    Gravity      (cm)                    Stock                                                 Stock\n");
			if(M_rdbTEXT.isSelected() && (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)))
    			dosREPORT.writeBytes(padSTRING('R'," ",15)); 							
			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 11;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX,"prnHEADER");
		}
	}	
}


