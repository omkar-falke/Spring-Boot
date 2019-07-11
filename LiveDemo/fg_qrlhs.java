/*
System Name   : Finished Goods Inventory Management System
Program Name  : Lot History
Program Desc. : Gives detail regarding
					a) Lot Details
					b) Q.C.details	
					c) Storage Details
					d) Despatch Details
Author        : Mr S.R. Mehesare
Date          : 18/02/2006
Version       : MMS v2.0.0
*/

/**
<b>Program Name :</b> Lot History Query Program.

<b>Purpose :</b> This Program can give History of Lot Details like Quality Testing,
Lot Material Despatches & so on.

List of tables used :
Table Name      Primary key                      Operation done
                                            Insert	Update	Query	Delete	
----------------------------------------------------------------------------------------------------------
FG_RCTRN        RCT_RCTTP,RCT_RCTNO                           #
QC_PSMST        PS_TSTTP                                      #
PR_LTMST        LT_PRDTP,LT_LOTNO                             #
MR_IVTRN        IVT_PKGTP,IOVT_PRDCD                          #
----------------------------------------------------------------------------------------------------------
List of  fields accepted/displayed on screen :
Field Name	Column Name		  Table name		    Type/Size       Description
----------------------------------------------------------------------------------------------------------
txtLOTNO        LT_LOTNO      PR_LTMST               varchar(8)     Lot Number
tblTSTDL       all column    QC_PSMST
tblLOTDL       all column    PR_LTMST
tblSTKDL       all column    FG_RCTRN
tblDSPDL       all column    MR_IVTRN                           
----------------------------------------------------------------------------------------------------------

List of fields with help facility : 
Field Name  Display Description     Display Columns       Table Name
----------------------------------------------------------------------------------------------------------
txtLOTNO     Lot number             LT_LOTNO              PR_LTMST
----------------------------------------------------------------------------------------------------------
*/
import java.sql.ResultSet;
import javax.swing.JRadioButton;import javax.swing.ButtonGroup;
import javax.swing.JTabbedPane;import javax.swing.JPanel;import java.awt.Color;
import javax.swing.JTable;import java.util.Hashtable;import javax.swing.JComboBox;
import javax.swing.JTextField;import javax.swing.JLabel;import javax.swing.JComponent;
import java.awt.event.FocusEvent;import java.io.DataOutputStream;import java.util.Date;
import java.awt.event.ActionEvent;import java.awt.event.KeyEvent;import java.util.Vector;
import java.util.Hashtable;import java.awt.event.MouseEvent;import java.io.FileOutputStream;

class fg_qrlhs extends cl_rbase
{
									/** JTextField to enter & to display the Lot Number. */
	private JTextField txtLOTNO;	/** JTabbed pane for tables */
	private	JTabbedPane jtpMANTB;	/** JTable to display records of Lot details table */
	private	cl_JTable tblLOTDL;		/** JTable to display records of testing table*/
	private	cl_JTable tblTSTDL;     /** JTable to display records of Finished Goods of inventory tables */
	private	cl_JTable tblSTKDL;     /** JTable to display records of Despatching Details */
	private	cl_JTable tblDSPDL;		/** JPanel for displaying Tables */
	private	JPanel pnlLOTDL;
	private	JPanel pnlTSTDL;
	private	JPanel pnlSTKDL;
	private	JPanel pnlDSPDL;
	                                /** Labels for displaying Information on screen */
	private JLabel lblPSTDT;
	private JLabel lblPENDT;
	private JLabel lblLINNO;
	private JLabel lblCYLNO;
	private JLabel lblBAGQT;
	private JLabel lblTPRCD;
	private JLabel lblCLSQT;
	private JLabel lblUCLQT;
	private JLabel lblTDSQT;
	private JLabel lblSLRQT;	      
		
	private	String strLOTNO;
	private String strDBPTP;
	private String strTSTTP;
	private int intROWCT = 25;
	
	private final int TB1_CHKFL = 0;
	private final int TB1_RCLNO = 1;
	private final int TB1_PRDTP = 2; 
	private final int TB1_PRDDS = 3;	
	private final int TB1_CLSFL = 4;
	private final int TB1_CLSTM = 5;
                                      
	private final int TB2_CHKFL = 0;
	private final int TB2_RCLNO = 1;
	private final int TB2_TSTTP = 2;
	private final int TB2_TSTDT = 3;
	private final int TB2_DSPVL = 4;
	private final int TB2_MFIVL = 5;
	private final int TB2_IZOVL = 6;
	private final int TB2_VICVL = 7;
	private final int TB2_TS_VL = 8;
	private final int TB2_EL_VL = 9;
	private final int TB2_RSMVL = 10;
	private final int TB2_WI_VL = 11;
	private final int TB2_A__VL = 12;
	private final int TB2_B__VL = 13;
	private final int TB2_Y1_VL = 14;
	                                
