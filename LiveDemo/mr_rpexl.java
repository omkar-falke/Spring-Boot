/**  Export List Printing for Excise Department
 */
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.lang.*;
import java.util.Properties;
import java.util.Date; 
import java.io.*; 

public class mr_rpexl extends cl_rbase
{
		String strDORNO, strDORDT, strINVNO, strINVDT, strINVQT, strRPTDT1, strRPTDT2, strBYRCD, strBYRNM, strFILNO, strFSPNO;
		String strPINNO,  strTOTQT, strECHRT, strFOBRT, strCIFRT, strRUPRT, strLRYNO;
		String strCNTDS, strTSLNO, strARENO,  strLR_NO, strLADNO, strLADDT, strPRDDS, strREQQT, strLFTMRG;
		JTextField txtDORNO,txtRPTDT1, txtRPTDT2;
		
		private String strRESFIN = "";//cl_dat.M_REPSTR;
		private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_rpexl.doc"; 
	
		private FileOutputStream O_FOUT;
		private DataOutputStream O_DOUT;
		private JRadioButton rdbEXPLST, rdbLADLST, rdbDORLST;
		
		private Process prcREPORT;/**	To write to file */
		//public JRadioButton rdbPREPN,rdbPLNPN;
		//private JCheckBox chbVWAMD;
	
