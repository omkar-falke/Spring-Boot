/*
System Name   : Material Management System
Program Name  : Statement of material receipt
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 6th Apr 2005
Version       : MMS 2.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import javax.swing.JLabel;import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
/**<pre>
System Name : Material Management System.
 
Program Name : Statement of Material Receipts

Purpose : This program will generate a Report for Material Receipt for given 
Date-Time range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_WBTRN       WB_DOCTP,WB_DOCNO,WB_SRLNO                               #	     
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name             Column Name  Table name  Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT & txtFMTIM    WB_OUTTM     MM_WBTRN    Timestamp     Weigh-Bridge out Time
txtTODAT & txtTOTIM    WB_OUTTM     MM_WBTRN    Timestamp     Weigh-Bridge out Time
--------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)MM_WBTRN
                        
<B>Conditions Give in Query:</b>
    Data is taken from MM_WBTRN for given Consignment Number
        1) WB_DOCTP = Given Gate-In Type ("01").
        2) and WB_OUTTM within the given Date-Time range.

<B>Validations :</B>
   - From-Date, Time & To-Date, Time cannot be blank.
   - DateTime entered must be smaller than today.
   - From-Date & Time must be smaller than or equal to To-Date & Time.   
</I> */

public class mm_rprct extends cl_rbase
{									/** JTextField to accept & display From-Date.*/
	private JTextField txtFMDAT;	/** JTextField to accept & display To-Date.*/
	private JTextField txtTODAT;    /** JTextField to accept & display FromDate-Time.*/
	private JTextField txtFMTIM;	/** JTextField to accept & display ToDate-Time.*/
	private JTextField txtTOTIM;	/** String variable for generated report File Name.*/
	private String strFILNM;		/** FileoutputStream object for generate report file handling.*/
	private FileOutputStream fosREPORT;/** DataOutputStream object to generate Repot File.*/
    private DataOutputStream dosREPORT;/** Integer variable for Record Count.*/
	private int intRECCT;		/** Integer variable for Serial Number.*/
	private int intSRLNO;		/** String variable for Gate-In Number.*/
	private String strGINNO;	/** String variable for Challan Number.*/
	private String strCHLNO;	/** String variable for Challan Date.*/
	private String strCHLDT;	/** String variable for Lorry Number.*/
	private String strLRYNO;	/** String variable for Transporter Description.*/
	private String strTPRDS;	/** String variable for Challan Quantity.*/
	private String strCHLQT;	/** String variable for Gross Weight.*/	
	private String strGRSWT;	/** String variable for Tare Weight.*/
	private String strTARWT;	/** String variable for Net Weight.*/	
	private String strNETWT;	/** String variable for Unit of Measurement.*/
	private String strUOMQT;	/** String variable for Difference Quantity*/	
	private String strDIFQT;	/** String variable for Material Description*/
	private String strMATDS;	/** String variable for Bill of Entry Number.*/
	private String strBOENO="";	/** String variable for Tank Number.*/
	private String strTNKNO;	/** StringBuffer object to hold Report Data fetched from DataBase. */
	private StringBuffer stbPRNST;	/** String variable for ISO Document Code.*/
	private String strISODCA;		/** String variable for ISO Document Code.*/
	private String strISODCB;		/** String variable for ISO Document Code.*/
	private String strISODCC;
	mm_rprct()
	{
		super(2);
		try
		{	
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);						
		    add(new JLabel("From Date"),4,4,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),4,5,1,1,this,'L');
			add(txtFMTIM = new TxtTime(),4,6,1,.7,this,'L');
			add(new JLabel("To Date"),5,4,1,1,this,'L');
			add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');
			add(txtTOTIM = new TxtTime(),5,6,1,.7,this,'L');						
			M_pnlRPFMT.setVisible(true);						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"");
		}	
	}
	/**
     * Super class method overrided to inhance its funcationality to set Components enable & 
     * disable, according to requriement.
     * @param L_flgSTAT boolean variable to pass state, to make Components enable or disable.
     */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtTODAT.setEnabled(L_flgSTAT);
		txtTOTIM.setEnabled(L_flgSTAT);
		txtFMDAT.setEnabled(L_flgSTAT);
		txtFMTIM.setEnabled(L_flgSTAT);		
		if ((L_flgSTAT==true)&&((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
		{			                
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);															
			txtFMDAT.setText(calFMDAT(cl_dat.M_strLOGDT_pbst));
			txtFMTIM.setText("06:00");
			txtTOTIM.setText("06:00");			
		}		
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
		 	if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				{
					setMSG("Please Select Destination for the Report..",'N');					
					clrCOMP();
				}
				else
					txtFMDAT.requestFocus();				
			}						
			else if(M_objSOURC == txtFMDAT && ((txtFMDAT.getText().trim()).length()!=0))
				txtFMTIM.requestFocus();
			else if(M_objSOURC == txtFMTIM && ((txtFMTIM.getText().trim()).length()!=0)) 
				txtTODAT.requestFocus();
			else if(M_objSOURC == txtTODAT && ((txtTODAT.getText().trim()).length()!=0)) 
				txtTOTIM.requestFocus();
			else if(M_objSOURC == txtTOTIM && ((txtTOTIM.getText().trim()).length()!=0)) 
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)		
			{
				cl_dat.M_PAGENO = 0;
				intRECCT = 0;
				intSRLNO=0;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Date Time Comparision");
		}
	}	
	/**
	 * Method to display, print report according to the selection.
	 */
	void exePRINT()
	{		
		try
		{
			if(!vldDATA())
				return;											
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rprct.html";
			else 
			    strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rprct.doc";								
			getALLREC(); 
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
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ strFILNM); 
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Statement of Material Receipt"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}																
	/**
	Method to validate data inserted, before execution of query,To check for blank input, wrong 
	Date & time.
	*/
	boolean vldDATA()
	{		
		try
		{
			String L_strFMDTM,L_strTODTM;
			L_strFMDTM = txtFMDAT.getText().trim()+" " + txtFMTIM.getText().trim();
			L_strTODTM = txtTODAT.getText().trim() +" "+ txtTOTIM.getText().trim();			
			strISODCA = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPRCT01");
			strISODCB = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPRCT02");
			strISODCC = cl_dat.getPRMCOD("CMT_CODDS","ISO","MMXXRPT","MM_RPRCT03");
			if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Spacify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter To-Date To Spacify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}		
			else if(txtFMTIM.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter From-Time, To Spacify Date Range ..",'E');
				txtFMTIM.requestFocus();
				return false;
			}			
			if(txtTOTIM.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter To-Time, To Spacify Date Range ..",'E');
				txtTOTIM.requestFocus();
				return false;
			}
						
			if(M_fmtLCDTM.parse(L_strTODTM).compareTo(M_fmtLCDTM.parse(cl_dat.getCURDTM()))>0)
			{
				setMSG("Date OR Time can not be greater than current Date OR Time..",'E');
				txtTODAT.requestFocus();
			}
			if(M_fmtLCDTM.parse(L_strFMDTM).compareTo(M_fmtLCDTM.parse(L_strTODTM))>0)
			{
				setMSG("To Date Time can not be smaller than From Date Time..",'E');
				txtFMDAT.requestFocus();
			}									
			if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
				txtTODAT.requestFocus();
				return false;
			}
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("Please Note that From-Date must be greater than To-Date .. ",'E');								
				txtFMDAT.setText(calFMDAT(cl_dat.M_strLOGDT_pbst));            
				txtFMDAT.requestFocus();
				return false;
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
	 * Method to fetch Data from table MM_WBTRN, for Gate-In Type "01" & Given 
	 * Date-Time range.
	 */	
	private void getALLREC()
	{
		try
		{			
			float fltCHLQT,fltNETQT,fltDIFQT;
			fltCHLQT = fltNETQT = fltDIFQT = 0;
			float fltTCHQT,fltTNTQT,fltTDFQT;
			fltTCHQT = fltTNTQT = fltTDFQT = 0;			
			String L_strGINTP ="01", L_strFMDAT, L_strTODAT;
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process....",'N');
			fosREPORT = new FileOutputStream(strFILNM);	
			dosREPORT = new DataOutputStream(fosREPORT);
						
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);		
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Vechicle Entry Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
    		prnHEADER();			
			L_strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim()+" " + txtFMTIM.getText().trim()));			
			L_strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim() +" "+ txtTOTIM.getText().trim()));					
										
			M_strSQLQRY = "Select WB_MATDS,WB_BOENO,WB_TNKNO,WB_DOCNO,WB_CHLNO,WB_CHLDT,";
			M_strSQLQRY += "WB_TPRDS,WB_LRYNO,WB_CHLQT,WB_GRSWT,WB_TARWT,WB_NETWT,WB_UOMQT,";
			M_strSQLQRY += "WB_INCTM from MM_WBTRN ";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + L_strGINTP + "'";
            M_strSQLQRY += " and WB_OUTTM between '" + L_strFMDAT; 
			M_strSQLQRY += "' and '" + L_strTODAT ;
            M_strSQLQRY += "' and isnull(WB_STSFL,'') <> 'X'";
			M_strSQLQRY += " order by WB_MATDS,WB_BOENO,WB_TNKNO,WB_DOCNO,WB_INCTM";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			stbPRNST = new StringBuffer("");
			String LM_OMATDS = "",LM_OBOENO = "",LM_OTNKNO = "";			
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				intSRLNO =intSRLNO + 1;
				strMATDS = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"");
				strBOENO = nvlSTRVL(M_rstRSSET.getString("WB_BOENO"),"");
