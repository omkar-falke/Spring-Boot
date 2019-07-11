/*
System Name    : FIMS
Program Name   : Reclassification Report
Program Desc.  : User selects date, Classification type & the destination for the report as screen,printer or file.
Author         : Mr. Santosh V. Sawant
Date           : 10th July 2002
Version        : FIMS 1.0

Modifications 
Modified By    : Ms. Aditi M. Kulkarni
Modified Date  :
Version        :

*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.lang.*;
import java.util.Properties;
import java.util.Date; 
import java.io.*; 
import java.lang.Math;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.DataOutputStream; 
import java.io.*; 
import java.awt.Cursor.*;

public class fg_rprcl extends cl_rbase
{
    JRadioButton rdbREGCL,rdbRECL,rdbDOGR;  /** JRadioButton to select report type */
    ButtonGroup chkGRP;
    JOptionPane L_OPTNPN;
    
    private String strPREVDT;               /** Variable for previous date*/
	private FileOutputStream fosREPORT;     /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;     /** Data Output Stream for generating Report File */
	private String strFILNM;                /** String for generated Report File Name*/
    private int intRECCT = 0;               /** Integer for counting the records fetched from DB.*/
    
    ResultSet L_rstRSLSET1,L_rstRSLSET2;
    
    String strISODCA,strISODCB,strISODCC;   /** Variable for ISO document No. */
	String strOPRCD,strPRDCD,strLOTNO,strPKGTP,strPKGQT,strPTFNO,strPTFDT,strPTFCT[];
	String strOPRCD1,strPRDCD1,strLOTNO1,strPKGTP1,strPKGQT1;
	String strDOCNO,strCATEGORY;
	String strHEADING;
	String strMANCAT,strSUBCAT;
	
	int  intGRDTOT1,intMGTOT1, intSGTOT1, intGRPTOT1,intPACKAGES;
	double dblGRDTOT2, LM_MGTOT2, LM_SGTOT2, LM_GRPTOT2, dblNCSVL;
	boolean flgISVALID, flgRECORD, flgRECORD1, flgEQUAL, flgEMPTY, flgISDATA;
	StringBuffer strPRNSTR;
    
    String strFRMDT,strTODAT;
    String strFILNM_T = "fg_rprcl.doc";
    String strFILNM_H = "fg_rprcl.html";
    
    fg_rprcl()
    {
        super(2);
        try
        {
              setMatrix(20,8);
              L_OPTNPN = new JOptionPane();
		      chkGRP = new ButtonGroup();
    
              add(new JLabel("Report Type"),4,3,2,1,this,'L');
	          add(rdbREGCL = new JRadioButton("Regular Classification / Reclassification"),4,4,1,3,this,'L');
	          add(rdbDOGR = new JRadioButton("Downgrading"),5,4,1,1.5,this,'L');
	          chkGRP.add(rdbREGCL);
	          chkGRP.add(rdbDOGR);
	         	         	          	    
		      M_pnlRPFMT.setVisible(true);
		      getPTFCT();
		}
		catch(Exception L_EX)
        {
			setMSG (L_EX,"Constructor");
	    }
    } // end of constructor
    
    /* super class Method overrided to enhance its functionality, to enable & disable components 
       according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{
         super.setENBL(L_flgSTAT);
		 M_txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
		 M_txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		 //M_txtFMDAT.setText("28/02/2007");
		 //M_txtTODAT.setText("28/02/2007");
    }
	
	public void keyPressed(KeyEvent L_KE)   // method to handle events fired on key press
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
			    if(M_objSOURC == M_txtFMDAT)
			    {
					if(M_txtFMDAT.getText().trim().length() == 0)
						M_txtFMDAT.requestFocus();
					else
					    M_txtTODAT.requestFocus();
				}
				else if(M_objSOURC == M_txtTODAT)
				{
					if(M_txtTODAT.getText().trim().length() == 0)
					    M_txtTODAT.requestFocus();
					else
					    rdbREGCL.requestFocus();
				}
				else if(M_objSOURC == rdbREGCL)
					 rdbDOGR.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Key Pressed");
		}
	}
	
	/*	Method to print, display report as per selection   */
	void exePRINT()
	{
		if (!vldDATA())
		{	
		    setMSG("Please Enter Valid Dates to generate the report..",'N');
			return;
		}
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + strFILNM_H;
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + strFILNM_T;
				
		    fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT  = 0 ;
	        cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
   		    if(M_rdbHTML.isSelected())
		    {
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Classification Report</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
		    }
		    else
   		        prnFMTCHR(dosREPORT,M_strNOCPI17);
   		        prnFMTCHR(dosREPORT,M_strCPI12);
				
			if(rdbDOGR.isSelected())        // Down Grading
			{	
			    flgISDATA = false;
				strHEADING = "Reclassification(Down Grading) report";
				strCATEGORY = strPTFCT[2].substring(4);
				getISODCN("FG_RPRCL");  //gets the ISO No. for this particular report
				getALLREC1();           //fetches all the records for Down Grading
				if(flgRECORD) 
					flgISDATA = true;
			}
			else
			{
			    flgISDATA = false;
				strHEADING = "Classification report";
				getISODCN("FG_RPPTF");     //gets the ISO No. for this particular report
				// Classification	
				strCATEGORY = strPTFCT[0].substring(4);
				getALLREC(strCATEGORY);    //Fetches all the records for Classification
				if(flgRECORD) 
					flgISDATA = true;
						
				cl_dat.M_intLINNO_pbst = 69;
				// Reclassification
				strCATEGORY = strPTFCT[1].substring(4);
				getALLREC(strCATEGORY);    //Fetches all the records for Reclassification
				if(flgRECORD)
				    flgISDATA = true;
				//dosREPORT.close();
			}				
			
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
				{
				    doPRINT(strFILNM);
				}   
				else 
			    {   
			        Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+ strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			     Runtime r = Runtime.getRuntime();
				 Process p = null;					
			     if(M_rdbHTML.isSelected())
			     {
			        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM);
			     }
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Classification Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}	
	}
	
	/*  Method to validate Product Type, Market Type, Sale Type  */	        
    boolean vldDATA()
	{
	    try
	    {	
	        //System.out.println("In vldDATA");    
    	    String strFMDAT = M_txtFMDAT.getText().trim();
    		String strTODAT = M_txtTODAT.getText().trim();
    		
    	    if(M_txtFMDAT.getText().trim().length()== 0)
        	{
            	setMSG("Please Enter From Date ..",'E');
            	M_txtFMDAT.requestFocus();
            	return true;
        	}
        	if(M_txtTODAT.getText().trim().length()==0)
        	{
            	setMSG("Please Enter To Date ..",'E');
            	M_txtTODAT.requestFocus();
            	return true;
        	}
        	if(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))>0)
    	    {
                setMSG("To Date cannot be Less than From Date ..",'E');
                M_txtFMDAT.requestFocus();
                return false;
    	    }
    	    if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
        	{
                setMSG(" To Date cannot be greater than today's date..",'E');
                M_txtTODAT.requestFocus();
                return false;
        	}
        }
	    catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	
	private void getALLREC(String LP_CAT)		// fetches all the records from FG_PTFRF
	{
	   	try
	   	{
	   	    cl_dat.M_PAGENO = 1;
   		    
			M_strSQLQRY = "Select count(*) CNT from FG_PTFRF,CO_CDTRN";
			M_strSQLQRY += " where PTF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PTF_PTFDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()));
			M_strSQLQRY += "' and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'";
			M_strSQLQRY += " and PTF_PTFCT = '" + LP_CAT + "'";
			M_strSQLQRY += " and PTF_PKGTP = CMT_CODCD and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null && M_rstRSSET.next())
			{	
				if(M_rstRSSET.getInt("CNT") > 0)
				{	
   					prnHEADER();
   					getTBLHDR();
				}
				else 
					return;
			}	
			System.out.println("Inside");
			flgRECORD = false;
			
			M_strSQLQRY = "Select PTF_PRDTP,PTF_RCLNO,PTF_LOTNO,PTF_PRDCD,PTF_OPRCD,";
			M_strSQLQRY += "PTF_PKGTP,PTF_PTFNO,PTF_PTFDT,sum(PTF_PTFQT) strPKGQT,";
			M_strSQLQRY += "CMT_SHRDS,CMT_NCSVL from FG_PTFRF,CO_CDTRN";
			M_strSQLQRY += " where PTF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PTF_PTFDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()));
			M_strSQLQRY += "' and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'";
			M_strSQLQRY += " and PTF_PTFCT = '" + LP_CAT + "'";
			M_strSQLQRY += " and PTF_PKGTP = CMT_CODCD and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG'";
			M_strSQLQRY += " group by PTF_PRDTP,PTF_RCLNO,PTF_LOTNO,PTF_PRDCD,PTF_OPRCD,";
			M_strSQLQRY += " PTF_PKGTP,PTF_PTFNO,PTF_PTFDT,CMT_SHRDS,CMT_NCSVL";
			M_strSQLQRY += " order by  PTF_PRDCD,PTF_OPRCD,PTF_LOTNO,PTF_PKGTP,PTF_PRDTP,PTF_RCLNO,PTF_PTFNO";
			//System.out.println("getALLREC query =" + M_strSQLQRY);
			L_rstRSLSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			strPRNSTR = new StringBuffer("");
			strOPRCD1 = strPRDCD1 = strLOTNO1 = strPKGTP1 = strPKGQT1 = "";
			
			// Initialises all the totals
			intGRDTOT1 = intMGTOT1 = intSGTOT1 = intGRPTOT1 = 0;
			dblGRDTOT2 = LM_MGTOT2 = LM_SGTOT2 = LM_GRPTOT2 = 0;
									
			while(L_rstRSLSET1.next())
			{
				if(!flgRECORD)
				   flgRECORD = true;
				strPRNSTR = new StringBuffer("");
				chkRPTHDR(68);
				// Get the data into the local variables
				strOPRCD = nvlSTRVL(L_rstRSLSET1.getString("PTF_OPRCD"),"").trim();
				strPRDCD = nvlSTRVL(L_rstRSLSET1.getString("PTF_PRDCD"),"").trim();
				strLOTNO = nvlSTRVL(L_rstRSLSET1.getString("PTF_LOTNO"),"").trim();
				strPKGTP = nvlSTRVL(L_rstRSLSET1.getString("CMT_SHRDS"),"").trim();
				String strTMPPKG1 = nvlSTRVL(L_rstRSLSET1.getString("strPKGQT"),"").trim();
				strPKGQT = setNumberFormat(Double.parseDouble(strTMPPKG1),3);
				strPTFNO = nvlSTRVL(L_rstRSLSET1.getString("PTF_PTFNO"),"").trim();
				strPTFDT = nvlSTRVL(M_fmtLCDAT.format(L_rstRSLSET1.getDate("PTF_PTFDT")).toString(),"");
				
				// If grade changes
				flgEQUAL = strPRDCD1.equals(strPRDCD);
				if(strCATEGORY.equals(strPTFCT[1].substring(4)))
				{
					flgEQUAL = strOPRCD1.equals(strOPRCD);
				}
								
				if(!flgEQUAL)
				{	
					// If grade is not empty
					flgEMPTY = strPRDCD1.equals("");
					if(strCATEGORY.equals(strPTFCT[1].substring(4)))
					{
						flgEMPTY = strOPRCD1.equals("");
					}
					if(!flgEMPTY)
					{	
						chkRPTHDR(64);
					    prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(padSTRING('L'," ",17) + padSTRING('R',"Total: ",23));
						dosREPORT.writeBytes(padSTRING('L',String.valueOf(intGRPTOT1),8) + padSTRING('L',setNumberFormat(LM_GRPTOT2,3),12) + "\n");
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n");
						
						cl_dat.M_intLINNO_pbst += 2;
						
						intSGTOT1 = intSGTOT1 + intGRPTOT1;
						LM_SGTOT2 = LM_SGTOT2 + LM_GRPTOT2;
						
						// Check for subcategory
						if(!strPRDCD1.substring(0,4).equals(strPRDCD.substring(0,4)))
						{
							intMGTOT1 = intMGTOT1 + intSGTOT1;
							LM_MGTOT2 = LM_MGTOT2 + LM_SGTOT2;
							prnFMTCHR(dosREPORT,M_strBOLD);
							chkRPTHDR(64);
							dosREPORT.writeBytes(padSTRING('L'," ",17) + padSTRING('R',"Total " + strSUBCAT + ": ",23));
							dosREPORT.writeBytes(padSTRING('L',String.valueOf(intSGTOT1),8) + padSTRING('L',setNumberFormat(LM_SGTOT2,3),12) + "\n");
							prnFMTCHR(dosREPORT,M_strNOBOLD);
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 2;

							intSGTOT1 = 0;
							LM_SGTOT2 = 0;
													
							// Check for main category
							if(!strPRDCD1.substring(0,2).equals(strPRDCD.substring(0,2)))
							{   
								chkRPTHDR(68);
							    prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes(padSTRING('L'," ",17) + padSTRING('R',"Total " + strMANCAT + ": ",23));
								dosREPORT.writeBytes(padSTRING('L',String.valueOf(intMGTOT1),8) + padSTRING('L',setNumberFormat(LM_MGTOT2,3),12) + "\n");								
								prnFMTCHR(dosREPORT,M_strNOBOLD);
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst += 2;
								
								
								intGRDTOT1 = intGRDTOT1 + intMGTOT1;
								dblGRDTOT2 = dblGRDTOT2 + LM_MGTOT2;
								
								intMGTOT1 = 0;
								LM_MGTOT2 = 0;
								
								strMANCAT = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",strPRDCD.substring(0,2)+ "0000000A");
								chkRPTHDR(68);
								prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes(emptySTR(strMANCAT,"--") + "\n");
								prnFMTCHR(dosREPORT,M_strNOBOLD);
								cl_dat.M_intLINNO_pbst++;
								
								strSUBCAT = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",strPRDCD.substring(0,4)+ "00000A");
								prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes(padSTRING('R'," ",4) + padSTRING('R',emptySTR(strSUBCAT,"--"),10) + "\n");
								prnFMTCHR(dosREPORT,M_strNOBOLD);
								cl_dat.M_intLINNO_pbst++;
							}
							else
							{
								//chkRPTHDR(68);
								strSUBCAT = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",strPRDCD.substring(0,4)+ "00000A");
								prnFMTCHR(dosREPORT,M_strBOLD);
								dosREPORT.writeBytes(padSTRING('R'," ",4) + padSTRING('R',emptySTR(strSUBCAT,"--"),10) + "\n");
								prnFMTCHR(dosREPORT,M_strNOBOLD);
								cl_dat.M_intLINNO_pbst++;
							}
						}
						intGRPTOT1 = 0;
						LM_GRPTOT2 = 0;
					}
					else				// initial	// Check for Main category		
					{	
						//chkRPTHDR(68);
						strMANCAT = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",strPRDCD.substring(0,2)+ "0000000A");
						prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(emptySTR(strMANCAT,"--") + "\n");
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						
						cl_dat.M_intLINNO_pbst++;
						
						// Check for subcategory
						strSUBCAT = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXPGR",strPRDCD.substring(0,4)+ "00000A");
						prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(padSTRING('R'," ",4) + padSTRING('R',emptySTR(strSUBCAT,"--"),10) + "\n");
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						
						cl_dat.M_intLINNO_pbst++;
					}
					
					strOPRCD1 = strOPRCD;
					strPRDCD1 = strPRDCD;
					
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('R'," ",8) + emptySTR(getPRDDS(strPRDCD),"--"));
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					
					if(strCATEGORY.equals(strPTFCT[1].substring(4)))    // Reclassification
					{					
						//chkRPTHDR(68);
						dosREPORT.writeBytes("\n"+padSTRING('R'," ",8)+"(From:" + emptySTR(getPRDDS(strOPRCD),"--") + " - RECLASSIFICATION)");
						dosREPORT.writeBytes("\n");
					}
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst++;
				}
							
				dblNCSVL = Double.parseDouble(nvlSTRVL(L_rstRSLSET1.getString("CMT_NCSVL"),"0").trim());
				if(dblNCSVL != 0)
					intPACKAGES = (int)Math.round(Double.parseDouble(strPKGQT)/dblNCSVL);
				else
					intPACKAGES = 0;
				
				intGRPTOT1 = intGRPTOT1 + intPACKAGES;
				LM_GRPTOT2 = LM_GRPTOT2 + Double.parseDouble(strPKGQT);
								
				strPRNSTR.append(padSTRING('L'," ",17) + padSTRING('R',emptySTR(strLOTNO,"--"),12));
				strPRNSTR.append(padSTRING('R',emptySTR(strPKGTP,"--"),11));
				strPRNSTR.append(padSTRING('L',emptySTR(String.valueOf(intPACKAGES),"--"),8));
				strPRNSTR.append(padSTRING('L',emptySTR(strPKGQT,"--"),12));
				strPRNSTR.append(padSTRING('L',emptySTR(strPTFNO,"--"),8) + padSTRING('L'," ",2));
				strPRNSTR.append(padSTRING('L',emptySTR(strPTFDT,"--"),10) + "\n");
				
				cl_dat.M_intLINNO_pbst++;
				chkRPTHDR(68);
				dosREPORT.writeBytes(strPRNSTR.toString());
			}
			intRECCT = 1;
		
			if(flgRECORD)
			{
				// Print the total for the last record
				flgEMPTY = strPRDCD1.equals("");
				if(strCATEGORY.equals(strPTFCT[1].substring(4)))
				{
					flgEMPTY = strOPRCD1.equals("");
				}
				
				if(!flgEMPTY)
				{
					cl_dat.M_intLINNO_pbst++;
					//chkRPTHDR(68);
					
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('L'," ",17) + padSTRING('R',"Total: ",23));
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(intGRPTOT1),8) + padSTRING('L',setNumberFormat(LM_GRPTOT2,3),12) + "\n");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes("\n");			
					cl_dat.M_intLINNO_pbst += 2;
					
					// Total for Sub Category
					intSGTOT1 = intSGTOT1 + intGRPTOT1;
					LM_SGTOT2 = LM_SGTOT2 + LM_GRPTOT2;
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('L'," ",17) + padSTRING('R',"Total " + strSUBCAT + ": ",23));
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(intSGTOT1),8) + padSTRING('L',setNumberFormat(LM_SGTOT2,3),12) + "\n");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 2;
					
					// Total for Main Category
					intMGTOT1 = intMGTOT1 + intSGTOT1;
					LM_MGTOT2 = LM_MGTOT2 + LM_SGTOT2;
					prnFMTCHR(dosREPORT,M_strBOLD);
					chkRPTHDR(68);
					dosREPORT.writeBytes(padSTRING('L'," ",17) + padSTRING('R',"Total " + strMANCAT + ": ",23));
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(intMGTOT1),8) + padSTRING('L',setNumberFormat(LM_MGTOT2,3),12) + "\n");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 2;
					
					intGRDTOT1 = intGRDTOT1 + intMGTOT1;
					dblGRDTOT2 = dblGRDTOT2 + LM_MGTOT2;
										
					cl_dat.M_intLINNO_pbst++;
					prnCREPFTR();
				}
			}
			else
			{
				setMSG("Record not found",'E');
			}
			setMSG("",'N');
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	
	private void chkRPTHDR(int LP_LINLMT)
	{
		try
		{
			if(cl_dat.M_intLINNO_pbst > LP_LINLMT)
			{
				prnFOOTR();
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				prnHEADER();    //gets the Header of the report
				getTBLHDR();    // gets the Header of the Table
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	
	private void getALLREC1()
	{
		try
		{
   		    cl_dat.M_PAGENO = 1;
   		    
		    prnHEADER();
   		    getTBLHDR();
			flgRECORD = false;
			
			// SELECT FROM FG_PTFRF
			M_strSQLQRY =  "Select PTF_PRDTP,PTF_PRDCD,PTF_LOTNO,PTF_PKGTP,PTF_PTFQT,PTF_PTFRF,PTF_PTFDT,";
			M_strSQLQRY += "PTF_PTFNO,CMT_SHRDS,CMT_NCSVL from FG_PTFRF,CO_CDTRN";
			M_strSQLQRY += " where PTF_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PTF_PTFDT between '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()));
			M_strSQLQRY += "' and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'";
			M_strSQLQRY += " and PTF_PTFCT = '" + strPTFCT[2].substring(4) + "'";
			M_strSQLQRY += " and PTF_PKGTP = CMT_CODCD and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG' ";
			M_strSQLQRY += " order by  PTF_PTFDT,PTF_PTFRF";          
			//System.out.println("DWGRD =" + M_strSQLQRY);
			L_rstRSLSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			// Initialises all the totals
			intGRDTOT1 = 0;
			dblGRDTOT2 = 0;
		    
			while(L_rstRSLSET1.next())
			{
				if(!flgRECORD)
				   flgRECORD = true;
				strPRNSTR = new StringBuffer("");
					
				chkRPTHDR(65);
				// Get the data into the local variables
				strDOCNO = nvlSTRVL(L_rstRSLSET1.getString("PTF_PTFRF"),"").trim();
				strPTFNO = nvlSTRVL(L_rstRSLSET1.getString("PTF_PTFNO"),"").trim();
				
				getDOCDET(strDOCNO);	// Get the records from FG_ISTRN related to strDOCNO
				chkRPTHDR(65);
				dosREPORT.writeBytes("PTF No. :  ");
				dosREPORT.writeBytes(padSTRING('R',strPTFNO,12));
				dosREPORT.writeBytes("Date : ");
				dosREPORT.writeBytes(nvlSTRVL(M_fmtLCDAT.format(L_rstRSLSET1.getDate("PTF_PTFDT")).toString(),"") + "\n");
				dosREPORT.writeBytes("\n");
				
				cl_dat.M_intLINNO_pbst ++;
				
				dosREPORT.writeBytes(padSTRING('L'," ",0) + "From:\n");
				cl_dat.M_intLINNO_pbst ++;				
				
				
				strPRDCD1 = "";
				intGRPTOT1 = 0;
				LM_GRPTOT2 = 0;
				
				while(L_rstRSLSET2.next())
				{
					if(!flgRECORD1)
					    flgRECORD1 = true;
					strPRDCD = nvlSTRVL(L_rstRSLSET2.getString("IST_PRDCD"),"").trim();
					strLOTNO = nvlSTRVL(L_rstRSLSET2.getString("IST_LOTNO"),"").trim();
					strPKGTP = nvlSTRVL(L_rstRSLSET2.getString("CMT_SHRDS"),"").trim();
					String strTMPPKG2 = nvlSTRVL(L_rstRSLSET2.getString("strPKGQT"),"").trim();
					strPKGQT = setNumberFormat(Double.parseDouble(strTMPPKG2),3);
					
					// If grade changes
					if(!strPRDCD.equals(strPRDCD1))
					{
						if(!strPRDCD1.equals(""))  // not initial
						{	
							cl_dat.M_intLINNO_pbst += 2;
							chkRPTHDR(65);
                            prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes(padSTRING('L'," ",23) + padSTRING('R',"Total " + " : ",27));
							dosREPORT.writeBytes(padSTRING('L',String.valueOf(intGRPTOT1),8) + padSTRING('L',setNumberFormat(LM_GRPTOT2,3),13) + "\n\n");
							prnFMTCHR(dosREPORT,M_strNOBOLD);
							cl_dat.M_intLINNO_pbst += 2;
							intMGTOT1 += intGRPTOT1;
							LM_MGTOT2 += LM_GRPTOT2;
							intGRPTOT1 = 0;
							LM_GRPTOT2 = 0;
						}
						
						strPRDCD1 = strPRDCD;
						dosREPORT.writeBytes(padSTRING('R'," ",11) + emptySTR(getPRDDS(strPRDCD1),"--") + "\n");
						cl_dat.M_intLINNO_pbst ++;
					}
					
					dblNCSVL = Double.parseDouble(nvlSTRVL(L_rstRSLSET2.getString("CMT_NCSVL"),"0").trim());
					if(dblNCSVL != 0)
						intPACKAGES = (int)Math.round(Double.parseDouble(strPKGQT)/dblNCSVL);
					else
						intPACKAGES = 0;
					
					intGRPTOT1 += intPACKAGES;
					LM_GRPTOT2 += Double.parseDouble(strPKGQT);
					
					chkRPTHDR(65);
					dosREPORT.writeBytes(padSTRING('R'," ",23) + padSTRING('R',emptySTR(strLOTNO,"--"),8) + padSTRING('R'," ",7));
					dosREPORT.writeBytes(padSTRING('R',emptySTR(strPKGTP,"--"),12));
					dosREPORT.writeBytes(padSTRING('L',emptySTR(String.valueOf(intPACKAGES),"--"),8));
					dosREPORT.writeBytes(padSTRING('L',emptySTR(strPKGQT,"--"),13) + "\n");
					cl_dat.M_intLINNO_pbst ++;
				}
				
				// print the total of the last record
				if(flgRECORD1)
				{
					cl_dat.M_intLINNO_pbst += 2;
					chkRPTHDR(65);
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('L'," ",23) + padSTRING('R',"Total " + " : ",27));
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(intGRPTOT1),8) + padSTRING('L',setNumberFormat(LM_GRPTOT2,3),13) + "\n\n");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					
					cl_dat.M_intLINNO_pbst += 2;

					intMGTOT1 += intGRPTOT1;
					LM_MGTOT2 += LM_GRPTOT2;
					chkRPTHDR(65);
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('L'," ",23) + padSTRING('R',"PTF Total " + " : ",27));
					dosREPORT.writeBytes(padSTRING('L',String.valueOf(intMGTOT1),8) + padSTRING('L',setNumberFormat(LM_MGTOT2,3),13) + "\n");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					
					cl_dat.M_intLINNO_pbst ++;
					intGRPTOT1 = intMGTOT1 = 0;
					LM_GRPTOT2 = LM_MGTOT2 = 0;
				}
				
				
				dosREPORT.writeBytes(padSTRING('L'," ",0) + "To:" + "\n");
				cl_dat.M_intLINNO_pbst ++;				
				
				strPRDCD = nvlSTRVL(L_rstRSLSET1.getString("PTF_PRDCD"),"").trim();
				strLOTNO = nvlSTRVL(L_rstRSLSET1.getString("PTF_LOTNO"),"").trim();
				strPKGTP = nvlSTRVL(L_rstRSLSET1.getString("CMT_SHRDS"),"").trim();
				String strTMPPKG3 = nvlSTRVL(L_rstRSLSET1.getString("PTF_PTFQT"),"").trim();
				strPKGQT = setNumberFormat(Double.parseDouble(strTMPPKG3),3);
				strPTFNO = nvlSTRVL(L_rstRSLSET1.getString("PTF_PTFNO"),"").trim();
				dblNCSVL = Double.parseDouble(nvlSTRVL(L_rstRSLSET1.getString("CMT_NCSVL"),"0").trim());
				if(dblNCSVL != 0)
					intPACKAGES = (int)Math.round(Double.parseDouble(strPKGQT)/dblNCSVL);
				else
					intPACKAGES = 0;
				
				intGRDTOT1 += intPACKAGES;
				dblGRDTOT2 += Double.parseDouble(strPKGQT);
				
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('R'," ",11) + emptySTR(getPRDDS(strPRDCD),"--") + "\n");
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				
				cl_dat.M_intLINNO_pbst++;
				chkRPTHDR(65);

				dosREPORT.writeBytes(padSTRING('R'," ",23) + padSTRING('R',emptySTR(strLOTNO,"--"),8) + padSTRING('R'," ",7));
				dosREPORT.writeBytes(padSTRING('R',emptySTR(strPKGTP,"--"),12));
				dosREPORT.writeBytes(padSTRING('L',emptySTR(String.valueOf(intPACKAGES),"--"),8));
				dosREPORT.writeBytes(padSTRING('L',emptySTR(strPKGQT,"--"),13) + "\n\n");
				
				cl_dat.M_intLINNO_pbst++;
			}
			intRECCT = 1;
					
			if(flgRECORD)
			{
				chkRPTHDR(65);
				dosREPORT.writeBytes("\n");
				crtLINE(80);
				dosREPORT.writeBytes("\n");
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('L'," ",23) + padSTRING('R',"Grand Total : ",27) + padSTRING('L',String.valueOf(intGRDTOT1),8) + padSTRING('L',setNumberFormat(dblGRDTOT2,3),13) + "\n");				
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				crtLINE(80);
				prnDREPFTR();
			}
			if(L_rstRSLSET1 != null)
				L_rstRSLSET1.close();
			setMSG("",'N');
					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC1");
		}
	}
	
	private void getISODCN(String LP_REPNM)
	{ 
		try
		{
		   	strISODCA = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT",LP_REPNM + "01");
			strISODCB = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT",LP_REPNM + "02");
			strISODCC = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT",LP_REPNM + "03");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getISODCN");
		}
	}
	
	private void prnHEADER()         //gets the Header for Report
	{
		try
		{
		    if(M_rdbTEXT.isSelected())
		    {
    		    prnFMTCHR(dosREPORT,M_strNOCPI17);
    		    prnFMTCHR(dosREPORT,M_strCPI12);
    			dosREPORT.writeBytes(padSTRING('R',"",48));
    			crtLINE(30);
    			dosREPORT.writeBytes("\n" + padSTRING('R'," ",50) + strISODCA);
    			dosREPORT.writeBytes("\n" + padSTRING('R'," ",50) + strISODCB);
    			dosREPORT.writeBytes("\n" + padSTRING('R'," ",50) + strISODCC);
    			dosREPORT.writeBytes("\n" + padSTRING('R',"",48));
    			crtLINE(30);
    			dosREPORT.writeBytes("\n\n" + padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
    			dosREPORT.writeBytes(padSTRING('L',"Report Date :" + cl_dat.M_strLOGDT_pbst,55) + "\n");
    			dosREPORT.writeBytes(strHEADING + "\n");
    			dosREPORT.writeBytes(padSTRING('R',"Period : " + M_txtFMDAT.getText().trim() + " to " + M_txtTODAT.getText().trim(),53));			
    			dosREPORT.writeBytes(padSTRING('L',"Page No. ",12) + padSTRING('L',String.valueOf(cl_dat.M_PAGENO),3) + "\n");
    			cl_dat.M_intLINNO_pbst = 9;
		    }
		    else
		    {
		        dosREPORT.writeBytes(padSTRING('R',"",48));
    			crtLINE(30);
    			dosREPORT.writeBytes("\n" + padSTRING('R'," ",50) + strISODCA);
    			dosREPORT.writeBytes("\n" + padSTRING('R'," ",50) + strISODCB);
    			dosREPORT.writeBytes("\n" + padSTRING('R'," ",50) + strISODCC);
    			dosREPORT.writeBytes("\n" + padSTRING('R',"",48));
    			crtLINE(30);
    			dosREPORT.writeBytes("\n\n" + padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
    			dosREPORT.writeBytes(padSTRING('L',"Report Date :" + cl_dat.M_strLOGDT_pbst,55) + "\n");
    			dosREPORT.writeBytes(strHEADING + "\n");
    			dosREPORT.writeBytes(padSTRING('R',"Period : " + M_txtFMDAT.getText().trim() + " to " + M_txtTODAT.getText().trim(),53));			
    			dosREPORT.writeBytes(padSTRING('L',"Page No. ",12) + padSTRING('L',String.valueOf(cl_dat.M_PAGENO),3) + "\n");
    			cl_dat.M_intLINNO_pbst = 9; 
    		}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	
	private void crtLINE(int LP_CNT)    // method to print lines in report
	{
		String strln = "";
		try
		{
		    for(int i=1;i<=LP_CNT;i++)
			{
			  strln += "-";
			}
			dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtLINE");
		}
	}
	
	private void prnFOOTR()
	{
		try
		{
		    dosREPORT.writeBytes ("\n");
			crtLINE(80);
			dosREPORT.writeBytes ("\n");
			prnFMTCHR(dosREPORT,M_strCPI10);
			prnFMTCHR(dosREPORT,M_strEJT);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
	
	/** method to print footer for classification report */
	private void prnCREPFTR()
	{
		try
		{
			crtLINE(80);
			dosREPORT.writeBytes("\n");
			if(strCATEGORY.equals(strPTFCT[0].substring(4)))
			    	dosREPORT.writeBytes(padSTRING('R',"Grand Total: ",40) + padSTRING('L',String.valueOf(intGRDTOT1),8) + padSTRING('L',setNumberFormat(dblGRDTOT2,3),12) + "\n");
			    else	
				    dosREPORT.writeBytes(padSTRING('R',"Grand Total(Reclassification): ",40) + padSTRING('L',String.valueOf(intGRDTOT1),8) + padSTRING('L',setNumberFormat(dblGRDTOT2,3),12) + "\n");	
		    crtLINE(80);
			dosREPORT.writeBytes("\n\n\n\n");
			dosREPORT.writeBytes("      CE(O)                                                      AGM(QCA)\n");
			crtLINE(80);
			dosREPORT.writeBytes("\n Note: This is a computer generated report, hence signature not required.");
			prnFMTCHR(dosREPORT,M_strNOCPI17);	
			prnFMTCHR(dosREPORT,M_strEJT);
			cl_dat.M_intLINNO_pbst = 0;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnCREPFTR");
		}
	}
	
	/** method to print footer for downgrading report */
	private void prnDREPFTR()
	{
		try
		{
			dosREPORT.writeBytes("\n\n\n\n");
			dosREPORT.writeBytes("      HOD(MHD)                                                      CE(O)");
			dosREPORT.writeBytes("\n      cc: MHD/COMM/CE(O)");
			prnFMTCHR(dosREPORT,M_strEJT);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnDREPFTR");
		}
	}

	// Print the Table header for Regular & Reclassification report
	private void getTBLHDR()
	{
		try
		{
			crtLINE(80);
			if(!rdbDOGR.isSelected())
			{
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("        Classified");
				cl_dat.M_intLINNO_pbst++;
			}
			if(rdbDOGR.isSelected())
				dosREPORT.writeBytes("\n           Grade       Lot No.        Pkg.Weight      Bags          Qty\n");
			else
				dosREPORT.writeBytes("\n        Grade    Lot No.     Pkg.Weight     Bags         Qty  PTF No  PTF Date\n");
			
			crtLINE(80);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 4;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTBLHDR");
		}
	}
	
	// Method to convert date from yyyy-mm-dd to dd/mm/yyyy
	public  String setDDMMYYYY(String LP_DATSTR)
	{
		try
		{
			if(LP_DATSTR != null)
			{
			    if(LP_DATSTR.trim().length() == 10)
			    {
					LP_DATSTR = LP_DATSTR.substring(8)+ "/"+ LP_DATSTR.substring(5,7)+"/"+LP_DATSTR.substring(0,4);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setDDMMYYYY");					
		}
		return LP_DATSTR;
	}
	
	
	// Method to replace empty string with '--' (dash)
	public String emptySTR(String LP_VARVL,String LP_DEFVL)
	{
		try
		{
			if(LP_VARVL == null || LP_VARVL.trim().length() == 0)
				LP_VARVL = LP_DEFVL;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"emptySTR");
		}
		return LP_VARVL;
	}
	
	// Method to replace empty date with '--' (dash)
	public String emptyDT(String LP_VARVL,String LP_DEFVL)
	{
		try
		{
			if(LP_VARVL.toString().equals("30/12/1899") || LP_VARVL.toString().trim().length() == 0)
				LP_VARVL = LP_DEFVL;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"emptyDT");
		}
		return LP_VARVL;
	}
	
	// Method to get PTFCT
	private void getPTFCT()
	{
	    int i = 0;
		ResultSet L_RSLSET;
		strPTFCT = new String[3];
		
		M_strSQLQRY ="select CMT_CODCD from CO_CDTRN where CMT_CGMTP = 'SYS' and ";
		M_strSQLQRY += "CMT_CGSTP = 'FGXXCAT' and ";
		M_strSQLQRY += " SUBSTRING(CMT_CODCD,1,3) = 'PTF' order by CMT_CODCD";
		//System.out.println("getPTFCT query = " + M_strSQLQRY);
		try
		{
			L_RSLSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			while(L_RSLSET.next())
			{
				strPTFCT[i] = L_RSLSET.getString("CMT_CODCD");
				i++;
			}
			
			if(L_RSLSET !=null)
				L_RSLSET.close();
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPTFCT");
		}
	}
	
	// Method to get Product Description
	private String getPRDDS(String LP_PRDCD)
	{
		String L_PRDDS = "";
		ResultSet L_RSLSET;
		M_strSQLQRY ="select PR_PRDDS from CO_PRMST where ";
		M_strSQLQRY += "PR_PRDCD = '" + LP_PRDCD +"'";
		
		try
		{
			L_RSLSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_RSLSET.next())
				L_PRDDS = padSTRING('R',L_RSLSET.getString("PR_PRDDS"),20) + LP_PRDCD;
						
			if(L_RSLSET !=null)
				L_RSLSET.close();
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDDS");
		}
		return L_PRDDS;
	}
	
	private void getDOCDET(String LP_DOCNO)
	{
		try
		{
			M_strSQLQRY =  "Select IST_PRDCD,IST_LOTNO,IST_PKGTP,CMT_SHRDS,CMT_NCSVL,sum(IST_ISSQT) strPKGQT";
			M_strSQLQRY += " from FG_ISTRN,CO_CDTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSNO = '"+ LP_DOCNO +"'";
			M_strSQLQRY += " and IST_PKGTP = CMT_CODCD and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG' ";
			M_strSQLQRY += " group by IST_PRDCD,IST_LOTNO,IST_PKGTP,CMT_SHRDS,CMT_NCSVL";  
			M_strSQLQRY += " order by IST_PRDCD,IST_LOTNO";          
			
			L_rstRSLSET2 = cl_dat.exeSQLQRY2(M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDOCDET");
		}
	}
    
} // end of class