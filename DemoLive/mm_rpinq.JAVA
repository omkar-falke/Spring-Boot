/*
System Name   : Material Management System
Program Name  : Indent Printing
Program Desc. : 
Author        : N.K.Virdi
Date          : 28/02/2004
Version       : MMS 2.0

Modificaitons 
Modified By    : Mr. S.R.Mehesare
Modified Date  : 03/10/2005
Modified det.  : 
Version        :

*/

import java.sql.ResultSet;import java.util.Hashtable;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import java.io.File;import java.io.FileOutputStream;import java.io.FileInputStream;import java.io.DataOutputStream;
import java.io.PrintWriter;import java.io.FileNotFoundException;import java.io.IOException;
/**<pre>
<B>System Name :</B> Material Management System.
 
<B>Program Name :</B> Enquiry Printing 

<B>Purpose :</B> This module prints the Enquiry raised to a particular Vendor. 
Enquiry is raised from a purchase Indent and sent to the vendor for receiving 
quotations.Enquiry number and Vendor code is given to print the report.Date is
accepted from user, by which the quotation should be received.

List of tables used :
Table Name    Primary key                            Operation done
                                          Insert   Update   Query   Delete	
-----------------------------------------------------------------------------
CO_PTMST      PT_PRTTP,PT_PTTCD                                #
MM_IQMST                                                       #
CO_CTMST      CT_GRPCD,CT_CODTP,CT_MATCD                       #   
CO_CTTRN      CTT_GRPCD,CTT_CODTP,CTT_MATCD,                 
              CTT_LVLNO,CTT_LINNO                              #
-----------------------------------------------------------------------------

List of  fields entered/displayed on screen :
Field Name  Column Name        Table name   Type/Size      Description
-----------------------------------------------------------------------------
txtINQNO    IQ_INQNO            MM_IQMST    VARCHAR(10)    Material Code
txtVENCD    PT_PRTCD            CO_PTMST    VARCHAR(5)     Material Code
txtQTNDT                                    Date           Date upto which
                                                           Quatation to be given 
-----------------------------------------------------------------------------
<B><I>
Conditions Give in Query:</B> 
  Details about the Material is taken from MM_IQMST,CO_CTMST and CO_CDTRN for
     1) IQ_MATCD = CT_MATCD 
     2) AND IQ_MMSBS = SubSystem Code.
     3) AND CMT_CGMTP = 'MST' 
     4) AND CMT_CGSTP ='COXXUOM' 
     5) AND CMT_CODCD = IQ_UOMCD 
     6) AND IQ_STRTP = Store Type.
     7) AND IQ_INQNO = given Enquiry Number
     8) AND isnull(CT_STSFL,'') <>'X'
     9) AND isnull(IQ_STSFL,'') <> 'X'
			
<b>Validations :</b>
    - Enquiry Number given to generate the report must be valid.
    - Vendor code must be valid.    
    - Text File is generated with the Vendor code as file name.
*/
public class mm_rpinq extends cl_rbase
{										/** JTextField to display & to enter Enquiry Number.*/
	private JTextField txtINQNO;		/** JTextField to display & to enter Quatation Date.*/
	private JTextField txtQTNDT;		/** JTextField to display & to enter Vender Code.*/
	private JTextField txtVENCD;		/** JTextField to display Vender Name.*/
	private JTextField txtVENNM;
									/** Integer variable to count the number of records fetched.*/		
	private int intRECCNT;			/** Integer variable to generate the Serial number.*/
	private int intSRLNO=0;			/** Integer variable to count number of records fetched to check that data found or not.*/
	private int intRECCT=0;			/** String variable to represent the name of the generated Report File.*/
	private String strFILNM ;		/** String variable for Enquiry Number.*/
	private String strINQNO;		/** String variable for Enquiry Date.*/
	private String strINQDT;		/** String variable for Vendor Name.*/  
	private String strVENNM;		/** String variable for ISO Number Description.*/
	private String strISO1="";		/** String variable for ISO Number Description.*/
	private String strISO2="";		/** String variable for ISO Number Description.*/
	private String strISO3="";		/** String variable for Store Type.*/
	private String strPRINT="";
	private String strTEMP ="";
	private String strSTRTP="";			/** String variable for Material Code.*/
	private String strMATCD;			/** FileOutputStream object to generate the Report file from Data Stream.*/
	private FileOutputStream fosREPORT;	/** DataoutputStream object to hold Report Data.*/
    private DataOutputStream dosREPORT;
	boolean flgFIRST  = false;			/** HashTable to hold Material Code with Description.*/
	private Hashtable hstMATCD = new Hashtable();/** String vaiable  to generate the Dotted line.*/
	private String strDOTLN ="---------------------------------------------------------------------------------------------";	
	mm_rpinq()
	{
		super(2);

		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("Enquiry No. "),4,3,1,1,this,'L');
			add(txtINQNO = new TxtLimit(10),4,4,1,1,this,'L');
			add(new JLabel("Vendor Code "),5,3,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),5,4,1,1,this,'L');
			add(txtVENNM = new TxtLimit(40),5,5,1,4,this,'L');
			add(new JLabel("Quotation By"),6,3,1,1,this,'L');
			add(txtQTNDT = new TxtDate(),6,4,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
						
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP ='ISO'"
				+ " AND CMT_CGSTP ='MMXXRPT' AND isnull(CMT_STSFL,'') <> 'X'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);        
			if(L_rstRSSET != null)
			{
				String L_strTEMP="";
				while(L_rstRSSET.next())
				{
					L_strTEMP = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");						
					if(!L_strTEMP.equals(""))
					{							
						if(L_strTEMP.equals("MM_RPINQ01"))
							strISO1 = nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPINQ02"))
							strISO2 = nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"");
						else if(L_strTEMP.equals("MM_RPINQ03"))
							strISO3 = nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),"");							
					}					
				}
				L_rstRSSET.close();
			}
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			strSTRTP =M_strSBSCD.substring(2,4);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
			{
				setMSG("Please Enter Enquiry No. OR Press F1 to Select from List..",'N');
				setENBL(true);
				txtVENNM.setEnabled(false);
				txtINQNO.requestFocus();
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
			cl_dat.M_intLINNO_pbst = 0;
		}		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == 9 || L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{ 
			if (M_objSOURC == txtINQNO)
			{
				txtVENCD.setText("");
				txtVENNM.setText("");
				txtQTNDT.setText("");
				if(vldINQNO(M_strSBSCD.substring(2,4),txtINQNO.getText().trim()))
				{
					setMSG("Please Enter Vendor Code,F1 to Select from List..",'N');
					txtVENCD.requestFocus();
				}
			}
			else if (M_objSOURC == txtVENCD)
			{
				if(vldVENCD(txtVENCD.getText().trim()))
				{
					setMSG("Please Enter the last date for recieving quotations ..",'N');
					txtQTNDT.requestFocus();
				}
				else
				{
				    setMSG("Invalid Vendor Code..Press F1 to select from list",'E');
				}
			}
			else if (M_objSOURC == txtQTNDT)
				cl_dat.M_btnSAVE_pbst.requestFocus();
		}
		if (L_KE.getKeyCode()== L_KE.VK_F1)					//F1 starts from here
		{
			if(M_objSOURC == txtINQNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtINQNO";
				String L_ARRHDR[] = {"Inqury No","Date"};
				M_strSQLQRY = "Select distinct IQ_INQNO,IQ_INQDT";
				M_strSQLQRY += " from MM_IQMST where IQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IQ_STSFL,'') <>'X'";
				M_strSQLQRY += " AND IQ_STRTP ='"+strSTRTP+"'";
				if(M_objSOURC == txtINQNO)
				{
					if(txtINQNO.getText().trim().length() >0)
					M_strSQLQRY += " AND IQ_INQNO like '"+txtINQNO.getText().trim() +"%'";
				}
				M_strSQLQRY += " order by IQ_INQNO DESC";				
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
			if(M_objSOURC == txtVENCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtVENCD";
				String L_ARRHDR[] = {"Vendor Code","Vendor Name"};
				M_strSQLQRY = "Select distinct IQ_VENCD,PT_PRTNM";
				M_strSQLQRY += " from MM_IQMST,CO_PTMST WHERE IQ_VENCD = PT_PRTCD AND PT_PRTTP ='S' AND isnull(IQ_STSFL,'') <>'X'";
				M_strSQLQRY += " AND IQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IQ_STRTP ='"+strSTRTP+"'";
				M_strSQLQRY += " AND IQ_INQNO ='"+txtINQNO.getText().trim()+"'";
				if(M_objSOURC == txtVENCD)
				{
					if(txtVENCD.getText().trim().length() >0)
					M_strSQLQRY += " AND IQ_VENCD like '"+txtVENCD.getText().trim() +"%'";
				}
				M_strSQLQRY += " order by IQ_VENCD";				
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,2,"CT");
			}
		}
	}
	/**
	 * Method to execuate the F1 Help for selected TextField.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtINQNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtINQNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtVENCD"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtVENCD.setText(cl_dat.M_strHLPSTR_pbst);								
				if(M_rdbHTML.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + txtVENCD.getText().trim()+".html";
				else if(M_rdbTEXT.isSelected())
					strFILNM = cl_dat.M_strREPSTR_pbst + txtVENCD.getText().trim()+".doc";	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}	
	/**
	 * Method to validate the Data befor Execuation of the Query.
	 */
	boolean vldDATA()
	{
		boolean L_flgRPT = false;
		if(txtINQNO.getText().trim().length()!=8)
		{
			setMSG("Enquiry No. should be of 8 chars..",'E');
			txtINQNO.requestFocus();
			return false;
		}
		else if(!vldINQNO(strSTRTP,txtINQNO.getText().trim()))
		{
			setMSG("Invalid Enquiry No...",'E');
			txtINQNO.requestFocus();
			return false;
		}
		if(txtVENCD.getText().trim().length()!=5)
		{
			setMSG("Vendor Code should be of 5 chars..",'E');
			txtVENCD.requestFocus();
			return false;
		}
		else if(!vldVENCD(txtVENCD.getText().trim()))
		{
			setMSG("Invalid Vendor Code...",'E');
			txtVENCD.requestFocus();
			return false;
		}
		if(txtQTNDT.getText().trim().length()==0)
		{
			setMSG("Enter the last date for recieving quotation..",'E');
			txtQTNDT.requestFocus();
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
	/**
	 * Method to generate the Header of the Report.
	 */
	private void prnHEAD()
	{
		try
		{			
			cl_dat.M_intLINNO_pbst =0;
			strVENNM ="";
			String L_strADR01 ="";
			String L_strADR02 ="";
			String L_strADR03 ="";
			String L_strADR04 ="";
			String L_strCTYNM ="";
			String L_strCONNM ="";
			String L_strPINCD ="";
			String L_strFAXNO ="";
			String L_strTEL01 ="";
			String L_strTEL02 ="";
			
			M_strSQLQRY = "Select PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04,PT_CTYNM,PT_CONNM,PT_PINCD,PT_FAXNO,PT_TEL01,PT_TEL02 FROM CO_PTMST WHERE ";
			M_strSQLQRY +=" PT_PRTTP ='S' AND PT_PRTCD ='"+txtVENCD.getText().trim()+"'";
			ResultSet rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(rstRSSET !=null)
			{
				if(rstRSSET.next())
				{
					strVENNM = nvlSTRVL(rstRSSET.getString("PT_PRTNM"),"");
					L_strADR01 = nvlSTRVL(rstRSSET.getString("PT_ADR01"),"");
					L_strADR02 = nvlSTRVL(rstRSSET.getString("PT_ADR02"),"");
					L_strADR03 = nvlSTRVL(rstRSSET.getString("PT_ADR03"),"");
					L_strADR04 = nvlSTRVL(rstRSSET.getString("PT_ADR04"),"");
					L_strCTYNM = nvlSTRVL(rstRSSET.getString("PT_CTYNM"),"");
					L_strCONNM = nvlSTRVL(rstRSSET.getString("PT_CONNM"),"");
					L_strFAXNO = nvlSTRVL(rstRSSET.getString("PT_FAXNO"),"");
					L_strTEL01 = nvlSTRVL(rstRSSET.getString("PT_TEL01"),"");
					L_strTEL02 = nvlSTRVL(rstRSSET.getString("PT_TEL02"),"");
				}
				rstRSSET.close();
			}			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strCPI12);
			
			dosREPORT.writeBytes("\n");
						
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length()-2)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO1,strDOTLN.length()-2)+"\n");
			dosREPORT.writeBytes(padSTRING('L',strISO2,strDOTLN.length()-2)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO3,strDOTLN.length()-2)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length()-2)+"\n");						
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strENH);
				prnFMTCHR(dosREPORT,M_strBOLD);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("<H3>");
				dosREPORT.writeBytes(padSTRING('L',"",8));
			}
			dosREPORT.writeBytes(padSTRING('L'," ",17));			
			dosREPORT.writeBytes("PURCHASE ENQUIRY ");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</H3>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOENH);
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			}
			dosREPORT.writeBytes("\nTo\n");
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',"Attn: "+L_strCONNM,50)+" ");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			
			dosREPORT.writeBytes("SUPREME PETROCHEM LTD");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes(padSTRING('R',txtVENCD.getText().trim()+" "+strVENNM,50)+" ");	
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("</B>");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
/*
M/s. Supreme Petrochem Ltd
Bldg. No. 11, 5th Floor,�Solitaire Corpo. Park, 167,�Guru Hargovindji Marg,
Andheri-Ghatkopar Link Road, Chakala, Andheri (E),
Mumbai - 400 093.
�
Phone No : 022-6709 1946, 6709 1900, 6709 1951
Fax No : 022-6709 1926, 6709 1925
E-mail : purchase@spl.co.in
*/			
			
			//dosREPORT.writeBytes("17/18 Shah Industrial Estate" );
			dosREPORT.writeBytes("Bldg. No. 11, 5th Floor,Solitaire Corpo. Park,167," );
			dosREPORT.writeBytes("\n");

			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',L_strADR01,50)+" ");	
			//dosREPORT.writeBytes("Veera Desai Road, Andheri(W)");
			dosREPORT.writeBytes("Guru Hargovindji Marg,Andheri-Ghatkopar Link Road,");
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',L_strADR02,50)+" ");	
			dosREPORT.writeBytes("Chakala, Andheri (E), Mumbai - 400 093");
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',L_strADR03,50)+" ");				
			//dosREPORT.writeBytes("Tel.: (91)022-26736194 to 99");
			dosREPORT.writeBytes("Phone No : 022-6709 1946, 6709 1900, 6709 1951");
			dosREPORT.writeBytes("\n");

			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',L_strADR04,50)+" ");	
			//dosREPORT.writeBytes("Fax : (91)022-26736203 ");
			dosREPORT.writeBytes("Fax No : 022-6709 1926, 6709 1925");
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',"Fax. No. :-"+L_strFAXNO+" Tel. No.:"+L_strTEL01+","+L_strTEL02,50)+" ");
			//dosREPORT.writeBytes("E-Mail: purchase@spl.co.in ");
			dosREPORT.writeBytes("E-mail : purchase@spl.co.in");
			dosREPORT.writeBytes("\n");
			
			dosREPORT.writeBytes(padSTRING('L',"",55)+" ");	
			dosREPORT.writeBytes("Report Date  : "+cl_dat.M_strLOGDT_pbst);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L',"",55)+" ");	
			dosREPORT.writeBytes("Page Number  : "+cl_dat.M_PAGENO);
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',"Stores Type      : "+cl_dat.getPRMCOD("CMT_CODDS","SYS","MMXXSST",strSTRTP),50)+" ");	
			dosREPORT.writeBytes("Enquiry No.  : "+strSTRTP+"/"+strINQNO );
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R'," ",55)+" ");	
			dosREPORT.writeBytes("Enquiry Date : "+strINQDT );
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("Dear Sir \n");
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes("We are giving you a list of our requirements. Please send us your best offer along with\n");
			dosREPORT.writeBytes(padSTRING('L'," ",5));			
			dosREPORT.writeBytes("other commercial terms AND conditions by "+txtQTNDT.getText().trim()+"\n");
			dosREPORT.writeBytes(strDOTLN);			
			dosREPORT.writeBytes("\nSrl. Material Code (Part No.)                          Indent No.  Unit Of         Required");
			dosREPORT.writeBytes("\nNo.  Description                                                   Measurement     Quantity\n");
			dosREPORT.writeBytes(strDOTLN+"\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			cl_dat.M_intLINNO_pbst += 27;			
		}
		catch(Exception L_exEXCP)
		{
			setMSG(L_exEXCP,"prnHEAD......");
		}
	}
	/**
	 * Method to generate the Table Header of the Report i.e Collumn Names.
	 */
	private void prnHEADER()
	{  
		try
		{			
			cl_dat.M_intLINNO_pbst  = 0;
			cl_dat.M_PAGENO += 1;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);			
			}
			dosREPORT.writeBytes("\n"+padSTRING('L',"-------------------------------",strDOTLN.length()-2)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO1,strDOTLN.length()-2)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO2,strDOTLN.length()-2)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',strISO3,strDOTLN.length()-2)+"\n");			
			dosREPORT.writeBytes(padSTRING('L',"-------------------------------",strDOTLN.length()-2)+"\n\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);			
			}
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");			
			dosREPORT.writeBytes(padSTRING('L'," ",5));
			dosREPORT.writeBytes(padSTRING('R',txtVENCD.getText().trim()+" "+strVENNM,50)+" ");
			dosREPORT.writeBytes("Enquiry No :"+strINQNO+"\n");			
			dosREPORT.writeBytes(padSTRING('L'," ",65));
			dosREPORT.writeBytes("Page No    :"+cl_dat.M_PAGENO+"\n");
			dosREPORT.writeBytes(strDOTLN +"\n\n");			
			cl_dat.M_intLINNO_pbst = 12;			
		}
		catch(Exception L_exEXCP)
		{
			setMSG(L_exEXCP,"prnHEAD......");
		}
	}	
	/**
	 * Method to generate the Header of the Report.
	 */
	private void prnFOOTR(String P_strINQNO)
	{
		try
		{
			String L_strREMDS ="";			
			dosREPORT.writeBytes("\n"+strDOTLN+"\n");			
			cl_dat.M_intLINNO_pbst++;
			dosREPORT.writeBytes ("Remarks : ");
			M_strSQLQRY = "SELECT * FROM MM_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND RM_STRTP ='"+strSTRTP +"'";
			M_strSQLQRY += " AND RM_DOCTP ='IQ'";
			M_strSQLQRY += " AND RM_TRNTP ='IQ'";		
			M_strSQLQRY += " AND RM_DOCNO ='"+P_strINQNO+"'";
			M_strSQLQRY += " AND isnull(RM_STSFL,'') <>'X'";
			ResultSet rstRSSET = cl_dat.exeSQLQRY3(M_strSQLQRY);
			if(rstRSSET !=null)
			{
				while(rstRSSET.next())
				{
					L_strREMDS = nvlSTRVL(rstRSSET.getString("RM_REMDS"),"");
					if(L_strREMDS.length() >strDOTLN.length()-2)
					{
						dosREPORT.writeBytes(L_strREMDS.substring(0,92)+"-");
						dosREPORT.writeBytes ("\n");			
						dosREPORT.writeBytes(padSTRING('L',"",10));
						dosREPORT.writeBytes(L_strREMDS.substring(92));
					}
					else
						dosREPORT.writeBytes(L_strREMDS);
				}		
				rstRSSET.close();
			}			
			dosREPORT.writeBytes("\n"+strDOTLN+"\n\n");			
			dosREPORT.writeBytes(padSTRING('L',"For SUPREME PETROCHEM LTD",strDOTLN.length()-2));			
			dosREPORT.writeBytes ("\n\n\n\n\n");
			dosREPORT.writeBytes(padSTRING('L',"",65));
			
			rstRSSET = cl_dat.exeSQLQRY3("SELECT US_USRNM FROM SA_USMST WHERE US_USRCD ='"+cl_dat.M_strUSRCD_pbst+"'");
			if(rstRSSET !=null)
			if(rstRSSET.next())
				dosREPORT.writeBytes ("("+nvlSTRVL(rstRSSET.getString("US_USRNM"),"")+ ")");
			dosREPORT.writeBytes ("\n");
			dosREPORT.writeBytes(padSTRING('L',"",65));
			dosREPORT.writeBytes ("Authorised Signatory"+"\n\n");
			
			cl_dat.M_intLINNO_pbst += 13;
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 1;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}		
	/**
	 * Method to forward the Report, to selected Destination.
	 */
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
				return;
						
			getDATA(txtINQNO.getText().trim()); 
			
			/*if(intRECCT >0)
			{
				setMSG("No Data Found for given Selection of Data..",'E');
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
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(M_strEMLNM))
			{		       
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Indent Printing"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }													
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error in exePRINT ");
		}
		finally
		{
			try
			{
				if(dosREPORT !=null)
					dosREPORT.close();
				if(fosREPORT !=null)
					fosREPORT.close();
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"exePRINT");
			}
		}
	}
	/**
	 * Method to fetch data from database & to generate Repor Body.
	 * @param P_strINQNO String argument to pass the Enquiry Number.
	 */
	private void getDATA(String P_strINQNO)
	{		
		java.sql.Timestamp L_tmsTEMP;
		java.sql.Date L_datTEMP;
		String L_strLVLNO ="",L_strPMATCD ="",L_strMATCD="",L_strDSCTP;
		String L_strPRVLVL="";
		String L_strSQLQRY ="";		
		try
		{
			intRECCNT = 0;
			intSRLNO =0;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Progress....",'N');
			fosREPORT = new FileOutputStream(strFILNM);	
			dosREPORT = new DataOutputStream(fosREPORT);
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 1;
			//intRECCT = 0;
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Indent Printing</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}
			M_strSQLQRY = " SELECT distinct IQ_INQNO,IQ_INQDT,IQ_INQNO,IQ_MATCD,IQ_UOMCD,IQ_INDNO,IQ_INQQT,CT_MATDS,CT_DSCTP,CT_PRTNO,CT_LVLRF,CMT_CODDS from MM_IQMST,CO_CTMST,CO_CDTRN where ltrim(str(IQ_MATCD,20,0)) = CT_MATCD AND IQ_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND CMT_CGMTP = 'MST' AND CMT_CGSTP ='COXXUOM' AND CMT_CODCD = IQ_UOMCD AND IQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IQ_STRTP ='"+strSTRTP+"'";
			M_strSQLQRY += " AND IQ_INQNO = '"+P_strINQNO.trim()+"'";
			M_strSQLQRY += " AND isnull(CT_STSFL,'') <>'X' AND isnull(IQ_STSFL,'') <> 'X' order by IQ_MATCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{	
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					intRECCNT++;
					intSRLNO++;
					strINQNO = M_rstRSSET.getString("IQ_INQNO");
					L_strMATCD = M_rstRSSET.getString("IQ_MATCD");
					/*if(!strINDNO.equals(strPRVIND))
					{
						intSRLNO =1;
						if(strPRVIND.trim().length() >0)
						{
							prnFOOTR(strPRVIND);
							intRECCNT =1;
						}
					}*/
					L_datTEMP = M_rstRSSET.getDate("IQ_INQDT");
					if(L_datTEMP !=null)
						strINQDT = M_fmtLCDAT.format(L_datTEMP);
					else
							strINQDT = "";
					if(intRECCNT ==1)
					{
						prnHEAD(); 		
					}
					if(cl_dat.M_intLINNO_pbst > 72)
					{
						dosREPORT.writeBytes(strDOTLN);
						prnFMTCHR(dosREPORT,M_strEJT);				
						prnHEADER(); 
					}					
					L_strLVLNO = M_rstRSSET.getString("CT_LVLRF");					
					L_strDSCTP = M_rstRSSET.getString("CT_DSCTP");					
					strMATCD = L_strMATCD;
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst++;
					if(cl_dat.M_intLINNO_pbst > 72)
					{					
						dosREPORT.writeBytes(strDOTLN);
						prnFMTCHR(dosREPORT,M_strEJT);				
						prnHEADER(); 
					}
					strPRINT = "";
					strTEMP = String.valueOf(intSRLNO);
					if(strTEMP.length() ==1)
						strTEMP ="00"+strTEMP+"  ";
					else if(strTEMP.length() ==2)
						strTEMP ="0"+strTEMP+"  ";
					strPRINT += strTEMP;					
					strPRINT += padSTRING('R',L_strMATCD + "    "+nvlSTRVL(M_rstRSSET.getString("CT_PRTNO")," "),50);
					strPRINT += padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IQ_INDNO")," "),12);
					strPRINT += padSTRING('R',nvlSTRVL(M_rstRSSET.getString("IQ_UOMCD")," "),4);
				//	strPRINT += padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CMT_CODDS")," "),12);
					strPRINT +=nvlSTRVL(M_rstRSSET.getString("CMT_CODDS")," ");
					strPRINT += padSTRING('L',M_rstRSSET.getString("IQ_INQQT"),13);
					strPRINT += padSTRING('L'," ",2);
					strPRINT += "\n";					
					cl_dat.M_intLINNO_pbst++;
					if(cl_dat.M_intLINNO_pbst > 72)
					{
						dosREPORT.writeBytes(strDOTLN);
						prnFMTCHR(dosREPORT,M_strEJT);				
						prnHEADER(); 
					}					
					if(L_strDSCTP.equals("S"))
					{
						if(L_strPMATCD.equals(""))
						{
							flgFIRST = true;
							// for First Record
							getMATDS(L_strMATCD,"MG",L_strLVLNO);
						}
						else if(L_strMATCD.substring(0,2).equals(L_strPMATCD.substring(0,2)))
						{
							flgFIRST = false;
							// If main group is same
							if(L_strMATCD.substring(0,4).equals(L_strPMATCD.substring(0,4)))
						    {
								// If sub group is same
								if(L_strMATCD.substring(0,6).equals(L_strPMATCD.substring(0,6)))
								{
									//if sub -sub group is same
									if(!L_strLVLNO.equals(L_strPRVLVL))
									{
										//if level header is differenet
										getMATDS(L_strMATCD,"HD",L_strLVLNO);
									}
									else
									{
										//if description of code
										getMATDS(L_strMATCD,"CD",L_strLVLNO);
									}
								}
								else
								{
									//if sub -sub group is different
									getMATDS(L_strMATCD,"SS",L_strLVLNO);
								}
							}
							else
							{
								// If sub group is different
								getMATDS(L_strMATCD,"SG",L_strLVLNO);
							}
						}
						else
						{
							// If main group is different
							getMATDS(L_strMATCD,"MG",L_strLVLNO);
						}						
					}
					else if(L_strDSCTP.equals("D"))
					{					
						M_strSQLQRY = "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
						M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='CD' ";
						M_strSQLQRY += " AND ctt_matcd = '"+L_strMATCD +"'";					
						M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
						M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
						M_strSQLQRY += " Order by ctt_matcd,ctt_lvlno,ctt_linno ";						
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						dosREPORT.writeBytes(strPRINT);
						if(L_rstRSSET !=null)
						{							
							while(L_rstRSSET.next())
							{
								dosREPORT.writeBytes(padSTRING('L',"",5)); // added on 03/04
								dosREPORT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst++;
								if(cl_dat.M_intLINNO_pbst > 72)
								{
									dosREPORT.writeBytes(strDOTLN);
									prnFMTCHR(dosREPORT,M_strEJT);				
									prnHEADER(); 
								}			
							}
							L_rstRSSET.close();
						}
					}					
					L_strPMATCD = L_strMATCD;
					L_strPRVLVL = L_strLVLNO;
					cl_dat.M_intLINNO_pbst++;
					if(cl_dat.M_intLINNO_pbst > 72)
					{
						dosREPORT.writeBytes(strDOTLN);
						prnFMTCHR(dosREPORT,M_strEJT);				
						prnHEADER(); 
					}
				}
				M_rstRSSET.close();
			}			
			if(intRECCNT > 0)
			{
				if(cl_dat.M_intLINNO_pbst > 65)
				{
					dosREPORT.writeBytes(strDOTLN);
					prnFMTCHR(dosREPORT,M_strEJT);				
					prnHEADER(); 
				}
				prnFOOTR(strINQNO);
			}
			dosREPORT.close();
			fosREPORT.close();			
			setMSG("",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Method to get the Material Description.
	 * @param P_strMATCD String argument to pass Material Code.
	 * @param P_strDSLVL String argument to pass Level Description
	 * @param P_strLVLNO String argument to pass level Number.
	 */
	private void getMATDS(String P_strMATCD,String P_strDSLVL,String P_strLVLNO)
	{
		try
		{
			int L_COUNT =0,L_intCOUNT =0;
			M_strSQLQRY ="";
			boolean pflag = true;
			if(P_strDSLVL.equals("MG"))
			{
				M_strSQLQRY = "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
				M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='MG' ";
				M_strSQLQRY += " AND ctt_matcd = '"+P_strMATCD.substring(0,2) +"0000000A'";
				M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
				M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
				M_strSQLQRY += "UNION ";
			}
			if((P_strDSLVL.equals("MG"))||(P_strDSLVL.equals("SG")))
			{
				M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
				M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SG' ";
				M_strSQLQRY += " AND ctt_matcd = '"+P_strMATCD.substring(0,4) +"00000A'";
				M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
				M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
							
				M_strSQLQRY += "UNION ";
			}
			if((P_strDSLVL.equals("MG"))||(P_strDSLVL.equals("SG"))||(P_strDSLVL.equals("SS")))
			{
				M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
				M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
				M_strSQLQRY += " AND ctt_matcd = '"+P_strMATCD.substring(0,6) +"000A'";
				M_strSQLQRY += " AND CTT_LVLNO ='00'";
				M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
				M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
				M_strSQLQRY += "UNION ";
			}				
			//if((!P_strLVLNO.equals("00"))&&(!P_strLVLNO.equals("0")))
			if(!P_strLVLNO.equals("00"))
		    {
				if((P_strDSLVL.equals("MG"))||(P_strDSLVL.equals("SG"))||(P_strDSLVL.equals("SS"))||(P_strDSLVL.equals("HD")))
				{
					M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
					M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
					M_strSQLQRY += " AND ctt_matcd = '"+P_strMATCD.substring(0,6) +"000A'";
					M_strSQLQRY += " AND CTT_LVLNO ='"+P_strLVLNO+"'";
					M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
					M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
					M_strSQLQRY += "UNION ";
				}
			}
			M_strSQLQRY += "Select CTT_MATCD,CTT_CODTP,CTT_MATDS,CTT_LVLNO,CTT_LINNO from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+P_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='CD' ";
			M_strSQLQRY += " AND ctt_matcd = '"+P_strMATCD +"'";
			M_strSQLQRY += " AND CTT_LVLNO ='"+P_strLVLNO+"'";
			M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
			M_strSQLQRY += " Order by ctt_matcd,ctt_lvlno,ctt_linno ";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			// after this the description of code is getting started
			if(L_rstRSSET !=null)
			{
				while(L_rstRSSET.next())
				{
					if(L_rstRSSET.getString("CTT_CODTP").equals("CD"))
					{
						L_COUNT++;
						if(L_COUNT ==1)
						{
							dosREPORT.writeBytes(strPRINT);
						}
						dosREPORT.writeBytes(padSTRING('L',"",5)); // added on 03/04
						dosREPORT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst += 2;
						if(cl_dat.M_intLINNO_pbst > 72)
						{
							dosREPORT.writeBytes(strDOTLN);
							prnFMTCHR(dosREPORT,M_strEJT);				
							prnHEADER(); 
						}
					}
					else
					{
						if(L_rstRSSET.getString("CTT_CODTP").equals("SS"))
						{
							if(L_rstRSSET.getString("CTT_LVLNO").equals(P_strLVLNO))
							{
								if(pflag)
								{							
									dosREPORT.writeBytes("     ----------*\n");
									cl_dat.M_intLINNO_pbst++;
									if(cl_dat.M_intLINNO_pbst > 72)
									{
										dosREPORT.writeBytes(strDOTLN);
										prnFMTCHR(dosREPORT,M_strEJT);				
										prnHEADER(); 
									}
									pflag = false;										
								}
							}
						}
						dosREPORT.writeBytes(padSTRING('L',"",5)); // added on 03/04
						dosREPORT.writeBytes(L_rstRSSET.getString("CTT_MATDS"));
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst++;
						if(cl_dat.M_intLINNO_pbst > 72)
						{
							dosREPORT.writeBytes(strDOTLN);
							prnFMTCHR(dosREPORT,M_strEJT);				
							prnHEADER(); 
						}
					}
				}
				L_rstRSSET.close();
			}
			dosREPORT.writeBytes("     ----------\n");
			cl_dat.M_intLINNO_pbst++;
			if(cl_dat.M_intLINNO_pbst > 72)
			{
				dosREPORT.writeBytes(strDOTLN);
				prnFMTCHR(dosREPORT,M_strEJT);				
				prnHEADER(); 
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getMATDS");
		}
	}
	/**
	 * Method to check the validity of the Vendor Code
	 * @param P_strVENCD String argument to pass Vendor Code.
	 */
	private boolean vldVENCD(String P_strVENCD)
	{
		try
		{
			M_strSQLQRY = "Select IQ_VENCD,PT_PRTNM from MM_IQMST,CO_PTMST WHERE PT_PRTTP ='S' AND IQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IQ_VENCD = PT_PRTCD  ";
            M_strSQLQRY += " AND isnull(IQ_STSFL,'')  <> 'X'";
			M_strSQLQRY += " AND isnull(PT_STSFL,'')  <> 'X'";
			M_strSQLQRY += " AND IQ_INQNO ='"+txtINQNO.getText().trim() +"'";
			M_strSQLQRY += " AND IQ_VENCD ='"+txtVENCD.getText().trim() +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					txtVENNM.setText(M_rstRSSET.getString("PT_PRTNM"));					
					if(M_rdbHTML.isSelected())
						strFILNM = cl_dat.M_strREPSTR_pbst + txtVENCD.getText().trim()+".html";
					else if(M_rdbTEXT.isSelected())
						strFILNM = cl_dat.M_strREPSTR_pbst + txtVENCD.getText().trim()+".doc";	
					return true;	
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldINQNO");
			return false;
		}
		return false;
	}
	/**
	 * Method to validate the Indent Number.
	 * @param P_strSTRTP String argument to pass Store Type.
	 * @param P_strINQNO String argument to pass Enquiry Number.
	 */
	private boolean vldINQNO(String P_strSTRTP,String P_strINQNO)
	{
		try
		{
			M_strSQLQRY = "Select IQ_INQNO from MM_IQMST ";
            M_strSQLQRY += " where IQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IQ_STSFL,'')  <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())				
					return true;
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldINQNO");
			return false;
		}
		return false;
	}
}
