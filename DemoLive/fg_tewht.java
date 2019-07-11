/**isnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnullisnull
xSystem Name   : Finished Goods Inventory Management System
Program Name  : WareHouse Transfer Entry Form
Program Desc. : 
Author        : Mr. Deepal N. Mehta
Date          : 24th October 2001
Version       : FIMS 1.0
Modifications 
Modified By    : Mr. SRD
Modified Date  : 18th April 2002
Version        : FIMS 1.02 
*/

import java.awt.event.*;
import java.awt.Component;

import java.awt.Color;

import java.awt.Dimension;

import java.awt.Cursor;

import java.sql.*;
import java.util.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;
import java.util.Vector;

import java.util.Hashtable;

import java.util.StringTokenizer;

import java.util.Calendar;

import java.sql.ResultSet;

import java.util.Enumeration;

import java.util.Arrays;

import java.text.NumberFormat;
import java.math.*;



class fg_tewht extends cl_pbase implements MouseListener
{
	JLabel lblISSHDR,lblRCTHDR;            
	JRadioButton rdbSLRSTK,rdbFRSSTK;
	JComboBox cmbWRHTP,cmbTRFTP,cmbCLSTG; // ComboBox for warehouse type,transfer type, classification tag
	ButtonGroup chkGRP;
	JTextField txtTRFNO,txtTRFDT,txtCLSTG,txtPRDTP,txtLOTNO,txtRCLNO,txtPKGTP,txtEDITR;
	JTextField txtREMDS,txtTOISS,txtTORCT,txtRCTMNL,txtRCTPKT,txtRCTPKG,txtISTQTY;
	cl_JTable tblRCTDTL,tblISSDTL;        // Tables for receipt and issue details
	
	int intIST_CHKFL = 0;      // status flag in tblISSDTL
    int intIST_MNLCD = 1;      // main location in tblISSDTL
	int intIST_PKGTP = 2;      // package category in tblISSDTL
	int intIST_ISSQT = 3;      // quantity in tblISSDTL
	int intIST_ISSPK = 4;      // no. of packages in tblISSDTL
	int intIST_REMDS = 5;      // no. of packages in tblISSDTL
	        
    int intRCT_CHKFL = 0;      // status flag in tblRCTDTL
	int intRCT_MNLCD = 1;      // main location in tblRCTDTL
	int intRCT_PKGTP = 2;      // package category in tblRCTDTL
	int intRCT_RCTQT = 3;      // quantity in tblRCTDTL
    int intRCT_RCTPK = 4;      // no. of packages in tblRCTDTL      
	int intRCT_SHFCD = 5;      // shift code in tblRCTDTL
	int intRCT_REMDS = 6;      // shift code in tblRCTDTL
	String strSELECTED="";	   // gives tblIssue's Remark 
		
	String strTRFDT,strTRFNO,strWRHTP,strPRDTP,strRCLNO,strISPKG;
	String strMNLCD,strSTKTP,strTRFQT_ISS,strCODCD,strTPRCD,strISSPK,strNCSVL,strUCLQT;
	String strUPDNO,strCHP02,strPKGWT,strMKTTP,strSHFCD,strREMDS,strREMDS_STK,strTRFQT_RCT,strFILNM,strRCTPK;
	String strCHP01,strREFDT,strSALTP,strRCTDT,strISSRF,strPKGCT,strRCPKG,strTRFQT_ISS1;
    String strGNTFNO,strPRDCD,strCPRCD,strPKGTP_RCT, strPKGTP_ISS;
	//String strCLSTG="";
	//String strTRFTP="";
	String strSTKSTR;
	String strUSRCD="";
	String L_strSTRSQL="";
	String L_strSTLSQL="";
	String L_strSTRQRY="";
	String L_strSTTQRY="";
	
	int LM_CNT = 0;
		
	String strTRFTP_WHT = "40";   //variable for warehouse transfer
	String strTRFTP_RBG = "16";   //variable for rebagging
	
	String strGENNO;                //Generated Issue Number without Code added to it.
	String strTRNTP = "";
	String strRESSTR = "";
	String strPRVNO = "";
    private String strWHRSTR;

	
	double dblTORCQT;               //Current Qty. total for the same lot from Receipt entry table
	double dblTOISQT;               //Current Qty. total for the same lot from Issue entry table
	
	JOptionPane LM_OPTNPN;
	boolean flgENBL = false;
	boolean flgHLPFLG = false;
	boolean flgPKGTP_VLD = true;
	private boolean flgCHK_EXIST = false;

	private String strPREVDT;       /** Variable for previous date*/
	double dblSTQTY = 0;
	String strCURTM = "";
	
