/*
System Name   : Finished Goods Inventory Management System
Program Name  : Down Grading Entry Form.
Program Desc. : Entry provision for PSFS/SCRAP Classification.
Author        : Mr. Deepal N. Mehta
Date          : 20th June 2002
Version       : FIMS 2.0

Modificaitons 

Modified By    : Mr.Deepal N.Mehta
Modified Date  : 09/11/2002
Modified det.  : Provision for Allocation & Authorization
Version        :

*/
/**
 * Year End Operation:
 * When year changes i.e. during January, Change year digit in Down Grading Lot Series
 * Down grading Lot Nos. (These are Open Lots) are available in CO_CDTRN for SYS/FGXXDGR/DGR_...  in CMT_CCSVL field
 */
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.math.BigDecimal;
import javax.swing.JCheckBox;
import javax.swing.undo.*;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.ResultSet;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Cursor;


public class fg_tedgr extends cl_pbase 
{
	
	JComboBox cmbWRHTP;
	JLabel lblISSU;
	ButtonGroup grpCHKBTN;
	JRadioButton rdbDGRALO,rdbDGRAUT;
	JTextField txtPRDTP,txtDGRDT,txtLOTNO,txtDGRCT,txtTOTQT,txtTOTPK,txtDGRNO,txtRCLNO1,txtLOC1,txtPCT1,txtQTY1;
	TxtLimit txtLOTNO1;
	JOptionPane LM_OPTNPN;
	JCheckBox chkPRINT;
	//JTable tblDGRTBL;
	JLabel lblTBLHDR;
	cl_JTable tblDGRTBL;
	
	int intTB_CHKFL = 0;
	int intTB_LOTNO = 1;
	int intTB_RCLNO = 2;
	int intTB_MNLCD = 3;
    int intTB_PKGTP = 4;
    int intTB_ISSQT = 5;
    int intTB_ISSPK = 6;
	private int intCNTRW =0;
	JCheckBox chkCHKFL1;
	
	int LM_CNT = 0,LM_UPDCTR = 0;
	boolean flgRTNFL=false;
	ResultSet M_rstRSSET;
	String strACTTXT,strPRDDS,strLOTNO,strRCLNO,strWRHTP,strPRDCD,strPRDTP,strCODCD="",strSTREM,strSTKQT,strCALPK,strRCPTP,strRCTPK,strPKGCT,strALOQT,strISREC,strREFDT;
	String strMNLCD,strPKGTP,strHLPFLD,strDGRNO,strDGRDT,strREMDS,strRCLOT,strDGRCT,strCCSVL,strLTREM,strCHP01,strCHP02,strRCTQT,strPKGWT,strSTSFL,strUPDQT;
	String LM_SQLSTR = "";
	String strUPDNO = "";
	String strDGRTP = "61";
	String strYRDGT = cl_dat.M_strFNNYR1_pbst.substring(3,4);
	//String LM_RESFIN = cl_dat.M_REPSTR;
	//String LM_RESSTR = LM_RESFIN.trim().concat("\\fg_rpdgr.doc"); 
	String strTBLLOTNO,strTBLRCLNO,strTBLMNLCD,strTBLPKGTP,strTBLSTKQT;				
	private TxtDate txtGRDDT;
	String AR_WRHTP[];
	String AR_ORGQT[];
	String AR_CLSFL[];
	String AR_PRDCD[];
	String AR_OPRCD[];
	String AR_PKGTP[];
	String AR_PRDTP[];
	
	//int LM_COUNT = 0;
        //int LM_ROWCNT = 100;  // Lot Selection allowed
	//int LM_STRLEN,LM_VALSTR;//to calculate the length of a string in getSTRTKN()
	int intRECCNT = 0;
	
	double dblDBSQT = 0;
	double dblTOTLTQT = 0;
	double dblDSTQT = 0;
	//double dblDSTQT = 0;
	double dblDUPQT = 0;
	private JPanel pnlRETCP; 
	double dblDALQT = 0.0;
	
	boolean L_NUMFL = true;
	boolean L_RETFL = true; 			
	boolean L_ERRENT = false; 	
	boolean flgENBL = false;
	
	
	Hashtable<String,String> hstLOTNO,hstRCLNO,hstMNLCD,hstPKGTP,hstSTKQT,hstENTQT;
	
