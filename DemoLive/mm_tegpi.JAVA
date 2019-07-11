/*
System Name   : Material Management System
Program Name  : Security Inward For Returnable Gate Pass
Program Desc. : 
Author        : A.T.Chaudhari
Date          : 11/05/2004
Version       : MMS 2.0

Modificaitons 

Modified By   :
Modified Date :
Modified det. :
Version       :

*/
	
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
import java.awt.Color;

class mm_tegpi extends cl_pbase
{
	/**TextFeild For Returnable Gate Pass Number*/
	private JTextField txtRGPNO;/**TextFeild For Returnable Gate Pass Date*/
	private JTextField txtRGPDT;/**TextFeild For Gate In Number*/
	private JTextField txtGINNO;/**TextFeild For Department Code*/	
	private JTextField txtDPTCD;/**TextFeild For Department Name*/
	private JTextField txtDPTNM;/**TextFeild For Vender Code*/
	private JTextField txtVENCD;/**TextFeild For Vender Name*/
	private JTextField txtVENNM;/**TextFeild For Remark*/
	private JTextField txtREMDS;
	
	private cl_JTable tblMATDS;	/**JTable For Material Detail*/
		
	private JLabel lblTBLNM;
	/**String For Store Type*/
	private String strSTRTP;/**String For Vender Code*/
	private String strVENCD;/**String For Vender Name*/
	private String strVENNM;/**String For Department Code*/
	private String strDPTCD;/**String For Gate In number*/
	private String strGINNO;/**String For Transaction Flag*/
	private String strTRNFL;/**String For Status Falg*/
	private String strSTSFL;/**String For last Used By*/
	private String strLUSBY;/**String For Last Updated Date*/
	private String strLUPDT;/**String For Gate Pass Type*/
	private String strMGPTP;/**String For Gate Pass Number*/
	private String strMGPNO;/**String For Material Code*/
	private String strMATCD;
	
	private double dblISSQT;	/**Double for Issue Quantity */
	private double dblTOREC;	/**Double for To Be Received Quantity */
	private double dblRECQT;	/**Double for Quantity Received */
	
	private final int TBL_MATCD = 1;
	private final int TBL_MATDS = 2;
	private final int TBL_UOMCD = 3;
	private final int TBL_ISSQT = 4;
	private final int TBL_TOREC = 5;
	private final int TBL_RECQT = 6;
	private final int TBL_DUEDT = 7;
	
	private PreparedStatement pstmUPDREC;	//Prepare statemet to update Record
		
