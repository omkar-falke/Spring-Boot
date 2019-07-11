/*
System Name   : Material Management System
Program Name  : Tankfarm Daily Activity Report
Program Desc. : Report for Bill of Entry Details.
Author        : Mr S.R.Mehesare
Date          : 23.02.2005
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
import java.io.FileOutputStream; import java.io.DataOutputStream; import java.io.File;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
/**<pre>
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> B/E wise Receipts

<b>Purpose :</b> This module gives Materialwise, Excise Category Wise & B/E wise 
Total Receipt between given dates.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
MM_BETRN    BE_STRTP,BE_PORNO,BE_CONNO,BE_BOENO                     #			    	     
CO_CDTRN    CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #  
CO_CTMST    CT_MATCD                                                #   
MM_GRMST    GR_STRTP,GR_GRNNO,GRMATCD,GR_BATNO                      #
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table Name      Type/Size     Description
--------------------------------------------------------------------------------
cmbEXECT    GR_EXECT        MM_GRMST       Varchar(2)    Store Type	
txtFRMDT    Date Before Current Date       Date          From Date
txtTORDT    Date Before Current Date       Date          To Date
--------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b>1)MM_GRMST             
                       2)CO_CTMST
<B>Conditions Give in Query:</b>
             Data is taken from MM_GRMST and CO_CTMST between the given range and 
             GR_EXCCT = selected category. grouping is done Item code and category wise 
             1)CT_MATCD = GR_MATCD.
             2)and GR_STRTP = selected Stores.
             3)and GR_GRNDT between given date Range.
             4)and if Excise Category is given, then GR_EXCCT is also matched.
             5)Group by GR_MATCD,GR_EXCCT,L_strBOENO,GR_GRNDT
</I>
Validations :
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
 */
