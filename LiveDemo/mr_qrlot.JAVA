/*
System Name   : Marketing System
Program Name  : Lot Detail Analysis
Program Desc. : Gives customer,grade & lotno. detail of despatch transaction.
Author        : Mr. Deepal N. Mehta
Date          : 4th February 2003
Version       : MKT 1.0

Modificaitons 

Modified By    :
Modified Date  :
Modified det.  :
Version        :

*/

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;
import java.lang.*;
import java.util.Properties;

public class mr_qrlot extends cl_frm{
	JTextField txtPRDTP,txtDATFR,txtDATTO,txtSPECF;
	JRadioButton rdbBYRWS,rdbGRDWS,rdbLOTWS;
	JCheckbox chkDATE;
	JOptionPane LM_OPTNPN;
	/*String LM_RESFIN = cl_dat.ocl_dat.M_REPSTR;
	String LM_RESSTR = LM_RESFIN.concat("mr_qrlot.doc");
	FileOutputStream O_FOUT = crtFILE(LM_RESSTR);
    DataOutputStream O_DOUT = crtDTOUTSTR(O_FOUT);*/
	JTable LM_DTLTBL;
	
	int TB_INVNO = 0;
    int TB_INVDT = 1;
    int TB_COLM3 = 2;	
	int TB_COLM4 = 3;
	int TB_INVQT = 4;
    int TB_INVPK = 5;
    int TB_LADNO = 6;
	
	int LM_LMRGN=0;
	ResultSet LM_RSLSET;
	String LM_ACTTXT,LM_DATFR,LM_DATTO,LM_INVNO,LM_INVDT,LM_COLM3,LM_COLM4,LM_STRQRY,LM_INVQT;
	String LM_INVPK,LM_LADNO;
	
	
	mr_qrlot(){
		try{
			cl_dat.ocl_dat.M_PRGVER = " (v1.01)";
			setLBLMOD();
			lblMODL.setText(cl_dat.ocl_dat.M_STRMOD);
			LM_OPTNPN = new JOptionPane();
			crtLBL(pnlMAIN,"Starting From:",305,45,90,20);
			crtLBL(pnlMAIN,"Ending To:",450,45,90,20);
			crtLBL(pnlMAIN,"Grade: ",220,75,90,20);
			txtGRDFR=crtTXT(pnlMAIN,LEFT,305,75,100,20);	
			txtGRDTO=crtTXT(pnlMAIN,LEFT,450,75,100,20);	
			crtLBL(pnlMAIN,"Lot No: ",220,105,90,20);
			txtLOTFR=crtTXT(pnlMAIN,LEFT,305,105,100,20);
			txtLOTTO=crtTXT(pnlMAIN,LEFT,450,105,100,20);
			crtLBL(pnlMAIN,"Location: ",220,135,90,20);
			txtLOCFR=crtTXT(pnlMAIN,LEFT,305,135,100,20);
			txtLOCTO=crtTXT(pnlMAIN,LEFT,450,135,100,20);
			crtLBL(pnlMAIN,"Package Type:",575,45,90,20);
			txtPKGTP=crtTXT(pnlMAIN,LEFT,575,75,100,20);
			crtLBL(pnlMAIN,"File Name:",575,105,90,20);
			txtDESTN=crtTXT(pnlMAIN,LEFT,575,135,100,20);
			setRPTBTN(pnlMAIN,305,185,'B',' ');
			cl_dat.ocl_dat.M_HLPFLG = false;
			addLSTN();
			exeINTSTA();
			txtDESTN.setEnabled(false);
			btnRUN.setEnabled(false);
			cl_dat.ocl_dat.M_CHKTBL = false;
		}catch(Exception L_EX){
			System.out.println("mr_qrlot: "+L_EX);
		}
		}
	
