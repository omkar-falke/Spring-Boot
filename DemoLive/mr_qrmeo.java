/*
System Name   : Marketing Management System
Program Name  : Month - End Operation
Program Desc. :  Month - End Operation is updatiing the DATABASE

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Date;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.lang.*;



public class mr_qrmeo extends cl_rbase implements MouseListener
{
	JOptionPane LM_OPTNPN;
	JComboBox cmbBCF;
	TxtDate txtCOFDT;
    JButton btnRUN,btnCLEAR,btnAUTHOR;
    JLabel lblCODT;
	CallableStatement cstCRFQT;//Call the Procedure from DATABASE
	String strCOFDT,strCFWQT,strMKTTP,strINVNO,strINVDT,strFILNM,strFLDNM="",strFLDVAL="",strHEADER;
	
	FileOutputStream fosREPORT;
    DataOutputStream dosREPORT;
    PreparedStatement PS01_dsp;
    ResultSet LP_rstRSSET;
    int intLMRGN;
    String[] dispHEADER= {"Market Type","Invoice Number","Date"};
    String[] dispHEADER1={"Product Type","Product Code","Document Type","Document No","Document Date"};
	/**Constructor for the form<br>
	 * to create the TextFields and ComboBox
	 * 
	 */
    mr_qrmeo()
	{
		super(1);
		try{	
			
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);		
		    LM_OPTNPN = new JOptionPane();
			add(cmbBCF=new JComboBox(),2,2,1,1.7,this,'L');
			
			cmbBCF.addItem( "Select");
			cmbBCF.addItem("Booking Carry Forward");
			cmbBCF.addItem("Payment Tranc. Verfication");
			add(lblCODT=new JLabel("Cut of Date") ,2,4,1,1,this,'L');
			add(txtCOFDT=new TxtDate(),2,5,1,1,this,'L');
			add(btnAUTHOR=new JButton("Authorize"),2,6,1,1,this,'L');
			cmbBCF.setEnabled( false);
			txtCOFDT.setEnabled( false);
			lblCODT.setEnabled( false);
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is constructor");
		}
	}
	/*
	 *  
	 * @see cl_pbase#setENBL(boolean) 
	 */
	void setENBL(boolean L_flgSTAT)
	{   
         super.setENBL(L_flgSTAT);
         try
         {
             cmbBCF.setEnabled( false);
 			txtCOFDT.setEnabled( false);
 			lblCODT.setEnabled(false);
 			btnAUTHOR.setEnabled(false);
 			
		     
         }catch(Exception L_EX)
         {
          setMSG(L_EX,"This is set Enabled");   
         }
	}
	void remENBL(boolean L_flgSTAT)
	{
	    M_txtFMDAT.setVisible(false);
	       M_txtTODAT.setVisible(false);
	       M_lblFMDAT.setVisible(false);
		   M_lblTODAT.setVisible(false);		
		    
	}
	/**
	 *  This ActionPerformed is to implement the events on the Components 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{ 
			super.actionPerformed(L_AE);
			 if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
	 			{
			     remENBL(false);
			     if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
					{	
			         setMSG("Please Select Option..",'N');		
			         cmbBCF.setEnabled( true);
					}
			     else
			     {
			         setMSG("Please Select Option..",'N');		
			         cmbBCF.setEnabled( false);
			     }
	 			}
			 if(M_objSOURC== cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString())
	 			{
			     txtCOFDT.setVisible(false);
	 	 		 lblCODT.setVisible(false); 
	 	 		M_pnlRPFMT.setVisible(false);
	 	 		btnAUTHOR.setVisible( false);
	 			}
			    if(cmbBCF.getSelectedItem().toString().equals("Booking Carry Forward"))
				 {  
			        txtCOFDT.setVisible( true);
	 	 			lblCODT.setVisible(true); 
	 	 			txtCOFDT.setEnabled( true); 
	 		 		lblCODT.setEnabled(true);
	 		 		btnAUTHOR.setEnabled( true);
	 		 		btnAUTHOR.setVisible( true);
			        txtCOFDT.setText("01/"+cl_dat.M_strLOGDT_pbst.substring(3));
			 		txtCOFDT.requestFocus() ;
			 		M_pnlRPFMT.setVisible(false);
			 		
					//strCOFDT = strCOFDT1.substring(0,3)+"01"+strCOFDT1.substring(4);
				 }
	    		if(cmbBCF.getSelectedItem().toString().equals("Payment Tranc. Verfication"))
    		    {
	    		    txtCOFDT.setVisible(false);
		 	 		 lblCODT.setVisible(false); 
		 	 		btnAUTHOR.setVisible( false);
		 	 		M_pnlRPFMT.setVisible(true);
	                
	            }
	    		
	    		 if(M_objSOURC==txtCOFDT)
	 		    {
	 		        btnAUTHOR.requestFocus();
	 		    }
	 		    if(cmbBCF.getSelectedItem().toString().equals("Booking Carry Forward"))
				 {  
		          
		    		 if(M_objSOURC==btnAUTHOR)
			    	{
			    			 
		    		    if(!vldDATA())
			            {
						return;
			            }
					 
					    this.setCursor(cl_dat.M_curWTSTS_pbst);
						setMSG("Updating DATA...........",'N');
					    strCOFDT =M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCOFDT.getText().toString().trim() ));
						System.out.println(strCOFDT);
						cstCRFQT = cl_dat.M_conSPDBA_pbst.prepareCall("{ call setCRFQT_IND(?,?)}");
						cstCRFQT.setString(1,cl_dat.M_strCMPCD_pbst);
						cstCRFQT.setString(2,strCOFDT);
						cstCRFQT.executeUpdate();
					    cl_dat.exeDBCMT("exeSAVE");
					    this.setCursor(cl_dat.M_curDFSTS_pbst);
					    M_strSQLQRY= "select sum(int_cfwqt) int_cfwqt from mr_intrn";
					    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					    while(M_rstRSSET.next() )
					    {
					        strCFWQT=M_rstRSSET.getString("int_cfwqt");
					        System.out.println(strCFWQT);
					    }
						
					    LM_OPTNPN.showMessageDialog(this,"Data Updation  Successfully. i.e "+strCFWQT,"Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
					 }
				 }	 
	    		 if(cmbBCF.getSelectedItem().toString().equals("Payment Tranc. Verfication"))
	  		    {
	  	           exePRINT();
	  		    }
	    		
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is actionPerformed");
		}
	}	

	
	
	 /**
	  * This exeSAVE Method to implement cl_rbase. 
	  */
