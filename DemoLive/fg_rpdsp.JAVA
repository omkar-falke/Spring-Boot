/**
System Name   : Finished Goods Inventory Management System
Program Name  : Despatch Details Report
Program Desc. : Report for taking the Despatch details corresponding to the Products
				
Author        : Mr. S.C.SANGOLLI
Date          : 28th May 2001
Version       : FIMS 1.0

Modificaitons 
Modified By    : Ms.Aditi M. Kulkarni
Modified Date  : 25.12.2006
Version        : FIMS 1.0

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN      IVT_PRDDS,IST_MKTTP,IST_SALTP                            #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtPRDTP    PT_PRTTP       CO_CDTRN      VARCHAR(1)     Product Type
txtMKTTP    PT_PRTCD       CO_CDTRN      VARCHAR(5)     Market Type 
txtSALTP    PT_PRTNM       CO_CDTRN      VARCHAR(40)    Sale Type
txtPRDCD    IST_PRDCD      FG_ISTRN      VARCHAR(10)    Product Code
--------------------------------------------------------------------------------------
<B>Conditions Given in Query:</b
*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.*;
import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.DataOutputStream; 
import java.io.*; 
import java.awt.Cursor.*;
import java.util.*;
import javax.swing.undo.*;
import java.awt.Component;
import java.sql.ResultSet;

class fg_rpdsp extends cl_rbase
{
	JTextField txtFRMDT;                 /** JTextField to display Start Date.*/
	JTextField txtTODAT;                 /** JTextField to display End Date.*/
	JTextField txtPRDTP;                 /** JTextField to display Product Type.*/
	JTextField txtMKTTP;                 /** JTextField to display Market Type.*/
	JTextField txtSALTP;                 /** JTextField to display Sales Type.*/
	JTextField txtPRDCD;                 /** JTextField to display Product Code.*/
	JTextField txtTRPCD;                 
	
	JLabel     lblMKTTP;                 
	JLabel     lblPRDTP;                 
	JLabel     lblSALTP;                 
	JLabel     lblPRDDS;                 /** JLabel to display Product Description*/
	JLabel     lblTRPNM; 
	
	JOptionPane L_strOPTNPN;
   	private String strPREVDT;            /** Variable for previous date*/
	private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
	private String strFILNM;             /** String for generated Report File Name*/
    private int intRECCT = 0;            /** Integer for counting the records fetched from DB.*/
    
   	String L_strMNGRP,L_strSBGRP,L_strDORNO,L_strDORDT,L_strTRPCD,L_strLRYNO,L_strCNTDS,L_strPRTCD,L_strDSTDS;
	String L_strINVNO,L_strISSNO,L_strISSDT,L_strLOTNO,L_strRCLNO,L_strSALTP,L_strPRDDS;
	String L_strMNGRP1,L_strMNGRP2,L_strSBGRP1,L_strSBGRP2,L_strISSDT1,L_strISSDT2,L_strSALTP1,L_strSALTP2;
	String L_strPRDDS1,L_strPRDDS2,L_strISSNO1,L_strISSNO2,L_strDORNO1,L_strDORNO2,L_strDORDT1,L_strDORDT2;
	String L_strTRPCD1,L_strTRPCD2,L_strLRYNO1,L_strLRYNO2,L_strCNTDS1,L_strCNTDS2,L_strPRTCD1,L_strPRTCD2;
	String L_strDSTDS1,L_strDSTDS2,L_strINVNO1,L_strINVNO2,L_strLOTNO1,L_strLOTNO2,L_strRCLNO1,L_strRCLNO2;
	String L_strSALDS,L_strMGRDS,L_strSGRDS,L_strTRNSP,L_strPRTNM;
	
	/*	
	String L_strMNGRP1 = "" , String L_strMNGRP2 = ""; 
	String L_strSBGRP1 = "" , String L_strSBGRP2 = "";
	String L_strISSDT1 = "" , String L_strISSDT2 = "";
	String L_strSALTP1 = "" , String L_strSALTP2 = "";
	String L_strPRDDS1 = "" , String L_strPRDDS2 = "";
	String L_strISSNO1 = "" , String L_strISSNO2 = "";
	String L_strDORNO1 = "" , String L_strDORNO2 = "";
	String L_strDORDT1 = "" , String L_strDORDT2 = "";
	String L_strTRPCD1 = "" , String L_strTRPCD2 = "";
	String L_strLRYNO1 = "" , String L_strLRYNO2 = "";
	String L_strCNTDS1 = "" , String L_strCNTDS2 = "";
	String L_strPRTCD1 = "" , String L_strPRTCD2 = "";
	String L_strDSTDS1 = "" , String L_strDSTDS2 = "";
	String L_strINVNO1 = "" , String L_strINVNO2 = "";
	String L_strLOTNO1 = "" , String L_strLOTNO2 = "";
	String L_strRCLNO1 = "" , String L_strRCLNO2 = "";
	*/
			
	String L_strTOTMGQT = "0";
	String L_strTOTSGQT = "0";
	String L_strTOTDTQT = "0";
	String L_strTOTSTQT = "0";
	String L_strTOTPRQT = "0";
	String L_strTOTLTQT = "0";
	String L_strTOTGTQT = "0";
	String L_strTOTXXQT = "0";
	
	String L_strTOTMGQT1,L_strTOTSGQT1,L_strTOTDTQT1,L_strTOTSTQT1,L_strTOTPRQT1,L_strTOTLTQT1,L_strTOTGTQT1;
	
	//int L_intLMRGN = 0;
	String L_strISSQT = "";
	double L_dblISSQT = 0;
    boolean flgHDRFL ;
    
    StringBuffer L_strPRNSTR,L_str2ndLIN;
       
	fg_rpdsp()
     {
		super(2);
		try
		{
		        setMatrix(20,8);
		
		   	    add(new JLabel("Market Type"),6,4,1,.8,this,'L');
			    add(new JLabel("Product Type"),7,4,1,.8,this,'L');
			    add(new JLabel("Sale Type"),8,4,1,.8,this,'L');
			    add(new JLabel("Product Code"),9,4,1,.8,this,'L');
				add(new JLabel("Transporter"),10,4,1,.8,this,'L');
				
				add(txtMKTTP = new TxtLimit(2),6,5,1,0.5,this,'L');
			    add(txtPRDTP = new TxtLimit(4),7,5,1,0.5,this,'L');
			    add(txtSALTP = new TxtLimit(2),8,5,1,0.5,this,'L');
			    add(txtPRDCD = new TxtLimit(10),9,5,1,1,this,'L');
				add(txtTRPCD = new TxtLimit(5),10,5,1,1,this,'L');
				
			    add(lblMKTTP = new JLabel(""),6,6,1,2,this,'L');
			    add(lblPRDTP = new JLabel(""),7,6,1,2,this,'L');
			    add(lblSALTP = new JLabel(""),8,6,1,2,this,'L');
			    add(lblPRDDS = new JLabel(""),9,6,1,2,this,'L');
				add(lblTRPNM = new JLabel(""),10,6,1,2,this,'L');
											 	
				txtPRDTP.setEnabled(false);
			    txtMKTTP.setEnabled(false);
    		    txtSALTP.setEnabled(false);
    		    txtPRDCD.setEnabled(false);
				txtTRPCD.setEnabled(false);
    		    txtPRDTP.addActionListener(this);
    		    txtMKTTP.addActionListener(this);
    		    txtSALTP.addActionListener(this);
    		    txtPRDCD.addActionListener(this);
				txtTRPCD.addActionListener(this);
    		    txtPRDTP.addFocusListener(this);
    		    txtMKTTP.addFocusListener(this);
    		    txtSALTP.addFocusListener(this);
    		    txtPRDCD.addFocusListener(this);
				txtTRPCD.addFocusListener(this);
    		    txtPRDTP.addKeyListener(this);
    		    txtMKTTP.addKeyListener(this);
    		    txtSALTP.addKeyListener(this);
    		    txtPRDCD.addKeyListener(this);
				txtTRPCD.addKeyListener(this);
    		    txtPRDTP.setActionCommand("txtPRDTP");
                txtMKTTP.setActionCommand("txtMKTTP");
                txtSALTP.setActionCommand("txtSALTP");
                txtPRDCD.setActionCommand("txtPRDCD");
				txtTRPCD.setActionCommand("txtTRPCD");
                M_pnlRPFMT.setVisible(true);
                
                M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));  // Convert Into Local Date Format
                M_calLOCAL.add(Calendar.DATE,-1);                              // Decrease Date by 1 from Login Date
                strPREVDT = M_fmtLCDAT.format(M_calLOCAL.getTime());           // Assign Date to Variable 
                M_txtFMDAT.setText(strPREVDT);
                
                //setENBL(false);
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
		 
	}

