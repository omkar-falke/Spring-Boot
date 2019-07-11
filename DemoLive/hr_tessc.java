/**
 * Created on Jun 9, 2007
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


import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Calendar;	




/**
 * @author SSR
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class hr_tessc extends cl_pbase
{
            private TxtLimit txtDPTCD;
            private TxtLimit txtEMPCT;
            private JLabel lblDPTCD;
            private JLabel lblEMPCT;
            private TxtDate txtFMDT;
            private TxtDate txtTODT;
            private TxtLimit txtSHFPT;
            private TxtLimit txtSHFO;
            private TxtLimit txtSHIF;
			private JTextField TXT1;
			private JTextField TXT2;
            private ButtonGroup btnGRP,btnGRP1;
            private cl_JTable tblSHFPT;
			private boolean flgALLDPT = false;
            private String[] arrSUMHDR = new String[] {"0","1","2","3"};
            private String[] arrDATEHDR = new String[] {"0","1","2","3"};
			private JTextField[] arrDATECOL_TXT = new JTextField[] {TXT1,TXT2};
			private int[] arrSUMHDR_WD = new int[] {0,1,2,3};
        	private char[] arrSUMHDR_PAD = new char[] {'0','1','2','3'};
        	//String[] arrMONNM = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
            private Object[] arrHSTKEY;
            //private int intSUMFIX=4;
            private int intSUMFIX=5;
            private JPanel pnlTBL;
            private JTabbedPane tabMAIN;
            private Hashtable<String,String> hstDAYS;
            private Hashtable<String,String> hstDATECOL;
            private Vector vtrDAYS;
            private TreeMap oleSorted;
            private String strFULNM;
			//private StringTokenizer stkFULNM;
            private String strTODT;
            private String strFMDT;
            private String strEMPCT_CHK;
            private String strWRKDT;
            private String[] arrCOLDAY;
            private String strEMPNO;
            private String strSAPNO;
            private String strSTSFL;
            private String strSHFCD;
			private String strWHRSTR_SS;
			private String strCMPCD_LOGIN;
			private String strEMPNO_LOGIN;
            private JButton btnDISP;
            private JRadioButton rdbON;
            private JRadioButton rdbOFF;
            private Calendar cal;
            private  Calendar CAL;
			private boolean flgOFFASSG = false;    // Flag for checking existance of previous off day
            private JCheckBox chkAUTH;
            //private JCheckBox chkOVERW;
			private int intCHKFL_COL = 0;
			private int intEMPCD_COL = 1;
			private int intSAPNO_COL = 2;
			private int intEMPNM_COL = 3;
			private int intSTSFL_COL = 4;
            private String strTEMP;
            //private String[] arrSUMHDR1;
            private Hashtable<String,String> hstDAYS1;
			private String strCGMTP;		
			private String strCGSTP;		
            private String strCODCD;
            private String strCHP01;
            private String strCHP02;
			private String strYRDGT;
			private Hashtable<String,String[]> hstCDTRN;			// Details of all codes used in program
			private Hashtable<String,String> hstCODDS;			// Code No. from Code Description
			private INPVF oINPVF;
			
			private int intCDTRN_TOT = 10;			
			private int intAE_CMT_CODCD = 0;		
			private int intAE_CMT_CODDS = 1;		
			private int intAE_CMT_SHRDS = 2;		
			private int intAE_CMT_CHP01 = 3;		
			private int intAE_CMT_CHP02 = 4;		
			private int intAE_CMT_NMP01 = 5;		
			private int intAE_CMT_NMP02 = 6;		
			private int intAE_CMT_CCSVL = 7;		
			private int intAE_CMT_NCSVL = 8;
			private int intAE_CMT_MODLS = 9;
			
			protected SimpleDateFormat fmtYYYYMMDD = new SimpleDateFormat("yyyyMMdd"); // format for keyvalue of report column
			protected SimpleDateFormat fmtDD = new SimpleDateFormat("dd");             // format for report column headings
			private SimpleDateFormat fmtDBDATTM = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss"); 
			private SimpleDateFormat fmtDBDATTM_YMD = new SimpleDateFormat("yyyy-MM-dd"); 
			
			private	java.util.Date datWRKDT0;		// Previous day
			private	java.util.Date datWRKDT1;		// Current Working day
			private	java.util.Date datWRKDT2;		// Next Day
			private	java.util.Date datWRKDT3;		// Next Day Ending date
			private	java.util.Date datWRKDT;		// Working date to be considered depending on In/Out time
			boolean flgENTER=false;//flag to set shift for a day only when enter key is pressed.
			boolean flgSICCHK = false;			
			
			hr_tessc()
            {
                super(2);
        		try
        		{
					if(cl_dat.M_strUSRCD_pbst.equals("SKS") || cl_dat.M_strUSRCD_pbst.equals("S_J") || cl_dat.M_strUSRCD_pbst.equals("MMN"))
					   flgALLDPT = true;
					flgSICCHK = exeSICCHK(cl_dat.M_strEMPNO_pbst);
					hstCDTRN = new Hashtable<String,String[]>();
					hstCODDS = new Hashtable<String,String>();
					hstCDTRN.clear();
					hstCODDS.clear();
					strYRDGT = cl_dat.M_strLOGDT_pbst.substring(9,10);
					//System.out.println("YRDGT : "+strYRDGT);
					crtCDTRN("'SYSHR"+cl_dat.M_strCMPCD_pbst+"PH"+strYRDGT+"','SYSHR"+cl_dat.M_strCMPCD_pbst+"RH"+strYRDGT+"'","",hstCDTRN);
                     setMatrix(20,20);
        		     btnGRP1 = new ButtonGroup();
        		     btnGRP = new ButtonGroup();
        		     add(new JLabel("Department") ,2,1,1,2.5,this,'L');
        		     add(txtDPTCD=new TxtLimit(3),2,3,1,1, this,'L');
        		     add(lblDPTCD=new JLabel(""),3,3,1,2, this,'L');
        		    
        		     add(new JLabel("Employee Category"), 2,6,1,3 ,this,'L');
        		     add(txtEMPCT =new TxtLimit(3),2,9,1,0.5 ,this,'L');
        		     add(lblEMPCT =new JLabel(""),3,9,1,2 ,this,'L');
        		    
        		    
        		     add( new JLabel ("Period From"),2,13,1,2.2,this,'L');
        		     add(txtFMDT=new TxtDate(),2,15,1,2,this,'L');
        		    
        		     add(new JLabel("To") ,2,17,1,1.2,this,'L');
        		     add(txtTODT=new TxtDate(),2,18,1,2,this,'L');
        		    
        		     add(new JLabel("Pattern ") ,4,1,1,2.5,this,'L');
        		     add(txtSHFPT=new TxtLimit(6),4,3,1,2.5,this,'L');        		    
        		     add(new JLabel("Shift Pattern Application"),3,6,1,2,this,'L');
        		     add(rdbON =new JRadioButton("On",false ),4,6,1,2,this,'L');
        		     add(rdbOFF =new JRadioButton("Off",false ),4,8,1,2,this,'L');
        		   
        		     btnGRP.add(rdbON);
        		     btnGRP.add(rdbOFF);
        		     add(btnDISP=new JButton("Display") ,4,10,1,2.5,this,'L');
        		    
        		     add(chkAUTH=new JCheckBox("Authorise", false),4,13,1,3.5,this,'L');
        		     //add(chkOVERW=new JCheckBox("Overwrite",false) ,4,17,1,2.5,this,'L');
        		  
        		     oINPVF=new INPVF();
					 
					 hstDAYS=new Hashtable<String,String>(); 
        		     hstDAYS1=new Hashtable<String,String>();
        		     hstDATECOL=new Hashtable<String,String>(); 
        		     tabMAIN=new JTabbedPane();
        		     cal = new GregorianCalendar(); 
					 
					if(!setEMPNO_LOGIN(cl_dat.M_strUSRCD_pbst))
						JOptionPane.showMessageDialog(null, "Base details for "+cl_dat.M_strUSRCD_pbst+"  Login are not available" , "Error Message", JOptionPane.ERROR_MESSAGE); 
        		     txtDPTCD.setEnabled(false);
             		 txtEMPCT.setEnabled(false);
             		 txtFMDT.setEnabled(false);
             		 txtTODT.setEnabled(false);
             		 txtSHFPT.setEnabled(false);
             	
             		 btnDISP.setEnabled(false);
             		 rdbON.setEnabled(false);
             		 rdbOFF.setEnabled(false);
					 
             		 //chkOVERW.setEnabled( false);
             		 chkAUTH.setEnabled(true);
             		 chkAUTH.setSelected(true);
        		     txtDPTCD.setInputVerifier(oINPVF);			
        		}catch(Exception L_EX)
        		{
        		    setMSG(L_EX,"This is constructor");
        		}
      
            }
            
            
			/**
			 */
            void setENBL(boolean L_flgSTAT)
        	{
        		super.setENBL(L_flgSTAT);
           		txtDPTCD.setEnabled(false);
        		txtEMPCT.setEnabled(false);
        		txtFMDT.setEnabled(false);
        		txtTODT.setEnabled(false);
        		txtSHFPT.setEnabled(false);
        		
        		btnDISP.setEnabled(false);
        		rdbON.setEnabled(false);
        		rdbOFF.setEnabled(false);
        		//chkOVERW.setEnabled( false);
         		chkAUTH.setEnabled(false);
         		//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem()>0)	
        		
        		if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
   			   {
        		    txtDPTCD.setEnabled(true);
        		    txtDPTCD.requestFocus() ;
   			   }
	        	if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst ))  )
	    		  {
	         		    txtDPTCD.setEnabled(true);
	         		    txtDPTCD.requestFocus() ;
	         		   tblSHFPT.setEnabled(false);
	         	   }
	        	if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))  )
	  		  	{
	       		    txtDPTCD.setEnabled(true);
	       		    txtDPTCD.requestFocus() ;
	       		    
	       	    }
   		     
        		
        	}
			
			
			
			/**
			 */
            public void actionPerformed(ActionEvent L_AE)
        	{
        	     super.actionPerformed(L_AE);
        		 try
        		 {
        		      
        		     if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))  || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst )))
     				{
							String L_strXXDAT = cl_dat.M_txtCLKDT_pbst.getText();
							if(txtFMDT.getText().length()<8)
								txtFMDT.setText("01/"+L_strXXDAT.substring(3));
							M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strXXDAT));      
							M_calLOCAL.add(Calendar.MONTH,1);    
							L_strXXDAT = M_fmtLCDAT.format(M_calLOCAL.getTime());
							M_calLOCAL.setTime(M_fmtLCDAT.parse("01/"+L_strXXDAT.substring(3)));      
							M_calLOCAL.add(Calendar.DATE,-1);    
							L_strXXDAT = M_fmtLCDAT.format(M_calLOCAL.getTime());
							if(txtTODT.getText().length()<8)
								txtTODT.setText(L_strXXDAT);
						 	 //txtFMDT.setText("01/12/2007");
							 //txtTODT.setText("31/12/2007");

		        		         if(M_objSOURC==txtDPTCD)
		        		            {
		 		        		             if(vldDPTCD())
				        		             {   
					        		             if(txtDPTCD.getText() .length() >0 || flgALLDPT)
											       {
						        		             txtEMPCT.setEnabled(true);
						        		             txtEMPCT.requestFocus() ;
											       }
					        		             else
					        		             {
					        		                 setMSG("Please Enter Department Code",'E');
					        		                 txtDPTCD.requestFocus() ;
					        		              }
				        		             }
				        		             else
				        		             {
				        		                 setMSG("Please Press F1 .....",'E');
				        		                 txtDPTCD.requestFocus() ;
				        		             }
			        		     }
		        		         if(M_objSOURC==txtEMPCT)
		        		         {
		        		             if(vldEMPCT())
		        		             {   
			        		             //if(txtEMPCT.getText() .length() >0)
									       //{
			        		                 setMSG("Valid Employee Category",'N');
			        		                 txtFMDT.setEnabled(true);
				        		             txtFMDT.requestFocus() ;
									       //}
		        		             }
		        		             else
		        		             {
		        		                 setMSG("Please Press F1 .....",'E');
		        		                 txtEMPCT.requestFocus() ;
		        		             }
		        		             
		        		         }
		        		         if(M_objSOURC==txtFMDT)
		        		         {
		        		             if(txtFMDT.getText() .length() >0)
								       {
		        		                 txtTODT.setEnabled(true);
		        		                 txtTODT.requestFocus() ;
								       }
		        		             else
		        		             {
		        		                 setMSG("Please Enter From Date" ,'E');
		        		                 txtFMDT.requestFocus();
		        		             }
		        		             
		        		         }
		        		         if(M_objSOURC==txtTODT)
		        		         {
		        		             if(txtTODT.getText().trim().length() == 0)
		        		 			{
		        		 				setMSG("Enter To Date..",'E');
		        		 				txtTODT.requestFocus();
		        		 			}
		        		             if(M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODT.getText().trim())).compareTo(M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDT.getText().trim())))>0)
		        		 			 {
			        		                 txtSHFPT.setEnabled(true);
			        		                 txtSHFPT.requestFocus() ;
			        		             
			        		                 rdbOFF.setEnabled(true);
			        		                
		        		 			 }
		        		             else
		        		             {
		        		                 setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
		        		                 txtTODT.requestFocus() ;
		        		             }
		        		                 
		        		             
		        		         }
		        		         if(M_objSOURC==txtSHFPT)
		        		         {
		        		             if(vldSHFPT())
								       {
		        		                 btnDISP.requestFocus() ;
		        		                 rdbON.setEnabled(true);
		        		                 rdbON.requestFocus() ;
								       }
		        		             else
		        		             {
		        		                 setMSG("Please Press F1 Shift Pattern " ,'E');
		        		                 txtSHFPT.requestFocus();
		        		             }
		        		             
		        		         }
		        		         if(M_objSOURC==btnDISP)
		        		         {
		        		            
		        		                 //chkOVERW.setEnabled( true);
		        		             	 chkAUTH.setEnabled(true);
             							 chkAUTH.setSelected(true);
		        		                 tabMAIN.remove(pnlTBL); 
			        		             exeTBLREC();
		        		         }
		        		            
		        		          
		        		         
		        		         if(M_objSOURC==rdbON)
		        		         {
		        		             btnDISP.setEnabled(true);
		        		             btnDISP.requestFocus() ;
		        		         }
		        		         if(rdbOFF.isSelected() )
		        		         {
		        	
		        		             btnDISP.setEnabled(true);
		        		             btnDISP.requestFocus() ;
		        		         }
		        		       
		        			 }
        		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst  )) 
      				{
 		        		         if(M_objSOURC==txtDPTCD)
 		        		            {
 		 		        		             if(vldDPTCD())
 				        		             {   
												 
 					        		             if(txtDPTCD.getText() .length() >0 || flgALLDPT)
 											       {
 						        		             txtEMPCT.setEnabled(true);
 						        		             txtEMPCT.requestFocus() ;
 											       }
 					        		             else
 					        		             {
 					        		                 setMSG("Please Enter Department Code",'E');
 					        		                 txtDPTCD.requestFocus() ;
 					        		              }
 				        		             }
 				        		             else
 				        		             {
 				        		                 setMSG("Please Press F1 .....",'E');
 				        		                 txtDPTCD.requestFocus() ;
 				        		             }
 			        		     }
 		        		         if(M_objSOURC==txtEMPCT)
 		        		         {
 		        		             if(vldEMPCT())
 		        		             {   
 			        		             if(txtEMPCT.getText() .length() >0)
 									       {
 			        		                 setMSG("Valid Employee Category",'N');
 			        		                 txtFMDT.setEnabled(true);
 				        		             txtFMDT.requestFocus() ;
 									       }
 			        		             else
 			        		             {
 			        		                 setMSG("Please Enter Employee Category",'E');
 			        		                 txtEMPCT.requestFocus() ;
 			        		              }
 		        		             }
 		        		             else
 		        		             {
 		        		                 setMSG("Please Press F1 .....",'E');
 		        		                 txtEMPCT.requestFocus() ;
 		        		             }
 		        		             
 		        		         }
 		        		         if(M_objSOURC==txtFMDT)
 		        		         {
 		        		             if(txtFMDT.getText() .length() >0)
 								       {
 		        		                 txtTODT.setEnabled(true);
 		        		                 txtTODT.requestFocus() ;
 								       }
 		        		             else
 		        		             {
 		        		                 setMSG("Please Enter From Date" ,'E');
 		        		                 txtFMDT.requestFocus();
 		        		             }
 		        		             
 		        		         }
 		        		         if(M_objSOURC==txtTODT)
 		        		         {
 		        		             if(txtTODT.getText().trim().length() == 0)
 		        		 			{
 		        		 				setMSG("Enter To Date..",'E');
 		        		 				txtTODT.requestFocus();
 		        		 			}
 		        		             if(M_fmtLCDAT.parse(txtTODT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDT.getText().trim()))>0)
 		        		 			 {
 			        		                 
 			        		             
 			        		                 btnDISP.setEnabled( true);
 			        		                
 		        		 			 }
 		        		             else
 		        		             {
 		        		                 setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
 		        		                 txtTODT.requestFocus() ;
 		        		             }
 		        		                 
 		        		             
 		        		         }
 		        		        if(M_objSOURC==btnDISP)
		        		         {
		        		            
		        		                 //chkOVERW.setEnabled( true);
		        		             	 chkAUTH.setEnabled(true);
             							 chkAUTH.setSelected(true);
		        		                 tabMAIN.remove(pnlTBL); 
			        		             exeTBLREC();
		        		         }
 		        		        
 		        		        
 		        		        
 		        		        if(M_objSOURC==tblSHFPT)
 		        		        {
 		        		           
 		        		        }
 		        		   
 		        	 }
         		
        		     if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst  )) 
       				{
  		        		         if(M_objSOURC==txtDPTCD)
  		        		            {
  		 		        		             if(vldDPTCD())
  				        		             {   
  					        		             if(txtDPTCD.getText() .length() >0 || flgALLDPT)
  											       {
  						        		             txtEMPCT.setEnabled(true);
  						        		             txtEMPCT.requestFocus() ;
  											       }
  					        		             else
  					        		             {
  					        		                 setMSG("Please Enter Department Code",'E');
  					        		                 txtDPTCD.requestFocus() ;
  					        		              }
  				        		             }
  				        		             else
  				        		             {
  				        		                 setMSG("Please Press F1 .....",'E');
  				        		                 txtDPTCD.requestFocus() ;
  				        		             }
  			        		     }
  		        		         if(M_objSOURC==txtEMPCT)
  		        		         {
  		        		             if(vldEMPCT())
  		        		             {   
  			        		             if(txtEMPCT.getText() .length() >0)
  									       {
  			        		                 setMSG("Valid Employee Category",'N');
  			        		                 txtFMDT.setEnabled(true);
  				        		             txtFMDT.requestFocus() ;
  									       }
  			        		             else
  			        		             {
  			        		                 setMSG("Please Enter Employee Category",'E');
  			        		                 txtEMPCT.requestFocus() ;
  			        		              }
  		        		             }
  		        		             else
  		        		             {
  		        		                 setMSG("Please Press F1 .....",'E');
  		        		                 txtEMPCT.requestFocus() ;
  		        		             }
  		        		             
  		        		         }
  		        		         if(M_objSOURC==txtFMDT)
  		        		         {
  		        		             if(txtFMDT.getText() .length() >0)
  								       {
  		        		                 txtTODT.setEnabled(true);
  		        		                 txtTODT.requestFocus() ;
  								       }
  		        		             else
  		        		             {
  		        		                 setMSG("Please Enter From Date" ,'E');
  		        		                 txtFMDT.requestFocus();
  		        		             }
  		        		             
  		        		         }
  		        		         if(M_objSOURC==txtTODT)
  		        		         {
  		        		             if(txtTODT.getText().trim().length() == 0)
  		        		 			{
  		        		 				setMSG("Enter To Date..",'E');
  		        		 				txtTODT.requestFocus();
  		        		 			}
  		        		             if(txtTODT.getText().trim().compareTo(txtFMDT.getText().trim())>0)
  		        		 			 {
  			        		                 
			        		                 
  			        		                 btnDISP.setEnabled( true);
  			        		                 btnDISP.requestFocus() ;
  			        		                
  		        		 			 }
  		        		             else
  		        		             {
  		        		                 setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
  		        		                 txtTODT.requestFocus() ;
  		        		             }
  		        		                 
  		        		             
  		        		         }
  		        		        if(M_objSOURC==btnDISP)
 		        		         {
 		        		            
 		        		                 //chkOVERW.setEnabled( true);
 		        		             	 chkAUTH.setEnabled(true);
             							 chkAUTH.setSelected(true);
 		        		                 tabMAIN.remove(pnlTBL); 
 			        		             exeTBLREC();
 		        		         }
       				}
        		 }catch(Exception L_EX)
        		 {
        		     setMSG(L_EX,"This is action Performed");
        		 }
        	} 
            
			
			
            
            /**
             *   // TODO Auto-generated method stub
             */
            private void exeTBLREC() 
            {
              try
              {
                  hstDAYS.clear() ;
                  hstDATECOL.clear() ;
                  strTODT= txtTODT.getText() .toString() .trim().substring(0,2) ;
                  strFMDT=txtFMDT.getText() .toString() .trim().substring(0,2);
                  String L_strRUNDT = txtFMDT.getText();
					while(true)
					{
						//	System.out.println(L_strRUNDT.substring(6,10)+L_strRUNDT.substring(3,5)+L_strRUNDT.substring(0,2)+" / "+L_strRUNDT.substring(0,2));
						hstDAYS.put(L_strRUNDT.substring(6,10)+L_strRUNDT.substring(3,5)+L_strRUNDT.substring(0,2),M_fmtLCDAT.parse( L_strRUNDT).toString() .substring(0,2)+"\n"+L_strRUNDT.substring(0,2));
						if(L_strRUNDT.equals(txtTODT.getText()))
							break;
						M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strRUNDT));       // Convert Into Local Date Format
						M_calLOCAL.add(Calendar.DATE,+1);                     // Increase Date from +1 with Locked Date
						L_strRUNDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
						M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strRUNDT));       // Convert Into Local Date Format
					}
					setHST_ARR(hstDAYS);
					
                  arrSUMHDR = new String[arrHSTKEY.length+intSUMFIX];
                  
                  arrDATEHDR      = new String[arrHSTKEY.length+intSUMFIX];
                  arrDATECOL_TXT  = new JTextField[arrHSTKEY.length+intSUMFIX];
                  arrSUMHDR[0]    = ""; arrSUMHDR[1] = "Emp.No";arrSUMHDR[2] = "SAP.No";arrSUMHDR[3] = "Employee  Name" ;arrSUMHDR[4] = "Status" ;
                  arrDATEHDR[0]   = ""; /*arrDATEHDR[1] = "Emp.No";arrDATEHDR[2] = "Employee Name" ;arrDATEHDR[3] = "Status" ;*/
            	  arrSUMHDR_WD    = new int[arrHSTKEY.length+intSUMFIX];
            	  arrSUMHDR_WD[0] = 20;arrSUMHDR_WD[1] = 20;arrSUMHDR_WD[2] = 40;arrSUMHDR_WD[3] =150; arrSUMHDR_WD[4] =27 ;
	               for( int i=intSUMFIX; i<arrHSTKEY.length+intSUMFIX; i++)
	           	   {
		                arrSUMHDR[i] = hstDAYS.get(arrHSTKEY[i-intSUMFIX].toString()).toString()+"\n";
		                
		                arrDATEHDR[i] = arrHSTKEY[i-intSUMFIX].toString();
		               	hstDATECOL.put(arrHSTKEY[i-intSUMFIX].toString(),String.valueOf(i));
	                    arrSUMHDR_WD[i] = 37;
	           	   }  
                  pnlTBL=new JPanel(null);
       		      tblSHFPT= crtTBLPNL1(pnlTBL,arrSUMHDR ,5000,1,1,11,19,arrSUMHDR_WD,new int[]{0});
       		      tabMAIN.addTab("Monthly ",pnlTBL);
       		  
       		    
       		      tblSHFPT.setInputVerifier(new TBLINPVF());
       		        
       		         tblSHFPT.addKeyListener(this);
       		         tblSHFPT.addMouseListener( this);
       		     
       		       add(tabMAIN,5,1,13.5,19.5,this,'L');
       		      updateUI() ;
       		    //ifnull(ss_shfcd,' ')
				strEMPCT_CHK = (txtEMPCT.getText().trim().length() >0) ? " and ep_empct = '"+txtEMPCT.getText() .toString().trim() +"'" : "";
                M_strSQLQRY = "select ep_empct,ep_empno,ep_sapno,rtrim(ltrim(ep_fstnm)) + ' ' + left(ISNULL(ep_mdlnm,' '),1) + '.' + rtrim(ltrim(ISNULL(ep_lstnm,' ')))  EP_EMPNM,ss_wrkdt,ISNULL(ss_orgsh,'') ss_orgsh,ISNULL(ss_cursh,'') ss_cursh,ISNULL(ss_stsfl,' ') ss_stsfl   from ";
                M_strSQLQRY += " HR_EPMST left outer join hr_sstrn on ss_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ss_cmpcd = ep_cmpcd and ss_empno=ep_empno  ";
                M_strSQLQRY += " and ss_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDT.getText() .toString().trim()))+"'  and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODT.getText() .toString().trim()))+"' where  ";
				M_strSQLQRY += " EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ISNULL(ep_stsfl,'X') <> 'U' and ep_jondt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODT.getText() .toString().trim()))+"' and ((ep_lftdt is null) or ep_lftdt < '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDT.getText() .toString().trim()))+"')"+strEMPCT_CHK+ (txtDPTCD.getText().length()>0 ? " and EP_DPTCD = '"+ txtDPTCD.getText() .toString().trim() +"'" : "")+"  order by ep_sapno,ss_wrkdt ";
                M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
                //System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
                int i=0;
                if(!M_rstRSSET.next() || M_rstRSSET==null)
   					return;
                boolean L_flgEOF = false;
                //strSHFCD=M_rstRSSET.getString("ss_shfcd");
                while(true)
                {
					if(L_flgEOF)
						break;
					strEMPNO=M_rstRSSET.getString("ep_empno");
					strSAPNO=M_rstRSSET.getString("ep_sapno");
					strFULNM=M_rstRSSET.getString("EP_EMPNM");
					strSTSFL=M_rstRSSET.getString("SS_STSFL");
					  
					String L_strSTSFL_MSG = "";
 		            if(strSTSFL.trim().equals(""))
						L_strSTSFL_MSG = "New Entry";
 		            else if(strSTSFL.equals("0"))
						L_strSTSFL_MSG = "Original Schedule";
 		            else if(strSTSFL.equals("1"))
						L_strSTSFL_MSG = "For Modification";
					tblSHFPT.setValueAt(strEMPNO,i,intEMPCD_COL);
					tblSHFPT.setValueAt(strSAPNO,i,intSAPNO_COL);
					tblSHFPT.setValueAt(strFULNM,i,intEMPNM_COL);
					tblSHFPT.setValueAt(L_strSTSFL_MSG,i,intSTSFL_COL);
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst )))
							    tblSHFPT.cmpEDITR[0].setEnabled(true);
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst )))
					{   
					    tblSHFPT.cmpEDITR[0].setEnabled(false);
					    tblSHFPT.cmpEDITR[3].setEnabled(false);
					}
					tblSHFPT.cmpEDITR[1].setEnabled(false);
					tblSHFPT.cmpEDITR[2].setEnabled(false);
					tblSHFPT.cmpEDITR[3].setEnabled(false);
					int L_intSHFCOL = 0;
					
												

                    while(strEMPNO.equals(M_rstRSSET.getString("ep_empno")) && !L_flgEOF)
					{ 
						if(M_rstRSSET.getString("ss_wrkdt") == null)
						{
							if(!M_rstRSSET.next())
								{L_flgEOF = true; break;}
							continue;
						}
						String L_strWRKDT = M_fmtLCDAT.format(M_rstRSSET.getDate("ss_wrkdt"));
						String L_strYYYYMMDD = L_strWRKDT.substring(6,10)+L_strWRKDT.substring(3,5)+L_strWRKDT.substring(0,2);
						L_intSHFCOL = Integer.parseInt(hstDATECOL.get(L_strYYYYMMDD).toString());
				
						//String L_strSHFCD = M_rstRSSET.getString("SS_STSFL").equals("1") ? "SS_CURSH" : "SS_ORGSH";
						String L_strSHFCD =  "SS_CURSH";
						tblSHFPT.setValueAt((M_rstRSSET.getString(L_strSHFCD).equals("X") ? "" : M_rstRSSET.getString(L_strSHFCD)),i,L_intSHFCOL);
						   tblSHFPT.cmpEDITR[L_intSHFCOL].setEnabled(true);
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst )) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst )))
							{   
							    
							    tblSHFPT.cmpEDITR[L_intSHFCOL].setEnabled(false);
							}
						if(!M_rstRSSET.next())
							{L_flgEOF = true; break;}
					}
					i++;
                }
              }catch(Exception L_EX)
              {
                  setMSG(L_EX,"This exeTBLREC");
              }
            }
			
			
			
			/**
			 */
            public void keyPressed(KeyEvent L_KE)
        	{
        		try
        		{
        			super.keyPressed(L_KE);
        			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
        			{
						flgENTER = true;//flag to set shift for a day only when enter key is pressed.
        			}
        			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        			{
        			    if(M_objSOURC==txtDPTCD)
        			    {
        			        cl_dat.M_flgHELPFL_pbst = true;
        			        M_strHLPFLD = "txtDPTCD";
        					String L_ARRHDR[] = {"Code","Department"};
        			        //M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
        					//M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' order by cmt_codcd";
							//M_strSQLQRY = " select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and cmt_codcd in ";
							//M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=US_EMPCD)";
        					M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
        					
				
							//M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  "+(flgALLDPT ? "" : " and  cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO in (select substr(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN')) )");
        					//if(flgALLDPT)
							//	M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "; 
							//else
								String L_strEMPCHK = flgSICCHK ? " and EP_EMPNO = '"+strEMPNO_LOGIN+"'" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN') and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"')";
								M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  "+(flgALLDPT ? "" : " and  cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+L_strEMPCHK+" )");

							System.out.println(M_strSQLQRY);
        					
							cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        			    }
        			    
        			    if(M_objSOURC==txtEMPCT)
        			    {
        			        cl_dat.M_flgHELPFL_pbst = true;
        			        M_strHLPFLD = "txtEMPCT";
        					String L_ARRHDR[] = {"Code","Category"};
        			        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
							M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXEPC' and rtrim(ltrim(cmt_codcd)) in (Select distinct ep_empct from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+ (flgALLDPT ? "" : " and EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"'")+") order by cmt_codcd";
        			        //M_strSQLQRY = "Select distinct ep_empct from HR_EPMST where substr(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and EP_DPTCD= '" + txtDPTCD.getText() .toString().trim() +"'";
        					
        					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
        			    }
        			    if(M_objSOURC==txtSHFPT)
        			    {
        			        cl_dat.M_flgHELPFL_pbst = true;
        			        M_strHLPFLD = "txtSHFPT";
        					String L_ARRHDR[] = {"Shift Code","Shift Description"};
        			        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
        					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXSPT'";
        					cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
        			    }
        	
        			}
        		}catch(Exception L_EX)
        		{
        		    setMSG(L_EX,"This is KeyPressed");
        		}
        	}	
    
    
	/**
	 */		
	void exeHLPOK()
    {
        super.exeHLPOK();
	    if(M_strHLPFLD.equals("txtDPTCD"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtDPTCD.setText(L_STRTKN.nextToken());
	        lblDPTCD.setText(L_STRTKN.nextToken());
		}
	    if(M_strHLPFLD.equals("txtEMPCT"))
		{
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			txtEMPCT.setText(L_STRTKN.nextToken());
	        lblEMPCT.setText(L_STRTKN.nextToken());
		}
	    if(M_strHLPFLD.equals("txtSHFPT"))
		{
	        txtSHFPT.setText(cl_dat.M_strHLPSTR_pbst);
		}
	   // if(M_strHLPFLD.equals("txtSHFO"))
		//{
	    //  txtSHFO.setText(cl_dat.M_strHLPSTR_pbst.toUpperCase());
	//	}
    }
  
	
	
    /**
     */
    public void exeSAVE()
	{
	    try
	    {
	       
		        if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))  || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))) 
				{
		            if(!saveHRSSTRN())
						return;
		            tblSHFPT.clrTABLE() ;
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
						   setMSG(" Data Saved Successfully..",'N');
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))
							   setMSG(" Data Deleation Successfully..",'N');
					}
					else
					{   
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
						setMSG("Error in saving details..",'E'); 
					
					}
				}
		}catch(Exception L_EX)
		{
	           setMSG(L_EX,"this is exe save");
		}
	   
	} 
	
    
	/**
	 */
    private boolean saveHRSSTRN()
    {
        	try
        	{
				inlTBLEDIT(tblSHFPT);
				ResultSet L_rstRSSET = null;
				ResultSet L_rstRSSET1 = null;
				String L_strSTSFL = "";
				String L_strSTSFL_OLD = "";
				String L_strLVECD = "";
				String L_strLVECD_OLD = "";
				String L_strSQLQRY;
				
        	    for(int i=0;i<tblSHFPT.getRowCount();i++)
				{
				    //if(tblSHFPT.getValueAt(i,intEMPCD_COL).toString().trim().length() >1)
					if(!tblSHFPT.getValueAt(i,intCHKFL_COL).toString().equals("true"))
						continue;
					String L_strEMPNM = tblSHFPT.getValueAt(i,intEMPNM_COL).toString();
					String L_strMSGVL = "";
					String L_strDTLVL = "";
					String L_strDTLVL_1 = "";
					String L_strDTLVL_2 = "";
					String L_strDTLVL_2S = arrHSTKEY[0].toString();  // Starting date for Off validation
					for(int ii=intSUMFIX;ii<tblSHFPT.getColumnCount();ii++)
					{
						if(ii>intSUMFIX)
						{
							L_strDTLVL_1 = tblSHFPT.getValueAt(i,ii-1).toString();
							L_strDTLVL_2 = tblSHFPT.getValueAt(i,ii-1).toString().equalsIgnoreCase("O") ? arrHSTKEY[ii-intSUMFIX-1].toString() : L_strDTLVL_2;
						}
						//if(tblSHFPT.getValueAt(i,ii).toString().trim().length() == 0)
						//	{setMSG("Data not saved .... Please define Schedule for All Employees",'E'); return false;}
							
						L_strDTLVL =  tblSHFPT.getValueAt(i,ii).toString(); 
						String L_strMSGVL1 = chkLEGDV("1",L_strDTLVL_1, L_strDTLVL,ii-intSUMFIX);
						flgOFFASSG = L_strDTLVL_2.length()==0 ? false : true;
							
						L_strMSGVL1 += chkLEGDV("2",(flgOFFASSG ? L_strDTLVL_2 : L_strDTLVL_2S), L_strDTLVL,ii-intSUMFIX);
						L_strMSGVL += L_strMSGVL1.length()>0 ? "Date : ("+String.valueOf(ii-intSUMFIX)+")"+L_strMSGVL1+"  ---   " : "";
					}
					//System.out.println(L_strEMPNM+L_strMSGVL);
					if(L_strMSGVL.length()>1)
						{JOptionPane.showMessageDialog(null, L_strEMPNM+ "    "+ L_strMSGVL+" \n  Please Note", "Warning", JOptionPane.ERROR_MESSAGE);}
				}
				
				
        	    for(int i=0;i<tblSHFPT.getRowCount();i++)
				{
				    //if(tblSHFPT.getValueAt(i,intCHKFL_COL).toString().trim().equals("false") && !cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					//	continue;
					
						if(tblSHFPT.getValueAt(i,intCHKFL_COL).toString().equals("false"))
							continue;	
						if(tblSHFPT.getValueAt(i,intEMPCD_COL).toString().trim().length()<1)
							break;
						for(int j=intSUMFIX;j<tblSHFPT.getColumnCount();j++)
						{
						    String L_strYYYYMMDD = arrDATEHDR[j].toString();
							String L_strWRKDT = L_strYYYYMMDD.substring(6,8)+"/"+L_strYYYYMMDD.substring(4,6)+"/"+L_strYYYYMMDD.substring(0,4);
							
							
							L_strSQLQRY = "select ISNULL(ss_stsfl,'X') ss_stsfl, ISNULL(ss_lvecd,'') ss_lvecd from HR_SSTRN where SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and ss_EMPNO = '"+tblSHFPT.getValueAt(i,intEMPCD_COL)+"' and ss_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'";
							L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
							L_strSTSFL_OLD = "";
							L_strLVECD_OLD = "";
							L_strLVECD = "";
							if(L_rstRSSET != null && L_rstRSSET.next())
								{L_strSTSFL_OLD = L_rstRSSET.getString("ss_STSFL"); L_strLVECD_OLD = L_rstRSSET.getString("ss_LVECD");}
							
							String L_strSTRTM = "00:00";
							String L_strENDTM = "00:00";
							String L_strSHFCD = tblSHFPT.getValueAt(i,j).toString().trim().length() >0 ? tblSHFPT.getValueAt(i,j).toString().toUpperCase() : "X";
							if(cl_dat.M_hstMKTCD_pbst.containsKey("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHFCD))
							{
								L_strSTRTM = getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHFCD,cl_dat.M_intCHP01_pbst);
								L_strENDTM = getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+L_strSHFCD,cl_dat.M_intCHP02_pbst);
							}

							strWHRSTR_SS = " SS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ss_EMPNO = '"+tblSHFPT.getValueAt(i,intEMPCD_COL)+"' and ss_WRKDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"'";
							String L_strSTRDTTM = L_strYYYYMMDD.substring(0,4)+"-"+L_strYYYYMMDD.substring(4,6)+"-"+L_strYYYYMMDD.substring(6,8)+"-"+L_strSTRTM.substring(0,2)+"."+L_strSTRTM.substring(3,5)+".00";
							String L_strENDDTTM = L_strYYYYMMDD.substring(0,4)+"-"+L_strYYYYMMDD.substring(4,6)+"-"+L_strYYYYMMDD.substring(6,8)+"-"+L_strENDTM.substring(0,2)+"."+L_strENDTM.substring(3,5)+".00";
							//System.out.println(L_strSTRDTTM+" / "+L_strENDDTTM);
							String L_strSTSFL_INS = (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) || chkAUTH.isSelected()) ? "1" : "0";
							if(hstCDTRN.containsKey("SYSHR"+cl_dat.M_strCMPCD_pbst+"RH"+strYRDGT+""+L_strYYYYMMDD))
							   L_strLVECD = L_strLVECD.length()==0 ? "RH" : "";
							if(hstCDTRN.containsKey("SYSHR"+cl_dat.M_strCMPCD_pbst+"PH"+strYRDGT+""+L_strYYYYMMDD))
							   L_strLVECD = L_strLVECD.length()==0 ? "PH" : "";
							if(L_strSHFCD.equalsIgnoreCase("O"))
							   L_strLVECD = "WO";
							M_strSQLQRY ="INSERT INTO HR_SSTRN(SS_CMPCD,ss_sbscd,ss_EMPNO,ss_WRKDT,ss_LUSBY,ss_LUPDT,ss_ORGSH, ss_CURSH, ss_LVECD, ss_STSFL,ss_trnfl)";
							M_strSQLQRY +="Values(";
							M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY += "'"+M_strSBSCD+"',";
							M_strSQLQRY += "'"+tblSHFPT.getValueAt(i,intEMPCD_COL)+"',";
							M_strSQLQRY += "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strWRKDT))+"',";
							M_strSQLQRY += " '"+cl_dat.M_strUSRCD_pbst+"',";
							M_strSQLQRY += " '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"',";
						    M_strSQLQRY += "'" +L_strSHFCD +"',";
						    M_strSQLQRY += "'" +L_strSHFCD +"',";
						    M_strSQLQRY += "'" +L_strLVECD +"',";
						    M_strSQLQRY += "'"+L_strSTSFL_INS+"',";
						    M_strSQLQRY += "'0')";
							//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
							//System.out.println("EMPNO : "+tblSHFPT.getValueAt(i,intEMPCD_COL)+"   Day : "+(j-intSUMFIX)+"    L_strSHFCD : "+L_strSHFCD+"   L_strSTSFL : "+L_strSTSFL);
							if(!L_strSTSFL_OLD.equals(""))
							{
								//String L_strORGSH_UPD = L_strSTSFL.equals("0") ? "ss_ORGSH = '"+L_strSHFCD+"'," : "";
								String L_strORGSH_UPD =  "";
								L_strSTSFL = (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) || chkAUTH.isSelected()) ? "1" : "0";
								String L_strLVECD_UPD  = (L_strLVECD_OLD.equals("WO") || L_strLVECD_OLD.equals("")) ? " SS_LVECD = '"+L_strLVECD+"'," : "";
									M_strSQLQRY  = "update HR_SSTRN set ss_cursh = '"+L_strSHFCD+"',"+L_strORGSH_UPD+" ss_stsfl = '"+L_strSTSFL+"',";
									M_strSQLQRY  += L_strLVECD_UPD;
									M_strSQLQRY  += " ss_lusby='"+cl_dat.M_strUSRCD_pbst +"' ,ss_lupdt='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) +"' where "+strWHRSTR_SS;
							}
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							//System.out.println(M_strSQLQRY);
							cl_dat.M_flgLCUPD_pbst =true;
								 
						}
						
				}
			
			
        	}catch(Exception L_EX)
        	{
        	    setMSG(L_EX,"saveHRSSTRN");
        	}
			return true;
        
    }
	
	
	/**
	 */
    public void cellChanged(ChangeEvent L_CE)
	 {
        
	         
	 }
	
    
    /**
     */
    private boolean vldDPTCD()
	{
	    try{
			//M_strSQLQRY = "Select CMT_CODCD,EP_DPTCD from CO_CDTRN,HR_EPMST  WHERE  substr(EP_HRSBS,1,2) = '"+M_strSBSCD.substring(0,2)+"' and CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'  and EP_DPTCD='"+txtDPTCD.getText().toString().trim()+"'";
			
			//M_strSQLQRY = " select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='COXXDPT' and CMT_CODCD= '"+txtDPTCD.getText().toString().trim()+"' and cmt_codcd in ";
			//M_strSQLQRY +=" (select  EP_DPTCD from HR_EPMST,SA_USMST where US_USRCD='"+cl_dat.M_strUSRCD_pbst+"' and EP_EMPNO=US_EMPCD)";

			String L_strEMPCHK = flgSICCHK ? " and EP_EMPNO = '"+strEMPNO_LOGIN+"'" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN') and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"')";
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "+(flgALLDPT ? "" : " and  cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+L_strEMPCHK+" and EP_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' )");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldDPTCD ....."+L_EX);							   
		}
	    return false;
	}
	
	/**
	 */
    private boolean vldEMPCT()
	{
	    try{
			if(txtEMPCT.getText() .length()==0)
				return true;
			M_strSQLQRY = "Select distinct ep_empct from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+(flgALLDPT ? "" : " and EP_DPTCD = '"+txtDPTCD.getText() .toString() .trim() +"' and  ep_empct= '"+txtEMPCT.getText() .toString() .trim() +"'");
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldEMPCT ....."+L_EX);							   
		}
	    return false;
	}
	
	/**
	 */
    private boolean vldSHFPT()
	{
	    try{
	        M_strSQLQRY = "Select CMT_CODCD ,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXSPT'  and CMT_CODDS='"+txtSHFPT.getText().toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldSHFPT ....."+L_EX);							   
		}
	    return false;
	}
	
	/**
	 */
    private boolean vldTBLSHFPT()
	{
	    try{
	        
		        M_strSQLQRY = "Select * from CO_CDTRN WHERE CMT_CGMTP = 'M"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'COXXSHF'  and CMT_CODCD='"+((JTextField)tblSHFPT.cmpEDITR[tblSHFPT.getSelectedColumn() ]).getText() +"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				//System.out.println(M_strSQLQRY);
				if(M_rstRSSET.next())
				{
					//System.out.println(" aaaaa");
				    return true;
				}
				if(M_rstRSSET != null)
					M_rstRSSET.close();
	    			
				
		}catch(Exception L_EX){
			System.out.println("L_EX..vldtblSHFPT ....."+L_EX);							   
		}
	    return false;
	}
    
    
	/**
	 */
    private void setHST_ARR(Hashtable LP_HSTNM)
	{
		try
		{
			arrHSTKEY = LP_HSTNM.keySet().toArray();
			Arrays.sort(arrHSTKEY);

		}
		catch(Exception e){setMSG(e,"setHST_ARR");}
	}
 
    
	/**
	 */
    public void mouseClicked(MouseEvent L_ME)
    {
        super.mouseClicked( L_ME);
        try
        {
	        if(L_ME.getSource() .equals(tblSHFPT))
	        {
	            if(rdbON.isSelected()  || rdbOFF.isSelected() )
	 	       {    
	 	    	    /*if(chkOVERW.isSelected() ==false)
	 		        {    
	 	    	       
	 			         if(tblSHFPT.isEditing())
	 					  {
	 			             
	 			  				if(tblSHFPT.getValueAt(tblSHFPT.getSelectedRow(),tblSHFPT.getSelectedColumn()).toString().length()>0)
	 			  				{
	 			  				  
	 			  				    tblSHFPT.getCellEditor().stopCellEditing();
	 				  				tblSHFPT.setRowSelectionInterval(tblSHFPT.getSelectedRow(),tblSHFPT.getSelectedRow());		
	 				  				tblSHFPT.setColumnSelectionInterval(0,0);		
	 				  				tblSHFPT.editCellAt(tblSHFPT.getSelectedRow(),0);
	 			  				}
	 					  }
	 		        }*/
	 	    	  
	 	    	    
	 	       }
	        }
	       
			  
        }catch(Exception L_EX)
        {
            setMSG(L_EX,"Mouse Clicked");
        }
    }
	
	/**
	 */
    private boolean vldSHFCD()
    {
        try
        {
                if(((JTextField)tblSHFPT.cmpEDITR[tblSHFPT.getSelectedColumn() ]).getText().toString() .equals("O"))
	               {
	                return true;   
	               }
		            if(vldTBLSHFPT())
		            {
		                setMSG("Valid Shift Code",'N');
		                return true;
		            }
		            else
		            {
		                setMSG("In Valid Shift Code .... Please Enter Shift Code..A,B,C,D,E,F,G,O....",'E');
		                return false;
		            }
	       
            
        }catch(Exception L_EX)
        {
            setMSG(L_EX,"This is vldSHFCD");
        }
        return false;
    }

	/**
	 */
	public void mousePressed(MouseEvent L_ME)
    {
        super.mousePressed( L_ME);
      try
      {
        if(L_ME.getSource() .equals(tblSHFPT))
        {
            if(rdbON.isSelected()  || rdbOFF.isSelected() )
 	       {    
 	    	    /*if(chkOVERW.isSelected() ==false)
 		        {    
 	    	       
 			         if(tblSHFPT.isEditing())
 					  {
 			             
 			  				if(tblSHFPT.getValueAt(tblSHFPT.getSelectedRow(),tblSHFPT.getSelectedColumn()).toString().length()>0)
 			  				{
 			  				  
 			  				    tblSHFPT.getCellEditor().stopCellEditing();
 				  				tblSHFPT.setRowSelectionInterval(tblSHFPT.getSelectedRow(),tblSHFPT.getSelectedRow());		
 				  				tblSHFPT.setColumnSelectionInterval(0,0);		
 				  				tblSHFPT.editCellAt(tblSHFPT.getSelectedRow(),0);
 			  				}
 					  }
 		        }*/
 	       }
           
        
        }
      
      }catch(Exception L_EX)
      {
          setMSG(L_EX,"Mouse Pressed");
      }
    }
	
	/**
	 */
    public void mouseDragged(MouseEvent L_ME)
    {
      try
      {
        if(L_ME.getSource() .equals(tblSHFPT))
        {
            if(rdbON.isSelected()  || rdbOFF.isSelected() )
 	       {    
 	    	    /*if(chkOVERW.isSelected() ==false)
 		        {    
 	    	       
 			         if(tblSHFPT.isEditing())
 					  {
 			             
 			  				if(tblSHFPT.getValueAt(tblSHFPT.getSelectedRow(),tblSHFPT.getSelectedColumn()).toString().length()>0)
 			  				{
 			  				  
 			  				    tblSHFPT.getCellEditor().stopCellEditing();
 				  				tblSHFPT.setRowSelectionInterval(tblSHFPT.getSelectedRow(),tblSHFPT.getSelectedRow());		
 				  				tblSHFPT.setColumnSelectionInterval(0,0);		
 				  				tblSHFPT.editCellAt(tblSHFPT.getSelectedRow(),0);
 			  				}
 					  }
 		        }*/
 	       }
           
        
        }
      }catch(Exception L_EX)
      {
        setMSG(L_EX,"Mouse Drageed");  
      }
        
    }
    
	/**
	 */
    public void mouseEntered(MouseEvent L_ME)
    {
        super.mouseEntered( L_ME);
       
        
        //System.out.println("mouseEntered");
    }
   
	/**
	 */
    public void mouseExisted(MouseEvent L_ME)
    {
        super.mouseExited( L_ME);
           
    }
	
	/**
	 */
    public void mouseReleased(MouseEvent L_ME)
    {
        super.mouseReleased( L_ME);
       
    }
   
   /**
    
    */
   private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{			
			try
			{
			    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
				{
		           	if(rdbON.isSelected() )
		           	{    
					    if(getSource()==tblSHFPT)
						{
					           if(P_intCOLID >= intSUMFIX && tblSHFPT.getValueAt(tblSHFPT.getSelectedRow(),intEMPCD_COL).toString().trim().length()>3 && flgENTER)
								{
									 tblSHFPT.setValueAt(new Boolean(true),tblSHFPT.getSelectedRow(),intCHKFL_COL);
										int L_intCOLID = P_intCOLID;
										while(true)
										{
										        tblSHFPT.setCellColor( P_intROWID ,L_intCOLID,Color.RED);
												tblSHFPT.removeMouseListener(tblSHFPT);
												setSHFPT(P_intROWID,L_intCOLID,0);
												setSHFPT(P_intROWID,L_intCOLID,1);
												setSHFPT(P_intROWID,L_intCOLID,2);
												setSHFPT(P_intROWID,L_intCOLID,3);
												setSHFPT(P_intROWID,L_intCOLID,4);
												setSHFPT(P_intROWID,L_intCOLID,5);
												setSHFPT(P_intROWID,L_intCOLID,6);
								                
											L_intCOLID = L_intCOLID+7;
											if(L_intCOLID>arrSUMHDR.length)
												break;
										}
					                 
										L_intCOLID = P_intCOLID;
										while(true)
										{
										        tblSHFPT.setCellColor( P_intROWID ,L_intCOLID,Color.RED);
												tblSHFPT.removeMouseListener(tblSHFPT);
												setSHFPT_R(P_intROWID,L_intCOLID,0);
												setSHFPT_R(P_intROWID,L_intCOLID,1);
												setSHFPT_R(P_intROWID,L_intCOLID,2);
												setSHFPT_R(P_intROWID,L_intCOLID,3);
												setSHFPT_R(P_intROWID,L_intCOLID,4);
												setSHFPT_R(P_intROWID,L_intCOLID,5);
												setSHFPT_R(P_intROWID,L_intCOLID,6);
								                
											L_intCOLID = L_intCOLID-7;
											if(L_intCOLID<intSUMFIX)
												break;
										}
										flgENTER=false; //flag to set shift for a day only when enter key is pressed.
							   }
					                 
						}
				
		           	}
		           	else
				           	     setMSG("Please Enter Manually  Shift Codes",'N'); 
		           	
				}
			   
			  /*if(chkOVERW.isSelected() ==true )
			  {    
			        if(getSource()==tblSHFPT)
			        {    
			            if(((JTextField)tblSHFPT.cmpEDITR[tblSHFPT.getSelectedColumn() ]).getText().toString() .equals("O"))
			               {
			                return true;   
			               }
				            if(vldTBLSHFPT())
				            {
				                setMSG("Valid Shift Code",'N');
				                return true;
				            }
				            else
				            {
				                setMSG("Please Enter the Valid Shift Code .... A,B,C,D,E,F,G,O....",'E');
				                return false;
				            }
			        }
			  }*/  
			  //if(rdbOFF.isSelected())
			  {    
			        if(getSource()==tblSHFPT)
			        {    
			            if(((JTextField)tblSHFPT.cmpEDITR[tblSHFPT.getSelectedColumn() ]).getText().toString() .equals("O")
						   || ((JTextField)tblSHFPT.cmpEDITR[tblSHFPT.getSelectedColumn() ]).getText().toString() .equals(""))
			               {
			                return true;   
			               }
				            if(vldTBLSHFPT())
				            {
				                setMSG("Valid Shift Code",'N');
				                return true;
				            }
				            else
				            {
				                setMSG("In Valid Shift Code .... Please Enter Shift Code..A,B,C,F,G,O....",'E');
				                return false;
				            }
			        }
			  }    
			     
			  if(getSource()==tblSHFPT)
		        {
			      if((tblSHFPT.getValueAt(P_intROWID,P_intCOLID).toString().length()==2) )
					{
			           return false;
					}
			      else
			          return true;
		        }
			    	
			  
						 
			  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst )) 
				{
			     
			          if(P_intCOLID == intCHKFL_COL)
			          {   
       	                        for(int j = intSUMFIX; j<(tblSHFPT.getColumnCount());j++)
       	                        {
       	                              if(tblSHFPT.getValueAt(P_intROWID,0).equals(new Boolean(true)))
       	                                {
       	                                       tblSHFPT.setValueAt(new Boolean(true),P_intROWID,j);
       	                                    tblSHFPT.setValueAt("",P_intROWID,j);
       	                                }
       	                               
       	                        }
       	    				 
			          }
			     }
					
				
				                  
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"This is Table Inputverfier");
			}
			return true;
		}
	      
        
	}	
   
	/** Validate data entered by user, Format all text */
	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
					return true;
				
				if(input == txtDPTCD)
				{
					
        			System.out.println("inside verify");
					M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN ";
        			//if(flgALLDPT)
					//	M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' and  cmt_codcd='"+txtDPTCD.getText()+"'";
					//else
					String L_strEMPCHK = flgSICCHK ? " and EP_EMPNO = '"+strEMPNO_LOGIN+"'" : " AND EP_EMPNO in (select SUBSTRING(cmt_codcd,1,4) from CO_CDTRN where cmt_cgmtp='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp in ('HR"+cl_dat.M_strCMPCD_pbst+"LRC','HR"+cl_dat.M_strCMPCD_pbst+"LSN') and SUBSTRING(cmt_codcd,6,4)='"+strEMPNO_LOGIN+"')";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT' "+(flgALLDPT ? "" : " and  cmt_codcd='"+txtDPTCD.getText()+"' and cmt_codcd in (select EP_DPTCD from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' "+L_strEMPCHK+" )");
					
					M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
				    System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					if(!M_rstRSSET.next() || M_rstRSSET==null)
					{
						setMSG("Enter Valid Department Code",'E');
						return false;
					}
					else
						lblDPTCD.setText(M_rstRSSET.getString("CMT_SHRDS"));

					
				}	
				
				if(M_rstRSSET != null)
					M_rstRSSET.close();
			}
			catch(Exception E_VR)
			{
				setMSG(E_VR,"class INPVF");		
			}
			return true;
		}
	}	   
   

