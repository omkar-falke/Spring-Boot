/*
System Name   : Finished Goods Inventory Management System
Program Name  : OverAll Summary
Program Desc. : Analysis of Bagging, Despatch & Stock for the selected day.
Author        : Mr. Deepal Mehta (Modified by Mr.Zheer)
Date          : 7th September 2006
Version       : FIMS 2.0.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/


import java.sql.ResultSet;import java.sql.PreparedStatement;
import java.util.*;import java.math.BigDecimal;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.JComponent;import java.awt.event.KeyEvent;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import javax.swing.JPanel;import javax.swing.JCheckBox;
import java.sql.Timestamp;

class fg_rprcn extends cl_rbase
{
	
	private JTextField txtWRHTP;
	private JTextField txtFMDAT;
    private	JCheckBox chkGRDTL;          /** String Variable for generated Report file Name.*/ 
	private String strFILNM;			 /** Integer Variable to count records fetched, to block report generation if data not found.*/
	private int intRECCT;				 /** FileOutputStream Object to generate the Report File from Stream of data.*/   
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
    private DataOutputStream dosREPORT ;
	private int intPAGENO=0;			/**String  variable for ISO value          */
	private String strISODC1="";        /**String  variable for ISO value          */
	private String strISODC2="";        /**String  variable for ISO value          */
	private String strISODC3="";                 /**Hashtable for storing general code  */												
	private Hashtable<String,String> hstGENCD = new Hashtable<String,String>();/**HashTable for storing selected Store type for printing     */   
		
	String   strPRDCT,strMBGQT,strMEDQT,strCODCD,strYEDQT,strYOSQT,strYSLRT,strYOSLR,strYBGAJ,strYSRED,strYBUQT;
    String   strPRDDS,strCODDS,strMDDQT,strYBGQT,strYDDQT,strTCSQT,strMCPCT,strYCPCT,strYSRDD,strYDSAJ,strMBCQT;
	String strPRDTP,strPRDTP1,strPRDTP2;
	String strSBGRP,strSBGRP1,strSBGRP2;
	String strPKGTP,strPKGTP1,strPKGTP2;
	String strPRDCD,strPRDCD1,strPRDCD2;
	
	double dblSUMQT;
	double dblPKGTOT = 0;
	double dblSGRTOT = 0;
	double dblMGRTOT = 0;
	double dblALLTOT = 0;
	double dblPRDTOT = 0;
	private String strFMDAT="";
	private String strREFDT="";
	private JCheckBox chkXPS;	
	PreparedStatement prpSTAMT,prpSTAMT1;
	ResultSet prpRSLSET,prpRSLSET1;
	
	String STRDOTLN="------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
	fg_rprcn()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Ware House Type"),3,3,1,2,this,'L');
			add(txtWRHTP = new TxtLimit(2),3,5,1,1,this,'R');
			add(chkGRDTL = new JCheckBox("Grade Wise"),3,6,1,2,this,'L');
			add(new JLabel("Date"),4,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),4,5,1,1,this,'R');
			chkXPS = new JCheckBox("XPS Report");
			add(chkXPS,3,2,1,2,this,'L');
			
			M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ( 'ISOFGXXRPT')";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			
			if(M_rstRSSET != null)   
			{
		    	while(M_rstRSSET.next())
        		{   
					hstGENCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
			}
			strISODC1 = hstGENCD.get("FG_RPRCM01").toString();
			strISODC2 = hstGENCD.get("FG_RPRCM02").toString();
			strISODC3 = hstGENCD.get("FG_RPRCM03").toString();
			//System.out.println("strISODC1 = "+strISODC1);		
		//	System.out.println("strISODC2 = "+strISODC2);
		//	System.out.println("strISODC3 = "+strISODC3);
			Date L_strTEMP=null;
			M_strSQLQRY = "Select CMT_CCSVL,CMT_CHP01,CMT_CHP02 from CO_CDTRN where CMT_CGMTP='S"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'FGXXREF' and CMT_CODCD='DOCDT'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				strREFDT = L_rstRSSET.getString("CMT_CCSVL").trim();
				L_rstRSSET.close();
				M_calLOCAL.setTime(M_fmtLCDAT.parse(strREFDT));       // Convert Into Local Date Format
				M_calLOCAL.add(Calendar.DATE,+0);                     // Increase Date from +1 with Locked Date
				strREFDT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
				//System.out.println("REFDT = "+strREFDT);
				
			}
			txtFMDAT.setText(strREFDT);
			M_pnlRPFMT.setVisible(true);
			chkGRDTL.setSelected(false);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		txtFMDAT.setText(strREFDT);
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtWRHTP)
			{
				M_strHLPFLD  = "txtWRHTP";
				M_strSQLQRY = "Select CMT_CODCD, CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXPTD'";
				if(txtWRHTP.getText().trim().length() > 0)
					M_strSQLQRY += " AND CMT_CODCD LIKE '"+txtWRHTP.getText().trim().toUpperCase()+"%'";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Ware House Type","Description"},2,"CT");
			}
		}
		
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC == txtWRHTP)
				{
					M_strSQLQRY = "Select CMT_CODCD, CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='FGXXPTD' AND CMT_CODCD='"+txtWRHTP.getText().trim()+"'";
					M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET!=null && M_rstRSSET.next())
					{
						txtFMDAT.requestFocus();
					}
					else
					{
						setMSG("Invalid Ware House Type ",'E');
						txtWRHTP.requestFocus();
					}
				}
				if(M_objSOURC == txtFMDAT)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
		
			catch(Exception E)
			{
				setMSG(E,"Key Pressed");
			}
		}
	}
		
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtWRHTP")
		{
			txtWRHTP.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
	
	
	
	/**
		*Method to print, display report as per selection
	*/
	public void exePRINT()
	{
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rprcn.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rprcn.doc";
			delSMWRK();
			updDATA();
			getDATA();
		
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
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{					
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM," Stock Summary"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	/**
	 *  Fetches all the records from the Different table 
	 */
	private void updDATA()
	{
		try
		{
			//System.out.println("IN UPD DATA");
			
			strFMDAT=txtFMDAT.getText().trim();
			String L_strMSTDT="01"+strFMDAT.substring(2,10);
			String L_strYSTDT=cl_dat.M_strYSTDT_pbst;
			//System.out.println(" strFMDAT = "+strFMDAT);
			//System.out.println(" L_strMSTDT = "+L_strMSTDT);
			//System.out.println(" L_strYSTDT = "+L_strYSTDT);
			M_strSQLQRY = "Select * from FG_SMWRK where SM_CMPCD = ? and SM_PRDCT = ? and SM_PRDCD = ?   and  SUBSTRING(ltrim(str(SM_PRDCD,20,0)),1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'");
			prpSTAMT = cl_dat.M_conSPDBA_pbst.prepareStatement(M_strSQLQRY,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			
			if(chkGRDTL.isSelected())
			{
				System.out.println("Gradewise Summary");
				setMSG("Updating monthly bagging detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt,lt_prdcd l_prdcd from fg_rctrn,pr_ltmst where RCT_CMPCD=LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'   and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'")+" and RCT_STSFL = '2'";
                                M_strSQLQRY += " and RCT_RCTTP in ('10','15','21','22','23')";
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp,lt_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("1>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_MBGQT");
				
				if(strFMDAT.equals(strREFDT))
				{
					setMSG("Updating unclassified stock detail",'N');
					M_strSQLQRY = "Select st_prdtp l_prdtp,SUBSTRING(st_prdcd,1,4) l_sbgrp,st_pkgtp l_pkgtp,sum(st_douqt)";
					M_strSQLQRY += " l_sumqt,st_prdcd l_prdcd from fg_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND st_douqt > 0   and  SUBSTRING(ST_PRDCD,1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'");
					M_strSQLQRY += " group by st_prdtp,SUBSTRING(st_prdcd,1,4),st_pkgtp,st_prdcd";
					M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
					System.out.println("2>>"+M_strSQLQRY);
					exeDBSQRY1(M_strSQLQRY,"SM_YBUQT");
				}
					
				setMSG("Updating yearly bagging detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt,lt_prdcd l_prdcd from fg_rctrn,pr_ltmst where RCT_CMPCD = LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'   and  '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'";
				M_strSQLQRY += " and RCT_STSFL = '2' and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
                                M_strSQLQRY += " and RCT_RCTTP in ('10','15','21','22','23')";
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp,lt_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("3>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YBGQT");
					
				setMSG("Updating yearly sales return bagging detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt,lt_prdcd l_prdcd from fg_rctrn,pr_ltmst where RCT_CMPCD = LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and RCT_STSFL = '2'   and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " and RCT_RCTTP = '30' ";
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp,lt_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("4>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YSLRT");
					
				setMSG("Updating monthly domestic despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_ISSTP = '10' and";
                M_strSQLQRY += " IST_SALTP not in ('12','16') and IST_STSFL='2'   and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				
				System.out.println("5>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_MDDQT");
					
				setMSG("Updating yearly domestic despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_ISSTP = '10' and";
                                M_strSQLQRY += " IST_SALTP not in ('12','16') and IST_STSFL='2'   and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("6>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YDDQT");
					
				setMSG("Updating monthly export despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_ISSTP = '10' and";
                                M_strSQLQRY += " IST_SALTP = '12' and IST_STSFL='2'    and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("7>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_MEDQT");
					
				setMSG("Updating yearly export despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_ISSTP = '10' and";
                                M_strSQLQRY += " IST_SALTP = '12' and IST_STSFL='2'    and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("8>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YEDQT");
					
				setMSG("Updating monthly captive consumption detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_MKTTP = '03' and";
                                M_strSQLQRY += " IST_SALTP = '16' and IST_STSFL='2'    and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("9>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_MCPCT");
						
				setMSG("Updating yearly captive consumption detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_MKTTP = '03' and";
                                M_strSQLQRY += " IST_SALTP = '16' and IST_STSFL='2'";
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("10>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YCPCT");
				
				setMSG("Updating yearly bagging adjustment detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt,lt_prdcd l_prdcd from fg_rctrn,pr_ltmst where RCT_CMPCD = LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and RCT_STSFL = '2'    and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " and RCT_RCTTP in ('50','16','51','61','40')";
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp,lt_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("11>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YBGAJ");
					
				setMSG("Updating yearly sales return domestic despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'  and IST_ISSTP = '30'";
                                M_strSQLQRY += " and IST_SALTP not in ('12','16') and IST_STSFL='2' and IST_STKTP='2' and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("12>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YSRDD");
					
				setMSG("Updating yearly sales return export despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'  and IST_ISSTP = '30' and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
                                M_strSQLQRY += " and IST_SALTP = '12' and IST_STSFL='2' and IST_STKTP='2'";
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("13>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YSRED");
		
				setMSG("Updating yearly despatch adjustment detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt,ist_prdcd l_prdcd from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' ";
				M_strSQLQRY += " and IST_ISSTP  in ('50','16','51','61','40') and IST_STSFL='2' and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp,ist_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("14>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YDSAJ");
					
				setMSG("Updating year opening stock",'N');
				M_strSQLQRY = "Select op_prdtp l_prdtp,SUBSTRING(op_prdcd,1,4) l_sbgrp,op_pkgtp l_pkgtp,sum(op_yosqt)";
				M_strSQLQRY += " l_sumqt,op_prdcd l_prdcd from fg_opstk where OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OP_PRDCD is not null and OP_PRDCD <> ' '   and  SUBSTRING(OP_PRDCD,1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'");
				M_strSQLQRY += " group by op_prdtp,SUBSTRING(op_prdcd,1,4),op_pkgtp,op_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("15>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YOSQT");
							
				setMSG("Updating year opening sales return stock",'N');
				M_strSQLQRY = "Select op_prdtp l_prdtp,SUBSTRING(op_prdcd,1,4) l_sbgrp,op_pkgtp l_pkgtp,sum(op_yoxqt)";
				M_strSQLQRY += " l_sumqt,op_prdcd l_prdcd from fg_opstk where OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OP_PRDCD is not null and OP_PRDCD <> ' '  and  SUBSTRING(OP_PRDCD,1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'");
				M_strSQLQRY += " group by op_prdtp,SUBSTRING(op_prdcd,1,4),op_pkgtp,op_prdcd";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp,l_prdcd";
				System.out.println("16>>"+M_strSQLQRY);
				exeDBSQRY1(M_strSQLQRY,"SM_YOSLR");
			}
			else
			{
	
				System.out.println("Categorywise summary");
				setMSG("Updating monthly classified bagging detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt from fg_rctrn,pr_ltmst where RCT_CMPCD = LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and RCT_STSFL = '2'  and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
                                M_strSQLQRY += " and RCT_RCTTP in ('10','15','21','22','23')";
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("18>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_MBGQT");
					
				if(strFMDAT.equals(strREFDT))
				{
					setMSG("Updating unclassified stock detail",'N');
					M_strSQLQRY = "Select st_prdtp l_prdtp,SUBSTRING(st_prdcd,1,4) l_sbgrp,st_pkgtp l_pkgtp,sum(st_douqt)";
					M_strSQLQRY += " l_sumqt from fg_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND st_douqt > 0  and  ST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
					M_strSQLQRY += " group by st_prdtp,SUBSTRING(st_prdcd,1,4),st_pkgtp";
					M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
					exeDBSQRY(M_strSQLQRY,"SM_YBUQT");
				}
					
				setMSG("Updating yearly bagging detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt from fg_rctrn,pr_ltmst where RCT_CMPCD = LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'";
				M_strSQLQRY += "  and RCT_STSFL = '2'  and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
                M_strSQLQRY += " and RCT_RCTTP in ('10','15','21','22','23')";
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("19>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YBGQT");
					
				setMSG("Updating yearly sales return bagging detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt from fg_rctrn,pr_ltmst where RCT_CMPCD = LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and RCT_STSFL = '2'";
				M_strSQLQRY += " and RCT_RCTTP = '30'  and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				exeDBSQRY(M_strSQLQRY,"SM_YSLRT");
					
				setMSG("Updating monthly domestic despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_ISSTP = '10' and ";
                                M_strSQLQRY += " IST_SALTP not in ('12','16') and IST_STSFL='2'  and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("20>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_MDDQT");
					
				setMSG("Updating yearly domestic despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'  and IST_ISSTP = '10' and";
                                M_strSQLQRY += " IST_SALTP not in ('12','16') and IST_STSFL='2' and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("21>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YDDQT");
					
				setMSG("Updating monthly export despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"' " ;
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_ISSTP = '10' and";
                                M_strSQLQRY += " IST_SALTP = '12' and IST_STSFL='2'  and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("22>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_MEDQT");
					
				setMSG("Updating yearly export despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_ISSTP = '10' and";
                M_strSQLQRY += " IST_SALTP = '12' and IST_STSFL='2'  and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("23>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YEDQT");
					
				setMSG("Updating monthly captive consumption detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strMSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_MKTTP = '03' and";
                                M_strSQLQRY += " IST_SALTP = '16' and IST_STSFL='2'  and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("24>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_MCPCT");
						
				setMSG("Updating yearly captive consumption detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+" ' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and IST_MKTTP = '03' and";
                                M_strSQLQRY += " IST_SALTP = '16' and IST_STSFL='2'  and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("25>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YCPCT");
							
				setMSG("Updating yearly bagging adjustment detail",'N');
				M_strSQLQRY = "Select rct_prdtp l_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4) l_sbgrp,rct_pkgtp l_pkgtp,sum(rct_rctqt)";
				M_strSQLQRY += " l_sumqt from fg_rctrn,pr_ltmst where RCT_CMPCD=LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and";
				M_strSQLQRY += " RCT_RCLNO=LT_RCLNO and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"' and RCT_STSFL = '2'  and  RCT_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " and RCT_RCTTP in ('50','16','51','61','40')";
				M_strSQLQRY += " group by rct_prdtp,SUBSTRING(ltrim(str(lt_prdcd,20,0)),1,4),rct_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("26>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YBGAJ");
					
				setMSG("Updating yearly sales return domestic despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+" ' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'  and IST_ISSTP = '30'";
                                M_strSQLQRY += " and IST_SALTP not in ('12','16') and IST_STSFL='2' and IST_STKTP='2'  and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("27>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YSRDD");
					
				setMSG("Updating yearly sales return export despatch detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"'";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'  and IST_ISSTP = '30'";
                                M_strSQLQRY += " and IST_SALTP = '12' and IST_STSFL='2' and IST_STKTP='2'  and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("28>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YSRED");
		
				setMSG("Updating yearly despatch adjustment detail",'N');
				M_strSQLQRY = "Select ist_prdtp l_prdtp,SUBSTRING(ist_prdcd,1,4) l_sbgrp,ist_pkgtp l_pkgtp,sum(ist_issqt)";
				M_strSQLQRY += " l_sumqt from fg_istrn where IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_AUTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strYSTDT))+"' ";
				M_strSQLQRY += " and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strFMDAT))+"'";
				M_strSQLQRY += " and IST_ISSTP  in ('50','16','51','61','40') and IST_STSFL='2'   and  IST_PRDTP "+(chkXPS.isSelected() ? " = '07' " : " <> '07'");
				M_strSQLQRY += " group by ist_prdtp,SUBSTRING(ist_prdcd,1,4),ist_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("29>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YDSAJ");
					
				setMSG("Updating year opening stock",'N');
				M_strSQLQRY = "Select op_prdtp l_prdtp,SUBSTRING(op_prdcd,1,4) l_sbgrp,op_pkgtp l_pkgtp,sum(op_yosqt)";
				M_strSQLQRY += " l_sumqt from fg_opstk where OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OP_PRDCD is not null and OP_PRDCD <> ' '   and  SUBSTRING(OP_PRDCD,1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'");
				M_strSQLQRY += " group by op_prdtp,SUBSTRING(op_prdcd,1,4),op_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("30>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YOSQT");
							
				setMSG("Updating year opening sales return stock",'N');
				M_strSQLQRY = "Select op_prdtp l_prdtp,SUBSTRING(op_prdcd,1,4) l_sbgrp,op_pkgtp l_pkgtp,sum(op_yoxqt)";
				M_strSQLQRY += " l_sumqt from fg_opstk where OP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND OP_PRDCD is not null and OP_PRDCD <> ' '   and  SUBSTRING(OP_PRDCD,1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'");
				M_strSQLQRY += " group by op_prdtp,SUBSTRING(op_prdcd,1,4),op_pkgtp";
				M_strSQLQRY += " order by l_prdtp,l_sbgrp,l_pkgtp";
				System.out.println("31>>"+M_strSQLQRY);
				exeDBSQRY(M_strSQLQRY,"SM_YOSLR");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updDATA");
		}
	}
		
	/**
	 * 
	 */
	private void exeDBSQRY(String LP_STRQRY,String P_strFLDNM)
	{
		try
		{
			boolean L_1STFL = true;
			boolean L_EOF = false;
			dblPKGTOT = 0;
			dblSGRTOT = 0;
			dblMGRTOT = 0;
			dblALLTOT = 0;
			prpSTAMT1 = cl_dat.M_conSPDBA_pbst.prepareStatement(LP_STRQRY);
			prpRSLSET1 = prpSTAMT1.executeQuery();
			if(prpRSLSET1.next())
			{
				strPRDTP2 = nvlSTRVL(prpRSLSET1.getString("l_prdtp"),"");
				strSBGRP2 = nvlSTRVL(prpRSLSET1.getString("l_sbgrp"),"");
				strPKGTP2 = nvlSTRVL(prpRSLSET1.getString("l_pkgtp"),"");
				dblSUMQT = prpRSLSET1.getDouble("l_sumqt");
				while(!L_EOF)
				{
					strPRDTP = strPRDTP2;
					strSBGRP = strSBGRP2;
					strPKGTP = strPKGTP2;
					if(L_1STFL)
					{
						strPRDTP1 = strPRDTP;
						strSBGRP1 = strSBGRP;
						strPKGTP1 = strPKGTP;
						L_1STFL = false;
					}
					strPRDTP1 = strPRDTP;
					while(strPRDTP.equals(strPRDTP1) && !L_EOF)
					{
						strPRDTP = strPRDTP2;
						strPRDTP1 = strPRDTP;
						while((strPRDTP+strSBGRP).equals(strPRDTP1+strSBGRP1) && !L_EOF)
						{
							strPRDTP = strPRDTP2;
							strPRDTP1 = strPRDTP;
							strSBGRP = strSBGRP2;
							strSBGRP1 = strSBGRP;
							while((strPRDTP+strSBGRP+strPKGTP).equals(strPRDTP1+strSBGRP1+strPKGTP1) && !L_EOF)
							{
								
								/*dblPKGTOT = Double.parseDouble(new BigDecimal(dblPKGTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());
								dblSGRTOT = Double.parseDouble(new BigDecimal(dblSGRTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());
								dblMGRTOT = Double.parseDouble(new BigDecimal(dblMGRTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());
								dblALLTOT = Double.parseDouble(new BigDecimal(dblALLTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());*/
								
								dblPKGTOT += dblSUMQT;
								dblSGRTOT += dblSUMQT;
								dblMGRTOT += dblSUMQT;
								dblALLTOT += dblSUMQT;
								
								if(!prpRSLSET1.next())
								{
									L_EOF = true;
									break;
								}
								
								strPRDTP2 = nvlSTRVL(prpRSLSET1.getString("l_prdtp"),"");
								strSBGRP2 = nvlSTRVL(prpRSLSET1.getString("l_sbgrp"),"");
								strPKGTP2 = nvlSTRVL(prpRSLSET1.getString("l_pkgtp"),"");
								dblSUMQT = prpRSLSET1.getDouble("l_sumqt");
								
								strPRDTP = strPRDTP2;
								strSBGRP = strSBGRP2;
								strPKGTP = strPKGTP2;
							}
							prnGRPTOT("PK",dblPKGTOT,P_strFLDNM);
							intGRPTOT("PK");
						}
						prnGRPTOT("SG",dblSGRTOT,P_strFLDNM);
						intGRPTOT("SG");
					}
					prnGRPTOT("MG",dblMGRTOT,P_strFLDNM);
					intGRPTOT("MG");
				}
				prnGRPTOT("GT",dblALLTOT,P_strFLDNM);
			}
			if(prpRSLSET1 != null)
				prpRSLSET1.close();
			prpSTAMT1.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeDBSQRY");
		}
	}
	
	/**
	 * Function to Fetch Record Fron 
	 */
	private void exeDBSQRY1(String LP_STRQRY,String P_strFLDNM)
	{
		try
		{
			boolean L_1STFL = true;
			boolean L_EOF = false;
			dblPKGTOT = 0;
			dblSGRTOT = 0;
			dblMGRTOT = 0;
			dblALLTOT = 0;
			dblPRDTOT = 0;
			prpSTAMT1 = cl_dat.M_conSPDBA_pbst.prepareStatement(LP_STRQRY);
			prpRSLSET1 = prpSTAMT1.executeQuery();
			if(prpRSLSET1.next())
			{
				strPRDTP2 = nvlSTRVL(prpRSLSET1.getString("l_prdtp"),"");
				strSBGRP2 = nvlSTRVL(prpRSLSET1.getString("l_sbgrp"),"");
				strPKGTP2 = nvlSTRVL(prpRSLSET1.getString("l_pkgtp"),"");
				strPRDCD2 = nvlSTRVL(prpRSLSET1.getString("l_prdcd"),"");
				dblSUMQT = prpRSLSET1.getDouble("l_sumqt");
				while(!L_EOF)
				{
					strPRDTP = strPRDTP2;
					strSBGRP = strSBGRP2;
					strPKGTP = strPKGTP2;
					strPRDCD = strPRDCD2;
					if(L_1STFL)
					{
						strPRDTP1 = strPRDTP;
						strSBGRP1 = strSBGRP;
						strPKGTP1 = strPKGTP;
						strPRDCD1 = strPRDCD;
						L_1STFL = false;
					}
					strPRDTP1 = strPRDTP;
					while(strPRDTP.equals(strPRDTP1) && !L_EOF)
					{
						strPRDTP = strPRDTP2;
						strPRDTP1 = strPRDTP;
						while((strPRDTP+strSBGRP).equals(strPRDTP1+strSBGRP1) && !L_EOF)
						{
							strPRDTP = strPRDTP2;
							strPRDTP1 = strPRDTP;
							strSBGRP = strSBGRP2;
							strSBGRP1 = strSBGRP;
							while((strPRDTP+strSBGRP+strPKGTP).equals(strPRDTP1+strSBGRP1+strPKGTP1) && !L_EOF)
							{
								strPRDTP = strPRDTP2;
								strPRDTP1 = strPRDTP;
								strSBGRP = strSBGRP2;
								strSBGRP1 = strSBGRP;
								strPKGTP = strPKGTP2;
								strPKGTP1 = strPKGTP;
								while((strPRDTP+strSBGRP+strPKGTP+strPRDCD).equals(strPRDTP1+strSBGRP1+strPKGTP1+strPRDCD1) && !L_EOF)
								{
									/*dblPKGTOT = Double.parseDouble(new BigDecimal(dblPKGTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());
									dblSGRTOT = Double.parseDouble(new BigDecimal(dblSGRTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());
									dblMGRTOT = Double.parseDouble(new BigDecimal(dblMGRTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());
									dblALLTOT = Double.parseDouble(new BigDecimal(dblALLTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());
									dblPRDTOT = Double.parseDouble(new BigDecimal(dblPRDTOT).toString()) + Double.parseDouble(new BigDecimal(dblSUMQT).toString());*/
									
									dblPKGTOT += dblSUMQT;
									dblSGRTOT += dblSUMQT;
									dblMGRTOT += dblSUMQT;
									dblALLTOT += dblSUMQT;
									dblPRDTOT += dblSUMQT;
									
									if(!prpRSLSET1.next())
									{
										L_EOF = true;
										break;
									}
								
									strPRDTP2 = nvlSTRVL(prpRSLSET1.getString("l_prdtp"),"");
									strSBGRP2 = nvlSTRVL(prpRSLSET1.getString("l_sbgrp"),"");
									strPKGTP2 = nvlSTRVL(prpRSLSET1.getString("l_pkgtp"),"");
									strPRDCD2 = nvlSTRVL(prpRSLSET1.getString("l_prdcd"),"");
									dblSUMQT = prpRSLSET1.getDouble("l_sumqt");
								
									strPRDTP = strPRDTP2;
									strSBGRP = strSBGRP2;
									strPKGTP = strPKGTP2;
									strPRDCD = strPRDCD2;
								}
								prnGRPTOT("PR",dblPRDTOT,P_strFLDNM);
								intGRPTOT("PR");
							}
							prnGRPTOT("PK",dblPKGTOT,P_strFLDNM);
							intGRPTOT("PK");
						}
						prnGRPTOT("SG",dblSGRTOT,P_strFLDNM);
						intGRPTOT("SG");
					}
					prnGRPTOT("MG",dblMGRTOT,P_strFLDNM);
					intGRPTOT("MG");
				}
				prnGRPTOT("GT",dblALLTOT,P_strFLDNM);
			}
			if(prpRSLSET1 != null)
				prpRSLSET1.close();
			prpSTAMT1.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeDBSQRY1");
		}
	}
	
	//private void prnGRPTOT(String LP_GRPCT,double LP_SUMQT,String LP_FLDNM)
	private void prnGRPTOT(String P_strGRPCT,double P_dblSUMQT,String P_strFLDNM)		
	{
		//String L_PRDCT = "";
		String L_strPRDCT = "";
		//String L_PRDCD = "";
		String L_strPRDCD = "";
		try
		{
			if(P_strGRPCT.equals("PK"))
			{
				L_strPRDCT = strPRDTP1+strSBGRP1+strPKGTP1;
                L_strPRDCD = L_strPRDCT;
			}
			else if(P_strGRPCT.equals("SG"))
			{
				L_strPRDCT = strPRDTP1+strSBGRP1+"00";
                L_strPRDCD = L_strPRDCT;
			}
			else if(P_strGRPCT.equals("MG"))
			{
				L_strPRDCT = strPRDTP1+"000000";
                L_strPRDCD = L_strPRDCT;
			}
			else if(P_strGRPCT.equals("GT"))
			{
				L_strPRDCT = "00000000";
                L_strPRDCD = L_strPRDCT;
			}
			else if(P_strGRPCT.equals("PR"))
			{
				L_strPRDCT = strPRDTP1+strSBGRP1+strPKGTP1;
                L_strPRDCD = strPRDCD1;
			}
			String L_SUMQT = setNumberFormat(P_dblSUMQT,3);
			//System.out.println(" L_SUMQT = "+L_SUMQT);
			P_dblSUMQT = Double.parseDouble(L_SUMQT);
			prpSTAMT.setString(1,cl_dat.M_strCMPCD_pbst);
			prpSTAMT.setString(2,L_strPRDCT);
            prpSTAMT.setString(3,L_strPRDCD);
			//System.out.println("\n prpSTAMT = "+prpSTAMT);
            prpRSLSET = prpSTAMT.executeQuery();
            if(prpRSLSET.next())
			{
                prpRSLSET.updateDouble(P_strFLDNM,P_dblSUMQT);
                prpRSLSET.updateRow();
            }
			else
			{
                prpRSLSET.moveToInsertRow();
                prpRSLSET.updateString("SM_CMPCD",cl_dat.M_strCMPCD_pbst);
                prpRSLSET.updateString("SM_PRDCT",L_strPRDCT);
                prpRSLSET.updateString("SM_PRDCD",L_strPRDCD);
                prpRSLSET.updateDouble(P_strFLDNM,P_dblSUMQT);
                prpRSLSET.insertRow();
                prpRSLSET.moveToCurrentRow();
            }
            prpSTAMT.clearParameters();
            if(prpRSLSET != null)
                prpRSLSET.close();
            cl_dat.M_conSPDBA_pbst.commit();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPTOT");
		}
	}
	
	private void intGRPTOT(String P_strGRPCT)
	{
		try
		{
			if(P_strGRPCT.equals("PK"))
			{
				dblPKGTOT = 0;
				strPKGTP1 = strPKGTP;
			}
			else if(P_strGRPCT.equals("SG"))
			{
				dblSGRTOT = 0;
				strSBGRP1 = strSBGRP;
			}
			else if(P_strGRPCT.equals("MG"))
			{
				dblMGRTOT = 0;
				strPRDTP1 = strPRDTP;
			}
			else if(P_strGRPCT.equals("PR"))
			{
				dblPRDTOT = 0;
				strPRDCD1 = strPRDCD;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"intGRPTOT");
		}
	}
			
	/**
	 *Method to fetch Data from Database and start creation of the output file.
	*/
	private void getDATA()
	{
		
		try
		{
			//System.out.println("IN Get DATA After MOD");
			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>UnClassified Stock Statement</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();	//gets the Header of the report		
			
    		setMSG("Report Generation is in Progress.......",'N');			   			
			
			M_strSQLQRY = "Select SM_PRDCT,SM_PRDCD,SM_MBGQT,SM_YBUQT,SM_MDDQT,SM_MEDQT,SM_MCPCT,SM_YBGQT,SM_YSLRT,SM_YBGAJ,SM_YDDQT,SM_YEDQT,SM_YCPCT,SM_YOSQT,SM_YSRDD,SM_YSRED,SM_YDSAJ,SM_YOSLR  from FG_SMWRK  where  SUBSTRING(ltrim(str(SM_PRDCD,20,0)),1,2) "+(chkXPS.isSelected() ? " = 'SX' " : " <> 'SX'")+"" ;
			M_strSQLQRY += " order by SM_PRDCT,SM_PRDCD,SM_MBGQT,SM_MDDQT,SM_MEDQT,SM_MCPCT,SM_YBGQT,SM_YOSLR,SM_YSLRT,SM_YSRDD,SM_YSRED,SM_YDDQT,SM_YEDQT,SM_YCPCT,SM_YBUQT"; 
			System.out.println("M_strSQLQRY>>>>>>>>>>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);	
			while(M_rstRSSET.next())
			{
				//System.out.println("IN WHILE ="+intRECCT);
				intRECCT++;
				strPRDCT = M_rstRSSET.getString("SM_PRDCT");
				strPRDCD = M_rstRSSET.getString("SM_PRDCD");
				strMBGQT = M_rstRSSET.getString("SM_MBGQT");
				//strMBCQT = M_rstRSSET.getString("SM_MBCQT");
				strYBUQT = M_rstRSSET.getString("SM_YBUQT");
				strMDDQT = M_rstRSSET.getString("SM_MDDQT");
				strMEDQT = M_rstRSSET.getString("SM_MEDQT");
				strMCPCT = M_rstRSSET.getString("SM_MCPCT");
				strYBGQT = M_rstRSSET.getString("SM_YBGQT");
				strYSLRT = M_rstRSSET.getString("SM_YSLRT");
				strYBGAJ = M_rstRSSET.getString("SM_YBGAJ");
				strYDDQT = M_rstRSSET.getString("SM_YDDQT");
				strYEDQT = M_rstRSSET.getString("SM_YEDQT");
				strYCPCT = M_rstRSSET.getString("SM_YCPCT");
				strYOSQT = M_rstRSSET.getString("SM_YOSQT");
				strYOSLR = M_rstRSSET.getString("SM_YOSLR");
				strYSRDD = M_rstRSSET.getString("SM_YSRDD");
				strYSRED = M_rstRSSET.getString("SM_YSRED");
				strYDSAJ = M_rstRSSET.getString("SM_YDSAJ");
				strCODDS = getCODDS();
				setMSG("Fetching: "+strCODDS,'N');
				if(cl_dat.M_intLINNO_pbst > 60)
				{
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst+=1;
					dosREPORT.writeBytes(padSTRING('R',STRDOTLN,228));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst+=1;
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					prnHEADER();
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst += 1;
				if(strPRDCT.trim().equals(strPRDCD.trim()))
				{
					if(strCODCD.trim().equals("00000000"))
					{
						dosREPORT.writeBytes ("\n");
						cl_dat.M_intLINNO_pbst += 1;
						//dosREPORT.writeBytes(padSTRING('R'," ",1));
						dosREPORT.writeBytes(padSTRING('R',strCODDS,15));
					}
					else if(strCODCD.substring(2,8).trim().equals("000000"))
					{
						dosREPORT.writeBytes ("\n");
						cl_dat.M_intLINNO_pbst += 1;
						//dosREPORT.writeBytes(padSTRING('R'," ",1));
						dosREPORT.writeBytes(padSTRING('R',strCODDS,15));
					}
					else if(strCODCD.substring(6,8).trim().equals("00"))
					{
						dosREPORT.writeBytes ("\n");
						cl_dat.M_intLINNO_pbst += 1;
						//dosREPORT.writeBytes(padSTRING('R'," ",3));
						dosREPORT.writeBytes(padSTRING('R',strCODDS,15));
					}
					else
					{
						dosREPORT.writeBytes(padSTRING('R'," ",5));
						dosREPORT.writeBytes(padSTRING('R',strCODDS,10));
					}
				}
				else
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					strPRDDS = getPRDDS(strPRDCD);
					dosREPORT.writeBytes(padSTRING('R',strPRDDS,15));
					//dosREPORT.writeBytes(padSTRING('R',strPRDCD,15));
				}
				strMBGQT = getDASH(strMBGQT);
				dosREPORT.writeBytes(padSTRING('L',strMBGQT,11));
				//strMBCQT = getDASH(strMBCQT);
				//dosREPORT.writeBytes(padSTRING('L',strMBCQT,10));
				strMDDQT = getDASH(strMDDQT);
				dosREPORT.writeBytes(padSTRING('L',strMDDQT,12));
				strMEDQT = getDASH(strMEDQT);
				dosREPORT.writeBytes(padSTRING('L',strMEDQT,12));
				strMCPCT = getDASH(strMCPCT);
				dosREPORT.writeBytes(padSTRING('L',strMCPCT,16));
				strYOSQT = getDASH(strYOSQT);
				dosREPORT.writeBytes(padSTRING('L',strYOSQT,16));
				strYBGAJ = getDASH(strYBGAJ);
				dosREPORT.writeBytes(padSTRING('L',strYBGAJ,12));
				strYBGQT = getDASH(strYBGQT);
				dosREPORT.writeBytes(padSTRING('L',strYBGQT,12));
				strYDDQT = getDASH(strYDDQT);
				dosREPORT.writeBytes(padSTRING('L',strYDDQT,12));
				strYEDQT = getDASH(strYEDQT);
				dosREPORT.writeBytes(padSTRING('L',strYEDQT,12));
				strYCPCT = getDASH(strYCPCT);
				dosREPORT.writeBytes(padSTRING('L',strYCPCT,16));
				strYDSAJ = getDASH(strYDSAJ);
				dosREPORT.writeBytes(padSTRING('L',strYDSAJ,12));
				strYOSLR = getDASH(strYOSLR);
				dosREPORT.writeBytes(padSTRING('L',strYOSLR,16));
				strYSLRT = getDASH(strYSLRT);
				dosREPORT.writeBytes(padSTRING('L',strYSLRT,10));
				strYSRDD = getDASH(strYSRDD);
				dosREPORT.writeBytes(padSTRING('L',strYSRDD,10));
				strYSRED = getDASH(strYSRED);
				dosREPORT.writeBytes(padSTRING('L',strYSRED,12));
				strTCSQT = getTDYST(strYOSQT,strYBGQT,strYSLRT,strYBGAJ,strYDDQT,strYEDQT,strYCPCT,strYOSLR,strYSRDD,strYSRED,strYDSAJ);
				strTCSQT = getDASH(strTCSQT);
				dosREPORT.writeBytes(padSTRING('L',strTCSQT,12));
				strYBUQT = getDASH(strYBUQT);
				dosREPORT.writeBytes(padSTRING('L',strYBUQT,10));
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			
			
			dosREPORT.writeBytes ("\n");
			cl_dat.M_intLINNO_pbst+=1;
			//crtLINE(224);
			dosREPORT.writeBytes(padSTRING('R',STRDOTLN,228));
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			dosREPORT.writeBytes(padSTRING('R',"Stock as on :"+cl_dat.M_strLOGDT_pbst+ "  " + cl_dat.M_txtCLKTM_pbst.getText() + " Hrs.",50));
			//cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText()))+"',";
			dosREPORT.writeBytes(padSTRING('L',"(Qty. in MTs.)" ,20));
			dosREPORT.writeBytes("\n System generated report, hence signature not required \n");
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX  + " GetDATA",'E');
		}
	}
	
	/**
	 * Function for Calculating Current Stock
	 */

	private String getTDYST(String P_strYOSQT,String P_strYBGQT,String P_strYSLRT,String P_strYBGAJ,String P_strYDDQT,String P_strYEDQT,String P_strYCPCT,String P_strYOSLR,String P_strYSRDD,String P_strYSRED,String P_strYDSAJ)
	{
		try
		{
			double L_dblYOSQT = 0;
			double L_dblYOSLR = 0;
			double L_dblYSRDD = 0;
			double L_dblYSRED = 0;
			double L_dblYBGQT = 0;
			double L_dblYSLRT = 0;
        	double L_dblYBGAJ = 0;
			double L_dblYDDQT = 0;
			double L_dblYEDQT = 0;
			double L_dblYCPCT = 0;
			double L_dblYDSAJ = 0;
			double L_dblTCSQT = 0;
			if(P_strYOSQT.equals("-"))
			{
				P_strYOSQT = "0.000";
				L_dblYOSQT = Double.parseDouble(new BigDecimal(P_strYOSQT).toString());
			}
			else
			{
				L_dblYOSQT = Double.parseDouble(new BigDecimal(P_strYOSQT).toString());
			}
			if(P_strYOSLR.equals("-"))
			{
				P_strYOSLR = "0.000";
				L_dblYOSLR = Double.parseDouble(new BigDecimal(P_strYOSLR).toString());
			}
			else
			{
				L_dblYOSLR = Double.parseDouble(new BigDecimal(P_strYOSLR).toString());
			}
			if(P_strYBGAJ.equals("-"))
			{
				P_strYBGAJ = "0.000";
				L_dblYBGAJ = Double.parseDouble(new BigDecimal(P_strYBGAJ).toString());
			}
			else
			{
				L_dblYBGAJ = Double.parseDouble(new BigDecimal(P_strYBGAJ).toString());
			}
			if(P_strYSRDD.equals("-"))
			{
				P_strYSRDD = "0.000";
				L_dblYSRDD = Double.parseDouble(new BigDecimal(P_strYSRDD).toString());
			}
			else
			{
				L_dblYSRDD = Double.parseDouble(new BigDecimal(P_strYSRDD).toString());
			}
			if(P_strYSRED.equals("-"))
			{
				P_strYSRED = "0.000";
				L_dblYSRED = Double.parseDouble(new BigDecimal(P_strYSRED).toString());
			}
			else
			{
				L_dblYSRED = Double.parseDouble(new BigDecimal(P_strYSRED).toString());
			}
			if(P_strYBGQT.equals("-"))
			{
				P_strYBGQT = "0.000";
				L_dblYBGQT = Double.parseDouble(new BigDecimal(P_strYBGQT).toString());
			}
			else
			{
				L_dblYBGQT = Double.parseDouble(new BigDecimal(P_strYBGQT).toString());
			}
			if(P_strYSLRT.equals("-"))
			{
				P_strYSLRT = "0.000";
				L_dblYSLRT = Double.parseDouble(new BigDecimal(P_strYSLRT).toString());
			}
			else
			{
				L_dblYSLRT = Double.parseDouble(new BigDecimal(P_strYSLRT).toString());			
			}
			if(P_strYDDQT.equals("-"))
			{
				P_strYDDQT = "0.000";
				L_dblYDDQT = Double.parseDouble(new BigDecimal(P_strYDDQT).toString());
			}
			else
			{
				L_dblYDDQT = Double.parseDouble(new BigDecimal(P_strYDDQT).toString());
			}
			if(P_strYEDQT.equals("-"))
			{
				P_strYEDQT = "0.000";
				L_dblYEDQT = Double.parseDouble(new BigDecimal(P_strYEDQT).toString());
			}
			else
			{
				L_dblYEDQT = Double.parseDouble(new BigDecimal(P_strYEDQT).toString());
			}
			if(P_strYCPCT.equals("-"))
			{
				P_strYCPCT = "0.000";
				L_dblYCPCT = Double.parseDouble(new BigDecimal(P_strYCPCT).toString());
			}
			else
			{
				L_dblYCPCT = Double.parseDouble(new BigDecimal(P_strYCPCT).toString());
			}
			if(P_strYDSAJ.equals("-"))
			{
				P_strYDSAJ = "0.000";
				L_dblYDSAJ = Double.parseDouble(new BigDecimal(P_strYDSAJ).toString());
			}
			else
			{
				L_dblYDSAJ = Double.parseDouble(new BigDecimal(P_strYDSAJ).toString());
			}
			L_dblTCSQT = (L_dblYOSQT + L_dblYOSLR + L_dblYBGQT + L_dblYSLRT + L_dblYBGAJ) - (L_dblYDDQT + L_dblYEDQT + L_dblYCPCT + L_dblYSRDD + L_dblYSRED + L_dblYDSAJ);
			return setNumberFormat(L_dblTCSQT,3).toString();
		//	return setFMT(new BigDecimal(L_dblTCSQT).toString(),3);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTDYST");
		}
		return "";
	}
	
	private String getCODDS()
	{
		try
		{
			ResultSet M_rstRSSET1;
			String L_CODDS = "";
			M_strSQLQRY = "Select CMT_CODDS,CMT_CODCD from CO_CDTRN where CMT_CGMTP='RPT' and CMT_CGSTP='FGXXSUM' and CMT_CODCD='"+strPRDCT+"'"; 
			M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET1.next())
			{
				L_CODDS = M_rstRSSET1.getString("CMT_CODDS");
				strCODCD = M_rstRSSET1.getString("CMT_CODCD");
			}
			M_rstRSSET1.close();
			return L_CODDS;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCODDS");
		}
		return "";
	}
	
	private String getPRDDS(String P_strPRDDS)
	{
		try
		{
			ResultSet M_rstRSSET1;
			String L_strCODDS = "";
			M_strSQLQRY = "Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+P_strPRDDS+"'";
			M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET1.next())
			{
				L_strCODDS = M_rstRSSET1.getString("PR_PRDDS");
			}
			M_rstRSSET1.close();
			return L_strCODDS;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRDDS");
		}
		return "";
	}
	
	
	private String getDASH(String L_DASHQT)
	{
		try
		{
			if(L_DASHQT == null || L_DASHQT.length() == 0 || L_DASHQT.equals("0.000"))
				L_DASHQT = "-";
			else
				L_DASHQT = L_DASHQT;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDASH");
		}
		return L_DASHQT.trim();
	}
	
	private void delSMWRK()
	{
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			M_strSQLQRY = "Delete from FG_SMWRK";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT("UpdateFG_SMWRK"))
			{
				setMSG("Records Deleted from FG_SMWRK",'N');
			}
			else
				setMSG("Error In Updating FG_SMWRK ",'E');	
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX);
		}
	}

	/**
	 * @return void
	 * Gets the Header of the Report when the report is displayed or printed 
	 * for the first time.
	*/
	private void prnHEADER()
	{
		try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			String L_strFMDAT=txtFMDAT.getText().toString().trim();
			M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strFMDAT));       // Convert Into Local Date Format
			M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
			L_strFMDAT = M_fmtLCDAT.format(M_calLOCAL.getTime());   // Assign Date to Veriable 
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",195));
			dosREPORT.writeBytes("------------------------------\n");
			dosREPORT.writeBytes(padSTRING('L'," ",195));
			dosREPORT.writeBytes(strISODC1+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",195));
			dosREPORT.writeBytes(strISODC2+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",195));
			dosREPORT.writeBytes(strISODC3+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",195));
			dosREPORT.writeBytes("------------------------------\n");
			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst ,195));
			dosREPORT.writeBytes("Report Date:"+ cl_dat.M_strLOGDT_pbst + "\n");	
			
			dosREPORT.writeBytes(padSTRING('R',"Overall Summary as on  '"+L_strFMDAT+"' at 07:00 Hrs.",195));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes("                    <---------------For the Month---------------->     <----------------------------------------------------------------------For the Year--------------------------------------------------->");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes("                    Bagging   Despatch    Despatch         Captive                    ReClsfn.     Bagging    Despatch    Despatch         Captive    ReClsfn.     <-----------------Sales Return------------>     Closing  Unclsfd.");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes("                              Domestic      Export     Consumption      Opng. Stk.         (+)                Domestic      Export     Consumption         (-)      Opng. Stk.  Receipts Dom. Dsp.   Exp. Dsp.       Stock      Qty.");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes ("\n");			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst += 13;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
}

