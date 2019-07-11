/*
System Name		: Materials Management System
Program Name	: Stock Adjustment Note Report 
Author			: Mr.S.R.Mehesare

Modified Date	: 17/10/2004
Version			: v2.0.0
*/
import java.sql.ResultSet;import java.util.Date;
import java.awt.event.KeyEvent;import javax.swing.JComponent; 
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComboBox;
/**<pre>
System Name  : Material Management System.
 
Program Name : Stock Adjustment Note Report 

Purpose : Program to generate Stock Adjustment Note Report for given range of SAN No.
List of SAN report is also generated. SAN value is also printed on SAN List.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_SAMST       SA_MMSBS,SA_STRTP,SA_SANNO,SA_MATCD                     #
MM_STMST       ST_MMSBS,ST_STRTP,ST_MATCD                              #
MM_RMMST                                                               #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size      Description
--------------------------------------------------------------------------------------
txtFMSAN    SA_SANNO       MM_SAMST      VARCHAR(8)     SAN Number
txtTOSAN    SA_SANNO       MM_SAMST      VARCHAR(8)     SAN Number
--------------------------------------------------------------------------------------
<B>Conditions Give in Query:</b>
            
   Report Data is taken from tables MM_SAMST and MM_STMST for given condition     
      1) ST_MATCD = SA_MATCD 
      2) AND ST_STRTP = SA_STRTP 
      3) AND SA_STRTP = specified Store type 
      4) AND SA_SANNO in the range of given range
      5) AND isnull(SA_STSFL,' ') <> 'X'
   Remark description is taken from MM_RMMST 
      1) RM_TRNTP = 'SA' 
      2) AND RM_DOCTP = 'SA' 
      3) AND RM_DOCNO = given SAN number
      4) AND isnull(RM_STSFL,'') <> 'X'
<I>
<B>Validations :</B>
   - SAN Number Specified must be valid 
</I> */

class mm_rpsan extends cl_rbase
{									/** JcomboBox to display & to select Report Type.*/
	private JComboBox cmbRPTOP;		/** JTextField to display & to enter SAN Number.*/
	private JTextField txtFMSAN;	/** JTextField to display & to enter SAN Number.*/
	private JTextField txtTOSAN;	/** String variable for Store Type.*/	
	private String strSTRNM;		/** String variable for SAN Number.*/
	private String strSANNO;		/** String variable for Remark Description.*/
	private String strREMDS;		/** String variable for the Name if the Person who Prepared the SAN.*/
	private String strPREBY;		/** String variable for the Name of the Person who has Appproved the SAN.*/
	private String strAPRBY;		/** String variable for the Name of the person who Recomended the SAN.*/
	private String strRECBY;		/** Boolean Varaible to manage the fetching of data only Once.*/
	private boolean flgFIRST;		/** Double Variable to calculate the SAN Value.*/
	private double dblSANVL=0;		/** Integer variable for Serial Number.*/
	private int intSRLNO;				/** FileOutputStream to generate the Report file from the stream of data.*/
	private FileOutputStream fosREPORT;	/** DataOutputStream to generate the stream of Reposrt Data.*/
	private DataOutputStream dosREPORT;	/** String variable for generated Report file Name.*/
	private String strFILNM;
	
	public mm_rpsan()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			strSTRNM = "";
			strSANNO = "";			
			cmbRPTOP = new JComboBox();
			cmbRPTOP.addItem("SAN List");
			cmbRPTOP.addItem("SAN Note");
			
