/**
System Name   : Laboratoty Information Management System
Program Name  : In process / Other tests details
Program Desc. : Generates the Report for the in process & other test Details. 				
Author        : Mr. S.R.Mehesare
Date          : 6 JUNE 2005
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
import javax.swing.JComponent;import javax.swing.InputVerifier;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name  : In process / Other tests details

Purpose : Report to print the in process & other test Details for given date.
----------------------------------------------
List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
QC_RMMST       RM_QCATP,RM_TSTTP,RM_TSTNO                      #       #
QC_RSMST       RS_QCATP,RS_TSTTP,RS_TSTNO
QC_SMTRN       SMT_QCATP,SMT_TSTTP,SMT_TSTNO,                          #
               SMT_MORTP,SMT_TSTDT,SMT_TSTRF                           #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtDATE	           Report date ( for the day ending at 07:00)
txtRMRK1          Remark to save and print on report
txtRMRK2          Remark to save and print on report
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
   3) List of Columns to fetch from the database is generated dynamically  
    i.e. "PS_"+ Vector.elementAt(i)+"VL" i.e  PS_QCATP, PS_TSTTP
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

Quality parameter codes List is taken from CO_CDTRN & Data is taken from QC_RSMST for following condition
  1)Reactor Sample 
      Quality parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'SYS'
       2) AND CMT_CGSTP = 'QCXXLNR'
      Quality parameter values are taken from QC_RSMST for condiations 
       1) RS_QCATP = '11'
       2) AND RS_TSTTP = '0201'
       3) AND RS_TSTDT between the last 24 hours of the given date.
  2)Recycle EB Proparties
      Quality parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = 'QCXXREB'
      Quality parameter values are taken from QC_SMTRN for condiations 
       1) SMT_QCATP = '11'
       2) AND SMT_TSTTP = '0203'
       3) AND SMT_TSTDT between the last 24 hours of the given date.
  3) TBC REMOVAL BED / OTHER SAMPLES
      Quality parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = 'QCXXTBC'
      Quality parameter values are taken from QC_RSMST for condiations 
       1) RS_QCATP = '11'
       2) AND RS_TSTTP = '0202'
       3) AND RS_TSTDT between the last 24 hours of the given date
   4) SM TANER
      Quality parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = 'QCXXSTA'
      Quality parameter values are taken from QC_RSMST for condiations 
       1) RS_QCATP = '11'
       2) AND RS_TSTTP = '0302'
       3) AND RS_TSTDT between the last 24 hours of the given date
    5) SM TANKERS
      Quality parameters are taken from CO_CDTRN for condiations
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = 'QCXXSTQ'
      Quality parameter values are taken from QC_RSMST for condiations 
       1) RS_QCATP = '11'
       2) AND RS_TSTTP = '0303'
       3) AND RS_TSTDT between the last 24 hours of the given date
   VI) FO/ MO TANKERS
      For Proparties
       1) CMT_CGMTP = 'RPT'
       2) AND CMT_CGSTP = 'QCXXFOM'
      Quality parameter values are taken from QC_RSMST for condiations 
       1) RS_QCATP = '11'
       2) AND RS_TSTTP = '0301'
       3) AND RS_TSTDT between the last 24 hours of the given date.
             
<B>Validations :</B>
    - Given date must be valid.
    - New Remark insertion, old remark updation is possible.
</I> */

public class qc_rpipt extends cl_rbase
{										/** JTextField to enter & display date to generate the  Repopt.*/
	private JTextField txtDATE;			/** JTextField to enter & display Remark1.*/
	private JTextField txtRMRK1;		/** JTextField to enter & display Remark2.*/
	private JTextField txtRMRK2;		/** String variable for Remark 1.*/
	private String strRMRK1="";			/** String variable for Remark 2.*/
	private String strRMRK2="";			/** String variable for ISODOC Number.*/
	private String strISOD1;			/** String variable for ISODOC Number.*/
	private String strISOD2;			/** String variable for ISODOC Number.*/
	private String strISOD3;			/** String variable for generated Report file Name.*/
	private String strFILNM;			/** Integer Variable to count the number of records fetched.*/
	private int intRECCT;				/** String variable for Print Dotted line.*/
	private String strDOTLN = "-------------------------------------------------------------------------------------------------------------------------";//120
										/** String variable for Test Type.*/
	private String strTSTTP;			/** String variable for QCA Type.*/	
	private String strQCATP;			/** String variable for To Date Time to specify date time Range.*/
	private String strTODAT;			/** String variable for from date time to specify the date time range.*/
	private String strFMDAT;			/** Hashtable to maintain Quality parameter & Unit of Measurement.*/
	private Hashtable<String,String> hstUOMCD;			/** Hashtable to maintain Line Number & Description.*/
	private Hashtable<String,String> hstLINDS;			/** FileOutputStream object for file handling.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream object to generate the Stream of data.*/
    private DataOutputStream dosREPORT;	/** Boolean variable to manage the insertion, updation of Remarks*/
	private boolean flgRMSTS = false;	/** String variable for dynamically generated header.*/
	private String strHEADR;		
	qc_rpipt()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Date"),4,3,1,.7,this,'R');
			add(txtDATE = new TxtDate(),4,4,1,1,this,'L');			
			add(new JLabel("Remark1"),5,3,1,.7,this,'R');
			add(txtRMRK1 = new TxtLimit(200),5,4,1,4,this,'L');			
			add(new JLabel("Remark2"),6,3,1,.7,this,'R');
			add(txtRMRK2 = new TxtLimit(200),6,4,1,4,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);						
			
