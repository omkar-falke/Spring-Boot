/*
System Name		: Materials Management System
Program Name	: List Of GRIN Pending For Valuation
Author			: Mr S.R.Mehesare
Modified Date	: 10/10/2005
Documented Date	: 
Version			: v2.0.0
*/

import java.sql.ResultSet;import java.util.Date;
import javax.swing.JTextField;import javax.swing.JLabel;
import java.io.FileOutputStream;import java.io.DataOutputStream;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;import javax.swing.JCheckBox;
/**<pre>
System Name : Material Management System.
 
Program Name : List Of GRIN Pending For Valuation

Purpose : This program generates Report for List Of Modvatable Issue for given date Range.

List of tables used :
Table Name     Primary key                                 Operation done
                                                   Insert   Update   Query   Delete	
--------------------------------------------------------------------------------------
MM_GRMST       GR_STRTP,GR_GRNTP,GR_GRNNO,GR_MATCD,GR_BATNO            #
CO_CTMST       CT_GRPCD,CT_CODTP,CT_MATCD                              #
--------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name  Column Name    Table name    Type/Size     Description
--------------------------------------------------------------------------------------
txtFMDAT   GR_ACPDT        MM_GRMST      Date          Accepted Date
txtTODAT   GR_ACPDT        MM_GRMST      Date          Accepted Date
--------------------------------------------------------------------------------------
<I>
<B>Query : </B>
    For Summary Data is taken from MM_GRMST for condiations :-
      1) GR_STRTP = Selected Store Type
      2) AND GR_ACPDT is not null
      3) AND ifnull(GR_BILVL,0) = 0 
      4) AND ifnull(GR_ACPQT,0) > 0 
      5) AND GR_ACPDT Between given date range.
      6) AND ifnull(GR_STSFL,' ') <> 'X' ";
     if Imported Item Details requried
      7) AND SUBSTR(gr_matcd,10,1) IN('2','6')

   For Details Data is taken from MM_GRMST & CO_CTMST for condiations 
     1) CT_MATCD = GR_MATCD 
     2) AND GR_STRTP = Selected Store Type
     3) AND GR_ACPDT is not null 
     4) AND ifnull(GR_BILVL,0) = 0 
     5) AND ifnull(GR_ACPQT,0) > 0 
     6) AND GR_ACPDT Between given Date range
     7) AND ifnull(GR_STSFL,' ') <> 'X'
    if Imported Item Details requried
     8) AND SUBSTR(gr_matcd,10,1) IN('2','6')

<B>Validations & Other Information:</B>
    - To date must be greater than from date & smaller than current date.
</I> */
class mm_rpgpv extends cl_rbase
{
										/** JTextField to display & to enter From date to specify date range.*/
	private JTextField txtFMDAT;		/** JTextField to display & to enter To date to specify date range.*/
	private JTextField txtTODAT;		/** JRadioButton to specify that summary  report for GRIN painding for valuation.*/
	private JRadioButton rdbSUMRY;		/** JRadioButton to specify that Details Report for GRIN painding for valuation.*/
	private JRadioButton rdbDETIL;		/** ButtonGroup to binding JRedioButton together.*/
	private ButtonGroup btgRPTOP;       /** JCheckBox to specify that, only Imported items should include in the Report.*/
	private JCheckBox chbIMPIT;			/** String varaible for generated report file name.*/	
	private String strFILNM;			/** Integer variable to count the total number of GRIN.*/	
	private int intGRNCT;				/** Integer variable to count the number of records fetched, to block the Report if no data found.*/	
	private int intRECCT;				/** double varaible to calculate the total amount of the GRINs.*/	
	private double dblTOTVL =0.00;		/** FileOutputStream Object to generate the Report file from the stream of data.*/		
	private FileOutputStream fosREPORT;	/** DataOutputStream to hold the Stream of data to generate the report file.*/
	private DataOutputStream dosREPORT;	/** String varaible for dotted Line to print in the Report.*/	
	private String strDOTLN = "-------------------------------------------------------------------------------------------------";
	
	public mm_rpgpv()
	{
		super(2);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);			
			
			btgRPTOP = new ButtonGroup();
			rdbSUMRY = new JRadioButton("Summary",true);
			rdbDETIL = new JRadioButton("Detail",false);
			
