/**
System Name   : Human Resource Management System
Program Name  : Release interview Details.
Program Desc. : Application to enter Release Interview Details of the Employee.
Author        : Mr.S.R. Mehesare
Date          : 
Version       : HRMS V2.0.0

Modificaitons
Modified By    : 
Modified Date  : 
Modified det.  : 
Version        : HRMS 2.0
*/

import javax.swing.JLabel;import javax.swing.JTextField;import javax.swing.JComboBox;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.Hashtable;
 
class hr_teeld extends cl_pbase 
{
	private JTextField txtEMPNO;
	private JTextField txtSHRNM;
	private JTextField txtFULNM;
	private JTextField txtDPTNM;
	private JTextField txtDESGN;
	private JTextField txtJONDT;
	private JTextField txtCRFDT;
	private JTextField txtLFTDT;
	private JTextArea txaHRDRM;
	private JTextArea txaEMPRM;
	private JComboBox cmbREASN;
	
	String strHRRMK="";
	String strOHRRMK="";
	String strERRMK="";
	String strOERRMK="";
	private Hashtable<String,String> hstREASN;
	hr_teeld()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Emp No / Initial"),3,2,1,1,this,'L');
			add(txtEMPNO = new TxtNumLimit(4),3,3,1,1,this,'L');
			add(txtSHRNM = new TxtLimit(3),3,4,1,.5,this,'L');
			add(new JLabel("Dept."),3,4,1,.4,this,'R');
			add(txtDPTNM = new TxtNumLimit(4),3,5,1,1,this,'L');
			add(new JLabel("Designation"),3,6,1,1,this,'L');
			add(txtDESGN = new TxtNumLimit(4),3,7,1,1,this,'L');	
			
			add(new JLabel("Name"),4,2,1,1,this,'L');		
			add(txtFULNM = new JTextField(),4,3,1,5,this,'L');
			
			add(new JLabel("Joining Date"),5,2,1,1,this,'L');
			add(txtJONDT = new TxtDate(),5,3,1,1,this,'L');
			add(new JLabel("Confirmed On"),5,4,1,1,this,'L');
			add(txtCRFDT = new TxtDate(),5,5,1,1.5,this,'L');
					
			add(new JLabel("Leaving Date"),6,2,1,1,this,'L');
			add(txtLFTDT = new TxtDate(),6,3,1,1,this,'L');
			add(new JLabel("Reason"),6,4,1,1,this,'L');
			add(cmbREASN = new JComboBox(),6,5,1,1.5,this,'L');
			
