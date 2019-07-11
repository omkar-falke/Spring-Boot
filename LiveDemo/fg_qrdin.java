/*
System Name   : Finished Goods Inventory Management System
Program Name  : Daily Inventory Query
Program Desc. : Gives Detail regarding Bagging,Despatches & Stock
Author        : Mr. S.r.Mehesare
Date          : 02/12/2005
Version       : FIMS v2.0.0

Modificaitons
Modified By   :
Modified Date :
Modified det. :
Version       :
*/

import java.sql.ResultSet;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JTable;import java.util.Hashtable;import javax.swing.JComboBox;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;import java.util.Date;
import java.io.DataOutputStream;import java.io.FileOutputStream;
import javax.swing.JPanel;import java.awt.Color;
import java.util.Hashtable;

/** <P><PRE >
<b>System Name :</b> 
 
<b>Program Name :</b>

<b>Purpose :</b>

List of tables used :
Table Name      Primary key                          Operation done
                                               Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name    Table name    Type/Size      Description
----------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description         Display Columns                  Table Name
----------------------------------------------------------------------------------------------------------

----------------------------------------------------------------------------------------------------------
<I>
<B>
</B>
</I>
*/
class fg_qrdin extends cl_rbase
{	
	private JLabel lbl1;
	private JComboBox cmbPRDTP;
	private JComboBox cmbQRYON;	
	private cl_JTable TBL_SMMRY;
	private cl_JTable TBL_DETLS;
	private JTextField txtLCKDT;
	private ButtonGroup chkBTNGRP;
	private JRadioButton rdbSMMRY;
	private JRadioButton rdbDETLS;
		
	private FileOutputStream fosREPORT;
    private DataOutputStream dosREPORT;					
			
	private final int TB1_CHKFL = 0;
	private final int TB1_GRADE = 1;
	private final int TB1_RECPT = 2;
	private final int TB1_ISSUE = 3;
	private final int TB1_CLSQT = 4;
	private final int TB1_UCLQT = 5;
	private final int TB1_SLRQT = 6;
	private final int TB1_TDSQT = 7;
	
	private final int TB2_CHKFL = 0;
	private final int TB2_GRADE = 1;
	private final int TB2_RECPT = 2;
	private final int TB2_SLRCT = 3;
	private final int TB2_DODSP = 4;
	private final int TB2_EXDSP = 5;
	private final int TB2_DEDSP = 6;
	private final int TB2_CCDSP = 7;
	private final int TB2_SRDSP = 8;
	
	
	double L_dblNRCQT=0,L_dblNSRQT=0,L_dblDODQT=0,L_dblEXDQT=0;
	double L_dblDEDQT=0,L_dblCCDQT=0,L_dblSLDQT=0,L_dblNSTQT=0;
	double L_dblNUCQT=0,L_dblNTDQT=0;
	//for total
	double L_dblNRCQT1=0,L_dblNSRQT1=0,L_dblDODQT1=0,L_dblEXDQT1=0;
	double L_dblDEDQT1=0,L_dblCCDQT1=0,L_dblSLDQT1=0,L_dblNSTQT1=0;
	double L_dblNUCQT1=0,L_dblNTDQT1=0;
	double L_dblTOTA1=0,L_dblTOTB1=0;
	///for grand Total
	double L_dblNRCQ2=0,L_dblNSRQ2=0,L_dblDODQ2=0,L_dblEXDQ2=0;
	double L_dblDEDQ2=0,L_dblCCDQ2=0,L_dblSLDQ2=0,L_dblNSTQ2=0;
	double L_dblNUCQ2=0,L_dblNTDQ2=0;
	double L_dblTOTA2=0,L_dblTOTB2=0;
		
	double L_dblNRCQ3=0,L_dblNSRQ3=0,L_dblDODQ3=0,L_dblEXDQ3=0;
	double L_dblDEDQ3=0,L_dblCCDQ3=0,L_dblSLDQ3=0,L_dblNSTQ3=0;
	double L_dblNUCQ3=0,L_dblNTDQ3=0;
			
	private String L_strPRMGR="",L_strPRSGR = "",L_strOPRMGR="",L_strOPRSGR = "";
	private String L_strSTSFL="",L_strOSTSFL="",L_strPRSDS="";
	
	private String strDOTLN = "---------------------------------------------------------------------------------------------------------------------";
	private String strQRYDT1 = cl_dat.M_strLOGDT_pbst;
	int intRECCT;
	
	String strSBPRD,strCODDS,strFLDQT,strCODCD,strPKGQT,strUCLQT,strSTKQT,strTDSQT,strLCKDT,strMNPRD,strGRADE;
	String strRECPT,strSLRCT,strRC,strDODSP,strEXDSP,strDEDSP,strCCDSP,strSLRDS,strTODSP,strSLRQT,strSRDSP,strQRYDT;
	
	private String strFILNM;	
		
	String strRCT1P,strRCTQT,strISSTP,strSALTP,strISSQT,strRESFL,strDOSQT,strDOUQT;
	
	String strSTSFL = "";
			
	int intCOUNT = 0;
	double dblTORCT = 0;
	double dblTOISS = 0;
	double dblTODOS = 0;
	double dblTODOU = 0;
	double dblNRCQT = 0;
	double dblNSRQT = 0;
	double dblDODQT = 0;
	double dblEXDQT = 0;
	double dblDEDQT = 0;
	double dblCCDQT = 0;
	double dblSLDQT = 0;
	double dblNSTQT = 0;
	double dblNUCQT = 0;
	double dblNTDQT = 0;
	
	boolean flgSMRYFL = false;	
	Hashtable<String,String> hstPDMGR;
	Hashtable<String,String> hstSTSDS;	
	fg_qrdin()
	{
		super(2);
		try
		{
			setMatrix(20,8);		
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
		
			add(new JLabel("Product Type"),2,2,1,1,this,'L');
			add(cmbPRDTP = new JComboBox(),2,3,1,1.7,this,'L');
			add(new JLabel("Date"),2,5,1,.5,this,'L');
			add(txtLCKDT = new TxtDate(),2,6,1,1.5,this,'R');
			
			add(new JLabel("Query"),3,2,1,1,this,'L');
			add(cmbQRYON = new JComboBox(),3,3,1,1.7,this,'L');										
			
			add(rdbSMMRY = new JRadioButton("Summary",true),3,5,1,1,this,'L');
			add(rdbDETLS = new JRadioButton("Details"),3,6,1,1,this,'L');

			ButtonGroup L_bgpTEMP = new ButtonGroup();
			L_bgpTEMP.add(rdbSMMRY);
			L_bgpTEMP.add(rdbDETLS);
			
			add(new JLabel("Summary"),5,1,1,3,this,'L');
			add(lbl1 = new JLabel("<-------------------Stock as on "+cl_dat.M_strLOGDT_pbst+" at 07:00 Hrs.------------------->"),5,8,1,5,this,'R');
			lbl1.setForeground(Color.blue);
			
			String[] L_strTBLHD = {"Select","Grades","Bagging","Despatches","Clsfd. Stk","Unclf. Stk.","Sales Rtn","T/DSP"};
			int[] L_intCOLSZ = {20,105,105,105,105,100,100,100};
			TBL_SMMRY = crtTBLPNL1(this,L_strTBLHD,20,6,1,5.8,7.9,L_intCOLSZ,new int[]{0});
		
			add(new JLabel("Bagging Details"),12,1,1,3,this,'L');
			add(new JLabel("<------------------------------------------Descpatches----------------------------------->"),12,8,1,5,this,'R');
			
			String[] L_strTBLHD1 = {"Select","Grades","Bagging","Sales Rtn.","Domestic","Export","Deemed Exp.","Cap. Cons.","Sales Return"};
			int[] L_intCOLSZ1 = {20,90,90,90,90,90,90,90,90};
			TBL_DETLS = crtTBLPNL1(this,L_strTBLHD1,20,13,1,5.8,7.9,L_intCOLSZ1,new int[]{0});
			M_pnlRPFMT.setVisible(true);
			setENBL(false);
			
			hstPDMGR = new Hashtable<String,String>();
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,4)CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='MST'";
			M_strSQLQRY += " AND CMT_CGSTP='COXXPGR' AND isnull(CMT_STSFL,'')<>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstPDMGR.put(nvlSTRVL(M_rstRSSET.getString("CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			// status Flag description
			hstSTSDS = new  Hashtable<String,String>();
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='STS'";
			M_strSQLQRY += " AND CMT_CGSTP='PRXXPRD' AND isnull(CMT_STSFL,'')<>'X'";
//			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);			
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstSTSDS.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			
			cmbQRYON.removeActionListener(this);
			cmbPRDTP.removeActionListener(this);
			cmbQRYON.addItem("For the Day");
			cmbQRYON.addItem("For the Month");
			cmbQRYON.addItem("For the Year");
					
			M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,2)CODCD,CMT_CODDS from CO_CDTRN where";
			M_strSQLQRY += " CMT_CGMTP='MST' AND CMT_CGSTP='COXXPGR' AND CMT_CCSVL='MG'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					cmbPRDTP.addItem(nvlSTRVL(M_rstRSSET.getString("CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			cmbQRYON.addActionListener(this);
			cmbPRDTP.addActionListener(this);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		TBL_SMMRY.cmpEDITR[TB1_CHKFL].setEnabled(false);
		TBL_SMMRY.cmpEDITR[TB1_GRADE].setEnabled(false);
		TBL_SMMRY.cmpEDITR[TB1_RECPT].setEnabled(false);
		TBL_SMMRY.cmpEDITR[TB1_ISSUE].setEnabled(false);
		TBL_SMMRY.cmpEDITR[TB1_CLSQT].setEnabled(false);
		TBL_SMMRY.cmpEDITR[TB1_UCLQT].setEnabled(false);
		TBL_SMMRY.cmpEDITR[TB1_SLRQT].setEnabled(false);
		TBL_SMMRY.cmpEDITR[TB1_TDSQT].setEnabled(false);
						