			hstUOMCD = new Hashtable<String,String>();
			hstLINDS = new Hashtable<String,String>(); 				
			M_strSQLQRY = "Select QS_QPRCD,QS_UOMCD from CO_QSMST where isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
						hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));						
				}
			}			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP = 'QCXXLOT'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strLINNO="";
				while(M_rstRSSET.next())
				{
					L_strLINNO = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");					
					if(!L_strLINNO.equals(""))
						hstLINDS.put(L_strLINNO,nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));						
				}
			}			
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
				if(txtDATE.getText().trim().length()== 0)
				{
					txtDATE.setText(cl_dat.M_strLOGDT_pbst);
					txtRMRK1.setText("");
					txtRMRK2.setText("");
				}
				txtDATE.requestFocus();	
				setMSG("Please Enter Sale Type OR Press F1 to Select from List..",'N');				
				setENBL(true);
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
			if(txtDATE.getText().trim().length()>0)
			{						
				try
				{
					String L_strRPTDT = txtDATE.getText().trim();
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					txtRMRK1.setText("");
					txtRMRK2.setText("");										
					strRMRK1 = "";
					strRMRK2 = "";
								
					M_strSQLQRY ="Select RM_REMDS,RM_TSTTP from QC_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = "
						+"'"+  M_strSBSCD.substring(2,4).trim() +"'"
						+" AND RM_TSTTP like " + "'IPT%'"
						+" AND RM_TSTNO = " + "'"+ L_strRPTDT.substring(0,2).trim()+L_strRPTDT.substring(3,5).trim()+L_strRPTDT.substring(6).trim()+"'";					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						String L_strTSTTP="";
						while(M_rstRSSET.next())
						{
							L_strTSTTP = nvlSTRVL(M_rstRSSET.getString("RM_TSTTP"),"");							
							if(!L_strTSTTP.equals(""))
							{	
								if(L_strTSTTP.equals("IPT1"))
								{
									strRMRK1 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK1.setText(strRMRK1);									
								}
								else if(L_strTSTTP.equals("IPT2"))
								{
									strRMRK2 = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
									txtRMRK2.setText(strRMRK2);									
								}						
							}
						}
						M_rstRSSET.close();
					}								
					this.setCursor(cl_dat.M_curDFSTS_pbst);
					txtRMRK1.requestFocus();
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					txtDATE.setEnabled(false);
				}
				catch(Exception L_EX)
				{
					setMSG(L_EX,"M_objSOURC == txtDATE");	
				}
			}			
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_ENTER)
 	    {
			if(M_objSOURC == txtDATE)
			{
				if(txtDATE.getText().trim().length()>0)
				{					
					txtRMRK1.requestFocus();				
					setMSG("Enter Or Modify the Remark1 if requries..",'N');
				}
			}
			if(M_objSOURC == txtRMRK1)
			{
				txtRMRK2.requestFocus();								
				setMSG("Enter OR Modify the Remark2 if requries..",'N');				
			}
			if(M_objSOURC == txtRMRK2)
			{				
				cl_dat.M_btnSAVE_pbst.requestFocus();				
			}			
		}
	}	
	/**
	Method to print, display report as per selection
	*/
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rpipt.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpipt.doc";
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();		
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"In Process & Other Test Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exePrint.. ");
		}
	}			
    /**
    * Method to get quality prameter list &  Dynamiclly generate the header & body of the Report
    *& to club all together in the DataOutputStream to generate the Report.
	*/
	private void getDATA()
	{ 						
		String L_strRMKDS = "";
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
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>In process / Other tests details </title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
			
			strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim()+" 07:00"));
			try
			{				
				M_calLOCAL.setTime(M_fmtLCDAT.parse(txtDATE.getText().trim()));
				M_calLOCAL.add(java.util.Calendar.DATE,-1);																       			
				strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(M_fmtLCDAT.format(M_calLOCAL.getTime()) +" 07:00"));
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX, "getDATA");
				return;
			}													
			
			prnHEADER();
			
			prnRPRSS();											
						
			prnRPREB();
			
			prnRPTBC();
						
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize>");
			else
				prnFMTCHR(dosREPORT,M_strEJT);
				
			prnHEADER();			    
			
			prnRPTNK();
			
			prnRPTKR();
			
			prnRPFMT();
				
			dosREPORT.writeBytes("\n\n"+"Remarks : "+"\n");
			L_strRMKDS = txtRMRK1.getText().trim();
			if(L_strRMKDS.length()>100)
			{
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(L_strRMKDS.substring(0,100)+"\n");
				dosREPORT.writeBytes(padSTRING('L',"",7));
				dosREPORT.writeBytes(L_strRMKDS.substring(100)+"\n");
			}
			else
			{
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(L_strRMKDS+"\n");
			}
			if(!strRMRK1.equals(txtRMRK1.getText().trim()))
			{						
				if(strRMRK1.equals(""))
					insRPTRM("QC_RPIPT",txtDATE.getText().trim(),"1",txtRMRK1.getText().trim(),strRMRK1);
				else
					updRPTRM("QC_RPIPT",txtDATE.getText().trim(),"1",txtRMRK1.getText().trim(),strRMRK1);
				if(cl_dat.M_flgLCUPD_pbst)
					cl_dat.exeDBCMT("updRPTRM");
			}						
			
			L_strRMKDS = txtRMRK2.getText().trim();
			if(L_strRMKDS.length()>100)
			{
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(L_strRMKDS.substring(0,100)+"\n");
				dosREPORT.writeBytes(padSTRING('L',"",7));
				dosREPORT.writeBytes(L_strRMKDS.substring(100)+"\n");
			}
			else
			{
				dosREPORT.writeBytes(padSTRING('L',"",10));
				dosREPORT.writeBytes(L_strRMKDS+"\n");
			}
			if(!strRMRK2.equals(txtRMRK2.getText().trim()))
			{																
				if(strRMRK2.equals(""))
					insRPTRM("QC_RPIPT",txtDATE.getText().trim(),"2",txtRMRK2.getText().trim(),strRMRK2);				
				else
					updRPTRM("QC_RPIPT",txtDATE.getText().trim(),"2",txtRMRK2.getText().trim(),strRMRK2);
				if(cl_dat.M_flgLCUPD_pbst)				
					cl_dat.exeDBCMT("updRPTRM");				
			}
			dosREPORT.writeBytes("\n\n\n");						
			dosREPORT.writeBytes(padSTRING('L',"HOD(QCA)",108));                                                      			
										
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    			
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	Method to validate Data, before execution, Check for blank and wrong Input.
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
				setMSG("Date must be Smaller than or equal to the current date .. ",'E');				
				txtDATE.setText(cl_dat.M_strLOGDT_pbst);
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
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			 
			if(cl_dat.M_PAGENO == 1)
			{
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
					+ " AND CMT_CGSTP ='QCXXIPT' AND isnull(CMT_STSFL,'') <> 'X'";
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
				
				StringBuffer L_stbHEADER = new StringBuffer(padSTRING('L',"-----------------------------------",120) + "\n");										
				L_stbHEADER.append(padSTRING('L',strISOD1,120)+"\n");
				L_stbHEADER.append(padSTRING('L',strISOD2,120)+"\n");
				L_stbHEADER.append(padSTRING('L',strISOD3,120)+"\n");
				L_stbHEADER.append(padSTRING('L',"-----------------------------------",120)+"\n");			
				L_stbHEADER.append(padSTRING('R',cl_dat.M_strCMPNM_pbst,96)+ "\n");			
				//L_stbHEADER.append("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");							
				L_stbHEADER.append(padSTRING('R',"IN PROCESS/RAW MATERIAL ANALYSIS REPORT",96));			
				strHEADR = L_stbHEADER.toString();							
			}
			dosREPORT.writeBytes(strHEADR + "\n");
			//dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");						
			dosREPORT.writeBytes("Day Ending at 07:00 Hrs. on "+txtDATE.getText().trim());
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to generate the header dynamically & to fetch the data for Reactor Sample 
	 * Report, for the last 24 hrs. of the given date
	 */
	private void prnRPRSS()
	{
		strTSTTP = "0201";		
		strQCATP = "01";
        //strQCATP =M_strSBSCD.substring(2,4);
		String L_strTSTDT = "",L_strPRNVL= "";
		StringBuffer L_stbTSTNM = new StringBuffer();				
		StringBuffer L_stbDATA = new StringBuffer();
		String L_strCODCD="";
		Vector<String> L_vtrTSTNM = new Vector<String>();
		try
		{	
			dosREPORT.writeBytes("\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("REACTOR SAMPLES (% Solids)");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");					
			L_stbTSTNM.append("\n"+strDOTLN+"\n");
			L_stbTSTNM.append(padSTRING('C',"Line/Reactor",117)+"\n");
			L_stbTSTNM.append(padSTRING('R',"Time",6));
				        
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS from CO_CDTRN where CMT_CGMTP ='SYS'";
			M_strSQLQRY += " AND CMT_CGSTP = 'QCXXLNR' order by cmt_shrds";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET != null)							
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = M_rstRSSET.getString("CMT_CODCD");	
					if(L_strCODCD !=null)					
						L_vtrTSTNM.addElement(L_strCODCD.trim());					
					else
						L_vtrTSTNM.addElement("");
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"");	
					L_stbTSTNM.append(padSTRING('L',L_strCODCD.trim(),9));
				}
				M_rstRSSET.close();
			}									
			L_stbTSTNM.append("\n"+strDOTLN);			
			dosREPORT.writeBytes(L_stbTSTNM.toString());
			
			M_strSQLQRY = "Select * FROM QC_RSMST WHERE RS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RS_QCATP = ";
			M_strSQLQRY += "'"+strQCATP.trim() +"'" + " AND RS_TSTTP = "+ "'"+ strTSTTP.trim() +"'";
			M_strSQLQRY += " AND RS_TSTDT BETWEEN '" + strFMDAT + "' AND '"+strTODAT;
			M_strSQLQRY += "' AND isnull(RS_STSFL,'') <> 'X' ORDER BY RS_TSTDT" ;									
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(M_rstRSSET != null)				
			{
				Timestamp L_tmsTEMP;
				while(M_rstRSSET.next())
				{								
					L_tmsTEMP = M_rstRSSET.getTimestamp("RS_TSTDT");
					if(L_tmsTEMP != null)
						L_strTSTDT = M_fmtLCDTM.format(L_tmsTEMP);					
					dosREPORT.writeBytes("\n");				
					L_stbDATA.append(padSTRING('L',L_strTSTDT.substring(11,16),6));					
					for(int j=0;j<L_vtrTSTNM.size();j++)
					{
						L_strPRNVL = M_rstRSSET.getString("RS_"+L_vtrTSTNM.elementAt(j).toString().trim());
						if(L_strPRNVL == null)
							L_stbDATA.append(padSTRING('L',"-",9));
						else
						{
							L_strPRNVL = L_strPRNVL.trim();
							if(setNumberFormat(Double.valueOf(L_strPRNVL.trim()).doubleValue(),1).equals("0.0"))
								L_stbDATA.append(padSTRING('L',"-",9));
							else
								L_stbDATA.append(padSTRING('L',setNumberFormat(Double.valueOf(L_strPRNVL.trim()).doubleValue() ,1),10));
						}
					}
					dosREPORT.writeBytes(L_stbDATA.toString().trim());
					L_stbDATA = L_stbDATA.delete(0,L_stbDATA.length());
				}
				dosREPORT.writeBytes("\n"+strDOTLN);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnRPRSS");
		}
	}
	/**
	 * Method to generate the header dynamically & to fetch the data for Recycle EB 
	 * Report, for the last 24 hrs. of the given date
	 */
	private void prnRPREB()
	{	
		strTSTTP = "0203";
        //strQCATP = "01";	
        strQCATP =M_strSBSCD.substring(2,4);	
		String L_strPRNVL="",L_strLINRF = "",L_strTIME="",L_strLINDS="";
		String L_strCODCD="",L_strCOLMN ="",L_strUOMCD="",L_strSHRDS="";
		StringBuffer L_stbDATA = new StringBuffer();
				
		Vector<String> L_vtrTSTNM =new Vector<String>();
		Vector<String> L_vtrDECCT =new Vector<String>();
		StringBuffer L_stbTSTNM = new StringBuffer(padSTRING('R',"Line",9));
		StringBuffer L_stbTSUOM = new StringBuffer(padSTRING('R',"",9)); 
		try
		{				        
			dosREPORT.writeBytes("\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("RECYCLE EB");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
						
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			L_stbTSTNM.append(padSTRING('R',"Time ",5));			
			L_stbTSUOM.append(padSTRING('R',"Hrs",5));
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS,CMT_NMP01,CMT_NMP02,CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'RPT'" + " AND CMT_CGSTP = 'QCXXREB'";		
			M_strSQLQRY += " AND CMT_STSFL <>'X' ORDER BY CMT_NMP02";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{															
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");						
					if(!L_strCODCD.equals(""))
					{
						L_vtrTSTNM.addElement(L_strCODCD.trim()); // Proparty column name
						L_strCOLMN +=  "SMT_" + L_strCODCD.toUpperCase() +"VL"+ ",";
						
						L_strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""); // Test Proparty Name
						L_stbTSTNM.append(padSTRING('L',L_strSHRDS,11));						
												
						L_strUOMCD = hstUOMCD.get(L_strCODCD.trim()).toString();						
						L_stbTSUOM.append(padSTRING('L',L_strUOMCD,11));											
												
						L_vtrDECCT.addElement(String.valueOf(M_rstRSSET.getInt("CMT_NMP01")).toString()); // No. of integers after decimal point
					}
				}
				M_rstRSSET.close();
			}						
			dosREPORT.writeBytes(L_stbTSTNM.toString()+"\n");
			dosREPORT.writeBytes(L_stbTSUOM.toString()+"\n");
			dosREPORT.writeBytes(strDOTLN+"\n");
						
			M_strSQLQRY = "Select "+ L_strCOLMN +"SMT_TSTRF,SMT_TSTDT FROM QC_SMTRN WHERE SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP = ";
			M_strSQLQRY += "'"+strQCATP +"'" + " AND SMT_TSTTP = "+ "'"+ strTSTTP +"'";
			M_strSQLQRY += " AND SMT_TSTDT BETWEEN '" + strFMDAT+ "' AND '"+strTODAT;
			M_strSQLQRY += "' AND SMT_STSFL <> 'X' ORDER BY SMT_TSTRF,SMT_TSTDT ";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(M_rstRSSET != null)					
			{		
				Timestamp L_tmsTEMP;
				while(M_rstRSSET.next())
				{								
					L_strLINRF = M_rstRSSET.getString("SMT_TSTRF");					
					if(L_strLINRF != null)
					{
						L_strCODCD = strTSTTP+"_"+L_strLINRF.trim();						
						L_strLINDS = hstLINDS.get(L_strCODCD).toString();						
					}
					else
						L_strLINDS = "";						
					L_tmsTEMP = M_rstRSSET.getTimestamp("SMT_TSTDT");	
					if(L_tmsTEMP !=null)
						L_strTIME = padSTRING('R',(M_fmtLCDTM.format(L_tmsTEMP).substring(11,16)),6);					
					L_stbDATA.append(padSTRING('R',L_strLINDS,8));					
					L_stbDATA.append(padSTRING('L',L_strTIME,6));										
					int L_intDECCT1 = 0;
					for(int j=0;j<L_vtrTSTNM.size();j++)
					{											
						L_intDECCT1 = Integer.parseInt(L_vtrDECCT.elementAt(j).toString());																	
						L_strPRNVL = nvlSTRVL(M_rstRSSET.getString("SMT_"+L_vtrTSTNM.elementAt(j).toString()+"VL"),"0");						
						if(setNumberFormat(Double.valueOf(L_strPRNVL.trim()).doubleValue(),1).equals("0.0"))						
							L_stbDATA.append(padSTRING('L',"-",11));	
						else
							L_stbDATA.append(padSTRING('L',setNumberFormat(Double.valueOf(L_strPRNVL.trim()).doubleValue() ,L_intDECCT1),11));							
					}					
					dosREPORT.writeBytes(L_stbDATA.toString()+"\n");					
					L_stbDATA = L_stbDATA.delete(0,L_stbDATA.length());					
				}
				M_rstRSSET.close();				
			}			
			dosREPORT.writeBytes(strDOTLN);			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnRPREB");
		}			
	}		
	/**
	 * Method to generate the header dynamically & to fetch the data for TBC REMOVAL 
	 * BED / OTHER SAMPLE Report, for the last 24 hrs. of the given date.
	 */
	private void prnRPTBC()
	{	
		strTSTTP = "0202";
		//strQCATP = "01";
        strQCATP =M_strSBSCD.substring(2,4);
		String L_strCODCD="";
		int L_intDECCT = 0;		
		String L_strTSTRF="",L_strSMPDS ="",L_strTSTDT="";
		String L_strPRNVL="",L_strTIME="",L_strCOLMN ="",L_strUOMCD="",L_strSHRDS="";	
		StringBuffer L_stbDATA = new StringBuffer();
		
		Vector<String> L_vtrTSTNM = new Vector<String>();
		Vector<String> L_vtrDECCT = new Vector<String>();
		StringBuffer L_stbTSTNM = new StringBuffer(padSTRING('R',"Time ",12));
		StringBuffer L_stbTSUOM = new StringBuffer(padSTRING('R',"Hrs",12)); 
																		
		try
		{			
			dosREPORT.writeBytes("\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("TBC REMOVAL BED / OTHER SAMPLE");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");						
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");	        									
			L_stbTSTNM.append(padSTRING('R',"Sample",12));			
			L_stbTSUOM.append(padSTRING('R',"",12));
					
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS,CMT_NMP01,CMT_NMP02,CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'RPT'" + " AND CMT_CGSTP = 'QCXXTBC'";;		
			M_strSQLQRY += " AND CMT_STSFL <>'X' ORDER BY CMT_NMP02";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");						
					if(!L_strCODCD.equals(""))
					{
						L_vtrTSTNM.addElement(L_strCODCD.trim()); // Proparty column name
						L_strCOLMN +=  "SMT_" + L_strCODCD.toUpperCase() +"VL"+ ",";
						
						L_strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""); // Test Proparty Name
						L_stbTSTNM.append(padSTRING('L',L_strSHRDS,12));						
												
						L_strUOMCD = hstUOMCD.get(L_strCODCD.trim()).toString();
						L_stbTSUOM.append(padSTRING('L',L_strUOMCD,12));											
						
						L_vtrDECCT.addElement(String.valueOf(M_rstRSSET.getInt("CMT_NMP01")).toString()); // No. of integers after decimal point
					}																
				}
				M_rstRSSET.close();
			}							        
			dosREPORT.writeBytes(L_stbTSTNM.toString()+"\n");
			dosREPORT.writeBytes(L_stbTSUOM.toString()+"\n");
			dosREPORT.writeBytes(strDOTLN);	
					
			M_strSQLQRY = "Select " +L_strCOLMN+ "SMT_TSTDT,SMT_TSTRF FROM QC_SMTRN WHERE SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP = ";
			M_strSQLQRY += "'"+ strQCATP +"'" + " AND SMT_TSTTP = "+ "'"+ strTSTTP +"'";
			M_strSQLQRY += " AND SMT_TSTDT BETWEEN '" + strFMDAT + "' AND '"+strTODAT;
			M_strSQLQRY += "' AND SMT_STSFL <> 'X'" ;   
			M_strSQLQRY += " ORDER BY SMT_TSTDT,SMT_TSTRF ";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)		
			{
				Timestamp L_tmsTEMP;
				while(M_rstRSSET.next())
				{
					L_tmsTEMP = M_rstRSSET.getTimestamp("SMT_TSTDT");
					if(L_tmsTEMP != null)
						L_strTSTDT = M_fmtLCDTM.format(L_tmsTEMP);																									
					L_strTIME = padSTRING('R',L_strTSTDT.substring(11,16),12);
					L_stbDATA.append("\n" + L_strTIME);
					
					L_strTSTRF = M_rstRSSET.getString("SMT_TSTRF");											
					L_strSMPDS = hstLINDS.get(strTSTTP+"_"+L_strTSTRF.trim()).toString();
						
					L_strSMPDS = padSTRING('R',L_strSMPDS.trim(),12);				
					L_stbDATA.append(L_strSMPDS);									
					for(int j=0;j<L_vtrTSTNM.size();j++)
					{												
						L_intDECCT = Integer.parseInt(L_vtrDECCT.elementAt(j).toString());
						L_strPRNVL = nvlSTRVL(M_rstRSSET.getString("SMT_"+L_vtrTSTNM.elementAt(j).toString().trim()+"VL"),"0");																		
						if(setNumberFormat(Double.valueOf(L_strPRNVL.trim()).doubleValue(),L_intDECCT).equals("0.0"))
							L_stbDATA.append(padSTRING('L',"-",12));
						else
						{							
							if(L_intDECCT == 0)
								L_stbDATA.append(padSTRING('L',L_strPRNVL,12));
							else
								L_stbDATA.append(padSTRING('L',setNumberFormat(Double.valueOf(L_strPRNVL).doubleValue(),L_intDECCT),12));							
						}								
					}																													
				}
				M_rstRSSET.close();
				dosREPORT.writeBytes(L_stbDATA.toString());	
			}	
			dosREPORT.writeBytes("\n"+strDOTLN);
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnRPTBC");
		}	
	}
	/**
	 * Method to generate the header dynamically & to fetch the data for SM TANKS
	 * Report, for the last 24 hrs. of the given date.
	 */
	private void prnRPTNK()
	{
		strTSTTP = "0302";
		//strQCATP = "11";		
        strQCATP =M_strSBSCD.substring(2,4);
		String L_strTIME="",L_strCODCD="",L_strCOLMN ="",L_strUOMCD="",L_strSHRDS="";
		String L_strTNKDS="",L_TSTREF="",L_strTSTDT="",L_strPRNVL ="";
		int L_intDECCT = 0;
		StringBuffer L_stbDATA = new StringBuffer("");
		
		Vector<String> L_vtrTSTNM = new Vector<String>();
		Vector<String> L_vtrDECCT = new Vector<String>();
		StringBuffer L_stbTSTNM = new StringBuffer("");
		StringBuffer L_stbTSUOM = new StringBuffer(""); 		
		try
		{	
			dosREPORT.writeBytes("\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("SM TANKS");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");
						
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");							        										
			L_stbTSTNM.append(padSTRING('R',"Tanks",10));			
			L_stbTSTNM.append(padSTRING('R',"Time",6));			
			L_stbTSUOM.append(padSTRING('R',"",10));
			L_stbTSUOM.append(padSTRING('R',"Hrs",6));
					
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS,CMT_NMP01,CMT_NMP02,CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'RPT'" + " AND CMT_CGSTP = 'QCXXSTA'";;		
			M_strSQLQRY += " AND CMT_STSFL <>'X' ORDER BY CMT_NMP02";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");						
					if(!L_strCODCD.equals(""))
					{
						L_vtrTSTNM.addElement(L_strCODCD.trim()); // Proparty column name
						L_strCOLMN +=  "SMT_" + L_strCODCD.toUpperCase() +"VL"+ ",";
						
						L_strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""); // Test Proparty Name
						L_stbTSTNM.append(padSTRING('L',L_strSHRDS,8));						
												
						L_strUOMCD = hstUOMCD.get(L_strCODCD.trim()).toString();
						L_stbTSUOM.append(padSTRING('L',L_strUOMCD,8));											
						
						L_vtrDECCT.addElement(String.valueOf(M_rstRSSET.getInt("CMT_NMP01")).toString()); // No. of integers after decimal point
					}																
				}
				M_rstRSSET.close();
			}							       
			L_stbTSTNM.append(padSTRING('L',"QLTY",8));			
			L_stbTSUOM.append(padSTRING('L',"(OK)",8));			
			dosREPORT.writeBytes(L_stbTSTNM.toString()+"\n");
			dosREPORT.writeBytes(L_stbTSUOM.toString()+"\n");
			dosREPORT.writeBytes(strDOTLN);	
						
			M_strSQLQRY = "Select "+L_strCOLMN+"SMT_TSTDT,SMT_TSTRF,SMT_QLTFL FROM QC_SMTRN WHERE SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP =";
			M_strSQLQRY += "'"+strQCATP.trim() +"'" + " AND SMT_TSTTP = "+ "'"+ strTSTTP.trim() +"'";
			M_strSQLQRY += " AND SMT_TSTDT BETWEEN '" + strFMDAT.trim() + "' AND '"+strTODAT.trim();
			M_strSQLQRY += "' AND SMT_STSFL <> 'X'" ;  
			M_strSQLQRY += " ORDER BY SMT_TSTRF,SMT_TSTDT ";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{
				Timestamp L_tmsTEMP;
				while(M_rstRSSET.next())
				{					
					L_TSTREF = nvlSTRVL(M_rstRSSET.getString("SMT_TSTRF"),"");	
					if(!L_TSTREF.equals(""))
					{						
						L_strCODCD = strTSTTP.trim()+"_"+L_TSTREF.trim();						
						L_strTNKDS = hstLINDS.get(L_strCODCD).toString();
						
						L_stbDATA.append("\n"+padSTRING('R',L_strTNKDS,10));																		
						L_tmsTEMP = M_rstRSSET.getTimestamp("SMT_TSTDT");
						if(L_tmsTEMP != null)
							L_strTSTDT = M_fmtLCDTM.format(L_tmsTEMP);																									
						L_strTIME = L_strTSTDT.substring(11,16);
						L_stbDATA.append(padSTRING('R',L_strTIME,6));																		
						for(int j=0;j<L_vtrTSTNM.size();j++)
						{							
							L_intDECCT = Integer.parseInt(L_vtrDECCT.elementAt(j).toString());							
							L_strPRNVL = nvlSTRVL(M_rstRSSET.getString("SMT_"+(L_vtrTSTNM.elementAt(j)).toString().trim()+"VL"),"0");							
							if(setNumberFormat(Double.valueOf(L_strPRNVL).doubleValue(),3).equals("0.000"))
								L_stbDATA.append(padSTRING('L',"-",8));							
							else
								L_stbDATA.append(padSTRING('L',setNumberFormat(Double.valueOf(L_strPRNVL).doubleValue(),L_intDECCT),8));												
						}
					}
					if(nvlSTRVL(M_rstRSSET.getString("SMT_QLTFL"),"").equals("N"))
						L_stbDATA.append(padSTRING('L',"NO",8));
					else
						L_stbDATA.append(padSTRING('L',"YES",8));										
				}
				M_rstRSSET.close();				
				dosREPORT.writeBytes(L_stbDATA.toString());
				dosREPORT.writeBytes("\n"+strDOTLN);
			}			
		}	
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnRPTNK");
		}						
	}	
	/** 
	 * Method to generate the header dynamically & to fetch the data for SM TANKERS
	 * Report, for the last 24 hrs. of the given date.
	 */
	private void prnRPTKR()
	{
		strTSTTP = "0303";
		//strQCATP = "11";							
        strQCATP =M_strSBSCD.substring(2,4);
		double[] L_arrPRTVL;
		String L_strOTKLCD,L_strDSPVL ="",L_strTKLCD ="";
		String L_strCODCD,L_strCOLMN ="",L_strUOMCD="",L_strSHRDS="";
		StringBuffer L_stbDATA = new StringBuffer("");
		int L_intCOUNT = 0;				
		int L_intTNKCT = 0;
		int L_intDECCT = 0;		
		Vector<String> L_vtrTSTNM = new Vector<String>();
		Vector<String> L_vtrDECCT = new Vector<String>();
		StringBuffer L_stbTSTNM = new StringBuffer("");
		StringBuffer L_stbTSUOM = new StringBuffer(""); 				
		try
		{					
			dosREPORT.writeBytes("\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("SM TANKERS");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");			
	        								
			dosREPORT.writeBytes("\n" + strDOTLN + "\n");
			L_stbTSTNM.append(padSTRING('R',"No. of",9));			
			L_stbTSTNM.append(padSTRING('R',"From",9));
			
			L_stbTSUOM.append(padSTRING('R',"Tankers",10));
			L_stbTSUOM.append(padSTRING('R',"",8));
					
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS,CMT_NMP01,CMT_NMP02,CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'RPT'" + " AND CMT_CGSTP = 'QCXXSTQ'";;		
			M_strSQLQRY += " AND CMT_STSFL <>'X' ORDER BY CMT_NMP02";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");						
					if(!L_strCODCD.equals(""))
					{
						L_vtrTSTNM.addElement(L_strCODCD.trim()); // Proparty column name
						L_strCOLMN +=  "SMT_" + L_strCODCD.toUpperCase() +"VL"+ ",";
						
						L_strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""); // Test Proparty Name
						L_stbTSTNM.append(padSTRING('L',L_strSHRDS,8));						
												
						L_strUOMCD = hstUOMCD.get(L_strCODCD.trim()).toString();
						L_stbTSUOM.append(padSTRING('L',L_strUOMCD,8));											
						
						L_vtrDECCT.addElement(String.valueOf(M_rstRSSET.getInt("CMT_NMP01")).toString()); // No. of integers after decimal point
					}																
				}
				M_rstRSSET.close();
			}							       
			L_stbTSTNM.append(padSTRING('L',"QLTY",8));			
			L_stbTSUOM.append(padSTRING('L',"(OK)",8));
			
			dosREPORT.writeBytes(L_stbTSTNM.toString()+"\n");
			dosREPORT.writeBytes(L_stbTSUOM.toString()+"\n");
			dosREPORT.writeBytes(strDOTLN);	
														
			int L_ROWCNT =0;						
			for(int w=1;w<3;w++)
			{
				L_stbDATA.delete(0,L_stbDATA.toString().length());
				L_arrPRTVL = new double[L_vtrTSTNM.size()];				
				
				M_strSQLQRY = "Select "+L_strCOLMN+"SMT_TNKCT,SMT_TKLCD,SMT_TSTRF ,SMT_QLTFL";			
				M_strSQLQRY += " from QC_SMTRN where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SUBSTRING(SMT_TSTRF,2,1) = '1' AND SMT_TSTDT BETWEEN";
				M_strSQLQRY += " '" + strFMDAT.trim()+ "' AND '" + strTODAT.trim()+ "' AND SMT_STSFL <> 'X'";							
				if(w ==1)
					M_strSQLQRY += " and SMT_QLTFL ='Y' ORDER BY SMT_TKLCD";	
				else
					M_strSQLQRY += " and SMT_QLTFL ='N' ORDER BY SMT_TKLCD";					
				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);															
				if(M_rstRSSET != null)				
				{				
					L_intCOUNT = 0;
					L_strOTKLCD = "";
					L_intTNKCT = 0;										
					while(M_rstRSSET.next())
					{
						L_strTKLCD="";
						L_strTKLCD = nvlSTRVL(M_rstRSSET.getString("SMT_TKLCD"),"");																	
						
						if(L_intCOUNT == 0)										
							L_strOTKLCD = L_strTKLCD;											
						
						if(L_strTKLCD.equals(L_strOTKLCD))					
						{
							L_intTNKCT += M_rstRSSET.getInt("SMT_TNKCT");	
							for(int p=0;p<L_vtrTSTNM.size();p++)
							{
								L_strCODCD ="SMT_"+L_vtrTSTNM.elementAt(p).toString().trim()+"VL";							
								L_arrPRTVL[p] += Double.valueOf(nvlSTRVL(M_rstRSSET.getString(L_strCODCD.trim()),"0")).doubleValue();							
							}
							L_intCOUNT++;						
						}
						else// Requried testing if raw material received from 2 diff. locations
						{										
							L_stbDATA.append(padSTRING('R',String.valueOf(L_intTNKCT).trim(),9)+padSTRING('R',L_strOTKLCD.trim(),9));
							for(int i=0;i<L_vtrTSTNM.size();i++)
							{
								L_intDECCT = Integer.parseInt(L_vtrDECCT.elementAt(i).toString());														
								if(setNumberFormat(L_arrPRTVL[i]/L_intCOUNT,L_intDECCT) == setNumberFormat(Double.valueOf("0").doubleValue(),L_intDECCT))
									L_strDSPVL = "-";
								else 							
									L_strDSPVL =setNumberFormat(L_arrPRTVL[i]/L_intCOUNT,L_intDECCT);								
								L_stbDATA.append(padSTRING('L',L_strDSPVL,8));															
							}
							if(M_rstRSSET.getString("SMT_QLTFL").equals("Y"))
								L_stbDATA.append(padSTRING('R',"YES",5));
							else
								L_stbDATA.append(padSTRING('R',"-",5));				        		                
							dosREPORT.writeBytes("\n" + L_stbDATA.toString());
							
							L_stbDATA.delete(0,L_stbDATA.toString().length());
							// for the initialisation of variables when new Location found.
							L_intTNKCT = 0;
							L_intTNKCT = M_rstRSSET.getInt("SMT_TNKCT");	
							for(int l=0;l<L_vtrTSTNM.size();l++)
							{
								L_strCODCD = "SMT_"+L_vtrTSTNM.elementAt(l).toString().trim()+"VL";
								L_arrPRTVL[l] = M_rstRSSET.getDouble(L_strCODCD.trim());
							}
							L_intCOUNT = 1;
						}					
						L_strOTKLCD = L_strTKLCD;					
					}
					M_rstRSSET.close();					
				}			
				
				if(L_intCOUNT >0)
				{
					if(w==1)
						dosREPORT.writeBytes("\nAverage Quality (Within Specifications)\n");
					else
						dosREPORT.writeBytes("\n\nTankers(Out Of Specifications)\n");
					L_stbDATA.append(padSTRING('R',String.valueOf(L_intTNKCT),9)+padSTRING('R',L_strTKLCD,9));				
					for(int i=0;i<L_vtrTSTNM.size();i++)
					{					
						L_intDECCT = Integer.parseInt(L_vtrDECCT.elementAt(i).toString());					
						if(L_arrPRTVL[i] == 0.000)
							L_stbDATA.append(padSTRING('L',"-",8));
						else
						{				
							double L_intTEMP = (L_arrPRTVL[i]/L_intCOUNT);													
							if(setNumberFormat(L_intTEMP,L_intDECCT) == setNumberFormat(Double.valueOf("0").doubleValue(),L_intDECCT))
								L_stbDATA.append(padSTRING('L',"-",8));
							else 						
								L_stbDATA.append(padSTRING('L',setNumberFormat(L_intTEMP,L_intDECCT),8));						
						}										
					}
					if(w==1)
						L_stbDATA.append("    "+"YES");
					else 
						L_stbDATA.append("    "+"NO");
					dosREPORT.writeBytes("\n"+L_stbDATA.toString());
				}								
			}
			dosREPORT.writeBytes("\n"+strDOTLN);						
		}			
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnRPTKR");
		}
	}	
	/** 
	 * Method to generate the header dynamically & to fetch the data for FO/MO TANKERS
	 * Report, for the last 24 hrs. of the given date.
	 */
	private void prnRPFMT()
	{
		strTSTTP = "0301";
        //strQCATP = "11";
        strQCATP =M_strSBSCD.substring(2,4);
		String L_strCODCD="";
		String L_strPRNVL,L_strTIME="",L_strUOMCD="",L_strSHRDS="",L_strCOLMN ="";
		String L_strVEHNO="",L_strPRDCD="";
		String L_strTSTDT="",L_strTSTRF="",L_strMATDS ="";
		int L_intDECCT = 0;																							
		Vector<String> L_vtrTSTNM = new Vector<String>();
		Vector<String> L_vtrDECCT = new Vector<String>();
		StringBuffer L_stbTSTNM = new StringBuffer("");
		StringBuffer L_stbTSUOM = new StringBuffer(""); 
		StringBuffer L_stbDATA = new StringBuffer("");								
		ResultSet L_rstRSSET;
		String L_strSQLQRY="";
		try
		{			
			dosREPORT.writeBytes("\n\n\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<b>");
			dosREPORT.writeBytes("FO/MO TANKERS");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))											
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</b>");			
	        								
			dosREPORT.writeBytes("\n" + strDOTLN + "\n");				        
			L_stbTSTNM.append(padSTRING('R',"Time",12));			
			L_stbTSTNM.append(padSTRING('R',"Tanker No.",16));
			L_stbTSTNM.append(padSTRING('R',"Material Desc.",15));
						
			L_stbTSUOM.append(padSTRING('R',"Hrs",12));
			L_stbTSUOM.append(padSTRING('R',"",31));
					
			M_strSQLQRY = "Select CMT_CODCD,CMT_SHRDS,CMT_NMP01,CMT_NMP02,CMT_CHP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'RPT'" + " AND CMT_CGSTP = 'QCXXFOM'";;		
			M_strSQLQRY += " AND CMT_STSFL <>'X' ORDER BY CMT_NMP02";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");						
					if(!L_strCODCD.equals(""))
					{
						L_vtrTSTNM.addElement(L_strCODCD.trim()); // Proparty column name
						L_strCOLMN +=  "SMT_" + L_strCODCD.toUpperCase() +"VL"+ ",";
						
						L_strSHRDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""); // Test Proparty Name
						L_stbTSTNM.append(padSTRING('L',L_strSHRDS,16));						
												
						L_strUOMCD = hstUOMCD.get(L_strCODCD.trim()).toString();
						L_stbTSUOM.append(padSTRING('L',L_strUOMCD,16));											
						
						L_vtrDECCT.addElement(String.valueOf(M_rstRSSET.getInt("CMT_NMP01")).toString()); // No. of integers after decimal point
					}																
				}
				M_rstRSSET.close();
			}							       		
			dosREPORT.writeBytes(L_stbTSTNM.toString()+"\n");
			dosREPORT.writeBytes(L_stbTSUOM.toString()+"\n");
			dosREPORT.writeBytes(strDOTLN);				
			
			M_strSQLQRY = "Select " +L_strCOLMN+ "SMT_TSTDT,SMT_TSTRF,SMT_PRDCD,CT_MATDS FROM QC_SMTRN,co_ctmst WHERE SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_PRDCD = CT_MATCD AND SMT_QCATP =";
			M_strSQLQRY += "'"+strQCATP +"'" + " AND SMT_TSTTP = "+ "'"+ strTSTTP.trim() +"'";
			M_strSQLQRY += " AND SMT_TSTDT BETWEEN '" + strFMDAT.trim() + "' AND '"+strTODAT.trim();
			M_strSQLQRY += "' AND SMT_STSFL <> 'X'"; 
			M_strSQLQRY += " ORDER BY SMT_TSTDT,SMT_TSTRF ";			
		    M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		    if(M_rstRSSET != null)			
			{
				int cnt = 0;
				Timestamp L_tmsTEMP;
				while(M_rstRSSET.next())
				{					
					L_tmsTEMP = M_rstRSSET.getTimestamp("SMT_TSTDT");
					if(L_tmsTEMP != null)
						L_strTSTDT = M_fmtLCDTM.format(L_tmsTEMP);																									
					L_strTIME = L_strTSTDT.substring(11,16);
					L_stbDATA.append("\n"+padSTRING('R',L_strTIME,12));
					
					L_strTSTRF = nvlSTRVL(M_rstRSSET.getString("SMT_TSTRF"),"");	
					L_strVEHNO = " ";					
					if(!L_strTSTRF.equals(""))
					{
						L_strTSTRF = L_strTSTRF.trim();
						L_strSQLQRY = " Select RM_REMDS from QC_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '";
						L_strSQLQRY += strQCATP + "'" + " AND RM_TSTTP = 'VEH'"+" AND RM_TSTNO = '"+L_strTSTRF.trim()+"'";						
						L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
						if(L_rstRSSET !=null)
						{
							if(L_rstRSSET.next())
							{								
								L_strVEHNO = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");								
								L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("SMT_PRDCD"),"");																								
								L_strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");																												
							}
							L_rstRSSET.close();
						}
					}									
					L_strVEHNO = padSTRING('R',L_strVEHNO.trim(),16);
					L_strMATDS = padSTRING('R',L_strMATDS.trim(),15);										
					L_stbDATA.append(L_strVEHNO);
					L_stbDATA.append(L_strMATDS);					
					
					for(int j=0;j<L_vtrTSTNM.size();j++)
					{						
						L_intDECCT = Integer.parseInt(L_vtrDECCT.elementAt(j).toString());
						L_strPRNVL = nvlSTRVL(M_rstRSSET.getString("SMT_"+L_vtrTSTNM.elementAt(j).toString().trim()+"VL"),"0");
						if(setNumberFormat(Double.valueOf(L_strPRNVL).doubleValue(),3).equals("0.000") )
							L_stbDATA.append(padSTRING('L',"-",16));
						else
							L_stbDATA.append(padSTRING('L',setNumberFormat(Double.valueOf(L_strPRNVL).doubleValue(),L_intDECCT),16));												
					}										
					cnt++;
				}
				if(cnt==0)
				{								
					L_stbDATA.append("\n"+padSTRING('R',"-",12));
					L_stbDATA.append(padSTRING('R',"-",16));
					L_stbDATA.append(padSTRING('R',"-",15));	
					for(int j=0;j<L_vtrTSTNM.size();j++)
					{
						L_stbDATA.append(padSTRING('L',"-",16));
					}					
				}
				dosREPORT.writeBytes(L_stbDATA.toString());
				dosREPORT.writeBytes("\n"+strDOTLN);
				M_rstRSSET.close();
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnRPFMT");
		}
	}
	/**
	 * Method to update the Remarks if the old remarks are modified.
	 * @param P_strRPTNM String argument to pass Report name.
	 * @param P_strRPTDT String to pass Report Date.
	 * @param P_strORMKDS String to Pass the remarks fetched from data base.
	 * @param P_strRMRK String to pass the current remarks from remark text box.
	 * @param P_strRMSRL String to pass the serial number of the Remark.
	 */		
	public void updRPTRM(String P_strRPTNM,String P_strRPTDT,String P_strRMSRL,String P_strRMRK,String P_strORMKDS)
	{		 				
		try
		{											
			if(!P_strORMKDS.equals(P_strRMRK))
			{
				if(P_strRMRK.trim().length() > 0)
				{
		   			M_strSQLQRY = "Update QC_RMMST set ";
					M_strSQLQRY += " RM_REMDS = '" + P_strRMRK.trim()+"',";
					M_strSQLQRY += " RM_LUSBY = '" + cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += " RM_LUPDT = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) + "'";
				}
				else 
					M_strSQLQRY = "Delete from QC_RMMST ";
				
				M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP = '"+ M_strSBSCD.substring(2,4).trim() +"'";
				M_strSQLQRY += " AND RM_TSTTP = "+ "'"+P_strRPTNM.substring(5).trim()+P_strRMSRL.trim()+"'";
				M_strSQLQRY += " AND RM_TSTNO = "+ "'"+P_strRPTDT.substring(0,2)+P_strRPTDT.substring(3,5)+P_strRPTDT.substring(6)+"'";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updRPTRM");
		}
	}
	/**
	 * Method to insert the new Remarks if remarks arenot entered in the dta base.
	 * @param P_strRPTNM String argument to pass Report name.
	 * @param P_strRPTDT String to pass Report Date.
	 * @param P_strORMKDS String to Pass the remarks fetched from data base.
	 * @param P_strRMRK String to pass the current remarks from remark text box.
	 * @param P_strRMSRL String to pass the serial number of the Remark.
	 */
	public void insRPTRM(String P_strRPTNM,String P_strRPTDT,String P_strRMSRL,String P_strRMRK,String P_strORMKDS)
	{		 				
		String L_strQCATP = M_strSBSCD.substring(2,4);
		try
		{
			if(!P_strORMKDS.equals(P_strRMRK))
			{				
				M_strSQLQRY = "Insert into QC_RMMST(RM_QCATP,RM_TSTTP,RM_TSTNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)values(";
				M_strSQLQRY +="'"+L_strQCATP+"',";
				M_strSQLQRY +="'"+P_strRPTNM.substring(5).trim()+P_strRMSRL.trim()+"',"	;
				M_strSQLQRY +="'"+P_strRPTDT.substring(0,2)+P_strRPTDT.substring(3,5)+P_strRPTDT.substring(6)+"',";
				M_strSQLQRY +="'"+P_strRMRK.trim() +"',";
				M_strSQLQRY += getUSGDTL("LP",'I',"") + ")";				
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");								
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"insRPTRM");	
		}
	}	
}