public class mm_rpbet extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;		/** JComboBox to accept the Material Type.*/
	private JComboBox cmbEXCCT;			/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** String variable For store Type.*/
	private String strSTRTP;			/** Hastable object for material type.*/
	private Hashtable<String,String> hstMATTP;			/** File OutputStream Object for file handling.*/	
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
    private DataOutputStream dosREPORT ;
	private ResultSet M_rstRSSET1;
	private Connection conSPBKA;
	private Statement stmSTBK1;
	mm_rpbet()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Excise Catagory"),4,4,1,1.3,this,'L');
			add(cmbEXCCT= new JComboBox(),4,5,1,1.3,this,'L');									
			add(new JLabel("From Date"),5,4,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,5,1,1,this,'L');			
			add(new JLabel("To Date"),6,4,1,1.3,this,'L');
			add(txtTODAT = new TxtDate(),6,5,1,1,this,'L');
			setCursor(cl_dat.M_curWTSTS_pbst);			
			hstMATTP=new Hashtable<String,String>();		
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMAT'";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
            if(M_rstRSSET != null)    			        		
		    {
				cmbEXCCT.addItem("All Catagories");
        		while(M_rstRSSET.next())
        		{        		         
        			cmbEXCCT.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));        			    
					hstMATTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
            }        		
			setCursor(cl_dat.M_curDFSTS_pbst);			
			M_pnlRPFMT.setVisible(true);			
			cmbEXCCT.setEnabled(true);
			setCONFTB("C:\\reports");			    
			setENBL(false);	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX+" GUI Designing",'E');
		}	
	}			
    //Super class method overide to inhance its funcationality
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtTODAT.setEnabled(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);		
		if (L_flgSTAT==true )
		{
			if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
			{					 
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));																					
       				txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));				            														
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" setENBL",'E');
				}
			}
			else 
			{
				if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				{
					txtTODAT.setText("");
					txtFMDAT.setText("");			
				}
			}
		}
	}		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
			cl_dat.M_PAGENO=0;
		if(M_objSOURC == txtFMDAT)			
			txtTODAT.requestFocus();
		else if(M_objSOURC == txtTODAT)			
			cl_dat.M_btnSAVE_pbst.requestFocus();
		else if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			//cl_dat.M_PAGENO=1;
			if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)					    
				cmbEXCCT.requestFocus();
		   	
		} 
		else if(M_objSOURC == cmbEXCCT)
			txtFMDAT.requestFocus();
		
		if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(M_cmbDESTN.getItemCount()==2)
				{
					M_cmbDESTN.setEnabled (true);
				}
				else if(M_cmbDESTN.getItemCount()==1)
				{
					setMSG("No Printer Attached to the System ..",'E');
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    cl_dat.M_txtDESTN_pbst.setText("c:\\reports\\adv_rct.dbf");
			}
		}
	}	        					
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if(vldDATA())
		{
			try
			{
			    if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpbet.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpbet.doc";				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				{
				    crtDATFL();
				    setMSG("Data File created ",'N');
				    return;
				}
				getALLREC();				
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
						  setMSG("No data found, Please check Date Range OR Excise Category ..",'E');				    
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			    {
		    	    cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Bill of Entry Report"," ");
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
	Method to validate data, before execution. Check for blank input and wrong selection of Memo type
	*/
	boolean vldDATA()
	{
		try
		{	
			if(txtFMDAT.getText().trim().toString().length() == 0)			
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
			else if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtTODAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" vldDATA",'E');
				}	
			}	
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("Please Note that From-Date must be Greater than To-Date .. ",'E');								
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtFMDAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX + " vldData", 'E');
				}																
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
	Method to fetch Data from Database.
	*/
	private void getALLREC()
	{ 	    		
		String L_strFMDAT,L_strTODAT;
		String L_strMATCD,L_strMATDS="",L_strBOENO,L_strBOEDT="";
		String L_strCHLQT,L_strNETQT,L_strMATTP = "",L_strMATCT;
		String L_strOMATCD = "",L_strOMATCT = "",L_strPRNSTR, L_strOBOENO ="X";			
		float L_fltTCHLQT =0, L_fltTNETQT =0;
		float L_fltTBECQT= 0, L_fltTBENQT =0;						
		try
	    {	
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Bill of Entry Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();			
    		// To fetch data from the DB.            			
			L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));		
			setMSG("Report Generation is in Progress.......",'N');			   			
		    M_strSQLQRY = "Select GR_MATCD,GR_EXCCT,isnull(GR_BOENO,'-')L_strBOENO,GR_GRNDT,CT_MATDS,";
			M_strSQLQRY += "GR_CHLQT L_strCHLQT,GR_RECQT L_RECQT";
			M_strSQLQRY += " from MM_GRMST,CO_CTMST WHERE CT_MATCD = GR_MATCD ";
			M_strSQLQRY += " and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
			M_strSQLQRY += " and GR_GRNDT between '" + L_strFMDAT + "' and '" + L_strTODAT +"'";
			if (cmbEXCCT.getSelectedIndex() !=0)
				M_strSQLQRY += " and GR_EXCCT = '"+ String.valueOf(cmbEXCCT.getSelectedItem()).substring(0,2) +"'"; 
			M_strSQLQRY += " order by GR_MATCD,GR_EXCCT,L_strBOENO,GR_GRNDT"; 		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{				
    			while(M_rstRSSET.next())
    			{
    				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");				
    				L_strMATCT = nvlSTRVL(M_rstRSSET.getString("GR_EXCCT"),"");
    				L_strBOENO = nvlSTRVL(M_rstRSSET.getString("L_strBOENO"),"");
    				L_strCHLQT = nvlSTRVL(M_rstRSSET.getString("L_strCHLQT"),"0");
    				L_strNETQT = nvlSTRVL(M_rstRSSET.getString("L_RECQT"),"0");
    				if(!L_strMATCD.trim().equals(L_strOMATCD))
    					L_strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");    					
    			
    				if(!L_strMATCD.trim().equals(L_strOMATCD) || !L_strMATCT.trim().equals(L_strOMATCT))
					{	    											
    					if(intRECCT != 0)
    					{
    						L_strBOEDT ="";							
    						if(L_strOBOENO.trim().length() >0)								
								L_strBOEDT = getBOEDT(L_strOBOENO);
							dosREPORT.writeBytes(padSTRING('R',"",42));							
    						dosREPORT.writeBytes(padSTRING('R',L_strOBOENO,16));
    						dosREPORT.writeBytes(padSTRING('R',L_strBOEDT,13));
    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTBECQT,3),14));
    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTBENQT,3),14)+"\n");    						
    						cl_dat.M_intLINNO_pbst += 1;    						
							if(cl_dat.M_intLINNO_pbst > 64)
							{
								dosREPORT.writeBytes("\n");								
								dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}							
    						L_fltTBECQT =0;
    						L_fltTBENQT =0;
    						L_strOBOENO =L_strBOENO; 							
							if(M_rdbHTML.isSelected())
    							dosREPORT.writeBytes("<b>");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);				
							dosREPORT.writeBytes("\n");							
							dosREPORT.writeBytes(padSTRING('L',"Total :",69));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTCHLQT,3),16));
							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTNETQT,3),14)  + "\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);				
							if(M_rdbHTML.isSelected())
    							dosREPORT.writeBytes("</b>");							
							dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");
    						cl_dat.M_intLINNO_pbst += 2;
    						L_fltTCHLQT =0;
							L_fltTNETQT =0;
    						L_fltTBECQT =0;
    						L_fltTBENQT =0;
    					}
    					L_strOMATCD = L_strMATCD;
    					L_strOMATCT = L_strMATCT;
    					L_strOBOENO = L_strBOENO;
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    						prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes("\n");
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<b>");						
    					dosREPORT.writeBytes(padSTRING('R',L_strMATCD,12));
						dosREPORT.writeBytes(padSTRING('R',L_strMATDS,26)); 												
						if(hstMATTP.containsKey(L_strMATCT))
							L_strMATTP = hstMATTP.get(L_strMATCT).toString();						
						dosREPORT.writeBytes(padSTRING('R',L_strMATTP,16));
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</b>");
    					dosREPORT.writeBytes("\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    						prnFMTCHR(dosREPORT,M_strNOBOLD);
    					cl_dat.M_intLINNO_pbst= 2;
						if(cl_dat.M_intLINNO_pbst > 64)
							{	
								dosREPORT.writeBytes("\n");								
								dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");		
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}
    				}
    				if(!L_strBOENO.trim().equals(L_strOBOENO))
    				{
    					L_strBOEDT ="";
    					if(L_strOBOENO.trim().length() >0)
    						L_strBOEDT = getBOEDT(L_strOBOENO);
    					L_strPRNSTR = padSTRING('R',L_strOBOENO,16);
    					L_strPRNSTR += padSTRING('R',L_strBOEDT,13);
    					L_strPRNSTR += padSTRING('L',setNumberFormat(L_fltTBECQT,3),14);
    					L_strPRNSTR += padSTRING('L',setNumberFormat(L_fltTBENQT,3),14);												
    					dosREPORT.writeBytes(padSTRING('R',"",42));
    					dosREPORT.writeBytes(L_strPRNSTR + "\n");
    					cl_dat.M_intLINNO_pbst += 1;    					
    					L_fltTBECQT =0;
    					L_fltTBENQT =0;
    					L_strOBOENO =L_strBOENO;
    			    }
    				L_fltTCHLQT += Float.parseFloat(L_strCHLQT);
    				L_fltTNETQT += Float.parseFloat(L_strNETQT);
    				L_fltTBECQT += Float.parseFloat(L_strCHLQT);
    				L_fltTBENQT += Float.parseFloat(L_strNETQT);					
    				L_strPRNSTR = "";
    				intRECCT++;
    			}
    			M_rstRSSET.close();
			}			
			else 
    		{							
				setMSG("No Data Found..",'E');
				return ;
			}
			if(intRECCT == 0)
			{
				setMSG("No Data Found for the given selection..",'E');
				return ;
			}
			else
			{
				L_strBOEDT ="";
				if(L_strOBOENO.trim().length() >0)
					L_strBOEDT = getBOEDT(L_strOBOENO);
				L_strPRNSTR = padSTRING('R',L_strOBOENO,16);
				L_strPRNSTR += padSTRING('R',L_strBOEDT,13);
				L_strPRNSTR += padSTRING('L',setNumberFormat(L_fltTBECQT,3),14);
				L_strPRNSTR += padSTRING('L',setNumberFormat(L_fltTBENQT,3),14);				
				dosREPORT.writeBytes(padSTRING('R',"",42));
				dosREPORT.writeBytes(L_strPRNSTR + "\n");
				cl_dat.M_intLINNO_pbst += 1;				
				if(cl_dat.M_intLINNO_pbst > 64)
				{	
					dosREPORT.writeBytes("\n");					
					dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");					
					prnHEADER();
				}
				L_fltTBECQT =0;
				L_fltTBENQT =0;
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				dosREPORT.writeBytes("\n");				
				dosREPORT.writeBytes(padSTRING('L',"Total :",69));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_fltTCHLQT,3),16)  
						+ padSTRING('L',setNumberFormat(L_fltTNETQT,3),14)  + "\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");				
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------");		
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</html>");
				setMSG("Report completed.. ",'N');
				setCursor(cl_dat.M_curDFSTS_pbst);			
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}						
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
		    if(M_rstRSSET != null)
				M_rstRSSET.close();						
	    }
	 	catch(Exception L_EX)
		{		    
		    //cl_dat.M_PAGENO=1;
			setMSG(L_EX+" getALLREC",'E');
		}	
	}
	/**
	Method to Generate the Header part of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			strSTRTP = M_strSBSCD.substring(2,4);
			dosREPORT.writeBytes("\n\n\n\n\n");											
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",75));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"B/E wise Reciept between " +txtFMDAT.getText().trim()+ " and " + txtTODAT.getText().trim(),75));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("Material Code & Description           Mat.Category   \n");		    			
			dosREPORT.writeBytes("                                           B.E./InvNo.    B.E/Inv Date   Challan Qty  Received Qty.\n");
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 11;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
	/**
	Method To get Date of Bill of entry. 
	*/
	private String getBOEDT(String P_strBOENO)
	{
		String L_strBOEDT = "";		
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);			
			M_strSQLQRY = "Select BE_BOEDT from MM_BETRN";
			M_strSQLQRY += " where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_BOENO = '"+P_strBOENO.trim() +"'";			
			M_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);						
			if(M_rstRSSET1 !=null)			
			{	
				if (M_rstRSSET1.next())
					L_strBOEDT =  M_fmtLCDAT.format(M_rstRSSET1.getDate("BE_BOEDT"));
			}
			M_rstRSSET1.close();
			setCursor(cl_dat.M_curDFSTS_pbst);									
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX+" getBOEDT",'E');
		}
		return L_strBOEDT;
	}
