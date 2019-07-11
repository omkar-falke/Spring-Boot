/**
System Name   : Laboratoty Information Management System
Program Name  : Export Certifate 
Program Desc. : Report for taking the Details of the Materials exported between given date range.
Author        : Mr. S.R.Mehesare
Date          : 1 JUNE 2005
Version       : LIMS V2.0.0

Modificaitons 
Modified By   : 
Modified Date : 
Modified det. : 
Version       : 
*/
import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Vector;

/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Export Certifate 

Purpose : Report for taking the Details of the Materials exported between given date range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
QC_PSMST       PS_QCATP, PS_TSTTP,PS_LOTNO,
               PS_RCLNO,PS_TSTNO,PS_TSTDT                              #
CO_CDTRN       CMT_CGMTP,CMT_CGSTP,CMT_CODCD                           #   
PR_LTMST       LT_PRDTP,LT_LOTNO,LT_RCLNO                              #
MR_IVTRN                                                               #
FG_ISTRN                                                               #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtDATE	                                                Current date by default 
txtFMDAT    LA_LODDT       MR_LAMST      DATE           From Date 
txtTODAT    LA_LODDT       MR_LAMST      DATE           To Date 
txtSALTP    CMT_CODC       CO_CDTRN      VARCHAR(15)	Sale Type
txtPRTCD    LA_BUYER       MR_LAMST      VARCHAR(5)     Party Code
txtPRTNM    PR_PRTNM       CO_PTMST      VARCHAR(40)	Party Name
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
    "PS_"+ Vector.elementAt(i)+"VL" i.e  PS_QCATP, PS_TSTTP
   4) Same logic is used to retrieve data from the Resultset, using loop & dynamiclly 
   generating column names.	

Sale Type is taken from CO_CDTRN for Conditation
   1) CMT_CGMTP = 'SYS'
   2) AND CMT_CGSTP = 'MR00SAL'
   3) AND CMT_CODCD IN ('12','03')

Party Code is taken from MR_IVTRN & CO_PTMST for Conditation
   1) IVT_SALTP = given sale type Code as '12' OR '03'
   2) AND IVT_LODDT BETWEEN given date range
   3) AND IVT_BYRCD = PT_PRTCD 
   4) AND PT_PRTTP ='C'";                    

<I><B>Report Data is Taken as :</b>
Main data is taken from MR_IVTRN, FG_ISTRN for given Date range,
Sale Type & optionally provided Party Name.
   1) IST_MKTTP = IVT_MKTTP 
   2) AND IST_ISSNO = IVT_LADNO 
   3) AND IVT_SALTP = given Sale Type
   4) AND IST_PRDTP = given product type '01'
   5) AND IVT_LODDT BETWEEN given date
   if Party Code is given 
   6) AND IVT_BYRCD = given party Code 	
And Test Details are taken from QC_PSMST for given conditations as
   1) PS_QCATP = given QCA Type
   2) AND PS_TSTTP = '0103'
   3) AND PS_LOTNO = given Lot Number
   4) AND PS_RCLNO = Reclassificaion Number.
       
<B>Validations :</B>
    - If Party code is not Spacified then the Report for all parties for given 
      date are given.
    - Party Code if given then must be valid.
    - To date must be greater than from date & smaller than current date.  
    - Sale type must be valid.
