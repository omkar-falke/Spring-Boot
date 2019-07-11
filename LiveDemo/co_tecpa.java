/*
System Name   : Material Management System
Program Name  : Customer Complaint
Program Desc. : Entry screen to enter the D part.
Author        : Mr S.R.Mehesare
Date          : 26.12.2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import javax.swing.JPanel;import javax.swing.JTextArea;import javax.swing.JLabel;
import javax.swing.JComboBox;import java.sql.ResultSet;import javax.swing.JComponent;
import javax.swing.JScrollPane;import javax.swing.JEditorPane;import javax.swing.JButton;
import javax.swing.JTextField;import javax.swing.JTextArea;import javax.swing.JTabbedPane;
import javax.swing.InputVerifier;import java .util.Hashtable;import java.util.StringTokenizer;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.awt.Color;

class co_tecpa extends cl_pbase
{
    private JPanel pnlPARTB;
    private JTabbedPane jtbMAIN;
    private JTextArea txaREMDS;
	private JComboBox cmbCMPTP;
    private JTextField txtREGNO;
	private JTextField txtBREGN;
	private JTextField txtREGDT;
	private JTextField txtPRTCD;
	private JTextField txtPRTNM;	
	private JTextField txtDATE;
	private JTextField txtDPTCD;
	private JTextField txtDPTNM;
    private JEditorPane edpRECOM;
    private JButton btnPRNT;
	private JButton btnPRTA;
	private JButton btnPRTB;
	private JButton btnPRTC;
	private JLabel lblSTATS;
	
	private JComboBox cmbYESNO;		
	private String strOREMDS="";
	private Hashtable<String,String> hstDPTDS; 
	private Hashtable hstTEMP;
	
	private String strCMPTP;
	private String strCPABY;
	private int intNODPT;
	
	private INPVF objINPVR = new INPVF();
	String strFILNM = "";
	String strEML= "No";
	String strREGNO = "";
    co_tecpa()
    {
        super(2);
		try
		{
			setMatrix(20,8);
			jtbMAIN = new JTabbedPane();
			pnlPARTB = new JPanel();
			pnlPARTB.setLayout(null);
		  
			add(new JLabel("Complaint Type"),2,1,1,2,this,'L');
			add(cmbCMPTP = new JComboBox(),2,2,1,1.1,this,'L');		
			add(new JLabel("Reg. No"),2,3,1,.9,this,'R');
			add(txtREGNO = new TxtLimit(8),2,4,1,1,this,'L');
			add(new JLabel("Brach Reg."),2,5,1,.8,this,'R');
			add(txtBREGN = new TxtLimit(8),2,6,1,1,this,'L');
			add(btnPRNT = new JButton("PRINT"),2,8,1,1,this,'L');  
		
			add(new JLabel("Reported On"),3,1,1,1,this,'L');
			add(txtREGDT = new TxtDate(),3,2,1,1.1,this,'L');
			add(new JLabel("Dept Code"),3,3,1,.9,this,'R');
			add(txtDPTCD = new TxtLimit(3),3,4,1,1,this,'L');  
			add(txtDPTNM = new TxtLimit(20),3,5,1,3,this,'L');  
		
			add(new JLabel("Inv. Date"),4,1,1,1,this,'L');
			add(txtDATE = new TxtDate(),4,2,1,1.1,this,'L');	    
			add(new JLabel("Party Name"),4,3,1,.9,this,'R');
			add(txtPRTCD = new TxtLimit(5),4,4,1,1,this,'L');  
			add(txtPRTNM = new TxtLimit(40),4,5,1,4,this,'L');  
			
			add(new JLabel("Inv. Completed"),5,1,1,1,this,'L');
			add(cmbYESNO = new JComboBox(),5,2,1,1.1,this,'L'); 
			add(new JLabel("Show Details"),5,3,1,.9,this,'R');
			add(btnPRTA = new JButton("PART A"),5,4,1,1,this,'L');
			add(btnPRTB = new JButton("PART B"),5,5,1,1,this,'L'); 
			add(btnPRTC = new JButton("PART C"),5,6,1,1,this,'L'); 
			
			add(new JLabel("Status : "),6,1,1,1,this,'L');
			add(lblSTATS = new JLabel(""),6,2,1,3,this,'L');
			lblSTATS.setForeground(Color.blue);				
			cmbYESNO.addItem("No");
			cmbYESNO.addItem("Yes");				 
			
			add(new JLabel("CAPA Details (Part D)"),1,1,1,6,pnlPARTB,'L');
			txaREMDS = new TxtAreaLimit(1000);
			add(new JScrollPane(txaREMDS,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),2,1,10,7.5,pnlPARTB,'L');
		  			
		 	add(pnlPARTB,6,1,13,8,this,'L');
			setENBL(false);
			hstDPTDS = new Hashtable<String,String>();
			M_strSQLQRY ="SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstDPTDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			txtREGNO.setInputVerifier(objINPVR);
			txtPRTCD.setInputVerifier(objINPVR);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
    }
	/**
	 * Super Class method overrided to enable & disable the Components.
	 * @param L_flgSTAT boolean argument to pass State of the Components.
	 */
	void setENBL(boolean L_flgSTAT)
	{       
		super.setENBL(L_flgSTAT);				
		if(L_flgSTAT == false)	
			return;		
		txtBREGN.setEnabled(false);
		txtREGDT.setEnabled(false);
		txtPRTCD.setEnabled(false);
		txtPRTNM.setEnabled(false);		
		txtDATE.setEnabled(false);
		txtDPTNM.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) 
		{
			txaREMDS.setEnabled(false);
			cmbYESNO.setEnabled(false);
		}
		else 
			txaREMDS.setEnabled(true);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);    
		try 
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() > 0)			
				{
				/*	M_strSQLQRY = "update CO_CMTRN set CMT_STSFL ='P'"
				+ " where CMT_REGNO in ('60100018','60100010')"
				+ " AND CMT_CMPTP = '01' AND CMT_REMTP = 'PARTC'";// AND CMT_DPTCD = '101'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Data Modified",'N');
			}
			M_strSQLQRY = "update CO_CMMST set CM_STSFL ='8'"
				+ " where CM_REGNO in ('60100018','60100010')";
				//+ " AND CMT_CMPTP = '01' AND CMT_REMTP = 'PARTD' AND CMT_DPTCD = '101'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				setMSG("Data Modified",'N');
			}*/
			 
					if(cmbCMPTP.getItemCount() == 0)
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP = 'COXXCMT'";
						M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
						{
							if(M_rstRSSET != null)
							{								
								while(M_rstRSSET.next())
									cmbCMPTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
								M_rstRSSET.close();
							}
						}
					}
					cmbCMPTP.setSelectedItem("02 Technical");
					strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);					
					setENBL(true);
					txtREGNO.requestFocus();
				}
				else
					setENBL(false);
			}
			else if(M_objSOURC == cmbCMPTP)
			{
				strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);
			}
			else if(M_objSOURC == txtREGNO)
			{
				if(txtREGNO.getText().trim().length() == 8)
				{	
					strREGNO = txtREGNO.getText().trim();
					lblSTATS.setText("");
					txtPRTCD.setText("");
					txtPRTNM.setText("");
					txtDATE.setText("");
					txtBREGN.setText("");
					strCPABY = "";					
					java.sql.Date L_tmpDATE;
					M_strSQLQRY = "select CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM,CM_CPABY from CO_CMMST where CM_SBSCD = '"+ M_strSBSCD+"'"
					+" AND CM_CMPTP ='"+ strCMPTP +"'"
					+" AND CM_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CM_STSFL,'')<>'X'";
					M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{
							txtPRTCD.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTCD"),""));
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),""));
							txtDATE.setText(cl_dat.M_strLOGDT_pbst);
							L_tmpDATE = M_rstRSSET.getDate("CM_REPDT");
							if(L_tmpDATE != null)
								txtREGDT.setText(M_fmtLCDAT.format(L_tmpDATE));
							txtBREGN.setText(nvlSTRVL(M_rstRSSET.getString("CM_BRNRG"),""));
							strCPABY = nvlSTRVL(M_rstRSSET.getString("CM_CPABY"),"");
						}
					}					
					txtDPTCD.requestFocus();					
					StringTokenizer L_stkTEMP = new StringTokenizer(strCPABY,"_");
					String L_strTEMP= "";
					while(L_stkTEMP.hasMoreTokens())
					{
						if(L_strTEMP.length() != 0)
							L_strTEMP +=",";
						L_strTEMP += "'"+ L_stkTEMP.nextToken() +"'";
					}
					strCPABY = L_strTEMP;
				}
			}
			else if(M_objSOURC == txtDPTCD)
			{
				java.sql.Date L_tmpDATE;
				if(txtDPTCD.getText().trim().length() == 3)
				{	
					if(hstDPTDS.containsKey(txtDPTCD.getText().trim()))						
						txtDPTNM.setText(hstDPTDS.get(txtDPTCD.getText().trim()).toString());
					else 
					{
						setMSG("Invalid Department Code, Press F1 to select from List..",'E');
						return;
					}						
					if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
					{							
						txaREMDS.setText("");
						M_strSQLQRY = "select CMT_REMDS,CMT_DPTCD,CMT_REMDT,CMT_STSFL from CO_CMTRN where"
						+" CMT_SBSCD = '"+ M_strSBSCD+"'"
						+" AND CMT_CMPTP ='"+ strCMPTP +"'"
						+" AND CMT_REGNO = '"+ txtREGNO.getText().trim()+"'"
						+" AND CMT_PRTNO = 'D'"
						+" AND CMT_REMTP = 'PARTD'"
						+" AND CMT_DPTCD = '"+ txtDPTCD.getText().trim()+"'"
						+" AND isnull(CMT_STSFL,'')<>'X'";							
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							M_strSQLQRY	 += " AND CMT_STSFL = 'P'";
						M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);							
						if(M_rstRSSET != null)
						{
							strOREMDS = "";
							String L_strDPTCD = "";
							if(M_rstRSSET.next())
							{
								L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
								if(L_tmpDATE != null)
									txtDATE.setText(M_fmtLCDAT.format(L_tmpDATE));
								strOREMDS = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"").trim();
								txaREMDS.setText(strOREMDS);
								L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
								txtDPTCD.setText(L_strDPTCD);
								if(hstDPTDS.containsKey(L_strDPTCD))
									txtDPTNM.setText(hstDPTDS.get(L_strDPTCD).toString());
								String L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("CMT_STSFL")," ");
								if(L_strSTSFL.equals("C"))
								{
									cmbYESNO.setSelectedItem("Yes");
									lblSTATS.setText("Details Entered & CAPA Completed");
								}
								else if(L_strSTSFL.equals("P"))
								{
									txaREMDS.requestFocus();
									cmbYESNO.setSelectedItem("No");
									lblSTATS.setText("Details Entered but CAPA Not Completed");
								}
							}
							else // OPENQ
								lblSTATS.setText("CAPA Details not Entered");
							M_rstRSSET.close();
						}
					}
					else // OPADD
					{
						lblSTATS.setText("CAPA Details not Entered");
						txaREMDS.requestFocus();
					}
				}
			}
			else if(M_objSOURC == btnPRTA)
			{
				if(txtREGNO.getText().trim().length() != 8)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to view Part A..",'E');
					return;
				}
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasA.html";
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"A");
				
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM);
			}
			else if(M_objSOURC == btnPRTB)
			{
				if(txtREGNO.getText().trim().length() != 8)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to view Part B..",'E');
					return;
				}
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasB.html";
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"B");
				
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM);
			}
			else if(M_objSOURC == btnPRTC)
			{
				if(txtREGNO.getText().trim().length() != 8)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to view Part C..",'E');
					return;
				}
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasC.html";
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"C");
				
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM);
			}
			else if(M_objSOURC == btnPRNT)
			{
				if(txtREGNO.getText().trim().length() != 8)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to generate the Report ..",'E');
					return;
				}
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasD.html";
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"D");
				
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"ActionPerformed");
		}
	}
	public void focusGained(FocusEvent L_FE)
	{
		super.focusGained(L_FE);
		if(M_objSOURC == txtREGNO)
			setMSG("Please Enter HO Registration number Or Press F1 to select from List..",'N');		
		else if(M_objSOURC == txtPRTCD)
			setMSG("Please Enter Party Code..",'N');		
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);		
		try
		{
			if((L_KE.getKeyCode() == L_KE.VK_F1))
			{			
				if(M_objSOURC == txtREGNO)
				{
					M_strHLPFLD = "txtREGNO";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strSQLQRY =" SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM from CO_CMMST where"
						+" CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CM_STSFL,'')<>'X' AND CM_STSFL >='5' AND CM_STSFL <>'9'"			 
						+" AND CM_FINFL = 'Y' AND CM_CPABY <>''";
						if(txtREGNO.getText().length() >0)
							M_strSQLQRY += " AND CM_REGNO like '"+txtREGNO.getText().trim()+"%'";						
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						M_strSQLQRY =" SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM from CO_CMMST where"
						+" CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CM_STSFL,'')<>'X' AND CM_STSFL <>'9' AND CM_FINFL = 'Y'"	//AND CM_STSFL not in('8','9')
						+" AND CM_REGNO in(Select CMT_REGNO from CO_CMTRN where CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CMPTP = '"+ strCMPTP+"' AND CMT_REMTP = 'PARTD'"
						+" AND CMT_STSFL = 'P'";						
						if(txtREGNO.getText().length() >0)
							M_strSQLQRY += " AND CMT_REGNO like '"+txtREGNO.getText().trim()+"%'";
						M_strSQLQRY += ")";
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						M_strSQLQRY =" SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM from CO_CMMST where"
						+" CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CM_STSFL,'')<>'X' AND CM_STSFL >'5'"			 
						+" AND CM_FINFL = 'Y'";
						if(txtREGNO.getText().length() >0)
							M_strSQLQRY += " AND CM_REGNO like '"+txtREGNO.getText().trim()+"%'";				
					}
						
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Reg. No.","Regional Reg.No.","Reg.Date","Party Code","Party Name"},5,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}				
				else if(M_objSOURC == txtDPTCD)
				{
					if(txtREGNO.getText().trim().length() != 8)
					{
						setMSG("Please Enter Registration Number before Party Code..",'E');
						return;
					}
					M_strHLPFLD = "txtDPTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN Where"
						+" CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' AND isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_CODCD in("+ strCPABY+")"
						+" AND CMT_CODCD NOT in(Select CMT_DPTCD from CO_CMTRN Where"
						+" CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_REGNO ='"+txtREGNO.getText().trim()+"'"
						+" AND CMT_CMPTP = '"+ strCMPTP +"'"
						+" AND CMT_REMTP = 'PARTD'"
						+" AND CMT_PRTNO = 'D')";
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN A,CO_CMTRN B"
						+" where isnull(A.CMT_STSFL,'')<>'X'"
						+" AND A.CMT_CGMTP ='SYS' AND A.CMT_CGSTP ='COXXDPT' AND isnull(B.CMT_STSFL,'')<>'X'"
						+" AND B.CMT_REGNO ='"+ txtREGNO.getText().trim()+"'"
						+" AND B.CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'"
						+" AND B.CMT_CMPTP = '"+ strCMPTP +"'"
						+" AND B.CMT_REMTP = 'PARTD'"
						+" AND B.CMT_PRTNO = 'D'"
						+" AND B.CMT_STSFL = 'P'"
						+" AND A.CMT_CODCD = B.CMT_DPTCD";
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{							
						M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN A,CO_CMTRN B Where"
						+" CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' AND isnull(A.CMT_STSFL,'')<>'X'"
						+" AND CMT_CODCD in("+ strCPABY+")";
					}
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Dept. Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if((L_KE.getKeyCode() == L_KE.VK_ENTER))
			{							
				if(M_objSOURC == txtPRTCD)
				{
					if(txtPRTCD.getText().trim().length()>3)				
						cmbYESNO.requestFocus();
				}				
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"VK_F1");
		}
	}
	/**
	 * Super class Method overrrided to execuate the F1 Help.
	 */
	public void exeHLPOK()
	{		
		super.exeHLPOK();
		if(M_strHLPFLD.equals("txtREGNO"))
		{
			java.sql.Date L_tmpDATE;
			cl_dat.M_flgHELPFL_pbst = false;			
			txtREGNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtBREGN.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			txtREGDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));			
			strREGNO = txtREGNO.getText().trim();
		}		
		else if(M_strHLPFLD.equals("txtDPTCD"))
		{
			cl_dat.M_flgHELPFL_pbst = false;			
			txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtDPTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
		}
	}
	/**
	 * Method to validate the data before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		try
		{
			if(txtREGNO.getText().trim().length() == 0)
			{
				setMSG("Please enter Registration Number..",'E');
				txtREGNO.requestFocus();
				return false;
			}			
			else if(txtDPTCD.getText().trim().length() == 0)
			{
				setMSG("Please enter Department Code Or Press F1 to select from List..",'E');
				txtBREGN.requestFocus();
				return false;
			}
			else if(txtDATE.getText().trim().length() == 0)
			{
				setMSG("Please enter Invoice Date ..",'E');
				txtDATE.requestFocus();
				return false;
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
		}
		return true;
	}	
	/**
	 * Super class method overrided here to inhance its functionality, to perform 
	 * Database operations.
	 */
	void exeSAVE()
	{					
		try
		{
			if(!vldDATA())			
				return ;			
			else
				setMSG("",'E');			
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))			
				modREMDS("PARTD",txaREMDS.getText().trim(),"",cl_dat.M_strUSRCD_pbst);			
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				modREMDS("PARTD",txaREMDS.getText().trim(),strOREMDS,cl_dat.M_strUSRCD_pbst);
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				modREMDS("PARTD","","strOREMDS",cl_dat.M_strUSRCD_pbst);
			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{				
				clrCOMP();	
				lblSTATS.setText("");
				setENBL(true);
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Saved Successfully..",'N'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Data Deleted Successsfully ..",'N');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Saved Successfully..",'N');
			}
			else
			{
				strEML = "No";
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
					setMSG("Error in saving details..",'E'); 
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
					setMSG("Error in Deleting data..",'E');
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Error in saving data..",'E');
			}
			if(strEML.equals("Yes"))
			{
				cl_eml ocl_eml = new cl_eml();
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasD.html";				
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,strREGNO);
				obj.getDATA(M_strSBSCD,strCMPTP,strREGNO,"D");
				
				M_strSQLQRY =" SELECT CMT_CODDS from CO_CDTRN where"
					+" isnull(CMT_STSFL,'')<>'X'"	
					+" AND CMT_CGMTP ='EML' AND CMT_CGSTP ='COXXCAS'"
					+" AND CMT_CODCD like '"+ strCMPTP+ "D%'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
						ocl_eml.sendfile(M_rstRSSET.getString("CMT_CODDS").trim()+"@spl.co.in",strFILNM,"CAPA Details"," ");
					M_rstRSSET.close();
				}
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeSAVE");
		}
	}
	/**
	 * Method for addition & Modification of the Remarks Entered.
	 * @param P_strREMTP String argument to pass the Remark Type.
	 * @param P_strREMDS String argument to pass the new Remark Description.
	 * @param P_strOREMDS String argument to pass the Old Remark Decription
	 * @param P_strUSRCD String argument to pass User Code who has entered the corresponding Ramraks.
	 */
	private void modREMDS(String P_strREMTP,String P_strREMDS,String P_strOREMDS,String P_strUSRCD)
	{
		try
		{
			if(!P_strOREMDS.equals(P_strREMDS))
			{
				if(P_strREMDS.length() == 0)
				{	
					M_strSQLQRY = "Delete from CO_CMTRN";
					/*
					M_strSQLQRY = "update CO_CMTRN set CMT_REMDS = '-',"
					+ "	CMT_STSFL ='0',"
					+ "	CMT_TRNFL = '',"
					+ "	CMT_LUSBY ='"+ P_strUSRCD+"',"
					+ "	CMT_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"*/
					M_strSQLQRY +=" where CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_REMTP = '"+ P_strREMTP +"' AND CMT_CMPTP = '"+strCMPTP+"'"
					+ " AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"				
					+ " AND CMT_DPTCD = '"+ txtDPTCD.getText().trim() +"'"
					+ " AND CMT_PRTNO = 'D'"
					+ " AND CMT_REMTP = '"+ P_strREMTP +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
				else if(P_strOREMDS.length() == 0) 
				{
					M_strSQLQRY = "Insert into CO_CMTRN(CMT_CMPCD,CMT_SBSCD,CMT_CMPTP,CMT_REGNO,CMT_PRTNO,CMT_REMTP,CMT_DPTCD,"
					+ "CMT_REMBY,CMT_REMDT,CMT_REMDS,CMT_CPAFL,CMT_TRNFL,CMT_STSFL,CMT_LUSBY,CMT_LUPDT)"
					+ " Values ( '"+cl_dat.M_strCMPCD_pbst+"','"+ M_strSBSCD +"',"
					+"'"+ strCMPTP +"',"
					+"'"+ txtREGNO.getText().trim() +"',"
					+"'D',"
					+"'"+ P_strREMTP +"',"
					+"'"+ txtDPTCD.getText().trim() +"',"
					+"'"+ P_strUSRCD +"',"
					+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()))+"',"
					+"'"+ P_strREMDS +"',"
					+"'',";
					if(cmbYESNO.getSelectedItem().toString().equals("No"))
						M_strSQLQRY += getUSGDTL("CMT",'I',"P") + ")";
					else if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
						M_strSQLQRY += getUSGDTL("CMT",'I',"C") + ")";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					if((cl_dat.M_flgLCUPD_pbst == true) && (cmbYESNO.getSelectedItem().toString().equals("Yes")))
					{
						updSTSFL(txtREGNO.getText().trim(),strCMPTP,P_strREMTP);
						strEML = "Yes";
					}
					else
						strEML ="No";

					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
				else
				{
					M_strSQLQRY = "update CO_CMTRN set CMT_REMDS ='"+ P_strREMDS +"',"
					+ " CMT_DPTCD = '"+ txtDPTCD.getText().trim() +"',";
					if(cmbYESNO.getSelectedItem().toString().equals("No"))
						M_strSQLQRY += " CMT_STSFL ='P',";
					else if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
						M_strSQLQRY +=  " CMT_STSFL ='C',";
					M_strSQLQRY += " CMT_TRNFL = '',"
					+ "	CMT_LUSBY ='"+ P_strUSRCD+"',"
					+ "	CMT_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+ " where CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_REMTP = '"+ P_strREMTP +"' AND CMT_CMPTP = '"+ strCMPTP+"'"
					+ " AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"
					+ " AND CMT_PRTNO = 'D'"
					+ " AND CMT_DPTCD = '"+ txtDPTCD.getText().trim() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if((cl_dat.M_flgLCUPD_pbst == true) && (cmbYESNO.getSelectedItem().toString().equals("Yes")))
					{
						updSTSFL(txtREGNO.getText().trim(),strCMPTP,P_strREMTP);
						strEML = "Yes";
					}
					else
						strEML ="No";					
					
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"modREMDS");
		}
	}
	/**
	 * Method to update the Status flag in the CO_CMMST to notify that the 
	 * Investigation is Completed & forward the Complaint for CAPA.
	 */
	void updSTSFL(String P_strREGNO,String P_strCMPTP,String P_strREMTP)
	{
		try
		{
			//String L_strCPABY = "";
			String L_strTEMP = "";
			String L_strSTSFL ="";
			/*M_strSQLQRY = "Select CM_CPABY from CO_CMMST where CM_STSFL >= '5'" 
				+" AND isnull(CM_STSFL,'')<>'X'"
				+" AND CM_CMPTP  ='"+ P_strCMPTP +"'"
				+" AND CM_REGNO  ='"+ P_strREGNO +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())				
					L_strCPABY = nvlSTRVL(M_rstRSSET.getString("CM_CPABY"),"");
				M_rstRSSET.close();
			}
			*/
			M_strSQLQRY = "Select CMT_DPTCD from CO_CMTRN where" // dpt completed CAPA
				+" CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"
				+" AND CMT_CMPTP = '"+ strCMPTP +"'"
				+" AND CMT_REMTP = 'PARTD'"
				+" AND CMT_PRTNO = 'D'"
				+" AND CMT_STSFL = 'C'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				L_strTEMP = "";
				String L_strDPTCD="";
				while(M_rstRSSET.next())
				{
					L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
					if(L_strTEMP.length() != 0)
							L_strTEMP +=",";
					L_strTEMP += "'"+ L_strDPTCD +"'";
				}
				M_rstRSSET.close();
			}
			
			if((L_strTEMP.equals(strCPABY)) ||(strCPABY.equals("")))
				L_strSTSFL = "9";
			else 
				L_strSTSFL = "8";

			// update Status Flag				
			M_strSQLQRY = "update CO_CMMST set CM_STSFL ='"+ L_strSTSFL +"'"
				+ " where CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CM_STSFL,'')<>'X' AND CM_REGNO ='"+ P_strREGNO+ "'"
				+ " AND CM_CMPTP = '"+ P_strCMPTP +"'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updSTSFL");
		}		
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				ResultSet L_rstRSSET;				
			/*	if((input == txtREGNO) && (txtREGNO.getText().trim().length() == 8))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strSQLQRY =" SELECT distinct CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM from CO_CMMST,CO_CMTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"
						+" AND CM_SBSCD ='"+ M_strSBSCD +"' AND isnull(CM_STSFL,'')<>'X'"
						+" AND CM_STSFL ='2'"
						+" AND CMT_STSFL <> 'C' AND CM_CMPTP = CMT_CMPTP AND CM_REGNO = CMT_REGNO";						
						M_strSQLQRY += " AND CM_REGNO = '"+txtREGNO.getText().trim()+"'";
					}
					else 
					{
						M_strSQLQRY =" SELECT distinct CMT_REGNO from CO_CMMST,CO_CMTRN where"
						+" isnull(CM_STSFL,'')<>'X' AND CM_SBSCD ='"+ M_strSBSCD +"' AND isnull(CM_STSFL,'')<>'X'"
						+" AND CMT_REGNO = CM_REGNO AND CMT_CMPTP = '"+ strCMPTP+"'"
						+" AND CMT_CMPTP = CM_CMPTP AND CMT_REMTP = 'PARTB'"
						+" AND CMT_STSFL <> 'X' AND CM_CMPTP = CMT_CMPTP AND CM_REGNO = CMT_REGNO";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							M_strSQLQRY += " AND CMT_STSFL <> 'C'";
						else 
							M_strSQLQRY += " AND CMT_STSFL in('C','P')";
						if(txtDPTCD.getText().length() == 3)
							M_strSQLQRY += " AND CMT_DPTCD = '"+txtDPTCD.getText().trim()+"'";						
						M_strSQLQRY += " AND CMT_REGNO ='"+ txtREGNO.getText().trim() +"'";
					}					
					L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if (L_rstRSSET != null)
					{
						if(L_rstRSSET.next())
						{
							L_rstRSSET.close();
							return true;
						}	
						else
						{
							L_rstRSSET.close();
							setMSG( "Invalid Registration Number, Press F1 to select from List ..",'E');
							return false;
						}
					}
				}				*/
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"InputVerifier");
			}
			return true;
		}
	}
}
/*int L_intCOUNT = 0;
			String L_strSTSFL = "";
			M_strSQLQRY="Select CMT_REGNO from CO_CMTRN where"
			+" CMT_REGNO ='"+ P_strREGNO +"'"
			+" AND CMT_CMPTP = '"+ P_strCMPTP+"'"
			+" AND CMT_REMTP = 'PARTC'"
			+" AND CMT_PRTNO = 'C'"
			+" AND CMT_SBSCD = '"+ M_strSBSCD +"'"
			+" AND CMT_STSFL = 'C'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())				
					L_strSTSFL = "9";
				else
					L_strSTSFL = "8";				
				M_rstRSSET.close();
			}								
			M_strSQLQRY="Select Count(CMT_STSFL) STSFL from CO_CMTRN where CMT_REGNO ='"+ P_strREGNO +"'"
				+" AND CMT_CMPTP = '"+ P_strCMPTP+"' AND CMT_REMTP = 'PARTD' AND CMT_SBSCD ='" + M_strSBSCD + "' AND CMT_STSFL = 'C'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next());
				{
					L_intCOUNT = M_rstRSSET.getInt("STSFL");
					if(intNODPT == L_intCOUNT)
					{
						M_strSQLQRY = "update CO_CMMST set CM_STSFL ='"+ L_strSTSFL +"'"
						+ " where isnull(CM_STSFL,'')<>'X' AND CM_REGNO ='"+ P_strREGNO+ "'"
						+ " AND CM_CMPTP = '"+ P_strCMPTP +"' AND CM_SBSCD = '"+ M_strSBSCD +"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}
				M_rstRSSET.close();
			}*/
