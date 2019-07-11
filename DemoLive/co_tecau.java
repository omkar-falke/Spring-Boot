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
import javax.swing.JTextArea;import javax.swing.JLabel;import javax.swing.JPanel;import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;import javax.swing.JEditorPane;import javax.swing.JButton;
import javax.swing.JTextField;import javax.swing.JTextArea;import javax.swing.JTabbedPane;
import javax.swing.JComboBox;import java.sql.ResultSet;import javax.swing.JComponent;
import javax.swing.InputVerifier;import java.awt.Color;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.util.Hashtable;import java.util.Vector;import java.util.StringTokenizer;

class co_tecau extends cl_pbase
{
	private JPanel pnlAUTDL;
	private JPanel pnlINVDL;	
	private JComboBox cmbCMPTP;	
	private JTextField txtREGNO;	
	private JTextField txtPRTNM;
	private JCheckBox chkCAPA;
	private cl_JTable tblDPTCD;		
	
	private JTextArea txaAUTDL;
	private JTextArea txaDPT1;
	private JTextArea txaDPT2;
	private JTextArea txaDPT3;
	private JTextArea txaDPT4;
	//private JTextArea txaCOMED;
	private JLabel lbl1,lbl2,lbl3,lbl4,lbl5;	
	private JTabbedPane jtbMAIN;
	JButton btnPRTA;
	JButton btnPRTB;
	
	private String strCMPTP;
	
	int intROWCT = 40;
	int intOPIND = 0;
	
	private final int TBL_CHKFL = 0;
	private final int TBL_DPTCD = 1;
	private final int TBL_DPTNM = 2;
	
	String strOREM1="";
	String strOREM2="";
	String strOREM3="";
	String strOREM4="";
	String strDPT1="";
	String strDPT2="";
	String strDPT3="";
	String strDPT4="";
	String strAUREM="";
	private Hashtable<String,String> hstDPTDS;
	String strEML = "No";
	String strFILNM = "";
	String strCPABY = "";
	String strREGNO = "";
	Vector<String> L_vtrDPTCD=new Vector<String>(); 	
					
