/*
System Name   : Material Management System
Program Name  : Customer Complaint
Program Desc. : Entry screen to enter the B part.
Author        : Mr S.R.Mehesare
Date          : 15.12.2005
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
class co_teccl extends cl_pbase
{
    private JPanel pnlPARTC;    
    private JTextArea txaREMDS;
	private JComboBox cmbCMPTP;
    private JTextField txtREGNO;
	private JTextField txtBRNRG;
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
	private JLabel lblSTATS;
	private JComboBox cmbYESNO;
		
	private String strOREMDS="";
	private String strCMPTP;
	private Hashtable<String,String> hstDPTDS;	
	private String strCPABY;
	private String strFINFL = "";
	
	private INPVF objINPVR = new INPVF();
	private String strFILNM = "";
	private String strEML = "";
	private String strREGNO = "";
    co_teccl()
    {
        super(2);
		try
		{
			setMatrix(20,8);			
			pnlPARTC = new JPanel();
			pnlPARTC.setLayout(null);
		  
			add(new JLabel("Complaint Type"),2,1,1,2,this,'L');
			add(cmbCMPTP = new JComboBox(),2,2,1,1.3,this,'L');		
			add(new JLabel("Reg. No"),2,3,1,.6,this,'R');
			add(txtREGNO = new TxtNumLimit(8),2,4,1,1,this,'L');
			add(new JLabel("Brach Reg."),2,5,1,.8,this,'R');
			add(txtBRNRG = new JTextField(),2,6,1,1,this,'L');			
		
			add(new JLabel("Reported On"),3,1,1,1,this,'L');
			add(txtREGDT = new JTextField(),3,2,1,1.3,this,'L');
			add(new JLabel("Dept"),3,3,1,.6,this,'R');
			add(txtDPTCD = new JTextField(),3,4,1,1,this,'L');  
			add(txtDPTNM = new JTextField(),3,5,1,4,this,'L');  
		
			add(new JLabel("Closer Date"),4,1,1,1,this,'L');
			add(txtDATE = new JTextField(),4,2,1,1.3,this,'L');	    
			add(new JLabel("Party"),4,3,1,.6,this,'R');
			add(txtPRTCD = new JTextField(),4,4,1,1,this,'L');  
			add(txtPRTNM = new JTextField(),4,5,1,4,this,'L');  
		
			add(new JLabel("Comp. Closed"),5,1,1,1.5,this,'L');
			add(cmbYESNO = new JComboBox(),5,2,1,1.3,this,'L'); 
			add(new JLabel("Report"),5,3,1,.6,this,'R');
			add(btnPRTA = new JButton("PART A"),5,4,1,1,this,'L'); 
			add(btnPRTB = new JButton("PART B"),5,5,1,1,this,'L');			
			add(btnPRNT = new JButton("PART C"),5,6,1,1,this,'L');  
			
			add(new JLabel("Status : "),6,1,1,1,this,'L');
			add(lblSTATS = new JLabel(""),6,2,1,4,this,'L'); 
			lblSTATS.setForeground(Color.blue);
					 
			cmbYESNO.addItem("No");
			cmbYESNO.addItem("Yes");

			add(new JLabel("Recommomdations / Resolution of Complaint"),1,1,1,6,pnlPARTC,'L');
			txaREMDS = new JTextArea();
			add(new JScrollPane(txaREMDS,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),2,1,10,7.5,pnlPARTC,'L');
			add(pnlPARTC,6,1,12,8,this,'L');
			
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
			setENBL(false);
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
		txtBRNRG.setEnabled(false);
		txtREGDT.setEnabled(false);
		txtPRTCD.setEnabled(false);
		txtPRTNM.setEnabled(false);		
		txtDATE.setEnabled(false);
		txtDPTCD.setEnabled(false);
		txtDPTNM.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) 
		{
			txaREMDS.setEnabled(false);
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
				strCPABY = "";
				java.sql.Date L_tmpDATE;
				if(txtREGNO.getText().trim().length() == 8)
				{					
					M_strSQLQRY = "select CM_PRTCD,CM_PRTNM,CM_BRNRG,CM_REPDT,CM_FINFL,CM_CPABY from CO_CMMST where"
						+" CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CM_CMPTP ='"+ strCMPTP +"'"
						+" AND CM_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CM_STSFL,'')<>'X'";
					M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(M_rstRSSET != null)
					{						
						if(M_rstRSSET.next())
						{	
							txtDATE.setText(cl_dat.M_strLOGDT_pbst);
							txtBRNRG.setText(nvlSTRVL(M_rstRSSET.getString("CM_BRNRG"),""));
							txtPRTCD.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTCD"),""));
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),""));
							strCPABY = nvlSTRVL(M_rstRSSET.getString("CM_CPABY"),"");
							L_tmpDATE = M_rstRSSET.getDate("CM_REPDT");
								if(L_tmpDATE != null)
							txtREGDT.setText(M_fmtLCDAT.format(L_tmpDATE));	
							strFINFL = M_rstRSSET.getString("CM_FINFL");
							if(strFINFL.equals("Y"))						
								btnPRTB.setEnabled(true);
							else 
								btnPRTB.setEnabled(false);
							txtDPTCD.setText("605");
							if(hstDPTDS.containsKey("605"))
								txtDPTNM.setText(hstDPTDS.get("605").toString());
						}
					}
					strREGNO = txtREGNO.getText().trim();
				}
				if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)) || (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
				{
					M_strSQLQRY = "select CMT_REMDS,CMT_DPTCD,CMT_REMDT,CMT_STSFL from CO_CMMST,CO_CMTRN where"
					+" CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CMPTP ='"+ strCMPTP +"'"
					+" AND CMT_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CMT_STSFL,'')<>'X'"
					+" AND CMT_PRTNO = 'C' AND CMT_REMTP = 'PARTC' AND CMT_REGNO = CM_REGNO";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
						M_strSQLQRY	 += " AND CMT_STSFL ='P'";
					else
						M_strSQLQRY	 += " AND CMT_STSFL in('P','C')";
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
							L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
							txtDPTCD.setText(L_strDPTCD);							
							if(hstDPTDS.containsKey(L_strDPTCD))
								txtDPTNM.setText(hstDPTDS.get(L_strDPTCD).toString());
							txaREMDS.setText(strOREMDS);
							String L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("CMT_STSFL"),"");
							if(L_strSTSFL.equals("C"))
							{
								cmbYESNO.setSelectedItem("Yes");
								lblSTATS.setText("Details Entered & Completed");
							}
							else if(L_strSTSFL.equals("P"))
							{
								txaREMDS.requestFocus();
								cmbYESNO.setSelectedItem("No");
								lblSTATS.setText("Details Entered but Not Completed");
							}
						}
						else // OPENQ
							lblSTATS.setText("Details not Entered");
						M_rstRSSET.close();
					}
				}
				if(txtREGNO.getText().trim().length() == 8)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						txaREMDS.requestFocus();
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
			else if(M_objSOURC == btnPRNT)
			{
				if(txtREGNO.getText().trim().length() != 8)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to generate the Report..",'E');
					return;
				}				
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasC.html";
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"C");
				
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
						M_strSQLQRY ="SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM FROM CO_CMMST"
						+" WHERE CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (CM_FINFL = 'N' OR CM_STSFL >= '5')"
						+" AND CM_REGNO NOT IN(SELECT CMT_REGNO FROM CO_CMTRN WHERE CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_PRTNO ='C')";
					}
					else 
					{
						M_strSQLQRY ="SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM FROM CO_CMMST"
						+" WHERE CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (CM_FINFL = 'N' OR CM_STSFL >= '5')"
						+" AND CM_REGNO IN(SELECT CMT_REGNO FROM CO_CMTRN WHERE CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_PRTNO ='C'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							M_strSQLQRY += " AND CMT_STSFL ='P')";
						else 
							M_strSQLQRY += " AND CMT_STSFL in('P','C'))";
					}
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Reg. No.","Branch Reg.No.","Reg.Date","Party Code","Party Name"},5,"CT");
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
			cl_dat.M_flgHELPFL_pbst = false;			
			txtREGNO.setText(cl_dat.M_strHLPSTR_pbst);
			txtBRNRG.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
			txtREGDT.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));			
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
			{
				modREMDS("PARTC",txaREMDS.getText().trim(),"",cl_dat.M_strUSRCD_pbst);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				modREMDS("PARTC",txaREMDS.getText().trim(),"strOREMDS",cl_dat.M_strUSRCD_pbst);			
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				modREMDS("PARTC","","strOREMDS",cl_dat.M_strUSRCD_pbst);
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{		
				if(cmbYESNO.getSelectedItem().toString().equals("Yes"))
					strEML = "Yes";
				else
					strEML = "No";
				clrCOMP();				
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
				strEML = "No";
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasC.html";
				cl_eml ocl_eml = new cl_eml();
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,strREGNO);
				obj.getDATA(M_strSBSCD,strCMPTP,strREGNO,"C");
				
				M_strSQLQRY =" SELECT CMT_CODDS from CO_CDTRN where"
					+" isnull(CMT_STSFL,'')<>'X'"	
					+" AND CMT_CGMTP ='EML' AND CMT_CGSTP ='COXXCAS'"
					+" AND CMT_CODCD like '"+ strCMPTP+ "C%'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
						ocl_eml.sendfile(M_rstRSSET.getString("CMT_CODDS").trim()+"@spl.co.in",strFILNM,"Customer Complaint Closing"," ");
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
					+ " AND CMT_PRTNO = 'C'";
					//+ " AND CMT_REMTP = '"+ P_strREMDS+"'";
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
					+ " Values ( '"+ cl_dat.M_strCMPCD_pbst +"','"+ M_strSBSCD +"',"
					+"'"+ strCMPTP +"',"
					+"'"+ txtREGNO.getText().trim() +"',"
					+"'C',"
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
					if((cmbYESNO.getSelectedItem().toString().equals("Yes")) && (cl_dat.M_flgLCUPD_pbst == true))
					{
						updSTSFL(txtREGNO.getText().trim(),strCMPTP,P_strREMTP);
					}

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
					+ " AND CMT_PRTNO = 'C'"
					+ " AND CMT_DPTCD = '"+ txtDPTCD.getText().trim() +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if((cmbYESNO.getSelectedItem().toString().equals("Yes")) && (cl_dat.M_flgLCUPD_pbst == true))
					{
						updSTSFL(txtREGNO.getText().trim(),strCMPTP,P_strREMTP);
					}
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
			String L_strTEMP = "";
			String L_strSTSFL ="";
			
			M_strSQLQRY = "Select CMT_DPTCD from CO_CMTRN where" // dpt completed CAPA
				+" CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"'"				
				+" AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"
				+" AND CMT_CMPTP = '"+ strCMPTP +"'"
				+" AND CMT_REMTP = 'PARTD'"
				+" AND CMT_PRTNO = 'D'"
				+" AND CMT_STSFL = 'C'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				L_strTEMP = "";
				while(M_rstRSSET.next())
					L_strTEMP += nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"")+"_";
				M_rstRSSET.close();
			}
			if((L_strTEMP.equals(strCPABY)) ||(strCPABY.equals("")))
				L_strSTSFL = "9";
			else 
				L_strSTSFL = "7";

			// update Status Flag				
			M_strSQLQRY = "update CO_CMMST set CM_STSFL ='"+ L_strSTSFL +"'"
				+ " where CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(CM_STSFL,'')<>'X' AND CM_REGNO ='"+ P_strREGNO+ "'"
				+ " AND CM_CMPTP = '"+ P_strCMPTP +"' ";
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
				if((input == txtREGNO) && (txtREGNO.getText().trim().length() == 8))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strSQLQRY ="SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM FROM CO_CMMST"
						+" WHERE CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (CM_FINFL = 'N' OR CM_STSFL >= '5')"
						+" AND CM_REGNO NOT IN(SELECT CMT_REGNO FROM CO_CMTRN WHERE CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_PRTNO ='C')"
						+" AND CM_REGNO = '"+ txtREGNO.getText().trim() +"'";
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
								setMSG( "Details are already entered, try to modify it through Modification..",'E');
								return false;
							}
						}
					}
					else 
					{
						M_strSQLQRY ="SELECT CM_REGNO,CM_BRNRG,CM_REPDT,CM_PRTCD,CM_PRTNM FROM CO_CMMST"
						+" WHERE CM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND (CM_FINFL = 'N' OR CM_STSFL >= '5')"
						+" AND CM_REGNO IN(SELECT CMT_REGNO FROM CO_CMTRN WHERE CMT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_PRTNO ='C'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							M_strSQLQRY += " AND CMT_STSFL ='P')";
						else 
							M_strSQLQRY += " AND CMT_STSFL in('P','C'))";
						M_strSQLQRY +=" AND CM_REGNO = '"+ txtREGNO.getText().trim() +"'";
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
								setMSG("Details are entered & Complaint is already closed..",'E');
								return false;
							}
						}
					}					
				}
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"InputVerifier..");
			}
			return true;
		}
	}				
}

/*if(cl_dat.exeDBCMT("exeSAVE"))
				setMSG("Status Flag Updated Successsfully ..",'N');
			else
				setMSG("Error in updating Flag..",'E');
			*/	
						
			/*int L_intCOUNT = 0;
			String L_strSTSFL = "";
			String L_strDPTLT = "";
			StringTokenizer L_stkTEMP;
			if(strFINFL.equals("Y"))
			{
				L_strDPTLT = getDPTLT(txtREGNO.getText().trim(),strCMPTP);// selected dpt for CAPA
				L_stkTEMP = new StringTokenizer(L_strDPTLT,",");
				while(L_stkTEMP.hasMoreTokens())
				{
					L_stkTEMP.nextToken();
					L_intCOUNT++;			
				}	
				M_strSQLQRY = "Select CM_CPABY from CO_CMMST where CM_STSFL >= '5'" 
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
				
				M_strSQLQRY = "Select CMT_DPTCD from CO_CMTRN where" // dpt completed CAPA
				+" CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"
				+" AND CMT_CMPTP = '"+ strCMPTP +"'"
				+" AND CMT_REMTP = 'PARTD'"
				+" AND CMT_PRTNO = 'D'"
				+" AND CMT_SBSCD = '"+ M_strSBSCD +"'"				
				+" AND CMT_STSFL = 'C'";				
//				+" AND CMT_DPTCD in("+ L_strDPTLT +")";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{					
					while(M_rstRSSET.next())
					{
						
						
						intTEMP = M_rstRSSET.getInt("CNT");
						if(L_intCOUNT == intTEMP)
							L_strSTSFL = "9";
						else 
							L_strSTSFL = "7";
					}
				}
				M_strSQLQRY="Select Count(CMT_STSFL) STSFL from CO_CMTRN where CMT_REGNO ='"+ P_strREGNO +"'"
					+" AND CMT_CMPTP = '"+ P_strCMPTP+"' AND CMT_REMTP = 'PARTB' AND CMT_STSFL = 'C'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next());
					{					
						M_strSQLQRY = "update CO_CMMST set CM_STSFL ='"+ L_strSTSFL +"'"
						+ " where isnull(CM_STSFL,'')<>'X' AND CM_REGNO ='"+ P_strREGNO+ "'"
						+ " AND CM_CMPTP = '"+ P_strCMPTP +"' AND CM_SBSCD = '"+ M_strSBSCD +"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
					}
					M_rstRSSET.close();
				}
			}
			else
			{
				/// if B part not Requried.
				M_strSQLQRY = "Select CM_REGNO from CO_CMMST where" // dpt completed CAPA
				+" CM_REGNO = '"+ txtREGNO.getText().trim() +"'"
				+" AND CM_CMPTP = '"+ strCMPTP +"'"
				+" AND CM_SBSCD = '"+ M_strSBSCD +"'"
				+" AND isnull(CM_STSFL,'')<> 'X'";				
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{				
					if(M_rstRSSET.next());
					{					
						M_strSQLQRY = "update CO_CMMST set CM_STSFL ='9'"
						+ " where isnull(CM_STSFL,'')<>'X' AND CM_REGNO ='"+ P_strREGNO+ "'"
						+ " AND CM_CMPTP = '"+ P_strCMPTP +"' AND CM_SBSCD = '"+ M_strSBSCD +"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
					}
					M_rstRSSET.close();
				}
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
			StringTokenizer L_stkTEMP = new StringTokenizer(strCPABY,"_");
			while(L_stkTEMP.hasMoreTokens())
			{
				if(L_strTEMP.length() != 0)
					L_strTEMP +=",";
				L_strTEMP += "'"+ L_stkTEMP.nextToken()+"'";				
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDPTLT");
		}
		return L_strTEMP;
	}*/
/*	if(M_objSOURC == txtDPTCD)
				{
					M_strHLPFLD = "txtDPTCD";
					cl_dat.M_flgHELPFL_pbst = true;
					setCursor(cl_dat.M_curWTSTS_pbst);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where"
						+" CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' AND isnull(CMT_STSFL,'')<>'X'";						
					}
					else 
					{
						M_strSQLQRY ="SELECT CMT_CODCD,CMT_CODDS from CO_CMTRN where isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_REGNO ='"+ txtREGNO.getText().trim()+"'";
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
							M_strSQLQRY +=" AND CMT_STSFL <>'C'";
						else 
							M_strSQLQRY +=" AND CMT_STSFL in('C','P')";						
					}
					cl_hlp(M_strSQLQRY,2,1,new String[]{"Dept. Code","Description"},2,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}*/
/*	else if(M_strHLPFLD.equals("txtDPTCD"))
		{			
			cl_dat.M_flgHELPFL_pbst = false;			
			txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
			txtDPTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)));
		}*/