import java.sql.*;
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
import javax.swing.table.DefaultTableCellRenderer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import javax.swing.JOptionPane;
class mm_mectm extends cl_pbase
{
	private JComboBox cmbMNGRP,cmbSBGRP,cmbSSGRP,cmbINDTG,cmbUOMCD,cmbDPTCD,cmbUSGBY;
	JTextField txtMATCD,txtMATDS,txtCRSRF,txtPRTNO,txtLVLRF,chkINDTG,txtDDDVL;
	private JTextField txtSTDPK,txtMPKQT;
	private JTextField txtILDTM,txtELDTM,txtPSVFR,txtPSVDT,txtSTKQT;
	private JTextField txtPPONO,txtPPORT,txtPPODT,txtPGRNO,txtPGRDT,txtPGRRT,txtPISNO,txtPISDT,txtMDVRT,txtSTMCD;
	private JTextField txtPMRNO,txtPMRDT,txtPSNNO,txtPSNDT,txtPSTNO,txtPSTDT;
	private JTextField txtLINNO,txtLINDS,txtDSBTX,txtACTDS,txtPSVCD;
    private JLabel lblITDTL;
	private JCheckBox chkTCFFL,chkINSFL,chkQARFL,chkSTKFL,chkPRTFL,chkPSVFL,chkOBSFL;
	private JCheckBox chkPROFL,chkBOTFL,chkIMPFL,chkINDFL,chkNEWCD;
	private JTabbedPane jtpCTDTL;
	private JPanel pnlGNDTL,pnlLNDTL,pnlCTDTL,pnlPRVRF,pnlCMBBX,pnlCTDEF,pnlPSVDT,pnlPRCDT,pnlPRCTP,pnlFLAGS;
	private ButtonGroup bgrCATDF;
	private String strILDTM,strELDTM,strCLSNM;
	private JList lstCTDTL;
	private JRadioButton rdoPRJIT,rdoOTMIT,rdoDESIT,rdoSTDIT;
	private boolean flgMGACT = false;
	private boolean flgSGACT = false;
	private boolean flgSSACT = false;
	private boolean flgMATCD = false;
	private boolean flgERRFL = false;
	private String strMNGRP,strSBGRP,strSSGRP,strMATCD,strTEMP;
	private cl_JTable tblLNDTL,tblCTDTL,tblPRCDT,tblPSVDT;
	private java.util.Vector<String> vtrMNGRP,vtrDPTCD;  	
	private java.util.Hashtable<String,String> hstLNDTL = new java.util.Hashtable<String,String>();
	PreparedStatement pstmINSCTT,pstmUPDCTT,pstmDELCTT;
	final String strPRJGP = "95";
	final String strOTMGP = "99";
	final String strSTDDF = "S";
	final String strDESDF = "D";
	final String str99ILD ="10";   // Default internal lead time for 99
	final String str99ELD ="20";   // Default external lead time for 99

	final int TBL_CHKFL = 0;
	final int TBL_LINNO = 1;
	final int TBL_LINDS = 2;
	final int TBL_PRTFL = 3;

	final int TBL_MATCD = 1;
	final int TBL_MATDS = 2;
	final int TBL_LVLRF = 3;

	final int TB3_LINNO = 0;
	final int TB3_LINDS = 1;
	final int TB3_LVLRF = 2;

	final int TB4_ACTSR = 1;
	final int TB4_ACTDS = 2;
	private FileOutputStream O_FOUT;
	private DataOutputStream O_DOUT;
	private mm_mectmTBLINVFR objTBLVRF;
	mm_mectm()
	{
		super(1);
		strCLSNM = getClass().getName();
		setMatrix(20,8);	
		java.awt.Color colBLUE = new java.awt.Color(63,91,167);
		pnlGNDTL = new JPanel(null);
		pnlLNDTL = new JPanel(null);
		pnlCTDTL = new JPanel(null);
		pnlCTDEF = new JPanel(null);
		pnlPRVRF = new JPanel(null);
		pnlPSVDT = new JPanel(null);
		pnlPRCDT = new JPanel(null);
		pnlPRCTP = new JPanel(null);
		pnlFLAGS = new JPanel(null);
		pnlCMBBX = new JPanel(null);
		jtpCTDTL=new JTabbedPane();
		/*jtpCTDTL.add(pnlGNDTL,"General Details");
		jtpCTDTL.add(pnlLNDTL,"Item Details");
		jtpCTDTL.add(pnlCTDTL,"Catalog Details");
		jtpCTDTL.add(pnlPRCDT,"Procurement Details ");
		jtpCTDTL.add(pnlPSVDT,"Preservation Details ");
		jtpCTDTL.add(pnlPRVRF,"History ");*/
		
		jtpCTDTL.add(pnlGNDTL,"General Details");
		jtpCTDTL.add(pnlLNDTL,"Item Details");
		jtpCTDTL.add(pnlPRCDT,"Header Details");
		jtpCTDTL.add(pnlPRVRF,"History ");
		jtpCTDTL.add(pnlPSVDT,"Preservation Details ");
	//	jtpCTDTL.add(pnlPSVDT,"Ins Details ");
		jtpCTDTL.add(pnlCTDTL,"Catalog Details");
		
		
		bgrCATDF = new ButtonGroup();
		add(rdoPRJIT = new JRadioButton("Project Items",false),1,1,1,2,pnlCTDEF,'L');
		add(rdoOTMIT = new JRadioButton("One Time Items",false),1,4,1,2,pnlCTDEF,'L');
		add(rdoDESIT = new JRadioButton("Descriptive",false),1,5,1,2,pnlCTDEF,'L');
		add(rdoSTDIT = new JRadioButton("Standard Definition",false),1,7,1,1.9,pnlCTDEF,'L');	
		bgrCATDF.add(rdoPRJIT);
		bgrCATDF.add(rdoOTMIT);
		bgrCATDF.add(rdoDESIT);
		bgrCATDF.add(rdoSTDIT);
		pnlCTDEF.setBorder(BorderFactory.createTitledBorder(null,"Catalog Definition",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),colBLUE));
		add(pnlCTDEF,1,1,2,8,this,'L');
		add(cmbMNGRP=new JComboBox(),1,1,1,3,pnlCMBBX,'L');
		add(cmbSBGRP=new JComboBox(),1,4,1,2.5,pnlCMBBX,'L');
		add(cmbSSGRP=new JComboBox(),1,8,1,2.6,pnlCMBBX,'R');
		
