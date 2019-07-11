/*
System Name   : Material Management System
Program Desc. : GRIN Acceptance
Author        : N.K. Virdi
Date          : 15/04/2004
Version       : MMS 2.0

Last Modified	: 03/09/2004 
Documented On	: 

*/
// GRIN Updation
import java.sql.ResultSet;import java.sql.SQLException;import java.sql.PreparedStatement;import java.sql.Date;
import javax.swing.*;
import java.awt.event.ActionEvent;import java.awt.event.FocusEvent;import java.awt.event.KeyEvent;import java.awt.event.ItemEvent;
import javax.swing.event.*;
import java.util.Vector;import java.util.Hashtable;import java.util.StringTokenizer;import java.util.Enumeration;

class mm_tegra extends cl_pbase
{
	private JComboBox cmbMATTP,cmbGRNTP;
	private JTextField txtEDITR;			// Table Cell Editor
	private JTextField txtGRNNO,txtGRNDT,txtAMDNO,txtAMDDT,txtACPDT,txtPORNO,txtVENCD,txtVENNM,txtTRNCD;
	private JTextField txtTRNNM,txtGINNO,txtLRYNO,txtCHLNO,txtCHLDT,txtREMDS,txtCMPDT;
	private JTextField txtREJCD,txtCHLQT,txtRECQT,txtACPQT,txtREJQT,txtCCTCD,txtOWNCD,txtUSGTP;
	private JCheckBox chkEDITR;
	private String strGRNNO,strGRNDT,strBOENO,strCNSNO,strPORNO;
	private String strVENCD,strVENNM,strTRNCD,strTRNNM,strSBSCD,strSTRTP,strGRNTP,strMATCD,strMATTP,strACPDT,strBATNO;
	private String strUOMCD,strCHLNO,strCHLDT,strCHLQT,strRECQT,strREJQT,strACPQT,strOACPQT,strPREBY,strPREDT,strAMDNO ="00",strAMDDT,strPRAMD="",strCSHPO ="";
	private String strLRYNO,strGINNO,LM_LUSBY,LM_LUPDT,strGINTP,strDELFL,strQLTFL,strREJCD,strLOCCD,strTEMP,strSRLNO;
	private int intTBLRW,intTBLCL,intROWCT,intOLDRW,intINDCT;
	private Hashtable<String,String> hstMATCD = new Hashtable<String,String>(); // For keeping pending P.O. qty.
	private Hashtable<String,String> hstITMDT = new Hashtable<String,String>(); // for GRIN Accepted qty.
	private ResultSet rstRSSET ;
	private PreparedStatement pstmINSREC,pstmDODREC;
	private float fltPORVL =0.0f;
	private INPVF objINPVR = new INPVF();	
	private cl_JTable tblGINVL;
      private  String [] staCYLCD;/** Hashtable to collect/maintain details of cylinders material code wise. KEY : Material Code VALUE : Vector in format "CYL. NO. || RENT FREE DAYS*/
	private Hashtable<String,Integer> hst99MAT = new Hashtable<String,Integer>();
	final int TBL_CHKFL = 0;
	final int TBL_MATCD = 1;
	final int TBL_UOMCD = 2;
	final int TBL_BATNO = 3;
	final int TBL_CHLQT = 4;
	final int TBL_RECQT = 5;
	final int TBL_ACPQT = 6;
	final int TBL_REJQT = 7;
	final int TBL_REJCD = 8;
	final int TBL_DELFL = 9;
	final int TBL_QLTFL = 10;
	final int TBL_LOCCD = 11;
	final int TBL_STKQT = 12;
	final int TBL_PORRT = 13;
	final int TBL_INDNO = 14;
	final int TBL_DPTCD = 15;
	final int TBL_OACPQT = 16;
	final int TBL_OREJQT = 17;
	
	final int TB1_MATCD =1;
	final int TB1_MATDS =2;
	final int TB1_BATNO =3;
	final int TB1_OWNCD =4;
	final int TB1_ISSQT =5;
	final int TB1_CCTCD =6;
	final int TB1_USGCD =7;
	final int TB1_ACPQT =8;
	
	final String strPORGR_fn ="01";					// PO GRIN	
	final String strCSHGR_fn ="02";					// PO GRIN	
	final String strJBWGR_fn ="05";					// Job Work Receipt
	final String strTRNFL_fn = "0";		// Transfer flag
	final String strDFLBT_fn ="COMMON";			// default bach no  
	
