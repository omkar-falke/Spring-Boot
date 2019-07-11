/*
System Name   : Laboratory Information Management System.
Program Name  : Monthly Water Test Report.
Program Desc. : Report to view monthly water Analysis details.
Author        : Mr. S.R.Mehesare
Date          : 15/06/2005
Version       : LIMS v2.0.0

Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/

import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.MouseEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
import javax.swing.border.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.DateFormat;
import java.util.Locale;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Monthly Water Test Report.	

Purpose : This Report gives the average of test parameter values taken between the given 
          Range.	

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
QC_WTTRN       WTT_QCATP,WTT_TSTTP,WTT_TSTNO,WTT_TSTDT,
               WTT_WTRTP                                               #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT     PS_TSTDT       QC_PSMST      DATE          Test Date 
txtTODAT     PS_TSTDT       QC_PSMST      DATE          Test Date 
--------------------------------------------------------------------------------------
<B>
Logic</B>
Each water test has some quality parameters. & these parameters change as per 
new standards adopted. 
System manages these parameters of each test & makes Latest parameters available from 
Codes transaction table (CO_CDTRN).
Hence to generate report every time list of parameters are generated dynamically as :-
   1) Latest parameters are fetched from the database associated with perticular water Test.
   2) These parameters are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    "WTT_"+ Vector.elementAt(i)+"VL"  
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	


<b>Test Parameter Details are taken from CO_CDTRN for conditions</b>
   1) CMT_CGMTP = 'RPT' 
   2) AND CMT_CGSTP ='QCXXEFA'

Test Details such as Numbers,QCA Type,Test Type,Test Report Type are taken from QC_WTTRN 
     1) WTT_TSTDT Between given Date range.
     2) AND WTT_TSTTP = '0401'

<I><B>Conditions Give in Query:</b>
<B>For Effluent Test </B>  
  Test Details are taken from QC_WTTRN for conditions
     1) WTT_QCATP = the QCA Type get from the above Query.
     2) And WTT_TSTTP = Test Type get from the above Query.
     3) And WTT_TSTNO = Test Number get from the above Query. 
     4) And WTT_WTRTP = Water Test Report Types get from the hashtable for loop counter.
  Water Test Report Types is fetched from CO_CDTRN as 
     1) CMT_CGMTP='SYS'";
     2) AND CMT_CGSTP = 'QCXXWTP'
     3) AND CMT_MODLS like '%0401%'			
     4) AND CMT_CODCD <> '11' 

<B>For Monthly Test </B>   
  Test Details are taken from QC_WTTRN for conditions
     1) WTT_QCATP ='01' 
     2) AND WTT_TSTTP ='0401'
	 3) AND date(WTT_TSTDT) between given Date Range.			

<B>Validations :</B>
    - Dates must be valid. i.e.smaller than current date.
</I> */

