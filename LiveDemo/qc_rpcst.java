/**
System Name   : Laboratoty Information Management System
Program Name  : Competitor Sample Testing Report for QCA only (for Document Record)s.
Program Desc. : 
Author        : Mr.S.R.Mehesare
Date          : 30 September 2005
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
import javax.swing.JTable;import javax.swing.JCheckBox;

/**<pre>
System Name : Laboratoty Information Management System.
 
Program Name : Competitor Sample Testing Report for QCA only.

Purpose : To take the Report of Competitors Sample Testing details with CSS Details
          for Documentation.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
QC_RMMST       RM_QCATP,RM_TSTTP, RM_TSTNO                             #
QC_PSMST       PS_QCATP, PS-TSTTP,PS_LOTNO,
               PS_RCLNO,PS_TSTNO,PS_TSTDT                              #
QC_SPMST       SP_SBSCD,SP_QCATP,SP_SMPTP,SP_SEFNO                     #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtSEFNO    SP_SEFNO       QC_SPMST      VARCHAR(8)    SEF Number.
--------------------------------------------------------------------------------------
<B>
Logic</B>
     Quality parameters are fetched for Test Type 0901.

<I><B>Conditions Give in Query:</b>

 Data is taken from QC_PSMST & QC_SPMST for given condiations
   1) PS_QCATP = Specified QCa Type.
   2) AND PS_SBSCD = Sub system Code
   3) AND PS_TSTTP = SP_SMPTP 
   4) AND PS_TSTNO = SP_TSTNO";
   5) AND PS_SBSCD = SP_SBSCD 
   6) AND SP_SMPTP = '0901'";
   7) AND SP_SEFNO = Specified SEF Number.  
   8) AND isnull(SP_STSFL,'')<>'X'
   9) AND isnull(PS_STSFL,'')<>'X' 

Remarks are taken from QC_RMMST as :-
   1) RM_QCATP = Specified QCa Type.
  For SEF Remark (Entered By CSS)
   2) AND RM_TSTTP || RM_TSTNO = 0901 + Specified SEF Number.
  For Testing Remark (Entered By QCA)
   3) AND RM_TSTTP || RM_TSTNO = 0901 + Test Number fetched from Database.
  For SEF Remark (Entered By QCA For QCA Only)
   4) AND RM_TSTTP || RM_TSTNO = CSSR + Test Number fetched from Database.

<B>Validations :</B>
    - SEF Number Specified must be valid.
</I> */

