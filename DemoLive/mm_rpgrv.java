/*
System Name		: Materials Management System
Program Name	: GRIN Values
Author			: Mr. S.R.Mehesare
Modified Date	: 18/10/2005
Documented Date	: 
Version			: MMS v2.0.0
*/

import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.KeyEvent;import java.sql.ResultSet;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
/**<pre>
System Name : Material Management System.
 
Program Name : GRIN Values.

Purpose : Program to generate the Report of various values releated to GRIN.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name             Table name         Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT    GR_ACPDT                MM_GRMST           Date          Acepted Date
txtTODAT    GR_ACPDT                MM_GRMST           Date          Acepted Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
Report Data is taken from MM_GRMST and CO_CTMST for condiations :-    
   1) GR_STRTP = selected Store Type
   2) AND GR_ACPDT BETWEEN secified Date Range
   3) AND GR_GRNTP ='01'
   4) AND isnull(GR_STSFL,'')<>'X'
   5) AND GR_MATCD = CT_MATCD
   if(rdbMODVT.isSelected())
   6) AND isnull(GR_EXCVL,0) > 0 
   7) AND isnull(GR_MODVL,0) = 0
   if(rdbPORVT.isSelected())					
   8) AND isnull(GR_VATVL,0) > 0 
   9) AND isnull(GR_VATBL,0) = 0 
   10) AND isnull(GR_BILQT,0) >0

<B>Validations & Other Information:</B>    
    - To Date must be greater than From Date & smaller then current Date.
</I> */

