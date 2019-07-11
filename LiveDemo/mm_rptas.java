/*
System Name   : Material Management System
Program Name  : Report - Time Analysis Sheet
Program Desc. :
Author        : Mr.S.R.Mehesare
Date          : 16 Mar 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.sql.Timestamp;
import java.util.StringTokenizer;
/**<pre>
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Time Analysis sheet

<b>Purpose :</b> This module gives information about time required by Vehicle for 
various activities.

List of tables used :
Table Name    Primary key                            Operation done
                                                Insert   Update   Query   Delete	
-------------------------------------------------------------------------------------
MM_WBTRN      WB_DOCTP,WB_DOCNO,WB_SRLNO                            #	     
MR_IVTRN      IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP               #	     
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD                         #  
-------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name           Table name     Type/Size      Description
-------------------------------------------------------------------------------------
cmbGINTP    WB_GINTP              MM_WBTRN       Varchar(2)	   Gate-In Type	
txtFRMDT    WB_GINDT/WB_GOTDT     MM_WBTRN       Timestamp	   From Date
            (Tanker/Despatch) 
txtTORDT    WB_GINDT/WB_GOTDT     MM_WBTRN       Timestamp	   To Date
            (Tanker/Despatch)
-------------------------------------------------------------------------------------

<B><I>
Tables Used For Query :</b> 1)MM_WBTRN      
                        2)MR_IVTRN 
                        3)CO_CDTRN 
<B>Conditions Give in Query:</b>
   For Despatch & Domastic Data is taken from MM_WBTRN & MR_IVTRN for folloing condition
      1)WB_DOCTP = Selected gate In Type.
      2)and IVT_GINNO = WB_DOCNO 
      3)and IVT_SALTP = Invoice Sale Type (IVT_SALTP)
      4)and WB_STSFL = '9'
      5)and WB_GOTDT in given Date-Time Range.
      6)group by WB_DOCNO,WB_LRYNO,WB_TPRDS,WB_REPTM,WB_GINDT,WB_GOTDT,IVT_SALTP,wb_reptm </I>

<b>Validations :</b>
  - Gate-In Type is identified as follows
       01 - Tanker
       02 - Stores & Spares
       03 - Despatch
       04 - Others
  - Date-Time must be smaller than Current Date-Time.
  - From-Date-Time must be smaller than To-Date-Time.
 */

class mm_rptas extends cl_rbase
{									/** JTextField to accept & display From Date.*/
	JTextField txtFMDAT;			/** JTextField to accept & display To Date.*/
	JTextField txtTODAT;    		/** JTextField to accept & display From-Date-Time.*/
	JTextField txtTOTIM;			/** JTextField to accept & display To-Date-Time.*/
	JTextField txtFMTIM;			/** JTextField to accept & display Deliverty Type */
	JTextField txtDTPCD;			/** JComboBox to select Gate-In Type.*/
    JComboBox cmbGINTP;             /** String variable for File Name.*/
    private String strFILNM;        /** String variable for Gate in Type.*/	
	private String strGINTP;        /** String variable for From Date.*/
	private String strFMDAT;        /** String variable for To Date.*/
	private String strTODAT;        /** String variable for Store Type.*/
	private String strSTRTP;        /** String variable for Delivery Type Code.*/
	private String strDTPCD = "";        /** String variable for Delivery Type Description*/
	private String strDTPDS = "";        /** Integer variable to count the Records fetched.*/
	private int intRECCT;           /** Integer variable for Serial Number.*/
	private int intSRLNO = 0;	    /** Array of String to store number of Days in each Month.*/
	private static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	
	                                /** String variable for Get-In Number.*/
	private String strGINNO;        /** String variable for Lorry Number.*/
	private String strLRYNO;        /** String variable for Transporter Description.*/
	private String strTPRDS;        /** String variable for Gate In Date.*/
	private String strGINDT;        /** String variable for Party Description.*/
	private String strPRTDS;        /** String variable for Order Number.*/	
	private String strORDNO;        /** String variable for Order Date.*/	
	private String strORDDT;        /** String variable for Ordered Quantity.*/	
	private String strORDQT;        /** String variable for Material Description.*/	
	private String strMATDS;        /** String variable for Gate_Out Date.*/
	private String strGOTDT;        /** String variable for Status.*/
	private String strSTATS;        /** String variable for load Date.*/
	private String strLADDT;        /** String variable for In_Time*/
	private String strINCTM;        /** String variable for allocated Date & Time.*/
	private String strALODT;        /** String variable for Load Time.*/
	private String strLODDT;        /** String variable for out Time.*/
	private String strOUTTM;        /** String variable for Invoice Date.*/
	private String strINVDT;        /** String variable for Sample Time.*/
	private String strSMPTM;        /** String variable for Test Time*/
	private String strTSTTM;        /** String variable for total Time.*/		
	private String strTOTTM;        /** String variable for acceptable Target Time.*/
	private String strACPTM;        /** String variable for reporting Time.*/
	private String strREPTM;        /** String variable for old Gate In Number.*/
	private String strOGINNO;       /** String variable for old Lorry Number.*/
	private String strOLRYNO="";    /** String variable for old Gate In Date.*/
	private String strOGINDT;       /** String variable for old Loading Date.*/
	private String strOLADDT;       /** String variable for In-Time*/
	private String strOINCTM;       /** String variable for old load Date.*/
	private String strOLODDT;       /** String variable for old out Time.*/	
	private String strOOUTTM;       /** String variable for old Invoice Time.*/
	private String strOINVDT;		/** String variable for old Gate-out Date.*/
	private String strOGOTDT;       /** String variable for old allocation Date & Time. */	
	private String strOALODT;       /** String variable for old Reporting Time.*/
	private String strOREPTM;       /** String variable forold Transporter Description*/
	private String strOTPRDS;       /** String variable for Generating Data part of the Report.*/
	private String strPRNSTR;       /** Integer variable for */ 	
private int intDUPCT;			/** Integer variable for */
private int intTOTCT;			/** Integer variable for */
private int intDEFCT;			/** Integer variable to count Delayed Vehicles.*/
private int intDLYCT;			/** String variable for Tanker type Code Number.*/	
	final String strTNKTP = "01";	/** String variable for Despatch type Code Number.*/		
	final String strDSPTP = "03";	/** Integer variable for Max time required for Tanker Processing. */		
	final int intTNKTM = 500;		/** Integer variable for Max time required for Despatch Processing. */		
	final int intDSPTM = 300;		/** String variable for Export Sale Code.*/	
	final String strEXPSL = "12";	
	
	private String strF_C;
        final String strSLBSTR0 = "Before 08:30 Hrs";
        final String strSLBSTR1 = "Between 08:30 - 13:00 Hrs";                              
        final String strSLBSTR2 = "Between 13:00 - 17:00 Hrs";
        final String strSLBSTR3 = "After 17:00 Hrs";			/** Integer variable for Time slab 1.*/
        final int intSLBTM1 =  830;                             /** Integer variable for Time slab 2.*/
        final int intSLBTM2 = 1300;                              /** Integer variable for Time slab 3.*/ 
        final int intSLBTM3 = 1700;         /** Integer variable for Maximum Time for Export Vehicle Reporting.*/                    
	final int intEXPTM = 1400;				/** Integer variable for Maximum Time for Domestic Vehicle Reporting.*/
	final int intDOMDT = 1400;				
        private boolean flgSLBCHO = false;
        private boolean flgSLBCH1 = false;
        private boolean flgSLBCH2 = false;
        private boolean flgSLBCH3 = false;
        private boolean flgSLBCHX = false;				/** Integer variable to check Reporting Time.*/
     private int intRPTCHK;
     private int intSLBCT0 = 0;
     private int intSLBCT1 = 0;
     private int intSLBCT2 = 0;
     private int intSLBCT3 = 0;	
        private String[] arrTOTM7 = new String[4];		
        private String[] arrAVTM7 = new String[4];
                                						/** String array for Total Time.*/
        private String[] arrTOTM1 = new String[4];		/** String array for Total Time.*/
        private String[] arrTOTM2 = new String[4];		/** String array for Total Time.*/
        private String[] arrTOTM3 = new String[4];		/** String array for Total Time.*/
        private String[] arrTOTM4 = new String[4];		/** String array for Total Time.*/
        private String[] arrTOTM5 = new String[4];		/** String array for Total Time.*/
        private String[] arrTOTM6 = new String[4];		/** String array for Average Time.*/
        private String[] arrAVTM1 = new String[4];		/** String array for Average Time.*/
        private String[] arrAVTM2 = new String[4];		/** String array for Average Time.*/
        private String[] arrAVTM3 = new String[4];		/** String array for Average Time.*/
        private String[] arrAVTM4 = new String[4];		/** String array for Average Time.*/
        private String[] arrAVTM5 = new String[4];		/** String array for Average Time.*/
        private String[] arrAVTM6 = new String[4];   /** Result Set object for Database operations.*/
		ResultSet M_rstRSSET1;		/** FileOutputStream for generated report file handling.*/
		FileOutputStream fosREPORT;	/** DataOutputStream object to generate report file.*/
		DataOutputStream dosREPORT;		
	
