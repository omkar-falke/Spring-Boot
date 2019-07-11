/*
System Name   : Material Management System
Program Name  : WeighBridge Ticket Printing
Program Desc. : User selects the Ticket No. & the destination for the ticket as screen,
				printer or file.
Author        : N.K.Virdi
Date          : 13/11/2003
Version       : MMS 2.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.File; 
import java.io.FileOutputStream; 
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class mm_rpwbt extends cl_rbase
{
	ResultSet M_rstRSSET1;
	public JTextField txtGINTP,txtGINNO;
	private JRadioButton btnTKTNO,btnLARNO;
	private ButtonGroup bgrOPTN = new ButtonGroup();
	//private String strREPFL = "c:\\reports\\mm_rpwbt.doc";
	private String strREPFL = cl_dat.M_strREPSTR_pbst +"mm_rpwbt.doc";
	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;
	
	String strGINTP,strGINNO;
	String strSRLNO = "1",L_strWBRNO;
	final String strRAWMT = "01";
	final String strDESPT = "03";
	final String strOTHER = "04";
	
	mm_rpwbt()
	{
		super(2);

		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			btnTKTNO = new JRadioButton("Ticket Printing",true); 
			btnLARNO = new JRadioButton("Loading Advice",false); 
			bgrOPTN.add(btnTKTNO);
			bgrOPTN.add(btnLARNO);
			add(btnTKTNO,1,3,1,1,this,'L');
			add(btnLARNO,2,3,1,1,this,'L');
			btnTKTNO.setSelected(true);
			add(new JLabel("Gate-In Type "),1,4,1,1,this,'L');
			add(txtGINTP = new TxtLimit(2),2,4,1,1,this,'L');
			add(new JLabel("Gate-In No. "),1,5,1,1,this,'L');
			add(txtGINNO = new TxtLimit(8),2,5,1,1,this,'L');
			txtGINNO.requestFocus();
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	mm_rpwbt(int i)
	{
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			btnTKTNO = new JRadioButton("Ticket Printing",true); 
			btnLARNO = new JRadioButton("Loading Advice",false); 
			bgrOPTN.add(btnTKTNO);
			bgrOPTN.add(btnLARNO);
			add(btnTKTNO,2,3,1,1,this,'L');
			add(btnLARNO,3,3,1,1,this,'L');
			add(new JLabel("Gate-In Type "),1,4,1,1,this,'L');
			add(txtGINTP = new TxtLimit(2),2,4,1,1,this,'L');
			add(new JLabel("Gate-In No. "),1,5,1,1,this,'L');
			add(txtGINNO = new TxtLimit(8),2,5,1,1,this,'L');
			txtGINNO.requestFocus();
			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	void setENBL(boolean P_flgENBL)
	{
		txtGINTP.setEnabled(P_flgENBL);
		txtGINNO.setEnabled(P_flgENBL);
		btnTKTNO.setEnabled(P_flgENBL);
		btnLARNO.setEnabled(P_flgENBL);

	}
	// Method to get the values required to print the ticket from MM_WBTRN
	private boolean getALLREC(String P_strGINTP,String P_strGINNO,String P_strSRLNO)
	{
		String L_strWOTBY,L_strMATDS,L_strPRTDS,L_strLOCCD,L_strLOCDS,L_strORDRF,L_strCHLNO,L_strLRYNO,L_strTPRDS;
		java.sql.Timestamp L_tmsINCTM,L_tmsOUTTM;
		double L_dblGRSWT,L_dblTARWT,L_dblNETWT;
		try{
			M_strSQLQRY = "Select WB_DOCNO,WB_CHLNO,WB_LRYNO,WB_TPRDS,WB_PRTDS,WB_MATDS,";
			M_strSQLQRY += "WB_GRSWT,WB_TARWT,WB_INCTM,WB_OUTTM,WB_NETWT,WB_WOTBY,WB_LOCCD,";
			M_strSQLQRY += "WB_ORDRF,WB_WBRNO from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			M_strSQLQRY += " and WB_SRLNO = '" + P_strSRLNO + "'";
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET1.next())
			{
				M_strSQLQRY = "Select WB_DOCNO,WB_CHLNO,WB_LRYNO,WB_TPRDS,WB_PRTDS,WB_MATDS,";
				M_strSQLQRY += "WB_GRSWT,WB_TARWT,WB_INCTM,WB_OUTTM,WB_NETWT,WB_WOTBY,";
				M_strSQLQRY += "WB_LOCCD,WB_ORDRF,WB_WBRNO from MM_WBTRN";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP + "'";
				M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
				M_strSQLQRY += " and WB_SRLNO = '" + P_strSRLNO + "'";
				M_strSQLQRY += " and WB_STSFL = 'N'";
				M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET1.next())
				{
					L_strCHLNO = nvlSTRVL(M_rstRSSET1.getString("WB_CHLNO"),"");
					L_strORDRF = nvlSTRVL(M_rstRSSET1.getString("WB_ORDRF"),"");
					L_strLRYNO = nvlSTRVL(M_rstRSSET1.getString("WB_LRYNO"),"");
					L_strTPRDS = nvlSTRVL(M_rstRSSET1.getString("WB_TPRDS"),"");
					L_strPRTDS = nvlSTRVL(M_rstRSSET1.getString("WB_PRTDS"),"");
					L_strLOCCD = nvlSTRVL(M_rstRSSET1.getString("WB_LOCCD"),"");
					L_strLOCDS = getLOCDS(L_strLOCCD);
					L_strMATDS = nvlSTRVL(M_rstRSSET1.getString("WB_MATDS"),"");
					L_dblGRSWT = M_rstRSSET1.getDouble("WB_GRSWT");
					L_dblTARWT = M_rstRSSET1.getDouble("WB_TARWT");
					L_tmsINCTM = M_rstRSSET1.getTimestamp("WB_INCTM");
					L_tmsOUTTM = M_rstRSSET1.getTimestamp("WB_OUTTM");
					L_dblNETWT = M_rstRSSET1.getDouble("WB_NETWT");
					L_strWOTBY = getUSRNM(nvlSTRVL(M_rstRSSET1.getString("WB_WOTBY"),"")).trim();
					L_strWBRNO = nvlSTRVL(M_rstRSSET1.getString("WB_WBRNO"),"");
				}
				else
				{
					setMSG("Record not found",'E');
					return false;
				}
			}
			else
			{
				L_strCHLNO = nvlSTRVL(M_rstRSSET1.getString("WB_CHLNO"),"");
				L_strORDRF = nvlSTRVL(M_rstRSSET1.getString("WB_ORDRF"),"");
				L_strLRYNO = nvlSTRVL(M_rstRSSET1.getString("WB_LRYNO"),"");
				L_strTPRDS = nvlSTRVL(M_rstRSSET1.getString("WB_TPRDS"),"");
				L_strPRTDS = nvlSTRVL(M_rstRSSET1.getString("WB_PRTDS"),"");
				L_strLOCCD = nvlSTRVL(M_rstRSSET1.getString("WB_LOCCD"),"");
				L_strLOCDS = getLOCDS(L_strLOCCD);
				L_strMATDS = nvlSTRVL(M_rstRSSET1.getString("WB_MATDS"),"");
				L_dblGRSWT = M_rstRSSET1.getDouble("WB_GRSWT");
				L_dblTARWT = M_rstRSSET1.getDouble("WB_TARWT");
				L_tmsINCTM = M_rstRSSET1.getTimestamp("WB_INCTM");
				L_tmsOUTTM = M_rstRSSET1.getTimestamp("WB_OUTTM");
				L_dblNETWT = M_rstRSSET1.getDouble("WB_NETWT");
				L_strWOTBY = getUSRNM(M_rstRSSET1.getString("WB_WOTBY")).trim();
				L_strWBRNO = nvlSTRVL(M_rstRSSET1.getString("WB_WBRNO"),"");
			}

			try
			{
				StringBuffer L_stbPRSTR = new StringBuffer();
				O_FOUT = new FileOutputStream(strREPFL);	
				O_DOUT = new DataOutputStream(O_FOUT);
				try
				{
					String L_strPRTNM = "Supplier     : ";
					if(P_strGINTP.equals(strDESPT) || P_strGINTP.equals(strOTHER))	// Despatch/Other
						L_strPRTNM = "Buyer        : ";
						
					prnFMTCHR(O_DOUT,M_strNOCPI17);
					prnFMTCHR(O_DOUT,M_strCPI10);
					prnFMTCHR(O_DOUT,M_strBOLD);
					O_DOUT.writeBytes("\n\n\n\n" + padSTRING('L',"Weighment Slip",45));
					prnFMTCHR(O_DOUT,M_strNOBOLD);
					L_stbPRSTR.append("\n" + padSTRING('R',"Supreme Petrochem Ltd.",52));
					L_stbPRSTR.append("Report Date : " + cl_dat.M_strLOGDT_pbst);
					L_stbPRSTR.append("\n--------------------------------------------------------------------------------\n");
					if(P_strGINTP.equals(strDESPT))			// Despatch
						L_stbPRSTR.append(padSTRING('R' ,"Ticket No    : " + P_strGINNO,50) + "Loading Adv.: " + L_strORDRF + "\n");
					else
						L_stbPRSTR.append(padSTRING('R',"Ticket No    : " + P_strGINNO,50) + "Challan No. : " + L_strCHLNO + "\n");
						
					if(P_strGINTP.equals(strRAWMT))		// Raw Material
						L_stbPRSTR.append(L_strPRTNM + L_strLOCDS + "\n");
					else
						L_stbPRSTR.append(L_strPRTNM + L_strPRTDS + "\n");
						
					L_stbPRSTR.append(padSTRING('R',"Transporter  : " + L_strTPRDS,50) + "Vehicle No. : " + L_strLRYNO +"\n");
					L_stbPRSTR.append(padSTRING('R',"Material     : " + L_strMATDS,50) + "W/b No.     : " + L_strWBRNO + "\n");
					L_stbPRSTR.append("Gross Weight : " + padSTRING('L',String.valueOf(L_dblGRSWT),10) + padSTRING('L',"In Time     :",38) + " " + M_fmtLCDTM.format(L_tmsINCTM) + "\n");	
					L_stbPRSTR.append("Tare Weight  : " + padSTRING('L',String.valueOf(L_dblTARWT),10) + padSTRING('L',"Out Time    :",38) + " " + M_fmtLCDTM.format(L_tmsOUTTM) + "\n");
					L_stbPRSTR.append("Net Weight   : " + padSTRING('L',String.valueOf(L_dblNETWT),10));
					L_stbPRSTR.append("\n--------------------------------------------------------------------------------\n\n\n\n");
					L_stbPRSTR.append("( " + L_strWOTBY + " )" + "\n");
					L_stbPRSTR.append(" Weigh Bridge                                           Security Officer\n\n\n");
					O_DOUT.writeBytes(L_stbPRSTR.toString());
					prnFMTCHR(O_DOUT,M_strEJT);	
				}
				catch(IOException e)
				{
					setMSG(e,"Erron in writing to the file");
					return false;
				}		
				finally
				{
					O_DOUT.close();
					O_FOUT.close();
				}
			}
			catch(FileNotFoundException e)
			{
				setMSG("File "+strREPFL +" not found",'E');
				return false;
			} 
				
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();
		}
		catch(Exception e)
		{
			setMSG(e,"Error in getALLREC : ");
			return false;
		}
		return true;
	}
		
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		
		String L_ACT = L_AE.getActionCommand();
		if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
			exeHLPOK();
		else if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			
		}
		else if(M_objSOURC == txtGINTP){
			txtGINNO.setText("");
			txtGINNO.requestFocus();
		}
	}
	public void keyPressed(KeyEvent L_KE){
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == 9 || L_KE.getKeyCode() == KeyEvent.VK_ENTER)
		{ 
			if (M_objSOURC == cl_dat.M_btnHLPOK_pbst)
					exeHLPOK();		
			else if(M_objSOURC == txtGINNO){				// Gate-In No.
				strGINTP = txtGINTP.getText().trim();
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
		if (L_KE.getKeyCode()== L_KE.VK_F1){					//F1 starts from here
			if(M_objSOURC == txtGINNO)
			{
				strGINTP = txtGINTP.getText().trim();
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtGINNO";
				String L_ARRHDR[] = {"Sr.No.","Gate-In No.","Lorry No","In Time","By","Out Time","By"};
				M_strSQLQRY = "Select WB_SRLNO,WB_DOCNO,WB_LRYNO,WB_INCTM,WB_WINBY,WB_OUTTM,WB_WOTBY";
				M_strSQLQRY += "  from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL not in ('X','0','1','2','3')";
				M_strSQLQRY += " and WB_DOCTP = '" + strGINTP + "'";
				M_strSQLQRY += " order by WB_INCTM,WB_DOCNO,WB_SRLNO desc";
			
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,7,"CT");
			}
			if(M_objSOURC == txtGINTP)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtGINTP";
				String L_ARRHDR[] = {"Gate In Type","Desciption"};
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'";
				M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
				if(M_strSQLQRY != null)
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
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
			if(M_strHLPFLD.equals("txtGINTP"))
			{
				cl_dat.M_flgHELPFL_pbst = false;
				txtGINTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"exeHLPOK");
		}
	}
	// Method to get the help on Gate-In No.
	private void hlpGINNO(String P_strGINTP){
		String L_MEMNO;
		
		try{
			cl_dat.M_flgHELPFL_pbst = true;
			M_strHLPFLD = "txtGINNO";
			String L_ARRHDR[] = {"Sr.No.","Gate-In No.","Lorry No","In Time","By","Out Time","By"};
			M_strSQLQRY = "Select WB_SRLNO,WB_DOCNO,WB_LRYNO,WB_INCTM,WB_WINBY,WB_OUTTM,WB_WOTBY";
            M_strSQLQRY += "  from MM_WBTRN where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL not in ('X','0','1','2','3')";
			M_strSQLQRY += " and WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " order by WB_INCTM,WB_DOCNO,WB_SRLNO desc";
			
			if(M_strSQLQRY != null)
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,7,"CT");
		}catch(Exception L_EX){
			setMSG(L_EX,"hlpGINNO");
		}										  
	}
	
	// Method to validate the Gate-In No.
	private boolean vldGINNO(String P_strGINTP,String P_strGINNO){
		try{
			M_strSQLQRY = "Select WB_DOCNO from MM_WBTRN ";
            M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL not in ('X','0','1','2','3','N')";
			M_strSQLQRY += " and WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			
			M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(!M_rstRSSET1.next()){
				M_strSQLQRY = "Select WB_DOCNO from MM_WBTRN ";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_STSFL = 'N'";
				M_strSQLQRY += " and WB_DOCTP = '" + P_strGINTP + "'";
				M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
					
				M_rstRSSET1 = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET1.next()){
					M_rstRSSET1.close();
					return true;
				}
				else{
					M_rstRSSET1.close();
					return false;
				}
			}
			else{
				M_rstRSSET1.close();
				return true;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"vldGINNO");
		}
		return false;
	}

	// Method to get the User Name from the MSTDATA/CO_USMST
	private String getUSRNM(String P_strUSRCD){
		String L_strUSRNM = P_strUSRCD;
		try{
			M_strSQLQRY = "Select US_USRNM from CO_USMST";
			M_strSQLQRY += " where US_USRCD = '" + P_strUSRCD + "'";
		
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		
			if(M_rstRSSET.next())
				L_strUSRNM = M_rstRSSET.getString("US_USRNM");
		
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception e){
			setMSG(e,"Error in getUSRNM");
		}
		return L_strUSRNM;
	}
	
	// Get the Location Description
	private String getLOCDS(String P_strLOCCD)
	{
		String L_strLOCDS = "";
		
		try{
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'QC11TKL' and ";
			M_strSQLQRY += " CMT_CODCD = '" + P_strLOCCD + "'";
            	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			if(M_rstRSSET.next())
				L_strLOCDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"getLOCDS");
			return L_strLOCDS;
		}	
		return L_strLOCDS;
	}
	
	// Method to print the Weigh-Bridge In/Out time on Loading Advice
	private boolean prnWBRTM(String P_strGINTP,String P_strGINNO)
	{
		java.sql.Timestamp L_tmsINCTM,L_tmsOUTTM;
		double L_dblGRSWT,L_dblNETWT,L_dblTARWT;
		try
		{
			M_strSQLQRY = "Select WB_GRSWT,WB_TARWT,WB_NETWT,WB_INCTM,WB_OUTTM,WB_WBRNO";
			M_strSQLQRY += " from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP + "'";
			M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET1.next())
			{
				L_dblGRSWT = M_rstRSSET1.getDouble("WB_GRSWT");
				L_dblTARWT = M_rstRSSET1.getDouble("WB_TARWT");
				L_dblNETWT = M_rstRSSET1.getDouble("WB_NETWT");
				L_tmsINCTM = M_rstRSSET1.getTimestamp("WB_INCTM");
				L_tmsOUTTM = M_rstRSSET1.getTimestamp("WB_OUTTM");
				L_strWBRNO = M_rstRSSET1.getString("WB_WBRNO");
                FileOutputStream L_FOUT = new FileOutputStream("LPT1:"); 
				PrintWriter prnWRITER = new PrintWriter(L_FOUT,true);
				try
				{
					StringBuffer L_PRNSTR = new StringBuffer("  ");
					L_PRNSTR.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");  // 27 lines
					L_PRNSTR.append(padSTRING('L',"",75) + " W/B In Time  : " + padSTRING('R',M_fmtLCDTM.format(L_tmsINCTM),25) + "Tare Weight  : " + setNumberFormat(L_dblTARWT,3) + "\n");
					L_PRNSTR.append(padSTRING('L',"",75) + " W/B Out Time : " + padSTRING('R',M_fmtLCDTM.format(L_tmsOUTTM),25) + "Gross Weight : " + setNumberFormat(L_dblGRSWT,3) + "\n");
					L_PRNSTR.append(padSTRING('L',"",75) + " W/B No.      : " + padSTRING('R',L_strWBRNO,25) + "Net Weight   : " + L_dblNETWT+ "\n");
					L_PRNSTR.append("  ");
					prnWRITER.print(L_PRNSTR.toString());
				}
				catch(Exception e)
				{
					setMSG(e,"Erron in writing to the file ");
					return false;
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
				return false;
			}
			
			if(M_rstRSSET1 != null)
					M_rstRSSET1.close();
			
		}catch(Exception e){
			setMSG(e,"Error in prnWBRTM : ");
			return false;
		}
		return true;
	}
	boolean vldDATA(String P_strOPTTP,String P_strGINTP,String P_strGINNO,String P_strOPT)
	{
		if(P_strGINTP.trim().length() ==0)
		{
			if(P_strOPT.equals("INT"))
			{
				txtGINTP.requestFocus();
				setMSG("Please, enter a valid Gate-in Type. Press F1 for help.",'E');
				return false;
			}
		}
		else if(P_strGINNO.trim().length() ==0)
		{
			if(P_strOPT.equals("INT"))
			{
				txtGINNO.requestFocus();
				setMSG("Please, enter a valid Gate-in No. Press F1 for help.",'E');
				return false;
			}
		}
		else if(P_strOPTTP.equals("LA"))
		{
			if(P_strGINTP.equals("01"))
				setMSG("Please select a valid category..",'E');
			return false;
		}
		
		else if(!vldGINNO(P_strGINTP,P_strGINNO))
		{
			if(P_strOPT.equals("INT"))
			{
				txtGINNO.requestFocus();
				setMSG("Please, enter valid Gate-In No. Press F1 for help.",'E');
				return false;
			}
		}
		if(P_strOPT.equals("INT"))
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPFIL_pbst))
			{
				if(M_txtDESTN.getText().trim().length() ==0)
				{
					setMSG("Please Enter a File Name ..",'E'); 
					return false;
				}
				else
					strREPFL = "c:\\reports\\"+M_txtDESTN.getText().trim()+".doc";
			}
			else
				strREPFL = "c:\\reports\\mm_rpwbt.doc";
		}
		else
				strREPFL = "c:\\reports\\mm_rpwbt.doc";
		return true;
	}
	void exePRINT()
	{
		strGINTP = txtGINTP.getText().trim();
		strGINNO = txtGINNO.getText().toString().trim();
		if(btnTKTNO.isSelected())
			crtREPT("TKT",strGINTP,strGINNO,strSRLNO,"EXT");
		else
			crtREPT("LA",strGINTP,strGINNO,strSRLNO,"INT");
	}
	public boolean crtREPT(String P_strRPOPT,String P_strGINTP,String P_strGINNO,String P_strSRLNO,String P_strOPT)
	{
		boolean L_flgRPT = false;
		if(!vldDATA(P_strRPOPT,P_strGINTP,P_strGINNO,P_strOPT))
		{
			//setMSG("Invalid Data..",'E');
			return false;
		}
		if(P_strRPOPT.equals("TKT"))	// Ticket printing
		{
			L_flgRPT = getALLREC(P_strGINTP,P_strGINNO,P_strSRLNO);
		}
		else
		{
			// Print the WeighBridge In/Out Time on Loading Advidce Paper
			if(P_strGINTP.equals(strDESPT)){	// Despatch
				JOptionPane.showMessageDialog(this,"Insert Loading Advice Paper in Printer","WeighBridge In/Out Time",JOptionPane.INFORMATION_MESSAGE); 
				L_flgRPT =  prnWBRTM(P_strGINTP,P_strGINNO);
				//return ;
			}
		}
		if(!L_flgRPT)
			return false;
		if(P_strOPT.equals("INT")) // internal report
	    {
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPSCN_pbst))
			{
				Runtime r = Runtime.getRuntime();
					Process p = null;
					try
					{
						p  = r.exec("c:\\windows\\wordpad.exe "+strREPFL); 
					}
					catch(IOException L_EX)
					{
						setMSG(L_EX,"Error.exescrn.....");
 					}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPPRN_pbst))
			{
				doPRINT(strREPFL);	
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
					
			}
		}
		else
		{
			doPRINT(strREPFL);
		}
		return true;
	}
}
