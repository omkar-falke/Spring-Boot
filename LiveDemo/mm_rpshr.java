/*
System Name   : Material Management System
Program Name  : Tankfarm Daily Activity Report
Program Desc. : Report for Material Shortage for the Material Recieved(Tankfarm).
Author        : Mr S.R.Mehesare
Date          : 11.02.2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;
/**<pre>
<B>System Name :</B> Material Management System.
 
<B>Program Name :</B> Material Shortage Report for the Material Recieved(Tankfarm).

<B>Purpose :</B> This module gives supplier wise Material Shortage statement for
 the given Material between given dates range.

List of tables used :
Table Name    Primary key                            Operation done
                                          Insert   Update   Query   Delete	
-----------------------------------------------------------------------------
MM_GRMST      GR_STRTP,GR_GRNTP,GR_GRNNO                       #	     
CO_CTMST      CT_MATCD                                         #
CO_PTMST      CT_MATCD                                         #
-----------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name   Table name     Type/Size       Description
-----------------------------------------------------------------------------
txtMATCD    CT_MATCD      CO_CTMST       Number(10)      Material Code 
txtMATDS    CT_MATDS      CO_CTMST       Varchar(50)     Material Description
txtVNDCD    PT_PRTCD      CO_PTMST       Varchar(5)      Vendor Code
txtVNDNM    PT_PRTNM      CO_PTMST       Varchar(40)     Vendor Name
txtFRMDT    GR_GRNDT      MM_GRMST       Date            From Date    
txtTORDT    GR_GRNDT      MM_GRMST       Date            To Date           
-----------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)MM_GRMST
                        2)CO_CDTRN                       
                        3)CO_CTMST

<B>Conditions Give in Query:</b>
	  - For a Material code, only those Vendors are shown in the help who has supplied 
        the Materials.
     
      - Data is taken from MM_GRMST & CO_CTMST for following condition.
          1)GR_MATCD = CT_MATCD 
          2)and GR_STRTP = Selcted Store Type Code(Tankfarm).
          3)and if date range is give then data between give date range is fetched.
          4)and if vendor code is given, GR_VENCD = given vendor code.
          5)and if Material code is given, GR_MATCD = given Material code.           
</I>
<b>Validations :</b>
   - Vendor code must be supplier of the given material.
   - Material code is validated with materials recieved from that vendor . 
   - From_Date & To-Date cannot be blank.
   - Dates should not be greater than today.
   - From-Date must be smaller than or equal to To-Date.*/
