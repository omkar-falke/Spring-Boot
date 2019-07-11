/*
System Name		: 
Program Name	: Part A Report.
Author			: Mr. S.R.Mehesare
Modified Date	: 16/12/2005
Documented Date	: 
Version			: MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
import java.awt.event.KeyEvent;import java.sql.ResultSet;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;
import java.util.StringTokenizer;import java.util.Hashtable;
import javax.swing.JComponent;import javax.swing.InputVerifier;
import java.util.Vector;
/**<pre>
System Name : 

Program Name : Part A Report.

Purpose : Program to generate the Report for the selected Complaint Type 
				  and given reg. No.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from  for condiations :-
    
<B>Validations & Other Information:</B>    
    
</I> */

public class co_rpcas extends cl_rbase
{										/** JTextField to display & to specify the Registration Number to generate the Report.*/
	private JTextField txtREGNO;		/** JTextField to display & to specify the Branch Reg. No to generate the report.*/
	//private JTextField txtBREGN;		/** JComboBox to display & to select the Comlaint Type. */
	private JComboBox cmbCMPTP;			/** JComboBox to display & to select the Report Type.*/
	private JComboBox cmbPRTNO;			/** String variable for generated Report File Name.*/
	private String strFILNM;			/** Integer Variable to count the Number of records fetched to block the Report if No data Found.*/
	private int intRECCT;				/** DataOutputStream to generate hld the stream of data.*/
	private DataOutputStream dosREPORT;	/** FileOutputStream to generate the Report file form stream of data.*/
	private FileOutputStream fosREPORT;	/** String variable to print Dotted Line in the Report.*/	
	private String strDOTLN = "-----------------------------------------------------------------------------------------";
										/** String varaible to hold the ISO Number fetched from the database.*/
	private String strISOD1 = "";		/** String varaible to hold the ISO Number fetched from the database.*/
	private String strISOD2 = "";		/** String varaible to hold the ISO Number fetched from the database.*/
	private String strISOD3 = "";		/** StringTokinizer user to allign the remark Description.*/
	private StringTokenizer stkREMDS;
	
