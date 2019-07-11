/*
System Name   : Material Management System
Program Name  : Dip Entry 
Program Desc. :
Author        : N.K.Virdi
Date          : 18 Apr 2005
Version       : MMS v2.0.0
Modificaitons 
Modified By   :
Modified Date :
Modified det. :
Version       :
*/ 
/* Dip Register Entry */

import javax.swing.*;
import java.awt.event.*;
import javax.swing.JTable.*;
import java.awt.Color;
import java.sql.ResultSet;import java.sql.PreparedStatement;import java.awt.event.MouseEvent;
import java.util.Calendar;import java.util.StringTokenizer;import java.util.Hashtable;
import java.sql.Timestamp;import java.sql.Date;
/**
<PRE>
System Name : Material Management System.

Program Name : Dip Entry

Purpose : This module captures  Daily Dip Entries.Temprature and Dip reading is recorded to
calculate the stock quantity.

List of tables used :
Table Name		Primary key                          Operation done
                                                      Insert  Update   Query     Delete	
-----------------------------------------------------------------------------------------------------------------------------------------------------
MM_DPTRN        DP_MEMTP,DP_MEMNO,DP_TNKNO            *       *         *        *   
MM_TKMST        TK_TNKNO                                                *
MM_TKCTR        TKC_TNKNO,TKC_DEPCT                                     *
MM_WBTRN        WB_DOCTP,WB_DOCNO,WB_SRLNO                              *
MM_RMMST        RM_STRTP,RM_TRNTP,RM_DOCTP,RM_DOCNO   *       *         *  
CO_CDTRN        CMT_CGMTP,CMT_CGSTP,CMT_CODCD                 *         *           
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of  fields accepted/displayed on screen :
Field Name		Column Name		Table name		Type/Size	Description
-----------------------------------------------------------------------------------------------------------------------------------------------------
cmbMEMTP		DP_MEMTP		MM_DPTRN		Varchar(2)	    Memo Type
txtMEMNO		DP_MEMNO		MM_DPTRN		Varchar (8)	    Memo No.
txtMEMDT		DP_MEMDT		MM_DPTRN		Timestamp	    Memo Date
txtMEMTM		                                                Memo Time
txtREMDS		RM_REMDS		MM_RMMST		Varchar (200)	Remark
TB1_TNKNO       TK_TNKNO        MM_TKMST        Varchar(10)     Tank No.
                DP_TNKNO        MM_DPTRN
TB1_MATDS       CT_MATDS        CO_CDTRN        Varchar(45)     Material Description
TB1_DIPVL		DP_DIPVL		MM_DPTRN		Decimal(6,1)	Dip Value
TB1_TMPVL 		DP_TMPVL		MM_DPTRN		Decimal(6,2)	Tempreture
TB1_SGRVL		DP_DENVL		MM_DPTRN		Decimal(9,4)	Specific Gravity
TB1_DOPQT		DP_DOPQT		MM_DPTRN		Decimal(16,3)	Day Opening Quantity
TB1_UOMCD		CT_UOMCD		CO_CDTRN		Varchar(3)	    Unit of Measurement
TB1_CONQT		                                                Calculated Field
TB1_RCTQT		DP_RCTQT		MM_DPTRN		Decimal(11,3)	Receipt Quantity
TB1_MNDVL		TK_MNDVL		MM_TKMST		Decimal(6,1)	Minimum Depth
TB1_MXDVL		TK_MXDVL		MM_TKMST		Decimal(6,1)	Maximum Depth
TB1_MATCD       TK_MATCD        MM_TKMST        Varchar(10)     Material Code
TB2_TNKNO		WB_TNKNO		MM_WBTRN		Varchar(10)	    Tank No.
TB2_GINNO		WB_DOCNO		MM_WBTRN		Varchar(8)	    Gate-In No.
TB2_TPRDS		WB_TPRDS		MM_WBTRN		Varchar(40)	    Transporter Description
TB2_LRYNO		WB_LRYNO		MM_WBTRN		Varchar(15)	    Lorry No.
TB2_CHLNO		WB_CHLNO		MM_WBTRN		Varchar(15)	    Chalan No.
TB2_CHLDT		WB_CHLDT		MM_WBTRN		Date		    Chalan Date
TB2_CHLQT 		WB_CHLQT		MM_WBTRN		Decimal(12,3)	Chalan Quantity
TB2_UOMQT       WB_UOMQT        MM_WBTRN        Decimal(12,3)   UOM Qty.
TB2_BOENO       WB_BOENO        MM_WBTRN        Varchar(15)     Bill of Entry No.
-----------------------------------------------------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name	Display Description			Display Columns			Table Name
-----------------------------------------------------------------------------------------------------------------------------------------------------
txtMEMNO	Memo No.,Memo Date			DP_MEMNO,DP_MEMDT		MM_DPTRN
-----------------------------------------------------------------------------------------------------------------------------------------------------

Logic   :  Dip is taken daily at 06:00 to check the day opening stock. Dip reading and
           temprature reading is entered, using which day opening quantity is calculated.
           day opening quantity(DP_DOPQT) of current day goes in day closing qunatity
           (DP_DCLQT)for the previous day.
	
Validations :
			
Memo Types 	
81 - Regular : This type of Memo is allowed to enter only once in a day and 
               as per the reading on 0600 hrs
91 - Adhoc   : No such constraints

Memo No. logic : First digit is Financial Year,next two digits are Memo Type and
                 remaining five digits are serial number within that Memo Type.
				 stored in DOC / MMXXDIP

<b>Opening Quantity calculation logic </b>
 
1. Take temprature nearer to recorded temprature and pick up Specific gravity at that 
   temprature for given Material from CO_QPMST.
             
2. Take Depth value from Tank Calibration Details such as  max(Depth Value) <= Given Dip
   if  no such Dip is found , then get the Dip Value such as  min(Depth Value) > Given Dip
   Pick up Tank Depth, Tank Volume & Incremental Volume

3.Calculate Quantity in MT as 
  (Tank Volume  + abs(Given Dip - Tank Depth) * Incremental Volume) / 1000  *  Sp.Gravity

4. Calculate Quantity in UOM as
   To convert in LT   :  (Quantity in MT / Sp.Gravity ) * 1000
   To convert in KL   :  Quantity in MT / Sp.Gravity
   To convert in KG   :  Quantity in MT  * 1000
	
 */
class mm_tedpt extends cl_pbase
{
	private boolean flgFIRST = true;
	private boolean flgREMDS = false;
	private boolean flgVLDDT = false;
	private ResultSet M_rstRSSET1;
	private PreparedStatement pstmDIPREG,pstmTNKNO,pstmUPDDIP,pstmPRVDIP;//,pstmUPDRCT
	private cl_JTable tblDIPVL,tblWBRVL;
	private int intROWCT1,LM_WBRROW;
	private JComboBox cmbMEMTP;
	private JTextField txtMEMNO,txtMEMDT,txtMEMTM,txtDEPCT,txtDEPVL,txtINCVL,txtTMPVL,txtDIPVL;
	private JTextField txtITMCD,txtVOLQT,txtREMDS;
	private JLabel lblGINLR,lblRCTLR,lblRCTQT;
	private JButton btnPRINT,btnWTTNK;
	private String strMEMDT,strMEMTP,strMEMNO,strMEMTM,strLUSBY,strLUPDT,strCURDT,strMATCD,strTMPSTR;
	private String strTTKNO,strTDPVL,strTTPVL,strDOPQT,strSGRVL,strUOMCD,strREMDS;
	private String strODPVL = "",strOTPVL = "";		// old values
	private String strTDPVL1,strTTPVL1,strTTKNO1,strPRVDT;
	private String strPMEMTP,strPMEMNO;
	private String strTNKNO,strGINNO,strTPRDS,strLRYNO,strCHLNO,strCHLDT,strCHLQT,strUOMQT;
	private String strBOENO,strCHKFL,strACPTG,strOLDFL;
	private JButton btnCLS ; 
	private JDialog wndDLG;
	private float fltDEPCT,fltDEPVL,fltINCVL,fltMATGR;
	private int intINDEX,intTBLROW,intTBLCL,intROWCT,intOROWVL;
	private float fltTNETWT;
	private final int TB1_CHKFL = 0;
	private final int TB1_TNKNO = 1;
	private final int TB1_MATDS = 2;
	private final int TB1_DIPVL = 3;
	private final int TB1_TMPVL = 4;
	private final int TB1_SGRVL = 5;
	private final int TB1_DOPQT = 6;
	private final int TB1_UOMCD = 7;
	private final int TB1_CONQT = 8;
	private final int TB1_RCTQT = 9;
	private final int TB1_MNDVL = 10;
	private final int TB1_MXDVL = 11;
	private final int TB1_MATCD = 12;
	
	private final int TB2_CHKFL = 0;
	private final int TB2_TNKNO = 1;
	private final int TB2_GINNO = 2;
	private final int TB2_TPRDS = 3;
	private final int TB2_LRYNO = 4;
	private final int TB2_CHLNO = 5;
	private final int TB2_CHLDT = 6;
	private final int TB2_CHLQT = 7;
	private final int TB2_UOMQT = 8;
	private final int TB2_BOENO = 9;
	private final int TB2_OLDFL = 10;
	
	private Hashtable<String,String> hstDIPVL = new Hashtable<String,String>();	
	private Hashtable<String,String> hstPRVQT = new Hashtable<String,String>();	
	
	private final String strREGDP_fn = "81";		// Regular Dip 
	private final String strDOCTP_fn = "01";		// Tanker - Raw Material
	private final String strTRNFL_fn = "0";			// Transfer flag
	private final String strSTRTP_fn = "04";		// Tankfarm Store Type
	private final String strTRNTP_fn = "DP";		// Transaction Type
	private INPVF objINPVR = new INPVF();
	mm_tedpt()
	{
		super(2);
		try
		{
			setMatrix(20,8);
			add(new JLabel("Memo Type"),1,1,1,1,this,'L');
			String L_strTEMP;
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXDIP'";
			M_strSQLQRY += " and isnull(CMT_STSFL,'') <> 'X' order by CMT_CODCD";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			add(cmbMEMTP = new JComboBox(),1,2,1,1,this,'L');
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					L_strTEMP = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
					cmbMEMTP.addItem(L_strTEMP);
				}
				M_rstRSSET.close();
			}
			add(new JLabel("Memo Number"),1,3,1,1,this,'L');
			add(txtMEMNO = new TxtLimit(8),1,4,1,1,this,'L');
			add(new JLabel("Memo Date and Time"),1,5,1,2,this,'L');
			add(txtMEMDT = new TxtDate(),1,7,1,1,this,'L');
			add(txtMEMTM = new TxtTime(),1,8,1,1,this,'L');
		
