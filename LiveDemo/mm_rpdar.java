/*
System Name   : Material Management System
Program Name  : Tankfarm Daily Activity Report
Program Desc. : Generates the Repot for Give Date, to view day & month opening stk, reciept, issue & closeing stock.
Author        : Mr S.R.Mehesare
Date          : 04.02.2005
Version       : MMS v2.0.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.awt.*;
import java.sql.*;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;  
import java.io.FileOutputStream; import java.io.DataOutputStream; 
/**<P><PRE>
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Daily Inventory Report

<b>Purpose :</b> Tankfarm daily Inventory report displays Material and Tankwise opening 
Stock, Receipts and closing Stock.It also displays the calculated figure for Issues. 
Total Receipt and Issue for the month is also displayed.

Tank wise Issue quantity for the day = (Closing stock - Opening stock - Receipts)
(Calculated figure)

List of tables used :
Table Name      Primary key                              Operation done
                                              Insert   Update   Query   Delete	
-------------------------------------------------------------------------------
MM_DPTRN        DP_MEMTP,DP_MEMNO,DP_TNKNO                        #	     
CO_CTMST        CT_MATCD                                          #
-------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name         Table name          Type/Size   Description
-------------------------------------------------------------------------------
txtMEMDT    DP_MEMDT            MM_DPTRN            Timestamp   Memo Date
-------------------------------------------------------------------------------

<B><I>
Tables Used For Query :</b>1)MM_DPTRN
                       2)CO_CTMST
<B>Conditions Give in Query:</b>
     Data is taken from MM_DPTRN and CO_CTMST for the Date Range as from 1st day of that month
and the date given by the user
             1)DP_MATCD = CT_MATCD
             2)and DP_MEMDT Between 1st Day of that Month & the Date given by the user             
            
</I>
Validations :
	 1) Stock Date should not be greater than today.	
*/

class mm_rpdar extends cl_rbase
{										/** JTextField box for accepting Memo Type.*/
	private JTextField txtMEMDT;        /** FileOutputStream object for file Oparation.*/
    private FileOutputStream fosREPORT; /** DataOutputStream object for storing data to genarate Report File.*/
    private DataOutputStream dosREPORT; /** String variable for Tank Number.*/
    private String strTNKNO;            /** String variable for Material Code.*/
    private String strMATCD;            /** String variable for Material Description.*/
    private String strMATDS;            /** String variable for Unit of Measurement.*/
    private String strUOMCD;            /** String variable for Memo Date.*/
    private String strMEMDT;            /** String variable for old Tank Number.*/
    private String strOTNKNO;           /** String variable for old Material Code.*/
    private String strOMATCD;           /** String variable to store data fetched from data base to add in Output Stream.*/
	private String strPRNSTR;           /** String variable for file name to open or to print on Request.*/
	private String strFILNM;            /** Integer variable to count the records fetched from DataBase.*/
	private int intRECCT;	            /** Final String for Memo Type (Dip Regular).*/	
    private final String strREGDP_fn = "81"; 
	
