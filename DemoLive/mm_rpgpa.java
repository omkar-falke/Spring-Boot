/*
System Name   : Material Management System
Program Name  : Material Groupwise Purchase Analysis Report on Financial year Basis
Program Desc. : Material Groupwise Purchase Analysis Report on Financial year Basis

Author        : Mr S.R.Tawde
Date          : 29.12.2005
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
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Material Groupwise Purchase Analysis Report on Financial year Basis

<b>Purpose :</b> This module gives Material Groupwise Purchase Analysis Report on Financial year Basis for given Material Group

List of tables used :
Table Name    Primary key                           Operation done
                                                Insert   Update   Query   Delete	
--------------------------------------------------------------------------------
CO_CTMST
MM_POMST
--------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name         Table Name         Type/Size    Description
--------------------------------------------------------------------------------
txtFMDAT    PO_PORDT            MM_POMST           Date         From Date
txtTODAT    PO_PORDT            MM_POMST           Date         To Date
txtMATMG    PO_MATCD/CTT_GRPCD  MM_POMST/CO_CTTRN  String		Main Group
txtMATSG    PO_MATCD/CTT_GRPCD  MM_POMST/CO_CTTRN  String       Sub Group   
rdbPODOM	-------------Select for Domestic Purchase Type----------------------
rdbPOIMP    ----------------Select for Imported Purchase Type-------------------
--------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description			      Display Columns		  	      Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtMATMG    Main Mat.Group,Description        CTT_GRPCD,CTT_MATDS             CO_CTTRN
txtMATSG    Sub. Material Group,Description   CTT_GRPCD,SUBSTR(CTT_MATCD,3,2) CO_CTTRN
-----------------------------------------------------------------------------------------------------------------------------------------------------
<I>

<B>Conditions Given in Query:</b>
             Data is taken from MM_POMST,CO_CTTRN between the given date range and for the entered Main Group and SubGroup. 
			 1) po_stsfl != 'X' 
             2)left(po_matcd,2)= Entered Main Group
             3)ct_matcd=po_matcd
             4)po_pordt between Entered From Date and To Date
             5)and po_curcd = Based on selection Domestic / Imported 
             6) order by po_strtp,po_MATCD,PO_PORDT DESC
</I>
Validations :
	1) Both(to date & from) dates should not be greater than today.
	2) From date should not be greater than To date.	 
    3) Main Group Code validation from CO_CTTRN (CTT_GRPCD,CTT_MATDS)
	4) Sub Group Code validation from CO_CTTRN (GROUP CODE = ENTERED GROUP CODE)
 */
public class mm_rpgpa extends cl_rbase
{ 										/** JTextField to accept from Date.*/
	private JTextField txtFMDAT;		/** JTextField to accept to Date.*/
	private JTextField txtTODAT;		/** JTextField to accept Main material Group*/
	private JTextField txtMATMG;		/**	JTextField to accept SubGroup*/
	private JTextField txtMATSG;		/** JTextField to Display Main Group Description*/
	private JTextField txtMGRDS;		/**	JTextField to Display SubGroup Description*/
	private JTextField txtSGRDS;

	private ButtonGroup bgrPORTP;
	private JRadioButton rdbPODOM,rdbPOIMP,rdbPOALL;
	
											/** Integer counter for counting total Records Retrieved.*/
	private int intRECCT;				/** String variable for Generated Rerport file Name.*/
	private String strFILNM;			/** File OutputStream Object for file handling.*/
	private FileOutputStream fosREPORT ;/** Data OutputStream for generating Report File.*/	
	private DataOutputStream dosREPORT ;/** Report Header String and Purchase Type Description*/	
	private String strRPHDR,strPURDS;   /** String variable for storing Store Type*/
	private String strSTRTP="";
	
