import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.lang.*;
import cl_eml;
import cl_cust;
import java.util.Properties;
import java.util.Date; 
import java.io.*; 

public class mr_tpdor extends cl_frm{
	JTextField txtFRDNO,txtTODNO,txtPRDTP;
	JRadioButton rdbPREPN,rdbPLNPN;
	JCheckBox chkVWAMD;
	JButton btnRUN,btnEXT;
    ButtonGroup chkPRNST;
	
	String LM_FRDNO,LM_TODNO,LM_PRDTP,LM_STRQRY,LM_HLPFLD,LM_ACTTXT,LM_ISODCA,LM_ISODCB,LM_ISODCC,LM_TSTFL,LM_ADDSTR;
	String LM_TRNSP,LM_DISTR,LM_FAXNO,LM_SLTYP,LM_DLTYP,LM_TRMOD,LM_EXCNO,LM_RNGDS,LM_DIVDS,LM_CLLDS,LM_ITPNO;
	
	String LM_BYRCD1,LM_BYRCD,LM_BYRCD2;	
	String LM_CNSCD1,LM_CNSCD,LM_CNSCD2;	
	String LM_INDNO1,LM_INDNO,LM_INDNO2;	
	String LM_INDDT1,LM_INDDT,LM_INDDT2;	
	String LM_AMIDT1,LM_AMIDT,LM_AMIDT2;	
	String LM_AMINO1,LM_AMINO,LM_AMINO2;	
	String LM_PORNO1,LM_PORNO,LM_PORNO2;	
	String LM_PORDT1,LM_PORDT,LM_PORDT2;	
	String LM_SALTP1,LM_SALTP,LM_SALTP2;	
	String LM_DTPCD1,LM_DTPCD,LM_DTPCD2;	
	String LM_DSRCD1,LM_DSRCD,LM_DSRCD2;	
	String LM_CPTVL1,LM_CPTVL,LM_CPTVL2;	
	String LM_DORNO1,LM_DORNO,LM_DORNO2;	
	String LM_DORDT1,LM_DORDT,LM_DORDT2;	
	String LM_AMDDT1,LM_AMDDT,LM_AMDDT2;	
	String LM_AMDNO1,LM_AMDNO,LM_AMDNO2;	
	String LM_TMOCD1,LM_TMOCD,LM_TMOCD2;	
	String LM_LRYNO1,LM_LRYNO,LM_LRYNO2;	
	String LM_MKTTP1,LM_MKTTP,LM_MKTTP2;	
    String LM_TRPCD1,LM_TRPCD,LM_TRPCD2; 
	String LM_PRDDS1,LM_PRDDS,LM_PRDDS2;	
	String LM_LOTRF1,LM_LOTRF,LM_LOTRF2;	
	String LM_DORPK1,LM_DORPK,LM_DORPK2;	
	String LM_DORQT1,LM_DORQT,LM_DORQT2;	
	String LM_DELTP1,LM_DELTP,LM_DELTP2;	
	String LM_AUTDT1,LM_AUTDT,LM_AUTDT2;	
	String LM_BASRT1,LM_BASRT,LM_BASRT2;	
	String LM_EXCRT1,LM_EXCRT,LM_EXCRT2;	
	String LM_DSPDT1,LM_DSPDT,LM_DSPDT2;	
	String LM_DODQT1,LM_DODQT,LM_DODQT2;	
	String LM_REMDS;	
	
	String LM_CNCOD;
	String LM_CNSNM,LM_BYRNM;
	String LM_CNAD1,LM_BYAD1;
	String LM_CNAD2,LM_BYAD2;
	String LM_CNAD3,LM_BYAD3;
	String LM_CNAD4,LM_BYAD4;
	String LM_CNPIN,LM_BYPIN;
	String LM_CNCST,LM_BYCST;
	String LM_CNSTN,LM_BYSTN;
	String LM_CNECC,LM_BYECC;
	
	String LM_BYHDR = " ";
	String LM_CNHDR = " ";
	String LM_DONHD = " ";
	String LM_DODHD = " ";
	String LM_AMDHD = " ";
	String LM_INNHD = " ";
	String LM_INDHD = " ";
	String LM_INAHD = " ";
	String LM_PONHD = " ";
	String LM_PODHD = " ";
	String LM_SLTHD = " ";
	String LM_DETHD = " ";
	String LM_TRSHD = " ";
	String LM_CSTHD = " ";
	String LM_STNHD = " ";
	String LM_ECCHD = " ";
	String LM_DSTHD = " ";
	String LM_CDTHD = " ";
	String LM_PYMHD = " ";
	String LM_PRDHD = " ";
	String LM_TRPHD = " ";
	String LM_FAXHD = " ";
	String LM_LRYHD = " ";
	String LM_PIPFL = "";
	
	ResultSet LM_RSLSET;
	
	String LM_RESFIN = cl_dat.ocl_dat.M_REPSTR;
        String LM_RESSTR = LM_RESFIN.trim().concat("\\mr_tpdor.doc"); 
	
	FileOutputStream O_FOUT;
    DataOutputStream O_DOUT;
	
	boolean LM_PRNHDR = false;
	
	int LM_LINENO = 0;
	mr_tpdor(){
		cl_dat.ocl_dat.M_PRGVER = " (v2.0)";
		setLBLMOD();
		chkPRNST = new ButtonGroup();
		cl_dat.ocl_dat.M_HLPFLG = false;
		crtLBL(pnlMAIN,"Product Category :",250,30,150,30);
		txtPRDTP = crtTXT(pnlMAIN,LEFT,370,30,100,20);
		chkVWAMD = crtCHKBOX(pnlMAIN,"View Amendment",480,30,150,20,false);
		crtLBL(pnlMAIN,"D.O No. From :",250,70,90,30);
		txtFRDNO = crtTXT(pnlMAIN,LEFT,370,70,100,20);
		crtLBL(pnlMAIN,"D.O No. To :",250,100,90,30);
		txtTODNO = crtTXT(pnlMAIN,LEFT,370,100,100,20);
		crtLBL(pnlMAIN,"Printing On :",250,130,90,30);
		rdbPREPN = crtRDOGRP(pnlMAIN,chkPRNST,"PrePrinted",250,150,100,30,false);
		rdbPLNPN = crtRDOGRP(pnlMAIN,chkPRNST,"Plain",370,150,70,30,true);
		btnRUN = crtBTN(pnlMAIN,"RUN",250,190,100,20);
		btnEXT = crtBTN(pnlMAIN,"EXIT",370,190,100,20);
		addLSTN();
		txtPRDTP.requestFocus();
	}
	