class mm_rpshr extends cl_rbase
{									    /** JTextfield to accept & display Material Code.*/
    private JTextField txtMATCD;		/** JTextField to Display Material Descripation.*/
    private JTextField txtMATDS;		/** JTextfield to accept & display Vender Code.*/                                 
    private JTextField txtVNDCD;		/** JTextfield to display Vender Name.*/
    private JTextField txtVENNM;		/** JTextfield to enter From-Date to spacify Date Range.*/
    private JTextField txtFMDAT;		/** JTextfield to enter To-Date to spacify date Range.*/
    private JTextField txtTODAT;		/**String variable for Help Field.*/	
    private String strHLPFLD;			/**String variable for Material Code.*/
	private String strMATCD;			/**String variable for Material Description.*/		
	private String strMATDS;			/**String variable for Unit of Measurement.*/	
	private String strUOMCD;			/**String variable for Vender Code.*/	
	private String strVNDCD="";			/**String variable for Vender Name.*/	
	private String strVENNM;			/**String variable for report file Name.*/	
	private String strFILNM;			/**Integer variable to Count the Records fetched from DataBase.*/	
	private int intRECCT;				/**FileOutPutStream object for generate report file handling.*/
	private FileOutputStream fosREPORT; /**DataOutputStream to generate the Report File.*/
    private DataOutputStream dosREPORT;		
	mm_rpshr()
	{		
		super(2);    
		try
		{	
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Vendor Code"),4,3,1,1,this,'L');
			add(txtVNDCD = new TxtLimit(10),4,4,1,1,this,'L');
			add(txtVENNM = new TxtLimit(30),4,5,1,3,this,'L');
			add(new JLabel("Material Code"),5,3,1,1,this,'L');
			add(txtMATCD = new TxtLimit(10),5,4,1,1,this,'L');
			add(txtMATDS = new TxtLimit(60),5,5,1,3,this,'L');			
			add(new JLabel("Date Range"),6,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),6,4,1,1,this,'L');						
			add(txtTODAT = new TxtDate(),6,5,1,1,this,'L');					    		 		    		    
			M_pnlRPFMT.setVisible(true);			
			setENBL(false);						
		}	
		catch(Exception L_EX)
		{
		setMSG(L_EX,"Constructor");
		}	
	}	
	/**
	 * Super class method overrided to inhance its funcationality, to set components 
	 * enable & disable according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtVENNM.setEnabled(false);
		txtMATDS.setEnabled(false);		
		txtVNDCD.setEnabled(L_flgSTAT);
		txtMATCD.setEnabled(L_flgSTAT);
		txtTODAT.setEnabled(L_flgSTAT);
		txtFMDAT.setEnabled(L_flgSTAT);
	}
    public void actionPerformed(ActionEvent L_AE)
	{
	    super.actionPerformed(L_AE);		
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)			
		{
			cl_dat.M_PAGENO = 0;				
			cl_dat.M_intLINNO_pbst = 0;
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{		
			
			if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				setENBL(false);	
			else 			
			{
				setENBL(true);	
				txtVNDCD.requestFocus();
				if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
				{	
					try
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);																
						txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));						
					}
					catch(Exception L_EX)
					{
			        	setMSG(L_EX,"txtMATCD EnterKey");
		        	}
				}
			}
    	}    		
		// If fully valid Vendor code is given and Enter Key pressed.
    	else if(M_objSOURC == txtVNDCD)
		{
		    if ((txtVNDCD.getText().trim()).length()== 5)
		    {
			    try
			    {  
					txtVNDCD.setText(txtVNDCD.getText().toUpperCase());
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY  = "Select PT_PRTCD,PT_PRTNM from CO_PTMST";
					M_strSQLQRY  +=" where PT_PRTTP ='S' and isnull(PT_STSFL,'') <>'X'";
					M_strSQLQRY += " AND PT_PRTCD ='"+ txtVNDCD.getText().trim()+ " '";						
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(M_rstRSSET != null)
					{		                     
		    		     if(M_rstRSSET.next())
							txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));       						 						    
					     txtMATCD.requestFocus();
					     M_rstRSSET.close();
					}					    
					setCursor(cl_dat.M_curDFSTS_pbst);
			    }
			    catch(Exception L_EX)
			    {
				    setCursor(cl_dat.M_curDFSTS_pbst);
					setMSG(L_EX,"txtVNDCD EnterKey");
				}
		    }
			else
			{				
			    setMSG("Enter complete Vendor code, then press EnterKey Or press F1 for Help ..",'E');			
				txtVENNM.setText("");
				txtVNDCD.requestFocus();
			}
		}
		// If fully valid Material code is given and Enter Key pressed.
		else if(M_objSOURC == txtMATCD)
    	{ 
    	    if (txtMATCD.getText().length()==10) 
    	    {
    	        try
		        {  	
    				setCursor(cl_dat.M_curWTSTS_pbst);    		
					M_strSQLQRY = "Select distinct CT_MATCD,CT_MATDS,CT_UOMCD,GR_VENCD from CO_CTMST,MM_GRMST";
					M_strSQLQRY +=" where GR_MATCD = CT_MATCD AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <>'X' and isnull(CT_STSFL,'') NOT IN('X','9')";// and CT_CODPT = 'CD'";									   					   
					M_strSQLQRY += " AND CT_MATCD = '"+txtMATCD.getText().trim() + "'";					    
					if(txtVNDCD.getText().trim().length() >0)
					    M_strSQLQRY += " AND GR_VENCD = '"+txtVNDCD.getText().trim() + "'";								
					M_strSQLQRY += " ORDER BY CT_MATDS";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
					if(M_rstRSSET != null)
		            {		                     
			    	    if(M_rstRSSET.next())         					          
							txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));       						 
						M_rstRSSET.close();
						txtFMDAT.requestFocus();
		            }					    		                
					setCursor(cl_dat.M_curDFSTS_pbst);
				} 				
				catch(Exception e)
				{
    				setCursor(cl_dat.M_curDFSTS_pbst);
				}
    		}
    		else
			{
    			setMSG("Enter valid material code, then Press Enterkey Or press F1 for Help ..",'E');			
				txtMATDS.setText("");
				txtMATCD.requestFocus();
			}
    	}    		
	}			
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);		
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{			
		    if(M_objSOURC == txtVNDCD)
			{
			    if (txtVNDCD.getText()!= "")
			    {					
				    try
				    {
						txtVNDCD.setText(txtVNDCD.getText().toUpperCase());
    					setCursor(cl_dat.M_curWTSTS_pbst);
						 M_strSQLQRY = "Select distinct GR_VENCD,GR_VENNM from MM_GRMST";
					    M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP ='"+M_strSBSCD.substring(2,4)+"' and isnull(GR_STSFL,'') <>'X'";					        					
    					if(txtVNDCD.getText().trim().length() > 0)
	    			        M_strSQLQRY += " AND GR_VENCD LIKE '" + txtVNDCD.getText().trim()+"%'";						                               									
						M_strSQLQRY +=" order by GR_VENCD";
					    M_strHLPFLD = "txtVENCD";										    
					    if(M_strSQLQRY  != null)
					    {						
    						cl_hlp(M_strSQLQRY,2,1,new String[]{"Vender Code","Vender Name"},2,"CT");										
						    setCursor(cl_dat.M_curDFSTS_pbst);						
					    }
				    }
				    catch(Exception e)
				    {
    					setCursor(cl_dat.M_curDFSTS_pbst);
				    }
			    }    
			    else
			    setMSG("please Enter Complete valid Vendor code or press F1 Fro Help ..",'E');
			}
			// If Material code is given and F1 Key pressed.
		    else if(M_objSOURC == txtMATCD)
			{					
				if((txtMATCD.getText().trim()).length()>=2)
				{
					if(!((txtMATCD.getText().trim()).substring(0,1)).equals("68"))
					{
						setMSG("Material Code Releated to Tankfarm are allowed only & it starts from 68..",'E');
						return;
					}				
				}
				try
				{  				    
				    setCursor(cl_dat.M_curWTSTS_pbst);
				    M_strHLPFLD = "txtMATCD";
					M_strSQLQRY = "Select DISTINCT CT_MATCD,CT_MATDS,CT_UOMCD from CO_CTMST ";
					if(txtVNDCD.getText().trim().length() >0)
					{
						M_strSQLQRY +=",MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') <>'X' and GR_MATCD = CT_MATCD ";
						if (txtVNDCD.getText()!="")
							M_strSQLQRY +=" AND GR_VENCD ='"+txtVNDCD.getText().trim() +"' AND ";
					}
					else
					{
						M_strSQLQRY +=" WHERE ";
					}
					M_strSQLQRY +="  isnull(CT_STSFL,'') <>'X' and ct_codtp ='CD'";				
					if(txtMATCD.getText().trim().length() >= 2)
						M_strSQLQRY += " AND CT_MATCD LIKE '"+txtMATCD.getText().trim()+"%'";			        
					else
						M_strSQLQRY += " and CT_MATCD like '68%'";
							
					M_strSQLQRY += " order by CT_MATDS";						
					if(M_strSQLQRY != null)
					{								
						cl_hlp(M_strSQLQRY,2,1,new String[]{"Material Code","Material Description","UOM"},3,"CT");												setCursor(cl_dat.M_curDFSTS_pbst);
					}
				}
				catch(Exception L_EX)
				{
					setCursor(cl_dat.M_curDFSTS_pbst);
					setMSG(L_EX,"F1 Help");
				}
			}			
		 }
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
		    if(M_objSOURC == txtFMDAT)
			    txtTODAT.requestFocus();
			if(M_objSOURC == txtTODAT)
			   cl_dat.M_btnSAVE_pbst.requestFocus();
        }		
	}			
	/**
	Method for execution of help for Material Code & Vendor Code.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();		
		if(M_strHLPFLD == "txtMATCD")
		{
			txtMATCD.setText(cl_dat.M_strHLPSTR_pbst);	
			txtMATDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtFMDAT.requestFocus();
		}
		if(M_strHLPFLD == "txtVENCD")
		{			
			txtVNDCD.setText(cl_dat.M_strHLPSTR_pbst);	
			txtVENNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());					
		}
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if(vldDATA())
		{
			try
			{
			    if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpshr.html";
			    else 
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpshr.doc";				
				getALLREC();				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT >0)
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
					 if(intRECCT >0)
					 {						
					     if(M_rdbHTML.isSelected())
					        p  = r.exec("C:\\windows\\iexplore.exe " + strFILNM); 
					     else
    					    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
	    			  }
 		    		  else
				        setMSG("No data found, Please check input data ..",'E');				    
				}	
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
				{
					cl_eml ocl_eml = new cl_eml();				    
					for(int i=0;i<M_cmbDESTN.getItemCount();i++)
					{						
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Material Shotrage Status Report"," ");						
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
					}
			    }
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"exePRINT");
			}
		}
	}
	/**
	* Method to fetch data from tables MM_GRMST and CO_CTMST and club it with Header & 
	*footer in DataOutputStream.
	*/
	private void getALLREC()
	{ 
	    try
	    {				
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)&&(M_rdbHTML.isSelected())))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);		
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Vechicle Entry Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");			
			}
			// To retrieve the unit of measurement for given material.
			if ((txtMATCD.getText()).length()==10)	
			{   			    
			    M_strSQLQRY = "Select CT_UOMCD from CO_CTMST where CT_MATCD ='" + txtMATCD.getText().trim() + "'";			    			    				
			    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
            }			    
			else
			    setMSG("Please Enter Material Code Or Press F1 for Help ..",'E');			
			if(M_rstRSSET !=null)
			{
			    if (M_rstRSSET.next())
					strUOMCD=M_rstRSSET.getString("CT_UOMCD");
			    M_rstRSSET.close();
			}			
            prnHEADER();
			//Procerure to retrieve requried data.
				strVNDCD=txtVNDCD.getText().trim();
				String L_strCHLNO,L_strCHLDT,L_strRECQT,L_strSHRQT;
				String L_strMATCD,L_strUOMCD,L_strCHLQT;
				String L_strGRNNO,L_strGRNDT,L_strVNDCD ="",L_strVENNM ="",L_strOVENCD ="";		
				String L_strFMDAT="",L_strTODAT="",L_strPRNSTR = "";		 		
				float L_fltDIFQT =0,L_fltTCHLQT = 0,L_fltTRECQT = 0,L_fltTDIFQT = 0;
				if (((txtFMDAT.getText().trim()).length()!=0) && ((txtTODAT.getText().trim()).length()!=0))
					{
						L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
						L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));		
					}
				strMATCD = txtMATCD.getText().trim();			
				M_strSQLQRY = "Select GR_MATCD,GR_TRNCD,GR_VENCD,GR_VENNM, ";
				M_strSQLQRY += "GR_CHLNO,GR_CHLDT,GR_CHLQT,GR_RECQT,GR_GRNNO,GR_GRNDT,CT_MATDS,CT_UOMCD ";
				M_strSQLQRY += " from MM_GRMST,CO_CTMST ";
				M_strSQLQRY += " where GR_MATCD = CT_MATCD and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + M_strSBSCD.substring(2,4) + "' ";
				if (((txtFMDAT.getText().trim()).length()!=0) && ((txtTODAT.getText().trim()).length()!=0))
					M_strSQLQRY += " and GR_GRNDT between '" + L_strFMDAT + "' and '" + L_strTODAT +"' ";			
				if(strVNDCD.trim().length() > 0)
					M_strSQLQRY += " and GR_VENCD ='"+strVNDCD +"' ";
				if(strMATCD.trim().length() > 0)
					M_strSQLQRY += " and GR_MATCD ='"+strMATCD +"' ";
				M_strSQLQRY += " and isnull(GR_STSFL,'') <>'X' and isnull(CT_STSFL,'') <>'X'";
				M_strSQLQRY += " order by GR_VENCD,GR_GRNNO "; 	
				
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
				if(M_rstRSSET ==null)
					setMSG("No Data Found , Please check input Data..",'E');					
				if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
					L_strGRNNO = "";
					L_strGRNDT = "";
					L_strCHLDT = "";								
					strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");								
					L_strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"");				
					L_strCHLNO = nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),"");
					java.sql.Date tempDATE = M_rstRSSET.getDate("GR_CHLDT");
					if (tempDATE!= null)
					    L_strCHLDT = M_fmtLCDAT.format(tempDATE);				
					L_strCHLQT = nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0");				
					L_strRECQT = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0");				
					L_strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
					tempDATE = M_rstRSSET.getDate("GR_GRNDT");
					if (tempDATE!= null)
					    L_strGRNDT = M_fmtLCDAT.format(tempDATE);	            			
					L_strVNDCD = nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"");
					L_strVENNM = nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"");
					L_fltDIFQT = Float.valueOf(L_strRECQT).floatValue() - Float.valueOf(L_strCHLQT).floatValue();
					L_strSHRQT = setNumberFormat(L_fltDIFQT,3);
					if(cl_dat.M_intLINNO_pbst > 64)
					{			
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",5));
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}				
					// Transporter Changes
					if(!L_strVNDCD.trim().equals(L_strOVENCD))
					{		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(intRECCT != 0)
						{
							dosREPORT.writeBytes("\n");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			                    dosREPORT.writeBytes(padSTRING('R'," ",5));
							dosREPORT.writeBytes("                                         Total : " 
								+ padSTRING('L',setNumberFormat(L_fltTCHLQT,3),9)  
								+ padSTRING('L',setNumberFormat(L_fltTRECQT,3),10)  
								+ padSTRING('L',setNumberFormat(L_fltTDIFQT,3),13) + "\n");
							cl_dat.M_intLINNO_pbst += 2;
							if(cl_dat.M_intLINNO_pbst > 64)
							{
								if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				                    dosREPORT.writeBytes(padSTRING('R'," ",5));
								dosREPORT.writeBytes("-----------------------------------------------------------------------------------");
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
							    	prnFMTCHR(dosREPORT,M_strEJT);
								prnHEADER();
							}
							L_fltTCHLQT = L_fltTRECQT = L_fltTDIFQT = 0;
						}
						if (M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");
						dosREPORT.writeBytes("\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				            dosREPORT.writeBytes(padSTRING('R'," ",5));
						dosREPORT.writeBytes(padSTRING('R',L_strVNDCD,7) + L_strVENNM + "\n\n");					
						if (M_rdbHTML.isSelected())
				            dosREPORT.writeBytes("</b>");
						L_strOVENCD = L_strVNDCD;
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
						    prnFMTCHR(dosREPORT,M_strNOBOLD);
						cl_dat.M_intLINNO_pbst += 3;
						if(cl_dat.M_intLINNO_pbst > 64)
						{
						    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						        dosREPORT.writeBytes(padSTRING('R'," ",5));
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------");
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
						}
					}
					L_fltTCHLQT += Float.valueOf(L_strCHLQT).floatValue();
					L_fltTRECQT += Float.valueOf(L_strRECQT).floatValue();
					L_fltTDIFQT += L_fltDIFQT;
					L_strPRNSTR = "";
					L_strPRNSTR += padSTRING('R',L_strGRNNO,13);
					L_strPRNSTR += padSTRING('R',L_strGRNDT,14);
					L_strPRNSTR += padSTRING('R',L_strCHLNO,12);
					L_strPRNSTR += padSTRING('R',L_strCHLDT,12);
					L_strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strCHLQT).floatValue(),3),8);
					L_strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strRECQT).floatValue(),3),12);
					L_strPRNSTR += padSTRING('L',setNumberFormat(Float.valueOf(L_strSHRQT).floatValue(),3),11);
					L_strPRNSTR += "  ";
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				        dosREPORT.writeBytes(padSTRING('R'," ",5));
					dosREPORT.writeBytes(L_strPRNSTR + "\n");
					cl_dat.M_intLINNO_pbst += 1;
					if(cl_dat.M_intLINNO_pbst > 64)
						{
						    if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						        dosREPORT.writeBytes(padSTRING('R'," ",5));
							dosREPORT.writeBytes("-----------------------------------------------------------------------------------");
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();
						}
					intRECCT++;
				}							
				if(M_rstRSSET != null)
					M_rstRSSET.close();			
			    if(intRECCT == 0)
			        setMSG("No Data Found, Please check input Data..",'E');				  			    
			    else
			    {       
			        if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
					    prnFMTCHR(dosREPORT,M_strBOLD);                            
					if (M_rdbHTML.isSelected())
			            dosREPORT.writeBytes("<b>");
			        dosREPORT.writeBytes("\n");
			        if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			            dosREPORT.writeBytes(padSTRING('R'," ",5));
					dosREPORT.writeBytes("                                         Total : "
					 + padSTRING('L',setNumberFormat(L_fltTCHLQT,3),10) 
					 + padSTRING('L',setNumberFormat(L_fltTRECQT,3),12) 
					 + padSTRING('L',setNumberFormat(L_fltTDIFQT,3),11) + "\n");
					 if (M_rdbHTML.isSelected())
			            dosREPORT.writeBytes("</b>");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
					    prnFMTCHR(dosREPORT,M_strNOBOLD);
					cl_dat.M_intLINNO_pbst += 2;
					if(cl_dat.M_intLINNO_pbst > 64)
					{
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			                dosREPORT.writeBytes(padSTRING('R'," ",5));
						dosREPORT.writeBytes("-----------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
						    prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    									
						prnFMTCHR(dosREPORT,M_strBOLD);
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",5));
					dosREPORT.writeBytes("-----------------------------------------------------------------------------------\n");				
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))    				
					    prnFMTCHR(dosREPORT,M_strNOBOLD);					
				}			
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);								
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");     										
			dosREPORT.close();
			fosREPORT.close();						        			        			   
        	setCursor(cl_dat.M_curDFSTS_pbst);
	    }
	 	catch(Exception L_EX)
		{			
		    cl_dat.M_PAGENO=0;
			setMSG(L_EX,"getALLREC");
		}
	}
	/**
	Method to validate data, before execution of query, To check for blank input and wrong input Data.
	*/
	boolean vldDATA()
	{
		try
		{	
			if(txtMATCD.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter Material Code Or Press F1 For Help.. ",'E');
				txtMATCD.requestFocus();
				return false;
			}				
            if (!(txtTODAT.getText().trim().equals("")) && (!(txtFMDAT.getText().trim().equals(""))))
			{	
				if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
				{			    
					setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
					txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
					txtTODAT.requestFocus();
					return false;
				}
				else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
				{			    
					setMSG("Please Note that From-Date must be Older than To-Date .. ",'E');													
					txtFMDAT.requestFocus();
					return false;
				}					
			}				
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getSelectedIndex() == 0)
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
			return true;
		}
		catch(Exception L_EX)
		{
			return false;
		}
	}
	/**
	Method to print Header of the Report.
	*/ 
	private void prnHEADER()
	{
		try
		{			
			String L_strSTRNM =cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString().trim(); 							 
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			strMATCD=txtMATCD.getText().trim();
			strMATDS=txtMATDS.getText().trim();													
			dosREPORT.writeBytes("\n\n\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",5));			    
			dosREPORT.writeBytes("SUPREME PETROCHEM LTD.                                    ");
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",5));
			dosREPORT.writeBytes("Material Shortage Statement                               ");
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");		
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",5));
			dosREPORT.writeBytes("Stores Type     : " + L_strSTRNM +"\n");			
			// if Date range is not specifed then the date range part from header is removed.
			if (!(txtTODAT.getText().trim().equals("")) && (!(txtFMDAT.getText().trim().equals(""))))
				{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",5));
				dosREPORT.writeBytes("Date Range      : " + txtFMDAT.getText().trim() +" To "+txtTODAT.getText().trim()+"\n");
				}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",5));
			dosREPORT.writeBytes("Material details: " + strMATCD +"/"+ strUOMCD +"/"+strMATDS +"\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",5));
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------\n");					
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",5));
			dosREPORT.writeBytes("GRIN No.     Grin Date     Chl. No.    Chl.Date    Chl. Qty    Rcpt.Qty   Shortage  \n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",5));
			dosREPORT.writeBytes("-----------------------------------------------------------------------------------\n");							
			// if Date range is not specifed then the date range part from header is removed.
			if (!(txtTODAT.getText().trim().equals("")) && (!(txtFMDAT.getText().trim().equals(""))))
				cl_dat.M_intLINNO_pbst += 12;	
			else
				cl_dat.M_intLINNO_pbst += 13;	
		}		
		catch(Exception L_SE)
		{
		    cl_dat.M_PAGENO=0;		    
			setMSG(L_SE,"prnHEADER");
		}
	}
}