/*
System Name   : Material Management System
Program Name  : Vehicle Entry Analysis
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 14th Feb 2005
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
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
/**<pre>
<B>System Name :</B> Material Management System.
 
<B>Program Name :</B> Vehicle Reporting.

<B>Purpose :</B> This module gives details of Vehicles Reported between given date & time.

List of tables used :
Table Name    Primary key                            Operation done
                                          Insert   Update   Query   Delete	
-----------------------------------------------------------------------------
MM_WBTRN      WB_DOCTP,WB_DOCNO,WB_SRLNO                      #	     
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD                   #
-----------------------------------------------------------------------------

List of  fields entered/displayed on screen :
Field Name  Column Name    Table name   Type/Size      Description
-----------------------------------------------------------------------------
cmbGINTP    CMT_CODCD      CO_CDTRN     Varchar(2)     Gate-In Code
            CMT_CODDS      CO_CDTRN     Varchar(100)   Gate_In Type	
-----------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b>1)MM_WBTRN                        
                       2)CO_CDTRN                                               
<B>Conditions Give in Query:</B>
     - Gate-In Code & Description is taken from CO_CDTRN for condition
         CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'
     - Report Data is taken from MM_WBTRN for following condition
         WB_DOCTP = Selected Gate-In Type						
         and WB_REPTM for given Date & time range. </I>

<b>Validations :</b>
    - Gate-In Type must be any one of the following.
        01 Tanker
        02 Stores & Spares
        03 Despatch
        04 Sales Return
        09 Others
   - From-Date-Time & To-Date-Time cannot be blank.
   - Date-Times must be smaller than today.
   - From-Date-Time must be smaller than or equal to To-Date-Time.*/
class mm_rpvea extends cl_rbase
{								/** JTextField to insert From Date.*/
	private JTextField txtFMDAT;		/** JTextField to insert To Date.*/
	private JTextField txtTODAT;		/** JTextField to insert From Time.*/
    private JTextField txtFMTIM;		/** JTextField to insert To Time.*/
    private JTextField txtTOTIM;		/** JComboBox to select Gate-In Type.*/
    private JComboBox cmbGINTP;			    /** String variable for generate Report File Name.*/
    private String strFILNM;		/** String variable for Gate-In Type.*/
    private String strGTNTP;		/** String variable for Old Reporting Date.*/
    private String strOREPDT="";    /** String variable for hold data fetched from database to generate Report File.*/
    private String strPRNSTR;		/** String variable for From-Date as Timestamp*/	
    private String strFMDAT;		/** String variable for To-Date as Timestamp.*/		
	private String strTODAT;		/** String variable for Gate in number.*/    
	private String strGINNO;		/** String variable for Lorry Number.*/		
	private String strLRYNO;		/** String variable for Transporter Code.*/
	private String strTPRCD;		/** String variable for Transporter Description.*/		
	private String strTPRDS;		/** String variable for Reporting Time.*/	
	private String strREPTM;		/** String variable for reporting Date.*/	
	private String strREPDT;		/** Integer variable for Recount.*/
	private int intRECCT;			/** FileOutputStream object for generate Report file Handling.*/
	private FileOutputStream fosREPORT;		/** DataOutputStream to generate Report file.*/
    private DataOutputStream dosREPORT;		
		
