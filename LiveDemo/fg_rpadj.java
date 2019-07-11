/*	
System Name   : Finished Goods Inventory Management System
Program Name  : ReClassification Statement
Program Desc. : Details regarding Reclassified Lots.
Author        : Mr. Satish S Reddy
Date          : 20th March 2007
Version       : FIMS 2.0

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
//import cl_eml;
import java.util.Properties;
import java.util.Date; 
import java.io.*; 

public class fg_rpadj extends cl_rbase 
{
	
	JTextField txtDATE,txtDESTN;
	JOptionPane LM_OPTNPN;
	
	String strISODCA,strISODCB,strISODCC,LM_FILNM,LM_EMLID,strDATE,strISSQT,strISMNL,strREMDS,strAUTBY,strPRDDS,strRCRCL;
	String strRCLOT,LM_HLPFLD,strRCTDT,strRCTQT,strISLOT,strRCLOT1,strRCMNL,strISSNO,strRCTNO,strISSDT,strISRCL;
	//String LM_RESFIN = cl_dat.ocl_dat.M_REPSTR;
      //  String LM_RESSTR = LM_RESFIN.trim().concat("\\fg_rpadj.doc"); 
	String LM_MARGN = "";
	String strISSNO1,strISSDT1,strRCTNO1,strRCTDT1,strAUTBY1,strREMDS1,strPRDDS1;
	FileOutputStream fosREPORT ;
    DataOutputStream dosREPORT ;
	String strFILNM;
	
	ResultSet M_rstRSSET;
	
	int LM_LMRGN=0;
	int i,j;
	
	boolean flgERRFL = false;
	boolean flgISTFL = false;	
	fg_rpadj()
	{
		super(2);
	  try
	  {	
	  	    
		
		//cl_dat.M_flgHELPFL_pbst = false;
		//LM_OPTNPN = new JOptionPane();
		add(new JLabel("Date :"),5,4,1,1,this,'L');
		add(txtDATE =new TxtDate(),5,5,1,1,this,'L');
		M_pnlRPFMT.setVisible(true);
		
		
		setMSG("Press F1 for help on Adjustment dates",'N');
	}catch(Exception L_EX){
		System.out.println(L_EX.toString());
		}	
	}
	void removeENBL(boolean L_flgSTAT)
     {
        
           M_txtFMDAT.setVisible(false);
	       M_txtTODAT.setVisible(false);
	       M_lblFMDAT.setVisible(false);
		   M_lblTODAT.setVisible(false);		
		    
	 }
	
		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst) /** Combo Opotion Remove the From Date & To Date 
		                                         */
			removeENBL(false);
	
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if((L_KE.getKeyCode() == 9) || (L_KE.getKeyCode() == L_KE.VK_ENTER))
			{
				if(M_objSOURC== txtDATE)
				{
					vldDATE();
				}
			}
			if (L_KE.getKeyCode()== L_KE.VK_F1) //F1 starts from here
			{
				if(M_objSOURC== txtDATE)
				{
					M_strSQLQRY = "Select distinct CONVERT(varchar,ist_autdt,103) ist_autdt from fg_rctrn,fg_istrn where";
					M_strSQLQRY += " rct_cmpcd = ist_cmpcd and rct_rctdt=ist_autdt and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp = '50' and IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_isstp = '50'";
					M_strSQLQRY += " and ist_stsfl = '2' and rct_stsfl = '2' order by ist_autdt desc";
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDATE";
							
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Adjustment Date"},1,"CT");
			
				}	
			}
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"This is key event ....");
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
		   if(M_strHLPFLD == "txtDATE")
		   {
			   txtDATE.setText(cl_dat.M_strHLPSTR_pbst);
			   
		   }	   
	 }
	 private void vldDATE()
	 { //Validates Entered E-Mail Id
		
		try
		{
				if(M_fmtLCDAT.parse(txtDATE.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("Invalid Date Format...",'E');
					txtDATE.requestFocus();
				}
					
				
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"This is Vld Date"); 
		}	
		
	}
	/** This is used for to print the report on the specified Location " Word Format & HTML Format printer & Fax "
 */	
	void exePRINT()
	{   
		
		if(!vldDATA())
			return;
	    
			try
			{
	        int RECCT  = 0 ;
	   		    
   		    setMSG("Report Generation in Process....." ,'N');
   		    setCursor(cl_dat.M_curWTSTS_pbst);
   		   if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rpadj.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpadj.doc";	
							
			getDATA();
			
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
	
	
	/**
	 * Method to validate the data before execuation of the SQL Quary.
	 */
	boolean vldDATA()
	{
		try
		{
			if(txtDATE.getText().trim().length() == 0)
			{
				setMSG("Enter  Date..",'E');
				txtDATE.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtDATE.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("From Date should not be grater than current Date..",'E');
				txtDATE.requestFocus();
				return false;
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
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"This is vldata");
			return false;
		}
		return true;
	}		
	/** Method to fetch data from tblSTKDTL table & club it with Header in Data Output Stream  */
	  private void getDATA()
	 {
		try
		{   
			cl_dat.M_PAGENO = 0;
		    String strln="";
		    System.out.println("sss");
		    System.out.println("th file name is " +strFILNM);
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
				prnFMTCHR(dosREPORT,M_strCPI17);
		    
   		      		    
		   	 prnHEADER();/****gets the header of the report*/
			 getISODCN();
			 getALLREC();
			 
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"getData");
		}	
	  }
	  /**
	 * @return void
	 * Fetches the ISO No. for this particular report
	 */
	private void getISODCN()
	{ 
		try
		{
			strISODCA = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpadj01");
			strISODCB = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpadj02");
			strISODCC = cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpadj03");
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"getISODCN");
		}
	}
	  
	  
	  
	  private void getALLREC()
	  {
		try
		{
			strDATE = txtDATE.getText().toString().trim();
			
			getISSDTL();
			getRCTDTL();
			prnFOOTR();
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	
	private void getISSDTL()
	{
		try
		{
			double L_TOTISS = 0;
			flgISTFL = false;	
			strISSQT = "0";
			M_strSQLQRY = "Select ist_issno,pr_prdds,ist_lotno,ist_rclno,ist_mnlcd,rm_remds,ist_autdt,";
			M_strSQLQRY += " sum(ist_issqt) L_ISSQT from fg_istrn,co_prmst,fg_rmmst where ist_prdcd=pr_prdcd";
			M_strSQLQRY += " and IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_isstp = '50' and ist_autdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDATE.getText().trim()))+"' and ist_stsfl = '2'";
			M_strSQLQRY += " and rm_cmpcd = ist_cmpcd and RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rm_wrhtp = ist_wrhtp and  rm_trntp = 'IS' and rm_doctp=ist_isstp";
			M_strSQLQRY += " and rm_docno = ist_issno";
			M_strSQLQRY += " group by ist_issno,ist_autdt,pr_prdds,ist_lotno,ist_rclno,ist_mnlcd,rm_remds";
			M_strSQLQRY += " order by ist_issno,ist_autdt,pr_prdds,ist_lotno,ist_rclno,ist_mnlcd,rm_remds";  
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
			//	System.out.println("aaa");
				strISSNO = M_rstRSSET.getString("IST_ISSNO").trim();
				System.out.println(M_rstRSSET.getString("IST_ISSNO"));
				strISSDT = M_fmtLCDAT.format(M_rstRSSET.getDate("IST_AUTDT"));
				System.out.println(M_fmtLCDAT.format(M_rstRSSET.getDate("IST_AUTDT")));
				
				strPRDDS = M_rstRSSET.getString("PR_PRDDS").trim();					
				System.out.println("bbb");
				strISLOT = M_rstRSSET.getString("IST_LOTNO").trim();
				strISRCL = M_rstRSSET.getString("IST_RCLNO").trim();
				strISMNL = M_rstRSSET.getString("IST_MNLCD").trim();
				strREMDS = M_rstRSSET.getString("RM_REMDS").trim();
				strISSQT = M_rstRSSET.getString("L_ISSQT").trim();
				if(cl_dat.M_intLINNO_pbst >= 68){
					if(flgISTFL){
						dosREPORT.writeBytes ("\n");
						crtLINE(120);
						prnFMTCHR(dosREPORT,M_strEJT);
					}
					flgISTFL = true;
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO += 1;
					prnHEADER();
					getTBLHDR();
				}
				if(!strISSNO.equals(strISSNO1) || !strISSDT.equals(strISSDT1) || !strREMDS.equals(strREMDS1))
				{
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('R',"",30));
					dosREPORT.writeBytes(padSTRING('R',"Adjustment( - )",20));
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					dosREPORT.writeBytes(padSTRING('R',"",30));
					dosREPORT.writeBytes(padSTRING('R',strISSNO,12));
					dosREPORT.writeBytes(padSTRING('R',strISSDT,15));
					dosREPORT.writeBytes(padSTRING('R',strREMDS,15));
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					strISSNO1 = strISSNO;
					strISSDT1 = strISSDT;
					strREMDS1 = strREMDS;
					}
				if(!strPRDDS.equals(strPRDDS1)){
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					strPRDDS1 = strPRDDS;
				}
				dosREPORT.writeBytes(padSTRING('R',"",30));
				dosREPORT.writeBytes(padSTRING('R',strISLOT,12));
				if(strPRDDS != null)
					dosREPORT.writeBytes(padSTRING('R',strPRDDS,15));
				else
					dosREPORT.writeBytes(padSTRING('R'," ",15));
				if(strISMNL != null){
					dosREPORT.writeBytes(padSTRING('R'," ",5));
					dosREPORT.writeBytes(padSTRING('R',strISMNL,10));
				}else
					dosREPORT.writeBytes(padSTRING('R'," ",15));
				if(strISSQT != null){
					dosREPORT.writeBytes(padSTRING('L',strISSQT,15));
				}else
					dosREPORT.writeBytes(padSTRING('L'," ",15));
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_TOTISS += Double.parseDouble(strISSQT); 
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('L'," ",42));
				dosREPORT.writeBytes(padSTRING('R',"Total Qty: ",15));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_TOTISS,3),30));
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				crtLINE(120);
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
		}catch(Exception L_EX){
			setMSG(L_EX,"getISSDTL");
		}
	}
	
	private void getRCTDTL()
	{
		try
		{
			strRCTQT = "0";
			double L_TOTRCT = 0;
			flgISTFL = false;	
			M_strSQLQRY = "Select rct_rctno,pr_prdds,rct_lotno,rct_rclno,rct_mnlcd,rm_remds,rct_rctdt,";
			M_strSQLQRY += " sum(rct_rctqt) L_RCTQT from fg_rctrn,co_prmst,fg_rmmst,pr_ltmst where";
			M_strSQLQRY += " rct_prdtp = lt_prdtp and rct_lotno = lt_lotno and rct_cmpcd = lt_cmpcd and rct_rclno=lt_rclno and str(lt_prdcd,10,0)=pr_prdcd";
			M_strSQLQRY += " and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rcttp = '50' and rct_rctdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtDATE.getText().trim()))+"' and rct_stsfl = '2'";
			M_strSQLQRY += " and rm_cmpcd = rct_cmpcd and RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rm_wrhtp=rct_wrhtp and  rm_trntp = 'RC' and rm_doctp=rct_rcttp";
			M_strSQLQRY += " and rm_docno=rct_rctno";
			M_strSQLQRY += " group by rct_rctno,rct_rctdt,pr_prdds,rct_lotno,rct_rclno,rct_mnlcd,rm_remds";
			M_strSQLQRY += " order by rct_rctno,rct_rctdt,pr_prdds,rct_lotno,rct_rclno,rct_mnlcd,rm_remds";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(M_strSQLQRY);
		//	System.out.println(M_rstRSSET);
			
			while(M_rstRSSET.next())
			{
				strRCTNO = M_rstRSSET.getString("RCT_RCTNO").trim();
				strRCTDT = M_fmtLCDAT.format(M_rstRSSET.getDate("RCT_RCTDT"));				
				strPRDDS = M_rstRSSET.getString("PR_PRDDS").trim();					
				strRCLOT = M_rstRSSET.getString("RCT_LOTNO").trim();
				strRCRCL = M_rstRSSET.getString("RCT_RCLNO").trim();
				strRCMNL = M_rstRSSET.getString("RCT_MNLCD").trim();
				strREMDS = M_rstRSSET.getString("RM_REMDS").trim();
				strRCTQT = M_rstRSSET.getString("L_RCTQT").trim();
				if(cl_dat.M_intLINNO_pbst >= 68){
					if(flgISTFL)
					{
						dosREPORT.writeBytes ("\n");
						crtLINE(120);
						prnFMTCHR(dosREPORT,M_strEJT);
					}
					flgISTFL = true;
					cl_dat.M_intLINNO_pbst = 0;
					cl_dat.M_PAGENO += 1;
					prnHEADER();
					getTBLHDR();
				}
				if(!strRCTNO.equals(strRCTNO1) || !strRCTDT.equals(strRCTDT1) || !strREMDS.equals(strREMDS1))
				{
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('R',"",30));
					dosREPORT.writeBytes(padSTRING('R',"Adjustment( + )",20));
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					dosREPORT.writeBytes(padSTRING('R',"",30));
					dosREPORT.writeBytes(padSTRING('R',strRCTNO,12));
					dosREPORT.writeBytes(padSTRING('R',strRCTDT,15));
					dosREPORT.writeBytes(padSTRING('R',strREMDS,15));
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					strRCTNO1 = strRCTNO;
					strRCTDT1 = strRCTDT;
					strREMDS1 = strREMDS;
				}
				if(!strPRDDS.equals(strPRDDS1))
				{
					dosREPORT.writeBytes ("\n");
					cl_dat.M_intLINNO_pbst+=1;
					strPRDDS1 = strPRDDS;
				}
				dosREPORT.writeBytes(padSTRING('R',"",30));
				dosREPORT.writeBytes(padSTRING('R',strRCLOT,12));
				if(strPRDDS != null)
					dosREPORT.writeBytes(padSTRING('R',strPRDDS,15));
				else
					dosREPORT.writeBytes(padSTRING('R'," ",15));
				if(strRCMNL != null){
					dosREPORT.writeBytes(padSTRING('R'," ",5));
					dosREPORT.writeBytes(padSTRING('R',strRCMNL,10));
				}else
					dosREPORT.writeBytes(padSTRING('R'," ",15));
				if(strRCTQT != null){
					dosREPORT.writeBytes(padSTRING('L',strRCTQT,15));
				}else
					dosREPORT.writeBytes(padSTRING('L'," ",15));
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				L_TOTRCT += Double.parseDouble(strRCTQT); 
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('L'," ",42));
				dosREPORT.writeBytes(padSTRING('R',"Total Qty: ",15));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_TOTRCT,3),30));
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes ("\n");
				cl_dat.M_intLINNO_pbst+=1;
				crtLINE(120);
		}catch(Exception L_EX){
			setMSG(L_EX,"getRCTDTL");
		}
	}
	  
	private void prnFOOTR()
	{
		try
		{
			if(cl_dat.M_intLINNO_pbst >= 61)
			{
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			for(int i = 1;i <= 5;i++)
				dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 5;
			prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('L'," ",40));//margin
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY ",20));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY ",20));
			dosREPORT.writeBytes(padSTRING('L',"H.O.D(MHD) ",20));
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst+=1;
			crtLINE(120);
			prnFMTCHR(dosREPORT,M_strEJT);			
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
			dosREPORT.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"prnFOOTR");
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
			
			prnFMTCHR(dosREPORT,M_strBOLD);
			
			dosREPORT.writeBytes(padSTRING('R',"",90));
			crtLINE(30);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpadj01"),120));
			
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpadj02"),120));
		
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L', cl_dat.getPRMCOD("CMT_CODDS","ISO","FGXXRPT","fg_rpadj03"),120));
			System.out.println("ee");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",90));
			crtLINE(30);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			System.out.println("ff");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
			
			dosREPORT.writeBytes(padSTRING('L',"Date    : "+cl_dat.M_strLOGDT_pbst,95));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Stock Adjustment Statement for the date: "+txtDATE.getText().trim() ,55));
			dosREPORT.writeBytes(padSTRING('L',"Page No : " + cl_dat.M_PAGENO,56));
			dosREPORT.writeBytes("\n");
			crtLINE(120);
			dosREPORT.writeBytes("\n");
			prnFMTCHR(dosREPORT,M_strNOBOLD);
			cl_dat.M_intLINNO_pbst += 9;
			}
		catch(Exception L_EX){
			setMSG(L_EX,"prnHEADER");
		}
	}
	
	/**
	 * @return void
	 * The following method displays the Header of the Table.
	 */
	private void getTBLHDR()
	{
			try
			{
				prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('R'," ",30));
				dosREPORT.writeBytes(padSTRING('R',"Lot No.",12));
				dosREPORT.writeBytes(padSTRING('R',"Grade",20));
				dosREPORT.writeBytes(padSTRING('R',"Location",10));
				dosREPORT.writeBytes(padSTRING('L',"Qty.",15));
				dosREPORT.writeBytes("\n");
				crtLINE(120);
				dosREPORT.writeBytes("\n");
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				cl_dat.M_intLINNO_pbst+=3;
			}catch(Exception L_EX)
			{
				setMSG(L_EX,"getTBLHDR");
			}
	}
	
	
	private void crtLINE(int LM_CNT)
	{
		String strln = "";
		try
		{
				for(int i=1;i<=LM_CNT;i++)
				{
					strln += "-";
				}
				dosREPORT.writeBytes(padSTRING('L'," ",LM_LMRGN));
				dosREPORT.writeBytes(strln);
			}
			catch(Exception L_EX)
			{
				System.out.println("L_EX Error in Line:"+L_EX);
			}
	}
}

	
	
	