	public mm_tegpi()
	{
		super(2);
		try
		{
			lblTBLNM = new JLabel("Gate Pass Details");
			lblTBLNM.setForeground(Color.blue);
			
			setMatrix(20,8);
			add(new JLabel("RGP No"),2,1,1,1,this,'L');
			add(txtRGPNO = new TxtNumLimit(8.0),2,2,1,1,this,'L');
			add(new JLabel("RGP Date"),2,3,1,1,this,'L');
			add(txtRGPDT = new JTextField(),2,4,1,1,this,'L');
			add(new JLabel("Gate In No"),2,5,1,1,this,'L');
			add(txtGINNO = new TxtNumLimit(10.0),2,6,1,1,this,'L');
			add(new JLabel("Dept. Code"),2,7,1,1,this,'L');
			add(txtDPTCD = new TxtLimit(10),2,8,1,1,this,'L');
			add(new JLabel("Ven Code"),3,1,1,1,this,'L');
			add(txtVENCD = new TxtNumLimit(3.0),3,2,1,1,this,'L');
			add(txtVENNM = new TxtLimit(40),3,3,1,3,this,'L');
			add(new JLabel("Dpt.Name"),3,6,1,1,this,'L');
			add(txtDPTNM = new TxtDate(),3,7,1,2,this,'L');
			add(lblTBLNM,5,1,1,1.3,this,'L');
			tblMATDS = crtTBLPNL1(this,new String[]{"","Material Code","Material Desc","UOM","Qty Issue","Qty.To Received","Qty Received","Due Date"},40,6,1,8,7.9,new int[]{20,75,300,40,78,78,78,70},new int[]{0});
			add(new JLabel("Remark"),16,1,1,1,this,'L');
			add(txtREMDS = new TxtLimit(60),16,2,1,6,this,'L');
		
			tblMATDS.setInputVerifier(new TBLINPVF());
			txtRGPNO.setInputVerifier(new INPVF());
			txtGINNO.setInputVerifier(new INPVF());
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		
			pstmUPDREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
				"UPDATE MM_GPTRN SET GP_MGPDT = ?,GP_RECQT = isnull(GP_RECQT,0)+?,"
				+"GP_LUSBY = ?,GP_LUPDT = ?,GP_RCPDT = ? "
				+"WHERE GP_STRTP = ? AND GP_MGPTP = ? AND GP_MGPNO = ? AND GP_MATCD = ?");
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)
			{
				setENBL(false);
				txtRGPNO.setEnabled(false);
				setMSG("Select Option..",'N');
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				setENBL(true);
				txtRGPNO.requestFocus();
				setMSG("Enter Returnable Gate Pass No Or press 'F1' For Help..",'N');
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				setENBL(false);
				txtRGPNO.requestFocus();
				setMSG("Enter Returnable Gate Pass No Or press 'F1' For Help..",'N');
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode()== L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtRGPNO)
			{
				getDATA();
			}
		}
		if(L_KE.getKeyCode()== L_KE.VK_F1)
		{
			strSTRTP = M_strSBSCD.substring(2,4);
			if(M_objSOURC == txtRGPNO)
			{
				M_strHLPFLD = "txtRGPNO";
				try
				{
					M_strSQLQRY = "SELECT DISTINCT GP_MGPNO,GP_MGPDT FROM MM_GPTRN WHERE "
						+"GP_STRTP = '"+strSTRTP+"' AND GP_MGPTP = '51' AND "
						+"isnull(GP_STSFL,'') NOT IN('X','C') ORDER BY GP_MGPNO ";
					
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Gate Pass No","Gate Pass Date"},2,"CT");
					else
						setMSG("Record Not Found..",'E');
				}
				catch(SQLException L_SQL){
					setMSG(L_SQL,"F1 For Gate pass Number..");
				}
			}
			if(M_objSOURC == txtGINNO)
			{
				M_strHLPFLD = "txtGINNO";
				try
				{
					M_strSQLQRY = "SELECT DISTINCT WB_DOCNO FROM MM_WBTRN WHERE WB_DOCTP = '02' AND "
						+"WB_DOCRF IS NULL AND isnull(WB_STSFL,'')<> 'X' ORDER BY WB_DOCNO";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
						cl_hlp(M_strSQLQRY,1,1,new String[]{"Gate In Number"},1,"CT");
					else
						setMSG("Recored Not Found..",'E');
				}
				catch(SQLException L_SQL)
				{
					setMSG(L_SQL,"F1 For Gate In Number..");
				}
			}
		}
	}
	public void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtRGPNO")
		{
			txtRGPNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtRGPDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			txtGINNO.requestFocus();
			getDATA();
		}
		if(M_strHLPFLD == "txtGINNO")
		{
			txtGINNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
	}
	/**Method to Display Data In Table
	 */
	public void getDATA()
	{
		tblMATDS.clrTABLE();
		txtGINNO.setText("");
		txtRGPDT.setText("");
		txtDPTCD.setText("");
		txtDPTNM.setText("");
		txtVENCD.setText("");
		txtVENNM.setText("");
		setENBL(false);
		strSTRTP = M_strSBSCD.substring(2,4);
		java.sql.Date jdtTEMP;	//temparory date varaible to store date
		char chrFIRST = 'Y';	//to check for first record only disply in the text fields
		char chrDATA = 'N';		//check flag to disply remark if data disply in table
		int i = 0;				//int i with intial value 0 for row count
		try
		{
			M_strSQLQRY = "SELECT GP_MGPDT,GP_VENCD,GP_VENNM,GP_DUEDT,GP_MATCD,GP_ISSQT,GP_RECQT,GP_DPTCD,"
				+"CT_MATDS,CT_UOMCD,CMT_CODDS "
				+"FROM MM_GPTRN,CO_CTMST,CO_CDTRN WHERE CT_MATCD = GP_MATCD AND "
				+"CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXDPT' AND CMT_CODCD = GP_DPTCD AND "
				+"GP_STRTP = '"+strSTRTP+"' AND GP_MGPTP = '51' AND "
				+"GP_MGPNO = '"+txtRGPNO.getText().trim()+"' AND "
				+"GP_STSFL NOT IN('X','C') AND isnull(CT_STSFL,'') <> 'X' "
				+"ORDER BY GP_MATCD ";
		
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					if(chrFIRST == 'Y')	//if first Recored To Print Only Once
					{	
						chrFIRST = 'N';		//make first flag as false to avoid repainting
						chrDATA = 'Y';		//status falg to disply remark in remark text field
						jdtTEMP = M_rstRSSET.getDate("GP_MGPDT");
						if(jdtTEMP != null)
							txtRGPDT.setText(M_fmtLCDAT.format(jdtTEMP));
						
						txtDPTCD.setText(nvlSTRVL(M_rstRSSET.getString("GP_DPTCD"),""));
						txtDPTNM.setText(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
						txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("GP_VENCD"),""));
						txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("GP_VENNM"),""));
					}
					tblMATDS.setValueAt(nvlSTRVL(M_rstRSSET.getString("GP_MATCD"),""),i,TBL_MATCD);
					tblMATDS.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),i,TBL_MATDS);
					tblMATDS.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),i,TBL_UOMCD);
					tblMATDS.setValueAt(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0"),i,TBL_ISSQT);
					tblMATDS.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0")),3),i,TBL_TOREC);
					tblMATDS.setValueAt(setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0")) - Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0")),3),i,TBL_RECQT);
					jdtTEMP = M_rstRSSET.getDate("GP_DUEDT");
					if(jdtTEMP != null)
						tblMATDS.setValueAt(M_fmtLCDAT.format(jdtTEMP),i,TBL_DUEDT);
					i++;
				}
			}
			if(chrDATA == 'Y')		//if data disply in tabe print remark 
			{
				M_strSQLQRY = "SELECT RM_REMDS FROM MM_RMMST WHERE RM_STRTP = '"+strSTRTP+"' AND "
					+"RM_TRNTP = 'GP' AND RM_DOCTP = 'GP' AND RM_DOCNO = '"+txtRGPNO.getText().trim()+"' AND "
					+"isnull(RM_STSFL,'') <> 'X' ";
				M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				if(M_rstRSSET.next())
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
			}
		}
		catch(SQLException L_SQL)
		{
			setMSG(L_SQL,"getDATA");
		}
	}
	
	public void setENBL(boolean k)
	{
		super.setENBL(k);
		txtRGPNO.setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)){
			tblMATDS.cmpEDITR[0].setEnabled(true);
			tblMATDS.cmpEDITR[6].setEnabled(true);
			txtGINNO.setEnabled(true);
		}
	}
	/**Method to Check data On Clicking Save/Change Button
	 */
	public boolean vldDATA()
	{
		String L_strTBLSL = "N";
		if(txtRGPNO.getText().trim().length() < 8)
		{
			txtRGPNO.requestFocus();
			setMSG("Enter Returnable Gate Pass Number Or Press F1 For Help..",'E');
			return false;
		}
		if(txtGINNO.getText().trim().length() < 8)
		{
			txtGINNO.requestFocus();
			setMSG("Enter Gate In Number Or Press F1 For Help..",'E');
			return false;
		}
		for(int i = 0;i<40;i++)
		{
			if(tblMATDS.getValueAt(i,0).toString().equals("true"))
				L_strTBLSL = "Y";
		}
		if(L_strTBLSL == "N")
		{
			setMSG("Select Or Edit At Least One Row",'E');
			return false;
		}
		return true;
	}
	/**Save Procedure
	 */
	void exeSAVE()
	{
		if(vldDATA())
		{
			boolean L_RETRN = false;	//value return by function by default false
			int L_intROWCT = 0;			//int varialbe to strote row count
			try
			{
				cl_dat.M_flgLCUPD_pbst = true;
				for(int i = 0 ;i<40;i++)
				{
					if(tblMATDS.getValueAt(i,0).toString().equals("true"))		//If value In First Column Is True
					{
						dblRECQT = Double.parseDouble(tblMATDS.getValueAt(i,TBL_RECQT).toString());
						strLUSBY = cl_dat.M_strUSRCD_pbst;		//User name
						strSTRTP = M_strSBSCD.substring(2,4);	//Store Type
						strMGPTP = "51";						//value for the returnable gate pass type
						strMATCD = tblMATDS.getValueAt(i,TBL_MATCD).toString();
						strMGPNO = txtRGPNO.getText().trim().toString();
						
						dblISSQT = Double.parseDouble(tblMATDS.getValueAt(i,TBL_ISSQT).toString());
						dblTOREC = Double.parseDouble(tblMATDS.getValueAt(i,TBL_TOREC).toString());
						//set value for prepare statement 
						/*
						"UPDATE MM_GPTRN SET GP_MGPDT = ?,GP_RECQT = isnull(GP_RECQT,0)+?,"
						+"GP_LUSBY = ?,GP_LUPDT = ?,GP_RCPDT = ? "
						+"WHERE GP_STRTP = ? AND GP_MGPTP = ? AND GP_MGPNO = ? AND GP_MATCD = ?");
						*/
						
						pstmUPDREC.setDate(1,new java.sql.Date(M_fmtLCDAT.parse(txtRGPDT.getText()).getTime()));	//System.out.println(1);
						pstmUPDREC.setDouble(2,dblRECQT);	//System.out.println(6);
						pstmUPDREC.setString(3,strLUSBY);	//System.out.println(10);
						pstmUPDREC.setDate(4,new java.sql.Date(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).getTime()));	//System.out.println(10);
						pstmUPDREC.setDate(5,new java.sql.Date(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).getTime()));	//System.out.println(11);
						pstmUPDREC.setString(6,strSTRTP);	//System.out.println(12);
						pstmUPDREC.setString(7,strMGPTP);	//System.out.println(13);
						pstmUPDREC.setString(8,strMGPNO);	//System.out.println(14);
						pstmUPDREC.setString(9,strMATCD);	//System.out.println(15);
						
						pstmUPDREC.executeUpdate();
						L_intROWCT++;
					}
				}
			}
			catch(Exception L_Ex)
			{
				cl_dat.M_flgLCUPD_pbst = false;
				setMSG(L_Ex,"exeSAVE");
			}
			//Update Status Flag if Record Is Successfully added
		
			if(cl_dat.M_flgLCUPD_pbst)
			{
				try
				{
					strSTSFL = "C";
					M_strSQLQRY = "SELECT GP_MATCD,GP_ISSQT,GP_RECQT FROM MM_GPTRN WHERE "
						+"GP_MGPNO = '"+strMGPNO+"' AND GP_STRTP = '"+strSTRTP+"' AND "
						+"GP_MGPTP = '"+strMGPTP+"'";
					M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
						{
							if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_ISSQT"),"0")) > Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("GP_RECQT"),"0")))
								strSTSFL = "A";
						}
					}
					if(strSTSFL == "C")
					{
						M_strSQLQRY = "UPDATE MM_GPTRN SET ";
						M_strSQLQRY += getUSGDTL("GP",'U',"C");
						M_strSQLQRY +=" WHERE GP_ISSQT = GP_RECQT AND GP_STRTP = '"+strSTRTP+"' AND "
							+"GP_MGPTP = '"+strMGPTP+"' AND GP_MGPNO = '"+strMGPNO+"'";
						//System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					M_strSQLQRY = "UPDATE MM_WBTRN SET WB_DOCRF = '"+strMGPNO+"',";
					M_strSQLQRY += getUSGDTL("WB",'U',null);
					M_strSQLQRY += "WHERE WB_DOCTP = '02' AND WB_DOCNO ='"+txtGINNO.getText().trim() +"' and wb_docrf is null";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.exeDBCMT("exeSAVE"))
					{
						setMSG("Data Saved Successfully..",'N');
						clrCOMP();
					}
					else
					{
						setMSG("Error in saving the data..",'E');
					}
				}
				catch(SQLException L_SQL)
				{
					setMSG(L_SQL,"exeSAVE");
				}
				
			}
		}
	}
	/**Checking Input For Table
	 */
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			if(getSource() == tblMATDS)
			{
				if(P_intCOLID == TBL_RECQT)
				{
					if(Double.parseDouble(((JTextField)tblMATDS.cmpEDITR[TBL_RECQT]).getText()) > Double.parseDouble(tblMATDS.getValueAt(tblMATDS.getSelectedRow(),TBL_TOREC).toString()))
					{
						setMSG("Return Qty Should Be Less Than Or Equal To Qty. To Received At Col 5",'E');
						return false;
					}
				}
			}
			return true;
		}
	}
	/**Checking Input For TextField
	 */
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try{
				if(input == txtRGPNO)
				{
					if(((JTextField)input).getText().trim().length() == 0)
						return true;
					if(txtRGPNO.getText().trim().length() < 8)
						return true;
					M_strSQLQRY = "SELECT COUNT(*) FROM MM_GPTRN WHERE GP_MGPNO = '"+((JTextField)input).getText()+"' AND "
						+"GP_STRTP = '"+strSTRTP+"' AND GP_MGPTP = '51' AND GP_STSFL NOT IN('X','C')";
					
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3(M_strSQLQRY);
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)== 0)
							{
								setMSG("Invalid Returnable Gate Pass Number..",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Returnable Gate Pass Number..",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Material Code ",'E');
						return false;
					}
				}
				if(input == txtGINNO)
				{
					if(((JTextField)input).getText().trim().length() == 0)
						return true;
					M_strSQLQRY = "SELECT COUNT(*) FROM MM_WBTRN WHERE WB_DOCNO = '"+((JTextField)input).getText()+"' AND "
						+"WB_DOCTP = '02' AND WB_DOCRF IS NULL AND isnull(WB_STSFL,'')<> 'X'";
					
					ResultSet L_rstTEMP = cl_dat.exeSQLQRY3(M_strSQLQRY);
					if(L_rstTEMP != null)
					{
						if(L_rstTEMP.next())
						{
							if(L_rstTEMP.getInt(1)== 0)
							{
								setMSG("Invalid Gate In Number..",'E');
								return false;
							}
						}
						else
						{
							setMSG("Invalid Gate In Number..",'E');
							return false;
						}
					}
					else
					{
						setMSG("Invalid Gate In Number..",'E');
						return false;
					}
				}
				return true;
			}
			catch(SQLException L_SQL)
			{
				setMSG(L_SQL,"verify");
				return false;
			}
		}
	}
}