//			System.out.println("BILL"+ strBOENO);
				if (strBOENO.length()==0)
					strBOENO="------";
				strTNKNO = nvlSTRVL(M_rstRSSET.getString("WB_TNKNO"),"");
				strGINNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"");
				strCHLNO = nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),"");
				strCHLDT = M_fmtLCDAT.format(M_rstRSSET.getDate("WB_CHLDT"));
				strTPRDS = (nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"") + "                                      ").substring(0,37);
				strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"");								
				strCHLQT = setNumberFormat(Float.valueOf(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0")).floatValue(),3);				
				strGRSWT = setNumberFormat(Float.valueOf(nvlSTRVL(M_rstRSSET.getString("WB_GRSWT"),"0")).floatValue(),3);				
				strTARWT = setNumberFormat(Float.valueOf(nvlSTRVL(M_rstRSSET.getString("WB_TARWT"),"0")).floatValue(),3);				
				strNETWT = setNumberFormat(Float.valueOf(nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0")).floatValue(),3);				
				strUOMQT = setNumberFormat(Float.valueOf(nvlSTRVL(M_rstRSSET.getString("WB_UOMQT"),"0")).floatValue(),3);
				if(!strBOENO.equals(LM_OBOENO) || !strTNKNO.equals(LM_OTNKNO))
				{
					if(intRECCT >0)
					{						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						{
							dosREPORT.writeBytes(padSTRING('R'," ",10));					
							prnFMTCHR(dosREPORT,M_strBOLD);				
						}
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<b>");						
						dosREPORT.writeBytes(padSTRING('L',"Total : ",74)
							+ padSTRING('L',setNumberFormat(fltCHLQT,3),9) 
							+ padSTRING('L',setNumberFormat(fltNETQT,3),35) 
							+ padSTRING('L',setNumberFormat(fltDIFQT,3),9));
						dosREPORT.writeBytes("\n");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);										
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</b>");
						cl_dat.M_intLINNO_pbst += 1;
						if(cl_dat.M_intLINNO_pbst> 64)
						{						
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								dosREPORT.writeBytes(padSTRING('R'," ",12));
							dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------");
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							prnHEADER();					
						}
						fltCHLQT = fltNETQT=fltDIFQT =0;
					}
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst++;
					if(cl_dat.M_intLINNO_pbst> 64)
					{						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",12));
						dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();					
					}
					LM_OBOENO = strBOENO;
					LM_OTNKNO = strTNKNO;
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					{
						dosREPORT.writeBytes(padSTRING('R'," ",12));					
						prnFMTCHR(dosREPORT,M_strBOLD);				
					}
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<b>");
					/*dosREPORT.writeBytes("  Material : " 
						+ padSTRING('R',strMATDS,30) + "Bill of Entry : " 
						+ padSTRING('R',strBOENO,30) 
						+ "Tank No : " + strTNKNO + "\n");*/
					dosREPORT.writeBytes("  Material : " 
						+ strMATDS + "     Bill of Entry : " 
						+ strBOENO + "     Tank No : " + strTNKNO + "\n");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);				
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</b>");
					cl_dat.M_intLINNO_pbst++;
					if(cl_dat.M_intLINNO_pbst> 64)
					{						
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							dosREPORT.writeBytes(padSTRING('R'," ",12));
						dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();					
					}
				}
				fltCHLQT += Float.parseFloat(strCHLQT);
				fltNETQT += Float.parseFloat(strUOMQT);
				strDIFQT = setNumberFormat((Float.valueOf(strUOMQT).floatValue() - Float.valueOf(strCHLQT).floatValue()),3);				
				fltDIFQT += Float.parseFloat(strDIFQT);			
				fltTCHQT += Float.parseFloat(strCHLQT);
				fltTNTQT += Float.parseFloat(strUOMQT);
				fltTDFQT += Float.parseFloat(strDIFQT);
				
				if(stbPRNST.length() != 0)
					stbPRNST.delete(0,stbPRNST.length());
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					stbPRNST.append(padSTRING('R'," ",12));
				else
					stbPRNST.append(padSTRING('R'," ",2));
				stbPRNST.append(padSTRING('L',String.valueOf(intSRLNO),3));
				stbPRNST.append(" ");
				stbPRNST.append(padSTRING('R',strGINNO,9));
				stbPRNST.append(padSTRING('R',strLRYNO,12));
				stbPRNST.append(padSTRING('R',strTPRDS.substring(0,25),26));
				stbPRNST.append(padSTRING('R',strCHLNO,11));
				stbPRNST.append(padSTRING('R',strCHLDT,11));
				stbPRNST.append(padSTRING('L',strCHLQT,8));
				stbPRNST.append(padSTRING('L',strGRSWT,9));
				stbPRNST.append(padSTRING('L',strTARWT,8));
				stbPRNST.append(padSTRING('L',strNETWT,9));
				stbPRNST.append(padSTRING('L',strUOMQT,9));
				stbPRNST.append(padSTRING('L',strDIFQT,9));
				stbPRNST.append("\n");
				intRECCT++;
				dosREPORT.writeBytes(stbPRNST.toString());				
				cl_dat.M_intLINNO_pbst++;
								
				if(cl_dat.M_intLINNO_pbst> 64)
				{						
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",12));
					dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------");
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					prnHEADER();					
				}
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
			if(stbPRNST.length() != 0)
				stbPRNST.delete(0,stbPRNST.length());			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				dosREPORT.writeBytes(padSTRING('R'," ",10));					
				prnFMTCHR(dosREPORT,M_strBOLD);				
			}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");					
			dosREPORT.writeBytes(padSTRING('L',"Total : ",74) 
				+ padSTRING('L',setNumberFormat(fltCHLQT,3),9) 
				+ padSTRING('L',setNumberFormat(fltNETQT,3),35) 
				+ padSTRING('L',setNumberFormat(fltDIFQT,3),9));						
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);							
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
			dosREPORT.writeBytes("\n\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",12));
			dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst += 3;
			if(cl_dat.M_intLINNO_pbst> 64)
			{						
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",12));
				dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------");
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);
				prnHEADER();					
			}	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				dosREPORT.writeBytes(padSTRING('R'," ",10));
				prnFMTCHR(dosREPORT,M_strBOLD);							
			}
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");				
			dosREPORT.writeBytes(padSTRING('L',"Total : ",74) 
				+ padSTRING('L',setNumberFormat(fltTCHQT,3),9) 
				+ padSTRING('L',setNumberFormat(fltTNTQT,3),35) 
				+ padSTRING('L',setNumberFormat(fltTDFQT,3),9));
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);							
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");			
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
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Method to generate header part of the Report.
	 */
	private void prnHEADER()
	{  
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",97));
			dosREPORT.writeBytes ("--------------------------------");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",99));
			dosREPORT.writeBytes(strISODCA);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",99));
			dosREPORT.writeBytes(strISODCB);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",99));
			dosREPORT.writeBytes(strISODCC);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",97));
			dosREPORT.writeBytes ("--------------------------------");
			dosREPORT.writeBytes("\n");									
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",14));
			else
				dosREPORT.writeBytes(padSTRING('R'," ",2));
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",101));
			dosREPORT.writeBytes ("Report Date : "+cl_dat.M_txtCLKDT_pbst.getText() + "\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",14));
			else
				dosREPORT.writeBytes(padSTRING('R'," ",2));
			dosREPORT.writeBytes(padSTRING('R',"Statement of Material Receipt",101));
			dosREPORT.writeBytes("Page No.    : " + cl_dat.M_PAGENO + "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",12));
			dosREPORT.writeBytes("  From " + txtFMDAT.getText().trim()+" " + txtFMTIM.getText().trim() + " to " + txtTODAT.getText().trim() +" "+ txtTOTIM.getText().trim() + "\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",12));
			dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",12));
			dosREPORT.writeBytes("  Sr. Gate-In  Vehicle No. Transporter               Chalan No. Chalan       Chalan    Gross    Tare     Net      UOM   Diff In\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",12));
			dosREPORT.writeBytes("  No. No.                                                       Date       Quantity   Weight  Weight   Weight     Qty. Qunatity\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",12));
			dosREPORT.writeBytes ("  -------------------------------------------------------------------------------------------------------------------------------\n");			
			cl_dat.M_intLINNO_pbst += 12;		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX, "prnHEADER");
		}
	}
	/**
	Method to calculate From-Date one day smaller than To-Date.
	@param P_strTODAT String argument to pass To-date.
	*/
    private String calFMDAT(String P_strTODAT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strTODAT));
		    M_calLOCAL.add(java.util.Calendar.DATE,-1);																
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{
	       setMSG(L_EX,"calFMDAT");
	       return (cl_dat.M_strLOGDT_pbst);
        }					
	}
}