	private void addLSTN(){
		try{
			txtPRDTP.addFocusListener(this);
			txtPRDTP.addKeyListener(this);
			txtPRDTP.setActionCommand("txtPRDTP");
			txtFRDNO.addFocusListener(this);
			txtFRDNO.addKeyListener(this);
			txtFRDNO.setActionCommand("txtFRDNO");
			txtTODNO.addFocusListener(this);
			txtTODNO.addKeyListener(this);
			txtTODNO.setActionCommand("txtTODNO");
			rdbPREPN.addFocusListener(this);
			rdbPREPN.addKeyListener(this);
			rdbPREPN.addMouseListener(this);
			rdbPREPN.setActionCommand("rdbPREPN");
			rdbPLNPN.addFocusListener(this);
			rdbPLNPN.addKeyListener(this);
			rdbPLNPN.addMouseListener(this);
			rdbPLNPN.setActionCommand("rdbPLNPN");
			btnRUN.addFocusListener(this);
			btnRUN.addKeyListener(this);
			btnRUN.addMouseListener(this);
			btnRUN.setActionCommand("btnRUN");
			chkVWAMD.addMouseListener(this);
		}catch(Exception L_EX){
			showEXMSG(L_EX,"addLSTN","");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE){
		try{
			super.actionPerformed(L_AE);
			LM_ACTTXT = L_AE.getActionCommand();
			if(L_AE.getSource().equals(btnEXT)){
      			cl_dat.ocl_dat.updUSG();
				this.dispose();
			}
			else if(LM_ACTTXT.equals("help"))
				exeHLPOK();
			else if(L_AE.getSource().equals(btnRUN)){
				this.setCursor(curWTSTS);
				setMSG("Fetching of data in progress... ",'N');
				prnALLREC();
				this.setCursor(curDFSTS);
				Runtime r = Runtime.getRuntime();
				Process p = null;
				try{
                      cl_dat.ocl_dat.M_DELFL = 'T' ;
                      p  = r.exec("co_rpprn.bat "+LM_RESSTR); 
				}catch(IOException L_EX){
					System.out.println("Error.exescrn.......... "+L_EX);
 					}
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"actionPerformed","");
		}
	}
	
	public void mouseClicked(MouseEvent L_ME){
		try{
			if(L_ME.getSource().equals(chkVWAMD))
				txtFRDNO.requestFocus();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"mouseClicked","");
		}
	}
	