												/** Hashtable for storing distinct indents in Financial Year 1*/
	private Hashtable<String,String> hstINDF1 = new Hashtable<String,String>();/** Hashtable for storing distinct indents in Financial Year 2*/
	private Hashtable<String,String> hstINDF2 = new Hashtable<String,String>();/** Hashtable for storing distinct indents in Financial Year 3*/
	private Hashtable<String,String> hstINDF3 = new Hashtable<String,String>();/** Hashtable for storing distinct POs in Financial Year 1*/
	private Hashtable<String,String> hstPORF1 = new Hashtable<String,String>();/** Hashtable for storing distinct POs in Financial Year 2*/	
	private Hashtable<String,String> hstPORF2 = new Hashtable<String,String>();/** Hashtable for storing distinct POs in Financial Year 3*/	
	private Hashtable<String,String> hstPORF3 = new Hashtable<String,String>();	

	private INPVF objINPVF = new INPVF();
	
	/**
	 *1.Screen Designing
	 *2.Hashtable is created using CO_CDTRN for maintaining various types of codes alongwith
	 *  their descriptions.
	 */
	mm_rpgpa()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			bgrPORTP = new ButtonGroup();
			
			add(new JLabel("From Date"),5,2,1,1.3,this,'L');
			add(txtFMDAT = new TxtDate(),5,3,1,1,this,'L');			
			add(new JLabel("To Date"),5,4,1,1.5,this,'L');
			add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');			
			add(new JLabel("Main Group"),7,2,1,1,this,'L');
			add(txtMATMG = new TxtLimit(2),7,3,1,1,this,'L');
			add(txtMGRDS = new TxtLimit(45),7,4,1,4,this,'L');
			add(new JLabel("Sub Group"),9,2,1,1,this,'L');
			add(txtMATSG = new TxtLimit(2),9,3,1,1,this,'L');
			add(txtSGRDS = new TxtLimit(45),9,4,1,4,this,'L');
			add(new JLabel("Purchase Type :"),11,2,1,1,this,'L');
			add(rdbPODOM = new JRadioButton("DOMESTIC",true),11,3,1,1,this,'L');
			add(rdbPOIMP = new JRadioButton("IMPORTED",false),11,4,1,1,this,'L');
			//add(rdbPOALL = new JRadioButton("ALL",false),11,5,1,1,this,'L');
			bgrPORTP.add(rdbPODOM);
			bgrPORTP.add(rdbPOIMP);
			//bgrPORTP.add(rdbPOALL);