			add(new JLabel("Remarks"),2,1,1,1,this,'L');
			add(txtREMDS = new TxtLimit(200),2,2,1,5,this,'L');
			add(btnWTTNK = new JButton("Waiting"),2,7,1,1,this,'L');
			add(btnPRINT = new JButton("Print"),2,8,1,1,this,'L');
	
			String[] L_COLHD = {"","Tank No.","Material","Dip","Temp","Sp.Gravity","Quantity","UOM","Cons.","Rcpt.Qty.","Min.Dip","Max.Dip","Mat.Code"};
			int[] L_COLSZ = {20,70,135,50,45,55,60,40,70,70,60,60,80};
			tblDIPVL = crtTBLPNL1(this,L_COLHD,10,3,1,7,7.9,L_COLSZ,new int[]{0});
			tblDIPVL.setCellEditor(TB1_TMPVL,txtTMPVL = new TxtNumLimit(6.2));
			tblDIPVL.setCellEditor(TB1_DIPVL,txtDIPVL = new TxtLimit(6));
			txtTMPVL.addKeyListener(this);txtTMPVL.addFocusListener(this);
			txtDIPVL.addKeyListener(this);txtDIPVL.addFocusListener(this);
			L_COLHD = new String[]{"","Tank No.","Gate-In No.","Transporter","Lorry No.","Chalan No.","Chalan Date","Chalan Qty","Net Weight","B/E No.","0"};
			L_COLSZ = new int[]{10,70,70,135,80,75,70,60,60,100,10};
			tblWBRVL = crtTBLPNL1(this,L_COLHD,100,14,1,5,7.9,L_COLSZ,new int[]{0});
	
			add(new JLabel("Depth"),11,1,1,1,this,'L');
			add(txtDEPCT = new JTextField(),11,2,1,1,this,'L');
			add(new JLabel("Volume"),11,4,1,1,this,'L');
			add(txtDEPVL = new JTextField(),11,5,1,1,this,'L');
			add(new JLabel("Inc. Volume"),11,7,1,1,this,'L');
			add(txtINCVL = new JTextField(),11,8,1,1,this,'L');
		
			add(new JLabel("Total Tankers"),12,1,1,1,this,'L');
			add(lblGINLR = new JLabel(),12,2,1,1,this,'R');
		
			add(new JLabel("Unloaded Tankers"),12,4,1,1.5,this,'L');
			add(lblRCTLR = new JLabel(),12,5,1,0.5,this,'R');
			add(new JLabel("Receipt Quantity"),12,7,1,1.25,this,'L');
			add(lblRCTQT = new JLabel(),12,8,1,0.75,this,'R');
		
			btnWTTNK.setEnabled(false);	btnPRINT.setEnabled(false);
			txtDEPCT.setEnabled(false);txtDEPVL.setEnabled(false);
			txtINCVL.setEnabled(false);	cmbMEMTP.setEnabled(false);
			txtMEMNO.setEnabled(false);		
			lblGINLR.setForeground(new Color(176,28,54));
			lblRCTLR.setForeground(new Color(176,28,54));
			lblRCTQT.setForeground(new Color(176,28,54));
			tblDIPVL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
			tblWBRVL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
	