	String strCMPTP = "01";
	String strSBSCD="";	
	String strREGNO;
	String strPRTNO;
	String strCLSNM = "";
	Hashtable<String,String> hstCMPTP = new Hashtable<String,String>();
	Hashtable<String,String> hstDPTNM = new Hashtable<String,String>();
	private INPVF objINPVR = new INPVF();
	public co_rpcas()
	{
		super(1);
		try
		{
			//System.out.println("strSBSCD "+M_strSBSCD);
			strCLSNM = getClass().getName();
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			add(new JLabel("Comlaint Type"),3,4,1,1,this,'L');
			add(cmbCMPTP = new JComboBox(),3,5,1,1.5,this,'L');
			add(new JLabel("Report Type"),4,4,1,1,this,'L');
			add(cmbPRTNO = new JComboBox(),4,5,1,1.5,this,'L');
			add(new JLabel("Reg.No"),5,4,1,1,this,'L');
			add(txtREGNO = new TxtNumLimit(10),5,5,1,1.5,this,'L');
			//add(new JLabel("Branch Reg."),6,4,1,1,this,'L');
			//add(txtBREGN = new TxtNumLimit(10),6,5,1,1.5,this,'L');				
			
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			txtREGNO.setInputVerifier(objINPVR);
			strPRTNO = "A";
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
			+" CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET= cl_dat.exeSQLQRY1(M_strSQLQRY);
			{
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
						hstDPTNM.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					M_rstRSSET.close();
				}
			}
			strSBSCD = M_strSBSCD;
			//System.out.println("strSBSCD "+strSBSCD);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public co_rpcas(String P_strSBSCD,String P_strCMPTP,String P_strREGNO)
	{
		//super(1);
		try
		{
			strCLSNM = getClass().getName();
			//setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
			+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXCMT'";
			M_rstRSSET= cl_dat.exeSQLQRY1(M_strSQLQRY);
			{
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
						hstCMPTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					M_rstRSSET.close();
				}
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
			+" CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET= cl_dat.exeSQLQRY1(M_strSQLQRY);
			{
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
						hstDPTNM.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
					M_rstRSSET.close();
				}
			}
			strSBSCD = P_strSBSCD;
			strCMPTP = P_strCMPTP;
			strREGNO = P_strREGNO;
			M_rdbHTML.setSelected(true);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					if(cmbCMPTP.getItemCount() == 0)
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXCMT'";
						M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
						{
							if(M_rstRSSET != null)
							{
								String L_strCODCD="",L_strCODDS="";
								while(M_rstRSSET.next())
								{
									L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
									L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
									cmbCMPTP.addItem(L_strCODCD+" "+L_strCODDS);
									hstCMPTP.put(L_strCODCD,L_strCODDS);
								}
								M_rstRSSET.close();
							}
						}
						cmbPRTNO.addItem("A Part");
						cmbPRTNO.addItem("B Part");
						cmbPRTNO.addItem("C Part");
						cmbPRTNO.addItem("D Part");
					}
					setENBL(true);
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == cmbCMPTP)
			{
				txtREGNO.requestFocus();
				strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);
			}
			else if(M_objSOURC == txtREGNO)
				strREGNO = txtREGNO.getText().trim();
			else if(M_objSOURC == cmbPRTNO)
			{
				txtREGNO.requestFocus();
				strPRTNO = cmbPRTNO.getSelectedItem().toString().substring(0,1);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}	
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);		
		if(M_objSOURC == cmbCMPTP)
			setMSG("Please select Complate type to generate the report..",'N');
		else if(M_objSOURC == cmbPRTNO)
			setMSG("Please select Part Number to generate the report..",'N');
		else if(M_objSOURC == txtREGNO)
			setMSG("Please Enter Reg. No. Or press F1 to select from List..",'N');				
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			try
			{
				if(M_objSOURC == txtREGNO)
				{
					M_strHLPFLD = "txtREGNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strSQLQRY =" SELECT distinct CM_REGNO,CM_BRNRG,CM_REPDT,CM_INVDT,CM_PRTNM from CO_CMMST,CO_CMTRN where"
					+" isnull(CM_STSFL,'')<>'X'" // AND CM_SBSCD ='"+ M_strSBSCD +"'"
					+" AND CM_CMPTP = '"+ strCMPTP +"'"
					+" AND CMT_PRTNO = '"+ cmbPRTNO.getSelectedItem().toString().substring(0,1)+"'"
					+" AND CMT_REGNO = CM_REGNO";// AND CMT_REMTP = CM_REMTP";
					if(txtREGNO.getText().length() >0)
						M_strSQLQRY += " AND CM_REGNO like '"+txtREGNO.getText().trim()+"%'";										
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Reg. No.","Regional Reg.No.","Reg.Date","Invoice Date","Party Name"},5,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"VK_F1");
			}
		}
		else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{					
			if(M_objSOURC == cmbCMPTP)
			{				
				txtREGNO.requestFocus();
			}
			else if(M_objSOURC == txtREGNO)
			{
				if(txtREGNO.getText().trim().length() > 0)					
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}	
	}
	/**
	 * Super class Method overrrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{		
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtREGNO"))
		{			
			cl_dat.M_flgHELPFL_pbst = false;			
			txtREGNO.setText(cl_dat.M_strHLPSTR_pbst);
		}		
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	public void exePRINT()
	{
		if(!vldDATA())
			return;
		try
		{						
//			getDATA("A");
			//if(cmbRPTNO.getSelectedItem().toString().substring(0,2);
		//	System.out.println("strSBSCD "+strSBSCD);
			if(strSBSCD == null)
				strSBSCD = M_strSBSCD;
			if(strPRTNO.equals("A"))
				getDATA(strSBSCD,strCMPTP,strREGNO,"A");
			else if(strPRTNO.equals("B"))
				getDATA_B(strSBSCD,strCMPTP,strREGNO,"B");
			else if(strPRTNO.equals("C"))
				getDATA(strSBSCD,strCMPTP,strREGNO,"C");
			else if(strPRTNO.equals("D"))
				getDATA(strSBSCD,strCMPTP,strREGNO,"D");
			
			if(intRECCT == 0)
			{
		//		System.out.println("msg from 2");
				setMSG("No Data found or Investigation In Progress..",'E');
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
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Customer Complaint Analysis"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to validate the data before execuation of the SQL Quary.
	 */
	boolean vldDATA()
	{
		try
		{			
			cl_dat.M_PAGENO = 0;			
			if(txtREGNO.getText().trim().length() == 0)
			{
				setMSG("Please Enter Reg. No Or Press F1 to select from List..",'E');
				txtREGNO.requestFocus();
				return false;
			}			
			/*if(txtBREGN.getText().trim().length() == 0)
			{
				setMSG("Please Enter Branch Reg. No Or Press F1 to select from List..",'E');
				txtBREGN.requestFocus();
				return false;
			}*/			
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA(String P_strSBSCD,String P_strCMPTP,String P_strREGNO,String P_strPRTNO)
	{			
		try
		{
			/*if(strISOD1.equals(""))
			{
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_SHRDS from CO_CDTRN where CMT_CGMTP ='ISO'"							 
					+" AND CMT_CGSTP = 'COXXCAS' AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like 'D"+cl_dat.M_strCMPCD_pbst+strCMPTP+"%'";
				
				System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					String L_strTEMP = "";
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"");
						if(L_strTEMP.equals("DOC1"))
							strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("DOC2"))
							strISOD2 = "ISSUE NO./DATE :"+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("DOC3"))
							strISOD3 = "REV NO./DATE :"+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					}
					M_rstRSSET.close();
				}
			}*/
			
			setISODTL(P_strCMPTP);
						
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcas"+ P_strPRTNO +".html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcas"+ P_strPRTNO +".doc";						
			
			intRECCT = 0; 
			String L_strREMTP ="";
			String L_strRMRK1 = "";
			String L_strRMRK2 = "";
			String L_strRMRK3 = "";
			String L_strRMRK4 = "";
			String L_strDAT1="",L_strDAT2="",L_strDAT3="",L_strDAT4="";
			String L_strRMBY1="",L_strRMBY2="",L_strRMBY3="",L_strRMBY4="";
			String L_strPRTNM="",L_strREGNO="",L_strBREGN="",L_strCONPR="",L_strADD1="",L_strADD2="";
			String L_strTPRNM="",L_strREGDT="",L_strDORNO="",L_strDORDT="",L_strPRDDS="",L_strINVNO="";
			String L_strINVDT="",L_strLOTNO="",L_strPRDQT="";
			String L_strTOTQT= "0.000";
			java .sql.Date L_tmpDATE;
			setCursor(cl_dat.M_curWTSTS_pbst);			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Customer Complaint Analysis</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			if(P_strPRTNO.equals("A"))
			{
				//prnHEADER();
				M_strSQLQRY = "Select CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM,"
				+"CM_CONPR,CM_ORDNO,CM_ORDDT,CM_INVNO,CM_INVDT,CM_INVQT,CM_MMSBS,CM_GRNNO,CM_TPRCD,CM_PRDCD,"
				+"CM_LOTNO,CM_ADLOT,CM_CMPQT,CM_SMPQT,CM_LOTNO from CO_CMMST"
				+" Where isnull(CM_STSFL,'') <>'X' AND CM_SBSCD = '"+ P_strSBSCD +"' AND CM_REGNO ='"+ P_strREGNO +"'"
				+" AND CM_CMPTP = '"+ P_strCMPTP +"'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
				if( M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						intRECCT = 1;
						L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("CM_LOTNO"),"");
						L_strBREGN = nvlSTRVL(M_rstRSSET.getString("CM_BRNRG"),"");
						L_tmpDATE = M_rstRSSET.getDate("CM_REPDT");
						if(L_tmpDATE != null)								
							L_strREGDT = M_fmtLCDAT.format(L_tmpDATE);
						
						L_strPRDQT = nvlSTRVL(M_rstRSSET.getString("CM_CMPQT"),"");
							
						M_strSQLQRY =" SELECT PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
						if(strCMPTP.equals("03"))
							M_strSQLQRY += " AND PT_PRTTP ='S'";
						else
							M_strSQLQRY += " AND PT_PRTTP ='C'";
						M_strSQLQRY += " AND PT_PRTCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_PRTCD"),"").trim().toUpperCase()+"'";
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
							{									
								L_strADD1 = nvlSTRVL(L_rstRSSET.getString("PT_ADR01"),"")+", "+nvlSTRVL(L_rstRSSET.getString("PT_ADR02"),"");
								L_strADD2 = nvlSTRVL(L_rstRSSET.getString("PT_ADR03"),"")+", "+nvlSTRVL(L_rstRSSET.getString("PT_ADR04"),"")+"  "+nvlSTRVL(L_rstRSSET.getString("PT_CTYNM"),"");
							}
							L_rstRSSET.close();
						}
						L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),"");
						L_strCONPR = nvlSTRVL(M_rstRSSET.getString("CM_CONPR"),"");
						L_strDORNO = nvlSTRVL(M_rstRSSET.getString("CM_ORDNO"),"");
						L_tmpDATE = M_rstRSSET.getDate("CM_ORDDT");
						if(L_tmpDATE != null)								
							L_strDORDT = M_fmtLCDAT.format(L_tmpDATE);
						L_strINVNO = nvlSTRVL(M_rstRSSET.getString("CM_INVNO"),"");
					
						L_tmpDATE = M_rstRSSET.getDate("CM_INVDT");
						if(L_tmpDATE != null)								
							L_strINVDT = M_fmtLCDAT.format(L_tmpDATE);
						
						M_strSQLQRY =" SELECT PT_PRTNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
						M_strSQLQRY += " AND PT_PRTTP ='T'";
						M_strSQLQRY += " AND PT_PRTCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_TPRCD"),"").trim().toUpperCase()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
								L_strTPRNM = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");
							L_rstRSSET.close();
						}						
						
						M_strSQLQRY =" SELECT PR_PRDDS from CO_PRMST where isnull(PR_STSFL,'')<>'X'";						
						M_strSQLQRY += " AND PR_PRDCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_PRDCD"),"").trim().toUpperCase()+"'";
						L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
								L_strPRDDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
							L_rstRSSET.close();
						}
						L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("CM_LOTNO"),"");
					}
					M_rstRSSET.close();
				}
				//cl_dat.M_PAGENO++;			
				dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISOD1,strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISOD2,strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISOD3,strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
				dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst);
				dosREPORT.writeBytes("\n"+padSTRING('R',"Customer Complaint Analysis Form",strDOTLN.length()-21)+"\n");
				dosREPORT.writeBytes(padSTRING('R',"Complaint Type : "+ hstCMPTP.get(P_strCMPTP).toString(),strDOTLN.length()-21));
				dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");
				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes(padSTRING('L',"PART A",(strDOTLN.length()/2)+3)+"\n");
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes(strDOTLN+"\n");
				
				dosREPORT.writeBytes("\n"+"Customer Name  : ");				
				dosREPORT.writeBytes(padSTRING('R',L_strPRTNM,46));
				dosREPORT.writeBytes(padSTRING('R',"HO Reg No.    : ",16));
				dosREPORT.writeBytes(P_strREGNO);
				dosREPORT.writeBytes("\n"+"Contact Person : ");
				dosREPORT.writeBytes(padSTRING('R',L_strCONPR,46));
				dosREPORT.writeBytes(padSTRING('R',"Branch Reg No.: ",16));
				dosREPORT.writeBytes(L_strBREGN);

				dosREPORT.writeBytes("\n"+"Address        : ");
				//dosREPORT.writeBytes(L_strADD1);
				String L_strTMP = L_strADD1;
				if(L_strADD1.length()>40)
				{
					dosREPORT.writeBytes(L_strADD1.substring(0,40));
					L_strTMP = L_strADD1.substring(40,L_strADD1.length());
				}
				else
				{
					dosREPORT.writeBytes(L_strADD1);
					L_strTMP="";
				}
				while(L_strTMP.length()>0)
				{
					dosREPORT.writeBytes("\n"+"                 ");
					if(L_strTMP.length()>40)
					{
						dosREPORT.writeBytes(L_strTMP.substring(0,40));
						L_strTMP = L_strTMP.substring(40,L_strTMP.length());
					}
					else
					{
						dosREPORT.writeBytes(L_strTMP);
						L_strTMP="";
					}
				}
				dosREPORT.writeBytes("\n"+padSTRING('R',"",17));			
				dosREPORT.writeBytes(L_strADD2);

				dosREPORT.writeBytes("\n"+"Transporter    : ");
				dosREPORT.writeBytes(padSTRING('R',L_strTPRNM,46));
				dosREPORT.writeBytes(padSTRING('R',"Date Reported : ",16));
				dosREPORT.writeBytes(L_strREGDT);

				dosREPORT.writeBytes("\n"+"D.O.No. / Date : ");
				dosREPORT.writeBytes(padSTRING('R',L_strDORNO +" "+L_strDORDT,46));
				dosREPORT.writeBytes("Lot No        : ");
				dosREPORT.writeBytes(L_strLOTNO);

				dosREPORT.writeBytes("\n"+"Invoice No/Date: ");
				dosREPORT.writeBytes(padSTRING('R',L_strINVNO +" "+L_strINVDT,46));
				dosREPORT.writeBytes("Quantity      : ");
				dosREPORT.writeBytes(L_strPRDQT+" MT");

				dosREPORT.writeBytes("\n"+"Grade          : ");
				dosREPORT.writeBytes(L_strPRDDS);			
				dosREPORT.writeBytes("\n"+strDOTLN);

				M_strSQLQRY = "select CMT_REMDT,CMT_REMTP,CMT_REMDS,CMT_REMBY from CO_CMTRN where CMT_SBSCD = '"+ strSBSCD+"'"
				+" AND CMT_CMPTP = '"+ P_strCMPTP +"'"
				+" AND CMT_REGNO = '"+ P_strREGNO +"' AND isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_PRTNO = '"+ P_strPRTNO +"'";				
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						L_strREMTP = nvlSTRVL(M_rstRSSET.getString("CMT_REMTP"),"");
						if(L_strREMTP.equals("CMDTL"))
						{
							L_strRMRK1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
							L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
							if(L_tmpDATE != null)
								L_strDAT1 = M_fmtLCDAT.format(L_tmpDATE);
							L_strRMBY1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						}
						else if(L_strREMTP.equals("IMDAC"))
						{
							L_strRMRK2 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
							L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
							if(L_tmpDATE != null)
								L_strDAT2 = M_fmtLCDAT.format(L_tmpDATE);
							L_strRMBY2 = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						}
						else if(L_strREMTP.equals("COMCS"))
						{
							L_strRMRK3 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
							L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
							if(L_tmpDATE != null)
								L_strDAT3 = M_fmtLCDAT.format(L_tmpDATE);
							L_strRMBY3 = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						}
						else if(L_strREMTP.equals("COMED"))
						{
							L_strRMRK4 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
							L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
							if(L_tmpDATE != null)
								L_strDAT4 = M_fmtLCDAT.format(L_tmpDATE);
							L_strRMBY4 = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						}
					}
					M_rstRSSET.close();
				}
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);	
				dosREPORT.writeBytes("\n"+"Details of the Complaint : "+"\n\n");
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				//dosREPORT.writeBytes(rmkALLGN(L_strRMRK1));
				dosREPORT.writeBytes(L_strRMRK1);
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT1 +"                                                              "+L_strRMBY1);
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes("\n"+strDOTLN);
				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n"+"Immediate disposition action taken / recommended : "+"\n\n");
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				//dosREPORT.writeBytes(rmkALLGN(L_strRMRK2));
				dosREPORT.writeBytes(L_strRMRK2);				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT2 +"                                                              "+L_strRMBY2);
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes("\n"+strDOTLN);
				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n"+"Comment of HOD (CSS) : "+"\n\n");
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				//dosREPORT.writeBytes(rmkALLGN(L_strRMRK3));
				dosREPORT.writeBytes(L_strRMRK3);				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT3 +"                                                              HOD (CSS)");
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes("\n"+strDOTLN);			
				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n"+"Complaint to be resolved by / Actual time period for resolving complaint :"+"\n\n");				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				//dosREPORT.writeBytes(rmkALLGN(L_strRMRK4));
				dosREPORT.writeBytes(L_strRMRK4);
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				dosREPORT.writeBytes("\n\n"+"Sent on Date : "+ L_strDAT4 +"                                                            ED (Styrenics)");
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				
				dosREPORT.writeBytes("\n"+strDOTLN);
				dosREPORT.writeBytes("\n"+"Distribution : ");
				if(strCMPTP.equals("02"))
				    dosREPORT.writeBytes("\n\n"+"      CE (O),   QCA,         MHD,          MKT(HO),        HOB,     PRD(PS),    PRD(SPS)");
				else
				    dosREPORT.writeBytes("\n\n"+"      CE (O),      CMS,            HOB,              MHD,             COMM(Excise) Works");
			}
			else //if(P_strPRTNO.equals("B"))
			{
			//	String L_strREMTP = "";
				int L_intTEMP = 0;
				//System.out.println("else");
				M_strSQLQRY = "Select CM_BRNRG,CM_PRTNM,CM_STSFL,CM_PRDCD,CM_LOTNO from CO_CMMST Where "+(strCLSNM.equals("co_rpca1") ? "isnull(CM_STSFL,'') >= '5'" : "isnull(CM_STSFL,'') <>'X'")
				+" AND CM_SBSCD = '"+ P_strSBSCD +"'"
				+" AND CM_REGNO = '"+ P_strREGNO +"'"
				+" AND CM_CMPTP = '"+ P_strCMPTP +"'";
				//System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
				if( M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						//System.out.println("part b rs not null");
						intRECCT = 1;
						L_strBREGN = nvlSTRVL(M_rstRSSET.getString("CM_BRNRG"),"");
						L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),"");
						L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("CM_LOTNO"),"");
						
						M_strSQLQRY =" SELECT PR_PRDDS from CO_PRMST where isnull(PR_STSFL,'')<>'X'";						
						M_strSQLQRY += " AND PR_PRDCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_PRDCD"),"").trim().toUpperCase()+"'";
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(L_rstRSSET != null)
						{
							if(L_rstRSSET.next())
								L_strPRDDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
							L_rstRSSET.close();
						}						
						L_intTEMP = Integer.valueOf(nvlSTRVL(M_rstRSSET.getString("CM_STSFL"),"")).intValue();
						
						//if part B Report & CSS User the inv details are only available after the authorization.			
						if((P_strPRTNO.equals("B")) && (L_intTEMP <= 5) && (cl_dat.M_strUSRTP_pbst.equals("CO131"))) //CO131
						{
							intRECCT = 0;
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strCPI10);
							dosREPORT.close();
							fosREPORT.close();
							//System.out.println("msg from 1");
							setMSG("Investigation in progress details are not available..",'E');
							return;
						}
					}
					M_rstRSSET.close();
				}
				dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISOD1,strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISOD2,strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',strISOD3,strDOTLN.length())+"\n");
				dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
				dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst);
				dosREPORT.writeBytes("\n"+padSTRING('R',"Customer Complaint Analysis Form",strDOTLN.length()-21)+"\n");
				dosREPORT.writeBytes(padSTRING('R',"Complaint Type : "+ hstCMPTP.get(P_strCMPTP).toString(),strDOTLN.length()-21));
				dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");
				
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("<b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strBOLD);
				
				dosREPORT.writeBytes(padSTRING('L',"PART "+P_strPRTNO,(strDOTLN.length()/2)+3)+"\n");
				if(M_rdbHTML.isSelected())
    				dosREPORT.writeBytes("</b>");
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				dosREPORT.writeBytes(strDOTLN+"\n");
				
				if((P_strPRTNO.equals("B")) ||(P_strPRTNO.equals("C")))
				{
					dosREPORT.writeBytes("\n"+"Customer Name : " +L_strPRTNM);
					dosREPORT.writeBytes("\n"+"HO Reg No.    : " +P_strREGNO);
					dosREPORT.writeBytes("\n"+"Branch Reg No.: " +L_strBREGN);
					dosREPORT.writeBytes("\n"+strDOTLN);
				}				
				else if(P_strPRTNO.equals("D"))
				{
					dosREPORT.writeBytes("\n"+padSTRING('R',"Customer Name : " +L_strPRTNM,65));
					dosREPORT.writeBytes("HO Reg No.     : " +P_strREGNO);
					dosREPORT.writeBytes("\n"+padSTRING('R',"Grade         : " +L_strPRDDS,65));
					dosREPORT.writeBytes("Branch Reg No. : " +L_strBREGN);
					dosREPORT.writeBytes("\n"+padSTRING('R',"Lot Number    : " +L_strLOTNO,65));
					dosREPORT.writeBytes("Total Quantity : " +L_strTOTQT);
					dosREPORT.writeBytes("\n"+strDOTLN);
				}
				
				M_strSQLQRY = "select CMT_REMDT,CMT_REMDS,CMT_REMBY,CMT_DPTCD,CMT_REMTP from CO_CMTRN where isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_SBSCD = '"+ strSBSCD +"'"
				+" AND CMT_CMPTP = '"+ P_strCMPTP +"'"
				+" AND CMT_REGNO = '"+ P_strREGNO +"'"
				+" AND CMT_PRTNO = '"+ P_strPRTNO +"'"
				+" Order By CMT_REMTP desc,CMT_DPTCD";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					String L_strDPTCD = "";
					while(M_rstRSSET.next())
					{
						L_strREMTP = nvlSTRVL(M_rstRSSET.getString("CMT_REMTP"),"");
						L_strRMRK1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
						L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
						if(hstDPTNM.containsKey(L_strDPTCD))
							L_strDPTCD = hstDPTNM.get(L_strDPTCD).toString();
						
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)
							L_strDAT1 = M_fmtLCDAT.format(L_tmpDATE);
						L_strRMBY1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
						
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);	
						if(P_strPRTNO.equals("B"))
						{
							if(L_strREMTP.equals("AUTRM"))
								dosREPORT.writeBytes("\n"+"Authorization Remarks : \n\n");
							else
								dosREPORT.writeBytes("\n"+"Investigation By : "+ L_strDPTCD +"\n\n");
						}
						else if(P_strPRTNO.equals("C"))
							dosREPORT.writeBytes("\n"+"Recommendation / Resolution of Complaint" +"\n\n");
						else if(P_strPRTNO.equals("D"))
							dosREPORT.writeBytes("\n"+"Corrective and Preventive Action By : "+ L_strDPTCD +"\n\n");
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						//dosREPORT.writeBytes(rmkALLGN(L_strRMRK1));
						dosREPORT.writeBytes(L_strRMRK1);
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
						//if(P_strPRTNO.equals("B"))
							dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT1 +"                                                                  "+L_strRMBY1);
						/*else if(P_strPRTNO.equals("B"))
							dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT1 +"                                                                  "+L_strRMBY1);
						else if(P_strPRTNO.equals("D"))
							dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT1 +"                                                                  "+L_strRMBY1);*/
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n"+strDOTLN);
					}
					M_rstRSSET.close();
				}				
			}
		/*	else if(P_strPRTNO.equals("C"))
			{
			
			}
			else if(P_strPRTNO.equals("D"))
			{
			
			}*/
		
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
	}
	
	private void setISODTL(String LP_CMPTP)
	{
		try
		{
			strISOD1="";
			strISOD2="";
			strISOD3="";
			
			String L_strCMPTP = "";
			if(LP_CMPTP.equals("02"))
				L_strCMPTP = "T";
			else if(LP_CMPTP.equals("01"))
				L_strCMPTP = "C";
			else 
			{
				setMSG("document control with revision does not exist for this Complaint Type",'E');
				return;
			}
			if(L_strCMPTP.length() > 0)
			{
				System.out.println("LP_CMPTP>>"+LP_CMPTP);
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_SHRDS from CO_CDTRN where CMT_CGMTP ='ISO'"							 
					+" AND CMT_CGSTP = 'COXXRPT' AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like 'CO_RPCAS_"+L_strCMPTP+"%'";
				
				System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					String L_strTEMP = "";
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD").substring(10,12),"");
						if(L_strTEMP.equals("01"))
							strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("02"))
							strISOD2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("03"))
							strISOD3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					}
					M_rstRSSET.close();
				}
			}
		}
		catch(Exception E)
		{
			System.out.println("error in setISODTL() : "+E);
		}
	}
		
	
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA_B(String P_strSBSCD,String P_strCMPTP,String P_strREGNO,String P_strPRTNO)
	{			
		try
		{
			/*if(strISOD1.equals(""))
			{
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS,CMT_SHRDS from CO_CDTRN where CMT_CGMTP ='ISO'"							 
					+" AND CMT_CGSTP = 'COXXCAS' AND isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD like 'D"+cl_dat.M_strCMPCD_pbst+strCMPTP+"%'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					String L_strTEMP = "";
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"");
						if(L_strTEMP.equals("DOC1"))
							strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("DOC2"))
							strISOD2 = "ISSUE NO./DATE :"+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("DOC3"))
							strISOD3 = "REV NO./DATE :"+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					}
					M_rstRSSET.close();
				}
			}*/
			setISODTL(P_strCMPTP);
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcas"+ P_strPRTNO +".html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcas"+ P_strPRTNO +".doc";						
			
			intRECCT = 0; 
			String L_strREMTP ="";
			String L_strCPABY ="";
			String L_strRMRK1 = "";
			String L_strRMRK2 = "";
			String L_strRMRK3 = "";
			String L_strRMRK4 = "";
			String L_strDAT1="",L_strDAT2="",L_strDAT3="",L_strDAT4="";
			String L_strRMBY1="",L_strRMBY2="",L_strRMBY3="",L_strRMBY4="";
			String L_strPRTNM="",L_strREGNO="",L_strBREGN="",L_strCONPR="",L_strADD1="",L_strADD2="";
			String L_strTPRNM="",L_strREGDT="",L_strDORNO="",L_strDORDT="",L_strPRDDS="",L_strINVNO="";
			String L_strINVDT="",L_strLOTNO="",L_strPRDQT="";
			String L_strTOTQT= "0.000";
			java .sql.Date L_tmpDATE;
			setCursor(cl_dat.M_curWTSTS_pbst);			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Customer Complaint Analysis</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
		//	String L_strREMTP = "";
			int L_intTEMP = 0;
			//System.out.println("else");
			M_strSQLQRY = "Select CM_BRNRG,CM_PRTNM,CM_STSFL,CM_PRDCD,CM_LOTNO from CO_CMMST Where isnull(CM_STSFL,'') <>'X'"
			+" AND CM_SBSCD = '"+ P_strSBSCD +"'"
			+" AND CM_REGNO = '"+ P_strREGNO +"'"
			+" AND CM_CMPTP = '"+ P_strCMPTP +"'";
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
			if( M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					//System.out.println("part b rs not null");
					intRECCT = 1;
					L_strBREGN = nvlSTRVL(M_rstRSSET.getString("CM_BRNRG"),"");
					L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),"");
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("CM_LOTNO"),"");
						
					M_strSQLQRY =" SELECT PR_PRDDS from CO_PRMST where isnull(PR_STSFL,'')<>'X'";						
					M_strSQLQRY += " AND PR_PRDCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_PRDCD"),"").trim().toUpperCase()+"'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
							L_strPRDDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
						L_rstRSSET.close();
					}						
					L_intTEMP = Integer.valueOf(nvlSTRVL(M_rstRSSET.getString("CM_STSFL"),"")).intValue();
						
					//if part B Report & CSS User the inv details are only available after the authorization.			
					if((P_strPRTNO.equals("B")) && (L_intTEMP <= 5) && (cl_dat.M_strUSRTP_pbst.equals("CO131"))) //CO131
					{
						intRECCT = 0;
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strCPI10);
						dosREPORT.close();
						fosREPORT.close();
						//System.out.println("msg from 1");
						setMSG("Investigation in progress details are not available..",'E');
						return;
					}
				}
				M_rstRSSET.close();
			}
			dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD1,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD2,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD3,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst);
			dosREPORT.writeBytes("\n"+padSTRING('R',"Customer Complaint Analysis Form",strDOTLN.length()-21)+"\n");
			dosREPORT.writeBytes(padSTRING('R',"Complaint Type : "+ hstCMPTP.get(P_strCMPTP).toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");
				
			if(M_rdbHTML.isSelected())
    			dosREPORT.writeBytes("<b>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
				
			dosREPORT.writeBytes(padSTRING('L',"PART "+P_strPRTNO,(strDOTLN.length()/2)+3)+"\n");
			if(M_rdbHTML.isSelected())
    			dosREPORT.writeBytes("</b>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(strDOTLN+"\n");
				
			if((P_strPRTNO.equals("B")) ||(P_strPRTNO.equals("C")))
			{
				dosREPORT.writeBytes("\n"+"Customer Name : " +L_strPRTNM);
				dosREPORT.writeBytes("\n"+"HO Reg No.    : " +P_strREGNO);
				dosREPORT.writeBytes("\n"+"Branch Reg No.: " +L_strBREGN);
				dosREPORT.writeBytes("\n"+strDOTLN);
			}				
			else if(P_strPRTNO.equals("D"))
			{
				dosREPORT.writeBytes("\n"+padSTRING('R',"Customer Name : " +L_strPRTNM,65));
				dosREPORT.writeBytes("HO Reg No.     : " +P_strREGNO);
				dosREPORT.writeBytes("\n"+padSTRING('R',"Grade         : " +L_strPRDDS,65));
				dosREPORT.writeBytes("Branch Reg No. : " +L_strBREGN);
				dosREPORT.writeBytes("\n"+padSTRING('R',"Lot Number    : " +L_strLOTNO,65));
				dosREPORT.writeBytes("Total Quantity : " +L_strTOTQT);
				dosREPORT.writeBytes("\n"+strDOTLN);
			}
				
			////departments to be fetched are strored in vector
			Vector<String> L_vtrDPTCD=new Vector<String>(); 	
			String L_strDPTCD_RQD="";
			try
			{

				String L_strSQLQRY =" SELECT SUBSTRING(CMT_CODCD,4,3) CMT_CODCD from CO_CDTRN where"
				+" CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXIDP' AND SUBSTRING(CMT_CODCD,1,2) = '"+ P_strCMPTP +"'  order by CMT_CHP01";
				//System.out.println("L_strSQLQRY>>>>"+L_strSQLQRY);
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET!=null)
				{
					while(L_rstRSSET.next())
					{	
						//StringTokenizer L_STRTKN=new StringTokenizer(L_rstRSSET.getString("CMT_CODDS"),"_");
						//while(L_STRTKN.hasMoreElements())
						//	L_vtrDPTCD.add(L_STRTKN.nextToken());
							L_vtrDPTCD.add(L_rstRSSET.getString("CMT_CODCD"));
					}	
				}				
			}
			catch(Exception L_EX){
				setMSG(L_EX,"fetching EXPFL()");
			}		
			//////////////////////////////////////////////////////////////////

			M_strSQLQRY = "select a.CMT_REMDT,a.CMT_REMDS,a.CMT_REMBY,a.CMT_DPTCD,a.CMT_REMTP,a.CMT_STSFL from CO_CMTRN a,CO_CDTRN b where b.CMT_CGMTP = 'SYS' and b.CMT_CGSTP = 'COXXIDP' and SUBSTRING(b.CMT_CODCD,4,3) = a.CMT_DPTCD and isnull(a.CMT_STSFL,'')<>'X' "
			+" AND a.CMT_SBSCD = '"+ strSBSCD +"'"
			+" AND a.CMT_CMPTP = '"+ P_strCMPTP +"'"
			+" AND a.CMT_REGNO = '"+ P_strREGNO +"'"
			+" AND a.CMT_PRTNO = '"+ P_strPRTNO +"'"
			+" AND a.CMT_REMTP = 'PARTB'"
			+" Order By b.CMT_CHP01,a.CMT_REMTP desc";
			//System.out.println("M_strSQLQRY>><<>><<>><<"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strDPTCD = "";
				while(M_rstRSSET.next())
				{
					if(!L_vtrDPTCD.isEmpty())
					if(!L_vtrDPTCD.get(0).equals(M_rstRSSET.getString("CMT_DPTCD")))
					{
						while(!L_vtrDPTCD.get(0).equals(M_rstRSSET.getString("CMT_DPTCD")))
						{	
							L_strDPTCD = L_vtrDPTCD.get(0);
							if(hstDPTNM.containsKey(L_strDPTCD))
								L_strDPTCD = hstDPTNM.get(L_strDPTCD).toString();
							prnFMTCHR(dosREPORT,M_strBOLD);	
							dosREPORT.writeBytes("\n"+"Investigation By : "+ L_strDPTCD +"\n\n");
							prnFMTCHR(dosREPORT,M_strNOBOLD);			
							if(L_intTEMP==5)
								dosREPORT.writeBytes("\n"+"NOT APPLICABLE \n\n");
							else
								dosREPORT.writeBytes("\n"+"INVESTIGATION NOT STARTED \n\n");
							dosREPORT.writeBytes("\n"+strDOTLN);
							L_vtrDPTCD.remove(0);
						}	
					}
					if(M_rstRSSET.getString("CMT_STSFL").equals("P") && M_rstRSSET.getString("CMT_REMDS").length()>0)
					{
						L_strDPTCD = M_rstRSSET.getString("CMT_DPTCD");
						if(hstDPTNM.containsKey(L_strDPTCD))
							L_strDPTCD = hstDPTNM.get(L_strDPTCD).toString();
						prnFMTCHR(dosREPORT,M_strBOLD);	
						dosREPORT.writeBytes("\n"+"Investigation By : "+ L_strDPTCD +"\n\n");
						prnFMTCHR(dosREPORT,M_strNOBOLD);							
						dosREPORT.writeBytes("\n"+"INVESTIGATION IN PROGRESS \n\n");
						dosREPORT.writeBytes("\n"+strDOTLN);
					}
					else if(M_rstRSSET.getString("CMT_STSFL").equals("P") && M_rstRSSET.getString("CMT_REMDS").length()==0)
					{
						L_strDPTCD = M_rstRSSET.getString("CMT_DPTCD");
						if(hstDPTNM.containsKey(L_strDPTCD))
							L_strDPTCD = hstDPTNM.get(L_strDPTCD).toString();
						prnFMTCHR(dosREPORT,M_strBOLD);	
						dosREPORT.writeBytes("\n"+"Investigation By : "+ L_strDPTCD +"\n\n");
						prnFMTCHR(dosREPORT,M_strNOBOLD);					
						if(L_intTEMP==5)
							dosREPORT.writeBytes("\n"+"NOT APPLICABLE \n\n");
						else
							dosREPORT.writeBytes("\n"+"INVESTIGATION NOT STARTED \n\n");
						dosREPORT.writeBytes("\n"+strDOTLN);
					}
					else
					{
						L_strREMTP = nvlSTRVL(M_rstRSSET.getString("CMT_REMTP"),"");
						L_strRMRK1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
						L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
						if(hstDPTNM.containsKey(L_strDPTCD))
							L_strDPTCD = hstDPTNM.get(L_strDPTCD).toString();
							
						L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
						if(L_tmpDATE != null)
							L_strDAT1 = M_fmtLCDAT.format(L_tmpDATE);
						L_strRMBY1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
							
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);	
						dosREPORT.writeBytes("\n"+"Investigation By : "+ L_strDPTCD +"\n\n");
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						//dosREPORT.writeBytes(rmkALLGN(L_strRMRK1));
						dosREPORT.writeBytes(L_strRMRK1);
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("<b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strBOLD);
							dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT1 +"                                                                  "+L_strRMBY1);
						if(M_rdbHTML.isSelected())
    						dosREPORT.writeBytes("</b>");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strNOBOLD);
						dosREPORT.writeBytes("\n"+strDOTLN);
					}
					L_vtrDPTCD.remove(0);
				}
				if(!L_vtrDPTCD.isEmpty())
				{
					while(!L_vtrDPTCD.isEmpty())
					{	
						L_strDPTCD = L_vtrDPTCD.get(0);
						if(hstDPTNM.containsKey(L_strDPTCD))
							L_strDPTCD = hstDPTNM.get(L_strDPTCD).toString();
						prnFMTCHR(dosREPORT,M_strBOLD);	
						dosREPORT.writeBytes("\n"+"Investigation By : "+ L_strDPTCD +"\n\n");
						prnFMTCHR(dosREPORT,M_strNOBOLD);						
						if(L_intTEMP==5)
							dosREPORT.writeBytes("\n"+"NOT APPLICABLE \n\n");
						else
							dosREPORT.writeBytes("\n"+"INVESTIGATION NOT STARTED \n\n");
						dosREPORT.writeBytes("\n"+strDOTLN);
						L_vtrDPTCD.remove(0);
					}	
				}
				M_rstRSSET.close();
			}				
		
			//M_strSQLQRY = "select CMT_REMDT,CMT_REMDS,CMT_REMBY,CMT_DPTCD,CMT_REMTP,CMT_STSFL,CM_CPABY from CO_CMTRN,CO_CMMST where CM_CMPCD=CMT_CMPCD and CM_CMPTP=CMT_CMPTP and CM_REGNO=CMT_REGNO and isnull(CMT_STSFL,'')<>'X'"
			//+" AND CMT_CMPCD = '"+ cl_dat.M_strCMPCD_pbst +"'"
			//+" AND CMT_CMPTP = '"+ P_strCMPTP +"'"
			//+" AND CMT_REGNO = '"+ P_strREGNO +"'"
			//+" AND CMT_PRTNO = '"+ P_strPRTNO +"'"
			//+" AND CMT_REMTP = 'AUTRM'"
			//+" Order By CMT_DPTCD,CMT_REMTP desc";
			
			M_strSQLQRY = "select CMT_REMDT,CMT_REMDS,CMT_REMBY,CMT_DPTCD,CMT_REMTP,CMT_STSFL,CM_CPABY"
			+" from CO_CMMST left outer join CO_CMTRN on"
			+" isnull(CMT_STSFL,'')<>'X' AND CMT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CMPTP = '"+P_strCMPTP+"' AND CMT_REGNO = '"+ P_strREGNO +"' AND CMT_PRTNO = '"+ P_strPRTNO +"' AND CMT_REMTP = 'AUTRM'"
			+" where CM_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' AND CM_CMPTP = '"+P_strCMPTP+"' AND CM_REGNO = '"+ P_strREGNO +"'"
			+" Order By CMT_DPTCD,CMT_REMTP desc";
			//System.out.println("M_strSQLQRY>><<>><<>><<"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			boolean flgAUTRM = false;
			if(M_rstRSSET != null)
			{
				String L_strDPTCD = "";
				while(M_rstRSSET.next())
				{
					flgAUTRM = true;
					L_strREMTP = nvlSTRVL(M_rstRSSET.getString("CMT_REMTP"),"");
					L_strRMRK1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
					L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
					if(hstDPTNM.containsKey(L_strDPTCD))
						L_strDPTCD = hstDPTNM.get(L_strDPTCD).toString();
					L_strCPABY = nvlSTRVL(M_rstRSSET.getString("CM_CPABY"),"");		
					L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
					if(L_tmpDATE != null)
						L_strDAT1 = M_fmtLCDAT.format(L_tmpDATE);
					L_strRMBY1 = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
							
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("<b>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);	
					
					dosREPORT.writeBytes("\n"+"CAPA By : ");
					if(!L_strCPABY.equals(""))
					{																											
						// To display the Depts Selected for CAPA
						StringTokenizer L_stkTEMP = new StringTokenizer(L_strCPABY,"_");
						String L_strTEMP = "";
						while(L_stkTEMP.hasMoreTokens())
						{
							L_strTEMP = L_stkTEMP.nextToken();
							dosREPORT.writeBytes(" "+hstDPTNM.get(L_strTEMP).toString()+",");
						}
					}
					dosREPORT.writeBytes("\n\n");
					
					dosREPORT.writeBytes("\n"+"Authorization Remarks : \n\n");
					//dosREPORT.writeBytes("\n"+"Comments By CE(O) : \n\n");
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("</b>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					//dosREPORT.writeBytes(rmkALLGN(L_strRMRK1));
					dosREPORT.writeBytes(L_strRMRK1);
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("<b>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes("\n\n"+"Date : "+ L_strDAT1 +"                                                                  "+L_strRMBY1);
					if(M_rdbHTML.isSelected())
    					dosREPORT.writeBytes("</b>");
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
						prnFMTCHR(dosREPORT,M_strNOBOLD);
					dosREPORT.writeBytes("\n"+strDOTLN);
				}
				if(flgAUTRM == false)
				{
					
				}
			}
			setMSG("Report completed.. ",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
	}
	/**
	 * Method to allign the ramark.
	 * @param P_strREMDS String arguement to pass Remark Description.
	 */
	private String rmkALLGN(String P_strREMDS) // to allign the remark
	{
		int L_intLENCT = 0;
		String L_strTEMP ="";
		String L_strREMDS = "";
		boolean L_flgFIRST = true;
		int L_intROWLN = strDOTLN.length();		
		stkREMDS = new StringTokenizer(P_strREMDS);
		StringBuffer L_stbREMDS= new StringBuffer();
		while(stkREMDS.hasMoreTokens())
		{
			L_strTEMP = stkREMDS.nextToken();
			L_intLENCT += L_strTEMP.length() + 1;
			if(L_intLENCT < L_intROWLN)
			{
				if(!L_flgFIRST)
					L_stbREMDS.append(" ");
				L_flgFIRST = false;				
				L_stbREMDS.append(L_strTEMP);
			}
			else
			{
				L_stbREMDS.append("\n"+L_strTEMP);
				L_intLENCT = L_strTEMP.length();
			}							
		}		
		return L_stbREMDS.toString();
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{		
				ResultSet L_rstRSSET;
				if((input == txtREGNO) && (txtREGNO.getText().trim().length() == 8))
				{				
					M_strSQLQRY =" SELECT CM_REGNO from CO_CMMST where"
					+" isnull(CM_STSFL,'')<>'X'"
					+" AND CM_CMPTP = '"+ strCMPTP +"'"
					+" AND CM_REGNO = '"+ txtREGNO.getText().trim() +"'";									    
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if (L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							strREGNO = txtREGNO.getText().trim();
							strSBSCD = M_strSBSCD;
							return true;
						}	
						else
						{
							L_rstRSSET.close();
							setMSG( "Invalid Registration Number, Press F1 to select from List ..",'E');
							return false;
						}
					}
				}				
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"InputVerifier..");
			}
			return true;
		}
	}
}