public class qc_rpcst extends cl_rbase
{										/** JTextField to enter & display Zone Code.*/
	private JTextField txtSEFNO;		/** JTextField to display Manufacturer Description.*/
	private JTextField txtMFGBY;		/** JTextField to display Grade Description.*/
	private JTextField txtGRDDS;		/** FileOutputStream for the generated Report File Handeling.*/		
	private FileOutputStream fosREPORT ;/** DataOutputStream object to store the data as a Stream */
    private DataOutputStream dosREPORT ;/** Final String variable for QCA Special Reamrk. */
	private final String strSPLRM ="CSSR";/** String varaible for class Name.*/
	private String strCLSNM;			/** String variable for generated Report File Name.*/	                        			 
	private String strFILNM;			/** Integer variable to count the Number of Rcords fetched to block the report if no data found.*/
	private int intRECCT;				/** String varaible to append Dotted Line in the Report.*/	
	private String strDOTLN="-------------------------------------------------------------------------------------------";
	qc_rpcst()
	{
		super(2);
		try
		{
			strCLSNM = getClass().getName();			
			setMatrix(20,8);
		    M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("SEF No."),4,3,1,.8,this,'R');
			add(txtSEFNO = new JTextField(),4,4,1,1,this,'L');
			add(new JLabel("MFG By"),5,3,1,.8,this,'R');
			add(txtMFGBY = new JTextField(),5,4,1,3,this,'L');
			add(new JLabel("Grade"),6,3,1,.8,this,'R');
			add(txtGRDDS = new JTextField(),6,4,1,3,this,'L');
			M_pnlRPFMT.setVisible(true);			
			setENBL(false);				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super Class Method overrided to enable & disable the components.
	 */
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);
		txtMFGBY.setEnabled(false);
		txtGRDDS.setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()!=0)
				{
					setMSG("Please Enter the Date..",'N');				
					setENBL(true);
					txtMFGBY.setEnabled(false);
					txtGRDDS.setEnabled(false);
					txtSEFNO.requestFocus();
					setMSG("Please Enter SEF No, Press F1 to Select from List..",'N');
				}								
				else
				{
					setMSG("Please Select an Option..",'N');
					setENBL(false);	
				}
			}
			if(M_objSOURC == txtSEFNO)
			{
				if(txtSEFNO.getText().trim().length()>0)
				{
					M_strSQLQRY = "Select SP_MFGBY,SP_GRDDS from QC_SPMST where ";
					M_strSQLQRY += " SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SMPTP = '0901' AND isnull(SP_STSFL,'')<>'X'";
					M_strSQLQRY += " AND SP_SEFNO ='"+ txtSEFNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							txtMFGBY.setText(nvlSTRVL(M_rstRSSET.getString("SP_MFGBY"),""));
							txtGRDDS.setText(nvlSTRVL(M_rstRSSET.getString("SP_GRDDS"),""));
							cl_dat.M_btnSAVE_pbst.requestFocus();
						}
						else						
							setMSG("Invalid SEF Number, Press F1 to select From List..",'E');						
						M_rstRSSET.close();
					}
					else
						setMSG("Invalid SEF Number, Press F1 to select From List..",'E');
				}								
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"");
		}		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);       
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			try
			{      
				this.setCursor(cl_dat.M_curWTSTS_pbst);
   				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtSEFNO";					
				String[] L_arrTBHDR = {"SEF No.","SEF Date","SEF By","Sample Type","Manufacturer"};					
				M_strSQLQRY = "Select SP_SEFNO,SP_SEFDT,SP_SEFBY,SP_SMPTP,SP_MFGBY from QC_SPMST where";
				M_strSQLQRY += " SP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SP_SBSCD = '"+ M_strSBSCD.trim() +"'";
				M_strSQLQRY += " AND SP_QCATP = '"+ M_strSBSCD.substring(2,4).trim() +"'";
				M_strSQLQRY += " AND isnull(SP_STSFL,'') <> 'X'";
				M_strSQLQRY += " order by SP_SEFNO";					
				cl_hlp(M_strSQLQRY,1,1,L_arrTBHDR,5,"CT");
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"F1 Help..");                            
			}
		}
	}
	/**
	 * Super class method to execute the F1 help for SEF Number.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();		
		if(M_strHLPFLD.equals("txtSEFNO"))
		{
			cl_dat.M_flgHELPFL_pbst = false;
			txtSEFNO.setText(cl_dat.M_strHLPSTR_pbst);			
		}		
	}
	/**
	* Method to generate the report & to forward ot to Specified Destination.
	*/
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
				return;							
			if(M_rdbHTML.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpcst.html";
			else if(M_rdbTEXT.isSelected())
			    strFILNM = cl_dat.M_strREPSTR_pbst + "qc_rpcst.doc";							
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Competitor Sample Testing Report"," ");
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
    * Method to fetch data from Qc_SPMST & QC_PSMST tables & club it with Header &
    * footer in DataOutputStream.
	*/
	private void getDATA()
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
	    try
		{	
			intRECCT = 0;
			String L_strTSTNO="",L_strDATE="";
			setCursor(cl_dat.M_curWTSTS_pbst);
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			setMSG("Report Generation in Process.......",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{				
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Finished product Analysis </title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}						
			prnHEADER();

			
			M_strSQLQRY = "Select * from QC_SPMST,QC_PSMST where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_QCATP = '"+M_strSBSCD.substring(0,2)+"'";
			M_strSQLQRY += " AND PS_SBSCD ='"+ M_strSBSCD+"' AND PS_CMPCD = SP_CMPCD and PS_TSTTP = SP_SMPTP AND PS_TSTNO = SP_TSTNO";			
			M_strSQLQRY += " AND PS_SBSCD = SP_SBSCD AND SP_SMPTP = '0901' AND isnull(SP_STSFL,'')<>'X'";
			M_strSQLQRY += " AND isnull(PS_STSFL,'')<>'X' AND SP_SEFNO = '"+ txtSEFNO.getText().trim() +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String strDATE="";
				if(M_rstRSSET.next())
				{	
					intRECCT = 1;				
					dosREPORT.writeBytes(padSTRING('R',"SEF No.        : "+txtSEFNO.getText().trim(),60));
					dosREPORT.writeBytes("SEF By         : " + nvlSTRVL(M_rstRSSET.getString("SP_SEFBY"),"")+"\n");
					
					dosREPORT.writeBytes(padSTRING('R',"MFG By         : " + nvlSTRVL(M_rstRSSET.getString("SP_MFGBY"),""),60));
					java.util.Date L_tmpDATE = M_rstRSSET.getDate("SP_SEFDT");
					if(L_tmpDATE != null)
						L_strDATE = M_fmtLCDAT.format(L_tmpDATE);
					dosREPORT.writeBytes("SEF Date       : " + L_strDATE +"\n");
					
					dosREPORT.writeBytes(padSTRING('R',"Grade          : " + nvlSTRVL(M_rstRSSET.getString("SP_GRDDS"),""),60));
					dosREPORT.writeBytes("Prd Category   : " + nvlSTRVL(M_rstRSSET.getString("SP_PRDCT"),"") +"\n");
					
					dosREPORT.writeBytes(padSTRING('R',"Sample Des.    : " + nvlSTRVL(M_rstRSSET.getString("SP_SMPDS"),""),60));
					dosREPORT.writeBytes("Sample Qty.    : " + nvlSTRVL(M_rstRSSET.getString("SP_SMPQT"),"")+"\n");
					
					dosREPORT.writeBytes(padSTRING('R',"Purpose        : " + nvlSTRVL(M_rstRSSET.getString("SP_SMPPR"),""),60));
					dosREPORT.writeBytes("Collected By   : " + nvlSTRVL(M_rstRSSET.getString("SP_SCLBY"),"")+"\n");
					
					dosREPORT.writeBytes(padSTRING('R',"Collected From : " + nvlSTRVL(M_rstRSSET.getString("SP_SCLFR"),""),60));
					L_tmpDATE = M_rstRSSET.getDate("SP_SCLDT");
					if(L_tmpDATE != null)
						L_strDATE = M_fmtLCDAT.format(L_tmpDATE);					
					dosREPORT.writeBytes("Collected Date : " + L_strDATE +"\n");
										
					dosREPORT.writeBytes(padSTRING('R',"Batch No.      : " + nvlSTRVL(M_rstRSSET.getString("SP_BATNO"),""),60));
					dosREPORT.writeBytes("Lot No.        : " + nvlSTRVL(M_rstRSSET.getString("SP_LOTNO"),"")+"\n");
					dosREPORT.writeBytes("Zone Code      : " + nvlSTRVL(M_rstRSSET.getString("SP_ZONCD"),"")+"\n");
					dosREPORT.writeBytes(strDOTLN+"\n");
					
					L_strTSTNO = nvlSTRVL(M_rstRSSET.getString("SP_TSTNO"),"");
					/////QCA DETAILS					
					dosREPORT.writeBytes("SrNo   Property  Description                             Unit           Test Results"+"\n");//   SPL Eq.Grade"+"\n");
					//dosREPORT.writeBytes("                                                                                  Test Date"+"\n");
					dosREPORT.writeBytes(strDOTLN+"\n");
					
					String L_strSQLQRY = "Select CMT_CODCD,CMT_CHP01,CMT_CHP02,QS_QPRDS,QS_UOMCD from CO_QSMST,CO_CDTRN";
					L_strSQLQRY += " where isnull(CMT_STSFL,'')<>'X' AND CMT_CODCD = QS_QPRCD ";
					L_strSQLQRY += " AND CMT_CGMTP ='SYS' AND CMT_CGSTP = 'QCXXCSA' ";					
					if(strCLSNM.equals("qc_rpcs2"))
						L_strSQLQRY += " AND CMT_CHP01 = 'Y'";
					L_strSQLQRY += " order by CMT_CHP02";
					
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
					if(L_rstRSSET != null)
					{																
						String L_strTEMP= "";
						int L_intSRLNO = 1;
						while (L_rstRSSET.next())
						{	
							L_strTEMP = Integer.toString(L_intSRLNO);
							if(L_strTEMP.length() == 1)
								L_strTEMP = "0" + L_strTEMP;
							
							dosREPORT.writeBytes(padSTRING('L',L_strTEMP,4)+"   ");
							L_strTEMP = nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),"");
							dosREPORT.writeBytes(padSTRING('R',L_strTEMP,9)+" ");
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("QS_QPRDS"),""),40));							
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(L_rstRSSET.getString("QS_UOMCD"),""),15));
							
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("PS_"+L_strTEMP+"VL"),"-"),12));
							dosREPORT.writeBytes("\n");
							L_intSRLNO++;
						}
						L_rstRSSET.close();
					}										
				}	
			}
			dosREPORT.writeBytes(strDOTLN+"\n");
			String L_strREMDS="";
			M_strSQLQRY = "Select RM_REMDS,RM_TSTTP,RM_TSTNO from QC_RMMST where";
			M_strSQLQRY += " RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_QCATP ='" + M_strSBSCD.substring(2,4)+"'";
			
			//if(strCLSNM.equals("qc_rpcst")) ///QCA
			//{
				M_strSQLQRY += " AND RM_TSTTP + RM_TSTNO in('0901" + txtSEFNO.getText().trim()+"'";				
				M_strSQLQRY += ",'0901" + L_strTSTNO +"','"+ strSPLRM + L_strTSTNO +"')" ;				
			//}				
			/*else if(strCLSNM.equals("qc_rpcs2"))////CSS
			{
				M_strSQLQRY += " AND RM_TSTTP || RM_TSTNO in('0901" + txtSEFNO.getText().trim()+"','0901";
				M_strSQLQRY +=  L_strTSTNO +"','"+ strSPLRM + L_strTSTNO +"')" ;
			}*/
			M_strSQLQRY += " order by RM_TSTTP desc";			
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{		
				String L_strTEMP1 ="";
				while(M_rstRSSET.next())
				{
					L_strTEMP1 = nvlSTRVL(M_rstRSSET.getString("RM_TSTTP"),"").trim();
					L_strTEMP1 = L_strTEMP1 + nvlSTRVL(M_rstRSSET.getString("RM_TSTNO"),"").trim();
										
					if(L_strTEMP1.equals( "0901" + txtSEFNO.getText().trim()))
					{
						L_strREMDS ="\n"+"SEF Remark     : "+ nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"").trim();
						if(L_strREMDS.length()> strDOTLN.length())
						{
							L_strREMDS = L_strREMDS.substring(0,strDOTLN.length())+"\n"
							 +"                 "+L_strREMDS.substring(strDOTLN.length())+"\n";
						}
						else
						   L_strREMDS = L_strREMDS +"\n";
						dosREPORT.writeBytes(L_strREMDS);
					}
					else if(L_strTEMP1.equals("0901" + L_strTSTNO ))
					{
						L_strREMDS = "\n"+"Testing Remark : "+nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"").trim();
						if(L_strREMDS.length()> strDOTLN.length())
						{
							L_strREMDS = L_strREMDS.substring(0,strDOTLN.length())+"\n"
							 +"                 "+L_strREMDS.substring(strDOTLN.length())+"\n";
						}
						else
						   L_strREMDS = L_strREMDS +"\n";
						dosREPORT.writeBytes(L_strREMDS);
					}
					else if(L_strTEMP1.equals(strSPLRM + L_strTSTNO ) && (strCLSNM.equals("qc_rpcst")))
					{
						L_strREMDS = "\n"+"Sp. Remark     : "+ nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"").trim();						
						if(L_strREMDS.length()> strDOTLN.length())
						{
							L_strREMDS = L_strREMDS.substring(0,strDOTLN.length())+"\n"
							 +"                 "+L_strREMDS.substring(strDOTLN.length())+"\n";
						}
						else
						   L_strREMDS = L_strREMDS +"\n";
					dosREPORT.writeBytes(L_strREMDS);
					}										
				}
				M_rstRSSET.close();
			}			
			dosREPORT.writeBytes("\n"+strDOTLN.toString()+"\n");
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
	 * Method to generate the Header Part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strBOLD);
			else if(M_rdbHTML.isSelected())				
				dosREPORT.writeBytes("<B>");
			dosREPORT.writeBytes("\n\n\n\n\n"+cl_dat.M_strCMPNM_pbst+"\n");						
			dosREPORT.writeBytes(padSTRING('R',"Competitors Sample Testing Report",strDOTLN.length() - 18));			
			dosREPORT.writeBytes("Date : " + cl_dat.M_strLOGDT_pbst + "\n");			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
				prnFMTCHR(dosREPORT,M_strNOBOLD);
			else if(M_rdbHTML.isSelected())				
				dosREPORT.writeBytes("</B>");
			dosREPORT.writeBytes(strDOTLN+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}		
	}
	/**
	* Method to validate Inputs given, before execution of SQL Query.
	*/
	boolean vldDATA()
	{
		try
		{		
			if(txtSEFNO.getText().trim().length()== 0)
			{
				setMSG("SEF number cannot be Blank..",'E');
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
}