public class qc_qrmwt extends cl_rbase
{									/** JTextField to display & enter From Date to specify Date range.*/
	private JTextField txtFMDAT;	/** JTextField to display & enter To Date to specify Date range.*/
	private JTextField txtTODAT;	/** JCheckBox to specify Joint Sample Report.*/
	private JCheckBox chkJNTSM;		/** JComboBox to display & to specify report Type.*/
	private JComboBox cmbPRNOP;		/** JTabbedPane to display the details of Effluent Test & Monthly details on Seprate Tab.*/
	private JTabbedPane jtpMANTB;    
									/** JPanel to display Month details.*/
	private JPanel pnlMONTH;		/** JPanel to display Effluent water Test details.*/
	private JPanel pnlEFFWT;		/** JTable to display Month details.*/
	private JTable tblEFWDTL;		/** JTable to display Month details.*/
	private JTable tblMONAVG;		
									/** String variale for ISODOC details.*/
	private String strISODOC1;      /** String variale for ISODOC details.*/
	private String strISODOC2;		/** String variale for ISODOC details.*/
	private String strISODOC3;
								/** Vector for temporary Storage of Quality parameter List.*/
	private Vector<String> vtrQPRLS;	/** Vector for temporary Storage of Quality parameter Description.*/
	private Vector<String> vtrQPRDS;	/** Vector for temporary Storage of Quality parameter's Unit of measurement.*/
	private Vector<String> vtrQPUOM;	/** Vector for temporary Storage of Quality parameter's MPCB Limits.*/
	private Vector<String> vtrQPFRQ;	/** Vector for temporary Storage of Water Analysis Code.*/
	private Vector<String> vtrWACOD;	/** Vector for temporary Storage of Water Analysis Description.*/
	private Vector<String> vtrWADES;
								/** String variable for To date.*/
	private String strTODT;		/** String variable for From date.*/
	private String strFMDT;
										/**	Integer variable to repersent Quality parameter Description Column.*/
	private final int TB1_QPRDS =0;		/**	Integer variable to repersent Unit of Measurement Column.*/
	private final int TB1_UOMDS =1;		/**	Integer variable to repersent Non Organic Pond value Column.*/
	private final int TB1_NOPVL =2;		/**	Integer variable to repersent Guard Pond value Column.*/
	private final int TB1_GOPVL =3;		/**	Integer variable to repersent Quality parameter Range Column.*/
	private final int TB1_QPRNG =4;
										/**	Integer variable to repersent Quality parameter Description Column.*/
	private final int TB2_QPRDS =0;		/**	Integer variable to repersent MPCB Limits Column.*/
	private final int TB2_FRQDS =1;		/**	Integer variable to repersent Unit of Measurement Column.*/
	private final int TB2_UOMDS =2;		/**	Integer variable to repersent Infuent value Column.*/
	private final int TB2_EQVL =3;		/**	Integer variable to repersent Guard pond value Column.*/
	private final int TB2_NOPVL =4;		/**	Integer variable to repersent Non Organic pond value Column.*/
	private final int TB2_GOPVL =5;		/**	Integer variable to repersent Range value Column.*/
	private final int TB2_QPRNG =6;
	private String strEQVAL ="11";		/** String variable for Parameter Values fetched from the database.*/
	private String strUMVAL ="";
										/** Hashtable to store quality parameter range value.*/
	private Hashtable<String,String> hstQPRNG = new Hashtable<String,String>();	/** String variable for Guard pond value.*/
	private String strGPVAL ="12";					/** String variable for Non Organic pond value.*/
	private String strNPVAL ="13";
										/**String array to store the values of parameters fetched from data base.*/
	private String arrUMVAL [][] = new String[30][30];	
	private boolean flgPRINT = true;
	private boolean flgPRNOK = true;
										/** String Variable for generated Report File Name.*/
	private String strFILNM;			/** FileOutPutStream for genetated Report File Name. */
	private FileOutputStream fosREPORT;	/** DataOutPutStream to hold the Report data in ByteStream to generate the Report File.*/
	private DataOutputStream dosREPORT;	
	private DefaultTableCellRenderer LM_CENALG = new DefaultTableCellRenderer();
	private DefaultTableCellRenderer LM_RGTALG = new DefaultTableCellRenderer();
	qc_qrmwt()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			this.setCursor(cl_dat.M_curWTSTS_pbst);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("From Date"),3,3,1,.8,this,'R');
			add(txtFMDAT = new TxtDate(),3,4,1,1.3,this,'L');
			
			add(new JLabel("To Date"),3,5,1,.5,this,'R');
			add(txtTODAT = new TxtDate(),3,6,1,1.3,this,'L');
			
			add(new JLabel("Print Option"),4,3,1,.8,this,'R');
			add(cmbPRNOP = new JComboBox(),4,4,1,1.3,this,'L');
			
		
			add(chkJNTSM = new JCheckBox("Joint Sample"),4,6,1,1.3,this,'L');			
			cmbPRNOP.addItem("NOP");
			cmbPRNOP.addItem("GP");
			
			pnlEFFWT = new JPanel(null);
			pnlMONTH = new JPanel(null);			
			jtpMANTB=new JTabbedPane();						
			add(jtpMANTB,6,1,10,8,this,'L');
		//	add(new JLabel("Click on respective tab to view details in table.."),19,1,1,7,this,'R');
			jtpMANTB.add(pnlEFFWT,"Effluent Water Analysis");
			jtpMANTB.add(pnlMONTH,"Mothly Analysis");	
			jtpMANTB.addMouseListener(this);
			M_pnlRPFMT.setVisible(true);
									
			strISODOC1 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXEFA","DOC1");
			strISODOC2 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXEFA","DOC2");
			strISODOC3 = cl_dat.getPRMCOD("CMT_CODDS","ISO","QCXXEFA","DOC3");
			
			vtrQPRDS = new  Vector<String>();
			vtrQPRLS = new Vector<String>();
			vtrQPUOM = new Vector<String>();
			vtrQPFRQ = new Vector<String>();
			vtrWACOD = new  Vector<String>();
			vtrWADES = new Vector<String>();
			
			getTSTDET();
			
			getQPRRNG();
						
																					
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)								 
		{
			setMSG(L_EX,"qc_qrmwt");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter the 01/mon/year to generate the report..",'N');				
				setENBL(true);				
				int L_intDAYCT = Integer.valueOf(cl_dat.M_strLOGDT_pbst.substring(0,2)).intValue();					
				txtFMDAT.setText(calDATE(cl_dat.M_strLOGDT_pbst,L_intDAYCT));
				txtTODAT.setText(cl_dat.M_strLOGDT_pbst);				
				txtFMDAT.requestFocus();
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{						
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;						
			flgPRINT = true;
		}		
		else if(M_objSOURC == txtFMDAT)
		{
			txtTODAT.requestFocus();
			setMSG("",'N');
		}
		else if(M_objSOURC == txtTODAT)		
			cmbPRNOP.requestFocus();
		else if(M_objSOURC == cmbPRNOP)		
			cl_dat.M_btnSAVE_pbst.requestFocus();
	}
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			super.mouseClicked(L_ME);			
			if(M_objSOURC == jtpMANTB)
			{					
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
				{
					setMSG("Select an Option and Enter Date ..",'N');
					return;
				}
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				if(jtpMANTB.getSelectedIndex() == 0)
				{		
					flgPRINT = false;
					strTODT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim() +" "+"23:59"));
					strFMDT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim() +" "+"00:01"));
					pnlEFFWT.removeAll();
					pnlEFFWT.updateUI();
					getWATCOD("0401",strEQVAL);					
					getEFWANY();
					pnlEFFWT.updateUI();					
				}
				else if(jtpMANTB.getSelectedIndex() == 1)
				{								
					pnlMONTH.removeAll();
					pnlMONTH.updateUI();
					getWATCOD("0401"," ");								
					getMONAVG();
					pnlMONTH.updateUI();					
				}				
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseClicked");
		}
	}
		
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{		
		if(!vldDATA())
			return;					
		try
		{
			flgPRINT = true;
			if(M_rdbHTML.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst +"qc_qrmwt.html";
			else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_qrmwt.doc";										
			
			getDATA();					
			
			if(flgPRNOK == false)
			{
				setMSG("No Data Found..",'E');
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"MONTHLY WET ANALYSIS REPORT"," ");
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
    *Method to fetch data & club it with Header & footer in DataOutputStream.
	*/
	private void getDATA()
	{ 				
		String L_strTMPDT = "";										
		java.sql.Timestamp L_tmsTSTDT;		
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
		{	        						
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}						
			if(M_rdbHTML.isSelected())
			{				
			    dosREPORT.writeBytes("<HTML><HEAD><Title>WET ANALTSIS REPORT </title> </HEAD> <BODY><P><PRE style =\" font-size : 11 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			prnHEADER();
			
			getWATCOD("0401",strEQVAL);
			
			strTODT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim() +" "+"23:59"));
			strFMDT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtFMDAT.getText().trim() +" "+"00:01"));
			
			getEFWANY();
			
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
				setMSG("For HTML Report Printing, Please insert 120 column Page..",'N');
			}			
			dosREPORT.close();
			fosREPORT.close();	
		}
		catch(Exception L_EX)
		{			
			flgPRNOK = false;
			setMSG(L_EX,"getDATA");
		}
	}			
	/**
	Method to check the validation of input data before execution of query.
	*/
	boolean vldDATA()
	{
		try
		{ 
			if(txtFMDAT.getText().length()==0)			
			{
				setMSG("Please Enter Date to generate the Report.. ",'E');
				txtFMDAT.requestFocus();			
				return false;
			}
			if (M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("From Date must be smaller than current Date.. ",'E');								
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length()==0)
			{
				setMSG("Please Enter To Date to generate the Report.. ",'E');
				txtTODAT.requestFocus();			
				return false;
			}
			if (M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("To Date must be smaller than current Date.. ",'E');								
				txtTODAT.requestFocus();
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
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	/**
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{		
		String L_strMONTH = "";		
		try
		{	
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);			
			String myDate = df.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));			
			StringTokenizer st = new StringTokenizer(myDate);
			if(st.hasMoreTokens())
				L_strMONTH =st.nextToken().toUpperCase();
			
			cl_dat.M_intLINNO_pbst = 13;
			cl_dat.M_PAGENO += 1;	
			int L_intROWLN =0;
			if(chkJNTSM.isSelected())
				L_intROWLN = 95;
			else
				L_intROWLN=95;
			L_strMONTH = L_strMONTH+" "+txtFMDAT.getText().trim().substring(6);
			
			dosREPORT.writeBytes(padSTRING('L',"-----------------------------------",L_intROWLN)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISODOC1,L_intROWLN) + "\n");			
			dosREPORT.writeBytes(padSTRING('L',strISODOC2,L_intROWLN) + "\n");			
			dosREPORT.writeBytes(padSTRING('L',strISODOC3,L_intROWLN) + "\n");			
			dosREPORT.writeBytes(padSTRING('L',"-----------------------------------",L_intROWLN)+"\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,L_intROWLN -24));			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			if(chkJNTSM.isSelected())
				dosREPORT.writeBytes(padSTRING('R',"JOINT VIGILANCE SAMLE ANALYSIS REPORT",L_intROWLN -24));
			else
				dosREPORT.writeBytes(padSTRING('R',"MONTHLY AVERAGE ANALYSIS REPORT FOR "+L_strMONTH +" ("+cmbPRNOP.getSelectedItem()+")",L_intROWLN -24));
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");								
			
			if(chkJNTSM.isSelected())
			{
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<B>");							
				dosREPORT.writeBytes("Date of Sample Collection : "+txtFMDAT.getText().trim()+"\n");
				dosREPORT.writeBytes("Sample Collected          : Jointly by MPCB & SPL Personnel"+"\n");
				dosREPORT.writeBytes("Source                    : Treated Effulent from Non Organic Pond"+"\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("</B>");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes("Parameters                    Units               Treated       MPCB Limits\n");
				dosREPORT.writeBytes("                                                 Effluent\n");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------\n");
			}
			else
			{
				dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------\n");
				dosREPORT.writeBytes("Parameters               Frequency    Units       Influent       Treated     MPCB Limits\n");
				dosREPORT.writeBytes("                         of Analysis                            Effluent  \n");
				dosREPORT.writeBytes("                         in a Month\n");
				dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------\n\n");
			}
		}
		catch(Exception L_EX)
		{
			flgPRNOK = false;
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	 * Method to get the Quality parameter Details for given Test.
	 */
	private void getTSTDET()
	{
		String L_strQPRCD ="",L_strQPRDS ="",L_strUOMDS ="",L_strFRQDS="";
		if(vtrQPRLS !=null)
			vtrQPRLS.removeAllElements();
		if(vtrQPRDS !=null)
			vtrQPRDS.removeAllElements();
		if(vtrQPUOM !=null)
			vtrQPUOM.removeAllElements();
		if(vtrQPFRQ !=null)
			vtrQPFRQ.removeAllElements();
		M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_CHP02,CMT_NMP01 from CO_CDTRN"
			+" where CMT_CGMTP = 'RPT' and CMT_CGSTP ='QCXXEFA' order by CMT_NMP01";
		try
		{
			ResultSet M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while (M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strQPRDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					L_strUOMDS = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					L_strFRQDS = nvlSTRVL(M_rstRSSET.getString("CMT_CHP02"),"");
					vtrQPRLS.addElement(L_strQPRCD);
					vtrQPRDS.addElement(L_strQPRDS);
					vtrQPUOM.addElement(L_strUOMDS);
					vtrQPFRQ.addElement(L_strFRQDS);										
				}				
				M_rstRSSET.close();
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getTSTDET");
		}
	}
	/**
	* Method to get the data of Effluent Water Analysis.
	 */
	private void getEFWANY()
	{
		try
		{	
			int L_intROWNO=0;
			int L_intNOCHR = 0;
			StringBuffer L_stbDATA= new StringBuffer();
			ResultSet L_rstRSSET ;
			String L_strSQLQRY ="";			
			vtrQPRLS.trimToSize();
			vtrQPRDS.trimToSize();
			//String arrUMVAL [][] = new String[vtrQPRLS.size()][vtrWACOD.size()];						
		    M_strSQLQRY = "Select distinct WTT_QCATP,WTT_TSTTP,WTT_TSTNO From QC_WTTRN Where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_TSTDT ";
            M_strSQLQRY += " Between '" + strFMDT + "' And '"+ strTODT + "' AND WTT_TSTTP = '0401'";
            M_strSQLQRY += " AND WTT_STSFL <> 'X'";						
         	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);						
			if(M_rstRSSET !=null)
			{							
				if(M_rstRSSET.next())
				{										
					String L_TSTDTE = " ";
					for(int i=0;i<vtrWACOD.size();i++)
					{						
						L_strSQLQRY = "Select * FROM QC_WTTRN where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_QCATP = '" ;
						L_strSQLQRY +=  nvlSTRVL(M_rstRSSET.getString("WTT_QCATP"),"").trim() 
							+ "'And WTT_TSTTP ='" + nvlSTRVL(M_rstRSSET.getString("WTT_TSTTP"),"").trim() 
							+ "'And WTT_TSTNO ='" + nvlSTRVL(M_rstRSSET.getString("WTT_TSTNO"),"").trim() 
							+ "'And WTT_WTRTP ='" + ((String)vtrWACOD.elementAt(i)).trim()+ "'" ;
						L_strSQLQRY +=" AND WTT_STSFL <> 'X'";
												
						L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);						
						if(L_rstRSSET !=null)
						{							
							while(L_rstRSSET.next())
							{													
								L_TSTDTE = L_rstRSSET.getString("WTT_TSTDT");
								for(int j=0;j<vtrQPRLS.size();j++)
								{														
									String L_strQPRCD = "WTT_" + ((String)(vtrQPRLS.elementAt(j))).toString().trim()+ "VL";
									strUMVAL = L_rstRSSET.getString(L_strQPRCD);
									if(strUMVAL !=null)	
										arrUMVAL [j][i] =  strUMVAL.trim();
								}
							}						
							L_rstRSSET.close();	
						}
					}					
					M_rstRSSET.close();
				}
			}
			String[] L_TBLHD= {"Parameter","Unit","Treated Effulent (NOP)","Treated Effulent (GP)","MPCB Limits"};			
			int[] L_COLSZ =  {160,100,160,150,160};
			JTextField txtDSBEDT = new JTextField();
			JTextField txtEDITR = new JTextField();
			txtDSBEDT.setEnabled(false);			
			
			tblEFWDTL = crtTBLPNL1(pnlEFFWT,L_TBLHD,vtrQPRLS.size(),1,1,10,7.8,L_COLSZ,new int[]{0});			
			tblEFWDTL.getColumn(tblEFWDTL.getColumnName(TB1_QPRDS)).setCellEditor(new DefaultCellEditor(txtDSBEDT));
			tblEFWDTL.getColumn(tblEFWDTL.getColumnName(TB1_UOMDS)).setCellEditor(new DefaultCellEditor(txtDSBEDT));
			tblEFWDTL.getColumn(tblEFWDTL.getColumnName(TB1_QPRNG)).setCellEditor(new DefaultCellEditor(txtDSBEDT));
			LM_RGTALG.setHorizontalAlignment(JLabel.RIGHT);
			LM_CENALG.setHorizontalAlignment(JLabel.CENTER);
			tblEFWDTL.getColumn(tblEFWDTL.getColumnName(TB1_NOPVL)).setCellRenderer(LM_RGTALG);
			tblEFWDTL.getColumn(tblEFWDTL.getColumnName(TB1_GOPVL)).setCellRenderer(LM_RGTALG);
			tblEFWDTL.getColumn(tblEFWDTL.getColumnName(TB1_QPRNG)).setCellRenderer(LM_CENALG);						
			if(flgPRINT == false)
			{								
				for(int i=0;i<vtrQPRLS.size();i++)
				{										
					tblEFWDTL.setValueAt(vtrQPRDS.elementAt(i).toString().trim(),i,0);					
					tblEFWDTL.setValueAt(vtrQPUOM.elementAt(i).toString().trim(),i,1);					
					tblEFWDTL.setValueAt((String)hstQPRNG.get(vtrQPRLS.elementAt(i).toString().trim()),i,4);
					for(int j=0;j<vtrWACOD.size();j++)
					{											
						if(arrUMVAL[i][j] !=null)
						{
						    if(arrUMVAL[i][j].trim().length()>0)
						    {												
						 		if(Double.valueOf(arrUMVAL [i][j].trim()).doubleValue()!=0.0)
						 			tblEFWDTL.setValueAt(arrUMVAL [i][j].trim(),i,j+2);
						 		else							
									tblEFWDTL.setValueAt("NIL",i,j+2);															
						 	}
							else
						 		tblEFWDTL.setValueAt("-",i,j+2);								
						}
						else 
						    tblEFWDTL.setValueAt("-",i,j+2);								
						// BAS 100 fixed
						if(i==vtrQPRLS.size()-1)
							tblEFWDTL.setValueAt("100",i,j+2);									
					 }												
				}					
			}
			else // to add data in the stream
			{											
				if(cmbPRNOP.getSelectedItem().equals("NOP"))
					L_intROWNO = 1;
				else
					L_intROWNO = 0;						
				for(int i=0;i<vtrQPRLS.size();i++)
				{															
					if(chkJNTSM.isSelected())
					{
						L_intNOCHR =19;
						L_stbDATA.append(padSTRING('R',vtrQPRDS.elementAt(i).toString().trim(),30));
						L_stbDATA.append(padSTRING('R',vtrQPUOM.elementAt(i).toString().trim(),8));	
					}					
					else
					{
						L_intNOCHR =13;
						L_stbDATA.append(padSTRING('R',vtrQPRDS.elementAt(i).toString().trim(),25));
						L_stbDATA.append(padSTRING('R',vtrQPFRQ.elementAt(i).toString().trim(),13));						
						L_stbDATA.append(padSTRING('R',vtrQPUOM.elementAt(i).toString().trim(),8));
					}					
					if(! chkJNTSM.isSelected())					
					{						
						if(cmbPRNOP.getSelectedItem().equals("NOP"))						
							L_stbDATA.append(padSTRING('L',"",L_intNOCHR));						
						else 						
							L_stbDATA.append(padSTRING('L',"",L_intNOCHR));						
					}										
					for(int j=L_intROWNO;j<L_intROWNO+1;j++)
					{
						if(arrUMVAL[i][j] !=null)
						{
						    if(arrUMVAL[i][j].trim().length()>0)
						    {
						 		if(Double.valueOf(arrUMVAL [i][j].trim()).doubleValue()!=0.0)
						 			L_stbDATA.append(padSTRING('L',arrUMVAL [i][j].trim(),L_intNOCHR));
						 		else							
									L_stbDATA.append(padSTRING('L',"NIL",L_intNOCHR));							
						 	}
							else
						 		L_stbDATA.append(padSTRING('L',"-",L_intNOCHR));	
						}
						else if (i!=vtrQPRLS.size()-1)
						    L_stbDATA.append(padSTRING('L',"-",L_intNOCHR));		
						// BAS 100 fixed
						if(i==vtrQPRLS.size()-1)
							L_stbDATA.append(padSTRING('L',"100",L_intNOCHR));									
					 }	
					if(chkJNTSM.isSelected())
						L_stbDATA.append("  ");
					L_stbDATA.append("     "+padSTRING('R',(String)hstQPRNG.get(vtrQPRLS.elementAt(i).toString().trim()),20)+"\n\n");					
				}								
				dosREPORT.writeBytes(L_stbDATA.toString());
				dosREPORT.writeBytes("-------------------------------------------------------------------------------------------------\n");				
			}												
		}
		catch(Exception L_EX)
		{
			flgPRNOK = false;
			setMSG(L_EX,"getEFWANY");
		}
	}
	/**
	 * Method to get the Quality parameter Range for selected Test.
	 */
	private void getQPRRNG()
	{			
		M_strSQLQRY = "Select QP_QPRCD,QP_NPFVL,QP_NPTVL from CO_QPMST "; 
    	M_strSQLQRY += " where QP_QCATP ='"+M_strSBSCD.substring(2,4)+"' AND QP_PRDCD = '6800000001'";
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);		
		try
		{
			if(L_rstRSSET !=null)
			{
				while(L_rstRSSET.next())
				{
					String L_strQPRCD = L_rstRSSET.getString("QP_QPRCD").trim();
					String L_NPFVL = L_rstRSSET.getString("QP_NPFVL").trim();
					String L_NPTVL = L_rstRSSET.getString("QP_NPTVL").trim();
					if(Double.valueOf(L_NPFVL).doubleValue()!=0.0)
					{
			            if(!L_strQPRCD.equals("DO_"))
			            	hstQPRNG.put(L_strQPRCD,Double.valueOf(L_NPFVL).toString()+ "-" +Double.valueOf(L_NPTVL).toString());
						 else
						   hstQPRNG.put(L_strQPRCD,"min"+ " " +Double.valueOf(L_NPFVL).toString());			             
					}
			        else
					{
			             if(L_strQPRCD.equals("BAS"))
			                 hstQPRNG.put(L_strQPRCD,"90% survival-96 hrs.");
						 else	
							hstQPRNG.put(L_strQPRCD,setNumberFormat(Double.valueOf(L_NPTVL).doubleValue() ,0));
					}
				}			
				L_rstRSSET.close();				
			}
		}
		catch (Exception L_EX)
		{
          setMSG(L_EX,"getQPRRNG");
	    }
	}
	/**
	 * Method to get Month average values for the selected Test Type.
	 */
	private boolean getMONAVG()
	{
		try
		{
			ResultSet L_rstRSSET ;
			String L_strSQLQRY ="";
			String L_strQPRCD ="";	
			String L_strRSLVL ="";
			String L_strWTRTP ="";
			String L_strFMDT,L_strTODT;
			double strUMVAL;			
			vtrQPRLS.trimToSize();
			vtrQPRDS.trimToSize();			
			
			if (txtTODAT.getText().trim().length()==0)
			{
				setMSG("To Date cannot be Blank, Please enter ToDate....",'E');
				return false;
			}
			L_strTODT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));																				       			
			L_strFMDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
																		
			String[] L_TBLHD= {"Parameter","Frequency","Unit","InFluent","Treated Effulent (NOP)","Treated Effulent (GP)","MPCB Limits"};
			int[] L_COLSZ =  {150,80,60,80,130,120,110};			
						
			JTextField txtDSBEDT = new JTextField();
			JTextField txtEDITR = new JTextField();
			txtDSBEDT.setEnabled(false);
			tblMONAVG = crtTBLPNL1(pnlMONTH,L_TBLHD,vtrQPRLS.size(),1,1,10,7.8,L_COLSZ,new int[]{0});
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_QPRDS)).setCellEditor(new DefaultCellEditor(txtDSBEDT));
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_FRQDS)).setCellEditor(new DefaultCellEditor(txtDSBEDT));
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_UOMDS)).setCellEditor(new DefaultCellEditor(txtDSBEDT));
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_QPRNG)).setCellEditor(new DefaultCellEditor(txtDSBEDT));
			LM_RGTALG.setHorizontalAlignment(JLabel.RIGHT);
			LM_CENALG.setHorizontalAlignment(JLabel.CENTER);
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_EQVL)).setCellRenderer(LM_RGTALG);
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_NOPVL)).setCellRenderer(LM_RGTALG);
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_GOPVL)).setCellRenderer(LM_RGTALG);
			tblMONAVG.getColumn(tblMONAVG.getColumnName(TB2_QPRNG)).setCellRenderer(LM_CENALG);
									
			for(int i=0;i<vtrQPRLS.size();i++)
			{
				tblMONAVG.setValueAt(vtrQPFRQ.elementAt(i).toString().trim(),i,TB2_FRQDS);
				tblMONAVG.setValueAt(vtrQPRDS.elementAt(i).toString().trim(),i,TB2_QPRDS);
				tblMONAVG.setValueAt(vtrQPUOM.elementAt(i).toString().trim(),i,TB2_UOMDS);
				tblMONAVG.setValueAt((String)hstQPRNG.get(vtrQPRLS.elementAt(i).toString().trim()),i,TB2_QPRNG);
			}
			M_strSQLQRY = " select wtt_wtrtp";
			for(int i=0;i<vtrQPRLS.size();i++)
			{
				L_strQPRCD = vtrQPRLS.elementAt(i).toString().trim();
				M_strSQLQRY += ",isnull(sum(isnull(WTT_"+L_strQPRCD+"VL,0))/NULLIF(count(WTT_"+L_strQPRCD+"VL),0),0) "+L_strQPRCD;
			}
			M_strSQLQRY += " from QC_WTTRN where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_QCATP ='"+M_strSBSCD.substring(2,4)+"' and WTT_TSTTP ='0401'";
			M_strSQLQRY += "  and CONVERT(varchar,WTT_TSTDT,101) between '"+L_strFMDT +"' AND '"+L_strTODT +"'";                          
			M_strSQLQRY += " group by WTT_WTRTP ";    
			System.out.println(M_strSQLQRY);                                      			
         	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strWTRTP = nvlSTRVL(M_rstRSSET.getString("WTT_WTRTP"),"");
					for(int i=0;i<vtrQPRLS.size();i++)
					{
						L_strRSLVL = nvlSTRVL(M_rstRSSET.getString(vtrQPRLS.elementAt(i).toString().trim()),"-");
						// BAS 100 fixed
						if(vtrQPRLS.elementAt(i).toString().trim().equals("BAS"))
						{
							tblMONAVG.setValueAt("100",i,TB2_GOPVL);
							tblMONAVG.setValueAt("100",i,TB2_NOPVL);
						}
						if(L_strRSLVL.trim().equals("0.0"))
							L_strRSLVL ="NIL";
						if(L_strWTRTP.equals(strEQVAL))
							tblMONAVG.setValueAt(L_strRSLVL,i,TB2_EQVL);
						else if(L_strWTRTP.equals(strGPVAL))
							tblMONAVG.setValueAt(L_strRSLVL,i,TB2_GOPVL);
						else if(L_strWTRTP.equals(strNPVAL))
							tblMONAVG.setValueAt(L_strRSLVL,i,TB2_NOPVL);
					}
				}
				M_rstRSSET.close();
			}		   
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMONAVG");
		}
		return true;
	}
	/**
	 * Method to get Water Analysis Test Code.
	 * @param P_strTSTTP String argument to pass Test Type.
	 * @param P_strEQTNK String Tank Code.
	 */
	private void getWATCOD(String P_strTSTTP,String P_strEQTNK)
	{
		ResultSet L_rstRSSET;
		try
		{
			vtrWACOD.removeAllElements();
			vtrWADES.removeAllElements();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'QCXXWTP'and CMT_MODLS like '%" + P_strTSTTP + "%'";
			if(P_strEQTNK.trim().length() >0)
				M_strSQLQRY += " and CMT_CODCD <> '"+P_strEQTNK+"'"; 					
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(L_rstRSSET !=null)
			{				
				while (L_rstRSSET.next())
				{					
					vtrWACOD.addElement(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),""));
					vtrWADES.addElement(nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
				}
				L_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getWATCOD");
		}
	}	
	/**
	*Method to Calculate From-Date, one day smaller than To-Date.
    *@param P_strTODT String argument to pass To_Date.
	*/
    private String calDATE(String P_strTODT,int P_intDATCT)
    {
        try
        {					
	        M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strTODT));
		    M_calLOCAL.add(java.util.Calendar.DATE,- (P_intDATCT -1));																
       		return (M_fmtLCDAT.format(M_calLOCAL.getTime()));				            
		}
		catch(Exception L_EX)
		{	       
			setMSG(L_EX, "calDATE");
			return (cl_dat.M_strLOGDT_pbst);
        }					
	}
}		