	fg_tedgr()
	{
	         
		super(2);
		try
		{
			setMatrix(20,20);
			hstLOTNO = new Hashtable<String,String>();
			LM_OPTNPN = new JOptionPane();
			hstRCLNO = new Hashtable<String,String>();
			hstMNLCD = new Hashtable<String,String>();
			hstPKGTP = new Hashtable<String,String>();
			hstSTKQT = new Hashtable<String,String>();
			hstENTQT = new Hashtable<String,String>();
			grpCHKBTN = new ButtonGroup();
		
		
		
		add(new JLabel("W/H type"),1,1,1,1.5,this,'L');
		add(cmbWRHTP = new JComboBox(),2,1,1,1.5,this,'L');
		cmbWRHTP.addItem("01");
		
		add(new JLabel("DGR.No"),1,3,1,1.5,this,'L');
	    add(txtDGRNO=new JTextField(),2,3,1,2.3,this,'L');
		
		add(new JLabel("Product Type"),1,6,1,2.5,this,'L');
		add(txtPRDTP=new JTextField(),2,6,1,2,this,'L');
		
		add(new JLabel("Date"),1,9,1,2,this,'L');
		add(txtDGRDT=new TxtDate(),2,9,1,2.2,this,'L');
		
		add(new JLabel("Category"),1,12,1,2,this,'L');
		add(txtDGRCT=new JTextField(),2,12,1,3.5,this,'L');
		
		add(new JLabel("Lot No"),1,17,1,1.5,this,'L');
		add(txtLOTNO=new JTextField(),2,17,1,2.5,this,'L');
		
		add(new JLabel("TotalQty"),3,12,1,2,this,'L');
		add(txtTOTQT=new JTextField(),4,12,1,2.5,this,'L');
		
		add(new JLabel("Total Pkg"),3,17,1,2,this,'L');
		add(txtTOTPK=new JTextField(),4,17,1,2.5,this,'L');
		
		add(lblISSU=new JLabel("Issue Details "),6,1,1,3,this,'L');
		lblISSU.setFont(new Font("Arial",Font.BOLD,15));
		lblISSU.setForeground(Color.black );
		add(rdbDGRALO = new JRadioButton("Allocation",true),4,5,1,2.5,this,'L');
		add(rdbDGRAUT = new JRadioButton("Authorization",false),4,8,1,3,this,'L');
		grpCHKBTN.add(rdbDGRALO);
		grpCHKBTN.add(rdbDGRAUT);
		
		String[] L_staSTKHD = {"Status ","Lot No","Rcl.No","Location","Package Type","Issue Qty","Issue Pkg"};/**This is for create the coloums */
       	int[] L_intCOLSZ = {60,120,100,100,115,100,120};
		   
		tblDGRTBL = crtTBLPNL1(this,L_staSTKHD,50, 7,1,11,19 ,L_intCOLSZ,new int[]{0});

	    tblDGRTBL.addKeyListener(this); 
		tblDGRTBL.setInputVerifier(new TBLINPVF());
		
		tblDGRTBL.setCellEditor(intTB_LOTNO,txtLOTNO1 = new TxtLimit(8));
		tblDGRTBL.setCellEditor(intTB_RCLNO,txtRCLNO1 = new TxtLimit(2));
		rdbDGRAUT.setEnabled(false);
		rdbDGRALO.setEnabled(false);
		txtDGRNO.setEnabled(false);
		txtPRDTP.setEnabled(false);
		txtDGRDT.setEnabled(false);
		txtDGRCT.setEnabled(false);
		txtLOTNO.setEnabled(false); 
		txtTOTQT.setEnabled(false); 
		txtTOTPK.setEnabled(false); 
		tblDGRTBL.setEnabled(false);

	
		txtLOTNO1.addKeyListener(this);
		//getREFDT();
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
			
	}
	/**
	 * @return void and the Components  are Enabled
	 */
	void setENBL(boolean L_flgSTAT)
	{   
         super.setENBL(L_flgSTAT);
		
		 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
         {
             txtDGRDT.setText(cl_dat.M_strLOGDT_pbst);
		
         }
		 txtDGRNO.setEnabled(false);
		txtPRDTP.setEnabled(false);
		txtDGRDT.setEnabled(false);
		txtDGRCT.setEnabled(false);
		txtLOTNO.setEnabled(false); 
		txtTOTQT.setEnabled(false); 
		txtTOTPK.setEnabled(false); 
		
		 
         
	}
	/**Action performed is used for the apply Action events into the  components 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				setENBL(false); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					 L_NUMFL = true;
					 txtDGRNO.setText("");
					 txtPRDTP.setText("");
					 txtDGRCT.setText("");
					 txtLOTNO.setText("");
					 txtTOTQT.setText("");
					 txtTOTPK.setText("");
					 
					 txtDGRDT.setEnabled(false);
					 txtDGRCT.setEnabled(false);
					 txtLOTNO.setEnabled(false); 
					 rdbDGRALO.setSelected(true);
					 M_strSQLQRY = "Select count(*) from fg_rctrn where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp='"+strDGRTP+"'";
					 M_strSQLQRY += " and rct_stsfl = '1'";
					 //System.out.println(M_strSQLQRY);
					 if(cl_dat.getRECCNT(M_strSQLQRY) > 0)	
					 {
        				 
						 //System.out.println("This is getRECEnt Method");
						 txtPRDTP.setEnabled(false);
						 txtDGRNO.setEnabled(true);
						 txtDGRNO.requestFocus();
						 L_NUMFL = false;
					}else
					 {
						txtPRDTP.setEnabled(true);
						txtDGRNO.setEnabled(false);
						txtPRDTP.requestFocus();
						L_NUMFL = true;
					}
				 } 
				
			
					 
					 
				
				 
			}	
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Action Performed");
		}
	}
	/**FocusEvents on the TextFields on Table
	 */
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			
			if(L_FE.getSource().equals(txtDGRNO))
			{
				setMSG("Press F1 to select the Receipt Type ",'N');
			}
			if(L_FE.getSource().equals(txtPRDTP))
			{
				setMSG("Press F1 to Select the Product Type",'N');
			}
			if(L_FE.getSource().equals(txtDGRDT))
			{
                setMSG("Enter Test Date in format dd/mm/yyyy",'N');
			}
			if(M_objSOURC==txtLOTNO1) 
			{
				setMSG("Press F1 to select the Lot No. & then press Enter.",'N');
				if(tblDGRTBL.getSelectedRow()<intCNTRW)
					((JTextField)tblDGRTBL.cmpEDITR[intTB_LOTNO]).setEditable(false);
				else
					((JTextField)tblDGRTBL.cmpEDITR[intTB_LOTNO]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblDGRTBL.cmpEDITR[intTB_RCLNO])) 
			{
				setMSG("Enter the Reclafication Number ",'N');
				if(tblDGRTBL.getSelectedRow()<intCNTRW)
					((JTextField)tblDGRTBL.cmpEDITR[intTB_RCLNO]).setEditable(false);
				else
					((JTextField)tblDGRTBL.cmpEDITR[intTB_RCLNO]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblDGRTBL.cmpEDITR[intTB_MNLCD])) 
			{
				setMSG("Enter the Main Location  ",'N');
				if(tblDGRTBL.getSelectedRow()<intCNTRW)
					((JTextField)tblDGRTBL.cmpEDITR[intTB_MNLCD]).setEditable(false);
				else
					((JTextField)tblDGRTBL.cmpEDITR[intTB_MNLCD]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblDGRTBL.cmpEDITR[intTB_PKGTP])) 
			{
				setMSG("Enter the Package Type ",'N');
				if(tblDGRTBL.getSelectedRow()<intCNTRW)
					((JTextField)tblDGRTBL.cmpEDITR[intTB_PKGTP]).setEditable(false);
				else
					((JTextField)tblDGRTBL.cmpEDITR[intTB_PKGTP]).setEditable(true);
			}
			if(M_objSOURC==((JTextField)tblDGRTBL.cmpEDITR[intTB_ISSQT])) 
			{
				setMSG("Enter the Issue Quantity ",'N');
				if(tblDGRTBL.getSelectedRow()<intCNTRW)
					((JTextField)tblDGRTBL.cmpEDITR[intTB_ISSQT]).setEditable(false);
				else
					((JTextField)tblDGRTBL.cmpEDITR[intTB_ISSQT]).setEditable(true);
			}
			
		}catch(Exception e)
		{
			setMSG(e,"TEIND.FocusGained"+M_objSOURC);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}	
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}		
	
	
	/** @ This is used for Apply Enter the TextField  & F1 Key on the TextFields.
	 * To press the F1 key display the records into the help screen
	 */
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);/* this keypressed was to take the super class call keypressed events*/		strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
			strDGRNO = txtDGRNO.getText().toString().trim(); 
			strPRDTP = txtPRDTP.getText().toString().trim(); 
			strDGRDT = txtDGRDT.getText().toString().trim(); 
			strDGRCT = txtDGRCT.getText().toString().trim(); 
			strRCLOT = txtLOTNO.getText().toString().trim(); 
			
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(M_objSOURC == txtDGRNO)
				{
					if(vldDGRNO()) /* Check the Vld DGRNO  or Not*/
					{
						setMSG("Fetching data...Please wait.",'N');
						getDGRREC();
						setMSG("Press change button to authorize down grading transaction.",'N');
					}
					else
					    setMSG("InValid DGR. No.",'E');
					    txtDGRNO.requestFocus();
				}		
				if(M_objSOURC == txtPRDTP)  /* Check the Valid Product Type or not */
				{
					if(vldPRDTP())
					{
					setMSG("Valid Product Type",'N');
					txtDGRDT.setEnabled(true);
					txtDGRDT.requestFocus();
					}
					else
					{
						setMSG("InValid Product Type",'E');
						txtDGRDT.setEnabled(false);
						txtPRDTP.requestFocus();
					}
				}
				if(M_objSOURC==txtDGRDT)/** Check the VAlid Date	or Not		                         */ 
				{
					
					  	vldDGRDT();				
					
					
				}
				   
				 if(M_objSOURC == txtDGRCT)
				 {
						if(vldDGRCT()) /**This is Valid DGRCT    */
						{
							setMSG("Press F-1 for Issue details.",'N');
							txtLOTNO.setText(strCCSVL);
							tblDGRTBL.requestFocus();
							//tblDGRTBL.setRowSelectionInterval(intTB_CHKFL,intTB_CHKFL);
							//	tblDGRTBL.setColumnSelectionInterval(intTB_LOTNO,intTB_ISSQT);
						}
						else
						{
						setMSG("InValid Category",'E');
						txtDGRCT.requestFocus();
						}
				
    			 }
				 
				 
				 
				 
				 
	       }
				
				if(L_KE.getKeyCode() == L_KE.VK_F1)
				{
					
						
						if(M_objSOURC== txtDGRNO)
						{
							M_strSQLQRY = "Select distinct RCT_RCTNO,RCT_RCTDT from FG_RCTRN where";
							M_strSQLQRY += " RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTTP='"+strDGRTP+"' and RCT_STSFL = '1'";
							cl_dat.M_flgHELPFL_pbst = true;
							M_strHLPFLD = "txtDGRNO";
							
							cl_hlp(M_strSQLQRY,1,1,new String[]{"DGR. No.","Date"},2,"CT");
						}		
						if(M_objSOURC== txtPRDTP)
						{
						   String L_ARRHDR[] ={ "Product Type.","Description"};
						   
							
							 
							 M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
						    M_strSQLQRY += " CMT_CGSTP = 'COXXPRD'";//and CMT_CODCD in ("+cl_dat.ocl_dat.M_STRPR+")";
							//System.out.println (M_strSQLQRY);
							M_strHLPFLD = "txtPRDTP";
			    			cl_hlp(M_strSQLQRY,1,1,new String[]{"Product Type.","Description"},2,"CT");
							
							   
					   }	   
				   if(M_objSOURC== txtDGRCT )
				   {
						
						
					     
						M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,5,2),CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and";
						M_strSQLQRY += " CMT_CGSTP = 'FGXXCAT' and SUBSTRING(CMT_CODCD,1,4) = 'DGR_' and CMT_CHP01 = '"+txtPRDTP.getText().trim()+"' and CMT_CHP02='1'";
							//System.out.println (M_strSQLQRY);
						M_strHLPFLD = "txtDGRCT";
						cl_hlp(M_strSQLQRY,1,2,new String[]{ "Type.","DESC."},2,"CT");
						
						
				   }	
				   //System.out.println("aaaa");
				   
				   if(M_objSOURC == txtLOTNO1)   //Create F1 for Table1 Lot No
				   {
							
							M_strSQLQRY = "Select ST_LOTNO,ST_RCLNO,ST_MNLCD,ST_PKGTP,(ST_STKQT-ST_ALOQT),LT_REMDS,ST_REMDS from FG_STMST,PR_LTMST";
						    M_strSQLQRY += " where ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and ST_CMPCD=LT_CMPCD and ST_STKQT > ST_ALOQT";
							M_strSQLQRY += " and ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND  st_wrhtp = '"+strWRHTP+"' and st_prdtp = '"+strPRDTP+"' order by ST_LOTNO,ST_RCLNO";
							
							
					//tblDGRTBL.setValueAt(txtLOTNO1,tblDGRTBL.getSelectedRow(),intTB_LOTNO);
							
								
							//System.out.println (M_strSQLQRY);
							M_strHLPFLD = "txtLOTNO1";
							cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No.","Rcl. No","Location","Pkg. Type","Quantity","Lot Remark","Stk. Remark"},7,"CT");
						
				   }
				   
		}		   
			
				
		}catch(Exception L_EX)
		 {
					setMSG(L_EX,"F1 Help");	
		 }		
	}
	
	/**This function used for the To Take the values into the DB display the Help Screen
	 * To perform actions on pressing enter or clicking on button "OK" 
	 * in help window Must be re-defined in chlid classes, with call to super at first. 
	 */
	
	 void exeHLPOK()
	 {
		   
		   super.exeHLPOK();
		   cl_dat.M_flgHELPFL_pbst = true;
		   //L_STRLEN = 0;
		   //L_VALSTR = 0;
		   
           StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		    
		   if(M_strHLPFLD == "txtDGRNO")
		   {
			   cl_dat.M_flgHELPFL_pbst = false;
     		   txtDGRNO.setText(L_STRTKN.nextToken());
			   txtDGRDT.setText(L_STRTKN.nextToken());
			   
		   }
		   if(M_strHLPFLD .equals("txtPRDTP"))
		   {
			   txtPRDTP.setText(L_STRTKN.nextToken());
			   
			   
		   }
		   if(M_strHLPFLD.equals("txtDGRCT"))
		   {
			   String L_strTEMP = L_STRTKN.nextToken();
			   txtDGRCT.setText(L_STRTKN.nextToken());
			   
		   }
		   if(M_strHLPFLD.equals("txtLOTNO1"))
		   {
			  String L_strLOTNO =  L_STRTKN.nextToken();
  			  String L_strRCLNO =  L_STRTKN.nextToken();
			  String L_strMNLCD =  L_STRTKN.nextToken();
			  String L_strPKGTP =  L_STRTKN.nextToken();
			  String L_strSTKQT =  L_STRTKN.nextToken();

			  txtLOTNO1.setText(L_strLOTNO);
			  tblDGRTBL.setValueAt(L_strRCLNO,tblDGRTBL.getSelectedRow(),intTB_RCLNO);/* Used for Display help screen on the Table */
			  tblDGRTBL.setValueAt(L_strMNLCD,tblDGRTBL.getSelectedRow(),intTB_MNLCD); 				  
			  tblDGRTBL.setValueAt(L_strPKGTP,tblDGRTBL.getSelectedRow(),intTB_PKGTP);
			  tblDGRTBL.setValueAt(L_strSTKQT,tblDGRTBL.getSelectedRow(),intTB_ISSQT);
			  hstSTKQT.put(L_strLOTNO+L_strRCLNO+L_strMNLCD+L_strPKGTP,L_strSTKQT);
			  
			}
			   
	}
	
	/**@return boolean and check the Vld DGRNO or not */
	private boolean vldDGRNO(){
		try{
			M_strSQLQRY = "Select * from FG_RCTRN where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTTP='"+strDGRTP+"'";
			M_strSQLQRY += " and RCT_STSFL = '1' and RCT_RCTNO='"+txtDGRNO.getText().toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
				return true;
			M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"vldDGRNO");
		}
		return false;
	}