			txaEMPRM = new TxtAreaLimit(500);
			txaHRDRM = new TxtAreaLimit(500);
			add(new JLabel("HRD Remarks"),7,2,1,2,this,'L');
			add(new JScrollPane(txaHRDRM,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),8,2,4,6,this,'L');
			txaHRDRM.setLineWrap(true);
			add(new JLabel("Employee Recommendations"),12,2,1,2,this,'L');
			add(new JScrollPane(txaEMPRM,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),13,2,4,6,this,'L');
			txaEMPRM.setLineWrap(true);
			String L_strCODCD="",L_strCODDS="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS FROM CO_CDTRN"
				+" where CMT_CGMTP = 'SYS'"
				+" AND CMT_CGSTP = 'HRXXROL'"
				+" AND isnull(CMT_STSFL,'')<>'X'";		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			hstREASN = new Hashtable<String,String>();
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					hstREASN.put(L_strCODCD,L_strCODDS);
					cmbREASN.addItem(L_strCODCD+" "+L_strCODDS);
				}
				M_rstRSSET.close();
			}
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super Class Method Overrided to enable & disable the Components
	 * P_flgSTATE boolean argument ot pass state of the component
	 */
	void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(P_flgSTATE);		
		if(P_flgSTATE == true)
		{
			txtFULNM.setEnabled(false);
			txtDPTNM.setEnabled(false);
			txtDESGN.setEnabled(false);
			txtJONDT.setEnabled(false);
			txtCRFDT.setEnabled(false);			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				txtEMPNO.setEnabled(true);
				txtSHRNM.setEnabled(true);
				txaEMPRM.setEnabled(true);
				txaHRDRM.setEnabled(true);
				txtLFTDT.setEnabled(true);
			}
			else
			{
				txaEMPRM.setEnabled(false);
				txaHRDRM.setEnabled(false);
				txtLFTDT.setEnabled(false);
			}
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
					setENBL(true);					
					txtEMPNO.requestFocus();
					setMSG("Please Enter Employee number..",'N');
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == txtEMPNO)
			{
				if(txtEMPNO.getText().trim().length() == 4)
				{
					java.sql.Date L_tmpDATE;
					String L_strTEMP = "";
					M_strSQLQRY = "Select EP_FULNM,EP_SHRNM,EP_EMPNO,EP_DESGN,EP_JONDT,EP_LFTDT,EP_CRFDT,EP_DPTNM,EP_RSNCD FROM HR_EPMST"
						+" where ";
					M_strSQLQRY += "EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '"+ txtEMPNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					String L_strEMPNO = txtEMPNO.getText().trim();
					clrCOMP();
					txtEMPNO.setText(L_strEMPNO);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("EP_FULNM"),"");
							L_strTEMP = L_strTEMP.replace('|',' ');
							txtFULNM.setText(L_strTEMP);
							txtEMPNO.setText(nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),""));
							txtSHRNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_SHRNM"),""));
							txtDESGN.setText(nvlSTRVL(M_rstRSSET.getString("EP_DESGN"),""));
							txtDPTNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""));
							
							L_tmpDATE = M_rstRSSET.getDate("EP_JONDT");
							if(L_tmpDATE != null)
								txtJONDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							else 
								txtJONDT.setText("");
							
							L_tmpDATE = M_rstRSSET.getDate("EP_CRFDT");
							if(L_tmpDATE != null)
								txtCRFDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							else 
								txtCRFDT.setText("");
							
							L_tmpDATE = M_rstRSSET.getDate("EP_LFTDT");
							if(L_tmpDATE != null)
								txtLFTDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							else
								txtLFTDT.setText("");
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("EP_RSNCD"),"");
							if(hstREASN.containsKey(L_strTEMP))
								cmbREASN.setSelectedItem(L_strTEMP+" "+hstREASN.get(L_strTEMP));
						}
						M_rstRSSET.close();
					}
					
					M_strSQLQRY = "Select RM_TRNCD,RM_REMDS FROM HR_RMMST"
					+" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_DOCNO = '"+ txtEMPNO.getText().trim()+"'"					
					+" ANd RM_TRNTP = 'LE'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					strOHRRMK = "";
					strOERRMK = "";
					if(M_rstRSSET != null)
					{
						String L_strTRNCD = "";
						while(M_rstRSSET.next())
						{
							L_strTRNCD = nvlSTRVL(M_rstRSSET.getString("RM_TRNCD"),"");
							if(L_strTRNCD.equals("HR"))
							{
								strOHRRMK = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
								txaHRDRM.setText(strOHRRMK);
							}
							else if(L_strTRNCD.equals("ER"))
							{
								strOERRMK = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
								txaEMPRM.setText(strOERRMK);
							}
						}
						M_rstRSSET.close();
					}
				}
				else
					setMSG("Please Enter valid Initial of the Employee..",'E');
			}
			else if(M_objSOURC == txtSHRNM)
			{
				if((txtSHRNM.getText().trim().length() == 2) || (txtSHRNM.getText().trim().length() == 3))
				{
					java.sql.Date L_tmpDATE;
					String L_strTEMP = "";
					M_strSQLQRY = "Select EP_FULNM,EP_EMPNO,EP_DESGN,EP_JONDT,EP_LFTDT,EP_CRFDT,EP_DPTNM,EP_RSNCD FROM HR_EPMST"
						+" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ";
					if(txtEMPNO.getText().length() ==0)
						M_strSQLQRY += "EP_SHRNM = '"+ txtSHRNM.getText().trim().toUpperCase()+"'";
					else
						M_strSQLQRY += "EP_EMPNO = '"+ txtEMPNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					clrCOMP();
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("EP_FULNM"),"");
							L_strTEMP = L_strTEMP.replace('|',' ');
							txtFULNM.setText(L_strTEMP);
							txtEMPNO.setText(nvlSTRVL(M_rstRSSET.getString("EP_EMPNO"),""));
							txtDESGN.setText(nvlSTRVL(M_rstRSSET.getString("EP_DESGN"),""));
							txtDPTNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_DPTNM"),""));
							
							L_tmpDATE = M_rstRSSET.getDate("EP_JONDT");
							if(L_tmpDATE != null)
								txtJONDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							else 
								txtJONDT.setText("");
							
							L_tmpDATE = M_rstRSSET.getDate("EP_CRFDT");
							if(L_tmpDATE != null)
								txtCRFDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							else 
								txtCRFDT.setText("");
							
							L_tmpDATE = M_rstRSSET.getDate("EP_LFTDT");
							if(L_tmpDATE != null)
								txtLFTDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							else
								txtLFTDT.setText("");
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("EP_RSNCD"),"");
							if(hstREASN.containsKey(L_strTEMP))
								cmbREASN.setSelectedItem(L_strTEMP+" "+hstREASN.get(L_strTEMP));
						}
						M_rstRSSET.close();
					}
					
					M_strSQLQRY = "Select RM_TRNCD,RM_REMDS FROM HR_RMMST"
					+" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_DOCNO = '"+ txtEMPNO.getText().trim()+"'"					
					+" ANd RM_TRNTP = 'LE'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					strOHRRMK = "";
					strOERRMK = "";
					if(M_rstRSSET != null)
					{
						String L_strTRNCD = "";
						while(M_rstRSSET.next())
						{
							L_strTRNCD = nvlSTRVL(M_rstRSSET.getString("RM_TRNCD"),"");
							if(L_strTRNCD.equals("HR"))
							{
								strOHRRMK = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
								txaHRDRM.setText(strOHRRMK);
							}
							else if(L_strTRNCD.equals("ER"))
							{
								strOERRMK = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
								txaEMPRM.setText(strOERRMK);
							}
						}
						M_rstRSSET.close();
					}
				}
				else
					setMSG("Please Enter valid Initial of the Employee..",'E');
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
			try
			{
				if(M_objSOURC == txtEMPNO)
				{
					M_strHLPFLD = "txtEMPNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
						M_strSQLQRY =" SELECT EP_EMPNO,EP_SHRNM,EP_FULNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(EP_STSFL,'')<>'X' ";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						 M_strSQLQRY += " AND EP_LFTDT is null ";
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
						M_strSQLQRY += " AND EP_LFTDT is not null";	
					if(txtEMPNO.getText().length() >0)
						M_strSQLQRY += " AND EP_EMPNO like '"+txtEMPNO.getText().trim()+"%'";
					M_strSQLQRY += " order By EP_FULNM";
					cl_hlp(M_strSQLQRY,3,1,new String[]{"Employee No","Initial","Name"},3,"CT");
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
			if(M_objSOURC == txtEMPNO)			
			{
				if(txtEMPNO.getText().trim().length() ==0)
					txtSHRNM.requestFocus();
				else
					txtLFTDT.requestFocus();
				setMSG("Please Enter Employee initial or Press F1 to select from List..",'N');
			}
			else if(M_objSOURC == txtSHRNM)			
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					txtLFTDT.requestFocus();
					setMSG("Please Enter Employee Initial or press F1 to select from List..",'N');
				}
			}	
			else if(M_objSOURC == txtLFTDT)			
			{
					cmbREASN.requestFocus();
					setMSG("Please select Reason of leaving from the List..",'N');
			}	
			else if(M_objSOURC == cmbREASN)			
			{
					txaHRDRM.requestFocus();
					setMSG("Please select Reason of leaving from the List..",'N');
			}	
			else if(M_objSOURC == txaHRDRM)			
			{
					txaEMPRM.requestFocus();
					setMSG("Please select Reason of leaving from the List..",'N');
			}	
			else if(M_objSOURC == txaEMPRM)			
			{
					cl_dat.M_btnSAVE_pbst.requestFocus();
					setMSG("Click on Chnage Button to Save the changes..",'N');
			}	
		}	
	}
	/**
	 * Super class Method overrrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{		
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtEMPNO"))
		{			
			cl_dat.M_flgHELPFL_pbst = false;			
			txtEMPNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtSHRNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			txtFULNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));
		}		
	}
	/**
	 * Method to validate the Data before Execuation of the SQL query.
	 */
	boolean vldDATA()
	{
		try
		{
			if(txtEMPNO.getText().trim().length()==0)
			{
				setMSG("Please Enter Employee Number..",'E');
				txtEMPNO.requestFocus();
				return false;
			}
			else if(txtLFTDT.getText().trim().length()==0)
			{
				setMSG("Please Enter release Date..",'E');
				txtLFTDT.requestFocus();
				return false;
			}
			if (M_fmtLCDAT.parse(txtLFTDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))>0)			 
			{					
				txtLFTDT.requestFocus();
				setMSG("Release Date Must be smaller than current Date..",'E');
				return false;
			}
			if(txaHRDRM.getText().length() > 500)
			{
				txaHRDRM.requestFocus();
				setMSG("HRD Remarks can be upto 500 chars,current length : "+txaHRDRM.getText().length(),'E');
				return false;
			}
			if(txaEMPRM.getText().length() > 500)
			{
				txaEMPRM.requestFocus();
				setMSG("Employee Remarks can be upto 500 chars,current length : "+txaEMPRM.getText().length(),'E');
				return false;
			}
			txaHRDRM.setText(txaHRDRM.getText().replace("'","`"));
			txaEMPRM.setText(txaEMPRM.getText().replace("'","`"));
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}
	
	/**
	 * Super class method overrided here to inhance its functionality, to perform 
	 * Database operations.
	 */
	void exeSAVE()
	{					
		try
		{
			if(!vldDATA())			
				return ;			
			else
				setMSG("",'E');	
			
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;					
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "update HR_EPMST set EP_LFTDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(txtLFTDT.getText().trim()))+"',"							 
					+" EP_RSNCD = '"+ cmbREASN.getSelectedItem().toString().substring(0,2)+"',"					
					+" EP_STSFL = 'U',"		 
					+" EP_TRNFL = '',"
					+" EP_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',"
					+" EP_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_HRSBS = '"+ M_strSBSCD+"'"				
					+" AND EP_EMPNO = '"+ txtEMPNO.getText().trim() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
				//modREMDS(P_strTRNTP,P_strTRNCD,P_strOREMDS,P_strREMDS)								
				modREMDS("LE","HR",strOHRRMK,txaHRDRM.getText().trim());
				modREMDS("LE","ER",strOERRMK,txaEMPRM.getText().trim());
				M_strSQLQRY = "update SA_USMST set US_STSFL ='D',US_DSBID ='EMPLOYEE LEFT THE ORG.'" ;							 				
				M_strSQLQRY += " WHERE US_EMPCD = '"+ txtEMPNO.getText().trim() +"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");
				if(cl_dat.exeDBCMT("exeSAVE"))
				{				
					clrCOMP();			
					setENBL(true);
					setMSG("Data Updated Successfully..",'N'); 				
				}
				else			
					setMSG("Error in saving details..",'E');
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
	}
	/**
	 * Method for addition & Modification of the Remarks Entered.
	 * @param P_strREMTP String argument to pass the Remark Type.
	 * @param P_strREMDS String argument to pass the new Remark Description.
	 * @param P_strOREMDS String argument to pass the Old Remark Decription
	 * @param P_strUSRCD String argument to pass User Code who has entered the corresponding Ramraks.
	 */
	private void modREMDS(String P_strTRNTP,String P_strTRNCD,String P_strOREMDS,String P_strREMDS)
	{
		try
		{				
			if(!P_strOREMDS.equals(P_strREMDS))
			{
				if(P_strREMDS.length() == 0)
				{					
					M_strSQLQRY = "update HR_RMMST set RM_REMDS = '-',"
					+" RM_LINNO ='1',"
					+" RM_STSFL ='X',"
					+" RM_TRNFL = '',"
					+" RM_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',"
					+" RM_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_SBSCD = '"+ M_strSBSCD+"'"
					+" AND RM_TRNTP = '"+ P_strTRNTP +"'"
					+" AND RM_TRNCD = '"+ P_strTRNCD +"'"
					+" AND RM_DOCNO = '"+ txtEMPNO.getText().trim() +"'";					
				}
				else if(P_strOREMDS.length() == 0) 
				{
					M_strSQLQRY = "Insert into HR_RMMST(RM_CMPCD,RM_SBSCD,RM_TRNTP,RM_TRNCD,RM_DOCNO,RM_REMDS,RM_LINNO)"
					+ " Values ( '"+ cl_dat.M_strCMPCD_pbst +"','"+ M_strSBSCD +"',"
					+"'"+ P_strTRNTP +"',"
					+"'"+ P_strTRNCD +"',"
					+"'"+ txtEMPNO.getText().trim() +"',"
					+"'"+ P_strREMDS +"','1')";					
				}
				else
				{
					M_strSQLQRY = "update HR_RMMST set RM_REMDS ='"+ P_strREMDS +"',"					
					+" RM_STSFL = 'U',"
					+" RM_TRNFL = '',"
					+" RM_LUSBY = '"+ cl_dat.M_strUSRCD_pbst+"',"
					+" RM_LUPDT = '"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = '"+ P_strTRNTP +"'"
					+" AND RM_TRNCD = '"+ P_strTRNCD +"'"
					+" AND RM_DOCNO = '"+ txtEMPNO.getText().trim() +"'"
					+" AND RM_SBSCD = '"+ M_strSBSCD +"'";					
				}
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"modREMDS");
		}
	}
}