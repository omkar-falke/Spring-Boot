/**
System Name   : Human Resource Management System
Program Name  : Employee Transfer Form
Program Desc. : Application to enter Transfer Details of the Employee.
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import java.util.StringTokenizer;
import javax.swing.JTextArea;
import java.util.Hashtable;

/**
 * <PRE>
 * <b>System Name :</b> Human Resource Management System.
 * 
 * <b>Program Name :</b> Employee Transfer Form
 * 
 * <b>Purpose :</b> This module is applicable to enter the Employee Transfer
 * Detials.
 * 
 * List of tables used : Table Name Primary key Operation done Insert Update
 * Query Delete
 * ----------------------------------------------------------------------------------------------------------
 * HR_EPMST EP_EMPNO # #
 * ----------------------------------------------------------------------------------------------------------
 * 
 * List of fields accepted/displayed on screen : Field Name Column Name Table
 * name Type/Size Description
 * ----------------------------------------------------------------------------------------------------------
 * txtEMPNO EP_EMPNO HR_EPMST VARCHAR(4) Employee Number txtFULNM EP_FULNM
 * HR_EPMST VARCHAR(50) Employee Full Name txtSHRNM EP_SHRNM HR_EPMST
 * VARVHAR(15) Employee Initial cmbEPLOC EP_EPLOC HR_EPMST VARCHAR(15) Work
 * Location
 * ----------------------------------------------------------------------------------------------------------
 * 
 * List of fields with help facility : Field Name Display Description Display
 * Columns Table Name
 * ----------------------------------------------------------------------------------------------------------
 * txtEMPNO Employee Number EP_EMPNO HR_EPMST
 * ----------------------------------------------------------------------------------------------------------
 * Query: - New location Code Assigned to the Employee is Updated in the
 * HR_EPMST table for coresponding Employee Number - if Remarks is entered then
 * the Remark is inserted in the HR_RMMSt Table for employee number As Doc
 * Number
 * 
 * Validations : - Employee number entered must be valid. - Location Coded must
 * be Selected to Specify the Transfer.
 */

class hr_teetf extends cl_pbase implements ActionListener 
{									/** JTextField to display & enter the Employee Number.*/ 
	private JTextField txtEMPNO;	/** JTextField to display Short Description.*/ 
	private JTextField txtSHRNM;	/** JTextField to display Employee Full Name.*/ 
	private JTextField txtFULNM;	/** JTextField to display Department Name.*/
	private JTextField txtDPTNM;	/** JTextField to display Designation of the Employee.*/
	private JTextField txtDESGN;	/** JTextField to display Qualification of the Employee.*/
	private JTextField txtQUALN;	/** JTextField to display the Join Date.*/
	private JTextField txtJONDT;	/** JTextField to display Conformation Date.*/
	private JTextField txtCRFDT;	/** JCheckBox to display & to Select the Location  .*/
	private JComboBox cmbEPLOC;		
									/** JTextField to display part of the Temporary Address of the Employee.*/
	private JTextField txtADD1;		/** JTextField to display part of the Temporary Address of the Employee.*/
	private JTextField txtADD2;		/** JTextField to display part of the Temporary Address of the Employee.*/
	private JTextField txtADD3;		/** JTextField to display District Part of the Employee Address.*/
	private JTextField txtDIST;		/** JTextField to display State part from the Employee Address.*/
	private JTextField txtSTATE;	/** JTextField to display Pin Number.*/
	private JTextField txtPINNO;	/** JTextField to display Phone Number.*/
	private JTextField txtPHNNO;	/** JTextField to display Email Address.*/
	private JTextField txtEMAIL;
	private JTextField txtTFRDT;
	private JTextField txtREMDS;
	private JTextField txtREPNO;
	
	String strREMDS;
	String strOREMDS;
	
