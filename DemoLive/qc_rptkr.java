/**
System Name   : Laboratory Information Management System
Program Name  : Tanker Register
Program Desc. : Report for taking the Tanker details				
Author        : Mr.S.R.Mehesare
Date          : 21/05/2005
Version       : LIMS V2.0.0
Modificaitons 
Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : 
*/

import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;
/**<pre>
System Name : Laboratory Information Management System.
 
Program Name : Tanker Register

Purpose : Report for taking the Tanker details.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
QC_SMTRN       SMT_QCATP,SMT_TSTTP,SMT_TSTNO,
               SMT_MORTP,SMT_TSTDT,SMT_TSTRF                           #

QC_RMMST       RM_QCATP,RM_TSTTP, RM_TSTNO                             #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtDATE     SMT_TSTDT      QC_SMTRN      DATE          Test Date 
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
    "SMT_"+ Vector.elementAt(i)+"VL" i.e SMT_COLVL, SMT_TOLVL
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	
<I>
Tables Used For Query :</b> 1)QC_SMTRN                      
                        2)QC_RMMST     
                        3)CO_CDTRN

<B>Conditions Give in Query:</b>
    Data is taken from QC_SMTRN & QC_RMMST for given Test Date
       1) RM_QCATP =SMT_QCATP 
       2) AND RM_TSTTP ='VEH' 
       3) AND RM_TSTNO = SMT_TSTRF 
       4) AND SMT_TSTDT BETWEEN given date & one day before given date
       5) AND SMT_TSTTP='0303' "
   - Test parameters are fected from QC_SMTRN where smt_TSTTP = '0303' and smt_tstdt 
     between the given range.
   - Tanker numbers are fecthed from qc_rmmst where rm_tsttp ='VEh' 
     and rm_tstno = smt_tstno 
       
<B>Validations :</B>
    - Date must be valid. i.e.smaller than current date.       
</I> */

