/*
System Name		: Marketing System.
Program Name	: Certificate of Analysis
Author			: Mr. S.R.Mehesare
Modified Date	: 13/01/2006
Documented Date	: 
Version			: MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JTextArea;import javax.swing.BorderFactory;
import java.awt.event.KeyEvent;import java.sql.ResultSet;import java.awt.Font;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;
import java.util.Hashtable;import java.util.Vector;
/**<pre>
System Name : Marketing System
 
Program Name : Certificate of Analysis

Purpose : Program to generate the Report called Certificate of Analysis.

List of tables used :
Table Name   Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CDTRN     CMT_CGMTP,CMT_CGSTP,CMT_CODCD                            #
CO_QSMST     QC_QPRCD                                                 #
CO_PRMST     PR_PRDCD                                                 #
CO_PTMST     PT_PRTTP,PT_PRTNM                                        #
MR_IVTRN     IVT_MKTTP,IVT_LADNO,IVT_PRDCD,IVT_PKGTP                  #
QC_PSMST     PS_QCATP,PS_TSTTP,PS_LOTNO,PS_RCLNO,PS_TSTNO,PS_TSTDT    #  
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtINDNO    IVT_INDNO                MR_IVTRN          VARCHAR(8)    Indent Number
txtPRTCD    IVT_BYRCD                MR_IVTRN          VARCHAR(5)    Party Code
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report details are taken from MR_IVTRN and FG_ISTRN for condiations :-
   1) WHERE IST_MKTTP = IVT_MKTTP
   2) AND IST_ISSNO = IVT_LADNO
   3) AND IVT_SALTP = given Sale Type
   4) AND IVT_INDNO = given Indent Number
   5) AND isnull(IVT_STSFL,'') <>'X'
   6) AND isnull(IST_STSFL,'') <>'X'
   7) AND IVT_BYRCD = given Byer Code.

Quality Parameter Values are taken from PR_LTMST and QC_PSMST for condiations
    1) PR_LTMST LEFT OUTER JOIN QC_PSMST ON LT_LOTNO = PS_LOTNO
    2) AND LT_RCLNO = PS_RCLNO
    3) AND PS_QCATP = '01'
    4) AND PS_TSTTP = '0103'
    5) AND PS_STSFL <> 'X'
    6) WHERE LT_PRDTP = given Product Type
    7) AND LT_LOTNO = given Lot Number
    8) AND LT_RCLNO = given LOt Number

<B>Validations & Other Information:</B>    
     - Indent Number Entered must be Valid.
     - Party Code Entered must be Valid.
</I> */

