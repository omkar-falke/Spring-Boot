import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.*;
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
import java.util.StringTokenizer;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

class mm_teind extends cl_pbase
{
	private JComboBox cmbURGTG,cmbAUTST,cmbFRWTO;
	private JTextField txtINDNO,txtAMDNO,txtINDDT,txtAMDDT,txtINDST,txtPREBY,txtPREDT,txtPRETM,txtAUTDT,txtAUTTM,txtAUTBY,txtFRWDT,txtFRWTM,txtFRWBY,txtITVAL,txtDPTCD,txtDPTDS;
	private JTextField txtUOMCD,txtPKGTP,txtINDRM,txtOTHRM,txtINDVL,txtQTHND,txtQTORD,txtQTIND,txtPPORT;
	private JTextField txtRORLV,txtRORQT,txtMATCD,txtINDQT,txtMATDS,txtREQDT,txtPKGQT,txtEXPDT;
	private JCheckBox chkTCFFL,chkINSFL,chkSTKIT;
	private JTabbedPane jtpINDTL;
	private JPanel pnlGNDTL,pnlVNDTL,pnlINDTL,pnlHISTR;
	private cl_JTable tblINDTL,tblVNDTL;
	private JButton btnFRWD,btnPRNT;
	private JLabel lblMATCD;
	private java.sql.PreparedStatement pstmINSREC,pstmINSRMK,pstmUPDREC,pstmUPDRMK;
	private String strSTRTP,strINDNO,strPRAMD,strSRLNO,strTEMP,strINDRM,strOTHRM,strMATCD,strINDST ="",strMATTP ="",strINDDT="",strFRWTO,strFRWBY,strFRWDT;
	private float fltINDVL ;
	private boolean flgINDRM = false;
	private boolean flgOTHRM = false;
	private boolean flgFRWIN = false;
	private boolean flgEML = false;
	private boolean flgMSCLK = false;
	private boolean flgAMDFL = false;
	private JTextField txtDSBEDT,txtENBEDT;	
	private JCheckBox chkENBFL ;
	private Hashtable hstITMDT = new Hashtable();
	private Hashtable hstITMVL = new Hashtable();
	private Vector<String> vtrFRWBY = new Vector<String>();
	private mm_teindTBLINVFR objTBLVRF;
	final String fnlAUTFL ="4";
	final String strINDTR_fn ="10";

	final int TBL_CHKFL = 0;
	final int TBL_VENCD = 1;
	final int TBL_VENDS = 2;
	final int TBL_STSFL = 3;
	
	final int TBL_MATCD = 1;
	final int TBL_MATDS = 2;
	final int TBL_INDQT = 3;
	final int TBL_AUTQT = 4;
	final int TBL_REQDT = 5;
	final int TBL_EXPDT = 6;
	final int TBL_INSFL = 7;
	final int TBL_TCFFL = 8;
	final int TBL_INDVL = 9;
	final int TBL_PORBY = 10;
	final int TBL_PPORT = 11;
	final String strTRNTP ="IN";
	
