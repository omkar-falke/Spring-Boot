import javax.swing.*;
import java.sql.*;
import java.awt.event.*;
class mm_teobs extends cl_pbase
{
	private JTextField txtMATCD,txtUOMCD,txtMATDS;
	private JCheckBox chkOBSFL;
	mm_teobs()
	{
		super(2);
		setMatrix(20,8);	
		add(new JLabel("Item Code"),3,3,1,1,this,'L');
		add(txtMATCD = new TxtLimit(10),3,4,1,1,this,'L');
		add(new JLabel("UOM Code"),3,5,1,1,this,'L');
		add(txtUOMCD = new TxtLimit(3),3,6,1,1,this,'L');
		add(new JLabel("Description"),4,3,1,1,this,'L');
		add(txtMATDS = new TxtLimit(60),4,4,1,6,this,'L');
		add(chkOBSFL = new JCheckBox("Obsolete"),5,3,1,1,this,'L');
		setENBL(false);
	}
	boolean vldDATA()
	{
		if(txtMATCD.getText().length() ==0)
		{
			setMSG("Item Code can not be blank..",'E');
			return false;
		}
		return true;
	}
	void exeSAVE()
	{
		try
		{
		if(!vldDATA())
			return;
		String L_strSTSFL ="";
		String L_strSRLNO ="";
		cl_dat.M_flgLCUPD_pbst = true;
		if(!chkOBSFL.isSelected())
			L_strSTSFL ="";
		else L_strSTSFL ="9";

		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
		{
			M_strSQLQRY = "SELECT MAX(CT_SRLNO) L_SRLNO from mm_ctohs where ";
			M_strSQLQRY += " CT_MATCD ='"+txtMATCD.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
				L_strSRLNO = nvlSTRVL(M_rstRSSET.getString("L_SRLNO"),"0");
			int L_intSRLNO = Integer.parseInt(L_strSRLNO)+1;
			 L_strSRLNO =String.valueOf(L_intSRLNO);
			if(L_strSRLNO.length() != 3)
			{
				if(L_strSRLNO.length() == 1)
				   L_strSRLNO ="00"+L_strSRLNO;
				else if(L_strSRLNO.length() == 2)
				   L_strSRLNO ="0"+L_strSRLNO;	
				else if(L_strSRLNO.length() > 3)
				{
					setMSG("Too many revisions..",'E');
					return;
				}
				System.out.println(L_strSRLNO);
			}
			M_strSQLQRY = " INSERT INTO MM_CTOHS(CT_MATCD,CT_MATDS,CT_SRLNO,CT_TRNFL,CT_STSFL,CT_LUSBY,CT_LUPDT) VALUES(";
			M_strSQLQRY += "'"+txtMATCD.getText().trim() +"',";
			M_strSQLQRY += "'"+txtMATDS.getText().trim() +"',";
			M_strSQLQRY += L_strSRLNO +",";
			M_strSQLQRY += getUSGDTL("CT",'I',L_strSTSFL)+")";
			
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");

			M_strSQLQRY = " UPDATE CO_CTMST SET ";
			M_strSQLQRY += getUSGDTL("CT",'U',L_strSTSFL);
			M_strSQLQRY += " WHERE CT_MATCD ='"+txtMATCD.getText().trim()+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");

			M_strSQLQRY = " UPDATE CO_CTTRN SET ";
			M_strSQLQRY += getUSGDTL("CTT",'U',L_strSTSFL);
			M_strSQLQRY += " WHERE CTT_MATCD ='"+txtMATCD.getText().trim()+"' and ctT_stsfl <>'X'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");
			M_strSQLQRY = " UPDATE MM_STMST SET ";
			M_strSQLQRY += getUSGDTL("ST",'U',L_strSTSFL);
			M_strSQLQRY += " WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD ='"+txtMATCD.getText().trim()+"' AND ST_STSFL <>'X'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"");


			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Obsolete Tag modified ..",'N');
				clrCOMP();
			}
			else
			{
				setMSG("Error in updating Obsolete Tag  ..",'E');
			}

		}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
		}
		
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		txtMATCD.setEnabled(true);
		txtMATDS.setEnabled(false);
		txtUOMCD.setEnabled(false);
		chkOBSFL.setEnabled(true);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			setENBL(false);	
		}
		if(M_objSOURC == txtMATCD)
		{
			try
			{
				txtMATDS.setText("");
				txtUOMCD.setText("");
				chkOBSFL.setSelected(false);
				getDATA();
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Item verification..");				
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
			
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X' ";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY +=" Order by CT_MATCD ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT",new int[]{100,400});
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F2)
		{
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCDF2";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
			
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X' ";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY +=" ORDER by CT_MATDS";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",new int[]{100,400});
			}
		
		}
	}
class inpVRFY extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
		{	
				return true;
		}
		else if(input == txtMATCD)
		{
			try
			{
				if(txtMATCD.getText().trim().length() ==10)
				{
				M_strSQLQRY ="Select * from CO_CTMST WHERE CT_CODTP IN('CD','CR') AND CT_MATCD ='"+txtMATCD.getText().trim() +"' AND isnull(CT_STSFL,'') <>'X'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						
					}
					else
					{
						setMSG("Material Code does not exist..",'E');
						return false;
					}
					return true;
				}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"MATCD verifier..");
			}
		}
		return false;	
	}
}
private void getDATA()
{
	try
	{
		M_strSQLQRY = "Select * from CO_CTMST where ";
		M_strSQLQRY += " isnull(ct_stsfl,'') <>'X' and ct_matcd ='"+txtMATCD.getText().trim()+"' ";
		setCursor(cl_dat.M_curWTSTS_pbst);		
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		if(M_rstRSSET.next())
		{
			txtMATDS.setText(M_rstRSSET.getString("CT_MATDS"));
			txtUOMCD.setText(M_rstRSSET.getString("CT_UOMCD"));
			if(nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"").equals("9"))
				chkOBSFL.setSelected(true);
			else
				chkOBSFL.setSelected(false);

		}
		if(M_rstRSSET !=null)
		  M_rstRSSET.close();	
		setCursor(cl_dat.M_curDFSTS_pbst);		

	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getDATA");
	}
}	
void exeHLPOK()
{
	super.exeHLPOK();
	if((M_strHLPFLD.equals("txtMATCD"))||(M_strHLPFLD.equals("txtMATCDF2")))
	{
		txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
		txtMATDS.setText(cl_dat.M_strHLPSTR_pbst);	
	}
}
}