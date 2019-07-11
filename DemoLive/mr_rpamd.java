/*
System Name   : Marketing Management System
Program Name  : Exclusive Transaction Report
Program Desc. : Exclusive Transaction Report

Author        : Mr S.R.Tawde
Date          : 13.08.2005
Version       : MARKSYS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;import javax.swing.JPanel;
import java.awt.Font;import java.awt.Color;import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;import javax.swing.InputVerifier;import javax.swing.JComponent;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;
/**<pre>
<b>System Name :</b> Marketing Management System.
 
<b>Program Name :</b> Exclusive Transaction Report

<b>Purpose :</b> This module gives Amendements/Cancellations related to Indents/D.O./Invoice between given dates.

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
VW_INTRN															#
MR_INTAM	  INT_MKTTP,INT_INDNO,INT_PRDCD,INT_PKGTP				#
MR_DOTRN	  DOT_MKTTP,DOT_DORNO,DOT_PRDCD,DOT_PKGTP				#
MR_DOTAM	  DOT_MKTTP,DOT_DORNO,DOT_PRDCD,DOT_PKGTP				#	
CO_PTMST	  PT_PRTTP,PT_PRTCD										#
CO_CDTRN      CMT_CGMTP,CMT_CGSTP,CMT_CODCD							#
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table Name      Type/Size     Description
--------------------------------------------------------------------------------
txtFRMDT    LW_RCTDT       FG_LWMST        Date          From Date
txtTORDT    LW_RCTDT       FG_LWMST        Date          From Date
rdbINDTP	---------------- Select for Indent Type Documents ------------------
rdbDORTP	---------------- Select for Delivery Type Documents ----------------
--------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description			      Display Columns			Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------

-----------------------------------------------------------------------------------------------------------------------------------------------------
<I>
<B>Conditions Give in Query:</b>
             Data is taken from VW_INTRN A,MR_INTAM B,MR_DOTRN,MR_DOTAM,CO_PTMST,CO_CDTRN C,CO_CDTRN D between the given date range 
             and depending upon the selection of Document Type i.e.Indent or Delivery Order.
			 1) PT_PRTTP='C' AND PT_PRTCD=IN_BYRCD 
			 2) D.CMT_CGMTP='SYS' AND D.CMT_CGSTP='MR00SAL' AND D.CMT_CODCD=IN_SALTP 
			 3) isnull(IN_AMDNO,'00') !='00' AND A.INT_LUPDT BETWEEN '"+L_strFMDAT+"' AND '"+L_strTODAT+"' 
			 4) A.IN_SALTP IN ('01','03','12') AND C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MR00ZON' AND IN_ZONCD=C.CMT_CODCD 
			 5) A.IN_INDDT != A.IN_LUPDT  
			 6) ORDER BY C.CMT_CHP02,IN_SALTP,A.INT_PRDGR,A.INT_INDNO ";             
</I>
Validations :
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
 */
