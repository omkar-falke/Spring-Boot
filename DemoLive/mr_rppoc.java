/*
System Name   : Export Marketing Management System
Program Name  : Pending Order Confirmation Report
Program Desc. : Pending Order Confirmation Report

Author        : Mr S.R.Tawde
Date          : 10.10.2008
Version       : MARKETING v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;import javax.swing.JCheckBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
/**<pre>
<b>System Name :</b> Marketing Management System
 
<b>Program Name :</b> Pending Order Confirmation Report

<b>Purpose :</b> This module gives the statement of Pending Order Confirmations as on date.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
MR_OCMST    OC_CMPCD,OC_MKTTP,OC_OCFNO                              #
MR_OCTRN    OCT_CMPCD,OCT_MKTTP,OCT_OCFNO,                          #
			OCT_PRDDS,OCT_PKGTP
CO_CDTRN	CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #
CO_PTMST    PT_PRTTP,PT_PRTCD										#
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table Name      Type/Size     Description
--------------------------------------------------------------------------------
txtFRMDT    AS ON DATE (SYSTEM DATE)
--------------------------------------------------------------------------------
<I>

<B>Conditions Give in Query:</b>
             Data is taken from MR_OCMST,MR_OCTRN,CO_CDTRN,CO_PTMST WHERE  

</I>
Validations :
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
 */
public class mr_rppoc extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
//	private JTextField txtTODAT;	
//	private JTextField txtFMTIM;
//	private JTextField txtTOTIM;
//	private JTextField txtNOTE1;
//	private JTextField txtNOTE2;
//	private JTextField txtNOTE3;
//	private JTextField txtNOTE4;

//	private JCheckBox chkCOMFL;
		
	private int intRCTPK = 0;
	
										/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;	
												 /** Hashtable for storing different Receipt Types */  
	//private Hashtable hstRCTTP = new Hashtable();/** Hashtable for storing different Main Product Types*/
	//private Hashtable hstMNGRP = new Hashtable();/** Hashtable for storing different Sub Product Types*/
	//private Hashtable hstSBGRP = new Hashtable();/** Hashtable for storing different Package Types*/
    //private Hashtable hstPKGTP = new Hashtable(); Hashtable for Commission Rate
	//private Hashtable hstCOMRT= new Hashtable();
	private Hashtable<String,String> hstCOMRT = new Hashtable<String,String>();/** Hashtable for storing Distr.Commission Rates for different product categories*/
	
	
	/**
	 *1.Screen Designing
	 *2.Hashtable is created using CO_CDTRN for maintaining various types of codes alongwith
	 *  their descriptions.
	 */
	mr_rppoc()
	{
		super(1);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("As On Date"),5,4,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,5,1,1,this,'L');			
			//add(txtFMTIM = new TxtLimit(8),5,6,1,1,this,'L');			
			//add(new JLabel("To Date"),6,4,1,1.3,this,'L');
			//add(txtTODAT = new TxtDate(),6,5,1,1,this,'L');
			//add(txtTOTIM = new TxtLimit(8),6,6,1,1,this,'L');			
			//add(chkCOMFL=new JCheckBox("With Commisison & Prices"),7,4,1,5,this,'L');
			//add(chkCOMFL = new JCheckBox(),7,5,1,1
			//add(new JLabel("Note"),8,1,1,1,this,'L');
			//add(txtNOTE1=new TxtLimit(135),8,2,1,7,this,'L');
			//add(txtNOTE2=new TxtLimit(135),9,2,1,7,this,'L');
			//add(txtNOTE4=new TxtLimit(135),11,2,1,7,this,'L');
	

