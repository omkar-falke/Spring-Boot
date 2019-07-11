/*
System Name   : Material Management System
Program Name  : Tanker Routing Slip

Program Desc. : User selects the Gate-In No. & Gate-In Type
Author        : N.K.Virdi
Date          : 24th Nov 2003
Version       : MMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.sql.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File; 
import java.io.FileOutputStream; 
import java.io.DataOutputStream; 
import java.io.PrintWriter;

public class mm_rptrs extends cl_rbase{
	private JComboBox cmbGINTP;
	private JTextField txtGINNO;
	private JOptionPane L_optOPTN;
	private String strREPFL = "c:\\reports\\mm_rptrs.doc";
	private String strGINTP,strGINNO;
	private String strSRLNO = "1";
	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;
	
	final String strRAWMT = "01";
	
	mm_rptrs()
	{
		super(2);
		try
		{
			setMatrix(20,6);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Gate-In Type "),1,4,1,1,this,'L');
			add(cmbGINTP = new JComboBox(),2,4,1,1,this,'L');
			add(new JLabel("Gate-In No. "),1,5,1,1,this,'L');
			add(txtGINNO = new TxtLimit(8),2,5,1,1,this,'L');
			
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			String L_strTEMP;
			while(M_rstRSSET.next())
			{
				L_strTEMP = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
				cmbGINTP.addItem(L_strTEMP);
			}
			
			txtGINNO.requestFocus();
			L_optOPTN = new JOptionPane();
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	/*void setENBL(boolean P_flgOPTN)
	{
		cmbGINTP.setEnabled(P_flgOPTN);
		txtGINNO.setEnabled(P_flgOPTN);
	}*/
				 
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
			exeHLPOK();		
		else if(M_objSOURC == cmbGINTP)
		{
			txtGINNO.setText("");
			txtGINNO.requestFocus();
		}
		else if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				setMSG("Please select the option as 'PRINT'",'E');
				setENBL(false);
			}
			else
			{
				setMSG("",'N');
				setENBL(true);
			}
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == 9 || L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{ 
			if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
					exeHLPOK();		
			else if(M_objSOURC == txtGINNO)
			{
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				strGINNO = txtGINNO.getText().trim();
				if(vldGINNO(strGINTP,strGINNO))
					setMSG("",'N');
				else
				{
					txtGINNO.requestFocus();
					setMSG("Invalid Gate-In No. Press F1 for Help",'E');
				}
			}
		}
		if (L_KE.getKeyCode()== L_KE.VK_F1)
		{
			if(M_objSOURC == txtGINNO)
			{
				
				setCursor(cl_dat.M_curWTSTS_pbst);
				strGINTP = String.valueOf(cmbGINTP.getSelectedItem()).substring(0,2);
				try
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtGINNO";
					String L_ARRHDR[] = {"Sr.No.","Gate-In No.","Lorry No","In Time","By","Out Time","By"};
					M_strSQLQRY = "Select WB_SRLNO,WB_DOCNO,WB_LRYNO,WB_INCTM,WB_WINBY,WB_OUTTM,WB_WOTBY";
				    M_strSQLQRY += "  from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(WB_STSFL,'') <> 'X'";
					M_strSQLQRY += " and WB_DOCTP = '" + strGINTP + "'";
					M_strSQLQRY += " order by WB_INCTM,WB_DOCNO,WB_SRLNO desc";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,7,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
				catch(Exception L_EX)
				{
					setCursor(cl_dat.M_curDFSTS_pbst);
					setMSG(L_EX,"hlpGINNO");
				}										  

			}
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtGINNO"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtGINNO.setText(cl_dat.M_strHLPSTR_pbst);
				strSRLNO = String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	// Method to validate the Gate-In No.
	private boolean vldGINNO(String P_strGINTP,String P_strGINNO)
	{
		try
		{
			M_strSQLQRY = "Select WB_DOCNO from MM_WBTRN ";
            M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL <> 'X'";
			M_strSQLQRY += " and WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				M_rstRSSET.close();
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldGINNO");
		}
		return false;
	}
	
	// Method to print the Gate-In details on Tanker Routing Slip
	void prnGINVL(String P_strGINTP,String P_strGINNO)
	{
			
		String L_strLRYNO,L_strTPRDS,L_strCHLNO,L_strCHLQT,L_strGINBY,L_strMATCD,L_strMATDS,L_strPRTDS;
		java.sql.Date L_datCHLDT;
		java.sql.Timestamp L_tmsGINDT;
		try
		{
			String L_strSTRYN ="6805010045";
			String L_strUOMCD ="";
			M_strSQLQRY = "Select WB_GINDT,WB_LRYNO,WB_TPRDS,WB_PRTDS,WB_MATCD,WB_MATDS,";
			M_strSQLQRY += "WB_CHLNO,WB_CHLDT,WB_CHLQT,WB_GINBY,CT_UOMCD from MM_WBTRN,CO_CTMST WHERE CT_MATCD = "+"isnull(WB_MATCD,'"+L_strSTRYN+"')";
			M_strSQLQRY += " AND WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			if(M_rstRSSET.next())
			{
				L_tmsGINDT = M_rstRSSET.getTimestamp("WB_GINDT");
				L_strLRYNO = M_rstRSSET.getString("WB_LRYNO");
				L_strTPRDS = M_rstRSSET.getString("WB_TPRDS");
				L_strPRTDS = M_rstRSSET.getString("WB_PRTDS");
				L_strMATCD = M_rstRSSET.getString("WB_MATCD");
				L_strUOMCD = M_rstRSSET.getString("CT_UOMCD");
				L_strMATDS = M_rstRSSET.getString("WB_MATDS");
				L_strCHLNO = M_rstRSSET.getString("WB_CHLNO");
				L_datCHLDT = M_rstRSSET.getDate("WB_CHLDT");
				L_strCHLQT = M_rstRSSET.getString("WB_CHLQT");
				L_strGINBY = M_rstRSSET.getString("WB_GINBY");
                FileOutputStream L_FOUT = new FileOutputStream("LPT1:"); 
				PrintWriter prnWRITER = new PrintWriter(L_FOUT,true);
				try
				{
					if(!L_strMATCD.equals(L_strSTRYN))
					{
						// For material other than styrene
						StringBuffer L_stbPRNST = new StringBuffer("");
						L_stbPRNST.append("\n\n\n\n\n\n\n\n\n\n\n");  // 11 lines
						if(L_tmsGINDT !=null)
						L_stbPRNST.append(padSTRING('L',"",15) + M_fmtLCDTM.format(L_tmsGINDT) + "\n");
						else
						L_stbPRNST.append(padSTRING('L',"",15) + " " + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + P_strGINNO + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + L_strLRYNO + "\n");
						L_stbPRNST.append(padSTRING('L',"",64) + L_strGINBY + "\n");
						L_stbPRNST.append("\n\n");  // 3 lines
						L_stbPRNST.append(padSTRING('L',"",15) + L_strTPRDS + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + L_strPRTDS + "\n");
						L_stbPRNST.append(padSTRING('L',"",36) + L_strMATDS + "\n");
						L_stbPRNST.append(padSTRING('L',"",36) + L_strCHLNO + "\n");
						if(L_datCHLDT !=null)
							L_stbPRNST.append(padSTRING('L',"",36) + M_fmtLCDAT.format(L_datCHLDT) + "\n");
						else
							L_stbPRNST.append(padSTRING('L',"",36) + "" + "\n");
						L_stbPRNST.append(padSTRING('L',"",36) + L_strCHLQT +"  "+L_strUOMCD+ "\n");
						prnWRITER.print(L_stbPRNST.toString());
					}
					else
					{
						// For Styrene
						StringBuffer L_stbPRNST = new StringBuffer("");
						L_stbPRNST.append("\n\n\n\n\n\n\n\n");  // 8 lines
						L_stbPRNST.append(padSTRING('L',"",15) + M_fmtLCDTM.format(L_tmsGINDT) + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + P_strGINNO + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + L_strLRYNO + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + L_strTPRDS + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + L_strPRTDS + "\n");
						L_stbPRNST.append(padSTRING('L',"",15) + L_strMATDS + "\n");
						L_stbPRNST.append(padSTRING('L',"",30) + padSTRING('R',L_strCHLNO,33) + L_strGINBY + "\n");
						L_stbPRNST.append(padSTRING('L',"",30) + M_fmtLCDAT.format(L_datCHLDT) + "\n");
						L_stbPRNST.append(padSTRING('L',"",30) + L_strCHLQT +"  "+L_strUOMCD+ "\n");
						//L_stbPRNST.append("");
						//L_stbPRNST.append("  ");
						prnWRITER.print(L_stbPRNST.toString());
					}
							}catch(Exception e){
					setMSG(e,"writing the file ");
				}		
				finally
				{
					prnWRITER.println(" ");
					L_FOUT.flush();
					L_FOUT.close();
				}
			}
			else
			{
				setMSG("Record not found",'E');
			}
			if(M_rstRSSET != null)
					M_rstRSSET.close();
			
		}catch(Exception e){
			setMSG(e,"prnGINVL");
		}
	}
void exePRINT()
{
	if(strGINTP.equals(strRAWMT)){	// Tankers
				JOptionPane.showMessageDialog(this,"Insert Tanker Routing Slip in Printer","WeighBridge In/Out Time",JOptionPane.INFORMATION_MESSAGE); 
				prnGINVL(cmbGINTP.getSelectedItem().toString().substring(0,2),txtGINNO.getText().trim());	
				return;
			}
	
}
}
