/*
System Name   : Finished Goods Inventory Management System
Program Name  : Gradewise Lot Statement as on given date

Program Desc. : User selects the Receipt Date & the destination for the report as screen,
				printer or file.
Author        : Mr. Santosh V. Sawant
Date          : 21st June 2001
Version       : FIMS 1.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
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
import java.lang.System;
import java.util.Hashtable;

public class fg_rpglt extends cl_rbase implements MouseListener
{
	JComboBox cmbPRCAT;
	JTextField txtMNCAT;
						  
	//String LM_RESFIN = cl_dat.ocl_dat.M_REPSTR;
    //String LM_RESSTR = LM_RESFIN.concat("\\fg_rpglt.doc");
	FileOutputStream fosREPORT;/** File OutputStream Object for file handling.*/
	
    DataOutputStream dosREPORT; /** Data OutputStream for generating Report File.*/	
    String strFILNM;	
	String LM_STRSQL,strISODCA,strISODCB,strISODCC;
	private ButtonGroup btgRPTST;    
	private JRadioButton rdbCURNT;	 
	private JRadioButton rdbDYOPN;	
	
    String strPRDDS,strSTSFL,strLOTNO,strRCTDT,strMNLCD,strREMDS,strTPRDS,strIPRDS,strPRDTP,strRCLNO;
	Date LM_RCTDATE;
	double dblDOSQT,dblRETQT,dblTOTAL, dblGTOTAL;
	String strTEMP;
	int intINDEX;
	int intRECCT;
	StringBuffer strbPRNSTR;
	//ResultSet M_rstRSSET;
	int intLMRGN=0;
	Hashtable<String,String> hstcodes = new Hashtable<String,String>();     
	String strPRDCD;
	/**
	 *1.Screen Designing & Inser the table or create the table into the panel 
	 *2.Alongwith their descriptions.
	 */
	
	fg_rpglt()
	{
		super(2);
		try
		{	
		
			setMatrix(20,8);
			 M_vtrSCCOMP.remove(M_lblFMDAT);
			 M_vtrSCCOMP.remove(M_lblTODAT);
			 M_vtrSCCOMP.remove(M_txtTODAT);
			 M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Category"),6,3,1,1,this,'L');
			add(cmbPRCAT=new JComboBox(),6,5,1,1.5,this,'L');
			btgRPTST=new ButtonGroup();		
			add(new JLabel("Stock Statment of"),3,1,1,1.3,this,'L');
			add(rdbCURNT=new JRadioButton("Current Stock",true),3,3,1,1,this,'L');
			add(rdbDYOPN=new JRadioButton("Day Opening Stock"),3,4,1,2,this,'L');

			btgRPTST.add(rdbCURNT);
			btgRPTST.add(rdbDYOPN);
			
			cmbPRCAT.addItem("All");
			cmbPRCAT.addMouseListener(this);
			getPRDDS();
			M_pnlRPFMT.setVisible(true);
			cmbPRCAT.setEnabled(false);
			
			
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
	}
	/** This we remove the From Date and ToDate TextFields and FromLabeland ToLabel */
	void removeENBL(boolean L_flgSTAT)
    {
       
          M_txtFMDAT.setVisible(false);
	       M_txtTODAT.setVisible(false);
	       M_lblFMDAT.setVisible(false);
		   M_lblTODAT.setVisible(false);		
		    
	 }
/**	 Get the product description from CO_CDTRN */
	private void getPRDDS()
	{			
		try
		{
			M_strSQLQRY = "Select	CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'MST' ";
			M_strSQLQRY += " and CMT_CGSTP = 'COXXPRD'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			while(M_rstRSSET.next())
			{
				cmbPRCAT.addItem(M_rstRSSET.getString("CMT_CODDS"));
				hstcodes.put(M_rstRSSET.getString("CMT_CODDS"),M_rstRSSET.getString("CMT_CODCD"));
			}
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
	  }catch(Exception L_EX)
	  {
	  	setMSG(L_EX,"getPRDDS");
	  }
	} 
	  /** Perform the actionperformed the RUN & CLEAR */
	  public void actionPerformed(ActionEvent L_AE)
	  {
	    	super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) /** Combo Opotion Remove the From Date & To Date                 */
			removeENBL(false);

			
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Action Performed");
		}	
	  }
	  public void MouseClicked(MouseEvent L_ME)
	  {
	  	if(M_objSOURC == cmbPRCAT.getSelectedItem().toString())
		{
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	  	
	  	
	  }
	  /** This is used for to print the report on the specified Location " Word Format & HTML Format printer & Fax "
	   */	
	  void exePRINT()
		{   
	  	if (vldDATA())
		{
			try
			{
		
		        intRECCT  = 0 ;
		     		    
	   		    setMSG("Report Generation in Process....." ,'N');
	   		    setCursor(cl_dat.M_curWTSTS_pbst);
	   		   if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_tpglt.html";
			    else if(M_rdbTEXT.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpglt.doc";	
								
				getDATA();
				/*if(dosREPORT !=null)
    				dosREPORT.close();
    			if(fosREPORT !=null)
    				fosREPORT.close();
    			if(intRECCT == 0)
    			{
    				setMSG("Data not found for the given Inputs..",'E');
    				return;
    			}*/
				
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
	    			    p  = r.exec("C:\\windows\\wordpad.exe " + strFILNM); 
				}				
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
					cl_eml ocl_eml = new cl_eml();				    
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Details"," ");
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					}
				}
				
	    			if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
				
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"Error.exescrn.. ");
			}
		}	
		}
	  