	mm_rpdar()
	{	
	    super(2);    
		try
		{	
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Stock Date"),4,4,1,1,this,'L');
			add(txtMEMDT = new TxtDate(),4,5,1,1.2,this,'L');
			M_pnlRPFMT.setVisible(true);			
			setENBL(false);						
			txtMEMDT.requestFocus();
			setMSG("Enter Stock Date to generate Report..",'N');
        }
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
	}
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		if(L_flgSTAT == true) 			
			getMEMDT();
		if (L_flgSTAT == false) 
			txtMEMDT.setText("");
	}
    public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		String L_ACT = L_AE.getActionCommand();
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPRSL_pbst))
			{
				getMEMDT();
				setENBL(true);							
			}
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    M_cmbDESTN.requestFocus();				
			}
			else
			{
				txtMEMDT.requestFocus();
				setMSG("Enter Stock Date to generate Report..",'N');
			}
		}		
	}
	/**
	Key event handler for, Enter key pressed in Memo Type Text Field.
	*/
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtMEMDT)
				cl_dat.M_btnSAVE_pbst.requestFocus();			
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtMEMDT)
			{
				M_strHLPFLD = "txtMEMDT";
				M_strSQLQRY = "SELECT DISTINCT DP_MEMDT,DP_MEMNO FROM MM_DPTRN "
					+"WHERE DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '"+strREGDP_fn+"' AND ";
				M_strSQLQRY +="isnull(DP_STSFL,' ') <> 'X' ORDER BY DP_MEMDT DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Stock Date","Memo No."},2,"CT");
			}
		}
	}
	
	/**
	Method to print or display report, as per the selection
	*/
	void exePRINT()
	{
	   if(vldDATA())
		{
			try
			{
			    strMEMDT = txtMEMDT.getText().trim();
                if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpdar.html";	        
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpdar.doc";	         			    		    
			    getALLREC(strMEMDT);			    				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT >0)
					{
					    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					         doPRINT(strFILNM); 					        					     
					    else 
                            {    
                            Runtime r = Runtime.getRuntime();
					        Process p = null;					
				            p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				            setMSG("For Printing Select FileMenu, then PrintMenu item  ..",'N');
                            }    
                    }
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				    Runtime r = Runtime.getRuntime();
					Process p = null;
					if(intRECCT >0)
					{	
					    if(M_rdbHTML.isSelected())
					        p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					    else
					        p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
				    }
					else
					    setMSG("No data found..",'E');
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			    {
		    		cl_eml ocl_eml = new cl_eml();
				    setMSG("Generating File to send Email ..",'N');
					if(M_cmbDESTN.getItemCount()>0)
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Tankfarm Activity Report"," ");					
	    				setMSG("Email Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " ",'N');
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
	* Method to generate Help Screen for F1 Key Pressed.	  
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtMEMDT")
		{
			txtMEMDT.setText(cl_dat.M_strHLPSTR_pbst.substring(0,10));
			txtMEMDT.requestFocus();
			setMSG("",'E');
		}
	}
	/**
	 * Method to generate the Report as on date given by user.
	 * @param P_strMEMDT String type Memo date given by user.
	 */
	private void getALLREC(String P_strMEMDT)
	{ 
		try
		{		    
		    float L_fltDOPQT,L_fltDCLQT,L_fltRCTQT,L_fltTRNQT,L_fltISSQT,L_fltDSPQT,L_fltMOPQT=0;
			float L_fltMRCQT=0,L_fltMISQT=0,L_fltTRCQT=0,L_fltTISQT=0,L_fltTCLQT=0,L_fltTOPQT =0;			
			boolean L_flgHDRFL = true;
			boolean L_flgFIRST = true;
			String L_strMEMDT="";
			String L_strCALISS= "0";
		    String L_strSTRDT = "01" + P_strMEMDT.substring(2);
		    L_strSTRDT = M_fmtDBDTM.format(M_fmtLCDTM.parse(L_strSTRDT.trim()+" 00:00"));	
            P_strMEMDT = M_fmtDBDTM.format(M_fmtLCDTM.parse(P_strMEMDT.trim()+" 23:59"));	
            
	       //generates the header part of the report 
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			String L_strMATCD ="";			
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');	   		    
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);			    
			    prnFMTCHR(dosREPORT,M_strCPI10);		
			    prnFMTCHR(dosREPORT,M_strCPI12);		
			}		    
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Tankfarm Activity Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");
			    dosREPORT.writeBytes("<Pre>");    
			}
			dosREPORT.writeBytes("\n\n\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",10));						
			dosREPORT.writeBytes("SUPREME PETROCHEM LTD.                                                 ");
			dosREPORT.writeBytes("Report Date :"+cl_dat.M_strLOGDT_pbst + "\n");	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",10));			
			dosREPORT.writeBytes("Tankfarm Activity Report as on " + strMEMDT  +"                              ");
			dosREPORT.writeBytes("Page No.    :" + 1 + "\n");

			//generate the the Header of the Table
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",10));
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
		    	dosREPORT.writeBytes(padSTRING('R'," ",10));
			dosREPORT.writeBytes("Material Code & Description    UOM    Day Opn Stk    Day Receipt    Day Issue    Closing Stock" + "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",10));
			dosREPORT.writeBytes("                                      Mon Opn Stk    Mon.Receipt    Mon.Issue " + "\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",10));
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n");
			
			// to fetch data & genarate body part of the report.
			setCursor(cl_dat.M_curDFSTS_pbst);
			M_strSQLQRY = "Select DP_TNKNO,DP_MATCD,CT_MATDS,CT_UOMCD," +
						" CONVERT(varchar,DP_MEMDT,101) L_strMEMDT,DP_DOPQT,DP_DCLQT,DP_RCTQT,DP_DSPQT,DP_TRNQT," +
						" DP_ISSQT from MM_DPTRN,CO_CTMST where DP_MATCD = CT_MATCD" +
						" and DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP ='" + strREGDP_fn + "'and DP_MEMDT between '" + L_strSTRDT + "' and '" + P_strMEMDT + 
						"' order by DP_MATCD,DP_TNKNO,DP_MEMDT";
						
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			strOTNKNO = "";
			strOMATCD = "";	        
			if(M_rstRSSET !=null)
			{			    
    			while(M_rstRSSET.next())
    			{
    		        strTNKNO = nvlSTRVL(M_rstRSSET.getString("DP_TNKNO"),"").trim();    		        
    				strMATCD = nvlSTRVL(M_rstRSSET.getString("DP_MATCD"),"").trim();
    				strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"").trim();
    				strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"").trim();    				
    				java.sql.Date datTEMP =M_rstRSSET.getDate("L_strMEMDT");		
    				if(datTEMP !=null)
                	    L_strMEMDT = M_fmtLCDAT.format(datTEMP);    				   			
    				L_fltDOPQT = M_rstRSSET.getFloat("DP_DOPQT");
                    L_fltDCLQT = M_rstRSSET.getFloat("DP_DCLQT");
    				L_fltRCTQT = M_rstRSSET.getFloat("DP_RCTQT");
    				L_fltDSPQT = M_rstRSSET.getFloat("DP_DSPQT");
    				L_fltTRNQT = M_rstRSSET.getFloat("DP_TRNQT");
    				L_fltISSQT = M_rstRSSET.getFloat("DP_ISSQT");
    				L_fltMRCQT += L_fltRCTQT;
    				L_fltMISQT += L_fltISSQT;
    				
    				if(intRECCT ==0)
    					strOMATCD = strMATCD;
    				if(strOMATCD.equals(strMATCD)) 
    				{
    					L_fltTRCQT += L_fltRCTQT;
    					L_fltTISQT += L_fltISSQT;
    				}
    				if(L_strMEMDT.equals("01" + strMEMDT.substring(2)))
    				{
    					L_fltMOPQT = L_fltDOPQT;
    				}
    				if(L_strMEMDT.equals(strMEMDT))
    				{
    				    if(strMATCD.equals(strOMATCD))
    					{
    						if(L_flgFIRST)
    						{
    							L_flgHDRFL = true;
    							L_flgFIRST = false;
    						}
    						else 
    						    L_flgHDRFL = false;
    						L_fltTOPQT +=L_fltMOPQT;
    						L_fltTCLQT +=L_fltDCLQT;
    					}
    					else
    					{
    						L_flgHDRFL =true;
    						// to print total quantity of perticular material.
    						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
    						    prnFMTCHR(dosREPORT,M_strBOLD);
    						strPRNSTR ="";
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								strPRNSTR = padSTRING('L',"",10); 
							if (M_rdbHTML.isSelected())
			                    dosREPORT.writeBytes("<b>");
							strPRNSTR += padSTRING('L',"Month Total :",36); 
			                strPRNSTR += padSTRING('L',setNumberFormat(L_fltTOPQT, 3),13) +
    						padSTRING('L',setNumberFormat(L_fltTRCQT,3),15) +
    						padSTRING('L',setNumberFormat(L_fltTISQT,3),13) +
    						padSTRING('L',setNumberFormat(L_fltTCLQT,3),17)+ "\n\n";
    						L_fltTRCQT =L_fltTISQT =L_fltTCLQT=L_fltTOPQT=0;    						
    						dosREPORT.writeBytes(strPRNSTR);    						
    						strPRNSTR="";
    						if (M_rdbHTML.isSelected())
			                    dosREPORT.writeBytes("</b>");
    						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
    						    prnFMTCHR(dosREPORT,M_strNOBOLD);				
    						L_fltTRCQT =L_fltMRCQT;
    						L_fltTISQT =L_fltMISQT;
    						L_fltTCLQT =L_fltDCLQT;
    						L_fltTOPQT =L_fltMOPQT;
    						strOMATCD = strMATCD;
    					}
    					if(!strTNKNO.equals(strOTNKNO))
    					{
    						L_strCALISS =setNumberFormat(L_fltDOPQT+L_fltRCTQT+L_fltDSPQT+L_fltTRNQT-L_fltDCLQT,3);    						
    						if(L_flgHDRFL)
    						{    
    						    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			                        strPRNSTR =padSTRING('R'," ",10);
			                    if (M_rdbHTML.isSelected())
			                        dosREPORT.writeBytes("<b>");
			                    //To print material code, name and unit of measurement    						      			                    
    							strPRNSTR += padSTRING('R',strMATCD,12) +
    										padSTRING('R',strMATDS,19) +
    										padSTRING('R',strUOMCD,3) ;
    							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
    							    prnFMTCHR(dosREPORT,M_strBOLD);	
    							dosREPORT.writeBytes(strPRNSTR+"\n\n");
    							strPRNSTR="";
    							if (M_rdbHTML.isSelected())
			                        dosREPORT.writeBytes("</b>");
    							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
    							    prnFMTCHR(dosREPORT,M_strNOBOLD);	    						
    
    						}    						
    						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			                    strPRNSTR =padSTRING('R'," ",10);
    						strPRNSTR += padSTRING('R'," ",12);    						
    						strPRNSTR +=padSTRING('R',strTNKNO,19);
    						strPRNSTR += padSTRING('L',setNumberFormat(L_fltDOPQT,3),18) +
    						padSTRING('L',setNumberFormat(L_fltRCTQT,3),15) +
    						padSTRING('L',L_strCALISS,13) +
    						padSTRING('L',setNumberFormat(L_fltDCLQT,3),17) + "\n";    				
    						strOTNKNO = strTNKNO;
    						dosREPORT.writeBytes(strPRNSTR);
    						strPRNSTR=""; 
    						//to print month data       											
    						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			                    strPRNSTR +=padSTRING('R'," ",10);
       						strPRNSTR += padSTRING('L',setNumberFormat(L_fltMOPQT,3),49); 
       						strPRNSTR +=padSTRING('L',setNumberFormat(L_fltMRCQT,3),15) +
    						padSTRING('L',setNumberFormat(L_fltMISQT,3),13) + "\n\n";
    						L_fltMRCQT =L_fltMISQT =0;    
    						dosREPORT.writeBytes(strPRNSTR);
    						strPRNSTR="";
    						}
    				}
    				intRECCT++;
    			}
    			M_rstRSSET.close();
			}
			if(intRECCT >0)
			{
                if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))			
				    prnFMTCHR(dosREPORT,M_strBOLD);
				    //to print Addition of quantity for last mterial type.
				if (M_rdbHTML.isSelected())
			        dosREPORT.writeBytes("<b>");
			    strPRNSTR=""; 	
			    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			        strPRNSTR =padSTRING('R'," ",10);
				strPRNSTR += padSTRING('L',"Month Total :",36)+
				            padSTRING('L',setNumberFormat(L_fltTOPQT,3),13) +
				            padSTRING('L',setNumberFormat(L_fltTRCQT,3),15) +
						    padSTRING('L',setNumberFormat(L_fltTISQT,3),13) +
						    padSTRING('L',setNumberFormat(L_fltTCLQT,3),17) + "\n";
						L_fltTRCQT =L_fltTISQT =L_fltTCLQT=0;
				    dosREPORT.writeBytes(strPRNSTR);
				    strPRNSTR="";
					if (M_rdbHTML.isSelected())
			            dosREPORT.writeBytes("</b>");
					strPRNSTR="";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
    				    prnFMTCHR(dosREPORT,M_strNOBOLD);				
			}
			// To print the footer part at the end of page
		    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
		        dosREPORT.writeBytes(padSTRING('R'," ",10));			
		    dosREPORT.writeBytes("-----------------------------------------------------------------------------------------------\n\n");		    
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
		        dosREPORT.writeBytes(padSTRING('R'," ",10));			
		    dosREPORT.writeBytes(padSTRING('L',"H.O.D.(M.H.D.)",85));
			setMSG("Report completed.. ",'N');
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
		  	setMSG(L_EX,"getALLREC");
		}
	}
	