	Hashtable<String,String> hstEPLOC = new Hashtable<String,String>();
	hr_teetf()
	{
		super(2);
		try
		{			
			setMatrix(20,8);
			add(new JLabel("Emp No/ Initial"),3,2,1,.9,this,'R');
			add(txtEMPNO = new TxtNumLimit(4),3,3,1,.5,this,'L');
			add(txtSHRNM = new TxtLimit(3),3,3,1,.5,this,'R');
			add(new JLabel("Name"),3,4,1,.9,this,'R');		
			add(txtFULNM = new JTextField(),3,5,1,3.5,this,'L');
			
			add(new JLabel("Department"),4,2,1,.9,this,'R');
			add(txtDPTNM = new TxtNumLimit(4),4,3,1,1,this,'L');
			add(new JLabel("Designation"),4,4,1,.9,this,'R');
			add(txtDESGN = new TxtNumLimit(4),4,5,1,1,this,'L');				
			add(new JLabel("Qualification"),4,6,1,.9,this,'R');
			add(txtQUALN = new JTextField(),4,7,1,1,this,'L');
			
			add(new JLabel("Joining Date"),5,2,1,.9,this,'R');
			add(txtJONDT = new TxtDate(),5,3,1,1,this,'L');			
			add(new JLabel("Confirmed On"),5,4,1,.9,this,'R');
			add(txtCRFDT = new TxtDate(),5,5,1,1,this,'L');
			
			add(new JLabel("Transfer To"),6,2,1,.9,this,'R');
			add(cmbEPLOC = new JComboBox(),6,3,1,1,this,'L');
			add(new JLabel("Transfer Date"),6,4,1,.9,this,'R');
			add(txtTFRDT = new TxtDate(),6,5,1,1,this,'L');
			
			add(new JLabel("Address"),7,2,1,.9,this,'R');
			add(txtADD1 = new JTextField(),7,3,1,4.5,this,'L');
			add(txtADD2 = new JTextField(),8,3,1,4.5,this,'L');			
			add(txtADD3 = new JTextField(),9,3,1,4.5,this,'L');
			
			add(new JLabel("District"),10,2,1,.9,this,'R');
			add(txtDIST = new JTextField(),10,3,1,1.3,this,'L');			
			add(new JLabel("State / Pin"),10,4,1,.7,this,'R');
			add(txtSTATE = new JTextField(),10,5,1,1,this,'L');
			add(txtPINNO = new JTextField(),10,6,1,1.3,this,'L');
			
			add(new JLabel("Phone No"),11,2,1,.9,this,'R');
			add(txtPHNNO = new JTextField(),11,3,1,1.3,this,'L');			
			add(new JLabel("E-Mail"),11,4,1,.7,this,'R');
			add(txtEMAIL = new JTextField(),11,5,1,3.5,this,'L');
			
			add(new JLabel("Remark"),12,2,1,.9,this,'R');
			add(txtREMDS = new JTextField(),12,3,1,5,this,'L');
			
			cmbEPLOC.addItem("Select");
			cmbEPLOC.removeActionListener(this);
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,3,6) CODCD,CMT_CODDS,CMT_SHRDS from CO_CDTRN"
				+" where isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_CGMTP = 'SYS'"
				+" AND CMT_CGSTP ='COXXSBS'"
				+" AND CMT_CODCD like 'HR%'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strCODCD="",L_strCODDS="";
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CODCD"),"");
					L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),"");
					hstEPLOC.put(L_strCODDS,L_strCODCD);
					cmbEPLOC.addItem(L_strCODDS);
				}
				M_rstRSSET.close();
			}
			cmbEPLOC.addActionListener(this);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/**
	 * Super class Method inherited to enable & disable the Components.
	 * @param P_flgSTATE boolean variable to pass state for the Components.
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
			txtQUALN.setEnabled(false);
			txtADD1.setEnabled(false);
			txtADD2.setEnabled(false);
			txtADD3.setEnabled(false);
			txtDIST.setEnabled(false);
			txtSTATE.setEnabled(false);
			txtPINNO.setEnabled(false);
			txtPHNNO.setEnabled(false);
			txtEMAIL.setEnabled(false);
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
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						setENBL(true);					
						txtEMPNO.requestFocus();
						setMSG("Please Enter Employee number..",'N');
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						setENBL(false);
						txtEMPNO.setEnabled(true);
						txtSHRNM.setEnabled(true);
						txtEMPNO.requestFocus();
						setMSG("Please Enter Employee number..",'N');
					}
					else
						setENBL(false);
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == txtEMPNO)
			{
				if(txtEMPNO.getText().trim().length() == 4)
				{
					M_strSQLQRY = "Select EP_SHRNM FROM HR_EPMST"
						+" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '"+ txtEMPNO.getText().trim()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())						
							txtSHRNM.setText(nvlSTRVL(M_rstRSSET.getString("EP_SHRNM"),""));
						M_rstRSSET.close();
					}
				}
			}
			else if(M_objSOURC == txtSHRNM)
			{
				if((txtSHRNM.getText().trim().length() == 2) || (txtSHRNM.getText().trim().length() == 3))
				{
					txtSHRNM.setText(txtSHRNM.getText().trim().toUpperCase());						
					java.sql.Date L_tmpDATE;
					String L_strTEMP = "";
					StringTokenizer L_stkTEMP;
					M_strSQLQRY = "Select EP_FULNM,EP_EMPNO,EP_EPLOC,EP_DESGN,EP_JONDT,EP_ADRTP,EP_CRFDT,EP_DPTNM FROM HR_EPMST"
						+" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_SHRNM = '"+ txtSHRNM.getText().trim().toUpperCase()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
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
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("EP_ADRTP"),"");
							L_stkTEMP = new StringTokenizer(L_strTEMP,"|");
							if(L_stkTEMP.hasMoreTokens())
								txtADD1.setText(L_stkTEMP.nextToken());
							if(L_stkTEMP.hasMoreTokens())
								txtADD2.setText(L_stkTEMP.nextToken());
							if(L_stkTEMP.hasMoreTokens())
								txtADD3.setText(L_stkTEMP.nextToken());
							
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("EP_EPLOC"),"");
							if(hstEPLOC.containsKey(L_strTEMP))
								cmbEPLOC.setSelectedItem(L_strTEMP);
							else
								cmbEPLOC.setSelectedItem("Select");
						}
						M_rstRSSET.close();
					}					
					
					M_strSQLQRY = "Select RM_REMDS FROM HR_RMMST"
					+" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_DOCNO = '"+ txtEMPNO.getText().trim()+"'"					
					+" AND RM_TRNTP = 'ET'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);					
					strOREMDS = "";
					if(M_rstRSSET != null)
					{						
						if(M_rstRSSET.next())
						{
							strOREMDS = nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),"");
							txtREMDS.setText(strOREMDS);
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
					
					M_strSQLQRY =" SELECT EP_EMPNO,EP_SHRNM,EP_FULNM from HR_EPMST";
					if(txtEMPNO.getText().length() >0)
						M_strSQLQRY += " where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO like '"+txtEMPNO.getText().trim()+"%'";
					M_strSQLQRY += " order By EP_EMPNO";
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Employee No","Initial","Name"},3,"CT");
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
				txtSHRNM.requestFocus();
				setMSG("Please Enter Employee initial or Press F1 to select from List..",'N');
			}
			else if(M_objSOURC == txtSHRNM)			
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					cmbEPLOC.requestFocus();
					setMSG("Please Select the new location of the Employee..",'N');
				}
			}
			else if(M_objSOURC == cmbEPLOC)
			{
				txtADD1.requestFocus();
				setMSG("Please Enter New Residential Address Of the Employee..",'N');
			}
			else if(M_objSOURC == txtADD1)
			{
				txtADD2.requestFocus();
				setMSG("Please Enter New Residential Address of the Employee..",'N');
			}
			else if(M_objSOURC == txtADD2)
			{
				txtADD3.requestFocus();
				setMSG("Please Enter New Residential Address of the Employee..",'N');
			}
			else if(M_objSOURC == txtADD3)
			{
				txtDIST.requestFocus();
				setMSG("Please Enter the District of the New Residential Address of the Employee..",'N');
			}
			else if(M_objSOURC == txtDIST)
			{
				txtSTATE.requestFocus();
				setMSG("Please Enter the State of the New Residential Address of the Employee..",'N');
			}
			else if(M_objSOURC == txtSTATE)
			{
				txtPINNO.requestFocus();
				setMSG("Please Enter the Pin Number of the New Residential Address of the Employee..",'N');
			}
			else if(M_objSOURC == txtPINNO)
			{
				txtPHNNO.requestFocus();
				setMSG("Please Enter the Phone Number..",'N');
			}
			else if(M_objSOURC == txtPHNNO)
			{
				txtEMAIL.requestFocus();
				setMSG("Please Enter the E-Mail Address of the Employee..",'N');
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
			else if(cmbEPLOC.getSelectedIndex() == 0)
			{
				setMSG("Please Select the Locaion..",'E');
				cmbEPLOC.requestFocus();
				return false;
			}
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
				M_strSQLQRY = "update HR_EPMST set EP_HRSBS = '"+ hstEPLOC.get(cmbEPLOC.getSelectedItem()).toString()+"',"
					+" EP_EPLOC = '"+ cmbEPLOC.getSelectedItem().toString()+"',"
					+" EP_STSFL = 'U',"		 
					+" EP_TRNFL = '',"
					+" EP_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',"
					+" EP_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+" where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO = '"+ txtEMPNO.getText().trim() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
									  
					modREMDS("ET",strOREMDS,txtREMDS.getText().trim());
								
				if(cl_dat.exeDBCMT("exeSAVE"))
				{				
					clrCOMP();
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
	 */
	private void modREMDS(String P_strTRNTP,String P_strOREMDS,String P_strREMDS)
	{
		try
		{				
			if(!P_strOREMDS.equals(P_strREMDS))
			{
				if(P_strREMDS.length() == 0)
				{					
					M_strSQLQRY = "update HR_RMMST set RM_REMDS = '-'"
					+" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = '"+ P_strTRNTP +"'"
					+" AND RM_TRNCD = 'ET'"
					+" AND RM_DOCNO = '"+ txtEMPNO.getText().trim() +"'";					
				}
				else if(P_strOREMDS.length() == 0) 
				{
					M_strSQLQRY = "Insert into HR_RMMST(RM_CMPCD,RM_SBSCD,RM_TRNTP,RM_TRNCD,RM_DOCNO,RM_REMDS,RM_LINNO)"
					+ " Values ( '"+cl_dat.M_strCMPCD_pbst+"','"+ hstEPLOC.get(cmbEPLOC.getSelectedItem()).toString() +"',"
					+"'"+ P_strTRNTP +"',"
					+"'ET',"					
					+"'"+ txtEMPNO.getText().trim() +"',"
					+"'"+ P_strREMDS +"','1')";					
				}
				else
				{
					M_strSQLQRY = "update HR_RMMST set RM_REMDS ='"+ P_strREMDS +"'"
					+" where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_TRNTP = '"+ P_strTRNTP +"'"	
					+" AND RM_TRNCD = 'ET'"
					+" AND RM_DOCNO = '"+ txtEMPNO.getText().trim() +"'";
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