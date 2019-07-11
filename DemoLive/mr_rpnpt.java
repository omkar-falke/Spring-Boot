/**
 * MODULE:
 * Program :DATA TRANSFER TO NARIMAN POINT
 * Author:
 * Modification :
 * 
 * Column Name	Table Name	Type Size
PI_PINNO	MR_PIMST                       	
PI_PINDT		
Pi_pdsds		
pi_ohtrf		
pi_mkttp		
pi_cnscd		
pi_adlno     		
		
	
IVT_GINNO	MR_IVTRN
ivt_ladno	
ivt_lryno	
ivt_cntds	
ivt_prdcd	
	
	
pt_CNSCD	MR_PIMST
pt_prtnm	
pt_prtcd	
	
WB_GOTDT	MM_WBTRN
wb_docno	
wb_gindt	
wb_gotdt	
pr_prdcd	CO_PRMST
pr_prdds	
	
	
	
pt_prtcd	CO_PTMST
pt_prtnm	
pt_prttp	
	
	
cmt_cgmtp	CO_CDTRN
cmt_cgstp		
cmt_codcd		
cmt_codds		
cmt_shrds		

 */



import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.lang.*;
import java.util.Properties;
import java.util.*; 
import java.util.Date;
import java.io.*;
import java.text.*;
public class mr_rpnpt extends cl_rbase
{
    
    JButton btnRUN;
    String strLRYNO,strADLNO,strOTHRF,strPRDDS,strFCLTP,strPRDQT,strPRDVL,strPINDT,strBYRRF,strPRTNM,strCNTTP,strPDSDS,strYYMM,strCNTDS,strTSLNO;
    FileOutputStream fosREPORT;
    DataOutputStream dosREPORT;
    String strFILNM;
    int intLMRGN,intSRLNO=0;
    Date dtGOTDT,dtGINDT;
    String strGOTDT,strGINDT,strQRDAT1 ;
    