	private final int TB3_CHKFL = 0;
	private final int TB3_RCLNO = 1;
	private final int TB3_TRNTP = 2;
	private final int TB3_TRNDT = 3;
	private final int TB3_MNLCD = 4;
	private final int TB3_PKGTP = 5;
	private final int TB3_TRNQT = 6;
	

	private final int TB4_CHKFL = 0;
	private final int TB4_INVNO = 1;
	private final int TB4_INVDT = 2;
	private final int TB4_BUYER = 3;
	private final int TB4_TRASP = 4;
	private final int TB4_LRYNO = 5;
	private final int TB4_INDNO = 6;
	private final int TB4_DONO  = 7;
	private final int TB4_PRDDS = 8;
	private final int TB4_DSPQT = 9;
	
	private double intDSTQT = 0,intDUCQT = 0;		/** Hashtable for getting value of Package type */
	private Hashtable<String,String> hstPKGTP = new Hashtable<String,String>();   /** Hashtable for getting value of Record type */
	private Hashtable<String,String> hstRTPTP = new Hashtable<String,String>();   /** Hashtable for getting value of Inventory type */
	private Hashtable<String,String> hstITPTP = new Hashtable<String,String>();
	private Hashtable<String,String> hstITMDS = new Hashtable<String,String>();
	private boolean flgFIRST = true;
	fg_qrlhs()
	{
		super(2);
    	try
		{
			//System.out.println("Changed");
			setMatrix(20,8);
			M_vtrSCCOMP.remove(M_lblFMDAT);
			M_vtrSCCOMP.remove(M_lblTODAT);
			M_vtrSCCOMP.remove(M_txtTODAT);
			M_vtrSCCOMP.remove(M_txtFMDAT);
						
			pnlLOTDL = new JPanel();
			pnlLOTDL.setLayout(null);
			pnlTSTDL = new JPanel();
			pnlTSTDL.setLayout(null);
			pnlSTKDL = new JPanel();
			pnlSTKDL.setLayout(null);
			pnlDSPDL = new JPanel();
			pnlDSPDL.setLayout(null);

			add(new JLabel("Lot No"),1,1,1,.7,this,'R');
			add(txtLOTNO = new TxtLimit(8),1,2,1,1,this,'L');
			
			add(new JLabel("Start Date"),2,1,1,.7,this,'R');
			add(lblPSTDT = new JLabel(""),2,2,1,2,this,'L');
			add(new JLabel("End Date"),2,3,1,.8,this,'R');
			add(lblPENDT = new JLabel(""),2,4,1,2,this,'L');
			add(new JLabel("Line No."),2,5,1,.8,this,'R');			
			add(lblLINNO = new JLabel(""),2,6,1,1,this,'L');
			
			add(new JLabel("Silo No."),3,1,1,.7,this,'R');
			add(lblCYLNO = new JLabel(""),3,2,1,1,this,'L');
			add(new JLabel("Bagged Qty."),3,3,1,.8,this,'R');
			add(lblBAGQT = new JLabel(""),3,4,1,1,this,'L');
			add(new JLabel("Trgt. Grade"),3,5,1,.8,this,'R');
			add(lblTPRCD = new JLabel(""),3,6,1,1,this,'L');
			
			add(new JLabel("Clsfd. Qty"),4,1,1,.7,this,'R');
			add(lblCLSQT = new JLabel(""),4,2,1,1,this,'L');
			add(new JLabel("Unclsfd.Qty."),4,3,1,.8,this,'R');
			add(lblUCLQT = new JLabel(""),4,4,1,1,this,'L');
			add(new JLabel("Trgt.Dsp.Qty"),4,5,1,.8,this,'R');
			add(lblTDSQT = new JLabel(""),4,6,1,1,this,'L');
			
			add(new JLabel("Sales Rtn.Qty"),4,7,1,1,this,'L');
			add(lblSLRQT = new JLabel(""),4,8,1,1,this,'L');
			add(jtpMANTB = new JTabbedPane(),6,1,12,8,this,'L');
			
			jtpMANTB.addTab("Lot Details",pnlLOTDL);
			jtpMANTB.addTab("Q.C. Details",pnlTSTDL);
			jtpMANTB.addTab("Storage Details",pnlSTKDL);
			jtpMANTB.addTab("Despatch Details",pnlDSPDL);
				
			String[] L_LOCHD = {"select","Rcl. No.","Product Type","Grade","Classification Status","Classification Date"};
			int[] L_COLSZ = {50,80,150,150,150,150};
			tblLOTDL = crtTBLPNL1(pnlLOTDL,L_LOCHD,intROWCT,1,1,9.7,7.8,L_COLSZ,new int[]{0});
				
			String[] L_strTBLHD = {"select","Rcl. No.","Test Type","Test Date","DSP","MFI","IZO","VIC","TS","EL","RSM","WI","a","b","Y1"};
			int[] L_intCOLSZ = {20,70,100,100,40,40,40,40,40,40,40,40,40,40,40};			
			tblTSTDL = crtTBLPNL1(pnlTSTDL,L_strTBLHD,intROWCT,1,1,9.7,7.8,L_intCOLSZ,new int[]{0});
			
			String[] L_strTBLHD1 = {"select","Rcl. No.","Type","Date","Location","Pkg. Type","Qty."};
			int[] L_intCOLSZ1 = {50,90,200,100,100,100,100};
			tblSTKDL = crtTBLPNL1(pnlSTKDL,L_strTBLHD1,intROWCT,1,1,9.7,7.9,L_intCOLSZ1,new int[]{0});
			
			String[] L_strTBLHD2 = {"select","Inv. No.","Inv. Date","Buyer","Transporter","Vehicle No.","Indent No.","D.O No.","Grade","Qty."};
			int[] L_intCOLSZ2 = {30,70,120,150,150,80,70,80,70,80};
			tblDSPDL = crtTBLPNL1(pnlDSPDL,L_strTBLHD2,50,1,1,9.7,7.9,L_intCOLSZ2,new int[]{0});
			
			jtpMANTB.addMouseListener(this);
			M_pnlRPFMT.setVisible(true);
			setENBL(true);
															
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' AND CMT_CGSTP = 'FGXXRTP' and isnull(CMT_STSFL,' ') <> 'X'";
			M_rstRSSET=  cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())			
					hstRTPTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}			
			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' AND CMT_CGSTP = 'FGXXPKG' and isnull(CMT_STSFL,' ') <> 'X'";
			M_rstRSSET=  cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstPKGTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}

			M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN where CMT_CGMTP='SYS' AND CMT_CGSTP = 'FGXXITP' and isnull(CMT_STSFL,' ') <> 'X'";
			M_rstRSSET=  cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstITPTP.put(nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(M_rstRSSET.getString("CMT_CODDS"),""));
				M_rstRSSET.close();
			}
			lblPSTDT.setForeground(Color.blue);
			lblPENDT.setForeground(Color.blue);
			lblLINNO.setForeground(Color.blue);
			lblCYLNO.setForeground(Color.blue);
			lblBAGQT.setForeground(Color.blue);
			lblTPRCD.setForeground(Color.blue);
			lblCLSQT.setForeground(Color.blue);
			lblUCLQT.setForeground(Color.blue);
			lblTDSQT.setForeground(Color.blue);
			lblSLRQT.setForeground(Color.blue);
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Constructor");
		}  
	}
	/**
	* Super class Method overrided to enable & disable the components.
	* @param P_flgSTAT boolean argument to pass boolean State for the component.	
	*/
	void setENBL(boolean L_flgSTAT)
	{
		super.setENBL(L_flgSTAT);	
		{
			txtLOTNO.setEnabled(L_flgSTAT);
			
			tblLOTDL.cmpEDITR[TB1_CHKFL].setEnabled(false);
			tblLOTDL.cmpEDITR[TB1_RCLNO].setEnabled(false);
			tblLOTDL.cmpEDITR[TB1_PRDTP].setEnabled(false);
			tblLOTDL.cmpEDITR[TB1_PRDDS].setEnabled(false);
			tblLOTDL.cmpEDITR[TB1_CLSFL].setEnabled(false);
			tblLOTDL.cmpEDITR[TB1_CLSTM].setEnabled(false); 
				
			tblTSTDL.cmpEDITR[TB2_CHKFL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_RCLNO].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_TSTTP].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_TSTDT].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_DSPVL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_MFIVL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_IZOVL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_VICVL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_TS_VL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_EL_VL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_RSMVL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_WI_VL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_A__VL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_B__VL].setEnabled(false);
			tblTSTDL.cmpEDITR[TB2_Y1_VL].setEnabled(false);
			
			tblSTKDL.cmpEDITR[TB3_CHKFL].setEnabled(false);
			tblSTKDL.cmpEDITR[TB3_RCLNO].setEnabled(false);
			tblSTKDL.cmpEDITR[TB3_TRNTP].setEnabled(false);
			tblSTKDL.cmpEDITR[TB3_TRNDT].setEnabled(false);
			tblSTKDL.cmpEDITR[TB3_MNLCD].setEnabled(false);
			tblSTKDL.cmpEDITR[TB3_PKGTP].setEnabled(false);
			tblSTKDL.cmpEDITR[TB3_TRNQT].setEnabled(false);

			tblDSPDL.cmpEDITR[TB4_CHKFL].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_INVNO].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_INVDT].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_BUYER].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_TRASP].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_LRYNO].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_INDNO].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_DONO].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_PRDDS].setEnabled(false);
			tblDSPDL.cmpEDITR[TB4_DSPQT].setEnabled(false);
		}
	}	
	public void actionPerformed(ActionEvent L_AE)
	{ 
		super.actionPerformed(L_AE);
		try
		{
			if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedIndex()==0)				
					setENBL(false);
				else
				{					
					if(flgFIRST)
					{																																															   
						String L_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN where"
							+" CMT_CGMTP + CMT_CGSTP in( 'SYSPRXXCYL' ,'SYSQCXXCLS')"
							+" AND isnull(CMT_STSFL,'')<>'X'";																
						ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
						if(L_rstRSSET != null)
						{							
							while(L_rstRSSET.next())
								hstITMDS.put(nvlSTRVL(L_rstRSSET.getString("CMT_CODCD"),""),nvlSTRVL(L_rstRSSET.getString("CMT_CODDS"),""));
							L_rstRSSET.close();
						}
						flgFIRST = false;
					}
					setENBL(true);
					txtLOTNO.requestFocus();
					setMSG("please Enter the Lot Number to view History or Press F1 to select fro List..",'N');
				}
			}
			else if(M_objSOURC == txtLOTNO)
			{
				tblLOTDL.clrTABLE();
				tblTSTDL.clrTABLE();
				tblSTKDL.clrTABLE();
				tblDSPDL.clrTABLE();				
				lblPSTDT.setText("");
				lblPENDT.setText("");
				lblLINNO.setText("");
				lblCYLNO.setText("");
				lblBAGQT.setText("");
				lblTPRCD.setText("");
				lblCLSQT.setText("");
				lblUCLQT.setText("");
				lblTDSQT.setText("");
				lblSLRQT.setText("");
				if(txtLOTNO.getText().length() == 8)
				{
					strLOTNO = txtLOTNO.getText().trim();
					M_strSQLQRY= "Select LT_PRDTP from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = '"+txtLOTNO.getText().trim()+"'";
					M_rstRSSET= cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					{
						if(M_rstRSSET.next())
							strDBPTP = nvlSTRVL(M_rstRSSET.getString("LT_PRDTP"),"");
						else
						{
							setMSG("Invalid Lot Number, Press F1 to Select from list..",'N');
							return;
						}
						M_rstRSSET.close();	
					}
				}
				else
				{
					setMSG("Invalid Lot Number, Press F1 to Select From List..",'N');
					return;
				}								
				getDATA(); // Lot details
				
				getTSTREC();
				
				getSTKREC();
					
				getDSPREC();
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"actionPerformed");
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			setCursor(cl_dat.M_curWTSTS_pbst);
 			strLOTNO = txtLOTNO.getText().toString().trim();
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{	
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtLOTNO)
				{
					M_strHLPFLD = "txtLOTNO";
					M_strSQLQRY = "Select distinct LT_LOTNO from PR_LTMST where LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(LT_STSFL,'')<>'X'";
					if(txtLOTNO.getText().trim().length()>0)
						M_strSQLQRY += " AND LT_LOTNO like '"+txtLOTNO.getText().trim()+"%' "; 
					M_strSQLQRY += " order by lt_lotno";
					cl_hlp(M_strSQLQRY,1,1,new String[]{"Lot No"},1,"CT");
				}
				setCursor(cl_dat.M_curDFSTS_pbst);
			}			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"KeyPressed");
		}
	}
	/**
	 * Super class method overrided to execuate the F1 help.
	 */
	void exeHLPOK()
	{
		super.exeHLPOK();
		if(M_strHLPFLD == "txtLOTNO")
		{
			txtLOTNO.setText(cl_dat.M_strHLPSTR_pbst);	
		}
	}	
	/**
	 * Method to fetch the Lot Details from the database.
	 */	
	private void getDATA()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			
			String L_strTEMP="";
			int i=0;
			String L_strRCLNO,L_strPSTDT,L_strPENDT,L_strLINNO,L_strCYLNO,L_strBAGQT;
			String L_strTPRCD,L_strCPRCD,L_strPRDTP,L_strPRDDS,L_strTPRDS="",L_strCPRDS,L_strCLSFL,L_strCLSTM,L_strPRDCD;
			String L_strSTSFL,L_strRESNO,L_strCYLDS,L_strCLSTS,L_strLRYNO,L_strCONTR;
			java.sql.Timestamp L_tmpTIME;			
			tblLOTDL.clrTABLE();		
			if(tblLOTDL.isEditing())
				tblLOTDL.getCellEditor().stopCellEditing();
			tblLOTDL.setRowSelectionInterval(0,0);
			tblLOTDL.setColumnSelectionInterval(0,0);
			
			M_strSQLQRY = "Select LT_PRDTP,LT_RCLNO,LT_PSTDT,LT_PENDT,LT_LINNO,LT_CYLNO,LT_BAGQT,"
				+" LT_TPRCD,LT_CPRCD,LT_PRDCD,LT_CLSFL,LT_CLSTM,PR_PRDDS from PR_LTMST,CO_PRMST"
				+" where LT_PRDCD = PR_PRDCD AND LT_PRDTP = '"+strDBPTP+"' AND LT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND LT_LOTNO = '"+strLOTNO+"'"
				+" order by LT_PRDTP,LT_RCLNO";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET!= null)
			{
				if(M_rstRSSET.next())
				{
					L_tmpTIME = M_rstRSSET.getTimestamp("LT_PSTDT");
					if (L_tmpTIME != null)				
						L_strPSTDT = M_fmtLCDTM.format(L_tmpTIME);
					else
						L_strPSTDT = "";						
					L_strPRDTP = nvlSTRVL(M_rstRSSET.getString("LT_PRDTP"),"");
					L_strRCLNO = nvlSTRVL(M_rstRSSET.getString("LT_RCLNO"),"");					
					L_tmpTIME = M_rstRSSET.getTimestamp("LT_PENDT");
					if(L_tmpTIME != null)				
						L_strPENDT = M_fmtLCDTM.format(L_tmpTIME);
					else
						L_strPENDT = "";				
					L_strLINNO = nvlSTRVL(M_rstRSSET.getString("LT_LINNO"),"");
					L_strCYLNO = nvlSTRVL(M_rstRSSET.getString("LT_CYLNO"),"");
					L_strBAGQT = nvlSTRVL(M_rstRSSET.getString("LT_BAGQT"),"");
					L_strTPRCD = nvlSTRVL(M_rstRSSET.getString("LT_TPRCD"),"");
					L_strCPRCD = nvlSTRVL(M_rstRSSET.getString("LT_CPRCD"),"");
					L_strPRDCD = nvlSTRVL(M_rstRSSET.getString("LT_PRDCD"),"");
					L_strCLSFL = nvlSTRVL(M_rstRSSET.getString("LT_CLSFL"),"");
					L_tmpTIME = M_rstRSSET.getTimestamp("LT_CLSTM");
					if (L_tmpTIME != null)				
						L_strCLSTM = M_fmtLCDTM.format(L_tmpTIME);
					else
						L_strCLSTM = "";				
					L_strPRDDS = nvlSTRVL(M_rstRSSET.getString("PR_PRDDS"),"");					
					lblPSTDT.setText(L_strPSTDT);
					lblPENDT.setText(L_strPENDT);
					lblLINNO.setText(L_strLINNO);									
					if(hstITMDS.containsKey(L_strCYLNO))
						L_strCYLDS = hstITMDS.get(L_strCYLNO).toString();
					else
						L_strCYLDS = "";
					if(hstITMDS.containsKey(L_strCLSFL))
						L_strCLSTS = hstITMDS.get(L_strCLSFL).toString();
					else
						L_strCLSTS = "";						
					lblCYLNO.setText(L_strCYLDS);
					lblBAGQT.setText(L_strBAGQT);												
					String L_strSQLQRY = "Select PR_PRDDS from CO_PRMST where PR_PRDCD='"+L_strTPRCD+"'";
					ResultSet L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
					if(L_rstRSSET != null)
					{
						if(L_rstRSSET.next())					
							L_strTPRDS = L_rstRSSET.getString("PR_PRDDS");					
						L_rstRSSET.close();
					}							
					lblTPRCD.setText(L_strTPRDS);					
					tblLOTDL.setValueAt(L_strRCLNO,i,TB1_RCLNO);
					tblLOTDL.setValueAt(L_strPRDTP,i,TB1_PRDTP);
					tblLOTDL.setValueAt(L_strPRDDS,i,TB1_PRDDS);
					tblLOTDL.setValueAt(L_strCLSTS,i,TB1_CLSFL);
					tblLOTDL.setValueAt(L_strCLSTM,i,TB1_CLSTM);
					i++;
				}
				M_rstRSSET.close();
			}
			setMSG(" ",'N');
			double L_intTDSQT = 0;
			double L_intSLRQT = 0;
			double L_intDSTQT = 0;
			double L_intDUCQT = 0;
			M_strSQLQRY = "Select ST_STSFL,ST_RESNO,sum(ST_STKQT) L_strSTKQT,sum(ST_UCLQT) L_strUCLQT"
			+" from FG_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_LOTNO = '"+strLOTNO+"' group by ST_STSFL,ST_RESNO";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{
				    L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("ST_STSFL"),"");
					L_strRESNO = nvlSTRVL(M_rstRSSET.getString("ST_RESNO"),"0");
					intDSTQT = M_rstRSSET.getDouble("L_strSTKQT");
					intDUCQT = M_rstRSSET.getDouble("L_strUCLQT");
					if(L_strRESNO.length() > 0)
						L_intTDSQT = L_intTDSQT + intDSTQT;
					if(L_strSTSFL.equals("2"))
						L_intSLRQT = L_intSLRQT + intDSTQT;
					L_intDSTQT = L_intDSTQT + intDSTQT;
					L_intDUCQT = L_intDUCQT + intDUCQT;
					lblCLSQT.setText(String.valueOf(L_intDSTQT)); 
					lblUCLQT.setText(String.valueOf(L_intDUCQT));
					lblTDSQT.setText(String.valueOf(L_intTDSQT));
					lblSLRQT.setText(String.valueOf(L_intSLRQT));
				}
				M_rstRSSET.close();
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getDATA");
		}
	}
	/**
	 * Method to fetch the Quality testing details to dsplay in the Table
	 */
	private void getTSTREC()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			ResultSet L_rstRSSET;			
			java.sql.Timestamp L_tmpTIME;
			String L_strTSTTP = "";
			int L_intROWCNT = 0;
			if(tblTSTDL.isEditing())
				tblTSTDL.getCellEditor().stopCellEditing();
			tblTSTDL.setRowSelectionInterval(0,0);
			tblTSTDL.setColumnSelectionInterval(0,0);
			
			int L_intCOUNT = 0;
			String L_strSQLQRY = "Select PS_RCLNO,PS_TSTTP,PS_TSTDT,PS_DSPVL,PS_MFIVL,PS_IZOVL,PS_VICVL,";
			L_strSQLQRY += "PS_TS_VL,PS_EL_VL,PS_RSMVL,PS_WI_VL,PS_A__VL,PS_B__VL,PS_Y1_VL from QC_PSMST";
			L_strSQLQRY += " where PS_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PS_PRDTP='"+strDBPTP+"' AND PS_LOTNO='"+strLOTNO+"' AND PS_STSFL <> 'X'";
			L_strSQLQRY += " order by PS_RCLNO,PS_TSTTP";
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_RCLNO"),""),L_intCOUNT,TB2_RCLNO);
					strTSTTP = nvlSTRVL(L_rstRSSET.getString("PS_TSTTP"),"");					
					if(strTSTTP.equals("0101"))
						L_strTSTTP = "Grab Test";
					else if(strTSTTP.equals("0103"))
						L_strTSTTP = "Composite Test";
					else if(strTSTTP.equals("0104"))
						L_strTSTTP = "Bag Test";
					else
						L_strTSTTP = "";
					tblTSTDL.setValueAt(L_strTSTTP,L_intCOUNT,TB2_TSTTP);					
					
					L_tmpTIME = L_rstRSSET.getTimestamp("PS_TSTDT");
					if(L_tmpTIME!= null)					
						tblTSTDL.setValueAt(M_fmtLCDTM.format(L_tmpTIME),L_intCOUNT,TB2_TSTDT);					
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_DSPVL"),""),L_intCOUNT,TB2_DSPVL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_MFIVL"),""),L_intCOUNT,TB2_MFIVL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_IZOVL"),""),L_intCOUNT,TB2_IZOVL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_VICVL"),""),L_intCOUNT,TB2_VICVL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_TS_VL"),""),L_intCOUNT,TB2_TS_VL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_EL_VL"),""),L_intCOUNT,TB2_EL_VL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_RSMVL"),""),L_intCOUNT,TB2_RSMVL);									
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_WI_VL"),""),L_intCOUNT,TB2_WI_VL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_A__VL"),""),L_intCOUNT,TB2_A__VL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_B__VL"),""),L_intCOUNT,TB2_B__VL);
					tblTSTDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("PS_Y1_VL"),""),L_intCOUNT,TB2_Y1_VL);
					L_intCOUNT++;
				}
				L_rstRSSET.close();
			}			
			if(L_intCOUNT == 0)
				setMSG("No Quality Details Found..",'E');
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getTSTREC");
		}
	}
	/**
	 * Method to fetch the stock details from the data base.
	 */
	private void getSTKREC()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);
			String L_strTEMP ="";
			ResultSet L_rstRSSET;
			String L_strSQLQRY;
			java.sql.Timestamp L_tmpTIME;
			String L_strTSTTP = "";
			int L_intROWCNT = 0;			
			if(tblSTKDL.isEditing())
				tblSTKDL.getCellEditor().stopCellEditing();
			tblSTKDL.setRowSelectionInterval(0,0);
			tblSTKDL.setColumnSelectionInterval(0,0);
			
			L_strSQLQRY = "Select RCT_RCLNO,RCT_RCTTP,RCT_RCTDT,RCT_MNLCD,RCT_PKGTP,sum(RCT_RCTQT) L_RCTQT"
			+" from FG_RCTRN where RCT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RCT_PRDTP ='"+ strDBPTP +"' AND RCT_LOTNO = '"+strLOTNO+"'"
			+" AND RCT_STSFL='2' group by RCT_RCLNO,RCT_RCTTP,RCT_RCTDT,RCT_MNLCD,RCT_PKGTP"
			+" order by RCT_RCLNO,RCT_RCTTP,RCT_RCTDT,RCT_MNLCD,RCT_PKGTP"; 
			//System.out.println("RCT "+L_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{
					tblSTKDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("RCT_RCLNO"),""),L_intROWCNT,TB3_RCLNO);					
					L_strTEMP = nvlSTRVL(L_rstRSSET.getString("RCT_RCTTP"),"");
					if(hstRTPTP.containsKey(L_strTEMP))					
						tblSTKDL.setValueAt(hstRTPTP.get(L_strTEMP).toString(),L_intROWCNT,TB3_TRNTP);
					L_tmpTIME = L_rstRSSET.getTimestamp("RCT_RCTDT");
					if (L_tmpTIME != null)
						tblSTKDL.setValueAt(M_fmtLCDTM.format(L_tmpTIME),L_intROWCNT,TB3_TRNDT);
					tblSTKDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("RCT_MNLCD"),""),L_intROWCNT,TB3_MNLCD);					
					L_strTEMP = nvlSTRVL(L_rstRSSET.getString("RCT_PKGTP"),"");
					if(hstPKGTP.containsKey(L_strTEMP))
						tblSTKDL.setValueAt(hstPKGTP.get(L_strTEMP).toString(),L_intROWCNT,TB3_PKGTP);
					tblSTKDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("L_RCTQT"),""),L_intROWCNT,TB3_TRNQT);
					L_intROWCNT++;
				}
				L_rstRSSET.close();
			}
			L_strSQLQRY = "Select ist_rclno,ist_isstp,ist_issdt,ist_mnlcd,ist_pkgtp,sum(ist_issqt) L_ISSQT";
			L_strSQLQRY += " from fg_istrn where ist_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ist_prdtp='"+strDBPTP+"' and ist_lotno='"+strLOTNO+"'";
			L_strSQLQRY += " and ist_stsfl='2' group by ist_rclno,ist_isstp,ist_issdt,ist_mnlcd,ist_pkgtp"; 
			L_strSQLQRY += " order by ist_rclno,ist_isstp,ist_issdt,ist_mnlcd,ist_pkgtp"; 
			//System.out.println("ISS "+L_strSQLQRY);
			L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(L_rstRSSET != null)
			{
				while(L_rstRSSET.next())
				{	
					tblSTKDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("IST_RCLNO"),""),L_intROWCNT,TB3_RCLNO);					
					L_strTEMP = nvlSTRVL(L_rstRSSET.getString("IST_ISSTP"),"");
					if(hstITPTP.containsKey(L_strTEMP))
						tblSTKDL.setValueAt(hstITPTP.get(L_strTEMP).toString(),L_intROWCNT,TB3_TRNTP);
					L_tmpTIME = L_rstRSSET.getTimestamp("IST_ISSDT");
					if (L_tmpTIME != null)
						tblSTKDL.setValueAt(M_fmtLCDTM.format(L_tmpTIME),L_intROWCNT,TB3_TRNDT);
					tblSTKDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("IST_MNLCD"),""),L_intROWCNT,TB3_MNLCD);
					L_strTEMP = nvlSTRVL(L_rstRSSET.getString("IST_PKGTP"),"");
					if(hstPKGTP.containsKey(L_strTEMP))
						tblSTKDL.setValueAt(hstPKGTP.get(L_strTEMP).toString(),L_intROWCNT,TB3_PKGTP);					
					tblSTKDL.setValueAt(nvlSTRVL(L_rstRSSET.getString("L_ISSQT"),""),L_intROWCNT,TB3_TRNQT);
					L_intROWCNT++;
				} 
				L_rstRSSET.close();
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getSTKREC");
		}
	}
	/**
	 * Method to fetch the despatch detials from the database.
	 */
	private void getDSPREC()
	{
		try
		{
			setCursor(cl_dat.M_curWTSTS_pbst);	
			String L_strTEMP = "";
			ResultSet L_rstRSSET;
			String L_strSQLQRY="";
			ResultSet L_rstRSSET1;
			String L_strSQLQRY1="";
			java.sql.Timestamp L_tmpTIME;
			int L_intROWCNT = 0;
			if(tblDSPDL.isEditing())
				tblDSPDL.getCellEditor().stopCellEditing();
			tblDSPDL.setRowSelectionInterval(0,0);
			tblDSPDL.setColumnSelectionInterval(0,0);
			
			Hashtable<String,String> hstBYRNM = new Hashtable<String,String>();
			L_strSQLQRY = "Select distinct IVT_BYRCD,PT_PRTNM from FG_ISTRN,MR_IVTRN,CO_PTMST where IVT_CMPCD = IST_CMPCD and IVT_MKTTP = IST_MKTTP"
				+" AND IVT_LADNO = IST_ISSNO AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP = '"+strDBPTP+"' AND IST_LOTNO = '"+strLOTNO+"'"
				+" AND IST_STSFL = '2' AND IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND PT_PRTTP = 'C' AND PT_PRTCD = IVT_BYRCD";			
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(M_rstRSSET != null)
			{				
				while(M_rstRSSET.next())
					hstBYRNM.put(nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),""),nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
				M_rstRSSET.close();
			}
			Hashtable<String,String> hstTRPNM = new Hashtable<String,String>();
			L_strSQLQRY = "Select distinct IVT_TRPCD,PT_PRTNM from CO_PTMST,FG_ISTRN,MR_IVTRN where IVT_CMPCD=IST_CMPCD and IVT_MKTTP = IST_MKTTP"
				+" AND IVT_LADNO = IST_ISSNO AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP = '"+strDBPTP+"' AND IST_LOTNO = '"+strLOTNO+"'"
				+" AND IST_STSFL = '2' AND IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP AND PT_PRTTP = 'T' AND PT_PRTCD = IVT_TRPCD ";
			M_rstRSSET = cl_dat.exeSQLQRY1(L_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
					hstTRPNM.put(nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),""),nvlSTRVL(M_rstRSSET.getString("PT_PRTNM"),""));
				M_rstRSSET.close();
			}
			
			L_strSQLQRY = "Select distinct IVT_INVNO,IVT_INVDT,IVT_BYRCD,IVT_TRPCD,IVT_LRYNO,IVT_CNTDS,IVT_INDNO,"
			+"IVT_DORNO,IST_ISSQT,IVT_PRDDS from FG_ISTRN,MR_IVTRN where IVT_CMPCD = IST_CMPCD and IVT_MKTTP = IST_MKTTP"
			+" AND IVT_LADNO = IST_ISSNO AND IST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IST_PRDTP = '"+strDBPTP+"' AND IST_LOTNO = '"+strLOTNO+"'"
			+" AND IST_STSFL = '2' AND IVT_PRDCD = IST_PRDCD AND IVT_PKGTP = IST_PKGTP order by IVT_BYRCD,IVT_INVNO";
			//System.out.println("INV "+L_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
			if(M_rstRSSET != null)
			{
				while(M_rstRSSET.next())
				{					
					tblDSPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INVNO"),""),L_intROWCNT,TB4_INVNO);
					L_tmpTIME = M_rstRSSET.getTimestamp("IVT_INVDT");
					if (L_tmpTIME != null)
						tblDSPDL.setValueAt(M_fmtLCDTM.format(L_tmpTIME),L_intROWCNT,TB4_INVDT);

					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_BYRCD"),"");
					if(hstBYRNM.containsKey(L_strTEMP))
						tblDSPDL.setValueAt(hstBYRNM.get(L_strTEMP).toString(),L_intROWCNT,TB4_BUYER);
					
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_TRPCD"),"");
					if(hstTRPNM.containsKey(L_strTEMP))
						tblDSPDL.setValueAt(hstTRPNM.get(L_strTEMP).toString(),L_intROWCNT,TB4_TRASP);
										
					L_strTEMP = nvlSTRVL(M_rstRSSET.getString("IVT_LRYNO"),"");
					if(L_strTEMP.length()>0)
						tblDSPDL.setValueAt(L_strTEMP,L_intROWCNT,TB4_LRYNO);
					else
						tblDSPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_CNTDS"),""),L_intROWCNT,TB4_LRYNO);
					
					tblDSPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_INDNO"),""),L_intROWCNT,TB4_INDNO);
					tblDSPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_DORNO"),""),L_intROWCNT,TB4_DONO);
					tblDSPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IVT_PRDDS"),""),L_intROWCNT,TB4_PRDDS);
					tblDSPDL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IST_ISSQT"),""),L_intROWCNT,TB4_DSPQT);
					L_intROWCNT++;
				}
				M_rstRSSET.close();
			}			
			hstTRPNM = null;
			hstBYRNM = null;
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		catch(Exception L_EX)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			setMSG(L_EX,"getDSPREC");
		}
	}
} 
