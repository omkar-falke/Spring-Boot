/*
System Name   : Material Management System
Program Name  : Excise Approval Entry
Program Desc. : Entry screen for Excise to approve the Gate Out vehicles (for Despatch).
Author        : Mr S.R.Mehesare
Date          : 25.06.2005
Version       : MMS v2.0.0
Modificaitons 
Modified By    :
Modified Date  :
Modified det.  :
Version        :
*/
import java.sql.*;
//import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Cursor;
import javax.swing.undo.*;
import java.awt.Component;
import java.awt.Dimension;
import java.io.FileOutputStream; 
import java.io.DataOutputStream; 
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.*;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.DefaultCellEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*; 
import javax.print.attribute.*;import javax.print.*;import javax.print.event.*;import javax.print.attribute.standard.*;


/**
 <P><PRE style = font-size : 10 pt >
<b>System Name :</b> Material Management System.
 
<b>Program Name :</b> Excise Approval Entry

<b>Purpose :</b> This Entry  Screen is used to approve the various vehicles used for 
various types of transportations
			
List of tables used :
Table Name      Primary key                      Operation done
                                        Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
MM_WBTRN        WB_DOCTP,WB_DOCNO                 #       #     
CO_CDTRN        CMT_CGMTP,CMT_CGSTP,CMT_CODCD             #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		Table name		Type/Size       Description
----------------------------------------------------------------------------------------------------------
cmbGINTP    WB_DOCTP        MM_WBTRN        VARCHAR(2)      Gate In Type
TBL_GINNO   WB_GINNO        MM_WBTRN        VARCHAR(8)      Lorry No.
TBL_GINDT   WB_GINDT        MM_WBTRN        Timestmp        Lorry Description
TBL_GINBY   WB_GINBY        MM_WBTRN        VARCHAAR(3)     Trip Count
TBL_CHLQT   WB_CHLQT        MM_WBTRN        Decimal(12,3)   Default Trip Count
TBL_MATDS   WB_MATDS        MM_WBTRN        VARCHAR(45)     Material Description
TBL_LRYNO   WB_LRYNO        MM_WBTRN        VARCHAR(15)     Lorry Number
TBL_TPRDS   WB_TPRDS        MM_WBTRN        VARCHAR(40)     Transporter Description
----------------------------------------------------------------------------------------------------------

Validations :
	Gate In Type Must be valid.
*/

class mm_teexa extends cl_pbase
{
	private Thread thrOBJRP;

									/** JCommboBox to display & to select the Gate-In Type.*/
	private JComboBox cmbGINTP;		/** JCommboBox to display & to record Vehicle Detention detail*/
	private JCheckBox chkVHDTN;		/** JCommboBox to display & to print loading sequence*/
	private JCheckBox chkUNLSQ;		/** JCheckBox to fetch the vehicles list for which Loading Invoice has been prepated.*/
	private JCheckBox chkINVIC;		/** JButton for printing unloading sequence*/
	private JButton btnPRINT;		/** JTable to show the Data in Tabular Form.*/
	private cl_JTable tblITMDT ;	/** JTable to show the Vehicle Detention*/
	private cl_JTable tblVHDTBL ;	/** JTable to show the Unloading sequence data.*/
	//private cl_JTable tblUNLSQ ;	/** Integer variable for serial Number.*/
	private int intSRLNO;			/** String final variable for Party Code.*/	
    private String strPRTCD;		/** Final Integer for Check Flag Column.*/		
	final int TBL_CHKFL = 0;		/** Final Integer for Serial Number Column.*/
	final int TBL_SRLNO = 1;		/** Final Integer for Gate in Number Column.*/
	final int TBL_GINNO = 2;		/** Final Integer for Trip Count Column.*/
	final int TBL_GINDT = 3;		/** Final Integer for Gate In By Column.*/
	final int TBL_GINBY = 4;		/** Final Integer for Material Description Column.*/
	final int TBL_MATDS = 5;		/** Final Integer for Quantity Column.*/
	final int TBL_CHLQT = 6;		/** Final Integer for Lorry Number Column.*/
	final int TBL_LRYNO = 7;		/** Final Integer for Transporter Code Column.*/
	final int TBL_TPRDS = 8;


	int intTB6_CHKFL = 0;
	int intTB6_SRLNO = 1;
    int intTB6_DETCD = 2;
    int intTB6_DETDS = 3;
    int intTB6_STRTM = 4;
    int intTB6_ENDTM = 5;
    int intTB6_EFFTM = 6;
	
	CallableStatement cstDSHINV;	    // Stored procedure for Delivery Schedule record updation
	CallableStatement cstPTTRN_INV;	    // Stored procedure for creating Credit Note (Bkg.Discount & Distr.Comm.)Transactions
	CallableStatement cstPLTRN_INV;		// Stored procedure for creating Sales Invoice records in Party Ledger
	CallableStatement cstPLTRN_PTT;		// Stored procedure for creating Credit Note (Bkg.Discount & Distr.Comm.) records in Party Ledger
	CallableStatement cstPATRN_PTT;		// Stored procedure for auto adjustment of Credit Note (Bkg.Discount & Distr.Comm.) records.
	CallableStatement cstDSPQT_RES;		// Stored procedure for updating Despatch Qty. against reservation
	//CallableStatement cstPATRN_ADV;		// Stored procedure for auto adjustment of Advance / LC payment receipts

									/** Final Integer for Report Line Number*/
    private int intLINENO = 0;
									/** Jpanel for displaying loading sequence*/
	private JPanel pnlLDGSQ;
	private JPanel pnlVHDTN;
	
								/** String variable for Gate In Type.*/
	private String strGINTP;	/** String variable for Weigh Bridge Number.*/
	private String strWBSNO="1";/** String variable for Gate In Number.*/
	private String strGINNO;
    private JLabel lblVHDMSG = new JLabel();
	private FileOutputStream O_FOUT;
    private DataOutputStream O_DOUT;
	private Process prcREPORT;
    private JOptionPane oppOPTNPN;

	private String strRESFIN = "";//cl_dat.M_REPSTR;
    private String strRESSTR = cl_dat.M_strREPSTR_pbst+"mr_unlsq.doc"; 
	private static String[] arrDAYS = {"31","28","31","30","31","30","31","31","30","31","30","31"};	
	
	private String strWHRSTR;
	private boolean flgCHK_EXIST;
	private JTextField txtDETCD;
	
