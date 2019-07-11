// Driver Master Entry
		
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.StringTokenizer;
import java.util.Hashtable;

class mm_medvm extends cl_pbase{
	
	private JTextField txtDRVCD,txtDRVNM,txtVENTP,
					   txtTPRCD,txtTPRNM,txtLICNO,
					   txtLICBY,txtLVLDT,txtBLKCD;
	private JTextArea txtREMDS;
	private JLabel lblBLKDS;
	private String strDRVCD;		
	private Hashtable<String,String> hstBLKCD;
			 
	mm_medvm()
	{
		super(1);
		int y = 80;
		setMatrix(14,7);
		setVGAP(15);
		add(new JLabel("Driver Code"),2,1,1,1,this,'L');
		add(txtDRVCD = new TxtLimit(5),2,2,1,1,this,'L');
		add(txtDRVNM = new TxtLimit(30),2,3,1,3,this,'L');
		add(new JLabel("Transporter"),3,1,1,1,this,'L');
		add(txtTPRCD = new TxtLimit(5),3,2,1,1,this,'L');
		add(txtTPRNM = new TxtLimit(100),3,3,1,3,this,'L');
		add(new JLabel("Licence No."),4,1,1,1,this,'L');
		add(txtLICNO = new TxtLimit(20),4,2,1,1,this,'L');
		add(new JLabel("Valid Upto"),4,3,1,1,this,'L');
		add(txtLVLDT = new TxtDate(),4,4,1,1,this,'L');	
		add(new JLabel("Iss. Authority"),5,1,1,1,this,'L');
		add(txtLICBY = new TxtLimit(20),5,2,1,1,this,'L');
		add(new JLabel("Black Listing Status"),6,1,1,1,this,'L');
		add(txtBLKCD = new TxtLimit(2),6,2,1,1,this,'L');
		add(lblBLKDS = new JLabel(""),6,3,1,5,this,'L');
		add(new JLabel("Black Listing Remark"),7,1,1,1,this,'L');
		//add(txtREMDS = new TxtLimit(20),7,2,1,3,this,'L');
		txtREMDS = new TxtAreaLimit(200);	
		JScrollPane scrollPane=new JScrollPane(txtREMDS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 		scrollPane.setBounds(10, 10, 300, 200);
		add(scrollPane,7,2,5,3,this,'L');
		txtTPRNM.setEnabled(false);
		setENBL(false);
		txtDRVCD.setEnabled(false);
		txtBLKCD.setEnabled(false);
		txtREMDS.setEnabled(false);
		
		////Driver black listing codes
		hstBLKCD=new Hashtable<String,String>(); 			
		try
		{
				M_strSQLQRY  = " Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXDBL'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
						hstBLKCD.put(M_rstRSSET.getString("CMT_CODCD"),M_rstRSSET.getString("CMT_CODDS"));
				}				
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"fetching BLKCD()");
		}
		//////////////////////////////////////////////////////////////////
			
			
		setMSG("Select an Option..",'N');
	}	
	void setENBL(boolean act)
	{
		if(!act)
			txtDRVCD.setEnabled(true);
		else
			txtDRVCD.setEnabled(false);
		txtDRVNM.setEnabled(act);
		txtDRVNM.setEnabled(act);
		txtTPRCD.setEnabled(act);
		txtLICNO.setEnabled(act);
		txtLICBY.setEnabled(act);
		txtLVLDT.setEnabled(act);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(txtBLKCD.getText().length()==0)
				lblBLKDS.setText("");
			
			if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
			{
              exeHLPOK();
			}
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					txtDRVCD.setEnabled(true);
					txtBLKCD.setEnabled(true);
					txtREMDS.setEnabled(true);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					txtBLKCD.setEnabled(false);
					txtREMDS.setEnabled(false);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					txtBLKCD.setEnabled(false);
					txtREMDS.setEnabled(false);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				{
					txtBLKCD.setEnabled(false);
					txtREMDS.setEnabled(false);
				}
			}
						
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Wbtrn ActionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			setMSG("",'N');
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(L_KE.getSource().equals(txtDRVCD))
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDRVCD";
					String L_ARRHDR[] = {"Code","Driver"};
					M_strSQLQRY = "select DV_DRVCD,DV_DRVNM from MM_DVMST where DV_STSFL <> 'X'";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT");
				}
				else if(L_KE.getSource().equals(txtTPRCD))
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtTPRCD";
					String L_ARRHDR[] = {"Code","Transporter"};
					M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST" +
								" where PT_PRTTP = 'T' and PT_STSFL <> 'X'";
						
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
				else if(L_KE.getSource().equals(txtBLKCD))
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtBLKCD";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN" +
								" where CMT_CGMTP = 'SYS' and CMT_CGSTP='MMXXDBL'";
						
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_TAB || L_KE.getKeyCode() == L_KE.VK_ENTER)
			{				
				//if (L_KE.getSource().equals(cl_dat.M_btnHLPOK_pbst) || L_KE.getSource().equals(cl_dat.M_txtHLPPOS_pbst))
				//	exeHLPOK();		
				if(L_KE.getSource().equals(txtDRVCD))
				{
					strDRVCD = txtDRVCD.getText().trim();
					exeGETREC(strDRVCD);
				}
				else if(L_KE.getSource().equals(txtDRVNM))
					txtTPRCD.requestFocus();
				else if(L_KE.getSource().equals(txtTPRCD))
					vldTPRCD(txtTPRCD.getText().trim());
				else if(L_KE.getSource().equals(txtLICNO))
					txtLVLDT.requestFocus();
				else if(L_KE.getSource().equals(txtLVLDT))
				{			
					if(txtLVLDT.getText().trim().length() == 0)
					{
						txtLVLDT.requestFocus();
						setMSG("License Validation Date should not be empty",'E');
					}
					else
						txtLICBY.requestFocus();
				}
				else if(L_KE.getSource().equals(txtLICBY))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						cl_dat.M_btnSAVE_pbst.requestFocus();
					else
						txtBLKCD.requestFocus();
				}	
				else if(L_KE.getSource().equals(txtBLKCD))
				{
					if(hstBLKCD.containsKey(txtBLKCD.getText().trim()))
					{	
						lblBLKDS.setText(hstBLKCD.get(txtBLKCD.getText().trim()));
						setMSG("",'N');
						txtREMDS.requestFocus();
					}	
					else
					{
						txtBLKCD.requestFocus();
						setMSG("Enter Valid Black Listing Code Code",'E');
					}
				}
				else if(L_KE.getSource().equals(txtREMDS))
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"keyPressed");
		}
	}
	private void vldTPRCD(String P_strTPRCD)
	{
		try
		{
			M_strSQLQRY = "select PT_PRTCD,PT_PRTNM from CO_PTMST" +
						" where PT_PRTTP = 'T' and PT_PRTCD = '" + P_strTPRCD + "'" +
						" and PT_STSFL <> 'X'";
				
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				txtTPRNM.setText(M_rstRSSET.getString("PT_PRTNM"));
				txtLICNO.requestFocus();
				setMSG("",'N');
			}
			else
			{
				txtTPRNM.setText("");
				txtTPRCD.requestFocus();
				setMSG("Invalid Transporter code. Press F1 for help.",'E');
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldTPRCD");
		}	
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtDRVCD"))
			{
				txtDRVNM.setText(cl_dat.M_strHLPSTR_pbst);
				txtDRVCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				strDRVCD = txtDRVCD.getText().trim();
				exeGETREC(strDRVCD);
			}
			else if(M_strHLPFLD.equals("txtTPRCD"))
			{
				txtTPRCD.setText(cl_dat.M_strHLPSTR_pbst);
				vldTPRCD(cl_dat.M_strHLPSTR_pbst);
			//	txtTPRCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtLICNO.requestFocus();
			}
			else if(M_strHLPFLD.equals("txtBLKCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtBLKCD.setText(L_STRTKN.nextToken());
				lblBLKDS.setText(L_STRTKN.nextToken());
				txtREMDS.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Method to check whether all necessary data has been provided
	 */ 
	private boolean chkDATVLD()
	{
		try
		{
			if(txtDRVNM.getText().trim().length() == 0)
			{		
				txtDRVNM.requestFocus();
				setMSG("Driver name should not be empty.",'E');
				return false;
			}
			else if(txtTPRCD.getText().trim().length() == 0)
			{		
				txtTPRCD.requestFocus();
				setMSG("Transporter should not be empty.",'E');
				return false;
			}
			else if(txtLICNO.getText().trim().length() == 0)
			{
				txtLICNO.requestFocus();
				setMSG("License No. should not be empty.",'E');
				return false;
			}
			else if(txtLICBY.getText().trim().length() == 0)
			{	
				txtLICBY.requestFocus();
				setMSG("License Issuing Authority should not be empty.",'E');
				return false;
			}
			else if(txtLVLDT.getText().trim().length() == 0)
			{		
				txtLVLDT.requestFocus();
				setMSG("Lic. Valid. Date should not be empty",'E');
				return false;
			}
		}catch(Exception e)
		{
			System.out.println("Error in chkDATCMPL : " + e.toString());
			return false;
		}
		return true;
	}
	private void exeGETREC(String P_strDRVCD)
	{
		try
		{
			M_strSQLQRY  = "Select	DV_DRVCD,DV_DRVNM,DV_TPRCD,DV_LICNO,DV_LICBY,DV_LVLDT,isnull(DV_BLKCD,'00') DV_BLKCD,DV_REMDS" ;
			M_strSQLQRY += " from MM_DVMST where DV_DRVCD = '" + P_strDRVCD + "'" ;
			M_strSQLQRY += " and DV_STSFL <> 'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				txtDRVNM.setText(nvlSTRVL(M_rstRSSET.getString("DV_DRVNM"),""));
				txtTPRCD.setText(nvlSTRVL(M_rstRSSET.getString("DV_TPRCD"),""));
				txtLICNO.setText(nvlSTRVL(M_rstRSSET.getString("DV_LICNO"),""));
				txtLICBY.setText(nvlSTRVL(M_rstRSSET.getString("DV_LICBY"),""));
				txtLVLDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("DV_LVLDT")));
				txtBLKCD.setText(nvlSTRVL(M_rstRSSET.getString("DV_BLKCD"),"00"));
				if(hstBLKCD.containsKey(M_rstRSSET.getString("DV_BLKCD")))
					lblBLKDS.setText(hstBLKCD.get(txtBLKCD.getText().trim()));
				txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("DV_REMDS"),""));
				vldTPRCD(txtTPRCD.getText().trim());
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					txtDRVNM.requestFocus();
					setENBL(true);
				}
				else
					txtDRVCD.requestFocus();
			}
			else
			{
				setMSG("No record found",'E');
				clrCOMP();
			}
			if(M_rstRSSET !=null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			System.out.println("Error in exeGETREC : " + e.toString());
		}
	}
	/** Method to Add / Modify / Delete record in the database
	 */
	void exeSAVE()
	{
		try
		{
			String L_strLVLDT,L_strLUPDT;
			String L_strDRVNM = txtDRVNM.getText().trim();
			String L_strLICBY = txtLICBY.getText().trim();
			String L_strLICNO = txtLICNO.getText().trim();
			String L_strTPRCD = txtTPRCD.getText().trim();
			L_strLVLDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtLVLDT.getText().trim()))+"'";
			L_strLUPDT = "'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()))+"'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				strDRVCD = getDRVCD(L_strDRVNM);			// Generate new Driver Code
			else
				strDRVCD = txtDRVCD.getText().trim();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY = "Insert into MM_DVMST(DV_DRVCD,DV_DRVNM,DV_TPRCD,DV_LICNO,DV_LICBY,";
				M_strSQLQRY += "DV_LVLDT,DV_TRNFL,DV_LUSBY,DV_LUPDT) values (" ;
				M_strSQLQRY += "'"+strDRVCD + "',"+"'" + L_strDRVNM + "','" + L_strTPRCD + "'," ;
				M_strSQLQRY += "'" + L_strLICNO + "','" + L_strLICBY + "'," + L_strLVLDT + "," ;
				M_strSQLQRY +="'0'"+",'" + cl_dat.M_strUSRCD_pbst.trim() + "'," + L_strLUPDT + ")";
			} 
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY =  "Update MM_DVMST set DV_DRVNM = '" + L_strDRVNM + "'," ;
				M_strSQLQRY += "DV_TPRCD = '" + L_strTPRCD + "',DV_LICNO = '" + L_strLICNO + "'," ;
				M_strSQLQRY += "DV_LICBY = '" + L_strLICBY + "',DV_LVLDT = " + L_strLVLDT + "," ;
				M_strSQLQRY += "DV_BLKCD = '" + txtBLKCD.getText().trim() + "',DV_REMDS = '" + txtREMDS.getText().replace("'","`") + "'," ;
				M_strSQLQRY += "DV_TRNFL = '0'" + ",DV_LUSBY = '" + cl_dat.M_strUSRCD_pbst.trim() + "'," ;
				M_strSQLQRY += "DV_LUPDT = " + L_strLUPDT + " where DV_DRVCD = '" + strDRVCD + "'";
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				M_strSQLQRY =  "Update MM_DVMST set DV_STSFL = 'X',"; 
				M_strSQLQRY += "DV_TRNFL = '0'" + ",DV_LUSBY = '" + cl_dat.M_strUSRCD_pbst.trim() + "'," ;
				M_strSQLQRY += "DV_LUPDT = " + L_strLUPDT + " where DV_DRVCD = '" + strDRVCD + "'";
			}

			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				
			if(cl_dat.exeDBCMT("")){
				setMSG("Record saved successfully.",'N');
				clrCOMP();
			}
			
		}catch(Exception L_EX){
			setMSG(L_EX,"exeADDREC");
		}
	}
	/**
	 *  Method to generate the new Driver Code, Driver code is generated on the basis of initial letter of Name
	 */
	private String getDRVCD(String P_strDRVNM)
	{
		String L_strDRVCD = "";
		String L_strINTLT = P_strDRVNM.substring(0,1).toUpperCase();
		try
		{
			M_strSQLQRY =  "Select max(DV_DRVCD) strDRVCD from MM_DVMST" ;
			M_strSQLQRY += " where SUBSTRING(DV_DRVCD,1,1) = '" + L_strINTLT + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				L_strDRVCD = M_rstRSSET.getString("strDRVCD");
				M_rstRSSET.close();
				if(L_strDRVCD == null)
				   L_strDRVCD = L_strINTLT + "0001";
				else
				{
					L_strDRVCD = String.valueOf(Integer.parseInt(L_strDRVCD.substring(1).trim()) + 1).trim();
					for(int i=L_strDRVCD.length(); i<4; i++)		
						L_strINTLT = L_strINTLT.concat("0");
			
					L_strDRVCD = L_strINTLT.concat(L_strDRVCD);
				}
			}
			else	
				L_strDRVCD = L_strINTLT + "0001";
		}
		catch(Exception e)
		{
			System.out.println("Error in getDRVCD : " + e.toString());
		}
		return L_strDRVCD;
	}
	public void itemStateChanged(ItemEvent L_IE)
	{
		super.itemStateChanged(L_IE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtDRVCD.setEnabled(true);
			}
		}
}
}	
