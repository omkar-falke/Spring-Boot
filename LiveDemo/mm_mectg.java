import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import java.util.Vector;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.sql.PreparedStatement;
import java.sql.Date;

class mm_mectg extends cl_pbase implements MouseMotionListener, FocusListener, KeyListener 
{
	private JComboBox cmbMNGRP,cmbSBGRP,cmbSSGRP,cmbLVLHD;
	private JTextField txtMNGRP,txtSBGRP,txtSSGRP,txtLVLHD;
	private JTextField txtMNGDS,txtSBGDS,txtSSGDS,txtLVHDS;
	private JTextField txtLINNO,txtLINDS,txtILDTM,txtELDTM,txtIILTM,txtIELTM;
	private JPanel pnlLNDTL,pnlCTDTL,pnlCTDEF,pnlCMBBX;
	private JTabbedPane jtpCTDTL;
	private JCheckBox chkPRTFL;
	private JCheckBox chkEDITR,chkEDITR1;
	private JRadioButton rdoMNGRP,rdoSBGRP,rdoSSGRP,rdoLVLHD;
	private cl_JTable tblCTDTL,tblLNDTL;
//	private cl_JTBL tblCTDTL,tblLNDTL;
	private ButtonGroup bgrCATDF;
	private Vector<String> vtrMNGRP;
	private boolean flgVLDFL = true;
	private int intLNRG1,intLNRG2;
	String strTEMP,strLVLDS="";
	PreparedStatement pstmINSCTT,pstmUPDCTT,pstmDELCTT;

	public java.util.Hashtable<String,String> hstEXTDT;
	final int TBL_CHKFL = 0;
	final int TBL_LINNO = 1;
	final int TBL_LINDS = 2;
	final int TBL_PRTFL = 3;

	final int TBL_MATCD = 1;
	final int TBL_MATDS = 2;
	final int TBL_LVLRF = 3;
	final int TBL_ILDTM = 4;
	final int TBL_ELDTM = 5;
	final int TBL_IILTM = 6;
	final int TBL_IELTM = 7;

	final String strDEFLV = "00"; // default level
	java.util.Hashtable<String,String> hstLNDTL = new java.util.Hashtable<String,String>();
	mm_mectg()
	{
		super(1);
		setMatrix(20,8);		
		java.awt.Color colBLUE = new java.awt.Color(63,91,167);
		pnlLNDTL = new JPanel(null);
		pnlCTDTL = new JPanel(null);
		pnlCTDEF = new JPanel(null);
		pnlCMBBX = new JPanel(null);
		jtpCTDTL=new JTabbedPane();
		jtpCTDTL.add(pnlLNDTL,"Line No. descriptions");
		jtpCTDTL.add(pnlCTDTL,"Catalog Details");
		bgrCATDF = new ButtonGroup();
		add(rdoMNGRP = new JRadioButton("Main Group",false),1,1,1,2,pnlCTDEF,'L');
		add(rdoSBGRP = new JRadioButton("Sub Group",false),1,3,1,2,pnlCTDEF,'L');
		add(rdoSSGRP = new JRadioButton("Sub-sub group",false),1,5,1,2,pnlCTDEF,'L');
		add(rdoLVLHD = new JRadioButton("Level header",false),1,7,1,1.9,pnlCTDEF,'L');	
		bgrCATDF.add(rdoMNGRP);
		bgrCATDF.add(rdoSBGRP);
		bgrCATDF.add(rdoSSGRP);
		bgrCATDF.add(rdoLVLHD);
		pnlCTDEF.setBorder(BorderFactory.createTitledBorder(null,"Catalog Definition",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),colBLUE));
		add(pnlCTDEF,1,1,2,8,this,'L');
		
		add(new JLabel("Main Group"),1,1,1,1,pnlCMBBX,'L');
		add(cmbMNGRP=new JComboBox(),1,2,1,2,pnlCMBBX,'L');
		add(txtMNGRP=new TxtLimit(2),1,4,1,0.5,pnlCMBBX,'L');
		add(txtMNGDS=new TxtLimit(60),1,8,1,4.8,pnlCMBBX,'R');
		cmbMNGRP.addMouseMotionListener(this);
		JToolTip tipMNGRP = cmbMNGRP.createToolTip();
		ToolTipManager tipMANGR = ToolTipManager.sharedInstance();
		tipMANGR.registerComponent(cmbMNGRP);
		tipMNGRP.setTipText("Test");
		add(new JLabel("Sub Group"),2,1,1,1,pnlCMBBX,'L');
		add(cmbSBGRP=new JComboBox(),2,2,1,2,pnlCMBBX,'L');
		add(txtSBGRP=new TxtLimit(2),2,4,1,0.5,pnlCMBBX,'L');
		add(txtSBGDS=new TxtLimit(60),2,8,1,4.8,pnlCMBBX,'R');
		
		add(new JLabel("Sub Sub Group"),3,1,1,1,pnlCMBBX,'L');
		add(cmbSSGRP=new JComboBox(),3,2,1,2,pnlCMBBX,'L');
		add(txtSSGRP=new TxtLimit(2),3,4,1,0.5,pnlCMBBX,'L');
		add(txtSSGDS=new TxtLimit(60),3,8,1,4.8,pnlCMBBX,'R');
		
		add(new JLabel("Level Header"),4,1,1,1,pnlCMBBX,'L');
		add(cmbLVLHD=new JComboBox(),4,2,1,2,pnlCMBBX,'L');
		add(txtLVLHD=new TxtLimit(2),4,4,1,0.5,pnlCMBBX,'L');
		add(txtLVHDS=new TxtLimit(60),4,8,1,4.8,pnlCMBBX,'R');
		add(new JLabel("Internal and External Lead Time (Indigenous)"),5,1,1,3,pnlCMBBX,'L');
		add(txtILDTM=new TxtNumLimit(3.0),5,4,1,1,pnlCMBBX,'L');
		add(txtELDTM=new TxtNumLimit(3.0),5,5,1,1,pnlCMBBX,'L');
		add(new JLabel("Imported"),5,6,1,1,pnlCMBBX,'L');
		add(txtIILTM=new TxtNumLimit(3.0),5,7,1,1,pnlCMBBX,'L');
		add(txtIELTM=new TxtNumLimit(3.0),5,8,1,1,pnlCMBBX,'L');
		
