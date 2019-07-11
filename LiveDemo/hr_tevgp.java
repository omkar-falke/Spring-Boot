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
import java.text.SimpleDateFormat;

class hr_tevgp extends cl_pbase implements KeyListener, ItemListener, FocusListener, Runnable // ,ChangeListener
		, MouseListener
{
    JOptionPane L_OPTNPN;
	protected SimpleDateFormat fmtDATE = new SimpleDateFormat("yyMMddHHmm");
	private FileOutputStream fosREPORT;  
	private DataOutputStream dosREPORT;  
	private String strPASNO,L_strVCATE;             
    private int intRECCT = 0;
    
    JLabel lblSTSFL;
	JTextField txtVTYPE;                 
	JTextField txtVSTNM;                 
	JTextField txtVSORG;                 
	JTextField txtVSTCT,txtVSPNO;                
    JTextField txtPERVS,txtDPTNM;
    JLabel lblEMPNO;
    JTextField txtREMDS,txtPURPS,txtVSITM,txtVEHNO;   
    private  JRadioButton	rdbMOBFLY;
	private  JRadioButton	rdbMOBFLN;
	private  JRadioButton	rdbCMRFLY;
	private  JRadioButton	rdbCMRFLN;
	private  ButtonGroup btgMOBFL;
	private  ButtonGroup btgCMRFL;
	TxtDate txtVSTDT;
   
    
    String strSTSFL,strDEPTNM,strPERVS="";
    private  JPanel	pnlTAB;            
	private  JPanel	pnlTAB1;   
	int intVSOCT,intVSICT;
	 int intVSCNT;         
    JComboBox cmbVSCAT;
            
    private  JTabbedPane tbpMAIN;
    private  cl_JTable tblVSTAUT;         
    private final int TB1_CHKFL =0;                          /** final value for Visitor tables Column */
	private final int TB1_VSCAT=1;
	private final int TB1_VSPNO =2;
	private final int TB1_VSTNM =3;
	private final int TB1_VSORG =4;
	private final int TB1_VSTCT =5;
	private final int TB1_PERVS =6;
	private final int TB1_CLRBY =7;
	private final int TB1_VSTDT =8;
	private final int TB1_PURPS =9;
	private final int TB1_VSARA =10;
	private final int TB1_VLDTM =11;
	private final int TB1_REMDS =12;
	private final int TB1_EMPNO =13;
    int i=0;
    private JTextField txtVSARA,txtARADS;
    private TxtDate txtVLDTM;
    private TxtTime txtVLDTM1;
    private TxtLimit txtCLRBY;
    private JLabel lblSTSFL1;

    private String strCODDS;
	/* Constructor */	
	hr_tevgp()
	{
	    super(2);
		try
		{
		    
		    setMatrix(20,12);
		    
		    tbpMAIN=new JTabbedPane();
		    pnlTAB=new JPanel(null);
		    pnlTAB1=new JPanel(null);
		    add(new JLabel("Guest Category"),1,3,1,1.5,pnlTAB,'L');
		    add(cmbVSCAT=new JComboBox(),1,6,1,2,pnlTAB,'L');
		   
		    add(new JLabel("Guest Pass No"),2,3,1,1.5,pnlTAB,'L');
		    add(txtVSPNO=new TxtLimit(8),2,6,1,2,pnlTAB,'L');
		    
		    add(new JLabel("Name of Guest"),3,3,1,1.5,pnlTAB,'L');
		    add(txtVSTNM=new TxtLimit(35),3,6,1,2.5,pnlTAB,'L');
		    
		    
		    add(new JLabel("Organization "),4,3,1,2,pnlTAB,'L');
		    add(txtVSORG=new TxtLimit(35),4,6,1,2.5,pnlTAB,'L');
		    
		    
		    add(new JLabel("No.of Guests"),5,3,1,3.3,pnlTAB,'L');
		    add(txtVSTCT=new TxtNumLimit(3),5,6,1,2,pnlTAB,'L');		    
		    
		    add(new JLabel("Person to be Visited"),6,3,1,3,pnlTAB,'L');
		    add(txtPERVS=new TxtLimit(20),6,6,1,2,pnlTAB,'L');
		    add(lblEMPNO=new JLabel(""),6,8,1,1,pnlTAB,'L');
		    
		    add(new JLabel("Department"),7,3,1,1.5,pnlTAB,'L');
		    add(txtDPTNM =new TxtLimit(10),7,6,1,2,pnlTAB,'L');
		    
		    
		    add(new JLabel("From Date/Time"),8,3,1,1.5,pnlTAB,'L');
		    add(txtVSTDT=new TxtDate(),8,6,1,1,pnlTAB,'L');
    		add(txtVSITM=new TxtTime(),8,7,1,1,pnlTAB,'L');

		    add(new JLabel("To Date/Time"),9,3,1,1.5,pnlTAB,'L');
		    add(txtVLDTM=new TxtDate(),9,6,1,1, pnlTAB,'L');
		    add(txtVLDTM1=new TxtTime(),9,7,1,1,pnlTAB,'L');
		    
		    add(new JLabel("Purpose"),10,3,1,1,pnlTAB,'L');
		    add(txtPURPS=new TxtLimit(50),10,6,1,5,pnlTAB,'L');
		    
		    	    
		    add(new JLabel("Area of Visit"),11,3,1,1.5,pnlTAB,'L');
		    add(txtVSARA=new TxtLimit(2),11,6,1,1,pnlTAB,'L');
    		    add(txtARADS=new TxtLimit(25),11,7,1,2,pnlTAB,'L');

		    add(new JLabel("Remarks"),12,3,1,1,pnlTAB,'L');
		    add(txtREMDS=new JTextField(),12,6,1,5,pnlTAB,'L');
			
		    add(new JLabel("Vehicle No."),13,3,1,1,pnlTAB,'L');
		    add(txtVEHNO=new TxtLimit(15),13,6,1,2,pnlTAB,'L');
			
			btgMOBFL=new ButtonGroup();
			btgCMRFL=new ButtonGroup();
			
			add(new JLabel("Mobile Allowed"),14,3,1,2,pnlTAB,'L');
			add(rdbMOBFLY=new JRadioButton("Yes"),14,6,1,1,pnlTAB,'L');
			add(rdbMOBFLN=new JRadioButton("No"),14,7,1,1,pnlTAB,'L');
				
			add(new JLabel("Camera Allowed"),15,3,1,2,pnlTAB,'L');
			add(rdbCMRFLY=new JRadioButton("Yes"),15,6,1,1,pnlTAB,'L');
			add(rdbCMRFLN=new JRadioButton("No"),15,7,1,1,pnlTAB,'L');
			
			rdbMOBFLN.setSelected(true);
			rdbCMRFLN.setSelected(true);
			
		    add(lblSTSFL1=new JLabel("Status"),16,3,1,1,pnlTAB,'L');
		    add(lblSTSFL=new JLabel(),16,6,1,2,pnlTAB,'L');
		    lblSTSFL.setForeground(Color.BLUE);    
		    tbpMAIN.addTab("Visitor Gate Pass Request",pnlTAB);
			btgMOBFL.add(rdbMOBFLY);btgMOBFL.add(rdbMOBFLN);btgCMRFL.add(rdbCMRFLY);btgCMRFL.add(rdbCMRFLN);
			String[] L_strCOLHD = {"Select","Guest Category","Guest Pass No"," Name of the Guest","Orgainsation Visit","No.of Guests","Person Visit","Visit Cleared by","Visit Date","Purpose","Area","Valid Time","Remarks","Emp.No."};
	        int[] L_intCOLSZ = {20,100,80,80,80,100,80,80,100,100,80,80, 100,40};	    				
		    tblVSTAUT = crtTBLPNL1(pnlTAB1,L_strCOLHD,5000,2,1,9,11.6,L_intCOLSZ,new int[]{0});
				
		    tblVSTAUT.setCellEditor(TB1_CLRBY,txtCLRBY = new TxtLimit(5));
		    cmbVSCAT.addMouseListener( this);
		    tbpMAIN.addMouseListener( this);
		    //tbpMAIN.addTab("Authorize",pnlTAB1);
		    add(tbpMAIN,1,1,18,12,this,'L')	;
		    
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXVCT' and CMT_CHP01='01' ";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD"; //AND CMT_CODCD = '03' only for external visitors
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_strVCATE = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
				cmbVSCAT.addItem(L_strVCATE);
			}
			
		    setENBL(false);
			INPVF oINPVF=new INPVF();			
			txtVSTDT.setInputVerifier(oINPVF);			
			txtVLDTM.setInputVerifier(oINPVF);			
			txtVSITM.setInputVerifier(oINPVF);			
			txtVLDTM1.setInputVerifier(oINPVF);			
	   	}
		catch(Exception L_EX)
           {
			setMSG(L_EX,"Constructor");
	     }
	}// end of constructor
	
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);
		{
		    tbpMAIN.setEnabled( false);
		    cmbVSCAT.setEnabled(false);
		    txtVSITM.setEnabled(false);
		    txtVSTNM.setEnabled(false);
		    txtVSORG.setEnabled(false);
		    txtPERVS.setEnabled(false);
		    txtVSTCT.setEnabled(false);
		    txtVSTDT.setEnabled(false);
		    txtPURPS.setEnabled(false);
		    txtREMDS.setEnabled(false);
		    txtVSPNO.setEnabled(false);
		    txtDPTNM.setEnabled(false);
		    tblVSTAUT.setEnabled(false);
		    txtVLDTM.setEnabled(false);
		    txtVLDTM1.setEnabled(false);
		    txtVSARA.setEnabled(false);
		    tblVSTAUT.setEnabled(false);
		    
		}
		 
	}
	public void actionPerformed(ActionEvent L_AE)
	{
	     super.actionPerformed(L_AE);
		 try
		 {
			if(M_objSOURC==cmbVSCAT)
			{
				if(cmbVSCAT.getItemCount() >0)
				{
					int L_intSELID= cmbVSCAT.getSelectedIndex();
					clrCOMP();
					lblSTSFL1.setText("");	
					cmbVSCAT.setSelectedIndex(L_intSELID);
				}
			}    
		     if(tbpMAIN.getSelectedIndex() == 0)
			 { 
				if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)	 
				{
					rdbMOBFLN.setSelected(true);
					rdbCMRFLN.setSelected(true);
				}	
			   if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			    {
			   
			        tbpMAIN.setEnabled( true);
				    cmbVSCAT.setEnabled(true);
				    tblVSTAUT.setEnabled(false);
				    
				    cmbVSCAT.requestFocus() ;
				    txtVSTDT.setText(cl_dat.M_strLOGDT_pbst);
    		  	    }
				if(M_objSOURC==txtVSPNO)
			        {
					String L_strVSPNO = txtVSPNO.getText().trim();
					int L_intSELID= cmbVSCAT.getSelectedIndex();
					clrCOMP();
					lblSTSFL1.setText("");	
					cmbVSCAT.setSelectedIndex(L_intSELID);
					txtVSPNO.setText(L_strVSPNO);	       
					getDATA();	
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		      		{	
      					txtVSTNM.setEnabled(true);
				            txtVSTNM.requestFocus() ;
					}
			        }
			    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			    {
			        tbpMAIN.setEnabled( true);
			        cmbVSCAT.setEnabled(true);
			        tblVSTAUT.setEnabled(false);
			        if(M_objSOURC==cmbVSCAT)
			        {    
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						 txtVSTNM.setEnabled(true);
		   		             txtVSTNM.requestFocus() ;
					}
					else
					{
			            	txtVSPNO.setEnabled(true);    
						System.out.println("Combo Action");
				            txtVSPNO.requestFocus() ;
					}
			        } 
			        
			        if(M_objSOURC==txtVSTNM)
			        {   
				       if(txtVSTNM.getText() .length() >0)
				       {
				        txtVSORG.setEnabled(true);   
					    txtVSORG.requestFocus() ;
				       } 
				       else
				       {   
				           setMSG("Enter Visitor's Name ",'E'); 
				           txtVSTNM.requestFocus() ;
				       }   
			        }  
					    if(M_objSOURC==txtVSORG)
					    {   
					       if(txtVSORG.getText() .length() >0)
					       {   
					           txtVSTCT.setEnabled(true);
					           txtVSTCT.requestFocus() ;
					       }   
					       else
					       {    
					           setMSG("Please Enter visitor's Organization",'E');  
					           txtVSORG.requestFocus() ;
					       } 
					       
					    }
					    if(M_objSOURC==txtVSTCT)
					    {
					       if(txtVSTCT.getText().length()>0)
					       {    
					           txtPERVS.setEnabled(true);
					           txtPERVS.requestFocus() ;
					       } 
					       else
					       {
					           setMSG("Enter the number of persons",'E');
					           txtVSTCT.requestFocus() ;
					       }   
					    }    
					    
					    if(M_objSOURC==txtPERVS)
					    {
					        if(txtPERVS.getText().length()  >0)
					        {  
					          if(strPERVS == null)
						    {
								setMSG("Please Press F1 and  Select ",'E');
						           txtPERVS.requestFocus(); 	
						    }	
						        if(strPERVS.equals(txtPERVS.getText().toString() .trim() ))
							  {    
						           txtVSTDT.setEnabled(true);
							     txtVSTDT.setText(cl_dat.M_strLOGDT_pbst);
						           txtVSTDT.requestFocus() ;
						           
						         } 
						       else
						       {   
						           setMSG("Please Press F1 and  Select ",'E');
						           txtPERVS.requestFocus() ;
						       }
					        }
					        else
					        {
					            setMSG("Please Valid Person of Visited " ,'E');
					            txtPERVS.requestFocus();
					        }
					            
					    }
					    if(M_objSOURC==txtDPTNM)
					    {
					        
					    }
					    if(M_objSOURC==txtVSTDT)
					    {
					        if(txtVSTDT.getText() .length() >0)
					        {   
					            txtVSITM.setEnabled(true);
					            txtVSITM.requestFocus() ;
					        }   
					      
					        else
					        {  
					            setMSG("Please Enter Visitor Date",'E');
					            txtVSTDT.requestFocus() ;
					        }   
					    }
					    if(M_objSOURC==txtVSITM)
					    {
					        if(txtVSITM.getText().length() >0)
					        {
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
								 txtVLDTM.setText(cl_dat.M_strLOGDT_pbst );
					             txtVLDTM.setEnabled(true);
					             txtVLDTM.requestFocus();
					         	
					         }
					        else
					        {
					         setMSG("Please Enter the time",'E');
					         txtVSITM.requestFocus() ;
					        }
					        
					    }
					    
				       if(M_objSOURC==txtPURPS)
					    {
					        if(txtPURPS.getText() .length() >0)
					        {   
					            txtVSARA.setEnabled(true);
					            txtVSARA.requestFocus() ;
					        }  
					        else
					        {
					            setMSG("Please Enter Purpose of Visitor",'E');
					            txtPURPS.requestFocus() ;
					        }
					    }
				       if(M_objSOURC==txtVSARA)
				       {
				           if(txtVSARA.getText() .length() >0)
					        {
				               if(vldVSARA())
				           	    {   
							  setMSG("Enter Area Description..",'N');
					              txtARADS.setEnabled(true);
					              txtARADS.requestFocus();
				           	    }
				               else
				               {
				                   setMSG("Please Press F1 Screen",'E');
				                   txtVSARA.requestFocus();
				               }
					        }
				           else
				           {
				               setMSG("please Press F1 To Display the Screen ",'E'); 
				               txtVSARA.requestFocus();
				           }   
				       }
  					if(M_objSOURC==txtARADS)
				       {
				           if(txtARADS.getText() .length() >0)
					        {
							  setMSG("Enter Remarks",'N');
					              txtREMDS.setEnabled(true);
					              txtREMDS.requestFocus();
					        }
				           else
				           {
				               setMSG("Please enter the Description of Area of visit..",'E'); 
				               txtARADS.requestFocus();
				           }   
				       }
					    if(M_objSOURC==txtREMDS)
					    {
							 setMSG("Enter Vehicle No.",'N');
					              txtVEHNO.setEnabled(true);
					              txtVEHNO.requestFocus();
					    }
					    if(M_objSOURC==txtVEHNO)
					        cl_dat.M_btnSAVE_pbst .requestFocus() ;

  
				       if(M_objSOURC==txtVLDTM)
				       {
				           if(txtVLDTM.getText() .length() >0)
					        { 
				              
				               txtVLDTM1.setEnabled(true);
				               txtVLDTM1.requestFocus();
				               
					        }
				           else
				           {
				               setMSG("Please Enter the Valid Date",'N');
				               txtVLDTM.requestFocus() ;
				           }
				           
				           
				       }
					    
					if(M_objSOURC==txtVLDTM1)
					{
					        if(txtVLDTM1.getText() .length() >0)
					        {
					            txtPURPS.setEnabled(true);
					            txtPURPS.requestFocus() ;
					         }
					        else
					        {
						         setMSG("Please Enter the time",'E');
						         txtVLDTM1.requestFocus() ;
						   }
					}

					if(M_objSOURC==txtPURPS)
					    {
					        if(txtPURPS.getText() .length() >0)
					        {   
					            txtVSARA.setEnabled(true);
					            txtVSARA.requestFocus() ;
					        }  
					        else
					        {
					            setMSG("Please Enter Purpose of Visitor",'E');
					            txtPURPS.requestFocus() ;
					        }
					    }
				       /*if(M_objSOURC==txtVSARA)
				       {
				           if(txtVSARA.getText() .length() >0)
					        {
				               if(vldVSARA())
				           	    {   
							  setMSG("Enter Remarks",'N');
					              txtREMDS.setEnabled(true);
					              txtREMDS.requestFocus();
				           	    }
				               else
				               {
				                   setMSG("Please Press F1 Screen",'E');
				                   txtVSARA.requestFocus();
				               }
					        }
				           else
				           {
				               setMSG("please Press F1 To Display the Screen ",'E'); 
				               txtVSARA.requestFocus();
				           }   
				       }

					    if(M_objSOURC==txtREMDS)
					    {
							 setMSG("Enter Vehicle No.",'N');
					              txtVEHNO.setEnabled(true);
					              txtVEHNO.requestFocus();
					    }
					    if(M_objSOURC==txtVEHNO)
					        cl_dat.M_btnSAVE_pbst .requestFocus() ;*/
			        
			    }
    		    
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			    {
		
			        tbpMAIN.setEnabled( true);
			        cmbVSCAT.setEnabled(true);
			        tblVSTAUT.setEnabled(false);
			        txtVSPNO.setEnabled(true);
				  System.out.println("Frpm 2");		        
			        txtVSPNO.requestFocus();
			        
			    }
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))
			    {
			        
			        tbpMAIN.setEnabled( true);
			        cmbVSCAT.setEnabled(true);
			        tblVSTAUT.setEnabled(false);
			        txtVSPNO.setEnabled(true);
				  System.out.println("Frpm 3");			       
			        txtVSPNO.requestFocus();
			        
			    }
			}
		     if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst ))
		     {
		         	tbpMAIN.setEnabled(true);
		         	tblVSTAUT.cmpEDITR[TB1_CHKFL].setEnabled(true);
		         	tblVSTAUT.cmpEDITR[TB1_CLRBY].setEnabled(true);
		         	tblVSTAUT.cmpEDITR[TB1_REMDS].setEnabled(true);
		         	
		         	
		         	if(tbpMAIN.getSelectedIndex() == 1)
				{
				    tblVSTAUT.clrTABLE();
				    getINVST();
				}
			}
			
		 }catch(Exception L_EX)
		 {
		     setMSG(L_EX,"This is actionPerformed");
		 }
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);	
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{
			    if(M_objSOURC == txtVSPNO)		
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVSPNO";
					String L_ARRHDR[] = {"Visitor No","Visitor Name","Date"};
					M_strSQLQRY = "Select VS_VSPNO,VS_VSTNM,VS_VSTDT from HR_VSTRN ";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					M_strSQLQRY += " where VS_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and (VS_STSFL='1' or VS_STSFL= '2' or VS_STSFL= '3') and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst) )  )
					M_strSQLQRY += " where VS_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and (VS_STSFL='1' or VS_STSFL= '2' or VS_STSFL= '3'  or VS_STSFL = '4' or VS_STSFL ='5') and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst ))
					    M_strSQLQRY +=  " where  isnull(VS_STSFL,'') <> 'X'   and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'  ";
					if(txtVSPNO.getText().trim().length() >0)
					    M_strSQLQRY += " AND VS_VSPNO like '"+txtVSPNO.getText().trim() +"%'";
					M_strSQLQRY += " and VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' order by  VS_VSPNO DESC";
					System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
				} 
			    if(M_objSOURC==txtPERVS)
			    {
			        cl_dat.M_flgHELPFL_pbst = true;
			        M_strHLPFLD="txtPERVS";
			        String L_ARRHDR[] ={"Emp Name","Department name","Department Code","Emp. No."};
			        M_strSQLQRY = "select SUBSTRING(EP_FSTNM,1,1) + ' ' + SUBSTRING(EP_MDLNM,1,1) +  ' ' + EP_LSTNM , EP_DPTCD,EP_DPTNM,EP_EMPNO FROM HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_LFTDT is null and SUBSTRING(EP_FSTNM,1,1) + ' ' + SUBSTRING(EP_MDLNM,1,1) + ' ' + EP_LSTNM like '"+txtPERVS.getText().toUpperCase()+"%' ";
			        cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
			    }
			    if(M_objSOURC==txtVSARA)
			    {
			        cl_dat.M_flgHELPFL_pbst = true;
			        M_strHLPFLD="txtVSARA";
			        String L_ARRHDR[] ={"Area","Description"};
			        M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXVSA'  ";
					cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
			        
			    }
			}
		}
		catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is Key Pressed");
		}
	}
	void exeHLPOK()
	{
	    super.exeHLPOK();
	    if(M_strHLPFLD.equals("txtVSPNO"))
		{
			txtVSPNO.setText(cl_dat.M_strHLPSTR_pbst);		
		}
	    if(M_strHLPFLD.equals("txtPERVS"))
	    {
	        strPERVS =String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim() ;
	        
	        txtPERVS.setText(strPERVS);
	        
	        strDEPTNM=String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)).trim();
	        lblEMPNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),3)).trim());
	        txtDPTNM.setText(strDEPTNM);
	    }
	    if(M_strHLPFLD.equals("txtVSARA"))
	    {
	        txtVSARA.setText(cl_dat.M_strHLPSTR_pbst );
	    }
	}
	private boolean vldVSARA()
	{
	    try{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'HRXXVSA'  and  CMT_CODDS='"+txtVSARA.getText().toString().trim()+"'";
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
	public void mouseClicked(MouseEvent L_ME)
	{
	  try
	  {
	    if(L_ME.getSource().equals(tbpMAIN))
   		{
    		if(tbpMAIN.getSelectedIndex() == 0)
			{

    		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))						
    		    {
    		        txtVSTDT.setText(cl_dat.M_strLOGDT_pbst);
    		        txtVSTNM.requestFocus();
    		    }
    		    else
    		        txtVSPNO.requestFocus();
    		    
			}
    		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst ))
		    {
		        tbpMAIN.setEnabled( true);
		        tblVSTAUT.clrTABLE();
		        getINVST();
		        
		        
		    }
		}
	  }
	  catch(Exception L_EX)
	  {
	     setMSG(L_EX,"This is Mouse Licked");
	  }
	  
		
	}