/**@return boolean
 *  This is check the Vld DGRNO or not */
	private boolean vldPRDTP(){
		try{
			M_strSQLQRY = "Select * from CO_CDTRN where CMT_CGMTP='MST' and CMT_CGSTP = 'COXXPRD'";
			M_strSQLQRY += " and CMT_CODCD='"+strPRDTP+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
				return true;
			M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"vldPRDTP");
		}
		return false;
	}
	
	/**@return void and  check the Date valid Date or not
	 */
	private void vldDGRDT()
	{
		try
		{
		                // Increase Date from +1 with Locked Date
			getREFDT();
		
		 
		 
			
			/*Get Reference Date */
		}catch(Exception L_EX)
		{}
	}
	
		
/**To check the Valid category or not
	 */
	
	private boolean vldDGRCT(){
		try{
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,5,2) L_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN where";
			M_strSQLQRY += " CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXCAT' and CMT_CODDS = '"+strDGRCT+"' and CMT_CHP02='1'";
			System.out.println("vldDGRCT : "+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				strCODCD = M_rstRSSET.getString("L_CODCD").toString().trim();
				System.out.println("strCODCD : "+strCODCD);
				strCCSVL = M_rstRSSET.getString("CMT_CCSVL").toString().trim();
				strRCPTP = M_rstRSSET.getString("CMT_CHP01").toString().trim();
				return true;
			}
			M_rstRSSET.close();
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDGRCT");
		}
		return false;
	}
	 /**To check the Valid LOTNO or not
	  */
	 private boolean vldLOTNO()
	 {
		try
		{
			M_strSQLQRY = "Select * from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"' and ST_PRDTP = '"+strPRDTP+"'";
			M_strSQLQRY += " and ST_LOTNO = '"+((JTextField)tblDGRTBL.cmpEDITR[intTB_LOTNO]).getText()+"' and ST_RCLNO = '"+tblDGRTBL.getValueAt(tblDGRTBL.getSelectedRow(),intTB_RCLNO).toString().trim()+"' and ST_STKQT > ST_ALOQT";
			//System.out.println(M_strSQLQRY);
			//System.out.println("This is boolean vaild Lotno");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
				return true;
			M_rstRSSET.close();
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"vldLOTNO");
		}
		return false;
	}

	/**This is used for Valid Stock Quantity  Using HashTable 
	 */ 
	private boolean vldSTKQT1(String LP_LOTNO, String LP_RCLNO,String LP_MNLCD,String LP_PKGTP, String LP_ISSQT)
	{
		try{
			if(!hstSTKQT.containsKey(LP_LOTNO+LP_RCLNO+LP_MNLCD+LP_PKGTP))
				{setMSG("Record not found in hash table ",'N');	return false;}
			double L_dblISSQT = Double.parseDouble(hstSTKQT.get(LP_LOTNO+LP_RCLNO+LP_MNLCD+LP_PKGTP).toString());
			if(Double.parseDouble(LP_ISSQT)>L_dblISSQT)
			{
				setMSG("Issued Qty Exceeds Stock Qty :"+setNumberFormat(L_dblISSQT,3),'E');
				return false;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"vldSTKQT1");
		}
		return true;
	}
	 
	 
	private boolean vldSTKQT(String LP_LOTNO, String LP_RCLNO,String LP_MNLCD,String LP_PKGTP)
	{
		try{
			double L_TOTQT = 0;
			String L_PRVQT = "";
            M_strSQLQRY = "Select (ST_STKQT-ST_ALOQT) L_STKQT from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"' and ST_PRDTP = '"+txtPRDTP.getText().trim()+"' and ST_LOTNO = '"+LP_LOTNO+"' and ST_RCLNO = '"+LP_RCLNO+"'";			
			M_strSQLQRY += " and ST_PKGTP = '"+LP_PKGTP+"' and ST_MNLCD = '"+LP_MNLCD+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				if(String.valueOf(hstENTQT.get(LP_LOTNO+strRCLNO+LP_MNLCD+LP_PKGTP)).equals("null"))
					L_PRVQT = "0.000";
				L_TOTQT = M_rstRSSET.getDouble("L_STKQT") + Double.parseDouble(new BigDecimal(L_TOTQT).toString());
			}
			M_rstRSSET.close();
			if(L_TOTQT >= Double.parseDouble(tblDGRTBL.getValueAt(tblDGRTBL.getSelectedRow(),intTB_ISSQT).toString().trim()))
				return true;
		}catch(Exception L_EX){
			setMSG(L_EX,"vldSTKQT");
		}
		return false;
	}
	
	 /**calculates Issue Packages & displays it*/
	private String	calPKGES()
	{ 
		try{
			double L_NCSVL = 0.000;
			double L_DISQT = 0.000;
			double L_DISPK = 0.000;
			long L_LISPK = 0;
			M_strSQLQRY = "Select CMT_NCSVL from CO_CDTRN where CMT_CGMTP='SYS' and";
			M_strSQLQRY += " CMT_CGSTP = 'FGXXPKG' and CMT_CODCD = '"+tblDGRTBL.getValueAt(tblDGRTBL.getSelectedRow(),intTB_PKGTP).toString().trim()+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
				L_NCSVL = M_rstRSSET.getDouble("CMT_NCSVL");
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			L_DISQT = Double.parseDouble(tblDGRTBL.getValueAt(tblDGRTBL.getSelectedRow(),intTB_ISSQT).toString().trim());
			L_DISPK = L_DISQT/L_NCSVL;
			strPKGWT = setNumberFormat(L_NCSVL,3);
			//System.out.println("L_NCSVL "+L_NCSVL);
			//System.out.println("strSTKQT "+tblDGRTBL.getValueAt(tblDGRTBL.getSelectedRow(),intTB_ISSQT).toString().trim());
			//System.out.println("L_DISPK "+L_DISPK);
			//L_LISPK = Math.round(L_DISPK);
			return setNumberFormat(L_DISPK,3);
		}catch(Exception L_EX){
			setMSG(L_EX,"calPKGES");
		}
		return "";
	}
	
	/**
	 * @return void
	 * Calculates the Total Qty. of Lot JTable & displays it.
	 */
	private void getTOTQP(){  
		try{
			double L_TBLQT = 0;
			double L_DUMQT = 0;
			int L_TBLPK = 0;
			int L_DUMPK = 0;
			for(int i = 0;i<(tblDGRTBL.getRowCount()-1);i++){  
				if(tblDGRTBL.getValueAt(i,intTB_CHKFL).toString().trim().equals("true")){	
					L_TBLQT = nvlNUMDB(tblDGRTBL.getValueAt(i,intTB_ISSQT).toString().trim());
					L_TBLPK = nvlNUMIN(tblDGRTBL.getValueAt(i,intTB_ISSPK).toString().trim());
					L_TBLQT = L_TBLQT + L_DUMQT;
					L_DUMQT = L_TBLQT;
					L_TBLPK = L_TBLPK + L_DUMPK;
					L_DUMPK = L_TBLPK;
					}
				}
			txtTOTQT.setText(setNumberFormat(L_DUMQT,3));
			txtTOTPK.setText(String.valueOf(L_DUMPK));
	}catch(Exception L_EX){
		setMSG(L_EX,"getTOTPK");
		}
	}
	

	/**@return void Get Reference Date
	 */
	
					 
	