/**			M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP||CMT_CGSTP in ( 'SYSFGXXRTP','MSTCOXXPGR','SYSFGXXPKG')";
			M_strSQLQRY += " and IFNULL(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
            M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	

			if(M_rstRSSET != null)   
			{
		    	while(M_rstRSSET.next())
        		{   
					if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("MG"))
						hstMNGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,2),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("COXXPGR") && nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"").equals("SG"))
						hstSBGRP.put(M_rstRSSET.getString("CMT_CODCD").substring(0,4),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("FGXXRTP"))
						hstRCTTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
					else if(nvlSTRVL(M_rstRSSET.getString("CMT_CGSTP"),"").equals("FGXXPKG"))
						hstPKGTP.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
        		}
        		M_rstRSSET.close();
			}*/
			
			M_strSQLQRY= "Select CMT_CODCD,CMT_NCSVL from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP='MRXXDCM' and ";
			M_strSQLQRY+= "	SUBSTRING(CMT_CODCD,14,2) in ('07','08','09')";

			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET!= null)
			{
				while(M_rstRSSET.next())
				{
					hstCOMRT.put(M_rstRSSET.getString("CMT_CODCD").trim(),M_rstRSSET.getString("CMT_NCSVL").trim());
				}
			}
			
			M_pnlRPFMT.setVisible(true);			
			setENBL(false);									
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX," GUI Designing");
		}	
	}			
    /**
     *Super class method overide to enhance its funcationality 
     *For populating FROM DATE AND TO DATE with required values 
     *We can use this for Enabling or Disabling other components on the screen as per our
     *requirement based on option selected ie. ADDITION,MODIFICATION,DELETION etc.
     * @param L_flgSTAT boolean argument to pass state of the component.
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		//txtTODAT.setEnabled(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);		
       	txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));				            														
		//if (L_flgSTAT==true )
		//{
		//	if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
		//	{					 
		//		try
		//		{
		//			M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
		//			//M_calLOCAL.add(java.util.Calendar.DATE,-1);																
		//			//M_calLOCAL.getTime(txtFMTIM);
		//			//System.out.println(M_calLOCAL.getTime(txtFMTIM));
		//			txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));																					
       	//			txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));				            														
		//		}
		//		catch(Exception L_EX)
		//		{
		//			setMSG(L_EX+" setENBL",'E');
		//		}
		//	}
		//	else 
		//	{
		//		if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
		//		{
		//			txtTODAT.setText("");
		//			txtFMDAT.setText("");			
		//		}
		//	}
		//}
	}		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
		if(M_objSOURC == txtFMDAT)
			cl_dat.M_btnSAVE_pbst.requestFocus();
		//if(M_objSOURC == txtFMTIM)
		//	txtTODAT.requestFocus();
		//if(M_objSOURC == txtTODAT)			
		//	txtTOTIM.requestFocus();
		//if(M_objSOURC == txtTOTIM)
		//	chkCOMFL.requestFocus();
		
		//if(M_objSOURC == chkCOMFL)	
		//	txtNOTE1.requestFocus();
		
		//if(M_objSOURC == txtNOTE1)
		//	txtNOTE2.requestFocus();
		//else if(M_objSOURC == txtNOTE2)
		//	txtNOTE3.requestFocus();
		//else if(M_objSOURC == txtNOTE3)
		//	txtNOTE4.requestFocus();
		//else if(M_objSOURC == txtNOTE4)
		//	cl_dat.M_btnSAVE_pbst.requestFocus();

		if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{	
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if(M_cmbDESTN.getItemCount() > 1)
				{
					M_cmbDESTN.setEnabled (true);
				}
				else if(M_cmbDESTN.getItemCount() == 1)
				{
					setMSG("No Printer Attached to the System ..",'E');
				}
			}
		}
	}	        					
	/**
	*Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
							
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rppoc.htm";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rppoc.doc";				
			getDATA();				
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
					  setMSG("No data found, Please check Date Range ..",'E');				    
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
		    {
			    cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Despatch Intimation"," ");
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
	*Method to validate data in the input components on the screen, before execution like
    *validation of Dates, availability of the printers etc.
    *Check for blank input
	*/
	boolean vldDATA()
	{
		try
		{	
			if(txtFMDAT.getText().trim().toString().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Specify Date Range ..",'E');
				txtFMDAT.requestFocus();
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
			return true;
		}
		catch(Exception L_EX)
		{
			return false;
		}
	}
	/**
	*Method to fetch Data from Database and start creation of the output file.
	*/
	private void getDATA()
	{ 	    		
		String L_strFMDAT;
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblOCFQT = 0;
		double L_dblPNDQT = 0;
		//double L_dblRCTPK = 0;
		
		String L_strBYRNM = "";
		String L_strOCFNO = "";
		String L_strOCFDT = "";
		String L_strPRDDS = "";
		String L_strPKGDS = "";
		
		try
	    {	
			
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Intimation </title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();			
    		
			L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			
			setMSG("Report Generation is in Progress.......",'N');			   			
			
			M_strSQLQRY = " select oc_ocfno,oc_ocfdt,a.pt_prtnm oc_byrnm,oct_prdds,b.cmt_codds oct_pkgds,oct_ocfqt,oct_ocfqt-oct_indqt oct_pndqt ";
			M_strSQLQRY+= " from mr_ocmst,mr_octrn,co_cdtrn b,co_ptmst a where oc_cmpcd=oct_cmpcd and oc_mkttp=oct_mkttp";
			M_strSQLQRY+= " and OC_OCFNO=OCT_OCFNO and a.pt_prttp='C' and oc_byrcd=a.pt_prtcd and b.cmt_cgstp='FGXXPKG'";
			M_strSQLQRY+= " and b.cmt_codcd=oct_pkgtp and oc_stsfl='1' and oct_stsfl='1' and oct_ocfqt-oct_indqt > 0 ";
			M_strSQLQRY+= " order by a.pt_prtnm ";
			
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					while(!L_flgEOF)
					{
						L_strBYRNM = nvlSTRVL(M_rstRSSET.getString("OC_BYRNM"),"");
						L_strOCFNO = nvlSTRVL(M_rstRSSET.getString("OC_OCFNO"),"");
						L_strOCFDT = nvlSTRVL(M_rstRSSET.getString("OC_OCFDT"),"");
						L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("OCT_PRDDS"),"");
						L_strPKGDS = nvlSTRVL(M_rstRSSET.getString("OCT_PKGDS"),"");
    					L_dblOCFQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("OCT_OCFQT"),"0"));
    					L_dblPNDQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("OCT_PNDQT"),"0"));
						
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
						}
					dosREPORT.writeBytes(padSTRING('R',L_strBYRNM,40));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('L',L_strOCFNO,12));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('L',L_strOCFDT,10));
					dosREPORT.writeBytes(padSTRING('L',"",4));
					dosREPORT.writeBytes(padSTRING('R',L_strPRDDS,27));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('R',L_strPKGDS,10));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblOCFQT,3),10));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPNDQT,3),10));

					dosREPORT.writeBytes("\n");
   					cl_dat.M_intLINNO_pbst+= 1;						
						intRECCT+=1;
						if(!M_rstRSSET.next())
						{
							L_flgEOF = true;
							break;
						}
					}	
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
				//prnFOOTR();
				if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
			    if(M_rstRSSET != null)
					M_rstRSSET.close();						
				else
		 		{							
					setMSG("No Data Found..",'E');
					return ;
				}
				}
				if(intRECCT == 0)
				{
					setMSG("No Data Found for the given selection..",'E');
					return ;
				}
				}
		}
	 	catch(Exception L_EX)
		{		    
			setMSG(L_EX+" getDATA",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}	
	}
	/**
	Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			dosREPORT.writeBytes("\n\n\n\n\n");										
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst ,120));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes("Pending Order Confirmations as on "+txtFMDAT.getText().trim());				
			dosREPORT.writeBytes("\n");										
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes("Customer                                       P.I.No.       Date       Grade                         Packing        Ord.Qty.  Pending Qty. ");
			dosREPORT.writeBytes("\n");										
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			cl_dat.M_intLINNO_pbst += 12;
	    }
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnHEADER",'E');
		}
	}
/**
  	*Method to Generate the Page Footer of the Report.
*/
private void prnFOOTR()
{
	try
	{
		if(cl_dat.M_intLINNO_pbst >= 64)
		{
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strEJT);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
			prnHEADER();
		}
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes ("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('L'," ",10));//margin
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",40));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY  ",40));
			//dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)  ",40));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"NOTE : ",7));
			cl_dat.M_intLINNO_pbst += 9;
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		    {   
				prnFMTCHR(dosREPORT,M_strEJT);
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
			}	
	}
	    catch(Exception L_EX)
	    {
			setMSG(L_EX  + " prnFOOTR",'E');
		}	
}
}