	private void addLSTN(){ // adds Listeners to all the components
		txtGRDFR.addFocusListener(this);
		txtGRDFR.addKeyListener(this);
		txtGRDFR.addActionListener(this);
		txtGRDFR.setActionCommand("txtGRDFR");
		txtGRDTO.addFocusListener(this);
		txtGRDTO.addKeyListener(this);
		txtGRDTO.addActionListener(this);
		txtGRDTO.setActionCommand("txtGRDTO");
		txtLOTFR.addFocusListener(this);
		txtLOTFR.addKeyListener(this);
		txtLOTFR.addActionListener(this);
		txtLOTFR.setActionCommand("txtLOTFR");
		txtLOTTO.addFocusListener(this);
		txtLOTTO.addKeyListener(this);
		txtLOTTO.addActionListener(this);
		txtLOTTO.setActionCommand("txtLOTTO");
		txtLOCFR.addFocusListener(this);
		txtLOCFR.addKeyListener(this);
		txtLOCFR.addActionListener(this);
		txtLOCFR.setActionCommand("txtLOCFR");
		txtLOCTO.addFocusListener(this);
		txtLOCTO.addKeyListener(this);
		txtLOCTO.addActionListener(this);
		txtLOCTO.setActionCommand("txtISCNT");
		txtDESTN.addFocusListener(this);
		txtDESTN.addKeyListener(this);
		txtDESTN.addActionListener(this);
		txtDESTN.setActionCommand("txtDESTN");
		txtPKGTP.addFocusListener(this);
		txtPKGTP.addKeyListener(this);
		txtPKGTP.addActionListener(this);
		txtPKGTP.setActionCommand("txtPKGTP");
		}
	
	private void exeINTSTA(){
		txtGRDFR.setText("");
		txtGRDTO.setText("");
		txtLOTFR.setText("");
		txtLOTTO.setText("");
		txtLOCFR.setText("");
		txtLOCTO.setText("");
		txtDESTN.setText("");
		txtPKGTP.setText("");
		}
	
	public void actionPerformed(ActionEvent L_AE){
	try{
		super.actionPerformed(L_AE);
		LM_ACTTXT = L_AE.getActionCommand();
		if (LM_ACTTXT.equals("help")){
			exeHLPOK();
		}
		else if (LM_ACTTXT.equals("btnEXT")){
			cl_dat.ocl_dat.updUSG();
			this.dispose();
		}
		else if(L_AE.getSource().equals(btnRUN)){
			this.setCursor(curWTSTS);
			getQRSTR();
			setMSG("Fetching Stock Records.",'N');
			String[] L_STKHD = {"Grade","Lot No.","Rcl. No.","Classified Qty.","UnClassified Qty.","Main Loc.","Package Type","Retention Tag"};
			LM_STRSQL = "Select PR_PRDDS,ST_LOTNO,ST_RCLNO,ST_STKQT,ST_UCLQT,ST_MNLCD,ST_PKGTP,LT_RETQT,ST_PRDCD from FG_STMST,CO_PRMST,PR_LTMST";
			LM_STRSQL += " where ST_PRDCD=PR_PRDCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and (ST_STKQT+ST_UCLQT) > 0";
			LM_STRSQL += LM_GRDSTR + LM_LOTSTR + LM_LOCSTR + LM_PKGSTR + LM_ORDBY;
			LM_STRSQL = LM_STRSQL.substring(0,LM_STRSQL.length()-1).trim();
			LM_DTLTBL = crtDYNTBL1(LM_STRSQL,"FG","ACT",L_STKHD,0,0,750,128,10,240,750,130);
			cl_dat.ocl_dat.M_CHKTBL = false;
			setMSG("Fetching Issued Records.",'N');
			String[] L_LADHD = {"Loading Advice No.","Lot No.","Rcl. No.","Main Loc.","Pkg. Type","Issued Qty.","Issue Date","Status"};
			LM_STRSQL = "Select distinct IST_ISSNO,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_PKGTP,IST_ISSQT,IST_ISSDT,IST_STSFL,";
			LM_STRSQL += "IST_PRDCD from FG_ISTRN,CO_PRMST where IST_PRDCD=PR_PRDCD and IST_STSFL <> 'X' ";
			LM_STRSQL += LM_LDGDST + LM_LDLTST + LM_LDLCST + LM_LDPKST + LM_LDODST;
			LM_STRSQL = LM_STRSQL.substring(0,LM_STRSQL.length()-1).trim();
			LM_LADTBL = crtDYNTBL2(LM_STRSQL,"FG","ACT",L_LADHD,0,0,750,128,10,368,750,130);
			setMSG("Query Completed Successfully.",'N');
			this.setCursor(curDFSTS);
			}
	}catch(Exception L_EX){
		System.out.println(L_EX);
		}
	
	}
	