	mm_teind()
	{
		super(2);
		setMatrix(20,8);		
		java.awt.Color colBLUE = new java.awt.Color(63,91,167);
		pnlINDTL = new JPanel(null);
		pnlGNDTL = new JPanel(null);
		pnlVNDTL = new JPanel(null);
		pnlHISTR = new JPanel(null);
		
		jtpINDTL=new JTabbedPane();
		jtpINDTL.add(pnlGNDTL,"General Details");
		jtpINDTL.add(pnlVNDTL,"Probable Vendors");
		jtpINDTL.add(pnlHISTR,"History");
		
		add(new JLabel("Indent Number"),1,1,1,1,this,'L');
		add(txtINDNO = new TxtLimit(8),1,2,1,0.75,this,'L');
		add(txtAMDNO = new TxtLimit(2),1,2,1,0.25,this,'R');
		add(new JLabel("Indent Type"),1,3,1,1,this,'L');
		add(cmbURGTG=new JComboBox(),1,4,1,1,this,'L');
		add(new JLabel("Department"),1,5,1,1,this,'L');
		add(txtDPTCD=new TxtLimit(3),1,6,1,0.5,this,'L');
		add(txtDPTDS=new TxtLimit(40),1,7,1,1.5,this,'R');
		add(btnPRNT = new JButton("Print"),1,8,1,1,this,'L');
	
		add(new JLabel("Prepared by "),1,1,1,1,pnlHISTR,'L');
		add(txtPREBY = new TxtLimit(3),1,2,1,1,pnlHISTR,'L');
		add(new JLabel("Prep. Date/Tm."),1,3,1,1,pnlHISTR,'L');
		add(txtPREDT = new TxtDate(),1,4,1,1,pnlHISTR,'L');
		add(txtPRETM = new TxtTime(),1,5,1,0.6,pnlHISTR,'L');
		add(new JLabel("Indent Date"),1,6,1,1,pnlHISTR,'L');
		add(txtINDDT = new TxtDate(),1,7,1,1,pnlHISTR,'L');
	
		add(new JLabel("Forwarded by"),2,1,1,1,pnlHISTR,'L');
		add(txtFRWBY = new TxtLimit(3),2,2,1,1,pnlHISTR,'L');
		add(new JLabel("Forw.Date/Tm."),2,3,1,1,pnlHISTR,'L');
		add(txtFRWDT = new TxtDate(),2,4,1,1,pnlHISTR,'L');
		add(txtFRWTM = new TxtTime(),2,5,1,0.6,pnlHISTR,'L');
		
		add(new JLabel("Amendment Dt."),2,6,1,1,pnlHISTR,'L');
		add(txtAMDDT = new TxtDate(),2,7,1,1,pnlHISTR,'L');
		
		add(new JLabel("Indent Status"),3,6,1,1,pnlHISTR,'L');
		add(txtINDST = new TxtLimit(30),3,7,1,2,pnlHISTR,'L');
	
		add(new JLabel("Authorised by"),3,1,1,1,pnlHISTR,'L');
		add(txtAUTBY = new TxtDate(),3,2,1,1,pnlHISTR,'L');
		add(new JLabel("Auth.Date/Tm."),3,3,1,1,pnlHISTR,'L');
		add(txtAUTDT = new TxtDate(),3,4,1,1,pnlHISTR,'L');
		add(txtAUTTM = new TxtTime(),3,5,1,0.6,pnlHISTR,'L');

		txtDPTCD.setInputVerifier(new inpVRFY());
		String[] L_strTB1HD = {" ","Item Code","Description","Ind.Qty","Aut.Qty","Req. Date","Exp.Date","Ins tag","TC tag","VL","PORBY","PPORT"};
        String[] L_strTB2HD = {" ","Vendor","Description","Status"};
		int[] L_intCOLSZ = {20,75,355,62,62,65,65,20,20,5,5,5};
		int[] L_intCOLSZ1 = {30,75,550,100};
		tblINDTL = crtTBLPNL1(pnlINDTL,L_strTB1HD,100,1,1,5,7.9,L_intCOLSZ,new int[]{0,TBL_INSFL,TBL_TCFFL});
		tblVNDTL = crtTBLPNL1(pnlVNDTL,L_strTB2HD,100,1,1,5,7.9,L_intCOLSZ1,new int[]{0});
		tblINDTL.addMouseListener(this);
		add(pnlINDTL,2,1,6,8,this,'L');
		add(new JLabel("Indent Remark"),9,1,1,1,this,'L');
		add(txtINDRM = new TxtLimit(200),9,2,1,7,this,'L');
		
		add(new JLabel("Comments"),10,1,1,1,this,'L');
		add(txtOTHRM = new TxtLimit(200),10,2,1,7,this,'L');
		add(new JLabel("Forward To"),11,1,1,1,this,'L');
		add(cmbFRWTO=new JComboBox(),11,2,1,1,this,'L');
		add(btnFRWD = new JButton("Forward"),11,3,1,1.2,this,'L');
		add(cmbAUTST=new JComboBox(),11,5,1,1.8,this,'R');
		add(new JLabel("Approx value"),11,7,1,1,this,'L');
		add(txtINDVL = new TxtNumLimit(10.3),11,8,1,1,this,'L');
		txtMATCD = new TxtLimit(10);
		txtINDQT = new TxtNumLimit(10.3);
		txtREQDT = new TxtDate();
		txtEXPDT = new TxtDate();
		
		txtMATCD.addFocusListener(this);
		txtINDQT.addFocusListener(this);
		txtREQDT.addFocusListener(this);
		txtMATCD.addKeyListener(this);
		txtINDQT.addKeyListener(this);
		txtREQDT.addKeyListener(this);
	
		tblINDTL.setCellEditor(TBL_MATCD,txtMATCD);
		tblINDTL.setCellEditor(TBL_INDQT,txtINDQT);
		tblINDTL.setCellEditor(TBL_REQDT,txtREQDT);
		tblINDTL.setCellEditor(TBL_EXPDT,txtEXPDT);
		
		add(jtpINDTL,12,1,6,8,this,'L');
		tblVNDTL.setEnabled(false);
		
		add(new JLabel("Details for Material Code : "),1,1,1,2,pnlGNDTL,'L');
		add(lblMATCD = new JLabel(" "),1,3,1,1,pnlGNDTL,'L');
		add(new JLabel("UOM Code"),2,1,1,1,pnlGNDTL,'L');
		add(txtUOMCD = new TxtLimit(8),2,2,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Qty.On Hand"),2,3,1,1,pnlGNDTL,'L');
		add(txtQTHND = new TxtNumLimit(10.3),2,4,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Stock Controlled"),2,5,1,1.5,pnlGNDTL,'L');
		add(chkSTKIT = new JCheckBox(),2,6,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Prv. P.O. Rate"),2,7,1,0.9,pnlGNDTL,'L');
		add(txtPPORT = new TxtNumLimit(10.3),2,8,1,0.9,pnlGNDTL,'L');
		
		add(new JLabel("Std. Packing"),3,1,1,1,pnlGNDTL,'L');
		add(txtPKGTP = new TxtLimit(10),3,2,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Qty on Indent"),3,3,1,1,pnlGNDTL,'L');
		add(txtQTIND = new TxtNumLimit(10.3),3,4,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Reorder Level"),3,5,1,1,pnlGNDTL,'L');
		add(txtRORLV = new TxtNumLimit(10.3),3,6,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Approx. Value"),3,7,1,1,pnlGNDTL,'L');
		add(txtITVAL = new TxtNumLimit(10.3),3,8,1,0.9,pnlGNDTL,'L');
		
		add(new JLabel("Min. Pkg. Qty"),4,1,1,1,pnlGNDTL,'L');
		add(txtPKGQT=new TxtNumLimit(10.3),4,2,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Qty. on Order"),4,3,1,1,pnlGNDTL,'L');
		add(txtQTORD=new TxtNumLimit(10.3),4,4,1,1,pnlGNDTL,'L');
		
		add(new JLabel("Reorder Qty"),4,5,1,1,pnlGNDTL,'L');
		add(txtRORQT=new TxtNumLimit(10.3),4,6,1,1,pnlGNDTL,'L');
		String L_strDATA ="";
		ResultSet M_rstRSSET1;
		try
		{
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='SYS' and cmt_cgstp ='MMXXURG'";
			L_strDATA ="";
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			cmbURGTG.addItem("Select");
			if(M_rstRSSET1 != null)	
			{
				while(M_rstRSSET1.next()){
				L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET1.getString("CMT_CODCD"),""),4) +M_rstRSSET1.getString("CMT_CODDS");
			  	cmbURGTG.addItem(L_strDATA);
				}
			}
			M_strSQLQRY = "SELECT CMT_CGSTP,CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='A"+cl_dat.M_strCMPCD_pbst+"' and cmt_cgstp IN('QCXXAU1','MMXXIND')";
			L_strDATA ="";
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET1 != null)	
			{
				cmbFRWTO.addItem("Forw.To");
				while(M_rstRSSET1.next()){
					L_strDATA = nvlSTRVL(M_rstRSSET1.getString("CMT_CODCD"),"");
				if(nvlSTRVL(M_rstRSSET1.getString("CMT_CGSTP"),"").equals("QCXXAU1"))
				{
			  		cmbFRWTO.addItem(L_strDATA);
				}
				else
				{
					vtrFRWBY.addElement(L_strDATA);
				}
				}
				
			}
			M_strSQLQRY = "SELECT CMT_CODCD,CMT_CODDS from CO_CDTRN where cmt_CGMTP ='STS' and cmt_cgstp ='MMXXIND' AND CMT_CHP01 ='A"+cl_dat.M_strCMPCD_pbst+"'";
			L_strDATA ="";
			M_rstRSSET1 = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET1 != null)	
			{
				cmbAUTST.addItem("Auth. Status");
				while(M_rstRSSET1.next()){
				L_strDATA = padSTRING('R',nvlSTRVL(M_rstRSSET1.getString("CMT_CODCD"),""),2) +M_rstRSSET1.getString("CMT_CODDS");
			  	cmbAUTST.addItem(L_strDATA);
				}
			}
			pstmINSREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"INSERT INTO MM_INMST(IN_CMPCD,IN_MMSBS,IN_STRTP,IN_INDNO,IN_AMDNO,IN_MATCD,IN_INDDT,"+
							"IN_INDQT,IN_AUTQT,IN_URGTG,IN_DPTCD,IN_PREBY,IN_PREDT,"+
						"IN_REQDT,IN_EXPDT,IN_PORBY,IN_INSFL,IN_TCFFL,IN_INDVL,IN_STSFL,IN_TRNFL,IN_LUSBY,IN_LUPDT,IN_FRWBY,IN_FRWTO,IN_FRWDT,IN_INDTP)VALUES("+
						"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"	
						);
			pstmUPDREC = cl_dat.M_conSPDBA_pbst.prepareStatement(
						"UPDATE MM_INMST SET IN_AMDNO = ?,IN_URGTG = ?,IN_DPTCD = ?,IN_INDQT = ?,IN_AUTQT = ?,"+
					    "IN_REQDT = ?,IN_EXPDT = ?,IN_PORBY = ?,IN_INSFL = ?,IN_TCFFL =?,IN_STSFL = ?,IN_TRNFL =?,IN_LUSBY=?,IN_LUPDT = ?,IN_INDVL=? "+
						" WHERE IN_CMPCD = ? AND IN_MMSBS = ? AND IN_STRTP = ? AND IN_INDNO = ? AND IN_AMDNO = ? AND IN_MATCD = ? AND isnull(IN_STSFL,'') <>'X'"); 
		
			pstmINSRMK =cl_dat.M_conSPDBA_pbst.prepareStatement(
						"INSERT INTO MM_RMMST(RM_CMPCD,RM_MMSBS,RM_STRTP,RM_DOCTP,RM_TRNTP,RM_REMTP,RM_DOCNO,RM_REMDS,RM_STSFL,RM_TRNFL,RM_LUSBY,RM_LUPDT)VALUES("+
						"?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmUPDRMK =cl_dat.M_conSPDBA_pbst.prepareStatement(
					"UPDATE MM_RMMST set RM_REMDS = ?,RM_STSFL = ?,RM_TRNFL= ?,RM_LUSBY= ?,RM_LUPDT = ? where RM_CMPCD = ? AND RM_MMSBS = ? and RM_STRTP = ? and RM_DOCTP = ? and RM_TRNTP = ? and RM_REMTP = ? and RM_DOCNO = ?");

		}
		catch(Exception L_E)
		{
			setMSG(L_E,"const");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		cl_dat.M_btnUNDO_pbst.setVerifyInputWhenFocusTarget(false);
		cl_dat.M_btnEXIT_pbst.setVerifyInputWhenFocusTarget(false);
		objTBLVRF = new mm_teindTBLINVFR();
		tblINDTL.setInputVerifier(objTBLVRF);		
		setENBL(false);
	}
	public void mousePressed(MouseEvent L_ME)
	{
		super.mousePressed(L_ME);
		if(L_ME.getSource().equals(tblINDTL))
		{
			getITMDTL(tblINDTL.getSelectedRow());
		}
	}
	public void keyPressed(KeyEvent L_KE)
	{
		super.keyPressed(L_KE);
		int[] L_inaCOLSZ = new int[]{100,420};
		if(L_KE.getKeyCode() == L_KE.VK_F2)
		{
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCDF2";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') not in('0','X','9')";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY += " Order by ct_matds ";
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
		}
		if(L_KE.getKeyCode() == L_KE.VK_F1)
		{
			if(M_objSOURC == txtMATCD)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtMATCD";
				String L_ARRHDR[] = {"Item Code","Description"};
				M_strSQLQRY = "Select CT_MATCD,CT_MATDS from CO_CTMST ";
				M_strSQLQRY += " where CT_CODTP ='CD' and isnull(CT_STSFL,'') not in('0','X','9')";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY += " and CT_MATCD like '" + txtMATCD.getText().trim() + "%'";
				M_strSQLQRY += " Order by ct_matcd ";
				cl_hlp(M_strSQLQRY,1,2,L_ARRHDR,2,"CT",L_inaCOLSZ);
			}
			else if(M_objSOURC == txtDPTCD)
				{	
				cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtDPTCD";
					strSTRTP =M_strSBSCD.substring(2,4);
					String L_ARRHDR[] = {"Department Code","Description"};
					M_strSQLQRY = "select CMT_CODCD,CMT_CODDS from CO_CDTRN WHERE CMT_CGMTP ='SYS' AND CMT_CGSTP ='COXXDPT' ";
					if(txtDPTCD.getText().trim().length() >0)
						M_strSQLQRY += " AND CMT_CODCD like '"+txtDPTCD.getText().trim() +"%'";
					M_strSQLQRY += " order by CMT_CODDS";
					if(M_strSQLQRY != null)
						cl_hlp(M_strSQLQRY,2,1,L_ARRHDR,2,"CT");
				}
			else if(M_objSOURC == txtINDNO)
			{
				cl_dat.M_flgHELPFL_pbst = true;
				M_strHLPFLD = "txtINDNO";
				String L_ARRHDR[] = {"Indent number","Amd no.","Status","Dept Code"};
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					M_strSQLQRY = "Select distinct IN_INDNO,IN_AMDNO,IN_STSFL,IN_DPTCD from MM_INMST ";
					M_strSQLQRY += " where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'') in('2','3')";
					M_strSQLQRY += " AND isnull(IN_FRWTO,'') ='"+cl_dat.M_strUSRCD_pbst +"'";
					
				}
				else
				{	
					M_strSQLQRY = "Select distinct IN_INDNO,IN_AMDNO,IN_STSFL,IN_DPTCD from MM_INMST ";
					M_strSQLQRY += " where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND isnull(IN_STSFL,'')<>'X'";
				}
				if(txtINDNO.getText().trim().length() >0)
						M_strSQLQRY += " and IN_INDNO like '" + txtINDNO.getText().trim() + "%'";
				
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				{
					M_strSQLQRY +=" AND isnull(IN_STSFL,'') <>'4' ";
				}
				M_strSQLQRY += " and IN_STRTP ='"+M_strSBSCD.substring(2,4) +"' AND IN_INDTP ='01' ";
				M_strSQLQRY +=" Order by IN_INDNO DESC ";
				cl_hlp(M_strSQLQRY,1,1,L_ARRHDR,4,"CT");
			}

		}
		else if(L_KE.getKeyCode() == L_KE.VK_F3)
		{
			if(M_objSOURC == txtMATCD)
			{
				M_strHLPFLD = "txtMATCDF3";
				M_strSQLQRY = "SELECT ST_MATCD,ST_MATDS,ST_STKQT,ST_MINLV from MM_STMST WHERE ";
				M_strSQLQRY +=" ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_STRTP ='"+M_strSBSCD.substring(2,4) +"'";
				if(txtMATCD.getText().trim().length() >0)
					M_strSQLQRY +=" AND ST_MATCD like '"+txtMATCD.getText().trim() +"%'";
				M_strSQLQRY +=" AND isnull(ST_STKQT,0) <isnull(ST_MINLV,0)";
				M_strSQLQRY += " Order by st_matcd ";
				String L_ARRHDR[] = {"Item Code","Description","Stock","Min. Level"};
				cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,4,"CT");
			}
		}
		else if(L_KE.getKeyCode() == L_KE.VK_F9)
		{
			try
			{
				setCursor(cl_dat.M_curWTSTS_pbst);
				if(M_objSOURC == txtMATCD)
				{
					String L_strLVLNO ="";
					String L_strMATCD = txtMATCD.getText().trim();//tblINDTL.getValueAt(tblINDTL.getSelectedRow(),TBL_MATCD).toString();
					if(L_strMATCD.length() !=10)
					{
						setMSG("F9 help is allowed for full length of material code..",'E');
						setCursor(cl_dat.M_curDFSTS_pbst);
						return;
					}
					M_strSQLQRY = "SELECT CT_LVLRF from CO_CTMST where ct_codtp ='CD' and ct_matcd ='"+L_strMATCD +"'";
					M_strSQLQRY += " AND isnull(ct_stsfl,'') not in ('0','X')";
					M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
					if(M_rstRSSET !=null)
						if(M_rstRSSET.next())
							L_strLVLNO = nvlSTRVL(M_rstRSSET.getString("CT_LVLRF"),"");
					cl_dat.M_flgHELPFL_pbst = true;
					M_strHLPFLD = "txtMATCDF9";
					String L_ARRHDR[] = {"Description"};
					M_strSQLQRY = "Select CTT_MATDS,CTT_MATCD,CTT_LVLNO,ctt_linno from CO_CTTRN ";
				//	M_strSQLQRY = "Select CTT_MATCD,CTT_MATDS,CTT_LVLNO,CTT_PRTFL from CO_CTTRN ";
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
					cl_hlp(M_strSQLQRY,2,2,L_ARRHDR,1,"CT");
				}
			}
			catch(Exception L_E)
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG(L_E,"F9");
			}
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		if(L_KE.getKeyCode() == L_KE.VK_ENTER)
		{
			if(M_objSOURC == txtMATCD)
			{
			}
			else if(M_objSOURC == cmbURGTG)
			{
				if(cmbURGTG.getSelectedIndex() ==0)
				{
					setMSG("Please select the Urgency tag..",'E');
					
				}
				else
				{
					if(cmbURGTG.getSelectedItem().toString().substring(0,1).equals("N"))
					{
						tblINDTL.cmpEDITR[TBL_REQDT].setEnabled(false);
					}
					else
					{
						tblINDTL.cmpEDITR[TBL_REQDT].setEnabled(true);
					}
					txtDPTCD.requestFocus();
					setMSG("Select the Department code..",'N');
				}
			}
			else if(M_objSOURC == txtDPTCD)
			{
				if(txtDPTCD.getText().trim().length() ==0)
				{
					setMSG("Please Select the Department code..",'E');
				}
				else
				{
					tblINDTL.editCellAt(0,1);
					setMSG("press F1 to select the item code ..",'N');
					L_KE.consume();
				}
			}
			else if(M_objSOURC == txtINDNO)
			{
				strSTRTP = M_strSBSCD.substring(2,4);
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					txtAMDNO.setText("00");
				strPRAMD = txtAMDNO.getText().trim();
			}
		}
	}
	public void actionPerformed(ActionEvent L_AE)
	{
		
		super.actionPerformed(L_AE);
		if(M_objSOURC == btnPRNT)
		{
			if(txtINDNO.getText().trim().length() ==8)
				strINDNO = txtINDNO.getText().trim();
			if(strINDNO.length() ==0)
			{
				setMSG("Indent number is not specified..",'E');
				return;
			}
			mm_rpind objINDRP  = new mm_rpind(M_strSBSCD,M_strSBSCD.substring(2,4));
			objINDRP.getALLREC(strINDNO,strINDNO,'I',"PI");
			JComboBox L_cmbLOCAL = objINDRP.getPRNLS();
			objINDRP.doPRINT(cl_dat.M_strREPSTR_pbst+"mm_rpind.doc",L_cmbLOCAL.getSelectedIndex());
				
		}
		if(M_objSOURC == cl_dat.M_cmbOPTN_pbst)
		{
			clrCOMP();
			txtINDNO.setText("");
			txtAMDNO.setText("");
			hstITMVL.clear();
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPSEL_pbst))
			{
				setENBL(false);
				setMSG("Please select an option ..",'N');
			}
			if(M_strSBSCD !=null)
			{
				M_strSBSCD = M_strSBSCD;
				strSTRTP = M_strSBSCD.substring(2,4);
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				setENBL(true);
				txtINDDT.setText(cl_dat.M_strLOGDT_pbst);
				txtPREBY.setText(cl_dat.M_strUSRCD_pbst);
				txtPREDT.setText(cl_dat.M_txtCLKDT_pbst.getText().trim());
				txtPRETM.setText(cl_dat.M_txtCLKTM_pbst.getText().trim());
				txtAMDNO.setText("00");
				cmbURGTG.requestFocus();
			}
			else
			{
				setMSG("Press F1 to select the indent number..",'N');
				txtINDNO.requestFocus();
			}
		}
		else if(M_objSOURC == cl_dat.M_btnUNDO_pbst)
		{
			clrCOMP();
			hstITMVL.clear();
		}
		else if(M_objSOURC == btnFRWD)
		{
			try
			{
				flgFRWIN  = true;
				cl_dat.M_flgLCUPD_pbst = true;
				String L_strFRWTO ="" ;
				if(cmbFRWTO.getSelectedIndex() ==0)
				{
					setMSG("Please select the Authorising person to whom indent is to be forwarded..",'E'); 
					setCursor(cl_dat.M_curDFSTS_pbst);
					return;
				}
				else
				{
					L_strFRWTO = cmbFRWTO.getSelectedItem().toString().trim();
				   if(strINDST.equals("4"))
				   {
						setMSG("Already authorised",'E'); 
						setCursor(cl_dat.M_curDFSTS_pbst);
						return;
					}
				   else if(strINDST.equals("3"))
				   {
						setMSG("Can not forward..Indent is in process of Authorisation..",'E'); 
						setCursor(cl_dat.M_curDFSTS_pbst);
						return;
					}
				   if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
				   {
					   if(txtDPTCD.getText().equals(""))
					   {
						   setMSG("Can not forward..Enter Department Code..",'E'); 
							setCursor(cl_dat.M_curDFSTS_pbst);
							return;
					   }
				   }
				}
				cmbFRWTO.setEnabled(false);
				btnFRWD.setEnabled(false);
				String L_strTEMP = txtINDNO.getText().trim();
				M_strSQLQRY = "UPDATE MM_INMST SET "+
							  " IN_FRWBY ='"+cl_dat.M_strUSRCD_pbst+"',"+
							  "IN_FRWTO ='"+L_strFRWTO+"',"+
							  "IN_FRWDT ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText().trim()+ " "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
				M_strSQLQRY += getUSGDTL("IN",'U',"2"); // status flag 2 is forwarded
				M_strSQLQRY += " WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+M_strSBSCD+"' AND IN_STRTP ='"+strSTRTP +"'";
				M_strSQLQRY += " AND IN_INDNO = '"+txtINDNO.getText().trim() +"'";
				M_strSQLQRY += " AND isnull(IN_STSFL,'') <>'X'";
			//	M_strSQLQRY += " AND IN_AMDNO = '"+strPRAMD +"'";
			//	L_strORGST = strINDST;
				strINDST = "2";
				cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
				if(cl_dat.M_flgLCUPD_pbst)
				{
					exeSAVE();
				}
			//	strINDST =L_strORGST;
				if(cl_dat.exeDBCMT("Forward"))
				{
					setMSG("Indent forwarded to "+L_strFRWTO,'N');
					M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+L_strFRWTO+"'";
					M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
					if(M_rstRSSET != null)
					if(M_rstRSSET.next())
					{
						String L_strEML = M_rstRSSET.getString("US_EMLRF");
						cl_eml ocl_eml = new cl_eml();
						ocl_eml.sendfile(L_strEML,null,"Intimation of Pending Indent for Authorisation ","Indent Number "+L_strTEMP+" is pending for Approval.");
					}
					if(M_rstRSSET != null)
					M_rstRSSET.close();
			
				}
				else
				{
					setMSG("Error in Forwarding the indent.. ",'E');
				}
				// enable
				cmbFRWTO.setEnabled(true);
				btnFRWD.setEnabled(true);
			}
			catch(Exception L_E)
			{
				
				setMSG(L_E,"forward ");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			flgFRWIN  = false;
		}
		else if(M_objSOURC == txtINDNO)
		{
			getDATA();
			
				
			/*	if(strINDST.equals(fnlAUTFL))
				{
					int L_SELOPT = JOptionPane.showConfirmDialog(this,"Indent is Authorised,Do you want to generate a Amendment..?","Amendment confirmation ",JOptionPane.YES_NO_OPTION); 
					strTEMP ="";
					if(L_SELOPT ==0)
					{
						flgAMDFL = true;
						txtAMDNO.setEnabled(false);
						strPRAMD = txtAMDNO.getText().trim();
						int L_intTEMP = Integer.parseInt(txtAMDNO.getText().trim()) + 1;
						for(int i=0;i<2-String.valueOf(L_intTEMP).toString().length();i++)
							strTEMP +=0;
						txtAMDNO.setText(strTEMP+String.valueOf(L_intTEMP));
						strINDST ="1"; // status flag for ammended indent
						setMSG("",'N');
					}
					else
					{
						flgAMDFL = false;
						clrCOMP();
						setMSG("Indent is authorised, modification not allowed..",'E');
					}
					
				}*/
			
			jtpINDTL.setSelectedIndex(2);
		}
		else if(M_objSOURC == cmbURGTG)
		{
			if(!cmbURGTG.getSelectedItem().toString().substring(0,1).equals("N"))
			{
				tblINDTL.clrTABLE();
				tblINDTL.cmpEDITR[TBL_REQDT].setEnabled(true);
			}
			else
			{
				tblINDTL.cmpEDITR[TBL_REQDT].setEnabled(false);
			}
		}
	}
	@SuppressWarnings("unchecked") private void getDATA()
	{
		try
		{
			double dblINDVL =0.0;
			java.sql.Date L_datTEMP;
			java.sql.Timestamp L_tmsTEMP;
			boolean L_flgFOUND = false;
			int L_intROWCT =0;
			int L_intINDQT=0;
			int L_intDAYS= 0;
			String L_strTCFFL,L_strINSFL,L_strMATCD ="",L_strINDQT ="0.00"	;
			String L_strEXPDT ="";
			String L_strPORBY ="";
			String L_strURGTG,L_strDPTCD;
			java.sql.Date datTEMP;
			strINDDT ="";
			M_strSQLQRY = " Select IN_MMSBS,IN_STRTP,IN_INDNO,IN_AMDNO,IN_MATCD,IN_INDDT,IN_AMDDT,IN_INDQT,IN_URGTG,IN_INDVL,";
			M_strSQLQRY +=" IN_DPTCD,IN_PREBY,IN_PREDT,IN_FRWBY,IN_FRWDT,IN_FRWTO,IN_AUTBY,IN_AUTDT,IN_AUTQT,IN_REQDT,IN_EXPDT,IN_PORBY,(DATEDIFF(day,IN_INDDT,getdate()))L_DAYS,IN_ENQNO,IN_ENQSR,IN_CCTCD,IN_ORDQT,IN_REQQT,IN_FCCQT,IN_INSFL,IN_TCFFL,IN_STSFL,CT_MATDS,CT_UOMCD from MM_INMST,CO_CTMST ";
			M_strSQLQRY +=" where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MATCD = CT_MATCD AND isnull(CT_STSFL,'') not in ('0','X') and IN_MMSBS ='"+M_strSBSCD+"'" +" AND IN_STRTP ='"+strSTRTP +"'";
			M_strSQLQRY +=" AND IN_INDNO ='"+txtINDNO.getText().trim()+"'";
			M_strSQLQRY += " and isnull(IN_STSFL,'') <>'X' AND IN_INDTP ='01'";
			
			clrCOMP();
			if(tblINDTL.isEditing())
				tblINDTL.getCellEditor().stopCellEditing();
			tblINDTL.setRowSelectionInterval(0,0);
			tblINDTL.setColumnSelectionInterval(0,0);
			hstITMDT.clear();
			strINDST ="";
			this.setCursor(cl_dat.M_curWTSTS_pbst);	
			System.out.println("M_strSQLQRY>>>>"+M_strSQLQRY);
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				L_flgFOUND = true;
				if(L_intROWCT == 0)
				{
					strINDST = nvlSTRVL(M_rstRSSET.getString("IN_STSFL"),""); 
					strFRWTO = nvlSTRVL(M_rstRSSET.getString("IN_FRWTO"),""); 
					strFRWBY = nvlSTRVL(M_rstRSSET.getString("IN_FRWBY"),""); 
					txtAMDNO.setText(nvlSTRVL(M_rstRSSET.getString("IN_AMDNO"),""));
					strPRAMD = txtAMDNO.getText().trim();
					if(!strINDST.equals("4"))
						flgAMDFL = false;
					// if authorisation being done.		  
					//if(flgAUTH)
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						if(strINDST.equals("0"))
						{
							setMSG("Indent is not Forwarded for authorisation..",'E');
							setCursor(cl_dat.M_curDFSTS_pbst);
							return;
						}
						else if(strINDST.equals("4"))
						{
							setMSG("Already authorised..",'E');
							int L_SELOPT = JOptionPane.showConfirmDialog(this,"Indent is already authorised,Do you want to Amend..?","Amendment confirmation ",JOptionPane.YES_NO_OPTION); 
							strTEMP ="";
							if(L_SELOPT ==0)
							{
								flgAMDFL = true;
								txtAMDNO.setEnabled(false);
								strPRAMD = txtAMDNO.getText().trim();
								int L_intTEMP = Integer.parseInt(txtAMDNO.getText().trim()) + 1;
								for(int i=0;i<2-String.valueOf(L_intTEMP).toString().length();i++)
									strTEMP +=0;
								txtAMDNO.setText(strTEMP+String.valueOf(L_intTEMP));
								strINDST ="4"; // status flag for ammended indent
								setMSG("",'N');
							}
							else
							{
								flgAMDFL = false;
								clrCOMP();
								setCursor(cl_dat.M_curDFSTS_pbst);
								return;
							}
						}
						else
						{
							if(!strFRWTO.equals(cl_dat.M_strUSRCD_pbst))
							{
								setMSG("Indent is Forwarded to "+strFRWTO+" for authorisation",'E');
								clrCOMP();
								setCursor(cl_dat.M_curDFSTS_pbst);
								return;
							}
							else
							tblINDTL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_CHKFL);
						}
					}
					else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
					{
						if(strINDST.equals("4"))
						{
							setMSG("Indent is Authorised,modification is not allowed..",'E');
							setCursor(cl_dat.M_curDFSTS_pbst);
							return;
						}
					}
					setMSG("",'N');
					txtINDNO.setText(nvlSTRVL(M_rstRSSET.getString("IN_INDNO"),""));
					
					L_strURGTG = nvlSTRVL(M_rstRSSET.getString("IN_URGTG"),"");
					L_strDPTCD = nvlSTRVL(M_rstRSSET.getString("IN_DPTCD"),"");
					L_datTEMP = M_rstRSSET.getDate("IN_INDDT");
					if(L_datTEMP !=null)
					{
						txtINDDT.setText(M_fmtLCDAT.format(L_datTEMP));
						strINDDT = txtINDDT.getText().trim();
					}
					int i;
					for(i=0;i<cmbURGTG.getItemCount();i++)
					{
						if(cmbURGTG.getItemAt(i).toString().substring(0,1).equals(L_strURGTG))
						{
							cmbURGTG.setSelectedIndex(i);
							break;
						}
					}
					txtDPTCD.setText(L_strDPTCD);
					vldDPTCD(L_strDPTCD);
					txtPREBY.setText(nvlSTRVL(M_rstRSSET.getString("IN_PREBY"),""));
					txtFRWBY.setText(strFRWBY);
					cmbFRWTO.setSelectedIndex(0);
					cmbAUTST.setSelectedIndex(0);
					for(i=0;i<cmbFRWTO.getItemCount();i++)
					{
						if(cmbFRWTO.getItemAt(i).toString().equals(nvlSTRVL(M_rstRSSET.getString("IN_FRWTO"),"")))
						{
							cmbFRWTO.setSelectedIndex(i);
							break;
						}
					}
					for(i=0;i<cmbAUTST.getItemCount();i++)
					{
						if(cmbAUTST.getItemAt(i).toString().substring(0,1).equals(strINDST))
						{
							cmbAUTST.setSelectedIndex(i);
							break;
						}
					}
					//txtFRWTO.setText(nvlSTRVL(M_rstRSSET.getString("IN_FRWTO"),""));
					txtAUTBY.setText(nvlSTRVL(M_rstRSSET.getString("IN_AUTBY"),""));
					L_tmsTEMP = M_rstRSSET.getTimestamp("IN_PREDT");
					if(L_tmsTEMP !=null)
					{
						txtPREDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
						txtPRETM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
					}	
					L_tmsTEMP = M_rstRSSET.getTimestamp("IN_AUTDT");
					if(L_tmsTEMP !=null)
					{
						txtAUTDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
						txtAUTTM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
					}
					L_tmsTEMP = M_rstRSSET.getTimestamp("IN_FRWDT");
					if(L_tmsTEMP !=null)
					{
						txtFRWDT.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(0,10));
						txtFRWTM.setText(M_fmtLCDTM.format(L_tmsTEMP).substring(11));
					}
					L_datTEMP = M_rstRSSET.getDate("IN_AMDDT");
					if(L_datTEMP !=null)
					{
						txtAMDDT.setText(M_fmtLCDAT.format(L_datTEMP));
					}
					
					txtINDST.setText(cl_dat.getPRMCOD("CMT_CODDS","STS","MMXXIND",strINDST));
					if(strINDST.equals("2")) // Forwarded
					{
						btnFRWD.setText("Re-Forward");
					}
					else
						btnFRWD.setText("Forward");
				}
			//	tblINDTL.setValueAt(new Boolean(true),L_intROWCT,TBL_CHKFL);
				L_strMATCD = nvlSTRVL(M_rstRSSET.getString("IN_MATCD"),"");
				L_strINDQT = M_rstRSSET.getString("IN_INDQT");
				hstITMDT.put(L_strMATCD,L_strINDQT);
				tblINDTL.setValueAt(L_strMATCD,L_intROWCT,TBL_MATCD);
				tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_MATDS"),""),L_intROWCT,TBL_MATDS);
				tblINDTL.setValueAt(String.valueOf(L_strINDQT),L_intROWCT,TBL_INDQT);
				tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("IN_INDVL"),"0.00"),L_intROWCT,TBL_INDVL);
				L_datTEMP = M_rstRSSET.getDate("IN_REQDT");
					if(L_datTEMP !=null)
					{
						tblINDTL.setValueAt(M_fmtLCDAT.format(L_datTEMP),L_intROWCT,TBL_REQDT);
					}
		
				L_datTEMP = M_rstRSSET.getDate("IN_EXPDT");
				if(L_datTEMP !=null)
				{
					L_strEXPDT = M_fmtLCDAT.format(L_datTEMP);
					tblINDTL.setValueAt(L_strEXPDT,L_intROWCT,TBL_EXPDT);
				}
				L_datTEMP = M_rstRSSET.getDate("IN_PORBY");
				if(L_datTEMP !=null)
				{
					L_strPORBY = M_fmtLCDAT.format(L_datTEMP);
					tblINDTL.setValueAt(L_strPORBY,L_intROWCT,TBL_PORBY);
				}
				L_intDAYS = M_rstRSSET.getInt("L_DAYS");
			//	dblINDVL +=M_rstRSSET.getDouble("IN_INDVL");
				//if(flgAUTH)
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
				{
					if(!strINDST.equals("4")) 
					{
						tblINDTL.setValueAt(tblINDTL.getValueAt(L_intROWCT,TBL_INDQT),L_intROWCT,TBL_AUTQT);
					}
					else
						tblINDTL.setValueAt(M_rstRSSET.getString("IN_AUTQT"),L_intROWCT,TBL_AUTQT);
					
					tblINDTL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_CHKFL);
			
					M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strEXPDT));
					M_calLOCAL.add(java.util.Calendar.DATE,L_intDAYS);
					L_strEXPDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
					tblINDTL.setValueAt(L_strEXPDT,L_intROWCT,TBL_EXPDT);
					
					M_calLOCAL.setTime(M_fmtLCDAT.parse(L_strPORBY));
					M_calLOCAL.add(java.util.Calendar.DATE,L_intDAYS);
					L_strPORBY = M_fmtLCDAT.format(M_calLOCAL.getTime());
					tblINDTL.setValueAt(L_strPORBY,L_intROWCT,TBL_PORBY);
				}
				else
				{
					if(strINDST.equals("4")) 
						tblINDTL.setValueAt(M_rstRSSET.getString("IN_AUTQT"),L_intROWCT,TBL_AUTQT);
					else
						tblINDTL.setValueAt("0",L_intROWCT,TBL_AUTQT);
				}
					if(nvlSTRVL(M_rstRSSET.getString("IN_TCFFL"),"").equals("Y"))
					{
						tblINDTL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_TCFFL);
					}
					else
						tblINDTL.setValueAt(Boolean.FALSE,L_intROWCT,TBL_TCFFL);
					
					if(nvlSTRVL(M_rstRSSET.getString("IN_INSFL"),"").equals("Y"))
					{
						tblINDTL.setValueAt(Boolean.TRUE,L_intROWCT,TBL_INSFL);
					}
					else
						tblINDTL.setValueAt(Boolean.FALSE,L_intROWCT,TBL_INSFL);
				
				L_intROWCT++;
			
			}
			if(!L_flgFOUND)
			{
				setCursor(cl_dat.M_curDFSTS_pbst);
				setMSG("Data not found ..",'E');
				setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			fltINDVL =0f;
			// Displaying the Indent Value
			for(int i=0;i<tblINDTL.getRowCount();i++)
			{
				if(tblINDTL.getValueAt(i,TBL_MATCD).toString().length() >0)
					fltINDVL += Float.parseFloat(tblINDTL.getValueAt(i,TBL_INDVL).toString());
			}
			txtINDVL.setText(String.valueOf(fltINDVL));	
			M_strSQLQRY = "SELECT * FROM MM_RMMST where RM_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND RM_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND RM_STRTP ='"+strSTRTP +"'";
			M_strSQLQRY += " AND RM_DOCTP ='"+strTRNTP +"'";
			M_strSQLQRY += " AND RM_TRNTP ='"+strTRNTP +"'";
			M_strSQLQRY += " AND RM_REMTP IN('IND','OTH')";
			M_strSQLQRY += " AND RM_DOCNO ='"+txtINDNO.getText().trim()+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			flgINDRM = false;
			flgOTHRM = false;
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("RM_REMTP").equals("IND"))
				{
					if(!M_rstRSSET.getString("RM_STSFL").equals("X"))
					{
						strINDRM = M_rstRSSET.getString("RM_REMDS");
						txtINDRM.setText(strINDRM);
					}
					flgINDRM = true;
				}
				else
				{
					if(!M_rstRSSET.getString("RM_STSFL").equals("X"))
					{
						strOTHRM = M_rstRSSET.getString("RM_REMDS");
						txtOTHRM.setText(strOTHRM);
					}
					flgOTHRM = true;
				}
			}
			if(!L_flgFOUND)
			{
				setMSG("Data not found..",'E');
			}
			this.setCursor(cl_dat.M_curDFSTS_pbst);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
				cmbAUTST.setEnabled(true);
			}
			else
				cmbAUTST.setEnabled(false);
		}
		catch(Exception L_E)
		{
			setMSG(L_E,"getDATA");
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
	}
	void exeHLPOK()
	{
		super.exeHLPOK();
		try
		{
			setMSG("",'N');
			if(M_strHLPFLD.equals("txtINDNO"))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
			
				txtINDNO.setText(L_STRTKN.nextToken());
				txtAMDNO.setText(L_STRTKN.nextToken());
			}
			else if(M_strHLPFLD.equals("txtDPTCD")){
				txtDPTCD.setText(cl_dat.M_strHLPSTR_pbst);
				txtDPTDS.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),1)).trim());
			}
			else if((M_strHLPFLD.equals("txtMATCD"))||(M_strHLPFLD.equals("txtMATCDF2"))||(M_strHLPFLD.equals("txtMATCDF3")))
			{
				StringTokenizer L_STRTKN=new StringTokenizer(cl_dat.M_strHELP_pbst,"|");
				L_STRTKN.nextToken();
				txtMATCD.setText(String.valueOf(cl_dat.M_tblHELP_pbst.getValueAt(cl_dat.M_tblHELP_pbst.getSelectedRow(),0)).trim());
				//if(tblINDTL.isEditing())
				//	tblINDTL.getCellEditor().stopCellEditing();
				strMATCD = tblINDTL.getValueAt(tblINDTL.getSelectedRow(),TBL_MATCD).toString();
				if(objTBLVRF.verify(tblINDTL.getSelectedRow(),TBL_MATCD))
				{
					tblINDTL.setRowSelectionInterval(tblINDTL.getSelectedRow(),tblINDTL.getSelectedRow());		
					tblINDTL.setColumnSelectionInterval(TBL_MATCD,TBL_MATCD);		
					tblINDTL.editCellAt(tblINDTL.getEditingRow(),TBL_MATCD);
					tblINDTL.cmpEDITR[TBL_MATCD].requestFocus();
				}
				//tblINDTL.setValueAt(L_STRTKN.nextToken(),tblINDTL.getSelectedRow(),TBL_MATDS);
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
		
		if(tblINDTL.isEditing())
		{
			tblINDTL.getCellEditor().stopCellEditing();
		}
		txtINDDT.setText("");
		txtAMDDT.setText("");
		txtINDST.setText("");
		txtPREBY.setText("");
		txtPREDT.setText("");
		txtPRETM.setText("");
		txtAUTDT.setText("");
		txtAUTTM.setText("");
		txtAUTBY.setText("");
		txtFRWDT.setText("");
		txtFRWTM.setText("");
		txtFRWBY.setText("");
		txtITVAL.setText("");
	    txtUOMCD.setText("");
		txtPKGTP.setText("");
		txtINDRM.setText("");
		txtOTHRM.setText("");
		txtDPTCD.setText("");
		txtDPTDS.setText("");
		txtINDVL.setText("");
		txtQTHND.setText("");
		txtQTORD.setText("");
		txtQTIND.setText("");
		txtPPORT.setText("");
	    txtRORLV.setText("");
		txtRORQT.setText("");
		txtPKGQT.setText("");
	    chkSTKIT.setSelected(false);
		tblINDTL.clrTABLE();
		tblVNDTL.clrTABLE();
		cl_dat.M_txtDESC_pbst.setText("");
		
	}
	void setENBL(boolean L_STAT)
	{
		super.setENBL(L_STAT);
		btnPRNT.setEnabled(false);
		txtINDNO.setEnabled(true);
		txtAMDNO.setEnabled(true);
		tblINDTL.clearSelection();
		tblVNDTL.setEnabled(false);
		txtUOMCD.setEnabled(false);
		txtPKGTP.setEnabled(false);
		txtINDVL.setEnabled(false);
		txtITVAL.setEnabled(false);
		txtQTHND.setEnabled(false);
		txtQTORD.setEnabled(false);
		txtQTIND.setEnabled(false);
		txtPPORT.setEnabled(false);
		txtRORLV.setEnabled(false);
		txtRORQT.setEnabled(false);
		txtPKGQT.setEnabled(false);
		
		txtPREBY.setEnabled(false);
		txtPREDT.setEnabled(false);
		txtPRETM.setEnabled(false);
		
		txtFRWBY.setEnabled(false);
		txtFRWDT.setEnabled(false);
		txtFRWTM.setEnabled(false);
		txtDPTDS.setEnabled(false);
		txtAUTBY.setEnabled(false);
		txtAUTDT.setEnabled(false);
		txtAUTTM.setEnabled(false);
		txtINDDT.setEnabled(false);
		txtAMDDT.setEnabled(false);
		txtINDST.setEnabled(false);
		chkSTKIT.setEnabled(false);
		tblINDTL.cmpEDITR[TBL_MATDS].setEnabled(false);
        tblINDTL.cmpEDITR[TBL_REQDT].setEnabled(false);
		tblINDTL.cmpEDITR[TBL_EXPDT].setEnabled(false);
		tblINDTL.cmpEDITR[TBL_PORBY].setEnabled(false);
		tblINDTL.cmpEDITR[TBL_INDVL].setEnabled(false);
		tblINDTL.cmpEDITR[TBL_AUTQT].setEnabled(false);
		cmbFRWTO.setEnabled(false);
		btnFRWD.setEnabled(false);
		if(cl_dat.M_cmbOPTN_pbst.getItemCount() >0)
		{
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				txtINDNO.setEnabled(false);
				txtAMDNO.setEnabled(false);
				txtAMDNO.setText("00");
				cmbURGTG.requestFocus();
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPENQ_pbst))
				btnPRNT.setEnabled(true);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				tblINDTL.cmpEDITR[TBL_CHKFL].setEnabled(true);
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
			{
				cmbFRWTO.setEnabled(true);
				btnFRWD.setEnabled(true);
				
				if(vtrFRWBY.contains(cl_dat.M_strUSRCD_pbst))
				{
					cmbFRWTO.setEnabled(true);
					btnFRWD.setEnabled(true);
				}
				else
				{
					cmbFRWTO.setEnabled(false);
					btnFRWD.setEnabled(false);
				}
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
			{
					cmbAUTST.setEnabled(true);
					cmbFRWTO.setEnabled(false);
					btnFRWD.setEnabled(false);
					tblINDTL.cmpEDITR[TBL_AUTQT].setEnabled(true);
			}
			else
				cmbAUTST.setEnabled(false);
		}
		
	}
	void exeSAVE()
	{
		try
		{
			if(txtINDDT.getText().trim().length() ==10)
				strINDDT = txtINDDT.getText().trim();
			flgEML = false;
			String L_strEML ="",L_strSTSDS ="";
			cl_dat.M_btnSAVE_pbst.setEnabled(false);
			if(!vldDATA())
			{
				cl_dat.M_btnSAVE_pbst.setEnabled(true);
				setCursor(cl_dat.M_curDFSTS_pbst);
				return;
			}
			if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
			{
				if(!flgFRWIN)
				if(txtINDNO.getText().trim().length() > 0)
				{
					// Indent no. will be generated afterwards
					setMSG("Invalid Data..",'E');
					setCursor(cl_dat.M_curDFSTS_pbst);
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					return;
				}
			}
				String L_strURGTG,L_strDPTCD;
				int L_intTEMP =0;
				strINDNO = txtINDNO.getText().trim();
				
				String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
				String L_strINDDT = strINDDT.substring(6,10) + "-" + strINDDT.substring(3,5) + "-" + strINDDT.substring(0,2);
				String L_strPREDTM ="",L_strFRWDTM="",L_strREQDT,L_strEXPDT,L_strPORBY;
				L_strPREDTM = cl_dat.M_txtCLKDT_pbst.getText().substring(6,10) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(3,5) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(0,2) + " "+cl_dat.M_txtCLKTM_pbst.getText().trim() + ":00.000000000";
				L_strFRWDTM = cl_dat.M_txtCLKDT_pbst.getText().substring(6,10) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(3,5) + "-" + cl_dat.M_txtCLKDT_pbst.getText().substring(0,2) + " "+cl_dat.M_txtCLKTM_pbst.getText().trim() + ":00.000000000";
				cl_dat.M_flgLCUPD_pbst = true;
				if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPDEL_pbst))
				{
					M_strSQLQRY ="UPDATE MM_INMST SET ";
					M_strSQLQRY +=getUSGDTL("IN",'U',"X");
					M_strSQLQRY +="where IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+M_strSBSCD+"'";
					M_strSQLQRY +=" AND IN_STRTP =" + "'" + strSTRTP + "'";
					M_strSQLQRY +=" AND IN_INDNO =" + "'" + strINDNO +"'";
					M_strSQLQRY +=" AND IN_AMDNO =" + "'" + txtAMDNO.getText().trim() +"'";
					M_strSQLQRY += " AND IN_MATCD IN(";
					for(int i=0;i<tblINDTL.getRowCount();i++)
					{
						if(tblINDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							
							if(L_intTEMP ==0)
								M_strSQLQRY +="'"+tblINDTL.getValueAt(i,TBL_MATCD).toString()+"'";
							else
								M_strSQLQRY +=",'"+tblINDTL.getValueAt(i,TBL_MATCD).toString()+"'";
							L_intTEMP ++;		
						}
					}
					M_strSQLQRY += ") AND isnull(IN_STSFL,'') <> '"+fnlAUTFL +"'";
					// and not authorised
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");	
				}
				else if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
				{
					if(!flgFRWIN)
					{
						if(!genINDNO())
						{
							setCursor(cl_dat.M_curDFSTS_pbst);
							cl_dat.M_btnSAVE_pbst.setEnabled(true);
							return;
						}
						if(txtDPTCD.getText().trim().length() ==0)
						{
							setMSG("Department Code can not be blank..",'E');
							txtDPTCD.requestFocus();
							setCursor(cl_dat.M_curDFSTS_pbst);
							cl_dat.M_btnSAVE_pbst.setEnabled(true);
							return;
						}
					}
					
					for(int i=0;i<tblINDTL.getRowCount();i++)
					{
						if(tblINDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(tblINDTL.getValueAt(i,TBL_REQDT).toString().length() == 10)
								L_strREQDT = tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(0,2);
							else 
								L_strREQDT = null;
							if(tblINDTL.getValueAt(i,TBL_EXPDT).toString().length() == 10)
								L_strEXPDT = tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(0,2);
							else L_strEXPDT = null;
							if(tblINDTL.getValueAt(i,TBL_PORBY).toString().length() == 10)
								L_strPORBY = tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(0,2);
							else L_strPORBY = null;
							pstmINSREC.setString(1,cl_dat.M_strCMPCD_pbst);
							pstmINSREC.setString(2,M_strSBSCD);
							pstmINSREC.setString(3,strSTRTP);
							pstmINSREC.setString(4,txtINDNO.getText().trim());
							pstmINSREC.setString(5,txtAMDNO.getText().trim());
							pstmINSREC.setString(6,tblINDTL.getValueAt(i,TBL_MATCD).toString().toUpperCase());
							pstmINSREC.setDate(7,Date.valueOf(L_strDATE));
							pstmINSREC.setFloat(8,Float.valueOf(tblINDTL.getValueAt(i,TBL_INDQT).toString()).floatValue());
						//	if(tblINDTL.getValueAt(i,TBL_AUTQT).toString().equals(""))
							pstmINSREC.setFloat(9,Float.valueOf(tblINDTL.getValueAt(i,TBL_AUTQT).toString()).floatValue());
							pstmINSREC.setString(10,cmbURGTG.getSelectedItem().toString().substring(0,1));
							pstmINSREC.setString(11,txtDPTCD.getText().trim());
							pstmINSREC.setString(12,cl_dat.M_strUSRCD_pbst);
							if(L_strPREDTM !=null)
								pstmINSREC.setTimestamp(13,Timestamp.valueOf(L_strPREDTM));
							else
								pstmINSREC.setTimestamp(13,null);
							pstmINSREC.setDate(14,Date.valueOf(L_strREQDT));
							pstmINSREC.setDate(15,Date.valueOf(L_strEXPDT));
							pstmINSREC.setDate(16,Date.valueOf(L_strPORBY));
							if(tblINDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
								pstmINSREC.setString(17,"Y");
							else
								pstmINSREC.setString(17,"N");
							if(tblINDTL.getValueAt(i,TBL_TCFFL).toString().equals("true"))
								pstmINSREC.setString(18,"Y");
							else
								pstmINSREC.setString(18,"N");
							strTEMP = tblINDTL.getValueAt(i,TBL_INDQT).toString();
						//	if(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()) == null)
						//		fltINDVL =0;
						//	else
						//		fltINDVL =Float.parseFloat(strTEMP)*Float.parseFloat(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()).toString());
							fltINDVL =Float.parseFloat(strTEMP)*Float.parseFloat(tblINDTL.getValueAt(i,TBL_PPORT).toString());

							pstmINSREC.setFloat(19,fltINDVL);
							pstmINSREC.setString(20,"0");
							pstmINSREC.setString(21,"0");
							pstmINSREC.setString(22,cl_dat.M_strUSRCD_pbst);
							pstmINSREC.setDate(23,Date.valueOf(L_strDATE));
							pstmINSREC.setString(24,"");
							pstmINSREC.setString(25,"");
							pstmINSREC.setTimestamp(26,null);
							pstmINSREC.setString(27,"01");
							pstmINSREC.executeUpdate();
						}
					}
					M_strSQLQRY ="UPDATE CO_CDTRN SET CMT_CCSVL = '"+strSRLNO +"',CMT_CHP01 =''";
					M_strSQLQRY +=" WHERE CMT_CGMTP ='D"+cl_dat.M_strCMPCD_pbst+"' AND CMT_CGSTP ='MM"+M_strSBSCD.substring(0,2)+"IND' ";
					M_strSQLQRY += " AND CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					if(txtINDRM.getText().trim().length() >0)
						insRMMST("IND",txtINDRM.getText().trim());
					if(txtOTHRM.getText().trim().length() >0)
						insRMMST("OTH",txtOTHRM.getText().trim());
				}
				else if((cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))||(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst)))
				{
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
					{
						flgEML = false;
						if(flgAMDFL)
						{
							M_strSQLQRY = "insert into mm_inmam select * from mm_inmst  ";
							M_strSQLQRY += " WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+M_strSBSCD+"' AND IN_STRTP ='"+strSTRTP +"'";
							M_strSQLQRY += " AND IN_INDNO = '"+txtINDNO.getText().trim() +"'";
							M_strSQLQRY += " AND IN_AMDNO = '"+strPRAMD +"'";
							cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
						}
						else
						{
							if(!autINDNT())
							{
								setCursor(cl_dat.M_curDFSTS_pbst);
								cl_dat.M_btnSAVE_pbst.setEnabled(true);
								return;
							}
							else flgEML = true;
							
						}
				
					}
					M_strSQLQRY = "UPDATE MM_INMST SET "+
								  " IN_URGTG ='"+cmbURGTG.getSelectedItem().toString().substring(0,1)+"',"+
								  " IN_DPTCD ='"+txtDPTCD.getText().trim()+"',"+
								  " IN_AMDNO ='"+txtAMDNO.getText().trim()+"',";
								  //"IN_FRWTO ='"+L_strFRWTO+"',"+
								  if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
								  {
									  if(!flgAMDFL)
										M_strSQLQRY += "IN_AUTDT ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText().trim()+ " "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
									  else
										M_strSQLQRY += "IN_AMDDT ='"+ M_fmtDBDAT.format(M_fmtLCDAT.parse(cl_dat.M_txtCLKDT_pbst.getText().trim()))+"',";
								  }
					M_strSQLQRY += getUSGDTL("IN",'U',strINDST); 
					M_strSQLQRY += " WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+M_strSBSCD+"' AND IN_STRTP ='"+strSTRTP +"'";
					M_strSQLQRY += " AND IN_INDNO = '"+txtINDNO.getText().trim() +"'";
					M_strSQLQRY += " AND IN_AMDNO = '"+strPRAMD +"'";
					M_strSQLQRY += " AND isnull(IN_STSFL,'')<>'X'";
					cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
					for(int i=0;i<tblINDTL.getRowCount();i++)
					{
						if(tblINDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
						{
							if(tblINDTL.getValueAt(i,TBL_REQDT).toString().length() == 10)
								L_strREQDT = tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(0,2);
							else 
								L_strREQDT = null;
							if(tblINDTL.getValueAt(i,TBL_EXPDT).toString().length() == 10)
								L_strEXPDT = tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(0,2);
							else 
								L_strEXPDT = null;
							if(tblINDTL.getValueAt(i,TBL_PORBY).toString().length() == 10)
								L_strPORBY = tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(0,2);
							else 
								L_strPORBY = null;
							if(hstITMDT.containsKey(tblINDTL.getValueAt(i,TBL_MATCD).toString()))
							{
								pstmUPDREC.setString(1,txtAMDNO.getText().trim());
								pstmUPDREC.setString(2,cmbURGTG.getSelectedItem().toString().substring(0,1));
								pstmUPDREC.setString(3,txtDPTCD.getText().trim());
								pstmUPDREC.setFloat(4,Float.valueOf(tblINDTL.getValueAt(i,TBL_INDQT).toString()).floatValue());
								//	held for discussion	
								if(cmbAUTST.getSelectedItem().toString().substring(0,1).equals("3"))
									pstmUPDREC.setFloat(5,Float.valueOf("0.00").floatValue());
								else	
									pstmUPDREC.setFloat(5,Float.valueOf(tblINDTL.getValueAt(i,TBL_AUTQT).toString()).floatValue());
								if(L_strREQDT !=null)
									pstmUPDREC.setDate(6,Date.valueOf(L_strREQDT));
								else
									pstmUPDREC.setDate(6,null);
								if(L_strEXPDT !=null)
									pstmUPDREC.setDate(7,Date.valueOf(L_strEXPDT));
								else
									pstmUPDREC.setDate(7,null);
								if(L_strPORBY !=null)
									pstmUPDREC.setDate(8,Date.valueOf(L_strPORBY));
								else
									pstmUPDREC.setDate(8,null);
								if(tblINDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
									pstmUPDREC.setString(9,"Y");
								else
									pstmUPDREC.setString(9,"N");
								if(tblINDTL.getValueAt(i,TBL_TCFFL).toString().equals("true"))
									pstmUPDREC.setString(10,"Y");
								else
									pstmUPDREC.setString(10,"N");
								pstmUPDREC.setString(11,strINDST);
								pstmUPDREC.setString(12,"0");
								pstmUPDREC.setString(13,cl_dat.M_strUSRCD_pbst);
								if(L_strDATE !=null)	
									pstmUPDREC.setDate(14,Date.valueOf(L_strDATE));
								else
									pstmUPDREC.setDate(14,null);
								strTEMP = tblINDTL.getValueAt(i,TBL_INDQT).toString();
								if(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()) == null)
									fltINDVL =0;
								else
									fltINDVL = Float.parseFloat(strTEMP)*Float.parseFloat(tblINDTL.getValueAt(i,TBL_PPORT).toString());
									//fltINDVL =Float.parseFloat(strTEMP)*Float.parseFloat(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()).toString());

								pstmUPDREC.setFloat(15,fltINDVL);
								pstmUPDREC.setString(16,cl_dat.M_strCMPCD_pbst);
								pstmUPDREC.setString(17,M_strSBSCD);
								pstmUPDREC.setString(18,strSTRTP);
								pstmUPDREC.setString(19,txtINDNO.getText().trim());
								pstmUPDREC.setString(20,strPRAMD);
								pstmUPDREC.setString(21,tblINDTL.getValueAt(i,TBL_MATCD).toString());
								pstmUPDREC.executeUpdate();
							}
							else
							{
								if(tblINDTL.getValueAt(i,TBL_REQDT).toString().length() == 10)
								L_strREQDT = tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_REQDT).toString().substring(0,2);
							else 
								L_strREQDT = null;
							if(tblINDTL.getValueAt(i,TBL_EXPDT).toString().length() == 10)
								L_strEXPDT = tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_EXPDT).toString().substring(0,2);
							else L_strEXPDT = null;
							
							if(tblINDTL.getValueAt(i,TBL_PORBY).toString().length() == 10)
								L_strPORBY = tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(6,10) + "-" + tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(3,5) + "-" + tblINDTL.getValueAt(i,TBL_PORBY).toString().substring(0,2);
							else L_strPORBY = null;
							
							pstmINSREC.setString(1,cl_dat.M_strCMPCD_pbst);
							pstmINSREC.setString(2,M_strSBSCD);
							pstmINSREC.setString(3,strSTRTP);
							pstmINSREC.setString(4,txtINDNO.getText().trim());
							pstmINSREC.setString(5,txtAMDNO.getText().trim());
							pstmINSREC.setString(6,tblINDTL.getValueAt(i,TBL_MATCD).toString());
							pstmINSREC.setDate(7,Date.valueOf(L_strINDDT));
							pstmINSREC.setFloat(8,Float.valueOf(tblINDTL.getValueAt(i,TBL_INDQT).toString()).floatValue());
							pstmINSREC.setFloat(9,Float.valueOf(tblINDTL.getValueAt(i,TBL_AUTQT).toString()).floatValue());
							pstmINSREC.setString(10,cmbURGTG.getSelectedItem().toString().substring(0,1));
							pstmINSREC.setString(11,txtDPTCD.getText().trim());
							pstmINSREC.setString(12,cl_dat.M_strUSRCD_pbst);
							if(L_strPREDTM !=null)
								pstmINSREC.setTimestamp(13,Timestamp.valueOf(L_strPREDTM));
							else
								pstmINSREC.setTimestamp(13,null);
							pstmINSREC.setDate(14,Date.valueOf(L_strREQDT));
							pstmINSREC.setDate(15,Date.valueOf(L_strEXPDT));
							pstmINSREC.setDate(16,Date.valueOf(L_strPORBY));
							if(tblINDTL.getValueAt(i,TBL_INSFL).toString().equals("true"))
								pstmINSREC.setString(17,"Y");
							else
								pstmINSREC.setString(17,"N");
							if(tblINDTL.getValueAt(i,TBL_TCFFL).toString().equals("true"))
								pstmINSREC.setString(18,"Y");
							else
								pstmINSREC.setString(18,"N");
							if(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()) == null)
								fltINDVL =0;
							else
								fltINDVL =Float.parseFloat(strTEMP)*Float.parseFloat(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()).toString());
							pstmINSREC.setFloat(19,fltINDVL);
						//	pstmINSREC.setString(18,"0");
							pstmINSREC.setString(20,strINDST);
							pstmINSREC.setString(21,"0");
							pstmINSREC.setString(22,cl_dat.M_strUSRCD_pbst);
							pstmINSREC.setDate(23,Date.valueOf(L_strDATE));
							pstmINSREC.setString(24,strFRWBY);
							pstmINSREC.setString(25,strFRWTO);
							pstmINSREC.setTimestamp(26,Timestamp.valueOf(L_strFRWDTM));
							pstmINSREC.setString(27,"01");
							pstmINSREC.executeUpdate();
							}
						}
					}
					if(txtINDRM.getText().trim().length() >0)
					{
						if(!flgINDRM)
							insRMMST("IND",txtINDRM.getText().trim());
						else
							updRMMST("IND",strINDRM," ");
					}
					else if(flgINDRM)
						updRMMST("IND",strINDRM,"X");
					
					if(txtOTHRM.getText().trim().length() >0)
					{
						if(!flgOTHRM)
							insRMMST("OTH",txtOTHRM.getText().trim());
						else
							updRMMST("OTH",strOTHRM.trim()," ");
					}
					else if(flgOTHRM)
						updRMMST("OTH",strOTHRM.trim(),"X");
					
				}
				if(cl_dat.exeDBCMT("exeSAVE"))
				{
					setMSG("Record saved successfully",'N');
					if(flgEML)
					{
						if(strINDST.equals("3"))
							L_strSTSDS = "Held for Discussion";
						else if(strINDST.equals("4"))
							L_strSTSDS = "Authorised";
						M_strSQLQRY = "Select US_EMLRF from SA_USMST WHERE US_USRCD ='"+txtFRWBY.getText().trim()+"'";
						M_rstRSSET = cl_dat.exeSQLQRY(M_strSQLQRY);
						if(M_rstRSSET != null)
						if(M_rstRSSET.next())
						{
							L_strEML = M_rstRSSET.getString("US_EMLRF");
							cl_eml ocl_eml = new cl_eml();
							String L_strDESC = "Indent No."+txtINDNO.getText().trim()+" is "+L_strSTSDS ;
							ocl_eml.sendfile(L_strEML,null,L_strDESC,L_strDESC);
						}
						if(M_rstRSSET != null)
						M_rstRSSET.close();
						/////
					}
					clrCOMP();
					cl_dat.M_btnSAVE_pbst.setEnabled(true);
					if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst))
					{
						if(!flgFRWIN)
						JOptionPane.showMessageDialog(this,"Please, Note down the Indent No. " + txtINDNO.getText().trim(),"Indent No.",JOptionPane.INFORMATION_MESSAGE);
						btnPRNT.setEnabled(true);
						strINDNO =txtINDNO.getText().trim();
						if(vtrFRWBY.contains(cl_dat.M_strUSRCD_pbst))
						{
							cmbFRWTO.setEnabled(true);
							btnFRWD.setEnabled(true);
							if(!flgFRWIN)
							setMSG("You can Forward the request now ..",'E');
							cmbFRWTO.requestFocus();
						}
						else
						{
							cmbFRWTO.setEnabled(false);
							btnFRWD.setEnabled(false);
						}
					}
				}
			
		}
		catch(Exception L_E)
		{
			flgFRWIN = false;
			setMSG(L_E,"exeSAVE");
			cl_dat.M_btnSAVE_pbst.setEnabled(true);
			setCursor(cl_dat.M_curDFSTS_pbst);
		}
		flgFRWIN = false;
		strINDDT = cl_dat.M_strLOGDT_pbst;
	}