/**
	 * Method to get the list of Deptartments Selected for the investigation.
	 * @param P_strREGNO String argument to pass the Registration Number.
	 * @param P_strCMPTP String argumnet to pass the Complaint Type.
	 */
	/*private String getDPTLT(String P_strREGNO,String P_strCMPTP)
	{
		String L_strTEMP = "";
		strCPABY="";
		try
		{
			M_strSQLQRY = "Select CM_CPABY from CO_CMMST where CM_STSFL >= '5'" 
			+" AND isnull(CM_STSFL,'')<>'X'"
			+" AND CM_CMPTP  ='"+ P_strCMPTP +"'"
			+" AND CM_REGNO  ='"+ P_strREGNO +"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())				
					strCPABY = nvlSTRVL(M_rstRSSET.getString("CM_CPABY"),"");
				M_rstRSSET.close();
			}
			intNODPT = 0;
			StringTokenizer L_stkTEMP = new StringTokenizer(strCPABY,"_");
			while(L_stkTEMP.hasMoreTokens())
			{
				if(L_strTEMP.length() != 0)
					L_strTEMP +=",";
				L_strTEMP += "'"+ L_stkTEMP.nextToken() +"'";
				intNODPT++;
			}
		//	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		//	{
		//		M_strSQLQRY = " Select CMT_DPTCD from CO_CMTRN where CMT_REGNO ='"+ txtREGNO.getText().trim()+"'"
		//		+" AND CMT_CMPTP =+'" + strCMPTP + "' AND CMT_REMTP ='PARTD' AND CMT_PRTNO='D' "
		//		+" AND CMT_DPTCD not in("+ L_strTEMP +")";
		//		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		//		if(M_rstRSSET != null)
		//		{
		//			while(M_rstRSSET.next())				
		//				L_strTEMP +=",'"+ nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"") +"'";
		//			M_rstRSSET.close();
		//		}
		//	}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDPTLT");
		}
		return L_strTEMP;
	}*/