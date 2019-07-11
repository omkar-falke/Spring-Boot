/*
System Name		: Materials Management System
Program Name	: List Of Rejected GRIN 
Author			: Mr.S.R.Mehesare
Modified Date	: 13/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import java.sql.ResultSet;import java.util.Date;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
/**<pre>
System Name : Material Management System.
 
Program Name : List Of Rejected GRIN

Purpose : This program generates Report for List Of rejected GRIN for given date Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT   GR_ACPDT        MM_GRMST      Date          GRIN Accepted Date
txtTODAT   GR_ACPDT        MM_GRMST      Date          GRIN Accepted Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
   Data is taken from MM_GRMST and CO_CTMST for condiations :- 
      1) CT_MATCD = GR_MATCD 
      2) AND GR_STRTP = selected Store Type.
      3) AND GR_REJQT > 0 
      4) AND GR_ACPDT between given Date range
      5) ifnull(GR_STSFL,' ') <> 'X'

<B>Validations & Other Information:</B>    
    - To date must be greater than from date & smaller than current date.
</I> */

class mm_rprej extends cl_rbase
{									/** JTextField to display & to enter from Date.*/
	private JTextField txtFMDAT;	/** JTextField to diaplay & to enter to Date.*/
	private JTextField txtTODAT;	/** String variable for grin number.*/
	private String strGRNNO="";		/** Integer variable for grin count.*/
	private int intGRNCT;			/** Integer variable to count number of Records fetched to block the Report if no data found.*/
	private int intRECCT;			/** String variable for generated Report File Name.*/
	private String strFILNM;		/**	FileOutputStream Object to generate the Report file from stream of data.*/
	private FileOutputStream fosREPORT;/** DataOutputStream Object to hold Stream of data to generate the Report File.*/
	private DataOutputStream dosREPORT;	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	private JTextField txtVENCD;
	private JTextField txtVENNM;
	private ButtonGroup btgRPTTP;    
	private JRadioButton rdbPRTWS;	 
	private JRadioButton rdbDOCWS;
   
	public mm_rprej()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			btgRPTTP=new ButtonGroup();
			add(rdbDOCWS=new JRadioButton("Doc No Wise",true),3,3,1,1.5,this,'L');
			add(rdbPRTWS=new JRadioButton("Vendor Wise"),3,5,1,2,this,'L');
			
			btgRPTTP.add(rdbDOCWS);
			btgRPTTP.add(rdbPRTWS);
						