		pnlCMBBX.setBorder(BorderFactory.createTitledBorder(null," Main Group                                                                        Sub Group                                                           Sub Sub Group ",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),colBLUE));
		add(pnlCMBBX,3,1,2,8,this,'L');
		add(lblITDTL = new JLabel("Item Details"),5,1,1,1,this,'L');
		lblITDTL.setForeground(colBLUE);
		add(new JLabel("Level Number"),6,1,1,1,this,'L');
		add(txtLVLRF = new TxtNumLimit(2.0),6,2,1,1,this,'L');
		add(new JLabel("DDD "),6,3,1,1,this,'L');
		add(txtDDDVL = new TxtNumLimit(3.0),6,4,1,1,this,'L');
		add(new JLabel("Indicator"),6,5,1,1,this,'L');
		add(cmbINDTG = new JComboBox(),6,6,1,2.5,this,'L');
		//cmbINDTG.setToolTipText("Select the Indicator Tag");
			
		add(new JLabel("Item Code"),7,1,1,0.7,this,'L');
		add(txtMATCD = new TxtLimit(10),7,2,1,1,this,'L');
		add(new JLabel("Cross Ref"),7,3,1,1,this,'L');
		add(txtCRSRF = new TxtLimit(10),7,4,1,1,this,'L');
		add(new JLabel("Mfg. Part No."),7,5,1,1,this,'L');
		add(txtPRTNO = new TxtLimit(20),7,6,1,2,this,'L');
		
		add(new JLabel("Description"),8,1,1,1,this,'L');
		add(txtMATDS = new TxtLimit(60),8,2,1,6,this,'L');
		add(new JLabel("UOM Code"),1,1,1,0.7,pnlGNDTL,'L');
		add(cmbUOMCD = new JComboBox(),1,2,1,1.3,pnlGNDTL,'R');
		
		add(new JLabel("Dept/Owner"),1,3,1,1,pnlGNDTL,'L');
		add(cmbDPTCD = new JComboBox(),1,4,1,2,pnlGNDTL,'L');
		
		add(new JLabel("Usage By"),1,6,1,0.7,pnlGNDTL,'L');
		add(cmbUSGBY = new JComboBox(),1,7,1,1.9,pnlGNDTL,'L');
				
		add(new JLabel("Std. Pkg."),2,1,1,0.7,pnlGNDTL,'L');
		add(txtSTDPK = new TxtNumLimit(2),2,2,1,1.3,pnlGNDTL,'R');
		add(new JLabel("Min. Ord. Qty."),2,3,1,1,pnlGNDTL,'L');
		add(txtMPKQT = new TxtNumLimit(9.3),2,4,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Int.Lead Time"),2,5,1,1,pnlGNDTL,'L');
		add(txtILDTM = new TxtNumLimit(2.0),2,6,1,1,pnlGNDTL,'L');
		add(new JLabel("Ext.Lead Time"),2,7,1,1,pnlGNDTL,'L');
		add(txtELDTM = new TxtNumLimit(3.0),2,8,1,0.9,pnlGNDTL,'L');
		add(chkTCFFL = new JCheckBox("Test Certificate Tag"),1,1,1,2,pnlFLAGS,'L');
		add(chkINSFL = new JCheckBox("Inspection Tag"),1,3,1,2,pnlFLAGS,'L');
		add(chkPSVFL = new JCheckBox("Preservation Tag"),1,5,1,2,pnlFLAGS,'L');
		add(new JLabel("Frequency"),1,7,1,1,pnlFLAGS,'L');
		add(txtPSVFR = new TxtNumLimit(3.0),1,8,1,0.85,pnlFLAGS,'L');
		
		add(chkBOTFL = new JCheckBox("Bought out"),1,1,1,2,pnlPRCTP,'L');
		add(chkIMPFL = new JCheckBox("Imported"),1,3,1,2,pnlPRCTP,'L');
		add(chkINDFL = new JCheckBox("Indegineous"),1,5,1,2,pnlPRCTP,'L');
		add(chkPROFL = new JCheckBox("properiotary"),1,7,1,2,pnlPRCTP,'L');
		pnlFLAGS.setBorder(BorderFactory.createTitledBorder(null," ",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),colBLUE));
		add(pnlPRCTP,3,1,2,7.9,pnlGNDTL,'L');
		pnlPRCTP.setBorder(BorderFactory.createTitledBorder(null,"Procurement Type",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION,java.awt.Font.getFont("Times New Roman"),colBLUE));
		add(pnlFLAGS,5,1,2,7.9,pnlGNDTL,'L');
		add(chkOBSFL = new JCheckBox("Obsolete"),7,1,1,1,pnlGNDTL,'L');
		setMatrix(20,8);
		add(new JLabel("Prv. P.O / Date"),1,2,1,1,pnlPRVRF,'L');
		add(txtPPONO = new TxtLimit(8),1,3,1,1,pnlPRVRF,'L');
		add(txtPPODT = new TxtDate(),1,4,1,1,pnlPRVRF,'L');		
		add(new JLabel("Prv. P.O.Rate"),1,5,1,1,pnlPRVRF,'L');
		add(txtPPORT = new TxtNumLimit(10.2),1,6,1,1,pnlPRVRF,'L');
		
		add(new JLabel("Prv. GRIN / Date"),2,2,1,1,pnlPRVRF,'L');
		add(txtPGRNO = new TxtLimit(8),2,3,1,1,pnlPRVRF,'L');
		add(txtPGRDT = new TxtDate(),2,4,1,1,pnlPRVRF,'L');
		add(new JLabel("Prv. GRIN Rate"),2,5,1,1,pnlPRVRF,'L');
		add(txtPGRRT = new TxtNumLimit(10.2),2,6,1,1,pnlPRVRF,'L');
		
		add(new JLabel("Prv.MIN / Date"),3,2,1,1,pnlPRVRF,'L');
		add(txtPISNO = new TxtLimit(8),3,3,1,1,pnlPRVRF,'L');
		add(txtPISDT = new TxtDate(),3,4,1,1,pnlPRVRF,'L');
	
		add(new JLabel("Modvat Rate"),3,5,1,1,pnlPRVRF,'L');
		add(txtMDVRT = new TxtNumLimit(10.2),3,6,1,1,pnlPRVRF,'L');
		
		add(new JLabel("Prv.MRN / Date"),4,2,1,1,pnlPRVRF,'L');
		add(txtPMRNO = new TxtLimit(8),4,3,1,1,pnlPRVRF,'L');
		add(txtPMRDT = new TxtDate(),4,4,1,1,pnlPRVRF,'L');
		
		add(new JLabel("Stock Qty"),4,5,1,1,pnlPRVRF,'L');
		add(txtSTKQT = new TxtNumLimit(12.3),4,6,1,1,pnlPRVRF,'L');
		
		add(new JLabel("Prv.SAN / Date"),5,2,1,1,pnlPRVRF,'L');
		add(txtPSNNO = new TxtLimit(8),5,3,1,1,pnlPRVRF,'L');
		add(txtPSNDT = new TxtDate(),5,4,1,1,pnlPRVRF,'L');
		add(new JLabel("Shifted To"),5,5,1,1,pnlPRVRF,'L');
		add(txtSTMCD = new TxtLimit(2),5,6,1,1,pnlPRVRF,'L');	
	    
		add(new JLabel("Prv.STN / Date"),6,2,1,1,pnlPRVRF,'L');
		add(txtPSTNO = new TxtLimit(8),6,3,1,1,pnlPRVRF,'L');
		add(txtPSTDT = new TxtDate(),6,4,1,1,pnlPRVRF,'L');
		add(new JLabel("Preserved on"),6,5,1,0.9,pnlPRVRF,'L');
		add(txtPSVDT = new TxtDate(),6,6,1,1,pnlPRVRF,'L');
		add(chkSTKFL = new JCheckBox("Available in Stock "),7,2,1,1.5,pnlPRVRF,'L');
		add(chkQARFL = new JCheckBox("QA Applicable"),7,5,1,2,pnlPRVRF,'L');
		
		pnlPRVRF.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
		String[] L_strTB1HD = {" ","Line Number","Description","print Flag"};
		String[] L_strTB2HD = {" ","Item Code","Description","Level Ref."};
		String[] L_strTB3HD = {"Line Number","Description","Level Ref."};
		int[] L_intCOLSZ1 = {30,70,550,70};
		int[] L_intCOLSZ2 = {30,85,540,65};
		int[] L_intCOLSZ3 = {75,550,120};
		tblLNDTL = crtTBLPNL1(pnlLNDTL,L_strTB1HD,200,1,1,8.3,8,L_intCOLSZ1,new int[]{0,3});
		tblLNDTL.setFont(new Font("Courier New",Font.PLAIN,13));
		tblPRCDT = crtTBLPNL1(pnlPRCDT,L_strTB3HD,200,1,1,8.3,8,L_intCOLSZ3,new int[]{});
		tblPRCDT.setFont(new Font("Courier New",Font.PLAIN,13));
		txtLINNO = new TxtNumLimit(2.0);
		txtLINDS = new TxtLimit(45);
		chkPRTFL = new JCheckBox();
		txtLINNO.addFocusListener(this);
		txtLINDS.addFocusListener(this);
		txtLINNO.addKeyListener(this);
		txtLINDS.addKeyListener(this);
		txtMATCD.setInputVerifier(new inpVRFY());
		txtMATDS.setInputVerifier(new inpVRFY());
		txtMPKQT.setInputVerifier(new inpVRFY());
		txtLVLRF.setInputVerifier(new inpVRFY());
		txtDDDVL.setInputVerifier(new inpVRFY());
		tblLNDTL.setCellEditor(TBL_LINNO,txtLINNO);
		tblLNDTL.setCellEditor(TBL_LINDS,txtLINDS);
		tblLNDTL.setCellEditor(TBL_PRTFL,chkPRTFL);
		tblCTDTL = crtTBLPNL1(pnlCTDTL,L_strTB2HD,10000,1,1,8.3,7.8,L_intCOLSZ2,new int[]{0});
		tblCTDTL.setFont(new Font("Courier New",Font.PLAIN,13));
		tblCTDTL.addMouseListener(this);
		txtDSBTX = new JTextField();
		tblCTDTL.setCellEditor(TBL_MATCD,txtDSBTX);
		tblCTDTL.setCellEditor(TBL_MATDS,txtDSBTX);
		tblCTDTL.setCellEditor(TBL_LVLRF,txtDSBTX);
	
		DefaultTableCellRenderer LM_CELLRENDER = new DefaultTableCellRenderer();
		LM_CELLRENDER.setHorizontalAlignment(JLabel.LEFT);
		tblCTDTL.getColumn(tblCTDTL.getColumnName(TBL_MATCD)).setCellRenderer(LM_CELLRENDER);
		add(new JLabel("Preservation Code"),1,1,1,2,pnlPSVDT,'L');
		add(txtPSVCD = new TxtLimit(3),1,3,1,1,pnlPSVDT,'L');
		add(chkNEWCD = new JCheckBox("Generate Code"),1,4,1,1.5,pnlPSVDT,'L');
		String[] L_strTB4HD = {"","Srl No.","Activity"};
		int[] L_intCOLSZ4 = {40,70,620};
		tblPSVDT = crtTBLPNL1(pnlPSVDT,L_strTB4HD,8,3,1,4,7.8,L_intCOLSZ4,new int[]{0});
		tblPSVDT.setFont(new Font("Courier New",Font.PLAIN,13));
		txtACTDS = new JTextField();
		tblPSVDT.setCellEditor(TB4_ACTDS,txtACTDS);
		add(jtpCTDTL,9,1,9.7,8,this,'L');
		vtrMNGRP = getMGLST();
		try
		{
			pstmINSCTT = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"INSERT INTO CO_CTTRN(CTT_GRPCD,CTT_CODTP,CTT_MATCD,CTT_LVLNO,CTT_LINNO,CTT_MATDS,"+
					"CTT_PRTFL,CTT_STSFL,CTT_TRNFL,CTT_LUSBY,CTT_LUPDT)VALUES("+
					"?,?,?,?,?,?,?,?,?,?,?)"	
					);
			pstmUPDCTT = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"UPDATE CO_CTTRN SET CTT_MATDS = ?,CTT_PRTFL = ?,CTT_TRNFL ='0',CTT_LUSBY =?,CTT_LUPDT =? where CTT_GRPCD =? and CTT_CODTP ='CD' and CTT_MATCD =? and CTT_LVLNO = ? and CTT_LINNO = ?");
	
			pstmDELCTT = cl_dat.M_conSPDBA_pbst.prepareStatement(
					"UPDATE CO_CTTRN SET CTT_STSFL = 'X',CTT_TRNFL ='0',CTT_LUSBY =?,CTT_LUPDT =? where CTT_GRPCD =? and CTT_CODTP ='CD' and CTT_MATCD =? and CTT_LVLNO = ? and CTT_LINNO = ?");
	
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='SYS' and cmt_cgstp ='MMXXCAT' order by cmt_codcd";
			String L_strDATA ="";
			System.out.println(M_strSQLQRY);
			ResultSet M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cmbINDTG.addItem("Select Indicator");
			if(M_rstRSSET1 != null)	
			while(M_rstRSSET1.next()){
				L_strDATA = M_rstRSSET1.getString("CMT_CODCD") + " " + M_rstRSSET1.getString("CMT_CODDS");
			  	cmbINDTG.addItem(L_strDATA);
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='MST' and cmt_cgstp ='COXXUOM'";
			L_strDATA ="";
			System.out.println(M_strSQLQRY);
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			 cmbUOMCD.addItem("Select UOM");
			if(M_rstRSSET1 != null)	
			while(M_rstRSSET1.next()){
				L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET1.getString("CMT_CODCD"),""),4) +M_rstRSSET1.getString("CMT_CODDS");
			  	cmbUOMCD.addItem(L_strDATA);
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='SYS' and cmt_cgstp ='COXXDPT'";
			L_strDATA ="";
			System.out.println(M_strSQLQRY);
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET1 != null)	
			{
				vtrDPTCD = new Vector<String>();
				cmbDPTCD.addItem("Select Owner");
				cmbUSGBY.addItem("Select Usage");
				cmbUSGBY.addItem("999 Common");
				while(M_rstRSSET1.next()){
				L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET1.getString("CMT_CODCD"),""),4) +M_rstRSSET1.getString("CMT_CODDS");
			  	cmbDPTCD.addItem(L_strDATA);
				cmbUSGBY.addItem(L_strDATA);
				vtrDPTCD.add(L_strDATA);				
				}
			}
			cl_dat.M_cmbOPTN_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
			cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
			objTBLVRF = new mm_mectmTBLINVFR();
			tblLNDTL.setInputVerifier(objTBLVRF);
			
			cmbMNGRP.addItem("Select Main Group");
			cmbSBGRP.addItem("Select Sub Group");
			cmbSSGRP.addItem("Select Sub sub Group");
			
			setCursor(cl_dat.M_curDFSTS_pbst);	
		}
		
		catch(Exception L_E)
		{
			setMSG(L_E,"constructor");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		setENBL(false);
	}
	public void focusGained(FocusEvent L_FE)
	{
		if(M_objSOURC == txtACTDS)
		{
			tblPSVDT.setValueAt("0"+tblPSVDT.getSelectedRow()+1,tblPSVDT.getSelectedRow(),TB4_ACTSR);
		}
	}
	public void focusLost(FocusEvent L_FE)
	{
		super.focusLost(L_FE);
		/*if(M_objSOURC == txtLINNO)
		{
			if(vldLINNO(txtLINNO.getText().trim(),tblLNDTL.getSelectedRow()))
			{
			
			}
		}*/
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
			
				M_strSQLQRY += " where CT_CODTP IN('CD','CR') and isnull(CT_STSFL,'') <>'X'"+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) ? " and CT_STSFL = '0' ":"" );
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY +=" Order by CT_MATCD ";
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT",new int[]{100,400});
			}
			if(M_objSOURC == txtPSVCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtPSVCD";
				String L_ARRHDR[] = {"Code","Activity Srl.","Description"};
				M_strSQLQRY = "Select PV_PSVCD,PV_ACTSR,PV_ACTDS from MM_PVMST ";
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,3,"CT");
			}
			if(M_objSOURC == txtLVLRF)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtLVLRF";
				String L_ARRHDR[] = {"Level No","Description"};
				M_strSQLQRY = "Select DISTINCT CTT_LVLNO,CTT_MATDS FROM CO_CTTRN WHERE CTT_LINNO ='71' AND SUBSTRING(CTT_MATCD,1,6) = '"+cmbMNGRP.getSelectedItem().toString().substring(0,2).toString()+cmbSBGRP.getSelectedItem().toString().substring(0,2).toString()+cmbSSGRP.getSelectedItem().toString().substring(0,2).toString()+"'";
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT",new int[]{100,400});
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F2)
		{
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCDF2";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
			
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') <>'X'"+(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) ? " and CT_STSFL = '0' ":"" );
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY +=" ORDER by CT_MATDS";
				System.out.println(M_strSQLQRY);
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",new int[]{100,400});
			}
		
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtMATDS)
			{
				txtMATDS.setText(txtMATDS.getText().toUpperCase());
				if(txtILDTM.getText().trim().length() ==0)
				  	txtILDTM.setText(strILDTM);
				else if(txtELDTM.getText().trim().length() ==0)
					txtELDTM.setText(strELDTM);
				cmbUOMCD.requestFocus();
				setMSG("Select the UOM Code ..",'N');
			
			}
			else if(M_objSOURC == cmbINDTG)
			{
				if(cmbINDTG.getSelectedIndex() ==0)
				{
					setMSG("Select the Indicator tag..",'E');
				}
				else
				{
					txtCRSRF.requestFocus();
					setMSG("Enter the Cross Ref...",'N');
					
				}
				
			}
			else if(M_objSOURC == txtCRSRF)
			{
				txtPRTNO.requestFocus();
				setMSG("Enter the Mfg. Part Number..",'N');
				
			}
			else if(M_objSOURC == txtPRTNO)
			{
				txtMATDS.requestFocus();
			}
			else if(M_objSOURC == txtLVLRF)
			{
				txtDDDVL.requestFocus();
				setMSG("Enter the DDD Value...",'N');
			}
			else if(M_objSOURC == txtDDDVL)
			{
				cmbINDTG.requestFocus();
				setMSG("Enter the Indicator Tag..",'N');
			}
			else if(M_objSOURC == cmbUOMCD)
			{
				if(cmbUOMCD.getSelectedIndex() ==0)
				{
					setMSG("Select the UOM Code..",'E');
				}
				else
				{
					cmbDPTCD.requestFocus();
					setMSG("Select the Dept. / Owner Code ..",'N');
				}
			}
			else if(M_objSOURC == cmbDPTCD)
			{
				if(cmbDPTCD.getSelectedIndex() ==0)
				{
					setMSG("Select the Department Code..",'E');
				}
				else
				{
					cmbUSGBY.requestFocus();
					setMSG("Select the Usage By code, 999 for common usage ..",'N');
				}
			}
			else if(M_objSOURC == cmbUSGBY)
			{
				if(cmbUSGBY.getSelectedIndex() ==0)
				{
					setMSG("Select the Usage option..",'E');
				}
				else
				{
					txtSTDPK.requestFocus();
					setMSG("Select the Standard packing type..",'N');
				}
			}
			else if(M_objSOURC == txtSTDPK)
			{
				txtMPKQT.requestFocus();
				setMSG("Enter the Minimum Packing Quantity ..",'N');
			}
			/*else if(M_objSOURC == txtMPKQT)
			{
				txtILDTM.requestFocus();
				setMSG("Enter the Internal Lead Time in days..",'N');
			}
			else if(M_objSOURC == txtILDTM)
			{
				txtELDTM.requestFocus();
				setMSG("Enter the External Lead Time in days..",'N');
			}
			else if(M_objSOURC == txtELDTM)
			{
				chkBOTFL.requestFocus();
				setMSG("Specify the Procurement Type ..",'N');
			}*/
			else if(M_objSOURC == txtMPKQT)
			{
				chkBOTFL.requestFocus();
				setMSG("Specify the Procurement Type ..",'N');
			}

			else if(M_objSOURC == chkTCFFL)
			{
				chkINSFL.requestFocus();
				setMSG("Specify the Inspection tag ..",'N');
			}
			else if(M_objSOURC == chkINSFL)
			{
				chkPSVFL.requestFocus();
				setMSG("Specify the Preservation Tag ..",'N');
			}
			else if(M_objSOURC == chkPSVFL)
			{
				if(chkPSVFL.isSelected())
				{
					txtPSVFR.requestFocus();
					setMSG("Specify the Preservation Frequency in days..",'N');
				}
				else
				{
					jtpCTDTL.setSelectedIndex(1);
					tblLNDTL.clearSelection();
					tblLNDTL.setRowSelectionInterval(0,0);
					if(rdoSTDIT.isSelected())
					{
						tblLNDTL.setColumnSelectionInterval(TBL_LINNO,TBL_LINDS);
						tblLNDTL.setEditingRow(tblLNDTL.getSelectedRow());
						tblLNDTL.setEditingColumn(2);
						tblLNDTL.editCellAt(0,2);
					}
					else
					{
						tblLNDTL.setColumnSelectionInterval(TBL_LINDS,TBL_PRTFL);
						tblLNDTL.setEditingRow(tblLNDTL.getSelectedRow());
						tblLNDTL.setEditingColumn(2);
						tblLNDTL.editCellAt(0,2);
					}
					tblLNDTL.cmpEDITR[TBL_LINDS].requestFocus();
					setMSG("Enter the Line wise Description details..",'N');
				}
			}
			else if(M_objSOURC == txtPSVFR)
			{
				if(txtPSVFR.getText().trim().length() >0)
				{
					if(!chkPSVFL.isSelected())
						setMSG("Preservation Flag must by checked for giving preservation details..",'E'); 	
				}
				else
				{
					jtpCTDTL.setSelectedIndex(4);
					txtPSVCD.requestFocus();
					setMSG("F1 to select the existing code ..select Generate Code for New Code.. ",'N');
				}
			}

			else if(M_objSOURC == chkBOTFL)
			{
				chkIMPFL.requestFocus();
			}
			else if(M_objSOURC == chkIMPFL)
			{
				chkINDFL.requestFocus();
			}
			else if(M_objSOURC == chkINDFL)
			{
				chkPROFL.requestFocus();
			}
			else if(M_objSOURC == chkPROFL)
			{
				chkTCFFL.requestFocus();
				setMSG("Specify the Test certificate tag ..",'N');
			}
			else if(M_objSOURC == chkPRTFL)
			{
				if(rdoSTDIT.isSelected())
			   {
					tblLNDTL.setRowSelectionInterval(tblLNDTL.getSelectedRow(),tblLNDTL.getSelectedRow()+1);
					/*tblLNDTL.setColumnSelectionInterval(TBL_PRTFL,TBL_LINNO);
					tblLNDTL.setEditingRow(tblLNDTL.getSelectedRow());
					tblLNDTL.setEditingColumn(TBL_LINNO);
					tblLNDTL.editCellAt(tblLNDTL.getSelectedRow()+1,TBL_LINNO);
					tblLNDTL.requestFocus();*/
					tblLNDTL.setColumnSelectionInterval(TBL_PRTFL,TBL_LINDS);
					tblLNDTL.setEditingRow(tblLNDTL.getSelectedRow());
					tblLNDTL.setEditingColumn(TBL_LINDS);
					tblLNDTL.editCellAt(tblLNDTL.getSelectedRow()+1,TBL_LINDS);
					//tblLNDTL.cmpEDITR[TBL_LINDS].requestFocus();
					tblLNDTL.requestFocus();
				}
				else
				{
					tblLNDTL.setRowSelectionInterval(tblLNDTL.getSelectedRow(),tblLNDTL.getSelectedRow()+1);
					tblLNDTL.setColumnSelectionInterval(TBL_PRTFL,TBL_LINDS);
					tblLNDTL.setEditingRow(tblLNDTL.getSelectedRow());
					if(tblLNDTL.getSelectedRow()<9)
					{
						tblLNDTL.setValueAt("0"+String.valueOf(tblLNDTL.getSelectedRow()),tblLNDTL.getSelectedRow(),TBL_LINNO);
					}
					else
						tblLNDTL.setValueAt(String.valueOf(tblLNDTL.getSelectedRow()),tblLNDTL.getSelectedRow(),TBL_LINNO);
					tblLNDTL.setEditingColumn(TBL_LINDS);
					tblLNDTL.editCellAt(tblLNDTL.getSelectedRow()+1,TBL_LINDS);
					//tblLNDTL.cmpEDITR[TBL_LINDS].requestFocus();
					tblLNDTL.requestFocus();
				}
			}		
	
			}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		super.actionPerformed(L_AE);
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			/*for (int j=cmbMNGRP.getItemCount()-1;j>0;j--)
				cmbMNGRP.removeItemAt(j);
			for (int j=cmbSBGRP.getItemCount()-1;j>0;j--)
				cmbSBGRP.removeItemAt(j);
			for (int j=cmbSSGRP.getItemCount()-1;j>0;j--)
				cmbSSGRP.removeItemAt(j);
			*/
			//cmbMNGRP.removeAllItems();
			//cmbSBGRP.removeAllItems();
			//cmbSSGRP.removeAllItems();
			txtMATCD.setText("");
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				setMSG("Please select The type of Catalog..",'N');
				rdoSTDIT.requestFocus();
				
				setENBL(false);
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				setMSG("Please select the Main Group..",'N');
				cmbMNGRP.setEnabled(true);
				cmbMNGRP.requestFocus();
				for(int i=0;i<vtrMNGRP.size();i++)
				{
					cmbMNGRP.addItem(vtrMNGRP.elementAt(i).toString());
				}
				flgMGACT = true;		// Action on Main group to be done
			//	flgSGACT = true;		// Action on Main group to be done
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
			{
				//clrCOMP();
				setENBL(false);
			}
		}
		if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			//clrCOMP();
		}
		if(((M_objSOURC == rdoPRJIT)||(M_objSOURC == rdoOTMIT)||(M_objSOURC == rdoDESIT)||(M_objSOURC == rdoSTDIT)))
		{
			flgMGACT = false;
			flgSGACT = false;
			flgSSACT = false;
			try
			{
				for (int j=cmbMNGRP.getItemCount()-1;j>0;j--)
					cmbMNGRP.removeItemAt(j);
				for (int j=cmbSBGRP.getItemCount()-1;j>0;j--)
					cmbSBGRP.removeItemAt(j);
				for (int j=cmbSSGRP.getItemCount()-1;j>0;j--)
					cmbSSGRP.removeItemAt(j);
				
				//cmbMNGRP.removeAllItems();
				//cmbSBGRP.removeAllItems();
				//cmbSSGRP.removeAllItems();
				tblCTDTL.clrTABLE();
				tblLNDTL.clrTABLE();
				cmbMNGRP.setEnabled(true);
				setMSG("Please select The Main Group..",'N');
				M_strSQLQRY = "Select distinct CT_GRPCD,CT_MATDS from CO_CTMST WHERE ";
				if(M_objSOURC == rdoPRJIT)
				{
					for(int i=0;i<vtrMNGRP.size();i++)
					{
						if(vtrMNGRP.elementAt(i).toString().substring(0,2).equals(strPRJGP))
							cmbMNGRP.addItem(vtrMNGRP.elementAt(i).toString());
					}
				}
				else if(M_objSOURC == rdoOTMIT)
				{
					for(int i=0;i<vtrMNGRP.size();i++)
					{
						if(vtrMNGRP.elementAt(i).toString().substring(0,2).equals(strOTMGP))
							cmbMNGRP.addItem(vtrMNGRP.elementAt(i).toString());
					}
				}
				else
				{
					for(int i=0;i<vtrMNGRP.size();i++)
					{
						cmbMNGRP.addItem(vtrMNGRP.elementAt(i).toString());
					}
				}
			}
			catch(Exception L_SE)
			{
				setMSG(L_SE,"Action on catalog Options");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			flgMGACT = flgSGACT = flgSSACT =true;
		}
		else if(M_objSOURC == cmbMNGRP)
		{
			try
			{
				int L_intROWCT =0;
				if(flgMGACT)
				{
					if(cmbMNGRP.getSelectedItem().toString().length() >=2)
					strMNGRP = cmbMNGRP.getSelectedItem().toString().substring(0,2);
					if(strMNGRP.equals("Se"))
					   return;
					
					for (int j=cmbSBGRP.getItemCount()-1;j>0;j--)
						cmbSBGRP.removeItemAt(j);
					for (int j=cmbSSGRP.getItemCount()-1;j>0;j--)
						cmbSSGRP.removeItemAt(j);
					
					//cmbSBGRP.removeAllItems();
					//cmbSSGRP.removeAllItems();
					tblCTDTL.clrTABLE();
					if(jtpCTDTL.getSelectedIndex() != 5)
					jtpCTDTL.setSelectedIndex(5);
					cmbSBGRP.setEnabled(true);
					setMSG("Please select the Sub Group..",'N');
					flgSGACT = false;
					flgSSACT = false;
					
					if(strMNGRP.equals(strPRJGP))
					{
						for(int i=0;i<vtrMNGRP.size();i++)
						{
							if(!vtrMNGRP.elementAt(i).toString().substring(0,2).equals(strPRJGP))
								cmbSBGRP.addItem(vtrMNGRP.elementAt(i).toString());
						}
					}
					else if(strMNGRP.equals(strOTMGP))
					{
						//cmbSBGRP.addItem("Select Department");
						for(int i=0;i<vtrDPTCD.size();i++)
						cmbSBGRP.addItem(vtrDPTCD.elementAt(i).toString());
						
					}
					else
					{
						M_strSQLQRY = "Select distinct SUBSTRING(ct_matcd,3,2)L_CODCD,CT_MATDS from CO_CTMST WHERE";
						M_strSQLQRY += " SUBSTRING(CT_MATCD,1,2) = '"+strMNGRP+"' ";
						M_strSQLQRY += " AND CT_CODTP = 'SG' ";
						M_strSQLQRY += " and isnull(CT_STSFL,'') <> 'X' ";
						M_strSQLQRY += " order by SUBSTRING(ct_matcd,3,2)";
						String L_strDATA ="";	
				//		cmbSBGRP.removeAllItems();
				//		cmbSSGRP.removeAllItems();
				//		tblCTDTL.clrTABLE();
				//		if(jtpCTDTL.getSelectedIndex() != 5)
				//		jtpCTDTL.setSelectedIndex(5);
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				//		cmbSBGRP.setEnabled(true);
				//		setMSG("Please select the Sub Group..",'N');
				//		flgSGACT = false;
				//		flgSSACT = false;
						if(M_rstRSSET != null)	
						{
						while(M_rstRSSET.next()){
							L_strDATA = M_rstRSSET.getString("L_CODCD") + " " + M_rstRSSET.getString("CT_MATDS");
						  	cmbSBGRP.addItem(L_strDATA);
						}
						}
					}
					flgSGACT = true;
					flgSSACT = false;
					M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_LVLRF from CO_CTMST WHERE";
					M_strSQLQRY += " SUBSTRING(CT_MATCD,1,2) = '"+strMNGRP+"' ";
					M_strSQLQRY += " AND CT_CODTP = 'CD' ";
					M_strSQLQRY += " AND isnull(CT_STSFL,'') <>'X'";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					tblCTDTL.clrTABLE();
					if(M_rstRSSET != null)	
					while(M_rstRSSET.next())
					{
						tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),L_intROWCT,TBL_MATCD);
						tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intROWCT,TBL_MATDS);
						tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),L_intROWCT,TBL_LVLRF);
						L_intROWCT++;
					}
					if(!flgMATCD)
					{
						if(!cmbMNGRP.getSelectedItem().equals("Select Main Group"))
							txtMATCD.setText(strMNGRP);
					}
					M_strSQLQRY = "Select CTT_LINNO,CTT_MATDS,CTT_LVLNO from CO_CTTRN WHERE";
					M_strSQLQRY += " CTT_GRPCD = '"+strMNGRP+"' ";
					M_strSQLQRY += " AND CTT_CODTP = 'MG' ";
					M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
					M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					tblPRCDT.clrTABLE();
					L_intROWCT =0;
					if(M_rstRSSET != null)	
					while(M_rstRSSET.next())
					{
						tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),""),L_intROWCT,TB3_LINNO);
						tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),""),L_intROWCT,TB3_LINDS);
						tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LVLNO"),""),L_intROWCT,TB3_LVLRF);
						L_intROWCT++;
					}
				}
			}
			catch(SQLException L_SE)
			{
				setMSG(L_SE,"Action on Main Group Combo");	
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		else if(M_objSOURC == cmbSBGRP)
		{
			try
			{
				int L_intROWCT=0;
				if(flgSGACT)
				{
					if(cmbMNGRP.getSelectedItem().toString().length() >=2)
						strMNGRP = cmbMNGRP.getSelectedItem().toString().substring(0,2);
					if(cmbSBGRP.getSelectedItem().toString().length() >=2)
					{
						if(rdoOTMIT.isSelected())
							strSBGRP = cmbSBGRP.getSelectedItem().toString().substring(0,3);
						else
							strSBGRP = cmbSBGRP.getSelectedItem().toString().substring(0,2);
					}
					else strSBGRP ="";
					String L_strDATA ="";	
					flgSSACT = false;
					
					for (int j=cmbSSGRP.getItemCount()-1;j>0;j--)
						cmbSSGRP.removeItemAt(j);
					
					//cmbSSGRP.removeAllItems();
					if(jtpCTDTL.getSelectedIndex() != 5)
					jtpCTDTL.setSelectedIndex(5);
					cmbSSGRP.setEnabled(true);
		//			setMSG("Please select the sub Group..",'E');
					if(strMNGRP.equals(strPRJGP))
					{
						M_strSQLQRY = "Select distinct SUBSTRING(ct_matcd,3,2)L_CODCD,CT_MATDS from CO_CTMST WHERE";
						M_strSQLQRY += " SUBSTRING(CT_MATCD,1,2) = '"+strSBGRP+"' ";
						M_strSQLQRY += " AND CT_CODTP = 'SG' ";
						M_strSQLQRY += " and isnull(CT_STSFL,'') <> 'X' ";
						M_strSQLQRY += " order by SUBSTRING(ct_matcd,3,2)";

						L_strDATA ="";	
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)	
						{
							while(M_rstRSSET.next())
							{
								L_strDATA = M_rstRSSET.getString("L_CODCD") + " " + M_rstRSSET.getString("CT_MATDS");
								System.out.println(L_strDATA);
							  	cmbSSGRP.addItem(L_strDATA);
							}
							M_rstRSSET.close();
						}
					}
					else if(strMNGRP.equals(strOTMGP))
					{
						//cmbSSGRP.addItem("Select Year Digit");
						cmbSSGRP.addItem(cl_dat.M_strFNNYR_pbst.substring(3) + " Year "+cl_dat.M_strLOGDT_pbst.substring(6));
					}
					else
					{
						M_strSQLQRY = "Select distinct SUBSTRING(ct_matcd,5,2)L_CODCD,CT_MATDS from CO_CTMST WHERE";
						M_strSQLQRY += " SUBSTRING(CT_MATCD,1,4) = '"+strMNGRP+strSBGRP+"' ";
						M_strSQLQRY += " and CT_CODTP = 'SS' ";
						M_strSQLQRY += " and isnull(CT_STSFL,'') <> 'X' ";
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)	
						{
						while(M_rstRSSET.next()){
							L_strDATA = M_rstRSSET.getString("L_CODCD") + " " + M_rstRSSET.getString("CT_MATDS");
						  	cmbSSGRP.addItem(L_strDATA);
						}
						}
					}

					flgSSACT = true;
					
					//if(rdoOTMIT.isSelected())
					if(strMNGRP.equals("99"))
					{
						L_intROWCT =0;
						M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_LVLRF from CO_CTMST WHERE";
						M_strSQLQRY += " SUBSTRING(CT_MATCD,1,5) = '"+strMNGRP+strSBGRP+"' ";
						M_strSQLQRY += " AND CT_CODTP = 'CD' ";
						tblCTDTL.clrTABLE();
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)	
						while(M_rstRSSET.next())
						{
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),L_intROWCT,TBL_MATCD);
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intROWCT,TBL_MATDS);
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),L_intROWCT,TBL_LVLRF);
							L_intROWCT++;
						}
					}
					else
					{
						M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_LVLRF from CO_CTMST WHERE";
						M_strSQLQRY += " SUBSTRING(CT_MATCD,1,4) = '"+strMNGRP+strSBGRP+"' ";
						M_strSQLQRY += " AND CT_CODTP = 'CD' ";
						tblCTDTL.clrTABLE();
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						if(M_rstRSSET != null)	
						while(M_rstRSSET.next())
						{
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),""),L_intROWCT,TBL_MATCD);
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intROWCT,TBL_MATDS);
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),L_intROWCT,TBL_LVLRF);
							L_intROWCT++;
						}
					}
					if(!flgMATCD)
					{
						if(!cmbSBGRP.getSelectedItem().equals("Select Sub Group"))
							txtMATCD.setText(strMNGRP+strSBGRP);
					}
					else
					{
						//flgMATCD = false;
					}
					String L_strGRPCHK = " SUBSTRING(CTT_MATCD,1,4) = '"+strMNGRP+strSBGRP+"'  AND CTT_CODTP = 'SG' ";
					if(txtMATCD.getText().trim().substring(0,2).equals(strPRJGP))
						L_strGRPCHK = " SUBSTRING(CTT_MATCD,1,2) = '"+strSBGRP+"'  AND CTT_CODTP = 'MG' ";
					M_strSQLQRY = "Select CTT_LINNO,CTT_MATDS,CTT_LVLNO from CO_CTTRN WHERE";
					M_strSQLQRY += L_strGRPCHK;
					//M_strSQLQRY += " AND CTT_CODTP = 'SG' ";
					M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
					M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
					System.out.println(M_strSQLQRY);
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					tblPRCDT.clrTABLE();
					L_intROWCT =0;
					if(M_rstRSSET != null)	
					while(M_rstRSSET.next())
					{
						tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),""),L_intROWCT,TB3_LINNO);
						tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),""),L_intROWCT,TB3_LINDS);
						tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LVLNO"),""),L_intROWCT,TB3_LVLRF);
						L_intROWCT++;
					}
				}
			}
			catch(SQLException L_SE)
			{
				setMSG(L_SE,"Action on SUB Group Combo");	
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		else if(M_objSOURC == cmbSSGRP)
		{
			try
			{
				int L_intROWCT =0,L_intSRLNO=0;
				String L_strMATCD="",L_strTEMP ="",L_strSRLNO="",L_strLSTNO;
				if(flgSSACT)
				{
					if(cmbSSGRP.getSelectedItem().toString().length() >=2)
					{
						if(!rdoOTMIT.isSelected())
							strSSGRP = cmbSSGRP.getSelectedItem().toString().substring(0,2);
						else
						{
							strSSGRP = cmbSSGRP.getSelectedItem().toString().substring(0,1);
						    txtLVLRF.setText("05");
							txtLVLRF.requestFocus();
							txtILDTM.setText(str99ILD);
							txtELDTM.setText(str99ELD);
							for(int i=0;i<cmbDPTCD.getItemCount();i++)
							{
								if(cmbDPTCD.getItemAt(i).toString().substring(0,3).equals(cmbSBGRP.getSelectedItem().toString().substring(0,3)))
								{
									cmbDPTCD.setSelectedIndex(i);
									break;
								}
							}
							for(int i=0;i<cmbUSGBY.getItemCount();i++)
							{
								if(cmbUSGBY.getItemAt(i).toString().substring(0,3).equals(cmbSBGRP.getSelectedItem().toString().substring(0,3)))
								{
									cmbUSGBY.setSelectedIndex(i);
									break;
								}
							}
							if(cmbINDTG.getSelectedIndex() >0)
							{
								strTEMP = cmbINDTG.getSelectedItem().toString().substring(0,1);
								if(strTEMP.equals("1"))
								{
									chkPROFL.setSelected(true);
									chkINDFL.setSelected(true);
									chkIMPFL.setSelected(false);
									chkBOTFL.setSelected(false);
								}
								else if(strTEMP.equals("2"))
								{
									chkPROFL.setSelected(true);
									chkIMPFL.setSelected(true);
									chkINDFL.setSelected(false);
									chkBOTFL.setSelected(false);
								}
								else if(strTEMP.equals("5"))
								{
									chkBOTFL.setSelected(true);
									chkINDFL.setSelected(true);
									chkIMPFL.setSelected(false);
									chkPROFL.setSelected(false);
								}
								else if(strTEMP.equals("6"))
								{
									chkBOTFL.setSelected(true);
									chkIMPFL.setSelected(true);
									chkINDFL.setSelected(false);
									chkPROFL.setSelected(false);
								}
							}
						}
					}
					else
						strSSGRP ="";
						M_strSQLQRY = "Select CT_MATCD,CT_MATDS,CT_LVLRF from CO_CTMST WHERE";
						M_strSQLQRY += " SUBSTRING(CT_MATCD,1,6) = '"+strMNGRP+strSBGRP+strSSGRP+"' ";
						M_strSQLQRY += " AND CT_CODTP = 'CD' order by ct_matcd";
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						tblCTDTL.clrTABLE();
						if(jtpCTDTL.getSelectedIndex() != 5)
							jtpCTDTL.setSelectedIndex(5);
						if(M_rstRSSET != null)	
						while(M_rstRSSET.next())
						{
							L_strMATCD = nvlSTRVL(M_rstRSSET.getString("CT_MATCD"),"");
							tblCTDTL.setValueAt(L_strMATCD,L_intSRLNO,TBL_MATCD);
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intSRLNO,TBL_MATDS);
							tblCTDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""),L_intSRLNO,TBL_LVLRF);
							L_intSRLNO++;
						}
					if(!rdoOTMIT.isSelected())
					{
						String L_strGRPCHK = " SUBSTRING(CTT_MATCD,1,6) = '"+strMNGRP+strSBGRP+strSSGRP+"'  AND CTT_CODTP = 'SS' ";
						if(txtMATCD.getText().trim().substring(0,2).equals(strPRJGP))
							L_strGRPCHK = " SUBSTRING(CTT_MATCD,1,4) = '"+strSBGRP+strSSGRP+"' and  CTT_CODTP = 'SG'  ";
						M_strSQLQRY = "Select CTT_LINNO,CTT_MATDS,CTT_LVLNO from CO_CTTRN WHERE";
						M_strSQLQRY += L_strGRPCHK;
						//M_strSQLQRY += " AND CTT_CODTP = 'SS' ";
						M_strSQLQRY += " AND isnull(CTT_STSFL,'') <>'X'";
						M_strSQLQRY += " AND isnull(CTT_PRTFL,'') = 'Y'";
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
						tblPRCDT.clrTABLE();
						L_intROWCT =0;
						if(M_rstRSSET != null)	
						while(M_rstRSSET.next())
						{
							tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),""),L_intROWCT,TB3_LINNO);
							tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),""),L_intROWCT,TB3_LINDS);
							tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LVLNO"),""),L_intROWCT,TB3_LVLRF);
							L_intROWCT++;
						}
					}
					else
					{
						
					}
					L_strSRLNO = "";
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						if(L_intSRLNO >0)
						{
							L_strLSTNO = L_strMATCD.substring(6,9);
							L_intSRLNO = Integer.parseInt(L_strLSTNO)+1;
							for(int i=0;i<3-String.valueOf(L_intSRLNO).toString().length();i++)
							{
								L_strTEMP +="0";	
							}
							L_strSRLNO = L_strTEMP +String.valueOf(L_intSRLNO).toString();
						}
						else L_strSRLNO ="001";
						if(!flgMATCD)
						{
							txtDDDVL.setText(L_strSRLNO);
							if(rdoOTMIT.isSelected())
							{
									if(cmbINDTG.getSelectedIndex() != 0)
									txtMATCD.setText(strMNGRP+strSBGRP+strSSGRP+L_strSRLNO+cmbINDTG.getSelectedItem().toString().substring(0,1));
							    else
									txtMATCD.setText(strMNGRP+strSBGRP+strSSGRP+L_strSRLNO);
							}
							else if(rdoPRJIT.isSelected())
							{
									if(cmbINDTG.getSelectedIndex() != 0)
										txtMATCD.setText(strMNGRP+strSBGRP+strSSGRP+L_strSRLNO+cmbINDTG.getSelectedItem().toString().substring(0,1));
									else
										txtMATCD.setText(strMNGRP+strSBGRP+strSSGRP+L_strSRLNO);
							}
							else
							{
								if(cmbMNGRP.getSelectedIndex() != 0)
								if(cmbINDTG.getSelectedIndex() != 0)
								{
									txtMATCD.setText(strMNGRP+strSBGRP+strSSGRP+L_strSRLNO+cmbINDTG.getSelectedItem().toString().substring(0,1));
								}
							    else
								{
									txtMATCD.setText(strMNGRP+strSBGRP+strSSGRP+L_strSRLNO);
								}
							}
							/*if(!strMNGRP.equals("99"))
							{
								//getLEDTM(strMNGRP+strSBGRP+strSSGRP);
								getLEDTM(txtMATCD.getText().trim());
								txtILDTM.setText(strILDTM);
								txtELDTM.setText(strELDTM);
							}*/
						}
						//else flgMATCD = false; 
							
						setENBL(true);
					//	txtMATCD.requestFocus();
						txtLVLRF.requestFocus();
						cmbINDTG.setSelectedIndex(3);
						if(!strMNGRP.equals("99"));
							cmbUSGBY.setSelectedIndex(1);
						setMSG("Last three digits Serial No. is auto generated,it can be changed..",'N');
						//txtMATDS.requestFocus();
						//setMSG("Enter the Material Description ..",'N');
						if(jtpCTDTL.getSelectedIndex() != 0)
						jtpCTDTL.setSelectedIndex(0);
					}
					else
					{
						if(!flgMATCD)
						{
							txtMATCD.setText(strMNGRP+strSBGRP+strSSGRP);
						}
						else
						{
							//flgMATCD = false;
						}
						txtMATCD.setEnabled(true);
						txtMATCD.requestFocus();
						setMSG("Press F1 to Select the Material Code ..",'N');
					}
				}
				txtLVLRF.requestFocus();
			}
			catch(Exception L_SE)
			{
				setMSG(L_SE,"Action on SUB SUB Group Combo");	
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
		else if(M_objSOURC == txtMATCD)
		{
			try
			{
				if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					clrCOMP();
					flgMATCD = true;
					exeSELREC();
					getPRCDT();
					jtpCTDTL.setSelectedIndex(0);
					flgMATCD = false;
				}
				else
				{
					if(txtMATCD.getText().length() == 10)
					{
						txtMATDS.requestFocus();
						setMSG("Enter the Material Dexcription..",'N');
					}
					else
					{
						setMSG("Select the Indicator Tag..",'N');
					}
				}
				
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"Item verification..");				
			}
		}
		else if(M_objSOURC == cmbINDTG)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(txtMATCD.getText().trim().length() >=9)
					if(cmbINDTG.getSelectedIndex() !=0)
					{
						txtMATCD.setText(txtMATCD.getText().trim().substring(0,9) + cmbINDTG.getSelectedItem().toString().trim().substring(0,1));
						//txtMATCD.requestFocus();
					}
				    else setMSG("Select a valid Indicator Tag",'N');
				txtMATDS.requestFocus();
				setMSG("Select the Level Ref. from F1. List..",'N');
				
			//	txtPRTNO.requestFocus();
				if(cmbINDTG.getSelectedIndex() >0)
				{
					//getLEDTM(txtMATCD.getText().trim());
					strTEMP = cmbINDTG.getSelectedItem().toString().substring(0,1);
						if(strTEMP.equals("1"))
					{
						chkPROFL.setSelected(true);
						chkINDFL.setSelected(true);
						chkIMPFL.setSelected(false);
						chkBOTFL.setSelected(false);
					}
					else if(strTEMP.equals("2"))
					{
						chkPROFL.setSelected(true);
						chkIMPFL.setSelected(true);
						chkINDFL.setSelected(false);
						chkBOTFL.setSelected(false);
					}
					else if(strTEMP.equals("5"))
					{
						chkBOTFL.setSelected(true);
						chkINDFL.setSelected(true);
						chkIMPFL.setSelected(false);
						chkPROFL.setSelected(false);
					}
					else if(strTEMP.equals("6"))
					{
						chkBOTFL.setSelected(true);
						chkIMPFL.setSelected(true);
						chkINDFL.setSelected(false);
						chkPROFL.setSelected(false);
					}
				}
			}
		}
		else if(M_objSOURC == chkNEWCD)
		{
			try
			{
				String L_strLSTCD ="000",L_strNEWCD="";
				M_strSQLQRY = "Select max(PV_PSVCD) L_PSVCD from MM_PVMST WHERE PV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PV_STSFL <>'X'";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						L_strLSTCD = nvlSTRVL(M_rstRSSET.getString("L_PSVCD"),"");
						int L_intNEWCD = Integer.parseInt(L_strLSTCD) +1;
						for(int i=0;i<3-String.valueOf(L_intNEWCD).length()-1;i++)
						{
							L_strNEWCD = "0";
						}
						L_strNEWCD +=L_strNEWCD+String.valueOf(L_intNEWCD);
						txtPSVCD.setText(L_strNEWCD);
					}
				tblPSVDT.getCellEditor().stopCellEditing();
				tblPSVDT.setRowSelectionInterval(tblLNDTL.getSelectedRow(),tblLNDTL.getSelectedRow()+1);
				tblPSVDT.setColumnSelectionInterval(TB4_ACTDS,TB4_ACTDS);
				tblPSVDT.setEditingRow(tblLNDTL.getSelectedRow());
				tblPSVDT.setEditingColumn(TB4_ACTDS);
				tblPSVDT.editCellAt(0,TB4_ACTDS);
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"chkNEWCD Action");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
		}
	}
	private void exeSELREC()
	{
		try
		{
			boolean L_flgFOUND = false;
			flgERRFL = false;
			int L_intROWCT =0;
			String L_strDEFTP,L_strPRTFL,L_strLINNO,L_strLINDS,L_strPPONO,L_strSTSFL,L_strSQLQRY ="";
			ResultSet L_rstRSSET;
			java.sql.Date datTEMP;
			strMATCD = txtMATCD.getText().trim();
			M_strSQLQRY = "Select * from CO_CTMST,CO_CTTRN where CT_GRPCD =CTT_GRPCD AND CT_CODTP = CTT_CODTP AND CT_MATCD = CTT_MATCD AND ";
			M_strSQLQRY += " isnull(ct_stsfl,'') <>'X' and isnull(ctt_stsfl,'') <>'X' and ct_matcd ='"+strMATCD+"' order by ctt_lvlno,ctt_linno";
			setCursor(cl_dat.M_curWTSTS_pbst);		
			hstLNDTL.clear();
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_flgFOUND = true;
				if(L_intROWCT == 0)
				{
					L_strPPONO ="";
					L_strSTSFL = nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"");
					L_strPPONO = nvlSTRVL(M_rstRSSET.getString("CT_PPONO"),"");
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
					    cmbUOMCD.setEnabled(true);
						if(L_strSTSFL.equals("9"))
						{
							// Obsolete material code modification not allowed
							setMSG("Obsolete item code, can not modify..",'E');
							JOptionPane.showMessageDialog(this,"Obsolete item code, can not modify..","Obsolete ",JOptionPane.INFORMATION_MESSAGE);
							flgERRFL = true;
							setCursor(cl_dat.M_curDFSTS_pbst);
							return;
						}
						if(strCLSNM.equals("mm_mectm"))
						{
							L_strSQLQRY = "SELECT sum(ST_STKIN) L_STKIN FROM MM_STMST WHERE ST_MATCD ='"+strMATCD +"'";
							System.out.println(L_strSQLQRY);
							L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
							if(L_rstRSSET !=null)
								if(L_rstRSSET.next())
								{
									if(L_rstRSSET.getFloat("L_STKIN") >0)
									{
										setMSG("Indent has been placed against this item, can not modify..",'E');
										flgERRFL = true;
										JOptionPane.showMessageDialog(this,"Indent has been placed ,modification is not allowed.."," ",JOptionPane.INFORMATION_MESSAGE);
										setCursor(cl_dat.M_curDFSTS_pbst);
										return;
									}
								}
							if(L_strPPONO.trim().length()>0)
							{
								// Obsolete material code modification not allowed
								setMSG("P.O has been placed against this item, can not modify..",'E');
								flgERRFL = true;
								JOptionPane.showMessageDialog(this,"P.O. has been placed ,modification is not allowed.."," ",JOptionPane.INFORMATION_MESSAGE);
								setCursor(cl_dat.M_curDFSTS_pbst);
								return;
							}
						}
                        else
                        {
                            if(L_strPPONO.trim().length()>0)
							{
							    cmbUOMCD.setEnabled(false);
							}
							else
							    cmbUOMCD.setEnabled(true);
                        }
					}
					L_strDEFTP = nvlSTRVL(M_rstRSSET.getString("CT_DSCTP"),"");
					if(txtMATCD.getText().trim().length() >=2)
					if(txtMATCD.getText().trim().substring(0,2).equals(strPRJGP))
					{
						rdoPRJIT.setSelected(true);
					}
					else if(txtMATCD.getText().trim().substring(0,2).equals(strOTMGP))
					{
						rdoOTMIT.setSelected(true);
					}
					else 
					{
						if(L_strDEFTP.equals(strSTDDF))
						{
							rdoSTDIT.setSelected(true);
						}
						if(L_strDEFTP.equals(strDESDF))
						{
							rdoDESIT.setSelected(true);
						}
					}
					txtMATDS.setText(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""));
					if(txtMATCD.getText().trim().length() ==10)
						txtDDDVL.setText(txtMATCD.getText().trim().substring(6,9));
					int i;
					for(i=0;i<cmbINDTG.getItemCount();i++)
					{
						if(cmbINDTG.getItemAt(i).toString().substring(0,1).equals(txtMATCD.getText().trim().substring(9)))
						{
							cmbINDTG.setSelectedIndex(i);
							break;
						}
					}
					String L_strUOMCD = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_UOMCD"),""),4);
					for(i=0;i<cmbUOMCD.getItemCount();i++)
					{
						if(cmbUOMCD.getItemAt(i).toString().substring(0,4).equals(L_strUOMCD))
						{
							cmbUOMCD.setSelectedIndex(i);
							break;
						}
					}
					String L_strDPTCD = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_OWNBY"),""),3);
					for(i=0;i<cmbDPTCD.getItemCount();i++)
					{
						if(cmbDPTCD.getItemAt(i).toString().substring(0,3).equals(L_strDPTCD))
						{
							cmbDPTCD.setSelectedIndex(i);
							break;
						}
					}
					String L_strUSGBY = padSTRING('R',nvlSTRVL(M_rstRSSET.getString("CT_USGBY"),""),3);
					for(i=0;i<cmbUSGBY.getItemCount();i++)
					{
						if(cmbUSGBY.getItemAt(i).toString().substring(0,3).equals(L_strUSGBY))
						{
							cmbUSGBY.setSelectedIndex(i);
							break;
						}
					}
					txtLVLRF.setText(nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),""));
					txtPRTNO.setText(nvlSTRVL(M_rstRSSET.getString("CT_PRTNO"),""));
					txtCRSRF.setText(nvlSTRVL(M_rstRSSET.getString("CT_MATRF"),""));
					if(nvlSTRVL(M_rstRSSET.getString("CT_BOTFL"),"").equals("Y"))
					   chkBOTFL.setSelected(true);
					if(nvlSTRVL(M_rstRSSET.getString("CT_IMPFL"),"").equals("Y"))
					   chkIMPFL.setSelected(true);
					if(nvlSTRVL(M_rstRSSET.getString("CT_INDFL"),"").equals("Y"))
					   chkINDFL.setSelected(true);
					if(nvlSTRVL(M_rstRSSET.getString("CT_PROFL"),"").equals("Y"))
					   chkPROFL.setSelected(true);
					if(nvlSTRVL(M_rstRSSET.getString("CT_PSVFL"),"").equals("Y"))
					{
						chkPSVFL.setSelected(true);
						txtPSVFR.setText(nvlSTRVL(M_rstRSSET.getString("CT_PSVFR")," "));
						datTEMP = M_rstRSSET.getDate("CT_PSVDT");
						if(datTEMP !=null)
							txtPSVDT.setText(M_fmtLCDAT.format(datTEMP));
						else
							txtPPODT.setText("");
						String L_strPSVCD = nvlSTRVL(M_rstRSSET.getString("CT_PSVCD"),""); 
						txtPSVCD.setText(L_strPSVCD);
						L_strSQLQRY ="Select * from MM_PVMST where PV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PV_PSVCD ='"+ L_strPSVCD +"'";
						System.out.println(L_strSQLQRY);
						L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
						int L_intCNT =0;
						if(L_rstRSSET != null)
							while(L_rstRSSET.next())
							{
								tblPSVDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("PV_ACTSR"),""),L_intCNT,TB4_ACTSR);
								tblPSVDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("PV_ACTDS"),""),L_intCNT,TB4_ACTDS);
								L_intCNT++;
							}
					}
					if(nvlSTRVL(M_rstRSSET.getString("CT_STSFL"),"").equals("9"))
					   chkOBSFL.setSelected(true);
					else
						chkOBSFL.setSelected(false);
					if((txtMATCD.getText().trim().substring(9).equals("2"))||(txtMATCD.getText().trim().substring(9).equals("6")))
				    {
						txtILDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),"0")); 
						txtELDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),"0")); 
					}
					else
					{
						txtILDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0")); 
						txtELDTM.setText(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0")); 
					}	
					if(nvlSTRVL(M_rstRSSET.getString("CT_TCFFL"),"").equals("Y"))
						chkTCFFL.setSelected(true);
					else
						chkTCFFL.setSelected(false);
					if(nvlSTRVL(M_rstRSSET.getString("CT_INSFL"),"").equals("Y"))
						chkINSFL.setSelected(true);
					else
						chkINSFL.setSelected(false);
					if(nvlSTRVL(M_rstRSSET.getString("CT_STKFL"),"").equals("Y"))
						chkSTKFL.setSelected(true);
					else
						chkSTKFL.setSelected(false);
					if(nvlSTRVL(M_rstRSSET.getString("CT_QARFL"),"").equals("Y"))
						chkQARFL.setSelected(true);
					else
						chkQARFL.setSelected(false);
					txtSTKQT.setText(nvlSTRVL(M_rstRSSET.getString("CT_STKQT"),"")); 
					txtPPONO.setText(nvlSTRVL(M_rstRSSET.getString("CT_PPONO"),"")); 
					datTEMP = M_rstRSSET.getDate("CT_PPODT");
					if(datTEMP !=null)
						txtPPODT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPPODT.setText("");
					txtPPORT.setText(nvlSTRVL(M_rstRSSET.getString("CT_PPORT"),"")); 
					txtPGRNO.setText(nvlSTRVL(M_rstRSSET.getString("CT_PGRNO"),"")); 
					datTEMP = M_rstRSSET.getDate("CT_PGRDT");
					if(datTEMP !=null)
						txtPGRDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPGRDT.setText("");
					txtPGRRT.setText(nvlSTRVL(M_rstRSSET.getString("CT_PGRRT"),"")); 
					txtMDVRT.setText(nvlSTRVL(M_rstRSSET.getString("CT_MDVRT"),"")); 
					
					txtPMRNO.setText(nvlSTRVL(M_rstRSSET.getString("CT_PMRNO"),"")); 
					datTEMP = M_rstRSSET.getDate("CT_PMRDT");
					if(datTEMP !=null)
						txtPMRDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPMRDT.setText("");
						txtPSNNO.setText(nvlSTRVL(M_rstRSSET.getString("CT_PSNNO"),"")); 
					datTEMP = M_rstRSSET.getDate("CT_PSNDT");
					if(datTEMP !=null)
						txtPSNDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPSNDT.setText("");
					txtPSTNO.setText(nvlSTRVL(M_rstRSSET.getString("CT_PSTNO"),"")); 
					datTEMP = M_rstRSSET.getDate("CT_PSTDT");
					if(datTEMP !=null)
						txtPSTDT.setText(M_fmtLCDAT.format(datTEMP));
					else
						txtPSTDT.setText("");
					txtSTMCD.setText(nvlSTRVL(M_rstRSSET.getString("CT_STMCD"),"")); 
					txtSTDPK.setText(nvlSTRVL(M_rstRSSET.getString("CT_PKGTP"),"")); 
					txtMPKQT.setText(nvlSTRVL(M_rstRSSET.getString("CT_PKGQT"),"")); 
					cl_dat.M_txtUSER_pbst.setText(nvlSTRVL(M_rstRSSET.getString("CT_LUSBY"),""));
					datTEMP = M_rstRSSET.getDate("CT_LUPDT");
					if(datTEMP !=null)
						cl_dat.M_txtDATE_pbst.setText(M_fmtLCDAT.format(datTEMP));
					else
						cl_dat.M_txtDATE_pbst.setText("");
				}
				L_strLINNO = nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),"");
				L_strLINDS = nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),"");
				tblLNDTL.setValueAt(L_strLINNO,L_intROWCT,TBL_LINNO);
				tblLNDTL.setValueAt(L_strLINDS,L_intROWCT,TBL_LINDS);
				L_strPRTFL = nvlSTRVL(M_rstRSSET.getString("CTT_PRTFL"),"");
				if(L_strPRTFL.equals("Y"))
					tblLNDTL.setValueAt(new Boolean(true),L_intROWCT,TBL_PRTFL);
				else
					tblLNDTL.setValueAt(new Boolean(false),L_intROWCT,TBL_PRTFL);
				hstLNDTL.put(L_strLINNO,L_strLINDS);
				L_intROWCT++;
			}
			if(!L_flgFOUND)
			{
				setMSG("Data not found..",'E');
			}
			String L_strMNGRP ="",L_strSBGRP="",L_strSSGRP="";
			if(txtMATCD.getText().trim().length() >=2)
			L_strMNGRP = txtMATCD.getText().trim().substring(0,2);
			else 
				L_strMNGRP ="";
			int i=0;
			L_flgFOUND = false;
			if(cmbMNGRP.getItemCount() >0)
			if(!cmbMNGRP.getSelectedItem().toString().substring(0,2).equals(L_strMNGRP))
			{
				for(i=0;i<cmbMNGRP.getItemCount();i++)
				{
					if(cmbMNGRP.getItemAt(i).toString().substring(0,2).equals(L_strMNGRP))
					{
						L_flgFOUND = true;
						break;
					}
				}
				if(L_flgFOUND)
				{
					cmbMNGRP.setSelectedIndex(i);
					L_flgFOUND = false;
				}
			}
			if(rdoOTMIT.isSelected())
		    {
					if(txtMATCD.getText().trim().length() >=5)
					L_strSBGRP = txtMATCD.getText().trim().substring(2,5);
				else
					L_strSBGRP = "";
			}
			else
			{
				if(txtMATCD.getText().trim().length() >=4)
					L_strSBGRP = txtMATCD.getText().trim().substring(2,4);
				else
					L_strSBGRP = "";
			}
			if(rdoOTMIT.isSelected())
			{
				if(cmbSBGRP.getItemCount() >0)
					strTEMP = cmbSBGRP.getSelectedItem().toString().substring(0,3);
				else strTEMP ="";
			}
			else
			{
				if(cmbSBGRP.getItemCount() >0)
				strTEMP = cmbSBGRP.getSelectedItem().toString().substring(0,2);
				else strTEMP ="";
			}
			if(!strTEMP.equals(L_strSBGRP))
			{
				for(i=0;i<cmbSBGRP.getItemCount();i++)
				{
					if(rdoOTMIT.isSelected())
						strTEMP = cmbSBGRP.getItemAt(i).toString().substring(0,3);
					else
						strTEMP = cmbSBGRP.getItemAt(i).toString().substring(0,2);
					if(strTEMP.equals(L_strSBGRP))
					{
						L_flgFOUND = true;
						break;
					}
				}
				if(L_flgFOUND)
				{
					flgSGACT = true;
					cmbSBGRP.setSelectedIndex(i);
					L_flgFOUND = false;
				}
				
			}
			/*else
			{
				if(cmbSBGRP.getSelectedIndex()== 0)
				   cmbSBGRP.setSelectedIndex(0);
			}*/
			if(rdoOTMIT.isSelected())
			{
				if(cmbSSGRP.getItemCount() > 1)
				{
					cmbSSGRP.setSelectedIndex(1);
					L_flgFOUND = false;
				}
			}
			else
			{
				if(txtMATCD.getText().length() >=6)
				L_strSSGRP = txtMATCD.getText().trim().substring(4,6);
				else
					L_strSSGRP ="";
				if(cmbSSGRP.getItemCount() >0)
				{
					if(cmbSSGRP.getSelectedItem().toString().length() >=2)
					if(!cmbSSGRP.getSelectedItem().toString().substring(0,2).equals(L_strSSGRP))
					{
						for(i=0;i<cmbSSGRP.getItemCount();i++)
						{
							if(cmbSSGRP.getItemAt(i).toString().substring(0,2).equals(L_strSSGRP))
							{
								L_flgFOUND = true;
								break;
							}
						}
						if(L_flgFOUND)
						{
							cmbSSGRP.setSelectedIndex(i);
							L_flgFOUND = false;
						}
					}
				}
			}
		}
		catch(SQLException L_E)
		{
			setMSG(L_E,"exeSELREC");
		}
		finally
		{
			setCursor(cl_dat.M_curDFSTS_pbst);
			M_rstRSSET = null;
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if((M_strHLPFLD.equals("txtMATCD"))||(M_strHLPFLD.equals("txtMATCDF2")))
			{
				txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				txtMATDS.setText(cl_dat.M_strHLPSTR_pbst);	
			}
			if(M_strHLPFLD.equals("txtLVLRF"))
			{
				txtLVLRF.setText(cl_dat.M_strHLPSTR_pbst);	
			}
			else if(M_strHLPFLD.equals("txtPSVCD"))
			{
				txtPSVCD.setText(cl_dat.M_strHLPSTR_pbst);	
				String L_strSQLQRY ="Select * from MM_PVMST where PV_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND PV_PSVCD ='"+ txtPSVCD.getText().trim() +"'";
				System.out.println(L_strSQLQRY);
				ResultSet L_rstRSSET = cl_dat.exeSQLQRY2(L_strSQLQRY);
				int L_intCNT =0;
				if(L_rstRSSET != null)
					while(L_rstRSSET.next())
					{
						tblPSVDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("PV_ACTSR"),""),L_intCNT,TB4_ACTSR);
						tblPSVDT.setValueAt(nvlSTRVL(L_rstRSSET.getString("PV_ACTDS"),""),L_intCNT,TB4_ACTDS);
						L_intCNT++;
					}
			}
		}
		catch(Exception e)
		{
			setMSG(e,"exeHLPOK ");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	void clrCOMP()
	{
		/*for (int j=cmbMNGRP.getItemCount()-1;j>0;j--)
			cmbMNGRP.removeItemAt(j);
		for (int j=cmbSBGRP.getItemCount()-1;j>0;j--)
			cmbSBGRP.removeItemAt(j);
		for (int j=cmbSSGRP.getItemCount()-1;j>0;j--)
			cmbSSGRP.removeItemAt(j);
		*/
	//	cmbMNGRP.removeAllItems();
	//	cmbSBGRP.removeAllItems();
	//	cmbSSGRP.removeAllItems();
		
		//cmbMNGRP.setSelectedIndex(0);
		//cmbSBGRP.setSelectedIndex(0);
		//cmbSSGRP.setSelectedIndex(0);
			
		if(tblLNDTL.isEditing())
		{
			tblLNDTL.getCellEditor().stopCellEditing();
		}
		if(tblCTDTL.isEditing())
		{
			tblCTDTL.getCellEditor().stopCellEditing();
		}
		if(cmbINDTG !=null)
			cmbINDTG.setSelectedIndex(0);
		if(cmbUOMCD !=null)
			cmbUOMCD.setSelectedIndex(0);
		if(cmbDPTCD !=null)
			cmbDPTCD.setSelectedIndex(0);
		if(cmbUSGBY !=null)
			cmbUSGBY.setSelectedIndex(0);
		rdoPRJIT.setSelected(false);
		rdoSTDIT.setSelected(false);
		rdoOTMIT.setSelected(false);
		rdoDESIT.setSelected(false);
		tblLNDTL.clearSelection();
		tblCTDTL.clearSelection();
		tblPRCDT.clearSelection();
	//	tblCTDTL.setEnabled(true);
		txtMATDS.setText("");
		txtCRSRF.setText("");
		txtPRTNO.setText("");
		txtLVLRF.setText("");
		txtDDDVL.setText("");
		txtSTDPK.setText("");
		txtMPKQT.setText("");
		txtILDTM.setText("");
		txtELDTM.setText("");
		txtPSVFR.setText("");
		txtSTKQT.setText("");
		txtPPONO.setText("");
		txtPPORT.setText("");
		txtPPODT.setText("");
		txtPGRNO.setText("");
		txtPGRDT.setText("");
		txtPGRRT.setText("");
		txtPISNO.setText("");
		txtPSVDT.setText("");
		txtPISDT.setText("");
		txtMDVRT.setText("");
		txtSTMCD.setText("");
		txtPMRNO.setText("");
		txtPMRDT.setText("");
		txtPSNNO.setText("");
		txtPSNDT.setText("");
		txtPSTNO.setText("");
		txtPSTDT.setText("");
		chkTCFFL.setSelected(false);
		chkINSFL.setSelected(false);
		chkQARFL.setSelected(false);
		chkSTKFL.setSelected(false);
	
		chkPSVFL.setSelected(false);
		chkBOTFL.setSelected(false);
		chkIMPFL.setSelected(false);
		chkINDFL.setSelected(false);
		chkPROFL.setSelected(false);
		txtPSVFR.setText("");
		txtPSVDT.setText("");
		txtPSVCD.setText("");
		tblLNDTL.clrTABLE();
		tblCTDTL.clrTABLE();
	    tblPRCDT.clrTABLE();
		tblPSVDT.clrTABLE();
	
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		tblLNDTL.clearSelection();
		rdoDESIT.setVisible(false);
		rdoDESIT.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
		{
			rdoPRJIT.setEnabled(true);
			rdoOTMIT.setEnabled(true);
			rdoSTDIT.setEnabled(true);
			rdoDESIT.setEnabled(true);
			cmbMNGRP.setEnabled(false);
			cmbSBGRP.setEnabled(false);
			cmbSSGRP.setEnabled(false);
			txtMATCD.setEnabled(false);
			chkNEWCD.setVisible(true);
			chkNEWCD.setSelected(false);
			txtDDDVL.setEnabled(true);
			txtLVLRF.setEnabled(true);
			cmbUOMCD.setEnabled(true);
		}
		else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst)) 
		{
			rdoPRJIT.setEnabled(false);
			rdoOTMIT.setEnabled(false);
			rdoSTDIT.setEnabled(false);
			rdoDESIT.setEnabled(false);
			cmbMNGRP.setEnabled(true);
			cmbSBGRP.setEnabled(true);
			cmbSSGRP.setEnabled(true);
			txtMATCD.setEnabled(true);
			chkNEWCD.setVisible(false);
			txtDDDVL.setEnabled(false);
			txtLVLRF.setEnabled(false);
			JCheckBox chkEDITR = (JCheckBox)tblCTDTL.getCellEditor(0,0).getTableCellEditorComponent(tblCTDTL,"",true,0,0);
			chkEDITR.setEnabled(true);
		}	
		//if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
		//	txtMATCD.setEnabled(true);
		else
		{
			rdoPRJIT.setEnabled(false);
			rdoOTMIT.setEnabled(false);
			rdoSTDIT.setEnabled(false);
			rdoDESIT.setEnabled(false);
			cmbMNGRP.setEnabled(false);
			cmbSBGRP.setEnabled(false);
			cmbSSGRP.setEnabled(false);
			txtMATCD.setEnabled(true);
			chkNEWCD.setVisible(false);
			txtDDDVL.setEnabled(false);
			txtLVLRF.setEnabled(false);
			txtMATCD.requestFocus();
		}
		txtMATDS.setEnabled(L_STAT);
		tblPRCDT.setEnabled(false);
		//tblCTDTL.setEditable(false);
		txtPPONO.setEnabled(false);
		txtPPODT.setEnabled(false);
		txtPPORT.setEnabled(false);
		txtPGRNO.setEnabled(false);
		txtPGRDT.setEnabled(false);
		txtPGRRT.setEnabled(false);
		txtPISNO.setEnabled(false);
		txtPISDT.setEnabled(false);
		txtMDVRT.setEnabled(false);
		txtSTMCD.setEnabled(false);
	    txtPMRNO.setEnabled(false);
		txtPMRDT.setEnabled(false);
		txtPSNNO.setEnabled(false);
		txtPSNDT.setEnabled(false);
		txtPSTNO.setEnabled(false);
		txtPSTDT.setEnabled(false);
		txtPSVDT.setEnabled(false);
		txtSTKQT.setEnabled(false);
		chkQARFL.setEnabled(false);
		chkSTKFL.setEnabled(false);
		if(strCLSNM.equals("mm_mectm"))
		{
			txtILDTM.setEnabled(false);
			txtELDTM.setEnabled(false);
			// if required uncomment
			/*if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				cmbUOMCD.setEnabled(true);
			else
				cmbUOMCD.setEnabled(false);*/
		}
		else
		{
			txtILDTM.setEnabled(true);
			txtELDTM.setEnabled(true);
		}
		//tblLNDTL.setEnabled(true);
		tblLNDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
		tblLNDTL.cmpEDITR[TBL_LINNO].setEnabled(false);
		//txtPSVCD.setEditable(false);
	}
	boolean vldDATA()
	{
		try
		{  
			setMSG("",'N');
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				if(flgERRFL)  // P.O, Indent raised or obsolete
				{
					setMSG("Modification is not allowed for this item..",'E');
					return false;
				}
			}
			if((txtILDTM.getText().trim().length() ==0)||(txtELDTM.getText().trim().length() ==0))
				getLEDTM(txtMATCD.getText().trim());
			
			if(txtILDTM.getText().trim().length() ==0)
			  	txtILDTM.setText(strILDTM);
			if(txtELDTM.getText().trim().length() ==0)
				txtELDTM.setText(strELDTM);
			
			if(txtMATCD.getText().trim().length() !=10)
			{
				setMSG("Item code can not be less than 10 digits..",'E');
				return false;
			}
			else if(txtMATDS.getText().trim().length() == 0)
			{
				setMSG("Please enter the material Description..",'E');
				txtMATDS.requestFocus();
				return false;
			}
			else if(txtLVLRF.getText().trim().length() != 2)
			{
				setMSG("Please enter a two digit Level Ref..e.g 00,01 ..",'E');
				txtLVLRF.requestFocus();
				return false;
			}
			else if(cmbINDTG.getSelectedIndex() ==0)
			{
				setMSG("Please select the Indicator Tag..",'E');
				cmbINDTG.requestFocus();
				return false;
			}
			else if(cmbUOMCD.getSelectedIndex() ==0)
			{
				setMSG("Please select the UOM Code..",'E');
				cmbUOMCD.requestFocus();
				return false;
			}
			else if(cmbDPTCD.getSelectedIndex() ==0)
			{
				setMSG("Please select the Owner..",'E');
				cmbDPTCD.requestFocus();
				return false;
			}
			else if(cmbUSGBY.getSelectedIndex() ==0)
			{
				setMSG("Please select the Usgae..999 for common usage",'E');
				cmbUSGBY.requestFocus();
				return false;
			}
			else if(chkPSVFL.isSelected())
			{
				if(txtPSVFR.getText().trim().length() ==0)
				{
					setMSG("Enter the preservation details..",'E');
					txtPSVFR.requestFocus();
					return false;
				}
				else if(txtPSVCD.getText().trim().length() ==0)
				{
					setMSG("Enter the preservation details..",'E');
					return false;
				}
			}
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY = " Select * from CO_CTMST where CT_CODTP = 'CD' AND ";
				M_strSQLQRY += " ct_matcd ='"+txtMATCD.getText().trim()+"'";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
					if(M_rstRSSET.next())
					{
						setMSG("Given Item Code already exists, Enter some other serial..",'E');		
						return false;
					}
			}	
			if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst)))
			{
				int L_intCNT =0;
				for(int i=0;i<tblLNDTL.getRowCount();i++)
				{
					if(tblLNDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						L_intCNT++;
						if(tblLNDTL.getValueAt(i,TBL_LINDS).toString().length() ==0)
						{
							setMSG("Line Description can not be blank..",'E');
							return false;
						}
					}
				}
				
				if(L_intCNT ==0)
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						setMSG("Enter the Line number details in Item Detials ..",'E');
					else
						setMSG("Select at least one line for modification ,in Item Detials ..",'E');
					return false;
				}
				if(Integer.parseInt(txtILDTM.getText().trim()) == 0)
				{
					setMSG("Int. Lead Time can not be zero..",'E');
					return false;
				}
				if(Integer.parseInt(txtELDTM.getText().trim()) == 0)
				{
					setMSG("Ext.Lead Time can not be blank..",'E');
					return false;
				}
			}
			// Checking special char. in item description
			for(int i=0;i<txtMATDS.getText().trim().length();i++)
			{
				if((txtMATDS.getText().indexOf("'")>=0)||(txtMATDS.getText().indexOf("\"")>=0)||(txtMATDS.getText().indexOf("\\")>=0))
				{
					setMSG("Special characters are not allowed in material desc ",'E');
					return false;
				}
			}
			for(int j=0;j<tblLNDTL.getRowCount();j++)
				if(tblLNDTL.getValueAt(j,TBL_CHKFL).toString().equals("true"))
					for(int i=0;i<tblLNDTL.getValueAt(j,TBL_LINDS).toString().trim().length();i++)
					{
						if((tblLNDTL.getValueAt(j,TBL_LINDS).toString().indexOf("'")>=0)||(tblLNDTL.getValueAt(j,TBL_LINDS).toString().indexOf("\"")>=0)||(tblLNDTL.getValueAt(j,TBL_LINDS).toString().indexOf("\\")>=0))
						{
							setMSG("Special characters are not allowed in Line description ",'E');
							return false;
						}
					}
		}catch(Exception L_E)
		{
			setMSG(L_E,"vldDATA");
			return false;
		}
			
		return true;
	}
	
	void exeSAVE()
	{
		try
		{
			if(vldDATA())
			{
				cl_dat.M_flgLCUPD_pbst = true;
			String L_strORAQRY ="";
			String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
		//	O_FOUT = new FileOutputStream("c:\\reports\\oradtr.sql");
		//	O_DOUT = new DataOutputStream(O_FOUT);
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
			{
				/*M_strSQLQRY ="UPDATE CO_CTMST SET ";
				M_strSQLQRY +=getUSGDTL("CT",'U',"X");
				M_strSQLQRY +="where CT_GRPCD ='"+strMNGRP+"'";
				M_strSQLQRY +=" AND CT_CODTP ='CD' AND CT_MATCD ='"+txtMATCD.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
		
				M_strSQLQRY ="UPDATE CO_CTTRN SET ";
				M_strSQLQRY +=getUSGDTL("CTT",'U',"X");
				M_strSQLQRY +="where CTT_GRPCD ='"+strMNGRP+"'";
				M_strSQLQRY +=" AND CTT_CODTP ='CD' AND CTT_MATCD ='"+txtMATCD.getText().trim()+"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	*/
				for(int i=0;i<tblLNDTL.getRowCount();i++)
				{
					strMNGRP = txtMATCD.getText().trim().substring(0,2);
					if(tblLNDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						pstmDELCTT.setString(1,cl_dat.M_strUSRCD_pbst);
						pstmDELCTT.setDate(2,Date.valueOf(L_strDATE));
						pstmDELCTT.setString(3,strMNGRP);
						pstmDELCTT.setString(4,txtMATCD.getText().trim());
						pstmDELCTT.setString(5,txtLVLRF.getText().trim());
						pstmDELCTT.setString(6,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
						pstmDELCTT.executeUpdate();
					}
				}
				
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				M_strSQLQRY = "INSERT INTO CO_CTMST(CT_GRPCD,CT_CODTP,CT_MATCD,CT_MATDS,CT_DSCTP,CT_UOMCD,CT_OWNBY,CT_USGBY,"+
							  "CT_LVLRF,CT_PRTNO,CT_MATRF,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,CT_TCFFL,CT_INSFL,CT_BOTFL,CT_IMPFL,CT_INDFL,CT_PROFL,"+
							  "CT_PSVFL,CT_PSVCD,CT_PSVFR,CT_PKGTP,CT_PKGQT,CT_TRNFL,CT_STSFL,CT_LUSBY,CT_LUPDT)VALUES("+
							  "'"+strMNGRP+"','CD',"+
							  "'"+txtMATCD.getText().trim()+"',"+
							  "'"+txtMATDS.getText().trim().toUpperCase()+"',";
							  if(rdoSTDIT.isSelected())
								M_strSQLQRY +="'S',";
							  else			  
									M_strSQLQRY +="'D',";
				M_strSQLQRY += "'"+cmbUOMCD.getSelectedItem().toString().substring(0,3).trim()+"',"+
							  "'"+cmbDPTCD.getSelectedItem().toString().substring(0,3).trim()+"',"+
							   "'"+cmbUSGBY.getSelectedItem().toString().substring(0,3).trim()+"',"+
							  "'"+txtLVLRF.getText().trim().toUpperCase()+"',"+
							  "'"+txtPRTNO.getText().trim().toUpperCase()+"',"+
							  "'"+txtCRSRF.getText().trim().toUpperCase()+"',";
							 if((txtMATCD.getText().trim().substring(9).equals("2"))||(txtMATCD.getText().trim().substring(9).equals("6")))
							 {
								 M_strSQLQRY +=	"0,0,";
								 if(txtILDTM.getText().trim().length()>0)
									M_strSQLQRY += txtILDTM.getText().trim()+",";
								else
								M_strSQLQRY +=	"0,";
								if(txtELDTM.getText().trim().length()>0)
									M_strSQLQRY +=txtELDTM.getText().trim()+",";
								else
									 M_strSQLQRY +=	"0,";
							 }
							 else
							 {
								if(txtILDTM.getText().trim().length()>0)
									M_strSQLQRY += txtILDTM.getText().trim()+",";
								 else
									M_strSQLQRY +=	"0,";
								if(txtELDTM.getText().trim().length()>0)
									M_strSQLQRY +=txtELDTM.getText().trim()+",";
								else
									 M_strSQLQRY +=	"0,";
								M_strSQLQRY +=	"0,0,";
							 }
							  if(chkTCFFL.isSelected())
								M_strSQLQRY += "'Y',"; 
							  else
								M_strSQLQRY += "'N',"; 
							  if(chkINSFL.isSelected())
								M_strSQLQRY += "'Y',"; 
							  else
								M_strSQLQRY += "'N',"; 
							  if(chkBOTFL.isSelected())
								M_strSQLQRY += "'Y',"; 
							  else
								M_strSQLQRY += "'N',"; 
							  if(chkIMPFL.isSelected())
								M_strSQLQRY += "'Y',"; 
							  else
								M_strSQLQRY += "'N',"; 
							  if(chkINDFL.isSelected())
								M_strSQLQRY += "'Y',"; 
							  else
								M_strSQLQRY += "'N',"; 
							  if(chkPROFL.isSelected())
								M_strSQLQRY += "'Y',"; 
							  else
								M_strSQLQRY += "'N',"; 
							  if(chkPSVFL.isSelected())
								M_strSQLQRY += "'Y',"; 
							  else
								M_strSQLQRY += "'N',"; 
							  M_strSQLQRY +="'"+txtPSVCD.getText().trim().toUpperCase()+"',";
							  if(txtPSVFR.getText().trim().length() >0)
								M_strSQLQRY +=txtPSVFR.getText().trim()+",";
							  else
								M_strSQLQRY +="0,";
						//	 M_strSQLQRY +="'"+txtSHFLF.getText().trim().toUpperCase()+"',";
							 M_strSQLQRY +="'"+txtSTDPK.getText().trim().toUpperCase()+"',";
							 if(txtMPKQT.getText().trim().length() >0)
								M_strSQLQRY +=txtMPKQT.getText().trim().toUpperCase()+",";
							 else
								 M_strSQLQRY +="0,";
							 M_strSQLQRY +=getUSGDTL("CT",'I',"0");
							 M_strSQLQRY +=")";
							 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
					
				/*	L_strORAQRY = "INSERT INTO MV_CATMAST(CT_MATCD,CT_LEVEL,CT_CREF,CT_PARTNO,"+
							      "CT_PFLAG,CT_UOM,CT_OBTAG,CT_PKLOT,CT_LUID,CT_LUPD,CT_APRFL,"+
							      "CT_LEADTM,CT_LEADTM_EXT,CT_LEADTM_CAT)values";
					O_DOUT.writeBytes(L_strORAQRY);
					O_DOUT.writeBytes("\n");
					L_strORAQRY = "('"+txtMATCD.getText().trim()+"',"+txtLVLRF.getText().trim()+","+		 
							       "'"+txtCRSRF.getText().trim()+"',"+"'"+txtPRTNO.getText().trim()+"',"+
							       "' ',"+ 
							       "'"+cmbUOMCD.getSelectedItem().toString().substring(0,3).trim()+"',"+ "'N',";
								  if(txtILDTM.getText().trim().length() >0) 
									L_strORAQRY += txtMPKQT.getText().trim()+",";
								  else
									L_strORAQRY +=  "0,";
								   L_strORAQRY += "'"+cl_dat.M_strUSRCD_pbst +"',"+"'"+cl_dat.M_strLOGDT_pbst +"',"+"'Y',";
							       if(txtILDTM.getText().trim().length() >0)
										L_strORAQRY +=  txtILDTM.getText() +",";
							       else
										L_strORAQRY +=  "0,";
								   if(txtELDTM.getText().trim().length() >0)
										L_strORAQRY +=  txtELDTM.getText() +",";
							       else
										L_strORAQRY +=  "0,";
							       L_strORAQRY += "' ')";
					O_DOUT.writeBytes(L_strORAQRY +" ;"); */
							 
				// insert into co_cttrn
				for(int i=0;i<tblLNDTL.getRowCount();i++)
				{
					if(tblLNDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
					{
						pstmINSCTT.setString(1,strMNGRP);
						pstmINSCTT.setString(2,"CD");
						pstmINSCTT.setString(3,txtMATCD.getText().trim());
						pstmINSCTT.setString(4,txtLVLRF.getText().trim());
						pstmINSCTT.setString(5,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
						pstmINSCTT.setString(6,tblLNDTL.getValueAt(i,TBL_LINDS).toString().toUpperCase());
						if(tblLNDTL.getValueAt(i,TBL_PRTFL).toString().equals("true"))
							pstmINSCTT.setString(7,"Y");
						else
							pstmINSCTT.setString(7,"N");
						pstmINSCTT.setString(8,"0");
						pstmINSCTT.setString(9,"0");
						pstmINSCTT.setString(10,cl_dat.M_strUSRCD_pbst);
						pstmINSCTT.setDate(11,Date.valueOf(L_strDATE));
						pstmINSCTT.executeUpdate();
							
						/*L_strORAQRY = "INSERT INTO MV_CATDESC(CD_MATCD,CD_LEVELNO,CD_LINENO,CD_DESC,CD_PFLAG)VALUES("+
						              "'"+txtMATCD.getText().trim() +"',"+txtLVLRF.getText().trim()+","+ 
									  tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase() +","+ 
						              "'"+tblLNDTL.getValueAt(i,TBL_LINDS).toString().toUpperCase() +"',";
									  if(tblLNDTL.getValueAt(i,TBL_PRTFL).toString().equals("true"))
										 L_strORAQRY +=	"'Y')";
						              else
										 L_strORAQRY +=	"'N')";
						O_DOUT.writeBytes(L_strORAQRY + " ;");*/
					
					}
				}
				if(chkNEWCD.isSelected())
				{
					for(int i=0;i<tblPSVDT.getRowCount();i++)
					{
						if(tblPSVDT.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							M_strSQLQRY = "INSERT INTO MM_PVMST(PV_CMPCD,PV_PSVCD,PV_ACTSR,PV_ACTDS,PV_STSFL,PV_TRNFL,PV_LUSBY,PV_LUPDT)VALUES(";
							M_strSQLQRY += "'" + cl_dat.M_strCMPCD_pbst + "'," ;
							M_strSQLQRY += "'" + txtPSVCD.getText().trim() + "'," ;
							M_strSQLQRY += "'" + tblPSVDT.getValueAt(i,TB4_ACTSR).toString()+"',";
							M_strSQLQRY += "'" + tblPSVDT.getValueAt(i,TB4_ACTDS).toString()+"',";
							M_strSQLQRY +=getUSGDTL("PV",'I'," ")+")";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
						}
					}
				}
			}
			else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst) || cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				if(txtMATCD.getText().length() < 10)
				{
					setMSG("Modification is allowed at Item Code level only..",'E');
					return;
				}
				strMNGRP = txtMATCD.getText().trim().substring(0,2);
				String L_strSTSFL = cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst) ? "1" : "0";
				M_strSQLQRY ="UPDATE CO_CTMST SET ";
				M_strSQLQRY +="CT_MATDS =" + "'"+txtMATDS.getText().trim().toUpperCase()+"',"+
							  "CT_UOMCD =" + "'"+cmbUOMCD.getSelectedItem().toString().substring(0,3).trim()+"',"+
							  "CT_OWNBY =" + "'"+cmbDPTCD.getSelectedItem().toString().substring(0,3).trim()+"',"+
							  "CT_USGBY =" + "'"+cmbUSGBY.getSelectedItem().toString().substring(0,3).trim()+"',"+
					//		  "CT_LVLRF =" + "'"+txtLVLRF.getText().trim().toUpperCase()+"',"+
							  "CT_PRTNO =" + "'"+txtPRTNO.getText().trim().toUpperCase()+"',"+
							  "CT_MATRF =" + "'"+txtCRSRF.getText().trim().toUpperCase()+"',";
							  if(txtILDTM.getText().trim().length() >0)
							  {
								   if((txtMATCD.getText().trim().substring(9).equals("2"))||(txtMATCD.getText().trim().substring(9).equals("6")))
										 M_strSQLQRY += "CT_IILTM =" + txtILDTM.getText().trim()+",";
								   else
									    M_strSQLQRY += "CT_ILDTM =" + txtILDTM.getText().trim()+",";
							  }
								if(txtILDTM.getText().trim().length() >0)
								{
									if((txtMATCD.getText().trim().substring(9).equals("2"))||(txtMATCD.getText().trim().substring(9).equals("6")))
										M_strSQLQRY += "CT_IELTM =" + txtELDTM.getText().trim()+",";
									else
										M_strSQLQRY += "CT_ELDTM =" + txtELDTM.getText().trim()+",";
								}
							  if(chkTCFFL.isSelected())
								M_strSQLQRY += "CT_TCFFL ="+"'Y',"; 						  
							  else
								M_strSQLQRY += "CT_TCFFL ="+"'N',"; 
							  if(chkINSFL.isSelected())
								M_strSQLQRY += "CT_INSFL ="+"'Y',"; 
							  else
								M_strSQLQRY += "CT_INSFL ="+"'N',"; 
							  if(chkBOTFL.isSelected())
								M_strSQLQRY += "CT_BOTFL ="+"'Y',"; 
							  else
								M_strSQLQRY += "CT_BOTFL ="+"'N',"; 
							  if(chkIMPFL.isSelected())
								M_strSQLQRY += "CT_IMPFL ="+"'Y',"; 
							  else
								M_strSQLQRY += "CT_IMPFL ="+"'N',"; 
							  if(chkINDFL.isSelected())
								M_strSQLQRY += "CT_INDFL ="+"'Y',"; 
							  else
								M_strSQLQRY += "CT_INDFL ="+"'N',"; 
							  if(chkPROFL.isSelected())
								M_strSQLQRY += "CT_PROFL ="+"'Y',"; 
							  else
								M_strSQLQRY += "CT_PROFL ="+"'N',"; 
							  if(chkPSVFL.isSelected())
								M_strSQLQRY += "CT_PSVFL ="+"'Y',"; 
							  else
								M_strSQLQRY += "CT_PSVFL ="+"'N',"; 
							  
							 if(txtPSVFR.getText().trim().length()>0) 
							 M_strSQLQRY +="CT_PSVFR ="+txtPSVFR.getText().trim()+",";
							 M_strSQLQRY +="CT_PSVCD ="+"'"+txtPSVCD.getText().trim().toUpperCase()+"',";
							 M_strSQLQRY +=" CT_PKGTP ="+"'"+txtSTDPK.getText().trim().toUpperCase()+"',";
							 if(txtMPKQT.getText().trim().length() >0)
								M_strSQLQRY +="CT_PKGQT ="+txtMPKQT.getText().trim().toUpperCase()+",";
							 //else
							 //	 M_strSQLQRY +="0,";
							 if(chkOBSFL.isSelected())
								M_strSQLQRY +=getUSGDTL("CT",'U',"9"); // Flag for Obsolete MAterial
							 else
							 	M_strSQLQRY +=getUSGDTL("CT",'U',L_strSTSFL);
							 M_strSQLQRY +="where CT_GRPCD ='"+strMNGRP+"'";
							 M_strSQLQRY +=" AND CT_CODTP ='CD' AND CT_MATCD ='"+txtMATCD.getText().trim()+"'";
							 cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
				
							 M_strSQLQRY =" UPDATE MM_STMST SET ST_MATDS ='"+txtMATDS.getText().trim().toUpperCase()+"',"+
							 " ST_UOMCD ='"+cmbUOMCD.getSelectedItem().toString().substring(0,3)+"',"+
							 getUSGDTL("ST",'U',null);
				M_strSQLQRY += " WHERE ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MATCD ='"+txtMATCD.getText().trim() +"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");	
				M_strSQLQRY =" UPDATE MM_STPRC SET STP_MATDS ='"+txtMATDS.getText().trim().toUpperCase()+"',"+
							 " STP_UOMCD ='"+cmbUOMCD.getSelectedItem().toString().substring(0,3)+"',"+
							 " STP_OWNBY ='"+cmbDPTCD.getSelectedItem().toString().substring(0,3)+"'";
				M_strSQLQRY += " WHERE STP_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND STP_MATCD ='"+txtMATCD.getText().trim() +"'";
				cl_dat.exeSQLUPD(M_strSQLQRY,"");	
				
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
								pstmUPDCTT.setString(5,strMNGRP);
								pstmUPDCTT.setString(6,txtMATCD.getText().trim());
								pstmUPDCTT.setString(7,txtLVLRF.getText().trim());
								pstmUPDCTT.setString(8,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
								pstmUPDCTT.executeUpdate();
							}
							else
							{
								pstmINSCTT.setString(1,strMNGRP);
								pstmINSCTT.setString(2,"CD");
								pstmINSCTT.setString(3,txtMATCD.getText().trim());
								pstmINSCTT.setString(4,txtLVLRF.getText().trim());
								pstmINSCTT.setString(5,tblLNDTL.getValueAt(i,TBL_LINNO).toString().toUpperCase());
								pstmINSCTT.setString(6,tblLNDTL.getValueAt(i,TBL_LINDS).toString().toUpperCase());
								if(tblLNDTL.getValueAt(i,TBL_PRTFL).toString().equals("true"))
									pstmINSCTT.setString(7,"Y");
								else
									pstmINSCTT.setString(7,"N");
								pstmINSCTT.setString(8,"0");
								pstmINSCTT.setString(9,"0");
								pstmINSCTT.setString(10,cl_dat.M_strUSRCD_pbst);
								pstmINSCTT.setDate(11,Date.valueOf(L_strDATE));
								pstmINSCTT.executeUpdate();
							}
							
						}
						
				//	}
				}
			}
			if(cl_dat.exeDBCMT("exeSAVE"))
			{
				  setMSG("Record saved successfully",'N');
				  rdoSTDIT.requestFocus();
				  //txtMATCD.requestFocus();
				  clrCOMP();
				  setMSG("Record saved successfully",'N');
			}
		}
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"exeSAVE");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	private Vector<String> getMGLST()
	{
		Vector<String> L_vtrMNGRP = new Vector<String>();
		try
		{
			M_strSQLQRY = "Select distinct CT_GRPCD,CT_MATDS from CO_CTMST WHERE ";
			M_strSQLQRY += "CT_CODTP = 'MG' ";
			M_strSQLQRY += "and isnull(CT_STSFL,'')  <> 'X' ";
			M_strSQLQRY += " order by CT_GRPCD";
			String L_strDATA ="";	
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET != null)	
			while(M_rstRSSET.next()){
			  	L_strDATA = nvlSTRVL(M_rstRSSET.getString("CT_GRPCD"),"") +" "+nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),"");
				L_vtrMNGRP.add(L_strDATA);
			}
		}
		catch(SQLException L_SE)
		{
			setMSG(L_SE,"Action on catalog Options");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		return L_vtrMNGRP;
	}
