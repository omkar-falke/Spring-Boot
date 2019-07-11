/*
System Name   : Finished Goods Inventory Management System
Program Name  : Customer Despatch Detail Report  
Program Desc. : Report regarding Customer Despatch details
Author        : Ms A M. Kulkarni
Date          : 26.04.2005
Version       : FIMS v2.0
Modifications 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Hashtable;
import java.io.FileOutputStream;
import java.io.DataOutputStream; 
import java.awt.Cursor.*;

/**<pre>
System Name : Finished Goods Inventory Management system
 
Program Name : Customer Despatch Detail Report 

Purpose : To generate the hard copy document for customer despatch details

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_PTMST       PT_PRTTP,PT_PRTCD                                       #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtPRTTP    PT_PRTTP       CO_PTMST      VARCHAR(1)     Party Type
txtPRTCD    PT_PRTCD       CO_PTMST      VARCHAR(5)     Party Code 
txtPRTNM    PT_PRTNM       CO_PTMST      VARCHAR(40)    Party Name
--------------------------------------------------------------------------------------
<B>Conditions Given in Query:</b>
      for Party Type
          Data is taken from CO_CDTRN for CMT_CGSTP ='MSTCOXXPRT'.
      for Party Zone
          Data is taken from CO_CDTRN for CMT_CGSTP ='SYSMRXXZON'. 
      for Party Code
          Data is taken from table CO_PTMST for given condition
             1) PT_PRTTP = Given Party Type.
             2) AND PT_PRTCD = given Part code.
      Report Data is Taken from CO_PTMST for given condition as
			 1) PT_PRTTP = Given Party Type.
                   2) AND PT_PRTCD = Given Party Code.
<I>
<B>Validations :</B>
    - Party Type must be valid, i.e. must be present in the DataBase.
    - Party Code must be valid, i.e. must be present in the DataBase.
</I> */

 class fg_rpcus extends cl_rbase
{  									
    private JTextField txtPRTCD;	  /** JTextField to display Party Code.*/
	private JTextField txtPRTNM;	  /** JTextField to display Party Name.*/
	private JTextField txtPRTTP;      /** JTextField to display Party Type.*/
	private JTextField txtDESTN;	  /** JTextField to display Destination.*/
		
	private boolean flgTRPFLT = false;   /** Filter flag to be activated when transporter enrollment is selected*/
	private FileOutputStream fosREPORT;  /** File Output Stream for File Handling */
	private DataOutputStream dosREPORT;  /** Data Output Stream for generating Report File */
    private String strFILNM;             /** String for generated Report File Name*/
    private int intRECCT = 0;            /** Integer for counting the records fetched from DB.*/
    private String strPRTDS;             /** String variable for Party Type Description.*/
    private Hashtable hstCODDS;          /** Hash Table to hold Zone Code Description & Party Code Description.*/
    private String strPREVDT;            /** Variable for previous date*/
    String L_strPRDDS , L_strISSQT;
    										                     
	fg_rpcus()
	{
	    super(2);
	    try
	    {			
		    setMatrix(20,8);
		
		   	add(new JLabel("Party Code"),6,4,1,.8,this,'L');
			add(new JLabel("Party Name"),7,4,1,.8,this,'L');
					
			add(txtPRTCD= new TxtLimit(5),6,5,1,1,this,'L');
			add(txtPRTNM= new JTextField(40),7,5,1,2.5,this,'L');
																 		
			txtPRTCD.setEnabled(false);
			txtPRTNM.setEnabled(false);
    		txtPRTCD.addActionListener(this);
    		txtPRTCD.addFocusListener(this);
    		txtPRTCD.addKeyListener(this);
    		txtPRTCD.setActionCommand("txtPRTCD");
            M_pnlRPFMT.setVisible(true);
            M_txtFMDAT.setText(strPREVDT);
                        
            M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));   // Convert Into Local Date Format
            M_calLOCAL.add(Calendar.DATE,-1);                               // Decrease Date by 1 from Login Date
            strPREVDT = M_fmtLCDAT.format(M_calLOCAL.getTime());            // Assign Date to Variable 
            