/* method to be executed when event is fired on press of a key */
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
 	        setCursor(cl_dat.M_curWTSTS_pbst);
 	         	        
 	        if(M_objSOURC == txtMKTTP)
 	        {
 	                try
 	                {
 	                    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP='COXXMKT'";
 	                    M_strHLPFLD = "txtMKTTP";
 	                    //System.out.println(M_strSQLQRY);
 	                    cl_hlp(M_strSQLQRY,2,1,new String[]{"Market Type","Description"},2,"CT");
 	                }
 	                catch(Exception L_EX)
    			    {
    				    setMSG(L_EX ," F1 help..");    		    
    			    }
    		}
            if(M_objSOURC == txtPRDTP)
 	        {
 	                try
 	                {
 	                    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'PRXXPRS'and CMT_CCSVL = '"+txtMKTTP.getText()+"' ";
 	                    M_strHLPFLD = "txtPRDTP";
 	                    //System.out.println(M_strSQLQRY);
                        cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Type","Description"},2,"CT");
 	                }
 	                catch(Exception L_EX)
    			    {
    				    setMSG(L_EX ," F1 help..");    		    
    			    } 
 	        }

    		if(M_objSOURC == txtSALTP)
 	        {
 	                try
 	                {
 	                    M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MR00SAL'";
 	                    M_strHLPFLD = "txtSALTP";
 	                    //System.out.println(M_strSQLQRY);
 	                    cl_hlp(M_strSQLQRY,2,1,new String[]{"Sale Type","Description"},2,"CT");
 	                }
 	                catch(Exception L_EX)
    			    {
    				    setMSG(L_EX ," F1 help..");    		    
    			    } 
 	        }  
    		if(M_objSOURC == txtPRDCD)
 	        {
 	                try
 	                {
 	                    M_strSQLQRY = "Select PR_PRDCD,PR_PRDDS from CO_PRMST where PR_PRDCD in (select IST_PRDCD from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"') order by PR_PRDCD";
 	                    M_strHLPFLD = "txtPRDCD";
 	                    //System.out.println(M_strSQLQRY);
 	                    cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Grade"},2,"CT");
 	                }
 	                catch(Exception L_EX)
    			    {
    				    setMSG(L_EX ," F1 help..");    		    
    			    } 
 	        }  
			if(M_objSOURC == txtTRPCD)
 	        {
 	                try
 	                {
 	                    M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from co_ptmst where pt_prttp='T'";
 	                    M_strHLPFLD = "txtTRPCD";
 	                    //System.out.println(M_strSQLQRY);
 	                    cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Name"},2,"CT");
 	                }
 	                catch(Exception L_EX)
    			    {
    				    setMSG(L_EX ," F1 help..");    		    
    			    } 
 	        }  
 	        setCursor(cl_dat.M_curDFSTS_pbst);
 	    }
 	}
	