private boolean crtDATFL() // For B/E/ wise advance licence receipt
{
	try
	{
		int i=0,j=0;
		java.sql.ResultSet L_rstRSSET ;
		//cl_dat.M_txtDESTN_pbst.setText("C:\\Reports\\adv_rct.dbf");    
		cpyDBFFL();
	   	M_strSQLQRY = "SELECT GR_GRNTP,GR_BOENO,BE_BOEDT,BE_BOEQT,GR_GRNNO,GR_GRNDT,GR_MATCD,'',"
					 +"GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_CHLQT,GR_acpQT,(GR_ACPQT-GR_CHLQT) DIFQT "
					 +" FROM MM_GRMST,MM_BETRN WHERE GR_CMPCD = BE_CMPCD and GR_BOENO = BE_BOENO ";
		if(cmbEXCCT.getSelectedIndex() >0)
	        M_strSQLQRY += " AND	BE_MATTP ='"+cmbEXCCT.getSelectedItem().toString().substring(0,2)+"'";
		M_strSQLQRY +=" AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRNDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' "+
		              " AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' ORDER BY GR_BOENO,GR_MATCD";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		cl_dat.M_flgLCUPD_pbst = true;
		if (M_rstRSSET != null){
				
			while(M_rstRSSET.next())
			{
				M_strSQLQRY =  "Insert into adv_rct values(";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNTP"),"")+"',";					
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_BOENO"),"")+"',";
				M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("BE_BOEDT"))))+"'),";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("BE_BOEQT"),"0")+",";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")+"',";
				M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_GRNDT"))))+"'),";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"")+"',";
				M_strSQLQRY += "' ',";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_LRYNO"),"")+"',";
				M_strSQLQRY += "'"+nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"")+"',";
				M_strSQLQRY += "ctod('"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_CHLDT"))))+"'),";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0")+",";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0")+",";
				M_strSQLQRY += nvlSTRVL(M_rstRSSET.getString("DIFQT"),"0")+")";
				if(stmSTBK1.executeUpdate(M_strSQLQRY) >0)
				{
					i++;
				}
				else
				{
					j++;
				}
				setMSG("saved "+i+" not saved "+j,'N');
				conSPBKA.commit();
			}
		}
	}
	catch(Exception L_E)
	{
		return false;
	}
	return true;
}
public Connection setCONFTB(String LP_PTHWD)
{
	String L_URLSTR ="";
    L_URLSTR = "jdbc:odbc:Visual FoxPro Tables;SourceDB = " + LP_PTHWD;
	try{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        conSPBKA = DriverManager.getConnection(L_URLSTR,"","");
	    stmSTBK1 = conSPBKA.createStatement();
	}
	catch(ClassNotFoundException L_CNFE){
		System.out.println("Connectiob not found");
	}
	catch(SQLException L_SQLE){
		System.out.print("Database not found "+M_strSQLQRY);
	}
	return conSPBKA;
}
void cpyDBFFL()
{

	boolean L_flgEXTVL = false;
	Process p = null;
	this.setCursor(cl_dat.M_curWTSTS_pbst);
 	L_flgEXTVL = false;	
	Runtime r = Runtime.getRuntime();
	try
	{
		    if(conSPBKA !=null)
		        conSPBKA.close();
		    setCONFTB("c:\\reports");
		    File L_TMPFL = new File("c:\\reports\\adv_rct.dbf");
			if(L_TMPFL.exists())
					L_TMPFL.delete();
			p  = r.exec("c:\\windows\\xcopy.exe" + " f:\\foxdat\\0102\\datatfr\\mmsdata\\adv_rct.dbf " + "c:\\reports"); 
			while(!L_flgEXTVL)
			{
    			try
    			{
    				L_flgEXTVL = true;
    				p.exitValue();
    			}catch(Exception L_E)
    			{
    				L_flgEXTVL = false;
    			}
			}
		}
		catch(Exception L_E)
		{
	        setMSG(L_E,"cpyDBFFL");
		}
		p.destroy();

}

}