/*private boolean vldLINNO(String P_strLINNO,int P_intROWCNT)
{
	try
	{
		if(rdoSTDIT.isSelected())
		{
			return((Double.parseDouble(P_strLINNO) >=90)&&(Double.parseDouble(P_strLINNO) <=99));
		}
		for(int i=0;i<P_intROWCNT;i++)
		{
			if(tblLNDTL.getValueAt(i,TBL_LINNO).toString().trim().equals(P_strLINNO.trim()))
			{
				setMSG("Duplicate Line number ..",'E');
				return false;
			}
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"vlDLINNO");		
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
	return true;
}*/

public void mouseClicked(MouseEvent L_ME)
{
	if(L_ME.getSource() == jtpCTDTL)
	{
		if(jtpCTDTL.getSelectedIndex() ==1)
		{
			tblLNDTL.clearSelection();
			tblLNDTL.setRowSelectionInterval(0,0);
			tblLNDTL.setColumnSelectionInterval(TBL_LINNO,TBL_LINDS);
			tblLNDTL.setEditingRow(tblLNDTL.getSelectedRow());
			tblLNDTL.setEditingColumn(2);
			tblLNDTL.editCellAt(0,2);
			//tblLNDTL.cmpEDITR[TBL_LINDS].requestFocus();
			tblLNDTL.requestFocus();
			setMSG("Enter the Line wise Description details..",'N');
		}
	}
	else if(L_ME.getSource() == tblCTDTL)
	{
		if(tblCTDTL.getSelectedColumn() == TBL_CHKFL)
		{
			
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
			{
				int L_intSELROW = tblCTDTL.getSelectedRow();
				for(int i=0;i<tblCTDTL.getRowCount();i++)
				{
					if(i != L_intSELROW)
						tblCTDTL.setValueAt(new Boolean(false),i,TBL_CHKFL);
				}
				strMATCD = tblCTDTL.getValueAt(L_intSELROW,TBL_MATCD).toString();
				if(strMATCD.length() > 0)
				{
					txtMATCD.setText(strMATCD);
					exeSELREC();
					getPRCDT();///// modi
				}
				else
				{
					setMSG("InValid Selection ..",'E');
				}
			}
		}
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
		if(input == txtMATDS)
		{
			if(txtMATDS.getText().length() > 60)
			{
				setMSG("Material desc. can not be blank or more than 60 characters",'E');
				return false;
			}
			else 
			{
				for(int i=0;i<txtMATDS.getText().trim().length();i++)
				{
					if((txtMATDS.getText().indexOf("'")>=0)||(txtMATDS.getText().indexOf("\"")>=0)||(txtMATDS.getText().indexOf("\\")>=0))
					{
						setMSG("Special characters are not allowed in material desc ",'E');
						return false;
					}
					else
					{
						//if(!cmbMNGRP.getSelectedItem().toString().substring(0,2).equals("99"))
						/*if(!strMNGRP.equals("99"))
						{
								//getLEDTM(strMNGRP+strSBGRP+strSSGRP);
								getLEDTM(txtMATCD.getText().trim());
								txtILDTM.setText(strILDTM);
								txtELDTM.setText(strELDTM);
						}*/
						return true;
					}
				}
			}
			return false;
		}
		else if(input == txtMATCD)
		{
			try
			{
			if(txtMATCD.getText().trim().length() ==10)
			{
				M_strSQLQRY ="Select * from CO_CTMST WHERE CT_CODTP IN('CD','CR') AND CT_MATCD ='"+txtMATCD.getText().trim() +"'";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							setMSG("Material Code alredy exist,Enter some other serial..",'E');
							return false;
						}
						else
						{	
							return true;
						}
					}
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					{
						setMSG("Material Code does not exist..",'E');
						return false;
					}
					return true;
				}
			}
			else return true;
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"MATCD verifier..");
			}
		}
		else if(input == txtLVLRF)
		{
			try
			{
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
					return true;
				if(txtLVLRF.getText().trim().length() < 2)
				{
					setMSG("Enter a two digit Level Number (e.g. 00,01..)..",'E');
					return false;
				}
				else
				{
					if(cmbMNGRP.getSelectedItem().toString().substring(0,2).equals("99"))
					{
						if(txtLVLRF.getText().trim().equals("05"))
						{
							//setMSG("Invalid Level Number..",'E');
							return true;
						}
						else
						{
							setMSG("Invalid Level Number..",'E');
							return false;
						}
					}
					else if(cmbMNGRP.getSelectedItem().toString().substring(0,2).equals(strPRJGP))
					{
						if(txtLVLRF.getText().trim().equals("05"))
						{
							//setMSG("Invalid Level Number..",'E');
							return true;
						}
						else
						{
							setMSG("Invalid Level Number..",'E');
							return false;
						}
					}
					else
					{
						
						M_strSQLQRY = "Select DISTINCT CTT_LVLNO FROM CO_CTTRN WHERE SUBSTRING(CTT_MATCD,1,6) LIKE '"+cmbMNGRP.getSelectedItem().toString().substring(0,2).toString()+cmbSBGRP.getSelectedItem().toString().substring(0,2).toString()+cmbSSGRP.getSelectedItem().toString().substring(0,2).toString()+"' AND CTT_LVLNO ='" +txtLVLRF.getText().trim() +"'";
						System.out.println(M_strSQLQRY);
						M_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
						if(M_rstRSSET !=null)
							if(M_rstRSSET.next())
							{
								return true;
							}
						else
						{
							setMSG("Given Level Number does not exist ..",'E');
							return false;
						}
					}
					return true;
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"verifier");
				return false;
			}
		}
		else if(input == txtMPKQT)
		{
			if(Double.parseDouble(txtMPKQT.getText()) <=0)
			{
				setMSG("Invalid value of Minimum packing quantity ..",'E');
					return false;
			}
			else return true;
		
		}
		else if(input == txtDDDVL)
		{
			try
			{
				if(txtDDDVL.getText().length() !=3)
			    {
					setMSG("Enter a three digit serial no..",'E');
					return false;
			    }
			if((txtMATCD.getText().length() >=9)&&(txtDDDVL.getText().length() ==3))
			{
				txtMATCD.setText(txtMATCD.getText().trim().substring(0,6)+txtDDDVL.getText().trim() + cmbINDTG.getSelectedItem().toString().substring(0,2));
				if(txtMATCD.getText().trim().length() ==10)
			   {
				M_strSQLQRY ="Select * from CO_CTMST WHERE CT_CODTP ='CD' AND CT_MATCD ='"+txtMATCD.getText().trim() +"'";
				System.out.println(M_strSQLQRY);
				M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
				if(M_rstRSSET !=null)
				{
					if(M_rstRSSET.next())
					{
						if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
						{
							setMSG("Material Code already exist,Enter some other serial..",'E');
							return false;
						}
						else
						{	
							return true;
						}
					}
					
					return true;
				}
			}

			}
			else
			{
				setMSG("Select the MG,SG and SS groups and enter a three digit serial no..",'E');
			}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"DDD verifier");
			}
		}
		return false;	
	}
}
private class mm_mectmTBLINVFR extends TableInputVerifier
{
	public boolean verify(int P_intROWID,int P_intCOLID)
	{
		try
		{
			int L_intLINNO =0;
			
			if(P_intCOLID==TBL_LINDS)
			{
				strTEMP =txtLINDS.getText().trim();
				if(strTEMP.length() ==0)
					return true;
				for(int i=0;i<txtLINDS.getText().trim().length();i++)
				{
					if((txtLINDS.getText().indexOf("'")>=0)||(txtLINDS.getText().indexOf("\"")>=0)||(txtLINDS.getText().indexOf("\\")>=0))
					{
						setMSG("Special characters are not allowed in Line description ",'E');
						return false;
					}
				}
				if(P_intROWID == 0)	
				{
					if(rdoSTDIT.isSelected())
						tblLNDTL.setValueAt("91",P_intROWID,TBL_LINNO);
					else
						tblLNDTL.setValueAt("01",P_intROWID,TBL_LINNO); // descriptive
					
				}
				else
				{
					if(rdoSTDIT.isSelected())
					{
						if(P_intROWID > 8)
						{
							setMSG("Line No. can not exceed 99..",'E');
							return false;
						}
						L_intLINNO = 91 +P_intROWID;
					}
					else
						L_intLINNO = 1 +P_intROWID;
					strTEMP = String.valueOf(L_intLINNO);
					if(strTEMP.length() ==1)
						strTEMP ="0"+strTEMP;
					tblLNDTL.setValueAt(strTEMP,P_intROWID,TBL_LINNO);
				}
				strTEMP ="";
				tblLNDTL.setValueAt(Boolean.TRUE,P_intROWID,TBL_PRTFL);
				txtLINDS.setText(txtLINDS.getText().trim().toUpperCase());
			}
			if(P_intCOLID==TBL_LINNO)
			{
				//if(!vldLINNO(txtLINNO.getText().trim(),tblLNDTL.getSelectedRow()))
				strTEMP =txtLINNO.getText().trim();
				if(strTEMP.length() ==0)
					return true;
				else if(strTEMP.length() < 2)
				{
					setMSG("Enter a two digit line no..",'E');
					return false;
				}
				if(rdoSTDIT.isSelected())
				if(!(Double.parseDouble(strTEMP) >=90)&&(Double.parseDouble(strTEMP) <=99))
				{
					setMSG("Enter the line number between 90 to 99..",'E');
					return false;
				}
				if(P_intROWID >0)
				for(int i=0;i<P_intROWID-1;i++)
				{
					if(tblLNDTL.getValueAt(i,TBL_LINNO).toString().trim().equals(strTEMP))
					{
						setMSG("Duplicate Line number ..",'E');
						return false;
					}
				}
				tblLNDTL.setValueAt(Boolean.TRUE,P_intROWID,TBL_PRTFL);
			}				
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"Table verifier");			
		}
		return true;
	}
		
}
private void getLEDTM(String L_strMATDT)
{
	String L_strSQLQRY;
	ResultSet L_rstRSSET;
	String L_strILDTM="0",L_strELDTM="0",L_strCODTP;
	strILDTM = "0";
	strELDTM ="0";
	try
	{
		/*L_strSQLQRY ="select ct_codtp,ct_ildtm,ct_eldtm,ct_iiltm,ct_ieltm from CO_CTMST WHERE CT_CODTP ='SS' AND substr(CT_MATCD,1,6) ='"+L_strMATDT.substring(0,6) +"'"
					+ " UNION "
					+ "select ct_codtp,ct_ildtm,ct_eldtm,ct_iiltm,ct_ieltm from CO_CTMST WHERE CT_CODTP ='SG' AND substr(CT_MATCD,1,4) ='"+L_strMATDT.substring(0,4) +"'"
					+ " UNION "
					+ " select ct_codtp,ct_ildtm,ct_eldtm,ct_iiltm,ct_ieltm from CO_CTMST WHERE CT_CODTP ='MG' AND substr(CT_MATCD,1,2) ='"+L_strMATDT.substring(0,2) +"'"
					+ " order by CT_CODTP DESC";*/
		// picking from Lead time from sub group /main group is blocked on 24/11/04 API HBP
		L_strSQLQRY ="select ct_codtp,ct_ildtm,ct_eldtm,ct_iiltm,ct_ieltm from CO_CTMST WHERE CT_CODTP ='SS' AND SUBSTRING(CT_MATCD,1,6) ='"+L_strMATDT.substring(0,6) +"'"
					+ " order by CT_CODTP DESC";
					System.out.println(L_strSQLQRY);
		L_rstRSSET = cl_dat.exeSQLQRY(L_strSQLQRY);
		if(L_rstRSSET !=null)
			while(L_rstRSSET.next())
			{
				L_strCODTP = L_rstRSSET.getString("CT_CODTP");
				if(L_strCODTP.equals("SS"))
				{
					if((L_strMATDT.substring(9).equals("2"))||(L_strMATDT.substring(9).equals("6"))||(L_strMATDT.substring(9).equals("9")))
					{
						L_strILDTM = nvlSTRVL(L_rstRSSET.getString("CT_IILTM"),"0");
						L_strELDTM = nvlSTRVL(L_rstRSSET.getString("CT_IELTM"),"0");
					}
					else
					{
						L_strILDTM = nvlSTRVL(L_rstRSSET.getString("CT_ILDTM"),"0");
						L_strELDTM = nvlSTRVL(L_rstRSSET.getString("CT_ELDTM"),"0");
					}
				}
			/*	if(L_strILDTM.length() == 0||L_strILDTM.equals("0")||(L_strELDTM.length() == 0)||L_strELDTM.equals("0"))
			    {
					if(L_strCODTP.equals("SG"))
					{
						if((L_strMATDT.substring(9).equals("2"))||(L_strMATDT.substring(9).equals("6"))||(L_strMATDT.substring(9).equals("9")))
						{
							L_strILDTM = nvlSTRVL(L_rstRSSET.getString("CT_IILTM"),"0");
							L_strELDTM = nvlSTRVL(L_rstRSSET.getString("CT_IELTM"),"0");
						}
						else
						{
							L_strILDTM = nvlSTRVL(L_rstRSSET.getString("CT_ILDTM"),"0");
							L_strELDTM = nvlSTRVL(L_rstRSSET.getString("CT_ELDTM"),"0");
						}
					}
				}
				if(L_strILDTM.length() == 0||L_strILDTM.equals("0")||(L_strELDTM.length() == 0)||L_strELDTM.equals("0"))
			    {
					if(L_strCODTP.equals("MG"))
					{
						if((L_strMATDT.substring(9).equals("2"))||(L_strMATDT.substring(9).equals("6"))||(L_strMATDT.substring(9).equals("9")))
						{
							L_strILDTM = nvlSTRVL(L_rstRSSET.getString("CT_IILTM"),"0");
							L_strELDTM = nvlSTRVL(L_rstRSSET.getString("CT_IELTM"),"0");
						}
						else
						{
							L_strILDTM = nvlSTRVL(L_rstRSSET.getString("CT_ILDTM"),"0");
							L_strELDTM = nvlSTRVL(L_rstRSSET.getString("CT_ELDTM"),"0");
						}
					}
				}*/														  
			}
		if(L_strILDTM.length() == 0||L_strILDTM.equals("0")||(L_strELDTM.length() == 0)||L_strELDTM.equals("0"))
		{
			if((L_strMATDT.substring(9).equals("2"))||(L_strMATDT.substring(9).equals("6"))||(L_strMATDT.substring(9).equals("9")))
		    {
				// Imported
				L_strILDTM = "30";
				L_strELDTM = "90";
			}
			else
			{
				L_strILDTM = "15";
				L_strELDTM = "30";
			}
		}
		txtILDTM.setText(L_strILDTM);
		txtELDTM.setText(L_strELDTM);
		strILDTM = L_strILDTM;
		strELDTM = L_strELDTM;
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getLEDTM");
	}
	
}
private void getPRCDT()
{
	try
	{
		setCursor(cl_dat.M_curWTSTS_pbst);
	//	if(M_objSOURC == txtMATCD)
	//	{
		//System.out.println("getPRCDT");
			String L_strLVLNO ="";
			String L_strMATCD = txtMATCD.getText().trim();//tblINDTL.getValueAt(tblINDTL.getSelectedRow(),TBL_MATCD).toString();
			if(L_strMATCD.length() !=10)
			{
				//setMSG("F9 help is allowed for full length of material code..",'E');
				//setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			M_strSQLQRY = "SELECT CT_LVLRF from CO_CTMST where ct_codtp ='CD' and ct_matcd ='"+L_strMATCD +"'";
			M_strSQLQRY += " AND isnull(ct_stsfl,'') <>'X'";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
				if(M_rstRSSET.next())
					L_strLVLNO = nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),"");
			cl_dat.M_flgHELPFL_pbst = true;
			M_strSQLQRY = "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='MG' ";
			M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,2) +"0000000A'";
			M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
			M_strSQLQRY += "UNION ";
			M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SG' ";
			M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,4) +"00000A'";
			M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
					
			M_strSQLQRY += "UNION ";
			M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
			M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,6) +"000A'";
			M_strSQLQRY += " and CTT_LVLNO ='00'";
			M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
					
			if(!L_strLVLNO.equals("00"))
		   {
				M_strSQLQRY += "UNION ";
				M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
				M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='SS' ";
				M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD.substring(0,6) +"000A'";
				M_strSQLQRY += " and CTT_LVLNO ='"+L_strLVLNO+"'";
				M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
				M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
						
			}
					
			M_strSQLQRY += "UNION ";
			M_strSQLQRY += "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
			M_strSQLQRY += " where CTT_GRPCD = '"+L_strMATCD.substring(0,2) +"'" +" AND CTT_CODTP ='CD' ";
			M_strSQLQRY += " and ctt_matcd = '"+L_strMATCD +"'";
			M_strSQLQRY += " and CTT_LVLNO ='"+L_strLVLNO+"'";
			M_strSQLQRY += " and isnull(CTT_STSFL,'') <>'X'";
			M_strSQLQRY += " and isnull(CTT_PRTFL,'') = 'Y'";
					
			M_strSQLQRY += " Order by ctt_matcd,ctt_lvlno,ctt_linno ";
			System.out.println(M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
			int L_intROWCT =0;
			if(M_rstRSSET != null)	
			while(M_rstRSSET.next())
			{
				tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LINNO"),""),L_intROWCT,TB3_LINNO);
				tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_MATDS"),""),L_intROWCT,TB3_LINDS);
				tblPRCDT.setValueAt(nvlSTRVL(M_rstRSSET.getString("CTT_LVLNO"),""),L_intROWCT,TB3_LVLRF);
				L_intROWCT++;
			}
	//	}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"PRC DET.");
	}
	finally
	{
		setCursor(cl_dat.M_curDFSTS_pbst);
		M_rstRSSET = null;
	}
}
}