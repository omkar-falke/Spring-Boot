/*
System Name		: Materials Management System
Program Name	: Stock Adjustment Note Report 
Author			: Mr S.R.Mehesare

Modified Date	: 08/10/2005
Version			: v2.0.0
*/

import java.sql.ResultSet;import java.util.Date;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.KeyEvent;import java.awt.event.ActionEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : GRIN Descripancy Report

Purpose : This program generates GRIN Descripancy Report for given Range of GRIN Number.
All those GRIN within the range are taken where challan qty. does not match the Received qty.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_MATCD                                                #
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO))          #
CO_PTMST       CP_CMPCD                                                #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMGRN    GR_GRNNO       MM_GRMST      VARCHAR(2)    GRIN Number
txtTOGRN    GR_GRNNO       MM_GRMST      VARCHAR(2)    GRIN Number
--------------------------------------------------------------------------------------
<B><I>
Tables Used For Query :</b> 1)MM_GRMST
                        2)CO_CTMST
                        3)CO_PTMST
<B>Conditions Give in Query:</b>
    Data is taken from MM_GRMST,CO_CTMST and CO_PTMST for condiations :- 
       1) CT_MATCD = GR_MATCD 
       2) AND PT_PRTCD = GR_VENCD 
       3) AND PT_PRTTP = 'S' 
       4) AND GR_GRNNO between given GRIN Number.
       5) GR_STRTP = selected Store Type 
       6) AND ifnull(GR_STSFL,'') <> 'X' 
       7) AND GR_CHLQT <> GR_RECQT 
       8) AND ifnull(PT_STSFL,'') <> 'X' 

<B>Validations & Other Information:</B>
    - GRIN number must be valid
    - From GRIN Number must be smaller than T0 GRIN number.
    - and  To GRIN Number must be greater than from GRIN number.