/**
 */
private void setSHFPT(int LP_ROWNO,int LP_COLNO,int LP_WKDAY)
{
	try
	{
		if(LP_COLNO+LP_WKDAY > arrSUMHDR.length)
			return;
		//if(tblSHFPT.getValueAt(LP_ROWNO,LP_COLNO+LP_WKDAY).toString().length()==0 /* || chkOVERW.isSelected()*/)
		if(((JTextField)tblSHFPT.cmpEDITR[tblSHFPT.getSelectedColumn()]).getText().toString().length()==0)
		{
		//	System.out.println("CleeCol : "+tblSHFPT.getCellColor( LP_ROWNO,LP_COLNO) +" Blue Col : "+Color.BLUE);
			//if(tblSHFPT.getCellColor( LP_ROWNO,LP_COLNO) != Color.BLUE)
				tblSHFPT.setValueAt((LP_WKDAY == 0 ? "O"  : txtSHFPT.getText().substring(LP_WKDAY-1,LP_WKDAY)),LP_ROWNO,LP_COLNO+LP_WKDAY);
		}
		
	}catch(Exception L_EX)
	{
	    setMSG(L_EX,"setSHFPT");
	}
}
   
/**
 */
private void setSHFPT_R(int LP_ROWNO,int LP_COLNO,int LP_WKDAY)
{
	try
	{
		if(LP_COLNO-LP_WKDAY < intSUMFIX)
			return;
		//if(tblSHFPT.getValueAt(LP_ROWNO,LP_COLNO-LP_WKDAY).toString().length()==0 /* || chkOVERW.isSelected()*/)
		if(((JTextField)tblSHFPT.cmpEDITR[tblSHFPT.getSelectedColumn()]).getText().toString().length()==0)
		{
		//	System.out.println("CleeCol : "+tblSHFPT.getCellColor( LP_ROWNO,LP_COLNO) +" Blue Col : "+Color.BLUE);
			//if(tblSHFPT.getCellColor( LP_ROWNO,LP_COLNO) != Color.BLUE)
				tblSHFPT.setValueAt((LP_WKDAY == 0 ? "O"  : txtSHFPT.getText().substring(6-LP_WKDAY,6-LP_WKDAY+1)),LP_ROWNO,LP_COLNO-LP_WKDAY);
		}
		
	}catch(Exception L_EX)
	{
	    setMSG(L_EX,"setSHFPT_R");
	}
}
	