class inpVRFY extends InputVerifier 
{
	public boolean verify(JComponent input) 
	{
		if(input == txtDPTCD)
		{
			if(txtDPTCD.getText().trim().length() > 0)
			{
				if(!vldDPTCD(txtDPTCD.getText().trim()))
				{
					setMSG("Please Select the Department code..",'E');
					txtDPTCD.requestFocus();
					return false;
				}
			}
				
		}
		return true;	
	}
}
private boolean genINDNO()
{
		try
		{
			String L_strINDNO  = "",  L_CODCD = "", L_CCSVL = "",L_CHP01 ="";
			M_strSQLQRY = "Select CMT_CODCD,CMT_CCSVL,CMT_CHP01 from CO_CDTRN ";
			M_strSQLQRY += " where CMT_CGMTP = 'D"+cl_dat.M_strCMPCD_pbst+"' and CMT_CGSTP = 'MM"+M_strSBSCD.substring(0,2)+"IND' and ";
			M_strSQLQRY += " CMT_CODCD = '" + cl_dat.M_strFNNYR_pbst.substring(3) + strINDTR_fn + "'";
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
				L_strINDNO += "0";
			
			L_CCSVL = L_strINDNO + L_CCSVL;
			strSRLNO = L_CCSVL;
			L_strINDNO = L_CODCD + L_CCSVL;
			txtINDNO.setText(L_strINDNO);
			txtAMDNO.setText("00");
			return true;
		}catch(Exception L_EX){
			setMSG(L_EX,"genINDNO");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}
	}