/**
	 * Method to generate Header part of the report.
	 */
	/*public void prnHEADER()
	{
		try
		{
			String L_strPRTNM="",L_strREGNO="",L_strBREGN="",L_strCONPR="",L_strADD1="",L_strADD2="";
			String L_strTPRNM="",L_strREGDT="",L_strDORNO="",L_strDORDT="",L_strPRDDS="",L_strINVNO="";
			String L_strINVDT="",L_strLOTNO="",L_strPRDQT="";
			if(cl_dat.M_PAGENO == 0)
			{				
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
					+" AND CMT_CGSTP = 'QCXXPTC' AND isnull(CMT_STSFL,'')<>'X'";				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					String L_strTEMP = "";
					while(M_rstRSSET.next())
					{
						L_strTEMP = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");						
						if(L_strTEMP.equals("DOC1"))
							strISOD1 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("DOC2"))
							strISOD2 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("DOC3"))
							strISOD3 = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					}
					M_rstRSSET.close();
				}
			}
			M_strSQLQRY = "Select CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM,"
			+"CM_CONPR,CM_ORDNO,CM_ORDDT,CM_INVNO,CM_INVDT,CM_INVQT,CM_MMSBS,CM_GRNNO,CM_TPRCD,CM_PRDCD,"
			+"CM_LOTNO,CM_ADLOT,CM_CMPQT,CM_SMPQT from CO_CMMST"
			+" Where isnull(CM_STSFL,'') <>'X' AND CM_REGNO ='"+txtREGNO.getText().trim()+"'"
			+" AND CM_CMPTP = '"+ cmbCMPTP.getSelectedItem().toString().substring(0,2)+"'";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);				
			if( M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					java.sql.Date L_tmpDATE = M_rstRSSET.getDate("CM_REPDT");
					if(L_tmpDATE != null)								
						L_strREGDT = M_fmtLCDAT.format(L_tmpDATE);
					
					L_strPRDQT = nvlSTRVL(M_rstRSSET.getString("CM_CMPQT"),"");
						
					M_strSQLQRY =" SELECT PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04 from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
					if(cmbCMPTP.getSelectedItem().toString().substring(0,2).equals("03"))
						M_strSQLQRY += " AND PT_PRTTP ='S'";
					else
						M_strSQLQRY += " AND PT_PRTTP ='C'";
					M_strSQLQRY += " AND PT_PRTCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_PRTCD"),"").trim().toUpperCase()+"'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{									
							L_strADD1 = nvlSTRVL(L_rstRSSET.getString("PT_ADR01"),"")+", "+nvlSTRVL(L_rstRSSET.getString("PT_ADR02"),"");
							L_strADD2 = nvlSTRVL(L_rstRSSET.getString("PT_ADR02"),"")+", "+nvlSTRVL(L_rstRSSET.getString("PT_ADR04"),"");
						}
						L_rstRSSET.close();
					}
					L_strPRTNM = nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),"");
					L_strCONPR = nvlSTRVL(M_rstRSSET.getString("CM_CONPR"),"");
					L_strDORNO = nvlSTRVL(M_rstRSSET.getString("CM_ORDNO"),"");
					L_tmpDATE = M_rstRSSET.getDate("CM_ORDDT");
					if(L_tmpDATE != null)								
						L_strDORDT = M_fmtLCDAT.format(L_tmpDATE);
					L_strINVNO = nvlSTRVL(M_rstRSSET.getString("CM_INVNO"),"");
				
					L_tmpDATE = M_rstRSSET.getDate("CM_INVDT");
					if(L_tmpDATE != null)								
						L_strINVDT = M_fmtLCDAT.format(L_tmpDATE);
					
					M_strSQLQRY =" SELECT PT_PRTNM from CO_PTMST where isnull(PT_STSFL,'')<>'X'";
					M_strSQLQRY += " AND PT_PRTTP ='T'";
					M_strSQLQRY += " AND PT_PRTCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_TPRCD"),"").trim().toUpperCase()+"'";
					L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
							L_strTPRNM = nvlSTRVL(L_rstRSSET.getString("PT_PRTNM"),"");
						L_rstRSSET.close();
					}						
					
					M_strSQLQRY =" SELECT PR_PRDDS from CO_PRMST where isnull(PR_STSFL,'')<>'X'";						
					M_strSQLQRY += " AND PR_PRDCD = '"+nvlSTRVL(M_rstRSSET.getString("CM_PRDCD"),"").trim().toUpperCase()+"'";
					L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
							L_strPRDDS = nvlSTRVL(L_rstRSSET.getString("PR_PRDDS"),"");
						L_rstRSSET.close();
					}
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("CM_LOTNO"),"");
				}
				M_rstRSSET.close();
			}
				
			cl_dat.M_PAGENO++;			
			dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD1,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD2,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISOD3,strDOTLN.length())+"\n");
			dosREPORT.writeBytes(padSTRING('L',"------------------------------",strDOTLN.length())+"\n");
			dosREPORT.writeBytes("SUPREME PETROCHEM LIMITED.");
			dosREPORT.writeBytes("\n"+padSTRING('R',"Customer Complaint Analysis Form",strDOTLN.length()-21)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Complaint Type : "+cmbCMPTP.getSelectedItem().toString().substring(3),strDOTLN.length()-21));			
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");
			if(M_rdbHTML.isSelected())
    			dosREPORT.writeBytes("<b>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			dosREPORT.writeBytes(padSTRING('L',"PART A",(strDOTLN.length()/2)+3)+"\n");
			if(M_rdbHTML.isSelected())
    			dosREPORT.writeBytes("</b>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			dosREPORT.writeBytes(strDOTLN+"\n");			
									
			dosREPORT.writeBytes("\n"+"Customer Name  : ");
			dosREPORT.writeBytes(padSTRING('R',L_strPRTNM,46));
			dosREPORT.writeBytes(padSTRING('R',"HO Reg No.    : ",16));
			dosREPORT.writeBytes(txtREGNO.getText().trim());
			
			dosREPORT.writeBytes("\n"+"Contact Person : ");
			dosREPORT.writeBytes(padSTRING('R',L_strCONPR,46));
			dosREPORT.writeBytes(padSTRING('R',"Branch Reg No.: ",16));
			dosREPORT.writeBytes(txtBREGN.getText().trim());
			
			dosREPORT.writeBytes("\n"+"Address        : ");
			dosREPORT.writeBytes(L_strADD1);
			dosREPORT.writeBytes("\n"+padSTRING('R',"",17));			
			dosREPORT.writeBytes(L_strADD2);
			
			dosREPORT.writeBytes("\n"+"Transporter    : ");
			dosREPORT.writeBytes(padSTRING('R',L_strTPRNM,46));
			dosREPORT.writeBytes(padSTRING('R',"Date Reported : ",16));
			dosREPORT.writeBytes(L_strREGDT);
									
			dosREPORT.writeBytes("\n"+"D.O.No. / Date : ");
			dosREPORT.writeBytes(padSTRING('R',L_strDORNO +" "+L_strDORDT,46));
			dosREPORT.writeBytes("Lot No        : ");
			dosREPORT.writeBytes(L_strLOTNO);

			dosREPORT.writeBytes("\n"+"Invoice No/Date: ");
			dosREPORT.writeBytes(padSTRING('R',L_strINVNO +" "+L_strINVDT,46));
			dosREPORT.writeBytes("Quantity      : ");
			dosREPORT.writeBytes(L_strPRDQT+" MT");

			dosREPORT.writeBytes("\n"+"Grade          : ");
			dosREPORT.writeBytes(L_strPRDDS);
			
			dosREPORT.writeBytes("\n"+strDOTLN);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER"); 
		}
	}*/