/*
System Name   : Finished Goods Inventory Management System
Program Name  : Transfer Entry Reports
Program Desc. : Provides a Statement which gives information regarding Ware House Transfer and ReBagging.
Author        : Mr. Deepal N. Mehta
Date          : 16th July 2002
Version       : FIMS 1.0

Modifications 
Modified By   : Ms. A M. Kulkarni
Modified Date : 16 February 2007
Version       :

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
FG_ISTRN     IST_WRHTP,IST_ISSTP,IST_ISSNO                             #
PR_LTMST     LT_PRDTP,LT_LOTNO,LT_RCLNO                                #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtWRHTP    IST_WRHTP      FG_ISTRN      VARCHAR(1)     WareHouse Type
--------------------------------------------------------------------------------------
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
import java.util.*;
import java.awt.Cursor.*;

class fg_rpwht extends cl_rbase
{
    JTextField txtWRHTP;                 /** JTextField to display WareHouse Type*/
	JLabel lblWRHTP,lblREPTP;            /** JLabel for WareHouse Type & Report Type.*/
	JRadioButton rdbWHTFR,rdbREBAG;      /** Radio Buttons for W/H Transfer & Rebagging.*/
	JOptionPane L_strOPTNPN;
	ButtonGroup chkGRP;
	private String strPREVDT;            /** Variable for previous date*/
	private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
	private String strFILNM;             /** String for generated Report File Name*/
    private int intRECCT = 0;            /** Integer for counting the records fetched from DB.*/
	
	String L_strWRHTP,L_strLOTNO,L_strPRDCD,L_strFRMDT,L_strTODAT,L_strEMLID,L_strDOCNO,L_strSBGRP;
    String L_strTRNDT,L_strMKTTP,L_strMNLCD,L_strPKGTP,L_strTRNTP,L_strCODDS,L_strSBPRD,L_strMNGRP;
	String L_strTRNSP,L_strSALDS,L_strMGRDS,L_strSGRDS,L_strISODCA,L_strISODCB,L_strISODCC,L_strHDRSTR;
	String L_strMNPRD,L_strPRDDS,L_strPKGDS;
		
	String L_strMNGRP1 = "";
	String L_strSBGRP1 = "";
	String L_strTRNDT1 = "";
	String L_strPRDCD1 = "";
	String L_strDOCNO1 = "";
	String L_strLOTNO1 = "";
	String L_strMNLCD1 = "";
	String L_strPKGTP1 = "";
	String L_strTRNTP1 = "";
			
	String L_strMNGRP2 = "";
	String L_strSBGRP2 = "";
	String L_strTRNDT2 = "";
	String L_strPRDCD2 = "";
	String L_strDOCNO2 = "";
	String L_strLOTNO2 = "";
	String L_strMNLCD2 = "";
	String L_strPKGTP2 = "";
	String L_strTRNTP2 = "";
			
	String L_strTORMGQT = "0";
	String L_strTORSGQT = "0";
	String L_strTORPRQT = "0";
	String L_strTORLTQT = "0";
	String L_strTORGTQT = "0";
	String L_strTORDTQT = "0";
			
	String L_strTOIMGQT = "0";
	String L_strTOISGQT = "0";
	String L_strTOIPRQT = "0";
	String L_strTOILTQT = "0";
	String L_strTOIGTQT = "0";
	String L_strTOIDTQT = "0";
	
	String LM_ISHTST = "";
	String LM_RCHTST = "";
	
	StringBuffer L_PRNSTR;
	Hashtable<String,String> L_IHSTBL,L_RHSTBL;
	
	double L_dblTRNQT = 0;
	
	int L_intCNT = 0;	
    int L_intLMRGN = 0;	
	int L_intISSCNT = 0,L_intRCTCNT = 0,  intDTCOUNT = 0, intDT1COUNT = 0;
	
	boolean L_flgDTLFL = true;
	
	fg_rpwht()
    {
		super(2);
		try
		{
		        setMatrix(20,8);
		        L_strOPTNPN = new JOptionPane();
		        chkGRP = new ButtonGroup();
		   	    add(new JLabel("WareHouse Type"),6,4,1,1,this,'L');
		   	    add(txtWRHTP = new TxtLimit(5),6,5,1,1,this,'L');
			    
			    add(new JLabel("Report Type"),7,4,1,.8,this,'L');
			    add(rdbWHTFR = new JRadioButton("WareHouse Transfer",true),7,5,1,1.5,this,'L');
	            add(rdbREBAG = new JRadioButton("Rebagging"),8,5,1,1.5,this,'L');
	            
	            chkGRP.add(rdbWHTFR);
	            chkGRP.add(rdbREBAG);
	            
	            txtWRHTP.addActionListener(this);
        		txtWRHTP.addFocusListener(this);
        		txtWRHTP.addKeyListener(this);
        		txtWRHTP.setActionCommand("txtWRHTP");	
        		
        		rdbWHTFR.addMouseListener(this);
        		rdbWHTFR.addActionListener(this);
        		rdbWHTFR.addKeyListener(this);
        		rdbWHTFR.setActionCommand("rdbWHTFR");
        		
        		rdbREBAG.addMouseListener(this);
        		rdbREBAG.addActionListener(this);
        		rdbREBAG.addKeyListener(this);
        		rdbREBAG.setActionCommand("rdbREBAG");			
			    
			    M_pnlRPFMT.setVisible(true);
			    
                M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));  // Convert Into Local Date Format
                M_calLOCAL.add(Calendar.DATE,-1);                              // Decrease Date by 1 from Login Date
                strPREVDT = M_fmtLCDAT.format(M_calLOCAL.getTime());           // Assign Date to Variable 
                M_txtFMDAT.setText(strPREVDT);
                
       	}
        catch(Exception L_EX)
        {
			setMSG (L_EX,"GUI Designing");
	    }
    }
    
    /* super class Method overrided to enhance its functionality, to enable & disable components 
    according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{
         super.setENBL(L_flgSTAT);
		 M_txtFMDAT.setText(strPREVDT);
		 M_txtTODAT.setText(strPREVDT);
		 txtWRHTP.setText("01");
		 
	}
	
	/* method to be executed when event is fired on press of a key */
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if (L_KE.getKeyCode()== L_KE.VK_ENTER)
 	    {
 	        if(M_objSOURC == txtWRHTP)
		    {
		        rdbWHTFR.requestFocus();
			}
		}
 	        
	}
	
	void exePRINT()
	{
		if (!vldDATA())
		{				
			setMSG("Please Enter WareHouse Type",'N');
			return;
		}
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rpwht.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpwht.doc";				
			getALLREC();
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
					p  = r.exec("C:\\windows\\iexplore.exe "+ strFILNM); 
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"WareHouse Transfer Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX," exePRINT ");
		}	
	}
	
	/*  Method to validate Data */	        
    boolean vldDATA()
	{
	    try
	    {
    	    if(M_txtFMDAT.getText().trim().length()==0)
    	    {
        		setMSG("Please Enter From Date ..",'E');
        		M_txtFMDAT.requestFocus();
        		return false;
    	    }
    	    if(M_txtTODAT.getText().trim().length()==0)
    	    {
        		setMSG("Please Enter To Date ..",'E');
        		M_txtTODAT.requestFocus();
        		return false;
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
    		if(txtWRHTP.getText().length()==0)
    	    {
    	        setMSG("Please enter valid WareHouse Type..",'N');
    	        txtWRHTP.requestFocus();
    	        return true;
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
		}	
		return true;
    }
	
/**
  * Method to get data  from the database as per user requirements
*/
	private void getALLREC()
    {
		try
        {
            fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT  = 0 ;
    		cl_dat.M_PAGENO = 0;
	        cl_dat.M_intLINNO_pbst=0;
	        setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
			L_IHSTBL = new Hashtable<String,String>();
			L_RHSTBL = new Hashtable<String,String>();
			String L_RCTSTR = "";
			String L_ISSSTR = "";
			L_strISODCA = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpwht01");
			L_strISODCB = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpwht02");
			L_strISODCC = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpwht03");
			L_strFRMDT = M_txtFMDAT.getText().trim();
			L_strTODAT = M_txtTODAT.getText().trim();
			L_strWRHTP = txtWRHTP.getText().trim();
			if(rdbWHTFR.isSelected())
            {
				L_strHDRSTR = "WareHouse Transfer Report";
				L_ISSSTR = " and ist_isstp = '40' ";
				L_RCTSTR = " and rct_rcttp = '40' ";
			}
			else if(rdbREBAG.isSelected())
            {
				L_strHDRSTR = "ReBagging Report";
				L_ISSSTR = " and ist_isstp = '16' ";
				L_RCTSTR = " and rct_rcttp = '16' ";
            }
            if(M_rdbHTML.isSelected())
		    {
		        dosREPORT.writeBytes("<HTML><HEAD><Title>WareHouse Transfer</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
		    }
			
			prnHEADER();        // gets the header of the report
			
			L_strMNGRP1 = "";
			L_strSBGRP1 = "";
			L_strTRNDT1 = "";
			L_strPRDCD1 = "";
			L_strDOCNO1 = "";
			L_strLOTNO1 = "";
			L_strMNLCD1 = "";
			L_strPKGTP1 = "";
			L_strTRNTP1 = "";
			
			L_strMNGRP2 = "";
			L_strSBGRP2 = "";
			L_strTRNDT2 = "";
			L_strPRDCD2 = "";
			L_strDOCNO2 = "";
			L_strLOTNO2 = "";
			L_strMNLCD2 = "";
			L_strPKGTP2 = "";
			L_strTRNTP2 = "";
			
			L_strTORDTQT = "0";
			L_strTORMGQT = "0";
			L_strTORSGQT = "0";
			L_strTORPRQT = "0";
			L_strTORLTQT = "0";
			L_strTORGTQT = "0";
			
			L_strTOIDTQT = "0";
			L_strTOIMGQT = "0";
			L_strTOISGQT = "0";
			L_strTOIPRQT = "0";
			L_strTOILTQT = "0";
			L_strTOIGTQT = "0";
			
			M_strSQLQRY = "Select SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,2) l_mngrp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,";
			M_strSQLQRY += "lt_prdcd l_prdcd, ist_lotno l_lotno, 'IS' l_trntp,SUBSTRING(ist_issno,4,5) l_docno,";
            M_strSQLQRY += "ist_mnlcd l_mnlcd,ist_pkgtp l_pkgtp,ist_autdt l_trndt,ist_mkttp l_mkttp, sum(ist_issqt) l_trnqt";
			M_strSQLQRY += " from fg_istrn,pr_ltmst where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_autdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+"'and'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'";
			M_strSQLQRY += " and ist_CMPCD=lt_cmpcd and ist_prdtp=lt_prdtp and ist_lotno=lt_lotno and ist_rclno=lt_rclno ";
			M_strSQLQRY += L_ISSSTR;
			M_strSQLQRY += " group by SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,2),SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),";
            M_strSQLQRY += " lt_prdcd,ist_lotno,SUBSTRING(ist_issno,4,5),ist_autdt,ist_mnlcd,ist_pkgtp,ist_mkttp";
			M_strSQLQRY += " union";
			M_strSQLQRY += " Select SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,2) l_mngrp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,lt_prdcd l_prdcd,";
			M_strSQLQRY += "rct_lotno l_lotno, 'RC' l_trntp,SUBSTRING(rct_rctno,4,5) l_docno,rct_mnlcd l_mnlcd,rct_pkgtp l_pkgtp,";
            M_strSQLQRY += "rct_rctdt l_trndt,SUBSTRING(rct_issrf,1,2) l_mkttp, sum(rct_rctqt) l_trnqt from fg_rctrn,pr_ltmst where";
			M_strSQLQRY += " rct_CMPCD=lt_cmpcd and rct_prdtp=lt_prdtp and rct_lotno=lt_lotno and rct_rclno=lt_rclno";
			M_strSQLQRY += " and  RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rctdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+"'and'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'";
			M_strSQLQRY += L_RCTSTR;
			M_strSQLQRY += " group by SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,2),SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),lt_prdcd,";
            M_strSQLQRY += " rct_lotno,SUBSTRING(rct_rctno,4,5),rct_rctdt,rct_mnlcd,rct_pkgtp,SUBSTRING(rct_issrf,1,2)";
			M_strSQLQRY += " order by l_mngrp,l_sbgrp,l_prdcd,l_lotno,l_docno,l_trndt,l_trntp,";
			M_strSQLQRY += " l_mnlcd,l_pkgtp";
			//System.out.println("M_strSQLQRY = " + M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println("aaa");					
			boolean L_1STFL = true;
			boolean L_EOF = false;
			
			L_flgDTLFL = true;
			L_intISSCNT = 0;
			L_intRCTCNT = 0;	
		
			if(M_rstRSSET.next())
            {
				while(!L_EOF)
                {
                   					
    				L_strMNGRP = M_rstRSSET.getString("l_mngrp").trim();
    				L_strSBGRP = M_rstRSSET.getString("l_sbgrp").trim();
    				L_strPRDCD = M_rstRSSET.getString("l_prdcd").trim();
    				L_strLOTNO = M_rstRSSET.getString("l_lotno").trim();
    				L_strTRNTP = M_rstRSSET.getString("l_trntp").trim();
    				L_strDOCNO = M_rstRSSET.getString("l_docno").trim();
    				L_strMNLCD = M_rstRSSET.getString("l_mnlcd").trim();
    				L_strPKGTP = M_rstRSSET.getString("l_pkgtp").trim();
                    L_strMKTTP = nvlSTRVL(M_rstRSSET.getString("l_mkttp"),"  ");
    				L_strTRNDT = nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("l_trndt")).toString(),"");
    				L_dblTRNQT = M_rstRSSET.getDouble("l_trnqt");
    								
    				/*if(cl_dat.M_intLINNO_pbst >= 68)
                    {
    						cl_dat.M_intLINNO_pbst= 0;
    						cl_dat.M_PAGENO += 1;
    						prnHEADER(); //gets the Header of the report          
    				}*/
					if(L_1STFL)
                    {
						L_strMNGRP1 = L_strMNGRP;
						L_strSBGRP1 = L_strSBGRP;
						L_strTRNDT1 = L_strTRNDT;
						L_strTRNTP1 = L_strTRNTP;
						L_strDOCNO1 = L_strDOCNO;
						L_strLOTNO1 = L_strLOTNO;
						L_strMNLCD1 = L_strMNLCD;
						L_strPKGTP1 = L_strPKGTP;
						L_strPRDCD1 = L_strPRDCD;
						
						L_strMNGRP2 = L_strMNGRP;
						L_strSBGRP2 = L_strSBGRP;
						L_strTRNDT2 = L_strTRNDT;
						L_strTRNTP2 = L_strTRNTP;
						L_strDOCNO2 = L_strDOCNO;
						L_strLOTNO2 = L_strLOTNO;
						L_strMNLCD2 = L_strMNLCD;
						L_strPKGTP2 = L_strPKGTP;
						L_strPRDCD2 = L_strPRDCD;
						L_1STFL = false;
					}
					prnGRPHDR("MG",2);
					L_strMNGRP1 = L_strMNGRP;
					while(L_strMNGRP.equals(L_strMNGRP1) && !L_EOF)
                    {
						prnGRPHDR("SG",2);
						L_strMNGRP = L_strMNGRP2;
						L_strMNGRP1 = L_strMNGRP;
						while((L_strMNGRP+L_strSBGRP).equals(L_strMNGRP1+L_strSBGRP1) && !L_EOF)
                        {
							prnGRPHDR("PR",4);
							L_strMNGRP = L_strMNGRP2;
							L_strSBGRP = L_strSBGRP2;
							L_strMNGRP1 = L_strMNGRP;
							L_strSBGRP1 = L_strSBGRP;
							while((L_strMNGRP+L_strSBGRP+L_strPRDCD).equals(L_strMNGRP1+L_strSBGRP1+L_strPRDCD1) && !L_EOF)
                            {
								//prnGRPHDR("LT",6);
								L_strMNGRP = L_strMNGRP2;
								L_strSBGRP = L_strSBGRP2;
								L_strPRDCD = L_strPRDCD2;
								L_strMNGRP1 = L_strMNGRP;
								L_strSBGRP1 = L_strSBGRP;
								L_strPRDCD1 = L_strPRDCD;
								intDTCOUNT = 0;
								while((L_strMNGRP+L_strSBGRP+L_strPRDCD+L_strLOTNO).equals(L_strMNGRP1+L_strSBGRP1+L_strPRDCD1+L_strLOTNO1) && !L_EOF)
                                {
									L_strMNGRP = L_strMNGRP2;
									L_strSBGRP = L_strSBGRP2;
									L_strPRDCD = L_strPRDCD2;
									L_strLOTNO = L_strLOTNO2;
									L_strMNGRP1 = L_strMNGRP;
									L_strSBGRP1 = L_strSBGRP;
									L_strPRDCD1 = L_strPRDCD;
									L_strLOTNO1 = L_strLOTNO;
									intDTCOUNT++;
									intDT1COUNT = 0;
									while((L_strMNGRP+L_strSBGRP+L_strPRDCD+L_strLOTNO+L_strDOCNO+L_strTRNDT).equals(L_strMNGRP1+L_strSBGRP1+L_strPRDCD1+L_strLOTNO1+L_strDOCNO1+L_strTRNDT1) && !L_EOF)
                                    {
										intDT1COUNT++;
										L_PRNSTR = new StringBuffer("");
										if(L_strTRNTP2.equals("IS"))
                                        {
											L_strTOIDTQT = setNumberFormat(Double.parseDouble(L_strTOIDTQT)+ L_dblTRNQT,3);
											L_strTOIPRQT = setNumberFormat(Double.parseDouble(L_strTOIPRQT)+ L_dblTRNQT,3);
											L_strTOILTQT = setNumberFormat(Double.parseDouble(L_strTOILTQT)+ L_dblTRNQT,3);
											L_strTOISGQT = setNumberFormat(Double.parseDouble(L_strTOISGQT)+ L_dblTRNQT,3);
											L_strTOIMGQT = setNumberFormat(Double.parseDouble(L_strTOIMGQT)+ L_dblTRNQT,3);
											L_strTOIGTQT = setNumberFormat(Double.parseDouble(L_strTOIGTQT)+ L_dblTRNQT,3);
											L_PRNSTR.append("       "+padSTRING('R',(intDT1COUNT>1 ? "" : L_strLOTNO1),25-7));
											//L_PRNSTR.append(padSTRING('R',"",(intDT1COUNT>1) ? 25 : 5));
											L_strPKGDS = cl_dat.getPRMCOD("CMT_SHRDS","SYS","FGXXPKG",L_strPKGTP2);
											L_PRNSTR.append(padSTRING('R',L_strPKGDS,15));
											L_PRNSTR.append(padSTRING('R',L_strMNLCD2,10));
                                            L_PRNSTR.append(padSTRING('L',L_strMKTTP,2));
                                            L_PRNSTR.append(padSTRING('L',L_strTOIDTQT,8));
                                            L_PRNSTR.append(padSTRING('L',"",5));
											L_IHSTBL.put(String.valueOf(L_intISSCNT),L_PRNSTR.toString());
											L_strTOIDTQT = "0";
											L_intISSCNT++;
										}
										else if(L_strTRNTP2.equals("RC"))
                                        {
											L_strTORDTQT = setNumberFormat(Double.parseDouble(L_strTORDTQT)+L_dblTRNQT,3);
											L_strTORPRQT = setNumberFormat(Double.parseDouble(L_strTORPRQT)+L_dblTRNQT,3);
											L_strTORLTQT = setNumberFormat(Double.parseDouble(L_strTORLTQT)+L_dblTRNQT,3);
											L_strTORSGQT = setNumberFormat(Double.parseDouble(L_strTORSGQT)+L_dblTRNQT,3);
											L_strTORMGQT = setNumberFormat(Double.parseDouble(L_strTORMGQT)+L_dblTRNQT,3);
											L_strTORGTQT = setNumberFormat(Double.parseDouble(L_strTORGTQT)+L_dblTRNQT,3);
											//L_PRNSTR.append(padSTRING('R'," ",5));
											L_strPKGDS = cl_dat.getPRMCOD("CMT_SHRDS","SYS","FGXXPKG",L_strPKGTP2);
											L_PRNSTR.append(padSTRING('R',L_strPKGDS,15));
											L_PRNSTR.append(padSTRING('R',L_strMNLCD2,10));
                                            L_PRNSTR.append(padSTRING('L',L_strMKTTP,2));
                                            L_PRNSTR.append(padSTRING('L',L_strTORDTQT,8));
											L_PRNSTR.append(padSTRING('R'," ",5));
											L_PRNSTR.append(padSTRING('R',L_strDOCNO2+":"+L_strTRNDT2,20));
											L_RHSTBL.put(String.valueOf(L_intRCTCNT),L_PRNSTR.toString());
											L_strTORDTQT = "0";
											L_intRCTCNT++;
										}
										if(!M_rstRSSET.next())
                                        {
											L_EOF = true;
											break;
										}
										
										L_strMNGRP2 = M_rstRSSET.getString("l_mngrp").trim();
										L_strSBGRP2 = M_rstRSSET.getString("l_sbgrp").trim();
										L_strPRDCD2 = M_rstRSSET.getString("l_prdcd").trim();
										L_strLOTNO2 = M_rstRSSET.getString("l_lotno").trim();
										L_strTRNTP2 = M_rstRSSET.getString("l_trntp").trim();
										L_strDOCNO2 = M_rstRSSET.getString("l_docno").trim();
										L_strMNLCD2 = M_rstRSSET.getString("l_mnlcd").trim();
										L_strPKGTP2 = M_rstRSSET.getString("l_pkgtp").trim();
										L_strTRNDT2 = nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("l_trndt")).toString(),"");										L_dblTRNQT = M_rstRSSET.getDouble("l_trnqt");
										
										L_strMNGRP = L_strMNGRP2;
										L_strSBGRP = L_strSBGRP2;
										L_strPRDCD = L_strPRDCD2;
										L_strLOTNO = L_strLOTNO2;
										L_strDOCNO = L_strDOCNO2;
										L_strTRNDT = L_strTRNDT2;
										intRECCT = 1;
									}
									prnGRPTOT("DT",L_strTORDTQT,L_strTOIDTQT,8,"B");
									intGRPTOT("DT");	
								}
								if (intDTCOUNT>1)
									prnGRPTOT("LT",L_strTORLTQT,L_strTOILTQT,6,"B");
								intGRPTOT("LT");	
							}
							prnGRPTOT("PR",L_strTORPRQT,L_strTOIPRQT,4,"B");
							intGRPTOT("PR");
						}
						prnGRPTOT("SG",L_strTORSGQT,L_strTOISGQT,2,"B");
						intGRPTOT("SG");
					}
					prnGRPTOT("MG",L_strTORMGQT,L_strTOIMGQT,2,"B");
					intGRPTOT("MG");
				}
			}
            else
				setMSG(L_strHDRSTR+" for the entered date does not exist.",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			prnFOOTR();
	    }
        catch(Exception L_EX)
        {
		    setMSG(L_EX,"getALLREC");
	    }
    }
    
    private void prnGRPHDR(String LP_GRPCT, int LP_MRGVL)
    {
		try
        {
            String L_GRPDS = "";
			if (LP_GRPCT.equals("MG"))
            {
				L_GRPDS = cl_cust.ocl_cust.getMNPRD(L_strMNGRP1);
			}
			else if (LP_GRPCT.equals("SG"))
            {
				L_GRPDS = cl_cust.ocl_cust.getSBPRD(L_strSBGRP1);
			}
			else if (LP_GRPCT.equals("PR"))
            {
				L_GRPDS = cl_cust.ocl_cust.getPRDDS(L_strPRDCD1);
			}
			else if (LP_GRPCT.equals("LT"))
            {
				L_GRPDS = L_strLOTNO1;
				LP_MRGVL = LP_MRGVL + 1;
			}
			if (LP_GRPCT.equals("PR"))
            {
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 2;
			}
			prnFMTCHR(dosREPORT,M_strBOLD);
			StringBuffer L_GRPHD = new StringBuffer(padSTRING('R',"",LP_MRGVL));
			//L_GRPHD.append(padSTRING('R',L_GRPDS,((LP_GRPCT.equals("LT") ? 20 : 40)-LP_MRGVL)));
			L_GRPHD.append(padSTRING('R',L_GRPDS,40-LP_MRGVL));
            L_GRPDS = L_strMKTTP.equals("SR") ? L_GRPDS+" Sales Return" : L_GRPDS;
			dosREPORT.writeBytes(L_GRPHD.toString());
			//System.out.println(L_GRPHD+"x"+cl_dat.M_intLINNO_pbst);
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			if (!LP_GRPCT.equals("LT"))
            {
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			if(cl_dat.M_intLINNO_pbst >= 68)
            {
				dosREPORT.writeBytes("\n");
				crtLINE(120);
				prnFMTCHR(dosREPORT,M_strEJT);
				prnHEADER();            //gets the Header of the report
			}
	    }
        catch(Exception L_EX)
        {
    		setMSG(L_EX,"prnGRPHDR");
    	}
    }
    
	/** Returning Lot No. String for detail line printing
	 */
    private String prnGRPHDR_LT( int LP_MRGVL)
    {
		String L_GRPDS = "";
		try
        {
			//if(intDT1COUNT>1 && (L_strMNGRP+L_strSBGRP+L_strPRDCD+L_strLOTNO).equals(L_strMNGRP1+L_strSBGRP1+L_strPRDCD1+L_strLOTNO1))
			//		L_GRPDS = L_strLOTNO1;
			if(intDT1COUNT>1)
					L_GRPDS = L_strLOTNO1;
			
	    }
        catch(Exception L_EX)
        {
    		setMSG(L_EX,"prnGRPHDR_LT");
    	}
		return L_GRPDS;
    }
	
	private void prnGRPTOT(String LP_GRPCT,String LP_TORXXQT,String LP_TOIXXQT,int LP_MRGVL,String LP_BLDFL)
    {
		try
        {
			int L_intTOTMRG = 25+15+10+2+LP_MRGVL;
			L_flgDTLFL = true;
			L_PRNSTR = new StringBuffer("");
			if (LP_GRPCT.equals("DT"))
            {
				L_intCNT = 0;
				LM_ISHTST = "";
				LM_RCHTST = "";
				dosREPORT.writeBytes (intDTCOUNT>1 ? "\n" : "");
				cl_dat.M_intLINNO_pbst+=(intDTCOUNT>1 ? 1 : 0);
				if(L_intISSCNT >= L_intRCTCNT)
					L_intCNT = L_intISSCNT;
				else
					L_intCNT = L_intRCTCNT;
				for(int i = 0;i < L_intCNT;i++)
                {
					LM_ISHTST = String.valueOf(L_IHSTBL.get(String.valueOf(i)));
					LM_RCHTST = String.valueOf(L_RHSTBL.get(String.valueOf(i)));
					if(LM_ISHTST.equals("null"))
						LM_ISHTST = padSTRING('R'," ",65);
					if(LM_RCHTST.equals("null"))
						LM_RCHTST = padSTRING('R'," ",70);
					dosREPORT.writeBytes(LM_ISHTST);	
					dosREPORT.writeBytes(LM_RCHTST);	
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					if(cl_dat.M_intLINNO_pbst >= 68)
					{
						dosREPORT.writeBytes("\n");
						crtLINE(120);
						prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();            //gets the Header of the report
					}
				}
				L_flgDTLFL = false;
			}
			else if (LP_GRPCT.equals("LT"))
            {
				L_PRNSTR.append(padSTRING('R',"",LP_MRGVL));
				L_PRNSTR.append(padSTRING('R',"",(20-LP_MRGVL)));
				//L_PRNSTR.append(padSTRING('R',"",33));
			}
			else if (LP_GRPCT.equals("PR"))
            {
				L_strPRDDS = cl_cust.ocl_cust.getPRDDS(L_strPRDCD1);
				L_PRNSTR.append(padSTRING('R',"",(LP_MRGVL)));
				L_PRNSTR.append(padSTRING('R',"Total "+L_strPRDDS,(20-LP_MRGVL)));
				//L_PRNSTR.append(padSTRING('R',"Total "+L_strPRDDS,23));
			}
			else if (LP_GRPCT.equals("SG"))
            {
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_strSBPRD = cl_cust.ocl_cust.getSBPRD(L_strSBGRP1);
				L_PRNSTR.append(padSTRING('R',"",LP_MRGVL));
				L_PRNSTR.append(padSTRING('R',"Total "+L_strSBPRD,(20-LP_MRGVL)));
				//L_PRNSTR.append(padSTRING('R',"Total "+L_strSBPRD,25));
			}
			else if (LP_GRPCT.equals("MG"))
            {
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_strMNPRD = cl_cust.ocl_cust.getMNPRD(L_strMNGRP1);
				L_PRNSTR.append(padSTRING('R',"",LP_MRGVL));
				L_PRNSTR.append(padSTRING('R',"Total "+L_strMNPRD,(20-LP_MRGVL)));
				//L_PRNSTR.append(padSTRING('R',"Total "+L_strMNPRD,25));
			}
			else if (LP_GRPCT.equals("GT"))
            {
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_PRNSTR.append(padSTRING('R',"",LP_MRGVL));
				L_PRNSTR.append(padSTRING('R',"Grand Total",(20-LP_MRGVL)));
				//L_PRNSTR.append(padSTRING('R',"Grand Total",25));
			}
			if(L_flgDTLFL)
            {
				if (LP_BLDFL.equals("B"))
					prnFMTCHR(dosREPORT,M_strBOLD);
				L_PRNSTR.append(padSTRING('L',getDASH(LP_TOIXXQT),15+10+2+13));
				L_PRNSTR.append(padSTRING('L',getDASH(LP_TORXXQT),15+10+2+13));
				dosREPORT.writeBytes(L_PRNSTR.toString());
				if (LP_BLDFL.equals("B"))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			if(cl_dat.M_intLINNO_pbst >= 68)
            {
				dosREPORT.writeBytes("\n");
				crtLINE(120);
				prnFMTCHR(dosREPORT,M_strEJT);
				prnHEADER(); //gets the Header of the report
			}
		}
        catch(Exception L_EX)
        {
			setMSG(L_EX,"prnGRPTOT");
		}
    }
    
    private String getDASH(String LP_DSHSTR)
    {
		try
        {
			if(LP_DSHSTR == null || LP_DSHSTR.equals("0"))
				LP_DSHSTR = "-";
		}
        catch(Exception L_EX)
        {
			setMSG(L_EX,"getDASH");
		}
		return LP_DSHSTR;
	}
		
	private void intGRPTOT(String LP_GRPCT)
    {
		try
        {
			if (LP_GRPCT.equals("DT"))
            {
				L_strTORDTQT = "0";
				L_strTOIDTQT = "0";
				L_intISSCNT = 0;
				L_intRCTCNT = 0;	
				L_RHSTBL.clear();
				L_IHSTBL.clear();	
				L_strDOCNO1 = L_strDOCNO;
				L_strTRNDT1 = L_strTRNDT;
			}
			else if (LP_GRPCT.equals("LT"))
            {
				L_strTORLTQT = "0";
				L_strTOILTQT = "0";
				L_strLOTNO1 = L_strLOTNO;
			}
			else if (LP_GRPCT.equals("PR"))
            {
				L_strTORPRQT = "0";
				L_strTOIPRQT = "0";
				L_strPRDCD1 = L_strPRDCD;
			}
			else if (LP_GRPCT.equals("SG"))
            {
				L_strTORSGQT = "0";
				L_strTOISGQT = "0";
				L_strSBGRP1 = L_strSBGRP;
			}
			else if (LP_GRPCT.equals("MG"))
            {
				L_strTORMGQT = "0";
				L_strTOIMGQT = "0";
				L_strMNGRP1 = L_strMNGRP;
			}
		}
        catch(Exception L_EX)
        {
			setMSG(L_EX,"intGRPTOT");
		}
    }
    
    private void prnFOOTR()         //prints the footer for the report
    {
    	try
        {
            
    		if(cl_dat.M_intLINNO_pbst >= 61)
            {
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(120);
    			prnFMTCHR(dosREPORT,M_strEJT);
    			cl_dat.M_intLINNO_pbst = 0;
    			cl_dat.M_PAGENO += 1;
    			prnHEADER();
    		}
    		prnGRPTOT("GT",L_strTORGTQT,L_strTOIGTQT,2,"B");
    		dosREPORT.writeBytes ("\n");
    		crtLINE(120);
    		for(int i = 1;i <= 5;i++)
    			dosREPORT.writeBytes ("\n");
    		dosREPORT.writeBytes(padSTRING('L'," ",10));//margin
    		prnFMTCHR(dosREPORT,M_strBOLD);
    		dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",20));
    		dosREPORT.writeBytes(padSTRING('L',"CHECKED BY ",30));
    		dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)",40));
    		prnFMTCHR(dosREPORT,M_strNOBOLD);
    		dosREPORT.writeBytes("\n");
    		crtLINE(120);
    		cl_dat.M_intLINNO_pbst += 7;
    		prnFMTCHR(dosREPORT,M_strEJT);
    		//prnFMTCHR(dosREPORT,M_strNOCPI17);
    		//prnFMTCHR(dosREPORT,M_strCPI10);
    		dosREPORT.close();
    	}
        catch(Exception L_EX)
        {
    		setMSG(L_EX,"prnFOOTR");
    	}
    }
	
/**
 *	Method to format header of the Report 
 */
	private void prnHEADER()
    {
		try
        {
            cl_dat.M_intLINNO_pbst = 0;
		    cl_dat.M_PAGENO += 1; 
		    //prnFMTCHR(dosREPORT,M_strCPI17);
		    if(M_rdbTEXT.isSelected())
		    {
		        prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
    			dosREPORT.writeBytes(padSTRING('L',"Report Date :"+cl_dat.M_strLOGDT_pbst,96));
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R',L_strHDRSTR+" for the date "+ L_strFRMDT +" to "+ L_strTODAT ,75));
    			dosREPORT.writeBytes(padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,37));
    			dosREPORT.writeBytes("\n");
    			crtLINE(121);
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R'," ",3));
    			dosREPORT.writeBytes(padSTRING('R',"Grade",9));
    			dosREPORT.writeBytes(padSTRING('R',"Lot No.",13));
    			dosREPORT.writeBytes(padSTRING('R',"<--------------From------------->",35));
    			dosREPORT.writeBytes(padSTRING('R',"",5));
    			dosREPORT.writeBytes(padSTRING('R',"<---------------To-------------->",35));
    			dosREPORT.writeBytes(padSTRING('R',"",5));
    			dosREPORT.writeBytes(padSTRING('R',"Doc.Ref.",16));
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R'," ",25));
    			dosREPORT.writeBytes(padSTRING('R',"Pkg.Wt.",15));
    			dosREPORT.writeBytes(padSTRING('R',"Loc.",8));
    			dosREPORT.writeBytes(padSTRING('L',"Qty.",10));
    			dosREPORT.writeBytes(padSTRING('R',"",7));
    			dosREPORT.writeBytes(padSTRING('R',"Pkg.Wt.",15));
    			dosREPORT.writeBytes(padSTRING('R',"Loc.",10));
    			dosREPORT.writeBytes(padSTRING('L',"Qty.",10));
    			dosREPORT.writeBytes("\n");
    			crtLINE(121);
    			dosREPORT.writeBytes("\n");
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
		    }
		    else
		    {
		        prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",25));
    			dosREPORT.writeBytes(padSTRING('L',"Report Date :"+cl_dat.M_strLOGDT_pbst,96));
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R',L_strHDRSTR+" for the date "+ L_strFRMDT +" to "+ L_strTODAT ,75));
    			dosREPORT.writeBytes(padSTRING('L',"Page No     :" + cl_dat.M_PAGENO,37));
    			dosREPORT.writeBytes("\n");
    			crtLINE(121);
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R'," ",3));
    			dosREPORT.writeBytes(padSTRING('R',"Grade",9));
    			dosREPORT.writeBytes(padSTRING('R',"Lot No.",13));
    			dosREPORT.writeBytes(padSTRING('R',"<--------------From------------->",35));
    			dosREPORT.writeBytes(padSTRING('R',"",5));
    			dosREPORT.writeBytes(padSTRING('R',"<---------------To-------------->",35));
    			dosREPORT.writeBytes(padSTRING('R',"",5));
    			dosREPORT.writeBytes(padSTRING('R',"Doc.Ref.",16));
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes(padSTRING('R'," ",25));
    			dosREPORT.writeBytes(padSTRING('R',"Pkg.Wt.",15));
    			dosREPORT.writeBytes(padSTRING('R',"Loc.",8));
    			dosREPORT.writeBytes(padSTRING('L',"Qty.",10));
    			dosREPORT.writeBytes(padSTRING('R',"",7));
    			dosREPORT.writeBytes(padSTRING('R',"Pkg.Wt.",15));
    			dosREPORT.writeBytes(padSTRING('R',"Loc.",10));
    			dosREPORT.writeBytes(padSTRING('L',"Qty.",10));
    			dosREPORT.writeBytes("\n");
    			crtLINE(121);
    			dosREPORT.writeBytes("\n");
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    		}
	    	cl_dat.M_intLINNO_pbst = 11;
		}
        catch(Exception L_EX)
        {
			setMSG(L_EX,"prnHEADER");
		}
    }
    
	/**
	 *Method to create lines that are used in the Reports
	 */
	private void crtLINE(int L_intCNT)
    {
		String strln = "";
		try
        {
			for(int i=1;i<=L_intCNT;i++)
            {
			     strln += "-";
			}
			dosREPORT.writeBytes(padSTRING('L'," ",L_intLMRGN));
			dosREPORT.writeBytes(strln);
	    }
        catch(Exception L_EX)
        {
		    setMSG(L_EX,"crtLINE");
		}
	}

	
}//end of class
