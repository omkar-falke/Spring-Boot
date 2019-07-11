/*
System Name   : Finished Goods Inventory Management System
Program Name  : Suitable Lots Query / Report
Program Desc. : Gives Details of the lots for a particular party as per user requirement.
Author        : Ms. Aditi M. Kulkarni
Date          : 22nd May 2001
Version       : FIMS 1.0

List of tables used :
Table Name                          Primary key                                             Operation done
                                                                                   Insert   Update   Query   Delete	
-------------------------------------------------------------------------------------------------------------------
FG_STMST       ST_LOTNO,ST_MNLCD,ST_RCLNO,ST_PKGTP,ST_PRDTP,ST_WRHTP                                   #
CO_PRMST                            PR_PRDCD                                                           #
CO_CDTRN                CMT_CGMTP,CMT_CGSTP,CMT_CODCD                                                  #
PR_LTMST                 LT_PRDTP,LT_LOTNO,LT_RCLNO                                                    #
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

class fg_qrslt extends cl_rbase
{
    JOptionPane L_OPTNPN;
    JButton btnDSPLY;                    /** JButton for DISPLAY */
	private String strPREVDT;            /** Variable for previous date */
	private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
	private String strFILNM;             /** String for generated Report File Name */
    private int intRECCT = 0;            /** Integer for counting the records fetched from DB */
    
	cl_JTable tblLOTDTL;                 /** Table for Lot Details */
	
	int TB_CHKFL = 0;
	int TB_GRADE = 1;
    int TB_LOTNO = 2;
    int TB_PKGTP = 3;
    int TB_STKQT = 4;
    int TB_REMDS = 5;
    
    JTextField txtPRTCD;                 /** JTextField to display Party Code */
    JTextField txtPRTNM;                 /** JTextField to display Party Name */
    JTextField txtGRADE;                 /** JTextField to display Grade */
    JTextField txtSTKDT;                 /** JTextField to display Stock Date */
    JTextField txtSTKTM;                 /** JTextField to display Stock Time */
    JTextField txtTOTQT;                 /** JTextField to display Total Stock Quantity */
    
    JCheckBox chkBOX;                    /** JCheckBox to display Remarks */
    
    JRadioButton rdbDYOPN;               /** JRadioButton for Day Opening Stock */
    JRadioButton rdbCRSTK;               /** JRadioButton for Current Stock */
    ButtonGroup chkGRP;
	
	String strPRTNM,strGRADE,strSTKDT,strSTKTM,strLOTNO,strPKGTP,strSTKQT,strREMDS,strREFDT,strGRADE1;
	String strTOTSTK;                    /** String variable for total stock quantity. */
	String strGRDTOT;                    /** String variable for gradewise total */
	
	int intLMRGN = 0;
	boolean flgENBL = false;
	boolean L_1STFL = true;
	
	/* Constructor */	
	fg_qrslt()
	{
	    super(2);
		try
		{
		    setMatrix(20,8);
		    L_OPTNPN = new JOptionPane();
		    chkGRP = new ButtonGroup();
		    
		    add(new JLabel("Party Code"),4,2,1,1,this,'L');
		    add(txtPRTCD = new TxtLimit(10),4,3,1,1.5,this,'L');
		    add(new JLabel("Party Name"),5,2,1,1,this,'L');
		    add(txtPRTNM = new TxtLimit(10),5,3,1,1.5,this,'L');
		    add(new JLabel("Grade"),6,2,1,1,this,'L');
		    add(txtGRADE = new TxtLimit(8),6,3,1,1,this,'L');
		    add(new JLabel("Current Stock As On"),2,5,1,1,this,'L');
		    add(txtSTKDT = new TxtLimit(10),2,6,1,0.8,this,'L');
		    add(txtSTKTM = new TxtLimit(10),2,7,1,0.8,this,'L');
		    
		    add(chkBOX = new JCheckBox("Remark Display"),3,6,1,1.5,this,'L');
		    		    
		    add(btnDSPLY = new JButton("DISPLAY"),6,7,1,1,this,'L');
		    btnDSPLY.addActionListener(this);
		    btnDSPLY.addFocusListener(this);
		    btnDSPLY.addKeyListener(this);
		    
		    String[] strSTKHD = {"Status","Grade","Lot No.","Package Type","Stock Qty.","Remarks"};
        	int [] intCOLSZ = {40,100,100,100,120,100};
        	tblLOTDTL = crtTBLPNL1(this,strSTKHD,10000,7,2,8,6,intCOLSZ,new int[] {0});
        	
        	add(new JLabel("Total Stock"),16,5,1,1,this,'L');
		    add(txtTOTQT = new TxtLimit(10),16,6,1,1,this,'L');
		    		    
		    M_pnlRPFMT.setVisible(true);
		    
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
								    
		    add(rdbDYOPN = new JRadioButton("Day Opening Stock"),4,6,1,1.5,this,'L');
			add(rdbCRSTK = new JRadioButton("Current Stock"),5,6,1,1.5,this,'L');
	        chkGRP.add(rdbCRSTK);
	        chkGRP.add(rdbDYOPN);
	                
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
		 btnDSPLY.setEnabled(true);
		 txtSTKDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
		 txtSTKTM.setText(cl_dat.M_txtCLKTM_pbst.getText());
	}
	
	/* method to be executed when event is fired on press of a key */
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
 	        setCursor(cl_dat.M_curWTSTS_pbst);
 	         	        
 	        if(M_objSOURC == txtPRTCD)
 	        {
 	           try
 	           {
 	              M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MRXXLAU'";
 	              M_strHLPFLD = "txtPRTCD";
 	              System.out.println(M_strSQLQRY);
                  cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT"); 
 	           }
 	           catch(Exception L_EX)
    		   {
    		      setMSG(L_EX ," F1 help..");    		    
    		   }  
 	        }
 	        if(M_objSOURC == txtGRADE)
 	        {
 	           try
 	           {
 	              if(rdbDYOPN.isSelected())
 	              {
     	              //M_strSQLQRY = "Select distinct ST_PRDCD,PR_PRDDS from FG_STMST,CO_PRMST where ST_PRDCD = PR_PRDCD";
     	              //M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_DOSQT > 0 and ST_CMPCD||ST_PRDTP||ST_LOTNO||ST_RCLNO in";
     	              //M_strSQLQRY += " (select LT_CMPCD||LT_PRDTP||LT_LOTNO||LT_RCLNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%')";
     	              M_strSQLQRY = "Select distinct ST_PRDCD,PR_PRDDS from FG_STMST,CO_PRMST,PR_LTMST where ST_PRDCD = PR_PRDCD";
     	              M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_DOSQT > 0 and ST_CMPCD=LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO ";
     	              M_strSQLQRY += " and  LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%'";
     	          }
 	              else
 	              {
 	                  M_strSQLQRY = "Select distinct ST_PRDCD,PR_PRDDS from FG_STMST,CO_PRMST,PR_LTMST where ST_PRDCD = PR_PRDCD";
     	              M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STKQT > 0 and ST_CMPCD=LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO ";
     	              M_strSQLQRY += " and  LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%'";
 	              }
 	              M_strHLPFLD = "txtGRADE"; 
 	              System.out.println(M_strSQLQRY);
                  cl_hlp(M_strSQLQRY,2,1,new String[]{"Product Code","Grade"},2,"CT");
 	           }
 	           catch(Exception L_EX)
    		   {
    		      setMSG(L_EX ," F1 help..");    		    
    		   }
    	    }
 	    }
 	    if(L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		    {
		       txtSTKDT.requestFocus();
		    }
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
		    {
		       txtSTKDT.requestFocus();
		    }
			if(M_objSOURC == txtSTKDT)
			{   
			   vldTRFDT();
			   txtSTKTM.requestFocus();
			}
			if(M_objSOURC == txtSTKTM)
			{
			   txtPRTCD.requestFocus();
			}
			if(M_objSOURC == txtPRTCD)
			{
			   rdbDYOPN.requestFocus();
			}
			if(M_objSOURC == rdbDYOPN || M_objSOURC == rdbCRSTK)
			{
			   txtGRADE.requestFocus();
			} 
			if(M_objSOURC == txtGRADE)
			{
			   btnDSPLY.requestFocus();
			}
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	/**	Method for execution of help for Memo Number Field 	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtPRTCD")
		{			
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);				
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
			if((txtPRTNM.getText().trim()).length()>0)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}	
		if(M_strHLPFLD == "txtGRADE")
		{			
			txtGRADE.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
	    }
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
	    try
	    {
	        super.actionPerformed(L_AE);		
		    strSTKDT = txtSTKDT.getText().toString().trim();
		    strSTKTM = txtSTKTM.getText().toString().trim();
		    strGRADE = txtGRADE.getText().toString().trim();
		    		    
		    if(M_objSOURC == btnDSPLY)
		    {
		        if(vldDATA())
		        {
		            setCursor(cl_dat.M_curWTSTS_pbst);
        			setMSG("Data Fetching in Progress... ",'N');
        			int i = 0;
        			
        			String strADDSTR = "";
			        if(txtGRADE.getText().length() > 0)
			            strADDSTR += " and PR_PRDDS = '"+txtGRADE.getText()+"' ";
		            else
		                strADDSTR += "";
                    String L_strSQLQRY;
                    if(rdbCRSTK.isSelected())
                    {	
                        L_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS,sum(ST_STKQT) L_QTY from FG_STMST,CO_PRMST,CO_CDTRN";
            			L_strSQLQRY += " where ST_PRDCD=PR_PRDCD ";
            			L_strSQLQRY += strADDSTR;
            			L_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STKQT > 0 and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and CMT_CODCD=ST_PKGTP and ST_CMPCD + ST_PRDTP + ST_LOTNO + ST_RCLNO in ";
            			L_strSQLQRY += "(select LT_CMPCD + LT_PRDTP + LT_LOTNO + LT_RCLNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%')";
            			L_strSQLQRY += " group by PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS order by PR_PRDDS,ST_LOTNO";
                    }
                    else
                    {
        			    L_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS,sum(ST_DOSQT) L_QTY from FG_STMST,CO_PRMST,CO_CDTRN";
        			    L_strSQLQRY += " where ST_PRDCD=PR_PRDCD ";
        			    L_strSQLQRY += strADDSTR;
        			    L_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_DOSQT > 0 and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and CMT_CODCD=ST_PKGTP and ST_CMPCD + ST_PRDTP + ST_LOTNO + ST_RCLNO in ";
        			    L_strSQLQRY += "(select LT_CMPCD + LT_PRDTP + LT_LOTNO + LT_RCLNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%')";
        			    L_strSQLQRY += " group by PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS order by PR_PRDDS,ST_LOTNO";
                    }
                    
        		    System.out.println(" btnDSPLY Query = " + L_strSQLQRY);	
    			    ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
    			    
    			    if(L_rstRSSET !=null)
			        {
			            double dblTOTAL = 0.00;
            			while(L_rstRSSET.next())
            			{
            			    tblLOTDTL.setValueAt(new Boolean(false),i,0);
            			    tblLOTDTL.setValueAt(L_rstRSSET.getString("PR_PRDDS"),i,TB_GRADE);
            				tblLOTDTL.setValueAt(L_rstRSSET.getString("ST_LOTNO"),i,TB_LOTNO);
            				tblLOTDTL.setValueAt(L_rstRSSET.getString("CMT_SHRDS"),i,TB_PKGTP);
            				tblLOTDTL.setValueAt(L_rstRSSET.getString("L_QTY"),i,TB_STKQT);
            				if(chkBOX.isSelected())
                            {
            				    tblLOTDTL.setValueAt(L_rstRSSET.getString("ST_REMDS"),i,TB_REMDS);
                            }
                            else
                            {}
                            String strSTKQTY = nvlSTRVL(L_rstRSSET.getString("L_QTY"),"");
                            dblTOTAL += Double.parseDouble(strSTKQTY); 
            				i++;
            			}
            			txtTOTQT.setText(setNumberFormat(dblTOTAL,3));
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
	
	/** Function for getting Reference Date */
	public void getREFDT(String P_strLOGDT)            //get reference date
	{
		try
		{
			Date L_strTEMP=null;
			String L_strREFDT="";
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				L_rstRSSET.close();
				L_strREFDT=strREFDT;			
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
				strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				if(!P_strLOGDT.equals(strREFDT))
				{
					JOptionPane.showMessageDialog(this,"Transactions upto "+L_strREFDT+" have already been locked.");					
					flgENBL = false;
				}
				txtSTKDT.setText(strREFDT);
				txtSTKDT.setEnabled(false);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
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
    		if(txtSTKDT.getText().trim().length()==0)
        	{
            	setMSG("Please Enter Date ..",'E');
            	txtSTKDT.requestFocus();
            	return false;
        	}
            if(M_fmtLCDAT.parse(txtSTKDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
        	{
            	setMSG(" Date cannot be greater than today's date..",'E');
            	txtSTKDT.requestFocus();
            	return false;
        	}
        	if(txtPRTCD.getText().trim().length()==0)
        	{
            	setMSG("Party Code cannot be left blank ...",'E');
            	txtPRTCD.requestFocus();
            	return false;
        	}
        }
	    catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}	
		return true;
	}
	
	/** method to validate transfer date */
	private void vldTRFDT()
	{ 
	    getREFDT(cl_dat.M_strLOGDT_pbst);
	}
	
	/*	Method to print, display report as per selection   */
	void exePRINT()
	{
	    if (vldDATA())
		{
		    try
		    {
    		    if(M_rdbHTML.isSelected())
    		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_qrslt.html";
    		    else if(M_rdbTEXT.isSelected())
    				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_qrslt.doc";
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
    					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Suitable Lots Query/Report"," ");
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
	
	private void getSTKREC()                //gets the records from stock
	{
	    try
		{
		    fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT  = 0 ;
	        cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
			setCursor(cl_dat.M_curWTSTS_pbst);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI12);
			cl_dat.M_PAGENO = 0;
							    
		    if(M_rdbHTML.isSelected())
		    {
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Suitable Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
		    }
		    
			prnHEADER();      //gets the header of the report
			
			String strADDSTR = "";
			if(txtGRADE.getText().length() > 0)
			    strADDSTR += " and PR_PRDDS = '"+txtGRADE.getText()+"' ";
		    else
		        strADDSTR += "";
								
			if(rdbCRSTK.isSelected())
            {	
                //M_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS,sum(ST_STKQT) L_QTY from FG_STMST,CO_PRMST,CO_CDTRN";
        		//M_strSQLQRY += " where ST_PRDCD=PR_PRDCD";
        		//M_strSQLQRY += strADDSTR;
        		//M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STKQT > 0 and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and CMT_CODCD=ST_PKGTP and ST_CMPCD||ST_PRDTP||ST_LOTNO||ST_RCLNO in ";
        		//M_strSQLQRY += "(select LT_CMPCD||LT_PRDTP||LT_LOTNO||LT_RCLNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%')";
        		//M_strSQLQRY += " group by PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS order by PR_PRDDS,ST_LOTNO";

				M_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS,sum(ST_STKQT) L_QTY from FG_STMST,CO_PRMST,CO_CDTRN,PR_LTMST";
        		M_strSQLQRY += " where ST_PRDCD=PR_PRDCD";
        		M_strSQLQRY += strADDSTR;
        		M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STKQT > 0 and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and CMT_CODCD=ST_PKGTP and ST_CMPCD = LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO  ";
        		M_strSQLQRY += " and  LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%'";
        		M_strSQLQRY += " group by PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS order by PR_PRDDS,ST_LOTNO";
            }
            else
            {
        	    //M_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS,sum(ST_DOSQT) L_QTY from FG_STMST,CO_PRMST,CO_CDTRN";
        	    //M_strSQLQRY += " where ST_PRDCD=PR_PRDCD";
        	    //M_strSQLQRY += strADDSTR;
        	    //M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_DOSQT > 0 and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and CMT_CODCD=ST_PKGTP and ST_CMPCD||ST_PRDTP||ST_LOTNO||ST_RCLNO in ";
        	    //M_strSQLQRY += "(select LT_CMPCD||LT_PRDTP||LT_LOTNO||LT_RCLNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%')";
        	    //M_strSQLQRY += " group by PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS order by PR_PRDDS,ST_LOTNO";
				
        	    M_strSQLQRY =  "Select PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS,sum(ST_DOSQT) L_QTY from FG_STMST,CO_PRMST,CO_CDTRN,PR_LTMST";
        	    M_strSQLQRY += " where ST_PRDCD=PR_PRDCD";
        	    M_strSQLQRY += strADDSTR;
        	    M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_DOSQT > 0 and cmt_cgmtp='SYS' and cmt_cgstp='FGXXPKG' and CMT_CODCD=ST_PKGTP and ST_CMPCD = LT_CMPCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO ";
        	    M_strSQLQRY += " and LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_AUTLS like '%"+txtPRTCD.getText()+"%'";
        	    M_strSQLQRY += " group by PR_PRDDS,ST_LOTNO,CMT_SHRDS,ST_REMDS order by PR_PRDDS,ST_LOTNO";
            }
            System.out.println("getSTKREC Query = " + M_strSQLQRY);	
    	    ResultSet M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
    	    
    	    int intGRDCTR = 0;              
		    double dblTOTQTY = 0.000;      
		    double L_ISSQT = 0;
		    String strGRADE1 = "xxx";      
		    boolean flgEOF = false;
    	    if(M_rstRSSET != null && M_rstRSSET.next())
		    {
    			while(true)
    			{
            		strGRADE1 = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
    			    while(!flgEOF)
    			    {
        			    strGRADE = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
        			    if(!strGRADE.equals(strGRADE1))
        			       break;
        				strLOTNO = nvlSTRVL(M_rstRSSET.getString("ST_LOTNO"),"");
        				strPKGTP = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"");
        				strSTKQT = nvlSTRVL(M_rstRSSET.getString("L_QTY"),"");
        				strREMDS = nvlSTRVL(M_rstRSSET.getString("ST_REMDS"),"");
        				if(chkBOX.isSelected())
                        {
        				    dosREPORT.writeBytes(padSTRING('R',strGRADE,14)+padSTRING('R',strLOTNO,16)+padSTRING('R',strPKGTP,11)+padSTRING('L',strSTKQT,10)+padSTRING('R',"",5)+padSTRING('R',strREMDS,35)+"\n");
                        }
                        else
                        {
                            dosREPORT.writeBytes(padSTRING('R',strGRADE,14)+padSTRING('R',strLOTNO,16)+padSTRING('R',strPKGTP,11)+padSTRING('L',strSTKQT,10)+padSTRING('R',"",5)+"\n");
                        }
    		            dblTOTQTY += Double.parseDouble(strSTKQT);
    		            L_ISSQT += Double.parseDouble(strSTKQT);
    		            intGRDCTR += 1;
        				if(!M_rstRSSET.next())
        				{
        				   flgEOF = true;
        				   break;
        				}
    				}
    				dosREPORT.writeBytes("\n");
	                if(dblTOTQTY > 0)
	                {
	                    if(intGRDCTR > 1)
	                    {
	                       prnFMTCHR(dosREPORT,M_strBOLD);
	                       dosREPORT.writeBytes(padSTRING('L',"Total",41)+padSTRING('L',setNumberFormat(dblTOTQTY,3),10)+"\n");
	                       dosREPORT.writeBytes("\n");
	                       prnFMTCHR(dosREPORT,M_strNOBOLD);
	                       cl_dat.M_intLINNO_pbst +=1;
	                    }
	                }
	                dblTOTQTY = 0.000;
    	            intGRDCTR = 0;
		            intRECCT = 1;
    				if(flgEOF)
    				   break;
    			}
    			if(cl_dat.M_intLINNO_pbst >= 60)
				{
					dosREPORT.writeBytes ("\n\n");
					prnFMTCHR(dosREPORT,M_strCPI10);
					prnFMTCHR(dosREPORT,M_strEJT);				
					prnHEADER();
				}
    			M_rstRSSET.close();
    		}
			cl_dat.M_intLINNO_pbst+=1;
			crtLINE(91,"-");
			dosREPORT.writeBytes("\n");
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('L',"Total Qty.",41)+padSTRING('L',setNumberFormat(L_ISSQT,3),10)+"\n");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			crtLINE(91,"-");
			setMSG("Report completed.. ",'N');
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTKREC");
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
    			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,28)+padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,61)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes(padSTRING('R',"Stock of suitable lots for "+txtPRTNM.getText()+" as on "+strSTKDT+","+strSTKTM+"",65)+padSTRING('L',"Page No:" + cl_dat.M_PAGENO,10));
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(91,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"Grade",15)+padSTRING('R',"Lot No.",15)+padSTRING('R',"Pkg Type",13)+padSTRING('R',"Quantity",10)+padSTRING('L',"Remarks",15)+"\n");
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(91,"-");
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
    			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",28)+padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,61)+"\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			dosREPORT.writeBytes(padSTRING('R',"Stock of suitable lots for "+txtPRTNM.getText()+" as on "+strSTKDT+","+strSTKTM+"",65)+padSTRING('L',"Page No:" + cl_dat.M_PAGENO,10));
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(91,"-");
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst += 1;
    			prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"Grade",15)+padSTRING('R',"Lot No.",15)+padSTRING('R',"Pkg Type",13)+padSTRING('R',"Quantity",10)+padSTRING('L',"Remarks",15)+"\n");
    			prnFMTCHR(dosREPORT,M_strNOBOLD);
    			cl_dat.M_intLINNO_pbst += 1;
    			crtLINE(91,"-");
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
			dosREPORT.writeBytes(padSTRING('L'," ",intLMRGN));
			dosREPORT.writeBytes(strln);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"crtLINE");	
		}
	}
	
} // end of class