	mr_rpexl()
	{
		super(2);
		strLFTMRG="  ";
		M_txtFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		M_lblFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_txtTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_lblTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblTODAT);
		setMatrix(20,6);
		add(rdbEXPLST = new JRadioButton("Exp.List"),3,3,1,1,this,'L');
		add(rdbDORLST = new JRadioButton("D.O. List"),3,4,1,1,this,'L');
		add(rdbLADLST = new JRadioButton("L.A. List"),3,5,1,1,this,'L');
		add(new JLabel("D.O. Number"),5,3,1,1,this,'L');
		add(txtDORNO = new JTextField(),5,4,1,1,this,'L');
		add(new JLabel("Date From : "),6,3,1,1,this,'L');
		add(txtRPTDT1 = new JTextField(),6,4,1,1,this,'L');
		add(new JLabel("Date To   : "),7,3,1,1,this,'L');
		add(txtRPTDT2 = new JTextField(),7,4,1,1,this,'L');
		ButtonGroup btg=new ButtonGroup();
		btg.add(rdbEXPLST);btg.add(rdbDORLST);btg.add(rdbLADLST);
		rdbEXPLST.setSelected(true);
		//txtDORNO.setVisible(false);
		inlDOHDR();
	}
	

	
	/**
	 */
	void exePRINT()
	{
		try
		{
			setMSG("Fetching of data in progress... ",'N');
			strDORNO = txtDORNO.getText().toString().trim();
			strRPTDT1 = txtRPTDT1.getText().toString().trim();
			strRPTDT2 = txtRPTDT2.getText().toString().trim();
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);	
			//	prnFMTCHR(O_DOUT,M_strNOCPI17);
			//	prnFMTCHR(O_DOUT,M_strCPI10);
			if(rdbEXPLST.isSelected())
					prnEXPLST();
			if(rdbDORLST.isSelected())
					prnDORLST();
			if(rdbLADLST.isSelected())
					prnLADLST();
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst))
				doPRINT(strRESSTR);
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst))
				prcREPORT  = Runtime.getRuntime().exec("c:\\windows\\wordpad.exe "+strRESSTR);
			//else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPEML_pbst))
			//	for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			//		((cl_eml)Class.forName("cl_eml").newInstance()).sendfile(M_cmbDESTN.getItemAt(i).toString(),cl_dat.M_strREPSTR_pbst+"mm_rpabc.doc",(rdbABC.isSelected() ? "ABC" : "XYZ")+" Analysis Report on "+cl_dat.M_txtCLKDT_pbst.getText(),"");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception e)
			{setMSG(e,"exePRINT");}
	}
	

	
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
        super.actionPerformed(L_AE);
		if(L_AE.getSource().equals(rdbEXPLST))
			if(rdbEXPLST.isSelected())
				{txtDORNO.setVisible(true); inlDOHDR();}
		if(L_AE.getSource().equals(rdbDORLST))
			if(!rdbEXPLST.isSelected())
				{txtDORNO.setVisible(false); inlDOHDR();}
		if(L_AE.getSource().equals(rdbLADLST))
			if(!rdbEXPLST.isSelected())
				{txtDORNO.setVisible(false); inlDOHDR();}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	
	
	/**
	 */
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseClicked");
		}
	}
	

	/**
	 */
	public void keyPressed(KeyEvent L_KE){
		try{
			super.keyPressed(L_KE);
			strDORNO = txtDORNO.getText().toString().trim();
			strRPTDT1 = txtRPTDT1.getText().toString().trim();
			strRPTDT2 = txtRPTDT2.getText().toString().trim();
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(L_KE.getSource().equals(txtDORNO))
				{
					String L_ARRHDR[] = {"DO No.","Date"};
					M_strSQLQRY = "Select distinct DOT_DORNO, DOT_DORDT from MR_DOTRN where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_STSFL <> 'X' and DOT_INDNO in (select IN_INDNO from mr_inmst where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_saltp='12')  and dot_dorno in (select ivt_dorno from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_epifl='1' and ivt_stsfl<>'X' and isnull(ivt_reqqt,0)>0) order by DOT_DORDT DESC,DOT_DORNO DESC";
					M_strHLPFLD = "txtDORNO";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY, 1,1,L_ARRHDR,2,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER || L_KE.getKeyCode() == 9)
			{
				if(L_KE.getSource().equals(txtDORNO))
				{
					vldDORNO();
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	

	private void vldDORNO()
	{
		try
		{
			M_strSQLQRY = "Select * from MR_DOTRN  where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_DORNO = '"+strDORNO+"' and DOT_STSFL <> 'X' and DOT_INDNO in (select IN_INDNO from mr_inmst where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND in_saltp='12' and in_stsfl<>'X')";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			if(!M_rstRSSET.next())
			{
				setMSG("InValid DO Number",'E');
				txtDORNO.requestFocus();
			}
			M_rstRSSET.close();
			M_strSQLQRY = "select ivt_dorno,min(CONVERT(varchar,ivt_laddt,101)) ivt_strdt, max(CONVERT(varchar,ivt_laddt,101)) ivt_enddt from mr_ivtrn  where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_dorno = '"+strDORNO+"'  and ivt_stsfl<>'X' and isnull(ivt_reqqt,0)>0  group by IVT_DORNO";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				txtRPTDT1.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("ivt_strdt")));
				txtRPTDT2.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("ivt_enddt")));
			}
			txtRPTDT1.requestFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDORNO");
		}
	}
	
	
	/**
	 */
	void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtDORNO"))
				txtDORNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	

	/**
	 */
	private void inlDOHDR()
	{
		strDORNO = "-";
		strDORDT = "-";
		strINVNO = "-";
		strINVDT = "-";
		strINVQT = "-";
		strRPTDT1 = cl_dat.M_strLOGDT_pbst;
		strRPTDT2 = cl_dat.M_strLOGDT_pbst;
		strBYRCD = "-";
		strBYRNM = "-";
		strFILNO = "-";
		strFSPNO = "-";
		strPINNO = "-";
		strTOTQT = "-";
		strECHRT = "-";
		strFOBRT = "-";
		strCIFRT = "-";
		strRUPRT = "-";
		strLRYNO = "-";
		strCNTDS = "-";
		strTSLNO = "-";
		strARENO = "-";
		strLR_NO = "-";
		strLADNO = "-";
		strLADDT = "-";
		strPRDDS = "-";
		txtRPTDT1.setText(strRPTDT1);
		txtRPTDT2.setText(strRPTDT2);
	}

	
	

	/**
	 */
    private void prnEXPLST(){
	try
	{
		
		prnFMTCHR(O_DOUT,M_strCPI12);
                        
		M_strSQLQRY  = "Select IVT_DORNO,IVT_BYRCD,IVT_ECHRT,IVT_LRYNO,IVT_CNTDS,IVT_TSLNO,IVT_LR_NO,IVT_LADNO,IVT_REQQT,IVT_PRDDS,IVT_INVRT from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_DORNO = '"+strDORNO+"' and CONVERT(varchar,IVT_LADDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT1))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT2))+"'  and ivt_stsfl<>'X' and isnull(ivt_reqqt,0)>0 ORDER BY IVT_LADNO, IVT_PRDDS";
		System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			
		if(!M_rstRSSET.next() || M_rstRSSET == null)
		{
			return;
		}
		System.out.println("001");
		prnEXPHDR();
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"------",15,"12",'A')    +padSTR1('R',"--------",15,"12",'A')     +padSTR1('R',"--------",10,"12",'A')   +padSTR1('R',"--------",10,"12",'A')  +padSTR1('R',"--------",10,"12",'A')  +padSTR1('R',"-------",10,"12",'A')  +padSTR1('R',"------------",15,"12",'A')  +padSTR1('R',"-----",10,"12",'A')  +"\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Veh.No.",15,"12",'A')   +padSTR1('R',"Contr.No.",15,"12",'A')    +padSTR1('R',"Seal No.",10,"12",'A')   +padSTR1('R',"ARE1 No.",10,"12",'A')  +padSTR1('R',"L.R. No.",10,"12",'A')  +padSTR1('R',"L.A. No.",10,"12",'A')  +padSTR1('R',"Grade",15,"12",'A') +padSTR1('R',"Qty",10,"12",'A')  +"\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"------",15,"12",'A')    +padSTR1('R',"--------",15,"12",'A')     +padSTR1('R',"--------",10,"12",'A')   +padSTR1('R',"--------",10,"12",'A')  +padSTR1('R',"--------",10,"12",'A')  +padSTR1('R',"-------",10,"12",'A')  +padSTR1('R',"------------",15,"12",'A')  +padSTR1('R',"-----",10,"12",'A')  +"\n\n");
			while(true)
			{
                strLRYNO = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"").trim(); 
                strCNTDS = nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),"").trim(); 
                strTSLNO = nvlSTRVL(M_rstRSSET.getString("IVT_TSLNO"),"").trim(); 
                strLR_NO = nvlSTRVL(M_rstRSSET.getString("IVT_LR_NO"),"").trim(); 
                strLADNO = nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),"").trim(); 
                strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"").trim(); 
                strREQQT = nvlSTRVL(M_rstRSSET.getString("IVT_REQQT"),"").trim(); 
 			    O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strLRYNO,15,"12",'A')   +padSTR1('R',strCNTDS,15,"12",'A')    +padSTR1('R',strTSLNO,10,"12",'A')   +padSTR1('R',strARENO,10,"12",'A')  +padSTR1('R',strLR_NO,10,"12",'A')  +padSTR1('R',strLADNO,10,"12",'A')  +padSTR1('R',strPRDDS,15,"12",'A')  +padSTR1('R',strREQQT,10,"12",'A')  +"\n\n");
				if(!M_rstRSSET.next())
					break;
			}
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"------",15,"12",'A')    +padSTR1('R',"--------",15,"12",'A')     +padSTR1('R',"--------",10,"12",'A')   +padSTR1('R',"--------",10,"12",'A')  +padSTR1('R',"--------",10,"12",'A')  +padSTR1('R',"-------",10,"12",'A')  +padSTR1('R',"------------",15,"12",'A')  +padSTR1('R',"-----",10,"12",'A') +"\n\n");
			prnFMTCHR(O_DOUT,M_strEJT);
			M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"prnEXPLST");
	  }
	}
	

	
	/**
	 */
    private void prnLADLST(){
	try
	{
		
		prnFMTCHR(O_DOUT,M_strCPI12);
                        
		M_strSQLQRY  = "Select IVT_LADNO, CONVERT(varchar,IVT_LADDT,101) IVT_LADDT, IVT_PRDDS, IVT_INVQT , IVT_DORNO,IVT_INVNO, IVT_BYRCD from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL <> 'X' and  CONVERT(varchar,IVT_LADDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT1))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT2))+"'  and ivt_stsfl<>'X' and isnull(ivt_reqqt,0)>0 ORDER BY IVT_LADDT DESC, IVT_LADNO DESC";
		System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			
		if(!M_rstRSSET.next() || M_rstRSSET == null)
		{
			return;
		}
			O_DOUT.writeBytes(strLFTMRG+padSTR1('C',"Loading Adv. List for Despatches Between "+strRPTDT1+" to "+strRPTDT2,110,"12",'A')  +"\n\n\n");
			
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"-------",12,"12",'A')    +padSTR1('R',"--------",12,"12",'A')     +padSTR1('R',"-----",18,"12",'A')   +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"--------",12,"12",'A')  +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"-----",20,"12",'A')  +"\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"L.A.No.",12,"12",'A')    +padSTR1('R',"L.A.Date",12,"12",'A')     +padSTR1('R',"Grade",18,"12",'A')   +padSTR1('R',"Inv.Qty.",12,"12",'A')  +padSTR1('R',"D.O.No.",12,"12",'A')  +padSTR1('R',"Inv.No.",12,"12",'A')  +padSTR1('R',"Buyer",20,"12",'A')  +"\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"-------",12,"12",'A')    +padSTR1('R',"--------",12,"12",'A')     +padSTR1('R',"-----",18,"12",'A')   +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"--------",12,"12",'A')  +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"-----",20,"12",'A')  +"\n\n");
			while(true)
			{
                strLADNO = nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),"").trim(); 
                strLADDT = nvlSTRVL(M_rstRSSET.getString("IVT_LADDT"),"").trim(); 
                strPRDDS = nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),"").trim(); 
                strINVQT = nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"").trim(); 
                strDORNO = nvlSTRVL(M_rstRSSET.getString("IVT_DORNO"),"").trim(); 
                strINVNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"").trim(); 
                strBYRCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"").trim(); 
				getBYRDTL();
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strLADNO,12,"12",'A')    +padSTR1('R',strLADDT,12,"12",'A')     +padSTR1('R',strPRDDS,18,"12",'A')   +padSTR1('R',strINVQT,12,"12",'A')  +padSTR1('R',strDORNO,12,"12",'A')  +padSTR1('R',strINVNO,12,"12",'A')  +padSTR1('R',strBYRNM,20,"12",'A')  +"\n");
				if(!M_rstRSSET.next())
					break;
			}
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"-------",12,"12",'A')    +padSTR1('R',"--------",12,"12",'A')     +padSTR1('R',"-----",18,"12",'A')   +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"--------",12,"12",'A')  +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"-----",20,"12",'A')  +"\n\n");
			prnFMTCHR(O_DOUT,M_strEJT);
			M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"prnEXPLST");
	  }
	}
	

	
	/**
	 */
    private void prnDORLST(){
	try
	{
		
		prnFMTCHR(O_DOUT,M_strCPI12);
                        
		M_strSQLQRY  = "Select DOT_DORNO, DOT_DORDT, DOT_PRDDS,  IVT_LADNO, IVT_INVNO, IVT_BYRCD, sum(IVT_INVQT) IVT_INVQT  from MR_DOTRN left outer join MR_IVTRN on DOT_CMPCD=IVT_CMPCD and DOT_MKTTP = IVT_MKTTP and DOT_DORNO = IVT_DORNO and DOT_PRDCD = IVT_PRDCD and DOT_PKGTP = IVT_PKGTP and IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_STSFL<>'X' where DOT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DOT_STSFL <> 'X' and  DOT_DORNO in (select IVT_DORNO from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_stsfl<>'X' and isnull(ivt_reqqt,0)>0 and CONVERT(varchar,IVT_INVDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT1))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT2))+"') group by DOT_DORNO, DOT_DORDT, DOT_PRDDS,  IVT_LADNO, IVT_INVNO, IVT_BYRCD ORDER BY DOT_DORDT DESC, DOT_DORNO DESC";
		System.out.println(M_strSQLQRY);
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
			
		if(!M_rstRSSET.next() || M_rstRSSET == null)
		{
			return;
		}
			O_DOUT.writeBytes(strLFTMRG+padSTR1('C',"Del.Order List for Despatches Between "+strRPTDT1+" to "+strRPTDT2,110,"12",'A')  +"\n\n\n");
			
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"-------",12,"12",'A')    +padSTR1('R',"--------",12,"12",'A')     +padSTR1('R',"-----",18,"12",'A')   +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"--------",12,"12",'A')  +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"-----",20,"12",'A')  +"\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"D.O.No.",12,"12",'A')    +padSTR1('R',"D.O.Date",12,"12",'A')     +padSTR1('R',"Grade",18,"12",'A')   +padSTR1('R',"Inv.Qty.",12,"12",'A') +padSTR1('R',"L.A.No.",12,"12",'A')   +padSTR1('R',"Inv.No.",12,"12",'A')  +padSTR1('R',"Buyer",20,"12",'A')  +"\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"-------",12,"12",'A')    +padSTR1('R',"--------",12,"12",'A')     +padSTR1('R',"-----",18,"12",'A')   +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"--------",12,"12",'A')  +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"-----",20,"12",'A')  +"\n\n");
			while(true)
			{
                strDORNO = nvlSTRVL(M_rstRSSET.getString("DOT_DORNO"),"").trim(); 
                strDORDT = nvlSTRVL(M_rstRSSET.getString("DOT_DORDT"),"").trim(); 
                strPRDDS = nvlSTRVL(M_rstRSSET.getString("DOT_PRDDS"),"").trim(); 
                strINVQT = nvlSTRVL(M_rstRSSET.getString("IVT_INVQT"),"").trim(); 
                strLADNO = nvlSTRVL(M_rstRSSET.getString("IVT_LADNO"),"").trim(); 
                strINVNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),"").trim(); 
                strBYRCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"").trim();
				getBYRDTL();
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strDORNO,12,"12",'A')    +padSTR1('R',strDORDT,12,"12",'A')     +padSTR1('R',strPRDDS,18,"12",'A')   +padSTR1('R',strINVQT,12,"12",'A')  +padSTR1('R',strLADNO,12,"12",'A')  +padSTR1('R',strINVNO,12,"12",'A')  +padSTR1('R',strBYRNM,20,"12",'A')  +"\n");
				if(!M_rstRSSET.next())
					break;
			}
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"-------",12,"12",'A')    +padSTR1('R',"--------",12,"12",'A')     +padSTR1('R',"-----",18,"12",'A')   +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"--------",12,"12",'A')  +padSTR1('R',"-------",12,"12",'A')  +padSTR1('R',"-----",20,"12",'A')  +"\n\n");
			prnFMTCHR(O_DOUT,M_strEJT);
			M_rstRSSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"prnEXPLST");
	  }
	}
	
	
	/**
	 */
	public static DataOutputStream crtDTOUTSTR(FileOutputStream outfile){
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}


	/**
	 */
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
	
	
	/**
	 */
	private void prnEXPHDR()
	{
		try{
			System.out.println("002");
            strBYRCD = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"").trim(); 
			getBYRDTL();
			getPINDTL();
            //strCNTDS = nvlSTRVL(M_rstRSSET.getString("IVT_ECHRT"),"").trim(); 
            //strTSLNO = nvlSTRVL(M_rstRSSET.getString("IVT_INVRT"),"").trim(); 
			O_DOUT.writeBytes("\n");
                        //prnFMTCHR(O_DOUT,M_BOLD);
            //prnFMTCHR(O_DOUT,M_strNOCPI17);
			String strRPTDTX = strRPTDT1;
			if(!strRPTDT1.equals(strRPTDT2))
				strRPTDTX = strRPTDTX + " to "+ strRPTDT2;
            prnFMTCHR(O_DOUT,M_strCPI12);
            O_DOUT.writeBytes("\n\n");
            O_DOUT.writeBytes(strLFTMRG+padSTR1('C',"EXPORT LIST",110,"12",'A')+"\n");
            O_DOUT.writeBytes(strLFTMRG+padSTR1('C',"===========",110,"12",'A')+"\n\n\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"DATE       : "+strRPTDTX,55,"12",'A')+padSTR1('R',"QTY (MT)    : "+strTOTQT,55,"12",'A')+"\n\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"D.O.No.    : "+strDORNO,55,"12",'A')+padSTR1('R',"Exch. Rate  : "+strECHRT,55,"12",'A')+"\n\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Customer   : "+strBYRNM,55,"12",'A')+padSTR1('R',"FOB Rate    : ",55,"12",'A')+"\n\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"File No.   : ",55,"12",'A')         +padSTR1('R',"CIF/CFR Rate: ",55,"12",'A')+"\n\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"FSP No.    : ",55,"12",'A')         +padSTR1('R',"Rate in Rs. : ",55,"12",'A')+"\n\n");
			O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Exp.Inv.No : "+strPINNO,110,"12",'A')+"\n\n\n");
		}catch(Exception L_EX){
			setMSG(L_EX,"prnEXPHDR");
		}
	}	
	
	/**
	 */
	private void getBYRDTL(){
		try{
			ResultSet L_RSLSET;
			M_strSQLQRY = "Select pt_prtnm,pt_prtcd,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd,pt_cstno,pt_stxno,pt_eccno";
			M_strSQLQRY += " from co_ptmst where pt_prtcd='"+strBYRCD+"' and pt_prttp='C'";
			L_RSLSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
			if(L_RSLSET.next()){
				strBYRNM = nvlSTRVL(L_RSLSET.getString("pt_prtnm"),"").trim();
			}
			L_RSLSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"getBYRDTL");
		}
	}
	

	/**
	 */
	private void getPINDTL(){
		try{
			ResultSet L_RSLSET;
			strPINNO = "";
			double dblPRDQT = 0.000;
			M_strSQLQRY = "Select pit_pinno, sum(pit_prdqt) pit_prdqt from mr_pitrn where PIT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND pit_ladno in (select ivt_ladno from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_DORNO = '"+strDORNO+"' and CONVERT(varchar,IVT_LADDT,101) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT1))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strRPTDT2))+"') group by pit_pinno";
			L_RSLSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
			if(L_RSLSET!=null)
			{
				while (L_RSLSET.next())
				{
					strPINNO = strPINNO+ (strPINNO.length()>5 ? " / " : "") + nvlSTRVL(L_RSLSET.getString("pit_pinno"),"").trim();
					dblPRDQT += Double.parseDouble(nvlSTRVL(L_RSLSET.getString("pit_prdqt"),"0.000"));
				}
			}
			strTOTQT = String.valueOf(dblPRDQT);
			L_RSLSET.close();
		}catch(Exception L_EX){
			setMSG(L_EX,"getPINDTL");
		}
	}
	
	
	
	/**
	 *
	 *Method to create lines that are used in the Reports
	 */
    private String crtLINE(int P_strCNT,String P_strLINCHR)
    {
	String strln = "";
	try{
                    for(int i=1;i<=P_strCNT;i++){
                     strln += P_strLINCHR;
		}
	}catch(Exception L_EX){
		System.out.println("L_EX Error in Line:"+L_EX);
	}
    return strln;
}
	

	/**
	*/
	private  String padSTR1(char P_chrPADTP,String P_strSTRVL,int P_intPADLN, String P_strCHRSZ, char P_chrPRPRT)
	{
		String P_strTRNVL = "";
                int L_intPADLN = new Double((Double.parseDouble(P_strCHRSZ)/12)*P_intPADLN).intValue();
                //int L_intPADLN = P_intPADLN;
		try
		{
			String L_STRSP = " ";
			P_strSTRVL = P_strSTRVL.trim();
			int L_STRLN = P_strSTRVL.length();
			if(L_intPADLN <= L_STRLN)
			{
				P_strSTRVL = P_strSTRVL.substring(0,L_intPADLN).trim();
				L_STRLN = P_strSTRVL.length();
				P_strTRNVL = P_strSTRVL;
			}
			int L_STRDF = L_intPADLN - L_STRLN;
			StringBuffer L_STRBUF;
			switch(P_chrPADTP)
			{
				case 'C':
					L_STRDF = L_STRDF / 2;
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL+L_STRBUF ;
					break;
				case 'R':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  P_strSTRVL+L_STRBUF ;
					break;
				case 'L':
					L_STRBUF = new StringBuffer(L_STRDF);
					for(int j = 0;j < L_STRBUF.capacity();j++)
						L_STRBUF.insert(j,' ');
					P_strTRNVL =  L_STRBUF+P_strSTRVL ;
					break;
			}
		}catch(Exception L_EX){
			setMSG(L_EX,"padSTR1");
		}
		return P_strTRNVL;
	}


	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	 */
	private String getRSTVAL(ResultSet LP_RSLSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
		if (LP_FLDTP.equals("C"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? LP_RSLSET.getString(LP_FLDNM).toString() : "";
			//return LP_RSLSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString()," ")) : "";
		else if (LP_FLDTP.equals("N"))
			return LP_RSLSET.getString(LP_FLDNM) != null ? nvlSTRVL(LP_RSLSET.getString(LP_FLDNM).toString(),"0") : "0";
		else if (LP_FLDTP.equals("D"))
			return LP_RSLSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(LP_RSLSET.getDate(LP_FLDNM)) : "";
		else if (LP_FLDTP.equals("T"))
		    return M_fmtDBDTM.format(M_fmtLCDTM.parse(LP_RSLSET.getString(LP_FLDNM)));
		else 
			return " ";
		}
		catch (Exception L_EX)
		{setMSG(L_EX,"getRSTVAL");}
	return " ";
	} 

		
		
	void exeSAVE(){}
}
