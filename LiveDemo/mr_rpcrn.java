/*
System Name   : Marketing System
Program Name  : Credit Note Transaction Printing 
Modify Date   : 03-07-2006
Purpose : This program is used to generate & print Credit Note document.
List of Table used :
-----------------------------------------------------------------------------------------------------------
Table Name      Primary Key														Operation done
																				Insert  Upd  Query  Del
-----------------------------------------------------------------------------------------------------------
MR_IVTRN		IVT_MKTTP,IVT_INVNO,IVT_PRDCD,IVT_PKGTP							  -      -     #     -
MR_CNTRN		CN_MKTTP,CN_INVNO,CN_PRDCD,CN_PKGTP,CN_CRDTP,CN_PRTTP,CN_GRPCD    -      -     #     -    
CO_PTMST		PT_PRTTP,PT_PRTCD												  -      -     #     -
-----------------------------------------------------------------------------------------------------------
List of field on the Report :
Sr No		Detail					Column Name				Table			Remark
1			Customer Name			PT_PRTNM				CO_PTMST
2			Address					PT_ADR01				CO_PTMST
3			Address					PT_ADR02				CO_PTMST
4			Address					PT_ADR03				CO_PTMST
5			Customer code			PT_GRPCD				CO_PTMST
6			Credit Note Number		PT_DOCRF				MR_CNTRN
7			Credit Note Date		PT_DOCDT				MR_CNTRN
8			Acc.Ref.No.				PT_ACCRF				MR_CNTRN
9			Acc.Ref.Date			PT_ACCDT				MR_CNTRN
(Multiple entries)
10  		Indent No				IVT_INDNO				MR_IVTRN
11  		Inv No					CN_INVNO				MR_CNTRN
12  		Inv Date				IVT_INVDT				MR_IVTRN
13		    Grade					IVT_PRDDS				MR_IVTRN
14		    Rate					CN_CRDRT				MR_CNTRN
15	    	Inv Qty					CN_INVQT				MR_CNTRN
16  		Amount					CN_CRDVL				MR_CNTRN
17			Service Tax				CN_SRTVL				MR_CNTRN
18			Edu cess				CN_ACSVL				MR_CNTRN
19			TDS						CN_TDSVL				MR_CNTRN
20			Surcharge on TDS		CN_SCHVL				MR_CNTRN
21			Edu cess(TDS)			CN_LCSVL				MR_CNTRN
22			Total after Tax Addn	CN_CRDVL+CN_SRTVL+CN_ACSVL --"--
23			Total Tax Deduction		CN_TDSVL+CN+SCHVL+CN_LCSVL --"--
24			Net Amount Figure		CN_NETVL				MR_CNTRN
25			Net Amt in words		amt_words(CN_NETVL)
<I>
<B>Query : </B>
   For Generating the report:
	M_strSQLQRY = "select CN_DOCRF,CN_DOCDT,CN_ACCRF,CN_INVNO,CN_CRDRT,CN_INVQT,CN_CRDVL,CN_SRTVL,CN_ACSVL,";
	           +=" CN_TDSVL,CN_SCHVL,CN_LCSVL,CN_CRDVL+CN_SRTVL+CN_ACSVL,CN_TDSVL+CN_SCHVL+CN_LCSVL,CN_NETVL,";
	           +=" IVT_INDNO,IVT_INVDT,IVT_PRDDS,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_GRPCD";
	           +=" from MR_CNTRN,MR_IVTRN,CO_PTMST where";
	1) CN_MKTTP = IVT_MKTTP and CN_INVNO = IVT_INVNO
	2) and CN_PRDCD = IVT_PRDCD and CN_PKGTP = IVT_PKGTP
	3) and CN_PRTTP = PT_PRTTP and CN_GRPCD = PT_PRTCD
	4) and CN_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'
	5) and CN_STSFL <> 'X' and CN_CRDTP ='"+strCRDTP+"'
	6) order by CN_CRDTP,CN_DOCRF,IVT_INDNO,CN_INVNO"

<B>Validations & Other Information:</B>    
Date 28/11/2006
	replace strDOCRF by strPDCRF at three Place .
    1)IN Tax Calculation Function
    2)Tax Prienting
    3)Amount Matching
</I>
*/
import java.sql.Date;import java.sql.ResultSet;import java.awt.event.*;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import javax.swing.JTextField;import javax.swing.JLabel;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import javax.swing.JComboBox;import java.util.Hashtable;import java.util.Vector;
import java.util.*;import java.math.*;
class mr_rpcrn extends cl_rbase
{
	private ButtonGroup btgRPTTP;    
	private JRadioButton rdbDATWS;	 
	private JRadioButton rdbDOCWS;
	private JComboBox cmbRPTOP;	   /** JComboBox to display & select Report.*/
	private JLabel lblFMDAT;       /** JLabel to display message on the Screen.*/
	private JLabel lblTODAT;       /**JLabel to display message on the Screen.*/
	private JLabel lblPRTCD;       /** JLabel for Party Code message.*/
	private JLabel lblPRTNM;       /** JLabel for displaying Party Name.*/
	private JTextField txtFMDAT;   /** JtextField to display & enter Date to generate the Report.*/
	private JTextField txtTODAT;   /** String Variable for date.*/
	private JTextField txtPRTCD;   /** JtextField to specify Party Code*/
	private JTextField txtFMDOC;   /** JtextField to  form Doc No.*/
	private JTextField txtTODOC;   /** JtextField to  To Doc No.*/
	private JLabel lblFMDOC;       /** JLabel to display message on the Screen.*/
	private JLabel lblTODOC;       /** JLabel to display message on the Screen.*/
	private JTextField txtOTHCT;
	private String strFMDAT;		/** String Variable for date.*/ 
	private String strTODAT;		/** String Variable for date.*/
		
	private String strFILNM;	    /** String Variable for generated Report file Name.*/ 
	private FileOutputStream fosREPORT ; /** FileOutputStream Object to generate the Report File from Stream of data.*/   
    private DataOutputStream dosREPORT ; /** FileOutputStream Object to hold Stream of Report Data to generate the Report File.*/
		
	private String strDOCRF;        /**String variable for credit note No  */
	private String strDOCDT;	    /**String variable for credit note date */
	//private String strACCDT;        /**                                */
	private String strACCRF;        /**String variable for Acc Ref No */
	private String strACCDT;        /**String variable for Acc Ref Date */
	private String strINDNO;		/**String variable for Indent No */
	private String strINVNO;        /**String variable for Invoice No */
	private String strINVDT;		/**String variable for Inv date */
	private String strPRDDS;		/**String variable for grade */
	private String strCRDRT;		/**String variable for rate */
	private String strINVQT;        /**String variable for Invoice Quentity */
	private String strCRDVL;		/**String variable for Amount */
	private String strSRTVL;		/**String variable for service tax */
	private String strACSVL;		/**String variable for Edu cess */
	private String strTDSVL;		/**String variable for TDS */
	private String strSCHVL;		/**String variable for surcharge on TDS */
	private String strLCSVL;		/**String variable for Edu cess(TDS) */
	private String strNETVL;		/**String variable for Net Amount fig */	
	private String strPRTTP;
	private String strPRTNM;		/**String variable for party name */
	private String strADR01;		/**String variable for add */
	private String strADR02;		/**String variable for add */
	private String strADR03;		/**String variable for add */
	private String strADR04;		/**String variable for add */
	private String strGRPCD;		/**String variable for customer code */
	private String strDSRCD;		
	private String strDSRNM;
	private String strPDCRF ="";	/**String reference variable for credit note No  */
	private String strPIVNO ="";	/**String ref variable for Invoice No */
	private String strPCDVL="0";		/**String ref variable for amount */
	private String strPIVQT="0";
	private String strATXVL;
	private String strLTXVL;
	private String strPRDCT ="";
	private int intRECCT;		    /** Integer Variable to count records fetched, to block report generation if data not found.*/
	Date L_datTEMP,L_datTEMP1;		/**variable for date */
	double dblAMTOT =0;
	double dblQTTOT =0;
	private boolean flgCHKLN;
	private int intCOUNT =0;
	double dblATXVL=0;
	double dblLTXVL=0;
	int i=0;int k=0;
	String M_strSQLQRY1;
	
	
	private Vector<String> vtrTXADD=new Vector<String>();
	private Vector<String> vtrTAXCD=new Vector<String>();
	private Vector<String> vtrTXDED=new Vector<String>();
	