   DateFormat df;
    
    
    
   
    mr_rpnpt()
    {
        super(1);
        try
        {
   

           // add(btnRUN=new JButton("RUN"),2,2,1,1,this,'L');
             
            M_pnlRPFMT.setVisible(true);
        }catch(Exception L_EX)
        {
            setMSG(L_EX,"This is Constructor");
            
        }
    }
    /**
     * Enabled Function is Selected the Screen Combobox Enabled the Companents
     */
    void setENBL(boolean L_flgSTAT)
	{   
        
        super.setENBL(L_flgSTAT);
        try
        {
            
        
        M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));       // Convert Into Local Date Format
		M_calLOCAL.add(Calendar.DATE,-1);                     // Increase Date from +1 with Locked Date
		strQRDAT1 = M_fmtLCDAT.format(M_calLOCAL.getTime());
		M_txtFMDAT.setText(strQRDAT1);
		M_txtTODAT.setText(strQRDAT1);
        }catch(Exception L_EX)
        {
            setMSG(L_EX,"setEnabled");
        }
        
	} 
    /**
     
     */
  /*  public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == btnRUN)
		{
		    getALLREC();
		}
	}*/
    /**
     * Key Events on the TextFields  
     */
    public void keyPressed(KeyEvent L_KE)
    {
        super.keyPressed(L_KE);
        try
        {
		        if (L_KE.getKeyCode() ==L_KE.VK_ENTER)
		        {
		            if(M_objSOURC ==M_txtFMDAT)
		            {
		                if(M_txtFMDAT.getText().trim().length() == 0)
		    			{
		    				setMSG("Enter From Date..",'E');
		    				M_txtFMDAT.requestFocus();
		    			}
		                else
		                    M_txtTODAT.requestFocus();    
		                if(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
		    			{
		    				setMSG("Date Should Not Be Grater Than Today..",'E');
		    				M_txtFMDAT.requestFocus();
		    			
		    			}
		                else
		                    M_txtTODAT.requestFocus();
		                
		                
		            }
		            if(M_objSOURC ==M_txtTODAT)
		            {
		                if(M_txtTODAT.getText().trim().length() == 0)
		    			{
		    				setMSG("Enter From Date..",'E');
		    				M_txtFMDAT.requestFocus();
		    				
		    			}
		                if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
		    			{
		    				setMSG("Date Should Not Be Grater Than Today..",'E');
		    				M_txtTODAT.requestFocus();
		    				
		    			}
		                else if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))<0)		    			{
		    				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
		    				M_txtTODAT.requestFocus();
		    				
		    			}
		    			 else
		    			     cl_dat.M_btnSAVE_pbst.requestFocus();	
		                
		                
		                
		            }
            
               }
        }catch(Exception L_EX)
        {
            setMSG(L_EX,"KeyPressed");
        }
    }
    
  /*  private void getALLREC()
    {
        try
        {
            M_strSQLQRY = "select substr(char(PI_PINDT),3,2)||substr(char(PI_PINDT),6,2) PI_YYMM, PIT_CNTDS, PIT_TSLNO, PI_PDSDS, PIT_CNTTP, PT_PRTNM, PI_BYRRF, PI_PINDT, PR_PRDDS, PIT_PRDQT, PIT_PRDVL, PI_OTHRF, PI_ADLNO, WB_GINDT, WB_GOTDT, IVT_LRYNO from MR_PIMST, MR_PITRN, MR_IVTRN, MM_WBTRN, CO_PRMST, CO_PTMST"; 
            M_strSQLQRY += " where PI_MKTTP=PIT_MKTTP and PI_PINNO = PIT_PINNO";
            M_strSQLQRY += " and PIT_MKTTP = IVT_MKTTP and PIT_LADNO=IVT_LADNO and PIT_PRDCD = IVT_PRDCD and IVT_GINNO = WB_DOCNO and WB_DOCTP='03' and date(WB_GOTDT) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+"'and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'"; 
            M_strSQLQRY += " and PT_PRTTP='C' and PI_CNSCD = PT_PRTCD and PIT_PRDCD = PR_PRDCD"; 
            M_strSQLQRY += " order by PI_YYMM, PI_PINDT, PIT_CNTDS, PR_PRDDS";
            M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
            while(M_rstRSSET.next())
            {
                
               strYYMM= M_rstRSSET.getString("PI_YYMM");
               System.out.println(strYYMM);
               
               strCNTDS= M_rstRSSET.getString("PIT_CNTDS");
               System.out.println("strCNTDS");
               strTSLNO=M_rstRSSET.getString("PIT_TSLNO");
               System.out.println("strTSLNO");
               strPDSDS=M_rstRSSET.getString("PI_PDSDS");
               System.out.println("strPDSDS");
               strCNTTP=M_rstRSSET.getString("PIT_CNTTP");
               System.out.println("strCNTTP");
               strPRTNM=M_rstRSSET.getString("PT_PRTNM");
               System.out.println("strPRTNM");
               strBYRRF=M_rstRSSET.getString("PI_BYRRF");
               System.out.println("strBYRRF");
               strPINDT=M_rstRSSET.getString("PI_PINDT");
               strPRDDS=M_rstRSSET.getString("PR_PRDDS");
                strPRDQT=M_rstRSSET.getString("PIT_PRDQT");
                strPRDVL=M_rstRSSET.getString("PIT_PRDVL");
                strOTHRF=M_rstRSSET.getString("PI_OTHRF");
                strADLNO=M_rstRSSET.getString("PI_ADLNO");
              dtGINDT=M_rstRSSET.getDate("WB_GINDT");
              dtGOTDT=M_rstRSSET.getDate("WB_GOTDT");
                strLRYNO=M_rstRSSET.getString("IVT_LRYNO"); 
                
            }
            
        }catch(Exception L_EX)
        {
            setMSG(L_EX,"This is getAllrec");
        }
        
    } */
    /**
	 * Method to validate the Inputs before execuation of the query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(M_txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				M_txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				M_txtFMDAT.requestFocus();
				return false;
			}
			if(M_txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
				M_txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
				M_txtTODAT.requestFocus();
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
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In vldDATA");
			return false;
		}
		return true;
	}
    
	/**
	 * Get The Print Header on Report
	 *
	 */
        private void prnHEADER()
        {
    		try
    		{
    		    
    			cl_dat.M_intLINNO_pbst=0;
    			cl_dat.M_PAGENO +=1;
    	    	cl_dat.M_intLINNO_pbst = 0;
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst+=1;
    			//prnFMTCHR(dosREPORT,M_strBOLD);
    		    dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,25));
    			dosREPORT.writeBytes(padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,265));
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst+=1;
    			dosREPORT.writeBytes(padSTRING('R',"Export Proforma Invoice details for the period : "+M_txtFMDAT.getText().toString().trim() +" To " +M_txtTODAT.getText().toString().trim() ,100));
    			dosREPORT.writeBytes(padSTRING('L',"Page No    : " + cl_dat.M_PAGENO,190));
    			//prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst+=1;
    			crtLINE(295);
    			dosREPORT.writeBytes("\n");
    			
    			cl_dat.M_intLINNO_pbst+=1;
    			//prnFMTCHR(dosREPORT,M_strBOLD);
    			dosREPORT.writeBytes(padSTRING('R',"Mon ",5));
    			dosREPORT.writeBytes(padSTRING('R',"S.No",7));
    			dosREPORT.writeBytes(padSTRING('R',"Contr",17));
    			dosREPORT.writeBytes(padSTRING('R',"Seal No",15));
    			dosREPORT.writeBytes(padSTRING('R',"Port",22));
    			dosREPORT.writeBytes(padSTRING('R',"FCL",18));
    			dosREPORT.writeBytes(padSTRING('R',"Party",30));
    			dosREPORT.writeBytes(padSTRING('R',"Ord.No",15));
    			dosREPORT.writeBytes(padSTRING('R',"StuffDt",10));
    			dosREPORT.writeBytes(padSTRING('R',"Line",6));
    			dosREPORT.writeBytes(padSTRING('R',"Product",15));
    			dosREPORT.writeBytes(padSTRING('L',"Qty(MT)",14));
    			dosREPORT.writeBytes(padSTRING('L',"CIF/USD",14));
    			dosREPORT.writeBytes(padSTRING('L'," ",3));
    			dosREPORT.writeBytes(padSTRING('R',"ARE.No",20));
    			dosREPORT.writeBytes(padSTRING('R',"FileNo",20));
    			dosREPORT.writeBytes(padSTRING('R',"InTime",26));
    			dosREPORT.writeBytes(padSTRING('R',"OutTime",26));
    			dosREPORT.writeBytes(padSTRING('R'," ",3));
    			dosREPORT.writeBytes(padSTRING('R',"Lorry No",21));
    			//prnFMTCHR(dosREPORT,M_strNOBOLD);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst+=1;
    			dosREPORT.writeBytes(padSTRING('R'," ",58));
    			
    			//dosREPORT.writeBytes(padSTRING('L',"Approx. in Days",28));
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst+=1;
    			crtLINE(295);
    			dosREPORT.writeBytes("\n");
    			cl_dat.M_intLINNO_pbst+=1;
    		}catch(Exception L_EX){
    			System.out .println ("L_EX..prnhead....... :"+L_EX);
    		}
         }
        /** This is used for to print the report on the specified Location " Word Format & HTML Format printer & Fax "
         */	
        	void exePRINT()
        	{   
        	    if (!vldDATA())
        			return;
        			try
        			{
        	        int RECCT  = 0 ;
        	   		    
           		    setMSG("Report Generation in Process....." ,'N');
           		    setCursor(cl_dat.M_curWTSTS_pbst);
           		   if(M_rdbHTML.isSelected())
        		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpnpt.html";
        		    else if(M_rdbTEXT.isSelected())
        				strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpnpt.txt";	
        							
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
        		    //System.out.println("sss");
        		    //System.out.println("th file name is " +strFILNM);
        		    fosREPORT = new FileOutputStream(strFILNM);
        		    //System.out.println("th file name is " +strFILNM);
        		    dosREPORT = new DataOutputStream(fosREPORT);
        		    //System.out.println("th file name is " +strFILNM);
        		    
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
        				prnFMTCHR(dosREPORT,M_strCPI10);
        		      
           		      		    
        		   	 prnHEADER();/****gets the header of the report*/
        		   	    M_strSQLQRY = "select SUBSTRING(convert(varchar,PI_PINDT,103),7,2) + SUBSTRING(convert(varchar,PI_PINDT,103),4,2) PI_YYMM, PIT_CNTDS, PIT_TSLNO, PI_PDSDS, PIT_CNTTP, PT_PRTNM, PI_BYRRF, PI_PINDT, PR_PRDDS, PIT_PRDQT, PIT_PRDVL, PI_OTHRF, PI_ADLNO, WB_GINDT, WB_GOTDT, IVT_LRYNO from MR_PIMST, MR_PITRN, MR_IVTRN, MM_WBTRN, CO_PRMST, CO_PTMST"; 
        	            M_strSQLQRY += " where PI_CMPCD=PIT_CMPCD and PI_MKTTP=PIT_MKTTP and PI_PINNO = PIT_PINNO";
        	            M_strSQLQRY += " and PIT_MKTTP = IVT_MKTTP and PIT_CMPCD = IVT_CMPCD and PIT_LADNO=IVT_LADNO and PIT_PRDCD = IVT_PRDCD and IVT_CMPCD = WB_CMPCD and IVT_GINNO = WB_DOCNO and WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP='03' and CONVERT(varchar,WB_GOTDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText().trim()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtTODAT.getText().trim()))+"'"; 
        	            M_strSQLQRY += " and PT_PRTTP='C' and PI_CNSCD = PT_PRTCD and PIT_PRDCD = PR_PRDCD"; 
        	            M_strSQLQRY += " order by PI_YYMM, PI_PINDT, PIT_CNTDS, PR_PRDDS";
        	            M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
        	            //System.out.println(M_strSQLQRY);
        	            int i=0;
        	            while(M_rstRSSET.next())
        	            {
        	                
        	               strYYMM= M_rstRSSET.getString("PI_YYMM");
        	               //System.out.println(strYYMM);
        	               strCNTDS= M_rstRSSET.getString("PIT_CNTDS");
        	               //System.out.println(strCNTDS);
        	               strTSLNO=M_rstRSSET.getString("PIT_TSLNO");
        	               //System.out.println(strTSLNO);
        	               strPDSDS=M_rstRSSET.getString("PI_PDSDS");
        	               strCNTTP=M_rstRSSET.getString("PIT_CNTTP");
        	               //System.out.println(strCNTTP);
        	               strPRTNM=M_rstRSSET.getString("PT_PRTNM");
        	               strBYRRF=M_rstRSSET.getString("PI_BYRRF");
        	               strPINDT=M_rstRSSET.getString("PI_PINDT");
        	               strPRDDS=M_rstRSSET.getString("PR_PRDDS");
        	               strPRDQT=M_rstRSSET.getString("PIT_PRDQT");
        	               strPRDVL=M_rstRSSET.getString("PIT_PRDVL");
        	               strOTHRF=M_rstRSSET.getString("PI_OTHRF");
        	               strADLNO=M_rstRSSET.getString("PI_ADLNO");
        	               strGINDT=M_rstRSSET.getString("WB_GINDT");
        	               strGOTDT=M_rstRSSET.getString("WB_GOTDT");
        	               strLRYNO=M_rstRSSET.getString("IVT_LRYNO"); 
        	                i++;
        	                outRECPRN();
        	                if(cl_dat.M_intLINNO_pbst > 60)
        					{
        						dosREPORT.writeBytes("\n");
        						crtLINE(295);
        						prnFMTCHR(dosREPORT,M_strEJT);
        						prnHEADER();
        					}
        	                
        	             }
        	            dosREPORT.writeBytes("\n");
        				cl_dat.M_intLINNO_pbst+=1;
        				
        				crtLINE(295);
        				prnFMTCHR(dosREPORT,M_strEJT);
        				//prnFMTCHR(dosREPORT,M_strNOCPI17);
        				//prnFMTCHR(dosREPORT,M_strCPI10);
        	            
        		   	 
        		   	 
        		   	 
        		}catch(Exception L_EX) 
        		{
        		    setMSG(L_EX," getDATA");
        		}
        	 }	
        	  
        	  /*To print the the screen  fetch & display the  Records..........*/
        	    private void outRECPRN()
        	    {
        		
        	        try{
        	            DateFormat fmt=DateFormat.getDateInstance(DateFormat.SHORT);
        				if(strCNTTP.equals("01"))
        				 {
        					strFCLTP="20 feet";
        				 }
        				 else if(strCNTTP.equals("02"))
        				 {
        				     strFCLTP="40 feet";
        				 }
        				 intSRLNO++;
        				
        				dosREPORT.writeBytes(padSTRING('R',strYYMM,5));
        				dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),7));
        				
        				dosREPORT.writeBytes(padSTRING('R',strCNTDS,17));
        				dosREPORT.writeBytes(padSTRING('R',strTSLNO,15));
        				dosREPORT.writeBytes(padSTRING('R',strPDSDS,22));
        				dosREPORT.writeBytes(padSTRING('R',strFCLTP,18));
        				dosREPORT.writeBytes(padSTRING('R',strPRTNM,30));
        				dosREPORT.writeBytes(padSTRING('R',strBYRRF,15));
        				dosREPORT.writeBytes(padSTRING('R',strPINDT,10));
        				dosREPORT.writeBytes(padSTRING('R',"--",6));
        				dosREPORT.writeBytes(padSTRING('R',strPRDDS,15));
        				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strPRDQT),3),14));
        				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(strPRDVL),2),14));
        				dosREPORT.writeBytes(padSTRING('R',"",3));
        				dosREPORT.writeBytes(padSTRING('R',strOTHRF,20));
        				dosREPORT.writeBytes(padSTRING('R',strADLNO,20));
        				
        				
        				dosREPORT.writeBytes(padSTRING('L',strGINDT.substring(0,16),26));
        				dosREPORT.writeBytes(padSTRING('L',strGOTDT.substring(0,16),26));
						dosREPORT.writeBytes(padSTRING('R',"",3));
        				dosREPORT.writeBytes(padSTRING('R',(strLRYNO),21));
        				dosREPORT.writeBytes("\n");
        				cl_dat.M_intLINNO_pbst +=1;
        				
        				
        	    	  
        	          
        			}catch(Exception L_EX){
        				System.out.println(L_EX);
        				}
        				
        		}       	  
        	  
        	  
   	/**
      * Method to create Lines used in Reports
     */
	  	private void crtLINE(int LM_CNT)
	  	{
	  		String strln = "";
	  		try
	  		{
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