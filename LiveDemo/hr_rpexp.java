import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.ResultSet;import java.text.SimpleDateFormat;
import java.util.Calendar;import javax.swing.border.*;import javax.swing.JPanel;
import java.sql.*;
/*

 */

class hr_rpexp extends cl_rbase 
{
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpexp.html";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;	
	private FileOutputStream F_OUT ;  /** FileOutputStream for generated report file handeling.*/
	private DataOutputStream D_OUT ;/** DataOutputStream to hold Stream of report data.*/ 
	private JTextField txtDOCNO;			
	private JTextField txtDOCDT;			
	private JTextField txtENDDT;
	private String strDOTLN ="______________________________________________________________________________________________________";
	private String strLFTGAP="            ";
	String strDOCNO="",strDOCDT="";	

	Hashtable<String,String> hstEMPDL=null;
	Hashtable<String,String> hstSHFDS=null;
	int flaghtml=0,prnPRMFL=0;
	static String P_strDOCNO,P_strDOCDT;
	hr_rpexp()		/*  Constructor   */
	{
		super(1);
		try
		{
			// P_strDOCNO=LP_strDOCNO;
			// P_strDOCDT=LP_strDOCDT;
			
			setMatrix(20,8);			

			add(new JLabel("DOC NO"),3,2,1,1,this,'L');
			add(txtDOCNO = new TxtLimit(8),3,3,1,1,this,'L');
			//txtDOCNO.setText(P_strDOCNO);
			//txtDOCDT.setText(P_strDOCDT);
			
			add(new JLabel("DOC Date"),3,4,1,1,this,'L');
			add(txtDOCDT = new TxtDate(),3,5,1,1,this,'L');

			setENBL(true);
			M_pnlRPFMT.setVisible(true);

			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);

			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtDOCNO.setInputVerifier(oINPVF);
			//txtSTRDT.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);
			hstEMPDL = new Hashtable<String,String>();
			hstSHFDS = new Hashtable<String,String>();
			
			String L_strSQLQRY= M_strSQLQRY = "select EX_EMPNO EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM,cmt_codds EP_DPTNM ,EP_DPTCD";
			L_strSQLQRY+=" from hr_extrn,hr_epmst,co_cdtrn"; 
			L_strSQLQRY+=" where ex_cmpcd=ep_cmpcd and ex_empno=ep_empno and cmt_cgmtp='SYS' and cmt_cgstp = 'COXXDPT' and cmt_codcd = ep_dptcd and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null ";
			
			/*
			String L_strSQLQRY= M_strSQLQRY = " select EX_EMPNO EP_EMPNO,EP_LSTNM + ' ' + EP_FSTNM + ' ' + EP_MDLNM EP_EMPNM,A.cmt_codds EP_DPTNM ,B.cmt_codds EP_SHFDS";
			M_strSQLQRY+=" from hr_extrn,hr_epmst,co_cdtrn A,co_cdtrn B where ex_cmpcd=ep_cmpcd and ex_empno=ep_empno and A.cmt_cgmtp='SYS' and A.cmt_cgstp = 'COXXDPT' and A.cmt_codcd = ep_dptcd  and B.cmt_cgmtp='"+cl_dat.M_strCMPCD_pbst+"' and B.cmt_cgstp = 'COXXSHF' and B.cmt_codcd = ex_shfcd and EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' and isnull(ep_stsfl,'X') <> 'U' and ep_lftdt is null";
			*/
						
			ResultSet L_rstRSSET= cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
					hstEMPDL.put(nvlSTRVL(L_rstRSSET.getString("ep_empno"),""),nvlSTRVL(L_rstRSSET.getString("ep_empnm"),"")+"|"+nvlSTRVL(L_rstRSSET.getString("ep_dptnm"),""));
				L_rstRSSET.close();
			}

			String L_strSQLQRY1= " select cmt_codcd,cmt_codds from co_cdtrn where cmt_cgmtp='M"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp='COXXSHF'";
			ResultSet L_rstRSSET1= cl_dat.exeSQLQRY1(L_strSQLQRY1);
			if(L_rstRSSET1 != null)
			{
				while(L_rstRSSET1.next())
					hstSHFDS.put(nvlSTRVL(L_rstRSSET1.getString("cmt_codcd"),""),nvlSTRVL(L_rstRSSET1.getString("cmt_codds"),""));
				L_rstRSSET1.close();
			}
			