class mm_rpgrv extends cl_rbase
{									/** JTextField to display to enter From date to specify date range.*/
	private JTextField txtFMDAT;	/** JTextField to display to enter To date to specify date range.*/
	private JTextField txtTODAT;	/** JRedioButton to specify Descripancy In Mod VAT .*/
	private JRadioButton rdbMODVT;	/** JRedioButton to specify Descripancy In P.O. VAT.*/
	private JRadioButton rdbPORVT;	/** JRedioButton to specify default Report.*/
	private JRadioButton rdbALLVT;	/** String variable for Store Type.*/
	private String strSTRTP;		/** String variable for Store Type Description.*/
	private String strSTRNM="";		/** String variable for generated Report File Name.*/
	private String strFILNM;		/** Integer Variable to count the Number of records fetched to block the Report if No data Found.*/
	private int intRECCT;			/** DataOutputStream to generate hld the stream of data.*/
	private DataOutputStream dosREPORT;/** FileOutputStream to generate the Report file form stream of data.*/
	private FileOutputStream fosREPORT;/** String variable to print Dotted Line in the Report.*/	
	private String strDOTLN = "--------------------------------------------------------------------------------------------------------------------------------";	
	public mm_rpgrv()
	{
		super(2);
		setMatrix(20,8);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);					
		add(new JLabel("From Date"),4,4,1,1,this,'L');
		add(txtFMDAT = new TxtDate(),4,5,1,1,this,'L');
		add(new JLabel("To Date"),5,4,1,1,this,'L');
		add(txtTODAT = new TxtDate(),5,5,1,1,this,'L');
		add(rdbALLVT = new JRadioButton("ALL Details",true),6,4,1,3,this,'L');
		add(rdbMODVT = new JRadioButton("Descripancy In Mod VAT"),7,4,1,3,this,'L');
		add(rdbPORVT = new JRadioButton("Descripancy In P.O. VAT"),8,4,1,3,this,'L');
		ButtonGroup bgrTEMP = new  ButtonGroup();
		bgrTEMP.add(rdbALLVT);
		bgrTEMP.add(rdbMODVT);
		bgrTEMP.add(rdbPORVT);
		M_pnlRPFMT.setVisible(true);
		setENBL(false);
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
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
				}
				else
					setENBL(false);
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
		if(M_objSOURC == txtFMDAT)			
			setMSG("Enter From Date..",'N');			
		else if(M_objSOURC == txtTODAT)			
			setMSG("Enter To Date..",'N');		
	}	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{					
			if(M_objSOURC == txtFMDAT)
			{
				if(txtFMDAT.getText().trim().length() == 10)
					txtTODAT.requestFocus();
			}
			else if(M_objSOURC == txtTODAT)
			{
				if(txtTODAT.getText().trim().length() == 10)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
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
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpgrv.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"mm_rpgrv.doc";
			
			getDATA();
			
			if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Receipt Summary"," ");
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
			strSTRNM = cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString();
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("From Date Should Not Be Grater Than Todays Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtTODAT.getText().trim().length() == 0)
			{
				setMSG("Enter To Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("To Date Should Not Be Grater Than Todays Date..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
			{
				setMSG("To Date Should Be Grater Than Or Equal To From Date..",'E');
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
			intRECCT = 0;
			String L_strGRNNO = "";
			String L_strOGRNNO = "";
			double L_dblACPQT=0,L_dblREGQT=0,L_dblEXCVL=0,L_dblMODVL=0;
			double L_dblVATBL=0,L_dblPORVL=0,L_dblBILVL=0,L_dblVATVL=0;
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Receipt Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
				
			prnHEADER();
				
			M_strSQLQRY = "SELECT GR_GRNNO,GR_GRNDT,GR_PORNO,GR_GINNO,GR_BILRF,GR_VENCD,GR_VENNM,GR_MATCD,CT_MATDS,CT_UOMCD,"
				+"GR_ACPQT,GR_EXCVL,GR_VATVL,GR_PORVL,GR_REJQT,GR_MODVL,GR_VATBL,GR_BILVL FROM MM_GRMST,CO_CTMST WHERE"
				+" GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"'"
				+" AND GR_ACPDT BETWEEN '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' "
				+" AND GR_GRNTP ='01'"
				+" AND isnull(GR_STSFL,'')<>'X'";
				if(rdbMODVT.isSelected())
					M_strSQLQRY +=" AND isnull(GR_EXCVL,0) > 0 AND isnull(GR_MODVL,0) = 0";
				if(rdbPORVT.isSelected())					
					M_strSQLQRY +=" AND isnull(GR_VATVL,0) > 0 AND isnull(GR_VATBL,0) = 0 AND isnull(GR_BILQT,0) >0";
				M_strSQLQRY +=" AND GR_MATCD = CT_MATCD order by GR_GRNNO";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strTEMP = "";
				while(M_rstRSSET.next())
				{
					intRECCT = 1; 
					if(cl_dat.M_intLINNO_pbst >65)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);					
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER();
					}					
					L_strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
					if(!L_strGRNNO.equals(L_strOGRNNO))
					{
						dosREPORT.writeBytes("\n"+padSTRING('R',L_strGRNNO,12));
						java.sql.Date L_datTEMP =  M_rstRSSET.getDate("GR_GRNDT");
						if(L_datTEMP != null)						
							L_strTEMP = M_fmtLCDAT.format(L_datTEMP);				
						dosREPORT.writeBytes(padSTRING('R',L_strTEMP,12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),""),12));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_BILRF"),"-"),12));
						dosREPORT.writeBytes(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),"")+"  "+ nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),"")+"\n");
						cl_dat.M_intLINNO_pbst++;
						L_strOGRNNO = L_strGRNNO;
						cl_dat.M_intLINNO_pbst++;
					}
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),12));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),50)+"  ");
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),5));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.00");
					L_dblACPQT += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),14));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_EXCVL"),"0.00");
					L_dblEXCVL += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),14));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_VATVL"),"0.00");
					L_dblVATVL += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),14));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_PORVL"),"0.00");
					L_dblPORVL += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),16)+"\n");
					cl_dat.M_intLINNO_pbst++;
					
					dosREPORT.writeBytes(padSTRING('R',"",69));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0.00");
					L_dblREGQT += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),14));					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_MODVL"),"0.00");
					L_dblMODVL += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),14));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_VATBL"),"0.00");
					L_dblVATBL += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),14));
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("GR_BILVL"),"0.00");
					L_dblBILVL += Double.valueOf(L_strTEMP).doubleValue();
					dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(L_strTEMP),2),16)+"\n");					
					cl_dat.M_intLINNO_pbst++;
				}
			}
			dosREPORT.writeBytes(padSTRING('L',"Total : ",69));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblACPQT).toString()),2),14));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblEXCVL).toString()),2),14));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblVATVL).toString()),2),14));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblPORVL).toString()),2),16)+"\n");
			dosREPORT.writeBytes(padSTRING('L',"Total : ",69));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblREGQT).toString()),2),14));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblMODVL).toString()),2),14));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblVATBL).toString()),2),14));
			dosREPORT.writeBytes(padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(L_dblBILVL).toString()),2),16)+"\n");
			dosREPORT.writeBytes("\n"+strDOTLN);
			
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
	 * Method to generate Header part of the report.
	 */
	public void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n"+ cl_dat.M_strCMPNM_pbst +"\n");
			dosREPORT.writeBytes(padSTRING('R',"List of Reciept From "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO+"\n");
			if(rdbMODVT.isSelected())
				dosREPORT.writeBytes("Descripancy In Mod VAT\n");
			else if(rdbPORVT.isSelected())
				dosREPORT.writeBytes("Descripancy In P.O. VAT\n");
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("GRIN No.    Date        P.O. No.    Gate-In No. Bill Ref.   Vendor Code and Name");
			dosREPORT.writeBytes("\n"+"Mat. Code   Description                                         UOM         Acp.Qty  Excise Value      P.O. VAT     PO based Val");
			dosREPORT.writeBytes("\n"+"                                                                            Rej.Qty   Mod VAT Val      Bill VAT  Actual Bill Val");
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");
			cl_dat.M_intLINNO_pbst = 10;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}