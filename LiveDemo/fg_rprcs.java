/*
System Name   : Finished Goods Inventory Management System
Program Name  : Reciept Summary Report
Program Desc. : Summary Details as per grade wise

Author        : Mr S.R.Tawde
Date          : 07.06.2005
Version       : FIMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
/**<pre>
<b>System Name :</b> Finished Goods Inventory Management System.
 
<b>Program Name :</b> Receipt Summary Report

<b>Purpose :</b> This module gives Gradewise Receipt Summary between given dates.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
FG_RCTRN    RCT_WHRTP,RCT_RCTTP,RCT_RCTNO,                          #
            RCT_PRDTP,RCT_LOTNO,RCT_RCLNO,
            RCT_PKGTP,RCT_MNLCD				
PR_LTMST    LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
CO_PRMST    PR_PRDCD                                                #
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table Name      Type/Size     Description
--------------------------------------------------------------------------------
txtFRMDT    RCT_RCTDT      FG_RCTRN        Date          From Date
txtTORDT    RCT_RCTDT      FG_RCTRN        Date          From Date
--------------------------------------------------------------------------------
<I>

<B>Conditions Give in Query:</b>
             Data is taken from FG_RCTRN,CO_PRMST,PR_LTMST between the given date range 
             Grouping is done by Receiptype,Main Product Category(viz.PS,SPS,MB etc.),
             SubProduct Category (viz.GPPS,HIPS,etc.),Actual Grade Description 
             (viz. SC203EL,SH731 etc.),Package Type (viz. 25 kg,950 kg etc.).
             1)RCT_PRDTP = LT_PRDTP.
             2)and RCT_LOTNO = LT_LOTNO.
             3)and RCT_RCLNO=LT_RCLNO.
             4)and LT_PRDCD=PR_PRDCD.
             5)and RCT_RCTTP in ('10','15','21','30'). 
             6)and RCT_STSFL not in ('X')
             7)Group by RCT_RCTTP,SUBSTRING(PR_PRDCD,1,2),SUBSTRING(PR_PRDCD,1,4),PR_PRDDS,RCT_PKGTP
</I>
Validations :
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
 */