/** Valid CDTRN or Not
 */	
	private boolean vldCDTRN()
	{
		try
		{
			M_strSQLQRY = "Select CMT_CHP01,CMT_CHP02,CMT_CCSVL FROM CO_CDTRN WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " AND CMT_CGSTP = 'FGXXDGR'";
			M_strSQLQRY += " AND CMT_CODCD = '"+strCODCD+"'";
			System.out.println("vldCDTRN : "+M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next()){
				strCHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"").trim();
				strCHP02 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"").trim();
				}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			if(strCHP02.equals("HOLD"))
				return false;
		}catch(Exception L_EX){
			setMSG(L_EX,"vldCDTRN");
		}
		return true;
	}
	
	
	
	

	/**@return void
	 * get reference date
	 */
	public void getREFDT()
	{
		try
		{
		//	Date L_strTEMP=null;
			String L_strREFDT="";
			String P_strLOGDT=cl_dat.M_strLOGDT_pbst ;
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				L_rstRSSET.close();
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
				strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				L_strREFDT =strREFDT ;
				//System.out.println("aaaaaaaa");
				//System.out.println(strREFDT);
				
			}	
			    
				if(M_fmtLCDAT.parse(txtDGRDT.getText().trim()).compareTo(M_fmtLCDAT.parse(strREFDT)) != 0)
				{
				  setMSG("Invalid Date Format...",'E');
				  if(!P_strLOGDT.equals(strREFDT))
				  JOptionPane.showMessageDialog(this,"Transactions upto "+L_strREFDT+" have already been locked.");					
				  JOptionPane.showMessageDialog(this,"Please Enter "+strREFDT);
				  txtDGRDT.requestFocus();
				}
				else
				 {
					setMSG("Valied Date" +strREFDT ,'N');
					txtDGRCT.setEnabled(true);
					txtDGRCT.requestFocus();
				 }
				
				 
		}				
			
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREFDT");
		}
	}
	