	public void keyPressed(KeyEvent L_KE){
		try{
			super.keyPressed(L_KE);
			LM_PRDTP = txtPRDTP.getText().toString().trim();
			LM_FRDNO = txtFRDNO.getText().toString().trim();
			LM_TODNO = txtTODNO.getText().toString().trim();
			if(L_KE.getKeyCode() == L_KE.VK_F1){
				if(L_KE.getSource().equals(txtPRDTP)){
					String L_ARRHDR[] = {"Type.","DESC"};
					cl_dat.ocl_dat.getSSTTP();
					LM_STRQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					LM_STRQRY += " CMT_CGSTP = 'COXXMKT' and CMT_CODCD in ("+cl_dat.ocl_dat.M_STRMR+")";
					cl_dat.ocl_dat.M_HLPFLG = true;
					LM_HLPFLD = "txtPRDTP";
					if(LM_STRQRY != null)
						cl_hlp(LM_STRQRY,"SP","ACT",1,1,L_ARRHDR,2,"CT");
				}
				else if(L_KE.getSource().equals(txtFRDNO)){
					if(chkVWAMD.isSelected()){
						LM_ADDSTR = " and dot_amdno > '00' ";
						LM_ADDSTR += " order by dot_amddt desc";
					}else{
						LM_ADDSTR = "  order by dot_dordt desc";
					}
					String L_ARRHDR[] = {"D.O. No.","D.O. Date","Buyer","Amd. No.","Amd. Date","Status","Last Updated By"};
					LM_STRQRY = "Select distinct dot_dorno,dot_dordt,pt_prtnm,dot_amdno,dot_amddt,dot_stsfl,dot_lupdt from mr_dotrn,mr_inmst,";
					LM_STRQRY += "co_ptmst where dot_mkttp=in_mkttp and dot_indno=in_indno and";
                                        LM_STRQRY += " in_byrcd=pt_prtcd and pt_prttp='C' and in_stsfl in ('H','O','P','C','1','2') and dot_stsfl in ('A','O','H','X')" + LM_ADDSTR;
					cl_dat.ocl_dat.M_HLPFLG = true;
					LM_HLPFLD = "txtFRDNO";
					if(LM_STRQRY != null)
						cl_hlp(LM_STRQRY,"SP","ACT",1,1,L_ARRHDR,7,"CT");
				}
				else if(L_KE.getSource().equals(txtTODNO)){
					if(chkVWAMD.isSelected()){
						LM_ADDSTR = " and dot_amdno > '00' and dot_amddt = '"+cc_dattm.occ_dattm.setDATFMT("MDY",cl_dat.ocl_dat.M_LOGDAT)+"'";
						LM_ADDSTR += " order by dot_amddt desc";
					}else{
						LM_ADDSTR = "  order by dot_dordt desc";
					}
					String L_ARRHDR[] = {"D.O. No.","D.O. Date","Buyer","Amd. No.","Amd. Date","Status","Last Updated By"};
					LM_STRQRY = "Select distinct dot_dorno,dot_dordt,pt_prtnm,dot_amdno,dot_amddt,dot_stsfl,dot_lupdt from mr_dotrn,mr_inmst,";
					LM_STRQRY += "co_ptmst where dot_mkttp=in_mkttp and dot_indno=in_indno and";
                                        LM_STRQRY += " in_byrcd=pt_prtcd and pt_prttp='C' and in_stsfl in ('H','O','P','C','1','2','3') and dot_stsfl in ('A','O','H','X')";
					LM_STRQRY += " and dot_dorno >= '"+LM_FRDNO+"'"+ LM_ADDSTR;
					cl_dat.ocl_dat.M_HLPFLG = true;
					LM_HLPFLD = "txtTODNO";
					if(LM_STRQRY != null)
						cl_hlp(LM_STRQRY,"SP","ACT",1,1,L_ARRHDR,7,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER || L_KE.getKeyCode() == 9){
				if(L_KE.getSource().equals(btnHLP_OK))
					exeHLPOK();
				else if(L_KE.getSource().equals(lstHELP))
					exeHLPOK();
				else if(L_KE.getSource().equals(txtHLPPOS))
					exeHLPOK();
				else if(L_KE.getSource().equals(txtPRDTP)){
					cl_dat.ocl_dat.getSSTTP();
					LM_STRQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='MST' and";
					LM_STRQRY += " CMT_CGSTP = 'COXXMKT' and CMT_CODCD in ("+cl_dat.ocl_dat.M_STRMR+")";
					LM_STRQRY += " and cmt_codcd='"+LM_PRDTP+"'";
					LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(LM_STRQRY,"SP","ACT");
					if(LM_RSLSET.next()){
						setMSG("Enter D.O. No. or Press F1 for help on D.O. No.",'N');
						txtFRDNO.requestFocus();
					}else{
						setMSG("InValid Product Category.",'E');
						txtPRDTP.requestFocus();
					}
					LM_RSLSET.close();
				}
				else if(L_KE.getSource().equals(txtFRDNO)){
					LM_STRQRY = "Select dot_dorno from mr_dotrn where dot_dorno='"+LM_FRDNO+"'";
					LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(LM_STRQRY,"SP","ACT");
					if(LM_RSLSET.next()){
						setMSG("Enter D.O. No. or Press F1 for help on D.O. No.",'N');
						txtTODNO.setText(LM_FRDNO);
						txtTODNO.requestFocus();
					}else{
						setMSG("InValid D.O. No.",'E');
						txtFRDNO.requestFocus();
					}
					LM_RSLSET.close();
				}
				else if(L_KE.getSource().equals(txtTODNO)){
					LM_STRQRY = "Select dot_dorno from mr_dotrn where dot_dorno='"+LM_TODNO+"'";
					LM_STRQRY += " and dot_dorno >= '"+LM_FRDNO+"'";
					LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(LM_STRQRY,"SP","ACT");
					if(LM_RSLSET.next()){
						setMSG("Select Printing Option.",'N');
						rdbPLNPN.requestFocus();
					}else{
						setMSG("InValid D.O. No.",'E');
						txtTODNO.requestFocus();
					}
					LM_RSLSET.close();
				}
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"keyPressed","");
		}
	}
	
	private void exeHLPOK(){
		try{
			cl_dat.ocl_dat.M_HLPFLG = false;
			if(LM_HLPFLD.equals("txtPRDTP"))
				txtPRDTP.setText(cl_dat.ocl_dat.M_HLPSTR);
			else if(LM_HLPFLD.equals("txtFRDNO"))
				txtFRDNO.setText(cl_dat.ocl_dat.M_HLPSTR);
			else if(LM_HLPFLD.equals("txtTODNO"))
				txtTODNO.setText(cl_dat.ocl_dat.M_HLPSTR);
		}catch(Exception L_EX){
			showEXMSG(L_EX,"exeHLPOK","");
		}
	}
	
	private void prnALLREC(){
		try{
			
			O_FOUT = crtFILE(LM_RESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);	
			prnFMTCHR(O_DOUT,M_NOCPI17);
			prnFMTCHR(O_DOUT,M_CPI10);
			prnFMTCHR(O_DOUT,M_CPI17);
			LM_ISODCA = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR01");
			LM_ISODCB = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR02");
			LM_ISODCC = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR03");
			
			boolean L_PREPN = false;
			boolean L_EOF = false;
			boolean L_1STSFL = true;
			LM_PRNHDR = false;
			LM_LINENO = 0;
			
			LM_PRDTP = txtPRDTP.getText().toString().trim();
			LM_FRDNO = txtFRDNO.getText().toString().trim();
			LM_TODNO = txtTODNO.getText().toString().trim();
			if(rdbPREPN.isSelected())
				L_PREPN = true;
			
			LM_STRQRY = "Select in_byrcd,in_cnscd,in_indno,in_inddt,in_amddt,in_porno,";
			LM_STRQRY += "in_pordt,in_saltp,in_dtpcd,in_dsrcd,in_cptvl,in_amdno,";
			LM_STRQRY += "dot_dorno,dot_dordt,dot_amdno,dot_amddt,dot_tmocd,dot_lryno,dot_mkttp,";
            LM_STRQRY += "dot_trpcd,dot_prdds,dot_lotrf,dot_dorpk,dot_dorqt,dot_deltp,dot_autdt,";
			LM_STRQRY += "int_basrt,int_excrt,dod_dspdt,dod_dorqt";
            LM_STRQRY += " from vw_intrn,mr_dotrn left outer join mr_dodel on dot_dorno=dod_dorno";
			LM_STRQRY += " and dot_mkttp=dod_mkttp and dot_prdcd=dod_prdcd";
			LM_STRQRY += " where dot_indno=in_indno and dot_mkttp=in_mkttp and dot_prdcd=int_prdcd";
			LM_STRQRY += " and dot_dorno between '"+LM_FRDNO+"' and '"+LM_TODNO+"' and";
			LM_STRQRY += " dot_mkttp='"+LM_PRDTP+"'";
			LM_STRQRY += " order by dot_dorno";
            
			LM_RSLSET = cl_dat.ocl_dat.exeSQLQRY1(LM_STRQRY,"SP","ACT");
			
			if(LM_RSLSET.next()){
				while(!L_EOF){
					LM_BYRCD = nvlSTRVL(LM_RSLSET.getString("in_byrcd"),"").trim();	
					LM_CNSCD = nvlSTRVL(LM_RSLSET.getString("in_cnscd"),"").trim();	
					LM_INDNO = nvlSTRVL(LM_RSLSET.getString("in_indno"),"").trim();	
					LM_INDDT = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("in_inddt"));	
					LM_AMIDT = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("in_amddt"));	
					LM_AMINO = nvlSTRVL(LM_RSLSET.getString("in_amdno").toString(),"").trim();	
					LM_PORNO = nvlSTRVL(LM_RSLSET.getString("in_porno"),"").trim();	
					LM_PORDT = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("in_pordt"));	
					LM_SALTP = nvlSTRVL(LM_RSLSET.getString("in_saltp").toString(),"").trim();	
					LM_DTPCD = nvlSTRVL(LM_RSLSET.getString("in_dtpcd"),"").trim();	
					LM_DSRCD = nvlSTRVL(LM_RSLSET.getString("in_dsrcd"),"").trim();	
					LM_CPTVL = nvlSTRVL(LM_RSLSET.getString("in_cptvl"),"").trim();	
					LM_DORNO = nvlSTRVL(LM_RSLSET.getString("dot_dorno"),"").trim();	
					LM_DORDT = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dot_dordt"));	
					LM_AMDDT = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dot_amddt"));	
					LM_AMDNO = nvlSTRVL(LM_RSLSET.getString("dot_amdno"),"").trim();	
					LM_TMOCD = nvlSTRVL(LM_RSLSET.getString("dot_tmocd"),"").trim();	
					LM_LRYNO = nvlSTRVL(LM_RSLSET.getString("dot_lryno"),"").trim();	
					LM_MKTTP = nvlSTRVL(LM_RSLSET.getString("dot_mkttp"),"").trim();	
                    LM_TRPCD = nvlSTRVL(LM_RSLSET.getString("dot_TRPCD"),"").trim(); 
					LM_PRDDS = nvlSTRVL(LM_RSLSET.getString("dot_prdds"),"").trim();	
					LM_LOTRF = nvlSTRVL(LM_RSLSET.getString("dot_lotrf"),"").trim();	
					LM_DORPK = nvlSTRVL(LM_RSLSET.getString("dot_dorpk"),"").trim();	
					LM_DORQT = nvlSTRVL(LM_RSLSET.getString("dot_dorqt"),"").trim();	
					LM_DELTP = nvlSTRVL(LM_RSLSET.getString("dot_deltp"),"").trim();	
					LM_AUTDT = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dot_autdt"));	
					LM_BASRT = nvlSTRVL(LM_RSLSET.getString("int_basrt"),"").trim();	
					LM_EXCRT = nvlSTRVL(LM_RSLSET.getString("int_excrt"),"").trim();	
					LM_DSPDT = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dod_dspdt"));	
					LM_DODQT = nvlSTRVL(LM_RSLSET.getString("dod_dorqt"),"").trim();	
					
					
					LM_BYRCD1 = LM_BYRCD;	
					LM_CNSCD1 = LM_CNSCD;	
					LM_INDNO1 = LM_INDNO;	
					LM_INDDT1 = LM_INDDT;	
					LM_AMIDT1 = LM_AMIDT;	
					LM_AMINO1 = LM_AMINO;	
					LM_PORNO1 = LM_PORNO;	
					LM_PORDT1 = LM_PORDT;	
					LM_SALTP1 = LM_SALTP;	
					LM_DTPCD1 = LM_DTPCD;	
					LM_DSRCD1 = LM_DSRCD;	
					LM_CPTVL1 = LM_CPTVL;	
					LM_DORNO1 = LM_DORNO;	
					LM_DORDT1 = LM_DORDT;	
					LM_AMDDT1 = LM_AMDDT;	
					LM_AMDNO1 = LM_AMDNO;	
					LM_TMOCD1 = LM_TMOCD;	
					LM_LRYNO1 = LM_LRYNO;	
					LM_MKTTP1 = LM_MKTTP;	
                    LM_TRPCD1 = LM_TRPCD; 
					LM_PRDDS1 = LM_PRDDS;	
					LM_LOTRF1 = LM_LOTRF;	
					LM_DORPK1 = LM_DORPK;	
					LM_DORQT1 = LM_DORQT;	
					LM_DELTP1 = LM_DELTP;	
					LM_AUTDT1 = LM_AUTDT;	
					LM_BASRT1 = LM_BASRT;	
					LM_EXCRT1 = LM_EXCRT;	
					LM_DSPDT1 = LM_DSPDT;	
					LM_DODQT1 = LM_DODQT;	
						
					LM_BYRCD2 = LM_BYRCD;	
					LM_CNSCD2 = LM_CNSCD;	
					LM_INDNO2 = LM_INDNO;	
					LM_INDDT2 = LM_INDDT;	
					LM_AMIDT2 = LM_AMIDT;	
					LM_AMINO2 = LM_AMINO;	
					LM_PORNO2 = LM_PORNO;	
					LM_PORDT2 = LM_PORDT;	
					LM_SALTP2 = LM_SALTP;	
					LM_DTPCD2 = LM_DTPCD;	
					LM_DSRCD2 = LM_DSRCD;	
					LM_CPTVL2 = LM_CPTVL;	
					LM_DORNO2 = LM_DORNO;	
					LM_DORDT2 = LM_DORDT;	
					LM_AMDDT2 = LM_AMDDT;	
					LM_AMDNO2 = LM_AMDNO;	
					LM_TMOCD2 = LM_TMOCD;	
					LM_LRYNO2 = LM_LRYNO;	
					LM_MKTTP2 = LM_MKTTP;	
                    LM_TRPCD2 = LM_TRPCD; 
					LM_PRDDS2 = LM_PRDDS;	
					LM_LOTRF2 = LM_LOTRF;	
					LM_DORPK2 = LM_DORPK;	
					LM_DORQT2 = LM_DORQT;	
					LM_DELTP2 = LM_DELTP;	
					LM_AUTDT2 = LM_AUTDT;	
					LM_BASRT2 = LM_BASRT;	
					LM_EXCRT2 = LM_EXCRT;	
					LM_DSPDT2 = LM_DSPDT;	
					LM_DODQT2 = LM_DODQT;	
						
					prnHEADER(L_PREPN);
					prnGRPHDR("DON",L_PREPN);
					LM_DORNO1 = LM_DORNO;
					while(LM_DORNO.equals(LM_DORNO1) && !L_EOF){
						prnGRPHDR("GRD",L_PREPN);
						LM_DORNO = LM_DORNO2;
						LM_DORNO1 = LM_DORNO;
						while((LM_DORNO+LM_PRDDS+LM_LOTRF+LM_DORPK+LM_DORQT+LM_BASRT+LM_EXCRT).equals(LM_DORNO1+LM_PRDDS1+LM_LOTRF1+LM_DORPK1+LM_DORQT1+LM_BASRT1+LM_EXCRT1) && !L_EOF){
							LM_DORNO = LM_DORNO2;
							LM_DORNO1 = LM_DORNO;
							LM_PRDDS = LM_PRDDS2;
							LM_PRDDS1 = LM_PRDDS;
							LM_LOTRF = LM_LOTRF2;
							LM_LOTRF1 = LM_LOTRF;
							LM_DORPK = LM_DORPK2;
							LM_DORPK1 = LM_DORPK;
							LM_DORQT = LM_DORQT2;
							LM_DORQT1 = LM_DORQT;
							LM_BASRT = LM_BASRT2;
							LM_BASRT1 = LM_BASRT;
							LM_EXCRT = LM_EXCRT2;
							LM_EXCRT1 = LM_EXCRT;
							while((LM_DORNO+LM_PRDDS+LM_LOTRF+LM_DORPK+LM_DORQT+LM_BASRT+LM_EXCRT+LM_DSPDT+LM_DODQT).equals(LM_DORNO1+LM_PRDDS1+LM_LOTRF1+LM_DORPK1+LM_DORQT1+LM_BASRT1+LM_EXCRT1+LM_DSPDT1+LM_DODQT1) && !L_EOF){
								if(!LM_RSLSET.next()){
									L_EOF = true;
									break;
								}
								
								LM_BYRCD2 = nvlSTRVL(LM_RSLSET.getString("in_byrcd"),"").trim();	
								LM_CNSCD2 = nvlSTRVL(LM_RSLSET.getString("in_cnscd"),"").trim();	
								LM_INDNO2 = nvlSTRVL(LM_RSLSET.getString("in_indno"),"").trim();	
								LM_INDDT2 = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("in_inddt"));	
								LM_AMIDT2 = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("in_amddt"));	
								LM_AMINO2 = nvlSTRVL(LM_RSLSET.getString("in_amdno"),"").trim();	
								LM_PORNO2 = nvlSTRVL(LM_RSLSET.getString("in_porno"),"").trim();	
								LM_PORDT2 = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("in_pordt"));	
								LM_SALTP2 = nvlSTRVL(LM_RSLSET.getString("in_saltp"),"").trim();	
								LM_DTPCD2 = nvlSTRVL(LM_RSLSET.getString("in_dtpcd"),"").trim();	
								LM_DSRCD2 = nvlSTRVL(LM_RSLSET.getString("in_dsrcd"),"").trim();	
								LM_CPTVL2 = nvlSTRVL(LM_RSLSET.getString("in_cptvl"),"").trim();	
								LM_DORNO2 = nvlSTRVL(LM_RSLSET.getString("dot_dorno"),"").trim();	
								LM_DORDT2 = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dot_dordt"));	
								LM_AMDDT2 = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dot_amddt"));	
								LM_AMDNO2 = nvlSTRVL(LM_RSLSET.getString("dot_amdno"),"").trim();	
								LM_TMOCD2 = nvlSTRVL(LM_RSLSET.getString("dot_tmocd"),"").trim();	
								LM_LRYNO2 = nvlSTRVL(LM_RSLSET.getString("dot_lryno"),"").trim();	
								LM_MKTTP2 = nvlSTRVL(LM_RSLSET.getString("dot_mkttp"),"").trim();	
                                LM_TRPCD2 = nvlSTRVL(LM_RSLSET.getString("dot_TRPCD"),"").trim(); 
								LM_PRDDS2 = nvlSTRVL(LM_RSLSET.getString("dot_prdds"),"").trim();	
								LM_LOTRF2 = nvlSTRVL(LM_RSLSET.getString("dot_lotrf"),"").trim();	
								LM_DORPK2 = nvlSTRVL(LM_RSLSET.getString("dot_dorpk"),"").trim();	
								LM_DORQT2 = nvlSTRVL(LM_RSLSET.getString("dot_dorqt"),"").trim();	
								LM_DELTP2 = nvlSTRVL(LM_RSLSET.getString("dot_deltp"),"").trim();	
								LM_AUTDT2 = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dot_autdt"));	
								LM_BASRT2 = nvlSTRVL(LM_RSLSET.getString("int_basrt"),"").trim();	
								LM_EXCRT2 = nvlSTRVL(LM_RSLSET.getString("int_excrt"),"").trim();	
								LM_DSPDT2 = cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("dod_dspdt"));	
								LM_DODQT2 = nvlSTRVL(LM_RSLSET.getString("dod_dorqt"),"").trim();	
								
								LM_DORNO = LM_DORNO2;
								LM_PRDDS = LM_PRDDS2;
								LM_LOTRF = LM_LOTRF2;
								LM_DORPK = LM_DORPK2;
								LM_DORQT = LM_DORQT2;
								LM_BASRT = LM_BASRT2;
								LM_EXCRT = LM_EXCRT2;
								LM_DSPDT = LM_DSPDT2;
								LM_DODQT = LM_DODQT2;
							}
							prnGRPTOT("SCH",L_PREPN);
							intGRPTOT("SCH");
						}
						intGRPTOT("GRD");
					}
					prnFOOTR(L_PREPN);
					prnFMTCHR(O_DOUT,M_EJT);
					intGRPTOT("DON");
				}
				LM_RSLSET.close();
				O_DOUT.close();
				O_FOUT.close();
				setMSG(" ",'N');
			}else{
				setMSG("No record exist.",'E');
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"prnALLREC","");
		}
	}
	
	public static DataOutputStream crtDTOUTSTR(FileOutputStream outfile){
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}
	public static FileOutputStream crtFILE(String strFILE){
		FileOutputStream outFILE = null;
		try{
			File file = new File(strFILE);
			outFILE = new FileOutputStream(file);
        	return outFILE;
		}
		catch(IOException L_IO){
			System.out.println("L_IO FOS...........:"+L_IO);
			return outFILE;		
		}
	}
	
	private void prnHEADER(boolean LP_PREPN){
		try{
			O_DOUT.writeBytes("\n");
                        //prnFMTCHR(O_DOUT,M_BOLD);
			if(LP_PREPN){
				O_DOUT.writeBytes(padSTRING('L'," ",110));
				crtLINE(30);
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',LM_ISODCA,140));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',LM_ISODCB,140));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',LM_ISODCC,140));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L'," ",110));
				crtLINE(30);
				O_DOUT.writeBytes("\n");
			}else{
				O_DOUT.writeBytes(padSTRING('C',"SUPREME PETROCHEM LTD.",110));
				crtLINE(30);
				O_DOUT.writeBytes("\n");
				String L_ADRESS = "17/18, SHAH INDUSTRIAL ESTATE, VEERA DESAI ROAD, ANDHERI(WEST), MUMBAI - 400 053";
				O_DOUT.writeBytes(padSTRING('C',L_ADRESS,110));
				O_DOUT.writeBytes(padSTRING('L',LM_ISODCA,30));
				O_DOUT.writeBytes("\n");
				L_ADRESS = "TEL : 91-22-26311839-42 FAX: 26902362,26367317,26324828 E-MAIL: marketing@spl.co.in";
				O_DOUT.writeBytes(padSTRING('C',L_ADRESS,110));
				O_DOUT.writeBytes(padSTRING('L',LM_ISODCB,30));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',LM_ISODCC,140));
				O_DOUT.writeBytes("\n");
				if(LM_AMDNO1.equals("00"))
					O_DOUT.writeBytes(padSTRING('C',"DELIVERY ORDER",110));
				else
					O_DOUT.writeBytes(padSTRING('C',"D.O. AMENDMENT",110));
				crtLINE(30);
				O_DOUT.writeBytes("\n");
				crtLINE(140);
			}
                                //prnFMTCHR(O_DOUT,M_NOBOLD);
			O_DOUT.writeBytes("\n");
		}catch(Exception L_EX){
			showEXMSG(L_EX,"prnHEADER","");
		}
	}	
	
	private void prnGRPHDR(String LP_GRPCT,boolean LP_PREPN){
		try{
                        //prnFMTCHR(O_DOUT,M_BOLD);
			if(LP_GRPCT.equals("DON")){
				boolean L_PIPFL = true;
				if(LP_PREPN){
					LM_BYHDR = " ";
					LM_CNHDR = " ";
					LM_DONHD = " ";
					LM_DODHD = " ";
					LM_AMDHD = "Amd. Date.        :";
					LM_INNHD = " ";
					LM_INDHD = " ";
					LM_INAHD = "Amd. Date         :";
					LM_PONHD = " ";
					LM_PODHD = " ";
					LM_SLTHD = " ";
					LM_DETHD = "Del. Type         :";
					LM_TRSHD = " ";
					LM_CSTHD = " ";
					LM_STNHD = " ";
					LM_ECCHD = " ";
					LM_DSTHD = " ";
					LM_CDTHD = " ";
					LM_PYMHD = " ";
					LM_PRDHD = " ";
					LM_TRPHD = " ";
					LM_LRYHD = " ";
					LM_FAXHD = "FAX NO :";
					LM_PIPFL = "";
				}else{
					LM_BYHDR = "Buyer & Address";
					LM_CNHDR = "Consignee Name & Address";
					LM_DONHD = "D.O. No.          :";
					LM_DODHD = "D.O. Date.        :";
					LM_AMDHD = "Amd. Date.        :";
					LM_INNHD = "Indent No.        :";
					LM_INDHD = "Indent Date       :";
					LM_INAHD = "Amd. Date         :";
					LM_PONHD = "P.O. No.          :";
					LM_PODHD = "P.O. Date.        :";
					LM_SLTHD = "Type of Sale      :";
					LM_DETHD = "Del. Type         :";
					LM_TRSHD = "Mode of Transport :";
					LM_CSTHD = "C.S.T. No. : ";
					LM_STNHD = "S.T. No.   : ";
					LM_ECCHD = "ECC No.    : ";
					LM_DSTHD = "DISTRIBUTOR";
					LM_CDTHD = "CREDIT";
					LM_PYMHD = "Period: ";
					LM_PRDHD = "PRODUCT";
					LM_TRPHD = "TRANSPORTER : ";
					LM_LRYHD = "Veh. No.          :";
					LM_FAXHD = "FAX NO :";
					LM_PIPFL = "|";
				}
				if(LM_PIPFL.trim().length() > 0)
					L_PIPFL = true;
				else
					L_PIPFL = false;
				getBYRDTL();
				if(LM_BYRCD1.equals(LM_CNSCD1)){
					LM_CNCOD = "Same as Buyer";
					LM_CNSNM = " ";
					LM_CNAD1 = " ";
					LM_CNAD2 = " ";
					LM_CNAD3 = " ";
					LM_CNAD4 = " ";
					LM_CNPIN = " ";
					LM_CNCST = " ";
					LM_CNSTN = " ";
					LM_CNECC = " ";
				}else
					getCNSDTL();
				O_DOUT.writeBytes(padSTRING('R',LM_BYHDR,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNHDR,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_DONHD,20));
				if(LM_AMDNO1.length() > 0)
					O_DOUT.writeBytes(padSTRING('R',LM_DORNO1+"/"+LM_AMDNO1,25));
				else
					O_DOUT.writeBytes(padSTRING('R',LM_DORNO1,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_BYRCD1,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNCOD,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_DODHD,20));
				O_DOUT.writeBytes(padSTRING('R',LM_DORDT1,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_BYRNM,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNSNM,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_AMDHD,20));
				O_DOUT.writeBytes(padSTRING('R',LM_AMDDT1,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_BYAD1,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNAD1,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				if(L_PIPFL)
					crtLINE(40);
				else
					O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,40));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_BYAD2,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNAD2,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_INNHD,20));
				if(LM_AMINO1.length() > 0)
					O_DOUT.writeBytes(padSTRING('R',LM_INDNO1+"/"+LM_AMINO1,25));
				else
					O_DOUT.writeBytes(padSTRING('R',LM_INDNO1,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_BYAD3,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNAD3,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_INDHD,20));
				O_DOUT.writeBytes(padSTRING('R',LM_INDDT1,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_BYAD4,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNAD4,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_AMDHD,20));
				O_DOUT.writeBytes(padSTRING('R',LM_AMIDT1,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_BYPIN,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CNPIN,40));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				if(L_PIPFL)
					crtLINE(40);
				else
					O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,40));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,50));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,55));
				O_DOUT.writeBytes(padSTRING('R',LM_PONHD,20));
				O_DOUT.writeBytes(padSTRING('R',LM_PORNO1,25));
				O_DOUT.writeBytes("\n");
				if(L_PIPFL)
					crtLINE(104);
				else
					O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,104));
				O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,1));
				O_DOUT.writeBytes(padSTRING('R',LM_PODHD,20));
				O_DOUT.writeBytes(padSTRING('R',LM_PORDT1,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_CSTHD,15));
				O_DOUT.writeBytes(padSTRING('R',LM_BYCST,25));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CSTHD,15));
				O_DOUT.writeBytes(padSTRING('R',LM_CNCST,25));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				if(L_PIPFL)
					crtLINE(40);
				else
					O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,40));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_STNHD,15));
				O_DOUT.writeBytes(padSTRING('R',LM_BYSTN,25));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_STNHD,15));
				O_DOUT.writeBytes(padSTRING('R',LM_CNSTN,25));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_SLTHD,20));
                                LM_SLTYP = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","SYS","MR00SAL",LM_SALTP1);
				O_DOUT.writeBytes(padSTRING('R',LM_SLTYP,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_ECCHD,15));
				O_DOUT.writeBytes(padSTRING('R',LM_BYECC,25));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_ECCHD,15));
				O_DOUT.writeBytes(padSTRING('R',LM_CNECC,25));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_DETHD,20));
				LM_DLTYP = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","SYS","MRXXDTP",LM_DTPCD1);
				O_DOUT.writeBytes(padSTRING('R',LM_DLTYP,25));
				O_DOUT.writeBytes("\n");
				if(L_PIPFL)
					crtLINE(104);
				else
					O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,104));
				O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,1));
				O_DOUT.writeBytes(padSTRING('R',LM_TRSHD,20));
				LM_TRMOD = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","SYS","MRXXMOT",LM_TMOCD1);
				O_DOUT.writeBytes(padSTRING('R',LM_TRMOD,25));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_DSTHD,60));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_CDTHD,20));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes("\n");
				LM_DISTR = cl_dat.ocl_dat.getPRMPRT("PT_PRTNM","D",LM_DSRCD1);	
				O_DOUT.writeBytes(padSTRING('R',LM_DISTR,60));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
				O_DOUT.writeBytes(padSTRING('R',LM_PYMHD,8));
				O_DOUT.writeBytes(padSTRING('R',LM_CPTVL1+" Days",12));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
				O_DOUT.writeBytes(padSTRING('R',LM_LRYHD,20));
				O_DOUT.writeBytes(padSTRING('R',LM_LRYNO1,25));
				O_DOUT.writeBytes("\n");
				if(L_PIPFL)
					crtLINE(140);
				else
					O_DOUT.writeBytes(padSTRING('R',LM_PIPFL,140));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R',LM_PRDHD,60));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
                LM_TRNSP = cl_dat.ocl_dat.getPRMPRT("PT_PRTNM","T",LM_TRPCD1); 
				O_DOUT.writeBytes(padSTRING('R',LM_TRPHD,14));
				O_DOUT.writeBytes(padSTRING('R',LM_TRNSP,56));
				O_DOUT.writeBytes("\n");
				LM_PRDTP = cl_dat.ocl_dat.getPRMCOD("CMT_CODDS","MST","COXXMKT",LM_MKTTP1);
				O_DOUT.writeBytes(padSTRING('R',LM_PRDTP,60));
				O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,10));
                LM_FAXNO = cl_dat.ocl_dat.getPRMPRT("PT_FAXNO","T",LM_TRPCD1); 
				O_DOUT.writeBytes(padSTRING('R'," ",14));
				O_DOUT.writeBytes(padSTRING('R',LM_FAXHD+LM_FAXNO,56));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes("\n");
				if(L_PIPFL){
					crtLINE(140);
					O_DOUT.writeBytes("\n");
					O_DOUT.writeBytes(padSTRING('R',"Grade",17));
					O_DOUT.writeBytes(padSTRING('R',"Lot Nos.",13));
					O_DOUT.writeBytes(padSTRING('L',"No. of Bags",25));
					O_DOUT.writeBytes(padSTRING('L',"Qty.",15));
					O_DOUT.writeBytes(padSTRING('L',"Basic Rate",20));
					O_DOUT.writeBytes(padSTRING('L',"Excise Rate",15));
					O_DOUT.writeBytes(padSTRING('C',"Schedule",35));
					O_DOUT.writeBytes("\n");
					O_DOUT.writeBytes(padSTRING('R'," ",55));
					O_DOUT.writeBytes(padSTRING('L',"MT.",15));
					O_DOUT.writeBytes(padSTRING('L',"Rs./MT",20));
					O_DOUT.writeBytes(padSTRING('L',"",15));
					O_DOUT.writeBytes(padSTRING('C',"Date",25));
					O_DOUT.writeBytes(padSTRING('C',"Qty.",10));
					O_DOUT.writeBytes("\n");
					crtLINE(140);
				}else{
					O_DOUT.writeBytes("\n");
					O_DOUT.writeBytes("\n");
					O_DOUT.writeBytes("\n");
				}
				O_DOUT.writeBytes("\n");
				LM_LINENO += 1;
			}
			else if(LP_GRPCT.equals("GRD")){
				O_DOUT.writeBytes(padSTRING('R',LM_PRDDS1,17));
				O_DOUT.writeBytes(padSTRING('R',LM_LOTRF1,13));
				O_DOUT.writeBytes(padSTRING('L',LM_DORPK1,25));
				O_DOUT.writeBytes(padSTRING('L',LM_DORQT1,15));
				O_DOUT.writeBytes(padSTRING('L',LM_BASRT1,20));
				O_DOUT.writeBytes(padSTRING('L',LM_EXCRT1,15));
				LM_PRNHDR = true;
			}
                        //prnFMTCHR(O_DOUT,M_NOBOLD);
		}catch(Exception L_EX){
			showEXMSG(L_EX,"prnGRPHDR","");
		}
	}
	
	private void prnGRPTOT(String LP_GRPCT,boolean LP_PREPN){
		try{
                        //prnFMTCHR(O_DOUT,M_BOLD);
			if(LP_GRPCT.equals("GRD")){
				O_DOUT.writeBytes(padSTRING('R',LM_PRDDS1,17));
				O_DOUT.writeBytes(padSTRING('R',LM_LOTRF1,13));
				O_DOUT.writeBytes(padSTRING('L',LM_DORPK1,25));
				O_DOUT.writeBytes(padSTRING('L',LM_DORQT1,15));
				O_DOUT.writeBytes(padSTRING('L',LM_BASRT1,20));
				O_DOUT.writeBytes(padSTRING('L',LM_EXCRT1,15));
				LM_PRNHDR = true;
			}
			else if(LP_GRPCT.equals("SCH")){
				if(LM_PRNHDR){
					if(LM_DELTP1.equalsIgnoreCase("I"))
						O_DOUT.writeBytes(padSTRING('C',"IMMEDIATE",35));
					else{
						O_DOUT.writeBytes(padSTRING('C',LM_DSPDT1,25));
						O_DOUT.writeBytes(padSTRING('C',LM_DODQT1,10));
					}
					LM_PRNHDR = false;
				}else{
					O_DOUT.writeBytes(padSTRING('R'," ",105));
					O_DOUT.writeBytes(padSTRING('C',LM_DSPDT1,25));
					O_DOUT.writeBytes(padSTRING('C',LM_DODQT1,10));
					}
			}
                        //prnFMTCHR(O_DOUT,M_NOBOLD);
			O_DOUT.writeBytes("\n");
			LM_LINENO += 1;
		}catch(Exception L_EX){
			showEXMSG(L_EX,"prnGRPTOT","");
		}
	}
	
	private void intGRPTOT(String LP_GRPCT){
		try{
			if(LP_GRPCT.equals("SCH")){
				LM_BYRCD1 = LM_BYRCD;	
				LM_CNSCD1 = LM_CNSCD;	
				LM_INDNO1 = LM_INDNO;	
				LM_INDDT1 = LM_INDDT;	
				LM_AMIDT1 = LM_AMIDT;	
				LM_PORNO1 = LM_PORNO;	
				LM_PORDT1 = LM_PORDT;	
				LM_SALTP1 = LM_SALTP;	
				LM_DTPCD1 = LM_DTPCD;	
				LM_DSRCD1 = LM_DSRCD;	
				LM_CPTVL1 = LM_CPTVL;	
				LM_DORDT1 = LM_DORDT;	
				LM_AMDDT1 = LM_AMDDT;	
				LM_TMOCD1 = LM_TMOCD;	
				LM_LRYNO1 = LM_LRYNO;	
				LM_MKTTP1 = LM_MKTTP;	
                LM_TRPCD1 = LM_TRPCD; 
				LM_DELTP1 = LM_DELTP;	
				LM_AUTDT1 = LM_AUTDT;	
				LM_DSPDT1 = LM_DSPDT;	
				LM_DODQT1 = LM_DODQT;	
			}
			else if(LP_GRPCT.equals("GRD")){
				LM_PRDDS1 = LM_PRDDS;	
				LM_LOTRF1 = LM_LOTRF;	
				LM_DORPK1 = LM_DORPK;	
				LM_DORQT1 = LM_DORQT;	
				LM_BASRT1 = LM_BASRT;	
				LM_EXCRT1 = LM_EXCRT;	
			}
			else if(LP_GRPCT.equals("DON")){
				LM_LINENO = 0;
				LM_DORNO1 = LM_DORNO;
			}
		}catch(Exception L_EX){
			showEXMSG(L_EX,"intGRPTOT","");
		}
	}
	
	private void getBYRDTL(){
		try{
			ResultSet L_RSLSET;
			LM_STRQRY = "Select pt_prtnm,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd,pt_cstno,";
			LM_STRQRY += "pt_stxno,pt_eccno,pt_excno,pt_rngds,pt_divds,pt_cllds,pt_itpno,pt_tstfl";
			LM_STRQRY += " from co_ptmst where pt_prtcd='"+LM_BYRCD1+"' and pt_prttp='C'";
			L_RSLSET = cl_dat.ocl_dat.exeSQLQRY2(LM_STRQRY,"SP","ACT");
			if(L_RSLSET.next()){
				LM_BYRNM = nvlSTRVL(L_RSLSET.getString("pt_prtnm"),"").trim();
				LM_BYAD1 = nvlSTRVL(L_RSLSET.getString("pt_adr01"),"").trim();
				LM_BYAD2 = nvlSTRVL(L_RSLSET.getString("pt_adr02"),"").trim();
				LM_BYAD3 = nvlSTRVL(L_RSLSET.getString("pt_adr03"),"").trim();
				LM_BYAD4 = nvlSTRVL(L_RSLSET.getString("pt_adr04"),"").trim();
				LM_BYPIN = nvlSTRVL(L_RSLSET.getString("pt_pincd"),"").trim();
				LM_BYCST = nvlSTRVL(L_RSLSET.getString("pt_cstno"),"").trim();
				LM_BYSTN = nvlSTRVL(L_RSLSET.getString("pt_stxno"),"").trim();
				LM_BYECC = nvlSTRVL(L_RSLSET.getString("pt_eccno"),"").trim();
				LM_EXCNO = nvlSTRVL(L_RSLSET.getString("pt_excno"),"").trim();
				LM_RNGDS = nvlSTRVL(L_RSLSET.getString("pt_rngds"),"").trim();
				LM_DIVDS = nvlSTRVL(L_RSLSET.getString("pt_divds"),"").trim();
				LM_CLLDS = nvlSTRVL(L_RSLSET.getString("pt_cllds"),"").trim();
				LM_ITPNO = nvlSTRVL(L_RSLSET.getString("pt_itpno"),"").trim();
				LM_TSTFL = nvlSTRVL(L_RSLSET.getString("pt_tstfl"),"").trim();
			}
			L_RSLSET.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getBYRDTL","");
		}
	}
	
	private void getCNSDTL(){
		try{
			ResultSet L_RSLSET;
			LM_STRQRY = "Select pt_prtnm,pt_prtcd,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd,pt_cstno,pt_stxno,pt_eccno";
			LM_STRQRY += " from co_ptmst where pt_prtcd='"+LM_CNSCD1+"' and pt_prttp='C'";
			L_RSLSET = cl_dat.ocl_dat.exeSQLQRY2(LM_STRQRY,"SP","ACT");
			if(L_RSLSET.next()){
				LM_CNSNM = nvlSTRVL(L_RSLSET.getString("pt_prtnm"),"").trim();
				LM_CNCOD = nvlSTRVL(L_RSLSET.getString("pt_prtcd"),"").trim();
				LM_CNAD1 = nvlSTRVL(L_RSLSET.getString("pt_adr01"),"").trim();
				LM_CNAD2 = nvlSTRVL(L_RSLSET.getString("pt_adr02"),"").trim();
				LM_CNAD3 = nvlSTRVL(L_RSLSET.getString("pt_adr03"),"").trim();
				LM_CNAD4 = nvlSTRVL(L_RSLSET.getString("pt_adr04"),"").trim();
				LM_CNPIN = nvlSTRVL(L_RSLSET.getString("pt_pincd"),"").trim();
				LM_CNCST = nvlSTRVL(L_RSLSET.getString("pt_cstno"),"").trim();
				LM_CNSTN = nvlSTRVL(L_RSLSET.getString("pt_stxno"),"").trim();
				LM_CNECC = nvlSTRVL(L_RSLSET.getString("pt_eccno"),"").trim();
			}
			L_RSLSET.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getCNSDTL","");
		}
	}
	
	/**
	 *
	 *Method to create lines that are used in the Reports
	 */
	private void crtLINE(int LM_CNT){
		String strln = "";
		try{
			for(int i=1;i<=LM_CNT;i++){
			 strln += "-";
			}
			//O_DOUT.writeBytes(padSTRING('L'," ",0));
			O_DOUT.writeBytes(strln);
		}catch(Exception L_EX){
			System.out.println("L_EX Error in Line:"+L_EX);
		}
	}
	
	private void prnFOOTR(boolean LP_PREPN){
		try{
			boolean L_PIPFL = false;
			LM_PIPFL = " ";
			String L_WARNG = "";
			String L_WARNG1 = "";
			String L_WARNG2 = "";
			String L_WARNG3 = "";
			String L_WARNG4 = "";
			String L_MESSG = "";
			String L_SPLIN = "";
			String L_MDVAT = "";
			String L_ADR01 = "";
			String L_ADR02 = "";
			String L_EXCRG = "";
			String L_RANGE = "";
			String L_DIVSN = "";
			String L_COLEC = "";
			String L_PANNO = "";
			String L_MRKDV = "";
			String L_AUTSG = "";
			String L_MONTH = LM_DORDT1.substring(3,5).toString().trim();
			if(L_MONTH.length() == 2){
				int L_MNTH = Integer.parseInt(L_MONTH);
                                if(L_MNTH >= 5 && L_MNTH <= 9){
					L_WARNG += "MONSOON ALARM - ";
                                        L_WARNG1 += "TARNSPORTER SHOULD ENSURE WET-FREE TRANSPORTATION OF MATERIAL.THEY SHOULD PROVIDE 3 SETS OF TARPAULIN IN GOOD";
                                        L_WARNG2 += "CONDITION WITH EACH TRUCK.ANY COMPENSATION CLAIMED BY THE BUYER DUE TO WETNESS OR ANY SORT";
					L_WARNG3 += "OF CONTAMINATION OR SPOILAGE ETC. WILL BE ON TRANSPORTER'S ACCOUNT.";
				}
			}
			if(!LP_PREPN){
				L_MESSG += "Validity of this D.O. is for 10 Days from the date of Issue.";
				L_SPLIN += "Special Instruction :";
				L_MDVAT += "Particulars of MODVAT :";
				L_EXCRG += "Exc. Reg. No. :";
				L_MRKDV += "For Marketing Div.";
				L_RANGE += "Range         :";
				L_DIVSN += "Divison       :";
				L_COLEC += "Collectorate  :";
				L_AUTSG += "Authorised Signatory.";
				L_PANNO += "IT PAN No.    :";
				L_ADR01 += "WORKS : VILLAGE AMDOSHI,WAKAN-ROHA,TALUKA : ROHA. DIST : RAIGAD. MAHARASHTRA - 402106";
				L_ADR02 += "TEL :(021442)-2228,2540 - 2548 FAX :021442 - 2337, 2537";
				LM_PIPFL = "|";
			}
			if(LM_PIPFL.trim().length() > 0)
				L_PIPFL = true; 
			O_DOUT.writeBytes("\n");
			LM_LINENO += 1;
			if(LM_TSTFL.equalsIgnoreCase("Y"))
				O_DOUT.writeBytes(padSTRING('C',"****TEST CERTIFICATE REQUIRED****",140));
			O_DOUT.writeBytes("\n");
			LM_LINENO += 1;
			if(LM_LINENO <= 14 ){
				O_DOUT.writeBytes("\n");
                                //prnFMTCHR(O_DOUT,M_BOLD);
				O_DOUT.writeBytes(padSTRING('R',L_WARNG,18));
				O_DOUT.writeBytes(padSTRING('R',L_WARNG1,122));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R'," ",18));
				O_DOUT.writeBytes(padSTRING('R',L_WARNG2,122));
				O_DOUT.writeBytes("\n");
				O_DOUT.writeBytes(padSTRING('R'," ",18));
				O_DOUT.writeBytes(padSTRING('R',L_WARNG3,122));
			}
			LM_LINENO += 3;
			do{
				O_DOUT.writeBytes("\n");
				LM_LINENO += 1;
			}while(LM_LINENO <= 17);
			if(L_PIPFL){
				crtLINE(140);
			}else{
				O_DOUT.writeBytes(padSTRING('R'," ",140));
			}
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_MESSG,140));
			O_DOUT.writeBytes("\n");
			if(L_PIPFL){
				crtLINE(140);
			}else{
				O_DOUT.writeBytes(padSTRING('R'," ",140));
			}
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_SPLIN,25));
			LM_REMDS = getREMDS(LM_DORNO1,LM_MKTTP1,"DO");
			int L_STRLEN = LM_REMDS.length();
			if(L_STRLEN > 115){
				O_DOUT.writeBytes(padSTRING('R',LM_REMDS.substring(0,100),115));
			}else{
				O_DOUT.writeBytes(padSTRING('R',LM_REMDS.substring(0,LM_REMDS.length()),115));
			}
			O_DOUT.writeBytes("\n");
			if(L_PIPFL){
				crtLINE(20);
				O_DOUT.writeBytes(padSTRING('R'," ",5));
			}else{
				O_DOUT.writeBytes(padSTRING('R'," ",25));
			}
			if(L_STRLEN > 115)
				O_DOUT.writeBytes(padSTRING('R',LM_REMDS.substring(100,LM_REMDS.length()),115));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			if(L_PIPFL){
				crtLINE(140);
			}else{
				O_DOUT.writeBytes(padSTRING('R'," ",140));
			}
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_MDVAT,140));
			O_DOUT.writeBytes("\n");
			if(L_PIPFL){
				crtLINE(140);
			}else{
				O_DOUT.writeBytes(padSTRING('R'," ",140));
			}
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_EXCRG,25));
			O_DOUT.writeBytes(padSTRING('R',LM_EXCNO,45));
			O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
			O_DOUT.writeBytes(padSTRING('C',L_MRKDV,55));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_RANGE,25));
			O_DOUT.writeBytes(padSTRING('R',LM_RNGDS,45));
			O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_DIVSN,25));
			O_DOUT.writeBytes(padSTRING('R',LM_DIVDS,45));
			O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_COLEC,25));
			O_DOUT.writeBytes(padSTRING('R',LM_CLLDS,45));
			O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,85));
			O_DOUT.writeBytes(padSTRING('C',L_AUTSG,55));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('R',L_PANNO,25));
			O_DOUT.writeBytes(padSTRING('R',LM_ITPNO,45));
			O_DOUT.writeBytes(padSTRING('L',LM_PIPFL,15));
			O_DOUT.writeBytes(padSTRING('C',LM_AUTDT1,55));
			O_DOUT.writeBytes("\n");
			if(L_PIPFL){
				crtLINE(140);
			}else{
				O_DOUT.writeBytes(padSTRING('R'," ",140));
			}
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('C',L_ADR01,140));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(padSTRING('C',L_ADR02,140));
                        //prnFMTCHR(O_DOUT,M_NOBOLD);
		}catch(Exception L_EX){
			showEXMSG(L_EX,"prnFOOTR","");
		}
	}
	
	private String getREMDS(String LP_DORNO,String LP_MKTTP,String LP_TRNTP){
		String L_REMDS = "";
		try{
			ResultSet L_RSLSET;
			LM_STRQRY = "Select rm_remds from mr_rmmst where rm_docno='"+LP_DORNO+"'";
			LM_STRQRY += " and rm_mkttp='"+LP_MKTTP+"' and rm_trntp='"+LP_TRNTP+"'";
			L_RSLSET = cl_dat.ocl_dat.exeSQLQRY2(LM_STRQRY,"SP","ACT");
			if(L_RSLSET.next())
				L_REMDS = nvlSTRVL(L_RSLSET.getString("rm_remds").toString(),"").trim();
			if(L_RSLSET != null)
				L_RSLSET.close();
		}catch(Exception L_EX){
			showEXMSG(L_EX,"getREMDS","");
		}
		return L_REMDS;
	}
}