	private Hashtable<String,String> hstDSRNM = new Hashtable<String,String>();  //Hash Table for Party code And party Name
	private Hashtable<String,String> hstTAXCD = new Hashtable<String,String>();
	private Hashtable<String,String> hstTAXSQ = new Hashtable<String,String>();
	private String strDOTLN = "----------------------------------------------------------------------------------------------------";
	private Hashtable<String,Double> hstTAXVL = new Hashtable<String,Double>();
	private Hashtable<String,String> hstPRDDS = new Hashtable<String,String>();
	mr_rpcrn()
	{
		super(2);
		cmbRPTOP = new JComboBox();
		try
		{
			String L_strCODCD="";
			String L_strCODDS="";
			ResultSet L_rstRSSET;
			try
			{
				btgRPTTP=new ButtonGroup();
					
				add(rdbDATWS=new JRadioButton("Date Wise",true),2,2,1,2,this,'L');
				add(rdbDOCWS=new JRadioButton("Doc No Wise"),2,4,1,2,this,'L');
				btgRPTTP.add(rdbDATWS);
				btgRPTTP.add(rdbDOCWS);
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXPTT' AND SUBSTRING(CMT_CODCD,1,1) in ('0','3')";
				L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				System.out.println("M_strSQLQRY 1>>"+M_strSQLQRY);
				if(L_rstRSSET!=null)
				{
					while(L_rstRSSET.next())
					{
						L_strCODCD = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");
						L_strCODDS = nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"");
						cmbRPTOP.addItem(L_strCODCD+" "+L_strCODDS);
					}
				}
			 	if(L_rstRSSET != null)
				L_rstRSSET.close();
			}
			catch(Exception e)
			{
				setMSG(e,"const");
			}
			
			M_strSQLQRY="Select PT_PRTTP,PT_PRTCD,PT_PRTNM from CO_PTMST Where PT_PRTTP='D' And isnull(PT_STSFL,'')<> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			System.out.println("M_strSQLQRY 2>>"+M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				String L_strPRTNM="";
				while(M_rstRSSET.next())
				{
					hstDSRNM.put(nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"  "),nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"  "));
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			
			//M_strSQLQRY= "Select  CMT_CODCD,CMT_CODDS  from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'COXXTAX' AND CMT_CHP01='01'";
			M_strSQLQRY= "Select  SUBSTRING(CMT_CODCD,1,3) L_CODCD,CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_CCSVL from CO_CDTRN where CMT_CGMTP='TCL' and CMT_CGSTP = 'MRXXDCM' ORDER BY CMT_NMP01";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			System.out.println("M_strSQLQRY 3>>"+M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{
				    if(M_rstRSSET.getString("CMT_CHP01").equals("A"))
					    vtrTXADD.add(M_rstRSSET.getString("L_CODCD"));
					else if(M_rstRSSET.getString("CMT_CHP01").equals("D"))
					    vtrTXDED.add(M_rstRSSET.getString("L_CODCD"));
					if((!M_rstRSSET.getString("CMT_CODCD").equals("EDC1"))&&(!M_rstRSSET.getString("CMT_CODCD").equals("EHC1")))
					    vtrTAXCD.add(M_rstRSSET.getString("L_CODCD"));    
					hstTAXCD.put(nvlSTRVL(M_rstRSSET.getString("L_CODCD"),"  ")+M_rstRSSET.getString("CMT_CHP01"),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"  "));
				//	if(!M_rstRSSET.getString("CMT_CODCD").equals("EDC1"))
				    hstTAXSQ.put(nvlSTRVL(M_rstRSSET.getString("L_CODCD"),"  ")+M_rstRSSET.getString("CMT_CHP01"),nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"  "));
				}
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			setMatrix(20,8);
			add(new JLabel("Credit Note Type"),4,2,1,2,this,'L');
			add(cmbRPTOP,4,3,1,2,this,'L');
			add(new JLabel("Category"),4,5,1,1,this,'L');
	    	add(txtOTHCT=new TxtLimit(2),4,6,1,1,this,'L');
	
			add(lblFMDAT = new JLabel("From Date "),5,2,1,2,this,'L');
			add(txtFMDAT = new TxtDate(),5,3,1,1.5,this,'L');
			add(lblTODAT = new JLabel("To Date "),6,2,1,2,this,'L');
			add(txtTODAT = new TxtDate(),6,3,1,1.5,this,'L');
			
			add(lblFMDOC = new JLabel("From Doc No"),5,2,1,2,this,'L');
			add(txtFMDOC = new TxtLimit(8),5,3,1,1.5,this,'L');
			add(lblTODOC = new JLabel("To Doc No"),6,2,1,2,this,'L');
			add(txtTODOC = new TxtLimit(8),6,3,1,1.5,this,'L');
			
			add(lblPRTCD = new JLabel("Party "),7,2,1,2,this,'L');
			add(txtPRTCD = new TxtLimit(5),7,3,1,0.5,this,'L');
			add(lblPRTNM = new JLabel(""),7,4,1,3,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		    M_strSQLQRY = "SELECT SUBSTRING(CMT_CODCD,1,4) CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='MST' AND CMT_CGSTP ='COXXPGR' AND CMT_CCSVL ='SG'";
		    M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		    System.out.println("M_strSQLQRY 4>>"+M_strSQLQRY);
			if(M_rstRSSET!=null)
			{	 
				while(M_rstRSSET.next())
				{
				    hstPRDDS.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}
				M_rstRSSET.close();
			}	
			txtOTHCT.setEnabled(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	mr_rpcrn(String P_strSBSCD)
	{
	    try
	    {
    	    M_strSBSCD = P_strSBSCD;
    	    M_strSQLQRY= "Select  SUBSTRING(CMT_CODCD,1,3) L_CODCD,CMT_CODCD,CMT_CODDS,CMT_CHP01,CMT_CCSVL from CO_CDTRN where CMT_CGMTP='TCL' and CMT_CGSTP = 'MRXXDCM' ORDER BY CMT_NMP01";
    		M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
    		if(M_rstRSSET!=null)
    		{
    			while(M_rstRSSET.next())
    			{
    			    if(M_rstRSSET.getString("CMT_CHP01").equals("A"))
    				    vtrTXADD.add(M_rstRSSET.getString("L_CODCD"));
    				else if(M_rstRSSET.getString("CMT_CHP01").equals("D"))
    				    vtrTXDED.add(M_rstRSSET.getString("L_CODCD"));
    				if(!M_rstRSSET.getString("CMT_CODCD").equals("EDC1"))
    				    vtrTAXCD.add(M_rstRSSET.getString("L_CODCD"));    
    				hstTAXCD.put(nvlSTRVL(M_rstRSSET.getString("L_CODCD"),"  ")+M_rstRSSET.getString("CMT_CHP01"),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"  "));
    			    hstTAXSQ.put(nvlSTRVL(M_rstRSSET.getString("L_CODCD"),"  ")+M_rstRSSET.getString("CMT_CHP01"),nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"  "));
    			}
    		}
    		if(M_rstRSSET!=null)
    			M_rstRSSET.close();
	    }
	    catch(Exception L_E)
	    {
            setMSG(L_E,"const");	        
	    }
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC==rdbDATWS)
		{
			txtFMDOC.setText("");
			txtTODOC.setText("");
			lblPRTNM.setText("");
			txtPRTCD.setText("");
			lblFMDOC.setVisible(false);
			txtFMDOC.setVisible(false);
			lblTODOC.setVisible(false);
			txtTODOC.setVisible(false);
			
			lblFMDAT.setVisible(true);
			txtFMDAT.setVisible(true);
			lblTODAT.setVisible(true);
			txtTODAT.setVisible(true);
		}
		if(M_objSOURC==rdbDOCWS)
		{
			lblPRTNM.setText("");
			txtPRTCD.setText("");
			lblFMDAT.setVisible(false);
			txtFMDAT.setVisible(false);
			lblTODAT.setVisible(false);
			txtTODAT.setVisible(false);
			
			lblFMDOC.setVisible(true);
			txtFMDOC.setVisible(true);
			lblTODOC.setVisible(true);
			txtTODOC.setVisible(true);
		}
		if(M_objSOURC==cmbRPTOP)
		{
			txtFMDOC.setText("");
			txtTODOC.setText("");
			txtPRTCD.setText("");
			lblPRTNM.setText("");
			if(cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("09"))
			{
	            txtOTHCT.setEnabled(true);		    
			}
			else
			{
			    txtOTHCT.setEnabled(false);		    
			    txtOTHCT.setText("");
			}
		}
		
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				txtFMDAT.requestFocus();
				setMSG("Please Enter Date to generate the Report..",'N');
			}
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
			txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
		}
	}
		
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		try
		{
			if(L_KE.getKeyCode() == KeyEvent.VK_ENTER)
			{
				if(M_objSOURC == cmbRPTOP)
				{
					txtFMDAT.requestFocus();
				}
				if(M_objSOURC == txtOTHCT)
				{
                    if(rdbDATWS.isSelected())
					    txtFMDAT.requestFocus();
                    else
                        txtFMDOC.requestFocus();
				}
				if(M_objSOURC == txtFMDAT)
				{
					if(txtFMDAT.getText().trim().length() == 10)
					{
						txtTODAT.requestFocus();
						setMSG("Enter Date",'N');
					}
					else
					{
						txtFMDAT.requestFocus();
						setMSG("Enter Date",'N');
					}
				}
				if(M_objSOURC == txtTODAT)
				{
					if(txtTODAT.getText().trim().length() == 10)
					{
                        txtPRTCD.setText("");
                        lblPRTNM.setText("");
                        txtPRTCD.requestFocus();
					}
					else
					{
						txtTODAT.requestFocus();
						setMSG("Enter Date",'N');
					}
				}
				if(M_objSOURC==txtFMDOC)
				{
					txtTODOC.requestFocus();
				}
				if(M_objSOURC==txtTODOC)
				{
                    txtPRTCD.setText("");
                    lblPRTNM.setText("");
					txtPRTCD.requestFocus();
				}
				if(M_objSOURC == txtPRTCD)
				{
					if(txtPRTCD.getText().trim().length()<5)
						lblPRTNM.setText("");
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				setMSG("",'N');
			}
	 		else if(L_KE.getKeyCode() == L_KE.VK_F1)
 			{						
				if(M_objSOURC == txtPRTCD) 
				{			
					txtPRTCD.setText((txtPRTCD.getText().trim()).toUpperCase());
					String L_strFMDAT = txtFMDAT.getText();
					String L_strTODAT = txtTODAT.getText();
					String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
					String strCRDTP = L_strCRDTP.substring(0,2);
					M_strHLPFLD = "txtPRTCD";				
					if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
					    M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP = 'D' ";
					else
					    M_strSQLQRY = "select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where PT_PRTTP = 'C' ";
					if(rdbDATWS.isSelected())
					{
						if(L_strFMDAT.length()==10 && L_strTODAT.length()==10)
						{	
			  				if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
			    				M_strSQLQRY += " and PT_PRTCD in (select b.PT_PRTCD from MR_PTTRN b where b.PT_CRDTP = '"+strCRDTP+"' and b.PT_PRTTP = 'C' and b.PT_RPTDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"') ";
			    			else
			    				M_strSQLQRY += " and PT_PRTCD in (select b.PT_PRTCD from MR_PTTRN b where b.PT_CRDTP = '"+strCRDTP+"' and b.PT_PRTTP = 'C' and b.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"') ";
						}
					}
					else if(rdbDOCWS.isSelected())
					{
						if(txtFMDOC.getText().trim().length()==8 && txtTODOC.getText().trim().length()==8)
						{
							if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
							    M_strSQLQRY += " and PT_PRTCD in (select b.PT_PRTCD from MR_PTTRN b where b.PT_CRDTP = '"+strCRDTP+"' and b.PT_PRTTP = 'D' and b.PT_DOCRF between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"') ";
							else
							    M_strSQLQRY += " and PT_PRTCD in (select b.PT_PRTCD from MR_PTTRN b where b.PT_CRDTP = '"+strCRDTP+"' and b.PT_PRTTP = 'C' and b.PT_DOCRF between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"') ";
						}
					}
					
					if(txtPRTCD.getText().trim().length()>0)
					{
						M_strSQLQRY +=" AND PT_PRTCD like '"+txtPRTCD.getText().trim()+"%'";
					}
					M_strSQLQRY += " order by PT_PRTNM";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Party Name"},2,"CT");
				}
				if(M_objSOURC == txtOTHCT)
    			{
    				M_strHLPFLD = "txtOTHCT";
    				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' and CMT_CGSTP = 'MRXXOCN' And CMT_CODCD like '0%'";					
    				cl_hlp(M_strSQLQRY ,1,1,new String[] {"Code","Description"},2,"CT");
    			}
				if(M_objSOURC == txtFMDOC) 
				{
					M_strHLPFLD = "txtFMDOC";		
					String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
					String strCRDTP = L_strCRDTP.substring(0,2);
					if((strCRDTP.equals("09"))&&(txtOTHCT.getText().length() >0))
					    strCRDTP = txtOTHCT.getText().trim();
					if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
					{
    					M_strSQLQRY = "select distinct A.PT_DOCRF,B.PT_PRTNM from MR_PTTRN A,CO_PTMST B where A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD AND A.PT_CRDTP = '"+strCRDTP+"' and A.PT_DOCRF <> '00000000'  AND PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
    					if(txtFMDOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND A.PT_DOCRF like '"+txtFMDOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by A.PT_DOCRF";
						System.out.println(M_strSQLQRY);
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Distributor"},2,"CT");
					}
					else 
					{
    					M_strSQLQRY = "select distinct A.PT_DOCRF,A.PT_DOCDT,B.PT_PRTNM from MR_PTTRN A,CO_PTMST B where A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD and PT_CRDTP = '"+strCRDTP+"'  AND PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
    					if(txtFMDOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND A.PT_DOCRF like '"+txtFMDOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by A.PT_DOCRF";
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Date","Party"},3,"CT");
					}
				}
				if(M_objSOURC == txtTODOC) 
				{
					M_strHLPFLD = "txtTODOC";
					String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
					String strCRDTP = L_strCRDTP.substring(0,2);
					if((strCRDTP.equals("09"))&&(txtOTHCT.getText().length() >0))
					    strCRDTP = txtOTHCT.getText().trim();
					if((strCRDTP.equals("02"))||(strCRDTP.equals("03")))
					{
    					M_strSQLQRY = "select distinct A.PT_DOCRF,B.PT_PRTNM from MR_PTTRN A,CO_PTMST B where A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD AND A.PT_CRDTP = '"+strCRDTP+"' and A.PT_DOCRF <> '00000000'  AND PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
    					if(txtFMDOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND a.PT_DOCRF like '"+txtTODOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by a.PT_DOCRF";
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Doc Date"},2,"CT");
					}
					else
					{
    					M_strSQLQRY = "select distinct A.PT_DOCRF,A.PT_DOCDT,B.PT_PRTNM from MR_PTTRN A,CO_PTMST B where A.PT_PRTTP = B.PT_PRTTP AND A.PT_PRTCD = B.PT_PRTCD and PT_CRDTP = '"+strCRDTP+"'  AND PT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
    					if(txtTODOC.getText().trim().length()>0)
    					{
    						M_strSQLQRY +=" AND A.PT_DOCRF like '"+txtTODOC.getText().trim()+"%'";
    					}
    					M_strSQLQRY += " order by A.PT_DOCRF";
    					cl_hlp(M_strSQLQRY,1,1,new String[]{"Doc No","Date","Party"},3,"CT");
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	
   /**
	Method for execution of help for Memo Number Field.
   */
	void exeHLPOK()
	{
		super.exeHLPOK();		
		StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
		if(M_strHLPFLD == "txtPRTCD")
		{
			txtPRTCD.setText(L_STRTKN.nextToken());
			lblPRTNM.setText(L_STRTKN.nextToken());
		}
		if(M_strHLPFLD == "txtFMDOC")
		{
			txtFMDOC.setText(L_STRTKN.nextToken());
		}
		if(M_strHLPFLD == "txtOTHCT")
		{
			txtOTHCT.setText(cl_dat.M_strHLPSTR_pbst);
		}
		if(M_strHLPFLD == "txtTODOC")
		{
			txtTODOC.setText(L_STRTKN.nextToken());
		}
	}
		
	/**
	 * User friendly messagees
	*/
	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		try
		{
			if(L_FE.getSource().equals(txtFMDAT))
			{
                setMSG("Enter  Date in format dd/mm/yyyy",'N');
			}
			if(L_FE.getSource().equals(txtTODAT))
			{
                setMSG("Enter  Date in format dd/mm/yyyy",'N');
			}
		}
		catch(Exception e)
		{
			setMSG(e,"TEIND.FocusGained"+M_objSOURC);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
			
	/**
	 * Method to fetch data from Database & club it with Header & Footer.
	*/
	public void getDATA()
	{		
		String strSRTVL1="";
		String strACSVL1="";
		String strTDSVL1="";
		String strSCHVL1="";	
		String strLCSVL1="";
		String strNETVL1="";
		
		java.sql.Date jdtTEMP;
		cl_dat.M_intLINNO_pbst =0;
		setCursor(cl_dat.M_curWTSTS_pbst);
				
		String L_strFMDAT = txtFMDAT.getText().trim();
       	String L_strTODAT = txtTODAT.getText().trim();
	
		String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
		String strCRDTP = L_strCRDTP.substring(0,2);
		if((strCRDTP.equals("09"))&&(txtOTHCT.getText().length() >0))
			strCRDTP = txtOTHCT.getText().trim();
		String strCRDTP1 = L_strCRDTP.substring(2,L_strCRDTP.length()).toString().trim();
	 
		try
	    {	        
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>"+ cmbRPTOP.getSelectedItem().toString()+"</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			String strPRTCD = txtPRTCD.getText();
			if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03")) // other than distributor comission
			{
			    prnDSTCM();
			}
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03")) // other than distributor comission
			{
                if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("39")) // Misc debit 
        	    {
                    M_strSQLQRY = "select (B.PT_DOCRF)PT_DOCRF, (B.PT_DOCDT)PT_DOCDT, (B.PT_ACCRF)PT_ACCRF,(B.PT_ACCDT) PT_ACCDT, (B.PT_INVNO)PT_INVNO,(B.PT_TRNRT)PT_TRNRT,(B.PT_INVQT)PT_INVQT,(B.PT_PGRVL)PT_PGRVL,(B.PT_PNTVL)PT_PNTVL,(B.PT_ATXVL)PT_ATXVL,(B.PT_LTXVL)PT_LTXVL,";
        			M_strSQLQRY +=" ''IVT_INDNO,''  IVT_INVDT,'' IVT_PRDDS,(A.PT_PRTNM)PT_PRTNM,(A.PT_ADR01)PT_ADR01,(A.PT_ADR02)PT_ADR02,(A.PT_ADR03)PT_ADR03,(A.PT_ADR04)PT_ADR04,(A.PT_PRTCD)PT_PRTCD,(A.PT_DSRCD)PT_DSRCD";
        			M_strSQLQRY +=" from MR_PTTRN B,CO_PTMST A where";
        			M_strSQLQRY +=" B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD";
        			if(rdbDATWS.isSelected())
        			{
        				M_strSQLQRY +=" and B.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
        			}
        			if(rdbDOCWS.isSelected())
        			{
        				M_strSQLQRY +=" and B.PT_DOCRF between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"'";
        			}
        			M_strSQLQRY +=" and B.PT_STSFL <> 'X' and B.PT_CRDTP ='"+strCRDTP+"'  ";
        			if(strPRTCD.length()==5)
        				M_strSQLQRY += " and B.PT_PRTTP = 'C' and B.PT_PRTCD = '"+strPRTCD+"' ";
        		    M_strSQLQRY +=" order by PT_CRDTP,B.PT_DOCRF,B.PT_INVNO";
                }
                else if(((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09"))&& strCRDTP.equals("0Z")) 
        		{
					
/**				M_strSQLQRY = "SELECT IN_INDNO,IN_MATCD,DATE(IN_AUTDT) IN_AUTDT,IN_PORBY,IN_EXPDT,IN_DPTCD,CT_MATDS,";
				M_strSQLQRY+= "CT_UOMCD,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,(IFNULL(CT_ILDTM,0)+IFNULL(CT_ELDTM,0)) ";
				M_strSQLQRY+= "AS CT_TOTAL,(IFNULL(CT_IILTM,0)+IFNULL(CT_IELTM,0)) AS CT_ITOTA,PO_PORNO,PT_PRTNM,";
				M_strSQLQRY+= "PO_PORDT,PO_PORQT,PO_FRCQT,GR_RECQT,GR_ACPQT,GR_REJQT,GR_GRNNO,GR_GRNDT,DATE(WB_GINDT)L_GINDT,";
				M_strSQLQRY+= "(DAYS(IFNULL(PO_PORDT,CURRENT_DATE)) - DAYS(IN_PORBY)) AS PO_DEVIA,(DAYS(IFNULL(DATE(WB_GINDT),";
				M_strSQLQRY+= "CURRENT_DATE)) - DAYS(IN_EXPDT)) AS GR_DEVIA FROM CO_CTMST,MM_INMST LEFT OUTER JOIN ";
				M_strSQLQRY+= "MM_POMST ON IN_STRTP = PO_STRTP AND IN_INDNO = PO_INDNO AND IN_MATCD = PO_MATCD AND ";
				M_strSQLQRY+= "IFNULL(PO_STSFL,'') <> 'X'  LEFT OUTER JOIN CO_PTMST ON PO_VENCD=PT_PRTCD AND PT_PRTTP ='S' ";
				M_strSQLQRY+= "LEFT OUTER JOIN MM_GRMST ON PO_STRTP = GR_STRTP AND PO_PORNO = GR_PORNO AND ";
				M_strSQLQRY+= "PO_MATCD = GR_MATCD LEFT OUTER JOIN MM_WBTRN ON GR_GINNO = WB_DOCNO AND ";
				M_strSQLQRY+= "GR_CMPCD=WB_CMPCD AND WB_DOCTP ='02' WHERE IN_MATCD = CT_MATCD AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
				M_strSQLQRY+= "IFNULL(IN_STSFL,'') <> 'X' AND IFNULL(IN_INDTP,'') = '01' and IFNULL(IN_AUTQT,0)-IFNULL(IN_FCCQT,0) >0 ";
				M_strSQLQRY+= "AND IN_STRTP = '"+M_strSBSCD.substring(2,4)+"'"; */
// NEW QUERY MODIFIED BY SRT ON 16/01/2009
					
				M_strSQLQRY = "select (B.PT_DOCRF)PT_DOCRF, (B.PT_DOCDT)PT_DOCDT, (B.PT_ACCRF)PT_ACCRF,(B.PT_ACCDT) PT_ACCDT, (B.PT_INVNO)PT_INVNO,(B.PT_TRNRT)PT_TRNRT,(B.PT_INVQT)PT_INVQT,(B.PT_PGRVL)PT_PGRVL,(B.PT_PNTVL)PT_PNTVL,(B.PT_ATXVL)PT_ATXVL,(B.PT_LTXVL)PT_LTXVL,";
       			M_strSQLQRY +=" IVT_INDNO, CONVERT(varchar,IVT_INVDT,103) IVT_INVDT,IVT_PRDDS,(A.PT_PRTNM)PT_PRTNM,(A.PT_ADR01)PT_ADR01,(A.PT_ADR02)PT_ADR02,(A.PT_ADR03)PT_ADR03,(A.PT_ADR04)PT_ADR04,(A.PT_PRTCD)PT_PRTCD,(A.PT_DSRCD)PT_DSRCD";
       			M_strSQLQRY +=" from CO_PTMST A,MR_PTTRN B  LEFT OUTER JOIN MR_IVTRN ON PT_CMPCD = IVT_CMPCD AND PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' AND PT_INVNO=IVT_INVNO AND PT_PRDCD=IVT_PRDCD where";
        		M_strSQLQRY +=" B.PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' AND B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD";
				System.out.println(M_strSQLQRY);	

				//OLD QUERY
				
      //  			M_strSQLQRY = "select (B.PT_DOCRF)PT_DOCRF, (B.PT_DOCDT)PT_DOCDT, (B.PT_ACCRF)PT_ACCRF,(B.PT_ACCDT) PT_ACCDT, (B.PT_INVNO)PT_INVNO,(B.PT_TRNRT)PT_TRNRT,(B.PT_INVQT)PT_INVQT,(B.PT_PGRVL)PT_PGRVL,(B.PT_PNTVL)PT_PNTVL,(B.PT_ATXVL)PT_ATXVL,(B.PT_LTXVL)PT_LTXVL,";
        //			M_strSQLQRY +=" ''IVT_INDNO,'' IVT_INVDT,'' IVT_PRDDS,(A.PT_PRTNM)PT_PRTNM,(A.PT_ADR01)PT_ADR01,(A.PT_ADR02)PT_ADR02,(A.PT_ADR03)PT_ADR03,(A.PT_ADR04)PT_ADR04,(A.PT_PRTCD)PT_PRTCD,(A.PT_DSRCD)PT_DSRCD";
        //			M_strSQLQRY +=" from MR_PTTRN B,CO_PTMST A where";
        //		//	M_strSQLQRY +=" B.PT_INVNO = IVT_INVNO";
        //		//	M_strSQLQRY +=" and B.PT_PRDCD = IVT_PRDCD and B.PT_PKGTP = IVT_PKGTP";
        //			M_strSQLQRY +=" B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD";
        			if(rdbDATWS.isSelected())
        			{
        				M_strSQLQRY +=" and B.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
        			}
        			if(rdbDOCWS.isSelected())
        			{
        				M_strSQLQRY +=" and B.PT_DOCRF between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"'";
        			}
        			M_strSQLQRY +=" and B.PT_STSFL <> 'X' and B.PT_CRDTP ='"+strCRDTP+"'  ";
        			if(strPRTCD.length()==5)
        				M_strSQLQRY += " and B.PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' AND B.PT_PRTTP = 'C' and B.PT_PRTCD = '"+strPRTCD+"' ";
        		//	M_strSQLQRY +=" order by PT_CRDTP,B.PT_DOCRF,IVT_INDNO,B.PT_INVNO";
        		M_strSQLQRY +=" order by PT_CRDTP,B.PT_DOCRF,B.PT_INVNO";
                }
        		else
        		{
        		    if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("02")) 
        		    	M_strSQLQRY = "select (B.PT_DOCRF)PT_DOCRF, (B.PT_RPTDT)PT_DOCDT, (B.PT_ACCRF)PT_ACCRF,(B.PT_ACCDT) PT_ACCDT, (B.PT_INVNO)PT_INVNO,(B.PT_TRNRT)PT_TRNRT,(B.PT_INVQT)PT_INVQT,(B.PT_PGRVL)PT_PGRVL,(B.PT_PNTVL)PT_PNTVL,(B.PT_ATXVL)PT_ATXVL,(B.PT_LTXVL)PT_LTXVL,";
        		    else
        			    M_strSQLQRY = "select (B.PT_DOCRF)PT_DOCRF, (B.PT_DOCDT)PT_DOCDT, (B.PT_ACCRF)PT_ACCRF,(B.PT_ACCDT) PT_ACCDT, (B.PT_INVNO)PT_INVNO,(B.PT_TRNRT)PT_TRNRT,(B.PT_INVQT)PT_INVQT,(B.PT_PGRVL)PT_PGRVL,(B.PT_PNTVL)PT_PNTVL,(B.PT_ATXVL)PT_ATXVL,(B.PT_LTXVL)PT_LTXVL,";
        			M_strSQLQRY +=" IVT_INDNO,CONVERT(varchar,IVT_INVDT,103) IVT_INVDT,IVT_PRDDS,(A.PT_PRTNM)PT_PRTNM,(A.PT_ADR01)PT_ADR01,(A.PT_ADR02)PT_ADR02,(A.PT_ADR03)PT_ADR03,(A.PT_ADR04)PT_ADR04,(A.PT_PRTCD)PT_PRTCD,(A.PT_DSRCD)PT_DSRCD";
        			M_strSQLQRY +=" from MR_PTTRN B,MR_IVTRN,CO_PTMST A where b.PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and b.PT_CMPCD = IVT_CMPCD ";
        			M_strSQLQRY +=" and B.PT_INVNO = IVT_INVNO";
        			M_strSQLQRY +=" and B.PT_PRDCD = IVT_PRDCD and B.PT_PKGTP = IVT_PKGTP";
        			M_strSQLQRY +=" and B.PT_PRTTP = A.PT_PRTTP and B.PT_PRTCD = A.PT_PRTCD";
        			if(rdbDATWS.isSelected())
        			{
        				M_strSQLQRY +=" and B.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
        			}
        			if(rdbDOCWS.isSelected())
        			{
        				M_strSQLQRY +=" and B.PT_DOCRF between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"'";
        			}
        			M_strSQLQRY +=" and B.PT_STSFL <> 'X' and B.PT_CRDTP ='"+strCRDTP+"'  ";
        			if(strPRTCD.length()==5)
        				M_strSQLQRY += " and B.PT_PRTTP = 'C' and B.PT_PRTCD = '"+strPRTCD+"' ";
        			M_strSQLQRY +=" order by PT_CRDTP,B.PT_DOCRF,IVT_INDNO,B.PT_INVNO";
        		}	
        			strPDCRF="";
        			strPIVNO="";
        			int L_CNT =0;
					System.out.println("M_strSQLQRY 5>>"+M_strSQLQRY);
        			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
        			if(M_rstRSSET != null)
        			{ 
        				while(M_rstRSSET.next())
        				{
        					intRECCT=1;
        					strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
        					strDSRCD = nvlSTRVL(M_rstRSSET.getString("PT_DSRCD"),"");
        					if(hstDSRNM.get(strDSRCD) !=null)
        					strDSRNM = hstDSRNM.get(strDSRCD).toString();
        					else strDSRNM = "";
        					strADR01 = nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),"");
        					strADR02 = nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),"");
        					strADR03 = nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),"");
        					strADR04 = nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),"");
        					strGRPCD = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
        					strDOCRF = nvlSTRVL(M_rstRSSET.getString("PT_DOCRF"),"");
							
				//	L_strINVDT = nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),"");
        						
        					L_datTEMP = M_rstRSSET.getDate("PT_DOCDT");
        					if(L_datTEMP !=null)
        						strDOCDT = nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),"");
        					strACCRF = nvlSTRVL(M_rstRSSET.getString("PT_ACCRF"),"");//if value is available,it display otherwise not display
        					L_datTEMP = M_rstRSSET.getDate("PT_ACCDT");
        					if(L_datTEMP !=null)
        						strACCDT = nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),"");
        					else strACCDT ="";
        					strINDNO = nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),"");
        					strINVNO = nvlSTRVL(M_rstRSSET.getString("PT_INVNO"),"");
        					
        					//L_datTEMP1 = M_rstRSSET.getDate("IVT_INVDT");
        					//if(L_datTEMP1 !=null)
        					//	strINVDT = nvlSTRVL(M_fmtLCDAT.format(L_datTEMP1),"");
							strINVDT = nvlSTRVL(M_rstRSSET.getString("IVT_INVDT"),"");
        						
        					strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"");
        					strCRDRT = nvlSTRVL(M_rstRSSET.getString("PT_TRNRT"),"");
        					strINVQT = nvlSTRVL(M_rstRSSET.getString("PT_INVQT"),"0");
        					strCRDVL = nvlSTRVL(M_rstRSSET.getString("PT_PGRVL"),"0");
						if(strCRDTP.equals("0D"))
	        					strCRDVL = nvlSTRVL(M_rstRSSET.getString("PT_PNTVL"),"0");
        					strATXVL = nvlSTRVL(M_rstRSSET.getString("PT_ATXVL"),"0");
        					strLTXVL = nvlSTRVL(M_rstRSSET.getString("PT_LTXVL"),"0");
        			//		dblATXVL +=M_rstRSSET.getString("PT_ATXVL");
        			//		dblLTXVL +=M_rstRSSET.getString("PT_LTXVL");
        					if(!strPDCRF.equals(strDOCRF))
        					{
        						if(L_CNT >0)
        						{
        							//prnTAX();	
        							//calTAXVL(strCRDTP,strPDCRF);						
        							prnHEADER_L();
        						}
        						dosREPORT.writeBytes("\n");
        						dosREPORT.writeBytes("\n");
        					    if(cl_dat.M_intLINNO_pbst >= 62)
        						{
        							cl_dat.M_intLINNO_pbst = 0;
        			    			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
        								prnFMTCHR(dosREPORT,M_strEJT);
        						}
        						if(cl_dat.M_intLINNO_pbst <= 31)
        							dosREPORT.writeBytes("\n");
        						prnHEADER_U();
        					    prnHEADER_M();
        						/*if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09"))
        						{
        						    prnHEADER_M();
        						}
        						else if (!txtOTHCT.getText().equals("0Z"))
        						{
        						    prnHEADER_M();
        						}*/
        						strPDCRF = strDOCRF;
        					}
        					else
        					{
        					  	prnHEADER_M();
        						/*if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09"))
        						    prnHEADER_M();
        						else if (!txtOTHCT.equals("0Z"))
        						    prnHEADER_M();*/
        				
        					}
        					strPIVNO = strINVNO;
        					strSRTVL=strSRTVL1;
        					strACSVL=strACSVL1;
        					strTDSVL=strTDSVL1;
        					strSCHVL=strSCHVL1;	
        					strLCSVL=strLCSVL1;
        					strNETVL=strNETVL1;
        					L_CNT++;
        				}
        				if(L_CNT >0)
        				{
        	  			    prnHEADER_L();
        				}
        			}
					//ADDED BY SATYA
					if(M_rstRSSET!=null)
					M_rstRSSET.close();
        		}
			///if(M_rstRSSET!=null)
			///	M_rstRSSET.close();
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);					
			}			
			if(M_rdbHTML.isSelected())				
				dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			if(fosREPORT !=null)    
			    fosREPORT.close();
			if(dosREPORT !=null)        
			    dosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	
	/**
	 * Method to validate inputs given before execuation of SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
			if(txtPRTCD.getText().trim().length()>0 && txtPRTCD.getText().trim().length()<5)
			{
				setMSG("Invalid Party Code Press f1 for help ..",'E');
				txtPRTCD.requestFocus();
				return false;
			}
			if(rdbDATWS.isSelected())
			{
				if(txtFMDAT.getText().trim().length() != 10)
				{
					setMSG("Enter the proper Date..",'E');
					txtFMDAT.requestFocus();
					return false;
				}
				if(txtTODAT.getText().trim().length() != 10)
				{
					setMSG("Enter the proper Date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
			
				if(txtFMDAT.getText().trim().length()==10 && txtTODAT.getText().trim().length()==10)
				{
					setMSG(" ",'N');			
					if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtTODAT.getText().trim()))>0)
					{
						setMSG("From Date can not be greater than TO Date's date..",'E');
						txtFMDAT.requestFocus();
						return false;
					}
				}
				if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
				{
					setMSG("Date can not be greater than today's date..",'E');
					txtTODAT.requestFocus();
					return false;
				}
			}
			if(rdbDOCWS.isSelected())
			{
				if(txtFMDOC.getText().trim().length()<8)
				{
					setMSG("Please Enter proper Doc. Number or Press f1 for Help..",'E');
					txtFMDOC.requestFocus();
					return false;
				}
				if(txtTODOC.getText().trim().length()<8)
				{
					setMSG("Please Enter proper Doc. Number or Press f1 for Help..",'E');
					txtTODOC.requestFocus();
					return false;
				}
				if( Integer.parseInt(txtTODOC.getText().trim().toString()) < Integer.parseInt(txtFMDOC.getText().trim().toString()) )
				{
					setMSG("To DOC Number Should Be Greater Or Equal To From DOC Number..",'E');
					txtTODOC.requestFocus();
					return false;
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
		}
		return true;
	}
	
	/**
	 * Method to generate the header of the Report.
	*/
	void prnHEADER_U()
	{
		try
		{
			String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
			String strCRDTP1 = L_strCRDTP.substring(2,L_strCRDTP.length()).toString().trim();
	        if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");		
			dosREPORT.writeBytes(padSTRING('C',"SUPREME PETROCHEM LTD",strDOTLN.length()));				
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
					
			dosREPORT.writeBytes("\n");
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			{
				dosREPORT.writeBytes(padSTRING('C',"Solitaire Corporate Park,Bldg No-11,5th floor,Chakala,Andheri(E)",strDOTLN.length()));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes(padSTRING('C',"Tel : 67091900 to 04 Fax :67011926",strDOTLN.length()));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 2;
			}
			else
			{				
				//dosREPORT.writeBytes(padSTRING('C',"Bldg No-11,5th floor,Solitair corporator park,chakala,Anadheri(E),Tel : 67091900 to 04 Fax :67011926",strDOTLN.length()));
                dosREPORT.writeBytes(padSTRING('C',"Solitaire Corporate Park,Bldg No-11,5th floor,Chakala,Andheri(E),Tel : 67091900 to 04 Fax :67011926",strDOTLN.length()));
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			 if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");		
			dosREPORT.writeBytes(padSTRING('C',nvlSTRVL(strCRDTP1+"",""),100)+"\n");
		   if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");	
			dosREPORT.writeBytes(strDOTLN+"\n");
			if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			    dosREPORT.writeBytes(padSTRING('R',"Customer Name		Distributor : "+strDSRNM+" ("+strDSRCD+")",80));
			else if(((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("02"))||((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03")))
			    dosREPORT.writeBytes(padSTRING('R',"Distributor : "+strPRTNM+" ("+strGRPCD+")",80));
            else
                dosREPORT.writeBytes(padSTRING('R',"Customer Name		Distributor : "+strDSRNM+" ("+strDSRCD+")",80));
               // dosREPORT.writeBytes(padSTRING('R',"Distributor : "+strPRTNM+" ("+strGRPCD+")",80));
			//dosREPORT.writeBytes(padSTRING('L',"Particulars",11)+"\n");
			dosREPORT.writeBytes(padSTRING('L'," ",11)+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
			dosREPORT.writeBytes(padSTRING('R',strPRTNM+""+"("+strGRPCD+""+")",45));
			dosREPORT.writeBytes(padSTRING('L',"|",5));														   
			if((cmbRPTOP.getSelectedItem().toString().substring(0,1)).equals("0"))
				dosREPORT.writeBytes(padSTRING('R'," C.N.No     : "+strDOCRF+"",30));
			else
				dosREPORT.writeBytes(padSTRING('R'," D.N.No     : "+strDOCRF+"",30));
			dosREPORT.writeBytes(padSTRING('R'," Date   : "+strDOCDT+"",20)+"\n");
			dosREPORT.writeBytes(padSTRING('R',strADR01+"",45));
			dosREPORT.writeBytes(padSTRING('L',"|",5));
			dosREPORT.writeBytes(padSTRING('R'," Acc.Ref.No : "+strACCRF+"",30));
			dosREPORT.writeBytes(padSTRING('R',"Date   : "+strACCDT,20)+"\n");
			dosREPORT.writeBytes(padSTRING('R',strADR02+"",45));
			dosREPORT.writeBytes(padSTRING('L',"|",5));
			dosREPORT.writeBytes(padSTRING('R',strDOTLN+"",50)+"\n");
			dosREPORT.writeBytes(padSTRING('R',strADR03+"",45));
			dosREPORT.writeBytes(padSTRING('L',"|",5));
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03"))
			{
				//dosREPORT.writeBytes(padSTRING('R',"BST No.400053-S 819 (BBY) W.E.F.01-04-96",strDOTLN.length()-16)+"\n");
				dosREPORT.writeBytes(padSTRING('R',"VAT TIN 27530283527V W.E.F.01-04-2006",strDOTLN.length()-16)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			else
			{
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			dosREPORT.writeBytes(padSTRING('R',strADR04+"",45));
			dosREPORT.writeBytes(padSTRING('L',"|",5));
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03"))
			{
				//dosREPORT.writeBytes(padSTRING('R',"BST No.402106-S 3 (NGT) W.E.F.01-04-96",strDOTLN.length()-16)+"\n");
				dosREPORT.writeBytes(padSTRING('R',"CST TIN 27530283527C W.E.F.01-04-2006",strDOTLN.length()-16)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			else
			{
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			dosREPORT.writeBytes(padSTRING('L',"|",50));
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03"))
			{
				//dosREPORT.writeBytes(padSTRING('R',"CST No.400063-C 711  W.E.F.01-04-96",strDOTLN.length()-16)+"\n");
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			else
			{
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			dosREPORT.writeBytes(strDOTLN+"\n");
			/*if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			{
				dosREPORT.writeBytes(padSTRING('R',"Description	",strDOTLN.length()-7)+"\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				cl_dat.M_intLINNO_pbst += 2;
			}*/			
			
			if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03"))
			{
			    dosREPORT.writeBytes(padSTRING('R',"Particulars                                               Comm./MT      Inv.Qty.",strDOTLN.length()-21));
			}
			else if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("02"))
			{
				dosREPORT.writeBytes(padSTRING('R',"Indent No.  Inv.No.     Inv.Date      Product & grade     Comm./MT      Inv.Qty.",strDOTLN.length()-21));
			}
			else if(((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09"))&& txtOTHCT.getText().equals("0Z") && strPRDDS.equals(""))
			{
				dosREPORT.writeBytes(padSTRING('R',"Description",strDOTLN.length()-21));
			}
			else if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("39"))
			{
				dosREPORT.writeBytes(padSTRING('R',"Description",strDOTLN.length()-21));
			}
			else
			{
				dosREPORT.writeBytes(padSTRING('R',"Indent No.  Inv.No.     Inv.Date      Product & grade      Rate/MT      Inv.Qty.",strDOTLN.length()-21));
			}
			dosREPORT.writeBytes(padSTRING('L',"|",6));
			dosREPORT.writeBytes(padSTRING('L',"Amount(Rs.)",strDOTLN.length()-85)+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst += 11;
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER_U ");
		}
	}
	void prnHEADER_M()
	{
		try
		{	
		    if(((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09"))&&txtOTHCT.getText().trim().equals("0Z")&&strINVNO.equals(""))
		    {
		        dosREPORT.writeBytes(padSTRING('R',"",79));
				dosREPORT.writeBytes(padSTRING('L',"|",6));
				dosREPORT.writeBytes(padSTRING('L',strCRDVL+"",14)+"\n");
				i+=1;
				cl_dat.M_intLINNO_pbst += 1;
		    }
		    else if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("39"))
		    {
		        dosREPORT.writeBytes(padSTRING('R',"",79));
				dosREPORT.writeBytes(padSTRING('L',"|",6));
				dosREPORT.writeBytes(padSTRING('L',strCRDVL+"",14)+"\n");
				i+=1;
				cl_dat.M_intLINNO_pbst += 1;
		    }
		    else
		    {
    			if(!strPIVNO.equals(strINVNO))
    			{
					System.out.println("in");
    				dosREPORT.writeBytes(padSTRING('R',strINDNO+"",12));
    				dosREPORT.writeBytes(padSTRING('R',strINVNO+"",12));
    				dosREPORT.writeBytes(padSTRING('R',strINVDT+"",14));
    				dosREPORT.writeBytes(padSTRING('R',strPRDDS+"",15));
    				dosREPORT.writeBytes(padSTRING('L',strCRDRT+"",13));
    				dosREPORT.writeBytes(padSTRING('L',strINVQT+"",13));
    				dosREPORT.writeBytes(padSTRING('L',"|",6));
    				dosREPORT.writeBytes(padSTRING('L',strCRDVL+"",14)+"\n");
    				i+=1;
    				cl_dat.M_intLINNO_pbst += 1;
    			}
    			else
    			{
    				dosREPORT.writeBytes(padSTRING('L'," ",38));
    				dosREPORT.writeBytes(padSTRING('R',strPRDDS+"",15));
    				dosREPORT.writeBytes(padSTRING('L',strCRDRT+"",13));
    				dosREPORT.writeBytes(padSTRING('L',strINVQT+"",13));
    				dosREPORT.writeBytes(padSTRING('L',"|",6));
    				dosREPORT.writeBytes(padSTRING('L',strCRDVL+"",14)+"\n");
    				i+=1;
    				cl_dat.M_intLINNO_pbst += 1;
    			}
			}
			strPCDVL=strCRDVL;
			strPIVQT=strINVQT;
			double dblCRDVL = Double.parseDouble(strPCDVL);
			double dblINVQT = Double.parseDouble(strPIVQT);
			dblAMTOT = dblAMTOT + dblCRDVL;
			dblQTTOT = dblQTTOT + dblINVQT;
			dblATXVL = dblATXVL + Double.parseDouble(strATXVL);
			dblLTXVL = dblLTXVL + Double.parseDouble(strLTXVL);
		}
		catch(Exception L_HD)
		{
		  	setMSG(L_HD,"prnHEADER_M ");
		}
	}
	void prnHEADER_M1()
	{
		try
		{	
			if(!strINVNO.equals(strPIVNO))
			{
				dosREPORT.writeBytes(getCODVL("SYSMR00SAL"+strINVNO,cl_dat.M_intCODDS_pbst)+"\n");
				dosREPORT.writeBytes(padSTRING('L'," ",15));
				dosREPORT.writeBytes(padSTRING('R',hstPRDDS.get(strPRDCT).toString()+"",38));
				dosREPORT.writeBytes(padSTRING('L',strCRDRT+"",13));
				dosREPORT.writeBytes(padSTRING('L',strINVQT+"",13));
				dosREPORT.writeBytes(padSTRING('L',"|",6));
				dosREPORT.writeBytes(padSTRING('L',strCRDVL+"",14)+"\n");
				i+=1;
				cl_dat.M_intLINNO_pbst += 2;
				//strPSALTP = strSALTP;
			}
			else
			{
			  	dosREPORT.writeBytes(padSTRING('L'," ",15));
				dosREPORT.writeBytes(padSTRING('R',hstPRDDS.get(strPRDCT).toString()+"",38));
				dosREPORT.writeBytes(padSTRING('L',strCRDRT+"",13));
				dosREPORT.writeBytes(padSTRING('L',strINVQT+"",13));
				dosREPORT.writeBytes(padSTRING('L',"|",6));
				dosREPORT.writeBytes(padSTRING('L',strCRDVL+"",14)+"\n");
				i+=1;
				cl_dat.M_intLINNO_pbst += 1;
			}
			strPCDVL=strCRDVL;
			strPIVQT=strINVQT;
			double dblCRDVL = Double.parseDouble(nvlSTRVL(strPCDVL,"0"));
			double dblINVQT = Double.parseDouble(nvlSTRVL(strPIVQT,"0"));
			dblAMTOT = dblAMTOT + dblCRDVL;
			dblQTTOT = dblQTTOT + dblINVQT;
			///dblATXVL = dblATXVL + Double.parseDouble(strATXVL);
			///dblLTXVL = dblLTXVL + Double.parseDouble(strLTXVL);
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER_M1 ");
		}
	}
	
	void prnHEADER_L()
	{
		try
		{	
		   	String L_strSQLQRY="";
			String L_strPRNST ="";
			ResultSet L_rstRSSET;
			String L_strTAXCD="",L_strDOCTP="",L_strTAXFL1="",L_strTAXFL="",L_strTAXVL="";
			//double dblTOTAD =0;	
			//double dblTOTDD =0;
			for(int j=i;j<5;j++)
			{
				dosREPORT.writeBytes(padSTRING('L',"|",85)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
				i=0;
				int z=0;
			if(!cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("01"))
			{
				// For type 02 and 03 TAx printing
			  	dosREPORT.writeBytes(padSTRING('L',"|",85));
				if(((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("09"))&& txtOTHCT.getText().equals("0Z"))
				{
 				   dosREPORT.writeBytes(padSTRING('L'," ",strDOTLN.length()-86)+"\n");
				    dosREPORT.writeBytes(padSTRING('L'," ",65));
				    dosREPORT.writeBytes(padSTRING('L',"     |",20));
			    	dosREPORT.writeBytes(padSTRING('L',"",14)+"\n");
   
				}
				else if(cmbRPTOP.getSelectedItem().toString().substring(0,2).equals("39"))
				{
				    dosREPORT.writeBytes(padSTRING('L'," ",strDOTLN.length()-86)+"\n");
				    dosREPORT.writeBytes(padSTRING('L'," ",65));
				    dosREPORT.writeBytes(padSTRING('L',"     |",20));
			    	dosREPORT.writeBytes(padSTRING('L',"",14)+"\n");
				}
				else
				{
				    dosREPORT.writeBytes(padSTRING('L',"---------------",strDOTLN.length()-86)+"\n");
				    dosREPORT.writeBytes(padSTRING('L'," ",65));
				    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(dblQTTOT,3)+"     |",20));
			    	dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(dblAMTOT,2)+"",""),14)+"\n");
				}
				
				dosREPORT.writeBytes(padSTRING('L',"|",85)+"\n");
				dosREPORT.writeBytes(padSTRING('L',"|",85)+"\n");
				dosREPORT.writeBytes(padSTRING('L',"|",85)+"\n");
				cl_dat.M_intLINNO_pbst += 5;
                String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().substring(0,2);
                if(L_strCRDTP.equals("09"))
                    L_strCRDTP = txtOTHCT.getText().trim();
				//System.out.println("strDOCRF"+strDOCRF);
				//calTAXVL(L_strCRDTP,strDOCRF);	           //
				// Change  on 28/11/2006 As per NKV instraction
				calTAXVL(L_strCRDTP,strPDCRF);	
				//L_strSQLQRY="Select * from CO_TXDOC where TX_DOCNO ='"+strDOCRF+"' AND ifnull(TX_STSFL,'')<> 'X' ";  //28/11/2006
				// Change  on 28/11/2006 As per NKV instraction
				L_strSQLQRY="Select * from CO_TXDOC where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_DOCNO ='"+strPDCRF+"' AND isnull(TX_STSFL,'')<> 'X' ";  //28/11/2006
				L_rstRSSET=cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET!=null)
				{
				    i=0;
				    double L_dblATXVL=0;
				    double L_dblLTXVL=0;
					while(L_rstRSSET.next())
					{
					    for(int j=0;j<vtrTXADD.size();j++)
						{		
							L_strTAXCD=(String)vtrTXADD.get(j);
							System.out.println("L_strTXADD "+L_strTAXCD);
							L_strTAXFL="TX_"+L_strTAXCD+"FL";
							L_strTAXFL1=nvlSTRVL(L_rstRSSET.getString(L_strTAXFL),"");
							if(!L_strTAXFL1.equals(""))
							{
							   	L_strTAXVL=L_rstRSSET.getString("TX_"+L_strTAXCD+"VL");
								L_strDOCTP=L_rstRSSET.getString("TX_DOCTP");
								if(L_strDOCTP.equals("CRA")||L_strDOCTP.equals("DBA"))
								{
								    dosREPORT.writeBytes(padSTRING('L'," ",16));
									if(i==0)
										dosREPORT.writeBytes(padSTRING('R',"Add  : ",8));
									else
										dosREPORT.writeBytes(padSTRING('R'," ",8));
			                        if(L_strTAXFL1.equals("P"))
			                        {    
			                            L_dblATXVL = ((Double)hstTAXVL.get(L_strTAXCD+"A")).doubleValue();
			                            L_strPRNST = padSTRING('R',(String)hstTAXCD.get(L_strTAXCD+"A")+" @ "+L_strTAXVL+" % ",32)+padSTRING('L',setNumberFormat(L_dblATXVL,2),10);
			                        }
			                        dblATXVL +=L_dblATXVL;
									System.out.println(dblATXVL);
			                      	dosREPORT.writeBytes(padSTRING('R',L_strPRNST,60));
									System.out.println(L_strPRNST);
									dosREPORT.writeBytes("|"+"\n");
									cl_dat.M_intLINNO_pbst += 1;
									i++;
								}
							}
						}
						for(int j=0;j<vtrTXDED.size();j++)
						{		
							L_strTAXCD=(String)vtrTXDED.get(j);
							//System.out.println("L_strTXDEDD "+L_strTAXCD);
							L_strTAXFL="TX_"+L_strTAXCD+"FL";
							L_strTAXFL1=nvlSTRVL(L_rstRSSET.getString(L_strTAXFL),"null");
							if(!L_strTAXFL1.equals("null"))
							{
							    
								L_strTAXVL=L_rstRSSET.getString("TX_"+L_strTAXCD+"VL");
								L_strDOCTP=L_rstRSSET.getString("TX_DOCTP");
								if(L_strDOCTP.equals("CRD")||L_strDOCTP.equals("DBD"))
								{
								    dosREPORT.writeBytes(padSTRING('L'," ",16));
						    		if(z==0)
						    		{
						    		    dosREPORT.writeBytes(padSTRING('L',"| ",69));
						    		    dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstTAXVL.get("ADD")).doubleValue(),2),14)+"\n");
						    		    cl_dat.M_intLINNO_pbst += 1;
						    		    dosREPORT.writeBytes(padSTRING('L'," ",16));
										dosREPORT.writeBytes(padSTRING('R',"Less  : ",8));
						    		}
						    		else
										dosREPORT.writeBytes(padSTRING('R'," ",8));
			                        if(L_strTAXFL1.equals("P"))
			                        {
			                            L_dblLTXVL = ((Double)hstTAXVL.get(L_strTAXCD+"D")).doubleValue();
			                            L_strPRNST = padSTRING('R',(String)hstTAXCD.get(L_strTAXCD+"D")+" @ "+L_strTAXVL+" % ",32)+padSTRING('L',setNumberFormat(L_dblLTXVL,2),10);
			                            dblLTXVL+=L_dblLTXVL;
			                        }
			              			dosREPORT.writeBytes(padSTRING('R',L_strPRNST,60));
									dosREPORT.writeBytes("|"+"\n");
									cl_dat.M_intLINNO_pbst += 1;
									z++;
								}
							}
						}
					}
				}
				if(L_rstRSSET!=null)
					L_rstRSSET.close();
				if(z >0)
				{
				    dosREPORT.writeBytes(padSTRING('L'," ",16));
				    dosREPORT.writeBytes(padSTRING('L',"| ",69));
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(((Double)hstTAXVL.get("LESS")).doubleValue(),2),14)+"\n");
					 cl_dat.M_intLINNO_pbst += 1;
					 System.out.println("z > 0");
				}
			}
			double L_dblPGRVL1=0.00, L_dblATXVL1=0.00, L_dblLTXVL1=0.00, L_dblPNTVL1=0.00;
			// Change  on 28/11/2006 As per NKV instraction  strPDCRF in place of strDOCRF
			L_strSQLQRY="Select sum(PT_PGRVL) PT_PGRVL, sum(PT_ATXVL) PT_ATXVL, sum(PT_LTXVL) PT_LTXVL, sum(PT_PNTVL) PT_PNTVL  from MR_PTTRN  where PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PT_DOCRF ='"+strPDCRF+"' AND isnull(PT_STSFL,'')<> 'X' ";    //28/11/2006
			L_rstRSSET=cl_dat.exeSQLQRY2(L_strSQLQRY);
			if(L_rstRSSET!=null && L_rstRSSET.next())
			{
				L_dblPGRVL1 = Double.parseDouble(L_rstRSSET.getString("PT_PGRVL"));
				L_dblATXVL1 = Double.parseDouble(L_rstRSSET.getString("PT_ATXVL"));
				L_dblLTXVL1 = Double.parseDouble(L_rstRSSET.getString("PT_LTXVL"));
				L_dblPNTVL1 = Double.parseDouble(L_rstRSSET.getString("PT_PNTVL"));
			}
			L_rstRSSET.close();
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			{
                String L_strREMDS = getRMKDS(strPDCRF);
                if(L_strREMDS.length() < 75)
                {
    			   	dosREPORT.writeBytes(padSTRING('R',"Remark :"+L_strREMDS,strDOTLN.length()-16));
					//System.out.println("L_strREMDS if"+L_strREMDS.length() );
    				dosREPORT.writeBytes(padSTRING('R',"|",20)+"\n");
    				//dosREPORT.writeBytes(padSTRING('R',"THE ABOVE VALUES ARE INCLUSIVE OF ALL TAXES,IF ANY.",strDOTLN.length()-16));
    				dosREPORT.writeBytes(padSTRING('R'," ",strDOTLN.length()-16));
    				dosREPORT.writeBytes(padSTRING('R',"|",20)+"\n");
    				cl_dat.M_intLINNO_pbst += 2;
                }
                else
                {
					//System.out.println("L_strREMDS Else "+L_strREMDS.length() );
					//System.out.println("L_strREMDS Else "+L_strREMDS );
    			   	dosREPORT.writeBytes(padSTRING('R',"Remark :"+L_strREMDS.substring(0,75),strDOTLN.length()-16));
					//dosREPORT.writeBytes(padSTRING('R',"Remark :"+L_strREMDS,strDOTLN.length()-16));
					//dosREPORT.writeBytes(padSTRING('R',"Remark :"+L_strREMDS,100));
					
					dosREPORT.writeBytes(padSTRING('R',"|",20)+"\n");
    				//dosREPORT.writeBytes(padSTRING('R',"THE ABOVE VALUES ARE INCLUSIVE OF ALL TAXES,IF ANY.",strDOTLN.length()-16));
    				dosREPORT.writeBytes(padSTRING('R',L_strREMDS.substring(75),strDOTLN.length()-16));
    				dosREPORT.writeBytes(padSTRING('R',"|",20)+"\n");
    				cl_dat.M_intLINNO_pbst += 2;
                }
         	}
			else
			{
				dosREPORT.writeBytes(padSTRING('R',"Remark :THE ABOVE VALUES ARE INCLUSIVE OF ALL TAXES,IF ANY.",strDOTLN.length()-16));
				dosREPORT.writeBytes(padSTRING('R',"|",20)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			dosREPORT.writeBytes(strDOTLN+"\n");
			//System.out.println("ATXVL : "+L_dblATXVL1+" / "+dblATXVL);
			//System.out.println("LTXVL : "+L_dblLTXVL1+" / "+dblLTXVL);
			L_dblPNTVL1 = dblAMTOT + L_dblATXVL1 - L_dblLTXVL1;
			double dblNETVL =0;
			if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			     dblNETVL = dblAMTOT + dblATXVL -dblLTXVL;
			 else   
			     dblNETVL = dblAMTOT + ((Double)hstTAXVL.get("ADD")).doubleValue() -((Double)hstTAXVL.get("LESS")).doubleValue();
			String L_strERRMSG = (java.lang.Math.abs((dblNETVL-L_dblPNTVL1))>4) ? " *ERR* " : "";
			
		//	System.out.println(dblAMTOT);
		//	System.out.println(dblATXVL);
		//	System.out.println(dblLTXVL);
			dblQTTOT =0;
			dblAMTOT =0;
			dblATXVL =0;
			dblLTXVL =0;
			if(!(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			{
			   	dosREPORT.writeBytes(padSTRING('R',"THE ABOVE VALUES ARE INCLUSIVE OF ALL TAXES,IF ANY.",strDOTLN.length()-21));
				dosREPORT.writeBytes(padSTRING('L',"Total ",strDOTLN.length()-95));
				dosREPORT.writeBytes(padSTRING('R',"|",3));
				dosREPORT.writeBytes(padSTRING('L',L_strERRMSG+nvlSTRVL(setNumberFormat(dblNETVL,0),"")+".00",12)+"\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes(padSTRING('R',"Rupees : "+cl_dat.getCURWRD(setNumberFormat(dblNETVL,0)+".00","paise"),strDOTLN.length()-21)+"\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				dosREPORT.writeBytes("\n\n\n\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				cl_dat.M_intLINNO_pbst += 9;
			}
			else
			{				
				dosREPORT.writeBytes(padSTRING('R',"Rupees : "+cl_dat.getCURWRD(setNumberFormat(dblNETVL,2),"paise"),strDOTLN.length()-21));
				dosREPORT.writeBytes(padSTRING('R',"Total ",strDOTLN.length()-95));
				dosREPORT.writeBytes(padSTRING('R',"|",3));
				dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(setNumberFormat(dblNETVL,0),"")+".00",12)+"\n");
				dosREPORT.writeBytes(strDOTLN+"\n");
				cl_dat.M_intLINNO_pbst += 2;
			}
			if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			{
				dosREPORT.writeBytes(padSTRING('R',"Initiator    			  			                 For SUPREME PETROCHEM LTD.",strDOTLN.length()-21)+"\n\n\n");
				cl_dat.M_intLINNO_pbst += 3;
			}
			else if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("02")||(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03"))
			{
				dosREPORT.writeBytes(padSTRING('R',"Initiator ",strDOTLN.length()-21)+"\n\n\n\n\n\n\n\n\n\n");
				cl_dat.M_intLINNO_pbst += 10;
			}
			else 
			{
			    dosREPORT.writeBytes(padSTRING('R',"Initiator    			  			                 For SUPREME PETROCHEM LTD.",strDOTLN.length()-21)+"\n\n\n");
				cl_dat.M_intLINNO_pbst += 3;   
			}
			//09-10-2006
			/*
			else
			{
				dosREPORT.writeBytes(padSTRING('R',"Initiator ",strDOTLN.length()-21)+"\n\n\n\n\n\n\n\n\n\n");
				cl_dat.M_intLINNO_pbst += 10;
			}*/
			if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			{
				dosREPORT.writeBytes(padSTRING('R',"(Marketing Department)    			  		             Authorised Signatory ",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
				dosREPORT.writeBytes(strDOTLN+"\n");
			}
			else if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("02")||(cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("03"))
			{
				dosREPORT.writeBytes(padSTRING('R',"(Marketing Department) ",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
				dosREPORT.writeBytes(strDOTLN+"\n");
			}
			else 
			{
				dosREPORT.writeBytes(padSTRING('R',"(Marketing Department)    			  		             Authorised Signatory ",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst = 0;
				dosREPORT.writeBytes(strDOTLN+"\n");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);
				
			}
			//09-10-2006
		/*
			else if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("39"))
			{
				dosREPORT.writeBytes(padSTRING('R',"(Marketing Department)    			  		             Authorised Signatory ",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
			else
			{
				dosREPORT.writeBytes(padSTRING('R',"(Marketing Department) ",strDOTLN.length()-21)+"\n");
				cl_dat.M_intLINNO_pbst += 1;
			}
		*/
			
			if((cmbRPTOP.getSelectedItem().toString().substring(0,2)).equals("01"))
			{
			    dosREPORT.writeBytes(padSTRING('R',"Registered office: 612,Raheja Chambers,Nariman Point,Mumbai-400021",strDOTLN.length()-5)+"\n");
			    dosREPORT.writeBytes(strDOTLN+"\n");
			}
			else
			{
			    //dosREPORT.writeBytes(strDOTLN+"\n\n");   
			    dosREPORT.writeBytes("\n\n");   
			}
			cl_dat.M_intLINNO_pbst +=4;		
		}
		catch(Exception L_HD)
		{
			setMSG(L_HD,"prnHEADER_L ");
		}
	}
	/**
	 * Method to generate the Report & send to the selected Destination.
	*/
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{
			intRECCT=0;
			if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mr_rpcrn.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mr_rpcrn.doc";
			
			getDATA();
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exescrn.. ");
		}
	}
	public Hashtable calTAXVL(String P_strDOCTP,String P_strDOCNO)
	{
	    double L_dblNETVL =0,L_dblGRSVL =0,L_dblTAXVL =0;
	    double P_dblATXVL =0;
	    double P_dblLTXVL =0;
	    ResultSet L_rstRSSET =null;
	    hstTAXVL = new Hashtable<String,Double>();
	    String L_strTAXSQ ="";
	    String L_strTAXCD="",L_strDOCTP="",L_strTAXFL1="",L_strTAXFL="",L_strTAXVL="";
	    StringTokenizer L_strTKN = new StringTokenizer("|");
	    try
	    {
	       M_strSQLQRY = " SELECT sum(isnull(PT_PGRVL,0))L_GRSVL from MR_PTTRN WHERE PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and PT_CRDTP ='"+P_strDOCTP+"' AND PT_DOCRF ='"+P_strDOCNO +"' AND isnull(PT_STSFL,'') <> 'X'";
	 	    L_rstRSSET=cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET != null)
			{
			    if(L_rstRSSET.next())
			        L_dblGRSVL = L_rstRSSET.getDouble("L_GRSVL");
			    //System.out.println("Gross value "+ L_dblGRSVL);
			    L_rstRSSET.close();
			}
			L_dblNETVL = L_dblGRSVL;
			String L_strSQLQRY="Select * from CO_TXDOC where  TX_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and TX_DOCNO ='"+P_strDOCNO+"' AND isnull(TX_STSFL,'')<> 'X' ";
			L_rstRSSET=cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET!=null)
			{
				while(L_rstRSSET.next())
				{
				    L_strDOCTP=L_rstRSSET.getString("TX_DOCTP");
				    if(L_strDOCTP.equals("CRA")||L_strDOCTP.equals("DBA"))
				    {
    				    for(int j=0;j<vtrTXADD.size();j++)
    					{		
    						L_strTAXCD=(String)vtrTXADD.get(j);
    						L_strTAXFL="TX_"+L_strTAXCD+"FL";
    						L_strTAXFL1=nvlSTRVL(L_rstRSSET.getString(L_strTAXFL),"");
    						if(!L_strTAXFL1.equals(""))
    						{
    							L_strTAXVL=L_rstRSSET.getString("TX_"+L_strTAXCD+"VL");
    							
    							if(L_strDOCTP.equals("CRA")||L_strDOCTP.equals("DBA"))
    							{
    							    L_strTAXSQ = (String)hstTAXSQ.get(L_strTAXCD+"A");
    		                        if(L_strTAXSQ.equals("RUN00"))
    		                        {
    		                            if(L_strTAXFL1.equals("P"))
    		                                L_dblTAXVL =  L_dblNETVL * Double.parseDouble(L_strTAXVL)/100;
    		                            else
    		                                L_dblTAXVL =  Double.parseDouble(L_strTAXVL);    
    		                            P_dblATXVL +=L_dblTAXVL;
    		                            hstTAXVL.put(L_strTAXCD+"A",new Double(L_dblTAXVL));  
    		                            L_dblNETVL += L_dblTAXVL;
    		                        }
    		                        else
    		                        {
							// commented on 01/04/07 after higher ed cess changes
    		                           // if(L_strTAXSQ.substring(3).equals("00"))
    		                            //{
        		                            if(L_strTAXFL1.equals("P"))
        		                            {  
        		                                L_dblTAXVL = (((Double)hstTAXVL.get(L_strTAXSQ.substring(0,3)+"A")).doubleValue()* Double.parseDouble(L_strTAXVL)/100);
        		                            }
        		                            hstTAXVL.put(L_strTAXCD+"A",new Double(L_dblTAXVL));  
        		                            P_dblATXVL +=L_dblTAXVL;
        		                            L_dblNETVL += L_dblTAXVL;
        		                     //   }
    		                           // else
    		                           // {
    		                           
    		                          //  }
    		                        }
    		                       	cl_dat.M_intLINNO_pbst += 1;
    								i++;
    							}
    						}
    					}// end of loop for vtrADD
				    }
				   	else if(L_strDOCTP.equals("CRD")||L_strDOCTP.equals("DBD"))
				   	{
    				   	for(int j=0;j<vtrTXDED.size();j++)
    					{		
    						L_strTAXCD=(String)vtrTXDED.get(j);
    						L_strTAXFL="TX_"+L_strTAXCD+"FL";
    						L_strTAXFL1=nvlSTRVL(L_rstRSSET.getString(L_strTAXFL),"");
    						if(!L_strTAXFL1.equals(""))
    						{
    							L_strTAXVL=L_rstRSSET.getString("TX_"+L_strTAXCD+"VL");
    							L_strDOCTP=L_rstRSSET.getString("TX_DOCTP");
						      L_strTAXSQ = (String)hstTAXSQ.get(L_strTAXCD+"D");
		                        //if(L_strTAXSQ.substring(0,3).equals("RUN"))
		                        if(L_strTAXSQ.equals("RUN00"))
		                        {
		                            if(L_strTAXFL1.equals("P"))
		                                L_dblTAXVL =  L_dblNETVL * Double.parseDouble(L_strTAXVL)/100;
		                            else
		                                L_dblTAXVL =  Double.parseDouble(L_strTAXVL);    
		                            hstTAXVL.put(L_strTAXCD+"D",new Double(L_dblTAXVL));  
		                            P_dblLTXVL +=L_dblTAXVL;
		                            L_dblNETVL -= L_dblTAXVL;
		                        }
		                        else
		                        {
		                            if(L_strTAXSQ.substring(3).equals("00"))
    		                        {
    		                           
    		                            if(L_strTAXFL1.equals("P"))
    		                            {  
    		                                L_dblTAXVL = (((Double)hstTAXVL.get(L_strTAXSQ.substring(0,3)+"D")).doubleValue()* Double.parseDouble(L_strTAXVL)/100);
    		                            }
    		                            hstTAXVL.put(L_strTAXCD+"D",new Double(L_dblTAXVL));  
    		                            P_dblLTXVL +=L_dblTAXVL;
    		                            L_dblNETVL -= L_dblTAXVL;
    		                            //System.out.println("Net value after "+L_strTAXCD +" :"+L_dblNETVL); 
    		                        }
                                    else
                                    {
                                        if(L_strTAXFL1.equals("P"))
    		                            {  
    		                                L_dblTAXVL = ((Double)hstTAXVL.get(L_strTAXSQ.substring(0,3)+"D")).doubleValue()+((Double)hstTAXVL.get(vtrTXDED.elementAt(vtrTXDED.indexOf(L_strTAXSQ.substring(0,3))+1).toString()+"D")).doubleValue();
    		                                //System.out.println(L_dblTAXVL);
    		                                L_dblTAXVL = L_dblTAXVL * (Double.parseDouble(L_strTAXVL)/100);
    		                            }
    		                            //System.out.println(L_strTAXCD +" AAA : "+L_dblTAXVL); 
    		                            hstTAXVL.put(L_strTAXCD+"D",new Double(L_dblTAXVL));  
    		                            P_dblLTXVL +=L_dblTAXVL;
    		                            L_dblNETVL -= L_dblTAXVL;
    		                            //System.out.println("Net value after "+L_strTAXCD +" :"+L_dblNETVL);    
                                    }
                                }
		                       	cl_dat.M_intLINNO_pbst += 1;
								i++;
    						}
    					}// end of loop for vtrADD
				   	}
				}
				hstTAXVL.put("ADD",new Double(P_dblATXVL));
				hstTAXVL.put("LESS",new Double(P_dblLTXVL));
			}
			if(L_rstRSSET!=null)
				L_rstRSSET.close();
		}
	    catch(Exception L_E)
	    {
	        System.out.println(L_E.toString());
	    }
	    return hstTAXVL;
	}
	private String getRMKDS(String P_strDOCNO)
	{
	    String L_strREMDS ="";
	    ResultSet L_rstRMMST;
	    try
	    {
	        M_strSQLQRY = "Select RM_REMDS,RM_STSFL from MR_RMMST WHERE RM_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and ";
            if(cmbRPTOP.getSelectedItem().toString().substring(0,1).equals("0"))			
                M_strSQLQRY += " RM_TRNTP = 'CR'";
            else if(cmbRPTOP.getSelectedItem().toString().substring(0,1).equals("3"))			
                M_strSQLQRY += " RM_TRNTP = 'DB'";
        //    M_strSQLQRY += " and RM_MKTTP = '" + P_strMKTTP + "'";
			M_strSQLQRY += " and RM_DOCNO = '" + P_strDOCNO + "' and  isnull(RM_STSFL,'') <> 'X'";
			L_rstRMMST = cl_dat.exeSQLQRY2(M_strSQLQRY );
			//txtREMDS.setText("");
			if(L_rstRMMST != null)
			{
				if(L_rstRMMST.next())
				{
					if(!nvlSTRVL(L_rstRMMST.getString("RM_STSFL"),"").equals("X"))
						L_strREMDS = nvlSTRVL(L_rstRMMST.getString("RM_REMDS"),"");
					else
						L_strREMDS ="";
				}
				L_rstRMMST.close();
			}
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"getRMKDS");
	    }
	    return L_strREMDS;
	}
void prnDSTCM()
{
    String strSRTVL1="";
	String strACSVL1="";
	String strTDSVL1="";
	String strSCHVL1="";	
	String strLCSVL1="";
	String strNETVL1="";
	String L_strFMDAT = txtFMDAT.getText().trim();
   	String L_strTODAT = txtTODAT.getText().trim();
   	String L_strCRDTP = cmbRPTOP.getSelectedItem().toString().trim();
   	String strPRTCD ="",strPDSRCD="";
	String strCRDTP = L_strCRDTP.substring(0,2);
	String strCRDTP1 = L_strCRDTP.substring(2,L_strCRDTP.length()).toString().trim();
	 
    try
    {
		M_strSQLQRY = "SELECT a.PT_DOCRF PT_DOCRF,a.pt_rptdt PT_DOCDT,a.pt_accrf PT_ACCRF,A.PT_ACCDT PT_ACCDT, SUBSTRING(A.PT_SBSCD,3,2)L_SALTP,SUBSTRING(A.PT_PRDCD,1,4) L_PRDCT,A.PT_TRNRT PT_TRNRT,A.PT_PRTTP PT_PRTTP,A.PT_PRTCD PT_PRTCD"
        +",SUM(A.PT_INVQT)L_INVQT,SUM(A.PT_PGRVL)L_PGRVL FROM MR_PTTRN A WHERE a.PT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and a.PT_STSFL <> 'X' ";
       
		if(rdbDATWS.isSelected())
		{
			M_strSQLQRY +=" and A.PT_DOCDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strFMDAT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strTODAT))+"'";
		}
		if(rdbDOCWS.isSelected())
		{
			M_strSQLQRY +=" and A.PT_DOCRF between '"+txtFMDOC.getText().trim()+"' and '"+txtTODOC.getText().trim()+"'";
		}
		M_strSQLQRY +="  and A.PT_CRDTP ='"+strCRDTP+"' ";
	
		//if(strPRTCD.length()==5)
		//	M_strSQLQRY += " and A.PT_PRTTP = 'D' and A.PT_PRTCD = '"+strPRTCD+"' ";
			
		M_strSQLQRY += " GROUP BY A.PT_DOCRF,A.PT_rptDT,A.PT_ACCRF,A.PT_ACCDT,SUBSTRING(A.PT_SBSCD,3,2),A.PT_PRTTP,SUBSTRING(A.PT_PRDCD,1,4),A.PT_TRNRT,A.PT_PRTCD ORDER BY a.PT_DOCRF,SUBSTRING(a.PT_SBSCD,3,2),SUBSTRING(a.PT_PRDCD,1,4) ";
		//M_strSQLQRY +=" order by PT_CRDTP,B.PT_DOCRF,IVT_INDNO,B.PT_INVNO";
		
		System.out.println("M_strSQLQRY>>>>>>>>>>>"+M_strSQLQRY);
		strPDCRF="";
		strPIVNO="";
		int L_CNT =0;
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET != null)
		{ 
			while(M_rstRSSET.next())
			{
				intRECCT=1;
				strDSRCD = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
				strPRTTP = nvlSTRVL(M_rstRSSET.getString("PT_PRTTP"),"");
				if(!strDSRCD.equals(strPDSRCD))
				{
				    //getPTDTL("D",strDSRCD) SATYA;
					getPTDTL(strPRTTP,strDSRCD);
				    strPDSRCD = strDSRCD;
				}
				strDSRNM = strPRTNM;
				strGRPCD = nvlSTRVL(M_rstRSSET.getString("PT_PRTCD"),"");
				strDOCRF = nvlSTRVL(M_rstRSSET.getString("PT_DOCRF"),"");
				L_datTEMP = M_rstRSSET.getDate("PT_DOCDT");
				if(L_datTEMP !=null)
					strDOCDT = nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),"");
			    else
			        strDOCDT ="";
				strACCRF = nvlSTRVL(M_rstRSSET.getString("PT_ACCRF"),"");//if value is available,it display otherwise not display
				L_datTEMP = M_rstRSSET.getDate("PT_ACCDT");
				if(L_datTEMP !=null)
					strACCDT = nvlSTRVL(M_fmtLCDAT.format(L_datTEMP),"");
				else strACCDT ="";
				strACCDT ="";
				strINVNO = nvlSTRVL(M_rstRSSET.getString("L_SALTP"),"");
				strPRDCT = nvlSTRVL(M_rstRSSET.getString("L_PRDCT"),"");
				strCRDRT = nvlSTRVL(M_rstRSSET.getString("PT_TRNRT"),"");
				strINVQT = nvlSTRVL(M_rstRSSET.getString("L_INVQT"),"");
				strCRDVL = nvlSTRVL(M_rstRSSET.getString("L_PGRVL"),"");
				strATXVL = "";//nvlSTRVL(M_rstRSSET.getString("PT_ATXVL"),"");
				strLTXVL = "";//nvlSTRVL(M_rstRSSET.getString("PT_LTXVL"),"");
				if(!strPDCRF.equals(strDOCRF))
				{
					if(L_CNT >0)
					{
						prnHEADER_L();
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes("\n");
				    if(cl_dat.M_intLINNO_pbst >= 62)
					{
						cl_dat.M_intLINNO_pbst = 0;
		    			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
					}
					if(cl_dat.M_intLINNO_pbst <= 31)
						dosREPORT.writeBytes("\n");
					prnHEADER_U();
					prnHEADER_M1();
					strPDCRF = strDOCRF;
				}
				else
				{
					prnHEADER_M1();
				}
				strPIVNO = strINVNO;
				strSRTVL=strSRTVL1;
				strACSVL=strACSVL1;
				strTDSVL=strTDSVL1;
				strSCHVL=strSCHVL1;	
				strLCSVL=strLCSVL1;
				strNETVL=strNETVL1;
				L_CNT++;
			}
			if(L_CNT >0)
			prnHEADER_L();
		}
		if(M_rstRSSET!=null)
			M_rstRSSET.close();
		}
		catch(Exception L_E)
		{
		    setMSG(L_E,"prnDCTCM");
		}
	}
	private void getPTDTL(String P_strPRTTP,String P_strPRTCD)
	{
		try
		{
			ResultSet L_rstRSSET=cl_dat.exeSQLQRY3("Select PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04 from CO_PTMST where PT_PRTTP='"+P_strPRTTP+"' and PT_PRTCD='"+P_strPRTCD+"'");
			if(L_rstRSSET!=null)
			if(L_rstRSSET.next())
			{
				strADR01 = nvlSTRVL(L_rstRSSET.getString("PT_ADR01"),"");
				strADR02 = nvlSTRVL(L_rstRSSET.getString("PT_ADR02"),"");
				strADR03 = nvlSTRVL(L_rstRSSET.getString("PT_ADR03"),"");
				strADR04 = nvlSTRVL(L_rstRSSET.getString("PT_ADR04"),"");
				strPRTNM = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");
			}
			L_rstRSSET.close(); 
		}
		catch(Exception e)
		{
			setMSG(e,"getPTDTL");
		}
	}
}
//SELECT PT_DOCRF,SUBSTRING(PT_PRDCD,1,4),PT_TRNRT,SUM(PT_INVQT),SUM(PT_pGRVL) FROM MR_PTTRN WHERE PT_CRDTP ='03' AND PT_DOCRF ='70300001' GROUP BY PT_DOCRF,SUBSTRING(PT_PRDCD,1,4),PT_TRNRT;