/**
* Method to validate date, before execution of Query, To Check for blank input 
*and wrong selection of Memo type
*/
	boolean vldDATA()
	{
		try
		{
			if(txtMEMDT.getText().equals (" ") )			
			{
				setMSG("Please Enter Memo Date.. ",'E');				
				txtMEMDT.requestFocus();				
				return false;
			}
			if(M_fmtLCDAT.parse(txtMEMDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Memo Date can not be greater than today's date.. ",'E');				
				txtMEMDT.requestFocus();
				return false;
			}			
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{					
					setMSG("Please Select the Email/s from List by using F1 Help ..",'E');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 				
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'E');
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
	 * Method to fetch the Recent Memo Date from the Data base. 
	 */
	void getMEMDT()
	{
		try
		{
			java.sql.Date datTEMP;
			M_strSQLQRY = "SELECT MAX(DP_MEMDT)L_MEMDT from MM_DPTRN WHERE DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP ='"+strREGDP_fn+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					datTEMP = M_rstRSSET.getDate("L_MEMDT");
					if((datTEMP !=null) && ((txtMEMDT.getText().trim()).length() == 0))
						txtMEMDT.setText(M_fmtLCDTM.format(datTEMP).substring(0,10));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMEMDT");
		}
		finally
		{
			M_rstRSSET = null;
		}
	}
}