			add(new JLabel("From Date"),4,3,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),4,4,1,1,this,'L');
			add(new JLabel("To Date"),5,3,1,1,this,'L');
			add(txtTODAT = new TxtDate(),5,4,1,1,this,'L');
			add(new JLabel("Vendor"),6,3,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),6,4,1,1,this,'L');
            add(txtVENNM = new TxtLimit(45),6,5,1,3,this,'L');
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error In Constructor");
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					txtFMDAT.requestFocus();
					if((txtFMDAT.getText().trim().length()==0) ||(txtTODAT.getText().trim().length()==0))
					{
						txtTODAT.setText(cl_dat.M_strLOGDT_pbst);
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
						M_calLOCAL.add(java.util.Calendar.DATE,-1);
       					txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
						setMSG("Please enter date to specify date range to generate Report..",'N');
					}
					setENBL(true);	
					txtVENNM.setEnabled(false);			
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
			{
				strGRNNO = "";
				intGRNCT = 0;
				cl_dat.M_PAGENO = 0;
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
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT )
			{
				if(txtFMDAT.getText().length() == 10)
				{
					txtTODAT.requestFocus();
					setMSG("Please Enter Date to generate the Report..",'N');
				}				
			}
			if(M_objSOURC == txtTODAT )
			{
				if(txtTODAT.getText().trim().length() == 10)
				    txtVENCD.requestFocus();
   			}	
			if(M_objSOURC == txtVENCD )
			{
			    try
			    {
                    if(txtVENCD.getText().length() ==5)
                    {
                        M_strSQLQRY = "SELECT PT_PRTNM from CO_PTMST WHERE PT_PRTTP ='S' "
                                    + " AND PT_PRTCD ='"+txtVENCD.getText().trim()+"'";
                        M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
                        if(M_rstRSSET !=null)
                        if(M_rstRSSET.next())
                        {
                            txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
                            cl_dat.M_btnSAVE_pbst.requestFocus();
                        }
                        else setMSG("Invalid Vendor Code..",'E');
                    }
			    }
			    catch(Exception L_E)
			    {
			        setMSG(L_E,"VK_ENTER");
			    }
			    
			}		
		}
		if(L_KE.getKeyCode()== L_KE.VK_F1)
		{
			if(M_objSOURC == txtVENCD)
			{
				M_strHLPFLD = "txtVENCD";
				M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE PT_PRTTP = 'S'";
				if(txtVENCD.getText().length()>0)
					M_strSQLQRY += " AND PT_PRTCD LIKE '"+txtVENCD.getText().toUpperCase()+"%' ";
				M_strSQLQRY += " ORDER BY PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Code","Description"},2,"CT");
			}
		}
	}
	public void exeHLPOK()
	{
    	cl_dat.M_flgHELPFL_pbst = false;
    	super.exeHLPOK();			
    	if(M_strHLPFLD.equals("txtVENCD"))
    	{				
    		txtVENCD.setText(cl_dat.M_strHLPSTR_pbst);
    		txtVENNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
    	}
	}
	/**
	* Mathod to check the validity of the inputs given before execuation of the SQL Query.
	*/
	public boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtFMDAT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date Should Not Be Grater Than Today..",'E');
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
				setMSG("Date Should Not Be Grater Than Today..",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(M_fmtLCDAT.parse(txtTODAT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtFMDAT.getText().trim()))<0)
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
			setMSG(L_EX,"Error In vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to generate the Report File & to forward it to selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rprej.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rprej.doc";			
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"List Of Rejected GRIN"," ");
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
	 * Method to fetch data from the database & to club it with Header & footer.
	 */	
	public void getDATA()
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
		try
		{			
			boolean flgPRINT = false;
			intRECCT = 0;
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Generating Report..Wait For Some Time...",'N');
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI12);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>List Of Rejected GRIN</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}		
			
			prnHEADER();
			
			M_strSQLQRY = "SELECT GR_GRNNO,GR_ACPDT,GR_VENCD,GR_VENNM,GR_ACPQT,GR_REJQT,"
				+"GR_PORNO,GR_MATCD,CT_MATDS,CT_UOMCD "
				+"FROM MM_GRMST,CO_CTMST WHERE CT_MATCD = GR_MATCD AND "
				+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
				+"GR_REJQT > 0 AND GR_ACPDT BETWEEN '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
				+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' AND "
				+"isnull(GR_STSFL,' ') <> 'X' ";
			if(txtVENCD.getText().trim().length() >0)
			    M_strSQLQRY += " AND GR_VENCD ='"+txtVENCD.getText().trim()+"' ";	
		    if(rdbDOCWS.isSelected())
		        M_strSQLQRY +=	" ORDER BY GR_GRNNO,GR_MATCD ";
		    else if(rdbPRTWS.isSelected())
		       M_strSQLQRY +=	" ORDER BY GR_VENCD,GR_GRNNO,GR_MATCD ";
		    
		        
						
			M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);							
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst > 63)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					dosREPORT.writeBytes("\n");cl_dat.M_intLINNO_pbst++;
					if(!strGRNNO.equals(nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"")))
					{
						String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE "
							+"RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
							+"RM_TRNTP = 'GR' AND RM_DOCTP = 'GR' AND "
							+"RM_DOCNO = '"+strGRNNO+"' AND RM_REMTP = 'ACP' AND "
							+"isnull(RM_STSFL,' ') <> 'X'";
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(L_rstRSSET.next())
						{
							dosREPORT.writeBytes("\n"); 
							cl_dat.M_intLINNO_pbst++;
							String L_strREMDS = nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),"");
							dosREPORT.writeBytes("Remark : ");
							if(L_strREMDS.length() > 87)
							{
								dosREPORT.writeBytes(L_strREMDS.substring(0,87));
								dosREPORT.writeBytes("\n");
								cl_dat.M_intLINNO_pbst++;
								dosREPORT.writeBytes(L_strREMDS.substring(87,L_strREMDS.length()));
							}
							else
								dosREPORT.writeBytes(L_strREMDS);	
							dosREPORT.writeBytes("\n\n");
							cl_dat.M_intLINNO_pbst += 3;
						}
						L_rstRSSET.close();
							
						flgPRINT = true;
						strGRNNO = nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),"");
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),11));
							
						java.util.Date jdtTEMP = M_rstRSSET.getDate("GR_ACPDT");
						if(jdtTEMP != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(jdtTEMP),11));
						else
							dosREPORT.writeBytes(padSTRING('R',"",11));
							
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""),6));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),41));
						intGRNCT++;
					}
					dosREPORT.writeBytes("\n");
					if(flgPRINT)
					{
						flgPRINT = false;
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),22));
					}
					else
						dosREPORT.writeBytes(padSTRING('R',"",22));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),30));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),3));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0.0"),21));
					dosREPORT.writeBytes(padSTRING('L',nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0.0"),20));
					dosREPORT.writeBytes("\n");
					dosREPORT.writeBytes(padSTRING('R',"",22));
					dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"0.0"),60));
					cl_dat.M_intLINNO_pbst += 2;
				}
				M_rstRSSET.close();
				String L_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE "
					+"RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"RM_TRNTP = 'GR' AND RM_DOCTP = 'GR' AND "
					+"RM_DOCNO = '"+strGRNNO+"' AND RM_REMTP = 'ACP' AND "
					+"isnull(RM_STSFL,' ') <> 'X'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET != null)
				{
					if(L_rstRSSET.next())
					{
						dosREPORT.writeBytes("\n");
						dosREPORT.writeBytes("Remark : "+nvlSTRVL(L_rstRSSET.getString("RM_REMDS"),""));
					}
					L_rstRSSET.close();
				}
			}						
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");
			dosREPORT.writeBytes("Total GRIN : "+intGRNCT);
			setMSG("Report generation Completed..",'N');
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);
			}
			if(M_rdbHTML.isSelected())
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");
			dosREPORT.close();
			fosREPORT.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Method to generate the header part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO ++;
			dosREPORT.writeBytes("\n"+cl_dat.M_strCMPNM_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"LIST OF GRIN Rejection From : "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst +"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");			
			dosREPORT.writeBytes(strDOTLN+"\n");
			dosREPORT.writeBytes("GRIN No.   Accepted   Vendor Code And Name"+"\n");			
			dosREPORT.writeBytes("P.O. No.   Date       Material Code.                UOM        Accepted Qty.       Rejected Qty."+"\n");			
			dosREPORT.writeBytes("                      Description"+"\n");			
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst = 8;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
}