/*	public void exeAUTHORISED()
	{
	    try
		{
	        if(cmbBCF.getSelectedItem().toString().equals("Booking Carry Forward"))
			 {  
	            if(!vldDATA())
	            {
				return;
	            }
			 
			    this.setCursor(cl_dat.M_curWTSTS_pbst);
				setMSG("Updating DATA...........",'N');
			    strCOFDT =M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCOFDT.getText().toString().trim() ));
				System.out.println(strCOFDT);
				cstCRFQT = cl_dat.M_conSPDBA_pbst.prepareCall("{ call setCRFQT_IND(?)}");
				cstCRFQT.setString(1,strCOFDT);
				cstCRFQT.executeUpdate();
			    cl_dat.exeDBCMT("exeSAVE");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			M_strSQLQRY= "select sum(int_cfwqt) int_cfwqt from mr_intrn";
		    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    while(M_rstRSSET.next() )
			    {
			        strCFWQT=M_rstRSSET.getString("int_cfwqt");
			        System.out.println(strCFWQT);
			    }
				
			    LM_OPTNPN.showMessageDialog(this,"Data Updation  Successfully. i.e "+strCFWQT,"Data Transfer Status",JOptionPane.INFORMATION_MESSAGE);
			 }
	        if(cmbBCF.getSelectedItem().toString().equals("Payment Tranc. Verfication"))
 		    {
 	            exePRINT();

 	            
 		    }
	        
	        
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is exeSAVE");
		}
			
	}*/
	
	
	/**
	 * This vldDATA function is vldation on the TextField
	 */
	boolean vldDATA()
	{
		try
		{
			if(txtCOFDT.getText().trim().length() == 0)
			{
				setMSG("Enter  Date..",'E');
				txtCOFDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtCOFDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("From Date should not be grater than current Date..",'E');
				txtCOFDT.requestFocus();
				return false;
			}
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is VldDATA");
		}
		return true;
	}
	
	/** This is used for to print the report on the specified Location " Word Format & HTML Format printer & Fax "
	 */	
		void exePRINT()
		{   
		    
				try
				{
		        int RECCT  = 0 ;
		   		    
	   		    setMSG("Report Generation in Process....." ,'N');
	   		    setCursor(cl_dat.M_curWTSTS_pbst);
	   		   if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_hkmeo.html";
			    else if(M_rdbTEXT.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + "mr_hkmeo.doc";	
								
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
		/** Method to fetch data from tblSTKDTL table & club it with Header in Data Output Stream  */
		  private void getDATA()
		 {
			try
			{   
				cl_dat.M_PAGENO = 0;
			    String strln="";
			    
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
			    
	   		      		    
			 	 //prnHEADER();/****gets the header of the report*/
		   		    missIPL();
				    ledgrMISS();
					missOTHCREDIT();
				    missSALESCN();
				    receSRCREQMISS();
				    missOTDEBNOT();
				    overallALL();
				    missCUSTBOOKDIS();
				    missDISBBOOKDIS();
				    missDISBCOMMI();
				    missSALRETUTRAN();
				    missCREDITDEBIT();
				    invoiceDEBPARTYLEADJU();
				    payRECECREPARTYLEAD();
				    extDEBITADJUVA();
				    extCREITADJUVA();
				    taxCALPREDFIND();
				    taxCALPREDFIND1();
				    taxRECORDFOUND();
				    taxRECORDFOUND1();
				    recordREPETSEQ();
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"getDATA");
			}
		 }
		  
		  
			private void missIPL()
		  	{
		  	    try
		  	    {
		  	      M_strSQLQRY ="select ivt_mkttp,ivt_invno,ivt_invdt from mr_ivtrn"; 
		            M_strSQLQRY += " where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,ivt_invdt,101) > '06/30/2006' and ivt_mkttp in ('01','04','05')";
		            M_strSQLQRY += " and ivt_saltp not in ('04','05','16','21')";
		            M_strSQLQRY += " and  ivt_invno not in(select pl_docno from mr_pltrn where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pl_doctp='21')"; 
		            M_strSQLQRY += " order by ivt_mkttp,ivt_invno";
		            M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
		            
		            prnFMTCHR(dosREPORT,M_strBOLD);
		            dosREPORT.writeBytes(padSTRING('R'," ",10));
		            dosREPORT.writeBytes(padSTRING('R',"Missing Invoices in Party Ledger",40));
		            prnFMTCHR(dosREPORT,M_strNOBOLD);
		            dosREPORT.writeBytes("\n\n");
		           String[] staHEADER={"ivt_mkttp","ivt_invno","ivt_invdt"};
		           dspCHKLST(M_rstRSSET,staHEADER);
		          
		           
		           
		         }catch(Exception L_EX)
		  	    {
		  	        setMSG(L_EX,"This is MissIPL");
		  	    }
		  	}
			
		  
			private void ledgrMISS()
			{
			   try
			   {
			    M_strSQLQRY ="select pl_prttp,pl_prtcd,pl_docno,pl_doctp,pl_docdt,pl_docvl " ;
			    M_strSQLQRY += " from mr_pltrn where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pl_docdt > '06/30/2006' and pl_mkttp  " ;
			    M_strSQLQRY += " in ('01','04','05')  and SUBSTRING(pl_doctp,1,1) in ('0','2') ";
			    M_strSQLQRY += " and pl_docno not in (select pt_docrf from mr_pttrn " ;
			    M_strSQLQRY += " where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_mkttp in ('01','04','05'))  and pl_doctp + pl_docno " ;
			    M_strSQLQRY += " not in (select '21' + ivt_invno from mr_ivtrn where " ;
			    M_strSQLQRY += " IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(ivt_cc1vl,0)+isnull(ivt_cc2vl,0)+isnull(ivt_cc3vl,0) = 0 or SUBSTRING(ivt_indno,2,2) = 'DR') " ;
			    M_strSQLQRY += " and ivt_mkttp in ('01','04','05') and CONVERT(varchar,ivt_invdt,101)> '06/30/2006') order by pl_docno";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Ledger records missing in MR_PTTRN",40));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    String[] staHEADER1={"pl_prttp","pl_prtcd","pl_docno","pl_docdt","pl_doctp","pl_docvl"};
			    dspCHKLST(M_rstRSSET,staHEADER1);
			   }catch(Exception L_EX)
			   {
			       setMSG(L_EX,"ledgrMISS");
			   }
			}
			
			private void missOTHCREDIT()
			{
			   try
			   {
			          M_strSQLQRY ="select pt_cmpcd, pt_crdtp,  pt_prttp, pt_prtcd, pt_docrf,pt_docdt,sum(pt_pntvl) pt_pntvl from " ;
					    M_strSQLQRY+= " mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pt_docrf,2,2)  = '09'  and pt_docdt > '06/30/2006' " ;
					    M_strSQLQRY+= " and pt_docrf <> '00000000' and pt_docrf not in " ;
					    M_strSQLQRY+= " (select pl_docno from mr_pltrn where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pl_docno,2,2) = '09' ) ";
					    M_strSQLQRY+= " group by pt_cmpcd, pt_crdtp, pt_crdtp, pt_prttp, pt_prtcd, pt_docrf,pt_docdt";
					    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
					    prnFMTCHR(dosREPORT,M_strBOLD);
			            dosREPORT.writeBytes(padSTRING('R'," ",10));
			            dosREPORT.writeBytes(padSTRING('R',"Missing Other Credit Notes",40));
			            prnFMTCHR(dosREPORT,M_strNOBOLD);
			            dosREPORT.writeBytes("\n\n");
					    
					    String[] staHEADER2={"pt_cmpcd","pt_crdtp","pt_prttp","pt_prtcd","pt_docrf","pt_docdt","pt_pntvl"};
					    dspCHKLST(M_rstRSSET,staHEADER2);
			       
			      
			   }catch(Exception L_EX)
			   {
			       setMSG(L_EX,"missOTHCREDIT");
			   }

			}
			private void missSALESCN()
			{
			   try
			   {
			    M_strSQLQRY=" select pt_cmpcd, pt_invno,pt_prttp,pt_prtcd from mr_pttrn " ;
			    M_strSQLQRY+=" where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pt_docrf,2,2) = '04'  and pt_docdt > '06/30/2006' ";
			    M_strSQLQRY+= " and pt_docrf <> '00000000' and pt_docrf not in ";
			    M_strSQLQRY+= " (select pl_docno from mr_pltrn where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pl_docno,2,2)  ='04' )";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Missing Sales return Credit Note",40));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER3={"pt_cmpcd","pt_invno","pt_prttp","pt_prtcd"};
			    dspCHKLST(M_rstRSSET,staHEADER3);
			   }catch(Exception L_EX)
			   {
			       setMSG(L_EX,"missSALESCN");
			   }
			    
			    
			}
			private void receSRCREQMISS()
			{
			    try
			    {
			        M_strSQLQRY =" select  mr_pttrn.pt_docrf,mr_pttrn.pt_docdt,mr_pttrn.pt_prtcd,pt_prtnm,rct_issrf,lt_prdcd,pr_prdds";
			        M_strSQLQRY += " pt_invqt,sum(rct_rctqt) rct_rctqt from fg_rctrn, pr_ltmst,  mr_pttrn, co_ptmst,co_prmst";
			        M_strSQLQRY += " where rct_rcttp='30' and rct_cmpcd + rct_lotno + rct_rclno = lt_cmpcd + lt_lotno + lt_rclno and rct_cmpcd + rct_issrf + lt_prdcd=pt_cmpcd + pt_invno + pt_prdcd ";
			        M_strSQLQRY += " and rct_rctdt=pt_docdt and PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_crdtp='04'  and co_ptmst.pt_prtcd=mr_pttrn.pt_prtcd and co_ptmst.pt_prttp='C' ";
			        M_strSQLQRY += " and pt_prdcd=pr_prdcd group by mr_pttrn.pt_docrf,mr_pttrn.pt_docdt,mr_pttrn.pt_prtcd,pt_prtnm,rct_issrf,lt_prdcd,pr_prdds,pt_invqt ";
			        M_strSQLQRY += " having pt_invqt<>sum(rct_rctqt)";
			        M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				    prnFMTCHR(dosREPORT,M_strBOLD);
		            dosREPORT.writeBytes(padSTRING('R'," ",10));
		            dosREPORT.writeBytes(padSTRING('R',"Records with Receipt Qty. and Sales Return Credit Quantity Mismatches",70));
		            prnFMTCHR(dosREPORT,M_strNOBOLD);
		            dosREPORT.writeBytes("\n\n");
		            String[] staHEADER4={"pt_docrf","pt_docdt","pt_prtcd","pt_prtnm","rct_issrf","lt_prdcd","pr_prdds","pt_invqt","rct_rctqt"};
				    dspCHKLST(M_rstRSSET,staHEADER4);
		            
			        
			    }catch(Exception L_EX)
			    {
			        setMSG(L_EX,"receSRCREQMISS");
			    }
			    
			}
			private void missOTDEBNOT()
			{
			   try
			   {
			    M_strSQLQRY ="select pt_cmpcd,pt_docrf,pt_prttp,pt_prtcd from mr_pttrn ";
			    M_strSQLQRY += " where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pt_docrf,2,2) = '39' and pt_docdt > '06/30/2006' ";
			    M_strSQLQRY += " and pt_docrf <> '00000000' and pt_docrf not in";
			    M_strSQLQRY += " (select pl_docno from mr_pltrn where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pl_docno,2,2) = '39' )";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Missing Other Debit Notes",40));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER5={"pt_cmpcd","pt_docrf","pt_prttp","pt_prtcd"};
			    dspCHKLST(M_rstRSSET,staHEADER5);
			   }catch(Exception L_EX)
			   {
			       setMSG(L_EX,"missOTDEBNOT");
			       
			   }
	       
			}
			
			
			private void overallALL()
			{
			  try
			  {
			    M_strSQLQRY= "select pt_cmpcd, pt_crdtp,  pt_prttp, pt_prtcd, pt_docrf,pt_docdt,sum(pt_pntvl) pt_pntvl " ;
			    M_strSQLQRY += " from mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pt_docrf,2,2) in ('01','02','03','04','09','31''32','39','41','42') ";
			    M_strSQLQRY += " and pt_docdt > '06/30/2006' and pt_docrf <> '00000000' and pt_docrf not in ";
			    M_strSQLQRY += " (select pl_docno from mr_pltrn where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pl_docno,2,2)  in ('01','02','03','04','09','31''32','39','41','42') )";
			    M_strSQLQRY += " group by pt_cmpcd, pt_crdtp, pt_crdtp, pt_prttp, pt_prtcd, pt_docrf,pt_docdt";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Overall (all categories) verification",40));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER6={"pt_cmpcd","pt_crdtp","pt_prttp","pt_prtcd","pt_docrf","pt_docdt","pt_pntvl"};
			    dspCHKLST(M_rstRSSET,staHEADER6);
			    
			  }catch(Exception L_EX)
			  {
			      setMSG(L_EX,"overallALL");
			  }
			    
			}
			private void missCUSTBOOKDIS()
			{
			    try
			    {
			    M_strSQLQRY ="select ivt_mkttp,ivt_invno,ivt_byrcd,ivt_invdt from mr_ivtrn " ;
			    M_strSQLQRY	+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp in ('01','04','05') and CONVERT(varchar,ivt_invdt,101) > '06/30/2006' ";
			    M_strSQLQRY	+= " and isnull(ivt_cc1vl,0) > 0 and len(ivt_cc1rf)=6 and ivt_invno not in ";
			    M_strSQLQRY	+= " (select pt_invno from mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_crdtp='01')";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Missing records in MR_PTTRN for Customer booking discount",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER7={"ivt_mkttp","ivt_invno","ivt_byrcd","ivt_invdt"};
			    dspCHKLST(M_rstRSSET,staHEADER7);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is missCUSTBOOKDIS");
			    }
			}
			private void missDISBBOOKDIS()
			{
			    try
			    {
			    M_strSQLQRY ="select ivt_mkttp,ivt_invno,ivt_byrcd,ivt_invdt from mr_ivtrn " ;
			    M_strSQLQRY	+=" where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp in ('01','04','05') and CONVERT(varchar,ivt_invdt,101) > '06/30/2006' ";
			    M_strSQLQRY	+= " and isnull(ivt_cc2vl,0) > 0 and len(ivt_cc2rf)=6 and ivt_invno not in ";
			    M_strSQLQRY	+= " (select pt_invno from mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_crdtp='02')";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Missing records in MR_PTTRN for Distributor  booking discount",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER8={"ivt_mkttp","ivt_invno","ivt_byrcd","ivt_invdt"};
			    dspCHKLST(M_rstRSSET,staHEADER8);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is missDISTBBOOKDIS");
			    }
			}
			private void missDISBCOMMI()
			{
			    try
			    {
			    M_strSQLQRY ="select ivt_mkttp,ivt_invno,ivt_byrcd,ivt_invdt,ivt_comvl,ivt_dsrcd  from mr_ivtrn " ;
			    M_strSQLQRY += " where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp in ('01','04','05') and CONVERT(varchar,ivt_invdt,101) > '06/30/2006'  and isnull(ivt_comvl,0) > 0  ";
			    M_strSQLQRY += " and ivt_invno not in (select pt_invno from mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_crdtp='03')";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Missing records in MR_PTTRN for Distributor  commission record",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER9={"ivt_mkttp","ivt_invno","ivt_byrcd","ivt_invdt","ivt_comvl","ivt_dsrcd"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER9);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is missDISBCOMMI");
			    }
			}
			private void missSALRETUTRAN()
			{
			    try
			    {
			    M_strSQLQRY ="select rct_rcttp, rct_rctno,rct_issrf,rct_rctdt,rct_lotno from fg_rctrn ";
			    M_strSQLQRY += " where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND rct_rctdt > '06/30/2006' and rct_rcttp = '30' and len(rct_issrf) = 8 ";
			    M_strSQLQRY += " and rct_issrf not in (select pt_invno from mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_crdtp='04')";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Missing records in MR_PTTRN for Sales Return transactions",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER10={"rct_rcttp","rct_rctno","rct_issrf","rct_rctdt","rct_lotno"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER10);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is missSALRETUTRAN");
			    }
			}
			private void missCREDITDEBIT()
			{
			    try
			    {
			    M_strSQLQRY ="select pt_prttp,pt_prtcd,pt_crdtp,pt_docrf,pt_docdt,pt_mkttp,pt_invno from mr_pttrn";
			    M_strSQLQRY += " where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_docdt > '06/30/2006' and pt_docrf <> '00000000' and pt_docrf not in ";
			    M_strSQLQRY += " (select pl_docno from mr_pltrn where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(pl_doctp,1,1) in ('0','3'))";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Missing records in MR_PLTRN for Credit / Debit Transactions",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER11={"pt_prttp","pt_prtcd","pt_crdtp","pt_docrf","pt_docdt","pt_mkttp","pt_invno"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER11);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is missCREDITDEBIT");
			    }
			}
			private void invoiceDEBPARTYLEADJU()
			{
			    try
			    {
			    M_strSQLQRY ="select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl ";
			    M_strSQLQRY += " from mr_pltrn, mr_patrn where pl_cmpcd=pa_cmpcd and pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_docno = pa_dbtno " ;
			    M_strSQLQRY += " and PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_docno, pl_adjvl  having abs(pl_adjvl -sum(isnull(pa_adjvl,0)))>1";
			  
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Invoice / Debit note records in Party Ledger with adjustment amount mismatch",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER12={"pl_prttp","pl_prtcd","pl_docno","pl_adjvl","pl_adjvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER12);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is invoiceDEBPARTYLEADJU");
			    }
			}
			private void payRECECREPARTYLEAD()
			{
			    try
			    {
			    M_strSQLQRY ="select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl , sum(isnull(pa_adjvl,0)) pl_adjvl ";
			    M_strSQLQRY += " from mr_pltrn, mr_patrn where pl_cmpcd=pa_cmpcd and pl_prttp=pa_prttp and pl_prtcd = pa_prtcd and pl_docno = pa_dbtno " ;
			    M_strSQLQRY += " and PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(pl_adjvl,0)>0 group by pl_prttp,pl_prtcd,pl_docno, pl_adjvl  having abs(pl_adjvl -sum(isnull(pa_adjvl,0)))>2";
			  
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Payment receipt / Credit Note records in Party Ledger with adjustment amount mismatch",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER13={"pl_prttp","pl_prtcd","pl_docno","pl_adjvl","pl_adjvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER13);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is payRECECREPARTYLEAD");
			    }
			}
			private void extDEBITADJUVA()
			{
			    try
			    {
			    M_strSQLQRY ="select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl from mr_pltrn "; 
			    M_strSQLQRY+= " where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pl_adjvl>0 and pl_CMPCD + pl_prttp + pl_prtcd + pl_docno not in ";
			    M_strSQLQRY+= " (select pa_CMPCD + pa_prttp + pa_prtcd + pa_dbtno from mr_patrn where pa_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"') and SUBSTRING(pl_doctp,1,1) in ('2','3')";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Extra Debit Adjustment values (records) in MR_PLTRN",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER14={"pl_prttp","pl_prtcd","pl_docno","pl_adjvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER14);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is extDEBITADJUVA");
			    }
			}
			private void extCREITADJUVA()
			{
			    try
			    {
			    M_strSQLQRY ="select pl_prttp,pl_prtcd,pl_docno, isnull(pl_adjvl,0) pl_adjvl from mr_pltrn "; 
			    M_strSQLQRY+= " where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pl_adjvl>0 and pl_CMPCD + pl_prttp + pl_prtcd + pl_docno not in ";
			    M_strSQLQRY+= " (select pa_CMPCD + pa_prttp + pa_prtcd + pa_dbtno from mr_patrn where pa_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"') and SUBSTRING(pl_doctp,1,1) in ('0','1')";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Extra Credit Adjustment values (records) in MR_PLTRN",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER15={"pl_prttp","pl_prtcd","pl_docno","pl_adjvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER15);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is extCREDITADJUVA");
			    }
			}
			private void taxCALPREDFIND()
			{
			    try
			    {
			    M_strSQLQRY ="select pt_crdtp,pt_invno,pt_docrf,pt_ltxvl, round( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)  +( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)) *0.02 ,2)  ";
			    M_strSQLQRY += " from mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND abs(pt_ltxvl - round( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)  +( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)) *0.02 ,2) )>0.01 and pt_ltxvl>0 "; 
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Records in MR_PTTRN where Tax Calculation is not as per predefined rates (TDS, Cess, Surcharge etc.)",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER16={"pt_crdtp","pt_invno","pt_docrf","pt_ltxvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER16);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is taxCALPREDFIND");
			    }
			}
			private void taxCALPREDFIND1()
			{
			    try
			    {
			    M_strSQLQRY =" select pt_crdtp,pt_docrf,pt_atxvl, round((pt_pgrvl*0.12)+((pt_pgrvl*0.12)*0.02),2)   from mr_pttrn " ;
			    M_strSQLQRY +=	" where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_atxvl <> round((pt_pgrvl*0.12)+((pt_pgrvl*0.12)*0.02),2)  and pt_docrf in";
			    M_strSQLQRY += " (select tx_docno from co_txdoc where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and tx_doctp='CRA') order by pt_docrf";
			     
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Records in MR_PTTRN where Tax Calculation is not as per predefined rates (TDS, Cess, Surcharge etc.)",60));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER17={"pt_crdtp","pt_docrf","pt_atxvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER17);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is taxCALPREDFIND1");
			    }
			}
			private void taxRECORDFOUND()
			{
			    try
			    {
			    M_strSQLQRY =" select pt_crdtp,pt_docrf,pt_ltxvl, round( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)  +( ((pt_pgrvl+pt_atxvl)*0.05)  +   (((pt_pgrvl+pt_atxvl)*0.05)*0.10)) *0.02 ,2)  from mr_pttrn ";
			    M_strSQLQRY += " where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_ltxvl=0 and pt_docrf in (select tx_docno from co_txdoc where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and tx_doctp='CRD') order by pt_docrf";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Records in MR_PTTRN where Tax Record is found in CO_TXDOC but tax calculation has not taken place",85));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER18={"pt_crdtp","pt_docrf","pt_ltxvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER18);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is taxRECORDFOUND");
			    }
			}
			private void taxRECORDFOUND1()
			{
			    try
			    {
			    M_strSQLQRY ="select pt_crdtp,pt_docrf,pt_atxvl, round((pt_pgrvl*0.12)+((pt_pgrvl*0.12)*0.02),2)";
			    M_strSQLQRY += " from mr_pttrn where PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pt_atxvl=0 and pt_docrf in (select tx_docno from co_txdoc where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and tx_doctp='CRA') order by pt_docrf";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Records in MR_PTTRN where Tax Record is found in CO_TXDOC but tax calculation has not taken place",85));
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER19={"pt_crdtp","pt_docrf","pt_atxvl"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER19);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is taxRECORDFOUND1");
			    }
			}
			private void recordREPETSEQ()
			{
			    try
			    {
			    M_strSQLQRY ="select pl_prttp,pl_prtcd,pl_seqno,count(*) from mr_pltrn ";
			    M_strSQLQRY += " where PL_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pl_docdt>'06/30/2006'  and pl_stsfl <> 'X' "; 
			    M_strSQLQRY += " group by pl_prttp,pl_prtcd,pl_seqno having count(*)>1";
			    M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			    prnFMTCHR(dosREPORT,M_strBOLD);
	            dosREPORT.writeBytes(padSTRING('R'," ",10));
	            dosREPORT.writeBytes(padSTRING('R',"Records with repeating sequence number in MR_PLTRN",85));	            
	            prnFMTCHR(dosREPORT,M_strNOBOLD);
	            dosREPORT.writeBytes("\n\n");
			    
			    String[] staHEADER20={"pl_prttp","pl_prtcd","pl_seqno"};
			    
			    dspCHKLST(M_rstRSSET,staHEADER20);
			    
			    }catch(Exception L_EX) 
			    {
			        setMSG(L_EX,"This is recordREPETSEQ");
			    }
			}
			
			
			
			
			
	/**
	 * 
	 * @param LP_RSSET
	 * @param LP_ARRHDR
	 */		
			
		private void dspCHKLST(ResultSet LP_RSSET,String[] LP_ARRHDR)
		{
		   try
		   {
		       if(!LP_RSSET.next() || LP_RSSET==null)
		       {
		           dosREPORT.writeBytes(padSTRING('R'," ",10));
		           dosREPORT.writeBytes(padSTRING('R',"......No Records Found.......",50));
		           dosREPORT.writeBytes("\n\n");

		           return;
		       }
		           
			    for(int i=0;i<LP_ARRHDR.length ;i++)
			    {
			        dosREPORT.writeBytes(padSTRING('R'," ",10));
			        strFLDNM=LP_ARRHDR[i].toString().trim();
				    dosREPORT.writeBytes(padSTRING('L',strFLDNM,12));
			        //System.out.println(padSTRING('L',strFLDNM,10));
			    }
			    dosREPORT.writeBytes("\n");
			    
		        while(true)
		        {
				    for(int i=0;i<LP_ARRHDR.length ;i++)
				    {
				        dosREPORT.writeBytes(padSTRING('R'," ",10));
				        strFLDNM=LP_ARRHDR[i].toString() .trim();
					    strFLDVAL=LP_RSSET.getString(strFLDNM);
					   
					    dosREPORT.writeBytes(padSTRING('L',strFLDVAL,12));
				    }

			       
				    if(!LP_RSSET.next())
				        break;
				    dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;	
				
/*	                if(cl_dat.M_intLINNO_pbst > 60)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R'," ",6));
						crtLINE(75);
						prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}*/
		        }
			    dosREPORT.writeBytes("\n\n");
		    
		   }catch(Exception L_EX)
		   {
		       	setMSG(L_EX,"This is dspchkList");
		   }
		}  
		  
		  
	    private void prnHEADER()
	        {
	    		try
	    		{
	    		    /*
	    			cl_dat.M_intLINNO_pbst=0;
	    			cl_dat.M_PAGENO +=1;
	    	    	cl_dat.M_intLINNO_pbst = 0;
	    	    	dosREPORT.writeBytes(padSTRING('R'," ",10));//margin
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			prnFMTCHR(dosREPORT,M_strBOLD);
	    		    dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
	    			dosREPORT.writeBytes(padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,45));
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			dosREPORT.writeBytes(padSTRING('R',"Missing Invoice Party Ledger :",55));
	    			dosREPORT.writeBytes(padSTRING('L',"Page No    : " + cl_dat.M_PAGENO,15));
	    			prnFMTCHR(dosREPORT,M_strNOBOLD);
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",6));
	    			crtLINE(75);
	    			dosREPORT.writeBytes("\n");
	    			
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));//margin
	    			prnFMTCHR(dosREPORT,M_strBOLD);*/
	    			for(int i=0 ;i<dispHEADER.length ;i++)
	    			{    
	    			dosREPORT.writeBytes(padSTRING('R',dispHEADER[i].toString() .trim() ,20));
	    			}
	    			for(int i=0 ;i<dispHEADER.length ;i++)
	    			{    
	    			dosREPORT.writeBytes(padSTRING('R',dispHEADER1[i].toString() .trim() ,20));
	    			}
	    			
	    			
	    			
	    			/*prnFMTCHR(dosREPORT,M_strNOBOLD);
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",58));
	    			
	    			//dosREPORT.writeBytes(padSTRING('L',"Approx. in Days",28));
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",6));
	    			crtLINE(75);
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;*/
	    			
	    		}catch(Exception L_EX)
	    		{
	    		    setMSG(L_EX,"This is prnHEADER");
	    		}
	        }
		  /*To print the the screen  fetch & display the  Records..........*/
		    private void outRECPRN()
		    {
				try
				{
				    dosREPORT.writeBytes(padSTRING('R'," ",10));
				   
				    dosREPORT.writeBytes(padSTRING('R',strFLDVAL,20));
				    dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst +=1;
				    
				}catch(Exception L_EX)
				{
				    setMSG(L_EX,"This is outRECPRN");
				}
		    }
			
		
	 
	   
	    /**
		   * Method to create Lines used in Reports
		   */
		  	private void crtLINE(int LM_CNT){
		  		String strln = "";
		  		try{
		  		for(int i=1;i<=LM_CNT;i++){
		  		 strln += "-";
		  		}
		  		dosREPORT.writeBytes(padSTRING('L'," ",intLMRGN));
		  		dosREPORT.writeBytes(strln);
		  		}
		  		catch(Exception L_EX){
		  			System.out.println("L_EX Error in Line:"+L_EX);
		  		}
		  	}
		  
	
}	