/*	private boolean vldPERVS()
	{
		try{
		    M_strSQLQRY = "select * FROM HR_EPMST where EP_LFTDT is null and SUBSTRING(EP_FSTNM,1,1)+' ' + SUBSTRING(EP_MDLNM,1,1)+ ' ' + EP_LSTNM)='"+txtPERVS.getText().toString().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception L_EX){
			System.out.println("L_EX..vldPERVS ....."+L_EX);							   
		}
		return false;
	}*/
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
		    if(L_FE.getSource().equals(txtVSPNO))
		    {
		     setMSG("Please press F1 Display the Screen",'N');   
		    }
			if(L_FE.getSource().equals(txtVSTNM))
			{
			    setMSG("Please enter Name of the Guest",'N');
			}
			if(L_FE.getSource() .equals(txtVSORG))
			{
			    setMSG("Please Enter the Visitor's Organization",'N');
			}
			if(L_FE.getSource() .equals(txtVSTCT))
		      {
			    
			    setMSG("Please Enter No.of Guests ",'N');
		      }
			if(L_FE.getSource() .equals(txtPERVS))
			{
			    setMSG("Please Press F1 TO Display the Screen",'N');
			}
			if(L_FE.getSource() .equals(txtVSTDT))
			{
			    setMSG("Please Enter Visitor Date",'N');
			}
			if(L_FE.getSource() .equals(txtPURPS))
			{
			    setMSG("Please Enter Purpose of Visitor",'N');
			}
			if(L_FE.getSource() .equals(txtVSARA))
			{
			    setMSG("Please Enter the Area ",'N');
			}
			if(L_FE.getSource() .equals(txtVLDTM))
			{
			    setMSG("Please Enter the  Date",'N');
			}
			if(L_FE.getSource() .equals(txtVLDTM1))
			{
			    setMSG("Please Enter the Time",'N');
			}
			
			if(L_FE.getSource() .equals(txtREMDS))
			{
			    setMSG("Please Enter the Remarks",'N');
			}
			
			if(L_FE.getSource() .equals(tblVSTAUT))
			{
			    setMSG("please Select the Table Check field and press Authorize Button",'N');
			    tblVSTAUT.setValueAt(new Boolean(true),tblVSTAUT.getSelectedRow(),TB1_CHKFL);
			}
			
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"This is focus Gained");
		}
	}
	
	
	private void getDATA()
	{        
		try  
		{    
			int L_ROWNO = 0;
			java.sql.Timestamp L_tmpTIME,L_tmpTIME1;
			java.sql.Date L_tmpDATE;
			String L_strSQLQRY ="";
			ResultSet L_rstRSSET;
			String L_strTEMP="",L_strTVSPNO="";
			//if(jtpMANTAB.getSelectedIndex() == 0)
			//{
				M_strSQLQRY = "Select * from co_cdtrn ,HR_VSTRN LEFT OUTER JOIN HR_RMMST ON  VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND RM_TRNTP ='HC' and RM_TRNCD ='01' ";
			//	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				M_strSQLQRY += " WHERE cmt_cgmtp ='STS' AND CMT_CGSTP ='HRXXVGP' AND CMT_CODCD = VS_STSFL ";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			   	M_strSQLQRY += " AND VS_STSFL = '1' and VS_VSOTM IS NULL and VS_VSTTP='01' and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst  ))
				M_strSQLQRY += " AND (VS_STSFL ='1' or VS_STSFL = '2' or VS_STSFL ='3' or VS_STSFL = '4' or VS_STSFL = '5') and VS_VSOTM IS NULL and VS_VSTTP='01' and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst  ))
				//M_strSQLQRY += " AND isnull(VS_STSFL, '') <>'X' and  VS_VSOTM IS NULL and VS_VSTTP='01' and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
				M_strSQLQRY += " AND isnull(VS_STSFL, '') <>'X' and VS_VSTTP='01'and VS_VSCAT='"+cmbVSCAT.getSelectedItem().toString().substring(0,2)+"' ";

			      M_strSQLQRY+= " AND VS_VSPNO ='"+txtVSPNO.getText().trim() +"'";
				M_strSQLQRY+= " order by VS_VSPNO ";
				intVSCNT =0;
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				System.out.println("getDATA Query" +M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
				while(M_rstRSSET.next())
				{
					L_tmpDATE = M_rstRSSET.getDate("VS_VSTDT");
		 			L_strTEMP="";
		 			if (L_tmpDATE != null)
						L_strTEMP = M_fmtLCDAT.format(L_tmpDATE);
		 			txtVSTDT.setText(L_strTEMP);
		 			txtVSTNM.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""));
					txtVSORG.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),""));
					lblEMPNO.setText(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""));
					txtPERVS.setText(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""));
					lblEMPNO.setText(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""));
					strPERVS = nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),"");
					txtPURPS.setText(nvlSTRVL(M_rstRSSET.getString("VS_PURPS"),""));
					txtVSTCT.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSTCT"),""));
					txtVSARA.setText(nvlSTRVL(M_rstRSSET.getString("VS_VSARA"),""));
					txtARADS.setText(nvlSTRVL(M_rstRSSET.getString("VS_ARADS"),""));
					txtDPTNM.setText(nvlSTRVL(M_rstRSSET.getString("VS_DPTNM"),""));
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
					txtVEHNO.setText(nvlSTRVL(M_rstRSSET.getString("VS_VEHNO"),""));
					//rdbMTNG1.setSelected(M_rstRSSET.getString("LG_MTGFL").equals("Y") ? true : false);
					if(M_rstRSSET.getString("VS_MOBFL").equals("Y"))
						rdbMOBFLY.setSelected(true);
					else rdbMOBFLN.setSelected(true);
					if(M_rstRSSET.getString("VS_CMRFL").equals("Y"))
						rdbCMRFLY.setSelected(true);
					else rdbCMRFLN.setSelected(true);
					//rdbCMRFLY.setSelected(M_rstRSSET.getString("VS_CMRFL").equals("Y") ? true : false);
					strCODDS =M_rstRSSET.getString("cmt_codds");
					L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
					L_tmpTIME1 = M_rstRSSET.getTimestamp("VS_VLDTM");
					if (L_tmpTIME1 != null)
					{
					txtVLDTM.setText(M_fmtLCDTM.format(L_tmpTIME1).substring(0,10));
				      txtVLDTM1.setText(M_fmtLCDTM.format(L_tmpTIME1).substring(11));
            			}
					L_strTEMP="";
					if (L_tmpTIME != null)
					{
						L_strTEMP = M_fmtLCDTM.format(L_tmpTIME).substring(11);
						txtVSITM.setText(L_strTEMP);
					}
					lblSTSFL.setText(strCODDS);
				    
					L_ROWNO ++;
				}
				if(L_ROWNO ==0)
				{
				    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				    {
				        
				        setMSG("Not Available for Modification ..",'E');
				        txtVSPNO.requestFocus();
 					  return;						
				    }   
				}
				 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				 {
						setMSG("Not Available for Deletion ..",'E');
						txtVSPNO.requestFocus();
				 }		
					M_rstRSSET.close();
				}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
		}
	}//visitors........
	
	public void exeSAVE()
	{
	    try
	    {
	       if(vldVISTOR())
	       {
			cl_dat.M_flgLCUPD_pbst = true;    
	        exeADDREC();
	        
	        
		        if(tbpMAIN.getSelectedIndex() == 1)
				{
		            String M_strSQLQRY1="",M_strSQLQRY2="";
					int a=0,b=0,c=0;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)) 
					{
						this.setCursor(cl_dat.M_curWTSTS_pbst);
						cl_dat.M_flgLCUPD_pbst = true;
	    				for(int i=0;i<tblVSTAUT.getRowCount();i++)
	    				{
	    					if(tblVSTAUT.getValueAt(i,TB1_CHKFL).toString().trim().equals("true"))
	    					{
									
																
									M_strSQLQRY ="UPDATE HR_VSTRN SET ";
	    							M_strSQLQRY += "VS_VSTNM = " + "'" + tblVSTAUT.getValueAt(i,TB1_VSTNM).toString().trim() +"',";
	    							M_strSQLQRY += "VS_VSORG = " + "'" + tblVSTAUT.getValueAt(i,TB1_VSORG).toString().trim() +"',";
	    							M_strSQLQRY += "VS_VSTCT =" + "'"+tblVSTAUT.getValueAt(i,TB1_VSTCT).toString().trim() +"'," ;
	    							M_strSQLQRY += "VS_PERVS = "+ "'"+tblVSTAUT.getValueAt(i,TB1_PERVS).toString().trim() +"',";
	    							M_strSQLQRY += "VS_EMPNO = "+ "'"+tblVSTAUT.getValueAt(i,TB1_EMPNO).toString().trim() +"',";
	    						    M_strSQLQRY += "VS_PURPS ="+ "'"+tblVSTAUT.getValueAt(i,TB1_PURPS).toString().trim() +"',";
	    						    M_strSQLQRY += "VS_CLRBY ="+ "'"+tblVSTAUT.getValueAt(i,TB1_CLRBY).toString().trim() +"',";
	    						    M_strSQLQRY += "VS_VSITM ="+ "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblVSTAUT.getValueAt(i,TB1_VSTDT).toString().trim() ))+"',";
	    						    M_strSQLQRY += "VS_VLDTM ="+ "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(tblVSTAUT.getValueAt(i,TB1_VLDTM).toString().trim() ))+"',";
	    						    M_strSQLQRY += "VS_VSARA ="+ "'"+tblVSTAUT.getValueAt(i,TB1_VSARA).toString().trim() +"',";
	    							M_strSQLQRY += "VS_LUSBY = " + "'" + cl_dat.M_strUSRCD_pbst+"',";
	    							M_strSQLQRY += "VS_STSFL = '4',";
	    							M_strSQLQRY += "VS_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"',";
	    							M_strSQLQRY += "VS_VSTTP = '01'";
	    							M_strSQLQRY += " WHERE  ";
	    							M_strSQLQRY += " VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSPNO = " + "'" + tblVSTAUT.getValueAt(i,TB1_VSPNO).toString().trim()+"'";
	    							System.out.println(M_strSQLQRY);
	    							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	    							if(tblVSTAUT.getValueAt(i,TB1_REMDS).toString().trim().length() >0)
	    			                {
	    								M_strSQLQRY ="UPDATE HR_RMMST SET ";
	    								M_strSQLQRY += "RM_REMDS = " + "'" + tblVSTAUT.getValueAt(i,TB1_REMDS).toString().trim() +"'";
	    								M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='01' and RM_DOCNO = '" + tblVSTAUT.getValueAt(i,TB1_VSPNO).toString().trim()+"'";
	    								cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	    			                }
	    							
								}
	    				}
	    				this.setCursor(cl_dat.M_curDFSTS_pbst);
	    				System.out.println(M_strSQLQRY);
				}
					tblVSTAUT.clrTABLE() ;
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							setMSG(" Data Authorised & Saved Successfully..",'N'); 
					}	
						
				}
	       }   
	        
	    }catch(Exception L_EX)
	    {
	        setMSG(L_EX, "This is exeSAVE");
	    }
	    
	    
	    
	}
	
	
	
	private void exeADDREC()
	{
		try
		{
			String L_strVSPNO  = "",  L_strCODCD = "",L_strCCSVL="";
	 		if(tbpMAIN.getSelectedIndex() == 0)
			{
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) 
				{
				    cl_dat.M_flgLCUPD_pbst = true;
				    this.setCursor(cl_dat.M_curWTSTS_pbst);
				    M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
    				M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'HRXXVST' and ";
    				M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3)+"01"+ "'";
    				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
    				if(M_rstRSSET != null)
    				{
    					if(M_rstRSSET.next())
    					{
    						L_strCODCD = M_rstRSSET.getString("CMT_CODCD");
    						L_strCCSVL = M_rstRSSET.getString("CMT_CCSVL");
    						M_rstRSSET.close();
    					}
    				}
    				L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
    				for(int i=L_strCCSVL.length(); i<5; i++)				
    					L_strVSPNO += "0";
    			
    				L_strCCSVL = L_strVSPNO + L_strCCSVL;
    				L_strVSPNO = L_strCODCD + L_strCCSVL;//code generations 
    				System.out.println("This is pass no "  +L_strVSPNO);
    				//strSTSFL ="'0',";
                    M_strSQLQRY ="INSERT INTO HR_VSTRN(VS_CMPCD,VS_VSPNO,VS_VSCAT,VS_VSTNM,VS_VSORG,VS_VSTCT,VS_PERVS,VS_EMPNO,VS_PURPS,VS_VSTDT,VS_VLDTM,VS_VSARA,VS_ARADS,VS_VSITM,VS_LUSBY,VS_STSFL,VS_DPTNM,VS_VEHNO,VS_VSTTP,VS_SBSCD,VS_MOBFL,VS_CMRFL,VS_REQBY)";
					M_strSQLQRY +="Values(";
					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
					M_strSQLQRY += "'"+L_strVSPNO+"',";
					M_strSQLQRY += "'" +cmbVSCAT.getSelectedItem().toString().substring(0,2)+"',";
					M_strSQLQRY += "'"+txtVSTNM.getText() .toString() .trim() + "',";
					M_strSQLQRY += "'" +txtVSORG.getText() .toString() .trim() + "',";
					M_strSQLQRY += "'" +txtVSTCT.getText() .toString() .trim() + "',";
					M_strSQLQRY += " '" +txtPERVS.getText() .toString() .trim() + " ',";
					M_strSQLQRY += " '" +lblEMPNO.getText() .toString() .trim() + " ',";
					
				//	if(txtDPTNM.getText() .trim().length()  <5)
					//M_strSQLQRY += " ' " +txtDPTNM.getText() .toString() .trim() + " ',";
					M_strSQLQRY += " ' "+txtPURPS.getText() .toString() .trim() + "',";
					M_strSQLQRY += "'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVSTDT.getText().toString().trim())) + "',";//text date
					M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtVLDTM.getText().toString().trim()+ " "+ txtVLDTM1.getText() .toString().trim()))+ "',";
					M_strSQLQRY += "'" +txtVSARA.getText().toString() .trim() +"',"; 
					M_strSQLQRY += "'" +txtARADS.getText().toString() .trim() +"',"; 
					M_strSQLQRY += "'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtVSTDT.getText().toString().trim()+ " "+ txtVSITM.getText() .toString().trim()))+ "',";
					M_strSQLQRY += "'" + cl_dat.M_strUSRCD_pbst +"',";
					M_strSQLQRY += "'1',"; 
					M_strSQLQRY += " '" +txtDPTNM.getText() .toString() .trim()+"',";
					M_strSQLQRY += " '" +txtVEHNO.getText() .toString() .trim()+"',";
					M_strSQLQRY += " '01',";
					M_strSQLQRY += "'"+M_strSBSCD+"',";
					M_strSQLQRY += "'"+(rdbMOBFLY.isSelected() ? "Y" : "N")+"',";
					M_strSQLQRY += "'"+(rdbCMRFLY.isSelected() ? "Y" : "N")+"',";
					M_strSQLQRY += "'"+cl_dat.M_strUSRCD_pbst+"')";
					
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					//System.out.println("inset into   " +M_strSQLQRY);
					 if((txtREMDS.getText().trim().length() > 0)&& cl_dat.M_flgLCUPD_pbst )
	                   	{
	                        M_strSQLQRY="INSERT INTO HR_RMMST(RM_CMPCD,RM_LINNO,RM_SBSCD,RM_REMDS,RM_TRNTP,RM_TRNCD,RM_DOCNO)Values(";
	    					M_strSQLQRY += "'"+cl_dat.M_strCMPCD_pbst+"',";
							M_strSQLQRY +="'1',";
	    					M_strSQLQRY += "'"+M_strSBSCD+"',";
	    					M_strSQLQRY += "'" + txtREMDS.getText().trim() +"',";
	    					M_strSQLQRY += "'HC',";
	    					M_strSQLQRY += "'01',";
	    					M_strSQLQRY += "'"+L_strVSPNO+"')";
	    					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	                   }
					//System.out.println("inset into   " +M_strSQLQRY);
					if(cl_dat.M_flgLCUPD_pbst)
	                   {
	                        M_strSQLQRY ="UPDATE CO_CDTRN SET ";
	        				M_strSQLQRY +="CMT_CCSVL ='"+L_strVSPNO.substring(3)+"'";
	        				M_strSQLQRY +=" where CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'HRXXVST' and CMT_CODCD= '" + cl_dat.M_strFNNYR_pbst.substring(3)+"01"+ "'";
	        				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	                   }
				}
			    else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) 
				{
			        M_strSQLQRY ="UPDATE HR_VSTRN SET ";
			        M_strSQLQRY +="VS_VSCAT=" +"'" +cmbVSCAT.getSelectedItem().toString().substring(0,2)+"',";
			        M_strSQLQRY +="VS_VSTNM =" + "'"+txtVSTNM.getText() .toString() .trim() + "',";
			        M_strSQLQRY += "VS_VSORG="+ "'" +txtVSORG.getText() .toString() .trim() + "',";
			        M_strSQLQRY += "VS_VSTCT=" +"'" +txtVSTCT.getText() .toString() .trim() + "',";
			        M_strSQLQRY += "VS_PERVS= " +" '" +txtPERVS.getText() .toString() .trim() + " ',";
			        M_strSQLQRY += "VS_EMPNO= " +" '" +lblEMPNO.getText() .toString() .trim() + " ',";
			        
			       // if(txtDPTNM.getText() .trim().length()  <=5)
			       // M_strSQLQRY += "VS_CLRBY=" +" ' " +txtDPTNM.getText() .toString() .trim() + " ',";
			        M_strSQLQRY += "VS_PURPS =" +" ' "+txtPURPS.getText() .toString() .trim() + "',";
					M_strSQLQRY += "VS_VSTDT =" +"'" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtVSTDT.getText().toString().trim()))+"',";//text date
					M_strSQLQRY += "VS_VSITM =" +"'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtVSTDT.getText().toString().trim()+ " "+ txtVSITM.getText() .toString().trim()))+ "',";
					M_strSQLQRY += "VS_VLDTM =" +"'" + M_fmtDBDTM.format(M_fmtLCDTM.parse(txtVLDTM.getText().toString().trim()+ " "+ txtVLDTM1.getText() .toString().trim()))+ "',";
					M_strSQLQRY += "VS_VSARA= " +"'" +txtVSARA.getText().toString() .trim() +"',";
					M_strSQLQRY += "VS_ARADS= " +"'" +txtARADS.getText().toString() .trim() +"',";
					M_strSQLQRY += "VS_DPTNM =" + "'" +txtDPTNM.getText().toString() .trim() +"',"; 
					M_strSQLQRY += "VS_VEHNO =" + "'" +txtVEHNO.getText().toString() .trim() +"',"; 
					M_strSQLQRY += "VS_LUSBY = " +"'" + cl_dat.M_strUSRCD_pbst +"',";
					
					M_strSQLQRY += "VS_SBSCD = " + "'" +M_strSBSCD+"',";
					M_strSQLQRY += "VS_VSTTP = '01',";
					
					M_strSQLQRY += "VS_STSFL= '1 ',"; 
					M_strSQLQRY += "VS_MOBFL = " + "'"+(rdbMOBFLY.isSelected() ? "Y" : "N")+"',";
					M_strSQLQRY += "VS_CMRFL = " + "'"+(rdbCMRFLY.isSelected() ? "Y" : "N")+"',";
					M_strSQLQRY += "VS_REQBY = " + "'"+cl_dat.M_strUSRCD_pbst+"'";
					M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSPNO = " + "'" + txtVSPNO.getText().trim()+"'";
					M_strSQLQRY += " and VS_VSCAT = " + "'" + cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					System.out.println(M_strSQLQRY);
					if(txtREMDS.getText().trim().length() >0 )
                   		 {
						M_strSQLQRY ="UPDATE HR_RMMST SET ";
						M_strSQLQRY += "RM_REMDS = " + "'" + txtREMDS.getText().trim() +"'";
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+M_strSBSCD+"' and RM_TRNTP ='HC' and RM_TRNCD ='01' and RM_DOCNO = '" + txtVSPNO.getText().trim()+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			       	 }
				}
			
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))
			{
			    M_strSQLQRY ="UPDATE HR_VSTRN SET ";
			    M_strSQLQRY += getUSGDTL("VS",'U',"X");
			    M_strSQLQRY += " WHERE VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_VSTTP = '01' and VS_SBSCD = '"+M_strSBSCD+"'";
				M_strSQLQRY += " and VS_VSPNO = " + "'" + txtVSPNO.getText().trim()+"'";
				M_strSQLQRY += " and VS_VSCAT = " + "'" + cmbVSCAT.getSelectedItem().toString().substring(0,2)+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				
				System.out.println(M_strSQLQRY);
			    
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{   
    				    lblSTSFL1.setText("");
				    setMSG(" Data Saved Successfully..",'N');
				    JOptionPane.showMessageDialog(this,"Visitor Gate Pass No. is " + L_strVSPNO,"Visitor Gate Pass No.",JOptionPane.INFORMATION_MESSAGE); 
				    clrCOMP();
					rdbMOBFLN.setSelected(true);
					rdbCMRFLN.setSelected(true);
				}	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{   
				    clrCOMP();
  				    lblSTSFL1.setText("");
				    setMSG(" Data Modified Successfully..",'N');
				    rdbMOBFLN.setSelected(true);
					rdbCMRFLN.setSelected(true);
				}	
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))
				{    
				    clrCOMP();
				    lblSTSFL1.setText("");
				    setMSG("Data Deletion Sucessfully...",'N');
					rdbMOBFLN.setSelected(true);
					rdbCMRFLN.setSelected(true);
				} 
			}
			else	
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				   setMSG("Error in Modified Data details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst ))
					 setMSG("Error in Deletion ...",'N');  
			}	
			
			}catch(Exception L_EX)
			{
			    setMSG(L_EX,"Exe AddREC");
			}
	
	}
	/** Method for Getting All Records from Table1. */
	private void getINVST()
	{        
		try  
		{    
			int L_ROWNO = 0;
			java.sql.Timestamp L_tmpTIME;
			java.sql.Date L_tmpDATE;
			String L_strSQLQRY ="";
			ResultSet L_rstRSSET;
			String L_strTEMP="",L_strTVSPNO="",L_strTEMP1="";
		 	
			
			if(tbpMAIN.getSelectedIndex() == 1)
			{
			    M_strSQLQRY ="Select * from HR_VSTRN LEFT OUTER JOIN HR_RMMST ON  VS_CMPCD = RM_CMPCD and VS_VSTTP = RM_TRNCD AND VS_VSPNO = RM_DOCNO AND RM_TRNTP ='HC' and RM_TRNCD ='01' ";
			    M_strSQLQRY += " where VS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND VS_STSFL = '1' and VS_VSOTM IS NULL and VS_VSTTP='01' order by VS_VSPNO";
			    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			    System.out.println(M_strSQLQRY);
			    if(M_rstRSSET !=null)
				{
					while(M_rstRSSET.next())
					{
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSCAT"),""),L_ROWNO,TB1_VSCAT);
					    
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSPNO"),""),L_ROWNO,TB1_VSPNO);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTNM"),""),L_ROWNO,TB1_VSTNM);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSORG"),""),L_ROWNO,TB1_VSORG);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTCT"),""),L_ROWNO,TB1_VSTCT);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PERVS"),""),L_ROWNO,TB1_PERVS);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_EMPNO"),""),L_ROWNO,TB1_EMPNO);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_CLRBY"),""),L_ROWNO,TB1_CLRBY);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSTDT"),""),L_ROWNO,TB1_VSTDT);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_PURPS"),""),L_ROWNO,TB1_PURPS);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("VS_VSARA"),""),L_ROWNO,TB1_VSARA);
					    tblVSTAUT.setValueAt(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""),L_ROWNO,TB1_REMDS);
					    Timestamp L_tmpTIME1 = M_rstRSSET.getTimestamp("VS_VLDTM");
					    if (L_tmpTIME1 != null)
	    				{
					        L_strTEMP1 = M_fmtLCDTM.format(L_tmpTIME1);
					        tblVSTAUT.setValueAt(L_strTEMP1,L_ROWNO,TB1_VLDTM);
	    				}
					    L_tmpTIME = M_rstRSSET.getTimestamp("VS_VSITM");
	    				L_strTEMP="";
	    				if (L_tmpTIME != null)
	    				{
	    					L_strTEMP = M_fmtLCDTM.format(L_tmpTIME);
	    					tblVSTAUT.setValueAt(L_strTEMP,L_ROWNO,TB1_VSTDT);
	    				}
					    L_ROWNO ++;    
					    
					}
					M_rstRSSET.close();
				}	
				    
			    
			}
			    
			
		}catch(Exception L_EX)
		{
		    setMSG(L_EX,"getINVAST");
		}
	}
	boolean vldVISTOR()
	{
		try
		{
		    if(tbpMAIN.getSelectedIndex() == 0)
			{
		        if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
				{
					/*if(M_fmtLCDAT.parse(txtVSTDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
					{
						setMSG(" Date should not be greater than current Date..",'E');
						txtVSTDT.requestFocus();
						return false;
					}*/
					if(txtVSTNM.getText().length() ==0)
				    {
				        setMSG("Visitor Name can not be blank..",'E');
						txtVSTNM.requestFocus();
				        return false;         
				    }
					if(txtVSORG.getText().length() ==0)
				    {
				        setMSG("Please Enter Organization..",'E');
						txtVSORG.requestFocus();
				        return false;         
				    }
					if(txtVSTCT.getText().length() ==0)
				    {
				        setMSG("Please Enter No of Guests..",'E');
						txtVSTCT.requestFocus();
				        return false;         
				    }
					if(txtPERVS.getText().length() ==0)
				    {
				        setMSG("Please Enter Person To Be Visited..",'E');
						txtPERVS.requestFocus();
				        return false;         
				    }
					if(txtVSORG.getText().length() ==0)
				    {
				        setMSG("Please Enter Organization..",'E');
						txtVSORG.requestFocus();
				        return false;         
				    }
					if(txtVSTDT.getText().length() ==0)
				    {
				        setMSG("Visit Date can not be blank..",'E');
						txtVSTDT.requestFocus();
				        return false;         
				    }
					if(txtVLDTM.getText().trim().length() == 0)
					{
						setMSG("Enter Valid Date..",'E');
						txtVLDTM.requestFocus();
						return false;
					}
					/*if(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst ).compareTo(M_fmtLCDAT.parse(txtVLDTM.getText().trim()))>0)
					{
						setMSG(" Date should not be greater than current Date..",'E');
						txtVLDTM.requestFocus();
						return false;
					}*/
					if(txtVLDTM1.getText().trim().length() == 0)
					{
						setMSG("Enter Time..",'E');
						txtVLDTM1.requestFocus();
						return false;
					}
					if(txtPURPS.getText().length() ==0)
				    {
				        setMSG("Please Enter Purpose..",'E');
						txtPURPS.requestFocus();
				        return false;         
				    }
					if(txtVSARA.getText().length() ==0)
				    {
				        setMSG("Please Enter Area of Visit..",'E');
						txtVSARA.requestFocus();
				        return false;         
				    }
					if(txtARADS.getText().length() ==0)
				    {
				        setMSG("Please Enter Area of Visit..",'E');
						txtARADS.requestFocus();
				        return false;         
				    }
				}
		        
			}
		    if(tbpMAIN.getSelectedIndex() == 1)
			{
		        if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
				{
			
				    boolean L_flgCHKFL= false;
					for(int i=0; i<tblVSTAUT.getRowCount(); i++)
					{				
						if(tblVSTAUT.getValueAt(i,TB1_CHKFL).toString().equals("true"))
						{				
							L_flgCHKFL= true;
							break;
						}	
					}	
					if(L_flgCHKFL== false)
					{
						setMSG("No row Selected..",'E');				
						return false;
					}
					
					
					
				}	
			}
		    
		    
		}catch(Exception L_EX)
		{setMSG(L_EX,"This is vld Visitore");}
		return true;
	}	
	
class INPVF extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		try
		{
			if(((JTextField)input).getText().length() == 0)
				return true;			
			if(input == txtVLDTM)
			{
				if(M_fmtLCDAT.parse(txtVSTDT.getText()).compareTo(M_fmtLCDAT.parse(txtVLDTM.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
					return false;
				}
			}
			if(input == txtVLDTM1)
			{
				System.out.println(M_fmtLCDTM.parse(txtVSTDT.getText()+" "+txtVSITM.getText()));
				System.out.println(M_fmtLCDTM.parse(txtVLDTM.getText()+" "+txtVLDTM1.getText()));
				if(M_fmtLCDTM.parse(txtVSTDT.getText()+" "+txtVSITM.getText()).compareTo(M_fmtLCDTM.parse(txtVLDTM.getText()+" "+txtVLDTM1.getText()))>0)
				{
					setMSG("Invalid Date Range..",'E');
					return false;
				}
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
	
	
}	
	
	