boolean insRMMST(String P_strREMTP,String P_strREMDS)
{
	String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
	try
	{
		pstmINSRMK.setString(1,cl_dat.M_strCMPCD_pbst);
		pstmINSRMK.setString(2,M_strSBSCD);
		pstmINSRMK.setString(3,strSTRTP);
		pstmINSRMK.setString(4,strTRNTP);
		pstmINSRMK.setString(5,strTRNTP);
		pstmINSRMK.setString(6,P_strREMTP);
		pstmINSRMK.setString(7,txtINDNO.getText().trim());
		pstmINSRMK.setString(8,P_strREMDS);
		pstmINSRMK.setString(9," ");
		pstmINSRMK.setString(10,"0");
		pstmINSRMK.setString(11,cl_dat.M_strUSRCD_pbst);
		pstmINSRMK.setDate(12,Date.valueOf(L_strDATE));
		pstmINSRMK.executeUpdate();
		pstmINSRMK.clearParameters();
		return true;
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"insRMMST");
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
}
boolean updRMMST(String P_strREMTP,String P_strREMDS,String P_strSTSFL)
{
	String L_strDATE = cl_dat.M_strLOGDT_pbst.substring(6,10) + "-" + cl_dat.M_strLOGDT_pbst.substring(3,5) + "-" + cl_dat.M_strLOGDT_pbst.substring(0,2);
	try
	{
//"UPDATE MM_RMMST set RM_REMDS = ?,RM_STSFL = ?,RM_TRNFL= ?,RM_LUSBY= ?,RM_LUPDT = ? where RM_MMSBS = ? and RM_STRTP = ? and RM_DOCTP = ? and RM_TRNTP = ? and RM_REMTP = ? and RM_DOCNO = ?");

		if(P_strREMTP.equals("IND"))
			pstmUPDRMK.setString(1,txtINDRM.getText().trim());
		else
			pstmUPDRMK.setString(1,txtOTHRM.getText().trim());
		pstmUPDRMK.setString(2,P_strSTSFL);
		pstmUPDRMK.setString(3,"0");
		pstmUPDRMK.setString(4,cl_dat.M_strUSRCD_pbst);
		pstmUPDRMK.setDate(5,Date.valueOf(L_strDATE));
		pstmUPDRMK.setString(6,cl_dat.M_strCMPCD_pbst);
		pstmUPDRMK.setString(7,M_strSBSCD);
		pstmUPDRMK.setString(8,strSTRTP);
		pstmUPDRMK.setString(9,strTRNTP);
		pstmUPDRMK.setString(10,strTRNTP);
		pstmUPDRMK.setString(11,P_strREMTP);
		pstmUPDRMK.setString(12,txtINDNO.getText().trim());
		pstmUPDRMK.executeUpdate();
		pstmUPDRMK.clearParameters();
		return true;
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"updRMMST");
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
}
@SuppressWarnings("unchecked") private void getITMDTL(int L_intROWID)
{
	try
	{
		int L_intROWCT =0;
	
		int L_intSTKIN =0;
		int L_intSTKOR =0;
		int L_intSTKHD =0;
		String L_PPORT ="0.00";
		String L_strEXPDT;
		String L_strPORBY;
		String L_strMATCD = tblINDTL.getValueAt(L_intROWID,TBL_MATCD).toString();
		if(L_strMATCD.length() !=10)
			return;
		M_strSQLQRY = "SELECT * from MM_STMST where ST_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND ST_MMSBS ='"+M_strSBSCD+"'";
			M_strSQLQRY += " AND ST_STRTP ='"+strSTRTP +"'";
			M_strSQLQRY += " AND SUBSTRING(ST_MATCD,1,9) ='"+L_strMATCD.substring(0,9)+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			txtRORLV.setText("");txtRORQT.setText("");
			txtPPORT.setText("");txtQTIND.setText("");
			txtQTORD.setText("");txtQTHND.setText("");
		//	txtUOMCD.setText("");
			txtQTHND.setText("");
			lblMATCD.setText("");
			chkSTKIT.setSelected(false);
			lblMATCD.setText(L_strMATCD);
			float L_fltAPRVL =0;
			if(M_rstRSSET !=null)
			while(M_rstRSSET.next())
			{
				if(nvlSTRVL(M_rstRSSET.getString("ST_MATCD"),"").equals(L_strMATCD))
				{
					txtRORLV.setText(nvlSTRVL(M_rstRSSET.getString("ST_RORLV"),""));
					txtRORQT.setText(nvlSTRVL(M_rstRSSET.getString("ST_RORQT"),""));
					L_PPORT = nvlSTRVL(M_rstRSSET.getString("ST_PPOVL"),"0");
					hstITMVL.put(L_strMATCD,Float.valueOf(L_PPORT));
					txtPPORT.setText(L_PPORT);
					if(tblINDTL.getValueAt(L_intROWID,TBL_INDQT).toString().length() >0)
						txtITVAL.setText(String.valueOf(Float.parseFloat(L_PPORT)*Float.parseFloat(tblINDTL.getValueAt(L_intROWID,TBL_INDQT).toString())));
					if(nvlSTRVL(M_rstRSSET.getString("ST_STKFL"),"").equals("Y"))
						chkSTKIT.setSelected(true);
					else
						chkSTKIT.setSelected(false);
				}
				L_intSTKIN += M_rstRSSET.getInt("ST_STKIN");
				L_intSTKOR += M_rstRSSET.getInt("ST_STKOR");
				L_intSTKHD += M_rstRSSET.getInt("ST_STKQT");
				txtQTIND.setText(String.valueOf(L_intSTKIN));
				txtQTORD.setText(String.valueOf(L_intSTKOR));
				txtQTHND.setText(String.valueOf(L_intSTKHD));
			}
			tblVNDTL.clrTABLE();
			L_intROWCT =0;
			M_strSQLQRY = "SELECT CTP_PRTCD,PT_PRTNM from CO_CTPTR,CO_PTMST where ";
			M_strSQLQRY += " CTP_PRTTP ='S' AND PT_PRTTP ='S' AND CTP_PRTCD = PT_PRTCD AND CTP_MATCD ='"+L_strMATCD+"'";
			M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
			if(M_rstRSSET !=null)
				while(M_rstRSSET.next())
				{
					tblVNDTL.setValueAt(M_rstRSSET.getString("CTP_PRTCD"),L_intROWCT,1);
					tblVNDTL.setValueAt(M_rstRSSET.getString("PT_PRTNM"),L_intROWCT,2);
					L_intROWCT++;
				}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"getITMDTL");
		setCursor(cl_dat.M_curDFSTS_pbst);
	}
			
}
private boolean vldMATCD(int L_intROWID,String P_strMATCD)
{
	try
	{
		int L_intROWCT =0;
		int L_intLEDTM =0;
		int L_intILDTM =0,L_intELDTM=0;
		String L_strEXPDT,L_strPORBY;
		String L_strMATCD = tblINDTL.getValueAt(L_intROWID,TBL_MATCD).toString();
		strINDDT = txtINDDT.getText().trim();
		setMSG("",'N');    
		if(L_strMATCD == null)
			L_strMATCD = P_strMATCD;
		if(L_strMATCD.length() != 10)
			return true;
        if((L_strMATCD.substring(9,10).equals("8"))||(L_strMATCD.substring(9,10).equals("9")))
        {
            setMSG("Indent Can not be raised for reconditioned items..",'E');
            return false;
        }
		for(int i=0;i<L_intROWID-1;i++)
		{
			if(tblINDTL.getValueAt(i,TBL_MATCD).toString().trim().length() >0)
			if(tblINDTL.getValueAt(i,TBL_MATCD).toString().trim().equals(L_strMATCD.trim()))
			{
				setMSG("Duplicate entry ..",'E');
				return false;
			}
		}
		/*if(!flgMSCLK)
		if(hstITMDT.containsKey(L_strMATCD))
		{
			setMSG("Duplicate entry ..",'E');
				return false;
		}
		flgMSCLK = false;*/
		/*M_strSQLQRY = "select CT_MATCD,CT_MATDS,CT_UOMCD,ST_STKQT,ST_LOCCD,ST_STKFL,CT_PKGTP from MM_STMST,CO_CTMST";
		M_strSQLQRY += " where CT_CODTP ='CD' AND ST_MATCD = CT_MATCD and ";
		M_strSQLQRY += " ST_STRTP = '" + M_strSBSCD.substring(2,4) + "' AND ";
		M_strSQLQRY += " ST_MATCD = '" + P_strMATCD + "'";*/
			
		txtUOMCD.setText("");
		M_strSQLQRY = "SELECT CT_CODTP,CT_MATDS,CT_UOMCD,CT_PKGTP,CT_PKGQT,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,CT_INSFL,CT_TCFFL,CT_PPORT from CO_CTMST where CT_CODTP ='CD' and CT_MATCD ='"+L_strMATCD+"' AND isnull(CT_STSFL,'') NOT IN('0','X','9')";
		M_strSQLQRY += " UNION SELECT CT_CODTP,CT_MATDS,CT_UOMCD,CT_PKGTP,CT_PKGQT,CT_ILDTM,CT_ELDTM,CT_IILTM,CT_IELTM,CT_INSFL,CT_TCFFL,CT_PPORT from CO_CTMST where CT_CODTP ='SS' and CT_MATCD ='"+L_strMATCD.substring(0,6)+"000A"+"' AND isnull(CT_STSFL,'') NOT IN ('0','X','9')";
		M_strSQLQRY += " ORDER BY CT_CODTP";
		M_rstRSSET = cl_dat.exeSQLQRY1(M_strSQLQRY);
		if(M_rstRSSET !=null)
		{
			int L_intRECCT =0;
			while(M_rstRSSET.next())
			{
				if(M_rstRSSET.getString("CT_CODTP").equals("CD"))
				{
				    L_intRECCT++;
					txtPKGTP.setText(M_rstRSSET.getString("CT_PKGTP"));
					txtPKGQT.setText(M_rstRSSET.getString("CT_PKGQT"));
					txtUOMCD.setText(M_rstRSSET.getString("CT_UOMCD"));
					tblINDTL.setValueAt(M_rstRSSET.getString("CT_MATDS"),L_intROWID,TBL_MATDS);
					tblINDTL.setValueAt(nvlSTRVL(M_rstRSSET.getString("CT_PPORT"),"0"),L_intROWID,TBL_PPORT);
					if(nvlSTRVL(M_rstRSSET.getString("CT_TCFFL"),"").equals("Y"))
						tblINDTL.setValueAt(Boolean.TRUE,L_intROWID,TBL_TCFFL);
					if(nvlSTRVL(M_rstRSSET.getString("CT_INSFL"),"").equals("Y"))
						tblINDTL.setValueAt(Boolean.TRUE,L_intROWID,TBL_INSFL);
				
					if((L_strMATCD.substring(9).equals("2"))||(L_strMATCD.substring(9).equals("6")))
				    {
					   L_intILDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),"0"));
					   L_intELDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),"0"));		   
					}
					else
					{
						L_intILDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0"));
						L_intELDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0"));		
					}
				}
				if(L_intRECCT ==0)
    			{
    				setMSG("Invalid Item code..",'E');
    				return false;
    			}
				if(M_rstRSSET.getString("CT_CODTP").equals("SS"))
				{
					if(L_intILDTM == 0)
					{
						if((L_strMATCD.substring(9).equals("2"))||(L_strMATCD.substring(9).equals("6")))
						{
							L_intILDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_IILTM"),"0"));
							L_intELDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_IELTM"),"0"));		   
							if(L_intILDTM == 0)
								L_intILDTM =30;			// Internal Lead Time for imported
							if(L_intELDTM == 0)
								L_intELDTM =90;			// External Lead Time for imported
						}
						else
						{
							L_intILDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_ILDTM"),"0"));
							L_intELDTM = Integer.parseInt(nvlSTRVL(M_rstRSSET.getString("CT_ELDTM"),"0"));		   
							if(L_intILDTM == 0)
								L_intILDTM =21;			// Internal Lead Time for indigenous
							if(L_intELDTM == 0)
								L_intELDTM =30;			// External Lead Time for indigenous
						}
					}
					L_intRECCT++;
				}
			//	System.out.println(L_strMATCD +" "+String.valueof(L_intILDTM)+","+String.valueof(L_intELDTM));
				
			}
			System.out.println("L_intRECCT "+L_intRECCT);
			if(L_intRECCT ==0)
			{
				setMSG("Invalid Item code..",'E');
				return false;
			}
			L_intLEDTM = L_intILDTM +L_intELDTM;
			M_calLOCAL.setTime(M_fmtLCDAT.parse(strINDDT));
			M_calLOCAL.add(java.util.Calendar.DATE,L_intLEDTM);
			L_strEXPDT = M_fmtLCDAT.format(M_calLOCAL.getTime());
			tblINDTL.setValueAt(L_strEXPDT,L_intROWID,TBL_EXPDT);
			//strREQDT = L_strEXPDT;
			tblINDTL.setValueAt(L_strEXPDT,L_intROWID,TBL_REQDT);
			
			M_calLOCAL.setTime(M_fmtLCDAT.parse(strINDDT));
			M_calLOCAL.add(java.util.Calendar.DATE,L_intILDTM);
			L_strPORBY = M_fmtLCDAT.format(M_calLOCAL.getTime());
			tblINDTL.setValueAt(L_strPORBY,L_intROWID,TBL_PORBY);
	
			M_rstRSSET.close();
		}
		else
		{
			setMSG("Invalid Item code..",'E');
			return false;
		}
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"vldMATCD");
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
return true;	
}
private class mm_teindTBLINVFR extends TableInputVerifier
{
		public boolean verify(int P_intROWID,int P_intCOLID)
		{
			try
			{
				
				if(P_intCOLID==TBL_MATCD)
				{
					if(tblINDTL.getValueAt(P_intROWID,TBL_MATCD).toString().length() ==0)
						return true;
					if(strMATCD ==null)
					{
						strTEMP = tblINDTL.getValueAt(P_intROWID,TBL_MATCD).toString();
						strMATCD = strTEMP;
					}
					else if(strMATCD.length() == 0)
					{
						strTEMP = tblINDTL.getValueAt(P_intROWID,TBL_MATCD).toString();
						strMATCD = strTEMP;
					}
					//strTEMP = tblINDTL.getValueAt(P_intROWID,TBL_MATCD).toString();
					if(strMATCD.length()>0)
						if(!vldMATCD(P_intROWID,strMATCD))
						{
							return false;
						}
						/*else{
							getITMDTL(P_intROWID);
						}*/
				}
				else if(P_intCOLID==TBL_INDQT)
				{
					strTEMP = tblINDTL.getValueAt(P_intROWID,TBL_INDQT).toString();
					if(!strINDST.equals("4"))
						tblINDTL.setValueAt("0",P_intROWID,TBL_AUTQT);
					if(!cl_dat.M_cmbOPTN_pbst.getSelectedItem().equals(cl_dat.M_OPAUT_pbst))
						tblINDTL.setValueAt("0",P_intROWID,TBL_AUTQT);
					if(!txtAMDNO.getText().equals("00"))
					if(Float.parseFloat(strTEMP)< ((Float)hstITMDT.get(tblINDTL.getValueAt(P_intROWID,TBL_MATCD))).floatValue())
					{
						setMSG("Requested qty. can not be less than the previous qty..",'E');
						return false;
					}
					getITMDTL(P_intROWID);
					/*if(hstITMVL.get(tblINDTL.getValueAt(P_intROWID,TBL_MATCD).toString()) == null)
					{
					}	
					else
					{
						fltINDVL =Float.parseFloat(strTEMP)*Float.parseFloat(hstITMVL.get(tblINDTL.getValueAt(P_intROWID,TBL_MATCD).toString()).toString());
						tblINDTL.setValueAt(String.valueOf(fltINDVL),P_intROWID,TBL_INDVL);
					}*/
					fltINDVL =Float.parseFloat(strTEMP)*Float.parseFloat(tblINDTL.getValueAt(P_intROWID,TBL_PPORT).toString());
					tblINDTL.setValueAt(String.valueOf(fltINDVL),P_intROWID,TBL_INDVL);

					float L_fltAPRVL =0;
					for(int k =0;k<tblINDTL.getRowCount();k++)
						if(tblINDTL.getValueAt(k,TBL_INDVL).toString().length() >0)
							L_fltAPRVL += Float.parseFloat(tblINDTL.getValueAt(k,TBL_INDVL).toString());
					txtINDVL.setText(String.valueOf(L_fltAPRVL));
		
					return true;
				}
				else if(P_intCOLID==TBL_REQDT)
				{
					strTEMP = tblINDTL.getValueAt(P_intROWID,TBL_REQDT).toString();
					if(cmbURGTG !=null)
					if(!cmbURGTG.getSelectedItem().toString().substring(0,1).equals("N"))
					{
						if(M_fmtLCDAT.parse(strTEMP).compareTo(M_fmtLCDAT.parse(txtINDDT.getText().trim()))<0)
						{
							setMSG("Date can not be less than indent date..",'E');
							return false;
						}
						 else if(M_fmtLCDAT.parse(strTEMP).compareTo(M_fmtLCDAT.parse(cl_dat.M_strLOGDT_pbst))<0)
						{
							// if(flgAUTH)
							 if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
							 {
								setMSG("Requested Date can not be less than authorisation date..",'E');
								return false;
							 }
						}
						else
						{	
						
							if(tblINDTL.getValueAt(P_intROWID,TBL_EXPDT).toString() !=null)
							if(M_fmtLCDAT.parse(strTEMP).compareTo(M_fmtLCDAT.parse(tblINDTL.getValueAt(P_intROWID,TBL_EXPDT).toString()))>0)
							{
								setMSG("Date can not be greater than expected date..",'E');
								return false;
							}
							else
							return true;
						}
					}
					else
					{
						if(tblINDTL.getValueAt(P_intROWID,TBL_EXPDT).toString() !=null)
						if(M_fmtLCDAT.parse(strTEMP).compareTo(M_fmtLCDAT.parse(tblINDTL.getValueAt(P_intROWID,TBL_EXPDT).toString()))!=0)
						{
							setMSG("Requested Date can not be modified for Normal Indents..",'E');
							return false;
						}
						else
							return true;
					}
					
				}
			}
			catch(Exception L_E)
			{
				setMSG(L_E,"table verify");
				setCursor(cl_dat.M_curDFSTS_pbst);
			}
			return true;
		}
}
boolean autINDNT()
{
	try
	{
		cl_dat.M_flgLCUPD_pbst = true;
		//String L_strSTSFL ="" ;
		if(cmbAUTST.getSelectedIndex() ==0)
		{
			setMSG("Please select the Authorisation status.. ",'E'); 
			return false;
		}
		else
			strINDST = cmbAUTST.getSelectedItem().toString().substring(0,1);
		M_strSQLQRY = "UPDATE MM_INMST SET "+
					  " IN_AUTBY ='"+cl_dat.M_strUSRCD_pbst+"',"+
					  //"IN_FRWTO ='"+L_strFRWTO+"',"+
					  "IN_AUTDT ='"+ M_fmtDBDTM.format(M_fmtLCDTM.parse(cl_dat.M_txtCLKDT_pbst.getText().trim()+ " "+cl_dat.M_txtCLKTM_pbst.getText().trim()))+"',";
		M_strSQLQRY += getUSGDTL("IN",'U',strINDST); // status flag 2 is forwarded
		M_strSQLQRY += " WHERE IN_CMPCD='"+cl_dat.M_strCMPCD_pbst+"' AND IN_MMSBS ='"+M_strSBSCD+"' AND IN_STRTP ='"+strSTRTP +"'";
		M_strSQLQRY += " AND IN_INDNO = '"+txtINDNO.getText().trim() +"'";
		M_strSQLQRY += " AND IN_AMDNO = '"+strPRAMD +"'";
		M_strSQLQRY += " AND isnull(IN_STSFL,'') in('2','3')";
		cl_dat.exeSQLUPD(M_strSQLQRY,"setLCLUPD");
	}
	catch(Exception L_E)
	{
		setMSG(L_E,"forward ");
		setCursor(cl_dat.M_curDFSTS_pbst);
		return false;
	}
	return true;
}
		