/* 	Method for execution of help for Memo Number Field. */
	void exeHLPOK()
	{
		cl_dat.M_flgHELPFL_pbst = false;
		super.exeHLPOK();
		cl_dat.M_wndHLP_pbst=null;
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD == "txtPRDTP")
		{			
			txtPRDTP.setText(L_STRTKN.nextToken());
			lblPRDTP.setText(L_STRTKN.nextToken());
			txtSALTP.requestFocus();				
		}
		else if(M_strHLPFLD == "txtMKTTP")
		{
		    txtMKTTP.setText(L_STRTKN.nextToken());
			lblMKTTP.setText(L_STRTKN.nextToken());
		    txtPRDTP.requestFocus();
		}
		else if(M_strHLPFLD == "txtSALTP")
		{
		    txtSALTP.setText(L_STRTKN.nextToken());
			lblSALTP.setText(L_STRTKN.nextToken());
		    cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		else if(M_strHLPFLD == "txtPRDCD")
		{
			txtPRDCD.setText(L_STRTKN.nextToken());
			lblPRDDS.setText(L_STRTKN.nextToken());
		    cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		else if(M_strHLPFLD == "txtTRPCD")
		{
			txtTRPCD.setText(L_STRTKN.nextToken());
			lblTRPNM.setText(L_STRTKN.nextToken());
		    cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
	
/*	Method to print, display report as per selection   */
	void exePRINT()
	{
		if (!vldDATA())
		{				
			setMSG("Please enter Market Type Or Press F1 for Help..",'N');
			return;
		}
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rpdsp.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpdsp.doc";				
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Details"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}	
	}
	
/*  Method to fetch data from CO_PTMST table & club it with Header in Data Output Stream  */
	private void getALLREC()
	{
		try
		{
		    //System.out.println("sss");
		    fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT  = 0 ;
	        cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
   		    //System.out.println("ask");
	        /*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		    {
		        prnFMTCHR(dosREPORT,M_strCPI17);
		        
		    }*/
   		    if(M_rdbHTML.isSelected())
		    {
		        
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
		    }
   		    prnHEADER();                  //gets the header of the report
   		    
   		    L_strMNGRP1 = "";   
	        L_strSBGRP1 = "";  
	        L_strISSDT1 = ""; 
	        L_strSALTP1 = ""; 
	        L_strPRDDS1 = "";   
	        L_strISSNO1 = "";   
	        L_strDORNO1 = "";   
	        L_strDORDT1 = ""; 
	        L_strTRPCD1 = ""; 
	        L_strLRYNO1 = "";  
	        L_strCNTDS1 = "";  
	        L_strPRTCD1 = "";  
	        L_strDSTDS1 = "";   
	        L_strINVNO1 = ""; 
	        L_strLOTNO1 = "";   
	        L_strRCLNO1 = "";  
	        
	        L_strMNGRP2 = "";
	        L_strSBGRP2 = "";
	        L_strISSDT2 = "";
	        L_strSALTP2 = "";
	        L_strPRDDS2 = "";
	        L_strISSNO2 = "";
	        L_strDORNO2 = ""; 
	        L_strDORDT2 = "";
	        L_strTRPCD2 = "";
	        L_strLRYNO2 = "";
	        L_strCNTDS2 = "";
	        L_strPRTCD2 = "";
	        L_strDSTDS2 = "";
	        L_strINVNO2 = "";
	        L_strLOTNO2 = "";
	        L_strRCLNO2 = ""; 
	        
	        L_strTOTMGQT = "0";
	        L_strTOTSGQT = "0";
	        L_strTOTDTQT = "0";
	        L_strTOTSTQT = "0";
	        L_strTOTPRQT = "0";
	        L_strTOTLTQT = "0";
	        L_strTOTGTQT = "0";
	        L_strTOTXXQT = "0";
	         
	        //System.out.println("aaa");
	        M_strSQLQRY  = "Select IST_SALTP,SUBSTRING(IST_PRDCD,1,2) L_MNPRD,SUBSTRING(IST_PRDCD,1,4) L_SBPRD,IVT_PRDDS,";
			M_strSQLQRY += "IST_ISSNO,IST_AUTDT,IST_LOTNO,IST_RCLNO,ivt_dorno,ivt_dordt,ivt_trpcd,ivt_lryno,ivt_cntds,";
			M_strSQLQRY += "ivt_byrcd,ivt_dstds,ivt_invno,sum(isnull(IST_ISSQT,0)) IST_ISSQT from mr_ivtrn,FG_ISTRN";
			M_strSQLQRY += " where IST_CMPCD = IVT_CMPCD and IST_MKTTP = ivt_mkttp and IST_ISSNO = ivt_ladno and IST_PRDCD = IVT_PRDCD and IST_PKGTP = IVT_PKGTP ";
			M_strSQLQRY += " and IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+"'and'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"' ";
			M_strSQLQRY += " and IST_STSFL='2'"+(txtTRPCD.getText().length()==5 ? " and IVT_TRPCD = '"+txtTRPCD.getText()+"'" : "")+(txtMKTTP.getText().length()==2 ? " and IST_MKTTP = '"+txtMKTTP.getText()+"'" : "")+(txtSALTP.getText().length()==2 ? " and IVT_SALTP = '"+txtSALTP.getText()+"'" : "")+(txtPRDTP.getText().length()==4 ? " and SUBSTRING(IST_PRDCD,1,4) = '"+txtPRDTP.getText()+"'" : "")+(txtPRDCD.getText().length()==10 ? " and IST_PRDCD = '"+txtPRDCD.getText()+"'" : "");
			M_strSQLQRY += " group by IST_AUTDT,SUBSTRING(IST_PRDCD,1,2),IST_SALTP,SUBSTRING(IST_PRDCD,1,4),ivt_prdds,";
			M_strSQLQRY += "IST_ISSNO,ivt_dorno,ivt_dordt,ivt_byrcd,ivt_invno,ivt_trpcd,ivt_lryno,ivt_cntds,ivt_dstds,IST_LOTNO,IST_RCLNO";
			M_strSQLQRY += " order by IST_AUTDT,L_MNPRD,IST_SALTP,L_SBPRD,ivt_prdds,";
			M_strSQLQRY += "IST_ISSNO,ivt_dorno,ivt_dordt,ivt_byrcd,ivt_invno,ivt_trpcd,ivt_lryno,ivt_cntds,ivt_dstds,IST_LOTNO,IST_RCLNO";
			
			//System.out.println(M_strSQLQRY);	    
		    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		    
		    boolean L_1STFL = true;
			boolean L_EOF = false;
			flgHDRFL = false;
			
			if(M_rstRSSET.next())
		    {		 
		        		      
		          while(!L_EOF)
		          {		                 
		                L_strMNGRP = M_rstRSSET.getString("L_MNPRD").trim();
					    L_strSBGRP = M_rstRSSET.getString("L_SBPRD").trim();
					    L_strDORNO = M_rstRSSET.getString("ivt_dorno").trim();
					    //System.out.println("003");
					    L_strDORDT = nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IVT_DORDT")).toString(),"");
					    L_strTRPCD = M_rstRSSET.getString("ivt_trpcd").trim();
					    L_strLRYNO = M_rstRSSET.getString("ivt_lryno").trim();
					    L_strCNTDS = M_rstRSSET.getString("ivt_cntds").trim();
					    L_strPRTCD = M_rstRSSET.getString("ivt_byrcd").trim();
					    L_strDSTDS = M_rstRSSET.getString("ivt_dstds").trim();
					    //System.out.println("004");
					    L_strINVNO = M_rstRSSET.getString("ivt_invno").trim();
					    L_strISSNO = M_rstRSSET.getString("IST_ISSNO").trim();
					    L_strISSDT = nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IST_AUTDT")).toString(),"");
					    L_strLOTNO = M_rstRSSET.getString("IST_LOTNO").trim();
					    L_strRCLNO = M_rstRSSET.getString("IST_RCLNO").trim();
					    L_strSALTP = M_rstRSSET.getString("IST_SALTP").trim();
					    L_strPRDDS = M_rstRSSET.getString("ivt_prdds").trim();
					    L_strISSQT = M_rstRSSET.getString("IST_ISSQT").trim();
					    L_dblISSQT = M_rstRSSET.getDouble("IST_ISSQT");
					   				    
		            /* 
                    if(cl_dat.M_intLINNO_pbst >= 68)
                    {
						//dosREPORT.writeBytes("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						prnHEADER();                  //gets the Header of the report
                    }*/
                    
                    if(L_1STFL)
                    {
                        // System.out.println("006");
						L_strMNGRP1 = L_strMNGRP;
						L_strSBGRP1 = L_strSBGRP;
						L_strISSDT1 = L_strISSDT;
						L_strSALTP1 = L_strSALTP;
						L_strPRDDS1 = L_strPRDDS;
						L_strISSNO1 = L_strISSNO;
						L_strDORNO1 = L_strDORNO;
						L_strDORDT1 = L_strDORDT;
						L_strTRPCD1 = L_strTRPCD;
						L_strLRYNO1 = L_strLRYNO;
						//System.out.println("007");
						L_strCNTDS1 = L_strCNTDS;
						L_strPRTCD1 = L_strPRTCD;
						L_strDSTDS1 = L_strDSTDS;
						L_strINVNO1 = L_strINVNO;
						L_strLOTNO1 = L_strLOTNO;
						L_strRCLNO1 = L_strRCLNO;
						L_strMNGRP2 = L_strMNGRP;
						L_strSBGRP2 = L_strSBGRP;
						L_strISSDT2 = L_strISSDT;
						L_strSALTP2 = L_strSALTP;
						L_strPRDDS2 = L_strPRDDS;
						L_strISSNO2 = L_strISSNO;
						//System.out.println("008");
						L_strDORNO2 = L_strDORNO;
						L_strDORDT2 = L_strDORDT;
						L_strTRPCD2 = L_strTRPCD;
						L_strLRYNO2 = L_strLRYNO;
						L_strCNTDS2 = L_strCNTDS;
						L_strPRTCD2 = L_strPRTCD;
						L_strDSTDS2 = L_strDSTDS;
						L_strINVNO2 = L_strINVNO;
						L_strLOTNO2 = L_strLOTNO;
						L_strRCLNO2 = L_strRCLNO;
						L_1STFL = false;
						//System.out.println("009");
                    }
                    prnGRPHDR("DT",1,"B");
					L_strISSDT1 = L_strISSDT;
					while((L_strISSDT).equals(L_strISSDT1) && !L_EOF)
					{
					    //System.out.println("010");
					    prnGRPHDR("MG",1,"B");
						L_strISSDT = L_strISSDT2;
						L_strISSDT1 = L_strISSDT;
						while((L_strISSDT+L_strMNGRP).equals(L_strISSDT1+L_strMNGRP1) && !L_EOF)
						{
						    //System.out.println("011");
							prnGRPHDR("ST",1,"B");
							L_strISSDT = L_strISSDT2;
							L_strMNGRP = L_strMNGRP2;
							L_strISSDT1 = L_strISSDT;
							L_strMNGRP1 = L_strMNGRP;
							while((L_strISSDT+L_strMNGRP+L_strSALTP).equals(L_strISSDT1+L_strMNGRP1+L_strSALTP1) && !L_EOF)
							{
							    //System.out.println("012");
								prnGRPHDR("SG",2,"B");
								L_strISSDT = L_strISSDT2;
								L_strMNGRP = L_strMNGRP2;
								L_strSALTP = L_strSALTP2;
								L_strISSDT1 = L_strISSDT;
								L_strMNGRP1 = L_strMNGRP;
								L_strSALTP1 = L_strSALTP;
								while((L_strISSDT+L_strMNGRP+L_strSALTP+L_strSBGRP).equals(L_strISSDT1+L_strMNGRP1+L_strSALTP1+L_strSBGRP1) && !L_EOF)
								{
								    //System.out.println("013");
									prnGRPHDR("PR",2,"B");
									L_strISSDT = L_strISSDT2;
									L_strMNGRP = L_strMNGRP2;
									L_strSALTP = L_strSALTP2;
									L_strSBGRP = L_strSBGRP2;
									L_strISSDT1 = L_strISSDT;
									L_strMNGRP1 = L_strMNGRP;
									L_strSALTP1 = L_strSALTP;
									L_strSBGRP1 = L_strSBGRP;
									while((L_strISSDT+L_strMNGRP+L_strSALTP+L_strSBGRP+L_strPRDDS).equals(L_strISSDT1+L_strMNGRP1+L_strSALTP1+L_strSBGRP1+L_strPRDDS1) && !L_EOF)
									{
									    //System.out.println("014");
										prnGRPHDR("IV",4,"N");
										L_strISSDT = L_strISSDT2;
										L_strMNGRP = L_strMNGRP2;
										L_strSALTP = L_strSALTP2;
										L_strSBGRP = L_strSBGRP2;
										L_strPRDDS = L_strPRDDS2;
										L_strISSDT1 = L_strISSDT;
										L_strMNGRP1 = L_strMNGRP;
										L_strSALTP1 = L_strSALTP;
										L_strSBGRP1 = L_strSBGRP;
										L_strPRDDS1 = L_strPRDDS;
										while((L_strISSDT+L_strMNGRP+L_strSALTP+L_strSBGRP+L_strPRDDS+L_strISSNO+L_strDORNO+L_strDORDT+L_strPRTCD+L_strINVNO+L_strTRPCD+L_strLRYNO+L_strCNTDS+L_strDSTDS).equals(L_strISSDT1+L_strMNGRP1+L_strSALTP1+L_strSBGRP1+L_strPRDDS1+L_strISSNO1+L_strDORNO1+L_strDORDT1+L_strPRTCD1+L_strINVNO1+L_strTRPCD1+L_strLRYNO1+L_strCNTDS1+L_strDSTDS1) && !L_EOF)
										{
										    //System.out.println("015");
											L_strISSDT = L_strISSDT2;
											L_strMNGRP = L_strMNGRP2;
											L_strSBGRP = L_strSBGRP2;
											L_strSALTP = L_strSALTP2;
											L_strPRDDS = L_strPRDDS2;
											L_strISSNO = L_strISSNO2;
											L_strDORNO = L_strDORNO2;
											L_strDORDT = L_strDORDT2;
											L_strTRPCD = L_strTRPCD2;
											L_strLRYNO = L_strLRYNO2;
											L_strCNTDS = L_strCNTDS2;
											L_strPRTCD = L_strPRTCD2;
											L_strDSTDS = L_strDSTDS2;
											//System.out.println("016");
											L_strINVNO = L_strINVNO2;
											L_strMNGRP1 = L_strMNGRP;
											L_strSBGRP1 = L_strSBGRP;
											L_strISSDT1 = L_strISSDT;
											L_strSALTP1 = L_strSALTP;
											L_strPRDDS1 = L_strPRDDS;
											L_strISSNO1 = L_strISSNO;
											L_strDORNO1 = L_strDORNO;
											L_strDORDT1 = L_strDORDT;
											L_strTRPCD1 = L_strTRPCD;
											L_strLRYNO1 = L_strLRYNO;
											L_strCNTDS1 = L_strCNTDS;
											L_strPRTCD1 = L_strPRTCD;
											L_strDSTDS1 = L_strDSTDS;
											L_strINVNO1 = L_strINVNO;
											//System.out.println("017");
			                                
											while((L_strISSDT+L_strMNGRP+L_strSALTP+L_strSBGRP+L_strPRDDS+L_strISSNO+L_strDORNO+L_strDORDT+L_strPRTCD+L_strINVNO+L_strTRPCD+L_strLRYNO+L_strCNTDS+L_strDSTDS+L_strLOTNO+L_strRCLNO).equals(L_strISSDT1+L_strMNGRP1+L_strSALTP1+L_strSBGRP1+L_strPRDDS1+L_strISSNO1+L_strDORNO1+L_strDORDT1+L_strPRTCD1+L_strINVNO1+L_strTRPCD1+L_strLRYNO1+L_strCNTDS1+L_strDSTDS1+L_strLOTNO1+L_strRCLNO1) && !L_EOF)
											{					
											    //System.out.println("018");							
												L_strTOTMGQT = setNumberFormat(Double.parseDouble(L_strTOTMGQT) + L_dblISSQT,3);
												L_strTOTSGQT = setNumberFormat(Double.parseDouble(L_strTOTSGQT) + L_dblISSQT,3);
												L_strTOTSTQT = setNumberFormat(Double.parseDouble(L_strTOTSTQT) + L_dblISSQT,3);
												L_strTOTDTQT = setNumberFormat(Double.parseDouble(L_strTOTDTQT) + L_dblISSQT,3);
												L_strTOTPRQT = setNumberFormat(Double.parseDouble(L_strTOTPRQT) + L_dblISSQT,3);
												L_strTOTLTQT = setNumberFormat(Double.parseDouble(L_strTOTLTQT) + L_dblISSQT,3);
												L_strTOTGTQT = setNumberFormat(Double.parseDouble(L_strTOTGTQT) + L_dblISSQT,3);
																																		
												if (!M_rstRSSET.next())
												{
												    L_EOF = true;
													break;
												}
                                    			/*if(cl_dat.M_intLINNO_pbst >= 64)
                                    			{
                                    			    dosREPORT.writeBytes("\n--------------------------------------------------------------------------------------------------------------------------------------------");
                                    				cl_dat.M_intLINNO_pbst = 0;
                                    				cl_dat.M_PAGENO += 1;
                                    				dosREPORT.writeBytes("\n");
                                    				cl_dat.M_intLINNO_pbst += 1;
                                    				//crtLINE(155);
                                    				prnFMTCHR(dosREPORT,M_strEJT);
                                    				prnHEADER(); //gets the Header of the report
                                    			}
												*/
												//System.out.println("019");
												L_strISSDT2 = nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IST_AUTDT")).toString(),"");
												L_strMNGRP2 = M_rstRSSET.getString("L_MNPRD").trim();
												L_strSBGRP2 = M_rstRSSET.getString("L_SBPRD").trim();
												L_strISSNO2 = M_rstRSSET.getString("IST_ISSNO").trim();
												L_strSALTP2 = M_rstRSSET.getString("IST_SALTP").trim();
												L_strPRDDS2 = M_rstRSSET.getString("ivt_prdds").trim();
												L_strDORNO2 = M_rstRSSET.getString("ivt_dorno").trim();
												L_strDORDT2 = nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("IVT_DORDT")).toString(),"");
												L_strTRPCD2 = M_rstRSSET.getString("ivt_trpcd").trim();
												L_strLRYNO2 = M_rstRSSET.getString("ivt_lryno").trim();
												L_strCNTDS2 = M_rstRSSET.getString("ivt_cntds").trim();
												L_strPRTCD2 = M_rstRSSET.getString("ivt_byrcd").trim();
												L_strDSTDS2 = M_rstRSSET.getString("ivt_dstds").trim();
												L_strINVNO2 = M_rstRSSET.getString("ivt_invno").trim();
												L_strLOTNO2 = M_rstRSSET.getString("IST_LOTNO").trim();
												L_strRCLNO2 = M_rstRSSET.getString("IST_RCLNO").trim();
												L_strISSQT  = M_rstRSSET.getString("IST_ISSQT").trim();
												L_dblISSQT  = M_rstRSSET.getDouble("IST_ISSQT");
												//System.out.println("020");
												L_strMNGRP = L_strMNGRP2;
												L_strSBGRP = L_strSBGRP2;
												L_strISSDT = L_strISSDT2;
												L_strSALTP = L_strSALTP2;
												L_strPRDDS = L_strPRDDS2;
												L_strISSNO = L_strISSNO2;
												L_strDORNO = L_strDORNO2;
												L_strDORDT = L_strDORDT2;
												L_strTRPCD = L_strTRPCD2;
												L_strLRYNO = L_strLRYNO2;
												L_strCNTDS = L_strCNTDS2;
												L_strPRTCD = L_strPRTCD2;
												L_strDSTDS = L_strDSTDS2;
												L_strINVNO = L_strINVNO2;
												L_strLOTNO = L_strLOTNO2;
												L_strRCLNO = L_strRCLNO2;
												intRECCT =1;
											}
											prnGRPTOT("LT",L_strTOTLTQT,1,"B");
											intGRPTOT("LT");
										}
										intGRPTOT("IV");
									}
									prnGRPTOT("PR",L_strTOTPRQT,1,"B");
									intGRPTOT("PR");
								}
								prnGRPTOT("SG",L_strTOTSGQT,1,"B");
								intGRPTOT("SG");
							}
							prnGRPTOT("ST",L_strTOTSTQT,1,"B");
							intGRPTOT("ST");
						}
						prnGRPTOT("MG",L_strTOTMGQT,1,"B");
						intGRPTOT("MG");
					}
					prnGRPTOT("DT",L_strTOTDTQT,1,"B");
					intGRPTOT("DT");
				}
				
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			prnFOOTR();
	    }
	    catch(Exception L_EX)
	    {
		    setMSG(L_EX,"getALLREC");
	    }
    }
        
    private void prnFOOTR()
    {
		try
		{
			if(cl_dat.M_intLINNO_pbst >= 61)
			prnFMTCHR(dosREPORT,M_strEJT);
			dosREPORT.writeBytes ("\n");
			prnGRPTOT("GT",L_strTOTGTQT,4,"B");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("----------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",20));
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",32));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY  ",25));
			dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)  ",35));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 9;
			prnFMTCHR(dosREPORT,M_strEJT);
			//prnFMTCHR(dosREPORT,M_strCPI17);
			dosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
    }
    
	private void prnGRPHDR(String LP_strGRPCT, int LP_intMRGVL,String LP_strBLDFL)
	{
		try
		{
			String L_strGRPDS = "";
			L_strPRNSTR = new StringBuffer("");
			L_str2ndLIN = new StringBuffer(padSTRING('R',"",LP_intMRGVL));
			if (LP_strGRPCT.equals("IV"))
			{
				L_strPRTNM = cl_cust.ocl_cust.getPRTNM(L_strPRTCD);
				L_strTRNSP = cl_cust.ocl_cust.getTRNNM(L_strPRTCD);
				L_strPRNSTR.append(padSTRING('R',"",4));
				L_strPRNSTR.append(padSTRING('R',L_strISSNO1,11));
				L_strPRNSTR.append(padSTRING('R',L_strDORNO1,13));
				L_strPRNSTR.append(padSTRING('R',getSUBSTR(L_strPRTNM,0,30),33));
				L_strPRNSTR.append(padSTRING('R',getSUBSTR(L_strDSTDS1,0,20),17));
				L_strPRNSTR.append(padSTRING('R',getSUBSTR(L_strTRNSP,0,30),28));
				L_strPRNSTR.append(padSTRING('R',"--------",10));
			}
			else if (LP_strGRPCT.equals("DT"))
			{
				L_strGRPDS = L_strISSDT1;
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',L_strGRPDS,(40-LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("MG"))
			{
				L_strGRPDS = cl_cust.ocl_cust.getMNPRD(L_strMNGRP1);
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',L_strGRPDS,(40-LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("ST"))
			{
                L_strGRPDS = cl_dat.getPRMCOD("CMT_CODDS","SYS","MR00SAL",L_strSALTP1);
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',L_strGRPDS,(40-LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("SG"))
			{
				L_strGRPDS = cl_cust.ocl_cust.getSBPRD(L_strSBGRP1);
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',L_strGRPDS,(40-LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("PR"))
			{
				L_strGRPDS = L_strPRDDS1;
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',L_strGRPDS,(40-LP_intMRGVL)));
			}
			if (LP_strBLDFL.equals("B"))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(L_strPRNSTR.toString());
			if(LP_strGRPCT.equals("IV"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_str2ndLIN.append(padSTRING('R',"",11));
				L_str2ndLIN.append(padSTRING('R',L_strDORDT1,13));
				L_str2ndLIN.append(padSTRING('R',L_strINVNO1,35));
				L_str2ndLIN.append(padSTRING('R',getSUBSTR(L_strCNTDS1,0,20),24));
				L_str2ndLIN.append(padSTRING('R',getSUBSTR(L_strLRYNO1,0,30),19));
				dosREPORT.writeBytes(L_str2ndLIN.toString());
				flgHDRFL = true;
			}
			if (LP_strBLDFL.equals("B"))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(!flgHDRFL)
			{
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			
			
		}
		catch(Exception L_EX)
		{
		setMSG(L_EX,"prnGRPHDR");
		}
	}
      
    private void prnGRPTOT(String LP_strGRPCT,String LP_TOTXXQT,int LP_intMRGVL,String LP_strBLDFL)
    {
		try
		{
			L_strPRNSTR = new StringBuffer("");
			if (LP_strGRPCT.equals("LT"))
			{
				if(flgHDRFL)
				{
					L_strPRNSTR.append(padSTRING('R',L_strLOTNO1,9));
					flgHDRFL = false;
				}
				else   
				{
				    L_strPRNSTR.append(padSTRING('R',"",106));
					L_strPRNSTR.append(padSTRING('R',L_strLOTNO1,9));
				}
			}			
			else if (LP_strGRPCT.equals("DT"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',"TOTAL " + L_strISSDT1,(40-LP_intMRGVL)));
				L_strPRNSTR.append(padSTRING('R',"",(115-(LP_intMRGVL+40)+LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("SG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_strSGRDS = cl_cust.ocl_cust.getSBPRD(L_strSBGRP1);
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',"TOTAL " + L_strSGRDS,(40-LP_intMRGVL)));
				L_strPRNSTR.append(padSTRING('R',"",(115-(LP_intMRGVL+40)+LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("MG"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_strMGRDS = cl_cust.ocl_cust.getMNPRD(L_strMNGRP1);
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',"TOTAL " + L_strMGRDS,(40-LP_intMRGVL)));
				L_strPRNSTR.append(padSTRING('R',"",(115-(LP_intMRGVL+40)+LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("PR"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',"TOTAL " + L_strPRDDS1,(40-LP_intMRGVL)));
				L_strPRNSTR.append(padSTRING('R',"",(115-(LP_intMRGVL+40)+LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("ST"))
			{
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
                L_strSALDS = cl_dat.getPRMCOD("CMT_CODDS","SYS","MR00SAL",L_strSALTP1);
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',"TOTAL " + L_strSALDS,(40-LP_intMRGVL)));
				L_strPRNSTR.append(padSTRING('R',"",(115-(LP_intMRGVL+40)+LP_intMRGVL)));
			}
			else if (LP_strGRPCT.equals("GT"))
			{
				cl_dat.M_intLINNO_pbst+=1;
				L_strPRNSTR.append(padSTRING('R',"",LP_intMRGVL));
				L_strPRNSTR.append(padSTRING('R',"GRAND TOTAL",(40-LP_intMRGVL)));
				L_strPRNSTR.append(padSTRING('R',"",(115-(LP_intMRGVL+40)+LP_intMRGVL)));
			}
			L_strPRNSTR.append(padSTRING('L',getDASH(LP_TOTXXQT),10));
			
			if (LP_strBLDFL.equals("B"))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(L_strPRNSTR.toString());
			if (LP_strBLDFL.equals("B"))
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			if(cl_dat.M_intLINNO_pbst >= 65)
			{
			    dosREPORT.writeBytes("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				cl_dat.M_intLINNO_pbst = 0;
				cl_dat.M_PAGENO += 1;
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
				prnFMTCHR(dosREPORT,M_strEJT);
				prnHEADER(); //gets the Header of the report
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPTOT");
			System.out.println("Exception: "+L_strPRNSTR.toString());
		}
	}
      
    private String getDASH(String LP_strDSHSTR)
    {
		try
		{
			if(LP_strDSHSTR == null || LP_strDSHSTR.equals("0"))
				LP_strDSHSTR = "-";
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDASH");
		}
		return LP_strDSHSTR;
	}
		
	private void intGRPTOT(String LP_strGRPCT)
	{
		try
		{
			if (LP_strGRPCT.equals("LT"))
			{
				L_strTOTLTQT = "0";
				L_strLOTNO1 = L_strLOTNO;
				L_strRCLNO1 = L_strRCLNO;
			}
			else if (LP_strGRPCT.equals("IV"))
			{
				L_strISSNO1 = L_strISSNO;
				L_strDORNO1 = L_strDORNO;
				L_strDORDT1 = L_strDORDT;
				L_strTRPCD1 = L_strTRPCD;
				L_strLRYNO1 = L_strLRYNO;
				L_strCNTDS1 = L_strCNTDS;
				L_strPRTCD1 = L_strPRTCD;
				L_strDSTDS1 = L_strDSTDS;
				L_strINVNO1 = L_strINVNO;
			}
			else if (LP_strGRPCT.equals("DT"))
			{
				L_strTOTDTQT = "0";
				L_strISSDT1 = L_strISSDT;
			}
			else if (LP_strGRPCT.equals("SG"))
			{
				L_strTOTSGQT = "0";
				L_strSBGRP1 = L_strSBGRP;
			}
			else if (LP_strGRPCT.equals("MG"))
			{
				L_strTOTMGQT = "0";
				L_strMNGRP1 = L_strMNGRP;
			}
			else if (LP_strGRPCT.equals("PR"))
			{
				L_strTOTPRQT = "0";
				L_strPRDDS1 = L_strPRDDS;
			}
			else if (LP_strGRPCT.equals("ST"))
			{
				L_strTOTSTQT = "0";
				L_strSALTP1 = L_strSALTP;
			}
			
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"intGRPTOT");
		}
	}
      
/*  Method to validate Product Type, Market Type, Sale Type  */	        
    boolean vldDATA()
	{
	    
	    if(txtMKTTP.getText().length()==0)
	    {
	        setMSG("Please enter Market Type Or Press F1 to select from list..",'N');
	        txtMKTTP.requestFocus();
	        return true;
	    }
	    if(txtPRDTP.getText().length()==0)
	    {
	        setMSG("Please enter Product Type Or Press F1 to select from list..",'N');
	        txtPRDTP.requestFocus();
	        return true;
	    }
	    if(txtSALTP.getText().length()==0)
	    {
	        setMSG("Please enter Sale Type Or Press F1 to select from list..",'N');
	        txtSALTP.requestFocus();
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
		return true;
    }
/*  Method to generate the Header Part of the Report  */ 
	private void prnHEADER()
	{
		try
		{
		    cl_dat.M_intLINNO_pbst = 0;
		    cl_dat.M_PAGENO += 1;    
		    dosREPORT.writeBytes("\n\n");
		    if(M_rdbTEXT.isSelected())
		    {
		        prnFMTCHR(dosREPORT,M_strBOLD);
                dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,55));
		        dosREPORT.writeBytes(padSTRING('L',"Report Date :" + cl_dat.M_strLOGDT_pbst,70)+"\n");
				dosREPORT.writeBytes(padSTRING('R',"Despatch Details for the Date: " + M_txtFMDAT.getText()+" to "+ M_txtTODAT.getText(),55)+ "\n");
				dosREPORT.writeBytes(padSTRING('R',(txtMKTTP.getText().length()==2 ? " Mkt.Type : "+lblMKTTP.getText() : "")+(txtSALTP.getText().length()==2 ? " Sale Type : "+lblSALTP.getText() : "")+(txtPRDTP.getText().length()==4 ? " Prod.Cat. : "+lblPRDTP.getText() : "")+(txtTRPCD.getText().length()==5 ? " Transporter : "+txtTRPCD.getText() : "")+(txtPRDCD.getText().length()==10 ? "  Grade : "+lblPRDDS.getText() : ""),100)+ "\n");
		        dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------\n");
		        dosREPORT.writeBytes(padSTRING('R'," ",7));
	            dosREPORT.writeBytes(padSTRING('R',"LA No.",10));
	            dosREPORT.writeBytes(padSTRING('R',"DO No.",14));
	            dosREPORT.writeBytes(padSTRING('R',"Customer",31));
	            dosREPORT.writeBytes(padSTRING('R',"Destination",21));
	            dosREPORT.writeBytes(padSTRING('R',"Transporter",40));
	            dosREPORT.writeBytes(padSTRING('R',"Desp",8));
	            dosREPORT.writeBytes("\n");
	            dosREPORT.writeBytes(padSTRING('R'," ",7));
	            dosREPORT.writeBytes(padSTRING('R'," ",10));		        
		        dosREPORT.writeBytes(padSTRING('R',"DO Date",14));
		        dosREPORT.writeBytes(padSTRING('R',"Invoice No.",31));
		        dosREPORT.writeBytes(padSTRING('R',"Container No.",21));
		        dosREPORT.writeBytes(padSTRING('R',"Vehicle No.",24));
		        dosREPORT.writeBytes(padSTRING('R',"Lot No.",12));
		        dosREPORT.writeBytes(padSTRING('L',"Qty(MT)",8));
		        dosREPORT.writeBytes("  ");
		        prnFMTCHR(dosREPORT,M_strNOBOLD);
	            
		    }
            else
		    {
		        prnFMTCHR(dosREPORT,M_strBOLD);
		        dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,55)+"\n\n");
				dosREPORT.writeBytes(padSTRING('R',"Despatch Details for the Date: " + M_txtFMDAT.getText()+" to "+ M_txtTODAT.getText(),70));
		        dosREPORT.writeBytes(padSTRING('L',"Report Date :" + cl_dat.M_strLOGDT_pbst,55)+"\n\n");
				dosREPORT.writeBytes(padSTRING('R',(txtMKTTP.getText().length()==2 ? " Mkt.Type : "+lblMKTTP.getText() : "")+(txtSALTP.getText().length()==2 ? " Sale Type : "+lblSALTP.getText() : "")+(txtPRDTP.getText().length()==4 ? " Prod.Cat. : "+lblPRDTP.getText() : "")+(txtTRPCD.getText().length()==5 ? " Transporter : "+txtTRPCD.getText() : "")+(txtPRDCD.getText().length()==10 ? "  Grade : "+lblPRDDS.getText() : ""),100)+ "\n");
		        dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------\n");
		        dosREPORT.writeBytes(padSTRING('R'," ",7));
		        dosREPORT.writeBytes(padSTRING('R',"LA No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"DO No.",14));
		        dosREPORT.writeBytes(padSTRING('R',"Customer",31));
		        dosREPORT.writeBytes(padSTRING('R',"Destination",21));
		        dosREPORT.writeBytes(padSTRING('R',"Transporter",40));
		        dosREPORT.writeBytes(padSTRING('R',"Desp",8));
		        dosREPORT.writeBytes("\n");
		        dosREPORT.writeBytes(padSTRING('R'," ",7));
		        dosREPORT.writeBytes(padSTRING('R'," ",7));
		        dosREPORT.writeBytes(padSTRING('R',"DO Date",14));
		        dosREPORT.writeBytes(padSTRING('R',"Invoice No.",31));
		        dosREPORT.writeBytes(padSTRING('R',"Container No.",21));
		        dosREPORT.writeBytes(padSTRING('R',"Vehicle No.",24));
		        dosREPORT.writeBytes(padSTRING('R',"Lot No.",12));
		        dosREPORT.writeBytes(padSTRING('L',"Qty(MT)",8));
		        dosREPORT.writeBytes("  ");
		    }
		     dosREPORT.writeBytes("\n-----------------------------------------------------------------------------------------------------------------------------------------------\n");
		     cl_dat.M_intLINNO_pbst+=16;
		    	    
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	
	    
}	// end of class      
	        
	        	        
	        
	        
	        
	        
	
