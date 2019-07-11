/**
System Name   : Laboratoty Information Management System
Program Name  : Wet Analysis Report
Program Desc. : Report for printing the Water test details
Author        : Mr.S.R.Mehesare
Date          : 8 june 2005
Version       : LIMS V2.0.0

Modificaitons 
Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : 
*/
import java.sql.ResultSet;import java.sql.Timestamp;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Wet Analysis Report

Purpose : Report for printing the Water test details

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
QC_RMMST       RM_QCATP,RM_TSTTP,RM_TSTNO             #        #       #        #
QC_WTTRN       WTT_QCATP,WTT_TSTTP,WTT_TSTNO,WTT_TSTDT,
               WTT_WTRTP                                               #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtDATE     PS_TSTDT       QC_PSMST      DATE          Test Date 
txtRMRK1   RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 1
txtRMRK2   RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 2
txtRMRK3   RM_REMDS       QC_RMMST      VARCHAR(50)   Remark 3
--------------------------------------------------------------------------------------
<B>
Logic</B>
Each grade (finshed product) has some proparties. & these proparties changes as per 
new standards adopted. 
These proparties are varing from grade to grade.
System manages proparties of each grade & makes Latest propatries available.
According to the new standards, some new properties are added and some unwanted
are removed for specific grade.
Hence to generate report every time list of proparties are generated dynamically as :-
   1) Latest property list is fetched from the database associated with perticular grade .
   2) These Properties are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    i.e."SMT_"+ Vector.elementAt(i)+"VL" as SMT_COLVL, SMT_TOLVL
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

  For Remark1,2 & 3 are taken from QC_RMMST for conditations
    1) RM_QCATP = given QCA Type here it is "01"
    2) AND RM_TSTTP like last three charectores of the report file name.
    3) AND RM_TSTNO = report date & time 	

<I><B>Conditions Give in Query:</b>
    Quality parameter codes List is taken from CO_CDTRN & Data is taken from QC_WTTRN 
    for following condition
  I)EFFLUENT ANALYSIS 
     Test Details such as QCA Type, Test Type & Test Number are taken from QC_WTTRN
       1) WTT_TSTDT = given date range.
       2) AND WTT_TSTTP = '0401'
       3) AND WTT_STSFL <> 'X'";
     Quality Parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = 'QCXXEFA'
       3) AND CMT_MODLS like % 0401 %.
     Quality Parameter Range is taken from CO_QPMST for condiations
       1) RS_QCATP = '01'
       2) AND QP_PRDCD = '6800000001'";
     Quality Parameter Details are taken from QC_WTTRN for condiations
       1) RS_QCATP = '01'
       2) WTT_TSTTP = '0401'
       3) And WTT_TSTNO = given test number.
       4) And WTT_WTRTP = Quality parameter Code.
       4) AND RS_TSTDT between the last 24 hours of the given date.4			 			
			
  II)UTILITY WATER ANALYSIS           
     Quality Parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = 'QCXXUWA'
       3) AND CMT_MODLS like % 0403 %.           
     Test Details are taken from QC_WTTRN for condiations
       At first Test Details are fetched for condiations
       1) WTT_TSTDT between Given Date Range
       2) AND WTT_TSTTP = '0403'
       3) AND WTT_STSFL <> 'X'";
     And from above Test Details Quality Parameter Details are taken from QC_WTTRN for condiations
      1) WTT_QCATP = QCA type fetched from QC_WTTRN
      2) AND WTT_TSTTP = Test Type fetched from QC_WTTRN
      3) AND WTT_TSTNO = Test Number fetched from QC_WTTRN 
      4) AND WTT_WTRTP = Quality Parameter Code from Vector
      
  III) EXTRA WATER ANALYSIS
     Quality Parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = ''QCXXEWA''
       3) AND CMT_MODLS like % 0402 %.
     Test Details are taken from QC_WTTRN for condiations
       At first Test Details are fetched for condiations
       1) WTT_TSTDT between Given Date Range
       2) AND WTT_TSTTP = '0402'
       3) AND WTT_STSFL <> 'X'";
     And from above Test Details Quality Parameter Details are taken from QC_WTTRN for condiations
      1) WTT_QCATP = QCA type fetched from QC_WTTRN
      2) AND WTT_TSTTP = Test Type fetched from QC_WTTRN
      3) AND WTT_TSTNO = Test Number fetched from QC_WTTRN 
      4) AND WTT_WTRTP = Quality Parameter Code from Vector
<B>Validations :</B>
    - Date Time must be valid. i.e.smaller than current date.
</I> */

