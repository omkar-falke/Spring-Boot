/*
System Name   : Material Management System
Program Name  : Stock Status (TankFarm System)
Program Desc. : Generates Report For selected Memo Type & Memo Number.
Author        : Mr.S.R.Mehesare
Date          : 28th jan 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
*/

import java.awt.*;
import java.sql.*;
import javax.swing.JTextField;import javax.swing.JComboBox;import javax.swing.JLabel;
import java.awt.event.KeyEvent; import java.awt.event.ActionEvent;
import java.io.DataOutputStream; import java.io.FileOutputStream; 
/**<pre>
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Stock Status Report

<b>Purpose :</b> This module provides information of Dip register for given Memo No. & Memo Type

<b>Bussiness Logic :</b> For checking the Stock of Liquid Raw Material stored in Tanks, Dip is taken
every day at 07:00 hrs and a memo number is generated.This report gives information about the 
Stock status. 					  

List of tables used :
Table Name    Primary key                             Operation done
                                                Insert  Update  Query  Delete	
-------------------------------------------------------------------------------
MM_DPTRN      DP_MEMTP,DP_MEMNO,DP_TNKNO                          #	     
MM_RMMST      RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO                 #
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD                       #		   
CO_CTMST      CT_MATCD                                            #
-------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name      Table Name         Type/Size         Description
-------------------------------------------------------------------------------
cmbMEMTP    DP_MEMTP         MM_DPTRN           Varchar(2)        Memo Type
txtMEMNO    DP_MEMNO         MM_DPTRN           Varchar(8)        Memo No.
txtMEMDT    DP_MEMDT         MM_DPTRN           Timestamp         Memo Date
-------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description     Display Columns         Table Name
-------------------------------------------------------------------------------
txtMEMNO    Memo No,Memo Date       DP_MEMNO,DP_MEMDT       MM_DPTRN
-------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b>1)MM_DPTRN
                       2)CO_CTMST
<B>Conditions Give in Query:</b>
     Data is taken from MM_DPTRN for selected Memo Type & given Memo Number.Material Description and
     UOM is taken from Catalog Master(CO_CTMST).
             1)DP_MATCD = CT_MATCD
             2)and DP_MEMTP = selected Memo type
             3)and DP_MEMNO = given Memo number
</I>
Validations :
	1) Memo Type should be in following list
             81 - Regular
             91 - Adhoc
	2) Memo Number should exists in Dip Register for given Memo Type.	
 */