    mm_rpvea()
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
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
            if(M_rstRSSET != null)    			        		
		    {
		        cmbGINTP.addItem("Select Gate-in Type");
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
     * Super class method overrided to enhance its funcationality to set Components enable & 
     * disable, according to requirement.
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
				txtFMTIM.setText("00:01");
				txtTOTIM.setText("12:00");	
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
			{
				setENBL(false);	
				setMSG("Please Select Destination for the Report..",'N');
			}
			else 
				setENBL(true);			    
		}    
		if(M_objSOURC == cmbGINTP)
		{
		    if (cmbGINTP.getSelectedIndex()!=0)   
		    {
		       setENBL(true);		       
		       strGTNTP=cmbGINTP.getSelectedItem().toString().substring(0,2);
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
		if(M_objSOURC ==cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;
		}
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
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpvea.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpvea.doc";				
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
						  setMSG("No data found, Please check Inputs given ..",'E');				    
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			    {
		    	   if(intRECCT == 0)
					   return;
					cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Vehicle Entry Analysis Sheet."," ");
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
			String L_strFMDTM,L_strTODTM;
			L_strFMDTM = txtFMDAT.getText().trim()+" " + txtFMTIM.getText().trim();
			L_strTODTM = txtTODAT.getText().trim() +" "+ txtTOTIM.getText().trim();	
			if(cmbGINTP.getSelectedIndex() == 0)			
			{
				setMSG("Please Select Gate-in type .. ",'E');
				cmbGINTP.requestFocus();
				return false;
			}	
			else if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Specify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter valid To-Date To Specify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}	
			else if(txtFMTIM.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter From-Time, To Specify Date Range ..",'E');
				txtFMTIM.requestFocus();
				return false;
			}
			else if(txtTOTIM.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter To-Time, To Specify Date Range ..",'E');
				txtTOTIM.requestFocus();
				return false;
			}
			else if(M_fmtLCDTM.parse(L_strTODTM).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
			{
				setMSG("Date OR Time can not be greater than current Date OR Time..",'E');
				txtTODAT.requestFocus();
			}
			else if(M_fmtLCDTM.parse(L_strFMDTM).compareTo(M_fmtLCDTM.parse(L_strTODTM))>0)
			{
				setMSG("To Date Time can not be smaller than From Date Time..",'E');
				txtFMDAT.requestFocus();
			}	
			else if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
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
	Method to fetch Data from tables MM_WBTRN for given Date-Time range.
	*/
	private void getALLREC()
	{ 	    
		int L_intSRLNO = 0;
		String L_strFMDAT,L_strTODAT;
	    try
	    {	        
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);		
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Vechicle Entry Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();	        		
    		// To fetch data from the DB.            					
			L_strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim()+" " + txtFMTIM.getText().trim()));
			L_strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim() +" "+ txtTOTIM.getText().trim()));				
			setMSG("Report Generation is in Progress.......",'N');			   
			/*M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_TPRCD,WB_TPRDS,Time(WB_REPTM) L_REPTM,Date(WB_REPTM) L_REPDT" +
						" from MM_WBTRN where WB_DOCTP = '" + strGTNTP  + "'" +
						" and isnull(WB_STSFL,'') <> 'X'" +
						" and Date(WB_REPTM) between '" + L_strFMDAT + "' and '" + L_strTODAT +
						"' and Time(WB_REPTM) between '" + txtFMTIM.getText().trim() + "' and '" + txtTOTIM.getText().trim() + "'" +
						" order by L_REPDT,L_REPTM";*/
			M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_TPRCD,WB_TPRDS,WB_REPTM  L_REPTM,CONVERT(varchar,WB_REPTM,103) L_REPDT" +
						" from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGTNTP  + "'" +
						" and isnull(WB_STSFL,'') <> 'X'" +
						" and WB_REPTM between '" + L_strFMDAT + "' and '" + L_strTODAT +						
						"' order by L_REPDT,L_REPTM";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);						
            intRECCT = 0;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&(M_rdbHTML.isSelected()))
			{				
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}
			if(M_rstRSSET != null)    			        		
			while(M_rstRSSET.next())
			{
				strGINNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"");
				strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"");
				strTPRCD = nvlSTRVL(M_rstRSSET.getString("WB_TPRCD"),"");
				strTPRDS = nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"");
				strREPTM = nvlSTRVL(M_rstRSSET.getString("L_REPTM"),"");				
				java.sql.Date tempDATE = M_rstRSSET.getDate("L_REPDT");
				if (tempDATE!= null)
					strREPDT = M_fmtLCDAT.format(tempDATE);																					
				strPRNSTR= "";
				if(!strREPDT.equals(strOREPDT))
				{
					if(!strOREPDT.equals(""))
					{	
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<pre><b>");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    									
							prnFMTCHR(dosREPORT,M_strBOLD);						
						dosREPORT.writeBytes("\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    						dosREPORT.writeBytes(padSTRING('R'," ",6)); 							
						dosREPORT.writeBytes("Total Vehicles : " + (L_intSRLNO-1)+"\n\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    									
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</b>");			
						cl_dat.M_intLINNO_pbst += 3;
						if(cl_dat.M_intLINNO_pbst > 64)
						{							
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    							dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
							dosREPORT.writeBytes("---------------------------------------------------------------------------------------\n");																		
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);						
							prnHEADER();
						}
					}
					L_intSRLNO = 1;
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("<pre><b>");   
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    					dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    									
						prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes("Reporting Date : " + strREPDT + "\n\n");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    									
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("</b>");    							
					strOREPDT = strREPDT;
					cl_dat.M_intLINNO_pbst +=2;
					if(cl_dat.M_intLINNO_pbst > 64)
					{						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    						dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
						dosREPORT.writeBytes("---------------------------------------------------------------------------------------\n");																		
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER();
					} 
				}
				if (L_intSRLNO < 10)
					strPRNSTR+= padSTRING('R',"0"+String.valueOf(L_intSRLNO)+".",3);
				else
					strPRNSTR+= padSTRING('R',String.valueOf(L_intSRLNO)+".",3);
				strPRNSTR+= "   ";
				strPRNSTR+= padSTRING('R',strGINNO,14);
				strPRNSTR+= padSTRING('R',strLRYNO,14);
				strPRNSTR+= padSTRING('R',strTPRCD,7);
				strPRNSTR+= padSTRING('R',strTPRDS,29) + "  ";
				strPRNSTR+= padSTRING('R',strREPTM,5);
				strPRNSTR+= "\n";
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
				dosREPORT.writeBytes(strPRNSTR);
				strPRNSTR="";
				cl_dat.M_intLINNO_pbst++;
				L_intSRLNO++;
				intRECCT++;
				if(cl_dat.M_intLINNO_pbst > 64)
				{					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    					dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
					dosREPORT.writeBytes("---------------------------------------------------------------------------------------\n");																		
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);						
					prnHEADER();
				}		
			}			
			if(intRECCT > 0)
			{
				if(cl_dat.M_intLINNO_pbst > 64)
				{					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    					dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
					dosREPORT.writeBytes("---------------------------------------------------------------------------------------\n");																		
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);						
					prnHEADER();
				}		
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<pre><b>");    		
				dosREPORT.writeBytes("\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    									
					prnFMTCHR(dosREPORT,M_strBOLD);
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes (padSTRING('R'," ",6));    							
				if (L_intSRLNO <=9)					
					dosREPORT.writeBytes("Total Vehicles : 0" + (L_intSRLNO-1) + "\n\n");
				else 
					dosREPORT.writeBytes("Total Vehicles : " + (L_intSRLNO-1) + "\n\n");				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");    							
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    									
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				//cl_dat.M_intLINNO_pbst += 3;
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes (padSTRING('R'," ",6));
				dosREPORT.writeBytes("---------------------------------------------------------------------------------------");				
			}			
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);								
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");     							
			strOREPDT="";
			dosREPORT.close();
			fosREPORT.close();						        			        			   
        	setCursor(cl_dat.M_curDFSTS_pbst);			
	    }
	 	catch(Exception L_EX)
		{		    		    
			setMSG(L_EX,"getALLREC");
		}	
	}
	/**
	Method to generate the Header part of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO += 1;
			cl_dat.M_intLINNO_pbst = 0;
			dosREPORT.writeBytes("\n\n\n\n\n");								
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    			dosREPORT.writeBytes(padSTRING('R'," ",6)); 							
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",63));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    			dosREPORT.writeBytes(padSTRING('R'," ",6)); 							
			dosREPORT.writeBytes(padSTRING('R',"Reporting of " + String.valueOf(cmbGINTP.getSelectedItem()).substring(2) + " Vehicles",63));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    			dosREPORT.writeBytes(padSTRING('R'," ",6)); 							
			dosREPORT.writeBytes("From " + txtFMDAT.getText().trim() + " To " + txtTODAT.getText().trim() + " between " + txtFMTIM.getText().trim() + " Hrs. to " + txtTOTIM.getText().trim() + " Hrs.\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    			dosREPORT.writeBytes(padSTRING('R'," ",6)); 							
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    			dosREPORT.writeBytes(padSTRING('R'," ",6)); 							
			dosREPORT.writeBytes("SrNo. Gate-In No.   Vehicle No.   Transporter Code & Name               Reporting Time" +"\n") ;		    
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    			dosREPORT.writeBytes(padSTRING('R'," ",6)); 							
			dosREPORT.writeBytes("---------------------------------------------------------------------------------------\n\n");		
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