		pnlCMBBX.setBorder(BorderFactory.createTitledBorder(null,"Catalog Options",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),colBLUE));
		add(pnlCMBBX,3,1,6,8,this,'L');
		String[] L_strTB1HD = {" ","Line Number","Description","print Flag"};
		String[] L_strTB2HD = {" ","Code","Description","Level Ref.","I.LD Tm","E.LD Tm","IIL","IEL"};
		int[] L_intCOLSZ1 = {30,70,600,50};
		int[] L_intCOLSZ2 = {30,75,520,45,30,30,10,10};
		tblLNDTL = crtTBLPNL1(pnlLNDTL,L_strTB1HD,100,1,1,8,8,L_intCOLSZ1,new int[]{0,3});
		tblLNDTL.setFont(new Font("Courier New",Font.PLAIN,15));
		txtLINNO = new TxtNumLimit(2.0);
		txtLINDS = new TxtLimit(45);
		chkPRTFL = new JCheckBox();
		txtLINNO.addFocusListener(this);
		txtLINDS.addFocusListener(this);
		txtLINNO.addKeyListener(this);
		txtLINDS.addKeyListener(this);
		txtMNGRP.setInputVerifier(new inpVRFY());
		txtSBGRP.setInputVerifier(new inpVRFY());
		txtSSGRP.setInputVerifier(new inpVRFY());
		txtLVLHD.setInputVerifier(new inpVRFY());
		txtMNGDS.setInputVerifier(new inpVRFY());
		txtSBGDS.setInputVerifier(new inpVRFY());
		txtSSGDS.setInputVerifier(new inpVRFY());
		tblLNDTL.setCellEditor(TBL_LINNO,txtLINNO);
		tblLNDTL.setCellEditor(TBL_LINDS,txtLINDS);
		tblLNDTL.setCellEditor(TBL_PRTFL,chkPRTFL);
		tblCTDTL = crtTBLPNL1(pnlCTDTL,L_strTB2HD,2500,1,1,8,7.8,L_intCOLSZ2,new int[]{0});
		tblCTDTL.setFont(new Font("Courier New",Font.PLAIN,15));
		tblCTDTL.addMouseListener(this);
		tblLNDTL.addMouseListener(this);
		DefaultTableCellRenderer LM_CELLRENDER = new DefaultTableCellRenderer();
		LM_CELLRENDER.setHorizontalAlignment(JLabel.LEFT);
		tblCTDTL.getColumn(tblCTDTL.getColumnName(TBL_MATCD)).setCellRenderer(LM_CELLRENDER);
		tblLNDTL.getColumn(tblLNDTL.getColumnName(TBL_LINNO)).setCellRenderer(LM_CELLRENDER);
		add(jtpCTDTL,9,1,10,8,this,'L');
		hstEXTDT = new java.util.Hashtable<String,String>();
		getMGLST();
		try
		{
			pstmINSCTT = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"INSERT INTO CO_CTTRN(CTT_GRPCD,CTT_CODTP,CTT_MATCD,CTT_LVLNO,CTT_LINNO,CTT_MATDS,"+
					"CTT_PRTFL,CTT_STSFL,CTT_TRNFL,CTT_LUSBY,CTT_LUPDT)VALUES("+
					"?,?,?,?,?,?,?,?,?,?,?)"	
					);
			pstmUPDCTT = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"UPDATE CO_CTTRN SET CTT_MATDS = ?,CTT_PRTFL = ?,CTT_TRNFL ='0',CTT_LUSBY =?,CTT_LUPDT =? where CTT_GRPCD =? and CTT_CODTP =? and CTT_MATCD =? and CTT_LVLNO = ? and CTT_LINNO = ?");
	
			pstmDELCTT = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"UPDATE CO_CTTRN SET CTT_STSFL = 'X',CTT_TRNFL ='0',CTT_LUSBY =?,CTT_LUPDT =? where CTT_GRPCD =? and CTT_CODTP =? and CTT_MATCD =? and CTT_LVLNO = ? and CTT_LINNO = ?");
			
			cmbMNGRP.addItem("Select Main Group");			
			cmbSBGRP.addItem("Select Sub group");
			cmbSSGRP.addItem("Select Sub-sub group");	
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
		}
		//setENBL(false);
		cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		tblLNDTL.setInputVerifier(new mm_mectgTBLINVFR());		
	}
	private void getMGLST()
	{
		Vector<String> L_vtrMNGRP = new Vector<String>();
		String L_strMATCD,L_strMATDS;
		int L_intROWCT =0;
		boolean L_flgDATA = false;
		if(vtrMNGRP !=null)
			if(vtrMNGRP.size() >0)
				L_flgDATA = true;
			else
				L_flgDATA = false;
		else
		{
			vtrMNGRP = new Vector<String>();
			L_flgDATA = false;
		}
		try
		{
			M_strSQLQRY = "Select distinct CT_GRPCD,CT_MATDS,CT_LVLRF,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM from CO_CTMST WHERE ";
			M_strSQLQRY += "CT_CODTP = 'MG' ";
			M_strSQLQRY += "and CT_STSFL <> 'X' ";
			M_strSQLQRY += " order by CT_GRPCD";
			String L_strDATA ="";	
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			tblCTDTL.clrTABLE();
			tblLNDTL.clrTABLE();
			if(M_rstRSSET != null)	
			while(M_rstRSSET.next()){
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("CT_GRPCD"),"");
				L_strMATDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
				if(rdoMNGRP.isSelected())
			    {
					tblCTDTL.setValueAt(L_strMATCD,L_intROWCT,TBL_MATCD);
					tblCTDTL.setValueAt(L_strMATDS,L_intROWCT,TBL_MATDS);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),L_intROWCT,TBL_LVLRF);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),""),L_intROWCT,TBL_ILDTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),""),L_intROWCT,TBL_ELDTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),""),L_intROWCT,TBL_IILTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),""),L_intROWCT,TBL_IELTM);
				}
			  	L_strDATA =  L_strMATCD +" "+L_strMATDS;
				if(!L_flgDATA)
					vtrMNGRP.add(L_strDATA);
				L_intROWCT ++;
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"Action on catalog Options");
		}
	}
	private void getSGLST(String P_strMNGRP)
	{
		Vector<String> L_vtrMNGRP = new Vector<String>();
		String L_strSBGRP,L_strSBGDS,L_strLVLRF;
		int L_intROWCT =0;
		try
		{
			M_strSQLQRY = "Select  SUBSTRING(CT_MATCD,1,4)L_SBGRP,CT_MATDS,CT_LVLRF,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM from CO_CTMST WHERE ";
			M_strSQLQRY += "CT_GRPCD = '"+P_strMNGRP+"'";
			M_strSQLQRY += " AND CT_CODTP = 'SG' ";
			M_strSQLQRY += "and CT_STSFL <> 'X' ";
			M_strSQLQRY += " order by L_SBGRP";
			String L_strDATA ="";	
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//cmbSBGRP.removeAllItems();
			for (int i=cmbSBGRP.getItemCount()-1;i>0;i--)
				cmbSBGRP.removeItemAt(i);

			tblCTDTL.clrTABLE();
			tblLNDTL.clrTABLE();
			hstEXTDT.clear();
			if(M_rstRSSET != null)	
			while(M_rstRSSET.next()){
			  	L_strSBGRP = nvlSTRVL(M_rstRSSET.getString("L_SBGRP"),"");
				L_strSBGDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
				L_strDATA = L_strSBGRP.substring(2,4) +" "+L_strSBGDS;
				hstEXTDT.put(L_strSBGRP.substring(2,4),L_strSBGDS);
				if(rdoSBGRP.isSelected())
				{
					tblCTDTL.setValueAt(L_strSBGRP,L_intROWCT,TBL_MATCD);
					tblCTDTL.setValueAt(L_strSBGDS,L_intROWCT,TBL_MATDS);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),L_intROWCT,TBL_LVLRF);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),""),L_intROWCT,TBL_ILDTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),""),L_intROWCT,TBL_ELDTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),""),L_intROWCT,TBL_IILTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),""),L_intROWCT,TBL_IELTM);
			
				}
				else
				{
					cmbSBGRP.addItem(L_strDATA);	
				}
				L_intROWCT ++;
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getSGLST");
		}
	}
	private void getSSLST(String P_strMNGRP,String P_strSBGRP)
	{
		String L_strSSGRP,L_strSSGDS;
		int L_intROWCT =0;
		try
		{
			M_strSQLQRY = "Select  SUBSTRING(CT_MATCD,1,6)L_SSGRP,CT_MATDS,CT_LVLRF,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM from CO_CTMST WHERE ";
			M_strSQLQRY += "CT_GRPCD = '"+P_strMNGRP+"'";
			M_strSQLQRY += " AND SUBSTRING(CT_MATCD,1,4) = '"+P_strMNGRP+P_strSBGRP+"'";
			M_strSQLQRY += " AND CT_CODTP = 'SS' ";
		//	M_strSQLQRY += " AND CT_LVLRF = '0' ";
			M_strSQLQRY += "and CT_STSFL <> 'X' ";
			M_strSQLQRY += " order by L_SSGRP";
			String L_strDATA ="";	
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			//cmbSSGRP.removeAllItems();
			for (int i=cmbSSGRP.getItemCount()-1;i>0;i--)
						cmbSSGRP.removeItemAt(i);

			tblCTDTL.clrTABLE();
			tblLNDTL.clrTABLE();
			hstEXTDT.clear();
			if(M_rstRSSET != null)	
			while(M_rstRSSET.next()){
			  	L_strSSGRP = nvlSTRVL(M_rstRSSET.getString("L_SSGRP"),"");
				L_strSSGDS = nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
				L_strDATA = L_strSSGRP.substring(4,6) +" "+L_strSSGDS;
				hstEXTDT.put(L_strSSGRP.substring(4,6),L_strSSGDS);
				if(rdoSSGRP.isSelected())
				{
					tblCTDTL.setValueAt(L_strSSGRP,L_intROWCT,TBL_MATCD);
					tblCTDTL.setValueAt(L_strSSGDS,L_intROWCT,TBL_MATDS);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),L_intROWCT,TBL_LVLRF);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),""),L_intROWCT,TBL_ILDTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),""),L_intROWCT,TBL_ELDTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),""),L_intROWCT,TBL_IILTM);
					tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),""),L_intROWCT,TBL_IELTM);
			
				}
				else
				{
					cmbSSGRP.addItem(L_strDATA);	
				}
				L_intROWCT ++;
			}
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getSSLST");
		}
	}
	private void getLVLHD(String P_strMNGRP,String P_strSBGRP,String P_strSSGRP)
	{
		String L_strLVLNO,L_strLVLDS;
		int L_intROWCT =0;
		String L_strMATCD = P_strMNGRP+P_strSBGRP+P_strSSGRP ;
		try
		{
			M_strSQLQRY = "Select  CTT_LVLNO,CTT_MATDS,CTT_LINNO from CO_CTTRN WHERE ";
			M_strSQLQRY += "CTT_GRPCD = '"+P_strMNGRP+"'";
			M_strSQLQRY += " AND SUBSTRING(CTT_MATCD,1,6) = '"+L_strMATCD+"'";
			M_strSQLQRY += " AND CTT_CODTP = 'SS' ";
			M_strSQLQRY += " AND CTT_LVLNO <> '00' ";
			M_strSQLQRY += " AND CTT_LINNO = '71' ";
			M_strSQLQRY += "and isnull(CTT_STSFL,'') <> 'X' ";
			String L_strDATA ="";	
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		//	cmbSSGRP.removeAllItems();
			tblCTDTL.clrTABLE();
			tblLNDTL.clrTABLE();
			hstEXTDT.clear();
			if(M_rstRSSET != null)	
			while(M_rstRSSET.next()){
			  	L_strLVLNO = nvlSTRVL(M_rstRSSET.getString("CTT_LVLNO"),"");
				L_strLVLDS = nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),"");
			//	L_strDATA = L_strSSGRP +" "+L_strSSGDS;
			//	if(!hstEXTDT.containsKey(L_strLVLNO))
					hstEXTDT.put(L_strLVLNO,L_strLVLDS);
				if(rdoLVLHD.isSelected())
				{
					tblCTDTL.setValueAt(L_strMATCD,L_intROWCT,TBL_MATCD);
					tblCTDTL.setValueAt(L_strLVLDS,L_intROWCT,TBL_MATDS);
					tblCTDTL.setValueAt(L_strLVLNO,L_intROWCT,TBL_LVLRF);
					//tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),""),L_intROWCT,TBL_LVLRF);
				}
				else
				{
					/*if(L_intROWCT ==0)
					{
						cmbSSGRP.addItem("Select Sub-sub group");	
					}
					cmbSSGRP.addItem(L_strDATA);	*/
				}
				L_intROWCT ++;
			}
			//System.out.println(L_intROWCT);
		}
		catch(Exception L_SE)
		{
			setMSG(L_SE,"getSSLST");
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
	/*	if(M_objSOURC == cl_dat.cmbOPTN_pbst)
		{
			clrCOMP();
		}*/
		if(M_objSOURC == cmbMNGRP)
		{
			clrCOMP();
			if(cmbMNGRP.getSelectedIndex() !=0)
			{
				if(cmbMNGRP.getSelectedItem().toString().length() >0)
				{
					txtMNGRP.setText(cmbMNGRP.getSelectedItem().toString().substring(0,2));
					txtMNGDS.setText(cmbMNGRP.getSelectedItem().toString().substring(2));
				}
				tblCTDTL.clrTABLE();
				tblLNDTL.clrTABLE();
				getSGLST(txtMNGRP.getText().trim());
				jtpCTDTL.setSelectedIndex(1);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(rdoSBGRP.isSelected())
					{
						txtSBGRP.requestFocus();
						setMSG("Enter the code for Sub group..",'N');
					}
					else
						cmbSBGRP.requestFocus();
				}
			}
		}
		else if(M_objSOURC == cmbSBGRP)
		{
			clrCOMP();
			if(cmbSBGRP.getSelectedIndex() !=0)
			{
				if(cmbSBGRP.getSelectedItem().toString().length() >0)
				{
					txtSBGRP.setText(cmbSBGRP.getSelectedItem().toString().substring(0,2));
					txtSBGDS.setText(cmbSBGRP.getSelectedItem().toString().substring(2));
				}
				tblCTDTL.clrTABLE();
				tblLNDTL.clrTABLE();
				getSSLST(txtMNGRP.getText().trim(),txtSBGRP.getText().trim());
				jtpCTDTL.setSelectedIndex(1);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(rdoSSGRP.isSelected())
					{
						txtSSGRP.requestFocus();
						setMSG("Enter the code for Sub-sub group..",'N');
					}
					else
						cmbSSGRP.requestFocus();
				}
			}
		}
		else if(M_objSOURC == cmbSSGRP)
		{
			clrCOMP();
			if(cmbSSGRP.getSelectedIndex() !=0)
			{
				if(cmbSSGRP.getSelectedItem().toString().length() >0)
				{
					txtSSGRP.setText(cmbSSGRP.getSelectedItem().toString().substring(0,2));
					txtSSGDS.setText(cmbSSGRP.getSelectedItem().toString().substring(2));
				}
				getLVLHD(txtMNGRP.getText().trim(),txtSBGRP.getText().trim(),txtSSGRP.getText().trim());
				txtLVLHD.requestFocus();
				setMSG("Enter the Level number..",'N');
				/*if(rdoSSGRP.isSelected())
				{
					txtSSGRP.requestFocus();
					setMSG("Enter the code for Sub-sub group..",'N');
				}
				else
					cmbSSGRP.requestFocus();*/
			}
			
		}
		if((M_objSOURC == rdoMNGRP)&&(rdoMNGRP.isSelected()))
		{
			clrCOMP();
			setENBL(false);
			getMGLST();
			jtpCTDTL.setSelectedIndex(1);
		}
		else if(M_objSOURC == rdoSBGRP)
		{
			clrCOMP();
			setENBL(false);
			for(int i=0;i<vtrMNGRP.size();i++)
				cmbMNGRP.addItem(vtrMNGRP.elementAt(i));
			cmbMNGRP.requestFocus();
		}
		else if(M_objSOURC == rdoSSGRP)
		{
			clrCOMP();
			setENBL(false);
			for(int i=0;i<vtrMNGRP.size();i++)
				cmbMNGRP.addItem(vtrMNGRP.elementAt(i));
				cmbMNGRP.requestFocus();
		}
		else if(M_objSOURC == rdoLVLHD)
		{
			clrCOMP();
			setENBL(false);
			//cmbMNGRP.removeAllItems();
			for (int i=cmbMNGRP.getItemCount()-1;i>0;i--)
				cmbMNGRP.removeItemAt(i);
			
			for(int i=0;i<vtrMNGRP.size();i++)
				cmbMNGRP.addItem(vtrMNGRP.elementAt(i));
			cmbMNGRP.requestFocus();
			strLVLDS ="";
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtMNGRP)
			{
				txtMNGDS.requestFocus();
				setMSG("Enter the description for Main Group..",'N');
			}
			else if(M_objSOURC == txtSBGRP)
			{
				txtSBGDS.requestFocus();
				setMSG("Enter the description for Sub Group..",'N');
				
			}
			else if(M_objSOURC == txtSSGRP)
			{
				txtSSGDS.requestFocus();
				setMSG("Enter the description for Sub-sub Group..",'N');
				
			}
			else if(M_objSOURC == txtLVLHD)
			{
				if(rdoLVLHD.isSelected())
				{
					txtLVHDS.requestFocus();
					setMSG("Enter the description for Level Header..",'N');
				}
			}
			else if(M_objSOURC == txtILDTM)
			{
				txtELDTM.requestFocus();
				setMSG("Enter the External Lead Time..",'N');
				
			}
			else if(M_objSOURC == txtELDTM)
			{
				txtIILTM.requestFocus();
				setMSG("Enter the Internal Lead Time for Imported codes..",'N');
			}
			else if(M_objSOURC == txtIILTM)
			{
				txtIELTM.requestFocus();
				setMSG("Enter the External Lead Time for Imported codes..",'N');
				
			}
			else if(M_objSOURC == txtIELTM)
			{
				jtpCTDTL.setSelectedIndex(0);
				tblLNDTL.setRowSelectionInterval(0,0);
				tblLNDTL.setColumnSelectionInterval(TBL_LINNO,TBL_LINNO);
				tblLNDTL.setEditingRow(0);
				tblLNDTL.setEditingColumn(TBL_LINNO);
				tblLNDTL.editCellAt(0,TBL_LINNO);
				tblLNDTL.requestFocus();
				setMSG("Enter the Line number details if any...",'N');	
				
			}
			if(M_objSOURC == txtMNGDS)
			{
				txtMNGDS.setText(txtMNGDS.getText().toUpperCase());
				txtLVLHD.setText(strDEFLV);
				txtILDTM.requestFocus();
			}
			else if(M_objSOURC == txtSBGDS)
			{
				txtSBGDS.setText(txtSBGDS.getText().toUpperCase());
				txtLVLHD.setText(strDEFLV);
				txtILDTM.requestFocus();
			}
			
			else if(M_objSOURC == txtSSGDS)
			{
				txtSSGDS.setText(txtSSGDS.getText().toUpperCase());
				txtLVLHD.setText(strDEFLV);
				txtILDTM.requestFocus();
			}
			else if(M_objSOURC == txtLVLHD)
			{
				if(rdoLVLHD.isSelected())
				{
					txtLVHDS.requestFocus();
					setMSG("Enter the level header description..",'N');
				}
				else
				{
					jtpCTDTL.setSelectedIndex(0);
					tblLNDTL.setRowSelectionInterval(0,0);
					tblLNDTL.setColumnSelectionInterval(TBL_LINNO,TBL_LINNO);
					tblLNDTL.setEditingRow(0);
					tblLNDTL.setEditingColumn(TBL_LINNO);
					tblLNDTL.editCellAt(0,TBL_LINNO);
					tblLNDTL.requestFocus();
				}
				
				//tblLNDTL.setValueAt(genLINNO(),getSelectedRow(),TBL_LINNO);
			}
			//((JComponent)M_objSOURC).transferFocus();
		}
	}
	void clrCOMP()
	{
		if((M_objSOURC == rdoMNGRP)||(M_objSOURC == rdoSBGRP)||(M_objSOURC == rdoSSGRP)||(M_objSOURC == rdoLVLHD)||(M_objSOURC == cl_dat.M_cmbOPTN_pbst)||(M_objSOURC == cl_dat.M_btnUNDO_pbst))
		{
			//cmbMNGRP.removeAllItems();
			//cmbSBGRP.removeAllItems();
			//cmbSSGRP.removeAllItems();
			//cmbLVLHD.removeAllItems();
			for (int i=cmbMNGRP.getItemCount()-1;i>0;i--)
						cmbMNGRP.removeItemAt(i);
			for (int i=cmbSBGRP.getItemCount()-1;i>0;i--)
						cmbSBGRP.removeItemAt(i);
			for (int i=cmbSSGRP.getItemCount()-1;i>0;i--)
						cmbSSGRP.removeItemAt(i);
			for (int i=cmbLVLHD.getItemCount()-1;i>0;i--)
						cmbLVLHD.removeItemAt(i);
			txtMNGRP.setText("");
			txtMNGDS.setText("");
			txtSBGRP.setText("");
			txtSSGRP.setText("");
			txtLVLHD.setText("");
			txtSBGDS.setText("");
			txtSSGDS.setText("");
			txtLVHDS.setText("");
			txtILDTM.setText("");
			txtELDTM.setText("");
			txtIILTM.setText("");
			txtIELTM.setText("");
		}
		else if(M_objSOURC == cmbMNGRP)
		{
			//cmbSBGRP.removeAllItems();
			//cmbSSGRP.removeAllItems();
			//cmbLVLHD.removeAllItems();
			for (int i=cmbSBGRP.getItemCount()-1;i>0;i--)
				cmbSBGRP.removeItemAt(i);
			for (int i=cmbSSGRP.getItemCount()-1;i>0;i--)
				cmbSSGRP.removeItemAt(i);
			for (int i=cmbLVLHD.getItemCount()-1;i>0;i--)
				cmbLVLHD.removeItemAt(i);
			
			txtSBGRP.setText("");
			txtSSGRP.setText("");
			txtLVLHD.setText("");
			txtSBGDS.setText("");
			txtSSGDS.setText("");
			txtLVHDS.setText("");
	
		}
		else if(M_objSOURC == cmbSBGRP)
		{
			//cmbSSGRP.removeAllItems();
			//cmbLVLHD.removeAllItems();
			for (int i=cmbSSGRP.getItemCount()-1;i>0;i--)
				cmbSSGRP.removeItemAt(i);
			for (int i=cmbLVLHD.getItemCount()-1;i>0;i--)
				cmbLVLHD.removeItemAt(i);
			
			txtSSGRP.setText("");
			txtLVLHD.setText("");
			txtSSGDS.setText("");
			txtLVHDS.setText("");
	
		}
		else if(M_objSOURC == cmbSSGRP)
		{
			//cmbLVLHD.removeAllItems();
			for (int i=cmbLVLHD.getItemCount()-1;i>0;i--)
				cmbLVLHD.removeItemAt(i);
			
			txtLVLHD.setText("");
			txtLVHDS.setText("");
		}
		txtILDTM.setText("");
		txtELDTM.setText("");
		txtIILTM.setText("");
		txtIELTM.setText("");
		tblCTDTL.clrTABLE();
		tblLNDTL.clrTABLE();
		if(tblLNDTL.isEditing())
			tblLNDTL.getCellEditor().stopCellEditing();
		if(tblCTDTL.isEditing())
			tblCTDTL.getCellEditor().stopCellEditing();
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
	//	if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			if(rdoLVLHD.isSelected())
		    {
				txtILDTM.setEnabled(false);
				txtELDTM.setEnabled(false);
				txtIILTM.setEnabled(false);
				txtIELTM.setEnabled(false);
			}
			else
			{
				txtILDTM.setEnabled(true);
				txtELDTM.setEnabled(true);
				txtIILTM.setEnabled(true);
				txtIELTM.setEnabled(true);
			}
			if(M_objSOURC == rdoMNGRP)	
			{
				txtMNGRP.setEnabled(cl_dat.M_strCMPCD_pbst.equals("01") ? true : false);
				txtMNGDS.setEnabled(cl_dat.M_strCMPCD_pbst.equals("01") ? true : false);
				cmbMNGRP.setEnabled(false);
				cmbSBGRP.setEnabled(false);
				txtSBGRP.setEnabled(false);
				txtSBGDS.setEnabled(false);
				cmbSSGRP.setEnabled(false);
				txtSSGRP.setEnabled(false);
				txtSSGDS.setEnabled(false);
				cmbLVLHD.setEnabled(false);
				txtLVLHD.setEnabled(false);
				txtLVHDS.setEnabled(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					txtMNGRP.requestFocus();
				else
				{
					txtMNGRP.setEnabled(false);
				}
				setMSG("Enter Main Group",'N');
			}
			else if(M_objSOURC == rdoSBGRP)	
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					txtSBGRP.setEnabled(cl_dat.M_strCMPCD_pbst.equals("01") ? true : false);
				else
					txtSBGRP.setEnabled(false);
				txtSBGDS.setEnabled(cl_dat.M_strCMPCD_pbst.equals("01") ? true : false);
				cmbMNGRP.setEnabled(true);
				txtMNGRP.setEnabled(false);
				txtMNGDS.setEnabled(false);
				cmbSBGRP.setEnabled(false);
				cmbSSGRP.setEnabled(false);
				txtSSGRP.setEnabled(false);
				txtSSGDS.setEnabled(false);
				cmbLVLHD.setEnabled(false);
				txtLVLHD.setEnabled(false);
				txtLVHDS.setEnabled(false);
			}
			else if(M_objSOURC == rdoSSGRP)	
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					txtSSGRP.setEnabled(true);
				else
					txtSSGRP.setEnabled(false);
				txtSSGDS.setEnabled(true);
				cmbMNGRP.setEnabled(true);
				cmbSBGRP.setEnabled(true);
				txtMNGRP.setEnabled(false);
				txtMNGDS.setEnabled(false);
				txtSBGRP.setEnabled(false);
				txtSBGDS.setEnabled(false);
				cmbSSGRP.setEnabled(false);
				cmbLVLHD.setEnabled(false);
				txtLVLHD.setEnabled(false);
				txtLVHDS.setEnabled(false);
			}
			else if(M_objSOURC == rdoLVLHD)	
			{
				cmbMNGRP.setEnabled(true);
				cmbSBGRP.setEnabled(true);
				cmbSSGRP.setEnabled(true);
				txtMNGRP.setEnabled(false);
				txtMNGDS.setEnabled(false);
				txtSBGRP.setEnabled(false);
				txtSBGDS.setEnabled(false);
				txtSSGRP.setEnabled(false);
				txtSSGDS.setEnabled(false);
				cmbLVLHD.setEnabled(false);
				txtSSGRP.setEnabled(false);
				txtSSGDS.setEnabled(false);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					txtLVLHD.setEnabled(true);
				//	txtLVHDS.setEnabled(true);
				}
				else
				{
					txtLVLHD.setEnabled(false);
				//	txtLVHDS.setEnabled(true);
					//txtLVHDS.setEnabled(false);
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				tblLNDTL.setEnabled(false);
				chkEDITR1 = (JCheckBox)tblLNDTL.getCellEditor(0,0).getTableCellEditorComponent(tblLNDTL,"",true,0,0);
				chkEDITR1.setEnabled(true);
			}
			else
				tblLNDTL.setEnabled(true);
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		{
			if(rdoMNGRP.isSelected())
			{
				cmbMNGRP.setEnabled(false);
				cmbSBGRP.setEnabled(false);
				cmbSSGRP.setEnabled(false);
				cmbLVLHD.setEnabled(false);
			}
			else if(rdoSBGRP.isSelected())
			{
				cmbMNGRP.setEnabled(true);
				cmbSBGRP.setEnabled(false);
				cmbSSGRP.setEnabled(false);
				cmbLVLHD.setEnabled(false);
			}
			else if(rdoSSGRP.isSelected())
			{
				cmbMNGRP.setEnabled(true);
				cmbSBGRP.setEnabled(true);
				cmbSSGRP.setEnabled(false);
				cmbLVLHD.setEnabled(false);
			}
			else if(rdoLVLHD.isSelected())
			{
				cmbMNGRP.setEnabled(true);
				cmbSBGRP.setEnabled(true);
				cmbSSGRP.setEnabled(true);
				cmbLVLHD.setEnabled(false);
			}
			tblLNDTL.setEnabled(false);
		}
		
		
		tblCTDTL.setEnabled(false);
		chkEDITR = (JCheckBox)tblCTDTL.getCellEditor(0,0).getTableCellEditorComponent(tblCTDTL,"",true,0,0);
		
		chkEDITR.setEnabled(true);
		if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
		{
			rdoMNGRP.setEnabled(true);
			rdoSBGRP.setEnabled(true);
			rdoSSGRP.setEnabled(true);
			rdoLVLHD.setEnabled(true);
		}
	}