</I> */
class mm_rpgds extends cl_rbase
{									/** JTextField to display & to enter GRIN Number.*/
	private JTextField txtFMGRN;	/** JTextField to display & to enter GRIN Number.*/
	private JTextField txtTOGRN;	/** String variable to GRIN Number.*/	
	private String strGRNNO="";		/** String variable for Purches order Number.*/
	private String strPORNO;		/** String variable for generated Report File Name.*/
	private String strFILNM;		/** Integer variable to count Number of records fetched to block Report if no data Found.*/
	private int intRECCT;			/** FileOutPutStream Object to generate the report file from the steam of data.*/
	private FileOutputStream fosREPORT;/** DataOutputstream Object to hold data in the stream & to generate the Report.*/
	private DataOutputStream dosREPORT;	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	String M_strADDRS="",M_strCONNO="";
	public mm_rpgds()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);												
						
			add(new JLabel("From GRIN No."),3,4,1,1,this,'L');
			add(txtFMGRN = new TxtNumLimit(8.0),3,5,1,1,this,'L');
			add(new JLabel("To GRIN No."),4,4,1,1,this,'L');
			add(txtTOGRN = new TxtNumLimit(8.0),4,5,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);		
			
			/**M_strADDRS String Varible to store Company address**/
			M_strSQLQRY =" Select CP_ADR01,CP_ADR02,CP_ADR03,CP_ADR04,CP_PINCD,CP_CTYNM,CP_TEL01,CP_TEL02,CP_FAXNO from CO_CPMST ";
			M_strSQLQRY+=" where CP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' ";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			//System.out.println(">>>>M_strSQLQRY>>>>"+M_strSQLQRY);	
			if(M_rstRSSET.next() && M_rstRSSET !=null)
			{
				 M_strADDRS=M_rstRSSET.getString("CP_ADR01")+" "+M_rstRSSET.getString("CP_ADR02")+" "+M_rstRSSET.getString("CP_ADR03")+" "+M_rstRSSET.getString("CP_ADR04")+" "+M_rstRSSET.getString("CP_PINCD");
				 M_strCONNO="Telephones : "+M_rstRSSET.getString("CP_TEL01")+" Fax : "+M_rstRSSET.getString("CP_FAXNO");
			}
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In Constructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() > 0)
				{
					txtFMGRN.requestFocus();
					setMSG("Please Enter GRIN number to generate Report..",'N');
					setENBL(true);
				}
				else
					setENBL(false);
			}
		}
		if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
		{
			strGRNNO = "";	
			txtFMGRN.requestFocus();
		}		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC == txtFMGRN)
			{
				M_strHLPFLD = "txtFMGRN";
				M_strSQLQRY = "SELECT DISTINCT GR_GRNNO,GR_GRNDT FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,' ') <> 'X'";
				if(txtFMGRN.getText().length() > 0)
					M_strSQLQRY += " AND GR_GRNNO LIKE '"+txtFMGRN.getText()+"%'";				
				if(txtTOGRN.getText().length() > 0)
					M_strSQLQRY += " AND GR_GRNNO <= '"+txtTOGRN.getText()+"'";				
				M_strSQLQRY += " AND GR_CHLQT <> GR_RECQT";
				M_strSQLQRY += " ORDER BY GR_GRNNO";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRN Number","GRN Date"},2,"CT");
			}
			if(M_objSOURC == txtTOGRN)
			{
				M_strHLPFLD = "txtTOGRN";
				M_strSQLQRY = " SELECT DISTINCT  GR_GRNNO,GR_GRNDT FROM MM_GRMST WHERE GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,' ') <> 'X'";
				if(txtTOGRN.getText().length() > 0)
					M_strSQLQRY += " AND GR_GRNNO LIKE '"+txtTOGRN.getText()+"%'";				
				if(txtFMGRN.getText().length() > 0)
					M_strSQLQRY += " AND GR_GRNNO >='"+txtFMGRN.getText()+"'";				
				M_strSQLQRY += " AND GR_CHLQT <> GR_RECQT";
				M_strSQLQRY += " ORDER BY GR_GRNNO ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"GRN Number","GRN Date"},2,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMGRN )
			{
				if(txtFMGRN.getText().length()==8)
				{
					txtTOGRN.requestFocus();
					setMSG("Please Enter GRIN number to generate the Report..",'N');
				}
				else
					setMSG("GRIN number cannot be blank, Please enter It..",'N');
			}
			if(M_objSOURC == txtTOGRN )
			{
				if(txtTOGRN.getText().length()==8)
					cl_dat.M_btnSAVE_pbst.requestFocus();
				else
					setMSG("Please Enter Grin Number to generate the Report..",'N');
			}			
		}
	}
	/**
	 * Super Class Method overrided to execuate the F1 help for selected Field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMGRN")
			txtFMGRN.setText(cl_dat.M_strHLPSTR_pbst);
		if(M_strHLPFLD == "txtTOGRN")
			txtTOGRN.setText(cl_dat.M_strHLPSTR_pbst);		
	}
	/**
	 * Method to validate the  inputs before execuation of the SQL Quary.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(txtFMGRN.getText().trim().length() == 0)
			{
				setMSG("Enter From GRN Number..",'E');
				txtFMGRN.requestFocus();
				return false;
			}
			if(txtTOGRN.getText().trim().length() == 0)
			{
				setMSG("Enter To GRN Number..",'E');
				txtTOGRN.requestFocus();
				return false;
			}
			else if( Integer.parseInt(txtTOGRN.getText().trim().toString()) < Integer.parseInt(txtFMGRN.getText().trim().toString()) )
			{
				setMSG("To GRN Number Should Be Grater Than Or Equal To From GRN Number..",'E');
				txtTOGRN.requestFocus();
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
			setMSG(L_EX,"Error In vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to generate the Report File & to send at selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpgds.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgds.doc";
			intRECCT = 0;
			
			getDATA();
			
			if(dosREPORT != null)
				dosREPORT.close();
			if(fosREPORT != null)
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock Adjustment Note Report"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
		    }
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT.. ");
		}		
	}
	/**
	 * Mthod to fetch data from database & to club it with header & footer.
	 */
	public void getDATA()
	{
		try
		{
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);				
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Stock Adjustment Note Report </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}							
			M_strSQLQRY = "SELECT GR_GRNNO,GR_GRNDT,GR_PORNO,GR_VENCD,GR_MATCD,GR_CHLNO,GR_CHLDT,"
				+"GR_CHLQT,GR_RECQT,CT_MATDS,CT_UOMCD,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,"
				+"PT_ADR04,PT_PINCD,PT_CTYNM FROM MM_GRMST,CO_CTMST,CO_PTMST WHERE "
				+"CT_MATCD = GR_MATCD AND PT_PRTCD = GR_VENCD AND PT_PRTTP = 'S' AND "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_GRNNO BETWEEN '"+txtFMGRN.getText()+"' AND '"+txtTOGRN.getText()+"' AND "
				+"GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND isnull(GR_STSFL,'') <> 'X' AND "
				+"GR_CHLQT <> GR_RECQT AND isnull(PT_STSFL,'') <> 'X' "
				+"ORDER BY GR_GRNNO, GR_MATCD ";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;					
					if(cl_dat.M_intLINNO_pbst >42)
					{						
						dosREPORT.writeBytes("\n"+strDOTLN+"\n");
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);						
						prnHEADER();
					}					
					if(!strGRNNO.equals(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")))
					{
						if(!strGRNNO.equals(""))
							prnFOOTER();
						strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
						prnHEADER();
					}					
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),61));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),3));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),""),11));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),""),11));					
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst++;
				}
				M_rstRSSET.close();
			}
			prnFOOTER();
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))			
				prnFMTCHR(dosREPORT,M_strCPI10);			
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("</fontsize></PRE></P></BODY></HTML>");
				setMSG("For HTML file Printing Please select Page Size 210-305 mm  through Page Setup..",'N');
			}
			dosREPORT.close();
			fosREPORT.close();
			strGRNNO = "";				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
	}
	/**
	 * Method to generate the header part of the Report.
	 */
	public void prnHEADER()
	{
		try
		{
			Date tmpDATE;
			dosREPORT.writeBytes("\n");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strENH);	
				prnFMTCHR(dosREPORT,M_strBOLD);
			}
			if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes("         SUPREME PETROCHEM LIMITED");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOBOLD);
				prnFMTCHR(dosREPORT,M_strNOENH);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}			
			else if (M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("<H3><CENTER>");
				dosREPORT.writeBytes(cl_dat.M_strCMPNM_pbst+"\n");
				dosREPORT.writeBytes("</H3></pre><PRE style =\" font-size : 8 pt \">");
				dosREPORT.writeBytes(M_strADDRS+"\n");
				dosREPORT.writeBytes(M_strCONNO+"\n");
			}
			if(M_rdbTEXT.isSelected())
			{
				dosREPORT.writeBytes("\n"+padSTRING('C',M_strADDRS,150)+"\n");
				dosREPORT.writeBytes(padSTRING('C',M_strCONNO,150)+"\n");		
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI12);				
				prnFMTCHR(dosREPORT,M_strENH);
			}
			if(M_rdbTEXT.isSelected())
				dosREPORT.writeBytes("                   Registered AD"+"\n");	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strNOENH);
			}
			else if (M_rdbHTML.isSelected())
			{
				dosREPORT.writeBytes("</CENTER></pre><H3><CENTER>");
				dosREPORT.writeBytes("Registered AD" +"\n");
				dosREPORT.writeBytes("</H3></CENTER><PRE style =\" font-size : 10 pt \">");
			}			
			dosREPORT.writeBytes(padSTRING('R',"Ref : GRIN Number : "+nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),71));
			dosREPORT.writeBytes(padSTRING('R',"Date        : "+cl_dat.M_strLOGDT_pbst,26)+"\n\n");			
			dosREPORT.writeBytes(padSTRING('R',"To,",71));
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),26)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),71));
			dosREPORT.writeBytes(padSTRING('R',"P. O. No.   : "+nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),26)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),""),71));
			//For P.O. Date 			
			strPORNO = nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),"");
			String L_strSQLQRY = "SELECT PO_PORDT FROM MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"PO_PORNO = '"+strPORNO+"'";
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY3(L_strSQLQRY);
			if(L_rstRSSET.next())
			{
				tmpDATE = L_rstRSSET.getDate("PO_PORDT");
				if(tmpDATE != null)
					dosREPORT.writeBytes(padSTRING('R',"P. O. Date  : "+M_fmtLCDAT.format(tmpDATE),26));
				else
					dosREPORT.writeBytes(padSTRING('R',"P. O. Date  : ",26));
			}
			else
				dosREPORT.writeBytes(padSTRING('R',"P. O. Date  : ",26));			
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),""),76)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),""),76)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),""),76)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PT_CTYNM"),"")+" "+nvlSTRVL(M_rstRSSET.getString("PT_PINCD"),""),76)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Ref : Your Challan No. "+nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),""),39));
			dosREPORT.writeBytes(" Dated : ");
			tmpDATE = M_rstRSSET.getDate("GR_CHLDT");
			if(tmpDATE != null)
				dosREPORT.writeBytes(M_fmtLCDAT.format(tmpDATE));			
			dosREPORT.writeBytes("\n\n"+"Dear Sir,"+"\n\n");			
			dosREPORT.writeBytes("The subject consignment was received by us on ");
			tmpDATE = M_rstRSSET.getDate("GR_GRNDT");
			if(tmpDATE != null)
				dosREPORT.writeBytes(M_fmtLCDAT.format(tmpDATE));
			else
				dosREPORT.writeBytes("           ");
			dosREPORT.writeBytes(" apparently in sound / damaged condition.");			
			dosREPORT.writeBytes("\n"+"Details of discrepancies observed are as follows.");			
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			dosREPORT.writeBytes("Material Code And Description                                           UOM    Chl Qty    Rec Qty");			
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
			cl_dat.M_intLINNO_pbst = 22;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	/**
	 * Method to generate the footer part of the report.
	 */
	public void prnFOOTER()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			dosREPORT.writeBytes(strDOTLN+"\n");			
			dosREPORT.writeBytes("You are therefore, requested to take immediate action on the following.."+"\n");			
			dosREPORT.writeBytes("1. Despatch short supplied items without any further delay."+"\n");			
			dosREPORT.writeBytes("2. Received quantity is not same as the challan quantity."+"\n");			
			dosREPORT.writeBytes("3. ");
			dosREPORT.writeBytes("\n\n\n");
			dosREPORT.writeBytes("Thanking you,"+"\n");			
			dosREPORT.writeBytes("Yours Faithfully,"+"\n");			
			dosREPORT.writeBytes("For SUPREME PETROCHEM LIMITED"+"\n\n\n");			
			dosREPORT.writeBytes("AUTHORISED SIGNATORY"+"\n\n");			
			dosREPORT.writeBytes("Material Handling Division");
			if(M_rdbHTML.isSelected())
				dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strEJT);				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTER");
		}
	}
}