import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;

class co_rpdoc extends cl_rbase 
{
	private JTextField txtPRTCD1;
	private JTextField txtPRTCD2;
	private JTextField txtFMDAT;	/** JTextField to display to specify To Group.*/
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"co_rpdoc.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;
	private String strSTRTP;
									/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private SimpleDateFormat sdfDATE;
	co_rpdoc()		/*  Constructor   */
	{
		super(2);
		try
		{
			M_pnlRPFMT.setVisible(true);
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		
			add(new JLabel("Letter Date"),4,4,1,2,this,'R');
			add(txtFMDAT = new TxtDate(),4,5,1,1,this,'L');
			add(new JLabel("From Party Code"),5,4,1,2,this,'R');
			add(txtPRTCD1 = new TxtLimit(5),5,5,1,1,this,'L');
			add(new JLabel("To Party Code"),6,4,1,2,this,'R');
			add(txtPRTCD2 = new TxtLimit(5),6,5,1,1,this,'L');
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			setENBL(false);
			sdfDATE=new SimpleDateFormat("MMMMM dd,yyyy");
		}
		catch(Exception L_SQLE)
		{
			setMSG(L_SQLE,"constructor");
		}
	}
	
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			setENBL(true);				
			txtFMDAT.requestFocus();
			txtFMDAT.setText(cl_dat.M_strLOGDT_pbst);
		
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtPRTCD1)
			{
				M_strHLPFLD = "txtPRTCD1";
				M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE ";
				M_strSQLQRY += " isnull(PT_STSFL,' ') <> 'X' AND PT_PRTTP ='S'";
				if(txtPRTCD1.getText().length() >0)
					M_strSQLQRY += " AND PT_PRTCD LIKE '"+txtPRTCD1.getText().toUpperCase() +"%'";
				M_strSQLQRY += " ORDER BY PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Supplier Code","Name"},2,"CT");
			}
			if(M_objSOURC == txtPRTCD2)
			{
				M_strHLPFLD = "txtPRTCD2";
				M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM FROM CO_PTMST WHERE ";
				M_strSQLQRY += " isnull(PT_STSFL,' ') <> 'X' AND PT_PRTTP ='S' ";
				if(txtPRTCD2.getText().length() >0)
					M_strSQLQRY += " AND PT_PRTCD LIKE '"+txtPRTCD2.getText().toUpperCase() +"%'";
				M_strSQLQRY += " and PT_PRTCD >='"+txtPRTCD1.getText().trim() +"'";
				M_strSQLQRY += " ORDER BY PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Supplier Code","Name"},2,"CT");
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtFMDAT)
			{
				txtPRTCD1.requestFocus();
				setMSG("Press F1 to Select the party Code..",'N');
			}
			else if(M_objSOURC == txtPRTCD1)
			{
				txtPRTCD2.requestFocus();
				setMSG("Press F1 to Select the party Code..",'N');
			}
			else if(M_objSOURC == txtPRTCD2)
			{
				cl_dat.M_btnSAVE_pbst.requestFocus();
			}
		}
		
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{
			if(M_strHLPFLD.equals("txtPRTCD1"))
			{
				txtPRTCD1.setText(cl_dat.M_strHLPSTR_pbst);			
			}
			if(M_strHLPFLD.equals("txtPRTCD2"))
			{
				txtPRTCD2.setText(cl_dat.M_strHLPSTR_pbst);			
			}
		}
		catch(Exception L_EX)
		{
		 setMSG(L_EX,"exeHLPOK"); 
		}
	}

	void genRPTFL()
	{
		try
		{
			F_OUT=new FileOutputStream(strRPFNM);
			D_OUT=new DataOutputStream(F_OUT); 
			//genRPHDR();
			// Header 1
			String strPRTNM ="X";
			String strADR01 ="ADR01";
			String strADR02 ="ADR02";
			String strADR03 ="ADR03";
			String strADR04 ="ADR04";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
			//	prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<HTML><HEAD><Title>Amalgamation Letter</title> </HEAD> <BODY><P><FONT NAME = \"Times New Roman \"><PRE style =\" font-size : 10 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				D_OUT.writeBytes("<PRE>");
			}
		//	System.out.println(txtFMDAT.getText());
		//	System.out.println("Formated "+sdfDATE.format(M_fmtLCDAT.parse(txtFMDAT.getText())));
			M_strSQLQRY = "SELECT PT_PRTCD,PT_PRTNM,PT_ADR01,PT_ADR02,PT_ADR03,PT_ADR04 from CO_PTMST WHERE pt_prttp ='S' AND PT_PRTCD between '"+txtPRTCD1.getText().trim()+"' AND '"+txtPRTCD2.getText().trim()+"' AND isnull(PT_STSFL,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{		
			while(M_rstRSSET.next())	
			{
				/*if(M_rdbHTML.isSelected())
				{
				D_OUT.writeBytes("<HTML><HEAD><Title>Amalgamation Letter</title> </HEAD> <BODY><P><FONT NAME = \"Times New Roman \"><PRE style =\" font-size : 10 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				D_OUT.writeBytes("<PRE>");
				}*/
				strPRTNM = M_rstRSSET.getString("PT_PRTNM");
				strADR01 = nvlSTRVL(M_rstRSSET.getString("PT_ADR01"),"");
				strADR02 = nvlSTRVL(M_rstRSSET.getString("PT_ADR02"),"");
				strADR03 = nvlSTRVL(M_rstRSSET.getString("PT_ADR03"),"");
				strADR04 = nvlSTRVL(M_rstRSSET.getString("PT_ADR04"),"");
				crtNWLIN();
				crtNWLIN();
				crtNWLIN();
				crtNWLIN();
				crtNWLIN();
				crtNWLIN();
				crtNWLIN();
				crtNWLIN();
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('L',"Date: "+txtFMDAT.getText(),90));
				crtNWLIN();
				D_OUT.writeBytes(padSTRING('L'," ",6));
				D_OUT.writeBytes("To,");
				crtNWLIN();
				//crtNWLIN();
				D_OUT.writeBytes("<b>");
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strPRTNM);
			D_OUT.writeBytes("</b>");
			crtNWLIN();
			//crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR01);
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR02);
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR03);
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR04);
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Dear Sir/Madam,");
			crtNWLIN();
			crtNWLIN();
		//	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		//		prnFMTCHR(D_OUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<b>");		
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("<u>");
			D_OUT.writeBytes("Re: Amalgamation of SPL Polymers Limited with Supreme Petrochem Ltd");
			D_OUT.writeBytes("</u>");		
			crtNWLIN();
			crtNWLIN();
		//	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
		//		prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");	

			
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("The purpose of this letter is to seek your consent to the proposed amalgamation of SPL ");
			crtNWLIN();	
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Polymers Limited (SPPL) with Supreme Petrochem Ltd (SPL).  The terms and conditions of  ");
			crtNWLIN();	
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("the Amalgamation are set forth in the enclosed draft scheme of Amalgamation.");
			crtNWLIN();	
			crtNWLIN();	
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Since you are one of the secured/unsecured creditors of SPL, we shall need your consent  ");
			crtNWLIN();	
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("in writing to the proposed Scheme of Amalgamation.  We enclose a specimen of the letter ");
			crtNWLIN();	
			D_OUT.writeBytes(padSTRING('L'," ",6));
			
			D_OUT.writeBytes("of consent and request you to kindly sign and return it to us."); 
			crtNWLIN();	
			//prnFMTCHR(D_OUT,M_strNOCPI17);
			//genRPFTR(); // Footer			 
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Thanking you");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Your faithfully");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes("For "+cl_dat.M_strCMPNM_pbst);
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes("\n");
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("AUTHORISED SIGNATORY");
			crtNWLIN();	
			D_OUT.writeBytes("</b>");
			D_OUT.writeBytes("<HR>");
			///////////////////////
			
									
			D_OUT.writeBytes(padSTRING('L',"Date: ",80));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("To,");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Secretarial Department");
			crtNWLIN();
			//crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes(cl_dat.M_strCMPNM_pbst);
			D_OUT.writeBytes("</b>");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Solitair Corporate Park, Bldg. No. 11,5th Flr.,167,");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Guru Hargobindji Marg, Andheri-Ghatkopar Link Road,");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Chakala, Andheri(E),Mumbai - 400093.");
		//	crtNWLIN();
		//	D_OUT.writeBytes(padSTRING('L'," ",6));
		//	D_OUT.writeBytes("Tel.:(91-22)67091900-04, Fax : 67091925/40055682");
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Dear Sirs,");
			crtNWLIN();
			crtNWLIN();

			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("<b><u>");
			D_OUT.writeBytes("Re: Amalgamation of SPL Polymers Limited with Supreme Petrochem Ltd");
			D_OUT.writeBytes("</b></u>");
			crtNWLIN();
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("1.    This has reference to your letter  dated ");
			D_OUT.writeBytes(sdfDATE.format(M_fmtLCDAT.parse(txtFMDAT.getText())));
			D_OUT.writeBytes(" enclosing a draft ");
			
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("of the proposed Scheme of Amalgamation of SPL Polymers Limited with Supreme ");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Petrochem Ltd.");
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("2.    We have perused the Scheme of  Amalgamation.  We hereby approve the Scheme  of ");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Amalgamation and agree to your proposal to apply to the Hon&#180ble  High Court at");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Bombay/Madras High Court to dispense with calling a meeting of the Unsecured/");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Secured Creditors and we  have no  objection to the Court passing  an order to  ");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("dispense with the calling of  meeting of the  Secured / Unsecured Creditors to   ");
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("approve the Scheme of Amalgamation.");
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Thanking you");
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Your faithfully");
			crtNWLIN();
			//crtNWLIN();
			//crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes("For ("+strPRTNM +")");
			D_OUT.writeBytes("</b>");
		
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
			}
				M_rstRSSET.close();
			}
			///////////////////////
			/*if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}*/
			D_OUT.close();
			F_OUT.close();
			if(M_rstRSSET==null)
			{
				M_rstRSSET.close();
			}	
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	
	
	private void crtNWLIN() 
	{
		try
		{
			D_OUT.writeBytes("\n");
			cl_dat.M_intLINNO_pbst++;
			/*if(M_rdbHTML.isSelected())
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
			}*/			
		}
		catch(Exception e)
		{
		    setMSG(e,"Chlid.crtNWLIN");
		}
	}

	/*void genRPHDR()
	{
		try
		{
			String strPRTNM ="X";
			String strADR01 ="ADR01";
			String strADR02 ="ADR02";
			String strADR03 ="ADR03";
			String strADR04 ="ADR04";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{	
				prnFMTCHR(D_OUT,M_strNOCPI17);
				prnFMTCHR(D_OUT,M_strCPI10);
				prnFMTCHR(D_OUT,M_strBOLD);
				prnFMTCHR(D_OUT,M_strNOENH);
			}	
			if(M_rdbHTML.isSelected())
			{
				D_OUT.writeBytes("<HTML><HEAD><Title>Amalgamation Letter</title> </HEAD> <BODY><P><FONT NAME = \"Times New Roman \"><PRE style =\" font-size : 10 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	    D_OUT.writeBytes("<PRE>");
			crtNWLIN();
			crtNWLIN();
			crtNWLIN();
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L',"Date: "+txtFMDAT.getText(),90));
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("To,");
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes("<b>");
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strPRTNM);
			D_OUT.writeBytes("</b>");
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR01);
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR02);
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR03);
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes(strADR04);
			crtNWLIN();
			crtNWLIN();
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("Dear Sir/Madam,");
			crtNWLIN();
			crtNWLIN();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<b>");		
			D_OUT.writeBytes(padSTRING('L'," ",6));
			D_OUT.writeBytes("<u>");
			D_OUT.writeBytes("Re: Amalgamation of SPL Polymers Limited with Supreme Petrochem Ltd");
			D_OUT.writeBytes("</u>");		
			crtNWLIN();
			crtNWLIN();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");	
			
			
			///////////////////
			
			
			///////////////////
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}*/
	
	/*void genRPFTR()
	{
		try
		{
		
			prnFMTCHR(D_OUT,M_strNOCPI17);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strEJT);			
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("<P CLASS = \"breakhere\">");
			//	if(M_rdbHTML.isSelected())			
			//			dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P>");    				
		}
		catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Footer");
		}
	}*/	

	boolean vldDATA()
	{
		try
		{
			if(txtFMDAT.getText().toString().length() == 0)
			{
				setMSG("Please Enter the Letter date ..",'E');
				txtFMDAT.requestFocus();
				return false;
			}
			if(txtPRTCD1.getText().toString().length() == 0)
			{
				setMSG("Please Enter the Party Code ..",'E');
				txtPRTCD1.requestFocus();
				return false;
			}
			if(txtPRTCD2.getText().toString().length() == 0)
			{
				setMSG("Please Enter the Party Code ..",'E');
				txtPRTCD2.requestFocus();
				return false;
			}
		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA");
		}
		return true;
	}
	
	void exePRINT()
	{
		try
		{
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			cl_dat.M_intLINNO_pbst=0;
			cl_dat.M_PAGENO = 0;
			
			if(M_rdbHTML.isSelected())
			     strRPFNM = strRPLOC + "co_rpdoc.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "co_rpdoc.doc";
				
			genRPTFL();
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{
				if (M_rdbTEXT.isSelected())
				{
				    doPRINT(strRPFNM);
				/*	Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\wordpad.exe "+strRPFNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				*/	
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
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				/*cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}*/				    	    	
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
}