            M_txtFMDAT.setText(strPREVDT);
	    }
	    catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
	}
	
	/*
	 * Printing blank string, if not applicable
	 */
	private void exeBLKPRT(String LP_STRVL)
	{
		try
		  {
			if(flgTRPFLT==true)
				dosREPORT.writeBytes(padSTRING('R',"  ",LP_STRVL.length()));
			else
				dosREPORT.writeBytes(LP_STRVL);
	      }
	    catch(Exception L_EX)
		  {
			setMSG(L_EX," exeBLKRPT");
		  }	
	}	

	/**
	 * Skipping printing part if not applicable
	 **/
	void exeSKIPPRT(String LP_STRVL)
	{
		try
		  {
			if(flgTRPFLT==true)
				return;
			dosREPORT.writeBytes(LP_STRVL);			
	      }
	      catch(Exception L_EX)
		  {
			setMSG(L_EX," exeSKIPPRT");
		  }	
	}
		
	
	/**
	 * super class Method overrided to enhance its functionality, to enable & disable components 
	 * according to requirement.
	**/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		//txtPRTTP.setEnabled(L_flgSTAT);
		 M_txtFMDAT.setText(strPREVDT);
		 M_txtTODAT.setText(strPREVDT);
		 txtPRTCD.setEnabled(L_flgSTAT);						
		 txtPRTNM.setEnabled(false);				
	}
	
	/*public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE);
	}
	 */
	
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
 	                    System.out.println(M_txtFMDAT.getText()+"  -  "+M_txtTODAT.getText());
 	                    M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP = 'C' and isnull(PT_STSFL,'')<> 'X' and pt_prtcd in (select ivt_byrcd from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_mkttp in ('01','03','04','05') and CONVERT(varchar,ivt_invdt,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText()))+"') order by PT_PRTCD ";
 	                    M_strHLPFLD = "txtPRTCD";
 	                    System.out.println(M_strSQLQRY);
                        cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name"},2,"CT"); 
 	                }
 	                catch(Exception L_EX)
    			    {
    				    setMSG(L_EX ," F1 help..");    		    
    			    }  
 	            }
 	            setCursor(cl_dat.M_curDFSTS_pbst);
 	    }
	}
	
	/**
	Method for execution of help for Memo Number Field.
	*/
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
	}
	
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
		{				
			setMSG("Please enter Party Code Or Press F1 for Help..",'N');
			return;
		}
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rpcus.html";
		    else if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rpcus.doc";				
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,strPRTDS+" Enrollment Form"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}	
	}
	
    /*
     *Method to fetch data from CO_PTMST table & club it with Header in Data Output Stream.
	 */
	private void getALLREC()
	{
		try
		{
		    fosREPORT = new FileOutputStream(strFILNM);
		    dosREPORT = new DataOutputStream(fosREPORT);
		    intRECCT = 0 ;
		    cl_dat.M_intLINNO_pbst=0;
   		    setMSG("Report Generation in Process....." ,'N');
   		    
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		    {
		        prnFMTCHR(dosREPORT,M_strCPI17);
		        
		    }
   		    if(M_rdbHTML.isSelected())
		    {
		        System.out.println("KKK");
		        dosREPORT.writeBytes("<HTML><HEAD><Title>Customer Despatch Detail Report</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
		        dosREPORT.writeBytes("</STYLE>");
		    }
   		    prnHEADER();
		    
		    String L_ADDSTR = " and IVT_BYRCD = '"+txtPRTCD.getText()+"'";
		    if(txtPRTCD.getText().trim().length() != 5 )
		                L_ADDSTR = " ";
		                
		    double L_ISSQT = 0;
		    boolean L_1STFL = false;
		    
		                
		    M_strSQLQRY  = "Select IVT_PRDDS,IVT_DORNO,IVT_LRYNO,IVT_CNTDS,IVT_LADNO,IVT_INVNO,IVT_TRPCD,IVT_LR_NO,IVT_TSLNO,";
		    M_strSQLQRY += "IVT_RSLNO,CONVERT(varchar,IVT_INVDT,103) IVT_INVDT,IST_LOTNO,IST_RCLNO,sum(IST_ISSQT)ist_issqt FROM MR_IVTRN,FG_ISTRN";
		    M_strSQLQRY += " where IVT_CMPCD = IST_CMPCD and IVT_MKTTP = IST_MKTTP and IVT_LADNO = IST_ISSNO and ivt_PRDCD = ist_prdcd and ivt_pkgtp = ist_pkgtp";
		    M_strSQLQRY += " and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CONVERT(varchar,IVT_INVDT,103) BETWEEN '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()));
		    M_strSQLQRY += "' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"' and IST_STSFL='2'" + L_ADDSTR;
		    M_strSQLQRY += " group by IVT_PRDDS,IVT_DORNO,IVT_LRYNO,IVT_CNTDS,IVT_LADNO,IVT_INVNO,";
		    M_strSQLQRY += " IVT_TRPCD,IVT_LR_NO,IVT_TSLNO,IVT_RSLNO,CONVERT(varchar,IVT_INVDT,103),IST_LOTNO,IST_RCLNO";
		    M_strSQLQRY += " order by IVT_PRDDS,IVT_INVDT,IVT_DORNO,IVT_INVNO,IVT_LR_NO";
		    
	    	System.out.println(M_strSQLQRY);	    
		    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		    
		    int L_GRDCTR = 0;
		    double L_ISSQT_TOT = 0.000;
		    String L_PRDDS_OLD = "xxx";
		    
		    if(M_rstRSSET != null)
		    {
		        while(M_rstRSSET.next())
		        {
		            L_strPRDDS = M_rstRSSET.getString("IVT_PRDDS").trim();
		            L_strISSQT = M_rstRSSET.getString("IST_ISSQT").trim();
		            if(!L_strPRDDS.equals(L_PRDDS_OLD))
		            {
		                if(L_ISSQT_TOT > 0)
		                {
		                    if(L_GRDCTR > 1)
		                       dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_ISSQT_TOT,3),131));
		                        dosREPORT.writeBytes("\n");
		                        cl_dat.M_intLINNO_pbst +=1;
		                }
		                dosREPORT.writeBytes("\n");
		                 cl_dat.M_intLINNO_pbst +=1;
		                L_ISSQT_TOT = 0.000;
		                L_GRDCTR = 0;
		                L_PRDDS_OLD = L_strPRDDS;
		            }
		            
                    if(cl_dat.M_intLINNO_pbst >66)
				    {
				    	dosREPORT.writeBytes("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
					    cl_dat.M_intLINNO_pbst = 0;
					    cl_dat.M_PAGENO +=1;
					    if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
					    	prnFMTCHR(dosREPORT,M_strEJT);
					    if(M_rdbHTML.isSelected())
					    	dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					    prnHEADER();
				    }	
				    dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_DORNO"),""),10));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),""),10));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),""),13));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),""),15));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),10));
		            dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),""),18));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),""),10));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_LR_NO"),""),10));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),12));
		            dosREPORT.writeBytes("  ");
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),""),11));
		            dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("IST_ISSQT"),""),10));
		            dosREPORT.writeBytes("  ");
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IST_RCLNO"),""),12));
		            dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IVT_RSLNO"),""),8));
		            
		            
		            L_ISSQT_TOT += Double.parseDouble(L_strISSQT);
		            L_ISSQT += Double.parseDouble(L_strISSQT);
		            L_GRDCTR += 1;
		            
		            dosREPORT.writeBytes("\n");
		            cl_dat.M_intLINNO_pbst +=1;
		            /*if(L_ISSQT_TOT > 0)
		                {
		                    if(L_GRDCTR > 1)
		                        dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_ISSQT_TOT,3),130));
		                        dosREPORT.writeBytes("\n");
		                        cl_dat.M_intLINNO_pbst +=1;
		                }
		            	       */     
		            
		            if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
		               prnFMTCHR(dosREPORT,M_strBOLD);
		            if(M_rdbHTML.isSelected())
		               dosREPORT.writeBytes("<B>");
		            if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
		               prnFMTCHR(dosREPORT,M_strNOBOLD);
		            if(M_rdbHTML.isSelected())
		               dosREPORT.writeBytes("</B>");
		            //dosREPORT.writeBytes("\n");
		            //cl_dat.M_intLINNO_pbst++;
		            intRECCT = 1;
		        }
                dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_ISSQT_TOT,3),130));
                dosREPORT.writeBytes("\n");
                cl_dat.M_intLINNO_pbst +=1;
		         dosREPORT.writeBytes("\n");
		         dosREPORT.writeBytes("\n");
		         prnFMTCHR(dosREPORT,M_strBOLD);
		         dosREPORT.writeBytes(padSTRING('R'," ",107));
		         dosREPORT.writeBytes(padSTRING('R',"Total Qty",11));
		         dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_ISSQT,3),10));
		         dosREPORT.writeBytes("\n");
		         dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		         M_rstRSSET.close();
		    }
		    setMSG("Report completed.. ",'N');
		    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		 	  	        
    	}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getALLREC");
		}
	}
	
	/*
	 *Method to validate Party Type & Party Code,i.e. to check for blank and wrong Inputs.
	 */
	boolean vldDATA()
	{
	    if(txtPRTCD.getText().length()==0)
	    {
	        setMSG("Please enter Party Code and Press Enter key Or Press F1 to select from list..",'N');
	        txtPRTCD.requestFocus();
	        return true;
	    }
	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
	    {
	        if(M_cmbDESTN.getItemCount()==0)
	        {
	            setMSG("Please Select the Email/s from List through F1 Help..",'N');
	            return false;
	        }
	    }
	    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
	    {
	        if(M_cmbDESTN.getSelectedIndex()==0)
	        {
	            setMSG("Please Select Printer from the List..",'N');
	            return false;
	        }
	    }
		return true;
	}
	
	/*
	 * Method to generate the Header Part of the Report.
	 */ 
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
		        dosREPORT.writeBytes("\n");
		        dosREPORT.writeBytes("\n");
		        dosREPORT.writeBytes("\n");
		        dosREPORT.writeBytes(padSTRING('R'," ",35));
		        dosREPORT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst,90)+"\n\n");
		        dosREPORT.writeBytes(padSTRING('R'," ",38));
		        //if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&(M_rdbTEXT.isSelected()))
		        //     prnFMTCHR(dosREPORT,M_strBOLD);
		        dosREPORT.writeBytes(padSTRING('C',"CUSTOMER DESPATCH DETAILS",90)+"\n\n");
		        //if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&(M_rdbTEXT.isSelected()))
				//	prnFMTCHR(dosREPORT,M_strNOBOLD);	
		        dosREPORT.writeBytes(padSTRING('R',"Details of despatches made to " + txtPRTNM.getText().trim() + " from " + M_txtFMDAT.getText()+" to "+ M_txtTODAT.getText(),90));
		        dosREPORT.writeBytes(padSTRING('L',"Report Date :" + cl_dat.M_strLOGDT_pbst,60)+"\n");
		        
		        dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		        dosREPORT.writeBytes(padSTRING('R',"D.O No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"LA No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"Vehicle No.",13));
		        dosREPORT.writeBytes(padSTRING('R',"Container No.",15));
		        dosREPORT.writeBytes(padSTRING('R',"Invoice No.",13));
		        dosREPORT.writeBytes(padSTRING('R',"Invoice Date.",14));
		        dosREPORT.writeBytes(padSTRING('R',"Trnp Code",12));
		        dosREPORT.writeBytes(padSTRING('R',"LR No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"Grade",12));
		        dosREPORT.writeBytes("  ");
		        dosREPORT.writeBytes(padSTRING('R',"Lot No.",10));
		        dosREPORT.writeBytes(padSTRING('L',"Quantity",10));
		        dosREPORT.writeBytes("  ");
		        dosREPORT.writeBytes(padSTRING('R',"RCL No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"RSL No.",8));
		        if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))&&(M_rdbTEXT.isSelected()))
		            prnFMTCHR(dosREPORT,M_strNOBOLD);
		    }
		    else
		    {
		        dosREPORT.writeBytes("<B><CENTER><PRE style = \"font-size : 15 pt \">");
		        dosREPORT.writeBytes(" SUPREME PETROCHEM LIMITED"+"\n\n");
		        dosREPORT.writeBytes("</B></CENTER>");
		        dosREPORT.writeBytes("<B><CENTER>");
		        dosREPORT.writeBytes(" CUSTOMER DESPATCH DETAILS " + "\n");
		        dosREPORT.writeBytes("</B></CENTER></FONT></PRE>");
		        dosREPORT.writeBytes(padSTRING('R',"Details of despatches made to " + txtPRTNM.getText().trim() + " from " + M_txtFMDAT.getText()+" to "+ M_txtTODAT.getText(),90));
		        dosREPORT.writeBytes(padSTRING('L',"Report Date :" + cl_dat.M_strLOGDT_pbst,60)+"\n\n");   
		        dosREPORT.writeBytes("<PRE style = \" font-size : 10 pt \">");
		        dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		        dosREPORT.writeBytes(padSTRING('R',"D.O No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"LA No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"Vehicle No.",13));
		        dosREPORT.writeBytes(padSTRING('R',"Container No.",15));
		        dosREPORT.writeBytes(padSTRING('R',"Invoice No.",13));
		        dosREPORT.writeBytes(padSTRING('R',"Invoice Date.",14));
		        dosREPORT.writeBytes(padSTRING('R',"Trnp Code",12));
		        dosREPORT.writeBytes(padSTRING('R',"LR No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"Grade",12));
		        dosREPORT.writeBytes("  ");
		        dosREPORT.writeBytes(padSTRING('R',"Lot No.",10));
		        dosREPORT.writeBytes(padSTRING('L',"Quantity",10));
		        dosREPORT.writeBytes("  ");
		        dosREPORT.writeBytes(padSTRING('R',"RCL No.",10));
		        dosREPORT.writeBytes(padSTRING('R',"RSL No.",8));
		    }
		     dosREPORT.writeBytes("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		     cl_dat.M_intLINNO_pbst +=13;
		    	    
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
}
	
	/*
	 * Method to get the description of the Transporter & Destributer for corresponding code.
	 * @param P_strPRTTP String argument to pass the party Type.
	 * @param P_strPRTCD String argument to pass the party code.
	 */
