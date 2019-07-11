/*
System Name   : Finished Goods Inventory Management System
Program Name  : Suitable Lots Query / Report
Program Desc. : Gives Details of the lots for a particular party as per user requirement.
Author        : Ms. Aditi M. Kulkarni
Date          : 21 March 2007
Version       : FIMS 1.0

List of tables used :
Table Name                          Primary key                                             Operation done
                                                                                   Insert   Update   Query   Delete	
-------------------------------------------------------------------------------------------------------------------
HR_GRMST                             GR_GRDCD                                                          #
-------------------------------------------------------------------------------------------------------------------
*/

import javax.swing.*;
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

class hr_rpgrd extends cl_rbase
{
    JOptionPane L_OPTNPN;
    JButton btnDSPLY;                    /** JButton for DISPLAY */
	private String strPREVDT;            /** Variable for previous date */
	private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
	private String strFILNM;             /** String for generated Report File Name */
    private int intRECCT = 0;            /** Integer for counting the records fetched from DB */
    
	JCheckBox chkBOX;                    /** JCheckBox to display Remarks */
    
    JRadioButton rdbALL;               /** JRadioButton for ALL */
    JRadioButton rdbOFF;               /** JRadioButton for OFFICER */
    JRadioButton rdbTMR;               /** JRadioButton for TEAM MEMBER */
    JRadioButton rdbWRK;               /** JRadioButton for WORKER */
    ButtonGroup chkGRP;
    
    String strGRDCD,strSPALW,strLNSUB,strCONVY,strCHEDN,strMDALW,strLTALW,strPFALW;
    String strSAALW,strHRALW,strDNALW,strWKALW,strWSALW,strSTRDT,strENDDT,strEMPCT,strPEMPCT;
		
	/* Constructor */	
	hr_rpgrd()
	{
	    super(2);
		try
		{
		    setMatrix(18,8);
		    L_OPTNPN = new JOptionPane();
		    chkGRP = new ButtonGroup();
		        		    
		    add(chkBOX = new JCheckBox("    History Display"),8,3,1,1.5,this,'L');
		     	
		    M_pnlRPFMT.setVisible(true);
		    
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);

			add(new JLabel("Grade Category"),3,3,1,1,this,'L');					    
		    add(rdbALL = new JRadioButton("All"),3,4,1,1.5,this,'L');
			add(rdbOFF = new JRadioButton("Officer"),4,4,1,1.5,this,'L');
			add(rdbTMR = new JRadioButton("Team Member"),5,4,1,1.5,this,'L');
			add(rdbWRK = new JRadioButton("Worker"),6,4,1,1.5,this,'L');
	        chkGRP.add(rdbALL);
	        chkGRP.add(rdbOFF);
	        chkGRP.add(rdbTMR);
	        chkGRP.add(rdbWRK);
	               
	   	}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"Constructor");
	    }
	}       // end of constructor