			tblDIPVL.addMouseListener(this);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			txtMEMDT.setInputVerifier(objINPVR);	
			txtMEMTM.setInputVerifier(objINPVR);	
			setENBL(false);
			setMSG("Select an Option..",'N');
			crtPRESTM();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
	}
	/**
	 * Enables / Disables the screen components
	 */
	void setENBL(boolean P_flgSTAT)
	{
		super.setENBL(P_flgSTAT);
		tblWBRVL.setEnabled(false);
		cmbMEMTP.setEnabled(!P_flgSTAT);
		txtMEMNO.setEnabled(!P_flgSTAT);
		txtMEMDT.setEnabled(P_flgSTAT);
		txtMEMTM.setEnabled(P_flgSTAT);
		tblDIPVL.setEnabled(P_flgSTAT);
		txtREMDS.setEnabled(P_flgSTAT);
		tblDIPVL.cmpEDITR[TB1_TNKNO].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_MATDS].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_SGRVL].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_DOPQT].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_UOMCD].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_CONQT].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_RCTQT].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_MNDVL].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_MXDVL].setEnabled(false);
		tblDIPVL.cmpEDITR[TB1_MATCD].setEnabled(false);
		cmbMEMTP.setEnabled(true);
		txtMEMNO.setEnabled(true);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
		{
			txtMEMNO.setEnabled(false);
			
		}
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			btnPRINT.setEnabled(true);
		flgREMDS = false;
		flgFIRST = true;
		hstPRVQT.clear();
	}
	
	/**
	 Method to clear the screen
	*/
	void clrCOMP()
	{
		setMSG("",'N');
		cmbMEMTP.setSelectedIndex(0);
		txtMEMNO.setText("");
		txtMEMDT.setText("");
		txtMEMTM.setText("");
		txtDEPCT.setText("");
		txtDEPVL.setText("");
		txtINCVL.setText("");
		txtREMDS.setText("");
		tblDIPVL.clrTABLE();
		tblWBRVL.clrTABLE();
		lblGINLR.setText("");
		lblRCTLR.setText("");
		lblRCTQT.setText("");
		flgREMDS = false;
		flgFIRST = true;
	}
	/**
	 *  In mouse pressed event on Dip Table, Tank depth, volume and incremental volume 
	 *  of corresponding tank is displyed. 
	 */
	public void mousePressed(MouseEvent L_ME)
	{
		super.mousePressed(L_ME);
		if(L_ME.getSource().equals(tblDIPVL))
		{
			intTBLROW = tblDIPVL.getSelectedRow();
			intTBLCL = tblDIPVL.getSelectedColumn();
			strTTKNO = String.valueOf(tblDIPVL.getValueAt(intTBLROW,TB1_TNKNO));
			strTDPVL = String.valueOf(tblDIPVL.getValueAt(intTBLROW,TB1_DIPVL));
			strTTPVL = String.valueOf(tblDIPVL.getValueAt(intTBLROW,TB1_TMPVL));
			if(strTDPVL.length() > 0)
				setVOL(strTTKNO,strTDPVL);
			else
			{
				txtDEPCT.setText("");
				txtDEPVL.setText("");
				txtINCVL.setText("");
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		try
		{
			super.actionPerformed(L_AE);
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				clrCOMP();		// Clear all fields
				strMEMTP = String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2);
				btnPRINT.setEnabled(false);
				btnWTTNK.setEnabled(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
				{
					setENBL(true);
					cmbMEMTP.setEnabled(true);
					tblDIPVL.setEnabled(false);
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
				{
					setENBL(false);
					txtMEMNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
				{
					setENBL(false);
					txtMEMNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))		
				{
					setENBL(false);
					txtMEMNO.requestFocus();
					btnWTTNK.setEnabled(true);
				}
				else
				{
					setENBL(false);
					cmbMEMTP.setEnabled(false);
					txtMEMNO.setEnabled(false);
					setMSG("Select an option",'N');
					btnWTTNK.setEnabled(false);
				}
			}
			if(M_objSOURC == btnPRINT)
			{
				mm_rpdpt objDIPRP  = new mm_rpdpt(M_strSBSCD);
				objDIPRP.getALLREC(strPMEMTP,strPMEMNO);
				JComboBox L_cmbLOCAL = objDIPRP.getPRNLS();
				objDIPRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpdpt.doc",L_cmbLOCAL.getSelectedIndex());
				btnPRINT.setEnabled(false);
				strPMEMTP = "";
				strPMEMNO = "";
			}
			else if(M_objSOURC == btnWTTNK)
			{
				getWTTNK();
			}
			else if(L_AE.getSource().equals(btnCLS))
			{
				wndDLG.dispose();
				txtMEMNO.requestFocus();
			}
			else if(M_objSOURC == cmbMEMTP)
			{
				setMSG("",'N');
				strMEMTP = String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(strMEMTP.equals(strREGDP_fn))			
					{
						txtMEMDT.setText(cl_dat.M_strLOGDT_pbst);
						txtMEMTM.setText("06:00");
					}
					else		
					{
						txtMEMDT.setText(cl_dat.M_txtCLKDT_pbst.getText());
						txtMEMTM.setText(cl_dat.M_txtCLKTM_pbst.getText());
					}
					txtMEMTM.requestFocus();
				}
				else
					txtMEMNO.requestFocus();
			}
			else if(M_objSOURC == txtMEMNO)
			{
				if(txtMEMNO.getText().trim().length() ==0)
				{
					setMSG("Enter a valid Memo number ..",'E');
					return;
				}
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				strMEMTP = String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2);
				strMEMNO = txtMEMNO.getText().trim();
				tblDIPVL.setRowSelectionInterval(0,0);
				tblDIPVL.setColumnSelectionInterval(0,0);
				tblWBRVL.setRowSelectionInterval(0,0);
				tblWBRVL.setColumnSelectionInterval(0,0);
				getTNKNO();
				exeGETREC(strMEMTP,strMEMNO);
				strTTKNO = String.valueOf(tblDIPVL.getValueAt(0,TB1_TNKNO));
				strTDPVL = String.valueOf(tblDIPVL.getValueAt(0,TB1_DIPVL));
				getPRVQT(txtMEMDT.getText());
				getWBRVL(txtMEMDT.getText());
				
				// Getting the data of the previous day's Memo
				// For Regular Dip only, For calculating the Day consumption
				if(String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2).equals(strREGDP_fn))
				{
					for(int i=0;i<intROWCT;i++)
					{
						strTTKNO = String.valueOf(tblDIPVL.getValueAt(i,TB1_TNKNO));
						strDOPQT = String.valueOf(tblDIPVL.getValueAt(i,TB1_DOPQT));
						calDAYCON(i,strTTKNO,strDOPQT);
					}
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
				{
					setENBL(true);
					txtMEMDT.setEnabled(false);
					txtMEMTM.setEnabled(false);
					tblDIPVL.requestFocus();
					tblDIPVL.setRowSelectionInterval(0,0);
					tblDIPVL.setColumnSelectionInterval(TB1_DIPVL,TB1_DIPVL);
					tblDIPVL.editCellAt(0,TB1_DIPVL);
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_objSOURC == txtMEMTM)
			{
				if(flgVLDDT)
				{
					tblDIPVL.clrTABLE();
					tblWBRVL.clrTABLE();
					getTNKNO();
					getPRVQT(txtMEMDT.getText().substring(0,10));
					setMSG("",'N');
					getWBRVL(txtMEMDT.getText().substring(0,10));
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"actionP");
		}
	}
	public void keyTyped(KeyEvent L_KE)
	{
		tblWBRVL.getCellEditor(1,1).cancelCellEditing();
	}
	public void keyReleased(KeyEvent L_KE)
	{
		// Check for Usage 	
		try
		{
			tblWBRVL.getCellEditor(1,1).cancelCellEditing();
			super.keyReleased(L_KE);
			if(L_KE.getSource() == tblWBRVL)
			{
				if((!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))&&(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))				
					return;
				
				if(tblWBRVL.getSelectedColumn() == TB2_CHKFL)
					return;
				
				LM_WBRROW = tblWBRVL.getSelectedRow();
				strGINNO = tblWBRVL.getValueAt(LM_WBRROW,TB2_GINNO).toString().trim();
				if(!strGINNO.equals(""))
				{
					int  L_KEY = L_KE.getKeyCode();
					if(L_KEY == 61 || L_KEY == 109) // Key code for (-) minus 
					{		
						strCHKFL = tblWBRVL.getValueAt(LM_WBRROW,TB2_CHKFL).toString();
						if(strCHKFL.equals("true"))
						{
							strUOMQT = tblWBRVL.getValueAt(LM_WBRROW,TB2_UOMQT).toString();
							lblRCTLR.setText(String.valueOf(Integer.parseInt(lblRCTLR.getText()) - 1));
							//lblRCTQT.setText(setFMT(new BigDecimal(Double.parseDouble(lblRCTQT.getText()) - Double.parseDouble(strUOMQT)).toString(),3));
							lblRCTQT.setText(setNumberFormat(Double.parseDouble(lblRCTQT.getText()) - Double.parseDouble(strUOMQT),3));
							tblWBRVL.setValueAt(new Boolean(false),LM_WBRROW,TB2_CHKFL);
						}
					}
					else if(L_KEY == 45 || L_KEY == 107)// Key code for (+) plus 
					{	
						strCHKFL = tblWBRVL.getValueAt(LM_WBRROW,TB2_CHKFL).toString();
						if(strCHKFL.equals("false"))
						{
							strUOMQT = tblWBRVL.getValueAt(LM_WBRROW,TB2_UOMQT).toString();
							lblRCTLR.setText(String.valueOf(Integer.parseInt(lblRCTLR.getText()) + 1));
							lblRCTQT.setText(setNumberFormat(Double.parseDouble(lblRCTQT.getText()) + Double.parseDouble(strUOMQT),3));
							tblWBRVL.setValueAt(new Boolean(true),LM_WBRROW,TB2_CHKFL);
						}
					}
					tblWBRVL.getCellEditor(1,1).cancelCellEditing();
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"keyPressed");
		}
	}
	/**
	 *  VK_F1 event		: help is present on Memo number field
	 *  VK_UP / VK_DOWN : on tblDIPVL for displaying 
	 *					  <br> depth, volume and incremental volume for tank 
	 */
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(L_KE.getSource().equals(txtMEMNO))
			{				
				strMEMTP = String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2);
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMEMNO";
				String L_strARRHDR[] = {"Memo No.","Memo Date"};
				M_strSQLQRY = "Select distinct DP_MEMNO,CONVERT(varchar,DP_MEMDT,103) from MM_DPTRN ";
				M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strMEMTP + "'";
				if(txtMEMNO.getText().trim().length() >0)
					M_strSQLQRY += " AND DP_MEMNO like '" + txtMEMNO.getText().trim() + "%'";
				M_strSQLQRY += " and isnull(DP_STSFL,'') <> 'X' order by DP_MEMNO desc";
				cl_hlp(M_strSQLQRY,1,1,L_strARRHDR,2,"CT");
			}
		}
		else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtMEMDT)
			{
				txtMEMTM.requestFocus();
				setMSG("Enter the Time ..",'N');
			}
			if(M_objSOURC == txtMEMTM)
			{
				txtREMDS.requestFocus();
				setMSG("Enter the Remarks ..",'N');
			}
			if(M_objSOURC == txtDIPVL)
			{
				intTBLROW = tblDIPVL.getSelectedRow();
				intTBLCL = tblDIPVL.getSelectedColumn();
				strTTKNO = tblDIPVL.getValueAt(intTBLROW,TB1_TNKNO).toString().trim();
				strTDPVL = txtDIPVL.getText().trim();
				strTTPVL = tblDIPVL.getValueAt(intTBLROW,TB1_TMPVL).toString().trim();
				if(strTDPVL.length() > 0)
				{
					Float.parseFloat(strTDPVL);
					strTMPSTR = String.valueOf(hstDIPVL.get(strTTKNO));
					//if(!hstDIPVL.containsKey((String)strTTKNO))
					getVOL(strTTKNO,strTDPVL);
				/*	if(strTMPSTR.equals("null") || strODPVL != strTDPVL)
					{
						getVOL(strTTKNO,strTDPVL);
					}*/
					tblDIPVL.setRowSelectionInterval(intTBLROW,intTBLROW);
					tblDIPVL.setColumnSelectionInterval(TB1_DIPVL,TB1_TMPVL);
				}
				else
				{
					hstDIPVL.remove(strTTKNO);
					tblDIPVL.setValueAt("",intTBLROW,TB1_DOPQT);
				}
				intOROWVL = intTBLROW;
			}
			if(M_objSOURC == txtTMPVL)
			{
				intTBLROW = tblDIPVL.getSelectedRow();
				intTBLCL = tblDIPVL.getSelectedColumn();
				setMSG("",'N');
				strTTKNO = tblDIPVL.getValueAt(intTBLROW,TB1_TNKNO).toString().trim();
				strTDPVL = tblDIPVL.getValueAt(intTBLROW,TB1_DIPVL).toString().trim();
				strTTPVL = txtTMPVL.getText().trim();
				strMATCD = tblDIPVL.getValueAt(intTBLROW,TB1_MATCD).toString();
				if(strTTPVL.length() > 0)
				{
					Float.parseFloat(strTTPVL);
				//	if(strOTPVL != strTTPVL)
				//	{
						getSPGVL(intTBLROW,strTTKNO,strMATCD,Float.parseFloat(strTTPVL));
				//	}
								
					intOROWVL = intTBLROW;
					
					tblDIPVL.setRowSelectionInterval(intTBLROW+1,intTBLROW+1);
					//tblDIPVL.setColumnSelectionInterval(TB1_SGRVL,TB1_DIPVL);
					tblDIPVL.setColumnSelectionInterval(TB1_SGRVL,TB1_CHKFL);
					intTBLROW = tblDIPVL.getSelectedRow();
					intTBLCL = tblDIPVL.getSelectedColumn();
					tblDIPVL.editCellAt(intTBLROW,intTBLCL);			
				//	intTBLROW = tblDIPVL.getSelectedRow();
				//	intTBLCL = tblDIPVL.getSelectedColumn();		
					// Setting data for the current row
					// Get the Tank No, Dip Value & Temp value in the varaibles
					strTTKNO1 = tblDIPVL.getValueAt(intTBLROW,TB1_TNKNO).toString().trim();
					strTDPVL1 = tblDIPVL.getValueAt(intTBLROW,TB1_DIPVL).toString().trim();
					strTTPVL1 = tblDIPVL.getValueAt(intTBLROW,TB1_TMPVL).toString().trim();
					strODPVL = strTDPVL1;
					strOTPVL = strTTPVL1;
					// Setting the values of corresponding Tank Depth,Tank Volume 
					// & Incremental Volume
					if(strTDPVL1.length() > 0)
						setVOL(strTTKNO1,strTDPVL1);
					else
					{
						txtDEPCT.setText("");
						txtDEPVL.setText("");
						txtINCVL.setText("");
					}
				}
				else
				{
					tblDIPVL.setValueAt("",intTBLROW,TB1_SGRVL);
					tblDIPVL.setValueAt("",intTBLROW,TB1_DOPQT);
				}
				if(strTDPVL.length() > 0 && strTTPVL.length() > 0) 
					calMATQT(intOROWVL,strTTKNO,strTDPVL);
			}
		}
		else if(L_KE.getKeyCode() == L_KE.VK_UP || L_KE.getKeyCode() == L_KE.VK_DOWN)
		{
			try
			{
				intTBLROW = tblDIPVL.getSelectedRow();
				intTBLCL = tblDIPVL.getSelectedColumn();
				
				if(L_KE.getKeyCode() == L_KE.VK_UP)
				{
					if(intTBLROW != 0)
						intTBLROW -= 1;
				}
				else if(L_KE.getKeyCode() == L_KE.VK_DOWN)
				{
					if(intTBLROW != intROWCT -1)
						intTBLROW += 1;
				}
				strTTKNO = String.valueOf(tblDIPVL.getValueAt(intTBLROW,TB1_TNKNO));
				strTDPVL = String.valueOf(tblDIPVL.getValueAt(intTBLROW,TB1_DIPVL));
				strTTPVL = String.valueOf(tblDIPVL.getValueAt(intTBLROW,TB1_TMPVL));
				
				// Setting the values of corresponding Tank Depth,Tank Volume 
				// & Incremental Volume
				if(strTDPVL.length() > 0)
				{
					Float.parseFloat(strTDPVL);
					setVOL(strTTKNO,strTDPVL);
				}
				else
				{
					txtDEPCT.setText("");
					txtDEPVL.setText("");
					txtINCVL.setText("");
				}
				setMSG("",'N');
			}
			catch(NumberFormatException e)
			{
				setMSG("Please Enter the Decimal Value",'E');
			}
		}
	}
	
	/** 
	 * Method to get the Tank Numbers, Material code, description, UOM, Minimum and
	 * maximum possible dip reading is displayed into Dip Table from MM_TKMST,CO_CTMST
	 */ 
	private void getTNKNO()
	{
		int L_intTKCNT = 0;
	    String L_strTNKNO,L_strMATCD,L_strMATDS,L_strUOMCD,L_strRCTQT,L_strMNDVL,L_strMXDVL;
		String L_strTEMP;
		int L_intINDEX;
		try
		{
			M_rstRSSET = pstmTNKNO.executeQuery();
			pstmTNKNO.clearParameters();
			while(M_rstRSSET.next())
			{
				L_strTNKNO = nvlSTRVL(M_rstRSSET.getString("TK_TNKNO"),"");
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("TK_MATCD"),"");
				L_strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
				L_strUOMCD = nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),"");
				L_strMNDVL = nvlSTRVL(M_rstRSSET.getString("TK_MNDVL"),"");
				L_strMXDVL = nvlSTRVL(M_rstRSSET.getString("TK_MXDVL"),"");
				tblDIPVL.setValueAt(L_strTNKNO,L_intTKCNT,TB1_TNKNO);
				tblDIPVL.setValueAt(L_strMATDS,L_intTKCNT,TB1_MATDS);
				tblDIPVL.setValueAt(L_strUOMCD,L_intTKCNT,TB1_UOMCD);
				//tblDIPVL.setValueAt(L_RCTQT,L_intTKCNT,TB1_RCTQT);
				tblDIPVL.setValueAt(L_strMNDVL,L_intTKCNT,TB1_MNDVL);
				tblDIPVL.setValueAt(L_strMXDVL,L_intTKCNT,TB1_MXDVL);
				tblDIPVL.setValueAt(L_strMATCD,L_intTKCNT,TB1_MATCD);
				L_intTKCNT++;
			}
			intROWCT = L_intTKCNT;
			if(M_rstRSSET.next())
				M_rstRSSET.close();
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getTNKNO");
		}
	}
	/**
	 *  Action after selecting from Help screen 
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtMEMNO"))
			{
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				strMEMTP = String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2);
				txtMEMNO.setText(cl_dat.M_strHLPSTR_pbst);
				exeGETREC(strMEMTP,cl_dat.M_strHLPSTR_pbst);
				strTTKNO = String.valueOf(tblDIPVL.getValueAt(0,TB1_TNKNO));
				strTDPVL = String.valueOf(tblDIPVL.getValueAt(0,TB1_DIPVL));
				getPRVQT(txtMEMDT.getText());
				getWBRVL(txtMEMDT.getText());				
				// Getting the data of the previous day's Memo (For Regular Dip only)
				if(String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2).equals(strREGDP_fn))
				{
					for(int i=0;i<intROWCT;i++)
					{
						strTTKNO = String.valueOf(tblDIPVL.getValueAt(i,TB1_TNKNO));
						strDOPQT = String.valueOf(tblDIPVL.getValueAt(i,TB1_DOPQT));
						calDAYCON(i,strTTKNO,strDOPQT);
					}
				}
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
				{
					setENBL(true);
					txtMEMDT.setEnabled(false);
					txtMEMTM.setEnabled(false);
					tblDIPVL.requestFocus();
					tblDIPVL.setRowSelectionInterval(0,0);
					tblDIPVL.setColumnSelectionInterval(TB1_DIPVL,TB1_DIPVL);
					tblDIPVL.editCellAt(0,TB1_DIPVL);
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	/**
	 * Converts from Metric Ton to specific UOM of Item
	 * @param P_fltMATQT Quantity in MT 
	 * @param P_strUOMCD Unit of Measurment of Item, in which conversion to be done
	 * @param P_fltDENVL Specific Density value of the Item
	 * @return String specifying the Quanitity in UOM which is passed
	 */
	private String qtyINUOM(float P_fltMATQT,String P_strUOMCD,float P_fltDENVL)
	{
		float L_fltITMQT = 0;
		try
		{
			if(P_strUOMCD.equals("LT"))
				L_fltITMQT = (P_fltMATQT/P_fltDENVL) * 1000;
			else if(P_strUOMCD.equals("KL"))
				L_fltITMQT = P_fltMATQT/P_fltDENVL;
			else if(P_strUOMCD.equals("KG"))
				L_fltITMQT = P_fltMATQT * 1000;
			else if(P_strUOMCD.equals("MT"))
				L_fltITMQT = P_fltMATQT;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"qtyINUOM");
		}
		return String.valueOf(L_fltITMQT);
	}
	
	/** Method to generate the new Memo No depending upon the Memo type
	 *  @param P_strMEMTP	Memo Type : Possible values are Regular / Adhoc (81/91)
	 *  Dip Document number is stored in codes transaction CO_CDTRN 
	 *	CMT_CGMTP :  DOC
	 *	CMT_CGSTP :  MMXXDIP
	 *	CMT_CODCD :  FIN. year digit + 81  Regular
	 *				 FIN. year digit + 91  Adhoc	
	 */ 
	private void genMEMNO(String P_strMEMTP)throws Exception 
	{
		String L_strMEMNO  = "",L_strCODCD = "", L_strCCSVL = "";
		M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL from CO_CDTRN ";
		M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MMXXDIP' and ";
		M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + P_strMEMTP + "'";
		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
		if(M_rstRSSET != null)
		{
			if(M_rstRSSET.next())
			{
				L_strCODCD = M_rstRSSET.getString("CMT_CODCD");
				L_strCCSVL = M_rstRSSET.getString("CMT_CCSVL");
			}
			M_rstRSSET.close();
		}
		L_strCCSVL = String.valueOf(Integer.parseInt(L_strCCSVL) + 1);
		for(int i=L_strCCSVL.length(); i<5; i++)				// for padding zero(s)
			L_strMEMNO += "0";
		L_strCCSVL = L_strMEMNO + L_strCCSVL;
		L_strMEMNO = L_strCODCD + L_strCCSVL;
		txtMEMNO.setText(L_strMEMNO);
	}
	/** 
	 * Method to check whether all the required values are entered
	 */
	boolean vldDATA()
	{
		String L_strTMPVL,L_strDIPVL,L_strMATQT;
		int L_intTBLCL=0,L_intTBLRW=0;
		try
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))			
			{
				if(txtMEMDT.getText().trim().length() == 0)
				{
					txtMEMDT.requestFocus();
					setMSG("Memo Date/Time can not be empty",'E');
					return false;
				}
				if(txtMEMTM.getText().trim().length() == 0)
				{
					txtMEMTM.requestFocus();
					setMSG("Memo Date/Time can not be empty",'E');
					return false;
				}
			}
			if(tblDIPVL.getSelectedRow() >= 0)
				tblDIPVL.editCellAt(tblDIPVL.getSelectedRow(),tblDIPVL.getSelectedColumn());
			if(tblDIPVL.isEditing())
				tblDIPVL.getCellEditor().stopCellEditing();
			for(int i=0;i<intROWCT;i++)
			{
				L_strDIPVL = tblDIPVL.getValueAt(i,TB1_DIPVL).toString();
				L_strTMPVL = tblDIPVL.getValueAt(i,TB1_TMPVL).toString();
				L_strMATQT = tblDIPVL.getValueAt(i,TB1_DOPQT).toString();
				if(L_strDIPVL.length() == 0)
				{
					tblDIPVL.requestFocus();
					tblDIPVL.editCellAt(i,TB1_DIPVL);
					tblDIPVL.setRowSelectionInterval(i,i);
					tblDIPVL.setColumnSelectionInterval(TB1_DIPVL,TB1_DIPVL);
					setMSG("Please enter the value for Dip",'E');
					return false;
				}
				else
				{
					L_intTBLCL = TB1_DIPVL;
					L_intTBLRW = i;
					Float.parseFloat(L_strDIPVL);
				}
				if(L_strTMPVL.length() == 0)
				{
					tblDIPVL.requestFocus();
					tblDIPVL.editCellAt(i,TB1_TMPVL);
					tblDIPVL.setRowSelectionInterval(i,i);
					tblDIPVL.setColumnSelectionInterval(TB1_TMPVL,TB1_TMPVL);
					setMSG("Please enter the value for Temprature",'E');
					return false;
				}
				else
				{
					L_intTBLCL = TB1_TMPVL;
					L_intTBLRW = i;
					Float.parseFloat(L_strTMPVL);
				}
				if(L_strMATQT.length() == 0)
				{
					tblDIPVL.requestFocus();
					tblDIPVL.editCellAt(i,TB1_TMPVL);
					tblDIPVL.setRowSelectionInterval(i,i);
					tblDIPVL.setColumnSelectionInterval(TB1_TMPVL,TB1_TMPVL);
					setMSG("Please Calculate the Quantity",'E');
					return false;
				}
			}
		}
		catch(NumberFormatException e)
		{
			tblDIPVL.requestFocus();
			tblDIPVL.editCellAt(L_intTBLRW,L_intTBLCL);
			tblDIPVL.setRowSelectionInterval(L_intTBLRW,L_intTBLRW);
			tblDIPVL.setColumnSelectionInterval(L_intTBLCL,L_intTBLCL);
			setMSG("Please enter decimal value",'E');
			return false;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
			return false;
		}
		return true;
	}
	/**
	 *  Method to get the record from the database, Memo Type and MEmo number is passed
	 *	Data if fetched from MM_DPTRN WHERE DP_MEMNO = <I> given memo no <I>
	 * */
	private void exeGETREC(String P_strMEMTP,String P_strMEMNO)
	{
		boolean L_ISFIRST = true;
		java.sql.Timestamp tmsTEMP;
		intROWCT = 0;
		try
		{
			pstmDIPREG.setString(1,P_strMEMTP);
			pstmDIPREG.setString(2,P_strMEMNO);
			M_rstRSSET1 = pstmDIPREG.executeQuery();
			pstmDIPREG.clearParameters();
			if(M_rstRSSET1 !=null)
			{
				while(M_rstRSSET1.next())
				{
					tblDIPVL.setValueAt(M_rstRSSET1.getString("DP_TNKNO"),intROWCT,TB1_TNKNO);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("DP_DIPVL"),intROWCT,TB1_DIPVL);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("DP_TMPVL"),intROWCT,TB1_TMPVL);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("DP_DENVL"),intROWCT,TB1_SGRVL);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("DP_DOPQT"),intROWCT,TB1_DOPQT);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("CT_MATDS"),intROWCT,TB1_MATDS);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("CT_UOMCD"),intROWCT,TB1_UOMCD);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("DP_RCTQT"),intROWCT,TB1_RCTQT);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("TK_MNDVL"),intROWCT,TB1_MNDVL);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("TK_MXDVL"),intROWCT,TB1_MXDVL);
					tblDIPVL.setValueAt(M_rstRSSET1.getString("DP_MATCD"),intROWCT,TB1_MATCD);
					if(L_ISFIRST)
					{
						L_ISFIRST = false;
						strMEMTP = M_rstRSSET1.getString("DP_MEMTP");
						strMEMTM = M_rstRSSET1.getString("DP_MEMDT");
						tmsTEMP = M_rstRSSET1.getTimestamp("DP_MEMDT");
						if(tmsTEMP !=null)
						{
							strMEMTM = M_fmtLCDTM.format(tmsTEMP);
							txtMEMDT.setText(strMEMTM.substring(0,10));
							txtMEMTM.setText(strMEMTM.substring(11));
						}
						cmbMEMTP.setSelectedIndex(Integer.parseInt(strMEMTP.substring(1,2)) - 1);
						//txtMEMDT.setText(strMEMTM.substring(8,10)+"/"+ strMEMTM.substring(5,7)+"/"+strMEMTM.substring(0,4) + " " + strMEMTM.substring(11,16));
					}
					intROWCT++;
				}
				M_rstRSSET1.close();
			}
			// Get the record from the MM_RMMST
			if(intROWCT > 0)
			{
				M_strSQLQRY = "Select RM_REMDS from MM_RMMST ";
				M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + strSTRTP_fn + "'";
				M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
				M_strSQLQRY += " and RM_DOCTP = '" + P_strMEMTP + "'";
				M_strSQLQRY += " and RM_DOCNO = '" + P_strMEMNO + "'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET != null)
				{
					if(M_rstRSSET.next())
					{
						flgREMDS = true;
						txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
					}
					M_rstRSSET.close();
				}
			}
			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeGETREC");
		}
	}
	/**
	 * Method to get Depth, Volume, Incremental Volume from MM_TKCTR
	 * WHERE TKC_TNKNO =  given TANK NO.
	 * AND TKC_DEPCT  <=  given Dip reading
	 * if details not found with above condition then condition is given as 
	 * TKC_DEPCT  >  given Dip reading
	 * 
	 */
	private void getVOL(String P_strTNKNO,String P_strDIPVL)
	{
		float L_fltDEPCT = 0;
		float L_fltDEPVL = 0, L_fltINCVL = 0;
		
		try{
			// To get the depth at dip less than or equal to the given dip.
			M_strSQLQRY = "Select TKC_DEPCT,TKC_DEPVL,TKC_INCVL from";
			M_strSQLQRY += " MM_TKCTR where TKC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TKC_TNKNO = '" + P_strTNKNO + "' and ";
			M_strSQLQRY += "  TKC_DEPCT <= " + P_strDIPVL + " order by TKC_DEPCT desc";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET ==null)
				return;
			if(M_rstRSSET.next())
			{
				L_fltDEPCT = M_rstRSSET.getFloat("TKC_DEPCT");
				L_fltDEPVL = M_rstRSSET.getFloat("TKC_DEPVL");
				L_fltINCVL = M_rstRSSET.getFloat("TKC_INCVL");
				M_rstRSSET.close();
			}
			else
			{
				M_strSQLQRY = "Select TKC_DEPCT,TKC_DEPVL,TKC_INCVL from MM_TKCTR where ";
				M_strSQLQRY += " TKC_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TKC_TNKNO = '" + P_strTNKNO + "' and ";
				M_strSQLQRY += "  TKC_DEPCT > " + P_strDIPVL + " order by TKC_DEPCT asc";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET ==null)
					return;
				if(M_rstRSSET.next())
				{
					L_fltDEPCT = M_rstRSSET.getFloat("TKC_DEPCT");
					L_fltDEPVL = M_rstRSSET.getFloat("TKC_DEPVL");
					L_fltINCVL = M_rstRSSET.getFloat("TKC_INCVL");
				}
				M_rstRSSET.close();
			}
			// Put Tank Depth, Tank Volume & Incremental Volume into HashTable
			if(hstDIPVL.containsKey((String)P_strTNKNO));
		    hstDIPVL.remove((String)P_strTNKNO);
			hstDIPVL.put(P_strTNKNO,L_fltDEPCT + "|" + L_fltDEPVL + "|" +  L_fltINCVL);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getVOL");
		}
	}
	/**
	 *  Calculates the specific gravity value  from CO_QPMST
	 *  @param P_strTNKNO	Tank number 
	 *  @param P_fltTMPVL   Temprature value
	 *  Temprature nearer to the recorded temprature is taken.
	 *  Two temratures are taken. One just above or equal the given temp. and other is just below.
	 *  A temprature value is calculated from picking up the specific gravity value.
	 *  Logic for calculating temprature -
	 *  if(L_fltMAXTP == 0 || L_fltMINTP == 0)     
				L_fltCALTP = L_fltMAXTP + L_fltMINTP;
		else if((L_fltMAXTP - P_fltTMPVL) < (P_fltTMPVL - L_fltMINTP))
			L_fltCALTP = L_fltMAXTP;
		else
			L_fltCALTP = L_fltMINTP;
        where L_fltMAXTP : Maximum temprature - just above or equal to the recorded temp
			  L_fltMINTP : Minimum temprature - just below the recorded temp
    
        Specific gravity(QP_STDVL) is calculated with QP_NPFVL = L_fltCALTP and qp_qprcd = 'SPG'
        and QP_PRDCD = given material code
	 * 
	 */
	private void getSPGVL(int P_intTBLROW,String P_strTNKNO,String P_strITMCD,float P_fltTMPVL)
	{ 
		float L_fltMAXTP = 0,L_fltMINTP = 0,L_fltCALTP;
		String L_strGRVVL = "";
		try
		{
			// To get the temp. greater than the given temp.
			M_strSQLQRY = "Select min(QP_NPFVL) LM_MAXTP from CO_QPMST where ";
			M_strSQLQRY += " QP_PRDCD = '" + P_strITMCD + "' and ";
			M_strSQLQRY += " QP_QCATP = '11' and QP_TSTTP = '1101' and ";
			M_strSQLQRY += " QP_QPRCD = 'SPG' and ";
			M_strSQLQRY += " QP_NPFVL >= " + P_fltTMPVL;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					L_fltMAXTP = M_rstRSSET.getFloat("LM_MAXTP");
					M_rstRSSET.close();
				}
				M_rstRSSET.close();
			}
			// To get the temp. less than the given temp.
			M_strSQLQRY = "Select max(QP_NPFVL) LM_MINTP from CO_QPMST where ";
			M_strSQLQRY += " QP_PRDCD = '" + P_strITMCD + "' and ";
			M_strSQLQRY += " QP_QCATP = '11' and QP_TSTTP = '1101' and ";
			M_strSQLQRY += " QP_QPRCD = 'SPG' and ";
			M_strSQLQRY += " QP_NPFVL <= " + P_fltTMPVL;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					L_fltMINTP = M_rstRSSET.getFloat("LM_MINTP");
				}
				M_rstRSSET.close();
			}
		
			// Now the uppper and lower temp values are in L_MAXTP & L_MINTP
		
			if(L_fltMAXTP == 0 || L_fltMINTP == 0)
				L_fltCALTP = L_fltMAXTP + L_fltMINTP;
			else if((L_fltMAXTP - P_fltTMPVL) < (P_fltTMPVL - L_fltMINTP))
				L_fltCALTP = L_fltMAXTP;
			else
				L_fltCALTP = L_fltMINTP;
		
			// To get the specifis gravity corresponding to the given item, at given tempreture
			M_strSQLQRY = "Select QP_STDVL from CO_QPMST where ";
			M_strSQLQRY += " QP_PRDCD = '" + P_strITMCD + "' and ";
			M_strSQLQRY += " QP_QCATP = '11' and QP_TSTTP = '1101' and ";
			M_strSQLQRY += " QP_QPRCD = 'SPG' and ";
			M_strSQLQRY += " QP_NPFVL = " + L_fltCALTP;
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				{
					L_strGRVVL = M_rstRSSET.getString("QP_STDVL");
				}
				M_rstRSSET.close();
			}
			tblDIPVL.setValueAt(L_strGRVVL,P_intTBLROW,TB1_SGRVL);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getSPGVL");
		}
		return;
	}
	/** Method to calculate the Day Opening Quantity 
	 * (Tank Volume  + abs(Given Dip - Tank Depth) * Incremental Volume) / 1000  *  Sp.Gravity
	 * */
	private void calMATQT(int P_intTBLROW,String P_strTNKNO,String P_strTDPVL)
	{
		int L_INDEX;
		
		try
		{
			StringTokenizer tknDATA = new StringTokenizer(String.valueOf(hstDIPVL.get(P_strTNKNO)),"|");
			fltDEPCT = Float.parseFloat(tknDATA.nextToken());
			fltDEPVL = Float.parseFloat(tknDATA.nextToken());
			fltINCVL = Float.parseFloat(tknDATA.nextToken());
			//System.out.println("fltDEPCT :"+fltDEPCT);
			//System.out.println("fltDEPVL :"+fltDEPVL);
			//System.out.println("fltINCVL :"+fltINCVL);
			// Item Specific Gravity
			fltMATGR = Float.parseFloat(String.valueOf(tblDIPVL.getValueAt(P_intTBLROW,TB1_SGRVL)));

			// Material Quantity in MT
			strDOPQT = String.valueOf(((fltDEPVL + Math.abs(fltDEPCT - Float.parseFloat(P_strTDPVL))*fltINCVL)/1000) * fltMATGR);
			strUOMCD = String.valueOf(tblDIPVL.getValueAt(P_intTBLROW,TB1_UOMCD));
			
			// Getting Quantity in UOM
			strDOPQT = qtyINUOM(Float.parseFloat(strDOPQT),strUOMCD,fltMATGR);
			//strDOPQT = setFMT(strDOPQT,"000");
			strDOPQT = setNumberFormat(Double.parseDouble(strDOPQT),3);
			tblDIPVL.setValueAt(strDOPQT,P_intTBLROW,TB1_DOPQT);
			
			// Getting the data of the previous day's Memo
			// For Regular Dip only
			if(String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2).equals(strREGDP_fn))
				calDAYCON(P_intTBLROW,P_strTNKNO,strDOPQT);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"calMATQT");
		}
	}
	/** Setting the values of corresponding Tank Depth,Tank Volume 
	  & Incremental Volume 
    */
	private void setVOL(String P_strTNKNO,String P_strDIPVL)
	{
		try
		{
			strTMPSTR = String.valueOf(hstDIPVL.get(P_strTNKNO));
			if(strTMPSTR.equals("null"))
			{
				getVOL(P_strTNKNO,P_strDIPVL);
			}
			// Tank Depth
			strTMPSTR = String.valueOf(hstDIPVL.get(P_strTNKNO));
			intINDEX = strTMPSTR.indexOf("|");
			txtDEPCT.setText(strTMPSTR.substring(0,intINDEX));
										
			// Volume Depth
			strTMPSTR = strTMPSTR.substring(intINDEX+1);
			intINDEX = strTMPSTR.indexOf("|");
			txtDEPVL.setText(strTMPSTR.substring(0,intINDEX));
												
			// Incremental volume
			txtINCVL.setText(strTMPSTR.substring(intINDEX+1));
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"setVOL");
		}
	}
	
	/*Method to get the Opening Quantity of the previous date into the HashTable
	*/
	private void getPRVQT(String P_strDIPDT)
	{
		String L_strTNKNO,L_strDOPQT,L_strRCTQT,L_strTRNQT,L_strDSPQT;

		try
		{
		//	strPRVDT = cc_dattm.getABDATE(LP_DIPDT,1,'B');
			M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strDIPDT));
			M_calLOCAL.add(Calendar.DATE,-1);
			strPRVDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
			strPRVDT = strPRVDT.substring(6) + "-" + strPRVDT.substring(3,5) + "-" + strPRVDT.substring(0,2);
			pstmPRVDIP.setString(1,strREGDP_fn);	
			pstmPRVDIP.setDate(2,java.sql.Date.valueOf(strPRVDT));
			M_rstRSSET = pstmPRVDIP.executeQuery();
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
				{
					flgFIRST = false;
					L_strTNKNO = M_rstRSSET.getString("DP_TNKNO");
					L_strDOPQT = nvlSTRVL(M_rstRSSET.getString("DP_DOPQT"),"0");
					L_strRCTQT = nvlSTRVL(M_rstRSSET.getString("DP_RCTQT"),"0");
					L_strTRNQT = nvlSTRVL(M_rstRSSET.getString("DP_TRNQT"),"0");
					L_strDSPQT = nvlSTRVL(M_rstRSSET.getString("DP_DSPQT"),"0");
									   
					hstPRVQT.put(L_strTNKNO,L_strDOPQT + "|" + L_strRCTQT + "|" + L_strTRNQT + "|" + L_strDSPQT);
				}
				M_rstRSSET.close();
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getPRVQT");
		}
	}
	/**
	 * Method to calculate the daily consumption
	 * @param P_intTBLROW : Row number of selected Row
	 * @param P_strTNKNO  : TAnk number
	 * @param P_strDOPQT  : Day Opening quantity
	 * Consumption is calculated as :
	 * Consumption qty = (Opening qty + Receipt qty + Transfer qty +Dispatch qty)- Closing qty
	 */ 
	private void calDAYCON(int P_intTBLROW,String P_strTNKNO,String P_strDOPQT)
	{
		String L_strCONQT;
		float L_fltDOPQT,L_fltRCTQT,L_fltTRNQT,L_fltDSPQT;
		int L_INDEX;
		
		try
		{
			if(hstPRVQT.containsKey((String)P_strTNKNO))	
				strTMPSTR = String.valueOf(hstPRVQT.get(P_strTNKNO)).trim();
			else	
			{
				getPRVQT(txtMEMDT.getText().trim());
				if(!hstPRVQT.containsKey((String)P_strTNKNO))	
				{
					flgFIRST = true;
					hstPRVQT.put(P_strTNKNO,"0|0|0|0");
					strTMPSTR = String.valueOf(hstPRVQT.get(P_strTNKNO)).trim();
				}
			}
			if(strTMPSTR.length() >0)
			{
				// Opening Quantity
				L_INDEX = strTMPSTR.indexOf("|");
				L_fltDOPQT = Float.parseFloat(strTMPSTR.substring(0,L_INDEX));
				strTMPSTR = strTMPSTR.substring(L_INDEX+1);
			
				// Receipt Quantity
				L_INDEX = strTMPSTR.indexOf("|");
				L_fltRCTQT = Float.parseFloat(strTMPSTR.substring(0,L_INDEX));
				strTMPSTR = strTMPSTR.substring(L_INDEX+1);
			
				// Transfer Quantity
				L_INDEX = strTMPSTR.indexOf("|");
				L_fltTRNQT = Float.parseFloat(strTMPSTR.substring(0,L_INDEX));
			
				// Despatch Quantity
				L_fltDSPQT = Float.parseFloat(strTMPSTR.substring(L_INDEX+1));
				//L_CONQT = String.valueOf(Float.parseFloat(LP_MOPQT) - (L_MOPQT + L_RCTQT + L_TRNQT + L_DSPQT));
				L_strCONQT = String.valueOf((L_fltDOPQT + L_fltRCTQT + L_fltTRNQT + L_fltDSPQT) - Float.parseFloat(P_strDOPQT));
				tblDIPVL.setValueAt(setNumberFormat(Double.parseDouble(L_strCONQT),3),P_intTBLROW,TB1_CONQT);
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"calDAYCON");
		}
	}
	/** Method to get the Tanker Details from WeighBridge Table 
	 *  @param P_strCURDT : Memo Date is passed
	 *  In case of Addition, those tankers are fetched where Date(WB_OUTTM) is between
	 *	given date and previos day's date and WB_ACPTG = 'W' (Waiting Tankers)
	 *  For other options WB_ACPTG ='A' and Date(WB_ACPDT) = Memo Date
	 * */
	private void getWBRVL(String P_strCURDT)
	{
		try
		{
			int L_intRECCT = 0;
			Boolean L_flgCHKFL;
			String L_strOLDFL = "";
			fltTNETWT = 0;
			java.sql.Date L_datTEMP;
			M_strSQLQRY = "Select WB_TNKNO,WB_DOCNO,WB_TPRDS,WB_LRYNO,WB_CHLNO,WB_CHLDT,";
			M_strSQLQRY += "WB_CHLQT,WB_UOMQT,WB_BOENO";
			M_strSQLQRY += " from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strDOCTP_fn + "'";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strCURDT));
				M_calLOCAL.add(Calendar.DATE,-1);
				String LM_TEMP = M_fmtLCDAT.format(M_calLOCAL.getTime());
				LM_TEMP = M_fmtDBDAT.format(M_fmtLCDAT.parse(LM_TEMP));
				M_strSQLQRY += " and CONVERT(varchar,WB_OUTTM,103) between '" + LM_TEMP+"'";
				M_strSQLQRY += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strCURDT))+"'";
				M_strSQLQRY += " and isnull(WB_ACPTG,'') <> 'A'";
				L_flgCHKFL = new Boolean(false);
				L_strOLDFL = "";
			}
			else
			{
				M_strSQLQRY += " and CONVERT(varchar,WB_ACPDT,103) = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strCURDT))+"'";
				M_strSQLQRY += " and isnull(WB_ACPTG,'') = 'A'";
				L_flgCHKFL = new Boolean(true);
				L_strOLDFL = "O";
			}
			M_strSQLQRY += " order by WB_TNKNO,WB_DOCNO,WB_TPRDS";
			
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET1 !=null)
			while(M_rstRSSET1.next())
			{
				strTNKNO = M_rstRSSET1.getString("WB_TNKNO");
				strGINNO = M_rstRSSET1.getString("WB_DOCNO");
				strTPRDS = M_rstRSSET1.getString("WB_TPRDS");
				strLRYNO = M_rstRSSET1.getString("WB_LRYNO");
				strCHLNO = M_rstRSSET1.getString("WB_CHLNO");
				//strCHLDT = cc_dattm.setDATFMT("DMY",M_rstRSSET1.getString("WB_CHLDT"));
				L_datTEMP =M_rstRSSET1.getDate("WB_CHLDT");
				if(L_datTEMP !=null)
				{
					strCHLDT = M_fmtDBDAT.format(L_datTEMP);
				}
				strCHLQT = nvlSTRVL(M_rstRSSET1.getString("WB_CHLQT"),"0");
				strUOMQT = nvlSTRVL(M_rstRSSET1.getString("WB_UOMQT"),"0");
				fltTNETWT += Float.parseFloat(strUOMQT);
				strBOENO = nvlSTRVL(M_rstRSSET1.getString("WB_BOENO"),"");
				tblWBRVL.setValueAt(L_flgCHKFL,L_intRECCT,TB2_CHKFL);
				tblWBRVL.setValueAt(strTNKNO,L_intRECCT,TB2_TNKNO);
				tblWBRVL.setValueAt(strGINNO,L_intRECCT,TB2_GINNO);
				tblWBRVL.setValueAt(strTPRDS,L_intRECCT,TB2_TPRDS);
				tblWBRVL.setValueAt(strLRYNO,L_intRECCT,TB2_LRYNO);
				tblWBRVL.setValueAt(strCHLNO,L_intRECCT,TB2_CHLNO);
				tblWBRVL.setValueAt(strCHLDT,L_intRECCT,TB2_CHLDT);
				tblWBRVL.setValueAt(strCHLQT,L_intRECCT,TB2_CHLQT);
				tblWBRVL.setValueAt(strUOMQT,L_intRECCT,TB2_UOMQT);
				tblWBRVL.setValueAt(strBOENO,L_intRECCT,TB2_BOENO);
				tblWBRVL.setValueAt(L_strOLDFL,L_intRECCT,TB2_OLDFL);
				L_intRECCT++;
			}
			
			lblRCTLR.setText(String.valueOf(L_intRECCT));
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
			{
				//java.sql.Date L_datTEMP = new java.sql.Date();
				M_calLOCAL.setTime(M_fmtLCDAT.parse(P_strCURDT));
				M_calLOCAL.add(Calendar.DATE,-1);
				String LM_TEMP = M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_calLOCAL.getTime())));
				M_strSQLQRY = "Select WB_TNKNO,WB_DOCNO,WB_TPRDS,WB_LRYNO,WB_CHLNO,WB_CHLDT,";
				M_strSQLQRY += "WB_CHLQT,WB_UOMQT,WB_BOENO";
				M_strSQLQRY += " from MM_WBTRN";
				M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strDOCTP_fn + "'";
				//M_strSQLQRY += " and Date(WB_OUTTM) between " + cc_dattm.setDBSDT(cc_dattm.getABDATE(LP_CURDT,1,'B'));
				M_strSQLQRY += " and CONVERT(varchar,WB_OUTTM,103) between '" + LM_TEMP +"'";
				M_strSQLQRY += " and '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(P_strCURDT))+"'";
				M_strSQLQRY += " and isnull(WB_ACPTG,'') <> 'A'";
				M_strSQLQRY += " order by WB_TNKNO,WB_DOCNO,WB_TPRDS";
				M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET1 !=null)
				while(M_rstRSSET1.next())
				{
					strTNKNO = M_rstRSSET1.getString("WB_TNKNO");
					strGINNO = M_rstRSSET1.getString("WB_DOCNO");
					strTPRDS = M_rstRSSET1.getString("WB_TPRDS");
					strLRYNO = M_rstRSSET1.getString("WB_LRYNO");
					strCHLNO = M_rstRSSET1.getString("WB_CHLNO");
					//strCHLDT = cc_dattm.setDATFMT("DMY",M_rstRSSET1.getString("WB_CHLDT"));
					L_datTEMP = M_rstRSSET1.getDate("WB_CHLDT");
					if(L_datTEMP !=null)
					{
						strCHLDT = M_fmtDBDAT.format(L_datTEMP);
					}
					strCHLQT = nvlSTRVL(M_rstRSSET1.getString("WB_CHLQT"),"0");
					strUOMQT = nvlSTRVL(M_rstRSSET1.getString("WB_UOMQT"),"0");
					//fltTNETWT += Float.parseFloat(strUOMQT);
					strBOENO = nvlSTRVL(M_rstRSSET1.getString("WB_BOENO"),"");
					tblWBRVL.setValueAt(strTNKNO,L_intRECCT,TB2_TNKNO);
					tblWBRVL.setValueAt(strGINNO,L_intRECCT,TB2_GINNO);
					tblWBRVL.setValueAt(strTPRDS,L_intRECCT,TB2_TPRDS);
					tblWBRVL.setValueAt(strLRYNO,L_intRECCT,TB2_LRYNO);
					tblWBRVL.setValueAt(strCHLNO,L_intRECCT,TB2_CHLNO);
					tblWBRVL.setValueAt(strCHLDT,L_intRECCT,TB2_CHLDT);
					tblWBRVL.setValueAt(strCHLQT,L_intRECCT,TB2_CHLQT);
					tblWBRVL.setValueAt(strUOMQT,L_intRECCT,TB2_UOMQT);
					tblWBRVL.setValueAt(strBOENO,L_intRECCT,TB2_BOENO);
					L_intRECCT++;
				}
			}
			lblGINLR.setText(String.valueOf(L_intRECCT));
			lblRCTQT.setText(setNumberFormat(fltTNETWT,3));
			intROWCT1 = L_intRECCT;
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getWBRVL");
		}
	}

	/** Method to create the Prepared Statements 
	 *  pstmDIPREG : Statement to get the Record from Dip Register Table for a given Memo number
	 *  pstmTNKNO  : Statement to get the Tank Nos.
	 *	pstmUPDRCT : Statement to update Receipts
		
	 * */
	private void crtPRESTM()throws Exception
	{
		pstmDIPREG = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"Select DP_MEMTP,DP_MEMNO,DP_MEMDT,DP_TNKNO,DP_DIPVL,DP_TMPVL,DP_RCTQT," +
					"DP_DOPQT,DP_MATCD,CT_MATDS,CT_UOMCD,DP_DENVL,CMT_CODDS,TK_MNDVL,TK_MXDVL " +
					" from MM_DPTRN,CO_CTMST,MM_TKMST,CO_CDTRN where isnull(CT_STSFL,' ') <>'X' and DP_MEMTP = ? and DP_MEMNO = ?" +
					" and CT_MATCD = DP_MATCD and DP_TNKNO = TK_TNKNO and DP_CMPCD=TK_CMPCD " +
					" and CMT_CGMTP = 'SYS' and DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP = 'MMXXTNK' " +
					" and CMT_CODCD = TK_TNKTP order by DP_MATCD,DP_TNKNO" 
					);
		// Statement to get the Tank Nos.
		pstmTNKNO = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"Select TK_TNKNO,TK_MNDVL,TK_MXDVL,TK_MATCD,CT_MATDS," + 
					"CT_UOMCD from MM_TKMST,CO_CTMST" +
					" where TK_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND TK_STSFL <> 'X'" +
					" and isnull(CT_STSFL,' ') <>'X' and CT_MATCD = TK_MATCD order by TK_MATCD,TK_TNKNO"
					);
		// Statement to update Receipts
		/*pstmUPDRCT = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"Update MM_WBTRN set WB_TNKNO = ?,WB_CHLQT = ?,WB_UOMQT = ?," +
					"WB_ACPTG = ?,WB_ACPDT = ?,WB_TRNFL = ?,WB_LUSBY = ?,WB_LUPDT = ?" +
					" where WB_DOCTP = '" + strDOCTP_fn + "' and WB_DOCNO = ?"
					);*/
		// Statement to update Dip Register
		pstmUPDDIP = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"Update MM_DPTRN set DP_RCTQT = DP_RCTQT + ?,DP_TRNFL = ?," +
					"DP_LUSBY = ?,DP_LUPDT = ?" +
					" where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP_fn + "' and CONVERT(varchar,DP_MEMDT,103) = ?" +
					" and DP_TNKNO = ?"
					);
		// Statement to get the Previous Day's Dip
		pstmPRVDIP = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"Select DP_MEMNO,DP_TNKNO,DP_DOPQT,DP_RCTQT,DP_TRNQT,DP_DSPQT" +
					" from MM_DPTRN where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = ? and " +
					"DP_STSFL <> 'X' and CONVERT(varchar,DP_MEMDT,103) = ?"
					);
	}
	/**
	 * Method to get the details of Waiting Tankers from MM_WBTRN WHERE WB_ACPTG ='W'
	 */
	private void getWTTNK()
	{
		try
		{
			setMatrix(20,8);
			int L_intRECCT = 0;
			Boolean L_flgCHKFL;
			fltTNETWT = 0;
			JPanel pnlWTTNK = new JPanel();
			JPanel pnlBTN = new JPanel();
			JTable tblWTTNK = new JTable();
			java.sql.Date L_datTEMP;// = new java.sql.Date();
			java.sql.Timestamp L_tmsTEMP;// = new java.sql.Timestamp();
			wndDLG = new JDialog();
			wndDLG.setLocation(10,10);
			String[] L_strCOLHD = new String[]{"","Truck No.","Transporter","Gate In Date","Chalan No.","Chalan Date","Chalan Qty","Net Weight","Tank No."};
			int[] L_intCOLSZ = new int[]{20,100,120,100,60,70,70,60,70};
			tblWTTNK = crtTBLPNL1(pnlWTTNK,L_strCOLHD,100,3,1,4,7.9,L_intCOLSZ,new int[]{0});
			M_strSQLQRY = "Select WB_TNKNO,WB_DOCNO,WB_TPRDS,WB_GINDT,WB_LRYNO,WB_CHLNO,WB_CHLDT,";
			M_strSQLQRY += "WB_CHLQT,WB_UOMQT,WB_BOENO";
			M_strSQLQRY += " from MM_WBTRN";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strDOCTP_fn + "'";
			M_strSQLQRY += " and isnull(WB_ACPTG,'') <> 'A'";
			M_strSQLQRY += " and isnull(WB_STSFL,' ') <> 'X'";
			M_strSQLQRY += " order by WB_TNKNO,WB_DOCNO,WB_TPRDS";
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			while(M_rstRSSET1.next())
			{
				strTNKNO = M_rstRSSET1.getString("WB_TNKNO");
				strGINNO = M_rstRSSET1.getString("WB_DOCNO");
				strTPRDS = M_rstRSSET1.getString("WB_TPRDS");
				strLRYNO = M_rstRSSET1.getString("WB_LRYNO");
				strCHLNO = M_rstRSSET1.getString("WB_CHLNO");
				L_datTEMP =M_rstRSSET1.getDate("WB_CHLDT");
				if(L_datTEMP !=null)
				{
					strCHLDT = M_fmtDBDAT.format(L_datTEMP);
				}
				strCHLQT = nvlSTRVL(M_rstRSSET1.getString("WB_CHLQT"),"0");
				strUOMQT = nvlSTRVL(M_rstRSSET1.getString("WB_UOMQT"),"0");
				fltTNETWT += Float.parseFloat(strUOMQT);
				strBOENO = nvlSTRVL(M_rstRSSET1.getString("WB_BOENO"),"");
				
				tblWTTNK.setValueAt(strLRYNO,L_intRECCT,1);
				tblWTTNK.setValueAt(strTPRDS,L_intRECCT,2);
				L_tmsTEMP =M_rstRSSET1.getTimestamp("WB_GINDT");
				if(L_tmsTEMP !=null)
				{
					tblWTTNK.setValueAt(M_fmtDBDTM.format(L_tmsTEMP),L_intRECCT,3);
				}
				tblWTTNK.setValueAt(strCHLNO,L_intRECCT,4);
				tblWTTNK.setValueAt(strCHLDT,L_intRECCT,5);
				tblWTTNK.setValueAt(strCHLQT,L_intRECCT,6);
				tblWTTNK.setValueAt(strUOMQT,L_intRECCT,7);
				tblWTTNK.setValueAt(strTNKNO,L_intRECCT,8);
			
				L_intRECCT++;
			} 
			if(M_rstRSSET1 != null)
				M_rstRSSET1.close();
			pnlWTTNK.setLocation(10,10);
			pnlWTTNK.setVisible(true);
			btnCLS = new JButton("Exit");//crtBTN(pnlBTN,"Exit",10,10,80,20);
			btnCLS.addActionListener(this);
			pnlWTTNK.add(btnCLS);
			wndDLG.getContentPane().add(pnlWTTNK);
			wndDLG.setBounds(30,115,730,310);
			wndDLG.toFront();
			wndDLG.setTitle("Waiting Tankers");
			//wndDLG.show();  deprecated in 1.6
			wndDLG.setVisible(true);			
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"get Wainting Tankers");
		}
	}
	/**
	 * For saving the Data, In Addition Details are insrted in Dip TAble and Remarks table.
	 * Day closing quantity is updated for previous day.
	 */
	void exeSAVE()
	{
		try
		{
			if(!vldDATA())
				return;
			this.setCursor(cl_dat.M_curWTSTS_pbst);
			cl_dat.M_flgLCUPD_pbst = true;
			strMEMTP = String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2);
			strMEMNO = txtMEMNO.getText().trim();
			strMEMTM = txtMEMDT.getText().trim()+" "+txtMEMTM.getText().trim();
			M_calLOCAL.setTime(M_fmtLCDAT.parse(strMEMTM.substring(0,10)));
			M_calLOCAL.add(Calendar.DATE,-1);
			strPRVDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(M_fmtLCDAT.format(M_calLOCAL.getTime())));
			strMEMTM = M_fmtDBDTM.format(M_fmtLCDTM.parse(strMEMTM));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				genMEMNO(strMEMTP);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
			{ 
				M_strSQLQRY = "Update MM_DPTRN set ";
				M_strSQLQRY += getUSGDTL("DP",'U',"X");
				M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + cmbMEMTP.getSelectedItem().toString().substring(0,2) + "'";
				M_strSQLQRY += " and DP_MEMNO = '" + txtMEMNO.getText().trim() + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(flgREMDS)
				{												// To update existing record
					M_strSQLQRY = "Update MM_RMMST set ";
					M_strSQLQRY += getUSGDTL("RM",'U',"X");
					M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + strSTRTP_fn + "'";
					M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
					M_strSQLQRY += " and RM_DOCTP = '" + strMEMTP + "'";
					M_strSQLQRY += " and RM_DOCNO = '" + strMEMNO + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			else
			{	
				// To get the data from JTable
				for(int i=0;i<intROWCT;i++)
				{
					strTTKNO = String.valueOf(tblDIPVL.getValueAt(i,TB1_TNKNO));
					strMATCD = String.valueOf(tblDIPVL.getValueAt(i,TB1_MATCD));
					strTDPVL = String.valueOf(tblDIPVL.getValueAt(i,TB1_DIPVL));
					strTTPVL = String.valueOf(tblDIPVL.getValueAt(i,TB1_TMPVL));	
					strSGRVL = String.valueOf(tblDIPVL.getValueAt(i,TB1_SGRVL));
					strDOPQT = String.valueOf(tblDIPVL.getValueAt(i,TB1_DOPQT));	
					
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
					{
						M_strSQLQRY = "Insert into MM_DPTRN(DP_CMPCD,DP_MEMTP,DP_MEMNO,DP_MEMDT,";
						M_strSQLQRY += "DP_TNKNO,DP_MATCD,DP_DENVL,DP_DIPVL,DP_TMPVL,";
						M_strSQLQRY += "DP_DOPQT,DP_TRNFL,DP_STSFL,DP_LUSBY,DP_LUPDT) values (";
						M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
						M_strSQLQRY += "'" + strMEMTP + "',";
						M_strSQLQRY += "'" + txtMEMNO.getText().trim() + "',";
						M_strSQLQRY += "'" + strMEMTM + "',";
						M_strSQLQRY += "'" + strTTKNO + "',";
						M_strSQLQRY += "'" + strMATCD + "',";
						M_strSQLQRY += strSGRVL + ",";
						M_strSQLQRY += strTDPVL + ",";
						M_strSQLQRY += strTTPVL + ",";
						M_strSQLQRY += strDOPQT + ",";
						M_strSQLQRY += getUSGDTL("DP",'I',"")+")";
					}	
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
					{
						M_strSQLQRY = "Update MM_DPTRN set ";
						M_strSQLQRY += "DP_MEMDT = '" + strMEMTM + "',";
						M_strSQLQRY += "DP_DIPVL = " + strTDPVL + ",";
						M_strSQLQRY += "DP_TMPVL = " + strTTPVL + ",";
						M_strSQLQRY += "DP_DENVL = " + strSGRVL + ",";
						M_strSQLQRY += "DP_DOPQT = " + strDOPQT + ",";
						M_strSQLQRY += getUSGDTL("DP",'U',"");
						M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strMEMTP + "'";
						M_strSQLQRY += " and DP_MEMNO = '" + strMEMNO + "'";
						M_strSQLQRY += " and DP_TNKNO = '" + strTTKNO + "'";
					}
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(!cl_dat.M_flgLCUPD_pbst)
						break;
					
					// To update the Closing Quantity of the previous record
					if(!flgFIRST && strMEMTP.equals(strREGDP_fn))
					{
						M_strSQLQRY = "Update MM_DPTRN set ";
						M_strSQLQRY += " DP_DCLQT = " + strDOPQT + ",";
						M_strSQLQRY += " DP_TRNFL = '" + strTRNFL_fn + "'";
						M_strSQLQRY += " where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP_fn + "'";
						M_strSQLQRY += " and DP_TNKNO = '" + strTTKNO + "'";
						M_strSQLQRY += " and CONVERT(varchar,DP_MEMDT,103) = '" + strPRVDT+"'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						if(!cl_dat.M_flgLCUPD_pbst)
							break;
					}
				}
				if(cl_dat.M_flgLCUPD_pbst)
				{
					// To update WeighBridge Transactions (Receipt Updation),
					// commented on 15/03/2005, API HBP
					// updRECPT();
					strREMDS = txtREMDS.getText().trim();
					if(flgREMDS)
					{												
						M_strSQLQRY = "Update MM_RMMST set ";
						M_strSQLQRY += "RM_REMDS = '" + strREMDS + "',";
						M_strSQLQRY += getUSGDTL("RM",'U',"");
						M_strSQLQRY += " where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_STRTP = '" + strSTRTP_fn + "'";
						M_strSQLQRY += " and RM_TRNTP = '" + strTRNTP_fn + "'";
						M_strSQLQRY += " and RM_DOCTP = '" + strMEMTP + "'";
						M_strSQLQRY += " and RM_DOCNO = '" + strMEMNO + "'";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
					else if(strREMDS.length() > 0)
					{								
						M_strSQLQRY = "Insert into MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,";
						M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,";
						M_strSQLQRY += "RM_LUPDT) values (";
						M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "',";
						M_strSQLQRY += "'" + strSTRTP_fn + "',";
						M_strSQLQRY += "'" + strTRNTP_fn + "',";
						M_strSQLQRY += "'" + strMEMTP + "',";
						M_strSQLQRY += "'" + strMEMNO + "',";
						M_strSQLQRY += "'" + strREMDS + "',";
						M_strSQLQRY += getUSGDTL("RM",'I',"")+")";
						cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					}
				}				
				
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))		
			{
				if(cl_dat.M_flgLCUPD_pbst)
				{
					M_strSQLQRY = "Update CO_CDTRN set ";
					M_strSQLQRY += " CMT_CCSVL = '" + txtMEMNO.getText().trim().substring(3) + "',";
					M_strSQLQRY += getUSGDTL("CMT",'U',"");
					M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
					M_strSQLQRY += " and CMT_CGSTP = 'MMXXDIP'";
					M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strMEMTP + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				}
			}
			
			if(cl_dat.M_flgLCUPD_pbst)
			{
				strPMEMTP = strMEMTP;
				strPMEMNO = strMEMNO;
				flgREMDS = false;
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					btnPRINT.setEnabled(true);
					flgREMDS = false;
					clrCOMP();
					setMSG("Data Saved Successfully..",'N');
				}
				else
				{
					setMSG("Error in Saving Data..",'E');
				}
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			this.setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				flgVLDDT = true;
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input == txtMEMDT)
				{
					if(M_fmtLCDAT.parse(txtMEMDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{							
						setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
						flgVLDDT = false;
						return false;
					}
				}
				if(input == txtMEMTM)
				{
					strMEMDT = txtMEMDT.getText().trim()+" "+txtMEMTM.getText().trim();
					strCURDT = cl_dat.M_strLOGDT_pbst+" "+cl_dat.M_txtCLKTM_pbst.getText();
					if(String.valueOf(cmbMEMTP.getSelectedItem()).substring(0,2).equals(strREGDP_fn))
					{
						strCURDT = strCURDT.substring(0,10) + " 06:00";
						if(M_fmtLCDTM.parse(strMEMDT).compareTo(M_fmtLCDTM.parse(strCURDT))!=0)
						{							
							setMSG("Memo Date should not exceed current Date & time should be 06:00 only",'E');
							flgVLDDT = false;
							return false;
						}
						M_strSQLQRY = " Select DP_MEMNO from MM_DPTRN where DP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND DP_MEMTP = '" + strREGDP_fn + "' and ";
						M_strSQLQRY += " CONVERT(varchar,DP_MEMDT,103) = '" + M_fmtDBDAT.format(M_fmtLCDAT.parse(txtMEMDT.getText().trim())) + "'";
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET ==null)
						{
							setMSG("Dip Record has been already noted for this day.",'E');
							hstPRVQT.clear();
							flgVLDDT = false;
							return false;
						}
						if(M_rstRSSET.next())
						{
							M_rstRSSET.close();
							setMSG("Dip Record has been already noted for this day.",'E');
							hstPRVQT.clear();
							flgVLDDT = false;
							return false;
						}
					}
					else
					{
						if(M_fmtLCDTM.parse(strMEMDT).compareTo(M_fmtLCDTM.parse(strCURDT))> 0)
						{							
							setMSG("Memo Date time can not be greater than current Date & time..",'E');
							return false;
						}
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;	
		}
	}
		
	/**Method to Update the Receipts 
	 * Commented on 15/03/2005 API HBP
	 * */
	/*private void updRECPT()
	{
		try
		{
			String L_strLUPDT = cl_dat.M_strLOGDT_pbst;
			L_strLUPDT = L_strLUPDT.substring(6) + "-" + L_strLUPDT.substring(3,5) + "-" + L_strLUPDT.substring(0,2);
			
			String L_strACPDT = txtMEMDT.getText().trim();
			L_strACPDT = L_strACPDT.substring(6,10) + "-" + L_strACPDT.substring(3,5) + "-" + L_strACPDT.substring(0,2) + L_strACPDT.substring(10) + ":00.000000000";
			
			String L_strMEMDT = txtMEMDT.getText().trim()+" "+txtMEMTM.getText().trim();
			L_strMEMDT = L_strMEMDT.substring(6,10) + "-" + L_strMEMDT.substring(3,5) + "-" + L_strMEMDT.substring(0,2);
			
			int L_intROWCT = 0;
			for(int i =0;i<intROWCT1;i++)
			{
				strCHKFL = tblWBRVL.getValueAt(i,TB2_CHKFL).toString();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))		
				{
					if(strCHKFL.equals("true"))
						strACPTG = "A";
					else
						strACPTG = "W";
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
					strACPTG = "W";
				else
				{
					if(strCHKFL.equals("true"))
						strACPTG = "A";
					else
						continue;
				}
				strOLDFL = tblWBRVL.getValueAt(i,TB2_OLDFL).toString().trim();
				strGINNO = tblWBRVL.getValueAt(i,TB2_GINNO).toString().trim();
				strTNKNO = tblWBRVL.getValueAt(i,TB2_TNKNO).toString().trim();
				strCHLQT = tblWBRVL.getValueAt(i,TB2_CHLQT).toString().trim();
				strUOMQT = tblWBRVL.getValueAt(i,TB2_UOMQT).toString().trim();
					
				pstmUPDRCT.setString(1,strTNKNO);
				pstmUPDRCT.setFloat(2,Float.parseFloat(strCHLQT));
				pstmUPDRCT.setFloat(3,Float.parseFloat(strUOMQT));
				pstmUPDRCT.setString(4,strACPTG);
				pstmUPDRCT.setTimestamp(5,Timestamp.valueOf(L_strACPDT));
				pstmUPDRCT.setString(6,strTRNFL_fn);
				pstmUPDRCT.setString(7,strLUSBY);
				pstmUPDRCT.setDate(8,java.sql.Date.valueOf(L_strLUPDT));
				pstmUPDRCT.setString(9,strGINNO);
				pstmUPDRCT.addBatch();
				
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))		
				{
					if(strOLDFL.equals("O"))
					{
						if(strCHKFL.equals("true"))
							pstmUPDDIP.setFloat(1,0);
						else
							pstmUPDDIP.setFloat(1,Float.parseFloat(strUOMQT)*(-1));
					}
					else
					{
						if(strCHKFL.equals("true"))
							pstmUPDDIP.setFloat(1,Float.parseFloat(strUOMQT));
						else
							pstmUPDDIP.setFloat(1,0);
					}
					pstmUPDDIP.setString(2,strTRNFL_fn);
					pstmUPDDIP.setString(3,strLUSBY);
					pstmUPDDIP.setDate(4,java.sql.Date.valueOf(L_strLUPDT));
					pstmUPDDIP.setDate(5,java.sql.Date.valueOf(L_strMEMDT));
					pstmUPDDIP.setString(6,strTNKNO);
					pstmUPDDIP.addBatch();
				}
				L_intROWCT++;
			}
			if(L_intROWCT > 0)
			{
				int rows1[] = pstmUPDRCT.executeBatch();
				int rows2[] = pstmUPDDIP.executeBatch();
				pstmUPDRCT.clearParameters();
				pstmUPDDIP.clearParameters();
				if(rows1.length == L_intROWCT && rows2.length == L_intROWCT)
					cl_dat.M_flgLCUPD_pbst = true;
				else
					cl_dat.M_flgLCUPD_pbst = false;
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in updRECPT : " + e.toString());
		}
	}*/
	
}