</I> */
public class qc_rpexc extends cl_rbase
{										/** JTextField to Display & accept the From Date to specify the Date Range.*/
	private JTextField txtFMDAT;		/** JTextField to Display & accept the To Date to spacify Date Range.*/
	private JTextField txtTODAT;		/** JTextField to Display & accept the Sale Type.*/
	private JTextField txtSALTP;		/** JTextField to Display Sale Description.*/
	private JTextField txtSALDS;		/** JTextField to Display & accept the Party Code.*/
	private JTextField txtPRTCD;		/** JTextField to Display the Party Description.*/
	private JTextField txtPRTDS;
	private JComboBox cmbPRDTP;
										/** String variable for generated report file name.*/
	private String strFILNM;			/** String variable for Dynamically generated Dotted Line.*/
	private String strDOTLN;			/** String variable for Part Type.*/
	private String strPRDTP="01";		/** String variable for Export Department.*/
	private String strEXPDPT = "EXPORT DEPARTMENT ";
											/** String variable for Sale Export Type.*/
	private String strSLEXP = "12";		/** String variable for Sale Type.*/
	private String strSLTYP ="" ;			/** String variable for Deemed Export.*/
	private String strSLDEX = "03";		/** String variable for Charector constant value.*/
	private String []arrCCSVL;	
											/** Integer variable to count the number of records fetched. */
	private int intRECCT = 0;				/** Vector object to hold Quality proparty Short Descripton.*/
	private Vector<String> vtrQPRDS = new Vector<String>(); /** Vector object to hold Quality Proparty Code.*/
	private Vector<String> vtrQPRLS = new Vector<String>(); /** FileOutputStream for generated Report File Name.*/
	private FileOutputStream fosREPORT;		/** DataOutputStream for generating Stream of the Report File.*/
    private DataOutputStream dosREPORT;
	private ResultSet M_rstRSSET2,M_rstRSSET1;	
	private Hashtable<String,String> hstSHRDS;
	private Hashtable<String,String> hstUOMCD;
	private Hashtable<String,String> hstPRTDS;
	private Hashtable<String,String> hstPRDDS;
	qc_rpexc()
	{
		super(2);
		try
		{
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);		    
			
			add(new JLabel("Product Type"),4,3,1,.9,this,'R');
			add(cmbPRDTP = new JComboBox(),4,4,1,2,this,'L');
			
			add(new JLabel("Sale Type"),5,3,1,.9,this,'R');
			add(txtSALTP = new TxtNumLimit(10),5,4,1,1,this,'L');
			add(txtSALDS = new JTextField(),5,5,1,2,this,'L');
			
			add(new JLabel("From Date"),6,3,1,.9,this,'R');
			add(txtFMDAT = new TxtDate(),6,4,1,1,this,'L');			
			add(new JLabel("TO Date"),6,5,1,.6,this,'R');
			add(txtTODAT = new TxtDate(),6,6,1,1,this,'L');
			
			add(new JLabel("Party Code"),7,3,1,.9,this,'R');
			add(txtPRTCD = new TxtLimit(10),7,4,1,1,this,'L');
			add(txtPRTDS = new JTextField(),7,5,1,3.5,this,'L');			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
						
			hstSHRDS = new Hashtable<String,String>();
			hstUOMCD = new Hashtable<String,String>();			
			M_strSQLQRY = "Select QS_QPRCD,QS_SHRDS,QS_UOMCD from CO_QSMST where isnull(QS_STSFL,'') <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("QS_QPRCD"),"");					
					if(!L_strQPRCD.equals(""))
					{
						hstSHRDS.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_SHRDS"),""));						
						hstUOMCD.put(L_strQPRCD,nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),""));
					}
				}
				M_rstRSSET.close();
			}
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP= 'MST'"
				+ " AND CMT_CGSTP ='COXXPRD'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strQPRCD = "";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					if(!L_strQPRCD.equals(""))
						cmbPRDTP.addItem(L_strQPRCD+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}		
	}
	/**
	 * Super class Method overrided to inhance its funcationality, to enable & disable components 
	 * according to requriement.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);				
		txtSALDS.setEnabled(false);
		txtPRTDS.setEnabled(false);
		if(L_flgSTAT =false)
			clrCOMP();
	}			
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE); 
	    if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{							
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter Sale Type OR Press F1 to Select from List..",'N');				
				setENBL(true);
				if((txtTODAT.getText().trim().length()==0)||(txtFMDAT.getText().trim().length()==0))
				{
					txtTODAT.setText(cl_dat.M_strLOGDT_pbst);															
					txtFMDAT.setText(calRQDAT(cl_dat.M_strLOGDT_pbst));	
				}
				txtSALTP.requestFocus();
			}
			else
			{
				setMSG("Please Select an Option..",'N');
				setENBL(false);	
			}
		}
		else if(M_objSOURC == cmbPRDTP)
		{
			strPRDTP = cmbPRDTP.getSelectedItem().toString().substring(0,2);			
		}
		else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			cl_dat.M_PAGENO=0;
			cl_dat.M_intLINNO_pbst=0;			
		}	
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
			if(M_objSOURC == txtSALTP)
			{
 				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{				    	  
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS'and CMT_CGSTP = 'MR00SAL'";
					M_strSQLQRY += " AND CMT_CODCD IN ('" +strSLEXP +"','"+strSLDEX +"')";
					M_strHLPFLD = "txtSALTP";																	
					cl_hlp(M_strSQLQRY,1,1,new String[]{"CODE","SALE TYPE"},2,"CT");    																	
				}
    			catch(Exception L_EX)
    			{
					setCursor(cl_dat.M_curDFSTS_pbst);
	    		    setMSG(L_EX ," F1 help..");    		    
			    }
    		    setCursor(cl_dat.M_curDFSTS_pbst);
 			}
			if(M_objSOURC == txtPRTCD)
			{		
				txtPRTDS.setText("");
				String L_strTODAT="";
				String L_strFMDAT="";
				if((txtTODAT.getText().trim().length() == 0)||(txtTODAT.getText().trim().length() == 0))
				{
					setMSG("Please Enter From Date & To Date then press F1 key for help..",'E');
					return;
				}				
 				setCursor(cl_dat.M_curWTSTS_pbst);
    			try
    	  		{				    	  
					M_strSQLQRY = " SELECT distinct IVT_BYRCD,PT_PRTNM,IVT_SALTP,CONVERT(varchar,IVT_LODDT,103) IVT_LODDT FROM MR_IVTRN,CO_PTMST";
                    M_strSQLQRY += " WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP = "+"'"+strSLTYP+"'";                        
                    M_strSQLQRY += " AND CONVERT(varchar,IVT_LODDT,103) BETWEEN '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
					M_strSQLQRY += "' AND '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
                    M_strSQLQRY += "' and IVT_BYRCD = PT_PRTCD and PT_PRTTP ='C'";
                    M_strSQLQRY += " AND isnull(IVT_STSFL,'') <>'X' ORDER BY IVT_BYRCD";
					M_strHLPFLD = "txtPRTCD";																					
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Party Code","Party Name Description","SALE TYPE","Date"},4,"CT");    																	
				}
    			catch(Exception L_EX)
    			{
	    		    setMSG(L_EX ," F1 help..");    	
					setCursor(cl_dat.M_curDFSTS_pbst);
			    }
    		    setCursor(cl_dat.M_curDFSTS_pbst);
 			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
 	    {
			if(M_objSOURC == txtSALTP)
			{
				if(txtSALTP.getText().trim().length()>0)
				{
					
					txtFMDAT.requestFocus();
					setMSG("Enter Date to specify Date Range..",'N');
				}				
			}
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length()>0)
				{
					txtTODAT.requestFocus();
					txtPRTCD.setText("");
					txtPRTDS.setText("");
					setMSG("Enter Date to specify Date Range..",'N');
				}				
			}
			if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length()>0)
				{
					txtPRTCD.requestFocus();
					txtPRTCD.setText("");
					txtPRTDS.setText("");
					setMSG("Enter Party Code OR Press F1 to select from List..",'N');
				}				
			}
			if(M_objSOURC == txtPRTCD) 
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
	}
	/**
	Method for execution of F1 help to select Sale Type & Party code from List.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtSALTP")
		{
			txtSALTP.setText(cl_dat.M_strHLPSTR_pbst);							
			strSLTYP = txtSALTP.getText().trim();			
			txtSALDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
			if(txtSALTP.getText().length()>0)			
			    txtFMDAT.requestFocus();
		}		
		if(M_strHLPFLD == "txtPRTCD")
		{
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);				
			txtPRTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());			
			if(txtPRTCD.getText().length()>0)			
			    cl_dat.M_btnSAVE_pbst.requestFocus();
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
		        strFILNM = cl_dat.M_strREPSTR_pbst +"qc_rpexc.html";
		    else if(M_rdbTEXT.isSelected())
		        strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpexc.doc";
			getDATA();
			if(dosREPORT !=null)
				dosREPORT.close();
			if(fosREPORT !=null)
				fosREPORT.close();
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Export Certificate"," ");
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
    * Method to export certificate main data from MR_IVTRN & FG_ISTRN tables & Test Details 
    * from QC_PSMST & to club it with Header & footer in the DataOutputStream.
	*/
	private void getDATA()
	{ 			
		String L_strNEWLN ="Y";
		String L_strTODAT="",L_strFMDAT="",L_strPRPRT="",L_strPRCNT="",L_strPRPRD="",L_strPRDCD ="",L_strPRDTP;
		String L_strPRTNM="",L_strPRTCD ="",L_strQPRVL="",L_strCNTNO,L_strLORNO,L_strPRDDS="",L_strLOTNO,L_strRCLNO;				
		String L_strOPRTCD="",L_strOCNTNO="",L_strOLORNO="",L_strOPRDDS="";
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
	    {	   
			StringBuffer L_strRECORD = new StringBuffer();
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Export Certificate </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
			prnHEADER();

			L_strTODAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText().trim()));
			L_strFMDAT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText().trim()));
			
			/** Party Description*****/
			hstPRTDS = new Hashtable<String,String>();			
			M_strSQLQRY = " SELECT distinct IVT_BYRCD,PT_PRTNM FROM MR_IVTRN,CO_PTMST WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP = "+"'"+strSLTYP+"'";            			
			M_strSQLQRY += " AND isnull(IVT_STSFL,'') <>'X' AND IVT_BYRCD=PT_PRTCD AND PT_PRTTP='C'";
			M_strSQLQRY += " AND CONVERT(varchar,IVT_LODDT,103) BETWEEN '" + L_strFMDAT + "' AND '"+L_strTODAT +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{			
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
					if(!L_strTEMP.equals(""))
						hstPRTDS.put(L_strTEMP,nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
				}
				M_rstRSSET.close();
			}			
			/*** Product Code */
			hstPRDDS = new Hashtable<String,String>();
			M_strSQLQRY = " SELECT distinct IVT_PRDCD,PR_PRDDS FROM MR_IVTRN,CO_PRMST WHERE IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP = "+"'"+strSLTYP+"'";
			M_strSQLQRY += " AND isnull(IVT_STSFL,'') <>'X' AND IVT_PRDCD = PR_PRDCD";
			M_strSQLQRY += " AND CONVERT(varchar,IVT_LODDT,103) BETWEEN '" + L_strFMDAT + "' AND '"+L_strTODAT +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{			
				String L_strTEMP="";
				while(M_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_PRDCD"),"");
					if(!L_strTEMP.equals(""))
						hstPRDDS.put(L_strTEMP,nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),""));
				}
				M_rstRSSET.close();
			}			
			
			M_strSQLQRY = " SELECT distinct IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PRDCD,IVT_CNTDS,IVT_LRYNO,IVT_BYRCD,IVT_SALTP FROM MR_IVTRN,FG_ISTRN ";
            M_strSQLQRY += " WHERE IST_CMPCD=IVT_CMPCD and IST_MKTTP = IVT_MKTTP AND IST_ISSNO = IVT_LADNO AND IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_SALTP = "+"'"+strSLTYP+"'";
			M_strSQLQRY += " AND IST_PRDTP ='" +strPRDTP +"'";
            M_strSQLQRY += " AND CONVERT(varchar,IVT_LODDT,103) BETWEEN '" + L_strFMDAT + "' AND '"+L_strTODAT +"'";
			if(txtPRTCD.getText().trim().length() >0)
				M_strSQLQRY += " AND IVT_BYRCD ='"+ txtPRTCD.getText().trim()+"'" ;
			M_strSQLQRY += " AND isnull(IVT_STSFL,'') <>'X' AND isnull(IST_STSFL,'') <>'X' ORDER BY IVT_BYRCD,IVT_CNTDS,IST_PRDCD,IST_PRDTP,IST_LOTNO,IST_RCLNO";            			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{				
				while(M_rstRSSET.next())
				{			
					intRECCT=1;					
					L_strRECORD.delete(0,L_strRECORD.length());
					L_strPRDTP = nvlSTRVL(M_rstRSSET.getString("IST_PRDTP"),"");
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),"-");;
					L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("IST_RCLNO"),"");
					L_strCNTNO = nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),"");
					L_strLORNO = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"");
					L_strPRTCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("IST_PRDCD"),"");					
					
					if(!L_strPRTCD.equals(L_strOPRTCD))
					{	
						if(txtPRTDS.getText().trim().length()==0)
						{
							if(hstPRTDS.containsKey(L_strPRTCD))
								L_strPRTNM = hstPRTDS.get(L_strPRTCD).toString();
							else /// not Requried but....
							{
								M_strSQLQRY = " Select distinct PT_PRTNM from CO_PTMST where PT_PRTTP= 'C' AND";
								M_strSQLQRY += " PT_PRTCD = '" + L_strPRTCD +"'" ;																			
								M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
								if(M_rstRSSET1 !=null)
								{
									if(M_rstRSSET1.next())								
										L_strPRTNM = nvlSTRVL(M_rstRSSET1.getString("PT_PRTNM"),"");								
									M_rstRSSET1.close();
								}
							}							
							L_strOPRTCD = L_strPRTCD;
						}
						else
						{
							L_strPRTNM = txtPRTDS.getText().trim();						
							L_strOPRTCD = L_strPRTCD;
						}
						dosREPORT.writeBytes("\n" + L_strPRTNM +"\n\n");
						cl_dat.M_intLINNO_pbst +=3;
					}								
					//Container Number.
					if(!L_strCNTNO.equals(L_strOCNTNO))
					{
						dosREPORT.writeBytes(L_strCNTNO);
						cl_dat.M_intLINNO_pbst +=1;
						L_strOCNTNO = L_strCNTNO;						
					}																					
					if(!L_strLORNO.equals(L_strOLORNO))
					{
						L_strRECORD.append("\n"+padSTRING('R',L_strLORNO,15));
						cl_dat.M_intLINNO_pbst +=1;
						L_strOLORNO = L_strLORNO;						
						L_strNEWLN = "Y";
					}				
					else
					{
						L_strRECORD.append(padSTRING('R',"",15));											
						L_strNEWLN = "N";
					}
					
					//Product Grade...
					if(hstPRDDS.containsKey(L_strPRDCD))
						L_strPRDDS = hstPRDDS.get(L_strPRDCD).toString();
					else /// not Requried but....
					{
						M_strSQLQRY = " Select PR_PRDDS from CO_PRMST where PR_PRDCD ='" + L_strPRDCD+"'";
						M_rstRSSET1 = cl_dat.exeSQLQRY3(M_strSQLQRY);					
						if(M_rstRSSET1 !=null)
						{
							if(M_rstRSSET1.next())						
								L_strPRDDS = nvlSTRVL(M_rstRSSET1.getString("PR_PRDDS"),"");
							M_rstRSSET1.close();
						}
					}										
					if((!L_strPRDDS.equals(L_strOPRDDS)) || (L_strNEWLN.equals("Y")))
					{
						L_strRECORD.append(padSTRING('R',L_strPRDDS.trim(),10));
						L_strOPRDDS = L_strPRDDS;						
					}
					else
						L_strRECORD.append(padSTRING('R',"",10));										
					
					L_strRECORD.append(padSTRING('R',L_strLOTNO.trim(),11));										
					
					M_strSQLQRY = " SELECT LT_CLSFL,PS_MFIVL,PS_DSPVL,PS_IZOVL,PS_TS_VL,PS_EL_VL,PS_VICVL,PS_RSMVL";
					M_strSQLQRY +=" FROM PR_LTMST LEFT OUTER JOIN QC_PSMST ON LT_LOTNO = PS_LOTNO AND LT_RCLNO = PS_RCLNO ";
					M_strSQLQRY +=" AND PS_QCATP = '"+M_strSBSCD.substring(2,4)+"'"+" AND PS_TSTTP = '0103'";
					M_strSQLQRY +=" AND PS_STSFL <> 'X'";
					M_strSQLQRY +=" WHERE LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_PRDTP ="+"'"+L_strPRDTP.trim() + "'" + " and LT_LOTNO = " + "'"+ L_strLOTNO.trim() +"'"; 
					M_strSQLQRY +=" AND LT_RCLNO ='"+L_strRCLNO.trim() +"'";
										
					M_rstRSSET2 = cl_dat.exeSQLQRY2(M_strSQLQRY);						
					if(M_rstRSSET2 !=null)
					{
						String L_strCLSFL="";
						String L_strQPFLD="";
						while(M_rstRSSET2.next())
						{
							L_strCLSFL = nvlSTRVL(M_rstRSSET2.getString("LT_CLSFL"),"");
							if(L_strCLSFL.equals("9"))
							{
								for(int i=0;i<vtrQPRLS.size();i++)
								{									
									L_strQPFLD = "PS_" + (String)(vtrQPRLS.elementAt(i)).toString().trim()+ "VL";
									L_strQPRVL = M_rstRSSET2.getString(L_strQPFLD.trim());
									if(L_strQPRVL !=null && !L_strQPRVL.trim().equals(""))
									{
										if(Double.valueOf(L_strQPRVL).doubleValue()!=0.0)
										{
											if(i==0)
												L_strRECORD.append(padSTRING('L',L_strQPRVL,5));
											else
												L_strRECORD.append(padSTRING('L',L_strQPRVL,8));
										}										
									}
									else											
										L_strRECORD.append(padSTRING('L',"-",8));
								}
							}
							else
								L_strRECORD.append(padSTRING('L'," Not Classified ",25));	
						}
						M_rstRSSET2.close();
					}
					
					dosREPORT.writeBytes(L_strRECORD.toString()+"\n");					
					cl_dat.M_intLINNO_pbst += 1;
					
					if(cl_dat.M_intLINNO_pbst>65)
					{						
						dosREPORT.writeBytes(strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();											
					}		
				}
				if(hstPRTDS != null)
					hstPRTDS = null;
				M_rstRSSET.close();
				dosREPORT.writeBytes("\n"+strDOTLN);				
				dosREPORT.writeBytes("\n"+padSTRING('L',"",75)+"Regards");
				dosREPORT.writeBytes("\n\n\n");				
				dosREPORT.writeBytes("\n"+padSTRING('L',"",75)+"HOD(MHD)");
			}					
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
			getTSTDET();
			if(txtSALTP.getText().length()==0)			
			{
				setMSG("Please Enter Sale Type OR Press F1 Key to select form list.. ",'E');
				txtSALTP.requestFocus();			
				return false;
			}
			if(txtFMDAT.getText().trim().length() == 0)			
			{
				setMSG("Please Enter valid From-Date, To Spacify Date Range ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().trim().length() == 0)			
			{	
				setMSG("Please Enter valid To-Date to spacify Date Range ..",'E');
				txtTODAT.requestFocus();
				return false;
			}	
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
			StringBuffer L_stbUMHEAD,L_stbSUBHDR,L_stbSUBUOM,L_stbDOTLN;
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;			
			dosREPORT.writeBytes("\n\n\n\n\n");			
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,65));			
			dosREPORT.writeBytes("Report Date : " + cl_dat.M_strLOGDT_pbst + "\n");
			
			dosREPORT.writeBytes(padSTRING('R',"Export Certificate",65));
			dosREPORT.writeBytes("Page No.    : " + String.valueOf(cl_dat.M_PAGENO) + "\n");
			
			dosREPORT.writeBytes(padSTRING('R',"From : MHD",65));
			dosREPORT.writeBytes("To : " + strEXPDPT +"\n");									
			
			dosREPORT.writeBytes(padSTRING('R',"Please find below the product test details of material",65)+"\n");;
			dosREPORT.writeBytes("despatched between  : " + txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim()+"\n");				
				
			L_stbUMHEAD = new StringBuffer(padSTRING('R',"Customer",15));			
			L_stbUMHEAD.append(padSTRING('R',"Grade",10));
			L_stbUMHEAD.append(padSTRING('R',"Lot No",15));
			L_stbUMHEAD.append(padSTRING('C',"Properties",42));									
				
			L_stbSUBHDR = new StringBuffer(padSTRING('R',"Container No.",33));
			for(int i=0;i<vtrQPRDS.size();i++)
				L_stbSUBHDR.append(padSTRING('L',(String)(vtrQPRDS.elementAt(i)).toString().trim(),8));									
			L_stbSUBUOM = new StringBuffer(padSTRING('R',"Lorry No. ",33));
			for(int i=0;i<arrCCSVL.length;i++)
				L_stbSUBUOM.append(padSTRING('L',arrCCSVL[i].trim(),8));						
											
			L_stbDOTLN = new StringBuffer("-");			
			for(int i=0; i < L_stbSUBUOM.toString().length();i++)
				L_stbDOTLN.append("-");
			strDOTLN =L_stbDOTLN.toString();			
			dosREPORT.writeBytes(strDOTLN +"\n");
			dosREPORT.writeBytes(L_stbUMHEAD.toString()+"\n");
			dosREPORT.writeBytes(L_stbSUBHDR.toString()+"\n");
			dosREPORT.writeBytes(L_stbSUBUOM.toString()+"\n");						
			dosREPORT.writeBytes(strDOTLN +"\n");
			cl_dat.M_intLINNO_pbst = 15;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}	
	/**
	 * Method to get the Test details & to put it into the vector.
	 */
	private void getTSTDET()
	{		
		int L_intRECCNT = 0;
	    vtrQPRLS.removeAllElements();
		vtrQPRDS.removeAllElements();
		int i=0;		
		try
		{			
			M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL from co_cdtrn where ";
			M_strSQLQRY += " CMT_CGMTP='RPT' and CMT_CGSTP = 'QCXXEXC' order by CMT_NCSVL";
			java.sql.Statement L_stat = cl_dat.M_conSPDBA_pbst.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,   ResultSet.CONCUR_UPDATABLE);		
			M_rstRSSET = L_stat.executeQuery(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{									
				while(M_rstRSSET.next())
					L_intRECCNT++;									
				if(L_intRECCNT >0)			
					arrCCSVL  = new String[L_intRECCNT];			
				M_rstRSSET.beforeFirst();							
				while(M_rstRSSET.next())																			
				{
					String L_QPRCD = M_rstRSSET.getString("CMT_CODCD");
					vtrQPRLS.addElement(L_QPRCD);					
					vtrQPRDS.addElement(hstSHRDS.get(L_QPRCD).toString());					
					arrCCSVL[i] = hstUOMCD.get(L_QPRCD).toString();					
					i+=1;
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
	* Method to calculate From-Date, one day smaller than To-Date.
	* @ param P_strTODAT String argument to pass the Date.
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
}