/* super class Method overrided to enhance its functionality, to enable & disable components 
    according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{
         super.setENBL(L_flgSTAT);
    }
	
    public void actionPerformed(ActionEvent L_AE)
    {
         super.actionPerformed(L_AE);
         if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
         {
           if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
            if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() !=0)
                setENBL(true);
            else
            {
                setENBL(false);
            }
            
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
	
	/*	Method to print, display report as per selection   */
	void exePRINT()
	{
	    if (vldDATA())
		{
		    try
		    {
    		    if(M_rdbHTML.isSelected())
    		        strFILNM = cl_dat.M_strREPSTR_pbst +"hr_rpgrd.html";
    		    else if(M_rdbTEXT.isSelected())
    				strFILNM = cl_dat.M_strREPSTR_pbst + "hr_rpgrd.doc";
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
    					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Grade Details"," ");
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
     
    private void getALLREC()                //gets the records from stock
	{
	    try
		{
		    fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT  = 0 ;
	        cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
			setCursor(cl_dat.M_curWTSTS_pbst);
			prnFMTCHR(dosREPORT,M_strCPI17);
			cl_dat.M_PAGENO = 0;
							    
		    if(M_rdbHTML.isSelected())
		    {
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Grade Details</Title></HEAD><BODY><P><PRE style = \"font-size : 8 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
		    }
		    prnHEADER();      //gets the header of the report
		    
		    String strADDSTR = "";
		    if(rdbALL.isSelected())
		    {
		       M_strSQLQRY  = "Select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,";
			   M_strSQLQRY += " GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_EMPCT from HR_GRMST order by GR_EMPCT,GR_GRDCD ";
			}
		    
		    if(rdbOFF.isSelected())
		    {
		       M_strSQLQRY  = "Select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,";
			   M_strSQLQRY += " GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_EMPCT from HR_GRMST where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_EMPCT = 'O'";
			   M_strSQLQRY += " order by GR_GRDCD";
		    }
		    else if(rdbTMR.isSelected())
		    {
		       M_strSQLQRY  = "Select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,";
			   M_strSQLQRY += " GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_EMPCT from HR_GRMST where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_EMPCT = 'T'";
			   M_strSQLQRY += " order by GR_GRDCD"; 
		    }
			else if(rdbWRK.isSelected())
			{
			   M_strSQLQRY  = "Select GR_GRDCD,GR_SPALW,GR_LNSUB,GR_CONVY,GR_CHEDN,GR_MDALW,GR_LTALW,GR_PFALW,GR_SAALW,";
			   M_strSQLQRY += " GR_HRALW,GR_DNALW,GR_WKALW,GR_WSALW,GR_EMPCT from HR_GRMST where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_EMPCT = 'W'";
			   M_strSQLQRY += " order by GR_GRDCD";  
			}
								
            System.out.println("getALLREC Query = " + M_strSQLQRY);	
    	    ResultSet M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
    	    
    	    strPEMPCT = "x";
    	    if(M_rstRSSET !=null)
    	    {
    	        while(M_rstRSSET.next())
    		    {
    		       strEMPCT = nvlSTRVL(M_rstRSSET.getString("GR_EMPCT"),"");
    		       //System.out.println("EMPCT =" + strEMPCT);
    		       strGRDCD = M_rstRSSET.getString("GR_GRDCD");
    		       //System.out.println("GRDCD =" + strGRDCD);
    		       strSPALW = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_SPALW")),0),"");
    		       //System.out.println("SPALW =" + strSPALW);
    		       strLNSUB = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_LNSUB")),0),"");
    		       //System.out.println("LNSUB =" + strLNSUB);
    		       strCONVY = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_CONVY")),0),"");
    		       //System.out.println("CONVY =" + strCONVY);
    		       strCHEDN = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_CHEDN")),0),"");
    		       //System.out.println("CHEDN =" + strCHEDN);
    		       strMDALW = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_MDALW")),0),"");
    		       //System.out.println("MDALW =" + strMDALW);
    		       strLTALW = nvlSTRVL(M_rstRSSET.getString("GR_LTALW"),"");
    		       //System.out.println("LTALW =" + strLTALW);
    		       strPFALW = nvlSTRVL(M_rstRSSET.getString("GR_PFALW"),"");
    		       //System.out.println("PFALW =" + strPFALW);
    		       strSAALW = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_SAALW")),0),"");
    		       //System.out.println("SAALW =" + strSAALW);
    		       strHRALW = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_HRALW")),0),"");
    		       //System.out.println("HRALW =" + strHRALW);
                   strDNALW = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_DNALW")),0),"");
                   //System.out.println("DNALW =" + strDNALW);
                   strWKALW = nvlSTRVL(setNumberFormat(Double.parseDouble(M_rstRSSET.getString("GR_WKALW")),0),"");
                   //System.out.println("WKALW =" + strWKALW);
                   strWSALW = nvlSTRVL(setNumberFormat(M_rstRSSET.getDouble("GR_WSALW"),0),"");                  
                                      
                   if(rdbALL.isSelected())
                   {                
                       if(!(strEMPCT.equals(strPEMPCT)))
                       {
                          dosREPORT.writeBytes("\n");
                          prnFMTCHR(dosREPORT,M_strBOLD);
                          if(strEMPCT.equals("O"))
            			     dosREPORT.writeBytes(padSTRING('R',"Officer",15)+"\n");
            			  else if(strEMPCT.equals("T"))
                             dosREPORT.writeBytes(padSTRING('R',"Team Member",15)+"\n");
                          else if(strEMPCT.equals("W"))
                             dosREPORT.writeBytes(padSTRING('R',"Worker",15)+"\n");
                          else if(strEMPCT.equals(""))
                             dosREPORT.writeBytes(padSTRING('R',"--",15)+"\n");
                          prnFMTCHR(dosREPORT,M_strNOBOLD);
                       }
                   }
                   
                   if(M_rdbTEXT.isSelected())
                   {         
                       dosREPORT.writeBytes(padSTRING('R',strGRDCD,6));
                       dosREPORT.writeBytes(padSTRING('L',strSPALW,10)+padSTRING('L',strLNSUB,10));
                       dosREPORT.writeBytes(padSTRING('L',strCONVY,10)+padSTRING('L',strCHEDN,10)+padSTRING('L',strMDALW,10));
                       dosREPORT.writeBytes(padSTRING('L',strLTALW,10)+padSTRING('L',strPFALW,10)+padSTRING('L',strSAALW,8));
                       dosREPORT.writeBytes(padSTRING('L',strHRALW,8)+padSTRING('R',"",5)+padSTRING('L',strDNALW,5)+padSTRING('L',strWKALW,10)+padSTRING('L',strWSALW,10)+"\n");
                   }
                   else
                   {
                       dosREPORT.writeBytes(padSTRING('R',strGRDCD,5));
                       dosREPORT.writeBytes(padSTRING('L',strSPALW,8)+padSTRING('L',strLNSUB,8));
                       dosREPORT.writeBytes(padSTRING('L',strCONVY,9)+padSTRING('L',strCHEDN,10)+padSTRING('L',strMDALW,10));
                       dosREPORT.writeBytes(padSTRING('L',strLTALW,10)+padSTRING('L',strPFALW,10)+padSTRING('L',strSAALW,8));
                       dosREPORT.writeBytes(padSTRING('L',strHRALW,8)+padSTRING('R',"",3)+padSTRING('L',strDNALW,5)+padSTRING('L',strWKALW,8)+padSTRING('L',strWSALW,8)+"\n");
                   }
                  strPEMPCT = strEMPCT;
                  
                }
    		    intRECCT = 1;
    	    }
    	    crtLINE(130,"-");
    	    M_rstRSSET.close();
    	    setMSG("Report completed.. ",'N');
        	dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
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
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",62)+padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,61)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			String strCATGY = "";
    			if(rdbALL.isSelected())
    			    strCATGY = "All";
    			else if(rdbOFF.isSelected())
    			    strCATGY = "Officer";
    			else if(rdbTMR.isSelected())
    			    strCATGY = "Team Member";
    			else if(rdbWRK.isSelected())
    			    strCATGY = "Worker";
    			dosREPORT.writeBytes(padSTRING('R',"Grade Details for "+ strCATGY +" category",99)+padSTRING('L',"Page No    : " + cl_dat.M_PAGENO,15));
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(130,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"Grade",12)+padSTRING('R',"Spcl",9)+padSTRING('R',"Lunch ",9)+padSTRING('R',"Convy.",10)+padSTRING('R',"Educn",11)+padSTRING('R',"Mdcl",11)+ padSTRING('R',"LTA",10)+padSTRING('R',"PF",8)+padSTRING('R',"Spr",8)+padSTRING('R',"HRA",8)+padSTRING('R',"",5)+padSTRING('R',"D.A",7)+padSTRING('R',"Wrk",10)+padSTRING('R',"Wsh",10)+"\n");
    			dosREPORT.writeBytes(padSTRING('R',"",12)+padSTRING('R',"Allw",9)+padSTRING('R',"Sbsdy",10)+padSTRING('R',"",10)+padSTRING('R',"Allw",10)+padSTRING('R',"Allw",11)+ padSTRING('R',"%",10)+padSTRING('R',"%",7)+padSTRING('R',"Ann.",10)+padSTRING('R',"",10)+padSTRING('R',"",5)+padSTRING('R',"",3)+padSTRING('R',"Allw",10)+padSTRING('R',"Allw",10)+"\n");
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(130,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
		    }
		    else
		    {
                cl_dat.M_PAGENO += 1;
    			cl_dat.M_intLINNO_pbst = 0;
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes("\n");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",50)+padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,61)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			String strCATGY = "";
    			if(rdbALL.isSelected())
    			    strCATGY = "All";
    			else if(rdbOFF.isSelected())
    			    strCATGY = "Officer";
    			else if(rdbTMR.isSelected())
    			    strCATGY = "Team Member";
    			else if(rdbWRK.isSelected())
    			    strCATGY = "Worker";
    			dosREPORT.writeBytes(padSTRING('R',"Grade Details for "+ strCATGY +" category",87)+padSTRING('L',"Page No    : " + cl_dat.M_PAGENO,15));
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(130,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"Grade",9)+padSTRING('R',"Spcl",7)+padSTRING('R',"Lunch ",8)+padSTRING('R',"Convy.",11)+padSTRING('R',"Educn",11)+padSTRING('R',"Mdcl",11)+ padSTRING('R',"LTA",10)+padSTRING('R',"PF",8)+padSTRING('R',"Spr",8)+padSTRING('R',"HRA",8)+padSTRING('R',"",1)+padSTRING('R',"D.A",9)+padSTRING('R',"Wrk",6)+padSTRING('R',"Wsh",8)+"\n");
    			dosREPORT.writeBytes(padSTRING('R',"",9)+padSTRING('R',"Allw",7)+padSTRING('R',"Sbsdy",9)+padSTRING('R',"",11)+padSTRING('R',"Allw",10)+padSTRING('R',"Allw",11)+ padSTRING('R',"%",10)+padSTRING('R',"%",7)+padSTRING('R',"Ann.",10)+padSTRING('R',"",10)+padSTRING('R',"",3)+padSTRING('R',"",3)+padSTRING('R',"Allw",6)+padSTRING('R',"Allw",8)+"\n");
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(130,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    		}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");	
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
			dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtLINE");	
		}
	}
     
}           // end of class