public class qc_rptkr extends cl_rbase
{										/** JTextField to enter & display Date.*/
	private JTextField txtDATE;			/** String variable for day ending Time*/
	private String strTIME = "07:00";	/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;	/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	/** String variable for generated report file name.*/    
	private String strFILNM;			/**	Vector to hold Short description of different properties.*/
	private Vector<String> vtrQPRDS;			/**	Vector to hold Codes associated with the different properties.*/
	private Vector<String> vtrQPRLS;			/**	Vector to hold Unit of measurement associated with the each Property.*/
	private Vector<String> vtrQPUOM;			/**	Vector to hold Vehicle number associate with each Lot Number.*/
	private Vector<String> vtrVEHNO;    		/**	String variable for "OK" result of the quality check.*/
	private String strOK ="0";			/**	String variable for "NOTOK" result of the quality check.*/
	private String strNOTOK ="9";		/** StringBuffer to hold header of the report.*/
	private String strHEADR = "";		/** StringBuffer to hold generated dotted Line.*/
	private StringBuffer stbDOTLN = new StringBuffer("-") ;	
										/** Integer array to specify the number of Digits after decimal point.*/
    private int arrCOLWID[];			/** Integer array for column description.*/
	private int arrCOLDEC[];       		/** Integer variable to count the max length (in charectors) of a row.*/
	private int intROWLN = 0;			/** Integer variable to count the number of records fetched.*/
	private int intRECCT;				/** Flag for vehicle status.*/	
	private boolean flgVEHST = false;
	private Hashtable<String,String> hstUOMCD;
	private Hashtable<String,String> hstSHRDS;
	qc_rptkr()
	{
		super(2);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Date"),4,4,1,.4,this,'R');
			add(txtDATE = new TxtDate(),4,5,1,1,this,'L');	
			
			vtrQPRDS = new  Vector<String>();
			vtrQPRLS = new Vector<String>();
			vtrQPUOM = new Vector<String>();
			vtrVEHNO = new  Vector<String>();			
			hstUOMCD = new Hashtable<String,String>();
			hstSHRDS = new Hashtable<String,String>();			
			
			M_strSQLQRY = "Select QS_QPRCD,QS_SHRDS,QS_UOMCD,QS_TSMCD from CO_QSMST where isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
					{
						hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));
						hstSHRDS.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_SHRDS"),""));					
					}
				}				
				M_rstRSSET.close();
			}													
			getTSTDET();
			M_pnlRPFMT.setVisible(true);
			setENBL(false);			
		}		
		catch(Exception L_EX)
		{
		setMSG(L_EX, "Constructor");
		}
	}		
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter the Date to generate the Report..",'N');
				txtDATE.requestFocus();
				setENBL(true);
				txtDATE.setText(cl_dat.M_strLOGDT_pbst);
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
			vtrVEHNO.removeAllElements();						
		}
		else if(M_objSOURC == txtDATE)
		{
			if (txtDATE.getText().trim().length()>0)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("Press SpaceBar to generate the Report..",'N');
			}
			else			
				setMSG("Please Enter Date to generate the Report..",'N');
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
			if(M_rdbHTML.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rptkr.html";
			else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rptkr.doc";							
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Tanker Register"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
    *Method to fetch data from Database & club it with Header & footer in Data Output Stream.
	*/
	private void getDATA()
	{ 				
		double L_arrAVGVL [] = new double[vtrQPRDS.size()];  
		String L_strFMDAT, L_strTODAT,L_strTSTDT="";
		String L_strQLTFL ="",L_strLOTNO="",L_strQPRCD="";
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
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Tanker Register</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}						
			prnHEADER();
			L_strTODAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(txtDATE.getText().trim()+" 07:00"));
			try
			{				
				M_calLOCAL.setTime(M_fmtLCDAT.parse(txtDATE.getText().trim()));
				M_calLOCAL.add(java.util.Calendar.DATE,-1);																       			
				L_strFMDAT = M_fmtDBDTM.format(M_fmtLCDTM.parse(M_fmtLCDAT.format(M_calLOCAL.getTime()) +" 07:00"));
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX, "calRQDAT");
				return;
			}											
			M_strSQLQRY = "Select SMT_TSTRF,SMT_TKLCD,SMT_TSTDT,SMT_TNKCT,SMT_QLTVL,RM_REMDS,";
			M_strSQLQRY += "SMT_"+((String)vtrQPRLS.elementAt(0)).trim()+"VL";			
			for(int i=1;i<vtrQPRLS.size();i++)
			{				
				M_strSQLQRY +=" ,"+"SMT_"+((String)vtrQPRLS.elementAt(i)).trim()+"VL";
			}
			M_strSQLQRY += " from QC_SMTRN,QC_RMMST where RM_CMPCD = SMT_CMPCD and RM_QCATP =SMT_QCATP and RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TSTTP ='VEH' and RM_TSTNO = SMT_TSTRF and SMT_TSTDT ";
			M_strSQLQRY += " BETWEEN '" +L_strFMDAT+ "' AND '" + L_strTODAT;
			M_strSQLQRY += "' AND SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_TSTTP='0303' ";
			M_strSQLQRY += " AND isnull(SMT_STSFL,'') <> 'X' AND isnull(RM_STSFL,'') <>'X'";
			M_strSQLQRY += " ORDER BY SMT_TKLCD,SMT_TSTDT";									
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET != null)				
			{
				while(M_rstRSSET.next())
				{
					intRECCT+=1;
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("SMT_TSTRF"),"");
					StringBuffer stbDATA = new StringBuffer((padSTRING('R',L_strLOTNO,10)));
					
					L_tmsTSTDT = M_rstRSSET.getTimestamp("SMT_TSTDT");
					if(L_tmsTSTDT != null)
						L_strTSTDT= M_fmtLCDTM.format(L_tmsTSTDT);
					if(L_strTSTDT!=null)
					{
						stbDATA.append((padSTRING('L',L_strTSTDT.trim().substring(0,10),10)));
						stbDATA.append((padSTRING('L',L_strTSTDT.trim().substring(11,16),7)));
					}				
					for(int j=0;j<vtrQPRLS.size();j++)
					{
						L_strQPRCD = "SMT_" + ((String)(vtrQPRLS.elementAt(j))).trim()+ "VL";
						L_strQPRCD = M_rstRSSET.getString(L_strQPRCD);
						if(L_strQPRCD!= null && L_strQPRCD.trim().length()>0)
						{
							if(Double.valueOf(L_strQPRCD.trim()).doubleValue()!=0.0)
							{
								L_arrAVGVL[j] += Double.valueOf(L_strQPRCD.trim()).doubleValue();														
								stbDATA.append(padSTRING('L',setNumberFormat(Double.valueOf(L_strQPRCD.trim()).doubleValue(),arrCOLDEC[j]),7));
						    }	
							else
							{
								stbDATA.append(padSTRING('L',"-",7));
								L_arrAVGVL[j]+= 0;							
							}	
						}
						else
						{
							stbDATA.append(padSTRING('L',"-",7));
							L_arrAVGVL[j]+=0;
						}	
					}
					stbDATA.append(padSTRING('L',M_rstRSSET.getString("SMT_TNKCT"),5));
					L_strQLTFL = M_rstRSSET.getString("SMT_QLTVL");
					if(L_strQLTFL == null)
						L_strQLTFL ="";
					else if(L_strQLTFL.equals(strOK))
						L_strQLTFL ="YES";
					else if(L_strQLTFL.equals(strNOTOK))
						L_strQLTFL ="NO";
					stbDATA.append(padSTRING('L'," ",4));
					stbDATA.append(padSTRING('R',L_strQLTFL,4));
					String L_stbVEHNO = M_rstRSSET.getString("RM_REMDS");
					if(L_stbVEHNO!=null && L_stbVEHNO.trim().length()>0)
					{
						vtrVEHNO.addElement(L_strLOTNO + "*" + L_stbVEHNO.trim());
						flgVEHST = true;
					}					
					dosREPORT.writeBytes(stbDATA.toString());
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst += 1;
					if(cl_dat.M_intLINNO_pbst> 64)
					{
						dosREPORT.writeBytes("\n");									
						dosREPORT.writeBytes(stbDOTLN.toString());
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
				}
				M_rstRSSET.close();
			}			
			StringBuffer L_strAVGVAL = new StringBuffer(padSTRING('L'," ",20));
			L_strAVGVAL.append(padSTRING('R',"Average",7));
			if(intRECCT!=0)
			{
				for(int k=0;k<L_arrAVGVL.length;k++)				
					L_strAVGVAL.append(padSTRING('L',setNumberFormat((L_arrAVGVL[k]/intRECCT),arrCOLDEC[k]),7));				
			}							
			dosREPORT.writeBytes(stbDOTLN.toString());
			dosREPORT.writeBytes(L_strAVGVAL.toString());
			dosREPORT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst += 1;
			if(cl_dat.M_intLINNO_pbst> 64)
			{
				dosREPORT.writeBytes("\n");								
				dosREPORT.writeBytes(stbDOTLN.toString());
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strEJT);
				prnHEADER();
			}
			if(flgVEHST)
			{
				String L_strTEMP = "";				
				StringBuffer L_stbVEHNO = new StringBuffer(padSTRING('R',"Lot No",10));
				L_stbVEHNO.append(padSTRING('R',"Vehical No.",80));				
				L_stbVEHNO.append("\n-------------------------\n");
				cl_dat.M_intLINNO_pbst += 2;
				for(int k=0;k<vtrVEHNO.size();k++)
				{
					StringTokenizer st = new StringTokenizer(((String)vtrVEHNO.elementAt(k)).trim(),"*");
					while(st.hasMoreTokens())
					{						
						L_stbVEHNO.append(padSTRING('R',st.nextToken(),10));
						L_strTEMP = st.nextToken();
						if(L_strTEMP.length() > stbDOTLN.length())
						{
							L_stbVEHNO.append(padSTRING('R',L_strTEMP.substring(0,stbDOTLN.length()),stbDOTLN.length()));
							L_stbVEHNO.append("\n            "+padSTRING('R',L_strTEMP.substring(stbDOTLN.length()),stbDOTLN.length()));
						}
						else 							
							L_stbVEHNO.append(padSTRING('R',L_strTEMP,stbDOTLN.length()));
						L_stbVEHNO.append("\n");
						cl_dat.M_intLINNO_pbst += 1;						
					}	
				}				
				dosREPORT.writeBytes(stbDOTLN + "\n");
				dosREPORT.writeBytes(L_stbVEHNO.toString());
				dosREPORT.writeBytes("\n");
				cl_dat.M_intLINNO_pbst += 1;				
			}							
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");			
			dosREPORT.close();			
			fosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	Method to validate Consignment number, before execution, Check for blank and wrong Input.
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
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;
			if (cl_dat.M_PAGENO == 1)
			{					
				StringBuffer L_stbHEADR = new StringBuffer(padSTRING('R',"Lot No.",10));
				L_stbHEADR.append(padSTRING('R',"Parameters",17));
				for(int i=0;i<vtrQPRDS.size();i++)
					L_stbHEADR.append(padSTRING('L',((String)vtrQPRDS.elementAt(i)).trim(),7));
				L_stbHEADR.append(padSTRING('L',"COUNT",7));
				L_stbHEADR.append(padSTRING('L',"QLTY",6)+"\n");								
				L_stbHEADR.append(padSTRING('R'," ",10));
				L_stbHEADR.append(padSTRING('R',"DATE ",10));
				intROWLN = 20;
				L_stbHEADR.append(padSTRING('L',"TIME ",7));
				intROWLN +=7;
				for(int i=0;i<vtrQPUOM.size();i++)			
				{
					L_stbHEADR.append(padSTRING('L',((String)vtrQPUOM.elementAt(i)).trim(),7));			
					intROWLN += 7;
				}
				L_stbHEADR.append(padSTRING('L'," ",7));			
				L_stbHEADR.append(padSTRING('L',"(OK)",6)+"\n");			
				intROWLN += 13;								
				for(int k=0; k < intROWLN;k++)
					stbDOTLN.append("-");
				stbDOTLN.append("\n");
				strHEADR = String.valueOf(stbDOTLN).toString();
				strHEADR += String.valueOf(L_stbHEADR).toString();
				strHEADR += String.valueOf(stbDOTLN).toString();
			}
			dosREPORT.writeBytes("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,intROWLN - 24));
			dosREPORT.writeBytes(padSTRING('L',"Report Date : " + cl_dat.M_strLOGDT_pbst,24) + "\n");			
			dosREPORT.writeBytes(padSTRING('R',"Styrene Tanker Analysis ",intROWLN - 24));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
			dosREPORT.writeBytes("Day ending at " + strTIME +" hrs on Date: "+txtDATE.getText().trim() +"\n");
			dosREPORT.writeBytes(strHEADR.toString());								
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	 * Method to get the Quality Parameter List.
	 */
	private void getTSTDET()
	{
		try
		{			
			int L_intROWCT;
			String L_strSQLQRY="";
			vtrQPRLS.removeAllElements();
			vtrQPRDS.removeAllElements();
			vtrQPUOM.removeAllElements();			
			L_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,CMT_NMP02,CMT_NMP01 from CO_CDTRN where";
			L_strSQLQRY += " CMT_CGMTP ='RPT' and CMT_CGSTP ='QCXXTFM' order by CMT_NCSVL";			
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,   ResultSet.CONCUR_UPDATABLE);		
			M_rstRSSET = L_stat.executeQuery(L_strSQLQRY);			
			if(M_rstRSSET != null)
			{				
				L_intROWCT=0;
				while(M_rstRSSET.next())
					L_intROWCT++;					
				if(L_intROWCT >0)
				{
					arrCOLWID = new int[L_intROWCT]; 
					arrCOLDEC = new int[L_intROWCT];
				}					
				M_rstRSSET.beforeFirst();
				int i=0;
				while(M_rstRSSET.next())										
				{													
					String L_strQPRCD = M_rstRSSET.getString("CMT_CODCD").trim();
					arrCOLDEC[i]= M_rstRSSET.getInt("CMT_NMP02");					
					arrCOLWID[i]= M_rstRSSET.getInt("CMT_NMP01");
					vtrQPRLS.addElement(L_strQPRCD);
					//vtrQPRDS.addElement((cl_dat.getPRMCOD("CMT_SHRDS","SYS","QCXXQPR",L_strQPRCD)).trim());
					vtrQPRDS.addElement(hstUOMCD.get(L_strQPRCD).toString());						
				    //vtrQPUOM.addElement((cl_dat.getPRMCOD("CMT_CCSVL","SYS","QCXXQPR",L_strQPRCD)).trim());
					vtrQPUOM.addElement(hstSHRDS.get(L_strQPRCD).toString());
					i+=1;					
				}				
				M_rstRSSET.close();      
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getTSTDET");
		}		
	}
}