public class qc_rpwar extends cl_rbase
{											/** JTextField to enter & display Date.*/
	private JTextField txtDATE;				/** JTextField to enter & display Remark1.*/
	private JTextField txtRMRK1;			/** JTextField to enter & display Remark2.*/
	private JTextField txtRMRK2;			/** JTextField to enter & display Remark3.*/
	private JTextField txtRMRK3;			/** FileOutputStream for the generated Report File Handeling.*/		
	private FileOutputStream fosREPORT ;    /** DataOutputStream object to store the data as a Stream */
    private DataOutputStream dosREPORT ;	/** String variable for generated Report File Name.*/
	private String strFILNM;				
											/** String variable for Remark 1.*/
	private String strRMRK1="";				/** String variable for Remark 2.*/
	private String strRMRK2="";				/** String variable for Remark 3.*/
	private String strRMRK3="";				/** String variable to Represent the Time.*/
	private String strTIME = "07:00";		/**	String variable for one day smaller date than given date.*/
	private String strFMDAT;				/**	String variable for given date & time.*/
	private String strTODAT;				/** Integer variable to count the number of records fetched.*/
	private int intRECCT;					/** Vector to store the Quality Propary Description.*/
	private Vector<String> vtrQPRDS = new Vector<String>();	/** Vector to store Quality proparty releated values.*/
	private Vector<String> vtrQPRLS = new Vector<String>();	/** Vector to store the water analysis proparty code.*/
	private Vector<String> vtrWACOD = new Vector<String>();	/** Vector to store the water analysis proparty Description.*/
	private Vector<String> vtrWADES = new Vector<String>();	/** Vector to store the unit of measurement of the quality proparty.*/	
	private Vector<String> vtrQPUOM = new Vector<String>();	/** Hashtable to store quality parameter range.*/	
	private Hashtable<String,String> hstQPRNG = new Hashtable<String,String>();	/** String variable for QCA type.*/	
	private String strQCATP ="";			/** String variable for doted Line.*/
	private String strDOTLN="-------------------------------------------------------------------------------------------";	
											/** String variable for Test Time.*/
	private String strTSTTM;				/** String variable for Test Time.*/
	private String strTSTM1;				/** String variable for Test Time.*/
	private String strTSTM2;				/** String variable for quality parameter values.*/
	private String strUMVAL="";				/** Boolean variable to manage the selection of query.*/
	private boolean flgRMXSTS = false;	
	private String strISOD1,strISOD2,strISOD3;
	private Hashtable<String,String> hstQPRCD;
	
