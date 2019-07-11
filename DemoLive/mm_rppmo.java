import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class mm_rppmo extends cl_rbase 
{
	 
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"mm_rppmo.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
	private DataOutputStream D_OUT ;
	private JTextField txtFRMDT,txtPRTCD;	//txtDOCDT.setText(cl_dat.M_txtCLKDT_pbst.getText());		
	private JTextField txtTODT,txtODASONDT,txtODDAYS;//text for overdue
	private JTextField txtFRMNO;
	private static String strFRMNO="";
	private static String strTONO="";
	private JLabel lblBLPTP;

	private static String strFRMDT="";
	private static String strTODT="";
	private JComboBox cmbBLPTP,cmbPRTTP;
	private String strYREND = "30/06/2009" +"";
	private String strYRDGT ,strBILTP,strBILTPDSC;

	private JTextField txtTONO;
	private JComboBox cmbPRQTP,cmbORDRBY;
	private JRadioButton rdbALL,rdbSPECIFIC,rdbOVRDUE,rdbOUTSTD;	
	private JLabel lblSPECIFIC,lblALL,lblNO,lblOVERDUE,lblOUTSTD,lblORDRBY;
	private String strPRQTP,strDETL,strCMB,M_strTOTAL;;
	private Hashtable<Object,Object> hst;
	private int selINDEX;

	int flaghtml=0; //0 text selected, 1 html selected 
	Hashtable<String,String> hst1to100;
	Thread thrSTORE;
	mm_rppmo()		/*  Constructor   */
	{
		super(2);

		try
		{
			//Graphics g2 = getGraphics();
		    
			hst=new Hashtable<Object,Object>();
			//System.out.println("hiiiiiiiiii");
			//thrSTORE=new Thread(this);
			//thrSTORE.start();
			setMatrix(20,16);
			strYRDGT = M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst).compareTo(M_fmtLCDAT.parse(strYREND))>0 ? "0" : "9";

			add(new JLabel("BILL TYPE "),2,2,1,2,this,'L');
			add(lblALL=new JLabel("ALL"),2,4,1,1,this,'R');			
			add(rdbALL=new JRadioButton(),2,5,1,1,this,'L');
			add(lblSPECIFIC=new JLabel("Specific"),2,6,1,2,this,'L');			
			add(rdbSPECIFIC=new JRadioButton(),2,7,1,1,this,'R');

			add(lblBLPTP=new JLabel("Bill Pass Type"),2,8,1,2,this,'L');
			add(cmbBLPTP=new JComboBox(new String[]{"Select"}),2,10,1,3,this,'L');	

			add(rdbOVRDUE=new JRadioButton(),4,5,1,1,this,'L');
			add(lblOVERDUE=new JLabel("Overdue"),4,6,1,2,this,'L');			


			add(rdbOUTSTD=new JRadioButton(),4,8,1,1,this,'L');
			add(lblOUTSTD=new JLabel("Outstanding"),4,9,1,2,this,'L');	


			
			add(new JLabel("O/D As On : "),7,5,1,2,this,'L');
			add(txtODASONDT=new TxtDate(),7,7,1,2,this,'L');

			add(new JLabel("O/D by : "),8,5,1,2,this,'L');
			add(txtODDAYS=new TxtNum(),8,7,1,2,this,'L');			

			ButtonGroup bg=new ButtonGroup();
			bg.add(rdbALL);
			bg.add(rdbSPECIFIC);
			ButtonGroup bg2=new ButtonGroup();
			bg2.add(rdbOVRDUE);
			bg2.add(rdbOUTSTD);

			rdbALL.setSelected(true);
			rdbALL.addActionListener(this);
			rdbSPECIFIC.addActionListener(this);
			rdbOVRDUE.setSelected(true);
			rdbOVRDUE.addActionListener(this);
			rdbOUTSTD.addActionListener(this);
			
			add(new JLabel("Order By : "),10,5,1,2,this,'L');
			add(cmbORDRBY=new JComboBox(new String[]{"Vendor Name","Due Date","Amount","Over Due Days"}),10,7,1,2,this,'L');			
			cmbORDRBY.addItemListener(this);
			add(cmbPRTTP=new JComboBox(new String[]{"Select"}),12,7,1,2,this,'L');
					
			M_strSQLQRY="select substring(CMT_CODCD,2,2) CMT_CODCD,CMT_CODDS FROM CO_CDTRN where CMT_CGSTP='MMXXBLP' and substring(CMT_CGMTP,2,2)='"+cl_dat.M_strCMPCD_pbst+"' order by CMT_CODCD";
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				
				//System.out.print(M_rstRSSET.getString(1));
				while(M_rstRSSET.next())
				{	   	   	 

					cmbBLPTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}				
			}

			if(M_rstRSSET != null)
				M_rstRSSET.close();

							
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS FROM CO_CDTRN WHERE "
				+" CMT_CGSTP = 'COXXPRT' AND CMT_CODCD IN('S','T','N')";// AND substring(CMT_CGMTP,2,2)='"+cl_dat.M_strCMPCD_pbst+"' order by CMT_CODCD";			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				while(M_rstRSSET.next())
				{	   	   	 
	
					cmbPRTTP.addItem(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"")+" : "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				}	
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			else
			{
				System.out.println("no data found........");
			}
			add(txtPRTCD=new TxtLimit(20),12,11,1,3,this,'L');	
			
			cmbPRTTP.addActionListener(this);	
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_pnlRPFMT.setVisible(true);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			//this.crtHST();	
			setENBL(false);	
			

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
			if(M_objSOURC==cl_dat.M_cmbOPTN_pbst)
			{

				clrCOMP();

				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{

					rdbALL.setSelected(true);					
					cmbBLPTP.setEnabled(false);
					txtODASONDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
										
					txtODDAYS.setText("0");
					cmbORDRBY.setEnabled(true);
					cmbPRTTP.setEnabled(true);
					txtPRTCD.setEnabled(true);
					txtODASONDT.requestFocus();
				}
				else
					setENBL(false);

			}
			if(M_objSOURC==rdbSPECIFIC)
			{
				//rdbSPECIFIC.setEnabled(true);
				cmbBLPTP.setEnabled(true);
				//rdbALL.setEnabled(false);
			}
			if(M_objSOURC==rdbALL)
			{
				//rdbALL.setEnabled(true);
				//rdbSPECIFIC.setEnabled(false);
				cmbBLPTP.setEnabled(false);
			}
			if(M_objSOURC==rdbOVRDUE)
			{
				
				txtODASONDT.setEnabled(true);
				txtODDAYS.setEnabled(true);
				
				setMSG("Enter the over due date", 'N');
			}
			if(M_objSOURC==rdbOUTSTD)
			{
				
				txtODASONDT.setEnabled(false);
				txtODDAYS.setEnabled(false);
				cmbORDRBY.requestFocus();
				setMSG("Select the option", 'N');
			}
			if(M_objSOURC==cmbBLPTP)
			{
				strBILTP=cmbBLPTP.getSelectedItem().toString().substring(0, 2);
				strBILTPDSC=cmbBLPTP.getSelectedItem().toString().substring(2);
				System.out.println("strBILTP"+strBILTP);
			
			}
			if(M_objSOURC==cmbPRTTP)
			{
				if(cmbPRTTP.getSelectedIndex()==0)
				txtPRTCD.setText("");
			
			}
			

		}
		catch(Exception L_EX)
		{
			
			setMSG(L_EX,"KeyPressed");
		}
	}


	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			try
			{
				if(M_objSOURC==txtODASONDT)
				{
					txtODDAYS.requestFocus();
					setMSG("Enter the days from overdue", 'N');
				}
				if(M_objSOURC==txtODDAYS)
				{
					cmbORDRBY.requestFocus();
					setMSG("Select the option", 'N');
				}
				if(M_objSOURC==cmbORDRBY)
				{
					cmbPRTTP.requestFocus();
					setMSG("Select the party type", 'N');
				}
				if(M_objSOURC==cmbPRTTP)
				{
					if(cmbPRTTP.getSelectedIndex()>0)
					{
						txtPRTCD.requestFocus();
						setMSG("Select the party", 'N');
					}
					else if(cmbPRTTP.getSelectedIndex()==0)
					{
						cl_dat.M_btnSAVE_pbst.requestFocus();
					}
				
				}
				if(M_objSOURC==txtPRTCD)
				{
					cl_dat.M_btnSAVE_pbst.requestFocus();
					setMSG("Click on display to generate report.....", 'N');
				
				}
				

		
			}
			catch(Exception L_EX)
			{
				setMSG(L_EX,"This is KeyPressed");
			}			
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			//setCursor(cl_dat.M_curWTSTS_pbst);			
			if(M_objSOURC == txtPRTCD)
			{
				M_strHLPFLD = "txtPRTCD";
				cl_dat.M_flgHELPFL_pbst = true;
				System.out.println("inside combo:"+cmbPRTTP.getSelectedItem().toString().substring(0,1));
				M_strSQLQRY="Select distinct PT_PRTCD,PT_PRTNM from CO_PTMST where  PT_PRTTP='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"'";
				
				if(txtPRTCD.getText().trim().length() > 0)
					M_strSQLQRY += " AND PT_PRTCD LIKE '"+txtPRTCD.getText().trim()+"%'";
				M_strSQLQRY += " ORDER BY PT_PRTNM";
				cl_hlp(M_strSQLQRY,2,1,new String[]{"Party Code"
						,"Party Name"},2,"CT");
			}
		}
		
	}
	
	void exeHLPOK()
	{
		super.exeHLPOK() ;
		try
		{
			if(M_strHLPFLD == "txtPRTCD")
			{
				txtPRTCD.setText(cl_dat.M_strHLPSTR_pbst);			
				cl_dat.M_flgHELPFL_pbst = false;
			}
		}catch(Exception L_EX)
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
			genRPHDR();
			/*
			M_calLOCAL.setTime(M_fmtLCDAT.parse(txtODASONDT.getText())); 
			Date dt=M_calLOCAL.getTime();
			M_calLOCAL.add(dt.getDay(), -1);
			dt=M_calLOCAL.getTime();
			*/
						
			Calendar cal = Calendar.getInstance();
			if(rdbOVRDUE.isSelected()==true)
			cal.setTime(M_fmtLCDAT.parse(txtODASONDT.getText()));
			else if(rdbOUTSTD.isSelected()==true)
			{
				cal.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));	
			}
				
			Date dtOLD=cal.getTime();
			
			String stDTOLD=M_fmtDBDAT.format(dtOLD);
				//new Calendar(dt.getYear(),dt.getMonth(),dt.getDay());
			//cal.setTime(M_fmtLCDAT.parse(txtODASONDT.getText()));
			if(rdbOVRDUE.isSelected()==true)
			cal.add(cal.getTime().getMonth(),-Integer.parseInt(txtODDAYS.getText().trim()));
			else if(rdbOUTSTD.isSelected()==true)
			cal.add(cal.getTime().getMonth(),-0);
				
			String st=M_fmtLCDAT.format(cal.getTime());
			System.out.println("***new dt :"+st);
			String strDOCNO=null;
			String strLEFT=null;
			String strPRTCD=null;
			if(rdbSPECIFIC.isSelected()==true)
				{
				strLEFT="substr(PL_DOCNO,2,2)=";
				strDOCNO="'"+strBILTP+"'"+" AND";
				}
			else if(rdbALL.isSelected()==true)
				{
				strLEFT="";
				strDOCNO="";
				}
			
			if((cmbPRTTP.getSelectedIndex()>0)&& (txtPRTCD.getText().trim().length() > 0))
				strPRTCD=" AND PL_PRTCD='"+txtPRTCD.getText().trim()+"'";
			else 
				strPRTCD="";
				/*
				 * 
				 * SELECT PT_PRTNM,PL_DOCVL-PL_ADJVL PL_BALVL,PL_DOCNO,CMT_CODDS,PL_DOCDT,PL_DUEDT,DAYS(date('06/01/2009'))-DAYS(PL_DUEDT) ODDAYS FROM spldata.MM_PLTRN,spldata.CO_PTMST,spldata.MM_BLMST,spldata.CO_CDTRN WHERE PL_DOCVL-PL_ADJVL > 0 AND PL_STSFL !='X' AND PL_PRTTP='S' AND PL_PRTCD='U0050' AND PT_PRTTP=PL_PRTTP AND PL_PRTCD=PT_PRTCD AND BL_DOCNO=PL_DOCNO  AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD AND  PL_DUEDT<='06/01/2009'  ORDER BY PT_PRTNM


				 */
			String strPRTTP;
			if(cmbPRTTP.getSelectedIndex()>0)
				strPRTTP=" AND PL_PRTTP='"+cmbPRTTP.getSelectedItem().toString().substring(0,1)+"'";
			else 
				strPRTTP="";
			
			//if(txtPRTCD.getText().trim().length()==0)
				
			if(cmbORDRBY.getSelectedItem().toString().trim().equalsIgnoreCase("Vendor Name"))
			{
				//M_strSQLQRY = "SELECT PT_PRTNM,PL_DOCVL-PL_ADJVL PL_BALVL,PL_DOCNO,CMT_CODDS,PL_DOCDT,ifnull(PL_DUEDT,'  /  /    '),DAYS(date('"+stDTOLD+"'))"+"-DAYS((ifnull(PL_DUEDT,PL_DOCDT))) ODDAYS FROM MM_PLTRN,CO_PTMST,MM_BLMST,CO_CDTRN WHERE PL_DOCVL-PL_ADJVL > 0 AND PL_STSFL !='X' AND PT_PRTTP='S' AND PL_PRTCD=PT_PRTCD AND BL_DOCNO=PL_DOCNO  AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD AND "+strLEFT+strDOCNO+" PL_DUEDT<='"+M_fmtDBDAT.format(cal.getTime())+"' ORDER BY PT_PRTNM";
				M_strSQLQRY = "SELECT PT_PRTNM,round(PL_DOCVL-PL_ADJVL,0) PL_BALVL,PL_DOCNO,CMT_CODDS,PL_DOCDT,PL_DUEDT,DAYS(date('"+stDTOLD+"'))-DAYS(PL_DUEDT) ODDAYS FROM MM_PLTRN,CO_PTMST,MM_BLMST,CO_CDTRN WHERE PL_DOCVL-PL_ADJVL > 0 AND PL_STSFL !='X'" +strPRTTP+" AND PT_PRTTP=PL_PRTTP AND PL_PRTCD=PT_PRTCD AND BL_DOCNO=PL_DOCNO  AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD AND "+strLEFT+strDOCNO+strPRTCD+" PL_DUEDT<='"+M_fmtDBDAT.format(cal.getTime())+"'"+" ORDER BY PT_PRTNM";

				//System.out.println("&&&&ODDAYS&&"+M_rstRSSET.getString("ODDAYS"));
			}
			else if(cmbORDRBY.getSelectedItem().toString().trim().equalsIgnoreCase("Due Date"))
				{
				M_strSQLQRY = "SELECT PT_PRTNM,round(PL_DOCVL-PL_ADJVL,0) PL_BALVL,PL_DOCNO,CMT_CODDS,PL_DOCDT,PL_DUEDT,DAYS(date('"+stDTOLD+"'))-DAYS(PL_DUEDT) ODDAYS FROM MM_PLTRN,CO_PTMST,MM_BLMST,CO_CDTRN WHERE PL_DOCVL-PL_ADJVL > 0 AND PL_STSFL !='X'" +strPRTTP+" AND PT_PRTTP=PL_PRTTP AND PL_PRTCD=PT_PRTCD AND BL_DOCNO=PL_DOCNO  AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD AND "+strLEFT+strDOCNO+" PL_DUEDT<='"+M_fmtDBDAT.format(cal.getTime())+"'"+strPRTCD+" ORDER BY PL_DUEDT";
					System.out.println("&&&&&"+cmbORDRBY.getSelectedItem().toString().trim());
				}
			
			else if(cmbORDRBY.getSelectedItem().toString().trim().equalsIgnoreCase("Amount"))	
			{
				M_strSQLQRY = "SELECT PT_PRTNM,round(PL_DOCVL-PL_ADJVL,0) PL_BALVL,PL_DOCNO,CMT_CODDS,PL_DOCDT,PL_DUEDT,DAYS(date('"+stDTOLD+"'))-DAYS(PL_DUEDT) ODDAYS FROM MM_PLTRN,CO_PTMST,MM_BLMST,CO_CDTRN WHERE PL_DOCVL-PL_ADJVL > 0 AND PL_STSFL !='X'" +strPRTTP+" AND PT_PRTTP=PL_PRTTP AND PL_PRTCD=PT_PRTCD AND BL_DOCNO=PL_DOCNO  AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD AND "+strLEFT+strDOCNO+" PL_DUEDT<='"+M_fmtDBDAT.format(cal.getTime())+"'"+strPRTCD+" ORDER BY PL_BALVL";
System.out.println("&&&&&"+cmbORDRBY.getSelectedItem().toString().trim());
			}
			else if(cmbORDRBY.getSelectedItem().toString().trim().equalsIgnoreCase("Over Due Days"))			
			{
				M_strSQLQRY = "SELECT PT_PRTNM,round(PL_DOCVL-PL_ADJVL,0) PL_BALVL,PL_DOCNO,CMT_CODDS,PL_DOCDT,PL_DUEDT,DAYS(date('"+stDTOLD+"'))-DAYS(PL_DUEDT) ODDAYS FROM MM_PLTRN,CO_PTMST,MM_BLMST,CO_CDTRN WHERE PL_DOCVL-PL_ADJVL > 0 AND PL_STSFL !='X'" +strPRTTP+" AND PT_PRTTP=PL_PRTTP AND PL_PRTCD=PT_PRTCD AND BL_DOCNO=PL_DOCNO  AND CMT_CGSTP='COXXCUR' AND CMT_CODCD=BL_CURCD AND "+strLEFT+strDOCNO+" PL_DUEDT<='"+M_fmtDBDAT.format(cal.getTime())+"'"+strPRTCD+" ORDER BY ODDAYS";
				System.out.println("&&&&&"+cmbORDRBY.getSelectedItem().toString().trim());
			}
					
			int L_CNT=0;
			System.out.println("***M_strSQLQRY :"+M_strSQLQRY);
			
			M_rstRSSET=cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				System.out.println("got resultset");
				while(M_rstRSSET.next())
				{
					L_CNT++;
					//System.out.println(M_rstRSSET.getString("PT_PRTNM"));
					//System.out.println(" As on Date : "+stDTOLD);
					//System.out.println(" due date : "+nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("PL_DUEDT")),""));
					//System.out.println(" diff : "+nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getString("ODDAYS")),""));
					//System.out.println("*******************");
					//D_OUT.writeBytes(padSTRING('L',""+L_CNT+"      "+nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),"")+"   "+nvlSTRVL(M_rstRSSET.getString("PL_BALVL"),"")+"   "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),"")+"    "+nvlSTRVL(M_rstRSSET.getString("PL_DOCDT"),""),80));crtNWLIN();
					D_OUT.writeBytes(padSTRING('C'," "+L_CNT,5));
					D_OUT.writeBytes(padSTRING('R'," "+nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""),35));					
					D_OUT.writeBytes(padSTRING('R'," "+nvlSTRVL(M_rstRSSET.getString("PL_DOCNO"),""),10));
					D_OUT.writeBytes(padSTRING('L'," "+nvlSTRVL(M_rstRSSET.getString("PL_BALVL"),""),10));
					D_OUT.writeBytes(padSTRING('L'," "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""),15));
					D_OUT.writeBytes(padSTRING('L'," "+nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("PL_DOCDT")),""),15));
					D_OUT.writeBytes(padSTRING('L'," "+nvlSTRVL(M_fmtLCDAT.format(M_rstRSSET.getDate("PL_DUEDT")),""),15));
					D_OUT.writeBytes(padSTRING('L'," "+nvlSTRVL(M_rstRSSET.getString("ODDAYS"),""),8));crtNWLIN();
					
				}
			}
			else
			{
				setMSG("resultset null in genRPFL", 'E');
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
			//setCursor(cl_dat.M_curDFSTS_pbst);
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
				D_OUT.writeBytes("<HTML><HEAD><Title>Attendance Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
    	   
			crtNWLIN();
    		prnFMTCHR(D_OUT,M_strBOLD);
    		//D_OUT.writeBytes(padSTRING('C',cl_dat.M_strCMPNM_pbst,50));
    		//D_OUT.writeBytes(padSTRING('L',","+ cl_dat.M_strCMPLC_pbst,10));
		    crtNWLIN();		    
		    		    
		    D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,40));
			D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
			D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,30));
			crtNWLIN();
			if(rdbSPECIFIC.isSelected()==true)
			D_OUT.writeBytes(padSTRING('R',""+"PENDING PAYMENT FOR "+strBILTPDSC+" AS ON "+cl_dat.M_strLOGDT_pbst,110));crtNWLIN();
			if(rdbALL.isSelected()==true)
			D_OUT.writeBytes(padSTRING('R',""+"PENDING PAYMENT FOR REVENUE EXPENSES(SPARE PARTS,R.M & OTHERS),INCOMMING TRANSPORTATION AS ON "+cl_dat.M_strLOGDT_pbst,110));crtNWLIN();
			
			D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------");crtNWLIN();
			D_OUT.writeBytes("SR.NO.          VENDOR  NAME             DOC NO      AMOUNT          CUR           SEND ON         DUE ON       O/D BY ");crtNWLIN();
			D_OUT.writeBytes("---------------------------------------------------------------------------------------------------------------------");crtNWLIN();

			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
				prnFMTCHR(D_OUT,M_strNOBOLD);
			if(M_rdbHTML.isSelected())
				D_OUT.writeBytes("</b>");		
		}catch(Exception L_IOE)
		{
			setMSG(L_IOE,"Report Header");
		}
	}
	
	void genRPFTR()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;	//new test
			D_OUT.writeBytes(padSTRING('L',"",65));
			crtNWLIN();
			D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------");
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
		}
		catch(Exception L_VLD)
		{
			setMSG(L_VLD,"vldDATA()");
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
			     strRPFNM = strRPLOC + "mm_rppmo.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "mm_rppmo.doc";
				
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
			/*else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strRPFNM,"Supplier Analysis Reort"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}*/
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

		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
}