/**	METHOD TO GET A PARAMETER FROM CODE TRANSACTION TABLE. */ 
	  
	  private void getISODCN()
	  { 
		try
		{
			strISODCA = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","FG_RPGLT01");
			strISODCB = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","FG_RPGLT02");
			strISODCC = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","FG_RPGLT03");
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"getISODCN");
		}
	}
	  /*  Method to validate data */	        
	    boolean vldDATA()
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
		
	  /** Method to fetch data from DB & club it with Header in Data Output Stream  */
	  private void getDATA()
	  {
			try
			{   
				cl_dat.M_PAGENO = 1;
			    String strln="";
			    //System.out.println("sss");
			    //System.out.println("th file name is " +strFILNM);
			    fosREPORT = new FileOutputStream(strFILNM);
			    System.out.println("th file name is " +strFILNM);
			    dosREPORT = new DataOutputStream(fosREPORT);
			    System.out.println("th file name is " +strFILNM);
			    
			    int RECCT  = 0 ;
		        cl_dat.M_intLINNO_pbst=0;
	   		    setMSG("Report Generation in Process....." ,'N');
	   		    setCursor(cl_dat.M_curWTSTS_pbst);
	   		    if(M_rdbHTML.isSelected())
			    {
			        
			        dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
			        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
			        dosREPORT.writeBytes("</STYLE>");
				 }
				 else
				 {
					prnFMTCHR(dosREPORT,M_strCPI10);
	   		    	//prnFMTCHR(dosREPORT,M_strCPI12);
				 }
	   		 
	   		 cl_dat.M_PAGENO = 1;
			 
	   		// prnFMTCHR(dosREPORT,M_strNOCPI17);
			 //prnFMTCHR(dosREPORT,M_strCPI10);   
	   		 getISODCN();
	   		 prnHEADER();/****gets the header of the report*/
	   		 
	   		 getALLREC();
	   		 
			 
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"getData");
			}
	   }	
	  