	String strYRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4);
	ResultSet L_rstCTRLST,L_rstRSLSET,L_rstRSSET,M_rstRSLSET,M_rstRSTSET;   //local variables for resultset 
	Hashtable<String,String> hstSTKQT;         
	
	/* Constructor */
	fg_tewht()
	{
	    super(2);
	    try
	    {
	        setMatrix(20,8);
	        
		    chkGRP = new ButtonGroup();
   		    
		    add(new JLabel("WareHouse"),1,1,1,1.5,this,'L');
		    add(cmbWRHTP = new JComboBox(),2,1,1,1,this,'L');
		    cmbWRHTP.addItem("01");
		    		    		    		    
		    add(new JLabel("Transfer Type"),1,2,1,1,this,'L');
		    add(cmbTRFTP = new JComboBox(),2,2,1,1.5,this,'L');
		    cmbTRFTP.addItem("40  W/H Transfer ");
		    cmbTRFTP.addItem("16  Rebagging ");
		    		    		            
	        add(new JLabel("Transfer No."),1,4,1,1,this,'L');
	        add(txtTRFNO = new TxtLimit(8),2,4,1,1,this,'L');
	        	        
	        add(new JLabel("Transfer Date"),1,5,1,1,this,'L');
	        add(txtTRFDT = new TxtLimit(10),2,5,1,1,this,'L');
	        		    	
	        add(new JLabel("Stock Type"),1,6,1,1,this,'L');
	        add(rdbFRSSTK = new JRadioButton("Fresh Stock",true),2,6,1,1.1,this,'L');
	        add(rdbSLRSTK = new JRadioButton("Sales Return Stock"),2,7,1,1.5,this,'L');
	        chkGRP.add(rdbFRSSTK);
	        chkGRP.add(rdbSLRSTK);
	        	        
	        add(new JLabel("Clsfn Tag"),3,1,1,1,this,'L');
		    add(cmbCLSTG = new JComboBox(),4,1,1,1,this,'L');
		    cmbCLSTG.addItem("C  Classified ");
		    cmbCLSTG.addItem("U  Unclassified");

			

	        add(new JLabel("Prd.Tp"),3,2,1,1,this,'L');
	        add(txtPRDTP = new TxtLimit(2),4,2,1,0.5,this,'L');
			
	        add(new JLabel("Lot No."),3,3,1,1,this,'L');
	        add(txtLOTNO = new TxtLimit(8),4,3,1,1,this,'L');
			
	        	        
	        add(new JLabel("RCL No."),3,4,1,1,this,'L');
	        add(txtRCLNO = new TxtLimit(6),4,4,1,1,this,'L');
	        	        
		    add(new JLabel("Package Type"),3,5,1,1,this,'L');
	        add(txtPKGTP = new TxtLimit(6),4,5,1,1,this,'L');
	        
	        add(new JLabel("Remarks"),3,6,1,1,this,'L');
	        add(txtREMDS = new TxtLimit(20),4,6,1,2,this,'L');
	        	        
	        add(new JLabel("Issue Entry Details"),5,1,1,1.5,this,'L');
	        add(new JLabel("Total Issue Quantity"),5,6,1,2,this,'L');
	        add(txtTOISS = new TxtLimit(6),5,8,1,1,this,'L');
	        	        
			String[] L_strTBLHD1 = {"Status","Main Location","Package Type","Quantity","Packages","Remark"};
			int[] L_intCOLSZ1 = {20,100,100,100,100,170};
			tblISSDTL = crtTBLPNL1(this,L_strTBLHD1,50,6,1,5,8,L_intCOLSZ1,new int[]{0});
						
	        add(new JLabel("Receipt Entry Details"),12,1,1,1.5,this,'L');
	        add(new JLabel("Total Receipt Quantity"),12,6,1,2,this,'L');
	        add(txtTORCT = new TxtLimit(6),12,8,1,1,this,'L');
	        	        			
			String[] L_strTBLHD2 = {"Status","Main Location","Package Type","Quantity","Packages","Shift","Remark"};
			int[] L_intCOLSZ2 = {20,100,100,100,100,100,170};
			tblRCTDTL = crtTBLPNL1(this,L_strTBLHD2,50,13,1,5,8,L_intCOLSZ2,new int[]{0});
			txtRCTMNL = new TxtLimit(6);                // textfield for main location in receipt table
			txtRCTPKG = new TxtLimit(6);                // textfield for no. of packages in receipt table
			txtRCTPKT = new TxtLimit(6);                // textfield for package type in receipt table
									
			tblISSDTL.setInputVerifier(new TBLINPVF());
			tblISSDTL.addFocusListener(this);
            tblISSDTL.addKeyListener(this);
            tblRCTDTL.setInputVerifier(new TBLINPVF());
			tblRCTDTL.addFocusListener(this);
            tblRCTDTL.addKeyListener(this);	
            			
			tblRCTDTL.setCellEditor(intRCT_MNLCD,txtRCTMNL);
	        txtRCTMNL.addFocusListener(this);
            txtRCTMNL.addKeyListener(this);
            
            tblRCTDTL.setCellEditor(intRCT_PKGTP,txtRCTPKT);
            txtRCTPKT.addFocusListener(this);
            txtRCTPKT.addKeyListener(this);
            
            tblRCTDTL.setCellEditor(intRCT_RCTPK,txtRCTPKG);
            txtRCTPKG.addFocusListener(this);
            txtRCTPKG.addKeyListener(this);
                                                
            hstSTKQT = new  Hashtable<String,String>();
            
        }    
	    catch(Exception L_EX)
	    {
	        setMSG(L_EX,"Constructor");
	    }
	}  // end of constructor
	
	/* super class Method overrided to enhance its functionality, to enable & disable components 
       according to requirement. */
	void setENBL(boolean L_flgSTAT)
	{   
         super.setENBL(L_flgSTAT);
         cmbWRHTP.setEnabled(false);
         cmbTRFTP.setEnabled(false);
         txtTRFNO.setEnabled(false);
         txtTRFDT.setEnabled(false);
         txtPRDTP.setEnabled(false);
         txtLOTNO.setEnabled(false);
         txtRCLNO.setEnabled(false);
         txtPKGTP.setEnabled(false);
         tblRCTDTL.setEnabled(false);
		 tblISSDTL.setEnabled(false);
		 
		 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
         {
             txtTRFDT.setText(cl_dat.M_strLOGDT_pbst);
		     tblISSDTL.setEnabled(true);
		     tblRCTDTL.setEnabled(true);
         }
         else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
         {
            txtTRFDT.setText("");
         }
	}
	

	/**

	 */
	public void actionPerformed(ActionEvent L_AE)
	{
	    super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
				    cmbWRHTP.setEnabled(true);
				    cmbWRHTP.requestFocus();
				    cmbTRFTP.setEnabled(true);
				    txtTRFDT.setEnabled(true);
				    txtTRFNO.setEnabled(false);	
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
				    cmbWRHTP.requestFocus();
				    cmbWRHTP.setEnabled(true);
				    cmbTRFTP.setEnabled(true);	
				    cmbTRFTP.requestFocus();
				    txtTRFNO.requestFocus();
				    txtTRFNO.setEnabled(true);
				    rdbFRSSTK.setEnabled(true);
				    rdbSLRSTK.setEnabled(true);
				    cmbCLSTG.setEnabled(true);
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
				    cmbWRHTP.requestFocus();
				    cmbWRHTP.setEnabled(true);
				    cmbTRFTP.setEnabled(true);	
				    cmbTRFTP.requestFocus();
				    txtTRFNO.setEnabled(true);
                    txtTRFDT.setText("");
				    rdbFRSSTK.setEnabled(true);
				    rdbSLRSTK.setEnabled(true);
				    cmbCLSTG.setEnabled(true);
				}
				
				if(M_objSOURC == cmbCLSTG)
				{
				    txtPRDTP.requestFocus();
				}
				
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Action Performed");
		}
	}
	
	/* method to uncheck both the tables */
	private void exeCLRTBL()    
	{ 
	    for(int i = 0;i < tblRCTDTL.getRowCount();i++)
		{
			for(int j = 0;j<(tblRCTDTL.getColumnCount()-1);j++)
			{
				if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).equals(new Boolean(true)))
				{
					tblRCTDTL.setValueAt(new Boolean(false),i,intRCT_CHKFL);
				}
					tblRCTDTL.setValueAt("",i,j+1);
			}
		}
		for(int i = 0;i < tblISSDTL.getRowCount();i++)
		{
			for(int j = 0;j<(tblISSDTL.getColumnCount()-1);j++)
			{
				if(tblISSDTL.getValueAt(i,intIST_CHKFL).equals(new Boolean(true)))
				{
					tblISSDTL.setValueAt(new Boolean(false),i,intIST_CHKFL);
				}
					tblISSDTL.setValueAt("",i,j+1);
			}
		}
	}
	
	/**

	 */
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			if(M_objSOURC == tblISSDTL)
			{
				if(tblISSDTL.getSelectedColumn() != intIST_ISSQT)
					txtEDITR.setEnabled(false);
			    else
			    {
				    txtEDITR.setEnabled(true);
			    }
			    if(tblISSDTL.getSelectedColumn() == intIST_MNLCD)
			    {
				    if(tblISSDTL.getValueAt(tblISSDTL.getSelectedRow(),intIST_CHKFL).toString().trim().equals("false"))
						tblISSDTL.setValueAt(new Boolean(true),tblISSDTL.getSelectedRow(),intIST_CHKFL);
				    else if(tblISSDTL.getValueAt(tblISSDTL.getSelectedRow(),intIST_CHKFL).toString().trim().equals("true"))
						tblISSDTL.setValueAt(new Boolean(false),tblISSDTL.getSelectedRow(),intIST_CHKFL);
				    dblTOISQT = calISSQT();
				    txtTOISS.setText(setNumberFormat(dblTOISQT,3));
				}
		    }
			else if((M_objSOURC == rdbFRSSTK) || (M_objSOURC == rdbSLRSTK))
			{
				if(rdbFRSSTK.isSelected() || rdbSLRSTK.isSelected())
				{
					exeCLRTBL();
				}
			}
			else if(M_objSOURC == cmbCLSTG)
			{
			    txtPRDTP.requestFocus();
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"MouseClicked");
		}
	}
	
	/* method to handle events fired on press of a key */	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
		    strWRHTP = getSUBSTR(cmbWRHTP.getSelectedItem().toString().trim(),0,2);
    		//strTRFTP = getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2);
    		strTRFNO = getSUBSTR(txtTRFNO.getText().toString().trim(),0,8);
    		strTRFDT = getSUBSTR(txtTRFDT.getText().toString().trim(),0,10);
    		//strCLSTG = getSUBSTR(cmbCLSTG.getSelectedItem().toString().trim(),0,1);
    		//strRCLNO = getSUBSTR(txtRCLNO.getText().toString().trim(),0,2);
    		strPKGTP_ISS = getSUBSTR(txtPKGTP.getText().toString().trim(),0,2);
    		strREMDS = getSUBSTR(txtREMDS.getText().toString().trim(),0,50);
			if(L_KE.getKeyCode()== L_KE.VK_ENTER)
			{
			    if(M_objSOURC == cmbWRHTP)
			    {   
			        cmbWRHTP.setEnabled(false);
					cmbTRFTP.requestFocus();
				}
				else if(M_objSOURC == cmbTRFTP)
				{
					if(vldTRFTP())
					{
					    //setMSG("Valid Issue Type",'N');
						cmbTRFTP.setEnabled(false);
						cmbCLSTG.setEnabled(true);
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) 
						    txtTRFNO.setEnabled(true);
						else	    
						    txtTRFNO.setEnabled(false);	
						txtTRFDT.setEnabled(true);
						txtTRFDT.requestFocus();
					}
					else
					{
					setMSG("InValid Issue Type",'E');
					cmbTRFTP.requestFocus();
					}
				}
				else if(M_objSOURC == txtTRFNO)
				{
				    exeENQRY();
				}
				else if(M_objSOURC == txtTRFDT)
				{
				    vldTRFDT();
				}
				else if(M_objSOURC == rdbFRSSTK || M_objSOURC == rdbSLRSTK)
				{      
				        cmbCLSTG.requestFocus();
				}   
				else if(M_objSOURC == cmbCLSTG)
				{      
					    cmbCLSTG.setEnabled(false);
						txtPRDTP.setEnabled(true);
						txtPRDTP.requestFocus();
						setMSG("Press F1 to select the Prod.Type",'N');
				}
				else if(M_objSOURC == txtPRDTP)
				{      
					    txtPRDTP.setEnabled(false);
						txtLOTNO.setEnabled(true);
						txtLOTNO.requestFocus();
						setMSG("Press F1 to select the Lot No.",'N');
				}
				else if(M_objSOURC == txtLOTNO)
				{
					if(vldLOTNO())
					{
						if(flgHLPFLG)
						{
						    //System.out.println("jjjj");
							setCursor(cl_dat.M_curWTSTS_pbst);
							getALLREC();
							txtLOTNO.setEnabled(false);
							txtRCLNO.setEnabled(false);	
							txtPKGTP.setEnabled(false);	
							
							setCursor(cl_dat.M_curDFSTS_pbst);
							setMSG("Click records for Transfer or ReBagging entry.",'N');
							flgHLPFLG = false; 
						}
						else
						{
							txtRCLNO.setText("");
							txtPKGTP.setText("");
							txtREMDS.setText("");
							exeCLRTBL();
							setMSG("Press F1 key for selecting Lot No.",'E');
						}
				    }
				    else
				    {
					    setMSG("InValid Lot No.",'E');									
					    txtLOTNO.requestFocus();
				    }
				}
			}
			if(L_KE.getKeyCode()==L_KE.VK_F1)
			{
			   	if(M_objSOURC==txtPRDTP)
				{
					M_strHLPFLD = "txtPRDTP";
					String L_ARRHDR[] ={ "Prd.Type","Description"};
					M_strSQLQRY = "Select CMT_CODCD, CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'MST' and CMT_CGSTP = 'COXXPRD' order by CMT_CODCD";
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
				}
			   	if(M_objSOURC==txtLOTNO)
				{
					M_strHLPFLD = "txtLOTNO";
					//strCLSTG=(cmbCLSTG.getSelectedItem().toString()).substring(0,1).trim();
					//strTRFTP=(cmbTRFTP.getSelectedItem().toString()).substring(0,2).trim();
					String L_ARRHDR[] ={ "Lot No.","Rcl. No.","Package Type","Remarks"};
					if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("C"))
					{
						M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_REMDS from FG_STMST";
					    M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) > 0 and ST_PRDTP = '"+txtPRDTP.getText()+"' ";
						if(getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2).equals(strTRFTP_RBG))
						{
							M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_REMDS from FG_STMST";
							M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) > 0  and ST_PRDTP = '"+txtPRDTP.getText()+"' ";
						}
					}
					else if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U"))
					{
						M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_REMDS from FG_STMST";
					    M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(ST_UCLQT,0) > 0   and ST_PRDTP = '"+txtPRDTP.getText()+"' ";
					}
					M_strSQLQRY += " and ST_STSFL = '"+(rdbFRSSTK.isSelected() ? "1" : "2")+"'";
					M_strSQLQRY += " group by ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_REMDS";
					M_strSQLQRY += " order by ST_LOTNO,ST_RCLNO,ST_PKGTP,ST_REMDS";
					//System.out.println("hlpLOTNO : "+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				}
				if(M_objSOURC==txtRCTMNL)
                {
                    M_strHLPFLD="txtRCTMNL";
                                
                    	String L_ARRHDR[] ={"Location"};
            			M_strSQLQRY = "Select st_mnlcd l_mnlcd from fg_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP = '"+txtPRDTP.getText()+"' and st_lotno='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
            			M_strSQLQRY += " and st_rclno='"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
            			M_strSQLQRY += " Union";
            			M_strSQLQRY += " Select st_mnlcd l_mnlcd from fg_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP <> '"+txtPRDTP.getText()+"' and st_lotno <> '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
            			M_strSQLQRY += " and st_rclno <> '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
            			M_strSQLQRY += " Union";
            			M_strSQLQRY += " Select lc_mnlcd l_mnlcd from fg_lcmst where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lc_mnlcd not in";
            			M_strSQLQRY += " (Select st_mnlcd from fg_stmst) order by l_mnlcd";
            			//System.out.println("txtRCTMNL:"+M_strSQLQRY);
            			if(M_strSQLQRY != null)
            			{
            			cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,1,"CT");
            			}
            	}
                if(M_objSOURC==txtRCTPKT)
                {
                    M_strHLPFLD="txtRCTPKT";
                    String L_ARRHDR[] ={ "Package Type","Package Weight"};
            	    M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'FGXXPKG'";
            		if(M_strSQLQRY != null)
            		{
            		cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
            		}
            	}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
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
				//System.out.println("REFDT = "+strREFDT);
				if(!P_strLOGDT.equals(strREFDT))
				{
					JOptionPane.showMessageDialog(this,"Transactions upto "+L_strREFDT+" have already been locked.");					
					flgENBL = false;
				}
				txtTRFDT.setText(strREFDT);
				txtTRFDT.setEnabled(false);
				rdbFRSSTK.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
		}
	}
	
	/* method for Help*/	
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD == "txtPRDTP")
			{
				flgHLPFLG = true;
				StringTokenizer L_STRTKN1 = new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtPRDTP.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD == "txtLOTNO")
			{
				flgHLPFLG = true;
				StringTokenizer L_STRTKN1 = new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtLOTNO.setText(L_STRTKN1.nextToken());
				txtRCLNO.setText(L_STRTKN1.nextToken());
				txtPKGTP.setText(L_STRTKN1.nextToken());
			}
			if(M_strHLPFLD == "txtRCTMNL")
			{
			    txtRCTMNL.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD == "txtRCTPKT")
			{
			    txtRCTPKT.setText(cl_dat.M_strHLPSTR_pbst);
				txtRCTPKT.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK"); 
		}
	}
	
	/* method to validate data */		 	
	public boolean vldDATA()        
	{
		String strTEMP="";
		//tblRCTDTL.setRowSelectionInterval(0,0);
		//tblRCTDTL.setColumnSelectionInterval(0,0);
		
		try
		{
			float L_fltSTKQT_AVL = 0;
			for(int i=0;i<tblRCTDTL.getRowCount();i++)
    		{
				if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).toString().equals("true"))
    			{
					M_strSQLQRY = "select (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) ST_STKQT,ST_UCLQT from FG_STMST where";
					M_strSQLQRY +=" ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+getSUBSTR(cmbWRHTP.getSelectedItem().toString().trim(),0,2)+"'"
						+ " and ST_PRDTP = '"+txtPRDTP.getText().toString().trim()+"'"
						+ " and ST_LOTNO = '"+txtLOTNO.getText().toString().trim()+"'"
						+ " and ST_RCLNO = '"+txtRCLNO.getText().toString().trim()+"'"
						+ " and ST_PKGTP = '"+tblISSDTL.getValueAt(i,intIST_PKGTP).toString().trim()+"'"
						+ " and ST_MNLCD = '"+tblISSDTL.getValueAt(i,intIST_MNLCD).toString().trim()+"'";
					//System.out.println("M_strSQLQRY"+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!= null && M_rstRSSET.next())
					{	
						//System.out.println("val1"+M_rstRSSET.getFloat("ST_STKQT"));
						//System.out.println("val1"+Float.parseFloat(tblISSDTL.getValueAt(i,intIST_ISSQT).toString().trim()));
						L_fltSTKQT_AVL = M_rstRSSET.getFloat(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("C") ? "ST_STKQT" : "ST_UCLQT");
						if(L_fltSTKQT_AVL < Float.parseFloat(tblISSDTL.getValueAt(i,intIST_ISSQT).toString().trim()))
						{
							setMSG("Current Qty. available for transfer is "+L_fltSTKQT_AVL+", Please confirm...",'E');
							return false;
						}	
					}	
				}
			}
			for(int i=0;i<tblRCTDTL.getRowCount();i++)
    		{
				if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).toString().equals("true"))
    			{
					strTEMP = nvlSTRVL(tblRCTDTL.getValueAt(i,intRCT_MNLCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Main Location Cannot be Blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblRCTDTL.getValueAt(i,intRCT_PKGTP).toString(),"");
    				if(strTEMP.length() ==0)
    				{
    					setMSG("Package Category Cannot be Blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblRCTDTL.getValueAt(i,intRCT_RCTQT).toString(),"");
    				if(strTEMP.length() ==0)
    				{
    					setMSG("Quantity Cannot be Blank..",'E');
						return false;
    				}
    				strTEMP = nvlSTRVL(tblRCTDTL.getValueAt(i,intRCT_SHFCD).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    				    setMSG("Shift Cannot be Blank..",'E');
						return false;
					}
				}
			}
		}
		catch(Exception E)
		{
			System.out.println("Error in vldDATA() "+E);
		}	
		return true;
	}
	
	/* method to save data */
	void exeSAVE()
    {
       try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(!vldDATA())
			{
				return;
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
            {
				setCursor(cl_dat.M_curWTSTS_pbst);
                exeAUTADD();
            }
        }
        catch(Exception L_EX)
        {
           setMSG(L_EX,"exeSAVE"); 
        }
    }
	
	
	
    /**

     */
    private void exeENQRY()
    {
        try            
        {
            cl_dat.M_flgLCUPD_pbst = true;
            strWRHTP = getSUBSTR(cmbWRHTP.getSelectedItem().toString().trim(),0,2);
    		//strTRFTP = getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2);
    		strTRFNO = getSUBSTR(txtTRFNO.getText().toString().trim(),0,8);
    		
            L_strSTRQRY =  "Select IST_MNLCD,IST_PKGTP,IST_ISSQT,IST_ISSPK,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_ISSDT,IST_ISSTP from fg_istrn "; 
			L_strSTRQRY += "where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+ strWRHTP +"' and IST_ISSTP = '"+ getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2) +"' and IST_ISSNO = '"+ strTRFNO +"'"; 
					
			//System.out.println("L_strSTRQRY = " + L_strSTRQRY);
			M_rstRSLSET = cl_dat.exeSQLQRY1(L_strSTRQRY);
									
			if(M_rstRSLSET != null)
			{
				LM_CNT = 0;
				while(M_rstRSLSET.next())
				{
				    txtPRDTP.setText(M_rstRSLSET.getString("ist_prdtp").trim());
				    txtLOTNO.setText(M_rstRSLSET.getString("ist_lotno").trim());
					txtRCLNO.setText(M_rstRSLSET.getString("ist_rclno").trim());
					txtPKGTP.setText(M_rstRSLSET.getString("ist_pkgtp").trim());
					txtTRFDT.setText(M_rstRSLSET.getString("ist_issdt").trim());
					tblISSDTL.setValueAt(nvlSTRVL(M_rstRSLSET.getString("ist_mnlcd"),"").trim(),LM_CNT,intIST_MNLCD);
					tblISSDTL.setValueAt(nvlSTRVL(M_rstRSLSET.getString("ist_pkgtp"),"").trim(),LM_CNT,intIST_PKGTP);
					tblISSDTL.setValueAt(nvlSTRVL(M_rstRSLSET.getString("ist_issqt"),"").trim(),LM_CNT,intIST_ISSQT);
					tblISSDTL.setValueAt(nvlSTRVL(M_rstRSLSET.getString("ist_isspk"),"").trim(),LM_CNT,intIST_ISSPK);
				}
				LM_CNT++;
			}
			
			String L_strSTMQRY = "";
			L_strSTMQRY += "Select RCT_MNLCD,RCT_RCTQT,RCT_RCTPK,RCT_LOTNO,RCT_RCLNO,RCT_RCTDT,RCT_RCTTP,RCT_PKGTP,RCT_SHFCD from fg_rctrn ";
			L_strSTMQRY += "where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP = '"+ strWRHTP +"' and RCT_RCTTP = '"+ getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2) +"' and RCT_RCTNO = '"+ strTRFNO +"'"; 		
			//System.out.println("L_strSTMQRY = " + L_strSTMQRY);
			ResultSet L_rstRSMSET;
			L_rstRSMSET = cl_dat.exeSQLQRY1(L_strSTMQRY);
			if(L_rstRSMSET != null)
			{
				LM_CNT = 0;
				while(L_rstRSMSET.next())
				{
				   	tblRCTDTL.setValueAt(nvlSTRVL(L_rstRSMSET.getString("rct_mnlcd"),"").trim(),LM_CNT,intRCT_MNLCD);
					tblRCTDTL.setValueAt(nvlSTRVL(L_rstRSMSET.getString("rct_pkgtp"),"").trim(),LM_CNT,intRCT_PKGTP);
					tblRCTDTL.setValueAt(nvlSTRVL(L_rstRSMSET.getString("rct_rctqt"),"").trim(),LM_CNT,intRCT_RCTQT);
					tblRCTDTL.setValueAt(nvlSTRVL(L_rstRSMSET.getString("rct_rctpk"),"").trim(),LM_CNT,intRCT_RCTPK);
					tblRCTDTL.setValueAt(nvlSTRVL(L_rstRSMSET.getString("rct_shfcd"),"").trim(),LM_CNT,intRCT_SHFCD);
				}
				LM_CNT++;
			}
		}
        catch(Exception L_EX)
        {
           setMSG(L_EX,"exeENQRY"); 
        }
    }
    
	
	/**

	 */
	private double calISSQT()       //calculates Issue Quantity
	{ 
		double L_RUNQT = 0;
		try
		{
			for(int i = 0;i < (tblISSDTL.getRowCount() - 1);i++)
			{
				if(tblISSDTL.getValueAt(i,intIST_MNLCD).toString().trim().length() == 0)
					break;
				if(tblISSDTL.getValueAt(i,intIST_CHKFL).toString().trim().equals("true"))
				{	
					strTRFQT_ISS = tblISSDTL.getValueAt(i,intIST_ISSQT).toString().trim();
		   			if(strTRFQT_ISS == null || strTRFQT_ISS.equals(""))
						strTRFQT_ISS = "0.000";
					L_RUNQT = L_RUNQT + Double.parseDouble(strTRFQT_ISS);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calISSQT");
		}
		return Double.parseDouble(setNumberFormat(L_RUNQT,3));
	}
	
	/** method to validate transfer date */
	private void vldTRFDT()
	{ 
	    getREFDT(cl_dat.M_strLOGDT_pbst);
	}
		
	/** Returns true if all the condition for Lot No. matches */
	private boolean vldLOTNO()      
	{ 
	    try
	    {
	        if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("C"))
		    {
			    M_strSQLQRY = "Select * from FG_STMST where";
                M_strSQLQRY += " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) > 0 and ST_PRDTP = '"+txtPRDTP.getText()+"' and ST_LOTNO='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' ";
            }
		    else if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U"))
		    {
			    M_strSQLQRY = "Select * from FG_STMST where";
			    M_strSQLQRY += " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_UCLQT > 0  and ST_PRDTP = '"+txtPRDTP.getText()+"' and  ST_LOTNO='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' ";
		    }
		    M_strSQLQRY += " and ST_STSFL = '"+(rdbFRSSTK.isSelected() ? "1" : "2")+"'";
		    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		    if(M_rstRSSET.next())
		    {
			    return true;
		    }
		    if(M_rstRSSET !=null)
			    M_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
		    setMSG(L_EX,"vldLOTNO");
	    }
	    return false;   
	}
	
	/** Returns true if all the condition for RCL No. matches */
	private boolean vldRCLNO()
	{ 
	    try
	    {
		    if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("C"))
		    {
			    M_strSQLQRY = "Select * from FG_STMST where (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) > 0";
                M_strSQLQRY += " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  ST_PRDTP = '"+txtPRDTP.getText()+"' and ST_LOTNO='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and ST_RCLNO='"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"' "; 
		    }
		    else if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U"))
		    {
			    M_strSQLQRY = "Select * from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_UCLQT > 0";
			    M_strSQLQRY += " and ST_PRDTP = '"+txtPRDTP.getText()+"'  and ST_LOTNO='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and ST_RCLNO='"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"' ";
		    }
		    M_strSQLQRY += " and ST_STSFL = '"+(rdbFRSSTK.isSelected() ? "1" : "2")+"'";
		    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		    if(M_rstRSSET.next())
		    {
			    return true;
		    }
		    if(M_rstRSSET !=null)
			    M_rstRSSET.close();
	    }
	    catch(Exception L_EX)
	    {
		    setMSG(L_EX,"vldRCLNO");
	    }
	    return false;   
	}
	
	/* method to fetch data from the database */
	private void getALLREC()
	{
		try
		{   
		    exeCLRTBL();
			if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("C"))
			{
			    M_strSQLQRY = "Select st_mnlcd,st_pkgtp,(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) l_trnqt,isnull(st_resqt,0) st_resqt, st_remds from fg_stmst";
				M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP = '"+txtPRDTP.getText()+"' and st_lotno='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and st_rclno='"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
				M_strSQLQRY += " and st_pkgtp = '"+strPKGTP_ISS+"' and (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) > 0 ";
				if(getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2).equals(strTRFTP_RBG))
				{
				    M_strSQLQRY = "Select st_mnlcd,st_pkgtp,(isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) l_trnqt,isnull(st_resqt,0) st_resqt,st_remds from fg_stmst ";
					M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP = '"+txtPRDTP.getText()+"'  and st_lotno='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and st_rclno='"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
					M_strSQLQRY += " and st_pkgtp = '"+strPKGTP_ISS+"' and (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) > 0 and isnull(st_resqt,0)=0 ";
					//System.out.println(M_strSQLQRY);
				}
			}
			else if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U"))
			{
			    //System.out.println("EEE");
				M_strSQLQRY = "Select st_mnlcd,st_pkgtp,st_uclqt l_trnqt, isnull(st_resqt,0) st_resqt, st_remds from fg_stmst";
				M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_PRDTP = '"+txtPRDTP.getText()+"' and st_lotno='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and st_rclno='"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
				M_strSQLQRY += " and st_pkgtp = '"+strPKGTP_ISS+"' and st_uclqt > 0 ";
				//System.out.println(M_strSQLQRY);
			}
			M_strSQLQRY += " and ST_STSFL = '"+(rdbFRSSTK.isSelected() ? "1" : "2")+"'";
			//System.out.println("M_strSQLQRY>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				LM_CNT = 0;
				while(M_rstRSSET.next())
				{
				   // System.out.println("FFF");
					strTRFQT_ISS = nvlSTRVL(M_rstRSSET.getString("l_trnqt"),"").trim();
					tblISSDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("st_mnlcd"),"").trim(),LM_CNT,intIST_MNLCD);
					tblISSDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("st_pkgtp"),"").trim(),LM_CNT,intIST_PKGTP);
					tblISSDTL.setValueAt(strTRFQT_ISS,LM_CNT,intIST_ISSQT);
					tblISSDTL.setValueAt(calTRPKG(strPKGTP_ISS,strTRFQT_ISS),LM_CNT,intIST_ISSPK);
					tblISSDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("st_remds"),"").trim(),LM_CNT,intIST_REMDS);
					hstSTKQT.put(txtPRDTP.getText()+txtLOTNO.getText()+ txtRCLNO.getText()+nvlSTRVL(M_rstRSSET.getString("st_mnlcd"),"").trim()+nvlSTRVL(M_rstRSSET.getString("st_pkgtp"),"").trim(),strTRFQT_ISS);
					//System.out.println(txtLOTNO.getText()+" / "+ txtRCLNO.getText()+" / "+nvlSTRVL(M_rstRSSET.getString("st_mnlcd"),"").trim()+" / "+nvlSTRVL(M_rstRSSET.getString("st_pkgtp"),"").trim()+" / "+strTRFQT_ISS);
										
                    /*if(strTRFTP.equals(strTRFTP_WHT))
                    {
                        tblRCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("st_pkgtp"),"").trim(),LM_CNT,intIST_PKGTP);
                        txtRCTPKT.setEnabled(false);
                    }*/
                    LM_CNT++;
				}
			}
			else
				setMSG("Record for the entered Lot No. does not exist",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	
	/** method to validate issue quantity against stock quantity*/
	private boolean vldISSQT(String LP_PRDTP,String LP_LOTNO, String LP_RCLNO,String LP_MNLCD,String LP_PKGTP, String LP_ISSQT)
	{
		try
		{
			if(!hstSTKQT.containsKey(LP_PRDTP+LP_LOTNO + LP_RCLNO + LP_MNLCD + LP_PKGTP))
			{
			    setMSG("Record not found in hash table ",'N');
			    return false;
			}
			double L_dblISSQT = Double.parseDouble(hstSTKQT.get(LP_PRDTP+LP_LOTNO+LP_RCLNO+LP_MNLCD+LP_PKGTP).toString());
			if(Double.parseDouble(LP_ISSQT)>L_dblISSQT)
			{
				setMSG("Issue Qty Exceeds Stock Qty :"+ setNumberFormat(L_dblISSQT,3),'E');
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldISSQT");
		}
		return true;
	}
			
	/** method to display issue and receipt quantities*/	
	private void dspTRNQT(JTable tblXXXDTL,int XXX_CHKFL,int XXX_TRNQT,JTextField txtTOXXX)
	{
		double dblTRNQT = 0;
		try
		{
			for(int i = 0;i < tblXXXDTL.getRowCount();i++)
			{
				if(tblXXXDTL.getValueAt(i,XXX_CHKFL).toString().trim().equals("true"))
				{
					dblTRNQT += Double.parseDouble(tblXXXDTL.getValueAt(i,XXX_TRNQT).toString().trim());
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"dspTRNQT");
		}
		txtTOXXX.setText(setNumberFormat(dblTRNQT,3));
	}
	
	
	
	/** method to calculate packages*/
	private String calTRPKG(String LP_PKGTP,String LP_TBLQT)    //calculates Issue Packages & displays it
	{  
		try
		{
			ResultSet L_RSLSET1;
			double L_DISQT = 0;
			double L_NCSVL = 0;
			double L_DISPK = 0;
			flgPKGTP_VLD = true;
			if(!LP_PKGTP.equals("99"))
			{
				//System.out.println("LP_PKGTP : "+LP_PKGTP);
				double L_dblPKGWT = Double.parseDouble(getCODVL("SYSFGXXPKG"+LP_PKGTP,cl_dat.M_intNCSVL_pbst));
				//System.out.println("Pkg.Wt : "+setNumberFormat(L_dblPKGWT,3));
				double L_dblPKGNO = Double.parseDouble(LP_TBLQT)/L_dblPKGWT;
				System.out.println(LP_TBLQT+"  / "+L_dblPKGWT+" / "+L_dblPKGNO);
				//System.out.println("No.of Pkgs : "+setNumberFormat(L_dblPKGNO,3));
				L_dblPKGNO=Float.parseFloat(setNumberFormat(L_dblPKGNO,3));
				int L_intPKGNO = new Double(L_dblPKGNO).intValue();
				if(L_intPKGNO != L_dblPKGNO)
				//if(!setNumberFormat(L_intPKGNO,3).equals(setNumberFormat(L_dblPKGNO,3)))
					{
					    //LM_OPTNPN.showMessageDialog(this,"Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblPKGWT,3),"Error Message",JOptionPane.ERROR_MESSAGE);
						setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblPKGWT,3)+" No.of pkgs Expected ="+setNumberFormat(L_intPKGNO,3)+" Entered ="+setNumberFormat(L_dblPKGNO,3) ,'E');
					    flgPKGTP_VLD = false;
					    return "0.001";
					}
				if(L_dblPKGNO == 0)
					L_dblPKGNO = 1;
				//System.out.println("Returning : "+L_dblPKGNO);
				return String.valueOf(L_dblPKGNO);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calTRPKG");
		}
		return "1";
	}
	
	
	
	/** method to get product code*/
	private String getPRDTP()       
	{  
		try
		{
		    M_strSQLQRY = "Select distinct ST_PRDTP,ST_PRDCD,ST_TPRCD,ST_CPRCD from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP='"+strWRHTP+"'";
			M_strSQLQRY += "  and ST_PRDTP = '"+txtPRDTP.getText()+"' and ST_LOTNO='"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and ST_RCLNO='"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"' and ST_PKGTP='"+strPKGTP_ISS+"' and ST_MNLCD='"+strMNLCD+"'";
			//System.out.println("getPRDTP query =" + M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				strPRDTP = M_rstRSSET.getString("ST_PRDTP");
				strPRDCD = M_rstRSSET.getString("ST_PRDCD");
				strTPRCD = M_rstRSSET.getString("ST_TPRCD");
				strCPRCD = M_rstRSSET.getString("ST_CPRCD");
			}
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
				if(strPRDCD == null)
				{
					strPRDCD = " ";
					LM_OPTNPN.showMessageDialog(this,"Product Code not found for Lot No: "+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8),"Data Transfer Status",JOptionPane.ERROR_MESSAGE);
				}
				if(strCPRCD == null)
					strCPRCD = " ";
				if(strTPRCD == null)
					strTPRCD = " ";
			return strPRDTP;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDTP");
		}	
		return "";
	}
		
	private String getSTKQT()    //gets the Stock Qty.
	{
		String L_STKQT = "";
		try
		{
			if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("C"))
			{		
				M_strSQLQRY = "Select (sum(ST_STKQT)-sum(ST_ALOQT)) STKQT from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"' and ST_PRDTP = '"+strPRDTP+"'";
				M_strSQLQRY += "  and ST_PRDTP = '"+txtPRDTP.getText()+"' and ST_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and ST_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"' and ST_MNLCD = '"+strMNLCD+"' and ST_PKGTP = '"+strPKGTP_ISS+"'"; 
			}
			else if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U"))
			{		
				M_strSQLQRY = "Select sum(isnull(ST_UCLQT,0)) STKQT from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_UCLQT > 0 and ST_WRHTP = '"+strWRHTP+"' and ST_PRDTP = '"+strPRDTP+"'";
				M_strSQLQRY += "  and ST_PRDTP = '"+txtPRDTP.getText()+"' and ST_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"' and ST_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"' and ST_MNLCD = '"+strMNLCD+"' and ST_PKGTP = '"+strPKGTP_ISS+"'"; 
			}
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
				L_STKQT = M_rstRSSET.getString("STKQT");
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getSTKQT");
		}
		return L_STKQT;
	}
	
	/** method to calculate receipt quantitys*/	
	private double calRCTQT()       
	{
		double L_RUNQT = 0;
		String L_RCTQT = "";
		try
		{
			for(int i = 0;i < tblRCTDTL.getRowCount();i++)
			{
				if(tblRCTDTL.getValueAt(i,intRCT_MNLCD).toString().trim().length() == 0)
					break;
				if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).toString().trim().equals("true"))
				{
					L_RCTQT = tblRCTDTL.getValueAt(i,intRCT_RCTQT).toString().trim(); 
					if(L_RCTQT == null || L_RCTQT.equals(""))
						L_RCTQT = "0.000";
					L_RUNQT = L_RUNQT + Double.parseDouble(L_RCTQT);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calRCTQT");
		}
		return Double.parseDouble(setNumberFormat(L_RUNQT,3));
	}
	
	/** method to validate main location */				
	private boolean vldRCMNL()      
	{
		try
		{   
		    strMNLCD = ((JTextField)tblRCTDTL.cmpEDITR[intRCT_MNLCD]).getText().toString().trim();
			if(strMNLCD.length() == 0)
				return true;
			if(strMNLCD.length() == 0)
				return true;
		    //System.out.println("strMNLCD =" + strMNLCD);
		    String L_strSQLQRY1 = "Select lc_mnlcd l_mnlcd from fg_lcmst";
            //System.out.println("vldRCMNL query1 ="+ L_strSQLQRY1); 
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY1);
			while(M_rstRSSET.next())
			{
			    if(strMNLCD.equals(nvlSTRVL(M_rstRSSET.getString("l_mnlcd").toString(),"").trim()))
			    //M_rstRSSET.close();
			    return true;
			}
			if(M_rstRSSET != null)
			    M_rstRSSET.close();
						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldRCMNL");
		}
		return false;
	}
	
	/** method to validate package type */
	private boolean vldRCPKT()      
	{
		try
		{
			strSTKTP = rdbSLRSTK.isSelected() ? "2" : "1";
			//System.out.println("strSTKTP : "+strSTKTP);
		    strRCPKG =	 ((JTextField)tblRCTDTL.cmpEDITR[intRCT_PKGTP]).getText().toString().trim();
			if(strRCPKG.length()==0)
				return true;
			//System.out.println("strRCPKG : "+strRCPKG);
		    if(strSTKTP.equals("1"))
			{
				M_strSQLQRY = "Select * from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'FGXXPKG' and CMT_CODCD='"+strRCPKG+"'";
                //System.out.println("M_strSQLQRY ="+M_strSQLQRY);
                M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET.next())
					return true;
				if(M_rstRSSET !=null)
					M_rstRSSET.close();
			}
			//else if(strSTKTP.equals("3"))
			//{
			//	if(strRCPKG.equals(strPKGTP))	
			//		return true;
			//}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldRCPKT");
		}
		return false;
	}

	
	/** Validating qty. for specified package type

	 */
	private boolean vldTRFQT(double LP_QTYVL, String LP_PKGTP)      
	{
		boolean L_flgRETFL = true;
		try
		{
			if(LP_PKGTP.length() == 0)
				return true;
			if(!cl_dat.M_hstMKTCD_pbst.containsKey("SYSFGXXPKG"+LP_PKGTP))
			{setMSG("Invalid Package Type : "+LP_PKGTP,'E'); return false;}
			setMSG("",'N');
			//System.out.println("LP_PKGTP : "+LP_PKGTP);
			double L_dblPKGWT = Double.parseDouble(getCODVL("SYSFGXXPKG"+LP_PKGTP,cl_dat.M_intNCSVL_pbst));
			//System.out.println("Pkg Wt : "+setNumberFormat(L_dblPKGWT,3));
			double L_dblPKGNO = LP_QTYVL/L_dblPKGWT;
			//System.out.println("No.of Pkgs : "+setNumberFormat(L_dblPKGNO,3));
			L_dblPKGNO=Float.parseFloat(setNumberFormat(L_dblPKGNO,3));
			int L_intPKGNO = new Double(L_dblPKGNO).intValue();
			if(L_intPKGNO != L_dblPKGNO)
			{
			    //LM_OPTNPN.showMessageDialog(this,"Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblPKGWT,3),"Error Message",JOptionPane.ERROR_MESSAGE);
				setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblPKGWT,3),'E');
			    L_flgRETFL = false;
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldPKGVL");
			L_flgRETFL = false;
		}
		return L_flgRETFL;
	}
	

	/** Validating packages for Specified 

	 */
	private boolean vldTRFPK(double LP_QTYVL,double LP_PKGVL, String LP_PKGTP)      
	{
		boolean L_flgRETFL = true;
		try
		{
			if(LP_PKGTP.length() == 0)
				return true;
			if(!cl_dat.M_hstMKTCD_pbst.containsKey("SYSFGXXPKG"+LP_PKGTP))
				{setMSG("Invalid Package Type : "+LP_PKGTP,'E'); return false;}
			setMSG("",'N');
			double L_dblPKGWT = Double.parseDouble(getCODVL("SYSFGXXPKG"+LP_PKGTP,cl_dat.M_intNCSVL_pbst));
			double L_dblPKGNO = LP_QTYVL/L_dblPKGWT;
			L_dblPKGNO=Float.parseFloat(setNumberFormat(L_dblPKGNO,3));
			int L_intPKGNO = new Double(L_dblPKGNO).intValue();
			if(L_intPKGNO != L_dblPKGNO)
			{
			    //LM_OPTNPN.showMessageDialog(this,"Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblPKGWT,3),"Error Message",JOptionPane.ERROR_MESSAGE);
				setMSG("Qty. not in multiple of pkg.wt:"+setNumberFormat(L_dblPKGWT,3),'E');
			    L_flgRETFL = false;
			}
			if(!setNumberFormat(LP_PKGVL,3).equals(setNumberFormat(L_dblPKGNO,3)))
			{
				//LM_OPTNPN.showMessageDialog(this,"No. of pkgs should be : "+setNumberFormat(L_dblPKGNO,3),"Error Message",JOptionPane.ERROR_MESSAGE);
				setMSG("No. of pkgs should be : "+setNumberFormat(L_dblPKGNO,3),'E');
			    L_flgRETFL = false;
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldPKGVL");
			L_flgRETFL = false;
		}
		return L_flgRETFL;
	}

	
	
	/** method to validate shift code*/
	private boolean vldSHFCD(String LP_SHFCD)      
	{
		try
		{   
			M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='M"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'COXXSHF' and CMT_CODCD='"+LP_SHFCD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
				return true;
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldSHFCD");
		}
		return false;
	}
	
	/* Validates the Transfer Type with the database Table CO_CDTRN &	fetches the Stock Type into strSTKTP */
	private boolean vldTRFTP()          
	{ 
		try
		{
		    M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01 from CO_CDTRN where CMT_CGMTP='SYS' and";
			M_strSQLQRY += " CMT_CGSTP = 'FGXXITP' and CMT_CHP01 in ('1','3') and CMT_CHP02 in '2'";
			M_strSQLQRY += " and CMT_CCSVL in ('1','2') and CMT_CODCD='"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"'";
			//System.out.println("M_strSQLQRY "+M_strSQLQRY);
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				strSTKTP = M_rstRSSET.getString("CMT_CHP01").toString().trim();
				//System.out.println("strSTKTP = "+strSTKTP);
				return true;
		    }
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTRFTP");							   
		}
		return false;
	}
	
	/**

	 */
	private boolean chkPKGWT()      //checks for package weight
	{
		String L_strPKGNO = "";
	    try
	    {
	        //System.out.println("In chkPKGWT");
		    for(int i=0;i<tblRCTDTL.getRowCount();i++)
		    {
		    	if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).equals(new Boolean(true)))
		    	{
		    	
				    if(tblRCTDTL.getValueAt(i,intRCT_PKGTP).toString().trim().length() != 2)
					    continue;
				    L_strPKGNO = calTRPKG(tblRCTDTL.getValueAt(i,intRCT_PKGTP).toString().trim(),tblRCTDTL.getValueAt(i,intRCT_RCTQT).toString().trim());
					//System.out.println("In L_strPKGNO =" + L_strPKGNO);
				    if(!flgPKGTP_VLD)
						return false;
				}
		    }
		    for(int i=0;i<tblISSDTL.getRowCount();i++)
		    {
		    	if(tblISSDTL.getValueAt(i,intIST_CHKFL).equals(new Boolean(true)))
		    	{
				    if(tblISSDTL.getValueAt(i,intIST_ISSQT).toString().trim().length() == 0)
						continue;
				    L_strPKGNO = calTRPKG(txtPKGTP.getText(),tblISSDTL.getValueAt(i,intIST_ISSQT).toString().trim());
					//System.out.println("In L_strPKGNO =" + L_strPKGNO);
					if(!flgPKGTP_VLD)
						return false;
				}
			}
	    }
	    catch(Exception L_EX)
	    {
	        setMSG("Error in chkPKGWT : "+L_EX,'E');
	    }
	    return true;
	}
	
	
	
	
	/* On Save Button click Authorization procedure starts from here i.e data are inserted or updated into respective Tables */
	private void exeAUTADD()
	{ 
	  try
	  {
	      //System.out.println("In exeAUTADD");
		  if(!chkPKGWT())
		  {
		    LM_OPTNPN.showMessageDialog(this,"Record Not Saved","Error Message",JOptionPane.ERROR_MESSAGE);
		    return;
		  }
		    //System.out.println("Get Values");
    		strWRHTP = getSUBSTR(cmbWRHTP.getSelectedItem().toString().trim(),0,2);
    		//strTRFTP = getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2);
    		strTRFNO = getSUBSTR(txtTRFNO.getText().toString().trim(),0,8);
    		strTRFDT = getSUBSTR(txtTRFDT.getText().toString().trim(),0,10);
    		//strCLSTG = getSUBSTR(cmbCLSTG.getSelectedItem().toString().trim(),0,1);
    		//strRCLNO = getSUBSTR(txtRCLNO.getText().toString().trim(),0,2);
    		strPKGTP_ISS = getSUBSTR(txtPKGTP.getText().toString().trim(),0,2);
    		strREMDS = getSUBSTR(txtREMDS.getText().toString().trim(),0,50);
    		strSALTP = "";
    		strMKTTP = "";
    		cl_dat.M_flgLCUPD_pbst = true;
    		strCODCD = strYRDGT + getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2);
    		setCursor(cl_dat.M_curWTSTS_pbst);
			strCURTM = "";
			strUSRCD = "";
			updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",strCODCD,strCURTM,strUSRCD);
			
		  if(chkISRCQT())
		  {
		   // System.out.println("Out of chkISRCQT");
			if(vldCDTRN())
			{
				//System.out.println("Out of vldCDTRN");
				strCURTM = cl_dat.getCURTIM();
				strUSRCD = cl_dat.M_strUSRCD_pbst;
				strSTKTP = rdbSLRSTK.isSelected() ? "2" : "1";
				
				updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",strCODCD,strCURTM,strUSRCD);
				//System.out.println("strTRFNO1 =" + strTRFNO);
				if(strTRFNO.length() == 0)
				{
					setMSG("Generating Transfer Number.",'N');
					genTRFNO();
				}
					strTRFNO = getSUBSTR(txtTRFNO.getText(),0,8);
					//System.out.println("strTRFNO2 =" + strTRFNO);
					setMSG("Updating Issues.",'N');
					genISSUE();             // Generating Issue for the entered Transfer Type
					//System.out.println("Out of genISSUE");
					//System.out.println("Issue Generated: " + cl_dat.M_flgLCUPD_pbst);
					if(cl_dat.M_flgLCUPD_pbst)
					{
						if(cl_dat.exeDBCMT("exeSAVE"))
						{
							setMSG("Generating Receipts.",'N');
							genRECPT();     // Generating Receipt for the entered Transfer Type
							//System.out.println("Receipt Generated: "+cl_dat.M_flgLCUPD_pbst);
							if(cl_dat.M_flgLCUPD_pbst)
							{
								setMSG("Authorizing Receipts.",'N');
								autRECPT();
								//System.out.println("Receipt Authorized: "+ cl_dat.M_flgLCUPD_pbst);
								if(cl_dat.M_flgLCUPD_pbst)
								{
									if(!strTRFNO.equals(strPRVNO))
									{ 
										updGENNO();
										strPRVNO = strTRFNO;
									}
									strCURTM = "";
									strUSRCD = "";
									updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",strCODCD,strCURTM,strUSRCD);
								    //System.out.println("Commit Completed: "+ cl_dat.M_flgLCUPD_pbst);
									setCursor(cl_dat.M_curDFSTS_pbst);
									LM_OPTNPN.showMessageDialog(this,"Data Transfer Completed Successfully.","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
									setMSG("Data Transfer Completed Successfully.",'N');
									exeCLRTBL();
									txtTOISS.setText("");
									txtTORCT.setText("");
									cmbCLSTG.requestFocus();
								}
								else
								{
									strCURTM = "";
									strUSRCD = "";
									updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",strCODCD,strCURTM,strUSRCD);
									setCursor(cl_dat.M_curDFSTS_pbst);
									setMSG("Receipt Authorization Failed ... ",'E');
								}
							}
							else
							{
								strCURTM = "";
								strUSRCD = "";
								updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",strCODCD,strCURTM,strUSRCD);
								setCursor(cl_dat.M_curDFSTS_pbst);
								setMSG("Receipt Generation Failed ... ",'E');
							}
						}
						else
						{
						    strCURTM = "";
							strUSRCD = "";
							updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXISS",strCODCD,strCURTM,strUSRCD);
							setCursor(cl_dat.M_curDFSTS_pbst);
							setMSG("Issue Generation Failed ... ",'E');
						}
					}
					else
					{
						LM_OPTNPN.showMessageDialog(this,"Please Wait... In use by: "+strCHP01+" since "+strCHP02,"Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
					}
			    }
			}
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);	
		}
	}
	
	/* method to update table CO_CDTRN */
	public void updCDTRN(String LP_CGMTP,String LP_CGSTP,String LP_CODCD,String LP_CURTM,String LP_USRCD)
	{
	    cl_dat.M_flgLCUPD_pbst = true;
		try
		{
		    //System.out.println("In updCDTRN");
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += "CMT_CHP01 = '"+LP_USRCD+"',";
			M_strSQLQRY += "CMT_CHP02 = '"+LP_CURTM+"'";
			M_strSQLQRY += " where CMT_CGMTP = '"+LP_CGMTP+"'";
			M_strSQLQRY += " and CMT_CGSTP = '"+LP_CGSTP+"'";
			M_strSQLQRY += " and CMT_CODCD = '"+LP_CODCD+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			//System.out.println("UpdCDTRN query =" + M_strSQLQRY);
			//System.out.println("cl_dat.M_flgLCUPD_pbst =" + cl_dat.M_flgLCUPD_pbst);                   
			if(cl_dat.M_flgLCUPD_pbst)
			{
				if(cl_dat.exeDBCMT("exeSAVE"))
	            {
		            setMSG("Data saved successfully",'N');
		            //txtTRFNO.setText("");
		            //txtLOTNO.setText("");
		            //txtRCLNO.setText("");
		            //txtPKGTP.setText("");
	            }		
			}
			//System.out.println("Out of updCDTRN");
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
	}
	
	/* method to check the issue and receipt quantity */
	private boolean chkISRCQT()
	{
	    //System.out.println("In chkISRCQT");
		boolean L_RETFL = true;
		try
		{
			dblTORCQT = calRCTQT();
			dblTOISQT = calISSQT();
			if(Double.parseDouble(setNumberFormat(dblTORCQT,3)) != Double.parseDouble(setNumberFormat(dblTOISQT,3)))
			{
				LM_OPTNPN.showMessageDialog(this,"Receipt Qty. "+setNumberFormat(dblTORCQT,3)+"  must match Issue Qty. "+setNumberFormat(dblTOISQT,3),"Data Transfer Status",JOptionPane.ERROR_MESSAGE);
				
				L_RETFL = false;
			}
			return L_RETFL;
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
		return L_RETFL;
		
	}
	
	private boolean vldCDTRN()
	{
		try
		{
		    // System.out.println("In vldCDTRN");
		    String L_strSTRSQL="";
			L_strSTRSQL = "Select CMT_CHP01,CMT_CHP02 FROM CO_CDTRN WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			L_strSTRSQL += " AND CMT_CGSTP = 'FGXXISS' AND CMT_CODCD = '"+ strCODCD +"'";
			//System.out.println("vldCDTRN query ="+ L_strSTRSQL);
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSTRSQL);
			while(M_rstRSSET.next())
			{
				strCHP01 = M_rstRSSET.getString("CMT_CHP01");
				strCHP02 = M_rstRSSET.getString("CMT_CHP02");
			}
			if(M_rstRSSET != null)
					M_rstRSSET.close();
			if(strCHP01 == null || strCHP01.equals(""))
					return true;
			//System.out.println("Moving Out of vldCDTRN");
		} 
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
		return false;
	}
	
	private void genTRFNO()     //generates transfer number
	{ 
		try
		{
		    strTRFNO = "";
			strUPDNO = "";
			strGNTFNO = "";
			int L_STRNO = 0;
			L_strSTRSQL = "Select CMT_CCSVL FROM CO_CDTRN WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			L_strSTRSQL += " AND CMT_CGSTP = 'FGXXISS' AND CMT_CODCD = '"+strCODCD+"'";
			//System.out.println("genTRFNO query =" + L_strSTRSQL);
			L_rstRSLSET = cl_dat.exeSQLQRY2(L_strSTRSQL);
			if(L_rstRSLSET.next())
				strGENNO = L_rstRSLSET.getString("CMT_CCSVL");
			if(L_rstRSLSET  !=null)
				L_rstRSLSET .close();
			if(strGENNO != null)
			{
				L_STRNO = Integer.parseInt(strGENNO.trim());
				L_STRNO = L_STRNO + 1;
				strGENNO = Integer.toString(L_STRNO);
				for(int i=0;i<(5-strGENNO.length());i++)
				{
						strUPDNO = strUPDNO + "0";
				}
				strUPDNO = strUPDNO + strGENNO;
				strGNTFNO = strCODCD + strUPDNO;
				txtTRFNO.setText(strGNTFNO);
				LM_OPTNPN.showMessageDialog(this,"Please note down the Transfer No.: "+strGNTFNO+"","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				cl_dat.M_flgLCUPD_pbst = false;
				LM_OPTNPN.showMessageDialog(this,"Transfer Number not generated.Contact Systems Dept.","Data Transfer Status",JOptionPane.ERROR_MESSAGE);
			}
			setCursor(cl_dat.M_curWTSTS_pbst);
			//System.out.println("Out of genTRFNO");
		}
		catch(Exception L_EX)
		{
		   setMSG(L_EX,"genTRFNO");
	    }
	}
	
	private void genISSUE()
	{
		try
		{
		    //System.out.println("In genISSUE");
			setMSG("Updating FG_ISTRN.",'N');
			addISTRN();
			setMSG("Updating FG_STMST.",'N');
			//addSTMST();
			saveSTMST_ISS();
			if(strREMDS.length() != 0)
			{
				strTRNTP = "IS";
				setMSG("Updating FG_RMMST.",'N');
				addRMMST();
			}
			//System.out.println("Moving out of genISSUE");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genISSUE");
		}
	}
	
	private void addISTRN()     //Inserting or Modifying Issue Transaction
	{  
	    cl_dat.M_flgLCUPD_pbst = true;
	    if(cl_dat.M_flgLCUPD_pbst)
	    {	
	        //System.out.println("In addISTRN");
		    try
		    {
                 strMKTTP = rdbFRSSTK.isSelected() ? "" : "SR";
			     for(int i = 0;i <= (tblISSDTL.getRowCount()-1);i++)
			     {			
				    if(getISSVAL(i))
				    {	
					    M_strSQLQRY = "Select * from fg_istrn where";
					    M_strSQLQRY += " IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_ISSTP = '"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"'";
					    M_strSQLQRY += " and IST_ISSNO = '"+strTRFNO+"'";
					    M_strSQLQRY += " and IST_PRDCD = '"+strPRDCD+"'";
					    M_strSQLQRY += " and IST_PRDTP = '"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"'";
					    M_strSQLQRY += " and IST_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
					    M_strSQLQRY += " and IST_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
					    M_strSQLQRY += " and IST_PKGTP = '"+strPKGTP_ISS+"'";
					    M_strSQLQRY += " and IST_MNLCD = '"+strMNLCD+"'";
					    M_rstRSSET =cl_dat.exeSQLQRY(M_strSQLQRY);
					    //System.out.println( " M_strSQLQRY  =" +  M_strSQLQRY);
						//strSTKTP = rdbSLRSTK.isSelected() ? "2" : "1";
					    if(getRECCNT(M_strSQLQRY) > 0)
					    {   					        
							updISTRN();
					    }
					    else
					    {
							insISTRN();
							//System.out.println( "cl_dat.M_flgLCUPD_pbst in insert  =" +  cl_dat.M_flgLCUPD_pbst);
				    	}
					    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						//System.out.println( "addISTRN : " +  cl_dat.M_flgLCUPD_pbst);
					     ///cl_dat.M_flgLCUPD_pbst = false;
				    }
				}
		    }
		    catch(Exception L_EX)
		    {
				setMSG(L_EX,"addISTRN");
			}
		}
	}
	
	public int getRECCNT(String LP_SQLSTR)
	{
		int L_RETVAL = -1;
		try
		{
			//System.out.println(" LP_SQLSTR = "+ LP_SQLSTR);
			M_rstRSSET = cl_dat.exeSQLQRY1(LP_SQLSTR);
			if(M_rstRSSET.next())
			  L_RETVAL = M_rstRSSET.getInt(1);
			M_rstRSSET.close();
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRECCNT");
	        L_RETVAL = -1;              
	    }
		return L_RETVAL;
	}
	
	/* method to update table FG_ISTRN i.e. Modifiy Issue Transaction */
	private void updISTRN()         
	{ 
	    cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			M_strSQLQRY = "Update FG_ISTRN set ";
			M_strSQLQRY += "IST_ISSQT = IST_ISSQT + "+strTRFQT_ISS+",";
			M_strSQLQRY += "IST_ISSPK = IST_ISSPK + "+strISPKG+",";
			M_strSQLQRY += "IST_PKGTP = '"+strPKGTP_ISS+"',";
			//M_strSQLQRY += "IST_PKGTP = '"+strPKGCT+"',";
            M_strSQLQRY += "IST_MKTTP = '"+strMKTTP+"',";
			if(setNumberFormat(Double.parseDouble(strTRFQT_ISS),3).equals("0.000"))
				M_strSQLQRY += "IST_STSFL = 'X',";
			else
				M_strSQLQRY += "IST_STSFL = '2',";
			M_strSQLQRY += "IST_TRNFL = '0',";
			M_strSQLQRY += "IST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "IST_LUPDT = "+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and IST_ISSTP = '"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and IST_ISSNO = '"+strTRFNO+"'";
			M_strSQLQRY += " and IST_PRDCD = '"+strPRDCD+"'";
			M_strSQLQRY += " and IST_PRDTP = '"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and IST_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
			M_strSQLQRY += " and IST_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and IST_PKGTP = '"+strPKGTP_ISS+"'";
			M_strSQLQRY += " and IST_MNLCD = '"+strMNLCD+"'";
			//System.out.println(" updISTRN  = " + M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updISTRN");
		}
	}
	
	/* method to insert data into table FG_ISTRN */
	private void insISTRN()     
	{ 
	    cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			M_strSQLQRY = "Insert into FG_ISTRN(IST_CMPCD,IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDTP,IST_TDSFL,";
			M_strSQLQRY += "IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGTP,IST_ISSDT,IST_AUTDT,IST_ISSQT,";
			M_strSQLQRY += "IST_ISSPK,IST_STKTP,IST_STSFL,IST_TRNFL,IST_LUSBY,IST_LUPDT,IST_PRDCD,";
			M_strSQLQRY += "IST_SALTP,IST_MKTTP) values (";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+strWRHTP+"',";
			M_strSQLQRY += "'"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"',";
			M_strSQLQRY += "'"+strTRFNO+"',";
			M_strSQLQRY += "'"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"',";
			M_strSQLQRY += "'"+cmbCLSTG.getSelectedItem().toString().substring(0,1).trim()+"',";
			M_strSQLQRY += "'"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"',";
			M_strSQLQRY += "'"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"',";
			M_strSQLQRY += "'"+strMNLCD+"',";
			M_strSQLQRY += "'"+strPKGTP_ISS+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRFDT))+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRFDT))+"',";
			M_strSQLQRY += "'"+strTRFQT_ISS+"',";
			M_strSQLQRY += "'"+strISPKG+"',";
			M_strSQLQRY += "'"+strSTKTP+"',";
			if(setNumberFormat(Double.parseDouble(strTRFQT_ISS),3).equals("0.000"))
				M_strSQLQRY += "'X',";
			else
				M_strSQLQRY += "'2',";
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY += "'"+strPRDCD+"',";
			M_strSQLQRY += "'"+strSALTP+"',";
			M_strSQLQRY += "'"+strMKTTP+"')";
			//System.out.println(" insISTRN query : " + M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insISTRN");
		}
	}                       
	
	/* Updating Stock Qty. into the Stock Master i.e FG_STMST */
	private void addSTMST()     
	{  
	    cl_dat.M_flgLCUPD_pbst = true;
	    if (cl_dat.M_flgLCUPD_pbst)
	    {	
		    try
		    {
			    for(int i=0;i <= (tblISSDTL.getRowCount()-1);i++)
			    {			
				    if(getISSVAL(i))
				    {
					    L_strSTLSQL = "Select count(*) from fg_stmst";
					    L_strSTLSQL += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"'";
					    L_strSTLSQL += " and ST_PRDTP = '"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"'";
					    L_strSTLSQL += " and ST_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
					    L_strSTLSQL += " and ST_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
					    L_strSTLSQL += " and ST_PKGTP = '"+strPKGTP_ISS+"'";
					    L_strSTLSQL += " and ST_MNLCD = '"+strMNLCD+"'";
					    if(getRECCNT(L_strSTLSQL) > 0)
					    {
						    updSTMST();
						    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						    
					    }
					    else
					    {
					        setMSG("Record does not exist in FG_STMST",'E');
					        cl_dat.M_flgLCUPD_pbst = false;
					    }
				    }
			    }
		    }   
		    catch(Exception L_EX)
		    {
			setMSG(L_EX,"addSTMST");					   
			}
		}
	}
	
	/* method for updating Stock Master i.e FG_STMST */
	private void updSTMST()     
	{ 
	    cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			M_strSQLQRY = "Update FG_STMST set ";
			if(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U"))
				M_strSQLQRY += "ST_UCLQT = ST_UCLQT - "+strTRFQT_ISS+",";
			else
			{
				M_strSQLQRY += "ST_STKQT = ST_STKQT - "+strTRFQT_ISS+",";
			}
			M_strSQLQRY += "ST_TRNFL = '0',";
			M_strSQLQRY += "ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "ST_LUPDT = "+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and ST_PRDTP = '"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and ST_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
			M_strSQLQRY += " and ST_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and ST_PKGTP = '"+strPKGTP_ISS+"'";
			M_strSQLQRY += " and ST_MNLCD = '"+strMNLCD+"'";
			//System.out.println(" updSTMST : " + M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updSTMST");
		}
	}
	
	private void genRECPT()
	{
		try
		{
			setMSG("Updating FG_RCTRN.",'N');
			addRCTRN();
			if(strREMDS.length() != 0)
			{
				strTRNTP = "RC";
				setMSG("Updating FG_RMMST.",'N');
				addRMMST();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genRECPT");
		}
	}
	
    /* Inserting into Receipt Transaction Table i.e FG_RCTRN */
	private void addRCTRN()  
	{
	    cl_dat.M_flgLCUPD_pbst = true;
	    if(cl_dat.M_flgLCUPD_pbst)
	    {
	        //System.out.println("in addRCTRN");
	        try
	        {              
		        //strTRFTP = getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2);
		        strTRFDT = getSUBSTR(txtTRFDT.getText(),0,10);
		        strTRFNO = getSUBSTR(txtTRFNO.getText(),0,8);
                strISSRF = rdbFRSSTK.isSelected() ? "" : "SR";
		        for(int i = 0;i < (tblRCTDTL.getRowCount()-1);i++)           
		        {  
			        if(getRCTVAL(i))
			        {
				        L_strSTLSQL = "Select count(*) from fg_rctrn";
        				L_strSTLSQL += " where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP = '"+strWRHTP+"'";
        				L_strSTLSQL += " and RCT_RCTTP = '"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"'";
        				L_strSTLSQL += " and RCT_RCTNO = '"+strTRFNO+"'";
        				L_strSTLSQL += " and RCT_PRDTP = '"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"'";
        				L_strSTLSQL += " and RCT_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
        				L_strSTLSQL += " and RCT_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
        				L_strSTLSQL += " and RCT_PKGTP = '"+strPKGTP_RCT+"'";
        				L_strSTLSQL += " and RCT_MNLCD = '"+strMNLCD+"'";
        				if(getRECCNT(L_strSTLSQL) > 0)
        					updRCTRN();
        				else
        					insRCTRN();
        				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        			}
			    }
	        }
	        catch(Exception L_EX)
	        {
			    cl_dat.M_flgLCUPD_pbst = false;
			    setMSG(L_EX,"addCDTRN");					   
		    }
		}
	}
	
	/* method for updating Receipt Transaction i.e FG_RCTRN */
	private void updRCTRN()
	{
	    cl_dat.M_flgLCUPD_pbst = true;              
		try
		{
			M_strSQLQRY = "Update FG_RCTRN set ";
			M_strSQLQRY += "RCT_RCTQT = RCT_RCTQT + "+strTRFQT_RCT+",";
			M_strSQLQRY += "RCT_RCTPK = RCT_RCTPK + "+strRCTPK+",";
			//M_strSQLQRY += "RCT_PKGCT = '"+strPKGCT+"',";
			M_strSQLQRY += "RCT_SHFCD = '"+strSHFCD+"',";
			M_strSQLQRY += "RCT_STKTP = '"+strSTKTP+"',";
			M_strSQLQRY += "RCT_AUTBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "RCT_STSFL = '2',";
			M_strSQLQRY += "RCT_TRNFL = '0',";
            M_strSQLQRY += "RCT_ISSRF = '"+strISSRF+"',";
			M_strSQLQRY += "RCT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "RCT_AUTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRFDT))+"',";
			M_strSQLQRY += "RCT_LUPDT = "+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and RCT_RCTTP = '"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and RCT_RCTNO = '"+strTRFNO+"'";
			M_strSQLQRY += " and RCT_PRDTP = '"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and RCT_LOTNO = '"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"'";
			M_strSQLQRY += " and RCT_RCLNO = '"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and RCT_PKGTP = '"+strPKGTP_RCT+"'";
			M_strSQLQRY += " and RCT_MNLCD = '"+strMNLCD+"'";
			//System.out.println(" updRCTRN : " + M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updRCTRN");
		}
	}
	
	/* method for inserting data into receipt transaction table i.e FG_RCTRN */	
	private void insRCTRN()
	{
	    cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			M_strSQLQRY = "Insert into FG_RCTRN(RCT_CMPCD,RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,";
			M_strSQLQRY += "RCT_RCTDT,RCT_LOTNO,RCT_RCLNO,RCT_MNLCD,RCT_STKTP,RCT_PKGTP,";
			M_strSQLQRY += "RCT_RCTQT,RCT_RCTPK,RCT_SHFCD,RCT_AUTBY,RCT_AUTDT,";
            M_strSQLQRY += "RCT_STSFL,RCT_TRNFL,RCT_ISSRF,RCT_LUSBY,RCT_LUPDT) values (";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+strWRHTP+"',";
			M_strSQLQRY += "'"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"',";
			M_strSQLQRY += "'"+strTRFNO+"',";
			M_strSQLQRY += "'"+getSUBSTR(txtPRDTP.getText().toString().trim(),0,2)+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRFDT))+"',";
			M_strSQLQRY += "'"+getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+"',";
			M_strSQLQRY += "'"+getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+"',";
			M_strSQLQRY += "'"+strMNLCD+"',";
			M_strSQLQRY += "'"+strSTKTP+"',";
			M_strSQLQRY += "'"+strPKGTP_RCT+"',";
			M_strSQLQRY += strTRFQT_RCT+",";
			M_strSQLQRY += strRCTPK+",";
			M_strSQLQRY += "'"+strSHFCD+"',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strTRFDT))+"',";
			M_strSQLQRY += "'2',";
			M_strSQLQRY += "'0',";
            M_strSQLQRY += "'"+strISSRF+"',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"')";
			//System.out.println(" insRCTRN : " + M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insRCTRN" );
		}
	}
	
	private void addRMMST()     //Inserting or Updating into Remark Master i.e FG_RMMST
	{  
	    cl_dat.M_flgLCUPD_pbst = true;
	    if(cl_dat.M_flgLCUPD_pbst)
	    {
		    try
		    {
			    L_strSTLSQL = "Select count(*) from fg_rmmst";
			    L_strSTLSQL += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP = '"+strWRHTP+"'";
			    L_strSTLSQL += " and RM_TRNTP = '"+strTRNTP+"'";
			    L_strSTLSQL += " and RM_DOCTP = '"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"'";
			    L_strSTLSQL += " and RM_DOCNO = '"+strTRFNO+"'";
			    if(getRECCNT(L_strSTLSQL) > 0)
				    updRMMST();
			    else
				    insRMMST();
			    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			    
		    }
		    catch(Exception L_EX)
		    {
			    setMSG(L_EX,"addRMMST");					   
		    }
		}
	}
	
	/* method for updating remark master i.e. FG_RMMST */
	private void updRMMST()
	{
	    cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			M_strSQLQRY = "Update FG_RMMST set ";
			M_strSQLQRY += "RM_REMDS = '"+strREMDS+"',";
			M_strSQLQRY += "RM_TRNFL = '0',";
			M_strSQLQRY += "RM_STSFL = '1',";
			M_strSQLQRY += "RM_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "RM_LUPDT = "+"'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and RM_TRNTP = '"+strTRNTP+"'";
			M_strSQLQRY += " and RM_DOCTP = '"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"'";
			M_strSQLQRY += " and RM_DOCNO = '"+strTRFNO+"'";
			//System.out.println(" updRMMST : " + M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updRMMST");
		}
	}
	
	/* method for inserting data into remark master */
	private void insRMMST()
	{
	    cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			M_strSQLQRY = "Insert into FG_RMMST(RM_CMPCD,RM_WRHTP,RM_TRNTP,RM_DOCTP,RM_DOCNO,";
			M_strSQLQRY += "RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT) values (";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+strWRHTP+"',";
			M_strSQLQRY += "'"+strTRNTP+"',";
			M_strSQLQRY += "'"+getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2)+"',";
			M_strSQLQRY += "'"+strTRFNO+"',";
			M_strSQLQRY += "'"+strREMDS+"',";
			M_strSQLQRY += "'1',";
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+")";
			//System.out.println(" insRMMST : " + M_strSQLQRY);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insRMMST");
		}
	}
	
	private void autRECPT()
	{
		try
		{
            cl_dat.M_flgLCUPD_pbst = true;
			setMSG("Authorizing Stock Master.",'N');
			saveSTMST_RCT();
			//autSTMST();//Stock Master update
			setMSG("Authorizing Location Master.",'N');
			autLCMST();//Location Master update
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"autRECPT");
		}
	}


	
		/**

	 */
	private void saveSTMST_RCT()
	{
		try
		{
			    for(int i=0;i<=(tblRCTDTL.getRowCount()-1);i++)
			    {
					if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).toString().trim().equals("true"))
						if(tblRCTDTL.getValueAt(i,intRCT_MNLCD).toString().trim().length()>2)
							saveSTMST_RCT1(i);
						else
							break;
				}
				setDOSMSG();

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"saveSTMST_RCT");
		}
	}
	
	
	
	/**

	 */
	private void saveSTMST_RCT1(int LP_ROW)
	{
		try
		{
			if(!cl_dat.M_flgLCUPD_pbst)

				return;

			//System.out.println("Row No. : "+LP_ROW);

			//System.out.println("MNLCD : "+tblRCTDTL.getValueAt(LP_ROW,intRCT_MNLCD).toString().trim());

			//System.out.println("PKGTP : "+tblRCTDTL.getValueAt(LP_ROW,intRCT_PKGTP).toString().trim());

			strMNLCD = tblRCTDTL.getValueAt(LP_ROW,intRCT_MNLCD).toString().trim();
			strPKGTP_RCT = tblRCTDTL.getValueAt(LP_ROW,intRCT_PKGTP).toString().trim();
			strTRFQT_RCT = tblRCTDTL.getValueAt(LP_ROW,intRCT_RCTQT).toString().trim();
			strRCTPK = tblRCTDTL.getValueAt(LP_ROW,intRCT_RCTPK).toString().trim();
			strSHFCD = tblRCTDTL.getValueAt(LP_ROW,intRCT_SHFCD).toString().trim();
			strREMDS_STK = tblRCTDTL.getValueAt(LP_ROW,intRCT_REMDS).toString().trim();
			strPKGCT = getCODVL("SYSFGXXPKG"+strPKGTP_RCT,cl_dat.M_intCCSVL_pbst);
			strPKGWT = getCODVL("SYSFGXXPKG"+strPKGTP_RCT,cl_dat.M_intNCSVL_pbst);
			//System.out.println("getMAXDT ("+strWRHTP +" / "+ txtPRDTP.getText() +" / "+ getSUBSTR(txtLOTNO.getText().toString().trim(),0,8)+" / "+ getSUBSTR(txtRCLNO.getText().toString().trim(),0,2)+")");
			strRCTDT = getMAXDT(strWRHTP, getSUBSTR(txtPRDTP.getText().toString().trim(),0,2), getSUBSTR(txtLOTNO.getText().toString().trim(),0,8), getSUBSTR(txtRCLNO.getText().toString().trim(),0,2));
			strWHRSTR = " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"'"
            + " and ST_PRDTP = '"+txtPRDTP.getText().toString().trim()+"'"
            + " and ST_LOTNO = '"+txtLOTNO.getText().toString().trim()+"'"
            + " and ST_RCLNO = '"+txtRCLNO.getText().toString().trim()+"'"
            + " and ST_PKGTP = '"+strPKGTP_RCT+"'"
            + " and ST_MNLCD = '"+strMNLCD+"'";

			flgCHK_EXIST =  chkEXIST("FG_STMST", strWHRSTR);

			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst) && flgCHK_EXIST)

			{

					JOptionPane.showMessageDialog(this,"Record alreay exists in FG_STMST");

					//return;

			}

		inlTBLEDIT(tblRCTDTL);

		if(!flgCHK_EXIST)

		{

			M_strSQLQRY= "Insert into FG_STMST(ST_CMPCD,ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_MNLCD,ST_STKQT,ST_ALOQT,ST_UCLQT,ST_DOSQT,ST_DOUQT,ST_PKGTP,ST_PKGWT,ST_PKGCT,ST_RCTDT,ST_STSFL,ST_REMDS,ST_TRNFL,ST_TPRCD,ST_CPRCD,ST_PRDCD,ST_LUPDT,ST_LUSBY) values (" 

			+setINSSTR("ST_CMPCD",cl_dat.M_strCMPCD_pbst,"C")

			+setINSSTR("ST_WRHTP",strWRHTP,"C")

			+setINSSTR("ST_PRDTP",getSUBSTR(txtPRDTP.getText().toString().trim(),0,2),"C")

			+setINSSTR("ST_LOTNO",getSUBSTR(txtLOTNO.getText().toString().trim(),0,8),"C")

			+setINSSTR("ST_RCLNO",getSUBSTR(txtRCLNO.getText().toString().trim(),0,2),"C")

			+setINSSTR("ST_MNLCD",strMNLCD,"C")

			+setINSSTR("ST_STKQT",(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U") ? "0.000" : strTRFQT_RCT),"N")

			+setINSSTR("ST_ALOQT","0.000","N")

		    +setINSSTR("ST_UCLQT",(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U") ? strTRFQT_RCT : "0.000"),"N")

			+setINSSTR("ST_DOSQT","0.000","N")

			+setINSSTR("ST_DOUQT","0.000","N")

			+setINSSTR("ST_PKGTP",strPKGTP_RCT,"C")

			+setINSSTR("ST_PKGWT",strPKGWT,"N")

			+setINSSTR("ST_PKGCT",strPKGCT,"C")

			+setINSSTR("ST_RCTDT",strRCTDT,"D")

			+setINSSTR("ST_STSFL",(rdbFRSSTK.isSelected() ? "1" : "2" ),"C")

			+setINSSTR("ST_REMDS",strREMDS_STK,"C")

			+setINSSTR("ST_TRNFL","0","C")

			+setINSSTR("ST_TPRCD",strTPRCD,"C")

			+setINSSTR("ST_CPRCD",strCPRCD,"C")

			+setINSSTR("ST_PRDCD",strPRDCD,"C")

			+setINSSTR("ST_LUPDT",cl_dat.M_strLOGDT_pbst,"D")

			+"'"+cl_dat.M_strUSRCD_pbst+"')";  //setINSSTR("ST_LUSBY,cl_dat.M_strUSRCD_pbst,"C")

		}

		

		

		else if(flgCHK_EXIST)

		{

			M_strSQLQRY = "update FG_STMST set "

			 +setUPDSTR((cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U") ? "ST_UCLQT" : "ST_STKQT" ),(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U") ? "ST_UCLQT+" : "ST_STKQT+" )+strTRFQT_RCT,"N")

			 +setUPDSTR("ST_REMDS",strREMDS,"C")

			 +setUPDSTR("ST_TRNFL","0","C")

			 +setUPDSTR("ST_LUPDT",cl_dat.M_strLOGDT_pbst,"D")

			 +" ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+strWHRSTR;

		}

		//System.out.println(M_strSQLQRY);

		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"saveSTMST_RCT1");
		}
	}
	
