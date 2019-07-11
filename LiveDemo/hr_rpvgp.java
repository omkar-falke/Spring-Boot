/*
System Name   : Human Resource Management System**/


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
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;
import java.util.Calendar;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.DataOutputStream; 
import java.io.*; 
import java.awt.Cursor.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.event.ChangeEvent;

public class hr_rpvgp extends cl_rbase 
{
    JOptionPane L_OPTNPN;
    JButton btnDSPLY;                    
	private String strPREVDT;            
	private FileOutputStream fosREPORT;  
	private DataOutputStream dosREPORT;  
	private String strFILNM;             
    private int intRECCT = 0;     
    int intPASNO;
	JTextField txtVSPFR,txtVSPTO;                
    JTextField txtPERVS,txtDPTNM;
    TxtDate txtVSTDT;
    
    String strPERVS,strVSPFR,strVSPTO;
    String strVSTNM;
    Date dtVSTDT;
    private  cl_JTable tblVSTAUT;         
    private String strVSORG;
    private String strPURPS;
    private String strCLRBY;
    private String strVSITM;
	private String strSTSFL;
    private Timestamp L_tmpTIME;
    private String L_strTEMP;
    private int L_ROWNO;
    private int intVSPSTR=0;;
    private int intADDSTR=0;
    private String strVSPNO;
    private String strVSTDT;
    private String strGINBY;
    private String strREMDS;;
	/* Constructor */	
	hr_rpvgp(String P_strSBSCD)
	{
		M_strSBSCD = P_strSBSCD;
		M_rdbHTML.setSelected(true);	
	}
	hr_rpvgp()
	{
	    super(2);
		try
		{
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);	
		    add(new JLabel("Visitor Pass No"),5,5,1,1,this,'L');
		    add(new JLabel("From"),6,4,1,1,this,'L');
		    add(txtVSPFR=new TxtNumLimit(8),6,5,1,1,this,'L');
		    add(new JLabel("To"),7,4,1,1,this,'L');
		    add(txtVSPTO=new TxtNumLimit(8),7,5,1,1,this,'L');
		    M_pnlRPFMT.setVisible(true);
		    txtVSPFR.setEnabled(false);
		    txtVSPTO.setEnabled(false);
		}
		catch(Exception L_EX)
        {
			setMSG(L_EX,"Constructor");
	    }
	}// end of constructor
	
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		
		    txtVSPFR.setEnabled(false);
		    txtVSPTO.setEnabled(false);
	}
	void remENBL(boolean L_flgSTAT)
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
		     if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			 {   
		         remENBL(false);
		         if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				 {	
		              
		             txtVSPFR.setEnabled(true);
		             txtVSPTO.setEnabled(true);
		             txtVSPFR.requestFocus();
				 }
		         else
				 {					
						cl_dat.M_cmbOPTN_pbst.requestFocus();
						setMSG("select option..",'N');
			           txtVSPFR.setEnabled(false);
					    txtVSPTO.setEnabled(false);
				}	
		         
				}
	
		     if(M_objSOURC==txtVSPTO )
		     {
		         
		     }
		     
		     
		 }catch(Exception L_EX)
		 {
		     
		 }
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
		try
		{
		    if(L_KE.getKeyCode()==L_KE.VK_ENTER)
			{
		        if(M_objSOURC==txtVSPFR)
		        {
		            strVSPFR=txtVSPFR.getText().toString().trim();
		            strVSPNO=strVSPTO;
		            if(vldVSPNO())
		            {
		              setMSG("Valid Visitor Pass No",'N');
		              txtVSPTO.setText(txtVSPFR.getText() .toString() .trim());
		              txtVSPTO.requestFocus() ;
		              
		            }
		            else
		            { 
		                setMSG("InValid Visitor Pass No Please Press F1",'E');
		                txtVSPFR.requestFocus() ;
		                
		            }
		                
		        }
		        if(M_objSOURC==txtVSPTO)
		        {
		            strVSPTO=txtVSPTO.getText().toString().trim();
		            strVSPNO=strVSPTO;
		            if(vldVSPNO())
		            {
		                setMSG("Valid Visitor Pass No",'N');
		                cl_dat.M_btnSAVE_pbst.requestFocus();
		           //     rdbSPEC.setEnabled(true);
		               // rdbSPEC.requestFocus() ;
		                
		            }
		            else
		            {
		                setMSG("InValid Visitor Pass No Please Press F1",'E');
		                txtVSPTO.requestFocus() ;
		                
		            }
		            
		        }
		        
		        
			}
	   		if (L_KE.getKeyCode()== L_KE.VK_F1)
			{					
		   		 if(M_objSOURC==txtVSPFR)
		   		 {   
		   		     	cl_dat.M_flgHELPFL_pbst = true;
		   		      	M_strHLPFLD = "txtVSPFR";
			   		    M_strSQLQRY="select VS_VSPNO,VS_VSTNM,VS_VSTDT,VS_VSITM from HR_VSTRN where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_STSFL <> 'X'";
			   		    if(txtVSPFR.getText().trim().length() >0)
						    M_strSQLQRY += " AND VS_VSPNO like '"+txtVSPFR.getText().trim() +"%'";
			   		    M_strSQLQRY += " order by VS_VSPNO desc";
					    cl_hlp(M_strSQLQRY,1,1,new String[]{"Vsitor Pass No","Visitor Name","Visitor Date","Vistor Time"},4	,"CT");
			   		    
		   		 }
		   		 if(M_objSOURC==txtVSPTO)
		   		 {   
		   		     	cl_dat.M_flgHELPFL_pbst = true;
		   		      	M_strHLPFLD = "txtVSPTO";
			   		    M_strSQLQRY="select VS_VSPNO,VS_VSTNM,VS_VSTDT,VS_VSITM from HR_VSTRN where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_STSFL='4'";
			   		    if(txtVSPFR.getText().trim().length() >0)
						    M_strSQLQRY += " AND VS_VSPNO like '"+txtVSPTO.getText().trim() +"%'";
			   		    M_strSQLQRY += " order by VS_VSPNO desc";
					    cl_hlp(M_strSQLQRY,1,1,new String[]{"Vsitor Pass No","Visitor Name","Visitor Date","Vistor Time"},4	,"CT");
			   		    
		   		 }
	   		    
			}
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"Key Pressed ");
		}
	}
	void exeHLPOK()
	{
	    super.exeHLPOK() ;
	    
	    
	    if(M_strHLPFLD=="txtVSPFR")
        {
			txtVSPFR.setText(cl_dat.M_strHLPSTR_pbst);
		//	strVSTNM =String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim() ;
		//	txtVSTNM.setText(strVSTNM);
		//	strVSTDT=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim() ;
		//	txtVSTDT.setText(strVSTDT);
		 }
	    if(M_strHLPFLD=="txtVSPTO")
        {
	        txtVSPTO.setText(cl_dat.M_strHLPSTR_pbst);
        }
	}
	
	private boolean vldVSPNO()
	{
		try{
			M_strSQLQRY = "Select * from HR_VSTRN where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSPNO='"+txtVSPFR.getText().toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldVSPNO ....."+L_EX);							   
		}
		return false;
	}
	
	
	
	
	/** This is used for to print the report on the specified Location " Word Format & HTML Format printer & Fax "
	 */	
		void exePRINT()
		{   
		    if (vldDATA())
			{
				try
				{
		        int RECCT  = 0 ;
		   		    
	   		    setMSG("Report Generation in Process....." ,'N');
	   		    setCursor(cl_dat.M_curWTSTS_pbst);
	   		   	getDATA(txtVSPFR.getText().toString() .trim(),txtVSPTO.getText().toString() .trim());
				System.out.println(strFILNM);
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
		}
		/** Method to fetch data from tblSTKDTL table & club it with Header in Data Output Stream  */
		 public void getDATA(String P_strFRVSP,String P_strTOVSP)
		 {
			try
			{   
				cl_dat.M_PAGENO = 0;
			    String strln="";
			   if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"hr_rpvgp.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "hr_rpvgp.doc";	
			    fosREPORT = new FileOutputStream(strFILNM);
			    dosREPORT = new DataOutputStream(fosREPORT);
			    int RECCT  = 0 ;
		          cl_dat.M_intLINNO_pbst=0;
	   		    setMSG("Report Generation in Process....." ,'N');
	   		    setCursor(cl_dat.M_curWTSTS_pbst);
	   		    if(M_rdbHTML.isSelected())
			    {
	   		     //</IMG>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <IMG src=d:\\splerp2\\spllogo_old.gif width=56 height=56 ></img>
	   		        dosREPORT.writeBytes("<HTML><HEAD><Title> </Title></HEAD><BODY> <P><PRE style = \"font-size : 10 pt \">");			        
	   		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
			        dosREPORT.writeBytes("</STYLE>");
			        dosREPORT.writeBytes("\n");
				 }
				 else
				 {
				    // prnFMTCHR(dosREPORT,M_strNOCPI17);
					 //   prnFMTCHR(dosREPORT,M_strCPI10);
					  //  prnFMTCHR(dosREPORT,M_strCPI12);
					    prnFMTCHR(dosREPORT,M_strCPI17);
				 }	
			    
	   		      		    
			   	/****gets the header of the report*/
			   	//VS_VSORG,VS_PURPS,VS_PERVS,VS_CLRBY,VS_VSITM
	   		    getRECORDS(P_strFRVSP,P_strTOVSP);
			 //   prnHEADER();
			    //outRESECD();
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"This is GetData");
			}
		 }	
		
		  
		  private void getRECORDS(String P_strFRVGP,String P_strTOVGP)
		  {
		      try
		      {
		          M_strSQLQRY = "select * from  HR_VSTRN LEFT OUTER JOIN HR_RMMST ON VS_CMPCD=RM_CMPCD AND VS_SBSCD = RM_SBSCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND RM_TRNTP ='HC' and RM_TRNCD ='01'" ;
                  M_strSQLQRY += "  where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSPNO between '"+P_strFRVGP+"'  and   '"+P_strTOVGP+"'";
				// and VS_VSOTM IS NULL and VS_STSFL='4'";
				  M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
				   	L_ROWNO=0;
				   	while(M_rstRSSET.next() )
				   	{
				   	    strVSTNM=nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),"");
				   	    strVSORG=nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),"");
				   	    strPURPS=nvlSTRVL(M_rstRSSET.getString("vs_purps"),"");
				   	    strPERVS=nvlSTRVL(M_rstRSSET.getString("vs_pervs"),"");
				   	    strCLRBY=nvlSTRVL(M_rstRSSET.getString("vs_clrby"),"");
				   	    strVSPNO=nvlSTRVL(M_rstRSSET.getString("vs_VSPNO"),"");
				   	    strGINBY=nvlSTRVL(M_rstRSSET.getString("VS_GINBY"),"");
				   	    strREMDS=nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
				   	    dtVSTDT=M_rstRSSET.getDate("vs_vstdt");
						strSTSFL=nvlSTRVL(M_rstRSSET.getString("VS_STSFL"),"");
				   	    L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
				   	    L_strTEMP="";
				   	    if (L_tmpTIME != null)
				   	    {
	 					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
	 			   	    }
				   	 L_ROWNO++;
				   	 prnHEADER();
				   	if(cl_dat.M_intLINNO_pbst > 60)
					{
				      	dosREPORT.writeBytes("\n");
						
						//prnHEADER();
						prnFMTCHR(dosREPORT,M_strEJT);
						
						
					}
				   	}
				   	M_rstRSSET.close() ;
		          
		      }catch(Exception L_EX)
		      {
		          setMSG(L_EX,"This is getRECORDS");
		          
		      }
		  }
		  
		public void prnHEADER()
		{
		    try
		    {
		        
		        cl_dat.M_PAGENO += 1;
			    cl_dat.M_intLINNO_pbst=0;
			    
				 strGINBY="";
				//prnFMTCHR(dosREPORT,M_strBOLD);
				
				   // dosREPORT.writeBytes("<PRE style =\" font-size : 10 pt \">");
				if(M_rdbHTML.isSelected())
				{   
			    		///dosREPORT.writeBytes("<HTML><HEAD><Title>Visitor Pass </Title></HEAD><BODY></IMG>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <IMG src=d:\\splerp2\\spllogo_old.gif width=56 height=56 ></img> </head></html> ");
					dosREPORT.writeBytes("<HTML><HEAD><Title>Visitor Pass </Title></HEAD><BODY>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </head></html> ");
					dosREPORT.writeBytes("\n"); 
					dosREPORT.writeBytes(padSTRING('R',"",14));
					prnFMTCHR(dosREPORT,M_strBOLD);
					
					
					dosREPORT.writeBytes(padSTRING('C',"SUPREME PETROCHEM LTD" ,60));
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					
					dosREPORT.writeBytes("\n\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"Date:" +M_fmtLCDAT.format(dtVSTDT) ,20 ));
					if((strSTSFL.equals("1"))||(strSTSFL.equals("2"))||(strSTSFL.equals("3"))||(strSTSFL.equals("4")))
						dosREPORT.writeBytes(padSTRING('L',"Visitor Request:"+strVSPNO  ,40 ));
					else
						dosREPORT.writeBytes(padSTRING('L',"Visitor Pass No:"+strVSPNO  ,40 ));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('L',"Badge No :",46 ));
					dosREPORT.writeBytes("\n\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"Name             : " +strVSTNM ,50 ));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"Organisation     : " +strVSORG ,50 ));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"To See           : " +strPERVS ,50 ));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"Purpose          : " +strPURPS ,50 ));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					
					dosREPORT.writeBytes(padSTRING('R',"Visit Cleared by : " +strCLRBY, 24));
					dosREPORT.writeBytes(padSTRING('L',"Escort by : " , 28));
										
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					if((strSTSFL.equals("1"))||(strSTSFL.equals("2"))||(strSTSFL.equals("3"))||(strSTSFL.equals("4")))
						dosREPORT.writeBytes(padSTRING('R',"Time Expected    : " +L_strTEMP ,41));//+ "      "  + "Time Out:" ,90 ));
					else												  
						dosREPORT.writeBytes(padSTRING('R',"Time In          : " +L_strTEMP ,41));//+ "      "  + "Time Out:" ,90 ));
					dosREPORT.writeBytes(padSTRING('R',"Time Out: " ,28));
					dosREPORT.writeBytes("\n\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"_____________",33));
					dosREPORT.writeBytes(padSTRING('L',strGINBY,15));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"Sign Visitor",34));
					dosREPORT.writeBytes(padSTRING('L',"Gate In Charge",20));
					dosREPORT.writeBytes("\n\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"_________________",40));
					dosREPORT.writeBytes(padSTRING('L',"_________________ "  ,15));
					
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"Sign Pers Visited",32));
					dosREPORT.writeBytes(padSTRING('L',"Escort",20));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"____________________________________________________________",90));
					
				//	prnFMTCHR(dosREPORT,M_strCPI17);
					if(M_rdbHTML.isSelected())
					{
					    dosREPORT.writeBytes("<HTML><HEAD><Title>Visitor Pass </Title></HEAD><BODY> <P><PRE style = \"font-size : 65% \">");			        
		   		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
				        dosREPORT.writeBytes("</STYLE>");
					    
					}
				    
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",23));
					dosREPORT.writeBytes(padSTRING('R',"*Visitor must declare material, to be taken inside and obtain endorsement from the gate office." ,95));
			//		prnFMTCHR(dosREPORT,M_strCPI17);
					dosREPORT.writeBytes("\n");
					
					dosREPORT.writeBytes(padSTRING('R',"",23));
					//prnFMTCHR(dosREPORT,M_strCPI17);
					dosREPORT.writeBytes(padSTRING('R',"*The Company(SPL) shall not be liable for any injury sustained by the visitor or loss/damage" ,135));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",24));
					dosREPORT.writeBytes(padSTRING('R',"to the property brought in by the Visitor.",45));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",23));
					dosREPORT.writeBytes(padSTRING('R',"*SMOKING,TOBACO ITEMS,CELL PHONE & PHOTOGRAPHY STRICTLY PROHIBITED" ,73));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",23));
					dosREPORT.writeBytes(padSTRING('R',"*THIS PASS IS VALID UPTO 17.00 HRS. ON THE DAY OF ISSUE" ,63));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",23));
					dosREPORT.writeBytes(padSTRING('R',"*PLEASE COLLECT YOUR COPY OF" ,30));
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('R','"' +"GUIDELINES FOR VISITORS" +'"',63));
					prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes("\n");
					
					
					
					//</IMG>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<IMG src=d:\\splerp2\\spllogo_old.gif width=56 height=56 ></img> 
					if(M_rdbHTML.isSelected())
					{
					    dosREPORT.writeBytes("<HTML><HEAD><Title>Visitor Pass </Title></HEAD><BODY><P><PRE style = \"font-size : 10 pt \">");			        
		   		        dosREPORT.writeBytes("<STYLE TYPE =\"text/css\">P.breakhere {page-break-before: always}");
				        dosREPORT.writeBytes("</STYLE>");
					}
					
					dosREPORT.writeBytes(padSTRING('R',"",15));
					dosREPORT.writeBytes(padSTRING('R',"Material Carried / Deposited : ",32));
					dosREPORT.writeBytes(padSTRING('R',strREMDS,60));
					dosREPORT.writeBytes("\n");
					for(int i=0;i<12;i++)
						dosREPORT.writeBytes("\n");
					
			}
				if(M_rdbTEXT.isSelected() )
				{
				   
				    	dosREPORT.writeBytes("\n");
				    	cl_dat.M_intLINNO_pbst+=1;
						dosREPORT.writeBytes("\n"); 
						dosREPORT.writeBytes(padSTRING('R',"",12));
						prnFMTCHR(dosREPORT,M_strBOLD);
						
						
						dosREPORT.writeBytes(padSTRING('C',"SUPREME PETROCHEM LTD" ,60));
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						
						dosREPORT.writeBytes("\n\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"Date:" +M_fmtLCDAT.format(dtVSTDT),20 ));
						dosREPORT.writeBytes(padSTRING('L',"Visitor Pass No:"+strVSPNO  ,50 ));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('L',"Badge No :",56 ));
						dosREPORT.writeBytes("\n\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"Name             : " +strVSTNM ,50 ));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"Organisation     : " +strVSORG ,50 ));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"To See           : " +strPERVS ,50 ));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"Purpose          : " +strPURPS ,50 ));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						
						dosREPORT.writeBytes(padSTRING('R',"Visit Cleared by : " +strCLRBY, 27));
						dosREPORT.writeBytes(padSTRING('L',"Escort by : " , 36));
						
						    
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"Time In          : " +L_strTEMP ,52));//+ "                 "  + "Time Out:" ,90 ));
						dosREPORT.writeBytes(padSTRING('R',"Time Out: ",28));   
						dosREPORT.writeBytes("\n\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"_____________",35));
						dosREPORT.writeBytes(padSTRING('L',strGINBY,30));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"Sign Visitor",36));
						dosREPORT.writeBytes(padSTRING('L',"Gate In Charge",35));
						dosREPORT.writeBytes("\n\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"_________________",40));
						dosREPORT.writeBytes(padSTRING('L',"_____________",30));
						
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"Sign Pers Visited",32));
						dosREPORT.writeBytes(padSTRING('L',"Escort",35));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"_______________________________________________________________________________________________",145));
						
						prnFMTCHR(dosREPORT,M_strCPI17);
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"*Visitor must declare material, to be taken inside and obtain endorsement from the gate office." ,95));
				//		prnFMTCHR(dosREPORT,M_strCPI17);
						dosREPORT.writeBytes("\n");
						
						dosREPORT.writeBytes(padSTRING('R',"",15));
						//prnFMTCHR(dosREPORT,M_strCPI17);
						dosREPORT.writeBytes(padSTRING('R',"*The Company(SPL) shall not be liable for any injury sustained by the Visitor or loss/damage" ,135));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",16));
						dosREPORT.writeBytes(padSTRING('R',"to the property brought in by the Visitor.",45));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"*SMOKING,TOBACO ITEMS,CELL PHONE & PHOTOGRAPHY STRICTLY PROHIBITED" ,73));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"*THIS PASS IS VALID UPTO 17.00 HRS. ON THE DAY OF ISSUE" ,63));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',"",15));
						dosREPORT.writeBytes(padSTRING('R',"*PLEASE COLLECT YOUR COPY OF" ,29));
						prnFMTCHR(dosREPORT,M_strBOLD);
						dosREPORT.writeBytes(padSTRING('R','"'+ "GUIDELINES FOR VISITORS" +'"' ,63));
						prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n");
						prnFMTCHR(dosREPORT,M_strNOCPI17);
						//dosREPORT.writeBytes(padSTRING('R',"",1));
						//dosREPORT.writeBytes(padSTRING('R',"____________________________________________________________",90));
						
						prnFMTCHR(dosREPORT,M_strCPI12);
						dosREPORT.writeBytes(padSTRING('R',"",11));
						dosREPORT.writeBytes(padSTRING('R',"Material Carried / Deposited : ",32));
						dosREPORT.writeBytes(padSTRING('R',strREMDS,30));
						prnFMTCHR(dosREPORT,M_strNOCPI17);
						for(int i=0;i<5;i++)
							dosREPORT.writeBytes("\n");
						
						 
						dosREPORT.writeBytes("\n");
						 cl_dat.M_intLINNO_pbst+=1;
				}	
					
					
					
					
					
					
					
					
					 
		    }catch(Exception L_EX)
		    {
		        setMSG(L_EX,"prnHEADER");
		    }
		}
		  
		  
		  /*  Method to validate data */	        
	    boolean vldDATA()
		{
		    try
		    {
		        if(txtVSPFR.getText().trim().length() == 0)
				{
					setMSG("Enter  Date..",'E');
					txtVSPFR.requestFocus();
					return false;
				}
		        if(txtVSPTO.getText().trim().length() == 0)
				{
					setMSG("Enter  Date..",'E');
					txtVSPTO.requestFocus();
					return false;
				}
				if(Integer.parseInt(txtVSPFR.getText().trim()) > (Integer.parseInt(txtVSPTO.getText().trim())))
				{
					setMSG("From Date should not be grater than current Date..",'E');
					txtVSPTO.requestFocus();
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
				setMSG(L_EX,"vldDATA");
			}	
			return true;
		}
	
	
	
	
}	