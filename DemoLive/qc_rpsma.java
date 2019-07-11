/**
System Name   : Laboratory Information Management System
Program Name  : Styrene Monomer Sample Analysis Report.
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 26/08/2005
Version       : LIMS V2.0.0
Modificaitons 
Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : 
*/
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;
import java.util.Hashtable;import java.util.Vector;
import javax.swing.JComponent;import javax.swing.InputVerifier;
/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Styrene Monomer Sample Analysis Report.

Purpose : 
List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
QC_SMTRN       SMT_QCATP,SMT_TSTTP,SMT_TSTNO,
               SMT_MORTP,SMT_TSTDT,SMT_TSTRF                           #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtCONNO     SMT_TSTRF      QC_SMTRN      DATE         Test Referance 
--------------------------------------------------------------------------------------
<B>
Logic</B>
Each grade (finshed product) has some Quality Parameters & these parameters changes as per 
new standards adopted. These parameters are changing from grade to grade.
System maintains these parameters for each grade & makes latest parameters available.
According to the new standards, some new quality parameters are added and some unwanted
are removed for specific grade.
Hence to generate report every time list of parameters are generated dynamically as :-
   1) Latest quality parameters list is fetched from the database for given grade .
   2) These Quality Parameters are maintained in the Vector.
   3) List of Columns to fetch from the database is generated dynamically as 
    "SMT_"+ Vector.elementAt(i)+"VL" i.e SMT_COLVL, SMT_TOLVL
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	
<I>
Tables Used For Query :</b> 1)QC_SMTRN                                             
                        2)CO_CDTRN

<B>Conditions Give in Query:</b>
Quality Parameters are taken from CO_CDTRN for condiations
   1) CMT_CGMTP = 'RPT'
   2) AND CMT_CGSTP = 'QCXXSMA'
   3) AND isnull(CMT_STSFL,'') <>'X' 
Unit of Measurement is taken from CO_QSMST for given Quality Parameter Code.
 
Suplier Name & Vessel Name is taken from MM_BETRN,MM_POMST,CO_PTMST for condiations
    1) BE_STRTP = '04'"
    2) AND PO_STRTP ='04' 
    3) AND PT_PRTTP = 'S'"
	4) AND PO_VENCD = PT_PRTCD 
    5) AND BE_PORNO = PO_PORNO
    6) AND BE_CONNO = given Consignment Number.

Report Data is taken from QC_SMTRN for given condiations as
     1) SMT_QCATP = '11'
     2) AND SMT_TSTTP = '0304';			
     3) AND isnull(SMT_STSFL,'') <> 'X' 
     4) AND SMT_TSTRF = given Consignment Number
       
<B>Validations :</B>
    - consignment Number must be valid.
</I> */

