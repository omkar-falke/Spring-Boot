/**
 */
import java.awt.event.KeyEvent;import javax.swing.JComponent;
import java.awt.event.FocusEvent;import java.awt.event.ActionEvent;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;import javax.swing.JRadioButton;
import java.util.StringTokenizer;import java.sql.ResultSet;import java.util.Date;import java.util.*;
import java.io.FileOutputStream;import java.io.DataOutputStream;import java.io.File;
import java.text.SimpleDateFormat;

public class mr_tplad extends cl_rbase
{
	private JTextField txtFRLNO,txtTOLNO;
	public JTextField txtPRDTP;
	
	
	String strFRLNO,strTOLNO,strMKTTP;
	String strISODCA,strISODCB,strISODCC,strTRPNM;
	String strBYRCD;
    String strCNSCD;
	String strSALTP;
	String strSALDS;
	String strGINNO;
	String strGINNO_OLD;
    String strGINDT;
	String strLADNO;
	String strLADDT;
	String strCNTDS;
	String strPKGWT;
	String strDTPCD;
	String strDTPDS;
	String strPRDCD;
	String strTSTFL;
	String strLDGSQ;
	String strDORNO;
	String strDORDT;
	String strDORTM;
	String strPKGTP;
	String strPKGDS;
	String strLRYNO;
    String strTRPCD;
	String strPRDDS;
	String strLOTRF;
	String strREQPK;
	String strREQQT;
	String strAUTBY;
	String strREMDS;	
	String strLEFTMRG;
	boolean flgCHKRM = false;    // variable for checking whether test remark printing part is completed
	boolean flgREMDS_EOF = false; 	// variable for checking whether Remark description printing is completed
	boolean flgISTRN = true; 	// flag for record existance in FG_ISTRN
	boolean flgCNSGN = false;
	int intREMCN = 0;  // running counter for starting position of remark description (substring)
	int intREMCN_END = 0;  // running counter for ending position of remark description (substring)
	SimpleDateFormat fmtHHMMSS = new SimpleDateFormat("hh:mm:ss");
	String strISSQT;
	String strISSPK;
	String strISSWT;
	String strISSWT_G;
	String strMNLCD;
	String strMRKFL;
	String strSTSFL;
	String strUNLSQ;
	String strREMDS_LT;
	String strPIPFL = " | ";
	
	String strBYRNM,strBYAD1,strBYAD2,strBYAD3,strBYAD4,strBYPIN;
    String strCNSNM,strCNAD1,strCNAD2,strCNAD3,strCNAD4,strCNPIN;
	
	
	//String LM_TOTRPK = "0";
	//String LM_TOTRQT = "0";
	String strALODT = "";
	String strLODDT = "";
	String strPRDTP = "";
	String strLOTNO = "";
	String strRCLNO = "";
	String strPRDCT = "";
	
	
	boolean flgAUTFL, flgEOFFL;
	public boolean LM_PPRNFL = false; // Preprinted status flag
	public JCheckBox chkPPRN;         // Checkbox for selecting preprinted status
	ButtonGroup chkPRNST;             // Button group for selecting front/back side printing
	JRadioButton rdbFRSIDE;			  // Front side printing
	JRadioButton rdbBKSIDE;			  // Back side printing
	JRadioButton rdbREPRINT;		  // Reprinting
	
    String strBALPK = "0.000";
	String strBALQT = "0.000";
	double dblTOTPK = 0.000;
	double dblTOTQT = 0.000;
	double dblTOTQT_WT = 0.000;
	double dblTOTQT_WTG = 0.000;
	double dblTOTPK_BAL = 0.000;
	double dblTOTQT_BAL = 0.000;
	//boolean flgAUTFL = false;
	int intLNHDR_ALO = 0;
	int intLNHDR_AUT = 0;
	int intLNCTR_ALO = 0;
	int intLNCTR_AUT = 0;
	int intLNCTR_CUR = 0;
	int intLNCTR = 0;
	int intRECCT = 0;

	private Vector<String> vtrUNLSQ;
	
	
	String strTRNTM_ALO = "";
	String strTRNTM_AUT = "";
	String strTRNTM_CUR = "";


	ResultSet M_rstRSSET;
	
	String LM_RESFIN = cl_dat.M_strREPSTR_pbst;
    String LM_RESSTR = LM_RESFIN.trim().concat("\\mr_tpla1.doc"); 
    String LM_RESSTR_B = LM_RESFIN.trim().concat("\\mr_tpla2.doc"); 
    String LM_RESSTR_R = LM_RESFIN.trim().concat("\\mr_tplar.doc"); 
	