boolean vldDATA()
{
	if(cmbURGTG.getSelectedIndex() ==0)
	{
		setMSG("Please select the urgency tag..",'E');
		return false;
	}
	fltINDVL =0F;
	/*if(cmbFRWTO.getSelectedIndex() ==0)
	{
		setMSG("Please select the Authorising person..",'E');
		cmbFRWTO.requestFocus();
		return false;
	}*/
	for(int i=0;i<tblINDTL.getRowCount();i++)
	{
		if(tblINDTL.getValueAt(i,TBL_CHKFL).toString().equals("true"))
		{
			if(tblINDTL.getValueAt(i,TBL_MATCD).toString().length() == 0)
			{
				setMSG("Item code can not be empty..",'E');
				return false;
			}
			else if(tblINDTL.getValueAt(i,TBL_INDQT).toString().length() == 0)
			{
				setMSG("Indent qty can not be empty..",'E');
				return false;
			}
			else if(tblINDTL.getValueAt(i,TBL_AUTQT).toString().length() == 0)
			{
				setMSG("Authorised qty can not be empty..",'E');
				return false;
			}
			else if(tblINDTL.getValueAt(i,TBL_REQDT).toString().length() == 0)
			{
				setMSG("Requested date can not be empty..",'E');
				return false;
			}
			else if(tblINDTL.getValueAt(i,TBL_EXPDT).toString().length() == 0)
			{
				setMSG("Expected date can not be empty..",'E');
				return false;
			}
		strTEMP = tblINDTL.getValueAt(i,TBL_INDQT).toString();
		/*if(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()) == null)
		{
			fltINDVL +=0;
		}
		else
			fltINDVL +=Float.parseFloat(strTEMP)*Float.parseFloat(hstITMVL.get(tblINDTL.getValueAt(i,TBL_MATCD).toString()).toString());
		txtINDVL.setText(String.valueOf(fltINDVL));*/
		}
	}
	//if(flgAUTH)
	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPAUT_pbst))
	{
		if(strINDST.equals("0"))
		{
			setMSG("Indent is not Forwarded for authorisation..",'E');
			return false;
		}
		/*if(strINDST.equals("4"))
		{
			setMSG("Already authorised..",'E');
			return false;
		}*/
	}
	if(cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPADD_pbst)||cl_dat.M_cmbOPTN_pbst.getSelectedItem().toString().equals(cl_dat.M_OPMOD_pbst))
	{
		if(!flgFRWIN)
		if(txtDPTCD.getText().trim().length() ==0)
		{
			setMSG("Department code can not be blank..",'E');
			txtDPTCD.requestFocus();
			return false;
		}
	}
	return true;
}
private boolean vldDPTCD(String P_strDPTCD)
{
		try
		{
			ResultSet L_rstRSSET;
			M_strSQLQRY = "Select CMT_CODCD,CMT_CODDS from CO_CDTRN";
			M_strSQLQRY += " where CMT_CGMTP = 'SYS'";
			M_strSQLQRY += " and CMT_CGSTP = 'COXXDPT'";
			M_strSQLQRY += " and CMT_CODCD = '" + P_strDPTCD + "'";
            L_rstRSSET = cl_dat.exeSQLQRY2(M_strSQLQRY);
			
			if(L_rstRSSET.next()){
				txtDPTDS.setText(L_rstRSSET.getString("CMT_CODDS"));
				return true;
			}
			txtDPTDS.setText("");
			txtDPTCD.requestFocus();
			setMSG("Invalid Department. Press F1 for help",'E');
				
			if(L_rstRSSET != null)
				L_rstRSSET.close();			
		}catch(Exception L_EX){
			setMSG(L_EX,"vldDPTCD");
			setCursor(cl_dat.M_curDFSTS_pbst);
			return false;
		}	
		return false;
	}
}