class mr_rpcoa extends cl_rbase
{										/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtINDNO;		/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtPRTCD;		/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtPRTDS;		/** String variable for generated Report File Name.*/
	private String strFILNM;			/** String variable to print Dotted Line in the Report.*/	
	private String strSALTP = "12";			/** String variable for Sale Type.*/
	private String strQPRLT = "";		/** String variable for List of Quality Parameter List*/
	private String strTSMCD = "";		/** DataOutputStream to generate hld the stream of data.*/
	private DataOutputStream dosREPORT;	/** FileOutputStream to generate the Report file form stream of data.*/
	private FileOutputStream fosREPORT;	/** Hash Table to hold the Product Code with Product Description.*/
	private Hashtable<String,String> hstPRDDS;			/** Vector to hold the Quality Parameter to dymanamically generate the Collumn List.*/
	private Vector<String> vtrQPRCD;			/** StringBuffer to hold the header part of the Report dislaying Quality Parameter List*/
	private StringBuffer stbQPRDS = new StringBuffer(); /** StringBuffer to hold the Unit of Measurement to display in the Header.*/
	private StringBuffer stbUOMCD = new StringBuffer();	/** String variable to append dotted Line in the Report.*/
	private String strDOTLN = "----------------------------------------------------------------------------------------------------";
	private JTextField txtMNHDR;
	private JTextArea txaHEADR;
	private JTextArea txaFOOTR;
	private String strMNHDR;
	public mr_rpcoa()
	{
		super(2);
		try
		{		
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Indent No."),2,1,1,1,this,'L');
			add(txtINDNO = new TxtLimit(8),2,2,1,1,this,'L');
			add(new JLabel("Party"),3,1,1,1,this,'L');
			add(txtPRTCD = new TxtLimit(5),3,2,1,1,this,'L');
			add(txtPRTDS = new JTextField(),3,3,1,3.5,this,'L');		
			add(new JLabel("Report Heading"),4,1,1,1,this,'L');
			add(txtMNHDR = new TxtLimit(30),4,2,1,4,this,'L');
			add(new JLabel("Header"),5,1,1,1,this,'L');
			txtMNHDR.setText("CERTIFICATE OF ANALYSIS");
			add(txaHEADR = new JTextArea(4,1),6,1,3,8,this,'L');
		    txaHEADR.setFont(new Font("Courier New",Font.PLAIN,15));
			txaHEADR.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			add(new JLabel("Footer"),9,1,1,1,this,'L');
			add(txaFOOTR = new JTextArea(4,1),10,1,3,8,this,'L');
			txaFOOTR.setFont(new Font("Courier New",Font.PLAIN,15));
			txaFOOTR.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
			//txaHEADR.setLineWrap(true);
			//txaFOOTR.setLineWrap(true);
			vtrQPRCD = new Vector<String>();
			M_strSQLQRY = "Select CMT_CODCD,CMT_NCSVL,QS_QPRDS1 QS_QPRDS,QS_UOMCD1 QS_UOMCD,QS_TSMCD1 QS_TSMCD from CO_CDTRN,CO_QSMST where CMT_CGMTP='RPT'"
				+" AND CMT_CGSTP = 'QCXXEXC' AND CMT_CODCD = QS_QPRCD order by CMT_NCSVL";
			//System.out.print(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
            int i=0;
			if(M_rstRSSET != null)
			{				
				String L_strQPRCD = "",L_strUOMCD="";
				while(M_rstRSSET.next())
				{
					L_strQPRCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					strQPRLT += ",PS_"+L_strQPRCD +"VL";
										
					vtrQPRCD.addElement(L_strQPRCD);
					L_strUOMCD = nvlSTRVL(M_rstRSSET.getString("QS_UOMCD"),"");
					if(i==0)
					    stbUOMCD.append(padSTRING('L',L_strUOMCD,6));
					else
					    stbUOMCD.append(padSTRING('L',L_strUOMCD,12));
					if(i==0)
					    strTSMCD += padSTRING('L',nvlSTRVL(M_rstRSSET.getString("QS_TSMCD"),""),6);
					else
					    strTSMCD += padSTRING('L',nvlSTRVL(M_rstRSSET.getString("QS_TSMCD"),""),12);
					if((L_strQPRCD.charAt(2))==('_'))
						L_strQPRCD = L_strQPRCD.substring(0,2);
					if(i==0)
                        stbQPRDS.append(padSTRING('L',L_strQPRCD,6));
                    else
                        stbQPRDS.append(padSTRING('L',L_strQPRCD,12));
                    i++;    
				}
				M_rstRSSET.close();
			}
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
    void clrCOMP()
    {
        super.clrCOMP();
       	txtMNHDR.setText("CERTIFICATE OF ANALYSIS");
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
					setENBL(true);
					txtPRTDS.setEnabled(false);
					txtPRTCD.setEnabled(false);
					txtINDNO.requestFocus();
					setMSG("Please Enter Indent No. or Press F1 to select from List..",'N');
					//strSALTP = M_strSBSCD.substring(2,4);
                    txtMNHDR.setText("CERTIFICATE OF ANALYSIS");
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == txtINDNO)
			{
				if(txtINDNO.getText().trim().length() == 8)
				{
					txtINDNO.setText(txtINDNO.getText().trim().toUpperCase());
					M_strSQLQRY = " SELECT distinct IVT_BYRCD,PT_PRTNM FROM MR_IVTRN,CO_PTMST"
					+" WHERE IVT_SBSCD1 in "+M_strSBSLS
					//+" WHERE IVT_SALTP = "+"'"+ M_strSBSCD.substring(2,4) +"'"
					//+" AND IVT_MKTTP = "+"'"+ M_strSBSCD.substring(0,2) +"'"
					+" AND IVT_BYRCD = PT_PRTCD and PT_PRTTP ='C'"
					+" AND isnull(IVT_STSFL,'') <>'X'"
					+" AND IVT_INDNO ='"+ txtINDNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							txtPRTCD.setText(nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),""));
							txtPRTDS.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
						}
						else 						
							setMSG("Invalid Indent Number, Press F1 to select From List..",'E');
					}
					//cl_dat.M_btnSAVE_pbst.requestFocus();
				}			
				else 
					setMSG("Invalid Indent Number, Press F1 to select From List..",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
	 	if(L_KE.getKeyCode() == L_KE.VK_F1)
 	    {
			if(M_objSOURC == txtPRTCD)
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				txtPRTDS.setText("");
				M_strHLPFLD = "txtPRTCD"; 				
    			cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = " SELECT distinct IVT_BYRCD,PT_PRTNM,IVT_SALTP,CONVERT(varchar,IVT_LODDT,103) IVT_LODDT,IVT_INDNO FROM MR_IVTRN,CO_PTMST"
					+" WHERE IVT_SBSCD1 in  "+M_strSBSLS
					//+" WHERE IVT_SALTP = "+"'"+ M_strSBSCD.substring(2,4) +"'"
					//+" AND IVT_MKTTP = "+"'"+ M_strSBSCD.substring(0,2) +"'"
					+" AND IVT_BYRCD = PT_PRTCD and PT_PRTTP ='C'"
					+" AND isnull(IVT_STSFL,'') <>'X'";
			//	if(txtPRTCD.getText().trim().length()>0)
			//		M_strSQLQRY = "IVT_BYRCD ='"+ txtPRTCD.getText().trim().toUpperCase()+"'";					
                M_strSQLQRY +=  "ORDER BY PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code","Party Name","SALE TYPE","Date","Indent No"},5,"CT");
    		    setCursor(cl_dat.M_curDFSTS_pbst);
 			}
			else if(M_objSOURC == txtINDNO)
			{
 				setCursor(cl_dat.M_curWTSTS_pbst);				
				M_strHLPFLD = "txtINDNO";
				cl_dat.M_flgHELPFL_pbst = true;
				M_strSQLQRY = "SELECT distinct IVT_INDNO,IVT_BYRCD,PT_PRTNM,IVT_SALTP,CONVERT(varchar,IVT_LODDT,103) IVT_LODDT FROM MR_IVTRN,CO_PTMST"
					+" WHERE IVT_SBSCD1 in  "+ M_strSBSLS
					//+" WHERE IVT_SALTP = "+"'"+ M_strSBSCD.substring(2,4) +"'"
					//+" AND IVT_MKTTP = "+"'"+ M_strSBSCD.substring(0,2) +"'";
					+" AND IVT_BYRCD = PT_PRTCD and PT_PRTTP ='C'"
					+" AND isnull(IVT_STSFL,'') <>'X'";
				if(txtPRTCD.getText().trim().length() == 5)
					M_strSQLQRY += " AND IVT_BYRCD = '"+ txtPRTCD.getText().trim().toUpperCase() +"'";
				M_strSQLQRY +=  "ORDER BY PT_PRTNM,IVT_INDNO";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,3,1,new String[]{"Indent Number","Party Code","Party Name","Sale Type","Loading Date"},5,"CT",new int[]{80,70,240,20,90});
    		    setCursor(cl_dat.M_curDFSTS_pbst);
 			}			
		}
		else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtINDNO)
			{
			   txtMNHDR.requestFocus();
			   setMSG("Enter Main Heading for Report ",'N');
			}
			else if(M_objSOURC == txtMNHDR)
			{
			    txaHEADR.requestFocus();
			    setMSG("Enter Header Part for Report ",'N');
			}
			/*else if(M_objSOURC == txaHEADR)
			{
			    txaFOOTR.requestFocus();
			    setMSG("Enter Footer Part for Report ",'N');
			}
			else if(M_objSOURC == txaFOOTR)
			{
			    cl_dat.M_btnSAVE_pbst.requestFocus();				
			}*/
		}	
	}
	/**
	 * Super class method to execute the F1 help for SEF Number.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();		
		if(M_strHLPFLD.equals("txtPRTCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			txtINDNO.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),4)));
		}
		else if(M_strHLPFLD.equals("txtINDNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtINDNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtPRTCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			txtPRTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));
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
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"mr_rpcoa.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mr_rpcoa.doc";
			
			getDATA();
				
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,strMNHDR," ");
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
			cl_dat.M_intLINNO_pbst = 0;
			if(txtINDNO.getText().trim().length() == 0)
			{
				setMSG("Please Enter Indent Number to generate the Report..",'E');
				txtINDNO.requestFocus();
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
			if(txaHEADR.getText().indexOf("'") >0)
			{
			    setMSG("Special Characters like ' are not allowed",'E');
			    return false;
			}
			if(txaFOOTR.getText().indexOf("'") >0)
			{
			    setMSG("Special Characters like ' are not allowed",'E');
			    return false;
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
	public void getDATA()
	{
		if(!vldDATA())
			return;		
		try
		{
			String L_strNEWLN ="Y";
			String L_strPRPRT="",L_strPRCNT="",L_strPRPRD="",L_strPRDCD ="",L_strPRDTP;
			String L_strPRTNM="",L_strPRTCD ="",L_strQPRVL="",L_strCNTNO,L_strLORNO,L_strPRDDS="",L_strLOTNO,L_strRCLNO;				
			String L_strOPRTCD="",L_strOCNTNO="",L_strOLORNO="",L_strOPRDDS="";
			StringBuffer L_stbRECORD = new StringBuffer();
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Certificate</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}

			prnHEADER();
			// Product Code 
			hstPRDDS = new Hashtable<String,String>();
			M_strSQLQRY = " SELECT distinct IVT_PRDCD,PR_PRDDS FROM MR_IVTRN,CO_PRMST WHERE"
				+" IVT_SALTP = "+"'"+strSALTP+"'"
				+" AND isnull(IVT_STSFL,'') <>'X'"
				+" AND IVT_PRDCD = PR_PRDCD"
				+" AND IVT_INDNO = '"+ txtINDNO.getText().trim().toUpperCase() +"'";
			//System.out.println(M_strSQLQRY);
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
			
			M_strSQLQRY = " SELECT distinct IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_PRDCD,IVT_CNTDS,IVT_LRYNO,IVT_BYRCD,IVT_SALTP FROM MR_IVTRN,FG_ISTRN "
            +" WHERE IST_MKTTP = IVT_MKTTP"
			+" AND IST_ISSNO = IVT_LADNO "
			+" AND IVT_SALTP = '"+ strSALTP +"'"
			+" AND IVT_INDNO = '"+ txtINDNO.getText().trim().toUpperCase() +"'"
			+" AND isnull(IVT_STSFL,'') <>'X'"
			+" AND isnull(IST_STSFL,'') <>'X'";
			if(txtPRTCD.getText().trim().length() >0)
				M_strSQLQRY += " AND IVT_BYRCD ='"+ txtPRTCD.getText().trim()+"'" ;
			M_strSQLQRY += " ORDER BY IVT_BYRCD,IVT_CNTDS,IST_PRDCD,IST_PRDTP,IST_LOTNO,IST_RCLNO";            
			//System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_stbRECORD.delete(0,L_stbRECORD.length());
					L_strPRDTP = nvlSTRVL(M_rstRSSET.getString("IST_PRDTP"),"");
					L_strLOTNO = nvlSTRVL(M_rstRSSET.getString("IST_LOTNO"),"-");;
					L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("IST_RCLNO"),"");
					L_strCNTNO = nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),"");
					L_strLORNO = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"");
					L_strPRTCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("IST_PRDCD"),"");
					
					//Container Number.
					if(!L_strCNTNO.equals(L_strOCNTNO))
					{
						dosREPORT.writeBytes("\n\n"+L_strCNTNO);
						cl_dat.M_intLINNO_pbst +=2;
						L_strOCNTNO = L_strCNTNO;						
					}
					//Product Grade...
					if(hstPRDDS.containsKey(L_strPRDCD))
						L_strPRDDS = hstPRDDS.get(L_strPRDCD).toString();
					else /// not Requried but....
					{
						M_strSQLQRY = " Select PR_PRDDS from CO_PRMST where PR_PRDCD ='" + L_strPRDCD+"'";
						ResultSet M_rstRSSET1 = cl_dat.exeSQLQRY3(M_strSQLQRY);					
						if(M_rstRSSET1 !=null)
						{
							if(M_rstRSSET1.next())						
								L_strPRDDS = nvlSTRVL(M_rstRSSET1.getString("PR_PRDDS"),"");
							M_rstRSSET1.close();
						}
					}										
					if((!L_strPRDDS.equals(L_strOPRDDS)) || (L_strNEWLN.equals("Y")))
					{
						L_stbRECORD.append("\n   "+padSTRING('R',L_strPRDDS.trim(),10));
						L_strOPRDDS = L_strPRDDS;						
						cl_dat.M_intLINNO_pbst++;
					}
					else
					{
						L_stbRECORD.append("\n"+padSTRING('R',"",10));
						cl_dat.M_intLINNO_pbst++;
					}
										
					L_stbRECORD.append(padSTRING('R',L_strLOTNO.trim(),9));										
										
					M_strSQLQRY = " SELECT LT_CLSFL"+strQPRLT
					+" FROM PR_LTMST LEFT OUTER JOIN QC_PSMST ON LT_LOTNO = PS_LOTNO"
					+" AND LT_RCLNO = PS_RCLNO "
					+" AND PS_QCATP = '01'"
					+" AND PS_TSTTP = '0103'"
					+" AND PS_STSFL <> 'X'"
					+" WHERE LT_PRDTP ='"+ L_strPRDTP.trim() + "'" 
					+" AND LT_LOTNO ='"+ L_strLOTNO.trim() +"'"
					+" AND LT_RCLNO ='"+ L_strRCLNO.trim() +"'";
			        //System.out.println(M_strSQLQRY);
					ResultSet M_rstRSSET2 = cl_dat.exeSQLQRY2(M_strSQLQRY);						
					if(M_rstRSSET2 !=null)
					{
						String L_strCLSFL="";
						String L_strQPFLD="";
						while(M_rstRSSET2.next())
						{
							L_strCLSFL = nvlSTRVL(M_rstRSSET2.getString("LT_CLSFL"),"");
							if(L_strCLSFL.equals("9"))
							{
								for(int i=0;i<vtrQPRCD.size();i++)
								{									
									L_strQPFLD = "PS_" + (String)(vtrQPRCD.elementAt(i)).toString().trim()+ "VL";
									L_strQPRVL = M_rstRSSET2.getString(L_strQPFLD.trim());
									if(L_strQPRVL !=null && !L_strQPRVL.trim().equals(""))
									{
										if(Double.valueOf(L_strQPRVL).doubleValue()!=0.0)
										{
											if(i==0)
											    L_stbRECORD.append(padSTRING('L',L_strQPRVL,6));
											else
											    L_stbRECORD.append(padSTRING('L',L_strQPRVL,12));
										}
									}
									else	
									{										
										if(i==0)
								    		L_stbRECORD.append(padSTRING('L',"-",6));
								    	else
								    		L_stbRECORD.append(padSTRING('L',"-",12));
									}
									
								}
							}
							else
								L_stbRECORD.append(padSTRING('L'," Not Classified ",25));	
						}
						M_rstRSSET2.close();
					}
					dosREPORT.writeBytes(L_stbRECORD.toString());
					
					if(cl_dat.M_intLINNO_pbst>52)
					{						
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();											
					}		
				}
				M_rstRSSET.close();
			}			
			if(hstPRDDS != null)
				hstPRDDS = null;					
			dosREPORT.writeBytes("\n"+strDOTLN);
			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
			}
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("</PRE><PRE style =\" font-size : 10 pt \">");
			dosREPORT.writeBytes(txaFOOTR.getText()+"\n");
			dosREPORT.writeBytes("\n\n\n"+"For SUPREME PETROCHEM LTD.");
			dosREPORT.writeBytes("\n\n\n\n\n\n"+"AUTHORIZED SIGNATORY"+"\n\n");
			dosREPORT.writeBytes("Place : "+"\n");
			dosREPORT.writeBytes("Date  : "+"\n");
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
		//	if(M_rdbHTML.isSelected())			
		//	    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
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
	 * Method to generate Header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{
		    if(txtMNHDR.getText().length() ==0)
		        strMNHDR = "CERTIFICATE OF ANAYSIS";
		    else  strMNHDR = txtMNHDR.getText().trim();   
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n\n\n");
			if(M_rdbHTML.isSelected())
			{			
			//	dosREPORT.writeBytes("</STYLE>");
			//	dosREPORT.writeBytes("<PRE style =\" font-size : 15 pt \">");
			//	dosREPORT.writeBytes("\n\n\n<B>"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",50)+"\n\n");
			//	dosREPORT.writeBytes("</STYLE>");
				dosREPORT.writeBytes("<PRE style =\" font-size : 13 pt \">");
			    dosREPORT.writeBytes(padSTRING('L',strMNHDR,52)+"\n\n");
				dosREPORT.writeBytes("</B></STYLE>");
				dosREPORT.writeBytes("<PRE style =\" font-size : 9 pt \">");
			}
			else 
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
			//		prnFMTCHR(dosREPORT,M_strENH);					
			//		dosREPORT.writeBytes("\n\n\n"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",40)+"\n\n");
			//		prnFMTCHR(dosREPORT,M_strNOENH);
					prnFMTCHR(dosREPORT,M_strBOLD);
					dosREPORT.writeBytes(padSTRING('L',strMNHDR,62)+"\n");
					prnFMTCHR(dosREPORT,M_strNOBOLD);
				}
				else
				{
					//dosREPORT.writeBytes("\n\n\n"+ padSTRING('L',"TO WHOMSOEVER IT MAY CONCERN",61)+"\n\n");
					dosREPORT.writeBytes(padSTRING('L',strMNHDR,58)+"\n");
				}
			}
			if(cl_dat.M_PAGENO ==1)
			{
			    dosREPORT.writeBytes("\n");
			    dosREPORT.writeBytes(txaHEADR.getText().trim());
			    
			}
			dosREPORT.writeBytes("\n\n"+padSTRING('R',"Order Reference : " +txtINDNO.getText().trim(),strDOTLN.length()-21));
		//	dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst);
			dosREPORT.writeBytes("\n"+padSTRING('R',"Customer        : "+ txtPRTDS.getText().trim(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO);
			
			dosREPORT.writeBytes("\n"+strDOTLN);
			dosREPORT.writeBytes("\nContainer No.  ");
			dosREPORT.writeBytes("\n   Grade      Lot No. ");//     MFI     DSP    IZOD      TS     ELG   VICAT     RSM");
			dosREPORT.writeBytes(stbQPRDS.toString());
			dosREPORT.writeBytes("\n                      "+stbUOMCD.toString());
			dosREPORT.writeBytes("\nTest Method           "+strTSMCD);
			dosREPORT.writeBytes("\n"+strDOTLN);
			
			cl_dat.M_intLINNO_pbst = 10;
			if((txaHEADR.getText().length() >0)&&(cl_dat.M_PAGENO ==1))
			{
			    //System.out.println(txaHEADR.getText().length()/90);
			    cl_dat.M_intLINNO_pbst+= txaHEADR.getText().length()/90;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}