			/** Registering the components with inputverifier */
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			txtFMDAT.setInputVerifier(objINPVF);
			txtTODAT.setInputVerifier(objINPVF);
			txtMATMG.setInputVerifier(objINPVF);
			txtMATSG.setInputVerifier(objINPVF);
			
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
		txtMGRDS.setText("");
		txtSGRDS.setText("");
		txtMGRDS.setEnabled(false);
		txtSGRDS.setEnabled(false);
		
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
			strSTRTP =M_strSBSCD.substring(2,4);
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
		if(M_objSOURC == rdbPODOM)
		strPURDS = " DOMESTIC ";
		if(M_objSOURC == rdbPOIMP)
		strPURDS = " IMPORTED ";
		//if(M_objSOURC == rdbPOALL)
		//strPURDS = " DOMESTIC & IMPORTED ";
	}	 
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)			
				txtTODAT.requestFocus();
			if(M_objSOURC == txtTODAT)
				txtMATMG.requestFocus();
			if(M_objSOURC == txtMATMG)
				txtMATSG.requestFocus();
			if(M_objSOURC == txtMATSG)
				rdbPODOM.requestFocus();
			if(M_objSOURC == rdbPODOM || M_objSOURC == rdbPOIMP)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		
		/**Method to display list of Main Groups and Subgroups in help window 
		when F1 is Pressed.*/
		
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtMATMG)
				{
					M_strHLPFLD = "txtMATMG";
					M_strSQLQRY = " SELECT CTT_GRPCD,CTT_MATDS FROM CO_CTTRN WHERE CTT_CODTP='MG' AND CTT_STSFL != 'X' ORDER BY CTT_GRPCD";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Main Group","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
				if(M_objSOURC == txtMATSG)
				{
					M_strHLPFLD = "txtMATSG";
					M_strSQLQRY = " SELECT SUBSTRING(CTT_MATCD,3,2),CTT_MATDS FROM CO_CTTRN WHERE CTT_CODTP='SG' ";
					M_strSQLQRY+= " AND CTT_GRPCD='"+txtMATMG.getText().toString().trim()+"' AND CTT_STSFL != 'X' ORDER BY LEFT(CTT_MATCD,4)";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"SubGroup","Description"},2,"CT");
					System.out.println(M_strSQLQRY);
					setCursor(cl_dat.M_curDFSTS_pbst);
				}	
			}	
			catch(Exception L_E)
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"Key Pressed F1");
			}
		}		
	}
		public void exeHLPOK()
		{
		super.exeHLPOK();
			if(M_strHLPFLD == "txtMATMG")
			{
				txtMATMG.setText(cl_dat.M_strHLPSTR_pbst);
			}	
			if(M_strHLPFLD == "txtMATSG")
			{
				txtMATSG.setText(cl_dat.M_strHLPSTR_pbst);
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpgpa.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgpa.doc";	
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
					  setMSG("No data found, Please check Date Range OR Main Group OR SubGroup...",'E');				    
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
	*Method to fetch Data from Database and start creation of the output file for Detail Report.
	*/
	private void getDATA()
	{ 	    		
		String L_strFMDAT,L_strTODAT,L_strPORDT,L_strPORNO,L_strINDNO;
		
		boolean L_flgEOF = false;
		boolean L_flg1STSFL = true;
		int L_intNOP1=0,L_intNOP2=0,L_intNOP3=0;
		int L_intINDF1,L_intINDF2,L_intINDF3;
		int L_intPORF1,L_intPORF2,L_intPORF3;
		double L_dblTOTQT1=0,L_dblTOTQT2=0,L_dblTOTQT3=0;
		double L_dblTOTVL1=0,L_dblTOTVL2=0,L_dblTOTVL3=0;
		double L_dblAVGRT1=0,L_dblAVGRT2=0,L_dblAVGRT3=0;
		double L_dblLSTRT1=0,L_dblLSTRT2=0,L_dblLSTRT3=0;
		
		double L_dblGTVL1=0,L_dblGTVL2=0,L_dblGTVL3=0;
		
		/* Variables Declared to store PREVIOUS values of Resultset **/
		String L_strPMATCD = new String();
		
		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strCMATCD = new String();

		/* Variables Declared to store CURRENT values of Resultset **/
		String L_strMATCD = new String();
		String L_strMATDS = new String();
		String L_strUOMCD = new String();

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
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
    		    dosREPORT.writeBytes("<HTML><HEAD><Title>Despatch Statement </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    							
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
    		prnHEADER();
			L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			
			setMSG("Report Generation is in Progress.......",'N');		
			M_strSQLQRY = " select PO_STRTP,PO_INDNO,PO_PORNO,po_matcd,ct_matds,ct_uomcd,po_pordt,po_porqt,po_porrt,po_exgrt,po_itval from mm_pomst,";
			M_strSQLQRY+= " co_ctmst  where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND po_stsfl not in('X','O') and left(po_matcd,2)='"+txtMATMG.getText().toString().trim()+"'";
			M_strSQLQRY+= " and ct_matcd=po_matcd and po_pordt between '"+L_strFMDAT+"' and '"+L_strTODAT+"' and PO_STRTP='"+strSTRTP+"'";
			if(rdbPODOM.isSelected())
				M_strSQLQRY+= " and po_curcd='01' ";
			if(rdbPOIMP.isSelected())
				M_strSQLQRY+= " and po_curcd != '01' ";
			if(txtMATSG.getText().toString().trim().length() > 0)
				M_strSQLQRY+= " and SUBSTRING(PO_MATCD,3,2)='"+txtMATSG.getText().toString().trim()+"'";
			M_strSQLQRY+= " order by po_strtp,po_MATCD,PO_PORDT DESC ";

			System.out.println(M_strSQLQRY);			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{	
				if(M_rstRSSET.next())
				{
					L_strMATCD = nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),"");
					L_strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
					L_strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"");
					L_strPORDT = M_fmtLCDAT.format(M_rstRSSET.getDate("PO_PORDT"));
					L_strPORNO = nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),"");
					L_strINDNO = nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),"");
					if(cl_dat.M_intLINNO_pbst >= 64)
					{	
						dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
							prnHEADER();
					}
					if(L_flg1STSFL){
						L_strPMATCD = L_strMATCD;
						L_strCMATCD = L_strMATCD;
						L_flg1STSFL = false;
					}

			while(!L_flgEOF)		
			{
				if(cl_dat.M_intLINNO_pbst >= 64)
				{	
					dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strEJT);
					if(M_rdbHTML.isSelected())
						dosREPORT.writeBytes("<P CLASS = \"breakhere\">");								
					prnHEADER();
				}
				dosREPORT.writeBytes(padSTRING('R',L_strMATCD,12));
				dosREPORT.writeBytes(padSTRING('R',L_strMATDS,47));
				dosREPORT.writeBytes(padSTRING('R',L_strUOMCD,5));
				L_strPMATCD = L_strMATCD;
				while(L_strMATCD.equals(L_strPMATCD) && !L_flgEOF)
				{
					intRECCT++;
					if(M_fmtLCDAT.parse(L_strPORDT).compareTo(M_fmtLCDAT.parse("01/07/2004"))>=0 && M_fmtLCDAT.parse(L_strPORDT).compareTo(M_fmtLCDAT.parse("30/06/2005"))<=0 )
					{	
						L_intNOP1+=1;
	   					L_dblTOTQT1+= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0"));
	   					L_dblTOTVL1+= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_ITVAL"),"0"));
	   					L_dblAVGRT1 = L_dblTOTVL1/L_dblTOTQT1;
						L_dblLSTRT1 = (Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),"0")))*(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_EXGRT"),"0")));
						L_dblGTVL1+=  Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_ITVAL"),"0"));
						if(!hstINDF1.containsKey((String)L_strINDNO))
							hstINDF1.put(L_strINDNO,"");
						if(!hstPORF1.containsKey((String)L_strPORNO))
							hstPORF1.put(L_strPORNO,"");
					}	
					if(M_fmtLCDAT.parse(L_strPORDT).compareTo(M_fmtLCDAT.parse("01/07/2005"))>=0 && M_fmtLCDAT.parse(L_strPORDT).compareTo(M_fmtLCDAT.parse("30/06/2006"))<=0 )
					{	
						L_intNOP2+=1;
	   					L_dblTOTQT2+= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0"));
	   					L_dblTOTVL2+= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_ITVAL"),"0"));
	   					L_dblAVGRT2 = L_dblTOTVL2/L_dblTOTQT2;
						L_dblLSTRT2 = (Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),"0")))*(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_EXGRT"),"0")));
						L_dblGTVL2+=  Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_ITVAL"),"0"));
						if(!hstINDF2.containsKey((String)L_strINDNO))
							hstINDF2.put(L_strINDNO,"");
						if(!hstPORF2.containsKey((String)L_strPORNO))
							hstPORF2.put(L_strPORNO,"");
					}	
					if(M_fmtLCDAT.parse(L_strPORDT).compareTo(M_fmtLCDAT.parse("01/07/2006"))>=0 && M_fmtLCDAT.parse(L_strPORDT).compareTo(M_fmtLCDAT.parse("30/06/2007"))<=0 )
					{	
						L_intNOP3+=1;
	   					L_dblTOTQT3+= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORQT"),"0"));
	   					L_dblTOTVL3+= Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_ITVAL"),"0"));
	   					L_dblAVGRT3 = L_dblTOTVL3/L_dblTOTQT3;
						L_dblLSTRT3 = (Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_PORRT"),"0")))*(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_EXGRT"),"0")));
						L_dblGTVL3+=  Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("PO_ITVAL"),"0"));
						if(!hstINDF3.containsKey((String)L_strINDNO))
							hstINDF3.put(L_strINDNO,"");
						if(!hstPORF3.containsKey((String)L_strPORNO))
							hstPORF3.put(L_strPORNO,"");
					}	

					if(!M_rstRSSET.next())
					{
						L_flgEOF = true;
						break;
					}
					L_strMATCD = nvlSTRVL(M_rstRSSET.getString("PO_MATCD"),"");
					L_strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
					L_strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"");
					L_strPORDT = M_fmtLCDAT.format(M_rstRSSET.getDate("PO_PORDT"));
					L_strPORNO = nvlSTRVL(M_rstRSSET.getString("PO_PORNO"),"");
					L_strINDNO = nvlSTRVL(M_rstRSSET.getString("PO_INDNO"),"");
				}//	while((L_strMATCD).equals(L_strPMATCD) && !L_EOF)

				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intNOP3,0),3));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT3,3),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTVL3,2),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblAVGRT3,2),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblLSTRT3,2),12));
				
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intNOP2,0),3));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT2,3),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTVL2,2),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblAVGRT2,2),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblLSTRT2,2),12));

				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intNOP1,0),3));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTQT1,3),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTVL1,2),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblAVGRT1,2),12));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblLSTRT1,2),12));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst+= 1;
				L_intNOP1=0;L_intNOP2=0;L_intNOP3=0;
				L_dblTOTQT1=0;L_dblTOTQT2=0;L_dblTOTQT3=0;
				L_dblTOTVL1=0;L_dblTOTVL2=0;L_dblTOTVL3=0;
				L_dblAVGRT1=0;L_dblAVGRT2=0;L_dblAVGRT3=0;
				L_dblLSTRT1=0;L_dblLSTRT2=0;L_dblLSTRT3=0;
			}//while(eof())
				L_intINDF1 = hstINDF1.size();
				L_intINDF2 = hstINDF2.size();
				L_intINDF3 = hstINDF3.size();

				L_intPORF1 = hstPORF1.size();
				L_intPORF2 = hstPORF2.size();
				L_intPORF3 = hstPORF3.size();

				dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst) && (M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");
				dosREPORT.writeBytes(padSTRING('R',"Grand Total ",79));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTVL3,2),12));
				dosREPORT.writeBytes(padSTRING('L',"",39));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTVL2,2),12));
				dosREPORT.writeBytes(padSTRING('L',"",39));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTVL1,2),12)+"\n");
				dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
				dosREPORT.writeBytes(padSTRING('R',"Total No. of Indents :",79));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intINDF3,0),12));
				dosREPORT.writeBytes(padSTRING('L',"",39));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intINDF2,0),12));
				dosREPORT.writeBytes(padSTRING('L',"",39));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intINDF1,0),12));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('R',"Total No. of POs :",79));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intPORF3,0),12));
				dosREPORT.writeBytes(padSTRING('L',"",39));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intPORF2,0),12));
				dosREPORT.writeBytes(padSTRING('L',"",39));
				dosREPORT.writeBytes(padSTRING('L',setNumberFormat(L_intPORF1,0),12));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
				cl_dat.M_intLINNO_pbst+= 6;
				L_dblGTVL1=0;L_dblGTVL2=0;L_dblGTVL3=0;
				L_intPORF1=0;L_intPORF2=0;L_intPORF3=0;
		}		prnFOOTR();
									
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
	
	/**
	Method to Generate the Page Header of the Report.
	*/	
	private void prnHEADER()
	{  
	    try
	    {
			strRPHDR = "Material Groupwise Purchase Analysis Report on Financial year basis for the period from "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim()+" for ";
			cl_dat.M_PAGENO +=1;
			cl_dat.M_intLINNO_pbst=0;
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",204));
			dosREPORT.writeBytes("Report Date :" + cl_dat.M_strLOGDT_pbst + "\n");			
			dosREPORT.writeBytes(padSTRING('R',strRPHDR,204));			
			dosREPORT.writeBytes("Page No.    :" + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("Group : " +"("+txtMATMG.getText().toString().trim()+") "+txtMGRDS.getText().trim());
			if(txtMATSG.getText().toString().trim().length() > 0 )
				dosREPORT.writeBytes(" And Subgroup : "+"("+txtMATSG.getText().toString().trim()+") "+txtSGRDS.getText().trim());
			dosREPORT.writeBytes(" And for "+strPURDS+" Purchases "+"\n");
			dosREPORT.writeBytes("Months : July to June"+"\n");
			dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			dosREPORT.writeBytes("                                                                                    2006-2007                                            2005-2006                                            2004-2005                           \n");
			dosREPORT.writeBytes(" Mat. Code   Material Description                           UOM  NOP     Total       Total       Average      Last   NOP      Total       Total       Average     Last    NOP  Total       Total       Average     Last          \n");
			dosREPORT.writeBytes("                                                                         Qty.        Value       Rate         Rate            Qty.        Value       Rate        Rate         Qty.        Value       Rate        Rate          \n");
			dosREPORT.writeBytes("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");			
			cl_dat.M_intLINNO_pbst += 9;
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

class INPVF extends InputVerifier
{
String L_strMATMG="",L_strMATSG="";

	public boolean verify(JComponent Jcomp)
	{
		if(((JTextField)Jcomp).getText().length() == 0)
			return true;
		if(Jcomp == txtMATMG)
		{
			L_strMATMG = txtMATMG.getText().toString().trim();
			M_strSQLQRY = " SELECT CTT_GRPCD,CTT_MATDS FROM CO_CTTRN WHERE CTT_CODTP='MG' AND CTT_GRPCD='"+L_strMATMG+"' AND CTT_STSFL != 'X' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				try
				{
					if(!M_rstRSSET.next())
					{
						setMSG("Invalid Main Group....Press F1 for Help..",'E');
						return false;
					}	
					else
					{
						txtMGRDS.setText(nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),""));
						M_rstRSSET.close();
						return true;	
					}	
				}	
				catch(Exception L_EX)
				{
					setMSG(L_EX," Error Validating Main Group");
				}
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}	
		}	
		if(Jcomp == txtMATSG)
		{
			L_strMATSG = txtMATSG.getText().toString().trim();
			M_strSQLQRY = " SELECT CTT_GRPCD,CTT_MATDS FROM CO_CTTRN WHERE CTT_CODTP='SG' AND ";
			M_strSQLQRY+= " CTT_GRPCD='"+txtMATMG.getText().toString().trim()+"' AND CTT_STSFL != 'X' AND SUBSTRING(CTT_MATCD,3,2)='"+L_strMATSG+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				try
				{
					if(!M_rstRSSET.next())
					{
						setMSG("Invalid SubGroup....Press F1 for Help..",'E');
						return false;
					}	
					else
					{
						txtSGRDS.setText(nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),""));
						M_rstRSSET.close();
						return true;	
					}	
				}	
				catch(Exception L_EX)
				{
					setMSG(L_EX," Error Validating SubGroup");
				}
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}	
		}
		if(Jcomp == txtFMDAT)
		{
			try
			{
				if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("From Date Can not be greater than Login Date .. ",'E');				
					return false;
				}
				else
				return true;	
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX+" From Date Input Verifier ",'E');
			}				
		}	

		if(Jcomp == txtTODAT)
		{
			try
			{
				if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)
				{
					setMSG("To-Date Can not be greater than Login Date .. ",'E');				
					return false;
				}
				else if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
				{
					setMSG("Please Note that From-Date must be Greater than To-Date .. ",'E');								
					return false;
				}
				else
				return true;	
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX+" To Date Input Verifier ",'E');
			}				
		}	
		return false;
	}
}	
}