	JComboBox L_cmbTEMP ;
	mm_teexa()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Gate-In Type"),3,4,1,1,this,'L');
			add(cmbGINTP = new JComboBox(),3,5,1,1.5,this,'L');
			add(chkINVIC = new JCheckBox("Only Invoice Prepared",false),3,2,1,2,this,'L');
			add(chkUNLSQ = new JCheckBox("Only Unloading Seq.",false),2,7,1,2,this,'L');
			add(btnPRINT = new JButton("Print U/Seq"),3,7,1,1,this,'L');
			add(chkVHDTN = new JCheckBox("Enter Vehicle Detention",false),16,7,1,2,this,'L');
			M_strSQLQRY = "Select CMT_CGMTP,CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP + CMT_CGSTP ='SYSMMXXWBT'";
			M_strSQLQRY += " and CMT_STSFL <> 'X' order by CMT_CODCD";			
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)	
			{
				cmbGINTP.addItem("Select Gate-In Type");
				while(M_rstRSSET.next())
				{
					if(M_rstRSSET.getString("CMT_CGSTP").equals("MMXXWBT"))					
						cmbGINTP.addItem(M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS"));				  							
				}
				M_rstRSSET.close();
			}			
			String[] L_LOCHD = {"Out","SrNo","Gate-In No","Gate-In DateTime","GateInBy","Material","Quantity","Lorry No.","Transporter"};
			int[] L_COLSZ = {40,40,80,100,55,75,90,100,150};
			tblITMDT = crtTBLPNL1(this,L_LOCHD,800,5,1,8.4,7.8,L_COLSZ,new int[]{0});
			tblITMDT.addMouseListener(this);	
			setENBL(false);			
			thrOBJRP=new Thread(this);thrOBJRP.start();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}
	}
	void setENBL(boolean P_flgSTAT)
	{		
		super.setENBL(P_flgSTAT);				
		tblITMDT.cmpEDITR[TBL_SRLNO].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_GINNO].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_GINDT].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_GINBY].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_MATDS].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_CHLQT].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_LRYNO].setEnabled(false);
		tblITMDT.cmpEDITR[TBL_TPRDS].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() == 0)		
		{
			chkINVIC.setEnabled(false);
			chkUNLSQ.setEnabled(false);
			chkVHDTN.setEnabled(false);
			btnPRINT.setEnabled(false);
			cmbGINTP.setEnabled(false);
		}
		else
		{
			chkINVIC.setEnabled(true);
			chkUNLSQ.setEnabled(true);
			chkVHDTN.setEnabled(true);
			chkVHDTN.setVisible(false);
			btnPRINT.setEnabled(true);
			cmbGINTP.setEnabled(true);
		}
		tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		else
			tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(false);
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		try
		{									
			if (M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{								
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex() != 0)
				{					
					cmbGINTP.requestFocus();					
					setMSG("Please Select Gate In Type..",'N');										
					setENBL(false);					
				}
				else
				{					
					cl_dat.M_cmbOPTN_pbst.requestFocus();
					setMSG("Addition & Deletation is not allowed here..",'N');
					setENBL(false);
				}				
			}
			if (M_objSOURC == cmbGINTP)
			{								
				String L_strTEMP ="";				
				if(!cmbGINTP.getSelectedItem().equals("Select Gate-In Type"))
				{	
					if(tblITMDT.isEditing())
						tblITMDT.getCellEditor().stopCellEditing();
					tblITMDT.setRowSelectionInterval(0,0);
					tblITMDT.setColumnSelectionInterval(0,0);
					setCursor(cl_dat.M_curWTSTS_pbst);	
					tblITMDT.clrTABLE();
					strGINTP = cmbGINTP.getSelectedItem().toString().substring(0,2);
					chkVHDTN.setVisible(false);
					if(strGINTP.equals("03"))
						{chkVHDTN.setVisible(true); tblITMDT.cmpEDITR[TBL_CHKFL].setEnabled(true);}

					M_strSQLQRY = "Select WB_DOCTP,WB_DOCNO,WB_GINBY,WB_GOTBY,";
					M_strSQLQRY += "WB_CHLNO,WB_CHLDT,WB_CHLQT,WB_GINDT,WB_GOTDT,";				
					M_strSQLQRY += "WB_ORDRF,WB_ORDDT,WB_PRTCD,WB_PRTDS,";                    
					M_strSQLQRY += "WB_MATTP,WB_MATCD,WB_MATDS,WB_LRYNO,WB_TPRCD,";
					M_strSQLQRY += "WB_TPRDS,WB_REMGT,WB_REPTM,WB_STSFL,WB_EXAPR";
					M_strSQLQRY += " from MM_WBTRN where ";
					M_strSQLQRY += " WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
				//	M_strSQLQRY += " AND ifnull(WB_STSFL,'') <> '9'";
					
					if(chkINVIC.isSelected())
						M_strSQLQRY += " AND WB_CHLQT > 0";
					if(chkUNLSQ.isSelected())
						M_strSQLQRY += " AND WB_DOCNO in (select IVT_GINNO from MR_IVTRN where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IVT_UNLSQ>1 and IVT_STSFL in ('L','D')) ";
					//M_strSQLQRY += " and WB_DOCNO = '" + P_strGINNO + "'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst) && strGINTP.equals("03"))
					{
						M_calLOCAL.setTime(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()));
						M_calLOCAL.add(Calendar.DATE,-8);
						String L_strSTRDT=M_fmtLCDAT.format(M_calLOCAL.getTime());
						M_strSQLQRY += " and CONVERT(varchar,WB_GINDT,103) between '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(L_strSTRDT))+"' and '"+M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))+"'";
					}
					else
					{
						M_strSQLQRY += " AND isnull(WB_STSFL,'') not in('X','9')";
						M_strSQLQRY += " AND isnull(WB_EXAPR,'') = ''";
						M_strSQLQRY += " AND WB_SRLNO = '" + strWBSNO+"'";
					}
					M_strSQLQRY += " order by WB_DOCNO desc";										
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET !=null)
					{
						int i = 0;
						while(M_rstRSSET.next())
						{
							tblITMDT.setValueAt(new Boolean(false),i,TBL_CHKFL);
							
							L_strTEMP = "";
							tblITMDT.setValueAt(String.valueOf(i+1).toString(),i,TBL_SRLNO);							
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("WB_DOCNO"),"");
							tblITMDT.setValueAt(L_strTEMP,i,TBL_GINNO);
							
							L_strTEMP = "";						
							java.sql.Timestamp L_tmsTEMP = M_rstRSSET.getTimestamp("WB_GINDT");
							if(L_tmsTEMP !=null)
								L_strTEMP = M_fmtLCDTM.format(L_tmsTEMP);
							tblITMDT.setValueAt(L_strTEMP,i,TBL_GINDT);
						
							L_strTEMP = "";
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("WB_GINBY"),"");
							tblITMDT.setValueAt(L_strTEMP,i,TBL_GINBY);
							
							L_strTEMP = "";
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0");
							tblITMDT.setValueAt(L_strTEMP,i,TBL_CHLQT);
							
							L_strTEMP = "";
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("WB_MATDS"),"");
							tblITMDT.setValueAt(L_strTEMP,i,TBL_MATDS);
							
							L_strTEMP = "";
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("WB_LRYNO"),"");
							tblITMDT.setValueAt(L_strTEMP,i,TBL_LRYNO);
							
							L_strTEMP = "";
							L_strTEMP = nvlSTRVL(M_rstRSSET.getString("WB_TPRDS"),"");
							tblITMDT.setValueAt(L_strTEMP,i,TBL_TPRDS);													
							i++;
						}
						M_rstRSSET.close();						
						setMSG("For Excise Approval for Gate Out, Please Check Mark the Gate In Number..",'N');
					}
				}
				setCursor(cl_dat.M_curDFSTS_pbst);	
			}
			else if(M_objSOURC==chkVHDTN)
			{
				for (int i=0;i<tblITMDT.getRowCount();i++)
				{
					if(tblITMDT.getValueAt(i,TBL_GINNO).toString().length()<8)
						break;
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						getVHDTN(tblITMDT.getValueAt(i,TBL_GINNO).toString());
						setCursor(cl_dat.M_curDFSTS_pbst);	
					}
				}
				//System.out.println("001");
				//mr_rpdor objRPDOR = new mr_rpdor(1, M_strSBSLS);
			}
			else if(M_objSOURC==btnPRINT)
			{
				if(thrOBJRP!=null)
					thrOBJRP.join();
				for (int i=0;i<tblITMDT.getRowCount();i++)
				{
					if(tblITMDT.getValueAt(i,TBL_GINNO).toString().length()<8)
						break;
					if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						getLDGSQ(tblITMDT.getValueAt(i,TBL_GINNO).toString());
						setCursor(cl_dat.M_curDFSTS_pbst);	
					}
				}
				//System.out.println("001");
				//mr_rpdor objRPDOR = new mr_rpdor(1, M_strSBSLS);
			}
			//if (M_objSOURC == chkLDGSQ)
			//{
			//	for (int i=0;i<tblITMDT.getRowCount();i++)
			//	{
			//		if(tblITMDT.getValueAt(i,TBL_GINNO).toString().length()<8)
			//			break;
			//		if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
			//		{
			//			getLDGSQ(tblITMDT.getValueAt(i,TBL_GINNO).toString());
			//			setCursor(cl_dat.M_curDFSTS_pbst);	
			//		}
			//	}
			//}
			/*if (M_objSOURC == chkINVIC)
			{
				if(tblITMDT !=null)
					tblITMDT.clrTABLE();
			}	*/		
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed"+M_objSOURC);
			setCursor(cl_dat.M_curDFSTS_pbst);	
		}
	}
	void exeSAVE()
	{
		try
		{							
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			for(int i=0;i<tblITMDT.getRowCount();i++)
			{					
				if(tblITMDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{																	
					if(Double.valueOf(tblITMDT.getValueAt(i,TBL_CHLQT).toString()).doubleValue() == 0.000)
					{
						int L_ACTION = JOptionPane.showConfirmDialog(this,"Invoice is not yet prepared for " +tblITMDT.getValueAt(i,TBL_GINNO).toString() + ".Do you want to Approve for Gate Out?","Invoice not prepared",JOptionPane.YES_NO_OPTION); 
						if(L_ACTION != 0)//if No Clicked...
						{
							setCursor(cl_dat.M_curDFSTS_pbst);
							continue ;
						}
					}
					strGINNO = tblITMDT.getValueAt(i,TBL_GINNO).toString();
					cl_dat.M_flgLCUPD_pbst = true;				
					M_strSQLQRY = "UPDATE MM_WBTRN SET"
						+" WB_EXAPR ='"+cl_dat.M_strUSRCD_pbst+"',";
					M_strSQLQRY += getUSGDTL("WB",'U',null); 
					M_strSQLQRY += " WHERE WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP ='"+strGINTP+"'";
					M_strSQLQRY += " AND WB_DOCNO = '"+strGINNO +"'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					exeCNTRN(strGINTP,strGINNO);

				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{				
				setMSG("Excise approval has been given for gate out..",'N');
				clrCOMP();				
			}
			else
				setMSG("Error in approving..",'N');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}


	/** Method for creating Credit Note Transactions against
	 * the Gate-in Number (This is applicable for Sales Invoice Gate-out approval)
	 */
	public void exeCNTRN(String LP_DOCTP,String LP_DOCNO)
	{
		
		try
		{
			if(!LP_DOCTP.equals("03"))
				return;
	
			M_strSQLQRY = "select distinct ivt_mkttp, ivt_invno from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ginno = '"+LP_DOCNO+"' and isnull(ivt_invqt,0)>0 and ivt_stsfl<>'X' and ivt_mkttp in ('01','04','05') and ivt_saltp not in ('04','05','16','21') order by ivt_mkttp, ivt_invno";
			System.out.println(M_strSQLQRY);
			ResultSet L_rstRSSET = null;
				
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			String L_strMKTTP, L_strINVNO;
			if(L_rstRSSET!=null && L_rstRSSET.next())
			{
				while(true)
				{
					
					
					//exeCNTRN(strGINTP, strGINNO);
					L_strMKTTP = getRSTVAL(L_rstRSSET,"IVT_MKTTP","C");
					L_strINVNO = getRSTVAL(L_rstRSSET,"IVT_INVNO","C");
					//System.out.println(L_strMKTTP+"/"+L_strINVNO);
					cstDSHINV = cl_dat.M_conSPDBA_pbst.prepareCall("call setDSHINV(?,?)");
					cstDSHINV.setString(1,cl_dat.M_strCMPCD_pbst);
					cstDSHINV.setString(2,L_strINVNO);
					cstDSHINV.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();

					
					cstPTTRN_INV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPTTRN_INV(?,?,?)");
					cstPTTRN_INV.setString(1,cl_dat.M_strCMPCD_pbst);
					cstPTTRN_INV.setString(2,L_strMKTTP);
					cstPTTRN_INV.setString(3,L_strINVNO);
					cstPTTRN_INV.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();

					cstPLTRN_INV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_INV(?,?,?)");
					cstPLTRN_INV.setString(1,cl_dat.M_strCMPCD_pbst);
					cstPLTRN_INV.setString(2,L_strMKTTP);
					cstPLTRN_INV.setString(3,L_strINVNO);
					cstPLTRN_INV.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();

					cstPLTRN_PTT = cl_dat.M_conSPDBA_pbst.prepareCall("call updPLTRN_PTT(?,?,?)");
					cstPLTRN_PTT.setString(1,cl_dat.M_strCMPCD_pbst);
					cstPLTRN_PTT.setString(2,L_strMKTTP);
					cstPLTRN_PTT.setString(3,L_strINVNO);
					cstPLTRN_PTT.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();

					cstPATRN_PTT = cl_dat.M_conSPDBA_pbst.prepareCall("call updPATRN_PTT(?,?,?)");
					cstPATRN_PTT.setString(1,cl_dat.M_strCMPCD_pbst);
					cstPATRN_PTT.setString(2,L_strMKTTP);
					cstPATRN_PTT.setString(3,L_strINVNO);
					cstPATRN_PTT.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();
					
					cstDSPQT_RES = cl_dat.M_conSPDBA_pbst.prepareCall("call setDSPQT_RES(?,?,?)");
					cstDSPQT_RES.setString(1,cl_dat.M_strCMPCD_pbst);
					cstDSPQT_RES.setString(2,L_strMKTTP);
					cstDSPQT_RES.setString(3,L_strINVNO);
					cstDSPQT_RES.executeUpdate();
					cl_dat.M_conSPDBA_pbst.commit();

					//cstPATRN_ADV = cl_dat.M_conSPDBA_pbst.prepareCall("call updPATRN_ADV(?,?,?)");
					//cstPATRN_ADV.setString(1,"01");
					//cstPATRN_ADV.setString(2,L_strMKTTP);
					//cstPATRN_ADV.setString(3,L_strINVNO);
					//cstPATRN_ADV.executeUpdate();
					//cl_dat.M_conSPDBA_pbst.commit();
					
					if(!L_rstRSSET.next())
						break;
				}
				L_rstRSSET.close();
			}
			M_strSQLQRY = "select distinct ivt_mkttp, ivt_invno from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ginno = '"+LP_DOCNO+"' and isnull(ivt_invqt,0)>0 and ivt_stsfl<>'X' and ivt_mkttp in ('01','04','05') and ivt_saltp in ('04') order by ivt_mkttp, ivt_invno";
				
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET!=null && L_rstRSSET.next())
			{
				while(true)
				{
						cl_dat.M_flgLCUPD_pbst = true;	
						M_strSQLQRY = "update MR_IVTRN set IVT_INVNO = IVT_INVNO where IVT_CMPCD = '"+cl_dat.M_strCMPCD_pbst+"' and IVT_MKTTP = '"+getRSTVAL(L_rstRSSET,"IVT_MKTTP","C")+"' and IVT_INVNO = '"+getRSTVAL(L_rstRSSET,"IVT_INVNO","C")+"'";
						System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");					
						cl_dat.exeDBCMT("exeCNTRN");
					if(!L_rstRSSET.next())
						break;
				}
				L_rstRSSET.close();
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeCNTRN");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	
	
	/**
	 */
	public JComboBox getPRNLS()
	{
		
		try
		{
		    String [] L_staTEMP=new String[cl_dat.M_vtrPRNTR_pbst.size()];
			for(int i=0;i<cl_dat.M_vtrPRNTR_pbst.size();i++)
			{
				L_staTEMP[i]=cl_dat.M_vtrPRNTR_pbst.elementAt(i).toString();
			}
			L_cmbTEMP=new JComboBox(L_staTEMP);
			cl_dat.M_intPRIND_pbst = JOptionPane.showConfirmDialog( this,L_cmbTEMP,"Select Printer",JOptionPane.OK_CANCEL_OPTION);
			//M_cmbDESTN=L_cmbTEMP;
			return L_cmbTEMP;
		}
		catch(Exception L_E)
		{
			System.out.println(L_E.toString());
		}
		return null;
	}

	/**
	 */
	protected void doPRINT(String LP_strFILNM,int LP_intPRNNO)
	{
		try
		{
			if(LP_intPRNNO>L_cmbTEMP.getItemCount()-1)
				throw( new Exception("Illegal Printer Index"));
			DocPrintJob job=cl_dat.pservices[LP_intPRNNO].createPrintJob();
			//PrintJobWatcher pjDone = new PrintJobWatcher(job);
			InputStream is = new BufferedInputStream(new FileInputStream(LP_strFILNM));
			DocAttributeSet daset = new HashDocAttributeSet();
			Doc d=new SimpleDoc(is,cl_dat.flavor,daset);
			InputStream inputStream = d.getStreamForBytes();
			job.print(d,null);
			//pjDone.waitForDone();
			inputStream.close();
		}catch(Exception e)
		{setMSG(e,"doPRINT");}
	}


	/**
	 */
	private void getLDGSQ(String LP_GINNO)
	{
		try
			{//DISPLAY WINDOW FOR SHOWING CURRENT LOADING STATUS
				//if(LP_GINNO.length()!=8)
				//	return;
			
				setCursor(cl_dat.M_curWTSTS_pbst);	
				M_strSQLQRY = "select count(distinct ivt_ladno) ivt_ladct from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ginno = '"+LP_GINNO+"' and ivt_stsfl <>'X'";
				//System.out.println(M_strSQLQRY);
				ResultSet L_rstRSSET = null;
				
				L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(!L_rstRSSET.next() || L_rstRSSET==null)
					return;
				if(Integer.parseInt(getRSTVAL(L_rstRSSET,"IVT_LADCT","N"))<2)
				{
					oppOPTNPN.showMessageDialog(this,"Single Unloading","Message",JOptionPane.INFORMATION_MESSAGE);
					//return;
				}
				L_rstRSSET.close();

				//LM_ROWCNT = 20;
				
				
				O_FOUT = crtFILE(strRESSTR);
				O_DOUT = crtDTOUTSTR(O_FOUT);	
				prnUNLSQ(LP_GINNO);
				prnUNLSQ(LP_GINNO);
				prnFMTCHR(O_DOUT,M_strEJT);
				O_DOUT.close();
				O_FOUT.close();
				setCursor(cl_dat.M_curDFSTS_pbst);	
				//System.out.println("befor getprnls");
				JComboBox L_cmbLOCAL = getPRNLS();
				//System.out.println("befor do PRINT");
				doPRINT(cl_dat.M_strREPSTR_pbst+"mr_unlsq.doc",L_cmbLOCAL.getSelectedIndex());
				//System.out.println("after do print");
				//doPRINT(strRESSTR);
				
			}

			catch (Exception L_EX)
			{setMSG("Error in getLDGSQ : "+L_EX,'E');}
	}
	
	/**
	 */
	private void prnUNLSQ(String LP_GINNO)
	{
		ResultSet L_rstRSSET = null;
		try
		{
				M_strSQLQRY = "select ivt_ladno,isnull(ivt_invno,'-') ivt_invno,ivt_lryno,a.pt_prtnm ivt_CNSNM,b.pt_prtnm IVT_TRPNM,ivt_prdds,ivt_ladqt,cmt_codds ivt_dstds,IVT_UNLSQ,ivt_stsfl from mr_ivtrn,mr_inmst,co_ptmst a,co_ptmst b,co_cdtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ginno = '"+LP_GINNO+"' and in_mkttp=ivt_mkttp and in_indno=ivt_indno and in_cmpcd=ivt_cmpcd and cmt_cgmtp = 'SYS' and cmt_cgstp='COXXDST' and cmt_codcd=IN_DSTCD and a.pt_prttp='C' and ivt_cnscd=a.pt_prtcd and b.pt_prttp='T' and ivt_trpcd=b.pt_prtcd and upper(ivt_stsfl) <> 'X' order by ivt_unlsq desc";
				//System.out.println(M_strSQLQRY);
				L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(!L_rstRSSET.next() || L_rstRSSET==null)
					return;
				prnUNLSQ(L_rstRSSET);
				L_rstRSSET.close();
		}

		catch (Exception L_EX)
		{setMSG("Error in prnUNLSQ: "+L_EX,'E');}
	}
	
	
	/**
	 */
	private void prnUNLSQ(ResultSet LP_RSSET)
	{
		try
		{
			String L_UNLSQ_OLD = "xx";
			int L_UNLSQ_PRT = 0;
			intLINENO = 0;
			String L_LFTMRG = "     ";
			String L_strTRPNM = getRSTVAL(LP_RSSET,"IVT_TRPNM","C");
			O_DOUT.writeBytes("\n\n\n"+L_LFTMRG+padSTRING('R'," ",20)+padSTRING('C',"SUPREME PETROCHEM LIMITED.",40)+padSTRING('L',"Date : "+cl_dat.M_txtCLKDT_pbst.getText(),20)+"\n\n"); intLINENO +=5;
			O_DOUT.writeBytes(L_LFTMRG+padSTRING('C',"MATERIAL UNLOADING SEQUENCE for Vehicle No.: "+getRSTVAL(LP_RSSET,"IVT_LRYNO","C"),80)+"\n \n"); intLINENO +=2;
			O_DOUT.writeBytes(L_LFTMRG+padSTRING('R',"------",6)+" "+padSTRING('R',"-----------------------------",30)+" "+padSTRING('R',"---------",10)+" "+padSTRING('R',"---------------",15)+" "+padSTRING('L',"---------",10)+" "+padSTRING('R',"------",10)+"\n"); intLINENO +=1;
			O_DOUT.writeBytes(L_LFTMRG+padSTRING('R',"U/Seq.",6)+" "+padSTRING('R',"Consignee",30)                    +" "+padSTRING('R',"Inv.No.",10)+" "+padSTRING('R',"Grade",15)+" "+padSTRING('L',"Qty",10)+" "+padSTRING('R',"Destn.",10)+"\n"); intLINENO +=1;
			O_DOUT.writeBytes(L_LFTMRG+padSTRING('R',"------",6)+" "+padSTRING('R',"-----------------------------",30)+" "+padSTRING('R',"---------",10)+" "+padSTRING('R',"---------------",15)+" "+padSTRING('L',"---------",10)+" "+padSTRING('R',"------",10)+"\n"); intLINENO +=1;
			while(true)
			{
				String L_strINVNO = getRSTVAL(LP_RSSET,"IVT_INVNO","C");
				if(!getRSTVAL(LP_RSSET,"IVT_UNLSQ","C").equals(L_UNLSQ_OLD))
					{L_UNLSQ_OLD = getRSTVAL(LP_RSSET,"IVT_UNLSQ","C"); O_DOUT.writeBytes("\n"); L_UNLSQ_PRT+= 1; intLINENO +=1;}
				O_DOUT.writeBytes(L_LFTMRG+padSTRING('R',String.valueOf(L_UNLSQ_PRT),6)+" "+padSTRING('R',getRSTVAL(LP_RSSET,"IVT_CNSNM","C"),30)+" "+padSTRING('R',(L_strINVNO.trim().length()==8 ? L_strINVNO.substring(2,8) : L_strINVNO),10)+" "+padSTRING('R',getRSTVAL(LP_RSSET,"IVT_PRDDS","C"),15)+" "+padSTRING('L',getRSTVAL(LP_RSSET,"IVT_LADQT","N"),10)+" "+padSTRING('R',getRSTVAL(LP_RSSET,"IVT_DSTDS","C"),10)+"\n"); intLINENO +=1;
				
				if(!LP_RSSET.next())
				{
					break;
				}
			}
			O_DOUT.writeBytes(L_LFTMRG+padSTRING('R',"------",6)+" "+padSTRING('R',"-----------------------------",30)+" "+padSTRING('R',"---------",10)+" "+padSTRING('R',"---------------",15)+" "+padSTRING('L',"---------",10)+" "+padSTRING('R',"------",10)+"\n"); intLINENO +=1;
			O_DOUT.writeBytes("\n\n\n\n"+L_LFTMRG+padSTRING('C',"(Prepared By)",40)+" "+padSTRING('C',"(Sign. of Transporter)",40)+"\n"); intLINENO +=5;
			O_DOUT.writeBytes(""+L_LFTMRG+padSTRING('C'," ",40)+" "+padSTRING('C',L_strTRPNM,40)+"\n"); intLINENO +=5;
			
			for(int k=intLINENO;k<34;k++)
				O_DOUT.writeBytes("\n");
			O_DOUT.writeBytes(L_LFTMRG+"................................................................................"+"\n"); intLINENO +=1;
			intLINENO = 0;
		}
		catch(Exception L_EX)
		{
			System.out.println("Error in prnUNLSQ(ResultSet) "+L_EX);
		}
	}
	
	

/** Method for returning values from Result Set
 * <br> with respective verifications against various data types
 * @param	LP_RSLSET		Result set name
 * @param       LP_FLDNM                Name of the field for which data is to be extracted
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
	{setMSG(L_EX,"getRSTVAL");}
return " ";
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


	
	
	/**
	 * Displaying Vehicle detntion detail
	 *
	 */
	private void getVHDTN(String LP_GINNO)
	{
		ResultSet L_rstRSSET = null;
		try
			{//DISPLAY WINDOW FOR SHOWING CURRENT LOADING STATUS
				//System.out.println("LP_GINNO : "+LP_GINNO);
				//if(chkVHDTN.isSelected()==false)
				//	return;
				//if(LP_GINNO.length()!=8)
				//	return;
			
			this.setCursor(cl_dat.M_curWTSTS_pbst);

				//LM_ROWCNT = 20;
				if(pnlVHDTN==null)
				{
					pnlVHDTN=new JPanel(null);
					//cl_dat.M_CHKTBL = false;
					String[] L_VHDHD = {"Sel","Srl.No.","Det.Code","Description","Time From","Time To","Eff.Time"};
					int[] L_COLSZ = {20,40,40,300,100,100,80};
					tblVHDTBL = crtTBLPNL1(pnlVHDTN,L_VHDHD,50,1,1,6,8,L_COLSZ,new int[]{0});

					tblVHDTBL.setInputVerifier(new TBLINPVF());

					tblVHDTBL.setRowSelectionInterval(0,0);
					tblVHDTBL.setColumnSelectionInterval(0,0);
					//tblVHDTBL.cmpEDITR[intTB6_DETCD].addFocusListener(this);
					tblVHDTBL.setCellEditor(intTB6_DETCD,txtDETCD=new JTextField());
					txtDETCD.addFocusListener(this);
					txtDETCD.addKeyListener(this);
					((JTextField) tblVHDTBL.cmpEDITR[intTB6_SRLNO]).addFocusListener(this);
					((JTextField) tblVHDTBL.cmpEDITR[intTB6_SRLNO]).addKeyListener(this);

					add(lblVHDMSG,8,4,1,4,pnlVHDTN,'L');
				}
				clrVHDTBL();
				M_strSQLQRY = "select ivt_mkttp,ivt_lryno from mr_ivtrn where IVT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ivt_ginno =  '"+LP_GINNO+"'";
				L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(!L_rstRSSET.next() || L_rstRSSET==null)
				{
					return;
				}
				String L_LRYNO = getRSTVAL(L_rstRSSET,"IVT_LRYNO","C");
				String L_MKTTP = getRSTVAL(L_rstRSSET,"IVT_MKTTP","C");
				lblVHDMSG.setText("Gate In : "+LP_GINNO+"        Lorry No.: "+L_LRYNO);
				M_strSQLQRY = "select iv1_srlno,iv1_detcd,cmt_codds,iv1_strtm,iv1_endtm,iv1_efftm from fg_ivtr1,co_cdtrn  where cmt_cgmtp='SYS' and cmt_cgstp='FGXXVHD' and cmt_codcd=iv1_detcd and IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv1_mkttp = '"+L_MKTTP+"' and iv1_ginno = '"+LP_GINNO+"' order by iv1_srlno";
				//System.out.println(M_strSQLQRY);
				L_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				int i =0;
				if(L_rstRSSET.next() && L_rstRSSET!=null)
				{
					while (true)
					{
						tblVHDTBL.setValueAt(getRSTVAL(L_rstRSSET,"IV1_SRLNO","C"),i,intTB6_SRLNO);
						tblVHDTBL.setValueAt(getRSTVAL(L_rstRSSET,"IV1_DETCD","C"),i,intTB6_DETCD);
						tblVHDTBL.setValueAt(getRSTVAL(L_rstRSSET,"CMT_CODDS","C"),i,intTB6_DETDS);
						tblVHDTBL.setValueAt(getRSTVAL(L_rstRSSET,"IV1_STRTM","T"),i,intTB6_STRTM);
						tblVHDTBL.setValueAt(getRSTVAL(L_rstRSSET,"IV1_ENDTM","T"),i,intTB6_ENDTM);
						tblVHDTBL.setValueAt(getRSTVAL(L_rstRSSET,"IV1_EFFTM","N"),i,intTB6_EFFTM);
						i++;
						if(!L_rstRSSET.next())
							break;
					}
					L_rstRSSET.close();
				}
				
				setCursor(cl_dat.M_curDFSTS_pbst);
				pnlVHDTN.setSize(100,100);
				pnlVHDTN.setPreferredSize(new Dimension(700,250));
				boolean L_flgEXIST = false;
				int L_intOPTN=JOptionPane.showConfirmDialog( this,pnlVHDTN,"Vehicle Detention",JOptionPane.OK_CANCEL_OPTION);
				if(L_intOPTN == 0)
				{
					
					for(int j=0;j<tblVHDTBL.getRowCount();j++)
					{
						if(tblVHDTBL.getValueAt(j,intTB6_DETCD).toString().length()<2)
						{
							break;
						}
						strWHRSTR = " iv1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND iv1_mkttp = '"+L_MKTTP+"' and iv1_ginno = '"+LP_GINNO+"' and iv1_srlno='"+tblVHDTBL.getValueAt(j,intTB6_SRLNO).toString()+"'";
						flgCHK_EXIST =  chkEXIST("FG_IVTR1",strWHRSTR);
		
						if(!flgCHK_EXIST)
						{
							M_strSQLQRY = "insert into fg_ivtr1 (iv1_cmpcd,iv1_mkttp,iv1_ginno,iv1_srlno,iv1_detcd,iv1_strtm,iv1_endtm,iv1_efftm,iv1_trnfl,iv1_stsfl,iv1_lupdt,iv1_lusby) values ("
							+setINSSTR("IV1_CMPCD",cl_dat.M_strCMPCD_pbst,"C")
							+setINSSTR("IV1_MKTTP",L_MKTTP,"C")
							+setINSSTR("IV1_GINNO",LP_GINNO,"C")
							+setINSSTR("IV1_SRLNO",tblVHDTBL.getValueAt(j,intTB6_SRLNO).toString(),"C")
							+setINSSTR("IV1_DETCD",tblVHDTBL.getValueAt(j,intTB6_DETCD).toString(),"C")
							+setINSSTR("IV1_STRTM",tblVHDTBL.getValueAt(j,intTB6_STRTM).toString(),"T")
							+setINSSTR("IV1_ENDTM",tblVHDTBL.getValueAt(j,intTB6_ENDTM).toString(),"T")
							+setINSSTR("IV1_EFFTM",tblVHDTBL.getValueAt(j,intTB6_EFFTM).toString(),"N")
							+setINSSTR("IV1_TRNFL","0","C")
							+setINSSTR("IV1_STSFL","1","C")
							+setINSSTR("IV1_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
							+"'"+cl_dat.M_strUSRCD_pbst+"')";
						}
						else
						{
							M_strSQLQRY = "update fg_ivtr1 set "
							+setUPDSTR("IV1_DETCD",tblVHDTBL.getValueAt(j,intTB6_DETCD).toString(),"C")
							+setUPDSTR("IV1_STRTM",tblVHDTBL.getValueAt(j,intTB6_STRTM).toString(),"T")
							+setUPDSTR("IV1_ENDTM",tblVHDTBL.getValueAt(j,intTB6_ENDTM).toString(),"T")
							+setUPDSTR("IV1_EFFTM",tblVHDTBL.getValueAt(j,intTB6_EFFTM).toString(),"N")
							+setUPDSTR("IV1_TRNFL","0","C")
							+setUPDSTR("IV1_STSFL","1","C")
							+setUPDSTR("IV1_LUPDT",cl_dat.M_txtCLKDT_pbst.getText(),"D")
							+"IV1_LUSBY ='"+cl_dat.M_strUSRCD_pbst+"' where "+strWHRSTR;
						}
						//System.out.println(M_strSQLQRY);
						cl_dat.exeSQLUPD(M_strSQLQRY,"");					
						if(cl_dat.exeDBCMT("getVHDTN"))
							setMSG("Record Saved",'N');
						M_strSQLQRY = "delete from fg_ivtr1 where IV1_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND upper(iv1_detcd)='XX'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"");					
						if(cl_dat.exeDBCMT("getVHDTN"))
							setMSG("Query Executed",'N');
						setMSG("Saved Successfully",'N');
					}
				}
				setCursor(cl_dat.M_curDFSTS_pbst);	
			}

			catch (Exception L_EX)
			{setMSG("Error in getVHDTN : "+L_EX,'E');}
	}

	
	private void clrVHDTBL(){ //clears Vehicle Detention Table
		for(int i = 0;i < tblVHDTBL.getRowCount();i++){
			for(int j = 0;j<(tblVHDTBL.getColumnCount()-1);j++){
					tblVHDTBL.setValueAt("",i,j+1);
				}
			}
	}
	
	
	
	
	/**
    Method to Calculate the differance two Date & Time.
	@param P_strFINTM String argument to Final Time.
	@param P_strINITM String argument to Initial Time.
    */
	public String calTIME(String P_strFINTM,String P_strINITM)
	{
		String L_strDIFTM = "";
		try
		{
			int L_intYRS,L_intMTH,L_intDAY,L_intHRS,L_intMIN;
			int L_intYRS1,L_intMTH1,L_intDAY1,L_intHRS1,L_intMIN1;
			int L_intYRS2,L_intMTH2,L_intDAY2,L_intHRS2,L_intMIN2;
			String L_strHOUR,L_strMINT;			
			if(P_strFINTM.equals("") || P_strINITM.equals(""))
				return L_strDIFTM;			
			// Seperating year,month,day,hour & minute from Final time
			L_intYRS1 = Integer.parseInt(P_strFINTM.substring(6,10));
			L_intMTH1 = Integer.parseInt(P_strFINTM.substring(3,5));
			L_intDAY1 = Integer.parseInt(P_strFINTM.substring(0,2));
			L_intHRS1 = Integer.parseInt(P_strFINTM.substring(11,13));
			L_intMIN1 = Integer.parseInt(P_strFINTM.substring(14));			
			// Seperating year,month,day,hour & minute from Initial time
			L_intYRS2 = Integer.parseInt(P_strINITM.substring(6,10));
			L_intMTH2 = Integer.parseInt(P_strINITM.substring(3,5));
			L_intDAY2 = Integer.parseInt(P_strINITM.substring(0,2));
			L_intHRS2 = Integer.parseInt(P_strINITM.substring(11,13));
			L_intMIN2 = Integer.parseInt(P_strINITM.substring(14));			
			L_intMIN = L_intMIN1 - L_intMIN2;
			L_intHRS = L_intHRS1 - L_intHRS2;			
			// Checking for leap year
			if(L_intYRS1%4 == 0)
				arrDAYS[1] = "29";
			else
				arrDAYS[1] = "28";			
			// Final date is of next month
			if(L_intMTH1 > L_intMTH2)
			{
				for(int i = L_intMTH2;i < L_intMTH1;i++)
					L_intDAY1 += Integer.parseInt(arrDAYS[i-1]);
			}			
			L_intDAY = L_intDAY1 - L_intDAY2;			
			if(L_intMIN < 0)
			{
				L_intMIN += 60;
				L_intHRS--;
			}
			if(L_intHRS < 0)
			{
				L_intHRS += 24;
				L_intDAY--;
			}
			if(L_intDAY > 0)
				L_intHRS += L_intDAY*24;			
			L_strHOUR = String.valueOf(L_intHRS);
			L_strMINT = String.valueOf(L_intMIN);
			if(L_strHOUR.length() < 2)
				L_strHOUR = "0" + L_strHOUR;
			if(L_strMINT.length() < 2)
				L_strMINT = "0" + L_strMINT;
			L_strDIFTM = L_strHOUR + ":" + L_strMINT;
		}
		catch(Exception L_EX)
		{
			setMSG("Error in calTIME : "+L_EX,'E');
		}
		return L_strDIFTM;
	}	
	

	
/** Generating string for Insertion Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private String setINSSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try 
{
	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
	if (LP_FLDTP.equals("C"))
		 return  "'"+nvlSTRVL(LP_FLDVL,"")+"',";
 	else if (LP_FLDTP.equals("N"))
         return   nvlSTRVL(LP_FLDVL,"0") + ",";
 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDVL.length()>=10) ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else if (LP_FLDTP.equals("T"))
		 return   (LP_FLDVL.length()>10) ? ("'"+M_fmtDBDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,";
	else return " ";
        }
    catch (Exception L_EX) 
	{setMSG("Error in setINSSTR : "+L_EX,'E');}
return " ";
}
		



/** Generating string for Updation Query
 * @param	LP_FLDNM	Field name to be inserted
 * @param	LP_FLDVL	Content / value of the field to be inserted
 * @param	LP_FLDTP	Type of the field to be inserted
 */
private String setUPDSTR(String LP_FLDNM, String LP_FLDVL, String LP_FLDTP) {
try 
{
	//System.out.println(LP_FLDNM+" : "+LP_FLDVL+" : "+LP_FLDTP);
	if (LP_FLDTP.equals("C"))
		 return (LP_FLDNM + " = '"+nvlSTRVL(LP_FLDVL,"")+"',");
 	else if (LP_FLDTP.equals("N"))
         return   (LP_FLDNM + " = "+nvlSTRVL(LP_FLDVL,"0") + ",");
 	else if (LP_FLDTP.equals("D"))
		 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>=10 ? ("'"+M_fmtDBDAT.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
	else if (LP_FLDTP.equals("T"))
		 return   (LP_FLDNM + " = "+(LP_FLDVL.length()>10 ? ("'"+M_fmtDBDTM.format(M_fmtLCDAT.parse(LP_FLDVL))+"',") : "null,"));
	else return " ";
        }
    catch (Exception L_EX) 
	{setMSG("Error in setUPDSTR : "+L_EX,'E');}
return " ";
}


/** Checking key in table for record existance
 */
private boolean chkEXIST(String LP_TBLNM, String LP_WHRSTR)
{
	boolean L_flgCHKFL = false;
	try
	{
		M_strSQLQRY =	"select * from "+LP_TBLNM + " where "+ LP_WHRSTR;
		ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
		if (L_rstRSSET != null && L_rstRSSET.next())
		{
			L_flgCHKFL = true;
			L_rstRSSET.close();
		}
	}
	catch (Exception L_EX)	
	{setMSG("Error in chkEXIST : "+L_EX,'E');}
	return L_flgCHKFL;
}

	/**
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		this.setCursor(cl_dat.M_curWTSTS_pbst);updateUI();
		int L_intKEYCD=L_KE.getKeyCode();
		try
		{
			if(tblVHDTBL != null && tblVHDTBL.isEditing())
			{
				if(L_intKEYCD==L_KE.VK_F1 && M_objSOURC==tblVHDTBL.cmpEDITR[intTB6_DETCD])
				{
						M_strHLPFLD = "txtDETCD";
						cl_hlp("Select CMT_CODCD,CMT_CODDS from CO_CDTRN  where cmt_cgmtp='SYS' and cmt_cgstp='FGXXVHD' and cmt_codcd > '70' order by cmt_codcd" ,2,1,new String[] {"Code","Description"},2,"CT");
				}
			}
		}catch(Exception e)	{setMSG(e,"Child.KeyPressed");}
		 finally{this.setCursor(cl_dat.M_curDFSTS_pbst);}
	}


	/** Action to be executed after selecting a code using F1
	 */
	public void exeHLPOK()
	{
		try
		{
			cl_dat.M_flgHELPFL_pbst = false;
			super.exeHLPOK();
			cl_dat.M_wndHLP_pbst=null;
			StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			if(M_strHLPFLD.equals("txtDETCD"))
			{
				txtDETCD.setText(L_STRTKN.nextToken());
				tblVHDTBL.setValueAt(L_STRTKN.nextToken(),tblVHDTBL.getSelectedRow(),intTB6_DETDS);
				tblVHDTBL.setValueAt(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText(),tblVHDTBL.getSelectedRow(),intTB6_STRTM);
				tblVHDTBL.setValueAt(cl_dat.M_txtCLKDT_pbst.getText()+" "+cl_dat.M_txtCLKTM_pbst.getText(),tblVHDTBL.getSelectedRow(),intTB6_ENDTM);
				tblVHDTBL.setColumnSelectionInterval(intTB6_DETDS,intTB6_STRTM);
			}
		}catch(Exception L_EX)
		{
			setMSG(L_EX,"in Child.exeHLPOK");
		}
	}
	
	

/** Data validation before saving the record
 */
boolean vldDATA()
	{
		try
		{
			if(tblVHDTBL.isEditing())
			{
				if(tblVHDTBL.getValueAt(tblVHDTBL.getSelectedRow(),tblVHDTBL.getSelectedColumn()).toString().length()>0)
				{
				TBLINPVF obj=new TBLINPVF();
				obj.setSource(tblVHDTBL);
				if(obj.verify(tblVHDTBL.getSelectedRow(),tblVHDTBL.getSelectedColumn()))
					tblVHDTBL.getCellEditor().stopCellEditing();
				else
					return false;
				}
			}
		}catch (Exception e)
		{
			setMSG(e,"vldDATA");
			return false;
		}
		return true;
	}
	

	/**
	 */
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{//System.out.println("inpvf"+getSource());
				if(getSource()==tblVHDTBL)
				{	
					if(P_intCOLID==intTB6_SRLNO)
						{if(getSource().getValueAt(P_intROWID,P_intCOLID).toString().length()!=2) 	{setMSG("Invalid Srl.No.",'E'); return false;}}
				}

				
			}catch(Exception e)
			{
				setMSG(e,"TableInputVerifier");
				setMSG("Invalid Data ..",'E');				
				return false;
			}
			setMSG("",'N');
			return true;
		}
	}



}				