import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;
import javax.swing.JLabel;import javax.swing.JTextField;
import javax.swing.JComboBox;import javax.swing.JComponent;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;import javax.swing.InputVerifier;
import java.util.Hashtable;import java.util.StringTokenizer;
import java.io.DataOutputStream;import java.io.FileOutputStream;import java.io.IOException;
import java.awt.Color;import java.sql.Timestamp;
import java.util.Calendar;import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

class hr_rpota extends cl_rbase 
{
	private JTextField txtDPTCD;			
	private JTextField txtEMPNO;
	private JTextField txtSTRDT;			
	private JTextField txtENDDT;
	private JLabel lblSTRDT;			
	private JLabel lblENDDT;
	private JLabel lblDPTNM;
	private JLabel lblEMPNM;
	private JLabel lblTMLIM;
	private JCheckBox chkOTSDT;
	private JRadioButton rdbWRKDT;
	private JRadioButton rdbOTSDT;
	private ButtonGroup btgDATE;
	private JComboBox cmbRPTYP;
	private String strRPFNM = cl_dat.M_strREPSTR_pbst+"hr_rpota.doc";
	private String strRPLOC = cl_dat.M_strREPSTR_pbst;										/** FileOutputStream for generated report file handeling.*/
	private FileOutputStream F_OUT ;/** DataOutputStream to hold Stream of report data.*/   
    private DataOutputStream D_OUT ;
	private String[] staYEAR = {"","JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
	
	private boolean flgTBLDT;
	hr_rpota()		/*  Constructor   */
	{
		super(2);
		try
		{
			setMatrix(20,20);			
			
			add(new JLabel("Report Type"),5,3,1,2,this,'L');
			add(cmbRPTYP = new JComboBox(),5,5,1,6,this,'L');			

			add(new JLabel("Department"),6,3,1,2,this,'L');
			add(txtDPTCD= new TxtLimit(3),6,5,1,2,this,'L');
			add(lblDPTNM=new JLabel(),6,7,1,8,this,'L');     
			
			add(new JLabel("Employee"),7,3,1,2,this,'L');
			add(txtEMPNO= new TxtLimit(4),7,5,1,2,this,'L');
			add(lblEMPNM=new JLabel(),7,7,1,8,this,'L');     
			
			add(rdbWRKDT=new JRadioButton("Work Date"),8,3,1,2,this,'L');
			add(rdbOTSDT=new JRadioButton("Accounts Submission Date"),8,5,1,4,this,'L');

			add(lblSTRDT = new JLabel("Form Date"),9,3,1,2,this,'L');
			add(txtSTRDT= new TxtDate(),9,5,1,2,this,'L');
			
			add(lblENDDT = new JLabel("To Date"),10,3,1,2,this,'L');
			add(txtENDDT = new TxtDate(),10,5,1,2,this,'L');
		
			add(chkOTSDT = new JCheckBox("Submit To Accounts"),11,3,1,5,this,'L');
			btgDATE=new ButtonGroup();
			btgDATE.add(rdbWRKDT);btgDATE.add(rdbOTSDT);
			setENBL(true);
			M_pnlRPFMT.setVisible(true);
			
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			INPVF oINPVF=new INPVF();
			txtDPTCD.setInputVerifier(oINPVF);
			txtEMPNO.setInputVerifier(oINPVF);
			txtSTRDT.setInputVerifier(oINPVF);
			txtENDDT.setInputVerifier(oINPVF);
			M_pnlRPFMT.setVisible(true);			

			cmbRPTYP.addItem("Select Report Type");
			cmbRPTYP.addItem("Details of Overtime hours");
			cmbRPTYP.addItem("Overtime Detail");
			cmbRPTYP.addItem("OT Wages Summary (Employee Number Wise)");
			cmbRPTYP.addItem("OT Wages Summary (Department Wise)");
			
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
			if(txtDPTCD.getText().length()==0)
				lblDPTNM.setText("");
			if(txtEMPNO.getText().length()==0)
				lblEMPNM.setText("");
			if(L_AE.getSource()==chkOTSDT)
			{
				if(chkOTSDT.isSelected())
				{
					int L_intOPTN=JOptionPane.showConfirmDialog( this,cl_dat.M_strLOGDT_pbst+" Date Will Be Stored As submission Date To Acconts. \n Do You Want To Proceed?","Confirmation",JOptionPane.OK_CANCEL_OPTION);
					if(L_intOPTN > 0)
					{
						chkOTSDT.setSelected(false);
						lblSTRDT.setVisible(true);
						txtSTRDT.setVisible(true);
					}
					else
					{
						cmbRPTYP.setSelectedItem("OT Wages Summary (Employee Number Wise)");
						txtSTRDT.setText("");
						lblSTRDT.setVisible(false);
						txtSTRDT.setVisible(false);
					}
				}
				else
				{
					lblSTRDT.setVisible(true);
					txtSTRDT.setVisible(true);
				}
			}
			if(L_AE.getSource()==cmbRPTYP)
			{
				if(cmbRPTYP.getSelectedItem().toString().equals("Details of Overtime hours"))
				{
					rdbWRKDT.setVisible(false);
					rdbOTSDT.setVisible(false);
					chkOTSDT.setVisible(false);
					M_rdbHTML.setSelected(true);
					M_rdbTEXT.setEnabled(true);
				}
				else
				{
					rdbWRKDT.setVisible(true);
					rdbOTSDT.setVisible(true);
					chkOTSDT.setVisible(true);
				}
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
		
		try
		{
			if(L_KE.getKeyCode() == L_KE.VK_F1 )
        	{						
										//help for Department Code
        		if(M_objSOURC==txtDPTCD)		
        		{
        		    cl_dat.M_flgHELPFL_pbst = true;
        		    M_strHLPFLD = "txtDPTCD";
        			String L_ARRHDR[] = {"Department Code","Department Description"};
					M_strSQLQRY=" Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
					M_strSQLQRY+=" and CMT_STSFL <> 'X'";
					cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
        								//help for Employee Category
				if(M_objSOURC==txtEMPNO)		
        		{
        			cl_dat.M_flgHELPFL_pbst = true;
        			M_strHLPFLD = "txtEMPNO";
        			String L_ARRHDR[] = {"Code","Category"};
        			M_strSQLQRY = "select distinct EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_STSFL <> 'U'";
					if(txtDPTCD.getText().length()>0)
						M_strSQLQRY += " and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
					M_strSQLQRY += " order by EP_EMPNO";
					//System.out.println(">>>"+M_strSQLQRY);
        			cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
        		}
			}
			if(L_KE.getKeyCode() == L_KE.VK_ENTER)
        	{						
        		if(M_objSOURC==txtDPTCD)		
					txtEMPNO.requestFocus();
        		if(M_objSOURC==txtEMPNO)		
					txtSTRDT.requestFocus();
        		if(M_objSOURC==txtSTRDT)		
					txtENDDT.requestFocus();
				if(M_objSOURC==txtENDDT)		
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
			if(M_strHLPFLD.equals("txtDPTCD"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtDPTCD.setText(L_STRTKN.nextToken());
				lblDPTNM.setText(L_STRTKN.nextToken());
			}
			if(M_strHLPFLD.equals("txtEMPNO"))
			{
			    StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				txtEMPNO.setText(L_STRTKN.nextToken());
				lblEMPNM.setText(L_STRTKN.nextToken().replace('|',' '));
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
			String strOLD_WRKDT="";
			String strNEW_WRKDT="";
			
			String strOLD_DPTCD="";
			String strNEW_DPTCD="";
			String strWHRSTR = "";
			setMSG("Printing Report..",'N');
			
			
			if(cmbRPTYP.getSelectedItem().toString().equals("Details of Overtime hours"))
			{
				int L_intSRLNO=1;
				crtTBL(D_OUT,1);
				
				M_strSQLQRY  =" select ot_wrkdt,ot_empno,ep_dptcd,ep_dptnm,trim(ep_fstnm)||' '||left(ifnull(ep_mdlnm,''),1)||'.'||trim(ifnull(ep_lstnm,' ')) ep_empnm,sw_incst,sw_outst,ot_strtm,ot_endtm,ot_ovtwk";
				M_strSQLQRY+= " FROM hr_ottrn,HR_SWMST,HR_EPMST";
				M_strSQLQRY+= " where sw_empno=ot_empno and sw_wrkdt=ot_wrkdt and sw_empno=ep_empno and ep_cmpcd=sw_cmpcd and ot_cmpcd='"+cl_dat.M_strCMPCD_pbst+"'";
				M_strSQLQRY +=" and ot_wrkdt between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'";
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and OT_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" ORDER BY OT_WRKDT,EP_DPTCD,EP_EMPCT,OT_EMPNO";
				
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				System.out.println("M_strSQLQRY>>"+M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					while(M_rstRSSET.next())
					{
						D_OUT.writeBytes("<tr>");
						D_OUT.writeBytes("<td align ='center' width='1%'>"+L_intSRLNO+"</td>");/**print Serial No*/
					    D_OUT.writeBytes("<td align ='left' width='5%'>"+M_rstRSSET.getString("ep_empnm")+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("ot_empno")+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_fmtLCDAT.format(M_rstRSSET.getDate("ot_wrkdt"))+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("sw_incst").substring(11,16)+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("sw_outst").substring(11,16)+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("ot_strtm").substring(11,16)+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("ot_endtm").substring(11,16)+"</td>");
						D_OUT.writeBytes("<td align ='center' width='2%'>"+M_rstRSSET.getString("ot_ovtwk").substring(0,5)+"</td>");
						D_OUT.writeBytes("</tr>");
						//crtNWLIN();
						L_intSRLNO++;
					}
				}
				else
					setMSG("No Data Found..",'E');	
				endTABLE(D_OUT);
			}
			
			
			if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)")
			|| cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)")
			|| cmbRPTYP.getSelectedItem().toString().equals("Overtime Detail"))
			{
				strWHRSTR =" where sw_empno=ep_empno and ep_cmpcd=sw_cmpcd and ifNull(ep_stsfl,'')<>'U' and ep_lftdt is null and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_ovtfl = 'O' ";
				if(chkOTSDT.isSelected())	
					strWHRSTR +=" and SW_OTSDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'"; 
				else
				{
					if(rdbWRKDT.isSelected())
						strWHRSTR +=" and SW_WRKDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
					else if(rdbOTSDT.isSelected())
					{
						if(txtSTRDT.getText().length()==0 && txtENDDT.getText().length()==0)
							strWHRSTR +=" and SW_OTSDT is null"; 
						else if(txtSTRDT.getText().length()==0 && txtENDDT.getText().length()==10)
							strWHRSTR +=" and SW_WRKDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and SW_OTSDT is null";
						else	
							strWHRSTR +=" and SW_OTSDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
					}
				}
				if(txtDPTCD.getText().trim().length()>0)
					strWHRSTR+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					strWHRSTR+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
			}	
			
			if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)"))
			{
				if(chkOTSDT.isSelected())
				{
					String strSQLQRY = " update HR_SWMST set sw_otsdt = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))+"'";
					strSQLQRY += " where  sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_ovtfl = 'O' and SW_WRKDT <= '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"' and SW_OTSDT is null";
					System.out.println(strSQLQRY);
					cl_dat.exeSQLUPD(strSQLQRY,"updSWMST_OTSDT");
					if(cl_dat.exeDBCMT("exePRINT"))
						setMSG("",'N');
					else
					    setMSG("Error in updating data..",'E');
				}
			}
			
			
			if(cmbRPTYP.getSelectedItem().toString().equals("Overtime Detail"))
			{
			
				M_strSQLQRY  =" select sw_wrkdt,sw_empno,ep_empct,ep_dptcd,ep_dptnm,trim(ep_fstnm)||' '||left(ifnull(ep_mdlnm,' '),1)||'.'||trim(ifnull(ep_lstnm,' ')) ep_empnm,sw_wrksh,sw_inctm,sw_outtm,sw_ovthr,sw_ovtfl,ifNull(sw_ovtby,'') sw_ovtby";
				M_strSQLQRY +=" FROM HR_SWMST,HR_EPMST";
				M_strSQLQRY +=  strWHRSTR;
				M_strSQLQRY +=" ORDER BY SW_WRKDT,EP_DPTCD,EP_EMPCT,SW_OVTFL,SW_EMPNO";

				//System.out.println("Query >>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						strNEW_WRKDT=M_rstRSSET.getString("SW_WRKDT");
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							
							if(!strOLD_WRKDT.equals(strNEW_WRKDT))
							{
								crtNWLIN();
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_WRKDT"),15));
								strOLD_WRKDT=M_rstRSSET.getString("SW_WRKDT");
								strOLD_DPTCD="";
								crtNWLIN();
							}
							if(!strOLD_DPTCD.equals(strNEW_DPTCD))
							{
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTCD"),4));
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),15));
								strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
							}
							else D_OUT.writeBytes(padSTRING('R',"",19)); 
								
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPCT"),4));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),6));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),20));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("SW_WRKSH"),""),8));
								
							if(M_rstRSSET.getString("SW_INCTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_INCTM").substring(11,16),10));
							else D_OUT.writeBytes(padSTRING('R',"-",10));
								
							if(M_rstRSSET.getString("SW_OUTTM")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OUTTM").substring(11,16),11));
							else D_OUT.writeBytes(padSTRING('R',"-",11));

							if(M_rstRSSET.getString("SW_OVTFL").equals("O"))
								D_OUT.writeBytes(padSTRING('R',"OT",8));
							else if(M_rstRSSET.getString("SW_OVTFL").equals("C"))
								D_OUT.writeBytes(padSTRING('R',"CO",8));
								
							if(M_rstRSSET.getString("SW_OVTHR")!=null)
								D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OVTHR"),11));
							else D_OUT.writeBytes(padSTRING('R',"-",11));
							
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_OVTBY"),11));
							crtNWLIN();
					}	
				}
				else
					setMSG("No Data Found..",'E');	
			}
			if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)")
			|| cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)"))
			{
				/*M_strSQLQRY  =" select sw_empno,trim(ep_fstnm)||' '||left(ifnull(ep_mdlnm,' '),1)||'.'||trim(ifnull(ep_lstnm,' ')) ep_empnm,ep_dptcd,ep_dptnm,ep_accrf,ifNull(eh_basal,0) eh_basal, ifNull(eh_dnalw,0) eh_dnalw,sum(hour(sw_ovthr)) HRS, sum(minute(sw_ovthr)) MNS";
				M_strSQLQRY +=" FROM HR_SWMST,HR_EPMST";
				M_strSQLQRY +=" left outer join hr_ehtrn on eh_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_empno=eh_empno and ((sw_wrkdt between eh_strdt and eh_enddt) or  (sw_wrkdt >= eh_strdt and eh_enddt is null))"; 
				M_strSQLQRY +=" where sw_empno=ep_empno and ep_cmpcd=sw_cmpcd and ifNull(ep_stsfl,'')<>'U' and ep_lftdt is null and sw_cmpcd='"+cl_dat.M_strCMPCD_pbst+"' and sw_ovtfl in ('O') AND SW_WRKDT between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtSTRDT.getText()))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(txtENDDT.getText()))+"'"; 
				if(txtDPTCD.getText().trim().length()>0)
					M_strSQLQRY+= " and EP_DPTCD='"+txtDPTCD.getText().trim()+"'";
				if(txtEMPNO.getText().trim().length()>0)
					M_strSQLQRY+= " and SW_EMPNO='"+txtEMPNO.getText().trim()+"'";
				M_strSQLQRY +=" group by sw_empno,trim(ep_fstnm)||' '||left(ifnull(ep_mdlnm,' '),1)||'.'||trim(ifnull(ep_lstnm,' ')),ep_dptcd,ep_dptnm,ep_accrf,eh_basal, eh_dnalw";
				if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)"))
					M_strSQLQRY +=" ORDER BY SW_EMPNO";
				else
					M_strSQLQRY +=" ORDER BY EP_DPTCD";
				*/
				
				M_strSQLQRY  =" select sw_empno,month(sw_wrkdt) LP_MONTH,year(sw_wrkdt) LP_YEAR,trim(ep_fstnm)||' '||left(ifnull(ep_mdlnm,' '),1)||'.'||trim(ifnull(ep_lstnm,' ')) ep_empnm,ep_dptcd,ep_dptnm,ep_accrf,sw_otsdt,sum(hour(sw_ovthr)) HRS, sum(minute(sw_ovthr)) MNS";
				M_strSQLQRY +=" FROM HR_SWMST,HR_EPMST";
				M_strSQLQRY += strWHRSTR;
				M_strSQLQRY +=" group by sw_empno,month(sw_wrkdt),year(sw_wrkdt),trim(ep_fstnm)||' '||left(ifnull(ep_mdlnm,' '),1)||'.'||trim(ifnull(ep_lstnm,' ')),ep_dptcd,ep_dptnm,ep_accrf,sw_otsdt";
				if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)"))
					M_strSQLQRY +=" ORDER BY SW_EMPNO,month(sw_wrkdt),year(sw_wrkdt)";
				else
					M_strSQLQRY +=" ORDER BY EP_DPTCD";
				
				System.out.println("Query >>"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				String L_strHOURS="";
				double L_dblDAYS=0;
				//double L_dblAMNT=0;
				double L_dblBASIC=0;
				double L_dblDA = 0;
				int L_intNOFDS = 0;
				
				int L_intTOTHRS=0;
				int L_intTOTMIN=0;
				String L_strTOTHOURS="";
				double L_dblTOTDAYS=0;
				//double L_dblTOTAMNT=0;
				
				int L_intGTOTHRS=0;
				int L_intGTOTMIN=0;
				String L_strGTOTHOURS="";
				double L_dblGTOTDAYS=0;
				//double L_dblGTOTAMNT=0;
				
				String L_strDPTNM = "";
				if(M_rstRSSET!=null)
				{
					while(M_rstRSSET.next())
					{	
						strNEW_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						L_strHOURS = calHOURS(M_rstRSSET.getString("HRS"),M_rstRSSET.getString("MNS"));
						//L_dblBASIC = M_rstRSSET.getDouble("EH_BASAL");
						//L_dblDA = M_rstRSSET.getDouble("EH_DNALW");
						L_dblDAYS = Double.parseDouble(calRND_HOURS(L_strHOURS))*2/8;
						//L_intNOFDS=Integer.parseInt(txtENDDT.getText().substring(0,2));
						//L_dblAMNT = ((L_dblBASIC+L_dblDA)*L_dblDAYS)/L_intNOFDS;
						
						
						if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)"))
						{
							if(!strOLD_DPTCD.equals(strNEW_DPTCD) && !strOLD_DPTCD.equals(""))
							{
							
								L_strTOTHOURS = calHOURS(String.valueOf(L_intTOTHRS),String.valueOf(L_intTOTMIN));
								L_dblTOTDAYS = Double.parseDouble(calRND_HOURS(L_strTOTHOURS))*2/8;
								
								D_OUT.writeBytes(padSTRING('R',strOLD_DPTCD,5));
								D_OUT.writeBytes(padSTRING('R',L_strDPTNM,25));
								D_OUT.writeBytes(padSTRING('L',L_strTOTHOURS,10));
								D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTDAYS,3),10));
								//D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAMNT,2),10));
								crtNWLIN();
								L_intTOTHRS=0;
								L_intTOTMIN=0;
								L_strTOTHOURS="";
								L_dblTOTDAYS=0;
								//L_dblTOTAMNT=0;											
							}	
						}
						L_intTOTHRS += Integer.parseInt(M_rstRSSET.getString("HRS"));
						L_intGTOTHRS += Integer.parseInt(M_rstRSSET.getString("HRS"));
						L_intTOTMIN += Integer.parseInt(M_rstRSSET.getString("MNS"));
						L_intGTOTMIN += Integer.parseInt(M_rstRSSET.getString("MNS"));
						//L_dblTOTAMNT += L_dblAMNT;
						//L_dblGTOTAMNT += L_dblAMNT;
						if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)"))
						{
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("SW_EMPNO"),5));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_EMPNM"),25));
							D_OUT.writeBytes(padSTRING('R',M_rstRSSET.getString("EP_DPTNM"),12));
							D_OUT.writeBytes(padSTRING('R',nvlSTRVL(M_rstRSSET.getString("EP_ACCRF"),""),12));
							//D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("EH_BASAL"),10));
							//D_OUT.writeBytes(padSTRING('L',M_rstRSSET.getString("EH_DNALW"),10));
							D_OUT.writeBytes(padSTRING('L',L_strHOURS,10));
							D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblDAYS,3),11));
							D_OUT.writeBytes(padSTRING('L',staYEAR[M_rstRSSET.getInt("LP_MONTH")]+"-"+M_rstRSSET.getString("LP_YEAR"),14));
							if(M_rstRSSET.getString("SW_OTSDT")!=null)
								D_OUT.writeBytes(padSTRING('L',M_fmtLCDAT.format(M_rstRSSET.getDate("SW_OTSDT")),14));
							else
								D_OUT.writeBytes(padSTRING('L',"",10));
							//D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblAMNT,2),10));
							crtNWLIN();
						}
						strOLD_DPTCD=M_rstRSSET.getString("EP_DPTCD");
						L_strDPTNM = M_rstRSSET.getString("EP_DPTNM");
					}
					if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)"))
					{
						L_strTOTHOURS = calHOURS(String.valueOf(L_intTOTHRS),String.valueOf(L_intTOTMIN));
						L_dblTOTDAYS = Double.parseDouble(calRND_HOURS(L_strTOTHOURS))*2/8;
								
						D_OUT.writeBytes(padSTRING('R',strOLD_DPTCD,5));
						D_OUT.writeBytes(padSTRING('R',L_strDPTNM,25));
						D_OUT.writeBytes(padSTRING('L',L_strTOTHOURS,10));
						D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTDAYS,3),10));
						//D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblTOTAMNT,2),10));
						crtNWLIN();
					}
					if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)"))
					{
						L_strGTOTHOURS = calHOURS(String.valueOf(L_intGTOTHRS),String.valueOf(L_intGTOTMIN));
						L_dblGTOTDAYS = Double.parseDouble(calRND_HOURS(L_strGTOTHOURS))*2/8;
						D_OUT.writeBytes(padSTRING('L',"",30));
						D_OUT.writeBytes(padSTRING('L',"-----------------------------------------",30));
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('L',"Grand Total",25));
						D_OUT.writeBytes(padSTRING('L',"",5));
						D_OUT.writeBytes(padSTRING('L',L_strGTOTHOURS,10));
						D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOTDAYS,3),10));
						//D_OUT.writeBytes(padSTRING('L',setNumberFormat(L_dblGTOTAMNT,2),10));
						crtNWLIN();
						D_OUT.writeBytes(padSTRING('L',"",30));
						D_OUT.writeBytes(padSTRING('L',"-----------------------------------------",30));
						crtNWLIN();
					}
				}
				else
					setMSG("No Data Found..",'E');	
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			crtNWLIN();
			genRPFTR();
			if(M_rdbHTML.isSelected())
			{
			    D_OUT.writeBytes("</fontsize></PRE></P></BODY></HTML>");    
			}
			D_OUT.close();
			F_OUT.close();
			if(M_rstRSSET==null)
			{
				M_rstRSSET.close();
			}
			chkOTSDT.setSelected(false);
			txtSTRDT.setVisible(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Report");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}

	/**
	 * sets time from 60 hrs calculation to 100 hrs. e.g if 47.30 >> 47.50
	 * if 14.15 >> 14.30    if 12.45 >> 12.75 
	 */
	private String calRND_HOURS(String LP_TIME)
	{
		try
		{
			int intMNS = Integer.parseInt(LP_TIME.substring(3,5));
			int intMNS_RND = 100*intMNS/60;
			String HRS = LP_TIME.substring(0,2);
			String MNS = (String.valueOf(intMNS_RND).length()==1?"0"+intMNS_RND:""+intMNS_RND);
			//System.out.println(LP_TIME+">>>>>>>>"+HRS+"."+MNS);
			return HRS+"."+MNS;
		}
		catch(Exception E)
		{
			setMSG(E,"calRND_HOURS()");
		}
		return LP_TIME;
	}
	
	/**
	 * Calculates total Hours and minutes for given hours and minutes.
	 */
	private String calHOURS(String LP_HRS,String LP_MNS)
	{
		try
		{
			int intHRS = Integer.parseInt(LP_HRS);
			int intMNS = Integer.parseInt(LP_MNS);
			int intMNS_DIV = intMNS/60;
			int intMNS_REM = intMNS%60;
			intHRS+=intMNS_DIV;
			intMNS=intMNS_REM;
			String HRS = (String.valueOf(intHRS).length()==1?"0"+intHRS:""+intHRS);
			String MNS = (String.valueOf(intMNS).length()==1?"0"+intMNS:""+intMNS);
			return HRS+"."+MNS;
		}
		catch(Exception E)
		{
			setMSG(E,"calHOURS()");
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

	/**Method to creat HTML Table*/
	private void crtTBL(DataOutputStream L_DOUT,int P_intBORDR) throws Exception
	{
		String L_strWIDTH="";
		if(M_rdbHTML.isSelected())
		{
				L_DOUT.writeBytes("<p><TABLE border="+P_intBORDR+" borderColor=ghostwhite borderColorDark=ghostwhite borderColorLight=gray  cellPadding=0 cellSpacing=0  width=\"100%\" align=center>");
			flgTBLDT=true;
		}
	}
	/**Method to end HTML Table*/
	private void endTABLE(DataOutputStream L_DOUT) throws Exception
	{
		if(M_rdbHTML.isSelected())
			L_DOUT.writeBytes("</TABLE></P>");
		flgTBLDT=false;
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
				D_OUT.writeBytes("<HTML><HEAD><Title>Attendance Report</title> </HEAD> <BODY><P><PRE style =\" font-size : 8 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}	

			cl_dat.M_PAGENO +=1;
		    if(!cmbRPTYP.getSelectedItem().toString().equals("Details of Overtime hours"))
			{
				crtNWLIN();
	    		prnFMTCHR(D_OUT,M_strBOLD);
	    		D_OUT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,50));
	    		D_OUT.writeBytes(padSTRING('L',"Report Date: ",40));
			    D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_strLOGDT_pbst ,10));
	    		crtNWLIN();
				if(cmbRPTYP.getSelectedItem().toString().equals("Overtime Detail"))
					D_OUT.writeBytes(padSTRING('R',"Overtime Detail ",75));
				else if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)"))
					D_OUT.writeBytes(padSTRING('R',"OT Wages Summary (Employee Number Wise) ",75));
				else if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)"))
					D_OUT.writeBytes(padSTRING('R',"OT Wages Summary (Department Wise) ",75));
				D_OUT.writeBytes(padSTRING('L',"Page No    : ",15));
				D_OUT.writeBytes(padSTRING('R',""+cl_dat.M_PAGENO,10));
	    		crtNWLIN();
				if(rdbWRKDT.isSelected())	
					D_OUT.writeBytes(padSTRING('R',"Working Date From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),75));
				else if(rdbOTSDT.isSelected())
					D_OUT.writeBytes(padSTRING('R',"Accounts Submission Date From "+txtSTRDT.getText()+" To "+txtENDDT.getText(),75));
				
				prnFMTCHR(D_OUT,M_strNOBOLD);
	
				crtNWLIN();
	    		crtNWLIN();
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();
			}
			if(cmbRPTYP.getSelectedItem().toString().equals("Overtime Detail"))
				D_OUT.writeBytes("Dept Code & Name   Employee Category,No & Name   Shift   In-Time   Out-Time   OT/CO   OT/CO Hrs  Auth By   Remark");
			else if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)"))
			{			
				D_OUT.writeBytes("Employee No & Name            Department  Acc.Ref          Total     Total       Period      Submission");
				crtNWLIN();
				D_OUT.writeBytes("                                          No.              Hrs.      Days                    Date");
			}
			else if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)"))
			{			
				D_OUT.writeBytes("Department Code & Name             Total     Total");
				crtNWLIN();
				D_OUT.writeBytes("                                    Hrs.      Days");
			}
			//else if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Employee Number Wise)"))
			//{			
			//	D_OUT.writeBytes("Employee No & Name            Department  Acc.Ref          BASIC        DA     Total     Total     Total");
			//	crtNWLIN();
			//	D_OUT.writeBytes("                                          No.                                   Hrs.      Days   Ammount");
			//}
			//else if(cmbRPTYP.getSelectedItem().toString().equals("OT Wages Summary (Department Wise)"))
			//{			
			//	D_OUT.writeBytes("Department Code & Name             Total     Total     Total");
			//	crtNWLIN();
			//	D_OUT.writeBytes("                                    Hrs.      Days   Ammount");
			//}
			else if(cmbRPTYP.getSelectedItem().toString().equals("Details of Overtime hours"))
			{	
				D_OUT.writeBytes("<br>");
				D_OUT.writeBytes("<HTML><HEAD><Title></title><P align=center><STRONG><FONT face=Arial size=5>"+cl_dat.M_strCMPNM_pbst+"</FONT></STRONG></P><P align=center><STRONG><FONT face=Arial size=3>"+cl_dat.M_strCMPLC_pbst+"</FONT></STRONG></P>" +
								"<P align=right><STRONG><FONT face=Arial size=2>DATE:"+cl_dat.M_strLOGDT_pbst+"</FONT></STRONG></P>"+
								"<p align=right><STRONG><FONT face=Arial size=2>TO:<strong><strong>HRD DIVISION</strong></strong></STRONG></FONT></p>"+
								"<P align=center><STRONG><FONT face=Arial size=2>Details of overtime hours performed by the employee </FONT></STRONG></P>"+		
								"<p align=left><STRONG><FONT face=Arial size=2>DEPARTMENT:"+lblDPTNM.getText()+"</FONT></STRONG></P>"+
								"<p align=left><STRONG><FONT face=Arial size=2>Period From: "+txtSTRDT.getText()+ "   To: "+txtENDDT.getText()+"</FONT></STRONG></P>"+
								//"<p align=left><STRONG><FONT face=Arial size=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TO: "+txtENDDT.getText()+"</FONT></STRONG></P>"+
								" </HEAD> <BODY><P><PRE style =\" font-size :10 pt \">");    
				D_OUT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
				
				crtTBL(D_OUT,1);
			    D_OUT.writeBytes("<tr>");
			    D_OUT.writeBytes("<td align ='center' width='1%' rowspan='2' ><b>Sr.No</b></td>");
			    D_OUT.writeBytes("<td align ='center' width='5%' rowspan='2' ><b>Name of Employee</b></td>");
				D_OUT.writeBytes("<td align ='center' width='2%' rowspan='2' ><b>Emp. No.</b></td>");
				D_OUT.writeBytes("<td align ='center' width='2%' colspan='3'><b>Original Shift</b></td>");
				D_OUT.writeBytes("<td align ='center' width='2%' colspan='2'><b>O.T.Hours</b></td>");
				D_OUT.writeBytes("<td align ='center' width='2%' rowspan='2'><b>Total O.T. Hours</b></td>");
			    D_OUT.writeBytes("</tr>");
			    D_OUT.writeBytes("<tr>");
			    D_OUT.writeBytes("<td align ='center' width='2%' ><b>Work Date</b></td>");
			    D_OUT.writeBytes("<td align ='center' width='2%' ><b>From</b></td>");
			    D_OUT.writeBytes("<td align ='center' width='2%' ><b>To</b></td>");
			    D_OUT.writeBytes("<td align ='center' width='2%' ><b>From</b></td>");
			    D_OUT.writeBytes("<td align ='center' width='2%' ><b>To</b></td>");
			    D_OUT.writeBytes("</tr>");
			    
			}
			else
			{
				crtNWLIN();
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();
			}
				
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
			if(!cmbRPTYP.getSelectedItem().toString().equals("Details of Overtime hours"))
			{	
				D_OUT.writeBytes(padSTRING('L',"",65));
				crtNWLIN();
				D_OUT.writeBytes("----------------------------------------------------------------------------------------------------------------------");
				crtNWLIN();
				prnFMTCHR(D_OUT,M_strNOCPI17);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
					prnFMTCHR(D_OUT,M_strEJT);		
			}
			else
			{
				D_OUT.writeBytes(padSTRING('R',"_______________________________________",65));
				D_OUT.writeBytes(padSTRING('L',"______________________",55));
				D_OUT.writeBytes("\n"+padSTRING('R',"  SHIFT IN-CHARGE/ENGINEER/OFFICER",65));
				D_OUT.writeBytes(padSTRING('L',"  HOS/HOD",45));
			}
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
			setMSG("",'N');
			if(!cmbRPTYP.getSelectedItem().toString().equals("Details of Overtime hours"))
			{
				if(!(rdbWRKDT.isSelected() || rdbOTSDT.isSelected()))
				{
					rdbWRKDT.requestFocus();
					setMSG("Please Select Work Date / Accounts Submission Date",'E');
					return false;				
				}
				if(!rdbOTSDT.isSelected())
			    {
					if(!chkOTSDT.isSelected())
						if(txtSTRDT.getText().length()<10)
						{
							txtSTRDT.requestFocus();
							setMSG("Please Enter From Date",'E');
							return false;
						}	
					if(txtENDDT.getText().length()<10)
					{
						txtENDDT.requestFocus();
						setMSG("Please Enter To Date",'E');
						return false;
					}		
				}
			}
			/*else
			{
				if(txtDPTCD.getText().length()==0)
				{
					txtDPTCD.requestFocus();
					setMSG("Please Enter Dept Code  or Press F1 to Select from List..",'E');
					return false;				
				}
			}*/
		
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
			     strRPFNM = strRPLOC + "hr_rpota.html";
			if(M_rdbTEXT.isSelected())
			     strRPFNM = strRPLOC + "hr_rpota.doc";
				
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
			if(((JTextField)input).getText().length() == 0)
				return true;
				
			if(input == txtDPTCD)
			{
				M_strSQLQRY=" Select CMT_CODDS from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'COXXDPT'";
				M_strSQLQRY+=" and CMT_STSFL <> 'X' and CMT_CODCD='"+txtDPTCD.getText().trim()+"'";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblDPTNM.setText(M_rstRSSET.getString("CMT_CODDS"));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Department Code",'E');
					return false;
				}			
			}	
			if(input == txtEMPNO)
			{
				M_strSQLQRY = " select distinct EP_EMPNO,EP_LSTNM||' '||substr(EP_FSTNM,1,1)||'.'||substr(EP_MDLNM,1,1)||'.' EP_EMPNM from HR_EPMST where EP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EP_EMPNO='"+txtEMPNO.getText().trim()+"' and EP_STSFL <> 'U' ";
				if(txtDPTCD.getText().length()>0)
					M_strSQLQRY +=" and EP_DPTCD = '"+txtDPTCD.getText().trim()+"'";
				//System.out.println("<<<<"+M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET.next() && M_rstRSSET!=null)
				{
					lblEMPNM.setText(M_rstRSSET.getString("EP_EMPNM"));
					setMSG("",'N');
				}	
				else
				{
					setMSG("Enter Valid Employee Code",'E');
					return false;
				}			
			}	
			if(input == txtSTRDT)
			{
				if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
				{
					setMSG("Date can not be greater than todays date..",'E');
					return false;
				}
			}
			if(input == txtENDDT)
			{
				if(M_fmtLCDAT.parse(txtENDDT.getText()).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst)) > 0)
				{
					setMSG("Date can not be greater than todays date..",'E');
					return false;
				}
				else if(txtSTRDT.getText().length()>0)
					if(M_fmtLCDAT.parse(txtSTRDT.getText()).compareTo(M_fmtLCDAT.parse(txtENDDT.getText())) > 0)
					{
						setMSG("From Date can not be greater than To date..",'E');
						return false;
					}
			}	
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"INPVF");
		}
		return true;
	}
}
}