			btgRPTOP.add(rdbSUMRY);
			btgRPTOP.add(rdbDETIL);			
			setMatrix(20,8);
			
			add(new JLabel("List Of Pending GRIN For Valuation "),2,4,1,2.5,this,'L');
			add(new JLabel("From Date"),3,4,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),3,5,1,1,this,'L');
			add(new JLabel("To Date"),4,4,1,1,this,'L');
			add(txtTODAT = new TxtDate(),4,5,1,1,this,'L');
			
			add(rdbSUMRY,5,4,1,1,this,'L');
			add(rdbDETIL,5,5,1,1,this,'L');		
			add(chbIMPIT = new JCheckBox("Imported Items"),6,4,1,2,this,'L');
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
				}
				else
					setENBL(false);
			}		
			else if(M_objSOURC == cl_dat.M_btnSAVE_pbst)
			{
				intGRNCT = 0;
				cl_dat.M_PAGENO=0;
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
					setMSG("Please Enter Date to generate the Reort..",'N');
				}
				else
				setMSG("From Date Cannot be Blank..",'N');
			}
			if(M_objSOURC == txtTODAT )
			{
				if(txtTODAT.getText().length() == 10)				
					cl_dat.M_btnSAVE_pbst.requestFocus();				
				else
					setMSG("From Date Cannot be Blank..",'N');
			}
			if(M_objSOURC == rdbDETIL )			
				cl_dat.M_btnSAVE_pbst.requestFocus();			
		}
	}	
	/**
	 * Method to validate the Inputs before execuation of the query.
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
	 * Method to generate the Report File & to send at selected Destination.
	 */
	void exePRINT()
	{
		if (!vldDATA())
			return;
		try
		{
		    if(M_rdbHTML.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst +"mm_rpgpv.html";
		    else if(M_rdbTEXT.isSelected())			
		        strFILNM = cl_dat.M_strREPSTR_pbst + "mm_rpgpv.doc";			
			
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
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"List Of GRIN Pending For Valuation"," ");
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
	 * Method to fetch data from the database & to Club it with header & footer.
	 */
	public void getDATA()
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
		setMSG("Generating Report..",'N');
		String L_strPOVAL = "";
		String L_strGRNNO = "";
		java.util.Date tmpDATE;
		double dblTOTVL =0;
		try
		{			
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
			    dosREPORT.writeBytes("<HTML><HEAD><Title>List Of GRIN Pending For Valuation</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>");				
			}
			prnHEADER();
			intRECCT = 0;
			if(rdbSUMRY.isSelected())
			{
				M_strSQLQRY = "SELECT GR_GRNNO,GR_ACPDT,GR_VENNM,GR_PORNO,sum(isnull(GR_PORVL,0)) L_POVAL FROM MM_GRMST WHERE "
					+"GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND GR_ACPDT is not null "
					+"AND isnull(GR_BILVL,0) = 0 AND isnull(GR_ACPQT,0) > 0 AND GR_ACPDT BETWEEN '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' AND isnull(GR_STSFL,' ') <> 'X' ";
				if(chbIMPIT.isSelected())
					M_strSQLQRY += " AND SUBSTRING(gr_matcd,10,1) IN('2','6') ";
				M_strSQLQRY += " GROUP BY GR_GRNNO,GR_ACPDT,GR_VENNM,GR_PORNO ORDER BY GR_GRNNO,GR_ACPDT ";
			}
			if(rdbDETIL.isSelected())
			{
				M_strSQLQRY = "SELECT GR_GRNNO,GR_MATCD,GR_ACPDT,GR_VENNM,GR_PORNO,GR_PORVL,CT_MATDS "
					+"FROM MM_GRMST,CO_CTMST WHERE CT_MATCD = GR_MATCD AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '"+M_strSBSCD.substring(2,4)+"' AND "
					+"GR_ACPDT is not null AND isnull(GR_BILVL,0) = 0 AND "
					+"isnull(GR_ACPQT,0) > 0 AND GR_ACPDT BETWEEN '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtFMDAT.getText()))+"' AND '"
					+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtTODAT.getText()))+"' AND "
					+"isnull(GR_STSFL,' ') <> 'X' ";
				if(chbIMPIT.isSelected())
					M_strSQLQRY += " AND SUBSTRING(gr_matcd,10,1) IN('2','6') ";
				M_strSQLQRY +="ORDER BY GR_GRNNO,GR_ACPDT ";
			}										
			M_rstRSSET = cl_dat.exeSQLQRY0(M_strSQLQRY);			
			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					intRECCT = 1;
					if(cl_dat.M_intLINNO_pbst >60)
					{
						dosREPORT.writeBytes("\n"+strDOTLN);
						if(M_rdbHTML.isSelected())
							dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
						if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
							prnFMTCHR(dosREPORT,M_strEJT);
						prnHEADER();
					}
					if(rdbSUMRY.isSelected())
					{
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst ++;
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),11));
									
						tmpDATE = M_rstRSSET.getDate("GR_ACPDT");
						if(tmpDATE != null)
							dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(tmpDATE),14));
						else
							dosREPORT.writeBytes(padSTRING('R',"",14));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),10));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),45));
						
						L_strPOVAL = nvlSTRVL(M_rstRSSET.getString("L_POVAL"),"0.00");
						dosREPORT.writeBytes(padSTRING('L',L_strPOVAL,15));
						dblTOTVL += Double.parseDouble(L_strPOVAL);
					}
					if(rdbDETIL.isSelected())
					{
						if(!L_strGRNNO.equals(M_rstRSSET.getString("GR_GRNNO")))
						{
							dosREPORT.writeBytes("\n\n");
							cl_dat.M_intLINNO_pbst += 2;
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_GRNNO"),""),11));
							tmpDATE = M_rstRSSET.getDate("GR_ACPDT");
							if(tmpDATE != null)
								dosREPORT.writeBytes(padSTRING('R',M_fmtLCDAT.format(tmpDATE),14));
							else
								dosREPORT.writeBytes(padSTRING('R',"",14));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),10));
							dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""),45));
							L_strGRNNO = M_rstRSSET.getString("GR_GRNNO");
						}
						dosREPORT.writeBytes("\n");
						cl_dat.M_intLINNO_pbst ++;
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),""),11));
						dosREPORT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),69));
						L_strPOVAL = nvlSTRVL(M_rstRSSET.getString("GR_PORVL"),"0.0");
						dosREPORT.writeBytes(padSTRING('L',L_strPOVAL,15));
						dblTOTVL += Double.parseDouble(L_strPOVAL);
					}
					intGRNCT++;										
				}
			}				
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");
			cl_dat.M_intLINNO_pbst ++;
			if(rdbDETIL.isSelected())
				dosREPORT.writeBytes("Total Items: "+intGRNCT +"                                                      Total P.O.Value : "+setNumberFormat(dblTOTVL,2));
			else
				dosREPORT.writeBytes("Total GRIN : "+intGRNCT +"                                                      Total P.O.Value : "+setNumberFormat(dblTOTVL,2));
			dosREPORT.writeBytes("\n"+ strDOTLN +"\n");			
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
		setMSG("",'N');
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
	/**
	 * Method to generate the header part of the Report.
	 */
	private void prnHEADER()
	{
		try
		{
			cl_dat.M_PAGENO++;
			dosREPORT.writeBytes("\n"+padSTRING('R',cl_dat.M_strCMPNM_pbst,96)+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"LIST OF GRIN Pending For Valuation Prepared From : "+txtFMDAT.getText().trim()+" To "+txtTODAT.getText().trim(),strDOTLN.length()-21));						
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst+"\n");			
			dosREPORT.writeBytes(padSTRING('R',"Stores Type : "+cl_dat.M_cmbSBSL2_pbst.getSelectedItem().toString(),strDOTLN.length()-21));
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");			
			dosREPORT.writeBytes(strDOTLN +"\n");			
			dosREPORT.writeBytes("GRIN No.   Accepted Dt.  P.O. No.  Supplier Name                                     P.O. Value");
			if(rdbDETIL.isSelected())
				dosREPORT.writeBytes("\nMaterial Code And Description                                               ");
			dosREPORT.writeBytes("\n");
			dosREPORT.writeBytes(strDOTLN);
			cl_dat.M_intLINNO_pbst = 7;
		}
		catch(Exception L_IO)
		{
			setMSG(L_IO,"getRPHDR");
		}
	}
}