public class fg_rprcs extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;		
	private int intRCTPK = 0;
	
										/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;	
												 /** Hashtable for storing different Receipt Types */  
	private Hashtable<String,String> hstRCTTP = new Hashtable<String,String>();/** Hashtable for storing different Main Product Types*/
	private Hashtable<String,String> hstMNGRP = new Hashtable<String,String>();/** Hashtable for storing different Sub Product Types*/
	private Hashtable<String,String> hstSBGRP = new Hashtable<String,String>();/** Hashtable for storing different Package Types*/
    	private Hashtable<String,String> hstPKGTP = new Hashtable<String,String>();
	
	
	/**
	 *1.Screen Designing
	 *2.Hashtable is created using CO_CDTRN for maintaining various types of codes alongwith
	 *  their descriptions.
	 */
	fg_rprcs()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("From Date"),5,4,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,5,1,1,this,'L');			
			add(new JLabel("To Date"),6,4,1,1.3,this,'L');
			add(txtTODAT = new TxtDate(),6,5,1,1,this,'L');

			M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP in ( 'SYSFGXXRTP','MSTCOXXPGR','SYSFGXXPKG')";
			M_strSQLQRY += " and  isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
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
		txtTODAT.setEnabled(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);		
		if (L_flgSTAT==true )
		{
			if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
			{					 
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));																					
       				txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));				            														
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" setENBL",'E');
				}
			}
			else 
			{
				if (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				{
					txtTODAT.setText("");
					txtFMDAT.setText("");			
				}
			}
		}
	}		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
		if(M_objSOURC == txtFMDAT)			
			txtTODAT.requestFocus();
		else if(M_objSOURC == txtTODAT)			
			cl_dat.M_btnSAVE_pbst.requestFocus();
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"fg_rprcs.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "fg_rprcs.doc";				
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
					  setMSG("No data found, Please check Date Range OR Excise Category ..",'E');				    
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
		    {
			    cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Gradewise Receipt Summary Report"," ");
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
			else if(txtTODAT.getText().trim().toString().length() == 0)			
			{	
				setMSG("Please Enter valid To-Date To Specify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}					
			else if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Please Enter valid To-Date, To Specify Date Range .. ",'E');				
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtTODAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtTODAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX+" vldDATA",'E');
				}	
			}	
			else if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)			
			{			    
				setMSG("Please Note that From-Date must be Greater than To-Date .. ",'E');								
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime())); 
					txtFMDAT.requestFocus();
					return false;
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX + " vldData", 'E');
				}																
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
		String L_strFMDAT,L_strTODAT;
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblRCTQT = 0;
		double L_dblRCTPK = 0;
		
		/** Variables declared for storing rceipt quantity and packages at different levels
		 */
		double L_dblRCRQT = 0;
		double L_dblRCPQT = 0;
		double L_dblMNRQT = 0;
		double L_dblMNPQT = 0;
		double L_dblSBRQT = 0;
		double L_dblSBPQT = 0;
		double L_dblGTRQT = 0;
		double L_dblGTPQT = 0;

		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPRCTTP = new String();
		String L_strPMNPRD = new String();
		String L_strPSBPRD = new String();
		String L_strPPRDDS = new String();
		String L_strPPKGTP = new String();

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCRCTTP = new String();
		String L_strCMNPRD = new String();
		String L_strCSBPRD = new String();
		String L_strCPRDDS = new String();
		String L_strCPKGTP = new String();

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strRCTTP = new String();
		String L_strMNPRD = new String();
		String L_strSBPRD = new String();
		String L_strPRDDS = new String();
		String L_strPKGTP = new String();
	/** Variables declared for storing current and previous code description
	 */	
		String L_strRCTDS="",L_strMPRDS="",L_strSPRDS="",L_strPKGDS="";
		String L_strPRCTDS="",L_strPMPRDS="",L_strPSPRDS="";
		String L_strMNPRD1="";
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
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    //prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Gradewise Receipt Summary Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();			
    		
			L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));		
			setMSG("Report Generation is in Progress.......",'N');			   			
			
	        M_strSQLQRY = "Select RCT_RCTTP,PR_PRDTP,PR_PRDCD,PR_PRDDS,SUBSTRING(PR_PRDCD,1,2) L_MNPRD,SUBSTRING(PR_PRDCD,1,4) L_SBPRD,";
			M_strSQLQRY += " RCT_PKGTP,sum(RCT_RCTQT) L_dblRCTQT,sum(RCT_RCTPK) intRCTPK from FG_RCTRN,PR_LTMST,CO_PRMST";
			M_strSQLQRY += " where RCT_CMPCD=LT_CMPCD and RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and RCT_RCLNO=LT_RCLNO and ltrim(str(LT_PRDCD,20,0))=PR_PRDCD";
			M_strSQLQRY += " and RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_RCTDT between '"+ L_strFMDAT +"' and '"+ L_strTODAT +"'";
			M_strSQLQRY += " and RCT_RCTTP in ('10','15','21','22','23','30') and  isnull(RCT_STSFL,'') <> 'X'";
			M_strSQLQRY += " group by RCT_RCTTP,PR_PRDTP,PR_PRDCD,SUBSTRING(PR_PRDCD,1,2),SUBSTRING(PR_PRDCD,1,4),PR_PRDDS,RCT_PKGTP ";
			M_strSQLQRY += " order by RCT_RCTTP,PR_PRDTP,PR_PRDCD,RCT_PKGTP ";
			/*
				M_strSQLQRY = "Select RCT_RCTTP,PR_PRDDS,SUBSTRING(PR_PRDCD,1,2) L_MNPRD,SUBSTRING(PR_PRDCD,1,4) L_SBPRD,";
				M_strSQLQRY += " RCT_PKGTP,sum(RCT_RCTQT) L_dblRCTQT,sum(RCT_RCTPK) intRCTPK from FG_RCTRN,PR_LTMST,CO_PRMST";
				M_strSQLQRY += " where RCT_PRDTP=LT_PRDTP and RCT_LOTNO=LT_LOTNO and RCT_RCLNO=LT_RCLNO and LT_PRDCD=PR_PRDCD";
				M_strSQLQRY += " and RCT_RCTDT between '"+ L_strFMDAT +"' and '"+ L_strTODAT +"'";
				M_strSQLQRY += " and RCT_RCTTP in ('10','15','21','22','23','30') and RCT_STSFL not in ('X')";
				String L_DBSSTR = " RCT_RCTTP,L_MNPRD,L_SBPRD,PR_PRDDS,RCT_PKGTP";
				M_strSQLQRY += " group by RCT_RCTTP,SUBSTRING(PR_PRDCD,1,2),SUBSTRING(PR_PRDCD,1,4),PR_PRDDS,RCT_PKGTP ";
				M_strSQLQRY += " order by RCT_RCTTP,L_MNPRD,L_SBPRD,PR_PRDDS,RCT_PKGTP ";
			*/
			System.out.println(M_strSQLQRY);
			double[] dbaXPSDT = new double[2]; 
			double dblMTCQT = 0.000;
			double dblMTTQT = 0.000;
			String L_strMTCQT0 = "",L_strMTTQT0 = "";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					while(!L_flgEOF)
					{
						L_strRCTTP = nvlSTRVL(M_rstRSSET.getString("RCT_RCTTP"),"");
						L_strMNPRD = nvlSTRVL(M_rstRSSET.getString("L_MNPRD"),"");
						L_strMNPRD1 = nvlSTRVL(M_rstRSSET.getString("L_MNPRD"),"");
						L_strSBPRD = nvlSTRVL(M_rstRSSET.getString("L_SBPRD"),"");
						L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");
						L_strPKGTP = nvlSTRVL(M_rstRSSET.getString("RCT_PKGTP"),"");
    					L_dblRCTQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_dblRCTQT"),"0"));
    					L_dblRCTPK = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("intRCTPK"),"0"));
						
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
					}
					if(L_flg1STSFL)
					{
						L_strPRCTTP = L_strRCTTP;
						L_strRCTDS  = hstRCTTP.get(L_strRCTTP).toString();	
						L_strPRCTDS = L_strRCTDS;
						L_strPMNPRD = L_strMNPRD;
						L_strMPRDS  = hstMNGRP.get(L_strMNPRD).toString();
						L_strPMPRDS = L_strMPRDS;
						L_strPSBPRD = L_strSBPRD;
						L_strSPRDS  = hstSBGRP.get(L_strSBPRD).toString();
						L_strPSPRDS = L_strSPRDS;
						L_strPPRDDS = L_strPRDDS;
						L_strPPKGTP = L_strPKGTP;
						L_strPKGDS  = hstPKGTP.get(L_strPKGTP).toString();
                        
						L_strCRCTTP = L_strRCTTP;
						L_strCMNPRD = L_strMNPRD;
						L_strCSBPRD = L_strSBPRD;
						L_strCPRDDS = L_strPRDDS;
						L_strCPKGTP = L_strPKGTP;
						L_flg1STSFL = false;
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes(L_strRCTDS+"\n");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
   					cl_dat.M_intLINNO_pbst+= 1;
					//System.out.println("Receipt type "+L_strRCTTP);
					
					while(L_strRCTTP.equals(L_strPRCTTP) && !L_flgEOF)
					{
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
						}
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes(padSTRING('L',"",4));
						dosREPORT.writeBytes(L_strMPRDS+"\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
   						cl_dat.M_intLINNO_pbst+= 1;
						L_strPMNPRD = L_strMNPRD;
						//System.out.println(" L_strPMNPRD = "+L_strMNPRD);
						while((L_strRCTTP+L_strMNPRD).equals(L_strPRCTTP+L_strPMNPRD) && !L_flgEOF)
						{
							if(cl_dat.M_intLINNO_pbst >= 64)
							{	
								dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							dosREPORT.writeBytes(padSTRING('L',"",8));
							dosREPORT.writeBytes(L_strSPRDS+"\n");
	   						cl_dat.M_intLINNO_pbst+= 1;
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
							dosREPORT.writeBytes(padSTRING('L',"",12));
							L_strMNPRD = L_strCMNPRD;
							L_strPMNPRD = L_strMNPRD;
							//System.out.println(" (L_strRCTTP+L_strMNPRD+L_strSBPRD).equals("+L_strPRCTTP+L_strPMNPRD+L_strPSBPRD);
							while((L_strRCTTP+L_strMNPRD+L_strSBPRD).equals(L_strPRCTTP+L_strPMNPRD+L_strPSBPRD) && !L_flgEOF)
							{
								if(cl_dat.M_intLINNO_pbst >= 64)
								{	
									dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
									prnHEADER();
								}
								dosREPORT.writeBytes(padSTRING('R',L_strPRDDS,30));
								L_strMNPRD  = L_strCMNPRD;
								L_strSBPRD  = L_strCSBPRD;
								L_strPMNPRD = L_strMNPRD;
								L_strPSBPRD = L_strSBPRD;
								while((L_strRCTTP+L_strMNPRD+L_strSBPRD+L_strPRDDS).equals(L_strPRCTTP+L_strPMNPRD+L_strPSBPRD+L_strPPRDDS) && !L_flgEOF)
								{
									dosREPORT.writeBytes(padSTRING('R',L_strPKGDS,21));
  									intRECCT++;								
									L_strMNPRD  = L_strCMNPRD;
									L_strSBPRD  = L_strCSBPRD;
									L_strPRDDS  = L_strCPRDDS;
									L_strPMNPRD = L_strMNPRD;
									L_strPSBPRD = L_strSBPRD;
									L_strPPRDDS = L_strPRDDS;								
									while((L_strRCTTP+L_strMNPRD+L_strSBPRD+L_strPRDDS+L_strPKGTP).equals(L_strPRCTTP+L_strPMNPRD+L_strPSBPRD+L_strPPRDDS+L_strPPKGTP) && !L_flgEOF)
									{
										L_strMTCQT0 = "";L_strMTTQT0 = "";
										if(M_rstRSSET.getString("L_MNPRD").equals("SX"))
										{
											dbaXPSDT[0]=0; dbaXPSDT[1]=0;
											dbaXPSDT = getXPSDT(L_dblRCTPK,M_rstRSSET.getString("PR_PRDCD"),M_rstRSSET.getString("RCT_PKGTP"));
											//System.out.println("out>>"+dbaXPSDT[0]+"|"+dbaXPSDT[1]);
											L_strMTCQT0 = setNumberFormat(dbaXPSDT[0],3);
											L_strMTTQT0 = setNumberFormat(dbaXPSDT[1],3);
											dblMTCQT += dbaXPSDT[0];
											dblMTTQT += dbaXPSDT[1];
										}
		    							dosREPORT.writeBytes(padSTRING('L',L_strMTCQT0,10));
		    							dosREPORT.writeBytes(padSTRING('L',L_strMTTQT0,10));
		    							dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCTQT,3),14));
										dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCTPK,0),14));
										dosREPORT.writeBytes("\n");   
										cl_dat.M_intLINNO_pbst+= 1;
										if(cl_dat.M_intLINNO_pbst >= 64)
										{	
											dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
											if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
											prnFMTCHR(dosREPORT,M_strEJT);
											if(M_rdbHTML.isSelected())
											dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
											prnHEADER();
										}
										L_dblRCRQT+=  L_dblRCTQT;
										L_dblRCPQT+=  L_dblRCTPK;
										L_dblMNRQT+=  L_dblRCTQT;
										L_dblMNPQT+=  L_dblRCTPK;
										L_dblSBRQT+=  L_dblRCTQT;
										L_dblSBPQT+=  L_dblRCTPK;
										L_dblGTRQT+=  L_dblRCTQT;
										L_dblGTPQT+=  L_dblRCTPK;
										if(!M_rstRSSET.next())
										{
											L_flgEOF = true;
												break;
										}
										//System.out.println("Before HashTable");
										L_strCRCTTP = M_rstRSSET.getString("RCT_RCTTP").trim();
										if(hstRCTTP.containsKey(L_strCRCTTP))
											L_strRCTDS  = hstRCTTP.get(L_strCRCTTP).toString();	
										else
											L_strRCTDS=L_strCRCTTP;
										L_strCMNPRD = M_rstRSSET.getString("L_MNPRD").trim();
										if(hstMNGRP.containsKey(L_strCMNPRD))
											L_strMPRDS  = hstMNGRP.get(L_strCMNPRD).toString();
										else
											L_strMPRDS=L_strCMNPRD;
										
										L_strCSBPRD = M_rstRSSET.getString("L_SBPRD").trim();
										if(hstSBGRP.containsKey(L_strCSBPRD))
											L_strSPRDS  = hstSBGRP.get(L_strCSBPRD).toString();
										else
											L_strSPRDS=L_strCSBPRD;
										L_strCPRDDS = M_rstRSSET.getString("PR_PRDDS").trim();
										L_strCPKGTP = M_rstRSSET.getString("RCT_PKGTP").trim();
										if(hstPKGTP.containsKey(L_strCPKGTP))
											L_strPKGDS  = hstPKGTP.get(L_strCPKGTP).toString();
										else
											L_strPKGDS=L_strCPKGTP;
										
				    					L_dblRCTQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("L_dblRCTQT"),"0"));
				    					L_dblRCTPK = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("intRCTPK"),"0"));
									
										L_strRCTTP = L_strCRCTTP;
										L_strMNPRD = L_strCMNPRD;
										L_strSBPRD = L_strCSBPRD;
										L_strPRDDS = L_strCPRDDS;
										L_strPKGTP = L_strCPKGTP;
									}		
									if((L_strRCTTP+L_strMNPRD+L_strSBPRD+L_strPRDDS).equals(L_strPRCTTP+L_strPMNPRD+L_strPSBPRD+L_strPPRDDS) && L_strPKGTP != L_strPPKGTP)
											dosREPORT.writeBytes(padSTRING('L',"",42));
									L_strPPKGTP = L_strPKGTP;
										
								}	
								dosREPORT.writeBytes(padSTRING('L',"",12));
								L_strPPRDDS = L_strPRDDS;									
							}	
							if(cl_dat.M_intLINNO_pbst >= 64)
							{	
								dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}
							//dosREPORT.writeBytes("\n");
				   			//cl_dat.M_intLINNO_pbst+= 1;
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<B>");
							//dosREPORT.writeBytes(padSTRING('L',"",8));
							dosREPORT.writeBytes(padSTRING('R',"Total "+L_strPSPRDS,51));
							
							if((dblMTCQT+dblMTTQT)>0)
							{
		    					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblMTCQT,3),10));
		    					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblMTTQT,3),10));
		    				}
		    				else
		    					dosREPORT.writeBytes(padSTRING('L'," ",20));
		    				
		    				
		    				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSBRQT,3),14));
		    				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblSBPQT,0),14)+"\n");
				   			cl_dat.M_intLINNO_pbst+= 1;
							if((dblMTCQT+dblMTTQT)>0)
							{
								dosREPORT.writeBytes(padSTRING('R'," ",63));
		    					dosREPORT.writeBytes(padSTRING('L',"Mt.Cube",10));
		    					dosREPORT.writeBytes(padSTRING('L',"MT",10));
		    					dosREPORT.writeBytes(padSTRING('L',"Sq.Mtr",14));
		    					dosREPORT.writeBytes(padSTRING('L',"Pcs",14)+"\n");
				   				cl_dat.M_intLINNO_pbst+= 1;
		    					//dblMTCQT = 0;dblMTTQT = 0;
		    					
		    				}
		    				else
		    				{
		    					dosREPORT.writeBytes("\n");
				   				cl_dat.M_intLINNO_pbst+= 1;
				   			}
		    				
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strNOBOLD);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("</B>");
		    				dblMTCQT = 0;
		    				dblMTTQT = 0;
								
							L_dblSBRQT = 0;
							L_dblSBPQT = 0;
							L_strPSBPRD = L_strSBPRD;
							L_strPSPRDS = L_strSPRDS;
						}	
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
						}
						dosREPORT.writeBytes("\n");// 
						cl_dat.M_intLINNO_pbst+= 1;
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<B>");
						dosREPORT.writeBytes(padSTRING('L',"",4));
						dosREPORT.writeBytes(padSTRING('R',"Total "+L_strPMPRDS,79));
		    			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblMNRQT,3),14));
		    			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblMNPQT,0),14)+"\n\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						cl_dat.M_intLINNO_pbst+= 2;
						L_dblMNRQT = 0;
						L_dblMNPQT = 0;
						L_strPMNPRD = L_strMNPRD;
						L_strPMPRDS = L_strMPRDS;
					}
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("----------------------------------------------------------------------------------------------");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
						prnHEADER();
					}
					dosREPORT.writeBytes("\n");   
					cl_dat.M_intLINNO_pbst+= 1;
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<B>");
					dosREPORT.writeBytes(padSTRING('R',"Total "+L_strPRCTDS,83));  
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCRQT,3),14));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblRCPQT,0),14)+"\n\n");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
					cl_dat.M_intLINNO_pbst+= 2;
					L_dblRCRQT = 0;
					L_dblRCPQT = 0;
					L_strPRCTTP = L_strRCTTP;
					L_strPRCTDS = L_strRCTDS;
				}
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Grand Total ",83)); 
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTRQT,3),14));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTPQT,0),14)+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				cl_dat.M_intLINNO_pbst+= 1;
				L_dblGTRQT = 0;
				L_dblGTPQT = 0;
										
				prnFOOTR();
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
	
	public double[] getXPSDT(double LP_PKGQT, String LP_PRDCD,String LP_PKGTP)
	{
		double[] LP_dbaXPSDT = new double[2];
		try
		{
			//LP_staXPSDT[0] = LP_PRDCD.substring(2,4);
			//System.out.println("PRDCD>>"+LP_PRDCD.substring(2,4));
			String L_strSQLQRY = "select cmt_nmp01,cmt_nmp02,cmt_chp02 from spldata.co_cdtrn where cmt_cgstp='FGXXPKG' and cmt_codcd='"+LP_PKGTP+"'";
			//System.out.println(L_strSQLQRY);
			java.sql.ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			double L_dblMTCQT = 0.000;
			double L_dblMTTQT = 0.000;
			if(L_rstRSSET != null)
			{	
				if(L_rstRSSET.next())
				{
					//LP_staXPSDT[1] = L_rstRSSET.getString("cmt_nmp01");
					//LP_staXPSDT[2] = L_rstRSSET.getString("cmt_nmp02");
					//LP_staXPSDT[3] = L_rstRSSET.getString("cmt_chp02");
					//System.out.println("1.."+L_rstRSSET.getString("cmt_nmp01"));
					//System.out.println("2.."+L_rstRSSET.getString("cmt_nmp02"));	
					//System.out.println("3.."+L_rstRSSET.getString("cmt_chp02"));	
					LP_dbaXPSDT[0] = LP_PKGQT * ((Double.parseDouble(L_rstRSSET.getString("cmt_chp02")) *Double.parseDouble(L_rstRSSET.getString("cmt_nmp01")) * Double.parseDouble(L_rstRSSET.getString("cmt_nmp02")))/(1000*1000*1000));
					LP_dbaXPSDT[1] = (Double.parseDouble(LP_PRDCD.substring(2,4))*LP_dbaXPSDT[0])/1000;
				}
			}
		}
		catch(Exception L_EX)
	    {
			setMSG(L_EX  + " getXPSDT",'E');
		}
		return LP_dbaXPSDT;
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
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI17);
			dosREPORT.writeBytes("\n\n\n\n\n");										
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst ,90));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Gradewise Receipt Summary Report between " +txtFMDAT.getText().trim()+ " and " + txtTODAT.getText().trim(),90));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("Receipt Type   \n");		    			
			dosREPORT.writeBytes("    Finished Goods Category \n");
			dosREPORT.writeBytes("        Product Category  \n");
			dosREPORT.writeBytes("            Grade                         Package Type                                 Total Qty.    Total Pkgs. \n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 14;
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
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------");		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strEJT);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
			prnHEADER();
		}
		dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes ("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('L'," ",10));//margin
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"PREPARED BY",20));
			dosREPORT.writeBytes(padSTRING('L',"CHECKED BY  ",35));
			dosREPORT.writeBytes(padSTRING('L',"H.O.D (MHD)  ",40));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------\n");		
			dosREPORT.writeBytes("\n System generatede report, hence signature not required \n");
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