/*****************************************************************

 */	
		/**

	 */
	private void saveSTMST_ISS()
	{
		try
		{
			    for(int i=0;i<=(tblISSDTL.getRowCount()-1);i++)
			    {
					if(!tblISSDTL.getValueAt(i,intRCT_CHKFL).toString().trim().equals("true"))
						continue;
					saveSTMST_ISS1(i);
				}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"saveSTMST_RCT");
		}
	}
	
	
	
	/**

	 */
	private void saveSTMST_ISS1(int LP_ROW)
	{
		try
		{
			if(!cl_dat.M_flgLCUPD_pbst)

				return;

			

			strMNLCD = tblISSDTL.getValueAt(LP_ROW,intIST_MNLCD).toString().trim();
			strPKGTP_ISS = tblISSDTL.getValueAt(LP_ROW,intIST_PKGTP).toString().trim();
			strTRFQT_ISS = setNumberFormat(Double.parseDouble(tblISSDTL.getValueAt(LP_ROW,intIST_ISSQT).toString().trim()),3);
			strISPKG = tblISSDTL.getValueAt(LP_ROW,intIST_ISSPK).toString().trim();
			strPKGCT = getCODVL("SYSFGXXPKG"+strPKGTP_ISS,cl_dat.M_intCCSVL_pbst);
			strPKGWT = getCODVL("SYSFGXXPKG"+strPKGTP_ISS,cl_dat.M_intNCSVL_pbst);
			strPRDTP = getPRDTP();
			

			
			strWHRSTR = " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+getSUBSTR(cmbWRHTP.getSelectedItem().toString().trim(),0,2)+"'"
			+ " and ST_PRDTP = '"+txtPRDTP.getText().toString().trim()+"'"
			+ " and ST_LOTNO = '"+txtLOTNO.getText().toString().trim()+"'"
			+ " and ST_RCLNO = '"+txtRCLNO.getText().toString().trim()+"'"
			+ " and ST_PKGTP = '"+strPKGTP_ISS+"'"
			+ " and ST_MNLCD = '"+strMNLCD+"'";

			flgCHK_EXIST =  chkEXIST("FG_STMST", strWHRSTR);

			inlTBLEDIT(tblRCTDTL);

			if(!flgCHK_EXIST)

			{

					setMSG("Record not found in FG_STMST for "+ getSUBSTR(cmbWRHTP.getSelectedItem().toString().trim(),0,2) + " / " + txtPRDTP.getText().toString().trim()+ " / "+ txtLOTNO.getText().toString().trim()+ " / "+ txtRCLNO.getText().toString().trim() + " / "+ strPKGTP_ISS + " / "+ strMNLCD,'E');

					return;

			}

			else if(flgCHK_EXIST)

			{

				M_strSQLQRY = "update FG_STMST set "

				 +setUPDSTR((cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U") ? "ST_UCLQT" : "ST_STKQT" ),(cmbCLSTG.getSelectedItem().toString().substring(0,1).trim().equalsIgnoreCase("U") ? "ST_UCLQT-" : "ST_STKQT-" )+strTRFQT_ISS,"N")

				 +setUPDSTR("ST_REMDS",strREMDS,"C")

				 +setUPDSTR("ST_TRNFL","0","C")

				 +setUPDSTR("ST_LUPDT",cl_dat.M_strLOGDT_pbst,"D")

				 +" ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"' where "+strWHRSTR;

			}

			//System.out.println(M_strSQLQRY);

			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"saveSTMST_ISS1");
		}
	}
	
	public String getMAXDT(String LP_WRHTP, String LP_PRDTP, String LP_LOTNO, String LP_RCLNO)
	{
		String L_RTNSTR = "";
		try
		{
			M_strSQLQRY = " Select isnull(max(RCT_RCTDT),'01/01/2001') L_RCTDT from FG_RCTRN where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_wrhtp='"+LP_WRHTP+"'";
			M_strSQLQRY += " and rct_prdtp='"+LP_PRDTP+"' and rct_lotno='"+LP_LOTNO+"' and rct_rclno='"+LP_RCLNO+"'";
			M_strSQLQRY += " and rct_rcttp in ('10','15') and rct_stsfl not in 'X'";
			//System.out.println("M_strSQLQRY =" + M_strSQLQRY);
			L_rstCTRLST = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstCTRLST.next())
			{
			  	L_RTNSTR = nvlSTRVL(M_fmtLCDAT.format(L_rstCTRLST.getDate("L_RCTDT")).toString(),"");
			}
			L_rstCTRLST.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMAXDT");
		}
		return L_RTNSTR;
	}
	
	
	private	void setDOSMSG()
	{
		setMSG("Updation Completed in "+strFILNM,'N');
	}
	
	private	void setERRMSG()
	{
		setMSG("Updation Failed in "+strFILNM,'E');
		cl_dat.M_flgLCUPD_pbst = false;
	}
	
	private void dspEXPMSG(Exception LP_EX)
	{
			//System.out.println(LP_EX);
			cl_dat.M_flgLCUPD_pbst = false;
			LM_OPTNPN.showMessageDialog(this,"Exception : "+LP_EX.toString()+" in "+strFILNM+" Table.Contact Systems Dept.","Data Transfer Status",JOptionPane.ERROR_MESSAGE);
	}
		

	/**

	 */
	private boolean getRCTVAL(int i)
	{
		try
		{
			if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).toString().trim().equals("true"))
			{
			    strMNLCD = tblRCTDTL.getValueAt(i,intRCT_MNLCD).toString().trim();
				strPKGTP_RCT = tblRCTDTL.getValueAt(i,intRCT_PKGTP).toString().trim();
				strTRFQT_RCT = tblRCTDTL.getValueAt(i,intRCT_RCTQT).toString().trim();
				strRCTPK = tblRCTDTL.getValueAt(i,intRCT_RCTPK).toString().trim();
				strSHFCD = tblRCTDTL.getValueAt(i,intRCT_SHFCD).toString().trim();
				strPKGCT = getCODVL("SYSFGXXPKG"+strPKGTP_RCT,cl_dat.M_intCCSVL_pbst);
				strPKGWT = getCODVL("SYSFGXXPKG"+strPKGTP_RCT,cl_dat.M_intNCSVL_pbst);
				return true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRCTVAL");
		}
		return false;
	}
	
	
	/**

	 */
	private boolean getISSVAL(int i)
	{
		try
		{
			if(tblISSDTL.getValueAt(i,intIST_CHKFL).toString().trim().equals("true"))
			{	
			   //System.out.println("In getISSVAL");
				strMNLCD = tblISSDTL.getValueAt(i,intIST_MNLCD).toString().trim();
				strPKGTP_ISS = tblISSDTL.getValueAt(i,intIST_PKGTP).toString().trim();
				strTRFQT_ISS = setNumberFormat(Double.parseDouble(tblISSDTL.getValueAt(i,intIST_ISSQT).toString().trim()),3);
				strISPKG = tblISSDTL.getValueAt(i,intIST_ISSPK).toString().trim();
				strPKGCT = getCODVL("SYSFGXXPKG"+strPKGTP_ISS,cl_dat.M_intCCSVL_pbst);
				strPKGWT = getCODVL("SYSFGXXPKG"+strPKGTP_ISS,cl_dat.M_intNCSVL_pbst);
				strPRDTP = getPRDTP();
				//System.out.println("strPRDTP =" + strPRDTP);
				//System.out.println("Out of getISSVAL");
				return true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getISSVAL");
		}
		return false;
	}
	
	/** Method to update Location  Master */
	private void autLCMST()
	{
	     strFILNM = "FG_LCMST";
	     cl_dat.M_flgLCUPD_pbst = true;
		 if(cl_dat.M_flgLCUPD_pbst)
		 {
			ResultSet L_rstRSSET = null;
		    try
		    {
			    for(int i=0;i<=(tblRCTDTL.getRowCount()-1);i++)
			    {
				    if(getRCTVAL(i))
				    {
					    L_strSTLSQL = "Select isnull(count(*),0) LC_CTR from fg_lcmst";
					    L_strSTLSQL += " where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lc_wrhtp = '"+strWRHTP+"'";
					    L_strSTLSQL += " and lc_mnlcd = '"+strMNLCD+"'";
					    L_rstRSSET = cl_dat.exeSQLQRY3(L_strSTLSQL);
					    if(L_rstRSSET != null && L_rstRSSET.next())
					       if(Integer.parseInt(L_rstRSSET.getString("LC_CTR").toString())>0)
						    updLCMST1();
					    else
						    setERRMSG();
					    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					    //System.out.println("value in autLCMST = " + cl_dat.M_flgLCUPD_pbst);
					}
				}
				setDOSMSG();
		    }
		    catch(Exception L_EX)
		    {
			dspEXPMSG(L_EX);
			}
		}
	}
	
	private void updLCMST1()
	{
	     cl_dat.M_flgLCUPD_pbst = true;
		try
		{
			M_strSQLQRY = "Update FG_LCMST set ";
			M_strSQLQRY += "LC_STKQT = LC_STKQT + "+strTRFQT_RCT+",";
			M_strSQLQRY += "LC_TRNFL = '0',";
			M_strSQLQRY += "LC_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "LC_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where LC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND lc_wrhtp = '"+strWRHTP+"'";
			M_strSQLQRY += " and lc_mnlcd = '"+strMNLCD+"'";
			//System.out.println("cl_dat.M_flgLCUPD_pbst in updLCMST1 =" + cl_dat.M_flgLCUPD_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updLCMST");   
		}
	}
	
	/* Updates Generated Issue No.*/
	 
	private void updGENNO()
	{ 
	   cl_dat.M_flgLCUPD_pbst = true;
	   try
	   {
	    	strCODCD = strYRDGT + getSUBSTR(cmbTRFTP.getSelectedItem().toString().trim(),0,2);
	    	M_strSQLQRY = "Update CO_CDTRN SET ";
		    M_strSQLQRY += "CMT_CCSVL = '"+strUPDNO+"'";
		    M_strSQLQRY += " where cmt_cgmtp = 'D"+cl_dat.M_strCMPCD_pbst+"'";
		    M_strSQLQRY += " and cmt_cgstp = 'FGXXISS'";
		    M_strSQLQRY += " and cmt_codcd = '"+strCODCD+"'";
		    cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	   }
	   catch(Exception L_EX)
	   {
		   setMSG(L_EX,"updGENNO");
	   }
	}
	
	/** Initializing table editing before poulating/capturing data  */
    private void inlTBLEDIT(JTable P_tblTBLNM)
    {
    	if(!P_tblTBLNM.isEditing())
    		return;
    	P_tblTBLNM.getCellEditor().stopCellEditing();
    	P_tblTBLNM.setRowSelectionInterval(0,0);
    	P_tblTBLNM.setColumnSelectionInterval(0,0);
    			
    }

	
	/** Displaying total issue quantity in text box

	 */
	private void setTOTQT_ISS()
	{
	    double dblISSTOT = 0.000;
		try
		{
    		for(int i=0;i<tblISSDTL.getRowCount();i++)
    		{
        	    if(tblISSDTL.getValueAt(i,intIST_CHKFL).toString().equals("true"))
                {
                   if(tblISSDTL.getValueAt(i,intIST_MNLCD).toString().length()==0)
                        break;
                   String L_strTEMP1 = nvlSTRVL(tblISSDTL.getValueAt(i,intIST_ISSQT).toString(),"");
                   dblISSTOT += Double.parseDouble(L_strTEMP1);
                }
    		}
    		txtTOISS.setText(setNumberFormat(dblISSTOT,3));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setTOTQT_ISS");
		}
	}


	/** Displaying total receipt quantity in text box

	 */
	private void setTOTQT_RCT()
	{
	    double dblRCTTOT = 0.000;
		try
		{
    		for(int i=0;i<tblRCTDTL.getRowCount();i++)
    		{
        	    if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).toString().equals("true"))
                {
                   if(tblRCTDTL.getValueAt(i,intIST_MNLCD).toString().length()==0)
                        break;
                   String L_strTEMP1 = nvlSTRVL(tblRCTDTL.getValueAt(i,intRCT_RCTQT).toString(),"");
                   dblRCTTOT += Double.parseDouble(L_strTEMP1);
                }
    		}
    		txtTORCT.setText(setNumberFormat(dblRCTTOT,3));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setTOTQT_RCT");
		}
	}
	
	
	

 private double getRCTTOT(int LP_ROWNO)
 {
    double L_dblTOTRCT = 0.000;
    try
    {
        for(int i=0;i<LP_ROWNO;i++)
        {
          String L_QTY = "";  
          if(tblRCTDTL.getValueAt(i,intRCT_CHKFL).toString().equals("true"))
          {
            L_dblTOTRCT += Double.parseDouble(tblRCTDTL.getValueAt(i,intRCT_RCTQT).toString());
          }
        }
    }

	catch(Exception L_EX)
	{
		setMSG(L_EX,"getRCTTOT");
	}
    return L_dblTOTRCT;
 }
 
 private double getISSTOT(int LP_ROWNO)
 {
    double L_dblTOTISS = 0.000;
    try
    {
        for(int i=0;i<LP_ROWNO;i++)
        {
          String L_QTY = "";  
          if(tblISSDTL.getValueAt(i,intIST_CHKFL).toString().equals("true"))
          {
            //System.out.println("IN FOR LOOP");
            //L_QTY = nvlSTRVL(tblRCTDTL.getValueAt(i,intRCT_RCTQT).toString(),"");
            L_dblTOTISS += Double.parseDouble(tblISSDTL.getValueAt(i,intIST_ISSQT).toString());
            //String strRCT=((JTextField)tblRCTDTL.cmpEDITR[intRCT_RCTQT]).getText().toString().trim();
          }
        }
    }
	catch(Exception L_EX)
	{
		setMSG(L_EX,"getISSTOT");
	}
    return L_dblTOTISS;
 }

	
	/** Generating string for Insertion Query

	 * @param	LP_FLDNM	Field name to be inserted

	 * @param	LP_FLDVL	Content / value of the field to be inserted

	 * @param	LP_FLDTP	Type of the field to be inserted

	 */

	private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {

	try {

		//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);

		if (LP_FLDTP.equals("C"))

			 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";

	 	else if (LP_FLDTP.equals("N"))

	         return   nvlSTRVL(LP_FLDVL,"0") + ",";

	 	else if (LP_FLDTP.equals("D"))

			 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";

		else if (LP_FLDTP.equals("T"))

			 return   (LP_FLDVL.length()>10) ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";

		else return " ";

	        }

	    catch (Exception L_EX) 

		{setMSG("Error in setINSSTR : "+L_EX,'E');}

	return " ";

	}

		







	/** Generating string for Updation Query

	 * @param	LP_FLDNM	Field name to be inserted

	 * @param	LP_FLDVL	Content / value of the field to be inserted

	 * @param	LP_FLDTP	Type of the field to be inserted

	 */

	private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) 

	{

		try 

		{

			//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);

			if (LP_FLDTP.equals("C"))

				 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");

		 	else if (LP_FLDTP.equals("N"))

		         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");

		 	else if (LP_FLDTP.equals("D"))

				 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));

			else if (LP_FLDTP.equals("T"))

				 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtLCDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));

			else return " ";

		}

		catch (Exception L_EX) 

			{setMSG("Error in setUPDSTR : "+L_EX,'E');}

		return " ";

	}

    /** Method for returning values from Result Set

	 * <br> with respective verifications against various data types

	 * @param	LP_RSLSET		Result set name

	 * @param       LP_FLDNM                Name of the field for which data is to be extracted

	 * @param	LP_FLDTP		Data Type of the field

	 */

	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 

	{

		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);

	    try

	    {

		if (LP_FLDTP.equals("C"))

			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";

			//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";

		else if (LP_FLDTP.equals("N"))

			return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";

		else if (LP_FLDTP.equals("D"))

			return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";

		else if (LP_FLDTP.equals("T"))

		    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));

		else 

			return " ";

		}

		catch (Exception L_EX)

		{setMSG(L_EX,"getRSTVAL");}

	return " ";

	} 



	/** Checking key in table for record existance

	 */

	private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)

	{

		boolean L_flgCHKFL = false;

		try

		{

			M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;

			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);

			if (L_rstRSSET != null && L_rstRSSET.next())

			{

				L_flgCHKFL = true;

				L_rstRSSET.close();

			}

		}

		catch (Exception L_EX)	

		{setMSG("Error in chkEXIST : "+L_EX,'E');}

		return L_flgCHKFL;

	}

	

	
	
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			int intTOT_SELCTD=0;
			try
			{
				setMSG("",'N');

				if(((JCheckBox) tblISSDTL.cmpEDITR[intIST_CHKFL]).isSelected())

				{
					//strSELECTED="";
					if(P_intCOLID == intIST_CHKFL)
					{
						for(int i = 0;i < tblISSDTL.getRowCount();i++)           
						{
							if(tblISSDTL.getValueAt(i,intIST_CHKFL).toString().equals("true"))
							{	
								intTOT_SELCTD++;
								//System.out.println("intTOT_SELCTD"+intTOT_SELCTD);	
								if(intTOT_SELCTD == 1)
									strSELECTED = tblISSDTL.getValueAt(i,intIST_REMDS).toString();
								else if(intTOT_SELCTD == 0)
									strSELECTED = "";
								if(!tblISSDTL.getValueAt(i,intIST_REMDS).toString().equals(strSELECTED))
								{
									setMSG("Selected Records Must Have Same Remark..",'E');
									tblISSDTL.setValueAt(new Boolean(false),tblISSDTL.getSelectedRow(),intIST_CHKFL);
									return false;
								}	
							}	
						}	
						if(tblISSDTL.getValueAt(P_intROWID,intIST_CHKFL).equals(new Boolean(true)))
							setTOTQT_ISS();
						return true;
					}					
				}	
				if(getSource() == tblISSDTL)
			    {
    			    if(P_intCOLID == intIST_ISSQT)
			        {
			            
			            String strTEMPQT = ((JTextField)tblISSDTL.cmpEDITR[intIST_ISSQT]).getText().toString().trim();
			            if(vldISSQT(txtPRDTP.getText(),txtLOTNO.getText(),txtRCLNO.getText(),tblISSDTL.getValueAt(P_intROWID,intIST_MNLCD).toString(),tblISSDTL.getValueAt(P_intROWID,intIST_PKGTP).toString(),strTEMPQT))
    					{		
    					    String L_strPKGTP = tblISSDTL.getValueAt(P_intROWID,intIST_PKGTP).toString();
    						String L_strPKG = calTRPKG(L_strPKGTP,strTEMPQT);
    						if(Double.parseDouble(L_strPKG) != Math.round(Double.parseDouble(L_strPKG)))
    						{
    						    setMSG("Qty. not in multiple of Pkg.Wt.",'E');
								return false;
    						}
							tblISSDTL.setValueAt(new Boolean(true),tblISSDTL.getSelectedRow(),intIST_CHKFL);
							setTOTQT_ISS();
    						tblISSDTL.setValueAt(setNumberFormat(Double.parseDouble(L_strPKG),0),tblISSDTL.getSelectedRow(),intIST_ISSPK);
							return true;
    					}
    					else
							return false;
			        }
			    }
				if(getSource()==tblRCTDTL)
				{   
				    if(P_intCOLID==intRCT_CHKFL)
						return true;
				    if(P_intCOLID==intRCT_MNLCD)
					{ 
						if(vldRCMNL())
						{
						    setMSG("",'N');
						    
							
							tblRCTDTL.setValueAt(new Boolean(true),tblRCTDTL.getSelectedRow(),intRCT_CHKFL);
							if(tblRCTDTL.getSelectedRow() == 0)
							{
								dblTOISQT = calISSQT();
								dblTORCQT = 0;
							}
							else
							{
								dblTOISQT = calISSQT();
								dblTORCQT = calRCTQT();
							}
							tblRCTDTL.setValueAt(new Boolean(true),tblRCTDTL.getSelectedRow(),intRCT_CHKFL);
							double L_dblRUNQT = (Double.parseDouble(txtTOISS.getText().trim())-getRCTTOT(P_intROWID));
							//System.out.println(" TOTISS : "+Double.parseDouble(txtTOISS.getText().trim()));
							//System.out.println("getRCTTOT(P_intROWID) : "+getRCTTOT(P_intROWID));
							//System.out.println(" "+L_dblRUNQT);
							if(tblRCTDTL.getValueAt(tblRCTDTL.getSelectedRow(),intRCT_RCTQT).toString().length() == 0)
								tblRCTDTL.setValueAt(setNumberFormat(L_dblRUNQT,3),tblRCTDTL.getSelectedRow(),intRCT_RCTQT);
							double dblTOTAL = Double.parseDouble(txtTOISS.getText().toString().trim());
												
							//double dblBALQTY = dblTOTAL - dblQTY ;
							return true;
											
						}
						else
						{
						    setMSG("Please enter valid location.",'E');
							return false;
						}
					}
					
					if(P_intCOLID == intRCT_PKGTP)
                    {
						////method that checks whether that record present in FG_STMST or not.
						////if present remark must be same as in Issue table.
						if(!chkREMEXIST(P_intROWID,P_intCOLID))
						{	
							setMSG("Material With Different Remark already Exist at Location..",'E');
							return false;
						}	
						else
						{	
							System.out.println("strSELECTED>>"+strSELECTED);
							tblRCTDTL.setValueAt(strSELECTED,P_intROWID,intRCT_REMDS);
						}	
						/////////////////////////////////////////////////////////////////
						
						
						if(tblRCTDTL.getValueAt(P_intROWID,intRCT_CHKFL).equals(new Boolean(false)))
							if(((JTextField)tblRCTDTL.cmpEDITR[intRCT_PKGTP]).getText().toString().trim().length() == 0)
								return true;
		                if(!cl_dat.M_hstMKTCD_pbst.containsKey("SYSFGXXPKG"+((JTextField)tblRCTDTL.cmpEDITR[intRCT_PKGTP]).getText().toString().trim()))
						{
							setMSG("Invalid Package Type. : "+((JTextField)tblRCTDTL.cmpEDITR[intRCT_PKGTP]).getText().toString().trim(),'E');
							return false;
						}
						setMSG(" ",'N');
						tblRCTDTL.setValueAt(new Boolean(true),tblRCTDTL.getSelectedRow(),intRCT_CHKFL);
						return true;
                    }

					if(P_intCOLID == intRCT_RCTQT)
					{ 

						//tblRCTDTL.editCellAt(tblRCTDTL.getSelectedRow(),intRCT_RCTQT);
						String L_strRCTQT = ((JTextField)tblRCTDTL.cmpEDITR[intRCT_RCTQT]).getText().toString().trim();
						String L_strPKGTP = tblRCTDTL.getValueAt(tblRCTDTL.getSelectedRow(),intRCT_PKGTP).toString().trim();
						if(!vldTRFQT(Double.parseDouble(L_strRCTQT),L_strPKGTP))
						{
							return false;
						}
						dblTORCQT = calRCTQT();
						dblTOISQT = calISSQT();
						if(dblTORCQT > dblTOISQT)
							{setMSG("Total Receipt :"+setNumberFormat(dblTORCQT,3) +" Exceeds Issue Qty : "+setNumberFormat(dblTOISQT,3),'E');return false;}
						String L_strRCTPK = calTRPKG(L_strPKGTP,L_strRCTQT);
    					tblRCTDTL.setValueAt(setNumberFormat(Double.parseDouble(L_strRCTPK),0),tblRCTDTL.getSelectedRow(),intRCT_RCTPK);
						tblRCTDTL.setValueAt(new Boolean(true),tblRCTDTL.getSelectedRow(),intRCT_CHKFL);
						setTOTQT_RCT();
						return true;
					}
						
					
                    if(P_intCOLID==intRCT_RCTPK)
                    {
						if(!vldTRFPK(Double.parseDouble(tblRCTDTL.getValueAt(tblRCTDTL.getSelectedRow(),intRCT_RCTQT).toString().trim()),Double.parseDouble(tblRCTDTL.getValueAt(tblRCTDTL.getSelectedRow(),intRCT_RCTPK).toString().trim()),tblRCTDTL.getValueAt(tblRCTDTL.getSelectedRow(),intRCT_PKGTP).toString().trim()))
							return false;
						setMSG("",'N');
						return true;
                    }
                    if(P_intCOLID==intRCT_SHFCD)
                    {
                        tblRCTDTL.editCellAt(tblRCTDTL.getSelectedRow(),intRCT_SHFCD);
						if(tblRCTDTL.getValueAt(P_intROWID,intRCT_CHKFL).equals(new Boolean(false)))
							if(tblRCTDTL.getValueAt(P_intROWID,intRCT_SHFCD).toString().trim().length() == 0)
								return true;
						
                        if(!vldSHFCD(tblRCTDTL.getValueAt(P_intROWID,intRCT_SHFCD).toString().trim()))
							return false;
							
						setMSG("",'N');
						return true;
						
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"TBLINPVF");
			}
			return true;
		}
		
	}// end of class TBLINPVF
	
	private boolean chkREMEXIST(int P_intROWID,int P_intCOLID)
	{
		try
		{
			////remark validation while warehouse transfer.
			M_strSQLQRY = " select (isnull(ST_STKQT,0)-isnull(ST_ALOQT,0)) ST_STKQT,ST_REMDS from FG_STMST where";
			M_strSQLQRY +=" ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+getSUBSTR(cmbWRHTP.getSelectedItem().toString().trim(),0,2)+"'"
				+ " and ST_PRDTP = '"+txtPRDTP.getText().toString().trim()+"'"
				+ " and ST_LOTNO = '"+txtLOTNO.getText().toString().trim()+"'"
				+ " and ST_RCLNO = '"+txtRCLNO.getText().toString().trim()+"'"
				+ " and ST_PKGTP = '"+((JTextField)tblRCTDTL.cmpEDITR[intRCT_PKGTP]).getText().toString().trim()+"'"
				+ " and ST_MNLCD = '"+tblRCTDTL.getValueAt(P_intROWID,intRCT_MNLCD).toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println("M_strSQLQRY"+M_strSQLQRY);
			if(M_rstRSSET != null && M_rstRSSET.next())
			{
				System.out.println("v1>>>"+M_rstRSSET.getString("ST_REMDS"));
				System.out.println("v2>>>"+tblISSDTL.getValueAt(tblISSDTL.getSelectedRow(),intIST_REMDS).toString().trim());
				if(M_rstRSSET.getFloat("ST_STKQT")==0)
					return true;
				if(M_rstRSSET.getString("ST_REMDS").equals(strSELECTED))
					return true;
				else if(!M_rstRSSET.getString("ST_REMDS").equals(strSELECTED))
					return false;
			}
			else 
				return true;
			//////////////////////////////////////////////////////
		}
		catch(Exception E)
		{
			System.out.println("Error in chkEXIST "+E);
		}	
		return true;
	}
	
}	//end of class        
