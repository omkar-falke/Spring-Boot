/*
System Name   : Material Management System
Program Name  : Gate-In Details

Program Desc. :
Author        : N.K.Virdi
Date          : 28/11/2003
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
import java.util.Date; 
import java.util.Calendar;
import java.io.File; 
import java.io.FileOutputStream;
import java.io.DataOutputStream;

class mm_rpgio extends cl_rbase{
	
	private JComboBox cmbGINTP;
	private TxtDate txtFMDAT, txtTODAT;
	private TxtTime txtFMTIM, txtTOTIM;
	private String strREPFL =cl_dat.M_strREPSTR_pbst+"mm_rpgio.doc"; 
	private String strFMDTM,strTODTM,strCRDTM,strGINTP,strGINNO,strOGINNO,strSTATS;
	private String strQTYNM = "Chalan ";	
	private int intRECCNT,intSRLNO,intLINNO,intPAGNO;
	private String strPRINT,strTOTQT = "0";

	final String strDEFTM = " 07:00";
	final String strRAWMT = "01";
	final String strDESPT = "03";
	ResultSet rstRSSET;

	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;
	
	mm_rpgio()
	{
		super(2);
		try
		{	
			setMatrix(20,6);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			add(new JLabel("Gate-In Type"),1,4,1,1,this,'L');
			add(cmbGINTP = new JComboBox(),2,4,1,1,this,'L');
			
			add(new JLabel("From Date-Time"),1,5,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),2,5,1,.6,this,'L');
			add(txtFMTIM = new TxtTime(),2,5,1,.4,this,'R');
			
			add(new JLabel("To Date-Time"),1,6,1,1,this,'L');
			add(txtTODAT = new TxtDate(),2,6,1,.6,this,'L');
			add(txtTOTIM = new TxtTime(),2,6,1,.4,this,'R');
			
					
			revalidate();
			updateUI();
			String L_strTEMP="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXWBT'";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				L_strTEMP = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
				cmbGINTP.addItem(L_strTEMP);
			}
			txtFMDAT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
			txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
			txtFMTIM.setText(strDEFTM);
			txtTOTIM.setText(strDEFTM);
			txtFMDAT.requestFocus();
		}
		catch(Exception L_EX)
		{
			System.out.println(L_EX.toString());
		}	
	}
	void setENBL(boolean P_flgSTAT)
	{
		cmbGINTP.setEnabled(P_flgSTAT);
		txtFMDAT.setEnabled(P_flgSTAT);
		txtTODAT.setEnabled(P_flgSTAT);
		txtFMTIM.setEnabled(P_flgSTAT);
		txtTOTIM.setEnabled(P_flgSTAT);
	}
	public void actionPerformed(ActionEvent L_AE){
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				M_vtrSCCOMP.remove(M_lblFMDAT);
				M_vtrSCCOMP.remove(M_lblTODAT);
				M_vtrSCCOMP.remove(M_txtTODAT);
				M_vtrSCCOMP.remove(M_txtFMDAT);
		
				M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
				M_calLOCAL.add(Calendar.DATE,-1);
				txtFMDAT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				txtTODAT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
				txtFMTIM.setText(strDEFTM);
				txtTOTIM.setText(strDEFTM);
				//txtFMDAT.requestFocus();
				//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
					cmbGINTP.requestFocus();
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"actionp");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER){
			if(M_objSOURC == cmbGINTP){
				txtFMDAT.requestFocus();
			}
			if(M_objSOURC == txtFMDAT){
				if(txtFMDAT.getText().trim().length() > 0){
					String L_strTIME = txtFMTIM.getText().trim();
					txtFMTIM.setText("");
					txtFMTIM.requestFocus();
					txtFMTIM.setText(L_strTIME);
				}
				else{
					txtFMDAT.requestFocus();
					setMSG("Enter Date In DD/MM/YYYY Format",'E');
				}
			}
			if(M_objSOURC == txtFMTIM){
				if(txtFMTIM.getText().trim().length() > 0){
					txtTODAT.requestFocus();
				}
				else{
					txtFMTIM.requestFocus();
					setMSG("Enter Time In HH:MM(24 Hour) Format..",'E');
						   
				}
			}
			if(M_objSOURC == txtTODAT){
				if(txtTODAT.getText().trim().length() > 0){
					String L_strTIME = txtTOTIM.getText().trim();
					txtTOTIM.setText("");
					txtTOTIM.requestFocus();
					txtTOTIM.setText(L_strTIME);
				}
				else{
					txtTODAT.requestFocus();
					setMSG("Enter Date In DD/MM/YYYY Format",'E');
				}
			}
			if(M_objSOURC == txtTOTIM){
				if(txtTOTIM.getText().trim().length() > 0){
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
				else{
					txtTOTIM.requestFocus();
					setMSG("Enter Time In HH:MM(24 Hour) Format..",'E');
				}
			}
			setMSG("",'N');
		}
	}
	private void prnHEADER()
	{  
		try
		{
			if(strGINTP.equals(strDESPT))
			{
				strSTATS ="Out";
			}
			else
				strSTATS ="In";
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",56));
			O_DOUT.writeBytes(padSTRING('L',"Report Date : "+cl_dat.M_strLOGDT_pbst,177));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"Gate Entry Details for " + String.valueOf(cmbGINTP.getSelectedItem()).substring(2),209));
			O_DOUT.writeBytes("Page No.    :" + padSTRING('L',String.valueOf(intPAGNO),10) + "\n");
			O_DOUT.writeBytes(strSTATS + " Vehicles from " + strFMDTM + " to " + strTODTM);
			O_DOUT.writeBytes("\n");
			intLINNO += 6;
			crtLINE(233);
			if(strGINTP.equals(strDESPT)){					// Despatch
				O_DOUT.writeBytes("\nSr.   Gate-In    Vehicle No.   Transporter                  Gate-In Time       Consignee                                    Invoice No.    Invoice        Quantity   Commodity                 Gate-Out time      Gate   Remark");
				O_DOUT.writeBytes("\nNo.   No.                                                                                                                                  Date              in MT                                                I/C\n");
			}
			else{
				O_DOUT.writeBytes("\nSr.   Gate-In    Vehicle No.   Transporter                  Gate-In Time       WeighBridge        Consignee                 Chalan No.     Chalan         Quantity   Commodity                 Gate-Out time      Gate   Remark");
				O_DOUT.writeBytes("\nNo.   No.                                                                      In Time                                                     Date              in MT                                                I/C\n");
			}
			crtLINE(233);
			O_DOUT.writeBytes("\n");
			intLINNO += 4;
		}
		catch(Exception L_exEXCP)
		{
			setMSG(L_exEXCP,"prnhead......");
		}
	}
	private void crtLINE(int P_intCHCNT)
	{
		String L_strLIN = "";
		try
		{
			for(int i=1;i<=P_intCHCNT;i++)
			{
				 L_strLIN += "-";
			}
			O_DOUT.writeBytes(L_strLIN);
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}
	private void getALLREC(String P_strGINTP,String P_strFMDTM,String P_strTODTM)
	{
		String strLRYNO,strTPRDS,strGINDT="",strINCTM="",strPRTDS,strLOCCD,strGOTBY;
		String strORDNO,strORDDT ="",strMATDS,strORDQT,strGOTDT,strREMGT;
	
		java.sql.Timestamp L_tmsTEMP;
		java.sql.Date L_datTEMP;
		try
		{
			if(P_strGINTP.equals(strDESPT))		//strDESPT Gate In Type
				strQTYNM = "Invoice";
			else
				strQTYNM = "Chalan ";
			double L_dblTOTQT = 0;
			strTOTQT = "0";
			intRECCNT = 0;
			
			P_strFMDTM = M_fmtDBDTM.format(M_fmtLCDTM.parse(P_strFMDTM));
			P_strTODTM = M_fmtDBDTM.format(M_fmtLCDTM.parse(P_strTODTM));
			
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			O_FOUT = new FileOutputStream(strREPFL);	
			O_DOUT = new DataOutputStream(O_FOUT);
			prnFMTCHR(O_DOUT,M_strCPI17);
			intLINNO = 0;
			intPAGNO = 1;
			prnHEADER(); 
			
			if(P_strGINTP.equals(strDESPT))
			{
				M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_TPRDS,WB_GINDT,WB_INCTM,WB_PRTDS,";
				M_strSQLQRY += "WB_LOCCD,IVT_INVNO L_ORDNO,CONVERT(varchar,IVT_INVDT,103) L_ORDDT,WB_MATDS,";
				M_strSQLQRY += "isnull(IVT_INVQT,0) L_ORDQT,WB_GOTDT,WB_REMGT,";
				M_strSQLQRY += "WB_STSFL,WB_GOTBY from MM_WBTRN,MR_IVTRN ";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP  + "'";
				M_strSQLQRY += " and IVT_GINNO = WB_DOCNO and IVT_CMPCD = WB_CMPCD ";
				M_strSQLQRY += " and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL <> 'X'";
				M_strSQLQRY += " and WB_GOTDT between '" + P_strFMDTM + "' and '" + P_strTODTM+"'";
				M_strSQLQRY += " and WB_STSFL = '9'";
				strSTATS = "Out";
			}
			else
			{
				M_strSQLQRY = "Select WB_DOCNO,WB_LRYNO,WB_TPRDS,WB_GINDT,WB_INCTM,WB_PRTDS,";
				M_strSQLQRY += "WB_LOCCD,WB_CHLNO L_ORDNO,WB_CHLDT L_ORDDT,WB_MATDS,";
				M_strSQLQRY += "isnull(WB_CHLQT,0) L_ORDQT,WB_GOTDT,WB_REMGT,";
				M_strSQLQRY += "WB_STSFL,WB_GOTBY from MM_WBTRN ";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + P_strGINTP  + "'";
				M_strSQLQRY += " and WB_GINDT between '" + P_strFMDTM + "'"+" and '" + P_strTODTM +"'";
				M_strSQLQRY += " and isnull(WB_STSFL,'') <> 'X'";
				strSTATS = "In";
			}
			M_strSQLQRY += " order by WB_DOCNO,WB_GINDT";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			strOGINNO = "";
			while(M_rstRSSET.next())
			{
				intSRLNO = intRECCNT + 1;
				strGINNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"").trim();
				strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"").trim();
				strTPRDS = (nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"").trim() + "                           ").substring(0,26);
				
				L_tmsTEMP = M_rstRSSET.getTimestamp("WB_GINDT");
				if(L_tmsTEMP !=null)
					strGINDT = M_fmtLCDTM.format(L_tmsTEMP);	
				
				L_tmsTEMP = M_rstRSSET.getTimestamp("WB_INCTM");
				if(L_tmsTEMP !=null)
					strINCTM = M_fmtLCDTM.format(L_tmsTEMP);	
				
				if(P_strGINTP.equals(strRAWMT))
				{
					strLOCCD = nvlSTRVL(M_rstRSSET.getString("WB_LOCCD"),"").trim();
					strPRTDS = (getLOCDS(strLOCCD) + "                        ").substring(0,23);
				}
				else
					strPRTDS = nvlSTRVL(M_rstRSSET.getString("WB_PRTDS"),"").trim();
				
				strORDNO = nvlSTRVL(M_rstRSSET.getString("L_ORDNO"),"-").trim();
				L_datTEMP = M_rstRSSET.getDate("L_ORDDT");
				if(L_datTEMP !=null)
					strORDDT = M_fmtLCDAT.format(L_datTEMP);	
				
				strMATDS = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"").trim();
				//setFMT is deprecated
				//****strORDQT = setFMT(nvlSTRVL(M_rstRSSET.getString("L_ORDQT"),"0").trim(),3);
				strORDQT = setNumberFormat(M_rstRSSET.getDouble("L_ORDQT"),3).toString();
				
				//strGOTDT = cc_dattm.setDTMFMT(nvlSTRVL(M_rstRSSET.getString("WB_GOTDT"),"").trim());
				L_tmsTEMP = M_rstRSSET.getTimestamp("WB_GOTDT");
				if(L_tmsTEMP !=null)
				{
					strGOTDT = M_fmtLCDTM.format(L_tmsTEMP);
				}
				else 
					strGOTDT = "";
				strREMGT = (nvlSTRVL(M_rstRSSET.getString("WB_REMGT"),"").trim() + "              ").substring(0,13);
				strGOTBY = nvlSTRVL(M_rstRSSET.getString("WB_GOTBY"),"").trim();
				L_dblTOTQT += Double.parseDouble(strORDQT);
			
				if(intLINNO > 66)
				{
					crtLINE(233);
					prnFMTCHR(O_DOUT,M_strEJT);				
					prnFMTCHR(O_DOUT,M_strCPI17);
					intLINNO = 0;
					intPAGNO += 1;
					prnHEADER(); 
				}
				if(!strGINNO.equals(strOGINNO))
				{
					strPRINT = padSTRING('L',String.valueOf(intSRLNO),3);	// Sr No
					strPRINT += "   ";			//Blank Space
					strPRINT += padSTRING('R',strGINNO,11);	//Gate In Number
					strPRINT += padSTRING('R',strLRYNO,14);	//Vehicle Number
					strPRINT += padSTRING('R',strTPRDS,29);	//Transpoprater Name
					strPRINT += padSTRING('R',strGINDT,19);	//Gate In Date And Time
					if(!P_strGINTP.equals(strDESPT)){
						strPRINT += padSTRING('R',strINCTM,19);	//Way Bridge In Time 
						strPRINT += padSTRING('R',strPRTDS,26);	//Party name 
					}
					else
						strPRINT += padSTRING('R',strPRTDS,45);
				}
				else
					strPRINT = padSTRING('R',"",124);
				strPRINT += padSTRING('R',"",1);
				strPRINT += padSTRING('R',strORDNO,14);		//Order Number(Challan)
				strPRINT += padSTRING('R',strORDDT,12);		//Order Date(Challan)
				strPRINT += padSTRING('L',strORDQT,11);		//Order Quantity (Challan)
				if(!strGINNO.equals(strOGINNO)){
					strOGINNO = strGINNO;		//Gate In Number
					strPRINT += "   ";			//Blank spaces
					strPRINT += padSTRING('R',strMATDS,26);	//Material Description
					strPRINT += padSTRING('R',strGOTDT,19);	//Gate Out Date And Time
					strPRINT += padSTRING('R',strGOTBY,7);	//Gate Out By
					strPRINT += padSTRING('R',strREMGT,13);	//Gate Out Remark
					intRECCNT++;
				}
				strPRINT += "\n";
				O_DOUT.writeBytes(strPRINT);
				intLINNO++;
			}
			//setFMT is deprecated
			//****strTOTQT = setFMT(String.valueOf(L_dblTOTQT),3);
			strTOTQT = setNumberFormat(L_dblTOTQT,3);
			if(intRECCNT > 0)
				prnFOOTR();
			O_DOUT.close();
			O_FOUT.close();
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			System.out.println(intRECCNT);
			setMSG("",'N');
		}catch(Exception L_EX){
			setMSG(L_EX,"getALLREC");
		}
		this.setCursor(cl_dat.M_curDFSTS_pbst);
	}
	
	private void prnFOOTR()
	{
		try
		{
			O_DOUT.writeBytes ("\n");
			crtLINE(233);
			if(strGINTP.equals(strDESPT))					// Despatch
				O_DOUT.writeBytes("\n" + padSTRING('L',"Total Qty : ",152) + padSTRING('L',strTOTQT,10));
			else
				O_DOUT.writeBytes("\n" + padSTRING('L',"Total Qty : ",152) + padSTRING('L',strTOTQT,10));
			
			O_DOUT.writeBytes ("\n\n");
			O_DOUT.writeBytes ("\n");
				
			intLINNO += 5;
			prnFMTCHR(O_DOUT,M_strEJT);				
			prnFMTCHR(O_DOUT,M_strCPI17);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
	private String getLOCDS(String P_strLOCCD)
	{
		String L_strLOCDS = "";
		try
		{
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'COXXDST' and ";
			M_strSQLQRY += " CMT_CODCD = '" + P_strLOCCD + "'";
            rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(rstRSSET.next())
				L_strLOCDS = rstRSSET.getString("CMT_CODDS");
			
			if(rstRSSET != null)
				rstRSSET.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getLOCDS");
			return L_strLOCDS;
		}	
		return L_strLOCDS;
	}
	void exePRINT()
	{
		try
		{
			strGINTP = cmbGINTP.getSelectedItem().toString().substring(0,2);
			strFMDTM = txtFMDAT.getText().trim() +" "+txtFMTIM.getText().trim();
			strTODTM = txtTODAT.getText().trim() +" "+txtTOTIM.getText().trim();
			if(vldDATA())
			{
				getALLREC(strGINTP,strFMDTM,strTODTM); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					doPRINT(strREPFL);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
					if(intRECCNT > 0)
					{
						Runtime r = Runtime.getRuntime();
						Process p = null;
						try
						{
							p  = r.exec("c:\\windows\\wordpad.exe "+strREPFL); 
						}catch(Exception L_EX){
							setMSG(L_EX,"Error.exescrn.. ");
 						}
					}
					else
							setMSG("Record could not be found",'E');	
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst))
				{
					if(intRECCNT > 0)
					{
						if(M_txtDESTN.getText().length()>0)
							setMSG("File Copied to file:"+M_txtDESTN.getText().trim()+"",'N');
						else
							setMSG("File Copied to file:c:\\reports\\mm_rprct.doc",'N');
					}
					else
						setMSG("Record could not be found",'E');
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error in exePRINT ");
		}
	}
	boolean vldDATA()
	{
		try
		{
			strFMDTM = txtFMDAT.getText().trim()+" "+txtFMTIM.getText().trim();
			strTODTM = txtTODAT.getText().trim()+" "+txtTOTIM.getText().trim();
			strCRDTM = cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText();
			if(txtFMDAT.getText().length()==0)
			{
				setMSG("Please Enter Start date",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			else if(txtFMTIM.getText().length()==0)
			{
				setMSG("Please Enter From Time",'E');
				txtFMTIM.requestFocus();
				return false;
			}
			else if(txtTODAT.getText().length()==0)
			{
				setMSG("Please Enter End date",'E');
				txtTODAT.requestFocus();
				return false;
			}
			else if(txtTOTIM.getText().length()==0)
			{
				setMSG("Please Enter From date",'E');
				txtTOTIM.requestFocus();
				return false;
			}
			else if(M_fmtLCDTM.parse(strFMDTM).compareTo(M_fmtLCDTM.parse(strCRDTM))> 0)
			{
				setMSG("From Date-time can not be greater than Current date time..",'E');
				return false;
			}
			else if(M_fmtLCDTM.parse(strTODTM).compareTo(M_fmtLCDTM.parse(strCRDTM))> 0)
			{
				setMSG("To Date-time can not be greater than Current date time..",'E');
				return false;
			}
			else if(M_fmtLCDTM.parse(strTODTM).compareTo(M_fmtLCDTM.parse(strFMDTM))< 0)
			{
				setMSG("To Date can not be smaller than From date..",'E');
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
}