private String genLINNO()
{
	if(rdoMNGRP.isSelected())
	{
		
	}
	return "";
}
boolean vldDATA()
{
	if(rdoMNGRP.isSelected())
    {
		if(txtMNGDS.getText().trim().length() <=0)
		{
			setMSG("Enter the Description for Main group..",'E');
			txtMNGDS.requestFocus();
			return false;
		}
	}
	else if(rdoSBGRP.isSelected())
    {
		if(txtSBGDS.getText().trim().length() <=0)
		{
			setMSG("Enter the Description for Sub group..",'E');
			txtSBGDS.requestFocus();
			return false;
		}
	}
	if(rdoSSGRP.isSelected())
    {
		if(txtSSGDS.getText().trim().length() <=0)
		{
			setMSG("Enter the Description for Sub-sub group..",'E');
			txtSSGDS.requestFocus();
			return false;
		}
		if(txtILDTM.getText().trim().length() ==0)
		{
			setMSG("Enter Internal Lead Time for Indigenous codes..",'E');
			return false;
		}
		else if(txtELDTM.getText().trim().length() ==0)
		{
			setMSG("Enter External Lead Time Indigenous codes..",'E');
			return false;
		}
		else if(txtIILTM.getText().trim().length() ==0)
		{
			setMSG("Enter Internal Lead Time for Imported codes..",'E');
			return false;
		}
		else if(txtIELTM.getText().trim().length() ==0)
		{
			setMSG("Enter External Lead Time Imported codes..",'E');
			return false;
		}
		// For sub sub group 0 lead times are not allowed
		if(Double.parseDouble(txtILDTM.getText().trim()) ==0)
		{
			setMSG("Enter Internal Lead Time for Indigenous codes..",'E');
			return false;
		}
		if(Double.parseDouble(txtELDTM.getText().trim()) ==0)
		{
			setMSG("Enter External Lead Time for Indigenous codes..",'E');
			return false;
		}
		if(Double.parseDouble(txtIILTM.getText().trim()) ==0)
		{
			setMSG("Enter Internal Lead Time for Imported codes..",'E');
			return false;
		}
		if(Double.parseDouble(txtIELTM.getText().trim()) ==0)
		{
			setMSG("Enter External Lead Time for Imported codes..",'E');
			return false;
		}
	}
	
    
	if(rdoLVLHD.isSelected())
    {
		/*if(txtLVHDS.getText().trim().length() >0)
			return true;
		else
		{
			setMSG("Enter the Description for Line 71..",'E');
			//txtMNGDS.requestFocus();
			return false;
		}*/
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		if(strLVLDS.trim().length() ==0)
			setMSG("Enter the level header description..",'E');
	}
	else
	{
		if(txtILDTM.getText().trim().length() ==0)
		{
			setMSG("Enter Internal Lead Time for Indigenous codes..",'E');
			return false;
		}
		else if(txtELDTM.getText().trim().length() ==0)
		{
			setMSG("Enter External Lead Time Indigenous codes..",'E');
			return false;
		}
		else if(txtIILTM.getText().trim().length() ==0)
		{
			setMSG("Enter Internal Lead Time for Imported codes..",'E');
			return false;
		}
		else if(txtIELTM.getText().trim().length() ==0)
		{
			setMSG("Enter External Lead Time Imported codes..",'E');
			return false;
		}
	}
	return true;
}
	void exeSAVE()
	{
		if(vldDATA())
		{
		
		try
		{
			String L_strORAQRY ="";
			String L_strMATCD="",L_strMATDS="",L_strCODTP="",L_strLVLRF ="";
			String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
		//	O_FOUT = new FileOutputStream("c:\\reports\\oradtr.sql");
		//	O_DOUT = new DataOutputStream(O_FOUT);
			cl_dat.M_flgLCUPD_pbst = true;
			if(rdoMNGRP.isSelected())
			{
			    L_strCODTP ="MG";
			    L_strMATCD = txtMNGRP.getText().trim() +"0000000A";
			    L_strMATDS = txtMNGDS.getText().trim();
				L_strLVLRF = strDEFLV;
			}
			else if(rdoSBGRP.isSelected())
			{
			    L_strCODTP ="SG";
			    L_strMATCD = txtMNGRP.getText().trim() +txtSBGRP.getText().trim()+"00000A";
			    L_strMATDS = txtSBGDS.getText().trim();
				L_strLVLRF = strDEFLV;
			}
			else if(rdoSSGRP.isSelected())
			{
			  L_strCODTP ="SS";
			  L_strMATCD = txtMNGRP.getText().trim() +txtSBGRP.getText().trim()+txtSSGRP.getText().trim()+"000A";
			  L_strMATDS = txtSSGDS.getText().trim();
			  L_strLVLRF = strDEFLV;  // ADDED
			}
			else if(rdoLVLHD.isSelected())
			{
			  L_strCODTP ="SS";
			  L_strMATCD = txtMNGRP.getText().trim() +txtSBGRP.getText().trim()+txtSSGRP.getText().trim()+"000A";
			  L_strMATDS = txtLVHDS.getText().trim();
			  L_strLVLRF = txtLVLHD.getText().trim();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			
			{
				if(!rdoLVLHD.isSelected())
				{
					M_strSQLQRY = "INSERT INTO CO_CTMST(CT_GRPCD,CT_CODTP,CT_DSCTP,CT_MATCD,CT_MATDS,CT_LVLRF,"+
								  "CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,CT_TRNFL,CT_STSFL,CT_LUSBY,CT_LUPDT)VALUES(";
					M_strSQLQRY += "'"+txtMNGRP.getText().trim()+"',"+
				               "'"+L_strCODTP+"','S',"+
								  "'"+L_strMATCD+"',"+
								  "'"+L_strMATDS.toUpperCase()+"',"+
								  "'"+txtLVLHD.getText().trim()+"',"+
					              txtILDTM.getText().trim()+","+
								  txtELDTM.getText().trim()+","+
								  txtIILTM.getText().trim()+","+
								  txtIELTM.getText().trim()+",";
								 M_strSQLQRY +=getUSGDTL("CT",'I'," ");
								 M_strSQLQRY +=")";
								 System.out.println(M_strSQLQRY);
								 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
				}
				// insert into co_cttrn
				for(int i=0;i<tblLNDTL.getRowCount();i++)
				{
					if(tblLNDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						pstmINSCTT.setString(1,txtMNGRP.getText().trim());
						pstmINSCTT.setString(2,L_strCODTP);
						pstmINSCTT.setString(3,L_strMATCD);
						pstmINSCTT.setString(4,L_strLVLRF);
						pstmINSCTT.setString(5,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
						pstmINSCTT.setString(6,tblLNDTL.getValueAt(i,TBL_LINDS).toString().toUpperCase());
						if(tblLNDTL.getValueAt(i,TBL_PRTFL).toString().equals("true"))
							pstmINSCTT.setString(7,"Y");
						else
							pstmINSCTT.setString(7,"N");
						pstmINSCTT.setString(8," ");
						pstmINSCTT.setString(9,"0");
						pstmINSCTT.setString(10,cl_dat.M_strUSRCD_pbst);
						pstmINSCTT.setDate(11,java.sql.Date.valueOf(L_strDATE));
						pstmINSCTT.executeUpdate();
					}
				}
			}
		    else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(!rdoLVLHD.isSelected())
				{
					for(int i=0;i<tblCTDTL.getRowCount();i++)
					{
						if(tblCTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							L_strLVLRF = (tblCTDTL.getValueAt(i,TBL_LVLRF).toString().equals("") ? "00" : tblCTDTL.getValueAt(i,TBL_LVLRF).toString());
							M_strSQLQRY ="UPDATE CO_CTMST SET ";
							M_strSQLQRY +="CT_MATDS =" + "'"+L_strMATDS.toUpperCase()+"',";
							M_strSQLQRY +="CT_ILDTM =" + txtILDTM.getText().trim()+",CT_ELDTM = "+txtELDTM.getText().trim()+",";
							M_strSQLQRY +="CT_IILTM =" + txtIILTM.getText().trim()+",CT_IELTM = "+txtIELTM.getText().trim()+",";
							M_strSQLQRY +=getUSGDTL("CT",'U',null);
							M_strSQLQRY +="where CT_GRPCD ='"+L_strMATCD.substring(0,2)+"'";
							M_strSQLQRY +=" AND CT_CODTP ='"+L_strCODTP +"'"+ " AND CT_MATCD ='"+L_strMATCD+"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
					}
				}
				for(int i=0;i<tblLNDTL.getRowCount();i++)
				{
				//	if(tblLNDTL.getValueAt(i,TBL_LINNO).toString().trim().length() >0)
				//	{
						if(tblLNDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(hstLNDTL.containsKey(tblLNDTL.getValueAt(i,TBL_LINNO).toString()))
						    {
								pstmUPDCTT.setString(1,tblLNDTL.getValueAt(i,TBL_LINDS).toString().toUpperCase());
								if(tblLNDTL.getValueAt(i,TBL_PRTFL).toString().equals("true"))
									pstmUPDCTT.setString(2,"Y");
								else
									pstmUPDCTT.setString(2,"N");
								pstmUPDCTT.setString(3,cl_dat.M_strUSRCD_pbst);
								pstmUPDCTT.setDate(4,Date.valueOf(L_strDATE));
								pstmUPDCTT.setString(5,L_strMATCD.substring(0,2));
								pstmUPDCTT.setString(6,L_strCODTP);
								pstmUPDCTT.setString(7,L_strMATCD);
								pstmUPDCTT.setString(8,L_strLVLRF);
								pstmUPDCTT.setString(9,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
								pstmUPDCTT.executeUpdate();
								pstmUPDCTT.clearParameters();
							}
							else
							{
								pstmINSCTT.setString(1,L_strMATCD.substring(0,2));
								pstmINSCTT.setString(2,L_strCODTP);
								pstmINSCTT.setString(3,L_strMATCD);
								pstmINSCTT.setString(4,L_strLVLRF);
								pstmINSCTT.setString(5,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
								pstmINSCTT.setString(6,tblLNDTL.getValueAt(i,TBL_LINDS).toString().toUpperCase());
								if(tblLNDTL.getValueAt(i,TBL_PRTFL).toString().equals("true"))
									pstmINSCTT.setString(7,"Y");
								else
									pstmINSCTT.setString(7,"N");
								pstmINSCTT.setString(8," ");
								pstmINSCTT.setString(9,"0");
								pstmINSCTT.setString(10,cl_dat.M_strUSRCD_pbst);
								pstmINSCTT.setDate(11,Date.valueOf(L_strDATE));
								pstmINSCTT.executeUpdate();
							}
							
						}
						
				//	}
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				for(int i=0;i<tblCTDTL.getRowCount();i++)
				{
					if(tblCTDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_strLVLRF = tblCTDTL.getValueAt(i,TBL_LVLRF).toString();
					}
				}
				for(int i=0;i<tblLNDTL.getRowCount();i++)
				{
					
					if(tblLNDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						
						pstmDELCTT.setString(1,cl_dat.M_strUSRCD_pbst);
						pstmDELCTT.setDate(2,Date.valueOf(L_strDATE));
						pstmDELCTT.setString(3,L_strMATCD.substring(0,2));
						pstmDELCTT.setString(4,L_strCODTP);
						pstmDELCTT.setString(5,L_strMATCD);
						pstmDELCTT.setString(6,L_strLVLRF);
						pstmDELCTT.setString(7,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
						pstmDELCTT.executeUpdate();
						pstmDELCTT.clearParameters();
					}
				}
				
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				  
				  if(L_strCODTP.equals("MG"))
					  vtrMNGRP.addElement(L_strMATCD.substring(0,2)+ " "+L_strMATDS.toUpperCase());
				//  Runtime r = Runtime.getRuntime();
				//  Process p = null;
				//	p = r.exec("O:\\LSOFT\\ORAWIN\\BIN\\PLUS33W.EXE ");
				//	p = r.exec("O:\\LSOFT\\ORAWIN\\BIN\\PLUS33W.EXE mavtest/mavtest@orcl1");				
				//	p = r.exec("O:\\LSOFT\\ORAWIN\\BIN\\PLUS33W.EXE mavtest/mavtest@orcl1 @c:\\reports\\oradtr.sql");				
				clrCOMP();
				setMSG("Record saved successfully",'N');
			}	
			else
			{
				  setMSG("Error in Updating..",'E');
			}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
		}
		}
	}
	public void mouseEntered(MouseEvent L_ME)
	{
	}
	public void mouseDragged(MouseEvent L_ME)
	{
	}
	public void mouseExited(MouseEvent L_ME)
	{
	}
	public void mousePressed(MouseEvent L_ME)
	{
	}
	public void mouseMoved(MouseEvent L_ME)
	{
		if(M_objSOURC == cmbMNGRP)
		{
			System.out.println("Mouse Moved on Main group combo");

		}
	}
	public void mouseClicked(MouseEvent L_ME)
	{
		super.mouseClicked(L_ME);
		if(M_objSOURC == tblCTDTL)
		{
			String L_strMATCD ="",L_strLVLRF="",L_strILDTM ="",L_strELDTM ="",L_strIILTM ="",L_strIELTM ="";
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			if(!rdoLVLHD.isSelected())
			{
				setMSG("Selection in Addition mode is allowed only for level header",'E');
				for(int i=0;i<tblCTDTL.getRowCount();i++)
					tblCTDTL.setValueAt(Boolean.FALSE,i,TBL_CHKFL);
				return;
			}
			else
				setMSG("",'N');
			int L_intSELROW = tblCTDTL.getSelectedRow();
			if(tblLNDTL.isEditing())
				tblLNDTL.getCellEditor().stopCellEditing();
			tblLNDTL.clearSelection();
			tblLNDTL.clrTABLE();
			if((tblCTDTL.getSelectedColumn() == TBL_CHKFL)&&(tblCTDTL.getValueAt(L_intSELROW,TBL_CHKFL).toString().equals("true")))
			{
				
				for(int i=0;i<tblCTDTL.getRowCount();i++)
				{
					if(i != L_intSELROW)
						tblCTDTL.setValueAt(Boolean.FALSE,i,TBL_CHKFL);
				}
				L_strMATCD = tblCTDTL.getValueAt(L_intSELROW,TBL_MATCD).toString();
				L_strLVLRF = tblCTDTL.getValueAt(L_intSELROW,TBL_LVLRF).toString();
				L_strILDTM = tblCTDTL.getValueAt(L_intSELROW,TBL_ILDTM).toString();
				L_strELDTM = tblCTDTL.getValueAt(L_intSELROW,TBL_ELDTM).toString();
				L_strIILTM = tblCTDTL.getValueAt(L_intSELROW,TBL_IILTM).toString();
				L_strIELTM = tblCTDTL.getValueAt(L_intSELROW,TBL_IELTM).toString();
				if(L_strMATCD.length() > 0)
				{
					if(rdoMNGRP.isSelected())
					{
						txtMNGRP.setText(L_strMATCD);
						txtMNGDS.setText(tblCTDTL.getValueAt(L_intSELROW,TBL_MATDS).toString());
						txtILDTM.setText(L_strILDTM);
						txtELDTM.setText(L_strELDTM);
						txtIILTM.setText(L_strIILTM);
						txtIELTM.setText(L_strIELTM);
						txtMNGRP.setText(L_strMATCD);
						getLINDT(L_strMATCD,"","MG");
					}
					else if(rdoSBGRP.isSelected())
					{
						txtSBGRP.setText(L_strMATCD.substring(2,4));
						txtSBGDS.setText(tblCTDTL.getValueAt(L_intSELROW,TBL_MATDS).toString());
						txtILDTM.setText(L_strILDTM);
						txtELDTM.setText(L_strELDTM);
						txtIILTM.setText(L_strIILTM);
						txtIELTM.setText(L_strIELTM);
						getLINDT(L_strMATCD,"","SG");
					}
					if(rdoSSGRP.isSelected())
					{
						txtSSGRP.setText(L_strMATCD.substring(4,6));
						txtSSGDS.setText(tblCTDTL.getValueAt(L_intSELROW,TBL_MATDS).toString());
						txtILDTM.setText(L_strILDTM);
						txtELDTM.setText(L_strELDTM);
						txtIILTM.setText(L_strIILTM);
						txtIELTM.setText(L_strIELTM);
						getLINDT(L_strMATCD,"00","SS");
					}
					if(rdoLVLHD.isSelected())
					{
						txtLVLHD.setText(L_strLVLRF);
						txtLVHDS.setText(tblCTDTL.getValueAt(L_intSELROW,TBL_MATDS).toString());
						getLINDT(L_strMATCD,L_strLVLRF,"SS");
					}
					jtpCTDTL.setSelectedIndex(0);
				}
				else
				{
					setMSG("InValid Selection ..",'E');
				}
			}
		}
	}
private void getLINDT(String P_strCODE,String P_strLVLRF,String P_strCODTP)
{
	try
	{
		int L_intROWCT =0;
		String L_strMATCD ="",L_strLINNO ="",L_strLINDS ="";
		M_strSQLQRY = "Select CTT_LINNO,CTT_MATDS,CTT_LVLNO,CTT_PRTFL from CO_CTTRN WHERE";
		M_strSQLQRY += " CTT_GRPCD = '"+P_strCODE.substring(0,2)+"' ";
		M_strSQLQRY += " AND CTT_CODTP = '"+P_strCODTP+"' ";
		if(P_strCODTP.equals("MG"))
			L_strMATCD = P_strCODE +"0000000A";
		else if(P_strCODTP.equals("SG"))
			L_strMATCD = P_strCODE +"00000A";
		else if(P_strCODTP.equals("SS"))
		{
			L_strMATCD = P_strCODE +"000A";
			M_strSQLQRY += " AND CTT_LVLNO = '"+P_strLVLRF+"'";
		}
		M_strSQLQRY += " AND CTT_MATCD = '"+L_strMATCD+"' ";
		M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
		//M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		tblLNDTL.clrTABLE();
		L_intROWCT =0;
		hstLNDTL.clear();				
		if(M_rstRSSET != null)	
		while(M_rstRSSET.next())
		{
			L_strLINNO = nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),"");
			L_strLINDS = nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),"");
			hstLNDTL.put(L_strLINNO,L_strLINDS);
			tblLNDTL.setValueAt(L_strLINNO,L_intROWCT,TBL_LINNO);
			tblLNDTL.setValueAt(L_strLINDS,L_intROWCT,TBL_LINDS);
			if(nvlSTRVL(M_rstRSSET.getString("CTT_PRTFL"),"").equals("Y"))
				tblLNDTL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_PRTFL);
			else
				tblLNDTL.setValueAt(Boolean.FALSE,L_intROWCT,TBL_PRTFL);
			L_intROWCT++;
		}
	
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getLINDT");
	}
		 
}

class inpVRFY extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		
		if(input instanceof JTextField&&((JTextField)input).getText().length()==0)
		{	
			return true;
		}
		if(input == txtMNGRP)
		{
			if(txtMNGRP.getText().trim().length() < 2)
			{
				setMSG("Please enter a two digit code..",'E');
				return false;
			}
			else if(txtMNGRP.getText().equals("00"))
			{
				setMSG("Please enter a code between 01 - 99..",'E');
				return false;
			}
			for(int i=0;i<vtrMNGRP.size();i++)
			{
				if(vtrMNGRP.elementAt(i).toString().substring(0,2).equals(txtMNGRP.getText().trim()))
				{
					setMSG("Main group already exist..Please enter some other value..",'E');
					flgVLDFL = false;
					return false;
				}
			}
		}
		else if(input == txtSBGRP)
		{
			if(txtSBGRP.getText().trim().length() < 2)
			{
				setMSG("Please enter a two digit code..",'E');
				return false;
			}
			else if(txtSBGRP.getText().equals("00"))
			{
				setMSG("Please enter a code between 01 - 99..",'E');
				return false;
			}
			if(hstEXTDT.containsKey(txtSBGRP.getText().trim()))
			{
				setMSG("Sub group already exist..Please enter some other value..",'E');
				flgVLDFL = false;
				return false;
			}
			else
			{
				return true;
			}
			
		}
		else if(input == txtSSGRP)
		{
			if(txtSSGRP.getText().trim().length() < 2)
			{
				setMSG("Please enter a two digit code..",'E');
				return false;
			}
			else if(txtSSGRP.getText().equals("00"))
			{
				setMSG("Please enter a code between 01 - 99..",'E');
				return false;
			}
			if(hstEXTDT.containsKey(txtSSGRP.getText().trim()))
			{
				setMSG("Sub sub group already exist..Please enter some other value..",'E');
				flgVLDFL = false;
				return false;
			}
			else
			{
				return true;
			}
			
		}
		else if(input == txtLVLHD)
		{
			if(txtLVLHD.getText().trim().length() < 2)
			{
				setMSG("Please enter a two digit code..",'E');
				return false;
			}
			if(txtLVLHD.getText().trim().equals("00"))
			{
				setMSG("Enter a code between 01 - 99 ..",'E');
				return false;
			}
			if(hstEXTDT.containsKey(txtLVLHD.getText().trim()))
			{
				setMSG("level header already exist..Please enter some other value..",'E');
				return false;
			}
			else
			{
				setMSG("Enter the Description ..",'N');
				//txtMNGDS.requestFocus();
				return true;
			}
			
		}
	
		else if((input == txtMNGDS)||(input == txtSBGDS)||(input == txtSSGDS))
		{
			int len = ((JTextField)input).getText().trim().length();
			String txt = ((JTextField)input).getText().trim();
			for(int i=0;i<len;i++)
			{
				if((txt.indexOf("'")>=0)||(txt.indexOf("\"")>=0)||(txt.indexOf("\\")>=0))
				{
					setMSG("Special characters are not allowed in description ",'E');
					flgVLDFL = false;
					return false;
				}
				else
				{
					//return true;
				}
			}
			
			return true;
		}
	flgVLDFL = true;
	return true;	
	}
}
private boolean vldLINNO(String P_strLINNO,int P_intROWCNT)
{
	try
	{
		if(P_strLINNO.length() <2)
		{
			setMSG("Enter a two digit code.",'E');
			return false;
		}
		if(rdoLVLHD.isSelected())
		{
			intLNRG1 = 70;
			intLNRG2 = 89;
		}
		else
		{
			intLNRG1 = 0;
			intLNRG2 = 70;
		}
		if((Integer.parseInt(P_strLINNO) >=intLNRG1)&&(Integer.parseInt(P_strLINNO) <=intLNRG2))
		{
			for(int i=0;i<P_intROWCNT-1;i++)
			{
				if(tblLNDTL.getValueAt(i,TBL_LINNO).toString().trim().equals(P_strLINNO.trim()))
				{
					setMSG("Duplicate Line number ..",'E');
					return false;
				}
			}
			if(rdoLVLHD.isSelected())
			{
				//if(P_strLINNO.equals("71"))
				//	txtLVHDS.setText(tblLNDTL.getValueAt(P_intROWCNT,TBL_LINDS).toString());
				//System.out.println("inside vldlineno "+P_strLINNO);
				//if(P_strLINNO.equals("71"))
				//	tblLNDTL.setValueAt(strLVLDS,P_intROWCNT,TBL_LINDS);
			}
		}
		else return false;
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"vlDLINNO");		
		return false;
	}
	
	return true;
}
private class mm_mectgTBLINVFR extends TableInputVerifier
{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			strTEMP = tblLNDTL.getValueAt(P_intROWID,TBL_LINNO).toString();
			if(P_intCOLID==TBL_LINNO)
			{
				if(strTEMP.length()>0)
					if(!vldLINNO(strTEMP,P_intROWID))
					return false;
			}
			///
			else if(P_intCOLID==TBL_LINDS)
			{
				strTEMP ="";
				strTEMP = tblLNDTL.getValueAt(P_intROWID,TBL_LINDS).toString();
				int len = strTEMP.trim().length();
				for(int i=0;i<len;i++)
				{
					if((strTEMP.indexOf("'")>=0)||(strTEMP.indexOf("\"")>=0)||(strTEMP.indexOf("\\")>=0))
					{
						setMSG("Special characters are not allowed in description ",'E');
						flgVLDFL = false;
						return false;
					}
				}
				if(tblLNDTL.getValueAt(P_intROWID,TBL_LINNO).toString().equals("71"))
				{
					strLVLDS = strTEMP;
				}	
			}

			///
			return true;
		}
	}

}
