import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.lang.*;
import java.util.Properties;
import java.util.Date; 
import java.util.Hashtable;
import java.io.*; 

class mr_rpdo1 extends cl_rbase
{
	JTextField txtFRDNO,txtTODNO,txtPRDTP;
	public JRadioButton rdbPREPN,rdbPLNPN;
	private JCheckBox chbVWAMD;
	private String strFRDNO,strTODNO,strPRDTP,strISODCA,strISODCB,strISODCC,strTSTFL,strADDSTR;
	private String strTRNSP,strDISTR,strFAXNO,strSLTYP,strDLTYP,strTRMOD,strEXCNO,strRNGDS,strDIVDS,strCLLDS,strITPNO;
	private String strBYRCD1,strBYRCD,strBYRCD2;
	private String strCNSCD1,strCNSCD,strCNSCD2;
	private String strINDNO1,strINDNO,strINDNO2;
	private String strINDDT1,strINDDT,strINDDT2;
	private String strAMIDT1,strAMIDT,strAMIDT2;
	private String strAMINO1,strAMINO,strAMINO2;
	private String strPORNO1,strPORNO,strPORNO2;	
	private String strPORDT1,strPORDT,strPORDT2;	
	private String strSALTP1,strSALTP,strSALTP2;	
	private String strDTPCD1,strDTPCD,strDTPCD2;	
	private String strDSRCD1,strDSRCD,strDSRCD2;	
	private String strCPTVL1,strCPTVL,strCPTVL2;	
	private String strDORNO1,strDORNO,strDORNO2;	
    private String strSTSFL1,strSTSFL,strSTSFL2; 
	private String strDORDT1,strDORDT,strDORDT2;	
	private String strAMDDT1,strAMDDT,strAMDDT2;	
	private String strAMDNO1,strAMDNO,strAMDNO2;	
	private String strTMOCD1,strTMOCD,strTMOCD2;	
	private String strLRYNO1,strLRYNO,strLRYNO2;	
	private String strMKTTP1,strMKTTP,strMKTTP2;	
    private String strTRPCD1,strTRPCD,strTRPCD2; 
	private String strPRDDS1,strPRDDS,strPRDDS2;	
	private String strPKGDS1,strPKGDS,strPKGDS2;	
	private String strDORPK1,strDORPK,strDORPK2;	
	private String strDORQT1,strDORQT,strDORQT2;	
	private String strDELTP1,strDELTP,strDELTP2;	
    private String strAUTBY1,strAUTBY,strAUTBY2; 
	private String strAUTDT1,strAUTDT,strAUTDT2;	
	private String strBASRT1,strBASRT,strBASRT2;	
	private String strEXCRT1,strEXCRT,strEXCRT2;	
	private String strDSPDT1,strDSPDT,strDSPDT2;	
	private String strDODQT1,strDODQT,strDODQT2;	
	private String strREMDS;	
	private String strCNCOD;
	private String strCNSNM,strBYRNM;
	private String strCNAD1,strBYAD1;
	private String strCNAD2,strBYAD2;
	private String strCNAD3,strBYAD3;
	private String strCNAD4,strBYAD4;
	private String strCNPIN,strBYPIN;
	private String strCNCST,strBYCST;
	private String strCNSTN,strBYSTN;
	private String strCNECC,strBYECC;
    private String L_TMPSTR, L_TMPSTR1;
    private String strLFTMRG;
	
    private JOptionPane oppOPTNPN;

	private String strRESFIN = "";//cl_dat.M_REPSTR;
        private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_rpdo1.doc"; 
	
	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	private Process prcREPORT;
	
	private boolean flgPRNHDR = false;
	private boolean flgCANCDO = false;
	private boolean L_flgCANCPRT = false;
	public boolean flgOUTPRT = false;
	
	private int strLINENO = 0;
	private double dblEXCRT;
	
