/*
System Name   : Material Management System
Program Name  : Styrene Monomer Tanker Status

Program Desc. :
Author        : N.K.Virdi
Date          : 29th Nov 2003
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
import java.io.DataOutputStream;
import java.util.Calendar;

class mm_rpsms extends cl_rbase{
	private JTextField txtFMDAT,txtTODAT,txtFMTIM,txtTOTIM;
	private String strREPFL = cl_dat.M_strREPSTR_pbst+"mm_rpsms.doc";
	private String strFMDTM,strTODTM,strGINNO,strCRDTM;
	private int intRECCT,intSRLNO,intLINNO,intPAGNO;
	double dblCHLVL,dblNETVL,dblDIFVL;	
	final String strGINTP = "01";
	final String strMATCD = "6805010045";				// Material Code for Styrene
	final String strDEFTM = " 07:00";
	

	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;

	mm_rpsms()
	{
		super(2);
		setMatrix(20,6);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			
			add(new JLabel("From Date-Time"),1,5,1,1,this,'L');
			add(txtFMDAT = new TxtDate(),2,5,1,.6,this,'L');
			add(txtFMTIM = new TxtTime(),2,5,1,.4,this,'R');
			
			add(new JLabel("To Date-Time"),1,6,1,1,this,'L');
			add(txtTODAT = new TxtDate(),2,6,1,.6,this,'L');
			add(txtTOTIM = new TxtTime(),2,6,1,.4,this,'R');
			
			revalidate();
			updateUI();
	
		//	txtFMDTM.setText(cc_dattm.getABDATE(cl_dat.M_LOGDAT,1,'B') + strDEFTM);
		//	txtTODTM.setText(cl_dat.M_LOGDAT + strDEFTM);

	}
	public void actionPerformed(ActionEvent L_AE)
	{
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
				txtFMDAT.requestFocus();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"actionP");
		}

	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
	}
	private void prnHEADER()
	{  
		try
		{
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"SUPREME PETROCHEM LTD.",56));
			O_DOUT.writeBytes(padSTRING('L',"Report Date : "+cl_dat.M_strLOGDT_pbst,100));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',"Styrene Monomer Tanker Status : from " + strFMDTM + " to " + strTODTM,132));
			O_DOUT.writeBytes("Page No.    : " + padSTRING('L',String.valueOf(intPAGNO),10) + "\n");
			intLINNO += 4;
			crtLINE(156);
			O_DOUT.writeBytes("\nSr.  Gate-In   Tanker No.   Transporter               Chalan  Chalan      Net   Short.   Gate-In Time       Plant-In Time      Gate-Out Time      Loading");
			O_DOUT.writeBytes("\nNo.  No.                                              No.        Qty      Qty     Qty                                                             Date\n");
			crtLINE(156);
			O_DOUT.writeBytes("\n");
			intLINNO += 4;
	
		}catch(Exception L_EX){
			System.out .println ("L_EX..prnhead....... :"+L_EX);
		}
	}
	private void crtLINE(int P_intCNT)
	{
		String L_strLIN = "";
		try
		{
			for(int i=1;i<=P_intCNT;i++){
			L_strLIN += "-";
			}
			O_DOUT.writeBytes(L_strLIN);
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}
	private void getALLREC(String P_strFMDTM,String P_strTODTM)
	{ 
		try
		{
			java.sql.Timestamp L_tmsTEMP;
			StringBuffer L_stbPRINT;

			String L_strCHLNO,L_strLRYNO,L_strTPRDS;
			String L_strCHLDT="",L_strGINDT="",L_strGOTDT="",L_strINCTM="";
			String L_strCHLQT,L_strDIFQT,L_strNETWT;
			dblCHLVL = dblNETVL = dblDIFVL = 0;
			intRECCT = 0;
			P_strFMDTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(P_strFMDTM))+"'";
			P_strTODTM = "'"+M_fmtDBDTM.format(M_fmtLCDTM.parse(P_strTODTM))+"'";
			
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Report Generation in Process.......",'N');
			O_FOUT = new FileOutputStream(strREPFL);	
			O_DOUT = new DataOutputStream(O_FOUT);
			
			prnFMTCHR(O_DOUT,M_strCPI17);
			intPAGNO = 1;
			intLINNO = 0;
			prnHEADER(); //gets the Header of the report
			
			M_strSQLQRY = "Select	WB_DOCNO,WB_CHLNO,WB_LRYNO,WB_TPRDS,WB_CHLQT,WB_NETWT,";
			M_strSQLQRY += "WB_GINDT,WB_INCTM,WB_GOTDT,WB_CHLDT";
			M_strSQLQRY += " from MM_WBTRN ";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
			M_strSQLQRY += " and WB_MATCD = '" + strMATCD + "'";
			M_strSQLQRY += " and WB_GINDT between " + P_strFMDTM + " and " + P_strTODTM;
			M_strSQLQRY += " and WB_STSFL <> 'X'";
			M_strSQLQRY += " order by WB_DOCNO,WB_GOTDT";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			
			L_stbPRINT = new StringBuffer("");
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				intSRLNO = intRECCT + 1;
				strGINNO = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"");
				L_strCHLNO = nvlSTRVL(M_rstRSSET.getString("WB_CHLNO"),"");
				L_strLRYNO = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"");
				L_strTPRDS = (nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"")+ "                         ").substring(0,24);
				
				L_strCHLQT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0").trim()),3);
				dblCHLVL += Double.parseDouble(L_strCHLQT);
				
				L_strNETWT = setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("WB_NETWT"),"0").trim()),3);
				dblNETVL += Double.parseDouble(L_strNETWT);
				
				L_strDIFQT = setNumberFormat(Double.parseDouble(String.valueOf(Double.parseDouble(L_strNETWT) - Double.parseDouble(L_strCHLQT))),3);
				dblDIFVL += Double.parseDouble(L_strDIFQT);
				
				L_tmsTEMP = M_rstRSSET.getTimestamp("WB_GINDT");
				if(L_tmsTEMP !=null)
					L_strGINDT = M_fmtLCDTM.format(L_tmsTEMP);
				
				L_tmsTEMP = M_rstRSSET.getTimestamp("WB_INCTM");
				if(L_tmsTEMP !=null)
					L_strINCTM = M_fmtLCDTM.format(L_tmsTEMP);
				
				L_tmsTEMP = M_rstRSSET.getTimestamp("WB_GOTDT");
				if(L_tmsTEMP !=null)
					L_strGOTDT = M_fmtLCDTM.format(L_tmsTEMP);
				
				L_tmsTEMP = M_rstRSSET.getTimestamp("WB_CHLDT");
				if(L_tmsTEMP !=null)
					L_strCHLDT = M_fmtLCDTM.format(L_tmsTEMP);
				
				if(L_stbPRINT.length() != 0)
					L_stbPRINT.delete(0,L_stbPRINT.length());
				
				L_stbPRINT.append(padSTRING('L',String.valueOf(intSRLNO),3));
				L_stbPRINT.append("  ");
				L_stbPRINT.append(padSTRING('R',strGINNO,10));
				L_stbPRINT.append(padSTRING('R',L_strLRYNO,13));
				L_stbPRINT.append(padSTRING('R',L_strTPRDS,26));
				L_stbPRINT.append(padSTRING('R',L_strCHLNO,7));
				L_stbPRINT.append(padSTRING('L',L_strCHLQT,7));
				L_stbPRINT.append(padSTRING('L',L_strNETWT,9));
				L_stbPRINT.append(padSTRING('L',L_strDIFQT,9));
				L_stbPRINT.append("   ");
				L_stbPRINT.append(padSTRING('R',L_strGINDT,19));
				L_stbPRINT.append(padSTRING('R',L_strINCTM,19));
				L_stbPRINT.append(padSTRING('R',L_strGOTDT,19));
				L_stbPRINT.append(padSTRING('R',L_strCHLDT,10));
				L_stbPRINT.append("\n");
				intRECCT++;
				O_DOUT.writeBytes(L_stbPRINT.toString());
				
				intLINNO++;
				if(intLINNO > 66){
					crtLINE(156);
					prnFMTCHR(O_DOUT,M_strEJT);				
					prnFMTCHR(O_DOUT,M_strCPI17);
					intLINNO = 0;
					intPAGNO += 1;
					prnHEADER(); //gets the Header of the report
				}
			}
			
			if(L_stbPRINT.length() != 0)
				L_stbPRINT.delete(0,L_stbPRINT.length());
			
			if(intLINNO <  60){
				prnFOOTR();
			}
			else{
				crtLINE(156);
				prnFMTCHR(O_DOUT,M_strEJT);				
				prnFMTCHR(O_DOUT,M_strCPI17);
				intLINNO = 0;
				intPAGNO += 1;
				prnHEADER(); //gets the Header of the report
				prnFOOTR();
			}
			O_DOUT.close();
			O_FOUT.close();
			
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			setMSG("",'N');
		}catch(Exception L_EX){
			setMSG(L_EX,"getALLREC");
		}
		this.setCursor(cl_dat.M_curWTSTS_pbst);
	}
	private void prnFOOTR()
	{
		try
		{
			O_DOUT.writeBytes ("\n");
			crtLINE(156);
			O_DOUT.writeBytes("\n" + padSTRING('L',"Total : ",57) + padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(dblCHLVL)),3),11) + padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(dblNETVL)),3),9) + padSTRING('L',setNumberFormat(Double.parseDouble(String.valueOf(dblDIFVL)),3),9) + "\n\n");
			O_DOUT.writeBytes ("\n                                          Email To - Mr.V.T.Nandkumar,SPL HO\n\n");
			intLINNO += 7;
			prnFMTCHR(O_DOUT,M_strEJT);				
			prnFMTCHR(O_DOUT,M_strCPI17);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
	void exePRINT()
	{
		try
		{
			strFMDTM = txtFMDAT.getText().trim() +" "+txtFMTIM.getText().trim();
			strTODTM = txtTODAT.getText().trim() +" "+txtTOTIM.getText().trim();
			if(vldDATA())
			{
				getALLREC(strFMDTM,strTODTM); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				{
					if(intRECCT > 0)
					{
						doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpsms.doc");
					}
					else
							setMSG("Record could not be found",'E');	
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				{
					if(intRECCT > 0)
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
					if(intRECCT > 0)
					{
						if(M_txtDESTN.getText().length()>0)
							setMSG("File Copied to file: "+M_txtDESTN.getText().trim()+"",'N');
						else
							setMSG("File Copied to "+cl_dat.M_strREPSTR_pbst+"mm_rpsms.doc",'N');
					}
					else
						setMSG("Record could not be found",'E');
				}
			}//vldDATA
		}catch(Exception L_EX)
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