public class mr_rpamd extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;		/** JTextField to accept Main Product Category.*/
	private JTextField txtMNPRD;        /** JTextField to accept Shift Code.*/
	private JTextField txtSHFCD;		/** JTextField to accept online Remark just to print on the report.*/
	private JTextField txtTREMDS;		/** Integer to store No.of packages.*/
	private int intRCTPK = 0;
	
	private JRadioButton rdbDOCTP,rdbINDTP,rdbDORTP;
	private ButtonGroup bgrDOCTP;
	
										/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;	
	
	
	/**
	 *1.Screen Designing
	 *2.Hashtable is created using CO_CDTRN for maintaining various types of codes alongwith
	 *  their descriptions.
	 */
	mr_rpamd()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("From Date"),5,2,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,3,1,1,this,'L');			
			add(new JLabel("To Date"),5,4,1,1.5,this,'L');
			add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');

			JPanel pnlDOCTP = new JPanel();
			setMatrix(20,8);
			pnlDOCTP.setLayout(null);
			bgrDOCTP=new ButtonGroup();
			add(rdbINDTP = new JRadioButton("Indent",true),1,2,1,1,pnlDOCTP,'L');
			add(rdbDORTP = new JRadioButton("Delivery Order",false),1,3,1,1,pnlDOCTP,'L');
			bgrDOCTP.add(rdbINDTP);
			bgrDOCTP.add(rdbDORTP);
			pnlDOCTP.setBorder(BorderFactory.createTitledBorder(null,"Document Types",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman")));
			add(pnlDOCTP,6,2,2,4,this,'L');
				
			/** Registering the components with inputverifier */
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			
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
     *For populating FROM DATE AND TO DATE with required default values 
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
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)			
				txtTODAT.requestFocus();
			else if(M_objSOURC == txtTODAT)			
				rdbINDTP.requestFocus();
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpext.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpext.doc";				
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
					  setMSG("No data found, Please check Date Range OR Product Category OR Shift Code..",'E');				    
			}				
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
		    {
			    cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Gradewise Receipt Detail Report"," ");
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
		String L_strFMDAT,L_strTODAT,L_strPRDCT="",L_strSHFCT="",L_strRGNDS="";
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		double L_dblBAGQT = 0;
		
		double L_dblDOCQT = 0,L_dblPREDQ = 0,L_dblPGRQT = 0,L_dblPPGRQT = 0 ,L_dblDIFQT = 0,L_dblTDIFQT = 0;
		double L_dblPDOCQT = 0,L_dblPPREDQ = 0,L_dblCDOCQT = 0,L_dblCPREDQ=0;

		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPDOCNO = new String();
		String L_strPAMDNO = new String();
		String L_strPDOCDT = new String();
		String L_strPAMDDT = new String();
		String L_strPRGNCD = new String();
		String L_strPZONCD = new String();
		String L_strPSALTP = new String();
		String L_strPSALDS = new String();
		String L_strPBUYER = new String();
		String L_strPPRDGR = new String();
		String L_strPPRDDS = new String();
		String L_strPLUSBY = new String();
	

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strDOCNO = new String();
		String L_strAMDNO = new String();
		String L_strDOCDT = new String();
		String L_strAMDDT = new String();
		String L_strRGNCD = new String();
		String L_strZONCD = new String();
		String L_strSALTP = new String();
		String L_strSALDS = new String();
		String L_strBUYER = new String();
		String L_strPRDGR = new String();
		String L_strPRDDS = new String();
		String L_strLUSBY = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCDOCNO = new String();
		String L_strCAMDNO = new String();
		String L_strCDOCDT = new String();
		String L_strCAMDDT = new String();
		String L_strCRGNCD = new String();
		String L_strCZONCD = new String();
		String L_strCSALTP = new String();
		String L_strCSALDS = new String();
		String L_strCBUYER = new String();
		String L_strCPRDGR = new String();
		String L_strCPRDDS = new String();
		String L_strCLUSBY = new String();
			
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
			    prnFMTCHR(dosREPORT,M_strCPI10);
				//prnFMTCHR(dosREPORT,M_strCPI12);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Gradewise Receipt Detail Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();			
    		
			L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			setMSG("Report Generation is in Progress.......",'N');	
			
			if(rdbDORTP.isSelected())
//			{
//			M_strSQLQRY = " SELECT  DISTINCT C.CMT_CHP02 RGNCD,A.DOT_DORNO DOCNO,A.DOT_AMDNO AMDNO,B.DOT_AMDNO,A.DOT_DORDT DOCDT, ";
//			M_strSQLQRY+= "	A.DOT_AMDDT AMDDT, SUBSTRING(A.DOT_SBSCD,1,2) ZONCD,SUBSTRING(A.DOT_SBSCD,3,2)  SALTP,D.CMT_CODDS SALDS, " ;
//			M_strSQLQRY+= "	PT_PRTNM BUYER,A.DOT_PRDDS PRDDS,INT_PRDGR PRDGR,A.DOT_DORQT DOCQT, B.DOT_DORQT PREDQ,A.DOT_LUSBY LUSBY " ;
//			M_strSQLQRY+= " FROM MR_DOTRN A,CO_PTMST,CO_CDTRN C,CO_CDTRN D," ;
//			M_strSQLQRY+= " VW_INTRN LEFT OUTER JOIN MR_DOTAM B ON A.DOT_SBSCD=B.DOT_SBSCD AND " ;
//			M_strSQLQRY+= " A.DOT_DORNO=B.DOT_DORNO    AND A.DOT_PRDCD=B.DOT_PRDCD   AND A.DOT_DORQT!= B.DOT_DORQT " ;
//			M_strSQLQRY+= " AND Integer(A.DOT_AMDNO)-Integer(B.DOT_AMDNO)=1 WHERE PT_PRTTP='C'  AND PT_PRTCD=IN_BYRCD " ;
//			M_strSQLQRY+= " AND  isnull(A.DOT_AMDNO,'00') !='00' AND A.DOT_LUPDT BETWEEN  '"+L_strFMDAT+"'"; 
//			M_strSQLQRY+= " AND '"+L_strTODAT+"' AND SUBSTRING(A.DOT_SBSCD,3,2)  IN ('01','03','12') " ;
//			M_strSQLQRY+= "	AND SUBSTRING(A.DOT_SBSCD,1,2)=C.CMT_CODCD AND C.CMT_CGMTP='SYS'  AND C.CMT_CGSTP='MR00ZON' " ;
//			M_strSQLQRY+= " AND D.CMT_CGMTP='SYS' AND D.CMT_CGSTP='MR00SAL' AND D.CMT_CODCD=SUBSTRING(A.DOT_SBSCD,3,2)" ;
//			M_strSQLQRY+= " AND A.DOT_SBSCD=IN_SBSCD AND IN_INDNO=A.DOT_INDNO and int_indno=A.dot_indno AND INT_PRDCD=A.DOT_PRDCD " ;
//			M_strSQLQRY+= "ORDER BY C.CMT_CHP02,SUBSTRING(A.DOT_SBSCD,3,2),INT_PRDGR,A.DOT_DORNO ";
//			}
			{
			M_strSQLQRY = " SELECT  distinct C.CMT_CHP02 RGNCD,A.DOT_DORNO DOCNO,A.DOT_AMDNO AMDNO,A.DOT_DORDT DOCDT,";
			M_strSQLQRY+= " A.DOT_AMDDT AMDDT, SUBSTRING(A.DOT_SBSCD,1,2) ZONCD,SUBSTRING(A.DOT_SBSCD,3,2) SALTP, ";
			M_strSQLQRY+= " D.CMT_CODDS SALDS,PT_PRTNM BUYER,A.DOT_PRDDS PRDDS,INT_PRDGR PRDGR, A.DOT_DORQT DOCQT,";
			M_strSQLQRY+= " B.DOT_DORQT PREDQ,IN_LUSBY LUSBY FROM MR_DOTRN A, CO_PTMST,";
			M_strSQLQRY+= " CO_CDTRN C,CO_CDTRN D,VW_INTRN I INNER JOIN MR_DOTAM B ON ";
			M_strSQLQRY+= " A.DOT_CMPCD=B.DOT_CMPCD AND A.DOT_SBSCD=B.DOT_SBSCD AND A.DOT_DORNO=B.DOT_DORNO  AND A.DOT_PRDCD=B.DOT_PRDCD AND ";
			M_strSQLQRY+= " cast ((A.DOT_AMDNO) as int) -cast((B.DOT_AMDNO) as int)=1  AND A.DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND A.DOT_DORQT!= B.DOT_DORQT ";
			M_strSQLQRY+= " WHERE PT_PRTTP='C'  AND PT_PRTCD=IN_BYRCD AND D.CMT_CGMTP='SYS' AND  D.CMT_CGSTP='MR00SAL' ";
			M_strSQLQRY+= " AND D.CMT_CODCD=SUBSTRING(A.DOT_SBSCD,3,2) AND isnull(A.DOT_AMDNO,'00') !='00' AND A.DOT_AMDDT ";
			M_strSQLQRY+= " BETWEEN '"+L_strFMDAT+"' AND '"+L_strTODAT+"'  AND SUBSTRING(A.DOT_SBSCD,3,2) IN ('01','03','12') AND ";
			M_strSQLQRY+= " C.CMT_CGMTP='SYS' AND  C.CMT_CGSTP='MR00ZON' AND SUBSTRING(A.DOT_SBSCD,1,2)=C.CMT_CODCD AND ";
			M_strSQLQRY+= " A.DOT_DORDT != A.DOT_LUPDT AND A.DOT_CMPCD=INT_CMPCD AND A.DOT_SBSCD=INT_SBSCD AND A.DOT_INDNO=INT_INDNO AND ";
			M_strSQLQRY+= " A.DOT_PRDCD=INT_PRDCD UNION ALL  SELECT  G.CMT_CHP02 RGNCD,E.DOT_DORNO DOCNO,";
			M_strSQLQRY+= " E.DOT_AMDNO AMDNO,E.DOT_DORDT DOCDT,E.DOT_AMDDT AMDDT,  SUBSTRING(E.DOT_SBSCD,1,2) ZONCD,";
			M_strSQLQRY+= " SUBSTRING(E.DOT_SBSCD,3,2) SALTP,H.CMT_CODDS SALDS,PT_PRTNM BUYER,E.DOT_PRDDS PRDDS,";
			M_strSQLQRY+= " INT_PRDGR PRDGR, E.DOT_DORQT DOCQT, 0  PREDQ,DOT_LUSBY LUSBY FROM MR_DOTRN E,";
			M_strSQLQRY+= " CO_PTMST,CO_CDTRN G,CO_CDTRN H,VW_INTRN  WHERE ";
			M_strSQLQRY+= " E.DOT_CMPCD + E.DOT_SBSCD + E.DOT_DORNO + E.DOT_PRDCD NOT IN (SELECT DOT_CMPCD + DOT_SBSCD + DOT_DORNO + DOT_PRDCD FROM ";
			M_strSQLQRY+= " MR_DOTAM where dot_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"') and  PT_PRTTP='C'  AND PT_PRTCD=IN_BYRCD AND H.CMT_CGMTP='SYS' AND ";
			M_strSQLQRY+= " H.CMT_CGSTP='MR00SAL' AND H.CMT_CODCD=SUBSTRING(E.DOT_SBSCD,3,2) AND ";
			M_strSQLQRY+= " E.DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(E.DOT_AMDNO,'00') !='00' AND E.DOT_AMDDT BETWEEN '"+L_strFMDAT+"' AND '"+L_strTODAT+"'";
			M_strSQLQRY+= " AND SUBSTRING(E.DOT_SBSCD,3,2) IN ('01','03','12') AND G.CMT_CGMTP='SYS' AND G.CMT_CGSTP='MR00ZON' ";
			M_strSQLQRY+= " AND SUBSTRING(E.DOT_SBSCD,1,2)=G.CMT_CODCD AND  E.DOT_DORDT != E.DOT_LUPDT ";
			M_strSQLQRY+= " ORDER BY RGNCD,SALTP,PRDGR,DOCNO ";	
			}			
			
			if(rdbINDTP.isSelected())

//			{	
//			M_strSQLQRY = " SELECT  C.CMT_CHP02 RGNCD,A.INT_INDNO DOCNO,A.INT_AMDNO AMDNO,B.INT_AMDNO,IN_INDDT DOCDT,IN_AMDDT AMDDT, " ;
//			M_strSQLQRY+= "	IN_ZONCD ZONCD,IN_SALTP SALTP,D.CMT_CODDS SALDS,PT_PRTNM BUYER,A.INT_PRDGR PRDGR,A.INT_PRDDS PRDDS,A.INT_INDQT DOCQT,A.INT_CFWQT CFWQT, ";
//			M_strSQLQRY+= "	B.INT_INDQT PREDQ,IN_LUSBY LUSBY FROM VW_INTRN A,CO_PTMST,CO_CDTRN C,CO_CDTRN D";
//			M_strSQLQRY+= " LEFT OUTER JOIN MR_INTAM B ON A.INT_SBSCD=B.INT_SBSCD AND A.INT_INDNO=B.INT_INDNO ";
//			M_strSQLQRY+= " AND A.INT_PRDCD=B.INT_PRDCD AND Integer(A.INT_AMDNO)-Integer(B.INT_AMDNO)=1  AND A.INT_INDQT!= B.INT_INDQT ";
//			M_strSQLQRY+= " WHERE PT_PRTTP='C'  AND PT_PRTCD=IN_BYRCD AND D.CMT_CGMTP='SYS' AND ";
//			M_strSQLQRY+= " D.CMT_CGSTP='MR00SAL' AND D.CMT_CODCD=IN_SALTP AND isnull(IN_AMDNO,'00') !='00' AND A.IN_AMDDT BETWEEN '"+L_strFMDAT+"'";
//			M_strSQLQRY+= " AND '"+L_strTODAT+"' AND A.IN_SALTP IN ('01','03','12') AND C.CMT_CGMTP='SYS' AND C.CMT_CGSTP='MR00ZON' AND IN_ZONCD=C.CMT_CODCD AND A.IN_INDDT != A.IN_LUPDT  " ;
//			M_strSQLQRY+= " ORDER BY C.CMT_CHP02,IN_SALTP,A.INT_PRDGR,A.INT_INDNO ";
//			}
			{

			M_strSQLQRY = " SELECT  C.CMT_CHP02 RGNCD,A.INT_INDNO DOCNO,A.INT_AMDNO AMDNO,IN_INDDT DOCDT,IN_AMDDT AMDDT, ";
			M_strSQLQRY+= "	IN_ZONCD ZONCD,IN_SALTP SALTP,D.CMT_CODDS SALDS,PT_PRTNM BUYER,A.INT_PRDGR PRDGR,A.INT_PRDDS PRDDS, ";
			M_strSQLQRY+= " (isnull(a.int_indqt,0)-isnull(a.int_fcmqt,0)) DOCQT,A.INT_CFWQT CFWQT,B.INT_INDQT PREDQ,IN_LUSBY LUSBY FROM VW_INTRN A,";
			M_strSQLQRY+= " CO_PTMST,CO_CDTRN C,CO_CDTRN D  INNER JOIN MR_INTAM B ON ";
			M_strSQLQRY+= " A.INT_CMPCD=B.INT_CMPCD AND A.INT_SBSCD=B.INT_SBSCD AND A.INT_INDNO=B.INT_INDNO  AND A.INT_PRDCD=B.INT_PRDCD AND ";
			M_strSQLQRY+= " cast ((A.INT_AMDNO) as int)- cast((B.INT_AMDNO) as int)=1  AND A.INT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (isnull(a.int_indqt,0)-isnull(a.int_fcmqt,0))!= (isnull(b.int_indqt,0)-isnull(b.int_fcmqt,0)) ";
			M_strSQLQRY+= " WHERE PT_PRTTP='C'  AND PT_PRTCD=IN_BYRCD AND D.CMT_CGMTP='SYS' AND  D.CMT_CGSTP='MR00SAL'";
			M_strSQLQRY+= " AND D.CMT_CODCD=IN_SALTP AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_AMDNO,'00') !='00' AND A.IN_AMDDT BETWEEN '"+L_strFMDAT+"'";
			M_strSQLQRY+= " AND '"+L_strTODAT+"'  AND A.IN_SALTP IN ('01','03','12') AND C.CMT_CGMTP='SYS' AND ";
			M_strSQLQRY+= " C.CMT_CGSTP='MR00ZON' AND IN_ZONCD=C.CMT_CODCD AND A.IN_INDDT != A.IN_LUPDT  UNION ALL ";
			M_strSQLQRY+= " SELECT  G.CMT_CHP02 RGNCD,E.INT_INDNO DOCNO,E.INT_AMDNO AMDNO,IN_INDDT DOCDT,IN_AMDDT AMDDT, ";
			M_strSQLQRY+= " IN_ZONCD ZONCD,IN_SALTP SALTP,H.CMT_CODDS SALDS,PT_PRTNM BUYER,E.INT_PRDGR PRDGR,";
			M_strSQLQRY+= " E.INT_PRDDS PRDDS,(isnull(e.int_indqt,0)-isnull(e.int_fcmqt,0)) DOCQT,E.INT_CFWQT CFWQT,0  PREDQ,IN_LUSBY LUSBY FROM ";
			M_strSQLQRY+= " VW_INTRN E,CO_PTMST,CO_CDTRN G,CO_CDTRN H WHERE ";
			M_strSQLQRY+= " E.INT_CMPCD + E.INT_SBSCD + E.INT_INDNO + E.INT_PRDCD NOT IN(SELECT INT_CMPCD + INT_SBSCD + INT_INDNO + INT_PRDCD FROM ";
			M_strSQLQRY+= " MR_INTAM where int_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"') and  PT_PRTTP='C'  AND PT_PRTCD=IN_BYRCD AND H.CMT_CGMTP='SYS' AND ";
			M_strSQLQRY+= " H.CMT_CGSTP='MR00SAL' AND H.CMT_CODCD=IN_SALTP AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_AMDNO,'00') !='00' AND ";
			M_strSQLQRY+= " E.IN_AMDDT BETWEEN '"+L_strFMDAT+"' AND '"+L_strTODAT+"'  AND E.IN_SALTP IN ('01','03','12') AND ";
			M_strSQLQRY+= " G.CMT_CGMTP='SYS' AND G.CMT_CGSTP='MR00ZON' AND IN_ZONCD=G.CMT_CODCD AND ";
			M_strSQLQRY+= " E.IN_INDDT != E.IN_LUPDT ORDER BY RGNCD,SALTP,PRDGR,DOCNO ";				
			}	
			
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);

			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					L_strDOCNO = nvlSTRVL(M_rstRSSET.getString("DOCNO"),"");
					L_strAMDNO = nvlSTRVL(M_rstRSSET.getString("AMDNO"),"");
					L_strDOCDT = nvlSTRVL(M_rstRSSET.getString("DOCDT"),"");
					L_strAMDDT = nvlSTRVL(M_rstRSSET.getString("AMDDT"),"");
					L_strZONCD = nvlSTRVL(M_rstRSSET.getString("ZONCD"),"");
					L_strRGNCD = nvlSTRVL(M_rstRSSET.getString("RGNCD"),"");
					L_strSALTP = nvlSTRVL(M_rstRSSET.getString("SALTP"),"");
					L_strSALDS = nvlSTRVL(M_rstRSSET.getString("SALDS"),"");
					L_strBUYER = nvlSTRVL(M_rstRSSET.getString("BUYER"),"");
					L_strPRDGR = nvlSTRVL(M_rstRSSET.getString("PRDGR"),"");
					L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("PRDDS"),"");
					L_dblDOCQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("DOCQT"),"0"));
					L_dblPREDQ = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PREDQ"),"0"));
					if(rdbINDTP.isSelected() && (Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("CFWQT"),"0"))) > 0 )
						L_dblDOCQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("CFWQT"),"0"));
					L_strLUSBY = nvlSTRVL(M_rstRSSET.getString("LUSBY"),"");
		
					if(L_strRGNCD.equals("01"))
					   L_strRGNDS = "West & Central";
					if(L_strRGNCD.equals("02"))
						L_strRGNDS = "North, East & South";
					if(L_strRGNCD.equals("03"))
						L_strRGNDS = "Export";
					
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
					}
					if(L_flg1STSFL){
						
						L_strPDOCNO = L_strDOCNO;
						L_strPDOCDT = L_strDOCDT;
						L_strPAMDNO = L_strAMDNO;
						L_strPRGNCD = L_strRGNCD;
						L_strPZONCD = L_strZONCD;
						L_strPSALTP = L_strSALTP;
						L_strPSALDS = L_strSALDS;
						L_strPBUYER = L_strBUYER;
						L_strPPRDGR = L_strPRDGR;
						L_strPPRDDS = L_strPRDDS;
						L_strPLUSBY = L_strLUSBY;
						L_dblPDOCQT = L_dblDOCQT;
						L_dblPPREDQ = L_dblPREDQ;
						
						L_strCDOCNO = L_strDOCNO;
						L_strCDOCDT = L_strDOCDT;
						L_strCAMDNO = L_strAMDNO;
						L_strCRGNCD = L_strRGNCD;
						L_strCZONCD = L_strZONCD;
						L_strCSALTP = L_strSALTP;
						L_strCSALDS = L_strSALDS;
						L_strCBUYER = L_strBUYER;
						L_strCPRDGR = L_strPRDGR;
						L_strCPRDDS = L_strPRDDS;
						L_strCLUSBY = L_strLUSBY;
						L_dblCDOCQT = L_dblDOCQT;
						L_dblCPREDQ = L_dblPREDQ;
						
						L_flg1STSFL = false;
					}
			while(!L_flgEOF)		
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(L_strRGNDS+"\n");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				cl_dat.M_intLINNO_pbst+= 1;
				L_strPRGNCD = L_strRGNCD;
				while(L_strRGNCD.equals(L_strPRGNCD) && !L_flgEOF)
				{
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
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
					dosREPORT.writeBytes(L_strSALDS+"\n");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
   					cl_dat.M_intLINNO_pbst+= 1;
					L_strPSALTP = L_strSALTP;
					while((L_strRGNCD+L_strSALTP).equals(L_strPRGNCD+L_strPSALTP) && !L_flgEOF)								
					{
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
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
						dosREPORT.writeBytes(L_strPRDGR+"\n");
	   					cl_dat.M_intLINNO_pbst+= 1;
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						L_strSALTP = L_strCSALTP;
						L_strPSALTP = L_strSALTP;
						while((L_strRGNCD+L_strSALTP+L_strPRDGR).equals(L_strPRGNCD+L_strPSALTP+L_strPPRDGR) && !L_flgEOF)
						{
							if(cl_dat.M_intLINNO_pbst >= 64)
							{	
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}
							L_strSALTP = L_strCSALTP;
							L_strPRDGR = L_strCPRDGR;
							L_strPSALTP = L_strSALTP;
							L_strPPRDGR = L_strPRDGR;
							while((L_strRGNCD+L_strSALTP+L_strPRDGR+L_strDOCNO).equals(L_strPRGNCD+L_strPSALTP+L_strPPRDGR+L_strPDOCNO) && !L_flgEOF)
							{
								L_strSALTP = L_strCSALTP;
								L_strPRDGR = L_strCPRDGR;
								L_strDOCNO = L_strCDOCNO;
								L_strPSALTP = L_strSALTP;
								L_strPPRDGR = L_strPRDGR;
								L_strPDOCNO = L_strDOCNO;
								while((L_strRGNCD+L_strSALTP+L_strPRDGR+L_strDOCNO+L_strPRDDS).equals(L_strPRGNCD+L_strPSALTP+L_strPPRDGR+L_strPDOCNO+L_strPPRDDS) && !L_flgEOF)
								{
									intRECCT++;								
									dosREPORT.writeBytes(padSTRING('L',"",12));	
									dosREPORT.writeBytes(padSTRING('R',L_strDOCNO,10));
									dosREPORT.writeBytes(padSTRING('R',L_strAMDNO,11));
									dosREPORT.writeBytes(padSTRING('R',L_strDOCDT,10));
									dosREPORT.writeBytes(padSTRING('R',L_strAMDDT,10));
									dosREPORT.writeBytes(padSTRING('R',L_strBUYER,35));
									dosREPORT.writeBytes(padSTRING('R',L_strPRDDS,17));
		    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDOCQT,3),10));
		    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPREDQ,3),10));
									L_dblDIFQT = L_dblDOCQT - L_dblPREDQ;
		    						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblDIFQT,3),10));
									dosREPORT.writeBytes(padSTRING('L',"",4));	
									dosREPORT.writeBytes(padSTRING('R',L_strLUSBY,3));
									dosREPORT.writeBytes("\n");
				   					cl_dat.M_intLINNO_pbst+= 1;
									if(cl_dat.M_intLINNO_pbst >= 64)
									{	
									dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
									if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
											prnFMTCHR(dosREPORT,M_strEJT);
										if(M_rdbHTML.isSelected())
											dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
										prnHEADER();
									}
									L_dblTDIFQT+= L_dblDIFQT;
									L_dblPGRQT+= L_dblDOCQT;
									L_dblPPGRQT+= L_dblPREDQ;
									L_dblDIFQT = 0;
									if(!M_rstRSSET.next())
									{
										L_flgEOF = true;
										break;
									}
									L_strCDOCNO = nvlSTRVL(M_rstRSSET.getString("DOCNO"),"");
									L_strCAMDNO = nvlSTRVL(M_rstRSSET.getString("AMDNO"),"");
									L_strCDOCDT = nvlSTRVL(M_rstRSSET.getString("DOCDT"),"");
									L_strCAMDDT = nvlSTRVL(M_rstRSSET.getString("AMDDT"),"");
									L_strCZONCD = nvlSTRVL(M_rstRSSET.getString("ZONCD"),"");
									L_strCRGNCD = nvlSTRVL(M_rstRSSET.getString("RGNCD"),"");
									L_strCSALTP = nvlSTRVL(M_rstRSSET.getString("SALTP"),"");
									L_strCSALDS = nvlSTRVL(M_rstRSSET.getString("SALDS"),"");
									L_strCBUYER = nvlSTRVL(M_rstRSSET.getString("BUYER"),"");
									L_strCPRDGR = nvlSTRVL(M_rstRSSET.getString("PRDGR"),"");
									L_strCPRDDS = nvlSTRVL(M_rstRSSET.getString("PRDDS"),"");
									L_dblCDOCQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("DOCQT"),"0"));
									L_dblCPREDQ = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PREDQ"),"0"));
									if(rdbINDTP.isSelected() && (Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("CFWQT"),"0"))) > 0 )
										L_dblCDOCQT = Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("CFWQT"),"0"));
									L_strCLUSBY = nvlSTRVL(M_rstRSSET.getString("LUSBY"),"");
									
									L_strRGNCD = L_strCRGNCD;
									L_strSALTP = L_strCSALTP;
									L_strSALDS = L_strCSALDS;
									L_strPRDGR = L_strCPRDGR;
									L_strDOCNO = L_strCDOCNO;
									L_strPRDDS = L_strCPRDDS;
									L_strDOCDT = L_strCDOCDT;
									L_strBUYER = L_strCBUYER;
									L_strAMDNO = L_strCAMDNO;
									L_strAMDDT = L_strCAMDDT;
									L_dblDOCQT = L_dblCDOCQT;
									L_dblPREDQ = L_dblCPREDQ;
									L_strZONCD = L_strCZONCD;
									L_strLUSBY = L_strCLUSBY;
									
									if(L_strRGNCD.equals("01"))
									L_strRGNDS = "West & Central";
									if(L_strRGNCD.equals("02"))
									L_strRGNDS = "North, East & South";
									if(L_strRGNCD.equals("03"))
									L_strRGNDS = "Export";
								}//	while((L_strRGNCD+L_strSALTP+L_strPRDGR+L_strDOCNO+L_strPRDDS).equals(L_strPRGNCD+L_strPSALTP+L_strPPRDGR+L_strPDOCNO+L_strPPRDDS) && !L_flgEOF)
								L_strPPRDDS = L_strPRDDS;
							}//while((L_strRGNCD+L_strSALTP+L_strPRDGR+L_strDOCNO)	
							if(cl_dat.M_intLINNO_pbst >= 64)
							{	
								dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
								if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
									prnFMTCHR(dosREPORT,M_strEJT);
								if(M_rdbHTML.isSelected())
									dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
								prnHEADER();
							}
								L_strPDOCNO = L_strDOCNO;
						}//while((L_strRGNCD+L_strSALTP+L_strPRDGR)	
						if(cl_dat.M_intLINNO_pbst >= 64)
						{	
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
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
						dosREPORT.writeBytes(padSTRING('L',"",8));
						dosREPORT.writeBytes(padSTRING('R',"Total "+L_strPPRDGR,97));						
		    			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPGRQT,3),10));
		    			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblPPGRQT,3),10));
						dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTDIFQT,3),10)+"\n");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("</B>");
						dosREPORT.writeBytes("\n");
				   		cl_dat.M_intLINNO_pbst+= 2;
						L_dblPGRQT = 0;
						L_dblPPGRQT = 0;
						L_dblTDIFQT = 0;
						L_strPPRDGR = L_strPRDGR;
					}//while((L_strRGNCD+L_strSALTP)	
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
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
					dosREPORT.writeBytes(padSTRING('L',"",4));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("</B>");
				   	cl_dat.M_intLINNO_pbst+= 2;
					L_strPSALTP=L_strSALTP;
				}//while((L_strRGNCD)
				if(cl_dat.M_intLINNO_pbst >= 64)
				{	
					dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------");		
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
					prnHEADER();
				}
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst+= 1;
				L_strPRGNCD = L_strRGNCD;
			}//while(eof())
		}	
            prnFOOTR();
			if(dosREPORT !=null)
			{	
				dosREPORT.close();
			}
			if(fosREPORT !=null)
			{	
				fosREPORT.close();
			}
		    if(M_rstRSSET != null)
			{	
				M_rstRSSET.close();						
			}
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
	/**
	Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			dosREPORT.writeBytes("\n");

			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,120));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Exclusive Trasaction Report between " +txtFMDAT.getText().trim()+ " and " + txtTODAT.getText().trim(),120));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("Region   \n");		    			
			dosREPORT.writeBytes("    Sale Type \n");
			dosREPORT.writeBytes("        Product Category \n");
			dosREPORT.writeBytes("            Document & Amnd.No.  Document & Amnd.Date  Buyer                            Grade              Amnd.Qty  Orig.Qty.  Difference User \n");
			dosREPORT.writeBytes("                                                                                                             (MT)       (MT)       (MT)         \n");								
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------\n");		
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
		dosREPORT.writeBytes("------------------------------------------------------------------------------------------------------------------------------------------------\n");		
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
	    {   
			prnFMTCHR(dosREPORT,M_strEJT);
			prnFMTCHR(dosREPORT,M_strNOCPI17);
			prnFMTCHR(dosREPORT,M_strCPI10);
		}	
	}
	catch(Exception L_EX)
	{
		setMSG(L_EX + " prnFOOTR",'E');
	}
}
}