	private cl_JTable tblCYLDT;/** Panel in tabbed pane to contain cylinder details*/
	private JPanel pnlCYLDT;/** Tabbed pane for GRIN details and cylinder details*/
	private JTabbedPane tbpMAIN;/** Panel to contain GRIN base details in tabbed pane*/
	private JPanel pnlGINVL;/**Vector for material codes having cylinders. Used for help module */
	private JPanel pnl99ITM;
	private cl_JTable tbl99ITM;
	private String strISSNO ="";
	mm_tegra()
	{
		super(2);
		try
		{	
			setMatrix(20,8);
			add(new JLabel("GRIN Type"),1,1,1,1,this,'L');
			add(cmbGRNTP = new JComboBox(),1,2,1,2,this,'L');
			add(new JLabel("Material Type"),1,4,1,1,this,'L');
			add(cmbMATTP = new JComboBox(),1,5,1,2,this,'L');
			
			add(new JLabel("GRIN No."),2,1,1,1,this,'L');
			add(txtGRNNO = new TxtLimit(8),2,2,1,1,this,'L');
			add(new JLabel("GRIN Date"),2,3,1,1,this,'L');
			add(txtGRNDT = new TxtDate(),2,4,1,1,this,'L');
			add(new JLabel("Accepted Date"),2,5,1,1,this,'L');
			add(txtACPDT = new TxtDate(),2,6,1,1,this,'L');
			
			add(new JLabel("Amd. No."),3,1,1,1,this,'L');
			add(txtAMDNO = new TxtLimit(2),3,2,1,1,this,'L');
			add(new JLabel("Amd. Date"),3,3,1,1,this,'L');
			add(txtAMDDT = new TxtDate(),3,4,1,1,this,'L');
			add(new JLabel("PO Compln.Date"),3,5,1,1,this,'L');
			add(txtCMPDT = new TxtDate(),3,6,1,1,this,'L');

			add(new JLabel("Gate In No."),4,1,1,1,this,'L');
			add(txtGINNO = new TxtLimit(8),4,2,1,1,this,'L');
			add(new JLabel("Challan No."),4,3,1,1,this,'L');
			add(txtCHLNO = new TxtLimit(10),4,4,1,1,this,'L');
			add(new JLabel("Challan Date"),4,5,1,1,this,'L');
			add(txtCHLDT = new TxtDate(),4,6,1,1,this,'L');
		
			add(new JLabel("P.O. No."),5,1,1,1,this,'L');
			add(txtPORNO = new TxtLimit(8),5,2,1,1,this,'L');
			add(new JLabel("Supplier"),5,3,1,1,this,'L');
			add(txtVENCD = new TxtLimit(5),5,4,1,1,this,'L');
			add(txtVENNM = new TxtLimit(5),5,5,1,4,this,'L');
			txtVENNM.setEnabled(false);
		
			add(new JLabel("Lorry No."),6,1,1,1,this,'L');
			add(txtLRYNO = new TxtLimit(15),6,2,1,1,this,'L');
			add(new JLabel("Transporter"),6,3,1,1,this,'L');
			add(txtTRNCD = new TxtLimit(5),6,4,1,1,this,'L');
			add(txtTRNNM = new TxtLimit(40),6,5,1,4,this,'L');
			txtTRNCD.setEnabled(false);
			txtTRNNM.setEnabled(false);
			M_rstRSSET=cl_dat.exeSQLQRY("select count(*) from CO_CDTRN where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXCLG' and isnull(cmt_stsfl,'') <>'X'");
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
				 if(M_rstRSSET.getInt(1)>0)
					staCYLCD=new String[M_rstRSSET.getInt(1)];
			    M_rstRSSET.close(); 
			}
		
			String L_strDATA;
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXCLG' and isnull(cmt_stsfl,'') <>'X'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			int i=0;
			if(M_rstRSSET !=null)
			{
				while(M_rstRSSET.next())
					staCYLCD[i++]=M_rstRSSET.getString("CMT_CODCD");
				M_rstRSSET.close();
			}
			String[] L_COLHD = {"","Material","UOM","Batch/Lot","Chl.Qty.","Recvd.Qty.","Acp.Qty.","Rej.Qty.","Rej.Code","Del Tag","Qlt.Tag","Location","Stock Qty.","Value","Ind.No.","Dept. Code","Old APCQT","Old RejQt"};
			int[] L_COLSZ = {20,80,50,65,65,65,65,60,60,45,45,60,60,60,60,20,10,10};
			intROWCT = 150;
//			tblGINVL = crtTBLPNL1(this,L_COLHD,intROWCT,8,1,7,8,L_COLSZ,new int[]{0});
						///*** ADDED BY AAP FOR CYLINDER ENTRY
			pnlGINVL=new JPanel(null);
	        tblGINVL = crtTBLPNL1(pnlGINVL,L_COLHD,intROWCT,1,1,7.5,8,L_COLSZ,new int[]{0});
			tbpMAIN=new JTabbedPane();
//			tbpMAIN.addChangeListener(this);
			tbpMAIN.add("GRIN Details",pnlGINVL);
			pnlCYLDT=new JPanel(null);
			tblCYLDT = crtTBLPNL1(pnlCYLDT,new String[]{"FL","Mat. Code","Cylinder No.","Rent Free Days"},50,1,1,7.5,8,new int[]{20,100,150,100},new int[]{0});
			tblCYLDT.setInputVerifier(new TBLINPVF());
			tbpMAIN.add("Cylinder Details",pnlCYLDT);
			add(tbpMAIN,7,1,9.5,8.1,this,'L');
          		pnl99ITM=new JPanel(null);
			tbl99ITM = crtTBLPNL1(pnl99ITM,new String[]{"FL","Mat. Code","Description ","Batch/Lot","Owner","Issue Qty","Cost Center","Usage","Acp.Qty."},50,1,1,7.5,8,new int[]{20,100,300,60,80,80,80,80,5},new int[]{0});
			tbl99ITM.setInputVerifier(new TBLINPVF());
			tbpMAIN.add("99 Auto Issue Details",pnl99ITM);
			add(tbpMAIN,7,1,9.5,8.1,this,'L');
           	tbl99ITM.setCellEditor(TB1_CCTCD,txtCCTCD = new TxtLimit(15));
            tbl99ITM.setCellEditor(TB1_OWNCD,txtOWNCD = new TxtLimit(3));
            tbl99ITM.setCellEditor(TB1_USGCD,txtUSGTP = new TxtLimit(2));
            txtCCTCD.addFocusListener(this);txtCCTCD.addKeyListener(this);
            txtOWNCD.addFocusListener(this);txtOWNCD.addKeyListener(this);
            txtUSGTP.addFocusListener(this);txtUSGTP.addKeyListener(this);
			///***

			txtEDITR = (JTextField)tblGINVL.getCellEditor(1,1).getTableCellEditorComponent(tblGINVL,"",false,1,1);
			tblGINVL.clrTABLE();
			add(new JLabel("Remarks"),17,1,1,1,this,'L');
			add(txtREMDS = new TxtLimit(200),17,2,1,7,this,'L');
			setMSG("Select an Option..",'N');
			cl_dat.M_flgHELPFL_pbst = false;
			strGINTP = "02";
			tblGINVL.setCellEditor(TBL_CHLQT,txtCHLQT = new TxtNumLimit(10.3));
			tblGINVL.setCellEditor(TBL_ACPQT,txtACPQT = new TxtNumLimit(10.3));
			tblGINVL.setCellEditor(TBL_REJQT,txtREJQT = new TxtNumLimit(10.3));
			tblGINVL.setCellEditor(TBL_REJCD,txtREJCD = new TxtLimit(3));
			txtCHLQT.addFocusListener(this);txtCHLQT.addKeyListener(this);
			txtACPQT.addFocusListener(this);txtACPQT.addKeyListener(this);
			txtREJQT.addFocusListener(this);txtREJCD.addKeyListener(this);
			txtREJCD.addFocusListener(this);txtREJQT.addKeyListener(this);
			pstmINSREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
			"INSERT INTO MM_POMST(PO_CMPCD,PO_MMSBS,PO_STRTP,PO_PORTP,PO_PORNO,PO_MATCD,PO_MATDS,PO_INDTP,"+
			"PO_INDNO,PO_PORDT,PO_AMDNO,PO_SHRDS,PO_BUYCD,PO_EFFDT,PO_CMPDT,PO_VENTP,PO_VENCD,PO_CURCD,"+
			"PO_EXGRT,PO_PORVL,PO_PREBY,PO_PREDT,PO_UOMCD,PO_UCNVL,"+
			"PO_PORRT,PO_PERRT,PO_PORQT,PO_ITVAL,PO_PMTRF,PO_DPTCD,PO_TRNFL,PO_STSFL,PO_LUSBY,PO_LUPDT)VALUES("+
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"	
		);
		pstmDODREC = cl_dat.M_conSPDBA_pbst.prepareStatement("INSERT INTO MM_PODEL(POD_CMPCD,POD_MMSBS,POD_STRTP,POD_PORTP,POD_PORNO,POD_MATCD,POD_INDNO,"+
			"POD_EDLDT,POD_EDLQT,POD_AMDNO,POD_TRNFL,POD_STSFL,POD_LUSBY,POD_LUPDT)VALUES("+
		"?,?,?,?,?,?,?,?,?,?,?,?,?,?)"	
		);

		}catch(Exception E){
			setMSG(E,"constructor");
		}	
		setENBL(false);
		cmbGRNTP.setEnabled(false);
		cmbMATTP.setEnabled(false);
		txtGRNNO.setEnabled(false);
		setMSG("Select an Option..",'N');
		cl_dat.M_flgHELPFL_pbst = false;
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		txtGRNDT.setInputVerifier(objINPVR);
		txtCHLDT.setInputVerifier(objINPVR);
		txtPORNO.setInputVerifier(objINPVR);
		tblGINVL.setInputVerifier(new TBLINPVF());		
	}	
	void setENBL(boolean P_flgSTATE)
	{
		super.setENBL(P_flgSTATE);
		tblCYLDT.setEnabled(false);
		if(tblGINVL.isEditing())
			tblGINVL.getCellEditor().stopCellEditing();
	    cmbGRNTP.setEnabled(!P_flgSTATE);
		cmbMATTP.setEnabled(!P_flgSTATE);
		tblGINVL.cmpEDITR[TBL_CHKFL].setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount()>0)
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				txtGRNNO.setEnabled(true);
			else
			{
				txtGRNNO.setEnabled(P_flgSTATE);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					tblGINVL.cmpEDITR[TBL_CHKFL].setEnabled(false);
				else
					tblGINVL.cmpEDITR[TBL_CHKFL].setEnabled(true);
			}
		txtLRYNO.setEnabled(P_flgSTATE);
		txtCHLNO.setEnabled(P_flgSTATE);
		txtCHLDT.setEnabled(P_flgSTATE);
	    txtGRNDT.setEnabled(false);
		txtACPDT.setEnabled(false);
		txtAMDNO.setEnabled(false);
		txtAMDDT.setEnabled(false);
		txtCMPDT.setEnabled(false);
		txtGINNO.setEnabled(false);
		txtVENCD.setEnabled(false);
		txtVENNM.setEnabled(false);
        txtTRNNM.setEnabled(false);
		txtPORNO.setEnabled(false);
		
		tblCYLDT.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblGINVL.cmpEDITR[TBL_RECQT].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_MATCD].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_BATNO].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_UOMCD].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_STKQT].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_QLTFL].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_OACPQT].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_OREJQT].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_DELFL].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_INDNO].setEnabled(false);
		tblGINVL.cmpEDITR[TBL_DPTCD].setEnabled(false);
		tbl99ITM.cmpEDITR[TB1_MATCD].setEnabled(false);
		tbl99ITM.cmpEDITR[TB1_MATDS].setEnabled(false);
		tbl99ITM.cmpEDITR[TB1_BATNO].setEnabled(false);

	}
	public void itemStateChanged(ItemEvent L_IE)
	{
		super.itemStateChanged(L_IE);
		if(M_objSOURC == cl_dat.M_cmbSBSL2_pbst)
		{
			try
			{
				if(M_strSBSCD==null)
					return;
				String L_strDATA ="";
				if(M_strSBSCD !=null)
				{
					cmbGRNTP.removeAllItems();
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXGRN'";
					M_strSQLQRY += " AND CMT_CHP01 like '%"+M_strSBSCD.substring(2,4) +"%'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					while(M_rstRSSET.next())
					{
						L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
						cmbGRNTP.addItem(L_strDATA);
					}
					if(M_rstRSSET != null)
						M_rstRSSET.close();	
				}
				M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
				M_strSQLQRY += " where CMT_CGMTP = 'SYS' and CMT_CGSTP = 'MMXXMTP' and isnull(cmt_stsfl,'') <>'X'";
				M_strSQLQRY += " AND CMT_CHP01 like '%"+M_strSBSCD.substring(2,4) +"%'";
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					cmbMATTP.removeAllItems();
					while(M_rstRSSET.next())
					{
						L_strDATA = M_rstRSSET.getString("CMT_CODCD") + " " + M_rstRSSET.getString("CMT_CODDS");
						cmbMATTP.addItem(L_strDATA);
					}
					M_rstRSSET.close();
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Child.itemStateChanged");
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
				cmbGRNTP.setEnabled(true);
				cmbMATTP.setEnabled(true);
				strSBSCD = M_strSBSCD;
				strSTRTP = M_strSBSCD.substring(2,4);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					setENBL(true);
					cmbGRNTP.setEnabled(true);
					cmbMATTP.setEnabled(true);
					txtACPDT.setText(cl_dat.M_strLOGDT_pbst);
					txtGRNNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
				{
					setENBL(true);
					cmbGRNTP.setEnabled(true);
					cmbMATTP.setEnabled(true);
					txtGRNNO.requestFocus();
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))
				{
				setENBL(false);
				cmbGRNTP.setEnabled(true);
				cmbMATTP.setEnabled(true);
				txtGRNNO.setEnabled(true);	
				}
				else
				{
					setMSG("Select an option",'N');
					setENBL(false);
					cmbGRNTP.setEnabled(false);
					cmbMATTP.setEnabled(false);
					txtGRNNO.setEnabled(false);
				}
				cmbGRNTP.requestFocus();
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(L_AE.getSource().equals(cmbGRNTP))
			{
				strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
				txtGRNNO.requestFocus();
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Child.ActionPerformed");
		}
	}
	
	public void keyPressed(KeyEvent L_KE)
	{
		try
		{
			super.keyPressed(L_KE);
			setMSG("",'N');
			if(L_KE.getKeyCode() == L_KE.VK_F1)
			{	
				if(L_KE.getSource().equals(txtGRNNO))		// GRIN No
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtGRNNO";
					strGRNTP = cmbGRNTP.getSelectedItem().toString().substring(0,2);
					strSTRTP = M_strSBSCD.substring(2,4);
					String L_ARRHDR[] = {"GRIN No.","GRIN Date","Gate-In No."};
					M_strSQLQRY = "Select distinct GR_GRNNO,GR_GRNDT,GR_GINNO from MM_GRMST";
					M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
					M_strSQLQRY += " and GR_GRNTP = '" + strGRNTP + "'";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
						M_strSQLQRY += " and gr_acpdt is null ";
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						M_strSQLQRY += " and gr_acpdt is not null ";
					if(txtGRNNO.getText().trim().length() >0)
						M_strSQLQRY += " and gr_grnno like '"+txtGRNNO.getText().trim() +"%'";
					M_strSQLQRY += " and isnull(GR_STSFL,' ') <>'X' order by GR_GRNNO desc";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,3,1,L_ARRHDR,3,"CT");
				}
				else if(L_KE.getSource().equals(txtPORNO))	// P.O.No.
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtPORNO";
					String L_ARRHDR[] = {"P.O.No","P.O.Date","Vendor Code","Vendor"};
					M_strSQLQRY = "Select PO_PORNO,PO_PORDT,PO_VENCD,PT_PRTNM from MM_POMST,CO_PTMST";
					M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STSFL <> 'X'";
					M_strSQLQRY += " and PO_VENCD = PT_PRTCD";
					M_strSQLQRY += " and PT_PRTTP = 'S'";
								
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
				}
				else if(L_KE.getSource().equals(txtVENCD))	// Supplier Code
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtVENCD";
					String L_ARRHDR[] = {"Code","Name"};
					M_strSQLQRY = "Select PT_PRTCD,PT_PRTNM from CO_PTMST";
					M_strSQLQRY += " where PT_PRTTP = 'S'";
								
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtREJCD)
				{		
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtREJCD";
					String L_ARRHDR[] = {"Rejection Code","Description "};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP ='SYS' and cmt_cgstp ='MMXXREJ'";
					M_strSQLQRY += " order by CMT_CODCD";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
				else if(M_objSOURC == txtCCTCD)				// Usage Type
			    {
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtCCTCD";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "Select EQ_TAGNO,EQ_TAGDS from mm_eqmst ";
					if(txtCCTCD.getText().trim().length() >0)
						M_strSQLQRY +=" WHERE EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EQ_TAGNO like '"+txtCCTCD.getText().trim() +"%'";
					M_strSQLQRY += " order by eq_tagno";
					cl_hlp(M_strSQLQRY ,1,1,L_ARRHDR,2,"CT");
			    }
			    else if(M_objSOURC == txtOWNCD)				// Usage Type
			    {
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtOWNCD";
					String L_ARRHDR[] = {"Code","Description"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' AND isnull(CMT_STSFL,'') <>'X' ";
					if(txtOWNCD.getText().trim().length() >0)
						M_strSQLQRY +=" WHERE CMT_CODCD like '"+txtOWNCD.getText().trim() +"%'";
					M_strSQLQRY += " order by CMT_CODCD";
					cl_hlp(M_strSQLQRY ,1,1,L_ARRHDR,2,"CT");
			    }
			    else if(M_objSOURC == txtUSGTP)				// Usage Type
				{
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtUSGTP";
					String L_ARRHDR[] = {"Code","Usage Type"};
					M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
					M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
					M_strSQLQRY += " and CMT_CGSTP = 'MMXXUSG'";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY ,2,1,L_ARRHDR,2,"CT");
				}
			}
			else if(L_KE.getKeyCode() == L_KE.VK_ENTER)
			{
				if(L_KE.getSource().equals(cmbGRNTP))
					cmbMATTP.requestFocus();
				if(L_KE.getSource().equals(cmbMATTP))
					txtGRNNO.requestFocus();
				if(L_KE.getSource().equals(txtGRNNO))
				{			
					this.setCursor(cl_dat.M_curWTSTS_pbst);
					strSTRTP = M_strSBSCD.substring(2,4);
					strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
					strMATTP = String.valueOf(cmbMATTP.getSelectedItem()).trim().substring(0,2);
					strGRNNO = txtGRNNO.getText().trim();
					clrCOMP();
					for(int i=0;i<cmbGRNTP.getItemCount();i++)
					{
						if(cmbGRNTP.getItemAt(i).toString().substring(0,2).equals(strGRNTP))
						{
							cmbGRNTP.setSelectedIndex(i);
							break;
						}
					}
					for(int i=0;i<cmbMATTP.getItemCount();i++)
					{
						if(cmbMATTP.getItemAt(i).toString().substring(0,1).equals(strMATTP))
						{
							cmbMATTP.setSelectedIndex(i);
							break;
						}
					}
					if(getDATA(strSTRTP,strGRNTP,strGRNNO))
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))	
							setENBL(false);
						else setENBL(true);
						///if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))	// line Added on 17/11/04
						///if(!chkDATA())
						///	setMSG("Indent /P.O data not found",'E');
					}
					this.setCursor(cl_dat.M_curDFSTS_pbst);
				}
				else if(L_KE.getSource().equals(txtGRNDT))
				{			
					strGRNDT = txtGRNDT.getText().trim();
					if(M_fmtLCDAT.parse(strGRNDT).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
						setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
				}
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"Child.keyPressed");
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			if(M_strHLPFLD.equals("txtGRNNO")){				// GRIN No
				this.setCursor(cl_dat.M_curWTSTS_pbst);
				strGRNTP = cmbGRNTP.getSelectedItem().toString().substring(0,2);
				strMATTP = cmbMATTP.getSelectedItem().toString().substring(0,1);
				txtGRNNO.setText(cl_dat.M_strHLPSTR_pbst);
				clrCOMP();
				for(int i=0;i<cmbGRNTP.getItemCount();i++)
				{
					if(cmbGRNTP.getItemAt(i).toString().substring(0,2).equals(strGRNTP))
					{
						cmbGRNTP.setSelectedIndex(i);
						break;
					}
				}
				for(int i=0;i<cmbMATTP.getItemCount();i++)
				{
					if(cmbMATTP.getItemAt(i).toString().substring(0,1).equals(strMATTP))
					{
						cmbMATTP.setSelectedIndex(i);
						break;
					}
				}
				if(getDATA(strSTRTP,strGRNTP,cl_dat.M_strHLPSTR_pbst))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPENQ_pbst))	
						setENBL(false);
					else setENBL(true);
				}
				this.setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else if(M_strHLPFLD.equals("txtREJCD"))
			{			// Rejection Code
				txtREJCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtOWNCD"))
			{
				txtOWNCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtCCTCD"))
			{
				txtCCTCD.setText(cl_dat.M_strHLPSTR_pbst);
			}
			else if(M_strHLPFLD.equals("txtUSGTP"))
			{
				txtUSGTP.setText(cl_dat.M_strHLPSTR_pbst);
			}
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"exeHLPOK");
		}
	}
	
	// Method to check whether all necessary data has been entered
	boolean vldDATA()
	{
		try
		{
			fltPORVL =0.0f;
			strGRNDT = txtGRNDT.getText().trim();
			strPORNO = txtPORNO.getText().trim();
			strGINNO = txtGINNO.getText().trim();
			strCHLNO = txtCHLNO.getText().trim();
			strCHLDT = txtCHLDT.getText().trim();
			if(strGRNDT.length() == 0)
			{					//  GRIN Date
				txtGRNDT.requestFocus();
				setMSG("GRIN Date can not be empty",'E');
				return false;
			}
			else if(strGINNO.length() == 0)
			{					// Chalan No
				txtGINNO.requestFocus();
				setMSG("Gate In No. can not be empty",'E');
				return false;
			}
			else if(strCHLNO.length() == 0)
			{					// Chalan No
				txtCHLNO.requestFocus();
				setMSG("Chalan No. can not be empty",'E');
				return false;
			}
			else if(strCHLDT.length() == 0)
			{					//  Chalan Date
				txtCHLDT.requestFocus();
				setMSG("Chalan Date can not be empty",'E');
				return false;
			}
			else if(strPORNO.length() == 0)
			{					//  GRIN Date
			    if(!cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strJBWGR_fn))				
				   if(!strGRNTP.equals(strCSHGR_fn))
				   {
					   txtPORNO.requestFocus();
						setMSG("P.O. Ref. can not be empty",'E');
						return false;
				   }
			}
			if(tblGINVL.isEditing())
    		{
    			if(tblGINVL.getValueAt(tblGINVL.getSelectedRow(),tblGINVL.getSelectedColumn()).toString().length()>0)
    			{
        			TBLINPVF obj=new TBLINPVF();
        			obj.setSource(tblGINVL);
        			if(obj.verify(tblGINVL.getSelectedRow(),tblGINVL.getSelectedColumn()))
        				tblGINVL.getCellEditor().stopCellEditing();
        			else
        				return false;
    			}
    		}
		//	if(tblGINVL.isEditing())
		//		tblGINVL.getCellEditor().stopCellEditing();
			if(tbl99ITM.isEditing())
				tbl99ITM.getCellEditor().stopCellEditing();
			Vector<String> L_vtrMATCD = new Vector<String>();
			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					// Vector L_vtrMATCD will contain the cylinder Itemcodes .
					for(int j=0;j<staCYLCD.length;j++)
						if(tblGINVL.getValueAt(i,1).toString().substring(0,staCYLCD[j].length()).equals(staCYLCD[j]))
							L_vtrMATCD.addElement(tblGINVL.getValueAt(i,1).toString());
				}
			}
			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(tblGINVL.getValueAt(i,TBL_CHLQT).toString().length() == 0)
					{
						setMSG("Chalan quantity can not be empty",'E');
						return false;
					}
					else if(tblGINVL.getValueAt(i,TBL_RECQT).toString().length() == 0)
					{
						setMSG("Recieved quantity can not be empty",'E');
						return false;
					}
					else if(tblGINVL.getValueAt(i,TBL_ACPQT).toString().length() == 0)
					{
						setMSG("Accepted quantity can not be empty",'E');
						return false;
					}
					if(strGRNTP.equals(strCSHGR_fn))
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							if(tblGINVL.getValueAt(i,TBL_PORRT).toString().length() == 0)
							{
								setMSG("Value can not be empty",'E');
								return false;
							}
							if(tblGINVL.getValueAt(i,TBL_INDNO).toString().length() == 0)
							{
								setMSG("INdent No. can not be empty",'E');
								return false;
							}
							fltPORVL += Double.parseDouble(tblGINVL.getValueAt(i,TBL_PORRT).toString());
								java.util.Hashtable<String,String> L_hstPENQT = new Hashtable<String,String>(); 
						// Modified  on 17/09/04 to stop ORDQT > IN_AUTQT in cash P.O.
							double L_dblPENQT =0.0;
							M_strSQLQRY = "SELECT GR_GRNNO,GR_PORNO,GR_MATCD,"
										  +"isnull(IN_AUTQT,0)-isnull(IN_ORDQT,0)-isnull(IN_FCCQT,0)L_PENQT"
										  +" from MM_GRMST,MM_INMST WHERE IN_STRTP = GR_STRTP AND IN_INDNO = GR_PORNO AND IN_MATCD = GR_MATCD AND IN_CMPCD = GR_CMPCD "
										  +" AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(GR_STSFL,'') ='0' AND IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') ='4' and GR_STRTP ='"+M_strSBSCD.substring(2,4) +"'"
										  +" AND GR_GRNNO ='"+txtGRNNO.getText().trim()+"'";
							M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
							if(M_rstRSSET !=null)
								while(M_rstRSSET.next())
								{
									L_hstPENQT.put(M_rstRSSET.getString("GR_MATCD"),M_rstRSSET.getString("L_PENQT"));
								}
							for(int j=0;j<tblGINVL.getRowCount();j++)
							{
								if(tblGINVL.getValueAt(j,TBL_CHKFL).toString().equals("true"))
								{
									L_dblPENQT = Double.parseDouble(L_hstPENQT.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString());
									///if(L_dblPENQT < Double.parseDouble(tblGINVL.getValueAt(j,TBL_ACPQT).toString()))
									if(Double.parseDouble(setNumberFormat(L_dblPENQT,3)) < getQTY("ACP",tblGINVL.getValueAt(j,TBL_MATCD).toString()))
									{
										//setMSG("Order Qty. can not be greater than pending Indent qty for.."+tblGINVL.getValueAt(j,TBL_MATCD).toString(),'E');
										setMSG("Accepted Qty. can not be greater than pending qty for.."+tblGINVL.getValueAt(j,TBL_MATCD).toString(),'E');
										return false;
									}
								}
							}
							// end Modified
						}
					}
					else if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))  
					{
						float L_fltDIFQT =0.0F; // Difference between current and previous accepted
				        for(int j=0;j<tblGINVL.getRowCount();j++)			// check with pending po qty.
						{
							strTEMP = tblGINVL.getValueAt(j,TBL_ACPQT).toString();
///							///strTEMP = String.valueOf(getQTY("ACP",tblGINVL.getValueAt(j,TBL_MATCD).toString()));
							if(tblGINVL.getValueAt(j,TBL_CHKFL).toString().equals("true"))
							{
							    /// check
							    if(!txtAMDNO.getText().equals("00"))
							    /// before the rej qty check with stock,checking the 
							    //diff of current and prv qty included on 03/03/06 
						    	if(Float.parseFloat(strTEMP)< Float.parseFloat(hstITMDT.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()+tblGINVL.getValueAt(j,TBL_BATNO).toString()).toString()))
						    	{
    						    	System.out.println("xxx");
    						    	//nvlSTRVL(tblIVTRN.getValueAt(i,TBL_INVQT).toString(),"0")
    						    	float L_fltREJQT = Float.parseFloat(tblGINVL.getValueAt(j,TBL_REJQT).toString());
    						    	///L_fltREJQT = L_fltREJQT - Float.parseFloat(nvlSTRVL(tblGINVL.getValueAt(j,TBL_OREJQT).toString(),"0"));
							L_fltREJQT = getQTY("REJ",tblGINVL.getValueAt(j,TBL_MATCD).toString()) - Float.parseFloat(nvlSTRVL(tblGINVL.getValueAt(j,TBL_OREJQT).toString(),"0"));

						    		System.out.println("Prv. Rej " +L_fltREJQT);
						    		if(L_fltREJQT > (Float.parseFloat(tblGINVL.getValueAt(j,TBL_STKQT).toString())))
    								{
    									setMSG("Rejected qty. can not be greater than the Current Stock qty...",'E');
    									return false;
    								}
    						    	/*if((nvlSTRVL(tblGINVL.getValueAt(j,TBL_OREJQT).toString(),"0")) == 0)
    						    	{
        						    	if(Float.parseFloat(tblGINVL.getValueAt(j,TBL_REJQT).toString())> (Float.parseFloat(tblGINVL.getValueAt(j,TBL_STKQT).toString())))
        								{
        									setMSG("Rejected qty. can not be greater than the Current Stock qty...",'E');
        									return false;
        								}
    						    	}
    						    	else // else part include on 04/11/06 , GRIN amd. prob
    						    	{
	                                     if((Float.parseFloat(tblGINVL.getValueAt(j,TBL_REJQT).toString()) - Float.parseFloat(tblGINVL.getValueAt(j,TBL_OREJQT).toString()))> (Float.parseFloat(tblGINVL.getValueAt(j,TBL_STKQT).toString())))
        								{
        									setMSG("Rejected qty. can not be greater than the Current Stock qty...",'E');
        									return false;
        								}
    						    	}*/
    						   	}
						   	    // Pending P.O. qty < ACPQT
								///if(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString()) < Float.parseFloat(strTEMP))
								if(Float.parseFloat(setNumberFormat(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString()),3)) < getQTY("ACP",tblGINVL.getValueAt(j,TBL_MATCD).toString()))
								{
								    if(!txtAMDNO.getText().equals("00"))
									{
										// Current ACPQT > Prv. ACPQT
										if(Float.parseFloat(strTEMP)> Float.parseFloat(hstITMDT.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()+tblGINVL.getValueAt(j,TBL_BATNO).toString()).toString()))
										{
											/// L_fltDIFQT = Float.parseFloat(hstITMDT.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString())-Float.parseFloat(strTEMP);
											// changed on 29/08/2005 as difqt was getting negative, validation was getting through 
											L_fltDIFQT = Float.parseFloat(strTEMP) - Float.parseFloat(hstITMDT.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()+tblGINVL.getValueAt(j,TBL_BATNO).toString()).toString());
											// Modified on 09/11/2004
											//	if(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString()) < Float.parseFloat(strTEMP))
											// if amended qty. is greater than prv . qty then check with pending P.O. qty
                                          				 // System.out.println("amd more difqt "+setNumberFormat(L_fltDIFQT,3));
				                                           // System.out.println(hstMATCD.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString());
											if(Float.parseFloat(setNumberFormat(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString()),3)) < Float.parseFloat(setNumberFormat(L_fltDIFQT,3)))
											{
												setMSG("Accepted qty. can not be greater than the pending order qty at.."+(j+1)+" Available : "+setNumberFormat(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString()),3)+"   Entered : "+setNumberFormat(L_fltDIFQT,3),'E');
												return false;
											}
										}
									    /* Moved above the if loop on 29/08/2005
									    if(Float.parseFloat(tblGINVL.getValueAt(j,TBL_REJQT).toString())> (Float.parseFloat(tblGINVL.getValueAt(j,TBL_STKQT).toString())))
        								    {
        									setMSG("Rejected qty. can not be greater than the Current Stock qty...",'E');
        									return false;
        								    }*/
									}
									else
									{
										setMSG("Accepted qty. > pending order qty, at row "+(j+1)+"  "+setNumberFormat(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(j,TBL_MATCD).toString()).toString()),3)+  " / "+getQTY("ACP",tblGINVL.getValueAt(j,TBL_MATCD).toString()),'E');
								        return false;
									}
								}
							}
						}
					}
					
					boolean L_flgTEMP = false;
					for(int k=0;k<L_vtrMATCD.size();k++)
					{
						L_flgTEMP = false;
						for(int j=0;j<tblCYLDT.getRowCount();j++)			
						{
							if(tblCYLDT.getValueAt(j,TBL_CHKFL).toString().equals("true"))
							{
								if(tblCYLDT.getValueAt(j,1).toString().equals(L_vtrMATCD.elementAt(k).toString()))
									L_flgTEMP = true;
							}
						}
						if(!L_flgTEMP)
						{
							setMSG("Select Cylinders for "+L_vtrMATCD.elementAt(k).toString(),'E');
							return false;
						}
					}
					L_flgTEMP = false;
					for(int j=0;j<tblCYLDT.getRowCount();j++)			
					{
						if(tblCYLDT.getValueAt(j,TBL_CHKFL).toString().equals("true"))
						{
							if(L_vtrMATCD.contains((String)tblCYLDT.getValueAt(j,1).toString()))
							   L_flgTEMP = true;
							else
							{
								setMSG("Invalid Selection of Cylinder at row "+j,'E');
								return false;
							}
						}
					}
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
    			String L_strMATCD,L_strDPTCD,L_strCCTCD,L_strISSQT,L_strACPQT_ISS,L_strUSGTP,L_strBATNO;
    			for(int i=0;i<tbl99ITM.getRowCount();i++)
    	        {
    	            if(tbl99ITM.getValueAt(i,TBL_CHKFL).toString().equals("true"))
    	            {
    	                L_strMATCD = tbl99ITM.getValueAt(i,TB1_MATCD).toString();
    	                L_strDPTCD = tbl99ITM.getValueAt(i,TB1_OWNCD).toString();
    	                L_strISSQT = tbl99ITM.getValueAt(i,TB1_ISSQT).toString();
    	                L_strACPQT_ISS = tbl99ITM.getValueAt(i,TB1_ACPQT).toString();
    	                L_strCCTCD = tbl99ITM.getValueAt(i,TB1_CCTCD).toString();
    	                L_strUSGTP = tbl99ITM.getValueAt(i,TB1_USGCD).toString();
    	                if(L_strDPTCD.length() ==0)
    	                {
    	                    setMSG("Enter Department Code for 99 Issue generation at row "+(i+1),'E'); 
    	                    return false;
    	                }
    	                else if(L_strCCTCD.length() ==0)
    	                {
    	                    setMSG("Enter Cost Center Code for 99 Issue generation at row "+(i+1),'E'); 
    	                    return false;
    	                }
    	                else if(L_strUSGTP.length() ==0)
    	                {
    	                    setMSG("Enter Usage Type for 99 Issue generation at row "+(i+1),'E'); 
    	                    return false;
    	                }
    	                else if(L_strISSQT.length() ==0)
    	                {
    	                    setMSG("Enter Issue Qty. for 99 Issue generation at row "+(i+1),'E');  
    	                    return false;
    	                }
    	                else if(Double.parseDouble(L_strISSQT) == 0 && Double.parseDouble(L_strACPQT_ISS)>0)
    	                {
    	                    setMSG("Enter Issue Qty for 99 Issue generation at row "+(i+1),'E'); 
    	                    return false;
    	                }
    	            }
    	        }
	        }// end of OPaDD for gen auto iss
		}
		catch(Exception e)
		{
			setMSG("Invalid data ..",'E');
			setMSG(e,"Child.vldDATA ");
			return false;
		}
		return true;
	}
	boolean getDATA(String LP_STRTP,String LP_GRNTP,String LP_GRNNO)
	{
		ResultSet L_rstRSSET ;
		boolean L_FIRST = true;
		java.sql.Date L_datTMPDT;
		String L_strYSTDT =""; // Year Start date to be taken from back end
										 // Harcoded presently
		try
		{
			String L_strMATCD,L_strBATNO;
			int i = 0;
			if(hstITMDT !=null)
				hstITMDT.clear();
			if(tblGINVL.isEditing())
				tblGINVL.getCellEditor().stopCellEditing();
			tblGINVL.setColumnSelectionInterval(0,0);
			tblGINVL.setRowSelectionInterval(0,0);
			if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					M_strSQLQRY = "Select GR_GRNNO,GR_AMDNO,gr_porno,gr_acpdt,GR_GRNDT,GR_AMDDT,GR_BOENO,GR_CNSNO,GR_PORNO,GR_VENCD,GR_VENNM,IN_DPTCD,";
					M_strSQLQRY += " GR_GINNO,GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_TRNCD,GR_TRNNM,GR_MATCD,GR_BATNO,ST_UOMCD,ST_LOCCD,ST_STKQT,";
					M_strSQLQRY += " GR_CHLQT,GR_RECQT,GR_ACPQT,GR_BILQT,GR_REJQT,GR_REJCD,GR_QLTFL,GR_DELFL,GR_LUPDT,GR_LUSBY from MM_GRMST,MM_STMST,MM_INMST";
					M_strSQLQRY += " where GR_STRTP = ST_STRTP AND GR_MATCD =  ST_MATCD and GR_CMPCD=ST_CMPCD and gr_strtp = in_strtp and gr_matcd = in_matcd and in_indno = gr_porno and IN_CMPCD = GR_CMPCD and IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(in_stsfl,'') <>'X' ";
					M_strSQLQRY += " and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + LP_STRTP + "'";
					M_strSQLQRY += " and GR_GRNTP = '" + LP_GRNTP + "'";
					M_strSQLQRY += " and GR_GRNNO = '" + LP_GRNNO + "'";
					//M_strSQLQRY += " and GR_BATNO = '" + strDFLBT_fn + "'";
					M_strSQLQRY += " and isnull(GR_STSFL,'') <> 'X'";
				}
				else
				{
					M_strSQLQRY = "Select GR_GRNNO,GR_AMDNO,gr_porno,gr_acpdt,GR_GRNDT,GR_AMDDT,GR_BOENO,GR_CNSNO,GR_PORNO,GR_VENCD,GR_VENNM,";
					M_strSQLQRY += " GR_GINNO,GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_TRNCD,GR_TRNNM,GR_MATCD,GR_BATNO,ST_UOMCD,ST_LOCCD,ST_STKQT,";
					M_strSQLQRY += " GR_CHLQT,GR_RECQT,GR_ACPQT,GR_BILQT,GR_REJQT,GR_REJCD,GR_QLTFL,GR_DELFL, GR_LUPDT,GR_LUSBY from MM_GRMST,MM_STMST";
					M_strSQLQRY += " where GR_STRTP = ST_STRTP AND GR_MATCD =  ST_MATCD AND GR_CMPCD = ST_CMPCD ";
					M_strSQLQRY += " and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + LP_STRTP + "'";
					M_strSQLQRY += " and GR_GRNTP = '" + LP_GRNTP + "'";
					M_strSQLQRY += " and GR_GRNNO = '" + LP_GRNNO + "'";
				//	M_strSQLQRY += " and GR_BATNO = '" + strDFLBT_fn + "'";
					M_strSQLQRY += " and isnull(GR_STSFL,'') <> 'X'";
				}
			}
			else
			{
				M_strSQLQRY = "Select GR_GRNNO,GR_AMDNO,gr_porno,gr_acpdt,GR_GRNDT,GR_AMDDT,GR_BOENO,GR_CNSNO,GR_PORNO,GR_VENCD,GR_VENNM,";
				M_strSQLQRY += " GR_GINNO,GR_LRYNO,GR_CHLNO,GR_CHLDT,GR_TRNCD,GR_TRNNM,GR_MATCD,GR_BATNO,ST_UOMCD,ST_LOCCD,ST_STKQT,";
				M_strSQLQRY += " GR_CHLQT,GR_RECQT,GR_ACPQT,GR_REJQT,GR_BILQT,GR_REJCD,GR_QLTFL,GR_DELFL,GR_LUPDT,GR_LUSBY from MM_GRMST,MM_STMST";
				M_strSQLQRY += " where GR_STRTP = ST_STRTP AND GR_MATCD =  ST_MATCD AND GR_CMPCD = ST_CMPCD ";
				M_strSQLQRY += " and GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + LP_STRTP + "'";
				M_strSQLQRY += " and GR_GRNTP = '" + LP_GRNTP + "'";
				M_strSQLQRY += " and GR_GRNNO = '" + LP_GRNNO + "'";
				//M_strSQLQRY += " and GR_BATNO = '" + strDFLBT_fn + "'";
				M_strSQLQRY += " and isnull(GR_STSFL,'') <> 'X'";
			}
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			
			while(M_rstRSSET.next())
			{
			    // Added on 21/11/2005 - check for partial bill passing
			    if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
				{
			        if(M_rstRSSET.getFloat("GR_BILQT") >0)
			        {
                        setMSG("Bill is Passed for this GRIN .. Can not amend",'E');	    
			            return false;
			            //clrCOMP();
			        }
				}
				if(L_FIRST)
				{
					txtGRNNO.setText(LP_GRNNO);
					strPRAMD = nvlSTRVL(M_rstRSSET.getString("GR_AMDNO"),"");
					txtAMDNO.setText(strPRAMD);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
					{
						L_rstRSSET = cl_dat.exeSQLQRY1("SELECT SPT_YSTDT from CO_SPTRN WHERE SPT_SYSCD ='MM'");
						if(L_rstRSSET !=null)
						{
							if(L_rstRSSET.next())
							{
								L_datTMPDT = L_rstRSSET.getDate("SPT_YSTDT");
								if(L_datTMPDT !=null)
								{
									L_strYSTDT = M_fmtLCDAT.format(L_datTMPDT);
								}
							}
							L_rstRSSET.close();
						}
						
						L_datTMPDT = M_rstRSSET.getDate("GR_GRNDT");
						if(L_datTMPDT !=null)
						{
							if(M_fmtLCDAT.parse(L_strYSTDT).compareTo(M_fmtLCDAT.parse(M_fmtLCDAT.format(L_datTMPDT)))>0)
							{
								// Year Start date is greater than GRIN DATE , Previous year GRIN
								setMSG("Amendment is not allowed for previous year GRIN..",'E');
								M_rstRSSET.close();
								return false;
							}
						}
					}
					L_datTMPDT = M_rstRSSET.getDate("GR_ACPDT");
					if(L_datTMPDT !=null)
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
						{
							setMSG("GRIN has already been Accepted..",'E');
							M_rstRSSET.close();
							return false;
						}
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						{
							strTEMP ="";
							int L_intTEMP = Integer.parseInt(txtAMDNO.getText().trim()) + 1;
							for(int j=0;j<2-String.valueOf(L_intTEMP).toString().length();j++)
								strTEMP +=0;
							txtAMDNO.setText(strTEMP+String.valueOf(L_intTEMP));
							txtAMDDT.setText(cl_dat.M_strLOGDT_pbst);
						}
						txtACPDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						cl_dat.M_txtUSER_pbst.setText(M_rstRSSET.getString("GR_LUSBY"));
						if(M_rstRSSET.getString("GR_LUPDT")!=null)
							cl_dat.M_txtDATE_pbst.setText(M_fmtLCDAT.format(M_rstRSSET.getDate("GR_LUPDT")));
					}
					else
					{
						txtAMDNO.setText(strPRAMD);
						txtACPDT.setText("");
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
						{
							setMSG("GRIN has not been Accepted,Modification not allowed..",'E');
							return false;
						}
						else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
							txtACPDT.setText(cl_dat.M_strLOGDT_pbst);
						L_datTMPDT = M_rstRSSET.getDate("GR_AMDDT");
						if(L_datTMPDT !=null)
							txtAMDDT.setText(M_fmtLCDAT.format(L_datTMPDT));
						else
							txtAMDDT.setText("");
					}
					L_datTMPDT = M_rstRSSET.getDate("GR_GRNDT");
					if(L_datTMPDT !=null)
						txtGRNDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					else
						txtGRNDT.setText("");
					txtPORNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""));
					txtVENCD.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENCD"),""));
					txtVENNM.setText(nvlSTRVL(M_rstRSSET.getString("GR_VENNM"),""));
					txtGINNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_GINNO"),""));
					txtLRYNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_LRYNO"),""));
					txtCHLNO.setText(nvlSTRVL(M_rstRSSET.getString("GR_CHLNO"),""));
					L_datTMPDT = M_rstRSSET.getDate("GR_CHLDT");
					if(L_datTMPDT !=null)
						txtCHLDT.setText(M_fmtLCDAT.format(L_datTMPDT));
					else
						txtCHLDT.setText("");
					txtTRNCD.setText(nvlSTRVL(M_rstRSSET.getString("GR_TRNCD"),""));
					txtTRNNM.setText(nvlSTRVL(M_rstRSSET.getString("GR_TRNNM"),""));
					L_FIRST = false;
					M_strSQLQRY = "SELECT PO_CMPDT from MM_POMST where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP ='"+strSTRTP+"'";
					M_strSQLQRY += " AND PO_PORNO ='"+txtPORNO.getText().trim() +"'";
					rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
					if(rstRSSET !=null)
					{
						if(rstRSSET.next())
						{
							L_datTMPDT = rstRSSET.getDate("PO_CMPDT");
							if(L_datTMPDT !=null)
								txtCMPDT.setText(M_fmtLCDAT.format(L_datTMPDT));
							else
								txtCMPDT.setText("");
						}
						rstRSSET.close();
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))	
					{
						if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))
						{
							if(txtCMPDT.getText().trim().length() >0)
							if(M_fmtLCDAT.parse(txtGRNDT.getText().trim()).compareTo(M_fmtLCDAT.parse(txtCMPDT.getText()))<= 0)
								strDELFL ="A";
							else
								strDELFL ="B";
						}
						else strDELFL =" ";
					}
					else strDELFL ="";
				}
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("GR_MATCD"),"");
				L_strBATNO = nvlSTRVL(M_rstRSSET.getString("GR_BATNO"),"");
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_PORNO"),""),i,TBL_INDNO);
				if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
						tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),""),i,TBL_DPTCD);
				tblGINVL.setValueAt(L_strMATCD,i,TBL_MATCD);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_UOMCD"),""),i,TBL_UOMCD);
				strCHLQT = nvlSTRVL(M_rstRSSET.getString("GR_CHLQT"),"0");
				tblGINVL.setValueAt(strCHLQT,i,TBL_CHLQT);
				strRECQT = nvlSTRVL(M_rstRSSET.getString("GR_RECQT"),"0");
				tblGINVL.setValueAt(strRECQT,i,TBL_RECQT);
				strACPQT = nvlSTRVL(M_rstRSSET.getString("GR_ACPQT"),"0");
				hstITMDT.put(L_strMATCD+L_strBATNO,strACPQT);
				strREJQT = nvlSTRVL(M_rstRSSET.getString("GR_REJQT"),"0");
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))	
				{
					strACPQT = strRECQT;
					strREJQT = "0";
				}
				tblGINVL.setValueAt(strACPQT,i,TBL_ACPQT);
				tblGINVL.setValueAt(strACPQT,i,TBL_OACPQT);
				tblGINVL.setValueAt(strREJQT,i,TBL_OREJQT);
				tblGINVL.setValueAt(strREJQT,i,TBL_REJQT);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					tblGINVL.setValueAt(Boolean.TRUE,i,TBL_CHKFL);
					tblGINVL.setValueAt(strDELFL,i,TBL_DELFL);
					tblGINVL.setValueAt(strQLTFL,i,TBL_QLTFL);
				}
				else
				{
					tblGINVL.setValueAt(Boolean.FALSE,i,TBL_CHKFL);
					tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_REJCD"),""),i,TBL_REJCD);
					tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_DELFL"),""),i,TBL_DELFL);
					tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_QLTFL"),""),i,TBL_QLTFL);
				}
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("GR_BATNO"),""),i,TBL_BATNO);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_LOCCD"),""),i,TBL_LOCCD);
				tblGINVL.setValueAt(nvlSTRVL(M_rstRSSET.getString("ST_STKQT"),"0"),i,TBL_STKQT);
				i++;
			}
			int x=0;
			hst99MAT.clear();
			tbl99ITM.clrTABLE();
			String L_strOWNST ="";
			for(i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(tblGINVL.getValueAt(i,TBL_MATCD).toString().substring(0,2).equals("99"))
					{
						hst99MAT.put(tblGINVL.getValueAt(i,TBL_MATCD).toString()+tblGINVL.getValueAt(i,TBL_BATNO).toString(),new Integer(x));
						System.out.println("99 mat");
					      tbl99ITM.setValueAt(Boolean.TRUE,x,TBL_CHKFL);
						tbl99ITM.setValueAt(tblGINVL.getValueAt(i,TBL_MATCD).toString(),x,TB1_MATCD);
						tbl99ITM.setValueAt(tblGINVL.getValueAt(i,TBL_BATNO).toString(),x,TB1_BATNO);
						tbl99ITM.setValueAt(tblGINVL.getValueAt(i,TBL_ACPQT).toString(),x,TB1_ISSQT);
						tbl99ITM.setValueAt(tblGINVL.getValueAt(i,TBL_ACPQT).toString(),x,TB1_ACPQT);
						if(x ==0)
						    L_strOWNST = "'"+tblGINVL.getValueAt(i,TBL_MATCD).toString()+"'";
						else
						    L_strOWNST += ",'"+tblGINVL.getValueAt(i,TBL_MATCD).toString()+"'";
						x++;
					}
				}
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
			System.out.println("99 mat "+L_strOWNST);
			if(L_strOWNST.trim().length() >0)
			{
    			M_rstRSSET = cl_dat.exeSQLQRY("SELECT CT_MATCD,CT_MATDS,CT_OWNBY from CO_CTMST WHERE CT_MATCD in("+L_strOWNST +")");	
    			if(M_rstRSSET !=null)
    			while(M_rstRSSET.next())
    			{
			   System.out.println(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
    			    //tbl99ITM.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),""),Integer.parseInt(hst99ITM.get(M_rstRSSET.getString("CT_MATCD")).toString()),TB1_OWNCD);
			    for(int j=0;j<tbl99ITM.getRowCount();j++)
			    {
				if(tbl99ITM.getValueAt(j,TBL_CHKFL).toString().equals("true"))
				{
					System.out.println("j=  "+ j);
				if(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),"").equals(tbl99ITM.getValueAt(j,TB1_MATCD).toString()))
				{
		    		   System.out.println(" matched at "+ j);
				    tbl99ITM.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),j,TB1_MATDS);
    				    tbl99ITM.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),""),j,TB1_OWNCD);
					
				}
				}				
			    }		
    			    //tbl99ITM.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),Integer.parseInt(hst99MAT.get(M_rstRSSET.getString("CT_MATCD")).toString()),TB1_MATDS);
    			    //tbl99ITM.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),""),Integer.parseInt(hst99MAT.get(M_rstRSSET.getString("CT_MATCD")).toString()),TB1_OWNCD);
    			}
    			if(M_rstRSSET !=null)
    			M_rstRSSET.close();
			}
			if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))  
				getPODTL(txtPORNO.getText().trim());
			
			M_strSQLQRY ="SELECT RM_REMDS from MM_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY +=" AND RM_STRTP ='"+strSTRTP+"'";
			M_strSQLQRY +=" AND RM_DOCTP  ='GR'";
			M_strSQLQRY +=" AND RM_TRNTP  ='ACP'";
			M_strSQLQRY +=" AND RM_DOCNO  ='"+txtGRNNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			{
				if(M_rstRSSET.next())
					txtREMDS.setText(nvlSTRVL(M_rstRSSET.getString("RM_REMDS"),""));
				M_rstRSSET.close();
			}
		///***
			M_strSQLQRY = "Select * from MM_CLTRN where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and CLT_GRNNO='"+txtGRNNO.getText()+"' and CLT_STSFL<>'X' order by CLT_MATCD,CLT_SRLNO";
			M_rstRSSET=cl_dat.exeSQLQRY0(M_strSQLQRY);
			//System.out.println(M_strSQLQRY);
			if(M_rstRSSET!=null)
			{
				i=0;
				while (M_rstRSSET.next())
				{
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_MATCD"),i,1);
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_SRLNO"),i,2);
					tblCYLDT.setValueAt(M_rstRSSET.getString("CLT_RFDAY"),i,3);
					i++;
				}
			}
	///***
		}
		catch(Exception e)
		{
			setMSG(e,"Child.getDATA");
		}
		//if(L_FIRST)
		//	setMSG("Record could not be found..",'E');
		//else
		//	setMSG("",'N');
		return !L_FIRST;
	}
	
	// Method to add the data in mm_tegrt table
	void exeSAVE()
	{
		String L_strACPQT ="0",L_strOACPQT ="0",L_strINDNO ="",L_strUPDQT="0";
		double L_dblDIFQT,L_dblPQTY,L_dblPOACP;
		try
		{
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			setCursor(cl_dat.M_curWTSTS_pbst);
			if(!vldDATA())
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return;
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
			{
                /*if(!cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strJBWGR_fn))				
                if(!chkDATA())
				{
					setCursor(cl_dat.M_curDFSTS_pbst);
					setMSG("Descripancies in Data ..",'E');
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}*/
				if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(!genCSHPO())
					{
						setMSG("Cash P.O. could not be generated ..",'E');
						cl_dat.M_btnSAVE_pbst.setEnabled(true);
						return;
					}
				}
			}
			cl_dat.M_flgLCUPD_pbst = true;
			strSTRTP = M_strSBSCD.substring(2,4);
			strGRNTP = String.valueOf(cmbGRNTP.getSelectedItem()).trim().substring(0,2);
			strMATTP = String.valueOf(cmbMATTP.getSelectedItem()).trim().substring(0,2);
			strGRNNO = txtGRNNO.getText().trim();
			strGRNDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtGRNDT.getText().trim()));
			if(txtACPDT.getText().trim().length()>0)
				strACPDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtACPDT.getText().trim()));
			if(txtAMDDT.getText().trim().length()>0)
				strAMDDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtAMDDT.getText().trim()));
			strPORNO = txtPORNO.getText().trim();
			strVENCD = txtVENCD.getText().trim();
			strVENNM = txtVENNM.getText().trim();
			strGINNO = txtGINNO.getText().trim();
			strLRYNO = txtLRYNO.getText().trim();
			strCHLNO = txtCHLNO.getText().trim();
			strCHLDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(txtCHLDT.getText().trim()));
			strTRNCD = txtTRNCD.getText().trim();
			strTRNNM = txtTRNNM.getText().trim();
			strPREBY = cl_dat.M_strUSRCD_pbst.trim();
			strPREDT = strGRNDT;
			LM_LUSBY = strPREBY;
			LM_LUPDT = M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst));
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
			{
				M_strSQLQRY = "Insert into MM_GRMAM select * from MM_GRMST ";
				M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
				M_strSQLQRY += " and GR_GRNTP = '" + strGRNTP + "'";
				M_strSQLQRY += " and GR_GRNNO = '" + strGRNNO + "'";
				M_strSQLQRY += " and GR_AMDNO = '" + strPRAMD + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
              // this query added on 25/10/05 as amd no. was not getting changed for 
              // all the items in GRIN  
                M_strSQLQRY = " UPDATE MM_GRMST SET GR_AMDNO ='"+txtAMDNO.getText().trim() +"'";
                M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
				M_strSQLQRY += " and GR_GRNTP = '" + strGRNTP + "'";
				M_strSQLQRY += " and GR_GRNNO = '" + strGRNNO + "'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
         	}
			for(int i=0;i<intROWCT;i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					if(Float.parseFloat(tblGINVL.getValueAt(i,TBL_RECQT).toString()) == Float.parseFloat(tblGINVL.getValueAt(i,TBL_ACPQT).toString()))
						strQLTFL ="A";
					else if(Float.parseFloat(tblGINVL.getValueAt(i,TBL_ACPQT).toString())== 0 )
							strQLTFL ="C";
					else
						strQLTFL ="B";
					strMATCD = String.valueOf(tblGINVL.getValueAt(i,TBL_MATCD));
					strBATNO = String.valueOf(tblGINVL.getValueAt(i,TBL_BATNO));
					strUOMCD = String.valueOf(tblGINVL.getValueAt(i,TBL_UOMCD));
					strCHLQT = String.valueOf(tblGINVL.getValueAt(i,TBL_CHLQT));	
					strRECQT = String.valueOf(tblGINVL.getValueAt(i,TBL_RECQT));
					strREJCD = String.valueOf(tblGINVL.getValueAt(i,TBL_REJCD));
					strACPQT = String.valueOf(tblGINVL.getValueAt(i,TBL_ACPQT));
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
					    strOACPQT = String.valueOf(tblGINVL.getValueAt(i,TBL_OACPQT));
					strREJQT = setNumberFormat(Double.parseDouble(strRECQT) - Double.parseDouble(strACPQT),3);
					strDELFL = String.valueOf(tblGINVL.getValueAt(i,TBL_DELFL));
					strLOCCD = String.valueOf(tblGINVL.getValueAt(i,TBL_LOCCD));
					M_strSQLQRY = "Update MM_GRMST set ";
					M_strSQLQRY += "GR_AMDNO = '" + txtAMDNO.getText().trim() + "',";
					if(!txtAMDNO.getText().equals("00"))
						M_strSQLQRY += "GR_AMDDT = '" + strAMDDT + "',";
					M_strSQLQRY += "GR_GRNDT = '" + strGRNDT + "',";
					M_strSQLQRY += "GR_ACPDT = '" + strACPDT + "',";
					M_strSQLQRY += "GR_GINNO = '" + strGINNO + "',";
					M_strSQLQRY += "GR_LRYNO = '" + strLRYNO + "',";
					M_strSQLQRY += "GR_CHLNO = '" + strCHLNO + "',";
					M_strSQLQRY += "GR_CHLDT = '" + strCHLDT + "',";
					M_strSQLQRY += "GR_CHLQT = " + strCHLQT + ",";
					M_strSQLQRY += "GR_PORNO = '" + strPORNO + "',";
					M_strSQLQRY += "GR_TRNCD = '" + strTRNCD + "',";
					M_strSQLQRY += "GR_TRNNM = '" + strTRNNM + "',";
					M_strSQLQRY += "GR_RECQT = " + strRECQT + ",";
					M_strSQLQRY += "GR_ACPQT = " + strACPQT + ",";
					M_strSQLQRY += "GR_REJQT = " + strREJQT + ",";
					M_strSQLQRY += "GR_REJCD = '" + strREJCD + "',";
					M_strSQLQRY += "GR_DELFL = '" + strDELFL + "',";
					M_strSQLQRY += "GR_QLTFL = '" + strQLTFL + "',";
					M_strSQLQRY += "GR_STSFL = '2',";
					M_strSQLQRY += "GR_TRNFL = '" + strTRNFL_fn + "',";
					M_strSQLQRY += "GR_LUSBY = '" + LM_LUSBY + "',";
					M_strSQLQRY += "GR_LUPDT = '" + LM_LUPDT+"'";
					if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
					{
						// iN CASE OF cASH grin , Billed Qty and Bil value should get updated.
						M_strSQLQRY += ",GR_BILQT = " + strACPQT + ",";
						M_strSQLQRY += "GR_BILVL = " + tblGINVL.getValueAt(i,TBL_PORRT);
					}
					M_strSQLQRY += " where GR_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND GR_STRTP = '" + strSTRTP + "'";
					M_strSQLQRY += " and GR_GRNTP = '" + strGRNTP + "'";
					M_strSQLQRY += " and GR_GRNNO = '" + strGRNNO + "'";
				//	M_strSQLQRY += " and GR_AMDNO = '" + strPRAMD + "'";
					M_strSQLQRY += " and GR_MATCD = '" + strMATCD + "'";
					M_strSQLQRY += " and GR_BATNO = '" + strBATNO + "'";
					// In case of amendment, if bill is passed then acpqt shoul not be less than bilqt
					if(!txtAMDNO.getText().equals("00"))
						M_strSQLQRY += " and isnull(GR_BILQT,0) <= "+strACPQT ;
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(cl_dat.M_flgLCUPD_pbst){
					M_strSQLQRY = "Update MM_WBTRN set";
					M_strSQLQRY += " WB_CHLNO = '" + strCHLNO + "',";
                    M_strSQLQRY += " WB_CHLDT = '" + strCHLDT + "',";
					M_strSQLQRY += " WB_LRYNO = '" + strLRYNO + "',";
                    M_strSQLQRY += " WB_PRTCD = '" + strVENCD + "',";
                    M_strSQLQRY += " WB_PRTDS = '" + strVENNM + "',";
					M_strSQLQRY += " WB_TRNFL = '" + strTRNFL_fn + "',";
					M_strSQLQRY += " WB_LUSBY = '" + LM_LUSBY + "',";
					M_strSQLQRY += " WB_LUPDT = '" + LM_LUPDT +"'";
					M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCTP = '" + strGINTP + "'";
					M_strSQLQRY += " and WB_DOCNO = '" + strGINNO + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					// Added on 30/10/2004 For Cylinders, After GRIN Acceptance Cylinder status flag should be 1
					int L_intROWCT = tblCYLDT.getRowCount();
					for(int k=0;k<L_intROWCT;k++)
					{
						if(tblCYLDT.getValueAt(k,TBL_CHKFL).toString().equals("true"))
						{
							if(cl_dat.M_flgLCUPD_pbst)
								cl_dat.exeSQLUPD("Update MM_CLTRN set CLT_STSFL='1' where CLT_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND CLT_STRTP='"+M_strSBSCD.substring(2,4)+"' and CLT_GRNNO='"+txtGRNNO.getText()+"' and CLT_MATCD='"+tblCYLDT.getValueAt(k,TBL_MATCD)+"' and CLT_SRLNO='"+tblCYLDT.getValueAt(k,2)+"'","setLCLUPD");
						}
					}
					String L_strSTSFL ="";
					String L_LUPDT = cl_dat.M_strLOGDT_pbst;
					L_LUPDT = L_LUPDT.substring(6) + "-" + L_LUPDT.substring(3,5) + "-" + L_LUPDT.substring(0,2);
					L_dblDIFQT =0;
					L_strACPQT = strACPQT;
					L_strOACPQT = strOACPQT;
					intINDCT =0;
					// Expression for FRC qty. included on 17/11/04  
					// IF FRCQT IS IN NEGATIVE IT MAY CAUSE PROBLEM
					M_strSQLQRY ="SELECT PO_MMSBS,PO_STRTP,PO_INDNO,PO_MATCD,PO_ACPQT,isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0) L_PQTY from MM_POMST WHERE PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(po_stsfl,'') <>'X' and PO_MMSBS ='"+strSBSCD+"'";
					M_strSQLQRY +=" AND PO_STRTP ='"+M_strSBSCD.substring(2,4) +"' and PO_MATCD ='"+strMATCD+"'";
					M_strSQLQRY +=" AND isnull(PO_STSFL,'') <>'X' ";
					M_strSQLQRY +=" AND PO_PORNO ='"+strPORNO+"'";
					M_strSQLQRY +=" ORDER BY PO_PORDT,PO_INDNO";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					L_strACPQT = strACPQT;
					L_strOACPQT = strOACPQT;
					if(M_rstRSSET !=null)
						while(M_rstRSSET.next())
						{
							intINDCT++;
							L_dblPQTY = M_rstRSSET.getDouble("L_PQTY");
							L_dblPOACP = M_rstRSSET.getDouble("PO_ACPQT");
							L_strSTSFL ="";
							if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
							{
    							if(L_dblPQTY >0)
    							{
    								///System.out.println("Pending P.O. qty found");
    								L_strINDNO = M_rstRSSET.getString("PO_INDNO");
    								if(Double.parseDouble(L_strACPQT) >0)
    								{
    									L_dblDIFQT = (L_dblPQTY - Double.parseDouble(L_strACPQT));
    									if(L_dblDIFQT >= 0) // Pending qty is greater than accepted qty, then only this indent will be updated
    									{
    										L_strUPDQT = L_strACPQT;
    										//L_strSTSFL ="C";
    									}
    									else
    									{
    										L_strUPDQT = String.valueOf(L_dblPQTY);
    										//L_strSTSFL ="H"; // Half updating
    									}
    										// update that indent no. for recqt and po for acpqt
    									M_strSQLQRY = "Update MM_POMST set";
    									M_strSQLQRY += " PO_ACPQT = isnull(PO_ACPQT,0) +" + L_strUPDQT + ",";
    									M_strSQLQRY += " PO_TRNFL = '" + strTRNFL_fn + "',";
    									M_strSQLQRY += " PO_LUSBY = '" + LM_LUSBY + "',";
    									M_strSQLQRY += " PO_LUPDT = '" + LM_LUPDT +"'";
    									if(L_strSTSFL.equals("C"))
    									{
    										M_strSQLQRY += ",PO_STSFL = 'C'";
    										//M_strSQLQRY += ","+getUSGDTL("PO",'U',"C");
    									}
    									M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '" + strSTRTP + "'";
    									M_strSQLQRY += " and PO_PORNO = '" + strPORNO + "'";
    									M_strSQLQRY += " and PO_INDNO = '" + L_strINDNO + "'";
    									M_strSQLQRY += " and PO_MATCD = '" + strMATCD + "'";
    									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    									if(cl_dat.M_flgLCUPD_pbst)
    									{
    										M_strSQLQRY ="UPDATE MM_INMST SET IN_ACPQT = isnull(IN_ACPQT,0) +"+L_strUPDQT;
    										M_strSQLQRY += " ,IN_TRNFL = '" + strTRNFL_fn + "',"+
    													   " IN_LUSBY = '" + LM_LUSBY + "',"+
    													   " IN_LUPDT = '" + LM_LUPDT +"'"+
    													   " WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+strSBSCD+"'"+
    										               " AND IN_STRTP ='"+M_strSBSCD.substring(2,4) +"' and IN_INDNO ='"+L_strINDNO+"'"+
    													  " and IN_MATCD ='"+strMATCD+"'"+
    													  " AND isnull(IN_STSFL,'') ='4'";
    										cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    									}
    									L_strACPQT = String.valueOf(Double.parseDouble(L_strACPQT)-Double.parseDouble(L_strUPDQT));
								    }
						    	}
							} // end if OPADD condition, added on 29/08/2005
							else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPMOD_pbst))
							{
								L_strINDNO = M_rstRSSET.getString("PO_INDNO");
								// If accepted quantity is increased
								if(Double.parseDouble(strACPQT) - Double.parseDouble(tblGINVL.getValueAt(i,TBL_OACPQT).toString()) >0)
								{
    								if(L_dblPQTY >0)
        							{
        								L_strINDNO = M_rstRSSET.getString("PO_INDNO");
        								if(Double.parseDouble(L_strACPQT) >0)
        								{
        									L_dblDIFQT = (L_dblPQTY - Double.parseDouble(L_strACPQT));
        									if(L_dblDIFQT >= 0) // Pending qty is greater than accepted qty, then only this indent will be updated
        									{
        										L_strUPDQT = L_strACPQT;
        										//L_strSTSFL ="C";
        									}
        									else
        									{
        										L_strUPDQT = String.valueOf(L_dblPQTY);
        										//L_strSTSFL ="H"; // Half updating
        									}
        										// update that indent no. for recqt and po for acpqt
        									M_strSQLQRY = "Update MM_POMST set";
        									M_strSQLQRY += " PO_ACPQT = isnull(PO_ACPQT,0) +" + L_strUPDQT + ",";
        									M_strSQLQRY += " PO_TRNFL = '" + strTRNFL_fn + "',";
        									M_strSQLQRY += " PO_LUSBY = '" + LM_LUSBY + "',";
        									M_strSQLQRY += " PO_LUPDT = '" + LM_LUPDT +"'";
        									if(L_strSTSFL.equals("C"))
        									{
        										M_strSQLQRY += ",PO_STSFL = 'C'";
        										//M_strSQLQRY += ","+getUSGDTL("PO",'U',"C");
        									}
        									M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '" + strSTRTP + "'";
        									M_strSQLQRY += " and PO_PORNO = '" + strPORNO + "'";
        									M_strSQLQRY += " and PO_INDNO = '" + L_strINDNO + "'";
        									M_strSQLQRY += " and PO_MATCD = '" + strMATCD + "'";
        									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        									if(cl_dat.M_flgLCUPD_pbst)
        									{
        										M_strSQLQRY ="UPDATE MM_INMST SET IN_ACPQT = isnull(IN_ACPQT,0) +"+L_strUPDQT;
        										M_strSQLQRY += " ,IN_TRNFL = '" + strTRNFL_fn + "',"+
        													   " IN_LUSBY = '" + LM_LUSBY + "',"+
        													   " IN_LUPDT = '" + LM_LUPDT +"'"+
        													   " WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+strSBSCD+"'"+
        										               " AND IN_STRTP ='"+M_strSBSCD.substring(2,4) +"' and IN_INDNO ='"+L_strINDNO+"'"+
        													  " and IN_MATCD ='"+strMATCD+"'"+
        													  " AND isnull(IN_STSFL,'') ='4'";
        										cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
        									}
        									L_strACPQT = String.valueOf(Double.parseDouble(L_strACPQT)-Double.parseDouble(L_strUPDQT));
    								    }
        							}
								} // End accepted qty is increased
								else if(Double.parseDouble(strOACPQT) - Double.parseDouble(strACPQT) > 0)
								{
								    // Accepted quantity is reduced, then difference must be subtracted from PO_ACPQT,IN_ACPQT
								    L_dblDIFQT = (Double.parseDouble(L_strOACPQT) - Double.parseDouble(L_strACPQT));
								    if(L_dblDIFQT >0)
								    {
    									if(L_dblDIFQT <= L_dblPOACP) // Differnce in acpqt is less than or equals to PO_ACPQT 
    									{
    										L_strUPDQT = String.valueOf(L_dblDIFQT);
    										//L_strSTSFL ="C";
    									}
    									else
    									{
    										L_strUPDQT = String.valueOf(L_dblPOACP);
    										//L_strSTSFL ="H"; // Half updating
    									}
    									// update that indent no. for recqt and po for acpqt
    									M_strSQLQRY = "Update MM_POMST set";
    									M_strSQLQRY += " PO_ACPQT = isnull(PO_ACPQT,0) - " + L_strUPDQT + ",";
    									M_strSQLQRY += " PO_TRNFL = '" + strTRNFL_fn + "',";
    									M_strSQLQRY += " PO_LUSBY = '" + LM_LUSBY + "',";
    									M_strSQLQRY += " PO_LUPDT = '" + LM_LUPDT +"'";
    									if(L_strSTSFL.equals("C"))
    									{
    										M_strSQLQRY += ",PO_STSFL = 'C'";
    										//M_strSQLQRY += ","+getUSGDTL("PO",'U',"C");
    									}
    									M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '" + strSTRTP + "'";
    									M_strSQLQRY += " and PO_PORNO = '" + strPORNO + "'";
    									M_strSQLQRY += " and PO_INDNO = '" + L_strINDNO + "'";
    									M_strSQLQRY += " and PO_MATCD = '" + strMATCD + "'";
    									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    									if(cl_dat.M_flgLCUPD_pbst)
    									{
    										M_strSQLQRY ="UPDATE MM_INMST SET IN_ACPQT = isnull(IN_ACPQT,0) - "+L_strUPDQT;
    										M_strSQLQRY += " ,IN_TRNFL = '" + strTRNFL_fn + "',"+
    													   " IN_LUSBY = '" + LM_LUSBY + "',"+
    													   " IN_LUPDT = '" + LM_LUPDT +"'"+
    													   " WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+strSBSCD+"'"+
    										               " AND IN_STRTP ='"+M_strSBSCD.substring(2,4) +"' and IN_INDNO ='"+L_strINDNO+"'"+
    													  " and IN_MATCD ='"+strMATCD+"'"+
    													  " AND isnull(IN_STSFL,'') ='4'";
    										cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
    									}
    									L_strACPQT = String.valueOf(Double.parseDouble(L_strACPQT)+Double.parseDouble(L_strUPDQT));
									}
								}
						    } // end if OPMOD condition, added on 29/08/2005
						}
						if(!txtAMDNO.equals("00"))
						if((!cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))&&(!cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strJBWGR_fn)))
						if(intINDCT ==0)
						{
							setMSG("PO records not found,GRIN could not be updated..",'E');
							setCursor(cl_dat.M_curDFSTS_pbst);
							cl_dat.M_flgLCUPD_pbst = false;
						}
						
						if(cl_dat.M_flgLCUPD_pbst)
						{
							// maintain a flag for location updating
							if(strLOCCD.length() >0)
							{
								M_strSQLQRY = "Update MM_STMST set";
								if(strLOCCD.length() >0)
									M_strSQLQRY += " ST_LOCCD = '" + strLOCCD + "',";
								M_strSQLQRY += " ST_TRNFL = '" + strTRNFL_fn + "',";
								M_strSQLQRY += " ST_LUSBY = '" + LM_LUSBY + "',";
								M_strSQLQRY += " ST_LUPDT = '" + LM_LUPDT +"'";
								M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MMSBS = '" + M_strSBSCD + "'";
								M_strSQLQRY += " AND ST_STRTP = '" + strSTRTP + "'";
								M_strSQLQRY += " and ST_MATCD = '" + strMATCD + "'";
									cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
							}
						}
					}
				}
			}
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))&&(txtREMDS.getText().trim().length() >0))
			{
				M_strSQLQRY = "INSERT INTO MM_RMMST(RM_CMPCD,RM_MMSBS,RM_STRTP,RM_DOCTP,RM_TRNTP,RM_REMTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES("+
							"'"+cl_dat.M_strCMPCD_pbst+"',"+
							"'"+M_strSBSCD+"',"+
							"'"+strSTRTP+"',"+
							"'GR','GR','ACP',"+    // doctp,trntp remtp
							"'"+txtGRNNO.getText().trim()+"',"+
							"'"+txtREMDS.getText().trim()+"',' ',";
				M_strSQLQRY += "'" + strTRNFL_fn + "',";
				M_strSQLQRY += "'" + LM_LUSBY + "',";
				M_strSQLQRY += "'" + LM_LUPDT +"')";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
			
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
			    setMSG("GRIN Updated successfully",'N');
			   if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
               	   {
                 		if(!gen99ISS())
			         setMSG("Error in generating 99 Auto Issue ..",'E');
                	   }
                	  clrCOMP();
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPADD_pbst))
				{
					txtACPDT.setText(M_fmtLCDAT.format(M_calLOCAL.getTime()));
				setENBL(true);
				cmbGRNTP.setEnabled(true);
				cmbMATTP.setEnabled(true);
				}
				//setMSG("GRIN Updated successfully",'N');
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			else
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG("GRIN could not be updated",'E');
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
			}
			if(M_rstRSSET!=null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
		}
	}
	// Method to get the data corresponding to Gate-In No. from Security
	private boolean getWBDTL(String LP_GINNO)
	{
		try
		{
			M_strSQLQRY = "Select WB_LRYNO,WB_MATCD,WB_MATDS,WB_CHLNO,WB_CHLDT,WB_CHLQT,";
			M_strSQLQRY += "WB_UOMQT,WB_TPRCD,WB_TPRDS,CT_UOMCD from MM_WBTRN,CO_CTMST";
			M_strSQLQRY += " where WB_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND WB_DOCNO = '" + LP_GINNO + "'";
			M_strSQLQRY += " and WB_STSFL <> 'X'";
			M_strSQLQRY += " and WB_MATCD = CT_MATCD";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
			{
				txtGINNO.setText(LP_GINNO);
				txtLRYNO.setText(M_rstRSSET.getString("WB_LRYNO"));
				tblGINVL.setValueAt(M_rstRSSET.getString("WB_MATCD"),0,TBL_MATCD);
				cl_dat.M_txtDESC_pbst.setText(M_rstRSSET.getString("WB_MATDS"));
				txtCHLNO.setText(M_rstRSSET.getString("WB_CHLNO"));
				txtCHLDT.setText(M_rstRSSET.getString("WB_CHLDT"));
				strCHLQT = nvlSTRVL(M_rstRSSET.getString("WB_CHLQT"),"0");
				tblGINVL.setValueAt(strCHLQT,0,TBL_CHLQT);
				strRECQT = nvlSTRVL(M_rstRSSET.getString("WB_UOMQT"),"0");
				tblGINVL.setValueAt(strRECQT,0,TBL_RECQT);
				txtTRNCD.setText(M_rstRSSET.getString("WB_TPRCD"));
				txtTRNNM.setText(M_rstRSSET.getString("WB_TPRDS"));
				M_rstRSSET.close();
				return true;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"getWBDTL");
		}
		return false;
	}
	
	// Method to return Material Description
	private String getMATDS(String LP_MATCD)
	{
		String L_MATDS = "";
		try
		{
			M_strSQLQRY = "Select ST_MATDS from MM_STMST";
			M_strSQLQRY += " where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD = '" + LP_MATCD + "'";
			M_strSQLQRY += " AND ST_STRTP = '" + M_strSBSCD.substring(2,4) + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET.next())
				L_MATDS = M_rstRSSET.getString("ST_MATDS");
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"getMATDS");
		}	
		return L_MATDS;
	}
	
	private void getPODTL(String P_strPORNO)
	{
		try
		{
			int i = 0;
			double L_CHLQT,L_UOMQT;
			String L_strMATCD ="",L_strBATNO="";
			M_strSQLQRY = "Select PO_MATCD,PO_UOMCD,sum(isnull(PO_PORQT,0)-isnull(PO_ACPQT,0)-isnull(PO_FRCQT,0))L_QTY ";
			M_strSQLQRY += " from MM_POMST";
			M_strSQLQRY += " where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_STRTP = '" + strSTRTP + "'";
			M_strSQLQRY += " and PO_PORNO = '" + P_strPORNO + "' ";
			/// ADDED ON 15/12/2004, AFTER ONE DESCRIPANCY OF RM PS, GR_ACPQT > PENQT
			/// ONE MORE INDENT WAS ADDED, BUT P.O. STATUS WAS 'O' 
			M_strSQLQRY += " AND isnull(po_stsfl,'') NOT IN('O','X')";
			/// END ADDED ON 15/12/2004
			M_strSQLQRY += " group by PO_MATCD,PO_UOMCD ORDER BY PO_MATCD";
			hstMATCD.clear();	
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			while(M_rstRSSET.next())
			{
				L_strMATCD = M_rstRSSET.getString("PO_MATCD");		
				hstMATCD.put(L_strMATCD,M_rstRSSET.getString("L_QTY"));
				i++;
			}
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}catch(Exception e)
		{
			setMSG(e,"getPODTL");
		}
	}
	private String getLOCCD(String P_strMATCD)
	{
		ResultSet L_rstRSSET;
		try
		{
			M_strSQLQRY = "SELECT ST_LOCCD from MM_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+strSTRTP+"'";
			M_strSQLQRY += " AND ST_MATCD ='"+P_strMATCD+"'";
			L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			if(L_rstRSSET !=null)
			if(L_rstRSSET.next())
				return nvlSTRVL(L_rstRSSET.getString("ST_LOCCD"),"");
		}
		catch(SQLException L_E)
		{
			setMSG(L_E,"Child.getLOCCD");
		}
		return "";
	}
	private class INPVF extends InputVerifier 
	{
		public boolean verify(JComponent input) 
		{
			try
			{
				if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
					return true;
				if(input == txtCHLDT)
				{
					if(M_fmtLCDAT.parse(txtCHLDT.getText().trim()).compareTo(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText()))>0)
					{							
						setMSG("Invalid Date,Should not be greater than today(DD/MM/YYYY)",'E');
						return false;
					}
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Chilid.verify");
			}
			return true;	
		}
	}
	private class TBLINPVF extends TableInputVerifier
	{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			float L_fltDIFQT =0.0F;
			if((P_intCOLID==TBL_ACPQT)&& (getSource() ==tblGINVL))
			{
			    strTEMP = tblGINVL.getValueAt(P_intROWID,TBL_ACPQT).toString();
				if(strTEMP.length()>0)
				{
					tblGINVL.setValueAt(setNumberFormat(Double.parseDouble(tblGINVL.getValueAt(P_intROWID,TBL_RECQT).toString())-Double.parseDouble(strTEMP),3),P_intROWID,TBL_REJQT);
					if(Float.parseFloat(tblGINVL.getValueAt(P_intROWID,TBL_RECQT).toString()) < Float.parseFloat(strTEMP))
					{
					       setMSG("Accepted qty. can not be greater than the Recieved qty..",'E');
					        return false;
					}
					if(cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strPORGR_fn))  
					// check with pending po qty.
						///if(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString()) < Float.parseFloat(strTEMP))
						if(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString()) < getQTY("ACP",tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()))
						{
						    if(!txtAMDNO.getText().equals("00"))
							{
							   	if(Float.parseFloat(strTEMP)> (Float.parseFloat(hstITMDT.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()+tblGINVL.getValueAt(P_intROWID,TBL_BATNO).toString()).toString())))
								{
									// if amended qty. is greater than prv .acp qty then check with pending P.O. qty
									// Modified on 09/11/2004
									L_fltDIFQT = Float.parseFloat(hstITMDT.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()+tblGINVL.getValueAt(P_intROWID,TBL_BATNO).toString()).toString())-Float.parseFloat(strTEMP);
									if(Float.parseFloat(hstMATCD.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString()) < L_fltDIFQT)
									{	
										setMSG("Accepted qty. can not be greater than the pending order qty..",'E');
										return false;
									}
									// Modified on 09/11/2004
								}
								if(Float.parseFloat(strTEMP)< (Float.parseFloat(hstITMDT.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()+tblGINVL.getValueAt(P_intROWID,TBL_BATNO).toString()).toString())))
						    	{
    						    	//nvlSTRVL(tblIVTRN.getValueAt(i,TBL_INVQT).toString(),"0")
    						    	float L_fltREJQT = Float.parseFloat(tblGINVL.getValueAt(P_intROWID,TBL_REJQT).toString());
    						    	L_fltREJQT = L_fltREJQT - Float.parseFloat(nvlSTRVL(tblGINVL.getValueAt(P_intROWID,TBL_OREJQT).toString(),"0"));
						    		System.out.println("Prv. Rej " +L_fltREJQT);
						    		if(L_fltREJQT > (Float.parseFloat(tblGINVL.getValueAt(P_intROWID,TBL_STKQT).toString())))
    								{
    									setMSG("Rejected qty. can not be greater than the Current Stock qty...",'E');
    									return false;
    								}
    						   	}
								// Problem noticed on 04/11/06, 10 qty rejected earlier,
								// later full qty 210 rejected, stock is only 200 as 200 was acepted earlier 
								/*else if(Float.parseFloat(tblGINVL.getValueAt(P_intROWID,TBL_REJQT).toString())> (Float.parseFloat(tblGINVL.getValueAt(P_intROWID,TBL_STKQT).toString())))
								{
									setMSG("Rejected qty. can not be greater than the Current Stock qty...",'E');
									return false;
								}*/
							}
							else
							{
								setMSG("Accepted qty. can not be greater than the pending order qty at .."+(P_intROWID+1),'E');
						        return false;
							}
						}
						if((hst99MAT !=null)&& hst99MAT.size() >0)
						{
						   // if(hst99MAT.containsKey((Integer)hst99MAT.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString())))
						        tbl99ITM.setValueAt(strTEMP,Integer.parseInt(hst99MAT.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()+tblGINVL.getValueAt(P_intROWID,TBL_BATNO).toString()).toString()),TB1_ISSQT);
						        tbl99ITM.setValueAt(strTEMP,Integer.parseInt(hst99MAT.get(tblGINVL.getValueAt(P_intROWID,TBL_MATCD).toString()+tblGINVL.getValueAt(P_intROWID,TBL_BATNO).toString()).toString()),TB1_ACPQT);
						}
				} 
			}
			else if((P_intCOLID==TB1_ISSQT)&& (getSource() ==tbl99ITM))
			{
			if(P_intROWID>0 && tbl99ITM.getValueAt(P_intROWID,TB1_USGCD).toString().length() == 0)
				tbl99ITM.setValueAt(tbl99ITM.getValueAt(P_intROWID-1,TB1_USGCD),P_intROWID,TB1_USGCD);
			if(P_intROWID>0 && tbl99ITM.getValueAt(P_intROWID,TB1_CCTCD).toString().length() == 0)
				tbl99ITM.setValueAt(tbl99ITM.getValueAt(P_intROWID-1,TB1_CCTCD),P_intROWID,TB1_CCTCD);
			}
			else if((P_intCOLID==TB1_CCTCD)&& (getSource() ==tbl99ITM))
			{
				strTEMP = txtCCTCD.getText().trim();
				if(strTEMP.length() == 0)
					return true;
				if(!vldCCTCD(strTEMP))
					return false;
			}
			else if((P_intCOLID==TB1_USGCD)&& (getSource() ==tbl99ITM))
			{
				strTEMP = txtUSGTP.getText().trim();
				if(strTEMP.length() == 0)
					return true;
				if(!vldUSGTP(strTEMP))
				{
					setMSG("Invalid Usage Type,Press F1 to select from the list..",'E');
					return false;
				}
			}
			return true;
		}
	}
	// commented on 03/07/07, to check for usage
	/*private boolean chkDATA()
	{
		try
		{
			int L_intMATCT =0;
			String L_strSQLQRY ="";
			M_strSQLQRY ="SELECT count(distinct st_matcd)L_CNT from MM_STMST where st_strtp ='"+M_strSBSCD.substring(2,4) +"' and st_matcd in(";
			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,0).toString().equals("true"))
				{
					if(i==0)
						L_strSQLQRY += "'"+tblGINVL.getValueAt(i,TBL_MATCD).toString() +"'";
					 else
						L_strSQLQRY += ",'"+tblGINVL.getValueAt(i,TBL_MATCD).toString() +"'";
					L_intMATCT++;
				}
			}
			System.out.println(L_intMATCT);
			M_strSQLQRY +=L_strSQLQRY;
			M_strSQLQRY +=")";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET !=null)
				if(M_rstRSSET.next())
				{
					if(M_rstRSSET.getInt("L_CNT") != L_intMATCT)
					{
						setMSG("Item not found in Stock master..",'E');
						M_rstRSSET.close();
						System.out.println("from 1");
						return false;
					}
				}
			if(!cmbGRNTP.getSelectedItem().toString().substring(0,2).equals(strCSHGR_fn))
			{
				M_strSQLQRY ="SELECT count(*)L_CNT from MM_POMST where po_strtp ='"+M_strSBSCD.substring(2,4) +"' and po_matcd in(";
				M_strSQLQRY +=L_strSQLQRY;
				M_strSQLQRY +=")";
				M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						if(M_rstRSSET.getInt("L_CNT") < L_intMATCT)
						{
							setMSG("Item not found in P.O...",'E');
							System.out.println("from 2");
							M_rstRSSET.close();
							return false;
						}
					}
					M_rstRSSET.close();
				}
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"Child.chkdata");
			return false;
		}
		return true;	
	}*/
	private boolean genPORNO()
	{
		try
		{
			String L_strPORNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01 ="";
			strSRLNO ="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"POR' and ";
		   	M_strSQLQRY += " CMT_SHRDS = 'RBCL'";  // Cash purchase local
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					M_rstRSSET.close();
				}
			}
			if(L_CHP01.trim().length() ==3)
			{
				setMSG("In use,try after some time..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return false;
			}
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_strPORNO += "0";
			L_CCSVL = L_strPORNO + L_CCSVL;
			strSRLNO = L_CCSVL;
			L_strPORNO = L_CODCD + L_CCSVL;
			strCSHPO ="";
			strCSHPO = L_strPORNO;
			txtPORNO.setText(L_strPORNO);
			txtAMDNO.setText("00");
			return true;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genINDNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
	}

	private boolean genCSHPO()
	{
		try
		{
			txtAMDNO.setText("00");
			if(!genPORNO())
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				return false;
			}
		
			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if(tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
				{
					pstmINSREC.setString(1,cl_dat.M_strCMPCD_pbst);
					pstmINSREC.setString(2,M_strSBSCD);
					pstmINSREC.setString(3,M_strSBSCD.substring(2,4));
					pstmINSREC.setString(4,strCSHGR_fn);
					pstmINSREC.setString(5,strCSHPO);
					pstmINSREC.setString(6,tblGINVL.getValueAt(i,TBL_MATCD).toString());
				   	pstmINSREC.setString(7,"");
					pstmINSREC.setString(8,M_strSBSCD.substring(2,4));
					pstmINSREC.setString(9,tblGINVL.getValueAt(i,TBL_INDNO).toString());
					strTEMP = txtGRNDT.getText().trim().substring(6,10) + "-" +  txtGRNDT.getText().trim().substring(3,5) + "-" +  txtGRNDT.getText().trim().substring(0,2);
					pstmINSREC.setDate(10,Date.valueOf(strTEMP));			//P.O.DAte
					pstmINSREC.setString(11,"00");							// Amd no.
					pstmINSREC.setString(12,"AUTO GENERATED CASH P.O. "); // short desc , auto gen cash p.o.
					pstmINSREC.setString(13,cl_dat.M_strUSRCD_pbst);
					pstmINSREC.setDate(14,Date.valueOf(strTEMP));
					strTEMP = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" +  cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" +  cl_dat.M_strLOGDT_pbst.substring(0,2);
					pstmINSREC.setDate(15,Date.valueOf(strTEMP));
					pstmINSREC.setString(16,"S");// VEntp
					pstmINSREC.setString(17,txtVENCD.getText().trim().toUpperCase());
					pstmINSREC.setString(18,"01"); // default currency code Rupee.
					pstmINSREC.setFloat(19,1); // Exchange rate
					pstmINSREC.setFloat(20,fltPORVL);
					pstmINSREC.setString(21,cl_dat.M_strUSRCD_pbst);
					strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
					pstmINSREC.setDate(22,Date.valueOf(strTEMP));
					pstmINSREC.setString(23,tblGINVL.getValueAt(i,TBL_UOMCD).toString());
					pstmINSREC.setFloat(24,1);
					if(Float.valueOf(tblGINVL.getValueAt(i,TBL_ACPQT).toString()).floatValue() >0)
						pstmINSREC.setFloat(25,Float.valueOf(tblGINVL.getValueAt(i,TBL_PORRT).toString()).floatValue()/Float.valueOf(tblGINVL.getValueAt(i,TBL_ACPQT).toString()).floatValue());
					else
						pstmINSREC.setFloat(25,0);
					pstmINSREC.setFloat(26,1);
					pstmINSREC.setFloat(27,Float.valueOf(tblGINVL.getValueAt(i,TBL_ACPQT).toString()).floatValue()); //PORQT
					pstmINSREC.setFloat(28,Float.valueOf(tblGINVL.getValueAt(i,TBL_PORRT).toString()).floatValue()); //ITMVL 	
					pstmINSREC.setString(29,"CASH "); // payment Ref.
					pstmINSREC.setString(30,tblGINVL.getValueAt(i,TBL_DPTCD).toString());
					pstmINSREC.setString(31,"0");
					pstmINSREC.setString(32,"O"); // FRESH ORDER ,ORDERED
					pstmINSREC.setString(33,cl_dat.M_strUSRCD_pbst);
					strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
					pstmINSREC.setDate(34,Date.valueOf(strTEMP));
					pstmINSREC.executeUpdate();
					// default delivery schedule
					pstmDODREC.setString(1,cl_dat.M_strCMPCD_pbst);
					pstmDODREC.setString(2,M_strSBSCD);
					pstmDODREC.setString(3,M_strSBSCD.substring(2,4));
					pstmDODREC.setString(4,strCSHGR_fn);
					pstmDODREC.setString(5,txtPORNO.getText().trim());
					pstmDODREC.setString(6,tblGINVL.getValueAt(i,TBL_MATCD).toString());
					pstmDODREC.setString(7,tblGINVL.getValueAt(i,TBL_INDNO).toString());
					strTEMP = txtGRNDT.getText().trim().substring(6,10) + "-" +  txtGRNDT.getText().trim().substring(3,5) + "-" +  txtGRNDT.getText().trim().substring(0,2);
					pstmDODREC.setDate(8,Date.valueOf(strTEMP));
					pstmDODREC.setFloat(9,Float.valueOf(tblGINVL.getValueAt(i,TBL_ACPQT).toString()).floatValue());
					pstmDODREC.setString(10,"");
					pstmDODREC.setString(11,"0");
					pstmDODREC.setString(12,"");
					pstmDODREC.setString(13,cl_dat.M_strUSRCD_pbst);
					strTEMP = cl_dat.M_txtCLKDT_pbst.getText().trim().substring(6,10) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(3,5) + "-" +  cl_dat.M_txtCLKDT_pbst.getText().trim().substring(0,2);
					pstmDODREC.setDate(14,Date.valueOf(strTEMP));
					pstmDODREC.executeUpdate();
				}
			}
			M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CCSVL = '"+strSRLNO +"',CMT_CHP01 =''";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"POR'";
			M_strSQLQRY += " AND CMT_SHRDS = 'RBCL'";
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			if(cl_dat.M_flgLCUPD_pbst)
			{
				M_strSQLQRY = "UPDATE MM_POMST SET PO_AUTBY ='"+cl_dat.M_strUSRCD_pbst+"',PO_AUTDT = current_date,PO_STSFL ='A',PO_TRNFL ='0',PO_LUPDT = CURRENT_DATE";
				M_strSQLQRY +=" where PO_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PO_MMSBS ='"+M_strSBSCD+"'";
				M_strSQLQRY +=" and PO_STRTP ='"+M_strSBSCD.substring(2,4)+"'";
				M_strSQLQRY +=" and PO_PORNO ='"+txtPORNO.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			}
		}
		catch(Exception L_E)
		{
			cl_dat.M_flgLCUPD_pbst = false;
			setMSG(L_E,"Child.genCSHPO");
			return false;
		}
		if(cl_dat.M_flgLCUPD_pbst)
			return true;
		else
		{
			setMSG("Error in generating cash P.O...",'E');
			return false;
		}
	}
	private boolean gen99ISS()
	{
	    try
	    {
	        
	        String L_strMATCD="",L_strDPTCD ="",L_strISSQT ="",L_strUSGTP,L_strCCTCD,L_strBATNO="";
	        boolean L_flgFIRST = true;
	        String L_strPOWNER ="",L_strISSLS ="";
	        strISSNO ="";
	        for(int i=0;i<tbl99ITM.getRowCount();i++)
	        {
	            if(tbl99ITM.getValueAt(i,TBL_CHKFL).toString().equals("true"))
	            {
	                setMSG("Generating the Auto Issue for 99 Items",'N');
                    L_strMATCD = tbl99ITM.getValueAt(i,TB1_MATCD).toString();
	                L_strDPTCD = tbl99ITM.getValueAt(i,TB1_OWNCD).toString();
	                L_strISSQT = tbl99ITM.getValueAt(i,TB1_ISSQT).toString();
	                L_strCCTCD = tbl99ITM.getValueAt(i,TB1_CCTCD).toString();
	                L_strUSGTP = tbl99ITM.getValueAt(i,TB1_USGCD).toString();
	    	     
	    	       if(L_flgFIRST)
	               {
	                  strISSNO = genISSNO("1"); // For Material Type 1, stores and spares
	                  L_flgFIRST = false;
	                  L_strPOWNER = L_strDPTCD;
	                  L_strISSLS = "'"+strISSNO +"'";
	                  // generating the remark
	                   M_strSQLQRY = "Insert into MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,";
        			   M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,";
        			   M_strSQLQRY += "RM_LUPDT) values (";
					   M_strSQLQRY += "'" +cl_dat.M_strCMPCD_pbst+ "',";
        			   M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
        			   M_strSQLQRY += "'IS',";
        			   M_strSQLQRY += "'1',";
        			   M_strSQLQRY += "'" + strISSNO + "',";
        			   M_strSQLQRY += "'Auto generated Issue note'," ;
        			   M_strSQLQRY += getUSGDTL("RM",'I',"")+")";
	                  cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	               }
	                   
	               if(strISSNO == null)
	               {
	                    setMSG("In Use for generating Issue no..",'E');
	                    return false;   
	               }
	             
    			   if(!L_strPOWNER.equals(L_strDPTCD))
	               {
	                    strISSNO = String.valueOf(Integer.parseInt(strISSNO)+1);
	                    L_strPOWNER = L_strDPTCD;
	                    L_strISSLS += ",'"+strISSNO +"'";
	                    // generating the remark
	                       M_strSQLQRY = "Insert into MM_RMMST(RM_CMPCD,RM_STRTP,RM_TRNTP,";
            			   M_strSQLQRY += "RM_DOCTP,RM_DOCNO,RM_REMDS,RM_TRNFL,RM_STSFL,RM_LUSBY,";
            			   M_strSQLQRY += "RM_LUPDT) values (";
            			   M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst+ "',";
						   M_strSQLQRY += "'" + M_strSBSCD.substring(2,4) + "',";
            			   M_strSQLQRY += "'IS',";
            			   M_strSQLQRY += "'1',";
            			   M_strSQLQRY += "'" + strISSNO + "',";
            			   M_strSQLQRY += "'Auto generated Issue note'," ;
            			   M_strSQLQRY += getUSGDTL("RM",'I',"")+")";
	                    cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
	               }
				    if(L_strBATNO.equals(""))
					    L_strBATNO = "COMMON";
	                M_strSQLQRY = "Insert into MM_ISMST(IS_CMPCD,IS_MMSBS,IS_STRTP,IS_ISSTP,IS_ISSNO,IS_ISSDT," +	
        			"IS_DPTCD,IS_MATCD,IS_TAGNO,IS_BATNO,IS_REQQT,IS_ISSQT," +
        			"IS_USGTP,IS_CCTCD,IS_PREBY,IS_PREDT,IS_MATTP,IS_GRNNO,";/*IS_BATNO,*/
        			M_strSQLQRY +="IS_TRNFL,IS_STSFL,IS_LUSBY,IS_LUPDT) values (" +
        			"'" + cl_dat.M_strCMPCD_pbst + "'," +
					"'" + M_strSBSCD + "'," +
        			"'" + M_strSBSCD.substring(2,4) + "'," +
        			//"'" + cmbISSTP.getSelectedItem().toString().substring(0,2) + "'," +
        			"'01',"+ // Regular Issue
        			"'" + strISSNO + "',CURRENT_DATE," +
        			"'" + L_strDPTCD + "'," +
        			"'" + L_strMATCD + "'," +
        			"'" + L_strCCTCD + "'," +
        			"'" + L_strBATNO + "'," +
        			L_strISSQT + ",0," +
        			"'" + L_strUSGTP + "'," +
        			"'" + L_strCCTCD + "'," +
        			"'" + cl_dat.M_strUSRCD_pbst + "',current_timestamp," +
        			"'1','COMMON'," ;       // Material Type 1- Stores and spares
        			/*"'COMMON',";*/   // Default BAtch
        			M_strSQLQRY +=getUSGDTL("IS",'I',"0")+")";
					if(Double.parseDouble(L_strISSQT)>0)
	        			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
               
        	    }
	        }
	        if(!L_flgFIRST) // Items present for Auto Issue
	        {
    	        M_strSQLQRY = "Update CO_CDTRN set ";
    			M_strSQLQRY += " CMT_CHP01 ='',CMT_CCSVL = '" + strISSNO.substring(3) + "',";
    			M_strSQLQRY += getUSGDTL("CMT",'U',"");
    			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
    			M_strSQLQRY += " and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"ISS'";	
    			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + "4" + "1" + "'";			
    			cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
    			M_strSQLQRY = "UPDATE MM_ISMST SET IS_ISSQT = IS_REQQT,IS_AUTDT = CURRENT_TIMESTAMP,"
                        + getUSGDTL("IS",'U',"2")
                        + " WHERE IS_MMSBS ='"+M_strSBSCD+"'"
                        + " AND IS_STRTP ='"+M_strSBSCD.substring(2,4)+"'"
                        + " AND IS_ISSNO in(" +L_strISSLS +")";
                cl_dat.exeSQLUPD(M_strSQLQRY ,"setLCLUPD");
                
                if(cl_dat.exeDBCMT("genISSNO"))
                {
                    setMSG("Generated Auto Issue nos. "+L_strISSLS,'N');
                }
                else return false;
	        }
	    }
	    catch(Exception L_E)
	    {
	        setMSG(L_E,"gen99ISS");
	        return false;
	    }
	    return true;
	}
	private String genISSNO(String P_strMATTP)
	{
		String L_ISSNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01="";// for issues;
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(2,4)+"ISS' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + "4" + P_strMATTP + "'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET != null)
			{
				if(M_rstRSSET.next())
				{
					L_CODCD = nvlSTRVL(M_rstRSSET.getString("CMT_CODCD"),"");
					L_CCSVL = nvlSTRVL(M_rstRSSET.getString("CMT_CCSVL"),"");
					L_CHP01 = nvlSTRVL(M_rstRSSET.getString("CMT_CHP01"),"");
					if(L_CHP01.trim().length() >0)
					{
						M_rstRSSET.close();
						return null;
					}
				}
				M_rstRSSET.close();
			}
			M_strSQLQRY = "Update CO_CDTRN set ";
			M_strSQLQRY += " CMT_CHP01 ='"+cl_dat.M_strUSRCD_pbst+"'";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"'";
			M_strSQLQRY += " and CMT_CGSTP = 'MM"+strSTRTP+"ISS'";	
			M_strSQLQRY += " and CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + "4" + P_strMATTP + "'";			
			cl_dat.M_flgLCUPD_pbst = true;
			cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
			L_CCSVL = String.valueOf(Integer.parseInt(L_CCSVL) + 1);
			for(int i=L_CCSVL.length(); i<5; i++)				// for padding zero(s)
				L_ISSNO += "0";
			L_CCSVL = L_ISSNO + L_CCSVL;
			L_ISSNO = L_CODCD + L_CCSVL;
			return L_ISSNO;
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"genISSNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_ISSNO;
	}
	// Validation of Usage Type
	private boolean vldUSGTP(String P_strUSGTP)
	{
		try
		{
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'MMXXUSG'";
			M_strSQLQRY += " and CMT_CODCD = '" + P_strUSGTP + "'";
	    		M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				setMSG("",'N');
				M_rstRSSET.close();			
				return true;
			}
			setMSG("Invalid Usage Type.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();			
		}
		catch(Exception L_EX)
		{
			setMSG(L_EX,"vldUSGTP");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}	
		return false;
	}
    private boolean vldCCTCD(String P_strCCTCD)
	{
		try
		{
			M_strSQLQRY = "select EQ_TAGNO from MM_EQMST";
			M_strSQLQRY +=" where EQ_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND EQ_TAGNO ='"+P_strCCTCD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY );
			if(M_rstRSSET.next())
			{
				M_rstRSSET.close();			
				return true;
			}
			setMSG("Invalid Cost Center Code.Press F1 for help",'E');
			if(M_rstRSSET != null)
				M_rstRSSET.close();
		}
		catch(Exception e)
		{
			setMSG(e,"vldMATCD");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return false;
	}
	private float getQTY(String P_strQTYTP,String P_strMATCD) 
	{
		float L_fltTOTQT =0;
		try
		{
			int L_intCOLID =0; 
			if(P_strQTYTP.equals("CHL"))
				L_intCOLID = TBL_CHLQT;
			if(P_strQTYTP.equals("RCT"))
				L_intCOLID = TBL_RECQT;
			if(P_strQTYTP.equals("ACP"))
				L_intCOLID = TBL_ACPQT;
			if(P_strQTYTP.equals("REJ"))
				L_intCOLID = TBL_REJQT;

			for(int i=0;i<tblGINVL.getRowCount();i++)
			{
				if((tblGINVL.getValueAt(i,TBL_CHKFL).toString().equals("true")) && (tblGINVL.getValueAt(i,TBL_MATCD).toString().equals(P_strMATCD)))
					if(tblGINVL.getValueAt(i,L_intCOLID).toString().length() >0)
						L_fltTOTQT += Float.parseFloat(tblGINVL.getValueAt(i,L_intCOLID).toString());
			}
			L_fltTOTQT = Float.parseFloat(setNumberFormat(L_fltTOTQT,3));
			System.out.println("Qty "+ P_strQTYTP + " "+L_fltTOTQT);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getQTY");		
		}
		return L_fltTOTQT;

	}

}	