			setMatrix(20,8);
			add(new JLabel("Report Type"),3,4,1,1,this,'L');
			add(cmbRPTOP,3,5,1,1.5,this,'L');
			add(new JLabel("From SAN No."),4,4,1,1,this,'L');
			add(txtFMSAN = new TxtNumLimit(8.0),4,5,1,1,this,'L');
			add(new JLabel("To SAN No."),5,4,1,1,this,'L');
			add(txtTOSAN = new TxtNumLimit(8.0),5,5,1,1,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In Constructor");
		}
	}
	public mm_rpsan(String P_strSBSCD)
	{
		M_strSBSCD = P_strSBSCD;
		M_rdbTEXT.setSelected(true);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				setENBL(true);
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				setENBL(true);
				cmbRPTOP.requestFocus();
				setMSG("Select Report Type..",'N');
			}
			else
				setENBL(false);
		}
		if(M_objSOURC == M_cmbDESTN)
		{
			if(M_cmbDESTN.getSelectedIndex() > 0)
			{
				cmbRPTOP.requestFocus();
				setMSG("Select Report Type..",'N');
			}
			else
				M_cmbDESTN.requestFocus();
		}
		if(M_objSOURC == cmbRPTOP)
		{
			txtFMSAN.setText("");	txtTOSAN.setText("");
			txtFMSAN.requestFocus();
			setMSG("Enter From SAN Number",'N');
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(M_objSOURC == txtFMSAN)
			{
				M_strHLPFLD = "txtFMSAN";
				M_strSQLQRY = "SELECT distinct SA_SANNO,SA_SANDT FROM MM_SAMST WHERE SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
				if(txtFMSAN.getText().length() > 0)
					M_strSQLQRY += "SA_SANNO LIKE '"+txtFMSAN.getText()+"%' AND ";
				M_strSQLQRY += "isnull(SA_STSFL,' ') <> 'X' AND "
					+"SA_STRTP = '"+M_strSBSCD.substring(2,4)+"' ORDER BY SA_SANNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"SAN Number","SAN Date"},2,"CT");
			}
			if(M_objSOURC == txtTOSAN)
			{
				M_strHLPFLD = "txtTOSAN";
				M_strSQLQRY = "SELECT distinct SA_SANNO,SA_SANDT FROM MM_SAMST WHERE SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
				if(txtTOSAN.getText().length() > 0)
					M_strSQLQRY += "SA_SANNO LIKE '"+txtTOSAN.getText()+"%' AND ";
				M_strSQLQRY += "isnull(SA_STSFL,' ') <> 'X' AND "
					+"SA_STRTP = '"+M_strSBSCD.substring(2,4)+"' ORDER BY SA_SANNO DESC";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"SAN Number","SAN Date"},2,"CT");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == cmbRPTOP)
			{
				txtFMSAN.requestFocus();
				setMSG("Enter From SAN Number",'N');
			}
			if(M_objSOURC == txtFMSAN )
			{
				if(txtFMSAN.getText().length()>0)
				{
					txtTOSAN.setEnabled(true);
					if(txtTOSAN.getText().length() == 0)
					{
						txtTOSAN.setText(txtFMSAN.getText().trim());
						txtTOSAN.select(0,txtTOSAN.getText().length());
						setMSG("Enter To SAN Number",'N');
					}
				}
				else
				{
					txtTOSAN.setText("");
					txtTOSAN.setEnabled(false);
				}
			}
			if(M_objSOURC == txtTOSAN )
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else
				((JComponent)M_objSOURC).transferFocus();
		}
	}
	/**
	 * Super Class method overrided to enable & disable the components.
	 */
	public void setENBL(boolean ACTION)
	{
		super.setENBL(ACTION);
		txtFMSAN.setEnabled(true);
		txtTOSAN.setEnabled(false);
	}
	/**
	 * Super Class method overrided to execuate the F1 help for selected Field.
	 */
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtFMSAN")
		{
			txtFMSAN.setText(cl_dat.M_strHLPSTR_pbst);
			if(txtTOSAN.getText().trim().length() == 0)
				txtTOSAN.setText(txtFMSAN.getText().trim());
			txtTOSAN.setEnabled(true);
			txtTOSAN.requestFocus();
		}
		if(M_strHLPFLD == "txtTOSAN")
		{
			txtTOSAN.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}	
	/**
	 * Method to fetch data from the database & to club it with header & footer of the report.
	 */
	void exePRINT()
	{
		try
		{
			int L_intSELID =0;
			if(!vldDATA())
				return;			
			
			if(cmbRPTOP.getSelectedItem().toString().equals("SAN List"))
				L_intSELID = 0;
			else 
				L_intSELID = 1;
			getDATA(txtFMSAN.getText().trim(),txtTOSAN.getText().trim(),L_intSELID);
				
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
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Stock adjestment Note"," ");
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
	 * Method to validate the inputs before execuation of the SQL Query.
	 */
	public boolean vldDATA()
	{
		try
		{
			if(M_rdbHTML.isSelected())
				 strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpsan.html";
			if(M_rdbTEXT.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpsan.doc";
			if(txtFMSAN.getText().trim().length() == 0)
			{
				setMSG("Enter From SAN Number..",'E');
				txtFMSAN.requestFocus();
				return false;
			}
			if(txtTOSAN.getText().trim().length() == 0)
			{
				setMSG("Enter To SAN Number..",'E');
				txtTOSAN.requestFocus();
				return false;
			}
			else if( Integer.parseInt(txtTOSAN.getText().trim().toString()) < Integer.parseInt(txtFMSAN.getText().trim().toString()) )
			{
				setMSG("To SAN Number Should Be Grater Than Or Equal To From SAN Number..",'E');
				txtTOSAN.requestFocus();
				return false;
			}
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'E');
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
	 * Method to fetch data from the database & to club it with header & footer
	 */
	public void getDATA(String P_strFMSAN,String P_strTOSAN,int P_intRPTOP) throws Exception
	{
		try
		{
			ResultSet L_rstRSSET;
			String L_strSANVL ="";
			String L_strSANRT ="";
			Date tmpDATE;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generating Report..",'N');
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 1;
			flgFIRST = false;
			strSANNO ="";
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>List of Issues </title> ");
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}");
				dosREPORT.writeBytes("</STYLE></HEAD>"); 
				dosREPORT.writeBytes("<BODY><P><PRE style =\" font-size : 10 pt \">");    
			}
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
			{
				prnFMTCHR(dosREPORT,M_strNOCPI17);
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}	
			M_strSQLQRY = "SELECT SA_SANNO,SA_SANDT,SA_PREBY,SA_RECBY,SA_APRBY,SA_MATCD,"
				+"SA_SANQT,SA_SANRT,SA_SANVL,ST_MATDS,ST_UOMCD,ST_LOCCD FROM MM_SAMST,MM_STMST WHERE "
				+"ST_MATCD = SA_MATCD AND ST_STRTP = SA_STRTP AND SA_CMPCD=ST_CMPCD AND "
				+"SA_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND SA_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND SA_SANNO BETWEEN '"
				+P_strFMSAN+"' AND '"+P_strTOSAN+"' AND "
				+"isnull(SA_STSFL,' ') <> 'X' ORDER BY SA_SANNO,SA_MATCD ";
						
			M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);
			if(P_intRPTOP == 1) // SAN Note
				prnSANNT(P_strFMSAN,P_strTOSAN);
			else  // SAN List
			{
				prnHEADER(P_strFMSAN,P_strTOSAN,P_intRPTOP);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						if(cl_dat.M_intLINNO_pbst >56)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							cl_dat.M_intLINNO_pbst = 0;
							cl_dat.M_PAGENO += 1;
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER(P_strFMSAN,P_strTOSAN,P_intRPTOP);
						}
						if(!strSANNO.equals(nvlSTRVL(M_rstRSSET.getString("SA_SANNO"),"")))
						{
							strREMDS = "";
							if(flgFIRST)
							{
								dosREPORT.writeBytes("\n\n");
								cl_dat.M_intLINNO_pbst++;
								String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'SA' AND "
									+"RM_STRTP ='"+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'SA' AND RM_DOCNO = '"+strSANNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
									
								L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
									
								if(L_rstRSSET.next())
									strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
									
								L_rstRSSET.close();
								dosREPORT.writeBytes(padSTRING('L',"Total  : "+setNumberFormat(dblSANVL,2),96));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst++;
								dblSANVL =0;	
								if(strREMDS.length() > 87)
								{
									dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS.substring(0,87),96));
									dosREPORT.writeBytes("\n");
									dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
									cl_dat.M_intLINNO_pbst++;
								}
								else
									dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS,96));
							}
							flgFIRST = true;
									
							dosREPORT.writeBytes("\n\n");
							dosREPORT.writeBytes(padSTRING('R',"",4));
							dosREPORT.writeBytes(padSTRING('R',M_strSBSCD.substring(2,4)+"/"+nvlSTRVL(M_rstRSSET.getString("SA_SANNO"),""),18));
							tmpDATE = M_rstRSSET.getDate("SA_SANDT");
							if(tmpDATE != null)
								dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(tmpDATE),15));
							else
								dosREPORT.writeBytes(padSTRING('R',"",15));
							strSANNO = nvlSTRVL(M_rstRSSET.getString("SA_SANNO"),"");
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SA_PREBY"),""),15));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SA_APRBY"),""),15));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SA_RECBY"),""),15));
							dosREPORT.writeBytes("\n");
							cl_dat.M_intLINNO_pbst += 3;
							intSRLNO = 1;
						}
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SA_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),50));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),6));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
						dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SA_SANQT"),""),10));
						L_strSANVL = nvlSTRVL(M_rstRSSET.getString("SA_SANVL"),"0");
						L_strSANRT = nvlSTRVL(M_rstRSSET.getString("SA_SANRT"),"0");
						dosREPORT.writeBytes(padSTRING('L',L_strSANVL,10));
						dosREPORT.writeBytes(padSTRING('L',L_strSANRT,10));
						dblSANVL +=  Double.parseDouble(L_strSANVL);
						cl_dat.M_intLINNO_pbst += 1;
						intSRLNO++;
					}
					String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'SA' AND RM_STRTP ='"
					+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'SA' AND RM_DOCNO = '"+strSANNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
					L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
					if(L_rstRSSET.next())
						strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
					L_rstRSSET.close();
					dosREPORT.writeBytes("\n");
			        dosREPORT.writeBytes(padSTRING('L',"Total  : "+setNumberFormat(dblSANVL,2),96));
					dosREPORT.writeBytes("\n");
					cl_dat.M_intLINNO_pbst++;
					dblSANVL =0;	
					if(strREMDS.length() > 87)
					{
						dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS.substring(0,87),96));
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
						cl_dat.M_intLINNO_pbst++;
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS,96));	
						M_rstRSSET.close();
				}
				else
				{
					setMSG("Record Not Found..",'E');
				}
			}
			if(P_intRPTOP == 0)
				dosREPORT.writeBytes("\n------------------------------------------------------------------------------------------------\n");
							
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
	 * Method to generate the header part of the Report.
	 */
	private void prnHEADER(String P_strFMSAN,String P_strTOSAN,int P_intRPTOP)
	{
		try
		{
			java.util.Date tmpDATE;			
			strSTRNM = cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString();
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,96));
			dosREPORT.writeBytes("\n");
			if(P_intRPTOP == 0)
				dosREPORT.writeBytes(padSTRING('R',"Stock Adjustment Note From : "+P_strFMSAN+" To "+P_strTOSAN,76));
			else
			{
				dosREPORT.writeBytes(padSTRING('R',"Stock Adjustment Note ",75));
				dosREPORT.writeBytes(padSTRING('R',"SAN No. : "+M_strSBSCD.substring(2,4)+"/"+nvlSTRVL(M_rstRSSET.getString("SA_SANNO"),""),21));
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"",75));
			if(P_intRPTOP == 0)
				dosREPORT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,21));
			else
			{
				tmpDATE = M_rstRSSET.getDate("SA_SANDT");
				if(tmpDATE != null)
					dosREPORT.writeBytes(padSTRING('R',"SAN Dt. : "+M_fmtLCDAT.format(tmpDATE),21));
				else
					dosREPORT.writeBytes(padSTRING('R',"SAN Dt. : ",21));
			}
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,75));
			dosREPORT.writeBytes(padSTRING('R',"Page No : "+cl_dat.M_PAGENO,21));
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			dosREPORT.writeBytes("\n");
			if(P_intRPTOP == 0)
			{
				dosREPORT.writeBytes("Sr. SAN Number        SAN Date       Prepared By    Approved By    Received By    ");
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("No. Material Code And Description                                Locn  UOM  Adj. Qty       Value");
			}
			else
			dosREPORT.writeBytes("No. Material Code And Description                                          Locn  UOM    Adj. Qty");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
			cl_dat.M_intLINNO_pbst += 7;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getRPHDR");
		}
	}
	/**
	 * Metod to fetch SAN Details & to include it in the Report.
	 */
	public void prnSANNT(String P_strFMSAN,String P_strTOSAN)
	{
		try
		{
			if(M_rstRSSET != null)
			{
				flgFIRST = false;
				strREMDS = "";
				while(M_rstRSSET.next())
				{
					if(cl_dat.M_intLINNO_pbst >56)
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO += 1;
						if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
							prnFMTCHR(dosREPORT,M_strEJT);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						prnHEADER(P_strFMSAN,P_strTOSAN,1);
					}
					if(!strSANNO.equals(nvlSTRVL(M_rstRSSET.getString("SA_SANNO"),"")))
					{
						if(flgFIRST)
						{
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'SA' AND RM_STRTP ='"
								+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'SA' AND RM_DOCNO = '"+strSANNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
							ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
													
							if(L_rstRSSET.next())
								strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
													
							L_rstRSSET.close();
							dosREPORT.writeBytes("\n");
							if(strREMDS.length() > 87)
							{
								dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS.substring(0,87),96));
								dosREPORT.writeBytes("\n");
								dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
								cl_dat.M_intLINNO_pbst++;
							}
							else
								dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS,96));		
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							dosREPORT.writeBytes("\n\n");
							dosREPORT.writeBytes(padSTRING('R',"Prepared By : "+nvlSTRVL(M_rstRSSET.getString("SA_PREBY"),""),30));
							dosREPORT.writeBytes(padSTRING('R',"Recomended By : "+nvlSTRVL(M_rstRSSET.getString("SA_RECBY"),""),30));		
							dosREPORT.writeBytes(padSTRING('R',"Approved By : "+nvlSTRVL(M_rstRSSET.getString("SA_APRBY"),""),30));
							dosREPORT.writeBytes("\n");
							dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
							if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						}
						flgFIRST = true;
						prnHEADER(P_strFMSAN,P_strTOSAN,1);
						strSANNO = nvlSTRVL(M_rstRSSET.getString("SA_SANNO"),"");
						intSRLNO = 1;
						cl_dat.M_intLINNO_pbst = 0;
						cl_dat.M_PAGENO = 1;
					}
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',String.valueOf(intSRLNO),4));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SA_MATCD"),""),11));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),""),60));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),6));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),3));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SA_SANQT"),""),12));
					//dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("SA_SANVL"),""),12));
					strAPRBY = nvlSTRVL(M_rstRSSET.getString("SA_APRBY"),"");
					strRECBY = nvlSTRVL(M_rstRSSET.getString("SA_RECBY"),"");
					strPREBY = nvlSTRVL(M_rstRSSET.getString("SA_PREBY"),"");
					cl_dat.M_intLINNO_pbst += 1;
					intSRLNO++;
				}
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
				String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = 'SA' AND RM_STRTP ='"
					+M_strSBSCD.substring(2,4)+"' AND RM_DOCTP = 'SA' AND RM_DOCNO = '"+strSANNO+"' AND isnull(RM_STSFL,'') <> 'X' ";
				
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
													
				if(L_rstRSSET.next())
					strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
													
				L_rstRSSET.close();
				dosREPORT.writeBytes("\n");
				if(strREMDS.length() > 87)
				{
					dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS.substring(0,87),96));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',strREMDS.substring(87,strREMDS.length()),96));
					cl_dat.M_intLINNO_pbst++;
				}
				else
					dosREPORT.writeBytes(padSTRING('R',"Remark : "+strREMDS,96));		
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
				dosREPORT.writeBytes("\n\n");
				dosREPORT.writeBytes(padSTRING('R',"Prepared By : "+strPREBY,30));
				dosREPORT.writeBytes(padSTRING('R',"Recomended By : "+strRECBY,30));		
				dosREPORT.writeBytes(padSTRING('R',"Approved By : "+strAPRBY,30));
				dosREPORT.writeBytes("\n");
				dosREPORT.writeBytes("------------------------------------------------------------------------------------------------");
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))	
					prnFMTCHR(dosREPORT,M_strEJT);
				if(M_rdbHTML.isSelected())
					dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnSANNT");
		}
	}
}	