class mm_rpdpt extends cl_rbase
{										/** JComboBox for accepting Memo Type.*/
	private JComboBox cmbMEMTP;         /** JTextFieldBox for accepting Memo Number.*/
	private JTextField txtMEMNO;        /** JTextField for displaying Memo Date.*/
	private JTextField txtMEMDT;        /** String variable for Memo Type.*/
	private String strMEMTP;            /** String variable for Memo Number.*/
	private String strMEMNO;            /** Integer variable for counting Retrived Records.*/
	private int intRECCT;	            /** FileOutputStream object for file Oprations.*/
	private FileOutputStream fosREPORT; /** DataOutputStream object to store Data to generate Report File.*/
    private DataOutputStream dosREPORT; /** String variable to specify the Location for Reports.*/
    private String strRPLOC=cl_dat.M_strREPSTR_pbst;/** String variable for Report File Name.*/
    private String strFILNM ;           /** String variable for Memo Date.*/
	private String strMEMDT;            /** String variable for Tank No.*/
	private String strTNKNO;            /** String variable for Material Code.*/
	private String strMATCD;            /** String variable for Material Descripation.*/
	private String strMATDS;            /** String variable for Unit of Measurement.*/
	private String strUOMCD;            /** Float variable for Material Quantity.*/	    
	private float fltMATQT;             /** Float variable for Material Temprature.*/
	private float fltTMPVL;             /** Float variable for Density of the liquid in Tankfarm.*/
	private float fltDENVL;             /** Float variable for depth of the liquid in Tankfarm.*/
	private float fltDIPVL;             			
	mm_rpdpt()
	{
		super(2);
	    try
	    {	
			setCursor(cl_dat.M_curWTSTS_pbst);
		    cmbMEMTP = new JComboBox();
		    cmbMEMTP.addItem("Select");
		    M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN "
		    + "where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXDIP' and " 
		    + "CMT_STSFL <> 'X' order by CMT_CODCD";	
		    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	        
	    	if(M_rstRSSET != null)
		    {
			   	while(M_rstRSSET.next())
			        cmbMEMTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));
			      M_rstRSSET.close();
			}		
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Memo Type"),3,4,1,1,this,'L');
			add(cmbMEMTP,3,5,1,1.2,this,'L');
			add(new JLabel("Memo No."),4,4,1,1,this,'L');
			add(txtMEMNO = new TxtNumLimit(10.0),4,5,1,1.2,this,'L');
			add(new JLabel("Memo Date"),5,4,1,1,this,'L');
			add(txtMEMDT = new TxtDate(),5,5,1,1.2,this,'L');
			setCursor(cl_dat.M_curDFSTS_pbst);
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}	
	mm_rpdpt(String P_strSBSCD)
	{
		//M_strSBSCD = P_strSBDCD;
		M_rdbTEXT.setSelected(true);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
	    /**
	    Action Event Handler for selected destination of the Report
	    */
	    super.actionPerformed(L_AE);	
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
		    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
				setENBL(true);
				txtMEMDT.setEnabled(false);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);
				txtMEMDT.setEnabled(false);
				cmbMEMTP.requestFocus();
			}
		}
		/**
		Action Event handler for enter Key event fo Memo Number text field
		*/
		else if (M_objSOURC == txtMEMNO)
		{
			 try
	  		{
				strMEMTP = cmbMEMTP.getSelectedItem().toString().substring(0,2);
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(cmbMEMTP.getSelectedIndex() > 0)
				{
					if(M_objSOURC == txtMEMNO)
					{
						M_strHLPFLD = "txtMEMNO";						
						M_strSQLQRY = "Select DP_MEMDT from MM_DPTRN Where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMNO = '" + txtMEMNO.getText().trim() + "' AND DP_MEMTP= '"+ cmbMEMTP.getSelectedItem().toString().substring(0,2)+"'";			           						
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					    txtMEMDT.setText("");
						if(M_rstRSSET != null)
	                    {		                     
			    	         if(M_rstRSSET.next())
							 {
			    			     java.sql.Timestamp L_tmsTEMP = M_rstRSSET.getTimestamp("DP_MEMDT");					        
					             if(L_tmsTEMP !=null)
					             {
						            txtMEMDT.setText(M_fmtLCDTM.format(L_tmsTEMP));
       						     }
							 }
							 M_rstRSSET.close();
		                }
		            }				
					setCursor(cl_dat.M_curDFSTS_pbst);					
				}
				else
				{
					setMSG("Please Select Memo Type..",'E');
					cmbMEMTP.requestFocus();
				}
	  		}
	  		catch(Exception L_E)
	  		{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"Key Pressed F1");
	  		}
        }	
		else if(M_objSOURC == cmbMEMTP)
		{
		    if (cmbMEMTP.getSelectedIndex() == 0)
		    {
		       setMSG("Select Memo Type ..",'E');
		       txtMEMNO.setEnabled(false);
		    }
            else
            {
               txtMEMNO.setEnabled(true);
               txtMEMNO.requestFocus();
               setMSG("Enter Memo Number, Press F1 to select from the list ..",'N');
             }
		   txtMEMNO.setText("");
		   txtMEMDT.setText("");
		   strMEMTP = cmbMEMTP.getSelectedItem().toString().substring(0,2);
		   txtMEMDT.setEnabled(false);  
		}
	}	
	public void keyPressed(KeyEvent L_KE)
	{
	    /**
	    Key event handler for Enter key for MEMO Type.
		*/
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbMEMTP)
			{			   
				if(cmbMEMTP.getSelectedIndex() !=0)
				{	
					txtMEMNO.requestFocus();
					setMSG("Enter Memo No, OR Press F1 for Help..",'N');					
				}
				else setMSG("Select The Memo Type..",'E');
			}			
		}		
		/**
		Key event handler for F1 help
		*/
 		if(L_KE.getKeyCode() == L_KE.VK_F1)
 		{
		  	try
		  	{
				strMEMTP = cmbMEMTP.getSelectedItem().toString().substring(0,2);
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(cmbMEMTP.getSelectedIndex() > 0)
				{
					if(M_objSOURC == txtMEMNO)
					{
						M_strHLPFLD = "txtMEMNO";
						M_strSQLQRY = " Select distinct DP_MEMNO,DP_MEMDT from MM_DPTRN";
						M_strSQLQRY += " Where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strMEMTP + "'";
						    if(txtMEMNO.getText().trim().length() > 0)
						        M_strSQLQRY += " AND DP_MEMNO LIKE '"+txtMEMNO.getText().trim()+"%'";
					        M_strSQLQRY +=" AND isnull(DP_STSFL,' ') <>'X' order by DP_MEMNO desc";
					 	cl_hlp(M_strSQLQRY,1,1,new String[]{"Memo No.","Memo Date"},2,"CT");													
					}									
				}
				else
				{
					setMSG("Please Select Memo Type..",'E');
					cmbMEMTP.requestFocus();
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
		  	}
		  	catch(Exception L_E)
		  	{				
				setMSG(L_E,"Key Pressed F1");
			}
		}
   }	
	/**
	Method for execution of help for F1 Key pressed for Memo Number.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtMEMNO")
		{
			txtMEMNO.setText(cl_dat.M_strHLPSTR_pbst);	
			txtMEMDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			if (txtMEMNO.getText().length()==8)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
		}		
	}	
	
    /**
    Method to Print OR Dispalay report depand on selection.
    */
	void exePRINT()
	{
		if(vldDATA())
		{
			try
			{
			    if(M_rdbHTML.isSelected())
					strFILNM = strRPLOC + "mm_rpdpt.html";
				if(M_rdbTEXT.isSelected())
					strFILNM = strRPLOC + "mm_rpdpt.doc";
			    txtMEMDT.setEnabled(false);
			    strMEMTP = cmbMEMTP.getSelectedItem().toString().substring(0,2);
			    strMEMNO = txtMEMNO.getText().trim();
				getALLREC(strMEMTP,strMEMNO);				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT >0)
					    if (M_rdbTEXT.isSelected())
					        doPRINT(strFILNM);
					    else 
                        {    
							Runtime r = Runtime.getRuntime();
							Process p = null;					
							p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
							setMSG("For Printing Select File Menu, then Print  ..",'N');
					    }    
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
				    Runtime r = Runtime.getRuntime();
					Process p = null;
					if(intRECCT >0)
					{	
					    if(M_rdbHTML.isSelected())
					        p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					    else
					        p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
				    }
					else
					    setMSG("No data found..",'E');
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			    {
		    		cl_eml ocl_eml = new cl_eml();				    
				    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				    {
					    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Material Shotrage Status Report"," ");
					    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
					}				    	    	
		    	}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Error.exescrn.. ");
			}
		}
	}
	/**
	Method to validate data, before execution of Query, To check for blank input 
	and wrong selection of Memo Type.
	*/
	boolean vldDATA()
	{
		try
		{
			if(cmbMEMTP.getSelectedIndex() == 0)
			{
				setMSG("Please Select Memo Type..",'E');
				cmbMEMTP.requestFocus();
				return false;
			}
			else if(txtMEMNO.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter Memo No. Or Press F1 For Help.. ",'E');
				txtMEMNO.requestFocus();
				return false;
			}			
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{					
					setMSG("Please Select the Email/s from List by using F1 Help ..",'E');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 				
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'E');
					return false;
				}
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
    /**
	Method to fetch data from table MM_DPTRN with given Memo number(DP_MEMNO). 
	*/
	public void getALLREC(String P_strMEMTP,String P_strMEMNO)
	{ 
	    try
	    {
	        String L_strPRNSTR ="";
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			strMEMDT = txtMEMDT.getText().trim();
			String L_strMATCD ="";			
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Progress.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);		
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Stock Status Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
			//generate the Header part of the report
			dosREPORT.writeBytes("\n\n\n\n\n");
			if (M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<Pre>");
			dosREPORT.writeBytes("SUPREME PETROCHEM LTD.                                    ");
			dosREPORT.writeBytes("Report Date :"+cl_dat.M_strLOGDT_pbst + "\n");				
			dosREPORT.writeBytes("Tankfarm Stock Statement as on " + strMEMDT  + " hrs       ");
			dosREPORT.writeBytes("Page No.    :" + 1 + "\n");// String.valueOf(M_intPAGNO) + "\n");)						
			dosREPORT.writeBytes("Dip Memo No. : " + strMEMNO + "\n");
			//generate the the Header of the Table			
			dosREPORT.writeBytes("---------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("Material                      Tank No.     Dip    Temp  Specific   Quantity  UOM" + "\n");			
			dosREPORT.writeBytes("                                          (Cms)  (Deg)  Gravity" + "\n");						
			dosREPORT.writeBytes("---------------------------------------------------------------------------------\n\n");
		
			M_strSQLQRY = "Select DP_MEMNO,DP_MEMDT,DP_TNKNO,DP_MATCD,DP_DIPVL,DP_TMPVL,DP_DENVL,";
			M_strSQLQRY += "DP_DOPQT,CT_MATDS,CT_UOMCD from MM_DPTRN,CO_CTMST  ";
			M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MATCD = CT_MATCD AND DP_MEMTP = '" + P_strMEMTP  + "'";
			M_strSQLQRY += " and DP_MEMNO = '" + P_strMEMNO  + "'";
			M_strSQLQRY += " order by DP_MEMNO,DP_MATCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			L_strMATCD = "";
			float L_fltTOTQT = 0;		
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
				    L_strPRNSTR ="";
				    intRECCT++;
					strMEMNO = nvlSTRVL(M_rstRSSET.getString("DP_MEMNO"),"");			
					strTNKNO = nvlSTRVL(M_rstRSSET.getString("DP_TNKNO"),"");
					strMATCD = nvlSTRVL(M_rstRSSET.getString("DP_MATCD"),"");
					fltDIPVL = M_rstRSSET.getFloat("DP_DIPVL");
					fltTMPVL = M_rstRSSET.getFloat("DP_TMPVL");
					fltDENVL = M_rstRSSET.getFloat("DP_DENVL");
					fltMATQT = M_rstRSSET.getFloat("DP_DOPQT");
					strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
					strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"");
					if(!L_strMATCD.equals(strMATCD))
					{
						// Print the Materialwise total quantity in stock
						if(!L_strMATCD.equals(""))
						{
							if(intRECCT > 1)
							{							
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
								    prnFMTCHR(dosREPORT,M_strBOLD);										    
								L_strPRNSTR ="";
								dosREPORT.writeBytes("\n");								
								L_strPRNSTR +=(padSTRING('R'," ",57));
								if(M_rdbHTML.isSelected())
				                    L_strPRNSTR +=("<b>");	
					            L_strPRNSTR +=("Total : ");
					            L_strPRNSTR += (padSTRING('L',setNumberFormat(L_fltTOTQT,3),10));
					            if(M_rdbHTML.isSelected())
				                    L_strPRNSTR +=("</b>");
					            dosREPORT.writeBytes (L_strPRNSTR);				            
					            L_strPRNSTR ="";				         						    
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
								    prnFMTCHR(dosREPORT,M_strNOBOLD);			
							}
							intRECCT = 0;
							dosREPORT.writeBytes("\n\n");
							L_fltTOTQT = fltMATQT;
						}
						else
							L_fltTOTQT += fltMATQT;
						
						L_strMATCD = strMATCD;
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						    prnFMTCHR(dosREPORT,M_strBOLD);
				        if(M_rdbHTML.isSelected())
						    dosREPORT.writeBytes("<PRE>");						
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
						    prnFMTCHR(dosREPORT,M_strNOBOLD);
					}
					else
					{						
				        strMATDS="";
					    if(M_rdbHTML.isSelected())
					        dosREPORT.writeBytes("<PRE>");
						L_fltTOTQT += fltMATQT;
					}				
					if(M_rdbHTML.isSelected())
					  L_strPRNSTR ="<b>";			        				
					L_strPRNSTR += (padSTRING('R',strMATDS ,30));
					if(M_rdbHTML.isSelected())
					    L_strPRNSTR +="</b>";				       
					L_strPRNSTR += (padSTRING('R',strTNKNO ,10));
					L_strPRNSTR += (padSTRING('L',String.valueOf(fltDIPVL) ,6));
					L_strPRNSTR += (padSTRING('L',String.valueOf(fltTMPVL),8));
					L_strPRNSTR += (padSTRING('L',setNumberFormat(fltDENVL, 3) ,10));
					L_strPRNSTR += (padSTRING('L',setNumberFormat(fltMATQT, 3) ,11));
					L_strPRNSTR +="  " +strUOMCD;				
					dosREPORT.writeBytes(L_strPRNSTR+"\n");				
					intRECCT++;				
				}
				M_rstRSSET.close();
			}
			// Print the total of quantity of specific item
			if(intRECCT > 0)
			{
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);			
				dosREPORT.writeBytes("\n" + (padSTRING('L'," ",57)));				
				L_strPRNSTR ="";
				if(M_rdbHTML.isSelected())
			       L_strPRNSTR +=("<b>");
				L_strPRNSTR +=("Total : ");
				L_strPRNSTR += (padSTRING('L',setNumberFormat(L_fltTOTQT, 3),10));
				dosREPORT.writeBytes (L_strPRNSTR + "\n");
				if(M_rdbHTML.isSelected())
			        L_strPRNSTR +=("</b>");
				L_strPRNSTR ="";				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				    prnFMTCHR(dosREPORT,M_strNOBOLD);							
			}
			// to generate the footer part of report			
			dosREPORT.writeBytes("---------------------------------------------------------------------------------\n");
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}						
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
		    if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
}