	private void getDATA(){
		try{
			this.setCursor(curWTSTS);
			getQRSTR();
			LM_FILNM = txtDESTN.getText().toString();
			if(LM_FILNM.length() > 0)
				LM_RESSTR = LM_RESFIN.concat(LM_FILNM.trim()+".doc");
			else
				LM_RESSTR = LM_RESFIN.concat("mr_qrlot.doc");
			O_FOUT = crtFILE(LM_RESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);	
			cl_dat.ocl_dat.M_PAGENO = 0;
			getISODCN();
			prnHEADER();
			getSTKREC();
			O_DOUT.writeBytes("\n");
			cl_dat.ocl_dat.M_LINENO+=1;
			crtLINE(122);
			prnFMTCHR(O_DOUT,M_NOCPI17);
			prnFMTCHR(O_DOUT,M_CPI10);
			O_DOUT.close();
			O_FOUT.close();
			this.setCursor(curDFSTS);
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
	}
	
	private void getSTKREC(){
		try{
			double L_DSTQT = 0;
			double L_DUCQT = 0;
			double L_DMSQT = 0;
			double L_DMUQT = 0;
			double L_DMSQT1 = 0;
			double L_DMUQT1 = 0;
			double L_DSQTY = 0;
			double L_DUQTY = 0;
			int i = 0;
			LM_PRVPDS = "";
			LM_DSPSQT = "";
			LM_DSPUQT = "";
                        LM_STRQRY = "Select PR_PRDDS,ST_LOTNO,ST_RCLNO,ST_MNLCD,ST_STKQT,ST_UCLQT,ST_PKGTP,ST_REMDS,ST_PRDCD from FG_STMST,CO_PRMST,PR_LTMST";
			LM_STRQRY += " where ST_PRDCD=PR_PRDCD and ST_PRDTP=LT_PRDTP and ST_LOTNO=LT_LOTNO and ST_RCLNO=LT_RCLNO and (ST_STKQT+ST_UCLQT) > 0";
			LM_STRQRY += LM_GRDSTR + LM_LOTSTR + LM_LOCSTR + LM_PKGSTR + LM_ORDBY;
			LM_STRQRY = LM_STRQRY.substring(0,LM_STRQRY.length()-1).trim();
			LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(LM_STRQRY,"FG","ACT");
			while(LM_RSLSET.next()){
				LM_PRDDS = LM_RSLSET.getString("PR_PRDDS");
				LM_LOTNO = LM_RSLSET.getString("ST_LOTNO");
				setMSG("Fetching Lot No: "+LM_LOTNO+" of Grade: "+LM_PRDDS,'N');
				LM_STKQT = LM_RSLSET.getString("ST_STKQT");
				LM_UCLQT = LM_RSLSET.getString("ST_UCLQT");
				LM_MNLCD = LM_RSLSET.getString("ST_MNLCD");
				LM_PKGTP = LM_RSLSET.getString("ST_PKGTP");
				LM_PKGDS = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","SYS","FGXXPKG",LM_PKGTP);	
                LM_REMDS = LM_RSLSET.getString("ST_REMDS");
				LM_PRDCD = LM_RSLSET.getString("ST_PRDCD");
				L_DSTQT = Double.parseDouble(LM_STKQT);
				L_DUCQT = Double.parseDouble(LM_UCLQT);
				if(!(LM_PRDDS.equals(LM_PRVPDS))){
					L_DMSQT1 = 0;
					L_DMUQT1 = 0;
					prnFMTCHR(O_DOUT,"BOLD");
					O_DOUT.writeBytes(padSTRING('R'," ",6));
					if(i == 0)
						O_DOUT.writeBytes(padSTRING('R',LM_PRDDS,15));
					else{
						O_DOUT.writeBytes(padSTRING('R',LM_PRVPDS,15));
						O_DOUT.writeBytes(padSTRING('L',LM_DSPSQT,23));
						O_DOUT.writeBytes(padSTRING('L',LM_DSPUQT,16));
						O_DOUT.writeBytes ("\n");
						cl_dat.ocl_dat.M_LINENO+=1;
						O_DOUT.writeBytes ("\n");
						cl_dat.ocl_dat.M_LINENO+=1;
						O_DOUT.writeBytes(padSTRING('R'," ",6));
						O_DOUT.writeBytes(padSTRING('R',LM_PRDDS,15));
					}
					LM_PRVPDS = LM_PRDDS;
					prnFMTCHR(O_DOUT,"NOBOLD");
					O_DOUT.writeBytes ("\n");
					cl_dat.ocl_dat.M_LINENO+=1;
					O_DOUT.writeBytes ("\n");
					cl_dat.ocl_dat.M_LINENO+=1;
					i++;
				}
			O_DOUT.writeBytes(padSTRING('R'," ",6));
			O_DOUT.writeBytes(padSTRING('R',LM_LOTNO,12));
			O_DOUT.writeBytes(padSTRING('R'," ",2));
			O_DOUT.writeBytes(padSTRING('R',LM_MNLCD,8));
			O_DOUT.writeBytes(padSTRING('L',LM_STKQT,16));
			O_DOUT.writeBytes(padSTRING('L',LM_UCLQT,16));
			O_DOUT.writeBytes(padSTRING('R'," ",4));
			O_DOUT.writeBytes(padSTRING('R',LM_PKGDS,16));
			O_DOUT.writeBytes(padSTRING('R'," ",4));
			if(LM_REMDS == null)
				LM_REMDS = "-";
			O_DOUT.writeBytes(padSTRING('R',LM_REMDS,20));
			O_DOUT.writeBytes("\n");
			cl_dat.ocl_dat.M_LINENO +=1;
			L_DMSQT = L_DMSQT + L_DSTQT;
			LM_DSTQT = L_DMSQT;
			L_DMUQT = L_DMUQT + L_DUCQT;
			LM_DUCQT = L_DMUQT;
			if(LM_PRDDS.equals(LM_PRVPDS)){
				L_DMSQT1 = L_DMSQT1 + L_DSTQT;
				L_DSQTY = L_DMSQT1;
				L_DMUQT1 = L_DMUQT1 + L_DUCQT;
				L_DUQTY = L_DMUQT1;
				LM_DSPSQT = setFMT("RND",String.valueOf(L_DSQTY),3);
				LM_DSPUQT = setFMT("RND",String.valueOf(L_DUQTY),3);
			}
			if(cl_dat.ocl_dat.M_LINENO > 60){
				crtLINE(122);
				for(int j = 0;j < 12;j++)
					O_DOUT.writeBytes ("\n");
				cl_dat.ocl_dat.M_LINENO=0;
				prnHEADER();
				}
			}
			dspTOTQT();
			if(LM_RSLSET != null)
				LM_RSLSET.close();
			setMSG("",'N');
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
	}
	
	private void dspTOTQT(){ // To display Total Qty.
		try{
			prnFMTCHR(O_DOUT,"BOLD");
			O_DOUT.writeBytes ("\n");
			cl_dat.ocl_dat.M_LINENO+=1;
			O_DOUT.writeBytes(padSTRING('R'," ",6));
			O_DOUT.writeBytes(padSTRING('R',LM_PRDDS,15));
			O_DOUT.writeBytes(padSTRING('L',LM_DSPSQT,23));
			O_DOUT.writeBytes(padSTRING('L',LM_DSPUQT,16));
			O_DOUT.writeBytes ("\n");
			cl_dat.ocl_dat.M_LINENO+=1;
			O_DOUT.writeBytes ("\n");
			cl_dat.ocl_dat.M_LINENO+=1;
			O_DOUT.writeBytes(padSTRING('R',"",6));
			O_DOUT.writeBytes(padSTRING('R',"Grand Total:",15));
			O_DOUT.writeBytes(padSTRING('L',setFMT("RND",String.valueOf(LM_DSTQT),3),23));
			O_DOUT.writeBytes(padSTRING('L',setFMT("RND",String.valueOf(LM_DUCQT),3),16));
			prnFMTCHR(O_DOUT,"NOBOLD");
			O_DOUT.writeBytes ("\n");
			cl_dat.ocl_dat.M_LINENO+=1;
		}catch(Exception L_EX){
			System.out.println(L_EX);
		}
	}
	
	private void clrDTLTBL(){ //clears Lot Table 
		for(int i = 0;i<LM_DTLTBL.getRowCount();i++){
			for(int j = 0;j<LM_DTLTBL.getColumnCount();j++){
				LM_DTLTBL.setValueAt("",i,j);
				}
			}
	}
	
	private void getQRSTR(){
		try{
			LM_GRDSTR = "";
			LM_LOTSTR = "";
			LM_LOCSTR = "";
			LM_PKGSTR = "";
			LM_LDGDST = "";
			LM_LDLTST = "";
			LM_LDLCST = "";
			LM_LDPKST = "";
			LM_LDODST = " order by ";
			LM_ORDBY = " order by ";
			LM_GRDFR = txtGRDFR.getText().toString().trim();
			LM_GRDTO = txtGRDTO.getText().toString().trim();
			LM_LOTFR = txtLOTFR.getText().toString().trim();
			LM_LOTTO = txtLOTTO.getText().toString().trim();
			LM_LOCFR = txtLOCFR.getText().toString().trim();
			LM_LOCTO = txtLOCTO.getText().toString().trim();
			LM_PKGTP = txtPKGTP.getText().toString().trim();
			if(!LM_GRDFR.equals("") && !LM_GRDTO.equals("")){
				LM_GRDSTR = " and PR_PRDDS between '"+LM_GRDFR.trim()+"' and '"+LM_GRDTO.trim()+"'";
				LM_ORDBY += "ST_PRDCD,ST_LOTNO,ST_RCLNO,";
				LM_LDGDST = " and PR_PRDDS between '"+LM_GRDFR.trim()+"' and '"+LM_GRDTO.trim()+"'";
				LM_LDODST += "IST_PRDCD,";
			}
			if(!LM_LOTFR.equals("") && !LM_LOTTO.equals("")){
				LM_LOTSTR = " and ST_LOTNO between '"+LM_LOTFR.trim()+"' and '"+LM_LOTTO.trim()+"'";
				LM_ORDBY += "ST_LOTNO,ST_RCLNO,";
				LM_LDLTST = " and IST_LOTNO between '"+LM_LOTFR.trim()+"' and '"+LM_LOTTO.trim()+"'";
				LM_LDODST += "IST_LOTNO,IST_RCLNO,";
			}
			if(!LM_LOCFR.equals("") && !LM_LOCTO.equals("")){
				LM_LOCSTR = " and ST_MNLCD between '"+LM_LOCFR.trim()+"' and '"+LM_LOCTO.trim()+"'";
				LM_ORDBY += "ST_MNLCD,";
				LM_LDLCST = " and IST_MNLCD between '"+LM_LOCFR.trim()+"' and '"+LM_LOCTO.trim()+"'";
				LM_LDODST += "IST_MNLCD,";
			}
			if(!LM_PKGTP.equals("")){
				LM_PKGSTR = " and ST_PKGTP = '"+LM_PKGTP+"'";
				LM_ORDBY += "ST_PKGTP,";
				LM_LDPKST = " and IST_PKGTP = '"+LM_PKGTP+"'";
				LM_LDODST += "IST_PKGTP,";
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getGRDSTR","");
		}
	}
	
	public void keyPressed(KeyEvent L_KE){
		super.keyPressed(L_KE);
		if (L_KE.getKeyCode() == KeyEvent.VK_ENTER){
			if (L_KE.getSource().equals(btnHLP_OK)){
				exeHLPOK();		
				}
			else if (L_KE.getSource().equals(lstHELP)){
				exeHLPOK();		
				}
			else if (L_KE.getSource().equals(txtHLPPOS)){
				exeHLPOK();		
				}
			else if (L_KE.getSource().equals(txtGRDFR)){
				LM_GRDFR = txtGRDFR.getText().toString().trim();
				LM_GRADE = LM_GRDFR;
				if(vldGRADE()){
					setMSG("Valid Grade",'N');
					txtGRDTO.setText(txtGRDFR.getText().toString().trim());
					txtGRDTO.requestFocus();
				}else{
					setMSG("InValid Grade",'E');
				}
			}
			else if (L_KE.getSource().equals(txtGRDTO)){
				LM_GRDTO = txtGRDTO.getText().toString().trim();
				LM_GRADE = LM_GRDTO;
				if(vldGRADE()){
					setMSG("Valid Grade",'N');
					txtLOTFR.requestFocus();
					txtDESTN.setEnabled(true);
					btnRUN.setEnabled(true);
				}else{
					setMSG("InValid Grade",'E');
				}
			}
			else if (L_KE.getSource().equals(txtLOTFR)){
				LM_LOTFR = txtLOTFR.getText().toString().trim();
				LM_LOTNO = LM_LOTFR;
				if(vldLOTNO()){
					setMSG("Valid LOTNO",'N');
					txtLOTTO.setText(txtLOTFR.getText().toString().trim());
					txtLOTTO.requestFocus();
				}else{
					setMSG("InValid LOTNO",'E');
				}
			}
			else if (L_KE.getSource().equals(txtLOTTO)){
				LM_LOTTO = txtLOTTO.getText().toString().trim();
				LM_LOTNO = LM_LOTTO;
				if(vldLOTNO()){
					setMSG("Valid LOTNO",'N');
					txtLOCFR.requestFocus();
					txtDESTN.setEnabled(true);
					btnRUN.setEnabled(true);
				}else{
					setMSG("InValid LOTNO",'E');
				}
			}
			else if (L_KE.getSource().equals(txtLOCFR)){
				LM_LOCFR = txtLOCFR.getText().toString().trim();
				LM_LOCND = LM_LOCFR;
				if(vldMNLCD()){
					setMSG("Valid Location",'N');
					txtLOCTO.setText(txtLOCFR.getText().toString().trim());
					txtLOCTO.requestFocus();
				}else{
					setMSG("InValid Location",'E');
				}
			}
			else if (L_KE.getSource().equals(txtLOCTO)){
				LM_LOCTO = txtLOCTO.getText().toString().trim();
				LM_LOCND = LM_LOCTO;
				if(vldMNLCD()){
					setMSG("Valid Location",'N');
					txtDESTN.setEnabled(true);
					btnRUN.setEnabled(true);
					btnRUN.requestFocus();
				}else{
					setMSG("InValid Location",'E');
				}
			}
			else if (L_KE.getSource().equals(txtPKGTP)){
				LM_PKGTP = txtPKGTP.getText().toString().trim();
				if(vldPKGTP()){
					setMSG("Valid Package Type",'N');
					txtDESTN.setEnabled(true);
					btnRUN.setEnabled(true);
					btnRUN.requestFocus();
				}else{
					setMSG("InValid Package Type.",'E');
				}
			}
			else if (L_KE.getSource().equals(txtDESTN)){
				btnRUN.requestFocus();
			}
		}
		if(L_KE.getKeyCode() == KeyEvent.VK_F1){
			LM_FRMFL = false;
			LM_TOFLG = false;
			LM_ADDSTR = "";
			getQRSTR();
			if(L_KE.getSource().equals(txtGRDFR)){
				LM_FRMFL = true;
				LM_TOFLG = false;
				hlpGRADE();
				}
			else if(L_KE.getSource().equals(txtGRDTO)){
				LM_TOFLG = true;
				LM_FRMFL = false;
				LM_ADDSTR = " and PR_PRDDS > '"+LM_GRDFR+"'";
				hlpGRADE();
				}
			else if(L_KE.getSource().equals(txtLOTFR)){
				LM_FRMFL = true;
				LM_TOFLG = false;
				hlpLOTNO();
				}
			else if(L_KE.getSource().equals(txtLOTTO)){
				LM_TOFLG = true;
				LM_FRMFL = false;
				LM_ADDSTR = " and ST_LOTNO > '"+LM_LOTFR+"'";
				hlpLOTNO();
				}
			else if(L_KE.getSource().equals(txtLOCFR)){
				LM_FRMFL = true;
				LM_TOFLG = false;
				hlpMNLCD();
				}
			else if(L_KE.getSource().equals(txtLOCTO)){
				LM_TOFLG = true;
				LM_FRMFL = false;
				LM_ADDSTR = " and ST_MNLCD > '"+LM_LOCFR+"'";
				hlpMNLCD();
				}
			else if(L_KE.getSource().equals(txtPKGTP)){
				LM_ADDSTR = "";
				hlpPKGTP();
				}
			}
		}
	
	private boolean vldGRADE(){
		try{
			LM_STRQRY = "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where PR_PRDCD=ST_PRDCD and PR_PRDDS='"+LM_GRADE+"'";
			LM_STRQRY += LM_LOTSTR + LM_LOCSTR + LM_PKGSTR;
			LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(LM_STRQRY,"FG","ACT");
			if(LM_RSLSET.next()){
				return true;
				}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"vldGRADE","");
		}
		return false;
	}
	
	private boolean vldLOTNO(){
		try{
			LM_STRQRY = "Select ST_LOTNO from FG_STMST,CO_PRMST where ST_PRDCD=PR_PRDCD and ST_LOTNO='"+LM_LOTNO+"'";
			LM_STRQRY += LM_GRDSTR + LM_LOCSTR + LM_PKGSTR;
			LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(LM_STRQRY,"FG","ACT");
			if(LM_RSLSET.next()){
				return true;
				}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"vldLOTNO","");
		}
		return false;
	}
	
	private void hlpGRADE(){
		String L_ARRHDR[] ={"Grade"};
		LM_STRQRY = "Select distinct PR_PRDDS from CO_PRMST,FG_STMST where PR_PRDCD=ST_PRDCD";
		LM_STRQRY += LM_ADDSTR + LM_LOTSTR + LM_LOCSTR;
		//System.out.println(LM_STRQRY);
		cl_dat.ocl_dat.M_HLPFLG = true;
		LM_HLPFLD = "txtGRADE";
		if(LM_STRQRY != null){
			cl_hlp(LM_STRQRY,"FG","ACT",1,1,L_ARRHDR,1,"CT");
			}
	}
	
	private void hlpLOTNO(){
		String L_ARRHDR[] ={ "Lot No."};
		LM_STRQRY = "Select distinct ST_LOTNO from FG_STMST,CO_PRMST where ST_PRDCD=PR_PRDCD";
		LM_STRQRY += LM_ADDSTR + LM_GRDSTR + LM_LOCSTR + LM_PKGSTR;
		//System.out.println(LM_STRQRY);
		cl_dat.ocl_dat.M_HLPFLG = true;
		LM_HLPFLD = "txtLOTNO";
		if(LM_STRQRY != null){
			cl_hlp(LM_STRQRY,"FG","ACT",1,1,L_ARRHDR,1,"CT");
			}
	}
	
	private void exeHLPOK(){
		try{
			if(LM_HLPFLD.equals("txtGRADE")){
			   cl_dat.ocl_dat.M_HLPFLG = false;
			   if(LM_FRMFL)
			  	txtGRDFR.setText(cl_dat.ocl_dat.M_HLPSTR);
			   if(LM_TOFLG)
			  	txtGRDTO.setText(cl_dat.ocl_dat.M_HLPSTR);
			}
			else if(LM_HLPFLD.equals("txtLOTNO")){
			   cl_dat.ocl_dat.M_HLPFLG = false;
			   if(LM_FRMFL)
			  	txtLOTFR.setText(cl_dat.ocl_dat.M_HLPSTR);
			   if(LM_TOFLG)
			  	txtLOTTO.setText(cl_dat.ocl_dat.M_HLPSTR);
			}
			else if(LM_HLPFLD.equals("txtMNLCD")){
			   cl_dat.ocl_dat.M_HLPFLG = false;
			   if(LM_FRMFL)
			  	txtLOCFR.setText(cl_dat.ocl_dat.M_HLPSTR);
			   if(LM_TOFLG)
			  	txtLOCTO.setText(cl_dat.ocl_dat.M_HLPSTR);
			}
			else if(LM_HLPFLD.equals("txtPKGTP")){
			   cl_dat.ocl_dat.M_HLPFLG = false;
			   txtPKGTP.setText(cl_dat.ocl_dat.M_HLPSTR);
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"exeHLPOK","");
		}
	}
}
