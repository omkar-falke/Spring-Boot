/*
System Name   : Material Management System
Program Name  : Tanker Routing Slip
Program Desc. : User selects the Gate-In No. & Gate-In Type
Author        : Mrs.Dipti.S.Shinde.
Date          : 15th Sep 2005
System        : MMS
Version       : MMS v2.0.0
Modificaitons : Query and report printing 
Modified By   : Mrs.Dipti.S.Shinde.
Modified Date : 23rd Sep 2005
Modified det. : Test type wise dynamic query generation and dynamic report printing.
Version       : MMS v2.0.0
*/

import java.sql.ResultSet;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;import java.awt.event.FocusEvent;
import java.io.FileOutputStream; import java.io.DataOutputStream; 
import javax.swing.JTextField;import javax.swing.JComboBox;
import javax.swing.JPanel;import javax.swing.JTabbedPane;
import javax.swing.JLabel;import javax.swing.JTable;import javax.swing.JButton;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.util.Date;import java.util.StringTokenizer;
import java.util.Hashtable;import java.util.Vector;
/**<pre>
<b>Program Name :</b> Tanker Routing Slip printing.

<b>Purpose :</b> Tanker routing slip is printed for Raw material coming in Tankers.All the
information including Gate-In, Weighment details, Testing details,Control room clearnce
for unloading, unloading details and gate-Out details are printed.
			
List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
MM_WBTRN        WB_DOCTP,WB_MATCD                             #
QC_SMTRN        SMT_QCATP,SMT_TSTTP                           #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		      Table name      Type/Size      Description
----------------------------------------------------------------------------------------------------------
 txtDOCNO    WB_DOCNO             MM_WBTRN        varchar(8)     Gate-In number
 cmbDOCTP    CO_CODCD,WB_DOCTP    CO_CDTRN        varchar(2)     Gate-In Type                 
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description        Display Columns      Table Name
----------------------------------------------------------------------------------------------------------
txtDOCNO    Documentation number       WB_DOCNO             MM_WBTRN
----------------------------------------------------------------------------------------------------------
Query       : Test details are taken from QC_SMTRN on the basis of Material code
            : SMT_TSTNO = WB_QLLRF WHERE WB_DOCTP ='01' and WB_DOCNO = given gate-In no.
Material code and Test type for HIGH SPEED DIESEL:6810150025-'0305',
STYRENE MONOMER                                  :6805010045-'0303'
MINERAL OIL and FURNACE OIL :other than styrene and HSD : Test type '0301' is used.

Validations : 

1. Gate in number should not be blank and should be valid.

*/
public class mm_rptkr extends cl_rbase 
{                                        /** String for file name */
	private String strFILNM;             /** File output Stream for generating file.*/
	private FileOutputStream fosREPORT;	 /** Data output Stream for generating Report File.*/
    private DataOutputStream dosREPORT;	 /** Integer for record counting */                                     		
	private int intRECCT=0;	             /** TextField for gate entry number */
	private JTextField txtDOCNO;         /** comboBox for entry type */
	private JComboBox cmbDOCTP;	         /** String for getting data from database */			
	private final String strMAT_ST ="6805010045";
	private final String strMAT_ST1 ="6810150025";/** value of test type */
	private Vector<String> vtrQPRCD = new Vector<String>();/** Vector for getting Test value */
	private Hashtable<String,String> hstTSTR = new Hashtable<String,String>();/** Hash table for storing value of Test code */
	mm_rptkr()
	{
	    super(2);
	    try
	    {	
			setCursor(cl_dat.M_curWTSTS_pbst);
		    setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Gate in type"),3,3,1,1,this,'L');
			add(cmbDOCTP = new JComboBox(),3,4,1,1.5,this,'L');
					
			add(new JLabel("Gate in number"),4,3,1,1,this,'R');
			add(txtDOCNO = new JTextField(),4,4,1,1.5,this,'L');	
			
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='SYS' AND CMT_CGSTP ='MMXXWBT' AND ISNULL(CMT_STSFL,'')<>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{	
				while(M_rstRSSET.next())
					cmbDOCTP.addItem(M_rstRSSET.getString("CMT_CODCD")+" "+M_rstRSSET.getString("CMT_CODDS"));				
				 M_rstRSSET.close();
			}
			M_pnlRPFMT.setVisible(true);
	 		setENBL(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{   
			setMSG(L_EX,"Constructor");
		}
	}	
	/** Method for action performing 
	 */
	public void actionPerformed(ActionEvent L_AE)
	{	 
	    super.actionPerformed(L_AE); 	
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)
			{
				setMSG("Select an Option..",'E');
				cl_dat.M_cmbOPTN_pbst.requestFocus();
			}			
			else
			{				
				setENBL(true);
				txtDOCNO.requestFocus();
				setMSG("Please Enter Gate-In Number to generate the Report..",'N');
			}
		}
		if(M_objSOURC==cmbDOCTP)
		{
			txtDOCNO.setText("");
			txtDOCNO.requestFocus();
			setMSG("Please Enter Gate-In Number to generate the Report..",'N');
		}
		if(M_objSOURC==txtDOCNO)
		{
			setMSG("Press F1 to get data",'N');
			if(txtDOCNO.getText().trim().length()>0)
				cl_dat.M_btnSAVE_pbst.requestFocus();
			else
				setMSG("Please Enter Gate-In number To generate the Report..",'N');
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{	    
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
			String L_strGATTP =cmbDOCTP.getSelectedItem().toString().substring(0,2);
			setCursor(cl_dat.M_curWTSTS_pbst);
    	    try
    	  	{				
				if(M_objSOURC== txtDOCNO)
				{
					M_strHLPFLD = "txtDOCNO";
					M_strSQLQRY = "SELECT DISTINCT WB_DOCNO,WB_LRYNO,convert(varchar,WB_GINDT,1) from MM_WBTRN"+
				      " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP ='"+L_strGATTP+"'";
					if(txtDOCNO.getText().length() >0)					
						M_strSQLQRY += " AND WB_DOCNO like '"+ txtDOCNO.getText().trim()+"%'"; 					
					M_strSQLQRY +=" Order by WB_DOCNO DESC";
                    cl_hlp(M_strSQLQRY,1,1,new String[]{"gate in number","Lory number","Date"},3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			catch(Exception L_EX)
			{   
				setMSG(L_EX,"f1 key event");
			}
		}
	}
	/**
	Method for execution of help for Memo Number Field.
	*/
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtDOCNO")
         {
			txtDOCNO.setText(cl_dat.M_strHLPSTR_pbst);	
			txtDOCNO.requestFocus();
		 }
	}
	void exePRINT()
	{
		if(!vldDATA())
			return;		
		try
		{
		   if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rptkr.html";				
		   else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rptkr.doc";				
			cl_dat.M_PAGENO = 0;
			cl_dat.M_intLINNO_pbst = 0;	
			
			getDATA();
			
			if(intRECCT ==0)
			{
				setMSG("Data not found ..",'E');
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Tanker Routing Slip"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}					
	}
 /** Method for Getting All Records from Table. */
   private void getDATA()
	{
		try
		{
			String L_strGINDT="",L_strGINTM="",L_strGINBY="",L_strTNKNO="",L_strDOCNO="";
			String L_strLRYNO="",L_strTPRDS="",L_strMATCD="",L_strMATDS="",L_strCHLNO="";
			String L_strCHLQT="",L_strINCTM="",L_strOUTTM="",L_strGRSWT="",L_strTARWT="";
			String L_strNETWT="",L_strCHLDT="",L_strGOTDT="",L_strGOTTM="",L_strINCDT="",L_strOUTDT="";
			String L_strTSTDT="",L_strTFODT="",L_strTFITM="",L_strTFIDT="",L_strSPGVL="",L_strTMPVL="",L_strSMPTM="",L_strSMPDT="",L_strQLTFL="",L_strGOTBY="",L_strTSTTM="",L_strTSTBY="",L_strWINBY="",L_strWOTBY="",L_strDIFQT="",L_strQLLRF="",L_strCOLVL="",L_strRI_VL="",L_strTBCVL="",L_strPOLVL="",L_strBZ_VL="",L_strTOLVL="",L_strEB_VL="",L_strXYLVL="",L_strSTYVL="",L_strULBVL="",L_strUHBVL="";
			String L_strTEMP="",L_strSQLQRY="",L_strUCLTM="",L_strUCLDT="",L_strBSCHK="",L_strTNCHK="",L_strLOCHK="",L_strSLCHK="",L_strUCLBY="",L_strERCHK="",L_strULSTM="",L_strULETM="",L_strEPCHK="",L_strTFOTM="",L_strSLCHK1="",L_strULDBY="";
			String L_strTEMP2="",L_strCODCD="",L_strSHRDS="",L_strUOMCD="",L_temp1="";
			String L_strREMDS ="",L_strPRDRM ="";
			int L_intDIFRS=0;int L_intRESCT=0;
			ResultSet L_rstRSSET;
			java.sql.Date L_tmpDATE;java.sql.Timestamp L_tmpTIME;
			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title> Tanker Routing Slip </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE>"); 
			}
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO += 1;						
			M_strSQLQRY = "SELECT WB_TFITM,WB_GINDT,WB_BSCHK,WB_TNCHK,WB_LOCHK,WB_ERCHK,WB_ULSTM,WB_ULETM,WB_EPCHK,WB_TFOTM,WB_SLCHK,WB_UCLTM,WB_UCLBY,WB_ULDBY,WB_MATCD,WB_TSTTM,WB_SMPTM,WB_GOTBY,WB_WINBY,WB_ULDBY,WB_WOTBY,WB_DOCNO,WB_QLLRF,WB_LRYNO,WB_GINBY,WB_TNKNO,WB_TPRDS,WB_MATDS,WB_CHLNO,WB_CHLQT,WB_INCTM,WB_OUTTM,WB_CHLDT,WB_GOTDT,WB_OUTTM,WB_GRSWT,WB_TARWT,WB_NETWT,WB_CHLQT from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCNO ='"+txtDOCNO.getText().trim()+"'";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					L_strDOCNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_DOCNO")," "),8);
					L_strGOTBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_GOTBY"),"_"),3);
					L_strLRYNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_LRYNO")," "),15);
					L_strTPRDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_TPRDS")," "),40);
					L_strMATDS = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_MATDS")," "),45);
					L_strCHLNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_CHLNO")," "),15);
					L_strCHLQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0");
					L_strGRSWT = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("WB_GRSWT"),"0"),14);
					L_strTARWT = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("WB_TARWT"),"0"),14);
					L_strNETWT = padSTRING('L',nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0"),14);
					L_strGINBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_GINBY"),"_"),3);
					L_strTNKNO = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_TNKNO")," "),10);
					L_strQLLRF = nvlSTRVL(M_rstRSSET.getString("WB_QLLRF"),"_");
					L_strWINBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_WINBY"),"_"),3);
					L_strWOTBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("WB_WOTBY"),"_"),3);
					L_strULDBY = nvlSTRVL(M_rstRSSET.getString("WB_ULDBY"),"_");
					L_strUCLBY = nvlSTRVL(M_rstRSSET.getString("WB_UCLBY"),"_");
					L_strMATCD = nvlSTRVL(M_rstRSSET.getString("WB_MATCD"),"_");
		 			L_tmpDATE = M_rstRSSET.getDate("WB_CHLDT");
		 			if (L_tmpDATE != null)
						L_strCHLDT = M_fmtLCDAT.format(L_tmpDATE);
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_GOTDT");
		 			if (L_tmpTIME != null)
					{
						L_strGOTTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strGOTDT = L_strGOTTM.substring(0,10);
						if(L_strGOTTM.toString().length()>=11)
							L_strGOTTM= L_strGOTTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_TFITM");
					if (L_tmpTIME != null)
					{
						L_strTFITM = M_fmtLCDTM.format(L_tmpTIME);
						L_strTFIDT = L_strTFITM.substring(0,10);
						if(L_strTFITM.toString().length()>=11)
							L_strTFITM= L_strTFITM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_SMPTM");
		 			if (L_tmpTIME != null)
					{
						L_strSMPTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strSMPDT = L_strSMPTM.substring(0,10);
						if(L_strSMPTM.toString().length()>=11)
							L_strSMPTM= L_strSMPTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_OUTTM");
		 			if (L_tmpTIME != null)
					{
						L_strOUTTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strOUTDT = L_strOUTTM.substring(0,10);
						if(L_strOUTTM.toString().length()>=11)
							L_strOUTTM= L_strOUTTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_INCTM");
		 			if (L_tmpTIME != null)
					{
						L_strINCTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strINCDT = L_strINCTM.substring(0,10);
						if(L_strINCTM.toString().length()>=11)
							L_strINCTM= L_strINCTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_GINDT");
		 			if (L_tmpTIME != null)
					{
						L_strGINTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strGINDT = L_strGINTM.substring(0,10); 
						if(L_strGINTM.toString().length()>=11)
							L_strGINTM= L_strGINTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_TSTTM");
		 			if(L_tmpTIME != null)
					{			
						L_strTSTTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strTSTDT = L_strTSTTM.substring(0,10);
						if(L_strTSTTM.toString().length()>=11)
							L_strTSTTM= L_strTSTTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_ULSTM");
		 			if(L_tmpTIME != null)
					{			
						L_strULSTM = M_fmtLCDTM.format(L_tmpTIME);
						if(L_strULSTM.toString().length()>=11)
							L_strULSTM= L_strULSTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_TFOTM");
		 			if(L_tmpTIME != null)
					{			
						L_strTFOTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strTFODT = L_strTFOTM.substring(0,10);
						if(L_strTFOTM.toString().length()>=11)
							L_strTFOTM= L_strTFOTM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_ULETM");
		 			if(L_tmpTIME != null)
					{			
						L_strULETM = M_fmtLCDTM.format(L_tmpTIME);
						if(L_strULETM.toString().length()>=11)
							L_strULETM= L_strULETM.substring(11);
					}
					L_tmpTIME = M_rstRSSET.getTimestamp("WB_UCLTM");
		 			if(L_tmpTIME != null)
					{			
						L_strUCLTM = M_fmtLCDTM.format(L_tmpTIME);
						L_strUCLDT = L_strUCLTM.substring(0,10);
						if(L_strUCLTM.toString().length()>=11)
							L_strUCLTM= L_strUCLTM.substring(11);
					}
					float L_DIFRS=Float.parseFloat(L_strNETWT)-Float.parseFloat(L_strCHLQT);
					L_strDIFQT=String.valueOf(L_DIFRS);
					
					L_strBSCHK = nvlSTRVL(M_rstRSSET.getString("WB_BSCHK"),"-");
					if(L_strBSCHK.trim().toString().equals("Y"))					
						L_strBSCHK="YES";						
					if(L_strBSCHK.trim().toString().equals("N"))					
						L_strBSCHK="NO";						
					L_strTNCHK = nvlSTRVL(M_rstRSSET.getString("WB_TNCHK"),"-");
					if(L_strTNCHK.trim().toString().equals("Y"))					
						L_strTNCHK="YES";						
					if(L_strTNCHK.trim().toString().equals("N"))					
						L_strTNCHK="NO";						
					L_strLOCHK = nvlSTRVL(M_rstRSSET.getString("WB_LOCHK"),"-");
					if(L_strLOCHK.trim().toString().equals("Y"))					
						L_strLOCHK="YES";						
					if(L_strLOCHK.trim().toString().equals("N"))					
						L_strLOCHK="NO";						
					L_strERCHK = nvlSTRVL(M_rstRSSET.getString("WB_ERCHK"),"-");
					if(L_strERCHK.trim().toString().equals("Y"))					
						L_strERCHK="YES";						
					if(L_strERCHK.trim().toString().equals("N"))					
						L_strERCHK="NO";						
					L_strEPCHK = nvlSTRVL(M_rstRSSET.getString("WB_EPCHK"),"-");
					if(L_strEPCHK.trim().toString().equals("Y"))					
						L_strEPCHK="YES";						
					if(L_strEPCHK.trim().toString().equals("N"))					
						L_strEPCHK="NO";						
					L_strSLCHK1 = nvlSTRVL(M_rstRSSET.getString("WB_SLCHK"),"-");
					if(L_strSLCHK1.trim().toString().equals("Y"))					
						L_strSLCHK1="YES";						
					if(L_strSLCHK1.trim().toString().equals("N"))					
						L_strSLCHK1="NO";					
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes("                                                                                  SPL /PRD/QR/025 \n");
			dosREPORT.writeBytes("SUPREME PETROCHEM LTD.");
			dosREPORT.writeBytes("                                                     Issue 1 Date:01/6/1998 \n");
			dosREPORT.writeBytes("TANKER ROUTING SLIP                                                        Rev   2 Date:01/6/2001 \n");
			dosREPORT.writeBytes(" ________________________________________________________________________________________________ \n");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			prnFMTCHR(dosREPORT,M_strBOLD);
			
			dosREPORT.writeBytes("| DATE                   : "+padSTRING('R',L_strGINDT,10)+"               TIME : "+padSTRING('R',L_strGINTM,6)+"             |AUTH. OFFICIAL    |\n");
			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD); 
	
			dosREPORT.writeBytes("| S.NO                   : "+L_strDOCNO+"                                           |..................|\n");
			dosREPORT.writeBytes("| TANKER NO              : "+L_strLRYNO+"                                    |                  |\n");
			dosREPORT.writeBytes("| TRANSPORTER            : "+L_strTPRDS+"           |                  |\n");
			dosREPORT.writeBytes("| CONSIGNOR              :                                                    |                  |\n");
			dosREPORT.writeBytes("| MATERIAL               : "+L_strMATDS+"      |                  |\n");
			dosREPORT.writeBytes("| CHALLAN DETAILS        : "+L_strCHLNO+"                                    |                  |\n");
			dosREPORT.writeBytes("| DATE OF LOADING AT BBY : "+padSTRING('R',L_strCHLDT,10)+"                                         |                  |\n");
			dosREPORT.writeBytes("| QUANTITY (MT)          : "+padSTRING('R',L_strCHLQT,14)+"                                     |                  |\n");
			dosREPORT.writeBytes("| TANKER HAS BEEN CHECKED FOR ALL FOREIGN MATERIAL.                           |     "+L_strGINBY+"          |\n");
			dosREPORT.writeBytes("| SEALS ARE INTACT,AND SEALED DIESEL TANK                                     |  (SECURITY)      |\n");
			dosREPORT.writeBytes("|_____________________________________________________________________________|__________________|\n");
			dosREPORT.writeBytes("|  TEST  |   UOM  | RESULTS|               SAMPLE DETAILS                     |                  |\n");
			dosREPORT.writeBytes("|________|________|________|__________________________________________________|                  |\n" );
			if(L_strQLLRF.trim().toString().length()>0)
			{
				L_strSQLQRY += "select QS_QPRCD,QS_SHRDS,QS_UOMCD";
				L_strSQLQRY +=" from CO_QSMST";				
				if(L_strMATCD.trim().toString().equals(strMAT_ST.trim().toString()))				
					L_strSQLQRY +=" where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QS_TSTLS like '%0303%'";					
				else if(L_strMATCD.trim().toString().equals(strMAT_ST1.trim().toString()))				
					L_strSQLQRY +=" where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QS_TSTLS like '%0305%'";				
				else				
					L_strSQLQRY +=" where QS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND QS_TSTLS like '%0301%'";				
				System.out.println(L_strSQLQRY);	
				L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);	
				vtrQPRCD.removeAllElements();
				if(L_rstRSSET !=null)
				{ 
					while(L_rstRSSET.next())
					{
						L_strCODCD = L_rstRSSET.getString("QS_QPRCD");
						L_strSHRDS = L_rstRSSET.getString("QS_SHRDS");
						L_strUOMCD = L_rstRSSET.getString("QS_UOMCD");
						vtrQPRCD.addElement(L_strCODCD);	
						hstTSTR.put("SMT_"+L_strCODCD+"VL",padSTRING('L',L_strSHRDS,8)+"|"+padSTRING('L',L_strUOMCD,8));
					}
					L_rstRSSET.close();
				}
				String L_strSMTVL="",L_strTEMP1="";
				
				M_strSQLQRY ="select * "; 
				M_strSQLQRY +="from QC_SMTRN";
			    M_strSQLQRY += " where SMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SMT_QCATP='11' and ltrim(str(SMT_TSTNO,20,0)) = '"+L_strQLLRF+"'";
				if(L_strMATCD.trim().toString().equals(strMAT_ST.trim().toString()))			
					M_strSQLQRY += " and SMT_TSTTP='0303'";				
				else if(L_strMATCD.trim().toString().equals(strMAT_ST1.trim().toString()))				
					M_strSQLQRY +=" and SMT_TSTTP='0305'";				
				else if(!L_strMATCD.trim().toString().equals(strMAT_ST.trim().toString())&& L_strMATCD.trim().toString().equals(strMAT_ST1.trim().toString()))				
					M_strSQLQRY += " and SMT_TSTTP='0301'";	
				M_strSQLQRY += " and ISNULL(SMT_STSFL,'') <>'X'";				
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				
				if(M_rstRSSET !=null)
				{ 
					while(M_rstRSSET.next())
					{
						L_strTSTBY = nvlSTRVL(M_rstRSSET.getString("SMT_TSTBY"),"-");
						L_strQLTFL = nvlSTRVL(M_rstRSSET.getString("SMT_QLTFL"),"-");
						if(L_strQLTFL.trim().toString().equals("Y"))						
							L_strQLTFL="OK";							
						if(L_strQLTFL.trim().toString().equals("N"))						
							L_strQLTFL="NOT OK";						
						if(vtrQPRCD.size()>0)
						{
							for(int j=0;j<vtrQPRCD.size();j++)
							{ 
								L_strSMTVL=vtrQPRCD.elementAt(j).toString().trim();
								L_strSMTVL="SMT_"+L_strSMTVL+"VL";
								L_strTEMP1=nvlSTRVL(M_rstRSSET.getString(L_strSMTVL.trim().toString()),"");	
								dosREPORT.writeBytes("|"+hstTSTR.get(L_strSMTVL)+"|"+padSTRING('L',"  "+L_strTEMP1,8)+"|");								
								if(L_intRESCT==0)								
									dosREPORT.writeBytes(" SAMPLE TAKEN AT        : "+padSTRING('L',L_strSMPDT,10)+" "+padSTRING('L',L_strSMPTM,6)+padSTRING('L',"|",8)+"                  |");								
								else if(L_intRESCT==1)								
									dosREPORT.writeBytes(" RESULT                 : "+padSTRING('R',L_strQLTFL,6)+"  "+padSTRING('L',"|",17)+"                  |" );								
								else if(L_intRESCT==2)								
									dosREPORT.writeBytes(" ANALYSIS COMPLETED AT  : "+padSTRING('R',L_strTSTDT,10)+"  "+padSTRING('R',L_strTSTTM,12)+"|                  |" );								
								else								
									dosREPORT.writeBytes(padSTRING('L',"|",51)+"                  |" );								
								dosREPORT.writeBytes("\n");
								L_intRESCT++;			
							}
						}
						if(L_intRESCT==2)						
							dosREPORT.writeBytes("|        |        |        | ANALYSIS COMPLETED AT  : "+padSTRING('R',L_strTSTDT,10)+"  "+padSTRING('R',L_strTSTTM,12)+"|                  |\n" );						
					}
					if(L_intRESCT==0)
					{
						dosREPORT.writeBytes("|        |        |        | SAMPLE TAKEN AT        : "+padSTRING('L',L_strSMPDT,10)+" "+padSTRING('L',L_strSMPTM,5)+padSTRING('L',"|",9)+"                  |\n");
						dosREPORT.writeBytes("|        |        |        | RESULT                 : "+padSTRING('R',L_strQLTFL,6)+"  "+padSTRING('L',"|",17)+"                  |\n" );
						dosREPORT.writeBytes("|        |        |        | ANALYSIS COMPLETED AT  : "+padSTRING('R',L_strTSTDT,10)+"  "+padSTRING('R',L_strTSTTM,12)+"|                  |\n" );
					}
					M_rstRSSET.close();
				}
				L_strREMDS ="";
				M_strSQLQRY ="select * "; 
				M_strSQLQRY +="from QC_RMMST";
			    M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP='11' and RM_TSTNO = '"+L_strQLLRF+"'";
				if(L_strMATCD.trim().toString().equals(strMAT_ST.trim().toString()))			
					M_strSQLQRY += " and RM_TSTTP='0303'";				
				else if(L_strMATCD.trim().toString().equals(strMAT_ST1.trim().toString()))				
					M_strSQLQRY +=" and RM_TSTTP='0305'";				
				else if(!L_strMATCD.trim().toString().equals(strMAT_ST.trim().toString())&& L_strMATCD.trim().toString().equals(strMAT_ST1.trim().toString()))				
					M_strSQLQRY += " and RM_TSTTP='0301'";	
				M_strSQLQRY += " and ISNULL(RM_STSFL,'') <>'X'";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				if(M_rstRSSET !=null)
				{ 
					if(M_rstRSSET.next())
					   L_strREMDS = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
				}
				L_strPRDRM ="";
				M_strSQLQRY ="select * "; 
				M_strSQLQRY +="from MM_RMMST";
			    M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_REMTP ='ULC' AND RM_DOCNO = '"+L_strDOCNO+"'";
                M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);	
				if(M_rstRSSET !=null)
				{ 
					if(M_rstRSSET.next())
					   L_strPRDRM = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
				}		
			}
			dosREPORT.writeBytes("|_____________________________________________________________________________|                  |\n");
      		if(L_strREMDS.length() == 0)
			{
		    	dosREPORT.writeBytes("|Remarks : "+ padSTRING('R'," ",67) + "|      "+padSTRING('R',L_strTSTBY,12)+"|\n");
		    	dosREPORT.writeBytes("|"+ padSTRING('R'," ",77) + "|    (QC-LAB)      |\n");
			}
			else if(L_strREMDS.length() <= 67)
			{
			    dosREPORT.writeBytes("|Remarks : "+ padSTRING('R',L_strREMDS,67) + "|      "+padSTRING('R',L_strTSTBY,12) +"|\n");
			    dosREPORT.writeBytes("|"+ padSTRING('R'," ",77) + "|    (QC-LAB)      |\n");
			}
			else 
			{
			    dosREPORT.writeBytes("|Remarks : "+ padSTRING('R',L_strREMDS,67) + "|      "+padSTRING('R',L_strTSTBY,12) +"|\n");
			    dosREPORT.writeBytes("|"+ padSTRING('R',L_strREMDS.substring(67),77) + "|    (QC-LAB)      |\n");
			}
			dosREPORT.writeBytes("|_____________________________________________________________________________|__________________|\n");
			dosREPORT.writeBytes("| UNLOAD THE TANKER INTO : "+L_strTNKNO+"TANK                                     |                  |\n");
			dosREPORT.writeBytes("| DATE : "+padSTRING('L',L_strUCLDT,10)+"                              TIME : "+padSTRING('L',L_strUCLTM,6)+"                |                  |\n");
			if(L_strPRDRM.length() ==0)
			{
			    dosREPORT.writeBytes("| Remarks :                                                                   |     "+  padSTRING('R',L_strUCLBY,13)+"|\n");
			    dosREPORT.writeBytes("|"+ padSTRING('R'," ",77) + "|(SHIFT-IN-CHARGE) |\n");
			}
			else if(L_strPRDRM.length() <= 67)
			{
			    dosREPORT.writeBytes("| Remarks :"+ padSTRING('R',L_strPRDRM,66) +" |     "+  padSTRING('R',L_strUCLBY,13)+"|\n");
			    dosREPORT.writeBytes("|"+ padSTRING('R'," ",77) + "|(SHIFT-IN-CHARGE) |\n");
			}
			else if(L_strPRDRM.length() > 67)
			{
			    dosREPORT.writeBytes("| Remarks :"+ padSTRING('R',L_strPRDRM,66) +" |     "+  padSTRING('R',L_strUCLBY,13)+"|\n");
			    dosREPORT.writeBytes("|"+ padSTRING('R',L_strPRDRM.substring(66),77) + "|(SHIFT-IN-CHARGE) |\n");
			}
			dosREPORT.writeBytes("|_____________________________________________________________________________|__________________|\n");
			dosREPORT.writeBytes("|                                                              |              |                  |\n");
			dosREPORT.writeBytes("| 1. TANKER IN               DATE : "+padSTRING('L',L_strTFIDT,10)+"                 |"+padSTRING('L',L_strTFITM,6)+"  HRS   |                  |\n");
			dosREPORT.writeBytes("| 2. BOTTOM SAMPLE CHECKED FOR SMELL,COLOUR AND WATER          |        "+padSTRING('R',L_strBSCHK,3)+"   |                  |\n");
			dosREPORT.writeBytes("| 3. LINE OUT CHECKED TO TANK                                  |        "+padSTRING('R',L_strLOCHK,3)+"   |                  |\n");
			dosREPORT.writeBytes("| 4. EARTHING PROVIDED TO TANKER                               |        "+padSTRING('R',L_strERCHK,3)+"   |                  |\n");
			dosREPORT.writeBytes("| 5. UNLOADING STARTED AT                                      |"+padSTRING('L',L_strULSTM,6)+"  HRS   |                  |\n");
			dosREPORT.writeBytes("| 6. UNLOADING COMPLETED AT                                    |"+padSTRING('L',L_strULETM,6)+"  HRS   |                  |\n");
			dosREPORT.writeBytes("| 7. TANKER CHECKED FROM TOP & IS EMPTY                        |        "+padSTRING('R',L_strEPCHK,3)+"   |  "+padSTRING('L',L_strULDBY,8)+"        |\n");
			dosREPORT.writeBytes("| 8. TANKER SENT OUT         DATE : "+padSTRING('L',L_strTFODT,10)+"                 |"+padSTRING('L',L_strTFOTM,6)+"  HRS   |  (  TANKFARM  )  |\n");
			dosREPORT.writeBytes("|______________________________________________________________|______________|__________________|\n");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);		
			dosREPORT.writeBytes("| WEIGHT                    DATE                 TIME          |            MT|                  |\n");			
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("| GROSS WEIGHT              "+padSTRING('R',L_strINCDT,10)+"           "+padSTRING('R',L_strINCTM,6)+"        |"+L_strGRSWT+"|      "+L_strWINBY+"         |\n");
			dosREPORT.writeBytes("| TARE WEIGHT               "+padSTRING('R',L_strOUTDT,10)+"           "+padSTRING('R',L_strOUTTM,6)+"        |"+L_strTARWT+"|      "+L_strWOTBY+"         |\n");
			dosREPORT.writeBytes("| NET WEIGHT(A)                                                |"+L_strNETWT+"|                  |\n");
			dosREPORT.writeBytes("| CHALLAN WEIGHT(B)                                            |"+padSTRING('L',L_strCHLQT,14)+"|                  |\n");
			dosREPORT.writeBytes("| DIFFERENCE WEGHT(A)-(B)                                      |"+padSTRING('L',setNumberFormat(Double.valueOf(L_strDIFQT).doubleValue(),3),14)+"| ( WEIGH BRIDGE ) |\n");
			dosREPORT.writeBytes("|______________________________________________________________|______________|__________________|\n");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes("|TANKER HAS BEEN SENT OUT AT : "+padSTRING('R',L_strGOTTM,6)+"             ON : "+padSTRING('R',L_strGOTDT,10)+"             |     "+L_strGOTBY+"          |\n");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes("|                                                                             |   (SECURITY)     |\n");
			dosREPORT.writeBytes("|_____________________________________________________________________________|__________________|\n");
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>"); 			
			setCursor(cl_dat.M_curDFSTS_pbst);			
			dosREPORT.close();			
			fosREPORT.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	Method to validate DATA before execution, Check for blank and wrong Input.
	*/
	boolean vldDATA()
	{
		try
		{
			if(txtDOCNO.getText().trim().length()== 0)
			{
				setMSG("please enter the number",'E');
				return false;
			}
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount()==0)
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			
		}
		return true;
	}
}