//HML Analysis Report
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Date;
import java.sql.SQLException;
import java.io.*; 

class mm_rphml extends cl_rbase {
	private TxtNumLimit txtHIVAL,txtMIVAL;
	private JButton btnPROCE;
	
	private JCheckBox chbHIVAL,chbMDVAL,chbLOVAL;
	
	float fltHIVAL,fltMIVAL;
	
	private String strSTRTP,strSTRNM;
	private String strMATCD,strMATDS,strUOMCD,strPPOVL,strHMLFL,strSTKQT;
	
	private int intLINNO = 0, intPAGNO = 1;
	
	private String strFILNM = "c:\\reports\\mm_rphml.doc";
	private char chrDATA = 'N';
	
	FileOutputStream F_OUT ;   
    DataOutputStream D_OUT ;
			
	mm_rphml(){
		super(2);
		txtHIVAL = new TxtNumLimit(12.2);
		txtMIVAL = new TxtNumLimit(12.2);
		btnPROCE = new JButton("PROCESS");
		chbHIVAL = new JCheckBox("High");
		chbMDVAL = new JCheckBox("Medium");
		chbLOVAL = new JCheckBox("Low");
		
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_vtrSCCOMP.remove(M_lblTODAT);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		
		setMatrix(20,8);
		add(new JLabel("Processing Option"),2,1,1,2,this,'L');
		add(new JLabel("High Value"),3,1,1,1,this,'L');
		add(txtHIVAL,3,2,1,1,this,'L');
		add(new JLabel("Medium Value"),3,3,1,1,this,'L');
		add(txtMIVAL,3,4,1,1,this,'L');
		add(btnPROCE,3,5,1,1.2,this,'L');
		add(new JLabel("Report Option"),4,1,1,1,this,'L');
		add(chbHIVAL,4,2,1,1,this,'L');
		add(chbMDVAL,4,3,1,1,this,'L');
		add(chbLOVAL,4,4,1,1,this,'L');
		setENBL(false);
	}
	public void actionPerformed(ActionEvent L_AE){
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst){
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
				setENBL(true);
				M_cmbDESTN.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
				setENBL(true);
				txtHIVAL.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPFIL_pbst)){
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst)){
			}
		}
		if(M_objSOURC == btnPROCE){
				float L_fltHIVAL = Float.parseFloat(txtHIVAL.getText());
				float L_fltMIVAL = Float.parseFloat(txtMIVAL.getText());
				if(L_fltHIVAL > L_fltMIVAL)
					exePROCE();
				else{
					setMSG("High Value Should Be Greater Than Minimum Value",'E');
					txtMIVAL.requestFocus();
				}
			}
	}
	public void keyPressed(KeyEvent L_KE){
		super.keyPressed(L_KE);
		
		if(L_KE.getKeyCode() == L_KE.VK_ENTER){
			if(M_objSOURC == txtHIVAL){
				if(txtHIVAL.getText().trim().length() != 0){
					txtMIVAL.requestFocus();
					setMSG("Enter Medium Range",'N');
				}
			}
			if(M_objSOURC == txtMIVAL){
				if(txtMIVAL.getText().trim().length() != 0){
					btnPROCE.requestFocus();
					setMSG("Press Enter To Process Database",'N');
				}
			}
		
		}
	}
	void exePROCE(){
		strSTRTP = M_strSBSCD.substring(2,4);
		try{
			M_strSQLQRY="SELECT CMT_CODDS FROM CO_CDTRN WHERE CMT_CGMTP='SYS' "
				+"AND CMT_CGSTP='MMXXSST' AND CMT_CODCD = '"+strSTRTP+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(M_rstRSSET != null)
				if(M_rstRSSET.next())
					strSTRNM = M_rstRSSET.getString("CMT_CODDS").toString();
		}
		catch(SQLException L_SQLE){
			setMSG(L_SQLE,"Exception in report");
		}

		fltHIVAL = Float.parseFloat(txtHIVAL.getText());
		fltMIVAL = Float.parseFloat(txtMIVAL.getText());
		setMSG("Updation in progres..",'N');
		try{
			setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			M_strSQLQRY = "UPDATE MM_STMST SET ST_HMLFL = ' ' WHERE "
						+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"'";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");	
			cl_dat.M_flgLCUPD_pbst = true;
			M_strSQLQRY = "UPDATE MM_STMST SET ST_HMLFL = 'H' WHERE "
				+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND MAX(ifnull(ST_PPOVL,0),ifnull(ST_WAVRT,0)) >= "+fltHIVAL;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.M_flgLCUPD_pbst = true;
			M_strSQLQRY = "UPDATE MM_STMST SET ST_HMLFL = 'M' WHERE "
				+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND MAX(ifnull(ST_PPOVL,0),ifnull(ST_WAVRT,0)) < "+fltHIVAL+" AND "
				+"MAX(ST_PPOVL,ST_WAVRT) >= "+fltMIVAL+" ";
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.M_flgLCUPD_pbst = true;
			M_strSQLQRY = "UPDATE MM_STMST SET ST_HMLFL = 'L' WHERE "
				+"ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP = '"+strSTRTP+"' AND MAX(ifnull(ST_PPOVL,0),ifnull(ST_WAVRT,0)) < "+fltMIVAL;
			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
			cl_dat.M_flgLCUPD_pbst = true;
			setCursor(cl_dat.M_curDFSTS_pbst);
			if(cl_dat.exeDBCMT("exeSAVE"))
				setMSG("Updation Complete ",'N');
			else
				setMSG("Error in updating.. ",'N');
		}
		catch(Exception L_PRC){
			setMSG("Error in Updation ",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		
	}
	void exeREPRT(){
		try{
			F_OUT = new FileOutputStream(strFILNM);
			D_OUT = new DataOutputStream(F_OUT);
			prnFMTCHR(D_OUT,M_strCPI12);
						
			if(chbHIVAL.isSelected()){
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,MAX(ST_PPOVL,ST_WAVRT)AS MATVL,ST_HMLFL,ST_STKQT FROM MM_STMST "
					+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_HMLFL ='H' AND ST_STRTP = '"+strSTRTP+"' ORDER BY MAX(ST_PPOVL,ST_WAVRT) DESC ";
			}
			if(chbMDVAL.isSelected()){
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,MAX(ST_PPOVL,ST_WAVRT)AS MATVL,ST_HMLFL,ST_STKQT FROM MM_STMST "
					+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_HMLFL ='M' AND ST_STRTP = '"+strSTRTP+"' ORDER BY MAX(ST_PPOVL,ST_WAVRT) DESC ";
			}
			if(chbLOVAL.isSelected()){
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,MAX(ST_PPOVL,ST_WAVRT)AS MATVL,ST_HMLFL,ST_STKQT FROM MM_STMST "
					+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_HMLFL ='L' AND ST_STRTP = '"+strSTRTP+"' ORDER BY MAX(ST_PPOVL,ST_WAVRT) DESC ";
			}
			if(chbHIVAL.isSelected() && chbMDVAL.isSelected()){
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,MAX(ST_PPOVL,ST_WAVRT)AS MATVL,ST_HMLFL,ST_STKQT FROM MM_STMST "
					+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_HMLFL IN ('H','M') AND ST_STRTP = '"+strSTRTP+"' ORDER BY MAX(ST_PPOVL,ST_WAVRT) DESC ";
			}
			if(chbHIVAL.isSelected() && chbLOVAL.isSelected()){
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,MAX(ST_PPOVL,ST_WAVRT)AS MATVL,ST_HMLFL,ST_STKQT FROM MM_STMST "
					+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_HMLFL IN ('H','L') AND ST_STRTP = '"+strSTRTP+"' ORDER BY MAX(ST_PPOVL,ST_WAVRT) DESC ";
			}
			if(chbMDVAL.isSelected() && chbLOVAL.isSelected()){
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,MAX(ST_PPOVL,ST_WAVRT)AS MATVL,ST_HMLFL,ST_STKQT FROM MM_STMST "
					+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_HMLFL IN ('M','L') AND ST_STRTP = '"+strSTRTP+"' ORDER BY MAX(ST_PPOVL,ST_WAVRT) DESC ";
			}
			if(chbHIVAL.isSelected() && chbMDVAL.isSelected() && chbLOVAL.isSelected()){
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_UOMCD,MAX(ST_PPOVL,ST_WAVRT)AS MATVL,ST_HMLFL,ST_STKQT FROM MM_STMST "
					+"WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_HMLFL IN ('H','M','L') AND ST_STRTP = '"+strSTRTP+"' ORDER BY MAX(ST_PPOVL,ST_WAVRT) DESC ";
			}
			
			try{
				int i = 1;
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null){
					genRPHDR();
					while(M_rstRSSET.next()){
						strMATCD = nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),"");
						strMATDS = nvlSTRVL(M_rstRSSET.getString("ST_MATDS"),"");
						strUOMCD = nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),"");
						strPPOVL = nvlSTRVL(M_rstRSSET.getString("MATVL"),"0");
						strHMLFL = nvlSTRVL(M_rstRSSET.getString("ST_HMLFL"),"");
						strSTKQT = nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0");
						
						if(intLINNO > 60){
							D_OUT.writeBytes("\n");
							D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
							D_OUT.writeBytes("\n");
							D_OUT.writeBytes("H - Mat. Rt. >= "+fltHIVAL+" ** M - "+fltHIVAL+" > Mat. Rt. >= "+fltMIVAL+" ** L - Mat. Rt. < "+fltMIVAL);
							prnFMTCHR(D_OUT,M_strEJT);
							intLINNO = 0;
							intPAGNO += 1;
							genRPHDR();
						}
						D_OUT.writeBytes("\n");
						D_OUT.writeBytes(padSTRING('R',String.valueOf(i),7));
						D_OUT.writeBytes(padSTRING('R',strMATCD,11));
						D_OUT.writeBytes(padSTRING('R',strMATDS,46));
						D_OUT.writeBytes(padSTRING('R',strUOMCD,3));
						D_OUT.writeBytes(padSTRING('L',strPPOVL,12));
						D_OUT.writeBytes(padSTRING('L',strHMLFL,4));
						D_OUT.writeBytes(padSTRING('L',strSTKQT,10));
						i++;
						intLINNO += 1;
					}
				}
			}
			catch(SQLException L_SQLE){
				setMSG(L_SQLE,"Exception in report");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			D_OUT.close();
			F_OUT.close();
			intLINNO = 0;
		}
		catch(IOException L_IOEX){
			setMSG(L_IOEX,"File I/O");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	void genRPHDR(){
		try{
			D_OUT.writeBytes(padSTRING('C',"SUPREM PETROCHEM LIMITED",96));
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('R',"HML Analysis Report ",75));
			D_OUT.writeBytes(padSTRING('R',"Date    : "+cl_dat.M_strLOGDT_pbst,20));
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('R',"Stores Type : "+strSTRNM,75));
			D_OUT.writeBytes(padSTRING('R',"Page No : "+intPAGNO,20));
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
			D_OUT.writeBytes("\n");																				  	
			D_OUT.writeBytes("S.N.   Mat. Code  Material Description                          UOM   Mat.Rate Flg  Stock Qty.");
			D_OUT.writeBytes("\n");	
			D_OUT.writeBytes("-----------------------------------------------------------------------------------------------");
			intLINNO += 4;
		}
		catch(Exception L_EX){
			setMSG(L_EX,"Report header");
			
		}
		
	}
	boolean valDATA(){
		if(chbHIVAL.isSelected() || chbMDVAL.isSelected() || chbLOVAL.isSelected())
		{
			setMSG(" ",'N');
			return true;
		}
		else{
			setMSG("Select At Least One Report Option",'E');
			chbHIVAL.requestFocus();
			return false;
		}
	}
	void exePRINT(){
		if(valDATA()){
			exeREPRT();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)){
				doPRINT(strFILNM);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst)){
				try{
					Runtime r = Runtime.getRuntime();
					Process P = null;
					P = r.exec("c:\\windows\\wordpad.exe "+strFILNM); 
				}
				catch(Exception L_EX){
					setMSG(L_EX,"exePRINT");
				}
			}
		}
	}
}
