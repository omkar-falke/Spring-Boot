/*
System Name   : Export Marketing Management System
Program Name  : Despatch Intimation Report
Program Desc. : Despatch Intimation Report

Author        : Mr S.R.Tawde
Date          : 25.03.2008
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
 
<b>Program Name :</b> Stuffing Schedule Report

<b>Purpose :</b> This module gives the statement of Stuffing Schedule between given (D.O.) dates and time.

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
public class mr_rpdin extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;	
	private JTextField txtFMTIM;
	private JTextField txtTOTIM;
	private JTextField txtNOTE1;
	private JTextField txtNOTE2;
	private JTextField txtNOTE3;
	private JTextField txtNOTE4;

	private JCheckBox chkCOMFL;
		
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
	mr_rpdin()
	{
		super(1);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("From Date"),5,4,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,5,1,1,this,'L');			
			add(txtFMTIM = new TxtLimit(8),5,6,1,1,this,'L');			
			add(new JLabel("To Date"),6,4,1,1.3,this,'L');
			add(txtTODAT = new TxtDate(),6,5,1,1,this,'L');
			add(txtTOTIM = new TxtLimit(8),6,6,1,1,this,'L');			
			add(chkCOMFL=new JCheckBox("With Commisison & Prices"),7,4,1,5,this,'L');
			//add(chkCOMFL = new JCheckBox(),7,5,1,1
			add(new JLabel("Note"),8,1,1,1,this,'L');
			add(txtNOTE1=new TxtLimit(135),8,2,1,7,this,'L');
			add(txtNOTE2=new TxtLimit(135),9,2,1,7,this,'L');
			add(txtNOTE3=new TxtLimit(135),10,2,1,7,this,'L');
			add(txtNOTE4=new TxtLimit(135),11,2,1,7,this,'L');
	

/**			M_strSQLQRY =  " Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS,CMT_CCSVL from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP||CMT_CGSTP in ( 'SYSFGXXRTP','MSTCOXXPGR','SYSFGXXPKG')";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
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
		txtTODAT.setEnabled(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);		
		if (L_flgSTAT==true )
		{
			if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
			{					 
				try
				{
					M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
					//M_calLOCAL.add(java.util.Calendar.DATE,-1);																
					//M_calLOCAL.getTime(txtFMTIM);
					//System.out.println(M_calLOCAL.getTime(txtFMTIM));
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
			txtFMTIM.requestFocus();
		if(M_objSOURC == txtFMTIM)
			txtTODAT.requestFocus();
		if(M_objSOURC == txtTODAT)			
			txtTOTIM.requestFocus();
		if(M_objSOURC == txtTOTIM)
			chkCOMFL.requestFocus();
		
		if(M_objSOURC == chkCOMFL)	
			txtNOTE1.requestFocus();
		
		if(M_objSOURC == txtNOTE1)
			txtNOTE2.requestFocus();
		else if(M_objSOURC == txtNOTE2)
			txtNOTE3.requestFocus();
		else if(M_objSOURC == txtNOTE3)
			txtNOTE4.requestFocus();
		else if(M_objSOURC == txtNOTE4)
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpdin.htm";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpdin.doc";				
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
					//M_calLOCAL.add(java.util.Calendar.DATE,-1);																					
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
		String L_strFMDAT,L_strTODAT,L_strFMTIM=null,L_strTOTIM=null;
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblDORQT = 0;
		double L_dblTOTQT = 0;
		//double L_dblRCTPK = 0;
		String L_strDSRCD = "";
		String L_strDSRNM = "";
		String L_strBYRCD = "";
		String L_strBYRNM = "";
		String L_strPRDCD = "";
		String L_strPRDDS = "";
		String L_strDSPDT = "";
		String L_strDTPDS = "";
		String L_strDSTDS = "";
		String L_strINDNO = "";
		String L_strOCFNO = "";
		String L_strDORNO = "";
		String L_strCURDS = "";
		String L_strCOMCT = "";
		String L_strCODCD = "";
		
		
		double L_dblBASRT = 0.00;
		String L_strCOMRT = "";
		
		try
	    {	
			
//			strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
//			strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
	        fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;
			cl_dat.M_PAGENO=0;	
            setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				//System.out.println("Format Char");
				//prnFMTCHR(O_DOUT,M_strCPI17);
			    //prnFMTCHR(dosREPORT,M_strNOCPI17);
			    //prnFMTCHR(dosREPORT,M_strCPI10);
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
			L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			L_strFMTIM = txtFMTIM.getText().trim();
			L_strTOTIM = txtTOTIM.getText().trim();
			//L_strFMTIM = TIME(parse(txtFMTIM.getText().trim()));
			//L_strTOTIM = TIME(parse(txtTOTIM.getText().trim()));
			
			setMSG("Report Generation is in Progress.......",'N');			   			
			
			/**
			 
			M_strSQLQRY = " Select IN_DSRCD,PT_PRTNM IN_DSRNM,IN_BYRCD,IN_BYRNM,INT_INDNO,INT_PRDCD,INT_PRDDS,DOT_DORQT-isnull(DOT_INVQT,0)-isnull(DOT_FCMQT,0) DOT_DORQT,";
			M_strSQLQRY+= " DOD_DSPDT,INT_OCFNO,DOT_DORNO,INT_BASRT,A.CMT_SHRDS IN_CURDS,C.CMT_CODDS IN_DTPDS,B.CMT_CODDS  IN_DSTDS ";
			M_strSQLQRY+= " from SPLDATA.VW_INTRN,SPLDATA.MR_DOTRN,SPLDATA.MR_OCTRN,SPLDATA.CO_CDTRN  A, SPLDATA.CO_CDTRN B,";
			M_strSQLQRY+= " SPLDATA.CO_CDTRN C,SPLDATA.MR_DODEL,SPLDATA.CO_PTMST where INT_INDNO=DOT_INDNO  AND DOT_AUTDT between '"+L_strFMDAT+"' AND '"+L_strTODAT+"'";
			M_strSQLQRY+= " and DOT_DORQT-isnull(DOT_INVQT,0)-isnull(DOT_FCMQT,0)>0  AND   INT_STSFL!='X'";
			M_strSQLQRY+= " AND IN_SALTP IN ('12')  AND OCT_OCFNO=INT_OCFNO AND OCT_PRDDS=INT_PRDDS  AND INT_PRDDS=DOT_PRDDS ";
			M_strSQLQRY+= " AND INT_PKGTP=OCT_PKGTP AND OCT_PKGTP=DOT_PKGTP  AND A.CMT_CGSTP='COXXCUR' AND A.CMT_CODCD=IN_CURCD ";
			M_strSQLQRY+= " and IN_DSTCD=B.CMT_CODCD AND B.CMT_CGSTP='MRXXPOD' AND C.CMT_CGSTP='COXXDTP' AND C.CMT_CODCD=IN_DTPCD ";
			M_strSQLQRY+= " AND DOD_DORNO=DOT_DORNO AND DOT_PRDCD=DOD_PRDCD AND DOT_PKGTP=DOD_PKGTP  AND DOD_DORQT-isnull(DOD_LADQT,0)>0  AND PT_PRTTP='D' AND PT_PRTCD=IN_DSRCD";
			M_strSQLQRY+= " order by DOT_DORNO ";
*/
			//M_strSQLQRY+= "BETWEEN '"+strFMDAT+"' AND '"+strTODAT+"'  AND IVT_BYRCD='"+txtBYRCD.getText()+"' AND ";

			M_strSQLQRY = " Select IN_DSRCD,PT_PRTNM IN_DSRNM,IN_BYRCD,IN_BYRNM,INT_INDNO,INT_PRDCD,INT_PRDDS,DOD_DORQT-isnull(DOD_LADQT,0) DOT_DORQT,";
			M_strSQLQRY+= " DOD_DSPDT,INT_OCFNO,DOT_DORNO,INT_BASRT,A.CMT_SHRDS IN_CURDS,C.CMT_CODDS IN_DTPDS,B.CMT_CODDS  IN_DSTDS ";
			M_strSQLQRY+= " from VW_INTRN,MR_DOTRN,MR_OCTRN,CO_CDTRN A,CO_CDTRN B,";
			M_strSQLQRY+= " CO_CDTRN C,MR_DODEL,CO_PTMST where INT_INDNO=DOT_INDNO  AND DOT_AUTDT between '"+L_strFMDAT+"' AND '"+L_strTODAT+"' AND DOT_AUTTM BETWEEN '"+L_strFMTIM+"' AND '"+L_strTOTIM+"'";
			M_strSQLQRY+= " and DOT_DORQT-isnull(DOT_INVQT,0)-isnull(DOT_FCMQT,0)>0  AND   INT_STSFL!='X'";
			M_strSQLQRY+= " AND IN_SALTP IN ('12')  AND OCT_OCFNO=INT_OCFNO AND OCT_PRDCD=INT_PRDCD  AND INT_PRDCD=DOT_PRDCD ";
			M_strSQLQRY+= " AND INT_PKGTP=OCT_PKGTP AND OCT_PKGTP=DOT_PKGTP  AND A.CMT_CGSTP='COXXCUR' AND A.CMT_CODCD=IN_CURCD ";
			M_strSQLQRY+= " and IN_DSTCD=B.CMT_CODCD AND B.CMT_CGSTP='MRXXPOD' AND C.CMT_CGSTP='MRXXDTP' AND C.CMT_CODCD=IN_DTPCD ";
			M_strSQLQRY+= " AND DOD_DORNO=DOT_DORNO AND DOT_PRDCD=DOD_PRDCD AND DOT_PKGTP=DOD_PKGTP  AND DOD_DORQT-isnull(DOD_LADQT,0)>0  AND DOT_DORQT-isnull(DOT_LADQT,0)-isnull(DOT_FCMQT,0)>0 AND PT_PRTTP='D' AND PT_PRTCD=IN_DSRCD AND DOD_CMPCD='01' and DOD_STSFL!='X'";
			M_strSQLQRY+= " order by DOD_DSPDT ";
			
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					while(!L_flgEOF)
					{
						L_strDSRCD = nvlSTRVL(M_rstRSSET.getString("IN_DSRCD"),"");
						L_strDSRNM = nvlSTRVL(M_rstRSSET.getString("IN_DSRNM"),"");
						L_strBYRCD = nvlSTRVL(M_rstRSSET.getString("IN_BYRCD"),"");
						L_strBYRNM = nvlSTRVL(M_rstRSSET.getString("IN_BYRNM"),"");
						L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("INT_PRDCD"),"");
						L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("INT_PRDDS"),"");
    					L_dblDORQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("DOT_DORQT"),"0"));
						L_strDSPDT = nvlSTRVL(M_rstRSSET.getString("DOD_DSPDT"),"");
						L_strDTPDS = nvlSTRVL(M_rstRSSET.getString("IN_DTPDS"),"");
						L_strDSTDS = nvlSTRVL(M_rstRSSET.getString("IN_DSTDS"),"");
						L_strINDNO = nvlSTRVL(M_rstRSSET.getString("INT_INDNO"),"");
						L_strOCFNO = nvlSTRVL(M_rstRSSET.getString("INT_OCFNO"),"");
						L_strDORNO = nvlSTRVL(M_rstRSSET.getString("DOT_DORNO"),"");
    					L_dblBASRT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("INT_BASRT"),"0"));
						L_strCURDS = nvlSTRVL(M_rstRSSET.getString("IN_CURDS"),"");
						L_dblTOTQT+= L_dblDORQT;
						if(L_strPRDCD.substring(0,2).equals("51")) 
							L_strCOMCT="07";
						else if(L_strPRDCD.substring(0,2).equals("52"))
							L_strCOMCT="08";
						else if(L_strPRDCD.substring(0,2).equals("54"))
							L_strCOMCT="09";
						L_strCODCD="D"+L_strDSRCD+"_"+L_strBYRCD+"_"+L_strCOMCT;	
						if(hstCOMRT.containsKey(L_strCODCD.trim()))
							L_strCOMRT  = hstCOMRT.get(L_strCODCD).toString();	
						else if(!hstCOMRT.containsKey(L_strCODCD.trim()))
						{	
							L_strCODCD="D"+L_strDSRCD+"_"+"XXXXX"+"_"+L_strCOMCT;	
							if(hstCOMRT.containsKey(L_strCODCD.trim()))
							{	
								L_strCOMRT = hstCOMRT.get(L_strCODCD.trim()).toString();
							}	
							else
								L_strCOMRT = "N.A.";
						}	
						
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
						}
				//	dosREPORT.writeBytes(padSTRING('L',"",4));
					dosREPORT.writeBytes(padSTRING('R',L_strBYRNM,22));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('R',L_strPRDDS,18));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDORQT,3),10));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('R',L_strDSPDT,10));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('R',L_strDTPDS,5));
					dosREPORT.writeBytes(padSTRING('R',L_strDSTDS,10));
					dosREPORT.writeBytes(padSTRING('L',"",3));
					dosREPORT.writeBytes(padSTRING('R',L_strINDNO,11));
					dosREPORT.writeBytes(padSTRING('R',L_strOCFNO,11));
					dosREPORT.writeBytes(padSTRING('R',L_strDORNO,11));

					if(chkCOMFL.isSelected() == true)
					{	
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblBASRT,2),10));
						dosREPORT.writeBytes(padSTRING('L',"",1));
						dosREPORT.writeBytes(padSTRING('R',L_strCURDS,3));
						dosREPORT.writeBytes(padSTRING('L',"",2));
					}	
					if(L_strCOMRT.equals("0.000"))
					{	
						L_strCOMRT="NIL";
						L_strDSRNM="";
					}	
					
					if(chkCOMFL.isSelected() == true)
						dosREPORT.writeBytes(padSTRING('R',L_strCOMRT,5));

					dosREPORT.writeBytes("\n");
   					cl_dat.M_intLINNO_pbst+= 1;						
					if(!L_strCOMRT.equals("NIL") && chkCOMFL.isSelected() == true)
					{	
						dosREPORT.writeBytes(padSTRING('L',"",136));
						dosREPORT.writeBytes(padSTRING('R',L_strDSRNM,10));
						dosREPORT.writeBytes("\n");
	   					cl_dat.M_intLINNO_pbst+= 1;						
					}	
						intRECCT+=1;
						if(!M_rstRSSET.next())
						{
							L_flgEOF = true;
							break;
						}
					}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes(padSTRING('L',"",43));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT,3),10));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
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
			dosREPORT.writeBytes(padSTRING('C',"I N T E R  O F F I C E  M E M O",120));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes("TO      : Mr.C.Das/Manoj                                                                                                    From : Umakant D.P."+"\n");
			dosREPORT.writeBytes("Ref.No. :                                                                                                                   Date : "+ cl_dat.M_strLOGDT_pbst + "\n");
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			dosREPORT.writeBytes(padSTRING('L',"Subject : Stuffing Schedule",50));
			dosREPORT.writeBytes(" for the DOs authorised between "+txtFMDAT.getText()+" "+txtFMTIM.getText()+" AND "+txtTODAT.getText()+" "+txtTOTIM.getText());
			dosREPORT.writeBytes("\n");										
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			//dosREPORT.writeBytes("Customer                 Grade                 Qty.     Stuffing    Terms            Indent No.  P.Invoice  D.O.No.        Price        Commission"+"\n");      
			dosREPORT.writeBytes("Customer                 Grade                 Qty.     Stuffing    Terms            Indent No.  P.Invoice  D.O.No.        ");
			if(chkCOMFL.isSelected()== true)
				dosREPORT.writeBytes("Price        Commission");

			dosREPORT.writeBytes("\n");										
			dosREPORT.writeBytes("                                                          Date                                                              "); 
			if(chkCOMFL.isSelected()== true)
				dosREPORT.writeBytes("/MT         To");

			dosREPORT.writeBytes("\n");										
			dosREPORT.writeBytes("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
			//dosREPORT.writeBytes("Receipt Type   \n");		    			
			//dosREPORT.writeBytes("    Finished Goods Category \n");
			//dosREPORT.writeBytes("        Product Category  \n");
			//dosREPORT.writeBytes("            Grade                         Package Type             Total Qty.    Total Pkgs. \n");
			//dosREPORT.writeBytes("----------------------------------------------------------------------------------------------\n");		
			cl_dat.M_intLINNO_pbst += 17;
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
			if(!txtNOTE1.getText().trim().equals(""))
			{	
				dosREPORT.writeBytes(padSTRING('R',txtNOTE1.getText().trim(),150));
				dosREPORT.writeBytes("\n");
			}
			if(!txtNOTE2.getText().trim().equals(""))
			{	
				dosREPORT.writeBytes(padSTRING('R',txtNOTE2.getText().trim(),150));
				dosREPORT.writeBytes("\n");
			}
			if(!txtNOTE3.getText().trim().equals(""))
			{	
				dosREPORT.writeBytes(padSTRING('R',txtNOTE3.getText().trim(),150));
				dosREPORT.writeBytes("\n");
			}
			if(!txtNOTE4.getText().trim().equals(""))
			{	
				dosREPORT.writeBytes(padSTRING('R',txtNOTE4.getText().trim(),150));
				dosREPORT.writeBytes("\n");
			}
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes(padSTRING('R',"Best Regards,",20));
			dosREPORT.writeBytes("\n\n\n");
			dosREPORT.writeBytes(padSTRING('R',"UMAKANT D.P.",20));
			dosREPORT.writeBytes("\n\n");
			dosREPORT.writeBytes("System generated report, hence signature not required \n");
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