		TBL_DETLS.cmpEDITR[TB2_CHKFL].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_GRADE].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_RECPT].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_SLRCT].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_DODSP].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_EXDSP].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_DEDSP].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_CCDSP].setEnabled(false);
		TBL_DETLS.cmpEDITR[TB2_SRDSP].setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{
			String L_ACT = L_AE.getActionCommand();
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()>0)
				{
					setENBL(true);					
					strQRYDT = cl_dat.getPRMCOD("CMT_CCSVL","S"+cl_dat.M_strCMPCD_pbst,"FGXXREF","DOCDT");
					txtLCKDT.setText(strQRYDT);       				
/*
					if(cmbQRYON.getItemCount() == 0)
					{
						cmbQRYON.addItem("For the Day");
						cmbQRYON.addItem("For the Month");
						cmbQRYON.addItem("For the Year");
					
						M_strSQLQRY = "Select SUBSTRING(CMT_CODCD,1,2)CODCD,CMT_CODDS from CO_CDTRN where";
						M_strSQLQRY += " CMT_CGMTP='MST' AND CMT_CGSTP='COXXPGR' AND CMT_CCSVL='MG'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)
						{
							while(M_rstRSSET.next())
								cmbPRDTP.addItem(nvlSTRVL(M_rstRSSET.getString("CODCD"),"")+" "+nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
							M_rstRSSET.close();
						}
					}*/
				}
				else
				{
					setENBL(false);
					lbl1.setText("<-------------------Stock as on "+ cl_dat.M_strLOGDT_pbst +" at 07:00 Hrs.------------------->");
				}
			}
			else if(M_objSOURC == cmbQRYON)
			{	
				if(cmbQRYON.getSelectedIndex() == 2)
				{
					rdbSMMRY.setEnabled(false);
					rdbDETLS.setSelected(true);
				}
				else				
					rdbSMMRY.setEnabled(true);
				txtLCKDT.requestFocus();
				setMSG("Please Enter Date to view the Stock Details..",'N');
			}			
			else if(M_objSOURC == cmbPRDTP)
			{
				txtLCKDT.requestFocus();
				setMSG("Please Enter Date to view the Stock Details..",'N');
			}
			else if(M_objSOURC == txtLCKDT)
			{				
				if(txtLCKDT.getText().trim().length() == 0)
				{
					lbl1.setText("<-------------------Stock as on "+ cl_dat.M_strLOGDT_pbst+" at 07:00 Hrs.------------------->");			
					setMSG("Please Click Print Button to view the Stock Details..",'N');
					return;
				}
				else			
				{
					String L_CODCD = cmbPRDTP.getSelectedItem().toString().trim().substring(0,2);
					if(cmbQRYON.getSelectedIndex() == 0)
					{						
						txtLCKDT.requestFocus();						
						strLCKDT = txtLCKDT.getText().toString().trim();						
						if(strQRYDT.equals(strLCKDT))
						{							
							M_strSQLQRY = "select SUBSTRING(OP_PRDCD,1,4)L_SBPRD,CMT_CODDS,sum(OP_DRCQT) L_RECPT,"
								+ "sum(OP_DXRQT) L_SLRCT,sum(OP_DRCQT+OP_DXRQT) L_TORCT,sum(OP_D1DQT) L_DODSP,"
								+ "sum(OP_D2DQT) L_EXDSP,sum(OP_D3DQT) L_DEDSP,sum(OP_D6DQT) L_CCDSP,"
								+ "sum(OP_DXDQT) L_SRDSP,sum(OP_D1DQT+OP_D2DQT+OP_D3DQT+OP_DXDQT) L_TODSP,"
								+ "sum(OP_UCLQT) L_UCLQT,sum(OP_STKQT) L_STKQT,sum(OP_SLRQT) L_SLRQT,"
								+ "sum(OP_TDSQT) L_TDSQT from FG_OPSTK,CO_CDTRN where CMT_CGMTP='MST'"
								+ " AND CMT_CGSTP='COXXPGR' AND CMT_CCSVL='SG' AND"
								+ " SUBSTRING(CMT_CODCD,1,4)=SUBSTRING(OP_PRDCD,1,4) AND"
								+ " SUBSTRING(CMT_CODCD,1,2)='"+L_CODCD+"'"
								+ " group by SUBSTRING(OP_PRDCD,1,4),CMT_CODDS "
								+ " order by L_SBPRD,CMT_CODDS ";						
						}
						else
						{															
							//get1stREC(" CMT_CODDS,SUBSTRING(CMT_CODCD,1,4) ","SG"," AND SUBSTRING(CMT_CODCD,1,2)='"+L_CODCD+"' ");							
							lbl1.setText("<-------------------Stock as on "+strLCKDT+" at 07:00 Hrs.------------------->");							
							intCOUNT = 0;
							M_strSQLQRY = "Select CMT_CODDS,SUBSTRING(CMT_CODCD,1,4)CODCD from co_cdtrn where CMT_CGMTP='MST'"
								+ " AND CMT_CGSTP='COXXPGR' AND CMT_CCSVL='SG'" + " AND SUBSTRING(CMT_CODCD,1,2)='"+L_CODCD+"' ";
							M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);							
							if(M_rstRSSET != null)
							{								
								while(M_rstRSSET.next())
								{									
									strCODDS = M_rstRSSET.getString("CMT_CODDS").trim();
									strCODCD = M_rstRSSET.getString("CODCD").trim();									
									TBL_DETLS.setValueAt(strCODDS,intCOUNT,TB2_GRADE);
											
									setRCTDTL(" FG_RCTRN,PR_LTMST "," RCT_RCTDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strLCKDT))+"' AND RCT_STSFL='2' AND RCT_RCT1P in ('10','15','30') AND RCT_PRDTP=LT_PRDTP AND RCT_LOTNO=LT_LOTNO AND SUBSTRING(LT_PRDCD,1,4)='"+strCODCD+"'");									
								    setDSPDTL(" FG_ISTRN "," IST_ISSDT = '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(strLCKDT))+"' AND IST_ISSTP in ('10','30') AND IST_SALTP in ('01','12','03','04') AND IST_STSFL='2' AND SUBSTRING(IST_PRDCD,1,4)='"+strCODCD+"'");									
											
									//setSTKDTL(" FG_STMST "," (ST_DOSQT+ST_DOUQT) > 0 AND SUBSTRING(ST_PRDCD,1,4)='"+strCODCD+"'");									
									dblTODOS = 0;
									dblTODOU = 0;			
									M_strSQLQRY = "Select st_resfl,st_stsfl,sum(st_dosqt) L_DOSQT,sum(st_douqt) L_DOUQT";
									M_strSQLQRY += " from FG_STMST where (ST_DOSQT+ST_DOUQT) > 0 AND SUBSTRING(ST_PRDCD,1,4)='" +strCODCD +"' group by st_resfl,st_stsfl order by st_resfl,st_stsfl";
			
									ResultSet L_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
									if(L_rstRSSET1.next())
									{
										do
										{
											strRESFL = nvlSTRVL(L_rstRSSET1.getString("st_resfl").trim(),"");
											strSTSFL = nvlSTRVL(L_rstRSSET1.getString("st_stsfl").trim(),"");
											strDOSQT = nvlSTRVL(L_rstRSSET1.getString("L_DOSQT").trim(),"");
											strDOUQT = nvlSTRVL(L_rstRSSET1.getString("L_DOUQT").trim(),"");											
											if(strRESFL.equals("1"))
												TBL_SMMRY.setValueAt(setNumberFormat(Double.valueOf(strDOSQT).doubleValue(),3),intCOUNT,TB1_TDSQT);
											if(strSTSFL.equals("2"))
												TBL_SMMRY.setValueAt(setNumberFormat(Double.valueOf(strDOSQT).doubleValue(),3),intCOUNT,TB1_SLRQT);
													
											dblTODOS += Double.parseDouble(strDOSQT);
											dblTODOU += Double.parseDouble(strDOUQT);
										}
										while(L_rstRSSET1.next());
									}
									else
									{
										TBL_SMMRY.setValueAt("0.000",intCOUNT,TB1_TDSQT);
										TBL_SMMRY.setValueAt("0.000",intCOUNT,TB1_SLRQT);
										dblTODOS = 0;
										dblTODOU = 0;										
									}
									L_rstRSSET1.close();									
											
									TBL_SMMRY.setValueAt(strCODDS,intCOUNT,TB1_GRADE);
									TBL_SMMRY.setValueAt(setNumberFormat(dblTORCT,3),intCOUNT,TB1_RECPT);
									TBL_SMMRY.setValueAt(setNumberFormat(dblTOISS,3),intCOUNT,TB1_ISSUE);
									TBL_SMMRY.setValueAt(setNumberFormat(dblTODOS,3),intCOUNT,TB1_CLSQT);
									TBL_SMMRY.setValueAt(setNumberFormat(dblTODOU,3),intCOUNT,TB1_UCLQT);
											
									intCOUNT++;
								}
								M_rstRSSET.close();
							}
							getCOLTOT(TBL_SMMRY);
							getCOLTOT(TBL_DETLS);
							return;
						}						
					}
					else if(cmbQRYON.getSelectedIndex() == 1)
					{						
						//txtLCKDT.setEnabled(false);						
						M_strSQLQRY = "select SUBSTRING(OP_PRDCD,1,4)L_SBPRD,CMT_CODDS,sum(OP_MRCQT) L_RECPT,"
							+ " sum(OP_MXRQT) L_SLRCT,sum(OP_MRCQT+OP_MXRQT) L_TORCT,sum(OP_M1DQT) L_DODSP,"
							+ " sum(OP_M2DQT) L_EXDSP,sum(OP_M3DQT) L_DEDSP,sum(OP_M6DQT) L_CCDSP,"
							+ " sum(OP_MXDQT) L_SRDSP,sum(OP_M1DQT+OP_M2DQT+OP_M3DQT+OP_MXDQT) L_TODSP,"
							+ " sum(OP_UCLQT) L_UCLQT,sum(OP_STKQT) L_STKQT,sum(OP_SLRQT) L_SLRQT,"
							+ " sum(OP_TDSQT) L_TDSQT from FG_OPSTK,CO_CDTRN where CMT_CGMTP='MST'"
							+ " AND CMT_CGSTP = 'COXXPGR' AND CMT_CCSVL = 'SG' AND"
							+ " SUBSTRING(CMT_CODCD,1,4) = SUBSTRING(OP_PRDCD,1,4) AND"
							+ " SUBSTRING(CMT_CODCD,1,2) ='"+L_CODCD+"'"
							+ " group by SUBSTRING(OP_PRDCD,1,4),CMT_CODDS"
							+ " order by L_SBPRD,CMT_CODDS";										
					}
					else if(cmbQRYON.getSelectedIndex() == 2)
					{
						//txtLCKDT.setEnabled(false);						
						M_strSQLQRY = "select SUBSTRING(OP_PRDCD,1,4)L_SBPRD,CMT_CODDS,sum(op_yrcqt) L_RECPT,"
							+ "sum(op_yxrqt) L_SLRCT,sum(op_yrcqt+op_yxrqt) L_TORCT,sum(op_y1dqt) L_DODSP,"
							+ "sum(op_y2dqt) L_EXDSP,sum(op_y3dqt) L_DEDSP,sum(op_y6dqt) L_CCDSP,"
							+ "sum(op_yxdqt) L_SRDSP,sum(op_y1dqt+op_y2dqt+op_y3dqt+op_yxdqt) L_TODSP,"
							+ "sum(OP_UCLQT) L_UCLQT,sum(OP_STKQT) L_STKQT,sum(OP_SLRQT) L_SLRQT,"
							+ "sum(OP_TDSQT) L_TDSQT from FG_OPSTK,CO_CDTRN where CMT_CGMTP='MST'"
							+ " AND CMT_CGSTP='COXXPGR' AND CMT_CCSVL='SG' AND"
							+ " SUBSTRING(CMT_CODCD,1,4) = SUBSTRING(OP_PRDCD,1,4) AND"
							+ " SUBSTRING(CMT_CODCD,1,2)='"+L_CODCD+"'"
							+ " group by SUBSTRING(OP_PRDCD,1,4),CMT_CODDS"
							+ " order by L_SBPRD, CMT_CODDS";
					}									
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					setMSG("Data Fetching for Main Product in Progress",'N');						
					String L_DSPSTR = "";
					L_DSPSTR = " Summary "+ cmbQRYON.getSelectedItem();					
					lbl1.setText("<-------------------Stock as on "+strQRYDT1+" at 07:00 Hrs.------------------->");						
					intCOUNT = 0;
//					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						TBL_SMMRY.clrTABLE();
						TBL_DETLS.clrTABLE();
						while(M_rstRSSET.next())
						{
							strSBPRD = M_rstRSSET.getString("L_SBPRD").trim();
							strRECPT = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_RECPT").trim()).doubleValue(),3);
							strSLRCT = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_SLRCT").trim()).doubleValue(),3);
							strDODSP = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_DODSP").trim()).doubleValue(),3);
							strEXDSP = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_EXDSP").trim()).doubleValue(),3);
							strDEDSP = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_DEDSP").trim()).doubleValue(),3);
							strCCDSP = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_CCDSP").trim()).doubleValue(),3);
							strSRDSP = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_SRDSP").trim()).doubleValue(),3);
							strUCLQT = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_UCLQT").trim()).doubleValue(),3);
							strSTKQT = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_STKQT").trim()).doubleValue(),3);
							strSLRQT = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_SLRQT").trim()).doubleValue(),3);
							strTDSQT = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_TDSQT").trim()).doubleValue(),3);
							strCODDS = M_rstRSSET.getString("CMT_CODDS").trim();
							strRC = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_TORCT").trim()).doubleValue(),3);
							strTODSP = setNumberFormat(Double.valueOf(M_rstRSSET.getString("L_TODSP").trim()).doubleValue(),3);
										
							TBL_SMMRY.setValueAt(new Boolean(false),intCOUNT,TB1_CHKFL);
							TBL_SMMRY.setValueAt(strCODDS,intCOUNT,TB1_GRADE);
							TBL_SMMRY.setValueAt(strRC,intCOUNT,TB1_RECPT);								
							TBL_SMMRY.setValueAt(strTODSP,intCOUNT,TB1_ISSUE);								
							TBL_SMMRY.setValueAt(strSTKQT,intCOUNT,TB1_CLSQT);								
							TBL_SMMRY.setValueAt(strUCLQT,intCOUNT,TB1_UCLQT);
							TBL_SMMRY.setValueAt(strTDSQT,intCOUNT,TB1_TDSQT);
							TBL_SMMRY.setValueAt(strSLRQT,intCOUNT,TB1_SLRQT);
										
							TBL_DETLS.setValueAt(new Boolean(false),intCOUNT,TB2_CHKFL);
							TBL_DETLS.setValueAt(strCODDS,intCOUNT,TB2_GRADE);
							TBL_DETLS.setValueAt(strRECPT,intCOUNT,TB2_RECPT);
							TBL_DETLS.setValueAt(strSLRCT,intCOUNT,TB2_SLRCT);
							TBL_DETLS.setValueAt(strDODSP,intCOUNT,TB2_DODSP);
							TBL_DETLS.setValueAt(strEXDSP,intCOUNT,TB2_EXDSP);
							TBL_DETLS.setValueAt(strDEDSP,intCOUNT,TB2_DEDSP);
							TBL_DETLS.setValueAt(strCCDSP,intCOUNT,TB2_CCDSP);
							TBL_DETLS.setValueAt(strSRDSP,intCOUNT,TB2_SRDSP);								
							intCOUNT++;
						}							
						M_rstRSSET.close();
						getCOLTOT(TBL_SMMRY);
						getCOLTOT(TBL_DETLS);							
					}						
					setMSG("Click on Product column to get Sub Product details.",'N');
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	/**
	 * Method to generate the Report & to forward it to specified destination.
	 */
	void exePRINT()
	{
		if(!vldDATA())
			return;		
		try
		{
			if(M_rdbHTML.isSelected())
				strFILNM = cl_dat.M_strREPSTR_pbst+"fg_qrdin.html";
			else
				strFILNM = cl_dat.M_strREPSTR_pbst+"fg_qrdin.doc";
			
			getDATA();
			
			/*if(intRECCT == 0)
			{	
				setMSG("No Data found..",'E');
				return;
			}*/
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{					
				if (M_rdbTEXT.isSelected())
				    doPRINT(strFILNM);
				else 
				{    
					Runtime r = Runtime.getRuntime();
					Process p = null;					
					p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
					setMSG("For Printing Select File Menu, then Print  ..",'N');
				}    
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSCN_pbst))
			{
			    Runtime r = Runtime.getRuntime();
				Process p = null;
					
				if(M_rdbHTML.isSelected())
				    p  = r.exec("c:\\windows\\iexplore.exe "+strFILNM); 
				else
				    p  = r.exec("c:\\windows\\wordpad.exe "+strFILNM);
			}
			else if(cl_dat.M_cmbDESTN_pbst.getSelectedItem().toString().trim().equals(cl_dat.M_OPEML_pbst))
			{			
				cl_eml ocl_eml = new cl_eml();				    
			    for(int i=0;i<M_cmbDESTN.getItemCount();i++)
			    {
				    ocl_eml.sendfile(M_cmbDESTN.getItemAt(i).toString().trim(),strFILNM,"Daily Inventory Query"," ");
				    setMSG("File Sent to " + M_cmbDESTN.getItemAt(i).toString().trim() + " Successfuly ",'N');				    
				}				    	    	
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exePRINT");
		}		
	}
	/**
	 * Method to validate the data before execuation of the SQL Quary.
	 */
	boolean vldDATA()
	{
		try
		{
			cl_dat.M_intLINNO_pbst = 0;
			cl_dat.M_PAGENO = 0;
			if(txtLCKDT.getText().trim().length() == 0)
			{
				setMSG("Enter From Date..",'E');
				txtLCKDT.requestFocus();
				return false;
			}
			if(M_fmtLCDAT.parse(txtLCKDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
			{
				setMSG("Date should not be grater than current Date..",'E');
				txtLCKDT.requestFocus();
				return false;
			}			
			if (cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPEML_pbst))
			{
				if (M_cmbDESTN.getItemCount() == 0)
				{					
					setMSG("Please Select the Email/s from List through F1 Help ..",'N');
					return false;
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst))
			{ 
				if (M_cmbDESTN.getSelectedIndex() == 0)
				{	
					setMSG("Please Select the Printer from Printer List ..",'N');
					return false;
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldDATA");
			return false;
		}
		return true;
	}
	/**
	 * Method to fetch data from the database & to club it with header & footer.
	 */
	public void getDATA()
	{
		if(!vldDATA())
			return;			
		try
		{
			intRECCT = 0; 						
			String L_strSQLDAY = "";
			String L_strSQLMTH = "";
			String L_strSQLYER = "";
				
			setCursor(cl_dat.M_curWTSTS_pbst);			
			fosREPORT = new FileOutputStream(strFILNM);
			dosREPORT = new DataOutputStream(fosREPORT);
			setMSG("Report Generation in Process.......",'N');
			String L_CODCD = cmbPRDTP.getSelectedItem().toString().trim().substring(0,2);
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
			{
			    prnFMTCHR(dosREPORT,M_strNOCPI17);
			    prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strCPI17);
			}
			if(M_rdbHTML.isSelected())
			{
			    dosREPORT.writeBytes("<HTML><HEAD><Title>Daily Inventory Query</title> </HEAD> <BODY><P><PRE style =\" font-size : 10 pt \">");    
				dosREPORT.writeBytes("<STYLE TYPE=\"text/css\"> P.breakhere {page-break-before: always}</STYLE>"); 
			}										
			if(rdbSMMRY.isSelected())
			{
				L_strSQLDAY = "Select SUBSTRING(OP_PRDCD,1,2) L_MNPRD,SUBSTRING(OP_PRDCD,1,4) L_SUBPD,"
					+ " sum(OP_DRCQT) L_RCTQT,sum(OP_DXRQT) L_SLRQT,sum(OP_D1DQT) L_DODQT,"
					+ " sum(OP_D2DQT) L_EXDQT,sum(OP_D3DQT) L_DEDQT,sum(OP_D6DQT) L_CCDQT,"
					+ " sum(OP_DXDQT) L_SRDQT,sum(OP_STKQT) L_STKQT,sum(OP_UCLQT) L_UCLQT,"
					+ " sum(OP_TDSQT) L_TDSQT from FG_OPSTK"
					+ " where (OP_DRCQT+OP_D1DQT+OP_D2DQT+OP_D3DQT+OP_D6DQT+OP_STKQT+OP_UCLQT) > 0"
					+ " group by SUBSTRING(OP_PRDCD,1,2),SUBSTRING(OP_PRDCD,1,4)"
					+ " order by L_MNPRD,L_SUBPD";
					
				L_strSQLMTH = "Select SUBSTRING(OP_PRDCD,1,2) L_MNPRD,SUBSTRING(OP_PRDCD,1,4) L_SUBPD,"
					+ " sum(OP_MRCQT) L_RCTQT,sum(OP_MXRQT) L_SLRQT,sum(OP_MRCQT+OP_MXRQT) L_TOTRC,"
					+ " sum(OP_M1DQT) L_DODQT,sum(OP_M2DQT) L_EXDQT,sum(OP_M3DQT) L_DEDQT,"
					+ " sum(OP_M1DQT+OP_M2DQT+OP_M3DQT) L_TODQT,sum(OP_M6DQT) L_CCDQT,"
					+ " sum(OP_MXDQT) L_STKQT,sum(OP_TDSQT) L_TDSQT from FG_OPSTK"
					+ " where (OP_MRCQT+OP_MXRQT+OP_M1DQT+OP_M2DQT+OP_M3DQT+OP_M6DQT+OP_MXDQT) > 0"
					+ " group by SUBSTRING(OP_PRDCD,1,2),SUBSTRING(OP_PRDCD,1,4)"
					+ " order by L_MNPRD,L_SUBPD";										
				flgSMRYFL = true;
			}
			else if(rdbDETLS.isSelected())
			{
				L_strSQLDAY = "Select SUBSTRING(OP_PRDCD,1,4) L_MNPRD,PR_STSFL,OP_PRDCD,PR_PRDDS,sum(OP_DRCQT) L_RCTQT,"
					+ " sum(OP_D1DQT) L_DODQT,sum(OP_D2DQT) L_EXDQT,sum(OP_D3DQT) L_DEDQT,"
					+ " sum(OP_UCLQT) L_UCLQT,sum(OP_D6DQT) L_CCDQT,sum(OP_STKQT) L_STKQT,"
					+ " sum(OP_TDSQT) L_TDSQT,sum(OP_DXDQT) L_SRDQT,sum(OP_DXRQT) L_SLRQT"
					+ " from FG_OPSTK,CO_PRMST,CO_CDTRN where SUBSTRING(OP_PRDCD,1,2)='"+L_CODCD+"'"
					+ " AND (OP_DRCQT+OP_D1DQT+OP_D2DQT+OP_D3DQT+OP_D6DQT+OP_STKQT+OP_UCLQT+OP_TDSQT+OP_DXDQT) > 0"
					+ " AND OP_PRDCD=pr_prdcd AND CMT_CGMTP='STS' AND CMT_CGSTP='PRXXPRD'"
					+ " AND CMT_CODCD=PR_STSFL group by SUBSTRING(OP_PRDCD,1,4),OP_PRDCD,PR_STSFL,PR_PRDDS"
					+ " order by L_MNPRD,PR_STSFL,OP_PRDCD";
					
				L_strSQLMTH = "Select SUBSTRING(OP_PRDCD,1,4) L_MNPRD,PR_STSFL,OP_PRDCD,PR_PRDDS,sum(OP_MRCQT) L_RCTQT,"
					+ " sum(OP_M1DQT) L_DODQT,sum(OP_M2DQT) L_EXDQT,sum(OP_M3DQT) L_DEDQT,"
					+ " sum(OP_UCLQT) L_UCLQT,sum(OP_M6DQT) L_CCDQT,sum(OP_STKQT) L_STKQT,"
					+ " sum(OP_TDSQT) L_TDSQT,sum(OP_MXDQT) L_SRDQT,sum(OP_MXRQT) L_SLRQT"
					+ " from FG_OPSTK,CO_PRMST,CO_CDTRN where SUBSTRING(OP_PRDCD,1,2)='"+L_CODCD+"'"
					+ " AND (OP_MRCQT+OP_M1DQT+OP_M2DQT+OP_M3DQT+OP_M6DQT+OP_STKQT+OP_UCLQT+OP_TDSQT+OP_MXDQT) > 0"
					+ " AND OP_PRDCD=pr_prdcd AND CMT_CGMTP='STS' AND CMT_CGSTP='PRXXPRD'"
					+ " AND CMT_CODCD=PR_STSFL group by SUBSTRING(OP_PRDCD,1,4),OP_PRDCD,PR_STSFL,PR_PRDDS"
					+ " order by L_MNPRD,PR_STSFL,OP_PRDCD";
					
				L_strSQLYER = "Select SUBSTRING(OP_PRDCD,1,4) L_MNPRD,OP_PRDCD,PR_STSFL,PR_PRDDS,sum(op_yrcqt) L_RCTQT,"
					+ " sum(op_y1dqt) L_DODQT,sum(op_y2dqt) L_EXDQT,sum(op_y3dqt) L_DEDQT,"
					+ " sum(OP_UCLQT) L_UCLQT,sum(op_y6dqt) L_CCDQT,sum(OP_STKQT) L_STKQT,"
					+ " sum(OP_TDSQT) L_TDSQT,sum(op_yxdqt) L_SRDQT,sum(op_yxrqt) L_SLRQT"
					+ " from FG_OPSTK,CO_PRMST,CO_CDTRN where SUBSTRING(OP_PRDCD,1,2)='"+L_CODCD+"'"
					+ " AND (op_yrcqt+op_y1dqt+op_y2dqt+op_y3dqt+op_y6dqt+OP_STKQT+OP_UCLQT+OP_TDSQT+op_yxdqt) > 0"
					+ " AND OP_PRDCD=pr_prdcd AND CMT_CGMTP='STS' AND CMT_CGSTP='PRXXPRD'"
					+ " AND CMT_CODCD=PR_STSFL group by SUBSTRING(OP_PRDCD,1,4),OP_PRDCD,PR_STSFL,PR_PRDDS"
					+ " order by L_MNPRD,PR_STSFL,OP_PRDCD";									
			}										
			if(rdbDETLS.isSelected())
			{				
				if(cmbQRYON.getSelectedIndex() == 0)
				{	
//					System.out.println(L_strSQLDAY);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLDAY);
					prnHEADER(true);
					getDATA(M_rstRSSET,"Stock Detail for the Day",true);
				}
				else if(cmbQRYON.getSelectedIndex() == 1)
				{
					//System.out.println(L_strSQLMTH);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLMTH);
					prnHEADER(false);
					getDATA(M_rstRSSET,"Stock Detail for the Month",false);
				}
				else if(cmbQRYON.getSelectedIndex() == 2)
				{
					//System.out.println(L_strSQLYER);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLYER);
					prnHEADER(false);
					getDATA(M_rstRSSET,"Stock Detail for the Year",false);
				}
			}
			else
			{
				if(cmbQRYON.getSelectedIndex() == 0)
				{	
//					System.out.println(L_strSQLDAY);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLDAY);
					prnHEADER(true);
					getDATA(M_rstRSSET,"Stock Summary for the Day",true);
				}
				else if(cmbQRYON.getSelectedIndex() == 1)
				{	
//					System.out.println(L_strSQLMTH);
					M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLMTH);
					prnHEADER(false);
					getDATA(M_rstRSSET,"Stock Summary for the Month",false);
				}
			}										
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}		
	}
	/**
	 * Method to generate Header part of the report.
	 */
	public void prnHEADER(boolean P_flgISOFL)
	{
		try
		{
			cl_dat.M_PAGENO ++;
			String L_strRPT1P = "Summary";
			String L_strDATE = cl_dat.M_strLOGDT_pbst;			
			//String L_strTEMP = "";
			if(rdbDETLS.isSelected())
				L_strRPT1P = "Details";
			if(cmbQRYON.getSelectedIndex() != 0)
				L_strDATE = strQRYDT;
			//if((P_flgISOFL == false) && (rdbSMMRY.isSelected()))
			//	L_strTEMP = "For the Month";
			//else 
			//	L_strTEMP = cmbQRYON.getSelectedItem().toString();
			dosREPORT.writeBytes("\n\n\n");	
			dosREPORT.writeBytes(padSTRING('R',cl_dat.M_strCMPNM_pbst,strDOTLN.length()-22));
			dosREPORT.writeBytes("Date    : "+cl_dat.M_strLOGDT_pbst +"\n");											
			dosREPORT.writeBytes(padSTRING('R',"Stock "+ L_strRPT1P +" "+ cmbQRYON.getSelectedItem().toString()+ " as on " +L_strDATE +" at 07:00 Hrs.",strDOTLN.length()-22));//strTEMP
			dosREPORT.writeBytes("Page No : "+cl_dat.M_PAGENO +"\n");
			dosREPORT.writeBytes(strDOTLN + "\n");
			if(rdbDETLS.isSelected())
			{    
				dosREPORT.writeBytes("                     <---Bagging---->  <-------------------Despatch------------------->    <----------Stock-------->\n");
				dosREPORT.writeBytes("                     Fresh   Sal.Rtn  Domastic    Export  D.Export    Cp.Con   Sal.Rtn     Clsfd   UnClsfd     T/Dsp\n");
			}
			else// summmary 
			{
				if(P_flgISOFL == true) //for the Day
				{
					dosREPORT.writeBytes("                     <---Bagging---->  <-------------------Despatch------------------->    <----------Stock-------->\n");
					dosREPORT.writeBytes("                     Fresh   Sal.Rtn  Domastic    Export  D.Export    Cp.Con   Sal.Rtn     Clsfd   UnClsfd     T/Dsp\n");
				}
				else //for the Day
				{
					dosREPORT.writeBytes("                     <----------Bagging-------->  <------------------------Despatch---------------------->\n");
					dosREPORT.writeBytes("                     Fresh   Sal.Rtn     Total  Domastic    Export  D.Export     Total    Cp.Con   Sal.Rtn\n");
				}
			}
			dosREPORT.writeBytes(strDOTLN +"\n");
			cl_dat.M_intLINNO_pbst = 10;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"prnHEADER");
		}
	}
	private void getDATA(ResultSet P_rstRSSET,String P_strQRYON, boolean P_flgISOFL)
	{
		try
		{
			L_strPRMGR="";L_strPRSGR="";L_strOPRMGR="";L_strOPRSGR = "";
			L_strSTSFL="";L_strOSTSFL="";L_strPRSDS="";
			L_dblNRCQT=0;L_dblNSRQT=0;L_dblDODQT=0;L_dblEXDQT=0;
			L_dblDEDQT=0;L_dblCCDQT=0;L_dblSLDQT=0;L_dblNSTQT=0;
			L_dblNUCQT=0;L_dblNTDQT=0;
			//for total
			L_dblNRCQT1=0;L_dblNSRQT1=0;L_dblDODQT1=0;L_dblEXDQT1=0;
			L_dblDEDQT1=0;L_dblCCDQT1=0;L_dblSLDQT1=0;L_dblNSTQT1=0;
			L_dblNUCQT1=0;L_dblNTDQT1=0;L_dblTOTA1=0;L_dblTOTB1=0;
			///for grand Total
			L_dblNRCQ2=0;L_dblNSRQ2=0;L_dblDODQ2=0;L_dblEXDQ2=0;
			L_dblDEDQ2=0;L_dblCCDQ2=0;L_dblSLDQ2=0;L_dblNSTQ2=0;
			L_dblNUCQ2=0;L_dblNTDQ2=0;L_dblTOTA2=0;L_dblTOTB2=0;
			
			L_dblNRCQ3=0;L_dblNSRQ3=0;L_dblDODQ3=0;L_dblEXDQ3=0;
			L_dblDEDQ3=0;L_dblCCDQ3=0;L_dblSLDQ3=0;L_dblNSTQ3=0;
			L_dblNUCQ3=0;L_dblNTDQ3=0;
			
			StringBuffer stbDATA = new StringBuffer();
			
			if(rdbDETLS.isSelected())
			{				
				stbDATA.delete(0,stbDATA.length());				
				if(P_rstRSSET != null)
				{
					int i = 0;
					while(P_rstRSSET.next())
					{						
						L_strPRMGR = P_rstRSSET.getString("L_MNPRD").trim();
						L_strPRSGR = P_rstRSSET.getString("OP_PRDCD").trim();
						L_strPRSDS = P_rstRSSET.getString("PR_PRDDS");
						L_strSTSFL = P_rstRSSET.getString("PR_STSFL").trim();
						L_dblNRCQT = P_rstRSSET.getDouble("L_RCTQT");
						L_dblNSRQT = P_rstRSSET.getDouble("L_SLRQT");
						L_dblDODQT = P_rstRSSET.getDouble("L_DODQT");
						L_dblEXDQT = P_rstRSSET.getDouble("L_EXDQT");
						L_dblDEDQT = P_rstRSSET.getDouble("L_DEDQT");
						L_dblCCDQT = P_rstRSSET.getDouble("L_CCDQT");						
						L_dblSLDQT = P_rstRSSET.getDouble("L_SRDQT");
						L_dblNSTQT = P_rstRSSET.getDouble("L_STKQT");
						L_dblNUCQT = P_rstRSSET.getDouble("L_UCLQT");
						L_dblNTDQT = P_rstRSSET.getDouble("L_TDSQT");
						
						if(!L_strPRMGR.equals(L_strOPRMGR))
						{
							if(!L_strOPRMGR.equals(""))
							{
								if(cl_dat.M_intLINNO_pbst >58)
								{
									dosREPORT.writeBytes("\n"+strDOTLN);					
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
									prnHEADER(false);
								}
								//dosREPORT.writeBytes("\n  Sub Total");
								calSUBTOT();								
			
								//dosREPORT.writeBytes("\nTotal \n");
								calTOTAL();
							}
							dosREPORT.writeBytes("\n"+hstPDMGR.get(L_strPRMGR).toString());														
							if(!L_strSTSFL.equals(L_strOSTSFL))
							{
								if(cl_dat.M_intLINNO_pbst >58)
								{
									dosREPORT.writeBytes("\n"+strDOTLN);					
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
									prnHEADER(false);
								}
								if(!L_strOSTSFL.equals(""))
									//dosREPORT.writeBytes("\n  Sub Total \n");
									calSUBTOT();
								
								dosREPORT.writeBytes("\n  "+hstSTSDS.get(L_strSTSFL).toString());
								cl_dat.M_intLINNO_pbst++;
								L_strOSTSFL = L_strSTSFL;
							}															
							L_strOPRMGR = L_strPRMGR;
						}
						else
						{
							if(!L_strSTSFL.equals(L_strOSTSFL))
							{
								
								if(cl_dat.M_intLINNO_pbst >58)
								{
									dosREPORT.writeBytes("\n"+strDOTLN);					
									if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
										prnFMTCHR(dosREPORT,M_strEJT);
									if(M_rdbHTML.isSelected())
										dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
									prnHEADER(false);
								}
								//dosREPORT.writeBytes("\n  Sub Total \n");
								calSUBTOT();
								
								dosREPORT.writeBytes("\n  "+hstSTSDS.get(L_strSTSFL).toString());
								cl_dat.M_intLINNO_pbst++;
								L_strOSTSFL = L_strSTSFL;
							}
						}
						dosREPORT.writeBytes("\n    " + padSTRING('R',L_strPRSDS,12)
						+padSTRING('L',setNumberFormat(L_dblNRCQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblNSRQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblDODQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblEXDQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblDEDQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblCCDQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblSLDQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblNSTQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblNUCQT,3),10)
						+padSTRING('L',setNumberFormat(L_dblNTDQT,3),10));					
						cl_dat.M_intLINNO_pbst++;
						
						if(cl_dat.M_intLINNO_pbst >58)
						{
							dosREPORT.writeBytes("\n"+strDOTLN);					
							if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) && (M_rdbTEXT.isSelected()))
								prnFMTCHR(dosREPORT,M_strEJT);
							if(M_rdbHTML.isSelected())
								dosREPORT.writeBytes("<P CLASS = \"breakhere\">");
							prnHEADER(false);
						}
						L_dblNRCQT1 = L_dblNRCQT1 + L_dblNRCQT;
						L_dblNSRQT1 = L_dblNSRQT1 + L_dblNSRQT;
						L_dblDODQT1 = L_dblDODQT1 + L_dblDODQT;
						L_dblEXDQT1 = L_dblEXDQT1 + L_dblEXDQT;
						L_dblDEDQT1 = L_dblDEDQT1 + L_dblDEDQT;
						L_dblCCDQT1 = L_dblCCDQT1 + L_dblCCDQT;
						L_dblSLDQT1 = L_dblSLDQT1 + L_dblSLDQT;
						L_dblNSTQT1 = L_dblNSTQT1 + L_dblNSTQT;
						L_dblNUCQT1 = L_dblNUCQT1 + L_dblNUCQT;
						L_dblNTDQT1 = L_dblNTDQT1 + L_dblNTDQT;											
					}
					//dosREPORT.writeBytes("\n  Sub Total");
					calSUBTOT();
					//dosREPORT.writeBytes("\nTotal \n");
					calTOTAL();
					
					//dosREPORT.writeBytes("\nGrand Total \n");					
					dosREPORT.writeBytes("\n" + padSTRING('R',"Grand Total",16)
					+padSTRING('L',setNumberFormat(L_dblNRCQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblNSRQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblDODQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblEXDQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblDEDQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblCCDQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblSLDQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblNSTQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblNUCQ3,3),10)
					+padSTRING('L',setNumberFormat(L_dblNTDQ3,3),10)+"\n");
				}				
			}
			else
			{
				stbDATA.delete(0,stbDATA.length());
				if(P_rstRSSET != null)
				{
					while(P_rstRSSET.next())
					{						
						L_strPRMGR = P_rstRSSET.getString("L_MNPRD").trim();
						L_strPRSGR = P_rstRSSET.getString("L_SUBPD").trim();											
						L_dblNRCQT = P_rstRSSET.getDouble("L_RCTQT");
						L_dblNSRQT = P_rstRSSET.getDouble("L_SLRQT");						
						L_dblDODQT = P_rstRSSET.getDouble("L_DODQT");
						L_dblEXDQT = P_rstRSSET.getDouble("L_EXDQT");
						L_dblDEDQT = P_rstRSSET.getDouble("L_DEDQT");
						L_dblCCDQT = P_rstRSSET.getDouble("L_CCDQT");						
						L_dblNSTQT = P_rstRSSET.getDouble("L_STKQT");
L_dblNTDQT = P_rstRSSET.getDouble("L_TDSQT");
						if(P_flgISOFL ==  true)
						{							
							L_dblSLDQT = P_rstRSSET.getDouble("L_SRDQT");
							//L_dblNSTQT = P_rstRSSET.getDouble("L_STKQT");
							L_dblNUCQT = P_rstRSSET.getDouble("L_UCLQT");
//							L_dblNTDQT = P_rstRSSET.getDouble("L_TDSQT");
						}
						if(!L_strPRMGR.equals(L_strOPRMGR))
						{
							if(!L_strOPRMGR.equals(""))
							{
								stbDATA.append("\n"+padSTRING('R',"Total",16)
								+padSTRING('L',setNumberFormat(L_dblNRCQT1,3),10)
								+padSTRING('L',setNumberFormat(L_dblNSRQT1,3),10));
								if(P_flgISOFL == false)
									stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTA1,3),10));
								stbDATA.append(padSTRING('L',setNumberFormat(L_dblDODQT1,3),10)
								+padSTRING('L',setNumberFormat(L_dblEXDQT1,3),10)
								+padSTRING('L',setNumberFormat(L_dblDEDQT1,3),10));
								if(P_flgISOFL == false)
									stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTB1,3),10));
								stbDATA.append(padSTRING('L',setNumberFormat(L_dblCCDQT1,3),10));
								
								if(P_flgISOFL == true)
								{
									stbDATA.append(padSTRING('L',setNumberFormat(L_dblSLDQT1,3),10));
									stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT1,3),10));
								}
								else
									stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT1,3),10));
									
								if(P_flgISOFL == true)								
									stbDATA.append(padSTRING('L',setNumberFormat(L_dblNUCQT1,3),10));
								
								if(P_flgISOFL == false)
									stbDATA.append("\n");
								else //if(P_flgISOFL == true)
									stbDATA.append(padSTRING('L',setNumberFormat(L_dblNTDQT1,3),10)+"\n");
								
								L_dblNRCQ2 = L_dblNRCQ2 + L_dblNRCQT1;
								L_dblNSRQ2 = L_dblNSRQ2 + L_dblNSRQT1;
								L_dblDODQ2 = L_dblDODQ2 + L_dblDODQT1;
								L_dblEXDQ2 = L_dblEXDQ2 + L_dblEXDQT1;
								L_dblDEDQ2 = L_dblDEDQ2 + L_dblDEDQT1;
								L_dblCCDQ2 = L_dblCCDQ2 + L_dblCCDQT1;
								L_dblSLDQ2 = L_dblSLDQ2 + L_dblSLDQT1;
								L_dblNSTQ2 = L_dblNSTQ2 + L_dblNSTQT1;
								L_dblNUCQ2 = L_dblNUCQ2 + L_dblNUCQT1;
								L_dblNTDQ2 = L_dblNTDQ2 + L_dblNTDQT1;
								L_dblTOTA2 = L_dblTOTA2 + L_dblTOTA1;
								L_dblTOTB2 = L_dblTOTB2 + L_dblTOTB1;
					
								L_dblNRCQT1=0;L_dblNSRQT1=0;L_dblDODQT1=0;L_dblEXDQT1=0;
								L_dblDEDQT1=0;L_dblCCDQT1=0;L_dblSLDQT1=0;L_dblNSTQT1=0;
								L_dblNUCQT1=0;L_dblNTDQT1=0;L_dblTOTA1=0;L_dblTOTB1=0;
							}
							stbDATA.append("\n"+hstPDMGR.get(L_strPRMGR+"00").toString());
							L_strOPRMGR = L_strPRMGR;
							stbDATA.append("\n  "+padSTRING('R',hstPDMGR.get(L_strPRSGR).toString(),14));
						}
						else						
							stbDATA.append("\n  "+padSTRING('R',hstPDMGR.get(L_strPRSGR).toString(),14));						
						
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNRCQT,3),10));
						//System.out.println("dataupend "+L_dblNSRQT);
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSRQT,3),10));
						if(P_flgISOFL ==  false)
							stbDATA.append(padSTRING('L',setNumberFormat(L_dblNRCQT+L_dblNSRQT,3),10));
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblDODQT,3),10));
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblEXDQT,3),10));
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblDEDQT,3),10));
						if(P_flgISOFL ==  false)
							stbDATA.append(padSTRING('L',setNumberFormat(L_dblDODQT+L_dblEXDQT+L_dblDEDQT,3),10));						
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblCCDQT,3),10));
					
						if(P_flgISOFL == true)
						{
							stbDATA.append(padSTRING('L',setNumberFormat(L_dblSLDQT,3),10));
							stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT,3),10));						
						}
						else
							stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT,3),10));
												
						if(P_flgISOFL == true)
						{
							//stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT,3),10));
							stbDATA.append(padSTRING('L',setNumberFormat(L_dblNUCQT,3),10));
							stbDATA.append(padSTRING('L',setNumberFormat(L_dblNTDQT,3),10));
						}
						L_dblNRCQT1 = L_dblNRCQT1 + L_dblNRCQT;
						L_dblNSRQT1 = L_dblNSRQT1 + L_dblNSRQT;
						L_dblDODQT1 = L_dblDODQT1 + L_dblDODQT;
						L_dblEXDQT1 = L_dblEXDQT1 + L_dblEXDQT;
						L_dblDEDQT1 = L_dblDEDQT1 + L_dblDEDQT;
						L_dblCCDQT1 = L_dblCCDQT1 + L_dblCCDQT;
						L_dblSLDQT1 = L_dblSLDQT1 + L_dblSLDQT;
						L_dblNSTQT1 = L_dblNSTQT1 + L_dblNSTQT;
						L_dblNUCQT1 = L_dblNUCQT1 + L_dblNUCQT;
						L_dblNTDQT1 = L_dblNTDQT1 + L_dblNTDQT;
						L_dblTOTA1 = L_dblTOTA1 + L_dblNRCQT + L_dblNSRQT;
						L_dblTOTB1 = L_dblTOTB1 + L_dblDODQT + L_dblEXDQT + L_dblDEDQT;
					}
					stbDATA.append("\n"+padSTRING('R',"Total",16)
					+padSTRING('L',setNumberFormat(L_dblNRCQT1,3),10)
					+padSTRING('L',setNumberFormat(L_dblNSRQT1,3),10));
					if(P_flgISOFL == false)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTA1,3),10));
					stbDATA.append(padSTRING('L',setNumberFormat(L_dblDODQT1,3),10)
					+padSTRING('L',setNumberFormat(L_dblEXDQT1,3),10)
					+padSTRING('L',setNumberFormat(L_dblDEDQT1,3),10));
					if(P_flgISOFL == false)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTB1,3),10));
					stbDATA.append(padSTRING('L',setNumberFormat(L_dblCCDQT1,3),10));
					
					if(P_flgISOFL == true)
					{
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblSLDQT1,3),10));
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT1,3),10));
					}
					else
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT1,3),10));
					
					if(P_flgISOFL == true)
					{
						//stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQT1,3),10)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNUCQT1,3),10));
					}
					if(P_flgISOFL == false)
						stbDATA.append("\n");
					else if(P_flgISOFL == true)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNTDQT1,3),10)+"\n");
								
					L_dblNRCQ2 = L_dblNRCQ2 + L_dblNRCQT1;
					L_dblNSRQ2 = L_dblNSRQ2 + L_dblNSRQT1;
					L_dblDODQ2 = L_dblDODQ2 + L_dblDODQT1;
					L_dblEXDQ2 = L_dblEXDQ2 + L_dblEXDQT1;
					L_dblDEDQ2 = L_dblDEDQ2 + L_dblDEDQT1;
					L_dblCCDQ2 = L_dblCCDQ2 + L_dblCCDQT1;
					L_dblSLDQ2 = L_dblSLDQ2 + L_dblSLDQT1;
					L_dblNSTQ2 = L_dblNSTQ2 + L_dblNSTQT1;
					L_dblNUCQ2 = L_dblNUCQ2 + L_dblNUCQT1;
					L_dblNTDQ2 = L_dblNTDQ2 + L_dblNTDQT1;
					
					L_dblTOTA2 = L_dblTOTA2 + L_dblTOTA1;
					L_dblTOTB2 = L_dblTOTB2 + L_dblTOTB1;
					
					stbDATA.append("\n"+padSTRING('R',"Grand Total",16)
					+padSTRING('L',setNumberFormat(L_dblNRCQ2,3),10)
					+padSTRING('L',setNumberFormat(L_dblNSRQ2,3),10));
					if(P_flgISOFL == false)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTA2,3),10));
					stbDATA.append(padSTRING('L',setNumberFormat(L_dblDODQ2,3),10)
					+padSTRING('L',setNumberFormat(L_dblEXDQ2,3),10)
					+padSTRING('L',setNumberFormat(L_dblDEDQ2,3),10));
					if(P_flgISOFL == false)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblTOTB2,3),10));
					stbDATA.append(padSTRING('L',setNumberFormat(L_dblCCDQ2,3),10));
					
					if(P_flgISOFL == true)
					{
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblSLDQ2,3),10));
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQ2,3),10));
					}
					else
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQ2,3),10));
					if(P_flgISOFL == true)
					{
						//stbDATA.append(padSTRING('L',setNumberFormat(L_dblNSTQ2,3),10)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNUCQ2,3),10));
					}
					if(P_flgISOFL == false)
						stbDATA.append("\n");
					else if(P_flgISOFL == true)
						stbDATA.append(padSTRING('L',setNumberFormat(L_dblNTDQ2,3),10)+"\n");
					//stbDATA.append("\n"+strDOTLN);
					dosREPORT.writeBytes(stbDATA.toString());
				}
			}			
			dosREPORT.writeBytes("\n"+strDOTLN);
			
			setMSG("Report completed.. ",'N');			
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPPRN_pbst)) &&(M_rdbTEXT.isSelected()))
			{
				prnFMTCHR(dosREPORT,M_strCPI10);
				prnFMTCHR(dosREPORT,M_strEJT);				
			}			
			if(M_rdbHTML.isSelected())			
			    dosREPORT.writeBytes("<P CLASS = \"breakhere\"></fontsize></PRE></P></BODY></HTML>");    									
			dosREPORT.close();
			fosREPORT.close();
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getDATA");
		}
	}
	
	private void calSUBTOT()
	{
		try
		{
			dosREPORT.writeBytes("\n  "+padSTRING('R',"Sub Total",14)
			+padSTRING('L',setNumberFormat(L_dblNRCQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblNSRQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblDODQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblEXDQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblDEDQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblCCDQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblSLDQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblNSTQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblNUCQT1,3),10)
			+padSTRING('L',setNumberFormat(L_dblNTDQT1,3),10)+"\n");
			cl_dat.M_intLINNO_pbst++;
				
			L_dblNRCQ2 = L_dblNRCQ2 + L_dblNRCQT1;
			L_dblNSRQ2 = L_dblNSRQ2 + L_dblNSRQT1;
			L_dblDODQ2 = L_dblDODQ2 + L_dblDODQT1;
			L_dblEXDQ2 = L_dblEXDQ2 + L_dblEXDQT1;
			L_dblDEDQ2 = L_dblDEDQ2 + L_dblDEDQT1;
			L_dblCCDQ2 = L_dblCCDQ2 + L_dblCCDQT1;
			L_dblSLDQ2 = L_dblSLDQ2 + L_dblSLDQT1;
			L_dblNSTQ2 = L_dblNSTQ2 + L_dblNSTQT1;
			L_dblNUCQ2 = L_dblNUCQ2 + L_dblNUCQT1;
			L_dblNTDQ2 = L_dblNTDQ2 + L_dblNTDQT1;
									
			L_dblNRCQT1=0;L_dblNSRQT1=0;L_dblDODQT1=0;L_dblEXDQT1=0;
			L_dblDEDQT1=0;L_dblCCDQT1=0;L_dblSLDQT1=0;L_dblNSTQT1=0;
			L_dblNUCQT1=0;L_dblNTDQT1=0;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calSUBTOT");
		}
	}
	private void calTOTAL()
	{
		try
		{
			dosREPORT.writeBytes("\n"+padSTRING('R',"Total",16)
			+padSTRING('L',setNumberFormat(L_dblNRCQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblNSRQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblDODQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblEXDQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblDEDQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblCCDQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblSLDQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblNSTQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblNUCQ2,3),10)
			+padSTRING('L',setNumberFormat(L_dblNTDQ2,3),10)+"\n");
			cl_dat.M_intLINNO_pbst++;
			L_strOSTSFL = "";
									
			L_dblNRCQ3 = L_dblNRCQ3 + L_dblNRCQ2;
			L_dblNSRQ3 = L_dblNSRQ3 + L_dblNSRQ2;
			L_dblDODQ3 = L_dblDODQ3 + L_dblDODQ2;
			L_dblEXDQ3 = L_dblEXDQ3 + L_dblEXDQ2;
			L_dblDEDQ3 = L_dblDEDQ3 + L_dblDEDQ2;
			L_dblCCDQ3 = L_dblCCDQ3 + L_dblCCDQ2;
			L_dblSLDQ3 = L_dblSLDQ3 + L_dblSLDQ2;
			L_dblNSTQ3 = L_dblNSTQ3 + L_dblNSTQ2;
			L_dblNUCQ3 = L_dblNUCQ3 + L_dblNUCQ2;
			L_dblNTDQ3 = L_dblNTDQ3 + L_dblNTDQ2;
									
			L_dblNRCQ2=0;L_dblNSRQ2=0;L_dblDODQ2=0;L_dblEXDQ2=0;
			L_dblDEDQ2=0;L_dblCCDQ2=0;L_dblSLDQ2=0;L_dblNSTQ2=0;
			L_dblNUCQ2=0;L_dblNTDQ2=0;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"calSUBTOT");
		}
	}
	
		
	private void setRCTDTL(String LP_TBLNM,String LP_CONDN)
	{
		try
		{
			ResultSet L_RSLSET1;
			String L_WRKSTR = "0";
			double L_SUMSTR = 0;
			dblTORCT = 0;
			
			M_strSQLQRY = "Select rct_rcT1p,sum(rct_rctqt) L_SUMQT from "+LP_TBLNM+" where "+LP_CONDN;
			M_strSQLQRY += " group by rct_rcT1p order by rct_rcT1p";
			
			L_RSLSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_RSLSET1.next())
			{
				do
				{
					strRCT1P = L_RSLSET1.getString("rct_rcT1p").trim();
					strRCTQT = L_RSLSET1.getString("L_SUMQT").trim();
					if(strRCT1P.equals("10"))
					{
						TBL_DETLS.setValueAt(setNumberFormat(Double.valueOf(strRCTQT).doubleValue(),3),intCOUNT,TB2_RECPT);
						L_WRKSTR = strRCTQT;
					}
					else if(strRCT1P.equals("15"))
					{
						L_SUMSTR = Double.parseDouble(L_WRKSTR) + Double.parseDouble(strRCTQT);
						TBL_DETLS.setValueAt(setNumberFormat(L_SUMSTR,3),intCOUNT,TB2_RECPT);
						L_SUMSTR = 0;
					}
					else if(strRCT1P.equals("30"))
					{
						TBL_DETLS.setValueAt(setNumberFormat(Double.valueOf(strRCTQT).doubleValue(),3),intCOUNT,TB2_SLRCT);
					}
					dblTORCT += Double.parseDouble(strRCTQT);
				}
				while(L_RSLSET1.next());
			}
			else
			{
				TBL_DETLS.setValueAt("0.000",intCOUNT,TB2_RECPT);
				TBL_DETLS.setValueAt("0.000",intCOUNT,TB2_SLRCT);
				dblTORCT = 0;
			}
			L_RSLSET1.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setRCTDTL");
//			System.out.println(intCOUNT);
		}
	}
	
	private void setDSPDTL(String LP_TBLNM,String LP_CONDN)
	{
		try
		{
			ResultSet L_RSLSET1;
			
			dblTOISS = 0;
			
			M_strSQLQRY = "Select ist_isstp,ist_saltp,sum(ist_issqt) L_SUMQT from "+LP_TBLNM+" where "+LP_CONDN;
			M_strSQLQRY += " group by ist_isstp,ist_saltp order by ist_isstp,ist_saltp";
			
			L_RSLSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(L_RSLSET1.next())
			{
				do
				{
					strISSTP = L_RSLSET1.getString("ist_isstp").trim();
					strSALTP = L_RSLSET1.getString("ist_saltp").trim();
					strISSQT = L_RSLSET1.getString("L_SUMQT").trim();
					if(strISSTP.equals("10"))
					{
						if(strSALTP.equals("01"))
							TBL_DETLS.setValueAt(setNumberFormat(Double.valueOf(strISSQT).doubleValue(),3),intCOUNT,TB2_DODSP);
                                                else if(strSALTP.equals("12"))
							TBL_DETLS.setValueAt(setNumberFormat(Double.valueOf(strISSQT).doubleValue(),3),intCOUNT,TB2_EXDSP);
                                                else if(strSALTP.equals("03"))
							TBL_DETLS.setValueAt(setNumberFormat(Double.valueOf(strISSQT).doubleValue(),3),intCOUNT,TB2_DEDSP);
                                                else if(strSALTP.equals("16"))
							TBL_DETLS.setValueAt(setNumberFormat(Double.valueOf(strISSQT).doubleValue(),3),intCOUNT,TB2_CCDSP);
					}
					else if(strISSTP.equals("30"))
						TBL_DETLS.setValueAt(setNumberFormat(Double.valueOf(strISSQT).doubleValue(),3),intCOUNT,TB2_SRDSP);
					dblTOISS += Double.parseDouble(strISSQT);
				}
					while(L_RSLSET1.next());
			}
			else
			{
				TBL_DETLS.setValueAt("0.000",intCOUNT,TB2_DODSP);
				TBL_DETLS.setValueAt("0.000",intCOUNT,TB2_EXDSP);
				TBL_DETLS.setValueAt("0.000",intCOUNT,TB2_DEDSP);
				TBL_DETLS.setValueAt("0.000",intCOUNT,TB2_CCDSP);
				TBL_DETLS.setValueAt("0.000",intCOUNT,TB2_SRDSP);
				dblTOISS = 0;
			}
			L_RSLSET1.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"setDSPDTL");
		}
	}
	/**
	 * Method to calcualate the sum of the data from a Column & to display in the last Row of the Table.
	 * @param P_tblNAME to pass the Name of the JTable.
	 */
	private void getCOLTOT(JTable P_tblNAME)
	{
		try
		{			
			double L_COLTOT = 0;
			int L_intSELRO = 0;
			for(int i =1; i< P_tblNAME.getRowCount()-1;i++)
			{				
				if(P_tblNAME.getValueAt(i,TB1_GRADE).toString().length() == 0)
				{
					P_tblNAME.setValueAt(new Boolean(false),intCOUNT,TB1_CHKFL);
					P_tblNAME.setValueAt("Total",i,TB1_GRADE);
					L_intSELRO = i;
					break;
				}
			}			
			for(int i = 2;i <= (P_tblNAME.getColumnCount()-1);i++)
			{				
				for(int j = 0;j <= L_intSELRO -1 ;j++)//P_tblNAME.getRowCount()-1
				{
					if(!P_tblNAME.getValueAt(j,i).toString().equals(""))
						L_COLTOT += Double.parseDouble(P_tblNAME.getValueAt(j,i).toString().trim());					
				}				
				P_tblNAME.setValueAt(setNumberFormat(L_COLTOT,3),L_intSELRO,i);
				L_COLTOT = 0;
			}
			for(int i = 2;i <= (P_tblNAME.getColumnCount()-1);i++)
			{				
				for(int j = 0;j <= L_intSELRO -1 ;j++)//P_tblNAME.
					if(P_tblNAME.getValueAt(j,i).toString().equals(""))
						P_tblNAME.setValueAt("0.000",j,i);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCOLTOT");
		}
	}
	/*public void mouseClicked(MouseEvent L_ME)
	{
		try
		{
			strLCKDT = txtLCKDT.getText().toString().trim();
			if(L_ME.getSource().equals(TBL_SMMRY))
			{
				if(TBL_SMMRY.getSelectedColumn() == TB1_GRADE)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					strSBPRD = TBL_SMMRY.getValueAt(TBL_SMMRY.getSelectedRow(),TB1_GRADE).toString().trim();
					setMSG("Data Fetching for "+strSBPRD+" in Progress",'N');
					strCODCD = getCODCD(" SUBSTRING(CMT_CODCD,1,4) ","SG",strSBPRD);
					//fg_qrgrd ofg_qrgrd = new fg_qrgrd(strSBPRD,strCODCD,strLCKDT,strQRYDT,intSELIDX);
//					ofg_qrgrd.show();
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
			else if(L_ME.getSource().equals(TBL_DETLS))
			{
				if(TBL_DETLS.getSelectedColumn() == TB2_GRADE)
				{
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					strSBPRD = TBL_DETLS.getValueAt(TBL_DETLS.getSelectedRow(),TB2_GRADE).toString().trim();
					setMSG("Data Fetching for "+strSBPRD+" in Progress",'N');
					strCODCD = getCODCD(" SUBSTRING(CMT_CODCD,1,4) ","SG",strSBPRD);
					//fg_qrgrd ofg_qrgrd = new fg_qrgrd(strSBPRD,strCODCD,strLCKDT,strQRYDT,intSELIDX);
					//ofg_qrgrd.show();
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"mouseClicked");
		}
	}*/
	
/*	private String getCODCD(String LP_FLDNM,String LP_CCSVL,String LP_PRDCD)
	{
		String L_CODCD = "";
		try
		{
			M_strSQLQRY = "Select "+LP_FLDNM+" from co_cdtrn where CMT_CGMTP='MST' AND";
			M_strSQLQRY += " CMT_CGSTP='COXXPGR' AND CMT_CCSVL='"+LP_CCSVL+"' AND CMT_CODDS='"+strSBPRD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET.next())
				L_CODCD = M_rstRSSET.getString(1);
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getCODCD");
		}
		return L_CODCD;
	}*/
}