public class qc_rpsma extends cl_rbase
{											/** JTextField to enter & display Consignment Number.*/
	private JTextField txtCONNO;			/** File Output Stream for File Handleing.*/
	private FileOutputStream fosREPORT;		/** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;		/** String variable for generated report file name.*/    
	private String strFILNM;				/** Integer variable to count the number of records fetched.*/
	private int intRECCT;					/** StringBuffer to hold generated dotted Line.*/
	private StringBuffer stbDOTLN;			/** String variable for Consignment Number.*/
	private String strCONNO;				/** String variable for Report Column Names.*/
	private String strCOLNM="";				/** String variable for Test Type.*/
	private String strTSTTP ="0304";			
	private INPVF objINPVR = new INPVF();	/** Hashtable for Unit of Measurement & Quality Parameter.*/
	private Hashtable<String,String> hstUOMCD;				/** Hashtable for Tank Referance Code & Description.*/
	private Hashtable<String,String> hstTNKRF;				/**	Vector to hold Quality parameter Code.*/
	private Vector<String> vtrQPRCD;				/** Integer variable to count the max length (in charectors) of a Row.*/
	private int intROWLN;	
	qc_rpsma()
	{
		super(2);
	    try
	    {			
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		    add(new JLabel("Consignment No."),4,4,1,1.2,this,'R');
			add(txtCONNO = new TxtLimit(10),4,5,1,1,this,'R');			
			
			vtrQPRCD = new  Vector<String>();						
			txtCONNO.setInputVerifier(objINPVR);
			
			M_pnlRPFMT.setVisible(true);
			stbDOTLN = new StringBuffer("-") ;
			hstUOMCD = new Hashtable<String,String>();
			hstTNKRF = new Hashtable<String,String>();
						
			M_strSQLQRY = "Select CMT_CGMTP,CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP IN('SYSQCXXMOR','RPTQCXXSMA')";;		
			M_strSQLQRY += " AND isnull(CMT_STSFL,'') <>'X' ORDER BY CMT_CGMTP,CMT_NMP02";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{				
				String L_strCODCD = "";				
				while(M_rstRSSET.next())
				{					
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					
                    if(M_rstRSSET.getString("CMT_CGMTP").equals("RPT"))
					{
						vtrQPRCD.addElement(L_strCODCD.trim()); // Proparty column name
						strCOLNM +=  "SMT_" + L_strCODCD.toUpperCase() +"VL"+ ",";						
					}
                    else
                    {
                        if(!L_strCODCD.equals(""))				
						    hstTNKRF.put(L_strCODCD,M_rstRSSET.getString("CMT_CODDS"));
                    }
				}
				M_rstRSSET.close();
			}
		/*	M_strSQLQRY = "Select CMT_CODCD,CMT_NMP02 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'RPT'" + " AND CMT_CGSTP = 'QCXXSMA'";;		
			M_strSQLQRY += " AND isnull(CMT_STSFL,'') <>'X' ORDER BY CMT_NMP02";						
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strCODCD = "";				
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");										
					if(!L_strCODCD.equals(""))
					{
						vtrQPRCD.addElement(L_strCODCD.trim()); // Proparty column name
						strCOLNM +=  "SMT_" + L_strCODCD.toUpperCase() +"VL"+ ",";						
					}
				}
			}	*/								
			M_strSQLQRY = "Select QS_QPRCD,QS_SHRDS,QS_UOMCD from CO_QSMST where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="",L_strUOMCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
						hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));						
				}
                M_rstRSSET.close();
			}						
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
				txtCONNO.requestFocus();
				setENBL(true);
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}		
		else if(M_objSOURC == txtCONNO)
		{
			if (txtCONNO.getText().trim().length()>0)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
				setMSG("Press SpaceBar to generate the Report..",'N');
			}
			else			
				setMSG("Please Enter Consignment number, to generate the Report..",'N');
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);			
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC == txtCONNO)	
				{					
					M_strHLPFLD = "txtCONNO";
					cl_dat.M_flgHELPFL_pbst = true;										
					String L_ARRHDR[] = {"Consignment Number","Description","Date"};
					M_strSQLQRY = "select distinct BE_CONNO,BE_CONDS,BE_CONDT from MM_BETRN where"
						+" BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_STRTP ='04' ";
                    if(txtCONNO.getText().length() >0)    
                        M_strSQLQRY +=" AND BE_CONNO like '"+txtCONNO.getText().trim()+"'";
                    M_strSQLQRY +=" order by BE_CONDT desc";					
					cl_hlp	(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");					
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"VK_F1");
		}
	}
	/**
	 * Super Class Method overrided here to execute help for F1 key press.	 
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtCONNO"))			
				txtCONNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
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
			    strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rpsma.html";
			else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpsma.doc";				
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Styrene Monomer Sample Analysis Report"," ");
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
    *Method to fetch data from MM_GRMST,CO_CTMST & MM_BETRN tables & club it with Header &
    *footer in Data Output Stream.
	*/
	private void getDATA()
	{ 				
		String L_strTNKRF="";
		StringBuffer L_stbDATA = new StringBuffer();		
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Styrene Monomer Sample Analysis Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}						
			prnHEADER();			
												
			M_strSQLQRY = "Select "+ strCOLNM +"SMT_MORTP FROM QC_SMTRN WHERE SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP = '"+M_strSBSCD.substring(2,4).trim()+"'"
			            + " AND SMT_TSTTP = "+ "'"+ strTSTTP +"'"
			            +  " AND isnull(SMT_STSFL,'') <> 'X' AND SMT_TSTRF='"+ strCONNO +"' ORDER BY SMT_TSTRF ";									
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(M_rstRSSET != null)					
			{				
				String strTEMP="";
				while(M_rstRSSET.next())
				{								
					L_strTNKRF = M_rstRSSET.getString("SMT_MORTP");																
					L_stbDATA.append("\n"+padSTRING('R',hstTNKRF.get(L_strTNKRF).toString(),23));
					for(int i=0;i<vtrQPRCD.size();i++)
					{
						String temp = (String)(vtrQPRCD.elementAt(i));
						if(!temp.equals(""))
						{
							String str = "SMT_" + ((String)(vtrQPRCD.elementAt(i))).trim()+ "VL";							
							strTEMP = nvlSTRVL(M_rstRSSET.getString(str),"-");							
							L_stbDATA.append(padSTRING('L',strTEMP,7));								
						}
					}
					L_stbDATA.append("\n");
					intRECCT = 1;
				}
				M_rstRSSET.close();				
			}
			dosREPORT.writeBytes(L_stbDATA.toString());						
			dosREPORT.writeBytes("\n"+ stbDOTLN + "\n");
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
	Method to validate Consignment number, before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{
			if(txtCONNO.getText().length()==0)			
			{
				setMSG("Please Enter Consignment number to generate the Report.. ",'E');
				txtCONNO.requestFocus();			
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
			intROWLN = 24;
			String L_strCONDS="",L_strPRTNM="",L_strHEADR="";						
			StringBuffer L_stbHEADR = new StringBuffer(padSTRING('R',"Tank Referance",23));
			for(int i=0;i<vtrQPRCD.size();i++)
			{
				L_stbHEADR.append(padSTRING('L',((String)vtrQPRCD.elementAt(i)).trim(),7));
				intROWLN += 7;
			}									
			L_stbHEADR.append("\n"+padSTRING('L'," ",23));
			for(int i=0;i<vtrQPRCD.size();i++)				
				L_stbHEADR.append(padSTRING('L',hstUOMCD.get((String)vtrQPRCD.elementAt(i)).toString(),7));
				
			stbDOTLN.delete(0,stbDOTLN.toString().length());
			for(int k=0; k < intROWLN;k++)
				stbDOTLN.append("-");
			stbDOTLN.append("\n");				
			L_strHEADR = String.valueOf(stbDOTLN).toString();
			L_strHEADR += String.valueOf(L_stbHEADR).toString()+"\n";
			L_strHEADR += String.valueOf(stbDOTLN).toString();
				
			M_strSQLQRY = "select Distinct BE_CONDS,BE_CONDT,PT_PRTNM"
				+" from MM_BETRN,MM_POMST,CO_PTMST where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_STRTP = '04'"
				+" AND PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='04' and PT_PRTTP = 'S'"
			    +" AND PO_VENCD = PT_PRTCD AND BE_CMPCD = PO_CMPCD AND BE_PORNO = PO_PORNO"
				+" AND BE_CONNO ='"+ strCONNO +"'";				
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(M_rstRSSET != null)					
			{									
				if(M_rstRSSET.next())
				{
					L_strCONDS = nvlSTRVL(M_rstRSSET.getString("BE_CONDS"),"");
					L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"");
				}
				M_rstRSSET.close();
			}												
			if(intROWLN < 65)
				intROWLN = 65;			
			
			dosREPORT.writeBytes("\n\n\n\n\n");						
			dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst +"\n");						
			dosREPORT.writeBytes("Consignment No.: " + strCONNO +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Description    : "+ L_strCONDS,intROWLN - 28));			
			dosREPORT.writeBytes("Report Date    : " + cl_dat.M_strLOGDT_pbst +"\n");						
			dosREPORT.writeBytes(padSTRING('R',"Supllier Name  : "+ L_strPRTNM,intROWLN - 28));
			dosREPORT.writeBytes("Page No.       : 1" +"\n");									
			dosREPORT.writeBytes(L_strHEADR.toString());								
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}	
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{							
				if(input == txtCONNO)
				{
					if (txtCONNO.getText().trim().length() == 7)
					{										
						M_strSQLQRY = "select distinct BE_CONDS,BE_CONDT from MM_BETRN "
							+"where BE_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND BE_STRTP ='04' AND BE_CONNO ='"+ txtCONNO.getText().trim() +"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);		
						if (M_rstRSSET != null)
						{  
							if(M_rstRSSET.next())
							{	
								strCONNO = txtCONNO.getText().trim();
								M_rstRSSET.close();
								return true;								
							}
							else
							{
								M_rstRSSET.close();
								setMSG("No Data found ..",'E');
								return false;				
							}							
						}
					}
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"INPVF");
			}
			return true;
		}
	}
}