/** Initializing table editing before poulating/capturing data
 */
private void inlTBLEDIT(JTable P_tblTBLNM)
{
	if(!P_tblTBLNM.isEditing())
		return;
	P_tblTBLNM.getCellEditor().stopCellEditing();
	P_tblTBLNM.setRowSelectionInterval(0,0);
	P_tblTBLNM.setColumnSelectionInterval(0,0);
			
}
   
   

		/** One time data capturing for specified codes from CO_CDTRN
		 * into the Hash Table
		 */
         private void crtCDTRN(String LP_CATLS,String LP_ADDCN,Hashtable<String,String[]> LP_HSTNM)
        {
			String L_strSQLQRY = "";
            try
            {
		        L_strSQLQRY = "select * from co_cdtrn where cmt_cgmtp + cmt_cgstp in ("+LP_CATLS+")   "+LP_ADDCN+" order by cmt_cgmtp,cmt_cgstp,cmt_codcd";
			    //System.out.println(L_strSQLQRY);
		        ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
                if(L_rstRSSET == null || !L_rstRSSET.next())
                {
		            //setMSG("Records not found in CO_CDTRN",'E');
                    return;
                }
                while(true)
                {
                        strCGMTP = getRSTVAL(L_rstRSSET,"CMT_CGMTP","C");
                        strCGSTP = getRSTVAL(L_rstRSSET,"CMT_CGSTP","C");
                        strCODCD = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                        String[] staCDTRN = new String[intCDTRN_TOT];
                        staCDTRN[intAE_CMT_CODCD] = getRSTVAL(L_rstRSSET,"CMT_CODCD","C");
                        staCDTRN[intAE_CMT_CODDS] = getRSTVAL(L_rstRSSET,"CMT_CODDS","C");
                        staCDTRN[intAE_CMT_SHRDS] = getRSTVAL(L_rstRSSET,"CMT_SHRDS","C");
                        staCDTRN[intAE_CMT_CHP01] = getRSTVAL(L_rstRSSET,"CMT_CHP01","C");
                        staCDTRN[intAE_CMT_CHP02] = getRSTVAL(L_rstRSSET,"CMT_CHP02","C");
                        staCDTRN[intAE_CMT_NMP01] = getRSTVAL(L_rstRSSET,"CMT_NMP01","C");
                        staCDTRN[intAE_CMT_NMP02] = getRSTVAL(L_rstRSSET,"CMT_NMP02","C");
                        staCDTRN[intAE_CMT_CCSVL] = getRSTVAL(L_rstRSSET,"CMT_CCSVL","C");
                        staCDTRN[intAE_CMT_NCSVL] = getRSTVAL(L_rstRSSET,"CMT_NCSVL","C");
                        staCDTRN[intAE_CMT_MODLS] = getRSTVAL(L_rstRSSET,"CMT_MODLS","C");
                        LP_HSTNM.put(strCGMTP+strCGSTP+strCODCD,staCDTRN);
				//if(strCGSTP.equals("MRXXPOD"))
					//System.out.println("Adding : "+strCGMTP+strCGSTP+strCODCD);
				hstCODDS.put(strCGMTP+strCGSTP+getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),strCODCD);
                        if (!L_rstRSSET.next())
                                break;
                }
                L_rstRSSET.close();
            }
            catch(Exception L_EX)
            {
                   setMSG(L_EX,"crtCDTRN");
            }
		}



		/** Picking up Specified Codes Transaction related details from Hash Table
		 * <B> for Specified Code Transaction key
		 * @param LP_CDTRN_KEY	Code Transaction key
		 * @param LP_FLDNM		Field name for which, details have to be picked up
		 */
        private String getCDTRN(String LP_CDTRN_KEY, String LP_FLDNM, Hashtable LP_HSTNM)
        {
		//System.out.println("getCDTRN : "+LP_CDTRN_KEY+"/"+LP_FLDNM);
        try
        {
				if(!LP_HSTNM.containsKey(LP_CDTRN_KEY))
					{setMSG(LP_CDTRN_KEY+" not found in CO_CDTRN hash table",'E'); return " ";}
                if (LP_FLDNM.equals("CMT_CODCD"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODCD];
                else if (LP_FLDNM.equals("CMT_CODDS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CODDS];
                else if (LP_FLDNM.equals("CMT_SHRDS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_SHRDS];
                else if (LP_FLDNM.equals("CMT_CHP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP01];
                else if (LP_FLDNM.equals("CMT_CHP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CHP02];
                else if (LP_FLDNM.equals("CMT_NMP01"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP01];
                else if (LP_FLDNM.equals("CMT_NMP02"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NMP02];
                else if (LP_FLDNM.equals("CMT_NCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_NCSVL];
                else if (LP_FLDNM.equals("CMT_CCSVL"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_CCSVL];
                else if (LP_FLDNM.equals("CMT_MODLS"))
                        return ((String[])LP_HSTNM.get(LP_CDTRN_KEY))[intAE_CMT_MODLS];
        }
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getCDTRN");
		}
        return "";
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
	{setMSG(L_EX,"getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);}
return " ";
} 
		


	/** Verification mehod for legal deviation
	 */
	private String chkLEGDV(String LP_LEGCD,String LP_PRVVL, String LP_SHFCD,int LP_DAYVL)
	{
		//System.out.println(strEMPNO+"   chkLEGDV :   "+LP_LEGCD+"  "+LP_PRVVL+"    "+LP_SHFCD+"   "+LP_DAYVL);
		//System.out.println(flgOFFASSG ? "Off assigned " : "---");
		String L_strRETVL = "";
		long L_intDFFVL = 0;
		try
		{
			//if(LP_PRVVL.equals(""))
			//	return "-";
			if(LP_LEGCD.equals("1"))
			{
				if(!LP_SHFCD.equalsIgnoreCase("O") && !LP_PRVVL.equalsIgnoreCase("O") && LP_DAYVL>0)
				{
					L_intDFFVL = getDTDIFF(getSHFTM_ST(LP_SHFCD,"IN",LP_DAYVL),getSHFTM_ST(LP_PRVVL,"OUT",LP_DAYVL-1),"HH");
					if(L_intDFFVL >= 0 && L_intDFFVL <12)
					   L_strRETVL = "Shift Gap is below 12 Hrs.";
				}
			}	
			else if(LP_LEGCD.equals("2"))
			{
				if(LP_SHFCD.equalsIgnoreCase("O"))
				{
					L_intDFFVL = getDTDIFF(fmtDBDATTM.format(fmtYYYYMMDD.parse(arrHSTKEY[LP_DAYVL].toString())),	fmtDBDATTM.format(fmtYYYYMMDD.parse(LP_PRVVL)),"DD");
					
					if(((L_intDFFVL >= 0 && L_intDFFVL <  3) && flgOFFASSG) || L_intDFFVL > (flgOFFASSG ? 10 : 9))
					   L_strRETVL = "Off Gap Excedds 10 days";
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkLEGDV");
		}
		return L_strRETVL;
	}


	private long getDTDIFF(String LP_strDAT1,String LP_strDAT2,String LP_RTNTP)// date format YYYY-mm-dd.hh.mm.ss 
	{
		long L_intRETVL=0;
		if(LP_strDAT1.length()<16 || LP_strDAT2.length()<16)
			return L_intRETVL;
		try
		{
			Calendar calendar1 = Calendar.getInstance();
			Calendar calendar2 = Calendar.getInstance();
			long milliseconds1;
			long milliseconds2;
			long diff;
			long diffSeconds;
			long diffMinutes;
			long diffHours;				//diff betn todays in time and yesterdays out time 		
			long diffDays;
			calendar1.set(Integer.parseInt(LP_strDAT1.substring(0,4)),Integer.parseInt(LP_strDAT1.substring(5,7)),Integer.parseInt(LP_strDAT1.substring(8,10)),Integer.parseInt(LP_strDAT1.substring(11,13)),Integer.parseInt(LP_strDAT1.substring(14,16)),Integer.parseInt(LP_strDAT1.substring(17,19)));
			calendar2.set(Integer.parseInt(LP_strDAT2.substring(0,4)),Integer.parseInt(LP_strDAT2.substring(5,7)),Integer.parseInt(LP_strDAT2.substring(8,10)),Integer.parseInt(LP_strDAT2.substring(11,13)),Integer.parseInt(LP_strDAT2.substring(14,16)),Integer.parseInt(LP_strDAT2.substring(17,19)));
			milliseconds1 = calendar1.getTimeInMillis();
			milliseconds2 = calendar2.getTimeInMillis();
			diff = milliseconds1 - milliseconds2;
			if(LP_RTNTP.equals("SS"))
				L_intRETVL= diff / 1000;
			else if(LP_RTNTP.equals("MM"))
				L_intRETVL= diff / (60 * 60 * 1000);
			else if(LP_RTNTP.equals("HH"))
				L_intRETVL= diff / (60 * 60 * 1000);
			else if(LP_RTNTP.equals("DD"))
				L_intRETVL= diff / (24 * 60 * 60 * 1000);

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDTDIFF");
		}
		//System.out.println("getDTDIFF : "+LP_strDAT1+"   "+LP_strDAT2+"   "+LP_RTNTP+"   L_intRETVL : "+L_intRETVL);
		return L_intRETVL;
	}

	
	/** Fetching Standard In / Out Time for specified shift
	 * Returns Timestamp value in String format "yyyy-MM-dd-HH.mm.ss"
	 */ 
	private String getSHFTM_ST(String LP_SHFCD,String LP_SHFFL,int LP_DAYVL)
	{
		String L_strRETVL = "";
		//System.out.println("getSHFTM_ST : "+LP_SHFCD+" / "+LP_SHFFL);
		if(LP_SHFCD.equalsIgnoreCase("O"))
			return L_strRETVL;
		try
		{
			if(!cl_dat.M_hstMKTCD_pbst.containsKey("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD))
				return L_strRETVL;
			datWRKDT1 = fmtYYYYMMDD.parse(arrHSTKEY[LP_DAYVL].toString());
			
			M_calLOCAL.setTime(datWRKDT1);      
			M_calLOCAL.add(Calendar.DATE,+1);    
			datWRKDT2 = M_calLOCAL.getTime();
			if(LP_SHFFL.equalsIgnoreCase("IN"))
			{
				datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)) >= 7 ? datWRKDT1 : datWRKDT2;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(0,2)+"."+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP01_pbst).substring(3,5)+".00";
			}
				
			if(LP_SHFFL.equalsIgnoreCase("OUT"))
			{
				datWRKDT = Integer.parseInt(getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)) <= 7 ? datWRKDT2 : datWRKDT1;
				L_strRETVL = fmtDBDATTM_YMD.format(datWRKDT)+"-"+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(0,2)+"."+getCODVL("M"+cl_dat.M_strCMPCD_pbst+"COXXSHF"+LP_SHFCD,cl_dat.M_intCHP02_pbst).substring(3,5)+".00";
			}
	
		}
		catch(Exception L_EX){
			setMSG(L_EX,"getSHFTM_ST");
		}
		return L_strRETVL;
	}	
	

	
	
	private boolean exeSICCHK(String LP_strEMPNO)
	{
		boolean flgSICCHK = false;
		try
		{
			M_strSQLQRY= " SELECT count(*) CNT from CO_CDTRN where cmt_cgmtp='S"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='HRXXSIC' and cmt_codcd = '"+LP_strEMPNO+"'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if (L_rstRSSET != null && L_rstRSSET.next())
			{
				if(L_rstRSSET.getInt("CNT")>0)
					flgSICCHK = true;
				L_rstRSSET.close();
			}
		}
		catch (Exception L_EX)	
		{
			setMSG("Error in exeSICCHK : "+L_EX,'E');
		}
			return flgSICCHK;
	}

	
	/** Fetching Employee code for specified user login
	 */ 
	private boolean setEMPNO_LOGIN(String LP_USRCD)
	{   
		try
		{
				M_strSQLQRY = "select US_EMPCD from SA_USMST where US_USRCD='"+LP_USRCD+"'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					return false;
				strEMPNO_LOGIN = L_rstRSSET.getString("US_EMPCD");
				System.out.println("001  "+strEMPNO_LOGIN);
				M_strSQLQRY = "select EP_CMPCD, EP_DPTCD from HR_EPMST where  ltrim(str(EP_EMPNO,20,0))='"+strEMPNO_LOGIN+"'";
				System.out.println(M_strSQLQRY);
				L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(L_rstRSSET==null || !L_rstRSSET.next())
					return false;
				System.out.println("ok1");
				strCMPCD_LOGIN = L_rstRSSET.getString("EP_CMPCD");
				System.out.println("ok1 2");
				txtDPTCD.setText(M_rstRSSET.getString("ep_dptcd"));
				System.out.println("ok1 1");
		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"setEMPNO_LOGIN");
	    }
		return true;
	}      

	
}
