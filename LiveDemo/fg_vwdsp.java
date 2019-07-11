/**  Despatch status display banner
 */

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
//import cl_dat;
//import cl_cust;
import java.text.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class fg_vwdsp extends JFrame implements ActionListener
{
  String[] LM_TBLHD = {"Grade","Sale Type","L.A. No.","L.A.Date","Qty.","Pkg.Type","Customer","D.O. No.","Transporter","Lorry No.","Contnr.No.","Wt.Period","Status"};
  int[] LM_COLSZ = {40,20,20,20,20,20,110,20,110,50,20,20,20};	 
  //cl_dat ocl_dat = new cl_dat();
  ResultSet LM_RSLSET,LM_RSLSET1;
  //String msg = "                                            Collecting Information , Please wait ...";
  //String msg1 = "                                           Collecting Information , Please wait ...";
  String msg = "Collecting Information ...";
  String msg1 = "Collecting Information ...";
  JLabel lblMSG,lblDSP,lblOPT;
  JWindow	  wndMAINWD,wndDSPTBL;
  JDialog wndMAINDL;
  JTable LM_DTLTBL,tblDSPDTL;
  JPanel pnlDSPTBL,pnlDSPREC;
  JButton btnREFSH, btnEXIT, btnDTLON, btnDTLOF;
  //JRadioButton rdbOTHER,rdbXPS;
  JRadioButton rdbGRDOPT,rdbCUSOPT,rdbTRPOPT,rdbSALOPT;
  ButtonGroup grpSELOPT;
  //ButtonGroup grpXPSOPT;
  Vector<String> LM_DPVCTR;
  NewThread t1,t2;
  Cursor curWTSTS = new Cursor(Cursor.WAIT_CURSOR);
  Cursor curDFSTS = new Cursor(Cursor.DEFAULT_CURSOR);
  private static boolean M_flgXPSFL_prst = false;
  private static String M_strCMPCD_prst = "01";
  private static String M_strCMPNM_prst = "";
  int TB_BUYER = 0;
  int TB_DONUM = 1;
  int TB_TRNPT = 2;
  int TB_LORYN = 3;
  int TB_STATS = 4;
  
  int sl_time = 200;
  int LM_CNT = 0;
  
  String LM_STRSQL = "";
  String LM_ACTTXT,LM_DSPQT,LM_LDQTY,LM_PRVDT,LM_REPQT,LM_CFWQT_R,LM_CFWQT_L,LM_DATFMT,LM_MTDSP,LM_TODAT;
  
  String[][] arrMNTH = {{"01","31"},{"02","28"},{"03","31"},
						  {"04","30"},{"05","31"},{"06","30"},
						  {"07","31"},{"08","31"},{"09","30"},
						  {"10","31"},{"11","30"},{"12","31"},
						 };
  String[]arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};
  fg_vwdsp(){
		opnDBCON();
	  	getDSPMSG();
		LM_DATFMT = "DMY";
		LM_TODAT = setDATE(new java.util.Date());
		LM_PRVDT = getABDATE(LM_TODAT,1,'B');
		LM_DSPQT = getALLQT("ivt_invqt","date(ivt_invdt)",LM_PRVDT,LM_PRVDT);
		//System.out.println("LM_TODAT : "+LM_TODAT);
		//do{
		//	LM_DATFMT = "DMY";
		//	LM_PRVDT = getABDATE(LM_TODAT,1,'B');
		//	//System.out.println("LM_PRVDT : "+LM_PRVDT);
		//	LM_DSPQT = getALLQT("ivt_invqt","date(ivt_invdt)",LM_PRVDT,LM_PRVDT);
		//	LM_TODAT = LM_PRVDT;
		//}while(LM_DSPQT.equals("0.000"));
		String L_STRDT = "01" + LM_PRVDT.substring(2,5).trim() + LM_PRVDT.substring(5,10).trim();
		LM_DATFMT = "DMY";
		LM_PRVDT = getABDATE(setDATE(new java.util.Date()),1,'B');
		//System.out.println("L_STRDT : "+L_STRDT);
		LM_MTDSP = getALLQT("ivt_invqt","date(ivt_invdt)",L_STRDT,LM_PRVDT);
		String L_DSPSTR = "Despatches            Prv.Day : "+LM_DSPQT+"      Monthly : "+LM_MTDSP+ (M_flgXPSFL_prst ? "(SQM)" : "(MT)");
		lblDSP.setText(L_DSPSTR);
		//super.hide();
		//this.hide();
		t1 = new NewThread("Fetch");
		t2 = new NewThread("Display");
	}
  
  private void opnDBCON(){
	  try{
		//cl_dat.ocl_dat.M_conSPDBA_pbst = cl_dat.ocl_dat.setCONDTB("01","spldata","FIMS","FIMS");
		setCONACT("01","spldata","FIMS","FIMS");		
		if(cl_dat.M_conSPDBA_pbst != null){
			cl_dat.M_stmSPDBA_pbst = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			cl_dat.M_stmSPDBQ_pbst  = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			cl_dat.M_stmSPDBQ_pbst1 = chkCONSTM(cl_dat.M_conSPDBA_pbst,"");
			}
		
	  }catch(Exception L_EX){
		  System.out.println("opnDBCON: "+L_EX);
	  }
  }
  
  private void clsDBCON(){
	  try{
		if(cl_dat.M_conSPDBA_pbst != null){
			cl_dat.M_conSPDBA_pbst.commit();
			cl_dat.M_stmSPDBA_pbst.close();
			cl_dat.M_stmSPDBQ_pbst.close();
			cl_dat.M_stmSPDBQ_pbst1.close();
			cl_dat.M_conSPDBA_pbst.close();
		}
	  }catch(Exception L_EX){
		  System.out.println("clsDBCON: "+L_EX);
	  }
  }
  
  
  public static void main(String args[])
	{
		if (args.length > 0)
        {
            M_strCMPCD_prst = args[0];
            M_flgXPSFL_prst = (args[1].equals("XPS")? true : false);
		}
        fg_vwdsp ofg_vwdsp = new fg_vwdsp();
        //ofg_vwdsp.show();
		ofg_vwdsp.setVisible(true);
	}
  
  public Statement chkCONSTM(Connection LP_CONVAL,String LP_QRYTP){
	try{
		if (LP_CONVAL != null){
			if(LP_QRYTP.equals("Q"))	
				return LP_CONVAL.createStatement();
			else if(LP_QRYTP.equals(""))
				return LP_CONVAL.createStatement();
			}
		}catch(Exception L_EX){}
			return null;			
		}
  
  private String setDATE(java.util.Date oDT){
		String strCURDT;
        SimpleDateFormat dtFORM;
        if (LM_DATFMT.trim().equals("DMY"))
            dtFORM = new SimpleDateFormat("dd/MM/yyyy");
        else 
            dtFORM = new SimpleDateFormat("MM/dd/yyyy");
		strCURDT = dtFORM.format(oDT);
		LM_DATFMT = " ";
		return strCURDT;
	}
  
	private String setDATFMT(String LP_DATSTR){
		if(LP_DATSTR != null){
			if(LP_DATSTR.trim().length() == 8){
				String L_CHRDT = LP_DATSTR.substring(0,2)+"/"+LP_DATSTR.substring(3,5)+"/"+"20"+LP_DATSTR.substring(6,LP_DATSTR.length());
				return L_CHRDT;
			}else
				return "";
		}else
			return "";
	}
	
	private String padSTRING(char LP_PADTP,String LP_STRVL,int LP_PADLN){
		String L_RTNSTR = "";
		try{
			String L_STRSP = " ";
			int L_STRLN = LP_STRVL.length();
			int L_STRDF = LP_PADLN - L_STRLN;
			StringBuffer L_STRBUF;
			if(LP_PADLN <= L_STRLN){
				L_RTNSTR = LP_STRVL;
			}else{
				switch(LP_PADTP){
					case 'C':
						L_STRDF = L_STRDF / 2;
						L_STRBUF = new StringBuffer(L_STRDF);
						for(int j = 0;j < L_STRBUF.capacity();j++)
							L_STRBUF.insert(j,' ');
						L_RTNSTR =  L_STRBUF+LP_STRVL+L_STRBUF ;
						break;
					case 'R':
						L_STRBUF = new StringBuffer(L_STRDF);
						for(int j = 0;j < L_STRBUF.capacity();j++)
							L_STRBUF.insert(j,' ');
						L_RTNSTR =  LP_STRVL+L_STRBUF ;
						break;	
					case 'L':
						L_STRBUF = new StringBuffer(L_STRDF);
						for(int j = 0;j < L_STRBUF.capacity();j++)
							L_STRBUF.insert(j,' ');
						L_RTNSTR =  L_STRBUF+LP_STRVL ;
						break;		
				}
			}
		}catch(Exception L_EX){
			System.out.println("padSTRING: "+L_EX);
		}
		return L_RTNSTR;
	}
	
	private String getABDATE(String LP_STRDT,int LP_DDCNT,char LP_ABFLG) 
	{
	  // remark Max limit for adding or removing days = 28
		
		int L_YRVAL = Integer.parseInt(LP_STRDT.substring(6,10));
	  if(L_YRVAL%4 == 0)
		  arrDAYS[1] = "29";
	  else
		  arrDAYS[1] = "28";
	  if(LP_STRDT.length()>=10) 
	  {
		  int L_DDVAL = Integer.parseInt(LP_STRDT.substring(0,2));
		  int L_MMVAL = Integer.parseInt(LP_STRDT.substring(3,5));
		  L_YRVAL = Integer.parseInt(LP_STRDT.substring(6,10));
		  if(LP_ABFLG == 'A')
		  {
			  L_DDVAL += LP_DDCNT;
			  if(L_DDVAL > Integer.parseInt(arrDAYS[L_MMVAL -1]))
			  {
				  if(L_MMVAL != 12)
				  {
					  L_DDVAL = L_DDVAL - Integer.parseInt(arrDAYS[L_MMVAL -1]);
					  L_MMVAL += 1; 
					  }
				 else 
				 {
					L_DDVAL = L_DDVAL - Integer.parseInt(arrDAYS[L_MMVAL -1]); 
					L_MMVAL = 1; 
					L_YRVAL +=1;  
					}
				}
		  }
		  else if(LP_ABFLG == 'B')
		  {
			  L_DDVAL -= LP_DDCNT;
			  if(L_DDVAL <=0)
			  {
				  if(L_MMVAL!= 1)
				  {
					  L_MMVAL -= 1; 
				  }
				  else
				  {
					  L_MMVAL = 12;
					  L_YRVAL -= 1; 
				  }
				  L_DDVAL = L_DDVAL + Integer.parseInt(arrDAYS[L_MMVAL -1]);
			  }
		  }
		  String L_STRDD = L_DDVAL + " ";
		  if(L_STRDD.trim().length() == 1)
			  L_STRDD = "0"+L_STRDD.trim();
		  
		  String L_STRMM = L_MMVAL + " ";
		  if(L_STRMM.trim().length() == 1)
			  L_STRMM = "0"+L_STRMM.trim();
		  
		  String L_STRRTN = L_STRDD.trim() + "/" + L_STRMM.trim() + "/" + L_YRVAL;
		  //System.out.println(L_STRRTN);
		  return L_STRRTN;
	  }
	  else 
		  return "";
	  
	}
	
  public void actionPerformed(ActionEvent L_AE){
        LM_ACTTXT = L_AE.getActionCommand();
		if(L_AE.getSource().equals("+"))
          sl_time -= 10;
        if(L_AE.getSource().equals("-"))
          sl_time += 10;
	}

  private void getDSPMSG(){
	  try{
			pnlDSPTBL = new JPanel();		  
			pnlDSPREC = new JPanel();
			pnlDSPREC.removeAll();
			pnlDSPREC.setLayout(null);
			Font f = new Font("TimesRoman",Font.PLAIN,15);
			lblMSG = new JLabel(padSTRING('C'," ",116));
			lblDSP = new JLabel(padSTRING('C'," ",116));
			lblOPT = new JLabel(padSTRING('L'," ",30));
			btnREFSH= new JButton("Refresh");
			btnEXIT= new JButton("Exit");
			btnDTLON= new JButton("Detail On");
			btnDTLOF= new JButton("Detail Off");
			//rdbOTHER = new JRadioButton("Others",true);
			//rdbXPS  = new JRadioButton("XPS ",false);
			//grpXPSOPT = new ButtonGroup();
			//grpXPSOPT.add(rdbOTHER);
			//grpXPSOPT.add(rdbXPS);

			rdbGRDOPT = new JRadioButton("Grade",true);
			rdbSALOPT = new JRadioButton("Sale Type.",false);

            rdbCUSOPT = new JRadioButton("Customer",false);
            rdbTRPOPT = new JRadioButton("Transporter",false);
			grpSELOPT = new ButtonGroup();
			grpSELOPT.add(rdbSALOPT);
			grpSELOPT.add(rdbGRDOPT);
			grpSELOPT.add(rdbCUSOPT);
            grpSELOPT.add(rdbTRPOPT);
			//rdbOTHER.setSize(100,20);
            //rdbOTHER.setLocation(200,30);
			//rdbXPS.setSize(100,20);
            //rdbXPS.setLocation(300,30);

			rdbSALOPT.setSize(100,20);
            rdbSALOPT.setLocation(400,30);
			rdbGRDOPT.setSize(100,20);
            rdbGRDOPT.setLocation(505,30);
			rdbCUSOPT.setSize(100,20);
            rdbCUSOPT.setLocation(610,30);
            rdbTRPOPT.setSize(100,20);
            rdbTRPOPT.setLocation(715,30);
			lblMSG.setFont(f);
			lblMSG.setForeground(Color.black);
			lblDSP.setFont(f);
			lblDSP.setForeground(Color.black);
			lblOPT.setFont(f);
			lblOPT.setForeground(Color.black);
			lblMSG.setBounds(0,0,1500,20);
			lblDSP.setBounds(0,50,500,20);
            lblOPT.setBounds(0,30,500,20);
			btnREFSH.setBounds(460,50,80,20);
			btnDTLON.setBounds(545,50,85,20);
			btnDTLOF.setBounds(635,50,90,20);
			btnEXIT.setBounds(730,50,60,20);
			pnlDSPREC.add(lblMSG);
			pnlDSPREC.add(lblDSP);
			pnlDSPREC.add(lblOPT);
			//pnlDSPREC.add(rdbOTHER);
			//pnlDSPREC.add(rdbXPS);

			pnlDSPREC.add(rdbSALOPT);
			pnlDSPREC.add(rdbGRDOPT);
			pnlDSPREC.add(rdbCUSOPT);
            pnlDSPREC.add(rdbTRPOPT);
			pnlDSPREC.add(btnREFSH);
			pnlDSPREC.add(btnEXIT);
			pnlDSPREC.add(btnDTLON);
			pnlDSPREC.add(btnDTLOF);
			btnREFSH.setEnabled(false);
			btnDTLON.setEnabled(false);
			btnDTLOF.setEnabled(false);
			btnEXIT.setEnabled(false);
			/*btnREFSH.setEnabled(true);
			btnDTLON.setEnabled(true);
			btnDTLOF.setEnabled(true);
			btnEXIT.setEnabled(true);*/
			wndMAINWD = new JWindow(this);
			wndMAINDL = new JDialog(this);
			LM_DATFMT = "DMY";
			opnDBCON();

			try{
				String L_strSQLQRY = " SELECT CP_CMPNM FROM CO_CPMST where CP_CMPCD ='"+M_strCMPCD_prst+"'";
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
				if(L_rstRSSET !=null && L_rstRSSET.next())
					M_strCMPNM_prst=L_rstRSSET.getString("CP_CMPNM");
			}catch(Exception E){System.out.println(E+":CO_CPMST");}
			
			wndMAINDL.setTitle("Supreme Petrochem Limited" + "  ("+M_strCMPNM_prst+")                       "+ " Despatch Status as on "+setDATE(new java.util.Date()));
			wndMAINWD = new JWindow(wndMAINDL);
			wndMAINDL.setBounds(0,0,800,120);
			wndMAINDL.getContentPane().add(pnlDSPREC);
			wndMAINDL.toFront();
			//wndMAINDL.show();
			wndMAINDL.setVisible(true);
			btnEXIT.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				//t1.t.stop();
				//t2.t.stop();
				//t1.t = null;
				//t2.t = null;
				//clsDBCON();
				setVisible(false);
				dispose();
				System.exit(0);
			}});
			btnREFSH.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				opnDBCON();
				refrsh();
			}});
			btnDTLON.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				opnDBCON();
				getDSPREC();
				wndMAINDL.setSize(800,500);
				wndMAINDL.validate();
				btnDTLOF.setEnabled(true);
			}});
			btnDTLOF.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent L_AE) {
				pnlDSPREC.remove(tblDSPDTL);
				pnlDSPREC.updateUI();
				wndMAINDL.setSize(800,100);
				btnDTLOF.setEnabled(false);
			}});
			addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//t1.t.stop();
				//t2.t.stop();
				//t1.t = null;
				//t2.t = null;
				
				setVisible(false);
				dispose();
				System.exit(0);
			 }});
	  }catch(Exception L_EX){
		  System.out.println("Refresh: "+L_EX.getMessage());
	  }
  }
  
  public String setDATFMT(String LP_FMTTP,String LP_DATSTR){
		String L_RTNSTR = "";
		try{
			if(LP_FMTTP.equals("MDY")){
				if(LP_DATSTR != null){
					if(LP_DATSTR.trim().length() == 10)
						L_RTNSTR = "'"+LP_DATSTR.substring(3,5)+ "/"+ LP_DATSTR.substring(0,2)+"/"+LP_DATSTR.substring(6,10)+"'";
				}
			}
			else if(LP_FMTTP.equals("DMY")){
				if(LP_DATSTR != null){
					if(LP_DATSTR.trim().length() == 8)
						L_RTNSTR = LP_DATSTR.substring(0,2)+ "/"+LP_DATSTR.substring(3,5)+"/"+"20"+LP_DATSTR.substring(6,8);
				}
			}
		}catch(Exception L_EX){
			System.out.println("setDATFMT: "+L_EX);
		}
		return L_RTNSTR;
	}
	
  /**
   */
  private void getDSPREC(){
		try{
			boolean L_EOF = false;
			boolean L_1stFL = true;
			String L_ADDSTR = "";
			String L_DSPSTR = "";
			String L_SALDS = "";
			String L_SALDS1 = "";
			String L_PRDDS = "";
			String L_PRDDS1 = "";
			String L_LADNO = "";
			String L_LADDT = "";
			String L_BYRCD = "";
			String L_BYRCD1 = "";
			String L_DORNO = "";
			String L_TRPCD = "";
            String L_TRPCD1 = "";
			String L_LRYNO = "";
			String L_CNTDS = "";
			String L_WTPRD = "";
			String L_PKGDS = "";
			String L_STSFL = "";
			double L_REQQT = 0;
			double L_TOTQTY = 0;
			LM_DATFMT = "DMY";
			LM_TODAT = setDATE(new java.util.Date());
			LM_CNT = 0;
			LM_DPVCTR = new Vector<String>();
			if(rdbSALOPT.isSelected()){
				 L_ADDSTR = " order by ivt_SALDS asc,ivt_lryno asc";
			}
			else if(rdbGRDOPT.isSelected()){
				 L_ADDSTR = " order by ivt_prdds asc,ivt_lryno asc";
			}
			else if(rdbCUSOPT.isSelected()){
				 L_ADDSTR = " order by ivt_byrcd asc,ivt_lryno asc";
			}
            else if(rdbTRPOPT.isSelected()){
                 L_ADDSTR = " order by ivt_trpcd asc,ivt_lryno asc";
			}
			//LM_STRSQL = "Select ivt_byrcd,ivt_dorno,ivt_ladno,ivt_trpcd,ivt_lryno,ivt_cntds,ivt_stsfl,ivt_prdds,cmt_codds ivt_salds,ivt_reqqt";
            //LM_STRSQL += " from mr_ivtrn,co_cdtrn where cmt_cgmtp='SYS' and cmt_cgstp='MR00SAL' and cmt_codcd=ivt_saltp and (date(ivt_laddt) between "+setDATFMT("MDY",LM_TODAT)+" and "+setDATFMT("MDY",LM_TODAT)+") or (date(ivt_invdt) between "+setDATFMT("MDY",LM_TODAT)+" and "+setDATFMT("MDY",LM_TODAT)+")";
			//LM_STRSQL += " and ivt_mkttp = '01'" + L_ADDSTR;
			LM_STRSQL = "Select ivt_byrcd,ivt_dorno,ivt_ladno,date(ivt_laddt) ivt_LADDT,ivt_trpcd,ivt_lryno,ivt_cntds,int(round((current_timestamp-ivt_laddt)/100,0)) ivt_wtprd,ivt_stsfl,ivt_prdds,b.cmt_codds ivt_salds,c.cmt_shrds ivt_pkgds,ivt_reqqt ";
            LM_STRSQL += " from mr_ivtrn,co_cdtrn b, co_cdtrn c where ivt_cmpcd = '"+M_strCMPCD_prst+"' and ivt_mkttp in ("+(M_flgXPSFL_prst ? "'07'" : "'01','04','05'")+") and date(isnull(ivt_invdt,isnull(ivt_loddt,ivt_laddt)))  between "+setDATFMT("MDY",LM_TODAT)+" and "+setDATFMT("MDY",LM_TODAT);
			LM_STRSQL += "  and b.cmt_cgmtp='SYS' and b.cmt_cgstp='MR00SAL' and b.cmt_codcd=ivt_saltp ";
			LM_STRSQL += "  and c.cmt_cgmtp='SYS' and c.cmt_cgstp='FGXXPKG' and c.cmt_codcd=ivt_pkgtp "+ L_ADDSTR;
			//System.out.println("LM_STRSQL>>"+LM_STRSQL);
			LM_RSLSET = cl_dat.exeSQLQRY1(LM_STRSQL);
			while(LM_RSLSET.next()){  
			  	L_SALDS = nvlSTRVL(LM_RSLSET.getString("ivt_salds"),"").trim();
			  	L_PRDDS = nvlSTRVL(LM_RSLSET.getString("ivt_prdds"),"").trim();
			  	L_REQQT = LM_RSLSET.getDouble("ivt_reqqt");
			  	L_PKGDS = LM_RSLSET.getString("ivt_pkgds");
			  	L_LADNO = nvlSTRVL(LM_RSLSET.getString("ivt_ladno"),"").trim();
			  	L_LADDT = nvlSTRVL(cc_dattm.occ_dattm.setDATE("DMY",LM_RSLSET.getDate("ivt_laddt")),"").trim();
			  	L_BYRCD = cl_cust.ocl_cust.getPRTNM(nvlSTRVL(LM_RSLSET.getString("ivt_byrcd"),"").trim());
			  	L_DORNO = nvlSTRVL(LM_RSLSET.getString("ivt_dorno"),"").trim();
			  	L_TRPCD = cl_cust.ocl_cust.getTRNNM(nvlSTRVL(LM_RSLSET.getString("ivt_trpcd"),"").trim());
			  	L_LRYNO = nvlSTRVL(LM_RSLSET.getString("ivt_lryno"),"").trim();
			  	L_CNTDS = nvlSTRVL(LM_RSLSET.getString("ivt_cntds"),"").trim();	
			  	L_WTPRD = nvlSTRVL(LM_RSLSET.getString("ivt_wtprd"),"").trim();	
			  	L_STSFL = nvlSTRVL(LM_RSLSET.getString("ivt_stsfl"),"").trim();
			  	if(L_1stFL){
			  		L_SALDS1 = L_SALDS;
			  		L_PRDDS1 = L_PRDDS;
					L_BYRCD1 = L_BYRCD;
                    L_TRPCD1 = L_TRPCD;
			  		L_1stFL = false;
			  	}
				if(rdbSALOPT.isSelected()){
			  		if(!L_SALDS.equals(L_SALDS1)){
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("Total");
			  			LM_DPVCTR.addElement(setDLFMT(String.valueOf(L_TOTQTY),4));
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			L_TOTQTY = 0;
			  			L_SALDS1 = L_SALDS;
			  			LM_CNT++;
			  		}
				}
				else if(rdbGRDOPT.isSelected()){
			  		if(!L_PRDDS.equals(L_PRDDS1)){
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("Total");
			  			LM_DPVCTR.addElement(setDLFMT(String.valueOf(L_TOTQTY),4));
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			L_TOTQTY = 0;
			  			L_PRDDS1 = L_PRDDS;
			  			LM_CNT++;
			  		}
				}
				else if(rdbCUSOPT.isSelected()){
			  		if(!L_BYRCD.equals(L_BYRCD1)){
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("Total");
			  			LM_DPVCTR.addElement(setDLFMT(String.valueOf(L_TOTQTY),4));
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			L_TOTQTY = 0;
			  			L_BYRCD1 = L_BYRCD;
			  			LM_CNT++;
			  		}
				}
                                else if(rdbTRPOPT.isSelected()){
                                        if(!L_TRPCD.equals(L_TRPCD1)){
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("Total");
			  			LM_DPVCTR.addElement(setDLFMT(String.valueOf(L_TOTQTY),4));
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			LM_DPVCTR.addElement("");
			  			L_TOTQTY = 0;
                                                L_TRPCD1 = L_TRPCD;
			  			LM_CNT++;
			  		}
				}
			  	LM_DPVCTR.addElement(L_PRDDS);
			  	LM_DPVCTR.addElement(L_SALDS);
			  	LM_DPVCTR.addElement(L_LADNO);
			  	LM_DPVCTR.addElement(L_LADDT);
			  	LM_DPVCTR.addElement(setDLFMT(String.valueOf(L_REQQT),4));
			  	LM_DPVCTR.addElement(L_PKGDS);
			  	LM_DPVCTR.addElement(L_BYRCD);
			  	LM_DPVCTR.addElement(L_DORNO);
			  	LM_DPVCTR.addElement(L_TRPCD);
			  	LM_DPVCTR.addElement(L_LRYNO);
			  	LM_DPVCTR.addElement(L_CNTDS);
			  	//if(L_CNTDS.length() > 0)
			  	//	LM_DPVCTR.addElement(L_CNTDS);
			  	//else
			  	//	LM_DPVCTR.addElement("("+L_LRYNO+")");
			  	if(L_STSFL.trim().equals("A"))
					L_DSPSTR = "Reported"; 
			  	else if(L_STSFL.trim().equals("L"))
			  		L_DSPSTR = "Loaded";
			  	else if(L_STSFL.trim().equals("D"))
					{L_DSPSTR = "Despatched"; L_WTPRD = "";}
			  	else if(L_STSFL.trim().equals("X"))
					{L_DSPSTR = "Cancelled"; L_WTPRD = "";}
				//String L1_WTPRD = "";
				//String L2_WTPRD = "";
				if(L_WTPRD.length()>0)
					L_WTPRD = ("0000"+L_WTPRD).substring(("0000"+L_WTPRD).length()-4,("0000"+L_WTPRD).length()-2)+":"+("0000"+L_WTPRD).substring(("0000"+L_WTPRD).length()-2,("0000"+L_WTPRD).length())+" Hrs";
			  	LM_DPVCTR.addElement(L_WTPRD);
			  	LM_DPVCTR.addElement(L_DSPSTR);
				
			  	L_TOTQTY += L_REQQT;
			  	LM_CNT++;
					
			}
		if(LM_RSLSET != null)
				LM_RSLSET.close();
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("Total");
			LM_DPVCTR.addElement(setDLFMT(String.valueOf(L_TOTQTY),4));
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_DPVCTR.addElement("");
			LM_CNT++;
			
			if(LM_CNT > 0)
			{
				int j = 0;
				tblDSPDTL = crtTBLPNL1(pnlDSPREC,LM_TBLHD,LM_CNT,10,90,780,400,LM_COLSZ);
				LM_DPVCTR.trimToSize();
				for(int L_ROWCNT = 0;L_ROWCNT < LM_CNT;L_ROWCNT++){
					for(int L_COLCNT = 0;L_COLCNT < LM_TBLHD.length;L_COLCNT++){
						tblDSPDTL.setValueAt(LM_DPVCTR.elementAt(j),L_ROWCNT,L_COLCNT);
						j++;
					}
			}
			}else
				System.out.println("No Record exists....");
		}catch(Exception L_EX){
			System.out.println("getDSPREC "+L_EX);
		}
	}
  
  public String setDLFMT(String L_VALUE,int L_DECML){
		try{
			if(L_VALUE.equals("") || L_VALUE == null)
				L_VALUE = "0.000";
			
			String L_CNT = "1";
			for(int i = 1;i <= L_DECML;i++)
				L_CNT = L_CNT + "0";
			int L_MTINT = Integer.parseInt(L_CNT);
			double L_VALST = Double.parseDouble(L_VALUE) * Double.parseDouble(String.valueOf(L_MTINT));
			double L_RTNST = Math.ceil(L_VALST);
			double L_RTNFL = L_RTNST/Double.parseDouble(String.valueOf(L_MTINT));
			String L_FMTST = setFMT(String.valueOf(L_RTNFL),3);
			return L_FMTST;
			
		}catch(Exception L_EX){
			System.out.println("setDLFMT: "+L_EX);				
		}
		return "";
	}
  
  public String setFMT(String LP_STR,int LP_DEC)
 {
	 int L_STRLEN,L_DOTPOS;
	String L_TMP = "";
    LP_STR = LP_STR.trim();
	L_STRLEN = LP_STR.length();
	L_DOTPOS = LP_STR.indexOf(".");
    if(LP_DEC == 0)
	{
		if(L_DOTPOS != -1) 	// dot found
		{
			LP_STR = LP_STR.substring(0,L_DOTPOS);
			return LP_STR;
		}
	}
	else
	{
		if(L_DOTPOS == -1) 	// dot not found
		{
			LP_STR = LP_STR.trim();
			if(L_TMP.length()< LP_DEC)
			{
				for(int i=0;i<(LP_DEC-L_TMP.length());i++)
				{
					L_TMP = L_TMP + "0";
				}
			}
		}
		else
		{
			L_TMP = LP_STR.substring(L_DOTPOS +1);
			if(L_TMP.length()< LP_DEC)
			{
				for(int i=0;i<=(LP_DEC-L_TMP.length());i++)
				{
					L_TMP = L_TMP + "0";
				}
			}
			else if(L_TMP.length()> LP_DEC)
			{
				L_TMP = L_TMP.substring(0,LP_DEC);
			}

		}
		if(L_DOTPOS >= 0)
		LP_STR = LP_STR.substring(0,L_DOTPOS);
		else
			//LP_STR = "";
			LP_STR = LP_STR.trim();
		LP_STR = LP_STR +"."+L_TMP;
		return LP_STR;
	}
      return(LP_STR);
	}
  
  public String nvlSTRVL(String LP_VARVL, String LP_DEFVL){
	try{
		if (LP_VARVL != null)
			LP_VARVL = LP_VARVL;
		else
			LP_VARVL = LP_DEFVL;
	}catch (Exception L_EX){
		System.out.println("nvl "+L_EX);
	}
	return LP_VARVL;
}
  
  /*
 *Cretaes JTable on the passed Panel
 */
 public JTable crtTBLPNL1(JPanel LP_TBLPNL,String[] LP_COLHD,int LP_ROWCNT,int LP_XPOS,int LP_YPOS,int LP_WID,int LP_HGT,int[] LP_ARRGSZ){ 
	 try{
		 cl_tab2 L_TBLOBJ1; 
		 JPanel pnlTAB1 = new JPanel();
		 Object[][] L_TBLDT1;
		 L_TBLDT1 = crtTBLDAT(LP_ROWCNT,LP_COLHD.length); // Creating the Object Data
		 L_TBLOBJ1 = new cl_tab2(L_TBLDT1,LP_COLHD);
		 JTable L_TBL1 = new JTable(L_TBLOBJ1); 
		 L_TBL1.setBackground(new Color(213,213,255));
		 int h1 = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
		 int v1 = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		 JScrollPane jspTBL1 = new JScrollPane(L_TBL1,v1,h1);
		 jspTBL1.setPreferredSize(new Dimension(LP_WID-25,LP_HGT-25));
		 jspTBL1.setLocation(0,100);
		 pnlTAB1.removeAll();
		 setCOLWDT(L_TBL1,LP_COLHD,LP_ARRGSZ);
		 pnlTAB1.add(jspTBL1);
		 pnlTAB1.setSize(LP_WID,LP_HGT);
		 pnlTAB1.setLocation(LP_XPOS,LP_YPOS);
		 LP_TBLPNL.add(pnlTAB1);
		 LP_TBLPNL.updateUI();
		return L_TBL1;
	 }catch(Exception L_EX){
		 System.out.println("crtTBLPNL1 "+L_EX);
	 }
	return null;
 }
  
  public void setCOLWDT(JTable LP_TBLNM,String[] LP_TBLHDG,int[] LP_WID){
	for(int i=0;i<LP_TBLHDG.length;i++){
		TableColumn L_TBLCOL = LP_TBLNM.getColumn(LP_TBLHDG[i]);
		if(LP_WID[i] !=0)
			L_TBLCOL.setPreferredWidth(LP_WID[i]);
		}
	}
  
  /**
   */
  public Object[][] crtTBLDAT(int LP_ROWCNT,int LP_COLCNT){
		int i = 0;
	    Object[][] L_TBLDT = new Object[LP_ROWCNT][LP_COLCNT];;
		for(int j = 0;j < LP_ROWCNT;j++){
			//if(cl_dat.M_CHKTBL){
			//	i = 1;
			//	L_TBLDT[j][0] = new Boolean(false);
			//}else
				i = 0;
			for( int k = i;k < LP_COLCNT;k++){
				L_TBLDT[j][k] = "";
			}
		}
		//cl_dat.M_CHKTBL = true;
		return L_TBLDT;
  }
  

  /**
   */
  private void BannDisp(){
        char ch;
        String dspMsg;
		//lblMSG.setText(msg1);
		for( ; ; )
		{
          dspMsg = "                     ";
		  dspMsg = dspMsg + msg1;
		  for(int k=0;k <= 1 * dspMsg.length();k++)
		  {
		    try{
                t2.t.sleep(sl_time);
               // ch = dspMsg.charAt(0);
		  	    //dspMsg = dspMsg.substring(1,dspMsg.length());
		  		lblMSG.setText(dspMsg);
                //dspMsg += ch;
		      }catch(InterruptedException e) {}
              catch(IllegalArgumentException ile) {sl_time = 10;}
          }
          for(int i=0;i <= dspMsg.length()/2;i++)
		  {
              try{
                t2.t.sleep(sl_time);
                //ch = ' ';
                //dspMsg = dspMsg.substring(1,dspMsg.length());
                lblMSG.setText(dspMsg);
                //dspMsg += ch;
              }catch(InterruptedException e) {}
              catch(IllegalArgumentException ile) {sl_time = 10;}
		  	}
        }
  }