	mm_rptas()
	{
		super(2); 
		try
		{							
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Gate-in Type"),4,4,1,1,this,'L');
			add(cmbGINTP=new JComboBox(),4,5,1,1.7,this,'L');			
		    add(new JLabel("From Date"),5,4,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),5,5,1,1,this,'L');
			add(txtFMTIM = new TxtTime(),5,6,1,.7,this,'L');
			add(new JLabel("To Date"),6,4,1,1,this,'L');
			add(txtTODAT = new TxtDate(),6,5,1,1,this,'L');
			add(txtTOTIM = new TxtTime(),6,6,1,.7,this,'L');						
			add(new JLabel("Del.Type"),7,4,1,1,this,'L');
			add(txtDTPCD = new TxtLimit(2),7,5,1,0.5,this,'L');
			M_pnlRPFMT.setVisible(true);		    
		        M_strSQLQRY =  " Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			    M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'";
			    M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
                M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
                if(M_rstRSSET != null)    			        		
		        {
		            cmbGINTP.addItem("Select Gate-In Type");
        		    while(M_rstRSSET.next())
        		    {        		         
        			     cmbGINTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));        			    
        		    }
        		    M_rstRSSET.close();
                }        			   
			setENBL(false);	  
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}	
    }

	
	/** 
     * Super class method overrided to inhance its funcationality, to enable & disable the 
     * components according to Requriement.
     */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);		
		txtTODAT.setEnabled(L_flgSTAT);		
		txtFMDAT.setEnabled(L_flgSTAT);
		txtTOTIM.setEnabled(L_flgSTAT);
		txtFMTIM.setEnabled(L_flgSTAT);
		if (L_flgSTAT==true)
		{
	        if (((txtTODAT.getText().trim()).length()== 0) ||((txtFMDAT.getText().trim()).length()==0))
			{
				if (cmbGINTP.getSelectedIndex()==0)
				{
					txtTODAT.setEnabled(false);		
					txtFMDAT.setEnabled(false);
					txtTOTIM.setEnabled(false);
					txtFMTIM.setEnabled(false);
				}
				else
				{
					txtTODAT.setText(cl_dat.M_strLOGDT_pbst);															
					txtFMDAT.setText(calRQDAT(cl_dat.M_strLOGDT_pbst));					
					txtTOTIM.setText("07:00");
					txtFMTIM.setText("07:00");
				}								
			}			 
		}
		else 
		{			
			if ((cmbGINTP.getSelectedIndex()==0) || (cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0))
			{
				txtTODAT.setText("");
				txtFMDAT.setText("");
				txtTOTIM.setText("");
				txtFMTIM.setText("");
			}
		}			
	}					

	
	/**
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
		int L_intKEYCD=L_KE.getKeyCode();
		try
		{
			if(M_objSOURC==txtDTPCD)
			{
				M_strHLPFLD = "txtDTPCD";
				if(L_KE.getKeyCode()==L_KE.VK_F1)
				{
					String L_strSQLQRY = "Select cmt_codcd, cmt_codds from co_cdtrn where cmt_cgmtp = 'SYS' and cmt_cgstp = 'MRXXDTP' order by CMT_CODCD";
					//System.out.println(L_strSQLQRY);
					cl_hlp(L_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");//	
				}
			}
		this.setCursor(cl_dat.M_curDFSTS_pbst);updateUI();
		}catch(Exception e)	{setMSG(e,"Child.KeyPressed");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}
			
	
	/**
	 */
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtDTPCD"))
				txtDTPCD.setText(L_STRTKN.nextToken());
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"Child.exeHLPOK");
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
			cl_dat.M_btnSAVE_pbst.requestFocus();			
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
			cl_dat.M_PAGENO=0;
		if(M_objSOURC == cmbGINTP)
		{
		    if (cmbGINTP.getSelectedIndex()!=0)   
		    {
				setMSG("",'N');
		       setENBL(true);		       
		       strGINTP=cmbGINTP.getSelectedItem().toString().substring(0,2);//cmbGINTP.getText().trim();
			   txtFMDAT.requestFocus();
		    }
		    else
		    {		        
				txtFMDAT.setEnabled(false);
				txtFMTIM.setEnabled(false);
				txtTODAT.setEnabled(false);
				txtTOTIM.setEnabled(false);				
		    }			
		}						
	}
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (vldDATA())
		{	
			try
			{
				cl_dat.M_PAGENO = 0;
			    if(M_rdbHTML.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rptas.html";
			    else if(M_rdbTEXT.isSelected())
			        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rptas.doc";				
				getALLREC();				
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
						ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Time Analysis Sheet"," ");
						setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
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
    * Method to fetch data from table MM_WBTRN,MR_IVTRN and club it with Header & footer 
    *in DataOutputStream to generate Report File.
	*/
	private void getALLREC()
	{ 	
		java.sql.Timestamp L_tmpDATTM;		
		intSRLNO = 0;	
	    try
	    {				
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			intRECCT = 0;			
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
			}
			
			if(M_rdbHTML.isSelected())
			{				
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Time Analysis Sheet</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");								
			}			
			strSTRTP=M_strSBSCD.substring(2,4);			
			strGINTP = cmbGINTP.getSelectedItem().toString().substring(0,2);						
			strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim()+" " + txtFMTIM.getText().trim()));
			strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtTODAT.getText().trim() +" "+ txtTOTIM.getText().trim()));			
			if(strGINTP.equals(strDSPTP))		// Despatch code=03
			{				
				strACPTM = "3";																																
				M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,SUBSTRING(WB_TPRDS,1,20) L_TPRDS,WB_REPTM," +
				              " WB_GINDT,WB_GOTDT,IVT_SALTP,IVT_DTPCD,time(wb_reptm) tt_reptm," +
				              " max(IVT_LADDT) IVT_LADDT,max(IVT_INVDT) IVT_INVDT,max(IVT_ALODT) IVT_ALODT,max(IVT_LODDT) IVT_LODDT from MM_WBTRN,MR_IVTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP  + "'" +
							  " and IVT_CMPCD=WB_CMPCD and IVT_GINNO = WB_DOCNO and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL <> 'X'" + 
							  " and IVT_SALTP = '" + strEXPSL + "' and WB_STSFL = '9'" +
				              " and WB_GOTDT between '" + strFMDAT + "' and '" + strTODAT + "' " ;
				if(txtDTPCD.getText().length()==2)
					M_strSQLQRY += " and IVT_DTPCD = '"+txtDTPCD.getText()+"'";
							  
				M_strSQLQRY += " group by WB_DOCNO,WB_LRYNO,SUBSTRING(WB_TPRDS,1,20),WB_REPTM,WB_GINDT,WB_GOTDT,IVT_SALTP,IVT_DTPCD,time(wb_reptm) order by time(WB_REPTM),WB_DOCNO";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);							
				strOGINNO = "XX";
				intRECCT = 0;
				intTOTCT = 0;
				intDUPCT = 0;
				intDEFCT = 0;				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strCPI17);
				prnHEADER(); 
				strOLADDT = strOLADDT = strOALODT = strOINVDT = "";
				dosREPORT.writeBytes("\n");				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes("Export : \n");
				cl_dat.M_intLINNO_pbst += 2;
				if(cl_dat.M_intLINNO_pbst > 64)
				{					
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    					dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
					if(M_rdbTEXT.isSelected())    				
    				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");							
				else
					dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");																		
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);						
					prnHEADER();
				}		
			    flgSLBCHO = false;
			    flgSLBCH1 = false;
			    flgSLBCH2 = false;
			    flgSLBCH3 = false;
			    flgSLBCHX = false;

				intSLBCT0 = 0;
			    intSLBCT1 = 0;
			    intSLBCT2 = 0;
			    intSLBCT3 = 0;
			    inzTOTTM(0);
			    inzTOTTM(1);
			    inzTOTTM(2);
			    inzTOTTM(3);					
				while(M_rstRSSET.next())
				{										
					getPRINTREC(M_rstRSSET);						
					if(strGINNO.equals(strOGINNO))
					{
						setOLDVAR1();						
						intDUPCT++;
						continue;
					}					
					if(strGINNO.equals("XX"))
					{						
						setOLDVAR();					
						continue;
					}					
					intDUPCT = 0;
					intTOTCT++;					
					exePRINTREC();									
					setOLDVAR();						
				}				
				intTOTCT++;				
				if(intDUPCT>1 || intTOTCT>1)					
					exePRINTREC();																	
			    flgSLBCHX = true;
			    chkTOTSLB();
				if(M_rstRSSET != null)
					M_rstRSSET.close();											
				// For Domestic
				intDUPCT = 0;
				intTOTCT = 0;
				M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,SUBSTRING(WB_TPRDS,1,20) L_TPRDS,WB_REPTM," +
			                 " WB_GINDT,WB_GOTDT,IVT_SALTP,IVT_DTPCD,time(wb_reptm) tt_reptm," +
			                 " max(IVT_LADDT) IVT_LADDT,max(IVT_INVDT) IVT_INVDT,max(IVT_ALODT) IVT_ALODT,max(IVT_LODDT) IVT_LODDT from MM_WBTRN,MR_IVTRN" +
							 " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP  + "'" +
							 " and IVT_CMPCD=WB_CMPCD and IVT_GINNO = WB_DOCNO and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL <> 'X'" +
							 " and IVT_SALTP <> '" + strEXPSL + "' and WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL = '9'" +
			                 " and WB_GOTDT between '" + strFMDAT + "' and '" + strTODAT +"' ";
				if(txtDTPCD.getText().length()==2)
					M_strSQLQRY += " and IVT_DTPCD = '"+txtDTPCD.getText()+"'";
							  
				M_strSQLQRY += " group by WB_DOCNO,WB_LRYNO,SUBSTRING(WB_TPRDS,1,20),WB_REPTM,WB_GINDT,WB_GOTDT,IVT_SALTP,IVT_DTPCD,time(wb_reptm) order by time(WB_REPTM),WB_DOCNO";				                     			
				System.out.println(M_strSQLQRY);
				M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);					
				strOGINNO = "XX";
				strOLADDT = strOLADDT = strOALODT = strOINVDT = "";				
				strPRNSTR = "";
				dosREPORT.writeBytes("\n");				
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes("Domestic : \n");
				cl_dat.M_intLINNO_pbst += 2;
		        flgSLBCHO = false;
		        flgSLBCH1 = false;
		        flgSLBCH2 = false;
		        flgSLBCH3 = false;
		        flgSLBCHX = false;
		        intSLBCT0 = 0;
		        intSLBCT1 = 0;
		        intSLBCT2 = 0;
		        intSLBCT3 = 0;
		        inzTOTTM(0);
		        inzTOTTM(1);
		        inzTOTTM(2);
		        inzTOTTM(3);					
				while(M_rstRSSET1.next())
				{					
					getPRINTREC(M_rstRSSET1);
					if(strGINNO.equals(strOGINNO))
					{
						setOLDVAR1();
						intDUPCT++; 
						continue;
					}							
					if(strOGINNO.equals("XX"))
					{
						setOLDVAR(); 
						continue;
					}												
					if(cl_dat.M_intLINNO_pbst > 64)
					{					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    						dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
						if(M_rdbTEXT.isSelected())    				
    						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");							
						else
							dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");																		
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER();
					}	
					intDUPCT = 0;
					intTOTCT++;					
					exePRINTREC();
 					strTOTTM = calTIME(strOGOTDT,strOGINDT);			// Total Time
					setOLDVAR();
				}					
				intTOTCT++;				
				if(intDUPCT>1 || intTOTCT>1)
					exePRINTREC();
				prnFOOTR();
				if(M_rstRSSET1 != null)
					M_rstRSSET1.close();																
			}
			// For Raw Material					
			if(strGINTP.equals(strTNKTP))	// Tankers
			{								
				strACPTM = "5";
				M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,SUBSTRING(WB_TPRDS,1,30) L_TPRDS,WB_GINDT," +
							" WB_SMPTM, WB_TSTTM, WB_INCTM, WB_OUTTM, WB_GOTDT from MM_WBTRN" +
							" where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP  + "' and WB_STSFL = '9'" +
							" and WB_GINDT between '" +	strFMDAT + "' and '" + strTODAT +"' ";
				//if(txtDTPCD.getText().length()==2)
				//	M_strSQLQRY += " and IVT_DTPCD = '"+txtDTPCD.getText()+"'";
							  
				M_strSQLQRY += " order by WB_DOCNO";				
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);				
				strOGINNO = "";
				intRECCT = 0;
				intDEFCT = 0;
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					prnFMTCHR(dosREPORT,M_strCPI17);				
				prnHEADER(); 
				while(M_rstRSSET.next())
				{				
					strGINNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"");										
					strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"");					
					strTPRDS = nvlSTRVL(M_rstRSSET.getString("L_TPRDS"),"");										
					L_tmpDATTM = M_rstRSSET.getTimestamp("WB_GINDT");
					if(L_tmpDATTM !=null)
						strGINDT = M_fmtLCDTM.format(L_tmpDATTM);
					L_tmpDATTM = M_rstRSSET.getTimestamp("WB_SMPTM");
					if(L_tmpDATTM !=null)
						strSMPTM = M_fmtLCDTM.format(L_tmpDATTM);
					L_tmpDATTM = M_rstRSSET.getTimestamp("WB_TSTTM");
					if(L_tmpDATTM !=null)
						strTSTTM = M_fmtLCDTM.format(L_tmpDATTM);
					L_tmpDATTM = M_rstRSSET.getTimestamp("WB_INCTM");
					if(L_tmpDATTM !=null)
						strINCTM = M_fmtLCDTM.format(L_tmpDATTM);
					L_tmpDATTM = M_rstRSSET.getTimestamp("WB_OUTTM");
					if(L_tmpDATTM !=null)
						strOUTTM = M_fmtLCDTM.format(L_tmpDATTM);
					L_tmpDATTM = M_rstRSSET.getTimestamp("WB_GOTDT");
					if(L_tmpDATTM !=null)
						strGOTDT = M_fmtLCDTM.format(L_tmpDATTM);
					strTOTTM = calTIME(strGOTDT,strGINDT);			// Total Time										
					strDTPCD = nvlSTRVL(M_rstRSSET.getString("IVT_DTPCD"),"");
					strDTPDS = nvlSTRVL(getCODVL("SYSMRXXDTP"+strDTPCD,cl_dat.M_intSHRDS_pbst),"");
					//System.out.println(strDTPCD+" / "+strDTPDS);
					if(cl_dat.M_intLINNO_pbst > 64)
					{					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    						dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
						if(M_rdbTEXT.isSelected())    				
		    				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");							
						else
							dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");																		
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER();
					}	
					intSRLNO =intSRLNO + 1;
					if (M_rdbHTML.isSelected())
					{
						strPRNSTR = padSTRING('L',String.valueOf(intSRLNO),3);
						strPRNSTR += "   ";
						strPRNSTR += padSTRING('R',strGINNO,11);
						strPRNSTR += padSTRING('R',strLRYNO,14);
						strPRNSTR += padSTRING('R',strTPRDS,30) + "    ";
						strPRNSTR += padSTRING('R',strGINDT,19);						
						strPRNSTR += padSTRING('R',(strSMPTM.substring(11,16)),8);					
						strPRNSTR += padSTRING('R',calTIME(strSMPTM,strGINDT),8);						
						strPRNSTR += padSTRING('R',(strTSTTM.substring(11,16)),8);					
						strPRNSTR += padSTRING('R',calTIME(strTSTTM,strSMPTM),8);						
						strPRNSTR += padSTRING('R',(strINCTM.substring(11,16)),8);					
						strPRNSTR += padSTRING('R',calTIME(strINCTM,strTSTTM),8);						
						strPRNSTR += padSTRING('R',(strOUTTM.substring(11,16)),8);					
						strPRNSTR += padSTRING('R',calTIME(strOUTTM,strINCTM),8);
						strPRNSTR += padSTRING('R',strGOTDT,18);
						strPRNSTR += padSTRING('R',calTIME(strGOTDT,strOUTTM),7);
						strPRNSTR += padSTRING('R',strTOTTM,5);						
					}					
					if(M_rdbTEXT.isSelected())
					{
						strPRNSTR = padSTRING('L',String.valueOf(intSRLNO),3);
						strPRNSTR += "   ";
						strPRNSTR += padSTRING('R',strGINNO,11);
						strPRNSTR += padSTRING('R',strLRYNO,14);
						strPRNSTR += padSTRING('R',strTPRDS,30) + "    ";
						strPRNSTR += padSTRING('R',strGINDT,19);						
						strPRNSTR += padSTRING('R',(strSMPTM.substring(11,16)),14);				
						strPRNSTR += padSTRING('R',calTIME(strSMPTM,strGINDT),11);						
						strPRNSTR += padSTRING('R',(strTSTTM.substring(11,16)),14);					
						strPRNSTR += padSTRING('R',calTIME(strTSTTM,strSMPTM),11);						
						strPRNSTR += padSTRING('R',(strINCTM.substring(11,16)),14);					
						strPRNSTR += padSTRING('R',calTIME(strINCTM,strTSTTM),11);						
						strPRNSTR += padSTRING('R',(strOUTTM.substring(11,16)),14);					
						strPRNSTR += padSTRING('R',calTIME(strOUTTM,strINCTM),11);
						strPRNSTR += padSTRING('R',strGOTDT,19);
						strPRNSTR += padSTRING('R',calTIME(strGOTDT,strOUTTM),7);
						strPRNSTR += padSTRING('R',strTOTTM,5);
					}
					if(Integer.parseInt(strTOTTM.substring(0,2) + strTOTTM.substring(3)) > intTNKTM)
					{
						strPRNSTR += " *";
						intDEFCT++;
					}				
					strPRNSTR += padSTRING('R',strDTPDS,4);						
					strPRNSTR += "\n";						
					cl_dat.M_intLINNO_pbst++;
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						dosREPORT.writeBytes(padSTRING('R'," ",8));
					dosREPORT.writeBytes(strPRNSTR);
					
					intRECCT++;
				}	
				if(intRECCT > 0 && cl_dat.M_intLINNO_pbst > 1)
					prnFOOTR();					
				if(M_rstRSSET != null)
					M_rstRSSET.close();					
			}											
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getALLREC");
		}
	}
	/**
	*Method to validate data, before execution of query, To check for blank input Dates & Time. 
    */
	boolean vldDATA()
	{
		try
		{	
			if(cmbGINTP.getSelectedIndex() == 0)			
			{
				setMSG("Please Select Gate-in type .. ",'E');
				cmbGINTP.requestFocus();
				return false;
			}				
            else if (!(txtTODAT.getText().trim().equals("")) && (!(txtFMDAT.getText().trim().equals(""))))
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
					txtFMDAT.setText(calRQDAT(cl_dat.M_strLOGDT_pbst));            
					txtFMDAT.requestFocus();
					return false;
				}				
			}				
	        else 
			{
	            if(txtFMDAT.getText().trim().toString().length() == 0)			
				{
					setMSG("Please Enter valid From-Date, To Spacify Date Range ..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				else if(txtTODAT.getText().trim().toString().length() == 0)			
				{	
					setMSG("Please Enter valid To-Date To Spacify Date Range ..",'E');
					txtTODAT.requestFocus();
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
	Method to calculate From-Date, one day smaller than To-Date.
	*/
    private String calRQDAT(String P_strTODAT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strTODAT));
		    M_calLOCAL.add(java.util.Calendar.DATE,-1);																
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{
	       setMSG(L_EX, "calRQDAT");
	       return (cl_dat.M_strLOGDT_pbst);
        }					
    }	
    /**
    Method to generate the Header part of the Report.
    */
	private void prnHEADER()  
	{
		try
		{				
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			dosREPORT.writeBytes("\n\n\n\n\n");
			if(M_rdbTEXT.isSelected())						
            {			    			
    			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes(padSTRING('R'," ",8));
    			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",56));
    			dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,162));//177
    			dosREPORT.writeBytes("\n");
				String L_strDTPDS = "";
				if(txtDTPCD.getText().length()==2)
					L_strDTPDS = "("+getCODVL("SYSMRXXDTP"+txtDTPCD.getText().trim(),cl_dat.M_intSHRDS_pbst).trim()+")";
    			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes(padSTRING('R'," ",8));
    			dosREPORT.writeBytes(padSTRING('R',String.valueOf(cmbGINTP.getSelectedItem()).substring(2) + " Time Sheet for Vehicles from " + txtFMDAT.getText()+" " + txtFMTIM.getText() + " to " + txtTODAT.getText() + " " + txtTOTIM.getText(),85));//100
    			dosREPORT.writeBytes(padSTRING('R',"Acceptable Target Time : " + strACPTM + " Hrs.  "+L_strDTPDS,109));			
    			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");			
    			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes(padSTRING('R'," ",8));
    			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			    			
    			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes(padSTRING('R'," ",8));
    			if(strGINTP.equals(strDSPTP))
    			{						// Despatch
    				dosREPORT.writeBytes(" Sr.  Security   Vehicle No.    Transporter             Reporting Date      Gate-In              L.A.                Alloc.              Loading             Invoice          Gate-Out Date      \n");
    				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    					dosREPORT.writeBytes(padSTRING('R'," ",8));				
    				dosREPORT.writeBytes(" No.  No.                                               And Time     A        B      (B-A)        C      (C-B)        D     (D-C)         E     (E-D)         F      (F-E)    And Time     G    (G-F)    (G-A)    (F-C)\n");
    			}
    			else if(strGINTP.equals(strTNKTP))
    			{					// Raw Material				
    				dosREPORT.writeBytes(" Sr.  Security   Vehicle No.   Transporter                       Gate-In Date       Sample Time              Analysis Time            Gross Weight             Net Weight               Gate-Out Date\n"); 
    				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    					dosREPORT.writeBytes(padSTRING('R'," ",8));
    				dosREPORT.writeBytes(" No.  No.                                                        And Time     A       B           (B-A)        C           (C-B)        D           (D-C)        E           (E-D)      And Time     F     (F-E)  (F-A)\n");
    			}			
    			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes(padSTRING('R'," ",8));
    			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");			
            }
            else// if html Selected.
            {			    			    			
    			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",56));
    			dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,123));//177
    			dosREPORT.writeBytes("\n");			    			
    			dosREPORT.writeBytes(padSTRING('R',String.valueOf(cmbGINTP.getSelectedItem()).substring(2) + " Time Sheet for Vehicles from " + txtFMDAT.getText()+" " + txtFMTIM.getText() + " to " + txtTODAT.getText() + " " + txtTOTIM.getText(),85));//100
    			dosREPORT.writeBytes(padSTRING('R',"Acceptable Target Time : " + strACPTM + " Hrs.",70));			
    			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");			    			
    			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
    			if(strGINTP.equals(strDSPTP))
    			{						// Despatch
    				dosREPORT.writeBytes(" Sr. Security  Vehicle No.  Transporter         Reporting Date     Gate-In         L.A.            Alloc.          Loading         Invoice         Gate-Out Date & Time\n");    				
    				dosREPORT.writeBytes(" No. No.                                        And Time     A       B      B-A      C     (C-B)     D     (D-C)     E     (E-D)     F     (F-E)                G     (G-F)   (G-A) (F-C)\n");
    			}
    			else if(strGINTP.equals(strTNKTP))
    			{					// Raw Material				
    				dosREPORT.writeBytes(" Sr.  Security   Vehicle No.   Transporter                       Gate-In Date       Sample          Analysis        Gross           Net             GateOut Date\n");     				
    				dosREPORT.writeBytes(" No.  No.                                                        And Time           Time            Time            Weight          Weight          And Time\n");
    				dosREPORT.writeBytes("                                                                              A       B     (B-A)     C     (C-B)     D     (D-C)     E     (E-D)                F    (F-E)  (F-A)\n");
    			}			    			
    			dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");			
    		}
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 12;			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	Method to generate Footer part of the Report.
	*/
	private void prnFOOTR()
	{
		try
		{
			dosREPORT.writeBytes ("\n");			
            if(strGINTP.equals(strDSPTP))                                             // Despatch
            {
				flgSLBCHX = true;
                chkTOTSLB();
                if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				    dosREPORT.writeBytes(padSTRING('R'," ",8));                
				if(M_rdbTEXT.isSelected())
				    dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
				else
				    dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes(padSTRING('L'," No. of Vehicles Delayed :",60) + " " +String.valueOf(intDLYCT) + padSTRING('L',String.valueOf(intDEFCT),113) + "\n");
			}
			else if(strGINTP.equals(strTNKTP))					// Raw Material
            {
                if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
				dosREPORT.writeBytes(padSTRING('R'," ",8));
                if(M_rdbTEXT.isSelected())
				    dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
				else
				    dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					dosREPORT.writeBytes(padSTRING('R'," ",8));
				dosREPORT.writeBytes (padSTRING('L',"                      Total No. of Vehicles : " + intRECCT,52) + padSTRING('L',"                       No. of Vehicles within Acceptable Time : " + (intRECCT-intDEFCT),65) + "                       No. of Vehicles Delayed : " + intDEFCT + "\n");
            }		
			setMSG("Report completed.. ",'N');
			setCursor(cl_dat.M_curDFSTS_pbst);			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}	
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    
			}
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
	/**
	Method to initialize arrays.
	*/
	private void inzTOTTM(int i)
	{
        arrTOTM1[i] = "00:00";        
        arrTOTM2[i] = "00:00";        
        arrTOTM3[i] = "00:00";        
        arrTOTM4[i] = "00:00";        
        arrTOTM5[i] = "00:00";        
        arrTOTM6[i] = "00:00";        
        arrTOTM7[i] = "00:00";        
		arrAVTM1[i] = "00:00";        
        arrAVTM2[i] = "00:00";        
        arrAVTM3[i] = "00:00";        
        arrAVTM4[i] = "00:00";        
        arrAVTM5[i] = "00:00";        
        arrAVTM6[i] = "00:00";        
        arrAVTM7[i] = "00:00";        
	}
	/**
	Method to set data fetched from DB in to the variables.
	@param P_rstRSSET ResultSet as argument to get Data through that Resultset Object.
	*/
	private void getPRINTREC(ResultSet P_rstRSSET)
	{
		java.sql.Timestamp L_tmpDATTM;
		try
		{				
			strGINNO = nvlSTRVL(P_rstRSSET.getString("WB_DOCNO"),"");				
			strLRYNO = nvlSTRVL(P_rstRSSET.getString("WB_LRYNO"),"");				
			strTPRDS = nvlSTRVL(P_rstRSSET.getString("L_TPRDS"),"");								
			L_tmpDATTM = P_rstRSSET.getTimestamp("WB_REPTM");
			if(L_tmpDATTM !=null)	
				strREPTM = M_fmtLCDTM.format(L_tmpDATTM);																			
			L_tmpDATTM = P_rstRSSET.getTimestamp("WB_GINDT");
			if(L_tmpDATTM !=null)	
				strGINDT = M_fmtLCDTM.format(L_tmpDATTM);				
			L_tmpDATTM = P_rstRSSET.getTimestamp("IVT_LADDT");
			if(L_tmpDATTM !=null)	
				strLADDT = M_fmtLCDTM.format(L_tmpDATTM);	
			L_tmpDATTM = P_rstRSSET.getTimestamp("IVT_ALODT");
			if(L_tmpDATTM !=null)	
				strALODT = M_fmtLCDTM.format(L_tmpDATTM);	
			L_tmpDATTM = P_rstRSSET.getTimestamp("IVT_LODDT");
			if(L_tmpDATTM !=null)	
				strLODDT = M_fmtLCDTM.format(L_tmpDATTM);	
			L_tmpDATTM = P_rstRSSET.getTimestamp("IVT_INVDT");
			if(L_tmpDATTM !=null)	
				strINVDT = M_fmtLCDTM.format(L_tmpDATTM);	
			L_tmpDATTM = P_rstRSSET.getTimestamp("WB_GOTDT");
			if(L_tmpDATTM !=null)	
				strGOTDT = M_fmtLCDTM.format(L_tmpDATTM);					
			if(L_tmpDATTM !=null)	
			{
				strDTPCD = nvlSTRVL(P_rstRSSET.getString("IVT_DTPCD"),"");
				strDTPDS = nvlSTRVL(getCODVL("SYSMRXXDTP"+strDTPCD,cl_dat.M_intSHRDS_pbst),"");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRINTREC");
		}
	}
	/**
	Method to Manipulate the L.A. preparetion Time, Reporting Time, loading Time Variables.
	*/
	private void setOLDVAR1()
	{
		try
		{			
			if(!strLADDT.equals(""))
			{
				if(strOLADDT.equals(""))		// L.A. Preparation Time
					strOLADDT = strLADDT;
				else if(Integer.parseInt(strLADDT.substring(11,13) + strLADDT.substring(14)) > Integer.parseInt(strOLADDT.substring(11,13) + strOLADDT.substring(14)))
					strOLADDT = strLADDT;
			}
			if(!strALODT.equals(""))
			{
				if(strOALODT.equals(""))		// Reporting at MHD
					strOALODT = strALODT;
				else if(Integer.parseInt(strALODT.substring(11,13) + strALODT.substring(14)) < Integer.parseInt(strOALODT.substring(11,13) + strOALODT.substring(14)))
					strOALODT = strALODT;
			}
			if(!strLODDT.equals(""))
			{
				if(strOLODDT.equals(""))		// Loading Time
					strOLODDT = strLODDT;
				else if(Integer.parseInt(strLODDT.substring(11,13) + strLODDT.substring(14)) > Integer.parseInt(strOLODDT.substring(11,13) + strOLODDT.substring(14)))
					strOLODDT = strLODDT;
			}
			if(!strINVDT.equals(""))
			{
				if(strOINVDT.equals(""))		// Invoice Time
					strOINVDT = strINVDT;
				else if(Integer.parseInt(strINVDT.substring(11,13) + strINVDT.substring(14)) > Integer.parseInt(strOINVDT.substring(11,13) + strOINVDT.substring(14)))
					strOINVDT = strINVDT;
			}
		}
		catch(Exception L_EX)
		{
					setMSG(L_EX,"setOLDVAR1");
		}
	}
	/**
	Method to set the values for the old variable to moniter change.
	*/
	private void setOLDVAR()
	{
		try
		{			
			strOGINNO = strGINNO;
			strOLRYNO = strLRYNO;
			strOTPRDS = strTPRDS;
			strOREPTM = strREPTM;
			strOGINDT = strGINDT;
			strOLADDT = strLADDT;
			strOALODT = strALODT;
			strOLODDT = strLODDT;
			strOINVDT = strINVDT;
			strOGOTDT = strGOTDT;					
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setOLDVAR");
		}
	}
	/**
	Method to generate the body part of the Report for Despatch Report.
	*/
	private void exePRINTREC()
	{		
		try
		{
		    if(nvlSTRVL(strOGINNO,"").length()<8)
			    return;
			intSRLNO =intSRLNO + 1;	
			if(M_rdbTEXT.isSelected())
			{
		    	strPRNSTR = padSTRING('L',String.valueOf(intSRLNO),3);				
    			strPRNSTR += "   ";
    			strPRNSTR += padSTRING('R',strOGINNO,11);
    			strPRNSTR += padSTRING('R',strOLRYNO,15);
    			strPRNSTR += padSTRING('R',strOTPRDS,20) + "    ";
    			strPRNSTR += padSTRING('R',strOREPTM,16);
    			// If vehicle Reports after Accepted Reporting Time
    			if(Integer.parseInt(strOREPTM.substring(11,13) + strOREPTM.substring(14)) > intEXPTM)
    			{
    				strPRNSTR += "*   ";
    				intDLYCT++;
    			}
    			else
    				strPRNSTR += "    ";
    				strPRNSTR += padSTRING('R',(strOGINDT + "                 ").substring(11),9);					
    				strPRNSTR += padSTRING('R',calTIME(strOGINDT,strOREPTM),11);					
    				strPRNSTR += padSTRING('R',(strOLADDT + "                 ").substring(11),9);		//c			
    				strPRNSTR += padSTRING('R',calTIME(strOLADDT,strOGINDT),11);					
    				strPRNSTR += padSTRING('R',(strOALODT + "                 ").substring(11),9);
    				strPRNSTR += padSTRING('R',calTIME(strOALODT,strOLADDT),11);					
    				strPRNSTR += padSTRING('R',(strOLODDT + "                 ").substring(11),9);
    				strPRNSTR += padSTRING('R',calTIME(strOLODDT,strOALODT),11);					
    				strPRNSTR += padSTRING('R',(strOINVDT + "                 ").substring(11),9); //f
    				strPRNSTR += padSTRING('R',calTIME(strOINVDT,strOLODDT),11);
    				strPRNSTR += padSTRING('R',strOGOTDT,18);				
    				strPRNSTR += padSTRING('R',calTIME(strOGOTDT,strOINVDT),9);					
    				strTOTTM = calTIME(strOGOTDT,strOREPTM);  
  				    strPRNSTR += padSTRING('R',strTOTTM,5);					    			
                 
                   
    			int L_CMPSTR = 0;
    			if(strTOTTM.length() == 6)
    				L_CMPSTR = Integer.parseInt(strTOTTM.substring(0,3) + strTOTTM.substring(4));
    			else if(strTOTTM.length() == 5)
    				L_CMPSTR = Integer.parseInt(strTOTTM.substring(0,2) + strTOTTM.substring(3));				
    			if(L_CMPSTR > intDSPTM)
    			{
    				strPRNSTR += "*   ";
    				intDEFCT++;
    			}
    			else 
    			    strPRNSTR += "    ";
    			strF_C = calTIME(strOINVDT,strOLADDT);  	
    			strPRNSTR += padSTRING('R',strF_C,5);					    						
                L_CMPSTR = 0;
    			if(strF_C.length() == 6)
    				L_CMPSTR = Integer.parseInt(strF_C.substring(0,3) + strF_C.substring(4));
    			else if(strTOTTM.length() == 5)
    				L_CMPSTR = Integer.parseInt(strF_C.substring(0,2) + strF_C.substring(3));				
    			if(L_CMPSTR > intDSPTM)
    			{
    				strPRNSTR += "*   ";
    				//intDEFCT++;
    			}
    			else 
    			    strPRNSTR += "    ";

  				strPRNSTR += padSTRING('R',strDTPDS,4);					    			
    			strPRNSTR += "\n";				
                intRPTCHK = Integer.parseInt(strOREPTM.substring(11,13) + strOREPTM.substring(14,16));
                chkTIMSLB();				
			}
			else//if HTML Selected.
			{
    			strPRNSTR = padSTRING('L',String.valueOf(intSRLNO),3);				
    			strPRNSTR += "  ";
    			strPRNSTR += padSTRING('R',strOGINNO,10);
    			strPRNSTR += padSTRING('R',strOLRYNO,13);
    			strPRNSTR += padSTRING('R',strOTPRDS,20);
    			strPRNSTR += padSTRING('R',strOREPTM,16);
    			// If vehicle Reports after Accepted Reporting Time
    			if(Integer.parseInt(strOREPTM.substring(11,13) + strOREPTM.substring(14)) > intEXPTM)
    			{
    				strPRNSTR += "*  ";
    				intDLYCT++;
    			}
    			else
    				strPRNSTR += "   ";
    				strPRNSTR += padSTRING('R',(strOGINDT.substring(11,16)),8);					
    				strPRNSTR += padSTRING('R',calTIME(strOGINDT,strOREPTM),8);					
    				strPRNSTR += padSTRING('R',(strOLADDT.substring(11,16)),8);					
    				strPRNSTR += padSTRING('R',calTIME(strOLADDT,strOGINDT),8);					
    				strPRNSTR += padSTRING('R',(strOALODT.substring(11,16)),8);
    				strPRNSTR += padSTRING('R',calTIME(strOALODT,strOLADDT),8);					
    				strPRNSTR += padSTRING('R',(strOLODDT.substring(11,16)),8);
    				strPRNSTR += padSTRING('R',calTIME(strOLODDT,strOALODT),8);					
    				strPRNSTR += padSTRING('R',(strOINVDT.substring(11,16)),8);
    				strPRNSTR += padSTRING('R',calTIME(strOINVDT,strOLODDT),8);
    				strPRNSTR += padSTRING('R',strOGOTDT,17);				
    				strPRNSTR += padSTRING('R',calTIME(strOGOTDT,strOINVDT),6);					
    				strTOTTM = calTIME(strOGOTDT,strOREPTM);			// Total Time
    				strPRNSTR += padSTRING('R',strTOTTM,5);					    			
    			int L_CMPSTR = 0;
    			if(strTOTTM.length() == 6)
    				L_CMPSTR = Integer.parseInt(strTOTTM.substring(0,3) + strTOTTM.substring(4));
    			else if(strTOTTM.length() == 5)
    				L_CMPSTR = Integer.parseInt(strTOTTM.substring(0,2) + strTOTTM.substring(3));				
    			if(L_CMPSTR > intDSPTM)
    			{
    				strPRNSTR += " *";
    				intDEFCT++;
    			}
    			strPRNSTR += padSTRING('R',strDTPDS,4);					    			
    			strPRNSTR += "\n";				
                intRPTCHK = Integer.parseInt(strOREPTM.substring(11,13) + strOREPTM.substring(14,16));
                chkTIMSLB();				
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			    dosREPORT.writeBytes(padSTRING('R'," ",8));
			dosREPORT.writeBytes(strPRNSTR);
			cl_dat.M_intLINNO_pbst++;
			intRECCT++;				
			if(cl_dat.M_intLINNO_pbst > 64)
			{					
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
				if(M_rdbTEXT.isSelected())    				
    				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");							
				else
					dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");																		
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);						
				prnHEADER();
			}	
		}
		catch(Exception L_EX)
		{	
		    setMSG(L_EX,"exePRINTREC");
		}
	}
	/**
	Method to insert the Date & Time Values in arrays for further Manipulation.
	*/
	private void  chkTIMSLB()
    {
	    try
		{
			int i=0;			
			if (intRPTCHK <= intSLBTM1)
            {
				intSLBCT0 += 1;                   
				i = 0;
                if(!flgSLBCHO)
                {
					flgSLBCHO = true;
	                chkTOTSLB();                    				
				}
			}
            else if (intRPTCHK > intSLBTM1 && intRPTCHK <= intSLBTM2)
            {
				intSLBCT1 += 1;                   
                i = 1;
                if(!flgSLBCH1)
                {
					flgSLBCH1 = true;
                    chkTOTSLB();                    				
				}
			}
            else if (intRPTCHK > intSLBTM2 && intRPTCHK <= intSLBTM3)
            {
				intSLBCT2 += 1;                   
                i = 2;
                if(!flgSLBCH2)
                {
					flgSLBCH2 = true;
                    chkTOTSLB();                    				
				}
			}                                           
			else if (intRPTCHK > intSLBTM3)
            {
				intSLBCT3 += 1;                   
                i = 3;
                if (!flgSLBCH3)
                {
					flgSLBCH3 = true;
                    chkTOTSLB();                    				
                }
			}
			if(cl_dat.M_intLINNO_pbst > 64)
			{					
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes (padSTRING('R'," ",6)); 											
				if(M_rdbTEXT.isSelected())    				
    				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");							
				else
					dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);						
				prnHEADER();
			}	
			arrTOTM1[i] = addTIME(arrTOTM1[i],calTIME(strOLADDT,strOGINDT));
            arrTOTM2[i] = addTIME(arrTOTM2[i],calTIME(strOALODT,strOLADDT));
            arrTOTM3[i] = addTIME(arrTOTM3[i],calTIME(strOLODDT,strOALODT));
            arrTOTM4[i] = addTIME(arrTOTM4[i],calTIME(strOINVDT,strOLODDT));
            arrTOTM5[i] = addTIME(arrTOTM5[i],calTIME(strOGOTDT,strOINVDT));
            arrTOTM6[i] = addTIME(arrTOTM6[i],calTIME(strOGOTDT,strOREPTM));
            arrTOTM7[i] = addTIME(arrTOTM7[i],calTIME(strOINVDT,strOLADDT));
		}
        catch(Exception L_EX)
        {
			setMSG(L_EX,"chkTIMSLB");
        }
    }
    /**
    Method to calculate the Total Time & Average Time.
    */
	private void chkTOTSLB()
    {
        try
        {
            String L_strTOTSTR1 = "";
            String L_strTOTSTR2 = "";
            String L_strREPTMHDR = "Reported: ";
            String L_strAVGTMHDR = "Avg Time: ";
            if (M_rdbTEXT.isSelected())
            {
                if(intSLBCT0>0 && (flgSLBCH1 || flgSLBCH2 || flgSLBCH3 || flgSLBCHX))
                {
                    arrAVTM1[0] = avgTIME(arrTOTM1[0],intSLBCT0);
                    arrAVTM2[0] = avgTIME(arrTOTM2[0],intSLBCT0);                          
                    arrAVTM3[0] = avgTIME(arrTOTM3[0],intSLBCT0);
                    arrAVTM4[0] = avgTIME(arrTOTM4[0],intSLBCT0);
                    arrAVTM5[0] = avgTIME(arrTOTM5[0],intSLBCT0);
                    arrAVTM6[0] = avgTIME(arrTOTM6[0],intSLBCT0);
                    arrAVTM7[0] = avgTIME(arrTOTM7[0],intSLBCT0);
                    
                    L_strTOTSTR1  = padSTRING('R',strSLBSTR0,27)+padSTRING('R',L_strREPTMHDR+intSLBCT0,48)+padSTRING('L',"Total Time:",15);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM1[0],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM2[0],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM3[0],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM4[0],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM5[0],29);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM6[0],9);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM7[0],9)+"\n";
        
                    L_strTOTSTR2  = padSTRING('R',"",27)+padSTRING('R',"",48)+padSTRING('L',"Avg. Time :",15);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM1[0],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM2[0],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM3[0],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM4[0],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM5[0],29);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM6[0],9);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM7[0],9)+"\n";
                    intSLBCT0 = 0; inzTOTTM(0);
                 }
                 else if(intSLBCT1>0 && (flgSLBCH2 || flgSLBCH3 || flgSLBCHX))
                 {        			     
                    arrAVTM1[1] = avgTIME(arrTOTM1[1],intSLBCT1);
                    arrAVTM2[1] = avgTIME(arrTOTM2[1],intSLBCT1);
                    arrAVTM3[1] = avgTIME(arrTOTM3[1],intSLBCT1);
                    arrAVTM4[1] = avgTIME(arrTOTM4[1],intSLBCT1);
                    arrAVTM5[1] = avgTIME(arrTOTM5[1],intSLBCT1);
                    arrAVTM6[1] = avgTIME(arrTOTM6[1],intSLBCT1);
                    arrAVTM7[1] = avgTIME(arrTOTM7[1],intSLBCT1);
        
                    L_strTOTSTR1  = padSTRING('R',strSLBSTR1,27)+padSTRING('R',L_strREPTMHDR+intSLBCT1,48)+padSTRING('L',"Total Time:",15);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM1[1],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM2[1],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM3[1],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM4[1],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM5[1],29);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM6[1],9);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM7[1],9)+"\n";
                    
                    L_strTOTSTR2  = padSTRING('R',"",27)+padSTRING('R',"",48)+padSTRING('L',"Avg. Time :",15);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM1[1],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM2[1],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM3[1],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM4[1],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM5[1],29);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM6[1],9);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM7[1],9)+"\n";
                    
                    intSLBCT1 = 0; inzTOTTM(1);
                }
                else if(intSLBCT2>0 && (flgSLBCH3 || flgSLBCHX))
                {        				
                    arrAVTM1[2] = avgTIME(arrTOTM1[2],intSLBCT2);
                    arrAVTM2[2] = avgTIME(arrTOTM2[2],intSLBCT2);
                    arrAVTM3[2] = avgTIME(arrTOTM3[2],intSLBCT2);
                    arrAVTM4[2] = avgTIME(arrTOTM4[2],intSLBCT2);
                    arrAVTM5[2] = avgTIME(arrTOTM5[2],intSLBCT2);
                    arrAVTM6[2] = avgTIME(arrTOTM6[2],intSLBCT2);
                    arrAVTM7[2] = avgTIME(arrTOTM7[2],intSLBCT2);
                    
                    L_strTOTSTR1  = padSTRING('R',strSLBSTR2,27)+padSTRING('R',L_strREPTMHDR+intSLBCT2,48)+padSTRING('L',"Total Time:",15);                        
                    L_strTOTSTR1 += padSTRING('L',arrTOTM1[2],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM2[2],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM3[2],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM4[2],20);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM5[2],29);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM6[2],9);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM7[2],9)+"\n";
                    
                    L_strTOTSTR2  = padSTRING('R',"",27)+padSTRING('R',"",48)+padSTRING('L',"Avg. Time :",15);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM1[2],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM2[2],20);                           
                    L_strTOTSTR2 += padSTRING('L',arrAVTM3[2],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM4[2],20);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM5[2],29);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM6[2],9);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM7[2],9)+"\n";
                    
                    intSLBCT2 = 0;  inzTOTTM(2);
               }
               else if(intSLBCT3>0 && (flgSLBCHX))
               {        			 
                   arrAVTM1[3] = avgTIME(arrTOTM1[3],intSLBCT3);
                   arrAVTM2[3] = avgTIME(arrTOTM2[3],intSLBCT3);
                   arrAVTM3[3] = avgTIME(arrTOTM3[3],intSLBCT3);
                   arrAVTM4[3] = avgTIME(arrTOTM4[3],intSLBCT3);
                   arrAVTM5[3] = avgTIME(arrTOTM5[3],intSLBCT3);
                   arrAVTM6[3] = avgTIME(arrTOTM6[3],intSLBCT3);
                   arrAVTM7[3] = avgTIME(arrTOTM7[3],intSLBCT3);
                    
                   L_strTOTSTR1  = padSTRING('R',strSLBSTR3,27)+padSTRING('R',L_strREPTMHDR+intSLBCT3,48)+padSTRING('L',"Total Time:",15);
                   L_strTOTSTR1 += padSTRING('L',arrTOTM1[3],20);
                   L_strTOTSTR1 += padSTRING('L',arrTOTM2[3],20);
                   L_strTOTSTR1 += padSTRING('L',arrTOTM3[3],20);
                   L_strTOTSTR1 += padSTRING('L',arrTOTM4[3],20);
                   L_strTOTSTR1 += padSTRING('L',arrTOTM5[3],29);
                   L_strTOTSTR1 += padSTRING('L',arrTOTM6[3],9);
                   L_strTOTSTR1 += padSTRING('L',arrTOTM7[3],9)+"\n";
        
                   L_strTOTSTR2  = padSTRING('R',"",27)+padSTRING('R',"",48)+padSTRING('L',"Avg. Time :",15);
                   L_strTOTSTR2 += padSTRING('L',arrAVTM1[3],20);
                   L_strTOTSTR2 += padSTRING('L',arrAVTM2[3],20);
                   L_strTOTSTR2 += padSTRING('L',arrAVTM3[3],20);
                   L_strTOTSTR2 += padSTRING('L',arrAVTM4[3],20);
                   L_strTOTSTR2 += padSTRING('L',arrAVTM5[3],29);
                   L_strTOTSTR2 += padSTRING('L',arrAVTM6[3],9);
                   L_strTOTSTR2 += padSTRING('L',arrAVTM7[3],9)+"\n";
                
                   intSLBCT3 = 0;  inzTOTTM(3);
                }
                if(!L_strTOTSTR1.equals(""))
                {                       
                    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) 
                        dosREPORT.writeBytes(padSTRING('R'," ",8));
                    dosREPORT.writeBytes(L_strTOTSTR1);
                    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					    dosREPORT.writeBytes(padSTRING('R'," ",8));
                    dosREPORT.writeBytes(L_strTOTSTR2);
                    cl_dat.M_intLINNO_pbst += 2;
					if(cl_dat.M_intLINNO_pbst > 64)
					{					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    						dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
						if(M_rdbTEXT.isSelected())    				
    						dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");							
						else
							dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");																								
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER();
					}	
                 }
            }
            else // if HTML is Selected.
            {
                if(intSLBCT0>0 && (flgSLBCH1 || flgSLBCH2 || flgSLBCH3 || flgSLBCHX))
                {
                    arrAVTM1[0] = avgTIME(arrTOTM1[0],intSLBCT0);
                    arrAVTM2[0] = avgTIME(arrTOTM2[0],intSLBCT0);                           
                    arrAVTM3[0] = avgTIME(arrTOTM3[0],intSLBCT0);
                    arrAVTM4[0] = avgTIME(arrTOTM4[0],intSLBCT0);
                    arrAVTM5[0] = avgTIME(arrTOTM5[0],intSLBCT0);
                    arrAVTM6[0] = avgTIME(arrTOTM6[0],intSLBCT0);
                    arrAVTM7[0] = avgTIME(arrTOTM7[0],intSLBCT0);
                    
                    L_strTOTSTR1  = padSTRING('R',strSLBSTR0,29)+padSTRING('R',L_strREPTMHDR+intSLBCT0,32)+padSTRING('L',"Total Time:",17);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM1[0],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM2[0],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM3[0],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM4[0],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM5[0],25);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM6[0],6);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM7[0],6)+"\n";
        
                    L_strTOTSTR2  = padSTRING('R',"",29)+padSTRING('R',"",32)+padSTRING('L',"Avg. Time :",17);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM1[0],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM2[0],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM3[0],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM4[0],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM5[0],25);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM6[0],6);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM7[0],6)+"\n";
                    intSLBCT0 = 0; inzTOTTM(0);
                }
                else if(intSLBCT1>0 && (flgSLBCH2 || flgSLBCH3 || flgSLBCHX))
                {        			 
                    arrAVTM1[1] = avgTIME(arrTOTM1[1],intSLBCT1);
                    arrAVTM2[1] = avgTIME(arrTOTM2[1],intSLBCT1);
                    arrAVTM3[1] = avgTIME(arrTOTM3[1],intSLBCT1);
                    arrAVTM4[1] = avgTIME(arrTOTM4[1],intSLBCT1);
                    arrAVTM5[1] = avgTIME(arrTOTM5[1],intSLBCT1);
                    arrAVTM6[1] = avgTIME(arrTOTM6[1],intSLBCT1);
                    arrAVTM7[1] = avgTIME(arrTOTM7[1],intSLBCT1);
        
                    L_strTOTSTR1  = padSTRING('R',strSLBSTR1,29)+padSTRING('R',L_strREPTMHDR+intSLBCT1,32)+padSTRING('L',"Total Time:",17);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM1[1],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM2[1],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM3[1],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM4[1],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM5[1],25);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM6[1],6);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM7[1],6)+"\n";
        
                    L_strTOTSTR2  = padSTRING('R',"",29)+padSTRING('R',"",32)+padSTRING('L',"Avg. Time :",17);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM1[1],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM2[1],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM3[1],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM4[1],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM5[1],25);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM6[1],6);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM7[1],6)+"\n";
                    intSLBCT1 = 0; inzTOTTM(1);
                }
                else if(intSLBCT2>0 && (flgSLBCH3 || flgSLBCHX))
                {        				
                    arrAVTM1[2] = avgTIME(arrTOTM1[2],intSLBCT2);
                    arrAVTM2[2] = avgTIME(arrTOTM2[2],intSLBCT2);
                    arrAVTM3[2] = avgTIME(arrTOTM3[2],intSLBCT2);
                    arrAVTM4[2] = avgTIME(arrTOTM4[2],intSLBCT2);
                    arrAVTM5[2] = avgTIME(arrTOTM5[2],intSLBCT2);
                    arrAVTM6[2] = avgTIME(arrTOTM6[2],intSLBCT2);
                    arrAVTM7[2] = avgTIME(arrTOTM7[2],intSLBCT2);
        
                    L_strTOTSTR1  = padSTRING('R',strSLBSTR2,29)+padSTRING('R',L_strREPTMHDR+intSLBCT2,32)+padSTRING('L',"Total Time:",17);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM1[2],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM2[2],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM3[2],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM4[2],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM5[2],25);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM6[2],6);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM7[2],6)+"\n";
        
                    L_strTOTSTR2  = padSTRING('R',"",29)+padSTRING('R',"",32)+padSTRING('L',"Avg. Time: ",17);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM1[2],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM2[2],16);                            
                    L_strTOTSTR2 += padSTRING('L',arrAVTM3[2],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM4[2],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM5[2],25);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM6[2],6);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM7[2],6)+"\n";
                    intSLBCT2 = 0;  inzTOTTM(2);
                }
                else if(intSLBCT3>0 && (flgSLBCHX))
                {        			     
                    arrAVTM1[3] = avgTIME(arrTOTM1[3],intSLBCT3);
                    arrAVTM2[3] = avgTIME(arrTOTM2[3],intSLBCT3);
                    arrAVTM3[3] = avgTIME(arrTOTM3[3],intSLBCT3);
                    arrAVTM4[3] = avgTIME(arrTOTM4[3],intSLBCT3);
                    arrAVTM5[3] = avgTIME(arrTOTM5[3],intSLBCT3);
                    arrAVTM6[3] = avgTIME(arrTOTM6[3],intSLBCT3);
                    arrAVTM7[3] = avgTIME(arrTOTM7[3],intSLBCT3);
        
                    L_strTOTSTR1  = padSTRING('R',strSLBSTR3,29)+padSTRING('R',L_strREPTMHDR+intSLBCT3,32)+padSTRING('L',"Total Time:",17);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM1[3],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM2[3],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM3[3],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM4[3],16);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM5[3],25);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM6[3],6);
                    L_strTOTSTR1 += padSTRING('L',arrTOTM7[3],6)+"\n";
        
                    L_strTOTSTR2  = padSTRING('R',"",29)+padSTRING('R',"",32)+padSTRING('L',"Avg. Time :",17);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM1[3],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM2[3],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM3[3],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM4[3],16);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM5[3],25);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM6[3],6);
                    L_strTOTSTR2 += padSTRING('L',arrAVTM7[3],6)+"\n";
                    intSLBCT3 = 0;  inzTOTTM(3);
                }
                if(!L_strTOTSTR1.equals(""))
                {                        
                    dosREPORT.writeBytes("  "+L_strTOTSTR1);                     
                    dosREPORT.writeBytes("  "+L_strTOTSTR2);
                    cl_dat.M_intLINNO_pbst += 2;
                } 
            }
			if(cl_dat.M_intLINNO_pbst > 64)
			{					
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
    				dosREPORT.writeBytes (padSTRING('R'," ",6)); 							
				if(M_rdbTEXT.isSelected())    				
    				dosREPORT.writeBytes("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");							
				else
					dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");																		
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);						
				prnHEADER();
			}	
        }
        catch(Exception L_EX)
        {
            setMSG(L_EX,"chkTOTSLB");
        }
    }
    /**
    Method to calaulate the Average Time.
	@param P_strSTRTM String argument to pass Starting Time 
	@param P_intTIMCT Integer argument to pass Time Count.
    */
	private String avgTIME(String P_strSTRTM,int P_intTIMCT)
	{
		String L_strRETSTR="";
        try
        {
			int L_intSTRCL = P_strSTRTM.indexOf(":");
            int L_intSTRLN = P_strSTRTM.length();
            int L_intTOTMN = Integer.parseInt(P_strSTRTM.substring(0,L_intSTRCL))*60+Integer.parseInt(P_strSTRTM.substring(L_intSTRCL+1,L_intSTRLN));
            int L_intAVGMN = L_intTOTMN / P_intTIMCT;
            String L_strAVGHR1 = "00"+String.valueOf(L_intAVGMN/60);
            String L_strAVGMN1 = "00"+String.valueOf(L_intAVGMN-((L_intAVGMN/60)*60)).trim();
            int L_intHRRLN = L_strAVGHR1.length();
            int L_intMINLN = L_strAVGMN1.length();
            L_strRETSTR = L_strAVGHR1.substring(L_intHRRLN-2,L_intHRRLN)+":"+L_strAVGMN1.substring(L_intMINLN-2,L_intMINLN);
			return(L_strRETSTR);
        }
        catch(Exception L_EX)
        {
			setMSG(L_EX,"avgTIME");
			return("");
		}        
	}		
    /**
    Method to add time given in second parameter to the time in first parameter 
    and returns the result in HH:MM format
	@param P_strSTRTM String argument to pass Starting Time.
	@param P_strNEWTM String argument to pass New Time.
    */
    private String addTIME(String P_strSTRTM,String P_strNEWTM)
    {
	    String L_strRETSTR = "";
		try
        {
            if (P_strSTRTM.equals(""))  P_strSTRTM = "00:00";
            if (P_strNEWTM.equals(""))  P_strNEWTM = "00:00";            
            int  L_intSTRLN = P_strSTRTM.trim().length();
            int  L_intNEWLN = P_strNEWTM.trim().length();
            int  L_intSTRCL = P_strSTRTM.indexOf(":");
            int  L_intNEWCL = P_strNEWTM.indexOf(":");                
            int  L_intSTRMN = Integer.parseInt(P_strSTRTM.substring(0,L_intSTRCL))*60+Integer.parseInt(P_strSTRTM.substring(L_intSTRCL+1,L_intSTRLN));
            int  L_intNEWMN = Integer.parseInt(P_strNEWTM.substring(0,L_intNEWCL))*60+Integer.parseInt(P_strNEWTM.substring(L_intNEWCL+1,L_intNEWLN));
            int  L_intTOTHR = (L_intSTRMN+L_intNEWMN) / 60;
            int  L_intTOTMN = (L_intSTRMN+L_intNEWMN)- ((L_intSTRMN+L_intNEWMN)/60)*60;
            String L_strTOTHR1 = "0000"+String.valueOf(L_intTOTHR).trim();
            String L_strTOTMN1 = "0000"+String.valueOf(L_intTOTMN).trim();                
            int L_intLENHR = L_strTOTHR1.length();
            int L_intLENMN = L_strTOTMN1.length();
            int L_intTOTCL;
            if (L_intTOTHR < 100)  
                L_intTOTCL = 2;
             else if (L_intTOTHR < 1000)
                    L_intTOTCL = 3;
                else 
                    L_intTOTCL = 4;
           L_strRETSTR = L_strTOTHR1.substring(L_intLENHR-L_intTOTCL,L_intLENHR)+":"+L_strTOTMN1.substring(L_intLENMN-2,L_intLENMN);                
           return  L_strRETSTR;
		}
        catch(Exception L_EX)
        {
		    setMSG(L_EX, "addTIME");
    		return "";
		}            
    }
    /**
    Method to Calculate the differance two Date & Time.
	@param P_strFINTM String argument to Final Time.
	@param P_strINITM String argument to Initial Time.
    */
	public String calTIME(String P_strFINTM,String P_strINITM)
	{
		String L_strDIFTM = "";
		try
		{
			int L_intYRS,L_intMTH,L_intDAY,L_intHRS,L_intMIN;
			int L_intYRS1,L_intMTH1,L_intDAY1,L_intHRS1,L_intMIN1;
			int L_intYRS2,L_intMTH2,L_intDAY2,L_intHRS2,L_intMIN2;
			String L_strHOUR,L_strMINT;			
			if(P_strFINTM.equals("") || P_strINITM.equals(""))
				return L_strDIFTM;			
			// Seperating year,month,day,hour & minute from Final time
			L_intYRS1 = Integer.parseInt(P_strFINTM.substring(6,10));
			L_intMTH1 = Integer.parseInt(P_strFINTM.substring(3,5));
			L_intDAY1 = Integer.parseInt(P_strFINTM.substring(0,2));
			L_intHRS1 = Integer.parseInt(P_strFINTM.substring(11,13));
			L_intMIN1 = Integer.parseInt(P_strFINTM.substring(14));			
			// Seperating year,month,day,hour & minute from Initial time
			L_intYRS2 = Integer.parseInt(P_strINITM.substring(6,10));
			L_intMTH2 = Integer.parseInt(P_strINITM.substring(3,5));
			L_intDAY2 = Integer.parseInt(P_strINITM.substring(0,2));
			L_intHRS2 = Integer.parseInt(P_strINITM.substring(11,13));
			L_intMIN2 = Integer.parseInt(P_strINITM.substring(14));			
			L_intMIN = L_intMIN1 - L_intMIN2;
			L_intHRS = L_intHRS1 - L_intHRS2;			
			// Checking for leap year
			if(L_intYRS1%4 == 0)
				arrDAYS[1] = "29";
			else
				arrDAYS[1] = "28";			
			// Final date is of next month
			if(L_intMTH1 > L_intMTH2)
			{
				for(int i = L_intMTH2;i < L_intMTH1;i++)
					L_intDAY1 += Integer.parseInt(arrDAYS[i-1]);
			}			
			L_intDAY = L_intDAY1 - L_intDAY2;			
			if(L_intMIN < 0)
			{
				L_intMIN += 60;
				L_intHRS--;
			}
			if(L_intHRS < 0)
			{
				L_intHRS += 24;
				L_intDAY--;
			}
			if(L_intDAY > 0)
				L_intHRS += L_intDAY*24;			
			L_strHOUR = String.valueOf(L_intHRS);
			L_strMINT = String.valueOf(L_intMIN);
			if(L_strHOUR.length() < 2)
				L_strHOUR = "0" + L_strHOUR;
			if(L_strMINT.length() < 2)
				L_strMINT = "0" + L_strMINT;
			L_strDIFTM = L_strHOUR + ":" + L_strMINT;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calTIME");
		}
		return L_strDIFTM;
	}	
}

