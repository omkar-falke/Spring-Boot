/*
System Name   : Finished Goods Inventory Management System
Program Name  : Inventory of Despatched Lots
Program Desc. : Gives Detail regarding previous days despatched Lots.
Author        : Mr. Deepal N. Mehta
Date          : 22nd May 2001
Version       : FIMS 1.0
Modifications 
Modified By   : Ms. Aditi M. Kulkarni.
Modified Date :
Version       :

List of tables used :
Table Name     Primary key                         Operation done
                                          Insert   Update   Query   Delete	
----------------------------------------------------------------------------
FG_STMST       ST_LOTNO,ST_MNLCD                              #
CO_PRMST         PR_PRDCD                                     #
----------------------------------------------------------------------------
*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;

public class fg_qrloc extends cl_rbase
{
    JOptionPane LM_OPTNPN;
    JButton btnRUN;                      /** JButton for RUN **/
	private String strPREVDT;            /** Variable for previous date*/
	private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
	private String strFILNM;             /** String for generated Report File Name*/
    private int intRECCT = 0;            /** Integer for counting the records fetched from DB.*/
    
	cl_JTable tblSTKTBL;                 /** Table for stock details */
	
	int TB_STATUS = 0;
	int TB_GRADE = 1;
    int TB_LOTNO = 2;
    int TB_RCLNO = 2;
	int TB_STKQT = 3;
    int TB_MNLCD = 4;
    int TB_CPRCD = 5;
	int TB_PKGCT = 6;
	int TB_RETTG = 7;
	
	int intLMRGN = 0;
	Object[][] L_DATA;
	String strFRMDT,strTODAT,strPRDDS,strSTKQT;
	String strMNLCD,strCPRCD,strTPRCD,strPKGTP,strRETTG,strPRDCD;
	String strLOTNO="";
	String strRCLNO="";
	int intCOUNT = 0;
	
	/* Constructor */	
	fg_qrloc()
	{
	    super(2);
		try
		{
		    setMatrix(20,8);
		    
		    add(btnRUN = new JButton("RUN"),3,5,1,1.5,this,'L');
		    
		    String[] strSTKHD = {"Status","Grade","Lot No.","Rcl.No.","Stk Qty.","Main Loc.","Classified Product Code","Pkg Category","Retention Tag"};
        	int [] intCOLSZ = {50,90,90,90,90,90,90,90,90};
        	tblSTKTBL = crtTBLPNL1(this,strSTKHD,300,5,1,8,6.5,intCOLSZ,new int[] {0});
		    
		    btnRUN.addActionListener(this);
		    btnRUN.addFocusListener(this);
		    btnRUN.addKeyListener(this);
		    
		    M_pnlRPFMT.setVisible(true);
		}
		catch(Exception L_EX)
        {
			setMSG (L_EX,"Constructor");
	    }
	}
	 
	/* super class Method overrided to enhance its functionality, to enable & disable components 
    according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{
         super.setENBL(L_flgSTAT);
		 M_txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
		 M_txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		 btnRUN.setEnabled(true);
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
	    try
	    {
	        super.actionPerformed(L_AE);		
		    strFRMDT = M_txtFMDAT.getText().toString().trim();
		    strTODAT = M_txtTODAT.getText().toString().trim();
		    //LM_ACTTXT = L_AE.getActionCommand();
		    
		    if(M_objSOURC == btnRUN)
		    {
		        if(vldDATA())
		        {
		            //System.out.println("Out of vldDATA ");	
        			setCursor(cl_dat.M_curWTSTS_pbst);
        			setMSG("Data Fetching in Progress... ",'N');
        			int i = 0;
        			
                    String L_strSQLQRY;	
        			L_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,ST_RCLNO,ST_STKQT,ST_MNLCD,ST_CPRCD,ST_PKGCT,LT_RETQT,ST_TPRCD,ST_PRDCD from FG_STMST,CO_PRMST,PR_LTMST";
    			    L_strSQLQRY += " where ST_PRDCD=PR_PRDCD and ST_PRDTP=LT_PRDTP and ST_CMPCD = LT_CMPCD and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_LOTNO in ";
    			    L_strSQLQRY += "(select ist_lotno from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_stsfl='2' and ist_autdt between '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim())) +"'";
    			    L_strSQLQRY += " and '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim())) +"' and ist_mkttp in ('01','03','04','05')) order by st_lotno,st_rclno";
    			    System.out.println(" btnRUN Query = " + L_strSQLQRY);	
    			    ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
        			
        			if(L_rstRSSET !=null)
			        {
            			while(L_rstRSSET.next())
            			{
            			    tblSTKTBL.setValueAt(new Boolean(false),i,0);
            			    tblSTKTBL.setValueAt(L_rstRSSET.getString("PR_PRDDS"),i,TB_GRADE);
            				tblSTKTBL.setValueAt(L_rstRSSET.getString("ST_LOTNO"),i,TB_LOTNO);
            				tblSTKTBL.setValueAt(L_rstRSSET.getString("ST_LOTNO"),i,TB_RCLNO);
            				tblSTKTBL.setValueAt(L_rstRSSET.getString("ST_STKQT"),i,TB_STKQT);
            				tblSTKTBL.setValueAt(L_rstRSSET.getString("ST_MNLCD"),i,TB_MNLCD);
            				tblSTKTBL.setValueAt(L_rstRSSET.getString("ST_CPRCD"),i,TB_CPRCD);
            				tblSTKTBL.setValueAt(L_rstRSSET.getString("ST_PKGCT"),i,TB_PKGCT);
            				tblSTKTBL.setValueAt(L_rstRSSET.getString("LT_RETQT"),i,TB_RETTG);
            				i++;
            			}
            		}
        			if(L_rstRSSET != null)
        				L_rstRSSET.close();
        			setMSG("Data Fetched Successfully.",'N');
        			setCursor(cl_dat.M_curDFSTS_pbst);
		        }
		        else
		            return;
		    }
		}
	    catch(Exception L_EX)
        {
			setMSG (L_EX,"Action Performed");
        }
	}
	
	/*	Method to print, display report as per selection   */
	void exePRINT()
	{
	    //System.out.println("In exePRINT");
		if (vldDATA())
		{
		    try
		    {
    		    if(M_rdbHTML.isSelected())
    		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_qrloc.html";
    		    else if(M_rdbTEXT.isSelected())
    				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_qrloc.doc";
    			//System.out.println("HHH");				
    			getSTKREC();
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
        			    p  = r.exec("C:\\windows\\wordpad.exe " + strFILNM); 
    			}				
    			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
    			{
    				cl_eml ocl_eml = new cl_eml();				    
    				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
    				{
    					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Inventory of Despatched Lots"," ");
    					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
    				}
    			}
    		}
    		catch(Exception L_EX)
    		{
    			setMSG(L_EX,"exePRINT");
    		}	
    	}
	}
	
	/*  Method to validate data */	        
    boolean vldDATA()
	{
	    try
	    {
	        //System.out.println("In vldDATA");
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
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}	
		return true;
    }
	
	private void getSTKREC()                //gets the records from stock
	{
	    try
		{
		    //System.out.println("In getSTKREC");
		    fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT  = 0 ;
	        cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
			setCursor(cl_dat.M_curWTSTS_pbst);
			prnFMTCHR(dosREPORT,M_strCPI17);
			cl_dat.M_PAGENO = 0;
			
			strFRMDT = M_txtFMDAT.getText().toString().trim();
		    strTODAT = M_txtTODAT.getText().toString().trim();
		    
		    if(M_rdbHTML.isSelected())
		    {
		        
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
		    }
		    
			prnHEADER();      //gets the header of the report
			
			//System.out.println("abab");
			/*L_strSQLQRY = "Select PR_PRDDS,ST_LOTNO,ST_MNLCD,sum(ST_STKQT+ST_UCLQT) L_STKQT from FG_STMST,CO_PRMST";
			L_strSQLQRY += " where ST_PRDCD=PR_PRDCD and ST_LOTNO in (select distinct ist_lotno from fg_istrn";
			L_strSQLQRY += " where ist_stsfl='2' and ist_autdt between "+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+" and ";
			L_strSQLQRY += M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+" and ist_mkttp in ('01','03','04','05'))";
			L_strSQLQRY += " group by pr_prdds,st_lotno,st_mnlcd order by st_lotno";*/
			
			M_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,ST_MNLCD,sum(ST_STKQT + ST_UCLQT) L_STKQT from ";
			M_strSQLQRY += " FG_STMST,CO_PRMST where ST_PRDCD = PR_PRDCD and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_LOTNO in ( select distinct";
			M_strSQLQRY += " IST_LOTNO from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_STSFL = '2' and IST_AUTDT between ";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+"' and ";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'";
			M_strSQLQRY += "and ist_mkttp in ('01','03','04','05')) group by PR_PRDDS,ST_LOTNO,ST_MNLCD order by ST_LOTNO";
			System.out.println("Query :" + M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			while(M_rstRSSET.next())
			{
				strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
				strLOTNO = nvlSTRVL(M_rstRSSET.getString("ST_LOTNO"),"");
				strSTKQT = nvlSTRVL(M_rstRSSET.getString("L_STKQT"),"");
				strMNLCD = nvlSTRVL(M_rstRSSET.getString("ST_MNLCD"),"");
				dosREPORT.writeBytes(padSTRING('R',strLOTNO,15)+padSTRING('R',strPRDDS,13)+padSTRING('R',strMNLCD,10));
				if(strSTKQT.equals("0.000"))
					dosREPORT.writeBytes(padSTRING('L',"-",15));
				else
					dosREPORT.writeBytes(padSTRING('L',strSTKQT,15));
				dosREPORT.writeBytes(padSTRING('R',"",5));
				crtLINE(10,"_");
				dosREPORT.writeBytes(padSTRING('R',"",5));
				crtLINE(10,"_");
				dosREPORT.writeBytes(padSTRING('R',"",5));
				crtLINE(25,"_");
				dosREPORT.writeBytes("\n\n");
				cl_dat.M_intLINNO_pbst +=2;
				intRECCT = 1;
				if(cl_dat.M_intLINNO_pbst >= 57)
				{
					crtLINE(113,"-");
					dosREPORT.writeBytes ("\n\n\n\n\n");
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('L',"CHECKED BY",40)+padSTRING('L',"VERIFIED BY  ",40));
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes("\n");
					crtLINE(113,"-");
					prnFMTCHR(dosREPORT,M_strEJT);				
					prnHEADER();
					}
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst+=1;
				crtLINE(113,"-");
				dosREPORT.writeBytes ("\n\n\n\n\n");
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('L'," CHECKED BY ",40)+padSTRING('L'," VERIFIED BY  ",40));
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes("\n");
				crtLINE(113,"-");
				prnFMTCHR(dosREPORT,M_strEJT);				
				//prnFMTCHR(dosREPORT,M_NOCPI17);
				//prnFMTCHR(dosREPORT,M_CPI10);		
				dosREPORT.close();
				fosREPORT.close();
				setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTKREC");
		}
	}
	
	private void crtLINE(int intCOUNT,String LP_SEPRTR)    //method to print lines in the report
	{
		String strln = "";
		try
		{
			for(int i=1;i<=intCOUNT;i++)
			{
		    	strln += LP_SEPRTR;
			}
			dosREPORT.writeBytes(padSTRING('L'," ",intLMRGN));
			dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtLINE");	
		}
	}
	
	private void prnHEADER()                               // gets the header of the report
	{
		try
		{
		    if(M_rdbTEXT.isSelected())
		    {
    			cl_dat.M_PAGENO += 1;
    			cl_dat.M_intLINNO_pbst = 0;
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",25)+padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,85)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes(padSTRING('R',"Inventory of Despatched Lots from "+strFRMDT+" to "+strTODAT+"",65)+padSTRING('L',"Page No:" + cl_dat.M_PAGENO,34));
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(113,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes(padSTRING('R',"Lot No.",15)+padSTRING('R',"Grade",13)+padSTRING('R',"Location",10)+padSTRING('L',"Stock Qty.",15)+padSTRING('R',"",5)+padSTRING('R',"Physical",15)+padSTRING('R',"Difference",15)+padSTRING('R',"Remarks",15)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(113,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
		    }
		    else
		    {
                cl_dat.M_PAGENO += 1;
    			cl_dat.M_intLINNO_pbst = 0;
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25)+padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,85)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes(padSTRING('R',"Inventory of Despatched Lots from "+strFRMDT+" to "+strTODAT+"",65)+padSTRING('L',"Page No:" + cl_dat.M_PAGENO,31));
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(113,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes(padSTRING('R',"Lot No.",15)+padSTRING('R',"Grade",13)+padSTRING('R',"Location",10)+padSTRING('L',"Stock Qty.",15)+padSTRING('R',"",5)+padSTRING('R',"Physical",15)+padSTRING('R',"Difference",15)+padSTRING('R',"Remarks",15)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(113,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    		}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");	
		}
    }    
}

    