/**
 */
  private void refrsh(){    
        try{
			LM_DATFMT = "DMY";
			//LM_PRVDT = setDATE(new java.util.Date());	  
			//LM_PRVDT = getABDATE(setDATE(new java.util.Date()),1,"B");	  
			LM_TODAT = getABDATE(LM_PRVDT,1,'A');isnullisnull
			LM_CFWQT_R = getALLQT("ivt_reqqt","date(ivt_laddt) <= "+setDATFMT("MDY",LM_PRVDT)+" and date(ifnull(ivt_invdt,ifnull(ivt_loddt,ivt_laddt)))",LM_TODAT,LM_TODAT);isnull
			LM_CFWQT_L = getALLQT("ivt_ladqt","date(ivt_laddt) <= "+setDATFMT("MDY",LM_PRVDT)+" and ivt_ladno in (select ivt_ladno from mr_ivbak where ivt_mkttp in ("+(M_flgXPSFL_prst ? "'07'" : "'01','04','05'")+")) and  date(ifnull(ivt_invdt,ivt_loddt))",LM_TODAT,LM_TODAT);
			//System.out.println("LM_CFWQT_L : "+LM_CFWQT_L);
			//lblOPT.setText();
			LM_REPQT = getALLQT("ivt_reqqt","date(ivt_laddt)",LM_TODAT,LM_TODAT);
			LM_LDQTY = getALLQT("ivt_ladqt","date(ivt_laddt) = "+setDATFMT("MDY",LM_TODAT)+" and date(ivt_loddt)",LM_TODAT,LM_TODAT);
			LM_DSPQT = getALLQT("ivt_invqt","date(ivt_invdt)",LM_TODAT,LM_TODAT);
			msg1 = "  Reported : "+LM_REPQT+(Double.parseDouble(LM_CFWQT_R)>0? " .. C/F: "+LM_CFWQT_R:"")+"             Loaded : "+LM_LDQTY+(Double.parseDouble(LM_CFWQT_L)>0? " .. C/F: "+LM_CFWQT_L:"")+"               Despatched : "+LM_DSPQT+"  (MT) ";
			clsDBCON();
		}catch (Exception L_EX) {
			System.out.println("refrsh :"+L_EX);
		}
	}