/**This TBLINPF class used for validation Apply on the Lot of Table. When using Table, TBLINPF is Mandataory 
 */	
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
				if(getSource()==tblDGRTBL)
				{
				   if(P_intCOLID==intTB_LOTNO)//Validates Lot No. for Production JTable
				   {
					   if(vldLOTNO())
						{	
							setMSG("Valid Lot No.",'N');
						    tblDGRTBL.setValueAt(new Boolean(true),tblDGRTBL.getSelectedRow(),intTB_CHKFL);
							tblDGRTBL.setRowSelectionInterval(tblDGRTBL.getSelectedRow(),tblDGRTBL.getSelectedRow());
							tblDGRTBL.setColumnSelectionInterval(intTB_PKGTP,intTB_ISSQT);
							//System.out.println("VldLOTNO");
						}
						else 
						{
							   
							   setMSG("In Valid Lot No.",'N');
							   tblDGRTBL.setColumnSelectionInterval(intTB_CHKFL,intTB_LOTNO);
							   
						}
					   
					  
					}
					if(P_intCOLID == intTB_RCLNO || P_intCOLID == intTB_MNLCD || P_intCOLID == intTB_PKGTP)
					{
						setMSG("Entered Field cannot be edited.",'E');
						tblDGRTBL.setValueAt(new Boolean(true),tblDGRTBL.getSelectedRow(),intTB_CHKFL);
						tblDGRTBL.setColumnSelectionInterval(intTB_CHKFL,intTB_LOTNO);
						
					}
					if(P_intCOLID == intTB_ISSQT)
					{

						if(vldSTKQT1(tblDGRTBL.getValueAt(P_intROWID,intTB_LOTNO).toString(),tblDGRTBL.getValueAt(P_intROWID,intTB_RCLNO).toString(),tblDGRTBL.getValueAt(P_intROWID,intTB_MNLCD).toString(),tblDGRTBL.getValueAt(P_intROWID,intTB_PKGTP).toString(),tblDGRTBL.getValueAt(P_intROWID,intTB_ISSQT).toString()))
						{
							//setMSG("Press F-1 for Issue details.",'N');
							strCALPK = calPKGES();
							//System.out.println(strCALPK);
							if(Double.parseDouble(strCALPK) != Math.round(Double.parseDouble(strCALPK)))
							{setMSG("Qty. is not in multiple of Pkg.Wt. : "+strPKGWT,'E');
							 tblDGRTBL.setColumnSelectionInterval(intTB_PKGTP,intTB_ISSQT);}
							
							tblDGRTBL.setValueAt(setNumberFormat(Double.parseDouble(strCALPK),0),tblDGRTBL.getSelectedRow(),intTB_ISSPK);
							getTOTQP();
							
							//tblDGRTBL.setColumnSelectionInterval(intTB_LOTNO,intTB_RCLNO);
						}
						else
						{
							//setMSG("Entered Qty. exceeds Stock qty.",'E');	
							tblDGRTBL.setColumnSelectionInterval(intTB_PKGTP,intTB_ISSQT);
						}
					}	
				}	
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"this is input verifier");
			}
			return true;
		}
	}
	
	/** this function used for each and every field to valid or not
	 */
	 boolean vldDATA()
	{
		try
		{
			String strTEMP="";
			//System.out.println("strTEMP");
			if(tblDGRTBL.isEditing())
			{
				if(tblDGRTBL.getValueAt(tblDGRTBL.getSelectedRow(),tblDGRTBL.getSelectedColumn()).toString().length()>0)
				{
				TBLINPVF obj=new TBLINPVF();
				obj.setSource(tblDGRTBL);
				if(obj.verify(tblDGRTBL.getSelectedRow(),tblDGRTBL.getSelectedColumn()))
					tblDGRTBL.getCellEditor().stopCellEditing();
				else
					return false;
				}
			}	
				//tblDGRTBL.getCellEditor().stopCellEditing();
			if(txtPRDTP.getText().trim().length()==0)
			{
				setMSG(" Production Type cannot be Blank........",'E');
				txtPRDTP.requestFocus();
				return false;
			}
			
			if(txtDGRCT.getText().trim().length()==0)
			{
				setMSG("DGR Categary cannot be blank..........",'E');
				txtDGRCT.requestFocus();
				return false;
			}
			if(txtDGRDT.getText().trim().length()==0)
			{
				setMSG("DGR Date cannot be blank..........",'E');
				txtDGRDT.requestFocus();
				return false;
			}
			for(int i=0;i<tblDGRTBL.getRowCount();i++)
    		{
				if(tblDGRTBL.getValueAt(i,intTB_CHKFL).toString().equals("true"))
    			{
					strTEMP = nvlSTRVL(tblDGRTBL.getValueAt(i,intTB_LOTNO).toString(),"");
    				if(strTEMP.length() == 0)
    				{
    					setMSG("Lot Number can not be blank..",'E');
						return false;
    				}
				 	strTEMP = nvlSTRVL(tblDGRTBL.getValueAt(i,intTB_RCLNO).toString(),"");
					if(strTEMP.length() == 0)
    				{
    					setMSG("ReClassification  Number can not be blank..",'E');
						return false;
    				}
					
					strTEMP = nvlSTRVL(tblDGRTBL.getValueAt(i,intTB_MNLCD).toString(),"");
					if(strTEMP.length() == 0)
    				{
    					setMSG("Main Location can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblDGRTBL.getValueAt(i,intTB_PKGTP).toString(),"");
					if(strTEMP.length() == 0)
    				{
    					setMSG("Package Type  Number can not be blank..",'E');
						return false;
    				}
					strTEMP = nvlSTRVL(tblDGRTBL.getValueAt(i,intTB_ISSPK).toString(),"");
					if(strTEMP.length() == 0)
    				{
    					setMSG("Table IssueQuanity  Number can not be blank..",'E');
						return false;
    				}
				}
			}	
			   
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Vald Data");
		}
		return true;
	}	
	
	/**This function used for get the DGRNumber
	 */
	
	private void getDGRREC()
	{
		try
		{
			hstENTQT.clear();
			String L_LOTNO = "";
			String L_RCLNO = "";
			String L_MNLCD = "";
			String L_PKGTP = "";
			String L_ISSQT = "";
			M_strSQLQRY = "Select rct_prdtp,CMT_CODDS,rct_lotno,sum(rct_rctqt) l_rctqt,";
			M_strSQLQRY += " sum(rct_rctpk) l_rctpk from FG_RCTRN,CO_CDTRN where CMT_CGMTP = 'SYS' and";
			M_strSQLQRY += " CMT_CGSTP = 'FGXXCAT' and SUBSTRING(CMT_CODCD,1,4) = 'DGR_' and CMT_CHP02='1'";
			M_strSQLQRY += " and rct_prdtp=cmt_chp01 and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp='"+strDGRTP+"' and";
			M_strSQLQRY += " rct_rctno='"+txtDGRNO.getText().toString().trim()+"' and rct_stsfl='1'";
			M_strSQLQRY += " group by rct_prdtp,cmt_codds,rct_lotno";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET.next()){
				strRCPTP = nvlSTRVL(M_rstRSSET.getString("rct_prdtp").trim(),"");
				txtDGRCT.setText(nvlSTRVL(M_rstRSSET.getString("cmt_codds").trim(),""));
				txtLOTNO.setText(nvlSTRVL(M_rstRSSET.getString("rct_lotno").trim(),""));
				txtTOTQT.setText(nvlSTRVL(M_rstRSSET.getString("l_rctqt").trim(),""));
				txtTOTPK.setText(nvlSTRVL(M_rstRSSET.getString("l_rctpk").trim(),""));
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			M_strSQLQRY = "Select ist_prdtp,ist_lotno,ist_rclno,ist_mnlcd,ist_pkgtp,ist_issqt,";
			M_strSQLQRY += "ist_isspk from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_isstp = '"+strDGRTP+"' and";
			M_strSQLQRY += " ist_issno='"+txtDGRNO.getText().trim()+"' and ist_stsfl='1'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			int i = 0;
			while(M_rstRSSET.next()){
				L_LOTNO = nvlSTRVL(M_rstRSSET.getString("ist_lotno").trim(),"");
				L_RCLNO = nvlSTRVL(M_rstRSSET.getString("ist_rclno").trim(),"");
				L_MNLCD = nvlSTRVL(M_rstRSSET.getString("ist_mnlcd").trim(),"");
				L_PKGTP = nvlSTRVL(M_rstRSSET.getString("ist_pkgtp").trim(),"");
				L_ISSQT = nvlSTRVL(M_rstRSSET.getString("ist_issqt").trim(),"");
				tblDGRTBL.setValueAt(new Boolean(true),i,intTB_CHKFL);
				txtPRDTP.setText(nvlSTRVL(M_rstRSSET.getString("ist_prdtp").trim(),""));
				tblDGRTBL.setValueAt(L_LOTNO,i,intTB_LOTNO);
				tblDGRTBL.setValueAt(L_RCLNO,i,intTB_RCLNO);
				tblDGRTBL.setValueAt(L_MNLCD,i,intTB_MNLCD);
				tblDGRTBL.setValueAt(L_PKGTP,i,intTB_PKGTP);
				tblDGRTBL.setValueAt(L_ISSQT,i,intTB_ISSQT);
				tblDGRTBL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ist_isspk").trim(),""),i,intTB_ISSPK);
				hstENTQT.put(L_LOTNO+L_RCLNO+L_MNLCD+L_PKGTP,L_ISSQT);
				i++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"getDGRREC");
		}
	}
	
	
	
	
	
	
	/**@return void This method is used for save records into the DataBase
	 *  Method to save/update/delete data. called by default To be overridden in child classes  
	 */
	
	void exeSAVE()
    {
       try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			if(!vldDATA())
			{
				return;
			}
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))	
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
    
	/**insert the record into the DB & Update the DB & Retrive the DB
	 */
		private void  exeAUTADD()
		{
			try
            {
				strWRHTP = cmbWRHTP.getSelectedItem().toString().trim();
				strDGRNO = txtDGRNO.getText().toString().trim(); 
				strPRDTP = txtPRDTP.getText().toString().trim(); 
				strDGRCT = txtDGRCT.getText().toString().trim(); 
				strDGRDT = txtDGRDT.getText().toString().trim(); 
				strRCLOT = txtLOTNO.getText().toString().trim(); 
				//System.out.println("This is ADD ");
				
				
				cl_dat.M_flgLCUPD_pbst = true;
				strCODCD = strYRDGT + strDGRTP;
				//System.out.println(""+strCODCD);
				
				if(vldCDTRN())
				{
					//System.out.println("vldCDTRN");
					if(chkNEGQTY())
					{
						//System.out.println("ChknegQTY");
						setCursor(cl_dat.M_curWTSTS_pbst);
						if(L_NUMFL)
						{	
							//System.out.println("General DGRNO");
							genDGRNO();
						}	
						if(rdbDGRALO.isSelected())
							strSTSFL = "1";
						else if(rdbDGRAUT.isSelected())
							strSTSFL = "2";
						String L_CURTM = cl_dat.getCURTIM();
						String L_USRCD = cl_dat.M_strUSRCD_pbst;
						cl_cust.updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXDGR",strCODCD,L_CURTM,L_USRCD);
						if(txtDGRNO.getText().toString().trim().length() == 8)
						{
							genISSUE();
							genRECPT();
							if(cl_dat.M_flgLCUPD_pbst)
							{
								cl_dat.exeDBCMT("ExeSAVE");
								
								L_CURTM = "";
								 L_USRCD = "";
								cl_cust.updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXDGR",strCODCD,L_CURTM,L_USRCD);	
								if(L_NUMFL)
									updDGRNO();
									
								setMSG("Down-Grading transactions have been saved Successfully.",'N');
								L_NUMFL = true;
								txtDGRNO.setText("");
								txtPRDTP.setText("");
								txtDGRCT.setText("");
								txtLOTNO.setText("");
								txtTOTQT.setText("");
								txtTOTPK.setText("");
								//chkPRINT.setEnabled(false);
								txtDGRDT.setEnabled(false);
								txtDGRCT.setEnabled(false);
								txtLOTNO.setEnabled(false); 
								rdbDGRALO.setSelected(true);
								M_strSQLQRY = "Select count(*) from fg_rctrn where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp='"+strDGRTP+"'";
								M_strSQLQRY += " and rct_stsfl = '1'";
								//System.out.println(M_strSQLQRY);
								if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
								{
									//System.out.println("This is getRecent method");
									txtPRDTP.setEnabled(false);
									txtDGRNO.setEnabled(true);
									txtDGRNO.requestFocus();
									L_NUMFL = false;
								}else
								{
									txtPRDTP.setEnabled(true);
									txtDGRNO.setEnabled(false);
									txtPRDTP.requestFocus();
									
									L_NUMFL = true;
								}
								for(int i = 0;i < tblDGRTBL.getRowCount();i++)
								{
									tblDGRTBL.setValueAt(new Boolean(false),i,0);
									for(int j = 1;j < tblDGRTBL.getColumnCount();j++)
									{
										tblDGRTBL.setValueAt("",i,j);
									}
								}
							
							}
								//chkPRINT.setEnabled(true);
							
							else
							{
								L_CURTM = "";
								L_USRCD = "";
								cl_cust.updCDTRN("D"+cl_dat.M_strCMPCD_pbst,"FGXXDGR",strCODCD,L_CURTM,L_USRCD);	
								setMSG("Updation of Down Grading transactions failed.",'E');
							}
						}
						else
							setMSG("DGR Transaction Number not generated properly",'E');
					}
					else
					{
						cl_dat.M_flgLCUPD_pbst = false;
						if(L_ERRENT)
						{
							LM_OPTNPN.showMessageDialog(this,"Lot No. "+strTBLLOTNO+" with location "+strTBLMNLCD+" is entered twice.","Authorization Status",JOptionPane.ERROR_MESSAGE);
							setMSG("Duplicate data entry is not valid.",'E');
						}
						else
						{
							LM_OPTNPN.showMessageDialog(this,"Stock is getting negative for Lot No. "+strTBLLOTNO+" at location "+strTBLMNLCD,"Authorization Status",JOptionPane.ERROR_MESSAGE);
							setMSG("Stock is getting negative for Lot No. "+strTBLLOTNO+" at location "+strTBLMNLCD,'E');
						}
					}
					
				}	else
					{
					LM_OPTNPN.showMessageDialog(this,"Please Wait... PTF generation in progress by "+strCHP01,"Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
					setMSG("Please Wait... PTF generation in progress by "+strCHP01,'E');
					cl_dat.M_flgLCUPD_pbst = false;
				    }
				  this.setCursor(cl_dat.M_curWTSTS_pbst);
				
			
				
						
				
			}catch(Exception L_EX)
			{}
		}	
	/**Tocheck the Quantity is negative or not and apply the validation on  Quanity 
	 */	
private boolean chkNEGQTY()
{
		L_RETFL = true;
		try{
			hstLOTNO.clear();
			hstRCLNO.clear();
			hstMNLCD.clear();
			hstPKGTP.clear();
			hstSTKQT.clear();
			intRECCNT = 0;
			dblTOTLTQT = 0.000;
			L_ERRENT = false;
			ResultSet M_rstRSSET1;
			//System.out.println("This is Check negetive Quantity");
			for(int i = 0;i < tblDGRTBL.getRowCount();i++){
				if(tblDGRTBL.getValueAt(i,intTB_CHKFL).toString().trim().equals("true"))
				{
					strTBLLOTNO = tblDGRTBL.getValueAt(i,intTB_LOTNO).toString().trim();
				
					strTBLRCLNO = tblDGRTBL.getValueAt(i,intTB_RCLNO).toString().trim();
				
					strTBLMNLCD = tblDGRTBL.getValueAt(i,intTB_MNLCD).toString().trim();
				
					strTBLPKGTP = tblDGRTBL.getValueAt(i,intTB_PKGTP).toString().trim();
				
					strTBLSTKQT = tblDGRTBL.getValueAt(i,intTB_ISSQT).toString().trim();
				
					if(strTBLSTKQT.trim().equals(""))
						strTBLSTKQT = "0.000";
					dblTOTLTQT = dblTOTLTQT + Double.parseDouble(strTBLSTKQT);
					//System.out.println(""+dblTOTLTQT);
					for(int j = 0;j < intRECCNT;j++)
					{
						if((strTBLLOTNO+strTBLRCLNO+strTBLMNLCD+strTBLPKGTP).equals(hstLOTNO.get(String.valueOf(j)).toString()+hstRCLNO.get(String.valueOf(j)).toString()+hstMNLCD.get(String.valueOf(j)).toString()+hstPKGTP.get(String.valueOf(j)).toString()))
						{
							L_ERRENT = true;
							return false;
						}
					}
					hstLOTNO.put(String.valueOf(intRECCNT),strTBLLOTNO);
					hstRCLNO.put(String.valueOf(intRECCNT),strTBLRCLNO);
					hstMNLCD.put(String.valueOf(intRECCNT),strTBLMNLCD);
					hstPKGTP.put(String.valueOf(intRECCNT),strTBLPKGTP);
					hstSTKQT.put(String.valueOf(intRECCNT),setNumberFormat(dblTOTLTQT,3));
					intRECCNT++;
					dblTOTLTQT = 0;
				}
			}
			for(int i = 0;i < intRECCNT;i++)
			{
				dblDBSQT = 0;
				M_strSQLQRY = "Select sum(st_stkqt) l_stkqt from fg_stmst where";
				M_strSQLQRY += " ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND st_prdtp='"+strPRDTP+"' and";
				M_strSQLQRY += " st_lotno='"+hstLOTNO.get(String.valueOf(i))+"' and";
				M_strSQLQRY += " st_rclno='"+hstRCLNO.get(String.valueOf(i))+"' and";
				M_strSQLQRY += " st_pkgtp='"+hstPKGTP.get(String.valueOf(i))+"' and";
				M_strSQLQRY += " st_mnlcd='"+hstMNLCD.get(String.valueOf(i))+"'";
				//System.out.println(M_strSQLQRY);
				try{
					M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
					
					if(M_rstRSSET1.next())
						dblDBSQT = M_rstRSSET1.getDouble("l_stkqt");
					if(M_rstRSSET1 != null)
						M_rstRSSET1.close();
					if(dblDBSQT < Double.parseDouble(hstSTKQT.get(String.valueOf(i)).toString()))
					{
						strTBLLOTNO = hstLOTNO.get(String.valueOf(i)).toString();
						strTBLMNLCD = hstMNLCD.get(String.valueOf(i)).toString();
						return false;
					}
				}catch(Exception L_EX)
				{
					setMSG(L_EX,"Stock Exceeds");
					return false;
				}
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"chkNEGQTY");
		}
		return L_RETFL;
	}
private boolean getDGRVAL(int i)
	{
			//System.out.println("This is getDGRVAL");
			try
			{
				
						if(tblDGRTBL.getValueAt(i,intTB_CHKFL).toString().trim().equals("true"))
						{	
				        strLOTNO = tblDGRTBL.getValueAt(i,intTB_LOTNO).toString().trim();
						//System.out.println("this is lotno " +strLOTNO);
				
						strRCLNO = tblDGRTBL.getValueAt(i,intTB_RCLNO).toString().trim();
						//System.out.println("this is lotno " +strRCLNO);
						strMNLCD = tblDGRTBL.getValueAt(i,intTB_MNLCD).toString().trim();
						strPKGTP = tblDGRTBL.getValueAt(i,intTB_PKGTP).toString().trim();
						strSTKQT = setNumberFormat(Double.parseDouble(tblDGRTBL.getValueAt(i,intTB_ISSQT).toString().trim()),3);
						strCALPK = tblDGRTBL.getValueAt(i,intTB_ISSPK).toString().trim();
						strPKGWT = cl_dat.getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",strPKGTP);
						strPKGCT = cl_dat.getPRMCOD("CMT_CCSVL","SYS","FGXXPKG",strPKGTP);
						getPRDCD();  //fetches PRDCD from fg_stmst
						return true;
						}
						else
						{
							return false;
						}	
				
				
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"This is getDGRVL");
			}	
			 return false;
	}
/**generated the DGRNUmber from the Table CMT_CHP01,CMT_CCSVL
 */
private void genDGRNO()
{
		try
		{
		  //System.out.println("This is genDGRNO");
			ResultSet M_rstRSSET2;
			strUPDNO = "";
		   int L_RCTNO = 0;
		   String L_CHP01 = "";
		   M_strSQLQRY = "Select CMT_CHP01,CMT_CCSVL FROM CO_CDTRN WHERE CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
		   M_strSQLQRY += " AND CMT_CGSTP = 'FGXXDGR'";
		   M_strSQLQRY += " AND CMT_CODCD = '"+strCODCD+"'";
		   //System.out.println(M_strSQLQRY);
		   M_rstRSSET2 = cl_dat.exeSQLQRY(M_strSQLQRY);
		   //System.out.println(M_strSQLQRY);
		   if(M_rstRSSET2.next()){
				L_CHP01 = M_rstRSSET2.getString("CMT_CHP01").trim();
				//System.out.println(L_CHP01);
				L_RCTNO = M_rstRSSET2.getInt("CMT_CCSVL");
				//System.out.println(L_RCTNO);
		   }
			if(M_rstRSSET2 != null)
				M_rstRSSET2.close();
			//if(L_CHP01.equals("YES"))
			L_RCTNO = L_RCTNO + 1;
			//System.out.println(L_RCTNO);
			String L_STRRCT = String.valueOf(L_RCTNO);
			for(int i=0;i<(5-L_STRRCT.length());i++)
				strUPDNO = strUPDNO + "0";
			strUPDNO = strUPDNO + L_STRRCT;
			strDGRNO=txtDGRNO.getText().toString().trim();
			strDGRNO = strCODCD + strUPDNO;
			txtDGRNO.setText(strDGRNO);
			LM_OPTNPN.showMessageDialog(this,"Please note down the Transaction No.: "+txtDGRNO.getText().toString().trim()+"","Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
		}catch(Exception L_EX){
			setMSG(L_EX,"genDGRNO");
		}
	}
/**Generation the Issues  update and insertion to the DB
 */
 private void genISSUE(){
		try{
			//this.setCursor(cl_dat.M_curDFSTS_pbst);
			//System.out.println("Generation Issues");
			setMSG("Generating Issues.",'N');
			for(int i=0;i<=(tblDGRTBL.getRowCount()-1);i++)
			{			
				if(getDGRVAL(i))
				{	
					if(cl_dat.M_flgLCUPD_pbst)
					{
						M_strSQLQRY = "Select count(*) from fg_stmst";
						M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"'";
						M_strSQLQRY += " and ST_PRDTP = '"+strPRDTP+"'";
						M_strSQLQRY += " and ST_LOTNO = '"+strLOTNO+"'";
						M_strSQLQRY += " and ST_RCLNO = '"+strRCLNO+"'";
						M_strSQLQRY += " and ST_PKGTP = '"+strPKGTP+"'";
						M_strSQLQRY += " and ST_MNLCD = '"+strMNLCD+"'";
						//System.out.println(M_strSQLQRY);
						if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
						{
							updSTMST();
						
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							//System.out.println(M_strSQLQRY);
						
						}else
						{
							setMSG("Record does not exist in FG_STMST",'E');
							cl_dat.M_flgLCUPD_pbst = false;
						}
						M_strSQLQRY = "Select count(*) from fg_istrn";
						M_strSQLQRY += " where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+strWRHTP+"'";
						M_strSQLQRY += " and IST_ISSTP = '"+strDGRTP+"'";
						M_strSQLQRY += " and IST_ISSNO = '"+strDGRNO+"'";
						M_strSQLQRY += " and IST_PRDCD = '"+strPRDCD+"'";
						M_strSQLQRY += " and IST_PRDTP = '"+strPRDTP+"'";
						M_strSQLQRY += " and IST_LOTNO = '"+strLOTNO+"'";
						M_strSQLQRY += " and IST_RCLNO = '"+strRCLNO+"'";
						M_strSQLQRY += " and IST_PKGTP = '"+strPKGTP+"'";
						M_strSQLQRY += " and IST_MNLCD = '"+strMNLCD+"'";
						//System.out.println(M_strSQLQRY);
						if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
							updISTRN();
						else
							insISTRN();
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						//System.out.println(M_strSQLQRY);
						
					}
				}
			}
			if(cl_dat.M_flgLCUPD_pbst)
			{
				cl_dat.exeDBCMT("exeSave");
				//cl_dat.exeDBCMT("");
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"genISSUE");
		}
	}

/** updating Stock Master i.e FG_STMST
 */
	private void updSTMST()	
	{
		try{
			//System.out.println("updating Stock Master");
			dblDSTQT = 0;
			dblDALQT = 0;
			dblDUPQT = 0;
			strALOQT = getALOQT();
			if(rdbDGRALO.isSelected())	//Allocation
			{		
				dblDSTQT = Double.parseDouble(strSTKQT);
				dblDALQT = Double.parseDouble(strALOQT);
				dblDUPQT = dblDSTQT - dblDALQT;
				strALOQT = setNumberFormat(dblDUPQT,3);
				strUPDQT = "0.000";
			}
			else if(rdbDGRAUT.isSelected() && strISREC.equals("0")){ // Authorizing & record
				strALOQT = "0.000";									//not present in fg_istrn
				strUPDQT = strSTKQT;
			}
			else if(rdbDGRAUT.isSelected() && strISREC.equals("1"))
			{ // Authorizing & record
				dblDSTQT = Double.parseDouble("-1");				// present in fg_istrn
				dblDALQT = Double.parseDouble(strALOQT);
				dblDUPQT = dblDSTQT * dblDALQT;
				strALOQT = setNumberFormat(dblDUPQT,3);
				strUPDQT = strSTKQT;
			}
			M_strSQLQRY = "Update FG_STMST SET ";
			M_strSQLQRY += "ST_ALOQT = ST_ALOQT + "+strALOQT+",";
			M_strSQLQRY += "ST_STKQT = ST_STKQT - "+strUPDQT+",";
			M_strSQLQRY += "ST_TRNFL = '0',";
			M_strSQLQRY += "ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and ST_PRDTP = '"+strPRDTP+"'";
			M_strSQLQRY += " and ST_LOTNO = '"+strLOTNO+"'";
			M_strSQLQRY += " and ST_RCLNO = '"+strRCLNO+"'";
			M_strSQLQRY += " and ST_PKGTP = '"+strPKGTP+"'";
			M_strSQLQRY += " and ST_MNLCD = '"+strMNLCD+"'";
			//System.out.println(M_strSQLQRY);
			
		}catch(Exception L_EX){
			setMSG(L_EX,"updSTMST");
		}
	}
	/**Modifies Issue Transaction i.e FG_ISTRN*/
	private void updISTRN(){ 
		try{
			//System.out.println("this is Modifies Issue Transaction");
			M_strSQLQRY = "Update FG_ISTRN SET ";
			M_strSQLQRY += "IST_ISSQT = "+strSTKQT+",";
			M_strSQLQRY += "IST_ISSPK = "+strCALPK+",";
			M_strSQLQRY += "IST_PKGTP = '"+strPKGTP+"',";
			M_strSQLQRY += "IST_PKGCT = '"+strPKGCT+"',";
			if(setNumberFormat(Double.parseDouble(strSTKQT),3).equals("0.000"))
				M_strSQLQRY += "IST_STSFL = 'X',";
			else
				M_strSQLQRY += "IST_STSFL = '"+strSTSFL+"',";
			M_strSQLQRY += "IST_TRNFL = '0',";
			M_strSQLQRY += "IST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "IST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
			M_strSQLQRY += " where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP = '"+strWRHTP+"'";
			M_strSQLQRY += " and IST_ISSTP = '"+strDGRTP+"'";
			M_strSQLQRY += " and IST_ISSNO = '"+txtDGRNO.getText().toString().trim()+"'";
			M_strSQLQRY += " and IST_PRDCD = '"+strPRDCD+"'";
			M_strSQLQRY += " and IST_PRDTP = '"+strPRDTP+"'";
			M_strSQLQRY += " and IST_LOTNO = '"+strLOTNO+"'";
			M_strSQLQRY += " and IST_RCLNO = '"+strRCLNO+"'";
			M_strSQLQRY += " and IST_PKGTP = '"+strPKGTP+"'";
			M_strSQLQRY += " and IST_MNLCD = '"+strMNLCD+"'";
			
			//System.out.println("Update ISTRN :"+M_strSQLQRY);
			//cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
		}catch(Exception L_EX){
			setMSG(L_EX,"updISTRN");
		}
	}
	/**Inserts into Issue Transaction i.e FG_ISTRN*/
	private void insISTRN(){ 
		try{
			
			//System.out.println("nserts into Issue Transaction ");
			M_strSQLQRY = "Insert into FG_ISTRN(IST_CMPCD,IST_WRHTP,IST_ISSTP,IST_ISSNO,IST_PRDTP,IST_TDSFL,";
			M_strSQLQRY += "IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGCT,IST_PKGTP,IST_ISSDT,IST_AUTDT,IST_ISSQT,";
			M_strSQLQRY += "IST_ISSPK,IST_STKTP,IST_STSFL,IST_TRNFL,IST_LUSBY,IST_LUPDT,IST_PRDCD,";
			M_strSQLQRY += "IST_SALTP,IST_MKTTP) values (";
			M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
			M_strSQLQRY += "'"+strWRHTP+"',";
			M_strSQLQRY += "'"+strDGRTP+"',";
			M_strSQLQRY += "'"+txtDGRNO.getText().toString().trim()+"',";
			M_strSQLQRY += "'"+strPRDTP+"',";
			M_strSQLQRY += "' ',";
			M_strSQLQRY += "'"+strLOTNO+"',";
			M_strSQLQRY += "'"+strRCLNO+"',";
			M_strSQLQRY += "'"+strMNLCD+"',";
			M_strSQLQRY += "'"+strPKGCT+"',";
			M_strSQLQRY += "'"+strPKGTP+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDGRDT.getText()))+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDGRDT.getText()))+"',";
			M_strSQLQRY += strSTKQT+",";
			M_strSQLQRY += strCALPK+",";
			M_strSQLQRY += "'1',";
			if(setNumberFormat(Double.parseDouble(strSTKQT),3).equals("0.000"))
			{
				M_strSQLQRY += "'X',";
			}
			else
				M_strSQLQRY += "'"+strSTSFL+"',";
			M_strSQLQRY += "'0',";
			M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
			M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
			M_strSQLQRY += "'"+strPRDCD+"',";
			M_strSQLQRY += "' ',";
			M_strSQLQRY += "' ')";
			//System.out.println(M_strSQLQRY);
			//cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
		}catch(Exception L_EX){
			setMSG(L_EX,"insISTRN");
			}
	}
	
	/**
	 * @return void
	 * picks up Product Codes from fg_stmst
	 */
	private void getPRDCD()
	{  
		try{
			ResultSet M_rstRSSET3;
			//System.out.println("this is picks products codes");
			M_strSQLQRY = "Select ST_PRDCD from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP='"+strWRHTP+"' and ST_PRDTP='"+strPRDTP+"'";
			M_strSQLQRY += " and ST_LOTNO='"+strLOTNO+"' and ST_RCLNO='"+strRCLNO+"' and ST_PKGTP='"+strPKGTP+"' and ST_MNLCD='"+strMNLCD+"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET3 = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET3.next())
				strPRDCD = nvlSTRVL(M_rstRSSET3.getString("ST_PRDCD"),"").trim();
			if(M_rstRSSET3 !=null)
				M_rstRSSET3.close();
			if(strPRDCD.equals("")){
				strPRDCD = " ";
				LM_OPTNPN.showMessageDialog(this,"Product Code not found for Lot No: "+strLOTNO,"Data Transfer Status",JOptionPane.ERROR_MESSAGE);
				cl_dat.M_flgLCUPD_pbst = false;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"getPRDCD");
		}	
	}
	
	
	/**
	 * @return void
	 *Fetching Allocated Qty. from Issue Transaction i.e FG_ISTRN
	 */
	private String getALOQT()
	{ 
		String L_ALOQT = "0.000";
		strISREC = "";
		try{
			//System.out.println("This is getALOQT");
			M_strSQLQRY = "Select IST_ISSQT from FG_ISTRN where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_WRHTP='"+strWRHTP+"' and IST_ISSTP='"+strDGRTP+"'";
			M_strSQLQRY += " and IST_ISSNO='"+txtDGRNO.getText().toString().trim()+"'  and IST_PRDCD='"+strPRDCD+"' and IST_PRDTP='"+strPRDTP+"'";
			M_strSQLQRY += " and IST_LOTNO='"+strLOTNO+"' and IST_RCLNO='"+strRCLNO+"' and IST_PKGTP='"+strPKGTP+"'";
			M_strSQLQRY += " and IST_MNLCD='"+strMNLCD+"' and IST_STSFL = '1'";
	      //System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println("Get Allquantity "+M_strSQLQRY);
			if(M_rstRSSET.next())
				L_ALOQT = M_rstRSSET.getString("IST_ISSQT");
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			if(L_ALOQT.equals("0.000") || L_ALOQT == null){
				strISREC = "0";
				return "0.000";
			}else{
				strISREC = "1";
				return setNumberFormat(Double.parseDouble(L_ALOQT),3);
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"getALOQT");
		}
		return L_ALOQT;
	}
	/**Generation Reception i.e FG_ISTRN*/
	private void genRECPT()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				
				ResultSet M_rstRSSET4;
				setMSG("Generating Receipt.",'N');
				M_strSQLQRY = "Select ist_mnlcd,sum(ist_issqt) l_issqt,sum(ist_isspk) l_isspk";
				M_strSQLQRY += " from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_wrhtp='"+strWRHTP+"' and";
				M_strSQLQRY += " ist_isstp='"+strDGRTP+"' and ist_issno='"+txtDGRNO.getText().toString().trim()+"'";
				M_strSQLQRY += " group by ist_mnlcd";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET4 = cl_dat.exeSQLQRY2(M_strSQLQRY);
			//	System.out.println("Generation Receipt ISTRN:"+M_strSQLQRY);
				while(M_rstRSSET4.next()){
					strMNLCD = nvlSTRVL(M_rstRSSET4.getString("ist_mnlcd"),"");
					strSTKQT = nvlSTRVL(M_rstRSSET4.getString("l_issqt"),"0.000");
					strCALPK = nvlSTRVL(M_rstRSSET4.getString("l_isspk"),"0");
					M_strSQLQRY = "Select count(*) from fg_rctrn";
					M_strSQLQRY += " where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP = '"+strWRHTP+"'";
					M_strSQLQRY += " and RCT_RCTTP = '"+strDGRTP+"'";
					M_strSQLQRY += " and RCT_RCTNO = '"+txtDGRNO.getText().toString().trim()+"'";
					M_strSQLQRY += " and RCT_PRDTP = '"+strRCPTP+"'";
					M_strSQLQRY += " and RCT_LOTNO = '"+strRCLOT+"'";
					M_strSQLQRY += " and RCT_RCLNO = '00'";
					M_strSQLQRY += " and RCT_PKGTP = '01'";
					M_strSQLQRY += " and RCT_MNLCD = '"+strMNLCD+"'";
					//System.out.println(M_strSQLQRY);
					if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
						updRCTRN();
					else
						insRCTRN();
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					//cl_dat.exeSQLQRY(M_strSQLQRY);
				}
				
				if(M_rstRSSET4 != null)
					M_rstRSSET4.close();
                                if(rdbDGRAUT.isSelected())
								{
                                     for(int i=0;i<=(tblDGRTBL.getRowCount()-1);i++)
									 {                 
                                                if(getDGRVAL(i))
            									{ 
													M_strSQLQRY = "Select count(*) from fg_stmst";
													M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"'";
													M_strSQLQRY += " and ST_PRDTP = '"+strRCPTP+"'";
													M_strSQLQRY += " and ST_LOTNO = '"+strRCLOT+"'";
													M_strSQLQRY += " and ST_RCLNO = '00'";
													M_strSQLQRY += " and ST_PKGTP = '01'";
													M_strSQLQRY += " and ST_MNLCD = '"+strMNLCD+"'";
													//System.out.println(M_strSQLQRY);
							if(cl_dat.getRECCNT(M_strSQLQRY) > 0)
								incSTKQT();
							else
								insSTMST();//Stock Master insert*/
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							
						}
					}
				}
		}catch(Exception L_EX)
		 {
		    	setMSG(L_EX,"genRECPT");
		 }
		}
	}
	/**Updating the Recepit Number i.e FG_RCTRN
	 */
	private void updRCTRN()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				//System.out.println("updation RCTRN");
				M_strSQLQRY = "Update FG_RCTRN SET ";
				M_strSQLQRY += "RCT_RCTQT = "+strSTKQT+",";
				M_strSQLQRY += "RCT_RCTPK = "+strCALPK+",";
				M_strSQLQRY += "RCT_PKGCT = '01',";
				M_strSQLQRY += "RCT_SHFCD = 'G',";
				M_strSQLQRY += "RCT_STKTP = '1',";
				M_strSQLQRY += "RCT_AUTBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				if(setNumberFormat(Double.parseDouble(strSTKQT),3).equals("0.000"))
					M_strSQLQRY += "RCT_STSFL = 'X',";
				else
					M_strSQLQRY += "RCT_STSFL = '"+strSTSFL+"',";
				M_strSQLQRY += "RCT_TRNFL = '0',";
				M_strSQLQRY += "RCT_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "RCT_AUTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDGRDT.getText().trim()))+"',";
				M_strSQLQRY += "RCT_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY += " where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_WRHTP = '"+strWRHTP+"'";
				M_strSQLQRY += " and RCT_RCTTP = '"+strDGRTP+"'";
				M_strSQLQRY += " and RCT_RCTNO = '"+txtDGRNO.getText().toString().trim()+"'";
				M_strSQLQRY += " and RCT_PRDTP = '"+strRCPTP+"'";
				M_strSQLQRY += " and RCT_LOTNO = '"+strRCLOT+"'";
				M_strSQLQRY += " and RCT_RCLNO = '00'";
				M_strSQLQRY += " and RCT_PKGTP = '01'";
				M_strSQLQRY += " and RCT_MNLCD = '"+strMNLCD+"'";
			//	System.out.println("Updating Receipt NumberRCTRN:"+M_strSQLQRY);
				//cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
			}catch(Exception L_EX){
				setMSG(L_EX,"updRCTRN");
			}
		}
	}
	/**
	 * insert the values into i.e Table FG_RCTRN
	 */
	private void insRCTRN()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				//System.out.println("This is insRCTRN");
				M_strSQLQRY = "Insert into FG_RCTRN(RCT_CMPCD,RCT_WRHTP,RCT_RCTTP,RCT_RCTNO,RCT_PRDTP,";
				M_strSQLQRY += "RCT_RCTDT,RCT_LOTNO,RCT_RCLNO,RCT_MNLCD,RCT_STKTP,RCT_PKGTP,";
				M_strSQLQRY += "RCT_PKGCT,RCT_RCTQT,RCT_RCTPK,RCT_SHFCD,RCT_AUTBY,RCT_AUTDT,";
				M_strSQLQRY += "RCT_STSFL,RCT_TRNFL,RCT_LUSBY,RCT_LUPDT) values (";
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
				M_strSQLQRY += "'"+strWRHTP+"',";
				M_strSQLQRY += "'"+strDGRTP+"',";
				M_strSQLQRY += "'"+txtDGRNO.getText().toString().trim()+"',";
				M_strSQLQRY += "'"+strRCPTP+"',";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDGRDT.getText().trim()))+"',";
				M_strSQLQRY += "'"+strRCLOT+"',";
				M_strSQLQRY += "'00',";
				M_strSQLQRY += "'"+strMNLCD+"',";
				M_strSQLQRY += "'1',";
				M_strSQLQRY += "'01',";
				M_strSQLQRY += "'01',";
				M_strSQLQRY += strSTKQT+",";
				M_strSQLQRY += strCALPK+",";
				M_strSQLQRY += "'G',";
				M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDGRDT.getText().trim()))+"',";
				if(setNumberFormat(Double.parseDouble(strSTKQT),3).equals("0.000")){
					M_strSQLQRY += "'X',";
				}else
					M_strSQLQRY += "'"+strSTSFL+"',";
				M_strSQLQRY += "'0',";
				M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY += ")";
			//	System.out.println("insert into FG_RCTRN:" +M_strSQLQRY);
			//	cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
			}catch(Exception L_EX){
				setMSG(L_EX,"insRCTRN");
			}
		}
	}
	/**Update the stock Quantity i.e FG_STMST
	 */
	private void incSTKQT()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
			    //System.out.println(M_strSQLQRY);
				M_strSQLQRY = "Update FG_STMST SET ";
				M_strSQLQRY += "ST_UCLQT = ST_UCLQT + "+strSTKQT+",";
				M_strSQLQRY += "ST_TRNFL = '0',";
				M_strSQLQRY += "ST_LUSBY = '"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "ST_LUPDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_WRHTP = '"+strWRHTP+"'";
				M_strSQLQRY += " and ST_PRDTP = '"+strRCPTP+"'";
				M_strSQLQRY += " and ST_LOTNO = '"+strRCLOT+"'";
				M_strSQLQRY += " and ST_RCLNO = '00'";
				M_strSQLQRY += " and ST_PKGTP = '01'";
				M_strSQLQRY += " and ST_MNLCD = '"+strMNLCD+"'";
			//	System.out.println("Update the Stock Quantity"+M_strSQLQRY);
				//cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
		}catch(Exception L_EX){
			setMSG(L_EX,"incSTKQT");
			}
		}
	}
	
	/**
	 * Method to insert new stock into stock master
	 * */
	private void insSTMST()
	{
		if(cl_dat.M_flgLCUPD_pbst)
		{
			try
			{
				//System.out.println("this is Method to insert new stock master");
				strPRDCD = cl_cust.getPRDCD(strRCPTP,strRCLOT,"00");
				M_strSQLQRY = "Insert into FG_STMST(ST_CMPCD,ST_WRHTP,ST_PRDTP,ST_LOTNO,ST_RCLNO,ST_MNLCD,ST_STKQT,ST_ALOQT,";
				M_strSQLQRY += "ST_UCLQT,ST_DOSQT,ST_DOUQT,ST_PKGCT,ST_PKGTP,ST_PKGWT,ST_RCTDT,ST_STSFL,ST_RESFL,ST_RESCD,";
				M_strSQLQRY += "ST_RESDT,ST_REXDT,ST_REMDS,ST_TRNFL,ST_TPRCD,ST_CPRCD,ST_PRDCD,ST_LUSBY,ST_LUPDT) Values (";
				M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
				M_strSQLQRY += "'"+strWRHTP+"',";
				M_strSQLQRY += "'"+strRCPTP+"',";
				M_strSQLQRY += "'"+strRCLOT+"',";
				M_strSQLQRY += "'00',";
				M_strSQLQRY += "'"+strMNLCD+"',";
				M_strSQLQRY += "0.000,";
				M_strSQLQRY += "0.000,";
				M_strSQLQRY += strSTKQT+",";
				M_strSQLQRY += "0.000,";
				M_strSQLQRY += "0.000,";
				M_strSQLQRY += "'01',";
				M_strSQLQRY += "'01',";
				M_strSQLQRY += "0.025,";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDGRDT.getText().trim()))+"',";
				M_strSQLQRY += "'1',";
				M_strSQLQRY += "'0',";
				M_strSQLQRY += "' ',";
				M_strSQLQRY += "null,";
				M_strSQLQRY += "null,";
				M_strSQLQRY += "' ',";
				M_strSQLQRY += "'0',";
				M_strSQLQRY += "'"+strPRDCD+"',";
				M_strSQLQRY += "' ',";
				M_strSQLQRY += "'"+strPRDCD+"',";
				M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"',";
				M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
				M_strSQLQRY += ")";
				
				
			//	System.out.println("insert into stock Master:"+M_strSQLQRY);
			//	cl_dat.ocl_dat.M_STRSQL = M_strSQLQRY;
		}catch(Exception L_EX){
			setMSG(L_EX,"insSTMST");
			}
		}
	}
	
	/**
	 * @return void
	 * Updates Generated Issue No.
	 */
	private void updDGRNO(){ 
	   try{
		   
			//System.out.println("updated DgrNo");
		    M_strSQLQRY = "Update CO_CDTRN SET ";
			M_strSQLQRY += "CMT_CHP01 = 'NO',";
			M_strSQLQRY += "CMT_CCSVL = '"+strUPDNO+"'";
			M_strSQLQRY += "where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += "and CMT_CGSTP = 'FGXXDGR'";
			M_strSQLQRY += "and CMT_CODCD = '"+strCODCD+"'";
			
			
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			
			//System.out.println("Update DGRNO :"+M_strSQLQRY);
			if(cl_dat.M_flgLCUPD_pbst)
			{
				cl_dat.exeDBCMT("");
				cl_dat.exeDBCMT("");
				
			}
	}catch(Exception L_EX){
		   setMSG(L_EX,"updDGRNO");
	   }
   }
}


			
		
		
	