	private Hashtable hstPKGTP;
        mr_rpdo1()
	{
		super(2);
		M_txtFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		M_lblFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_txtTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_lblTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblTODAT);
		setMatrix(20,6);
		add(new JLabel("Product Catagory"),3,3,1,1,this,'L');
		add(txtPRDTP=new JTextField(),3,4,1,1,this,'L');
		add(chbVWAMD=new JCheckBox("View Ammendment"),3,5,1,2,this,'L');
		add(new JLabel("D.O. No. From"),4,3,1,1,this,'L');
		add(txtFRDNO=new JTextField(),4,4,1,1,this,'L');
		add(new JLabel("D.O. No. To"),5,3,1,1,this,'L');
		add(txtTODNO=new JTextField(),6,4,1,1,this,'L');
		add(new JLabel("Printing On"),7,3,1,1,this,'L');
		add(rdbPREPN=new JRadioButton("Pre-printed"),7,4,1,1,this,'L');
		add(rdbPLNPN=new JRadioButton("Plain"),7,5,1,1,this,'L');
		ButtonGroup btg=new ButtonGroup();
		btg.add(rdbPREPN);btg.add(rdbPLNPN);rdbPLNPN.setSelected(true);
		try
		{
			hstPKGTP = new Hashtable(4,0.5f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_SHRDS,CMT_NCSVL FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'FGXXPKG' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),"0"))*1000,0));
					//hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Pkgtp In Constructor");
		}
	}
        mr_rpdo1(int P_intTEMP)
	{
		super();
		M_txtFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtFMDAT);
		M_lblFMDAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblFMDAT);
		M_txtTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_txtTODAT);
		M_lblTODAT.setVisible(false);
		M_vtrSCCOMP.remove(M_lblTODAT);
		setMatrix(20,6);
		add(new JLabel("Product Catagory"),3,3,1,1,this,'L');
		add(txtPRDTP=new JTextField(),3,4,1,1,this,'L');
		add(chbVWAMD=new JCheckBox("View Ammendment"),3,5,1,2,this,'L');
		add(new JLabel("D.O. No. From"),4,3,1,1,this,'L');
		add(txtFRDNO=new JTextField(),4,4,1,1,this,'L');
		add(new JLabel("D.O. No. To"),5,3,1,1,this,'L');
		add(txtTODNO=new JTextField(),6,4,1,1,this,'L');
		add(new JLabel("Printing On"),7,3,1,1,this,'L');
		add(rdbPREPN=new JRadioButton("Pre-printed"),7,4,1,1,this,'L');
		add(rdbPLNPN=new JRadioButton("Plain"),7,5,1,1,this,'L');
		ButtonGroup btg=new ButtonGroup();
		btg.add(rdbPREPN);btg.add(rdbPLNPN);rdbPLNPN.setSelected(true);
		
		try
		{
			hstPKGTP = new Hashtable(4,0.5f);
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_SHRDS,CMT_NCSVL FROM CO_CDTRN WHERE CMT_CGMTP = 'SYS' AND "
				+"CMT_CGSTP = 'FGXXPKG' ORDER BY CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
					//hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_SHRDS"),""));
					hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),setNumberFormat(Double.parseDouble(nvlSTRVL(M_rstRSSET.getString("CMT_NCSVL"),"0"))*1000,0));
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Pkgtp In Constructor");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				txtPRDTP.requestFocus();				
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
				txtPRDTP.requestFocus();				
		}
		if(M_objSOURC == rdbPREPN || M_objSOURC == rdbPLNPN)
			cl_dat.M_btnSAVE_pbst.requestFocus();
	}
	void exePRINT()
	{
		try
		{
			setMSG("Fetching of data in progress... ",'N');
			strPRDTP = txtPRDTP.getText().toString().trim();
            strFRDNO = txtFRDNO.getText().toString().trim();
            strTODNO = txtTODNO.getText().toString().trim();
            strLFTMRG = "     ";
            prnALLREC(strPRDTP, strFRDNO, strTODNO);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPPRN_pbst) || flgOUTPRT)
				doPRINT(strRESSTR);
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPSCN_pbst))
				prcREPORT  = Runtime.getRuntime().exec("c:\\windows\\wordpad.exe "+strRESSTR);
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}
	}
	
	public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			if(L_ME.getSource().equals(chbVWAMD))
				txtFRDNO.requestFocus();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseClicked");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			strPRDTP = txtPRDTP.getText().toString().trim();
			strFRDNO = txtFRDNO.getText().toString().trim();
			strTODNO = txtTODNO.getText().toString().trim();
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(L_KE.getSource().equals(txtPRDTP))
				{
					String L_ARRHDR[] = {"Type.","DESC"};
					M_strSQLQRY = "Select CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN where "
						+"(CMT_CGMTP='MST' and CMT_CGSTP = 'COXXMKT' ) order by CMT_CGSTP,CMT_CODCD";
					M_strHLPFLD = "txtPRDTP";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY, 1,2,L_ARRHDR,3,"CT");
				}
				else if(L_KE.getSource().equals(txtFRDNO))
				{
					if(chbVWAMD.isSelected())
					{
						strADDSTR = " and dot_amdno > '00' ";
						strADDSTR += " order by dot_amddt desc";
					}
					else
					{
						strADDSTR = "  order by dot_dordt desc";
					}
					String L_ARRHDR[] = {"D.O. No.","D.O. Date","Buyer","Amd. No.","Amd. Date","Status","Last Updated By"};
					M_strSQLQRY = "Select distinct dot_dorno,dot_dordt,pt_prtnm,dot_amdno,dot_amddt,dot_stsfl,dot_lupdt from mr_dotrn,mr_inmst,";
					M_strSQLQRY += "co_ptmst where dot_mkttp=in_mkttp and dot_indno=in_indno and";
                    M_strSQLQRY += " in_byrcd=pt_prtcd and pt_prttp='C' and in_stsfl in ('U','A','2','3','4','1','X') and dot_stsfl in ('1','X')" + strADDSTR;
					M_strHLPFLD = "txtFRDNO";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY, 1,1,L_ARRHDR,7,"CT");
				}
				else if(L_KE.getSource().equals(txtTODNO))
				{
					if(chbVWAMD.isSelected())
					{
						strADDSTR = " and dot_amdno > '00' and dot_amddt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
						strADDSTR += " order by dot_amddt desc";
					}
					else
					{
						strADDSTR = "  order by dot_dordt desc";
					}
					String L_ARRHDR[] = {"D.O. No.","D.O. Date","Buyer","Amd. No.","Amd. Date","Status","Last Updated By"};
					M_strSQLQRY = "Select distinct dot_dorno,dot_dordt,pt_prtnm,dot_amdno,dot_amddt,dot_stsfl,dot_lupdt from mr_dotrn,mr_inmst,";
					M_strSQLQRY += "co_ptmst where dot_mkttp=in_mkttp and dot_indno=in_indno and";
					M_strSQLQRY += " in_byrcd=pt_prtcd and pt_prttp='C' and in_stsfl in ('U','A','1','2','3','X') and dot_stsfl in ('1','2','X')";
					M_strSQLQRY += " and dot_dorno >= '"+strFRDNO+"'"+ strADDSTR;
					M_strHLPFLD = "txtTODNO";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY, 1,1,L_ARRHDR,7,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER || L_KE.getKeyCode() == 9)
			{
				if(L_KE.getSource().equals(txtPRDTP))
				{
					M_strSQLQRY = "Select * from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += "  cmt_codcd='"+strPRDTP+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET.next())
					{
						setMSG("Enter D.O. No. or Press F1 for help on D.O. No.",'N');
						txtFRDNO.requestFocus();
					}
					else
					{
						setMSG("InValid Product Category.",'E');
						txtPRDTP.requestFocus();
					}
					M_rstRSSET.close();
				}
				else if(L_KE.getSource().equals(txtFRDNO))
				{
                    M_strSQLQRY = "Select dot_dorno from mr_dotrn where dot_dorno='"+strFRDNO+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET.next())
					{
						setMSG("Enter D.O. No. or Press F1 for help on D.O. No.",'N');
						txtTODNO.setText(strFRDNO);
						txtTODNO.requestFocus();
					}
					else
					{
						setMSG("InValid D.O. No.",'E');
						txtFRDNO.requestFocus();
					}
					M_rstRSSET.close();
				}
				else if(L_KE.getSource().equals(txtTODNO))
				{
					M_strSQLQRY = "Select dot_dorno from mr_dotrn where dot_dorno='"+strTODNO+"'";
					M_strSQLQRY += " and dot_dorno >= '"+strFRDNO+"'";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
					if(M_rstRSSET.next())
					{
						setMSG("Select Printing Option.",'N');
						rdbPLNPN.requestFocus();
					}
					else
					{
						setMSG("InValid D.O. No.",'E');
						txtTODNO.requestFocus();
					}
					M_rstRSSET.close();
				}
				else if(L_KE.getSource().equals(rdbPLNPN))
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	
	void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			if(M_strHLPFLD.equals("txtPRDTP"))
				txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtFRDNO"))
				txtFRDNO.setText(cl_dat.M_strHLPSTR_pbst);
			else if(M_strHLPFLD.equals("txtTODNO"))
				txtTODNO.setText(cl_dat.M_strHLPSTR_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	
	private void inlDOHDR()
	{
		strBYRCD1="";strBYRCD="";strBYRCD2="";	
		strCNSCD1="";strCNSCD="";strCNSCD2="";	
		strINDNO1="";strINDNO="";strINDNO2="";	
		strINDDT1="";strINDDT="";strINDDT2="";	
		strAMIDT1="";strAMIDT="";strAMIDT2="";	
		strAMINO1="";strAMINO="";strAMINO2="";	
		strPORNO1="";strPORNO="";strPORNO2="";	
		strPORDT1="";strPORDT="";strPORDT2="";	
		strSALTP1="";strSALTP="";strSALTP2="";	
		strDTPCD1="";strDTPCD="";strDTPCD2="";	
		strDSRCD1="";strDSRCD="";strDSRCD2="";	
		strCPTVL1="";strCPTVL="";strCPTVL2="";	
	    strSTSFL1="";strSTSFL="";strSTSFL2=""; 
		strDORDT1="";strDORDT="";strDORDT2="";	
		strAMDDT1="";strAMDDT="";strAMDDT2="";	
		strAMDNO1="";strAMDNO="";strAMDNO2="";	
		strTMOCD1="";strTMOCD="";strTMOCD2="";	
		strLRYNO1="";strLRYNO="";strLRYNO2="";	
		strTRPCD1="";strTRPCD="";strTRPCD2=""; 
	}

	/** Procedure for confirming, whether D.O. is cancelled completelly or not
	*/	
	private boolean chkCANCSTS(String LP_MKTTP, String LP_DORNO)
	{
		ResultSet L_rstRSSET = null;
		boolean L_flgRETFL = true;
		try
		{
			L_rstRSSET=cl_dat.exeSQLQRY2("Select dot_dorqt,dot_stsfl from MR_DOTRN where DOT_MKTTP = '"+LP_MKTTP+"' and DOT_DORNO='"+LP_DORNO+"'");
			if(L_rstRSSET==null || !L_rstRSSET.next())
			{
				setMSG("Record not found in MR_DOTRN for : "+LP_DORNO,'E'); 
				return true;
			}
			while (true)
			{
				if(!getRSTVAL(L_rstRSSET,"DOT_STSFL","C").equals("X") || Float.parseFloat(getRSTVAL(L_rstRSSET,"DOT_DORQT","N"))>0)
				{
					L_flgRETFL = false; 
					break;
				}
				if(!L_rstRSSET.next())
					break;
			}
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkCANCSTS"); 
			return false;
		}
		return L_flgRETFL;
	}
	
	/** Initializing D.O.detail variables
	 */
	private void inlDODTL()
	{
	}
	private void prnALLREC(String LP_PRDTP, String LP_FRDNO, String LP_TODNO)
	{
		try
		{
			strPRDTP = LP_PRDTP;
			O_FOUT = crtFILE(strRESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);	
			prnFMTCHR(O_DOUT,M_strCPI17);
		    //O_DOUT.writeBytes("0         1         2         3         4         5         6         7         8         9         0         1         2         3         4\n");
		    //O_DOUT.writeBytes("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
		    //O_DOUT.writeBytes("0         1         2         3         4         5         6         7         8         9         0         1         2         3         4\n");
		    //O_DOUT.writeBytes("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
		    //O_DOUT.writeBytes("0         1         2         3         4         5         6         7         8         9         0         1         2         3         4\n");
		    //O_DOUT.writeBytes("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n");
		    strISODCA = cl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR01");
		    strISODCB = cl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR02");
		    strISODCC = cl_dat.getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPDOR03");
			boolean L_PREPN = false;
			boolean L_EOF = false;
			boolean L_1STSFL = true;
			flgPRNHDR = false;
			strLINENO = 0;
			
			if(rdbPREPN.isSelected())
				L_PREPN = true;
				
			M_strSQLQRY  = "Select in_byrcd,in_cnscd,in_indno,in_inddt,in_amddt,in_porno,";
			M_strSQLQRY += " in_pordt,in_saltp,in_dtpcd,in_dsrcd,in_cptvl,in_amdno,";
		    M_strSQLQRY += " dot_mkttp,dot_dorno,dot_dordt,dot_stsfl,dot_amdno,dot_amddt,dot_tmocd,dot_lryno,dot_mkttp,";
		    M_strSQLQRY += " dot_trpcd,dot_prdds,dot_lotrf,dot_dorpk,dot_dorqt,dot_deltp,dot_autdt,dot_autby,dot_pkgtp,";
			M_strSQLQRY += " int_basrt,int_excrt,dod_dspdt,dod_dorqt";
		    M_strSQLQRY += " from vw_intrn,mr_dotrn left outer join mr_dodel on ";
			M_strSQLQRY += " dot_dorno=dod_dorno and dot_mkttp=dod_mkttp and dot_prdcd=dod_prdcd ";
			M_strSQLQRY += " where dot_mkttp='"+LP_PRDTP+"'  and  dot_mkttp=in_mkttp and dot_indno=in_indno and dot_prdcd=int_prdcd and dot_pkgtp = int_pkgtp ";
		    M_strSQLQRY += " and dot_dorno between '"+LP_FRDNO+"' and '"+LP_TODNO+"'";
			M_strSQLQRY += " order by dot_dorno";
		        
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY );
				
			if(M_rstRSSET.next())
			{
				while(!L_EOF)
				{
					strDORNO = nvlSTRVL(M_rstRSSET.getString("dot_dorno"),"").trim();	
		            strSTSFL = nvlSTRVL(M_rstRSSET.getString("dot_stsfl"),"").trim();
		            if (strSTSFL.equals("0"))
		            {
						oppOPTNPN.showMessageDialog(this,"D.O."+strDORNO+" is not Authorised","Error Message",JOptionPane.INFORMATION_MESSAGE);
						if(!M_rstRSSET.next())
						{
							L_EOF = true;
							break;
						}
		                continue;
		            }

					flgCANCDO = false;
					if(chkCANCSTS(LP_PRDTP, strDORNO))
						flgCANCDO = true;
					L_flgCANCPRT = false;
		            strBYRCD = nvlSTRVL(M_rstRSSET.getString("in_byrcd"),"").trim(); 
					strCNSCD = nvlSTRVL(M_rstRSSET.getString("in_cnscd"),"").trim();	
					//System.out.println("12");
					strINDNO = nvlSTRVL(M_rstRSSET.getString("in_indno"),"").trim();	
					if(M_rstRSSET.getString("in_inddt")!=null)
						strINDDT = M_fmtLCDAT.format(M_rstRSSET.getDate("in_inddt"));	
					else
						strINDDT = "";
					if(M_rstRSSET.getString("in_amddt")!=null)
						strAMIDT = M_fmtLCDAT.format(M_rstRSSET.getDate("in_amddt"));	
					else
						strAMIDT = "";
					strAMINO = nvlSTRVL(M_rstRSSET.getString("in_amdno").toString(),"").trim();	
					strPORNO = nvlSTRVL(M_rstRSSET.getString("in_porno"),"").trim();	
					if(M_rstRSSET.getString("in_pordt")!=null)
						strPORDT = M_fmtLCDAT.format(M_rstRSSET.getDate("in_pordt"));	
					else
						strPORDT = "";
					strSALTP = nvlSTRVL(M_rstRSSET.getString("in_saltp").toString(),"").trim();	
					strDTPCD = nvlSTRVL(M_rstRSSET.getString("in_dtpcd"),"").trim();	
					strDSRCD = nvlSTRVL(M_rstRSSET.getString("in_dsrcd"),"").trim();	
					strCPTVL = nvlSTRVL(M_rstRSSET.getString("in_cptvl"),"").trim();	
		            strSTSFL = nvlSTRVL(M_rstRSSET.getString("dot_stsfl"),"").trim(); 
					if(M_rstRSSET.getString("dot_dordt")!=null)
						strDORDT = M_fmtLCDAT.format(M_rstRSSET.getDate("dot_dordt"));	
					else 
						strDORDT = "";
					if(M_rstRSSET.getString("dot_amddt")!=null)
						strAMDDT = M_fmtLCDAT.format(M_rstRSSET.getDate("dot_amddt"));	
					else
						strAMDDT = "";
					strAMDNO = nvlSTRVL(M_rstRSSET.getString("dot_amdno"),"").trim();	
					strTMOCD = nvlSTRVL(M_rstRSSET.getString("dot_tmocd"),"").trim();	
					strLRYNO = nvlSTRVL(M_rstRSSET.getString("dot_lryno"),"").trim();	
					strMKTTP = nvlSTRVL(M_rstRSSET.getString("dot_mkttp"),"").trim();	
		            strTRPCD = nvlSTRVL(M_rstRSSET.getString("dot_TRPCD"),"").trim(); 
					strPRDDS = nvlSTRVL(M_rstRSSET.getString("dot_prdds"),"").trim();	
					//strPKGDS = nvlSTRVL(M_rstRSSET.getString("dot_lotrf"),"").trim();
					strPKGDS = hstPKGTP.get(nvlSTRVL(M_rstRSSET.getString("dot_pkgtp"),"")).toString();	//ATC
					strDORPK = nvlSTRVL(M_rstRSSET.getString("dot_dorpk"),"").trim();	
					strDORQT = nvlSTRVL(M_rstRSSET.getString("dot_dorqt"),"").trim();	
					strDELTP = nvlSTRVL(M_rstRSSET.getString("dot_deltp"),"").trim();	
		            strAUTBY = nvlSTRVL(M_rstRSSET.getString("dot_autby"),"").trim(); 
					if(M_rstRSSET.getString("dot_autdt")!=null)
						strAUTDT = M_fmtLCDAT.format(M_rstRSSET.getDate("dot_autdt"));	
					else
						strAUTDT = "";
					strBASRT = nvlSTRVL(M_rstRSSET.getString("int_basrt"),"").trim();	
					strEXCRT = nvlSTRVL(M_rstRSSET.getString("int_excrt"),"").trim();	
					if(M_rstRSSET.getString("dod_dspdt")!=null)
						strDSPDT = M_fmtLCDAT.format(M_rstRSSET.getDate("dod_dspdt"));	
					else
						strDSPDT = "";
					strDODQT = nvlSTRVL(M_rstRSSET.getString("dod_dorqt"),"").trim();	
					
					strBYRCD1 = strBYRCD;	
					strCNSCD1 = strCNSCD;	
					strINDNO1 = strINDNO;	
					strINDDT1 = strINDDT;	
					strAMIDT1 = strAMIDT;	
					strAMINO1 = strAMINO;	
					strPORNO1 = strPORNO;	
					strPORDT1 = strPORDT;	
					strSALTP1 = strSALTP;	
					strDTPCD1 = strDTPCD;	
					strDSRCD1 = strDSRCD;	
					strCPTVL1 = strCPTVL;	
					strDORNO1 = strDORNO;	
					strDORDT1 = strDORDT;	
		            strSTSFL1 = strSTSFL; 
					strAMDDT1 = strAMDDT;	
					strAMDNO1 = strAMDNO;	
					strTMOCD1 = strTMOCD;	
					strLRYNO1 = strLRYNO;	
					strMKTTP1 = strMKTTP;	
		            strTRPCD1 = strTRPCD; 
					strPRDDS1 = strPRDDS;	
					strPKGDS1 = strPKGDS;	
					strDORPK1 = strDORPK;	
					strDORQT1 = strDORQT;	
					strDELTP1 = strDELTP;	
		            strAUTBY1 = strAUTBY; 
					strAUTDT1 = strAUTDT;	
					strBASRT1 = strBASRT;	
					strEXCRT1 = strEXCRT;	
					strDSPDT1 = strDSPDT;	
					strDODQT1 = strDODQT;	

					strBYRCD2 = strBYRCD;
					strCNSCD2 = strCNSCD;	
					strINDNO2 = strINDNO;	
					strINDDT2 = strINDDT;	
					strAMIDT2 = strAMIDT;	
					strAMINO2 = strAMINO;	
					strPORNO2 = strPORNO;	
					strPORDT2 = strPORDT;	
					strSALTP2 = strSALTP;	
					strDTPCD2 = strDTPCD;	
					strDSRCD2 = strDSRCD;	
					strCPTVL2 = strCPTVL;	
					strDORNO2 = strDORNO;	
					strDORDT2 = strDORDT;	
		            strSTSFL2 = strSTSFL; 
					strAMDDT2 = strAMDDT;	
					strAMDNO2 = strAMDNO;	
					strTMOCD2 = strTMOCD;	
					strLRYNO2 = strLRYNO;	
					strMKTTP2 = strMKTTP;	
		            strTRPCD2 = strTRPCD; 
					strPRDDS2 = strPRDDS;	
					strPKGDS2 = strPKGDS;	
					strDORPK2 = strDORPK;	
					strDORQT2 = strDORQT;	
					strDELTP2 = strDELTP;	
		            strAUTBY2 = strAUTBY; 
					strAUTDT2 = strAUTDT;	
					strBASRT2 = strBASRT;	
					strEXCRT2 = strEXCRT;	
					strDSPDT2 = strDSPDT;	
					strDODQT2 = strDODQT;	

					prnHEADER(L_PREPN);
					prnGRPHDR("DON",L_PREPN);
					strDORNO1 = strDORNO;
					if(flgCANCDO && !L_flgCANCPRT)
					{
					    prnFMTCHR(O_DOUT,M_strNOCPI17);
					    prnFMTCHR(O_DOUT,M_strCPI12);
						O_DOUT.writeBytes(" * * *   C A N C E L L E D   D E L I V E R Y   O R D E R  * * *  \n");
						L_flgCANCPRT = true;
					    prnFMTCHR(O_DOUT,M_strCPI17);
					}
					while(strDORNO.equals(strDORNO1) && !L_EOF)
					{
						prnGRPHDR("GRD",L_PREPN);
						strDORNO = strDORNO2;
						strDORNO1 = strDORNO;
						while((strDORNO+strPRDDS+strPKGDS+strDORPK+strDORQT+strBASRT+strEXCRT).equals(strDORNO1+strPRDDS1+strPKGDS1+strDORPK1+strDORQT1+strBASRT1+strEXCRT1) && !L_EOF)
						{
							strDORNO = strDORNO2;
							strDORNO1 = strDORNO;
							strPRDDS = strPRDDS2;
							strPRDDS1 = strPRDDS;
							strPKGDS = strPKGDS2;
							strPKGDS1 = strPKGDS;
							strDORPK = strDORPK2;
							//System.out.println("21");
							strDORPK1 = strDORPK;
							strDORQT = strDORQT2;
							//System.out.println("21a");
							strDORQT1 = strDORQT;
							strBASRT = strBASRT2;
							strBASRT1 = strBASRT;
							//System.out.println("21b");
							strEXCRT = strEXCRT2;
							strEXCRT1 = strEXCRT;
							//System.out.println("22");

							while((strDORNO+strPRDDS+strPKGDS+strDORPK+strDORQT+strBASRT+strEXCRT+strDSPDT+strDODQT+strDELTP).equals(strDORNO1+strPRDDS1+strPKGDS1+strDORPK1+strDORQT1+strBASRT1+strEXCRT1+strDSPDT1+strDODQT1+strDELTP1) && !L_EOF)
							{
								if(!M_rstRSSET.next())
								{
									L_EOF = true;
									break;
								}
								strBYRCD2 = nvlSTRVL(M_rstRSSET.getString("in_byrcd"),"").trim();	
								strCNSCD2 = nvlSTRVL(M_rstRSSET.getString("in_cnscd"),"").trim();	
								strINDNO2 = nvlSTRVL(M_rstRSSET.getString("in_indno"),"").trim();	

								if(M_rstRSSET.getString("in_inddt")!=null)
									strINDDT2 = M_fmtLCDAT.format(M_rstRSSET.getDate("in_inddt"));	
								else
									strINDDT2 = "";

								if(M_rstRSSET.getString("in_amddt")!=null)
									strAMIDT2 = M_fmtLCDAT.format(M_rstRSSET.getDate("in_amddt"));	
								else 
									strAMIDT2 = "";
								strAMINO2 = nvlSTRVL(M_rstRSSET.getString("in_amdno"),"").trim();	
								strPORNO2 = nvlSTRVL(M_rstRSSET.getString("in_porno"),"").trim();	

								if(M_rstRSSET.getString("in_pordt")!=null)
									strPORDT2 = M_fmtLCDAT.format(M_rstRSSET.getDate("in_pordt"));	
								else
									strPORDT2 = "";

								strSALTP2 = nvlSTRVL(M_rstRSSET.getString("in_saltp"),"").trim();	
								strDTPCD2 = nvlSTRVL(M_rstRSSET.getString("in_dtpcd"),"").trim();	
								strDSRCD2 = nvlSTRVL(M_rstRSSET.getString("in_dsrcd"),"").trim();	
								strCPTVL2 = nvlSTRVL(M_rstRSSET.getString("in_cptvl"),"").trim();	
								strDORNO2 = nvlSTRVL(M_rstRSSET.getString("dot_dorno"),"").trim();	
		                        strSTSFL2 = nvlSTRVL(M_rstRSSET.getString("dot_stsfl"),"").trim(); 
								if(M_rstRSSET.getString("dot_dordt")!=null)
									strDORDT2 = M_fmtLCDAT.format(M_rstRSSET.getDate("dot_dordt"));	
								else
									strDORDT2 = "";

								if(M_rstRSSET.getString("dot_amddt")!=null)
									strAMDDT2 = M_fmtLCDAT.format(M_rstRSSET.getDate("dot_amddt"));	
								else
									strAMDDT2 = "";

								strAMDNO2 = nvlSTRVL(M_rstRSSET.getString("dot_amdno"),"").trim();	
								strTMOCD2 = nvlSTRVL(M_rstRSSET.getString("dot_tmocd"),"").trim();	
								strLRYNO2 = nvlSTRVL(M_rstRSSET.getString("dot_lryno"),"").trim();	
								strMKTTP2 = nvlSTRVL(M_rstRSSET.getString("dot_mkttp"),"").trim();	
		                        strTRPCD2 = nvlSTRVL(M_rstRSSET.getString("dot_TRPCD"),"").trim(); 
								strPRDDS2 = nvlSTRVL(M_rstRSSET.getString("dot_prdds"),"").trim();	
								//strPKGDS2 = nvlSTRVL(M_rstRSSET.getString("dot_lotrf"),"").trim();
								strPKGDS2 = hstPKGTP.get(nvlSTRVL(M_rstRSSET.getString("dot_pkgtp"),"")).toString();	//ATC
								
								strDORPK2 = nvlSTRVL(M_rstRSSET.getString("dot_dorpk"),"").trim();	
								strDORQT2 = nvlSTRVL(M_rstRSSET.getString("dot_dorqt"),"").trim();	
								strDELTP2 = nvlSTRVL(M_rstRSSET.getString("dot_deltp"),"").trim();	
		                        strAUTBY2 = nvlSTRVL(M_rstRSSET.getString("dot_autby"),"").trim(); 
								if(M_rstRSSET.getString("dot_autdt")!=null)
									strAUTDT2 = M_fmtLCDAT.format(M_rstRSSET.getDate("dot_autdt"));	
								else
									strAUTDT2 = "";

								strBASRT2 = nvlSTRVL(M_rstRSSET.getString("int_basrt"),"").trim();	
								strEXCRT2 = nvlSTRVL(M_rstRSSET.getString("int_excrt"),"").trim();	

								if(M_rstRSSET.getString("dod_dspdt")!=null)
									strDSPDT2 = M_fmtLCDAT.format(M_rstRSSET.getDate("dod_dspdt"));	
								else
									strDSPDT2 = "";

								strDODQT2 = nvlSTRVL(M_rstRSSET.getString("dod_dorqt"),"").trim();	
									
								strDORNO = strDORNO2;
								strPRDDS = strPRDDS2;
								strPKGDS = strPKGDS2;
								strDORPK = strDORPK2;
								strDORQT = strDORQT2;
								strBASRT = strBASRT2;
								strEXCRT = strEXCRT2;
								strDSPDT = strDSPDT2;
								strDODQT = strDODQT2;
								strDELTP = strDELTP2;
							}
							prnGRPTOT("SCH",L_PREPN);
							intGRPTOT("SCH");
						}

		                intGRPTOT("GRD");
					}
					prnFOOTR(L_PREPN);
					prnFMTCHR(O_DOUT,M_strEJT);
					intGRPTOT("DON");
				}
				M_rstRSSET.close();
				O_DOUT.close();
				O_FOUT.close();
				setMSG(" ",'N');
			}
			else
				setMSG("No record exist.",'E');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnALLREC");
		}
	}
	
	public static DataOutputStream crtDTOUTSTR(FileOutputStream outfile)
	{
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}
	public static FileOutputStream crtFILE(String strFILE)
	{
		FileOutputStream outFILE = null;
		try
		{
			File file = new File(strFILE);
			outFILE = new FileOutputStream(file);
		   	return outFILE;
		}
		catch(IOException L_IO)
		{
			System.out.println("L_IO FOS...........:"+L_IO);
			return outFILE;		
		}
	}
	
	private void prnHEADER(boolean LP_PREPN)
	{
		try
		{
			O_DOUT.writeBytes("\n");
            //prnFMTCHR(O_DOUT,M_BOLD);
			prnFMTCHR(O_DOUT,M_strNOCPI17);
            prnFMTCHR(O_DOUT,M_strCPI12);
            O_DOUT.writeBytes(strLFTMRG+(padSTR1('C',"SUPREME PETROCHEM LTD.",125,"120",'P')).substring(0,(125-21)*120/171));
            prnFMTCHR(O_DOUT,M_strCPI17);
            O_DOUT.writeBytes(padSTR1('C',"------------------------------",30,"171",'N')+"\n");

			String L_ADRESS = "17/18, SHAH INDUSTRIAL ESTATE, VEERA DESAI ROAD, ANDHERI(WEST), MUMBAI - 400 053";
            O_DOUT.writeBytes(strLFTMRG+padSTR1('C',L_ADRESS,125,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strISODCA,30,"171",'N')+"\n");

			L_ADRESS = "TEL : 91-22-26311839-42 FAX: 26902362,26367317,26324828 E-MAIL: marketing@spl.co.in";
            O_DOUT.writeBytes(strLFTMRG+padSTR1('C',L_ADRESS,125,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strISODCB,30,"171",'N')+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',".",125,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strISODCC,30,"171",'N')+"\n");
			String L_TMPSTR0 = rdbPREPN.isSelected() ? "" : "DELIVERY ORDER";
            L_TMPSTR = strAMDNO1.equals("00") ? L_TMPSTR0 :"D.O.AMENDMENT";

			prnFMTCHR(O_DOUT,M_strNOCPI17);
            prnFMTCHR(O_DOUT,M_strCPI12);
			String L_strORDHDR = flgCANCDO ? "CANCELLED DELIVERY ORDER" : L_TMPSTR;
			O_DOUT.writeBytes(strLFTMRG+(padSTR1('C',L_strORDHDR,125,"120",'N')).substring(0,(125-21)*120/171));
			prnFMTCHR(O_DOUT,M_strCPI17);
            O_DOUT.writeBytes(padSTR1('R',"------------------------------",30,"171",'N')+"\n");
            O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-"));
            //prnFMTCHR(O_DOUT,M_NOBOLD);
			O_DOUT.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}	
	
    private void prnGRPHDR(String LP_GRPCT,boolean LP_PREPN)
	{
		try
		{
        	if(LP_GRPCT.equals("DON"))
			{
				getBYRDTL();
				if(strBYRCD1.equals(strCNSCD1))
				{
					strCNCOD = "Same as Buyer";
					strCNSNM = " ";
					strCNAD1 = " ";
					strCNAD2 = " ";
					strCNAD3 = " ";
					strCNAD4 = " ";
					strCNPIN = " ";
					strCNCST = " ";
					strCNSTN = " ";
					strCNECC = " ";
				}
				else
					getCNSDTL();
                prnFMTCHR(O_DOUT,M_strCPI17);
                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Buyer & Address",53,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Consignee Name & Address",49,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"D.O.No.",15,"171",'P'));
				
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                L_TMPSTR = Integer.parseInt(strAMDNO1)>0 ? "/"+strAMDNO1 : "";
                //O_DOUT.writeBytes(padSTR1('R',strDORNO1+L_TMPSTR,18,"12",'N'));
				O_DOUT.writeBytes(padSTRING('R',strDORNO1+L_TMPSTR,20));//ATC
				O_DOUT.writeBytes("\n");

                prnFMTCHR(O_DOUT,M_strCPI17);
                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYRCD1,53,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',strCNCOD,49,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"D.O. Date",15,"171",'P'));
				
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strDORDT1,15,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strDORDT1,15)+"\n");
				prnFMTCHR(O_DOUT,M_strCPI17);

                
				//O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYRNM,62,"171",'N'));
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYRNM,53,"171",'N'));//ATC
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNSNM,62,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNSNM,49,"171",'N'));//ATC
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Amd.Date",15,"171",'N'));
				L_TMPSTR = Integer.parseInt(strAMDNO1)>0 ? strAMDDT1 : "";
                //O_DOUT.writeBytes(padSTR1('R',L_TMPSTR,18,"171",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',L_TMPSTR,15)+"\n");//ATC

                //O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD1,62,"171",'N'));
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD1,53,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNAD1,62,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNAD1,49,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(crtLINE(45,"-")+"\n");

                //O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD2,62,"171",'N'));
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD2,53,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNAD2,62,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNAD2,49,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Ind.No.",15,"171",'P'));
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                L_TMPSTR = Integer.parseInt(strAMINO1)>0 ? "/"+strAMINO1:"";
                //O_DOUT.writeBytes(padSTR1('R',strINDNO1+L_TMPSTR,18,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strINDNO1+L_TMPSTR,25)+"\n");//ATC

                prnFMTCHR(O_DOUT,M_strCPI17);
                //O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD3,62,"171",'N'));
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD3,53,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNAD3,62,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNAD3,49,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Ind.Date",15,"171",'P'));

                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strINDDT1,18,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strINDDT1,18)+"\n"); //ATC
                prnFMTCHR(O_DOUT,M_strCPI17);

                //O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD4,62,"171",'N'));
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYAD4,53,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNAD4,62,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNAD4,49,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Amd.Date",15,"171",'N'));
                L_TMPSTR = Integer.parseInt(strAMINO1)>0 ? strAMIDT1:"";
                //O_DOUT.writeBytes(padSTR1('R',L_TMPSTR,18,"171",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',L_TMPSTR,18)+"\n"); //ATC

                //O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYPIN,62,"171",'N'));
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strBYPIN,53,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNPIN,62,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNPIN,49,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(crtLINE(45,"-")+"\n");

                //O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",62,"171",'P'));
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",53,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R'," ",62,"171",'P'));
				O_DOUT.writeBytes(padSTR1('R'," ",49,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',"P.O.No.",20,"171",'P'));
				O_DOUT.writeBytes(padSTR1('R',"P.O.No.",15,"171",'P'));//ATC
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strPORNO1,15,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strPORNO1,15)+"\n"); //ATC

                prnFMTCHR(O_DOUT,M_strCPI17);
                //O_DOUT.writeBytes(strLFTMRG+crtLINE(124,"-"));
				O_DOUT.writeBytes(strLFTMRG+crtLINE(105,"-"));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',"P.O.Date",20,"171",'P'));
				O_DOUT.writeBytes(padSTR1('R',"P.O.Date",15,"171",'P'));//ATC
                //O_DOUT.writeBytes(padSTR1('R',strPORDT1,15,"171",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strPORDT1,15)+"\n");  //ATC
				
                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"C.S.T.No.",18,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strBYCST,45,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strBYCST,35,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"C.S.T.No.",18,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNCST,45,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNCST,31,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(crtLINE(45,"-")+"\n");
				
                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"S.T.No.",18,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strBYSTN,45,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strBYSTN,35,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"S.T.No.",18,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNSTN,45,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNSTN,31,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',"Type of sale",20,"171",'P'));
				O_DOUT.writeBytes(padSTR1('R',"Type of sale",15,"171",'P'));//ATC

				prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                strSLTYP = cl_dat.getPRMCOD("CMT_CODDS","SYS","MR00SAL",strSALTP1);
                //O_DOUT.writeBytes(padSTR1('R',strSLTYP,15,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strSLTYP,15)+"\n");  //ATC
				
                prnFMTCHR(O_DOUT,M_strCPI17);
                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"ECC No.",18,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strBYECC,45,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strBYECC,35,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"ECC No.",18,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',strCNECC,45,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',strCNECC,31,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',"Del.Type",20,"171",'N'));
				O_DOUT.writeBytes(padSTR1('R',"Del.Type",15,"171",'N'));//ATC
				strDLTYP = cl_dat.getPRMCOD("CMT_CODDS","SYS","MRXXDTP",strDTPCD1);
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strDLTYP,15,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strDLTYP,15)+"\n");  //ATC

                prnFMTCHR(O_DOUT,M_strCPI17);
                //O_DOUT.writeBytes(strLFTMRG+crtLINE(124,"-"));
				O_DOUT.writeBytes(strLFTMRG+crtLINE(105,"-"));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P')+"\n");

                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Distributor",60,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Credit",42,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                //O_DOUT.writeBytes(padSTR1('R',"Mode of Trans.",20,"171",'P'));
				O_DOUT.writeBytes(padSTR1('R',"Mode of Trans.",15,"171",'P'));//ATC
				strTRMOD = cl_dat.getPRMCOD("CMT_CODDS","SYS","MR"+strPRDTP+"MOT",strTMOCD1);
                //O_DOUT.writeBytes(padSTR1('R',strTRMOD,15,"171",'N')+"\n");
				O_DOUT.writeBytes(padSTR1('R',strTRMOD,10,"171",'N')+"\n");//ATC

				strDISTR = cl_dat.getPRMPRT("PT_PRTNM","D",strDSRCD1);
                O_DOUT.writeBytes(strLFTMRG+padSTR1('C',strDISTR,60,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Period",15,"171",'P'));
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strCPTVL1+" Days",42,"12",'N'));
				O_DOUT.writeBytes(padSTRING('C',strCPTVL1+" Days",16));
				prnFMTCHR(O_DOUT,M_strCPI17);
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Veh.No.",15,"171",'P'));
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strLRYNO1,15,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strLRYNO1,15)+"\n");


                prnFMTCHR(O_DOUT,M_strCPI17);
                O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n");

                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Product",60,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                strTRNSP = cl_dat.getPRMPRT("PT_PRTNM","T",strTRPCD1); 
                O_DOUT.writeBytes(padSTR1('R',"Transporter",29,"171",'P'));
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strTRNSP,62,"12",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strTRNSP,40)+"\n");  //ATC

                prnFMTCHR(O_DOUT,M_strCPI17);
                L_TMPSTR = cl_dat.getPRMCOD("CMT_CODDS","MST","COXXMKT",strMKTTP1);
                O_DOUT.writeBytes(strLFTMRG+padSTR1('C',L_TMPSTR,60,"171",'N'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Fax No :",29,"171",'P'));
                strFAXNO = cl_dat.getPRMPRT("PT_FAXNO","T",strTRPCD1); 
                prnFMTCHR(O_DOUT,M_strNOCPI17);
                prnFMTCHR(O_DOUT,M_strCPI12);
                //O_DOUT.writeBytes(padSTR1('R',strFAXNO,62,"171",'N')+"\n");
				O_DOUT.writeBytes(padSTRING('R',strFAXNO,40)+"\n");

                prnFMTCHR(O_DOUT,M_strCPI17);
                O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n");

                /*O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Grade",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Lot Nos.",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"No. of Bags",18,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"20",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Qty.",18,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Basic Rate",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Rate of",16,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('C',"Schedule",30,"171",'P')+"\n");

                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',".",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L'," ",18,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"MT.",19,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Rs./MT",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Excise",16,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('C',"Date",16,"171",'P'));
                O_DOUT.writeBytes(padSTR1('C',"Qty",14,"171",'P')+"\n");
                O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n\n");*/
				
				///Replace by ATC
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Grade",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"Qty.",19,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Pkg.",12,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"20",'P'));
                O_DOUT.writeBytes(padSTR1('L',"No. Of",20,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Basic Rate",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Rate of",15,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('C',"Schedule",30,"171",'P')+"\n");

                O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"MT.",19,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Type(kg)",12,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Pkgs.",17,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Rs./MT",26,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',"Excise",15,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('C',"Date",16,"171",'P'));
                O_DOUT.writeBytes(padSTR1('C',"Qty",14,"171",'P')+"\n");
                O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n\n");
				////
				strLINENO += 1;
			}
			else if(LP_GRPCT.equals("GRD"))
			{
				prnFMTCHR(O_DOUT,M_strNOCPI17);
				prnFMTCHR(O_DOUT,M_strCPI12);
                /*O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strPRDDS1,24,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',strPKGDS1,22,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strDORPK1,16,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strDORQT1,18,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strBASRT1,24,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strEXCRT1,14,"120",'N'));*/
				dblEXCRT = Double.parseDouble(strEXCRT1)>0 ? Double.parseDouble(strEXCRT1)+0.32 : Double.parseDouble(strEXCRT1);
				//Replace By ATC
				O_DOUT.writeBytes(strLFTMRG+padSTRING('R',strPRDDS1,15));
                O_DOUT.writeBytes(padSTRING('L',strDORQT1,8));
				O_DOUT.writeBytes(padSTRING('L',strPKGDS1,8));
				O_DOUT.writeBytes(padSTRING('L',strDORPK1,12));
                O_DOUT.writeBytes(padSTRING('L',strBASRT1,17));
                O_DOUT.writeBytes(padSTRING('L',setNumberFormat(dblEXCRT,2),10));
				prnFMTCHR(O_DOUT,M_strCPI17);
				flgPRNHDR = true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPHDR");
		}
	}
	
	private void prnGRPTOT(String LP_GRPCT,boolean LP_PREPN)
	{
		try
		{
			if(LP_GRPCT.equals("GRD"))
			{
				prnFMTCHR(O_DOUT,M_strNOCPI17);
				prnFMTCHR(O_DOUT,M_strCPI12);
                /*O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strPRDDS1,24,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('R',strPKGDS1,22,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strDORPK1,16,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strDORQT1,18,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strBASRT1,24,"120",'N'));
                O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'P'));
                O_DOUT.writeBytes(padSTR1('L',strEXCRT1,14,"120",'N'));*/
				//Replace By ATC
				dblEXCRT = Double.parseDouble(strEXCRT1)>0 ? Double.parseDouble(strEXCRT1)+0.32 : Double.parseDouble(strEXCRT1);
				O_DOUT.writeBytes(strLFTMRG+padSTRING('R',strPRDDS1,15));
                O_DOUT.writeBytes(padSTRING('L',strDORQT1,8));
				O_DOUT.writeBytes(padSTRING('L',strPKGDS1,8));
				O_DOUT.writeBytes(padSTRING('L',strDORPK1,12));
                O_DOUT.writeBytes(padSTRING('L',strBASRT1,17));
                O_DOUT.writeBytes(padSTRING('L',setNumberFormat(dblEXCRT,2),10));
				prnFMTCHR(O_DOUT,M_strCPI17);
				flgPRNHDR = true;
			}
			else if(LP_GRPCT.equals("SCH"))
			{
                prnFMTCHR(O_DOUT,M_strCPI17);
				if(flgPRNHDR)
				{
					if(strDELTP1.equalsIgnoreCase("I"))
						O_DOUT.writeBytes("    "+padSTR1('L',"Immediate",12,"171",'N'));
					else
					{
						O_DOUT.writeBytes("    "+padSTR1('L',strDSPDT1,12,"171",'N'));
                        O_DOUT.writeBytes(padSTR1('L',strDODQT1,12,"171",'N'));
					}
					flgPRNHDR = false;
				}
				else
				{
                    prnFMTCHR(O_DOUT,M_strNOCPI17);
                    prnFMTCHR(O_DOUT,M_strCPI12);
                    O_DOUT.writeBytes(padSTR1('L'," ",15+8+8+12+17+10+38,"120",'N'));
                    prnFMTCHR(O_DOUT,M_strCPI17);
                    O_DOUT.writeBytes("   "+padSTR1('L',strDSPDT1,12,"171",'N'));
                    O_DOUT.writeBytes(padSTR1('L',strDODQT1,12,"171",'N'));
				}
				prnFMTCHR(O_DOUT,M_strCPI17);
			}
            O_DOUT.writeBytes("\n");
			strLINENO += 1;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnGRPTOT");
		}
	}
	
	private void intGRPTOT(String LP_GRPCT)
	{
		try
		{
			if(LP_GRPCT.equals("SCH"))
			{
				strBYRCD1 = strBYRCD;	
				strCNSCD1 = strCNSCD;	
				strINDNO1 = strINDNO;	
				strINDDT1 = strINDDT;	
				strAMIDT1 = strAMIDT;	
				strPORNO1 = strPORNO;	
				strPORDT1 = strPORDT;	
				strSALTP1 = strSALTP;	
				strDTPCD1 = strDTPCD;	
				strDSRCD1 = strDSRCD;	
				strCPTVL1 = strCPTVL;	
				strDORDT1 = strDORDT;	
                strSTSFL1 = strSTSFL; 
				strAMDDT1 = strAMDDT;	
				strTMOCD1 = strTMOCD;	
				strLRYNO1 = strLRYNO;	
				strMKTTP1 = strMKTTP;	
                strTRPCD1 = strTRPCD; 
				strDELTP1 = strDELTP;	
                strAUTBY1 = strAUTBY; 
				strAUTDT1 = strAUTDT;	
				strDSPDT1 = strDSPDT;	
				strDODQT1 = strDODQT;	
			}
			else if(LP_GRPCT.equals("GRD"))
			{
				strPRDDS1 = strPRDDS;	
				strPKGDS1 = strPKGDS;	
				strDORPK1 = strDORPK;	
				strDORQT1 = strDORQT;	
				strBASRT1 = strBASRT;	
				strEXCRT1 = strEXCRT;	
			}
			else if(LP_GRPCT.equals("DON"))
			{
				strLINENO = 0;
				strDORNO1 = strDORNO;
			}
		}
		catch(Exception L_EX)
        {
             setMSG(L_EX,"intGRPTOT");
		}
	}
	
	private void getBYRDTL()
	{
		try
		{
			ResultSet L_RSLSET;
			M_strSQLQRY = "Select pt_prtnm,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd,pt_cstno,";
			M_strSQLQRY += "pt_stxno,pt_eccno,pt_excno,pt_rngds,pt_divds,pt_cllds,pt_itpno,pt_tstfl";
			M_strSQLQRY += " from co_ptmst where pt_prtcd='"+strBYRCD1+"' and pt_prttp='C'";
			L_RSLSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
			if(L_RSLSET.next())
			{
				strBYRNM = nvlSTRVL(L_RSLSET.getString("pt_prtnm"),"").trim();
				strBYAD1 = nvlSTRVL(L_RSLSET.getString("pt_adr01"),"").trim();
				strBYAD2 = nvlSTRVL(L_RSLSET.getString("pt_adr02"),"").trim();
				strBYAD3 = nvlSTRVL(L_RSLSET.getString("pt_adr03"),"").trim();
				strBYAD4 = nvlSTRVL(L_RSLSET.getString("pt_adr04"),"").trim();
				strBYPIN = nvlSTRVL(L_RSLSET.getString("pt_pincd"),"").trim();
				strBYCST = nvlSTRVL(L_RSLSET.getString("pt_cstno"),"").trim();
				strBYSTN = nvlSTRVL(L_RSLSET.getString("pt_stxno"),"").trim();
				strBYECC = nvlSTRVL(L_RSLSET.getString("pt_eccno"),"").trim();
				strEXCNO = nvlSTRVL(L_RSLSET.getString("pt_excno"),"").trim();
				strRNGDS = nvlSTRVL(L_RSLSET.getString("pt_rngds"),"").trim();
				strDIVDS = nvlSTRVL(L_RSLSET.getString("pt_divds"),"").trim();
				strCLLDS = nvlSTRVL(L_RSLSET.getString("pt_cllds"),"").trim();
				strITPNO = nvlSTRVL(L_RSLSET.getString("pt_itpno"),"").trim();
				strTSTFL = nvlSTRVL(L_RSLSET.getString("pt_tstfl"),"").trim();
			}
			L_RSLSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getBYRDTL");
		}
	}
	
	private void getCNSDTL()
	{
		try
		{
			ResultSet L_RSLSET;
			M_strSQLQRY = "Select pt_prtnm,pt_prtcd,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd,pt_cstno,pt_stxno,pt_eccno";
			M_strSQLQRY += " from co_ptmst where pt_prtcd='"+strCNSCD1+"' and pt_prttp='C'";
			L_RSLSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
			if(L_RSLSET.next())
			{
				strCNSNM = nvlSTRVL(L_RSLSET.getString("pt_prtnm"),"").trim();
				strCNCOD = nvlSTRVL(L_RSLSET.getString("pt_prtcd"),"").trim();
				strCNAD1 = nvlSTRVL(L_RSLSET.getString("pt_adr01"),"").trim();
				strCNAD2 = nvlSTRVL(L_RSLSET.getString("pt_adr02"),"").trim();
				strCNAD3 = nvlSTRVL(L_RSLSET.getString("pt_adr03"),"").trim();
				strCNAD4 = nvlSTRVL(L_RSLSET.getString("pt_adr04"),"").trim();
				strCNPIN = nvlSTRVL(L_RSLSET.getString("pt_pincd"),"").trim();
				strCNCST = nvlSTRVL(L_RSLSET.getString("pt_cstno"),"").trim();
				strCNSTN = nvlSTRVL(L_RSLSET.getString("pt_stxno"),"").trim();
				strCNECC = nvlSTRVL(L_RSLSET.getString("pt_eccno"),"").trim();
			}
			L_RSLSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCNSDTL");
		}
	}
	
	/**
	 *
	 *Method to create lines that are used in the Reports
	 */
	private String crtLINE(int P_strCNT,String P_strLINCHR)
    {
		String strln = "";
        if(rdbPREPN.isSelected() && P_strLINCHR.equals("-"))
        P_strLINCHR = " ";
		try
		{
			for(int i=1;i<=P_strCNT;i++)
			{
				strln += P_strLINCHR;
			}
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
        return strln;
	}
	
	private void prnFOOTR(boolean LP_PREPN)
	{
		try
		{
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
			String L_MONTH = strDORDT1.substring(3,5).toString().trim();
			if(L_MONTH.length() == 2)
			{
				int L_MNTH = Integer.parseInt(L_MONTH);
				if(L_MNTH >= 5 && L_MNTH <= 9)
				{
					
                    L_WARNG = "MONSOON ALARM - ";
                    L_WARNG1 = "TARNSPORTER SHOULD ENSURE WET-FREE TRANSPORTATION OF MATERIAL.THEY SHOULD PROVIDE 3 SETS OF TARPAULIN IN GOOD";
                    L_WARNG2 = "CONDITION WITH EACH TRUCK.ANY COMPENSATION CLAIMED BY THE BUYER DUE TO WETNESS OR ANY SORT";
                    L_WARNG3 = "OF CONTAMINATION OR SPOILAGE ETC. WILL BE ON TRANSPORTER'S ACCOUNT.";
				}
			}
			O_DOUT.writeBytes("\n");
			strLINENO += 1;
            L_TMPSTR = strTSTFL.equalsIgnoreCase("Y") ? "****TEST CERTIFICATE REQUIRED****": "";
            O_DOUT.writeBytes(strLFTMRG+padSTR1('C',L_TMPSTR,82,"171",'N')+"\n");
			strLINENO += 1;
			if(strLINENO <= 14 )
			{
				O_DOUT.writeBytes("\n");
			    //prnFMTCHR(O_DOUT,M_BOLD);
			    O_DOUT.writeBytes(strLFTMRG+padSTR1('R',L_WARNG,20,"171",'N'));
			    O_DOUT.writeBytes(padSTR1('R',L_WARNG1,120,"171",'N')+"\n");

			    O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",20,"171",'N'));
			    O_DOUT.writeBytes(padSTR1('R',L_WARNG2,120,"171",'N')+"\n");

			    O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",20,"171",'N'));
			    O_DOUT.writeBytes(padSTR1('R',L_WARNG3,72,"171",'N'));
			}
			strLINENO += 3;
			do
			{
				O_DOUT.writeBytes("\n");
				strLINENO += 1;
			}while(strLINENO <= 17);
            O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n");
            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Validity of this D.O. is for 10 Days from the date of Issue.\n",82,"171",'P')+"\n");
            O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n");
            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Special Instruction :",25,"171",'P'));

			strREMDS = getREMDS(strDORNO1,strMKTTP1,"DO");
			int L_STRLEN = strREMDS.length();
            O_DOUT.writeBytes(padSTR1('R',strREMDS.substring(0,L_STRLEN < 115 ? strREMDS.length() : 100),140,"171",'N')+"\n");
            O_DOUT.writeBytes(strLFTMRG+crtLINE(20,"-"));

            O_DOUT.writeBytes(padSTR1('R'," ",3,"171",'N'));
			if(L_STRLEN > 115)
				O_DOUT.writeBytes(strLFTMRG+padSTR1('R',strREMDS.substring(100,strREMDS.length()),140,"171",'N'));
            O_DOUT.writeBytes("\n\n");
            O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n");
            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Particulars of MODVAT :",140,"171",'P')+"\n");
            O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n");
            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Exc. Reg. No. :",25,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strEXCNO,81,"171",'N'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
            O_DOUT.writeBytes(padSTR1('C',"For Marketing Div.",42,"171",'P')+"\n");
            //O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",105,"171",'P'));
            //O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P')+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Range         :",25,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strRNGDS,81,"171",'N'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P')+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",106,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P')+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Divison       :",25,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strDIVDS,81,"171",'N'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P')+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",106,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P')+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"Collectorate  :",25,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strCLLDS,81,"171",'N'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
            prnFMTCHR(O_DOUT,M_strNOCPI17);
            prnFMTCHR(O_DOUT,M_strCPI12);
            O_DOUT.writeBytes(padSTR1('C',strAUTBY1,21,"120",'N')+"\n");
			prnFMTCHR(O_DOUT,M_strCPI17);

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R'," ",81,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
            O_DOUT.writeBytes(padSTR1('C',"Authorised Signatory.",21,"171",'P')+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('R',"IT PAN No.    :",25,"171",'P'));
            O_DOUT.writeBytes(padSTR1('R',strITPNO,81,"171",'N'));
            O_DOUT.writeBytes(padSTR1('R',"|",3,"171",'P'));
            //O_DOUT.writeBytes(padSTR1('C',strAUTDT1,42,"171",'N')+"\n");
            prnFMTCHR(O_DOUT,M_strNOCPI17);
            prnFMTCHR(O_DOUT,M_strCPI12);
            O_DOUT.writeBytes(padSTR1('C',cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText()+" Hrs",21,"171",'N')+"\n");
            prnFMTCHR(O_DOUT,M_strCPI17);

            O_DOUT.writeBytes(strLFTMRG+crtLINE(156,"-")+"\n");

            O_DOUT.writeBytes(strLFTMRG+padSTR1('C',"WORKS : VILLAGE AMDOSHI,WAKAN-ROHA,TALUKA : ROHA. DIST : RAIGAD. MAHARASHTRA - 402106",156,"171",'P')+"\n");
            O_DOUT.writeBytes(padSTR1('C',"TEL :(021442)-2228,2540 - 2548 FAX :021442 - 2337, 2537",156,"171",'P')+"\n");
            prnFMTCHR(O_DOUT,M_strNOCPI17);
            prnFMTCHR(O_DOUT,M_strCPI10);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnFOOTR");
		}
	}
	
	/**
	 */
    private  String padSTR1(char P_chrPADTP,String P_strSTRVL,int P_intPADLN, String P_strCHRSZ, char P_chrPRPRT)
	{
		String P_strTRNVL = "";
		//int L_intPADLN = new Double(Double.parseDouble(P_strCHRSZ)*(100/171)*P_intPADLN).intValue();
        int L_intPADLN = new Double((Double.parseDouble(P_strCHRSZ)/171)*P_intPADLN).intValue();
        //int L_intPADLN = P_intPADLN;
        if (P_chrPRPRT == 'P')
        {
			if(rdbPREPN.isSelected())
				return(crtLINE1(L_intPADLN," "));
		}
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
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"padSTR1");
		}
		return P_strTRNVL;
	}
	/** Method to create lines that are used in the Reports
	 */
	private String crtLINE1(int P_strCNT,String P_strLINCHR)
	{
		String strln = "";
        if(rdbPREPN.isSelected() && P_strLINCHR.equals("-"))
			P_strLINCHR = " ";
		try
		{
			for(int i=1;i<=P_strCNT;i++)
			{
				strln += P_strLINCHR;
			}
		}
		catch(Exception L_EX)
		{
			System.out.println("L_EX Error in Line:"+L_EX);
		}
        return strln;
	}
	/** Method for returning values from Result Set
	 * <br> with respective verifications against various data types
	 * @param	LP_RSLSET		Result set name
	 * @param   LP_FLDNM        Name of the field for which data is to be extracted
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
		{
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 
	private String getREMDS(String LP_DORNO,String LP_MKTTP,String LP_TRNTP)
	{
		String L_REMDS = "";
		try
		{
			ResultSet L_RSLSET;
			M_strSQLQRY = "Select rm_remds from mr_rmmst where rm_docno='"+LP_DORNO+"'";
			M_strSQLQRY += " and rm_mkttp='"+LP_MKTTP+"' and rm_trntp='"+LP_TRNTP+"'";
			L_RSLSET = cl_dat.exeSQLQRY2(M_strSQLQRY );
			if(L_RSLSET.next())
				L_REMDS = nvlSTRVL(L_RSLSET.getString("rm_remds").toString(),"").trim();
			if(L_RSLSET != null)
				L_RSLSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREMDS");
		}
		return L_REMDS;
	}
}
