
/*
 * Created on Jun 27, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * @author SSR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.awt.event.*;
import java.awt.*;
import java.io.*;

import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.event.*;

public class mr_rpres extends cl_rbase 
{
    
    private int intRECCT;
    private String strFILNM; /** Data OutputStream for generating Report File.*/
    private FileOutputStream fosREPORT;/** File OutputStream Object for file handling.*/
	
    private DataOutputStream dosREPORT;
    private int intLMRGN;
    private String strRESNO;
    private String strPRDCD;
    private String strPRDDS;
    private String strLOTNO;
    private String strRCLNO;
    private String strPRTNM;
    private String strSTKQT;
    private String strRESQT_TOT;
    private String strRESQT;
    private String strBALQT;
    private double dblBALQT;
    private String strENDDT;	
 

    mr_rpres()
    {
        super(1);
		try
		{
		    setMatrix(20,8);
			
			 M_pnlRPFMT.setVisible(true);		
		    
		   
		    
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"Constructor");
		}
        
    }
    
    /** Perform the actionperformed the RUN & CLEAR */
	  public void actionPerformed(ActionEvent L_AE)
	  {
	    	super.actionPerformed(L_AE);
		try
		{
		    
		    if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{   
				//if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)))
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{				
					//txtMAINL.setText(M_strSBSCD.substring(0,2));
				    M_lblTODAT.setVisible( false);
				    M_txtTODAT.setVisible( false);
				    M_lblFMDAT.setVisible( true);
				    M_txtFMDAT.setVisible( true);

					M_lblFMDAT.setText("As On : ");
				    M_txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
					
					M_txtFMDAT.requestFocus();
					setMSG("Please Select Option..",'N');					
					setENBL(true);
				}
				else
				{					
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					setMSG("select option..",'N');
					setENBL(false);
				}	
			}		
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
                    cl_dat.M_btnSAVE_pbst.requestFocus();
                
                
            }
        /*    if(M_objSOURC ==M_txtTODAT)
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
                
                
                
            }*/
			
			
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Action Performed");
		}	
	  }
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
			           
			           
	               }
	        }catch(Exception L_EX)
	        {
	            setMSG(L_EX,"KeyPressed");
	        }
	    }
	   
	    
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
		/*		if(M_txtTODAT.getText().trim().length() == 0)
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
				}*/
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
	    		
	    			prnFMTCHR(dosREPORT,M_strBOLD);
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    		    dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,60));
	    			dosREPORT.writeBytes(padSTRING('L',"Report Date: " + cl_dat.M_strLOGDT_pbst ,40));
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			dosREPORT.writeBytes(padSTRING('R',"Reservation Gradewise/Lotwise Report As On : "+M_txtFMDAT.getText().toString().trim()  ,66));
	    			dosREPORT.writeBytes(padSTRING('L',"Page No    : " + cl_dat.M_PAGENO,25));
	    			prnFMTCHR(dosREPORT,M_strNOBOLD);
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",7));
	    			crtLINE(106);
	    			dosREPORT.writeBytes("\n");
	    			
	    			cl_dat.M_intLINNO_pbst+=1;
	    			
	    			prnFMTCHR(dosREPORT,M_strBOLD);
	    			
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			dosREPORT.writeBytes(padSTRING('R',"Grade ",10));
	    			dosREPORT.writeBytes(padSTRING('R',"Lot.No",10));
	    			dosREPORT.writeBytes(padSTRING('R',"Customer",30));
	    			dosREPORT.writeBytes(padSTRING('L',"Current",10));
	    			dosREPORT.writeBytes(padSTRING('L',"Reserved",10));
	    			dosREPORT.writeBytes(padSTRING('L',"Avlbl.",10));
	    			dosREPORT.writeBytes(padSTRING('L',"Expiry",12));
	    			dosREPORT.writeBytes(padSTRING('L',"Res.",10));
	    			dosREPORT.writeBytes("\n");
	    			
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			dosREPORT.writeBytes(padSTRING('R'," ",30));
	    			dosREPORT.writeBytes(padSTRING('L',"Stock",10));
	    			dosREPORT.writeBytes(padSTRING('L',"Qty.",10));
	    			dosREPORT.writeBytes(padSTRING('L',"Qty.",10));
	    			dosREPORT.writeBytes(padSTRING('L',"Date",12));
	    			dosREPORT.writeBytes(padSTRING('L',"No.",10));
	    			dosREPORT.writeBytes("\n");
	    			prnFMTCHR(dosREPORT,M_strNOBOLD);
	    			cl_dat.M_intLINNO_pbst+=2;
	    			dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			
	    			//dosREPORT.writeBytes(padSTRING('L',"Approx. in Days",28));
	    			dosREPORT.writeBytes("\n");
	    			cl_dat.M_intLINNO_pbst+=1;
	    			dosREPORT.writeBytes(padSTRING('R'," ",7));
	    			crtLINE(106);
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
	        		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpres.html";
	        		    else if(M_rdbTEXT.isSelected())
	        				strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpres.doc";	
	        							
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
	        		    dosREPORT = new DataOutputStream(fosREPORT);
	        		    int RECCT  = 0 ;
	        	        cl_dat.M_intLINNO_pbst=0;
	           		    setMSG("Report Generation in Process....." ,'N');
	           		    setCursor(cl_dat.M_curWTSTS_pbst);
	           		    if(M_rdbHTML.isSelected())
	        		    {
	        		        dosREPORT.writeBytes("<HTML><HEAD><Title>Reservation Gradewise/Lotwise Details</Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");
	        		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
	        		        dosREPORT.writeBytes("</STYLE>");
	        			 }
	        			 else
	        				prnFMTCHR(dosREPORT,M_strCPI12);
	        		   	 prnHEADER();/****gets the header of the report*/
	        		   	// M_strSQLQRY="select pr_prdds,st_lotno,min(pt_prtnm) pt_prtnm,sum(isnull(st_stkqt,0)) st_stkqt,";
	        		   	//M_strSQLQRY+=" isnull(sr_resqt,0) sr_resqt,(isnull(sr_resqt,0)-isnull(sr_rdsqt,0)) sr_balqt,sr_enddt from ";
	        		   	//M_strSQLQRY+= " fg_stmst ,fg_srtrn,co_ptmst,co_prmst  where  pt_prttp='C' and pt_grpcd=sr_grpcd ";
	        		   	//M_strSQLQRY +=" and sr_prdcd=pr_prdcd and st_lotno=sr_lotno  and st_prdcd=sr_prdcd ";
	        		   	//M_strSQLQRY+= " and (isnull(sr_resqt,0)-isnull(sr_rdsqt,0))>0   and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText() .toString())) +"' ";
	        		   	//M_strSQLQRY+= " and isnull(st_stkqt,0)>0  group by pr_prdds,st_lotno,isnull(sr_resqt,0) ,";
	        		   	//M_strSQLQRY+= " (isnull(sr_resqt,0)-isnull(sr_rdsqt,0)),sr_enddt  order by pr_prdds,st_lotno";
	        		   	 M_strSQLQRY="select sr_resno,sr_prdcd,pr_prdds,sr_prdtp,sr_lotno,sr_rclno,min(pt_prtnm) pt_prtnm,";
	        		   	M_strSQLQRY+=" isnull(sr_resqt,0) sr_resqt,(isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0))) sr_balqt,sr_enddt from ";
	        		   	M_strSQLQRY+= " fg_srtrn,co_ptmst,co_prmst  where  pt_prttp='C' and pt_grpcd=sr_grpcd ";
	        		   	M_strSQLQRY +=" and sr_prdcd=pr_prdcd ";
	        		   	M_strSQLQRY+= " and SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0)))>0   and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText() .toString())) +"' ";
	        		   	M_strSQLQRY+= " group by sr_resno,sr_prdcd,pr_prdds,sr_prdtp,sr_lotno,sr_rclno,isnull(sr_resqt,0) ,";
	        		   	M_strSQLQRY+= " (isnull(sr_resqt,0)-(isnull(sr_rdsqt,0)+isnull(sr_ralqt,0))),sr_enddt  order by sr_prdcd,pr_prdds,sr_prdtp,sr_lotno,sr_rclno";
						System.out.println(M_strSQLQRY);
	        		   	M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
	        		   	int i=0;
	        		   	while(M_rstRSSET.next() )
	        		   	{
	        		   	    strRESNO=M_rstRSSET.getString("sr_resno");
	        		   	    strPRDCD=M_rstRSSET.getString("sr_prdcd");
	        		   	    strPRDDS=M_rstRSSET.getString("pr_prdds");
	        		   	    strLOTNO=M_rstRSSET.getString("sr_lotno");
	        		   	    strRCLNO=M_rstRSSET.getString("sr_rclno");
	        		   	    strPRTNM=M_rstRSSET.getString("pt_prtnm");
	        		   	    //strRESQT=M_rstRSSET.getString("sr_resqt");
	        		   	    strBALQT=M_rstRSSET.getString("sr_balqt");
	        		   	    strSTKQT=getSTKQT(M_rstRSSET.getString("sr_prdtp"),M_rstRSSET.getString("sr_lotno"),M_rstRSSET.getString("sr_rclno"));
	        		   	    strRESQT_TOT=getRESQT_TOT(M_rstRSSET.getString("sr_prdtp"),M_rstRSSET.getString("sr_lotno"),M_rstRSSET.getString("sr_rclno"));
	        		   	    dblBALQT=(Double.parseDouble(strSTKQT))- Double.parseDouble(strRESQT_TOT);
	        		   	    strENDDT=M_rstRSSET.getString("sr_enddt");
	        		   		i++;	  
	        				outRECPRN(); 
	        				if(cl_dat.M_intLINNO_pbst > 60)
	        				{
	     					dosREPORT.writeBytes("\n");
	     					dosREPORT.writeBytes(padSTRING('R'," ",7));
	     					crtLINE(106);
	     					prnFMTCHR(dosREPORT,M_strEJT);
	     					prnHEADER();
	        				}   
	        		   	 
	        		   	}
	        		   	dosREPORT.writeBytes("\n");
	        			cl_dat.M_intLINNO_pbst+=1;
	        			dosREPORT.writeBytes(padSTRING('R'," ",7));
	        			crtLINE(106);
	        			prnFMTCHR(dosREPORT,M_strEJT);
	        			prnFMTCHR(dosREPORT,M_strNOCPI17);
	        			prnFMTCHR(dosREPORT,M_strCPI10);
	        			dosREPORT.close();
	        			fosREPORT.close(); 
	        		}catch(Exception L_EX)
	        		{
	        		    setMSG(L_EX,"......getDATA.....");
	        		}
	        	 }

			/**
			 */
			private String getSTKQT(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO)	  
			{	  
				String L_strRETSTR1 = "0.000";
				String L_strRETSTR2 = "0.000";
				try
				{
	        		   	M_strSQLQRY="select isnull(sum(isnull(st_stkqt,0)),0) st_stkqt from fg_stmst where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND st_prdtp='"+LP_PRDTP+"' and st_lotno = '"+LP_LOTNO+"' and st_rclno = '"+LP_RCLNO+"'";
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET.next() && L_rstRSSET != null)
						{
							L_strRETSTR1 = L_rstRSSET.getString("ST_STKQT");
							L_rstRSSET.close();
						}
						//System.out.println(M_strSQLQRY);
					
						/*
	        		   	M_strSQLQRY="select sum(isnull(ist_issqt,0)) ist_issqt from fg_istrn where ist_stsfl='2' and ist_prdtp='"+LP_PRDTP+"' and ist_lotno = '"+LP_LOTNO+"' and ist_rclno = '"+LP_RCLNO+"' and ist_issno in (select ivt_ladno from mr_ivtrn where isnull(ivt_invqt,0)=0 and isnull(ivt_ladqt,0)>0 and ivt_prdcd= '"+strPRDCD+"' and ivt_stsfl not in ('D','X'))";
						System.out.println(M_strSQLQRY);
						L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET.next() && L_rstRSSET != null)
						{
							L_strRETSTR2 = nvlSTRVL(L_rstRSSET.getString("IST_ISSQT"),"0.000");
							L_rstRSSET.close();
						}*/
						
	        		}catch(Exception L_EX)
	        		{
	        		    setMSG(L_EX,"getSTKQT");
	        		}
				return setNumberFormat(Double.parseDouble(L_strRETSTR1)+Double.parseDouble(L_strRETSTR2),3);
	        }

				  
			/**
			 */
			private String getRESQT_TOT(String LP_PRDTP,String LP_LOTNO,String LP_RCLNO)	  
			{	  
				String L_strRETSTR = "0.000";
				try
				{
	        		   	M_strSQLQRY="select isnull(sum(isnull(sr_resqt,0)-isnull(sr_ralqt,0)-isnull(sr_rdsqt,0)),0) sr_resqt from fg_srtrn where SR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND sr_prdtp='"+LP_PRDTP+"' and sr_lotno = '"+LP_LOTNO+"' and sr_rclno = '"+LP_RCLNO+"' and (isnull(sr_resqt,0)-isnull(sr_ralqt,0)-isnull(sr_rdsqt,0))>0 and sr_enddt>= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(M_txtFMDAT.getText() .toString())) +"' ";
						//System.out.println(M_strSQLQRY);
						ResultSet L_rstRSSET=cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET.next() && L_rstRSSET != null)
						{
							L_strRETSTR = L_rstRSSET.getString("SR_RESQT");
							L_rstRSSET.close();
						}
	        		}catch(Exception L_EX)
	        		{
	        		    setMSG(L_EX,"getRESQT_TOT");
	        		}
				return L_strRETSTR;
	        }
				  
				  
			/**
            * 
            */ 
			private void outRECPRN() 
			{
			    try
			    {
			        dosREPORT.writeBytes(padSTRING('R'," ",10));
	    			dosREPORT.writeBytes(padSTRING('R',strPRDDS ,10));
	    			dosREPORT.writeBytes(padSTRING('R',strLOTNO,10));
	    			dosREPORT.writeBytes(padSTRING('R',strRCLNO,4));
	    			dosREPORT.writeBytes(padSTRING('R',strPRTNM,30));
			       
	    			dosREPORT.writeBytes(padSTRING('L',strSTKQT,10));
	    			dosREPORT.writeBytes(padSTRING('L',strBALQT,10));
			      
	    			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblBALQT,3),10));
	    			dosREPORT.writeBytes(padSTRING('L',strENDDT,12));
	    			dosREPORT.writeBytes(padSTRING('L',strRESNO,10));
	    			dosREPORT.writeBytes("\n");
    				cl_dat.M_intLINNO_pbst +=1;
			        
			        
			    }catch(Exception L_EX)
			    {
			        setMSG(L_EX,"OUTRECPRN");
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
