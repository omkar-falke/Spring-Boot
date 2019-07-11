/*
System Name   : Material Management System
Program Name  : Update weighbridge 
Program Desc. : Modify Weighbridge entry if problem in weighbridge
Author        : A. T. Chaudhari
Date          : 01/10/2004
Version       : MMS 2.0
*/
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
class mm_tewba extends cl_pbase
{									/**JText Field for Gross Weight*/
	private JTextField txtGRSWT;	/**Text field for tare weight*/
	private JTextField txtTARWT;	/**Text Field for new weight*/
	private JTextField txtNETWT;	/**Text Field for uonit of measuerment quantity*/
	private JTextField txtUOMQT;	/**Text Field for location code*/
	private JTextField txtLOCCD;	/**Text Field for tank number*/
	private JTextField txtTNKNO;	/**Text Filed for Document number*/
	private JTextField txtDOCNO;
	//private JTextField txtWOTBY;
	//private JTextField txtOUTTM;
	private boolean flgINPVF = true;
	
	public mm_tewba()
	{
		super(2);
		setMatrix(20,8);
		add(new JLabel("Document Number"),2,3,1,2,this,'L');
		add(txtDOCNO = new TxtLimit(8),2,5,1,1,this,'L');
		add(new JLabel("Gross Weight"),3,3,1,2,this,'L');
		add(txtGRSWT = new TxtNumLimit(12.3),3,5,1,1,this,'L');
		add(new JLabel("Tare Weight"),4,3,1,2,this,'L');
		add(txtTARWT = new TxtNumLimit(12.3),4,5,1,1,this,'L');
		add(new JLabel("Net Weigth"),5,3,1,2,this,'L');
		add(txtNETWT = new TxtNumLimit(12.3),5,5,1,1,this,'L');
		add(new JLabel("UOM Quantity"),6,3,1,2,this,'L');
		add(txtUOMQT = new TxtNumLimit(12.3),6,5,1,1,this,'L');
		add(new JLabel("Location Code"),7,3,1,2,this,'L');
		add(txtLOCCD = new TxtLimit(3),7,5,1,1,this,'L');
		add(new JLabel("Tank Number"),8,3,1,2,this,'L');
		add(txtTNKNO = new TxtLimit(10),8,5,1,1,this,'L');
		//add(new JLabel("Weigh Bridge Out By"),9,3,1,2,this,'L');
		//add(txtWOTBY = new TxtLimit(3),9,5,1,1,this,'L');
		//add(new JLabel("Out Time"),10,3,1,2,this,'L');
		//add(txtOUTTM = new JTextField(15),10,5,1,1,this,'L');
		txtTNKNO.setInputVerifier(new INPVF());
		txtDOCNO.setInputVerifier(new INPVF());
		
		setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					txtDOCNO.requestFocus();
				}
				else
					setENBL(false);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtDOCNO)
				getWBDATA();
			if(M_objSOURC == txtTNKNO)
			{
				txtTNKNO.setText(txtTNKNO.getText().trim().toUpperCase());
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
			else
				((JComponent)M_objSOURC).transferFocus();
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			flgINPVF = false;
			//method to display help window for document number i.e gate in number where gross or tera or net weight is null
			if(M_objSOURC == txtDOCNO)
			{
				M_strHLPFLD = "txtDOCNO";
				M_strSQLQRY = "SELECT DISTINCT WB_DOCNO,WB_GINDT,WB_GRSWT,WB_TARWT,WB_NETWT FROM "
					+"MM_WBTRN WHERE (ISNULL(WB_GRSWT,0)=0 OR ISNULL(WB_TARWT,0)=0 OR ISNULL(WB_NETWT,0)=0) and wb_doctp='01' and SUBSTRING(wb_docno,1,1)='5'";
				if(txtDOCNO.getText().trim().length() > 0)
					M_strSQLQRY += "AND WB_DOCNO LIKE '"+txtDOCNO.getText().trim()+"%' ";
				M_strSQLQRY += " ORDER BY WB_DOCNO ";
				//System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Document Number","In Date","Gross Weignt","Tare Weignt","Net Weight"},5,"CT");
			}
			if(M_objSOURC == txtTNKNO)
			{
				M_strHLPFLD = "txtTNKNO";
				M_strSQLQRY = "SELECT DISTINCT DP_TNKNO FROM MM_DPTRN WHERE ISNULL(DP_STSFL,'') <> 'X' ";
				if(txtTNKNO.getText().trim().length() > 0)
					M_strSQLQRY += " AND DP_TNKNO LIKE '"+txtTNKNO.getText().trim()+"%'";
				M_strSQLQRY += "ORDER BY DP_TNKNO ";
				cl_hlp(M_strSQLQRY,1,1,new String[]{"Tank Number"},1,"CT");
			}
		}
	}
	public void exeHLPOK()
	{
		flgINPVF=true;
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtDOCNO"))
		{
			txtDOCNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtGRSWT.requestFocus();
			getWBDATA();
		}
		if(M_strHLPFLD.equals("txtTNKNO"))
		{
			txtTNKNO.setText(cl_dat.M_strHLPSTR_pbst);
			cl_dat.M_btnSAVE_pbst.requestFocus();
		}
	}
  	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtGRSWT)
			setMSG("Enter Gross Weight ..",'N');
		if(M_objSOURC == txtTARWT)
			setMSG("Enter Tran Weigth ..",'N');
		if(M_objSOURC == txtNETWT)
			setMSG("Enter Net Weigth ..",'N');
		if(M_objSOURC == txtUOMQT)
		{
			if(txtNETWT.getText().trim().length() > 0)
			{
				txtUOMQT.setText(txtNETWT.getText().trim());
				txtUOMQT.select(0,txtUOMQT.getText().length());
			}
			setMSG("Enter Unit Of Measurment Quantity ..",'N');
		}
		if(M_objSOURC == txtLOCCD)
		{
			txtLOCCD.setText("AGS");
			txtLOCCD.select(0,txtLOCCD.getText().length());
			setMSG("Enter Location Code ..",'N');
		}
		if(M_objSOURC == txtTNKNO)
			setMSG("Enter Tank Number ..",'N');
		if(M_objSOURC == txtDOCNO)
			setMSG("Enter Document Number ..",'N');
	}
	public boolean vldDATA()
	{
		try
		{
			if(txtGRSWT.getText().trim().length() == 0)
			{
				setMSG("Enter Gross Weigth ..",'E');
				txtGRSWT.requestFocus();
				return false;
			}
			if(txtTARWT.getText().trim().length() == 0)
			{
				setMSG("Enter Tare Weight ..",'E');
				txtTARWT.requestFocus();
				return false;
			}
			if(txtNETWT.getText().trim().length() == 0)
			{
				setMSG("Enter Net Weight ..",'E');
				txtNETWT.requestFocus();
				return false;
			}
			if(txtUOMQT.getText().trim().length() == 0)
			{
				setMSG("Enter Unit Of Measurment Quantity Weigth ..",'E');
				txtUOMQT.requestFocus();
				return false;
			}
			if(txtLOCCD.getText().trim().length() == 0)
			{
				setMSG("Enter Location Code ..",'E');
				txtLOCCD.requestFocus();
				return false;
			}
			if(txtTNKNO.getText().trim().length() == 0)
			{
				setMSG("Enter Tank Number ..",'E');
				txtTNKNO.requestFocus();
				return false;
			}
			if(txtDOCNO.getText().trim().length() == 0)
			{
				setMSG("Enter Document Number( Gate In Number)..",'E');
				txtDOCNO.requestFocus();
				return false;
			}
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
	}
	private class INPVF extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			try
			{
				if(!flgINPVF)
				{
					flgINPVF=true;
					return true;
				}
				if(input == txtDOCNO)
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					M_strSQLQRY = "SELECT COUNT(*) FROM MM_WBTRN WHERE WB_DOCNO = '"
						+txtDOCNO.getText().trim()+"' AND ISNULL(WB_STSFL,'') <> 'X' ";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt(1)<= 0)
						{
							setMSG("Enter Valid Gate In Number Or Press 'F1' For Help ..",'E');
							txtDOCNO.requestFocus();
							return false;
						}
					}
					else
					{	
						setMSG("Entre valid Gate In Numbre or Press 'F1' For Help ..",'N');
						txtDOCNO.requestFocus();
						return false;
					}		
				}
				if(input == txtTNKNO)
				{
					if(((JTextField)input).getText().length() == 0)
						return true;
					M_strSQLQRY = "SELECT COUNT(*) FROM MM_DPTRN WHERE ISNULL(DP_STSFL,'') <> 'X' "
						+"AND DP_TNKNO = '"+txtTNKNO.getText().trim().toUpperCase()+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt(1) <= 0)
						{
							setMSG("Enter Tank Number Valid Tank Number Or Press 'F1' For Help ..",'E');
							txtTNKNO.requestFocus();
							return false;
						}
					}
					else
					{
						setMSG("Enter Tank Number Valid Tank Number Or Press 'F1' For Help ..",'E');
						txtTNKNO.requestFocus();
						return false;
					}
				}
				return true;
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"Input Verifier");
				return false;
			}
			
		}
	}
	public void getWBDATA()
	{
		try
		{
			M_strSQLQRY = "SELECT WB_GRSWT,WB_TARWT,WB_NETWT,WB_UOMQT,WB_LOCCD,WB_TNKNO,WB_WOTBY,"
				+"WB_OUTTM FROM MM_WBTRN WHERE WB_DOCNO = '"+txtDOCNO.getText().trim()+"' ";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_GRSWT"),"0.000")) > 0)
					txtGRSWT.setEnabled(false);
				txtGRSWT.setText(M_rstRSSET.getString("WB_GRSWT"));
				if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_TARWT"),"0.000")) > 0)
					txtTARWT.setEnabled(false);
				txtTARWT.setText(M_rstRSSET.getString("WB_TARWT"));
				if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0.000")) > 0)
					txtNETWT.setEnabled(false);
				txtNETWT.setText(M_rstRSSET.getString("WB_NETWT"));
				if(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_UOMQT"),"0.000")) > 0)
					txtUOMQT.setEnabled(false);
				txtUOMQT.setText(M_rstRSSET.getString("WB_UOMQT"));
				if(nvlSTRVL(M_rstRSSET.getString("WB_LOCCD"),"").length() > 0)
					txtLOCCD.setEnabled(false);
				txtLOCCD.setText(M_rstRSSET.getString("WB_LOCCD"));
				if(nvlSTRVL(M_rstRSSET.getString("WB_TNKNO"),"").length() > 0)
					txtTNKNO.setEnabled(false);
				txtTNKNO.setText(M_rstRSSET.getString("WB_TNKNO"));
				/*
				if(M_rstRSSET.getString("WB_WOTBY").trim().length() > 0)
					txtWOTBY.setEnabled(false);
				txtWOTBY.setText(M_rstRSSET.getString("WB_WOTBY"));
				if(M_rstRSSET.getString("WB_OUTTM").trim().length() > 0)
					txtOUTTM.setEnabled(false);
				txtOUTTM.setText(M_rstRSSET.getString("WB_OUTTM"));
				*/
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getWBDATA");
		}
	}
	public void exeSAVE()
	{
		if(vldDATA())
		{
			try
			{
				cl_dat.M_flgLCUPD_pbst = true;
				
				M_strSQLQRY = "UPDATE MM_WBTRN SET WB_GRSWT = "+txtGRSWT.getText().trim()+","
					+"WB_TARWT = "+txtTARWT.getText().trim()+",WB_NETWT = "+txtNETWT.getText().trim()+","
					+"WB_UOMQT = "+txtUOMQT.getText().trim()+",WB_LOCCD = '"+txtLOCCD.getText().trim()+"',"
					+"WB_TRNFL ='0',WB_TNKNO = '"+txtTNKNO.getText().trim()+"' "
					+"WHERE WB_DOCTP = '01' AND WB_DOCNO = '"+txtDOCNO.getText().trim()+"'";
					//System.out.println(M_strSQLQRY);
				cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Record Updateded Successfully..",'N');
					clrCOMP();
					txtDOCNO.requestFocus();
				}
				else
					setMSG("Error Updating Record ..",'N');
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"exeSAVE");
			}
		}
	}
}