/**	gets the Header of the Report */  
	  private void prnHEADER()
	  {  
		try
		{
			for(int i = 1;i < 3;i++)
			dosREPORT.writeBytes("\n");
			//cl_dat.M_PAGENO +=1;
			dosREPORT.writeBytes(padSTRING('R',"",48));
			crtLINE(32);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISODCA,80));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISODCB,80));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',strISODCC,80));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",48));
			crtLINE(32);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
			dosREPORT.writeBytes(padSTRING('L',"Report Date :"+cl_dat.M_strLOGDT_pbst,55));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"GRADEWISE LOT STATEMENT AT 0700 HRS ON " + cl_dat.M_strLOGDT_pbst,55));
			dosREPORT.writeBytes(padSTRING('L',"Page No.:",22) + padSTRING('L',String.valueOf(cl_dat.M_PAGENO) ,3));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 9;
			getTBLHDR();
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"...PrnHeader...");		
		}
	 }
	  /** fetches all the records from DB*/
	  private void getALLREC()
	  { 
		String L_STXXXQT = rdbDYOPN.isSelected() ? "ST_DOSQT" : "ST_STKQT";
		try{
			//CONVERT(varchar,LT_PSTDT,103)
            M_strSQLQRY = "Select a.PR_PRDDS L_PRDDS,ST_PRDTP,ST_LOTNO, LT_PSTDT,"+L_STXXXQT+",ST_MNLCD,LT_RETQT,ST_STSFL,";
			M_strSQLQRY += "(ST_REMDS   +   LT_REMDS) L_REMDS,b.PR_PRDDS L_TPRDS,";
            M_strSQLQRY += "LT_IPRDS,ST_RCLNO FROM FG_STMST,pr_ltmst,co_prmst a,co_prmst b ";
			M_strSQLQRY += "where ST_CMPCD = LT_CMPCD and ST_PRDTP = LT_PRDTP and ST_LOTNO = LT_LOTNO AND ";
			M_strSQLQRY += "ST_RCLNO = LT_RCLNO AND  ltrim(str(LT_PRDCD,20,0)) = a.PR_PRDCD AND ";
			M_strSQLQRY += "ltrim(str(LT_TPRCD,20,0)) = b.PR_PRDCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND "+L_STXXXQT+" > 0";
			
			if(cmbPRCAT.getSelectedIndex() != 0)
			{
				strPRDCD = hstcodes.get(cmbPRCAT.getSelectedItem()).toString();
				M_strSQLQRY += " and ST_PRDTP = '" + strPRDCD + "'";
			}
			
			M_strSQLQRY += " order by L_PRDDS,LT_PSTDT,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_MNLCD";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			//System.out.println("LM_RSLSET = " + LM_RSLSET);
			
			String L_PRDDS = "";
			strbPRNSTR = new StringBuffer("");
			
			dblGTOTAL = 0;
			while(M_rstRSSET.next())
			{
				strPRDDS = nvlSTRVL(M_rstRSSET.getString("L_PRDDS"),"").trim();
				strPRDTP = nvlSTRVL(M_rstRSSET.getString("ST_PRDTP"),"").trim();
				strLOTNO = nvlSTRVL(M_rstRSSET.getString("ST_LOTNO"),"").trim();
				strRCLNO = nvlSTRVL(M_rstRSSET.getString("ST_RCLNO"),"").trim();
                strSTSFL = nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"").trim();
				LM_RCTDATE = M_rstRSSET.getDate("LT_PSTDT");
				dblDOSQT = M_rstRSSET.getDouble(L_STXXXQT);
				strMNLCD = nvlSTRVL(M_rstRSSET.getString("ST_MNLCD"),"").trim();
				dblRETQT = M_rstRSSET.getDouble("LT_RETQT");
				strREMDS = nvlSTRVL(M_rstRSSET.getString("L_REMDS"),"").trim();
                strREMDS = strSTSFL.equals("2") ? strREMDS + "* Sales Return" : strREMDS;
				strTPRDS = nvlSTRVL(M_rstRSSET.getString("L_TPRDS"),"").trim();
				strIPRDS = nvlSTRVL(M_rstRSSET.getString("LT_IPRDS"),"").trim();
				
				if(strbPRNSTR.length() != 0)
					strbPRNSTR.delete(0,strbPRNSTR.length());
				
				if(cl_dat.M_intLINNO_pbst > 67){
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO += 1;
					prnHEADER(); //gets the Header of the report
					getTBLHDR(); // gets the Header of the Table
				}
				
				if(strPRDDS.trim().equals(L_PRDDS.trim())){
					dblTOTAL += dblDOSQT;
				}
				else{
					if(!L_PRDDS.equals("")){
						if(strbPRNSTR.length() != 0)
							strbPRNSTR.delete(0,strbPRNSTR.length());
						
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 1;
						
						if(cl_dat.M_intLINNO_pbst > 67){
							//prnFOOTR();
							crtLINE(80);
							prnFMTCHR(dosREPORT,M_strEJT);				
														
							cl_dat.M_intLINNO_pbst = 0;
							cl_dat.M_PAGENO += 1;
							prnHEADER(); //gets the Header of the report
							//getTBLHDR(); // gets the Header of the Table
						}
						
						strbPRNSTR.append("Total:");
						strbPRNSTR.append(padSTRING('L',L_PRDDS,15));
						
						strTEMP = setNumberFormat(dblTOTAL,3);
						dblGTOTAL += dblTOTAL;
						strbPRNSTR.append(padSTRING('L',strTEMP,11));
						dosREPORT.writeBytes(strbPRNSTR.toString());
						
						dosREPORT.writeBytes("\n");	
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 2;
						if(cl_dat.M_intLINNO_pbst > 67){
							//prnFOOTR();
							crtLINE(80);
							prnFMTCHR(dosREPORT,M_strEJT);				
							
							cl_dat.M_intLINNO_pbst = 0;
							cl_dat.M_PAGENO += 1;
							prnHEADER(); //gets the Header of the report
							//getTBLHDR(); // gets the Header of the Table
						}
					}
					
					dblTOTAL = dblDOSQT;
					
					if(strbPRNSTR.length() != 0)
						strbPRNSTR.delete(0,strbPRNSTR.length());
					
					
					L_PRDDS = strPRDDS.trim();
					strbPRNSTR.append((strPRDDS));
					
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(strbPRNSTR.toString());
					prnFMTCHR(dosREPORT,M_strNOBOLD);

					dosREPORT.writeBytes("\n");	
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 2;
					if(cl_dat.M_intLINNO_pbst > 66){
						//prnFOOTR();
						crtLINE(80);
						prnFMTCHR(dosREPORT,M_strEJT);				
						
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO+= 1;
						prnHEADER(); //gets the Header of the report
						//getTBLHDR(); // gets the Header of the Table
					}
										
					if(strbPRNSTR.length() != 0)
						strbPRNSTR.delete(0,strbPRNSTR.length());
				}
				if(strTPRDS.compareTo(strIPRDS) == 0)
					strbPRNSTR.append(padSTRING('R'," ",2));
				else{
					strbPRNSTR.append(padSTRING('R',"$",2));
				}
				strbPRNSTR.append(padSTRING('L',(strLOTNO),8));
				strbPRNSTR.append(padSTRING('R'," ",2));
				strbPRNSTR.append(padSTRING('R',(LM_RCTDATE == null ? "" : M_fmtLCDAT.format(LM_RCTDATE)),10));
				strTEMP = setNumberFormat(dblDOSQT,3);
				strbPRNSTR.append(padSTRING('L',strTEMP,10));
				strbPRNSTR.append(padSTRING('R'," ",2));
				strbPRNSTR.append(padSTRING('R',(strMNLCD),7));
				strbPRNSTR.append(padSTRING('R'," ",6));
				
				if(dblRETQT == 0){
					strbPRNSTR.append(padSTRING('L',"--",5));
				}
				else
					strbPRNSTR.append(setNumberFormat(dblRETQT,3));
				strbPRNSTR.append(padSTRING('R'," ",4));
									
				dosREPORT.writeBytes(strbPRNSTR.toString());
				
				if(strbPRNSTR.length() != 0)
					strbPRNSTR.delete(0,strbPRNSTR.length());
				
				if(strREMDS.length() > 0){
					//prnFMTCHR(dosREPORT,M_strCPI17);
					strbPRNSTR.append(padSTRING('R',(strREMDS.trim()),35));
					dosREPORT.writeBytes(strbPRNSTR.toString());
					//prnFMTCHR(dosREPORT,M_strNOCPI17);
				}	
				dosREPORT.writeBytes("\n");								
				cl_dat.M_intLINNO_pbst += 1;
				if(cl_dat.M_intLINNO_pbst > 67){
					//prnFOOTR();
					crtLINE(80);
					prnFMTCHR(dosREPORT,M_strEJT);				
					
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO += 1;
					prnHEADER(); //gets the Header of the report
					//getTBLHDR(); // gets the Header of the Table
				}
			}
			
			// To print the total of the last grade
			if(strbPRNSTR.length() != 0)
				strbPRNSTR.delete(0,strbPRNSTR.length());
			
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			
			if(cl_dat.M_intLINNO_pbst > 67){
				//prnFOOTR();
				crtLINE(80);
				prnFMTCHR(dosREPORT,M_strEJT);				
				
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				prnHEADER(); //gets the Header of the report
				//getTBLHDR(); // gets the Header of the Table
			}
					
			strbPRNSTR.append("Total:");
			strbPRNSTR.append(padSTRING('L',L_PRDDS,15));
						
			strTEMP = setNumberFormat(dblTOTAL,3);
			dblGTOTAL += dblTOTAL;
			strbPRNSTR.append(padSTRING('L',strTEMP,11));
			dosREPORT.writeBytes(strbPRNSTR.toString());
			
			
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 2;
			
			
			if(cl_dat.M_intLINNO_pbst > 61){
				crtLINE(80);
				prnFMTCHR(dosREPORT,M_strEJT);				
				
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				prnHEADER(); //gets the Header of the report
				//getTBLHDR(); 
			}		
			crtLINE(80);
			strTEMP = setNumberFormat((dblGTOTAL),3);
			dosREPORT.writeBytes("\nGross Total: " + padSTRING('L',strTEMP,19) + "\n");
			crtLINE(80);
			prnFOOTR();
						
			dosREPORT.close();
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			setMSG("",'N');
		}catch(Exception L_EX){
			setMSG(L_EX,"getALLREC");
		}
	}
	  /**Print the Footer of the Report*/
	  private void prnFOOTR(){
		try{
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("                                        SR.MANAGER - MATERIAL HANDLING DIVISION");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes("'$' - Classified grade mismatches with prov. grade, ensure marking classified");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes("      grade while dispatch");
						
			cl_dat.M_intLINNO_pbst += 7;
			prnFMTCHR(dosREPORT,M_strEJT);	
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);	
						
		}catch(Exception L_EX){
			setMSG(L_EX,"prnFOOTR");
			}
		}
	
	/** getting the Table Header for Report Generation*/
	private void getTBLHDR(){
		try{
			crtLINE(80);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Grade",17));
			dosREPORT.writeBytes("\n  Lot No.   Date           Total  LOCATION Retention  Remark");
			dosREPORT.writeBytes("\n                          QTY/MT           Sample\n");
			crtLINE(80);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 5;
		}catch(Exception L_EX){
			setMSG(L_EX,"getTableHeader");
		}
	}


	  	
/**Print the Line at end of the Report  */
private void crtLINE(int LM_CNT)
{
	String strln = "";
		try{
		for(int i=1;i<=LM_CNT;i++){
		 strln += "-";
		}
		dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"CrtLine Method");
		}
}
}
	
	