	qc_rpwar()
	{
		super(2);
		try
		{
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Date"),4,3,1,.7,this,'R');
			add(txtDATE = new TxtDate(),4,4,1,1,this,'L');			
			add(new JLabel("Remark1"),5,3,1,.7,this,'R');
			add(txtRMRK1 = new JTextField(),5,4,1,3.5,this,'L');						
			add(new JLabel("Remark2"),6,3,1,.7,this,'R');
			add(txtRMRK2 = new JTextField(),6,4,1,3.5,this,'L');			
			add(new JLabel("Remark3"),7,3,1,.7,this,'R');
			add(txtRMRK3 = new JTextField(),7,4,1,3.5,this,'L');						
			
			vtrQPRDS = new  Vector<String>();
			vtrQPRLS = new Vector<String>();
			vtrQPUOM = new Vector<String>();
			vtrWACOD = new  Vector<String>();
			vtrWADES = new Vector<String>();			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);		
						
			M_strSQLQRY = "Select QS_QPRCD,QS_QPRDS,QS_SHRDS,QS_UOMCD from CO_QSMST where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(QS_STSFL,'') <> 'X'";
	     	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			hstQPRCD = new Hashtable<String,String>();
			if(M_rstRSSET !=null)
			{				
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{							
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strTEMP.equals(""))																		
						hstQPRCD.put(L_strTEMP,nvlSTRVL(M_rstRSSET.getString("QS_QPRDS"),"")+"|"+nvlSTRVL(M_rstRSSET.getString("QS_SHRDS"),"")+"|"+nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));					
				}
				M_rstRSSET.close();
			}					
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Method to enable & disable the Components according to requriements
	 * @param P_flgSTAT boolean argument to pass the state of the variable.
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)		
			cl_dat.M_btnSAVE_pbst.setEnabled(false);					
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter the Date..",'N');				
				setENBL(true);				
				txtDATE.requestFocus();
				if(txtDATE.getText().trim().length() == 0)
				{
					txtDATE.setText(cl_dat.M_strLOGDT_pbst);
					txtRMRK1.setText("");
					txtRMRK2.setText("");
					txtRMRK3.setText("");					
				}
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
		{
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			txtDATE.setEnabled(true);
			txtDATE.setText(cl_dat.M_strLOGDT_pbst);
			txtDATE.requestFocus();
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;																		
		}
		else if(M_objSOURC == txtDATE)
		{
			try
			{				
				if (txtDATE.getText().trim().length()>0)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					txtRMRK1.setText("");
					txtRMRK2.setText("");
					txtRMRK3.setText("");
					strRMRK1="";
					strRMRK2="";
					strRMRK3="";										
					strQCATP = M_strSBSCD.substring(2,4);
					
					M_strSQLQRY ="Select * from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"
						+ strQCATP.trim()+ "' AND RM_TSTTP like " + "'WAR%'"
						+ " AND RM_TSTNO = " + "'"+ txtDATE.getText().trim().substring(0,2).trim()+txtDATE.getText().trim().substring(3,5).trim()+txtDATE.getText().trim().substring(6).trim()+"'";					
			
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{					
						String L_strTSTTP="";
						while(M_rstRSSET.next())
						{
							L_strTSTTP = nvlSTRVL(M_rstRSSET.getString("RM_TSTTP"),"");
							if(!L_strTSTTP.equals(""))
							{	
								if(L_strTSTTP.equals("WAR1"))
								{
									strRMRK1 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK1.setText(strRMRK1);
								}
								else if(L_strTSTTP.equals("WAR2"))
								{
									strRMRK2 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK2.setText(strRMRK2);
								}
								else if(L_strTSTTP.equals("WAR3"))
								{
									strRMRK3 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK3.setText(strRMRK3);
								}
							}
						}
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					setMSG("Enter Remark & Prass Enter Key..",'N');
					txtRMRK1.requestFocus();
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					txtDATE.setEnabled(false);
				}
				else			
					setMSG("Please Enter Date to generate the Report..",'N');
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"M_objSOURC == txtDATE");
			}
		}
		else if(M_objSOURC == txtRMRK1)
		{
			setMSG("Enter Remark2 & Prass Enter Key..",'N');
			txtRMRK2.requestFocus();
		}
		else if(M_objSOURC == txtRMRK2)
		{
			setMSG("Enter Remark 3 & Prass Enter Key..",'N');
			txtRMRK3.requestFocus();
		}
		else if(M_objSOURC == txtRMRK3)		
			cl_dat.M_btnSAVE_pbst.requestFocus();				
	}
	
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		cl_dat.M_flgLCUPD_pbst = true;
		if(!vldDATA())
			return;					
		try
		{
			if(M_rdbHTML.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rpwar.html";
			else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpwar.doc";							
			getDATA();			
		/*	if(intRECCT == 0)
			{
				setMSG("Data not found for the given Inputs..",'E');
				return;
			}*/
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"WET ANALYSIS REPORT"," ");
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
			intRECCT = 0;			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
				setMSG("For HTML Report Printing Please insert 120 column Page..",'N');
			    dosREPORT.writeBytes("<HTML><HEAD><Title>WET ANALYSIS REPORT </title> </HEAD> <BODY><P><PRE style =\" font-size : 11 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}	
			try
			{	
				strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim()+" 07:00"));						
				M_calLOCAL.setTime(M_fmtLCDAT.parse(txtDATE.getText().trim()));
				M_calLOCAL.add(java.util.Calendar.DATE,-1);																       			
				strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(M_fmtLCDAT.format(M_calLOCAL.getTime()) +" 07:00"));								
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX, "Date Manipulation at actionPerformed");				
			}	
			
			prnHEADER();
			
			getRPTTM();
						
			getTSTDET("QCXXEFA");
			
			getWATCOD("0401");
			
			getQPRRNG();
			
			getEFWANY();
			dosREPORT.writeBytes("Remark : BIO - ASSAY (90% survival after 96 Hrs)");
			
			getTSTDET("QCXXUWA");
			
			getWATCOD("0403");
			
			getUWANY();
			
			getTSTDET("QCXXEWA");
			
			getWATCOD("0402");
			
			getEXWANY();
						
			dosREPORT.writeBytes("\n"+"Remarks : ");			
			if(txtRMRK1.getText().length()> 0)			
				dosREPORT.writeBytes(txtRMRK1.getText().trim()+"\n");						
			if( !strRMRK1.equals(txtRMRK1.getText().trim()))
			{								
				if(strRMRK1.equals(""))
					insRPTRM("QC_RPWAR",txtDATE.getText().trim(),"1",txtRMRK1.getText().trim(),strRMRK1);
				else
					updRPTRM("QC_RPWAR",txtDATE.getText().trim(),"1",txtRMRK1.getText().trim(),strRMRK1);
				if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeDBCMT("updRPTRM");
			}			
			
			if(txtRMRK2.getText().length()> 0)							
				dosREPORT.writeBytes(padSTRING('L',"",10)+txtRMRK2.getText().trim()+"\n");									
			if( ! strRMRK2.equals(txtRMRK2.getText().trim()))
			{									
				if(strRMRK2.equals(""))
					insRPTRM("QC_RPWAR",txtDATE.getText().trim(),"2",txtRMRK2.getText().trim(),strRMRK2);
				else
					updRPTRM("QC_RPWAR",txtDATE.getText().trim(),"2",txtRMRK2.getText().trim(),strRMRK2);
				if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeDBCMT("updRPTRM");
			}			
			
			if(txtRMRK3.getText().length()> 0)			
				dosREPORT.writeBytes(padSTRING('L'," ",10) + txtRMRK3.getText().trim());								
			if(!strRMRK3.equals(txtRMRK3.getText().trim()))
			{														
				if(strRMRK3.equals(""))
					insRPTRM("QC_RPWAR",txtDATE.getText().trim(),"3",txtRMRK3.getText().trim(),strRMRK1);				
				else
					updRPTRM("QC_RPWAR",txtDATE.getText().trim(),"3",txtRMRK3.getText().trim(),strRMRK1);				
				if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeDBCMT("updRPTRM");
			}									
			dosREPORT.writeBytes("\n"+padSTRING('L',"HOD(QCA)",85));			
			
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
			if(txtDATE.getText().length()==0)			
			{
				setMSG("Please Enter Date to generate the Report.. ",'E');
				txtDATE.requestFocus();			
				return false;
			}
			if (M_fmtLCDAT.parse(txtDATE.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			
			{			    
				setMSG("Date must be smaller than current Date.. ",'E');								
				txtDATE.requestFocus();
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
	 * Method to Generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{		
		try
		{								
			cl_dat.M_intLINNO_pbst = 13;
			cl_dat.M_PAGENO += 1;	
			if(cl_dat.M_PAGENO == 1)
			{				
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
					+ " AND CMT_CGSTP ='QCXXWAR' AND isnull(CMT_STSFL,'') <> 'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
				if(M_rstRSSET != null)
				{
					String L_strTEMP="";
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
						if(!L_strTEMP.equals(""))
						{
							if(L_strTEMP.equals("DOC1"))
								strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals("DOC2"))
								strISOD2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
							else if(L_strTEMP.equals("DOC3"))
								strISOD3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						}					
					}
					M_rstRSSET.close();
				}	
			}
			dosREPORT.writeBytes(padSTRING('L',"-----------------------------------",91)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISOD1,91) + "\n");			
			dosREPORT.writeBytes(padSTRING('L',strISOD2,91) + "\n");			
			dosREPORT.writeBytes(padSTRING('L',strISOD3,91) + "\n");
			dosREPORT.writeBytes(padSTRING('L',"-----------------------------------",91)+"\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,67) + "\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			//dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes(padSTRING('R',"WET ANALYSIS REPORT",67)+ "\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			//dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");								
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
			
	/**
	 * Method to get the remarks from the database.
	 * @param P_strRPTNM String argument to pass Report Name.
	 * @param P_strRPTDT String argument to pass Report Date.
	 * @param P_strRMRK String argumnt to pass Remark Description.
	 */
	/*public boolean getRPTRM(String P_strRPTNM,String P_strRPTDT,String P_strRMRK,String P_strRMSRL)
	{
		// For getting the remarks for a date.P_strRMRK should be blank.
		// For getting the individual remarks for a date.P_strRMRK should be given.	 
		// For Saving the Remarks for daily Reports.
		int L_intCOUNT =0;
		String L_strRMRK ="";
		try
		{
			strQCATP = M_strSBSCD.substring(2,4);
//			hstRMKDS.clear();
			M_strSQLQRY ="Select * from QC_RMMST where RM_QCATP = " ;
			M_strSQLQRY +="'"+ strQCATP.trim()+ "'";
			M_strSQLQRY += " AND RM_TSTTP like " + "'"+P_strRPTNM.substring(5).trim()+ "%'";
			M_strSQLQRY += " AND RM_TSTNO = " + "'"+ P_strRPTDT.substring(0,2).trim()+P_strRPTDT.substring(3,5).trim()+P_strRPTDT.substring(6).trim()+"'";			
			if(P_strRMSRL.equals(""))
				M_strSQLQRY += " AND RM_TSTTP like " + "'"+P_strRPTNM.substring(5).trim()+ "%'";	
			else			
				M_strSQLQRY += " AND RM_TSTTP ='" + P_strRPTNM.substring(5).trim()+P_strRMSRL.trim()+"'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strRMRK = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
					if(L_strRMRK !=null)
					{
						hstRMKDS.put(nvlSTRVL(M_rstRSSET.getString("RM_TSTTP"),""),L_strRMRK.trim());		
						L_intCOUNT++;
					}
				}
			}			
			if(L_intCOUNT >0)
				return true;
			else
				return false;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPTRM");		 
		}	 
		return false;
	}*/
	/**
	 * Method to get Report date Time.
	 */
	public void getRPTTM()
	{
		try
		{
			String L_strTSTTP="";
			strTSTTM ="";
			strTSTM1 ="";
			strTSTM2= "";
			Timestamp L_tmsTSTDT;
			strQCATP = M_strSBSCD.substring(2,4);
			M_strSQLQRY = "select distinct WTT_TSTTP,WTT_TSTDT from QC_WTTRN where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_QCATP =" + "'"+strQCATP +"'";
			M_strSQLQRY += " AND WTT_TSTDT between '" ;
			M_strSQLQRY += strFMDAT + "' AND '" + strTODAT +"'";
			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)			
			{
				while(M_rstRSSET.next())
				{
					L_strTSTTP = nvlSTRVL(M_rstRSSET.getString("WTT_TSTTP"),"");					
					L_tmsTSTDT = M_rstRSSET.getTimestamp("WTT_TSTDT");
					if(L_tmsTSTDT!= null)
						strTSTTM = M_fmtLCDTM.format(L_tmsTSTDT);						
					if(!L_strTSTTP.equals(""))
					{
						if(L_strTSTTP.equals("0401"))
						{
							if((strTSTTM !=null) && (strTSTTM.trim().length() > 15))
								strTSTM1 = strTSTTM.substring(11,16);
						}
						else if(L_strTSTTP.equals("0403"))
						{
							if((strTSTTM !=null) && (strTSTTM.trim().length() > 15))								
								strTSTM2 = strTSTTM.substring(11,16);
						}
					}
				}			
				M_rstRSSET.close();
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getRPTTM");
		}
	}
	/**
	 * Method to get the effluent water anaysis details.
	 */
	private void getEFWANY()
	{
		try
		{		
			ResultSet L_rstRSSET ;
			String L_strTSTDTE = "";
			String L_strSQLQRY ="";
			String L_strQPRCD = "";
			
			StringBuffer L_stbQPRNM;
			StringBuffer L_stbQPUOM; 			
			String L_arrUMVAL [][] = new String[vtrQPRLS.size()][vtrWACOD.size()];			
		    M_strSQLQRY = "Select distinct WTT_QCATP,WTT_TSTTP,WTT_TSTNO From QC_WTTRN Where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_TSTDT ";
            M_strSQLQRY += " Between '" + strFMDAT + "' And '"+ strTODAT + "' AND WTT_TSTTP = '0401'";
            M_strSQLQRY += " AND WTT_STSFL <> 'X'";
	     	M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{			
					L_strTSTDTE="";							
					for(int i=0;i<vtrWACOD.size();i++)
					{
						 L_strSQLQRY = "Select * FROM QC_WTTRN where"
							+ " WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_QCATP = '" + nvlSTRVL(M_rstRSSET.getString("WTT_QCATP"),"").trim() 
							+ "' And WTT_TSTTP ='" + nvlSTRVL(M_rstRSSET.getString("WTT_TSTTP"),"").trim() 
							+ "' And WTT_TSTNO ='" + nvlSTRVL(M_rstRSSET.getString("WTT_TSTNO"),"").trim() 
							+ "' And WTT_WTRTP ='" + ((String)vtrWACOD.elementAt(i)).trim() 
							+ "' AND WTT_TSTDT BETWEEN '" + strFMDAT + "' And '"+ strTODAT 
							+ "' AND isnull(WTT_STSFL,'') <> 'X'";						
						L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(L_rstRSSET !=null)
						{											
							while(L_rstRSSET.next())
							{								
								L_strTSTDTE = L_rstRSSET.getString("WTT_TSTDT");
								for(int j=0;j<vtrQPRLS.size();j++)
								{									
									L_strQPRCD = "WTT_" + ((String)(vtrQPRLS.elementAt(j))).toString().trim()+ "VL";
									strUMVAL = L_rstRSSET.getString(L_strQPRCD);
									if(strUMVAL !=null)	
									L_arrUMVAL [j][i] =  strUMVAL.trim();									
								}					
							}
							L_rstRSSET.close();
						}											
					}			
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("Day ending at " + strTIME +" hrs on Date: "+txtDATE.getText().trim()+ "\n");
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");			
			dosREPORT.writeBytes(padSTRING('R',"A-EFFLUENT ANALYSIS",30));
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");			
			
			dosREPORT.writeBytes(padSTRING('R',"TIME:"+strTSTM1,17));			
//			dosREPORT.writeBytes(padSTRING('L',"Day ending at " + strTIME +" hrs on Date: "+txtDATE.getText().trim(),44));
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst += 1;
						
			L_stbQPRNM = new StringBuffer(padSTRING('R',"Parameter",10));
			L_stbQPUOM = new StringBuffer(padSTRING('R'," ",10));
			L_stbQPRNM.append(padSTRING('L',"Unit",8));
			L_stbQPUOM.append(padSTRING('L'," ",8));
			L_stbQPRNM.append(padSTRING('L',"MPCB",10));
			L_stbQPUOM.append(padSTRING('L',"Limits",10));
			for(int i=0;i<vtrWADES.size();i++)
			{
				L_stbQPRNM.append(padSTRING('L',"EFFLUENT",15));
				L_stbQPUOM.append(padSTRING('L',((String)vtrWADES.elementAt(i)).trim(),15));
			}			
			
			dosREPORT.writeBytes(L_stbQPRNM.toString());
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(L_stbQPUOM.toString());
			dosREPORT.writeBytes("\n"+strDOTLN +"\n");
			cl_dat.M_intLINNO_pbst += 2;			
			StringBuffer L_stbQPRCD = new StringBuffer();
			for(int i=0;i<vtrQPRLS.size();i++)
			{
				L_stbQPRCD.delete(0,L_stbQPRCD.toString().length());
				L_stbQPRCD.append(padSTRING('R',((String)(vtrQPRDS.elementAt(i))),10));
				L_stbQPRCD.append(padSTRING('L',((String)(vtrQPUOM.elementAt(i))),8));
				L_stbQPRCD.append(padSTRING('L',((String)hstQPRNG.get(((String)vtrQPRLS.elementAt(i)))),10));
				for(int j=0;j<vtrWACOD.size();j++)
				 {
					if(L_arrUMVAL[i][j] !=null)
					{						
					    if(L_arrUMVAL[i][j].trim().length()>0)
					    {							
					 		if(Double.valueOf(L_arrUMVAL [i][j].trim()).doubleValue()!=0.0)
					 			L_stbQPRCD.append(padSTRING('L',L_arrUMVAL [i][j].trim(),15));
					 		else							
								L_stbQPRCD.append(padSTRING('L',"ND",15));							
					 	}
						else						
					 		L_stbQPRCD.append(padSTRING('L',"-",15));															
					}
					else 					
					    L_stbQPRCD.append(padSTRING('L',"-",15));													
				 }												
				dosREPORT.writeBytes(L_stbQPRCD.toString()+"\n");				
				cl_dat.M_intLINNO_pbst += 1;				
			}	
			dosREPORT.writeBytes(strDOTLN+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getEFWANY");
		}
	}
	/**
	 * Method to get the quality parameter value range.
	 */
	private void getQPRRNG()
	{
		try
		{
			String L_strQPRCD = "";
			String L_strNPFVL = "";
			String L_strNPTVL = "";
		    strQCATP = M_strSBSCD.substring(2,4);
			M_strSQLQRY = "Select QP_QPRCD,QP_NPFVL,QP_NPTVL from CO_QPMST where QP_QCATP ='"; 
			M_strSQLQRY = M_strSQLQRY +strQCATP.trim();		
			M_strSQLQRY = M_strSQLQRY + " 'and QP_PRDCD = '6800000001'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					L_strQPRCD = L_rstRSSET.getString("QP_QPRCD").trim();
					L_strNPFVL = nvlSTRVL(L_rstRSSET.getString("QP_NPFVL"),"0");
					L_strNPTVL = nvlSTRVL(L_rstRSSET.getString("QP_NPTVL"),"0");
					if(Double.valueOf(L_strNPFVL).doubleValue() != 0.0)
					{
						if(!L_strQPRCD.equals("DO_"))
							hstQPRNG.put(L_strQPRCD,Double.valueOf(L_strNPFVL).toString()+ "-" +Double.valueOf(L_strNPTVL).toString());
						else
							hstQPRNG.put(L_strQPRCD,"min"+ " " +Double.valueOf(L_strNPFVL).toString());
					}
				    else					
						hstQPRNG.put(L_strQPRCD,Double.valueOf(L_strNPTVL).toString());					
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
	 * Method to get quality parameter for the given Test parameter i.e.'QCXXXEFA' for Effluent Test.
	 * @param P_strTSTPR String argument to pass test parameter.
	 */
	private void getTSTDET(String P_strTSTPR)
	{
		ResultSet L_rstRSSET;
		StringTokenizer stzTEMP;
		String L_strCODDS="";
		String L_strSHRDS="";
		String L_strCCSVL="";
		String L_strQPRCD="";
		try
		{
			if(vtrQPRLS !=null)
				vtrQPRLS.removeAllElements();
			if(vtrQPRDS !=null)
				vtrQPRDS.removeAllElements();
			if(vtrQPUOM !=null)
				vtrQPUOM.removeAllElements();
			M_strSQLQRY = "Select CMT_CODCD,CMT_NMP02 from CO_CDTRN where CMT_CGMTP = ";
			M_strSQLQRY += "'"+"RPT"+"' and CMT_CGSTP = '" + P_strTSTPR + "'";
			M_strSQLQRY += " order by CMT_NMP02"; 			
			L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_rstRSSET !=null)
			{				
				while(L_rstRSSET.next())
				{					
					L_strQPRCD = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");
					vtrQPRLS.addElement(L_strQPRCD);					
					int i=0;
					stzTEMP	= new StringTokenizer(hstQPRCD.get(L_strQPRCD).toString(),"|");
					while(stzTEMP.hasMoreTokens())
					{
						if(i==0)
						{
							L_strCODDS = stzTEMP.nextToken();//cl_dat.getPRMCOD("CMT_CODDS","SYS","QCXXQPR",L_strQPRCD).trim());							
						}
						else if(i==1)
						{
							L_strSHRDS = stzTEMP.nextToken();//cl_dat.getPRMCOD("CMT_SHRDS","SYS","QCXXQPR",L_strQPRCD).trim());							
						}
						else if(i==2)
							L_strCCSVL = stzTEMP.nextToken();//cl_dat.getPRMCOD("CMT_CCSVL","SYS","QCXXQPR",L_strQPRCD).trim());
						i++;						
					}					
					if(P_strTSTPR.equals("QCXXUWA"))
						vtrQPRDS.addElement(L_strCODDS);					
					else											
						vtrQPRDS.addElement(L_strSHRDS);
																				
					vtrQPUOM.addElement(L_strCCSVL);
				}
				L_rstRSSET.close();
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getTSTDET");
		}
	}
	/**
	 * Method to get the quality parameter code & description 
	 * @param L_strTSTTP String to pass the Test Type.
	 */
	private void getWATCOD(String L_strTSTTP)
	{
		try
		{
			vtrWACOD.removeAllElements();
			vtrWADES.removeAllElements();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from co_cdtrn where cmt_cgmtp='SYS'and CMT_CGSTP = 'QCXXWTP'";
			M_strSQLQRY = M_strSQLQRY + " and CMT_MODLS like '%" + L_strTSTTP + "%'";		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while (M_rstRSSET.next())
				{
					vtrWACOD.addElement(M_rstRSSET.getString("CMT_CODCD").trim());
					vtrWADES.addElement(M_rstRSSET.getString("CMT_CODDS").trim());
				}
				M_rstRSSET.close();				
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getWATCOD");
		}
	}
	/**
	 * Method to get the Utiltiy water analysis details.
	 */
	private void getUWANY()
	{
		StringBuffer L_stbQPRNM;
		StringBuffer L_stbQPUOM;
		int L_intTKNCT = 0;
		try
		{	
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',"B-UTILITY WATER ANALYSIS",30));
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("TIME:"+strTSTM2+"\n"+strDOTLN+"\n");			
			cl_dat.M_intLINNO_pbst += 1;
			
			L_stbQPRNM = new StringBuffer(padSTRING('R',"Parameter",13));
			L_stbQPUOM = new StringBuffer(padSTRING('R'," ",13));
			L_stbQPRNM.append(padSTRING('L',"Unit",7));
			L_stbQPUOM.append(padSTRING('L',"",7));
			String L_arrUMVAL [][] = new String[vtrQPRLS.size()][vtrWACOD.size()];
			for(int i=0;i<vtrWADES.size();i++)
			{
				StringTokenizer st = new StringTokenizer(((String)vtrWADES.elementAt(i)).trim());
				L_intTKNCT = st.countTokens();
				while (st.hasMoreTokens())
				{
					L_stbQPRNM.append(padSTRING('L',st.nextToken(),10));
					if(L_intTKNCT == 2)
						L_stbQPUOM.append(padSTRING('L',st.nextToken(),10));
					else if(L_intTKNCT == 3)
						L_stbQPUOM.append(padSTRING('L',st.nextToken() + " " +st.nextToken() ,10));	
					else
						L_stbQPUOM.append(padSTRING('L'," ",10));	
				}
			}			
			dosREPORT.writeBytes(L_stbQPRNM.toString()+"\n");			
			cl_dat.M_intLINNO_pbst += 1;			
			
			dosREPORT.writeBytes(L_stbQPUOM.toString());
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst += 1;
			/*****************************************************************			 */
            M_strSQLQRY = "Select distinct WTT_QCATP,WTT_TSTTP,WTT_TSTNO From QC_WTTRN Where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_TSTDT "; //WTT_TSTTP = '0403'and WTT_TSTNO = '10100006'" ;
            M_strSQLQRY += " Between '" + strFMDAT.trim() + "' And '"+ strTODAT.trim() + "' AND WTT_TSTTP = '0403'";
            M_strSQLQRY += " AND WTT_STSFL <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			String L_strSQLQRY="";
			if(M_rstRSSET.next())
			{
				for(int i=0;i<vtrWACOD.size();i++)
				{
                    L_strSQLQRY = "Select * FROM QC_WTTRN where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_QCATP = '" 
						+ M_rstRSSET.getString("WTT_QCATP").trim() 
						+ "'And WTT_TSTTP ='" + M_rstRSSET.getString("WTT_TSTTP").trim() 
						+ "'And WTT_TSTNO ='" + M_rstRSSET.getString("WTT_TSTNO").trim() 
						+ "'And WTT_WTRTP ='" + ((String)vtrWACOD.elementAt(i)).trim()+ "'" 
						+ " AND WTT_STSFL <> 'X'";				
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					String L_strQPRCD ="";
					while(L_rstRSSET.next())
					{
						for(int j=0;j<vtrQPRLS.size();j++)
						{
							L_strQPRCD = "WTT_" + ((String)(vtrQPRLS.elementAt(j)).toString().trim())+ "VL";
							// for Null change 
							strUMVAL = L_rstRSSET.getString(L_strQPRCD);
							if(strUMVAL !=null)
								L_arrUMVAL [j][i] =  strUMVAL.trim();
						}
					}
					L_rstRSSET.close();
				}
				M_rstRSSET.close();
			}
			String L_strQPRLS ="";
			StringBuffer L_stbQPRCD = new StringBuffer();
		    for(int i=0;i<vtrQPRLS.size();i++)
			{
				 L_stbQPRCD.delete(0,L_stbQPRCD.length());
				L_strQPRLS = ((String)(vtrQPRDS.elementAt(i))).trim();
				if(L_strQPRLS.trim().length()> 13)
					L_strQPRLS.substring(13);
				L_stbQPRCD.append(padSTRING('R',L_strQPRLS.trim(),13));
				L_stbQPRCD.append(padSTRING('L',((String)(vtrQPUOM.elementAt(i))).trim(),7));
				for(int j=0;j<vtrWACOD.size();j++)
				{
					if(L_arrUMVAL[i][j]!=null)
				   {
						if(L_arrUMVAL[i][j]!=null && L_arrUMVAL[i][j].trim().length()>0)
						{
							if(Double.valueOf(L_arrUMVAL [i][j]).doubleValue()!=0.0)
								L_stbQPRCD.append(padSTRING('L',(L_arrUMVAL [i][j]),10));
							else
								L_stbQPRCD.append(padSTRING('L',"ND",10));							
						}
						else
							L_stbQPRCD.append(padSTRING('L',"-",10));	
					}
					else 
						L_stbQPRCD.append(padSTRING('L',"-",10));										
				}				
				dosREPORT.writeBytes(L_stbQPRCD.toString()+"\n");
				cl_dat.M_intLINNO_pbst += 1;				
			}	
			dosREPORT.writeBytes(strDOTLN+"\n");
		}
		catch(Exception E)
		{
			setMSG(E,"getUWANY");
		}		
	}
	/**
	 * Method to get the Extra water analysis details.
	 */
	private void getEXWANY()
	{
		Timestamp L_tmsTSTDT;
		String L_strQPRCD = "";
		try
		{					
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");					 
			dosREPORT.writeBytes("C-EXTRA WATER ANALYSIS"+"\n"+strDOTLN+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			cl_dat.M_intLINNO_pbst += 1;
			
			StringBuffer L_stbQPRNM = new StringBuffer(padSTRING('R',"SAMPLE",20));
			L_stbQPRNM.append(padSTRING('L',"TIME",7));			
			for(int i=0;i<vtrQPRDS.size();i++)			
				L_stbQPRNM.append(padSTRING('L',((String)vtrQPRDS.elementAt(i)).trim(),8));			
			
			dosREPORT.writeBytes(L_stbQPRNM.toString());
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			
			StringBuffer L_stbQPUOM = new StringBuffer(padSTRING('R'," ",20));
			L_stbQPUOM.append(padSTRING('L',"(HRS)",7));
			for(int i=0;i<vtrQPUOM.size();i++)			
				L_stbQPUOM.append(padSTRING('L',((String)vtrQPUOM.elementAt(i)).trim(),8));
			
			dosREPORT.writeBytes(L_stbQPUOM.toString());
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst += 1;
			
			M_strSQLQRY = "Select * From QC_WTTRN Where WTT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WTT_TSTDT "
				+ " between '"+strFMDAT+ "' and '"+strTODAT+ "' AND WTT_TSTTP = '0402' "
				+ " AND WTT_STSFL <> 'X' order by WTT_WTRTP,WTT_TSTDT";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			String L_strTMPDT = " ";
			if (M_rstRSSET != null)
			{
				StringBuffer L_stbRECORD = new StringBuffer();	
				String L_strSQLQRY ="";
				while(M_rstRSSET.next())
				{						
					L_stbRECORD.delete(0,L_stbRECORD.toString().length());
					L_strSQLQRY = "Select CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS'"
						+" AND CMT_CGSTP = 'QCXXWTP' and CMT_CODCD = '"
						+ M_rstRSSET.getString("WTT_WTRTP").trim()+ "'";					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					
					if(L_rstRSSET.next())				
						L_stbRECORD.append((padSTRING('R',L_rstRSSET.getString("CMT_CODDS").trim(),20)));						
					
					L_tmsTSTDT = M_rstRSSET.getTimestamp("WTT_TSTDT");
					if(L_tmsTSTDT!= null)
						L_strTMPDT = M_fmtLCDTM.format(L_tmsTSTDT);
				    
					if(L_strTMPDT != null)
					{
						if((L_strTMPDT.trim()!= null)&&(L_strTMPDT.trim().length()>0))
							L_stbRECORD.append((padSTRING('L',L_strTMPDT.substring(11,16),7)));
					}					
					for(int j=0;j<vtrQPRLS.size();j++)
					{
						L_strQPRCD = "WTT_" + ((String)(vtrQPRLS.elementAt(j))).trim()+ "VL";
						L_strQPRCD = M_rstRSSET.getString(L_strQPRCD);
						if(L_strQPRCD !=null)
						{
							L_strQPRCD = L_strQPRCD.trim();
							if(L_strQPRCD.length()>0)
							{
								if(Double.valueOf(L_strQPRCD).doubleValue()!=0.0)
									L_stbRECORD.append(padSTRING('L',L_strQPRCD,8));
								else								
									L_stbRECORD.append(padSTRING('L',"ND",8));								
							}
							else
								L_stbRECORD.append(padSTRING('L',"-",8));
						}
						else
							L_stbRECORD.append(padSTRING('L',"-",8));						
					}				
					dosREPORT.writeBytes(L_stbRECORD.toString());
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;				
					L_rstRSSET.close();
				}
				M_rstRSSET.close();
				dosREPORT.writeBytes(strDOTLN);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getEXWANY");
		}
	}
	/**
	 * Method to modify the Remarks if old remarks are changed.
	 * @param P_strRPTNM String argument to pass Report Name.
	 * @param P_strRPTDT String argument to pass Report Date.
	 * @param P_strRMSRL String argument to pass Remark Serial Number
	 * @param P_strRMRK String argument to pass new Repark Description.
	 * @param P_strORMKDS String argument to pass old Remark Description.
	 */
	public void updRPTRM(String P_strRPTNM,String P_strRPTDT,String P_strRMSRL,String P_strRMRK,String P_strORMKDS)
	{					
		try
		{		 			
		    strQCATP = M_strSBSCD.substring(2,4);
			if(!P_strORMKDS.equals(P_strRMRK))
			{
				if(P_strRMRK.length() > 0)
				{					
    				M_strSQLQRY = "Update QC_RMMST set ";
					M_strSQLQRY += " RM_REMDS ='" + P_strRMRK.trim()+"',";
					M_strSQLQRY += " RM_LUSBY = '" + cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += " RM_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "'";
				}
				else
					M_strSQLQRY = "Delete from QC_RMMST ";				
				M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = " + "'"+strQCATP + "'";
				M_strSQLQRY += " AND RM_TSTTP = "+ "'"+P_strRPTNM.substring(5).trim()+P_strRMSRL.trim()+"'";
				M_strSQLQRY += " AND RM_TSTNO = "+ "'"+P_strRPTDT.substring(0,2)+P_strRPTDT.substring(3,5)+P_strRPTDT.substring(6)+"'";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"updRPTRM");
		}
	}
	/**
	 * Method to insert the Remarks if remarks are insert first.
	 * @param P_strRPTNM String argument to pass Report Name.
	 * @param P_strRPTDT String argument to pass Report Date.
	 * @param P_strRMSRL String argument to pass Remark Serial Number
	 * @param P_strRMRK String argument to pass new Repark Description.
	 * @param P_strORMKDS String argument to pass old Remark Description.
	 */
	public void insRPTRM(String P_strRPTNM,String P_strRPTDT,String P_strRMSRL,String P_strRMRK,String P_strORMKDS)
	{		
		try
		{			
		    strQCATP = M_strSBSCD.substring(2,4);
			if(!P_strORMKDS.equals(P_strRMRK))
			{
				M_strSQLQRY = "Insert into QC_RMMST(RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)values(";
				M_strSQLQRY +="'"+strQCATP+"',";
				M_strSQLQRY +="'"+P_strRPTNM.substring(5).trim()+P_strRMSRL.trim()+"',";
				M_strSQLQRY +="'"+P_strRPTDT.substring(0,2)+P_strRPTDT.substring(3,5)+P_strRPTDT.substring(6)+"',";
				M_strSQLQRY +="'"+P_strRMRK.trim()+"',";
				M_strSQLQRY += getUSGDTL("RM",'I',"") + ")";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");				
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"insRPTRM");
		}
	}
}