	FileOutputStream O_FOUT, O_FOUT_B, O_FOUT_R;
    DataOutputStream O_DOUT, O_DOUT_B, O_DOUT_R;
	//boolean LM_1stLIN = true;
	//int LM_LINENO = 0;
	mr_tplad()
	{
		super(2);
		setMatrix(20,6);
		try
		{
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			String L_strCODCD="";
			chkPRNST = new ButtonGroup();
			vtrUNLSQ = new Vector<String>();
			
			add(new JLabel("Product Category :"),4,2,1,1,this,'L');
			add(txtPRDTP=new TxtLimit(2),4,3,1,.8,this,'L');
			
			add(new JLabel("Enter L.A No. "),5,2,1,1,this,'L');
			add(txtFRLNO=new TxtLimit(8),5,3,1,.8,this,'L');
			//add(new JLabel("L.A No. To :"),5,3,1,1,this,'L');
			//add(txtTOLNO=new TxtLimit(8),5,4,1,1,this,'L');
			add(rdbFRSIDE =new JRadioButton("Front Side",true),7,2,1,1,this,'L');
			add(rdbBKSIDE =new JRadioButton("Back Side"),7,3,1,1,this,'L');
			add(rdbREPRINT=new JRadioButton("Reprint"),7,4,1,1,this,'L');
	        add(chkPPRN   =new JCheckBox("Preprinted"),7,5,1,1,this,'L');
				
			chkPRNST.add(rdbFRSIDE);
			chkPRNST.add(rdbBKSIDE);
			chkPRNST.add(rdbREPRINT);
		
			//M_pnlRPFMT.setVisible(true);
			setENBL(false);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
	}
	
	// method for generating the reports
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC==chkPPRN)
			{
				LM_PPRNFL = false;
				if(chkPPRN.isSelected()==true)
					LM_PPRNFL = true;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	
	public void exePRINT()
	{
		try
		{
			
			setCursor(cl_dat.M_curWTSTS_pbst);
			setMSG("Fetching of data in progress... ",'N');
            strMKTTP = txtPRDTP.getText().toString().trim();
            strFRLNO = txtFRLNO.getText().toString().trim();
            strTOLNO = txtFRLNO.getText().toString().trim();
			String L_RESSTR_CUR = "";
			if(rdbFRSIDE.isSelected())
			{
				prnALLREC_F(strMKTTP, strFRLNO, strTOLNO);
				L_RESSTR_CUR = LM_RESSTR;
			}
			else if(rdbBKSIDE.isSelected())
			{
				prnALLREC_B(strMKTTP, strFRLNO, strTOLNO);
				L_RESSTR_CUR = LM_RESSTR_B;
			}
			else if(rdbREPRINT.isSelected())
			{
				prnALLREC_R(strMKTTP, strFRLNO, strTOLNO);
				L_RESSTR_CUR = LM_RESSTR_R;
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
			
			System.out.println("Report generation completed");
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
					doPRINT(L_RESSTR_CUR);
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;								
					p  = r.exec("C:\\windows\\iexplore.exe "+L_RESSTR_CUR); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				Runtime r = Runtime.getRuntime();
				Process p = null;					
				if(M_rdbHTML.isSelected())
			        p  = r.exec("C:\\windows\\iexplore.exe " + L_RESSTR_CUR); 
			     else
    			    p  = r.exec("C:\\windows\\wordpad.exe "+ L_RESSTR_CUR); 
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
				for(int i=0;i<M_cmbDESTN.getItemCount();i++)
				{					
					ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),L_RESSTR_CUR,"Payment Register"," ");
					setMSG("File Sent to " + M_cmbDESTN.getSelectedItem() + " Successfuly ",'N');				    
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Error.exePrint.. ");
		}
	}
	
	
	/**
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{
				if(M_objSOURC==txtPRDTP)
				{
					M_strHLPFLD="txtPRDTP";
					
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXMKT' ";//and CMT_CODCD in ("+cl_dat.ocl_dat.M_STRMR+")";
					
					if(txtPRDTP.getText().trim().length()>0)
					{
						M_strSQLQRY += "AND CMT_CODCD LIKE '"+txtPRDTP.getText().trim().toUpperCase()+"%'"; 
					}
					
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Type","Description"},2,"CT");
				}
			

				if(M_objSOURC==txtFRLNO)
				{
					M_strHLPFLD="txtFRLNO";
				
					M_strSQLQRY = "Select distinct ivt_ladno,ivt_laddt,pt_prtnm from mr_ivtrn,co_ptmst";
					M_strSQLQRY += " where pt_prttp='C' and pt_prtcd=ivt_byrcd and ivt_mkttp = '"+txtPRDTP.getText()+"'  ";
					if(txtFRLNO.getText().trim().length()>0)
					{
						M_strSQLQRY += "AND ivt_ladno LIKE '"+txtFRLNO.getText().trim().toUpperCase()+"%'"; 
					}
					M_strSQLQRY += " order by ivt_ladno desc ";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"L.A. No.","L.A. Date","Buyer"},3,"CT");
				}
			
				/*if(M_objSOURC==txtTOLNO)
				{
					M_strHLPFLD="txtTOLNO";
				
					M_strSQLQRY =  "Select distinct ivt_ladno,ivt_laddt,pt_prtnm from mr_ivtrn,co_ptmst";
					M_strSQLQRY += " where pt_prtcd=ivt_byrcd  and ivt_mkttp = '"+txtPRDTP.getText()+"' and pt_prttp='C' and ivt_ladno >= '"+txtFRLNO.getText().trim()+"'";
					if(txtFRLNO.getText().trim().length()>0)
					{
						M_strSQLQRY += "AND ivt_ladno LIKE '"+txtFRLNO.getText().trim().toUpperCase()+"%'"; 
					}
					M_strSQLQRY += " order by ivt_ladno desc ";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"L.A. No.","L.A. Date","Buyer"},3,"CT");
				}
				*/
			}	
			if(L_KE.getKeyCode()==L_KE.VK_ENTER)
			{
				ResultSet L_rstRSSET;	
				if(M_objSOURC == txtPRDTP)
				{	
					M_strSQLQRY = "Select CMT_CODCD from CO_CDTRN where CMT_CGMTP='MST' and";
					M_strSQLQRY += " CMT_CGSTP = 'COXXMKT' ";
					M_strSQLQRY += " and cmt_codcd='"+txtPRDTP.getText().trim()+"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET.next())
					{
						setMSG("Enter L.A No. or Press F1 for help on L.A No.",'N');
						txtFRLNO.requestFocus();
					}
					else
					{
						setMSG("InValid Product Category.",'E');
						txtPRDTP.requestFocus();
					}
					if(L_rstRSSET!=null)
						L_rstRSSET.close();
				}
				if(M_objSOURC == txtFRLNO)
				{	
					M_strSQLQRY = "Select ivt_ladno from mr_ivtrn where ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_ladno='"+txtFRLNO.getText().trim()+"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET.next())
					{
						setMSG("Enter L.A No. or Press F1 for help on L.A No.",'N');
						//txtTOLNO.setText(txtFRLNO.getText().trim());
						cl_dat.M_btnSAVE_pbst.requestFocus();
						//txtTOLNO.requestFocus();
					}
					else
					{
						setMSG("InValid L.A No.",'E');
						txtFRLNO.requestFocus();
					}
					if(L_rstRSSET!=null)
						L_rstRSSET.close();
				}
				/*if(M_objSOURC == txtTOLNO)
				{
					M_strSQLQRY = "Select ivt_ladno from mr_ivtrn where ivt_ladno='"+txtTOLNO.getText().trim()+"'";
					M_strSQLQRY += " and ivt_ladno >= '"+txtFRLNO.getText().trim()+"'";
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(L_rstRSSET.next())
					{
						setMSG("Select Printing Option.",'N');
						cl_dat.M_btnSAVE_pbst.requestFocus();
					}
					else
					{
						setMSG("InValid L.A No.",'E');
						txtTOLNO.requestFocus();
					}
					if(L_rstRSSET!=null)
						L_rstRSSET.close();
					
				}
				*/
			}
		}
		catch(Exception E)
		{
			setMSG(E,"KeyPressed");
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtPRDTP"))
			{
				txtPRDTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
			if(M_strHLPFLD.equals("txtFRLNO"))
			{
				txtFRLNO.setText(cl_dat.M_strHLPSTR_pbst);
			}
			/*if(M_strHLPFLD.equals("txtTOLNO"))
			{
				txtTOLNO.setText(cl_dat.M_strHLPSTR_pbst);
			}*/
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK");
		}
	}
	
	/** Loading Advice Front page printing
	 */
    public void prnALLREC_F(String P_strMKTTP, String P_strFRLNO, String P_strTOLNO)
	{
		try
		{
            setCursor(cl_dat.M_curWTSTS_pbst);
			strPIPFL = (LM_PPRNFL ? "   " : " | ");
			strMKTTP = P_strMKTTP;			// Added on 24.12.2002
			strFRLNO = P_strFRLNO;
			strTOLNO = P_strTOLNO;
			O_FOUT = crtFILE(LM_RESSTR);
			O_DOUT = crtDTOUTSTR(O_FOUT);
			prnFMTCHR(O_DOUT,M_strNOCPI17);
			prnFMTCHR(O_DOUT,M_strCPI10);
			prnFMTCHR(O_DOUT,M_strCPI17);
			
			strISODCA = getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPLAD01");
			strISODCB = getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPLAD02");
			strISODCC = getPRMCOD("CMT_CODDS","ISO","MRXXRPT","MR_TPLAD03");
			
			//System.out.println(strISODCA+"  "+strISODCB+"  "+strISODCC);
			flgCHKRM = false;
			flgREMDS_EOF = false;
			intREMCN = 0;
			intREMCN_END = 0;
			
			boolean L_EOF = false;
			
			M_strSQLQRY = "Select ivt_ginno,ivt_ladno,ivt_laddt ,dot_dorno,dot_dordt,isnull(dot_auttm,'00:00:00') dot_auttm,ivt_trpcd,";
            M_strSQLQRY += "ivt_saltp,ivt_byrcd,ivt_cnscd,ivt_lryno,ivt_cntds,ivt_dtpcd,ivt_tstfl,ivt_unlsq,ivt_prdds,";
            M_strSQLQRY += "ivt_prdcd,ivt_lotrf,ivt_pkgwt,ivt_pkgtp,ivt_reqqt,ivt_autby,ivt_alodt,ivt_loddt from mr_ivtrn,mr_dotrn ";
            M_strSQLQRY += " where ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_cmpcd = dot_cmpcd and ivt_mkttp=dot_mkttp and ivt_dorno=dot_dorno and ivt_prdcd=dot_prdcd and ivt_pkgtp=dot_pkgtp and ivt_ladno between '"+strFRLNO+"' and '"+strTOLNO+"' and";
            M_strSQLQRY += " ivt_mkttp='"+P_strMKTTP+"' order by ivt_ladno";
			
            //System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(!M_rstRSSET.next())
				return;
			flgEOFFL = false;
			strGINNO_OLD = "";
			while(true)
			{
				try 
				{
					strLADNO = getRSTVAL(M_rstRSSET,"IVT_LADNO","C");
                    strGINNO = getRSTVAL(M_rstRSSET,"IVT_GINNO","C");
					if(!strGINNO.equals(strGINNO_OLD))
						{strGINNO_OLD = strGINNO; setVTRUNLSQ(strGINNO);}
                    strLADDT = getRSTVAL(M_rstRSSET,"IVT_LADDT","T")+" Hrs";
                    strDORNO = getRSTVAL(M_rstRSSET,"DOT_DORNO","C");
                    strDORDT = getRSTVAL(M_rstRSSET,"DOT_DORDT","D");
                    strDORTM = getRSTVAL(M_rstRSSET,"DOT_AUTTM","T1").substring(0,5);
                    strTRPCD = getRSTVAL(M_rstRSSET,"IVT_TRPCD","C");
                    strSALTP = getRSTVAL(M_rstRSSET,"IVT_SALTP","C");
                    strBYRCD = getRSTVAL(M_rstRSSET,"IVT_BYRCD","C");
                    strCNSCD = getRSTVAL(M_rstRSSET,"IVT_CNSCD","C");
                    strLRYNO = getRSTVAL(M_rstRSSET,"IVT_LRYNO","C");
                    strCNTDS = getRSTVAL(M_rstRSSET,"IVT_CNTDS","C");
                    strDTPCD = getRSTVAL(M_rstRSSET,"IVT_DTPCD","C");
                    strTSTFL = getRSTVAL(M_rstRSSET,"IVT_TSTFL","C");
                    //strLDGSQ = getRSTVAL(M_rstRSSET,"IVT_UNLSQ","C");
                    strAUTBY = getRSTVAL(M_rstRSSET,"IVT_AUTBY","C");
                    strUNLSQ = getRSTVAL(M_rstRSSET,"IVT_UNLSQ","C");
					strALODT = getRSTVAL(M_rstRSSET,"IVT_ALODT","T");
					strLODDT = getRSTVAL(M_rstRSSET,"IVT_LODDT","T");
					strREMDS = getREMDS(strLADNO,strMKTTP,"LA");
					flgCNSGN = false;
					if(!strBYRCD.equals(strCNSCD))
						flgCNSGN = true;
					prnHEADER();
					prnLADTL(M_rstRSSET);
					if(flgEOFFL)
						break;
				} 
				catch (Exception L_EX) 
				{
                     setMSG(L_EX,"prnALLREC_F");
                } 
			}
			M_rstRSSET.close();
			prnFMTCHR(O_DOUT,M_strEJT);
			O_DOUT.close();
			O_FOUT.close();
			setMSG(" ",'N');
        }
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnALLREC_F");
		}
	}

	
	/** Loading Advice Loading sequence Setting
	 * 
	*/
	public void setVTRUNLSQ(String P_GINNO)
	{
		try
		{

			String L_strSQLQRY = "Select distinct ivt_unlsq  from mr_ivtrn  where ivt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ivt_ginno = '"+P_GINNO+"' order by IVT_UNLSQ";
            //System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
			vtrUNLSQ.clear();
			if(!L_rstRSSET.next())
				return;
			while(true)
			{
				System.out.println(getRSTVAL(M_rstRSSET,"IVT_UNLSQ","C"));
				vtrUNLSQ.add(getRSTVAL(L_rstRSSET,"IVT_UNLSQ","C"));
				if(!L_rstRSSET.next())
					break;
			}
			
        }
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnALLREC_F");
		}
	}
	
	
	
	/** Loading Advice Front page printing
	 * 
	*/
	public void prnALLREC_R(String P_strMKTTP, String P_strFRLNO, String P_strTOLNO)
	{
		try
		{
            setCursor(cl_dat.M_curWTSTS_pbst);
			strPIPFL = (LM_PPRNFL ? "   " : " | ");
			strMKTTP = P_strMKTTP;			// Added on 24.12.2002
			strFRLNO = P_strFRLNO;
			strTOLNO = P_strTOLNO;
			O_FOUT_R = crtFILE(LM_RESSTR_R);
			O_DOUT_R = crtDTOUTSTR(O_FOUT_R);	
			prnFMTCHR(O_DOUT_R,M_strNOCPI17);
			prnFMTCHR(O_DOUT_R,M_strCPI10);
			prnFMTCHR(O_DOUT_R,M_strCPI17);
			O_DOUT_R.writeBytes("\n \n \n \n \n \n");
			O_DOUT_R.writeBytes("\n \n \n \n \n");
			O_DOUT_R.writeBytes("\n \n \n \n \n");
			O_DOUT_R.writeBytes("\n \n \n \n \n");
			O_DOUT_R.writeBytes("\n\n \n \n \n\n");
			prnLADTL_R();
			prnFMTCHR(O_DOUT_R,M_strEJT);
			O_DOUT_R.close();
			O_FOUT_R.close();
			setMSG(" ",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnALLREC_R");
		}
	}
		
	/**
	 *  Printing Loading Advice gradewise detail
	*/
    public void prnLADTL_R()
	{
		try
		{
			dblTOTPK = 0.000; 
			dblTOTQT = 0.000;
			dblTOTQT_WT = 0.000;
			dblTOTQT_WTG = 0.000;
			dblTOTPK_BAL = 0.000;
			dblTOTQT_BAL = 0.000;
			M_strSQLQRY = "Select IVT_PRDDS,IST_PRDCD,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_ISSPK,IST_ISSQT,IST_PKGTP,IST_STSFL,";
			M_strSQLQRY += "IST_WRHTP,IST_PRDTP, isnull(ST_STKQT,0)-isnull(ST_ALOQT,0) ST_STKQT, IVT_UNLSQ, IVT_ALODT, IVT_LODDT, LT_REMDS ";
			M_strSQLQRY += " from FG_ISTRN,MR_IVTRN,FG_STMST,PR_LTMST ";
			M_strSQLQRY += " where  ";
			M_strSQLQRY += " ist_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ist_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ist_cmpcd = st_cmpcd and IST_WRHTP=ST_WRHTP AND IST_LOTNO=ST_LOTNO AND IST_RCLNO = ST_RCLNO AND IST_MNLCD=ST_MNLCD AND IST_PKGTP=ST_PKGTP AND  IST_MKTTP = '"+strMKTTP+"' and  IST_ISSNO between '"+strFRLNO+"' and '"+strTOLNO+"' ";
			M_strSQLQRY += " and IST_STSFL not in 'X' and  ist_cmpcd=ivt_cmpcd and IST_ISSNO=IVT_LADNO and  IST_PRDCD=IVT_PRDCD and IST_PKGTP = IVT_PKGTP and ist_cmpcd=lt_cmpcd and IST_PRDTP=LT_PRDTP and IST_LOTNO = LT_LOTNO and IST_RCLNO = LT_RCLNO  order by IST_PRDCD,IST_LOTNO,IST_RCLNO";
			System.out.println(M_strSQLQRY);
			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET == null || !M_rstRSSET.next())
				return;
			flgAUTFL = false;
			intLNCTR_ALO=9;// it specify no. of row in Quality Alloted table
			intLNCTR_AUT=8;// it specify no. of row in Quality loaded table
			intLNHDR_ALO=4;// No. of rows in Allocation header
			intLNHDR_AUT=4;// No. of rows in Authorisation header
			strALODT = getRSTVAL(M_rstRSSET,"IVT_ALODT","T");
			strLODDT = getRSTVAL(M_rstRSSET,"IVT_LODDT","T");
					
			strTRNTM_ALO = (strALODT.length()>7 ? strALODT.substring(11,16)+" Hrs" : "");
			strTRNTM_AUT = (strLODDT.length()>7 ? strLODDT.substring(11,16)+" Hrs" : "");
			//strTRNTM_ALO =  "         ";
			//strTRNTM_AUT =  "         ";
			intLNCTR_CUR = intLNCTR_ALO;
			strTRNTM_CUR = strTRNTM_ALO;
			intLNCTR = 0;
			strSTSFL = getRSTVAL(M_rstRSSET,"IST_STSFL","C");
			exeSKIP_LN_R(intLNHDR_ALO);
			if(strSTSFL.equals("2"))
			{
				flgAUTFL=true;
				exeSKIP_LN_R(intLNCTR_ALO);
				exeSKIP_LN_R(2); 
				intLNCTR_CUR = intLNCTR_AUT;
				strTRNTM_CUR = strTRNTM_AUT;
				exeSKIP_LN_R(intLNHDR_AUT);
			}
			while(true)
			{
				strPRDCD = getRSTVAL(M_rstRSSET,"IST_PRDCD","C");
				strPKGTP = getRSTVAL(M_rstRSSET,"IST_PKGTP","C");
				strPRDDS = getRSTVAL(M_rstRSSET,"IVT_PRDDS","C");
				strPRDTP = getRSTVAL(M_rstRSSET,"IST_PRDTP","C");
				strLOTNO = getRSTVAL(M_rstRSSET,"IST_LOTNO","C");
				strRCLNO = getRSTVAL(M_rstRSSET,"IST_RCLNO","C");
				strMRKFL = (getDOLAR() ? " " : "$");
				strMNLCD = getRSTVAL(M_rstRSSET,"IST_MNLCD","C");
				strISSQT = getRSTVAL(M_rstRSSET,"IST_ISSQT","N");
				strPKGTP = getRSTVAL(M_rstRSSET,"IST_PKGTP","C");
				strUNLSQ = getRSTVAL(M_rstRSSET,"IVT_UNLSQ","C");
				strREMDS_LT = getRSTVAL(M_rstRSSET,"LT_REMDS","C");
				strBALQT = getRSTVAL(M_rstRSSET,"ST_STKQT","N");
				strPKGWT = getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",strPKGTP);
				strBALPK = calREQPK(strBALQT,strPKGWT);
				strISSPK = calREQPK(strISSQT,strPKGWT);
				prnISS_REC_R();
				if(!M_rstRSSET.next())
					break;
			}
			exeSKIP_LN_R(intLNCTR_CUR);
			prnISS_TOT_R();
			if(!flgAUTFL)
			{
				exeSKIP_LN_R(intLNHDR_AUT); 
				exeSKIP_LN_R(intLNCTR_AUT); 
				exeSKIP_LN_R(3);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnLADTL_R");
		}
	}

/**
 *  Total printing after Issue details (Reprint Case)
*/
	private void prnISS_TOT_R()
	{
		try
		{
			if(flgAUTFL)
			{
				O_DOUT_R.writeBytes(padSTR1('R',"Loading Seq.",18,"171",'P'));
				String L_strUNLSQ = "";
				for(int i=0; i<vtrUNLSQ.size(); i++)
				{
					L_strUNLSQ += " "+ (vtrUNLSQ.get(i).toString().equals(strUNLSQ) ? " [" : "")+ vtrUNLSQ.get(i).toString()+ (vtrUNLSQ.get(i).toString().equals(strUNLSQ) ? "] " : "")+ " ";
				}
				
				O_DOUT_R.writeBytes(padSTR1('R',L_strUNLSQ,55,"171",'A')+"\n");
			}
			O_DOUT_R.writeBytes(padSTR1('R',crtLINE1(65,"- "),130,"171",'P')+"\n");
			O_DOUT_R.writeBytes(padSTR1('R',"Total" ,17,"171",'P'));
			O_DOUT_R.writeBytes(padSTR1('R',strTRNTM_CUR,15,"171",'A'));
			O_DOUT_R.writeBytes(padSTR1('L',"     " ,12,"171",'P'));
			if(flgISTRN)
			{
				O_DOUT_R.writeBytes(padSTR1('L',setNumberFormat(dblTOTPK,0),9,"171",'A'));
				O_DOUT_R.writeBytes(padSTR1('L',setNumberFormat(dblTOTQT,3),13,"171",'A'));
				if(!flgAUTFL)
				{
					O_DOUT_R.writeBytes(padSTR1('L',setNumberFormat(dblTOTPK_BAL,0),23,"171",'A'));
					O_DOUT_R.writeBytes(padSTR1('L',setNumberFormat(dblTOTQT_BAL,3),12,"171",'A'));
				}
				//else
				//	O_DOUT_R.writeBytes(padSTR1('L',(dblTOTQT_WT>0 ? setNumberFormat(dblTOTQT_WT,3) : ""),12,"171",'A'));
			}
			O_DOUT_R.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnISS_TOT_R");
		}
	}
	/** Printing issue record (Re-printing)
	 * 
	*/
	private void prnISS_REC_R()
	{
		try
		{
			O_DOUT_R.writeBytes(padSTR1('R',strPRDDS ,12,"171",'A'));
			O_DOUT_R.writeBytes(padSTR1('R',strLOTNO ,12,"171",'A'));
			O_DOUT_R.writeBytes(padSTR1('R',strMRKFL ,12,"171",'A'));
			O_DOUT_R.writeBytes(padSTR1('R',strMNLCD ,8,"171",'A'));
			O_DOUT_R.writeBytes(padSTR1('L',strISSPK ,9,"171",'A'));
			O_DOUT_R.writeBytes(padSTR1('L',strISSQT ,13,"171",'A'));
			if(!flgAUTFL)
			{
				O_DOUT_R.writeBytes(padSTR1('L',strBALPK ,23,"171",'A'));
				O_DOUT_R.writeBytes(padSTR1('L',strBALQT ,12,"171",'A'));
			}
			O_DOUT_R.writeBytes("   "+padSTR1('L',strREMDS_LT ,30,"171",'A'));
			O_DOUT_R.writeBytes("\n");
			dblTOTPK += Double.parseDouble(strISSPK);
			dblTOTQT += Double.parseDouble(strISSQT);
			//dblTOTQT_WT += Double.parseDouble(strISSWT);
			dblTOTPK_BAL += Double.parseDouble(strBALPK);
			dblTOTQT_BAL += Double.parseDouble(strBALQT);
			intLNCTR +=1;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnISS_REC_R");
		}
	}
		
	/** Loading Advice Back page printing
	 */
    public void prnALLREC_B(String P_strMKTTP, String P_strFRLNO, String P_strTOLNO)
	{
		try
		{
			setMSG(" ",'N');
			strLEFTMRG = "          ";
			O_FOUT_B = crtFILE(LM_RESSTR_B);
			O_DOUT_B = crtDTOUTSTR(O_FOUT_B);	
			
			prnFMTCHR(O_DOUT_B,M_strNOCPI17);
			prnFMTCHR(O_DOUT_B,M_strCPI10);
			prnFMTCHR(O_DOUT_B,M_strCPI17);				
			//prnFMTCHR(O_DOUT_B,M_strBOLD);
			prnFMTCHR(O_DOUT_B,M_strNOENH);
			
			//System.out.println("000");
			O_DOUT_B.writeBytes("\n");
			O_DOUT_B.writeBytes("\n");
			prnFMTCHR(O_DOUT_B,M_strENH);// for enhance the size of the character
			O_DOUT_B.writeBytes(padSTR1('C',"SUPREME PETROCHEM LTD",60,"200",'A')+"\n\n\n");
			prnFMTCHR(O_DOUT_B,M_strNOENH); prnFMTCHR(O_DOUT_B,M_strNOCPI17); 	prnFMTCHR(O_DOUT_B,M_strCPI10); 	prnFMTCHR(O_DOUT_B,M_strCPI17);		O_DOUT_B.writeBytes(strLEFTMRG); prnFMTCHR(O_DOUT_B,M_strENH);

			O_DOUT_B.writeBytes(padSTR1('R',"CHECKLIST OF VEHICLE",130,"50",'P')+"\n");
			prnFMTCHR(O_DOUT_B,M_strNOENH);// for closing the enhance.
			O_DOUT_B.writeBytes(strLEFTMRG+crtLINE1(130,"-")+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"                            Requirements                                                                          <---Status--->"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"                                                                                                                  OK      NOT OK"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+crtLINE1(130,"-")+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       1) Vehicle Type : AC/Closed vehicle/With/Without Hood/cartainer                                          [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       2) Flooring                                                                 :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       3) Protruded bolts/parts/damaged body portion inside vehicle                :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       4) Side walls/floor verified for splinters/rust/contamination/cleanliness   :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       5) Free of odor/smell                                                                                    [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       6) Tarpauline Covering :"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"          1) Availability                                                          :     2  /   1               [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"          2) Quality                                                               :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"          3) Placement after loading                                               :     Properly Tied          [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"                      a)On Material                                                :     Y / N                  [ Yes ]   [ No ]"+"\n");  
			O_DOUT_B.writeBytes(strLEFTMRG+"                      b)On Vehicle Hood                                            :     Y / N                  [ Yes ]   [ No ]"+"\n");  
			O_DOUT_B.writeBytes(strLEFTMRG+"       7) Others   :                                                                                            [ Yes ]   [ No ]"+"\n");  
			
			//System.out.println("001");
			O_DOUT_B.writeBytes(strLEFTMRG+crtLINE1(130,"-")+"\n\n");
			prnFMTCHR(O_DOUT_B,M_strENH);
			prnFMTCHR(O_DOUT_B,M_strNOENH); prnFMTCHR(O_DOUT_B,M_strNOCPI17); 	prnFMTCHR(O_DOUT_B,M_strCPI10); 	prnFMTCHR(O_DOUT_B,M_strCPI17);		O_DOUT_B.writeBytes(strLEFTMRG); prnFMTCHR(O_DOUT_B,M_strENH);
			O_DOUT_B.writeBytes(padSTR1('R',"CHECKLIST OF CONTAINER",130,"50",'P')+"\n");
			prnFMTCHR(O_DOUT_B,M_strNOENH);
			O_DOUT_B.writeBytes(strLEFTMRG+crtLINE1(130,"-")+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"                            Requirements                                                                          <---Status--->"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"                                                                                                                  OK      NOT OK"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+crtLINE1(130,"-")+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       1) Door Rubber Gasket                                                       :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       2) Flooring                                                                 :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       3) Protruded bolts/parts/damaged body portion inside vehicle                :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       4) Side walls/floor verified for splinters/burrs/stains/rust/               :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"          contamination/cleanliness"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       5) Free of odor/smell                                                       :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       6) Holes/Cuts from inside                                                   :     Present / Absent       [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       7) No. of patches & approx size,if any-                                                                  [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       8) Door Dual Locking Bars/cams lock/door handle/latches functioning         :     Good / Bad             [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       9) Air Vent Condition                                                       :     Good / Bad             [ Yes ]   [ No ]"+"\n"); 
			O_DOUT_B.writeBytes(strLEFTMRG+"      10) Covering on Floor   -   Availability                                     :      1                     [ Yes ]   [ No ]"+"\n"); 
			O_DOUT_B.writeBytes(strLEFTMRG+"                              -   Quality                                          :     Good / Bad             [ Yes ]   [ No ]"+"\n"); 
			O_DOUT_B.writeBytes(strLEFTMRG+"      11) Others   :                                                                                            [ Yes ]   [ No ]"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+crtLINE1(130,"-")+"\n\n"); 
			prnFMTCHR(O_DOUT_B,M_strENH);
			prnFMTCHR(O_DOUT_B,M_strNOENH); prnFMTCHR(O_DOUT_B,M_strNOCPI17); 	prnFMTCHR(O_DOUT_B,M_strCPI10); 	prnFMTCHR(O_DOUT_B,M_strCPI17);		O_DOUT_B.writeBytes(strLEFTMRG); prnFMTCHR(O_DOUT_B,M_strENH);
			O_DOUT_B.writeBytes(padSTR1('R',"Vehicle Accepted/Rejected for loading",130,"50",'P')+"\n\n");
			prnFMTCHR(O_DOUT_B,M_strNOENH);
			O_DOUT_B.writeBytes(strLEFTMRG+"        Reason For Rejection:"+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        1)	Insufficient / Damaged / Contaminated /Wet Tarpaulin."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        2)	Hood not okay."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        3)	Side Walls /floor found with splinters/burrs/stains/rust/contamination/uncleaned from inside."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        4)	Floor damaged."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        5)	Wet/moisture/odor/smell from inside."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        6)	Leaked Hood / Container."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        7)	Door gasket not okay."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        8)	Hole/cut on container body."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"        9)	Door dual locking bars/cams lock/door handle/latches not functioning."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       10)	Protruded bolts/parts/damaged body ortion inside vehicle."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       11)	Air Vent not okay."+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"       12)	Others."+"\n");
			//O_DOUT_B.writeBytes("\n");
			//System.out.println("003");
			O_DOUT_B.writeBytes(strLEFTMRG+"                                                                                                                  ____________ "+"\n");
			O_DOUT_B.writeBytes(strLEFTMRG+"                                                                                                                  Verified by  "+"\n");
			//System.out.println("004");
						
			prnFMTCHR(O_DOUT_B,M_strEJT);
			O_DOUT_B.close();
			O_FOUT_B.close();
			setMSG(" ",'N');
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnALLREC_B");
		}
	}
		
	/** Capturing Gate-In detail for specified L.A.Number
	 * 
	*/
    private void getGINDTL(String LP_LADNO) 
	{
		strGINDT = "";
        String L_strSQLQRY;
        ResultSet L_rstRSSET;
        try 
		{
            L_strSQLQRY = "Select wb_gindt from mm_wbtrn where wb_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and wb_doctp||wb_ordrf = '"+"03"+LP_LADNO+"'";
            //System.out.println(L_strSQLQRY);
            L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
            if(!L_rstRSSET.next() || L_rstRSSET==null)
              return;
            strGINDT = getRSTVAL(L_rstRSSET,"WB_GINDT","T")+" Hrs";
            //strGINDT = getRSTVAL(L_RSLSET,"WB_GINDT","T").substring(0,10);
            //System.out.println("strGINDT 1 : "+strGINDT);
		}
        catch(Exception L_EX)
		{
              setMSG(L_EX,"getGINDTL");
        }
	}



	/** Calculating Number of packages for Specified Pkg.Type & Quantity
	*/
    public String calREQPK(String LP_REQQT,String LP_PKGWT)
	{ 
		String L_strRETRN = "";
		try
		{
			double L_PKGWT = Double.parseDouble(LP_PKGWT);
			if(L_PKGWT == 0)
				L_PKGWT = 0.001;
			double L_REQQT = Double.parseDouble(LP_REQQT);
			double L_REQPK = L_REQQT/L_PKGWT;
			long L_CALPK = Math.round(L_REQPK);
			L_strRETRN = setNumberFormat(L_CALPK,0).toString();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calREQPK");
		}
		return L_strRETRN;
	}
	
	public DataOutputStream crtDTOUTSTR(FileOutputStream outfile)
	{
		DataOutputStream outSTRM = new DataOutputStream(outfile);
		return outSTRM;
	}
	public FileOutputStream crtFILE(String strFILE)
	{
		FileOutputStream outFILE = null;
		try
		{
			File file = new File(strFILE);
			outFILE = new FileOutputStream(file);
        	return outFILE;
		}
		catch(Exception L_IO)
		{
			setMSG(L_IO," FileOutputStream..");
			return outFILE;		
		}
	}
	
    public void prnHEADER()
	{
		try
		{
			O_DOUT.writeBytes("\n");
            prnFMTCHR(O_DOUT,M_strBOLD);
			O_DOUT.writeBytes(crtLINE1(30," "));
			prnFMTCHR(O_DOUT,M_strENH);// used for enhance the size of character
			O_DOUT.writeBytes(padSTR1('C',"SUPREME PETROCHEM LTD",60,"100",'P'));
			prnFMTCHR(O_DOUT,M_strNOENH);// used for closing the enhance 
			O_DOUT.writeBytes(padSTR1('L',crtLINE1(32,"-"),30,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(100," ")+padSTR1('L',strISODCA,30,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(30," "));
			prnFMTCHR(O_DOUT,M_strENH);
			O_DOUT.writeBytes(padSTR1('C',"LOADING ADVICE",60,"100",'P'));
			prnFMTCHR(O_DOUT,M_strNOENH);
			O_DOUT.writeBytes(padSTR1('L',strISODCB,32,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(100," ")+padSTR1('L',strISODCC,30,"171",'P')+"\n");
			O_DOUT.writeBytes(padSTR1('R',crtLINE1(130,"-"),130,"171",'P')+"\n");
			prnFMTCHR(O_DOUT,M_strNOBOLD);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}	
	
		/** Printing Loading Advice gradewise detail
		 */
    public void prnLADTL(ResultSet LP_RSLSET)
	{
		try
		{
			ResultSet L_rstRSSET;
		   
            getGINDTL(strLADNO);
			getBYRDTL();
            strTRPNM ="";
			//cl_dat.ocl_dat.getPRMPRT("PT_PRTNM","T",strTRPCD); 
			M_strSQLQRY = "Select PT_PRTNM  from CO_PTMST where PT_PRTTP = 'T' and PT_PRTCD = " + "'" + strTRPCD + "'";
			//System.out.println("M_strSQLQRY = "+M_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
			{
				strTRPNM = L_rstRSSET.getString("PT_PRTNM");
				//L_rstRSSET.close();
			}
			strSALDS = getPRMCOD("CMT_CODDS","SYS","MR00SAL",strSALTP);
			strDTPDS = getPRMCOD("CMT_CODDS","SYS","MRXXDTP",strDTPCD);

			//if(strTRPNM.length() >= 35)
            //   strTRPNM = strTRPNM.substring(0,34).trim();
			O_DOUT.writeBytes(padSTR1('R',"Security No & Date " ,33,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strGINNO + " " +strGINDT,40,"171",'A')+strPIPFL);
			O_DOUT.writeBytes(padSTR1('R',"Buyer ",8,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strBYRNM+" ("+strBYRCD+")",50,"171",'A')+"\n");

			O_DOUT.writeBytes(padSTR1('R',"L.Adv. No. & Date " ,33,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strLADNO + " " +strLADDT,40,"171",'A')+strPIPFL);
			O_DOUT.writeBytes(padSTR1('R',"   ",8,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strBYAD1,50,"171",'A')+"\n");

			O_DOUT.writeBytes(padSTR1('R',"DO No. & Date " ,33,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strDORNO + " " +strDORDT+ " " +strDORTM+" Hrs",40,"171",'A')+strPIPFL);
			O_DOUT.writeBytes(padSTR1('R',"   ",8,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strBYAD2,50,"171",'A')+"\n");

					
			O_DOUT.writeBytes(padSTR1('R',"Sale Type & Del.Type " ,33,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strSALDS + " " +strDTPDS,40,"171",'A')+strPIPFL);
			O_DOUT.writeBytes(padSTR1('R',"   ",8,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strBYAD3,50,"171",'A')+"\n");
			O_DOUT.writeBytes(padSTR1('R',"Transporter " ,33,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strTRPNM+ " ("+strTRPCD+")",40,"171",'A')+strPIPFL);
			O_DOUT.writeBytes(padSTR1('R',"Veh.No. ",11,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strLRYNO,15,"171",'A'));
			O_DOUT.writeBytes(padSTR1('R',"Container No. ",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strCNTDS,15,"171",'A')+"\n");
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
				
			O_DOUT.writeBytes(padSTR1('C',"ADVISED TO LOAD" ,73,"171",'P'));
			O_DOUT.writeBytes(padSTR1('C',"S P E C I A L  I N S T R U C T I O N S" ,63,"171",'P')+"\n");

			O_DOUT.writeBytes(padSTR1('R',"Prod." ,15,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Grade" ,12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Lot No." ,7,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"Pkgs" ,6,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"Pkg.Type" ,12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"Qty" ,15,"171",'P'));
			O_DOUT.writeBytes(padSTR1('C',"" ,63,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
			intRECCT = 1;	
			dblTOTQT = 0.000;  
			dblTOTQT_WT = 0.000;  
			dblTOTQT_WTG = 0.000;  
			dblTOTPK = 0.000;
			while(true)
			{
				strPRDDS = getRSTVAL(LP_RSLSET,"IVT_PRDDS","C");
				strPRDCD = getRSTVAL(LP_RSLSET,"IVT_PRDCD","C");
				strLOTRF = getRSTVAL(LP_RSLSET,"IVT_LOTRF","C");
				strPKGWT = getRSTVAL(LP_RSLSET,"IVT_PKGWT","C");
				strPKGTP = getRSTVAL(LP_RSLSET,"IVT_PKGTP","C");
				strREQQT = getRSTVAL(LP_RSLSET,"IVT_REQQT","C");
				strPKGDS = getPRMCOD("CMT_SHRDS","SYS","FGXXPKG",strPKGTP);
				strREQPK = calREQPK(strREQQT,strPKGWT);
				strPRDCT = "";
				// here it compare the product name with its code no.
				if(strPRDCD.substring(0,4).equals("5111"))
					strPRDCT = "GPPS";
				else if(strPRDCD.substring(0,4).equals("5112"))
					strPRDCT = "HIPS";
				else if(strPRDCD.substring(0,4).equals("5211"))
					strPRDCT = "Coloured GPPS";
				else if(strPRDCD.substring(0,4).equals("5212"))
					strPRDCT = "Coloured HIPS";
				else if(strPRDCD.substring(0,2).equals("54"))
					strPRDCT = "Master Batch";
				O_DOUT.writeBytes(padSTR1('R',strPRDCT ,15,"171",'A'));// A for actual
				O_DOUT.writeBytes(padSTR1('R',strPRDDS ,12,"171",'A'));
				O_DOUT.writeBytes(padSTR1('R',strLOTRF ,7,"171",'A'));
				O_DOUT.writeBytes(padSTR1('L',strREQPK ,6,"171",'A')+"      ");
				O_DOUT.writeBytes(padSTR1('R',strPKGDS,12,"171",'A'));
				O_DOUT.writeBytes(padSTR1('L',strREQQT ,13,"171",'A')+"  "+strPIPFL);
				O_DOUT.writeBytes(padSTR1('R',getSPLINS(intRECCT),63,"171",'A')+"\n");
				dblTOTQT += Double.parseDouble(strREQQT);
				dblTOTPK += Double.parseDouble(strREQPK);
				intRECCT +=1 ;
				if(!LP_RSLSET.next())
				{
					flgEOFFL = true; 
					for(int i=intRECCT;i<8;i++)
						O_DOUT.writeBytes(crtLINE1(73," ")+strPIPFL+padSTR1('R',getSPLINS(i),63,"171",'A')+"\n");
					break;
				}
			}
					
			O_DOUT.writeBytes(padSTR1('R',"Loading Seq.",18,"171",'P'));
			String L_strUNLSQ = "";
			for(int i=0; i<vtrUNLSQ.size(); i++)
			{
				L_strUNLSQ += " "+ (vtrUNLSQ.get(i).toString().equals(strUNLSQ) ? " [" : "")+ vtrUNLSQ.get(i).toString()+ (vtrUNLSQ.get(i).toString().equals(strUNLSQ) ? "] " : "")+ " ";
			}
				
			O_DOUT.writeBytes(padSTR1('R',L_strUNLSQ,55,"171",'A')+strPIPFL+"        "+padSTR1('R',"|        |        |        |        |        ",63,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(73,"-")+strPIPFL+padSTR1('R',"--------|--------|--------|--------|--------|--------",63,"171",'P')+"\n");
			O_DOUT.writeBytes(padSTR1('R',"Total ",25,"171",'P'));
			//System.out.println("dblTOTPK = "+dblTOTPK);
			O_DOUT.writeBytes(padSTR1('L',setNumberFormat(dblTOTPK,0),15,"171",'A'));
			O_DOUT.writeBytes(padSTR1('L',setNumberFormat(dblTOTQT,3),27,"171",'A')+crtLINE1(6," ")+strPIPFL+"        "+padSTR1('R',"|        |        |        |        |        ",63,"171",'P'));
			O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
			O_DOUT.writeBytes(padSTR1('R',"Auth.Signatory (Comm)",35,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strAUTBY,38,"171",'A')+strPIPFL+padSTR1('R',"Bag brstd: Rpk/Rplcd & Bal. A/Ldg",40,"171",'P')+strPIPFL+"\n");
						
			dblTOTPK = 0.000;
			dblTOTQT=0.000;
			dblTOTQT_WT=0.000;
			dblTOTQT_WTG=0.000;
			dblTOTPK_BAL = 0.000; dblTOTQT_BAL = 0.000;
			M_strSQLQRY = "Select IVT_PRDDS,IST_PRDCD,IST_PRDTP,IST_LOTNO,IST_RCLNO,IST_MNLCD,IST_ISSPK,IST_ISSQT,IST_PKGTP,IST_STSFL,";
			M_strSQLQRY += "IST_WRHTP,IST_PRDTP, isnull(ST_STKQT,0)-isnull(ST_ALOQT,0) ST_STKQT, LT_REMDS ";
			M_strSQLQRY += " from FG_ISTRN,MR_IVTRN,FG_STMST,PR_LTMST ";
			M_strSQLQRY += " where  ";
			M_strSQLQRY += " ist_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and ist_cmpcd=st_cmpcd and IST_WRHTP=ST_WRHTP AND IST_LOTNO=ST_LOTNO AND IST_RCLNO = ST_RCLNO AND IST_MNLCD=ST_MNLCD AND IST_PKGTP=ST_PKGTP AND  IST_MKTTP = '"+strMKTTP+"' and  IST_ISSNO between '"+strFRLNO+"' and '"+strTOLNO+"' ";
			M_strSQLQRY += " and IST_STSFL not in 'X' and  ist_cmpcd = ivt_cmpcd and IST_ISSNO=IVT_LADNO and  IST_PRDCD=IVT_PRDCD and IST_PKGTP = IVT_PKGTP and ist_cmpcd = lt_cmpcd and IST_PRDTP=LT_PRDTP and IST_LOTNO = LT_LOTNO and IST_RCLNO = LT_RCLNO  order by IST_PRDCD,IST_LOTNO,IST_RCLNO";
			//M_strSQLQRY += " order by IST_PRDCD,IST_LOTNO,IST_RCLNO";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				
			flgISTRN = true;
			if(M_rstRSSET == null || !M_rstRSSET.next())
				flgISTRN = false;
			prnHDR_ALO();
			flgAUTFL = false;
			intLNCTR_ALO=9;// it specify no. of row in Quality Alloted table
			intLNCTR_AUT=8;// it specify no. of row in Quality loaded table
			strTRNTM_ALO = (strALODT.length()>7 ? strALODT.substring(11,16)+" Hrs" : "");
			strTRNTM_AUT = (strLODDT.length()>7 ? strLODDT.substring(11,16)+" Hrs" : "");
			//strTRNTM_ALO =  "         ";
			//strTRNTM_AUT =  "         ";
									
			//System.out.println("strTRNTM_AUT "+strTRNTM_AUT);
			//System.out.println("strTRNTM_ALO "+strTRNTM_ALO);
			intLNCTR_CUR = intLNCTR_ALO;
			strTRNTM_CUR = strTRNTM_ALO;

			intLNCTR = 0;
						
			/** !strSTSFL used for printing Quentity Alloated & 
			 *   strSTSFL used for printing Quentity Loaded table.
			*/
			//if(!strSTSFL.equals("2"))
			if(flgISTRN)
			{
				strSTSFL = getRSTVAL(M_rstRSSET,"IST_STSFL","C");
				if(strSTSFL.equals("2"))
				{
					flgAUTFL=true;
					exeSKIP_LN(intLNCTR_ALO);
					exeSKIP_LN(2); 
					intLNCTR_CUR = intLNCTR_AUT; 
					strTRNTM_CUR = strTRNTM_AUT; 
					prnHDR_AUT();
				}
				while(true)
				{
					strPRDCD = getRSTVAL(M_rstRSSET,"IST_PRDCD","C");
					strPKGTP = getRSTVAL(M_rstRSSET,"IST_PKGTP","C");
					strPRDDS = getRSTVAL(M_rstRSSET,"IVT_PRDDS","C");
					strPRDTP = getRSTVAL(M_rstRSSET,"IST_PRDTP","C");
					strLOTNO = getRSTVAL(M_rstRSSET,"IST_LOTNO","C");
					strRCLNO = getRSTVAL(M_rstRSSET,"IST_RCLNO","C");
					strMRKFL = (getDOLAR() ? " " : "$");
					strMNLCD = getRSTVAL(M_rstRSSET,"IST_MNLCD","C");
					strISSQT = getRSTVAL(M_rstRSSET,"IST_ISSQT","N");
					strPKGTP = getRSTVAL(M_rstRSSET,"IST_PKGTP","C");
					strREMDS_LT = getRSTVAL(M_rstRSSET,"LT_REMDS","C");
					strBALQT = getRSTVAL(M_rstRSSET,"ST_STKQT","N");
					strPKGWT = getPRMCOD("CMT_NCSVL","SYS","FGXXPKG",strPKGTP);
					//System.out.println("strBALQT : "+strBALQT);
					strBALPK = calREQPK(strBALQT,strPKGWT);
					strISSPK = calREQPK(strISSQT,strPKGWT);
					strISSWT = txtPRDTP.getText().equals("07") ? getISSWT(strLOTNO,strRCLNO,strISSPK) : "0.000";
					strISSWT_G = txtPRDTP.getText().equals("07") ? getISSWT_G(strPKGTP,strISSPK) : "0.000";
					prnISS_REC();
					if(!M_rstRSSET.next())
						break;
				}
			}
			exeSKIP_LN(intLNCTR_CUR);
			prnISS_TOT();
			//O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
			if(!flgAUTFL)
			{
				prnHDR_AUT();
				exeSKIP_LN(intLNCTR_AUT);
				exeSKIP_LN(3);
			}
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
							
			O_DOUT.writeBytes(padSTR1('R',"*In case classified grade mismatches with provisional grade:Sup.By",68,"171",'P')+strPIPFL+padSTR1('L',"Verified by (Officer)",40,"171",'P')+strPIPFL+"\n");
			//O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
			O_DOUT.writeBytes(padSTR1('R',"Received goods in Good & Dry Condition",130,"171",'P')+"\n");
			O_DOUT.writeBytes("\n\n");
			O_DOUT.writeBytes(padSTR1('R',"Customer/Vehicle Driver",35,"171",'P')+padSTR1('R',"    ",35,"171",'P')+padSTR1('R',"Supervised by:",35,"171",'P')+padSTR1('R',"Authorised Signatory(MHD)",40,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
			if(flgCNSGN)
				O_DOUT.writeBytes("Cnsignee : "+strCNSNM+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnLADDTL");
		}
	}
		
		
/**
 *  Total printing after Issue details
*/
	private void prnISS_TOT()
	{
		try
		{
			if(flgAUTFL)
			{
				O_DOUT.writeBytes(padSTR1('R',"Loading Seq.",18,"171",'P'));
				String L_strUNLSQ = "";
				for(int i=0; i<vtrUNLSQ.size(); i++)
				{
					L_strUNLSQ += " "+ (vtrUNLSQ.get(i).toString().equals(strUNLSQ) ? " [" : "")+ vtrUNLSQ.get(i).toString()+ (vtrUNLSQ.get(i).toString().equals(strUNLSQ) ? "] " : "")+ " ";
				}
				O_DOUT.writeBytes(padSTR1('R',L_strUNLSQ,55,"171",'A')+"\n");
			}
			if(!flgISTRN)
			{
				O_DOUT.writeBytes("\n\n");
				return;
			}
			O_DOUT.writeBytes(padSTR1('R',crtLINE1(65,"- "),130,"171",'P')+"\n");
			O_DOUT.writeBytes(padSTR1('R',"Time" ,17,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',strTRNTM_CUR,15,"171",'A'));
			O_DOUT.writeBytes(padSTR1('L',"Total" ,12,"171",'P'));
			if(flgISTRN)
			{
				O_DOUT.writeBytes(padSTR1('L',setNumberFormat(dblTOTPK,0),9,"171",'A'));
				O_DOUT.writeBytes(padSTR1('L',setNumberFormat(dblTOTQT,3),13,"171",'A'));
				if(!flgAUTFL)
				{
					O_DOUT.writeBytes(padSTR1('L',setNumberFormat(dblTOTPK_BAL,0),23,"171",'A'));
					O_DOUT.writeBytes(padSTR1('L',setNumberFormat(dblTOTQT_BAL,3),12,"171",'A'));
				}
				else
				{
					O_DOUT.writeBytes(padSTR1('L',dblTOTQT_WT>0 ? setNumberFormat(dblTOTQT_WT,3) : "",12,"171",'A'));
					O_DOUT.writeBytes(padSTR1('L',dblTOTQT_WTG>0 ? setNumberFormat(dblTOTQT_WTG,3) : "",12,"171",'A'));
				}
				
				
			}
			O_DOUT.writeBytes("\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnISS_TOT");
		}
	}
		
		

	private String getISSWT(String LP_LOTNO, String LP_RCLNO, String LP_ISSPK)
	{
		String L_strISSWT = "0.000";
		String L_strPKGWT = "0.000";
		try
		{
			M_strSQLQRY = "Select isnull(LT_PKGWT,0) LT_PKGWT from PR_LTMST where LT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and LT_LOTNO = '"+LP_LOTNO+"' and LT_RCLNO = '"+LP_RCLNO+"'";
			System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				
			if(L_rstRSSET != null && L_rstRSSET.next())
				L_strPKGWT = L_rstRSSET.getString("LT_PKGWT");
			L_rstRSSET.close();
			L_strISSWT = setNumberFormat((Double.parseDouble(L_strPKGWT)*Double.parseDouble(LP_ISSPK))/1000,3);
			System.out.println("L_strPKGWT : "+L_strPKGWT);
			System.out.println("L_strISSWT : "+L_strISSWT);
			System.out.println("LP_ISSPK : "+LP_ISSPK);
		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getISSWT");
		}
		return L_strISSWT;
	}
		

	private String getISSWT_G(String LP_PKGTP, String LP_ISSPK)
	{
		String L_strISSWT_G = "0.000";
		String L_strTARWT = "0.000";
		String L_strPCSNO = "0.000";
		try
		{
			M_strSQLQRY = "Select CMT_CHP01,cmt_modls from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'FGXXPKG' and CMT_CODCD = '"+LP_PKGTP+"'";
			System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
				
			if(L_rstRSSET != null && L_rstRSSET.next())
			{
				L_strTARWT = L_rstRSSET.getString("CMT_CHP01");
				L_strPCSNO = L_rstRSSET.getString("CMT_MODLS");
			}
			L_rstRSSET.close();
			L_strISSWT_G = setNumberFormat((Double.parseDouble(L_strTARWT)*(Double.parseDouble(LP_ISSPK)/Double.parseDouble(L_strPCSNO)))/1000,3);
			System.out.println("L_strTARWT : "+L_strTARWT);
			System.out.println("L_strPCSNO : "+L_strPCSNO);
			System.out.println("L_strISSWT_G : "+L_strISSWT_G);
		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getISSWT_G");
		}
		return L_strISSWT_G;
	}


		
		/** Printing Test certificate remark and L.A Remark
		 */
	private String getSPLINS(int P_intCURLN)
	{
		String L_strRETRN = "";
		//System.out.println("strREMDS.length() "+strREMDS.length());
		//System.out.println("intREMCN "+intREMCN);
		//System.out.println("intREMCN_END "+intREMCN_END);
		if(!flgCHKRM)
		{
			flgCHKRM = true;
			if(strTSTFL.equalsIgnoreCase("Y"))
				L_strRETRN =  "**** Test Certificate Required ****";
		}
		else if(P_intCURLN==5)
			L_strRETRN = padSTR1('R',"Container seal No. :",54,"171",'P');
		else if(P_intCURLN==6)
			L_strRETRN = padSTR1('R',crtLINE1(60,"-"),54,"171",'P');
		else if(P_intCURLN==7)
			L_strRETRN = padSTR1('R',"Vehicle Stack Details",30,"171",'P')+padSTR1('L',"G.TOTAL",24,"171",'P');
		else if(intREMCN>200 || strREMDS.length()==0 || intREMCN>=strREMDS.length())
		{
			flgREMDS_EOF = true;
			L_strRETRN = "";
		}
		else if(flgREMDS_EOF)
			L_strRETRN = "";
		else
		{
			intREMCN_END = (strREMDS.length()<intREMCN+60 ? strREMDS.length() : intREMCN+60);
			//System.out.println("strREMDS.substring(intREMCN,intREMCN_END) "+strREMDS.substring(intREMCN,intREMCN_END));
			L_strRETRN = strREMDS.substring(intREMCN,intREMCN_END);
			intREMCN = intREMCN_END;
		}
		//System.out.println(P_intCURLN+"  "+L_strRETRN);
		return L_strRETRN;
	}
		

		
	/** Line skipping method for reprinting option
	 */
	private void exeSKIP_LN_R(int P_intTOTLN)
	{
		try
		{
			for(int i=intLNCTR; i<P_intTOTLN; i++)
			{
				O_DOUT_R.writeBytes("\n");
			}
			intLNCTR = 0;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSKIP_LN_R");
		}
	}
		
		
		
	/**
	 */
	private void exeSKIP_LN(int P_intTOTLN)
	{
		try
		{
			for(int i=intLNCTR; i<P_intTOTLN; i++)
			{
				O_DOUT.writeBytes("\n");
			}
			intLNCTR = 0;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSKIP_LN");
		}
	}




	/**
	 */
	private void prnISS_REC()
	{
		try
		{
			String strISSWT1 = Double.parseDouble(strISSWT)>0 ? strISSWT : "";
			String strISSWT1_G = Double.parseDouble(strISSWT_G)>0 ? setNumberFormat(Double.parseDouble(strISSWT)+Double.parseDouble(strISSWT_G),3): "";
			O_DOUT.writeBytes(padSTR1('R',strPRDDS ,12,"171",'A'));
			O_DOUT.writeBytes(padSTR1('R',strLOTNO ,12,"171",'A'));
			O_DOUT.writeBytes(padSTR1('R',strMRKFL ,12,"171",'A'));
			O_DOUT.writeBytes(padSTR1('R',strMNLCD ,8,"171",'A'));
			O_DOUT.writeBytes(padSTR1('L',strISSPK ,9,"171",'A'));
			O_DOUT.writeBytes(padSTR1('L',strISSQT ,13,"171",'A'));
			if(!flgAUTFL)
			{
				O_DOUT.writeBytes(padSTR1('L',strBALPK ,23,"171",'A'));
				O_DOUT.writeBytes(padSTR1('L',strBALQT ,12,"171",'A'));
			}
			else
			{
				O_DOUT.writeBytes(padSTR1('L',strISSWT1,12,"171",'A'));
				O_DOUT.writeBytes(padSTR1('L',strISSWT1_G,12,"171",'A'));
			}
			
			O_DOUT.writeBytes("   "+padSTR1('L',strREMDS_LT ,30,"171",'A'));
			O_DOUT.writeBytes("\n");
			dblTOTPK += Double.parseDouble(strISSPK);
			dblTOTQT += Double.parseDouble(strISSQT);
			dblTOTQT_WT += Double.parseDouble(strISSWT);
			dblTOTQT_WTG += Double.parseDouble(strISSWT1_G);
			dblTOTPK_BAL += Double.parseDouble(strBALPK);
			dblTOTQT_BAL += Double.parseDouble(strBALQT);
			intLNCTR +=1;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnISS_REC");
		}
	}


	/** For Printing Quality Alloated  and Remark
	*/
	public void prnHDR_ALO()
    {
		try
		{		
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
			O_DOUT.writeBytes(padSTR1('C',"Q U A N T I T Y   A L L O T E D",135,"171",'P')+"\n");
			O_DOUT.writeBytes(padSTR1('R',"Grade",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Lot No.",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Mrkg(Y/N)",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Locn.",10,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"Bags",7,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"Qty(MT)",13,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"Bal.Bags",23,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"Bal.Qty",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('C',"Remark",22,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHDR_ALO");
		}
	}
	
	/** For Printing Quality Loaded  and Remark
	*/
	public void prnHDR_AUT()
    {
		try
		{
			String L_strISSPK = (txtPRDTP.getText().equals("07") ? "Pcs":"Bags");
			String L_strISSQT = (txtPRDTP.getText().equals("07") ? "Qty (SQM)":"Qty (MT)");
			String L_strISSWT = (txtPRDTP.getText().equals("07") ? "Net (MT)":"");
			String L_strISSWT_G = (txtPRDTP.getText().equals("07") ? "Grs (MT)":"");
			
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
			O_DOUT.writeBytes(padSTR1('C',"Q U A N T I T Y   L O A D E D",135,"171",'P')+"\n");
			O_DOUT.writeBytes(padSTR1('R',"Grade",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Lot No.",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Mrkg(Y/N)",12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"Locn.",10,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',L_strISSPK,7,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',L_strISSQT,13,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',L_strISSWT,12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',L_strISSWT_G,12,"171",'P'));
			O_DOUT.writeBytes(padSTR1('L',"",15,"171",'P'));
			O_DOUT.writeBytes(padSTR1('R',"",7,"171",'P'));
			O_DOUT.writeBytes(padSTR1('C',"Remark",25,"171",'P')+"\n");
			O_DOUT.writeBytes(crtLINE1(130,"-")+"\n");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHDR_AUT");
		}
	}
        

	private String getREMDS(String LP_LADNO,String P_strMKTTP,String LP_TRNTP)
	{
		String L_REMDS = "";
		try
		{
			ResultSet L_rstRSSET;
			M_strSQLQRY = "Select rm_remds from mr_rmmst where rm_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and rm_docno='"+LP_LADNO+"'";
			M_strSQLQRY += " and rm_mkttp='"+P_strMKTTP+"' and rm_trntp='"+LP_TRNTP+"'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
                                L_REMDS = nvlSTRVL(L_rstRSSET.getString("rm_remds").toString(),"");
			if(L_rstRSSET != null)
				L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getREMDS");
		}
		return L_REMDS;
	}
	
	private boolean getDOLAR()
	{
		try
		{
			ResultSet M_rstRSSET1; 
			M_strSQLQRY = "Select * from PR_LTMST where lt_cmpcd = '"+cl_dat.M_strCMPCD_pbst+"' and LT_PRDTP='"+strPRDTP+"' and LT_LOTNO='"+strLOTNO+"'";
			M_strSQLQRY += " and LT_RCLNO='"+strRCLNO+"' and LT_IPRDS='"+strPRDDS+"'";
			//System.out.println(M_strSQLQRY);
			//System.out.println("strPRDDS : "+strPRDDS);
			M_rstRSSET1 = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(M_rstRSSET1.next())
			{
				System.out.println("LM_IPRDS : "+M_rstRSSET1.getString("LT_IPRDS"));
				return true;
			}
			M_rstRSSET1.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDOLAR");
		}
		return false;
	}


	
	private void getBYRDTL()
	{
		try
		{
			ResultSet L_rstRSSET;
			M_strSQLQRY = "Select pt_prtnm,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd";
			M_strSQLQRY += " from co_ptmst where pt_prtcd='"+strBYRCD+"' and pt_prttp='C'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
			{
				strBYRNM = nvlSTRVL(L_rstRSSET.getString("pt_prtnm"),"");
                strBYAD1 = nvlSTRVL(L_rstRSSET.getString("pt_adr01"),"");
                strBYAD2 = nvlSTRVL(L_rstRSSET.getString("pt_adr02"),"");
                strBYAD3 = nvlSTRVL(L_rstRSSET.getString("pt_adr03"),"");
                strBYAD4 = nvlSTRVL(L_rstRSSET.getString("pt_adr04"),"");
                strBYPIN = nvlSTRVL(L_rstRSSET.getString("pt_pincd"),"");
			}
			if(!flgCNSGN)
				return;
			M_strSQLQRY = "Select pt_prtnm,pt_adr01,pt_adr02,pt_adr03,pt_adr04,pt_pincd";
			M_strSQLQRY += " from co_ptmst where pt_prtcd='"+strCNSCD+"' and pt_prttp='C'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET.next())
			{
                strCNSNM = nvlSTRVL(L_rstRSSET.getString("pt_prtnm"),"");
                strCNAD1 = nvlSTRVL(L_rstRSSET.getString("pt_adr01"),"");
                strCNAD2 = nvlSTRVL(L_rstRSSET.getString("pt_adr02"),"");
                strCNAD3 = nvlSTRVL(L_rstRSSET.getString("pt_adr03"),"");
                strCNAD4 = nvlSTRVL(L_rstRSSET.getString("pt_adr04"),"");
                strCNPIN = nvlSTRVL(L_rstRSSET.getString("pt_pincd"),"");
			}
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getBYRDTL");
		}
	}
	
	
	
	/**
	 */
    private  String padSTR1(char P_chrPADTP,String P_strSTRVL,int P_intPADLN, String P_strCHRSZ, char P_chrPRPRT)
	{
		//System.out.println(P_strSTRVL+"    "+P_intPADLN+"    "+P_strCHRSZ);
		if(P_strSTRVL == null)
			P_strSTRVL = " ";
		String P_strTRNVL = "";
		//int L_intPADLN = new Double(Double.parseDouble(P_strCHRSZ)*(100/171)*P_intPADLN).intValue();
        int L_intPADLN = new Double((Double.parseDouble(P_strCHRSZ)/171)*P_intPADLN).intValue();
        //int L_intPADLN = P_intPADLN;
        if (P_chrPRPRT == 'P')
        {
			if(LM_PPRNFL)
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
			setMSG("padSTR1 "+L_EX,'E');
		}
		return P_strTRNVL;
	}
	

	/** Method to create lines that are used in the Reports
	 */
	private String crtLINE1(int P_strCNT,String P_strLINCHR)
	{
		String strln = "";
        if(LM_PPRNFL)
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
	 * @param       LP_FLDNM                Name of the field for which data is to be extracted
	 * @param	LP_FLDTP		Data Type of the field
	*/
	private String getRSTVAL(ResultSet P_rstRSSET, String LP_FLDNM, String LP_FLDTP) 
	{
		//System.out.println("getRSTVAL : "+LP_FLDNM+"/"+LP_FLDTP);
	    try
	    {
			if (LP_FLDTP.equals("C"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? P_rstRSSET.getString(LP_FLDNM).toString() : "";
			//return P_rstRSSET.getString(LP_FLDNM) != null ? delQuote(nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString()," ")) : "";
			else if (LP_FLDTP.equals("N"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? nvlSTRVL(P_rstRSSET.getString(LP_FLDNM).toString(),"0") : "0";
			else if (LP_FLDTP.equals("D"))
				return P_rstRSSET.getDate(LP_FLDNM) != null ? M_fmtLCDAT.format(P_rstRSSET.getDate(LP_FLDNM)) : "";
			else if (LP_FLDTP.equals("T"))
				return P_rstRSSET.getTimestamp(LP_FLDNM) != null ? M_fmtLCDTM.format(P_rstRSSET.getTimestamp(LP_FLDNM)) : "";
			else if (LP_FLDTP.equals("T1"))
				return P_rstRSSET.getString(LP_FLDNM) != null ? P_rstRSSET.getString(LP_FLDNM).toString() : "";
				//return P_rstRSSET.getTime(LP_FLDNM) != null ? fmtHHMMSS.format(P_rstRSSET.getTime(LP_FLDNM)) : "";
				//   return M_fmtLCDTM.parse(P_rstRSSET.getString(LP_FLDNM)));
			else 
				return " ";
		}
		catch (Exception L_EX)
		{
			setMSG(L_EX,"getRSTVAL");
		}
		return " ";
	} 
	
	//METHOD TO GET A PARAMETER FROM CODE TRANSACTION TABLE.
	
	public String getPRMCOD(String LP_FLDRTN, String LP_STRMGP, String LP_STRSGP, String LP_STRCOD)
	{
		String L_strTEMP = "";
		ResultSet L_rstRSSET;
		M_strSQLQRY ="select " + LP_FLDRTN ;
		M_strSQLQRY += " from CO_CDTRN where CMT_CGMTP = "+ "'" + LP_STRMGP.trim() +"'" ;
		M_strSQLQRY += " AND CMT_CGSTP = " + "'" + LP_STRSGP.trim() + "'";
		M_strSQLQRY += " AND CMT_CODCD = " + "'" + LP_STRCOD.trim() + "'";
		try
		{
			L_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(L_rstRSSET.next())
			    L_strTEMP = L_rstRSSET.getString(1);
			L_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getPRMCOD"); 
		}
		
		return L_strTEMP;
	}

	
	
}