			M_pnlRPFMT.setVisible(true);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_rdbHTML.setSelected(true);
			
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}

	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
			{
				txtDOCNO.requestFocus();
				setENBL(true);
			}
			else
				setENBL(false);

		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is actionPerformed()");
		}		
	}

	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);

		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
			{						
				if(M_objSOURC == txtDOCNO)
				{
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					M_strHLPFLD = "txtDOCNO";
					String L_ARRHDR[] = {"DOC No","DOC Date","Personal/Official Flag"};
					M_strSQLQRY = "select EX_DOCNO,EX_DOCDT,EX_OFPFL from HR_EXTRN where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'and isnull(EX_STSFL,'')<>'X' ";
					if(txtDOCNO.getText().length() >0)				
						M_strSQLQRY += " AND EX_DOCNO like '"+txtDOCNO.getText().trim()+"%'";

					M_strSQLQRY += " order by EX_DOCNO";
					//System.out.println(">>>>EMPNO>>>>"+M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);     
				}
			}	
			if(L_KE.getKeyCode() == L_KE.VK_ENTER )
			{
				if(M_objSOURC == txtDOCNO)
					cl_dat.M_btnSAVE_pbst.requestFocus();
			}	
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"This is KeyPressed");
		}	

	}

	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;		
			if(M_strHLPFLD == "txtDOCNO")
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDOCNO.setText(L_STRTKN.nextToken());
				txtDOCDT.setText(L_STRTKN.nextToken());
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	void exePRNTPRM(String LP_strDOCNO,String LP_strDOCDT)
	{
		try{
			prnPRMFL=1; //indicates that it is called from entry prog
		P_strDOCNO=LP_strDOCNO;
		P_strDOCDT=LP_strDOCDT;	
		strRPFNM = strRPLOC + "hr_rpexp.html";
		M_rdbHTML.setSelected(true);
		exePRINT();
		//doPRINT(strRPFNM);
		//Runtime r = Runtime.getRuntime();
		//Process p = null;					    
		//p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
		}
		catch(Exception e)
		{
		setMSG("For Printing Select File Menu, then Print  ..",'N');
		}
	}
	
	
	
	void genRPTFL(String LP_strDOCNO,String LP_strDOCDT)
	{
		try
		{
			String P_strDOCNO=LP_strDOCNO;
			String P_strDOCDT=LP_strDOCDT;
			
			//System.out.println("%%%%"+M_rdbHTML.isSelected()+""+M_rdbTEXT.isSelected());
			
			if(M_rdbHTML.isSelected())
			{
				flaghtml=1;
			}
			
			String L_strEMPNO="",L_strEMPNM="",L_strAUTBY="",L_strDPTNM="",L_strREMDS="",L_strDOCNO="",L_strDOCDT="",L_strEINTM="",L_strEOTTM="",L_strEMPDL="";
			String L_substrEOTTM="",L_substrEINTM="",L_strSHFDS="",L_strSHFCD="",L_strOFPFL="";
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			genRPHDR();
			
			M_strSQLQRY =" Select ex_docno,ex_docdt,ex_empno,ex_ofpfl,ex_remds,ex_eottm,ex_eintm,ex_autby,ex_shfcd from hr_extrn";
			M_strSQLQRY+=" where ex_docno='"+P_strDOCNO+"' and ex_docdt='"+M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strDOCDT))+"'";

			//System.out.println(M_strSQLQRY);
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null && M_rstRSSET.next() )
			{
				L_strEMPNO=nvlSTRVL(M_rstRSSET.getString("ex_empno"),"");

				if(hstEMPDL.containsKey(L_strEMPNO))
				{
					L_strEMPDL = hstEMPDL.get(L_strEMPNO);
					//System.out.println("L_strEMPDL"+L_strEMPDL);
					StringTokenizer L_STRTKN=new StringTokenizer( L_strEMPDL,"|");
					L_strEMPNM=L_STRTKN.nextToken();
					L_strDPTNM=L_STRTKN.nextToken();

				}
				
				{
					L_strDOCNO=nvlSTRVL(M_rstRSSET.getString("ex_docno"),"");
					L_strDOCDT=nvlSTRVL(M_rstRSSET.getString("ex_docdt"),"");
					L_strEOTTM=nvlSTRVL(M_rstRSSET.getString("ex_eottm"),"");
					L_strEINTM=nvlSTRVL(M_rstRSSET.getString("ex_eintm"),"");
					L_strSHFCD=nvlSTRVL(M_rstRSSET.getString("ex_shfcd"),"");					
					L_strREMDS=nvlSTRVL(M_rstRSSET.getString("ex_remds"),"");
					L_strAUTBY=nvlSTRVL(M_rstRSSET.getString("ex_autby"),"");
					L_strOFPFL=nvlSTRVL(M_rstRSSET.getString("ex_ofpfl"),"");
					if(L_strOFPFL.equals("O"))
					{
						L_strOFPFL="Official";
					}
					
					else if(L_strOFPFL.equals("P"))
					{
						L_strOFPFL="Personal";
					}
							
					if(hstSHFDS.containsKey(L_strSHFCD))
					{
						L_strSHFDS = hstSHFDS.get(L_strSHFCD);
						System.out.println("L_strSHFDS"+L_strSHFDS);
						
					}
					

				}
				if(M_rdbHTML.isSelected())
				{
					
					prnFMTCHR(D_OUT,M_strCPI12);prnFMTCHR(D_OUT,M_strBOLD);
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst+"., "+cl_dat.M_strCMPLC_pbst,100));
					D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");									
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',"E X I T   P A S S",100));	
					D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('L',"Doc No. "+L_strDOCNO,100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");			
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Emp No. "+L_strEMPNO,50));					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('L',"Date. "+L_strDOCDT,50));D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP+strDOTLN);D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");			
					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Please allow Mr.: "+L_strEMPNM.toUpperCase()+" of "+L_strDPTNM+" department",100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"to leave the factory premises for following reason:",100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',L_strREMDS,100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"("+L_strOFPFL+")",50));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Original shift: "+L_strSHFDS,50));
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('L',L_strAUTBY,50));D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('L',"(Department Head)",100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					
					if(L_strEOTTM.trim().length()>0)
					L_substrEOTTM=L_strEOTTM.substring(11,16)+" hrs";
					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Out Time:"+L_substrEOTTM,50));
					
					
					if(L_strEINTM.trim().length()>0)
					L_substrEINTM=L_strEINTM.substring(11,16)+" hrs";
						
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('L',"IN Time:"+L_substrEINTM,50));D_OUT.writeBytes("\n");
									
					
					D_OUT.writeBytes(strLFTGAP+strDOTLN);D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");		
					
			
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',"(Employee reporting back must inform to security Department at the time of ",100));D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',"entry the factory premises)",100));
					
					flaghtml=0;
				}
				
				if(M_rdbTEXT.isSelected())
				{
					prnFMTCHR(D_OUT,M_strCPI12);prnFMTCHR(D_OUT,M_strBOLD);
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst+"., "+cl_dat.M_strCMPLC_pbst,100));
					D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");									
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',"E X I T   P A S S",100));	
					D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
										
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('L',"Doc No. "+L_strDOCNO,100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");			
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Emp No. "+L_strEMPNO,50));					
					D_OUT.writeBytes(padSTRING('L',"Date. "+L_strDOCDT,50));D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP+strDOTLN);D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");			
					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Please allow Mr.: "+L_strEMPNM.toUpperCase()+" of "+L_strDPTNM+" department",100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"to leave the factory premises for following reason:",100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',L_strREMDS,100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Original shift: "+L_strSHFDS,50));
					D_OUT.writeBytes(padSTRING('L',L_strAUTBY,50));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('L',"(Department Head)",100));D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");
					
					if(L_strEOTTM.trim().length()>0)
					L_substrEOTTM=L_strEOTTM.substring(11,16)+" hrs";
					
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('R',"Out Time:"+L_substrEOTTM,50));
					
					
					if(L_strEINTM.trim().length()>0)
					L_substrEINTM=L_strEINTM.substring(11,16)+" hrs";
						
					D_OUT.writeBytes(padSTRING('L',"IN Time:"+L_substrEINTM,50));D_OUT.writeBytes("\n");
									
					
					D_OUT.writeBytes(strLFTGAP+strDOTLN);D_OUT.writeBytes("\n");D_OUT.writeBytes("\n");		
					
			
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',"(Employee reporting back must inform to security Department at the time of ",100));D_OUT.writeBytes("\n");
					D_OUT.writeBytes(strLFTGAP);D_OUT.writeBytes(padSTRING('C',"entry the factory premises)",100));
					
					flaghtml=0;
				}
			}
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			if(M_rstRSSET!=null)
			{
				M_rstRSSET.close();
			}	
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			//L_EX.printStackTrace();
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	String prnSTR(float P_fltVAL)
	{
		if(P_fltVAL == 0)
			return "-";
		else
			return String.valueOf(P_fltVAL).toString();
	}

	private String chkZERO(String LP_STR)
	{
		try
		{
			if(LP_STR.equals("0.000"))
				return "-";
			else
				return LP_STR;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"chkZERO()");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return "";
	}


	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			if(M_rdbHTML.isSelected())
			{
				if(cl_dat.M_intLINNO_pbst >70)
				{
					genRPFTR();
					genRPHDR();
				}
			}	
			else if(cl_dat.M_intLINNO_pbst >60)
			{		
				genRPFTR();
				genRPHDR();			
			}			
		}
		catch(Exception e)
		{
			setMSG(e,"Chlid.crtNWLIN");
		}
	}

	void genRPHDR()
	{
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strCPI17);				
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	


			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<b>");
				//D_OUT.writeBytes("<HTML><HEAD><Title>Lotwise Bagging Despatch Summary</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<HTML><HEAD></HEAD> <BODY><P><PRE style =\" font-size : 9 pt \">");   
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;

			crtNWLIN();
			prnFMTCHR(D_OUT,M_strBOLD);
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");

		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
			setCursor(cl_dat.M_curDFSTS_pbst);

		}
	}

	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			//D_OUT.writeBytes(padSTRING('L',"Employee reporting back must inform to security Department at the time of entry the factory: ",70));
			crtNWLIN();
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}	

	boolean vldDATA()
	{
		try
		{
			/*
			if(txtDOCNO.getText().length()==0)
			{
				txtDOCNO.requestFocus();
				setMSG("Please Enter DOC NO",'E');
				return false;
			}
			if(txtDOCDT.getText().length()==0)
			{
				txtDOCDT.requestFocus();
				setMSG("Please Enter DOC Date",'E');
				return false;
			}	
                       */

		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA()");
		}
		return true;
	}

	void exePRINT()  //called when PRINT clicked
	{
		try
		{			
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}

			if(cl_dat.exeDBCMT("exeUPDTBLS"))
				setMSG("Generating Report ... ",'N');

			if(M_rdbHTML.isSelected())
				strRPFNM = strRPLOC + "hr_rpexp.html";
			if(M_rdbTEXT.isSelected())
				strRPFNM = strRPLOC + "hr_rpexp.doc";
			
			if(prnPRMFL==0)//called from within report prog
			genRPTFL(txtDOCNO.getText().trim(),txtDOCDT.getText().trim()); //called within
			else if(prnPRMFL==1)
			{
				M_rdbHTML.setSelected(true);
				genRPTFL(P_strDOCNO,P_strDOCDT);   //called from entry prog}
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)||prnPRMFL==1)
			{
				if (M_rdbTEXT.isSelected())
				{
					doPRINT(strRPFNM);
				}	
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;					    
					p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}

			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
				Runtime r = Runtime.getRuntime();
				Process p = null;
				if(M_rdbHTML.isSelected())
					p  = r.exec("c:\\windows\\iexplore.exe "+strRPFNM); 
				else
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 

			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exePRINT");
		}
	}

	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);

	}

	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				if(((JTextField)input).getText().length() == 0)
				return true;
				
				if(input == txtDOCNO)
				{
					M_strSQLQRY = "select EX_DOCNO,EX_DOCDT from HR_EXTRN where EX_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'and isnull(EX_STSFL,'')<>'X' ";
					M_strSQLQRY += " and EX_DOCNO='"+txtDOCNO.getText()+"'";
					//System.out.println("INPVF DOCNO : "+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET.next() && M_rstRSSET!=null)
					{
						java.sql.Date tmpDAT=M_rstRSSET.getDate("EX_DOCDT");
						if(!(M_rstRSSET.getDate("EX_DOCDT")==null))
							txtDOCDT.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("EX_DOCDT")));
						   //txtDOCDT.setText(M_rstRSSET.getString("EX_DOCDT"));
						else
							txtDOCDT.setText("");
	}	
					else
					{
						setMSG("Enter Valid DOC No",'E');
						return false;
					}
				}
			}
			catch(Exception L_E)
			{
				//L_E.printStackTrace();
				setMSG(L_E,"INPVF");
			}
			return true;
		}
	}




}