	co_tecau()
    {
        super(2);
		try
		{
			setMatrix(20,8);
			jtbMAIN = new JTabbedPane();
			txaAUTDL = new JTextArea();
			txaDPT1 = new JTextArea();
			txaDPT2 = new JTextArea();
			txaDPT3 = new JTextArea();
			txaDPT4 = new JTextArea();
			
			pnlAUTDL = new JPanel();pnlAUTDL.setLayout(null);
		    pnlINVDL = new JPanel();pnlINVDL.setLayout(null);
		    
			add(new JLabel("Complaint Type"),1,1,1,1.2,this,'L');
			add(cmbCMPTP = new JComboBox(),1,2,1,1.2,this,'L');			
		    add(new JLabel("Reg. No."),1,3,1,.6,this,'R');
		   	add(txtREGNO = new TxtNumLimit(8),1,4,1,1,this,'L');
		    add(new JLabel("Party Name"),1,5,1,.8,this,'R');
		   	add(txtPRTNM = new JTextField(),1,6,1,2.8,this,'L');
			
			add(btnPRTA = new JButton("Part A"),2,4,1,1,this,'L');
			add(btnPRTB = new JButton("Part B"),2,5,1,1,this,'L');
	
			add(lbl5 = new JLabel("Authorization Remarks"),1,2,1,2,pnlAUTDL,'L');
			add(new JScrollPane(txaAUTDL,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),2,2,6,6,pnlAUTDL,'L');
			
			add(chkCAPA = new JCheckBox("CAPA Required"),9,2,1,2,pnlAUTDL,'L');			
			String[] arrCOLHD = {"CAPA Req.","Dept.Code","Department Name"};
      		int[] arrCOLSZ = {80,82,200};	    				
			tblDPTCD = crtTBLPNL1(pnlAUTDL,arrCOLHD,intROWCT,10,2,5,4,arrCOLSZ,new int[]{0});				
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);			
						
			add(lbl1 = new JLabel("Investigation Details"),1,2,1,6,pnlINVDL,'L');
			add(new JScrollPane(txaDPT1,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),2,2,3,6,pnlINVDL,'L');			
		  
			add(lbl2 = new JLabel("Investigation Details"),5,2,1,4,pnlINVDL,'L');
			add(new JScrollPane(txaDPT2,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),6,2,3,6,pnlINVDL,'L');

			add(lbl3 = new JLabel("Investigation Details"),9,2,1,4,pnlINVDL,'L');
			add(new JScrollPane(txaDPT3,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),10,2,3,6,pnlINVDL,'L');
			
			add(lbl4 = new JLabel("Investigation Details"),13,2,1,4,pnlINVDL,'L');
			add(new JScrollPane(txaDPT4,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ),14,2,3,6,pnlINVDL,'L');
	
			
			jtbMAIN.addTab("Authorization Details",pnlAUTDL);
		   	jtbMAIN.addTab("Investigation Details",pnlINVDL);		    
			add(jtbMAIN,2,1,17,8,this,'L');
			setENBL(false);
			
			M_strSQLQRY ="SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
				+" AND CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				String L_strCODCD="",L_strCODDS="";
				hstDPTDS = new Hashtable<String,String>();
				while(M_rstRSSET.next())
				{
					L_strCODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_strCODDS = nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"");
					hstDPTDS.put(L_strCODCD,L_strCODDS);
				}
				M_rstRSSET.close();
			}	
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
		txtPRTNM.setEnabled(false);
		//tblDPTCD.cmpEDITR[TBL_CHKFL].setEnabled(false);
    	tblDPTCD.cmpEDITR[TBL_DPTNM].setEnabled(false);
		tblDPTCD.cmpEDITR[TBL_DPTCD].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			txaAUTDL.setEnabled(false);
			txaDPT1.setEnabled(false);
			txaDPT2.setEnabled(false);
			txaDPT3.setEnabled(false);
			txaDPT4.setEnabled(false);
			chkCAPA.setEnabled(false);
		}
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
						cmbCMPTP.setSelectedItem("02 Technical");
					}
					if(intOPIND == 0)
					{	
						if(tblDPTCD.isEditing())
							tblDPTCD.getCellEditor().stopCellEditing();
						
						tblDPTCD.setRowSelectionInterval(0,0);
						tblDPTCD.setColumnSelectionInterval(0,0);
						for(int j=0;j<intROWCT-1;j++)					
							tblDPTCD.setValueAt(new Boolean(false), j,TBL_CHKFL);
						chkCAPA.setSelected(false);
						M_strSQLQRY ="SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where isnull(CMT_STSFL,'')<>'X'"
							+" AND CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							int i = 0;
							while(M_rstRSSET.next())
							{
								tblDPTCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),i,TBL_DPTCD);								
								tblDPTCD.setValueAt(nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),i,TBL_DPTNM);								
								i++;
							}
							M_rstRSSET.close();
						}
					}										
					strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);					
					setENBL(true);					
					txtREGNO.requestFocus();
				}
				else
					setENBL(false);
				
				intOPIND = cl_dat.M_cmbOPTN_pbst.getSelectedIndex();
			}
			else if(M_objSOURC == cmbCMPTP)
			{
				strCMPTP = cmbCMPTP.getSelectedItem().toString().substring(0,2);
				tblDPTCD.setRowSelectionInterval(0,0);
				tblDPTCD.setColumnSelectionInterval(0,0);
				for(int j=0;j<intROWCT-1;j++)					
					tblDPTCD.setValueAt(new Boolean(false), j,TBL_CHKFL);
				chkCAPA.setSelected(false);
			}
			else if(M_objSOURC == txtREGNO)
			{
					strREGNO = txtREGNO.getText().trim();
					txaAUTDL.setText("");
					txaDPT1.setText("");
					txaDPT2.setText("");
					txaDPT3.setText("");
					txaDPT4.setText("");
					lbl1.setText("");
					lbl2.setText("");
					lbl3.setText("");
					lbl4.setText("");
					java.sql.Date L_tmpDATE;
					String L_strCPABY = "";
					String L_strSTSFL = "";
					
				    /////////////////////////////////////////////////////////////////////////////////////
					L_vtrDPTCD.removeAllElements();
					String L_strDPTCD_RQD="";
					int CNT=1;
					try
					{
						String L_strSQLQRY =" SELECT SUBSTRING(CMT_CODCD,4,3) CMT_CODCD from CO_CDTRN where"
						+" CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXIDP' AND SUBSTRING(CMT_CODCD,1,2) = '"+ strCMPTP +"'  order by CMT_CHP01";
						//System.out.println("L_strSQLQRY>>>>"+L_strSQLQRY);
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
						if(L_rstRSSET!=null)
						{
							while(L_rstRSSET.next())
							{	
								L_vtrDPTCD.add(L_rstRSSET.getString("CMT_CODCD"));
								if(hstDPTDS.containsKey(L_rstRSSET.getString("CMT_CODCD")))
									L_strDPTCD_RQD=hstDPTDS.get(L_rstRSSET.getString("CMT_CODCD")).toString();
								if(CNT==1)
									lbl1.setText(L_strDPTCD_RQD);
								else if(CNT==2)
									lbl2.setText(L_strDPTCD_RQD);
								else if(CNT==3)
									lbl3.setText(L_strDPTCD_RQD);
								else if(CNT==4)
									lbl4.setText(L_strDPTCD_RQD);
								CNT++;
							}
						}				
					}
					catch(Exception L_EX){
						setMSG(L_EX,"fetching EXPFL()");
					}		
					//////////////////////////////////////////////////////////////////
						
					
					M_strSQLQRY = "select CM_PRTNM,CM_CPABY,CM_STSFL from CO_CMMST where CM_SBSCD = '"+ M_strSBSCD+"'"
					+" AND CM_CMPTP ='"+ strCMPTP +"'"
					+" AND CM_REGNO = '"+ txtREGNO.getText().trim()+"' AND isnull(CM_STSFL,'')<>'X'";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					
					M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
						{							
							txtPRTNM.setText(nvlSTRVL(M_rstRSSET.getString("CM_PRTNM"),""));
							L_strCPABY = nvlSTRVL(M_rstRSSET.getString("CM_CPABY"),"");
							L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("CM_STSFL"),"");
							
							chkCAPA.setSelected(false);
							for(int j=0;j<intROWCT-1;j++)
								tblDPTCD.setValueAt(new Boolean(false), j,TBL_CHKFL);
							chkCAPA.setSelected(false);
							if(!L_strCPABY.equals(""))
							{																											
								// To display the Depts Selected for CAPA
								StringTokenizer L_stkTEMP = new StringTokenizer(L_strCPABY,"_");
								String L_strTEMP = "";
								while(L_stkTEMP.hasMoreTokens())
								{
									L_strTEMP = L_stkTEMP.nextToken();
									for(int j=0;j<intROWCT-1;j++)
									{
										if(tblDPTCD.getValueAt(j,TBL_DPTCD).equals(L_strTEMP))
										{
											tblDPTCD.setValueAt(new Boolean(true), j,TBL_CHKFL);
											chkCAPA.setSelected(true);
											break;
										}
									}
								}
							}			
							
							if((Integer.valueOf(L_strSTSFL).intValue() >= 5) ||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)))
							{
								if(tblDPTCD.isEditing())
									tblDPTCD.getCellEditor().stopCellEditing();
								tblDPTCD.setRowSelectionInterval(0,0);
								tblDPTCD.setColumnSelectionInterval(0,0);
								txaAUTDL.setEnabled(false);
								txaDPT1.setEnabled(false);
								txaDPT2.setEnabled(false);
								txaDPT3.setEnabled(false);
								txaDPT4.setEnabled(false);
								chkCAPA.setEnabled(false);
							}
							else 
							{
								txaAUTDL.setEnabled(true);
								txaDPT1.setEnabled(true);
								txaDPT2.setEnabled(true);
								txaDPT3.setEnabled(true);
								txaDPT4.setEnabled(true);
								chkCAPA.setEnabled(true);
							}							
						}						
						M_rstRSSET.close();						
					}
					
					M_strSQLQRY = "select CMT_REMDS,CMT_DPTCD,CMT_REMBY,CMT_REMDT,CMT_REMTP,CMT_DPTCD,isnull(CMT_CPAFL,' ') CMT_CPAFL from CO_CMTRN where"
						+" CMT_SBSCD = '"+ M_strSBSCD+"'"
						+" AND isnull(CMT_STSFL,'')<>'X'"
						+" AND CMT_PRTNO = 'B'"
						+" AND CMT_REMTP in('PARTB','AUTRM')"
						+" AND CMT_CMPTP ='"+ strCMPTP +"'"
						+" AND CMT_REGNO = '"+ txtREGNO.getText().trim()+"'";
					//System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(M_rstRSSET != null)
					{			
						strOREM1=""; strOREM2=""; strOREM3="";
						String L_strREMTP="";
						int i = 0;
						String L_strREMDS="",L_strDPTCD="",L_strDPTDS="",L_strREMDT="",L_strREMBY="",L_strCPAFL="";
						String L_strDPT=""; strDPT1="";strDPT2="";strDPT3="";
						while(M_rstRSSET.next())
						{
							L_strREMDS = nvlSTRVL(M_rstRSSET.getString("CMT_REMDS"),"");
							L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
							L_strREMTP = nvlSTRVL(M_rstRSSET.getString("CMT_REMTP"),"");
							L_strDPT = nvlSTRVL(M_rstRSSET.getString("CMT_DPTCD"),"");
							if(hstDPTDS.containsKey(L_strDPTCD))
								L_strDPTDS = hstDPTDS.get(L_strDPTCD).toString();
							L_strREMBY = nvlSTRVL(M_rstRSSET.getString("CMT_REMBY"),"");
							L_strCPAFL = M_rstRSSET.getString("CMT_CPAFL");
							L_tmpDATE = M_rstRSSET.getDate("CMT_REMDT");
							if(L_tmpDATE != null)								
								L_strREMDT = M_fmtLCDAT.format(L_tmpDATE);
							else 
								L_strREMDT = "";
							
							if(L_strREMTP.equals("PARTB"))
							{
								/*if(i==0)
								{
									strOREM1 = L_strREMDS;
									txaDPT1.setText(L_strREMDS);
									strDPT1 = L_strDPT;
									lbl1.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
								}
								else if(i==1)
								{
									strDPT2 = L_strDPT;
									strOREM2 = L_strREMDS;
									txaDPT2.setText(L_strREMDS);
									lbl2.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
								}
								else if(i==2)
								{
									strDPT3 = L_strDPT;
									strOREM3 = L_strREMDS;
									txaDPT3.setText(L_strREMDS);
									lbl3.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
								}
								else if(i==3)
								{
									strDPT4 = L_strDPT;
									strOREM4 = L_strREMDS;
									txaDPT4.setText(L_strREMDS);
									lbl4.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
								}*/
								
								//System.out.println("L_vtrDPTCD.size()>>"+L_vtrDPTCD.size());
								for(int j=0;j<L_vtrDPTCD.size();j++)
								{
									//System.out.println("j>>"+j);
									//System.out.println("L_strDPTCD>>"+L_strDPTCD);
									//System.out.println("L_vtrDPTCD.elementAt(j)>>"+L_vtrDPTCD.elementAt(j));
									if(L_strDPTCD.equals(L_vtrDPTCD.elementAt(j)))
									{
										if(j==0)
										{
											strOREM1 = L_strREMDS;
											txaDPT1.setText(L_strREMDS);
											strDPT1 = L_strDPT;
											lbl1.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
										}
										if(j==1)
										{
											strDPT2 = L_strDPT;
											strOREM2 = L_strREMDS;
											txaDPT2.setText(L_strREMDS);
											lbl2.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
										}
										if(j==2)
										{
											strDPT3 = L_strDPT;
											strOREM3 = L_strREMDS;
											txaDPT3.setText(L_strREMDS);
											lbl3.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
										}
										if(j==3)
										{
											strDPT4 = L_strDPT;
											strOREM4 = L_strREMDS;
											txaDPT4.setText(L_strREMDS);
											lbl4.setText("Entered by  "+L_strREMBY+" for Dept. "+L_strDPTDS+" on Date "+L_strREMDT);
										}										
									}
								}
								i++;
							}
							else//REMTP == AUTRM
							{
								strAUREM = L_strREMDS;
								txaAUTDL.setText(L_strREMDS);
								lbl5.setText("Authorization Remarks entered by "+L_strREMBY+" on Date "+L_strREMDT);
								if(txaDPT1.getText().trim().length()==0)
									txaDPT1.setText("NOT APPLICABLE");
								if(txaDPT2.getText().trim().length()==0)
									txaDPT2.setText("NOT APPLICABLE");
								if(txaDPT3.getText().trim().length()==0)
									txaDPT3.setText("NOT APPLICABLE");
								if(txaDPT4.getText().trim().length()==0)
									txaDPT4.setText("NOT APPLICABLE");
							}
							
							/*for(int j=0;j<intROWCT-1;j++)
							{
								if(tblDPTCD.getValueAt(j,TBL_DPTCD).equals(L_strDPTCD))
								{
									tblDPTCD.setValueAt(new Boolean(true), j,TBL_CHKFL);
									chkCAPA.setSelected(true);
									break;
								}
							}*/
							
						}
					}					
			}
			else if(M_objSOURC == chkCAPA)
			{
				if(chkCAPA.isSelected())
					tblDPTCD.cmpEDITR[TBL_CHKFL].setEnabled(true);
				else
				{
					if(tblDPTCD.isEditing())
						tblDPTCD.getCellEditor().stopCellEditing();
					tblDPTCD.setRowSelectionInterval(0,0);
					tblDPTCD.setColumnSelectionInterval(0,0);
					for(int j=0;j<intROWCT-1;j++)					
						tblDPTCD.setValueAt(new Boolean(false), j,TBL_CHKFL);
					chkCAPA.setSelected(false);
					//tblDPTCD.cmpEDITR[TBL_CHKFL].setEnabled(false);
				}
			}			
			else if(M_objSOURC == btnPRTA)
			{
				/*if(txtREGNO.getText().trim().length() != 8)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to view Part A..",'E');
					return;
				}*/				
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasA.html";
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"A");
				
				Runtime r = Runtime.getRuntime();
				Process p = null;
				p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM);
			}
			else if(M_objSOURC == btnPRTB)
			{
				if(txtREGNO.getText().trim().length() == 0)
				{
					txtREGNO.requestFocus();
					setMSG("Please enter Registration Number to generate the Report..",'E');
					return;
				}
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasB.html";
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,txtREGNO.getText().trim());
				obj.getDATA_B(M_strSBSCD,strCMPTP,txtREGNO.getText().trim(),"B");
				
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
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						M_strSQLQRY =" SELECT CM_REGNO,CM_REPDT,CM_PRTNM from CO_CMMST where"
						+" isnull(CM_STSFL,'')<>'X'"
						+" AND CM_SBSCD ='"+ M_strSBSCD +"'"
						+" AND CM_STSFL in ('3','4')"
						+" AND CM_FINFL = 'Y'";
						if(txtREGNO.getText().length() >0)
							M_strSQLQRY += " AND CM_REGNO like '"+txtREGNO.getText().trim()+"%'";
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) 
					{
						M_strSQLQRY =" SELECT CM_REGNO,CM_REPDT,CM_PRTNM from CO_CMMST where"
						+" isnull(CM_STSFL,'')<>'X'"
						+" AND CM_SBSCD ='"+ M_strSBSCD +"'"
						+" AND CM_CMPTP ='"+ strCMPTP +"'"
						+" AND CM_FINFL = 'Y'";
					}
					//System.out.println(M_strSQLQRY);
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Reg. No.","Reg.Date","Party Name"},3,"CT");
					setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if((L_KE.getKeyCode() == L_KE.VK_ENTER))
			{
				if(M_objSOURC == cmbCMPTP)
				{
					txtREGNO.requestFocus();
					setMSG("Please Enter Registration Number to view details..",'N');
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
			txtPRTNM.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),2)));
			strREGNO = txtREGNO.getText().trim();
		}
	}
	/**
	 * Method to validate the data before execuation of the SQL Query.
	 */
	boolean vldDATA()
	{
		try
		{
			boolean L_flgDPT = false;
			if(txtREGNO.getText().trim().length() == 0)
			{
				setMSG("Please enter Registration Number..",'E');
				txtREGNO.requestFocus();
				return false;
			}
			if(txaAUTDL.getText().trim().length() == 0)
			{
				setMSG("Please enter Remark ..",'E');
				txaAUTDL.requestFocus();
				return false;
			}
			if(chkCAPA.isSelected())
			{
				for(int j=0;j<intROWCT-1;j++)
				{
					if(tblDPTCD.getValueAt(j,TBL_CHKFL).toString().equals("true"))						
					{
						L_flgDPT = true;
						break;
					}
				}
				if(L_flgDPT == false)
				{
					setMSG("Please Select Departments to perform CAPA..",'E');
					return false;
				}
			
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
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				if(!strOREM1.equals(txaDPT1.getText().trim()))
					modREMDS("PARTB",txaDPT1.getText().trim(),strOREM1,strDPT1);
				if(!strOREM2.equals(txaDPT2.getText().trim()))
					modREMDS("PARTB",txaDPT2.getText().trim(),strOREM2,strDPT2);
				if(!strOREM3.equals(txaDPT3.getText().trim()))
					modREMDS("PARTB",txaDPT3.getText().trim(),strOREM3,strDPT3);
				if(!strOREM4.equals(txaDPT4.getText().trim()))
					modREMDS("PARTB",txaDPT4.getText().trim(),strOREM4,strDPT4);
				if(!txaAUTDL.getText().trim().equals(""))/// aut remark inserted..
				{
					strEML = "Yes";
					modREMDS("AUTRM",txaAUTDL.getText().trim(),strAUREM,"");
				}
			}			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{				
				//clrCOMP();
				txaAUTDL.setText("");
				txaDPT1.setText("");
				txaDPT2.setText("");
				txaDPT3.setText("");
				txaDPT4.setText("");
				txtREGNO.setText("");
				txtPRTNM.setText("");
				if(tblDPTCD.isEditing())
					tblDPTCD.getCellEditor().stopCellEditing();
				tblDPTCD.setRowSelectionInterval(0,0);
				tblDPTCD.setColumnSelectionInterval(0,0);
				for(int j=0;j<intROWCT-1;j++)					
					tblDPTCD.setValueAt(new Boolean(false), j,TBL_CHKFL);
				chkCAPA.setSelected(false);
				//tblDPTCD.cmpEDITR[TBL_CHKFL].setEnabled(false);
				//setENBL(true);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Saved Successfully..",'N');
			}
			else
			{
				strEML = "No";
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					setMSG("Error in saving data..",'E');
			}
			if(strEML.equals("Yes"))
			{
				// Default Mails
				strEML = "No";
				strFILNM = cl_dat.M_strREPSTR_pbst+"co_rpcasB.html";
				cl_eml ocl_eml = new cl_eml();
				co_rpcas obj = new co_rpcas(M_strSBSCD,strCMPTP,strREGNO);
				obj.getDATA(M_strSBSCD,strCMPTP,strREGNO,"B");
				
				M_strSQLQRY =" SELECT CMT_CODDS from CO_CDTRN where"
					+" isnull(CMT_STSFL,'')<>'X'"	
					+" AND CMT_CGMTP ='EML' AND CMT_CGSTP ='COXXCAS'"
					+" AND CMT_CODCD like '"+ strCMPTP+ "E%'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
						ocl_eml.sendfile(M_rstRSSET.getString("CMT_CODDS").trim()+"@spl.co.in",strFILNM,"Customer Complaint Authorization"," ");
					M_rstRSSET.close();
				}
				//if CAPA Req Mail to Dept selected for CAPA
				if(chkCAPA.isSelected())
				{
					M_strSQLQRY =" SELECT CMT_CHP01 from CO_CDTRN where"
						+" isnull(CMT_STSFL,'')<>'X'"	
						+" AND CMT_CGMTP = 'SYS' AND CMT_CGSTP ='COXXDPT'"
						+" AND CMT_CODCD in ("+ strCPABY +")";						
					//System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						while(M_rstRSSET.next())
							ocl_eml.sendfile(M_rstRSSET.getString("CMT_CHP01").trim()+"@spl.co.in",strFILNM,"Customer Complaint Authorization"," ");
						M_rstRSSET.close();
					}
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
	 */
	private void modREMDS(String P_strREMTP,String P_strREMDS,String P_strOREMDS,String P_strDPTCD)
	{
		try
		{
			if(!P_strOREMDS.equals(P_strREMDS))
			{
				if(P_strREMDS.length() == 0)
				{	
					M_strSQLQRY = "Delete from CO_CMTRN";				
					M_strSQLQRY +=" where CMT_REMTP = '"+ P_strREMTP +"' AND CMT_CMPTP = '"+strCMPTP+"'"
					+ " AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"				
					+ " AND CMT_DPTCD = '"+ P_strDPTCD +"'"
					+ " AND CMT_PRTNO = 'B'"
					+ " AND CMT_REMTP = 'PARTB'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
				else if(P_strOREMDS.length() == 0) 
				{
					M_strSQLQRY = "Insert into CO_CMTRN(CMT_SBSCD,CMT_CMPTP,CMT_REGNO,CMT_PRTNO,CMT_REMTP,CMT_DPTCD,"
						+ "CMT_REMBY,CMT_REMDT,CMT_REMDS,CMT_CPAFL,CMT_TRNFL,CMT_STSFL,CMT_LUSBY,CMT_LUPDT)"
						+ " Values ( '"+ M_strSBSCD +"',"
						+"'"+ strCMPTP +"',"
						+"'"+ txtREGNO.getText().trim() +"',"
						+"'B',"
						+"'"+ P_strREMTP +"',"
						+"'"+ P_strDPTCD +"',"
						+"'"+ cl_dat.M_strUSRCD_pbst +"',"
						+"'"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst.trim()))+"',"
						+"'"+ P_strREMDS +"',"
						+"'',"
						+ getUSGDTL("CMT",'I',"C") + ")";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					if(cl_dat.M_flgLCUPD_pbst == true)
						updSTSFL(txtREGNO.getText().trim(),strCMPTP);

					if(cl_dat.M_flgLCUPD_pbst == false)
					{
						setMSG("Error in Saving Data ..",'E');
						return;
					}
				}
				else
				{
					M_strSQLQRY = "update CO_CMTRN set CMT_REMDS ='"+ P_strREMDS +"',"
					+ " CMT_DPTCD = '"+ P_strDPTCD +"',"
					+ " CMT_STSFL ='C',"
					+ " CMT_TRNFL = '',"
					+ "	CMT_LUSBY ='"+ cl_dat.M_strUSRCD_pbst+"',"
					+ "	CMT_LUPDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"
					+ " where CMT_REMTP = '"+ P_strREMTP +"' AND CMT_CMPTP = '"+ strCMPTP+"'"
					+ " AND CMT_REGNO = '"+ txtREGNO.getText().trim() +"'"
					+ " AND CMT_PRTNO = 'B'"
					+ " AND CMT_DPTCD = '"+ P_strDPTCD +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					
					//if(cl_dat.M_flgLCUPD_pbst == true)
					//	updSTSFL(txtREGNO.getText().trim(),strCMPTP);
					
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
	void updSTSFL(String P_strREGNO,String P_strCMPTP)
	{
		try
		{	
			String L_strCPABY = "";
			strCPABY = "";
			int L_intTEMP = 0;
			if(chkCAPA.isSelected())
			{
				for(int j=0;j<intROWCT-1;j++)
				{
					if(tblDPTCD.getValueAt(j,TBL_CHKFL).toString().equals("true"))						
					{
						L_strCPABY += tblDPTCD.getValueAt(j,TBL_DPTCD).toString() +"_";
						if(L_intTEMP != 0)
							strCPABY +=",";
						strCPABY = "'"+ tblDPTCD.getValueAt(j,TBL_DPTCD).toString() +"'";
						L_intTEMP++;
					}
				}
			}
			else
			{
				L_strCPABY = "";
				strCPABY = "";
			}
			M_strSQLQRY = "update CO_CMMST set CM_STSFL ='5'";
			if(chkCAPA.isSelected())
				M_strSQLQRY +=",CM_CPABY = '"+ L_strCPABY +"'"; 
			M_strSQLQRY +=" where isnull(CM_STSFL,'')<>'X' AND CM_REGNO ='"+ P_strREGNO+ "'"
				+ " AND CM_CMPTP = '"+ P_strCMPTP +"'"
				+ " AND isnull(CM_STSFL,'')<>'X'";
			//System.out.println(M_strSQLQRY);
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"updSTSFL");
		}		
	}	
}