/**
 */
private String getALLQT(String LP_SUMQT,String LP_LADAT,String LP_FRMDT,String LP_TODAT){
	String L_RTNVL = "";
	try{
                LM_STRSQL = "Select sum("+LP_SUMQT+") from mr_ivtrn where ivt_cmpcd = '"+M_strCMPCD_prst+"' and "+LP_LADAT+" between "+setDATFMT("MDY",LP_FRMDT) + " and "+setDATFMT("MDY",LP_TODAT);
                LM_STRSQL += " and (ivt_stsfl is not null and ivt_stsfl not in 'X') and ivt_mkttp in ("+(M_flgXPSFL_prst ? "'07'" : "'01','04','05'")+")";
				System.out.println("LM_STRSQL>>"+LM_STRSQL);
		LM_RSLSET = cl_dat.exeSQLQRY1(LM_STRSQL);
		if(LM_RSLSET.next()){
			L_RTNVL = LM_RSLSET.getString(1);
			}
		if(LM_RSLSET != null)
			LM_RSLSET.close();
		if(L_RTNVL == null)
			L_RTNVL = "0.000";
	}catch(Exception L_EX){
		System.out.println("getALLQT: "+L_EX);
	}
	return L_RTNVL;
}
	public  void setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX)
    {
		try
        {
          cl_dat.M_conSPDBA_pbst = this.setCONDTB(LP_SYSLC,LP_XXLBX, LP_XXUSX, LP_XXPWX);
          if(cl_dat.M_conSPDBA_pbst != null)
          {
              cl_dat.M_conSPDBA_pbst.rollback();
		  	  cl_dat.M_stmSPDBA_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
              cl_dat.M_stmSPDBQ_pbst  = (cl_dat.M_conSPDBA_pbst != null) ? cl_dat.M_conSPDBA_pbst.createStatement() : null;
		  }
	    }
        catch(Exception L_EX)
        {
			System.out.println("Error in setCONTACT"+L_EX);
		}
	}
	/** Establishes connection to DB.
	 * Called internally from
	 * {@link  fr_log#setCONACT(String LP_SYSLC,String LP_XXLBX,String LP_XXUSX,String LP_XXPWX) }
	 */
	private  Connection setCONDTB(String LP_SYSLC,String LP_DTBLB, String LP_DTBUS, String LP_DTBPW)
        {
		Connection LM_CONDTB=null;
		try
                {       String L_STRURL = "", L_STRDRV = "";
						if(LP_SYSLC.equals("01"))
                        {
                                L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                                L_STRURL = "jdbc:as400://SPLWS01/";
                                Class.forName(L_STRDRV);
                        }
                        else if(LP_SYSLC.equals("02"))
                        {
                                int port = 50000;
                                LP_DTBUS = "SPLDATA";
                                LP_DTBPW = "SPLDATA";

                                L_STRURL = "jdbc:db2://" + "splhos01" + ":" + 50000 + "/" ;
                                Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
                        }
						else if(LP_SYSLC.equals("03"))
                        {
                                L_STRDRV = "com.ibm.as400.access.AS400JDBCDriver";
                                L_STRURL = "jdbc:as400://MUMAS2/";
                                Class.forName(L_STRDRV);
                        }
                        L_STRURL = L_STRURL + LP_DTBLB;
                        LM_CONDTB = DriverManager.getConnection(L_STRURL,LP_DTBUS,LP_DTBPW);

                        if(LM_CONDTB == null)
				return null;
                        LM_CONDTB.setAutoCommit(false);

                        SQLWarning L_STRWRN = LM_CONDTB.getWarnings();
			if ( L_STRWRN != null )
			   System.out.println("Warning in setCONDTB : "+L_STRWRN);
                        return LM_CONDTB;
		}
                catch(java.lang.Exception L_EX)
                {
			System.out.println("setCONDTB" + L_EX.toString());
			return null;
		}

    }


	
class NewThread implements Runnable{
		String name;
		Thread t;
		NewThread (String tname){
			name = tname;
			t = new Thread(this, name);
			t.start();
			}
		public void run(){
		if(name.equals("Display")){
          BannDisp();
	}else{
          for ( ; ; ){
			refrsh();
			btnREFSH.setEnabled(true);
			btnDTLON.setEnabled(true);
			btnEXIT.setEnabled(true);
            try {
				Thread.sleep(30000000); 
			}catch (InterruptedException e) {}
          }
         }
   }
}
}
